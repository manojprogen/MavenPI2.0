/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.display.table;

import com.progen.report.data.TableBuilder;
import com.progen.report.data.TableHeaderRow;
import org.apache.poi.ss.util.CellReference;

/**
 *
 * @author progen
 */
public class ExcelHeaderDisplay extends TableDisplay {

    public ExcelHeaderDisplay(TableBuilder builder) {
        super(builder);
    }

    @Override
    public StringBuilder generateHTML() {

        TableHeaderRow[] headerRows;
        headerRows = tableBldr.getHeaderRowData();
        int colCount = tableBldr.getColumnCount();
        int rowCount = tableBldr.getRowCount();

        StringBuilder header = new StringBuilder();

        header.append("<div class=\"scrollable\" id=\"progenTableDiv\">");
        header.append("<Table ID=\"progenTable\" class=\"prgtable jSheet\"   CELLPADDING=\"0\" CELLSPACING=\"1\">");

        //Creating the colgroup tag
//        header.append("<colgroup>");
//        header.append("<col width=\"20px\">");
//        for (int i=0;i<colCount;i++){
//            header.append("<col width=\"240px\">");
//        }
//        header.append("</colgroup>");
        header.append("<thead id=\"theaddiv\">");
        String heading;
        String id;
        String sortImage;
        int rowSpan;
        int colSpan;
        String dispStyle;
        int rowNumber = tableBldr.getTotalRowCount();

        //Creating the Excel Column Headers A, B, C, D, etc...
        header.append("<tr>");
        header.append("<td class=\"jSheetBarCornerParent ui-widget-header\" width=\"20px\" id=\"jSheetBarCornerParent_0_0\">");
        header.append("<div title=\"Select All\" onclick=\"handleSelectAll(" + rowCount + "," + colCount + ")\" class=\"jSheetBarCorner\" id=\"jSheetBarCorner_0_0\" style=\"\">&nbsp;</div>");
        header.append("</td>");

//        header.append("<td class=\"barTop\" style=\"width: 995px;\">");
//        header.append("<div class=\"jSheetBarTopParent\" id=\"jSheetBarTopParent_0_0\" style=\"width: 995px;\">");
//        header.append("<div class=\"jSheetBarTop\" id=\"jSheetBarTop_0_0\" style=\"height: 20px;\">");

        for (int i = 0; i < colCount; i++) {
            String colName = CellReference.convertNumToColString(i);
            header.append("<td class=\"barTop\" align=\"center\" onclick=\"handleBarTopClick(this," + rowCount + ")\" colName=\"" + colName + "\">");
            header.append("<div id=\"barTopMenuDiv" + i + "\" style=\"height: 20px;\" class=\"ui-widget-header\">").append(colName).append("</div>");
            header.append("</td>");
        }
//        header.append("</div></div></td>");
//        header.append("</td>");
        header.append("</tr>");

        //Create the header region for the progen table
        for (TableHeaderRow headerRow : headerRows) {
            header.append("<tr>");
            header.append("<td class=\"barLeft ui-widget-header\" onclick=\"handleBarLeftClick(this)\"><div>");
            header.append(rowNumber + 1);
            header.append("</div></td>");
            for (int i = tableBldr.getFromColumn(); i < colCount; i++) {
                heading = headerRow.getRowData(i);
                sortImage = headerRow.getSortImagePath(i);
//                 id = headerRow.getID(i);
                String cellAddress = CellReference.convertNumToColString(i) + (rowNumber + 1);
                id = "cell_" + cellAddress;

                rowSpan = headerRow.getRowSpan(i);
                colSpan = headerRow.getColumnSpan(i);
                dispStyle = headerRow.getDisplayStyle(i);

                header.append("<td ID=\"" + id + "\" class=\"headerText\" style=\"");
                if (!("\"\"").equalsIgnoreCase(dispStyle)) {
                    header.append(" 'display:").append(dispStyle).append(";");
                }
                header.append("cursor:pointer;height:auto;font-size:11px;background-color:#b4d9ee;\" "
                        + "rowspan=" + rowSpan + " colspan=" + colSpan + " colNum=\"" + i + "\">");
                header.append(heading);
                /*
                 * header.append("<table width=\"100%\" border=\"0\">");
                 * header.append("<tr valign=\"top\">"); //header.append("<td
                 * valign=\"top\" style=\"background-color:#b4d9ee\">"); if (
                 * sortImage != null && ! "".equals(sortImage) ) {
                 * header.append("<td valign=\"top\" id=\"tdRef_" + id + "\"
                 * class=\"headerText\" >" + heading + "");
                 * header.append("</td>"); header.append("<td valign=\"top\"
                 * align=\"right\" width=\"30%\"
                 * style=\"background-color:#b4d9ee\">").append(sortImage).append("</td>");
                 * } else { header.append("<td valign=\"top\" id=\"tdRef_" + id
                 * + "\" class=\"headerText\" align=\"center\" width=\"100%\">"
                 * + heading + ""); header.append("</td>"); //header.append("<td
                 * valign=\"top\" align=\"right\" width=\"1%\"
                 * style=\"background-color:#b4d9ee\">").append("</td>"); }
                 * header.append("</tr>");
                header.append("</table>");
                 */
                header.append("</td>");
            }
            rowNumber++;
            header.append("</tr>");
        }
        tableBldr.setTotalRowCount(rowNumber);
        return super.parentHtml.append(header);
    }

    @Override
    protected void setParentHtml(StringBuilder parentHtml) {
        super.parentHtml = parentHtml;
    }
}
