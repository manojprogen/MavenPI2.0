/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.display;

import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.progen.report.data.TableBuilder;
import com.progen.report.data.TableDataRow;
import com.progen.report.data.TableSubtotalRow;
import com.progen.report.display.table.TableBodyDisplay;
import java.util.ArrayList;

/**
 *
 * @author progen
 */
public class PdfTableBodyDisplay extends TableBodyDisplay {

    public PdfTableBodyDisplay(TableBuilder builder) {
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
        PdfPCell cell = null;
        Font font = FontFactory.getFont(FontFactory.TIMES_ROMAN, 8);
        for (TableDataRow tableRow : rowLst) {
            subTotalCount = super.generateSubtotal(tableRow, subtotalRowLst, from, body);
            if (subTotalCount != 0) {
                usedUpSubtotals += subTotalCount;
                from += subTotalCount;

            }
            for (int col = tableBldr.getFromColumn(); col < colCount; col++) {
                data = tableRow.getRowData(col);
                bgColor = tableRow.getColor(col);
                cssClass = tableRow.getCssClass(col);

                if (cssClass.equalsIgnoreCase("dimensionCell")) {
                    cell = new PdfPCell(new Phrase(data, font));
                    cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                    if (this.getCellHeight() != 0.0f) {
                        cell.setFixedHeight(this.getCellHeight());
                    }

                } else {
                    cell = new PdfPCell(new Phrase(data, font));
                    cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
                    if (this.getCellHeight() != 0.0f) {
                        cell.setFixedHeight(this.getCellHeight());
                    }
                }
                cell.setNoWrap(false);
                pdfTable.addCell(cell);

            }
            //         
        }
        super.updateSubtotalList(usedUpSubtotals, subtotalRowLst);
        return body;
    }

    protected StringBuilder generateSubTotalHTML(TableSubtotalRow[] subTotalRow) {
        PdfTableSubTotalDisplay pdfTotalDisplay = new PdfTableSubTotalDisplay(tableBldr);
        pdfTotalDisplay.setPdfTable(pdfTable);
        pdfTotalDisplay.setCellHeight(cellHeight);
        return pdfTotalDisplay.generateSubTotalHtml(subTotalRow);

//        ExcelTableSubTotalDisplay excelTotalDisplay=new ExcelTableSubTotalDisplay(tableBldr);
//        excelTotalDisplay.setWritableSheet(writableSheet);
//        excelTotalDisplay.setWritableWorkbook(writableWorkbook);
//        return excelTotalDisplay.generateSubTotalHtml(subTotalRow);
    }
}
