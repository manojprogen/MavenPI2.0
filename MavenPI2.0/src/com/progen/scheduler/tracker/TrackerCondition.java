/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.scheduler.tracker;

/**
 *
 * @author progen
 */
public class TrackerCondition {

    private String viewByValue;
    private String operator;
    private double measureStartValue;
    private double measureEndValue;
    private String mailIds;
    private String tagType;
    private boolean isSendAnywayCheck;
    private double targetValue;
    private double deviationPerVal;
    private boolean fromFlagTarget;

    public String getOperator() {
        return operator;
    }

    public void setOperator(String condition) {
        this.operator = condition;
    }

    public String getViewByValue() {
        return viewByValue;
    }

    public void setViewByValue(String viewByValue) {
        this.viewByValue = viewByValue;
    }

    public double getMeasureStartValue() {
        return measureStartValue;
    }

    public void setMeasureStartValue(double measureStartValue) {
        this.measureStartValue = measureStartValue;
    }

    public double getMeasureEndValue() {
        return measureEndValue;
    }

    public void setMeasureEndValue(double measureEndValue) {
        this.measureEndValue = measureEndValue;
    }

    public String getMailIds() {
        return mailIds;
    }

    public void setMailIds(String mailIds) {
        this.mailIds = mailIds;
    }

    public boolean isSendAnywayCheck() {
        return isSendAnywayCheck;
    }

    public void setSendAnywayCheck(boolean isSendAnywayCheck) {
        this.isSendAnywayCheck = isSendAnywayCheck;
    }

    public double getTargetValue() {                  //for targetBasis
        return targetValue;
    }

    public void setTargetValue(double targetValue) {
        this.targetValue = targetValue;
    }

    public double getDeviationPerVal() {
        return deviationPerVal;
    }

    public void setDeviationPerVal(double deviationPerVal) {
        this.deviationPerVal = deviationPerVal;
    }

    public boolean isFromFlagTarget() {
        return fromFlagTarget;
    }

    public void setFromFlagTarget(boolean fromFlagTarget) {               //for targetBasis
        this.fromFlagTarget = fromFlagTarget;
    }

    public String getTagType() {
        return tagType;
    }

    public void setTagType(String tagType) {
        this.tagType = tagType;
    }
}