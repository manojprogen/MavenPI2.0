/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.userlayer.bean;

import java.util.ArrayList;

/**
 * @filename UserFolderBucketBean
 *
 * @author santhosh.kumar@progenbusiness.com @date Sep 7, 2009, 11:51:36 AM
 */
public class UserFolderBucketBean {

    private String bucketId;
    private String bucketName;
    private ArrayList UserFolderBucketsColumns;
    private String bucketColId;
    private String bucketColName;

    public String getBucketId() {
        return bucketId;
    }

    public void setBucketId(String bucketId) {
        this.bucketId = bucketId;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getBucketColId() {
        return bucketColId;
    }

    public void setBucketColId(String bucketColId) {
        this.bucketColId = bucketColId;
    }

    public String getBucketColName() {
        return bucketColName;
    }

    public void setBucketColName(String bucketColName) {
        this.bucketColName = bucketColName;
    }

    public ArrayList getUserFolderBucketsColumns() {
        return UserFolderBucketsColumns;
    }

    public void setUserFolderBucketsColumns(ArrayList UserFolderBucketsColumns) {
        this.UserFolderBucketsColumns = UserFolderBucketsColumns;
    }
}
