/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.scheduler;

/**
 *
 * @author progen
 */
public class ReportScheduleSlices {

    private int schedulerId;
    private int schdPreferenceId;
    private String rowViewById;
    private String rowViewByVal;

    public int getSchedulerId() {
        return schedulerId;
    }

    public void setSchedulerId(int schedulerId) {
        this.schedulerId = schedulerId;
    }

    public int getSchdPreferenceId() {
        return schdPreferenceId;
    }

    public void setSchdPreferenceId(int schdPreferenceId) {
        this.schdPreferenceId = schdPreferenceId;
    }

    public String getRowViewById() {
        return rowViewById;
    }

    public void setRowViewById(String rowViewById) {
        this.rowViewById = rowViewById;
    }

    public String getRowViewByVal() {
        return rowViewByVal;
    }

    public void setRowViewByVal(String rowViewByVal) {
        this.rowViewByVal = rowViewByVal;
    }
}
