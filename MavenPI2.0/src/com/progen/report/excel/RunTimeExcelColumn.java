/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.excel;

import com.google.common.collect.Iterables;
import com.progen.query.RunTimeColumn;
import com.progen.report.ReportParameter;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author progen
 */
public class RunTimeExcelColumn implements RunTimeColumn {

    String measureId;
    String measureName;
    Boolean columnPersisted;
    List<ExcelColumn> excelColumnList = new ArrayList<ExcelColumn>();
    List<ExcelColumn> defaultExcelColumnList = new ArrayList<ExcelColumn>();

    public RunTimeExcelColumn() {
    }

    public RunTimeExcelColumn(String measureId, String measureName) {
        this();
        this.measureId = measureId;
        this.measureName = measureName;
    }

    public String getMeasureId() {
        return measureId;
    }

    public void setMeasureId(String measureId) {
        this.measureId = measureId;
    }

    public String getMeasureName() {
        return this.measureName;
    }

    public void setMeasureName(String measureName) {
        this.measureName = measureName;
    }

    public Boolean isColumnPersisted() {
        return columnPersisted;
    }

    public void setColumnPersisted(Boolean columnPersisted) {
        this.columnPersisted = columnPersisted;
    }

    public void addColumn(ReportParameter repParam, ArrayList data) {
        ExcelColumn col = new ExcelColumn();
        col.setData(data);
        col.setRepParam(repParam);
        this.excelColumnList.add(col);
        this.defaultExcelColumnList.add(col);
    }

    public void setData(ArrayList colData, int fromRow, int toRow) {
        ExcelColumn col = this.defaultExcelColumnList.get(0);
        col.setData(colData, fromRow, toRow);
    }

    public void setData(Object newData, int row) {
        ExcelColumn col = this.defaultExcelColumnList.get(0);
        col.setData(newData, row);
    }

    public Object getData(int row) {
        ExcelColumn col = this.defaultExcelColumnList.get(0);
        return col.getData().get(row);
    }

    public ArrayList getData() {
        ExcelColumn col = this.defaultExcelColumnList.get(0);
        return col.getData();
    }

    public Boolean isDataAvailable() {
        if (this.defaultExcelColumnList.isEmpty()) {
            return false;
        }
        return true;
    }

    public void update(ReportParameter param) {
        Iterable<ExcelColumn> matchingCells = Iterables.filter(excelColumnList, ExcelColumn.getReportParameterPredicate(param));
        this.defaultExcelColumnList.clear();

        for (ExcelColumn excelCol : matchingCells) {
            this.defaultExcelColumnList.add(excelCol);
        }
    }

    public List<ExcelColumnTransferObject> getExcelColumnsTransObject() {
        List<ExcelColumnTransferObject> excelColTrans = new ArrayList<ExcelColumnTransferObject>();
        ReportParameter repParam;
//        String rowViewBys="";
//        String colViewBys="";
        StringBuilder rowViewBys = new StringBuilder(400);
        StringBuilder colViewBys = new StringBuilder(400);
        StringBuilder paramXml;
        StringBuilder colXml;

        for (ExcelColumn col : excelColumnList) {
            repParam = col.getRepParam();
            int ct = -1;
            for (String rowView : repParam.getRowViewByForParameter()) {
//                rowViewBys+=rowView;
                rowViewBys.append(rowView);
                ct++;
                if (ct != repParam.getRowViewByForParameter().size() - 1) {
//                    rowViewBys+=",";
                    rowViewBys.append(",");
                }
            }

            ct = -1;
            for (String colView : repParam.getColViewByForParameter()) {
//                colViewBys+=colView;
                colViewBys.append(colView);
                ct++;
                if (ct != repParam.getColViewByForParameter().size() - 1) {
//                colViewBys+=",";
                    colViewBys.append(",");
                }
            }

            colXml = col.toXml();
            paramXml = repParam.toXml();
            if (colXml != null) {
                excelColTrans.add(new ExcelColumnTransferObject(rowViewBys.toString(), colViewBys.toString(), paramXml, this.measureId, colXml));
            }
//            rowViewBys="";
//            colViewBys="";
        }

        return excelColTrans;
    }
}
