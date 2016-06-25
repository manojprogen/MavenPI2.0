/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.charts;

import java.io.Serializable;

/**
 *
 * @author progen
 */
public class JqplotGraphProperty implements Serializable {

    private static final long serialVersionUID = 79876647115562L;
    private String graphId = "";
    private String graphTypename = "";
    private String graphTypeId = "";
    private String reportId = "";
    private String slectedGraphType = null;
    public Boolean isJqPlot = false;
    private String[] seriescolors;
    private String yaxiscalibration = "";
    private String graphDisplayCols = null;
    private boolean graphDrill = false;
    private boolean isTranspose = false;
    private String[] rowValues;
    private String yaxisstart = "";
    private String yaxisend = "";
    private String yaxisinterval = "";
    private String xaxisstart = "";
    private String xaxisend = "";
    private String xaxisinterval = "";
    private String y2axisstart = "";
    private String y2axisend = "";
    private String y2axisinterval = "";
    private int yAxisRounding;
    private int xAxisRounding;
    private int y2AxisRounding;
    private String targetValue = "";
    private String tickdisplay = "";
    private boolean adhocEnabled;
    private boolean colorGrouping = false;
    private int labelDirection = -30;
    private int legendsPerRow;
    private String[] tableColumns = null;
    private String graphdisptype = "";
    private String[] graphColumns = null;
    private String graphtype1 = "Bar";
    private String graphtype2 = "Line";
    private String targetColor = "rgb(88,88,88)";
    private String drillType = "Standard";
    private String datebyInterval = "Default";
    private String targetType = "standard";
    private boolean appendXaxis = false;
    private boolean legenaAppend = false;
    private boolean SupriseZero = false;
    private boolean tooltipXaxis = true;
    private String ischeckedLAxis = "";
    private String ischeckedRAxis = "";
    private String ischeckedXAxishide = "";
    private String ischeckedYAxishide = "";
    private String Flagg = "";
    private String viewbyLabels = "";
    private String isShowOthers = "";
    private String OtherFlag = "";

    /**
     * @return the graphId for reports it acts as a individual graph id which we
     * generate every time when we create a new graph and for dashboards this
     * refers to dashletId
     */
    public String getGraphId() {
        return graphId;
    }

    /**
     * @param graphId the graphId to set
     */
    public void setGraphId(String graphId) {
        this.graphId = graphId;
    }

    /**
     * @return the graphTypename
     */
    public String getGraphTypename() {
        return graphTypename;
    }

    /**
     * @param graphTypename the graphTypename to set
     */
    public void setGraphTypename(String graphTypename) {
        this.graphTypename = graphTypename;
    }

    /**
     * @return the graphTypeId
     */
    public String getGraphTypeId() {
        return graphTypeId;
    }

    /**
     * @param graphTypeId the graphTypeId to set
     */
    public void setGraphTypeId(String graphTypeId) {
        this.graphTypeId = graphTypeId;
    }

    /**
     * @return the selectedGraphType
     */
    public String getSlectedGraphType(String grpId) {

        return this.slectedGraphType;

    }

    /**
     * @param slectedGraphType the slectedGraphType to set
     */
    public void setSlectedGraphType(String grpId, String displayType) {

        this.slectedGraphType = displayType;

    }

    /**
     * @return the reportId
     */
    public String getReportId() {
        return reportId;
    }

    /**
     * @param reportId the reportId to set
     */
    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public String getischeckedLA() {
        return ischeckedLAxis;
    }

    public void setischeckedLA(String ischeckedLAxis) {
        this.ischeckedLAxis = ischeckedLAxis;
    }

    public String getischeckedRA() {
        return ischeckedRAxis;
    }

    public void setischeckedRA(String ischeckedRAxis) {
        this.ischeckedRAxis = ischeckedRAxis;
    }

    public String getischeckedXA() {
        return ischeckedXAxishide;
    }

    public void setischeckedXA(String ischeckedXAxishide) {
        this.ischeckedXAxishide = ischeckedXAxishide;
    }

    public String getischeckedYA() {
        return ischeckedYAxishide;
    }

    public void setischeckedYA(String ischeckedYAxishide) {
        this.ischeckedYAxishide = ischeckedYAxishide;
    }

    public String getFlag() {
        return Flagg;
    }

    public void setFlag(String Flagg) {
        this.Flagg = Flagg;
    }

    public String getviewbyLabels() {
        return viewbyLabels;
    }

    public void setviewbyLabels(String viewbyLabels) {
        this.viewbyLabels = viewbyLabels;
    }

    /**
     * @return the yaxiscalibration
     */
    public String getYaxiscalibration() {
        return yaxiscalibration;
    }

    /**
     * @param yaxiscalibration the yaxiscalibration to set
     */
    public void setYaxiscalibration(String yaxiscalibration) {
        this.yaxiscalibration = yaxiscalibration;
    }

    /**
     * @return the seriescolors
     */
    public String[] getSeriescolors() {
        return seriescolors;
    }

