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
public class GraphProperty implements Serializable {

    private String showlyAxis = " ";
    private String showryAxis = " ";
    private String showxAxis = " ";
    private boolean showLabels;
    private String numberFormat;
    private String swapGraphColumns;
    private String symbol;
    private String measureNamePosition = "";
    private boolean transposeData;
    private static final long serialVersionUID = 753264719878228L;

    public String getMeasureNamePosition() {
        return measureNamePosition;
    }

    public void setMeasurePosition(String measureNamePosition) {
        this.measureNamePosition = measureNamePosition;
    }
    private String targetValueType = "";
    private double startValue;
    private double endValue;
    private boolean minMaxRange;
    private String graphLegendLoc;
    private String showGT;
    private String graphGridLines;
    private String showLegends;
    private String grpSize;
    private String graphDisplayRows;
    private String showOverAllMin;
    private String showOverAllMax;
    private String showCategoryMin;
    private String showCategoryMax;
    private String[] barChartColumnNames1;
    private String[] barChartColumnNames2;
    private String[] barChartColumnTitles1;
    private String[] barChartColumnTitles2;
    private String fromDate;
    private String toDate;
    private double targetPerDay;
    private String stackedType = "absStacked";
    private boolean stackedFlag = false;
    private String measureFormat;
    private String measureValueRounding;
    private String axisLabelPosition;
    private String calibration;
    private String firstChartType;
    private String secondChartType;
    private String[] rgbColorArr;

    public String[] getRgbColorArr() {
        return rgbColorArr;
    }

    public void setRgbColorArr(String[] rgbColorArr) {
        this.rgbColorArr = rgbColorArr;
    }
    private String[] colorSeries;

    public String getFirstChartType() {
        return firstChartType;
    }

    public void setFirstChartType(String firstChartType) {
        this.firstChartType = firstChartType;
    }

    public String getSecondChartType() {
        return secondChartType;
    }

    public void setSecondChartType(String secondChartType) {
        this.secondChartType = secondChartType;
    }

    public String getCalibration() {
        return calibration;
    }

    public void setCalibration(String calibration) {
        this.calibration = calibration;
    }

    public String getAxisLabelPosition() {
        return axisLabelPosition;
    }

    public void setAxisLabelPosition(String axisLabelPosition) {
        this.axisLabelPosition = axisLabelPosition;
    }

    public String getMeasureFormat() {
        return measureFormat;
    }

    public void setMeasureFormat(String measureFormat) {
        this.measureFormat = measureFormat;
    }

    public String getMeasureValueRounding() {
        return measureValueRounding;
    }

    public void setMeasureValueRounding(String measureValueRounding) {
        this.measureValueRounding = measureValueRounding;
    }

    public GraphProperty() {
        symbol = "";
        swapGraphColumns = "";
        numberFormat = "";
    }

    public boolean isLabelsDisplayed() {
        return showLabels;
    }

    public void setLabelsDisplayed(boolean showLabels) {
        this.showLabels = showLabels;
    }

    public double getEndValue() {
        return endValue;
    }

    public void setEndValue(double endValue) {
        this.endValue = endValue;
    }

    public String getNumberFormat() {
        return numberFormat;
    }

    public void setNumberFormat(String numberFormat) {
        this.numberFormat = numberFormat;
    }

    public double getStartValue() {
        return startValue;
    }

    public void setStartValue(double startValue) {
        this.startValue = startValue;
    }

    public String getSwapGraphColumns() {
        return swapGraphColumns;
    }

