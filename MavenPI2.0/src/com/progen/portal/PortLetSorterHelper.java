/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.portal;

import java.io.Serializable;

/**
 *
 * @author progen
 */
public class PortLetSorterHelper implements Serializable {

    private int countVal = 0;
    private String sortByColumeVal = "";
    private String sortType = "Top";
    private Boolean isSortAll = false;

    public int getCountVal() {
        return countVal;
    }

    public void setCountVal(int countVal) {
        this.countVal = countVal;
    }

    public String getSortByColumeVal() {
        return sortByColumeVal;
    }

    public void setSortByColumeVal(String sortByColumeVal) {
        this.sortByColumeVal = sortByColumeVal;
    }

    public String getSortType() {
        return sortType;
    }

    public void setSortType(String sortType) {
        this.sortType = sortType;
    }

    public Boolean getIsSortAll() {
        return isSortAll;
    }

    public void setIsSortAll(Boolean isSortAll) {
        this.isSortAll = isSortAll;
    }
}
