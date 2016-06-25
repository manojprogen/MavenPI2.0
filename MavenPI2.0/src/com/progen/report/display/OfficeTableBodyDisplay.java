/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.display;

import com.progen.report.data.TableBuilder;
import com.progen.report.data.TableDataRow;
import com.progen.report.data.TableSubtotalRow;
import com.progen.report.display.table.TableBodyDisplay;
import java.awt.Color;
import java.util.ArrayList;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFRow;

/**
 *
 * @author progen
 */
public class OfficeTableBodyDisplay extends TableBodyDisplay {

    public OfficeTableBodyDisplay(TableBuilder builder) {
        super(builder);
    }

    @Override
    protected StringBuilder generateHtmlForRows(ArrayList<TableDataRow> rowLst, ArrayList<TableSubtotalRow> subtotalRowLst) {
        //
        StringBuilder body = new StringBuilder();
        int colCount = tableBldr.getColumnCount();
        String displayStyle;
        String data;
        String id;
        String bgColor = "";
        String href;
        String textColor = "";
        String cssClass = "";
        String spaceAftrNo = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
        int row = 0;
        int column;
        XSSFRow xssfRow;
        XSSFCell xssfCell;
        row = getSheet().getLastRowNum();
        row = row + 1;
        Double doubleValue = 0.0;
        XSSFCellStyle style = workbook.createCellStyle();
        // code modified by amar
        //style.setFillBackgroundColor(new XSSFColor(new Color(220,220,220)));
        style.setFillForegroundColor(new XSSFColor(new Color(220, 220, 220)));
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        // end of code
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        int from = 0;
        int subTotalCount = 0;
        int usedUpSubtotals = 0;
        int mergeRow = 0;
        for (TableDataRow tableRow : rowLst) {
            mergeRow = tableRow.getRowNumber();
            subTotalCount = generateSubtotal(tableRow, subtotalRowLst, from, body);
            if (subTotalCount != 0) {
                usedUpSubtotals += subTotalCount;
                from += subTotalCount;
//                if(subTotalCount>1)
//                sheet.addMergedRegion(new CellRangeAddress(
//                        mergeRow, //first row (0-based)
//                        mergeRow+subTotalCount, //last row  (0-based)
//                        0, //first column (0-based)
//                        0  //last column  (0-based)
//                ));
                row = row + subTotalCount;
            }
            xssfRow = getSheet().createRow(row);

            column = 0;
            for (int col = tableBldr.getFromColumn(); col < colCount; col++) {
                data = tableRow.getRowData(col);
                bgColor = tableRow.getColor(col);
                cssClass = tableRow.getCssClass(col);
                xssfCell = xssfRow.createCell(column);
                if (cssClass.equalsIgnoreCase("dimensionCell")) {
                    if (tableRow.getDisplayStyle(column).equalsIgnoreCase("none")) {
                        xssfCell.setCellValue("");
                    } else {
                        xssfCell.setCellValue(data);
                    }
                    xssfCell.setCellStyle(style);
                } else {
//                   if(data!=null && !data.equalsIgnoreCase(""))
//                     doubleValue=new Double(data.replace(",", ""));
//                    xssfCell.setCellValue(doubleValue);
                    xssfCell.setCellValue(data);
                }



//                displayStyle = tableRow.getDisplayStyle(col);
//                id = tableRow.getID(col);
//                bgColor = tableRow.getColor(col);
//                textColor = tableRow.getFontColors(col);
//                cssClass = tableRow.getCssClass(col);

                column++;
            }
            row++;
        }
        super.updateSubtotalList(usedUpSubtotals, subtotalRowLst);
        return body;
    }

    protected StringBuilder generateSubTotalHTML(TableSubtotalRow[] subTotalRow) {
        OfficeTableSubTotalDisplay officeTotalDisplay = new OfficeTableSubTotalDisplay(tableBldr);
        officeTotalDisplay.setSheet(sheet);
        officeTotalDisplay.setWorkbook(workbook);
        return officeTotalDisplay.generateSubTotalHtml(subTotalRow);
    }
}
