/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.scorecard;

import com.google.common.base.Predicate;
import com.progen.report.scorecard.query.ScorecardQueryDetails;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author progen
 */
public class ScoreCardMember implements ScorecardComponent, Serializable {

    private String memberId;
    private String type;
    private double contribution;
    private String basis;
    private int sequence;
    private String elementId;
    private String elementName;
    private String priorElementId;
    private String changeElementId;
    private String changePercElementId;
    private String dimElementId;
    private String dimValue;
    private List<ScoreCardMemberRule> ruleList;
    private Score score;
    private String period;
    private String unit;
    private Map<String, Double> historicScores;
    private String targetElementId;
    private String targetElementName;
    private String targetMeasureType;
    private double targetMeasureValue;

    public ScoreCardMember() {
        this.ruleList = new ArrayList<ScoreCardMemberRule>();
    }

    public double getContribution() {
        return contribution;
    }

    public void setContribution(double contribution) {
        this.contribution = contribution;
    }

    public String getElementId() {
        return elementId;
    }

    public void setElementId(String elementId) {
        this.elementId = elementId;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public List<ScoreCardMemberRule> getRuleList() {
        return ruleList;
    }

    public void setRuleList(List<ScoreCardMemberRule> ruleList) {
        this.ruleList = ruleList;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getElementName() {
        return elementName;
    }

    public void setElementName(String elementName) {
        this.elementName = elementName;
    }

    public String getPriorElementId() {
        return priorElementId;
    }

    public void setPriorElementId(String priorElementId) {
        this.priorElementId = priorElementId;
    }

    public String getBasis() {
        return basis;
    }

    public void setBasis(String basis) {
        this.basis = basis;
    }

    public String getChangeElementId() {
        return changeElementId;
    }

    public void setChangeElementId(String changeElementId) {
        this.changeElementId = changeElementId;
    }

    public String getChangePercElementId() {
        return changePercElementId;
    }

    public void setChangePercElementId(String changePercElementId) {
        this.changePercElementId = changePercElementId;
    }

    public void addRule(ScoreCardMemberRule rule) {
        this.ruleList.add(rule);
    }

    public double getScore(double value) {
        double score = 0;
        /*
         * Iterable<ScoreCardMemberRule> filter = Iterables.filter(ruleList,
         * getPredicate(value)); Iterator<ScoreCardMemberRule> iter =
         * filter.iterator(); if (iter.hasNext()){ ScoreCardMemberRule rule =
         * iter.next(); score = rule.getScore();
        }
         */
        score = getNormalizedScore(value);
        return score;
    }

    private double getNormalizedScore(double value) {
        double score = 0;

        for (int i = 0; i < ruleList.size(); i++) {
            ScoreCardMemberRule rule = ruleList.get(i);
            String operator = rule.getOperator();

            boolean success = false;
            //Check whether the value satisfy this rule
            if (ScoreCardConstants.OPERATOR_GREATER_THAN.equals(operator)) {
                if (value > rule.getStartValue()) {
                    success = true;
                }
            } else if (ScoreCardConstants.OPERATOR_GREATER_THAN_EQUAL_TO.equals(operator)) {
                if (value >= rule.getStartValue()) {
                    success = true;
                }
            } else if (ScoreCardConstants.OPERATOR_LESS_THAN.equals(operator)) {
                if (value < rule.getStartValue()) {
                    success = true;
                }
            } else if (ScoreCardConstants.OPERATOR_LESS_THAN_EQUAL_TO.equals(operator)) {
                if (value <= rule.getStartValue()) {
                    success = true;
                }
            } else if (ScoreCardConstants.OPERATOR_EQUAL_TO.equals(operator)) {
                if (value == rule.getStartValue()) {
                    success = true;
                }
            } else if (ScoreCardConstants.OPERATOR_NOT_EQUAL_TO.equals(operator)) {
                if (value != rule.getStartValue()) {
                    success = true;
                }
            } else if (ScoreCardConstants.OPERATOR_BETWEEN.equals(operator)) {
                if (value >= rule.getStartValue() && value <= rule.getEndValue()) {
                    success = true;
                }
            }

            if (success) {
                //Check whether the rule fired is in the beginning/end. In this case, return the score available in the rule.
                //If the score is in any of the range rules, apply the normalization logic
                if (i == 0 || i == (ruleList.size() - 1)) {
                    score = rule.getScore();
                } else {
                    double ruleStartScore = ruleList.get(i - 1).getScore();
                    double ruleEndScore = rule.getScore();

                    if (ruleStartScore > ruleEndScore) { // For measures like discount, which takes higher score values for less measure vals
                        ruleStartScore = ruleEndScore;
                        ruleEndScore = ruleList.get(i + 1).getScore();
                    }
                    double scoreRange = Math.abs(ruleEndScore - ruleStartScore);
                    double valueRange = Math.abs(rule.getEndValue() - rule.getStartValue());
                    double scorePerVal = scoreRange / valueRange;
                    double scoreDelta = ((value - rule.getStartValue()) * scorePerVal);
                    if (scoreDelta == Double.NEGATIVE_INFINITY) {
                        scoreDelta = 0.0;
                    }
                    if (ruleStartScore > ruleEndScore) {
                        score = ruleStartScore - scoreDelta;
                    } else {
                        score = ruleStartScore + scoreDelta;
                    }
                }
                break;
            }
        }

        return score;
    }

    private Predicate<ScoreCardMemberRule> getPredicate(final double value) {
        Predicate<ScoreCardMemberRule> predicate = new Predicate<ScoreCardMemberRule>() {

            public boolean apply(ScoreCardMemberRule rule) {
                String operator = rule.getOperator();
                if (ScoreCardConstants.OPERATOR_GREATER_THAN.equals(operator)) {
                    if (value > rule.getStartValue()) {
                        return true;
                    }
                } else if (ScoreCardConstants.OPERATOR_GREATER_THAN_EQUAL_TO.equals(operator)) {
                    if (value >= rule.getStartValue()) {
                        return true;
                    }
                } else if (ScoreCardConstants.OPERATOR_LESS_THAN.equals(operator)) {
                    if (value < rule.getStartValue()) {
                        return true;
                    }
                } else if (ScoreCardConstants.OPERATOR_LESS_THAN_EQUAL_TO.equals(operator)) {
                    if (value <= rule.getStartValue()) {
                        return true;
                    }
                } else if (ScoreCardConstants.OPERATOR_EQUAL_TO.equals(operator)) {
                    if (value == rule.getStartValue()) {
                        return true;
                    }
                } else if (ScoreCardConstants.OPERATOR_NOT_EQUAL_TO.equals(operator)) {
                    if (value != rule.getStartValue()) {
                        return true;
                    }
                } else if (ScoreCardConstants.OPERATOR_BETWEEN.equals(operator)) {
                    if (value >= rule.getStartValue() && value <= rule.getEndValue()) {
                        return true;
                    }
                }
                return false;
            }
        };
        return predicate;
    }

    public String getRulesToolTip() {
        StringBuilder toolTip = new StringBuilder();
        toolTip.append("<table><thead><tr><th style='font-weight:bold' align='left'>Condition</th><th width='8px'></th><th align='left' style='font-weight:bold'>Score</th></tr></thead><tbody>");
        for (ScoreCardMemberRule rule : ruleList) {
            toolTip.append("<tr>");
            String operator = rule.getOperator();
//            toolTip.append("If ").append(elementName).append(" ");
            toolTip.append("<td align='left'>");
            if (ScoreCardConstants.OPERATOR_BETWEEN.equals(operator)) {
                toolTip.append("between ").append(rule.getStartValue()).append(" and ").append(rule.getEndValue());
            } else {
                toolTip.append(operator).append(" ").append(rule.getStartValue());
            }
            toolTip.append("</td>");
            toolTip.append("<td width='8px'></td>");
            toolTip.append("<td align='left'>").append(rule.getScore()).append("</td>");
            toolTip.append("</tr>");
        }
        toolTip.append("</tbody></table>");
        return toolTip.toString();
    }

    public Iterable<ScorecardQueryDetails> getQueryDetails() {
        ScorecardQueryDetails qryDetails = new ScorecardQueryDetails();

        if (ScoreCardConstants.SCARD_MEMBER_TYPE_DIM_KPI.equalsIgnoreCase(type) && dimElementId != null) {
            qryDetails.setParamDimId(dimElementId);
            qryDetails.setParamValue(dimValue);
        }

        if (ScoreCardConstants.SCORECARD_BASIS_ABSOLUTE.equalsIgnoreCase(basis)) {
            qryDetails.setMeasElementId(elementId);
        } else if (ScoreCardConstants.SCORECARD_BASIS_CHANGE_AMT.equalsIgnoreCase(basis)) {
            qryDetails.setMeasElementId(changeElementId);
        } else if (ScoreCardConstants.SCORECARD_BASIS_CHANGE_PERC.equalsIgnoreCase(basis)) {
            qryDetails.setMeasElementId(changePercElementId);
        } else {
            qryDetails.setMeasElementId(elementId);
            qryDetails.setTargetElementId(targetElementId);
        }
        qryDetails.setScoreCardMemberId(this.memberId);

        ArrayList<ScorecardQueryDetails> qryDetlsLst = new ArrayList<ScorecardQueryDetails>();
        qryDetlsLst.add(qryDetails);

        return qryDetlsLst;
    }

    public Score getScore() {
        return this.score;
    }

    public void setScore(Score score) {
        this.score = score;
    }

    public String getDimElementId() {
        return dimElementId;
    }

    public void setDimElementId(String dimElementId) {
        this.dimElementId = dimElementId;
    }

    public String getDimValue() {
        return dimValue;
    }

    public void setDimValue(String dimValue) {
        this.dimValue = dimValue;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public static Predicate<ScorecardComponent> getScoreCardMemberKPIPredicate(final String elementId) {

        Predicate<ScorecardComponent> scoreCardMember = new Predicate<ScorecardComponent>() {

            public boolean apply(ScorecardComponent member) {
                if (member instanceof ScoreCardMember && (((ScoreCardMember) member).getElementId().equalsIgnoreCase(elementId))) {
                    return true;
                } else {
                    return false;
                }
            }
        };
        return scoreCardMember;
    }

    public static Predicate<ScorecardComponent> getScoreCardMemberScoreCardPredicate(final String scardId) {
        Predicate<ScorecardComponent> scoreCardMember = new Predicate<ScorecardComponent>() {

            public boolean apply(ScorecardComponent member) {
                if (member instanceof ScoreCard && (((ScoreCard) member).getScoreCardId().equalsIgnoreCase(scardId))) {
                    return true;
                } else {
                    return false;
                }
            }
        };
        return scoreCardMember;
    }

    public static Predicate<ScorecardComponent> getChildScoreCardMemberPredicate(final String dimValue) {
        Predicate<ScorecardComponent> scoreCardMember = new Predicate<ScorecardComponent>() {

            public boolean apply(ScorecardComponent member) {
                if (member instanceof ScoreCardMember && (((ScoreCardMember) member).getDimValue().equalsIgnoreCase(dimValue))) {
                    return true;
                } else {
                    return false;
                }
            }
        };
        return scoreCardMember;
    }

    public void setHistoricScores(Map<String, Double> histScores) {
        this.historicScores = histScores;
    }

    public Map<String, Double> getHistoricScores() {
        return this.historicScores;
    }

    public Double getHistoricScore(String period) {
        Double hscore = null;
        if (this.historicScores != null) {
            hscore = this.historicScores.get(period);
        }
        return hscore;
    }

    public String getTargetElementId() {
        return targetElementId;
    }

    public void setTargetElementId(String targetElementId) {
        this.targetElementId = targetElementId;
    }

    public String getTargetElementName() {
        return targetElementName;
    }

    public void setTargetElementName(String targetElementName) {
        this.targetElementName = targetElementName;
    }

    public String getTargetMeasureType() {
        return targetMeasureType;
    }

    public void setTargetMeasureType(String targetMeasureType) {
        this.targetMeasureType = targetMeasureType;
    }

    public double getTargetMeasureValue() {
        return targetMeasureValue;
    }

    public void setTargetMeasureValue(double targetMeasureValue) {
        this.targetMeasureValue = targetMeasureValue;
    }
}
