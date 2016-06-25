/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.scheduler;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Dinanath
 */
public class KPIAlertSchedule {

    private String kpiAlertSchedId = null;
    private String kpiAlertSchedName = null;
    private String measureType = null;
    private String operatorValuesArr[] = null;
    private String startValuesArr[] = null;
    private String endValuesArr[] = null;
    private String mailIdsArr[] = null;
    private Date startDate;
    private Date endDate;
    private String scheduledTime = "00:00";
    private String frequency = null;
    private String chartName = null;
    private String reportId = null;
    private String userId = null;
    private String particularDay = null;
    private String contextPath=null;
    private String globalFilePath=null;
    private boolean isFromKPIChart=false;
    private String reportName=null;
    private Date lastSentDate;
    private String moduleType = "";

    public Date getLastSentDate() {
        return lastSentDate;
    }

    public void setLastSentDate(Date lastSentDate) {
        this.lastSentDate = lastSentDate;
    }

    public String getModuleType() {
        return moduleType;
    }

    public void setModuleType(String moduleType) {
        this.moduleType = moduleType;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }
    private Map<String, Map<String,String>> chartMapData = new HashMap<String,Map<String,String>>();

    public Map<String, Map<String, String>> getChartMapData() {
        return chartMapData;
    }

    public void setChartMapData(Map<String, Map<String, String>> chartMapData) {
        this.chartMapData = chartMapData;
    }
    public boolean isIsFromKPIChart() {
        return isFromKPIChart;
    }

    public void setIsFromKPIChart(boolean isFromKPIChart) {
        this.isFromKPIChart = isFromKPIChart;
    }

    public String getContextPath() {
        return contextPath;
    }

    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }

    public String getGlobalFilePath() {
        return globalFilePath;
    }

    public void setGlobalFilePath(String globalFilePath) {
        this.globalFilePath = globalFilePath;
    }

    public String getParticularDay() {
        return particularDay;
    }

    public void setParticularDay(String particularDay) {
        this.particularDay = particularDay;
    }

    public String getKpiAlertSchedName() {
        return kpiAlertSchedName;
    }

    public void setKpiAlertSchedName(String kpiAlertSchedName) {
        this.kpiAlertSchedName = kpiAlertSchedName;
    }



    public String getMeasureType() {
        return measureType;
    }

    public void setMeasureType(String measureType) {
        this.measureType = measureType;
    }

    public String[] getOperatorValuesArr() {
        return operatorValuesArr;
    }

    public void setOperatorValuesArr(String[] operatorValuesArr) {
        this.operatorValuesArr = operatorValuesArr;
    }

    public String[] getStartValuesArr() {
        return startValuesArr;
    }

    public void setStartValuesArr(String[] startValuesArr) {
        this.startValuesArr = startValuesArr;
    }

    public String[] getEndValuesArr() {
        return endValuesArr;
    }

    public void setEndValuesArr(String[] endValuesArr) {
        this.endValuesArr = endValuesArr;
    }

    public String[] getMailIdsArr() {
        return mailIdsArr;
    }

    public void setMailIdsArr(String[] mailIdsArr) {
        this.mailIdsArr = mailIdsArr;
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

   

    public String getScheduledTime() {
        return scheduledTime;
    }

    public void setScheduledTime(String scheduledTime) {
        this.scheduledTime = scheduledTime;
    }

   

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getChartName() {
        return chartName;
    }

    public void setChartName(String chartName) {
        this.chartName = chartName;
    }

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getKpiAlertSchedId() {
        return kpiAlertSchedId;
    }

    public void setKpiAlertSchedId(String kpiAlertSchedId) {
        this.kpiAlertSchedId = kpiAlertSchedId;
    }
}
