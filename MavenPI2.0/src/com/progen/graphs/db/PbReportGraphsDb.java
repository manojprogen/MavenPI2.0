/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.graphs.db;

import java.util.ArrayList;
import java.util.ResourceBundle;
import org.apache.log4j.Logger;
import prg.db.PbDb;
import prg.db.PbReturnObject;

/**
 * @filename PbReportGraphsDb
 *
 * @author santhosh.kumar@progenbusiness.com @date Jul 22, 2009, 5:03:35 PM
 */
public class PbReportGraphsDb extends PbDb {

    public static Logger logger = Logger.getLogger(PbReportGraphsDb.class);
    private PbReportGraphsResourceBundle resBundle = (PbReportGraphsResourceBundle) ResourceBundle.getBundle("com.progen.graphs.db.PbReportGraphsResourceBundle");

    public ArrayList getReportGraphsData(int reportId) {
        ArrayList GraphsData = new ArrayList();
        PbReturnObject pbro = null;
        String getGraphsDataQuery = resBundle.getString("getReportGraphsData");
        String getGraphTypesQuery = resBundle.getString("getReportGraphTypes");
        String getGraphSizesQuery = resBundle.getString("getReportGraphSizes");

        String getGraphHeaderInfoQuery = resBundle.getString("getReportGraphHeaderInfo");
        String getCrossTabReportGraphTypes = resBundle.getString("getCrossTabReportGraphTypes");

        String finalQuery = "";
        Object[] details = null;
        try {
            details = new Object[1];
            details[0] = Integer.valueOf(reportId);
            finalQuery = buildQuery(getGraphsDataQuery, details);

            pbro = execSelectSQL(finalQuery);
            GraphsData.add(pbro);//0

            pbro = null;
            pbro = execSelectSQL(getGraphTypesQuery);
            GraphsData.add(pbro);//1

            pbro = null;
            pbro = execSelectSQL(getGraphSizesQuery);
            GraphsData.add(pbro);//2

            details = new Object[1];
            details[0] = Integer.valueOf(reportId);
            finalQuery = buildQuery(getGraphHeaderInfoQuery, details);
            pbro = execSelectSQL(finalQuery);
            GraphsData.add(pbro);//3

            pbro = null;
            pbro = execSelectSQL(getCrossTabReportGraphTypes);
            GraphsData.add(pbro);//4



            return GraphsData;
        } catch (Exception exception) {
            logger.error("Exception:", exception);
            return GraphsData;
        }
    }

    public void updateGraphSizes(int grpId, int grpSizeId) {
        String updateGraphSizesQuery = resBundle.getString("updateGraphSizes");
        Object[] details = null;
        String finalQuery = "";
        try {
            details = new Object[2];
            details[0] = Integer.valueOf(grpSizeId);
            details[1] = Integer.valueOf(grpId);

            finalQuery = buildQuery(updateGraphSizesQuery, details);
            execModifySQL(finalQuery);

        } catch (Exception exp) {
            logger.error("Exception:", exp);
        }
    }

    public void updateGraphTypes(int grpId, int grpTypeId) {
        String updateGraphTypesQuery = resBundle.getString("updateGraphTypes");
        Object[] details = null;
        String finalQuery = "";
        try {
            details = new Object[3];
            details[0] = Integer.valueOf(grpTypeId);
            details[1] = Integer.valueOf(grpTypeId);
            details[2] = Integer.valueOf(grpId);

            finalQuery = buildQuery(updateGraphTypesQuery, details);
            execModifySQL(finalQuery);

        } catch (Exception exp) {
            logger.error("Exception:", exp);
        }
    }

    public void updateGraphColumns(int grpId, int tableId, String[] grpColNames) {
        String deleteGraphColumnsQuery = resBundle.getString("deleteGraphColumns");
        String getGrpDetailsInfoQuery = resBundle.getString("getGrpDetailsInfo");
        String insertGraphColumnsQuery = resBundle.getString("insertGraphColumns");

        PbReturnObject pbro = null;

        String[] tableColId = null;
        String[] colNames = null;
        String[] dispColNames = null;

        String finalQuery = "";
        Object[] details = null;

        ArrayList queries = new ArrayList();
        try {
            //deleting previous columns from graph details
            details = new Object[2];
            details[0] = Integer.valueOf(grpId);
            details[1] = Integer.valueOf(tableId);

            finalQuery = buildQuery(deleteGraphColumnsQuery, details);
            execModifySQL(finalQuery);

            //building query for retrieving table column info from table details

            for (int j = 0; j < grpColNames.length; j++) {
                if (j != (grpColNames.length - 1)) {
                    getGrpDetailsInfoQuery = getGrpDetailsInfoQuery + "'" + grpColNames[j].toUpperCase() + "',";
                } else {
                    getGrpDetailsInfoQuery = getGrpDetailsInfoQuery + "'" + grpColNames[j].toUpperCase() + "') ORDER BY DECODE(column_name";
                }
            }
            for (int l = 0; l < grpColNames.length; l++) {
                if (l != (grpColNames.length - 1)) {
                    getGrpDetailsInfoQuery = getGrpDetailsInfoQuery + ",'" + grpColNames[l].toUpperCase() + "'," + (l + 1);
                } else {
                    getGrpDetailsInfoQuery = getGrpDetailsInfoQuery + ",'" + grpColNames[l].toUpperCase() + "'," + (l + 1) + ")";
                }
            }
            //retrieving result set for graph details info
            details = new Object[1];
            details[0] = Integer.valueOf(tableId);
            finalQuery = buildQuery(getGrpDetailsInfoQuery, details);

            pbro = execSelectSQL(finalQuery);
            tableColId = new String[pbro.getRowCount()];
            colNames = new String[pbro.getRowCount()];
            dispColNames = new String[pbro.getRowCount()];

            String[] ColumnNames = pbro.getColumnNames();

            for (int i = 0; i < pbro.getRowCount(); i++) {
                tableColId[i] = pbro.getFieldValueString(i, ColumnNames[0]);
                colNames[i] = pbro.getFieldValueString(i, ColumnNames[2]);
                dispColNames[i] = pbro.getFieldValueString(i, ColumnNames[1]);
            }
            //inserting graph columns in graph details

            for (int i = 0; i < tableColId.length; i++) {

                details = new Object[8];
                details[0] = Integer.valueOf(tableId);
                details[1] = Integer.valueOf(grpId);

                details[2] = Integer.valueOf(tableColId[i]);
                details[3] = dispColNames[i];
                details[4] = dispColNames[i];
                details[5] = Integer.valueOf(grpId);
                details[6] = Integer.valueOf(tableId);
                if (i == 0 || i == 1) {
                    details[7] = Integer.valueOf(0);
                } else {
                    details[7] = Integer.valueOf(1);
                }
                finalQuery = buildQuery(insertGraphColumnsQuery, details);

                queries.add(finalQuery);
            }
            boolean flag = executeMultiple(queries);

        } catch (Exception exception) {
            logger.error("Exception:", exception);
        }
    }

