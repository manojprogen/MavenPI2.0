/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report;

import java.util.List;

/**
 *
 * @author
 */
public class ReportObjectMeta {

    private String finalSql_AO = null;
    private String finalSqlNew_AO = null;
    private String osql_AO = null;
    private String OViewByCol_AO = null;
    private String OorderByCol_AO = null;
    private String omsql_AO = null;
    private String OmViewByCol_AO = null;
    private String OmorderByCol_AO = null;
    private String ColOrderByCol_AO = null;
    private String osqlGroup_AO = null;
    private String finalViewByCol_AO = null;
    private String oWrapper_AO = null;
    private String tableName_AO = null;
    private String filters_AO = null;
    private boolean isAOEnable = false;
    private List<String> viewIds = null;
    private List<String> ReportRowViewbyvals = null;
    private List<String> ReportColViewbyvals = null;
    private List<String> ReportQryElementsIds = null;
    private List<String> ReportQryAggregations = null;
//    private HashMap reportParameters = new HashMap();
//    private HashMap<String,HashMap<String,List>> operatorFilters=new HashMap<String,HashMap<String,List>>();
//    private boolean isTimeDrill;
//    private boolean isTimeSeries;
    private String[] reportBizRoles = null;
//    private String defaultSortedColumn = "";
    private String crosstabGTDisplayPos;
    private String crosstabSTDisplayPos;
    private List<String> viewNames = null;
    private List<String> measIds = null;
    private List<String> measNames = null;
    private List<String> aggregations = null;
    private String dateValues_AO = null;
    private String finalSqlSummarized_AO = null;
    private String finalSummarizedGrpBy_AO = null;
    private String OViewByColGrp_AO = null;
    private boolean isDimSegOnSumm = false;
    public String graphfiltersize = "";
//    public String getFinalSql_AO() {
//        return finalSql_AO;
//    }
//
//    public void setFinalSql_AO(String finalSql_AO) {
//        this.finalSql_AO = finalSql_AO;
//    }

    public String getOsql_AO() {
        return osql_AO;
    }

    public void setOsql_AO(String osql_AO) {
        this.osql_AO = osql_AO;
    }

    public String getOViewByCol_AO() {
        return OViewByCol_AO;
    }

    public void setOViewByCol_AO(String OViewByCol_AO) {
        this.OViewByCol_AO = OViewByCol_AO;
    }

    public String getOorderByCol_AO() {
        return OorderByCol_AO;
    }

    public void setOorderByCol_AO(String OorderByCol_AO) {
        this.OorderByCol_AO = OorderByCol_AO;
    }

    public String getOmsql_AO() {
        return omsql_AO;
    }

    public void setOmsql_AO(String omsql_AO) {
        this.omsql_AO = omsql_AO;
    }

    public String getOmViewByCol_AO() {
        return OmViewByCol_AO;
    }

    public void setOmViewByCol_AO(String OmViewByCol_AO) {
        this.OmViewByCol_AO = OmViewByCol_AO;
    }

    public void setOmorderByCol_AO(String OmorderByCol_AO) {
        this.OmorderByCol_AO = OmorderByCol_AO;
    }

    public String getOmorderByCol_AO() {
        return OmorderByCol_AO;
    }

    public String getColOrderByCol_AO() {
        return ColOrderByCol_AO;
    }

    public String getOsqlGroup_AO() {
        return osqlGroup_AO;
    }

    public void setOsqlGroup_AO(String osqlGroup_AO) {
        this.osqlGroup_AO = osqlGroup_AO;
    }

    public String getFinalViewByCol_AO() {
        return finalViewByCol_AO;
    }

    public void setFinalViewByCol_AO(String finalViewByCol_AO) {
        this.finalViewByCol_AO = finalViewByCol_AO;
    }

    public String getoWrapper_AO() {
        return oWrapper_AO;
    }

    public void setoWrapper_AO(String oWrapper_AO) {
        this.oWrapper_AO = oWrapper_AO;
    }

    public String getTableName_AO() {
        return tableName_AO;
    }

    public void setTableName_AO(String tableName_AO) {
        this.tableName_AO = tableName_AO;
    }

    public String getFilters_AO() {
        return filters_AO;
    }

    public void setFilters_AO(String filters_AO) {
        this.filters_AO = filters_AO;
    }

    public boolean isIsAOEnable() {
        return isAOEnable;
    }

    public void setIsAOEnable(boolean isAOEnable) {
        this.isAOEnable = isAOEnable;
    }

    public void setColOrderByCol_AO(String ColOrderByCol_AO) {
        this.ColOrderByCol_AO = ColOrderByCol_AO;
    }

    public List<String> getViewIds() {
        return viewIds;
    }

    public void setViewIds(List<String> viewIds) {
        this.viewIds = viewIds;
    }

    public List<String> getReportRowViewbyvals() {
        return ReportRowViewbyvals;
    }

    public void setReportRowViewbyvals(List<String> ReportRowViewbyvals) {
        this.ReportRowViewbyvals = ReportRowViewbyvals;
    }

    public List<String> getReportColViewbyvals() {
        return ReportColViewbyvals;
    }