    /**
     * @param seriescolors the seriescolors to set
     */
    public void setSeriescolors(String[] seriescolors) {
        this.seriescolors = seriescolors;
    }

    /**
     * @return the graphDrill
     */
    public boolean isGraphDrill() {
        return graphDrill;
    }

    /**
     * @param graphDrill the graphDrill to set
     */
    public void setGraphDrill(boolean graphDrill) {
        this.graphDrill = graphDrill;
    }

    public String getGraphDisplayCols() {
        return graphDisplayCols;
    }

    public void setGraphDisplayCols(String graphDisplayCols) {
        this.graphDisplayCols = graphDisplayCols;
    }

    public boolean isTranspose() {
        return isTranspose;
    }

    public void setIsTranspose(boolean isTranspose) {
        this.isTranspose = isTranspose;
    }

    public String[] getRowValues() {
        return rowValues;
    }

    /**
     * @param rowValues the rowValues to set
     */
    public void setRowValues(String[] rowValues) {
        this.rowValues = rowValues;
    }

    /**
     * @return the yaxisstart
     */
    public String getYaxisstart() {
        return yaxisstart;
    }

    /**
     * @param yaxisstart the yaxisstart to set
     */
    public void setYaxisstart(String yaxisstart) {
        this.yaxisstart = yaxisstart;
    }

    /**
     * @return the yaxisend
     */
    public String getYaxisend() {
        return yaxisend;
    }

    /**
     * @param yaxisend the yaxisend to set
     */
    public void setYaxisend(String yaxisend) {
        this.yaxisend = yaxisend;
    }

    /**
     * @return the yaxisinterval
     */
    public String getYaxisinterval() {
        return yaxisinterval;
    }

    /**
     * @param yaxisinterval the yaxisinterval to set
     */
    public void setYaxisinterval(String yaxisinterval) {
        this.yaxisinterval = yaxisinterval;
    }

    /**
     * @return the xaxisstart
     */
    public String getXaxisstart() {
        return xaxisstart;
    }

    /**
     * @param xaxisstart the xaxisstart to set
     */
    public void setXaxisstart(String xaxisstart) {
        this.xaxisstart = xaxisstart;
    }

    /**
     * @return the xaxisend
     */
    public String getXaxisend() {
        return xaxisend;
    }

    /**
     * @param xaxisend the xaxisend to set
     */
    public void setXaxisend(String xaxisend) {
        this.xaxisend = xaxisend;
    }

    /**
     * @return the xaxisinterval
     */
    public String getXaxisinterval() {
        return xaxisinterval;
    }

    /**
     * @param xaxisinterval the xaxisinterval to set
     */
    public void setXaxisinterval(String xaxisinterval) {
        this.xaxisinterval = xaxisinterval;
    }

    /**
     * @return the y2axisstart
     */
    public String getY2axisstart() {
        return y2axisstart;
    }

    /**
     * @param y2axisstart the y2axisstart to set
     */
    public void setY2axisstart(String y2axisstart) {
        this.y2axisstart = y2axisstart;
    }

    /**
     * @return the y2axisend
     */
    public String getY2axisend() {
        return y2axisend;
    }

    /**
     * @param y2axisend the y2axisend to set
     */
    public void setY2axisend(String y2axisend) {
        this.y2axisend = y2axisend;
    }

    /**
     * @return the y2axisinterval
     */
    public String getY2axisinterval() {
        return y2axisinterval;
    }

    /**
     * @param y2axisinterval the y2axisinterval to set
     */
    public void setY2axisinterval(String y2axisinterval) {
        this.y2axisinterval = y2axisinterval;
    }

    /**
     * @return the yAxisRounding
     */
    public int getyAxisRounding() {
        return yAxisRounding;
    }

    /**
     * @param yAxisRounding the yAxisRounding to set
     */
    public void setyAxisRounding(int yAxisRounding) {
        this.yAxisRounding = yAxisRounding;
    }

    /**
     * @return the xAxisRounding
     */
    public int getxAxisRounding() {
        return xAxisRounding;
    }

    /**
     * @param xAxisRounding the xAxisRounding to set
     */
    public void setxAxisRounding(int xAxisRounding) {
        this.xAxisRounding = xAxisRounding;
    }

    /**
     * @return the y2AxisRounding
     */
    public int getY2AxisRounding() {
        return y2AxisRounding;
    }

    /**
     * @param y2AxisRounding the y2AxisRounding to set
     */
    public void setY2AxisRounding(int y2AxisRounding) {
        this.y2AxisRounding = y2AxisRounding;
    }

    /**
     * @return the targetValue
     */
    public String getTargetValue() {
        return targetValue;
    }

    /**
     * @param targetValue the targetValue to set
     */
    public void setTargetValue(String targetValue) {
        this.targetValue = targetValue;
    }

    /**
     * @return the targetValue
     */
    public String gettickdisplay() {
        return tickdisplay;
    }

    /**
     * @param targetValue the targetValue to set
     */
    public void settickdisplay(String tickdisplay) {
        this.tickdisplay = tickdisplay;
    }

