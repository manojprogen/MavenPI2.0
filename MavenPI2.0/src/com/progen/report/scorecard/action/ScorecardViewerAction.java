/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.scorecard.action;

import com.google.common.collect.Iterables;
import com.google.gson.Gson;
import com.progen.dashboardView.bd.PbDashboardViewerBD;
import com.progen.dashboardView.db.DashboardViewerDAO;
import com.progen.report.DashletDetail;
import com.progen.report.drill.DrillMaps;
import com.progen.report.insights.InsightBaseDetails;
import com.progen.report.insights.InsightsBD;
import com.progen.report.kpi.DashletPropertiesHelper;
import com.progen.report.pbDashboardCollection;
import com.progen.report.scorecard.bd.ScorecardHistoryBuilder;
import com.progen.report.scorecard.bd.ScorecardViewerBD;
import com.progen.report.scorecard.db.ScorecardDAO;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.LookupDispatchAction;
import prg.db.Container;

/**
 *
 * @author progen
 */
public class ScorecardViewerAction extends LookupDispatchAction {

    public static Logger logger = Logger.getLogger(ScorecardViewerAction.class);

    @Override
    protected Map getKeyMethodMap() {
        Map map = new HashMap();
        map.put("getScorecardActionTypes", "getScorecardActionTypes");
        map.put("sendMail", "sendMail");
        map.put("saveNote", "saveNote");
        map.put("getPastActions", "getPastActions");
        map.put("loadScorecard", "loadScorecard");
        map.put("loadParamRegion", "loadParamRegion");
        map.put("loadScorecardHistory", "loadScorecardHistory");
        map.put("getScorecardKPIInsightViewerPage", "getScorecardKPIInsightViewerPage");
        map.put("addAdhocRows", "addAdhocRows");
        map.put("saveAdhocRows", "saveAdhocRows");
        map.put("getDashletTable", "getDashletTable");

        return map;
    }

    public ActionForward loadScorecard(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession();
        String scardId = (String) session.getAttribute("ScoreCardId");
        PrintWriter out = response.getWriter();
        ScorecardViewerBD viewerBD = new ScorecardViewerBD();
        String userId = (String) session.getAttribute("USERID");
        String dateVal = request.getParameter("dateVal");
        String cmpVal = request.getParameter("cmpVal");
        String duration = request.getParameter("duration");
        String result = viewerBD.loadScorecard(scardId, userId, dateVal, cmpVal, duration);
        out.println(result);
        return null;
    }

    public ActionForward loadScorecardHistory(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession();
        PrintWriter out = response.getWriter();
        String scardId = request.getParameter("scorecardId");
        String scardMemId = request.getParameter("scorecardMemberId");
        String userId = (String) session.getAttribute("USERID");

        String timeLevel = request.getParameter("timeLevel");
        ScorecardHistoryBuilder builder = new ScorecardHistoryBuilder();
        String result = builder.loadScorecardHistory(request, out, scardId, scardMemId, userId, timeLevel);

        out.println(result);
        return null;
    }

    public ActionForward loadParamRegion(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession();
        String scardId = (String) session.getAttribute("ScoreCardId");
        PrintWriter out = response.getWriter();
        ScorecardViewerBD viewerBD = new ScorecardViewerBD();
        String userId = (String) session.getAttribute("USERID");
        HashMap map = viewerBD.loadParams(scardId, userId);
        String result = viewerBD.displayTimeParamsForScorecard(map);
        out.println(result);
        return null;
    }

    public ActionForward getScorecardActionTypes(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        ScorecardViewerBD viewerBD = new ScorecardViewerBD();
        PrintWriter out = response.getWriter();

        String actionTypes = viewerBD.getScoreCardActionTypes();
        out.println(actionTypes);
        return null;
    }

    public ActionForward sendMail(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String scoreCardId = request.getParameter("scoreCard");
        String memberId = request.getParameter("memberId");
        String score = request.getParameter("score");
        String toAddress = request.getParameter("toAddress");
        String subject = request.getParameter("subject");
        String mailContent = request.getParameter("mailContent");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String elemName = request.getParameter("elementName");
        ScorecardViewerBD bd = new ScorecardViewerBD();

        bd.sendMail(scoreCardId, memberId, score, toAddress, subject, mailContent, startDate, endDate, elemName);
        return null;
    }

    public ActionForward saveNote(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String noteText = request.getParameter("noteContent");
        String scoreCardId = request.getParameter("scoreCard");
        String memberId = request.getParameter("memberId");
        String score = request.getParameter("score");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String elemName = request.getParameter("elementName");
        ScorecardViewerBD bd = new ScorecardViewerBD();
        bd.saveNote(scoreCardId, memberId, score, noteText, startDate, endDate, elemName);
        return null;
    }

