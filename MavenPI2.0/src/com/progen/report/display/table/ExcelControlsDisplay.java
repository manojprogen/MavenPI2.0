/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.display.table;

import com.progen.report.data.TableBuilder;

/**
 *
 * @author progen
 */
public class ExcelControlsDisplay extends TableDisplay {

    public ExcelControlsDisplay(TableBuilder tblBuilder) {
        super(tblBuilder);
    }

    @Override
    protected StringBuilder generateHTML() {
        StringBuilder html = new StringBuilder();

        //Building the buttons
        html.append("<div class=\"jSheetControls ui-widget-header ui-corner-to\" id=\"jSheetControls_0\">");
        if (tableBldr.isRTExcelColsAvailable() || tableBldr.isTargetEntryApplicable()) {
            html.append("<table cellspacing=\"0\" cellpadding=\"0\" border=\"0\" style=\"display: block;\" id=\"firstRowTable\">");
            html.append("<tbody>");
            html.append("<tr>");
            if (tableBldr.isRTExcelColsAvailable()) {
                html.append("<td id=\"buttonsDiv\">");
                html.append("<input type=\"button\" onclick=\"saveExcel()\" value=\"Save Data\" style=\"width: auto;\" class=\"navtitle-hover\">");
                html.append("<input type=\"button\" onclick=\"exportExcel()\" value=\"Export\" style=\"width: auto;\" class=\"navtitle-hover\">");
                html.append("<input type=\"button\" onclick=\"importExcel()\" value=\"Import\" style=\"width: auto;\" class=\"navtitle-hover\">");
                html.append("</td>");
            }
            if (tableBldr.isTargetEntryApplicable()) {
                html.append("<td id=\"targetDiv\">");
                html.append("<input type=\"button\" onclick=\"openMeasuresDialag(reportId)\" value=\"Add Target\" style=\"width: auto;\" class=\"navtitle-hover\">");
                html.append("</td>");
            }
            html.append("</tr>");
            html.append("</tbody>");
            html.append("</table>");
        }

        //Building the formula bar
        html.append("<table cellspacing=\"0\" cellpadding=\"0\" border=\"0\">");
        html.append("<tbody>");
        html.append("<tr>");
        html.append("<td class=\"jSheetControls_loc ui-widget-header ui-corner-top\" id=\"jSheetControls_loc\" style=\"width: 35px; text-align: right;\"></td>");
        html.append("<td>");
        html.append("<textarea class=\"jSheetControls_formula ui-widget-content\" id=\"jSheetControls_formula\">");
        html.append("</textarea>");
        html.append("</td>");
        html.append("<td>");
        html.append("<textarea style=\"display: none;\" id=\"jSheetCopyArea\"></textarea>");
        html.append("</td>");
        html.append("<td>");
        html.append("<textarea style=\"display: none;\" id=\"jSheetDebugArea\"></textarea>");
        html.append("</td>");
        html.append("</tr>");
        html.append("</tbody>");
        html.append("</table>");
        html.append("</div>");



        return html;
    }

    @Override
    protected void setParentHtml(StringBuilder parentHtml) {
        super.parentHtml = parentHtml;
    }
}
