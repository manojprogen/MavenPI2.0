/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.reportview.db;

import com.progen.report.XtendReportMeta;
import java.util.List;
import java.util.Map;

public class WallData {
//    private String tab_name;

    private List<Map<String, String>> data;
    private XtendReportMeta reportMeta;
    private List<Map<String, String>> chartGridDetail;
    private String row;
    private String col;
    private String size_x;
    private String size_y;
    private String id;

//    public String getTab_name() {
//        return tab_name;
//    }
//
//    public void setTab_name(String tab_name) {
//        this.tab_name = tab_name;
//    }
    public XtendReportMeta getReportMeta() {
        return reportMeta;
    }

    public void setReportMeta(XtendReportMeta reportMeta) {
        this.reportMeta = reportMeta;
    }

    /**
     * @return the data
     */
    public List<Map<String, String>> getData() {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(List<Map<String, String>> data) {
        this.data = data;
    }

    /**
     * @return the row
     */
    public String getRow() {
        return row;
    }

    /**
     * @param row the row to set
     */
    public void setRow(String row) {
        this.row = row;
    }

    /**
     * @return the col
     */
    public String getCol() {
        return col;
    }

    /**
     * @param col the col to set
     */
    public void setCol(String col) {
        this.col = col;
    }

    /**
     * @return the size_x
     */
    public String getSize_x() {
        return size_x;
    }

    /**
     * @param size_x the size_x to set
     */
    public void setSize_x(String size_x) {
        this.size_x = size_x;
    }

    /**
     * @return the size_y
     */
    public String getSize_y() {
        return size_y;
    }

    /**
     * @param size_y the size_y to set
     */
    public void setSize_y(String size_y) {
        this.size_y = size_y;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the chartGridDetail
     */
    public List<Map<String, String>> getChartGridDetail() {
        return chartGridDetail;
    }

    /**
     * @param chartGridDetail the chartGridDetail to set
     */
    public void setChartGridDetail(List<Map<String, String>> chartGridDetail) {
        this.chartGridDetail = chartGridDetail;
    }
    /**
     * @return the data
     */
}