    public ActionForward getPastActions(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        PrintWriter out = null;
        try {
            HttpSession session = request.getSession(false);
            String userId = (String) session.getAttribute("USERID");
            ScorecardViewerBD bd = new ScorecardViewerBD();
            String scoreCardId = request.getParameter("scoreCard");
            String memberId = request.getParameter("memberId");

            String actionsHtml = bd.getScoreCardActions(userId, scoreCardId, memberId);
            out = response.getWriter();
            if (actionsHtml.length() != 0) {
                out.println(actionsHtml);

            }
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        return null;
    }

    public ActionForward getScorecardKPIInsightViewerPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        String elementId = request.getParameter("elementId");
        String scorecardUrl = request.getParameter("scorecardUrl");
        String scorecardId = request.getParameter("scorecardId");
        String userId = request.getParameter("userId");
        String folderId = request.getParameter("folderId");
        String insightDimId = request.getParameter("insightDimId");
        String insightDimValue = request.getParameter("insightDimValue");
        String dateval = request.getParameter("dateval");
        String cmpval = request.getParameter("cmpval");
        String duration = request.getParameter("duration");
        String url = request.getContextPath() + "/" + scorecardUrl + scorecardId;
        InsightBaseDetails baseDetails = new InsightBaseDetails();
        ScorecardViewerBD scorecardViewerBD = new ScorecardViewerBD();
        ScorecardDAO scorecardDAO = new ScorecardDAO();
        InsightsBD insightsBD = new InsightsBD();
        ArrayList<String> dimensionIds = new ArrayList<String>();
        ArrayList<String> dimensionNames = new ArrayList<String>();
        DrillMaps drillMaps = new DrillMaps();
        ArrayList<String> dispMeasures = new ArrayList<String>();
        ArrayList<String> dispMeasuresNames = new ArrayList<String>();

        drillMaps.setElementId(new StringBuffer(elementId));
        drillMaps.setUserId(new StringBuffer(userId));
        HashMap<String, String> drillMap = drillMaps.getDrillForUser();
        HashMap<String, String> dimensionDataMap = new HashMap<String, String>();
        String[] insightDimElementIds = null;
        String[] insightDimElemtValue = null;
        String[] folderIds = new String[1];
        LinkedHashMap reportParams;
        folderIds[0] = folderId;
        if (insightDimId != null && insightDimValue != null) {
            insightDimElementIds = insightDimId.split(",");
            insightDimElemtValue = insightDimValue.split(",");
        }
        int index = 0;
        ArrayList<String> timeDetails = scorecardViewerBD.buildTimeInfo(dateval, cmpval, duration);
        HttpSession session = request.getSession(false);
        dimensionDataMap = scorecardViewerBD.getDimensions(folderIds);
//        dimensionDataMap.put("75770", "Area");
//        dimensionDataMap.put("75778", "Sales Type");
//        dimensionDataMap.put("75777", "Sales Group");
//        dimensionDataMap.put("75775", "Channel");
//        dimensionDataMap.put("75773", "BP Name");
//        dimensionDataMap.put("75772", "BP Code");

        Set<String> keySet = dimensionDataMap.keySet();
        for (String key : keySet) {
            dimensionIds.add(key);
            dimensionNames.add((String) dimensionDataMap.get(key));

        }





        HashMap insightParams = new HashMap();
        if (insightDimElementIds != null && insightDimElementIds.length > 0) {

            for (String dimelemtids : insightDimElementIds) {
//               if (dimelemtids.startsWith("A_")) {
//                   dimelemtids = dimelemtids.replace("A_", "");
//               }
                insightParams.put(dimelemtids, insightDimElemtValue[index]);
                index++;
            }
        }

        LinkedHashMap<String, String> paramsMap = new LinkedHashMap<String, String>();
        for (int i = 0; i < dimensionIds.size(); i++) {
            paramsMap.put(dimensionIds.get(i), "All");
        }
        reportParams = insightsBD.getParameters(paramsMap, insightParams);

        baseDetails.setUserId(userId);
        baseDetails.setParameters(reportParams);
        baseDetails.setDimensionIds(dimensionIds);
        baseDetails.setDimensionNames(dimensionNames);
        baseDetails.setDrillMap(drillMap);
        baseDetails.setBizRoles(folderIds);
        baseDetails.setKpiElement(insightsBD.getKPIElements(elementId));
        baseDetails.setTimeDetailsArray(timeDetails);
        baseDetails.setCallBackUrl(url);
        for (int i = 0; i < baseDetails.getKpiElement().size(); i++) {
            dispMeasures.add(baseDetails.getKpiElement().get(i).getElementId());
            dispMeasuresNames.add(baseDetails.getKpiElement().get(i).getElementName());
        }
        baseDetails.setDispMeasures(dispMeasures);
        baseDetails.setDispMeasureNames(dispMeasuresNames);
        if (!baseDetails.getKpiElement().isEmpty()) {
            baseDetails.setIndicatorMeasure(baseDetails.getKpiElement().get(baseDetails.getKpiElement().size() - 1).getElementId());
        }

        session.setAttribute("insightBaseDetails", baseDetails);

        return mapping.findForward("insightsPage");
    }

