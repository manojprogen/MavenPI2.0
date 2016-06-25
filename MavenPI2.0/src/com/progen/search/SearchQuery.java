/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.search;

import com.google.common.base.Splitter;
import com.progen.charts.ChartInfo;
import com.progen.reportdesigner.db.ReportTemplateDAO;
import com.progen.ui.messages.PrgMessage;
import java.io.*;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.LookupDispatchAction;
import org.jfree.chart.JFreeChart;
import prg.db.Container;
import prg.db.OnceViewContainer;
import prg.db.PbDb;
import prg.db.PbReturnObject;
import utils.db.ProgenConnection;

/**
 *
 * @author Administrator
 */
public class SearchQuery extends LookupDispatchAction {

    public static Logger logger = Logger.getLogger(SearchQuery.class);
    private final static String SUCCESS = "success";

    public ActionForward isSearchValid(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        String searchValues = request.getParameter("srchText");
        Iterable<String> searchKeys = Splitter.on(",").split(searchValues);

        ArrayList<String> trimmedKeys = new ArrayList<String>();

        for (String key : searchKeys) {
            trimmedKeys.add(key.trim());
        }
        SearchBd searchBd = new SearchBd();
        String userId = (String) session.getAttribute("USERID");
        try {

            Search userSearch = searchBd.isValidSearch(trimmedKeys, Integer.parseInt(userId));

            String reportId = "-999";
            if (userSearch.isValid()) {
                Container container = searchBd.prepareReportContainer(userSearch, reportId, userId);
                container.setSessionContext(session, container);
                PrintWriter out = null;

                out = response.getWriter();

                session.setAttribute("USERSEARCH", userSearch);
                if (userSearch.isDimensionOnlySearch()) {
                    out.print("DimensionOnly");
                } else {
                    out.print("ReportOnly");
                }

                //write code to insert data into database , and set search id into search class and when corresponding search is saved save with that searchid
            }
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }
        return null;
    }

