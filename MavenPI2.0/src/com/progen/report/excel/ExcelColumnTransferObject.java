/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.excel;

/**
 *
 * @author progen
 */
public class ExcelColumnTransferObject {

    private String rowViewByValues;
    private String colViewByValues;
    private StringBuilder parameter;
    private String measureId;
    private StringBuilder excelColumnData;

    public ExcelColumnTransferObject() {
    }

    public ExcelColumnTransferObject(String rowViewByValues, String colViewByValues, StringBuilder parameter, String measId, StringBuilder excelColumnData) {
        this.rowViewByValues = rowViewByValues;
        this.colViewByValues = colViewByValues;
        this.parameter = parameter;
        this.measureId = measId;
        this.excelColumnData = excelColumnData;
    }

    public String getColViewByValues() {
        return colViewByValues;
    }

    public void setColViewByValues(String colViewByValues) {
        this.colViewByValues = colViewByValues;
    }

    public StringBuilder getExcelColumnData() {
        return excelColumnData;
    }

    public void setExcelColumnData(StringBuilder excelColumnData) {
        this.excelColumnData = excelColumnData;
    }

    public StringBuilder getParameter() {
        return parameter;
    }

    public void setParameter(StringBuilder parameter) {
        this.parameter = parameter;
    }

    public String getRowViewByValues() {
        return rowViewByValues;
    }

    public void setRowViewByValues(String rowViewByValues) {
        this.rowViewByValues = rowViewByValues;
    }

    public String getMeasureId() {
        return measureId;
    }

    public void setMeasureId(String measureId) {
        this.measureId = measureId;
    }
}
