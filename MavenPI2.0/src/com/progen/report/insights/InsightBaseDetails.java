/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.insights;

import com.progen.report.KPIElement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author progen
 */
public class InsightBaseDetails {

    private List<KPIElement> kpiElement;
    private String userId;
    private String[] bizRoles;
    private LinkedHashMap parameters;
    private Map<String, String> drillMap;
    private ArrayList timeDetailsArray;
    private ArrayList dimensionIds;
    private ArrayList dimensionNames;
    private String callBackUrl;
    private List<String> dispMeasures;
    private ArrayList<String> dispMeasureNames = new ArrayList<String>();
    private String indicatorMeasure;

    public List<KPIElement> getKpiElement() {
        return kpiElement;
    }

    public void setKpiElement(List<KPIElement> kpiElement) {
        this.kpiElement = kpiElement;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public LinkedHashMap getParameters() {
        return parameters;
    }

    public void setParameters(LinkedHashMap parameters) {
        this.parameters = parameters;
    }

    public Map<String, String> getDrillMap() {
        return drillMap;
    }

    public void setDrillMap(Map<String, String> drillMap) {
        this.drillMap = drillMap;
    }

    public ArrayList getTimeDetailsArray() {
        return timeDetailsArray;
    }

    public void setTimeDetailsArray(ArrayList timeDetailsArray) {
        this.timeDetailsArray = timeDetailsArray;
    }

    public ArrayList getDimensionIds() {
        return dimensionIds;
    }

    public void setDimensionIds(ArrayList dimensionIds) {
        this.dimensionIds = dimensionIds;
    }

    public ArrayList getDimensionNames() {
        return dimensionNames;
    }

    public void setDimensionNames(ArrayList dimensionNames) {
        this.dimensionNames = dimensionNames;
    }

    public String getCallBackUrl() {
        return callBackUrl;
    }

    public void setCallBackUrl(String callBackUrl) {
        this.callBackUrl = callBackUrl;
    }

    public String[] getBizRoles() {
        return bizRoles;
    }

    public void setBizRoles(String[] bizRoles) {
        this.bizRoles = bizRoles;
    }

    public ArrayList<String> getDispMeasureNames() {
        return dispMeasureNames;
    }

    public void setDispMeasureNames(ArrayList<String> dispMeasureNames) {
        this.dispMeasureNames = dispMeasureNames;
    }

    public String getIndicatorMeasure() {
        return indicatorMeasure;
    }

    public void setIndicatorMeasure(String indicatorMeasure) {
        this.indicatorMeasure = indicatorMeasure;
    }

    public List<String> getDispMeasures() {
        return dispMeasures;
    }

    public void setDispMeasures(List<String> dispMeasures) {
        this.dispMeasures = dispMeasures;
    }
}
