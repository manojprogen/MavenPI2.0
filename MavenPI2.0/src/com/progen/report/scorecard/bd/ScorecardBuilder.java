/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.scorecard.bd;

import com.google.common.collect.Iterables;
import com.progen.report.display.util.NumberFormatter;
import com.progen.report.scorecard.*;
import com.progen.report.scorecard.db.ScorecardDAO;
import com.progen.report.scorecard.query.ScorecardQueryBuilder;
import com.progen.report.scorecard.query.ScorecardQueryDetails;
import com.progen.report.scorecard.query.ScorecardQueryResult;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import prg.db.PbReturnObject;

/**
 *
 * @author progen
 */
public class ScorecardBuilder {

    public String buildScoreCard(ScoreCard scorecard, String userId, String folderId, ArrayList timeDetails) {
        StringBuilder output = new StringBuilder();

        // Get the scorecard query details from the scorecard and call scorecardQueryBuilder to get the scorecard query resultset
        Iterable<ScorecardQueryDetails> queryDetails = scorecard.getQueryDetails();
        ScorecardQueryBuilder queryBuilder = new ScorecardQueryBuilder();
        Iterable<ScorecardQueryResult> queryResult = queryBuilder.buildScorecardResultset(queryDetails, userId, folderId, timeDetails);

        // Build the score object
        buildScoreObject(scorecard, queryResult);

        // Build the UI
        output = buildScoreCardUI(scorecard, userId, folderId);

        return output.toString();
    }

    public void buildScoreObject(ScoreCard scorecard, Iterable<ScorecardQueryResult> queryResult) {

        List<ScorecardComponent> memberList = scorecard.getMemberList();

        for (ScorecardComponent component : memberList) {
            if (component instanceof ScoreCardMember) {
                ScoreCardMember scMember = (ScoreCardMember) component;
                Iterable<ScorecardQueryResult> filter = Iterables.filter(queryResult, ScorecardQueryResult.getResultForMember(scMember.getMemberId()));
                Iterator<ScorecardQueryResult> iter = filter.iterator();

                if (iter.hasNext()) {
                    ScorecardQueryResult result = iter.next();
                    buildScoreCardMemberScore(scMember, result);
                }
            } else if (component instanceof ScoreCard) {
                ScoreCard sc = (ScoreCard) component;
                buildScoreObject(sc, queryResult);
            }
        }

        Double currentScore = null;
        Double priorScore = null;
        Double targetScore = null;
        //Create the score object for scorecard
        for (ScorecardComponent member : memberList) {
            Score score = member.getScore();
            if (score != null) {
                if (score.getCurrentScore() != null) {
                    if (currentScore == null) {
                        currentScore = (member.getContribution() / 100 * score.getCurrentScore());
                    } else {
                        currentScore += (member.getContribution() / 100 * score.getCurrentScore());
                    }
                }
                if (score.getPriorScore() != null) {
                    if (priorScore == null) {
                        priorScore = (member.getContribution() / 100 * score.getPriorScore());
                    } else {
                        priorScore += (member.getContribution() / 100 * score.getPriorScore());
                    }
                }
                if (score.getTargetScore() != null) {
                    if (targetScore == null) {
                        targetScore = (member.getContribution() / 100 * score.getTargetScore());
                    } else {
                        targetScore += (member.getContribution() / 100 * score.getTargetScore());
                    }
                }
            }
        }

        String lightType = scorecard.getLightType();
        String bgColor = "";
        if (ScoreCardConstants.LIGHT_TYPE_TARGET.equalsIgnoreCase(lightType)) {
            bgColor = scorecard.getLightColor(currentScore);
        } else if (ScoreCardConstants.LIGHT_TYPE_PRIOR.equalsIgnoreCase(lightType)) {
            bgColor = getScoreColor(currentScore, priorScore);
        }

        String lightIcon = getLightIcon(bgColor);
        String toolTip = getRuleTooltip(scorecard.getRuleList(), 1, "", true);

        ScorecardScore scScore = new ScorecardScore();
        scScore.setScoreCardName(scorecard.getScoreCardName());
        scScore.setPriorScore(priorScore);
        scScore.setCurrentScore(currentScore);
        scScore.setContribution(scorecard.getContribution());
        scScore.setLightIcon(lightIcon);
        scScore.setRuleHtml(toolTip);
        scScore.setTargetScore(targetScore);
        scorecard.setScore(scScore);
    }

