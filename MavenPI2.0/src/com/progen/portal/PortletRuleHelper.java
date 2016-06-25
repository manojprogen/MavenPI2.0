/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.portal;

import java.io.Serializable;
import java.util.Map;

/**
 *
 * @author progen
 */
public class PortletRuleHelper implements Serializable {

    private String actualRule;
    private Map<String, String> reportParms;
    private String ruleOn;

    /**
     * @return the actualRule
     */
    public String getActualRule() {
        return actualRule;
    }

    /**
     * @param actualRule the actualRule to set
     */
    public void setActualRule(String actualRule) {
        this.actualRule = actualRule;
    }

    /**
     * @return the reportParms
     */
    public Map<String, String> getReportParms() {
        return reportParms;
    }

    /**
     * @param reportParms the reportParms to set
     */
    public void setReportParms(Map<String, String> reportParms) {
        this.reportParms = reportParms;
    }

    /**
     * @return the ruleOn
     */
    public String getRuleOn() {
        return ruleOn;
    }

    /**
     * @param ruleOn the ruleOn to set
     */
    public void setRuleOn(String ruleOn) {
        this.ruleOn = ruleOn;
    }
}
