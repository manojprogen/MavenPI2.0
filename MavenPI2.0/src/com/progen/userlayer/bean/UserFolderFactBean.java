/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.userlayer.bean;

import java.util.ArrayList;

/**
 * @filename UserFolderFactBean
 *
 * @author santhosh.kumar@progenbusiness.com @date Sep 7, 2009, 11:51:23 AM
 */
public class UserFolderFactBean {

    private String factName;
    private String factId;
    private String factColName;
    private String factColId;
    private ArrayList UserFolderFactsColumns;

    public String getFactName() {
        return factName;
    }

    public void setFactName(String factName) {
        this.factName = factName;
    }

    public String getFactId() {
        return factId;
    }

    public void setFactId(String factId) {
        this.factId = factId;
    }

    public String getFactColName() {
        return factColName;
    }

    public void setFactColName(String factColName) {
        this.factColName = factColName;
    }

    public String getFactColId() {
        return factColId;
    }

    public void setFactColId(String factColId) {
        this.factColId = factColId;
    }

    public ArrayList getUserFolderFactsColumns() {
        return UserFolderFactsColumns;
    }

    public void setUserFolderFactsColumns(ArrayList UserFolderFactsColumns) {
        this.UserFolderFactsColumns = UserFolderFactsColumns;
    }
}
