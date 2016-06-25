/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.scorecard;

import java.math.BigDecimal;

/**
 *
 * @author progen
 */
public class ScoreCardMeasureMetrics {

    private String label;
    private String measValue;
    private BigDecimal measValAsBigDecimal;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getMeasValue() {
        return measValue;
    }

    public void setMeasValue(String measValue) {
        this.measValue = measValue;
    }

    public BigDecimal getMeasValAsBigDecimal() {
        return measValAsBigDecimal;
    }

    public void setMeasValAsBigDecimal(BigDecimal measValAsBigDecimal) {
        this.measValAsBigDecimal = measValAsBigDecimal;
    }
}
