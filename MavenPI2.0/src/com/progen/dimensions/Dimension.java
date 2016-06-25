/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.dimensions;

import java.util.ArrayList;

/**
 *
 * @author Administrator
 */
public class Dimension {

    private String dimensionId;
    private String dimensionName;
    private String tableId;
    private String tableName;
    private String dimensionTblId;
    private ArrayList tableList;
    private String endDimension;

    /**
     * @return the dimensionId
     */
    public String getDimensionId() {
        return dimensionId;
    }

    /**
     * @param dimensionId the dimensionId to set
     */
    public void setDimensionId(String dimensionId) {
        this.dimensionId = dimensionId;
    }

    /**
     * @return the dimensionName
     */
    public String getDimensionName() {
        return dimensionName;
    }

    /**
     * @param dimensionName the dimensionName to set
     */
    public void setDimensionName(String dimensionName) {
        this.dimensionName = dimensionName;
    }

    /**
     * @return the tableId
     */
    public String getTableId() {
        return tableId;
    }

    /**
     * @param tableId the tableId to set
     */
    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    /**
     * @return the tableName
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * @param tableName the tableName to set
     */
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    /**
     * @return the dimensionTblId
     */
    public String getDimensionTblId() {
        return dimensionTblId;
    }

    /**
     * @param dimensionTblId the dimensionTblId to set
     */
    public void setDimensionTblId(String dimensionTblId) {
        this.dimensionTblId = dimensionTblId;
    }

    /**
     * @return the tableList
     */
    public ArrayList getTableList() {
        return tableList;
    }

    /**
     * @param tableList the tableList to set
     */
    public void setTableList(ArrayList tableList) {
        this.tableList = tableList;
    }

    /**
     * @return the endDimension
     */
    public String getEndDimension() {
        return endDimension;
    }

    /**
     * @param endDimension the endDimension to set
     */
    public void setEndDimension(String endDimension) {
        this.endDimension = endDimension;
    }
}
