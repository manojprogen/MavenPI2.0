/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.display;

import com.progen.report.data.TableBuilder;
import com.progen.report.data.TableHeaderRow;
import com.progen.report.display.table.TableHeaderDisplay;
import java.awt.Color;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.xssf.usermodel.*;

/**
 *
 * @author progen
 */
public class OfficeTableHeaderDisplay extends TableHeaderDisplay {

    private XSSFSheet sheet;
    private XSSFWorkbook workbook;

    public OfficeTableHeaderDisplay(TableBuilder builder) {
        super(builder);
    }

    public StringBuilder generateHTML() {

        TableHeaderRow[] headerRows;
        headerRows = tableBldr.getHeaderRowData();
        int colCount = tableBldr.getColumnCount();

        StringBuilder header = new StringBuilder();

        String heading;
        int row = 0;
        int column;
        XSSFRow xssfRow;
        XSSFCell xssfCell;
        XSSFCellStyle style = getWorkbook().createCellStyle();
        // code modified by amar
        //style.setFillBackgroundColor(new XSSFColor(new Color(153,180,255)));
        style.setFillForegroundColor(new XSSFColor(new Color(153, 180, 255)));
        style.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
        // end of code
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        Font font = getWorkbook().createFont();
        font.setFontHeightInPoints((short) 10);
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        style.setFont(font);
        for (TableHeaderRow headerRow : headerRows) {
            xssfRow = getSheet().createRow(row);
            column = 0;
            // header.append("<tr>");
            for (int i = tableBldr.getFromColumn(); i < colCount; i++) {
                heading = headerRow.getRowData(i);
                xssfCell = xssfRow.createCell(column);
                xssfCell.setCellValue(heading);
                xssfCell.setCellStyle(style);
                column++;
            }
            row++;
        }
        return super.parentHtml.append(header);
    }

    @Override
    protected void setParentHtml(StringBuilder parentHtml) {
        super.parentHtml = parentHtml;
    }

    public XSSFSheet getSheet() {
        return sheet;
    }

    public void setSheet(XSSFSheet sheet) {
        this.sheet = sheet;
    }

    public XSSFWorkbook getWorkbook() {
        return workbook;
    }

    public void setWorkbook(XSSFWorkbook workbook) {
        this.workbook = workbook;
    }
}
