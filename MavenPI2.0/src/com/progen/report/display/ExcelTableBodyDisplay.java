/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.display;

import com.progen.report.data.TableBuilder;
import com.progen.report.data.TableDataRow;
import com.progen.report.data.TableSubtotalRow;
import com.progen.report.display.table.TableBodyDisplay;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import jxl.format.Alignment;
import jxl.format.Colour;
import jxl.write.NumberFormats;
import jxl.write.WritableCell;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WriteException;
import jxl.write.Label;
import jxl.write.Number;
import prg.db.PbReturnObject;
import org.apache.log4j.*;

/**
 *
 * @author progen
 */
public class ExcelTableBodyDisplay extends TableBodyDisplay {

    public static Logger logger = Logger.getLogger(ExcelTableBodyDisplay.class);

    public ExcelTableBodyDisplay(TableBuilder builder) {
        super(builder);
    }

    protected StringBuilder generateHtmlForRows(ArrayList<TableDataRow> rowLst, ArrayList<TableSubtotalRow> subtotalRowLst) {
        StringBuilder body = new StringBuilder();
        int colCount = tableBldr.getColumnCount();
        ArrayList<String> hidemeasurecount = tableBldr.facade.container.getReportCollect().getHideMeasures();
        int from = 0;
        String id = null;
        int subTotalCount = 0;
        int usedUpSubtotals = 0;
        String data;
        String bgColor = "";
        String cssClass = "";
        int rowStart = 0;
        WritableFont writableFont = new WritableFont(WritableFont.ARIAL, 8, WritableFont.NO_BOLD);
        boolean lastRow1 = tableBldr.lastRow;

        WritableCell cell;
        Double doubleValue = 0.0;
        HashMap NFMap = new HashMap();
        HashMap<String, String> crosstabMeasureId = ((PbReturnObject) tableBldr.facade.container.getRetObj()).crosstabMeasureId;
        ArrayList dataTypes = tableBldr.facade.container.getDataTypes();
        ArrayList rowviewbyValues = tableBldr.facade.container.getReportCollect().reportRowViewbyValues;
        ArrayList<Integer> index = new ArrayList<Integer>();
        int actualcol = 0;
//        
        if (hidemeasurecount.size() > 0) {
            for (int i = 0; i < hidemeasurecount.size(); i++) {
                index.add(tableBldr.facade.container.getDisplayColumns().indexOf("A_" + hidemeasurecount.get(i)));
            }
        }
        for (TableDataRow tableRow : rowLst) {
            subTotalCount = super.generateSubtotal(tableRow, subtotalRowLst, from, body);
            rowStart = getWritableSheet().getRows();
            if (subTotalCount != 0) {
                usedUpSubtotals += subTotalCount;
                from += subTotalCount;

            }


            if (!lastRow1) {
                actualcol = tableBldr.getFromColumn();
                StringBuilder dimData = new StringBuilder();
//                String dimData = "";//added by sruthi for colour in exlsheet
                int ViewByCount = 0; //added by sruthi for colour in exlsheet
                ViewByCount = tableBldr.getViewByCount(); //added by sruthi for colour in exlsheet
                for (int col = tableBldr.getFromColumn(); col < colCount; col++) {
                    if (!index.contains(col)) {

                        String Symbol = "";
                        String nbrSymbol = "";
                        WritableCellFormat writableCellFormat = new WritableCellFormat(writableFont);
                        try {
                            writableCellFormat.setWrap(true);
                        } catch (WriteException ex) {
                            logger.error("Exception:", ex);
                        }
                        WritableCellFormat cf2 = new WritableCellFormat(writableFont, NumberFormats.THOUSANDS_FLOAT);
                        try {
                            cf2.setWrap(true);
                        } catch (WriteException ex) {
                            logger.error("Exception:", ex);
                        }
                        data = tableRow.getRowData(col);

                        bgColor = tableRow.getColor(col);
                        cssClass = tableRow.getCssClass(col);
                        ////added by sruthi for colour in exlsheet
                        if (col <= ViewByCount - 1) {
                            dimData.append(dimData).append("~").append(data);
                        }
                        String subTotalDeviation = "";
                        id = tableRow.getID(col);
                        String measureType = "Standard";
                        String newId = id.replace("A_", "");
                        String reportDrillUrl = null;
                        String selfReportDrillUrl = null;
                        int indexval = newId.indexOf("_");
                        // 
                        String elementId = newId.substring(0, indexval);
                        subTotalDeviation = tableBldr.getSubTotalDeviation("A_" + elementId);
                        measureType = tableBldr.getMeasureType("A_" + elementId);
                        //end of code by Nazneen for sub total deviation

                        if (tableBldr.getMeasureDrillType().equalsIgnoreCase("ReportDrill") || tableBldr.getMeasureDrillType().equalsIgnoreCase("ReportDrillPopUp")) {
                            reportDrillUrl = tableRow.getReportDrillList(col);
                        }
                        selfReportDrillUrl = tableRow.getSelfreportDrillList(col);
                        bgColor = tableRow.getColor(col);
                        //added by Nazneen on 8May14 for disable ST Dev properties for rank with ST measures
                        if (newId.contains("rankST")) {
                            subTotalDeviation = null;
                        }
                        //ended by Nazneen on 8May14 for disable ST Dev properties for rank with ST measures
                        //start of code by Nazneen
                        if (subTotalDeviation != null && !subTotalDeviation.equalsIgnoreCase("") && !subTotalDeviation.equalsIgnoreCase("null") && subTotalDeviation.equalsIgnoreCase("Y")) {
                            BigDecimal subTotalValue = null;
                            if (!cssClass.equalsIgnoreCase("dimensionCell")) {
                                subTotalValue = tableBldr.getSubTotalVal(col, "CATST", dimData.toString(), ViewByCount);
                                if (subTotalValue != null) {
                                    String newData = data.replace(",", "").replace("$", "").replace("Rs", "").replace("Euro", "").replace("Yen", "").replace("%", "").replace("AED", "").replace("K", "").replace("M", "").replace("L", "").replace("C", "").replace(" ", "");
                                    BigDecimal newDataVal = new BigDecimal(newData);
                                    int res = newDataVal.compareTo(subTotalValue); // compare newDataVal with subTotalValue
                                    if (res == 0) {
                                        bgColor = "";
                                    } else if (res == 1) {
                                        if (measureType.equalsIgnoreCase("Standard")) {
                                            try {

                                                cf2.setBackground(Colour.GREEN);
                                            } catch (WriteException ex) {
                                                logger.error("Exception:", ex);
                                            }
                                        } else {
                                            try {
                                                cf2.setBackground(Colour.RED);
                                            } catch (WriteException ex) {
                                                logger.error("Exception:", ex);
                                            }
                                        }
                                    } else if (res == -1) {
                                        if (measureType.equalsIgnoreCase("Standard")) {

                                            try {
                                                cf2.setBackground(Colour.RED);
                                            } catch (WriteException ex) {
                                                logger.error("Exception:", ex);
                                            }
                                        } else {
                                            try {
                                                cf2.setBackground(Colour.GREEN);
                                            } catch (WriteException ex) {
                                                logger.error("Exception:", ex);
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            //end of code by Nazneen
                            //start of code for colorGroup by veena
                            if (ViewByCount >= 2 && tableBldr.facade.isAnySubtotallingRequired() && !tableBldr.facade.container.isReportCrosstab()) {
                                BigDecimal subTotalValue = null;
                                if (!cssClass.equalsIgnoreCase("dimensionCell")) {
                                    subTotalValue = tableBldr.getSubTotalVal(col, "CATST", dimData.toString(), ViewByCount);
                                }
                                if (elementId.contains("A")) {
                                    bgColor = tableBldr.facade.getColor(tableRow.getRowNumber(), elementId, subTotalValue);
                                } else {
                                    bgColor = tableBldr.facade.getColor(tableRow.getRowNumber(), "A_" + elementId, subTotalValue);
                                }
                            }
                            //end of code by veena
                        }
                        //ended by sruthi
                        //added by sruthi for excel formating in case of ST
                        String displayStyle = tableRow.getDisplayStyle(col);
                        if (displayStyle.equals("none")) {
                            data = "";
                        }
                        //ended by sruthi
                        Symbol = tableBldr.facade.container.symbol.get(tableRow.getColumnId(col));
                        NFMap = (HashMap) tableBldr.facade.container.getTableHashMap().get("NFMap");
                        if (NFMap != null && !NFMap.isEmpty()) {
                            if (tableBldr.facade.container.isReportCrosstab() && crosstabMeasureId != null && !crosstabMeasureId.isEmpty()) {
                                if (crosstabMeasureId.containsKey(tableRow.getColumnId(col)) && NFMap.get(crosstabMeasureId.get(tableRow.getColumnId(col))) != null) {
                                    nbrSymbol = String.valueOf(NFMap.get(crosstabMeasureId.get(tableRow.getColumnId(col))));
                                }
                            } else if (NFMap.get(tableRow.getColumnId(col)) != null) {
                                nbrSymbol = String.valueOf(NFMap.get(tableRow.getColumnId(col)));
                            }
                        }
                        if (cssClass.equalsIgnoreCase("dimensionCell")) {
                            //cell = new Label(col, rowStart,data);
                            cell = new Label(actualcol, rowStart, data);
                            cell.setCellFormat(writableCellFormat);
                        } else {
                            if (tableRow.getTextAlign(col) != null) {
                                String alignment = tableRow.getTextAlign(col);
                                if (alignment != null) {
                                    try {
                                        if (alignment.equalsIgnoreCase("left")) {
                                            writableCellFormat.setAlignment(Alignment.LEFT);
                                            cf2.setAlignment(Alignment.LEFT);
                                        } else if (alignment.equalsIgnoreCase("center")) {
                                            writableCellFormat.setAlignment(Alignment.CENTRE);
                                            cf2.setAlignment(Alignment.CENTRE);
                                        } else if (alignment.equalsIgnoreCase("right")) {
                                            writableCellFormat.setAlignment(Alignment.RIGHT);
                                            cf2.setAlignment(Alignment.RIGHT);
                                        } else {
                                            writableCellFormat.setAlignment(Alignment.GENERAL);
                                            cf2.setAlignment(Alignment.GENERAL);
                                        }
                                    } catch (Exception ex) {
                                        logger.error("Exception:", ex);
                                    }
                                }
                            }
                            if ((Symbol != null && !Symbol.equalsIgnoreCase("")) || (nbrSymbol != null && !nbrSymbol.equalsIgnoreCase("")) || "D".equals(dataTypes.get(col))) {
                                //cell = new Label(col,rowStart,data,writableCellFormat);
                                cell = new Label(actualcol, rowStart, data, writableCellFormat);
                            } else if ("N".equals(dataTypes.get(col))) {
                                if (data != null && !data.equalsIgnoreCase("") && isNumeric(data) && !rowviewbyValues.contains(tableRow.getColumnId(col).replace("A_", ""))) {
                                    doubleValue = new Double(data.replace(",", ""));
                                    //cell = new Number(col, rowStart,doubleValue,cf2);
                                    cell = new Number(actualcol, rowStart, doubleValue, cf2);
                                } else //cell = new Label(col,rowStart,data,writableCellFormat);
                                {
                                    cell = new Label(actualcol, rowStart, data, writableCellFormat);
                                }
                            } else {
                                //cell = new Label(col,rowStart,data,writableCellFormat);
                                cell = new Label(actualcol, rowStart, data, writableCellFormat);
                            }
//                    cell.setCellFormat(writableCellFormat);
                        }

                        try {
                            writableSheet.addCell(cell);
                        } catch (WriteException ex) {
                            logger.error("Exception:", ex);
                        } catch (Exception ex) {
                            logger.error("Exception:", ex);
                        }
                        actualcol++;
                    }

                }
            }
            // 
        }
        super.updateSubtotalList(usedUpSubtotals, subtotalRowLst);
        return body;

    }

    protected StringBuilder generateSubTotalHTML(TableSubtotalRow[] subTotalRow) {
        ExcelTableSubTotalDisplay excelTotalDisplay = new ExcelTableSubTotalDisplay(tableBldr);
        excelTotalDisplay.setWritableSheet(writableSheet);
        excelTotalDisplay.setWritableWorkbook(writableWorkbook);
        return excelTotalDisplay.generateSubTotalHtml(subTotalRow);
    }

    public boolean isNumeric(String str) {
        try {
            double d = Double.parseDouble(str.replace(",", ""));
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}
