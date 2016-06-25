/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.query;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;

/**
 *
 * @author progen
 */
public class RunTimeMeasure implements RunTimeColumn, Serializable {

    private static final long serialVersionUID = 227666805398651909L;
    private ArrayList<BigDecimal> measData = new ArrayList<BigDecimal>();

    public RunTimeMeasure(ArrayList<BigDecimal> measData) {
        this.measData = measData;
    }

    public RunTimeMeasure(BigDecimal measData) {
        this.measData.add(measData);
    }

    public void addMeasureData(BigDecimal measData) {
        this.measData.add(measData);
    }

    public BigDecimal getGrandTotal() {
        BigDecimal grandTotal = new BigDecimal(0);
        for (BigDecimal data : measData) {
            grandTotal = grandTotal.add(data);
        }
//        grandTotal = new BigDecimal("100");
        return grandTotal;
    }

    public BigDecimal getMaximum() {
        int i = 0;
        //BigDecimal maximum = new BigDecimal(0);
        //BigDecimal maximum = measData.get(0);
        BigDecimal maximum = new BigDecimal(0);
        for (BigDecimal data : measData) {
            // if(i<measData.size())
            //{
            //maximum = data.max(measData.get(i++));
            maximum = data.max(maximum);
            //i++;
            //}
        }
        return maximum;


    }

    public BigDecimal getMinimum() {
        int i = 0;
        BigDecimal minimum = measData.get(0);

        for (BigDecimal data : measData) {
            // if(i<measData.size())
            //{
            minimum = data.min(minimum);
            //  i++;
            //   }
        }
        return minimum;
    }

    public BigDecimal getAverage() {
        BigDecimal average = new BigDecimal(0);
        average = getGrandTotal().divide(new BigDecimal(measData.size()), MathContext.DECIMAL64);
        return average;
    }

    public BigDecimal getData(int row) {
        return this.measData.get(row);
    }

    public ArrayList<BigDecimal> getData() {
        return this.measData;
    }
}
