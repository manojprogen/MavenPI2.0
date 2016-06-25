/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.excel;

/**
 *
 * @author progen
 */
public class ExcelCellFormatTransferObject {

    private String rowViewByValues;
    private String colViewByValues;
    private StringBuilder parameter;
    private StringBuilder excelCellProps;

    public ExcelCellFormatTransferObject(String rowViewByValues, String colViewByValues, StringBuilder parameter, StringBuilder excelCellProps) {
        this.rowViewByValues = rowViewByValues;
        this.colViewByValues = colViewByValues;
        this.parameter = parameter;
        this.excelCellProps = excelCellProps;
    }

    public String getColViewByValues() {
        return colViewByValues;
    }

    public void setColViewByValues(String colViewByValues) {
        this.colViewByValues = colViewByValues;
    }

    public StringBuilder getExcelCellProps() {
        return excelCellProps;
    }

    public void setExcelCellProps(StringBuilder excelCellProps) {
        this.excelCellProps = excelCellProps;
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
}
