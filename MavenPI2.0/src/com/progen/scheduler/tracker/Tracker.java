/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.scheduler.tracker;

import com.google.common.collect.ArrayListMultimap;
import java.util.Date;
import java.util.List;

/**
 *
 * @author progen
 */
public class Tracker {

    private int trackerId;
    private String trackerName;
    private Date startDate;
    private Date endDate;
    private String frequency;
    private String scheduledTime;
    private String measureId;
    private String measureName;
    private String dimensionName;
    private String viewById;
    private String mode;
    private String subscribers;
    private String userId;
    private String folderId;
    private ArrayListMultimap<String, TrackerCondition> trackerConditions;
//    private ArrayList<TrackerCondition> trackerConditions;
    private boolean isAutoIdentifier;
    private String parameterXml;
    private String parameter;
    private String condValue;
    private String condOperator;
    private String supportingMsr;
    private String supportingMsrNames;
    private boolean sendAnyWay;
    private String particularDay;

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getMeasureId() {
        return measureId;
    }

    public void setMeasureId(String measureId) {
        this.measureId = measureId;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getScheduledTime() {
        return scheduledTime;
    }

    public void setScheduledTime(String scheduledTime) {
        this.scheduledTime = scheduledTime;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public String getSubscribers() {
        return subscribers;
    }

    public void setSubscribers(String subsribers) {
        this.subscribers = subsribers;
    }

    public int getTrackerId() {
        return trackerId;
    }

    public void setTrackerId(int trackerId) {
        this.trackerId = trackerId;
    }

    public String getTrackerName() {
        return trackerName;
    }

    public void setTrackerName(String trackerName) {
        this.trackerName = trackerName;
    }

    public String getViewById() {
        return viewById;
    }

    public void setViewById(String viewById) {
        this.viewById = viewById;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

//
//    public void setTrackerConditions(List<TrackerCondition> trackerConditions) {
//        this.trackerConditions = trackerConditions;
//    }
//
//    public void addTrackerCondition(TrackerCondition condition){
//        if (this.trackerConditions == null)
//            this.trackerConditions = new ArrayList<TrackerCondition>();
//
//        this.trackerConditions.add(condition);
//    }
    public String getFolderId() {
        return folderId;
    }

    public void setFolderId(String folderId) {
        this.folderId = folderId;
    }

    public String getMeasureName() {
        return measureName;
    }

    public void setMeasureName(String measureName) {
        this.measureName = measureName;
    }

    public String getDimensionName() {
        return dimensionName;
    }

    public void setDimensionName(String dimensionName) {
        this.dimensionName = dimensionName;
    }

    public boolean isAutoIdentify() {
        return isAutoIdentifier;
    }

    public void setisAutoIdentify(boolean isAutoIdentify) {
        this.isAutoIdentifier = isAutoIdentify;
    }

    public String getParameterXml() {
        return parameterXml;
    }

    public void setParameterXml(String parameterXml) {
        this.parameterXml = parameterXml;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public String getCondValue() {
        return condValue;
    }

    public void setCondValue(String condValue) {
        this.condValue = condValue;
    }

    public String getCondOperator() {
        return condOperator;
    }

    public void setCondOperator(String condOperator) {
        this.condOperator = condOperator;
    }

    public String getSupportingMsr() {
        return supportingMsr;
    }

    public void setSupportingMsr(String supportingMsr) {
        this.supportingMsr = supportingMsr;
    }

    public String getSupportingMsrNames() {
        return supportingMsrNames;
    }

    public void setSupportingMsrNames(String supportingMsrNames) {
        this.supportingMsrNames = supportingMsrNames;
    }

    public boolean isSendAnyWay() {
        return sendAnyWay;
    }

    public void setSendAnyWay(boolean sendAnyWay) {
        this.sendAnyWay = sendAnyWay;
    }

    public String getParticularDay() {
        return particularDay;
    }

    public void setParticularDay(String particularDay) {
        this.particularDay = particularDay;
    }

    public void setTrackerConditions(ArrayListMultimap<String, TrackerCondition> trackerConditions) {
        this.trackerConditions = trackerConditions;
    }

    public ArrayListMultimap<String, TrackerCondition> getTrackerConditions() {
        return trackerConditions;
    }

    public List<TrackerCondition> getTrackerConditions(String dimensionValue) {
        return trackerConditions.get(dimensionValue);
    }

    public void addTrackerCond(String dimValue, TrackerCondition trackerCondition) {
        trackerConditions.put(dimValue, trackerCondition);
    }
}