    public void setReportColViewbyvals(List<String> ReportColViewbyvals) {
        this.ReportColViewbyvals = ReportColViewbyvals;
    }

    public List<String> getReportQryElementsIds() {
        return ReportQryElementsIds;
    }

    public void setReportQryElementsIds(List<String> ReportQryElementsIds) {
        this.ReportQryElementsIds = ReportQryElementsIds;
    }

    public List<String> getReportQryAggregations() {
        return ReportQryAggregations;
    }

    public void setReportQryAggregations(List<String> ReportQryAggregations) {
        this.ReportQryAggregations = ReportQryAggregations;
    }
//    public HashMap getreportParameters() {
//        return reportParameters;
//    }
//
//    public void setreportParameters(HashMap reportParameters) {
//        this.reportParameters = reportParameters;
//    }
//    public HashMap<String,HashMap<String,List>> getoperatorFilters() {
//        return operatorFilters;
//    }
//
//    public void setoperatorFilters(HashMap<String,HashMap<String,List>> operatorFilters) {
//        this.operatorFilters = operatorFilters;
//    }
//    public boolean getisTimeDrill() {
//        return isTimeDrill;
//    }
//
//    public void isTimeDrill(boolean isTimeDrill) {
//        this.isTimeDrill = isTimeDrill;
//    }
//    public boolean getisTimeSeries() {
//        return isTimeSeries;
//    }
//
//    public void isTimeSeries(boolean isTimeSeries) {
//        this.isTimeSeries = isTimeSeries;
//    }

    public String[] getreportBizRoles() {
        return reportBizRoles;
    }

    public void setreportBizRoles(String[] reportBizRoles) {
        this.reportBizRoles = reportBizRoles;
    }
//    public String getdefaultSortedColumn() {
//        return defaultSortedColumn;
//    }
//
//    public void setdefaultSortedColumn(String defaultSortedColumn) {
//        this.defaultSortedColumn = defaultSortedColumn;
//    }

    public String getcrosstabGTDisplayPos() {
        return crosstabGTDisplayPos;
    }

    public void setcrosstabGTDisplayPos(String crosstabGTDisplayPos) {
        this.crosstabGTDisplayPos = crosstabGTDisplayPos;
    }

    public String getcrosstabSTDisplayPos() {
        return crosstabGTDisplayPos;
    }

    public void setcrosstabSTDisplayPos(String crosstabSTDisplayPos) {
        this.crosstabGTDisplayPos = crosstabGTDisplayPos;
    }

    public List<String> getViewNames() {
        return viewNames;
    }

    public void setViewNames(List<String> viewNames) {
        this.viewNames = viewNames;
    }

    public List<String> getMeasIds() {
        return measIds;
    }

    public void setMeasIds(List<String> measIds) {
        this.measIds = measIds;
    }

    public List<String> getMeasNames() {
        return measNames;
    }

    public void setMeasNames(List<String> measNames) {
        this.measNames = measNames;
    }

    public List getAggregations() {
        return aggregations;
    }

    public void setAggregations(List aggregations) {
        this.aggregations = aggregations;
    }

    public String getFinalSql_AO() {
        return finalSql_AO;
    }

    public void setFinalSql_AO(String finalSql_AO) {
        this.finalSql_AO = finalSql_AO;
    }

    /**
     * @return the finalSqlNew_AO
     */
    public String getFinalSqlNew_AO() {
        return finalSqlNew_AO;
    }

    /**
     * @param finalSqlNew_AO the finalSqlNew_AO to set
     */
    public void setFinalSqlNew_AO(String finalSqlNew_AO) {
        this.finalSqlNew_AO = finalSqlNew_AO;
    }

    public String getDateValues_AO() {
        return dateValues_AO;
    }

    /**
     * @param finalSqlNew_AO the finalSqlNew_AO to set
     */
    public void setDateValues_AO(String dateValues_AO) {
        this.dateValues_AO = dateValues_AO;
    }

    public String getFinalSqlSummarized_AO() {
        return finalSqlSummarized_AO;
    }

    public void setFinalSqlSummarized_AO(String finalSqlSummarized_AO) {
        this.finalSqlSummarized_AO = finalSqlSummarized_AO;
    }

    public String getFinalSummarizedGrpBy_AO() {
        return finalSummarizedGrpBy_AO;
    }

    public void setFinalSummarizedGrpBy_AO(String finalSummarizedGrpBy_AO) {
        this.finalSummarizedGrpBy_AO = finalSummarizedGrpBy_AO;
    }

    public boolean getIsDimSegOnSumm() {
        return isDimSegOnSumm;
    }

    public void setIsDimSegOnSumm(boolean isDimSegOnSumm) {
        this.isDimSegOnSumm = isDimSegOnSumm;
    }

    public String getOViewByColGrp_AO() {
        return OViewByColGrp_AO;
    }

    public void setOViewByColGrp_AO(String OViewByColGrp_AO) {
        this.OViewByColGrp_AO = OViewByColGrp_AO;
    }
//added by sruthi to show filters

    public void setgraphfiltersize(String currencyType) {
        this.graphfiltersize = currencyType;
    }

    public String getgraphfiltersize() {
        return graphfiltersize;
    }//ended by sruthi
}
