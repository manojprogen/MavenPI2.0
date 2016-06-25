/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.portal.viewer;

import com.google.common.collect.Iterables;
import com.google.gson.Gson;
import com.progen.charts.GraphProperty;
import com.progen.charts.ProgenChartDisplay;
import com.progen.dashboard.DashboardTableColorGroupHelper;
import com.progen.portal.*;
import com.progen.portal.portlet.PortletDesigner;
import com.progen.report.PbReportCollection;
import com.progen.reportdesigner.db.DashboardTemplateDAO;
import com.progen.reportdesigner.db.ReportTemplateDAO;
import com.progen.rulesHelp.RulesHelpDAO;
import com.progen.scheduler.ReportSchedule;
import com.progen.scheduler.ReportSchedulePreferences;
import com.progen.scheduler.SchedulerBD;
import com.progen.scheduler.db.SchedulerDAO;
import com.progen.userlayer.action.GenerateDragAndDrophtml;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
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
import prg.reportscheduler.ReportSchedulerJob;
import utils.db.ProgenParam;

/**
 *
 * @author Saurabh
 */
public class PortalViewerAction extends LookupDispatchAction {

    public static Logger logger = Logger.getLogger(PortalViewerAction.class);
    private final static String SUCCESS = "success";
//    PbReportViewerDAO reportViewerDAO = new PbReportViewerDAO();
//    ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();
    //ReportTemplateBD reportBD = new ReportTemplateBD();
    //PortalViewerBD portalViewerbd = new PortalViewerBD();
//    DashboardTemplateDAO dashboardTemplateDAO = new DashboardTemplateDAO();
//    DashboardTemplateBD dashboardTempBD = new DashboardTemplateBD();
    ProgenChartDisplay pchart = null;
    PbReportCollection collect = null;
    String resetGpType = "";
    boolean flag = false;
    int count = 0;
    String sortTypeVal = "";

    protected Map getKeyMethodMap() {
        Map map = new HashMap();
        map.put("viewPortal", "viewPortal");
        map.put("viewPortalTab", "viewPortalTab");
        map.put("goToPortletGraph", "goToPortletGraph");
        map.put("goToPortletTable", "goToPortletTable");
        map.put("savePortlet", "savePortlet");
        map.put("viewPortlet", "viewPortlet");
        map.put("viewPortletGadgets", "viewPortletGadgets");
        map.put("savePortletOrder", "savePortletOrder");
        map.put("deletePortlet", "deletePortlet");
        map.put("cancelPortlet", "cancelPortlet");
        map.put("buildPortletKPIS", "buildPortletKPIS");
        map.put("getPortletKpis", "getPortletKpis");
        map.put("dispPortletKPITable", "dispPortletKPITable");
        map.put("goToPortletKPIGraph", "goToPortletKPIGraph");
        map.put("goToPortletKPITable", "goToPortletKPITable");
        map.put("displayKPIGraphRegion", "displayKPIGraphRegion");
        map.put("displayKPIGraph", "displayKPIGraph");
        map.put("updatePortletName", "updatePortletName");
        map.put("deletePortals", "deletePortals");//added by santhosh.k on 06-03-2010 for deleting portals
        map.put("updatePortalTimeDetails", "updatePortalTimeDetails");
        map.put("viewPortals", "viewPortals");
        map.put("setSortOrder", "setSortOrder");
        map.put("goToPortletCreation", "goToPortletCreation");
        map.put("deletePortlets", "deletePortlets");
        map.put("portletPreview", "portletPreview");
        map.put("portletAssign", "portletAssign");
        map.put("savePortletProperties", "savePortletProperties");
        map.put("getPortletProperty", "getPortletProperty");
        map.put("saveXmalOfPortlet", "saveXmalOfPortlet");
        map.put("extendTablePortlet", "extendTablePortlet");
        map.put("getTimePeriodDetails", "getTimePeriodDetails");
        map.put("getKpiTargetDeviation", "getKpiTargetDeviation");
        map.put("DrillToReport", "DrillToReport");
        map.put("setRelatedPortlet", "setRelatedPortlet");
        map.put("getRelatedPortlets", "getRelatedPortlets");
        map.put("saveRelatedPortlets", "saveRelatedPortlets");
        map.put("resetTimePeriodDetails", "resetTimePeriodDetails");
        map.put("applySortOnTable", "applySortOnTable");
        map.put("saveTargetValue", "saveTargetValue");
        map.put("applySortOnTable", "applySortOnTable");
        map.put("printPortal", "printPortal");
        map.put("getMinMaxAvgValueOfElement", "getMinMaxAvgValueOfElement");
        map.put("savePortalColors", "savePortalColors");
        map.put("resetTablePortlet", "resetTablePortlet");
        map.put("runPortalScheduler", "runPortalScheduler");
        map.put("savePortalScheduler", "savePortalScheduler");
        map.put("changeGraphMeasure", "changeGraphMeasure");
        map.put("editTimePeriodDetails", "editTimePeriodDetails");
        map.put("setTargetVal", "setTargetVal");
        map.put("resetGraphPortlet", "resetGraphPortlet");
        map.put("deleteEmptyPortlet", "deleteEmptyPortlet");
        map.put("deleteSelectedPortlet", "deleteSelectedPortlet");
        map.put("getDimensionsForFilter", "getDimensionsForFilter");
        map.put("applyDimensionFilter", "applyDimensionFilter");
        map.put("getDimensionBasedOnRule", "getDimensionBasedOnRule");
        map.put("saveFilterInPortal", "saveFilterInPortal");
        map.put("getFiltervals", "getFiltervals");
        map.put("resetGlobalFilter", "resetGlobalFilter");
        map.put("getPortalName", "getPortalName");
        return map;
    }

    //old code to delete start
//    public ActionForward viewPortal(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
//        HttpSession session = request.getSession(false);
//        String portalId = "";
//        HashMap map = null;
//        Container container = null;
//        if (session != null) {
//            try {
//                if (request.getParameter("PORTALID") != null) {
//                    portalId = request.getParameter("PORTALID");
//                } else if (request.getAttribute("PORTALID") != null) {
//                    portalId = String.valueOf(request.getAttribute("PORTALID"));
//                }
////                if (session.getAttribute("PROGENTABLES") != null) {
////                    map = (HashMap) session.getAttribute("PROGENTABLES");
////                    if (map.get(portletId) != null) {
////                        container = (Container) map.get(portletId);
////                    } else {
////                        container = new Container();
////                    }
////                } else {
////                    container = new Container();
////                }
//                String Url = request.getRequestURL().toString();
//                Url = Url + "?portalBy=viewPortal&PORTALID=" + portalId;
//                request.setAttribute("protalurl", Url);
//                portalViewerbd.preparePortal(portalId, request, response, session);
//                request.setAttribute("PORTALID", portalId);
//                return mapping.findForward("showViewPortal");
//            } catch (Exception exp) {
//                logger.error("Exception:",exp);
//                return mapping.findForward("exceptionPage");
//            }
//        } else {
//            return mapping.findForward("sessionExpired");
//        }
//    }
    //old code to delete end
//portals new code : for opening all portal tabs .
    public ActionForward viewPortals(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession(false);
        try {
            if (session != null) {
                String Url = request.getRequestURL().toString();
                Url = Url + "?portalBy=viewPortals";
                request.setAttribute("protalurl", Url);
                return mapping.findForward("showViewPortal");
            } else {
                return mapping.findForward("sessionExpired");

            }

        } catch (Exception exception) {
            logger.error("Exception:", exception);
            return mapping.findForward("exceptionPage");
        }


    }

    //is called when clicks on a particular portal tab
    public ActionForward viewPortalTab(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession(false);
        int portalViewCount = 0;
        if (session.getAttribute("PORTALCOUNT") == null) {
            session.setAttribute("PORTALCOUNT", portalViewCount);
        } else {
            portalViewCount++;
            session.setAttribute("PORTALCOUNT", portalViewCount);
        }
        String tabId = "";
        String tabName = "";
        String currDate = "";
        String periodType = "";
        if (session != null) {
            try {
                if (request.getParameter("TABID") != null) {
                    tabId = request.getParameter("TABID");
                    tabName = request.getParameter("TABNAME");
                } else if (request.getAttribute("TABID") != null) {
                    tabId = String.valueOf(request.getAttribute("TABID"));
                    tabName = String.valueOf(request.getAttribute("TABNAME"));
                }

                if (request.getParameter("currDate") != null) {
                    currDate = request.getParameter("currDate");
                }
                if (request.getParameter("periodType") != null) {
                    periodType = request.getParameter("periodType");
                }
                if (session.getAttribute("dateCurr") != null) {
                    if (request.getParameter("currDate") != null) {
                        if (request.getParameter("currDate").equals(session.getAttribute("dateCurr"))) {
                            currDate = String.valueOf(session.getAttribute("dateCurr"));
                        } else {
                            currDate = request.getParameter("currDate");
                        }
                    } else {
                        currDate = String.valueOf(session.getAttribute("dateCurr"));
                    }
                }
                if (session.getAttribute("periodType") != null) {
                    if (request.getParameter("periodType") != null) {
                        if (request.getParameter("periodType").equals(session.getAttribute("periodType"))) {
                            periodType = String.valueOf(session.getAttribute("periodType"));
                        } else {
                            periodType = request.getParameter("periodType");
                        }
                    } else {
                        periodType = String.valueOf(session.getAttribute("periodType"));
                    }
                }
                String value = "";
                String valu = "";
                String mont = "";
                String CurrValue = "";

                request.setAttribute("CURRDATE", currDate);
                value = currDate;
                String ddformat = null;
                if (session.getAttribute("dateFormat") != null) {
                    ddformat = session.getAttribute("dateFormat").toString();
                }
                if (ddformat == null && !currDate.equalsIgnoreCase("")) {
                    int slashval = value.indexOf("/");
                    int slashLast = value.lastIndexOf("/");
                    valu = value.substring(0, slashval);
                    mont = value.substring(slashval + 1, slashLast + 1);
                    CurrValue = mont.concat(valu).concat(value.substring(slashLast));
                    currDate = CurrValue;
                } else if (ddformat != null && ddformat.equalsIgnoreCase("dd/mm/yy") && !currDate.equalsIgnoreCase("")) {
                    int slashval = value.indexOf("/");
                    int slashLast = value.lastIndexOf("/");
                    valu = value.substring(0, slashval);
                    mont = value.substring(slashval + 1, slashLast + 1);
                    CurrValue = mont.concat(valu).concat(value.substring(slashLast));
                    currDate = CurrValue;
                }
                PortalViewerBD bd = new PortalViewerBD();
//                bd.preparePortalTab(tabId, tabName, currDate, request);
                bd.preparePortalTab(Integer.parseInt(tabId), currDate, request, periodType);
                request.setAttribute("TABID", tabId);
                request.setAttribute("TABNAME", tabName);
                request.setAttribute("PERIODTYPE", periodType);
                return mapping.findForward("showViewPortalTab");
            } catch (Exception exp) {
                logger.error("Exception:", exp);
                return mapping.findForward("exceptionPage");

            }
        } else {
            return mapping.findForward("sessionExpired");
        }
    }

    //is called  to view portlet under each portals
    public ActionForward viewPortlet(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession(false);
        ServletContext context = this.getServlet().getServletContext();
        PrintWriter out = response.getWriter();
        boolean isFxCharts = Boolean.parseBoolean(context.getInitParameter("isFxCharts"));

        String portletId = "";
        String UserId = "";
        String REP = "";
        String CEP = "";
        String perBy = "";
        String gpType = "";
        String portalTabId = "";
        String currDate = "";
        String periodType = "";
        String dateCheck = "";
        String CBOARPElementDetail = "";
//        CBOARP111656=WEST ZONE
        String tempDirllVal = "";
        for (Enumeration e = request.getParameterNames(); e.hasMoreElements();) {
            tempDirllVal = e.nextElement().toString();
            if (tempDirllVal.contains("CBOARP")) {
//       tempDirllVal=tempDirllVal;
                break;
            }
        }
        HashMap map = null;
        Container container = null;
        PortalViewerBD portalViewerbd = new PortalViewerBD();
        if (session != null) {
            try {
                if (request.getParameter("PORTLETID") != null) {
                    portletId = request.getParameter("PORTLETID");
                    REP = request.getParameter("REP");
                    CEP = request.getParameter("CEP");
                    perBy = request.getParameter("perBy");
                    gpType = request.getParameter("gpType");
                    portalTabId = request.getParameter("portalTabId");
                    currDate = request.getParameter("currDate");
                    periodType = request.getParameter("periodType");
                    dateCheck = request.getParameter("dateCheck");
                    if (request.getParameter(tempDirllVal) != null && request.getParameter("fromModule") != null) {
                        CBOARPElementDetail = request.getParameter(tempDirllVal);
                    }

                } else if (request.getAttribute("PORTLETID") != null) {
                    portletId = String.valueOf(request.getAttribute("PORTLETID"));
                    REP = request.getParameter("REP");
                    CEP = request.getParameter("CEP");
                    perBy = request.getParameter("perBy");
                    dateCheck = request.getParameter("dateCheck");
                    gpType = request.getParameter("gpType");
                    portalTabId = request.getParameter("portalTabId");
                    currDate = request.getParameter("currDate");
                    periodType = request.getParameter("periodType");
                    if (request.getParameter(tempDirllVal) != null && request.getParameter("fromModule") != null) {
                        CBOARPElementDetail = request.getParameter(tempDirllVal);
                    }
                }
                if (session.getAttribute("USERID") != null) {
                    UserId = String.valueOf(session.getAttribute("USERID"));
                }
                if (flag == true) {
                    gpType = resetGpType;
                    flag = false;
                }
                String value = "";
                String valu = "";
                String mont = "";
                String CurrValue = "";
                value = currDate;
                String ddformat = null;
                if (session.getAttribute("dateFormat") != null) {
                    ddformat = session.getAttribute("dateFormat").toString();
                }
                if (CBOARPElementDetail.equalsIgnoreCase("") && (dateCheck.equalsIgnoreCase("updatePortletName"))) {
                    if (ddformat == null && !currDate.equalsIgnoreCase("") && !currDate.equalsIgnoreCase("undefined")) {
                        int slashval = value.indexOf("/");
                        int slashLast = value.lastIndexOf("/");
                        valu = value.substring(0, slashval);
                        mont = value.substring(slashval + 1, slashLast + 1);
                        CurrValue = mont.concat(valu).concat(value.substring(slashLast));
                        currDate = CurrValue;
                    } else if (ddformat != null && ddformat.equalsIgnoreCase("dd/mm/yy") && !currDate.equalsIgnoreCase("")) {
                        int slashval = value.indexOf("/");
                        int slashLast = value.lastIndexOf("/");
                        valu = value.substring(0, slashval);
                        mont = value.substring(slashval + 1, slashLast + 1);
                        CurrValue = mont.concat(valu).concat(value.substring(slashLast));
                        currDate = CurrValue;
                    }
                }
//               else if(ddformat!=null && ddformat.equalsIgnoreCase("mm/dd/yy")){
//                     int slashval=value.indexOf("/");
//                     int slashLast=value.lastIndexOf("/");
//                     valu=value.substring(0, slashval);
//                     mont=value.substring(slashval+1, slashLast+1);
//                     CurrValue=mont.concat(valu).concat(value.substring(slashLast));
//                   currDate= CurrValue;
//                }

                //response.setHeader("Refresh", "1");
                //response.setHeader("Refresh","1; portalViewer.do?portalBy=viewPortlet&PORTLETID="+portletId+"&REP="+REP+"&CEP="+CEP);

                portalViewerbd.setIsFxCharts(isFxCharts);
                portalViewerbd.setDrillElmentDetails(CBOARPElementDetail);
                portalViewerbd.setDirllOn(tempDirllVal.replace("CBOARP", "").trim());
                portalViewerbd.preparePortletContent(portletId, request, response, REP, CEP, perBy, gpType, currDate, portalTabId, periodType);
                request.setAttribute("PORTLETID", portletId);
                return null;
            } catch (Exception exp) {
                logger.error("Exception:", exp);
                return mapping.findForward("exceptionPage");
            }
        } else {
            return mapping.findForward("sessionExpired");
        }
    }

