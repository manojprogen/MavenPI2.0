/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.data;

import java.util.ArrayList;

/**
 *
 * @author arun
 */
public class SingleRowViewTableBuilder extends RowViewTableBuilder {

    public SingleRowViewTableBuilder(DataFacade facade) {
        super(facade);
        //perform sort
        this.sortRowViews();
    }

    private void sortRowViews() {
        ArrayList<String> sortColumns = new ArrayList<String>();
        char[] sortTypes = new char[facade.getViewByCount() - 1];
        char[] sortDataTypes = new char[facade.getViewByCount() - 1];
        for (int column = 0; column < facade.getViewByCount() - 1; column++) {
            sortColumns.add(facade.getDisplayColumns().get(column));
            sortTypes[column] = '0'; //asc
            sortDataTypes[column] = facade.getDataTypes().toString().charAt(0);
        }
        if (!sortColumns.isEmpty()) {
            facade.sortDataSet(sortColumns, sortTypes, sortDataTypes);
        }
    }

    @Override
    public int getFromColumn() {
        return facade.getViewByCount() - 1;
    }

    @Override
    public TableHeaderRow[] getHeaderRowData() {
        TableHeaderBuilder hdrBldr;
        if (facade.isReportCrosstab()) {
            hdrBldr = new CrosstabHeaderBuilder(facade);
            hdrBldr.setRowViewMode(TableBuilderConstants.SINGLE_ROW_VIEW_MODE);
        } else {
            hdrBldr = new NonCtHeaderBuilder(facade);
        }
        return hdrBldr.getHeaderRowData();
    }

    @Override
    protected TableSubtotalRow createSubTotalRowsForSubTotalType(String stType, int subIndex) {
        TableSubtotalRow subtotalRow = super.createSubTotalRowsForSubTotalType(stType, subIndex);
        ArrayList<Object> rowDataForSingleView = this.getSubtotalRowDataForSingleView(subtotalRow);
        subtotalRow.rowData = rowDataForSingleView;
        super.cellSpan.clear();
        return subtotalRow;
    }

    @Override
    protected TableSubtotalRow createGrandTotalRowForSubTotalType(String stType) {
        TableSubtotalRow subtotalRow = super.createGrandTotalRowForSubTotalType(stType);
        ArrayList<Object> rowDataForSingleView = this.getSubtotalRowDataForSingleView(subtotalRow);
        subtotalRow.rowData = rowDataForSingleView;
        super.cellSpan.clear();
        return subtotalRow;
    }

    private ArrayList<Object> getSubtotalRowDataForSingleView(TableSubtotalRow subtotalRow) {
        ArrayList<Object> rowDataForSingleView = new ArrayList<Object>();
//        String dimValue = "";
        StringBuilder dimValue = new StringBuilder(300);
        Object cellData;
        for (int column = 0; column < getColumnCount(); column++) {
            cellData = subtotalRow.getRowData(column);
            if (column < facade.getViewByCount()) {
                if (!subtotalRow.getRowData(column).equals("")) {
                    dimValue.append(subtotalRow.getRowData(column)).append(" ");
//                    dimValue += subtotalRow.getRowData(column) + " ";
                }
            }
            if (column == facade.getViewByCount() - 1) {
                cellData = dimValue;
            }

            rowDataForSingleView.add(cellData);
        }
        return rowDataForSingleView;
    }
}
