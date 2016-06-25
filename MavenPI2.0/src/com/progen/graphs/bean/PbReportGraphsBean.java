/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.graphs.bean;

import com.progen.graphs.db.PbReportGraphsDb;
import java.util.ArrayList;
import org.apache.log4j.Logger;

/**
 * @filename PbReportGraphsBean
 *
 * @author santhosh.kumar@progenbusiness.com @date Jul 22, 2009, 5:21:16 PM
 */
public class PbReportGraphsBean {

    public static Logger logger = Logger.getLogger(PbReportGraphsBean.class);

    public ArrayList getReportGraphsData(int tableId) {
        PbReportGraphsDb db = new PbReportGraphsDb();
        try {
            return db.getReportGraphsData(tableId);
        } catch (Exception exp) {
            logger.error("Exception:", exp);
            return new ArrayList();
        }
    }

    public void updateGraphSizes(int grpId, int grpSizeId) {
        PbReportGraphsDb db = new PbReportGraphsDb();
        try {
            db.updateGraphSizes(grpId, grpSizeId);
        } catch (Exception exp) {
            logger.error("Exception:", exp);
        }
    }

    public void updateGraphTypes(int grpId, int grpTypeId) {
        PbReportGraphsDb db = new PbReportGraphsDb();
        try {
            db.updateGraphTypes(grpId, grpTypeId);
        } catch (Exception exp) {
            logger.error("Exception:", exp);
        }
    }

    public void updateGraphColumns(int grpId, int tableId, String[] grpColNames) {
        PbReportGraphsDb db = new PbReportGraphsDb();
        try {
            db.updateGraphColumns(grpId, tableId, grpColNames);
        } catch (Exception exp) {
            logger.error("Exception:", exp);
        }
    }

    //added by santhosh.kumar@progenbusiness.com on 29/07/09  for updating column axis
    public void updateGraphColumnsAxis(int grpId, int tableId, String[] columnKeys, String[] columnAxis) {
        PbReportGraphsDb db = new PbReportGraphsDb();
        try {
            db.updateGraphColumnsAxis(grpId, tableId, columnKeys, columnAxis);
        } catch (Exception exp) {
            logger.error("Exception:", exp);
        }
    }
    //added by santhosh.kumar@progenbusiness.com on 30/07/09  for getting column Drill Down Pre requisites for full filling column drill down

    public ArrayList getColumnDrillDownPreReq(String reportId, String tableId, String[] colNames) {
        PbReportGraphsDb db = new PbReportGraphsDb();
        try {
            return db.getColumnDrillDownPreReq(reportId, tableId, colNames);
        } catch (Exception exp) {
            logger.error("Exception:", exp);
            return new ArrayList();
        }
    }
    //added by santhosh.kumar@progenbusiness.com on 30/07/09  for updating column Drill Down

    public void updateColumnDrillDown(String[] colNames, String[] reportIds, String tableId) {
        PbReportGraphsDb db = new PbReportGraphsDb();
        try {
            db.updateColumnDrillDown(colNames, reportIds, tableId);
        } catch (Exception exp) {
            logger.error("Exception:", exp);
        }
    }
}
