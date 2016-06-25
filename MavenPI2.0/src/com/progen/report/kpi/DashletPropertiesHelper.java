package com.progen.report.kpi;

public class DashletPropertiesHelper {
    //for sort icon

    private int countForDisplay = 0;
    private String displayOrder = "top";
    private String sortOnMeasure = "";
    private boolean displayAllRows = false;
    private boolean sortAll = false;
    private boolean isTranspose = false;
    //for sort word
    private int typeForSort = -1;
    private String element_IDforSort;
    private boolean isFromTopBottom = false;
    private String dashletId;
    private String sortFlag;

    public int getCountForDisplay() {
        return countForDisplay;
    }

    public void setCountForDisplay(int countForDisplay) {
        this.countForDisplay = countForDisplay;
    }

    public String getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(String displayOrder) {
        this.displayOrder = displayOrder;
    }

    public String getSortOnMeasure() {
        return sortOnMeasure;
    }

    public void setSortOnMeasure(String sortOnMeasure) {
        this.sortOnMeasure = sortOnMeasure;
    }

    public boolean isDisplayAllRows() {
        return displayAllRows;
    }

    public void setDisplayAllRows(boolean displayAllRows) {
        this.displayAllRows = displayAllRows;
    }

    /**
     * @return the sortAll
     */
    public boolean isSortAll() {
        return sortAll;
    }

    /**
     * @param sortAll the sortAll to set
     */
    public void setSortAll(boolean sortAll) {
        this.sortAll = sortAll;
    }

    public boolean isIsTranspose() {
        return isTranspose;
    }

    public void setIsTranspose(boolean isTranspose) {
        this.isTranspose = isTranspose;
    }

    public int getTypeForSort() {
        return typeForSort;
    }

    public void setTypeForSort(int typeForSort) {
        this.typeForSort = typeForSort;
    }

    public String getElement_IDforSort() {
        return element_IDforSort;
    }

    public void setElement_IDforSort(String element_IDforSort) {
        this.element_IDforSort = element_IDforSort;
    }

    public boolean isIsFromTopBottom() {
        return isFromTopBottom;
    }

    public void setIsFromTopBottom(boolean isFromTopBottom) {
        this.isFromTopBottom = isFromTopBottom;
    }

    public String getDashletId() {
        return dashletId;
    }

    public void setDashletId(String dashletId) {
        this.dashletId = dashletId;
    }

    public String getSortFlag() {
        return sortFlag;
    }

    public void setSortFlag(String sortFlag) {
        this.sortFlag = sortFlag;
    }
}