    private void buildScoreCardMemberScore(ScoreCardMember scMember, ScorecardQueryResult result) {
        PbReturnObject retObj = result.getRetObj();
        PbReturnObject priorRetObj = result.getPriorRetObj();
        String basis = scMember.getBasis();
        String elemId = "";
        String targetEleId = "";
        String targetMeasureType = "";
        Double targetMeasureValue = null;
        BigDecimal targetElementValue = null;

        if (ScoreCardConstants.SCORECARD_BASIS_ABSOLUTE.equalsIgnoreCase(basis)) {
            elemId = scMember.getElementId();
        } else if (ScoreCardConstants.SCORECARD_BASIS_CHANGE_AMT.equalsIgnoreCase(basis)) {
            elemId = scMember.getChangeElementId();
        } else if (ScoreCardConstants.SCORECARD_BASIS_CHANGE_PERC.equalsIgnoreCase(basis)) {
            elemId = scMember.getChangePercElementId();
        } else if (ScoreCardConstants.SCORECARD_BASIS_TARGET.equalsIgnoreCase(basis)
                || ScoreCardConstants.SCORECARD_BASIS_TARGET_PERC.equalsIgnoreCase(basis)) {
            elemId = scMember.getElementId();
            targetMeasureType = scMember.getTargetMeasureType();
            if (ScoreCardConstants.TARGET_MEASURE.equalsIgnoreCase(targetMeasureType)) {
                targetEleId = scMember.getTargetElementId();
            } else if (ScoreCardConstants.TARGET_VALUE.equalsIgnoreCase(targetMeasureType)) {
                targetElementValue = new BigDecimal(scMember.getTargetMeasureValue());
            }
        }

        BigDecimal elementValue = null;
        BigDecimal priorElementValue = null;

        if (ScoreCardConstants.SCARD_MEMBER_TYPE_DIM_KPI.equalsIgnoreCase(scMember.getType())) {
            String dimId = scMember.getDimElementId();
            String dimValue = scMember.getDimValue();
            elementValue = getValueForDimMember(retObj, dimId, dimValue, elemId);
            priorElementValue = getValueForDimMember(priorRetObj, dimId, dimValue, elemId);

            if (ScoreCardConstants.TARGET_MEASURE.equalsIgnoreCase(targetMeasureType)
                    && (ScoreCardConstants.SCORECARD_BASIS_TARGET.equalsIgnoreCase(basis)
                    || ScoreCardConstants.SCORECARD_BASIS_TARGET_PERC.equalsIgnoreCase(basis))) {
                targetElementValue = getValueForDimMember(retObj, dimId, dimValue, targetEleId);
            }
        } else {
            if (retObj != null) {
                elementValue = retObj.getFieldValueBigDecimal(0, "A_" + elemId);
            }
            if (priorRetObj != null) {
                priorElementValue = priorRetObj.getFieldValueBigDecimal(0, "A_" + elemId);
            }

            if (ScoreCardConstants.TARGET_MEASURE.equalsIgnoreCase(targetMeasureType)
                    && (ScoreCardConstants.SCORECARD_BASIS_TARGET.equalsIgnoreCase(basis)
                    || ScoreCardConstants.SCORECARD_BASIS_TARGET_PERC.equalsIgnoreCase(basis))) {
                targetElementValue = retObj.getFieldValueBigDecimal(0, "A_" + targetEleId);
            }
        }

        Double score = null;
        Double pScore = null;
        Double measureValue = null;
        Double priorMeasValue = null;
        Double targetMeasValue = null;
        Double tScore = null;
        Double targetDeviation = null;
        Double targetDevPercent = null;
        Double targetDeviationPrior = null;
        Double targetDevPercentPrior = null;
        if (elementValue != null) {
            measureValue = elementValue.doubleValue();
            Double normMeasureValue = changeValueToScardMemberPeriod(measureValue, scMember.getPeriod(), result.getNoOfDays());
            score = scMember.getScore(normMeasureValue);
        }
        if (priorElementValue != null) {
            priorMeasValue = priorElementValue.doubleValue();
            Double normPriorMeasValue = changeValueToScardMemberPeriod(priorMeasValue, scMember.getPeriod(), result.getPriorNoOfDays());
            pScore = scMember.getScore(normPriorMeasValue);
        }
        if (targetElementValue != null && measureValue != null && (ScoreCardConstants.SCORECARD_BASIS_TARGET.equalsIgnoreCase(basis)
                || ScoreCardConstants.SCORECARD_BASIS_TARGET_PERC.equalsIgnoreCase(basis))) {

            targetMeasValue = targetElementValue.doubleValue();
            Double nMeasureValue = changeValueToScardMemberPeriod(measureValue, scMember.getPeriod(), result.getNoOfDays());
            Double tMeasValue = changeValueToScardMemberPeriod(targetMeasValue, scMember.getPeriod(), result.getNoOfDays());

            targetDeviation = nMeasureValue - tMeasValue;
            targetDevPercent = targetDeviation / nMeasureValue * 100;

            if (priorMeasValue != null) {
                Double normPriorMeasValue = changeValueToScardMemberPeriod(priorMeasValue, scMember.getPeriod(), result.getPriorNoOfDays());
                targetDeviationPrior = normPriorMeasValue - tMeasValue;
                targetDevPercentPrior = targetDeviationPrior / normPriorMeasValue * 100;
            }

            if (ScoreCardConstants.SCORECARD_BASIS_TARGET.equalsIgnoreCase(basis)) {
                score = scMember.getScore(targetDeviation);
                if (targetDeviationPrior != null) {
                    pScore = scMember.getScore(targetDeviationPrior);
                }
            } else if (ScoreCardConstants.SCORECARD_BASIS_TARGET_PERC.equalsIgnoreCase(basis)) {
                score = scMember.getScore(targetDevPercent);
                if (targetDevPercentPrior != null) {
                    pScore = scMember.getScore(targetDevPercentPrior);
                }
            }
        }
        String ruleHtml = getRuleTooltip(scMember.getRuleList(), result.getNoOfDays(), scMember.getPeriod(), false);
        String bgColor = getScoreColor(score, pScore);
        String lightIcon = getLightIcon(bgColor);

        ScorecardMemberScore memberScore = new ScorecardMemberScore();
        memberScore.setElementId(elemId);
        memberScore.setElementName(scMember.getElementName());
        memberScore.setCurrentScore(score);
        memberScore.setPriorScore(pScore);
        memberScore.setCurrentValue(measureValue);
        memberScore.setPriorValue(priorMeasValue);
        memberScore.setLightIcon(lightIcon);
        memberScore.setContribution(scMember.getContribution());
        memberScore.setRuleHTML(ruleHtml);
//        memberScore.setTargetScore(tScore);
        memberScore.setTargetDev(targetDeviation);
        memberScore.setTargetDevPercent(targetDevPercent);
        scMember.setScore(memberScore);
    }

