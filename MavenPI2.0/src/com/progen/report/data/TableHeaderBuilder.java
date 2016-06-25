/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.data;

/**
 *
 * @author arun
 */
public abstract class TableHeaderBuilder {

    protected DataFacade facade;
    protected String rowViewMode;

    public TableHeaderBuilder(DataFacade facade) {
        this.facade = facade;
    }

    public void setRowViewMode(String rowViewMode) {
        this.rowViewMode = rowViewMode;
    }

    public abstract TableHeaderRow[] getHeaderRowData();
}
