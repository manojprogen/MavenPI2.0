/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.excel;

import com.google.common.collect.Iterables;
import com.progen.report.ReportParameter;
import java.io.Serializable;
import java.util.*;

/**
 *
 * @author progen
 */
public class ExcelCellFormatGroup implements Observer, Serializable {

    private static final long serialVersionUID = 753264711556228L;
    private List<ExcelCellFormat> excelCellList = new ArrayList<ExcelCellFormat>();
    private List<ExcelCellFormat> defaultExcelProps = new ArrayList<ExcelCellFormat>();

    public ExcelCellFormat createExcelCell(int rowNum, String measId, ReportParameter repParam) {
        ExcelCellFormat excelCell = new ExcelCellFormat(rowNum, measId, repParam);

        int index = excelCellList.indexOf(excelCell);

        if (index > -1) {
            excelCell = excelCellList.get(index);
        } else {
            excelCellList.add(excelCell);
            defaultExcelProps.add(excelCell);
        }
        return excelCell;
    }

    public ExcelCellFormat getExcelCell(int rowNum, String measId, ReportParameter repParam) {
        ExcelCellFormat excelCell = new ExcelCellFormat(rowNum, measId, repParam);

        int index = defaultExcelProps.indexOf(excelCell);

        if (index > -1) {
            return defaultExcelProps.get(index);
        } else {
            return null;
        }
    }

    public void update(Observable o, Object arg) {
        Iterable<ExcelCellFormat> matchingCells = Iterables.filter(excelCellList, ExcelCellFormat.getReportParameterPredicate((ReportParameter) o));
        this.defaultExcelProps.clear();

        for (ExcelCellFormat colorCode : matchingCells) {
            this.defaultExcelProps.add(colorCode);
        }
    }

    public ExcelCellFormatTransferObject[] getExcelCellsTransObject() {
        ExcelCellFormatTransferObject[] excelCellTrans = new ExcelCellFormatTransferObject[excelCellList.size()];
        ReportParameter repParam;
//        String rowViewBys="";
//        String colViewBys="";
        StringBuilder rowViewBys = new StringBuilder(400);
        StringBuilder colViewBys = new StringBuilder(400);
        StringBuilder paramXml;
        StringBuilder cellXml;
        ExcelCellFormatTransferObject excelCellTransObj = null;
        int index = 0;
        for (ExcelCellFormat cell : excelCellList) {
            repParam = cell.getRepParam();
            int ct = -1;
            for (String rowView : repParam.getRowViewByForParameter()) {
//                rowViewBys+=rowView;
                rowViewBys.append(rowView);
                ct++;
                if (ct != repParam.getRowViewByForParameter().size() - 1) {
//                rowViewBys+=",";
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
            cellXml = cell.toXml();
            paramXml = repParam.toXml();

            if (cellXml != null) {
                excelCellTrans[index] = new ExcelCellFormatTransferObject(rowViewBys.toString(), colViewBys.toString(), paramXml, cellXml);
                index++;
            }
//            rowViewBys="";
//            colViewBys="";
        }
        return excelCellTrans;
    }

    public ExcelCellFormat createExcelCell(ReportParameter repParam, Map<String, String> cellHM) {
        ExcelCellFormat cell = new ExcelCellFormat();
        cell.setRepParam(repParam);
        cell.setRowNum(Integer.parseInt((String) cellHM.get("Row")));
        cell.setMeasureId(cellHM.get("Measure"));
        cell.setComment(cellHM.get("Comment"));
        cell.setBgColor(cellHM.get("BgColor"));
        cell.setFontColor(cellHM.get("FontColor"));

        excelCellList.add(cell);
        return cell;
    }
}
