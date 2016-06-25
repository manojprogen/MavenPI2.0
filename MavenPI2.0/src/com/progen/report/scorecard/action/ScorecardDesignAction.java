/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.scorecard.action;

import com.google.common.base.Function;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.gson.Gson;
import com.progen.report.scorecard.*;
import com.progen.report.scorecard.bd.ScorecardDesignBd;
import com.progen.report.scorecard.db.ScorecardDAO;
import com.progen.reportdesigner.db.ReportTemplateDAO;
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

/**
 *
 * @author progen
 */
public class ScorecardDesignAction extends LookupDispatchAction {

    public static Logger logger = Logger.getLogger(ScorecardDesignAction.class);

    @Override
    protected Map getKeyMethodMap() {
        Map map = new HashMap();
        map.put("getAllBusRoles", "getAllBusRoles");
        map.put("getMeasures", "getMeasures");
        map.put("saveScorecardDetails", "saveScorecardDetails");
        map.put("deleteScoreCard", "deleteScoreCard");
        map.put("purgeScoreCard", "purgeScoreCard");
        map.put("editScoreCard", "editScoreCard");
        map.put("editMemberDetails", "editMemberDetails");
        map.put("getMeasureValues", "getMeasureValues");
        map.put("changeValuesAsPerUnit", "changeValuesAsPerUnit");
        map.put("copyScoreCard", "copyScoreCard");
        map.put("getSelectedMeasureValues", "getSelectedMeasureValues");
        map.put("checkScorecardName", "checkScorecardName");
        map.put("changeScoreBasis", "changeScoreBasis");
        map.put("viewScorecard", "viewScorecard");
        map.put("getDimensions", "getDimensions");
        map.put("getCreatedScoreCards", "getCreatedScoreCards");
        map.put("setScoreCardMasterDetails", "setScoreCardMasterDetails");
        map.put("setScoreCardMemberDetails", "setScoreCardMemberDetails");
        map.put("setWeightageForComponents", "setWeightageForComponents");
        map.put("setScoreCardMembersForDimension", "setScoreCardMembersForDimension");
        map.put("createScoreCard", "createScoreCard");
        map.put("insertScoreCardDetails", "insertScoreCardDetails");
        map.put("getJsonForAssignedComponents", "getJsonForAssignedComponents");
        map.put("getWeightageJsonForComponents", "getWeightageJsonForComponents");
        map.put("allRulesAppliedForComponents", "allRulesAppliedForComponents");
        map.put("getWeightageJsonForMeasures", "getWeightageJsonForMeasures");
        map.put("getCreatedDimensions", "getCreatedDimensions");
        map.put("getUserList", "getUserList");
        map.put("copySameValuesToAll", "copySameValuesToAll");
        map.put("shareScoreCards", "shareScoreCards");
        map.put("saveTargetMeasure", "saveTargetMeasure");
        return map;
    }

    public ActionForward getAllBusRoles(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);