    private StringBuilder buildScoreCardUI(ScoreCard scorecard, String userId, String folderId) {
        StringBuilder output = new StringBuilder();
        String scardId = scorecard.getScoreCardId();
        //Scorecard Header
        output.append("<Table width=\"99%;height:auto\" cellpadding=\"1\" cellspacing=\"1\" id=\"tablesorter\"  class=\"tablesorter\">");
        output.append("<thead>");
        output.append("<tr>");
        output.append("<th/>");
        output.append("<th style='font-weight:bold;'>").append("ScoreCard").append("</th>");
        output.append("<th style='font-weight:bold;'>").append("Current Score").append("</th>");
        output.append("<th style='font-weight:bold;'>").append("Prior Score").append("</th>");
        output.append("<th style='font-weight:bold;'>").append("Target Score").append("</th>");
        output.append("<th>").append("").append("</th>");
        output.append("<th style='font-weight:bold;'>").append("History").append("</th>");
        output.append("<th style='font-weight:bold;'>").append("Action").append("</th>");
        output.append("<th style='font-weight:bold;'>").append("Past Action").append("</th>");
        output.append("</tr>");
        output.append("</thead>");
        output.append("<tbody>");

        //Main Scorecard Row
        Score score = scorecard.getScore();

        StringBuilder action = new StringBuilder();
        action.append("<a onClick='showActions(").append(scorecard.getScoreCardId()).append(",null,").append(score.getCurrentScore()).append(",\"").append(scorecard.getScoreCardName()).append("\")'><img src=\"images/actionIcon.gif\"/></a>");

        StringBuilder history = new StringBuilder();
        history.append("<a onClick='viewHistory(").append(scardId).append(",\"\"").append(")'><span class=\"ui-icon ui-icon-image\"></span></a>");

        String pastActionsLink = "<a onClick='showPastActions(" + scardId + ",null)'><span class=\"ui-icon ui-icon-calendar\"></span></a>";

        output.append("<tr>");
        output.append("<td rowspan=\"2\" class=\"collapsible\">");
        output.append("<a class=\"collapsed\"></a></td>");
        output.append("<td style='font-weight:bold;'>").append(scorecard.getScoreCardName()).append("</td>");
        if (score.getCurrentScore() != null) {
            output.append("<td style='font-weight:bold;'>").append(NumberFormatter.getModifiedNumber(new BigDecimal(score.getCurrentScore()), "", -1)).append("</td>");
        } else {
            output.append("<td style='font-weight:bold;'>").append("-").append("</td>");
        }
        if (score.getPriorScore() != null) {
            output.append("<td style='font-weight:bold;'>").append(NumberFormatter.getModifiedNumber(new BigDecimal(score.getPriorScore()), "", -1)).append("</td>");
        } else {
            output.append("<td style='font-weight:bold;'>").append("-").append("</td>");
        }
//        if (scorecard.getTargetScore() != null)
        output.append("<td style='font-weight:bold;'>").append(scorecard.getTargetScore()).append("</td>");
//        else
//            output.append("<td style='font-weight:bold;'>").append("-").append("</td>");
        output.append("<td align=\"center\">").append(score.getLightIcon()).append("</td>");
        output.append("<td align=\"center\">").append(history.toString()).append("</td>");
        output.append("<td align=\"center\">").append(action.toString()).append("</td>");
        output.append("<td align=\"center\">").append(pastActionsLink).append("</td>");
        output.append("</tr>");

        output.append("<tr class=\"expand-child\">");
        output.append("<td style=\"display: none;\" colspan=\"8\">");
        output.append("<table width=\"99%\"><thead><tr>");
        output.append("<th/>");
        output.append("<th>Measure/Dimension</th>");
        output.append("<th>Current Score</th>");
        output.append("<th>Prior Score</th>");
//        output.append("<th>Target Score</th>");
        output.append("<th>Current Value</th>");
        output.append("<th>Prior Value</th>");
        output.append("<th>Weightage</th>");
        output.append("<th></th>");
        output.append("<th>Rule</th>");
        output.append("<th>History</th>");
        output.append("<th>Action</th>");
        output.append("<th>Past Actions</th>");
        output.append("<th>Insights</th>");
        output.append("</tr></thead><tbody>");
        //Construct the rows for each member in the list
        for (ScorecardComponent component : scorecard.getMemberList()) {
            if (component instanceof ScoreCardMember) {
                output.append(buildUIForScoreCardMember(scardId, (ScoreCardMember) component, userId, folderId));
            } else if (component instanceof ScoreCard) {
                output.append(buildChildScoreCardUI((ScoreCard) component, userId, folderId));
            }
        }
        //Adding adHoc rows
        output.append(adHocRowsUIForScorecard(scardId));
        //closing tag for scorecard member
        output.append("</tbody></table></td></tr>");

        //closing table tag for main scorecard
        output.append("</tbody>");
        output.append("</Table>");

        return output;
    }

