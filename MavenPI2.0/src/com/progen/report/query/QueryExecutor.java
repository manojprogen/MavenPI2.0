/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.query;

import com.progen.db.ProgenDataSet;
import com.progen.report.PbReportCollection;
import com.progen.report.ReportObjectMeta;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import prg.db.PbReturnObject;

/**
 *
 * @author progen
 */
public class QueryExecutor {

    public PbReportQuery formulateQuery(PbReportCollection collect, String userId) {
        PbReportQuery repQuery = new PbReportQuery();
//        HashMap collectMulticalender = collect.getcollectMulticalender();
        repQuery.collectMulticalender = collect.getcollectMulticalender();
        //modified by Dinanath for handling null
        if (collect.ReportLayout != null && (collect.ReportLayout.equalsIgnoreCase("KPI") || collect.ReportLayout.equalsIgnoreCase("None"))) {
            collect.isKpi = true;
            repQuery.isKpi = true;
            repQuery.setRowViewbyCols(new ArrayList());
            repQuery.setColViewbyCols(new ArrayList());            //added by mohit for kpi and none
//        collect.reportRowViewbyValues=new ArrayList();
//        collect.reportColViewbyValues=new ArrayList();
        } else {
            collect.isKpi = false;
            repQuery.isKpi = false;
            repQuery.setRowViewbyCols(collect.reportRowViewbyValues);
            repQuery.setColViewbyCols(collect.reportColViewbyValues);
        }
        repQuery.avoidProGenTime = collect.avoidProgenTime;
        repQuery.setQryColumns(collect.reportQryElementIds);
        repQuery.setColAggration(collect.reportQryAggregations);

        HashMap<String, String> repParamsIncluded = new HashMap<String, String>();
        HashMap<String, String> repParamsExcluded = new HashMap<String, String>();

        HashMap collRepParameters = collect.reportParameters;
        Set<String> dimElements = collRepParameters.keySet();
        for (String dimension : dimElements) {
            ArrayList paramInfo = (ArrayList) collRepParameters.get(dimension);
            // 
            StringBuilder filters = new StringBuilder();
            for (String filterStr : (List<String>) paramInfo.get(8)) {
                filters.append(",").append(filterStr).append("");
            }
            if (paramInfo.get(10).equals("NOT_SELECTED") || paramInfo.get(10).equals("INCLUDED") || paramInfo.get(10).equals("LIKE") || paramInfo.get(10).equals("IN")) {
                repParamsIncluded.put(dimension, filters.substring(1));
            } else if (paramInfo.get(10).equals("EXCLUDED") || paramInfo.get(10).equals("NOT LIKE") || paramInfo.get(10).equals("NOT IN")) {
                repParamsExcluded.put(dimension, filters.substring(1));
            }
        }
        //modified start by Nazneen on 9 Dec 2014 for handling null pointer
//        repQuery.setInMap(collect.operatorFilters.get("IN"));
//        repQuery.setNotInMap(collect.operatorFilters.get("NOTIN"));
//        repQuery.setLikeMap(collect.operatorFilters.get("LIKE"));
//        repQuery.setNotLikeMap(collect.operatorFilters.get("NOTLIKE"));
        if (collect.operatorFilters != null && !collect.operatorFilters.isEmpty()) {
            if (collect.operatorFilters.get("IN") != null) {
                repQuery.setInMap(collect.operatorFilters.get("IN"));
            }
            if (collect.operatorFilters.get("NOTIN") != null) {
                repQuery.setNotInMap(collect.operatorFilters.get("NOTIN"));
            }
            if (collect.operatorFilters.get("LIKE") != null) {
                repQuery.setLikeMap(collect.operatorFilters.get("LIKE"));
            }
            if (collect.operatorFilters.get("NOTLIKE") != null) {
                repQuery.setNotLikeMap(collect.operatorFilters.get("NOTLIKE"));
            }
        }
        //modified end by Nazneen on 9 Dec 2014 for handling null pointer


        repQuery.setParamValue(repParamsIncluded);
        repQuery.setExcludedParameters(repParamsExcluded);
        repQuery.isTimeDrill = collect.isTimeDrill;
        repQuery.isTimeSeries = collect.isTimeSeries;
        repQuery.isKpi = collect.isKpi;
        repQuery.setTimeDetails(collect.timeDetailsArray);
        if (collect.reportQryElementIds != null && !(collect.reportQryElementIds.isEmpty())) {
            repQuery.setDefaultMeasure((String) collect.reportQryElementIds.get(0));
            repQuery.setDefaultMeasureSumm((String) collect.reportQryAggregations.get(0));
        }
        repQuery.setUserId(userId);

        StringBuilder bizRolesSb = new StringBuilder();
        for (int str = 0; str < collect.reportBizRoles.length; str++) {
            if (str == (collect.reportBizRoles.length - 1)) {
                bizRolesSb.append(collect.reportBizRoles[str]);
            } else {
                bizRolesSb.append(collect.reportBizRoles[str] + ",");
            }
        }
        repQuery.setBizRoles(bizRolesSb.toString());
        if (collect.reportId != null && !"".equals(collect.reportId)) {
            repQuery.setReportId(collect.reportId);
        }
        boolean defSortExists = false;


        //default sorted column here is the order number of the columns in select query
        if (collect.defaultSortedColumn != null) {
            if (collect.reportRowViewbyValues.contains(collect.defaultSortedColumn)) {
                for (int i = 0; i < collect.reportRowViewbyValues.size(); i++) {
                    if (collect.reportRowViewbyValues.get(i).toString().equalsIgnoreCase(collect.defaultSortedColumn)) {
                        collect.defaultSortedColumn = String.valueOf(i + 1);
                        defSortExists = true;
                    }
                }
            } else if (collect.reportColViewbyValues != null && collect.reportColViewbyValues.size() == 0 && collect.reportQryElementIds != null && !collect.reportQryElementIds.isEmpty()) {
                if (collect.reportQryElementIds.contains(collect.defaultSortedColumn)) {
                    defSortExists = true;
                    for (int i = 0; i < collect.reportQryElementIds.size(); i++) {
                        if (collect.reportQryElementIds.get(i).toString().equalsIgnoreCase(collect.defaultSortedColumn)) {
                            defSortExists = true;
                            collect.defaultSortedColumn = String.valueOf(i + collect.reportRowViewbyValues.size() + 1);
                        }
                    }
                }
            }
        }
        if (defSortExists) {
            repQuery.setDefaultSortedColumn(collect.defaultSortedColumn);//need to be removed later
        }


        if (collect.reportColViewbyValues != null && !collect.reportColViewbyValues.isEmpty()) {
            repQuery.setGrandTotalSubTotalDisplayPosition(collect.crosstabGTDisplayPos, collect.crosstabSTDisplayPos);
        }

        return repQuery;

    }

