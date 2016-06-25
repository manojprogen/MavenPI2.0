/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.entities;

/**
 *
 * @author progen
 */
public class KPIColorRange {

    private double rangeStartValue;
    private double rangeEndValue;
    private String operator;
    private String color;

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public double getRangeEndValue() {
        return rangeEndValue;
    }

    public void setRangeEndValue(double rangeEndValue) {
        this.rangeEndValue = rangeEndValue;
    }

    public double getRangeStartValue() {
        return rangeStartValue;
    }

    public void setRangeStartValue(double rangeStartValue) {
        this.rangeStartValue = rangeStartValue;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + (this.color != null ? this.color.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof KPIColorRange) {
            KPIColorRange range = (KPIColorRange) obj;
            if (range.color != null && this.color != null) {
                if (range.color.equalsIgnoreCase(this.color)) {
                    return true;
                }
            }
        }
        return false;
    }
}
