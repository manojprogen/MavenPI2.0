/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.query;

//import com.progen.log.ProgenLog;
import com.progen.report.PbReportCollection;
import java.sql.SQLException;
import java.util.*;
import org.apache.log4j.Logger;
import prg.db.PbDb;
import prg.db.PbReturnObject;

/**
 *
 * @author progen
 */
public class DataSetHelper {

    private PbReportCollection collect;
    private String sortString;
    private String userId;
    private String measConditions;
    public static Logger logger = Logger.getLogger(DataSetHelper.class);

    public DataSet getDataSet() {

        QueryExecutor queryExecutor = new QueryExecutor();
        PbReportQuery repQuery = queryExecutor.formulateQuery(collect, userId);

        DataSet dataSet = null;
        try {
            String query = repQuery.generateViewByQry();

            if (measConditions != null) {
                query = query + measConditions;
            }
            if (sortString != null && !"".equals(sortString)) {
                query = query + " " + sortString;
            }

            PbReturnObject pbretObj = queryExecutor.executeQuery(collect, query);
            dataSet = new DataSet(pbretObj);
            dataSet.setNoOfDays(repQuery.getNoOfDays());

            List<String> qryColsNames = new ArrayList<String>();
            for (String meas : collect.reportQryElementIds) {
                qryColsNames.add((String) repQuery.NonViewByMap.get("A_" + meas));
            }
            dataSet.setMeasNames(qryColsNames);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }

//        String[] cols = pbretObj.getColumnNames();
//        
//        for (int i=0;i<cols.length;i++)
//            System.out.print(cols[i]+"   ");
//        
//        for (int i=0;i<pbretObj.getRowCount();i++){
//            for (int j=0;j<cols.length;j++){
//                System.out.print(pbretObj.getFieldValueString(i, j)+ "   ");
//            }
//            
//        }
        return dataSet;
    }

    public static Map<String, String> getAggregationMap(List<String> measIds) {
        Map<String, String> aggMap = new HashMap<String, String>();
        if (!measIds.isEmpty()) {
            StringBuilder measSB = new StringBuilder();
            for (String measId : measIds) {
                measSB.append(",").append(measId);
            }

            String measuresStr = measSB.substring(1);

            StringBuilder sqlstr = new StringBuilder();
//            String sqlstr = "select ELEMENT_ID , AGGREGATION_TYPE ";
//            sqlstr += " from ";
//            sqlstr += " PRG_USER_ALL_INFO_DETAILS ";
//            sqlstr += " where ELEMENT_ID in (" + measuresStr + ")";
            sqlstr.append("select ELEMENT_ID , AGGREGATION_TYPE  from  PRG_USER_ALL_INFO_DETAILS  where ELEMENT_ID in (");
            sqlstr.append(measuresStr).append(")");
            try {
                PbDb pbDb = new PbDb();
                PbReturnObject pbro = pbDb.execSelectSQL(sqlstr.toString());

                if (pbro != null && pbro.getRowCount() > 0) {
                    for (int i = 0; i < pbro.getRowCount(); i++) {
                        String elementId = pbro.getFieldValueString(i, 0);
                        String aggType = pbro.getFieldValueString(i, 1);
                        aggMap.put(elementId, aggType);
                    }
                }
            } catch (SQLException ex) {
//                ProgenLog.log(ProgenLog.SEVERE, DataSetHelper.class,"getAggregationMap","Exception in getting agg types");
                logger.error("Exception in getting agg types: ", ex);
            }
        }
        return aggMap;
    }

    private DataSetHelper(DataSetHelperBuilder builder) {
        collect = new PbReportCollection();
        collect.setColumnViewBys(builder.colViewBys);
        collect.setRowViewBys(builder.rowViewBys);
        collect.reportBizRoles = builder.bizRole;
        this.userId = builder.userId;
        this.sortString = builder.orderBys;

        Map<String, String> paramValues = builder.paramValues;
        if (paramValues != null) {
            Set<String> keySet = paramValues.keySet();
            Iterator<String> keyIter = keySet.iterator();
            while (keyIter.hasNext()) {
                String dimId = keyIter.next();
                String dimVals = paramValues.get(dimId);

                String[] valArr = dimVals.split(",");
                List<String> valueList = new ArrayList<String>();
                valueList.addAll(Arrays.asList(valArr));
                collect.setParameters(dimId, "", valueList);
            }
        }

        ArrayList<String> queryCols = builder.measIds;
        ArrayList<String> queryAggs = builder.measAggrs;

        if (queryAggs == null || queryAggs.isEmpty()) {
            queryAggs = new ArrayList<String>();
            Map<String, String> aggMap = getAggregationMap(queryCols);

            for (String measId : queryCols) {
                if (aggMap.containsKey(measId)) {
                    queryAggs.add(aggMap.get(measId));
                }
            }
        }

        collect.reportQryElementIds = queryCols;
        collect.reportQryAggregations = queryAggs;
        collect.timeDetailsArray = builder.timeDetails;

        if (builder.conditions != null && !builder.conditions.isEmpty()) {
            Set<String> keySet = builder.conditions.keySet();
            for (String measId : keySet) {
                String condition = builder.conditions.get(measId);
                measConditions = " and " + measId + condition;
            }
            measConditions = measConditions.substring(4);
            measConditions = " where " + measConditions;
        }
    }

    public static class DataSetHelperBuilder {

        private ArrayList<String> rowViewBys;
        private ArrayList<String> colViewBys;
        private ArrayList<String> timeDetails;
        private ArrayList<String> measIds;
        private Map<String, String> paramValues;
        private String userId;
        private String[] bizRole;
        private Map<String, String> conditions;
        private ArrayList<String> measAggrs;
        private String orderBys;

        public DataSetHelper build() {
            return new DataSetHelper(this);
        }

        public DataSetHelperBuilder bizRole(String[] val) {
            bizRole = val;
            return this;
        }

        public DataSetHelperBuilder userId(String val) {
            userId = val;
            return this;
        }

        public DataSetHelperBuilder paramValues(Map<String, String> val) {
            paramValues = val;
            return this;
        }

        public DataSetHelperBuilder measConditions(Map<String, String> val) {
            conditions = val;
            return this;
        }

        public DataSetHelperBuilder measIds(ArrayList<String> val) {
            measIds = val;
            return this;
        }

        public DataSetHelperBuilder measAggrs(ArrayList<String> val) {
            measAggrs = val;
            return this;
        }

        public DataSetHelperBuilder timeDetails(ArrayList<String> val) {
            timeDetails = val;
            return this;
        }

        public DataSetHelperBuilder rowViewBys(ArrayList<String> val) {
            rowViewBys = val;
            return this;
        }

        public DataSetHelperBuilder colViewBys(ArrayList<String> val) {
            colViewBys = val;
            return this;
        }

        public DataSetHelperBuilder orderBys(String val) {
            orderBys = val;
            return this;
        }
    }
}
