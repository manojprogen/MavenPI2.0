/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.entities;

import java.math.BigDecimal;
import java.util.Map;

/**
 *
 * @author progen
 */
public class TargetData {

    private String measureId;
    private String timeLevel;
    private Map<String, BigDecimal> targetValues;

    public String getMeasureId() {
        return measureId;
    }

    public void setMeasureId(String measureId) {
        this.measureId = measureId;
    }

    public Map<String, BigDecimal> getTargetValues() {
        return targetValues;
    }

    public void setTargetValues(Map<String, BigDecimal> targetValues) {
        this.targetValues = targetValues;
    }

    public String getTimeLevel() {
        return timeLevel;
    }

    public void setTimeLevel(String timeLevel) {
        this.timeLevel = timeLevel;
    }
}
