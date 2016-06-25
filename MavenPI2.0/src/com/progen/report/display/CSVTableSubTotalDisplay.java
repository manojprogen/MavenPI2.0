package com.progen.report.display;

import com.progen.report.data.TableBuilder;
import com.progen.report.data.TableSubtotalRow;
import com.progen.report.display.table.TableSubtotalDisplay;

public class CSVTableSubTotalDisplay extends TableSubtotalDisplay {

    public CSVTableSubTotalDisplay(TableBuilder builder) {
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


        for (int i = 0; i < subTotalRow.length; i++) {
            row = subTotalRow[i];
            colCount = row.getColumnCount();




            for (int col = tableBldr.getFromColumn(); col < colCount; col++) {

                subTotalHtml.append((row.getRowData(col)).replace(",", "").replace("%", "").replace("$", "").replace("Yen", "").replace("Rs", "").replace("Euro", ""));
                if (col != colCount - 1) {
                    subTotalHtml.append(",");
                }

            }
            subTotalHtml.append("\n");
        }

        return subTotalHtml;

    }
}