    private String buildChildScoreCardUI(ScoreCard scorecard, String userId, String folderId) {
        StringBuilder output = new StringBuilder();
        ScorecardScore score = (ScorecardScore) scorecard.getScore();
        String scardId = scorecard.getScoreCardId();

        String ruleLink = "<a class=\"bubbleAnchor\" tooltip=\"" + score.getRuleHtml() + "\"><span class=\"ui-icon ui-icon-info\"></span></a>";

        StringBuilder action = new StringBuilder();
        action.append("<a onClick='showActions(").append(scardId).append(",null,").append(score.getCurrentScore()).append(",\"").append(scorecard.getScoreCardName()).append("\")'><img src=\"images/actionIcon.gif\"/></a>");

        String pastActionsLink = "<a onClick='showPastActions(" + scardId + ",null)'><span class=\"ui-icon ui-icon-calendar\"></span></a>";

        StringBuilder history = new StringBuilder();
        history.append("<a onClick='viewHistory(").append(scardId).append(",\"\"").append(")'><span class=\"ui-icon ui-icon-image\"></span></a>");

        output.append("<tr>");
        output.append("<td rowspan=\"2\" class=\"collapsible\">");
        output.append("<a class=\"collapsed\"></a></td>");
        output.append("<td style='font-weight:bold;'>").append(scorecard.getScoreCardName()).append("</td>");
        if (score.getCurrentScore() != null) {
            output.append("<td style='font-weight:bold;'>").append(NumberFormatter.getModifiedNumber(new BigDecimal(score.getCurrentScore()), "", -1)).append("</td>");
        } else {
            output.append("<td style='font-weight:bold;'>").append("-").append("</td>");
        }
        if (score.getPriorScore() != null) {
            output.append("<td style='font-weight:bold;'>").append(NumberFormatter.getModifiedNumber(new BigDecimal(score.getPriorScore()), "", -1)).append("</td>");
        } else {
            output.append("<td style='font-weight:bold;'>").append("-").append("</td>");
        }
//        if (score.getTargetScore() != null)
//            output.append("<td style='font-weight:bold;'>").append(NumberFormatter.getModifiedNumber(new BigDecimal(score.getTargetScore() ),"",-1)).append("</td>");
//        else
//            output.append("<td style='font-weight:bold;'>").append("-").append("</td>");
        output.append("<td align=\"right\">-</td>");
        output.append("<td align=\"right\">-</td>");
        output.append("<td align=\"right\">").append(score.getContribution()).append("</td>");
        output.append("<td align=\"center\">").append(score.getLightIcon()).append("</td>");
        output.append("<td align=\"center\">").append(ruleLink).append("</td>");
        output.append("<td align=\"center\">").append(history.toString()).append("</td>");
        output.append("<td align=\"center\">").append(action.toString()).append("</td>");
        output.append("<td align=\"center\">").append(pastActionsLink).append("</td>");
        output.append("<td align=\"center\" ><a onclick=\"nestedInsights()\" style=\"cursor:pointer\"><span class=\"ui-icon ui-icon-search\"></span></a></td>");

        output.append("</tr>");
        output.append("<tr class=\"expand-child\">");
        output.append("<td style=\"display: none;\" colspan=\"12\">");
        output.append("<table width=\"99%\"><thead><tr>");
        output.append("<th></th>");
        output.append("<th>Measure/Dimension</th>");
        output.append("<th>Current Score</th>");
        output.append("<th>Prior Score</th>");
//        output.append("<th>Target Score</th>");
        output.append("<th>Current Value</th>");
        output.append("<th>Prior Value</th>");
        output.append("<th>Weightage</th>");
        output.append("<th></th>");
        output.append("<th>Rule</th>");
        output.append("<th>History</th>");
        output.append("<th>Action</th>");
        output.append("<th>Past Actions</th>");
        output.append("<th>Insights</th>");
        output.append("</tr></thead><tbody>");

        //Construct the rows for each member in the list
        for (ScorecardComponent component : scorecard.getMemberList()) {
            if (component instanceof ScoreCardMember) {
                output.append(buildUIForScoreCardMember(scardId, (ScoreCardMember) component, userId, folderId));
            } else if (component instanceof ScoreCard) {
                output.append(buildChildScoreCardUI((ScoreCard) component, userId, folderId));
            }
        }

        //closing tag for scorecard member
        output.append("</tbody></table></td></tr>");

        return output.toString();
    }