    public ActionForward portletPreview(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        String portletID = request.getParameter("Portletid");
        String portalID = request.getParameter("portalID");
        String callFrom = request.getParameter("callFrom");
        String width = request.getParameter("width");
        String height = request.getParameter("height");
        PortalViewerBD portalViewerbd = new PortalViewerBD();
        portalViewerbd.setIsFxCharts(false);
        portalViewerbd.setCallFrom(callFrom);
        HttpSession session = request.getSession(false);
        String fromModule = (String) session.getAttribute("ONEVIEW");
        if (fromModule != null && !fromModule.equalsIgnoreCase("") && "ONEVIEW".equals(fromModule)) {
            OnceViewContainer onecontainer = new OnceViewContainer();
            HashMap oneviewHashMap = null;
            oneviewHashMap = (HashMap) session.getAttribute("ONEVIEWDETAILS");
            if (oneviewHashMap != null) {
                onecontainer = (OnceViewContainer) oneviewHashMap.get(request.getParameter("oneViewIdValue"));
                OneViewLetDetails detail = new OneViewLetDetails();
                detail = onecontainer.onviewLetdetails.get(Integer.parseInt(request.getParameter("divId")));
                detail.setRepId(portletID);
//           detail.setGrapId(graphId);repTableoneViewIdValue
                detail.setRepName(request.getParameter("pName"));
                detail.setReptype(request.getParameter("repPortal"));
                detail.setPortalId(request.getParameter("portalID"));
                detail.setRoleId(request.getParameter("busroleId"));
                detail.setWidth(Integer.parseInt(width));
                detail.setHeight(Integer.parseInt(height));
                session.setAttribute("height", height);
                session.setAttribute("width", width);
            } else {
                OneViewLetDetails detail = new OneViewLetDetails();
                ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();
                onecontainer = reportTemplateDAO.getOneViewData(request.getParameter("oneViewIdValue"));
                detail = onecontainer.onviewLetdetails.get(Integer.parseInt(request.getParameter("divId")));
                detail.setRepId(portletID);
//           detail.setGrapId(graphId);repTableoneViewIdValue
                detail.setRepName(request.getParameter("pName"));
                detail.setReptype(request.getParameter("repPortal"));
                detail.setPortalId(request.getParameter("portalID"));
                reportTemplateDAO.updateOneviewData(onecontainer, request.getParameter("oneViewIdValue"));
            }
        }
        portalViewerbd.preparePortletContent(portletID, request, response, "", "", "", "", "", portalID, "");
//        String portletPreview = portalViewerbd.preparePortletPreview(portletID, request, response,portalID);
        //response.getWriter().print(portletPreview);
        return null;
    }

    //used to create portlet graph and forwards to a report designing type of page where user can  design the portlet graph
    public ActionForward goToPortletGraph(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession(false);
        HashMap ParametersHashMap = null;
        HashMap TableHashMap = null;
        HashMap GraphHashMap = null;
        HashMap ReportHashMap = null;
        HashMap map = null;
        String portletId = null;
        Container container = null;
        String portletName = null;
        String portletDesc = null;
        String portalTabId = null;
        String PbUserId = "";
        ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();
        if (session != null) {
            try {
                portletName = request.getParameter("portletName");
                portletDesc = request.getParameter("portletDesc");
                portletId = request.getParameter("portletId");
                PbUserId = String.valueOf(session.getAttribute("USERID"));
                portalTabId = request.getParameter("portalTabId");
                request.setAttribute("portalTabId", portalTabId);
                if (session.getAttribute("PROGENTABLES") != null) {
                    map = (HashMap) session.getAttribute("PROGENTABLES");
                    if (map.get(portletId) != null) {
                        container = (Container) map.get(portletId);
                    } else {
                        container = new Container();
                    }
                } else {
                    container = new Container();
                }
                collect = new PbReportCollection();
                container.setReportCollect(collect);
                container.setReportId(portletId);
                container.setTableId(portletId);


                ParametersHashMap = container.getParametersHashMap();
                TableHashMap = container.getTableHashMap();
                GraphHashMap = container.getGraphHashMap();
                ReportHashMap = container.getReportHashMap();

                if (ParametersHashMap == null) {
                    ParametersHashMap = new HashMap();
                }
                if (TableHashMap == null) {
                    TableHashMap = new HashMap();
                }
                if (GraphHashMap == null) {
                    GraphHashMap = new HashMap();
                }
                if (ReportHashMap == null) {
                    ReportHashMap = new HashMap();
                }

                ReportHashMap.put("PortletName", portletName);
                ReportHashMap.put("PortletDesc", portletDesc);

                String result = reportTemplateDAO.getUserFoldersByUserId(PbUserId);

                request.setAttribute("ReportId", portletId);
                request.setAttribute("UserFlds", result);

                container.setReportName(portletName);
                container.setReportDesc(portletDesc);

                container.setSessionContext(session, container);
                return mapping.findForward("PortletGraphTemplate");
            } catch (Exception exception) {
                logger.error("Exception:", exception);
                return mapping.findForward("exceptionPage");
            }

        } else {
            return mapping.findForward("sessionExpired");
        }
    }

    //used to create portlet table and forwards to a report designing type of page where user can  design the portlet table
    public ActionForward goToPortletCreation(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession(false);
        HashMap ParametersHashMap = null;
        HashMap TableHashMap = null;
        HashMap GraphHashMap = null;
        HashMap ReportHashMap = null;
        HashMap map = null;
        String portletId = null;
        Container container = null;
        String portletName = null;
        String portletDesc = null;
        String portalTabId = null;
        String PbUserId = "";
        String rptType = "";
        ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();
        if (session != null) {
            try {

                portletName = request.getParameter("portletName");
                portletDesc = request.getParameter("portletDesc");
                portletId = request.getParameter("portletId");
                rptType = request.getParameter("rptType");
                PbUserId = String.valueOf(session.getAttribute("USERID"));
                if (portletId == null) {
                    PortalDAO dao = new PortalDAO();
                    portletId = String.valueOf(dao.getNewPortletId());
                }

                if (session.getAttribute("PROGENTABLES") != null) {
                    map = (HashMap) session.getAttribute("PROGENTABLES");
                    if (map.get(portletId) != null) {
                        container = (Container) map.get(portletId);
                    } else {
                        container = new Container();
                        map.put(portletId, container);
                    }
                } else {
                    container = new Container();
                }

                collect = new PbReportCollection();

                container.setReportCollect(collect);
                container.setReportId(portletId);
                container.setTableId(portletId);

                ParametersHashMap = container.getParametersHashMap();
                TableHashMap = container.getTableHashMap();
                GraphHashMap = container.getGraphHashMap();
                ReportHashMap = container.getReportHashMap();

                if (ParametersHashMap == null) {
                    ParametersHashMap = new HashMap();
                }
                if (TableHashMap == null) {
                    TableHashMap = new HashMap();
                }
                if (GraphHashMap == null) {
                    GraphHashMap = new HashMap();
                }
                if (ReportHashMap == null) {
                    ReportHashMap = new HashMap();
                }

                ReportHashMap.put("PortletName", portletName);
                ReportHashMap.put("PortletDesc", portletDesc);
                String result = reportTemplateDAO.getUserFoldersByUserId(PbUserId);
                request.setAttribute("ReportId", portletId);
                request.setAttribute("UserFlds", result);
                request.setAttribute("rptType", rptType);

                container.setReportName(portletName);
                container.setReportDesc(portletDesc);

                container.setSessionContext(session, container);
                if ("table".equalsIgnoreCase(rptType)) {
                    return mapping.findForward("PortletTableTemplate");
                } else if ("graph".equalsIgnoreCase(rptType)) {
                    return mapping.findForward("PortletGraphTemplate");
                } else if ("kpi".equalsIgnoreCase(rptType) || "BasicTarget".equalsIgnoreCase(rptType)) {
                    return mapping.findForward("PortletKPITableTemplate");
                } else if ("kpigraph".equalsIgnoreCase(rptType)) {
                    return mapping.findForward("PortletKPIGraphTemplate");
                }
            } catch (Exception exception) {
                logger.error("Exception:", exception);
                return mapping.findForward("exceptionPage");
            }

        } else {
            return mapping.findForward("sessionExpired");
        }
        return null;
    }

    //used to create portlet table and forwards to a report designing type of page where user can  design the portlet table
    public ActionForward goToPortletTable(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession(false);
        HashMap ParametersHashMap = null;
        HashMap TableHashMap = null;
        HashMap GraphHashMap = null;
        HashMap ReportHashMap = null;
        HashMap map = null;
        String portletId = null;
        Container container = null;
        String portletName = null;
        String portletDesc = null;
        String portalTabId = null;
        String PbUserId = "";
        ArrayList TimeDetailstList = new ArrayList();
        ProgenParam pParam = new ProgenParam();
        ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();
        if (session != null) {
            try {

                portletName = request.getParameter("portletName");
                portletDesc = request.getParameter("portletDesc");
                portletId = request.getParameter("portletId");
                PbUserId = String.valueOf(session.getAttribute("USERID"));
                portalTabId = request.getParameter("portalTabId");

                request.setAttribute("portalTabId", portalTabId);

                if (session.getAttribute("PROGENTABLES") != null) {
                    map = (HashMap) session.getAttribute("PROGENTABLES");
                    if (map.get(portletId) != null) {
                        container = (Container) map.get(portletId);
                    } else {
                        container = new Container();
                    }
                } else {
                    container = new Container();
                }

                collect = new PbReportCollection();
                container.setReportCollect(collect);
                container.setReportId(portletId);
                container.setTableId(portletId);

//                TimeDetailstList.add("Day");
//                TimeDetailstList.add("PRG_STD");
//                TimeDetailstList.add(pParam.getdateforpage().toString());
//                TimeDetailstList.add("Month");
//                TimeDetailstList.add("Last Period");
//
//                collect.timeDetailsArray=TimeDetailstList;
//                container.setReportCollect(collect);

                ParametersHashMap = container.getParametersHashMap();
                TableHashMap = container.getTableHashMap();
                GraphHashMap = container.getGraphHashMap();
                ReportHashMap = container.getReportHashMap();

                if (ParametersHashMap == null) {
                    ParametersHashMap = new HashMap();
                }
                if (TableHashMap == null) {
                    TableHashMap = new HashMap();
                }
                if (GraphHashMap == null) {
                    GraphHashMap = new HashMap();
                }
                if (ReportHashMap == null) {
                    ReportHashMap = new HashMap();
                }

                ReportHashMap.put("PortletName", portletName);
                ReportHashMap.put("PortletDesc", portletDesc);
                String result = reportTemplateDAO.getUserFoldersByUserId(PbUserId);
                request.setAttribute("ReportId", portletId);
                request.setAttribute("UserFlds", result);

                container.setReportName(portletName);
                container.setReportDesc(portletDesc);

                container.setSessionContext(session, container);
                return mapping.findForward("PortletTableTemplate");
            } catch (Exception exception) {
                logger.error("Exception:", exception);
                return mapping.findForward("exceptionPage");
            }

        } else {
            return mapping.findForward("sessionExpired");
        }
    }

    //used to create portlet KPIGraph and forwards to a report designing type of page where user can  design the portlet KPIGraph
    public ActionForward goToPortletKPIGraph(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession(false);
        HashMap ParametersHashMap = null;
        HashMap TableHashMap = null;
        HashMap GraphHashMap = null;
        HashMap ReportHashMap = null;
        HashMap map = null;
        String portletId = null;
        Container container = null;
        String portletName = null;
        String portletDesc = null;
        String portalTabId = null;
        String PbUserId = "";
        ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();
        if (session != null) {
            try {

                portletName = request.getParameter("portletName");
                portletDesc = request.getParameter("portletDesc");
                portletId = request.getParameter("portletId");
                PbUserId = String.valueOf(session.getAttribute("USERID"));
                portalTabId = request.getParameter("portalTabId");
                request.setAttribute("portalTabId", portalTabId);

                if (session.getAttribute("PROGENTABLES") != null) {
                    map = (HashMap) session.getAttribute("PROGENTABLES");
                    if (map.get(portletId) != null) {
                        container = (Container) map.get(portletId);
                    } else {
                        container = new Container();
                    }
                } else {
                    container = new Container();
                }

                collect = new PbReportCollection();
                container.setReportCollect(collect);
                container.setReportId(portletId);
                container.setTableId(portletId);


                ParametersHashMap = container.getParametersHashMap();
                TableHashMap = container.getTableHashMap();
                GraphHashMap = container.getGraphHashMap();
                ReportHashMap = container.getReportHashMap();

                if (ParametersHashMap == null) {
                    ParametersHashMap = new HashMap();
                }
                if (TableHashMap == null) {
                    TableHashMap = new HashMap();
                }
                if (GraphHashMap == null) {
                    GraphHashMap = new HashMap();
                }
                if (ReportHashMap == null) {
                    ReportHashMap = new HashMap();
                }

                ReportHashMap.put("PortletName", portletName);
                ReportHashMap.put("PortletDesc", portletDesc);

                String result = reportTemplateDAO.getUserFoldersByUserId(PbUserId);
                request.setAttribute("ReportId", portletId);
                request.setAttribute("UserFlds", result);
                container.setReportName(portletName);
                container.setReportDesc(portletDesc);
                container.setSessionContext(session, container);
                return mapping.findForward("PortletKPIGraphTemplate");
            } catch (Exception exception) {
                logger.error("Exception:", exception);
                return mapping.findForward("exceptionPage");
            }

        } else {
            return mapping.findForward("sessionExpired");
        }
    }

