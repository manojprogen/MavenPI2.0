/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.display;

import com.progen.report.data.TableBuilder;
import com.progen.report.data.TableHeaderRow;
import com.progen.report.display.table.TableHeaderDisplay;
import java.util.ArrayList;

/**
 *
 * @author progen
 */
public class CDTableHeaderDisplay extends TableHeaderDisplay {

    private String delimiter;
    private String textIdentifier;
    ArrayList<String> timedetailArray;

    public CDTableHeaderDisplay(TableBuilder builder, ArrayList values) {
        super(builder);
        this.timedetailArray = values;
    }

    @Override
    public StringBuilder generateHTML() {
        TableHeaderRow[] headerRows;
        headerRows = tableBldr.getHeaderRowData();
        int colCount = tableBldr.getColumnCount();
        StringBuilder header = new StringBuilder();

        // header.append("<div class=\"scrollable\" id=\"progenTableDiv\">");
//        header.append("<Table ID=\"progenTable\"  border='1' style='border-collapse:separate;border-spacing:0px;-moz-border-radius: 6px 6px 6px 6px;' CELLPADDING=\"0\" CELLSPACING=\"1\">");
//        header.append("<thead id=\"theaddiv\">");
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

                header.append(getTextIdentifier()).append(heading).append(getTextIdentifier());
                if (i != colCount - 1) {
                    header.append(getDelimiter());
                }
                //sortImage = headerRow.getSortImagePath(i);
//                id = headerRow.getID(i);
//                rowSpan = headerRow.getRowSpan(i);
//                colSpan = headerRow.getColumnSpan(i);
//                dispStyle = headerRow.getDisplayStyle(i);

//                header.append("<th ID=\"" + id + "\" style=\"background-color:#B4D9EE;display:" + dispStyle + "; \" rowspan=" + rowSpan + " colspan=" + colSpan + ">");//colspan="+colSpan+"
//                header.append("<table width=\"100%\" border=\"0\">");
//                header.append("<tr valign=\"top\">");
//                header.append("<td valign=\"top\" id=\"tdRef_").append(id).append("\" style='font-size: 12px;-moz-border-radius: 6px 6px 6px 6px;' align=\"center\" width=\"100%\"><b>").append(heading).append("</b>");
//                header.append("</td>");
//                header.append("</tr>");
//                header.append("</table>");
//                header.append("</th>");

            }
            header.append("\n");
//            header.append("</tr>");
        }

        return super.parentHtml.append(header);
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
