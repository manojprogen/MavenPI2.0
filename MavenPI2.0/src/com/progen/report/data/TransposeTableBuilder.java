/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.data;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.progen.report.display.table.TableCellSpan;
import com.progen.report.display.util.NumberFormatter;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import prg.db.PbReturnObject;

/**
 *
 * @author arun
 */
public class TransposeTableBuilder extends TableBuilder {

    public TransposeTableBuilder(DataFacade facade) {
        super(facade);
    }

    @Override
    public TableHeaderRow[] getHeaderRowData() {
        ArrayList<TableHeaderRow> headerRowLst = new ArrayList<TableHeaderRow>();
        TableHeaderRow headerRow;
        ArrayList<Boolean> isDisplayed;
        TableCellSpan headerCellSpan;
        ArrayList<String> columnHeadings = new ArrayList<String>();
        ArrayList<String> displayImageList = new ArrayList<String>();
        ArrayList<String> rowDataIds = new ArrayList<String>();

        ArrayList<String> displayCols = facade.getDisplayColumns();

        headerRow = new TableHeaderRow();
        headerRow.setLayerNumber(0);
        isDisplayed = new ArrayList<Boolean>();

        if (facade.getViewByCount() == 1) {
            for (int column = 0; column < facade.getColumnLayerCount(); column++) {
                if (column == facade.getColumnLayerCount() - 1) {
                    columnHeadings.add("Measure");
                } else {
                    columnHeadings.add(facade.getColumnEdgeLabel(column));
                }
                displayImageList.add("");
                isDisplayed.add(Boolean.TRUE);
                rowDataIds.add("A_COL_" + column);
                //
            }
        }
        if (facade.container.isSplitBy()) {
            String groupHeader = "TIME_1";
            String prevGroupHeader = null;
            int row = 0;
            for (row = 0; row < facade.getRowCount(); row++) {

                String currGroupHeader = facade.getDimensionData(row, groupHeader);
                if (prevGroupHeader != null && !prevGroupHeader.equalsIgnoreCase(currGroupHeader)) {
                    columnHeadings.add(prevGroupHeader);
                    displayImageList.add("");
                    isDisplayed.add(Boolean.TRUE);
                    rowDataIds.add("A_DDIM_" + row);
                    prevGroupHeader = currGroupHeader;
                    facade.increseColCount(1);
                }
                if (prevGroupHeader == null) {
                    prevGroupHeader = facade.getDimensionData(row, groupHeader);
                    facade.setrunTimeColCount(0);
                }
                columnHeadings.add(facade.getDimensionData(row, displayCols.get(0)));

                displayImageList.add("");
                isDisplayed.add(Boolean.TRUE);
                rowDataIds.add("A_DIM_" + row);
            }
            if (prevGroupHeader != null) {
                columnHeadings.add(prevGroupHeader);
                displayImageList.add("");
                isDisplayed.add(Boolean.TRUE);
                rowDataIds.add("A_DDIM_" + row);
                facade.increseColCount(1);
            }
        } else {      //start of code by mayank;
            for (int row1 = 0; row1 <= facade.getViewByCount() - 1; row1++) {
                if (row1 == 0 && facade.getViewByCount() > 1) {
                    columnHeadings.add("Measure");
                } else if (row1 >= 1) {
                    //
                    columnHeadings.add("");
                } else {
                }
                for (int row = 0; row < facade.getRowCount(); row++) {   //headerCellSpan = new TableCellSpan(facade.getDimensionData(row, displayCols.get(row)));
                    // headerCellSpan.setRowSpan(2=

                    columnHeadings.add(facade.getDimensionData(row, displayCols.get(row1)));

                    displayImageList.add("");
                    isDisplayed.add(Boolean.TRUE);
                    rowDataIds.add("A_DIM_" + row);
                }
            }
        }
        headerRow.setRowDataIds(rowDataIds);
        headerRow.setRowData(columnHeadings);
        headerRow.setSortImagePath(displayImageList);
        headerRow.setDisplayStyle(isDisplayed);
        headerRowLst.add(headerRow);
        return headerRowLst.toArray(new TableHeaderRow[]{});
    }
//end of code by mayank

