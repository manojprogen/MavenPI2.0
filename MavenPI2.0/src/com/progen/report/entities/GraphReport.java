/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.entities;

import com.progen.charts.GraphProperty;
import com.progen.report.kpi.DashletPropertiesHelper;
import java.util.List;

/**
 *
 * @author progen
 */
public class GraphReport extends Report {

    //Graph Master
    private String graphName;
    private String graphFamily;
    private int graphType;
    private String graphTypeName;
    private int graphOrder;
    private int graphSize;
    private String graphSizeName;
    private String graphWidth;
    private String graphHeight;
    private boolean linkAllowed;
    private boolean labelAllowed;
    private boolean legendAllowed;
    private boolean tooltipAllowed;
    private int graphClass;
    private String graphClassName;
    private String leftYAxisLabel;
    private String rightYAxisLabel;
    private String XAxisLabel;
    private String fontName;
    private int fontSize;
    private String backgroundColor;
    private String fontColor;
    private String legendLocation;
    private boolean showXAxisGrid;
    private boolean showYAxisGrid;
    private boolean showData;
    private String rowValues;
    private boolean showGT;
    private String showlyAxis;
    private String showryAxis;
    private String showxAxis;
    private String displayRows;
    private boolean showAsTable;
    private String measureNamePosition;
    private DashletPropertiesHelper dashletpropertieshelper;
    private boolean isreset;
    private String buildElement;
    private boolean OLAPEnabled;
    private String initialGraphHeight;
    private String initialGraphWidth;

    public String getMeasureNamePosition() {
        return measureNamePosition;
    }

    public void setMeasureNamePosition(String measureNamePosition) {
        this.measureNamePosition = measureNamePosition;
    }
    private GraphProperty graphProperty;
    //Graph Details
    private List<QueryDetail> queryDetails;
    private boolean timeSeries;
    private String axis;

    public GraphReport() {
        graphProperty = new GraphProperty();
    }

    public String getXAxisLabel() {
        return XAxisLabel;
    }

    public void setXAxisLabel(String XAxisLabel) {
        this.XAxisLabel = XAxisLabel;
    }

    public String getAxis() {
        return axis;
    }

    public void setAxis(String axis) {
        this.axis = axis;
    }

    public String getDisplayRows() {
        return displayRows;
    }

    public void setDisplayRows(String displayRows) {
        this.displayRows = displayRows;
    }

    @Override
    public List<QueryDetail> getQueryDetails() {
        return this.queryDetails;
    }

    public void setQueryDetails(List<QueryDetail> queryDetails) {
        this.queryDetails = queryDetails;
    }

    public String getFontColor() {
        return fontColor;
    }

    public void setFontColor(String fontColor) {
        this.fontColor = fontColor;
    }

    public String getFontName() {
        return fontName;
    }

