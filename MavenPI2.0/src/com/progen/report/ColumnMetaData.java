package com.progen.report;

public class ColumnMetaData {

    String columnName;
    String displayName;
    String searchName;
    String rounding;
    String aggregation;
    String isFilter;
    String dataType;
    String dateFormat;
    String compare;

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setSearchName(String searchName) {
        this.searchName = searchName;
    }

    public String getSearchName() {
        return searchName;
    }

    public void setRounding(String rounding) {
        this.rounding = rounding;
    }

    public String getRounding() {
        return rounding;
    }

    public void setAggregation(String aggregation) {
        this.aggregation = aggregation;
    }

    public String getAggregation() {
        return aggregation;
    }

    public void setIsFilter(String isFilter) {
        this.isFilter = isFilter;
    }

    public String getIsFilter() {
        return isFilter;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public void setCompare(String compare) {
        this.compare = compare;
    }

    public String getCompare() {
        return compare;
    }
}