    public String getrelatedreports(ArrayList<String> rowViewBy, ArrayList<String> colViewBy, ArrayList<String> qryCols) {
//        String dimEleIds = "";
        StringBuilder dimEleIds = new StringBuilder(700);
//        String measEleIds = "";
        StringBuilder measEleIds = new StringBuilder(700);
        String openLinkPath = "";
        PbDb pbdb = new PbDb();
        String dispReportsTab = "";
//        String dispRepsTab = "";
        StringBuilder dispRepsTab = new StringBuilder(700);
//        String dispDbrdTab = "";
        StringBuilder dispDbrdTab = new StringBuilder(700);
        try {
            for (String elementId : rowViewBy) {
//                dimEleIds = dimEleIds + "," + elementId;
                dimEleIds.append(",").append(elementId);
            }

            for (String elementId : colViewBy) {
//                dimEleIds = dimEleIds + "," + elementId;
                dimEleIds.append(",").append(elementId);
            }

            for (String elementId : qryCols) {
//                measEleIds = measEleIds + "," + elementId;
                measEleIds.append(",").append(elementId);
            }


            if (!dimEleIds.equals("")) {
                dimEleIds = new StringBuilder(dimEleIds.toString().substring(1));
            }
            if (!measEleIds.equals("")) {
//                measEleIds = measEleIds.substring(1);
                measEleIds = new StringBuilder(measEleIds.toString().substring(1));
            }

            String relatedReportsQry = "SELECT distinct REPORT_ID,REPORT_NAME,REPORT_DESC,REPORT_TYPE,HELP_TEXT,REPORT_STATUS from  (";
            if (!rowViewBy.isEmpty() || !colViewBy.isEmpty()) {
                relatedReportsQry += " SELECT distinct M.REPORT_ID,REPORT_NAME,REPORT_DESC,REPORT_TYPE,HELP_TEXT,REPORT_STATUS  ";
                relatedReportsQry += "FROM PRG_AR_REPORT_MASTER M,PRG_AR_REPORT_PARAM_DETAILS P ";
                relatedReportsQry += "where M.REPORT_ID = p.REPORT_ID  and P.element_id in (" + dimEleIds.toString() + ") ";

            }
            if ((!rowViewBy.isEmpty() || !colViewBy.isEmpty()) && !qryCols.isEmpty()) {
                relatedReportsQry += " INTERSECT ";
            }


            if (!qryCols.isEmpty()) {
                relatedReportsQry += " SELECT distinct M.REPORT_ID,REPORT_NAME,REPORT_DESC,REPORT_TYPE,HELP_TEXT,REPORT_STATUS ";
                relatedReportsQry += "FROM PRG_AR_REPORT_MASTER M,PRG_AR_QUERY_DETAIL Q ";
                relatedReportsQry += "where M.REPORT_ID = Q.REPORT_ID  and Q.element_id in (" + measEleIds.toString() + ")  ";

            }

            relatedReportsQry += " ) OU1 ";
            relatedReportsQry += " order by REPORT_TYPE DESC ";

            PbReturnObject relReportObj = pbdb.execSelectSQL(relatedReportsQry);

            dispReportsTab += "<Table width=\"97%\" height=\"45%\"  cellpadding=\"0\" cellspacing=\"1\" id=\"tablesorter\" class=\"tablesorter\" border=\"1\" >";
//            dispRepsTab += "<thead><tr>";
            dispRepsTab.append("<thead><tr><th width=\"70px\"><strong>Reports</strong></th></tr></thead>");
//            dispDbrdTab += "<thead><tr>";
            dispDbrdTab.append("<thead><tr><th width=\"70px\"><strong>Dashboards</strong></th></tr></thead>");
//            dispRepsTab += "<th width=\"70px\"><strong>Reports</strong></th>";
//            dispDbrdTab += "<th width=\"70px\"><strong>Dashboards</strong></th>";
//            dispRepsTab += "</tr></thead>";
//            dispDbrdTab += "</tr></thead>";
            for (int siz = 0; siz < relReportObj.getRowCount(); siz++) {
                if (siz != relReportObj.getRowCount() - 1) {
                    if (relReportObj.getFieldValueString(siz, "REPORT_TYPE").equalsIgnoreCase(relReportObj.getFieldValueString(siz + 1, "REPORT_TYPE"))) {
                        if (relReportObj.getFieldValueString(siz, "REPORT_TYPE").equalsIgnoreCase("R")) {
//                            dispRepsTab += "<tfoot></tfoot><tbody>";
                            dispRepsTab.append("<tfoot></tfoot><tbody><Tr><Td align=\"left\"><a href=\"#\" style=\"cursor:pointer;text-decoration:none;\" onclick=\"viewSrchReport('").append(openLinkPath).append("') >").append(relReportObj.getFieldValueString(siz, 1)).append("</a></Td>");
                            openLinkPath = "reportViewer.do?reportBy=viewReport&REPORTID=" + relReportObj.getFieldValueString(siz, "REPORT_ID") + "&action=open";
//                            dispRepsTab += "<Tr>";
//                            dispRepsTab += "<Td align=\"left\"><a href=\"#\" style=\"cursor:pointer;text-decoration:none;\" onclick=\"viewSrchReport('" + openLinkPath + "')\" >" + relReportObj.getFieldValueString(siz, 1) + "</a></Td>";
//                            dispRepsTab += "<Tr>";
                        } else {
//                            dispDbrdTab += "<tfoot></tfoot><tbody>";
                            dispDbrdTab.append("<tfoot></tfoot><tbody><Tr><Td align=\"left\"><a href=\"#\" style=\"cursor:pointer;text-decoration:none;\" onclick=\"viewSrchReport('").append(openLinkPath).append("')\" >").append(relReportObj.getFieldValueString(siz, 1)).append("</a></Td></Tr>");
                            openLinkPath = "dashboardViewer.do?reportBy=viewDashboard&REPORTID=" + relReportObj.getFieldValueString(siz, 0) + "&pagename=" + relReportObj.getFieldValueString(siz, 1);
//                            dispDbrdTab += "<Tr>";
//                            dispDbrdTab += "<Td align=\"left\"><a href=\"#\" style=\"cursor:pointer;text-decoration:none;\" onclick=\"viewSrchReport('" + openLinkPath + "')\" >" + relReportObj.getFieldValueString(siz, 1) + "</a></Td>";
//                            dispDbrdTab += "<Tr>";
                        }
                    } else {
                        if (relReportObj.getFieldValueString(siz + 1, "REPORT_TYPE").equalsIgnoreCase("R")) {
//                            dispRepsTab += "<tfoot></tfoot><tbody>";
                            dispRepsTab.append("<tfoot></tfoot><tbody><Tr><Td align=\"left\"><a href=\"#\" style=\"cursor:pointer;text-decoration:none;\" onclick=\"viewSrchReport('");
                            dispRepsTab.append(openLinkPath).append("')\" >").append(relReportObj.getFieldValueString(siz, 1)).append("</a></Td></tr>");
                            openLinkPath = "reportViewer.do?reportBy=viewReport&REPORTID=" + relReportObj.getFieldValueString(siz + 1, "REPORT_ID") + "&action=open";
//                            dispRepsTab += "<Tr>";
//                            dispRepsTab += "<Td align=\"left\"><a href=\"#\" style=\"cursor:pointer;text-decoration:none;\" onclick=\"viewSrchReport('" + openLinkPath + "')\" >" + relReportObj.getFieldValueString(siz, 1) + "</a></Td>";
//                            dispRepsTab += "<Tr>";
                        } else {
//                            dispDbrdTab += "<tfoot></tfoot><tbody>";
                            dispDbrdTab.append("<tfoot></tfoot><tbody><Tr><Td align=\"left\"><a href=\"#\" style=\"cursor:pointer;text-decoration:none;\" onclick=\"viewSrchReport('").append(openLinkPath).append("')\" >").append(relReportObj.getFieldValueString(siz, 1)).append("</a></Td>");
                            openLinkPath = "dashboardViewer.do?reportBy=viewDashboard&REPORTID=" + relReportObj.getFieldValueString(siz, 0) + "&pagename=" + relReportObj.getFieldValueString(siz, 1);
//                            dispDbrdTab += "<Tr>";
//                            dispDbrdTab += "<Td align=\"left\"><a href=\"#\" style=\"cursor:pointer;text-decoration:none;\" onclick=\"viewSrchReport('" + openLinkPath + "')\" >" + relReportObj.getFieldValueString(siz, 1) + "</a></Td>";
//                            dispDbrdTab += "<Tr>";
                        }

                    }

                } else {
                    if (relReportObj.getFieldValueString(siz, "REPORT_TYPE").equalsIgnoreCase("R")) {
//                        dispRepsTab += "<tfoot></tfoot><tbody>";
                        dispRepsTab.append("<tfoot></tfoot><tbody><Tr><Td align=\"left\"><a href=\"#\" style=\"cursor:pointer;text-decoration:none;\" onclick=\"viewSrchReport('").append(openLinkPath).append("')\" >").append(relReportObj.getFieldValueString(siz, 1)).append("</a></Td></Tr>");
                        openLinkPath = "reportViewer.do?reportBy=viewReport&REPORTID=" + relReportObj.getFieldValueString(siz, "REPORT_ID") + "&action=open";
//                        dispRepsTab += "<Tr>";
//                        dispRepsTab += "<Td align=\"left\"><a href=\"#\" style=\"cursor:pointer;text-decoration:none;\" onclick=\"viewSrchReport('" + openLinkPath + "')\" >" + relReportObj.getFieldValueString(siz, 1) + "</a></Td>";
//                        dispRepsTab += "</Tr>";
                    } else {
//                        dispDbrdTab += "<tfoot></tfoot><tbody>";
                        dispDbrdTab.append("<tfoot></tfoot><tbody><Tr><Td align=\"left\"><a href=\"#\" style=\"cursor:pointer;text-decoration:none;\" onclick=\"viewSrchReport('").append(openLinkPath).append("')\" >").append(relReportObj.getFieldValueString(siz, 1)).append("</a></Td></Tr>");
                        openLinkPath = "dashboardViewer.do?reportBy=viewDashboard&REPORTID=" + relReportObj.getFieldValueString(siz, 0) + "&pagename=" + relReportObj.getFieldValueString(siz, 1);
//                        dispDbrdTab += "<Tr>";
//                        dispDbrdTab += "<Td align=\"left\"><a href=\"#\" style=\"cursor:pointer;text-decoration:none;\" onclick=\"viewSrchReport('" + openLinkPath + "')\" >" + relReportObj.getFieldValueString(siz, 1) + "</a></Td>";
//                        dispDbrdTab += "</Tr>";
                    }
                }
            }
            dispReportsTab += dispRepsTab.toString();
            dispReportsTab += dispDbrdTab.toString();
            dispReportsTab += "</tbody>";
            dispReportsTab += "</Table>";
        } catch (Exception exp) {
            logger.error("Exception:", exp);
        }

        return dispReportsTab;
    }