    public void setFontName(String fontName) {
        this.fontName = fontName;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public int getGraphClass() {
        return graphClass;
    }

    public void setGraphClass(int graphClass) {
        this.graphClass = graphClass;
    }

    public String getGraphFamily() {
        return graphFamily;
    }

    public void setGraphFamily(String graphFamily) {
        this.graphFamily = graphFamily;
    }

    public String getGraphHeight() {
        return graphHeight;
    }

    public void setGraphHeight(String graphHeight) {
        this.graphHeight = graphHeight;
    }

    public String getGraphName() {
        return graphName;
    }

    public void setGraphName(String graphName) {
        this.graphName = graphName;
    }

    public int getGraphOrder() {
        return graphOrder;
    }

    public void setGraphOrder(int graphOrder) {
        this.graphOrder = graphOrder;
    }

    public int getGraphSize() {
        return graphSize;
    }

    public void setGraphSize(int graphSize) {
        this.graphSize = graphSize;
    }

    public int getGraphType() {
        return graphType;
    }

    public void setGraphType(int graphType) {
        this.graphType = graphType;
    }

    public String getGraphWidth() {
        return graphWidth;
    }

    public void setGraphWidth(String graphWidth) {
        this.graphWidth = graphWidth;
    }

    public boolean isLabelAllowed() {
        return labelAllowed;
    }

    public void setLabelAllowed(boolean labelAllowed) {
        this.labelAllowed = labelAllowed;
    }

    public String getLeftYAxisLabel() {
        return leftYAxisLabel;
    }

    public void setLeftYAxisLabel(String leftYAxisLabel) {
        this.leftYAxisLabel = leftYAxisLabel;
    }

    public boolean isLegendAllowed() {
        return legendAllowed;
    }

    public void setLegendAllowed(boolean legendAllowed) {
        this.legendAllowed = legendAllowed;
    }

    public String getLegendLocation() {
        return legendLocation;
    }

    public void setLegendLocation(String legendLocation) {
        this.legendLocation = legendLocation;
    }

    public boolean isLinkAllowed() {
        return linkAllowed;
    }

    public void setLinkAllowed(boolean linkAllowed) {
        this.linkAllowed = linkAllowed;
    }

    public String getRightYAxisLabel() {
        return rightYAxisLabel;
    }

    public void setRightYAxisLabel(String rightYAxisLabel) {
        this.rightYAxisLabel = rightYAxisLabel;
    }

    public String getRowValues() {
        return rowValues;
    }

    public void setRowValues(String rowValues) {
        this.rowValues = rowValues;
    }

    public boolean isShowAsTable() {
        return showAsTable;
    }

    public void setShowAsTable(boolean showAsTable) {
        this.showAsTable = showAsTable;
    }

    public boolean isShowData() {
        return showData;
    }

    public void setShowData(boolean showData) {
        this.showData = showData;
    }

    public boolean isShowGT() {
        return showGT;
    }

    public void setShowGT(boolean showGT) {
        this.showGT = showGT;
    }

    public boolean isShowXAxisGrid() {
        return showXAxisGrid;
    }

    public void setShowXAxisGrid(boolean showXAxisGrid) {
        this.showXAxisGrid = showXAxisGrid;
    }

    public boolean isShowYAxisGrid() {
        return showYAxisGrid;
    }

    public void setShowYAxisGrid(boolean showYAxisGrid) {
        this.showYAxisGrid = showYAxisGrid;
    }

    public boolean isTooltipAllowed() {
        return tooltipAllowed;
    }

    public void setTooltipAllowed(boolean tooltipAllowed) {
        this.tooltipAllowed = tooltipAllowed;
    }

    public String getGraphClassName() {
        return graphClassName;
    }

    public void setGraphClassName(String graphClassName) {
        this.graphClassName = graphClassName;
    }

    public String getGraphSizeName() {
        return graphSizeName;
    }

    public GraphProperty getGraphProperty() {
        return graphProperty;
    }

    public void setGraphProperty(GraphProperty graphProperty) {
        this.graphProperty = graphProperty;
    }

    public void setGraphSizeName(String graphSizeName) {
        this.graphSizeName = graphSizeName;
    }

    public String getGraphTypeName() {
        return graphTypeName;
    }

    public void setGraphTypeName(String graphTypeName) {
        this.graphTypeName = graphTypeName;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public boolean isTimeSeries() {
        return timeSeries;
    }

    public void setTimeSeries(boolean timeSeries) {
        this.timeSeries = timeSeries;
    }

    public int getElementCount() {
        int elementCount = 0;
        if (queryDetails != null) {
            elementCount = queryDetails.size();
        }
        return elementCount;
    }

    /**
     * @param showlyAxis the showlyAxis to set
     */
    public void setShowlyAxis(String showlyAxis) {
        this.showlyAxis = showlyAxis;
    }

    /**
     * @param showryAxis the showryAxis to set
     */
    public void setShowryAxis(String showryAxis) {
        this.showryAxis = showryAxis;
    }

    /**
     * @param showxAxis the showxAxis to set
     */
    public void setShowxAxis(String showxAxis) {
        this.showxAxis = showxAxis;
    }
//      public boolean isShowlyAxis() {
//        return showlyAxis;
//    }
//
//    public void setShowlyAxis(boolean showlyAxis) {
//        this.showlyAxis = showlyAxis;
//    }
//      public boolean isShowryAxis() {
//        return showryAxis;
//    }
//
//    public void setShowryAxis(boolean showryAxis) {
//        this.showryAxis = showryAxis;
//    }
//      public boolean isShowxAxis() {
//        return showxAxis;
//    }
//
//    public void setShowxAxis(boolean showlyAxis) {
//        this.showxAxis = showxAxis;
//    }

    public DashletPropertiesHelper getDashletpropertieshelper() {
        return dashletpropertieshelper;
    }

    public void setDashletpropertieshelper(DashletPropertiesHelper dashletpropertieshelper) {
        this.dashletpropertieshelper = dashletpropertieshelper;
    }

    public boolean isIsreset() {
        return isreset;
    }

    public void setIsreset(boolean isreset) {
        this.isreset = isreset;
    }

    /**
     * @return the buildElement
     */
    public String getBuildElement() {
        return buildElement;
    }

    /**
     * @param buildElement the buildElement to set
     */
    public void setBuildElement(String buildElement) {
        this.buildElement = buildElement;
    }

    /**
     * @return the OLAPEnabled
     */
    public boolean isOLAPEnabled() {
        return OLAPEnabled;
    }

    /**
     * @param OLAPEnabled the OLAPEnabled to set
     */
    public void setOLAPEnabled(boolean OLAPEnabled) {
        this.OLAPEnabled = OLAPEnabled;
    }

    /**
     * @return the initialGraphHeight
     */
    public String getInitialGraphHeight() {
        return initialGraphHeight;
    }

    /**
     * @param initialGraphHeight the initialGraphHeight to set
     */
    public void setInitialGraphHeight(String initialGraphHeight) {
        this.initialGraphHeight = initialGraphHeight;
    }

    /**
     * @return the initialGraphWidth
     */
    public String getInitialGraphWidth() {
        return initialGraphWidth;
    }

    /**
     * @param initialGraphWidth the initialGraphWidth to set
     */
    public void setInitialGraphWidth(String initialGraphWidth) {
        this.initialGraphWidth = initialGraphWidth;
    }
}