    public ActionForward addAdhocRows(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        PrintWriter out = null;
        try {
            out = response.getWriter();
            HttpSession session = request.getSession(false);
            String userId = (String) session.getAttribute("USERID");
            ScorecardDAO dao = new ScorecardDAO();
            String scoreCardId = String.valueOf(session.getAttribute("ScoreCardId"));
            String html = dao.getAdhocDetails(scoreCardId);
            out.print(html);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        return null;
    }

    public ActionForward saveAdhocRows(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        PrintWriter out = null;
        try {
            out = response.getWriter();
            ScorecardDAO dao = new ScorecardDAO();
            dao.saveAdHocRows(request);

        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        return null;
    }

    public ActionForward getDashletTable(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

        PbDashboardViewerBD dashboardViewerBD = new PbDashboardViewerBD();
        HttpSession session = request.getSession(false);
        String dashletId = request.getParameter("dashletId");
        String sortType = request.getParameter("sortType");
        String countVal = request.getParameter("countVal");
        String sortByColumeVal = request.getParameter("sortByColumeVal");
        String checkval = request.getParameter("checkval");
        Boolean flag = Boolean.parseBoolean(request.getParameter("flag"));
        Gson gson = new Gson();
        String DashProp = " ";
        DashboardViewerDAO dbrdDAO = new DashboardViewerDAO();
        if (checkval.equalsIgnoreCase("true")) {
            countVal = "0";
        }
        HashMap map = (HashMap) session.getAttribute("PROGENTABLES");
        String dashReportID = request.getParameter("PbReportId");
        Container container = (Container) map.get(dashReportID);
//        Container container = Container.getContainerFromSession(request, dashReportID);
        pbDashboardCollection collect = (pbDashboardCollection) container.getReportCollect();
        List<DashletDetail> dashletList = collect.dashletDetails;
        DashletDetail dashlet = Iterables.find(dashletList, DashletDetail.getDashletDetailPredicatBaseOnDashLetID(dashletId));
        DashletPropertiesHelper dashletPropertiesHelper = null;
        if (dashlet.getDashletPropertiesHelper() != null) {
            dashletPropertiesHelper = dashlet.getDashletPropertiesHelper();
            dashletPropertiesHelper.setSortAll(Boolean.parseBoolean(checkval));
            dashletPropertiesHelper.setCountForDisplay(Integer.parseInt(countVal));
            dashletPropertiesHelper.setDisplayOrder(sortType);
            dashletPropertiesHelper.setSortOnMeasure(sortByColumeVal);
            dashletPropertiesHelper.setElement_IDforSort(sortByColumeVal);
            dashletPropertiesHelper.setIsFromTopBottom(true);
            dashletPropertiesHelper.setDashletId(dashletId);
            dashletPropertiesHelper.setSortFlag("false");
//         DashProp = gson.toJson(dashletPropertiesHelper);
//         dbrdDAO.InsertDashletProp(dashletId,DashProp);
        } else {
            dashletPropertiesHelper = new DashletPropertiesHelper();
            dashletPropertiesHelper.setSortAll(Boolean.parseBoolean(checkval));
            dashletPropertiesHelper.setCountForDisplay(Integer.parseInt(countVal));
            dashletPropertiesHelper.setDisplayOrder(sortType);
            dashletPropertiesHelper.setSortOnMeasure(sortByColumeVal);
            dashletPropertiesHelper.setElement_IDforSort(sortByColumeVal);
            dashletPropertiesHelper.setIsFromTopBottom(true);
            dashletPropertiesHelper.setDashletId(dashletId);
            dashletPropertiesHelper.setSortFlag("false");
//         gson.toJson(dashletPropertiesHelper);
            dashlet.setDashletPropertiesHelper(dashletPropertiesHelper);
            container.setDashletPropertiesHelper(dashletPropertiesHelper);
//         DashProp = gson.toJson(dashletPropertiesHelper);
//         dbrdDAO.InsertDashletProp(dashletId,DashProp);
        }

        try {
//            dashboardViewerBD.saveDashletProperties(dashletId,sortType,countVal,sortByColumeVal,checkval);
            dashboardViewerBD.setSortType(sortType);
            dashboardViewerBD.setCountVal(Integer.parseInt(countVal));
            String htmlTable = dashboardViewerBD.buildDbrdTable(container, dashletId, false, "false", flag);
            response.getWriter().print(htmlTable);

        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }

        return null;
    }
}
