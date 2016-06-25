/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.display;

import com.progen.report.data.TableBuilder;
import com.progen.report.data.TableHeaderRow;
import com.progen.report.display.table.TableHeaderDisplay;

/**
 *
 * @author progen
 */
public class XMLTableHeaderDisplay extends TableHeaderDisplay {

    public XMLTableHeaderDisplay(TableBuilder builder) {
        super(builder);
    }

    @Override
    public StringBuilder generateHTML() {
        TableHeaderRow[] headerRows;
        headerRows = tableBldr.getHeaderRowData();
        int colCount = tableBldr.getColumnCount();
        StringBuilder header = new StringBuilder();

        String heading;
        String id;
        String sortImage;
        int rowSpan;
        int colSpan;
        String dispStyle;
        header.append("<XML>");
        header.append("<HEADER>");
        for (TableHeaderRow headerRow : headerRows) {

            for (int i = tableBldr.getFromColumn(); i < colCount; i++) {
                heading = headerRow.getRowData(i);
                header.append("<COLUMN_NAME>");
                header.append(heading);
                header.append("</COLUMN_NAME>");

            }


        }
        header.append("</HEADER>");
        return super.parentHtml.append(header);
    }
}