    public ActionForward getRelatedReports(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        Search userSearch = (Search) session.getAttribute("USERSEARCH");

        String relatedReports = this.getrelatedreports(userSearch.getDimensionElementsRowViewBy(),
                userSearch.getDimensionElementsColumnViewBy(),
                userSearch.getMeasureElements());

        try {
            response.getWriter().print(relatedReports);
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }

        return null;
    }

    public ActionForward checkReportName(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String reportName = request.getParameter("reportName");
        String status = "";
//        String roleId;
        String userId;
        HttpSession session = request.getSession(false);
        if (session != null) {
            userId = String.valueOf(session.getAttribute("USERID"));
            Search userSearch = (Search) session.getAttribute("USERSEARCH");
            ReportTemplateDAO tempDAO = new ReportTemplateDAO();
            boolean exists = tempDAO.checkReportNameExists(reportName, userId, userSearch.folderId.toString());
            if (exists) {
                status = "Report Exists";
            }
            try {
                response.getWriter().print(status);
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
            return null;
        } else {
            return mapping.findForward("sessionExpired");
        }
    }

    public ActionForward createReport(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String reportName = request.getParameter("reportName");
        String reportDesc = request.getParameter("reportDesc");
        String reportId;
        HttpSession session = request.getSession(false);

        if (session != null) {
//            Search userSearch = (Search) session.getAttribute("USERSEARCH");
            String userId = String.valueOf(session.getAttribute("USERID"));
            Container container = Container.getContainerFromSession(request, "-999");
            SearchBd searchBd = new SearchBd();
            reportId = searchBd.createReport(container, userId, reportName, reportDesc);

            try {
                response.getWriter().print(reportId);
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
            return null;
        } else {
            return mapping.findForward("sessionExpired");
        }
    }

    public ActionForward getKpis(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            Container container = Container.getContainerFromSession(request, "-999");
            SearchBd searchBd = new SearchBd();
            String timeLevel = request.getParameter("timeLevel");
            String userId = String.valueOf(session.getAttribute("USERID"));
            String kpiHtml = searchBd.processKpi(container, timeLevel, userId);
            try {
                response.getWriter().print(kpiHtml);
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
            return null;
        } else {
            return mapping.findForward("sessionExpired");
        }
    }

    public ActionForward getRelatedTrendMeasures(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);

        if (session != null) {
            String ctxPath = request.getContextPath();

            Container container = Container.getContainerFromSession(request, "-999");
            SearchBd searchBd = new SearchBd();
//            Search userSearch = (Search) session.getAttribute("USERSEARCH");
            String userId = String.valueOf(session.getAttribute("USERID"));
            PbReturnObject trendMeasData = searchBd.getRelatedTrendMeasureData(container, userId);
//            String relatedMeasHtml = searchBd.buildRelatedTrendMesuresUI(container, trendMeasData);
            String relatedMeasHtml = "";

            try {
                response.getWriter().print(relatedMeasHtml);
                StringBuilder chartHtml = this.generateTimeSeriesChart(container, trendMeasData, ctxPath, session, response);
                response.getWriter().print(chartHtml.toString());
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
            return null;
        } else {
            return mapping.findForward("sessionExpired");
        }
    }

    public ActionForward autoSuggest(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            String qryString = request.getParameter("query");


            SearchBd searchBd = new SearchBd();

            String acJson = searchBd.formulateAutoSuggestJson(qryString);

            try {
                response.getWriter().print(acJson);
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
            return null;
        } else {
            return mapping.findForward("sessionExpired");
        }
    }

    public ActionForward getsearchPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession(false);
        session.setAttribute("ViewFrom", "Search");
        return mapping.findForward("searchpage");

    }

    private StringBuilder generateTimeSeriesChart(Container container,
            PbReturnObject retObj,
            String ctxPath,
            HttpSession session,
            HttpServletResponse response) {
        StringBuilder chartHtml = new StringBuilder();
        int monthCount = retObj.getRowCount();
        ChartInfo chartInfo = new ChartInfo();
        int measCount;
        if (container.getReportMeasureCount() >= 2) {
            measCount = 2;
        } else {
            measCount = 1;
        }

        chartHtml.append("<table>");
        for (int i = 0; i < measCount; i++) {
            String measure = container.getReportMeasureNames().get(i);
            JFreeChart chart = chartInfo.generateChartDataSet(retObj, measure, i + 1, "", "", false, false);

            chartHtml.append("<tr>");
//                chartHtml.append("<td class='subTotalCell'>").append(measure).append(" Last ").append(monthCount).append(" months</td></tr>");
            chartHtml.append("<td class='subTotalCell'>").append(measure).append(" Last ").append(monthCount).append(" years</td></tr>");
            String chartPath = null;
            try {
                chartPath = chartInfo.generateChartPath(session, response.getWriter(), chart);
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
            chartHtml.append("<tr><td>");
            String chartDisplay = "<div align='center' id=\"overDiv\" style=\"position:absolute;z-index:1000;\"></div>";
            chartDisplay += "<center><img align='top' usemap=\"#" + "\"  src=\"" + ctxPath + "/" + chartPath + "\" border='0' > </img></center>";
            chartHtml.append(chartDisplay);
            chartHtml.append("</td>");
            chartHtml.append("</tr>");
            if (i == container.getReportMeasureNames().size() - 1) {
                chartHtml.append("</table>");
            }
        }
        return chartHtml;
    }

    public ActionForward saveFavSearch(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        String searchName = request.getParameter("searchName");
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession(false);
        String userId = (String) session.getAttribute("USERID");
        Search userSearch = (Search) session.getAttribute("USERSEARCH");
        SearchBd searchBd = new SearchBd();
        SearchConstants mesg = searchBd.saveAsFavoriteSearch(userSearch, searchName, Integer.parseInt(userId));
        PrgMessage uiMsg = new PrgMessage(mesg.getErrorCode(), mesg.getMessageCode());
        uiMsg.setMesgTxt(mesg.getMessageCode());

        out.print(uiMsg.getMessageJSON());
        return null;
    }

    public ActionForward getFavSearch(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        SearchBd searchBd = new SearchBd();
        PrintWriter out = response.getWriter();
        ArrayList<String> searchName = new ArrayList<String>();
        ArrayList<String> searchText = new ArrayList<String>();
        HashMap<String, String> favSearch = searchBd.getFavSearchData();
        Set<String> keys = favSearch.keySet();
        for (String keyValue : keys) {
            searchName.add(keyValue);
            searchText.add(favSearch.get(keyValue));
        }
        StringBuilder html = new StringBuilder();
        //html.append("<br>");
        for (int i = 0; i < searchName.size(); i++) {

            html.append("<tr style=\"width:100%\"><td width=\"40%\"><a href=\"javascript:getFavSearch(\'").append(searchText.get(i)).append("\')").append("\">").append(searchName.get(i)).append("</a></td>");
            html.append("<td width=\"50%\">").append(searchText.get(i)).append("</td>");
            html.append("<td width=\"10%\" align=\"center\">").append("<input  class=\"navtitle-hover\" type=\"button\" value=\"Delete\" onclick=\"javascript:deleteFavSearch(\'").append(searchName.get(i)).append("\',").append(i).append(")").append("\"/>").append("</td>").append("</tr>");
        }
        out.print(html.toString());
        return null;
    }

    public ActionForward deleteFavSearch(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String searchName = request.getParameter("searchName");
        SearchBd searchBd = new SearchBd();
        searchBd.deleteFavSearch(searchName);
        return null;
    }

    public ActionForward isMobileSearchValid(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        String searchValues = request.getParameter("srchText");
        String srchVal = "";


        Iterable<String> searchKeys = Splitter.on(",").split(searchValues);

        ArrayList<String> trimmedKeys = new ArrayList<String>();
        for (String key : searchKeys) {
            trimmedKeys.add(key.trim());
        }

        SearchBd searchBd = new SearchBd();
        String userId = (String) session.getAttribute("USERID");
        Search userSearch = searchBd.isValidSearch(trimmedKeys, Integer.parseInt(userId));

        if (userSearch.isValid()) {
            String reportId = "-999";
            Container container = searchBd.prepareReportContainer(userSearch, reportId, userId);
            container.setSessionContext(session, container);
            String kpiHtml = searchBd.processKpi(container, "MONTH", userId);
            request.setAttribute("mobilesrchVals", searchValues);
            request.setAttribute("mobileKpi", kpiHtml);
            srchVal = "mobSearch";
        } else {
            srchVal = "mobSearch";
            String noDataMsg = "No Data found, Please Redefine your Search";
            request.setAttribute("mobilesrchVals", searchValues);
            request.setAttribute("mobileKpi", noDataMsg);
        }
        return mapping.findForward(srchVal);
    }
//Surender

    public ActionForward oneViewBy(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession(false);
        boolean homeFlag = Boolean.parseBoolean(request.getParameter("homeFlag"));
        String oneViewId = String.valueOf(request.getParameter("oneViewId"));
        String oneviewname = String.valueOf(request.getParameter("oneviewname"));
        String assignFlag = (String) session.getAttribute("assignFlagsequence");
        ReportTemplateDAO dao = new ReportTemplateDAO();
        PbReturnObject retOjb = null;
        PbReturnObject retOjb1 = null;
        PbReturnObject retOjb2 = null;
        PbReturnObject retOjb3 = null;
        OnceViewContainer onecontainer = null;
        String userId = String.valueOf(session.getAttribute("USERID"));
        retOjb = dao.getAllOneViewBys(userId);
        if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.MYSQL)) {
            retOjb1 = dao.getOneViewsBasedOnSequence1(userId);
            retOjb3 = dao.getOneViewsBasedOnSequencemysql(userId);
        }
        retOjb2 = dao.getOneViewsBasedOnSequence(userId);
        if (retOjb1 == null) {
            retOjb1 = retOjb2;
        }
        if (assignFlag != null && assignFlag.equalsIgnoreCase("sequence")) {
            retOjb1 = retOjb;
            if (session.getAttribute("assignFlagsequence") != null) {
                session.removeAttribute("assignFlagsequence");
            }
        }

        List<String> viewByNames = new ArrayList<String>();
        List<String> viewByIds = new ArrayList<String>();
        List<String> createdDates = new ArrayList<String>();
        List<String> createdBy = new ArrayList<String>();
        List<String> modifiedDates = new ArrayList<String>();
        List<String> modifiedBy = new ArrayList<String>();
        List<String> viewroleIds = new ArrayList<String>();
        List<String> viewroleNames = new ArrayList<String>();
        List<String> createdByNames = new ArrayList<String>();
        List<String> modifiedByNames = new ArrayList<String>();

        List<String> viewByNamesSeq = new ArrayList<String>();
        List<String> viewByIdsSeq = new ArrayList<String>();
        List<String> folderIds = new ArrayList<String>();
        List<String> folderNames = new ArrayList<String>();
        if (retOjb1 != null && retOjb1.rowCount > 0) {
//         for(int i=0;i<retOjb1.rowCount;i++){
            if (retOjb3 != null && retOjb3.rowCount > 0) {
                for (int j = 0; j < retOjb3.rowCount; j++) {
                    for (int i = 0; i < retOjb1.rowCount; i++) {
                        String oneid = Integer.toString(retOjb3.getFieldValueInt(j, 0));
                        String oneid1 = Integer.toString(retOjb1.getFieldValueInt(i, 0));

                        if (oneid == null ? oneid1 == null : oneid.equals(oneid1)) {
                            viewByIds.add(Integer.toString(retOjb1.getFieldValueInt(i, 0)));
                            viewByNames.add(retOjb1.getFieldValueString(i, 4));
                            createdDates.add(retOjb1.getFieldValueString(i, 3));
                            createdBy.add(retOjb1.getFieldValueString(i, 6));
                            if (!createdBy.isEmpty()) {
                                createdByNames.add(dao.getUserNames(createdBy.get(j)));
                            }
                            modifiedDates.add(retOjb1.getFieldValueString(i, 5));
                            modifiedBy.add(retOjb1.getFieldValueString(i, 7));
                            if (!modifiedBy.isEmpty()) {
                                modifiedByNames.add(dao.getUserNames(modifiedBy.get(j)));
                            }
                            viewroleIds.add(retOjb1.getFieldValueString(i, 8));
                            if (!modifiedBy.isEmpty()) {
                                viewroleNames.add(dao.getRoleNames(viewroleIds.get(j), modifiedBy.get(j)));

                            }
                        }
                    }
                }
            } else {
                for (int i = 0; i < retOjb1.rowCount; i++) {
                    viewByIds.add(Integer.toString(retOjb1.getFieldValueInt(i, 0)));
                    viewByNames.add(retOjb1.getFieldValueString(i, 4));
                    createdDates.add(retOjb1.getFieldValueString(i, 3));
                    createdBy.add(retOjb1.getFieldValueString(i, 6));
                    if (!createdBy.isEmpty()) {
                        createdByNames.add(dao.getUserNames(createdBy.get(i)));
                    }
                    modifiedDates.add(retOjb1.getFieldValueString(i, 5));
                    modifiedBy.add(retOjb1.getFieldValueString(i, 7));
                    if (!modifiedBy.isEmpty()) {
                        modifiedByNames.add(dao.getUserNames(modifiedBy.get(i)));
                    }
                    viewroleIds.add(retOjb1.getFieldValueString(i, 8));
                    if (!modifiedBy.isEmpty()) {
                        viewroleNames.add(dao.getRoleNames(viewroleIds.get(i), modifiedBy.get(i)));
                    }
                }
            }
            for (int i = 0; i < retOjb1.rowCount; i++) {
                viewByIdsSeq.add(Integer.toString(retOjb1.getFieldValueInt(i, 0)));
                viewByNamesSeq.add(retOjb1.getFieldValueString(i, 4));
            }
            retOjb1 = dao.getBurolsByUserId(userId);

            HashMap<String, String> map = new HashMap<String, String>();
            String FolderId = "";
            String FolderName = "";
            String[] colNames = null;
//        List<String>folderIds=new ArrayList<String>();
//        List<String>folderNames=new ArrayList<String>();

            if (retOjb1 != null && retOjb1.getRowCount() != 0) {
                colNames = retOjb1.getColumnNames();
                for (int i = 0; i < retOjb1.getRowCount(); i++) {
                    FolderId = retOjb1.getFieldValueString(i, colNames[0]);
                    FolderName = retOjb1.getFieldValueString(i, colNames[1]);
                    folderIds.add(FolderId);
                    folderNames.add(FolderName);
//                    map.put(FolderId, FolderName);
                }
            }
        } else {

            for (int i = 0; i < retOjb.rowCount; i++) {
                viewByIds.add(Integer.toString(retOjb.getFieldValueInt(i, 0)));
                viewByNames.add(retOjb.getFieldValueString(i, 4));
                createdDates.add(retOjb.getFieldValueString(i, 3));
                createdBy.add(retOjb.getFieldValueString(i, 6));
                if (!createdBy.isEmpty()) {
                    createdByNames.add(dao.getUserNames(createdBy.get(i)));
                }
                modifiedDates.add(retOjb.getFieldValueString(i, 5));
                modifiedBy.add(retOjb.getFieldValueString(i, 7));
                if (!modifiedBy.isEmpty()) {
                    modifiedByNames.add(dao.getUserNames(modifiedBy.get(i)));
                }
                viewroleIds.add(retOjb.getFieldValueString(i, 8));
                if (!modifiedBy.isEmpty()) {
                    viewroleNames.add(dao.getRoleNames(viewroleIds.get(i), modifiedBy.get(i)));
                }
            }
            for (int i = 0; i < retOjb.rowCount; i++) {
                viewByIdsSeq.add(Integer.toString(retOjb.getFieldValueInt(i, 0)));
                viewByNamesSeq.add(retOjb.getFieldValueString(i, 4));
            }
            retOjb = dao.getBurolsByUserId(userId);

            HashMap<String, String> map = new HashMap<String, String>();
            String FolderId = "";
            String FolderName = "";
            String[] colNames = null;
//        List<String>folderIds=new ArrayList<String>();
//        List<String>folderNames=new ArrayList<String>();

            if (retOjb != null && retOjb.getRowCount() != 0) {
                colNames = retOjb.getColumnNames();
                for (int i = 0; i < retOjb.getRowCount(); i++) {
                    FolderId = retOjb.getFieldValueString(i, colNames[0]);
                    FolderName = retOjb.getFieldValueString(i, colNames[1]);
                    folderIds.add(FolderId);
                    folderNames.add(FolderName);
//                    map.put(FolderId, FolderName);
                }
            }

        }
        if (session.getAttribute("MeasureDrillTest") != null) {
            session.removeAttribute("MeasureDrillTest");
        }
        if (session.getAttribute("ONEVIEWDETAILS") != null) {
            session.removeAttribute("ONEVIEWDETAILS");
        }

        request.setAttribute("BusRoleIds", folderIds);
        request.setAttribute("BusRoleNames", folderNames);
        request.setAttribute("ViewByIds", viewByIds);
        request.setAttribute("viewByNames", viewByNames);
        request.setAttribute("createdDates", createdDates);
        request.setAttribute("createdBy", createdByNames);
        request.setAttribute("modifiedDates", modifiedDates);
        request.setAttribute("modifiedBy", modifiedByNames);
        request.setAttribute("viewroleIds", viewroleIds);
        request.setAttribute("viewroleNames", viewroleNames);
        request.setAttribute("homeFlag", homeFlag);
        request.setAttribute("oneViewId", oneViewId);
        request.setAttribute("oneviewname", oneviewname);
        request.setAttribute("viewByIdsSeq", viewByIdsSeq);
        request.setAttribute("viewByNamesSeq", viewByNamesSeq);
//if (session != null){
//        
//        
        if ((request.getParameter("hmflag") != null) && request.getParameter("hmflag").equalsIgnoreCase("true")) {
            if (session.getAttribute("tempFileName") != null && session.getAttribute("advHtmlFileProps") != null) {
                File file = new File(session.getAttribute("advHtmlFileProps").toString() + "/" + session.getAttribute("tempFileName").toString());
                if (file.exists()) {
                    FileInputStream fis = new FileInputStream(session.getAttribute("advHtmlFileProps").toString() + "/" + session.getAttribute("tempFileName").toString());
                    ObjectInputStream ois = new ObjectInputStream(fis);
                    onecontainer = (OnceViewContainer) ois.readObject();
                    ois.close();
                    if (onecontainer.getTempRegHashMap() != null) {
                        for (int i = 0; i < onecontainer.onviewLetdetails.size(); i++) {
                            File file1 = new File(session.getAttribute("advHtmlFileProps").toString() + "/" + onecontainer.getTempRegHashMap().get(i));
                            file1.delete();
                        }
                    }
                }
                file.delete();
//        }
            }
//        
        }
//        }
        return mapping.findForward("oneViewBY");

    }

