package com.progen.querylayer;

import java.util.ArrayList;

/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
/**
 *
 * @author Saurabh
 */
public class DatabaseConnection {

    private String connectionName;
    private String connectionType;
    private String connectionId;
    private ArrayList tableList;
    private ArrayList viewList;
    private ArrayList dimensionList;
    private String openConnection;

    /**
     * @return the connectionName
     */
    public String getConnectionName() {
        return connectionName;
    }

    /**
     * @param connectionName the connectionName to set
     */
    public void setConnectionName(String connectionName) {
        this.connectionName = connectionName;
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
     * @return the viewList
     */
    public ArrayList getViewList() {
        return viewList;
    }

    /**
     * @param viewList the viewList to set
     */
    public void setViewList(ArrayList viewList) {
        this.viewList = viewList;
    }

    /**
     * @return the openConnection
     */
    public String getOpenConnection() {
        return openConnection;
    }

    /**
     * @param openConnection the openConnection to set
     */
    public void setOpenConnection(String openConnection) {
        this.openConnection = openConnection;
    }

    /**
     * @return the connectionId
     */
    public String getConnectionId() {
        return connectionId;
    }

    /**
     * @param connectionId the connectionId to set
     */
    public void setConnectionId(String connectionId) {
        this.connectionId = connectionId;
    }

    /**
     * @return the dimensionList
     */
    public ArrayList getDimensionList() {
        return dimensionList;
    }

    /**
     * @param dimensionList the dimensionList to set
     */
    public void setDimensionList(ArrayList dimensionList) {
        this.dimensionList = dimensionList;
    }

    /**
     * @return the connectionType
     */
    public String getConnectionType() {
        return connectionType;
    }

    /**
     * @param connectionType the connectionType to set
     */
    public void setConnectionType(String connectionType) {
        this.connectionType = connectionType;
    }
}
