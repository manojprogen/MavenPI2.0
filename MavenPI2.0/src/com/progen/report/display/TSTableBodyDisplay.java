package com.progen.report.display;

import com.progen.report.data.TableBuilder;
import com.progen.report.data.TableDataRow;
import com.progen.report.data.TableSubtotalRow;
import com.progen.report.display.table.TableBodyDisplay;
import com.progen.report.display.table.TableDisplay;
import java.util.ArrayList;

public class TSTableBodyDisplay extends TableBodyDisplay {

    public TSTableBodyDisplay(TableBuilder builder) {
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

        StringBuilder body = new StringBuilder();
        int colCount = tableBldr.getColumnCount();
        String displayStyle;
        String data;
        String id;
        String bgColor = "";

        String textColor = "";
        String cssClass = "";

        for (TableDataRow tableRow : rowLst) {


            for (int col = tableBldr.getFromColumn(); col < colCount; col++) {
                data = tableRow.getRowData(col);
                body.append(data.replace(",", ""));
                if (col != colCount - 1) {
                    body.append("\t");
                }


                displayStyle = tableRow.getDisplayStyle(col);
                id = tableRow.getID(col);
                bgColor = tableRow.getColor(col);
                textColor = tableRow.getFontColors(col);
                cssClass = tableRow.getCssClass(col);


            }
            body.append("\n");

        }
        return body;
    }

    public StringBuilder generateSubTotalHTML(TableSubtotalRow[] subTotalRow) {
        TSTableSubTotalDisplay subTotalDisplay = new TSTableSubTotalDisplay(tableBldr);
        return subTotalDisplay.generateSubTotalHtml(subTotalRow);
    }
}
