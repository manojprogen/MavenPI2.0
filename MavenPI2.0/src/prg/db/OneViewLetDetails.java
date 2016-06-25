/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prg.db;

import com.progen.report.MultiPeriodKPI;
import com.progen.report.entities.KPIComment;
import com.progen.reportview.db.CreateKPIFromReport;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 *
 * @author progen
 */
public class OneViewLetDetails implements Serializable {

    private String noOfViewLets;
    private String repId;
    private String repName;
    private String chartname;
    private String graphname;
    private String grapId;
    private String portalId;
    private int row;
    private int col;
    private int rowSpan;
    private int colSpan;
    private int height;
    private int width;
    private String reptype;
    private String healinesIds;
    private String roleId;
    private String Rolename;
    private String chartdrills;
    private String refreshdrills;
    private String drillviewby;
    private String oneviewId;
    private String commentDate;
    private String commentData;
    private String userId;
    private String userName;
    private String roundVal;
    private String formatVal;
    private String measType;
    private String currValue;
    private String priorValue;
    private String measurDrill;
    private String kpiDashLetNo;
    private String kpiMasterId;
    private String kpiType;
    private String displayType;
    private boolean newTypeMeasure;
    private boolean oneviewReportTimeDetails;
    private boolean oneviewcustomtimedetails;
    private boolean oneviewMeasureCompare;
    public HashMap<String, String> graphDetails = new HashMap<String, String>();
    public HashMap<String, String> kpiGraphDetails = new HashMap<String, String>();
    public List<KPIComment> measureComments = new ArrayList<KPIComment>();
    private int grapNo;
    private static final long serialVersionUID = 7526471155622776147L;
    private String graphType;
    private String trendDate;
    private boolean trendGraph;
    private String trendColor;
    private String fontColor;
    private String measureColor;
    private MultiPeriodKPI multiPeriodKPI;
    private ArrayList assignedGraphIds;
    private ArrayList assignedGraphNames;
    private String AssignedReportId;
    private int trendDays;
    private String[] complexKpiMeasureId;
    private String complexMeasureName;
    private String regDate;
    private String prevMeasId;
    public HashMap<String, ArrayList> noteMap = new HashMap<String, ArrayList>();
    private ArrayList complexrowviewbys;
    private ArrayList complexqryAggregations;
    private ArrayList complexreportQryCols;
    private HashMap complexparamValue = new HashMap();
    private Object[] complexdispMeasures;
    private String prefixValue;
    private String suffixValue;
    private String customizePrefix;
    private String customizeSuffix;
    private CreateKPIFromReport kpiFromReport;
    private PbReturnObject pbretObjTime;
    private HashMap retobjects;
    private Container container;
    private boolean measurOprFlag;
    private LinkedHashMap reportParameterValues;
    private String customKpiVal;
    public boolean ispdfEnabled;
    private boolean logicalColor;
    private HashMap rangeColorMap;
    private boolean dialChart;
    private String graphtoreport;
    private LinkedHashMap dialMap;
    private Double targetValPerDay;
    private String dialMeasureBase;
    private boolean userStatus;
    private boolean isschedule;
    private ArrayList msrCustomTimeDetails;  // customTimedetails for Msr and DbrdKpi
    private ArrayList CustomTimeDetails;  // customTimedetails for each region
    private String msrCustomAggregation;
    private String paramId;
    private String paramName = "";
    //   private Container container;

    public OneViewLetDetails() {
    }

    public void addMeasureComments(KPIComment detail) {
        this.measureComments.add(detail);
    }

    public String getNoOfViewLets() {
        return noOfViewLets;
    }

    public void setNoOfViewLets(String noOfViewLets) {
        this.noOfViewLets = noOfViewLets;
    }

    public String getgraphtoreport() {
        return graphtoreport;
    }

    public void setgraphtoreport(String graphtoreport) {
        this.graphtoreport = graphtoreport;
    }

    public String getRepId() {
        return repId;
    }

    public void setRepId(String repId) {
        this.repId = repId;
    }

    public String getRepName() {
        return repName;
    }

    public void setRepName(String repName) {
        this.repName = repName;
    }

    public String getchartname() {
        return chartname;
    }

    public void setchartname(String chartname) {
        this.chartname = chartname;
    }

    public String getgraphname() {
        return graphname;
    }

    public void setgraphname(String graphname) {
        this.graphname = graphname;
    }

    public String getGrapId() {
        return grapId;
    }

