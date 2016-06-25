package com.progen.report.display;

import com.progen.report.data.TableBuilder;
import com.progen.report.data.TableSubtotalRow;
import com.progen.report.display.table.TableSubtotalDisplay;

/**
 *
 * @author progen
 */
public class HtmlTableSubTotalDisplay extends TableSubtotalDisplay {

    public HtmlTableSubTotalDisplay(TableBuilder builder) {
        super(builder);
    }

    @Override
    public void setFromOneviewflag(String fromOneview) {
        super.fromOneviewflag = fromOneview;
    }

    @Override
    public StringBuilder generateSubTotalHtml(TableSubtotalRow[] subTotalRow) {
        //
        TableSubtotalRow row;
        StringBuilder subTotalHtml = new StringBuilder();
        String cssClass = "";
        int colCount;
        String spaceAftrNo = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
        String fromOneview = "";
        String getFromtableBuilder = "";
        fromOneview = super.getFromOneviewflag();
        getFromtableBuilder = tableBldr.getGetFromOneview();
        for (int i = 0; i < subTotalRow.length; i++) {
            row = subTotalRow[i];
            colCount = row.getColumnCount();
            subTotalHtml.append("<Thead><Tr ID=\"").append(row.getSubtotalType()).append("\" style='background-color:(230,230,230)'>");
            for (int col = tableBldr.getFromColumn(); col < colCount; col++) {
                cssClass = row.getCssClass(col);
                if (!row.getDisplayStyle(col).equals("none")) {
                    if (cssClass.equalsIgnoreCase("subTotalCell")) {
                        if ((getFromtableBuilder == null || getFromtableBuilder.equalsIgnoreCase("")) && (fromOneview == null || fromOneview.equalsIgnoreCase(""))) {
                            if (tableBldr.getHtmlCellHeight() != null && !tableBldr.getHtmlCellHeight().equalsIgnoreCase("")) {
                                subTotalHtml.append("<Td  ID=\"").append(row.getID(col) + "\"").append(" style='font-size:12px;text-align:left;background-color:#B4D9EE;color:#336699;").append(" height=").append(tableBldr.getHtmlCellHeight()).append("px");
                            } else {
                                subTotalHtml.append("<Td  ID=\"").append(row.getID(col) + "\"").append(" style='font-size:12px;text-align:left;background-color:#B4D9EE;color:#336699;");
                            }
                        } else {
                            subTotalHtml.append("<Td  ID=\"").append(row.getID(col) + "\"").append(" style='font-size:8px;font-weight:bold;text-align:left;color:#336699;");
                        }
                        if (!("\"\"").equalsIgnoreCase(row.getDisplayStyle(col))) {
                            subTotalHtml.append("display:").append(row.getDisplayStyle(col)).append(";");
                        }
                        subTotalHtml.append("'>");
                        if ((getFromtableBuilder == null || getFromtableBuilder.equalsIgnoreCase("")) && (fromOneview == null || fromOneview.equalsIgnoreCase(""))) {
                            subTotalHtml.append(row.getRowData(col));
                        } else {
                            subTotalHtml.append("<Font color=").append("").append("><strong>").append(row.getRowData(col)).append("</strong></Font>");
                        }
                        subTotalHtml.append(spaceAftrNo);
                        subTotalHtml.append("</Td>");
                    } else if (cssClass.equalsIgnoreCase("tabFooter")) {
                        if ((getFromtableBuilder == null || getFromtableBuilder.equalsIgnoreCase("")) && (fromOneview == null || fromOneview.equalsIgnoreCase(""))) {
                            if (tableBldr.getHtmlCellHeight() != null && !tableBldr.getHtmlCellHeight().equalsIgnoreCase("")) {
                                subTotalHtml.append("<Td  ID=\"").append(row.getID(col) + "\"").append(" style='font-size:12px;background-color:#E6E6E6;color:#000000;font-weight:bold;text-align:right;").append(" height=").append(tableBldr.getHtmlCellHeight()).append("px");
                            } else {
                                subTotalHtml.append("<Td  ID=\"").append(row.getID(col) + "\"").append(" style='font-size:12px;background-color:#E6E6E6;color:#000000;font-weight:bold;text-align:right;");
                            }
                        } else {
                            subTotalHtml.append("<Td  ID=\"").append(row.getID(col) + "\"").append(" style='font-size:8px;font-weight:bold;text-align:right;");
                        }
                        if (!("\"\"").equalsIgnoreCase(row.getDisplayStyle(col))) {
                            subTotalHtml.append("display:").append(row.getDisplayStyle(col)).append(";");
                        }
                        subTotalHtml.append("'>");
                        if ((getFromtableBuilder == null || getFromtableBuilder.equalsIgnoreCase("")) && (fromOneview == null || fromOneview.equalsIgnoreCase(""))) {
                            subTotalHtml.append(row.getRowData(col));
                        } else {
                            subTotalHtml.append("<Font color=").append("").append("><strong>").append(row.getRowData(col)).append("</strong></Font>");
                        }
                        subTotalHtml.append(spaceAftrNo);
                        subTotalHtml.append("</Td>");
                    } else {
                        subTotalHtml.append("<Td  ID=\"").append(row.getID(col) + "\"");
                        if (!("\"\"").equalsIgnoreCase(row.getDisplayStyle(col))) {
                            subTotalHtml.append(" style=\"display:").append(row.getDisplayStyle(col)).append(";\"");
                        }
                        subTotalHtml.append(">");
                        subTotalHtml.append(row.getRowData(col));
                        subTotalHtml.append(spaceAftrNo);
                        subTotalHtml.append("</Td>");
                    }
                }
            }
// background-color:#E6E6E6;
//color:#000000;
//font-size:11px;
//font-weight:bold;
//text-align:right;
            subTotalHtml.append("</Tr></Thead>");
        }
        return subTotalHtml;
    }
}
