/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.data;

import java.util.ArrayList;

/**
 *
 * @author progen
 */
public class TableHeaderRow extends TableRow {

    ArrayList<String> rowData;
    ArrayList<String> sortImagePath;
    int layer;
    String headerLinks;
    private String headerRepLinks;

    public String getHeaderLinks() {
        return headerLinks;
    }

    public String getHeaderRepLinks() {
        return headerRepLinks;
    }

    public void setHeaderLinks(String headerLinks) {
        this.headerLinks = headerLinks;
    }

    public void setHeaderRepLinks(String headerRepLinks) {
        this.headerRepLinks = headerRepLinks;
    }

    @Override
    public String getID(int column) {
        return rowDataIds.get(column) + "_" + layer + "_H";
    }

    @Override
    public String getRowData(int column) {
        return this.getRowData().get(column);
    }

    public void setRowData(ArrayList<String> rowData) {
        this.rowData = rowData;
    }

    public void setLayerNumber(int layer) {
        this.layer = layer;
    }

    public String getSortImagePath(int column) {
        if (column < sortImagePath.size()) {
            return sortImagePath.get(column).toString();
        } else {
            //   
            return "";

        }
    }

    public void setSortImagePath(ArrayList<String> sortImagePath) {
        this.sortImagePath = sortImagePath;
    }

    public int getLayer() {
        return layer;
    }

    /**
     * @return the rowData
     */
    public ArrayList<String> getRowData() {
        return rowData;
    }
}
