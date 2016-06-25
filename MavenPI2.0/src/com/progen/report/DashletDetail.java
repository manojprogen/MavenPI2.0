/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report;

import com.google.common.base.Predicate;
import com.progen.charts.JqplotGraphProperty;
import com.progen.dashboard.DashboardTableColorGroupHelper;
import com.progen.report.entities.KPI;
import com.progen.report.entities.Report;
import com.progen.report.kpi.DashletPropertiesHelper;
import com.progen.report.kpi.KPISingleGroupHelper;
import java.io.Serializable;
import java.util.*;

/**
 *
 * @author progen
 */
public class DashletDetail implements Serializable {

    private static final long serialVersionUID = 2275768053074881909L;
    private String dashBoardDetailId;
    private String refReportId;
    private String graphId;
    private String kpiMasterId;
    private int displaySequence;
    private String displayType;
    private String dashletName;
    private String kpiType;
    private Report reportDetails;
    private DashletPropertiesHelper dashletPropertiesHelper;
    private int row;
    private int col;
    private int rowSpan;
    private int colSpan;
    private String kpiName;
    private String kpiSymbol;
    private Boolean isTimeSeries;
    private List<KPISingleGroupHelper> singleGroupHelpers = new ArrayList<KPISingleGroupHelper>();
    private List<DashboardTableColorGroupHelper> dashbrdTableColor = new ArrayList<DashboardTableColorGroupHelper>();
    private ArrayList<String> kpiheads = new ArrayList<String>();
    private Boolean editFlag;
    private String rowViewBy = null;
    private ArrayList<String> TextKpis = new ArrayList<String>();
    public Map<String, String> TextkpiDrill = new HashMap<String, String>();
    public Map<String, String> TextkpiComment = new HashMap<String, String>();
    public String Userid = null;
    private String groupId;
    private String groupElements;
    private KPI kpiDetails;
    private String assignedGraphId;
    private String assignedGraphType;
    private boolean jqplotGraph;
    private String jqPlotGraphType;
    private String graphtype;
    private JqplotGraphProperty jqplotgrapprop;
    private String updateClobQry;
    public HashMap modifymeasureAttrChnge = new HashMap();
    private ArrayList<String> hidecolumns = new ArrayList<String>();//aded by sruthi for hide columns
    public String numberofdays = null;
    public HashMap<String, Double> targetmanuval = new HashMap<String, Double>();

    public Boolean getEditFlag() {
        return editFlag;
    }

    public void setEditFlag(Boolean editFlag) {
        this.editFlag = editFlag;
    }

    public Boolean getIsTimeSeries() {
        return isTimeSeries;
    }

    public void setIsTimeSeries(Boolean isTimeSeries) {
        this.isTimeSeries = isTimeSeries;
    }

    public DashletDetail() {
    }

    public String getDashBoardDetailId() {
        return dashBoardDetailId;
    }

    public void setDashBoardDetailId(String dashBoardDetailId) {
        this.dashBoardDetailId = dashBoardDetailId;
    }

    public String getDashletName() {
        return dashletName;
    }

    public void setDashletName(String dashletName) {
        this.dashletName = dashletName;
    }

    public String getkpiName() {
        if (kpiName != null && !kpiName.equalsIgnoreCase("")) {
            return kpiName;
        } else {
            return "KPI REGION";
        }
    }

    public void setkpiName(String kpiName) {
        this.kpiName = kpiName;
    }

    public String getKpiSymbol() {
        if (kpiSymbol != null) {
            return kpiSymbol;
        } else {
            return "";
        }
    }

    public void setKpiSymbol(String kpiSymbol) {
        this.kpiSymbol = kpiSymbol;
    }

    public int getDisplaySequence() {
        return displaySequence;
    }

    public void setDisplaySequence(int displaySequence) {
        this.displaySequence = displaySequence;
    }

    public String getDisplayType() {
        return displayType;
    }

    public void setDisplayType(String displayType) {
        this.displayType = displayType;
    }

    public String getGraphId() {
        return graphId;
    }

    public void setGraphId(String graphId) {
        this.graphId = graphId;
    }

    public String getKpiMasterId() {
        return kpiMasterId;
    }

    public void setKpiMasterId(String kpiMasterId) {
        this.kpiMasterId = kpiMasterId;
    }

    public String getKpiType() {
        return kpiType;
    }

    public void setKpiType(String kpiType) {
        this.kpiType = kpiType;
    }

    public String getRefReportId() {
        return refReportId;
    }

    public void setRefReportId(String refReportId) {
        this.refReportId = refReportId;
    }

    public Report getReportDetails() {
        return reportDetails;
    }

    public void setReportDetails(Report reportDetails) {
        this.reportDetails = reportDetails;
    }

    /**
     * @return the row
     */
    public int getRow() {
        return row;
    }

    /**
     * @param row the row to set
     */
    public void setRow(int row) {
        this.row = row;
    }

