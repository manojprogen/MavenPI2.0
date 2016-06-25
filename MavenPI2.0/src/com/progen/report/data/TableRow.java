/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.data;

import com.google.common.collect.Iterables;
import com.progen.report.display.table.TableCellSpan;
import java.util.*;

/**
 *
 * @author progen
 */
public abstract class TableRow {

    protected ArrayList<String> rowDataIds;
    ArrayList<Boolean> isDisplayed;
    HashSet<TableCellSpan> spans;
    ArrayList<String> reportParameters = new ArrayList<String>();
    ArrayList<String> reportParameterNames = new ArrayList<String>();
    String viewbyId;
    LinkedHashMap<String, String> reportParameterValues = new LinkedHashMap<String, String>();
    int viewbyCount;
    String adhocUrl;
    String adhocTimeUrl;
    String adhocParamDrillUrl;
    public String MsrdrillUrl;
    public HashMap paramhashmapPA;
    public String userTypeAdmin;
    HashMap<String, Boolean> lockdataset;

    public String getAdhocParamDrillUrl() {
        return adhocParamDrillUrl;
    }

    public void setAdhocParamDrillUrl(String adhocParamDrillUrl) {
        this.adhocParamDrillUrl = adhocParamDrillUrl;
    }

    public String getAdhocTimeUrl() {
        return adhocTimeUrl;
    }

    public void setAdhocTimeUrl(String adhocTimeUrl) {
        this.adhocTimeUrl = adhocTimeUrl;
    }

    public String getAdhocUrl() {
        return adhocUrl;
    }

    public void setAdhocUrl(String adhocUrl) {
        this.adhocUrl = adhocUrl;
    }

    public int getViewbyCount() {
        return viewbyCount;
    }

    public void setViewbyCount(int viewbyCount) {
        this.viewbyCount = viewbyCount;
    }

    public LinkedHashMap<String, String> getReportParameterValues() {
        return reportParameterValues;
    }

    public void setReportParameterValues(LinkedHashMap<String, String> reportParameterValues) {
        this.reportParameterValues = reportParameterValues;
    }

    public String getViewbyId() {
        return viewbyId;
    }

    public void setViewbyId(String viewbyId) {
        this.viewbyId = viewbyId;
    }

    public ArrayList<String> getReportParameterNames() {
        return reportParameterNames;
    }

    public void setReportParameterNames(ArrayList<String> reportParameterNames) {
        this.reportParameterNames = reportParameterNames;
    }

    public ArrayList<String> getReportParameters() {
        return reportParameters;
    }

    public void setReportParameters(ArrayList<String> reportParameters) {
        this.reportParameters = reportParameters;
    }

    public abstract String getRowData(int column);

    public abstract String getID(int column);

    public TableRow() {
        isDisplayed = new ArrayList<Boolean>();
    }

    public void setRowDataIds(ArrayList<String> rowDataIds) {
        this.rowDataIds = rowDataIds;
    }

    public String getColumnId(int column) {
        return rowDataIds.get(column);
    }

    public void setDisplayStyle(ArrayList<Boolean> isDisplayed) {
        this.isDisplayed = isDisplayed;
    }

    public String getDisplayStyle(int column) {
        if (this.isDisplayed.isEmpty()) {
            return "\"\"";
        }
        if (column >= this.isDisplayed.size()) {
            //    
            return "\"\"";
        }
        if (this.isDisplayed.get(column)) {
            return "\"\"";
        } else {
            return "none";
        }
    }

    public int getColumnSpan(int column) {
        if (spans != null) {
            Iterator<TableCellSpan> span = Iterables.filter(spans, TableCellSpan.getCellIdPredicate(getID(column))).iterator();
            if (span.hasNext()) {
                return span.next().getColumnSpan();
            }
        }
        return 1;
    }

    public int getRowSpan(int column) {
        if (spans != null) {
            Iterator<TableCellSpan> span = Iterables.filter(spans, TableCellSpan.getCellIdPredicate(getID(column))).iterator();
            if (span.hasNext()) {
                return span.next().getRowSpan();
            }
        }

        return 1;
    }

    public void setCellSpans(HashSet<TableCellSpan> cellSpan) {
        this.spans = cellSpan;
    }

    public void addCellSpan(TableCellSpan cellSpan) {
        if (spans == null) {
            spans = new HashSet<TableCellSpan>();
        }
        spans.add(cellSpan);
    }

    public String getMsrdrillUrl() {
        return MsrdrillUrl;
    }

    public void setMsrdrillUrl(String MsrdrillUrl) {
        this.MsrdrillUrl = MsrdrillUrl;
    }

    public HashMap getparameterHash() {
        return paramhashmapPA;
    }

    public void setparameterHash(HashMap paramhashmapPA) {
        this.paramhashmapPA = paramhashmapPA;
    }

    public String getuserTypeAdmin() {
        return userTypeAdmin;
    }

    public void setuserTypeAdmin(String userTypeAdmin) {
        this.userTypeAdmin = userTypeAdmin;
    }
}