        String userId = request.getParameter("userId");
        ScorecardDesignBd scoreBd = new ScorecardDesignBd();
        String busRoles = scoreBd.getUserFoldersByUserId(userId);
        try {
            response.getWriter().print(busRoles);
        } catch (IOException ex) {
        }
        return null;
    }

    public ActionForward getMeasures(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        String folderId = request.getParameter("folderId");
        String ctxPath = request.getParameter("ctxPath");
        String userId = request.getParameter("userId");
        String type = request.getParameter("type");
        ArrayList list = new ArrayList();
        ScorecardDesignBd designBd = new ScorecardDesignBd();
        ReportTemplateDAO repDao = new ReportTemplateDAO();
        String measures = repDao.getMeasures(folderId, list, ctxPath);
        String sCards = "";
        if (!"target".equalsIgnoreCase(type)) {
            sCards = designBd.getCreatedScoreCards(userId, ctxPath);
        }
        try {
            response.getWriter().print(measures + sCards);
            // response.getWriter().print(measures);
        } catch (IOException ex) {
        }
        return null;
    }

    public ActionForward deleteScoreCard(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        String scoreCardIds = request.getParameter("deletescoreids");
        String userId = request.getParameter("userId");
        ScorecardDAO dao = new ScorecardDAO();
        dao.deleteScorecard(scoreCardIds, userId);
        return null;
    }

    public ActionForward purgeScoreCard(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        String scoreCardIds = request.getParameter("deletescoreids");
        String userId = request.getParameter("userId");
        ScorecardDesignBd scoreBd = new ScorecardDesignBd();
        scoreBd.purgeScorecard(scoreCardIds, "purge");
        return null;
    }

    public ActionForward getUserList(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        String scoreCardIds = request.getParameter("sharescoreids");
        String userId = request.getParameter("userId");
        ScorecardDAO dao = new ScorecardDAO();
        String userList = dao.getUserList(userId);
        try {
            PrintWriter out = response.getWriter();
            out.print(userList);
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }
        // dao.deleteScorecard(scoreCardIds, userId);
        return null;
    }

    public ActionForward shareScoreCards(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        String scoreCardIds = request.getParameter("scoreIds");
        String userId = request.getParameter("userId");
        String shareUserIds = request.getParameter("shareUserIds");
        ScorecardDAO dao = new ScorecardDAO();
        dao.shareScoreCards(scoreCardIds, shareUserIds);
        // dao.deleteScorecard(scoreCardIds, userId);
        return null;
    }

    public ActionForward editScoreCard(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        String scoreCardId = request.getParameter("editscoreid");
        String userId = request.getParameter("userId");
        ScoreCard scoreCard = new ScoreCard();
        ScorecardDesignBd scoreBd = new ScorecardDesignBd();
        ScorecardDAO dao = new ScorecardDAO();
        boolean isEditable = dao.isScorecardEditableForUser(userId, scoreCardId);
        String measureJson = "";
        Gson gson = new Gson();
        PrintWriter out = null;
        try {
            out = response.getWriter();
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }
        if (isEditable) {
            scoreCard.setScoreCardId(scoreCardId);
            scoreBd.editScoreCardMaster(userId, scoreCardId, scoreCard);
            measureJson = scoreBd.getSelectedMeas(scoreCardId, scoreCard);
            if (scoreCard.isDimensionBased()) {
                measureJson = scoreBd.editScoreCardMembersInDimensionBased(scoreCard, scoreCardId);
            } else {
                scoreBd.editMemberDetails(scoreCardId, scoreCard);
            }
            ArrayList<ScoreCardColorRule> scoreCardColorLst = scoreBd.editScardLights(scoreCardId);
            scoreCard.setColorList(scoreCardColorLst);
            session.setAttribute("ScoreCard", scoreCard);
            out.print(gson.toJson(scoreCard) + "||" + measureJson);
        } else {
            out.print(isEditable);
        }

        return null;
    }

    public ActionForward getMeasureValues(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        String userId = request.getParameter("userId");
        String folderId = request.getParameter("folderId");
        String measureIds = request.getParameter("measureIds");
        String timeLevel = request.getParameter("timeLevel");
        String isDimBased = request.getParameter("isDimBased");
        String dimId = request.getParameter("dimId");
        String dims = request.getParameter("dims");
        ScorecardDesignBd designBd = new ScorecardDesignBd();
        ArrayList<ScoreCardMeasure> sCardMeasLst = null;

        if ("true".equalsIgnoreCase(isDimBased)) {
//          String dimValues=dims.replace("~",",");
            String[] dimValues = dims.split("~");
            List<String> dimValList = Arrays.asList(dimValues);
            sCardMeasLst = designBd.getScoreCardMeasureLstForDimension(userId, folderId, measureIds, timeLevel, dimId, dimValList);
        } else {

            sCardMeasLst = designBd.getScoreCardMeasureLst(userId, folderId, measureIds, timeLevel);
        }


        session.setAttribute("SCardMeasLst", sCardMeasLst);
        try {
            PrintWriter out = response.getWriter();
            out.print("success");
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }
        return null;
    }

    public ActionForward changeValuesAsPerUnit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        ScorecardDesignBd designBd = new ScorecardDesignBd();
        HttpSession session = request.getSession(false);

        String measId = request.getParameter("measureId");
        String period = request.getParameter("period");
        String unit = request.getParameter("unitType");
        ArrayList<ScoreCardMeasure> sCardMeasLst = (ArrayList<ScoreCardMeasure>) session.getAttribute("SCardMeasLst");
        ScoreCardMeasure scoreCardMeas = null;
        String isDimBased = request.getParameter("isDimBased");
        String dimValue = request.getParameter("dimValue");
        for (ScoreCardMeasure sCardMeas : sCardMeasLst) {
            if (isDimBased.equalsIgnoreCase("true")) {
                if (sCardMeas.getMeasId().equalsIgnoreCase(measId) && sCardMeas.getDimValue().equalsIgnoreCase(dimValue)) {
                    scoreCardMeas = sCardMeas;
                    break;
                }
            } else {
                if (!measId.startsWith("Scards") && sCardMeas.getMeasId().equalsIgnoreCase(measId)) {
                    scoreCardMeas = sCardMeas;
                    break;
                }
            }
        }
        String json = "";
        if (scoreCardMeas != null) {
            json = designBd.changeValuesAsPerUnit(scoreCardMeas, period, unit);
        }
        //
        try {
            PrintWriter out = response.getWriter();
            out.print(json);
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }
        return null;
    }

    public ActionForward copyScoreCard(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        String sCardId = request.getParameter("copyscoreid");
        String sCardName = request.getParameter("copyScardName");
        String sCardDesc = request.getParameter("copyScardDesc");
        String userId = request.getParameter("userId");
        ScorecardDesignBd scoreBd = new ScorecardDesignBd();

        ScoreCard scoreCard = new ScoreCard();
        String measureJson = "";
        scoreCard.setScoreCardId(sCardId);
        scoreBd.editScoreCardMaster(userId, sCardId, scoreCard);
        measureJson = scoreBd.getSelectedMeas(sCardId, scoreCard);
        if (scoreCard.isDimensionBased()) {
            measureJson = scoreBd.editScoreCardMembersInDimensionBased(scoreCard, sCardId);
        } else {
            scoreBd.editMemberDetails(sCardId, scoreCard);
        }
        ArrayList<ScoreCardColorRule> scoreCardColorLst = scoreBd.editScardLights(sCardId);
        scoreCard.setColorList(scoreCardColorLst);
        session.setAttribute("ScoreCard", scoreCard);
        scoreCard.setScoreCardName(sCardName);
        scoreCard.setScoreCardArea(sCardDesc);
        scoreCard.setUserId(userId);
        ScorecardDAO dao = new ScorecardDAO();
        boolean flag = dao.insertScoreCard(scoreCard, "copy");
        // boolean flag=scoreBd.copyScoreCard(sCardId,sCardName,sCardDesc,userId);
        try {
            PrintWriter out = response.getWriter();
            if (flag == true) {
                out.print(flag);
            } else {
                out.print("failed");
            }
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }
        return null;
    }

    public ActionForward getSelectedMeasureValues(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        String measId = request.getParameter("measureId");
        String period = request.getParameter("period");
        ArrayList<ScoreCardMeasure> sCardMeasLst = (ArrayList<ScoreCardMeasure>) session.getAttribute("SCardMeasLst");
        ScoreCardMeasure scoreCardMeas = null;
        String isDimBased = request.getParameter("isDimBased");

        String dimValue = request.getParameter("dimValue");
        for (ScoreCardMeasure sCardMeas : sCardMeasLst) {
            if (isDimBased.equalsIgnoreCase("true")) {
                if (sCardMeas.getMeasId().equalsIgnoreCase(measId) && sCardMeas.getDimValue().equalsIgnoreCase(dimValue)) {
                    scoreCardMeas = sCardMeas;
                    break;
                }
            } else {
                if (!measId.startsWith("Scards") && sCardMeas.getMeasId().equalsIgnoreCase(measId)) {
                    scoreCardMeas = sCardMeas;
                    break;
                }
            }

        }
        Gson gson = new Gson();
        String json = "";
        if (scoreCardMeas != null) {
            json = gson.toJson(scoreCardMeas);
        }
        try {
            PrintWriter out = response.getWriter();
            out.print(json);
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }
        return null;
    }

    public ActionForward checkScorecardName(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        String sCardName = request.getParameter("sCardName");
        ScorecardDesignBd scoreBd = new ScorecardDesignBd();
        boolean isExists = scoreBd.isScorecardNameExists(sCardName);
        try {
            PrintWriter out = response.getWriter();
            out.print(isExists);
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }
        return null;
    }

    public ActionForward changeScoreBasis(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String measId = request.getParameter("measureId");
        String period = request.getParameter("period");
        String userId = request.getParameter("userId");
        String folderId = request.getParameter("folderId");
        String scoreBasis = request.getParameter("scoreBasis");
        String isDimBased = request.getParameter("isDimBased");
        String dims = request.getParameter("dimValues");
        String dimId = request.getParameter("dimId");
        ArrayList<ScoreCardMeasure> scoreCardMeasLst = null;
        ScoreCardMeasure scoreCardMeas = null;
        ScorecardDesignBd designBd = new ScorecardDesignBd();
        String refElementId = designBd.getPriorAndChangeMeasures(measId, scoreBasis);

        if ("true".equalsIgnoreCase(isDimBased)) {
            String[] dimValues = dims.split("~");
            List<String> dimValList = Arrays.asList(dimValues);
            scoreCardMeasLst = designBd.getScoreCardMeasureLstForDimension(userId, folderId, refElementId, "", dimId, dimValList);
        } else {

            scoreCardMeasLst = designBd.getScoreCardMeasureLst(userId, folderId, refElementId, "");
        }

        String dimValue = request.getParameter("selDim");
        for (ScoreCardMeasure sCardMeas : scoreCardMeasLst) {
            if (isDimBased.equalsIgnoreCase("true")) {
                if (sCardMeas.getMeasId().equalsIgnoreCase(refElementId) && sCardMeas.getDimValue().equalsIgnoreCase(dimValue)) {
                    scoreCardMeas = sCardMeas;
                    break;
                }
            } else {
                if (!measId.startsWith("Scards") && sCardMeas.getMeasId().equalsIgnoreCase(refElementId)) {
                    scoreCardMeas = sCardMeas;
                    break;
                }
            }

        }
        Gson gson = new Gson();
        String json = "";
        if (scoreCardMeas != null) {
            json = gson.toJson(scoreCardMeas);
        }
        try {
            PrintWriter out = response.getWriter();
            out.print(json);
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }

        //String json=designBd.getJsonForScorecardBasis(refElementId, period, userId, folderId,scoreBasis);
//        try {
//            PrintWriter out = response.getWriter();
//            out.print(json);
//         } catch (IOException ex) {
//            logger.error("Exception:",ex);
//         }
        return null;
    }

    public ActionForward viewScorecard(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String scardId = request.getParameter("scorecardId");
        HttpSession session = request.getSession();
        session.setAttribute("ScoreCardId", scardId);
        return mapping.findForward("viewScorecard");
    }

    public ActionForward getDimensions(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String folderId = request.getParameter("folderId");
        String userId = request.getParameter("userId");
        ReportTemplateDAO templateDAO = new ReportTemplateDAO();
        String dimensionData = templateDAO.editFavoriteParameters(folderId, userId, "");
        try {
            PrintWriter out = response.getWriter();
            out.print(dimensionData.replace("@", ""));
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }
        return null;
    }

    public ActionForward getCreatedDimensions(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        ScoreCard scoreCard = (ScoreCard) session.getAttribute("ScoreCard");
        ScorecardDesignBd designBd = new ScorecardDesignBd();
        String json = designBd.getJsonForCreatedDimensions(scoreCard);
        try {
            PrintWriter out = response.getWriter();
            out.print(json);
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }
        return null;
    }

    public ActionForward getCreatedScoreCards(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        String userId = request.getParameter("userId");
        ScorecardDesignBd designBd = new ScorecardDesignBd();
        String html = designBd.getCreatedScoreCards(userId, "pi");
        try {
            PrintWriter out = response.getWriter();
            out.print(html);
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }
        return null;
    }

    public ActionForward setScoreCardMasterDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        String userId = request.getParameter("userId");
        String sName = request.getParameter("sName");
        String area = request.getParameter("area");
        String colors = request.getParameter("colCode");
        String colOperators = request.getParameter("colOper");
        String colStValues = request.getParameter("stVal");
        String colEndValues = request.getParameter("endVal");
        String bussFolderId = request.getParameter("bussFolderId");
        String measId = request.getParameter("measId");
        String measureName = request.getParameter("measureName");
        String colorType = request.getParameter("colorType");
        String mode = request.getParameter("mode");
        String targetScore = request.getParameter("targetScore");
        double targetScoreDouble = 0;
        if (targetScore != null && !targetScore.equalsIgnoreCase("") && !targetScore.equalsIgnoreCase("undefined")) {
            targetScoreDouble = Double.parseDouble(request.getParameter("targetScore"));
        }
        boolean isDimBased = false;
        if (request.getParameter("isDimBased") != null) {
            isDimBased = Boolean.valueOf(request.getParameter("isDimBased"));
        }

        ScorecardDesignBd bd = new ScorecardDesignBd();
        ArrayList<String> elementIdsLst = this.splitParameterArgument(measId, ",");
        ArrayList<String> elementNamesLst = this.splitParameterArgument(measureName, ",");
        ArrayList<String> componentTypes = new ArrayList<String>();
        ArrayList<ScoreCardColorRule> colorRules = null;
        if (!"".equalsIgnoreCase(colors)) {
            ArrayList<String> colorLst = this.splitParameterArgument(colors, ",");
            ArrayList<String> operatorsLst = this.splitParameterArgument(colOperators, ",");
            ArrayList<Double> stValuesLst = this.convertStringListToDouble(this.splitParameterArgument(colStValues, ","));
            ArrayList<Double> endValuesLst = this.convertStringListToDouble(this.splitParameterArgument(colEndValues, ","));
            colorRules = bd.createScoreCardColorRules(colorLst, operatorsLst, stValuesLst, endValuesLst);
        }
        if (session.getAttribute("ScoreCard") == null || !"Edit".equalsIgnoreCase(mode)) {
            ScoreCard scoreCard = bd.createScoreCard(sName, area, bussFolderId, userId);
            scoreCard.setDimensionBased(isDimBased);
            if ("Target".equalsIgnoreCase(colorType)) {
                scoreCard.setLightType(colorType);
            } else {
                scoreCard.setLightType("Prior");
            }

            scoreCard.setColorList(colorRules);
            for (String id : elementIdsLst) {
                if (id.startsWith("Scards")) {
                    componentTypes.add(ScoreCardConstants.SCARD_MEMBER_TYPE_NESTED_SCARD);
                } else {
                    if (isDimBased) {
                        componentTypes.add(ScoreCardConstants.SCARD_MEMBER_TYPE_DIM_KPI);
                    } else {
                        componentTypes.add(ScoreCardConstants.SCARD_MEMBER_TYPE_KPI);
                    }
                }
            }
            ArrayList<ScorecardComponent> scoreCardComponents = bd.createScoreCardComponents(componentTypes, elementIdsLst, elementNamesLst);
            scoreCard.setMemberList(scoreCardComponents);
            scoreCard.setTargetScore(targetScoreDouble);
            session.setAttribute("ScoreCard", scoreCard);
        } else if (session.getAttribute("ScoreCard") != null) {
            ScoreCard scoreCard = (ScoreCard) session.getAttribute("ScoreCard");
            scoreCard.setScoreCardName(sName);
            scoreCard.setScoreCardArea(area);
            scoreCard.setLightType(colorType);
            scoreCard.setUserId(userId);
            scoreCard.setColorList(colorRules);
            scoreCard.setTargetScore(targetScoreDouble);
            if ("".equalsIgnoreCase(colorType)) {
                scoreCard.setLightType("Prior");
            }
            this.createNewScoreCardMembers(elementIdsLst, elementNamesLst, scoreCard, isDimBased);
        }
        return null;
    }

    private void createNewScoreCardMembers(ArrayList<String> measIdsLst, ArrayList<String> measNamesLst, ScoreCard scoreCard, boolean isDimBased) {
        ScorecardDesignBd designBd = new ScorecardDesignBd();
        List<ScorecardComponent> scoreCardCompLst = scoreCard.getMemberList();
        List<ScorecardComponent> newScoreCardCompsLst = new ArrayList<ScorecardComponent>();
        ArrayList<String> existedMeasures = new ArrayList<String>();
        ScoreCardMember scoreCardMem = null;
        ScoreCard childScoreCard = null;
        ScoreCardMember childScoreCardMember = null;
        List<ScorecardComponent> childScoreCardCompsLst = new ArrayList<ScorecardComponent>();
        for (ScorecardComponent scoreCardComp : scoreCardCompLst) {
            if (scoreCardComp instanceof ScoreCardMember) {
                scoreCardMem = (ScoreCardMember) scoreCardComp;
                if (measIdsLst.contains(scoreCardMem.getElementId())) {
                    newScoreCardCompsLst.add(scoreCardMem);
                    existedMeasures.add(scoreCardMem.getElementId());
                }
            } else {
                childScoreCard = (ScoreCard) scoreCardComp;
                childScoreCardCompsLst = childScoreCard.getMemberList();
                if (childScoreCardCompsLst.size() > 0 && measIdsLst.contains(((ScoreCardMember) childScoreCardCompsLst.get(0)).getElementId())) {
                    newScoreCardCompsLst.add(childScoreCard);
                    existedMeasures.add(childScoreCard.getScoreCardId());
                } else if (measIdsLst.contains(childScoreCard.getScoreCardId()) || measIdsLst.contains("Scards" + childScoreCard.getScoreCardId())) {
                    newScoreCardCompsLst.add(childScoreCard);
                    existedMeasures.add(childScoreCard.getScoreCardId());
                }
            }
        }
        for (int i = 0; i < measIdsLst.size(); i++) {
            if (!existedMeasures.contains(measIdsLst.get(i))) {
                if (!measIdsLst.get(i).startsWith("Scards") && !isDimBased) {
                    newScoreCardCompsLst.add(designBd.createScoreCardComponent(ScoreCardConstants.SCARD_MEMBER_TYPE_KPI, measIdsLst.get(i), measNamesLst.get(i)));
                } else if (measIdsLst.get(i).startsWith("Scards")) {
                    newScoreCardCompsLst.add(designBd.createScoreCardComponent(ScoreCardConstants.SCARD_MEMBER_TYPE_NESTED_SCARD, measIdsLst.get(i), measNamesLst.get(i)));
                } else {
                    newScoreCardCompsLst.add(designBd.createScoreCardComponent(ScoreCardConstants.SCARD_MEMBER_TYPE_DIM_KPI, measIdsLst.get(i), measNamesLst.get(i)));
                }

            }
        }
        scoreCard.setMemberList(newScoreCardCompsLst);


    }

    public ActionForward setScoreCardMemberDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        String measId = request.getParameter("measId");
        String measureName = request.getParameter("measName");
        String units = request.getParameter("units");
        String period = request.getParameter("period");
        String scoreBasis = request.getParameter("scoreBasis");
        String sCards = request.getParameter("sCards");
        String operators = request.getParameter("operators");
        String stValues = request.getParameter("stValues");
        String endValues = request.getParameter("endValues");
        String userId = request.getParameter("userId");
        String dimId = request.getParameter("dimId");
        String dimValue = request.getParameter("dimValue");

        String targetMeasureType = request.getParameter("tMeasType");
        String targetMeasValue = request.getParameter("tMeasVal");

