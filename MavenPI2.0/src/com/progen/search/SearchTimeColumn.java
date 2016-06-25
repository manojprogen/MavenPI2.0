/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.search;

import java.util.ArrayList;

/**
 *
 * @author arun
 */
public class SearchTimeColumn<T> extends SearchDimColumn {

    String timeLevel;
    String timeType;
    ArrayList<String> timeColumnTypes;

    public SearchTimeColumn(String elementId, String colName) {
        super(elementId, colName);
        timeColumnTypes = new ArrayList<String>();
    }

    public void setTimeLevel(String timeLevel) {
        this.timeLevel = timeLevel;
    }

    public void setTimeType(String timeType) {
        this.timeType = timeType;
    }

    public void addTimeColumnType(String columnType) {
        this.timeColumnTypes.add(columnType);
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
