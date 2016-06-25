/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.query;

//import com.progen.log.ProgenLog;
import com.progen.report.PbReportCollection;
import java.util.ArrayList;
import org.apache.log4j.Logger;
import prg.db.PbDb;
import prg.db.PbReturnObject;

/**
 *
 * @author progen
 */
public class KPIDataSetHelper {

    public static Logger logger = Logger.getLogger(KPIDataSetHelper.class);

    public static KPIDataSet buildKPIDataSet(Iterable<String> measIds, ArrayList timeDetails, String userId, String folderId) {
        QueryExecutor queryExecutor = new QueryExecutor();
        PbReturnObject pbro = new PbReturnObject();
        ArrayList<String> rowViewBys = new ArrayList<String>();
        rowViewBys.add("TIME");
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
        collect.isKpi = true;

        PbReportQuery reportQuery = queryExecutor.formulateQuery(collect, userId);
        String query = "";
        try {
            query = reportQuery.generateViewByQry();
        } catch (Exception ex) {
//            ProgenLog.log(ProgenLog.SEVERE, KPIDataSetHelper.class,"buildKPIDataSet","Query Generation Failed");
            logger.error("Query Generation Failed", ex);
        }
        PbReturnObject kpiData = null;
        if (query.length() > 0) {
            kpiData = queryExecutor.executeQuery(collect, query);
        }

        KPIDataSet kpiDataSet = new KPIDataSet(kpiData);
        kpiDataSet.setNoOfDays(reportQuery.getNoOfDays());
//
//        String[] cols = kpiData.getColumnNames();
//        
//        for (int i=0;i<cols.length;i++)
//            System.out.print(cols[i]+"   ");
//        
//        for (int i=0;i<kpiData.getRowCount();i++){
//            for (int j=0;j<cols.length;j++){
//                System.out.print(kpiData.getFieldValueString(i, j)+ "   ");
//            }
//            
//        }

        return kpiDataSet;
    }
}
