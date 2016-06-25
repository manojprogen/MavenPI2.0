/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.query;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Map;
import prg.db.PbReturnObject;

/**
 *
 * @author arun
 */
public class TrendDataSet {

    PbReturnObject trendData;
    Iterable<String> measIds;
    ArrayList<String> measLabels;
    Map<String, Integer> noOfDays;

    public TrendDataSet(PbReturnObject retObj) {
        if (retObj == null || retObj.getRowCount() == 0) {
            throw new RuntimeException("No data in the result set");
        }
        this.trendData = retObj;
    }

    public void setMeasures(Iterable<String> measIds) {
        this.measIds = measIds;
        //this.measLabels = measLabels;
    }

    public int getTimeRangeCount() {
        return trendData.getRowCount();
    }

    public BigDecimal getLastPeriodValue(String measure) {
        int row = trendData.getRowCount() - 1;
        return trendData.getFieldValueBigDecimal(row, measure);
    }

    public BigDecimal findLastNPeriodAverage(String measure, int periodCount) {
        if (periodCount > trendData.getRowCount()) {
            periodCount = trendData.getRowCount();
        }
        if (periodCount <= 0) {
            periodCount = trendData.getRowCount();
        }

        BigDecimal avg = BigDecimal.ZERO;
        BigDecimal sum = BigDecimal.ZERO;
        for (int i = trendData.getRowCount() - 1, count = 0; count < periodCount; count++, i--) {
            sum = sum.add(trendData.getFieldValueBigDecimal(i, measure));
        }
        avg = sum.divide(new BigDecimal(periodCount), MathContext.DECIMAL64);
        return avg;
    }

    public BigDecimal findMaxInPeriod(String measure) {
        return findMaxMinInPeriod(measure, "MAX");
    }

    public BigDecimal findMinInPeriod(String measure) {
        return findMaxMinInPeriod(measure, "MIN");
    }

    private BigDecimal findMaxMinInPeriod(String measure, String maxMinType) {

        BigDecimal maxMin = BigDecimal.ZERO;
        for (int i = 0; i < trendData.getRowCount(); i++) {
            if (i == 0) {
                maxMin = trendData.getFieldValueBigDecimal(i, measure);
            } else {
                if (maxMinType.equals("MAX")) {
                    maxMin = maxMin.max(trendData.getFieldValueBigDecimal(i, measure));
                } else {
                    maxMin = maxMin.min(trendData.getFieldValueBigDecimal(i, measure));
                }
            }
        }
        return maxMin;
    }

    public PbReturnObject getReturnObject() {
        return this.trendData;
    }

    public Map<String, Integer> getNoOfDays() {
        return noOfDays;
    }

    public void setNoOfDays(Map<String, Integer> noOfDays) {
        this.noOfDays = noOfDays;
    }
}
