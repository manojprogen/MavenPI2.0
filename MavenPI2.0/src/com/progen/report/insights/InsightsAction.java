/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.insights;

import com.progen.reportdesigner.db.ReportTemplateDAO;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.LookupDispatchAction;

/**
 *
 * @author progen
 */
public class InsightsAction extends LookupDispatchAction {

    public static Logger logger = Logger.getLogger(InsightsAction.class);

    protected Map getKeyMethodMap() {
        Map map = new HashMap();
        map.put("getInitialInsightsTable", "getInitialInsightsTable");
        map.put("getInsightsDataForViewBy", "getInsightsDataForViewBy");
        map.put("getInsightsDrillData", "getInsightsDrillData");
        map.put("getInsightMeasures", "getInsightMeasures");
        map.put("saveInsightDrillForUser", "saveInsightDrillForUser");
        map.put("sendInsightMail", "sendInsightMail");
        map.put("getIndicatorMeasures", "getIndicatorMeasures");
        map.put("setIndicatorMeasure", "setIndicatorMeasure");
        // map.put("displayDrilldownOptions","displayDrilldownOptions");
        return map;
    }

    public ActionForward getInitialInsightsTable(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        PrintWriter out = null;
        try {
            HttpSession session = request.getSession(false);
            String table = "";
            String changedMeasures = null;
            out = response.getWriter();
            String dispType = request.getParameter("displayType");
            InsightBaseDetails baseDetails = (InsightBaseDetails) session.getAttribute("insightBaseDetails");
            InsightParams params = new InsightParams();

            changedMeasures = request.getParameter("changedMeasures");

//            if(changedMeasures!=null){
//                baseDetails.setChangedMeasures(changedMeasures);
//
//
//            }

            InsightsBD bd = new InsightsBD();
            table = bd.getInitialInsights(params, dispType, baseDetails, changedMeasures);
            out.println(table);
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }
        return null;
    }

    public ActionForward saveInsightDrillForUser(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String drillValues = request.getParameter("drillValues");
        String reportId = request.getParameter("reportId");
        InsightsBD insightsBd = new InsightsBD();

        Map<String, String> drillMap = insightsBd.makeDrillMap(drillValues);
        HttpSession session = request.getSession(false);
        InsightBaseDetails baseDetails = (InsightBaseDetails) session.getAttribute("insightBaseDetails");
        baseDetails.setDrillMap(drillMap);


        return null;
    }

    public ActionForward getInsightsDataForViewBy(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        PrintWriter out = null;
        try {
            HttpSession session = request.getSession(false);
            InsightBaseDetails baseDetails = (InsightBaseDetails) session.getAttribute("insightBaseDetails");
            InsightParams insightParams = new InsightParams();
            String table = "";
            out = response.getWriter();
            String viewById = request.getParameter("viewByDim");
            String dispType = request.getParameter("displayType");

            Map<String, String> drillMap = baseDetails.getDrillMap();
            String paramVals = request.getParameter("paramVals");
            insightParams.setParameter(paramVals);

            if (drillMap == null) {
                drillMap = baseDetails.getDrillMap();
            }
            InsightsBD bd = new InsightsBD();
            insightParams.setDimId(viewById);
            table = bd.getInsightForDimension(baseDetails, insightParams, viewById, dispType);
            out.println(table);
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }
        return null;
    }

    public ActionForward getInsightsDrillData(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        PrintWriter out = null;
        try {
            HttpSession session = request.getSession(false);
            InsightBaseDetails baseDetails = (InsightBaseDetails) session.getAttribute("insightBaseDetails");
            InsightParams insightParams = new InsightParams();
            String table = "";
            out = response.getWriter();
            String viewById = request.getParameter("viewByDim");
            String dimValue = request.getParameter("dimValue");
            String paramVals = request.getParameter("paramVals");
            String dispType = request.getParameter("displayType");
            String nextLevel = request.getParameter("nextLevel");
            String masterDimId = request.getParameter("masterDimension");
            String childDimId = request.getParameter("childDimId");
            boolean isOddLevel = false;
            if ("odd".equalsIgnoreCase(nextLevel)) {
                isOddLevel = true;
            }

            Map<String, String> drillMap = baseDetails.getDrillMap();
            insightParams.setIsOddLevel(isOddLevel);
            insightParams.setChildDimId(childDimId);
            insightParams.setMasterDimId(masterDimId);
            insightParams.setParameter(paramVals);
            insightParams.setDimValue(dimValue);
            insightParams.setDimId(viewById);

            if (drillMap == null) {
                drillMap = baseDetails.getDrillMap();
            }
            InsightsBD bd = new InsightsBD();

            table = bd.getInsightForDimensionDrill(baseDetails, insightParams, dispType);
            out.println(table);
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }
        return null;
    }

    public ActionForward getInsightMeasures(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        InsightsBD insightsBD = new InsightsBD();
        String folderId = request.getParameter("folderId");
        StringBuilder insightBuilder = new StringBuilder();
        String ctxPath = request.getParameter("ctxPath");
        InsightBaseDetails baseDetails = (InsightBaseDetails) session.getAttribute("insightBaseDetails");
        ArrayList list = new ArrayList();
        ReportTemplateDAO repDao = new ReportTemplateDAO();
        String measures = repDao.getMeasures(folderId, list, ctxPath);
        String selectedMeasures = insightsBD.getSelectedMeasures(baseDetails);
        insightBuilder.append(measures).append("|").append(selectedMeasures);
        try {
            //response.getWriter().print(measures);
            response.getWriter().print(insightBuilder.toString());
        } catch (IOException ex) {
        }
        return null;
    }

    public ActionForward sendInsightMail(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        String toAddress = request.getParameter("toAddress");
        String subject = request.getParameter("subject");
        String mailContent = request.getParameter("mailContent");
        String dimName = request.getParameter("dimName");
        String dimData = request.getParameter("dimData");
        String amount = request.getParameter("amount");
        InsightsBD bd = new InsightsBD();
        bd.sendMail(toAddress, subject, mailContent, dimName, dimData, amount, session);
        return null;
    }

    public ActionForward getIndicatorMeasures(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        InsightsBD insightsBD = new InsightsBD();
        try {
            PrintWriter out = response.getWriter();
            InsightBaseDetails baseDetails = (InsightBaseDetails) session.getAttribute("insightBaseDetails");
            String indicatorMeasures = insightsBD.getIndicatorMeasures(baseDetails);
            out.print(indicatorMeasures);
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }
        return null;
    }

    public ActionForward setIndicatorMeasure(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String indicatorId = request.getParameter("indicatorId");
        HttpSession session = request.getSession(false);
        InsightBaseDetails baseDetails = (InsightBaseDetails) session.getAttribute("insightBaseDetails");
        baseDetails.setIndicatorMeasure(indicatorId);
        return null;
    }
}