    public PbReturnObject executeQuery(PbReportCollection collect, String query) {
        PbReportQuery repQuery = new PbReportQuery();
        PbReturnObject pbretObj;
        if (collect.reportQryElementIds != null && !collect.reportQryElementIds.isEmpty()) {
            pbretObj = repQuery.getPbReturnObjectWithFlag((String) collect.reportQryElementIds.get(0), query);
        } else {
            pbretObj = repQuery.getPbReturnObjectWithFlag((String) collect.reportRowViewbyValues.get(0), query);
        }
        return pbretObj;
    }

    public ProgenDataSet executeQuery(PbReportCollection collect, String query, boolean poiDataSet) {
        PbReportQuery repQuery = new PbReportQuery();
        ProgenDataSet pbretObj;
        if (collect.reportQryElementIds != null && !collect.reportQryElementIds.isEmpty()) {
            pbretObj = repQuery.getProgenDataSet((String) collect.reportQryElementIds.get(0), query, poiDataSet);
        } else {
            pbretObj = repQuery.getProgenDataSet((String) collect.reportRowViewbyValues.get(0), query, poiDataSet);
        }
        return pbretObj;
    }

    public ProgenDataSet executeQuery1(ReportObjectMeta goMeta, String query, boolean poiDataSet) {
        PbReportQuery repQuery = new PbReportQuery();
        ProgenDataSet pbretObj;
        if (goMeta.getReportQryElementsIds() != null && !goMeta.getReportQryElementsIds().isEmpty()) {
            pbretObj = repQuery.getProgenDataSet((String) goMeta.getReportQryElementsIds().get(0), query, poiDataSet);
        } else {
            pbretObj = repQuery.getProgenDataSet((String) goMeta.getReportRowViewbyvals().get(0), query, poiDataSet);
        }
        return pbretObj;
    }

