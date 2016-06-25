/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.scheduler;

/**
 *
 * @author progen
 */
public class ReportSchedulePreferences {

    private int schedulerId;
    private String dimId;
    private String dimValues;
    private String mailIds;

    public int getSchedulerId() {
        return schedulerId;
    }

    public void setSchedulerId(int schedulerId) {
        this.schedulerId = schedulerId;
    }

    public String getDimId() {
        return dimId;
    }

    public void setDimId(String dimId) {
        this.dimId = dimId;
    }

    public String getDimValues() {
        return dimValues;
    }

    public void setDimValues(String dimValues) {
        this.dimValues = dimValues;
    }

    public String getMailIds() {
        return mailIds;
    }

    public void setMailIds(String mailIds) {
        this.mailIds = mailIds;
    }
}
