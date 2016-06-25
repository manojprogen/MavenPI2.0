/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.colorgroup;

/**
 *
 * @author Administrator
 */
public class ColorCodeTransferObject {

    private String measEleId;
    private String rowViewByValues;
    private String colViewByValues;
    private StringBuilder parameter;
    private StringBuilder rule;
    private String crossTabMes = "";
    private boolean gradientBased = false;
    private boolean avgBased = false;
    private boolean percentBased = false;
    private boolean minMaxBased = false;

    public ColorCodeTransferObject(String measEleId, String rowViewByValues, String colViewByValues, StringBuilder paramXML, StringBuilder ruleXML, String crossTabMes) {
        this.measEleId = measEleId;
        this.rowViewByValues = rowViewByValues;
        this.colViewByValues = colViewByValues;
        this.parameter = paramXML;
        this.rule = ruleXML;
        this.crossTabMes = crossTabMes;
        this.avgBased = false;
        this.percentBased = false;
        this.minMaxBased = false;

    }

    public ColorCodeTransferObject(String measEleId, String rowViewByValues, String colViewByValues, StringBuilder paramXML, StringBuilder ruleXML, String crossTabMes, boolean IsAvgbased, boolean isPercentBased) {
        this.measEleId = measEleId;
        this.rowViewByValues = rowViewByValues;
        this.colViewByValues = colViewByValues;
        this.parameter = paramXML;
        this.rule = ruleXML;
        this.crossTabMes = crossTabMes;
        this.avgBased = IsAvgbased;
        this.percentBased = isPercentBased;
    }

    public ColorCodeTransferObject(String measEleId, String rowViewByValues, String colViewByValues, StringBuilder paramXML, StringBuilder ruleXML, String crossTabMes, boolean IsAvgbased, boolean isMinMaxBased, boolean isPercentBased) {
        this.measEleId = measEleId;
        this.rowViewByValues = rowViewByValues;
        this.colViewByValues = colViewByValues;
        this.parameter = paramXML;
        this.rule = ruleXML;
        this.crossTabMes = crossTabMes;
        this.avgBased = IsAvgbased;
        this.percentBased = isPercentBased;
        this.minMaxBased = isMinMaxBased;
    }

    public String getMeasEleId() {
        return measEleId;
    }

    public String getRowViewByValues() {
        return rowViewByValues;
    }

    public String getColViewByValues() {
        return colViewByValues;
    }

    public StringBuilder getParameter() {
        return parameter;
    }

    public StringBuilder getRule() {
        return rule;
    }

    public String getCrossTabMes() {
        return crossTabMes;
    }

    /**
     * @return the gradientBased
     */
    public boolean isGradientBased() {
        return gradientBased;
    }

    /**
     * @param gradientBased the gradientBased to set
     */
    public void setGradientBased(boolean gradientBased) {
        this.gradientBased = gradientBased;
    }

    /**
     * @return the avgBased
     */
    public boolean isAvgBased() {
        return avgBased;
    }

    /**
     * @param avgBased the avgBased to set
     */
    public void setAvgBased(boolean avgBased) {
        this.avgBased = avgBased;
    }

    /**
     * @return the avgBased
     */
    public boolean isPercentBased() {
        return percentBased;
    }

    /**
     * @param avgBased the avgBased to set
     */
    public void setPercentBased(boolean percentBased) {
        this.percentBased = percentBased;
    }

    /**
     * @return the minMaxBased
     */
    public boolean isMinMaxBased() {
        return minMaxBased;
    }

    /**
     * @param minMaxBased the minMaxBased to set
     */
    public void setMinMaxBased(boolean minMaxBased) {
        this.minMaxBased = minMaxBased;
    }
}