    @Override
    public TableMenuRow getMenuRowData() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public TableSearchRow getSearchData() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public TableDataRow getRowData(int row) {
        TableDataRow dataRow = new TableDataRow();
        ArrayList<String> displayCols = facade.getDisplayColumns();
        ArrayList<String> measLst = facade.getTableDisplayMeasures();
        ArrayList<String> measureNames = facade.getReportMeasureNames();
        Object cellData;
        boolean isDimension;
        String css;
        int measRow;
        String rowDataId;
        ArrayList<Boolean> displayStyle = new ArrayList<Boolean>();
        ArrayList<Object> rowData = new ArrayList<Object>();
        ArrayList<String> colorList = new ArrayList<String>();
        ArrayList<String> cssClass = new ArrayList<String>();
        ArrayList<String> anchors = new ArrayList<String>();
        ArrayList<String> rowDataIds = new ArrayList<String>();
        ArrayList<String> fontColorList = new ArrayList<String>();
        ArrayList<BigDecimal> measData = new ArrayList<BigDecimal>();
        ArrayList<Boolean> editableList = new ArrayList<Boolean>();
        //added by anitha
        ArrayList<String> reportDrillList = new ArrayList<String>();
        HashMap<String, String> finalCrossTabReportDrillMap = ((PbReturnObject) facade.container.getRetObj()).finalCrossTabReportDrillMap;
        HashMap<String, String> crosstabMeasureId = ((PbReturnObject) facade.container.getRetObj()).crosstabMeasureId;
        int totalColCount = this.getColumnCount();
        TableHeaderRow[] headerRows;
        headerRows = this.getHeaderRowData();
        Type tarType = new TypeToken<List<String>>() {
        }.getType();
        //end of code by anitha
        if (facade.container.isSplitBy()) {
            totalColCount += facade.getIncreaseColCount();
        }
        for (int column = 0; column < totalColCount; column++) {
            Boolean editable = false;
            String reportDrillUrl = "";
            String finalrepDrillUrl = facade.getContextPath() + "/reportViewer.do?reportBy=viewReport";
            if (column < this.getViewByCount()) {
                isDimension = true;
            } else {
                isDimension = false;
            }

            if (isDimension) {
                cellData = measureNames.get(row);
                css = "dimensionCell1";
                rowDataId = measLst.get(row);
                if (facade.container.isSplitBy()) {
                    Object aggType = facade.getAggregationType(rowDataId);
                    if (aggType != null) {
                        measData = facade.retrieveNumericDataForMultiTime(rowDataId, "TIME_1", aggType.toString());
                    }


                } else {
                    measData = facade.retrieveMeasureData(rowDataId);
                }
            } else {
                measRow = column - this.getViewByCount();
                BigDecimal data = measData.get(measRow);
                cellData = NumberFormatter.getModifiedNumber(data, "", -1);
                css = "measureNumericCell";
                rowDataId = measLst.get(row) + "_" + column;
            }
            editable = facade.isEditable(rowDataId);
            displayStyle.add(facade.isColumnVisible(measLst.get(row)));
            anchors.add("#");
            colorList.add("");
            fontColorList.add("");
            rowData.add(cellData);
            cssClass.add(css);
            rowDataIds.add(rowDataId);
            editableList.add(editable);
            //added by anitha
            Gson gson = new Gson();
            if (column == 0) {
                reportDrillList.add(" ");
            } else {
                finalrepDrillUrl += "&REPORTID=" + facade.container.getReportDrillMap(measLst.get(row));
                List cellDataList = new ArrayList();
                String cellData1 = headerRows[0].getRowData(column);
                cellDataList.add(cellData1);
                reportDrillUrl += "&CBOARP" + displayCols.get(0).replace("A_", "") + "=" + java.net.URLEncoder.encode(gson.toJson(cellDataList, tarType));
                finalrepDrillUrl += reportDrillUrl + "&reportDrill=Y";
                if (facade.container.isReportCrosstab()) {

                    if (!finalCrossTabReportDrillMap.containsKey(measLst.get(row))) {
                        reportDrillList.add("#");
                    } else if (finalCrossTabReportDrillMap.get(measLst.get(row)) != null && finalCrossTabReportDrillMap.get(measLst.get(row)).equalsIgnoreCase("0")) {
                        reportDrillList.add("#");
                    } else {
                        reportDrillList.add(finalrepDrillUrl);
                    }
                } else {
                    if (!facade.container.getReportCollect().reportDrillMap.containsKey(measLst.get(row))) {
                        reportDrillList.add("#");
                    } else if (facade.container.getReportDrillMap(measLst.get(row)) != null && facade.container.getReportDrillMap(measLst.get(row)).equalsIgnoreCase("0")) {
                        reportDrillList.add("#");
                    } else {
                        reportDrillList.add(finalrepDrillUrl);
                    }
                }
                //end of code by anitha
            }

        }
        dataRow.setAnchors(anchors);
        dataRow.setColorList(colorList);
        dataRow.setCssClass(cssClass);
        dataRow.setRowData(rowData);
        dataRow.setRowDataIds(rowDataIds);
        dataRow.setFontColors(fontColorList);
        dataRow.setEditableList(editableList);
        //added by anitha
        dataRow.setReportDrillList(reportDrillList);
        dataRow.setDisplayStyle(displayStyle);
        //end of code by anitha
        //initalize graph data set
//        if ( row == 0 )
//        {
//            for ( int i=0; i<facade.getRowCount(); i++ )
//                facade.initializeGraphDataSet(i);
//        }
        return dataRow;
    }

    @Override
    public TableSubtotalRow[] getSubtotalRowData() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public TableSubtotalRow[] getGrandtotalRowData() {
        //code added by anitha for GT in transpose
        ArrayList<TableSubtotalRow> subTotalRowLst = new ArrayList<TableSubtotalRow>();
        TableDataRow dataRow = new TableDataRow();
        ArrayList<Object> rowData = new ArrayList<Object>();
        if (facade.getRowCount() > 0) {
            if (facade.isTopBottomWithOthersEnable()) {
                subTotalRowLst.add(createGrandTotalRowForSubTotalType("TOPOTHERS"));
            }
            if (facade.isGrandTotalRequired()) {
                subTotalRowLst.add(createGrandTotalRowForSubTotalType("GT"));
            } else {
                createGrandTotalRowForSubTotalType("GT");
            }
        }
        return (TableSubtotalRow[]) subTotalRowLst.toArray(new TableSubtotalRow[]{});
    }

    @Override
    public TableSubtotalRow[] getSubtotalRowDataLastRow() {
//        throw new UnsupportedOperationException("Not supported yet.");
        return null;
    }

    @Override
    public int getColumnCount() {
        return facade.getRowCount() + facade.getColumnLayerCount();
    }

    @Override
    public int getViewByCount() {
        return facade.getColumnLayerCount();
    }

    @Override
    public int getFromColumn() {
        return 0;
    }

    @Override
    public int getFromRow() {
        return 0;
    }

    @Override
    public int getToRow() {
        return facade.getTableDisplayMeasures().size();
    }

    @Override
    public TableDataRow getRowData1(int rowNum) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
