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
import com.progen.report.data.TableSubtotalRow;
import com.progen.report.display.table.TableSubtotalDisplay;

/**
 *
 * @author progen
 */
public class PdfTableSubTotalDisplay extends TableSubtotalDisplay {

    public PdfTableSubTotalDisplay(TableBuilder builder) {
        super(builder);
    }

    @Override
    public StringBuilder generateSubTotalHtml(TableSubtotalRow[] subTotalRow) {
        TableSubtotalRow row;
        StringBuilder subTotalHtml = new StringBuilder();
        String cssClass = "";
        int colCount;
        String data = "";
        PdfPCell cell = null;
        Font font = FontFactory.getFont(FontFactory.TIMES_ROMAN, 8);
        for (int i = 0; i < subTotalRow.length; i++) {
            row = subTotalRow[i];
            colCount = row.getColumnCount();
            for (int col = tableBldr.getFromColumn(); col < colCount; col++) {
                data = row.getRowData(col);
                cssClass = row.getCssClass(col);
                if (cssClass.equalsIgnoreCase("subTotalCell")) {
                    cell = new PdfPCell(new Phrase(data, font));
                    cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
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
        return subTotalHtml;
    }
}
