/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.template;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 *
 * @author shobhit
 */
public class TemplateMeta implements Serializable {
    private String reportId;
    private List<String> measureNames;
    private List<String> measureIds;
    private List<String> defaultMeasures;
    private List<String> defaultMeasureIds;
    private List<String> defaultAggregation;
    private List<String> aggregation;
    private Map<String,String> alias;
    private Map<String,String> alias2;
    private Map<String,String> fontSize;
    private Map<String,String> fontColor;
    private Map<String,String> rounding;
    private Map<String,String> format;
     private Map<String, String> graphDrillMap;
      private Map<String,String> comparison;
    private Map<String,String> prefix;
    private Map<String,String> suffix;
    private Map<String,String> changePercent;
    private Map<String,List<String>> timeDetails;

    /**
     * @return the reportId
     */
    public String getReportId() {
        return reportId;
    }

    /**
     * @param reportId the reportId to set
     */
    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    /**
     * @return the measureNames
     */
    public List<String> getMeasureNames() {
        return measureNames;
    }

    /**
     * @param measureNames the measureNames to set
     */
    public void setMeasureNames(List<String> measureNames) {
        this.measureNames = measureNames;
    }

    /**
     * @return the measureIds
     */
    public List<String> getMeasureIds() {
        return measureIds;
    }

    /**
     * @param measureIds the measureIds to set
     */
    public void setMeasureIds(List<String> measureIds) {
        this.measureIds = measureIds;
    }

    /**
     * @return the aggregation
     */
    public List<String> getAggregation() {
        return aggregation;
    }

    /**
     * @param aggregation the aggregation to set
     */
    public void setAggregation(List<String> aggregation) {
        this.aggregation = aggregation;
    }

    /**
     * @return the alias
     */
    public Map<String,String> getAlias() {
        return alias;
    }

    /**
     * @param alias the alias to set
     */
    public void setAlias(Map<String,String> alias) {
        this.alias = alias;
    }

    /**
     * @return the alias2
     */
    public Map<String,String> getAlias2() {
        return alias2;
    }

    /**
     * @param alias2 the alias2 to set
     */
    public void setAlias2(Map<String,String> alias2) {
        this.alias2 = alias2;
    }
    
    /**
     * @return the fontSize
     */
    public Map<String,String> getfontSize() {
        return fontSize;
    }

    /**
     * @param fontSize the fontSize to set
     */
    public void setfontSize(Map<String,String> fontSize) {
        this.fontSize = fontSize;
    }
    
    /**
     * @return the fontColor
     */
    public Map<String,String> getfontColor() {
        return fontColor;
    }

    /**
     * @param fontColor the fontColor to set
     */
    public void setfontColor(Map<String,String> fontColor) {
        this.fontColor = fontColor;
    }
    /**
     * @return the rounding
     */
    public Map<String,String> getRounding() {
        return rounding;
    }

    /**
     * @param rounding the rounding to set
     */
    public void setRounding(Map<String,String> rounding) {
        this.rounding = rounding;
    }

    /**
     * @return the format
     */
    public Map<String,String> getFormat() {
        return format;
    }

    /**
     * @param format the format to set
     */
    public void setFormat(Map<String,String> format) {
        this.format = format;
    }

    /**
     * @return the graphDrillMap
     */
    public Map<String, String> getGraphDrillMap() {
        return graphDrillMap;
    }

    /**
     * @param graphDrillMap the graphDrillMap to set
     */
    public void setGraphDrillMap(Map<String, String> graphDrillMap) {
        this.graphDrillMap = graphDrillMap;
    }
     /**
     * @return the defaultMeasures
     */
    public List<String> getDefaultMeasures() {
        return defaultMeasures;
    }

    /**
     * @param defaultMeasures the defaultMeasures to set
     */
    public void setDefaultMeasures(List<String> defaultMeasures) {
        this.defaultMeasures = defaultMeasures;
    }

    /**
     * @return the defaultMeasureIds
     */
    public List<String> getDefaultMeasureIds() {
        return defaultMeasureIds;
    }

    /**
     * @param defaultMeasureIds the defaultMeasureIds to set
     */
    public void setDefaultMeasureIds(List<String> defaultMeasureIds) {
        this.defaultMeasureIds = defaultMeasureIds;
    }

    /**
     * @return the defaultAggregation
     */
    public List<String> getDefaultAggregation() {
        return defaultAggregation;
    }

    /**
     * @param defaultAggregation the defaultAggregation to set
     */
    public void setDefaultAggregation(List<String> defaultAggregation) {
        this.defaultAggregation = defaultAggregation;
    }

    /**
     * @return the comparison
     */
    public Map<String,String> getComparison() {
        return comparison;
    }

    /**
     * @param comparison the comparison to set
     */
    public void setComparison(Map<String,String> comparison) {
        this.comparison = comparison;
    }

    /**
     * @return the prefix
     */
    public Map<String,String> getPrefix() {
        return prefix;
    }

    /**
     * @param prefix the prefix to set
     */
    public void setPrefix(Map<String,String> prefix) {
        this.prefix = prefix;
    }

    /**
     * @return the suffix
     */
    public Map<String,String> getSuffix() {
        return suffix;
    }

    /**
     * @param suffix the suffix to set
     */
    public void setSuffix(Map<String,String> suffix) {
        this.suffix = suffix;
    }

    /**
     * @return the timeDetails
     */
    public Map<String,List<String>> getTimeDetails() {
        return timeDetails;
    }

    /**
     * @param timeDetails the timeDetails to set
     */
    public void setTimeDetails(Map<String,List<String>> timeDetails) {
        this.timeDetails = timeDetails;
    }

    /**
     * @return the changePercent
     */
    public Map<String,String> getChangePercent() {
        return changePercent;
}

    /**
     * @param changePercent the changePercent to set
     */
    public void setChangePercent(Map<String,String> changePercent) {
        this.changePercent = changePercent;
    }
}
