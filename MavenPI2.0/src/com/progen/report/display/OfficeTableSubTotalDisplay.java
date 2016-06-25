/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.display;

import com.progen.report.data.TableBuilder;
import com.progen.report.data.TableSubtotalRow;
import com.progen.report.display.table.TableSubtotalDisplay;
import java.awt.Color;
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
public class OfficeTableSubTotalDisplay extends TableSubtotalDisplay {

    public OfficeTableSubTotalDisplay(TableBuilder builder) {
        super(builder);
    }

    @Override
    public StringBuilder generateSubTotalHtml(TableSubtotalRow[] subTotalRow) {
        //
        TableSubtotalRow row;
        StringBuilder subTotalHtml = new StringBuilder();
        String cssClass = "";
        int colCount;
        int column;
        int rowCount = 0;
        XSSFRow xssfRow;
        XSSFCell xssfCell;
        rowCount = getSheet().getLastRowNum();
        rowCount = rowCount + 1;
        Double doubleValue = 0.0;
        String data = "";
        XSSFCellStyle style = getWorkbook().createCellStyle();
        //style.setFillBackgroundColor(new XSSFColor(new Color(153,180,255)));
        style.setFillForegroundColor(new XSSFColor(new Color(153, 180, 255)));
        style.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);

        XSSFCellStyle styleNumeric = workbook.createCellStyle();
        // code modified by amar
        //styleNumeric.setFillBackgroundColor(new XSSFColor(new Color(220,220,220)));
        styleNumeric.setFillForegroundColor(new XSSFColor(new Color(220, 220, 220)));
        styleNumeric.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        // end of code
        styleNumeric.setBorderBottom(BorderStyle.THIN);
        styleNumeric.setBorderTop(BorderStyle.THIN);
        styleNumeric.setBorderLeft(BorderStyle.THIN);
        styleNumeric.setBorderRight(BorderStyle.THIN);
        for (int i = 0; i < subTotalRow.length; i++) {
            row = subTotalRow[i];
            colCount = row.getColumnCount();
            column = 0;
            xssfRow = getSheet().createRow(rowCount);
            subTotalHtml.append("<Tr ID=\"").append(row.getSubtotalType()).append("\" >");
            for (int col = tableBldr.getFromColumn(); col < colCount; col++) {
                xssfCell = xssfRow.createCell(column);
                data = row.getRowData(col);
                cssClass = row.getCssClass(col);
                if (cssClass.equalsIgnoreCase("subTotalCell")) {
                    xssfCell.setCellValue(data);
                    xssfCell.setCellStyle(style);
                } else {
                    if (data != null && !data.equalsIgnoreCase("")) {
                        doubleValue = new Double(data.replace(",", ""));
                    }
                    xssfCell.setCellValue(doubleValue);
                    xssfCell.setCellStyle(styleNumeric);
                }

                column++;
            }
            rowCount++;
        }
        return subTotalHtml;
    }
}
