/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.data;

import com.google.common.collect.ArrayListMultimap;
import com.progen.query.RTMeasureElement;
import static com.progen.report.data.TableBuilderConstants.SINGLE_ROW_VIEW_MODE;
import com.progen.report.display.table.TableCellSpan;
import com.progen.reportview.bd.PbReportViewerBD;
import java.util.*;
import prg.db.ContainerConstants;
import prg.db.PbReturnObject;

/**
 *
 * @author arun
 */
public class CrosstabHeaderBuilder extends TableHeaderBuilder {

    public CrosstabHeaderBuilder(DataFacade facade) {
        super(facade);
    }

    @Override
    public TableHeaderRow[] getHeaderRowData() {
        TableHeaderRow[] headerRow;
        headerRow = getHeaderRowDataBasedOnRowViewMode(rowViewMode);
        if (facade.isCrosstabHeaderWrapped() && headerRow.length > 2) {
            headerRow = wrapHeaderRows(headerRow);
        }
        return headerRow;
    }

    private TableHeaderRow[] wrapHeaderRows(TableHeaderRow[] headerRow) {
        // 
        TableHeaderRow[] wrappedHeader = new TableHeaderRow[2];
        int columnLayerCount = headerRow.length;
        // 
        ArrayList<String> columnHeadings = new ArrayList<String>();
        TableCellSpan headerCellSpan;
        HashSet<TableCellSpan> cellSpans = new HashSet<TableCellSpan>();
//        String heading;
        StringBuilder heading;
        String[] layerHeadings = new String[columnLayerCount - 1];

        wrappedHeader[1] = headerRow[columnLayerCount - 1];
        // 

        for (int column = 0; column < facade.getColumnCount(); column++) {
            headerCellSpan = new TableCellSpan(facade.getDisplayColumns().get(column) + "_" + 0 + "_H");
            if (column < facade.getViewByCount()) {
                columnHeadings.add("");
            } else {

                for (int layer = 0; layer < columnLayerCount - 1; layer++) {

                    if (headerRow[layer].isDisplayed.get(column) == true) {
                        layerHeadings[layer] = headerRow[layer].rowData.get(column);
                        if (layerHeadings[layer].equalsIgnoreCase("Grand Total")) {
                            layerHeadings[layer] = "All";
                        } else if (layerHeadings[layer].equalsIgnoreCase("Sub Total")) {
                            layerHeadings[layer] = "";
                        } else if (layerHeadings[layer].trim().equals("")) {
                            layerHeadings[layer] = "";
                        }
                    }
                }
                heading = new StringBuilder(1000);
                for (int layer = 0; layer < columnLayerCount - 1; layer++) {
                    if (!layerHeadings[layer].equalsIgnoreCase("")) //                        heading =  heading + " - " + layerHeadings[layer];
                    {
                        heading.append(" - ").append(layerHeadings[layer]);
                    }
                }
//                heading = heading.substring(3);
                heading = new StringBuilder(heading.substring(3));
                columnHeadings.add(heading.toString());
            }
            headerCellSpan.setRowSpan(1);
            headerCellSpan.setColumnSpan(headerRow[columnLayerCount - 2].getColumnSpan(column));
            cellSpans.add(headerCellSpan);
        }
        // 
        wrappedHeader[0] = new TableHeaderRow();
        wrappedHeader[0].rowDataIds = headerRow[columnLayerCount - 2].rowDataIds;
        wrappedHeader[0].setLayerNumber(0);
        wrappedHeader[0].setRowData(columnHeadings);
        wrappedHeader[0].setSortImagePath(headerRow[columnLayerCount - 2].sortImagePath);
        wrappedHeader[0].setDisplayStyle(headerRow[columnLayerCount - 2].isDisplayed);
        wrappedHeader[0].setCellSpans(cellSpans);

        return wrappedHeader;
    }