    private String buildUIForScoreCardMember(String scardId, ScoreCardMember member, String userId, String folderId) {
        StringBuilder output = new StringBuilder();
        ScorecardMemberScore score = (ScorecardMemberScore) member.getScore();

        String dimElementId = member.getDimElementId();
        String dimElementVal = member.getDimValue();
        String elementName = member.getElementName();
        String basis = member.getBasis();
        if (ScoreCardConstants.SCORECARD_BASIS_ABSOLUTE.equalsIgnoreCase(basis)) {
            basis = "";
        }
        if (member.getDimElementId() != null) {
            elementName = member.getDimValue();
        }

        String actionLink = "<a onClick='showActions(" + scardId + "," + member.getMemberId() + ","
                + score.getCurrentScore() + ",\"" + member.getElementName() + "\")'><img src=\"images/actionIcon.gif\"/></a>";
        String ruleLink = "<a class=\"bubbleAnchor\" tooltip=\"" + score.getRuleHTML() + "\"><span class=\"ui-icon ui-icon-info\"></span></a>";

        String pastActionsLink = "<a onClick='showPastActions(" + scardId + "," + member.getMemberId()
                + ")'><span class=\"ui-icon ui-icon-calendar\"></span></a>";

        StringBuilder history = new StringBuilder();
        history.append("<a onClick='viewHistory(").append(scardId).append(",").append(member.getMemberId()).append(")'><span class=\"ui-icon ui-icon-image\"></span></a>");

        output.append("<tr>");
        output.append("<td/>");
        output.append("<td>").append(basis + " " + elementName).append("</td>");

        if (score.getCurrentScore() != null) {
            output.append("<td style='font-weight:bold;'>").append(NumberFormatter.getModifiedNumber(new BigDecimal(score.getCurrentScore()), "", -1)).append("</td>");
        } else {
            output.append("<td style='font-weight:bold;'>").append("-").append("</td>");
        }
        if (score.getPriorScore() != null) {
            output.append("<td style='font-weight:bold;'>").append(NumberFormatter.getModifiedNumber(new BigDecimal(score.getPriorScore()), "", -1)).append("</td>");
        } else {
            output.append("<td style='font-weight:bold;'>").append("-").append("</td>");
        }
//        if (score.getTargetScore() != null)
//            output.append("<td style='font-weight:bold;'>").append(NumberFormatter.getModifiedNumber(new BigDecimal(score.getTargetScore() ),"",-1)).append("</td>");
//        else
//            output.append("<td style='font-weight:bold;'>").append("-").append("</td>");
        if (score.getCurrentValue() != null) {
            output.append("<td align=\"right\">").append(NumberFormatter.getModifiedNumber(new BigDecimal(score.getCurrentValue()), "", -1)).append("</td>");
        } else {
            output.append("<td align=\"right\">").append("-").append("</td>");
        }
        if (score.getPriorValue() != null) {
            output.append("<td align=\"right\">").append(NumberFormatter.getModifiedNumber(new BigDecimal(score.getPriorValue()), "", -1)).append("</td>");
        } else {
            output.append("<td align=\"right\">").append("-").append("</td>");
        }
        output.append("<td align=\"right\">").append(score.getContribution()).append("</td>");
        output.append("<td align=\"center\">").append(score.getLightIcon()).append("</td>");
        output.append("<td align=\"center\">").append(ruleLink).append("</td>");
        output.append("<td align=\"center\">").append(history).append("</td>");
        output.append("<td align=\"center\">").append(actionLink).append("</td>");
        output.append("<td align=\"center\">").append(pastActionsLink).append("</td>");
        output.append("<td align=\"center\">");
        output.append("<a onclick=\"gotoInsight(\'" + member.getElementId() + "\',\'" + userId + "\',\'" + folderId + "\',\'" + dimElementId + "\',\'" + dimElementVal + "\')\" style=\"cursor:pointer\"><span class=\"ui-icon ui-icon-search\"></span></a></td>");
        output.append("</tr>");
        return output.toString();
    }

