/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.scheduler;

/**
 *
 * @author progen
 */
public class ReportDetails {

    private String reportId;
    private String reportName;
    private String reportType;
    private String reportDesc;

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }
    //addded by srikanth.p for onmouseover show descriptin

    public void setReportDesc(String reportDesc) {
        this.reportDesc = reportDesc;
    }

    public String setReportDesc() {
        return reportDesc;
    }
}