    //used to create portlet KPITable and forwards to a report designing type of page where user can  design the portlet KPITable
    public ActionForward goToPortletKPITable(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {

        HttpSession session = request.getSession(false);
        HashMap ParametersHashMap = null;
        HashMap TableHashMap = null;
        HashMap GraphHashMap = null;
        HashMap ReportHashMap = null;
        HashMap map = null;
        String portletId = null;
        Container container = null;
        String portletName = null;
        String portletDesc = null;
        String portalTabId = null;
        String PbUserId = "";
        ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();
        if (session != null) {
            try {

                portletName = request.getParameter("portletName");
                portletDesc = request.getParameter("portletDesc");
                portletId = request.getParameter("portletId");
                PbUserId = String.valueOf(session.getAttribute("USERID"));
                portalTabId = request.getParameter("portalTabId");

                request.setAttribute("portalTabId", portalTabId);


                if (session.getAttribute("PROGENTABLES") != null) {
                    map = (HashMap) session.getAttribute("PROGENTABLES");
                    if (map.get(portletId) != null) {
                        container = (Container) map.get(portletId);
                    } else {
                        container = new Container();
                    }
                } else {
                    container = new Container();
                }

                collect = new PbReportCollection();
                container.setReportCollect(collect);
                container.setReportId(portletId);
                container.setTableId(portletId);


                ParametersHashMap = container.getParametersHashMap();
                TableHashMap = container.getTableHashMap();
                GraphHashMap = container.getGraphHashMap();
                ReportHashMap = container.getReportHashMap();

                if (ParametersHashMap == null) {
                    ParametersHashMap = new HashMap();
                }
                if (TableHashMap == null) {
                    TableHashMap = new HashMap();
                }
                if (GraphHashMap == null) {
                    GraphHashMap = new HashMap();
                }
                if (ReportHashMap == null) {
                    ReportHashMap = new HashMap();
                }

                ReportHashMap.put("PortletName", portletName);
                ReportHashMap.put("PortletDesc", portletDesc);

                String result = reportTemplateDAO.getUserFoldersByUserId(PbUserId);
                request.setAttribute("ReportId", portletId);
                request.setAttribute("UserFlds", result);

                container.setReportName(portletName);
                container.setReportDesc(portletDesc);

                container.setSessionContext(session, container);
                return mapping.findForward("PortletKPITableTemplate");
            } catch (Exception exception) {
                logger.error("Exception:", exception);
                return mapping.findForward("exceptionPage");
            }

        } else {
            return mapping.findForward("sessionExpired");
        }
    }

    //is used to save the portlet in data base in the form of xml and which is used to build portlet by reading from xml
    public ActionForward savePortlet(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession(false);
        String portletId = null;
        Container container = null;
        HashMap map = null;
        HashMap ReportHashMap = null;
        HashMap ParameterHashMap = new HashMap();
        String portletName = "";
        String portletDesc = "";
        String displayType = "";
        HashMap allViewBys = new HashMap();
        ArrayList<String> allViewIds = new ArrayList<String>();
        ArrayList<String> allViewNames = new ArrayList<String>();
        ArrayList<String> rowviewbyIds = new ArrayList<String>();
        String userId = String.valueOf(session.getAttribute("USERID"));
        PortletDesigner portalDesigner = new PortletDesigner();
        String measType = request.getParameter("measType");
        String daytargetVal = request.getParameter("daytargetVal");
        String measureName = request.getParameter("measureName");
        if (session != null) {
            try {
                portletId = request.getParameter("portletId");
                if (session.getAttribute("PROGENTABLES") != null) {
                    map = (HashMap) session.getAttribute("PROGENTABLES");
                    if (map.get(portletId) != null) {
                        container = (Container) map.get(portletId);

                        ReportHashMap = container.getReportHashMap();

                        portletName = String.valueOf(ReportHashMap.get("PortletName"));
                        portletDesc = String.valueOf(ReportHashMap.get("PortletDesc"));
                        displayType = request.getParameter("displayType");

                        if (displayType != null && displayType.equalsIgnoreCase("KPI Graph")) {
                            buildKPIGraph(request, response, measType);
                        }

                        collect = container.getReportCollect();
                        allViewBys = collect.getReportParameters();
                        String[] roles = collect.reportBizRoles;
                        String[] allKeys = (String[]) (allViewBys.keySet()).toArray(new String[0]);
                        for (int i = 0; i < allViewBys.size(); i++) {
                            allViewIds.add(allKeys[i]);
                            allViewNames.add(collect.getElementName(allKeys[i]));
                        }
                        rowviewbyIds = collect.reportRowViewbyValues;

                        ParameterHashMap.put("Parameters", allViewIds);
                        ParameterHashMap.put("RowViewByIds", rowviewbyIds);
                        ParameterHashMap.put("TimeDetailstList", container.getReportCollect().timeDetailsArray);
                        ParameterHashMap.put("TimeDimHashMap", container.getReportCollect().timeDetailsMap);
//                        portalDesigner.setParametersHashMap(container.getParametersHashMap());

                        portalDesigner.setParametersHashMap(ParameterHashMap);
                        portalDesigner.setTableHashMap(container.getTableHashMap());
                        portalDesigner.setGraphHashMap(container.getGraphHashMap());
                        portalDesigner.setReportHashMap(container.getReportHashMap());
                        portalDesigner.setPortletId(Integer.parseInt(portletId));

                        portalDesigner.setPortletName(portletName);
                        portalDesigner.setDisplayType(displayType);
                        portalDesigner.setPortletDesc(portletDesc);
                        portalDesigner.setRequest(request);
                        portalDesigner.setFolderIds(roles);
                        portalDesigner.setUserId(userId);
                        portalDesigner.createDocument();
                        map.remove(portletId);//to clear portlet details from session
                        PortalViewerBD viewBD = new PortalViewerBD();
                        boolean status = viewBD.portletAssign(portletId, "-1", "", userId);
                        PortalDAO portalDAO = new PortalDAO();
                        session.setAttribute("PORTALS", portalDAO.buildPortal(Integer.parseInt(userId)));
                    }
                }
            } catch (Exception exp) {
                logger.error("Exception:", exp);
            }
        } else {
        }
        return mapping.findForward("showViewPortal");
    }

    //is used to view portlet gadgets in portals
    public ActionForward viewPortletGadgets(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        PbDb pbdb = new PbDb();
        //String portletName = request.getParameter("portletName");
        String portletId = request.getParameter("PORTLETID");
        //String portletTabId = request.getParameter("portletTabId");
        String xmltype = request.getParameter("xmlstr");
        HttpSession session = request.getSession(false);
        //String status = "";
        String xmlPath = "";
        if (xmltype.equalsIgnoreCase("Calender")) {
            xmlPath = "<script src=\"http://www.gmodules.com/ig/ifr?url=http://www.google.com/ig/modules/datetime.xml&up_color=blue&up_dateFormat=wmd&up_firstDay=0&up_clocks=%5B%5B-73.986951%2C40.756054%2C%22New%20York%22%2C0%5D%5D&synd=open&w=360&h=140&title=Clock&lang=en&country=ALL&border=http%3A%2F%2Fwww.gmodules.com%2Fig%2Fimages%2F&output=js\"></script>";
        } else if (xmltype.equalsIgnoreCase("Clock")) {
            xmlPath = "<script src=\"http://www.gmodules.com/ig/ifr?url=http://www.canbuffi.de/gadgets/clock/clock.xml&up_title=Clock%20%26%20Date&up_time_format=0&up_seconds=1&up_date_format=0&up_dayofweek=1&up_offset_hours=&up_offset_minutes=&up_daylight=0&up_color=red&up_amazon=1&synd=open&w=320&h=170&title=__UP_title__&lang=en&country=CA&border=%23ffffff%7C3px%2C1px+solid+%23999999&output=js\"></script>";
        } else if (xmltype.equalsIgnoreCase("Msgbox")) {
            xmlPath = "<script src=\"http://www.gmodules.com/ig/ifr?url=http://www.labpixies.com/campaigns/todo/todo.xml&amp;up_saved_tasks=&amp;up_todos=&amp;up_sd=&amp;up_wasSocial=0&amp;synd=open&amp;w=350&amp;h=250&amp;title=__MSG_gTitle__&amp;lang=en&amp;country=US&amp;border=%23ffffff%7C3px%2C1px+solid+%23999999&amp;output=js\"></script>";
        }
        String sqlQuery = "update PRG_PORTLETS_MASTER set xml_path='" + xmlPath.replace("||chr(38)||", "' ||chr(38)|| '") + "' , PORTLET_TYPE='E' where portlet_id=" + portletId;

        pbdb.execModifySQL(sqlQuery);

        if (session != null) {

            response.getWriter().print(xmlPath.replace("||chr(38)||", "&"));
            List<Portal> portals = (List<Portal>) session.getAttribute("PORTALS");
            List<PortLet> portlets = null;
            PortLet portLet = null;
            for (Portal portal : portals) {
                portlets = portal.getPortlets();
                Iterator<PortLet> moduleIter = Iterables.filter(portlets, PortLet.getAccessPortletPredicate(Integer.parseInt(portletId))).iterator();
                if (moduleIter.hasNext()) {
                    portLet = moduleIter.next();
                    break;
                }
            }
            portLet.setXmlString(xmlPath);
            portLet.setPortletType("E");
            return null;
        } else {
            return mapping.findForward("sessionExpired");
        }
    }

    //is called when user re orders his/her portlets and to make the persistence of his order  we stored it in database
    public ActionForward savePortletOrder(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        PortalViewerBD portalViewerbd = new PortalViewerBD();
        String portletIds = "";
        String tabId = "";
        String userId = String.valueOf(request.getSession(false).getAttribute("USERID"));
        portletIds = request.getParameter("portletIds");
        tabId = request.getParameter("tabId");
        String[] columnPortletIds = null;
        HashMap PortletOrderMap = new HashMap();
        String[] divIds = {"1", "2", "3"};
        if (!"".equalsIgnoreCase(portletIds)) {
            columnPortletIds = portletIds.split(";");
            for (int colIndex = 0; colIndex < columnPortletIds.length; colIndex++) {
                PortletOrderMap.put(divIds[colIndex], columnPortletIds[colIndex]);
            }
            boolean result = portalViewerbd.savePortletOrder(PortletOrderMap, divIds, tabId, request);
            PortalDAO portalDAO = new PortalDAO();
            portalDAO.buildPortal(Integer.parseInt(userId));
        }
        return null;
    }

    //is used to called to delete a porytlet 
    public ActionForward deletePortlet(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession(false);
        PortalViewerBD portalViewerbd = new PortalViewerBD();
        String portletId = "";
        String portalTabId = "";
        PrintWriter out = response.getWriter();
        portletId = request.getParameter("portletId");
        portalTabId = request.getParameter("portalTabId");
        boolean result = false;
        result = portalViewerbd.deletePortlet(portletId, portalTabId);
        if (result) {
            List<Portal> portals = (List<Portal>) session.getAttribute("PORTALS");
            PortLet portLet = null;
            for (Portal portal : portals) {
                ArrayList<PortLet> portLets = (ArrayList<PortLet>) portal.getPortlets();
                Iterator<PortLet> moduleIter = Iterables.filter(portLets, PortLet.getAccessPortletPredicate(Integer.parseInt(portletId))).iterator();
                if (moduleIter.hasNext()) {
                    portLet = moduleIter.next();
                    portLets.remove(portLet);
                    break;
                }

            }
            session.setAttribute("PORTALS", portals);


        }

        out.print(result);
        return null;
    }

    //is called when user wants to cancel designing  in middle of the portlet designing
    public ActionForward cancelPortlet(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession(false);
        String portletId = "";
        HashMap map = null;
        if (session != null) {
            try {
                if (request.getParameter("PORTLETID") != null) {
                    portletId = request.getParameter("PORTLETID");
                    if (session.getAttribute("PROGENTABLES") != null) {
                        map = (HashMap) session.getAttribute("PROGENTABLES");
                        if (map != null && map.get(portletId) != null) {
                            map.remove(portletId);
                        }
                    }
                }
                // return null;
            } catch (Exception exp) {
                logger.error("Exception:", exp);
                //return mapping.findForward("exceptionPage");
            }
            return null;
        } else {
            return mapping.findForward("sessionExpired");
        }
    }

    public ActionForward getPortletKpis(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        String foldersIds = request.getParameter("foldersIds");
        PrintWriter out = response.getWriter();
        DashboardTemplateDAO dashboardTemplateDAO = new DashboardTemplateDAO();
        out.print(dashboardTemplateDAO.getKpis(foldersIds));

        return null;
    }

    public ActionForward buildPortletKPIS(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession(false);
        String KPIS = request.getParameter("Kpis");
        String[] KPIIds = null;
        String portletId = "";
        HashMap map = null;
        Container container = null;
        ArrayList REP = new ArrayList();
        ArrayList REPNames = new ArrayList();
        ArrayList Parameters = new ArrayList();
        ArrayList ParametersNames = new ArrayList();
        ArrayList Measures = new ArrayList();
        HashMap TableHashMap = null;
        HashMap ParametersHashMap = null;

        if (session != null) {
            try {
                if (request.getParameter("PORTLETID") != null) {
                    portletId = request.getParameter("PORTLETID");
                    if (session.getAttribute("PROGENTABLES") != null) {
                        map = (HashMap) session.getAttribute("PROGENTABLES");
                        if (map != null && map.get(portletId) != null) {
                            container = (Container) map.get(portletId);
                            ParametersHashMap = container.getParametersHashMap();
                            if (ParametersHashMap.get("ParametersNames") != null) {
                                Parameters = (ArrayList) ParametersHashMap.get("Parameters");
                                ParametersNames = (ArrayList) ParametersHashMap.get("ParametersNames");
                            }
                            if (Parameters != null && Parameters.size() != 0) {
                                REP.add(String.valueOf(Parameters.get(0)));
                                REPNames.add(String.valueOf(ParametersNames.get(0)));
                            }
                            TableHashMap = container.getTableHashMap();
                            TableHashMap.put("REP", REP);
                            TableHashMap.put("REPNames", REPNames);

                            if (KPIS != null) {
                                KPIIds = KPIS.split(",");
                                container.setKPIS(KPIIds);
                                for (int i = 0; i < KPIIds.length; i++) {
                                    Measures.add(KPIIds[i]);
                                }
                                TableHashMap.put("Measures", Measures);
                                TableHashMap.put("MeasuresNames", Measures);
                            }
                        }
                    }
                }
            } catch (Exception exp) {
                logger.error("Exception:", exp);
            }
            return null;
        } else {
            return mapping.findForward("sessionExpired");
        }
    }

    //preview of kpi in portlet designing
    public ActionForward dispPortletKPITable(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        PortalViewerBD portalViewerbd = new PortalViewerBD();
        HttpSession session = request.getSession(false);
        String[] KPIIds = null;
        String KPIS = "";
        HashMap ParametersMap = null;
        String portletId = "";
        HashMap map = null;
        Container container = null;
        String kpiTableStr = "";
        PrintWriter out = response.getWriter();

        if (session != null) {
            try {
                if (request.getParameter("PORTLETID") != null) {
                    portletId = request.getParameter("PORTLETID");
                    if (session.getAttribute("PROGENTABLES") != null) {
                        map = (HashMap) session.getAttribute("PROGENTABLES");
                        if (map != null && map.get(portletId) != null) {
                            container = (Container) map.get(portletId);
                            KPIIds = container.getKPIS();
                            if (KPIIds != null) {
                                for (String str : KPIIds) {
                                    KPIS += "," + str;
                                }
                                if (KPIS != null && !"".equalsIgnoreCase(KPIS)) {
                                    KPIS = KPIS.substring(1);
                                }

                                ParametersMap = container.getParametersHashMap();
                                //kpiTableStr = dashboardTempBD.dashboardKpiPreview(KPIS, (pbDashboardCollection) container.getReportCollect());
                                kpiTableStr = portalViewerbd.getKPIPreview(KPIS, container.getReportCollect());
                                out.print(kpiTableStr);
                            }
                        }
                    }
                }
            } catch (Exception exp) {
                logger.error("Exception:", exp);
            }
            return null;
        } else {
            return mapping.findForward("sessionExpired");
        }
    }

    public ActionForward deletePortlets(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession(false);
        String portletIds = request.getParameter("portletids");
        PortalViewerBD bd = new PortalViewerBD();
        String[] portletIDs = portletIds.split(",");
        bd.deletePortlets(portletIds);
        List<Portal> portals = (List<Portal>) session.getAttribute("PORTALS");
        List<PortLet> portlets = null;
        PortLet portLet = null;
        for (String polet : portletIDs) {
            for (Portal portal : portals) {
                portlets = portal.getPortlets();
                Iterator<PortLet> moduleIter = Iterables.filter(portlets, PortLet.getAccessPortletPredicate(Integer.parseInt(polet))).iterator();
                if (moduleIter.hasNext()) {
                    portLet = moduleIter.next();

                }
                portlets.remove(portLet);

            }
        }
        return null;
    }

    //preview of kpi graph region in portlet designing
    public ActionForward displayKPIGraphRegion(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {

        HttpSession session = request.getSession(false);
        HashMap map = null;
        String portletId = null;
        Container container = null;
        String dispGraph = null;
        String[] KPIIds = null;
        String KPIS = "";
        PrintWriter out = response.getWriter();
        HashMap GraphHashMap = null;
        ArrayList AllGraphColumns = new ArrayList();
        HashMap GraphDetails = new HashMap();
        HashMap ParametersHashMap = null;
        HashMap TableHashMap = null;

        ArrayList Parameters = new ArrayList();
        ArrayList ParametersNames = new ArrayList();

        ArrayList REP = new ArrayList();
        ArrayList REPNames = new ArrayList();

        ArrayList CEP = new ArrayList();
        ArrayList CEPNames = new ArrayList();


        String[] barChartColumnNames = new String[2];
        String[] barChartColumnTitles = new String[2];
        String[] pieChartColumns = new String[2];
        String[] axis = {"0", "0"};
        String[] viewByElementIds = new String[1];
        HashMap allViewBys = new HashMap();
        ArrayList<String> allViewIds = new ArrayList<String>();
        ArrayList<String> allViewNames = new ArrayList<String>();
        ArrayList<String> rowviewbyIds = new ArrayList<String>();
        PortalViewerBD portalViewerbd = new PortalViewerBD();
        if (session != null) {
            try {
                portletId = request.getParameter("portletId");
                if (session.getAttribute("PROGENTABLES") != null) {
                    map = (HashMap) session.getAttribute("PROGENTABLES");
                    if (map.get(portletId) != null) {
                        container = (Container) map.get(portletId);
                        GraphHashMap = container.getGraphHashMap();
                        ParametersHashMap = container.getParametersHashMap();
                        TableHashMap = container.getTableHashMap();
                        KPIIds = container.getKPIS();
                        if (KPIIds != null) {
                            for (String str : KPIIds) {
                                KPIS += "," + str;
                            }
                            if (KPIS != null && !"".equalsIgnoreCase(KPIS)) {
                                KPIS = KPIS.substring(1);
                            }
                        }
                        ////.println("KPIS is " + KPIS);
                        collect = container.getReportCollect();
                        allViewBys = collect.getReportParameters();
                        String[] allKeys = (String[]) (allViewBys.keySet()).toArray(new String[0]);
                        for (int i = 0; i < allViewBys.size(); i++) {
                            allViewIds.add(allKeys[i]);
                            allViewNames.add(collect.getElementName(allKeys[i]));
                        }

                        ParametersHashMap.put("Parameters", allViewIds);
                        ParametersHashMap.put("ParametersNames", allViewNames);
                        ParametersHashMap.put("TimeDetailstList", collect.timeDetailsArray);
                        ParametersHashMap.put("TimeDimHashMap", collect.timeDetailsMap);
                        dispGraph = portalViewerbd.displayKPIGraphRegion(KPIS, container);

                        if (ParametersHashMap.get("Parameters") != null) {
                            ParametersNames = (ArrayList) ParametersHashMap.get("ParametersNames");
                            Parameters = (ArrayList) ParametersHashMap.get("Parameters");

                            REP.add(String.valueOf(Parameters.get(0)));
                            REPNames.add(String.valueOf(ParametersNames.get(0)));

                            viewByElementIds[0] = String.valueOf(Parameters.get(0));
                        }
                        if (REP != null && REP.size() != 0) {
                            barChartColumnNames[0] = String.valueOf(Parameters.get(0));
                            barChartColumnNames[1] = KPIS.split(",")[0];

                            barChartColumnTitles[0] = String.valueOf(ParametersNames.get(0));
                            barChartColumnTitles[1] = KPIS.split(",")[0];
                            pieChartColumns = barChartColumnNames;

                        }

                        AllGraphColumns.add(KPIS);
                        GraphHashMap.put("AllGraphColumns", AllGraphColumns);
                        GraphHashMap.put("graphIds", "1");

                        GraphDetails.put("graphId", "1");
                        GraphDetails.put("grpSize", "Medium");
                        GraphDetails.put("graphWidth", "400");
                        GraphDetails.put("graphHeight", "250");
                        GraphDetails.put("barChartColumnNames", barChartColumnNames);
                        GraphDetails.put("pieChartColumns", pieChartColumns);
                        GraphDetails.put("barChartColumnTitles", barChartColumnTitles);
                        GraphDetails.put("viewByElementIds", viewByElementIds);
                        GraphDetails.put("axis", axis);


                        GraphHashMap.put("1", GraphDetails);

                        TableHashMap.put("REP", REP);
                        TableHashMap.put("REPNames", REPNames);
                        TableHashMap.put("CEP", CEP);
                        TableHashMap.put("CEPNames", CEPNames);
                    }
                }
            } catch (Exception exception) {
                logger.error("Exception:", exception);
            }
            out.print(dispGraph);
            return null;
        } else {
            return mapping.findForward("sessionExpired");
        }
    }

    //building kpi graph with all inputs required
    public String buildKPIGraph(HttpServletRequest request, HttpServletResponse response, String measType) {
        HttpSession session = request.getSession(false);
        pchart = new ProgenChartDisplay(400, 250);
        HashMap map = null;
        String portletId = null;
        Container container = null;
        String dispGraph = "";
        String[] KPIIds = null;
        String KPIS = "";
        String hr1 = null;
        String hr2 = null;
        String mr1 = null;
        String mr2 = null;
        String lr1 = null;
        String lr2 = null;
        String kpigrpType = null;
        String needleStr = null;
        String path = null;
        PrintWriter out = null;
        String daytargetVal = null;
        String measureName = null;

        double C;
        double D;
        double A;
        double B;
        double needle;
        double E;
        double sRange = 0;
        double eRange;

        double fBreak;
        double sBreak;
        double tempB = 0;

        HashMap GraphHashMap = null;
        HashMap ReportHashMap = null;
        HashMap GraphDetails = null;
        if (session != null) {
            try {
                out = response.getWriter();

                portletId = request.getParameter("portletId");
                hr1 = request.getParameter("hr1");
                hr2 = request.getParameter("hr2");
                mr1 = request.getParameter("mr1");
                mr2 = request.getParameter("mr2");
                lr1 = request.getParameter("lr1");
                lr2 = request.getParameter("lr2");
                kpigrpType = request.getParameter("kpigrpType");
                needleStr = request.getParameter("needleStr");
                daytargetVal = request.getParameter("daytargetVal");
                measureName = request.getParameter("measureName");

                C = (double) Integer.parseInt(hr1);
                D = (double) Integer.parseInt(lr1);
                A = (double) Integer.parseInt(mr1);
                B = (double) Integer.parseInt(mr2);
                needle = new Double(needleStr);
                E = B - A;
                //sRange = C;

                if (Integer.parseInt(lr1) > Integer.parseInt(hr1)) {
                    sRange = C;
                } else if (Integer.parseInt(lr1) < Integer.parseInt(hr1)) {
                    sRange = D;
                } else {
                    sRange = C;
                }

                if (Integer.parseInt(lr2) == 0) {
                    eRange = D;
                } else if (Integer.parseInt(lr2) < Integer.parseInt(hr1)) {
                    eRange = (double) Integer.parseInt(hr2);
                } else {
                    eRange = (double) Integer.parseInt(lr2);
                }
                fBreak = A;
                sBreak = B;
                tempB = 0;
                if (fBreak > sBreak) {
                    tempB = fBreak;
                    fBreak = sBreak;
                    sBreak = tempB;
                }

                if (session.getAttribute("PROGENTABLES") != null) {
                    map = (HashMap) session.getAttribute("PROGENTABLES");
                    if (map.get(portletId) != null) {
                        container = (Container) map.get(portletId);
                        KPIIds = container.getKPIS();

                        if (KPIIds != null) {
                            for (String str : KPIIds) {
                                KPIS += "," + str;
                            }
                            if (KPIS != null && !"".equalsIgnoreCase(KPIS)) {
                                KPIS = KPIS.substring(1);
                            }
                            ReportHashMap = container.getReportHashMap();
                            GraphHashMap = container.getGraphHashMap();
                            GraphDetails = (HashMap) GraphHashMap.get("1");
                            GraphDetails.put("startRange", sRange);
                            GraphDetails.put("endRange", eRange);
                            GraphDetails.put("firstBreak", fBreak);
                            GraphDetails.put("secondBreak", sBreak);
                            GraphDetails.put("needleValue", needleStr);
                            GraphDetails.put("graphName", String.valueOf(ReportHashMap.get("PortletName")));
                            GraphDetails.put("measType", measType);
                            GraphDetails.put("daytargetVal", daytargetVal);
                            GraphDetails.put("measureName", measureName);

                            pchart.setCtxPath(request.getContextPath());
                            if (!measType.equalsIgnoreCase("Standard")) {
                                pchart.setColorchange("yes");
                            }

                            if (kpigrpType.equalsIgnoreCase("Meter")) {
                                path = pchart.GetMeterChart(sRange, eRange, fBreak, sBreak, needle, "", session, response, out);

                            } else {
                                path = pchart.GetThermChart(sRange, eRange, fBreak, sBreak, needle, session, response, out);
                            }
                            GraphDetails.put("graphClassName", kpigrpType);
                            GraphDetails.put("graphTypeName", kpigrpType);
                            if (D > C) {
                                GraphDetails.put("getKpiGraphStatus", "N");
                            } else {
                                GraphDetails.put("getKpiGraphStatus", "Y");
                            }

                        }
                        dispGraph = pchart.chartDisplay;
                    }
                }
            } catch (Exception exception) {
                logger.error("Exception:", exception);
            }

        }
        return dispGraph;
    }

    //preview of kpi graph in portlet designing
    public ActionForward displayKPIGraph(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {

        HttpSession session = request.getSession(false);
        String dispGraph = "";
        PrintWriter out = response.getWriter();
        String measType = request.getParameter("measType");

        if (session != null) {
            try {
                dispGraph = buildKPIGraph(request, response, measType);
            } catch (Exception exception) {
                logger.error("Exception:", exception);
            }
            out.print(dispGraph);
            return null;
        } else {
            return mapping.findForward("sessionExpired");
        }
    }

    //used to edit and update portlet name in portlets 
    public ActionForward updatePortletName(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {

        String portletId = "";
        String portalTabId = "";
        String portletName = "";
        String portletDesc = "";
        PrintWriter out = response.getWriter();
        portletId = request.getParameter("portletId");
        portalTabId = request.getParameter("portalTabId");
        portletName = request.getParameter("portletName");
        portletDesc = request.getParameter("portletDesc");
        HttpSession session = request.getSession(false);
        boolean result = false;
        PortalViewerBD portalViewerbd = new PortalViewerBD();
        result = portalViewerbd.updatePortletName(portalTabId, portletId, portletName, portletDesc);
        List<Portal> portals = (List<Portal>) session.getAttribute("PORTALS");
        List<PortLet> portlets = null;
        PortLet portLet = null;
        Portal tempportal = null;
        for (Portal portal : portals) {
            Iterator<Portal> moduleIter = Iterables.filter(portals, Portal.getAccessPortalPredicate(Integer.parseInt(portalTabId))).iterator();
            if (moduleIter.hasNext()) {
                tempportal = moduleIter.next();
                break;
            }
        }
        Iterator<PortLet> moduleIterforPortlets = Iterables.filter(tempportal.getPortlets(), PortLet.getAccessPortletPredicate(Integer.parseInt(portletId))).iterator();
        if (moduleIterforPortlets.hasNext()) {
            portLet = moduleIterforPortlets.next();
        }
        portLet.setPortLetName(portletName);
        portLet.setPortLetDes(portletDesc);

        out.print(result);
        return null;
    }

    //used to delete user portals and his/her corressponding portlets
    public ActionForward deletePortals(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession(false);
        PortalViewerBD portalViewerbd = new PortalViewerBD();
        PrintWriter out = response.getWriter();
        String portalIds = null;
        String userId = null;
        boolean flag = false;
        if (session != null) {
            try {
                portalIds = request.getParameter("portalIds");
                userId = String.valueOf(session.getAttribute("USERID"));
                flag = portalViewerbd.deletePortals(portalIds, userId);
                ////.println("flag is " + flag);
                String[] portaIds = portalIds.split(",");
                if (flag) {
                    List<Portal> portals = (List<Portal>) session.getAttribute("PORTALS");
                    Portal portal = null;
                    for (String portalID : portaIds) {
                        Iterator<Portal> moduleIter = Iterables.filter(portals, Portal.getAccessPortalPredicate(Integer.parseInt(portalID))).iterator();
                        if (moduleIter.hasNext()) {
                            portal = moduleIter.next();
                        }
                        portals.remove(portal);
                    }
                    session.setAttribute("PORTALS", portals);
                }


            } catch (Exception exp) {
                logger.error("Exception:", exp);
            }
        } else {
            ////.println("flag is " + flag);
        }
        if (flag) {
            out.print("Portals Deleted Successfully...");
        } else {
            out.print("Failed in deleting Portals...");
        }
        return null;
    }

    public ActionForward updatePortalTimeDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession(false);
        PrintWriter out = response.getWriter();
        String portalTabId = null;
        String currDate = null;
        boolean flag = false;
        if (session != null) {
            try {
                PortalViewerBD viewBD = new PortalViewerBD();
                portalTabId = request.getParameter("TABID");
                currDate = String.valueOf(session.getAttribute("curDate"));
                flag = viewBD.updatePortalTimeDetails(portalTabId, currDate);
                ////.println("flag is " + flag);
            } catch (Exception exp) {
                logger.error("Exception:", exp);
            }
        } else {
            ////.println("flag is " + flag);
        }
        out.print(flag);
        return null;
    }

    public ActionForward setSortOrder(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String portletId = request.getParameter("portletId");
        String sortlevel = request.getParameter("sortlevel");
        HttpSession session = request.getSession(false);
        List<Portal> portals = (List<Portal>) session.getAttribute("PORTALS");
        List<PortLet> portlets = null;
        PortLet portLet = null;
        for (Portal portal : portals) {
            portlets = portal.getPortlets();
            Iterator<PortLet> moduleIter = Iterables.filter(portlets, PortLet.getAccessPortletPredicate(Integer.parseInt(portletId))).iterator();
            if (moduleIter.hasNext()) {
                portLet = moduleIter.next();
                break;
            }
        }
        portLet.setSortOrder(sortlevel);
        PortalViewerBD viewBD = new PortalViewerBD();
        boolean checkUpdate = viewBD.setSortOrder(portletId, sortlevel);
        return null;
    }

    public ActionForward portletAssign(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String Portletid = request.getParameter("Portletid");
        String portalId = request.getParameter("portalId");
        String portletHeight = request.getParameter("portletHeight");
        PortalViewerBD viewBD = new PortalViewerBD();
        String userId = (String) request.getSession().getAttribute("USERID");
        boolean status = viewBD.portletAssign(Portletid, portalId, portletHeight, userId);
        Portal portal = null;
        try {
            if (status) {
                PortalDAO portalDAO = new PortalDAO();

                List<Portal> portals = portalDAO.buildPortal(Integer.parseInt(userId));
                request.getSession(false).setAttribute("PORTALS", portals);
                Iterator<Portal> moduleIter = Iterables.filter(portals, Portal.getAccessPortalPredicate(Integer.parseInt(portalId))).iterator();
                if (moduleIter.hasNext()) {
                    portal = moduleIter.next();
                }
                response.getWriter().print(status + "~" + portal.getPortalName());
            } else {
                response.getWriter().print(status + "~" + " ");
            }

        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }
        return null;
    }

    public ActionForward savePortletProperties(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String portletId = request.getParameter("PortletID");
        String portalId = request.getParameter("portalID");
        String showLegends = request.getParameter("showLegendsVar");
        String graphGridLines = request.getParameter("graphGridLinesvar");
        String showGT = request.getParameter("showGTvar");
        String showLabels = request.getParameter("showLabelsvar");
        String showMinMaxRange = request.getParameter("showMinMaxRangevar");
        String nbrFormat = request.getParameter("nbrFormat");
        String graphLegendLoc = request.getParameter("graphLegendLoc");
        String graphSymbol = request.getParameter("graphSymbol");
        String SwapColumn = request.getParameter("SwapColumn");
        String grpSize = request.getParameter("grpSize");
        String graphDisplayRows = request.getParameter("graphDisplayRows");
        GraphProperty graphProperty = new GraphProperty();
        graphProperty.setLabelsDisplayed(Boolean.parseBoolean(showLabels));
        graphProperty.setMinMaxRange(Boolean.parseBoolean(showMinMaxRange));
        graphProperty.setSwapGraphColumns(SwapColumn);
        graphProperty.setSymbol(graphSymbol);
        graphProperty.setNumberFormat(nbrFormat);
        graphProperty.setGrpSize(grpSize);
        graphProperty.setGraphLegendLoc(graphLegendLoc);
        graphProperty.setShowGT(showGT);
        graphProperty.setGraphGridLines(graphGridLines);
        graphProperty.setShowLegends(showLegends);
        graphProperty.setGraphDisplayRows(graphDisplayRows);
        Gson gson = new Gson();
        String gsonString = gson.toJson(graphProperty);
//        
        PortalDAO portalDAO = new PortalDAO();
        portalDAO.savePortletProperties(portalId, portletId, gsonString);
        ArrayList<Portal> portals = new ArrayList<Portal>();
        if (request.getSession() != null) {
            portals = (ArrayList<Portal>) request.getSession().getAttribute("PORTALS");
        }
        Portal portal = null;
        PortLet portLet = null;
        Iterator<PortLet> moduleIter = null;
        for (Portal port : portals) {
            moduleIter = Iterables.filter(port.getPortlets(), PortLet.getAccessPortletPredicate(Integer.parseInt(portletId))).iterator();
            if (moduleIter.hasNext()) {
                portLet = moduleIter.next();
                portal = port;
                if (portal.getPortalID() == Integer.parseInt(portalId)) {
                    break;
                }
            }
        }
        portLet.setGraphProperty(graphProperty);
        try {
            response.getWriter().print("true");
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }
        return null;
    }

    public ActionForward getPortletProperty(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String portalID = request.getParameter("portalID");
        String portletID = request.getParameter("PortletID");
        PortalViewerBD viewerBD = new PortalViewerBD();
        String properties = viewerBD.getPortletProperty(portalID, portletID, request);

        try {
            response.getWriter().print(properties);
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }
        return null;
    }

    public ActionForward saveXmalOfPortlet(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String portalid = request.getParameter("portalTabId");
        String portletId = request.getParameter("portletId");
        String REP = request.getParameter("rowEdgeParams");
        String CEP = request.getParameter("colEdgeParams");
        String graphType = request.getParameter("gpType");
        String saveStatus = request.getParameter("saveStatus");
        String nameofNewPortlet = request.getParameter("nameofNewPortlet");
        String userId = (String) request.getSession(false).getAttribute("USERID");
        String portalName = "";
        PbDb pbdb = new PbDb();
        PbReturnObject oldid = new PbReturnObject();
        boolean status = false;
        HashMap GraphClassesHashMap = (HashMap) request.getSession(false).getAttribute("GraphClassesHashMap");
        ArrayList<String> paramList = new ArrayList<String>();
        paramList.add(portalid);
        paramList.add(portletId);
        paramList.add(REP);
        paramList.add(CEP);
        paramList.add(graphType);
        PortalViewerBD viewerBD = new PortalViewerBD();
        ArrayList<Portal> portals = new ArrayList<Portal>();
        if (request.getSession() != null) {
            portals = (ArrayList<Portal>) request.getSession().getAttribute("PORTALS");
        }
        Portal portal = null;
        PortLet portLet = null;
        Iterator<PortLet> moduleIter = null;
        String sortlevel = "";
        String portName = null;
        for (Portal port : portals) {
            moduleIter = Iterables.filter(port.getPortlets(), PortLet.getAccessPortletPredicate(Integer.parseInt(portletId))).iterator();
            if (moduleIter.hasNext()) {
                portLet = moduleIter.next();
                portal = port;
                if (portal.getPortalID() == Integer.parseInt(portalid)) {
                    portalName = portal.getPortalName();
                    portName = portLet.getPortLetName();
                    if (portLet != null && portLet.getPortletXMLHelper() != null && graphType.equalsIgnoreCase("Table")) {
                        portLet.getPortletXMLHelper().getMetaInfo().put("DisplayType", graphType);
                    }
                    if (portal.isFilterflag() && saveStatus.equalsIgnoreCase("saveAsNew")) {
                        portLet.setPortLetName(nameofNewPortlet);
                        String qry = "select distinct PORTLET_ID from PRG_BASE_PORTLETS_MASTER where PORTLET_NAME='" + portName + "'";
                        oldid = pbdb.execSelectSQL(qry);
                        String qry1 = "update PRG_USER_PORTLETS_MASTER set PORTLET_NAME='" + nameofNewPortlet + "' where PORTLET_ID=" + oldid.getFieldValueString(0, "PORTLET_ID");
                        pbdb.execUpdateSQL(qry1);
                    }
                    break;
                }
            }
        }
        sortlevel = portLet.getSortOrder();
        if (portal.isFilterflag() && saveStatus.equalsIgnoreCase("saveAsNew")) {
            status = viewerBD.saveXmalOfPortlet(paramList, portLet, userId, GraphClassesHashMap, saveStatus, portName, sortlevel);
            portal.setFilterflag(false);
        } else {
            status = viewerBD.saveXmalOfPortlet(paramList, portLet, userId, GraphClassesHashMap, saveStatus, nameofNewPortlet, sortlevel);
        }
//        status = viewerBD.saveXmalOfPortlet(paramList, portLet, userId, GraphClassesHashMap, saveStatus, nameofNewPortlet, sortlevel);
        if (status) {
            PortalDAO portalDAO = new PortalDAO();
            List<Portal> portals1 = portalDAO.buildPortal(Integer.parseInt(userId));
            request.getSession(false).setAttribute("PORTALS", portals1);

        }

        try {
            if (saveStatus.equalsIgnoreCase("saveAsNew")) {
                response.getWriter().print(status + "~" + portalName);
            } else {
                response.getWriter().print(status);
            }
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }
        return null;
    }

    public ActionForward extendTablePortlet(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {

        String portletId = request.getParameter("portletId");
        String portalTabId = request.getParameter("portalTabId");
        int value = Integer.parseInt(request.getParameter("selectId"));
        int selectedvalue = 0;

        if (value == 1) {
            selectedvalue = 420;
        } else if (value == 2) {
            selectedvalue = 700;
        } else if (value == 3) {
            selectedvalue = 1050;
        }
        HttpSession session = request.getSession(false);
        boolean result = false;
        PrintWriter out = response.getWriter();
        PortalViewerBD portalViewerbd = new PortalViewerBD();
        result = portalViewerbd.extendTablePortlet(portalTabId, portletId, selectedvalue);
        List<Portal> portals = (List<Portal>) session.getAttribute("PORTALS");
        List<PortLet> portlets = null;
        PortLet portLet = null;
        Portal tempportal = null;

        tempportal = Iterables.find(portals, Portal.getAccessPortalPredicate(Integer.parseInt(portalTabId)));

        portLet = Iterables.find(tempportal.getPortlets(), PortLet.getAccessPortletPredicate(Integer.parseInt(portletId)));

        portLet.setPortletHeight(selectedvalue);
        out.print(result);
        return null;
    }

    public ActionForward getTimePeriodDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String portletId = request.getParameter("PortletId");
        String portalTabId = request.getParameter("portalTabId");
        String time = request.getParameter("time");
        String period = request.getParameter("period");
        String comparePeriod = request.getParameter("comparePeriod");

        PortLetTimeHelper portLetTimeHelper = new PortLetTimeHelper();
        HttpSession session = request.getSession(false);
        List<Portal> portals = (List<Portal>) session.getAttribute("PORTALS");
        List<PortLet> portlets = null;
        PortLet portLet = null;
        Portal tempportal = null;
        Calendar cal;
        String s;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date date;
        if (time == null || time.equalsIgnoreCase("")) {
            cal = Calendar.getInstance();
            date = cal.getTime();
            s = sdf.format(date);
            time = s;
        }
        tempportal = Iterables.find(portals, Portal.getAccessPortalPredicate(Integer.parseInt(portalTabId)));
        Iterator<PortLet> moduleIterforPortlets = Iterables.filter(tempportal.getPortlets(), PortLet.getAccessPortletPredicate(Integer.parseInt(portletId))).iterator();
        if (moduleIterforPortlets.hasNext()) {
            portLet = moduleIterforPortlets.next();
        }
        String value = "";
        String valu = "";
        String mont = "";
        String CurrValue = "";
        value = time;
        String ddformat = null;
        if (session.getAttribute("dateFormat") != null) {
            ddformat = session.getAttribute("dateFormat").toString();
        }
        if (ddformat == null && !time.equalsIgnoreCase("")) {
            int slashval = value.indexOf("/");
            int slashLast = value.lastIndexOf("/");
            valu = value.substring(0, slashval);
            mont = value.substring(slashval + 1, slashLast + 1);
            CurrValue = mont.concat(valu).concat(value.substring(slashLast));
            time = CurrValue;
        } else if (ddformat != null && ddformat.equalsIgnoreCase("dd/mm/yy")) {
            int slashval = value.indexOf("/");
            int slashLast = value.lastIndexOf("/");
            valu = value.substring(0, slashval);
            mont = value.substring(slashval + 1, slashLast + 1);
            CurrValue = mont.concat(valu).concat(value.substring(slashLast));
            time = CurrValue;
        }

        Gson gson = new Gson();
        HashMap timeHashMap = new HashMap();
        timeHashMap.put("portletTime", time);
        timeHashMap.put("portletPeriod", period);
        timeHashMap.put("comparePeriod", comparePeriod);
        String gsonString = gson.toJson(timeHashMap);
        PortalDAO portalDAO = new PortalDAO();
        int status = portalDAO.updatePortletTimeDetails(portletId, portalTabId, gsonString);
        portLetTimeHelper.setPortletTime(time);
        portLetTimeHelper.setPortletPeriod(period);
        portLetTimeHelper.setStdComprePeriod(comparePeriod);

        portLet.setPortLetTimeHelper(portLetTimeHelper);
        return null;

    }

    public ActionForward resetTimePeriodDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String portletId = request.getParameter("PortletId");
        String portalTabId = request.getParameter("portalTabId");
        HttpSession session = request.getSession(false);
        List<Portal> portals = (List<Portal>) session.getAttribute("PORTALS");
        List<PortLet> portlets = null;
        PortLet portLet = null;
        Portal tempportal = null;
        for (Portal portal : portals) {
            Iterator<Portal> moduleIter = Iterables.filter(portals, Portal.getAccessPortalPredicate(Integer.parseInt(portalTabId))).iterator();
            if (moduleIter.hasNext()) {
                tempportal = moduleIter.next();
                break;
            }
        }
        Iterator<PortLet> moduleIterforPortlets = Iterables.filter(tempportal.getPortlets(), PortLet.getAccessPortletPredicate(Integer.parseInt(portletId))).iterator();
        if (moduleIterforPortlets.hasNext()) {
            portLet = moduleIterforPortlets.next();
        }

        PortalDAO portalDAO = new PortalDAO();
        int status = portalDAO.resetPortletTimeDetails(portletId, portalTabId);

        if (portLet.getPortLetTimeHelper() != null) {
            PortLetTimeHelper portLetTimeHelper = portLet.getPortLetTimeHelper();
            portLetTimeHelper.setPortletPeriod("");
            portLetTimeHelper.setPortletTime("");
            portLet.setPortLetTimeHelper(portLetTimeHelper);
        }



        return null;
    }

    public ActionForward applySortOnTable(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String countVal = request.getParameter("countVal");
        String sortByColumeVal = request.getParameter("sortByColumeVal");
        String sortType = request.getParameter("sortType");
        String PortletId = request.getParameter("PortletId");
        String portalTabId = request.getParameter("portalTabId");
        String checkval = request.getParameter("checkval");
        String gpType = request.getParameter("gpType");
        if (checkval.equalsIgnoreCase("true")) {
            countVal = "0";
        }
        HttpSession session = request.getSession(false);
        List<Portal> portals = (List<Portal>) session.getAttribute("PORTALS");
        PortLet portLet = null;
        Portal tempportal1 = null;
        tempportal1 = Iterables.find(portals, Portal.getAccessPortalPredicate(Integer.parseInt(portalTabId)));
        portLet = Iterables.find(tempportal1.getPortlets(), PortLet.getAccessPortletPredicate(Integer.parseInt(PortletId)));
        if (count != 0) {
            count = portLet.getPortletSorterHelper().getCountVal();
            sortTypeVal = portLet.getPortletSorterHelper().getSortByColumeVal();
        }
        Portal tempportal = null;
        for (Portal portal : portals) {
            Iterator<Portal> moduleIter = Iterables.filter(portals, Portal.getAccessPortalPredicate(Integer.parseInt(portalTabId))).iterator();
            if (moduleIter.hasNext()) {
                tempportal = moduleIter.next();
                break;
            }
        }

        Iterator<PortLet> moduleIterforPortlets = Iterables.filter(tempportal.getPortlets(), PortLet.getAccessPortletPredicate(Integer.parseInt(PortletId))).iterator();
        if (moduleIterforPortlets.hasNext()) {
            portLet = moduleIterforPortlets.next();
        }


        Gson gson = new Gson();
        HashMap sortHashMap = new HashMap();
        sortHashMap.put("countVal", countVal);
        sortHashMap.put("sortByColumeVal", sortByColumeVal);
        sortHashMap.put("sortType", sortType);
        sortHashMap.put("isSortAll", checkval);
        String gsonstring = gson.toJson(sortHashMap);

        // PortalDAO portalDAO = new PortalDAO();
        // portalDAO.updatePortLetSorterHelper(PortletId,portalTabId,gsonstring);

        PortLetSorterHelper portletSorterHelper = new PortLetSorterHelper();
        portletSorterHelper.setCountVal(Integer.parseInt(countVal));
        portletSorterHelper.setSortByColumeVal(sortByColumeVal);
        portletSorterHelper.setSortType(sortType);
        portletSorterHelper.setIsSortAll(Boolean.parseBoolean(checkval));
        portLet.setPortletSorterHelper(portletSorterHelper);



        return null;
    }

    public ActionForward getKpiTargetDeviation(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String targetVal = request.getParameter("targetVal");
        String actualValue = request.getParameter("actualValue");
        DashboardTemplateDAO templateDAO = new DashboardTemplateDAO();
        PrintWriter out = null;
        String deviationVal = String.valueOf(templateDAO.getDeviation(actualValue, targetVal));
        try {
            out = response.getWriter();
            out.print(deviationVal);
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }

        return null;

    }

    public ActionForward DrillToReport(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String portletId = request.getParameter("PORTLETID");
        String portalTabId = request.getParameter("portalTabId");
        HttpSession session = request.getSession(false);
        String folderId = "";
        String portletName = "";
        List<Portal> portals = (List<Portal>) session.getAttribute("PORTALS");
        List<PortLet> portlets = null;
        PortLet portLet = null;
        Portal tempportal = null;
        tempportal = Iterables.find(portals, Portal.getAccessPortalPredicate(Integer.parseInt(portalTabId)));
        portLet = Iterables.find(tempportal.getPortlets(), PortLet.getAccessPortletPredicate(Integer.parseInt(portletId)));
        folderId = String.valueOf(portLet.getFolderId());
        portletName = portLet.getPortLetName();
        DashboardTemplateDAO dashdao = new DashboardTemplateDAO();
        PrintWriter out = response.getWriter();
        out.print(dashdao.getKpiDrillToAnyReport(portletId, folderId, portletName));

        return null;
    }

    public ActionForward setRelatedPortlet(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String portletId = request.getParameter("portletId");
        String portalTabId = request.getParameter("portalTabId");
        List<Portal> portals = (List<Portal>) request.getSession(false).getAttribute("PORTALS");
        PortLet portLet = null;
        Portal tempportal = null;
        tempportal = Iterables.find(portals, Portal.getAccessPortalPredicate(Integer.parseInt(portalTabId)));
        portLet = Iterables.find(tempportal.getPortlets(), PortLet.getAccessPortletPredicate(Integer.parseInt(portletId)));
        Set<String> relatedPortLetIds = portLet.getRelatedPortlets();
        List<String> relatedPortlets = new ArrayList<String>(relatedPortLetIds);
        Iterator it = relatedPortLetIds.iterator();
        String value = null;
        Portal portal = null;
        try {
            portal = Iterables.find(portals, Portal.getAccessPortalPredicate(-2));
        } catch (NoSuchElementException elementException) {
            portal = new Portal(-2, "RelPortlets");
        }
        if (portal.getPortlets() != null || !portal.getPortlets().isEmpty()) {
            portal.getPortlets().clear();
        }
        portal.setPortalDes("RelPortlets");
        for (Portal portal1 : portals) {
            if (!relatedPortlets.isEmpty()) {
                for (int i = 0; i < relatedPortlets.size(); i++) {
                    try {
                        if (relatedPortlets.get(i) != "") {
                            portal.addPortlet(Iterables.find(portal1.getPortlets(), PortLet.getAccessPortletPredicate(Integer.parseInt(relatedPortlets.get(i)))));
                        }
                        relatedPortlets.remove(i);
                        i--;
                    } catch (NoSuchElementException elementException) {
                    }
                }
            } else {
                break;
            }
        }
//           for(String string:relatedPortLetIds){
//              portLet = Iterables.find(tempportal.getPortlets(), PortLet.getAccessPortletPredicate(Integer.parseInt(string)));
//              portal.addPortlet(portLet);
//
//           }
        try {
            portal = Iterables.find(portals, Portal.getAccessPortalPredicate(-2));
        } catch (NoSuchElementException elementException) {
            portals.add(portal);
        }
        request.getSession(false).setAttribute("PORTALS", portals);
        while (it.hasNext()) {
            value = (String) it.next();
        }
        if (value.equalsIgnoreCase("")) {
            response.getWriter().print("false");
        } else {
            response.getWriter().print("true");
        }
        return null;

    }

    public ActionForward getRelatedPortlets(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String portletId = request.getParameter("portletId");
        String portalTabId = request.getParameter("portalTabId");
        String folderId = "";
        HttpSession session = request.getSession(false);
        List<Portal> portals = (List<Portal>) session.getAttribute("PORTALS");
        List<PortLet> portlets = null;
        PortLet portLet = null;
//                 Portal tempportal=null;
//                 tempportal=Iterables.find(portals,Portal.getAccessPortalPredicate(-1));
        Portal selectedPortal = Iterables.find(portals, Portal.getAccessPortalPredicate(Integer.parseInt(portalTabId)));
        portLet = Iterables.find(selectedPortal.getPortlets(), PortLet.getAccessPortletPredicate(Integer.parseInt(portletId)));
        Set<String> seletedRelatePIDs = portLet.getRelatedPortlets();
        HashMap<String, String> relatedPortlet = new HashMap<String, String>();
        ArrayList<String> selectedRelPortletIds = new ArrayList<String>();
        ArrayList<String> selectedRelPortletNames = new ArrayList<String>();
        for (String relString : seletedRelatePIDs) {
            if (!relString.trim().equalsIgnoreCase("")) {
                for (Portal portal : portals) {
                    if (portal.isExistingPotlet(Integer.parseInt(relString))) {
                        portLet = Iterables.find(portal.getPortlets(), PortLet.getAccessPortletPredicate(Integer.parseInt(relString)));
                        selectedRelPortletIds.add(Integer.toString(portLet.getPortLetId()));
                        selectedRelPortletNames.add(portLet.getPortLetName());
                        break;
                    }

                }
            }

        }

        for (Portal portal : portals) {
            for (PortLet let : portal.getPortlets()) {
                relatedPortlet.put(Integer.toString(let.getPortLetId()), let.getPortLetName());

            }
        }
        Set<String> keySet = relatedPortlet.keySet();
        ArrayList<String> dragableArrayList = new ArrayList<String>();
        ArrayList<String> PortletsNames = new ArrayList<String>();
        for (String rel : keySet) {
            dragableArrayList.add(rel);
            PortletsNames.add(relatedPortlet.get(rel));
        }

        GenerateDragAndDrophtml dragAndDrophtml = new GenerateDragAndDrophtml("Drag Portlet from here", "Drop Portlet here", selectedRelPortletIds, (ArrayList<String>) dragableArrayList, request.getContextPath());
        dragAndDrophtml.setDragableListNames((ArrayList<String>) PortletsNames);
        dragAndDrophtml.setDropedmesNames(selectedRelPortletNames);
        String htmlJson = dragAndDrophtml.getDragAndDropDiv();
        try {
            response.getWriter().print(htmlJson);
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }


        return null;
    }

    public ActionForward saveRelatedPortlets(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String PortalTabId = request.getParameter("PortalTabId");
        String PortLetId = request.getParameter("PortLetId");
        String reletedIds = request.getParameter("reletedIds");
        PortalDAO portaldao = new PortalDAO();
        portaldao.updateRelatedPortals(PortalTabId, PortLetId, reletedIds);
        String[] reletedIdsArrSes = reletedIds.split(",");
        List<String> relatedList = new ArrayList<String>();
        relatedList.addAll(Arrays.asList(reletedIdsArrSes));
        List<Portal> portals = (List<Portal>) request.getSession(false).getAttribute("PORTALS");
        Portal tempPortal = Iterables.find(portals, Portal.getAccessPortalPredicate(-2));
        Portal tempPortalForName = Iterables.find(portals, Portal.getAccessPortalPredicate(Integer.parseInt(PortalTabId)));
        PortLet portLet = Iterables.find(tempPortalForName.getPortlets(), PortLet.getAccessPortletPredicate(Integer.parseInt(PortLetId)));
        portLet.setRelatedPortlets(new HashSet<String>(relatedList));
        tempPortal.getPortlets().clear();
//              Portal portal=  Iterables.find(portals, Portal.getAccessPortalPredicate(-1));

        for (Portal portal : portals) {
            if (!relatedList.isEmpty()) {
                for (int i = 0; i < relatedList.size(); i++) {
                    try {
                        tempPortal.addPortlet(Iterables.find(portal.getPortlets(), PortLet.getAccessPortletPredicate(Integer.parseInt(relatedList.get(i)))));
                        relatedList.remove(i);
                        i--;
                    } catch (NoSuchElementException elementException) {
                    }
                }
            } else {
                break;
            }
        }


        portals.add(tempPortal);
        request.getSession(false).setAttribute("PORTALS", portals);
        response.getWriter().print(tempPortalForName.getPortalName());
        return null;
    }

    public ActionForward saveTargetValue(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String portletId = "";
        String portalTabId = "";
        String ElementId = "";
        String targetVal = "";
        String reportId = "";
        String kpimasterId = "";
        portletId = request.getParameter("portletId");
        portalTabId = request.getParameter("portalTabId");
        ElementId = request.getParameter("ElementId");
        targetVal = request.getParameter("tval");
        HashMap<String, String> hmap = new HashMap<String, String>();
        hmap.put(ElementId, targetVal);
        kpimasterId = request.getParameter("kpiMaterId");
        PortletKPIHelper portltkpihelper = new PortletKPIHelper();
        portltkpihelper.setEidMap(hmap);
        portltkpihelper.setPortletId(portletId);
        portltkpihelper.setPortalTabId(portalTabId);
        portltkpihelper.setElementId(ElementId);
        portltkpihelper.setTargetVal(targetVal);
        portltkpihelper.setKpiMasterId(kpimasterId);
        Gson gson = new Gson();
        String jsonString = gson.toJson(portltkpihelper);
        PortalDAO portaldao = new PortalDAO();
        portaldao.saveTargetValueOfKpi(jsonString, portletId, portalTabId);

        return null;
    }

    public ActionForward getMinMaxAvgValueOfElement(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {

        String portletId = request.getParameter("portletId");
        String portalId = request.getParameter("portalId");
        String columnname = request.getParameter("columnname");
        // 
        PortalViewerBD viewerBD = new PortalViewerBD();
        String valuesOfElement = viewerBD.getMinMaxAvgValueOfElement(request, response, portletId, portalId, columnname);
        response.getWriter().print(valuesOfElement);
        return null;
    }

    public ActionForward savePortalColors(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);                         //fordashboardTableColor
        String elementClId = request.getParameter("colName");
        String elementId = elementClId.substring(2);
        String disColName = request.getParameter("disColName");
        String maxVal = request.getParameter("max");
        String minVal = request.getParameter("min");
        boolean isgradint = Boolean.parseBoolean(request.getParameter("gradientBased"));
        String elementName = request.getParameter("colmnlabelName");
        String portalId = request.getParameter("portalId");
        String portletId = request.getParameter("portletId");

        String colorCodes[] = request.getParameterValues("colorCodes");
        String operators[] = request.getParameterValues("operators");
        String sValues[] = request.getParameterValues("sValues");
        String eValues[] = request.getParameterValues("eValues");

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

        List<Portal> portals = (List<Portal>) request.getSession(false).getAttribute("PORTALS");
        Portal tempportal = Iterables.find(portals, Portal.getAccessPortalPredicate(Integer.parseInt(portalId)));
        PortLet portlet = Iterables.find(tempportal.getPortlets(), PortLet.getAccessPortletPredicate(Integer.parseInt(portletId)));
        List<DashboardTableColorGroupHelper> tableColorGrpList = portlet.getPortalTableColor();

        DashboardTableColorGroupHelper portalTblcolhelper = null;
        boolean statusOfAdding = true;
        if (tableColorGrpList.isEmpty()) {
            portalTblcolhelper = new DashboardTableColorGroupHelper();
        } else {
            for (DashboardTableColorGroupHelper helper : tableColorGrpList) {
                if (helper.getElementId().equalsIgnoreCase(elementId)) {
                    portalTblcolhelper = helper;
                    statusOfAdding = false;
                    break;
                }
            }
            if (portalTblcolhelper == null) {
                portalTblcolhelper = new DashboardTableColorGroupHelper();
            }
        }
        portalTblcolhelper.setElementId(elementId);
        portalTblcolhelper.setElementName(elementName);
        portalTblcolhelper.setMeasureType(disColName);
        portalTblcolhelper.setMeasureMaxValue(maxVal);
        portalTblcolhelper.setMeasureMinValue(minVal);
        portalTblcolhelper.setIsgradiantBase(isgradint);
        portalTblcolhelper.setColorVal(colrcode);
        portalTblcolhelper.setColorCondOper(operator);
        portalTblcolhelper.setCondStartValue(strtVal);
        portalTblcolhelper.setCondEndValue(endVal);

        if (statusOfAdding) {
            tableColorGrpList.add(portalTblcolhelper);
        }

        Gson gson = new Gson();
        String portalTableColor = "";
        if (!tableColorGrpList.isEmpty()) {
            portalTableColor = gson.toJson(tableColorGrpList);
        }
        //  PortalDAO portaldao = new PortalDAO();
        //  portaldao.setPortalColors(portalTableColor,portalId,portletId);
        //
        portlet.setPortalTableColor(tableColorGrpList);
        return null;
    }

    public ActionForward resetTablePortlet(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
        String portletId = request.getParameter("portletId");
        String portalId = request.getParameter("portalTabId");
//        
        String userId = "";
        HttpSession session = request.getSession(false);
        if (session.getAttribute("USERID") != null) {
            userId = String.valueOf(session.getAttribute("USERID"));
        }
        List<Portal> portals = (List<Portal>) session.getAttribute("PORTALS");
        Portal tempportal = Iterables.find(portals, Portal.getAccessPortalPredicate(Integer.parseInt(portalId)));
        PortLet portlet = Iterables.find(tempportal.getPortlets(), PortLet.getAccessPortletPredicate(Integer.parseInt(portletId)));
        tempportal.getPortlets().remove(portlet);
        PortalDAO portalDAO = new PortalDAO();
        PortLet portLet = portalDAO.getPortlet(portletId, portalId, userId);
        tempportal.getPortlets().add(portLet);
        return null;
    }

    public ActionForward runPortalScheduler(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {

        String portalId = request.getParameter("portalTabId");
        String portalname = request.getParameter("schdPortalName");
        String startDate = request.getParameter("startdate");
        String endDate = request.getParameter("enddate");
        int frequency = Integer.parseInt(request.getParameter("frequency"));
        int hrs = 0;
        int mins = 0;
        String userId = "";
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
        String emailid = "";
        // String emil_Id = request.getParameter("mail");
        String[] email_Ids = request.getParameterValues("mail");
        for (int j = 0; j < email_Ids.length; j++) {
            emailid = emailid + "," + email_Ids[j];
        }
        ReportSchedule schedule = new ReportSchedule();
        schedule.setFrequency(request.getParameter("frequency"));
        int rep_Id = Integer.parseInt(portalId);
        schedule.setReportId(rep_Id);
        String time = request.getParameter("hrs") + ":" + request.getParameter("mins");
        schedule.setScheduledTime(time);
        schedule.setViewBy("");
        userId = (String) request.getSession(false).getAttribute("USERID");
        schedule.setUserId(userId);
        schedule.setContentType("PortalPDF");
        List<ReportSchedulePreferences> schdPreferenceList = new ArrayList<ReportSchedulePreferences>();
        ReportSchedulePreferences schedulePreferences = new ReportSchedulePreferences();
        schedulePreferences.setMailIds(emailid);
        schedulePreferences.setDimValues("All");
        schedulePreferences.setSchedulerId(rep_Id);
        schdPreferenceList.add(schedulePreferences);
        schedule.setIsAutoSplited(true);
        schedule.setReportSchedulePrefrences(schdPreferenceList);
        schedule.setSchedulerName(portalname);
        schedule.setRequest(request);
        schedule.setResponse(response);
        ReportSchedulerJob job = new ReportSchedulerJob();
        try {
            job.sendSchedulerMail(schedule);
        } catch (ParseException ex) {
            logger.error("Exception:", ex);
        }
        return null;
    }

    public ActionForward savePortalScheduler(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException, ParseException {
        String portalId = request.getParameter("portalTabId");
        String portalname = request.getParameter("schdPortalName");
        String startDate = request.getParameter("startdate");
        String endDate = request.getParameter("enddate");
        String emailid = "";
        String[] mailDetail = request.getParameterValues("mail");
        for (int i = 0; i < mailDetail.length; i++) {
            emailid = emailid + "," + mailDetail[i];
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
        ReportSchedule schedule = new ReportSchedule();
        Date sDate;
        Date eDate;
        DateFormat formatter;
        formatter = new SimpleDateFormat("MM/dd/yyyy");
        sDate = formatter.parse(startDate);
        eDate = formatter.parse(endDate);
        schedule.setFrequency(request.getParameter("frequency"));
        schedule.setReportId(Integer.parseInt(portalId));
        schedule.setContenType("PortalPDF");
        schedule.setEndDate(eDate);
        schedule.setFrequency(schedulerFrequency);
        schedule.setStartDate(sDate);
        schedule.setViewBy("");
        schedule.setScheduledTime(scheduledTime);
        String userId = (String) request.getSession(false).getAttribute("USERID");
        schedule.setUserId(userId);
        schedule.setSchedulerName(portalname);
        schedule.setIsAutoSplited(true);
        schedule.setRequest(request);
        schedule.setResponse(response);
        List<ReportSchedulePreferences> schdPreferenceList = new ArrayList<ReportSchedulePreferences>();
        ReportSchedulePreferences schedulePreferences = new ReportSchedulePreferences();
        schedulePreferences.setMailIds(emailid);
        schedulePreferences.setDimValues("All");
        schdPreferenceList.add(schedulePreferences);
        schedule.setIsAutoSplited(true);
        schedule.setSchedulerName(portalname);
        schedule.setParticularDay(particularDate);
        schedule.setDataSelection(dataSelection);
        schedule.setReportSchedulePrefrences(schdPreferenceList);
        SchedulerDAO dao = new SchedulerDAO();
        String schedId = dao.insertReportSchedule(schedule);
        dao.insertReportSchedulePreferences(schedule);
        SchedulerBD bd = new SchedulerBD();
        bd.scheduleReport(schedule, false);
        PrintWriter out = null;
        try {
            out = response.getWriter();
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }
        out.print(schedId);
        return null;
    }

    public ActionForward changeGraphMeasure(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException, ParseException {
        String portletId = request.getParameter("portletId");
        String portalTabId = request.getParameter("portalTabId");
        //String grpColumns=request.getParameter("grpColumns");
        String measureNames = request.getParameter("measureNames");
        String measureIds = request.getParameter("measureIds");
        PortalDAO dao = new PortalDAO();
        HashMap measureInfo = dao.getMesureInfoDetails(measureIds);
        //
        List<Portal> portals = (List<Portal>) request.getSession(false).getAttribute("PORTALS");
        List<PortLet> portlets = null;
        PortLet portLet = null;
        Portal tempportal = null;
        tempportal = Iterables.find(portals, Portal.getAccessPortalPredicate(Integer.parseInt(portalTabId)));
        portLet = Iterables.find(tempportal.getPortlets(), PortLet.getAccessPortletPredicate(Integer.parseInt(portletId)));
        ArrayList<String> QryElementIds = portLet.getPortletXMLHelper().getQryElementIds();
        QryElementIds.clear();
        portLet.getPortletXMLHelper().getQryAggregations().clear();
        portLet.getPortletXMLHelper().getQryColNames().clear();
        String[] mIds = measureIds.split(",");
        for (int i = 0; i < mIds.length; i++) {
//            if(!QryElementIds.contains(mIds[i])){
            ArrayList elementDeatils = (ArrayList) measureInfo.get(mIds[i]);
            QryElementIds.add(QryElementIds.size(), (String) elementDeatils.get(0));
            portLet.getPortletXMLHelper().getQryAggregations().add(portLet.getPortletXMLHelper().getQryAggregations().size(), (String) elementDeatils.get(1));
            portLet.getPortletXMLHelper().getQryColNames().add(portLet.getPortletXMLHelper().getQryColNames().size(), (String) elementDeatils.get(2));
            if (i == 0) {
                portLet.getPortletXMLHelper().getSortColumnsAndOrder().clear();
                String mem = "A_" + (String) elementDeatils.get(0);
                portLet.getPortletXMLHelper().getSortColumnsAndOrder().put(mem, "ASC");
            }
//            }
        }
        return null;
    }

    public ActionForward editTimePeriodDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException, ParseException {
        String portletId = request.getParameter("PortletId");
        String portalTabId = request.getParameter("portalTabId");
        List<Portal> portals = (List<Portal>) request.getSession(false).getAttribute("PORTALS");
        List<PortLet> portlets = null;
        PortLet portLet = null;
        Portal tempportal = null;
        tempportal = Iterables.find(portals, Portal.getAccessPortalPredicate(Integer.parseInt(portalTabId)));
        portLet = Iterables.find(tempportal.getPortlets(), PortLet.getAccessPortletPredicate(Integer.parseInt(portletId)));
        HashMap<String, String> timedetails = new HashMap<String, String>();
        if (portLet.getPortLetTimeHelper() != null) {
            PortLetTimeHelper portLetTimeHelper = portLet.getPortLetTimeHelper();
            String date = portLetTimeHelper.getPortletTime();
            String periodType = portLetTimeHelper.getPortletPeriod();
            String comparePeriod = portLetTimeHelper.getStdComprePeriod();

            String value = "";
            String valu = "";
            String mont = "";
            String CurrValue = "";
            value = date;
            if (!date.equals("")) {
                int slashval = value.indexOf("/");
                int slashLast = value.lastIndexOf("/");
                valu = value.substring(0, slashval);
                mont = value.substring(slashval + 1, slashLast + 1);
                CurrValue = mont.concat(valu).concat(value.substring(slashLast));
                date = CurrValue;
            }

            timedetails.put("date", date);
            timedetails.put("periodType", periodType);
            timedetails.put("comparePeriod", comparePeriod);
        }
        Gson gson = new Gson();
        String jsonString = gson.toJson(timedetails);
        response.getWriter().print(jsonString);
        return null;
    }

    public ActionForward setTargetVal(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException, ParseException {
        String portletId = request.getParameter("portletId");
        String portalTabId = request.getParameter("portalTabId");
        String targetVal = request.getParameter("targetVal");
        List<Portal> portals = (List<Portal>) request.getSession(false).getAttribute("PORTALS");
        List<PortLet> portlets = null;
        PortLet portLet = null;
        Portal tempportal = null;
        tempportal = Iterables.find(portals, Portal.getAccessPortalPredicate(Integer.parseInt(portalTabId)));
        portLet = Iterables.find(tempportal.getPortlets(), PortLet.getAccessPortletPredicate(Integer.parseInt(portletId)));
        portLet.setTargetVal(targetVal);
        return null;
    }

    public ActionForward resetGraphPortlet(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException, ParseException {
        int countVal = count;
        String sortByColumeVal = sortTypeVal;
        String countOldVal = request.getParameter("countVal");
        String sortByColumeOldVal = request.getParameter("sortByColumeVal");
        String sortType = request.getParameter("sortType");
        String PortletId = request.getParameter("PortletId");
        String portalTabId = request.getParameter("portalTabId");
        String checkval = request.getParameter("checkval");
        String gpType = request.getParameter("gpType");
        if (checkval.equalsIgnoreCase("true")) {
            countVal = 0;
        }
        HttpSession session = request.getSession(false);
        List<Portal> portals = (List<Portal>) session.getAttribute("PORTALS");
        PortLet portLet = null;
        Portal tempportal1 = null;
        tempportal1 = Iterables.find(portals, Portal.getAccessPortalPredicate(Integer.parseInt(portalTabId)));
        portLet = Iterables.find(tempportal1.getPortlets(), PortLet.getAccessPortletPredicate(Integer.parseInt(PortletId)));
        if (gpType != "Table" && gpType != null) {
            flag = true;
            resetGpType = portLet.getPortletXMLHelper().getGraphDeails().get("graphType").toString();
        }
        Portal tempportal = null;
        for (Portal portal : portals) {
            Iterator<Portal> moduleIter = Iterables.filter(portals, Portal.getAccessPortalPredicate(Integer.parseInt(portalTabId))).iterator();
            if (moduleIter.hasNext()) {
                tempportal = moduleIter.next();
                break;
            }
        }

        Iterator<PortLet> moduleIterforPortlets = Iterables.filter(tempportal.getPortlets(), PortLet.getAccessPortletPredicate(Integer.parseInt(PortletId))).iterator();
        if (moduleIterforPortlets.hasNext()) {
            portLet = moduleIterforPortlets.next();
        }


        Gson gson = new Gson();
        HashMap sortHashMap = new HashMap();
        if (count != 0 && sortTypeVal != null) {
            if (countVal != 0 && countVal < 2) {
                sortHashMap.put("countVal", 10);
            } else {
                sortHashMap.put("countVal", countVal);
            }
            sortHashMap.put("sortByColumeVal", sortByColumeVal);
        } else {
            if ((Integer.parseInt(countOldVal)) != 0 && (Integer.parseInt(countOldVal)) < 2) {
                countOldVal = "10";
                sortHashMap.put("countVal", countOldVal);
            } else {
                sortHashMap.put("countVal", countOldVal);
            }
            sortHashMap.put("sortByColumeVal", sortByColumeOldVal);
        }
        sortHashMap.put("sortType", sortType);
        sortHashMap.put("isSortAll", checkval);
        String gsonstring = gson.toJson(sortHashMap);

        PortalDAO portalDAO = new PortalDAO();
        portalDAO.updatePortLetSorterHelper(PortletId, portalTabId, gsonstring);

        PortLetSorterHelper portletSorterHelper = new PortLetSorterHelper();
        if (count != 0 && sortTypeVal != null) {
            portletSorterHelper.setCountVal(countVal);
            portletSorterHelper.setSortByColumeVal(sortByColumeVal);
        } else {
            portletSorterHelper.setCountVal(Integer.parseInt(countOldVal));
            portletSorterHelper.setSortByColumeVal(sortByColumeOldVal);
        }
        portletSorterHelper.setSortType(sortType);
        portletSorterHelper.setIsSortAll(Boolean.parseBoolean(checkval));
        portLet.setPortletSorterHelper(portletSorterHelper);

        return null;
    }

    public ActionForward deleteEmptyPortlet(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException, ParseException {
        String portalTabId = request.getParameter("portalTabId");
        HttpSession session = request.getSession(false);
        LinkedHashMap portletNames = new LinkedHashMap();
        String jsonString = null;
        List<Portal> portals = (List<Portal>) session.getAttribute("PORTALS");
        List<PortLet> portlets = null;
        PortLet portLet = null;
        Portal tempportal = null;
        int id = 0;
        String name = null;
        tempportal = Iterables.find(portals, Portal.getAccessPortalPredicate(Integer.parseInt(portalTabId)));
        portlets = tempportal.getPortlets();

        for (int count = 0; count < portlets.size(); count++) {
            portLet = Iterables.find(tempportal.getPortlets(), PortLet.getAccessPortletPredicate(portlets.get(count).getPortLetId()));
            id = portlets.get(count).getPortLetId();
            name = portlets.get(count).getPortLetName();
            portletNames.put(id, name);
            Gson gson = new Gson();
            jsonString = gson.toJson(portletNames);
        }

        PrintWriter out = response.getWriter();
        out.print(jsonString);
        return null;
    }

    public ActionForward deleteSelectedPortlet(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException, ParseException {
        HttpSession session = request.getSession(false);
        String portletIdList = request.getParameter("portletIds");
        String portalTabId = request.getParameter("portalTabId");
        String portletNames = request.getParameter("portletNames");
        String userId = String.valueOf(session.getAttribute("USERID"));
        String[] portletIds = portletIdList.split(",");
        boolean portletNamesDeleted = false;
        PortalDAO pdao = new PortalDAO();
        portletNamesDeleted = pdao.deleteEmptySelectedPortlets(portletIdList, portalTabId, portletNames);
        if (portletNamesDeleted == true) {
            List<Portal> portals = (List<Portal>) session.getAttribute("PORTALS");
            PortLet portLet = null;
            Portal tempportal = null;
            tempportal = Iterables.find(portals, Portal.getAccessPortalPredicate(Integer.parseInt(portalTabId)));
            for (Portal portal : portals) {
                ArrayList<PortLet> portLets = (ArrayList<PortLet>) tempportal.getPortlets();
                for (int i = 0; i < portletIds.length; i++) {
                    Iterator<PortLet> moduleIter = Iterables.filter(tempportal.getPortlets(), PortLet.getAccessPortletPredicate(Integer.parseInt(portletIds[i]))).iterator();
                    if (moduleIter.hasNext()) {
                        portLet = moduleIter.next();
                    }
                    portLets.remove(portLet);
                }
            }
            session.setAttribute("PORTALS", portals);
//            PortalDAO portalDAO=new PortalDAO();
//                      session.setAttribute("PORTALS", portalDAO.buildPortal(Integer.parseInt(userId)));
            PrintWriter out = response.getWriter();
            out.print(portletNamesDeleted);

        }
        return null;
    }

    public ActionForward getDimensionsForFilter(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException, ParseException {
        String elementID = request.getParameter("elementID");
        String path = request.getParameter("path");
        RulesHelpDAO rdao = new RulesHelpDAO();
        String dimNamesGson = null;
        dimNamesGson = rdao.getDimensionsForFilter(elementID, path);
        PrintWriter out = response.getWriter();
        out.print(dimNamesGson);
        return null;
    }

    public ActionForward applyDimensionFilter(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException, ParseException {
        String elementID = request.getParameter("elementID");
        String dimensionName = request.getParameter("dimensionName");
        String portalTabId = request.getParameter("currentTabId");
        String folderId = request.getParameter("folderId");
        String ruleOn = "Dimension";
        PbDb pbdb = new PbDb();
        Map<String, String> reportParams = new HashMap<String, String>();
        reportParams.put(elementID, dimensionName);
        PortletRuleHelper portletRuleHelper = new PortletRuleHelper();
        portletRuleHelper.setActualRule(elementID);
        portletRuleHelper.setReportParms(reportParams);
        portletRuleHelper.setRuleOn(ruleOn);
        String qry = "select distinct PORTLET_ID from PRG_PORTAL_PORTLETS_ASSIGN where PORTL_ID=" + portalTabId;
        String busIdQry = "";
        PbReturnObject portletIds = pbdb.execSelectSQL(qry);
        List<Portal> portals = (List<Portal>) request.getSession(false).getAttribute("PORTALS");
        List<PortLet> portlets = null;
        PortLet portLet = null;
        Portal tempportal = null;
        tempportal = Iterables.find(portals, Portal.getAccessPortalPredicate(Integer.parseInt(portalTabId)));
        for (int i = 0; i < portletIds.getRowCount(); i++) {
            portlets = tempportal.getPortlets();
            tempportal.setPortletRuleHelper(portletRuleHelper);
            Iterator<PortLet> moduleIter = Iterables.filter(portlets, PortLet.getAccessPortletPredicate(Integer.parseInt(portletIds.getFieldValueString(i, 0)))).iterator();
            if (moduleIter.hasNext()) {
                portLet = moduleIter.next();
                busIdQry = "select FOLDER_ID from PRG_USER_PORTLETS_MASTER where PORTLET_ID=" + portLet.getPortLetId();
                PbReturnObject portFolderId = pbdb.execSelectSQL(busIdQry);
                for (int j = 0; j < portFolderId.getRowCount(); j++) {
                    if (folderId.equals(portFolderId.getFieldValueString(j, 0))) {
                        portLet.setPortletRuleHelper(portletRuleHelper);
                    }
                }
            }
        }
        return null;
    }

    public ActionForward getDimensionBasedOnRule(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException, ParseException {
        String folderId = request.getParameter("folderId");
        RulesHelpDAO helpDAO = new RulesHelpDAO();
        HashMap<String, String> dimensionList = new HashMap<String, String>();
        String[] colNames = null;
        String MbrName = "";
        String jsonString = null;
        String elementid = "";
        PbReturnObject dimentionObject = helpDAO.getDimentionsforRule(Integer.parseInt(folderId));
        colNames = dimentionObject.getColumnNames();
        for (int i = 0; i < dimentionObject.getRowCount(); i++) {
            MbrName = dimentionObject.getFieldValueString(i, colNames[11]);
            elementid = dimentionObject.getFieldValueString(i, colNames[1]);
            dimensionList.put(dimentionObject.getFieldValueString(i, colNames[1]), dimentionObject.getFieldValueString(i, colNames[11]));
            Gson gson = new Gson();
            jsonString = gson.toJson(dimensionList);
        }
        PrintWriter out = response.getWriter();
        out.print(jsonString);
        return null;
    }

    public ActionForward saveFilterInPortal(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException, ParseException {
        String BusinessRole = request.getParameter("BusinessRole");
        String SelectedMeasure = request.getParameter("SelectedMeasure");
        String DimName = request.getParameter("DimName");
        String userId = request.getParameter("userId");
        String portalTabId = request.getParameter("currentTabId");
        boolean portalFilterFlag = true;
        PortalDAO pdao = new PortalDAO();
        String ruleDetail = "";
        int result = 0;
        List<Portal> portals = (List<Portal>) request.getSession(false).getAttribute("PORTALS");
        Portal tempportal = null;
        Gson gson = new Gson();
        tempportal = Iterables.find(portals, Portal.getAccessPortalPredicate(Integer.parseInt(portalTabId)));
        PortletRuleHelper rulehelper = tempportal.getPortletRuleHelper();
        if (rulehelper != null) {
            ruleDetail = gson.toJson(rulehelper);
        }
        tempportal.setSavePortalFilterFlag(true);
        result = pdao.updatePortlalFilterProperties(BusinessRole, ruleDetail, portalFilterFlag, SelectedMeasure, DimName, portalTabId, userId);
        PrintWriter out = response.getWriter();
        out.print(result);
        return null;
    }

    public ActionForward getFiltervals(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException, ParseException {
        String elementID = request.getParameter("elementID");
        String qry = "select DISP_NAME from PRG_USER_ALL_INFO_DETAILS where ELEMENT_ID = " + elementID;
        PbReturnObject returnObject = new PbReturnObject();
        PbDb pbdb = new PbDb();
        returnObject = pbdb.execSelectSQL(qry);
        PrintWriter out = response.getWriter();
        out.print(returnObject.getFieldValueString(0, 0));
        return null;
    }

    public ActionForward resetGlobalFilter(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException, ParseException {
        String portalTabId = request.getParameter("currentTabId");
        String userId = request.getParameter("userId");
        String folderId = request.getParameter("folderId");
        PortalDAO pdao = new PortalDAO();
        Map<String, String> reportParams = new HashMap<String, String>();
        PortletRuleHelper portletRuleHelper = new PortletRuleHelper();
        portletRuleHelper.setActualRule("");
        portletRuleHelper.setReportParms(reportParams);
        portletRuleHelper.setRuleOn("");
        String qry = "select distinct PORTLET_ID from PRG_PORTAL_PORTLETS_ASSIGN where PORTL_ID=" + portalTabId;
        PbDb pbdb = new PbDb();
        String busIdQry = "";
        PbReturnObject portletIds = pbdb.execSelectSQL(qry);
        List<Portal> portals = (List<Portal>) request.getSession(false).getAttribute("PORTALS");
        List<PortLet> portlets = null;
        PortLet portLet = null;
        Portal tempportal = null;
        tempportal = Iterables.find(portals, Portal.getAccessPortalPredicate(Integer.parseInt(portalTabId)));
        for (int i = 0; i < portletIds.getRowCount(); i++) {
            portlets = tempportal.getPortlets();
            tempportal.setPortletRuleHelper(portletRuleHelper);
            Iterator<PortLet> moduleIter = Iterables.filter(portlets, PortLet.getAccessPortletPredicate(Integer.parseInt(portletIds.getFieldValueString(i, 0)))).iterator();
            if (moduleIter.hasNext()) {
                portLet = moduleIter.next();
                busIdQry = "select FOLDER_ID from PRG_USER_PORTLETS_MASTER where PORTLET_ID=" + portLet.getPortLetId();
                PbReturnObject portFolderId = pbdb.execSelectSQL(busIdQry);
                for (int j = 0; j < portFolderId.getRowCount(); j++) {
                    if (folderId.equals(portFolderId.getFieldValueString(j, 0))) {
                        portLet.setPortletRuleHelper(portletRuleHelper);
                    }
                }
            }
        }
        pdao.resetGlobalFilter(portalTabId, userId);
        return null;
    }

    public ActionForward getPortalName(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException, ParseException {
        String portaltabId = request.getParameter("portaltabId");
        List<Portal> portals = (List<Portal>) request.getSession(false).getAttribute("PORTALS");
        List<PortLet> portlets = null;
        PortLet portLet = null;
        Portal tempportal = null;
        tempportal = Iterables.find(portals, Portal.getAccessPortalPredicate(Integer.parseInt(portaltabId)));
        String portName = tempportal.getPortalName();
        PrintWriter out = response.getWriter();
        out.print(portName);
        return null;
    }
}
