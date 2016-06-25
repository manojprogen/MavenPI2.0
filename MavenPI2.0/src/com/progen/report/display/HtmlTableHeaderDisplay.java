package com.progen.report.display;

import com.progen.report.data.TableBuilder;
import com.progen.report.data.TableHeaderRow;
import com.progen.report.display.table.TableHeaderDisplay;

/**
 *
 * @author progen
 */
public class HtmlTableHeaderDisplay extends TableHeaderDisplay {

    public HtmlTableHeaderDisplay(TableBuilder builder) {
        super(builder);
    }

    @Override
    public void setHeadlineflag(String Headlineflag) {
        super.headlineflag = Headlineflag;
    }

    @Override
    public void setFromOneviewflag(String fromOneview) {
        super.fromOneviewflag = fromOneview;
    }

    @Override
    public StringBuilder generateHTML() {
        TableHeaderRow[] headerRows;
        String flag = super.getHeadlineflag();
        String oneview = "";
        oneview = super.getFromOneviewflag();
        headerRows = tableBldr.getHeaderRowData();
        int colCount = tableBldr.getColumnCount();
        StringBuilder header = new StringBuilder();
        String isoneview = tableBldr.getGetFromOneview();
        // header.append("<div class=\"scrollable\" id=\"progenTableDiv\">");
//        if(super.)
        if (isoneview != null && isoneview.equalsIgnoreCase("fromOneview")) {
        } else {
            if (flag != null) {
                if (!flag.equalsIgnoreCase("fromHeadline")) {
                    header.append("<Table ID=\"progenTable\"  border='1' style='border-collapse:separate;border-spacing:0px;-moz-border-radius: 6px 6px 6px 6px;' CELLPADDING=\"0\" CELLSPACING=\"1\">");
                    header.append("<thead id=\"theaddiv\">");
                } else {
                    header.append("<Table id='headline' class='tablesorter'");
                    header.append("<thead>");
                }
            }
            String heading;
            String id;
            String sortImage;
            int rowSpan;
            int colSpan;
            String dispStyle;

            for (TableHeaderRow headerRow : headerRows) {
                if (oneview != null && !oneview.equalsIgnoreCase("")) {
                    header.append("<thead>");
                }
                header.append("<tr valign=\"top\">");
                for (int i = tableBldr.getFromColumn(); i < colCount; i++) {
                    heading = headerRow.getRowData(i);
                    //sortImage = headerRow.getSortImagePath(i);
                    id = headerRow.getID(i);
                    rowSpan = headerRow.getRowSpan(i);
                    colSpan = headerRow.getColumnSpan(i);
                    dispStyle = headerRow.getDisplayStyle(i);
                    if (!headerRow.getDisplayStyle(i).equalsIgnoreCase("none")) {
                        if (oneview == null || oneview.equalsIgnoreCase("")) {
                            if (tableBldr.getHtmlCellHeight() != null && !tableBldr.getHtmlCellHeight().equalsIgnoreCase("")) {
                                header.append("<th ID=\"" + id + "\" style=\"background-color:#B4D9EE;display:" + dispStyle + ";  rowspan=\"" + rowSpan + "\" colspan=\"" + colSpan + "\"  height=\"" + tableBldr.getHtmlCellHeight() + "px\">");//colspan="+colSpan+"
                            } else {
                                header.append("<th ID=\"" + id + "\" style=\"background-color:#B4D9EE;display:" + dispStyle + ";  rowspan=\"" + rowSpan + "\" colspan=\"" + colSpan + "\">");//colspan="+colSpan+"
                            }
                            header.append("<table width=\"100%\" border=\"0\">");
                            header.append("<tr valign=\"top\">");
                            header.append("<td valign=\"top\" id=\"tdRef_").append(id).append("\" style='font-size: 12px;-moz-border-radius: 6px 6px 6px 6px;' align=\"center\" width=\"100%\"><b>").append(heading).append("</b>");
                            header.append("</td>");
                            header.append("</tr>");
                            header.append("</table>");
                            header.append("</th>");
                        } else {
                            header.append("<th ID=\"" + id + "\" style=\"display:" + dispStyle + "  rowspan=\"" + rowSpan + "\" colspan=\"" + colSpan + "\"  align='center'>");//colspan="+colSpan+"
                            header.append("<strong>").append(heading).append("</strong>");
                            header.append("</th>");

                        }
                    }
                }
                // if(!flag.equalsIgnoreCase("fromHeadline"))

                header.append("</tr>");
                if (oneview != null && !oneview.equalsIgnoreCase("")) {
                    header.append("</thead>");
                }
            }
        }
        return super.parentHtml.append(header);

    }