    public void setSwapGraphColumns(String swapGraphColumns) {
        this.swapGraphColumns = swapGraphColumns;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public boolean getMinMaxRange() {
        return minMaxRange;
    }

    public void setMinMaxRange(boolean minMaxRange) {
        this.minMaxRange = minMaxRange;
    }

    public String toXml() {
        StringBuilder builder = new StringBuilder();
        builder.append("<Graph_Property>");
        builder.append("<Show_Labels>");
        builder.append(this.showLabels);
        builder.append("</Show_Labels>");
        builder.append("<Show_Grid>");
        builder.append(this.graphGridLines);
        builder.append("</Show_Grid>");
        builder.append("<symbol>").append(symbol).append("</symbol>");
        builder.append("<measureNamePosition>").append(measureNamePosition).append("</measureNamePosition>");
        builder.append("<swapColumns>").append(swapGraphColumns).append("</swapColumns>");
        builder.append("<targetValueType>").append(targetValueType).append("</targetValueType>");
        builder.append("<startValue>").append(startValue).append("</startValue>");
        builder.append("<endValue>").append(endValue).append("</endValue>");
        builder.append("<numberFormat>").append(numberFormat).append("</numberFormat>");
        builder.append("<minMaxRange>").append(minMaxRange).append("</minMaxRange>");
        builder.append("<showlyAxis>").append(showlyAxis).append("</showlyAxis>");
        builder.append("<showryAxis>").append(showryAxis).append("</showryAxis>");
        builder.append("<showxAxis>").append(showxAxis).append("</showxAxis>");
        builder.append("<showOverAllMin>").append(showOverAllMin).append("</showOverAllMin>");
        builder.append("<showOverAllMax>").append(showOverAllMax).append("</showOverAllMax>");
        builder.append("<showCategoryMin>").append(showCategoryMin).append("</showCategoryMin>");
        builder.append("<showCategoryMax>").append(showCategoryMax).append("</showCategoryMax>");

        builder.append("<colorSeries>");
        if (colorSeries != null) {
            for (int j = 0; j < colorSeries.length; j++) {
                builder.append("<clr>").append(colorSeries[j]).append("</clr>");
            }
        }
        builder.append("</colorSeries>");


        builder.append("<barChartColumnNames1>");
        if (barChartColumnNames1 != null) {
            for (int i = 0; i < barChartColumnNames1.length; i++) {
                builder.append("<barChartColumnName>").append(barChartColumnNames1[i]).append("</barChartColumnName>");
            }
        }
        builder.append("</barChartColumnNames1>");

        builder.append("<barChartColumnNames2>");
        if (barChartColumnNames2 != null) {
            for (int i = 0; i < barChartColumnNames2.length; i++) {
                builder.append("<barChartColumnName>").append(barChartColumnNames2[i]).append("</barChartColumnName>");
            }
        }
        builder.append("</barChartColumnNames2>");
        builder.append("<barChartColumnTitles1>");
        if (barChartColumnTitles1 != null) {
            for (int i = 0; i < barChartColumnTitles1.length; i++) {
                builder.append("<barChartColumnTitle>").append(barChartColumnTitles1[i]).append("</barChartColumnTitle>");
            }
        }
        builder.append("</barChartColumnTitles1>");
        builder.append("<barChartColumnTitles2>");
        if (barChartColumnTitles2 != null) {
            for (int i = 0; i < barChartColumnTitles2.length; i++) {
                builder.append("<barChartColumnTitle>").append(barChartColumnTitles2[i]).append("</barChartColumnTitle>");
            }
        }
        builder.append("</barChartColumnTitles2>");

        builder.append("<fromDate>").append(getFromDate()).append("</fromDate>");
        builder.append("<toDate>").append(getToDate()).append("</toDate>");
        builder.append("<targetPerDay>").append(getTargetPerDay()).append("</targetPerDay>");
        builder.append("<stackedType>").append(getStackedType()).append("</stackedType>");
        builder.append("<measureFormat>").append(measureFormat).append("</measureFormat>");
        builder.append("<measureValueRounding>").append(measureValueRounding).append("</measureValueRounding>");
        builder.append("<axisLabelPosition>").append(axisLabelPosition).append("</axisLabelPosition>");
        builder.append("<calibration>").append(calibration).append("</calibration>");
        builder.append("<graphDisplayRows>").append(graphDisplayRows).append("</graphDisplayRows>");
        builder.append("<firstChartType>").append(firstChartType).append("</firstChartType>");
        builder.append("<secondChartType>").append(secondChartType).append("</secondChartType>");
        builder.append("<rgbColorArray>");
        if (rgbColorArr != null) {
            for (int i = 0; i < rgbColorArr.length; i++) {
                builder.append("<rgbColorArr>").append(rgbColorArr[i]).append("</rgbColorArr>");
            }
        }
        builder.append("</rgbColorArray>");
        builder.append("</Graph_Property>");
        return builder.toString();
    }

    public String getGraphLegendLoc() {
        return graphLegendLoc;
    }

    public void setGraphLegendLoc(String graphLegendLoc) {
        this.graphLegendLoc = graphLegendLoc;
    }

    public String getShowGT() {
        return showGT;
    }

    public void setShowGT(String showGT) {
        this.showGT = showGT;
    }

    public String getGraphGridLines() {
        return graphGridLines;
    }

    public void setGraphGridLines(String graphGridLines) {
        this.graphGridLines = graphGridLines;
    }

    public String getShowLegends() {
        return showLegends;
    }

    public void setShowLegends(String showLegends) {
        this.showLegends = showLegends;
    }

    public String getGrpSize() {
        return grpSize;
    }

    public void setGrpSize(String grpSize) {
        this.grpSize = grpSize;
    }

    public String getGraphDisplayRows() {
        return graphDisplayRows;
    }

    public void setGraphDisplayRows(String graphDisplayRows) {
        this.graphDisplayRows = graphDisplayRows;
    }

    public String getShowlyAxis() {
        return showlyAxis;
    }

    public void setShowlyAxis(String showlyAxis) {
        this.showlyAxis = showlyAxis;
    }

    public String getShowryAxis() {
        return showryAxis;
    }

    public void setShowryAxis(String showryAxis) {
        this.showryAxis = showryAxis;
    }

    public String getShowxAxis() {
        return showxAxis;
    }

    public void setShowxAxis(String showxAxis) {
        this.showxAxis = showxAxis;
    }

    public String getShowOverAllMin() {
        return showOverAllMin;
    }

    public void setShowOverAllMin(String showOverAllMin) {
        this.showOverAllMin = showOverAllMin;
    }

    public String getShowOverAllMax() {
        return showOverAllMax;
    }

    public void setShowOverAllMax(String showOverAllMax) {
        this.showOverAllMax = showOverAllMax;
    }

    public String getShowCategoryMin() {
        return showCategoryMin;
    }

    public void setShowCategoryMin(String showCategoryMin) {
        this.showCategoryMin = showCategoryMin;
    }

    public String getShowCategoryMax() {
        return showCategoryMax;
    }

    public void setShowCategoryMax(String showCategoryMax) {
        this.showCategoryMax = showCategoryMax;
    }

    public String[] getBarChartColumnNames1() {
        return barChartColumnNames1;
    }

    public void setBarChartColumnNames1(String[] barChartColumnNames1) {
        this.barChartColumnNames1 = barChartColumnNames1;
    }

    public String[] getBarChartColumnNames2() {
        return barChartColumnNames2;
    }

    public void setBarChartColumnNames2(String[] barChartColumnNames2) {
        this.barChartColumnNames2 = barChartColumnNames2;
    }

    public String[] getBarChartColumnTitles1() {
        return barChartColumnTitles1;
    }

    public void setBarChartColumnTitles1(String[] barChartColumnTitles1) {
        this.barChartColumnTitles1 = barChartColumnTitles1;
    }

    public String[] getBarChartColumnTitles2() {
        return barChartColumnTitles2;
    }

    public void setBarChartColumnTitles2(String[] barChartColumnTitles2) {
        this.barChartColumnTitles2 = barChartColumnTitles2;
    }

    public String getTargetValueType() {
        return targetValueType;
    }

    public void setTargetValueType(String targetValueType) {
        this.targetValueType = targetValueType;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public double getTargetPerDay() {
        return targetPerDay;
    }

    public void setTargetPerDay(double targetPerDay) {
        this.targetPerDay = targetPerDay;
    }

    public String getStackedType() {
        return stackedType;
    }

    public void setStackedType(String stackedType) {
        this.stackedType = stackedType;
    }

    public boolean isStackedFlag() {
        return stackedFlag;
    }

    public void setStackedFlag(boolean stackedFlag) {
        this.stackedFlag = stackedFlag;
    }

    public String[] getColorSeries() {
        return this.colorSeries;
    }

    public void setColorSeries(String[] colorSeries) {
        this.colorSeries = colorSeries;
    }

    public boolean isTransposeData() {
        return this.transposeData;
    }

    public void settransposeData(boolean transposeDataval) {
        this.transposeData = transposeDataval;
    }
}
