/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.query;

import com.progen.report.PbReportCollection;
import com.progen.report.ReportParameter;
import com.progen.report.util.sort.DataSetFilter;
import com.progen.reportdesigner.db.ReportTemplateDAO;
import java.util.*;
import org.apache.log4j.Logger;
import prg.db.PbDb;
import prg.db.PbReturnObject;
import utils.db.ProgenParam;

/**
 *
 * @author arun
 */
public class TrendDataSetHelper {

    public static Logger logger = Logger.getLogger(TrendDataSetHelper.class);

    public static TrendDataSet buildTrendDataSet(List<String> measIds, String timeLevel, String userId, String folderId) {
        QueryExecutor queryExecutor = new QueryExecutor();
        PbReturnObject pbro = new PbReturnObject();
        ArrayList<String> rowViewBys = new ArrayList<String>();
        rowViewBys.add("TIME");
        ArrayList timeDetails = TrendDataSetHelper.buildTimeInfo(timeLevel);
        PbReportCollection collect = new PbReportCollection();
        PbDb pbDb = new PbDb();

        ArrayList<String> measIdList = new ArrayList<String>();
        measIdList.addAll(measIds);

        TrendDataSetHelper.updateQryColsInCollection(measIdList, collect);

        collect.setRowViewBys(rowViewBys);
        collect.setColumnViewBys(new ArrayList<String>());
        collect.setReportBizRole(Integer.parseInt(folderId));
        collect.timeDetailsArray = timeDetails;
        if (timeLevel.equalsIgnoreCase("Day")) {
            collect.isKpi = true;
        } else {
            collect.isKpi = false;
        }
        PbReportQuery reportQuery = queryExecutor.formulateQuery(collect, userId);
        String query = "";
        try {
            query = reportQuery.generateViewByQry();
        } catch (Exception ex) {
//            ProgenLog.log(ProgenLog.SEVERE, TrendDataSetHelper.class,"buildTrendDataSet","Query Generation Failed");
            logger.error("Query Generation Failed");
        }
        PbReturnObject trendData = null;
        if (query.length() > 0) {
            trendData = queryExecutor.executeQuery(collect, query);
        }

        TrendDataSet trendDataSet = new TrendDataSet(trendData);
        trendDataSet.setNoOfDays(reportQuery.getTrendNoOfDays());
        ArrayList<String> qryMeasIds = new ArrayList<String>();
        for (String measure : measIds) {
            qryMeasIds.add(measure);
        }
        trendDataSet.setMeasures(qryMeasIds);
        return trendDataSet;
    }

    public static void updateQryColsInCollection(ArrayList<String> measIdList, PbReportCollection collect) {
        ArrayList<String> reportQryColAggs = new ArrayList<String>();
        ArrayList<String> reportQryColTypes = new ArrayList<String>();
        ArrayList<String> originalQryColTypes = new ArrayList<String>();
        ReportTemplateDAO dao = new ReportTemplateDAO();
        reportQryColAggs = dao.getReportQryAggregations(measIdList);
        reportQryColTypes = dao.getReportQryColTypes();
        originalQryColTypes = dao.getOriginalReportQryColTypes();

        for (int i = 0; i < measIdList.size(); i++) {
            collect.setReportQueryColumns(measIdList.get(i), "A_" + measIdList.get(i), reportQryColAggs.get(i), reportQryColTypes.get(i), originalQryColTypes.get(i));
        }
    }

