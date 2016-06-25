/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.rulesHelp;

import java.util.Map;

public class RuleProperty {

    private String actualRule;
    private String displayRule;
    private String[] measureIDs;
    private String[] dimensionIDs;
    private String ruleOn;
    private Map<String, String> reportParms;

    public String getActualRule() {
        return actualRule;
    }

    public void setActualRule(String actualRule) {
        this.actualRule = actualRule;
    }

    public String getDisplayRule() {
        return displayRule;
    }

    public void setDisplayRule(String displayRule) {
        this.displayRule = displayRule;
    }

    public String[] getMeasureIDs() {
        return measureIDs;
    }

    public void setMeasureIDs(String[] measureIDs) {
        this.measureIDs = measureIDs;
    }

    public String[] getDimensionIDs() {
        return dimensionIDs;
    }

    public void setDimensionIDs(String[] dimensionIDs) {
        this.dimensionIDs = dimensionIDs;
    }

    public String getRuleOn() {
        return ruleOn;
    }

    public void setRuleOn(String ruleOn) {
        this.ruleOn = ruleOn;
    }

    public Map<String, String> getReportParms() {
        return reportParms;
    }

    public void setReportParms(Map<String, String> reportParms) {
        this.reportParms = reportParms;
    }
}
