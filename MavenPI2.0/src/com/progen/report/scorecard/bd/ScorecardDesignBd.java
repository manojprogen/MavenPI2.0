/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.scorecard.bd;

import com.google.gson.Gson;
import com.progen.report.ReportParameter;
import com.progen.report.query.TrendDataSet;
import com.progen.report.query.TrendDataSetHelper;
import com.progen.report.scorecard.*;
import com.progen.report.scorecard.db.ScorecardResBundleSqlServer;
import com.progen.report.scorecard.db.ScorecardResourceBundle;
import com.progen.userlayer.db.UserLayerDAO;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import prg.db.PbDb;
import prg.db.PbReturnObject;
import utils.db.ProgenConnection;

/**
 *
 * @author progen
 */
public class ScorecardDesignBd {

    public static Logger logger = Logger.getLogger(ScorecardDesignBd.class);
    private ResourceBundle resBundle;

    private ResourceBundle getResourceBundle() {
        if (this.resBundle == null) {
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                resBundle = new ScorecardResBundleSqlServer();
            } else {
                resBundle = new ScorecardResourceBundle();
            }
        }
        return resBundle;

    }

    public String getUserFoldersByUserId(String pbUserId) {
        PbReturnObject retObj = null;
        String finalQuery;
        Object obj[] = null;
        PbDb pbdb = new PbDb();
        String folderId = "";
        String folderName = "";
        String getUserFoldersByUserIdQuery = getResourceBundle().getString("getUserFoldersByUserId");
        StringBuffer buffer = new StringBuffer("");

        try {
            obj = new Object[1];
            obj[0] = pbUserId;
            finalQuery = pbdb.buildQuery(getUserFoldersByUserIdQuery, obj);
            retObj = pbdb.execSelectSQL(finalQuery);
            if (retObj != null && retObj.getRowCount() > 0) {
                for (int i = 0; i < retObj.getRowCount(); i++) {
                    folderId = retObj.getFieldValueString(i, "FOLDER_ID");
                    folderName = retObj.getFieldValueString(i, "FOLDER_NAME");
                    buffer.append("<option id=" + folderId + " value=" + folderId + ">");
                    buffer.append(folderName);
                    buffer.append("</option>");
                }
            }
        } catch (Exception exception) {
            logger.error("Exception:", exception);

        }
        return buffer.toString();
    }

    public boolean purgeScorecard(String scardId, String mode) {
        ArrayList queries = new ArrayList();
        PbDb pbdb = new PbDb();
        boolean result = false;
        String deleteScardActionDetailsQuery = "delete from PRG_AR_SCARD_ACTION_DETAILS where scard_id in (" + scardId + ")";
        String deleteScoreCardLightsQuery = "delete from PRG_AR_SCORECARD_LIGHTS where SCARDID in (" + scardId + ")";
        String deleteScardMemberKpiRuleQuery = "delete from PRG_AR_SCARD_MEMBER_KPI_RULE where scard_id in (" + scardId + ")";
        String deleteScardMemberKpiQuery = "delete from PRG_AR_SCARD_MEMBER_KPI where scard_id in (" + scardId + ")";
        String deleteScardMemberQuery = "delete from PRG_AR_SCARD_MEMBER where scard_id in (" + scardId + ")";
        String deleteScardUsersQuery = "delete from PRG_AR_SCARD_USERS where scard_id in (" + scardId + ")";
        String deleteScardMasterQuery = "delete from PRG_AR_SCARD_MASTER where scard_id in (" + scardId + ")";
        try {

            queries.add(deleteScardActionDetailsQuery);
            queries.add(deleteScoreCardLightsQuery);
            queries.add(deleteScardMemberKpiRuleQuery);
            queries.add(deleteScardMemberKpiQuery);
            queries.add(deleteScardMemberQuery);

            if ("purge".equalsIgnoreCase(mode)) {
                queries.add(deleteScardMasterQuery);
                queries.add(deleteScardUsersQuery);
            }


            result = pbdb.executeMultiple(queries);
        } catch (Exception e) {
            logger.error("Exception:", e);
        }

        return result;
    }

    public boolean editScoreCardMaster(String userId, String sCardId, ScoreCard scoreCard) {
        String editScoreCardMaster = getResourceBundle().getString("editScardMaster");
        PbDb pbdb = new PbDb();
        PbReturnObject pbro = new PbReturnObject();
        Object bindObj[] = null;
        String finalQry = "";
        StringBuilder builder = new StringBuilder();
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
            bindObj = new Object[1];
            bindObj[0] = sCardId;
            finalQry = pbdb.buildQuery(editScoreCardMaster, bindObj);
        } else {
            bindObj = new Object[1];
            bindObj[0] = sCardId;
            finalQry = pbdb.buildQuery(editScoreCardMaster, bindObj);
        }
        try {
            pbro = pbdb.execSelectSQL(finalQry);
            if (pbro.getRowCount() > 0) {
                scoreCard.setScoreCardName(pbro.getFieldValueString(0, "SCARD_NAME"));
                scoreCard.setScoreCardArea(pbro.getFieldValueString(0, "SCARD_AREA"));

                scoreCard.setLightType(pbro.getFieldValueString(0, "LIGHTTYPE"));
                scoreCard.setFolderId(pbro.getFieldValueString(0, "FOLDER_ID"));
                if (pbro.getFieldValueString(0, "TARGET_SCORE") != null && !pbro.getFieldValueString(0, "TARGET_SCORE").equalsIgnoreCase("")) {
                    scoreCard.setTargetScore(Double.parseDouble(pbro.getFieldValueString(0, "TARGET_SCORE")));
                }
                return false;
            }
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        return true;

    }

    public String getSelectedMeas(String sCardId, ScoreCard scoreCard) {
        String editScoreCardMemKpi = getResourceBundle().getString("editScardMemberKpi");
        PbDb pbdb = new PbDb();
        PbReturnObject pbro = new PbReturnObject();
        Object bindObj[] = null;
        String finalQry = "";
        StringBuilder builder = new StringBuilder();
        String elementIds = "";
        String elementNames = "";
        String scoreBasis = "";
        ArrayList<String> elementIdsLst = new ArrayList<String>();
        ArrayList<String> elementNamesLst = new ArrayList<String>();
        ArrayList<String> compTypeLst = new ArrayList<String>();
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
            bindObj = new Object[1];
            bindObj[0] = sCardId;
            finalQry = pbdb.buildQuery(editScoreCardMemKpi, bindObj);
        } else {
            bindObj = new Object[1];
            bindObj[0] = sCardId;
            finalQry = pbdb.buildQuery(editScoreCardMemKpi, bindObj);
        }
        ArrayList<ScorecardComponent> scoreCardCompLst = null;
        ScoreCardMember scoreCardMem = null;
        ScoreCard scoreCardChild = null;
        try {
            pbro = pbdb.execSelectSQL(finalQry);
            if (pbro != null && pbro.getRowCount() > 0) {
                for (int i = 0; i < pbro.getRowCount(); i++) {
                    if (!elementIdsLst.contains(pbro.getFieldValueString(i, "ELEMENT_ID"))) {
                        elementIdsLst.add(pbro.getFieldValueString(i, "ELEMENT_ID"));
                        String elementName = pbro.getFieldValueString(i, "ELEMENT_NAME");
                        elementNamesLst.add(elementName);
                        if (pbro.getFieldValueString(i, "DIMENSION_VALUE") != null && !"".equalsIgnoreCase(pbro.getFieldValueString(i, "DIMENSION_VALUE"))) {
                            compTypeLst.add(ScoreCardConstants.SCARD_MEMBER_TYPE_DIM_KPI);
                        } else {
                            compTypeLst.add(ScoreCardConstants.SCARD_MEMBER_TYPE_KPI);
                        }
                    }
                }
                scoreCardCompLst = this.createScoreCardComponents(compTypeLst, elementIdsLst, elementNamesLst);
                scoreCard.setMemberList(scoreCardCompLst);
                for (int i = 0; i < scoreCardCompLst.size(); i++) {
                    if (scoreCardCompLst.get(i) instanceof ScoreCardMember) {
                        scoreCardMem = (ScoreCardMember) scoreCardCompLst.get(i);
                        scoreCardMem.setBasis(pbro.getFieldValueString(i, "SCARD_BASIS"));
                        scoreCardMem.setTargetElementId(pbro.getFieldValueString(i, "TARGET_ELEMENT_ID"));
                        scoreCardMem.setTargetElementName(pbro.getFieldValueString(i, "TARGET_ELEMENT_NAME"));
                        String targetType = pbro.getFieldValueString(i, "TARGET_TYPE");
                        if (ScoreCardConstants.TARGET_VALUE.equalsIgnoreCase(targetType)) {
                            scoreCardMem.setTargetMeasureType(targetType);
                            scoreCardMem.setTargetMeasureValue(Double.parseDouble(pbro.getFieldValueString(i, "TARGET_VALUE")));
                        }
                    }
                }
                this.editMembersForNestedScoreCard(sCardId, scoreCard, elementIdsLst, elementNamesLst);
                if (isDimensionScoreCard(sCardId)) {
                    scoreCard.setDimensionBased(true);
                } else {
                    scoreCard.setDimensionBased(false);
                }

            } else {
                // scoreCard.setDimensionBased(true);
                this.editMembersForNestedScoreCard(sCardId, scoreCard, elementIdsLst, elementNamesLst);
            }
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        return this.getJsonForMeasures(elementIdsLst, elementNamesLst);

    }

    private void editMembersForNestedScoreCard(String sCardId, ScoreCard scoreCard, ArrayList<String> elementIdsLst, ArrayList<String> elementNamesLst) {
        String getScardMemDetails = getResourceBundle().getString("editScardMemberDetails");
        PbDb pbdb = new PbDb();
        PbReturnObject pbro = new PbReturnObject();
        Object bindObj[] = new Object[1];
        bindObj[0] = sCardId;
        try {
            pbro = pbdb.execSelectSQL(pbdb.buildQuery(getScardMemDetails, bindObj));
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        ScoreCard childScoreCard = null;
        for (int i = 0; i < pbro.getRowCount(); i++) {
            if (pbro.getFieldValueString(i, "CHILD_SCARD_ID") != null && !"".equalsIgnoreCase(pbro.getFieldValueString(i, "CHILD_SCARD_ID"))) {
                childScoreCard = new ScoreCard();
                childScoreCard.setScoreCardId(pbro.getFieldValueString(i, "CHILD_SCARD_ID"));
                childScoreCard.setContribution(Double.parseDouble(pbro.getFieldValueString(i, "SCARD_CONTRIBUTION")));
                this.editScoreCardMaster("", pbro.getFieldValueString(i, "CHILD_SCARD_ID"), childScoreCard);
                elementIdsLst.add("Scards" + pbro.getFieldValueString(i, "CHILD_SCARD_ID"));
                if (isDimensionScoreCard(pbro.getFieldValueString(i, "CHILD_SCARD_ID"))) {
                    scoreCard.setDimensionBased(true);
                } else {
                    scoreCard.setDimensionBased(false);
                    childScoreCard.setScoreCardId("Scards" + pbro.getFieldValueString(i, "CHILD_SCARD_ID"));
                }
                elementNamesLst.add(childScoreCard.getScoreCardName());
                scoreCard.getMemberList().add(childScoreCard);

            }
        }
    }

    private boolean isDimensionScoreCard(String childScardId) {
        boolean isDimensionBased = false;
        String getScardMemDetails = getResourceBundle().getString("editScardMemberDetails");
        Object bindObj[] = new Object[1];
        bindObj[0] = childScardId;
        PbReturnObject pbro = null;
        PbDb pbdb = new PbDb();
        try {
            pbro = pbdb.execSelectSQL(pbdb.buildQuery(getScardMemDetails, bindObj));
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        if (pbro != null && pbro.getRowCount() > 0) {
            if (ScoreCardConstants.SCARD_MEMBER_TYPE_DIM_KPI.equalsIgnoreCase(pbro.getFieldValueString(0, "SCARD_TYPE"))) {
                isDimensionBased = true;
            } else {
                isDimensionBased = false;
            }
        }
        return isDimensionBased;
    }

    private String getJsonForMeasures(ArrayList<String> elementIdsLst, ArrayList<String> elementNamesLst) {
        StringBuilder builder = new StringBuilder();
        builder.append("{");
        StringBuilder elementIdsBuilder = new StringBuilder();
        StringBuilder elementNamesBuilder = new StringBuilder();
        elementIdsBuilder.append("ElementIds:[");
        elementNamesBuilder.append("ElementNames:[");
        for (int i = 0; i < elementIdsLst.size(); i++) {
            elementIdsBuilder.append("\"").append(elementIdsLst.get(i)).append("\"");
            elementNamesBuilder.append("\"").append(elementNamesLst.get(i)).append("\"");
            if (i != elementIdsLst.size() - 1) {
                elementIdsBuilder.append(",");
                elementNamesBuilder.append(",");
            }
        }
        elementIdsBuilder.append("],");
        elementNamesBuilder.append("]");
        builder.append(elementIdsBuilder).append(elementNamesBuilder);
        builder.append("}");
        return builder.toString();
    }

    private String getModifiedValue(String value, String units) {
        String modifiedVal = "";
        if (!"".equalsIgnoreCase(value)) {
            if (units.equalsIgnoreCase("Thousands")) {
                modifiedVal = String.valueOf(new BigDecimal(Double.parseDouble(value) / 1000));
            } else if (units.equalsIgnoreCase("Lakhs")) {
                modifiedVal = String.valueOf(new BigDecimal(Double.parseDouble(value) / 100000));
            } else if (units.equalsIgnoreCase("Millions")) {
                modifiedVal = String.valueOf(new BigDecimal(Double.parseDouble(value) / 1000000));
            } else if (units.equalsIgnoreCase("Crores")) {
                modifiedVal = String.valueOf(new BigDecimal(Double.parseDouble(value) / 10000000));
            } else if (units.equalsIgnoreCase("Billions")) {
                modifiedVal = String.valueOf(new BigDecimal(Double.parseDouble(value) / 1000000000));
            } else {
                return value;
            }
        }
        return modifiedVal;
    }

    public void editMemberDetails(String sCardId, ScoreCard scoreCard) {
        ScoreCardMemberRuleTransferObject transObj = new ScoreCardMemberRuleTransferObject();
        String getMemberIds = getResourceBundle().getString("getMemberIds");
        String getScardMemDetails = getResourceBundle().getString("editScardMemberDetails");
        String editScoreCardMemKpiRules = getResourceBundle().getString("editScardMemberKpiRules");
        PbDb pbdb = new PbDb();
        PbReturnObject pbro = new PbReturnObject();
        PbReturnObject pbroMembs = new PbReturnObject();
        PbReturnObject pbroMemberDet = new PbReturnObject();
        Object bindObj[] = null;
        String finalQry = "";
        String units = "";
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
            bindObj = new Object[1];
            bindObj[0] = sCardId;
            finalQry = pbdb.buildQuery(getMemberIds, bindObj);
        } else {
            bindObj = new Object[1];
            bindObj[0] = sCardId;

            finalQry = pbdb.buildQuery(getMemberIds, bindObj);
        }
        try {
            pbroMemberDet = pbdb.execSelectSQL(pbdb.buildQuery(getScardMemDetails, bindObj));
            pbroMembs = pbdb.execSelectSQL(finalQry);
            bindObj = new Object[2];
            List<ScorecardComponent> scoreCardCompLst = scoreCard.getMemberList();
            ScoreCardMember scoreCardMem = null;
            ScoreCard childScoreCard = null;
            ScoreCardMemberRule scoreCardMemberRule = null;
            ArrayList<ScoreCardMemberRule> scoreCardMemRuleLst = null;
            for (int i = 0; i < pbroMembs.getRowCount(); i++) {
                bindObj[0] = sCardId;
                bindObj[1] = pbroMembs.getFieldValueString(i, "SCARD_MEM_ID");
                finalQry = pbdb.buildQuery(editScoreCardMemKpiRules, bindObj);
                pbro = pbdb.execSelectSQL(finalQry);
                if (scoreCardCompLst != null && scoreCardCompLst.size() > 0 && scoreCardCompLst.get(i) instanceof ScoreCardMember) {
                    scoreCardMem = (ScoreCardMember) scoreCardCompLst.get(i);
                    scoreCardMem.setContribution(Double.parseDouble(pbroMemberDet.getFieldValueString(i, "SCARD_CONTRIBUTION")));
                    scoreCardMem.setPeriod(pbroMemberDet.getFieldValueString(i, "PERIOD"));
                    scoreCardMem.setUnit(pbroMemberDet.getFieldValueString(i, "UNITS"));
                    scoreCardMem.setType(pbroMemberDet.getFieldValueString(i, "SCARD_TYPE"));
                    scoreCardMemRuleLst = new ArrayList<ScoreCardMemberRule>();
                    for (int j = 0; j < pbro.getRowCount(); j++) {
                        scoreCardMemberRule = new ScoreCardMemberRule();
                        scoreCardMemberRule.setScore(Double.parseDouble(pbro.getFieldValueString(j, "SCARD_SCORE")));
                        scoreCardMemberRule.setOperator(pbro.getFieldValueString(j, "OPERATOR"));
                        scoreCardMemberRule.setStartValue(Double.parseDouble(getModifiedValue(pbro.getFieldValueString(j, "SCARD_MEM_VALUE_ST"), pbroMemberDet.getFieldValueString(i, "UNITS"))));
                        if (!"<>".equalsIgnoreCase(pbro.getFieldValueString(j, "OPERATOR"))) {
                            scoreCardMemberRule.setEndValue(0.0);
                        } else {
                            scoreCardMemberRule.setEndValue(Double.parseDouble(getModifiedValue(pbro.getFieldValueString(j, "SCARD_MEM_VALUE_END"), pbroMemberDet.getFieldValueString(i, "UNITS"))));
                        }
                        scoreCardMemRuleLst.add(scoreCardMemberRule);
                    }
                    scoreCardMem.setRuleList(scoreCardMemRuleLst);
                } else {
                    childScoreCard = (ScoreCard) scoreCardCompLst.get(i);
                    this.editScoreCardMaster("", childScoreCard.getScoreCardId().replace("Scards", ""), childScoreCard);
                    if (!childScoreCard.getScoreCardId().startsWith("Scards")) {
                        childScoreCard.setScoreCardId("Scards" + childScoreCard.getScoreCardId());
                    } else {
                        childScoreCard.setScoreCardId(childScoreCard.getScoreCardId());
                    }
                    childScoreCard.setContribution(Double.parseDouble(pbroMemberDet.getFieldValueString(i, "SCARD_CONTRIBUTION")));
                    scoreCardMemRuleLst = new ArrayList<ScoreCardMemberRule>();
                    if (pbro != null && pbro.getRowCount() > 0) {
                        for (int j = 0; j < pbro.getRowCount(); j++) {
                            scoreCardMemberRule = new ScoreCardMemberRule();
                            scoreCardMemberRule.setScore(Double.parseDouble(pbro.getFieldValueString(j, "SCARD_SCORE")));
                            scoreCardMemberRule.setOperator(pbro.getFieldValueString(j, "OPERATOR"));
                            scoreCardMemberRule.setStartValue(Double.parseDouble(getModifiedValue(pbro.getFieldValueString(j, "SCARD_MEM_VALUE_ST"), pbroMemberDet.getFieldValueString(i, "UNITS"))));
                            if (!"<>".equalsIgnoreCase(pbro.getFieldValueString(j, "OPERATOR"))) {
                                scoreCardMemberRule.setEndValue(0.0);
                            } else {
                                scoreCardMemberRule.setEndValue(Double.parseDouble(getModifiedValue(pbro.getFieldValueString(j, "SCARD_MEM_VALUE_END"), pbroMemberDet.getFieldValueString(i, "UNITS"))));
                            }
                            scoreCardMemRuleLst.add(scoreCardMemberRule);
                        }
                    }
                    childScoreCard.setRuleList(scoreCardMemRuleLst);
                    //this.editScoreCardMembersInDimensionBased(childScoreCard, sCardId);
                }
            }

        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
    }

    public String editScoreCardMembersInDimensionBased(ScoreCard scoreCard, String scoreCardId) {
        String getScardMemDetails = getResourceBundle().getString("editScardMemberDetails");
        PbDb pbdb = new PbDb();
        PbReturnObject pbro = new PbReturnObject();
        Object[] bindObj = new Object[1];
        bindObj[0] = scoreCardId;
        ArrayList<ScorecardComponent> scoreCardCompLst = new ArrayList<ScorecardComponent>();
        ArrayList<String> elementIdsLst = new ArrayList<String>();
        ArrayList<String> elementNamesLst = new ArrayList<String>();
        try {
            pbro = pbdb.execSelectSQL(pbdb.buildQuery(getScardMemDetails, bindObj));
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }

        if (pbro != null && pbro.getRowCount() > 0) {
            for (int i = 0; i < pbro.getRowCount(); i++) {
                ScoreCard scoreCardChild = new ScoreCard();
                if (pbro.getFieldValueString(i, "CHILD_SCARD_ID") != null && !"".equalsIgnoreCase(pbro.getFieldValueString(i, "CHILD_SCARD_ID"))) {
                    this.createChildScoreCardForDimension(scoreCardChild, pbro.getFieldValueString(i, "CHILD_SCARD_ID"));
                    this.editMemberDetails(pbro.getFieldValueString(i, "CHILD_SCARD_ID"), scoreCardChild);
                    scoreCardChild.setContribution(Double.parseDouble(pbro.getFieldValueString(i, "SCARD_CONTRIBUTION")));
                    scoreCardCompLst.add(scoreCardChild);
                    elementIdsLst.add(((ScoreCardMember) scoreCardChild.getMemberList().get(0)).getElementId());
                    elementNamesLst.add(((ScoreCardMember) scoreCardChild.getMemberList().get(0)).getElementName());
                    scoreCardChild.setScoreCardId(((ScoreCardMember) scoreCardChild.getMemberList().get(0)).getElementId());
                    scoreCardChild.setScoreCardName(((ScoreCardMember) scoreCardChild.getMemberList().get(0)).getElementName());
                }
            }
        }
        scoreCard.setMemberList(scoreCardCompLst);
        return this.getJsonForMeasures(elementIdsLst, elementNamesLst);
    }

    private ScoreCard createChildScoreCardForDimension(ScoreCard scoreCard, String scoreCardId) {
        String editScoreCardMemKpi = getResourceBundle().getString("editScardMemberKpi");
        PbDb pbdb = new PbDb();
        PbReturnObject pbro = new PbReturnObject();
        Object[] bindObj = new Object[1];
        bindObj[0] = scoreCardId;
        ScoreCardMember scoreCardMem = null;
        ArrayList<ScorecardComponent> scoreCardCompsLst = new ArrayList<ScorecardComponent>();
        try {
            pbro = pbdb.execSelectSQL(pbdb.buildQuery(editScoreCardMemKpi, bindObj));
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        for (int i = 0; i < pbro.getRowCount(); i++) {
            scoreCardMem = new ScoreCardMember();
            scoreCardMem.setElementId(pbro.getFieldValueString(i, "ELEMENT_ID"));
            String elementName = pbro.getFieldValueString(i, "ELEMENT_NAME");
            scoreCardMem.setElementName(elementName);
            scoreCardMem.setDimElementId(pbro.getFieldValueString(i, "DIMENSION_ID"));
            scoreCardMem.setDimValue(pbro.getFieldValueString(i, "DIMENSION_VALUE"));
            scoreCardMem.setBasis(pbro.getFieldValueString(i, "SCARD_BASIS"));
            scoreCardMem.setTargetElementId(pbro.getFieldValueString(i, "TARGET_ELEMENT_ID"));
            scoreCardMem.setTargetElementName(pbro.getFieldValueString(i, "TARGET_ELEMENT_NAME"));
            String targetType = pbro.getFieldValueString(i, "TARGET_TYPE");
            if (ScoreCardConstants.TARGET_VALUE.equalsIgnoreCase(targetType)) {
                scoreCardMem.setTargetMeasureType(targetType);
                scoreCardMem.setTargetMeasureValue(Double.parseDouble(pbro.getFieldValueString(i, "TARGET_VALUE")));
            }
            //scoreCardMem.setMemberId(pbro.getFieldValueString(i, "SCARD_MEM_ID"));
            scoreCardCompsLst.add(scoreCardMem);
        }
        scoreCard.setMemberList(scoreCardCompsLst);
        return scoreCard;
    }

    private String getScorecardWeightageUnitsPeriods(String sCardId) {
        String getScardWeightage = getResourceBundle().getString("editScardMemberDetails");
        PbDb pbdb = new PbDb();
        PbReturnObject pbro = new PbReturnObject();
        Object bindObj[] = null;
        StringBuilder builder = new StringBuilder();
        StringBuilder unitsBuilder = new StringBuilder("Units:[");
        StringBuilder periodBuilder = new StringBuilder("Period:[");
        builder.append("Weightage:[");
        String finalQry = "";
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
            bindObj = new Object[1];
            bindObj[0] = sCardId;
            finalQry = pbdb.buildQuery(getScardWeightage, bindObj);
        } else {
            bindObj = new Object[1];
            bindObj[0] = sCardId;
            finalQry = pbdb.buildQuery(getScardWeightage, bindObj);
        }
        try {
            pbro = pbdb.execSelectSQL(finalQry);
            if (pbro.getRowCount() > 0) {
                for (int i = 0; i < pbro.getRowCount(); i++) {
                    builder.append("\"").append(pbro.getFieldValueInt(i, "SCARD_CONTRIBUTION")).append("\"");
                    unitsBuilder.append("\"").append(pbro.getFieldValueString(i, "UNITS")).append("\"");
                    periodBuilder.append("\"").append(pbro.getFieldValueString(i, "PERIOD")).append("\"");
                    if (i != pbro.getRowCount() - 1) {
                        builder.append(",");
                        unitsBuilder.append(",");
                        periodBuilder.append(",");
                    }
                }
            }
            unitsBuilder.append("],");
            periodBuilder.append("],");
            builder.append("],");
            builder.append(unitsBuilder);
            builder.append(periodBuilder);
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        return builder.toString();
    }

    public void updateScardMaster(String sName, String area, String userId, String colorType, String scoreCardId) {
        String updateScardMaster = getResourceBundle().getString("updateScardMaster");
        PbDb pbdb = new PbDb();
        PbReturnObject pbro = new PbReturnObject();
        String finalQry = "";
        Object[] bindObj = null;
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
            bindObj = new Object[5];
            bindObj[0] = sName;
            bindObj[1] = area;
            bindObj[2] = colorType;
            bindObj[3] = "getdate()";
            bindObj[4] = scoreCardId;
        } else {
            bindObj = new Object[5];
            bindObj[0] = sName;
            bindObj[1] = area;
            bindObj[2] = colorType;
            bindObj[3] = "sysdate";
            bindObj[4] = scoreCardId;
        }
        finalQry = pbdb.buildQuery(updateScardMaster, bindObj);
        try {
            pbdb.execUpdateSQL(finalQry);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
    }

    public ArrayList<ScoreCardColorRule> editScardLights(String sCardId) {
        ArrayList<ScoreCardColorRule> scoreCardColorRuleLst = new ArrayList<ScoreCardColorRule>();
        ScoreCardColorRule colorRule = null;
        String editScardLights = getResourceBundle().getString("editScardLights");
        PbDb pbdb = new PbDb();
        PbReturnObject pbro = new PbReturnObject();
        String finalQry = "";
        Object[] bindObj = null;
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
            bindObj = new Object[1];
            bindObj[0] = sCardId;
        } else {
            bindObj = new Object[1];
            bindObj[0] = sCardId;

        }
        finalQry = pbdb.buildQuery(editScardLights, bindObj);
        try {
            pbro = pbdb.execSelectSQL(finalQry);
            if (pbro != null && pbro.getRowCount() > 0) {
                for (int i = 0; i < pbro.getRowCount(); i++) {
                    colorRule = new ScoreCardColorRule();
                    colorRule.setColor(pbro.getFieldValueString(i, "LIGHT_COLOR"));
                    colorRule.setOperator(pbro.getFieldValueString(i, "OPERATOR"));
                    colorRule.setStartValue(Double.parseDouble(pbro.getFieldValueString(i, "SCORE_START")));
                    if ("<>".equalsIgnoreCase(pbro.getFieldValueString(i, "OPERATOR"))) {
                        colorRule.setEndValue(Double.parseDouble(pbro.getFieldValueString(i, "SCORE_END")));
                    } else {
                        colorRule.setEndValue(0.0);
                    }
                    scoreCardColorRuleLst.add(colorRule);
                }
            }
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        return scoreCardColorRuleLst;
    }

    public ArrayList<ScoreCardMeasure> getScoreCardMeasureLst(String userId, String folderId, String measureIds, String timlevel) {
        HashMap<String, ArrayList<String>> measureData = null;
        String measureList[] = measureIds.split(",");

        ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < measureList.length; i++) {
            if (!measureList[i].startsWith("Scards")) {
                list.add(measureList[i]);
            }
        }

        ScoreCardMeasure sCardMeasure = null;
        ArrayList<ScoreCardMeasure> scoreCardMeasLst = new ArrayList<ScoreCardMeasure>();
        if (!list.isEmpty()) {
            TrendDataSet trendDataSetYr = TrendDataSetHelper.buildTrendDataSet(list, "Year", userId, folderId);
            TrendDataSet trendDataSetQtr = TrendDataSetHelper.buildTrendDataSet(list, "Qtr", userId, folderId);
            TrendDataSet trendDataSetMonth = TrendDataSetHelper.buildTrendDataSet(list, "Month", userId, folderId);
            TrendDataSet trendDataSetDay = TrendDataSetHelper.buildTrendDataSet(list, "Day", userId, folderId);
            ScoreCardMeasureMetrics measMetrics = null;
            int periodCt = 0;
            ArrayList<ScoreCardMeasureMetrics> measMetxLst = null;
            HashMap<String, ArrayList<ScoreCardMeasureMetrics>> measMetxMap = new HashMap<String, ArrayList<ScoreCardMeasureMetrics>>();


            for (String measure : list) {
                sCardMeasure = new ScoreCardMeasure();
                measMetxMap = new HashMap<String, ArrayList<ScoreCardMeasureMetrics>>();

                measMetxLst = this.getMeasureDataForPeriod(trendDataSetYr, measure, "Year");
                measMetxMap.put("Year", measMetxLst);

                measMetxLst = this.getMeasureDataForPeriod(trendDataSetQtr, measure, "Quarter");
                measMetxMap.put("Qtr", measMetxLst);

                measMetxLst = this.getMeasureDataForPeriod(trendDataSetMonth, measure, "Month");
                measMetxMap.put("Month", measMetxLst);

                measMetxLst = this.getMeasureDataForPeriod(trendDataSetDay, measure, "Day");
                measMetxMap.put("Day", measMetxLst);

                sCardMeasure.setMeasId(measure);
                sCardMeasure.setMeasMetricsLst(measMetxMap);
                scoreCardMeasLst.add(sCardMeasure);

            }
        }
        Gson gson = new Gson();
        String json = gson.toJson(scoreCardMeasLst);
        // ---" + gson.toJson(scoreCardMeasLst));

        return scoreCardMeasLst;
    }

    public ArrayList<ScoreCardMeasure> getScoreCardMeasureLstForDimension(String userId, String folderId, String measureIds, String timlevel, String dimId, List<String> dimValues) {
        HashMap<String, ArrayList<String>> measureData = null;
        String measureList[] = measureIds.split(",");
        ArrayList<String> list = new ArrayList<String>();
        for (String measure : measureList) {
            list.add(measure);
        }
        ReportParameter reportParameter = new ReportParameter();
        // 
        reportParameter.setParameter(dimId, dimValues, false);
//        String paramValues[] = dimValues.split(",");

//         for(int i=0;i<dimValues.length;i++)
//         {
//             reportParameter.setParameter(dimId,dimValues[i],false);
//         }
        ArrayList<String> rowViewBys = new ArrayList<String>();
        rowViewBys.add(dimId);
        // rowViewBys.add("TIME");
        reportParameter.setViewBys(rowViewBys, new ArrayList<String>());

        ScoreCardMeasure sCardMeasure = null;
        HashMap<String, TrendDataSet> trendDataSetYr = TrendDataSetHelper.buildTrendDataSet(list, reportParameter, "Year", userId, folderId);
        HashMap<String, TrendDataSet> trendDataSetQtr = TrendDataSetHelper.buildTrendDataSet(list, reportParameter, "Qtr", userId, folderId);
        HashMap<String, TrendDataSet> trendDataSetMonth = TrendDataSetHelper.buildTrendDataSet(list, reportParameter, "Month", userId, folderId);
        HashMap<String, TrendDataSet> trendDataSetDay = TrendDataSetHelper.buildTrendDataSet(list, reportParameter, "Day", userId, folderId);
        ScoreCardMeasureMetrics measMetrics = null;
        int periodCt = 0;
        ArrayList<ScoreCardMeasureMetrics> measMetxLst = null;
        HashMap<String, ArrayList<ScoreCardMeasureMetrics>> measMetxMap = new HashMap<String, ArrayList<ScoreCardMeasureMetrics>>();
        ArrayList<ScoreCardMeasure> scoreCardMeasLst = new ArrayList<ScoreCardMeasure>();

        //for(String measure:measureList)
        for (String measure : list) {
            for (String paramVal : dimValues) {
                sCardMeasure = new ScoreCardMeasure();
                sCardMeasure.setDimId(dimId);
                sCardMeasure.setDimValue(paramVal);
                measMetxMap = new HashMap<String, ArrayList<ScoreCardMeasureMetrics>>();

                measMetxLst = this.getMeasureDataForPeriod(trendDataSetYr.get(paramVal), measure, "Year");
                measMetxMap.put("Year", measMetxLst);

                measMetxLst = this.getMeasureDataForPeriod(trendDataSetQtr.get(paramVal), measure, "Quarter");
                measMetxMap.put("Qtr", measMetxLst);

                measMetxLst = this.getMeasureDataForPeriod(trendDataSetMonth.get(paramVal), measure, "Month");
                measMetxMap.put("Month", measMetxLst);

                measMetxLst = this.getMeasureDataForPeriod(trendDataSetDay.get(paramVal), measure, "Day");
                measMetxMap.put("Day", measMetxLst);

                sCardMeasure.setMeasId(measure);
                sCardMeasure.setMeasMetricsLst(measMetxMap);
                scoreCardMeasLst.add(sCardMeasure);

            }
        }

        Gson gson = new Gson();
        String json = gson.toJson(scoreCardMeasLst);
        //  ---" + gson.toJson(scoreCardMeasLst));

        return scoreCardMeasLst;
    }

    private ArrayList<ScoreCardMeasureMetrics> getMeasureDataForPeriod(TrendDataSet dataSet, String measure, String period) {
        ArrayList<ScoreCardMeasureMetrics> measMetxLst = new ArrayList<ScoreCardMeasureMetrics>();
        ScoreCardMeasureMetrics measMetrics = new ScoreCardMeasureMetrics();
        int periodCt = 0;
        if (dataSet != null) {
            periodCt = dataSet.getTimeRangeCount();
            measMetrics.setLabel("Maximum in Last " + periodCt + " " + period + "(s)");
            measMetrics.setMeasValue(String.valueOf(dataSet.findMaxInPeriod("A_" + measure)));
            measMetrics.setMeasValAsBigDecimal(dataSet.findMaxInPeriod("A_" + measure));
            measMetxLst.add(measMetrics);

            measMetrics = new ScoreCardMeasureMetrics();
            measMetrics.setLabel("Minmum in Last " + periodCt + " " + period + "(s)");
            measMetrics.setMeasValue(String.valueOf(dataSet.findMinInPeriod("A_" + measure)));
            measMetrics.setMeasValAsBigDecimal(dataSet.findMinInPeriod("A_" + measure));
            measMetxLst.add(measMetrics);

            measMetrics = new ScoreCardMeasureMetrics();
            measMetrics.setLabel("Average in Last " + periodCt + " " + period + "(s)");
            measMetrics.setMeasValue(String.valueOf(dataSet.findLastNPeriodAverage("A_" + measure, periodCt)));
            measMetrics.setMeasValAsBigDecimal(dataSet.findLastNPeriodAverage("A_" + measure, periodCt));
            measMetxLst.add(measMetrics);

            measMetrics = new ScoreCardMeasureMetrics();
            measMetrics.setLabel("Last " + period + " value");
            measMetrics.setMeasValue(String.valueOf(dataSet.getLastPeriodValue("A_" + measure)));
            measMetrics.setMeasValAsBigDecimal(dataSet.getLastPeriodValue("A_" + measure));
            measMetxLst.add(measMetrics);


            if (periodCt > 3) {
                measMetrics = new ScoreCardMeasureMetrics();
                measMetrics.setLabel(" Last 3 " + period + " Average");
                measMetrics.setMeasValue(String.valueOf(dataSet.findLastNPeriodAverage("A_" + measure, 3)));
                measMetrics.setMeasValAsBigDecimal(dataSet.findLastNPeriodAverage("A_" + measure, 3));
                measMetxLst.add(measMetrics);
            } else {
                measMetrics = new ScoreCardMeasureMetrics();
                measMetrics.setLabel(" Last 3 " + period + " Average");
                measMetrics.setMeasValue("null");
                measMetrics.setMeasValAsBigDecimal(BigDecimal.ZERO);
                measMetxLst.add(measMetrics);
            }
        }
        return measMetxLst;
    }

    private ArrayList<ScoreCardMeasureMetrics> getDimensionDataForPeriod(TrendDataSet dataSet, String measure, String period) {
        ArrayList<ScoreCardMeasureMetrics> measMetxLst = new ArrayList<ScoreCardMeasureMetrics>();
        ScoreCardMeasureMetrics measMetrics = new ScoreCardMeasureMetrics();
        int periodCt = 0;
        if (dataSet != null) {
            periodCt = dataSet.getTimeRangeCount();
            measMetrics.setLabel("Maximum in Last " + periodCt + " " + period + "(s)");
            measMetrics.setMeasValue(dataSet.findMaxInPeriod("A_" + measure).toString());
            measMetrics.setMeasValAsBigDecimal(dataSet.findMaxInPeriod("A_" + measure));
            measMetxLst.add(measMetrics);

            measMetrics = new ScoreCardMeasureMetrics();
            measMetrics.setLabel("Minmum in Last " + periodCt + " " + period + "");
            measMetrics.setMeasValue(dataSet.findMinInPeriod("A_" + measure).toString());
            measMetrics.setMeasValAsBigDecimal(dataSet.findMinInPeriod("A_" + measure));
            measMetxLst.add(measMetrics);

            measMetrics = new ScoreCardMeasureMetrics();
            measMetrics.setLabel("Average in Last " + periodCt + " " + period + "");
            measMetrics.setMeasValue(dataSet.findLastNPeriodAverage("A_" + measure, periodCt).toString());
            measMetrics.setMeasValAsBigDecimal(dataSet.findLastNPeriodAverage("A_" + measure, periodCt));
            measMetxLst.add(measMetrics);

            measMetrics = new ScoreCardMeasureMetrics();
            measMetrics.setLabel("Last " + period + " value");
            measMetrics.setMeasValue(dataSet.getLastPeriodValue("A_" + measure).toString());
            measMetrics.setMeasValAsBigDecimal(dataSet.getLastPeriodValue("A_" + measure));
            measMetxLst.add(measMetrics);


            if (periodCt > 3) {
                measMetrics = new ScoreCardMeasureMetrics();
                measMetrics.setLabel(" Last 3 " + period + " Average");
                measMetrics.setMeasValue(dataSet.findLastNPeriodAverage("A_" + measure, 3).toString());
                measMetrics.setMeasValAsBigDecimal(dataSet.findLastNPeriodAverage("A_" + measure, 3));
                measMetxLst.add(measMetrics);
            } else {
                measMetrics = new ScoreCardMeasureMetrics();
                measMetrics.setLabel(" Last 3 " + period + " Average");
                measMetrics.setMeasValue("null");
                measMetrics.setMeasValAsBigDecimal(BigDecimal.ZERO);
                measMetxLst.add(measMetrics);
            }
        }
        return measMetxLst;
    }

    public String changeValuesAsPerUnit(ScoreCardMeasure sCardMeas, String period, String unit) {
        PbReturnObject returnObj = new PbReturnObject();
        HashMap<String, ArrayList<ScoreCardMeasureMetrics>> measMetrixMap = new HashMap<String, ArrayList<ScoreCardMeasureMetrics>>();
        measMetrixMap = sCardMeas.getMeasMetricsLst();
        BigDecimal bigMax = null;
        BigDecimal bigMin = null;
        BigDecimal bigAvg = null;
        BigDecimal bigLastPeriodVal = null;
        BigDecimal bigLastThreePeriodVal = null;

        if (period.equalsIgnoreCase("Quarter")) {
            period = "Qtr";
        }
        ArrayList<ScoreCardMeasureMetrics> sCardMeasMtxLst = measMetrixMap.get(period);

        bigMax = sCardMeasMtxLst.get(0).getMeasValAsBigDecimal();
        bigMin = sCardMeasMtxLst.get(1).getMeasValAsBigDecimal();
        bigAvg = sCardMeasMtxLst.get(2).getMeasValAsBigDecimal();
        bigLastPeriodVal = sCardMeasMtxLst.get(3).getMeasValAsBigDecimal();
        bigLastThreePeriodVal = sCardMeasMtxLst.get(4).getMeasValAsBigDecimal();

        StringBuilder jsonString = new StringBuilder("{");

        if (unit.equalsIgnoreCase("Thousands")) {
            jsonString.append("Max:");
            jsonString.append("\"").append(returnObj.getModifiedNumber(bigMax, "K")).append("\",");
            jsonString.append("Min:");
            jsonString.append("\"").append(returnObj.getModifiedNumber(bigMin, "K")).append("\",");
            jsonString.append("Avg:");
            jsonString.append("\"").append(returnObj.getModifiedNumber(bigAvg, "K")).append("\",");
            jsonString.append("LastPeriodValue:");
            jsonString.append("\"").append(returnObj.getModifiedNumber(bigLastPeriodVal, "K")).append("\",");
            jsonString.append("LastThreePeriodValue:");
            jsonString.append("\"").append(returnObj.getModifiedNumber(bigLastThreePeriodVal, "K")).append("\"");


        } else if (unit.equalsIgnoreCase("Millions")) {
            jsonString.append("Max:");
            jsonString.append("\"").append(returnObj.getModifiedNumber(bigMax, "Mn")).append("\",");
            jsonString.append("Min:");
            jsonString.append("\"").append(returnObj.getModifiedNumber(bigMin, "Mn")).append("\",");
            jsonString.append("Avg:");
            jsonString.append("\"").append(returnObj.getModifiedNumber(bigAvg, "Mn")).append("\",");
            jsonString.append("LastPeriodValue:");
            jsonString.append("\"").append(returnObj.getModifiedNumber(bigLastPeriodVal, "Mn")).append("\",");
            jsonString.append("LastThreePeriodValue:");
            jsonString.append("\"").append(returnObj.getModifiedNumber(bigLastThreePeriodVal, "Mn")).append("\"");


        } else if (unit.equalsIgnoreCase("Billions")) {
            jsonString.append("Max:");
            jsonString.append("\"").append(returnObj.getModifiedNumber(bigMax, "Bn")).append("\",");
            jsonString.append("Min:");
            jsonString.append("\"").append(returnObj.getModifiedNumber(bigMin, "Bn")).append("\",");
            jsonString.append("Avg:");
            jsonString.append("\"").append(returnObj.getModifiedNumber(bigAvg, "Bn")).append("\",");
            jsonString.append("LastPeriodValue:");
            jsonString.append("\"").append(returnObj.getModifiedNumber(bigLastPeriodVal, "Bn")).append("\",");
            jsonString.append("LastThreePeriodValue:");
            jsonString.append("\"").append(returnObj.getModifiedNumber(bigLastThreePeriodVal, "Bn")).append("\"");



        } else if (unit.equalsIgnoreCase("Lakhs")) {
            jsonString.append("Max:");
            jsonString.append("\"").append(returnObj.getModifiedNumber(bigMax, "L")).append("\",");
            jsonString.append("Min:");
            jsonString.append("\"").append(returnObj.getModifiedNumber(bigMin, "L")).append("\",");
            jsonString.append("Avg:");
            jsonString.append("\"").append(returnObj.getModifiedNumber(bigAvg, "L")).append("\",");
            jsonString.append("LastPeriodValue:");
            jsonString.append("\"").append(returnObj.getModifiedNumber(bigLastPeriodVal, "L")).append("\",");
            jsonString.append("LastThreePeriodValue:");
            jsonString.append("\"").append(returnObj.getModifiedNumber(bigLastThreePeriodVal, "L")).append("\"");

        } else if (unit.equalsIgnoreCase("Crores")) {
            jsonString.append("Max:");
            jsonString.append("\"").append(returnObj.getModifiedNumber(bigMax, "Cr")).append("\",");
            jsonString.append("Min:");
            jsonString.append("\"").append(returnObj.getModifiedNumber(bigMin, "Cr")).append("\",");
            jsonString.append("Avg:");
            jsonString.append("\"").append(returnObj.getModifiedNumber(bigAvg, "Cr")).append("\",");
            jsonString.append("LastPeriodValue:");
            jsonString.append("\"").append(returnObj.getModifiedNumber(bigLastPeriodVal, "Cr")).append("\",");
            jsonString.append("LastThreePeriodValue:");
            jsonString.append("\"").append(returnObj.getModifiedNumber(bigLastThreePeriodVal, "Cr")).append("\"");

        } else {
            jsonString.append("Max:");
            jsonString.append("\"").append(returnObj.getModifiedNumber(bigMax, "")).append("\",");
            jsonString.append("Min:");
            jsonString.append("\"").append(returnObj.getModifiedNumber(bigMin, "")).append("\",");
            jsonString.append("Avg:");
            jsonString.append("\"").append(returnObj.getModifiedNumber(bigAvg, "")).append("\",");
            jsonString.append("LastPeriodValue:");
            jsonString.append("\"").append(returnObj.getModifiedNumber(bigLastPeriodVal, "")).append("\",");
            jsonString.append("LastThreePeriodValue:");
            jsonString.append("\"").append(returnObj.getModifiedNumber(bigLastThreePeriodVal, "")).append("\"");

        }
        //jsonString.append(startValuesJson).append(endValuesJson);
        jsonString.append("}");
        // 
        //
        return jsonString.toString();
    }

    public boolean isScorecardNameExists(String sCardName) {
        boolean isExists = false;
        PbDb pbdb = new PbDb();
        PbReturnObject pbro = null;
        String sCardNamesQry = getResourceBundle().getString("getScorecardNames");
        try {
            pbro = pbdb.execSelectSQL(sCardNamesQry);
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        if (pbro != null && pbro.getRowCount() > 0) {
            for (int i = 0; i < pbro.getRowCount(); i++) {
                if (sCardName.equalsIgnoreCase(pbro.getFieldValueString(i, "SCARD_NAME"))) {
                    return true;
                }
            }

        }
        return false;
    }

    public String getPriorAndChangeMeasures(String measureId, String scoreBasis) {
        // ArrayList list = new ArrayList();
        UserLayerDAO userLayerDao = new UserLayerDAO();
        HashMap<String, String> refEleMap = userLayerDao.getPriorAndChangeMeasures(measureId);
        String refEleId = "";
        if (scoreBasis.equalsIgnoreCase("Change%")) {
            refEleId = refEleMap.get("Change%");
        } else if (scoreBasis.equalsIgnoreCase("Change Amount")) {
            refEleId = refEleMap.get("Change");
        } else if (scoreBasis.equalsIgnoreCase("Absolute")) {
            refEleId = measureId;
        } else {
            refEleId = refEleMap.get("Prior");
        }

        // list.add(refEleId);
        return refEleId;
    }

    public String getJsonForScorecardBasis(String refEleId, String period, String userId, String folderId, String scoreBasis) {
        StringBuilder builder = new StringBuilder();

        ArrayList<ScoreCardMeasure> scoreCardMeasLst = null;


        scoreCardMeasLst = this.getScoreCardMeasureLst(userId, folderId, refEleId, period);
//        if("true".equalsIgnoreCase(isDimBased)){
//          String dimValues=dims.replace("~",",");
//           sCardMeasLst=designBd.getScoreCardMeasureLstForDimension( userId, folderId, measureIds, timeLevel,dimId,dimValues);
//        }else{
//
//           sCardMeasLst=designBd.getScoreCardMeasureLst( userId, folderId, measureIds, timeLevel);
//        }
        // scoreCardMeasLst=this.getScoreCardMeasureLstForDimension(userId, folderId, measureId, refEleId, userId, measureId);
        Gson gson = new Gson();
        // 
        return gson.toJson(scoreCardMeasLst);
    }

    public HashMap<String, ArrayList<String>> getDimensionDetailsMap(String measId, String members) {
        HashMap<String, ArrayList<String>> dimMap = new HashMap<String, ArrayList<String>>();
        ArrayList<String> dimsList = new ArrayList<String>();
        String memsStr[] = members.split(",");
        dimsList.addAll(Arrays.asList(memsStr));
        dimMap.put(measId, dimsList);
        return dimMap;
    }

    public String getCreatedScoreCards(String userId, String contextPath) {
        StringBuilder builder = new StringBuilder();
        PbDb pbdb = new PbDb();
        Object bindObj[] = new Object[1];
        bindObj[0] = userId;
        PbReturnObject pbro = null;

        String getScoreCards = getResourceBundle().getString("getScoreCardList");
        String finalQry = pbdb.buildQuery(getScoreCards, bindObj);
        try {
            pbro = pbdb.execSelectSQL(finalQry);
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        builder.append("<li class='closed' id='Scorecards'>");
        builder.append("<img src='" + contextPath + "/images/folder-closed.gif' style='width:20px;height:20px'></img>");
        builder.append("<span style='font-family:verdana;' title='Scorecards'>Scorecards</span>");
        builder.append("<ul id='Scorecards'>");
        builder.append(getCreatedScoresLst(pbro, contextPath));
        builder.append("</ul>");
        builder.append("</li>");
        return builder.toString();
    }

    private String getCreatedScoresLst(PbReturnObject pbro, String contextPath) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < pbro.getRowCount(); i++) {
            builder.append("<li class='closed' id='" + pbro.getFieldValueInt(i, "SCARD_ID") + "'>");
            builder.append("<img src='" + contextPath + "/images/sun.png'></img>");
            builder.append("<span id='Scards" + pbro.getFieldValueInt(i, "SCARD_ID") + "'  title='" + pbro.getFieldValueString(i, "SCARD_NAME") + "' style='font-family:verdana;'>" + pbro.getFieldValueString(i, "SCARD_NAME") + "</span>");
            builder.append("</li>");
        }

        return builder.toString();
    }

    public static void main(String args[]) {
        ScorecardDesignBd designBd = new ScorecardDesignBd();
        ArrayList list = new ArrayList();
        list.add("78363");
        list.add("78429");
        list.add("75733");
        designBd.getScoreCardMeasureLst("41", "1203", "78363,78429", "Month");
    }

    public ScoreCard createScoreCard(String scoreCardName, String scoreCardDesc, String folderId, String userId) {
        ScoreCard scoreCard = new ScoreCard();
        scoreCard.setScoreCardName(scoreCardName);
        scoreCard.setScoreCardArea(scoreCardDesc);
        scoreCard.setFolderId(folderId);
        scoreCard.setUserId(userId);
        return scoreCard;
    }

    public ArrayList<ScoreCardColorRule> createScoreCardColorRules(ArrayList<String> colors, ArrayList<String> operators, ArrayList<Double> startValues, ArrayList<Double> endValues) {
        ArrayList<ScoreCardColorRule> colorRuleLst = new ArrayList<ScoreCardColorRule>();
        for (int i = 0; i < colors.size(); i++) {
            ScoreCardColorRule rule = this.createScoreCardColorRule(colors.get(i), operators.get(i), startValues.get(i), endValues.get(i));
            colorRuleLst.add(rule);
        }
        return colorRuleLst;
    }

    public ScoreCardColorRule createScoreCardColorRule(String color, String operator, Double startValue, Double endValue) {
        ScoreCardColorRule scoreCardRule = null;
        scoreCardRule = new ScoreCardColorRule();
        scoreCardRule.setColor(color);
        scoreCardRule.setOperator(operator);
        scoreCardRule.setStartValue(startValue);
        scoreCardRule.setEndValue(endValue);
        return scoreCardRule;
    }

    public ArrayList<ScorecardComponent> createScoreCardComponents(ArrayList<String> componentTypes, ArrayList<String> elementIdsLst, ArrayList<String> elementNamesLst) {
        ArrayList<ScorecardComponent> scoreCardComponents = new ArrayList<ScorecardComponent>();
        for (int i = 0; i < elementIdsLst.size(); i++) {
            ScorecardComponent component = this.createScoreCardComponent(componentTypes.get(i), elementIdsLst.get(i), elementNamesLst.get(i));
            scoreCardComponents.add(component);
        }
        return scoreCardComponents;
    }

    public ScorecardComponent createScoreCardComponent(String type, String elementId, String elementName) {
        ScorecardComponent scoreCardMember = null;
        if (type.equals(ScoreCardConstants.SCARD_MEMBER_TYPE_KPI)) {
            scoreCardMember = new ScoreCardMember();
            ((ScoreCardMember) scoreCardMember).setElementId(elementId);
            ((ScoreCardMember) scoreCardMember).setElementName(elementName);
        } else {
            scoreCardMember = new ScoreCard();
            ((ScoreCard) scoreCardMember).setScoreCardId(elementId);
            ((ScoreCard) scoreCardMember).setScoreCardName(elementName);
        }
        return scoreCardMember;
    }

    public void setRulesForScoreCardMember(ScoreCardMemberRuleTransferObject transObj, ScoreCard scoreCard, String elementId, String compType) {
        ScoreCardMember scoreCardMember = null;
        ScoreCard scoreCardChild = null;
        ScoreCardMember childScoreCardMember = null;
        ArrayList<ScoreCardMemberRule> scoreCardMemRuleLst = new ArrayList<ScoreCardMemberRule>();
        ScoreCardMemberRule scoreCardMemberRule = null;
        for (int i = 0; i < transObj.scores.size(); i++) {
            scoreCardMemberRule = new ScoreCardMemberRule();
            scoreCardMemberRule.setScore(transObj.scores.get(i));
            scoreCardMemberRule.setOperator(transObj.operators.get(i));
            scoreCardMemberRule.setStartValue(transObj.stValues.get(i));
            scoreCardMemberRule.setEndValue(transObj.endValues.get(i));
            scoreCardMemRuleLst.add(scoreCardMemberRule);
        }

        if (compType.equalsIgnoreCase(ScoreCardConstants.SCARD_MEMBER_TYPE_DIM_KPI)) {
            scoreCardChild = (ScoreCard) scoreCard.getScoreCardMemberScoreCardPredicate(elementId);
            scoreCardChild.setDimensionBased(true);
            scoreCardChild.setUserId(transObj.userId);
            childScoreCardMember = (ScoreCardMember) scoreCardChild.getChildScoreCardMemberPredicate(transObj.dimensionValue);
            childScoreCardMember.setRuleList(scoreCardMemRuleLst);
            childScoreCardMember.setPeriod(transObj.period);
            childScoreCardMember.setType(compType);
            childScoreCardMember.setBasis(transObj.scoreBasis);
            childScoreCardMember.setUnit(transObj.unit);
            childScoreCardMember.setTargetMeasureType(transObj.targetMeasureType);
            childScoreCardMember.setTargetMeasureValue(transObj.targetMeasureValue);

        } else if (compType.equalsIgnoreCase(ScoreCardConstants.SCARD_MEMBER_TYPE_NESTED_SCARD)) {
            scoreCardChild = (ScoreCard) scoreCard.getScoreCardMemberScoreCardPredicate(elementId);
            scoreCardChild.setRuleList(scoreCardMemRuleLst);
        } else {
            scoreCardMember = (ScoreCardMember) scoreCard.getScoreCardMemberKPIPredicate(elementId);
            scoreCardMember.setPeriod(transObj.period);
            scoreCardMember.setType(compType);
            scoreCardMember.setBasis(transObj.scoreBasis);
            scoreCardMember.setUnit(transObj.unit);
            scoreCardMember.setRuleList(scoreCardMemRuleLst);
            scoreCardMember.setTargetMeasureType(transObj.targetMeasureType);
            scoreCardMember.setTargetMeasureValue(transObj.targetMeasureValue);
        }
    }

    private boolean areRulesAvailable(ScoreCard scoreCard, String measId, String dimValue, String compType) {
        boolean ruleAvailable = false;
        ScoreCardMember scoreCardMember = null;
        ScoreCard scoreCardChild = null;
        ScoreCardMember childScoreCardMember = null;
        Gson gson = new Gson();
        if (compType.equalsIgnoreCase(ScoreCardConstants.SCARD_MEMBER_TYPE_DIM_KPI)) {
            scoreCardChild = (ScoreCard) scoreCard.getScoreCardMemberScoreCardPredicate(measId);
            if (scoreCardChild.getChildScoreCardMemberPredicate(dimValue) != null) {
                childScoreCardMember = (ScoreCardMember) scoreCardChild.getChildScoreCardMemberPredicate(dimValue);
            }
            if (childScoreCardMember != null && childScoreCardMember.getRuleList() != null && childScoreCardMember.getRuleList().size() > 0) {
                return true;
            } else {
                return false;
            }
        } else if (compType.equalsIgnoreCase(ScoreCardConstants.SCARD_MEMBER_TYPE_NESTED_SCARD)) {
            scoreCardChild = (ScoreCard) scoreCard.getScoreCardMemberScoreCardPredicate(measId);
            if (scoreCardChild.getRuleList() != null && scoreCardChild.getRuleList().size() > 0) {
                return true;
            } else {
                return false;
            }
        } else {
            scoreCardMember = (ScoreCardMember) scoreCard.getScoreCardMemberKPIPredicate(measId);
            if (scoreCardMember != null && scoreCardMember.getRuleList() != null && scoreCardMember.getRuleList().size() > 0) {
                return true;
            } else {
                return false;
            }
        }

    }

    public String getAssignedRulesForComponent(ScoreCard scoreCard, String measId, String dimValue, String compType) {
        ScoreCardMember scoreCardMember = null;
        ScoreCard scoreCardChild = null;
        ScoreCardMember childScoreCardMember = null;
        Gson gson = new Gson();
        if (compType.equalsIgnoreCase(ScoreCardConstants.SCARD_MEMBER_TYPE_DIM_KPI)) {
            scoreCardChild = (ScoreCard) scoreCard.getScoreCardMemberScoreCardPredicate(measId);
            if (scoreCardChild != null && scoreCardChild.getChildScoreCardMemberPredicate(dimValue) != null) {
                childScoreCardMember = (ScoreCardMember) scoreCardChild.getChildScoreCardMemberPredicate(dimValue);
            }
            if (childScoreCardMember != null && this.areRulesAvailable(scoreCard, measId, dimValue, compType)) {
                return gson.toJson(childScoreCardMember);
            } else {
                return "";
            }
        } else if (compType.equalsIgnoreCase(ScoreCardConstants.SCARD_MEMBER_TYPE_NESTED_SCARD)) {

            scoreCardChild = (ScoreCard) scoreCard.getScoreCardMemberScoreCardPredicate(measId);
            if (this.areRulesAvailable(scoreCard, scoreCardChild.getScoreCardId(), dimValue, compType)) {
                return gson.toJson(scoreCardChild);
            } else {
                return "";
            }
        } else {
            scoreCardMember = (ScoreCardMember) scoreCard.getScoreCardMemberKPIPredicate(measId);
            if (this.areRulesAvailable(scoreCard, measId, dimValue, compType)) {
                return gson.toJson(scoreCardMember);
            } else {
                return "";
            }
        }
        // return "";
    }

    public void setWeightageForComponent(ScoreCard scoreCard, ArrayList<String> measIdsLst, ArrayList<Double> measWeighLst, ArrayList<String> compTypeLst, String selMeasId, String weightType) {
        ScoreCardMember scoreCardMember = null;
        ScoreCard scoreCardChild = null;
        ScoreCardMember childScoreCardMember = null;
        for (int i = 0; i < measIdsLst.size(); i++) {
            if (compTypeLst.get(i).equalsIgnoreCase(ScoreCardConstants.SCARD_MEMBER_TYPE_DIM_KPI)) {
                if (weightType.equalsIgnoreCase("weightForMeas")) {
                    scoreCardChild = (ScoreCard) scoreCard.getScoreCardMemberScoreCardPredicate(measIdsLst.get(i));
                    scoreCardChild.setContribution(measWeighLst.get(i));
                } else {
                    scoreCardChild = (ScoreCard) scoreCard.getScoreCardMemberScoreCardPredicate(selMeasId);
                    childScoreCardMember = (ScoreCardMember) scoreCardChild.getChildScoreCardMemberPredicate(measIdsLst.get(i));
                    childScoreCardMember.setContribution(measWeighLst.get(i));
                }

            } else if (compTypeLst.get(i).equalsIgnoreCase(ScoreCardConstants.SCARD_MEMBER_TYPE_KPI)) {
                scoreCardMember = (ScoreCardMember) scoreCard.getScoreCardMemberKPIPredicate(measIdsLst.get(i));
                scoreCardMember.setContribution(measWeighLst.get(i));
            } else {
                scoreCardChild = (ScoreCard) scoreCard.getScoreCardMemberScoreCardPredicate(measIdsLst.get(i));
                scoreCardChild.setContribution(measWeighLst.get(i));


            }
        }
    }

    public String getJsonForWeightage(ScoreCard scoreCard, String currMeasId) {
        ScoreCardMember scoreCardMember = null;
        ScoreCard scoreCardChild = null;
        ScoreCardMember childScoreCardMember = null;

        //   if (compType.equalsIgnoreCase(ScoreCardConstants.SCARD_MEMBER_TYPE_DIM_KPI)) {
        if (currMeasId.equalsIgnoreCase("weightForMeas")) {
            return this.getJsonWeightageForMeasuresInDimKpi(scoreCard);

        } else {
            scoreCardChild = (ScoreCard) scoreCard.getScoreCardMemberScoreCardPredicate(currMeasId);
            return this.getJsonWeightageForDimKpi(scoreCardChild);
        }

        // }

        // return "";
    }

    private String getJsonWeightageForDimKpi(ScoreCard scoreCard) {
        List<ScorecardComponent> scoreCardMemLst = scoreCard.getMemberList();
        StringBuilder builder = new StringBuilder();
        builder.append("{");
        ScoreCardMember sCardMem = null;
        int i = 0;
        for (ScorecardComponent sCardComp : scoreCardMemLst) {
            sCardMem = (ScoreCardMember) sCardComp;
            builder.append("\"").append(sCardMem.getDimValue()).append("\"");
            builder.append(":");
            builder.append("\"").append(sCardMem.getContribution()).append("\"");
            if (i != scoreCardMemLst.size() - 1) {
                builder.append(",");
            }
            i++;
        }
        builder.append("}");

        return builder.toString();
    }

    private String getJsonWeightageForMeasuresInDimKpi(ScoreCard scoreCard) {
        List<ScorecardComponent> scoreCardMemLst = scoreCard.getMemberList();
        StringBuilder builder = new StringBuilder();
        builder.append("{");
        ScoreCard sCardChild = null;
        int i = 0;
        for (ScorecardComponent sCardComp : scoreCardMemLst) {
            sCardChild = (ScoreCard) sCardComp;
            builder.append("\"").append(sCardChild.getScoreCardId()).append("\"");
            builder.append(":");
            builder.append("\"").append(sCardChild.getContribution()).append("\"");
            if (i != scoreCardMemLst.size() - 1) {
                builder.append(",");
            }
            i++;
        }
        builder.append("}");

        return builder.toString();
    }

    public String getJsonWeightageForKpi(ScoreCard scoreCard) {
        ScoreCardMember scoreCardMember = null;
        ScoreCard childScoreCard = null;
        StringBuilder builder = new StringBuilder();
        builder.append("{");
        int i = 0;
        for (ScorecardComponent scoreCardComp : scoreCard.getMemberList()) {
            if (scoreCardComp instanceof ScoreCardMember) {
                scoreCardMember = ((ScoreCardMember) scoreCardComp);
                builder.append("\"").append(scoreCardMember.getElementId()).append("\"");
                builder.append(":");
                builder.append("\"").append(scoreCardMember.getContribution()).append("\"");
            } else {
                childScoreCard = (ScoreCard) scoreCardComp;
                builder.append("\"").append(childScoreCard.getScoreCardId()).append("\"");
                builder.append(":");
                builder.append("\"").append(childScoreCard.getContribution()).append("\"");
            }
            if (i != scoreCard.getMemberList().size() - 1) {
                builder.append(",");
            }
            i++;
        }
        builder.append("}");
        return builder.toString();
    }

    public boolean allRulesAppliedForComponents(ScoreCard scoreCard) {
        boolean allApplied = false;
        List<ScorecardComponent> scoreCardMemLst = scoreCard.getMemberList();
        ScoreCard sCardChild = null;
        ScoreCardMember scoreCardMem = null;
        int i = 0;

        if (scoreCard.isDimensionBased()) {
            for (ScorecardComponent sCardComp : scoreCardMemLst) {
                sCardChild = (ScoreCard) sCardComp;
                for (ScorecardComponent scoreCardComp : sCardChild.getMemberList()) {
                    ScoreCardMember childScoreMem = (ScoreCardMember) scoreCardComp;
                    if (childScoreMem.getRuleList() == null || childScoreMem.getRuleList().isEmpty()) {
                        return false;
                    }
                }
            }
            return true;
        } else {
            for (ScorecardComponent sCardComp : scoreCardMemLst) {
                if (sCardComp instanceof ScoreCardMember) {
                    scoreCardMem = (ScoreCardMember) sCardComp;
                    if (scoreCardMem.getRuleList() == null || scoreCardMem.getRuleList().isEmpty()) {
                        return false;
                    }
                } else {
                    // sCardChild=(ScoreCard)sCardComp;
//                     if(sCardChild.getRuleList()==null||sCardChild.getRuleList().isEmpty())
//                         return true;
                }
            }
            return true;
        }

    }

    public String getJsonForCreatedDimensions(ScoreCard scoreCard) {
        StringBuilder builder = new StringBuilder();
        List<ScorecardComponent> scoreCardCompLst = scoreCard.getMemberList();
        ScoreCard scoreCardChild = null;
        String dimElementId = "";
        ScoreCard scoreCardComponent = null;
        scoreCardComponent = (ScoreCard) scoreCardCompLst.get(0);
        if (scoreCardComponent instanceof ScoreCard) {
            builder.append("{");
            scoreCardChild = (ScoreCard) scoreCardComponent;
            dimElementId = this.getDimensionElementId(scoreCardChild);
            builder.append("DimElementId:");
            builder.append("\"").append(dimElementId).append("\"").append(",");
            builder.append(this.getDimensionMembers(scoreCardChild));
            builder.append("}");
        }
        return builder.toString();
    }

    public String getDimensionElementId(ScoreCard scoreCard) {
        List<ScorecardComponent> scoreCardCompLst = scoreCard.getMemberList();
        if (scoreCardCompLst.get(0) instanceof ScoreCardMember) {
            return ((ScoreCardMember) scoreCardCompLst.get(0)).getDimElementId();
        } else {
            return "";
        }
    }

    public String getDimensionMembers(ScoreCard scoreCard) {
        StringBuilder builder = new StringBuilder();
        List<ScorecardComponent> scoreCardCompLst = scoreCard.getMemberList();
        String dimElementId = this.getDimensionElementId(scoreCard);
        builder.append("DimMembers:[");
        int i = 0;
        for (ScorecardComponent scoreCardComp : scoreCardCompLst) {
            if (scoreCardCompLst.get(i) instanceof ScoreCardMember) {
                builder.append("\"").append(((ScoreCardMember) scoreCardCompLst.get(i)).getDimValue()).append("\"");
            }
            if (i != scoreCardCompLst.size() - 1) {
                builder.append(",");
            }
            i++;
        }
        builder.append("]");
        return builder.toString();
    }

    public void copySameValuesToAll(ScoreCard scoreCard, String measureId, String dimValue, String compType, ArrayList<String> measuresLst, ArrayList<String> dimsLst) {
        ScoreCard scoreCardChild = null;
        ScoreCardMember childScoreCardMember = null;
        ScoreCardMember scoreCardMember = null;
        List<ScoreCardMemberRule> scoreCardMemRuleLst = new ArrayList<ScoreCardMemberRule>();
        boolean isDimBased = scoreCard.isDimensionBased();
        if (compType.equalsIgnoreCase(ScoreCardConstants.SCARD_MEMBER_TYPE_DIM_KPI)) {
            scoreCardChild = (ScoreCard) scoreCard.getScoreCardMemberScoreCardPredicate(measureId);
            if (scoreCardChild != null && scoreCardChild.getChildScoreCardMemberPredicate(dimValue) != null) {
                childScoreCardMember = (ScoreCardMember) scoreCardChild.getChildScoreCardMemberPredicate(dimValue);
            }
            scoreCardMemRuleLst = childScoreCardMember.getRuleList();
        } else if (compType.equalsIgnoreCase(ScoreCardConstants.SCARD_MEMBER_TYPE_NESTED_SCARD)) {

            scoreCardChild = (ScoreCard) scoreCard.getScoreCardMemberScoreCardPredicate(measureId);
            scoreCardMemRuleLst = scoreCardChild.getRuleList();
        } else {
            scoreCardMember = (ScoreCardMember) scoreCard.getScoreCardMemberKPIPredicate(measureId);
            scoreCardMemRuleLst = scoreCardMember.getRuleList();
        }
        ArrayList<Double> scoresLst = new ArrayList<Double>();
        ArrayList<String> operatorsLst = new ArrayList<String>();
        ArrayList<Double> startValsLst = new ArrayList<Double>();
        ArrayList<Double> endValsLst = new ArrayList<Double>();
        for (ScoreCardMemberRule rule : scoreCardMemRuleLst) {
            scoresLst.add(rule.getScore());
            operatorsLst.add(rule.getOperator());
            startValsLst.add(rule.getStartValue());
            endValsLst.add(rule.getEndValue());
        }


        for (String measure : measuresLst) {
            ScoreCardMemberRuleTransferObject memRuleTransObject = new ScoreCardMemberRuleTransferObject();
            if (!isDimBased) {

                memRuleTransObject.measureId = measure;
                memRuleTransObject.period = scoreCardMember.getPeriod();
                memRuleTransObject.unit = scoreCardMember.getUnit();
                memRuleTransObject.scoreBasis = scoreCardMember.getBasis();
                memRuleTransObject.scores = scoresLst;
                memRuleTransObject.operators = operatorsLst;
                memRuleTransObject.stValues = startValsLst;
                memRuleTransObject.endValues = endValsLst;
                memRuleTransObject.userId = scoreCard.getUserId();

                if (isDimBased) {
                    memRuleTransObject.dimensionId = "";
                    memRuleTransObject.dimensionValue = dimValue;
                }
                if (!measure.startsWith("Scards")) {
                    this.setRulesForScoreCardMember(memRuleTransObject, scoreCard, measure, ScoreCardConstants.SCARD_MEMBER_TYPE_KPI);
                }
            } else {
                for (String dimension : dimsLst) {
                    memRuleTransObject.measureId = measure;
                    memRuleTransObject.period = childScoreCardMember.getPeriod();
                    memRuleTransObject.unit = childScoreCardMember.getUnit();
                    memRuleTransObject.scoreBasis = childScoreCardMember.getBasis();
                    memRuleTransObject.scores = scoresLst;
                    memRuleTransObject.operators = operatorsLst;
                    memRuleTransObject.stValues = startValsLst;
                    memRuleTransObject.endValues = endValsLst;
                    memRuleTransObject.userId = scoreCard.getUserId();

                    if (isDimBased) {
                        memRuleTransObject.dimensionId = dimension;
                        memRuleTransObject.dimensionValue = dimension;
                    }
                    this.setRulesForScoreCardMember(memRuleTransObject, scoreCard, measure, ScoreCardConstants.SCARD_MEMBER_TYPE_DIM_KPI);
                }


            }
        }
    }

    public void saveTargetMeasure(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        String measureId = request.getParameter("measId");
        String targetMeasureName = request.getParameter("targetMeasName");
        String targetMeasId = request.getParameter("targetMeasId");
        String dimValue = request.getParameter("dimValue");
        boolean isDimBased = false;
        if (request.getParameter("isDimBased") != null) {
            isDimBased = Boolean.parseBoolean(request.getParameter("isDimBased"));
        }

        ScorecardDesignBd designBd = new ScorecardDesignBd();
        ScoreCard scoreCard = (ScoreCard) session.getAttribute("ScoreCard");

        ScoreCard scoreCardChild = null;
        ScoreCardMember childScoreCardMember = null;
        ScoreCardMember scoreCardMember = null;
        if (isDimBased) {
            scoreCardChild = (ScoreCard) scoreCard.getScoreCardMemberScoreCardPredicate(measureId);
            if (scoreCardChild != null && scoreCardChild.getChildScoreCardMemberPredicate(dimValue) != null) {
                childScoreCardMember = (ScoreCardMember) scoreCardChild.getChildScoreCardMemberPredicate(dimValue);
            }
            if (childScoreCardMember != null) {
                childScoreCardMember.setTargetElementId(targetMeasId);
                childScoreCardMember.setTargetElementName(targetMeasureName);
            }
        } else {
            scoreCardMember = (ScoreCardMember) scoreCard.getScoreCardMemberKPIPredicate(measureId);
            if (scoreCardMember != null) {
                scoreCardMember.setTargetElementId(targetMeasId);
                scoreCardMember.setTargetElementName(targetMeasureName);
            }
        }
    }
}
