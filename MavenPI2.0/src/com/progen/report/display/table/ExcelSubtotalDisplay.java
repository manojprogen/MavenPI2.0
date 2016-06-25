/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.display.table;

import com.progen.query.RTMeasureElement;
import com.progen.report.data.TableBuilder;
import com.progen.report.data.TableSubtotalRow;
import org.apache.poi.hssf.util.CellReference;

/**
 *
 * @author progen
 */
public class ExcelSubtotalDisplay extends TableDisplay {

    public ExcelSubtotalDisplay(TableBuilder builder) {
        super(builder);
    }

    @Override
    public StringBuilder generateHTML() {
        StringBuilder subTotalHtml = new StringBuilder();
        TableSubtotalRow[] subTotalRow = tableBldr.getSubtotalRowDataLastRow();
        subTotalHtml.append(this.generateSubTotalHtml(subTotalRow));
        subTotalRow = tableBldr.getGrandtotalRowData();

//        for ( int i=0; i<subTotalRow.length; i++ )
//        {
//            row = subTotalRow[i];
//            subTotalHtml.append("<Tr ID=\"").append(row.getSubtotalType()).append("\" ");
//            for ( int col=0; col < colCount; col++ )
//            {
//                subTotalHtml.append("<Td  ID=\"").append(row.getID(col)).append("\" align=\"").append(row.getAlignment(col)).append("\">");
//                subTotalHtml.append("<B>");
//                subTotalHtml.append(row.getRowData(col));
//                subTotalHtml.append("</B></Td>");
//            }
//            subTotalHtml.append("</Tr>");
//        }
        subTotalHtml.append(this.generateSubTotalHtml(subTotalRow));
        subTotalHtml.append("</tbody>");
        subTotalHtml.append("</Table>");
        subTotalHtml.append("</div>");

        return super.parentHtml.append(subTotalHtml);
    }

    protected StringBuilder generateSubTotalHtml(TableSubtotalRow[] subTotalRow) {
        TableSubtotalRow row;
        StringBuilder subTotalHtml = new StringBuilder();
        String cssClass = "";
        int colCount;
        String spaceAftrNo = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
        int rowNumber = tableBldr.getTotalRowCount();
        String subTotalType = "";
        for (int i = 0; i < subTotalRow.length; i++) {
            row = subTotalRow[i];
            colCount = row.getColumnCount();
            subTotalHtml.append("<Tr ID=\"").append(row.getSubtotalType()).append("\">");
            subTotalHtml.append("<td class=\"barLeft ui-widget-header\"><div>");
            subTotalHtml.append(rowNumber + 1);
            subTotalHtml.append("</div></td>");
            rowNumber++;
            for (int col = tableBldr.getFromColumn(); col < colCount; col++) {
                cssClass = row.getCssClass(col);
                String measId = row.getColumnId(col);
                if (RTMeasureElement.isRunTimeExcelColumn(measId)) {
                    tableBldr.setSubTotalRows(measId, rowNumber);
                }
                subTotalType = row.getSubtotalType();
                // 
                String cellAddress = CellReference.convertNumToColString(col) + (rowNumber);
                if (!subTotalType.equalsIgnoreCase("ST")) {
                    cellAddress = "";
                } else {
                    cellAddress = "_" + cellAddress;
                }
                subTotalHtml.append("<Td  ID=\"").append(row.getID(col) + cellAddress).append("\" ").append("class=\"").append(cssClass).append("\"");
                if (!("\"\"").equalsIgnoreCase(row.getDisplayStyle(col))) {
                    subTotalHtml.append(" style=\"display:").append(row.getDisplayStyle(col)).append(";\"");
                }
                subTotalHtml.append(">");

                subTotalHtml.append(row.getRowData(col));
                subTotalHtml.append(spaceAftrNo);
                subTotalHtml.append("</Td>");
            }
            subTotalHtml.append("</Tr>");
        }
        tableBldr.setTotalRowCount(rowNumber);
        return subTotalHtml;
    }

    @Override
    protected void setParentHtml(StringBuilder parentHtml) {
        super.parentHtml = parentHtml;
    }
}
