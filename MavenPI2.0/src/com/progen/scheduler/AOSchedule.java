/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.scheduler;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 *
 * @author parsi
 */
public class AOSchedule {
    private String mailIdsArr[] = null;
    private String mailIds = null;
    private String AOSchedName = null;
    private String AOSchedId = null;
    private String loadType = null;
    private Date startDate;
    private Date endDate;
    private String scheduledTime = "00:00";
   // private String Date = "";
    private String frequency = null;
    private String reportId = null;
    private String userId = null;
    private String particularDay = null;
    private String contextPath=null;
    private String globalFilePath=null;
    private String startDateString=null;
    private String loadStartDate=null;
    private String endDateString=null;
    private String dateType=null;
    private String loadEndDate=null;
    private boolean isAO=false;
    private HashMap<String , ArrayList<String>> AOdetails=new HashMap();

    public boolean isIsAO() {
        return isAO;
    }

    public void setIsAO(boolean isAO) {
        this.isAO = isAO;
    }

    public String getAOSchedId() {
        return AOSchedId;
    }

    public void setAOSchedId(String AOSchedId) {
        this.AOSchedId = AOSchedId;
    }

    public String getAOSchedName() {
        return AOSchedName;
    }

    public void setAOSchedName(String AOSchedName) {
        this.AOSchedName = AOSchedName;
    }

    public String[] getMailIdsArr() {
        return mailIdsArr;
    }

    public void setMailIdsArr(String[] mailIdsArr) {
        this.mailIdsArr = mailIdsArr;
    }
    public String getMailIds() {
        return mailIds;
    }

    public void setMailIds(String mailIds) {
        this.mailIds = mailIds;
    }
    public String getLoadType() {
        return loadType;
    }

    public void setLoadType(String loadType) {
        this.loadType = loadType;
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
//    public void setDate(String Date) {
//        this.Date = Date;
//    }
//
//    public String getDate() {
//        return Date;
//    }

    public void setScheduledTime(String scheduledTime) {
        this.scheduledTime = scheduledTime;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
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

    public String getParticularDay() {
        return particularDay;
    }

    public void setParticularDay(String particularDay) {
        this.particularDay = particularDay;
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
    public String getStartDateString(){
        
    return startDateString;
    }
    public void setStartDateString(String startDateString){
        
    this.startDateString=startDateString;
    
    }
    public String getEndDateString(){
        
    return endDateString;
    }
    public void setEndDateString(String endDateString){
        
    this.endDateString=endDateString;
    
    }
    public String getDateType(){
        
    return dateType;
    }
    public void setDateType(String dateType){
        
    this.dateType=dateType;
    
    }
     public String getloadStartDate(){
        
    return loadStartDate;
    }
    public void setloadStartDate(String loadStartDate){
        
    this.loadStartDate=loadStartDate;
    
    }
    public String getloadEndDate(){
        
    return loadEndDate;
    }
    public void setloadEndDate(String loadEndDate){
        
    this.loadEndDate=loadEndDate;
    
    }
    public HashMap getAOdetails(){
        
    return AOdetails;
    
    }
    public void setAOdetails(HashMap dataMap){
        
    this.AOdetails=dataMap;
    
            }
    
}
