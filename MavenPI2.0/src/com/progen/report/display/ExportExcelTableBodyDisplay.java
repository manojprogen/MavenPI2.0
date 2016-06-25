/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.display;

import com.progen.report.data.TableBuilder;
import com.progen.report.data.TableDataRow;
import com.progen.report.data.TableSubtotalRow;
import com.progen.report.display.table.TableBodyDisplay;
import java.util.ArrayList;
import java.util.HashMap;
import jxl.format.Alignment;
import jxl.write.*;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import prg.db.PbReturnObject;

/**
 *
 * @author progen
 */
public class ExportExcelTableBodyDisplay extends TableBodyDisplay {

    public static Logger logger = Logger.getLogger(ExportExcelTableBodyDisplay.class);

    public ExportExcelTableBodyDisplay(TableBuilder builder) {
        super(builder);
    }

    protected StringBuilder generateHtmlForRows(ArrayList<TableDataRow> rowLst, ArrayList<TableSubtotalRow> subtotalRowLst) {
        StringBuilder body = new StringBuilder();
        int colCount = tableBldr.getColumnCount();
        int from = 0;
        int subTotalCount = 0;
        int usedUpSubtotals = 0;
        String data;
        String bgColor = "";
        String cssClass = "";
        int rowStart = 0;
        int rowStartCount = 0;
        int exactRowIndex = 0;
        // int colIndex = 0;
        WritableFont writableFont = new WritableFont(WritableFont.ARIAL, 8, WritableFont.NO_BOLD);
        //Code added by Amar
        HSSFRow hRow;
        HSSFCell hCell;
        HSSFSheet hSheet;
        // end of code
        WritableCell cell;
        Double doubleValue = 0.0;
        HashMap NFMap = new HashMap();
        HashMap<String, String> crosstabMeasureId = ((PbReturnObject) tableBldr.facade.container.getRetObj()).crosstabMeasureId;
        ArrayList dataTypes = tableBldr.facade.container.getDataTypes();
        ArrayList rowviewbyValues = tableBldr.facade.container.getReportCollect().reportRowViewbyValues;
//        
        rowStartCount = getSatartRow();
        for (TableDataRow tableRow : rowLst) {
            subTotalCount = super.generateSubtotal(tableRow, subtotalRowLst, from, body);
            //added by amar
//            rowStart=getHSheet().getPhysicalNumberOfRows();
            //colIndex = rowStart+1;
            exactRowIndex = rowStartCount + rowStart;
            hSheet = getHSheet();
            hRow = hSheet.createRow((short) exactRowIndex);
            rowStart++;
            //rowStart=getWritableSheet().getRows();
            if (subTotalCount != 0) {
                usedUpSubtotals += subTotalCount;
                from += subTotalCount;

            }
            for (int col = tableBldr.getFromColumn(); col < colCount; col++) {
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
                    hCell = hRow.createCell((short) col);
                    hCell.setCellValue(data);
//                     cell = new Label(col, rowStart,data);
//                     cell.setCellFormat(writableCellFormat);                  
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
                        hCell = hRow.createCell(col);
                        hCell.setCellValue(data);
                    } else if ("N".equals(dataTypes.get(col))) {
                        if (data != null && !data.equalsIgnoreCase("") && isNumeric(data) && !rowviewbyValues.contains(tableRow.getColumnId(col).replace("A_", ""))) {
                            doubleValue = new Double(data.replace(",", ""));
                            //cell = new Number(col, rowStart,doubleValue,cf2);
                            hCell = hRow.createCell(col);
                            hCell.setCellValue(doubleValue);
                        } else {
                            //  cell = new Label(col,rowStart,data,writableCellFormat);
                            hCell = hRow.createCell(col);
                            hCell.setCellValue(data);
                        }
                    } else {
                        //cell = new Label(col,rowStart,data,writableCellFormat);
                        hCell = hRow.createCell(col);
                        hCell.setCellValue(data);
                    }
//                    cell.setCellFormat(writableCellFormat);
                }

//                try {
//                    writableSheet.addCell(cell);
//                } catch (WriteException ex) {
//                    logger.error("Exception:",ex);
//                } catch (Exception ex) {
//                    logger.error("Exception:",ex);
//                }                
            }
        }
        super.updateSubtotalList(usedUpSubtotals, subtotalRowLst);
        return body;

    }

    protected StringBuilder generateSubTotalHTML(TableSubtotalRow[] subTotalRow) {
        ExportExcelTableSubTotalDisplay exportExcelTotalDisplay = new ExportExcelTableSubTotalDisplay(tableBldr);
        exportExcelTotalDisplay.setWritableSheet(writableSheet);
        exportExcelTotalDisplay.setWritableWorkbook(writableWorkbook);
        return exportExcelTotalDisplay.generateSubTotalHtml(subTotalRow);
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