    public ActionForward editWall(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession(false);
        session.setAttribute("fromWall", "edit");
        boolean homeFlag = Boolean.parseBoolean(request.getParameter("homeFlag"));
        String oneViewId = String.valueOf(request.getParameter("oneViewId"));
        String oneviewname = String.valueOf(request.getParameter("oneviewname"));
        String assignFlag = (String) session.getAttribute("assignFlagsequence");
        ReportTemplateDAO dao = new ReportTemplateDAO();
        PbReturnObject retOjb = null;
        PbReturnObject retOjb1 = null;
        PbReturnObject retOjb2 = null;
        PbReturnObject retOjb3 = null;
        OnceViewContainer onecontainer = null;
        String userId = String.valueOf(session.getAttribute("USERID"));
        retOjb = dao.getAllOneViewBys(userId);
        if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.MYSQL)) {
            retOjb1 = dao.getOneViewsBasedOnSequence1(userId);
            retOjb3 = dao.getOneViewsBasedOnSequencemysql(userId);
        }
        retOjb2 = dao.getOneViewsBasedOnSequence(userId);
        if (retOjb1 == null) {
            retOjb1 = retOjb2;
        }
        if (assignFlag != null && assignFlag.equalsIgnoreCase("sequence")) {
            retOjb1 = retOjb;
            if (session.getAttribute("assignFlagsequence") != null) {
                session.removeAttribute("assignFlagsequence");
            }
        }

        List<String> viewByNames = new ArrayList<String>();
        List<String> viewByIds = new ArrayList<String>();
        List<String> createdDates = new ArrayList<String>();
        List<String> createdBy = new ArrayList<String>();
        List<String> modifiedDates = new ArrayList<String>();
        List<String> modifiedBy = new ArrayList<String>();
        List<String> viewroleIds = new ArrayList<String>();
        List<String> viewroleNames = new ArrayList<String>();
        List<String> createdByNames = new ArrayList<String>();
        List<String> modifiedByNames = new ArrayList<String>();

        List<String> viewByNamesSeq = new ArrayList<String>();
        List<String> viewByIdsSeq = new ArrayList<String>();
        List<String> folderIds = new ArrayList<String>();
        List<String> folderNames = new ArrayList<String>();
        if (retOjb1 != null && retOjb1.rowCount > 0) {
//         for(int i=0;i<retOjb1.rowCount;i++){
            if (retOjb3 != null && retOjb3.rowCount > 0) {
                for (int j = 0; j < retOjb3.rowCount; j++) {
                    for (int i = 0; i < retOjb1.rowCount; i++) {
                        String oneid = Integer.toString(retOjb3.getFieldValueInt(j, 0));
                        String oneid1 = Integer.toString(retOjb1.getFieldValueInt(i, 0));

                        if (oneid == null ? oneid1 == null : oneid.equals(oneid1)) {
                            viewByIds.add(Integer.toString(retOjb1.getFieldValueInt(i, 0)));
                            viewByNames.add(retOjb1.getFieldValueString(i, 4));
                            createdDates.add(retOjb1.getFieldValueString(i, 3));
                            createdBy.add(retOjb1.getFieldValueString(i, 6));
                            if (!createdBy.isEmpty()) {
                                createdByNames.add(dao.getUserNames(createdBy.get(j)));
                            }
                            modifiedDates.add(retOjb1.getFieldValueString(i, 5));
                            modifiedBy.add(retOjb1.getFieldValueString(i, 7));
                            if (!modifiedBy.isEmpty()) {
                                modifiedByNames.add(dao.getUserNames(modifiedBy.get(j)));
                            }
                            viewroleIds.add(retOjb1.getFieldValueString(i, 8));
                            if (!modifiedBy.isEmpty()) {
                                viewroleNames.add(dao.getRoleNames(viewroleIds.get(j), modifiedBy.get(j)));

                            }
                        }
                    }
                }
            } else {
                for (int i = 0; i < retOjb1.rowCount; i++) {
                    viewByIds.add(Integer.toString(retOjb1.getFieldValueInt(i, 0)));
                    viewByNames.add(retOjb1.getFieldValueString(i, 4));
                    createdDates.add(retOjb1.getFieldValueString(i, 3));
                    createdBy.add(retOjb1.getFieldValueString(i, 6));
                    if (!createdBy.isEmpty()) {
                        createdByNames.add(dao.getUserNames(createdBy.get(i)));
                    }
                    modifiedDates.add(retOjb1.getFieldValueString(i, 5));
                    modifiedBy.add(retOjb1.getFieldValueString(i, 7));
                    if (!modifiedBy.isEmpty()) {
                        modifiedByNames.add(dao.getUserNames(modifiedBy.get(i)));
                    }
                    viewroleIds.add(retOjb1.getFieldValueString(i, 8));
                    if (!modifiedBy.isEmpty()) {
                        viewroleNames.add(dao.getRoleNames(viewroleIds.get(i), modifiedBy.get(i)));
                    }
                }
            }
            for (int i = 0; i < retOjb1.rowCount; i++) {
                viewByIdsSeq.add(Integer.toString(retOjb1.getFieldValueInt(i, 0)));
                viewByNamesSeq.add(retOjb1.getFieldValueString(i, 4));
            }
            retOjb1 = dao.getBurolsByUserId(userId);

            HashMap<String, String> map = new HashMap<String, String>();
            String FolderId = "";
            String FolderName = "";
            String[] colNames = null;
//        List<String>folderIds=new ArrayList<String>();
//        List<String>folderNames=new ArrayList<String>();

            if (retOjb1 != null && retOjb1.getRowCount() != 0) {
                colNames = retOjb1.getColumnNames();
                for (int i = 0; i < retOjb1.getRowCount(); i++) {
                    FolderId = retOjb1.getFieldValueString(i, colNames[0]);
                    FolderName = retOjb1.getFieldValueString(i, colNames[1]);
                    folderIds.add(FolderId);
                    folderNames.add(FolderName);
//                    map.put(FolderId, FolderName);
                }
            }
        } else {

            for (int i = 0; i < retOjb.rowCount; i++) {
                viewByIds.add(Integer.toString(retOjb.getFieldValueInt(i, 0)));
                viewByNames.add(retOjb.getFieldValueString(i, 4));
                createdDates.add(retOjb.getFieldValueString(i, 3));
                createdBy.add(retOjb.getFieldValueString(i, 6));
                if (!createdBy.isEmpty()) {
                    createdByNames.add(dao.getUserNames(createdBy.get(i)));
                }
                modifiedDates.add(retOjb.getFieldValueString(i, 5));
                modifiedBy.add(retOjb.getFieldValueString(i, 7));
                if (!modifiedBy.isEmpty()) {
                    modifiedByNames.add(dao.getUserNames(modifiedBy.get(i)));
                }
                viewroleIds.add(retOjb.getFieldValueString(i, 8));
                if (!modifiedBy.isEmpty()) {
                    viewroleNames.add(dao.getRoleNames(viewroleIds.get(i), modifiedBy.get(i)));
                }
            }
            for (int i = 0; i < retOjb.rowCount; i++) {
                viewByIdsSeq.add(Integer.toString(retOjb.getFieldValueInt(i, 0)));
                viewByNamesSeq.add(retOjb.getFieldValueString(i, 4));
            }
            retOjb = dao.getBurolsByUserId(userId);

            HashMap<String, String> map = new HashMap<String, String>();
            String FolderId = "";
            String FolderName = "";
            String[] colNames = null;
//        List<String>folderIds=new ArrayList<String>();
//        List<String>folderNames=new ArrayList<String>();

            if (retOjb != null && retOjb.getRowCount() != 0) {
                colNames = retOjb.getColumnNames();
                for (int i = 0; i < retOjb.getRowCount(); i++) {
                    FolderId = retOjb.getFieldValueString(i, colNames[0]);
                    FolderName = retOjb.getFieldValueString(i, colNames[1]);
                    folderIds.add(FolderId);
                    folderNames.add(FolderName);
//                    map.put(FolderId, FolderName);
                }
            }

        }
        if (session.getAttribute("MeasureDrillTest") != null) {
            session.removeAttribute("MeasureDrillTest");
        }
        if (session.getAttribute("ONEVIEWDETAILS") != null) {
            session.removeAttribute("ONEVIEWDETAILS");
        }

        request.setAttribute("BusRoleIds", folderIds);
        request.setAttribute("BusRoleNames", folderNames);
        request.setAttribute("ViewByIds", viewByIds);
        request.setAttribute("viewByNames", viewByNames);
        request.setAttribute("createdDates", createdDates);
        request.setAttribute("createdBy", createdByNames);
        request.setAttribute("modifiedDates", modifiedDates);
        request.setAttribute("modifiedBy", modifiedByNames);
        request.setAttribute("viewroleIds", viewroleIds);
        request.setAttribute("viewroleNames", viewroleNames);
        request.setAttribute("homeFlag", homeFlag);
        request.setAttribute("oneViewId", oneViewId);
        request.setAttribute("oneviewname", oneviewname);
        request.setAttribute("viewByIdsSeq", viewByIdsSeq);
        request.setAttribute("viewByNamesSeq", viewByNamesSeq);
