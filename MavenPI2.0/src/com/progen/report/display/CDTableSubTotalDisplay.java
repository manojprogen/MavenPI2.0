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
public class CDTableSubTotalDisplay extends TableSubtotalDisplay {

    private String delimiter;
    private String textIdentifier;

    public CDTableSubTotalDisplay(TableBuilder builder) {
        super(builder);
    }

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


        return super.parentHtml.append(subTotalHtml);
    }

    @Override
    public StringBuilder generateSubTotalHtml(TableSubtotalRow[] subTotalRow) {
        //
        TableSubtotalRow row;
        StringBuilder subTotalHtml = new StringBuilder();
//        String cssClass = "";
        int colCount;
//        String spaceAftrNo = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";

        for (int i = 0; i < subTotalRow.length; i++) {
            row = subTotalRow[i];
            colCount = row.getColumnCount();

//            subTotalHtml.append(colCount);

//            subTotalHtml.append("<Tr ID=\"").append(row.getSubtotalType()).append("\" >");
            for (int col = tableBldr.getFromColumn(); col < colCount; col++) {
//                cssClass = row.getCssClass(col);
//                if (cssClass.equalsIgnoreCase("subTotalCell")) {
//                    subTotalHtml.append("<Td  ID=\"").append(row.getID(col) + "\"").append(" style='font-size:12px;text-align:left;background-color:#B4D9EE;color:#336699;'>");
                subTotalHtml.append(getTextIdentifier()).append((row.getRowData(col)).replace(",", "")).append(getTextIdentifier());
                if (col != colCount - 1) {
                    subTotalHtml.append(getDelimiter());
                }
//                    subTotalHtml.append(spaceAftrNo);
//                    subTotalHtml.append("</Td>");
//                } else if(cssClass.equalsIgnoreCase("tabFooter")){
//                    subTotalHtml.append("<Td  ID=\"").append(row.getID(col) + "\"").append(" style='font-size:12px;background-color:#E6E6E6;color:#000000;font-weight:bold;text-align:right;'>");
//                    subTotalHtml.append(row.getRowData(col));
//                    subTotalHtml.append(spaceAftrNo);
//                    subTotalHtml.append("</Td>");
//                }else{
//                    subTotalHtml.append("<Td  ID=\"").append(row.getID(col) + "\"").append(">");//.append(" style='background-color:#B4D9EE;'>");
//                    subTotalHtml.append(row.getRowData(col));
//                    subTotalHtml.append(spaceAftrNo);
//                    subTotalHtml.append("</Td>");
//                }
            }
            subTotalHtml.append("\n");
// background-color:#E6E6E6;
//color:#000000;
//font-size:11px;
//font-weight:bold;
//text-align:right;
//            subTotalHtml.append("</Tr>");
        }

        return subTotalHtml;

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
