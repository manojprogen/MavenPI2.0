/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.entities;

/**
 *
 * @author progen
 */
public class KPITarget {

    private String elementId;
    private String timeLevel;
    private double targetValue;

    public String getElementId() {
        return elementId;
    }

    public void setElementId(String elementId) {
        this.elementId = elementId;
    }

    public double getTargetValue() {
        return targetValue;
    }

    public void setTargetValue(double targetValue) {
        this.targetValue = targetValue;
    }

    public String getTimeLevel() {
        return timeLevel;
    }

    public void setTimeLevel(String timeLevel) {
        this.timeLevel = timeLevel;
    }
}
