/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.entities;

/**
 *
 * @author progen
 */
public class QueryDetail {

    private String elementId;
    private String refElementId;
    private int colSequence;
    private String displayName;
    private int folderId;
    private int subFolderId;
    private String aggregationType;
    private String columnType;

    public String getAggregationType() {
        return aggregationType;
    }

    public void setAggregationType(String aggregationType) {
        this.aggregationType = aggregationType;
    }

    public int getColSequence() {
        return colSequence;
    }

    public void setColSequence(int colSequence) {
        this.colSequence = colSequence;
    }

    public String getColumnType() {
        return columnType;
    }

    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getElementId() {
        return elementId;
    }

    public void setElementId(String elementId) {
        this.elementId = elementId;
    }

    public int getFolderId() {
        return folderId;
    }

    public void setFolderId(int folderId) {
        this.folderId = folderId;
    }

    public String getRefElementId() {
        return refElementId;
    }

    public void setRefElementId(String refElementId) {
        this.refElementId = refElementId;
    }

    public int getSubFolderId() {
        return subFolderId;
    }

    public void setSubFolderId(int subFolderId) {
        this.subFolderId = subFolderId;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final QueryDetail other = (QueryDetail) obj;
        if (this.elementId != null && other.elementId != null && this.elementId.equals(other.elementId)) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }
}
