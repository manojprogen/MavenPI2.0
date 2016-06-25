/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.scorecard;

/**
 *
 * @author progen
 */
public class ScorecardMemberScore implements Score {

    private String elementId;
    private String elementName;
    private Double currentScore;
    private Double currentValue;
    private Double priorScore;
    private Double priorValue;
    private Double contribution;
    private String lightIcon;
    private String ruleHTML;
    private Double targetScore;
    private Double targetDev;
    private Double targetDevPercent;

    public Double getCurrentScore() {
        return currentScore;
    }

    public void setCurrentScore(Double currentScore) {
        this.currentScore = currentScore;
    }

    public Double getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(Double currentValue) {
        this.currentValue = currentValue;
    }

    public String getElementId() {
        return elementId;
    }

    public void setElementId(String elementId) {
        this.elementId = elementId;
    }

    public String getElementName() {
        return elementName;
    }

    public void setElementName(String elementName) {
        this.elementName = elementName;
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

    public Double getPriorValue() {
        return priorValue;
    }

    public void setPriorValue(Double priorValue) {
        this.priorValue = priorValue;
    }

    public String getRuleHTML() {
        return ruleHTML;
    }

    public void setRuleHTML(String ruleHTML) {
        this.ruleHTML = ruleHTML;
    }

    public Double getContribution() {
        return this.contribution;
    }

    public void setContribution(Double contribution) {
        this.contribution = contribution;
    }

    public Double getTargetScore() {
        return targetScore;
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
