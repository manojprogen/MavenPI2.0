/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.search;

/**
 *
 * @author arun
 */
public class SearchDimColumn<T> extends SearchColumn {

    boolean isRowViewBy;
    boolean isColViewBy;

    public SearchDimColumn(String elementId, String colName) {
        super(elementId, colName);
        isRowViewBy = false;
        isColViewBy = false;
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    public void setRowViewByColumn(boolean isRowViewBy) {
        this.isRowViewBy = isRowViewBy;
    }

    public void setColViewByColumn(boolean isColViewBy) {
        this.isColViewBy = isColViewBy;
    }

    public boolean isRowViewByColumn() {
        return this.isRowViewBy;
    }
}