    //added by santhosh.kumar@progenbusiness.com on 29/07/09  for updating column axis
    public void updateGraphColumnsAxis(int grpId, int tableId, String[] columnKeys, String[] columnAxis) {
        String updateGraphColumnsAxisQuery = resBundle.getString("updateGraphColumnsAxis");
        Object[] details = null;
        String finalQuery = "";
        ArrayList queriesList = new ArrayList();
        boolean flag = false;
        try {
            for (int i = 0; i < columnKeys.length; i++) {
                details = new Object[5];
                details[0] = columnAxis[i];
                details[1] = Integer.valueOf(grpId);
                details[2] = Integer.valueOf(tableId);
                details[3] = Integer.valueOf(tableId);
                details[4] = columnKeys[i];

                finalQuery = buildQuery(updateGraphColumnsAxisQuery, details);

                queriesList.add(finalQuery);
            }
            flag = executeMultiple(queriesList);
        } catch (Exception exception) {
            logger.error("Exception:", exception);
        }
    }

    public ArrayList getColumnDrillDownPreReq(String reportId, String tableId, String[] colNames) {
        String getColDispNamesQuery = resBundle.getString("getColDispNames");
        String getReportByBizAreasQuery = resBundle.getString("getReportByBizAreas");
        PbReturnObject pbro = null;
        Object[] details = null;
        String finalQuery = "";
        ArrayList ColumnDrillDownPreReq = new ArrayList();

        try {
            for (int i = 0; i < colNames.length; i++) {
                if (i != (colNames.length - 1)) {
                    getColDispNamesQuery = getColDispNamesQuery + "'" + colNames[i].toUpperCase() + "', ";
                } else {
                    getColDispNamesQuery = getColDispNamesQuery + "'" + colNames[i].toUpperCase() + "')and is_view_by_column='N'order by column_seq_number";
                }
            }
            details = new Object[1];
            details[0] = tableId;
            finalQuery = buildQuery(getColDispNamesQuery, details);

            pbro = execSelectSQL(finalQuery);
            ColumnDrillDownPreReq.add(pbro);//3

            pbro = null;
            details = new Object[2];
            details[0] = reportId;
            details[1] = reportId;

            finalQuery = buildQuery(getReportByBizAreasQuery, details);

            pbro = execSelectSQL(finalQuery);
            ColumnDrillDownPreReq.add(pbro);//3

        } catch (Exception exception) {
            logger.error("Exception:", exception);
        }
        return ColumnDrillDownPreReq;
    }

    public void updateColumnDrillDown(String[] colNames, String[] reportIds, String tableId) {
        String updateColumnDrillDownQuery = resBundle.getString("updateColumnDrillDown");
        PbReturnObject pbro = null;
        Object[] details = null;
        String finalQuery = "";
        ArrayList queriesList = new ArrayList();
        boolean flag = false;
        try {
            for (int i = 0; i < colNames.length; i++) {
                details = new Object[4];
                if (reportIds[i].equalsIgnoreCase("")) {
                    details[0] = "";
                } else {
                    details[0] = "Y";
                }
                details[1] = reportIds[i];
                details[2] = Integer.valueOf(tableId);
                details[3] = colNames[i].toUpperCase();

                finalQuery = buildQuery(updateColumnDrillDownQuery, details);

                queriesList.add(finalQuery);
            }
            flag = executeMultiple(queriesList);
        } catch (Exception exception) {
            logger.error("Exception:", exception);
        }
    }

    public static void main(String[] a) {
        try {
            PbReportGraphsDb db = new PbReportGraphsDb();
            ArrayList alist = db.getReportGraphsData(1);

        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }

    }
}
