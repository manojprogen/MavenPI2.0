/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.scorecard;

/**
 *
 * @author progen
 */
public class ScorecardScore implements Score {

    private String scoreCardName;
    private Double currentScore;
    private Double priorScore;
    private String lightIcon;
    private Double contribution;
    private String ruleHtml;
    private Double targetScore;
    private Double targetDev;
    private Double targetDevPercent;

    public Double getCurrentScore() {
        return currentScore;
    }

    public void setCurrentScore(Double currentScore) {
        this.currentScore = currentScore;
    }

    public String getLightIcon() {
        return lightIcon;
    }

    public void setLightIcon(String lightType) {
        this.lightIcon = lightType;
    }

    public Double getPriorScore() {
        return priorScore;
    }

    public void setPriorScore(Double priorScore) {
        this.priorScore = priorScore;
    }

    public String getScoreCardName() {
        return scoreCardName;
    }

    public void setScoreCardName(String scoreCardName) {
        this.scoreCardName = scoreCardName;
    }

    public Double getContribution() {
        return this.contribution;
    }

    public void setContribution(Double contribution) {
        this.contribution = contribution;
    }

    public String getRuleHtml() {
        return ruleHtml;
    }

    public void setRuleHtml(String ruleHtml) {
        this.ruleHtml = ruleHtml;
    }

    public Double getTargetScore() {
        return this.targetScore;
    }

    public void setTargetScore(Double targetScore) {
        this.targetScore = targetScore;
    }

    public Double getTargetDev() {
        return targetDev;
    }

    public void setTargetDev(Double targetDev) {
        this.targetDev = targetDev;
    }

    public Double getTargetDevPercent() {
        return targetDevPercent;
    }

    public void setTargetDevPercent(Double targetDevPercent) {
        this.targetDevPercent = targetDevPercent;
    }
}
