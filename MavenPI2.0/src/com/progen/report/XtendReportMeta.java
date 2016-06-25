package com.progen.report;

import com.progen.report.template.TemplateMeta;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class XtendReportMeta implements Serializable {
    //coded added by Dinanath to make serializable

    private static final long serialVersionUID = 75264711556229L;
    //end of code by Dinanath
    private String reportName;
    private List<String> viewbys = new ArrayList<String>();
    private List<String> viewbyIds = new ArrayList<String>();
    private List<String> measures;
    private List<String> defaultMeasure;
    private List<String> defaultMeasureId;
    private List<String> measureIds;
    private List<String> dimensions;
    private List<String> aggregations;
    private List<String> nameMap;
    private Map<String, List<String>> filterMap;
    private Map<String, List<String>> FilterMapgraph;
    private Map<String, List<String>> drills;
    private Map<String, List<String>> Notfilters;
    private String dataPath;
    private boolean isMap = false;
    private String tempJson;
    private String onDimSort;
    private boolean isHirarchichal = false;
    private String chartType;
    private String dashboardType;
    private String drillStatus;
    private String Others;
    private String drillType;
    private String drillFormat;
    private List<String> chartList;
    private String ismeasureBubble;
    private boolean isDashboard = false;
    private boolean isMultiKpi = false;
    private boolean isTable = false;
    private String filterType;
    private List<String> filterParams;
    private String crossTab;
    private String drillIn;
    private String legends;
    private List<String> orderMeasure;
    private List<String> orderViewBy;
    private String isCompare;
    private String priorToDate;
    private String priorFromDate;
    private String priorDateFormat;
    private String curToDate;
    private String curFromDate;
    private boolean isMultipleCsv = false;
    private boolean isOverAllWordCloud = false;
    private String jsonPath;
    private String tableAttribute;
    private Map<String, Map<String, String>> attributes;
    private Boolean updateFlag;
    private String reportData;
    private String tarPath;
    private String mapBy;
    private boolean isOverLay = false;
    private Map<String, DashboardChartData> chartData;
    private Map<String, TemplateMeta> templateMeta;
    private Map<String, DashboardChartMeta> chartMeta;
    private List<String> headers;
    private Map<String, String> commonColList;
    private Map<String, String> colorMap;
    private Map<String, String> timeMap;
    private Map<String, Map<String, Double>> countDis;
    private String isSection;
    private List<String> driverList;
    private Map<String, String> visualChartType;
    private String currType;
    private String shadeType;
    private String isShaded;
    private String changeVisualType;
    private Map<String, String> measureColor;
    private Map<String, String> numberFormatMap;
    private Map<String, Map<String, String>> conditionalMap;
    private String conditionalMeasure;
    private String enablefilter;
    private List<String> draggableViewBys = new ArrayList<String>();
    private List<String> Timedetails = new ArrayList<String>();
    //added by shobhit lfor multi dashboard on 22/09/15
    private String parentViewBy;
    private List<String> childViewBys = new ArrayList<String>();
    private List<String> childMeasBys = new ArrayList<String>();
    private List<String> selectedViewBys = new ArrayList<String>();
    private List<String> selectedMeasBys = new ArrayList<String>();
    private String currencyType;
    public String graphfiltersize = "";
    private List<String> pageSequence = new ArrayList<String>();
      private Map<String, Map<String, Map<String, String>>> reportPageMapping;
    //end by shobhit
    private String advanceChartType;
//    private List<String> measureIds;
    private Map<String,Map<String,DashboardChartMeta>> advanceChartData;

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public List<String> getViewbys() {
        return viewbys;
    }

    public void setViewbys(List<String> viewbys) {
        this.viewbys = viewbys;
    }

    public List<String> getViewbysIds() {
        return viewbyIds;
    }

    public void setViewbysIds(List<String> viewbyIds) {
        this.viewbyIds = viewbyIds;
    }

    public List<String> getMeasures() {
        return measures;
    }

    public void setMeasures(List<String> measures) {
        this.measures = measures;
    }

    public List<String> getMeasuresIds() {
        return measureIds;
    }

    public void setMeasuresIds(List<String> measuresIds) {
        this.measureIds = measuresIds;
    }

    public List<String> getDimensions() {
        return dimensions;
    }

    public void setDimensions(List<String> dimensions) {
        this.dimensions = dimensions;
    }

    public List<String> getAggregations() {
        return aggregations;
    }

    public void setAggregations(List<String> aggregations) {
        this.aggregations = aggregations;
    }

    public List<String> getNameMap() {
        return nameMap;
    }

    public void setNameMap(List<String> nameMap) {
        this.nameMap = nameMap;
    }

    public void setFilterMap(Map<String, List<String>> filterMap) {
        this.filterMap = filterMap;
    }

    public Map<String, List<String>> getFilterMap() {
        return filterMap;
    }

    public void setFilterMapgraph(Map<String, List<String>> FilterMapgraph) {
        this.FilterMapgraph = FilterMapgraph;
    }

    public Map<String, List<String>> getFilterMapgraph() {
        return FilterMapgraph;
    }

    public void setDrills(Map<String, List<String>> drills) {
        this.drills = drills;
    }

    public Map<String, List<String>> getDrills() {
        return drills;
    }
    public void setNotfilters(Map<String, List<String>> Notfilters) {
        this.Notfilters = Notfilters;
    }

    public Map<String, List<String>> getNotfilters() {
        return Notfilters;
    }

    public void setFilterType(String filterType) {
        this.filterType = filterType;
    }

    public String getFilterType() {
        return filterType;
    }

    public String getDataPath() {
        return dataPath;
    }

    public void setDataPath(String dataPath) {
        this.dataPath = dataPath;
    }

    public boolean getIsMap() {
        return isMap;
    }

    public void setIsMap(boolean isMap) {
        this.isMap = isMap;
    }

    public String getTempJson() {
        return tempJson;
    }

    public void setTempJson(String tempJson) {
        this.tempJson = tempJson;
    }

    public String getOnDimSort() {
        return onDimSort;
    }

    public void setOnDimSort(String onDimSort) {
        this.onDimSort = onDimSort;
    }

    public boolean getIsHirarchichal() {
        return isHirarchichal;
    }

    public void setIsHirarchichal(boolean isHirarchichal) {
        this.isHirarchichal = isHirarchichal;
    }

    public String getChartType() {
        return chartType;
    }

    public void setChartType(String chartType) {
        this.chartType = chartType;
    }

    public String getDashboardType() {
        return dashboardType;
    }

    public void setDashboardType(String dashboardType) {
        this.dashboardType = dashboardType;
    }

    public String getDrillStatus() {
        return drillStatus;
    }

    public void setDrillStatus(String drillStatus) {
        this.drillStatus = drillStatus;
    }

    public String getOthers() {
        return Others;
    }

    public void setOthers(String Others) {
        this.Others = Others;
    }

    public String getDrillType() {
        return drillType;
    }

    public void setDrillType(String drillType) {
        this.drillType = drillType;
    }

    public List<String> getChartList() {
        return chartList;
    }

    public void setChartList(List<String> chartList) {
        this.chartList = chartList;
    }

    public String getIsmeasureBubble() {
        return ismeasureBubble;
    }

    public void setIsmeasureBubble(String ismeasureBubble) {
        this.ismeasureBubble = ismeasureBubble;
    }

    public boolean getIsDashboard() {
        return isDashboard;
    }

    public void setIsDashboard(boolean isDashboard) {
        this.isDashboard = isDashboard;
    }

    public boolean getIsMultiKpi() {
        return isMultiKpi;
    }

    public void setIsMultiKpi(boolean isMultiKpi) {
        this.isMultiKpi = isMultiKpi;
    }

    public boolean getIsTable() {
        return isTable;
    }

    public void setIsTable(boolean isTable) {
        this.isTable = isTable;
    }

    public List<String> getFilterParams() {
        return filterParams;
    }

    public void setFilterParams(List<String> filterParams) {
        this.filterParams = filterParams;
    }

    public String getCrossTab() {
        return crossTab;
    }

    public void setCrossTab(String crossTab) {
        this.crossTab = crossTab;
    }

    public String getDrillIn() {
        return drillIn;
    }

    public void setDrillIn(String drillIn) {
        this.drillIn = drillIn;
    }

    public String getLegends() {
        return legends;
    }

    public void setLegends(String legends) {
        this.legends = legends;
    }

    public List<String> getOrderMeasure() {
        return orderMeasure;
    }

    public void setOrderMeasure(List<String> orderMeasure) {
        this.orderMeasure = orderMeasure;
    }

    public List<String> getOrderViewBy() {
        return orderViewBy;
    }

    public void setOrderViewBy(List<String> orderViewBy) {
        this.orderViewBy = orderViewBy;
    }

    public String getIsCompare() {
        return isCompare;
    }

    public void setIsCompare(String isCompare) {
        this.isCompare = isCompare;
    }

    public String getPriorToDate() {
        return priorToDate;
    }

    public void setPriorToDate(String priorToDate) {
        this.priorToDate = priorToDate;
    }

    public String getPriorFromDate() {
        return priorFromDate;
    }

    public void setPriorFromDate(String priorFromDate) {
        this.priorFromDate = priorFromDate;
    }

    public String getPriorDateFormat() {
        return priorDateFormat;
    }

    public void setPriorDateFormat(String priorDateFormat) {
        this.priorDateFormat = priorDateFormat;
    }

    public String getCurToDate() {
        return curToDate;
    }

    public void setCurToDate(String curToDate) {
        this.curToDate = curToDate;
    }

    public String getCurFromDate() {
        return curFromDate;
    }

    public void setCurFromDate(String curFromDate) {
        this.curFromDate = curFromDate;
    }

    public boolean getIsMultipleCsv() {
        return isMultipleCsv;
    }

    public void setIsMultipleCsv(boolean isMultipleCsv) {
        this.isMultipleCsv = isMultipleCsv;
    }

    public boolean getIsOverAllWordCloud() {
        return isOverAllWordCloud;
    }

    public void setIsOverAllWordCloud(boolean isOverAllWordCloud) {
        this.isOverAllWordCloud = isOverAllWordCloud;
    }

    public String getJsonPath() {
        return jsonPath;
    }

    public void setJsonPath(String jsonPath) {
        this.jsonPath = jsonPath;
    }

    public String getTableAttribute() {
        return tableAttribute;
    }

    public void setTableAttribute(String tableAttribute) {
        this.tableAttribute = tableAttribute;
    }

    public Map<String, Map<String, String>> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, Map<String, String>> attributes) {
        this.attributes = attributes;
    }

    public Boolean getUpdateFlag() {
        return updateFlag;
    }

    void setUpdateFlag(Boolean updateFlag) {
        this.updateFlag = updateFlag;
    }

    public String getReportData() {
        return reportData;
    }

    public void setReportData(String reportData) {
        this.reportData = reportData;
    }

    public String getTarPath() {
        return tarPath;
    }

    public void setTarPath(String tarPath) {
        this.tarPath = tarPath;
    }

    public String getMapBy() {
        return mapBy;
    }

    public void setMapBy(String mapBy) {
        this.mapBy = mapBy;
    }

    public boolean getIsOverLay() {
        return isOverLay;
    }

    public void setIsOverLay(boolean isOverLay) {
        this.isOverLay = isOverLay;
    }

    public Map<String, DashboardChartData> getChartData() {
        return chartData;
    }

    public void setChartData(Map<String, DashboardChartData> chartData) {
        this.chartData = chartData;
    }

    public List<String> getHeaders() {
        return headers;
    }

    public void setHeaders(List<String> headers) {
        this.headers = headers;
    }

    public Map<String, String> getColorMap() {
        return colorMap;
    }

    public void setColorMap(Map<String, String> colorMap) {
        this.colorMap = colorMap;
    }

    public Map<String, Map<String, Double>> getCountDistinctMap() {
        return countDis;
    }

    public void setCountDistinctMap(Map<String, Map<String, Double>> countDis) {
        this.countDis = countDis;
    }

    public Map<String, String> getCommonColList() {
        return commonColList;
    }

    public void setCommonColList(Map<String, String> commonColList) {
        this.commonColList = commonColList;
    }

    public String getIsSection() {
        return isSection;
    }

    public void setIsSection(String isSection) {
        this.isSection = isSection;
    }

    /**
     * @return the driverList
     */
    public List<String> getDriverList() {
        return driverList;
    }

    /**
     * @param driverList the driverList to set
     */
    public void setDriverList(List<String> driverList) {
        this.driverList = driverList;
    }

    /**
     * @return the visualChartType
     */
    public Map<String, String> getVisualChartType() {
        return visualChartType;
    }

    /**
     * @param visualChartType the visualChartType to set
     */
    public void setVisualChartType(Map<String, String> visualChartType) {
        this.visualChartType = visualChartType;
    }

    /**
     * @return the currtype
     */
    public String getCurrType() {
        return currType;
    }

    /**
     * @param currtype the currtype to set
     */
    public void setCurrType(String currType) {
        this.currType = currType;
    }

    public String getchangeVisualType() {
        return changeVisualType;
    }

    /**
     * @param currtype the currtype to set
     */
    public void setchangeVisualType(String changeVisualType) {
        this.changeVisualType = changeVisualType;
    }

    /**
     * @return the measureColor
     */
    public Map<String, String> getMeasureColor() {
        return measureColor;
    }

    /**
     * @param measureColor the measureColor to set
     */
    public void setMeasureColor(Map<String, String> measureColor) {
        this.measureColor = measureColor;
    }

    /**
     * @return the shadeType
     */
    public String getShadeType() {
        return shadeType;
    }

    /**
     * @param shadeType the shadeType to set
     */
    public void setShadeType(String shadeType) {
        this.shadeType = shadeType;
    }

    /**
     * @return the isShaded
     */
    public String getIsShaded() {
        return isShaded;
    }

    /**
     * @param isShaded the isShaded to set
     */
    public void setIsShaded(String isShaded) {
        this.isShaded = isShaded;
    }

    /**
     * @return the conditionalMeasure
     */
    public String getConditionalMeasure() {
        return conditionalMeasure;
    }

    /**
     * @param conditionalMeasure the conditionalMeasure to set
     */
    public void setConditionalMeasure(String conditionalMeasure) {
        this.conditionalMeasure = conditionalMeasure;
    }

    /**
     * @return the conditionalMap
     */
    public Map<String, Map<String, String>> getConditionalMap() {
        return conditionalMap;
    }

    /**
     * @param conditionalMap the conditionalMap to set
     */
    public void setConditionalMap(Map<String, Map<String, String>> conditionalMap) {
        this.conditionalMap = conditionalMap;
    }

    /**
     * @return the draggableViewBys
     */
    public List<String> getDraggableViewBys() {
        return draggableViewBys;
    }

    /**
     * @param draggableViewBys the draggableViewBys to set
     */
    public void setDraggableViewBys(List<String> draggableViewBys) {
        this.draggableViewBys = draggableViewBys;
    }

    public List<String> getTimedetails() {
        return Timedetails;
    }

    /**
     * @param draggableViewBys the draggableViewBys to set
     */
    public void setTimedetails(List<String> Timedetails) {
        this.Timedetails = Timedetails;
    }

    public String getenablefilter() {
        return enablefilter;
    }

    /**
     * @param conditionalMeasure the conditionalMeasure to set
     */
    public void setenablefilter(String enablefilter) {
        this.enablefilter = enablefilter;
    }

    /**
     * @return the timeMap
     */
    public Map<String, String> getTimeMap() {
        return timeMap;
    }

    /**
     * @param timeMap the timeMap to set
     */
    public void setTimeMap(Map<String, String> timeMap) {
        this.timeMap = timeMap;
    }

    /**
     * @return the parentViewBy
     */
    public String getParentViewBy() {
        return parentViewBy;
    }

    /**
     * @param parentViewBy the parentViewBy to set
     */
    public void setParentViewBy(String parentViewBy) {
        this.parentViewBy = parentViewBy;
    }

    /**
     * @return the childViewBys
     */
    public List<String> getChildViewBys() {
        return childViewBys;
    }

    /**
     * @param childViewBys the childViewBys to set
     */
    public void setChildViewBys(List<String> childViewBys) {
        this.childViewBys = childViewBys;
    }

    /**
     * @return the childMeasBys
     */
    public List<String> getChildMeasBys() {
        return childMeasBys;
    }

    /**
     * @param childMeasBys the childMeasBys to set
     */
    public void setChildMeasBys(List<String> childMeasBys) {
        this.childMeasBys = childMeasBys;
    }

    /**
     * @return the selectedViewBys
     */
    public List<String> getSelectedViewBys() {
        return selectedViewBys;
    }

    /**
     * @param selectedViewBys the selectedViewBys to set
     */
    public void setSelectedViewBys(List<String> selectedViewBys) {
        this.selectedViewBys = selectedViewBys;
    }

    /**
     * @return the selectedMeasBys
     */
    public List<String> getSelectedMeasBys() {
        return selectedMeasBys;
    }

    /**
     * @param selectedMeasBys the selectedMeasBys to set
     */
    public void setSelectedMeasBys(List<String> selectedMeasBys) {
        this.selectedMeasBys = selectedMeasBys;
    }

    /**
     * @return the drillFormat
     */
    public String getDrillFormat() {
        return drillFormat;
    }

    /**
     * @param drillFormat the drillFormat to set
     */
    public void setDrillFormat(String drillFormat) {
        this.drillFormat = drillFormat;
    }

    /**
     * @return the currencyType
     */
    public String getCurrencyType() {
        return currencyType;
    }

    /**
     * @param currencyType the currencyType to set
     */
    public void setCurrencyType(String currencyType) {
        this.currencyType = currencyType;
    }

    /**
     * @return the numberFormatMap
     */
    public Map<String, String> getNumberFormatMap() {
        return numberFormatMap;
    }

    /**
     * @param numberFormatMap the numberFormatMap to set
     */
    public void setNumberFormatMap(Map<String, String> numberFormatMap) {
        this.numberFormatMap = numberFormatMap;
    }
    //added by sruthi to show filters

    public void setgraphfiltersize(String currencyType) {
        this.graphfiltersize = currencyType;
    }

    public String getgraphfiltersize() {
        return graphfiltersize;
    }
    //ended by sruthi

    /**
     * @return the defaultMeasure
     */
    public List<String> getDefaultMeasure() {
        return defaultMeasure;
    }

    /**
     * @param defaultMeasure the defaultMeasure to set
     */
    public void setDefaultMeasure(List<String> defaultMeasure) {
        this.defaultMeasure = defaultMeasure;
    }

    /**
     * @return the defaultMeasureId
     */
    public List<String> getDefaultMeasureId() {
        return defaultMeasureId;
    }

    /**
     * @param defaultMeasureId the defaultMeasureId to set
     */
    public void setDefaultMeasureId(List<String> defaultMeasureId) {
        this.defaultMeasureId = defaultMeasureId;
    }

    /**
     * @return the chartMeta
     */
    public Map<String, DashboardChartMeta> getChartMeta() {
        return chartMeta;
    }

    /**
     * @param chartMeta the chartMeta to set
     */
    public void setChartMeta(Map<String, DashboardChartMeta> chartMeta) {
        this.chartMeta = chartMeta;
    }
    /**
     * @return the reportPageMapping
     */
    public Map<String, Map<String, Map<String, String>>> getReportPageMapping() {
        return reportPageMapping;
    }

    /**
     * @param reportPageMapping the reportPageMapping to set
     */
    public void setReportPageMapping(Map<String, Map<String, Map<String, String>>> reportPageMapping) {
        this.reportPageMapping = reportPageMapping;
    }

    /**
     * @return the templateMeta
     */
    public Map<String, TemplateMeta> getTemplateMeta() {
        return templateMeta;
}

    /**
     * @param templateMeta the templateMeta to set
     */
    public void setTemplateMeta(Map<String, TemplateMeta> templateMeta) {
        this.templateMeta = templateMeta;
    }
    
    /**
     * @return the advanceChartType
     */
    public String getAdvanceChartType() {
        return advanceChartType;
}

    /**
     * @param advanceChartType the advanceChartType to set
     */
    public void setAdvanceChartType(String advanceChartType) {
        this.advanceChartType = advanceChartType;
    }

    /**
     * @return the advanceChartData
     */
    public Map<String,Map<String,DashboardChartMeta>> getAdvanceChartData() {
        return advanceChartData;
    }

    /**
     * @param advanceChartData the advanceChartData to set
     */
    public void setAdvanceChartData(Map<String,Map<String,DashboardChartMeta>> advanceChartData) {
        this.advanceChartData = advanceChartData;
    }
}