    /**
     * @return the col
     */
    public int getCol() {
        return col;
    }

    /**
     * @param col the col to set
     */
    public void setCol(int col) {
        this.col = col;
    }

    /**
     * @return the rowSpan
     */
    public int getRowSpan() {
        return rowSpan;
    }

    /**
     * @param rowSpan the rowSpan to set
     */
    public void setRowSpan(int rowSpan) {
        this.rowSpan = rowSpan;
    }

    /**
     * @return the colSpan
     */
    public int getColSpan() {
        return colSpan;
    }

    /**
     * @param colSpan the colSpan to set
     */
    public void setColSpan(int colSpan) {
        this.colSpan = colSpan;
    }

    public static Predicate<DashletDetail> getDashletDetailPredicate(final String kpiMasterID) {
        Predicate<DashletDetail> predicate = new Predicate<DashletDetail>() {

            @Override
            public boolean apply(DashletDetail input) {
                if (kpiMasterID.equalsIgnoreCase(input.getKpiMasterId())) {
                    return true;
                } else {
                    return false;
                }
            }
        };
        return predicate;
    }

    public List<KPISingleGroupHelper> getSingleGroupHelpers() {
        if (singleGroupHelpers != null) {
            return singleGroupHelpers;
        } else {
            return new ArrayList<KPISingleGroupHelper>();
        }
    }

    public void setSingleGroupHelpers(List<KPISingleGroupHelper> singleGroupHelpers) {
        this.singleGroupHelpers = singleGroupHelpers;
    }

    public List<DashboardTableColorGroupHelper> getDashbrdTableColor() {
        if (dashbrdTableColor != null) {
            return dashbrdTableColor;
        } else {
            return new ArrayList<DashboardTableColorGroupHelper>();
        }
    }

    public void setDashbrdTableColor(List<DashboardTableColorGroupHelper> dadhbrdTableColor) {
        this.dashbrdTableColor = dadhbrdTableColor;
    }

    public static Predicate<DashletDetail> getDashletDetailPredicatBaseOnDashLetID(final String dashletID) {
        Predicate<DashletDetail> predicate = new Predicate<DashletDetail>() {

            @Override
            public boolean apply(DashletDetail input) {
                if (dashletID.equalsIgnoreCase(input.getDashBoardDetailId())) {
                    return true;
                } else {
                    return false;
                }
            }
        };
        return predicate;
    }

    public boolean isGroupElement(String element) {
        boolean status = false;
        if (singleGroupHelpers != null && !singleGroupHelpers.isEmpty()) {
            for (KPISingleGroupHelper singleGroupHelper : singleGroupHelpers) {
                if (singleGroupHelper.getGroupName() != null) {
                    if (singleGroupHelper.getGroupName().equalsIgnoreCase(element)) {
                        status = true;
                        break;
                    }
                }
            }
        }
        return status;
    }

    public boolean isInGroupElement(String element) {
        boolean status = false;
        if (singleGroupHelpers != null && !singleGroupHelpers.isEmpty()) {
            for (KPISingleGroupHelper singleGroupHelper : singleGroupHelpers) {
                if (singleGroupHelper.getElementIds().contains(element)) {
                    status = true;
                    break;
                }
            }
        }

        return status;
    }

    public String getGroupName(String element) {
        String groupName = "";
        if (singleGroupHelpers != null && !singleGroupHelpers.isEmpty()) {
            for (KPISingleGroupHelper singleGroupHelper : singleGroupHelpers) {
                if (singleGroupHelper.getGroupName() != null) {
                    if (singleGroupHelper.getElementIds().contains(element)) {
                        groupName = singleGroupHelper.getGroupName();
                        break;
                    }
                }
            }
        }
        return groupName;
    }

    public void removeSingleGroup(String string) {
        KPISingleGroupHelper helper = null;
        if (!this.singleGroupHelpers.isEmpty()) {
            for (KPISingleGroupHelper groupHelper : this.singleGroupHelpers) {
                if (groupHelper.getGroupName().equalsIgnoreCase(string)) {
                    helper = groupHelper;
                    break;
                }
            }

        }
        if (helper != null) {
            if (helper.getAtrelementIds().isEmpty()) {
                singleGroupHelpers.remove(helper);
            } else {
                helper.setGroupName(null);
                helper.setCalcType(null);
                helper.setElementIds(null);
                helper.setElementNameList(null);

            }
        }
    }

    public Set<KPIElement> removeGroupElements(Set<KPIElement> elemSet) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public ArrayList<String> getKpiheads() {
        return kpiheads;
    }

    public void setKpiheads(ArrayList<String> kpiheads) {
        this.kpiheads = kpiheads;
    }

    /**
     * @return the dashletPropertiesHelper
     */
    public DashletPropertiesHelper getDashletPropertiesHelper() {
        return dashletPropertiesHelper;
    }

