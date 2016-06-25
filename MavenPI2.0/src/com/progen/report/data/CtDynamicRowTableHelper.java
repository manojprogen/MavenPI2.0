/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.data;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import prg.db.ContainerConstants;

/**
 *
 * @author progen
 */
public class CtDynamicRowTableHelper {

    DataFacade facade;

    public CtDynamicRowTableHelper(DataFacade facade) {
        this.facade = facade;
    }

    public TableDataRow createDynamicRow(TableDataRow dataRow) {
        TableDataRow calculatedRow = new TableDataRow();
        ArrayList<String> anchors = new ArrayList<String>();
        ArrayList<String> colorList = new ArrayList<String>();
        ArrayList<String> fontColorLst = new ArrayList<String>();
        ArrayList<Boolean> editableList = new ArrayList<Boolean>();
        ArrayList<String> commentList = new ArrayList<String>();


        calculatedRow = (TableDataRow) formulateDynamicRow(dataRow);
        for (int column = 0; column < facade.getColumnCount(); column++) {
            anchors.add("#");
            colorList.add("");
            fontColorLst.add("");
            commentList.add("");
            editableList.add(false);
        }

        calculatedRow.setAnchors(anchors);
        calculatedRow.setColorList(colorList);
        calculatedRow.setFontColors(fontColorLst);
        calculatedRow.printSubTotals = dataRow.printSubTotals;
        calculatedRow.setRowNumber(dataRow.getRowNumber());

        calculatedRow.setCommentList(commentList);
        calculatedRow.setEditableList(editableList);
        return calculatedRow;
    }

    public TableSubtotalRow createDynamicSubtotalRow(TableSubtotalRow subtotalRow) {
        TableSubtotalRow calculatedRow = new TableSubtotalRow();

        calculatedRow = (TableSubtotalRow) formulateDynamicRow(subtotalRow);

        return calculatedRow;
    }

    private TableDataRow formulateDynamicRow(TableDataRow row) {
        TableDataRow dynamicRow;
        if (row instanceof TableSubtotalRow) {
            dynamicRow = new TableSubtotalRow();
        } else {
            dynamicRow = new TableDataRow();
        }

        ArrayList<Object> rowData = new ArrayList<Object>();
        ArrayList<String> colorList = new ArrayList<String>();
        ArrayList<Boolean> displayStyle = new ArrayList<Boolean>();
        int grandTotalColumn;
        int currentMeasure;
        int measCount = facade.getReportMeasureCount();
        BigDecimal cellValue;
        BigDecimal baseMeasValue;
        BigDecimal grandTotal;

        if (facade.getGrandTotalDisplayPosition().equals(ContainerConstants.CROSSTAB_GRANDTOTAL_LAST)) {
            currentMeasure = measCount;
        } else {
            currentMeasure = facade.getViewByCount();
        }

        for (int column = 0; column < facade.getColumnCount(); column++) {

            colorList.add("");
            displayStyle.add(row.isDisplayed.get(column));

            if (column < facade.getViewByCount()) {
                if ("".equals(row.rowData.get(column))) {
                    rowData.add("");
                } else {
                    rowData.add(row.rowData.get(column) + " (% wise)");
                }
            } else {
                if (facade.getGrandTotalDisplayPosition().equals(ContainerConstants.CROSSTAB_GRANDTOTAL_LAST)) {
                    grandTotalColumn = facade.getColumnCount() - currentMeasure;

                    if (row instanceof TableDataRow) {
                        cellValue = facade.getMeasureData(((TableDataRow) row).getRowNumber(), facade.getColumnId(grandTotalColumn));
                    } else {
                        cellValue = ((TableSubtotalRow) row).getRawNumericData(grandTotalColumn);
                    }

                    if (cellValue == null) {
                        grandTotal = BigDecimal.ZERO;
                    } else {
                        grandTotal = cellValue;
                    }
                    currentMeasure--;
                    if (currentMeasure == 0) {
                        currentMeasure = measCount;
                    }

                } else {
                    grandTotalColumn = currentMeasure;

                    if (row instanceof TableDataRow) {
                        cellValue = facade.getMeasureData(((TableDataRow) row).getRowNumber(), facade.getColumnId(grandTotalColumn));
                    } else {
                        cellValue = ((TableSubtotalRow) row).getRawNumericData(grandTotalColumn);
                    }


                    if (cellValue == null) {
                        grandTotal = BigDecimal.ZERO;
                    } else {
                        grandTotal = cellValue;
                    }
                    currentMeasure++;
                    if (currentMeasure == (measCount + facade.getViewByCount())) {
                        currentMeasure = facade.getViewByCount();
                    }
                }

                if (row instanceof TableDataRow) {
                    baseMeasValue = facade.getMeasureData(((TableDataRow) row).getRowNumber(), facade.getColumnId(column));
                } else {
                    baseMeasValue = ((TableSubtotalRow) row).getRawNumericData(column);
                }

                if (baseMeasValue == null) {
                    baseMeasValue = BigDecimal.ZERO;
                }

                if (!grandTotal.equals(BigDecimal.ZERO)) {
                    cellValue = baseMeasValue.divide(grandTotal, MathContext.DECIMAL64);
                    cellValue = cellValue.multiply(BigDecimal.TEN);
                    cellValue = cellValue.multiply(BigDecimal.TEN);
                } else {
                    cellValue = BigDecimal.ZERO;
                }
                rowData.add(facade.formatMeasureData(cellValue, "%"));
            }
            dynamicRow.setRowDataIds(row.rowDataIds);
            dynamicRow.setRowData(rowData);
            dynamicRow.setDisplayStyle(row.isDisplayed);
            dynamicRow.setCellSpans(row.spans);
            dynamicRow.setCssClass(row.cssClass);

        }

        return dynamicRow;
    }
}