    public void setGrapId(String grapId) {
        this.grapId = grapId;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public int getRowSpan() {
        return rowSpan;
    }

    public void setRowSpan(int rowSpan) {
        this.rowSpan = rowSpan;
    }

    public int getColSpan() {
        return colSpan;
    }

    public void setColSpan(int colSpan) {
        this.colSpan = colSpan;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public String getReptype() {
        return reptype;
    }

    public void setReptype(String reptype) {
        this.reptype = reptype;
    }

    public String getPortalId() {
        return portalId;
    }

    public void setPortalId(String portalId) {
        this.portalId = portalId;
    }

    public String getHealinesIds() {
        return healinesIds;
    }

    public void setHealinesIds(String healinesIds) {
        this.healinesIds = healinesIds;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getRolename() {
        return Rolename;
    }

    public void setRolename(String Rolename) {
        this.Rolename = Rolename;
    }

    public String getchartdrills() {
        return chartdrills;
    }

    public void setchartdrills(String chartdrills) {
        this.chartdrills = chartdrills;
    }

    public String getchartrefreshdrills() {
        return refreshdrills;
    }

    public void setchartrefreshdrills(String refreshdrills) {
        this.refreshdrills = refreshdrills;
    }

    public String getdrillviewby() {
        return drillviewby;
    }

    public void setdrillviewby(String drillviewby) {
        this.drillviewby = drillviewby;
    }

    public String getOneviewId() {
        return oneviewId;
    }

    public void setOneviewId(String oneviewId) {
        this.oneviewId = oneviewId;
    }

    public String getCommentDate() {
        return commentDate;
    }

    public void setCommentDate(String commentDate) {
        this.commentDate = commentDate;
    }

    public String getCommentData() {
        return commentData;
    }

    public void setCommentData(String commentData) {
        this.commentData = commentData;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRoundVal() {
        return roundVal;
    }

    public void setRoundVal(String roundVal) {
        this.roundVal = roundVal;
    }

    public String getFormatVal() {
        return formatVal;
    }

    public void setFormatVal(String formatVal) {
        this.formatVal = formatVal;
    }

    public String getMeasType() {
        return measType;
    }

    public void setMeasType(String measType) {
        this.measType = measType;
    }

    public String getCurrValue() {
        return currValue;
    }

    public void setCurrValue(String currValue) {
        this.currValue = currValue;
    }

    public boolean isNewTypeMeasure() {
        return newTypeMeasure;
    }

    public void setNewTypeMeasure(boolean newTypeMeasure) {
        this.newTypeMeasure = newTypeMeasure;
    }

    public String getPriorValue() {
        return priorValue;
    }

    public void setPriorValue(String priorValue) {
        this.priorValue = priorValue;
    }

    public String getMeasurDrill() {
        return measurDrill;
    }

    public void setMeasurDrill(String measurDrill) {
        this.measurDrill = measurDrill;
    }

    public String getKpiDashLetNo() {
        return kpiDashLetNo;
    }

    public void setKpiDashLetNo(String kpiDashLetNo) {
        this.kpiDashLetNo = kpiDashLetNo;
    }

    public String getKpiMasterId() {
        return kpiMasterId;
    }

    public void setKpiMasterId(String kpiMasterId) {
        this.kpiMasterId = kpiMasterId;
    }

    public boolean isOneviewReportTimeDetails() {
        return oneviewReportTimeDetails;
    }

    public void setOneviewReportTimeDetails(boolean oneviewReportTimeDetails) {
        this.oneviewReportTimeDetails = oneviewReportTimeDetails;
    }

    public boolean isoneviewcustomtimedetails() {
        return oneviewcustomtimedetails;
    }

    public void setoneviewcustomtimedetails(boolean oneviewcustomtimedetails) {
        this.oneviewcustomtimedetails = oneviewcustomtimedetails;
    }

    public int getGrapNo() {
        return grapNo;
    }

    public void setGrapNo(int grapNo) {
        this.grapNo = grapNo;
    }

    public boolean isOneviewMeasureCompare() {
        return oneviewMeasureCompare;
    }

    public void setOneviewMeasureCompare(boolean oneviewMeasureCompare) {
        this.oneviewMeasureCompare = oneviewMeasureCompare;
    }

    public String getKpiType() {
        return kpiType;
    }

    public void setKpiType(String kpiType) {
        this.kpiType = kpiType;
    }

    public String getDisplayType() {
        return displayType;
    }

    public void setDisplayType(String displayType) {
        this.displayType = displayType;
    }

    public String getGraphType() {
        return graphType;
    }

    /**
     * @param graphType the graphType to set
     */
    public void setGraphType(String graphType) {
        this.graphType = graphType;
    }

    /**
     * @return the trendDate
     */
    public String getTrendDate() {
        return trendDate;
    }

    /**
     * @param trendDate the trendDate to set
     */
    public void setTrendDate(String trendDate) {
        this.trendDate = trendDate;
    }

    /**
     * @return the trendGraph
     */
    public boolean isTrendGraph() {
        return trendGraph;
    }

    /**
     * @param trendGraph the trendGraph to set
     */
    public void setTrendGraph(boolean trendGraph) {
        this.trendGraph = trendGraph;
    }

    /**
     * @return the trendColor
     */
    public String getTrendColor() {
        return trendColor;
    }

    /**
     * @param trendColor the trendColor to set
     */
    public void setTrendColor(String trendColor) {
        this.trendColor = trendColor;
    }

    /**
     * @return the fontColor
     */
    public String getFontColor() {
        return fontColor;
    }

    /**
     * @param fontColor the fontColor to set
     */
    public void setFontColor(String fontColor) {
        this.fontColor = fontColor;
    }

    public String getMeasureColor() {
        return measureColor;
    }

    public void setMeasureColor(String measureColor) {
        this.measureColor = measureColor;
    }

    public MultiPeriodKPI getMultiPeriodKPI() {
        return multiPeriodKPI;
    }

    public void setMultiPeriodKPI(MultiPeriodKPI multiPeriodKPI) {
        this.multiPeriodKPI = multiPeriodKPI;
    }

    /**
     * @return the assignedGraphIds
     */
    public ArrayList getAssignedGraphIds() {
        return assignedGraphIds;
    }

    /**
     * @param assignedGraphIds the assignedGraphIds to set
     */
    public void setAssignedGraphIds(ArrayList assignedGraphIds) {
        this.assignedGraphIds = assignedGraphIds;
    }

    /**
     * @return the assignedGraphNames
     */
    public ArrayList getAssignedGraphNames() {
        return this.assignedGraphNames;
    }

    /**
     * @param assignedGraphNames the assignedGraphNames to set
     */
    public void setAssignedGraphNames(ArrayList assignedGraphNames) {
        this.assignedGraphNames = assignedGraphNames;
    }

    /**
     * @return the AssignedReportId
     */
    public String getAssignedReportId() {
        return this.AssignedReportId;
    }

    /**
     * @param AssignedReportId the AssignedReportId to set
     */
    public void setAssignedReportId(String AssignedReportId) {
        this.AssignedReportId = AssignedReportId;
    }

    /**
     * @return the trendDays
     */
    public int getTrendDays() {
        return trendDays;
    }

    /**
     * @param trendDays the trendDays to set
     */
    public void setTrendDays(int trendDays) {
        this.trendDays = trendDays;
    }

    public String getComplexMeasureName() {
        return complexMeasureName;
    }

    public void setComplexMeasureName(String complexMeasureName) {
        this.complexMeasureName = complexMeasureName;
    }

    public String[] getComplexKpiMeasureId() {
        return complexKpiMeasureId;
    }

    public void setComplexKpiMeasureId(String[] complexKpiMeasureId) {
        this.complexKpiMeasureId = complexKpiMeasureId;
    }

    public String getRegDate() {
        return regDate;
    }

    public void setRegDate(String regDate) {
        this.regDate = regDate;
    }

    public String getPrevMeasId() {
        return prevMeasId;
    }

    public void setPrevMeasId(String prevMeasId) {
        this.prevMeasId = prevMeasId;
    }

    public ArrayList getComplexrowviewbys() {
        return complexrowviewbys;
    }

    public void setComplexrowviewbys(ArrayList complexrowviewbys) {
        this.complexrowviewbys = complexrowviewbys;
    }

    public ArrayList getComplexqryAggregations() {
        return complexqryAggregations;
    }

    public void setComplexqryAggregations(ArrayList complexqryAggregations) {
        this.complexqryAggregations = complexqryAggregations;
    }

    public ArrayList getComplexreportQryCols() {
        return complexreportQryCols;
    }

    public void setComplexreportQryCols(ArrayList complexreportQryCols) {
        this.complexreportQryCols = complexreportQryCols;
    }

    public HashMap getComplexparamValue() {
        return complexparamValue;
    }

    public void setComplexparamValue(HashMap complexparamValue) {
        this.complexparamValue = complexparamValue;
    }

    public Object[] getComplexdispMeasures() {
        return complexdispMeasures;
    }

    public void setComplexdispMeasures(Object[] complexdispMeasures) {
        this.complexdispMeasures = complexdispMeasures;
    }

    public String getPrefixValue() {
        return prefixValue;
    }

    public void setPrefixValue(String prefixValue) {
        this.prefixValue = prefixValue;
    }

    public String getSuffixValue() {
        return suffixValue;
    }

    public void setSuffixValue(String suffixValue) {
        this.suffixValue = suffixValue;
    }

    public String getCustomizePrefix() {
        return customizePrefix;
    }

    public void setCustomizePrefix(String customizePrefix) {
        this.customizePrefix = customizePrefix;
    }

    public String getCustomizeSuffix() {
        return customizeSuffix;
    }

    public void setCustomizeSuffix(String customizeSuffix) {
        this.customizeSuffix = customizeSuffix;
    }

    public CreateKPIFromReport getKpiFromReport() {
        return kpiFromReport;
    }

    public void setKpiFromReport(CreateKPIFromReport kpiFromReport) {
        this.kpiFromReport = kpiFromReport;
    }

    /**
     * @return the ReturnObjject
     */
    public PbReturnObject getPbReturnObject() {
        return pbretObjTime;
    }

    public HashMap getretobjects() {
        return retobjects;
    }

    public Container getContainer() {
        return container;
    }

    public void setPbReturnObject(PbReturnObject pbretObjTime) {
        this.pbretObjTime = pbretObjTime;
    }

    public void setretobjects(HashMap retobjects) {
        this.retobjects = retobjects;
    }

    public void setContainer(Container container) {
        this.container = container;
    }

    public boolean getMeasurOprFlag() {
        return this.measurOprFlag;
    }

    public void setMeasurOprFlag(boolean measurOprFlagg) {
        this.measurOprFlag = measurOprFlagg;
    }

    public LinkedHashMap getReportParameterValues() {
        return reportParameterValues;
    }

    public void setReportParameterValues(LinkedHashMap reportParameterValues) {
        this.reportParameterValues = reportParameterValues;
    }

    public String getCustomKpiVal() {
        return customKpiVal;
    }

    public void setCustomKpiVal(String customKpiVal) {
        this.customKpiVal = customKpiVal;
    }

    public boolean isLogicalColor() {
        return logicalColor;
    }

    public void setLogicalColor(boolean logicalColor) {
        this.logicalColor = logicalColor;
    }

    public HashMap getRangeColorMap() {
        return rangeColorMap;
    }

    public void setRangeColorMap(HashMap rangeColorMap) {
        this.rangeColorMap = rangeColorMap;
    }

    public boolean isDialChart() {
        return dialChart;
    }

    public LinkedHashMap getDialMap() {
        return dialMap;
    }

    public void setDialMap(LinkedHashMap dialMap) {
        this.dialMap = dialMap;
    }

    public void setDialChart(boolean dialChart) {
        this.dialChart = dialChart;
    }

    public Double getTargetValPerDay() {
        return targetValPerDay;
    }

    public void setTargetValPerDay(Double targetVal) {
        this.targetValPerDay = targetVal;
    }

    public String getDialMeasureBase() {
        return dialMeasureBase;
    }

    public void setDialMeasureBase(String dialMeasureBase) {
        this.dialMeasureBase = dialMeasureBase;
    }

    public boolean getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(boolean userStatus) {
        this.userStatus = userStatus;
    }

    public boolean getisschedule() {
        return isschedule;
    }

    public void setisschedule(boolean isschedule) {
        this.isschedule = isschedule;
    }

    public ArrayList getMsrCustomTimeDetails() {
        return msrCustomTimeDetails;
    }

    public void setMsrCustomTimeDetails(ArrayList msrCustomTimeDetails) {
        this.msrCustomTimeDetails = msrCustomTimeDetails;
    }

    public ArrayList getCustomTimeDetails() {
        return CustomTimeDetails;
    }

    public void setCustomTimeDetails(ArrayList CustomTimeDetails) {
        this.CustomTimeDetails = CustomTimeDetails;
    }

    /**
     * @return the msrCustomAggregation
     */
    public String getMsrCustomAggregation() {
        return msrCustomAggregation;
    }

    /**
     * @param msrCustomAggregation the msrCustomAggregation to set
     */
    public void setMsrCustomAggregation(String msrCustomAggregation) {
        this.msrCustomAggregation = msrCustomAggregation;
    }

    /**
     * @return the paramId
     */
    public String getParamId() {
        return paramId;
    }

    /**
     * @param paramId the paramId to set
     */
    public void setParamId(String paramId) {
        this.paramId = paramId;
    }

    /**
     * @return the paramName
     */
    public String getParamName() {
        return paramName;
    }

    /**
     * @param paramName the paramName to set
     */
    public void setParamName(String paramName) {
        this.paramName = paramName;
    }
}
