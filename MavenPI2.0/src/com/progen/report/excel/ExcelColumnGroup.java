/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.excel;

import com.progen.report.ReportParameter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 *
 * @author progen
 */
public class ExcelColumnGroup implements Observer, Serializable {

    private static final long serialVersionUID = 752647115567689L;
    private List<RunTimeExcelColumn> rtColumnList = new ArrayList<RunTimeExcelColumn>();

    public ExcelColumnGroup() {
    }

    public RunTimeExcelColumn createRunTimeColumn(String columnId, String columnName) {
        RunTimeExcelColumn rtColumn = new RunTimeExcelColumn(columnId, columnName);
        rtColumnList.add(rtColumn);
        return rtColumn;
    }

    public RunTimeExcelColumn getRunTimeColumn(String columnId) {
        RunTimeExcelColumn col = null;
        for (RunTimeExcelColumn rtCol : rtColumnList) {
            if (rtCol.getMeasureId().equalsIgnoreCase(columnId)) {
                col = rtCol;
                break;
            }
        }
        return col;
    }

    public RunTimeExcelColumn getRunTimeColumn(String columnId, ReportParameter repParam) {
        RunTimeExcelColumn col = null;
        for (RunTimeExcelColumn rtCol : rtColumnList) {
            if (rtCol.getMeasureId().equalsIgnoreCase(columnId)
                    && !rtCol.defaultExcelColumnList.isEmpty()
                    && rtCol.defaultExcelColumnList.get(0).getRepParam().equals(repParam)) {
                col = rtCol;
                break;
            }
        }
        return col;
    }

    public void update(Observable o, Object arg) {
        ReportParameter repParam = (ReportParameter) o;
        for (RunTimeExcelColumn rtColumn : rtColumnList) {
            rtColumn.update(repParam);
        }
    }

    public List<ExcelColumnTransferObject> getExcelColumnsTransObject() {
        //ExcelColumnTransferObject[] excelColTrans=new ExcelColumnTransferObject[excelColumnList.size()];
        List<ExcelColumnTransferObject> excelColTrans = new ArrayList<ExcelColumnTransferObject>();

        for (RunTimeExcelColumn col : rtColumnList) {
            List<ExcelColumnTransferObject> transObj = col.getExcelColumnsTransObject();
            excelColTrans.addAll(transObj);
        }
        return excelColTrans;
    }

    public List<RunTimeExcelColumn> getRunTimeColumnList() {
        return this.rtColumnList;
    }
}