    private String getScoreColor(Double currScore, Double priorScore) {
        if (currScore == null || priorScore == null) {
            return "";
        }

        if (currScore < priorScore) {
            return "red";
        } else if (currScore == priorScore) {
            return "blue";
        } else {
            return "green";
        }
    }

    private String getLightIcon(String bgColor) {
        String lightIcon = "";
        if ("red".equalsIgnoreCase(bgColor)) {
            lightIcon = "<img src='icons pinvoke/status-busy.png'></img>";
        } else if ("green".equalsIgnoreCase(bgColor)) {
            lightIcon = "<img src='icons pinvoke/status.png'></img>";
        } else if ("yellow".equalsIgnoreCase(bgColor)) {
            lightIcon = "<img src='icons pinvoke/status-offline.png'></img>";
        }

        return lightIcon;
    }

    private String getRuleTooltip(List<ScoreCardMemberRule> ruleList, int noOfDays, String compPeriod, boolean isScard) {
        StringBuilder toolTip = new StringBuilder();
        toolTip.append("<table><thead><tr><th style='font-weight:bold' align='left'>Condition</th><th width='8px'></th><th align='left' style='font-weight:bold'>Score</th></tr></thead><tbody>");
        for (ScoreCardMemberRule rule : ruleList) {
            toolTip.append("<tr>");
            String operator = rule.getOperator();
            toolTip.append("<td align='left'>");

            if (isScard) {
                if (ScoreCardConstants.OPERATOR_BETWEEN.equals(operator)) {
                    toolTip.append("between ").append(NumberFormatter.getModifiedNumber(new BigDecimal(rule.getStartValue()), "", -1)).append(" and ").append(NumberFormatter.getModifiedNumber(new BigDecimal(rule.getEndValue()), "", -1));
                } else {
                    toolTip.append(operator).append(" ").append(NumberFormatter.getModifiedNumber(new BigDecimal(rule.getStartValue()), "", -1));
                }
            } else {
                Double startVal = rule.getStartValue();
                int periodDays = getPeriodDays(compPeriod);
                startVal = startVal / periodDays * noOfDays;
                if (ScoreCardConstants.OPERATOR_BETWEEN.equals(operator)) {
                    Double endVal = rule.getEndValue();
                    endVal = endVal / periodDays * noOfDays;
                    toolTip.append("between ").append(NumberFormatter.getModifiedNumber(new BigDecimal(startVal), "", -1)).append(" and ").append(NumberFormatter.getModifiedNumber(new BigDecimal(endVal), "", -1));
                } else {
                    toolTip.append(operator).append(" ").append(NumberFormatter.getModifiedNumber(new BigDecimal(startVal), "", -1));
                }
            }
            toolTip.append("</td>");
            toolTip.append("<td width='8px'></td>");
            toolTip.append("<td align='left'>").append(rule.getScore()).append("</td>");
            toolTip.append("</tr>");
        }
        toolTip.append("</tbody></table>");
        return toolTip.toString();
    }

