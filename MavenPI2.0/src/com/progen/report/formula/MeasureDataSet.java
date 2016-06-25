/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.formula;

import com.google.common.base.Predicate;
import java.math.BigDecimal;
import java.util.ArrayList;

/**
 *
 * @author progen
 */
public class MeasureDataSet {

    private String measEleId;
    private ArrayList<BigDecimal> measData;

    public MeasureDataSet(String measEleId, ArrayList<BigDecimal> measData) {
        this.measEleId = measEleId;
        this.measData = measData;
    }

    public BigDecimal getData(int row) {
        return this.measData.get(row);
    }

    public String getMeasure() {
        return this.measEleId;
    }

    //implement equals compare measEleId
    public static Predicate<MeasureDataSet> getMeasureDataSetPredicate(final String measEleId) {
        Predicate<MeasureDataSet> measDataSetPredicate = new Predicate<MeasureDataSet>() {

            public boolean apply(MeasureDataSet input) {
                if (measEleId.equals(input.getMeasure())) {
                    return true;
                } else {
                    return false;
                }
            }
        };
        return measDataSetPredicate;
    }
}