//         String currMeasId=request.getParameter("currMeasId");
//         String currDimValue=request.getParameter("currDimValue");

        PrintWriter out = null;
        ScorecardDesignBd designBd = new ScorecardDesignBd();
        boolean isDimBased = false;
        if (request.getParameter("isDimBased") != null) {
            isDimBased = Boolean.valueOf(request.getParameter("isDimBased"));
        }
        ScoreCardMemberRuleTransferObject memRuleTransObject = new ScoreCardMemberRuleTransferObject();

        ArrayList<Double> scoreLst = new ArrayList<Double>();
        ArrayList<String> operatorsLst = new ArrayList<String>();
        ArrayList<Double> stValuesLst = new ArrayList<Double>();
        ArrayList<Double> endValuesLst = new ArrayList<Double>();

        scoreLst = this.convertStringListToDouble(this.splitParameterArgument(sCards, ","));
        operatorsLst = this.splitParameterArgument(operators, ",");
        stValuesLst = this.convertStringListToDouble(this.splitParameterArgument(stValues, ","));
        endValuesLst = this.convertStringListToDouble(this.splitParameterArgument(endValues, ","));

        memRuleTransObject.measureId = measId;
        memRuleTransObject.measureName = measureName;
        memRuleTransObject.period = period;
        memRuleTransObject.unit = units;
        memRuleTransObject.scoreBasis = scoreBasis;
        memRuleTransObject.scores = scoreLst;
        memRuleTransObject.operators = operatorsLst;
        memRuleTransObject.stValues = stValuesLst;
        memRuleTransObject.endValues = endValuesLst;
        memRuleTransObject.userId = userId;

        if (isDimBased) {
            memRuleTransObject.dimensionId = dimId;
            memRuleTransObject.dimensionValue = dimValue;
        }
        if (ScoreCardConstants.SCORECARD_BASIS_TARGET.equalsIgnoreCase(scoreBasis)
                || ScoreCardConstants.SCORECARD_BASIS_TARGET_PERC.equalsIgnoreCase(scoreBasis)) {
            memRuleTransObject.targetMeasureType = targetMeasureType;
            if (ScoreCardConstants.TARGET_VALUE.equalsIgnoreCase(targetMeasureType)) {
                if (!"".equalsIgnoreCase(targetMeasValue)) {
                    memRuleTransObject.targetMeasureValue = Double.parseDouble(targetMeasValue);
                }
            }
        }

        ScoreCard scoreCard = (ScoreCard) session.getAttribute("ScoreCard");
        String componentType = this.getScorecardMemberType(measId, isDimBased);

        designBd.setRulesForScoreCardMember(memRuleTransObject, scoreCard, measId, componentType);


        return null;
    }

    public ActionForward getJsonForAssignedComponents(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        ScorecardDesignBd designBd = new ScorecardDesignBd();
        ScoreCard scoreCard = (ScoreCard) session.getAttribute("ScoreCard");
        boolean isDimBased = false;
        if (request.getParameter("isDimBased") != null) {
            isDimBased = Boolean.valueOf(request.getParameter("isDimBased"));
        }
        String currMeasId = request.getParameter("currMeasId");
        String currDimValue = request.getParameter("currDimValue");
        PrintWriter out = null;
        String componentType = this.getScorecardMemberType(currMeasId, isDimBased);
        String json = designBd.getAssignedRulesForComponent(scoreCard, currMeasId, currDimValue, componentType);
//         
        try {
            out = response.getWriter();
            out.print(json);
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }
        return null;
    }

    /**
     * After dragging dimension members invoke this method to create
     * ScoreCardMember's for the Dimension Based Scorecard
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward setScoreCardMembersForDimension(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        String measId = request.getParameter("measId");
        String measureName = request.getParameter("measName");
        String dimId = request.getParameter("dimId");
        String dimValues = request.getParameter("dimValues");
        ArrayList<String> elementIdsLst = this.splitParameterArgument(measId, ",");
        ArrayList<String> elementNamesLst = this.splitParameterArgument(measureName, ",");
        ArrayList<String> dimValuesLst = this.splitParameterArgument(dimValues, ",");
        ScoreCard scoreCard = (ScoreCard) session.getAttribute("ScoreCard");
        String mode = request.getParameter("mode");
        ScoreCard childScoreCard = null;
        String userId = request.getParameter("userId");
        String folderId = request.getParameter("folderId");
        ScoreCardMember scoreCardMember = null;
        ArrayList<ScorecardComponent> scoreCardMemLst = new ArrayList<ScorecardComponent>();
        int i = 0;
        if (mode.equalsIgnoreCase("Edit")) {

            for (String elementId : elementIdsLst) {
                scoreCardMemLst = new ArrayList<ScorecardComponent>();
                childScoreCard = (ScoreCard) scoreCard.getScoreCardMemberScoreCardPredicate(elementId);
                if (childScoreCard.getMemberList() == null || childScoreCard.getMemberList().size() == 0) {
                    for (String dimVal : dimValuesLst) {
                        scoreCardMember = new ScoreCardMember();
                        scoreCardMember.setDimElementId(dimId);
                        scoreCardMember.setDimValue(dimVal);
                        scoreCardMember.setElementId(elementId);
                        //scoreCardMember.setElementName(elementNamesLst.get(i));
                        scoreCardMember.setElementName(childScoreCard.getScoreCardName());
                        scoreCardMemLst.add(scoreCardMember);

                    }
                    childScoreCard.setMemberList(scoreCardMemLst);
                    childScoreCard.setUserId(userId);
                    childScoreCard.setFolderId(folderId);
                    i++;
                } else {
                    childScoreCard.setUserId(userId);
                    childScoreCard.setFolderId(folderId);
                    this.createNewScoreCardDimensions(dimValuesLst, childScoreCard, elementId, dimId, elementNamesLst.get(i));
                    i++;
                }
            }
        } else {
            for (String elementId : elementIdsLst) {
                scoreCardMemLst = new ArrayList<ScorecardComponent>();
                childScoreCard = (ScoreCard) scoreCard.getScoreCardMemberScoreCardPredicate(elementId);

                for (String dimVal : dimValuesLst) {
                    scoreCardMember = new ScoreCardMember();
                    scoreCardMember.setDimElementId(dimId);
                    scoreCardMember.setDimValue(dimVal);
                    scoreCardMember.setElementId(elementId);
                    scoreCardMember.setElementName(elementNamesLst.get(i));
                    scoreCardMemLst.add(scoreCardMember);

                }
                childScoreCard.setMemberList(scoreCardMemLst);
                childScoreCard.setUserId(userId);
                childScoreCard.setFolderId(folderId);
                i++;
            }
        }
        return null;
    }

    private void createNewScoreCardDimensions(ArrayList<String> dimValuesLst, ScoreCard scoreCard, String elementId, String dimId, String elementName) {
        List<ScorecardComponent> scoreCardCompsLst = scoreCard.getMemberList();
        List<ScorecardComponent> newScoreCardCompsLst = new ArrayList<ScorecardComponent>();
        ArrayList<String> existingDimsList = new ArrayList<String>();
        ScoreCardMember scoreCardMember = null;
        ScoreCardMember childScoreCardMem = null;
        for (ScorecardComponent scoreCardComp : scoreCardCompsLst) {
            scoreCardMember = (ScoreCardMember) scoreCardComp;
            existingDimsList.add(scoreCardMember.getDimValue());
        }
        for (String memValue : dimValuesLst) {
            if (existingDimsList.contains(memValue)) {
                newScoreCardCompsLst.add(scoreCard.getChildScoreCardMemberPredicate(memValue));
            } else {
                childScoreCardMem = new ScoreCardMember();
                childScoreCardMem.setDimElementId(dimId);
                childScoreCardMem.setDimValue(memValue);
                childScoreCardMem.setElementId(elementId);
                childScoreCardMem.setElementName(elementName);
                newScoreCardCompsLst.add(childScoreCardMem);
            }
        }
        scoreCard.setMemberList(newScoreCardCompsLst);

    }

    public ActionForward setWeightageForComponents(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        ScoreCard scoreCard = (ScoreCard) session.getAttribute("ScoreCard");
        String measureIds = request.getParameter("measIds");
        String weightages = request.getParameter("measWeight");
        String selectedMeasId = request.getParameter("selectedMeasId");
        String weightType = request.getParameter("weightType");
        boolean isDimBased = false;
        if (request.getParameter("isDimBased") != null) {
            isDimBased = Boolean.valueOf(request.getParameter("isDimBased"));
        }
        ArrayList<String> measIdLst = this.splitParameterArgument(measureIds, ",");
        ArrayList<Double> measWeightLst = this.convertStringListToDouble(this.splitParameterArgument(weightages, ","));
        ArrayList<String> compTypeLst = new ArrayList<String>();
        ScorecardDesignBd designBd = new ScorecardDesignBd();
        for (String id : measIdLst) {
            compTypeLst.add(this.getScorecardMemberType(id, isDimBased));
        }

        designBd.setWeightageForComponent(scoreCard, measIdLst, measWeightLst, compTypeLst, selectedMeasId, weightType);

        return null;
    }

    public ActionForward getWeightageJsonForComponents(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String currMeasId = request.getParameter("currMeasId");
        HttpSession session = request.getSession(false);
        ScoreCard scoreCard = (ScoreCard) session.getAttribute("ScoreCard");
        ScorecardDesignBd designBd = new ScorecardDesignBd();
        PrintWriter out = null;
        boolean isDimBased = false;
        if (request.getParameter("isDimBased") != null) {
            isDimBased = Boolean.valueOf(request.getParameter("isDimBased"));
        }
        String compType = this.getScorecardMemberType(currMeasId, isDimBased);
        String json = designBd.getJsonForWeightage(scoreCard, currMeasId);
        //
        try {
            out = response.getWriter();
            out.print(json);
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }
        return null;
    }

    public ActionForward getWeightageJsonForMeasures(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        ScoreCard scoreCard = (ScoreCard) session.getAttribute("ScoreCard");
        ScorecardDesignBd designBd = new ScorecardDesignBd();
        PrintWriter out = null;
        String json = designBd.getJsonWeightageForKpi(scoreCard);
        try {
            out = response.getWriter();
            out.print(json);
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }
        return null;
    }

    public ActionForward createScoreCard(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        boolean isDimBased = false;
        if (request.getParameter("isDimBased") != null) {
            isDimBased = Boolean.valueOf(request.getParameter("isDimBased"));
        }
        ScoreCard scoreCard = (ScoreCard) session.getAttribute("ScoreCard");

        return null;
    }

    public ActionForward insertScoreCardDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        ScorecardDesignBd designBd = new ScorecardDesignBd();
        String mode = request.getParameter("mode");
        ScoreCard scoreCard = (ScoreCard) session.getAttribute("ScoreCard");
        ScorecardDAO dao = new ScorecardDAO();
        boolean flag = dao.insertScoreCard(scoreCard, mode);
        PrintWriter out = null;
        try {
            out = response.getWriter();
            if (flag) {
                out.print(mode);
            } else {
                out.print("failed" + mode);
            }

        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }

        return null;
    }

    public ActionForward allRulesAppliedForComponents(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        ScorecardDesignBd designBd = new ScorecardDesignBd();
        ScoreCard scoreCard = (ScoreCard) session.getAttribute("ScoreCard");
        boolean flag = designBd.allRulesAppliedForComponents(scoreCard);
        PrintWriter out = null;
        try {
            out = response.getWriter();
            out.print(flag);
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }
        return null;
    }

    public ActionForward copySameValuesToAll(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        ScorecardDesignBd designBd = new ScorecardDesignBd();
        ScoreCard scoreCard = (ScoreCard) session.getAttribute("ScoreCard");
        String measId = request.getParameter("measId");
        String dimId = request.getParameter("dimId");
        String dimValue = request.getParameter("dimValue");
        String measures = request.getParameter("measures");
        String dims = request.getParameter("dims");
        boolean isDimBased = false;
        ArrayList<String> measureLst = new ArrayList<String>();
        ArrayList<String> dimLst = new ArrayList<String>();
        if (request.getParameter("isDimBased") != null) {
            isDimBased = Boolean.valueOf(request.getParameter("isDimBased"));
        }
        String componentType = this.getScorecardMemberType(measId, isDimBased);
        measureLst = this.splitParameterArgument(measures, ",");
        dimLst = this.splitParameterArgument(dims, "~");
        designBd.copySameValuesToAll(scoreCard, measId, dimValue, componentType, measureLst, dimLst);
        return null;
    }

    public ActionForward saveTargetMeasure(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        ScorecardDesignBd designBd = new ScorecardDesignBd();
        designBd.saveTargetMeasure(request);
        return null;
    }

    private String getScorecardMemberType(String id, boolean isDimBased) {
        if (id.startsWith("Scards")) {
            return ScoreCardConstants.SCARD_MEMBER_TYPE_NESTED_SCARD;
        } else {
            if (isDimBased) {
                return ScoreCardConstants.SCARD_MEMBER_TYPE_DIM_KPI;
            } else {
                return ScoreCardConstants.SCARD_MEMBER_TYPE_KPI;
            }
        }
    }

    private ArrayList<String> splitParameterArgument(String parameter, String separator) {
        Iterable<String> paramValues = Splitter.on(separator).split(parameter);
        ArrayList<String> paramValuesLst = new ArrayList<String>();
        Iterator<String> itr = paramValues.iterator();
        while (itr.hasNext()) {
            paramValuesLst.add(itr.next());
        }
        //  paramValuesLst.addAll((Collection<? extends String>) paramValues);
        return paramValuesLst;
    }

    private ArrayList<Double> convertStringListToDouble(ArrayList<String> doubleStrLst) {
        Iterable<Double> doubleValues = Iterables.transform(doubleStrLst, new Function<String, Double>() {

            public Double apply(String value) {
                Double convertedValue = 0.0;
                try {
                    if (value != null) {
                        convertedValue = Double.parseDouble(value);
                    }
                } catch (NumberFormatException nfe) {
                }
                return convertedValue;
            }
        });
        ArrayList<Double> doubleValueLst = new ArrayList<Double>();
        Iterator<Double> itr = doubleValues.iterator();
        while (itr.hasNext()) {
            doubleValueLst.add(itr.next());
        }

        //doubleValueLst.addAll((Collection<? extends Double>) doubleValues);
        return doubleValueLst;
    }
}
