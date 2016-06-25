/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.reportdesigner.action;

import com.progen.report.KPIElement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author srikanth.p@progenbusiness.com added for the GroupMeassure KPI
 */
public class GroupMeassureParams {

    private String meassureId;
    private String masterMeassureId;
    private String meassureValue;
    private boolean isOddLevel;
    private String childMeassureId;
    private String dahletId;
    private String dbrdId;
    private String displayType;
    private ArrayList<HashMap<String, ArrayList>> groupMap = new ArrayList<HashMap<String, ArrayList>>();
    private String folderId;
    private String groupId;
    private String groupName;
    private String groupDisplayType = "";
    private List<KPIElement> kPIElements;
//    private String parameter;

    public String getMeassureId() {
        return meassureId;
    }

    public void setMeassureId(String meassureId) {
        this.meassureId = meassureId;
    }

    public String getMasterMeassureId() {
        return masterMeassureId;
    }

    public void setMasterMeassureId(String masterMeassureId) {
        this.masterMeassureId = masterMeassureId;
    }

    public String getMeassureValue() {
        return meassureValue;
    }

    public void setMeassureValue(String meassureValue) {
        this.meassureValue = meassureValue;
    }

    public boolean isIsOddLevel() {
        return isOddLevel;
    }

    public void setIsOddLevel(boolean isOddLevel) {
        this.isOddLevel = isOddLevel;
    }

    public String getChildDimId() {
        return childMeassureId;
    }

    public void setChildMeassureId(String childMeassureId) {
        this.childMeassureId = childMeassureId;
    }

    public String getDahletId() {
        return dahletId;
    }

    public void setDahletId(String dahletId) {
        this.dahletId = dahletId;
    }

    public String getDbrdId() {
        return dbrdId;
    }

    public void setDbrdId(String dbrdId) {
        this.dbrdId = dbrdId;
    }

    /**
     * @return the displayType
     */
    public String getDisplayType() {
        return displayType;
    }

    /**
     * @param displayType the displayType to set
     */
    public void setDisplayType(String displayType) {
        this.displayType = displayType;
    }

    /**
     * @return the folderId
     */
    public String getFolderId() {
        return folderId;
    }

    /**
     * @param folderId the folderId to set
     */
    public void setFolderId(String folderId) {
        this.folderId = folderId;
    }

    /**
     * @return the grouId
     */
    public String getGroupId() {
        return groupId;
    }

    /**
     * @param grouId the grouId to set
     */
    public void setGroupId(String grouId) {
        this.groupId = grouId;
    }

    /**
     * @return the groupName
     */
    public String getGroupName() {
        return groupName;
    }

    /**
     * @param groupName the groupName to set
     */
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    /**
     * @return the groupMap
     */
    public ArrayList<HashMap<String, ArrayList>> getGroupMap() {
        return groupMap;
    }

    /**
     * @param groupMap the groupMap to set
     */
    public void setGroupMap(HashMap<String, ArrayList> groupMap) {
        this.groupMap.add(groupMap);
    }

    /**
     * @return the groupDisplayType
     */
    public String getGroupDisplayType() {
        return groupDisplayType;
    }

    /**
     * @param groupDisplayType the groupDisplayType to set
     */
    public void setGroupDisplayType(String groupDisplayType) {
        this.groupDisplayType = groupDisplayType;
    }

    /**
     * @return the kPIElements
     */
    public List<KPIElement> getkPIElements() {
        return kPIElements;
    }

    /**
     * @param kPIElements the kPIElements to set
     */
    public void setkPIElements(List<KPIElement> kPIElements) {
        this.kPIElements = kPIElements;
    }
//    public String getParameter() {
//        return parameter;
//    }
//
//    public void setParameter(String parameter) {
//        this.parameter = parameter;
//    }
}