    private BigDecimal getValueForDimMember(PbReturnObject retObj, String dimElementId, String dimValue, String measElementId) {
        BigDecimal measValue = null;
        if (retObj != null && retObj.getRowCount() > 0) {
            for (int i = 0; i < retObj.getRowCount(); i++) {
                String rowDimVal = retObj.getFieldValueString(i, "A_" + dimElementId);
                if (rowDimVal.equalsIgnoreCase(dimValue)) {
                    measValue = retObj.getFieldValueBigDecimal(i, "A_" + measElementId);
                }
            }
        }

        return measValue;
    }

    private double changeValueToScardMemberPeriod(double measureValue, String period, int noOfDays) {
        if (noOfDays > 0) {
            int periodDays = getPeriodDays(period);
            measureValue = measureValue / noOfDays * periodDays;
        }
        return measureValue;
    }

    private int getPeriodDays(String period) {
        int periodDays = 1;
        if (period.equalsIgnoreCase("Month")) {
            periodDays = 30;
        } else if (period.equalsIgnoreCase("Quarter")) {
            periodDays = 90;
        } else if (period.equalsIgnoreCase("Year")) {
            periodDays = 365;
        }
        return periodDays;
    }

    public String buildImpactString(String userId, int scardId, int memberId, Date startDate, Date endDate) {
        StringBuilder impact = new StringBuilder();

        ScorecardDAO dao = new ScorecardDAO();
        List<String> idList = new ArrayList<String>();
        idList.add(String.valueOf(scardId));

        List<ScoreCard> scardList = dao.getScoreCards(idList);
        if (!scardList.isEmpty()) {
            ScoreCard scard = scardList.get(0);
            String folderId = scard.getFolderId();

            String dateFormat = "MM/dd/yyyy";
            SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
            ArrayList<String> timeDetails = new ArrayList<String>();
            timeDetails.add("Day");
            timeDetails.add("PRG_DATE_RANGE");
            timeDetails.add(sdf.format(startDate));
            timeDetails.add(sdf.format(endDate));
            timeDetails.add(null);
            timeDetails.add(null);

            Iterable<ScorecardQueryDetails> queryDetails = scard.getQueryDetails();
            ScorecardQueryBuilder queryBuilder = new ScorecardQueryBuilder();
            Iterable<ScorecardQueryResult> queryResult = queryBuilder.buildScorecardResultset(queryDetails, userId, folderId, timeDetails);

            // Build the score object
            buildScoreObject(scard, queryResult);

            Score score = scard.getScore();
            double scardScore = score.getCurrentScore();

            if (memberId > 0) {
                List<ScorecardComponent> components = scard.getMemberList();
                for (ScorecardComponent component : components) {
                    if (component instanceof ScoreCardMember) {
                        ScoreCardMember member = (ScoreCardMember) component;
                        if (memberId == Integer.parseInt(member.getMemberId())) {
                            ScorecardMemberScore memberScore = (ScorecardMemberScore) member.getScore();
                            impact.append(memberScore.getCurrentScore());
                            break;
                        }
                    }
                }
            } else {
                impact.append(score.getCurrentScore());
            }

        }
        return impact.toString();
    }

