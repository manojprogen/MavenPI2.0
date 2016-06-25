/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.scorecard.tracker;

import java.util.Date;
import java.util.List;

/**
 *
 * @author progen
 */
public class ScorecardTracker {

    private int sCardShedId;
    private int sCardId;
    private String trackerName;
    private Date startDate;
    private Date endDate;
    private int createdBy;
    private int updatedBy;
    private Date createdDate;
    private Date upDatedDate;
    private String frequency;
    private String folderId;
    private String particularDay;
    private String scheduleTime;
    private Date asOfDate;
    private String compareWith;
    private String period;
    private List<ScorecardTrackerRule> ruleLst;

    public int getsCardShedId() {
        return sCardShedId;
    }

    public void setsCardShedId(int sCardShedId) {
        this.sCardShedId = sCardShedId;
    }

    public int getsCardId() {
        return sCardId;
    }

    public void setsCardId(int sCardId) {
        this.sCardId = sCardId;
    }

    public String getTrackerName() {
        return trackerName;
    }

    public void setTrackerName(String trackerName) {
        this.trackerName = trackerName;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    public int getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(int updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getUpDatedDate() {
        return upDatedDate;
    }

    public void setUpDatedDate(Date upDatedDate) {
        this.upDatedDate = upDatedDate;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getFolderId() {
        return folderId;
    }

    public void setFolderId(String folderId) {
        this.folderId = folderId;
    }

    public String getParticularDay() {
        return particularDay;
    }

    public void setParticularDay(String particularDay) {
        this.particularDay = particularDay;
    }

    public List<ScorecardTrackerRule> getRuleLst() {
        return ruleLst;
    }

    public void setRuleLst(List<ScorecardTrackerRule> ruleLst) {
        this.ruleLst = ruleLst;
    }

    public String getScheduleTime() {
        return scheduleTime;
    }

    public void setScheduleTime(String scheduleTime) {
        this.scheduleTime = scheduleTime;
    }

    public Date getAsOfDate() {
        return asOfDate;
    }

    public void setAsOfDate(Date asOfDate) {
        this.asOfDate = asOfDate;
    }

    public String getCompareWith() {
        return compareWith;
    }

    public void setCompareWith(String compareWith) {
        this.compareWith = compareWith;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }
}
