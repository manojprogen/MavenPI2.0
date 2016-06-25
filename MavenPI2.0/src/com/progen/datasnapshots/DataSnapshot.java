/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.datasnapshots;

import java.sql.Clob;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author Administrator
 */
public class DataSnapshot {

    private int snapShotId;
    private String reportId;
    private String snapShotName;
    private int refreshInterval;
    private Date refreshDate;
    private Clob htmlView;
    private String htmlView1;
    private Clob xmlView;
    private String AdvSnapshotFileName;

    public void setHtmlView(Clob htmlView) {
        this.htmlView = htmlView;
    }

    public void setHtmlViewString(String htmlView1) {
        this.htmlView1 = htmlView1;
    }

    public void setRefreshDate(Date refreshDate) {
        this.refreshDate = refreshDate;
    }

    public Date getRefreshDate() {
        return refreshDate;
    }

    public int getRefreshInterval() {
        return refreshInterval;
    }

    public String getReportId() {
        return reportId;
    }

    public void setSnapShotId(int snapShotId) {
        this.snapShotId = snapShotId;
    }

    public void setSnapShotName(String snapShotName) {
        this.snapShotName = snapShotName;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public void setRefreshInterval(int refreshInterval) {
        this.refreshInterval = refreshInterval;
    }

    public int getSnapShotId() {
        return snapShotId;
    }

    public Clob getHtmlView() {
        return htmlView;
    }

    public String getHtmlViewString() {
        return htmlView1;
    }

    public String getSnapShotName() {
        return snapShotName;
    }

    boolean isRefreshNeeded(Calendar currentDate) {
        if (refreshInterval == -1) {
            return false;
        } else {
            Calendar lastRefreshDate = Calendar.getInstance();
            lastRefreshDate.setTime(refreshDate);
            long milis1 = currentDate.getTimeInMillis();
            long milis2 = lastRefreshDate.getTimeInMillis();
            long diff = milis1 - milis2;
            diff = diff / (60 * 1000);
            if (diff > refreshInterval) {
                return true;
            } else {
                return false;
            }
        }
    }

    public Clob getXmlView() {
        return xmlView;
    }

    public void setXmlView(Clob xmlView) {
        this.xmlView = xmlView;
    }

    public String getAdvSnapshotFileName() {
        return AdvSnapshotFileName;
    }

    public void setAdvSnapshotFileName(String AdvSnapshotFileName) {
        this.AdvSnapshotFileName = AdvSnapshotFileName;
    }
}
