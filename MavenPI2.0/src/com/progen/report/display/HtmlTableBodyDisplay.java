package com.progen.report.display;

import com.progen.report.data.TableBuilder;
import com.progen.report.data.TableDataRow;
import com.progen.report.data.TableSubtotalRow;
import com.progen.report.display.table.TableBodyDisplay;
import java.util.ArrayList;

/**
 *
 * @author progen
 */
public class HtmlTableBodyDisplay extends TableBodyDisplay {

    public HtmlTableBodyDisplay(TableBuilder builder) {
        super(builder);
    }

    @Override
    public void setFromOneviewflag(String fromOneview) {
        super.fromOneviewflag = fromOneview;
    }

    @Override
    public StringBuilder generateHtmlForRows(ArrayList<TableDataRow> rowLst, ArrayList<TableSubtotalRow> subtotalRowLst) {
        //
        String flag = super.getHeadlineflag();
        StringBuilder body = new StringBuilder();
        int colCount = tableBldr.getColumnCount();
        String displayStyle;
        String data;
        String id;
        String bgColor = "";
        String href;
        String textColor = "";
        String cssClass = "";
        String spaceAftrNo = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
        int from = 0;
        int subTotalCount = 0;
        String fromOneview = "";
        String getFromtableBuilder = "";
        fromOneview = super.getFromOneviewflag();
        getFromtableBuilder = tableBldr.getGetFromOneview();
        int usedUpSubtotals = 0;
        for (TableDataRow tableRow : rowLst) {
            subTotalCount = generateSubtotal(tableRow, subtotalRowLst, from, body);
            if (subTotalCount != 0) {
                usedUpSubtotals += subTotalCount;
                from += subTotalCount;
            }
            body.append("<tr>");
            for (int col = tableBldr.getFromColumn(); col < colCount; col++) {
                data = tableRow.getRowData(col);
                displayStyle = tableRow.getDisplayStyle(col);
                id = tableRow.getID(col);
                bgColor = tableRow.getColor(col);
                textColor = tableRow.getFontColors(col);
                cssClass = tableRow.getCssClass(col);
                if (!tableRow.getDisplayStyle(col).equals("none")) {
                    if (cssClass.equalsIgnoreCase("dimensionCell")) {
                        body.append("<td id='").append(id).append("'");//.append(" class='").append(cssClass)
                        if ((getFromtableBuilder == null || getFromtableBuilder.equalsIgnoreCase("")) && (fromOneview == null || fromOneview.equalsIgnoreCase(""))) {
                            if (tableBldr.getHtmlCellHeight() != null && !tableBldr.getHtmlCellHeight().equalsIgnoreCase("")) {
                                //body.append(" height=").append(tableBldr.getHtmlCellHeight()).append("px").append("\"");
                                body.append(" rowspan=").append(tableRow.getRowSpan(col)).append(" colspan=").append(tableRow.getColumnSpan(col)).append(" height=").append(tableBldr.getHtmlCellHeight()).append("px").append("  style='font-size:12px;text-align:right;background-color:#E6E6E6;color:#336699;text-align:left;");
                            } else {
                                body.append(" rowspan=").append(tableRow.getRowSpan(col)).append(" colspan=").append(tableRow.getColumnSpan(col)).append("  style='font-size:12px;text-align:right;background-color:#E6E6E6;color:#336699;text-align:left;");
                            }
                        } else {
                            body.append(" rowspan=").append(tableRow.getRowSpan(col)).append(" colspan=").append(tableRow.getColumnSpan(col)).append("  style='font-size:8px;color:#336699;text-align:left;");
                        }
                        if (!("\"\"").equalsIgnoreCase(displayStyle)) {
                            body.append(" display:").append(displayStyle).append(";");
                        }

                        body.append("'>");
//                    if((getFromtableBuilder==null || getFromtableBuilder.equalsIgnoreCase("")) && (fromOneview==null || fromOneview.equalsIgnoreCase(""))){
                        body.append("<Font color=").append(textColor).append(">").append(data).append("</Font>");
//                    }else{
//                        body.append(data);
//                    }
                        body.append(spaceAftrNo);
                        body.append("</td>");
                    } else {
                        body.append("<td id='").append(id).append("'");//.append(" class='").append(cssClass)
                        if ((getFromtableBuilder == null || getFromtableBuilder.equalsIgnoreCase("")) && (fromOneview == null || fromOneview.equalsIgnoreCase(""))) {
                            if (tableBldr.getHtmlCellHeight() != null && !tableBldr.getHtmlCellHeight().equalsIgnoreCase("")) {
                                body.append(" rowspan=").append(tableRow.getRowSpan(col)).append(" colspan=").append(tableRow.getColumnSpan(col)).append(" height=").append(tableBldr.getHtmlCellHeight()).append("px").append("  style='font-size:12px;background-color:	#F5F5F5;color:#000000;text-align:right;");
                            } else {
                                body.append(" rowspan=").append(tableRow.getRowSpan(col)).append(" colspan=").append(tableRow.getColumnSpan(col)).append("  style='font-size:12px;background-color:	#F5F5F5;color:#000000;text-align:right;");
                            }
                        } else {
                            body.append(" rowspan=").append(tableRow.getRowSpan(col)).append(" colspan=").append(tableRow.getColumnSpan(col)).append("  style='font-size:8px;text-align:right;");
                        }
                        if (!("").equalsIgnoreCase(bgColor)) {
                            body.append("background-color:").append(bgColor).append(";");
                        }
                        if (!("\"\"").equalsIgnoreCase(displayStyle)) {
                            body.append(" display:").append(displayStyle).append(";");
                        }
                        body.append("'>");
                        body.append("<Font color=").append(textColor).append(">").append(data).append("</Font>");
                        body.append(spaceAftrNo);
                        body.append("</td>");
                    }
                }

            }
            body.append("</tr>");
        }
        super.updateSubtotalList(usedUpSubtotals, subtotalRowLst);
        return body;
    }
//    background-color:#E6E6E6;
//color:#336699;
//font-family:verdana;
//text-align:left;

    @Override
    public StringBuilder generateSubTotalHTML(TableSubtotalRow[] subTotalRow) {
        HtmlTableSubTotalDisplay subTotalDisplay = new HtmlTableSubTotalDisplay(tableBldr);
        return subTotalDisplay.generateSubTotalHtml(subTotalRow);
    }
}