    public StringBuilder generateHTML1() {
        TableHeaderRow[] headerRows;
        String flag = super.getHeadlineflag();
        String oneview = "";
        oneview = super.getFromOneviewflag();
        headerRows = tableBldr.getHeaderRowData();
        int colCount = tableBldr.getColumnCount();
        StringBuilder header = new StringBuilder();

        // header.append("<div class=\"scrollable\" id=\"progenTableDiv\">");
//        if(super.)
        if (flag != null) {
            if (!flag.equalsIgnoreCase("fromHeadline")) {
                header.append("<Table ID=\"progenTable\"  border='1' style='border-collapse:separate;border-spacing:0px;-moz-border-radius: 6px 6px 6px 6px;' CELLPADDING=\"0\" CELLSPACING=\"1\">");
                header.append("<thead id=\"theaddiv\">");
            } else {
                header.append("<Table id='headline' class='tablesorter'");
                header.append("<thead>");
            }
        }
        String heading;
        String id;
        String sortImage;
        int rowSpan;
        int colSpan;
        String dispStyle;

        for (TableHeaderRow headerRow : headerRows) {
            if (oneview != null && !oneview.equalsIgnoreCase("")) {
                header.append("<thead>");
            }
            header.append("<tr valign=\"top\">");
            for (int i = tableBldr.getFromColumn(); i < colCount; i++) {
                heading = headerRow.getRowData(i);
                //sortImage = headerRow.getSortImagePath(i);
                id = headerRow.getID(i);
                rowSpan = headerRow.getRowSpan(i);
                colSpan = headerRow.getColumnSpan(i);
                dispStyle = headerRow.getDisplayStyle(i);
                if (!headerRow.getDisplayStyle(i).equalsIgnoreCase("none")) {
                    if (oneview == null || oneview.equalsIgnoreCase("")) {
                        if (tableBldr.getHtmlCellHeight() != null && !tableBldr.getHtmlCellHeight().equalsIgnoreCase("")) {
                            header.append("<th ID=\"" + id + "\" style=\"background-color:#B4D9EE;display:" + dispStyle + ";  rowspan=\"" + rowSpan + "\" colspan=\"" + colSpan + "\"  height=\"" + tableBldr.getHtmlCellHeight() + "px\">");//colspan="+colSpan+"
                        } else {
                            header.append("<th ID=\"" + id + "\" style=\"background-color:#B4D9EE;display:" + dispStyle + ";  rowspan=\"" + rowSpan + "\" colspan=\"" + colSpan + "\">");//colspan="+colSpan+"
                        }
                        header.append("<table width=\"100%\" border=\"0\">");
                        header.append("<tr valign=\"top\">");
                        header.append("<td valign=\"top\" id=\"tdRef_").append(id).append("\" style='font-size: 12px;-moz-border-radius: 6px 6px 6px 6px;' align=\"center\" width=\"100%\"><b>").append(heading).append("</b>");
                        header.append("</td>");
                        header.append("</tr>");
                        header.append("</table>");
                        header.append("</th>");
                    } else {
                        header.append("<th ID=\"" + id + "\" style=\"display:" + dispStyle + "  rowspan=\"" + rowSpan + "\" colspan=\"" + colSpan + "\"  align='center'>");//colspan="+colSpan+"
                        header.append("<strong>").append(heading).append("</strong>");
                        header.append("</th>");

                    }
                }
            }
            // if(!flag.equalsIgnoreCase("fromHeadline"))

            header.append("</tr>");
            if (oneview != null && !oneview.equalsIgnoreCase("")) {
                header.append("</thead>");
            }
        }
        return super.parentHtml.append(header);
    }
}
