package com.progen.report;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class DashboardChartData implements Serializable {

    private static final long serialVersionUID = 75264711556232L;
    private List<String> viewBys;
    private List<String> viewIds;
    private List<String> colViewBys;
    private List<String> colViewIds;
    private List<String> dimensions;
    private List<String> aggregation;
    private List<String> meassures;
    private List<String> comparedMeasure;
    private List<String> comparedMeasureId;
    private List<String> comparedMeasureAgg;
    private List<String> runtimeMeasure;
    private List<String> defaultMeasures;
    private List<String> defaultMeasureIds;
    private List<String> meassureIds;
    private Map<String, List<String>> filters;
    private Map<String, List<String>> comparableFilters;
    private Map<String, List<String>> comparedValue;
    private String size;
    private String Name;
    private String records;
    private String dataDisplay;
    private String sorting;
    private String sortBasis;
    private String innerSortBasis;
    private String innerSorting;
    private String rounding;
    private String rounding1;
    private String yAxisFormat;
    private String y2AxisFormat;
    private String valueOf;
    private String chartType;
    private String remarks;
    private String viewByLevel;
    private String isHirarchichal;
    private String viewType;
    private String colorLegend;
    private String label;
    private String width;
    private String height;
    private String csvName;
    private String workDir;
    private String comment;
    private String align;
    private String numOfCharts;
    private List<String> dimensionsIds;
    private List<String> dimensionsNames;
    private String innerRecords;
    private List<Map<String, String>> charts;
    private List<String> measures1;
    private List<String> measures2;
    private List<String> chartList;
    private Map<String, String> yaxisrange;
    private Map<String, String> y1axisrange;
    private String displayX;
    private String displayY;
    private String displayY1;
    private String drillType;
    private String others;
    private String transpose;
    private String othersL;
    private String globalEnable;
    private String SubTotalProp;
    private String TargetMax;
    private String TargetMin;
    private String Target;
    private String colorPicker;
    private String GridLines;
    private String divBackGround;
    private String globalfilter;
    private List<String> hideLabel;
    private Map<String, String> dialValues;
    private Map<String, String> graphDrillMap;
    private Map<String, Map<String, String>> measureFilters;
    private String row;
    private String col;
    private String size_x;
    private String size_y;
    private String id;
    private String displayLegends;
    private String KPIName;
    private String Prefix;
    private String Suffix;
    private String Tilealign;
    private String filename;
    private String legendLocation;
    private String innerLabels;
    private String showST;// add by maynk sh.
    private String showGT; // add by maynk sh.
    private String fontsize;// add by maynk sh.
    private String lineType;// add by maynk sh.
    private String kpiFont; // add by maynk sh.
    private String kpiGTFont;// add by maynk sh.
    private String lableColor;// add by maynk sh.
    private String legendNo;// add by maynk sh.
    private Map<String, String> measureAlias;// add by maynk sh.
    private String appendNumberFormat;// add by maynk sh.
    private String targetLine;
    private String labelPosition;
    private Map<String, String> colorMapPicker;
    private Map<String, String> customColors;
    private String isFilterApplied;
    private String XaxisRange;//Added By Ram
    private String refElementType;
    private String GTValue;
    private List<String> GTValueList;
    private String showPercent;
    private String showEmoji;
    private String imageType;
    private String emojiPosition;
    private String enableGraphDrill;
    private String dialSelectChart;
    private String lbColor;
    private List<String> emojiValue;
    private List<String> emojiAbsValue;
    private String Pattern;
    private String MaxRange;
    private String LabelPos;
    private String stackLight;
    private String stackDark;
    private String lbPosition;
    private String displayXLine;
    private String displayYLine;
    private List<Map<String, Map<String, String>>> bulletParameters;//add by mynk sh.
    private Map<String, Map<String, String>> refElementInfo;
    private String dDLable;
    private String editXLable;
    private String startRecords;
    private String endRecords;
    private String headingFontSize;
    private String chartFontType;
    private Map<String, List<String>> anchorChart;
    private Map<String, List<String>> localDrills;
    private String dialMeasureSuffix;
    private String Prefixfontsize;
    private String Suffixfontsize;
    private List<String> PrefixList;
    private List<String> PrefixfontsizeList;
    private List<String> SuffixList;
    private List<String> SuffixfontsizeList;
    private String radialDefaultColor;
    private String toolTip;
    private String dialType;
    private String lFilledFont;
    private String chartDecription;
    private String bulletAbovecolor;
    private String bulletBelowcolor;
    private String bulletHighcolor;
    private String bulletMediumcolor;
    private String bulletLowcolor;
    private Map<String, String> numberFormat;
    private Map<String, String> numberRounding;
    private String XaxisTicks;
    private String enableQuickFilter2;
    private Map<String, String> dataDisplayArr;
    private Map<String, String> measureLabelLength;
    private String legendBoxBorder;
    private String labelColor;
    private Map<String, String> measureValueSize;
    private Map<String, String> measureNameSize;
    private String DualAxisProp;
    private String roundingType;
    private Map<String, String> createBarLine;
    private String labelColors;
    private String Afontsize;
    private String trlineType;
    private Map<String, String> measureAvg;
    private String multiFilter;
    private String isDashboardDefined;
    private String chartTypeBarTrend;
    private Map<String, String> dataLabelType;
    private Map<String, String> defineTargetline;
    private Map<String, List<String>> QuickFilterValue;
    private String gtGraph;
    private String multiselecctQuickFilter;
    private String quickFilterLength;
    private String transposeMeasure;
    private List<Map<String, String>> selectedComparison;
    private List<Map<String, String>> timeComparison;
    private String quickViewname;
    private String legendPrintType;
    private String valueTilealign;
    private Map<String, String> dLabelDisplay;
    private String dataLabDisplay;
    private String completeChartData;
    private String iconShowHide;
    private String QuickFilterId;
    private String tableBorder;
    private String legendFontSize;
    private String showViewByinLBox;
    private String SuffixFormat;
    private String changeType;
    private String changeEnable;
    private String showViewBy;
    private String lineSymbol;
    private String lineSymbol1;
    private Map<String, String> measValueAlign;
    private Map<String, String> measNameAlign;
    private String AxisNameColor;
    private Map<String, String> sortMeasure;
    private String hideDate;
    private String hideBorder;
    private String labelFSize;
    private String chartTypeComparison;
    private String isComparison;
    private String kpiSubData;
    private String fontSize1;
    private String tableWithSymbol;
    private String enableVieweby;
    private String enableMeasures;
    private Map<String, String> ticksValueSize;
    private Map<String, String> dataColor;
    private Map<String, String> measureColor;
    private String prevPeriodDisplay;
    private Map<String, List<String>> MapTransformation;
    private String circularChartTab;
    private String enableComparison;
    private String barsize;
    private String QuickFilterShowHide;
    private String excludeFromDrill;
    private Map<String, String> LabFtColor;
    private String comparisonType;
    private List<String> currentMeasures;
    private String titleFontSize;
    private String titleColor;
    private String FilledColor1;
    private String FilledColor2;
    private String FilledColor3;
    private String FilledColor4;
    private List<String> ParentMeasures;
    private String comparativeValue;
    private Map<String, String> comparativeValueFont;
    private Map<String, String> measureAliasCombo;
    private String mapCountryName;
    private String trendViewMeasures;
    private String dataTranspose;
    private String fillBackground;
    private String valueOf1;
    private String absoluteValue;
    private String startCurrentRecords;
    private String endCurrentRecords;
    private Map<String, String> currencySymbol;
    private List<String> timeBasedData;
    private String timeEnable;
    private boolean comboData;
    private String comboType;
    private String changPercentArrow;
    private String TableFontColor;
    private String viewByDisplayType;
    private String doublePie;
    private List<Map<String, String>> trendChartData;
    private List<String> selectedMeasures;
    private String GridLines1;
    private String kpiMultiSelect;
    private String xAxisFont;
    private String yAxisFont;
    private boolean isKPI = false;
    private List<String> customTimeComparisons;
    private Map<String, String> groupedBarWith;
    private Map<String, String> gradientLogicalMap;
    private Map<String, String> tooltipType;
    private Map<String, String> dataSlider;
    private Map<String, Map<String, String>> sliderAxisVal;
    private List<Map<String, String>> logicalParameters;
    private String usBar;
    private String equalInterval;
    private String enableRootAnalysis;
    private String mapdrill;
    private String measure_seq;
         private String mapMultiLevelDrill;
    private Map<String,List<String>> dataSliderMinMaxValue;
//     private Map<String, String> colorMapGradient;
    private Map<String, List<String>> crossTableHeaderMap;
    private List<String> crossTabFinalOrder;
    private String topChartOrder;
    private String dateFlag_New;
    private String drillPage;
    private String chartNameColol;
    private Map<String, Map<String, List<String>>> actionDrillMap;
    private String customtimeType ;
    private String customTimeflag ;
    private String customTimeDate ;
    
    public List<String> getViewBys() {
        return viewBys;
    }

    public void setViewBys(List<String> viewBys) {
        this.viewBys = viewBys;
    }

    public List<String> getDimensions() {
        return dimensions;
    }

    public void setDimensions(List<String> dimensions) {
        this.dimensions = dimensions;
    }

    public List<String> getAggregation() {
        return aggregation;
    }

    public void setAggregation(List<String> aggregation) {
        this.aggregation = aggregation;
    }

    public List<String> getMeassures() {
        return meassures;
    }

    public void setMeassures(List<String> meassures) {
        this.meassures = meassures;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getRecords() {
        return records;
    }

    public void setRecords(String records) {
        this.records = records;
    }

    public String getDataDisplay() {
        return dataDisplay;
    }

    public void setDataDisplay(String dataDisplay) {
        this.dataDisplay = dataDisplay;
    }

    public String getValueOf() {
        return valueOf;
    }

    public void setValueOf(String valueOf) {
        this.valueOf = valueOf;
    }

    public String getChartType() {
        return chartType;
    }

    public void setChartType(String chartType) {
        this.chartType = chartType;
    }

    public String getViewByLevel() {
        return viewByLevel;
    }

    public void setViewByLevel(String viewByLevel) {
        this.viewByLevel = viewByLevel;
    }

    public String getIsHirarchichal() {
        return isHirarchichal;
    }

    public void setIsHirarchichal(String isHirarchichal) {
        this.isHirarchichal = isHirarchichal;
    }

    public String getViewType() {
        return viewType;
    }

    public void setViewType(String viewType) {
        this.viewType = viewType;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public Map<String, List<String>> getFilters() {
        return filters;
    }

    public void setFilters(Map<String, List<String>> filters) {
        this.filters = filters;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCsvName() {
        return csvName;
    }

    public void setCsvName(String csvName) {
        this.csvName = csvName;
    }

    public String getWorkDir() {
        return workDir;
    }

    public void setWorkDir(String workDir) {
        this.workDir = workDir;
    }

    public String getAlign() {
        return align;
    }

    public void setAlign(String align) {
        this.align = align;
    }

    public String getNumOfCharts() {
        return numOfCharts;
    }

    public void setNumOfCharts(String numOfCharts) {
        this.numOfCharts = numOfCharts;
    }

    public List<Map<String, String>> getCharts() {
        return charts;
    }

    public void setCharts(List<Map<String, String>> charts) {
        this.charts = charts;
    }

    public String getSorting() {
        return sorting;
    }

    public void setSorting(String sorting) {
        this.sorting = sorting;
    }

    public String getSortBasis() {
        return sortBasis;
    }

    public void setSortBasis(String sortBasis) {
        this.sortBasis = sortBasis;
    }

    public String getRounding() {
        return rounding;
    }

    public void setRounding(String rounding) {
        this.rounding = rounding;
    }

    public String getyAxisFormat() {
        return yAxisFormat;
    }

    public void setyAxisFormat(String yAxisFormat) {
        this.yAxisFormat = yAxisFormat;
    }

    /**
     * @return the viewIds
     */
    public List<String> getViewIds() {
        return viewIds;
    }

    /**
     * @param viewIds the viewIds to set
     */
    public void setViewIds(List<String> viewIds) {
        this.viewIds = viewIds;
    }

    /**
     * @return the meassureIds
     */
    public List<String> getMeassureIds() {
        return meassureIds;
    }

    /**
     * @param meassureIds the meassureIds to set
     */
    public void setMeassureIds(List<String> meassureIds) {
        this.meassureIds = meassureIds;
    }

    /**
     * @return the displayX
     */
    public String getDisplayX() {
        return displayX;
    }

    /**
     * @param displayX the displayX to set
     */
    public void setDisplayX(String displayX) {
        this.displayX = displayX;
    }

    /**
     * @return the displayY
     */
    public String getDisplayY() {
        return displayY;
    }

    /**
     * @param displayY the displayY to set
     */
    public void setDisplayY(String displayY) {
        this.displayY = displayY;
    }

    /**
     * @return the displayLegend
     */
    public String getDisplayLegends() {
        return displayLegends;
    }

    /**
     * @param displayLegend the displayLegend to set
     */
    public void setDisplayLegends(String displayLegend) {
        this.displayLegends = displayLegends;
    }

    /**
     * @return the colorLegend
     */
    public String getColorLegend() {
        return colorLegend;
    }

    /**
     * @param colorLegend the colorLegend to set
     */
    public void setColorLegend(String colorLegend) {
        this.colorLegend = colorLegend;
    }

    /**
     * @return the displayY1
     */
    public String getDisplayY1() {
        return displayY1;
    }

    /**
     * @param displayY1 the displayY1 to set
     */
    public void setDisplayY1(String displayY1) {
        this.displayY1 = displayY1;
    }

    public List<String> getmeasures1() {
        return measures1;
    }

    public void setmeasures1(List<String> measures1) {
        this.measures1 = measures1;
    }

    public List<String> getmeasures2() {
        return measures2;
    }

    public void setmeasures2(List<String> measures2) {
        this.measures2 = measures2;
    }

    public List<String> getchartList() {
        return chartList;
    }

    public void setchartList(List<String> measures2) {
        this.chartList = chartList;
    }

    /**
     * @return the innerRecords
     */
    public String getInnerRecords() {
        return innerRecords;
    }

    /**
     * @param innerRecords the innerRecords to set
     */
    public void setInnerRecords(String innerRecords) {
        this.innerRecords = innerRecords;
    }

    /**
     * @return the innerSortBasis
     */
    public String getInnerSortBasis() {
        return innerSortBasis;
    }

    /**
     * @param innerSortBasis the innerSortBasis to set
     */
    public void setInnerSortBasis(String innerSortBasis) {
        this.innerSortBasis = innerSortBasis;
    }

    /**
     * @return the innerSorting
     */
    public String getInnerSorting() {
        return innerSorting;
    }

    public void setInnerSorting(String innerSorting) {
        this.innerSorting = innerSorting;
    }

    public String getDrillType() {
        return drillType;
    }

    public void setDrillType(String drillType) {
        this.drillType = drillType;
    }

    public String getOthers() {
        return others;
    }

    public void setOthers(String others) {
        this.others = others;
    }

    public Map<String, String> getYaxisrange() {
        return yaxisrange;
    }

    public void setYaxisrange(Map<String, String> yaxisrange) {
        this.yaxisrange = yaxisrange;
    }

    /**
     * @return the othersL
     */
    public String getOthersL() {
        return othersL;
    }

    /**
     * @param othersL the othersL to set
     */
    public void setOthersL(String othersL) {
        this.othersL = othersL;
    }

    public String getglobalEnable() {
        return globalEnable;
    }

    /**
     * @param othersL the othersL to set
     */
    public void setglobalEnable(String globalEnable) {
        this.globalEnable = globalEnable;
    }

    /**
     * @return the hideLabel
     */
    public List<String> getHideLabel() {
        return hideLabel;
    }

    /**
     * @param hideLabel the hideLabel to set
     */
    public void setHideLabel(List<String> hideLabel) {
        this.hideLabel = hideLabel;
    }

    /**
     * @return the y2AxisFormat
     */
    public String getY2AxisFormat() {
        return y2AxisFormat;
    }

    /**
     * @param y2AxisFormat the y2AxisFormat to set
     */
    public void setY2AxisFormat(String y2AxisFormat) {
        this.y2AxisFormat = y2AxisFormat;
    }

    public Map<String, Map<String, String>> getMeasureFilters() {
        return measureFilters;
    }

    public void setMeasureFilters(Map<String, Map<String, String>> measureFilters) {
        this.measureFilters = measureFilters;
    }

    //    added by manik
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

    public String getdisplayLegends() {
        return displayLegends;
    }

    /**
     * @param id the id to set
     */
    public void setdisplayLegends(String displayLegends) {
        this.displayLegends = displayLegends;
    }

    public String getSubTotalProp() {
        return SubTotalProp;
    }

    /**
     * @param SubTotalProp the SubTotalProp to set
     */
    public void setSubTotalProp(String SubTotalProp) {
        this.SubTotalProp = SubTotalProp;
    }

    /**
     * @return the TargetMax
     */
    public String getTargetMax() {
        return TargetMax;
    }

    /**
     * @param TargetMax the TargetMax to set
     */
    public void setTargetMax(String TargetMax) {
        this.TargetMax = TargetMax;
    }

    /**
     * @return the TargetMin
     */
    public String getTargetMin() {
        return TargetMin;
    }

    /**
     * @param TargetMin the TargetMin to set
     */
    public void setTargetMin(String TargetMin) {
        this.TargetMin = TargetMin;
    }

    /**
     * @return the Target
     */
    public String getTarget() {
        return Target;
    }

    /**
     * @param Target the Target to set
     */
    public void setTarget(String Target) {
        this.Target = Target;
    }

    /**
     * @return the colorPicker
     */
    public String getColorPicker() {
        return colorPicker;
    }

    /**
     * @param colorPicker the colorPicker to set
     */
    public void setColorPicker(String colorPicker) {
        this.colorPicker = colorPicker;
    }

    /**
     * @return the GridLines
     */
    public String getGridLines() {
        return GridLines;
    }

    /**
     * @param GridLines the GridLines to set
     */
    public void setGridLines(String GridLines) {
        this.GridLines = GridLines;
    }

    /**
     * @return the KPIName
     */
    public String getKPIName() {
        return KPIName;
    }

    /**
     * @param KPIName the KPIName to set
     */
    public void setKPIName(String KPIName) {
        this.KPIName = KPIName;
    }

    /**
     * @return the rounding1
     */
    public String getRounding1() {
        return rounding1;
    }

    /**
     * @param rounding1 the rounding1 to set
     */
    public void setRounding1(String rounding1) {
        this.rounding1 = rounding1;
    }

    public Map<String, String> getdialValues() {
        return dialValues;
    }

    public void setdialValues(Map<String, String> dialValues) {
        this.dialValues = dialValues;
    }

    public String getPrefix() {
        return Prefix;
    }

    /**
     * @param Prefix the Prefix to set
     */
    public void setPrefix(String Prefix) {
        this.Prefix = Prefix;
    }

    /**
     * @return the Suffix
     */
    public String getSuffix() {
        return Suffix;
    }

    /**
     * @param Suffix the Suffix to set
     */
    public void setSuffix(String Suffix) {
        this.Suffix = Suffix;
    }

    /**
     * @return the Tilealign
     */
    public String getTilealign() {
        return Tilealign;
    }

    /**
     * @param Tilealign the Tilealign to set
     */
    public void setTilealign(String Tilealign) {
        this.Tilealign = Tilealign;
    }

    /**
     * @return the filename
     */
    public String getFilename() {
        return filename;
    }

    /**
     * @param filename the filename to set
     */
    public void setFilename(String filename) {
        this.filename = filename;
    }

    /**
     * @return the colorMapPicker
     */
    public Map<String, String> getColorMapPicker() {
        return colorMapPicker;
    }

    /**
     * @param colorMapPicker the colorMapPicker to set
     */
    public void setColorMapPicker(Map<String, String> colorMapPicker) {
        this.colorMapPicker = colorMapPicker;
    }

    /**
     * @return the transpose
     */
    public String getTranspose() {
        return transpose;
    }

    /**
     * @param transpose the transpose to set
     */
    public void setTranspose(String transpose) {
        this.transpose = transpose;
    }

    /**
     * @return the remarks
     */
    public String getRemarks() {
        return remarks;
    }

    /**
     * @param remarks the remarks to set
     */
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Map<String, String> getCustomColors() {
        return customColors;
    }

    /**
     * @param customColors the customColors to set
     */
    public void setCustomColors(Map<String, String> customColors) {
        this.customColors = customColors;
    }

    /**
     * @return the legendLocation
     */
    public String getLegendLocation() {
        return legendLocation;
    }

    /**
     * @param legendLocation the legendLocation to set
     */
    public void setLegendLocation(String legendLocation) {
        this.legendLocation = legendLocation;
    }

    /**
     * @return the innerLabels
     */
    public String getInnerLabels() {
        return innerLabels;
    }

    /**
     * @param innerLabels the innerLabels to set
     */
    public void setInnerLabels(String innerLabels) {
        this.innerLabels = innerLabels;
    }

    /**
     * @return the targetLine
     */
    public String getTargetLine() {
        return targetLine;
    }

    /**
     * @param targetLine the targetLine to set
     */
    public void setTargetLine(String targetLine) {
        this.targetLine = targetLine;
    }

    /**
     * @return the isFilterApplied
     */
    public String getIsFilterApplied() {
        return isFilterApplied;
    }

    /**
     * @param isFilterApplied the isFilterApplied to set
     */
    public void setIsFilterApplied(String isFilterApplied) {
        this.isFilterApplied = isFilterApplied;
    }

    /**
     * @return the labelPosition
     */
    public String getLabelPosition() {
        return labelPosition;
    }

    /**
     * @param labelPosition the labelPosition to set
     */
    public void setLabelPosition(String labelPosition) {
        this.labelPosition = labelPosition;
    }

    /**
     * @return the globalfilter
     */
    public String getGlobalfilter() {
        return globalfilter;
    }

    /**
     * @param globalfilter the globalfilter to set
     */
    public void setGlobalfilter(String globalfilter) {
        this.globalfilter = globalfilter;
    }
//Added By Ram

    /**
     * @return the XaxisRange
     */
    public String getXaxisRange() {
        return XaxisRange;
    }

    /**
     * @param XaxisRange the XaxisRange to set
     */
    public void setXaxisRange(String XaxisRange) {
        this.XaxisRange = XaxisRange;
    }

    public String getRefElementType() {
        return refElementType;
    }

    /**
     * @param refElementType the refElementType to set
     */
    public void setRefElementType(String refElementType) {
        this.refElementType = refElementType;
    }

    /**
     * @return the refElementInfo
     */
    public Map<String, Map<String, String>> getRefElementInfo() {
        return refElementInfo;
    }

    /**
     * @param refElementInfo the refElementInfo to set
     */
    public void setRefElementInfo(Map<String, Map<String, String>> refElementInfo) {
        this.refElementInfo = refElementInfo;
    }

    /**
     * @return the GTValue
     */
    public String getFontsize() {
        return fontsize;
    }

    public void setFontsize(String fontsize) {
        this.fontsize = fontsize;
    }

    public String getShowST() {
        return showST;
    }

    public void setShowST(String showST) {
        this.showST = showST;
    }

    public String getShowGT() {
        return showGT;
    }

    public void setShowGT(String showGT) {
        this.showGT = showGT;
    }

    /**
     * @return the emojiValue
     */
    public List<String> getEmojiValue() {
        return emojiValue;
    }

    /**
     * @param emojiValue the emojiValue to set
     */
    public void setEmojiValue(List<String> emojiValue) {
        this.emojiValue = emojiValue;
    }

    /**
     * @return the showEmoji
     */
    public String getShowEmoji() {
        return showEmoji;
    }

    /**
     * @param showEmoji the showEmoji to set
     */
    public void setShowEmoji(String showEmoji) {
        this.showEmoji = showEmoji;
    }

    /**
     * @return the emojiAbsValue
     */
    public List<String> getEmojiAbsValue() {
        return emojiAbsValue;
    }

    /**
     * @param emojiAbsValue the emojiAbsValue to set
     */
    public void setEmojiAbsValue(List<String> emojiAbsValue) {
        this.emojiAbsValue = emojiAbsValue;
    }

    /**
     * @return the showPercent
     */
    public String getShowPercent() {
        return showPercent;
    }

    /**
     * @param showPercent the showPercent to set
     */
    public void setShowPercent(String showPercent) {
        this.showPercent = showPercent;
    }

    public String getGTValue() {
        return GTValue;
    }

    /**
     * @return the lineType
     */
    public String getLineType() {
        return lineType;
    }

    /**
     * @param lineType the lineType to set
     */
    public void setLineType(String lineType) {
        this.lineType = lineType;
    }

    /**
     * @param GTValue the GTValue to set
     */
    public void setGTValue(String GTValue) {
        this.GTValue = GTValue;
    }

    /**
     * @return the GTValueList
     */
    public List<String> getGTValueList() {
        return GTValueList;
    }

    /**
     * @param GTValueList the GTValueList to set
     */
    public void setGTValueList(List<String> GTValueList) {
        this.GTValueList = GTValueList;
    }

    /**
     * @return the graphDrillMap
     */
    public Map<String, String> getGraphDrillMap() {
        return graphDrillMap;
    }

    /**
     * @param graphDrillMap the graphDrillMap to set
     */
    public void setGraphDrillMap(Map<String, String> graphDrillMap) {
        this.graphDrillMap = graphDrillMap;
    }

    /**
     * @return the imageType
     */
    public String getImageType() {
        return imageType;
    }

    /**
     * @param imageType the imageType to set
     */
    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    /**
     * @return the kpiFont
     */
    public String getKpiFont() {
        return kpiFont;
    }

    /**
     * @param kpiFont the kpiFont to set
     */
    public void setKpiFont(String kpiFont) {
        this.kpiFont = kpiFont;
    }

    /**
     * @return the kpiGTFont
     */
    public String getKpiGTFont() {
        return kpiGTFont;
    }

    /**
     * @param kpiGTFont the kpiGTFont to set
     */
    public void setKpiGTFont(String kpiGTFont) {
        this.kpiGTFont = kpiGTFont;
    }

    /**
     * @return the emojiPosition
     */
    public String getEmojiPosition() {
        return emojiPosition;
    }

    /**
     * @param emojiPosition the emojiPosition to set
     */
    public void setEmojiPosition(String emojiPosition) {
        this.emojiPosition = emojiPosition;
    }

    /**
     * @return the enableGraphDrill
     */
    public String getEnableGraphDrill() {
        return enableGraphDrill;
    }

    /**
     * @param enableGraphDrill the enableGraphDrill to set
     */
    public void setEnableGraphDrill(String enableGraphDrill) {
        this.enableGraphDrill = enableGraphDrill;
    }

    /**
     * @return the lableColor
     */
    public String getLableColor() {
        return lableColor;
    }

    /**
     * @param lableColor the lableColor to set
     */
    public void setLableColor(String lableColor) {
        this.lableColor = lableColor;
    }

    /**
     * @return the Pattern
     */
//    public String getPattern() {
//        return Pattern;
//    }
//
//    /**
//     * @param Pattern the Pattern to set
//     */
//    public void setPattern(String Pattern) {
//        this.Pattern = Pattern;
//    }
    public String getLegendNo() {
        return legendNo;
    }

    /**
     * @param legendNo the legendNo to set
     */
    public void setLegendNo(String legendNo) {
        this.legendNo = legendNo;
    }

    /**
     * @return the dialSelectChart
     */
    public String getDialSelectChart() {
        return dialSelectChart;
    }

    /**
     * @param dialSelectChart the dialSelectChart to set
     */
    public void setDialSelectChart(String dialSelectChart) {
        this.dialSelectChart = dialSelectChart;
    }

    /**
     * @return the measureAlias
     */
    public Map<String, String> getMeasureAlias() {
        return measureAlias;
    }

    /**
     * @param measureAlias the measureAlias to set
     */
    public void setMeasureAlias(Map<String, String> measureAlias) {
        this.measureAlias = measureAlias;
    }

    /**
     * @return the lbColor
     */
    public String getLbColor() {
        return lbColor;
    }

    /**
     * @param lbColor the lbColor to set
     */
    public void setLbColor(String lbColor) {
        this.lbColor = lbColor;
    }

    public String getPattern() {
        return Pattern;
    }

    /**
     * @param Pattern the Pattern to set
     */
    public void setPattern(String Pattern) {
        this.Pattern = Pattern;
    }

    /**
     * @return the LabelPos
     */
    public String getLabelPos() {
        return LabelPos;
    }

    /**
     * @param LabelPos the LabelPos to set
     */
    public void setLabelPos(String LabelPos) {
        this.LabelPos = LabelPos;
    }

    /**
     * @return the stackLight
     */
    public String getStackLight() {
        return stackLight;
    }

    /**
     * @param stackLight the stackLight to set
     */
    public void setStackLight(String stackLight) {
        this.stackLight = stackLight;
    }

    /**
     * @return the stackDark
     */
    public String getStackDark() {
        return stackDark;
    }

    /**
     * @param stackDark the stackDark to set
     */
    public void setStackDark(String stackDark) {
        this.stackDark = stackDark;
    }

    /**
     * @return the lbPosition
     */
    public String getLbPosition() {
        return lbPosition;
    }

    /**
     * @param lbPosition the lbPosition to set
     */
    public void setLbPosition(String lbPosition) {
        this.lbPosition = lbPosition;
    }

    public String getMaxRange() {
        return MaxRange;
    }

    /**
     * @param MaxRange the MaxRange to set
     */
    public void setMaxRange(String MaxRange) {
        this.MaxRange = MaxRange;
    }

    /**
     * @return the displayXLine
     */
    public String getDisplayXLine() {
        return displayXLine;
    }

    /**
     * @param displayXLine the displayXLine to set
     */
    public void setDisplayXLine(String displayXLine) {
        this.displayXLine = displayXLine;
    }

    /**
     * @return the displayYLine
     */
    public String getDisplayYLine() {
        return displayYLine;
    }

    /**
     * @param displayYLine the displayYLine to set
     */
    public void setDisplayYLine(String displayYLine) {
        this.displayYLine = displayYLine;
    }

    /**
     * @return the appendNumberFormat
     */
    public String getAppendNumberFormat() {
        return appendNumberFormat;
    }

    /**
     * @param appendNumberFormat the appendNumberFormat to set
     */
    public void setAppendNumberFormat(String appendNumberFormat) {
        this.appendNumberFormat = appendNumberFormat;
    }

    //    /**
//     * @return the bulletParameters
//     */
    public List<Map<String, Map<String, String>>> getBulletParameters() {
        return bulletParameters;
    }

    /**
     * @param bulletParameters the bulletParameters to set
     */
    public void setBulletParameters(List<Map<String, Map<String, String>>> bulletParameters) {
        this.bulletParameters = bulletParameters;
    }

    public String getEditXLable() {
        return editXLable;
    }

    public void setEditXLable(String editXLable) {
        this.editXLable = editXLable;
    }

    public String getdDLable() {
        return dDLable;
    }

    /**
     * @param dDLable the dDLable to set
     */
    public void setdDLable(String dDLable) {
        this.dDLable = dDLable;
    }

    /**
     * @return the startRecords
     */
    public String getStartRecords() {
        return startRecords;
    }

    /**
     * @param startRecords the startRecords to set
     */
    public void setStartRecords(String startRecords) {
        this.startRecords = startRecords;
    }

    /**
     * @return the endRecords
     */
    public String getEndRecords() {
        return endRecords;
    }

    /**
     * @param endRecords the endRecords to set
     */
    public void setEndRecords(String endRecords) {
        this.endRecords = endRecords;
    }

    /**
     * @return the headingFontSize
     */
    public String getHeadingFontSize() {
        return headingFontSize;
    }

    /**
     * @param headingFontSize the headingFontSize to set
     */
    public void setHeadingFontSize(String headingFontSize) {
        this.headingFontSize = headingFontSize;
    }

    /**
     * @return the chartFontType
     */
    public String getChartFontType() {
        return chartFontType;
    }

    /**
     * @param chartFontType the chartFontType to set
     */
    public void setChartFontType(String chartFontType) {
        this.chartFontType = chartFontType;
    }

    /**
     * @return the localDrills
     */
    public Map<String, List<String>> getLocalDrills() {
        return localDrills;
    }

    /**
     * @param localDrills the localDrills to set
     */
    public void setLocalDrills(Map<String, List<String>> localDrills) {
        this.localDrills = localDrills;
    }

    /**
     * @return the anchorChart
     */
    public Map<String, List<String>> getAnchorChart() {
        return anchorChart;
    }

    /**
     * @param anchorChart the anchorChart to set
     */
    public void setAnchorChart(Map<String, List<String>> anchorChart) {
        this.anchorChart = anchorChart;
    }

    public String getDialMeasureSuffix() {
        return dialMeasureSuffix;
    }

    /**
     * @param dialMeasureSuffix the dialMeasureSuffix to set
     */
    public void setDialMeasureSuffix(String dialMeasureSuffix) {
        this.dialMeasureSuffix = dialMeasureSuffix;
    }

    /**
     * @return the PrefixList
     */
    public List<String> getPrefixList() {
        return PrefixList;
    }

    /**
     * @param PrefixList the PrefixList to set
     */
    public void setPrefixList(List<String> PrefixList) {
        this.PrefixList = PrefixList;
    }

    /**
     * @return the PrefixfontsizeList
     */
    public List<String> getPrefixfontsizeList() {
        return PrefixfontsizeList;
    }

    /**
     * @param PrefixfontsizeList the PrefixfontsizeList to set
     */
    public void setPrefixfontsizeList(List<String> PrefixfontsizeList) {
        this.PrefixfontsizeList = PrefixfontsizeList;
    }

    /**
     * @return the SuffixList
     */
    public List<String> getSuffixList() {
        return SuffixList;
    }

    /**
     * @param SuffixList the SuffixList to set
     */
    public void setSuffixList(List<String> SuffixList) {
        this.SuffixList = SuffixList;
    }

    /**
     * @return the Suffixfontsize
     */
    public List<String> getSuffixfontsizeList() {
        return SuffixfontsizeList;
    }

    /**
     * @param Suffixfontsize the Suffixfontsize to set
     */
    public void setSuffixfontsizeList(List<String> SuffixfontsizeList) {
        this.SuffixfontsizeList = SuffixfontsizeList;
    }

    /**
     * @return the redialDefaultColor
     */
    public String getRadialDefaultColor() {
        return radialDefaultColor;
    }

    /**
     * @param redialDefaultColor the redialDefaultColor to set
     */
    public void setRadialDefaultColor(String radialDefaultColor) {
        this.radialDefaultColor = radialDefaultColor;
    }

    /**
     * @return the Prefixfontsize
     */
    public String getPrefixfontsize() {
        return Prefixfontsize;
    }

    /**
     * @param Prefixfontsize the Prefixfontsize to set
     */
    public void setPrefixfontsize(String Prefixfontsize) {
        this.Prefixfontsize = Prefixfontsize;
    }

    /**
     * @return the Suffixfontsize
     */
    public String getSuffixfontsize() {
        return Suffixfontsize;
    }

    /**
     * @param Suffixfontsize the Suffixfontsize to set
     */
    public void setSuffixfontsize(String Suffixfontsize) {
        this.Suffixfontsize = Suffixfontsize;
    }

    /**
     * @return the toolTip
     */
    public String getToolTip() {
        return toolTip;
    }

    /**
     * @param toolTip the toolTip to set
     */
    public void setToolTip(String toolTip) {
        this.toolTip = toolTip;
    }

    /**
     * @return the dialType
     */
    public String getDialType() {
        return dialType;
    }

    /**
     * @param dialType the dialType to set
     */
    public void setDialType(String dialType) {
        this.dialType = dialType;
    }

    /**
     * @return the lFilledFont
     */
    public String getlFilledFont() {
        return lFilledFont;
    }

    /**
     * @param lFilledFont the lFilledFont to set
     */
    public void setlFilledFont(String lFilledFont) {
        this.lFilledFont = lFilledFont;
    }

    /**
     * @return the chartDecription
     */
    public String getChartDecription() {
        return chartDecription;
    }

    /**
     * @param chartDecription the chartDecription to set
     */
    public void setChartDecription(String chartDecription) {
        this.chartDecription = chartDecription;
    }

    /**
     * @return the numberFormat
     */
    public Map<String, String> getNumberFormat() {
        return numberFormat;
    }

    /**
     * @param numberFormat the numberFormat to set
     */
    public void setNumberFormat(Map<String, String> numberFormat) {
        this.numberFormat = numberFormat;
    }

    /**
     * @return the numberRounding
     */
    public Map<String, String> getNumberRounding() {
        return numberRounding;
    }

    /**
     * @param numberRounding the numberRounding to set
     */
    public void setNumberRounding(Map<String, String> numberRounding) {
        this.numberRounding = numberRounding;
    }

    /**
     * @return the XaxisTicks
     */
    public String getXaxisTicks() {
        return XaxisTicks;
    }

    /**
     * @param XaxisTicks the XaxisTicks to set
     */
    public void setXaxisTicks(String XaxisTicks) {
        this.XaxisTicks = XaxisTicks;
    }

    public String getBulletAbovecolor() {
        return bulletAbovecolor;
    }

    /**
     * @param bulletAbovecolor the bulletAbovecolor to set
     */
    public void setBulletAbovecolor(String bulletAbovecolor) {
        this.bulletAbovecolor = bulletAbovecolor;
    }

    /**
     * @return the bulletHighcolor
     */
    public String getBulletHighcolor() {
        return bulletHighcolor;
    }

    /**
     * @param bulletHighcolor the bulletHighcolor to set
     */
    public void setBulletHighcolor(String bulletHighcolor) {
        this.bulletHighcolor = bulletHighcolor;
    }

    /**
     * @return the bulletMediumcolor
     */
    public String getBulletMediumcolor() {
        return bulletMediumcolor;
    }

    /**
     * @param bulletMediumcolor the bulletMediumcolor to set
     */
    public void setBulletMediumcolor(String bulletMediumcolor) {
        this.bulletMediumcolor = bulletMediumcolor;
    }

    /**
     * @return the bulletLowcolor
     */
    public String getBulletLowcolor() {
        return bulletLowcolor;
    }

    /**
     * @param bulletLowcolor the bulletLowcolor to set
     */
    public void setBulletLowcolor(String bulletLowcolor) {
        this.bulletLowcolor = bulletLowcolor;
    }

    /**
     * @return the enableQuickFilter2
     */
    public String getEnableQuickFilter2() {
        return enableQuickFilter2;
    }

    /**
     * @param enableQuickFilter2 the enableQuickFilter2 to set
     */
    public void setEnableQuickFilter2(String enableQuickFilter2) {
        this.enableQuickFilter2 = enableQuickFilter2;
    }

    /**
     * @return the dataDisplayArr
     */
    public Map<String, String> getDataDisplayArr() {
        return dataDisplayArr;
    }

    /**
     * @param dataDisplayArr the dataDisplayArr to set
     */
    public void setDataDisplayArr(Map<String, String> dataDisplayArr) {
        this.dataDisplayArr = dataDisplayArr;
    }

    /**
     * @return the measureLabelLength
     */
    public Map<String, String> getMeasureLabelLength() {
        return measureLabelLength;
    }

    /**
     * @param measureLabelLength the measureLabelLength to set
     */
    public void setMeasureLabelLength(Map<String, String> measureLabelLength) {
        this.measureLabelLength = measureLabelLength;
    }

    /**
     * @return the bulletBelowcolor
     */
    public String getBulletBelowcolor() {
        return bulletBelowcolor;
    }

    /**
     * @param bulletBelowcolor the bulletBelowcolor to set
     */
    public void setBulletBelowcolor(String bulletBelowcolor) {
        this.bulletBelowcolor = bulletBelowcolor;
    }

    /**
     * @return the legendBoxBorder
     */
    public String getLegendBoxBorder() {
        return legendBoxBorder;
    }

    /**
     * @param legendBoxBorder the legendBoxBorder to set
     */
    public void setLegendBoxBorder(String legendBoxBorder) {
        this.legendBoxBorder = legendBoxBorder;
    }

    /**
     * @return the labelColor
     */
    public String getLabelColor() {
        return labelColor;
    }

    /**
     * @param labelColor the labelColor to set
     */
    public void setLabelColor(String labelColor) {
        this.labelColor = labelColor;
    }

    /**
     * @return the measureValueSize
     */
    public Map<String, String> getMeasureValueSize() {
        return measureValueSize;
    }

    /**
     * @param measureValueSize the measureValueSize to set
     */
    public void setMeasureValueSize(Map<String, String> measureValueSize) {
        this.measureValueSize = measureValueSize;
    }

    /**
     * @return the measureNameSize
     */
    public Map<String, String> getMeasureNameSize() {
        return measureNameSize;
    }

    /**
     * @param measureNameSize the measureNameSize to set
     */
    public void setMeasureNameSize(Map<String, String> measureNameSize) {
        this.measureNameSize = measureNameSize;
    }

    /**
     * @return the DualAxisProp
     */
    public String getDualAxisProp() {
        return DualAxisProp;
    }

    /**
     * @param DualAxisProp the DualAxisProp to set
     */
    public void setDualAxisProp(String DualAxisProp) {
        this.DualAxisProp = DualAxisProp;
    }

    /**
     * @return the roundingType
     */
    public String getRoundingType() {
        return roundingType;
    }

    /**
     * @param roundingType the roundingType to set
     */
    public void setRoundingType(String roundingType) {
        this.roundingType = roundingType;
    }

    /**
     * @return the createBarLine
     */
    public Map<String, String> getCreateBarLine() {
        return createBarLine;
    }

    /**
     * @param createBarLine the createBarLine to set
     */
    public void setCreateBarLine(Map<String, String> createBarLine) {
        this.createBarLine = createBarLine;
    }

    /**
     * @return the labelColors
     */
    public String getLabelColors() {
        return labelColors;
    }

    /**
     * @param labelColors the labelColors to set
     */
    public void setLabelColors(String labelColors) {
        this.labelColors = labelColors;
    }

    /**
     * @return the Afontsize
     */
    public String getAfontsize() {
        return Afontsize;
    }

    /**
     * @param Afontsize the Afontsize to set
     */
    public void setAfontsize(String Afontsize) {
        this.Afontsize = Afontsize;
    }

    /**
     * @return the trlineType
     */
    public String getTrlineType() {
        return trlineType;
    }

    /**
     * @param trlineType the trlineType to set
     */
    public void setTrlineType(String trlineType) {
        this.trlineType = trlineType;
    }

    /**
     * @return the targetAvg
     */
    public Map<String, String> getMeasureAvg() {
        return measureAvg;
    }

    /**
     * @param measureAvg the targetAvg to set
     */
    public void setMeasureAvg(Map<String, String> measureAvg) {
        this.measureAvg = measureAvg;
    }

    /**
     * @return the multiFilter
     */
    public String getMultiFilter() {
        return multiFilter;
    }

    /**
     * @param multiFilter the multiFilter to set
     */
    public void setMultiFilter(String multiFilter) {
        this.multiFilter = multiFilter;
    }

    //added by shobhti for multidashboard on 22/09/015
    /**
     * @return the isDashboardDefined
     */
    public String getIsDashboardDefined() {
        return isDashboardDefined;
    }

    /**
     * @param isDashboardDefined the isDashboardDefined to set
     */
    public void setIsDashboardDefined(String isDashboardDefined) {
        this.isDashboardDefined = isDashboardDefined;
    }
    //end

    /**
     * @return the chartTypeBarTrend
     */
    public String getChartTypeBarTrend() {
        return chartTypeBarTrend;
    }

    /**
     * @param chartTypeBarTrend the chartTypeBarTrend to set
     */
    public void setChartTypeBarTrend(String chartTypeBarTrend) {
        this.chartTypeBarTrend = chartTypeBarTrend;
    }

    /**
     * @return the dataLabelType
     */
    public Map<String, String> getDataLabelType() {
        return dataLabelType;
    }

    /**
     * @param dataLabelType the dataLabelType to set
     */
    public void setDataLabelType(Map<String, String> dataLabelType) {
        this.dataLabelType = dataLabelType;
    }

    /**
     * @return the defineTargetline
     */
    public Map<String, String> getDefineTargetline() {
        return defineTargetline;
    }

    /**
     * @param defineTargetline the defineTargetline to set
     */
    public void setDefineTargetline(Map<String, String> defineTargetline) {
        this.defineTargetline = defineTargetline;
    }

    /**
     * @return the gtGraph
     */
    public String getGtGraph() {
        return gtGraph;
    }

    /**
     * @param gtGraph the gtGraph to set
     */
    public void setGtGraph(String gtGraph) {
        this.gtGraph = gtGraph;
    }

    public String getMultiselecctQuickFilter() {
        return multiselecctQuickFilter;
    }

    /**
     * @param multiselecctQuickFilter the multiselecctQuickFilter to set
     */
    public void setMultiselecctQuickFilter(String multiselecctQuickFilter) {
        this.multiselecctQuickFilter = multiselecctQuickFilter;
    }

    /**
     * @param QuickFilterValue the QuickFilterValue to set
     */
    public void setQuickFilterValue(Map<String, List<String>> QuickFilterValue) {
        this.QuickFilterValue = QuickFilterValue;
    }
    

    /**
     * @return the quickFilterLength
     */
    public String getQuickFilterLength() {
        return quickFilterLength;
    }

    /**
     * @param quickFilterLength the quickFilterLength to set
     */
    public void setQuickFilterLength(String quickFilterLength) {
        this.quickFilterLength = quickFilterLength;
    }

    /**
     * @return the transposeMeasure
     */
    public String getTransposeMeasure() {
        return transposeMeasure;
    }

    /**
     * @param transposeMeasure the transposeMeasure to set
     */
    public void setTransposeMeasure(String transposeMeasure) {
        this.transposeMeasure = transposeMeasure;
    }

    /**
     * @return the comparableFilters
     */
    public Map<String, List<String>> getComparableFilters() {
        return comparableFilters;
    }

    /**
     * @param comparableFilters the comparableFilters to set
     */
    public void setComparableFilters(Map<String, List<String>> comparableFilters) {
        this.comparableFilters = comparableFilters;
    }

    /**
     * @return the comparedValue
     */
    public Map<String, List<String>> getComparedValue() {
        return comparedValue;
    }

    /**
     * @param comparedValue the comparedValue to set
     */
    public void setComparedValue(Map<String, List<String>> comparedValue) {
        this.comparedValue = comparedValue;
    }

    /**
     * @return the defineTargetline
     */
    /**
     * @return the quickViewname
     */
    public String getQuickViewname() {
        return quickViewname;
    }

    /**
     * @param quickViewname the quickViewname to set
     */
    public void setQuickViewname(String quickViewname) {
        this.quickViewname = quickViewname;
    }

    /**
     * @return the selectedComparison
     */
    public List<Map<String, String>> getSelectedComparison() {
        return selectedComparison;
    }

    /**
     * @param selectedComparison the selectedComparison to set
     */
    public void setSelectedComparison(List<Map<String, String>> selectedComparison) {
        this.selectedComparison = selectedComparison;
    }

    /**
     * @return the timeComparison
     */
    public List<Map<String, String>> getTimeComparison() {
        return timeComparison;
    }

    /**
     * @param timeComparison the timeComparison to set
     */
    public void setTimeComparison(List<Map<String, String>> timeComparison) {
        this.timeComparison = timeComparison;
    }

    /**
     * @return the quickViewname
     */
    /**
     * @return the legendPrintType
     */
    public String getLegendPrintType() {
        return legendPrintType;
    }

    /**
     * @param legendPrintType the legendPrintType to set
     */
    public void setLegendPrintType(String legendPrintType) {
        this.legendPrintType = legendPrintType;
    }

    /**
     * @return the valueTilealign
     */
    public String getValueTilealign() {
        return valueTilealign;
    }

    /**
     * @param valueTilealign the valueTilealign to set
     */
    public void setValueTilealign(String valueTilealign) {
        this.valueTilealign = valueTilealign;
    }

    /**
     * @return the dLabelDisplay
     */
    public Map<String, String> getdLabelDisplay() {
        return dLabelDisplay;
    }

    /**
     * @param dLabelDisplay the dLabelDisplay to set
     */
    public void setdLabelDisplay(Map<String, String> dLabelDisplay) {
        this.dLabelDisplay = dLabelDisplay;
    }

    /**
     * @return the dataLabDisplay
     */
    public String getDataLabDisplay() {
        return dataLabDisplay;
    }

    /**
     * @param dataLabDisplay the dataLabDisplay to set
     */
    public void setDataLabDisplay(String dataLabDisplay) {
        this.dataLabDisplay = dataLabDisplay;
    }

    /**
     * @return the completeChartData
     */
    public String getCompleteChartData() {
        return completeChartData;
    }

    /**
     * @param completeChartData the completeChartData to set
     */
    public void setCompleteChartData(String completeChartData) {
        this.completeChartData = completeChartData;
    }

    /**
     * @return the iconShowHide
     */
    public String getIconShowHide() {
        return iconShowHide;
    }

    /**
     * @param iconShowHide the iconShowHide to set
     */
    public void setIconShowHide(String iconShowHide) {
        this.iconShowHide = iconShowHide;
    }

    /**
     * @return the QuickFilterId
     */
    public String getQuickFilterId() {
        return QuickFilterId;
    }

    /**
     * @param QuickFilterId the QuickFilterId to set
     */
    public void setQuickFilterId(String QuickFilterId) {
        this.QuickFilterId = QuickFilterId;
    }

    /**
     * @return the tableBorder
     */
    public String getTableBorder() {
        return tableBorder;
    }

    /**
     * @param tableBorder the tableBorder to set
     */
    public void setTableBorder(String tableBorder) {
        this.tableBorder = tableBorder;
    }

    /**
     * @return the legendFontSize
     */
    public String getLegendFontSize() {
        return legendFontSize;
    }

    /**
     * @param legendFontSize the legendFontSize to set
     */
    public void setLegendFontSize(String legendFontSize) {
        this.legendFontSize = legendFontSize;
    }

    /**
     * @return the showViewByinLBox
     */
    public String getShowViewByinLBox() {
        return showViewByinLBox;
    }

    /**
     * @param showViewByinLBox the showViewByinLBox to set
     */
    public void setShowViewByinLBox(String showViewByinLBox) {
        this.showViewByinLBox = showViewByinLBox;
    }

    /**
     * @return the SuffixFormat
     */
    public String getSuffixFormat() {
        return SuffixFormat;
    }

    /**
     * @param SuffixFormat the SuffixFormat to set
     */
    public void setSuffixFormat(String SuffixFormat) {
        this.SuffixFormat = SuffixFormat;
    }

    /**
     * @return the changeEnable
     */
    public String getChangeEnable() {
        return changeEnable;
    }

    /**
     * @param changeEnable the changeEnable to set
     */
    public void setChangeEnable(String changeEnable) {
        this.changeEnable = changeEnable;
    }

    /**
     * @return the changeType
     */
    public String getChangeType() {
        return changeType;
    }

    /**
     * @param changeType the changeType to set
     */
    public void setChangeType(String changeType) {
        this.changeType = changeType;
    }

    /**
     * @return the showViewBy
     */
    public String getShowViewBy() {
        return showViewBy;
    }

    /**
     * @param showViewBy the showViewBy to set
     */
    public void setShowViewBy(String showViewBy) {
        this.showViewBy = showViewBy;
    }

    /**
     * @return the lineSymbol
     */
    public String getLineSymbol() {
        return lineSymbol;
    }

    /**
     * @param lineSymbol the lineSymbol to set
     */
    public void setLineSymbol(String lineSymbol) {
        this.lineSymbol = lineSymbol;
    }

    /**
     * @return the lineSymbol1
     */
    public String getLineSymbol1() {
        return lineSymbol1;
    }

    /**
     * @param lineSymbol1 the lineSymbol1 to set
     */
    public void setLineSymbol1(String lineSymbol1) {
        this.lineSymbol1 = lineSymbol1;
    }

    /**
     * @return the measValueAlign
     */
    public Map<String, String> getMeasValueAlign() {
        return measValueAlign;
    }

    /**
     * @param measValueAlign the measValueAlign to set
     */
    public void setMeasValueAlign(Map<String, String> measValueAlign) {
        this.measValueAlign = measValueAlign;
    }

    /**
     * @return the measNameAlign
     */
    public Map<String, String> getMeasNameAlign() {
        return measNameAlign;
    }

    /**
     * @param measNameAlign the measNameAlign to set
     */
    public void setMeasNameAlign(Map<String, String> measNameAlign) {
        this.measNameAlign = measNameAlign;
    }

    /**
     * @return the AxisNameColor
     */
    public String getAxisNameColor() {
        return AxisNameColor;
    }

    /**
     * @param AxisNameColor the AxisNameColor to set
     */
    public void setAxisNameColor(String AxisNameColor) {
        this.AxisNameColor = AxisNameColor;
    }

    /**
     * @return the sortMeasure
     */
    public Map<String, String> getSortMeasure() {
        return sortMeasure;
    }

    /**
     * @param sortMeasure the sortMeasure to set
     */
    public void setSortMeasure(Map<String, String> sortMeasure) {
        this.sortMeasure = sortMeasure;
    }

    /**
     * @return the hideDate
     */
    public String getHideDate() {
        return hideDate;
    }

    /**
     * @param hideDate the hideDate to set
     */
    public void setHideDate(String hideDate) {
        this.hideDate = hideDate;
    }

    /**
     * @return the hideBorder
     */
    public String getHideBorder() {
        return hideBorder;
    }

    /**
     * @param hideBorder the hideBorder to set
     */
    public void setHideBorder(String hideBorder) {
        this.hideBorder = hideBorder;
    }

    /**
     * @return the labelFSize
     */
    public String getLabelFSize() {
        return labelFSize;
    }

    /**
     * @param labelFSize the labelFSize to set
     */
    public void setLabelFSize(String labelFSize) {
        this.labelFSize = labelFSize;
    }

    /**
     * @return the chartTypeComparison
     */
    public String getChartTypeComparison() {
        return chartTypeComparison;
    }

    /**
     * @param chartTypeComparison the chartTypeComparison to set
     */
    public void setChartTypeComparison(String chartTypeComparison) {
        this.chartTypeComparison = chartTypeComparison;
    }

    /**
     * @return the isComparison
     */
    public String getIsComparison() {
        return isComparison;
    }

    /**
     * @param isComparison the isComparison to set
     */
    public void setIsComparison(String isComparison) {
        this.isComparison = isComparison;
    }

    /**
     * @return the kpiSubData
     */
    public String getKpiSubData() {
        return kpiSubData;
    }

    /**
     * @param kpiSubData the kpiSubData to set
     */
    public void setKpiSubData(String kpiSubData) {
        this.kpiSubData = kpiSubData;
    }

    /**
     * @return the runtimeMeasure
     */
    public List<String> getRuntimeMeasure() {
        return runtimeMeasure;
    }

    /**
     * @param runtimeMeasure the runtimeMeasure to set
     */
    public void setRuntimeMeasure(List<String> runtimeMeasure) {
        this.runtimeMeasure = runtimeMeasure;
    }

    /**
     * @return the fontSize1
     */
    public String getFontSize1() {
        return fontSize1;
    }

    /**
     * @param fontSize1 the fontSize1 to set
     */
    public void setFontSize1(String fontSize1) {
        this.fontSize1 = fontSize1;
    }

    /**
     * @return the tableWithSymbol
     */
    public String getTableWithSymbol() {
        return tableWithSymbol;
    }

    /**
     * @param tableWithSymbol the tableWithSymbol to set
     */
    public void setTableWithSymbol(String tableWithSymbol) {
        this.tableWithSymbol = tableWithSymbol;
    }

    /**
     * @return the enableVieweby
     */
    public String getEnableVieweby() {
        return enableVieweby;
    }

    /**
     * @param enableVieweby the enableVieweby to set
     */
    public void setEnableVieweby(String enableVieweby) {
        this.enableVieweby = enableVieweby;
    }

    /**
     * @return the enableMeasures
     */
    public String getEnableMeasures() {
        return enableMeasures;
    }

    /**
     * @param enableMeasures the enableMeasures to set
     */
    public void setEnableMeasures(String enableMeasures) {
        this.enableMeasures = enableMeasures;
    }

    /**
     * @return the ticksValueSize
     */
    public Map<String, String> getTicksValueSize() {
        return ticksValueSize;
    }

    /**
     * @param ticksValueSize the ticksValueSize to set
     */
    public void setTicksValueSize(Map<String, String> ticksValueSize) {
        this.ticksValueSize = ticksValueSize;
    }

    /**
     * @return the dataColor
     */
    public Map<String, String> getDataColor() {
        return dataColor;
    }

    /**
     * @param dataColor the dataColor to set
     */
    public void setDataColor(Map<String, String> dataColor) {
        this.dataColor = dataColor;
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
     * @return the prevPeriody
     */
    public String getPrevPeriodDisplay() {
        return prevPeriodDisplay;
    }

    /**
     * @param prevPeriodDisplay the prevPeriodDisplay to set
     */
    public void setPrevPeriodDisplay(String prevPeriodDisplay) {
        this.prevPeriodDisplay = prevPeriodDisplay;
    }

    /**
     * @return the MapTransformation
     */
    public Map<String, List<String>> getMapTransformation() {
        return MapTransformation;
    }

    /**
     * @param MapTransformation the MapTransformation to set
     */
    public void setMapTransformation(Map<String, List<String>> MapTransformation) {
        this.MapTransformation = MapTransformation;
    }

    /**
     * @return the circularChartTab
     */
    public String getCircularChartTab() {
        return circularChartTab;
    }

    /**
     * @param circularChartTab the circularChartTab to set
     */
    public void setCircularChartTab(String circularChartTab) {
        this.circularChartTab = circularChartTab;
    }

    /**
     * @return the enableComparison
     */
    public String getEnableComparison() {
        return enableComparison;
    }

    /**
     * @param enableComparison the enableComparison to set
     */
    public void setEnableComparison(String enableComparison) {
        this.enableComparison = enableComparison;
    }

    public String getBarsize() {
        return barsize;
    }

    public void setBarsize(String barsize) {
        this.barsize = barsize;
    }

    /**
     * @return the QuickFilterShowHide
     */
    public String getQuickFilterShowHide() {
        return QuickFilterShowHide;
    }

    /**
     * @param QuickFilterShowHide the QuickFilterShowHide to set
     */
    public void setQuickFilterShowHide(String QuickFilterShowHide) {
        this.QuickFilterShowHide = QuickFilterShowHide;
    }

    /**
     * @return the excludeFromDrill
     */
    public String getExcludeFromDrill() {
        return excludeFromDrill;
    }

    /**
     * @param excludeFromDrill the excludeFromDrill to set
     */
    public void setExcludeFromDrill(String excludeFromDrill) {
        this.excludeFromDrill = excludeFromDrill;
    }

    /**
     * @return the LabFtColor
     */
    public Map<String, String> getLabFtColor() {
        return LabFtColor;
    }

    /**
     * @param LabFtColor the LabFtColor to set
     */
    public void setLabFtColor(Map<String, String> LabFtColor) {
        this.LabFtColor = LabFtColor;
    }

    /**
     * @return the comparisonType
     */
    public String getComparisonType() {
        return comparisonType;
    }

    /**
     * @param comparisonType the comparisonType to set
     */
    public void setComparisonType(String comparisonType) {
        this.comparisonType = comparisonType;
    }

    /**
     * @return the comparedMeasure
     */
    public List<String> getComparedMeasure() {
        return comparedMeasure;
    }

    /**
     * @param comparedMeasure the comparedMeasure to set
     */
    public void setComparedMeasure(List<String> comparedMeasure) {
        this.comparedMeasure = comparedMeasure;
    }

    /**
     * @return the comparedMeasureId
     */
    public List<String> getComparedMeasureId() {
        return comparedMeasureId;
    }

    /**
     * @param comparedMeasureId the comparedMeasureId to set
     */
    public void setComparedMeasureId(List<String> comparedMeasureId) {
        this.comparedMeasureId = comparedMeasureId;
    }

    /**
     * @return the comparedMeasureAgg
     */
    public List<String> getComparedMeasureAgg() {
        return comparedMeasureAgg;
    }

    /**
     * @param comparedMeasureAgg the comparedMeasureAgg to set
     */
    public void setComparedMeasureAgg(List<String> comparedMeasureAgg) {
        this.comparedMeasureAgg = comparedMeasureAgg;
    }

    /**
     * @return the currentMeasures
     */
    public List<String> getCurrentMeasures() {
        return currentMeasures;
    }

    /**
     * @param currentMeasures the currentMeasures to set
     */
    public void setCurrentMeasures(List<String> currentMeasures) {
        this.currentMeasures = currentMeasures;
    }

    /**
     * @return the titleFontSize
     */
    public String getTitleFontSize() {
        return titleFontSize;
    }

    /**
     * @param titleFontSize the titleFontSize to set
     */
    public void setTitleFontSize(String titleFontSize) {
        this.titleFontSize = titleFontSize;
    }

    /**
     * @return the titleColor
     */
    public String getTitleColor() {
        return titleColor;
    }

    /**
     * @param titleColor the titleColor to set
     */
    public void setTitleColor(String titleColor) {
        this.titleColor = titleColor;
    }

    /**
     * @return the FilledColor1
     */
    public String getFilledColor1() {
        return FilledColor1;
    }

    /**
     * @param FilledColor1 the FilledColor1 to set
     */
    public void setFilledColor1(String FilledColor1) {
        this.FilledColor1 = FilledColor1;
    }

    /**
     * @return the FilledColor2
     */
    public String getFilledColor2() {
        return FilledColor2;
    }

    /**
     * @param FilledColor2 the FilledColor2 to set
     */
    public void setFilledColor2(String FilledColor2) {
        this.FilledColor2 = FilledColor2;
    }

    /**
     * @return the FilledColor3
     */
    public String getFilledColor3() {
        return FilledColor3;
    }

    /**
     * @param FilledColor3 the FilledColor3 to set
     */
    public void setFilledColor3(String FilledColor3) {
        this.FilledColor3 = FilledColor3;
    }

    /**
     * @return the FilledColor4
     */
    public String getFilledColor4() {
        return FilledColor4;
    }

    /**
     * @param FilledColor4 the FilledColor4 to set
     */
    public void setFilledColor4(String FilledColor4) {
        this.FilledColor4 = FilledColor4;
    }

    /**
     * @return the ParentMeasures
     */
    public List<String> getParentMeasures() {
        return ParentMeasures;
    }

    /**
     * @param ParentMeasures the ParentMeasures to set
     */
    public void setParentMeasures(List<String> ParentMeasures) {
        this.ParentMeasures = ParentMeasures;
    }

    /**
     * @return the comparativeValueFont
     */
    public Map<String, String> getComparativeValueFont() {
        return comparativeValueFont;
    }

    /**
     * @param comparativeValueFont the comparativeValueFont to set
     */
    public void setComparativeValueFont(Map<String, String> comparativeValueFont) {
        this.comparativeValueFont = comparativeValueFont;
    }

    /**
     * @return the comparativeValue
     */
    public String getComparativeValue() {
        return comparativeValue;
    }

    /**
     * @param comparativeValue the comparativeValue to set
     */
    public void setComparativeValue(String comparativeValue) {
        this.comparativeValue = comparativeValue;
    }

    /**
     * @return the mapCountryName
     */
    public String getMapCountryName() {
        return mapCountryName;
    }

    /**
     * @param mapCountryName the mapCountryName to set
     */
    public void setMapCountryName(String mapCountryName) {
        this.mapCountryName = mapCountryName;
    }

    /**
     * @return the trendViewMeasures
     */
    public String getTrendViewMeasures() {
        return trendViewMeasures;
    }

    /**
     * @param trendViewMeasures the trendViewMeasures to set
     */
    public void setTrendViewMeasures(String trendViewMeasures) {
        this.trendViewMeasures = trendViewMeasures;
    }

    /**
     * @return the dataTranspose
     */
    public String getDataTranspose() {
        return dataTranspose;
    }

    /**
     * @param dataTranspose the dataTranspose to set
     */
    public void setDataTranspose(String dataTranspose) {
        this.dataTranspose = dataTranspose;
    }

    /**
     * @return the valueOf1
     */
    public String getValueOf1() {
        return valueOf1;
    }

    /**
     * @param valueOf1 the valueOf1 to set
     */
    public void setValueOf1(String valueOf1) {
        this.valueOf1 = valueOf1;
    }

    /**
     * @return the absoluteValue
     */
    public String getAbsoluteValue() {
        return absoluteValue;
    }

    /**
     * @param absoluteValue the absoluteValue to set
     */
    public void setAbsoluteValue(String absoluteValue) {
        this.absoluteValue = absoluteValue;
    }

    /**
     * @return the measureAliasCombo
     */
    public Map<String, String> getMeasureAliasCombo() {
        return measureAliasCombo;
    }

    /**
     * @param measureAliasCombo the measureAliasCombo to set
     */
    public void setMeasureAliasCombo(Map<String, String> measureAliasCombo) {
        this.measureAliasCombo = measureAliasCombo;
    }

    public Map<String, String> getCurrencySymbol() {
        return currencySymbol;
    }

    /**
     * @param currencySymbol the currencySymbol to set
     */
    public void setCurrencySymbol(Map<String, String> currencySymbol) {
        this.currencySymbol = currencySymbol;
    }

    /**
     * @return the startCurrentRecords
     */
    public String getStartCurrentRecords() {
        return startCurrentRecords;
    }

    /**
     * @param startCurrentRecords the startCurrentRecords to set
     */
    public void setStartCurrentRecords(String startCurrentRecords) {
        this.startCurrentRecords = startCurrentRecords;
    }

    /**
     * @return the endCurrentRecords
     */
    public String getEndCurrentRecords() {
        return endCurrentRecords;
    }

    /**
     * @param endCurrentRecords the endCurrentRecords to set
     */
    public void setEndCurrentRecords(String endCurrentRecords) {
        this.endCurrentRecords = endCurrentRecords;
    }

    /**
     * @return the timeBasedData
     */
    public List<String> getTimeBasedData() {
        return timeBasedData;
    }

    /**
     * @param timeBasedData the timeBasedData to set
     */
    public void setTimeBasedData(List<String> timeBasedData) {
        this.timeBasedData = timeBasedData;
    }

    /**
     * @return the timeEnable
     */
    public String getTimeEnable() {
        return timeEnable;
    }

    /**
     * @param timeEnable the timeEnable to set
     */
    public void setTimeEnable(String timeEnable) {
        this.timeEnable = timeEnable;
    }

    /**
     * @return the comboData
     */
    public boolean isComboData() {
        return comboData;
    }

    /**
     * @param comboData the comboData to set
     */
    public void setComboData(boolean comboData) {
        this.comboData = comboData;
    }

    /**
     * @return the comboType
     */
    public String getComboType() {
        return comboType;
    }

    /**
     * @param comboType the comboType to set
     */
    public void setComboType(String comboType) {
        this.comboType = comboType;
    }

    /**
     * @return the TableFontColor
     */
    public String getTableFontColor() {
        return TableFontColor;
    }

    /**
     * @param TableFontColor the TableFontColor to set
     */
    public void setTableFontColor(String TableFontColor) {
        this.TableFontColor = TableFontColor;
    }

    /**
     * @return the viewByDisplayType
     */
    public String getViewByDisplayType() {
        return viewByDisplayType;
    }

    /**
     * @param viewByDisplayType the viewByDisplayType to set
     */
    public void setViewByDisplayType(String viewByDisplayType) {
        this.viewByDisplayType = viewByDisplayType;
    }

    /**
     * @return the doublePie
     */
    public String getDoublePie() {
        return doublePie;
    }

    /**
     * @param doublePie the doublePie to set
     */
    public void setDoublePie(String doublePie) {
        this.doublePie = doublePie;
    }

    /**
     * @return the divBackGround
     */
    public String getDivBackGround() {
        return divBackGround;
    }

    /**
     * @param divBackGround the divBackGround to set
     */
    public void setDivBackGround(String divBackGround) {
        this.divBackGround = divBackGround;
    }

    /**
     * @return the y1axisrange
     */
    public Map<String, String> getY1axisrange() {
        return y1axisrange;
    }

    /**
     * @param y1axisrange the y1axisrange to set
     */
    public void setY1axisrange(Map<String, String> y1axisrange) {
        this.y1axisrange = y1axisrange;
    }

    /**
     * @return the GridLines1
     */
    public String getGridLines1() {
        return GridLines1;
    }

    /**
     * @param GridLines1 the GridLines1 to set
     */
    public void setGridLines1(String GridLines1) {
        this.GridLines1 = GridLines1;
    }

    /**
     * @return the changPercentArrow
     */
    public String getChangPercentArrow() {
        return changPercentArrow;
    }

    /**
     * @param changPercentArrow the changPercentArrow to set
     */
    public void setChangPercentArrow(String changPercentArrow) {
        this.changPercentArrow = changPercentArrow;
    }

    /**
     * @return the fillBackground
     */
    public String getFillBackground() {
        return fillBackground;
    }

    /**
     * @param fillBackground the fillBackground to set
     */
    public void setFillBackground(String fillBackground) {
        this.fillBackground = fillBackground;
    }

    /**
     * @return the trendChartData
     */
    public List<Map<String, String>> getTrendChartData() {
        return trendChartData;
    }

    /**
     * @param trendChartData the trendChartData to set
     */
    public void setTrendChartData(List<Map<String, String>> trendChartData) {
        this.trendChartData = trendChartData;
    }

    /**
     * @return the selectedMeasures
     */
    public List<String> getSelectedMeasures() {
        return selectedMeasures;
    }

    /**
     * @param selectedMeasures the selectedMeasures to set
     */
    public void setSelectedMeasures(List<String> selectedMeasures) {
        this.selectedMeasures = selectedMeasures;
    }

    /**
     * @return the kpiMultiSelect
     */
    public String getKpiMultiSelect() {
        return kpiMultiSelect;
    }

    /**
     * @param kpiMultiSelect the kpiMultiSelect to set
     */
    public void setKpiMultiSelect(String kpiMultiSelect) {
        this.kpiMultiSelect = kpiMultiSelect;
    }

    /**
     * @return the xAxisFont
     */
    public String getxAxisFont() {
        return xAxisFont;
    }

    /**
     * @param xAxisFont the xAxisFont to set
     */
    public void setxAxisFont(String xAxisFont) {
        this.xAxisFont = xAxisFont;
    }

    /**
     * @return the yAxisFont
     */
    public String getyAxisFont() {
        return yAxisFont;
    }

    /**
     * @param yAxisFont the yAxisFont to set
     */
    public void setyAxisFont(String yAxisFont) {
        this.yAxisFont = yAxisFont;
    }

    /**
     * @return the defaultMeasures
     */
    public List<String> getDefaultMeasures() {
        return defaultMeasures;
    }

    /**
     * @param defaultMeasures the defaultMeasures to set
     */
    public void setDefaultMeasures(List<String> defaultMeasures) {
        this.defaultMeasures = defaultMeasures;
    }

    /**
     * @return the defaultMeasureIds
     */
    public List<String> getDefaultMeasureIds() {
        return defaultMeasureIds;
    }

    /**
     * @param defaultMeasureIds the defaultMeasureIds to set
     */
    public void setDefaultMeasureIds(List<String> defaultMeasureIds) {
        this.defaultMeasureIds = defaultMeasureIds;
    }

    /**
     * @return the isKPI
     */
    public boolean isIsKPI() {
        return isKPI;
    }

    /**
     * @param isKPI the isKPI to set
     */
    public void setIsKPI(boolean isKPI) {
        this.isKPI = isKPI;
    }

    /**
     * @return the customTimeComparisons
     */
    public List<String> getCustomTimeComparisons() {
        return customTimeComparisons;
    }

    /**
     * @param customTimeComparisons the customTimeComparisons to set
     */
    public void setCustomTimeComparisons(List<String> customTimeComparisons) {
        this.customTimeComparisons = customTimeComparisons;
    }

    /**
     * @return the groupedBarWith
     */
    public Map<String, String> getGroupedBarWith() {
        return groupedBarWith;
    }

    /**
     * @param groupedBarWith the groupedBarWith to set
     */
    public void setGroupedBarWith(Map<String, String> groupedBarWith) {
        this.groupedBarWith = groupedBarWith;
    }

   /**
     * @return the gradientLogicalMap
     */
    public Map<String, String> getGradientLogicalMap() {
        return gradientLogicalMap;
    }
   
    /**
     * @param gradientLogicalMap the gradientLogicalMap to set
     */
    public void setGradientLogicalMap(Map<String, String> gradientLogicalMap) {
        this.gradientLogicalMap = gradientLogicalMap;
    }

  

    /**
     * @return the usBar
     */
    public String getUsBar() {
        return usBar;
    }

    /**
     * @param usBar the usBar to set
     */
    public void setUsBar(String usBar) {
        this.usBar = usBar;
    }
  
    /**
     * @return the tooltipType
     */
    public Map<String, String> getTooltipType() {
        return tooltipType;
}

    /**
     * @param tooltipType the tooltipType to set
     */
    public void setTooltipType(Map<String, String> tooltipType) {
        this.tooltipType = tooltipType;
    }
  
    
      /**
     * @return the colViewBys
     */
    public List<String> getColViewBys() {
        return colViewBys;
    }

    /**
     * @param colViewBys the colViewBys to set
     */
    public void setColViewBys(List<String> colViewBys) {
        this.colViewBys = colViewBys;
    }

    /**
     * @return the colViewIds
     */
    public List<String> getColViewIds() {
        return colViewIds;
    }

    /**
     * @param colViewIds the colViewIds to set
     */
    public void setColViewIds(List<String> colViewIds) {
        this.colViewIds = colViewIds;
    }

    /**
     * @return the nonViewByMapNew
     */
    public Map<String, List<String>> getcrossTableHeaderMap() {
        return crossTableHeaderMap;
    }

    /**
     * @param nonViewByMapNew the nonViewByMapNew to set
     */
    public void setcrossTableHeaderMap(Map<String, List<String>> nonViewByMapNew) {
        this.crossTableHeaderMap = nonViewByMapNew;
    }

    /**
     * @return the crossTabFinalOrder
     */
    public List<String> getCrossTabFinalOrder() {
        return crossTabFinalOrder;
    }

    /**
     * @param crossTabFinalOrder the crossTabFinalOrder to set
     */
    public void setCrossTabFinalOrder(List<String> crossTabFinalOrder) {
        this.crossTabFinalOrder = crossTabFinalOrder;
    }

    /**
     * @return the logicalParameters
     */
    public List<Map<String, String>> getLogicalParameters() {
        return logicalParameters;
}

    /**
     * @param logicalParameters the logicalParameters to set
     */
    public void setLogicalParameters(List<Map<String, String>> logicalParameters) {
        this.logicalParameters = logicalParameters;
    }

    /**
     * @return the equalInterval
     */
    public String getEqualInterval() {
        return equalInterval;
    }

    /**
     * @param equalInterval the equalInterval to set
     */
    public void setEqualInterval(String equalInterval) {
        this.equalInterval = equalInterval;
    }

    /**
     * @return the enableRootAnalysis
     */
    public String getEnableRootAnalysis() {
        return enableRootAnalysis;
}

    /**
     * @param enableRootAnalysis the enableRootAnalysis to set
     */
    public void setEnableRootAnalysis(String enableRootAnalysis) {
        this.enableRootAnalysis = enableRootAnalysis;
    }

    /**
     * @return the mapMultiLevelDrill
     */
    public String getMapMultiLevelDrill() {
        return mapMultiLevelDrill;
}

    /**
     * @param mapMultiLevelDrill the mapMultiLevelDrill to set
     */
    public void setMapMultiLevelDrill(String mapMultiLevelDrill) {
        this.mapMultiLevelDrill = mapMultiLevelDrill;
    }
     /**
     * @return the mapdrill
     */
    public String getMapdrill() {
        return mapdrill;
    }

    /**
     * @param mapdrill the mapdrill to set
     */
    public void setMapdrill(String mapdrill) {
        this.mapdrill = mapdrill;
    }

    /**
     * @return the dataSlider
     */
    public Map<String, String> getDataSlider() {
        return dataSlider;
}

    /**
     * @param dataSlider the dataSlider to set
     */
    public void setDataSlider(Map<String, String> dataSlider) {
        this.dataSlider = dataSlider;
    }

    /**
     * @return the dataSliderMinMaxValue
     */
    public Map<String,List<String>> getDataSliderMinMaxValue() {
        return dataSliderMinMaxValue;
}

    /**
     * @param dataSliderMinMaxValue the dataSliderMinMaxValue to set
     */
    public void setDataSliderMinMaxValue(Map<String,List<String>> dataSliderMinMaxValue) {
        this.dataSliderMinMaxValue = dataSliderMinMaxValue;
    }

    /**
     * @return the sliderAxisVal
     */
    public Map<String, Map<String, String>> getSliderAxisVal() {
        return sliderAxisVal;
}

    /**
     * @param sliderAxisVal the sliderAxisVal to set
     */
    public void setSliderAxisVal(Map<String, Map<String, String>> sliderAxisVal) {
        this.sliderAxisVal = sliderAxisVal;
    }

    /**
     * @return the topChartOrder
     */
    public String getTopChartOrder() {
        return topChartOrder;
    }

    /**
     * @param topChartOrder the topChartOrder to set
     */
    public void setTopChartOrder(String topChartOrder) {
        this.topChartOrder = topChartOrder;
}

    /**
     * @return the chartNameColol
     */
    public String getChartNameColol() {
        return chartNameColol;
    }

    /**
     * @param chartNameColol the chartNameColol to set
     */
    public void setChartNameColol(String chartNameColol) {
        this.chartNameColol = chartNameColol;
}
/**
     * @return the dateFlag_New
     */
    public String getDateFlag_New() {
        return dateFlag_New;
    }

    /**
     * @param dateFlag_New the dateFlag_New to set
     */
    public void setDateFlag_New(String dateFlag_New) {
        this.dateFlag_New = dateFlag_New;
    }

    /**
     * @return the drillPage
     */
    public String getDrillPage() {
        return drillPage;
}

    /**
     * @param drillPage the drillPage to set
     */
    public void setDrillPage(String drillPage) {
        this.drillPage = drillPage;
    }
        /**
     * @return the actionDrillMap
     */
    public Map<String, Map<String, List<String>>> getActionDrillMap() {
        return actionDrillMap;
    }

    /**
     * @param actionDrillMap the actionDrillMap to set
     */
    public void setActionDrillMap(Map<String, Map<String, List<String>>> actionDrillMap) {
        this.actionDrillMap = actionDrillMap;
    }

    /**
     * @return the customtimeType
     */
    public String getCustomtimeType() {
        return customtimeType;
}

    /**
     * @param customtimeType the customtimeType to set
     */
    public void setCustomtimeType(String customtimeType) {
        this.customtimeType = customtimeType;
    }

    /**
     * @return the customTimeflag
     */
    public String getCustomTimeflag() {
        return customTimeflag;
    }

    /**
     * @param customTimeflag the customTimeflag to set
     */
    public void setCustomTimeflag(String customTimeflag) {
        this.customTimeflag = customTimeflag;
    }

    /**
     * @return the customTimeDate
     */
    public String getCustomTimeDate() {
        return customTimeDate;
    }

    /**
     * @param customTimeDate the customTimeDate to set
     */
    public void setCustomTimeDate(String customTimeDate) {
        this.customTimeDate = customTimeDate;
    }
    
}
