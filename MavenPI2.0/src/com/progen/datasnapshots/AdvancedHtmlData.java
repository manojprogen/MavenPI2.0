/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.datasnapshots;

import com.progen.db.ProgenDataSet;
import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author progen
 */
public class AdvancedHtmlData implements Serializable {

    private ProgenDataSet returnObject;
    private ArrayList<String> columnIds;
    private ArrayList columnNames;
    private ArrayList<String> dimensionIds;
    private ArrayList dimensionNames;
    private ArrayList<String> measureIds;
    private ArrayList measureNames;
    private static final long serialVersionUID = 227666805307498765L;

    public ProgenDataSet getReturnObject() {
        return returnObject;
    }

    public void setReturnObject(ProgenDataSet returnObject) {
        this.returnObject = returnObject;
    }

    public ArrayList<String> getColumnIds() {
        return columnIds;
    }

    public void setColumnIds(ArrayList<String> columnIds) {
        this.columnIds = columnIds;
    }

    public ArrayList getColumnNames() {
        return columnNames;
    }

    public void setColumnNames(ArrayList columnNames) {
        this.columnNames = columnNames;
    }

    public ArrayList<String> getDimensionIds() {
        return dimensionIds;
    }

    public void setDimensionIds(ArrayList<String> dimensionIds) {
        this.dimensionIds = dimensionIds;
    }

    public ArrayList getDimensionNames() {
        return dimensionNames;
    }

    public void setDimensionNames(ArrayList dimensionNames) {
        this.dimensionNames = dimensionNames;
    }

    public ArrayList<String> getMeasureIds() {
        return measureIds;
    }

    public void setMeasureIds(ArrayList<String> measureIds) {
        this.measureIds = measureIds;
    }

    public ArrayList getMeasureNames() {
        return measureNames;
    }

    public void setMeasureNames(ArrayList measureNames) {
        this.measureNames = measureNames;
    }
}
