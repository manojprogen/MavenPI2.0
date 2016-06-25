/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.display;

import com.progen.report.data.TableBuilder;
import com.progen.report.data.TableSubtotalRow;
import com.progen.report.display.table.TableSubtotalDisplay;

/**
 *
 * @author progen
 */
public class XMLTableSubTotalDisplay extends TableSubtotalDisplay {

    public XMLTableSubTotalDisplay(TableBuilder builder) {
        super(builder);
    }

    public StringBuilder generateHTML() {
        StringBuilder subTotalHtml = new StringBuilder();
        TableSubtotalRow[] subTotalRow = tableBldr.getSubtotalRowDataLastRow();
        subTotalHtml.append(this.generateSubTotalHtml(subTotalRow));
        subTotalRow = tableBldr.getGrandtotalRowData();


        subTotalHtml.append(this.generateSubTotalHtml(subTotalRow));


        return super.parentHtml.append(subTotalHtml);
    }

    @Override
    public StringBuilder generateSubTotalHtml(TableSubtotalRow[] subTotalRow) {

        TableSubtotalRow row;
        StringBuilder subTotalHtml = new StringBuilder();

        int colCount;

//        subTotalHtml.append("<TOTAL_DATA>");
        for (int i = 0; i < subTotalRow.length; i++) {
            subTotalHtml.append("<ROW_TOTAL>");
            row = subTotalRow[i];
            colCount = row.getColumnCount();
            for (int col = tableBldr.getFromColumn(); col < colCount; col++) {

                subTotalHtml.append("<TOTAL_DATA>");
                subTotalHtml.append((row.getRowData(col)).replace(",", ""));
                subTotalHtml.append("</TOTAL_DATA>");
//                     if(col!=colCount-1)
//                     subTotalHtml.append("\t");

            }
            subTotalHtml.append("</ROW_TOTAL>");
//            subTotalHtml.append("\n");

        }
        if (subTotalRow.length > 0) {
//        subTotalHtml.append("</BODY>");
            subTotalHtml.append("</XML>");
        }
//        subTotalHtml.append("</TOTAL_DATA>");
        return subTotalHtml;

    }
}