    public static HashMap<String, TrendDataSet> buildTrendDataSet(Iterable<String> measIds, ReportParameter parameters, String timeLevel, String userId, String folderId) {
        QueryExecutor queryExecutor = new QueryExecutor();
        PbReturnObject pbro = new PbReturnObject();
        ArrayList<String> rowViewBys = new ArrayList<String>();
        for (String rowViewBy : parameters.getRowViewByForParameter()) {
            rowViewBys.add(rowViewBy);
        }
        rowViewBys.add("TIME");
        ArrayList timeDetails = TrendDataSetHelper.buildTimeInfo(timeLevel);
        PbReportCollection collect = new PbReportCollection();
        PbDb pbDb = new PbDb();

        ArrayList<String> measIdList = new ArrayList<String>();
        for (String measEleId : measIds) {
            measIdList.add(measEleId);
        }

        TrendDataSetHelper.updateQryColsInCollection(measIdList, collect);
        collect.setRowViewBys(rowViewBys);
        collect.setColumnViewBys(new ArrayList<String>());
        collect.setReportBizRole(Integer.parseInt(folderId));
        collect.timeDetailsArray = timeDetails;
//        if(timeLevel.equalsIgnoreCase("Day"))
//            collect.isKpi=true;
//        else
        collect.isKpi = false;

        for (String elementId : parameters.getParameterElementIds()) {
            collect.setParameters(elementId, parameters.getParameterElementValues(elementId));
        }

        PbReportQuery reportQuery = queryExecutor.formulateQuery(collect, userId);
        String query = "";
        try {
            query = reportQuery.generateViewByQry();
        } catch (Exception ex) {
//            ProgenLog.log(ProgenLog.SEVERE, TrendDataSetHelper.class,"buildTrendDataSet","Query Generation Failed");
            logger.error("Query Generation Failed");
        }
        PbReturnObject trendData = null;
        if (query.length() > 0) {
            trendData = queryExecutor.executeQuery(collect, query);
        }


        ArrayList<String> qryMeasIds = new ArrayList<String>();
        for (String measure : measIds) {
            qryMeasIds.add(measure);
        }

        Map<String, Integer> noOfDays = reportQuery.getTrendNoOfDays();
        HashMap<String, TrendDataSet> dataSetMap = TrendDataSetHelper.buildTrendDataSet(trendData, rowViewBys, qryMeasIds);

        Collection<TrendDataSet> vals = dataSetMap.values();
        Iterator<TrendDataSet> iter = vals.iterator();
        while (iter.hasNext()) {
            TrendDataSet dataSet = iter.next();
            dataSet.setNoOfDays(noOfDays);
        }
        return dataSetMap;
    }

    private static HashMap<String, TrendDataSet> buildTrendDataSet(PbReturnObject retObj, ArrayList<String> rowViewBys, ArrayList<String> measIds) {
        HashMap<String, TrendDataSet> trendDataMap = new HashMap<String, TrendDataSet>();

        Set<String> dimValueSet = new HashSet<String>();

        ArrayList<String> viewBysExceptTime = new ArrayList<String>();
        char[] dataTypes = new char[rowViewBys.size() - 1];

        for (int i = 0; i < rowViewBys.size() - 1; i++) {
            viewBysExceptTime.add("A_" + rowViewBys.get(i));
            dataTypes[i] = 'C';
        }
        DataSetFilter filter = new DataSetFilter();
        Object[][] rowData = retObj.retrieveData(viewBysExceptTime, dataTypes);
        filter.setData(rowData, retObj.getViewSequence());

        for (int i = 0; i < retObj.getRowCount(); i++) {
            dimValueSet.add(retObj.getFieldValueString(i, 0));
        }

        ArrayList<String> srchConditions = new ArrayList<String>();
        ArrayList<Object> srchValues = new ArrayList<Object>();
        ArrayList<Integer> rowSeq;
        srchConditions.add("EQ");
        for (String dimValue : dimValueSet) {
            srchValues.clear();
            srchValues.add(dimValue);
            rowSeq = filter.searchDataSet(srchConditions, srchValues);
            TrendDataSet dataSet = TrendDataSetHelper.makeTrendDataSet(retObj, rowSeq);
            dataSet.setMeasures(measIds);
            trendDataMap.put(dimValue, dataSet);
        }
        return trendDataMap;
    }

    private static TrendDataSet makeTrendDataSet(PbReturnObject retObj, ArrayList<Integer> rows) {
        String[] columnTypes = retObj.getColumnTypes();
        String[] newColTypes = new String[columnTypes.length - 1];


        String[] columnNames = retObj.getColumnNames();
        String[] newColNames = new String[columnNames.length - 1];

        for (int i = 1; i < columnTypes.length; i++) {
            newColTypes[i - 1] = columnTypes[i];
        }

        for (int i = 1; i < columnNames.length; i++) {
            newColNames[i - 1] = columnNames[i];
        }

        PbReturnObject trendData = new PbReturnObject();
        trendData.setColumnNames(newColNames);
        trendData.setColumnTypes(newColTypes);
        for (Integer row : rows) {
            for (int i = 0; i < newColNames.length; i++) {
                trendData.setFieldValue(newColNames[i], retObj.getFieldValue(row, newColNames[i]));
            }
            trendData.addRow();
        }



        TrendDataSet dataSet = new TrendDataSet(trendData);
        return dataSet;
    }

    public static ArrayList<String> buildTimeInfo(String timeLevel) {
        ArrayList<String> timeDetails = new ArrayList<String>();
        ProgenParam pParam = new ProgenParam();
        timeDetails.add("Day");
        timeDetails.add("PRG_STD");
        timeDetails.add(pParam.getdateforpage().toString());
        timeDetails.add(timeLevel);
        timeDetails.add("Last Period");
        return timeDetails;
    }
}
