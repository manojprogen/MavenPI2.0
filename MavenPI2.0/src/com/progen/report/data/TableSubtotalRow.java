/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.data;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 *
 * @author progen
 */
public class TableSubtotalRow extends TableDataRow {

    String subTotalType;
    ArrayList<BigDecimal> measData;

    public void setSubtotalType(String subTotalType) {
        this.subTotalType = subTotalType;
    }

    public String getSubtotalType() {
        return this.subTotalType;
    }

    @Override
    public String getID(int column) {
        return rowDataIds.get(column) + "_" + subTotalType;
    }

    public void setRawNumericMeasData(ArrayList<BigDecimal> measData) {
        this.measData = measData;
    }

    public BigDecimal getRawNumericData(int column) {
        return this.measData.get(column);
    }

    public String getReportDrillList(int column) {
        String val = "";
        if (this.reportDrillList.get(column) != null) {
            return this.reportDrillList.get(column).toString();
        } else {
            return val;
        }
    }

    public void setReportDrillList(ArrayList<String> reportDrillList) {
        this.reportDrillList = reportDrillList;
    }
}