    public String buildOriginalRowsHtml(PbReturnObject pbro) {
        StringBuilder builder = new StringBuilder();
        ScorecardDAO dao = new ScorecardDAO();
        if (pbro.getRowCount() > 0) {
            builder.append("<table align='center' border='1' id='originalMeasTable' class='tablesorter' style='border-collapse: collapse;' >");
            builder.append("<thead><tr><th>Measure Name</th><th>Weightage</th></tr></thead>");
            builder.append("<tbody>");
            for (int i = 0; i < pbro.getRowCount(); i++) {
                builder.append("<tr id='origTR" + i + "'>");
                if (pbro.getFieldValueString(i, "CHILD_SCARD_ID") != null && !pbro.getFieldValueString(i, "CHILD_SCARD_ID").equalsIgnoreCase("")) {
                    builder.append("<td width='50%'>").append(dao.getDimensionMeasureName(pbro.getFieldValueString(i, "CHILD_SCARD_ID")));
                } else {
                    builder.append("<td width='50%'>").append(pbro.getFieldValueString(i, "ELEMENT_NAME"));
                }
                builder.append("<input type='hidden' value=" + pbro.getFieldValueString(i, "SCARD_MEM_ID") + " name='memberIds'>").append("</td>");
                builder.append("<td width='50%'>");
                builder.append("<input type='text' id='origWeigh" + i + "' name='origWeight' value=" + pbro.getFieldValueString(i, "SCARD_CONTRIBUTION") + " onchange='calculateTotalWeightage()'>");
                builder.append("</td></tr>");
            }
            builder.append("</tbody>");
            builder.append("</table>");
        }

        return builder.toString();
    }

    public String buildAdHocRowsHtml(PbReturnObject pbro) {
        StringBuilder builder = new StringBuilder();
        if (pbro.getRowCount() > 0) {
            for (int i = 0; i < pbro.getRowCount(); i++) {
                builder.append("<tr id='row" + i + "'>");
                builder.append("<td align='center'>");
                builder.append("<input id='adHocMeas" + i + "' type='text' value=" + pbro.getFieldValueString(i, "ELEMENT_NAME") + " name='adHocMeas'>").append("</td>");
                builder.append("<td align='center'>");
                builder.append("<input id='adHocScore" + i + "' type='text' value='' name='adHocScore'>").append("</td>");
                builder.append("<td align='center'>");
                builder.append("<input type='text' id='adHocWeigh" + i + "' name='adHocWeight' value=" + pbro.getFieldValueString(i, "SCARD_CONTRIBUTION") + " onchange='calculateTotalWeightage()'>");
                builder.append("</td></tr>");
            }
        }

        return builder.toString();
    }

    public String adHocRowsUIForScorecard(String scardId) {
        StringBuilder builder = new StringBuilder();
        ScorecardDAO dao = new ScorecardDAO();

        PbReturnObject pbro = dao.getRetObjForAdHoc(scardId);
        if (pbro != null && pbro.getRowCount() > 0) {
            builder.append("<tr>");
            builder.append("<table width='99%'>");
            builder.append("<thead><tr><th colspan=''>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Adhoc Measure Name</th><th align='center'>Weightage</th></tr></thead>");
            builder.append("<tbody>");
            for (int i = 0; i < pbro.getRowCount(); i++) {
                builder.append("<tr>");
                builder.append("<td width='20%' >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
                builder.append(pbro.getFieldValueString(i, "ELEMENT_NAME"));
                builder.append("</td>");
                builder.append("<td style='font-weight: bold;' align='center'>");
                builder.append(pbro.getFieldValueInt(i, "SCARD_CONTRIBUTION"));
                builder.append("</td>");
                builder.append("</tr>");
            }
            builder.append("</tbody>");
            builder.append("</table>");
            builder.append("</tr>");
        }

        return builder.toString();

    }
}
