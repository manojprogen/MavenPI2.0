/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.display.table;

import com.progen.report.data.TableBuilder;
import com.progen.report.data.TableDataRow;
import com.progen.report.data.TableSubtotalRow;
import java.util.ArrayList;
import org.apache.log4j.Logger;
import org.apache.poi.ss.util.CellReference;

/**
 *
 * @author progen
 */
public class ExcelBodyDisplay extends TableBodyDisplay {

    public static Logger logger = Logger.getLogger(ExcelBodyDisplay.class);

    public ExcelBodyDisplay(TableBuilder builder) {
        super(builder);
    }

    @Override
    public StringBuilder generateHTML() {
        StringBuilder body = new StringBuilder();
        TableDataRow tableRow;
        int fromRow, toRow;
        int batchCount = 0;
        ArrayList<TableDataRow> rowLst = new ArrayList<TableDataRow>();


        fromRow = tableBldr.getFromRow();
        toRow = tableBldr.getToRow();
        body.append("</thead>");
        body.append("<tbody id=\"myTableBody\" class=\"myTableBodyClass\" style=\"overflow-x:hidden\">");

        for (int row = fromRow; row < toRow; row++) {
            tableRow = (TableDataRow) tableBldr.getRowData(row);
            if (tableRow.printSubTotals()) {
                TableSubtotalRow[] subtotalRows = tableBldr.getSubtotalRowData();
                this.addCellSpanToRows(rowLst);
                body.append(this.generateHtmlForRows(rowLst, null));
                body.append(generateSubTotalHTML(subtotalRows));
                rowLst.clear();
            }
            rowLst.add(tableRow);
            batchCount++;
            if (batchCount >= TableDisplay.BATCH_SIZE) {
                batchCount = 0;
                this.addCellSpanToRows(rowLst);
                body.append(this.generateHtmlForRows(rowLst, null));
                this.flushToOutputStream(super.parentHtml.append(body));
                body = new StringBuilder();
                super.parentHtml = new StringBuilder();
                rowLst.clear();
            }
        }
        if (!rowLst.isEmpty()) {
            this.addCellSpanToRows(rowLst);
            body.append(this.generateHtmlForRows(rowLst, null));
            rowLst.clear();
        }
        return super.parentHtml.append(body);
    }

