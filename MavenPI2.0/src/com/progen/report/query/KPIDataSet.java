/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.query;

import prg.db.PbReturnObject;

/**
 *
 * @author progen
 */
public class KPIDataSet {

    PbReturnObject kpiData;
    int noOfDays;

    public int getNoOfDays() {
        return noOfDays;
    }

    public void setNoOfDays(int noOfDays) {
        this.noOfDays = noOfDays;
    }

    public KPIDataSet(PbReturnObject retObj) {
        this.kpiData = retObj;
    }

    public PbReturnObject getKPIData() {
        return this.kpiData;
    }
}
