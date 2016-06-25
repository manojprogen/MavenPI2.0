package com.progen.querylayer;

import java.util.ArrayList;

public class Table {

    private String tableName;
    private String tableId;
    private String columnId;
    private String columnName;
    private String isPk;
    private String isAvailable;
    private String endTable;
    private ArrayList connections;
    private String dropString;

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
     * @return the columnId
     */
    public String getColumnId() {
        return columnId;
    }

    /**
     * @param columnId the columnId to set
     */
    public void setColumnId(String columnId) {
        this.columnId = columnId;
    }

    /**
     * @return the columnName
     */
    public String getColumnName() {
        return columnName;
    }

    /**
     * @param columnName the columnName to set
     */
    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    /**
     * @return the isPk
     */
    public String getIsPk() {
        return isPk;
    }

    /**
     * @param isPk the isPk to set
     */
    public void setIsPk(String isPk) {
        this.isPk = isPk;
    }

    /**
     * @return the isAvailable
     */
    public String getIsAvailable() {
        return isAvailable;
    }

    /**
     * @param isAvailable the isAvailable to set
     */
    public void setIsAvailable(String isAvailable) {
        this.isAvailable = isAvailable;
    }

    /**
     * @return the endTable
     */
    public String getEndTable() {
        return endTable;
    }

    /**
     * @param endTable the endTable to set
     */
    public void setEndTable(String endTable) {
        this.endTable = endTable;
    }

    /**
     * @return the connections
     */
    public ArrayList getConnections() {
        return connections;
    }

    /**
     * @param connections the connections to set
     */
    public void setConnections(ArrayList connections) {
        this.connections = connections;
    }

    /**
     * @return the dropString
     */
    public String getDropString() {
        return dropString;
    }

    /**
     * @param dropString the dropString to set
     */
    public void setDropString(String dropString) {
        this.dropString = dropString;
    }
}
