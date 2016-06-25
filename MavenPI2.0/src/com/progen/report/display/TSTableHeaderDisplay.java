package com.progen.report.display;

import com.progen.report.data.TableBuilder;
import com.progen.report.data.TableHeaderRow;
import com.progen.report.display.table.TableHeaderDisplay;
import java.util.ArrayList;

public class TSTableHeaderDisplay extends TableHeaderDisplay {

    ArrayList<String> timedetailArray;

    public TSTableHeaderDisplay(TableBuilder builder, ArrayList values) {
        super(builder);
        this.timedetailArray = values;
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
        if (!timedetailArray.isEmpty()) {
            header.append("Progen Business Solutions").append("\n").append("\n");
            header.append("Report Title ").append(":").append(timedetailArray.get(5)).append("\n");
            header.append("Created Date :").append(timedetailArray.get(6)).append("\n");
            header.append("Date         :").append(timedetailArray.get(2)).append("\n");
            header.append("Duration     :").append(timedetailArray.get(3)).append("\n");
            header.append("Compare With :").append(timedetailArray.get(4)).append("\n");
            if (timedetailArray.size() > 7) {
                for (int i = 7; i < timedetailArray.size(); i++) {
                    header.append("Filter Applyed On  ").append(timedetailArray.get(i)).append("\n");
                }
            }
            header.append("\n").append("\n").append("\n");
        }


        for (TableHeaderRow headerRow : headerRows) {

            for (int i = tableBldr.getFromColumn(); i < colCount; i++) {
                heading = headerRow.getRowData(i);

                header.append(heading);
                if (i != colCount - 1) {
                    header.append("\t");
                }

            }
            header.append("\n");

        }

        return super.parentHtml.append(header);
    }
}
