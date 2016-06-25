/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.query;

import java.util.List;
import prg.db.PbReturnObject;

/**
 *
 * @author progen
 */
public class DataSet {

    PbReturnObject data;
    int noOfDays;
    private List<String> measNames;

    public int getNoOfDays() {
        return noOfDays;
    }

    public void setNoOfDays(int noOfDays) {
        this.noOfDays = noOfDays;
    }

    public DataSet(PbReturnObject retObj) {
        this.data = retObj;
    }

    public PbReturnObject getData() {
        return this.data;
    }

    public List<String> getMeasNames() {
        return measNames;
    }

    public void setMeasNames(List<String> measNames) {
        this.measNames = measNames;
    }
}
