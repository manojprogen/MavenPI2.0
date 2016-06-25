/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.display;

import com.progen.report.data.TableBuilder;
import com.progen.report.data.TableDataRow;
import com.progen.report.data.TableSubtotalRow;
import com.progen.report.display.table.TableBodyDisplay;
import com.progen.report.display.table.TableDisplay;
import java.util.ArrayList;

/**
 *
 * @author progen
 */
public class CDTableBodyDisplay extends TableBodyDisplay {

    private String delimiter;
    private String textIdentifier;

    public CDTableBodyDisplay(TableBuilder builder) {
        super(builder);
    }

    public StringBuilder generateHTML() {
        StringBuilder body = new StringBuilder();
        TableDataRow tableRow;
        int fromRow, toRow;
        int batchCount = 0;
        ArrayList<TableDataRow> rowLst = new ArrayList<TableDataRow>();
        ArrayList<TableSubtotalRow> subtotalRowLst = new ArrayList<TableSubtotalRow>();

        fromRow = tableBldr.getFromRow();
        toRow = tableBldr.getToRow();

        for (int row = fromRow; row < toRow; row++) {
            tableRow = (TableDataRow) tableBldr.getRowData(row);
            if (tableRow.printSubTotals()) {
                TableSubtotalRow[] subtotalRows = tableBldr.getSubtotalRowData();
                this.addCellSpanToRows(rowLst);
                body.append(this.generateHtmlForRows(rowLst, subtotalRowLst));
                body.append(generateSubTotalHTML(subtotalRows));
                rowLst.clear();
            }
            rowLst.add(tableRow);
            batchCount++;
            if (batchCount >= TableDisplay.BATCH_SIZE) {
                batchCount = 0;
                this.addCellSpanToRows(rowLst);
                body.append(this.generateHtmlForRows(rowLst, subtotalRowLst));
                this.flushToOutputStream(super.parentHtml.append(body));
                body = new StringBuilder();
                super.parentHtml = new StringBuilder();
                rowLst.clear();
            }
        }
        if (!rowLst.isEmpty()) {
            this.addCellSpanToRows(rowLst);
            body.append(this.generateHtmlForRows(rowLst, subtotalRowLst));
            rowLst.clear();
        }
        return super.parentHtml.append(body);
    }

    @Override
    public StringBuilder generateHtmlForRows(ArrayList<TableDataRow> rowLst, ArrayList<TableSubtotalRow> subtotalRowLst) {
        //
        StringBuilder body = new StringBuilder();
        int colCount = tableBldr.getColumnCount();
        String displayStyle;
        String data;
        String id;
        String bgColor = "";
//        String href;
        String textColor = "";
        String cssClass = "";
//        String spaceAftrNo = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
        for (TableDataRow tableRow : rowLst) {

//            body.append("<Tr>");
            for (int col = tableBldr.getFromColumn(); col < colCount; col++) {
                data = tableRow.getRowData(col);
//                body.append(data.replace(",", ""));
                body.append(getTextIdentifier()).append(data.replace(",", "")).append(getTextIdentifier());
                if (col != colCount - 1) {
                    body.append(getDelimiter());
                }


                displayStyle = tableRow.getDisplayStyle(col);
                id = tableRow.getID(col);
                bgColor = tableRow.getColor(col);
                textColor = tableRow.getFontColors(col);
                cssClass = tableRow.getCssClass(col);
//                if (cssClass.equalsIgnoreCase("dimensionCell")) {
//                    body.append("<td id='").append(id).append("'")//.append(" class='").append(cssClass)
//                            .append(" rowspan=").append(tableRow.getRowSpan(col)).append(" colspan=").append(tableRow.getColumnSpan(col)).append("  style='font-size:12px;text-align:right;background-color:#E6E6E6;color:#336699;text-align:left;").append(" display:").append(displayStyle).append(";' ").append(">");
//                    body.append("<Font color=").append(textColor).append(">").append(data).append("</Font>");
//                    body.append(spaceAftrNo);
//                    body.append("</td>");
//                } else {
//                    body.append("<td id='").append(id).append("'")//.append(" class='").append(cssClass)
//                            .append(" rowspan=").append(tableRow.getRowSpan(col)).append(" colspan=").append(tableRow.getColumnSpan(col)).append("  style='font-size:12px;text-align:right;background-color:").append(bgColor).append(";").append(" display:").append(displayStyle).append(";' ").append(">");
//                    body.append("<Font color=").append(textColor).append(">").append(data).append("</Font>");
//                    body.append(spaceAftrNo);
//                    body.append("</td>");
//                }

            }
            body.append("\n");
//            body.append("</tr>");
        }
        return body;
    }
//    background-color:#E6E6E6;
//color:#336699;
//font-family:verdana;
//text-align:left;

    public StringBuilder generateSubTotalHTML(TableSubtotalRow[] subTotalRow) {
        CDTableSubTotalDisplay subTotalDisplay = new CDTableSubTotalDisplay(tableBldr);
        return subTotalDisplay.generateSubTotalHtml(subTotalRow);
    }

    /**
     * @return the delimiter
     */
    public String getDelimiter() {
        return delimiter;
    }

    /**
     * @param delimiter the delimiter to set
     */
    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    /**
     * @return the textIdentifier
     */
    public String getTextIdentifier() {
        return textIdentifier;
    }

    /**
     * @param textIdentifier the textIdentifier to set
     */
    public void setTextIdentifier(String textIdentifier) {
        this.textIdentifier = textIdentifier;
    }
}
