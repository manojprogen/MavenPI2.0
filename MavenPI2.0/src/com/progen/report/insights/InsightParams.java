/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.insights;

/**
 *
 * @author progen
 */
public class InsightParams {

    private String dimId;
    private String masterDimId;
    private String dimValue;
    private boolean isOddLevel;
    private String childDimId;
    private String parameter;

    public String getDimId() {
        return dimId;
    }

    public void setDimId(String dimId) {
        this.dimId = dimId;
    }

    public String getMasterDimId() {
        return masterDimId;
    }

    public void setMasterDimId(String masterDimId) {
        this.masterDimId = masterDimId;
    }

    public String getDimValue() {
        return dimValue;
    }

    public void setDimValue(String dimValue) {
        this.dimValue = dimValue;
    }

    public boolean isIsOddLevel() {
        return isOddLevel;
    }

    public void setIsOddLevel(boolean isOddLevel) {
        this.isOddLevel = isOddLevel;
    }

    public String getChildDimId() {
        return childDimId;
    }

    public void setChildDimId(String childDimId) {
        this.childDimId = childDimId;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }
}
