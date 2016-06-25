/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.data;

import java.util.ArrayList;

/**
 *
 * @author arun
 */
public class TableSearchRow extends TableRow {

    ArrayList<String> rowData;
    String reportId;
    String ctxPath;
    ArrayList<String> dataType;

    @Override
    public String getRowData(int column) {
        return rowData.get(column);
    }

    public void setRowData(ArrayList<String> searchData) {
        this.rowData = searchData;
    }

    @Override
    public String getID(int column) {
        return rowDataIds.get(column) + "_SRCH";
    }

    public String getReportId() {
        return this.reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public String getPath() {
        return this.ctxPath;
    }

    public void setPath(String ctxpath) {
        this.ctxPath = ctxpath;
    }

    public ArrayList<String> getDataType() {
        return this.dataType;
    }

    public void setDataType(ArrayList<String> dataType) {
        this.dataType = dataType;
    }
}