//if (session != null){
//        
//        
        if ((request.getParameter("hmflag") != null) && request.getParameter("hmflag").equalsIgnoreCase("true")) {
            if (session.getAttribute("tempFileName") != null && session.getAttribute("advHtmlFileProps") != null) {
                File file = new File(session.getAttribute("advHtmlFileProps").toString() + "/" + session.getAttribute("tempFileName").toString());
                if (file.exists()) {
                    FileInputStream fis = new FileInputStream(session.getAttribute("advHtmlFileProps").toString() + "/" + session.getAttribute("tempFileName").toString());
                    ObjectInputStream ois = new ObjectInputStream(fis);
                    onecontainer = (OnceViewContainer) ois.readObject();
                    ois.close();
                    if (onecontainer.getTempRegHashMap() != null) {
                        for (int i = 0; i < onecontainer.onviewLetdetails.size(); i++) {
                            File file1 = new File(session.getAttribute("advHtmlFileProps").toString() + "/" + onecontainer.getTempRegHashMap().get(i));
                            file1.delete();
                        }
                    }
                }
                file.delete();
//        }
            }
//        
        }
        return mapping.findForward("editWall");

    }

    public ActionForward icalPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        boolean icalhomeFlag = Boolean.parseBoolean(request.getParameter("icalhomeFlag"));
        String icalId = String.valueOf(request.getParameter("icalId"));
        String icalName = String.valueOf(request.getParameter("icalName"));
        request.setAttribute("icalhomeFlag", icalhomeFlag);
        request.setAttribute("icalId", icalId);
        request.setAttribute("icalName", icalName);
        return mapping.findForward("icalPage");
    }

    public ActionForward groupMeassure(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {

        return mapping.findForward("groupMeassure");
    }

    public ActionForward pbBiManager(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {

        return mapping.findForward("pbBiManager");
    }

    public ActionForward headlinePage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {

        return mapping.findForward("headlineHome");
    }

    protected Map getKeyMethodMap() {
        Map map = new HashMap();
        map.put("getsearchPage", "getsearchPage");
        map.put("isSearchValid", "isSearchValid");
        map.put("checkReportName", "checkReportName");
        map.put("createReport", "createReport");
        map.put("getRelatedReports", "getRelatedReports");
        map.put("getKpis", "getKpis");
        map.put("getRelatedTrendMeasures", "getRelatedTrendMeasures");
        map.put("autoSuggest", "autoSuggest");
//for mobile search
        map.put("isMobileSearchValid", "isMobileSearchValid");
        map.put("saveFavSearch", "saveFavSearch");
        map.put("getFavSearch", "getFavSearch");
        map.put("deleteFavSearch", "deleteFavSearch");
        map.put("oneViewBy", "oneViewBy");
        map.put("icalPage", "icalPage");
        map.put("groupMeassure", "groupMeassure");
        map.put("pbBiManager", "pbBiManager");
        map.put("headlinePage", "headlinePage");
        map.put("editWall", "editWall");
        return map;
    }
}
