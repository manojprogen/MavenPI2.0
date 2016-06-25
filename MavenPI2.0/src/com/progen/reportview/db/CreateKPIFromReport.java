/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.reportview.db;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author progen
 */
public class CreateKPIFromReport implements Serializable {

    private String measureName;
    private String measureId;
    private String reportKPIName;
    private Object[] rowViewBys;
    private Object[] colViewBys;
    private String aggType;
    private Map<String, String> reportParametersValues;
    private Object[] timeDetailsArray;
    private Object[] dimensionIds;
    private Object[] dimensionNames;
    private Object[] dispMeasures;
    private Object[] dispMeasureNames;
    private String userId;
    private String[] bizRoles;
    private String reportId;
    private BigDecimal aggTypeValue;
    private int count;
    private boolean topBtmFlag;
    private String topBtmType;
    private String topBtmMode;
    private int topBtmCount;
    private Object[] qryAggregations;
    private Object[] reportQryCols;
    private Object[] sortColumns;
    private char[] sortTypes;
    private Object[] srchConditions;
    private Object[] srchColumn;
    private Object[] srchValue;
    private char[] colDataTypes;
    private String dynamicTimeDetails;
    private String otherTimeDetails;
    private String pagesPerSlide;
    private String totalValue;
    private static final long serialVersionUID = -3112492408403028723L;

    public String getMeasureName() {
        return measureName;
    }

    public void setMeasureName(String measureName) {
        this.measureName = measureName;
    }

    public String getAggType() {
        return aggType;
    }

    public void setAggType(String aggType) {
        this.aggType = aggType;
    }

    public String getMeasureId() {
        return measureId;
    }

    public void setMeasureId(String measureId) {
        this.measureId = measureId;
    }

    public HashMap<String, List<String>> getReportParametersValues() {
        String[] keys = (String[]) reportParametersValues.keySet().toArray(new String[0]);
        HashMap<String, List<String>> reportParamValNew = new HashMap<String, List<String>>();
        for (String str : keys) {
            StringBuilder temp = new StringBuilder();
            String[] list = reportParametersValues.get(str).split(",");
            reportParamValNew.put(str, Arrays.asList(list));
        }
        return reportParamValNew;
    }

    public void setReportParametersValues(HashMap reportParametersValues) {
        String[] keys = (String[]) reportParametersValues.keySet().toArray(new String[0]);
        for (String str : keys) {
            StringBuilder temp = new StringBuilder();
            List<String> list = (List<String>) reportParametersValues.get(str);
            for (String val : list) {
                temp.append(",").append(val).append("");
            }
            if (temp.length() > 0) {
                reportParametersValues.put(str, temp.substring(1));
            }
        }
        this.reportParametersValues = reportParametersValues;
    }

    public Object[] getTimeDetailsArray() {
        return timeDetailsArray;
    }

    public void setTimeDetailsArray(Object[] timeDetailsArray) {
        this.timeDetailsArray = timeDetailsArray;
    }

    public String getReportKPIName() {
        return reportKPIName;
    }

    public void setReportKPIName(String reportKPIName) {
        this.reportKPIName = reportKPIName;
    }

    public Object[] getRowViewBys() {
        return rowViewBys;
    }

    public void setRowViewBys(Object[] rowViewBys) {
        this.rowViewBys = rowViewBys;
    }

    public Object[] getDimensionIds() {
        return dimensionIds;
    }

    public void setDimensionIds(Object[] dimensionIds) {
        this.dimensionIds = dimensionIds;
    }

    public Object[] getDimensionNames() {
        return dimensionNames;
    }

    public void setDimensionNames(Object[] dimensionNames) {
        this.dimensionNames = dimensionNames;
    }

    public Object[] getDispMeasures() {
        return dispMeasures;
    }

    public void setDispMeasures(Object[] dispMeasures) {
        this.dispMeasures = dispMeasures;
    }

    public Object[] getDispMeasureNames() {
        return dispMeasureNames;
    }

    public void setDispMeasureNames(Object[] dispMeasureNames) {
        this.dispMeasureNames = dispMeasureNames;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String[] getBizRoles() {
        return bizRoles;
    }

    public void setBizRoles(String[] bizRoles) {
        this.bizRoles = bizRoles;
    }

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public BigDecimal getAggTypeValue() {
        return aggTypeValue;
    }

    public void setAggTypeValue(BigDecimal aggTypeValue) {
        this.aggTypeValue = aggTypeValue;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isTopBottomSet() {
        return topBtmFlag;
    }

    public void setTopBottom(boolean topBtmFlag) {
        this.topBtmFlag = topBtmFlag;
    }

    public String getTopBtmType() {
        return topBtmType;
    }

    public void setTopBtmType(String topBtmType) {
        this.topBtmType = topBtmType;
    }

    public String getTopBtmMode() {
        return topBtmMode;
    }

    public void setTopBtmMode(String topBtmMode) {
        this.topBtmMode = topBtmMode;
    }

    public int getTopBtmCount() {
        return topBtmCount;
    }

    public void setTopBtmCount(int topBtmCount) {
        this.topBtmCount = topBtmCount;
    }

    public Object[] getColViewBys() {
        return colViewBys;
    }

    public void setColViewBys(Object[] colViewBys) {
        this.colViewBys = colViewBys;
    }

    public Object[] getQryAggregations() {
        return qryAggregations;
    }

    public void setQryAggregations(Object[] qryAggregations) {
        this.qryAggregations = qryAggregations;
    }

    public Object[] getReportQryCols() {
        return reportQryCols;
    }

    public void setReportQryCols(Object[] reportQryColNames) {
        this.reportQryCols = reportQryColNames;
    }

    public Object[] getSortColumns() {
        return sortColumns;
    }

    public void setSortColumns(Object[] sortColumns) {
        this.sortColumns = sortColumns;
    }

    public char[] getSortTypes() {
        return sortTypes;
    }

    public void setSortTypes(char[] sortTypes) {
        this.sortTypes = sortTypes;
    }

    public Object[] getSrchConditions() {
        return srchConditions;
    }

    public void setSrchConditions(Object[] srchConditions) {
        this.srchConditions = srchConditions;
    }

    public Object[] getSrchColumn() {
        return srchColumn;
    }

    public void setSrchColumn(Object[] srchColumn) {
        this.srchColumn = srchColumn;
    }

    public Object[] getSrchValue() {
        return srchValue;
    }

    public void setSrchValue(Object[] srchValue) {
        this.srchValue = srchValue;
    }

    public char[] getColDataTypes() {
        return colDataTypes;
    }

    public void setColDataTypes(char[] colDataTypes) {
        this.colDataTypes = colDataTypes;
    }

    public String getDynamicTimeDetails() {
        return dynamicTimeDetails;
    }

    public void setDynamicTimeDetails(String dynamicTimeDetails) {
        this.dynamicTimeDetails = dynamicTimeDetails;
    }

    public String getOtherTimeDetails() {
        return otherTimeDetails;
    }

    public void setOtherTimeDetails(String otherTimeDetails) {
        this.otherTimeDetails = otherTimeDetails;
    }

    public String getPagesPerSlide() {
        return pagesPerSlide;
    }

    public void setPagesPerSlide(String pagesPerSlide) {
        this.pagesPerSlide = pagesPerSlide;
    }

    public String getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(String totalValue) {
        this.totalValue = totalValue;
    }
}