    /**
     * @return the adhocEnabled
     */
    public boolean isAdhocEnabled() {
        return adhocEnabled;
    }

    /**
     * @param adhocEnabled the adhocEnabled to set
     */
    public void setAdhocEnabled(boolean adhocEnabled) {
        this.adhocEnabled = adhocEnabled;
    }

    /**
     * @return the colorGrouping
     */
    public boolean isColorGrouping() {
        return colorGrouping;
    }

    /**
     * @param colorGrouping the colorGrouping to set
     */
    public void setColorGrouping(boolean colorGrouping) {
        this.colorGrouping = colorGrouping;
    }

    /**
     * @return the labelDirection
     */
    public int getLabelDirection() {
        return labelDirection;
    }

    /**
     * @param labelDirection the labelDirection to set
     */
    public void setLabelDirection(int labelDirection) {
        this.labelDirection = labelDirection;
    }

    /**
     * @return the legendsPerRow
     */
    public int getLegendsPerRow() {
        return legendsPerRow;
    }

    /**
     * @param legendsPerRow the legendsPerRow to set
     */
    public void setLegendsPerRow(int legendsPerRow) {
        this.legendsPerRow = legendsPerRow;
    }

    /**
     * @return the tableColumns
     */
    public String[] getTableColumns() {
        return tableColumns;
    }

    /**
     * @param tableColumns the tableColumns to set
     */
    public void setTableColumns(String[] tableColumns) {
        this.tableColumns = tableColumns;
    }

    /**
     * @return the graphdisptype
     */
    public String getGraphdisptype() {
        return graphdisptype;
    }

    /**
     * @param graphdisptype the graphdisptype to set
     */
    public void setGraphdisptype(String graphdisptype) {
        this.graphdisptype = graphdisptype;
    }

    /**
     * @return the graphColumns
     */
    public String[] getGraphColumns() {
        return graphColumns;
    }

    /**
     * @param graphColumns the graphColumns to set
     */
    public void setGraphColumns(String[] graphColumns) {
        this.graphColumns = graphColumns;
    }

    /**
     * @return the graphtype1
     */
    public String getGraphtype1() {
        return graphtype1;
    }

    /**
     * @param graphtype1 the graphtype1 to set
     */
    public void setGraphtype1(String graphtype1) {
        this.graphtype1 = graphtype1;
    }

    /**
     * @return the graphtype2
     */
    public String getGraphtype2() {
        return graphtype2;
    }

    /**
     * @param graphtype2 the graphtype2 to set
     */
    public void setGraphtype2(String graphtype2) {
        this.graphtype2 = graphtype2;
    }

    /**
     * @return the targetColor
     */
    public String getTargetColor() {
        return targetColor;
    }

    /**
     * @param targetColor the targetColor to set
     */
    public void setTargetColor(String targetColor) {
        this.targetColor = targetColor;
    }

    /**
     * @return the drillType
     */
    public String getDrillType() {
        return drillType;
    }

    /**
     * @param drillType the drillType to set
     */
    public void setDrillType(String drillType) {
        this.drillType = drillType;
    }

    /**
     * @return the datebyInterval
     */
    public String getDatebyInterval() {
        return datebyInterval;
    }

    /**
     * @param datebyInterval the datebyInterval to set
     */
    public void setDatebyInterval(String datebyInterval) {
        this.datebyInterval = datebyInterval;
    }

    /**
     * @return the targetType
     */
    public String getTargetType() {
        return targetType;
    }

    /**
     * @param targetType the targetType to set
     */
    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    /**
     * @return the appendXaxis
     */
    public boolean isAppendXaxis() {
        return appendXaxis;
    }

    /**
     * @param appendXaxis the appendXaxis to set
     */
    public void setAppendXaxis(boolean appendXaxis) {
        this.appendXaxis = appendXaxis;
    }

    /**
     * @return the legenaAppend
     */
    public boolean isLegenaAppend() {
        return legenaAppend;
    }

    /**
     * @param legenaAppend the legenaAppend to set
     */
    public void setLegenaAppend(boolean legenaAppend) {
        this.legenaAppend = legenaAppend;
    }

    /**
     * @return the SupriseZero
     */
    public boolean isSupriseZero() {
        return SupriseZero;
    }

    /**
     * @param SupriseZero the SupriseZero to set
     */
    public void setSupriseZero(boolean SupriseZero) {
        this.SupriseZero = SupriseZero;
    }

    public boolean istooltipXaxis() {
        return tooltipXaxis;
    }

    public void settooltipXaxis(boolean tooltipXaxis) {
        this.tooltipXaxis = tooltipXaxis;
    }

    public void setviewOthers(String isShowOthers) {
        this.isShowOthers = isShowOthers;
    }

    public void setisOthersFlag(String OtherFlag) {
        this.OtherFlag = OtherFlag;
    }

    public String getisOthersFlag() {
        return OtherFlag;
    }

    public String getviewOthers() {
        return isShowOthers;
    }
}
