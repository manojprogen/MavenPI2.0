package com.progen.querylayer;

/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
/**
 *
 * @author Saurabh
 */

public class DimensionTable {

    private String tableName;
    private String tableId;
    private String columnId;
    private String columnName;
    private String memberName;
    private String memberId;
    private String relationName;
    private String relationId;
    private String relColumnId;
    private String relColumnName;
    private String isPk;
    private String isAvailable;
    private String endTable;
    private String relType;
    private String endColumn;

    /**
     * @return the endColumn
     */
    public String getEndColumn() {
        return endColumn;
    }

    /**
     * @param endColumn the endColumn to set
     */
    public void setEndColumn(String endColumn) {
        this.endColumn = endColumn;
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
     * @return the endTable
     */
    /**
     * @return the memberName
     */
    public String getMemberName() {
        return memberName;
    }

    /**
     * @param memberName the memberName to set
     */
    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    /**
     * @return the memberId
     */
    public String getMemberId() {
        return memberId;
    }

    /**
     * @param memberId the memberId to set
     */
    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    /**
     * @return the relationName
     */
    public String getRelationName() {
        return relationName;
    }

    /**
     * @param relationName the relationName to set
     */
    public void setRelationName(String relationName) {
        this.relationName = relationName;
    }

    /**
     * @return the relationId
     */
    public String getRelationId() {
        return relationId;
    }

    /**
     * @param relationId the relationId to set
     */
    public void setRelationId(String relationId) {
        this.relationId = relationId;
    }

    /**
     * @return the relColumnId
     */
    public String getRelColumnId() {
        return relColumnId;
    }

    /**
     * @param relColumnId the relColumnId to set
     */
    public void setRelColumnId(String relColumnId) {
        this.relColumnId = relColumnId;
    }

    /**
     * @return the relColumnName
     */
    public String getRelColumnName() {
        return relColumnName;
    }

    /**
     * @param relColumnName the relColumnName to set
     */
    public void setRelColumnName(String relColumnName) {
        this.relColumnName = relColumnName;
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
     * @return the relType
     */
    public String getRelType() {
        return relType;
    }

    /**
     * @param relType the relType to set
     */
    public void setRelType(String relType) {
        this.relType = relType;
    }
}
