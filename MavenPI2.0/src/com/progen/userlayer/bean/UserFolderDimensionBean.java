/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.userlayer.bean;

import java.util.ArrayList;

/**
 * @filename UserFolderDimensionBean
 *
 * @author santhosh.kumar@progenbusiness.com @date Sep 7, 2009, 11:51:10 AM
 */
public class UserFolderDimensionBean {

    private String subFolderId;
    private String subFolderName;
    private String dimId;
    private String dimName;
    private String memberId;
    private String subFolderTabId;
    private String memberName;
    private String memColName;
    private String memColId;
    private String elementId;
    private ArrayList UserFolderDimMbrsColumns;

    public String getSubFolderId() {
        return subFolderId;
    }

    public void setSubFolderId(String subFolderId) {
        this.subFolderId = subFolderId;
    }

    public String getSubFolderName() {
        return subFolderName;
    }

    public void setSubFolderName(String subFolderName) {
        this.subFolderName = subFolderName;
    }

    public String getDimName() {
        return dimName;
    }

    public void setDimName(String dimName) {
        this.dimName = dimName;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getMemColName() {
        return memColName;
    }

    public void setMemColName(String memColName) {
        this.memColName = memColName;
    }

    public String getMemColId() {
        return memColId;
    }

    public void setMemColId(String memColId) {
        this.memColId = memColId;
    }

    public String getDimId() {
        return dimId;
    }

    public void setDimId(String dimId) {
        this.dimId = dimId;
    }

    public String getSubFolderTabId() {
        return subFolderTabId;
    }

    public void setSubFolderTabId(String subFolderTabId) {
        this.subFolderTabId = subFolderTabId;
    }

    public ArrayList getUserFolderDimMbrsColumns() {
        return UserFolderDimMbrsColumns;
    }

    public void setUserFolderDimMbrsColumns(ArrayList UserFolderDimMbrsColumns) {
        this.UserFolderDimMbrsColumns = UserFolderDimMbrsColumns;
    }

    public String getElementId() {
        return elementId;
    }

    public void setElementId(String elementId) {
        this.elementId = elementId;
    }
}