    @Override
    protected StringBuilder generateSubTotalHTML(TableSubtotalRow[] subTotalRow) {
        ExcelSubtotalDisplay subTotalDisplay = new ExcelSubtotalDisplay(tableBldr);
        return subTotalDisplay.generateSubTotalHtml(subTotalRow);
    }
//    private StringBuilder generateSubTotalHTML(TableSubtotalRow[] subTotalRow)
//    {
//        TableSubtotalDisplay subTotalDisplay = new TableSubtotalDisplay(tableBldr);
//        return subTotalDisplay.generateSubTotalHtml(subTotalRow);
//    }

//    private void addCellSpanToRows(ArrayList<TableDataRow> rows)
//    {
//        Set<TableCellSpan> cellSpanSet = tableBldr.getCellSpans();
//        Set<TableCellSpan> cellSpanSetToRemove = new HashSet<TableCellSpan>();
//        HashSet<TableCellSpan> cellSpanSetToAdd;
//
//        int dimColCount = tableBldr.getViewByCount() - 1;
//        if ( ! cellSpanSet.isEmpty() )
//        {
//            for ( TableDataRow row : rows )
//            {
//                cellSpanSetToAdd = new HashSet<TableCellSpan>();
//                for ( int column = 0; column < dimColCount; column++ )
//                {
//                    Iterator<TableCellSpan> spanSet = Iterables.filter(cellSpanSet,TableCellSpan.getCellIdPredicate(row.getID(column))).iterator();
//                    if ( spanSet.hasNext() )
//                    {
//                        TableCellSpan span = spanSet.next();
//                        cellSpanSetToAdd.add(span);
//                        cellSpanSetToRemove.add(span);
//                    }
//                }
//                row.setCellSpans(cellSpanSetToAdd);
//            }
//            for ( TableCellSpan cellSpan : cellSpanSetToRemove )
//            {
//               tableBldr.removeCellSpanFromSet(cellSpan);
//            }
//        }
//     }
//    private void flushToOutputStream(StringBuilder body)
//    {
//        try {
//            out.print(body); //.toString());
//            out.flush();
//            out.clearBuffer();
//        } catch (IOException ex) {
//            logger.error("Exception:",ex);
//        }
//    }
    @Override
    protected StringBuilder generateHtmlForRows(ArrayList<TableDataRow> rowLst, ArrayList<TableSubtotalRow> subtotalRowLst) {
        StringBuilder body = new StringBuilder();
        String cssClass = "";
        int colCount = tableBldr.getColumnCount();
        String displayStyle;
        String data;
        String id;
        String bgColor = "";
        String fontColor = "";
        String href;
        String formula = null;
        String spaceAftrNo = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
        Boolean editable = false;
        int rowNumber = tableBldr.getTotalRowCount();
        int currentRow = 0;
        for (TableDataRow tableRow : rowLst) {
            rowNumber++;
            currentRow++;
            body.append("<tr onMouseOut=\"parent.mouseOut(this)\" onMouseOver=\"parent.mouseOn(this)\">");
            body.append("<td class=\"barLeft ui-widget-header\" rowNumber=\"" + (rowNumber) + "\" onclick=\"handleBarLeftClick(this," + colCount + ")\">");
            body.append("<div id=\"barLeft" + rowNumber + "\">");
            body.append(rowNumber);
            body.append("</div></td>");
            for (int col = tableBldr.getFromColumn(); col < colCount; col++) {
                data = tableRow.getRowData(col);
                formula = tableRow.getFormula(col);
                displayStyle = tableRow.getDisplayStyle(col);
//               id = tableRow.getID(col);
                String cellAddress = CellReference.convertNumToColString(col) + (rowNumber);
                id = "cell_" + cellAddress;
                cssClass = tableRow.getCssClass(col);
                bgColor = tableRow.getColor(col);
                fontColor = tableRow.getFontColors(col);
                editable = tableRow.isEditable(col);
                body.append("<td id='").append(id).append("'").append(" class='" + cssClass + "'").append(" rowspan=").append(tableRow.getRowSpan(col)).append(" colspan=").append(tableRow.getColumnSpan(col)).append(" cellAddress='").append(cellAddress).append("'").append(" colNum='" + col + "'").append(" rowNum='" + tableRow.getRowNumber() + "'");
                body.append(" style='");
                if (!("\"\"").equalsIgnoreCase(displayStyle)) {
                    body.append(" display:").append(displayStyle).append(";");
                }
                if (!("").equalsIgnoreCase(bgColor)) {
                    body.append("background-color:").append(bgColor).append(";");
                }

                if (fontColor != null && !("".equals(fontColor))) {
                    body.append(";color:").append(fontColor);
                }
                body.append("'");
                String comment = tableRow.getComment(col);
                if (comment != null) {
                    body.append(" title='" + comment + "'");
                    body.append(" comment='" + comment + "'");
                }
                if (formula != null) {   // Formula Cell. Add a formula attribute
                    formula = tableBldr.getDisplayFormula(formula);
                    body.append(" formula='").append(formula).append("'");
                }
                if (tableRow.isEditable(col)) {           // Mention whether the cell is editable or not. Only Runtime Measure cells are editable
                    body.append(" editable='").append(tableRow.isEditable(col)).append("'");
                }
                body.append(" onclick='handleCellSelect(this)'");

                if (currentRow == rowLst.size()) {
                    body.append(" lastRow='true'");
                }

                if (col == (colCount - 1)) {
                    body.append(" lastCol='true'");
                }

                body.append(">");
                href = tableRow.getAnchors(col);
                if (!"#".equals(href)) {
                    body.append("<A href='javascript:parent.submiturls(\"" + tableRow.getAnchors(col) + "\")'").append("  target=\"_parent\" style=\"text-decoration:none\">");
                    body.append(data);
                    body.append("</A>");
                } else {
                    body.append(data);
                    if (!editable) {
                        body.append(spaceAftrNo);
                    }
                }
                body.append("</td>");
            }
            body.append("</tr>");
            tableBldr.addRowMapping(rowNumber, tableRow.getRowNumber());
        }
        tableBldr.setTotalRowCount(rowNumber);
        return body;
    }
//    @Override
//    protected void setParentHtml(StringBuilder parentHtml) {
//        super.parentHtml = parentHtml;
//    }
    /*
     * private StringBuilder substituteCellSpans(StringBuilder body) { //
     * ProgenLog.log(ProgenLog.FINE,this,"flushHtmlToStream", "Enter
     * flushHtmlToStream "+System.currentTimeMillis()); logger.info("Enter
     * flushHtmlToStream "+System.currentTimeMillis()); Set<TableCellSpan>
     * cellSpanSet = facade.getCellSpans(); Set<TableCellSpan>
     * cellSpanSetToRemove = new HashSet<TableCellSpan>(); int count = 2;
     * Pattern cellIdPattern; Matcher matcher;
     *
     * StringBuilder pattern = new StringBuilder(); StringBuilder replacement =
     * new StringBuilder();
     *
     *
     * for ( TableCellSpan cellSpan : cellSpanSet ) {
     * pattern.append("'").append(cellSpan.getCellId()).append("'");
     * replacement.append("'").append(cellSpan.getCellId()).append("'
     * rowspan=").append(cellSpan.getRowSpan()).append(" ");
     * cellSpanSetToRemove.add(cellSpan); cellIdPattern =
     * Pattern.compile(pattern.toString()); matcher =
     * cellIdPattern.matcher(body); body = new
     * StringBuilder(matcher.replaceAll(replacement.toString())); pattern = new
     * StringBuilder(); replacement = new StringBuilder(); }
     *
     * for ( TableCellSpan cellSpan : cellSpanSetToRemove ) {
     * facade.removeCellSpanFromSet(cellSpan); } //
     * ProgenLog.log(ProgenLog.FINE,this,"flushHtmlToStrem", "Exit
     * flushHtmlToStream "+System.currentTimeMillis()); logger.info("Exit
     * flushHtmlToStream "+System.currentTimeMillis()); return body;
    }
     */
}
