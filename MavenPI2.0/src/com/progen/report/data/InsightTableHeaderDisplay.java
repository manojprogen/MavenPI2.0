/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.data;

import com.progen.report.PbReportCollection;
import com.progen.report.display.table.TableDisplay;
import com.progen.report.display.util.NumberFormatter;
import com.progen.report.query.PbReportQuery;
import com.progen.report.query.QueryExecutor;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.log4j.Logger;
import prg.db.PbDb;
import prg.db.PbReturnObject;

/**
 *
 * @author progen
 */
public class InsightTableHeaderDisplay extends TableDisplay {

    public static Logger logger = Logger.getLogger(InsightTableHeaderDisplay.class);

    public InsightTableHeaderDisplay(TableBuilder builder) {
        super(builder);
    }

    @Override
    protected StringBuilder generateHTML() {
        TableHeaderRow[] headerRows;
        DataFacade facade = tableBldr.facade;
        ArrayList<String> displayCols = facade.getDisplayColumns();
        headerRows = tableBldr.getHeaderRowData();
        int colCount = tableBldr.getColumnCount();
        String heading;
        int rowSpan;
        int colSpan;
        String dispStyle;
        StringBuilder header = new StringBuilder();
        header.append("<div class=\"scrollable\" id=\"progenTableDiv\">");
        header.append("<Table ID=\"progenTable\" class=\"prgtable\" CELLPADDING=\"0\" CELLSPACING=\"1\" style=\"border-collapse:collapse\" width=\"100%\">");
        header.append("<thead id=\"theaddiv\">");
        header.append("<tr width=\"100%\"  height=\"20px\">");
        header.append("<th class=\"tableHeaderStyle\"/>");
        if (facade.container.getReportType() != null && facade.container.getReportType().equalsIgnoreCase("I")) {
            header.append("<th class=\"tableHeaderStyle\" align=\"center\" ><font color=\"#000000\">Parameter</font></th>");
        } else {
            header.append("<th class=\"tableHeaderStyle\" align=\"center\" ><font color=\"#000000\">Dimension</font></th>");
        }
        for (TableHeaderRow headerRow : headerRows) {

            for (int i = tableBldr.getFromColumn() + 1; i < colCount; i++) {
                heading = headerRow.getRowData(i);
                rowSpan = headerRow.getRowSpan(i);
                colSpan = headerRow.getColumnSpan(i);
                dispStyle = headerRow.getDisplayStyle(i);
                header.append("<th class=\"tableHeaderStyle\" align=\"center\">").append("<font color=\"#000000\">").append(heading).append("</font>").append("</th>");
            }
        }
        header.append("</tr>");
        header.append("</thead>");
        header.append("<tbody>");
        for (TableHeaderRow headerRow : headerRows) {
            String id = String.valueOf(headerRow.rowDataIds.get(0)).replace("A_", "");
            String divId = id + "Div";
            String childDivId = id + "ChildDiv";
            header.append("<tr id='").append(divId).append("'>");
            header.append("<td class=\"collapsible\" >");
            header.append("<a class=\"collapsed\" onclick=\"loadChildData('" + divId + "','" + id + "','" + childDivId + "','" + tableBldr.facade.getReportParameterValues().toString() + "');\"></a></td>");
            heading = headerRow.getRowData(0);
            header.append("<td align=\"center\" >").append("<font color=\"#000000\">").append(heading).append("</font>").append("</td>");
            PbReturnObject retObj = genereateGrandTotal();
            if (retObj.getRowCount() > 0) {
                StringBuffer elementId = new StringBuffer();
                PbDb pbdb = new PbDb();
                HashMap<String, String> nfrmat = new HashMap<String, String>();
                HashMap<String, String> round = new HashMap<String, String>();
                NumberFormatter nf = new NumberFormatter();
                for (int i = 0; i < facade.container.getReportCollect().reportQryElementIds.size(); i++) {
                    elementId.append(",'").append(facade.container.getReportCollect().reportQryElementIds.get(i)).append("'");
                }
                String qry = "SELECT ELEMENT_ID,NO_FORMAT,ROUND FROM PRG_USER_SUB_FOLDER_ELEMENTS WHERE ELEMENT_ID in (" + elementId.substring(1) + ")";
                try {
                    PbReturnObject retObj1 = pbdb.execSelectSQL(qry);
                    if (retObj1.getRowCount() > 0) {
                        for (int i = 0; i < retObj1.getRowCount(); i++) {
                            nfrmat.put(retObj1.getFieldValueString(i, 0), retObj1.getFieldValueString(i, 1));
                            round.put(retObj1.getFieldValueString(i, 0), retObj1.getFieldValueString(i, 2));
                        }
                    }
                } catch (SQLException ex) {
                    logger.error("Exception:", ex);
                }
                // for(int i=1;i<retObj.getColumnCount();i++){
//                    header.append("<td align=\"center\">").append("<font color=\"#000000\">").append(retObj.getModifiedNumber(new BigDecimal(retObj.getFieldValueString(0, i)))).append("</font>").append("</td>");
//                    }
                for (int i = facade.getViewByCount(); i < facade.getColumnCount(); i++) {
//                     
//                     
                    if (retObj.getFieldValueString(0, displayCols.get(i)) != "" && retObj.getFieldValueString(0, displayCols.get(i)) != null && !retObj.getFieldValueString(0, displayCols.get(i)).equalsIgnoreCase("null")) {
                        if (nfrmat.get(displayCols.get(i).replace("A_", "")) != null && nfrmat.get(displayCols.get(i).replace("A_", "")) != "" && round.get(displayCols.get(i).replace("A_", "")) != null && round.get(displayCols.get(i).replace("A_", "")) != "") {
                            BigDecimal currVal = new BigDecimal(retObj.getFieldValueString(0, displayCols.get(i)));
                            String frmtVal = nf.getModifiedNumber(currVal, String.valueOf(nfrmat.get(displayCols.get(i).replace("A_", ""))), Integer.parseInt(String.valueOf(round.get(displayCols.get(i).replace("A_", "")))));
                            //
                            header.append("<td align=\"center\">").append("<font color=\"#000000\">").append(frmtVal).append("</font>").append("</td>");
                        } else if (nfrmat.get(displayCols.get(i).replace("A_", "")) != null && nfrmat.get(displayCols.get(i).replace("A_", "")) != "") {
                            BigDecimal currVal = new BigDecimal(retObj.getFieldValueString(0, displayCols.get(i)));
                            String frmtVal = nf.getModifiedNumber(currVal, String.valueOf(nfrmat.get(displayCols.get(i).replace("A_", ""))));
                            //
                            header.append("<td align=\"center\">").append("<font color=\"#000000\">").append(frmtVal).append("</font>").append("</td>");
                        } else {
                            header.append("<td align=\"center\">").append("<font color=\"#000000\">").append(retObj.getModifiedNumber(new BigDecimal(retObj.getFieldValueString(0, displayCols.get(i))))).append("</font>").append("</td>");
                        }
                    } else {
                        header.append("<td align=\"center\">").append("<font color=\"#000000\">").append("").append("</font>").append("</td>");
                    }
                }
            } else {
                for (int i = tableBldr.getFromColumn() + 1; i < colCount; i++) {
                    header.append("<td>");
                    header.append("</td>");
                }
            }
            header.append("</tr>");
            header.append("<tr class=\"expand-child\"><td style=\"display: none;\" colspan='").append(2 + colCount).append("'>");
            header.append("<div style=\"display: ;\" id='").append(childDivId).append("'>");
            header.append("</div>");
            header.append("<div id='").append(childDivId).append("prgBar").append("'></div>");
        }
        header.append("</tbody>");
        return super.parentHtml.append(header);
    }

    @Override
    protected void setParentHtml(StringBuilder parentHtml) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public PbReturnObject genereateGrandTotal() {
        DataFacade facade = tableBldr.facade;
        PbReportQuery repQuery = new PbReportQuery();
        PbReturnObject retObj = null;
        QueryExecutor qryExec = new QueryExecutor();
        PbReportCollection collect = facade.container.getReportCollect();

        repQuery.setRowViewbyCols(collect.reportRowViewbyValues);
        repQuery.setColViewbyCols(collect.reportColViewbyValues);
        repQuery.setParamValue(facade.getReportParameterValues());
        repQuery.setQryColumns(collect.reportQryElementIds);
        repQuery.setColAggration(collect.reportQryAggregations);

        repQuery.setTimeDetails(collect.timeDetailsArray);
        repQuery.isKpi = true;
        repQuery.setReportId(collect.reportId);
        repQuery.setBizRoles(collect.reportBizRoles[0]);
        repQuery.setUserId(facade.getUserId());
        try {
            String query = repQuery.generateViewByQry();
            //
            retObj = (PbReturnObject) qryExec.executeQuery(collect, query, false);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        return retObj;
    }
}
