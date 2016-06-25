/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.search;

/**
 *
 * @author arun
 */
public class SearchMeasColumn<T> extends SearchColumn {

    String aggType;
    String colType;
    String filterOperator;
    Object filterOperand;
    String originalColType;

    public SearchMeasColumn(String elementId, String colName) {
        super(elementId, colName);
    }

    public void setAggregationType(String aggType) {
        this.aggType = aggType;
    }

    public void setColumnType(String colType) {
        this.colType = colType;
    }

    public void setFilterOperand(Object filterOperand) {
        this.filterOperand = filterOperand;
    }

    public void setOriginalColType(String originalColType) {
        this.originalColType = originalColType;
    }

    public String getOriginalColType() {
        return originalColType;
    }

    public void setFilterOperator(String filterOperator) {
        this.filterOperator = filterOperator;
    }

    public String getFilterOperator() {
        return this.filterOperator;
    }

    public Object getFilterOperand() {
        return this.filterOperand;
    }

    public boolean isFilterApplicableForMeasure() {
        if (this.filterOperator != null) {
            return true;
        } else {
            return false;
        }
    }
}