    private TableHeaderRow[] getHeaderRowDataBasedOnRowViewMode(String rowViewMode) {
        // 
        ArrayList<TableHeaderRow> headerRowLst = new ArrayList<TableHeaderRow>();
        TableHeaderRow row;
        TableCellSpan headerCellSpan;
        ArrayList<Boolean> isDisplayed;

        int columnLayerCount = 0;
        int rowLayerCount;
        ArrayList<String> columnHeadings = new ArrayList<String>();
        ArrayList<String> displayImageList = new ArrayList<String>();
        ArrayList<String> hiddenMeasureId = new ArrayList<String>();
        Map<String, String> crosstabMeasureId = ((PbReturnObject) facade.container.getRetObj()).crosstabMeasureId;
        String hiddenMeasureName = "";
        Map<String, String> mapValue = new LinkedHashMap<String, String>();
        ArrayList displayLabels = facade.getDisplayLabels();
        ArrayList<String> displayGraphLabels = new ArrayList<>();
        int measurePosition = facade.getMeasurePosition();
        ArrayListMultimap<Integer, Integer> crosstabColSpans = null;
        List<Integer> colLayerSpanLst;
        int nextColumnToConsider;
        int colSpanIndex;
        HashSet<TableCellSpan> cellSpans;
        int ctColumnCount = 0;
        int rtMeasCount = 0;
        int tblMeasCount = 0;
        int ctDynamicColumnCount = 0;
        int j = 0;

        int hiddenMeas = 0;
        int reportMeasCount = facade.getReportMeasureCount();
        crosstabColSpans = facade.getCrosstabColumnSpan();
        columnLayerCount = crosstabColSpans.keySet().size();
        PbReportViewerBD viewerbd = new PbReportViewerBD();
        ArrayList<String> tblMeasures = facade.getTableDisplayMeasures();
        String GTPos = null;
        if (facade.container.getCrosstabGrandTotalDisplayPosition().equalsIgnoreCase(ContainerConstants.CROSSTAB_GRANDTOTAL_FIRST)) {
            GTPos = "first";
        } else if (facade.container.getCrosstabGrandTotalDisplayPosition().equalsIgnoreCase(ContainerConstants.CROSSTAB_GRANDTOTAL_LAST)) {
            GTPos = "Last";
        } else if (facade.container.getCrosstabGrandTotalDisplayPosition().equalsIgnoreCase(ContainerConstants.CROSSTAB_GRANDTOTAL_NONE)) {
            GTPos = "none";
        }
        for (String measure : tblMeasures) {
            if (RTMeasureElement.isRunTimeMeasure(measure)) {
                rtMeasCount++;
            } else {
                tblMeasCount++;
            }
        }
        // code Added By Amar
        int summCount = 0;
        if (facade.container.isSummarizedMeasuresEnabled()) {
            if (facade.container.getSummerizedTableHashMap() != null && !facade.container.getSummerizedTableHashMap().isEmpty()) {
                summCount = facade.container.getSummerizedTableHashMap().get("summerizedQryeIds").size();

            }
        }
        //code modified by Bhargavi
        for (int i = 0, col = facade.getViewByCount(); i < reportMeasCount + summCount; col++, i++) {
            if (!facade.isColumnVisible(facade.getColumnId(col))) {
                hiddenMeas++;
            }
        }
        // added by Mayank    
        hiddenMeasureId = facade.container.getReportCollect().getHideMeasures();
        if (hiddenMeasureId != null && hiddenMeasureId.size() != 0 && !hiddenMeasureId.get(0).isEmpty()) {
            for (int measId = 0; measId < hiddenMeasureId.size(); measId++) {
                hiddenMeasureName = facade.container.getMeasureName(hiddenMeasureId.get(measId));
                if (hiddenMeasureName != null) {
                    if (hiddenMeasureName.contains("Prev Month")) {
                        if (!mapValue.containsKey(hiddenMeasureName.trim())) {
                            mapValue.put(hiddenMeasureName.trim(), facade.container.getMonthNameforTrailingFormulaOnColName(hiddenMeasureName));
                            displayGraphLabels.add(hiddenMeasureName.trim());
                        }
                    } else if (hiddenMeasureName.contains("Prev Year")) {
                        if (!mapValue.containsKey(hiddenMeasureName.trim())) {
                            mapValue.put(hiddenMeasureName.trim(), facade.container.getYearNameforTrailingFormulaOnColName(hiddenMeasureName));
                            displayGraphLabels.add(hiddenMeasureName.trim());
                        }
                    } else if (hiddenMeasureName.contains("Prev Day")) {
                        if (!mapValue.containsKey(hiddenMeasureName.trim())) {
                            mapValue.put(hiddenMeasureName.trim(), facade.container.getDayNameforTrailingFormulaOnColName(hiddenMeasureName));
                            displayGraphLabels.add(hiddenMeasureName.trim());
                        }
                    } else if (hiddenMeasureName.contains("Prev Quarter")) {
                        if (!mapValue.containsKey(hiddenMeasureName.trim())) {
                            mapValue.put(hiddenMeasureName.trim(), facade.container.getQuaterNameforTrailingFormulaOnColName(hiddenMeasureName).trim());
                            displayGraphLabels.add(hiddenMeasureName.trim());
                        }
                    } else if (hiddenMeasureName.contains("YTD") || hiddenMeasureName.contains("CYD") || hiddenMeasureName.contains("QTD") || hiddenMeasureName.contains("MTD")) {
                        if (!mapValue.containsKey(hiddenMeasureName.trim())) {
                            mapValue.put(hiddenMeasureName.trim(), facade.container.getQuaterNameforTimeFormulaOnColName(hiddenMeasureName));
                            displayGraphLabels.add(hiddenMeasureName.trim());
                        }
                    } else if (hiddenMeasureName.contains("Prev Month")) {
                        if (!mapValue.containsKey(hiddenMeasureName.trim())) {
                            mapValue.put(hiddenMeasureName.trim(), facade.container.getMonthNameforTrailingFormulaOnColName(hiddenMeasureName));
                            displayGraphLabels.add(hiddenMeasureName.trim());
                        }
                    }
                }
            }
        }
//        for ( int i=0,col=facade.getViewByCount(); i<reportMeasCount; col++,i++ )
//        {
//            if ( ! facade.isColumnVisible(facade.getColumnId(col)) )
//                hiddenMeas++;
//        }
        //end of code
//        ctColumnCount = tblMeasCount + rtMeasCount;// - hiddenMeas;
        ctDynamicColumnCount = rtMeasCount - hiddenMeas;
        rowLayerCount = facade.getViewByCount();

        for (int colLayer = 0; colLayer < columnLayerCount; colLayer++) {
            nextColumnToConsider = facade.getViewByCount();
            //   
            colSpanIndex = 0;
            row = new TableHeaderRow();
            //   
            isDisplayed = new ArrayList<Boolean>();
            cellSpans = new HashSet<TableCellSpan>();
            columnHeadings = new ArrayList<String>();
            displayImageList = new ArrayList<String>();
            row.setLayerNumber(colLayer);
            for (int column = 0; column < facade.getColumnCount(); column++) {
                if (column == 0) {
                    j = 0;
                }
                headerCellSpan = new TableCellSpan(facade.getDisplayColumns().get(column) + "_" + colLayer + "_H");
                if (!facade.isColumnVisible(facade.getDisplayColumns().get(column))) {
                    isDisplayed.add(Boolean.FALSE);
                    headerCellSpan.setColumnSpan(0);
                    headerCellSpan.setRowSpan(0);
                    columnHeadings.add("");
                    displayImageList.add("");
                    nextColumnToConsider++;
                } else if (colLayer < columnLayerCount) {
                    if (rowViewMode.equals(SINGLE_ROW_VIEW_MODE)) {
                        if (column < facade.getViewByCount() - 1) {
                            isDisplayed.add(Boolean.FALSE);
                            headerCellSpan.setColumnSpan(0);
                            headerCellSpan.setRowSpan(1);
                            columnHeadings.add("");
                            displayImageList.add("");
                        } else if (column == facade.getViewByCount() - 1) {
                            headerCellSpan.setColumnSpan(1);
                            headerCellSpan.setRowSpan(1);
                            columnHeadings.add(facade.getColumnEdgeLabel(colLayer));
                            displayImageList.add("");
                            isDisplayed.add(Boolean.TRUE);
                        }
                    } else {
                        if (column == 0) {
                            if (measurePosition == columnLayerCount - 1) {
                                if (colLayer < columnLayerCount - 1) {
                                    headerCellSpan.setColumnSpan(rowLayerCount);    // for col view by name
                                    headerCellSpan.setRowSpan(1);
                                    columnHeadings.add(facade.getColumnEdgeLabel(colLayer));
                                    displayImageList.add("");
                                    isDisplayed.add(Boolean.TRUE);
                                    ctColumnCount++;
                                } else {
                                    isDisplayed.add(facade.isColumnVisible(facade.getColumnId(column)));   // for row view  by name
                                    headerCellSpan.setColumnSpan(1);
                                    headerCellSpan.setRowSpan(1);
                                    //Modified, start of code By nazneen
//                                    if (column < facade.getViewByCount()) {
//                                        columnHeadings.add(displayLabels.get(column).toString());
//                                    } else {
//                                        columnHeadings.add(((ArrayList) displayLabels.get(column)).get(colLayer).toString());
//                                    }
                                    String tempDispLbl = "";
                                    if (column < facade.getViewByCount()) {
                                        tempDispLbl = displayLabels.get(column).toString();
                                    } else {
                                        tempDispLbl = ((ArrayList) displayLabels.get(column)).get(colLayer).toString();
                                    }
                                    if (tempDispLbl.contains("Prev Month")) {
                                        columnHeadings.add(facade.container.getMonthNameforTrailingFormulaOnColName(tempDispLbl));
                                    } else {
                                        columnHeadings.add(tempDispLbl);
                                    }
                                    //Modified, End of code By nazneen
                                    displayImageList.add(facade.getSortImageStyle(facade.getColumnId(column)));
                                }
                            } else {
                                if (colLayer < columnLayerCount - 1 && colLayer == measurePosition) {
                                    isDisplayed.add(facade.isColumnVisible(facade.getColumnId(column)));
                                    headerCellSpan.setColumnSpan(1);
                                    headerCellSpan.setRowSpan(1);
                                    //Modified, start of code By nazneen
//                                    if (column < facade.getViewByCount()) {
//                                        columnHeadings.add(displayLabels.get(column).toString());
//                                    } else {
//                                        columnHeadings.add(((ArrayList) displayLabels.get(column)).get(colLayer).toString());
//                                    }
                                    String tempDispLbl = "";
                                    if (column < facade.getViewByCount()) {
                                        tempDispLbl = displayLabels.get(column).toString();
                                    } else {
                                        tempDispLbl = ((ArrayList) displayLabels.get(column)).get(colLayer).toString();
                                    }
                                    if (tempDispLbl.contains("Prev Month")) {
                                        columnHeadings.add(facade.container.getMonthNameforTrailingFormulaOnColName(tempDispLbl));
                                    } else {
                                        columnHeadings.add(tempDispLbl);
                                    }
                                    //Modified, End of code By nazneen
                                    displayImageList.add(facade.getSortImageStyle(facade.getColumnId(column)));
                                } else {
                                    headerCellSpan.setColumnSpan(rowLayerCount);
                                    headerCellSpan.setRowSpan(1);
                                    columnHeadings.add(facade.getColumnEdgeLabel(ctColumnCount));
                                    displayImageList.add(facade.getSortImageStyle(facade.getColumnId(column)));
                                    isDisplayed.add(Boolean.TRUE);
                                    ctColumnCount++;
                                }
                            }

                        } else if (column < facade.getViewByCount() && colLayer == measurePosition) {
                            isDisplayed.add(facade.isColumnVisible(facade.getColumnId(column)));
                            headerCellSpan.setColumnSpan(1);
                            headerCellSpan.setRowSpan(1);
                            //Modified, start of code By nazneen
//                                    if (column < facade.getViewByCount()) {
//                                        columnHeadings.add(displayLabels.get(column).toString());
//                                    } else {
//                                        columnHeadings.add(((ArrayList) displayLabels.get(column)).get(colLayer).toString());
//                                    }
                            String tempDispLbl = "";
                            if (column < facade.getViewByCount()) {
                                tempDispLbl = displayLabels.get(column).toString();
                            } else {
                                tempDispLbl = ((ArrayList) displayLabels.get(column)).get(colLayer).toString();
                            }
                            if (tempDispLbl.contains("Prev Month")) {
                                columnHeadings.add(facade.container.getMonthNameforTrailingFormulaOnColName(tempDispLbl));
                            } else {
                                columnHeadings.add(tempDispLbl);
                            }
                            //Modified, End of code By nazneen
                            displayImageList.add(facade.getSortImageStyle(facade.getColumnId(column)));
                        } else if (column < facade.getViewByCount() && colLayer != measurePosition) {
                            isDisplayed.add(Boolean.FALSE);
                            headerCellSpan.setColumnSpan(0);
                            headerCellSpan.setRowSpan(1);
                            columnHeadings.add("");
                            displayImageList.add("");
                        }
                    }


                    if (column == nextColumnToConsider) {
                        isDisplayed.add(Boolean.TRUE);
                        colLayerSpanLst = crosstabColSpans.get(Integer.valueOf(colLayer));
                        if (j >= colLayerSpanLst.size()) {
                            j = 0;
                        }
                        List<Integer> childColLayerSpanLst;
                        if (colLayer == columnLayerCount - 2 && colLayerSpanLst.size() > colSpanIndex) {
                            headerCellSpan.setColumnSpan(colLayerSpanLst.get(colSpanIndex) + ctDynamicColumnCount);
                            //Code Added by Amar for Hybrid report
                            if (facade.container.isSummarizedMeasuresEnabled()) {
                                //code modified by Bhargavi
                                if (facade.container.getSummerizedTableHashMap() != null && !facade.container.getSummerizedTableHashMap().isEmpty() && column < facade.container.getSummerizedTableHashMap().get("summerizedQryeIds").size() + facade.container.getViewByCount()) {
                                    nextColumnToConsider = column + colLayerSpanLst.get(colSpanIndex);
                                } else {
                                    nextColumnToConsider = column + colLayerSpanLst.get(colSpanIndex) + (ctDynamicColumnCount);
                                }
                            } else {
                                nextColumnToConsider = column + colLayerSpanLst.get(colSpanIndex) + (ctDynamicColumnCount);
                            }
                            // end of code
//                            nextColumnToConsider = column + colLayerSpanLst.get(colSpanIndex) + (ctDynamicColumnCount);
                        } else {
                            childColLayerSpanLst = crosstabColSpans.get(Integer.valueOf(columnLayerCount - 2));
                            int noOfChild = colLayerSpanLst.get(j) / tblMeasCount;
                            int columnsToAdd = noOfChild * ctDynamicColumnCount;
                            headerCellSpan.setColumnSpan(colLayerSpanLst.get(j) + columnsToAdd);//(ctDynamicColumnCount * childColLayerSpanLst.size() / colLayerSpanLst.size()));
                            nextColumnToConsider = column + colLayerSpanLst.get(j) + columnsToAdd;//(ctDynamicColumnCount * childColLayerSpanLst.size() / colLayerSpanLst.size());
                            j++;
                        }
                        headerCellSpan.setRowSpan(1);

                        if (GTPos != null && GTPos.equalsIgnoreCase("Last") && displayLabels.size() - tblMeasCount == column && colLayer == 0 && facade.getColumnId(column).contains("_") && facade.container.getRowRenamedTotalName() != null && !facade.container.getRowRenamedTotalName().equalsIgnoreCase("none")) {
                            String name1 = facade.container.getRowRenamedTotalName();
                            columnHeadings.add(facade.container.getRowRenamedTotalName());
                        } //Code modified by Govardhan for Renaming GT In first position...
                        else if (GTPos != null && GTPos.equalsIgnoreCase("First") && displayLabels.size() - tblMeasCount == column && colLayer == 0 && facade.getColumnId(column).contains("_") && facade.container.getRowRenamedTotalName() != null && !facade.container.getRowRenamedTotalName().equalsIgnoreCase("none")) {
                            String name2 = facade.container.getRowRenamedTotalName();
                            columnHeadings.add(facade.container.getRowRenamedTotalName());
                        } else {
                            if (colLayer == 0 && column == 1 && GTPos != null && GTPos.equalsIgnoreCase("First") && facade.container.getRowRenamedTotalName() != null && !facade.container.getRowRenamedTotalName().equalsIgnoreCase("none")) {
                                String name2 = facade.container.getRowRenamedTotalName();
                                columnHeadings.add(facade.container.getRowRenamedTotalName());
                            } else {
                                //Code ended by Govardhan..
                                //Modified, start of code By nazneen
//                                  columnHeadings.add(((ArrayList) displayLabels.get(column)).get(colLayer).toString());
                                String tempDispLbl = "";
                                tempDispLbl = ((ArrayList) displayLabels.get(column)).get(colLayer).toString();
                                if (tempDispLbl.contains("Prev Month")) {
                                    columnHeadings.add(facade.container.getMonthNameforTrailingFormulaOnColName(tempDispLbl));
                                    if (!mapValue.containsKey(tempDispLbl.trim())) {
                                        mapValue.put(tempDispLbl.trim(), facade.container.getMonthNameforTrailingFormulaOnColName(tempDispLbl));
                                        displayGraphLabels.add(tempDispLbl.trim());
                                    }
                                } else if (tempDispLbl.contains("Prev Year")) {
                                    columnHeadings.add(facade.container.getYearNameforTrailingFormulaOnColName(tempDispLbl));
                                    if (!mapValue.containsKey(tempDispLbl.trim())) {
                                        mapValue.put(tempDispLbl.trim(), facade.container.getYearNameforTrailingFormulaOnColName(tempDispLbl));
                                        displayGraphLabels.add(tempDispLbl.trim());
                                    }
                                } else if (tempDispLbl.contains("Prev Day")) {
                                    columnHeadings.add(facade.container.getDayNameforTrailingFormulaOnColName(tempDispLbl));
                                    if (!mapValue.containsKey(tempDispLbl.trim())) {
                                        mapValue.put(tempDispLbl.trim(), facade.container.getDayNameforTrailingFormulaOnColName(tempDispLbl));
                                        displayGraphLabels.add(tempDispLbl.trim());
                                    }
                                } else if (tempDispLbl.contains("Prev Quarter")) {
                                    columnHeadings.add(facade.container.getQuaterNameforTrailingFormulaOnColName(tempDispLbl));
                                    if (!mapValue.containsKey(tempDispLbl.trim())) {
                                        mapValue.put(tempDispLbl.trim(), facade.container.getQuaterNameforTrailingFormulaOnColName(tempDispLbl).trim());
                                        displayGraphLabels.add(tempDispLbl.trim());
                                    }
                                } else if (tempDispLbl.contains("Prior ytd") || tempDispLbl.contains("YTD") || tempDispLbl.contains("CYD") || tempDispLbl.contains("QTD") || tempDispLbl.contains("MTD")) {
                                    columnHeadings.add(facade.container.getQuaterNameforTimeFormulaOnColName(tempDispLbl));
                                    if (!mapValue.containsKey(tempDispLbl.trim())) {
                                        mapValue.put(tempDispLbl.trim(), facade.container.getQuaterNameforTimeFormulaOnColName(tempDispLbl));
                                        displayGraphLabels.add(tempDispLbl.trim());
                                    }
                                } else if (facade.container.isShowStTimePeriod() && crosstabMeasureId.containsKey(facade.getDisplayColumns().get(column)) && colLayer == (columnLayerCount - 1)) {
                                    columnHeadings.add(facade.container.getStdTimePeriodNameOnColName(tempDispLbl));
                                } else {

                                    columnHeadings.add(tempDispLbl);
                                }
                                //Modified, End of code By nazneen

                            }
                        }
                        String name2 = facade.container.getRowRenamedTotalName();
                        if (colLayer == columnLayerCount - 1) {
                            displayImageList.add(facade.getSortImageStyle(facade.getColumnId(column)));
                        } else {
                            displayImageList.add("");
                        }
                        colSpanIndex++;
                    } else if (column >= facade.getViewByCount()) {
                        isDisplayed.add(Boolean.FALSE);
                        headerCellSpan.setColumnSpan(0);
                        headerCellSpan.setRowSpan(0);
                        //Modified, start of code By nazneen
//                        columnHeadings.add(((ArrayList) displayLabels.get(column)).get(colLayer).toString());
                        String tempDispLbl = "";
                        tempDispLbl = ((ArrayList) displayLabels.get(column)).get(colLayer).toString();
                        if (tempDispLbl.contains("Prev Month")) {
                            columnHeadings.add(facade.container.getMonthNameforTrailingFormulaOnColName(tempDispLbl));
                            if (!mapValue.containsKey(tempDispLbl.trim())) {
                                mapValue.put(tempDispLbl.trim(), facade.container.getMonthNameforTrailingFormulaOnColName(tempDispLbl));
                                displayGraphLabels.add(tempDispLbl.trim());
                            }
                        } else {
                            columnHeadings.add(tempDispLbl);
                        }
                        //Modified, End of code By nazneen
                        displayImageList.add("");
                    }
                }
//                else
//                {
//                    isDisplayed.add(facade.isColumnVisible(facade.getColumnId(column)));
//                    headerCellSpan.setColumnSpan(1);
//                    headerCellSpan.setRowSpan(1);
//                    if ( column < facade.getViewByCount() )
//                        columnHeadings.add(displayLabels.get(column).toString());
//                    else
//                        columnHeadings.add(((ArrayList)displayLabels.get(column)).get(colLayer).toString());
//                    displayImageList.add(facade.getSortImageStyle(facade.getColumnId(column)));
//                }
                //colSpanIndex++;
                cellSpans.add(headerCellSpan);
                //  
            }


            facade.container.setTimeDetailsMap(mapValue);
            facade.container.setDisplayGraphLabels(displayGraphLabels);
            //  
            row.setCellSpans(cellSpans);
            //  
            row.setRowDataIds(facade.getDisplayColumns());
            row.setRowData(columnHeadings);
            //  
            row.setDisplayStyle(isDisplayed);
            //  
            //  
            row.setSortImagePath(displayImageList);
            row.setReportParameters(facade.getReportParameters());
            row.setReportParameterNames(facade.getReportParameterNames());
            row.setViewbyCount(facade.getViewByCount());
            row.setAdhocParamDrillUrl(facade.getadhocParamUrl());
            // row.setViewbyId(facade.getViewbyId());
            //  row.setReportParameterValues(facade.getReportParameterValues());
            headerRowLst.add(row);
        }
        return headerRowLst.toArray(new TableHeaderRow[]{});
    }
}