    public PbDashBoardQuery formulateQueryDashbard(PbReportCollection collect, String userId) {
        PbDashBoardQuery repQuery = new PbDashBoardQuery();
        ArrayList<String> rowviewbys = new ArrayList<String>();
        repQuery.avoidProGenTime = collect.avoidProgenTime;
//        repQuery.setRowViewbyCols(collect.reportRowViewbyValues);
//        repQuery.setColViewbyCols(collect.reportColViewbyValues);
        repQuery.setRowViewbyCols(rowviewbys);
        repQuery.setColViewbyCols(rowviewbys);
        repQuery.setQryColumns(collect.reportQryElementIds);
        repQuery.setColAggration(collect.reportQryAggregations);

        HashMap<String, String> repParamsIncluded = new HashMap<String, String>();
        HashMap<String, String> repParamsExcluded = new HashMap<String, String>();

        HashMap collRepParameters = collect.reportParameters;
        Set<String> dimElements = collRepParameters.keySet();
        for (String dimension : dimElements) {
            ArrayList paramInfo = (ArrayList) collRepParameters.get(dimension);
            // 
            StringBuilder filters = new StringBuilder();
            for (String filterStr : (List<String>) paramInfo.get(8)) {
                filters.append(",").append(filterStr).append("");
            }
            if (paramInfo.get(10).equals("NOT_SELECTED") || paramInfo.get(10).equals("INCLUDED") || paramInfo.get(10).equals("LIKE") || paramInfo.get(10).equals("IN")) {
                repParamsIncluded.put(dimension, filters.substring(1));
            } else if (paramInfo.get(10).equals("EXCLUDED") || paramInfo.get(10).equals("NOT LIKE") || paramInfo.get(10).equals("NOT IN")) {
                repParamsExcluded.put(dimension, filters.substring(1));
            }
        }
        //modified start by Nazneen on 9 Dec 2014 for handling null pointer
//        repQuery.setInMap(collect.operatorFilters.get("IN"));
//        repQuery.setNotInMap(collect.operatorFilters.get("NOTIN"));
//        repQuery.setLikeMap(collect.operatorFilters.get("LIKE"));
//        repQuery.setNotLikeMap(collect.operatorFilters.get("NOTLIKE"));
        if (collect.operatorFilters != null && !collect.operatorFilters.isEmpty()) {
            if (collect.operatorFilters.get("IN") != null) {
                repQuery.setInMap(collect.operatorFilters.get("IN"));
            }
            if (collect.operatorFilters.get("NOTIN") != null) {
                repQuery.setNotInMap(collect.operatorFilters.get("NOTIN"));
            }
            if (collect.operatorFilters.get("LIKE") != null) {
                repQuery.setLikeMap(collect.operatorFilters.get("LIKE"));
            }
            if (collect.operatorFilters.get("NOTLIKE") != null) {
                repQuery.setNotLikeMap(collect.operatorFilters.get("NOTLIKE"));
            }
        }
        //modified end by Nazneen on 9 Dec 2014 for handling null pointer


        repQuery.setParamValue(repParamsIncluded);
        repQuery.setExcludedParameters(repParamsExcluded);
        repQuery.isTimeDrill = collect.isTimeDrill;
        repQuery.isTimeSeries = collect.isTimeSeries;
        repQuery.isKpi = collect.isKpi;
        repQuery.setTimeDetails(collect.timeDetailsArray);
        if (collect.reportQryElementIds != null && !(collect.reportQryElementIds.isEmpty())) {
            repQuery.setDefaultMeasure((String) collect.reportQryElementIds.get(0));
            repQuery.setDefaultMeasureSumm((String) collect.reportQryAggregations.get(0));
        }
        repQuery.setUserId(userId);

        StringBuilder bizRolesSb = new StringBuilder();
        for (int str = 0; str < collect.reportBizRoles.length; str++) {
            if (str == (collect.reportBizRoles.length - 1)) {
                bizRolesSb.append(collect.reportBizRoles[str]);
            } else {
                bizRolesSb.append(collect.reportBizRoles[str] + ",");
            }
        }
        repQuery.setBizRoles(bizRolesSb.toString());
        if (collect.reportId != null && !"".equals(collect.reportId)) {
            repQuery.setReportId(collect.reportId);
        }
        boolean defSortExists = false;


        //default sorted column here is the order number of the columns in select query
        if (collect.defaultSortedColumn != null) {
            if (collect.reportRowViewbyValues.contains(collect.defaultSortedColumn)) {
                for (int i = 0; i < collect.reportRowViewbyValues.size(); i++) {
                    if (collect.reportRowViewbyValues.get(i).toString().equalsIgnoreCase(collect.defaultSortedColumn)) {
                        collect.defaultSortedColumn = String.valueOf(i + 1);
                        defSortExists = true;
                    }
                }
            } else if (collect.reportColViewbyValues != null && collect.reportColViewbyValues.size() == 0 && collect.reportQryElementIds != null && !collect.reportQryElementIds.isEmpty()) {
                if (collect.reportQryElementIds.contains(collect.defaultSortedColumn)) {
                    defSortExists = true;
                    for (int i = 0; i < collect.reportQryElementIds.size(); i++) {
                        if (collect.reportQryElementIds.get(i).toString().equalsIgnoreCase(collect.defaultSortedColumn)) {
                            defSortExists = true;
                            collect.defaultSortedColumn = String.valueOf(i + collect.reportRowViewbyValues.size() + 1);
                        }
                    }
                }
            }
        }
        if (defSortExists) {
            repQuery.setDefaultSortedColumn(collect.defaultSortedColumn);//need to be removed later
        }


        if (collect.reportColViewbyValues != null && !collect.reportColViewbyValues.isEmpty()) {
            repQuery.setGrandTotalSubTotalDisplayPosition(collect.crosstabGTDisplayPos, collect.crosstabSTDisplayPos);
        }

        return repQuery;

    }
}
