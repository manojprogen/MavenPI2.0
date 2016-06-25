package com.progen.querylayer;

import java.util.ArrayList;

public class Dimension {

    private String dimensionName;
    private String dimensionId;
    private ArrayList tableList;
    private ArrayList membersList;
    private ArrayList hierarchyList;

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
     * @return the membersList
     */
    public ArrayList getMembersList() {
        return membersList;
    }

    /**
     * @param membersList the membersList to set
     */
    public void setMembersList(ArrayList membersList) {
        this.membersList = membersList;
    }

    /**
     * @return the hierarchyList
     */
    public ArrayList getHierarchyList() {
        return hierarchyList;
    }

    /**
     * @param hierarchyList the hierarchyList to set
     */
    public void setHierarchyList(ArrayList hierarchyList) {
        this.hierarchyList = hierarchyList;
    }
    /**
     * @return the heirarchyList
     */
}
