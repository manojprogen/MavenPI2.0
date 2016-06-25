/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.graphs.client;

import com.progen.graphs.bean.PbReportGraphsBean;
import java.util.ArrayList;
import org.apache.log4j.Logger;

/**
 * @filename PbReportGraphsManager
 *
 * @author santhosh.kumar@progenbusiness.com @date Jul 22, 2009, 5:21:07 PM
 */
public class PbReportGraphsManager {

    public static Logger logger = Logger.getLogger(PbReportGraphsManager.class);

    public ArrayList getReportGraphsData(int tableId) {
        PbReportGraphsBean bean = new PbReportGraphsBean();
        //ArrayList alist=new ArrayList();
        try {
            return bean.getReportGraphsData(tableId);
        } catch (Exception exp) {
            logger.error("Exception:", exp);
            return new ArrayList();
        }
    }

    public void updateGraphTypes(int grpId, int grpTypeId) {
        PbReportGraphsBean bean = new PbReportGraphsBean();
        try {
            bean.updateGraphTypes(grpId, grpTypeId);
        } catch (Exception exp) {
            logger.error("Exception:", exp);
        }
    }

    public void updateGraphSizes(int grpId, int grpSizeId) {
        PbReportGraphsBean bean = new PbReportGraphsBean();
        try {
            bean.updateGraphSizes(grpId, grpSizeId);
        } catch (Exception exp) {
            logger.error("Exception:", exp);
        }
    }

    public void updateGraphColumns(int grpId, int tableId, String[] grpColNames) {
        PbReportGraphsBean bean = new PbReportGraphsBean();
        try {
            bean.updateGraphColumns(grpId, tableId, grpColNames);
        } catch (Exception exp) {
            logger.error("Exception:", exp);
        }
    }

    //updateGraphColumnsAxis
    //added by santhosh.kumar@progenbusiness.com on 29/07/09  for updating column axis
    public void updateGraphColumnsAxis(int grpId, int tableId, String[] columnKeys, String[] columnAxis) {
        PbReportGraphsBean bean = new PbReportGraphsBean();
        try {
            bean.updateGraphColumnsAxis(grpId, tableId, columnKeys, columnAxis);
        } catch (Exception exp) {
            logger.error("Exception:", exp);
        }
    }
    //added by santhosh.kumar@progenbusiness.com on 30/07/09  for getting column Drill Down Pre requisites for full filling column drill down

    public ArrayList getColumnDrillDownPreReq(String reportId, String tableId, String[] colNames) {
        PbReportGraphsBean bean = new PbReportGraphsBean();
        try {
            return bean.getColumnDrillDownPreReq(reportId, tableId, colNames);
        } catch (Exception exp) {
            logger.error("Exception:", exp);
            return new ArrayList();
        }
    }
    //added by santhosh.kumar@progenbusiness.com on 30/07/09  for updating column Drill Down

    public void updateColumnDrillDown(String[] colNames, String[] reportIds, String tableId) {
        PbReportGraphsBean bean = new PbReportGraphsBean();
        try {
            bean.updateColumnDrillDown(colNames, reportIds, tableId);
        } catch (Exception exp) {
            logger.error("Exception:", exp);
        }
    }
}