    /**
     * @param dashletPropertiesHelper the dashletPropertiesHelper to set
     */
    public void setDashletPropertiesHelper(DashletPropertiesHelper dashletPropertiesHelper) {
        this.dashletPropertiesHelper = dashletPropertiesHelper;
    }

    public String getRowViewBy() {
        return rowViewBy;
    }

    public void setRowViewBy(String rowViewBy) {
        this.rowViewBy = rowViewBy;
    }

    public ArrayList<String> getTextKpis() {
        return TextKpis;
    }

    public void setTextKpis(ArrayList<String> TextKpis) {
        this.TextKpis = TextKpis;
    }

    public void addTextKPIDrill(String elementId, String reportId) {
        TextkpiDrill.put(elementId, reportId);
    }

    public String getTextKPIDrill(String elementId) {
        return TextkpiDrill.get(elementId);
    }

    public void addTextKPIComment(String elementId, String reportId) {
        TextkpiComment.put(elementId, reportId);
    }

    public String getTextKPIComment(String elementId) {
        return TextkpiComment.get(elementId);
    }

    /**
     * @return the groupId
     */
    public String getGroupId() {
        return groupId;
    }

    /**
     * @param groupId the groupId to set
     */
    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    /**
     * @return the groupElements
     */
    public String getGroupElements() {
        return groupElements;
    }

    /**
     * @param groupElements the groupElements to set
     */
    public void setGroupElements(String groupElements) {
        this.groupElements = groupElements;
    }

    /**
     * @author added by srikanth.p to hold kpi details along with graph Details
     * for kpiWithGraph
     * @param kpiDetails
     * @param dispType
     */
    public void setKpiDetails(KPI kpiDetails, String dispType) {
        if (dispType.equalsIgnoreCase("KpiWithGraph")) {
            this.kpiDetails = kpiDetails;
        }
    }

    /**
     * @return the kpiDetails
     */
    public KPI getKpiDetails() {
        return kpiDetails;
    }

    /**
     * @return the assignedGraphId
     */
    public String getAssignedGraphId() {
        return assignedGraphId;
    }

    /**
     * @param assignedGraphId the assignedGraphId to set
     */
    public void setAssignedGraphId(String assignedGraphId) {
        this.assignedGraphId = assignedGraphId;
    }

    /**
     * @return the assignedGraphType
     */
    public String getAssignedGraphType() {
        return this.assignedGraphType;
    }

    /**
     * @param assignedGraphType the assignedGraphType to set
     */
    public void setAssignedGraphType(String assignedGraphType) {
        this.assignedGraphType = assignedGraphType;
    }

    /**
     * @return the jqplotGraph
     */
    public boolean isJqplotGraph() {
        return jqplotGraph;
    }

    /**
     * @param jqplotGraph the jqplotGraph to set
     */
    public void setJqplotGraph(boolean jqplotGraph) {
        this.jqplotGraph = jqplotGraph;
    }

    public String getJqPlotGraphType() {
        return jqPlotGraphType;
    }

    public void setJqPlotGraphType(String jqPlotGraphType) {
        this.jqPlotGraphType = jqPlotGraphType;
    }

    public JqplotGraphProperty getJqplotgrapprop() {
        return jqplotgrapprop;
    }

    public void setJqplotgrapprop(JqplotGraphProperty jqplotgrapprop) {
        this.jqplotgrapprop = jqplotgrapprop;
    }

    /**
     * @return the graphtype
     */
    public String getGraphtype() {
        return graphtype;
    }

    /**
     * @param graphtype the graphtype to set
     */
    public void setGraphtype(String graphtype) {
        this.graphtype = graphtype;
    }

    /**
     * @return the updateClobQry
     */
    public String getUpdateClobQry() {
        return updateClobQry;
    }

    /**
     * @param updateClobQry the updateClobQry to set
     */
    public void setUpdateClobQry(String updateClobQry) {
        this.updateClobQry = updateClobQry;
    }

    public void setmodifymeasureAttrChnge(HashMap modifymeasureAttrChnge) {
        this.modifymeasureAttrChnge = modifymeasureAttrChnge;

    }

    public HashMap getmodifymeasureAttrChng() {
        return modifymeasureAttrChnge;
    }
    //added by sruthi for hide columns

    public ArrayList<String> getHidecolumns() {
        return hidecolumns;
    }

    public void setHidecolumns(ArrayList<String> hidecolumns) {
        this.hidecolumns = hidecolumns;
    }
//ended by sruthi
    //added by sruthi for manuval bud based on dates

    public void setNumberOfDays(String nodays) {
        this.numberofdays = nodays;
    }

    public String getNumberOfDays() {
        return numberofdays;
    }

    public void setTargetMauval(HashMap<String, Double> targetmanuval) {
        this.targetmanuval = targetmanuval;
    }

    public HashMap<String, Double> getTargetMauval() {
        return targetmanuval;
    }
    //ended by sruthi
}
