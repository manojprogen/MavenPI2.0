/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.action;

import java.util.Properties;

/**
 *
 * @author Veena
 */
public class UserStatusHelper {

    private Boolean isActive = false;
    private Boolean portalViewer = false;
    private Boolean reportAnalyzer = false;
    private Boolean whatif = false;
    private Boolean headlines = false;
    private String userType;
    private Boolean queryStudio = false;
    private Boolean powerAnalyser = false;
    private Boolean restrictedpowerAnalyser = false;
    private Boolean oneView = false;
    private Boolean scoreCards = false;
    private Boolean Xtend_Users = false;
    public static UserStatusHelper statusHelper;
//    public int AdminTotalCnt;
    public int QueryStudionTotalCnt;
    public int PowerAnalyzerTotalCnt;
    public int PortalsTotalCnt;
    public int OneViewTotalCnt;
    public int ScorecardsTotalCnt;
    public int SuperAdmin;
    public int XtendUsers;

    public static UserStatusHelper getUserStatusHelper() {
        return statusHelper;
    }

    public UserStatusHelper() {
    }

    public static UserStatusHelper createUserStatusHelper(Properties props) {

        if (statusHelper == null) {
            statusHelper = new UserStatusHelper();
        }
        //  statusHelper.setAdminTotalCnt(Integer.parseInt(props.getProperty("Admin")));
        statusHelper.setQueryStudionTotalCnt(Integer.parseInt(props.getProperty("Query Studio")));
        statusHelper.setPowerAnalyzerTotalCnt(Integer.parseInt(props.getProperty("Power Analyzer")));
        statusHelper.setPortalsTotalCnt(Integer.parseInt(props.getProperty("Portals")));
        statusHelper.setOneViewTotalCnt(Integer.parseInt(props.getProperty("One View")));
        statusHelper.setScorecardsTotalCnt(Integer.parseInt(props.getProperty("Scorecards")));
        statusHelper.setSuperAdmin(Integer.parseInt(props.getProperty("SuperAdmin")));
//        statusHelper.setXtendUsers(Integer.parseInt(props.getProperty("Xtend")));
        return statusHelper;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Boolean getPortalViewer() {
        return portalViewer;
    }

    public void setPortalViewer(Boolean portalViewer) {
        this.portalViewer = portalViewer;
    }

    public Boolean getReportAnalyzer() {
        return reportAnalyzer;
    }

    public void setReportAnalyzer(Boolean reportAnalyzer) {
        this.reportAnalyzer = reportAnalyzer;
    }

    public Boolean getWhatif() {
        return whatif;
    }

    public void setWhatif(Boolean whatif) {
        this.whatif = whatif;
    }

    public Boolean getHeadlines() {
        return headlines;
    }

    public void setHeadlines(Boolean headlines) {
        this.headlines = headlines;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public int getOneViewTotalCnt() {
        return OneViewTotalCnt;
    }

    public void setOneViewTotalCnt(int OneViewTotalCnt) {
        this.OneViewTotalCnt = OneViewTotalCnt;
    }

    public int getPortalsTotalCnt() {
        return PortalsTotalCnt;
    }

    public void setPortalsTotalCnt(int PortalsTotalCnt) {
        this.PortalsTotalCnt = PortalsTotalCnt;
    }

    public int getPowerAnalyzerTotalCnt() {
        return PowerAnalyzerTotalCnt;
    }

    public void setPowerAnalyzerTotalCnt(int PowerAnalyzerTotalCnt) {
        this.PowerAnalyzerTotalCnt = PowerAnalyzerTotalCnt;
    }

    public int getQueryStudionTotalCnt() {
        return QueryStudionTotalCnt;
    }

    public void setQueryStudionTotalCnt(int QueryStudionTotalCnt) {
        this.QueryStudionTotalCnt = QueryStudionTotalCnt;
    }

    public int getScorecardsTotalCnt() {
        return ScorecardsTotalCnt;
    }

    public void setScorecardsTotalCnt(int ScorecardsTotalCnt) {
        this.ScorecardsTotalCnt = ScorecardsTotalCnt;
    }

//    public int getAdminTotalCnt() {
//        return AdminTotalCnt;
//    }
//
//    public void setAdminTotalCnt(int AdminTotalCnt) {
//        this.AdminTotalCnt = AdminTotalCnt;
//    }
    public Boolean getOneView() {
        return oneView;
    }

    public void setOneView(Boolean oneView) {
        this.oneView = oneView;
    }

    public Boolean getPowerAnalyser() {
        return powerAnalyser;
    }

    public void setPowerAnalyser(Boolean powerAnalyser) {
        this.powerAnalyser = powerAnalyser;
    }
    //Added by Ashutosh for RestrictedPowerAnalyser 10-12-2015

    public Boolean getRestrictedPowerAnalyser() {
        return restrictedpowerAnalyser;
    }

    public void setRestrictedPowerAnalyser(Boolean restrictedpowerAnalyser) {
        this.restrictedpowerAnalyser = restrictedpowerAnalyser;
    }
//Ended by Ashutosh

    public Boolean getQueryStudio() {
        return queryStudio;
    }

    public void setQueryStudio(Boolean queryStudio) {
        this.queryStudio = queryStudio;
    }

    public Boolean getScoreCards() {
        return scoreCards;
    }

    public void setScoreCards(Boolean scoreCards) {
        this.scoreCards = scoreCards;
    }

    public int getSuperAdmin() {
        return SuperAdmin;
    }

    public void setSuperAdmin(int SuperAdmin) {
        this.SuperAdmin = SuperAdmin;
    }
//     public int getXtendUsers() {
//        return XtendUsers;
//    }
//
//    public void setXtendUsers(int XtendUsers) {
//        this.XtendUsers = XtendUsers;
//    }

    public Boolean getXtendUser() {
        return Xtend_Users;
    }

    public void setXtendUser(Boolean Xtend_Users) {
        this.Xtend_Users = Xtend_Users;
    }
}
