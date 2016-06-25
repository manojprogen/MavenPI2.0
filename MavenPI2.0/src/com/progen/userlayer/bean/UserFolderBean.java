/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.userlayer.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @filename UserFolderBean
 *
 * @author santhosh.kumar@progenbusiness.com @date Sep 7, 2009, 11:49:35 AM
 */
public class UserFolderBean implements Serializable {

    private String folderName;
    private String folderId;
    private String subFolderId;
    private String subFolderName;
    private ArrayList factList;
    private ArrayList bucketList;
    private ArrayList dimensionList;
    private ArrayList SubFolderList;
    private ArrayList userFolderDims;
    private ArrayList userFolderFacts;
    private ArrayList userFolderBuckets;

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public String getFolderId() {
        return folderId;
    }

    public void setFolderId(String folderId) {
        this.folderId = folderId;
    }

    public ArrayList getFactList() {
        return factList;
    }

    public void setFactList(ArrayList factList) {
        this.factList = factList;
    }

    public ArrayList getBucketList() {
        return bucketList;
    }

    public void setBucketList(ArrayList bucketList) {
        this.bucketList = bucketList;
    }

    public ArrayList getDimensionList() {
        return dimensionList;
    }

    public void setDimensionList(ArrayList dimensionList) {
        this.dimensionList = dimensionList;
    }

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

    public ArrayList getSubFolderList() {
        return SubFolderList;
    }

    public void setSubFolderList(ArrayList SubFolderList) {
        this.SubFolderList = SubFolderList;
    }

    public ArrayList getUserFolderDims() {
        return userFolderDims;
    }

    public void setUserFolderDims(ArrayList userFolderDims) {
        this.userFolderDims = userFolderDims;
    }

    public ArrayList getUserFolderFacts() {
        return userFolderFacts;
    }

    public void setUserFolderFacts(ArrayList userFolderFacts) {
        this.userFolderFacts = userFolderFacts;
    }

    public ArrayList getUserFolderBuckets() {
        return userFolderBuckets;
    }

    public void setUserFolderBuckets(ArrayList userFolderBuckets) {
        this.userFolderBuckets = userFolderBuckets;
    }
}
