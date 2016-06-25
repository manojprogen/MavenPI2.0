/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.query;

/**
 *
 * @author arun
 */
public enum RTDimensionElement {

    SEGMENT("_seg", " (Segment)"),
    NONE("", "");
    private final String colType;
    private final String colDisplay;

    RTDimensionElement(String colType, String colDisplay) {
        this.colDisplay = colDisplay;
        this.colType = colType;
    }

    public String getColumnType() {
        return this.colType;
    }

    public String getColumnDisplay() {
        return this.colDisplay;
    }

    public static RTDimensionElement getDimensionType(String dimEleId) {
        if (dimEleId.endsWith(RTDimensionElement.SEGMENT.getColumnType())) {
            return RTDimensionElement.SEGMENT;
        } else {
            return RTDimensionElement.NONE;
        }

    }

    public static boolean isRunTimeDimension(String dimEleId) {
        if (dimEleId.endsWith(RTDimensionElement.SEGMENT.getColumnType())) {
            return true;
        } else {
            return false;
        }

    }

    public static String getOriginalColumnName(String dimEleId) {
        if (dimEleId.endsWith(RTDimensionElement.SEGMENT.getColumnType())) {
            return dimEleId.split(RTDimensionElement.SEGMENT.getColumnType())[0];
        } else {
            return dimEleId;
        }

    }
}
