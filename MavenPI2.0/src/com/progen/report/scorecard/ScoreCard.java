/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.scorecard;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.progen.report.scorecard.query.ScorecardQueryDetails;
import java.util.*;
import prg.db.PbReturnObject;

/**
 *
 * @author progen
 */
public class ScoreCard implements ScorecardComponent {

    private String scoreCardId;
    private String scoreCardName;
    private String scoreCardArea;
    private String lightType;
    private int noOfDays;
    private List<ScoreCardColorRule> colorList;
    private List<ScorecardComponent> memberList;
    private double contribution;
    private Score score;
    private List<ScoreCardMemberRule> ruleList;
    private String folderId;
    private boolean isDimensionBased;
    private String userId;
    private Map<String, Double> historicScores;
    private double targetScore;

    public ScoreCard() {
        this.memberList = new ArrayList<ScorecardComponent>();
        this.colorList = new ArrayList<ScoreCardColorRule>();
        this.ruleList = new ArrayList<ScoreCardMemberRule>();
    }

    public List<ScorecardComponent> getMemberList() {
        return memberList;
    }

    public void setMemberList(List<ScorecardComponent> memberList) {
        this.memberList = memberList;
    }

    public double getContribution() {
        return contribution;
    }

    public void setContribution(double contribution) {
        this.contribution = contribution;
    }

    public int getNoOfDays() {
        return noOfDays;
    }

    public void setNoOfDays(int noOfDays) {
        this.noOfDays = noOfDays;
    }

    public String getScoreCardArea() {
        return scoreCardArea;
    }

    public void setScoreCardArea(String scoreCardArea) {
        this.scoreCardArea = scoreCardArea;
    }

    public String getScoreCardId() {
        return scoreCardId;
    }

    public void setScoreCardId(String scoreCardId) {
        this.scoreCardId = scoreCardId;
    }

    public String getScoreCardName() {
        return scoreCardName;
    }

    public void setScoreCardName(String scoreCardName) {
        this.scoreCardName = scoreCardName;
    }

    public void addMember(ScorecardComponent member) {
        this.memberList.add(member);
    }

    public List<ScoreCardColorRule> getColorList() {
        return colorList;
    }

    public void setColorList(List<ScoreCardColorRule> colorList) {
        this.colorList = colorList;
    }

    public String getLightType() {
        return lightType;
    }

    public void setLightType(String lightType) {
        this.lightType = lightType;
    }

    public String getLightColor(double score) {
        String color = "";
        Iterable<ScoreCardColorRule> filter = Iterables.filter(colorList, getPredicate(score));
        Iterator<ScoreCardColorRule> iter = filter.iterator();

        if (iter.hasNext()) {
            color = iter.next().getColor();
        }
        return color;
    }

    private Predicate<ScoreCardColorRule> getPredicate(final double value) {
        Predicate<ScoreCardColorRule> predicate = new Predicate<ScoreCardColorRule>() {

            public boolean apply(ScoreCardColorRule rule) {
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

    public Score getScore(PbReturnObject retObj) {
        ScorecardScore score = new ScorecardScore();

        return score;
    }

    public Iterable<ScorecardQueryDetails> getQueryDetails() {
        ScorecardQueryDetails qryDetails = new ScorecardQueryDetails();
        ArrayList<ScorecardQueryDetails> qryDtlsLst = new ArrayList<ScorecardQueryDetails>();
        for (ScorecardComponent component : memberList) {
            qryDtlsLst.addAll((Collection<? extends ScorecardQueryDetails>) component.getQueryDetails());
        }
        return qryDtlsLst;
    }

    public Score getScore() {
        return this.score;
    }

    public void setScore(Score score) {
        this.score = score;
    }

    public List<ScoreCardMemberRule> getRuleList() {
        return ruleList;
    }

    public void setRuleList(List<ScoreCardMemberRule> ruleList) {
        this.ruleList = ruleList;
    }

    public void addRule(ScoreCardMemberRule rule) {
        ruleList.add(rule);
    }

    public String getFolderId() {
        return folderId;
    }

    public void setFolderId(String folderId) {
        this.folderId = folderId;
    }

    public boolean isDimensionBased() {
        return this.isDimensionBased;
    }

    public void setDimensionBased(boolean isDimensionBased) {
        this.isDimensionBased = isDimensionBased;
    }

    public ScorecardComponent getScoreCardMemberKPIPredicate(final String elementId) {
        Iterator<ScorecardComponent> memberIterator = Iterables.filter(memberList, ScoreCardMember.getScoreCardMemberKPIPredicate(elementId)).iterator();
        if (memberIterator.hasNext()) {
            return memberIterator.next();
        } else {
            return null;
        }
    }

    public ScorecardComponent getScoreCardMemberScoreCardPredicate(final String scardId) {
        Iterator<ScorecardComponent> memberIterator = Iterables.filter(memberList, ScoreCardMember.getScoreCardMemberScoreCardPredicate(scardId)).iterator();
        if (memberIterator.hasNext()) {
            return memberIterator.next();
        } else {
            return null;
        }
    }

    public ScorecardComponent getChildScoreCardMemberPredicate(final String dimValue) {
        Iterator<ScorecardComponent> memberIterator = Iterables.filter(memberList, ScoreCardMember.getChildScoreCardMemberPredicate(dimValue)).iterator();
        if (memberIterator.hasNext()) {
            return memberIterator.next();
        } else {
            return null;
        }
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isDimensionBasedScorecard() {
        boolean isDimBased = false;
        for (ScorecardComponent comp : memberList) {
            if (comp instanceof ScoreCardMember) {
                ScoreCardMember member = (ScoreCardMember) comp;
                if (member.getType().equalsIgnoreCase(ScoreCardConstants.SCARD_MEMBER_TYPE_DIM_KPI)) {
                    isDimBased = true;
                    break;
                }
            }
        }
        return isDimBased;
    }

    public boolean isNestedScorecard() {
        boolean isNested = false;
        for (ScorecardComponent comp : memberList) {
            if (comp instanceof ScoreCard) {
                isNested = true;
                break;
            }
        }
        return isNested;
    }

    public ScorecardComponent getScorecardComponent(String compId) {
        ScorecardComponent comp = null;
        for (ScorecardComponent component : memberList) {
            if (component instanceof ScoreCardMember) {
                ScoreCardMember member = (ScoreCardMember) component;
                if (member.getMemberId().equalsIgnoreCase(compId)) {
                    comp = member;
                    break;
                }
            } else {
                ScoreCard scard = (ScoreCard) component;
                if (scard.getScoreCardId().equalsIgnoreCase(compId)) {
                    comp = scard;
                    break;
                }
            }
        }
        return comp;
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

    public void generateHistoricScores() {
        historicScores = new LinkedHashMap<String, Double>();
        Set<String> periods = new LinkedHashSet<String>();

        for (ScorecardComponent component : memberList) {
            Map<String, Double> histScores = component.getHistoricScores();
            if (histScores != null) {
                periods.addAll(histScores.keySet());
            }
        }

        // For each period generate the score
        Iterator<String> iter = periods.iterator();
        while (iter.hasNext()) {
            String period = iter.next();
            double periodScore = 0;
            for (ScorecardComponent component : memberList) {
                double weightage = component.getContribution();
                Map<String, Double> histScores = component.getHistoricScores();
                if (histScores != null) {
                    double compScore = histScores.get(period);
                    periodScore += (compScore * weightage / 100);
                }
            }
            historicScores.put(period, periodScore);
        }
    }

    public double getTargetScore() {
        return targetScore;
    }

    public void setTargetScore(double targetScore) {
        this.targetScore = targetScore;
    }
}
