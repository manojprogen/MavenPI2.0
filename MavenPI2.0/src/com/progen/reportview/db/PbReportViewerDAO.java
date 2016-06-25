package com.progen.reportview.db;

import com.google.common.collect.LinkedHashMultimap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.progen.cacheLayer.CacheLayer;
import com.progen.charts.JqplotGraphProperty;
import com.progen.connection.ConnectionDAO;
import com.progen.connection.ConnectionMetadata;
import com.progen.contypes.GetConnectionType;
import com.progen.db.ProgenDataSet;
import com.progen.oneView.bd.OneViewBD;
import com.progen.query.RTMeasureElement;
import com.progen.report.*;
import com.progen.report.query.PbReportQuery;
import com.progen.report.query.QueryExecutor;
import com.progen.reportdesigner.db.ReportTemplateDAO;
import com.progen.reportview.bd.PbReportViewerBD;
import com.progen.scheduler.KPIAlertSchedule;
import com.progen.scheduler.ReportDetails;
import com.progen.scheduler.ReportSchedule;
import com.progen.userlayer.db.UserLayerDAO;
import java.io.*;
import java.lang.reflect.Type;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import oracle.jdbc.OraclePreparedStatement;
import org.apache.log4j.Logger;
import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.Mem;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import prg.business.group.BusinessGroupDAO;
import prg.db.*;
import utils.db.ProgenConnection;

public class PbReportViewerDAO extends PbDb {

    public static Logger logger = Logger.getLogger(PbReportViewerDAO.class);
    private ResourceBundle resourceBundle;
    public int memberId = 0;
    Gson gson = new Gson();
    public static String FilePath = "";

    private ResourceBundle getResourceBundle() {
        if (resourceBundle == null) {
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.ORACLE)) {
                resourceBundle = new PbReportViewerResourceBundle();
            } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                resourceBundle = new PbReportViewerResourceBundleMySql();
            } else {
                resourceBundle = new PbReportViewerResBunSqlServer();
            }
        }
        return resourceBundle;
    }

    public String createCustomizeReport(String oldReportId, String reportName, String reportDesc) {
        ArrayList queries = new ArrayList();
        boolean status1 = false;
        boolean status2 = false;
        boolean status3 = false;
        String CustomReportId = "";

        try {
            CustomReportId = String.valueOf(getSequenceNumber("select PRG_AR_REPORT_MASTER_SEQ.nextval from dual"));


            queries = insertCustomReportMaster(oldReportId, CustomReportId, reportName, reportDesc, queries);
            queries = insertCustomReportDetails(oldReportId, CustomReportId, queries);
            //queries = insertCustomReportParamDetails(oldReportId, CustomReportId, queries);
            insertCustomReportParamDetails(oldReportId, CustomReportId, queries);
            queries = insertCustomReportViewByMaster(oldReportId, CustomReportId, queries);



            status1 = executeMultiple(queries);

            queries = new ArrayList();
            queries = insertCustomReportViewByDetails(oldReportId, CustomReportId, queries);
            queries = insertCustomReportQueryDetails(oldReportId, CustomReportId, queries);


            status2 = executeMultiple(queries);


            queries = new ArrayList();
            queries = insertCustomReportTableMaster(oldReportId, CustomReportId, reportName, queries);
            queries = insertCustomReportGraphMaster(oldReportId, CustomReportId, reportName, queries);

            status3 = executeMultiple(queries);


        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        return CustomReportId;
    }

    public ArrayList insertCustomReportMaster(String oldReportId, String CustomReportId, String reportName, String reportDesc, ArrayList queries) throws Exception {

        String insertCustomReportMasterQuery = getResourceBundle().getString("insertCustomReportMaster");
        Object[] records = null;
        String finalQuery = "";



        records = new Object[3];
        records[0] = CustomReportId;
        records[1] = reportName;
        records[2] = reportDesc;

        finalQuery = buildQuery(insertCustomReportMasterQuery, records);
        queries.add(finalQuery);

        records = null;
        CustomReportId = null;
        finalQuery = null;

        return queries;

    }

    public ArrayList insertCustomReportDetails(String oldReportId, String customReportId, ArrayList queries) throws Exception {
        String insertCustomReportDetailsQuery = getResourceBundle().getString("insertCustomReportDetails");
        String getReportDetailInfoQuery = getResourceBundle().getString("getReportDetailInfo");
        PbReturnObject retObj = null;
        Object[] records = null;
        String finalQuery = "";
        String[] dbColumns = null;

        records = new Object[1];
        records[0] = oldReportId;
        finalQuery = buildQuery(getReportDetailInfoQuery, records);
        retObj = execSelectSQL(finalQuery);

        dbColumns = retObj.getColumnNames();

        for (int dtlIndex = 0; dtlIndex < retObj.getRowCount(); dtlIndex++) {
            records = new Object[3];
            records[0] = customReportId;
            records[1] = retObj.getFieldValueString(dtlIndex, dbColumns[2]);
            records[2] = retObj.getFieldValueString(dtlIndex, dbColumns[3]);

            finalQuery = buildQuery(insertCustomReportDetailsQuery, records);
            queries.add(finalQuery);
        }

        //for cleaning memory
        insertCustomReportDetailsQuery = null;
        getReportDetailInfoQuery = null;
        retObj = null;
        records = null;
        finalQuery = null;
        dbColumns = null;

        return queries;
    }

    public void insertCustomReportParamDetails(String oldReportId, String customReportId, ArrayList queries) throws Exception {
        String getCustomReportParamDetailsInfoQuery = getResourceBundle().getString("getCustomReportParamDetailsInfo");
        String insertCustomReportParamDetailsQuery = getResourceBundle().getString("insertCustomReportParamDetails");
        PbReturnObject retObj = null;
        Object[] records = null;
        String finalQuery = "";
        String[] dbColumns = null;

        records = new Object[1];
        records[0] = oldReportId;
        finalQuery = buildQuery(getCustomReportParamDetailsInfoQuery, records);
        retObj = execSelectSQL(finalQuery);

        dbColumns = retObj.getColumnNames();

        //PARAM_DISP_ID, REPORT_ID, PARAM_DISP_NAME, ELEMENT_ID, DIM_ID, DIM_TAB_ID, BUSS_TABLE, CHILD_ELEMENT_ID ,
        //DISP_SEQ_NO, DISPLAY_TYPE, DEFAULT_VALUE, REL_LEVEL
        //modified by bharathi reddy for clob datatype

        Connection connection = ProgenConnection.getInstance().getConnection();
        OraclePreparedStatement opstmt = null;
        insertCustomReportParamDetailsQuery = "insert into PRG_AR_REPORT_PARAM_DETAILS (PARAM_DISP_ID, REPORT_ID, PARAM_DISP_NAME, ELEMENT_ID, DIM_ID, DIM_TAB_ID, BUSS_TABLE, CHILD_ELEMENT_ID ,"
                + " DISP_SEQ_NO, DISPLAY_TYPE, DEFAULT_VALUE, REL_LEVEL) values (PRG_AR_REPORT_PARAM_DETLS_SEQ.nextval,?,?,?,?,?,?,?,?,?,?,?)";
        for (int paramIndex = 0; paramIndex < retObj.getRowCount(); paramIndex++) {
            /*
             * records = new Object[11]; records[0] = customReportId; records[1]
             * = retObj.getFieldValueString(paramIndex, dbColumns[2]);
             * records[2] = retObj.getFieldValueString(paramIndex,
             * dbColumns[3]); records[3] =
             * retObj.getFieldValueString(paramIndex, dbColumns[4]); records[4]
             * = retObj.getFieldValueString(paramIndex, dbColumns[5]);
             * records[5] = retObj.getFieldValueString(paramIndex,
             * dbColumns[6]); records[6] =
             * retObj.getFieldValueString(paramIndex, dbColumns[7]); records[7]
             * = retObj.getFieldValueString(paramIndex, dbColumns[8]);
             * records[8] = retObj.getFieldValueString(paramIndex,
             * dbColumns[9]); records[9] =
             * retObj.getFieldValueClobString(paramIndex,
             * dbColumns[10]);//changed by bharathi reddy records[10] =
             * retObj.getFieldValueString(paramIndex, dbColumns[11]);
             *
             * finalQuery = buildQuery(insertCustomReportParamDetailsQuery,
             * records);
             */
            opstmt = (OraclePreparedStatement) connection.prepareStatement(insertCustomReportParamDetailsQuery);
            opstmt.setString(1, customReportId);
            opstmt.setString(2, retObj.getFieldValueString(paramIndex, dbColumns[2]));
            opstmt.setString(3, retObj.getFieldValueString(paramIndex, dbColumns[3]));
            opstmt.setString(4, retObj.getFieldValueString(paramIndex, dbColumns[4]));
            opstmt.setString(5, retObj.getFieldValueString(paramIndex, dbColumns[5]));
            opstmt.setString(6, retObj.getFieldValueString(paramIndex, dbColumns[6]));
            opstmt.setString(7, retObj.getFieldValueString(paramIndex, dbColumns[7]));
            opstmt.setString(8, retObj.getFieldValueString(paramIndex, dbColumns[8]));
            opstmt.setString(9, retObj.getFieldValueString(paramIndex, dbColumns[9]));
            opstmt.setString(10, retObj.getFieldValueClobString(paramIndex, "DEFAULT_VALUE"));
            opstmt.setString(11, retObj.getFieldValueString(paramIndex, dbColumns[11]));
            int rows = opstmt.executeUpdate();
            // finalQuery = buildQuery(insertCustomReportParamDetailsQuery, records);
            // queries.add(finalQuery);
        }

        //for cleaning memory
        insertCustomReportParamDetailsQuery = null;
        getCustomReportParamDetailsInfoQuery = null;
        retObj = null;
        records = null;
        finalQuery = null;
        dbColumns = null;
        //by Prabal
        if (connection != null) {
            connection.close();
        }
        // return queries;
    }

    public ArrayList insertReportTimeDimensions(String oldReportId, String customReportId, ArrayList queries) throws Exception {

//        String insertReportTimeDimensionsQuery = getResourceBundle().getString("insertReportTimeDimensions"); // commented unused Variable by prabal
        String getReportTimeDimensionsInfoQuery = getResourceBundle().getString("getReportTimeDimensionsInfo");
        PbReturnObject retObj = null;
        Object[] records = null;
        String finalQuery = "";
//        String[] dbColumns = null;

        records = new Object[1];
        records[0] = oldReportId;
        finalQuery = buildQuery(getReportTimeDimensionsInfoQuery, records);
        retObj = execSelectSQL(finalQuery);

//        dbColumns = retObj.getColumnNames();
//        String oldTimeId = null;
        String customTimeId = null;

        for (int paramIndex = 0; paramIndex < retObj.getRowCount(); paramIndex++) {

            customTimeId = String.valueOf("Select PRG_AR_REPORT_TIME_SEQ.nextval from dual");

            records = new Object[1];
            records[0] = customReportId;


        }

        return queries;
    }

    public ArrayList insertReportTimeDimensionsDetails(String oldReportId, String customReportId, ArrayList queries) throws Exception {

//        String insertReportTimeDimensionsDetailsQuery = getResourceBundle().getString("insertReportTimeDimensionsDetails");
        String getReportTimeDimensionsDetailsInfoQuery = getResourceBundle().getString("getReportTimeDimensionsDetailsInfo");
        PbReturnObject retObj = null;
        Object[] records = null;
        String finalQuery = "";
//        String[] dbColumns = null;

        records = new Object[1];
        records[0] = oldReportId;
        finalQuery = buildQuery(getReportTimeDimensionsDetailsInfoQuery, records);
        retObj = execSelectSQL(finalQuery);

//        dbColumns = retObj.getColumnNames();

        for (int paramIndex = 0; paramIndex < retObj.getRowCount(); paramIndex++) {
            records = new Object[1];
            records[0] = customReportId;
        }

        return queries;

    }

    public ArrayList insertCustomReportViewByMaster(String oldReportId, String customReportId, ArrayList queries) throws Exception {
        String getReportViewByMasterInfoQuery = getResourceBundle().getString("getReportViewByMasterInfo");
        String insertCustomReportViewByMasterQuery = getResourceBundle().getString("insertCustomReportViewByMaster");
        PbReturnObject retObj = null;
        Object[] records = null;
        String finalQuery = "";
        String[] dbColumns = null;

        records = new Object[1];
        records[0] = oldReportId;
        finalQuery = buildQuery(getReportViewByMasterInfoQuery, records);
        retObj = execSelectSQL(finalQuery);

        dbColumns = retObj.getColumnNames();

        //VIEW_BY_SEQ, IS_ROW_EDGE, ROW_SEQ, COL_SEQ, DEFAULT_VALUE
        for (int paramIndex = 0; paramIndex < retObj.getRowCount(); paramIndex++) {
            records = new Object[6];
            records[0] = customReportId;
            records[1] = retObj.getFieldValueString(paramIndex, dbColumns[2]);
            records[2] = retObj.getFieldValueString(paramIndex, dbColumns[3]);
            records[3] = retObj.getFieldValueString(paramIndex, dbColumns[4]);
            records[4] = retObj.getFieldValueString(paramIndex, dbColumns[5]);
            records[5] = retObj.getFieldValueString(paramIndex, dbColumns[6]);

            finalQuery = buildQuery(insertCustomReportViewByMasterQuery, records);
            queries.add(finalQuery);
        }

        //for cleaning memory
        insertCustomReportViewByMasterQuery = null;
        getReportViewByMasterInfoQuery = null;
        retObj = null;
        records = null;
        finalQuery = null;
        dbColumns = null;

        return queries;
    }

    public ArrayList insertCustomReportViewByDetails(String oldReportId, String customReportId, ArrayList queries) throws Exception {

        String getCustomReportViewByDetailsInfoQuery = getResourceBundle().getString("getCustomReportViewByDetailsInfo");
        String insertCustomReportViewByDetailsQuery = getResourceBundle().getString("insertCustomReportViewByDetails");
        PbReturnObject retObj = null;
        Object[] records = null;
        String finalQuery = "";
        String[] dbColumns = null;

        records = new Object[1];
        records[0] = customReportId;
        finalQuery = buildQuery(getCustomReportViewByDetailsInfoQuery, records);

        retObj = execSelectSQL(finalQuery);


        //VIEW_BY_SEQ, IS_ROW_EDGE, ROW_SEQ, COL_SEQ, DEFAULT_VALUE
        for (int paramIndex = 0; paramIndex < retObj.getRowCount(); paramIndex++) {
            records = new Object[3];
            records[0] = retObj.getFieldValueString(paramIndex, dbColumns[1]);
            records[1] = retObj.getFieldValueString(paramIndex, dbColumns[1]);
            records[2] = retObj.getFieldValueString(paramIndex, dbColumns[2]);

            finalQuery = buildQuery(insertCustomReportViewByDetailsQuery, records);
            queries.add(finalQuery);
        }

        //for cleaning memory
        insertCustomReportViewByDetailsQuery = null;
        getCustomReportViewByDetailsInfoQuery = null;
        retObj = null;
        records = null;
        finalQuery = null;
        dbColumns = null;

        return queries;

    }

    public ArrayList insertCustomReportQueryDetails(String oldReportId, String customReportId, ArrayList queries) throws Exception {
        String getCustomReportQueryDetailsInfoQuery = getResourceBundle().getString("getCustomReportQueryDetailsInfo");
        String insertCustomReportQueryDetailsQuery = getResourceBundle().getString("insertCustomReportQueryDetails");
        PbReturnObject retObj = null;
        Object[] records = null;
        String finalQuery = "";
        String[] dbColumns = null;

        records = new Object[1];
        records[0] = oldReportId;
        finalQuery = buildQuery(getCustomReportQueryDetailsInfoQuery, records);
        retObj = execSelectSQL(finalQuery);

        dbColumns = retObj.getColumnNames();

        //VIEW_BY_SEQ, IS_ROW_EDGE, ROW_SEQ, COL_SEQ, DEFAULT_VALUE
        for (int paramIndex = 0; paramIndex < retObj.getRowCount(); paramIndex++) {
            records = new Object[9];
            records[0] = retObj.getFieldValueString(paramIndex, dbColumns[1]);
            records[1] = retObj.getFieldValueString(paramIndex, dbColumns[2]);
            records[2] = retObj.getFieldValueString(paramIndex, dbColumns[3]);
            records[3] = retObj.getFieldValueString(paramIndex, dbColumns[4]);
            records[4] = retObj.getFieldValueString(paramIndex, dbColumns[5]);
            records[5] = retObj.getFieldValueString(paramIndex, dbColumns[6]);
            records[6] = customReportId;
            records[7] = retObj.getFieldValueString(paramIndex, dbColumns[8]);
            records[8] = retObj.getFieldValueString(paramIndex, dbColumns[9]);

            finalQuery = buildQuery(insertCustomReportQueryDetailsQuery, records);
            queries.add(finalQuery);
        }

        //for cleaning memory
        insertCustomReportQueryDetailsQuery = null;
        getCustomReportQueryDetailsInfoQuery = null;
        retObj = null;
        records = null;
        finalQuery = null;
        dbColumns = null;

        return queries;
    }

    public ArrayList insertCustomReportTableMaster(String oldReportId, String customReportId, String reportName, ArrayList queries) throws Exception {
        String getCustomReportTableMasterInfoQuery = getResourceBundle().getString("getCustomReportTableMasterInfo");
        String insertCustomReportTableMasterQuery = getResourceBundle().getString("insertCustomReportTableMaster");
        String customReportTableId = null;
        PbReturnObject retObj = null;
        Object[] records = null;
        String finalQuery = "";
        String[] dbColumns = null;

        records = new Object[1];
        records[0] = oldReportId;
        finalQuery = buildQuery(getCustomReportTableMasterInfoQuery, records);
        retObj = execSelectSQL(finalQuery);

        dbColumns = retObj.getColumnNames();

        //VIEW_BY_SEQ, IS_ROW_EDGE, ROW_SEQ, COL_SEQ, DEFAULT_VALUE
        for (int paramIndex = 0; paramIndex < retObj.getRowCount(); paramIndex++) {
            customReportTableId = String.valueOf(getSequenceNumber("select PRG_AR_REPORT_TABLE_MASTER_SEQ.nextval from dual"));

            records = new Object[5];
            records[0] = customReportTableId;
            records[1] = customReportId;
            records[2] = reportName;
            records[3] = retObj.getFieldValueString(paramIndex, dbColumns[3]);
            records[4] = retObj.getFieldValueString(paramIndex, dbColumns[4]);


            finalQuery = buildQuery(insertCustomReportTableMasterQuery, records);
            queries.add(finalQuery);

            queries = insertCustomReportTableDetails(oldReportId, customReportId, customReportTableId, reportName, queries);
        }

        //for cleaning memory
        insertCustomReportTableMasterQuery = null;
        getCustomReportTableMasterInfoQuery = null;
        retObj = null;
        records = null;
        finalQuery = null;
        dbColumns = null;

        return queries;
    }

    public ArrayList insertCustomReportTableDetails(String oldReportId, String customReportId, String customReportTableId, String reportName, ArrayList queries) throws Exception {

        String getCustomReportTableDetailsInfoQuery = getResourceBundle().getString("getCustomReportTableDetailsInfo");

        //String getCustomReportTableDetailsInfo1Query = getResourceBundle().getString("getCustomReportTableDetailsInfo1");
        //String getCustomReportTableDetailsInfo2Query = getResourceBundle().getString("getCustomReportTableDetailsInfo2");
        String insertCustomReportTableDetailsQuery = getResourceBundle().getString("insertCustomReportTableDetails");
        String updateCustomReportTableDetailsQuery = getResourceBundle().getString("updateCustomReportTableDetails");

        PbReturnObject retObj = null;
        Object[] records = null;
        String finalQuery = "";
        String[] dbColumns = null;

        records = new Object[1];
        records[0] = oldReportId;
        // records[1] = customReportId;
        finalQuery = buildQuery(getCustomReportTableDetailsInfoQuery, records);
        retObj = execSelectSQL(finalQuery);

        dbColumns = retObj.getColumnNames();


        for (int paramIndex = 0; paramIndex < retObj.getRowCount(); paramIndex++) {

            records = new Object[12];
            records[0] = customReportId;
            records[1] = customReportTableId;
            records[2] = retObj.getFieldValueString(paramIndex, dbColumns[3]);
            records[3] = retObj.getFieldValueString(paramIndex, dbColumns[4]);
            records[4] = retObj.getFieldValueString(paramIndex, dbColumns[5]);
            records[5] = retObj.getFieldValueString(paramIndex, dbColumns[6]);
            records[6] = retObj.getFieldValueString(paramIndex, dbColumns[7]);
            records[7] = retObj.getFieldValueString(paramIndex, dbColumns[8]);
            records[8] = retObj.getFieldValueString(paramIndex, dbColumns[9]);
            records[9] = retObj.getFieldValueString(paramIndex, dbColumns[10]);
            records[10] = retObj.getFieldValueString(paramIndex, dbColumns[11]);
            records[11] = retObj.getFieldValueString(paramIndex, dbColumns[12]);

            finalQuery = buildQuery(insertCustomReportTableDetailsQuery, records);
            queries.add(finalQuery);


            records = new Object[3];
            records[0] = retObj.getFieldValueString(paramIndex, dbColumns[12]);
            records[1] = oldReportId;
            records[2] = customReportTableId;

            finalQuery = buildQuery(updateCustomReportTableDetailsQuery, records);
            queries.add(finalQuery);

        }

        /*
         * records = new Object[1]; records[0] = oldReportId; finalQuery =
         * buildQuery(getCustomReportTableDetailsInfo2Query, records); retObj =
         * execSelectSQL(finalQuery);
         *
         * for (int paramIndex = 0; paramIndex < retObj.getRowCount();
         * paramIndex++) {
         *
         * records = new Object[6]; records[0] =
         * retObj.getFieldValueString(paramIndex, dbColumns[0]); records[1] =
         * retObj.getFieldValueString(paramIndex, dbColumns[1]); records[2] =
         * retObj.getFieldValueString(paramIndex, dbColumns[2]); records[3] =
         * retObj.getFieldValueString(paramIndex, dbColumns[3]); records[4] =
         * retObj.getFieldValueString(paramIndex, dbColumns[4]); records[5] =
         * retObj.getFieldValueString(paramIndex, dbColumns[5]);
         *
         * finalQuery = buildQuery(updateCustomReportTableDetailsQuery,
         * records); queries.add(finalQuery); }
         *
         *
         */


        //for cleaning memory
        getCustomReportTableDetailsInfoQuery = null;
        insertCustomReportTableDetailsQuery = null;
        //getCustomReportTableDetailsInfo1Query = null;
        updateCustomReportTableDetailsQuery = null;
        //getCustomReportTableDetailsInfo2Query = null;
        retObj = null;
        records = null;
        finalQuery = null;
        dbColumns = null;

        return queries;
    }

    public ArrayList insertCustomReportGraphMaster(String oldReportId, String customReportId, String reportName, ArrayList queries) throws Exception {
        String getCustomReportGraphMasterInfoQuery = getResourceBundle().getString("getCustomReportGraphMasterInfo");
        String insertCustomReportGraphMasterQuery = getResourceBundle().getString("insertCustomReportGraphMaster");
        PbReturnObject retObj = null;
        Object[] records = null;
        String finalQuery = "";
        String[] dbColumns = null;
        String customGraphId = null;
        String oldGraphId = null;
        records = new Object[1];
        records[0] = oldReportId;
        finalQuery = buildQuery(getCustomReportGraphMasterInfoQuery, records);

        retObj = execSelectSQL(finalQuery);

        dbColumns = retObj.getColumnNames();

        //GRAPH_ID, REPORT_ID, GRAPH_NAME, GRAPH_FAMILY, GRAPH_TYPE, GRAPH_ORDER, GRAPH_SIZE, GRAPH_HEIGHT, GRAPH_WIDTH,ALLOW_LINK, ALLOW_LABEL,
        //ALLOW_LEGEND, ALLOW_TOOLTIP, GRAPH_CLASS, LEFT_Y-AXIS_LABEL, RIGHT_Y_AXIS_LABEL, X_AXIS_LABEL, FONT_NAME, FONT_SIZE, FONT_COLOR
        for (int paramIndex = 0; paramIndex < retObj.getRowCount(); paramIndex++) {
            customGraphId = String.valueOf(getSequenceNumber("select PRG_AR_GRAPH_MASTER_SEQ.nextval from dual"));

            oldGraphId = retObj.getFieldValueString(paramIndex, dbColumns[0]);

            records = new Object[20];
            records[0] = customGraphId;
            records[1] = customReportId;
            records[2] = reportName;
            records[3] = retObj.getFieldValueString(paramIndex, dbColumns[3]);
            records[4] = retObj.getFieldValueString(paramIndex, dbColumns[4]);
            records[5] = retObj.getFieldValueString(paramIndex, dbColumns[5]);
            records[6] = retObj.getFieldValueString(paramIndex, dbColumns[6]);
            records[7] = retObj.getFieldValueString(paramIndex, dbColumns[7]);
            records[8] = retObj.getFieldValueString(paramIndex, dbColumns[8]);
            records[9] = retObj.getFieldValueString(paramIndex, dbColumns[9]);
            records[10] = retObj.getFieldValueString(paramIndex, dbColumns[10]);
            records[11] = retObj.getFieldValueString(paramIndex, dbColumns[11]);
            records[12] = retObj.getFieldValueString(paramIndex, dbColumns[12]);
            records[13] = retObj.getFieldValueString(paramIndex, dbColumns[13]);
            records[14] = retObj.getFieldValueString(paramIndex, dbColumns[14]);
            records[15] = retObj.getFieldValueString(paramIndex, dbColumns[15]);
            records[16] = retObj.getFieldValueString(paramIndex, dbColumns[16]);
            records[17] = retObj.getFieldValueString(paramIndex, dbColumns[17]);
            records[18] = retObj.getFieldValueString(paramIndex, dbColumns[18]);
            records[19] = retObj.getFieldValueString(paramIndex, dbColumns[19]);

            finalQuery = buildQuery(insertCustomReportGraphMasterQuery, records);
            queries.add(finalQuery);

            queries = insertCustomReportGraphDetails(customGraphId, oldGraphId, customReportId, oldReportId, queries);
        }

        //for cleaning memory
        insertCustomReportGraphMasterQuery = null;
        getCustomReportGraphMasterInfoQuery = null;
        retObj = null;
        records = null;
        finalQuery = null;
        dbColumns = null;

        return queries;
    }

    private ArrayList insertCustomReportGraphDetails(String customGraphId, String oldGraphId, String customReportId, String oldReportId, ArrayList queries) throws Exception {

        String getCustomReportGraphDetailsInfoQuery = getResourceBundle().getString("getCustomReportGraphDetailsInfo");
        String insertCustomReportGraphDetailsQuery = getResourceBundle().getString("insertCustomReportGraphDetails");
        String updateCustomReportGraphDetailsQuery = getResourceBundle().getString("updateCustomReportGraphDetails");
        PbReturnObject retObj = null;
        Object[] records = null;
        String finalQuery = "";
        String[] dbColumns = null;

        String[] qryElementIds = null;



        records = new Object[1];
        records[0] = oldGraphId;
        finalQuery = buildQuery(getCustomReportGraphDetailsInfoQuery, records);
        retObj = execSelectSQL(finalQuery);

        dbColumns = retObj.getColumnNames();
        //GRAPH_COL_ID, GRAPH_ID, COL_NAME, ELEMENT_ID, COLUMN_ORDER, QUERY_COL_ID, AXIS
        qryElementIds = new String[retObj.getRowCount()];

        for (int paramIndex = 0; paramIndex < retObj.getRowCount(); paramIndex++) {
            records = new Object[6];

            records[0] = customGraphId;
            records[1] = retObj.getFieldValueString(paramIndex, dbColumns[2]);
            records[2] = retObj.getFieldValueString(paramIndex, dbColumns[3]);
            records[3] = retObj.getFieldValueString(paramIndex, dbColumns[4]);
            records[4] = retObj.getFieldValueString(paramIndex, dbColumns[5]);
            records[5] = retObj.getFieldValueString(paramIndex, dbColumns[6]);

            qryElementIds[paramIndex] = retObj.getFieldValueString(paramIndex, dbColumns[3]);

            finalQuery = buildQuery(insertCustomReportGraphDetailsQuery, records);
            queries.add(finalQuery);

            records = new Object[3];
            records[0] = retObj.getFieldValueString(paramIndex, dbColumns[3]);
            records[1] = customReportId;
            records[2] = customGraphId;

            finalQuery = buildQuery(updateCustomReportGraphDetailsQuery, records);
            queries.add(finalQuery);
        }


        return queries;
    }

    //new Customize code starts from here
    public String getCustomReportId() throws Exception {
        String CustomReportId = "";
        int reportId;
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
            reportId = getSequenceNumber("select IDENT_CURRENT('PRG_AR_REPORT_MASTER') NEXTVAL ");
            reportId++;
            CustomReportId = String.valueOf(reportId);

        } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
            reportId = getSequenceNumber("select LAST_INSERT_ID(REPORT_ID) from PRG_AR_REPORT_MASTER ");
            reportId = reportId + 1;
            CustomReportId = String.valueOf(reportId);
        } else {
            CustomReportId = String.valueOf(getSequenceNumber("select PRG_AR_REPORT_MASTER_SEQ.nextval from dual"));
        }

        return CustomReportId;
    }

    public Container customizeReport(Container container, String customReportId, String reportId, HttpServletRequest request) {
        HashMap ParametersHashMap = null;
        ParametersHashMap = container.getParametersHashMap();
        try {
            ParametersHashMap = getReportUserFolders(reportId, ParametersHashMap);
        } catch (Exception exp) {
            logger.error("Exception:", exp);
        }
        return container;

    }

    public HashMap getReportUserFolders(String reportId, HashMap ParametersHashMap) {
        ReportTemplateDAO templateDAo = new ReportTemplateDAO();
        String folders = templateDAo.getReportUserFolders(reportId);

        ParametersHashMap.put("UserFolderIds", folders);

        return ParametersHashMap;
    }

    public HashMap getReportTimeParams(String reportId, HashMap ParametersHashMap) throws Exception {
        String getReportTimeParamsQuery = getResourceBundle().getString("getReportTimeParams");
        Object[] data = new Object[1];
        data[0] = reportId;
        PbReturnObject retObj = null;
        ArrayList ReportTimeParams = new ArrayList();
        ArrayList ReportTimeParamsNames = new ArrayList();
        String finalQuery = null;
        String[] dbColumns = null;

        finalQuery = buildQuery(getReportTimeParamsQuery, data);
        retObj = execSelectSQL(finalQuery);
        dbColumns = retObj.getColumnNames();

        for (int index = 0; index < retObj.getRowCount(); index++) {

            ReportTimeParams.add(retObj.getFieldValueString(index, dbColumns[0]));
            ReportTimeParamsNames.add(retObj.getFieldValueString(index, dbColumns[1]));
        }
        ParametersHashMap.put("TimeParameters", ReportTimeParams);
        ParametersHashMap.put("TimeParametersNames", ReportTimeParamsNames);

        return ParametersHashMap;


    }

    public HashMap getReportParams(String reportId, HashMap ParametersHashMap) throws Exception {

        String getReportParamsQuery = getResourceBundle().getString("getReportParams");
        Object[] data = new Object[1];
        data[0] = reportId;
        PbReturnObject retObj = null;

        String finalQuery = null;
        String[] dbColumns = null;

        ArrayList ReportParamsList = new ArrayList();
        ArrayList ReportParamsNamesList = new ArrayList();

        finalQuery = buildQuery(getReportParamsQuery, data);
        retObj = execSelectSQL(finalQuery);
        dbColumns = retObj.getColumnNames();

        for (int index = 0; index < retObj.getRowCount(); index++) {
            ReportParamsList.add(retObj.getFieldValueString(index, dbColumns[0]));
            ReportParamsNamesList.add(retObj.getFieldValueString(index, dbColumns[1]));

        }
        ParametersHashMap.put("Parameters", ReportParamsList);
        ParametersHashMap.put("ParametersNames", ReportParamsNamesList);

        return ParametersHashMap;
    }

    public HashMap getReportViewBys(String reportId, HashMap TableHashMap) throws Exception {

        String getReportViewBysQuery = getResourceBundle().getString("getReportViewBys");
        Object[] data = new Object[1];
        data[0] = reportId;
        PbReturnObject retObj = null;

        String finalQuery = null;
        String[] dbColumns = null;

        ArrayList REPList = null;
        ArrayList CEPList = null;

        ArrayList REPNamesList = null;
        ArrayList CEPNamesList = null;

        finalQuery = buildQuery(getReportViewBysQuery, data);
        retObj = execSelectSQL(finalQuery);
        dbColumns = retObj.getColumnNames();

        for (int index = 0; index < retObj.getRowCount(); index++) {
            if (retObj.getFieldValueString(index, dbColumns[1]).equalsIgnoreCase("-1")) {
                if (CEPList == null) {
                    CEPList = new ArrayList();
                    CEPNamesList = new ArrayList();
                }
                CEPList.add(retObj.getFieldValueString(index, dbColumns[0]));
                CEPNamesList.add(retObj.getFieldValueString(index, dbColumns[3]));

            } else {
                if (REPList == null) {
                    REPList = new ArrayList();
                    REPNamesList = new ArrayList();
                }
                REPList.add(retObj.getFieldValueString(index, dbColumns[0]));
                REPNamesList.add(retObj.getFieldValueString(index, dbColumns[3]));
            }
        }
        TableHashMap.put("REP", REPList);
        TableHashMap.put("CEP", CEPList);
        TableHashMap.put("REPNames", REPNamesList);
        TableHashMap.put("CEPNames", CEPNamesList);

        return TableHashMap;
    }

    public HashMap getReportMeasures(String reportId, HashMap TableHashMap) throws Exception {

        String getReportMeasuresQuery = getResourceBundle().getString("getReportMeasures");
        Object[] data = new Object[1];
        data[0] = reportId;
        PbReturnObject retObj = null;
        String finalQuery = null;
        String[] dbColumns = null;

        ArrayList MeasuresList = new ArrayList();
        ArrayList MeasuresNamesList = new ArrayList();

        finalQuery = buildQuery(getReportMeasuresQuery, data);
        retObj = execSelectSQL(finalQuery);
        dbColumns = retObj.getColumnNames();

        for (int index = 0; index < retObj.getRowCount(); index++) {
            MeasuresList.add(retObj.getFieldValueString(index, dbColumns[0]));
            MeasuresNamesList.add(retObj.getFieldValueString(index, dbColumns[1]));

        }
        TableHashMap.put("Measures", MeasuresList);
        TableHashMap.put("MeasuresNames", MeasuresNamesList);

        return TableHashMap;
    }

    /*
     * public HashMap getReportGraphHeader(String reportId, HashMap
     * GraphHashMap, HashMap ReportHashMap, HashMap TableHashMap) throws
     * Exception {
     *
     * String getReportGraphHeaderQuery =
     * getResourceBundle().getString("getReportGraphHeader"); Object[] data =
     * new Object[1]; data[0] = reportId; PbReturnObject retObj = null; String
     * finalQuery = null; String[] dbColumns = null; HashMap GraphDetails =
     * null; String[] viewByElementIds = null; String grpIds = "";
     *
     * ArrayList AllGraphColumns = new ArrayList();
     *
     * String REP = String.valueOf(TableHashMap.get("REP")); viewByElementIds =
     * REP.split(",");
     *
     * String reportName = String.valueOf(ReportHashMap.get("ReportName"));
     *
     * finalQuery = buildQuery(getReportGraphHeaderQuery, data); retObj =
     * execSelectSQL(finalQuery); dbColumns = retObj.getColumnNames();
     *
     * for (int index = 0; index < retObj.getRowCount(); index++) { GraphDetails
     * = new HashMap();
     *
     * grpIds = grpIds + "," + (index + 1); GraphDetails.put("graphId", (index +
     * 1)); GraphDetails.put("graphName", reportName + (index + 1));
     * GraphDetails.put("graphClassName", retObj.getFieldValueString(index,
     * dbColumns[2])); GraphDetails.put("graphTypeName",
     * retObj.getFieldValueString(index, dbColumns[3]));
     * GraphDetails.put("viewByElementIds", viewByElementIds);
     * GraphDetails.put("grpSize", retObj.getFieldValueString(index,
     * dbColumns[4])); GraphDetails.put("graphWidth",
     * retObj.getFieldValueString(index, dbColumns[5]));
     * GraphDetails.put("graphHeight", retObj.getFieldValueString(index,
     * dbColumns[6]));
     *
     * GraphDetails = getReportGraphDetails(retObj.getFieldValueString(index,
     * dbColumns[0]), GraphHashMap, ReportHashMap, TableHashMap, GraphDetails,
     * AllGraphColumns);
     *
     * GraphHashMap.put(String.valueOf((index + 1)), GraphDetails); } if
     * (!(grpIds.equalsIgnoreCase(""))) { grpIds = grpIds.substring(1); }
     *
     * GraphHashMap.put("graphIds", grpIds); GraphHashMap.put("AllGraphColumns",
     * AllGraphColumns);
     *
     * return GraphHashMap;
    }
     */
    public HashMap getReportGraphDetails(String graphId, HashMap GraphHashMap, HashMap ReportHashMap, HashMap TableHashMap, HashMap GraphDetails, ArrayList AllGraphColumns) throws Exception {

        String[] barChartColumnNames = null;
        String[] pieChartColumns = null;
        String[] barChartColumnTitles = null;
        String[] axis = null;

        String[] viewByElementIds = null;
        String[] viewBys = null;
        String[] REP = (String[]) ((ArrayList) TableHashMap.get("REP")).toArray(new String[0]);

        //Measures of the grpah
        //LinkedHashMap stores the Element Id as Key and Col Name as Value
        LinkedHashMap<String, String> grphMeasMap = new LinkedHashMap<String, String>();
        String measId;
        String measDispName;

        viewBys = REP;
        viewByElementIds = REP;

        String getReportGraphDetailsQuery = getResourceBundle().getString("getReportGraphDetails");
        String finalQuery = null;
        String[] dbColumns = null;
        PbReturnObject retObj = null;
        Object[] data = new Object[1];
        data[0] = graphId;

        finalQuery = buildQuery(getReportGraphDetailsQuery, data);

        retObj = execSelectSQL(finalQuery);
        dbColumns = retObj.getColumnNames();

        barChartColumnNames = new String[viewBys.length + retObj.getRowCount()];
        pieChartColumns = new String[barChartColumnNames.length];
        barChartColumnTitles = new String[barChartColumnNames.length];
        axis = new String[barChartColumnNames.length];

        for (int index = 0; index < viewBys.length; index++) {
            if (viewBys[index].contains("A_")) {
                viewByElementIds[index] = viewBys[index];
                barChartColumnNames[index] = viewBys[index];
                pieChartColumns[index] = viewBys[index];
                barChartColumnTitles[index] = viewBys[index];
            } else {
                viewByElementIds[index] = "A_" + viewBys[index];
                barChartColumnNames[index] = "A_" + viewBys[index];
                pieChartColumns[index] = "A_" + viewBys[index];
                barChartColumnTitles[index] = "A_" + viewBys[index];
            }
            axis[index] = "0";
        }

        for (int index = viewBys.length; index < barChartColumnNames.length; index++) {

            barChartColumnNames[index] = "A_" + retObj.getFieldValueString((index - viewBys.length), dbColumns[0]);
            pieChartColumns[index] = "A_" + retObj.getFieldValueString((index - viewBys.length), dbColumns[0]);
            barChartColumnTitles[index] = retObj.getFieldValueString((index - viewBys.length), dbColumns[1]);
            axis[index] = retObj.getFieldValueString((index - viewBys.length), dbColumns[2]);



            if (!AllGraphColumns.contains(retObj.getFieldValueString((index - viewBys.length), dbColumns[0]))) {
                AllGraphColumns.add(retObj.getFieldValueString((index - viewBys.length), dbColumns[0]));
            }
            measId = retObj.getFieldValueString((index - viewBys.length), dbColumns[0]);
            measDispName = retObj.getFieldValueString((index - viewBys.length), dbColumns[1]);
            grphMeasMap.put(measId, measDispName);
        }


        GraphDetails.put("barChartColumnNames", barChartColumnNames);
        GraphDetails.put("pieChartColumns", barChartColumnNames);
        GraphDetails.put("barChartColumnTitles", barChartColumnTitles);
        GraphDetails.put("viewByElementIds", viewByElementIds);
        GraphDetails.put("axis", axis);
        GraphDetails.put("graphMeasures", grphMeasMap);

        return GraphDetails;
    }

    public String buildGraphColumns(String[] viewByElementIds, String[] barChartColumnNames, String[] barChartColumnTitles) {
        StringBuffer graphBuffer = new StringBuffer("");

        if (viewByElementIds != null && barChartColumnNames != null && barChartColumnTitles != null) {
            for (int i = viewByElementIds.length; i < barChartColumnNames.length; i++) {
                if (barChartColumnNames[i] != null && barChartColumnTitles[i] != null) {
                    graphBuffer.append(" <li id='GrpCol" + barChartColumnNames[i].replace("A_", "") + "' style='width:auto;height:auto;color:white' class='navtitle-hover'>");
                    graphBuffer.append("<table id='GrpTab" + barChartColumnNames[i].replace("A_", "") + "'>");
                    graphBuffer.append(" <tr>");
                    graphBuffer.append(" <td >");
                    graphBuffer.append("  <a href=\"javascript:deleteColumn('GrpCol" + barChartColumnNames[i].replace("A_", "") + "')\" class=\"ui-icon ui-icon-close\"></a>");
                    graphBuffer.append("</td>");
                    graphBuffer.append("<td id=\"eleName" + barChartColumnNames[i].replace("A_", "") + "\" style=\"color:black\">" + barChartColumnTitles[i] + "</td>");
                    graphBuffer.append("</tr>");
                    graphBuffer.append("</table>");
                    graphBuffer.append("</li>");
                }
            }
        }
        return graphBuffer.toString();
    }

    public String buildChangeViewBy(ArrayList<String> viewById, ArrayList<String> viewByNames, String dropTar, String ctxPath) {
        StringBuffer graphBuffer = new StringBuffer("");
        if (viewById != null && viewByNames != null) {
            for (int i = 0; i < viewById.size(); i++) {
                graphBuffer.append(" <li id='ViewBy" + viewById.get(i) + "' style='width:auto;height:18px;color:white' >");
                graphBuffer.append("<table id='viewTab" + viewById.get(i) + "'>");
                graphBuffer.append(" <tr>");
                graphBuffer.append(" <td >");
                graphBuffer.append("<img src=\"" + ctxPath + "/icons pinvoke/cross-small.png\" style=\"cursor:pointer\" onclick=\"javascript:deleteColumn('ViewBy" + viewById.get(i) + "','" + dropTar + "','" + viewByNames.get(i) + "')\" />");
                graphBuffer.append("</td>");
                graphBuffer.append("<td class=\"gFontFamily gFontSize12\" style=\"color:black\">" + viewByNames.get(i) + "</td>");
                graphBuffer.append("</tr>");
                graphBuffer.append("</table>");
                graphBuffer.append("</li>");
            }
        }
        return graphBuffer.toString();
    }
    //Added By Ram For Measure As ViewBys

    public String buildChangeMeasureViewBy(ArrayList<String> viewById, ArrayList<String> viewByNames, ArrayList<String> MeasureAsViewBysList, String dropTar, String ctxPath) {
        StringBuffer graphBuffer = new StringBuffer("");

        if (viewById != null && viewByNames != null) {
            for (int i = 0; i < viewById.size(); i++) {
                String display = "none";
                if (MeasureAsViewBysList.contains(viewById.get(i))) {
                    display = "";
                }
                graphBuffer.append(" <li id='" + viewById.get(i) + "'  style='width:auto;height:18px;color:white;display:" + display + "' >");
                graphBuffer.append("<table id='viewTab" + viewById.get(i) + "'>");
                graphBuffer.append(" <tr>");
                graphBuffer.append(" <td >");
                graphBuffer.append("<img src=\"" + ctxPath + "/icons pinvoke/cross-small.png\" style=\"cursor:pointer\" onclick=\"javascript:deleteColumn('ViewBy" + viewById.get(i) + "','" + dropTar + "','" + viewByNames.get(i) + "')\" />");
                graphBuffer.append("</td>");
                graphBuffer.append("<td style=\"color:black\">" + viewByNames.get(i) + "</td>");
                graphBuffer.append("</tr>");
                graphBuffer.append("</table>");
                graphBuffer.append("</li>");
            }
        }
        return graphBuffer.toString();
    }

    public String buildDragableViewBy(String reportId, String dropTar, String ctxPath, Container container, String chartId, String CheckViewbyflag) {
        PbReportCollection collect = container.getReportCollect();
//        String folderName = collect.getReportBizRoleName(collect.reportBizRoles[0]);

        XtendReportMeta reportMeta = new XtendReportMeta();
//        FileReadWrite readWrite = new FileReadWrite();
//        String metaString = readWrite.loadJSON("/usr/local/cache/"+folderName.replaceAll("\\W", "").trim()+"/"+collect.reportName.replaceAll("\\W", "").trim()+"_"+collect.reportId+"/"+collect.reportName.replaceAll("\\W", "").trim()+"_"+collect.reportId+"_data.json");
//         Type tarType=new TypeToken<XtendReportMeta>() {}.getType();
//         Gson gson = new Gson();
//        if(CheckViewbyflag.equalsIgnoreCase("Graph")){
//         reportMeta = container.getReportMeta();//gson.fromJson(metaString,tarType);
//        }else if(CheckViewbyflag.equalsIgnoreCase("Trend")) {
//            reportMeta = container.getTrendReportMeta();
//        }else {
        reportMeta = container.getReportMeta();
//        }

        StringBuffer graphBuffer = new StringBuffer("");
//        ArrayList viewById = new ArrayList();
//        ArrayList viewByNames = new ArrayList();
//                    viewById.add("TIME");
//                    viewByNames.add("Time");
        if (reportMeta != null && reportMeta.getChartData() != null) {
            Map<String, DashboardChartData> chartData = new LinkedHashMap<String, DashboardChartData>(reportMeta.getChartData());

            List<String> charts = new ArrayList(chartData.keySet());

            int countOfviewbys = charts.size();
            container.setCountOfviewbys(countOfviewbys); //added by Bhargavi for Chart count

            if (chartId.equalsIgnoreCase("Local")) {
                for (String chart : charts) {
                    DashboardChartData chartDetails = reportMeta.getChartData().get(chart);
                    if (chartDetails != null && chartDetails.getViewIds() != null) {
                        graphBuffer.append(" <li id='ViewBy" + chartDetails.getViewIds().get(0) + "' style='width:auto;height:auto;color:white' >");
                        graphBuffer.append("<table id='viewTab" + chartDetails.getViewIds().get(0) + "'>");
                        graphBuffer.append(" <tr>");
                        graphBuffer.append(" <td >");
                        graphBuffer.append("<img src=\"" + ctxPath + "/icons pinvoke/cross-small.png\" style=\"cursor:pointer\" onclick=\"javascript:deleteColumn('ViewBy" + chartDetails.getViewIds().get(0) + "','" + dropTar + "','" + chartDetails.getViewBys().get(0) + "')\" />");
                        graphBuffer.append("</td>");
                        graphBuffer.append("<td class=\"gFontFamily gFontSize12\" style=\"color:black\">" + chartDetails.getViewBys().get(0) + "</td>");
                        graphBuffer.append("</tr>");
                        graphBuffer.append("</table>");
                        graphBuffer.append("</li>");
                    }
                }
            } else if (chartId.equalsIgnoreCase("paramsequnce")) {
                for (int i = 0; i < reportMeta.getViewbysIds().size(); i++) {
                    if (reportMeta != null && reportMeta.getViewbysIds() != null) {
                        graphBuffer.append(" <li id=" + i + " style='width:auto;height:auto;color:white' >");
                        graphBuffer.append("<table id=" + i + ">");
                        graphBuffer.append(" <tr>");
//                graphBuffer.append(" <td >");
//                graphBuffer.append("<img src=\""+ctxPath+"/icons pinvoke/cross-small.png\" style=\"cursor:pointer\" onclick=\"javascript:deleteColumn('ViewBy" + chartDetails.getViewIds().get(i) + "','"+dropTar+"','"+chartDetails.getViewBys().get(i)+"')\" />");
//                graphBuffer.append("</td>");
                        graphBuffer.append("<td class=\"gFontFamily gFontSize12\" style=\"color:black\">" + reportMeta.getViewbys().get(i) + "</td>");
                        graphBuffer.append("</tr>");
                        graphBuffer.append("</table>");
                        graphBuffer.append("</li>");
                    }
                }
            } else {
                DashboardChartData chartDetails = reportMeta.getChartData().get(chartId);
                if (chartDetails.getDimensions() != null && chartDetails.getDimensions().size() > 0) {
                    String view = "";
                    view = chartDetails.getViewBys().get(0);
                    String viewId = "";
                    viewId = chartDetails.getViewIds().get(0);
                    if (chartDetails.getChartType().equalsIgnoreCase("Combo-IndiaCity") || chartDetails.getChartType().equalsIgnoreCase("Combo-India-Map")||chartDetails.getChartType().equalsIgnoreCase("Combo-US-Map")||chartDetails.getChartType().equalsIgnoreCase("Combo-USCity-Map")|| chartDetails.getChartType().equalsIgnoreCase("Combo-Aus-State")||chartDetails.getChartType().equalsIgnoreCase("Combo-Aus-City")) { 
                    } else {
                        if (chartDetails != null && chartDetails.getViewIds() != null) {
                            graphBuffer.append(" <li id='ViewBy" + chartDetails.getViewIds().get(0) + "' style='width:auto;height:auto;color:white' >");
                            graphBuffer.append("<table id='viewTab" + chartDetails.getViewIds().get(0) + "'>");
                            graphBuffer.append(" <tr>");
                            graphBuffer.append(" <td >");
                            graphBuffer.append("<img src=\"" + ctxPath + "/icons pinvoke/cross-small.png\" style=\"cursor:pointer\" onclick=\"javascript:deleteColumn('ViewBy" + chartDetails.getViewIds().get(0) + "','" + dropTar + "','" + view + "')\" />");
                            graphBuffer.append("</td>");
                            graphBuffer.append("<td class=\"gFontFamily gFontSize12\" style=\"color:black\">" + view + "</td>");
                            graphBuffer.append("</tr>");
                            graphBuffer.append("</table>");
                            graphBuffer.append("</li>");
                        }
                    }
                    for (int i = 0; i < chartDetails.getDimensions().size(); i++) {
                        if (chartDetails.getChartType().equalsIgnoreCase("Combo-IndiaCity")|| chartDetails.getChartType().equalsIgnoreCase("Combo-India-Map")||chartDetails.getChartType().equalsIgnoreCase("Combo-US-Map")||chartDetails.getChartType().equalsIgnoreCase("Combo-USCity-Map")||chartDetails.getChartType().equalsIgnoreCase("Combo-Aus-State")||chartDetails.getChartType().equalsIgnoreCase("Combo-Aus-City")) {
                        } else {
                            if (viewId.equalsIgnoreCase(chartDetails.getDimensions().get(i))) {
                                continue;
                            }
                        }
                        String dimview = chartDetails.getDimensions().get(i);
                        String dimName = reportMeta.getViewbys().get(reportMeta.getViewbysIds().indexOf(dimview));
                        if (chartDetails != null && chartDetails.getDimensions() != null) {
                            graphBuffer.append(" <li id='ViewBy" + chartDetails.getDimensions().get(i) + "' style='width:auto;height:auto;color:white' >");
                            graphBuffer.append("<table id='viewTab" + chartDetails.getDimensions().get(i) + "'>");
                            graphBuffer.append(" <tr>");
                            graphBuffer.append(" <td >");
                            graphBuffer.append("<img src=\"" + ctxPath + "/icons pinvoke/cross-small.png\" style=\"cursor:pointer\" onclick=\"javascript:deleteColumn('ViewBy" + chartDetails.getDimensions().get(i) + "','" + dropTar + "','" + dimName + "')\" />");
                            graphBuffer.append("</td>");
                            graphBuffer.append("<td class=\"gFontFamily gFontSize12\" style=\"color:black\">" + dimName + "</td>");
                            graphBuffer.append("</tr>");
                            graphBuffer.append("</table>");
                            graphBuffer.append("</li>");
                        }
                    }
                } else {
                    for (int i = 0; i < chartDetails.getViewBys().size(); i++) {
                        if (chartDetails != null && chartDetails.getViewIds() != null) {
                            graphBuffer.append(" <li id='ViewBy" + chartDetails.getViewIds().get(i) + "' style='width:auto;height:auto;color:white' >");
                            graphBuffer.append("<table id='viewTab" + chartDetails.getViewIds().get(i) + "'>");
                            graphBuffer.append(" <tr>");
                            graphBuffer.append(" <td >");
                            graphBuffer.append("<img src=\"" + ctxPath + "/icons pinvoke/cross-small.png\" style=\"cursor:pointer\" onclick=\"javascript:deleteColumn('ViewBy" + chartDetails.getViewIds().get(i) + "','" + dropTar + "','" + chartDetails.getViewBys().get(i) + "')\" />");
                            graphBuffer.append("</td>");
                            graphBuffer.append("<td class=\"gFontFamily gFontSize12\" style=\"color:black\">" + chartDetails.getViewBys().get(i) + "</td>");
                            graphBuffer.append("</tr>");
                            graphBuffer.append("</table>");
                            graphBuffer.append("</li>");
                        }
                    }
                }
            }
        }
        return graphBuffer.toString();
    }

    public String buildDragableViewByForAdvance(String reportId, String dropTar, String ctxPath, Container container, String chartId, String CheckViewbyflag) {
        PbReportCollection collect = container.getReportCollect();
        String folderName = collect.getReportBizRoleName(collect.reportBizRoles[0]);

        XtendReportMeta reportMeta = new XtendReportMeta();
        reportMeta = container.getReportMeta();
        StringBuffer graphBuffer = new StringBuffer("");
        if (reportMeta != null && reportMeta.getChartData() != null) {
            Map<String, DashboardChartData> chartData = new LinkedHashMap<String, DashboardChartData>(reportMeta.getChartData());
            List<String> charts = new ArrayList(chartData.keySet());
            if (chartData.get("chart1").getIsDashboardDefined() != null && chartData.get("chart1").getIsDashboardDefined().equalsIgnoreCase("Y")) {
                List<String> allViewBys = reportMeta.getViewbys();
                List<String> allViewByIds = reportMeta.getViewbysIds();
                for (int m = 0; m < reportMeta.getSelectedViewBys().size(); m++) {
                    String viewByName = "";
                    String viewById = reportMeta.getSelectedViewBys().get(m);
                    int i = 0;
                    for (i = 0; i < allViewByIds.size(); i++) {
                        if (allViewByIds.get(i).equalsIgnoreCase(viewById)) {
                            break;
                        }
                    }
                    viewByName = allViewBys.get(i);
                    graphBuffer.append(" <li id='ViewBy" + viewById + "' style='width:auto;height:auto;color:white' >");
                    graphBuffer.append("<table id='viewTab" + viewById + "'>");
                    graphBuffer.append(" <tr>");
                    graphBuffer.append(" <td >");
                    graphBuffer.append("<img src=\"" + ctxPath + "/icons pinvoke/cross-small.png\" style=\"cursor:pointer\" onclick=\"javascript:deleteColumn('ViewBy" + viewById + "','" + dropTar + "','" + viewByName + "')\" />");
                    graphBuffer.append("</td>");
                    graphBuffer.append("<td style=\"color:black\">" + viewByName + "</td>");
                    graphBuffer.append("</tr>");
                    graphBuffer.append("</table>");
                    graphBuffer.append("</li>");
                }
            } else {
                for (String chart : charts) {
                    DashboardChartData chartDetails = reportMeta.getChartData().get(chart);
//                        if(chartDetails!=null && chartDetails.getViewIds() !=null){
                    for (int m = 0; m < chartDetails.getViewIds().size(); m++) {
                        graphBuffer.append(" <li id='ViewBy" + chartDetails.getViewIds().get(m) + "' style='width:auto;height:auto;color:white' >");
                        graphBuffer.append("<table id='viewTab" + chartDetails.getViewIds().get(m) + "'>");
                        graphBuffer.append(" <tr>");
                        graphBuffer.append(" <td >");
                        graphBuffer.append("<img src=\"" + ctxPath + "/icons pinvoke/cross-small.png\" style=\"cursor:pointer\" onclick=\"javascript:deleteColumn('ViewBy" + chartDetails.getViewIds().get(m) + "','" + dropTar + "','" + chartDetails.getViewBys().get(m) + "')\" />");
                        graphBuffer.append("</td>");
                        graphBuffer.append("<td style=\"color:black\">" + chartDetails.getViewBys().get(m) + "</td>");
                        graphBuffer.append("</tr>");
                        graphBuffer.append("</table>");
                        graphBuffer.append("</li>");
                    }
                }
            }
        }
        return graphBuffer.toString();
    }

    public String buildDragableViewByObject(String reportId, Container container, String dropTar, String ctxPath, String fileLocation) {
        ReportObjectMeta reportMeta = new ReportObjectMeta();
        String filePath = fileLocation + "/analyticalobject/R_GO_" + reportId + ".json";
        FileReadWrite fileReadWrite = new FileReadWrite();
        String objectData = fileReadWrite.loadJSON(filePath);
        Type type = new TypeToken<ReportObjectMeta>() {
        }.getType();
        //Gson gson = new Gson();
        reportMeta = gson.fromJson(objectData, type);
        StringBuffer graphBuffer = new StringBuffer("");
        ArrayList viewById = new ArrayList();
        ArrayList viewByName = new ArrayList();
        if (reportMeta != null && reportMeta.getViewNames() != null) {
//        for(int j=0;j<reportMeta.getViewNames().size();j++){
            for (int k = 0; k < reportMeta.getViewIds().size(); k++) {
//        if(viewByNames.get(k).equalsIgnoreCase(reportMeta.getViewbys().get(j))){
                viewById.add(reportMeta.getViewIds().get(k));
                viewByName.add(reportMeta.getViewNames().get(k));
//            }
            }
//        }
            for (int i = 0; i < viewByName.size(); i++) {
                if (reportMeta != null && viewById != null) {
                    graphBuffer.append(" <li id='ViewBy" + viewById.get(i) + "' style='width:auto;height:auto;color:white' >");
                    graphBuffer.append("<table id='ViewTab" + viewById.get(i) + "'>");
                    graphBuffer.append(" <tr>");
                    graphBuffer.append(" <td >");
                    graphBuffer.append("<img src=\"" + ctxPath + "/icons pinvoke/cross-small.png\" style=\"cursor:pointer\" onclick=\"javascript:deleteColumn('ViewBy" + viewById.get(i) + "','" + dropTar + "','" + viewByName.get(i) + "')\" />");
                    graphBuffer.append("</td>");
                    graphBuffer.append("<td class=\"gFontFamily gFontSize12\" style=\"color:black\">" + viewByName.get(i) + "</td>");
                    graphBuffer.append("</tr>");
                    graphBuffer.append("</table>");
                    graphBuffer.append("</li>");
                }
            }
        }
        return graphBuffer.toString();
    }

    public String buildDragableMeasBy(String reportId, String dropTar, String ctxPath, Container container, String chartId, String checkMeasureFlag) {
        XtendReportMeta reportMeta = new XtendReportMeta();
        reportMeta = container.getReportMeta();//gson.fromJson(metaString,tarType);
        StringBuffer graphBuffer = new StringBuffer("");
        ArrayList viewById = new ArrayList();
        ArrayList viewByNames = new ArrayList();
//                    viewById.add("TIME");
//                    viewByNames.add("Time");
        if (reportMeta != null && reportMeta.getChartData() != null) {
            Map<String, DashboardChartData> chartData = new LinkedHashMap<String, DashboardChartData>(reportMeta.getChartData());

            List<String> charts = new ArrayList(chartData.keySet());
            Map map = new HashMap();
            if (chartId.equalsIgnoreCase("Local")) {
                for (String chart : charts) {
                    DashboardChartData chartDetails = reportMeta.getChartData().get(chart);
                    if (!map.containsKey(chartDetails.getMeassures().get(0))) {
                        map.put(chartDetails.getMeassures().get(0), chartDetails.getMeassures().get(0));
                        if (chartDetails != null && chartDetails.getMeassureIds() != null) {
                            graphBuffer.append(" <li id='MeasBy" + chartDetails.getMeassureIds().get(0) + "' style='width:auto;height:auto;color:white' >");
                            graphBuffer.append("<table id='measTab" + chartDetails.getMeassureIds().get(0) + "'>");
                            graphBuffer.append(" <tr>");
                            graphBuffer.append(" <td >");
                            graphBuffer.append("<img src=\"" + ctxPath + "/icons pinvoke/cross-small.png\" style=\"cursor:pointer\" onclick=\"javascript:deleteColumn('MeasBy" + chartDetails.getMeassureIds().get(0) + "','" + dropTar + "','" + chartDetails.getMeassures().get(0) + "')\" />");
                            graphBuffer.append("</td>");
                            graphBuffer.append("<td class=\"gFontFamily gFontSize12\" style=\"color:black\">" + chartDetails.getMeassures().get(0) + "</td>");
                            graphBuffer.append("</tr>");
                            graphBuffer.append("</table>");
                            graphBuffer.append("</li>");
                        }
                    }
                }
            } else {
                DashboardChartData chartDetails = reportMeta.getChartData().get(chartId);
                if (chartDetails.getChartType().equalsIgnoreCase("KPI-Dashboard")) {
                    for (int i = 0; i < chartDetails.getDefaultMeasures().size(); i++) {

                        if (!map.containsKey(chartDetails.getDefaultMeasures().get(i))) {
                            map.put(chartDetails.getDefaultMeasures().get(i), chartDetails.getDefaultMeasures().get(i));
                            if (chartDetails != null && chartDetails.getDefaultMeasureIds() != null) {
                                graphBuffer.append(" <li id='MeasBy" + chartDetails.getDefaultMeasureIds().get(i) + "' style='width:auto;height:auto;color:white' >");
                                graphBuffer.append("<table id='measTab" + chartDetails.getDefaultMeasureIds().get(i) + "'>");
                                graphBuffer.append(" <tr>");
                                graphBuffer.append(" <td >");
                                graphBuffer.append("<img src=\"" + ctxPath + "/icons pinvoke/cross-small.png\" style=\"cursor:pointer\" onclick=\"javascript:deleteColumn('MeasBy" + chartDetails.getDefaultMeasureIds().get(i) + "','" + dropTar + "','" + chartDetails.getDefaultMeasures().get(i) + "')\" />");
                                graphBuffer.append("</td>");
                                graphBuffer.append("<td class=\"gFontFamily gFontSize12\" style=\"color:black\">" + chartDetails.getDefaultMeasures().get(i) + "</td>");
                                graphBuffer.append("</tr>");
                                graphBuffer.append("</table>");
                                graphBuffer.append("</li>");
                            }
                        }
                    }
                } else {



                    for (int i = 0; i < chartDetails.getMeassures().size(); i++) {

                        if (!map.containsKey(chartDetails.getMeassures().get(i))) {
                            map.put(chartDetails.getMeassures().get(i), chartDetails.getMeassures().get(i));
                            if (chartDetails != null && chartDetails.getMeassureIds() != null) {
                                graphBuffer.append(" <li id='MeasBy" + chartDetails.getMeassureIds().get(i) + "' style='width:auto;height:auto;color:white' >");
                                graphBuffer.append("<table id='measTab" + chartDetails.getMeassureIds().get(i) + "'>");
                                graphBuffer.append(" <tr>");
                                graphBuffer.append(" <td >");
                                graphBuffer.append("<img src=\"" + ctxPath + "/icons pinvoke/cross-small.png\" style=\"cursor:pointer\" onclick=\"javascript:deleteColumn('MeasBy" + chartDetails.getMeassureIds().get(i) + "','" + dropTar + "','" + chartDetails.getMeassures().get(i) + "')\" />");
                                graphBuffer.append("</td>");
                                graphBuffer.append("<td class=\"gFontFamily gFontSize12\" style=\"color:black\">" + chartDetails.getMeassures().get(i) + "</td>");
                                graphBuffer.append("</tr>");
                                graphBuffer.append("</table>");
                                graphBuffer.append("</li>");
                            }
                        }
                    }
                }
            }
        }
        return graphBuffer.toString();
    }

    public String buildDragableMeasByForAdvance(String reportId, String dropTar, String ctxPath, Container container, String chartId, String checkMeasureFlag) {
        XtendReportMeta reportMeta = new XtendReportMeta();
        reportMeta = container.getReportMeta();//gson.fromJson(metaString,tarType);
        StringBuffer graphBuffer = new StringBuffer("");
        if (reportMeta != null && reportMeta.getChartData() != null) {
            Map<String, DashboardChartData> chartData = new LinkedHashMap<String, DashboardChartData>(reportMeta.getChartData());

            List<String> charts = new ArrayList(chartData.keySet());
            Map map = new HashMap();
            if (chartData.get("chart1").getIsDashboardDefined() != null && chartData.get("chart1").getIsDashboardDefined().equalsIgnoreCase("Y")) {
                List<String> allMeasBys = reportMeta.getMeasures();
                List<String> allMeasByIds = reportMeta.getMeasuresIds();
                for (int m = 0; m < reportMeta.getSelectedMeasBys().size(); m++) {
                    String measByName = "";
                    String measById = reportMeta.getSelectedMeasBys().get(m);
                    int i = 0;
                    for (i = 0; i < allMeasByIds.size(); i++) {
                        if (allMeasByIds.get(i).equalsIgnoreCase(measById)) {
                            break;
                        }
                    }
                    measByName = allMeasBys.get(i);
                    if (measByName != null) {
                        graphBuffer.append(" <li id='MeasBy" + measById + "' style='width:auto;height:auto;color:white' >");
                        graphBuffer.append("<table id='measTab" + measById + "'>");
                        graphBuffer.append(" <tr>");
                        graphBuffer.append(" <td >");
                        graphBuffer.append("<img src=\"" + ctxPath + "/icons pinvoke/cross-small.png\" style=\"cursor:pointer\" onclick=\"javascript:deleteColumn('MeasBy" + measById + "','" + dropTar + "','" + measByName + "')\" />");
                        graphBuffer.append("</td>");
                        graphBuffer.append("<td style=\"color:black\">" + measByName + "</td>");
                        graphBuffer.append("</tr>");
                        graphBuffer.append("</table>");
                        graphBuffer.append("</li>");
                    }
                }
            } else {

                for (String chart : charts) {
                    DashboardChartData chartDetails = reportMeta.getChartData().get(chart);
//
                    for (int m = 0; m < chartDetails.getMeassures().size(); m++) {
                        if (!map.containsKey(chartDetails.getMeassures().get(m))) {
                            map.put(chartDetails.getMeassures().get(m), chartDetails.getMeassures().get(m));
                            if (chartDetails != null && chartDetails.getMeassureIds() != null) {
                                graphBuffer.append(" <li id='MeasBy" + chartDetails.getMeassureIds().get(m) + "' style='width:auto;height:auto;color:white' >");
                                graphBuffer.append("<table id='measTab" + chartDetails.getMeassureIds().get(m) + "'>");
                                graphBuffer.append(" <tr>");
                                graphBuffer.append(" <td >");
                                graphBuffer.append("<img src=\"" + ctxPath + "/icons pinvoke/cross-small.png\" style=\"cursor:pointer\" onclick=\"javascript:deleteColumn('MeasBy" + chartDetails.getMeassureIds().get(m) + "','" + dropTar + "','" + chartDetails.getMeassures().get(m) + "')\" />");
                                graphBuffer.append("</td>");
                                graphBuffer.append("<td style=\"color:black\">" + chartDetails.getMeassures().get(m) + "</td>");
                                graphBuffer.append("</tr>");
                                graphBuffer.append("</table>");
                                graphBuffer.append("</li>");
                            }
                        }
                    }
                }
            }

        }
        return graphBuffer.toString();
    }

    public String buildDragableMeasByObject(String reportId, Container container, String dropTar, String ctxPath, String fileLocation) {
        ReportObjectMeta reportMeta = new ReportObjectMeta();
        String filePath = fileLocation + "/analyticalobject/R_GO_" + reportId + ".json";
        FileReadWrite fileReadWrite = new FileReadWrite();
        String objectData = fileReadWrite.loadJSON(filePath);
        Type type = new TypeToken<ReportObjectMeta>() {
        }.getType();
        //Gson gson = new Gson();
        reportMeta = gson.fromJson(objectData, type);
        StringBuffer graphBuffer = new StringBuffer("");
        ArrayList measById = new ArrayList();
        ArrayList measByName = new ArrayList();
        if (reportMeta != null && reportMeta.getMeasIds() != null) {
//        for(int j=0;j<reportMeta.getViewNames().size();j++){
            for (int k = 0; k < reportMeta.getMeasIds().size(); k++) {
//        if(viewByNames.get(k).equalsIgnoreCase(reportMeta.getViewbys().get(j))){
                measById.add(reportMeta.getMeasIds().get(k));
                measByName.add(reportMeta.getMeasNames().get(k));
//            }
            }
            for (int i = 0; i < measByName.size(); i++) {
                if (reportMeta != null && measById != null) {
                    graphBuffer.append(" <li id='MeasBy" + measById.get(i) + "' style='width:auto;height:auto;color:white' >");
                    graphBuffer.append("<table id='measTab" + measById.get(i) + "'>");
                    graphBuffer.append(" <tr>");
                    graphBuffer.append(" <td >");
                    graphBuffer.append("<img src=\"" + ctxPath + "/icons pinvoke/cross-small.png\" style=\"cursor:pointer\" onclick=\"javascript:deleteColumn('MeasBy" + measById.get(i) + "','" + dropTar + "','" + measByName.get(i) + "')\" />");
                    graphBuffer.append("</td>");
                    graphBuffer.append("<td class=\"gFontFamily gFontSize12\" style=\"color:black\">" + measByName.get(i) + "</td>");
                    graphBuffer.append("</tr>");
                    graphBuffer.append("</table>");
                    graphBuffer.append("</li>");
                }
            }
        }
        return graphBuffer.toString();
    }

    public String buildTrendDragableViewBy(String reportId, String dropTar, String ctxPath, Container container, String chartId, String localAddEdit) {
        PbReportCollection collect = container.getReportCollect();
        String folderName = collect.getReportBizRoleName(collect.reportBizRoles[0]);

        XtendReportMeta reportMeta = new XtendReportMeta();
//        FileReadWrite readWrite = new FileReadWrite();
//        String metaString = readWrite.loadJSON("/usr/local/cache/"+folderName.replaceAll("\\W", "").trim()+"/"+collect.reportName.replaceAll("\\W", "").trim()+"_"+collect.reportId+"/"+collect.reportName.replaceAll("\\W", "").trim()+"_"+collect.reportId+"_data.json");
//         Type tarType=new TypeToken<XtendReportMeta>() {}.getType();
//         //Gson gson = new Gson();
        reportMeta = container.getTrendReportMeta();//gson.fromJson(metaString,tarType);
        StringBuffer graphBuffer = new StringBuffer("");
        ArrayList viewById = new ArrayList();
        ArrayList viewByNames = new ArrayList();
        ArrayList trendViewByNames = new ArrayList();
        viewById.add("TIME");
        viewByNames.add("Time");
        if (localAddEdit.equalsIgnoreCase("localEdit")) {
            if (reportMeta != null && reportMeta.getChartData() != null) {
                Map<String, DashboardChartData> chartData = new LinkedHashMap<String, DashboardChartData>(reportMeta.getChartData());

                List<String> charts = new ArrayList(chartData.keySet());
                if (chartId.equalsIgnoreCase("Local")) {
                    for (String chart : charts) {
                        DashboardChartData chartDetails = reportMeta.getChartData().get(chart);
                        if (chartDetails != null && chartDetails.getViewBys() != null) {
                            graphBuffer.append(" <li id='ViewBy" + chartDetails.getViewBys().get(0) + "' style='width:auto;height:auto;color:white' >");
                            graphBuffer.append("<table id='viewTab" + chartDetails.getViewBys().get(0) + "'>");
                            graphBuffer.append(" <tr>");
                            graphBuffer.append(" <td >");
                            graphBuffer.append("<img src=\"" + ctxPath + "/icons pinvoke/cross-small.png\" style=\"cursor:pointer\" onclick=\"javascript:deleteColumn('ViewBy" + chartDetails.getViewBys().get(0) + "','" + dropTar + "','" + chartDetails.getViewBys().get(0) + "')\" />");
                            graphBuffer.append("</td>");
                            graphBuffer.append("<td style=\"color:black\">" + chartDetails.getViewBys().get(0) + "</td>");
                            graphBuffer.append("</tr>");
                            graphBuffer.append("</table>");
                            graphBuffer.append("</li>");
                        }
                    }
                } else {
                    DashboardChartData chartDetails = reportMeta.getChartData().get(chartId);
                    if (chartDetails.getDimensions() != null && chartDetails.getDimensions().size() > 0) {
                        String view = "";
                        view = chartDetails.getViewBys().get(0);
                        graphBuffer.append(" <li id='ViewBy" + view + "' style='width:auto;height:auto;color:white' >");
                        graphBuffer.append("<table id='viewTab" + view + "'>");
                        graphBuffer.append(" <tr>");
                        graphBuffer.append(" <td >");
                        graphBuffer.append("<img src=\"" + ctxPath + "/icons pinvoke/cross-small.png\" style=\"cursor:pointer\" onclick=\"javascript:deleteColumn('ViewBy" + view + "','" + dropTar + "','" + view + "')\" />");
                        graphBuffer.append("</td>");
                        graphBuffer.append("<td style=\"color:black\">" + view + "</td>");
                        graphBuffer.append("</tr>");
                        graphBuffer.append("</table>");
                        graphBuffer.append("</li>");
                        for (int i = 0; i < chartDetails.getDimensions().size(); i++) {
                            if (view.equalsIgnoreCase(chartDetails.getDimensions().get(i))) {
                                continue;
                            }
                            String dimview = chartDetails.getDimensions().get(i);
                            graphBuffer.append(" <li id='ViewBy" + dimview + "' style='width:auto;height:auto;color:white' >");
                            graphBuffer.append("<table id='viewTab" + dimview + "'>");
                            graphBuffer.append(" <tr>");
                            graphBuffer.append(" <td >");
                            graphBuffer.append("<img src=\"" + ctxPath + "/icons pinvoke/cross-small.png\" style=\"cursor:pointer\" onclick=\"javascript:deleteColumn('ViewBy" + dimview + "','" + dropTar + "','" + dimview + "')\" />");
                            graphBuffer.append("</td>");
                            graphBuffer.append("<td style=\"color:black\">" + dimview + "</td>");
                            graphBuffer.append("</tr>");
                            graphBuffer.append("</table>");
                            graphBuffer.append("</li>");
                        }
                    } else {
                        for (int i = 0; i < chartDetails.getViewBys().size(); i++) {
                            graphBuffer.append(" <li id='ViewBy" + chartDetails.getViewBys().get(i) + "' style='width:auto;height:auto;color:white' >");
                            graphBuffer.append("<table id='viewTab" + chartDetails.getViewBys().get(i) + "'>");
                            graphBuffer.append(" <tr>");
                            graphBuffer.append(" <td >");
                            graphBuffer.append("<img src=\"" + ctxPath + "/icons pinvoke/cross-small.png\" style=\"cursor:pointer\" onclick=\"javascript:deleteColumn('ViewBy" + chartDetails.getViewBys().get(i) + "','" + dropTar + "','" + chartDetails.getViewBys().get(i) + "')\" />");
                            graphBuffer.append("</td>");
                            graphBuffer.append("<td style=\"color:black\">" + chartDetails.getViewBys().get(i) + "</td>");
                            graphBuffer.append("</tr>");
                            graphBuffer.append("</table>");
                            graphBuffer.append("</li>");
                        }
                    }
                }
            }

        } else {
            if (reportMeta != null && reportMeta.getChartData() != null) {
                Map<String, DashboardChartData> chartData = new LinkedHashMap<String, DashboardChartData>(reportMeta.getChartData());
                List<String> charts = new ArrayList(chartData.keySet());
                for (String chart : charts) {
                    DashboardChartData chartDetails = reportMeta.getChartData().get(chart);
                    String view = "";
//              if(chartDetails.getDimensions()!=null && chartDetails.getDimensions().size()>0){
//
//
//               for(int i=0;i<chartDetails.getDimensions().size();i++){
//              view =  chartDetails.getDimensions().get(i);
//              if(view.equalsIgnoreCase("Time")){
//              trendViewByNames.add(view);
//
//              }
//                   else if(view.equalsIgnoreCase("Year"))
//                   {
//              trendViewByNames.add(view);
//
//              }
//                   else if(view.equalsIgnoreCase("Month Year") || view.equalsIgnoreCase("Month - Year"))
//                   {
//              trendViewByNames.add(view);
//
//              }
//                   else if(view.equalsIgnoreCase("Qtr Year") || view.equalsIgnoreCase("Qtr - Year"))
//                   {
//              trendViewByNames.add(view);
//
//              }
//                   else if(view.equalsIgnoreCase("Month"))
//                   {
//              trendViewByNames.add(view);
//
//              }
//                   else if(view.equalsIgnoreCase("Qtr"))
//                   {
//              trendViewByNames.add(view);
//
//              }
//
//               }
//
//                       }

                    view = chartDetails.getViewBys().get(0);
                    trendViewByNames.add(view);

                }
            }
            graphBuffer.append(" <li id='ViewBy" + trendViewByNames.get(0) + "' style='width:auto;height:auto;color:white' >");
            graphBuffer.append("<table id='viewTab" + trendViewByNames.get(0) + "'>");
            graphBuffer.append(" <tr>");
            graphBuffer.append(" <td >");
            graphBuffer.append("<img src=\"" + ctxPath + "/icons pinvoke/cross-small.png\" style=\"cursor:pointer\" onclick=\"javascript:deleteColumn('ViewBy" + trendViewByNames.get(0) + "','" + dropTar + "','" + trendViewByNames.get(0) + "')\" />");
            graphBuffer.append("</td>");
            graphBuffer.append("<td style=\"color:black\">" + trendViewByNames.get(0) + "</td>");
            graphBuffer.append("</tr>");
            graphBuffer.append("</table>");
            graphBuffer.append("</li>");
//                graphBuffer.append(" <li id='ViewBy" + viewByNames.get(0) + "' style='width:auto;height:auto;color:white' >");
//                graphBuffer.append("<table id='viewTab" + viewByNames.get(0) + "'>");
//                graphBuffer.append(" <tr>");
//                graphBuffer.append(" <td >");
//                graphBuffer.append("<img src=\""+ctxPath+"/icons pinvoke/cross-small.png\" style=\"cursor:pointer\" onclick=\"javascript:deleteColumn('ViewBy" + viewByNames.get(0) + "','"+dropTar+"','"+viewByNames.get(0)+"')\" />");
//                graphBuffer.append("</td>");
//                graphBuffer.append("<td style=\"color:black\">" + viewByNames.get(0) + "</td>");
//                graphBuffer.append("</tr>");
//                graphBuffer.append("</table>");
//                graphBuffer.append("</li>");
        }
        return graphBuffer.toString();
    }

    public String buildTrendDragableMeasBy(String reportId, String dropTar, String ctxPath, Container container, String chartId, String localAddEdit) {
        XtendReportMeta reportMeta = new XtendReportMeta();
        reportMeta = container.getTrendReportMeta();//gson.fromJson(metaString,tarType);
        StringBuffer graphBuffer = new StringBuffer("");
        ArrayList viewById = new ArrayList();
        ArrayList viewByNames = new ArrayList();
        viewById.add("TIME");
        viewByNames.add("Time");
        if (reportMeta != null && reportMeta.getChartData() != null) {
            Map<String, DashboardChartData> chartData = new LinkedHashMap<String, DashboardChartData>(reportMeta.getChartData());

            List<String> charts = new ArrayList(chartData.keySet());
            Map map = new HashMap();
            if (chartId.equalsIgnoreCase("Local")) {
                for (String chart : charts) {
                    DashboardChartData chartDetails = reportMeta.getChartData().get(chart);
                    if (!map.containsKey(chartDetails.getMeassures().get(0))) {
                        map.put(chartDetails.getMeassures().get(0), chartDetails.getMeassures().get(0));
                        graphBuffer.append(" <li id='MeasBy" + chartDetails.getMeassures().get(0) + "' style='width:auto;height:auto;color:white' >");
                        graphBuffer.append("<table id='measTab" + chartDetails.getMeassures().get(0) + "'>");
                        graphBuffer.append(" <tr>");
                        graphBuffer.append(" <td >");
                        graphBuffer.append("<img src=\"" + ctxPath + "/icons pinvoke/cross-small.png\" style=\"cursor:pointer\" onclick=\"javascript:deleteColumn('MeasBy" + chartDetails.getMeassures().get(0) + "','" + dropTar + "','" + chartDetails.getMeassures().get(0) + "')\" />");
                        graphBuffer.append("</td>");
                        graphBuffer.append("<td style=\"color:black\">" + chartDetails.getMeassures().get(0) + "</td>");
                        graphBuffer.append("</tr>");
                        graphBuffer.append("</table>");
                        graphBuffer.append("</li>");
                    }
                }
            } else {
                DashboardChartData chartDetails = reportMeta.getChartData().get(chartId);
                for (int i = 0; i < chartDetails.getMeassures().size(); i++) {
                    if (!map.containsKey(chartDetails.getMeassures().get(i))) {
                        map.put(chartDetails.getMeassures().get(i), chartDetails.getMeassures().get(i));
                        graphBuffer.append(" <li id='MeasBy" + chartDetails.getMeassures().get(i) + "' style='width:auto;height:auto;color:white' >");
                        graphBuffer.append("<table id='measTab" + chartDetails.getMeassures().get(i) + "'>");
                        graphBuffer.append(" <tr>");
                        graphBuffer.append(" <td >");
                        graphBuffer.append("<img src=\"" + ctxPath + "/icons pinvoke/cross-small.png\" style=\"cursor:pointer\" onclick=\"javascript:deleteColumn('MeasBy" + chartDetails.getMeassures().get(i) + "','" + dropTar + "','" + chartDetails.getMeassures().get(i) + "')\" />");
                        graphBuffer.append("</td>");
                        graphBuffer.append("<td style=\"color:black\">" + chartDetails.getMeassures().get(i) + "</td>");
                        graphBuffer.append("</tr>");
                        graphBuffer.append("</table>");
                        graphBuffer.append("</li>");
                    }
                }
            }
        }
        return graphBuffer.toString();
    }

    public String getCustomMembers(String foldersIds) {
        PbReturnObject retObj = null;
        String finalQuery = null;
        String[] colNames = null;
        Object obj[] = new Object[1];
        if (foldersIds != null && !"".equalsIgnoreCase(foldersIds)) {
            obj[0] = foldersIds;
        } else {
            obj[0] = "''";
        }
        String custmemName = "";

        StringBuffer outerBuffer = new StringBuffer("");
        try {
            finalQuery = "SELECT distinct USER_COL_NAME,REF_ELEMENT_ID from PRG_USER_ALL_INFO_DETAILS  where SUB_FOLDER_TYPE='Formula' and FOLDER_ID in(" + obj[0] + ") order by USER_COL_NAME,REF_ELEMENT_ID";
            retObj = execSelectSQL(finalQuery);
            colNames = retObj.getColumnNames();




            retObj.writeString();
            for (int i = 0; i < retObj.getRowCount(); i++) {

                custmemName = retObj.getFieldValueString(i, colNames[0]);
                outerBuffer.append("<li class='closed'>");
                outerBuffer.append("<img src='icons pinvoke/table.png'></img>");
                outerBuffer.append("<span style='font-family:verdana;font-size:8pt'>" + custmemName + "</span>");
                outerBuffer.append("</li>");
            }
        } catch (Exception exception) {
            logger.error("Exception:", exception);
        }
        return outerBuffer.toString();
    }

    public String buildTableColumns(String[] MeasureIds, String[] MeasureNames, StringBuffer prevColumns) {
        StringBuffer graphBuffer = new StringBuffer("");
        if (MeasureIds != null && MeasureNames != null) {
            for (int i = 0; i < MeasureIds.length; i++) {
                graphBuffer.append(" <li id='Msr" + MeasureIds[i].replace("A_", "") + "' style='width:auto;height:auto;color:white' class='navtitle-hover'>");
                graphBuffer.append("<table id='Tab" + MeasureIds[i].replace("A_", "") + "'>");
                graphBuffer.append(" <tr>");
                graphBuffer.append(" <td >");
                graphBuffer.append("  <a href=\"javascript:deleteMeasure('Msr" + MeasureIds[i].replace("A_", "") + "')\" class=\"ui-icon ui-icon-close\"></a>");
                graphBuffer.append("</td>");
                graphBuffer.append("<td style=\"color:black\">" + MeasureNames[i] + "</td>");
                graphBuffer.append("</tr>");
                graphBuffer.append("</table>");
                graphBuffer.append("</li>");
            }
        }

        return graphBuffer.toString();
    }

//    public String buildTabCols(int viewByCount, ArrayList displayColumns, ArrayList displayLabels,HttpServletRequest request) {
//        StringBuffer graphBuffer = new StringBuffer("");
//
//        if (displayColumns != null && displayLabels != null) {
//            graphBuffer.append("<li class='closed' id='tableColumns' style='color:white'>");
//            graphBuffer.append("<img src='"+request.getContextPath()+"/icons pinvoke/table.png'></img>");
//            graphBuffer.append("<span style='font-family:verdana;font-size:8pt;color:black'>TableColumns</span>");
//            for (int i = viewByCount; i < displayColumns.size(); i++) {
//                String disColumn = String.valueOf(displayColumns.get(i));
//                String disLabel = String.valueOf(displayLabels.get(i));
//                if (disColumn.lastIndexOf("_percentwise") == -1) {
//                    graphBuffer.append("<ul id='tabColumn-" + disColumn.replace("A_", "") + "'>");
//                    graphBuffer.append("<li id='GrpCol-" + disColumn.replace("A_", "") + "' style='color:white' >");
//                    graphBuffer.append("<img src='"+request.getContextPath()+"/icons pinvoke/table.png'></img>");
//                    graphBuffer.append("<span id='"+disColumn.replace("A_", "")+"' style='font-family:verdana;font-size:8pt;color:black'>" + disLabel + "</span>");
//                    graphBuffer.append("</li>");
//                    graphBuffer.append("</ul>");
//            }
//        }
//            graphBuffer.append("</li>");
//        }
//        return graphBuffer.toString();
//    }
    public String buildTableMeasures(int viewByCount, ArrayList displayColumns, ArrayList displayLabels, boolean avoidRuntimeColumns, ArrayList hideColumns) {
        StringBuffer graphBuffer = new StringBuffer("");

        if (displayColumns != null && displayLabels != null) {
            for (int i = viewByCount; i < displayColumns.size(); i++) {
                String disColumn = String.valueOf(displayColumns.get(i));
                String disLabel = String.valueOf(displayLabels.get(i));
                //added by Nazneen for getting fact names of the measures
                String getbussColNameQry = "";
                if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.SQL_SERVER)) {
                    getbussColNameQry = "select isnull(table_disp_name,disp_name) from PRG_USER_ALL_INFO_DETAILS where ELEMENT_ID = " + disColumn.replace("A_", "");
                } else if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.MYSQL)) {
                    getbussColNameQry = "select ifnull(table_disp_name,disp_name) from PRG_USER_ALL_INFO_DETAILS where ELEMENT_ID = " + disColumn.replace("A_", "");
                } else {
                    getbussColNameQry = "select NVL(table_disp_name,disp_name) from PRG_USER_ALL_INFO_DETAILS where ELEMENT_ID = " + disColumn.replace("A_", "");
                }

                String bussColName = "";
                PbReturnObject returnObject = null;
                try {
                    returnObject = execSelectSQL(getbussColNameQry);
                    if (returnObject != null && returnObject.getRowCount() != 0) {
                        bussColName = returnObject.getFieldValueString(0, 0);
                    }
                } catch (SQLException ex) {

                    logger.error("Exception:", ex);
                }
                //end of code by Nazneen
                //if (disColumn.lastIndexOf("_percentwise") == -1) {
                if (avoidRuntimeColumns && RTMeasureElement.isRunTimeMeasure(disColumn)) {
                    continue;
                } else {//modified by bhargavi
                    String displayColumns1 = disColumn.replace("A_", "");
                    if (!hideColumns.contains(displayColumns1)) {
                        //changed by Naznee
                        graphBuffer.append(" <li onmouseover=\"tooltipShow('GrpCol" + disColumn.replace("A_", "") + "','" + bussColName + "')\" id='GrpCol" + disColumn.replace("A_", "") + "' style='width:auto;height:auto;color:white' class='navtitle-hover'>");
//                    graphBuffer.append(" <li id='GrpCol" + disColumn.replace("A_", "") + "' style='width:auto;height:auto;color:white' class='navtitle-hover'>");
                        graphBuffer.append("<table id='GrpTab" + disColumn.replace("A_", "") + "'>");
                        graphBuffer.append(" <tr>");
                        graphBuffer.append(" <td >");
                        graphBuffer.append("  <a href=\"javascript:deleteColumn('GrpCol" + disColumn.replace("A_", "") + "')\" class=\"ui-icon ui-icon-close\"></a>");
                        graphBuffer.append("</td>");
                        graphBuffer.append("<td class=\"gFontFamily gFontSize12\" style=\"color:black\">" + disLabel + "</td>");
                        graphBuffer.append("</tr>");
                        graphBuffer.append("</table>");
                        graphBuffer.append("</li>");
                    }
                }
            }

        }
        return graphBuffer.toString();
    }

    public String buildTableMeasuresWithTarget(int viewByCount, ArrayList displayColumns, ArrayList displayLabels, boolean avoidRuntimeColumns, String target, ArrayList hideColumns) {
        StringBuffer graphBuffer = new StringBuffer("");

        if (displayColumns != null && displayLabels != null) {
            for (int i = viewByCount; i < displayColumns.size(); i++) {
                String disColumn = String.valueOf(displayColumns.get(i));
                String disLabel = String.valueOf(displayLabels.get(i));
                //added by Nazneen for getting fact names of the measures
                String getbussColNameQry = "";
                if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.SQL_SERVER)) {
                    getbussColNameQry = "select isnull(table_disp_name,disp_name) from PRG_USER_ALL_INFO_DETAILS where ELEMENT_ID = " + disColumn.replace("A_", "");
                } else if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.MYSQL)) {
                    getbussColNameQry = "select ifnull(table_disp_name,disp_name) from PRG_USER_ALL_INFO_DETAILS where ELEMENT_ID = " + disColumn.replace("A_", "");
                } else {
                    getbussColNameQry = "select NVL(table_disp_name,disp_name) from PRG_USER_ALL_INFO_DETAILS where ELEMENT_ID = " + disColumn.replace("A_", "");
                }

                String bussColName = "";
                PbReturnObject returnObject = null;
                try {
                    returnObject = execSelectSQL(getbussColNameQry);
                    if (returnObject != null && returnObject.getRowCount() != 0) {
                        bussColName = returnObject.getFieldValueString(0, 0);
                    }
                } catch (SQLException ex) {

                    logger.error("Exception:", ex);
                }
                //end of code by Nazneen
                //if (disColumn.lastIndexOf("_percentwise") == -1) {
                if (avoidRuntimeColumns && RTMeasureElement.isRunTimeMeasure(disColumn)) {
                    continue;
                } else {//modified by bhargavi
                    String displayColumns1 = disColumn.replace("A_", "");
                    if (!hideColumns.contains(displayColumns1)) {
//                    graphBuffer.append(" <li id='GrpCol" + disColumn.replace("A_", "") + "' style='width:auto;height:auto;color:white' class='navtitle-hover'>");
                        graphBuffer.append(" <li onmouseover=\"tooltipShow('GrpCol" + disColumn.replace("A_", "") + "','" + bussColName + "')\" id='GrpCol" + disColumn.replace("A_", "") + "' style='width:auto;height:auto;color:white' class='navtitle-hover'>");
                        graphBuffer.append("<table id='GrpTab" + disColumn.replace("A_", "") + "'>");
                        graphBuffer.append(" <tr>");
                        graphBuffer.append(" <td >");
                        graphBuffer.append("  <a href=\"javascript:deleteColumn1('GrpCol" + disColumn.replace("A_", "") + "','" + target + "')\" class=\"ui-icon ui-icon-close\"></a>");
                        graphBuffer.append("</td>");
                        graphBuffer.append("<td style=\"color:black\">" + disLabel + "</td>");
                        graphBuffer.append("</tr>");
                        graphBuffer.append("</table>");
                        graphBuffer.append("</li>");
                    }
                }
            }

        }
        return graphBuffer.toString();
    }

    //reading snap shot xml from database
    public Clob readSnapShotXML(Container container, String snapShotId) throws Exception {
        String pbReportId = container.getReportId();
        String[] dbColumns = null;
        String snpaShotXMLQuery = "select PRG_REPORT_PARAMETERS from PRG_AR_PERSONALIZED_REPORTS where PRG_PERSONALIZED_ID='" + snapShotId + "' ";
        String snpaShotXML = null;

        PbReturnObject returnObject = execSelectSQL(snpaShotXMLQuery);
        Clob clob = null;
        if (returnObject != null && returnObject.getRowCount() != 0) {
            dbColumns = returnObject.getColumnNames();
            //snpaShotXML = returnObject.getFieldValueClobString(0, dbColumns[0]);
//            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)){
//            String clobString =  returnObject.getFieldUnknown(0, 0);
//            clob.setString(1, clobString);
//
//            }else {
            clob = (Clob) returnObject.getFieldValueClob(0, dbColumns[0]);
        }
        // }
        return clob;
    }
    //added by gopesh for string sanphshots
    public String readSnapShotString(Container container, String snapShotId) throws Exception {
        String pbReportId = container.getReportId();
        String[] dbColumns = null;
        String snpaShotXMLQuery = "select PRG_REPORT_PARAMETERS from PRG_AR_PERSONALIZED_REPORTS where PRG_PERSONALIZED_ID='" + snapShotId + "' ";
        String snpaShotXML = null;

        PbReturnObject returnObject = execSelectSQL(snpaShotXMLQuery);
        String clob = null;
        if (returnObject != null && returnObject.getRowCount() != 0) {
            dbColumns = returnObject.getColumnNames();
            //snpaShotXML = returnObject.getFieldValueClobString(0, dbColumns[0]);
//            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)){
            clob = returnObject.getFieldUnknown(0, 0);
//            clob.setString(1, clobString);
//
//            }else {
            //clob = (Clob) returnObject.getFieldValueClob(0, dbColumns[0]);
        }
        // }
        return clob;
    }
    //added by bharathi reddy for report Access

    public boolean checkUserReportAccess(String reportId, String userId) throws Exception {

        PbDb pbdb = new PbDb();
        String userAcessCheckQuery = "";
        boolean status = true;
        //   String userId=userId;
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
            userAcessCheckQuery = "SELECT USER_ID, REPORT_ID FROM PRG_AR_USER_REPORTS where REPORT_ID=" + reportId + " and USER_ID=" + userId;
        } else {
            userAcessCheckQuery = "SELECT USER_ID, REPORT_ID FROM PRG_AR_USER_REPORTS where REPORT_ID=" + reportId + "  and USER_ID=" + userId;
        }
        //.println("userAcessCheckQuery is : "+userAcessCheckQuery);
        PbReturnObject pbro = pbdb.execSelectSQL(userAcessCheckQuery);
        if (pbro.getRowCount() > 0) {
            status = false;
        }
        return status;
    }

    //added by susheela  for report drill
    public void saveReportDrillForUser(String userId, String reportId, String memIds, String memTypes) throws Exception {
        ArrayList al = new ArrayList();
        String alreadyDrillQ = "select * from prg_report_custom_drill where report_id=" + reportId + " and user_id=" + userId;
        PbReturnObject userDrillEx = execSelectSQL(alreadyDrillQ);
        String inserQ = "insert into  prg_report_custom_drill(USER_ID,REPORT_ID,MEMBER_ID,CHILD_MEMBER_ID,Assigned_Type) values('&','&','&','&','&')";
        String updateQ = "update prg_report_custom_drill set CHILD_MEMBER_ID='&', Assigned_Type='&' where USER_ID='&' and REPORT_ID='&' and MEMBER_ID='&'";
        if (userDrillEx.getRowCount() > 0) {
            String allV[] = memIds.split("-");
            String allTypes[] = memTypes.split("-");
            for (int m = 0; m < allV.length; m++) {
                String val = allV[m];
                String type = allTypes[m];
                String memId = "";
                String childMemType = "";
                String childMemId = "";
                String indVal[] = val.split("=");
                String indType[] = type.split("=");
                memId = indVal[0];
                childMemId = indVal[1];
                childMemType = indType[1];
                Object updateObj[] = new Object[5];
                updateObj[0] = childMemId;
                updateObj[1] = childMemType;
                updateObj[2] = userId;
                updateObj[3] = reportId;
                updateObj[4] = memId;
                String finupdateQ = buildQuery(updateQ, updateObj);
                al.add(finupdateQ);

            }
        } else {
            String allV[] = memIds.split("-");
            String allTypes[] = memTypes.split("-");
            for (int m = 0; m < allV.length; m++) {
                String val = allV[m];
                String type = allTypes[m];
                String memId = "";
                String childMemType = "";
                String childMemId = "";
                String indVal[] = val.split("=");
                String indType[] = type.split("=");
                memId = indVal[0];
                childMemId = indVal[1];
                childMemType = indType[1];
                Object insertObj[] = new Object[5];
                insertObj[0] = userId;
                insertObj[1] = reportId;
                insertObj[2] = memId;
                insertObj[3] = childMemId;
                insertObj[4] = childMemType;
                String fininserQ = buildQuery(inserQ, insertObj);
                al.add(fininserQ);

            }
        }
        try {
            executeMultiple(al);
        } catch (Exception n) {
            logger.error("Exception:", n);
        }
    }

    //added by bharathi reddy for report grpViby for getting details
    public PbReturnObject getViewByNames(ArrayList viewbyIds, String ReportId) throws Exception {
        PbDb pbdb = new PbDb();
        PbReturnObject pbro = new PbReturnObject();
        StringBuffer viewList = new StringBuffer("");
        for (int i = 0; i < viewbyIds.size(); i++) {
            if (i == 0) {
                if (!(String.valueOf(viewbyIds.get(i)).equalsIgnoreCase("Time"))) {
                    viewList.append("," + String.valueOf(viewbyIds.get(i)).replace("A_", "").replace("_G", ""));
                }
            }
        }
        if (!viewList.toString().equalsIgnoreCase("")) {
            String userAcessCheckQuery = "select distinct ELEMENT_ID, DISP_NAME from PRG_USER_ALL_INFO_DETAILS where element_id "
                    + " in(" + viewList.toString().substring(1) + ")";
            //select default_value  from prg_ar_report_view_by_master where view_by_seq=1  and report_id="+ReportId+")";

            //"select distinct ELEMENT_ID, DISP_NAME from PRG_USER_ALL_INFO_DETAILS where element_id  in(" + viewList.toString().substring(1) + ")";

            pbro = pbdb.execSelectSQL(userAcessCheckQuery);
        }
        return pbro;
    }

    //added by uday on 20-mar-2010
    public String buildWhatIfGraphColumns(ArrayList allGraphColumns, HashMap displayNamesMap) {
        StringBuffer graphBuffer = new StringBuffer("");
        if (allGraphColumns != null && displayNamesMap != null) {
            for (int i = 0; i < allGraphColumns.size(); i++) {
                if (String.valueOf(allGraphColumns.get(i)).lastIndexOf("_P") == -1) {
                    graphBuffer.append(" <li id='GrpCol" + String.valueOf(allGraphColumns.get(i)).replace("A_", "") + "' style='width:auto;height:auto;color:white' class='navtitle-hover'>");
                    graphBuffer.append("<table id='GrpTab" + String.valueOf(allGraphColumns.get(i)).replace("A_", "") + "'>");
                    graphBuffer.append(" <tr>");
                    graphBuffer.append(" <td >");
                    graphBuffer.append("  <a href=\"javascript:deleteColumn('GrpCol" + String.valueOf(allGraphColumns.get(i)).replace("A_", "") + "')\" class=\"ui-icon ui-icon-close\"></a>");
                    graphBuffer.append("</td>");
                    graphBuffer.append("<td style=\"color:black\">" + String.valueOf(displayNamesMap.get(String.valueOf(allGraphColumns.get(i)))) + "</td>");
                    graphBuffer.append("</tr>");
                    graphBuffer.append("</table>");
                    graphBuffer.append("</li>");
                }
            }
        }
        return graphBuffer.toString();
    }

    public String buildWhatIfTableMeasures(int viewByCount, ArrayList displayColumns, ArrayList displayLabels, ArrayList Measures) {
        StringBuffer graphBuffer = new StringBuffer("");
        //.println("v is:: " + displayColumns);
        //.println("displayLabels is:: " + displayLabels);
        if (displayColumns != null && displayLabels != null) {
            for (int i = viewByCount; i < displayColumns.size(); i++) {
                String disColumn = String.valueOf(displayColumns.get(i));
                String disLabel = String.valueOf(displayLabels.get(i));
                //.println("disColumn is:: " + disColumn);
                //.println("disColumn.lastIndexOf(_P) is:: " + disColumn.lastIndexOf("_P"));
                if (Measures.contains(disColumn.replace("A_", "") + "CM")) {
                    if (disColumn.lastIndexOf("_P") == -1) {
                        graphBuffer.append(" <li id='GrpCol" + disColumn.replace("A_", "") + "CM" + "' style='width:auto;height:auto;color:white' class='navtitle-hover'>");
                        graphBuffer.append("<table id='GrpTab" + disColumn.replace("A_", "") + "CM" + "'>");
                        graphBuffer.append(" <tr>");
                        graphBuffer.append(" <td >");
                        graphBuffer.append("  <a href=\"javascript:deleteColumn('GrpCol" + disColumn.replace("A_", "") + "CM" + "')\" class=\"ui-icon ui-icon-close\"></a>");
                        graphBuffer.append("</td>");
                        graphBuffer.append("<td style=\"color:black\">" + disLabel + "</td>");
                        graphBuffer.append("</tr>");
                        graphBuffer.append("</table>");
                        graphBuffer.append("</li>");
                    }
                } else {
                    if (disColumn.lastIndexOf("_P") == -1) {
                        graphBuffer.append(" <li id='GrpCol" + disColumn.replace("A_", "") + "' style='width:auto;height:auto;color:white' class='navtitle-hover'>");
                        graphBuffer.append("<table id='GrpTab" + disColumn.replace("A_", "") + "'>");
                        graphBuffer.append(" <tr>");
                        graphBuffer.append(" <td >");
                        graphBuffer.append("  <a href=\"javascript:deleteColumn('GrpCol" + disColumn.replace("A_", "") + "')\" class=\"ui-icon ui-icon-close\"></a>");
                        graphBuffer.append("</td>");
                        graphBuffer.append("<td style=\"color:black\">" + disLabel + "</td>");
                        graphBuffer.append("</tr>");
                        graphBuffer.append("</table>");
                        graphBuffer.append("</li>");
                    }
                }
            }
        }
        return graphBuffer.toString();
    }

    public String buildWhatIfTableColumns(String[] MeasureIds, String[] MeasureNames, StringBuffer prevColumns) {
        StringBuffer graphBuffer = new StringBuffer("");

        if (MeasureIds != null && MeasureNames != null) {
            for (int i = 0; i < MeasureIds.length; i++) {
                graphBuffer.append(" <li id='Msr" + MeasureIds[i].replace("A_", "") + "' style='width:auto;height:auto;color:white' class='navtitle-hover'>");
                graphBuffer.append("<table id='Tab" + MeasureIds[i].replace("A_", "") + "'>");
                graphBuffer.append(" <tr>");
                graphBuffer.append(" <td >");
                graphBuffer.append(" <input id=\"Chk-" + MeasureIds[i].replace("A_", "") + "\" type=\"checkbox\"  checked name=\"ChkFor\" value=\"" + MeasureIds[i].replace("A_", "") + "-" + MeasureNames[i] + "\"  />");
                graphBuffer.append("</td>");
                graphBuffer.append(" <td >");
                graphBuffer.append("  <a href=\"javascript:deleteMeasure('Msr" + MeasureIds[i].replace("A_", "") + "')\" class=\"ui-icon ui-icon-close\"></a>");
                graphBuffer.append("</td>");
                graphBuffer.append("<td style=\"color:black\">" + MeasureNames[i] + "</td>");
                graphBuffer.append("</tr>");
                graphBuffer.append("</table>");
                graphBuffer.append("</li>");
            }
        }
        return graphBuffer.toString();
    }

    public PbReturnObject getREPValues(String elementId) {
        PbDb pbdb = new PbDb();
        PbReportQuery repQry = new PbReportQuery();
        PbReturnObject pbro = new PbReturnObject();
        String connectionId = null;
        String bussTableName = null;
        String bussColName = null;
        String finalQry = null;
        String qry = null;

        try {
            if (elementId.equalsIgnoreCase("TIME")) {
            } else {
                qry = "select buss_table_name,buss_col_name, connection_id from prg_user_all_info_details where element_id=" + elementId;
                pbro = pbdb.execSelectSQL(qry);
                bussTableName = pbro.getFieldValueString(0, 0);
                bussColName = pbro.getFieldValueString(0, 1);
                connectionId = pbro.getFieldValueString(0, 2);
                //.println("connectionId is:: " + connectionId);
                Connection con = repQry.getConnection(elementId);
                finalQry = "select distinct " + bussColName + " from " + bussTableName + " order by 1 ";
                pbro = pbdb.execSelectSQL(finalQry, con);
            }

        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        return pbro;
    }

    public String insertParameterizedFormula(String dimMembers,
            String paramFilterName,
            String measEleId,
            String dimBussTableId,
            String dimElementId,
            String qualDimColName,
            String dimType,
            String type,
            ArrayList conditions, ArrayList mIds, ArrayList mcond, ArrayList mValues, ArrayList endVals,
            String staticDimValue, String dimCalOperator, String factFormula) {
//        ProgenLog.log(ProgenLog.FINE,this,"insertParameterizedFormula","Entered method "+measEleId+" "+dimBussTableId+" "+paramFilterName+" "+qualDimColName+""+dimType);
        logger.info("Entered method " + measEleId + " " + dimBussTableId + " " + paramFilterName + " " + qualDimColName + "" + dimType);
        ArrayList<String> qryList = new ArrayList<String>();
        String finalinsertUserAllInfoQry = null;
        String finalinsertSubFolderElementsQry = null;
        String finalupdateEleIdQry = null;
        PbDb pbDb = new PbDb();
        PbReturnObject pbRetObj;
        PbReturnObject retObj;
        String elementId = null;
        int ref_elmntId = 0;
        String caseStmt = "";

        String insertUserAllInfoQry = getResourceBundle().getString("insertUserAllInfo");
        String insertSubFolderElementsQry = getResourceBundle().getString("insertSubFolderElements");

        String getDbTablColQry = getResourceBundle().getString("getDBTableColumn");
        String pkeyQry;
        String measDbTable = null;
        String measDbCol = null;
        String bussColName;
        String actulaFormula = null;
        String userColType = null;
        String aggType = null;

        bussColName = paramFilterName;
//        bussColName = bussColName.replace("#", "_");
//        bussColName = bussColName.replace("&", "_");
//        bussColName = bussColName.replace("!", "_");
//        bussColName = bussColName.replace("@", "_");
//        bussColName = bussColName.replace("(", "_");
//        bussColName = bussColName.replace(")", "_");
//        bussColName = bussColName.replace("[", "_");
//        bussColName = bussColName.replace("]", "_");
//        bussColName = bussColName.replace("{", "_");
//        bussColName = bussColName.replace("}", "_");
//        bussColName = bussColName.replace(" ", "_");

//        ProgenLog.log(ProgenLog.FINE,this,"insertParameterizedFormula","Business Column Name "+bussColName);
        logger.info("Business Column Name " + bussColName);
        String getActualfFormula = getResourceBundle().getString("getActualfFormula");


        try {
            Object[] formulaObj = new Object[1];
            formulaObj[0] = measEleId.replace("A_", "");
            getActualfFormula = buildQuery(getActualfFormula, formulaObj);
            retObj = pbDb.execSelectSQL(getActualfFormula);
            if (retObj.getRowCount() > 0) {
                actulaFormula = retObj.getFieldValueString(0, 0);
                userColType = retObj.getFieldValueString(0, 1);
                aggType = retObj.getFieldValueString(0, 2);
                ///
            }
            Object[] qryBind = new Object[1];
            qryBind[0] = measEleId.replace("A_", "");
            String id = measEleId.replace("A_", ""); //element_id in where clause
            getDbTablColQry = buildQuery(getDbTablColQry, qryBind);
            pbRetObj = pbDb.execSelectSQL(getDbTablColQry);
            String qry1 = "SELECT USER_COL_TYPE,ACTUAL_COL_FORMULA,CONNECTION_ID FROM PRG_USER_ALL_INFO_DETAILS  WHERE ELEMENT_ID= " + id;

            PbReturnObject retObj1 = pbDb.execSelectSQL(qry1);
            String colType = retObj1.getFieldValueString(0, 0);
            String actualFrom = retObj1.getFieldValueString(0, 1);
            String connectionId = retObj1.getFieldValueString(0, 2);
            if (actualFrom.startsWith("'")) {
                if (actualFrom.endsWith("'")) {
                    actualFrom = actualFrom.substring(1, actualFrom.length() - 1);
                }
            }

            if (pbRetObj.getRowCount() > 0) {
                measDbTable = pbRetObj.getFieldValueString(0, 0);
                if (pbRetObj.getFieldValueString(0, 2).equalsIgnoreCase("calculated")) {
                    measDbCol = pbRetObj.getFieldValueString(0, 3);
                } else {
                    measDbCol = pbRetObj.getFieldValueString(0, 1);
                }
            }
            if (dimElementId == null) {
                dimElementId = measEleId.replace("A_", "");
            }
            if (type != null && type.equalsIgnoreCase("facts")) {//for facts
                if (factFormula.length() == 0) {
                    caseStmt = " case when ";
                    String sqlqry = null;
                    PbReturnObject returnObj = new PbReturnObject();
                    for (int i = 0; i < mIds.size(); i++) {
                        dimMembers = "";
                        if (String.valueOf(mValues.get(i)).contains(",")) {
                            String[] val = String.valueOf(mValues.get(i)).split(",");
                            StringBuffer str = new StringBuffer();
                            for (int v = 0; v < val.length; v++) {
                                val[v] = "''" + val[v] + "''";
                                str.append(",").append(val[v]);
                            }
                            dimMembers = str.substring(1);
                        } else {
                            if (mValues != null && !mValues.isEmpty() && mValues.get(i) != "") {
                                dimMembers = "''" + (String) mValues.get(i) + "''";
                            }
                        }
                        String sql = "select buss_col_name from prg_user_all_info_details where element_id=&";
                        Object[] obj = new Object[1];
                        obj[0] = mIds.get(i);
                        sqlqry = buildQuery(sql, obj);
                        returnObj = pbDb.execSelectSQL(sqlqry);
                        if (returnObj.getRowCount() != 0) {
                            if (i == 0) {
                                if (String.valueOf(mcond.get(i)).equalsIgnoreCase("between")) {
                                    String evalue = "''" + (String) endVals.get(i) + "''";
                                    caseStmt += qualDimColName + "." + returnObj.getFieldValueString(0, 0) + " " + String.valueOf(mcond.get(i)) + " (" + dimMembers + ") and " + "(" + evalue + ") ";
                                } else //commentes by nazneen
                                //                         if(dimMembers != null && !dimMembers.isEmpty() && mcond.get(i) != "")
                                // if(String.valueOf(mcond.get(i)).equals("is null") || String.valueOf(mcond.get(i)).equals("is not null"))
                                // dimMembers="''" +(String) mValues.get(i)+ "''";
                                ///// anitha lucky
                                //commented By Nazneen
                                //                            dimMembers="''" +(String) mValues.get(i)+ "''";
                                if (dimMembers != null && !dimMembers.isEmpty() && mValues.get(i) != "") {
                                    caseStmt += qualDimColName + "." + returnObj.getFieldValueString(0, 0) + " " + String.valueOf(mcond.get(i)) + " (" + dimMembers + ") ";
                                } else {
                                    caseStmt += qualDimColName + "." + returnObj.getFieldValueString(0, 0) + " " + String.valueOf(mcond.get(i) + " ");
                                }
                            } else {
                                ////// lucky
                                if (dimMembers != null && !dimMembers.isEmpty()) // if(dimMembers != null && !dimMembers.isEmpty() && mcond.get(i) != "")
                                // if(String.valueOf(mcond.get(i)).equals("is null") || String.valueOf(mcond.get(i)).equals("is not null"))
                                // dimMembers="''" +(String) mValues.get(i)+ "''";
                                {
                                    caseStmt += String.valueOf(conditions.get(i)) + " " + qualDimColName + "." + returnObj.getFieldValueString(0, 0) + " " + String.valueOf(mcond.get(i)) + " (" + dimMembers + ") ";
                                } else {
                                    caseStmt += String.valueOf(conditions.get(i)) + " " + qualDimColName + "." + returnObj.getFieldValueString(0, 0) + " " + String.valueOf(mcond.get(i)) + " ";
                                }

                            }
                        }
                    }
                    if (colType.equalsIgnoreCase("calculated")) {
                        caseStmt += "then " + actualFrom.replace("'", "''") + " else null end";
                    } else {
                        caseStmt += "then " + measDbTable + "." + measDbCol + " else null end";
                    }
                    // 
                } else {
                    caseStmt = factFormula;
                }
            } else {// for dimension
                if (colType.equalsIgnoreCase("calculated")) {
                    caseStmt = " case when " + qualDimColName + " " + dimType + " (" + dimMembers + ") then " + actualFrom.replace("'", "''") + " else null end";
                } else {
                    if (staticDimValue != "" && staticDimValue != null)// if static value selected in dim
                    {
                        caseStmt = " case when " + qualDimColName + " " + dimType + " (" + dimMembers + ") then " + staticDimValue + " else null end";
                    } else {
                        caseStmt = " case when " + qualDimColName + " " + dimType + " (" + dimMembers + ") then " + measDbTable + "." + measDbCol + " else null end";
                    }

                }
                if (dimCalOperator != null) {
                    if (aggType.equalsIgnoreCase("count")) {
                        caseStmt = "(case when " + (measDbTable + "." + measDbCol) + " is null then 0 else " + aggType + "(" + (measDbTable + "." + measDbCol) + ")" + " end)";
                    } else if (aggType.equalsIgnoreCase("COUNTDISTINCT")) {
                        caseStmt = "(case when " + (measDbTable + "." + measDbCol) + " is null then 0 else " + "count" + "( distinct " + (measDbTable + "." + measDbCol) + ")" + " end)";
                    } else {
                        caseStmt = "(case when " + (measDbTable + "." + measDbCol) + " is null then 0 else " + (measDbTable + "." + measDbCol) + " end)";
                    }
                    if (dimCalOperator.equalsIgnoreCase("add")) {
                        caseStmt += " + ";
                    } else if (dimCalOperator.equalsIgnoreCase("sub")) {
                        caseStmt += " - ";
                    } else if (dimCalOperator.equalsIgnoreCase("mul")) {
                        caseStmt += " * ";
                    } else if (dimCalOperator.equalsIgnoreCase("div")) {
                        caseStmt += " / ";
                    }

                    if (dimCalOperator.equalsIgnoreCase("div")) {
                        caseStmt += "(case when " + qualDimColName + " = 0 then null else " + qualDimColName + "*1" + " end)";
                    } else {
                        caseStmt += "(case when " + qualDimColName + " is null then 0 else " + qualDimColName + "*1" + " end)";
                    }
                }
                // 

                // caseStmt =" case when "+qualDimColName+" in ("+dimMembers+") then "+measDbTable+"."+measDbCol+" else null end";
                if (type != null && type.equalsIgnoreCase("ConversionFormula")) {//for conversionformula
                    dimElementId = measEleId.replace("A_", "");
                    //added by Nazneen for Progen Standard Date Diff
                    if (dimMembers.contains("@@")) {
                        String sourceDbType = "";
                        ConnectionDAO connectionDAO = new ConnectionDAO();
                        ConnectionMetadata conMetadata;
                        conMetadata = connectionDAO.getConnectionByConId(connectionId);
                        sourceDbType = conMetadata.getDbType();
                        if (userColType.equalsIgnoreCase("calculated")) {

                            if (sourceDbType.equalsIgnoreCase("SQLSERVER")) {
                                caseStmt = actulaFormula.replace("'", "''") + "/ DATEDIFF(day,@PROGENTIME@@ED_DATE,@PROGENTIME@@ST_DATE)";
                            } else if (sourceDbType.equalsIgnoreCase("MYSQL")) {
                                caseStmt = actulaFormula.replace("'", "''") + "/ DATEDIFF(@PROGENTIME@@ED_DATE,@PROGENTIME@@ST_DATE)";
                            } else {
                                caseStmt = actulaFormula.replace("'", "''") + "/ (TO_NUMBER(@PROGENTIME@@ED_DATE - @PROGENTIME@@ST_DATE)+1)";
                            }
                        } else {
                            if (sourceDbType.equalsIgnoreCase("SQLSERVER")) {
                                caseStmt = qualDimColName + "/ DATEDIFF(day,@PROGENTIME@@ED_DATE,@PROGENTIME@@ST_DATE)";
                            } else if (sourceDbType.equalsIgnoreCase("MYSQL")) {
                                caseStmt = qualDimColName + "/ DATEDIFF(@PROGENTIME@@ED_DATE,@PROGENTIME@@ST_DATE)";
                            } else {
                                caseStmt = qualDimColName + "/ (TO_NUMBER(@PROGENTIME@@ED_DATE - @PROGENTIME@@ST_DATE)+1)";
                            }
                        }
                    } else {
                        if (userColType.equalsIgnoreCase("calculated")) {
                            caseStmt = actulaFormula.replace("'", "''") + "/" + dimMembers;
                        } else {
                            caseStmt = qualDimColName + "/" + dimMembers;
                        }
                    }

                }
            }

//           written by veena
//           if(colType.equalsIgnoreCase("calculated")){
//                caseStmt =" case when "+qualDimColName+" in ("+dimMembers+") then "+actualFrom.replace("'", "''") +" else null end";
//
//            }else{
//              caseStmt =" case when "+qualDimColName+" "+dimType+" ("+dimMembers+") then "+measDbTable+"."+measDbCol+" else null end";
//            }
            // caseStmt =" case when "+qualDimColName+" in ("+dimMembers+") then "+measDbTable+"."+measDbCol+" else null end";

            UserLayerDAO userDAO = new UserLayerDAO();
            HashMap childMeasElmnts = userDAO.getPriorAndChangeMeasures(measEleId.replace("A_", ""));
            String columnNamesList[] = new String[4];
            columnNamesList[0] = bussColName;
            columnNamesList[1] = "Prior_" + bussColName;
            columnNamesList[2] = "Change_" + bussColName;
            columnNamesList[3] = "Change%_" + bussColName;

            String columnDescList[] = new String[4];
            columnDescList[0] = paramFilterName.replace("_", " ");
            columnDescList[1] = "Prior " + paramFilterName;
            columnDescList[2] = "Change " + paramFilterName;
            columnDescList[3] = "Change% " + paramFilterName;
//            columnDescList[0] = paramFilterName.replace("_", " ");
//            columnDescList[1] = "Prior " + paramFilterName.replace("_", " ");
//            columnDescList[2] = "Change " + paramFilterName.replace("_", " ");
//            columnDescList[3] = "Change% " + paramFilterName.replace("_", " ");
            String refEleTypes[] = new String[4];
            refEleTypes[0] = "1";
            refEleTypes[1] = "2";
            refEleTypes[2] = "3";
            refEleTypes[3] = "4";
            int childElmnts = 1;
            if (childMeasElmnts.size() > 0) {
                childElmnts = 4;
            }

            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.ORACLE)) {
                //find the value of pkey
                for (int i = 0; i < childElmnts; i++) {
                    pkeyQry = getResourceBundle().getString("getSubFldrElePkey");
                    pbRetObj = pbDb.execSelectSQL(pkeyQry);
                    elementId = String.valueOf(pbRetObj.getFieldValueInt(0, 0));
                    if (i == 0) {
                        ref_elmntId = Integer.parseInt(elementId);
                    }
//                    if(columnNamesList[i].length()>30){
//                        columnNamesList[i]=columnNamesList[i].substring(0,30);
//                        columnDescList[i]=columnDescList[i].substring(0,30);
//                    }
                    qryBind = new Object[13];
                    qryBind[0] = elementId; //pkey element_id
                    qryBind[1] = 0;//buss_col_id
                    qryBind[2] = columnNamesList[i]; //buss_col_name
                    qryBind[3] = "E_" + elementId; //user_col_name
                    qryBind[4] = columnDescList[i]; //user_col_desc
                    qryBind[5] = "calculated"; //user_col_type
                    qryBind[6] = ref_elmntId; //ref_element_id
                    qryBind[7] = refEleTypes[i]; //ref_element_type
                    qryBind[8] = measEleId.replace("A_", ""); //referred_elements
                    qryBind[9] = caseStmt; //actual_formula
                    qryBind[10] = caseStmt; //display_formula
                    qryBind[11] = dimBussTableId; //display_formula
                    qryBind[12] = measEleId.replace("A_", ""); //element_id in where clause
                    finalinsertSubFolderElementsQry = buildQuery(insertSubFolderElementsQry, qryBind);


                    qryBind = new Object[14];
                    qryBind[0] = elementId; //pkey
                    qryBind[1] = 0; //buss_col_id
                    qryBind[2] = columnNamesList[i]; //buss_col_name
                    qryBind[3] = "E_" + elementId; //user_col_name
                    qryBind[4] = columnDescList[i]; //user_col_desc
                    qryBind[5] = "calculated"; //user_col_type
                    qryBind[6] = ref_elmntId; //ref_element_id
                    qryBind[7] = refEleTypes[i]; //ref_element_type
                    qryBind[8] = caseStmt; //actual_formula
                    qryBind[9] = measEleId.replace("A_", ""); //referred_elements
                    qryBind[10] = caseStmt; //display_formula
                    qryBind[11] = dimBussTableId; //display_formula
                    qryBind[12] = dimElementId;
                    qryBind[13] = measEleId.replace("A_", "");//element_id in where clause
                    finalinsertUserAllInfoQry = buildQuery(insertUserAllInfoQry, qryBind);

//                    ProgenLog.log(ProgenLog.FINE, this, "insertParameterizedFormula", "Insert Query1 " + finalinsertUserAllInfoQry);
                    logger.info("Insert Query1 " + finalinsertUserAllInfoQry);
//                    ProgenLog.log(ProgenLog.FINE, this, "insertParameterizedFormula", "Insert Query2 " + finalinsertSubFolderElementsQry);
                    logger.info("Insert Query2 " + finalinsertSubFolderElementsQry);
                    qryList.add(finalinsertUserAllInfoQry);
                    qryList.add(finalinsertSubFolderElementsQry);

                }
            } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                String updateEleIdQry = getResourceBundle().getString("updateRefElement");
                for (int i = 0; i < childElmnts; i++) {

//                    if(columnNamesList[i].length()>30){
//                        columnNamesList[i]=columnNamesList[i].substring(0,30);
//                        columnDescList[i]=columnDescList[i].substring(0,30);
//                    }

                    qryBind = new Object[11];
                    qryBind[0] = 0;//buss_col_id
                    qryBind[1] = columnNamesList[i]; //buss_col_name
                    qryBind[2] = "E_" + columnNamesList[i]; //user_col_name
                    qryBind[3] = columnDescList[i]; //user_col_desc
                    //
                    qryBind[4] = "calculated"; //user_col_type
                    qryBind[5] = refEleTypes[i]; //ref_element_type
                    qryBind[6] = measEleId.replace("A_", ""); //referred_elements
                    qryBind[7] = caseStmt; //actual_formula
                    qryBind[8] = caseStmt; //display_formula
                    qryBind[9] = dimBussTableId; //display_formula
                    qryBind[10] = measEleId.replace("A_", ""); //element_id in where clause
                    finalinsertSubFolderElementsQry = buildQuery(insertSubFolderElementsQry, qryBind);

                    if (i == 0) {
                        ref_elmntId = pbDb.insertAndGetSequenceInSQLSERVER(finalinsertSubFolderElementsQry, "PRG_USER_SUB_FOLDER_ELEMENTS");
                        elementId = String.valueOf(ref_elmntId);
                    } else {
                        elementId = String.valueOf(pbDb.insertAndGetSequenceInSQLSERVER(finalinsertSubFolderElementsQry, "PRG_USER_SUB_FOLDER_ELEMENTS"));
                    }
                    qryBind = new Object[14];
                    qryBind[0] = elementId; //element_id
                    qryBind[1] = 0; //buss_col_id
                    qryBind[2] = columnNamesList[i]; //buss_col_name
                    qryBind[3] = "E_" + elementId; //user_col_name
                    qryBind[4] = columnDescList[i]; //user_col_desc
                    qryBind[5] = "calculated"; //user_col_type
                    qryBind[6] = ref_elmntId; //ref_element_id
                    qryBind[7] = refEleTypes[i]; //ref_element_type
                    qryBind[8] = caseStmt; //actual_formula
                    qryBind[9] = measEleId.replace("A_", ""); //referred_elements
                    qryBind[10] = caseStmt; //display_formula
                    qryBind[11] = dimBussTableId; //display_formula
                    qryBind[12] = dimElementId;
                    qryBind[13] = measEleId.replace("A_", "");//element_id in where clause
                    finalinsertUserAllInfoQry = buildQuery(insertUserAllInfoQry, qryBind);

                    qryBind = new Object[2];
                    qryBind[0] = ref_elmntId; //ref_element_id
                    qryBind[1] = elementId; //element_id
                    finalupdateEleIdQry = buildQuery(updateEleIdQry, qryBind);

                    qryBind = new Object[2];
                    qryBind[0] = "E_" + elementId;
                    qryBind[1] = elementId;
                    String updateuserColnameQry = "update PRG_USER_SUB_FOLDER_ELEMENTS set USER_COL_NAME='&' where ELEMENT_ID='&'";
                    String finalupdateusercolqry = buildQuery(updateuserColnameQry, qryBind);

                    qryList.add(finalinsertUserAllInfoQry);
                    qryList.add(finalupdateEleIdQry);
                    qryList.add(finalupdateusercolqry);

//                    ProgenLog.log(ProgenLog.FINE, this, "insertParameterizedFormula", "Insert Querylist :" + qryList);
                    logger.info("Insert Querylist :" + qryList);
//                    ProgenLog.log(ProgenLog.FINE, this, "insertParameterizedFormula", "Insert Query2 " + finalinsertSubFolderElementsQry);
//                    ProgenLog.log(ProgenLog.FINE, this, "updateEleIdQry", "Update Qry " + updateEleIdQry);

//                    qryList.add(updateEleIdQry);

//                executeMultiple(qryList);

//                    pkeyQry = getResourceBundle().getString("getSubFldrElePkey");
//                    pbRetObj = pbDb.execSelectSQL(pkeyQry);
//                    elementId = String.valueOf(pbRetObj.getFieldValueInt(0, 0));
                }
            } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                String updateEleIdQry = getResourceBundle().getString("updateRefElement");
                for (int i = 0; i < childElmnts; i++) {

//                    if(columnNamesList[i].length()>30){
//                        columnNamesList[i]=columnNamesList[i].substring(0,30);
//                        columnDescList[i]=columnDescList[i].substring(0,30);
//                    }

                    qryBind = new Object[11];
                    qryBind[0] = 0;//buss_col_id
                    qryBind[1] = columnNamesList[i]; //buss_col_name
                    qryBind[2] = "E_" + columnNamesList[i]; //user_col_name
                    qryBind[3] = columnDescList[i]; //user_col_desc
                    //
                    qryBind[4] = "calculated"; //user_col_type
                    qryBind[5] = refEleTypes[i]; //ref_element_type
                    qryBind[6] = measEleId.replace("A_", ""); //referred_elements
                    qryBind[7] = caseStmt; //actual_formula
                    qryBind[8] = caseStmt; //display_formula
                    qryBind[9] = dimBussTableId; //display_formula
                    qryBind[10] = measEleId.replace("A_", ""); //element_id in where clause
                    finalinsertSubFolderElementsQry = buildQuery(insertSubFolderElementsQry, qryBind);

                    if (i == 0) {
                        ref_elmntId = pbDb.insertAndGetSequenceInMySql(finalinsertSubFolderElementsQry, "PRG_USER_SUB_FOLDER_ELEMENTS", "ELEMENT_ID");
                        elementId = String.valueOf(ref_elmntId);
                    } else {
                        elementId = String.valueOf(pbDb.insertAndGetSequenceInMySql(finalinsertSubFolderElementsQry, "PRG_USER_SUB_FOLDER_ELEMENTS", "ELEMENT_ID"));
                    }
                    qryBind = new Object[14];
                    qryBind[0] = elementId; //element_id
                    qryBind[1] = 0; //buss_col_id
                    qryBind[2] = columnNamesList[i]; //buss_col_name
                    qryBind[3] = "E_" + elementId; //user_col_name
                    qryBind[4] = columnDescList[i]; //user_col_desc
                    qryBind[5] = "calculated"; //user_col_type
                    qryBind[6] = ref_elmntId; //ref_element_id
                    qryBind[7] = refEleTypes[i]; //ref_element_type
                    qryBind[8] = caseStmt; //actual_formula
                    qryBind[9] = measEleId.replace("A_", ""); //referred_elements
                    qryBind[10] = caseStmt; //display_formula
                    qryBind[11] = dimBussTableId; //display_formula
                    qryBind[12] = dimElementId;
                    qryBind[13] = measEleId.replace("A_", "");//element_id in where clause
                    finalinsertUserAllInfoQry = buildQuery(insertUserAllInfoQry, qryBind);

                    qryBind = new Object[2];
                    qryBind[0] = ref_elmntId; //ref_element_id
                    qryBind[1] = elementId; //element_id
                    finalupdateEleIdQry = buildQuery(updateEleIdQry, qryBind);

                    qryBind = new Object[2];
                    qryBind[0] = "E_" + elementId;
                    qryBind[1] = elementId;
                    String updateuserColnameQry = "update PRG_USER_SUB_FOLDER_ELEMENTS set USER_COL_NAME='&' where ELEMENT_ID='&'";
                    String finalupdateusercolqry = buildQuery(updateuserColnameQry, qryBind);

                    qryList.add(finalinsertUserAllInfoQry);
                    qryList.add(finalupdateEleIdQry);
                    qryList.add(finalupdateusercolqry);

//                    ProgenLog.log(ProgenLog.FINE, this, "insertParameterizedFormula", "Insert Querylist :" + qryList);
                    logger.info("Insert Querylist :" + qryList);
//                    ProgenLog.log(ProgenLog.FINE, this, "insertParameterizedFormula", "Insert Query2 " + finalinsertSubFolderElementsQry);
//                    ProgenLog.log(ProgenLog.FINE, this, "updateEleIdQry", "Update Qry " + updateEleIdQry);

//                    qryList.add(updateEleIdQry);

//                executeMultiple(qryList);

//                    pkeyQry = getResourceBundle().getString("getSubFldrElePkey");
//                    pbRetObj = pbDb.execSelectSQL(pkeyQry);
//                    elementId = String.valueOf(pbRetObj.getFieldValueInt(0, 0));
                }
            }
            executeMultiple(qryList);

        }catch (SQLException ex) {
            logger.error("Parameter Filter Insertion Failed " + ex.toString());
        } catch (Exception ex) {
            logger.error("Parameter Filter Insertion Failed " + ex.toString());
        }//        ProgenLog.log(ProgenLog.FINE,this,"insertParameterizedFormula","Inserted new Element Id "+elementId);
        logger.info("Inserted new Element Id " + elementId);
        return String.valueOf(ref_elmntId);
    }

    public void saveMapValues(double latval, double lngval, String address, Container container) {
//        PbReturnObject pbret = new PbReturnObject();
//         PbReturnObject pbret1 = new PbReturnObject();
        String dimension = null;
        if (container != null) {
            dimension = container.getDisplayColumns().get(0);
        }
        String dimenvalue = dimension.substring(2);
        Connection con = null;
        con = ProgenConnection.getInstance().getConnectionForElement(dimenvalue);
//        PbDb pbdb = new PbDb();
        //int one=Integer.parseInt(latval);
        Object[] maplocvalue = new Object[3];
        maplocvalue[0] = latval;
        maplocvalue[1] = lngval;
        maplocvalue[2] = address;
        // maplocvalue[1] = Integer.parseInt(lngval);
        String finalQuery = null;
        //String value[]="";
        try {
            String updatemapvalue = getResourceBundle().getString("updateMapValues");

            finalQuery = buildQuery(updatemapvalue, maplocvalue);
            ArrayList queryList = new ArrayList();
            queryList.add(finalQuery);

//         boolean check= executeMultiple(queryList,con);
            executeMultiple(queryList, con);
            con = null;
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {

                    logger.error("Exception:", ex);
                }
            }
        }


    }

    public void resetReportReferences(Integer newReportId, Integer reportId) {
        String dataSnapShotReportId = getResourceBundle().getString("updateDataSnapshotReportId");
        String dashbordDetailReportId = getResourceBundle().getString("updateDashboardDetails");
        String kpiDetailReportId = getResourceBundle().getString("updateKpiDetail");
        String repScheduler = getResourceBundle().getString("updateReportScheduler");
        ArrayList queryList = new ArrayList();
        Object obj[] = new Object[2];
        obj[0] = newReportId;
        obj[1] = reportId;
        String dataSnapShotQry = buildQuery(dataSnapShotReportId, obj);
        queryList.add(dataSnapShotQry);
        String dashboardQry = buildQuery(dashbordDetailReportId, obj);
        queryList.add(dashboardQry);
        String kpiDetailQry = buildQuery(kpiDetailReportId, obj);
        queryList.add(kpiDetailQry);
        String repSchedQry = buildQuery(repScheduler, obj);
        queryList.add(repSchedQry);
        executeMultiple(queryList);
    }
    //@Jaya

    public PbReturnObject getReferredElementsofMeasure(String measElementId) {
        String sql = "select REFFERED_ELEMENTS,AGGREGATION_TYPE from prg_user_all_info_details where element_id=" + Integer.parseInt(measElementId);
        PbReturnObject retObj = new PbReturnObject();
//        String  reffered_elements = "";
        try {
            retObj = execSelectSQL(sql);

        } catch (SQLException ex) {

            logger.error("Exception:", ex);
        }
        return retObj;
    }

    public static void main(String[] a) {
        PbReportViewerDAO DAO = new PbReportViewerDAO();
        DAO.createCustomizeReport("123", "Test", "Test");
    }

    public String getFormulaForMeasure(String elementId) {
        String formulaQuery = getResourceBundle().getString("getFormulaForMeasure");
        PbReturnObject formulaObj = null;
        StringBuilder formulaData = new StringBuilder();
        String formula = "";
        String bussColName = "";
        String aggType = "";
        String actualColFormula = "";
        String userColType = "";
        String prePost = "";
        Object[] obj = new Object[1];

        try {
            obj[0] = elementId.replace("A_", "");
            formulaObj = execSelectSQL(buildQuery(formulaQuery, obj));
            if (formulaObj.getRowCount() > 0) {
                formula = formulaObj.getFieldValueString(0, 0);
            }
            bussColName = formulaObj.getFieldValueString(0, 1);
            aggType = formulaObj.getFieldValueString(0, 2);
            actualColFormula = formulaObj.getFieldValueString(0, 3);
            userColType = formulaObj.getFieldValueString(0, 4);
            prePost = formulaObj.getFieldValueString(0, 5);
        } catch (SQLException ex) {

            logger.error("Exception:", ex);
        }
        if (prePost == null || prePost.equalsIgnoreCase("") || prePost.equalsIgnoreCase("null")) {
            prePost = "post";
        }
        formulaData.append(formula).append(",,").append(bussColName).append(",,").append(aggType).append(",,").append(actualColFormula).append(",,").append(userColType).append(",,").append(prePost);
//        return formula;
        return formulaData.toString();
    }

    public LinkedHashMultimap<String, String> getQuarterDetails(String elementId, String levelType) {
        Connection con = null;


        String finalQuery = "select distinct cq_cust_name,cq_st_date,cq_end_date from pr_day_denom order by cq_st_date asc";
        if (levelType.equalsIgnoreCase("Quarter")) {
            finalQuery = "select distinct cq_cust_name,cq_st_date,cq_end_date from pr_day_denom order by cq_st_date asc";
        } else if (levelType.equalsIgnoreCase("Month")) {
            finalQuery = "select distinct cm_cust_name,cm_st_date,cm_end_date from pr_day_denom order by cm_st_date asc";
        } else if (levelType.equalsIgnoreCase("Week")) {
            finalQuery = "select distinct cw_cust_name,cw_st_date,cw_end_date from pr_day_denom order by cw_st_date asc";
        }
        LinkedHashMultimap<String, String> measMap = LinkedHashMultimap.create();
        PbReturnObject retObj = null;
        try {
            con = ProgenConnection.getInstance().getConnectionForElement(elementId);
            if (con != null) {
                retObj = execSelectSQL(finalQuery, con);
                if (retObj != null && retObj.rowCount > 0) {
                    for (int i = 0; i < retObj.rowCount; i++) {
                        measMap.put(retObj.getFieldValueString(i, 0), retObj.getFieldValueDateString(i, 1));
                        measMap.put(retObj.getFieldValueString(i, 0), retObj.getFieldValueDateString(i, 2));
                    }
                }
            }
        } catch (Exception ex) {

            logger.error("Exception:", ex);
        }
        return measMap;
    }

    public List<String> getLevelDetailsForDiscrete(String elementId, String levelType) {
        List<String> levelList = new ArrayList<String>();
        String finalQuery = "select distinct cyear from pr_day_denom order by cyear asc";
        if (levelType.equalsIgnoreCase("Year")) {
            finalQuery = "select distinct cyear from pr_day_denom order by cyear asc";
        } else if (levelType.equalsIgnoreCase("Quarter")) {
            finalQuery = "select distinct cq_end_date from pr_day_denom order by cq_end_date asc";
        } else if (levelType.equalsIgnoreCase("Month")) {
            finalQuery = "select distinct cm_end_date from pr_day_denom order by cm_end_date asc";
        }
        PbReturnObject pbretObj = null;
        Connection con = null;
        try {
            con = ProgenConnection.getInstance().getConnectionForElement(elementId);
            if (con != null) {
                pbretObj = execSelectSQL(finalQuery, con);
                if (pbretObj != null && pbretObj.rowCount > 0 && levelType.equalsIgnoreCase("Year")) {
                    for (int i = 0; i < pbretObj.rowCount; i++) {
                        String year = pbretObj.getFieldValueString(i, 0);
                        PbReturnObject ret = new PbReturnObject();
                        con = ProgenConnection.getInstance().getConnectionForElement(elementId);
                        ret = execSelectSQL("select max(ddate) from pr_day_denom where cyear =" + year, con);
                        levelList.add(ret.getFieldValueDateString(0, 0));
                    }
                } else if (pbretObj != null && pbretObj.rowCount > 0 && (levelType.equalsIgnoreCase("Quarter") || levelType.equalsIgnoreCase("Month"))) {
                    for (int i = 0; i < pbretObj.rowCount; i++) {
                        levelList.add(pbretObj.getFieldValueDateString(i, 0));
                    }
                }
            }
        } catch (Exception ex) {

            logger.error("Exception:", ex);
        }

        return levelList;
    }

    public String buildGraphColumns(Container container, String graphId) {
        List<String> graphDetails = container.getCrossTabGraphColMap().get(graphId);
        int count = 0;
        ArrayList tempStr = null;
        ArrayList summmeas = new ArrayList();
        int measCnt = container.getReportMeasureCount();
        if (container.isSummarizedMeasuresEnabled() && container.getSummerizedTableHashMap() != null) {

            HashMap summarizedmMesMap = container.getSummerizedTableHashMap();
            summmeas.addAll((List<String>) summarizedmMesMap.get("summerizedQryeIds"));
            measCnt += summmeas.size();
        }
        for (int i = container.getViewByCount(); i < container.getViewByCount() + container.getReportMeasureCount(); i++) {
            tempStr = new ArrayList();
            tempStr = (ArrayList) container.getDisplayLabels().get(i + summmeas.size());
            if (graphDetails.get(1).equalsIgnoreCase((String) tempStr.get(tempStr.size() - 1))) {
                count = count + 1;
            }

        }
        if (count == 0) {
            container.getCrossTabGraphColMap().removeAll(graphId);
            tempStr = new ArrayList();
            tempStr = (ArrayList) container.getDisplayLabels().get(container.getViewByCount());
            container.getCrossTabGraphColMap().put(graphId, (String) container.getDisplayColumns().get(container.getViewByCount()));
            container.getCrossTabGraphColMap().put(graphId, tempStr.get(tempStr.size() - 1).toString());
        }
//         if(!graphDetails.contains(container.getDisplayColumns())&& !graphDetails.contains(container.getDisplayLabels())){
//                                    int whichMeasure=container.getViewByCount();
//                                    ArrayList LabelStr= (ArrayList) container.getDisplayLabels().get(whichMeasure);
//            graphDetails.add(0,  container.getDisplayColumns().get(whichMeasure).toString());
//            graphDetails.add(1 , LabelStr.get(LabelStr.size()-1).toString());
//        }
        StringBuffer graphBuffer = new StringBuffer("");
        container.getReportMeasureCount();

        if (container.getGraphCrossTabMeas() != null && !container.getGraphCrossTabMeas().isEmpty()) {
            ArrayList tempStr1 = new ArrayList();

            for (int i = container.getViewByCount(); i < measCnt + container.getViewByCount(); i++) {
                tempStr1 = (ArrayList) container.getDisplayLabels().get(i);
                if (container.getGraphCrossTabMeas().contains(tempStr1.get(tempStr1.size() - 1).toString())) {
                    graphBuffer.append(" <li id='GrpCol" + container.getDisplayColumns().get(i) + "' style='width:auto;height:auto;color:white' class='navtitle-hover'>");
                    graphBuffer.append("<table id='GrpTab" + container.getDisplayColumns().get(i) + "'>");
                    graphBuffer.append(" <tr>");
                    graphBuffer.append(" <td >");
                    graphBuffer.append("  <a href=\"javascript:deleteColumn('GrpCol" + container.getDisplayColumns().get(i) + "')\" class=\"ui-icon ui-icon-close\"></a>");
                    graphBuffer.append("</td>");
                    graphBuffer.append("<td id=\"eleName" + container.getDisplayColumns().get(i) + "\" style=\"color:black\">" + tempStr1.get(tempStr1.size() - 1) + "</td>");
                    graphBuffer.append("</tr>");
                    graphBuffer.append("</table>");
                    graphBuffer.append("</li>");
                }
            }
        } else {
            graphBuffer.append(" <li id='GrpCol" + graphDetails.get(0) + "' style='width:auto;height:auto;color:white' class='navtitle-hover'>");
            graphBuffer.append("<table id='GrpTab" + graphDetails.get(0) + "'>");
            graphBuffer.append(" <tr>");
            graphBuffer.append(" <td >");
            graphBuffer.append("  <a href=\"javascript:deleteColumn('GrpCol" + graphDetails.get(0) + "')\" class=\"ui-icon ui-icon-close\"></a>");
            graphBuffer.append("</td>");
            graphBuffer.append("<td id=\"eleName" + graphDetails.get(0) + "\" style=\"color:black\">" + graphDetails.get(1) + "</td>");
            graphBuffer.append("</tr>");
            graphBuffer.append("</table>");
            graphBuffer.append("</li>");
        }


        return graphBuffer.toString();
    }

    public ArrayList<Integer> setCustomViewSequence(String reportId, Container container) {
        ArrayList<Integer> customSeqList = new ArrayList<Integer>();
        ArrayList<Integer> normalSeqList = new ArrayList<Integer>();
        char[] sortTypes = new char[1];
        char[] sortDataTypes = new char[1];
        ArrayList<String> columnNames = new ArrayList<String>();
        String elementId = (String) container.getDisplayColumns().get(0);

        sortTypes[0] = 'A';
        sortDataTypes[0] = 'C';
        columnNames.add(elementId);
        ProgenDataSet dataSet = container.getRetObj();
        ArrayList<Integer> viewSequence = dataSet.sortDataSet(columnNames, sortTypes, sortDataTypes);

        for (String str : container.getReportCollect().getCustomSequence()) {
            for (int seq : viewSequence) {
                if (str.equalsIgnoreCase(dataSet.getFieldValueString(seq, elementId))) {
                    customSeqList.add(seq);
                    break;
                }

            }
        }
        for (int seq : viewSequence) {
            if (!customSeqList.contains(seq)) {
                normalSeqList.add(seq);
            }
        }
        viewSequence.clear();
        viewSequence.addAll(customSeqList);
        viewSequence.addAll(normalSeqList);
        container.getReportCollect().setCustomSequence((container.getReportCollect().getCustomSequence()));
        return viewSequence;

    }

    public void insertCreateKPIDetails(CreateKPIFromReport createKPIFromReport) {

        String sqlQuery = getResourceBundle().getString("insertCreateKPIDetails");
        String createkpistr = null;
        Gson gson = new Gson();
        Type tarType = new TypeToken<ArrayList<CreateKPIFromReport>>() {
        }.getType();
        Object[] obj = new Object[2];
        createkpistr = gson.toJson(createKPIFromReport);
        obj[0] = createkpistr;
        obj[1] = createKPIFromReport.getReportKPIName();
        String finalQry = buildQuery(sqlQuery, obj);
        try {
            execModifySQL(finalQry);
        } catch (Exception ex) {

            logger.error("Exception:", ex);
        }

    }
//    public ReportSchedule getPeriodType(String snapshotId){
//        String qry=getResourceBundle().getString("getSchedulerDetails");
//        Object[] obj=new Object[1];
//        obj[0]=snapshotId;
//        String finalqry=buildQuery(qry, obj);
//        PbReturnObject retobj=new PbReturnObject();
//        ReportSchedule schedule = null;
//        try{
//        retobj=execSelectSQL(finalqry);
//        Gson json=new Gson();
//        schedule=json.fromJson(retobj.getFieldValueString(0,"SCHEDULER_DETAILS"), ReportSchedule.class);
//        }
//    catch(Exception ex){
//        logger.error("Exception:",ex);
//    }
//        if(schedule!=null)
//            return schedule;
//        else
//        return null;
//    }

    public PbReturnObject getScheduledNames(String reportId, String userId) {
        String qry = getResourceBundle().getString("getSchedulerNames");
        Object[] obj = new Object[2];
        obj[0] = reportId;
        obj[1] = userId;
        String finalqry = buildQuery(qry, obj);
        PbReturnObject retobj = new PbReturnObject();
        try {
            retobj = execSelectSQL(finalqry);
        } catch (Exception ex) {

            logger.error("Exception:", ex);
        }
        return retobj;
    }

    public ReportSchedule getSchedulerDetails(String schedulerId) {
        String qry = getResourceBundle().getString("getSchedulerDetails");
        Object[] obj = new Object[1];
        obj[0] = schedulerId;
        String finalqry = buildQuery(qry, obj);
        PbReturnObject retobj = new PbReturnObject();
        ReportSchedule schedule = null;
        try {
            retobj = execSelectSQL(finalqry);
            if (retobj != null && retobj.getRowCount() != 0) {
                Gson json = new Gson();
                java.lang.reflect.Type type = new TypeToken<List<ReportSchedule>>() {
                }.getType();
                List<ReportSchedule> scheduleList = null;
                if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                    scheduleList = json.fromJson(retobj.getFieldUnknown(0, 0), type);
                } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.ORACLE)) {
                    scheduleList = json.fromJson(retobj.getFieldValueString(0, "SCHEDULER_DETAILS"), type);
                } else {
                    scheduleList = json.fromJson(retobj.getFieldValueString(0, "scheduler_details"), type);
                }
                Date sdate = retobj.getFieldValueDate(0, "SCHEDULE_START_DATE");
                Date edate = retobj.getFieldValueDate(0, "SCHEDULE_END_DATE");
                if (scheduleList != null && !scheduleList.isEmpty()) {
                    for (ReportSchedule schedule1 : scheduleList) {
                        schedule = schedule1;
                        schedule.setStartDate(sdate);
                        schedule.setEndDate(edate);
                    }
                }
            }
        } 
        catch (SQLException ex) {
            logger.error("Exception:", ex);
        }catch (Exception ex) {
            logger.error("Exception:", ex);
        }        if (schedule != null) {
            return schedule;
        } else {
            return null;
        }
    }

    // addded By Ram 02Nov2015 for Dashboard schedule
    public ReportSchedule getSchedulerDetailsDashboard(String schedulerId) {
        String qry = getResourceBundle().getString("getSchedulerDetailsDashboard");
        Object[] obj = new Object[1];
        obj[0] = schedulerId;
        String finalqry = buildQuery(qry, obj);
        PbReturnObject retobj = new PbReturnObject();
        ReportSchedule schedule = null;
        try {
            retobj = execSelectSQL(finalqry);
            if (retobj != null && retobj.getRowCount() != 0) {
                Gson json = new Gson();
                java.lang.reflect.Type type = new TypeToken<List<ReportSchedule>>() {
                }.getType();
                List<ReportSchedule> scheduleList = null;
                if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                    scheduleList = json.fromJson(retobj.getFieldUnknown(0, 0), type);
                } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.ORACLE)) {
                    scheduleList = json.fromJson(retobj.getFieldValueString(0, "ALERTS_DETAILS"), type);
                } else {
                    scheduleList = json.fromJson(retobj.getFieldValueString(0, "alerts_details"), type);
                }
                Date sdate = retobj.getFieldValueDate(0, "ALERT_START_DATE");
                Date edate = retobj.getFieldValueDate(0, "ALERT_END_DATE");
                if (scheduleList != null && !scheduleList.isEmpty()) {
                    for (ReportSchedule schedule1 : scheduleList) {
                        schedule = schedule1;
                        schedule.setStartDate(sdate);
                        schedule.setEndDate(edate);
                    }
                }
            }
        }
        catch (SQLException ex) {
            logger.error("Exception:", ex);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }        if (schedule != null) {
            return schedule;
        } else {
            return null;
        }
    }

    public void deleteScheduledReport(String schedulerId) {
        String qry = getResourceBundle().getString("deleteSchedledReport");
        Object[] obj = new Object[1];
        obj[0] = schedulerId;
        String finalqry = buildQuery(qry, obj);
        try {
            execModifySQL(finalqry);
        } catch (Exception ex) {

            logger.error("Exception:", ex);
        }
    }
    //Added By Ram 02Nov2015 for delete Dashboard schedule

    public void deleteDashboardScheduledReport(String schedulerId) {
        String qry = getResourceBundle().getString("deleteDashboardSchedledReport");
        Object[] obj = new Object[1];
        obj[0] = schedulerId;
        String finalqry = buildQuery(qry, obj);
        try {
            execModifySQL(finalqry);
        } catch (Exception ex) {

            logger.error("Exception:", ex);
        }
    }

    public int getScheduleCount(String userId) {
        String qry = getResourceBundle().getString("getScheduleCount");
        Object[] obj = new Object[1];
        obj[0] = userId;
        String finalqry = buildQuery(qry, obj);
        PbReturnObject retobj = new PbReturnObject();
        try {
            retobj = execSelectSQL(finalqry);
        } catch (Exception ex) {

            logger.error("Exception:", ex);
        }
        return retobj.getRowCount();
    }

    public PbReturnObject getAllSchedulerdetails() {
        String qry = getResourceBundle().getString("getAllSchedulerDetails");
        PbReturnObject retObj = new PbReturnObject();
        try {
            retObj = execSelectSQL(qry);
        } catch (Exception e) {
            logger.error("Exception:", e);
        }
        return retObj;

    }

    public HashMap pbParamDefaultValues(String elementid, String reportid, HttpServletRequest request) throws Exception {
        HashMap map = new HashMap();
        Container container = null;
        String eledetsQuery = "select buss_col_name, buss_table_name, connection_id,member_id from prg_user_all_info_details where element_id=" + elementid;
        PbReturnObject pbro;

        pbro = super.execSelectSQL(eledetsQuery);
        String bussTableName = pbro.getFieldValueString(0, 1);
        String bussColName = pbro.getFieldValueString(0, 0);
        String connectionId = String.valueOf(pbro.getFieldValueInt(0, 2));
        String memberId = String.valueOf(pbro.getFieldValueInt(0, 3));
        String secureParamValues = "";
        String eledetsexistsQuery = "select member_value from PRG_AR_PARAMETER_SECURITY where element_id=" + elementid + "and report_id=" + reportid;
        PbReturnObject pbroexist = super.execSelectSQL(eledetsexistsQuery);
        if (pbroexist.getRowCount() > 0) {
            secureParamValues = String.valueOf(pbroexist.getFieldValueClobString(0, "MEMBER_VALUE"));
        }
        String elementvalQuery = "";
        if (secureParamValues.equalsIgnoreCase("")) {

            elementvalQuery = "select distinct " + bussColName + " from " + bussTableName + "  where " + bussColName + "  is not null";
        } else {
//         String securevaluesList="";
            StringBuilder securevaluesList = new StringBuilder();
            String secureParamValuesarr[] = secureParamValues.split(",");
            for (int i = 0; i < secureParamValuesarr.length; i++) {
//         securevaluesList+=",'"+secureParamValuesarr[i]+"'";
                securevaluesList.append(",'").append(secureParamValuesarr[i]).append("'");
            }
//        if(!securevaluesList.equalsIgnoreCase("")){
//        securevaluesList=securevaluesList.substring(1);
//        }
            if (securevaluesList.length() != 0) {
                securevaluesList = new StringBuilder(securevaluesList.toString().substring(1));
            }
            elementvalQuery = "select distinct " + bussColName + " from " + bussTableName + "  where " + bussColName + "  in(" + securevaluesList.toString() + ")";
        }

        elementvalQuery = elementvalQuery + " order by 1 ";
//         BusinessGroupDAO busgrpDao = new BusinessGroupDAO();

        PbReturnObject pbroval = null;
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        ArrayList list = new ArrayList();
//            ArrayList exlist=new ArrayList();
        HashMap hmap = new HashMap();
        try {
            con = ProgenConnection.getInstance().getConnectionByConId(connectionId);
            st = con.createStatement();
            rs = st.executeQuery(elementvalQuery);
            pbroval = new PbReturnObject(rs);
            for (int i = 0; i < pbroval.getRowCount(); i++) {
                list.add(pbroval.getFieldValueString(i, 0));
            }

            rs.close();
            rs = null;
            st.close();
            st = null;
            con.close();
            con = null;
        } catch (SQLException e) {
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (st != null) {
                    st.close();
                }
                if (con != null) {
                    con.close();
                }
                if (pbroval == null) {
                    pbroval = new PbReturnObject();
                }
            } catch (SQLException e) {
            }
        }

        List<String> existparamvalues = null;
        if (request.getSession(false) != null && request.getSession(false).getAttribute("PROGENTABLES") != null) {
            if (request.getSession(false).getAttribute("PROGENTABLES") != null) {
                map = (HashMap) request.getSession(false).getAttribute("PROGENTABLES");
            }
            if (map.get(reportid) != null) {
                container = (prg.db.Container) map.get(reportid);
            } else {
                container = new prg.db.Container();
            }

            if (container.getReportCollect().isParameterValueSet(elementid)) {
                existparamvalues = container.getReportCollect().getDefaultValue(elementid);
            }
//                  String paramvalues[]=existparamvalues.split(",");
//                  for(int i=0;i<paramvalues.length;i++)
//                  {
//                      String existparams=paramvalues[i];
//                      exlist.add(existparams);
//                       hmap.put("exlist", exlist);
//
//                  }
//                 exlist.clear();
//                   exlist.add(existparamvalues);
        }
        hmap.put("list", list);
        hmap.put("existparamvalues", existparamvalues);

        return hmap;
    }

    public PbReturnObject viewScheduleReportDetails(String userId) {
        PbReturnObject retObj = new PbReturnObject();
        String qry = getResourceBundle().getString("viewScheduleReports");
        Object[] obj = new Object[1];
        obj[0] = userId;
        String finalqry = buildQuery(qry, obj);
        try {
            retObj = execSelectSQL(finalqry);
        } catch (Exception ex) {

            logger.error("Exception:", ex);
        }
        return retObj;
    }

    public PbReturnObject getMeasureValues(String elementId, String factName) throws Exception {

        PbReturnObject retObj = new PbReturnObject();
        PbReturnObject retObj1 = new PbReturnObject();
        String qry = getResourceBundle().getString("getBussinessColName");
        String busscolName = null;
        String conId = null;
        Object[] obj = new Object[1];
        obj[0] = elementId;
        try {
            String finalqry = buildQuery(qry, obj);
            retObj = execSelectSQL(finalqry);
            if (retObj != null && retObj.getRowCount() != 0) {
                busscolName = retObj.getFieldValueString(0, 0);
                conId = retObj.getFieldValueString(0, 1);
                String sqlqry = "select distinct " + busscolName + " from " + factName + " order by " + busscolName;
                //            String valuesqry=getResourceBundle().getString("getMeasureValuesqry");
//            Object[] obj1=new Object[2];
//            obj1[0]= ;
//            obj1[1]= busscolName;
//            String sqlqry=buildQuery(valuesqry, obj1);
                //
                retObj1 = execSelectSQL(sqlqry, ProgenConnection.getInstance().getConnectionByConId(conId));
            }
        } catch (SQLException ex) {

            logger.error("Exception:", ex);
        }
        return retObj1;

    }

    public int checkMesureType(String measureId) {
        PbReturnObject retObj = new PbReturnObject();
        int rowCnt = 0;
        String sql = "select buss_table_name from prg_user_all_info_details where element_id=" + measureId + " and buss_table_name is not null and aggregation_type is not null and user_col_type not in ('TIMECALUCULATED','SUMMARIZED','summarized','summarised')";
        try {
            retObj = execSelectSQL(sql);
            String busTableName = retObj.getFieldValueString(0, 0);
            if (busTableName.equalsIgnoreCase("")) {
                rowCnt = 0;
            } else {
                rowCnt = retObj.getRowCount();
            }
        } catch (SQLException ex) {

            logger.error("Exception:", ex);
        }
        return rowCnt;
    }

    public int chkMsrforConversionFormula(String measureId) {
        PbReturnObject retObj = new PbReturnObject();
        int rowCnt = 0;
        String sql = getResourceBundle().getString("chkMsrforConversionFormula");
        Object[] obj = new Object[1];
        obj[0] = measureId;
        try {
            String finalqry = buildQuery(sql, obj);
            retObj = execSelectSQL(finalqry);
            String busTableName = retObj.getFieldValueString(0, 0);
            if (busTableName.equalsIgnoreCase("") && retObj.getFieldValueString(0, 1).equalsIgnoreCase("") && retObj.getFieldValueString(0, 1).equalsIgnoreCase("null")) {
                rowCnt = 0;
            } else {
                rowCnt = retObj.getRowCount();
            }
        } catch (SQLException ex) {

            logger.error("Exception:", ex);
        }
        return rowCnt;
    }

    public HashMap getViewbysRelatedReport(String reportId, String from) {
        PbReturnObject retObj = new PbReturnObject();
        PbReturnObject reportRetObj = new PbReturnObject();
        PbReturnObject viewRetObj = new PbReturnObject();
        String sql = "";
        if (from != null && from.equalsIgnoreCase("initializeReport")) {
            sql = getResourceBundle().getString("getViewbysRelatedReportForInitilizeReport");
        } else {
            sql = getResourceBundle().getString("getViewbysRelatedReport");
        }
        Object[] obj = new Object[1];
        obj[0] = reportId;
        String qry = buildQuery(sql, obj);
        ArrayList<String> viewbyNames = new ArrayList<String>();

        HashMap<String, ArrayList> viewbyRelatedReports = new HashMap<String, ArrayList>();
        HashMap<String, String> assignedMap = new HashMap<String, String>();
        HashMap<String, String> conditionMap = new HashMap<String, String>();
//        String jsonString=null;
        HashMap map = new HashMap();
        try {
            retObj = execSelectSQL(qry);
//            
            if (retObj.getRowCount() > 0) {
                for (int i = 0; i < retObj.getRowCount(); i++) {
                    viewbyNames.add(retObj.getFieldValueString(i, 0) + "&" + retObj.getFieldValueString(i, 1));
                    assignedMap.put(retObj.getFieldValueString(i, 1), retObj.getFieldValueString(i, 2));
                    conditionMap.put(retObj.getFieldValueString(i, 1), retObj.getFieldValueString(i, 3));
//                    
                    sql = getResourceBundle().getString("getReportNamesRelatedViewBy");
                    obj[0] = retObj.getFieldValueString(i, 1);
                    String reportqry = buildQuery(sql, obj);
                    reportRetObj = execSelectSQL(reportqry);
                    if (reportRetObj.getRowCount() > 0) {
                        ArrayList<String> ReportNames = new ArrayList<String>();
                        for (int j = 0; j < reportRetObj.getRowCount(); j++) {
                            ReportNames.add(reportRetObj.getFieldValueString(j, 0) + "&&" + reportRetObj.getFieldValueString(j, 1));
                        }
                        viewbyRelatedReports.put(retObj.getFieldValueString(i, 1), ReportNames);

//                     
                    }
                }
            }

            map.put("viewbys", viewbyNames);
            map.put("reports", viewbyRelatedReports);
            map.put("assigned", assignedMap);
            map.put("conditions", conditionMap);

        } catch (SQLException ex) {

            logger.error("Exception:", ex);
        }
        return map;

    }

    public void insertAdvanceParameters(String reportId, Map viewbyMap) {


        //Gson gson = new Gson();
        String gsonString = gson.toJson(viewbyMap);
        String sql = getResourceBundle().getString("saveAdvanceParameterOptions");
        Object[] obj = new Object[2];
        obj[0] = gsonString;
        obj[1] = reportId;
        try {
            String finalqry = buildQuery(sql, obj);
            execUpdateSQL(finalqry);
        } catch (Exception ex) {

            logger.error("Exception:", ex);
        }

    }

    public void updateHideLeftTdStatus(String repId, String status, String tdType) {
        PbReturnObject retObj = new PbReturnObject();
        String qry = null;
        if (tdType.equalsIgnoreCase("lefttd")) {
            qry = getResourceBundle().getString("updateLeftTdStatus");
        } else {
            qry = getResourceBundle().getString("updateParamTdStatus");
        }
        Object[] obj = new Object[2];
        obj[0] = status;
        obj[1] = repId;
        String finalqry = buildQuery(qry, obj);
        try {
            execModifySQL(finalqry);
        } catch (Exception ex) {

            logger.error("Exception:", ex);
        }
    }

    public String getLeftTdStatus(String repId, String tdtype) {
        String repleftTdStatus = null;
        PbReturnObject retObj = new PbReturnObject();
        String qry = null;
        if (tdtype.equalsIgnoreCase("lefttd")) {
            qry = getResourceBundle().getString("getLeftTdStatus");
        } else {
            qry = getResourceBundle().getString("getParamTdStatus");
        }
        Object[] obj = new Object[1];
        obj[0] = repId;
        String finalqry = buildQuery(qry, obj);
        try {
            retObj = execSelectSQL(finalqry);
        } catch (Exception ex) {

            logger.error("Exception:", ex);
        }
        if (tdtype.equalsIgnoreCase("lefttd")) {
            repleftTdStatus = retObj.getFieldValueString(0, "HIDE_LEFT_TD");
        } else {
            repleftTdStatus = retObj.getFieldValueString(0, "PARAM_TD");
        }
        return repleftTdStatus;
    }

    public List getDependentReportdetails(String reportId) {
        PbReturnObject retObject = new PbReturnObject();
        String qry = getResourceBundle().getString("getDependentReportdetails");
        Object[] obj = new Object[1];
        obj[0] = reportId;
        String finalqry = buildQuery(qry, obj);
        List<String> list = null;
        try {
            retObject = execSelectSQL(finalqry);
            Gson json = new Gson();
            Map map = json.fromJson(retObject.getFieldValueString(0, 0), new TypeToken<Map<String, List<String>>>() {
            }.getType());
            if (map != null && !map.isEmpty()) {
                list = (List<String>) map.get(reportId);
            }
        } catch (SQLException ex) {

            logger.error("Exception:", ex);
        }
//            
        return list;

    }

    public void saveAdvanceParameterOptions(String reportId, String[] ViewbyIds, String[] conditions, String[] CheckViewbyId, String[] dReportIds, String[] checkReportIdArr1, String[] uncheckViewbyIdArr) {
        String qry = getResourceBundle().getString("insertViewbyRltdCondition");
        Object[] obj = new Object[4];
        obj[2] = reportId;
        ArrayList list = new ArrayList();
        ArrayList list1 = new ArrayList();
        if (ViewbyIds != null) {
            for (int i = 0; i < ViewbyIds.length; i++) {
                obj[1] = ViewbyIds[i];
                obj[0] = conditions[i];
                String sqlqry = buildQuery(qry, obj);
                list.add(sqlqry);
            }
            executeMultiple(list);
        }
        String qry1 = getResourceBundle().getString("insertDependentReportID");
        if (CheckViewbyId != null) {
            for (int j = 0; j < CheckViewbyId.length; j++) {
                obj[0] = dReportIds[j];
                obj[1] = CheckViewbyId[j];
                obj[2] = reportId;
                String sqlqry = buildQuery(qry1, obj);
                list1.add(sqlqry);
            }
            executeMultiple(list1);
        }
        if (checkReportIdArr1 != null) {
            for (int i = 0; i < checkReportIdArr1.length; i++) {
                if (!uncheckViewbyIdArr[i].toString().equalsIgnoreCase("")) {
                    for (int j = 0; j < uncheckViewbyIdArr.length; j++) {
                        obj[0] = null;
                        obj[1] = uncheckViewbyIdArr[j];
                        obj[2] = reportId;
                        String sqlqry = buildQuery(qry1, obj);
                        list1.add(sqlqry);
                    }
                    executeMultiple(list1);

                }
            }
        }
    }

    public ArrayList getDependentViewByidsandReps(String repId) {
        ArrayList dependentdets = new ArrayList();
        HashMap viewbysandConditions = new HashMap();
        HashMap viewbyRepHashMap = new HashMap();
        PbReturnObject retObj = new PbReturnObject();
        PbReturnObject retObj1 = new PbReturnObject();
        String qry = getResourceBundle().getString("getDependentViewbys");
        Object[] obj = new Object[1];
        obj[0] = repId;
        String finalqry = buildQuery(qry, obj);
        String qry2 = getResourceBundle().getString("getDependentReps");
        String finalqry2 = buildQuery(qry2, obj);
        try {
            retObj = execSelectSQL(finalqry);
            retObj1 = execSelectSQL(finalqry2);
            for (int i = 0; i < retObj.getRowCount(); i++) {
                viewbysandConditions.put(retObj.getFieldValueString(i, "ELEMENT_ID"), retObj.getFieldValueString(i, "VIEWBY_CONDITION"));
            }
            for (int i = 0; i < retObj1.getRowCount(); i++) {
                if (!retObj1.getFieldValueString(i, "DEPENDENT_REPORTID").equalsIgnoreCase("null") && !retObj1.getFieldValueString(i, "DEPENDENT_REPORTID").equalsIgnoreCase("")) {
                    viewbyRepHashMap.put(retObj1.getFieldValueString(i, "ELEMENT_ID"), retObj1.getFieldValueString(i, "DEPENDENT_REPORTID"));
                }

            }
            dependentdets.add(viewbysandConditions);
            dependentdets.add(viewbyRepHashMap);
        } catch (SQLException ex) {

            logger.error("Exception:", ex);
        }


        return dependentdets;
    }

    public void insertFactFilterDetails(String busTableId, String reportId, String factFilterName, String factFormula, String progenTime, String dimElementId, String priority) {
        String sql = getResourceBundle().getString("insertFactFilterDetails");
        if (dimElementId == null || dimElementId.equalsIgnoreCase("") || dimElementId.equalsIgnoreCase(null)) {
            dimElementId = "null";
            priority = "null";
        }

        Object[] obj = new Object[7];
        obj[0] = busTableId;
        obj[1] = reportId;
        obj[2] = factFilterName;
        obj[3] = factFormula.replace("''", "'").replace("'", "''");
        obj[4] = progenTime;
        obj[5] = dimElementId;
        obj[6] = priority;
        String finalqry = buildQuery(sql, obj);
        try {
            //
            int flag = execUpdateSQL(finalqry);
            //

        } catch (Exception ex) {

            logger.error("Exception:", ex);
        }

    }

    public PbReturnObject getFactDetails(String reportId) {
        PbReturnObject returnobj = new PbReturnObject();
        String qry = getResourceBundle().getString("getFactDetails");
        Object[] obj = new Object[1];
        obj[0] = reportId;
        String finalqry = buildQuery(qry, obj);
        try {
            //
            returnobj = execSelectSQL(finalqry);


        } catch (Exception ex) {

            logger.error("Exception:", ex);
        }
        return returnobj;
    }

    public void deleteFactFormulas(String filterId, String reportId) {
        String qry = getResourceBundle().getString("deleteFactFomulas");
        Object[] obj = new Object[2];
        obj[0] = filterId;
        obj[1] = reportId;
        String finalqry = buildQuery(qry, obj);
        try {
            //
            execModifySQL(finalqry);
        } catch (Exception ex) {

            logger.error("Exception:", ex);
        }
    }

    public String getBussTabId(String elementId) throws Exception {
        String getReportTimeParamsQuery = "select buss_table_id,grp_id,buss_col_id,buss_col_name,buss_table_name,folder_id,user_col_type from prg_user_all_info_details where element_id=&";
        Object[] data = new Object[1];
        data[0] = elementId;
        PbReturnObject retObj = null;
        String jsonString = null;
        Gson json = new Gson();
        String finalQuery = null;

        List<String> buss_table_id = new ArrayList<String>();
        List<String> grp_id = new ArrayList<String>();
        List<String> buss_col_id = new ArrayList<String>();
        List<String> buss_col_name = new ArrayList<String>();
        List<String> buss_table_name = new ArrayList<String>();
        List<String> folder_id = new ArrayList<String>();
        List<String> user_col_id = new ArrayList<String>();
        HashMap grpmap = new HashMap();

        finalQuery = buildQuery(getReportTimeParamsQuery, data);
        retObj = execSelectSQL(finalQuery);

        for (int i = 0; i < retObj.getRowCount(); i++) {
            buss_table_id.add(retObj.getFieldValueString(i, 0));
            grp_id.add(retObj.getFieldValueString(i, 1));
            buss_col_id.add(retObj.getFieldValueString(i, 2));
            buss_col_name.add(retObj.getFieldValueString(i, 3));
            buss_table_name.add(retObj.getFieldValueString(i, 4));
            folder_id.add(retObj.getFieldValueString(i, 5));
            user_col_id.add(retObj.getFieldValueString(i, 6));
        }
        grpmap.put("buss_table_id", buss_table_id);
        grpmap.put("grp_id", grp_id);
        grpmap.put("buss_col_id", buss_col_id);
        grpmap.put("buss_col_name", buss_col_name);
        grpmap.put("buss_table_name", buss_table_name);
        grpmap.put("folder_id", folder_id);
        grpmap.put("user_col_id", user_col_id);
        jsonString = json.toJson(grpmap);
        return jsonString;
    }
    //started by Nazneen

    public ArrayList getTableid(String buscolid) {

        PbReturnObject returnObject = null;
        ArrayList<String> tablelist = new ArrayList<String>();
        try {
            String busstableid = getResourceBundle().getString("gettableid");
            returnObject = new PbReturnObject();
            PbDb pbDb = new PbDb();
            Object ojb[] = new Object[1];
            ojb[0] = buscolid;
            String finalQuery = buildQuery(busstableid, ojb);
            returnObject = pbDb.execSelectSQL(finalQuery);

            for (int i = 0; i < returnObject.getRowCount(); i++) {
                tablelist.add(returnObject.getFieldValueString(i, 1));
                tablelist.add(returnObject.getFieldValueString(i, 2));
            }

        } catch (Exception e) {
        }
        return tablelist;
    }

    public String getAllFields(String tabId) {
        String jsonString = null;
        PbReturnObject returnObject = null;
        try {
            String Query = getResourceBundle().getString("getDetails");
            returnObject = new PbReturnObject();
            PbDb pbDb = new PbDb();
            Object ojb[] = new Object[1];
            ojb[0] = tabId;

            String finalQuery = buildQuery(Query, ojb);
            returnObject = pbDb.execSelectSQL(finalQuery);
//            ArrayList<String> displayNameIds=new ArrayList<String>();

            HashMap defaultaggr = new HashMap();
            for (int i = 0; i < returnObject.getRowCount(); i++) {
                ArrayList<String> agr = new ArrayList<String>();
                agr.add(returnObject.getFieldValueString(i, 14));

                if ((returnObject.getFieldValueString(i, 5)).equals("Y")) {
                    agr.add("sum");
                } else {
                    agr.add("");
                }
                if ((returnObject.getFieldValueString(i, 2)).equals("Y")) {
                    agr.add("avg");
                } else {
                    agr.add("");
                }
                if ((returnObject.getFieldValueString(i, 3)).equals("Y")) {
                    agr.add("min");
                } else {
                    agr.add("");
                }
                if ((returnObject.getFieldValueString(i, 4)).equals("Y")) {
                    agr.add("max");
                } else {
                    agr.add("");
                }
                if ((returnObject.getFieldValueString(i, 6)).equals("Y")) {
                    agr.add("count");
                } else {
                    agr.add("");
                }
                if ((returnObject.getFieldValueString(i, 7)).equals("Y")) {
                    agr.add("COUNTDISTINCT");
                } else {
                    agr.add("");
                }
                if (returnObject.getFieldValueString(i, 15).equals("Y")) {
                    agr.add("roleflag");
                } else {
                    agr.add("");
                }
                agr.add(returnObject.getFieldValueString(i, 1));
                defaultaggr.put(returnObject.getFieldValueString(i, 0), agr);
            }
            Gson gson = new Gson();
            jsonString = gson.toJson(defaultaggr);

        } 
        catch (SQLException ex) {
            logger.error("Exception:", ex);
        }catch (Exception e) {
            logger.error("Exception:", e);
        }        return jsonString;
        }

    public String quicktimebasedFormula(ArrayList selvalueslist, String bustableid, ArrayList columnalias, String buscolid, String colName, String tablename, String elementId, String folder_id, String calender_id, String dateEId, String dateoption, String withProgenTimeVar, String withoutProgenTimeVar) {
        String status = "failure";
        PbDb pbdb = new PbDb();
        PbReturnObject pbretobj = new PbReturnObject();
        try {
            ArrayList queryieslist = new ArrayList();
            String columnvalue = null;
            String buildquery = null;
            String dispcolname = null;
            String bussColId = null;
            String actual_col_formula = "";
            int bussTableId = 0;
            int subFolderTabId = 0;
            String bussTableName = "";
            String disp_name = "";
            String table_disp_name = "";
            String table_tooltip_name = "";
            String referred_elements = elementId;
            String ref_dim_tab_id = null;
            String ref_dim_element_id = null;
            String dependentElementIds = elementId;
            String[] dateinfo = null;
            String dateeid = null;
            String datedetails = null;
            if (dateEId.contains(";")) {
                dateinfo = dateEId.split(";");
                dateeid = dateinfo[0];
                datedetails = dateinfo[1];
            }
//            String columndisplydesc = colName;
            String column_disply_desc = getResourceBundle().getString("getcolumndisplaydes");
            Object[] columndisply = new Object[1];
            columndisply[0] = elementId;
            String buildquer = pbdb.buildQuery(column_disply_desc, columndisply);
            pbretobj = pbdb.execSelectSQL(buildquer);
            String columndisplydesc = pbretobj.getFieldValueString(0, 0);
            Object[] timenxtobj = new Object[7];

            String checkDistFactsQry = " SELECT distinct BUSS_TABLE_ID,SUB_FOLDER_TAB_ID FROM PRG_USER_SUB_FOLDER_ELEMENTS  where element_id in(" + dependentElementIds + ")";
            PbReturnObject checkDistFactsRO = pbdb.execSelectSQL(checkDistFactsQry);

            //Elements from a Single Fact
            if (!(checkDistFactsRO.getRowCount() > 1)) {

                String doubleExistQuery1 = " SELECT distinct BUSS_TABLE_ID,SUB_FOLDER_TAB_ID FROM PRG_USER_SUB_FOLDER_ELEMENTS  where element_id in(" + dependentElementIds + ") AND BUSS_TABLE_ID!=0";

                PbReturnObject doubleExistQuerypbro1 = pbdb.execSelectSQL(doubleExistQuery1);
                if (doubleExistQuerypbro1.getRowCount() > 0) {
                    String nameQuery = "";
                    String nameQuery1 = "";
                    if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                        nameQuery = "SELECT BUSS_TABLE_NAME,isnull(USER_COL_NAME,BUSS_COL_NAME) BUSS_COL_NAME,element_id,isnull(USER_COL_DESC, user_col_name) USER_COL_DESC,user_col_type,ACTUAL_COL_FORMULA,REFFERED_ELEMENTS,AGGREGATION_TYPE,DATALENGTH(isnull(USER_COL_DESC, user_col_name)),BUSS_COL_ID,REF_DIM_TAB_ID,BUSS_COL_NAME,isnull(isnull(TABLE_DISP_NAME,DISP_NAME),BUSS_TABLE_NAME) DISP_NAME,isnull(isnull(TABLE_DISP_NAME,DISP_NAME),BUSS_TABLE_NAME) TABLE_DISP_NAME,isnull(isnull(TABLE_DISP_NAME,DISP_NAME),BUSS_TABLE_NAME) TABLE_TOOLTIP_NAME,REFFERED_ELEMENTS,ref_dim_tab_id,ref_dim_element_id  FROM PRG_USER_ALL_INFO_DETAILS WHERE element_id IN(" + dependentElementIds + ") order by DATALENGTH(isnull(USER_COL_DESC, user_col_name)) desc";
                        nameQuery1 = "SELECT BUSS_TABLE_NAME,isnull(USER_COL_NAME,BUSS_COL_NAME) BUSS_COL_NAME,element_id,isnull(USER_COL_DESC, user_col_name) USER_COL_DESC,user_col_type,ACTUAL_COL_FORMULA,REFFERED_ELEMENTS,AGGREGATION_TYPE,DATALENGTH(isnull(USER_COL_DESC, user_col_name)),BUSS_COL_ID,REF_DIM_TAB_ID,BUSS_COL_NAME,isnull(isnull(TABLE_DISP_NAME,DISP_NAME),BUSS_TABLE_NAME) DISP_NAME,isnull(isnull(TABLE_DISP_NAME,DISP_NAME),BUSS_TABLE_NAME) TABLE_DISP_NAME,isnull(isnull(TABLE_DISP_NAME,DISP_NAME),BUSS_TABLE_NAME) TABLE_TOOLTIP_NAME,REFFERED_ELEMENTS,ref_dim_tab_id,ref_dim_element_id   FROM PRG_USER_ALL_INFO_DETAILS WHERE element_id IN(" + dependentElementIds + ") and user_col_type in('calculated','summarized','summarised') order by DATALENGTH(isnull(USER_COL_DESC, user_col_name)) desc";
                    } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                        nameQuery = "SELECT BUSS_TABLE_NAME,ifnull(USER_COL_NAME,BUSS_COL_NAME) BUSS_COL_NAME,element_id,ifnull(USER_COL_DESC, user_col_name) USER_COL_DESC,user_col_type,ACTUAL_COL_FORMULA,REFFERED_ELEMENTS,AGGREGATION_TYPE,LENGTH(ifnull(USER_COL_DESC, user_col_name)),BUSS_COL_ID,REF_DIM_TAB_ID,BUSS_COL_NAME,ifnull(ifnull(TABLE_DISP_NAME,DISP_NAME),BUSS_TABLE_NAME) DISP_NAME,ifnull(ifnull(TABLE_DISP_NAME,DISP_NAME),BUSS_TABLE_NAME) TABLE_DISP_NAME,ifnull(ifnull(TABLE_DISP_NAME,DISP_NAME),BUSS_TABLE_NAME) TABLE_TOOLTIP_NAME,REFFERED_ELEMENTS,ref_dim_tab_id,ref_dim_element_id  FROM PRG_USER_ALL_INFO_DETAILS WHERE element_id IN(" + dependentElementIds + ") order by LENGTH(ifnull(USER_COL_DESC, user_col_name)) desc";
                        nameQuery1 = "SELECT BUSS_TABLE_NAME,ifnull(USER_COL_NAME,BUSS_COL_NAME) BUSS_COL_NAME,element_id,ifnull(USER_COL_DESC, user_col_name) USER_COL_DESC,user_col_type,ACTUAL_COL_FORMULA,REFFERED_ELEMENTS,AGGREGATION_TYPE,LENGTH(ifnull(USER_COL_DESC, user_col_name)),BUSS_COL_ID,REF_DIM_TAB_ID,BUSS_COL_NAME,ifnull(ifnull(TABLE_DISP_NAME,DISP_NAME),BUSS_TABLE_NAME) DISP_NAME,ifnull(ifnull(TABLE_DISP_NAME,DISP_NAME),BUSS_TABLE_NAME) TABLE_DISP_NAME,ifnull(ifnull(TABLE_DISP_NAME,DISP_NAME),BUSS_TABLE_NAME) TABLE_TOOLTIP_NAME,REFFERED_ELEMENTS,ref_dim_tab_id,ref_dim_element_id   FROM PRG_USER_ALL_INFO_DETAILS WHERE element_id IN(" + dependentElementIds + ") and user_col_type in('calculated','summarized','summarised') order by LENGTH(ifnull(USER_COL_DESC, user_col_name)) desc";
                    } else {
                        nameQuery = "SELECT BUSS_TABLE_NAME,NVL(USER_COL_NAME,BUSS_COL_NAME),element_id,nvl(USER_COL_DESC, user_col_name),user_col_type,ACTUAL_COL_FORMULA,REFFERED_ELEMENTS,AGGREGATION_TYPE,length(nvl(USER_COL_DESC, user_col_name)),BUSS_COL_ID,REF_DIM_TAB_ID,BUSS_COL_NAME,NVL(NVL(TABLE_DISP_NAME,DISP_NAME),BUSS_TABLE_NAME) DISP_NAME,NVL(NVL(TABLE_DISP_NAME,DISP_NAME),BUSS_TABLE_NAME) TABLE_DISP_NAME,NVL(NVL(TABLE_DISP_NAME,DISP_NAME),BUSS_TABLE_NAME) TABLE_TOOLTIP_NAME,REFFERED_ELEMENTS,ref_dim_tab_id,ref_dim_element_id   FROM PRG_USER_ALL_INFO_DETAILS WHERE element_id IN(" + dependentElementIds + ") order by length(nvl(USER_COL_DESC, user_col_name)) desc";
                        nameQuery1 = "SELECT BUSS_TABLE_NAME,NVL(USER_COL_NAME,BUSS_COL_NAME),element_id,nvl(USER_COL_DESC, user_col_name),user_col_type,ACTUAL_COL_FORMULA,REFFERED_ELEMENTS,AGGREGATION_TYPE,length(nvl(USER_COL_DESC, user_col_name)),BUSS_COL_ID,REF_DIM_TAB_ID,BUSS_COL_NAME,NVL(NVL(TABLE_DISP_NAME,DISP_NAME),BUSS_TABLE_NAME) DISP_NAME,NVL(NVL(TABLE_DISP_NAME,DISP_NAME),BUSS_TABLE_NAME) TABLE_DISP_NAME,NVL(NVL(TABLE_DISP_NAME,DISP_NAME),BUSS_TABLE_NAME) TABLE_TOOLTIP_NAME,REFFERED_ELEMENTS,ref_dim_tab_id,ref_dim_element_id    FROM PRG_USER_ALL_INFO_DETAILS WHERE element_id IN(" + dependentElementIds + ") and user_col_type in('calculated','summarized','summarised') order by length(nvl(USER_COL_DESC, user_col_name)) desc";
                    }

                    PbReturnObject pbro = pbdb.execSelectSQL(nameQuery);
                    PbReturnObject pbro1 = pbdb.execSelectSQL(nameQuery1);
                    String bussNamecolName = "";
                    String onlyColName = "";
                    String formulaold = "";

                    int count = 0;
                    int count1 = 0;
                    for (int n = 0; n < pbro.getRowCount(); n++) {
                        bussNamecolName = pbro.getFieldValueString(n, 0) + "." + pbro.getFieldValueString(n, 1);
                        onlyColName = pbro.getFieldValueString(n, 3);
                        bussNamecolName = bussNamecolName.toUpperCase();
                        onlyColName = onlyColName.toUpperCase();
                        bussColId = pbro.getFieldValueString(n, 9);
                        if (!pbro.getFieldValueString(n, 4).equalsIgnoreCase("calculated")) {
                            actual_col_formula = pbro.getFieldValueString(n, 0) + "." + colName;
                        } else {
                            actual_col_formula = pbro.getFieldValueString(n, 5);
                            actual_col_formula = actual_col_formula.replace("'", "''");
                        }

                        bussTableId = checkDistFactsRO.getFieldValueInt(0, 0);
                        subFolderTabId = checkDistFactsRO.getFieldValueInt(0, 1);
                        bussTableName = pbro.getFieldValueString(0, 0);
                        disp_name = pbro.getFieldValueString(0, 12);
                        table_disp_name = pbro.getFieldValueString(0, 13);
                        table_tooltip_name = pbro.getFieldValueString(0, 14);
                        if (!(pbro.getFieldValueString(0, 15).equalsIgnoreCase("") || pbro.getFieldValueString(0, 15).equalsIgnoreCase("null") || pbro.getFieldValueString(0, 15) == null)) {
                            referred_elements = referred_elements + "," + pbro.getFieldValueString(0, 15);
                        }
                        if (!(pbro.getFieldValueString(0, 16).equalsIgnoreCase("") || pbro.getFieldValueString(0, 16).equalsIgnoreCase("null") || pbro.getFieldValueString(0, 16) == null)) {
                            ref_dim_tab_id = pbro.getFieldValueString(0, 16);
                        }
                        if (!(pbro.getFieldValueString(0, 17).equalsIgnoreCase("") || pbro.getFieldValueString(0, 17).equalsIgnoreCase("null") || pbro.getFieldValueString(0, 17) == null)) {
                            ref_dim_element_id = pbro.getFieldValueString(0, 17);
                        }
                    }
                }
            }

            for (int i = 0; i < selvalueslist.size(); i++) {
                queryieslist.clear();
                columnvalue = columnalias.get(i).toString();
                dispcolname = columnvalue.replace("-", " ");
                String timebased = null;
                String columndesc = null;
                String timecal = null;
                String timecals = null;
                String st = null;
                String timecase = null;
                String timest = null;

                int x = 0;
                if (dispcolname.substring(0, 5).equals("count")) {
                    columndesc = dispcolname.substring(0, 5);
                    timebased = dispcolname.substring(6);
                } else {
                    columndesc = dispcolname.substring(0, 4);
                    timebased = dispcolname.substring(4);
                }
                int len = timebased.length();
                if (timebased.endsWith("s")) {
                    x = timebased.lastIndexOf("days");
                    timecal = timebased.substring(0, x);
                    timecals = timebased.substring(x);
                    st = timecals.substring(0, 1);
                    timecase = st.toUpperCase() + "" + timecals.substring(1);
                    timest = timecal + " " + timecase;
                } else if (timebased.endsWith("r")) {
                    x = timebased.lastIndexOf("year");
                    timecal = timebased.substring(0, x);
                    timecals = timebased.substring(x);
                    st = timecals.substring(0, 1);
                    timecase = st.toUpperCase() + "" + timecals.substring(1);
                    timest = timecal + " " + timecase;
                } else if (timebased.endsWith("h")) {
                    x = timebased.lastIndexOf("month");
                    timecal = timebased.substring(0, x);
                    timecals = timebased.substring(x);
                    st = timecals.substring(0, 1);
                    timecase = st.toUpperCase() + "" + timecals.substring(1);
                    if (timecal.trim().equals("1")) {
                        timest = timecal + " " + timecase;
                    } else {
                        timest = timecal + " " + timecase + "s";
                    }
                } else if (timebased.startsWith("m") || timebased.startsWith("y") || timebased.startsWith("q") || timebased.startsWith("l") || timebased.startsWith("w") || timebased.startsWith("c") || timebased.startsWith("n")) {
                    timest = timebased.substring(0, 3).toUpperCase();
                } else if (timebased.startsWith("p") && len == 4) {
                    timest = "Prior" + " " + timebased.substring(1, 4);
                } else if (timebased.startsWith("p") && len == 5) {
                    timest = "Prior Year" + " " + timebased.substring(2, 5);
                }

                if (withProgenTimeVar.equalsIgnoreCase("true")) {
                    String[] tempcolumnvalue = columnvalue.split("-");
                    columnvalue = tempcolumnvalue[0] + "#PROGEN_RANGE";
//                    column_display_desc = column_display_desc +" (PERIOD)";
                    timest = "PERIOD";
                }
                if (withoutProgenTimeVar.equalsIgnoreCase("true")) {
                    String[] tempcolumnvalue = columnvalue.split("-");
                    columnvalue = tempcolumnvalue[0] + "#NO_TIME";
//                    column_display_desc = column_display_desc +" (THROUGHOUT)";
                    timest = "THROUGHOUT";
                }

                String column_display_desc = null;
                if (columndesc.trim().equals("sum")) {
                    column_display_desc = columndisplydesc.concat("(" + timest + ")");
                } else if (columndesc.trim().equals("avg") || columndesc.trim().equals("min") || columndesc.trim().equals("max")) {
                    column_display_desc = columndesc + " " + columndisplydesc.concat("(" + timest + ")");
                } else {
                    column_display_desc = columndisplydesc + " " + columndesc + " " + "(" + timest + ")";
                }

                String folderdets = "select distinct GRP_ID, FOLDER_ID, FOLDER_NAME,CONNECTION_ID from PRG_USER_ALL_INFO_DETAILS where FOLDER_ID=" + folder_id;
                PbReturnObject pbrofolderdet = pbdb.execSelectSQL(folderdets);
                String folderName = pbrofolderdet.getFieldValueString(0, 2);
                String connectionId = pbrofolderdet.getFieldValueString(0, 3);
                String grpId = String.valueOf(pbrofolderdet.getFieldValueInt(0, 0));

                String existFolderQuery = "SELECT Distinct SUB_FOLDER_ID, SUB_FOLDER_NAME, SUB_FOLDER_TYPE FROM PRG_USER_FOLDER_DETAIL where folder_id =" + folder_id + " and SUB_FOLDER_TYPE='Facts'";
                PbReturnObject pbroext = pbdb.execSelectSQL(existFolderQuery);
                int subfolderId = 0;
                String subFolderName = "";
                String subFolderType = "";
                if (pbroext.getRowCount() > 0) {
                    subfolderId = pbroext.getFieldValueInt(0, 0);
                    subFolderName = pbroext.getFieldValueString(0, 1);
                    subFolderType = pbroext.getFieldValueString(0, 2);
                }
                String calendar_name = "";
                String calendar_table = "";
                if (!calender_id.contains("0000")) {
                    String calendarQuery = "SELECT CALENDER_NAME,DENOM_TABLE from PRG_CALENDER_SETUP where CALENDER_ID=" + calender_id;
                    PbReturnObject pbroextcal = pbdb.execSelectSQL(calendarQuery);

                    calendar_name = pbroextcal.getFieldValueString(0, 0);
                    calendar_table = pbroextcal.getFieldValueString(0, 1);

                    column_display_desc = column_display_desc + " " + calendar_name;
                }

                String addSubFolderElements = getResourceBundle().getString("addSubFolderElements");
                String elementIdQuery = "";
                PbReturnObject pbrofolderelement = null;
                int last_element_id = 0;
                if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                    //added by mohit for sqlserver
                    last_element_id = getSequenceNumber("select ident_current('PRG_USER_SUB_FOLDER_ELEMENTS') ");
                    last_element_id++;
                    elementId = String.valueOf(last_element_id);
                    Object obj1[] = new Object[17];
                    obj1[0] = subfolderId;
                    obj1[1] = bussTableId;
                    obj1[2] = buscolid;
                    obj1[3] = colName;
                    obj1[4] = column_display_desc;
                    obj1[5] = column_display_desc;
                    obj1[6] = "TIMECALUCULATED";
                    obj1[7] = subFolderTabId;
//                        obj1[8] = "ident_current('PRG_USER_SUB_FOLDER_ELEMENTS')";
                    obj1[8] = elementId;
                    obj1[9] = "1";
                    obj1[10] = columnvalue.replace("-", "#");
                    obj1[11] = "Y";
                    obj1[12] = referred_elements;
                    obj1[13] = actual_col_formula;
//                        obj1[14] = dateeid;
                    if (dateeid != null && !dateeid.isEmpty()) {
                        obj1[14] = dateeid;
                    } else {
                        obj1[14] = "null";
                    }
                    obj1[15] = datedetails;
                    obj1[16] = dateoption;

                    buildquery = pbdb.buildQuery(addSubFolderElements, obj1);

//                        String updateQuery1 = "update PRG_USER_SUB_FOLDER_ELEMENTS set USER_COL_NAME='E_'+cast(IDENT_CURRENT('PRG_USER_SUB_FOLDER_ELEMENTS') as varchar(258)) where ELEMENT_ID=ident_current('PRG_USER_SUB_FOLDER_ELEMENTS')";
//                        String updateQuery = "update PRG_USER_SUB_FOLDER_ELEMENTS set REF_ELEMENT_ID=ident_current('PRG_USER_SUB_FOLDER_ELEMENTS') where  ELEMENT_ID = ident_current('PRG_USER_SUB_FOLDER_ELEMENTS')";
                    String updateQuery1 = "update PRG_USER_SUB_FOLDER_ELEMENTS set USER_COL_NAME='E_" + elementId + "', REF_ELEMENT_ID=" + elementId + " where ELEMENT_ID=" + elementId;
//                        String updateQuery = "update PRG_USER_SUB_FOLDER_ELEMENTS set REF_ELEMENT_ID=ident_current('PRG_USER_SUB_FOLDER_ELEMENTS') where  ELEMENT_ID = ident_current('PRG_USER_SUB_FOLDER_ELEMENTS')";
                    queryieslist.add(buildquery);
//                        queryieslist.add(updateQuery);
                    queryieslist.add(updateQuery1);

                } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                    //added by mohit for mysql
                    String LastElementIdQuery = "select LAST_INSERT_ID(ELEMENT_ID) from PRG_USER_SUB_FOLDER_ELEMENTS order by 1 desc limit 1";
                    pbrofolderelement = pbdb.execSelectSQL(LastElementIdQuery);
                    last_element_id = pbrofolderelement.getFieldValueInt(0, 0);
                    last_element_id++;
                    Object obj1[] = new Object[17];
                    obj1[0] = subfolderId;
                    obj1[1] = bussTableId;
                    obj1[2] = buscolid;
                    obj1[3] = colName;
                    obj1[4] = column_display_desc;
                    obj1[5] = column_display_desc;
                    obj1[6] = "TIMECALUCULATED";
                    obj1[7] = subFolderTabId;
                    obj1[8] = last_element_id;
                    obj1[9] = "1";
                    obj1[10] = columnvalue.replace("-", "#");
                    obj1[11] = "Y";
                    obj1[12] = referred_elements;
                    obj1[13] = actual_col_formula;
                    obj1[14] = dateeid;
                    if (dateeid != null && !dateeid.isEmpty()) {
                        obj1[14] = dateeid;
                    } else {
                        obj1[14] = "null";
                    }
                    obj1[15] = datedetails;
                    obj1[16] = dateoption;
//                        String last_element_id1 = String.valueOf(last_element_id);
                    buildquery = pbdb.buildQuery(addSubFolderElements, obj1);
//                        String updateQuery1 = "update PRG_USER_SUB_FOLDER_ELEMENTS set USER_COL_NAME=concat('E_','"+last_element_id+"') where ELEMENT_ID='"+last_element_id+"'";
//                        String updateQuery = "update PRG_USER_SUB_FOLDER_ELEMENTS set REF_ELEMENT_ID='"+last_element_id+"' where  ELEMENT_ID ='"+last_element_id+"'";
                    String updateQuery1 = "update PRG_USER_SUB_FOLDER_ELEMENTS set USER_COL_NAME=concat('E_','" + last_element_id + "') ,REF_ELEMENT_ID=" + last_element_id + " where ELEMENT_ID=" + last_element_id;

                    queryieslist.add(buildquery);
//                        queryieslist.add(updateQuery);
                    queryieslist.add(updateQuery1);

                } else {
                    elementIdQuery = "select PRG_USER_SUB_FLDR_ELEMENTS_SEQ.nextval from dual";
                    pbrofolderelement = pbdb.execSelectSQL(elementIdQuery);
                    elementId = String.valueOf(pbrofolderelement.getFieldValueInt(0, 0));

                    Object obj1[] = new Object[21];
                    obj1[0] = elementId;
                    obj1[1] = subfolderId;
                    obj1[2] = bussTableId;
                    obj1[3] = buscolid;
                    obj1[4] = column_display_desc;
                    obj1[5] = "E_" + elementId;
//                        obj1[6] = column_display_desc;
                    obj1[6] = column_display_desc;
                    obj1[7] = "TIMECALUCULATED";
                    obj1[8] = subFolderTabId;
                    obj1[9] = elementId;
                    obj1[10] = "1";
                    obj1[11] = columnvalue.replace("-", "#");
                    obj1[12] = "";
                    obj1[13] = "Y";
                    obj1[14] = referred_elements;
                    obj1[15] = actual_col_formula;
                    obj1[16] = actual_col_formula;
                    obj1[17] = bussTableId;
//                        obj1[18] = dateeid;
                    if (dateeid != null && !dateeid.isEmpty()) {
                        obj1[18] = dateeid;
                    } else {
                        obj1[18] = "null";
                    }
                    obj1[19] = datedetails;
                    obj1[20] = dateoption;
                    buildquery = pbdb.buildQuery(addSubFolderElements, obj1);
                    queryieslist.add(buildquery);
                }

                String addUserAllInfoDets = getResourceBundle().getString("addUserAllInfoDets");
                if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                    Object obj2[] = new Object[38];

                    obj2[0] = grpId;
                    obj2[1] = folder_id;
                    obj2[2] = folderName;
                    obj2[3] = subfolderId;
                    obj2[4] = subFolderName;
                    obj2[5] = subFolderType;
                    obj2[6] = subFolderTabId;
                    obj2[7] = "N";
                    obj2[8] = "Y";
                    obj2[9] = "N";
                    obj2[10] = "0";
                    obj2[11] = "0";
                    obj2[12] = "0";
//                        obj2[13] = "ident_current('PRG_USER_SUB_FOLDER_ELEMENTS')";
                    obj2[13] = elementId;
                    obj2[14] = bussTableId;
                    obj2[15] = buscolid;
                    obj2[16] = colName;
//                        obj2[17] = (columnalias.get(i).toString().replace("-", "_")) + "(" + colName + ")";
//                        obj2[18] = column_display_desc;
                    obj2[17] = column_display_desc;
//                        obj2[18] = (columnalias.get(i).toString().replace("-", "_")) + "(" + colName + ")";
                    obj2[18] = column_display_desc;
                    obj2[19] = "TIMECALUCULATED";
                    obj2[20] = elementId;
                    obj2[21] = "1";
                    obj2[22] = "0";
                    obj2[23] = bussTableName;
                    obj2[24] = connectionId;
                    obj2[25] = columnvalue.replace("-", "#");
                    obj2[26] = actual_col_formula;
                    obj2[27] = referred_elements;
                    obj2[28] = actual_col_formula;
                    obj2[29] = "Y";
                    obj2[30] = disp_name;
                    obj2[31] = table_disp_name;
                    obj2[32] = table_tooltip_name;
                    obj2[33] = calendar_name;
                    obj2[34] = calendar_table;
//                        obj2[35] = dateeid;
                    if (dateeid != null && !dateeid.isEmpty()) {
                        obj2[35] = dateeid;
                    } else {
                        obj2[35] = "null";
                    }
                    obj2[36] = datedetails;
                    obj2[37] = dateoption;

                    buildquery = pbdb.buildQuery(addUserAllInfoDets, obj2);
//                        String updateQuery1 = "update PRG_USER_ALL_INFO_DETAILS set USER_COL_NAME='E_'+cast(IDENT_CURRENT('PRG_USER_SUB_FOLDER_ELEMENTS') as varchar(258)) where ELEMENT_ID=ident_current('PRG_USER_SUB_FOLDER_ELEMENTS')";
//                        String updateQuery2 = "update PRG_USER_ALL_INFO_DETAILS set REF_ELEMENT_ID =ident_current('PRG_USER_SUB_FOLDER_ELEMENTS') where ELEMENT_ID=ident_current('PRG_USER_SUB_FOLDER_ELEMENTS')";
//                        String updateQuery3 = "update PRG_USER_ALL_INFO_DETAILS set ref_dim_element_id ="+ref_dim_element_id+" where ELEMENT_ID=ident_current('PRG_USER_SUB_FOLDER_ELEMENTS')";
                    String updateQuery1 = "update PRG_USER_ALL_INFO_DETAILS set USER_COL_NAME='E_" + elementId + "', REF_ELEMENT_ID=" + elementId + ", ref_dim_element_id =" + ref_dim_element_id + " where ELEMENT_ID=" + elementId;
                    queryieslist.add(buildquery);
                    queryieslist.add(updateQuery1);
//                        queryieslist.add(updateQuery2);
//                        queryieslist.add(updateQuery3);

                } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {

                    Object obj2[] = new Object[38];

                    obj2[0] = grpId;
                    obj2[1] = folder_id;
                    obj2[2] = folderName;
                    obj2[3] = subfolderId;
                    obj2[4] = subFolderName;
                    obj2[5] = subFolderType;
                    obj2[6] = subFolderTabId;
                    obj2[7] = "N";
                    obj2[8] = "Y";
                    obj2[9] = "N";
                    obj2[10] = "0";
                    obj2[11] = "0";
                    obj2[12] = "0";
//                        obj2[13] = "(select cast(LAST_INSERT_ID(ELEMENT_ID) as decimal) from PRG_USER_SUB_FOLDER_ELEMENTS order by 1 desc limit 1)";
                    obj2[13] = last_element_id;
                    obj2[14] = bussTableId;
                    obj2[15] = buscolid;
                    obj2[16] = colName;
//                        obj2[17] = (columnalias.get(i).toString().replace("-", "_")) + "(" + colName + ")";
//                        obj2[18] = column_display_desc;
                    obj2[17] = column_display_desc;
//                        obj2[18] = (columnalias.get(i).toString().replace("-", "_")) + "(" + colName + ")";
                    obj2[18] = column_display_desc;
                    obj2[19] = "TIMECALUCULATED";
//                        obj2[20] = "(select cast(LAST_INSERT_ID(ELEMENT_ID) as decimal) from PRG_USER_SUB_FOLDER_ELEMENTS order by 1 desc limit 1)";
                    obj2[20] = last_element_id;
                    obj2[21] = "1";
                    obj2[22] = "0";
                    obj2[23] = bussTableName;
                    obj2[24] = connectionId;
                    obj2[25] = columnvalue.replace("-", "#");
                    obj2[26] = actual_col_formula;
                    obj2[27] = referred_elements;
                    obj2[28] = actual_col_formula;
                    obj2[29] = "Y";
                    obj2[30] = disp_name;
                    obj2[31] = table_disp_name;
                    obj2[32] = table_tooltip_name;
                    obj2[33] = calendar_name;
                    obj2[34] = calendar_table;
                    if (dateeid != null && !dateeid.isEmpty()) {
                        obj2[35] = dateeid;
                    } else {
                        obj2[35] = "null";
                    }
                    obj2[36] = datedetails;
                    obj2[37] = dateoption;

                    buildquery = pbdb.buildQuery(addUserAllInfoDets, obj2);
//                        String updateQuery1 = "update PRG_USER_ALL_INFO_DETAILS set USER_COL_NAME=concat('E_','select LAST_INSERT_ID(ELEMENT_ID) from PRG_USER_SUB_FOLDER_ELEMENTS order by 1 desc limit 1 ') where ELEMENT_ID=(select cast(LAST_INSERT_ID(ELEMENT_ID) as decimal) from PRG_USER_SUB_FOLDER_ELEMENTS order by 1 desc limit 1)";
//                        String updateQuery2 = "update PRG_USER_ALL_INFO_DETAILS set ref_dim_tab_id ="+ref_dim_tab_id+" where ELEMENT_ID=(select cast(LAST_INSERT_ID(ELEMENT_ID) as decimal) from PRG_USER_SUB_FOLDER_ELEMENTS order by 1 desc limit 1)";
//                        String updateQuery3 = "update PRG_USER_ALL_INFO_DETAILS set ref_dim_tab_id ="+ref_dim_element_id+" where ELEMENT_ID=(select cast(LAST_INSERT_ID(ELEMENT_ID) as decimal) from PRG_USER_SUB_FOLDER_ELEMENTS order by 1 desc limit 1)";
//
//                        String updateQuery1 = "update PRG_USER_ALL_INFO_DETAILS set USER_COL_NAME=concat('E_','"+last_element_id+"') where ELEMENT_ID="+last_element_id;
//                        String updateQuery2 = "update PRG_USER_ALL_INFO_DETAILS set REF_ELEMENT_ID ="+last_element_id+" where ELEMENT_ID="+last_element_id;
//                        String updateQuery3 = "update PRG_USER_ALL_INFO_DETAILS set REF_DIM_ELEMENT_ID ="+ref_dim_element_id+" where ELEMENT_ID="+last_element_id;
                    String updateQuery1 = "update PRG_USER_ALL_INFO_DETAILS set USER_COL_NAME=concat('E_','" + last_element_id + "') ,REF_ELEMENT_ID=" + last_element_id + ",REF_DIM_ELEMENT_ID =" + ref_dim_element_id + " where ELEMENT_ID=" + last_element_id;
                    queryieslist.add(buildquery);
                    queryieslist.add(updateQuery1);
//                        queryieslist.add(updateQuery2);
//                        queryieslist.add(updateQuery3);

                } else {
                    Object obj2[] = new Object[42];
                    obj2[0] = grpId;
                    obj2[1] = folder_id;
                    obj2[2] = folderName;
                    obj2[3] = subfolderId;
                    obj2[4] = subFolderName;
                    obj2[5] = subFolderType;
                    obj2[6] = subFolderTabId;
                    obj2[7] = "N";
                    obj2[8] = "Y";
                    obj2[9] = "N";
                    obj2[10] = disp_name;
                    obj2[11] = "0";
                    obj2[12] = "0";
                    obj2[13] = "0";
                    obj2[14] = "";
                    obj2[15] = elementId;
                    obj2[16] = bussTableId;
                    obj2[17] = buscolid;
                    obj2[18] = colName;
                    obj2[19] = "E_" + elementId;
//                        obj2[20] = column_display_desc;
                    obj2[20] = column_display_desc;
                    obj2[21] = "TIMECALUCULATED";
                    obj2[22] = elementId;
                    obj2[23] = "1";
                    obj2[24] = "0";
                    obj2[25] = "";
                    obj2[26] = "";
                    obj2[27] = bussTableName;
                    obj2[28] = connectionId;
                    obj2[29] = columnvalue.replace("-", "#");
                    obj2[30] = actual_col_formula;
                    obj2[31] = referred_elements;
                    obj2[32] = actual_col_formula;
                    obj2[33] = table_disp_name;
                    obj2[34] = table_tooltip_name;
                    obj2[35] = ref_dim_tab_id;
                    obj2[36] = ref_dim_element_id;
                    obj2[37] = calendar_name;
                    obj2[38] = calendar_table;
//                        obj2[39] = dateeid;
                    if (dateeid != null && !dateeid.isEmpty()) {
                        obj2[39] = dateeid;
                    } else {
                        obj2[39] = "null";
                    }
                    obj2[40] = datedetails;
                    obj2[41] = dateoption;

                    buildquery = pbdb.buildQuery(addUserAllInfoDets, obj2);
                    queryieslist.add(buildquery);
                }
                pbdb.executeMultiple(queryieslist);
            }

            status = "success";
        }catch (SQLException ex) {
             logger.error("Exception:", ex);
        }  catch (Exception e) {
            logger.error("Exception:", e);
        }       return status;
        }
    //ended by Nazneen
    public String getConn(String elementId) throws Exception {
        String getReportTimeParamsQuery = "select connection_id from prg_user_all_info_details where element_id=&";
        Object[] data = new Object[1];
        data[0] = elementId;
        String finalQuery = buildQuery(getReportTimeParamsQuery, data);
        PbReturnObject retObj = execSelectSQL(finalQuery);
        String conn = retObj.getFieldValueString(0, 0);
        return conn;
    }

    public String getCalDetails(String connectionID) {
        String Query = "SELECT CALENDER_ID,CALENDER_NAME FROM PRG_CALENDER_SETUP WHERE CONNECTION_ID =" + connectionID;
        PbReturnObject pbro = new PbReturnObject();
        try {
            pbro = new PbDb().execSelectSQL(Query);
        } catch (SQLException ex) {

            logger.error("Exception:", ex);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("<option value='0000'>--SELECT--</option>");
        for (int i = 0; i < pbro.getRowCount(); i++) {
            sb.append("<option value='" + pbro.getFieldValueInt(i, 0) + "'>");
            sb.append(pbro.getFieldValueString(i, 1));
            sb.append("</option>");
        }
        return sb.toString();
    }
    //ended by Nazneen

    public String resetParameterReport(String reportId) {

        String jsonString = null;

        List displaySeqNoList = new ArrayList();
        List paramDisplyNameList = new ArrayList();
        List parametertypeList = new ArrayList();
        List elementIdList = new ArrayList();
        List paramdispIdList = new ArrayList();

        Map map = new HashMap();
        Gson json = new Gson();
        String qry = "select DISP_SEQ_NO, PARAM_DISP_NAME ,PARAMETER_TYPE, ELEMENT_ID,PARAM_DISP_ID from PRG_AR_REPORT_PARAM_DETAILS where REPORT_ID=" + reportId;
        PbReturnObject retobj = new PbReturnObject();
        try {
            retobj = super.execSelectSQL(qry);
            for (int i = 0; i < retobj.getRowCount(); i++) {
                displaySeqNoList.add(retobj.getFieldValueString(i, "DISP_SEQ_NO"));
                paramDisplyNameList.add(retobj.getFieldValueString(i, "PARAM_DISP_NAME"));
                parametertypeList.add(retobj.getFieldValueString(i, "PARAMETER_TYPE"));
                elementIdList.add(retobj.getFieldValueString(i, "ELEMENT_ID"));
                paramdispIdList.add(retobj.getFieldValueString(i, "PARAM_DISP_ID"));
            }

            map.put("displaySeqNoList", displaySeqNoList);
            map.put("paramDisplyNameList", paramDisplyNameList);
            map.put("parametertypeList", parametertypeList);
            map.put("elementIdList", elementIdList);
            map.put("paramdispIdList", paramdispIdList);
            jsonString = json.toJson(map);

        } catch (SQLException ex) {

            logger.error("Exception:", ex);
        }
        return jsonString;
    }

    public void updateRepParams(String reportId, String[] paramIds, HashMap paramidsandTypes, Container container) {
        try {
            String sql = getResourceBundle().getString("updateParams");
            Object[] obj;

            for (int i = 0; i < paramIds.length; i++) {
                obj = new Object[3];
                String finalQuery = "";
                obj[0] = paramidsandTypes.get(paramIds[i]);
                obj[1] = paramIds[i];
                obj[2] = reportId;
                finalQuery = buildQuery(sql, obj);
                execUpdateSQL(finalQuery);
            }
            String qry = "update PRG_AR_REPORT_PARAM_DETAILS set PARAM_DISP_NAME='&' where ELEMENT_ID='&' and REPORT_ID='&'";
            ArrayList RepElementIds = (ArrayList) container.getTableHashMap().get("REP");
            ArrayList RepNames = new ArrayList();
            for (int i = 0; i < RepElementIds.size(); i++) {
                RepNames.add(container.getDisplayLabels().get(i));
            }

            if (RepElementIds != null && RepElementIds.size() > 0) {
                obj = new Object[3];
                String finalQuery = "";
                for (int i = 0; i < RepElementIds.size(); i++) {
                    obj[0] = RepNames.get(i);
                    obj[1] = RepElementIds.get(i);
                    obj[2] = reportId;
                    if (RepNames.get(i) != null && !RepNames.get(i).toString().equalsIgnoreCase("Time") && !RepNames.get(i).toString().equalsIgnoreCase("")
                            && !RepNames.get(i).toString().equalsIgnoreCase("KPI") && !RepNames.get(i).toString().equalsIgnoreCase("None")) {
                        finalQuery = buildQuery(qry, obj);
//                 //added by mohit for kpi and none
                        execUpdateSQL(finalQuery);
                    }
                }
            }

        } catch (Exception ex) {

            logger.error("Exception:", ex);
        }

    }

    public PbReturnObject getAllSchedules() {
        PbReturnObject retObj = new PbReturnObject();
        String finalqry = "Select pr.*,pu.pu_login_id as USERNAME,Pm.Table_Name as REPORTNAME From Prg_Ar_Personalized_Reports pr,Prg_Ar_Users Pu,prg_ar_report_table_master pm Where Pr.Scheduler_Details Is Not Null And Prg_User_Id =pu.pu_id and pm.report_id=pr.prg_report_id";
        try {
            retObj = execSelectSQL(finalqry);
        } catch (Exception ex) {

            logger.error("Exception:", ex);
        }
        return retObj;
    }
    //Added by Ram 31Oct2015 for all schedules Dashboard.

    public PbReturnObject getAllDashboardSchedules() {
        PbReturnObject retObj = new PbReturnObject();
        String finalqry = "select oma.*,u.pu_login_id as USERNAME ,rm.report_name as DASHBOARD_NAME from prg_ar_oneview_measure_alerts oma, prg_ar_report_master rm , Prg_Ar_Users u where oma.Alerts_Details Is Not Null and oma.oneview_id=rm.report_id and oma.prg_user_id=u.pu_id";
        try {
            retObj = execSelectSQL(finalqry);
        } catch (Exception ex) {

            logger.error("Exception:", ex);
        }
        return retObj;
    }
    //Added by Dinanath 04Apr2016 for all KPI Chart schedulers.
    public PbReturnObject getAllKPIChartScheduler() {
        PbReturnObject retObj = new PbReturnObject();
        String finalqry = "Select pr.*,pu.pu_login_id as USERNAME,Pm.Table_Name as REPORTNAME From Prg_Ar_Personalized_kpi pr,Prg_Ar_Users Pu,prg_ar_report_table_master pm Where Pr.Scheduler_Details Is Not Null And Prg_User_Id =pu.pu_id and pm.report_id=pr.prg_report_id";
        try {
            retObj = execSelectSQL(finalqry);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        return retObj;
    }

    public StringBuilder getOneviewMeasureHeasder(OneViewLetDetails oneviewlet, String valu, double currVal, double priorVal, String curval, String chper, String prior) {

        StringBuilder finalStringVal = new StringBuilder();
        String fontcolor;
//         if(oneviewlet.getFontColor()!=null && !oneviewlet.getFontColor().isEmpty() ){
//             fontcolor=oneviewlet.getFontColor();
//         }
//         else
        fontcolor = "#000000";
        finalStringVal.append("<table style='margin-left: 10px;width:" + oneviewlet.getWidth() + "px;'>");
        finalStringVal.append("<tr >");
//            finalStringVal.append("<td width='2%'></td>");
        String reportName = oneviewlet.getRepName();
        String title = reportName;
        int strlength = (oneviewlet.getWidth() / 11);
        if (reportName.length() >= strlength) {
            reportName = reportName.substring(0, strlength - 3);
            reportName += "..";
        }
        if (valu != null) {
            finalStringVal.append("<td id=\"Dashlets" + oneviewlet.getNoOfViewLets() + "\" style='font-size:12pt;color:000000;white-space:nowrap'>");
            finalStringVal.append("<a style=\"\" href=\"javascript:submiturls12('" + valu + "')\">");
            finalStringVal.append("<strong id='forDillDown" + oneviewlet.getNoOfViewLets() + "' style=\"font-size: 12pt;white-space:nowrap;font-weight: normal\" title=\"" + title + "\">" + reportName + "</strong></a>"); //oneviewlet.getRepName()
            finalStringVal.append("</td>");
        } else {
            finalStringVal.append("<td id=\"Dashlets" + oneviewlet.getNoOfViewLets() + "\" style='font-size:12pt;color:#000000;white-space:nowrap;' title=\"" + title + "\" >" + reportName + "</td>");//oneviewlet.getRepName()
        }

        finalStringVal.append("<td id=\"alertTabId" + oneviewlet.getNoOfViewLets() + "\" style='width:0px;align:right;'><a style=\"text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;font-size: 9pt;\" class=\"ui-icon ui-icon-alert\" title=\"Compose Alerts\" onclick=\"oneviewAlerts('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + curval + "','" + chper + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + prior + "','" + oneviewlet.getRoleId() + "')\" href=\"#\"></a></td>");
        finalStringVal.append("<td id=\"refreshTabId" + oneviewlet.getNoOfViewLets() + "\" style='width:1%;align:right;'><a style=\"text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;font-size: 9pt;\" class=\"ui-icon ui-icon-arrowrefresh-1-w\" title=\"Refresh Region\" onclick=\"refreshOneVIewReg(" + oneviewlet.getNoOfViewLets() + ")\" href=\"#\"></a></td>");
        if (oneviewlet.getUserStatus()) {
            finalStringVal.append("<td id=\"saveTabId" + oneviewlet.getNoOfViewLets() + "\" style='width:1%;align:right;'><a style=\"text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;font-size: 9pt;\" class=\"ui-icon ui-icon-disk\" title=\"Save\" onclick=\"saveEachOneVIewReg(" + oneviewlet.getNoOfViewLets() + ")\" href=\"#\"></a></td>");
        }
        if (oneviewlet.getUserStatus()) {
            finalStringVal.append("<td id=\"optionId" + oneviewlet.getNoOfViewLets() + "\" style='width:1%;align:right;'>");
            finalStringVal.append("<a href='javascript:void(0)' class=\"ui-icon ui-icon-triangle-2-n-s\" onclick=\"selectforReadd(" + oneviewlet.getNoOfViewLets() + ")\"  style='text-decoration:none'  title=\"Region Options\"></a>");

            finalStringVal.append("<div id=\"reigonOptionsDivId" + oneviewlet.getNoOfViewLets() + "\" style='display:none;width:120px;height:auto;background-color:white;overflow:auto;position:absolute;text-align:left;border:1px solid #000000;' class=\"overlapDiv\">");
            finalStringVal.append("<table border='0' align='left' >");
            finalStringVal.append("<tr><td>");

            finalStringVal.append("<table><tr><td><a href='javascript:void(0)' style='text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;' onclick=\"renameRegion('Dashlets-" + oneviewlet.getNoOfViewLets() + "'," + oneviewlet.getNoOfViewLets() + ",'Dashlets" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "')\" >Rename</a></td></tr></table>");
            finalStringVal.append("<table><tr><td><a href='javascript:void(0)' style='text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;' onclick=\"drillToReport('" + oneviewlet.getRepName() + "','" + oneviewlet.getRoleId() + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getReptype() + "')\"  >Drill</a></td></tr></table>");
//            finalStringVal.append("<table><tr><td><a href='javascript:void(0)' style='text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;' onclick=\"drillToReport('dashboard','" + oneviewlet.getRepName() + "','" + oneviewlet.getRoleId() + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "')\" >Drill To Dashboard</a></td></tr></table>");
            finalStringVal.append("<table><tr><td><a href='javascript:void(0)' style='text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;' onclick=\"measureOptions('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + curval + "','" + chper + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + prior + "')\" title='MeasuresOptions' href='javascript:void(0)'>Measure Option</a></td></tr></table>");
            finalStringVal.append("<table><tr><td><a href='javascript:void(0)' style='text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;' onclick=\"customTimeAggregation('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "')\" title='customTimeAggregation' href='javascript:void(0)'>CustomTimeAggregation</a></td></tr></table>");
            finalStringVal.append("<table><tr><td><a href='javascript:void(0)' style='text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;' onclick=\"taggleNewMeasures('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + curval + "','','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "')\" title='NewMeasureType' href='javascript:void(0)'>Toggle Display</a></td></tr></table>");
            finalStringVal.append("<table><tr><td><a href='javascript:void(0)' style='text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;' onclick=\"measureTrendGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getRoleId() + "','" + oneviewlet.getHeight() + "','" + oneviewlet.getWidth() + "','" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "')\" title='MeasureTrend' href='javascript:void(0)'>Trend</a></td></tr></table>");
//            finalStringVal.append("<table><tr><td><a href='javascript:void(0)' style='text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;' onclick=\"saveEachOneVIewReg("+oneviewlet.getNoOfViewLets()+")\"  >Save</a></td></tr></table>");
//            finalStringVal.append("<table><tr><td><a  style='text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;'  onclick=\"measureComments('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "')\" href='javascript:void(0)' title='Add/View Comments'>Comments</a></td></tr></table>");
//            if(currVal!=priorVal){
//            if(oneviewlet.isOneviewMeasureCompare()){
//            finalStringVal.append("<table><tr><td><input type='radio' onclick=\"measureNoCompare('nocomp"+oneviewlet.getNoOfViewLets()+"','" + oneviewlet.getRepName() + "','" + oneviewlet.getRoleId() + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "')\" valign='top' href='javascript:void(0)'    id='mesNoCompare"+oneviewlet.getNoOfViewLets()+"'></input>No-Comparision</td></tr></table>");
//            finalStringVal.append("<table><tr><td><input type='radio' onclick=\"measureCompare('comp"+oneviewlet.getNoOfViewLets()+"','" + oneviewlet.getRepName() + "','" + oneviewlet.getRoleId() + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "')\" valign='top' href='javascript:void(0)' checked=''  id='mesCompare"+oneviewlet.getNoOfViewLets()+"' ></input>Comparision</td></tr></table>");
//            }
//            else{
//            finalStringVal.append("<table><tr><td><input type='radio' onclick=\"measureNoCompare('nocomp"+oneviewlet.getNoOfViewLets()+"','" + oneviewlet.getRepName() + "','" + oneviewlet.getRoleId() + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "')\" valign='top' href='javascript:void(0)' checked=''  id='mesNoCompare"+oneviewlet.getNoOfViewLets()+"'></input>No-Comparision</td></tr></table>");
//            finalStringVal.append("<table><tr><td><input type='radio' onclick=\"measureCompare('comp"+oneviewlet.getNoOfViewLets()+"','" + oneviewlet.getRepName() + "','" + oneviewlet.getRoleId() + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "')\" valign='top' href='javascript:void(0)'  id='mesCompare"+oneviewlet.getNoOfViewLets()+"'  ></input>Comparision</td></tr></table>");
//            }
//            }
            finalStringVal.append("</td></tr>");
            finalStringVal.append("</table>");
            finalStringVal.append("</div>");
            finalStringVal.append("</td>");
        }


//            finalStringVal.append("<td id=\"regionId" + oneviewlet.getNoOfViewLets() + "\" style='height:10px;>");
        if (oneviewlet.getUserStatus()) {
            finalStringVal.append("<td id=\"optionId1" + oneviewlet.getNoOfViewLets() + "\" style='width:0px;align:right;'>");
            finalStringVal.append("<a href='javascript:void(0)' class=\"ui-icon ui-icon-plusthick\" onclick=\"selectReadd(" + oneviewlet.getNoOfViewLets() + ")\"  style='text-decoration:none'  title=\"Re Add Onevielet\"></a>");

            finalStringVal.append("<div id=\"readdDivId" + oneviewlet.getNoOfViewLets() + "\" style='display:none; width:90px; height:100px; background-color:#ffffff; overflow:auto;  position:absolute; text-align:left; border:1px solid #000000; border-top-width: 0px; z-index:1002;'>");
            finalStringVal.append("<table border='0' align='left' >");
            finalStringVal.append("<tr><td>");

            finalStringVal.append("<table><tr><td><a href='javascript:void(0)' style='text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;' onclick=\"selectedType('report','Dashlets-" + oneviewlet.getNoOfViewLets() + "'," + oneviewlet.getNoOfViewLets() + "," + oneviewlet.getWidth() + "," + oneviewlet.getHeight() + ",'Dashlets" + oneviewlet.getNoOfViewLets() + "','GrpTyp" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "')\" >Reports</a></td></tr></table>");
            finalStringVal.append("<table><tr><td><a href='javascript:void(0)' style='text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;' onclick=\"selectedType('measures','Dashlets-" + oneviewlet.getNoOfViewLets() + "'," + oneviewlet.getNoOfViewLets() + "," + oneviewlet.getWidth() + "," + oneviewlet.getHeight() + ",'Dashlets" + oneviewlet.getNoOfViewLets() + "','GrpTyp" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "')\" >Measures</a></td></tr></table>");

            finalStringVal.append("<table><tr><td><a href='javascript:void(0)' style='text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;' onclick=\"selectedType('dashboard','Dashlets-" + oneviewlet.getNoOfViewLets() + "'," + oneviewlet.getNoOfViewLets() + "," + oneviewlet.getWidth() + "," + oneviewlet.getHeight() + ",'Dashlets" + oneviewlet.getNoOfViewLets() + "','GrpTyp" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "')\" >KPIs</a></td></tr></table>");
            finalStringVal.append("<table><tr><td><a href='javascript:void(0)' style='text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;' onclick=\"selectedType('headline','Dashlets-" + oneviewlet.getNoOfViewLets() + "'," + oneviewlet.getNoOfViewLets() + "," + oneviewlet.getWidth() + "," + oneviewlet.getHeight() + ",'Dashlets" + oneviewlet.getNoOfViewLets() + "','GrpTyp" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "')\" >Headlines</a></td></tr></table>");
            finalStringVal.append("<table><tr><td><a href='javascript:void(0)' style='text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;white-space:nowrap;' onclick=\"selectedType('complexkpi','Dashlets-" + oneviewlet.getNoOfViewLets() + "'," + oneviewlet.getNoOfViewLets() + "," + oneviewlet.getWidth() + "," + oneviewlet.getHeight() + ",'Dashlets" + oneviewlet.getNoOfViewLets() + "','GrpTyp" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "')\" >Custom KPI</a></td></tr></table>");
            finalStringVal.append("<table><tr><td><a href='javascript:void(0)' style='text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;white-space:nowrap;' onclick=\"selectedType('Date','Dashlets-" + oneviewlet.getNoOfViewLets() + "'," + oneviewlet.getNoOfViewLets() + "," + oneviewlet.getWidth() + "," + oneviewlet.getHeight() + ",'Dashlets" + oneviewlet.getNoOfViewLets() + "','GrpTyp" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "')\" >Date</a></td></tr></table>");
            finalStringVal.append("<table><tr><td><a href='javascript:void(0)' style='text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;white-space:nowrap;' onclick=\"selectedType('notes','Dashlets-" + oneviewlet.getNoOfViewLets() + "'," + oneviewlet.getNoOfViewLets() + "," + oneviewlet.getWidth() + "," + oneviewlet.getHeight() + ",'Dashlets" + oneviewlet.getNoOfViewLets() + "','GrpTyp" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "')\" >Notes</a></td></tr></table>");
            finalStringVal.append("</td></tr>");
            finalStringVal.append("</table>");
            finalStringVal.append("</div>");
            finalStringVal.append("</td>");
        }
        finalStringVal.append("</tr></table>");
        return finalStringVal;
    }

    //added by Nazneen
    public PbReturnObject getAllSchedulerLoadDetails() {
        String query = getResourceBundle().getString("getAllSchedulerLoadDetails");
        PbReturnObject retObj1 = new PbReturnObject();
        try {
            retObj1 = execSelectSQL(query);
        } catch (SQLException ex) {

            logger.error("Exception:", ex);
        }
        return retObj1;
    }

    public PbReturnObject getEmailConfigDetails() {
        String query = "select HOST_NAME,PORT_NO,FROM_ADD,DEBUG,USER_ID,PWD,ssl_status from PRG_CONFIG_EMAIL";
        PbReturnObject returnObj = new PbReturnObject();

        try {
            returnObj = execSelectSQL(query);
        } catch (SQLException ex) {

            logger.error("Exception:", ex);
        }
        return returnObj;
    }
//            public String  defineCustomSequence(String reportid,String graphId,String folderId,String grpId,HttpSession session,HttpServletRequest request)
//       {
//           String tamp=null;
//           Container container = null;
//           ArrayList crosstablist=new ArrayList();
//           HashMap map = (HashMap) request.getSession(false).getAttribute("PROGENTABLES");
//           HashMap TableHashMap = null;
//           int columnViewbyCount = 0;
//           int ViewbyCount = 0;
//           container = (Container) map.get(reportid);
//           ArrayList displayLabels = container.getDisplayLabels();
//           PbReturnObject pbretObj = new PbReturnObject();
//           pbretObj = (PbReturnObject) container.getRetObj();
//           ArrayList crosstaborder= pbretObj.CrossTabfinalOrder;
//           int measCnt=container.getReportMeasureCount();
//           int b=displayLabels.size();
//           ArrayList tempStr=null;
//           ArrayList corsstablist=new ArrayList();
//           StringBuffer crosstabbuffer= new StringBuffer();
//           crosstabbuffer.append("<ul id='sortable' class='sortable ui-sortable'>");
//           corsstablist.add("Grand Total");
//            for (int i = container.getViewByCount(); i < b; i++)
//             {
//                tempStr=new ArrayList();
//                tempStr= (ArrayList) container.getDisplayLabels().get(i);
//
//                if(!corsstablist.contains(tempStr.get(tempStr.size()-2)))
//                     {
//                         corsstablist.add(tempStr.get(tempStr.size()-2));
//                         crosstabbuffer.append("<li id='li-"+crosstaborder.get(i-1) +","+crosstaborder.get(i) +"' class='navtitle-hover' style='width: 180px; height: 30; color: white;'>");
//                         crosstabbuffer.append("<span>"+tempStr.get(tempStr.size()-2)+"</span>");
//                         crosstabbuffer.append("</li>");
//                     }
//              }
//           crosstabbuffer.append("<input type='hidden' name='custseqsubid' id='custseqsubid' class='navtitle-hover' value='"+reportid+"' onclick=''/>");
//    return  crosstabbuffer.append("</ul>").toString();
//       }

    public String defineCustomSequence(String reportid, String graphId, String folderId, String grpId, HttpSession session, HttpServletRequest request) {
        String tamp = null;
        Container container = null;
        ArrayList crosstablist = new ArrayList();

        HashMap map = (HashMap) request.getSession(false).getAttribute("PROGENTABLES");
        HashMap TableHashMap = null;
        int columnViewbyCount = 0;
        int ViewbyCount = 0;
        container = (Container) map.get(reportid);
        ArrayList displayLabels = container.getDisplayLabels();
        int measCnt = container.getReportMeasureCount();
        int b = displayLabels.size();
        PbReportCollection collect = container.getReportCollect();
        String colViewby = collect.reportColViewbyValues.get(0);
        ArrayList tempStr = null;
        ArrayList corsstablist = new ArrayList();
        StringBuffer crosstabbuffer = new StringBuffer();
        crosstabbuffer.append("<ul id='sortable' class='sortable ui-sortable'>");
        corsstablist.add("Grand Total");
        for (int i = container.getViewByCount(); i < b; i++) {
            tempStr = new ArrayList();
            tempStr = (ArrayList) container.getDisplayLabels().get(i);

            if (!corsstablist.contains(tempStr.get(tempStr.size() - 2))) {
                corsstablist.add(tempStr.get(tempStr.size() - 2));
                crosstabbuffer.append("<li id='li-" + tempStr.get(tempStr.size() - 2) + "' class='navtitle-hover' style='width: 180px; height: 30; color: white;'>");
                crosstabbuffer.append("<span>" + tempStr.get(tempStr.size() - 2) + "</span>");
                crosstabbuffer.append("</li>");
            }
        }
        crosstabbuffer.append("<input type='hidden' name='custseqsubid' id='custseqsubid' class='navtitle-hover' value='" + reportid + "' onclick=''/>");
        crosstabbuffer.append("</ul>").toString();
        crosstabbuffer.append("<input type='button' onclick=\"reOrderCustSeq('" + colViewby + "')\"  style='width:55px;' class='navtitle-hover' value='Done'>");
        return crosstabbuffer.toString();
    }

    public void reOrderCustomSeq(String totalurl, String action, String reportId, HttpServletRequest request, String colviewby) {
        String[] crosstabelements = totalurl.split(",");
        PbReturnObject retobj = new PbReturnObject();
        Container container = Container.getContainerFromSession(request, reportId);
        PbReportCollection collect = new PbReportCollection();
        collect = container.getReportCollect();
        Object[] obj = new Object[3];

        retobj = (PbReturnObject) container.getRetObj();
        if (collect.reportColViewbyValues != null && !collect.reportColViewbyValues.isEmpty()) {
            retobj.setCrosstabelements(crosstabelements, colviewby);
            collect.setCrosstabelements(crosstabelements, colviewby);
        }
        obj[0] = totalurl;
        obj[1] = colviewby;
        obj[2] = reportId;
        String finalquery = getResourceBundle().getString("UpdatecrossTabSequence");
        finalquery = buildQuery(finalquery, obj);
        try {
            execUpdateSQL(finalquery);
        } catch (Exception ex) {

            logger.error("Exception:", ex);
        }
        retobj.setElementaction(action);

    }

    public void saveInnerViewbys(String reportId, String elementId) {

        String qry;
        String finalqry;
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
            qry = "insert into PRG_AR_ADDITIONAL_VIEWBY(REPORT_ID,ELEMENT_ID) values('&','&')";
        } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
            qry = "insert into PRG_AR_ADDITIONAL_VIEWBY(REPORT_ID,ELEMENT_ID) values('&','&')";
        } else {
            qry = "insert into PRG_AR_ADDITIONAL_VIEWBY(ASSIGN_ID,REPORT_ID,ELEMENT_ID) values(PRG_AR_ADDITIONAL_VIEWBY_SEQ.nextval,'&','&')";
        }
        Object[] obj = new Object[2];
        obj[0] = reportId;
        obj[1] = elementId;
        finalqry = buildQuery(qry, obj);
//            
        try {
            execUpdateSQL(finalqry);
        } catch (Exception ex) {

            logger.error("Exception:", ex);
        }
    }
    //added for getting jqplot graph properties

    public JqplotGraphProperty getJqGraphDetails(String graphid) {
        String sql = getResourceBundle().getString("JqGraphProperties");
        JqplotGraphProperty jqProperty = null;
        Object[] objArr = new Object[1];
        objArr[0] = graphid;
        try {
            PbReturnObject pbReturnObject = super.execSelectSQL(super.buildQuery(sql, objArr));
            if (pbReturnObject.getRowCount() > 0 && pbReturnObject.getFieldUnknown(0, 0) != null) {
                Gson gson = new GsonBuilder().serializeNulls().create();
                jqProperty = gson.fromJson(pbReturnObject.getFieldUnknown(0, 0), JqplotGraphProperty.class);
            }
        } 
        catch (SQLException ex) {
            logger.error("Exception:", ex);
        }catch (Exception ex) {
            logger.error("Exception:", ex);
        }        return jqProperty;
        }

    public String getLastUpdatedDate(Connection con, String elementID) {
        String date = "";
        PbReturnObject returnObject = null;
        if (con != null) {
            String query = "";
            GetConnectionType gettypeofconn = new GetConnectionType();
            String connType = gettypeofconn.getConTypeByElementId(elementID);
            if (connType.equalsIgnoreCase("sqlserver")) {
                query = "select Last_Update_Date from PRG_Last_Update_Table";
            } else {
                if (connType.equalsIgnoreCase("Mysql")) {
                    query = "SELECT * FROM Prg_Last_Update_Table";
                } else {
                    query = "SELECT TO_CHAR(Last_Update_Date, 'MM/DD/yyyy hh24:mi:ss') FROM Prg_Last_Update_Table";
                }
            }

            try {
                returnObject = execSelectSQL(query, con);
                if (returnObject != null && returnObject.getRowCount() > 0) {
                    date = returnObject.getFieldValueString(0, 0);
                }
            } catch (Exception ex) {

                logger.error("Exception:", ex);
            }

        }

        return date;
    }

    public String getAllDateEids(String bussTableId) {
        String qry = null;
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
            qry = "Select ELEMENT_ID,USER_COL_DESC,BUSS_TABLE_NAME,BUSS_COL_NAME From Prg_User_All_Info_Details Where Buss_Table_Id=" + bussTableId + " and user_col_type in ('DATE')";
        } else {
            qry = "Select ELEMENT_ID,USER_COL_DESC,BUSS_TABLE_NAME,BUSS_COL_NAME From Prg_User_All_Info_Details Where Buss_Table_Id=" + bussTableId + " and user_col_type in 'DATE'";
        }
        PbReturnObject returnObject = null;
        HashMap<String, ArrayList> map = new HashMap<String, ArrayList>();
        ArrayList<String> eIdlist = new ArrayList<String>();
        ArrayList<String> eNamelist = new ArrayList<String>();
        ArrayList<String> tablenamelist = new ArrayList<String>();
        Gson gson = new Gson();
        String jsonString = null;
        try {
            returnObject = execSelectSQL(qry);
            if (returnObject != null && returnObject.getRowCount() > 0) {
                for (int i = 0; i < returnObject.getRowCount(); i++) {
                    eIdlist.add(returnObject.getFieldValueString(i, 0));
                    eNamelist.add(returnObject.getFieldValueString(i, 1));
                    tablenamelist.add(returnObject.getFieldValueString(i, 2) + "." + returnObject.getFieldValueString(i, 3));
                }
            }
            map.put("eIdlist", eIdlist);
            map.put("eNamelist", eNamelist);
            map.put("tablenamelist", tablenamelist);
            jsonString = gson.toJson(map);

        } catch (SQLException ex) {

            logger.error("Exception:", ex);
        }
        return jsonString;
    }

    public String buildGttotals(String[] viewByElementIds, String[] barChartColumnNames, String[] barChartColumnTitles, String graphdisptyp, String[] displaycolumns1) {
        StringBuffer graphBuffer = new StringBuffer("");
        String graphdisptype = "-" + graphdisptyp;
        if (displaycolumns1 != null && barChartColumnNames != null && barChartColumnTitles != null) {
            for (int i = 0; i < displaycolumns1.length; i++) {
                String graphdispt = displaycolumns1[i].toString().substring(displaycolumns1[i].toString().lastIndexOf("-"));
                graphBuffer.append(" <li id='GrpCol" + displaycolumns1[i].replace("A_", "").replace("[", "").replace("]", "") + "' style='width:auto;height:auto;color:white' class='navtitle-hover'>");
                graphBuffer.append("<table id='GrpTab" + displaycolumns1[i].replace("A_", "").replace("[", "").replace("]", "") + "'>");
                graphBuffer.append(" <tr>");
                graphBuffer.append(" <td >");
                graphBuffer.append("  <a href=\"javascript:deleteColumn('GrpCol" + displaycolumns1[i].replace("A_", "").replace("[", "").replace("]", "") + "')\" class=\"ui-icon ui-icon-close\"></a>");
                graphBuffer.append("</td>");
                graphBuffer.append("<td id=\"eleName" + displaycolumns1[i].replace("A_", "").replace("[", "").replace("]", "") + "\" style=\"color:black\">" + barChartColumnTitles[i].replace("[", "").replace("]", "").concat("" + graphdispt + "") + "</td>");
                graphBuffer.append("</tr>");
                graphBuffer.append("</table>");
                graphBuffer.append("</li>");
            }
        } else if (viewByElementIds != null && barChartColumnNames != null && barChartColumnTitles != null) {
            for (int i = viewByElementIds.length; i < barChartColumnNames.length; i++) {
                graphBuffer.append(" <li id='GrpCol" + barChartColumnNames[i].replace("A_", "").replace("[", "").replace("]", "").concat("" + graphdisptype + "") + "' style='width:auto;height:auto;color:white' class='navtitle-hover'>");
                graphBuffer.append("<table id='GrpTab" + barChartColumnNames[i].replace("A_", "").replace("[", "").replace("]", "").concat("" + graphdisptype + "") + "'>");
                graphBuffer.append(" <tr>");
                graphBuffer.append(" <td >");
                graphBuffer.append("  <a href=\"javascript:deleteColumn('GrpCol" + barChartColumnNames[i].replace("A_", "").replace("[", "").replace("]", "").concat("" + graphdisptype + "") + "')\" class=\"ui-icon ui-icon-close\"></a>");
                graphBuffer.append("</td>");
                graphBuffer.append("<td id=\"eleName" + barChartColumnNames[i].replace("A_", "").replace("[", "").replace("]", "").concat("" + graphdisptype + "") + "\" style=\"color:black\">" + barChartColumnTitles[i].replace("[", "").replace("]", "").concat("" + graphdisptype + "") + "</td>");
                graphBuffer.append("</tr>");
                graphBuffer.append("</table>");
                graphBuffer.append("</li>");
            }
        }

        return graphBuffer.toString();
    }

    public String buildGtmeasures(String[] viewByElementIds, String[] barChartColumnNames, String[] barChartColumnTitles) {
        StringBuffer graphBuffer = new StringBuffer("");

        if (viewByElementIds != null && barChartColumnNames != null && barChartColumnTitles != null) {
            for (int i = viewByElementIds.length; i < barChartColumnNames.length; i++) {
                graphBuffer.append("<li><ul>");
                graphBuffer.append("<li class=\"closed\" id='" + barChartColumnNames[i].replace("A_", "").replace("[", "").replace("]", "").concat("-GT") + "' style='width:auto;height:auto;color:white' class='navtitle-hover'>");
                graphBuffer.append("<span class=\"ui-draggable\" id=\"" + barChartColumnNames[i].replace("A_", "").replace("[", "").replace("]", "").concat("-GT") + "\" style=\"color:black\">" + barChartColumnTitles[i].replace("[", "").replace("]", "").concat("-GT") + "</span>");
                graphBuffer.append("</li>");
                graphBuffer.append("<li class=\"closed\" id='" + barChartColumnNames[i].replace("A_", "").replace("[", "").replace("]", "").concat("-AVG") + "' style='width:auto;height:auto;color:white' class='navtitle-hover'>");
                graphBuffer.append("<span class=\"ui-draggable\" id=\"" + barChartColumnNames[i].replace("A_", "").replace("[", "").replace("]", "").concat("-AVG") + "\" style=\"color:black\">" + barChartColumnTitles[i].replace("[", "").replace("]", "").concat("-AVG") + "</span>");
                graphBuffer.append("</li>");
                graphBuffer.append("<li class=\"closed\" id='" + barChartColumnNames[i].replace("A_", "").replace("[", "").replace("]", "").concat("-ST") + "' style='width:auto;height:auto;color:white' class='navtitle-hover'>");
                graphBuffer.append("<span class=\"ui-draggable\" id=\"" + barChartColumnNames[i].replace("A_", "").replace("[", "").replace("]", "").concat("-ST") + "\" style=\"color:black\">" + barChartColumnTitles[i].replace("[", "").replace("]", "").concat("-ST") + "</span>");
                graphBuffer.append("</li></li></ul>");
            }
        }

        return graphBuffer.toString();
    }

    public HashMap<String, ArrayList> getAllReportsRelatedtoRole(String reportId, String userId, Container container) {
        String qry = "SELECT FOLDER_ID,FOLDER_NAME From Prg_User_Folder Where Folder_Id In(select D.FOLDER_ID from PRG_AR_REPORT_DETAILS d,PRG_AR_REPORT_MASTER m where d.REPORT_ID=M.REPORT_ID and M.REPORT_ID=" + reportId + ")";
        String roleId = null, roleName = null;
        PbReturnObject returnObject = null;
        List<ReportDetails> reportDetails = null;
        HashMap<String, ArrayList> resultMap = new HashMap<String, ArrayList>();
        try {
            returnObject = execSelectSQL(qry);
            if (returnObject != null && returnObject.getRowCount() > 0) {
                roleId = returnObject.getFieldValueString(0, 0);
                roleName = returnObject.getFieldValueString(0, 1);
            }
            ArrayList role = new ArrayList();
            role.add(roleName);
            HashMap<String, ArrayList> reportMap = this.getRoleRelatedReports(roleId, userId);
            logger.info("***container.getTableMeasure()" + container.getTableMeasure());
            logger.info("***container.getDisplayColumns()" + container.getDisplayColumns());
//         ArrayList columns=container.getDisplayColumns();
//         ArrayList labels=container.getDisplayLabels();
            ArrayList columns = container.getTableMeasure();
            ArrayList labels = container.getTableMeasureNames();
            ArrayList assignRepIds = this.getAssignedReportIds(columns, reportId);

//         for(int i=0;i<container.getViewByCount();i++)
//         {
//             columns.remove(i);
//             labels.remove(i);
//         }
            resultMap.put("roleName", role);
            resultMap.put("MsrIds", columns);
            resultMap.put("MsrNames", labels);
            resultMap.put("reportIds", (ArrayList) reportMap.get("reportIds"));
            resultMap.put("reportNames", (ArrayList) reportMap.get("reportNames"));
            resultMap.put("assignRepIds", assignRepIds);

        } catch (SQLException ex) {

            logger.error("Exception:", ex);
        }
        return resultMap;
    }

    public HashMap<String, ArrayList> getRoleRelatedReports(String roleId, String userId) {
        HashMap<String, ArrayList> map = new HashMap<String, ArrayList>();
        try {
            String qry = "SELECT DISTINCT p.report_id,p.report_name FROM prg_ar_report_master p WHERE p.report_type='R' and p.report_id IN  (SELECT A.REPORT_ID  FROM PRG_AR_REPORT_DETAILS a,    prg_ar_user_reports b  WHERE a.REPORT_ID = b.REPORT_ID  AND b.USER_ID =" + userId + " AND a.FOLDER_ID=" + roleId + ") ORDER BY  p.report_name";
            PbReturnObject returnObject = null;
            returnObject = execSelectSQL(qry);
            ArrayList reportIds = new ArrayList();
            ArrayList reportNames = new ArrayList();
            if (returnObject != null && returnObject.getRowCount() > 0) {
                for (int i = 0; i < returnObject.getRowCount(); i++) {
                    reportIds.add(returnObject.getFieldValueString(i, 0));
                    reportNames.add(returnObject.getFieldValueString(i, 1));
                }
                map.put("reportIds", reportIds);
                map.put("reportNames", reportNames);
            }

        } catch (SQLException ex) {

            logger.error("Exception:", ex);
        }
        return map;
    }

    public void saveDrillAssignmentReports(String reportId, String[] msrIds, String[] assignReportIds, Container container, String[] multireportIds) {
        HashMap<String, String> map = new HashMap<String, String>();
        String qry = null;
        for (int i = 0; i < msrIds.length; i++) {
            if (container.getMsrDrillReportSelection().equalsIgnoreCase("multi report")) {
                qry = "update PRG_AR_REPORT_TABLE_DETAILS set MULTI_REPORT_DRILL_ASSIGN='" + multireportIds[i] + "' where QRY_COL_ID in(select QRY_COL_ID from PRG_AR_QUERY_DETAIL where ELEMENT_ID=" + msrIds[i] + " and REPORT_ID=" + reportId + ")";
                container.setReportDrillMap("A_" + msrIds[i], multireportIds[i]);
            } else {
                qry = "update PRG_AR_REPORT_TABLE_DETAILS set REPORT_DRILL_ASSIGN_REPORT=" + assignReportIds[i] + " where QRY_COL_ID in(select QRY_COL_ID from PRG_AR_QUERY_DETAIL where ELEMENT_ID=" + msrIds[i] + " and REPORT_ID=" + reportId + ")";
                container.setReportDrillMap("A_" + msrIds[i], assignReportIds[i]);
            }
//          map.put(msrIds[i].replace("A_",""), assignReportIds[i]);
            try {
                execUpdateSQL(qry);
            } catch (Exception ex) {

                logger.error("Exception:", ex);
            }
        }

        qry = "update PRG_AR_REPORT_MASTER set REPORT_SELCTION_REPORT_DRILL='" + container.getMsrDrillReportSelection() + "' where REPORT_ID=" + reportId;
        try {
            execUpdateSQL(qry);
        } catch (Exception ex) {

            logger.error("Exception:", ex);
        }
//      
//     boolean flag=this.executeMultiple(list);
//      
//      return map;
    }

    public ArrayList getAssignedReportIds(ArrayList columns, String reportId) {
        ArrayList assignRepIds = new ArrayList();
        PbReturnObject returnObject = new PbReturnObject();
        for (int i = 0; i < columns.size(); i++) {
            String qry = "select REPORT_DRILL_ASSIGN_REPORT from PRG_AR_REPORT_TABLE_DETAILS WHERE QRY_COL_ID IN (SELECT QRY_COL_ID FROM PRG_AR_QUERY_DETAIL WHERE ELEMENT_ID=" + columns.get(i) + " AND REPORT_ID=" + reportId + ")";
            try {
                returnObject = execSelectSQL(qry);
            } catch (SQLException ex) {

                logger.error("Exception:", ex);
            }
            if (returnObject != null && returnObject.rowCount > 0) {
                assignRepIds.add(returnObject.getFieldValueString(0, 0));
            }
        }
        //  
        return assignRepIds;

    }

    public HashMap<String, String> getReportIdndNamesforMultiSelectDrill(String reportId, String msrId, Container container, String userId) {
        String qry = "SELECT FOLDER_ID,FOLDER_NAME From Prg_User_Folder Where Folder_Id In(select D.FOLDER_ID from PRG_AR_REPORT_DETAILS d,PRG_AR_REPORT_MASTER m where d.REPORT_ID=M.REPORT_ID and M.REPORT_ID=" + reportId + ")";
        String roleId = null;
        PbReturnObject returnObject = null;
        HashMap<String, String> resultMap = new HashMap<String, String>();
        try {
            returnObject = execSelectSQL(qry);
            if (returnObject != null && returnObject.getRowCount() > 0) {
                roleId = returnObject.getFieldValueString(0, 0);
            }

        } catch (SQLException ex) {

            logger.error("Exception:", ex);
        }


        HashMap<String, ArrayList> reportMap = this.getRoleRelatedReports(roleId, userId);
        ArrayList reportIdlist = (ArrayList) reportMap.get("reportIds");
        ArrayList reportNameslist = (ArrayList) reportMap.get("reportNames");
        String assignRepIds = container.getReportDrillMap("A_" + msrId);
        if (assignRepIds != null && !assignRepIds.isEmpty() && !assignRepIds.equalsIgnoreCase("0")) {
            if (assignRepIds.contains(",")) {
                String[] reportIds = assignRepIds.split(",");
                for (int i = 0; i < reportIds.length; i++) {
                    resultMap.put(reportIds[i], (String) reportNameslist.get(reportIdlist.indexOf(reportIds[i])));
                }
            } else {
                resultMap.put(assignRepIds, (String) reportNameslist.get(reportIdlist.indexOf(assignRepIds)));
            }
        }
        return resultMap;

    }

    public HashMap<String, List<String>> getYearLevelForTargetMeas(String elementId, String levelType) {
        List<String> levelList = new ArrayList<String>();
        List<String> custmYear = new ArrayList<String>();
        String finalQuery = "select distinct cyear from pr_day_denom order by cyear asc";
        if (levelType.equalsIgnoreCase("Year")) {
            finalQuery = "select distinct cyear from pr_day_denom order by cyear asc";
        } else if (levelType.equalsIgnoreCase("Quarter")) {
            finalQuery = "select distinct cq_end_date from pr_day_denom order by cq_end_date asc";
        } else if (levelType.equalsIgnoreCase("Month")) {
            finalQuery = "select distinct cm_end_date from pr_day_denom order by cm_end_date asc";
        }
        HashMap<String, List<String>> measMap = new HashMap<String, List<String>>();
        PbReturnObject pbretObj = null;
        Connection con = null;
        try {
            con = ProgenConnection.getInstance().getConnectionForElement(elementId);
            if (con != null) {
                pbretObj = execSelectSQL(finalQuery, con);
                if (pbretObj != null && pbretObj.rowCount > 0 && levelType.equalsIgnoreCase("Year")) {
                    for (int i = 0; i < pbretObj.rowCount; i++) {
                        String year = pbretObj.getFieldValueString(i, 0);
                        PbReturnObject ret = new PbReturnObject();
                        con = ProgenConnection.getInstance().getConnectionForElement(elementId);
                        ret = execSelectSQL("select min(ddate) from pr_day_denom where cyear =" + year, con);
                        levelList.add(ret.getFieldValueDateString(0, 0));
                        custmYear.add(year);

                    }
                    measMap.put("DDATE", levelList);
                    measMap.put("CUSTOMYEAR", custmYear);
                } else if (pbretObj != null && pbretObj.rowCount > 0 && (levelType.equalsIgnoreCase("Quarter") || levelType.equalsIgnoreCase("Month"))) {
                    for (int i = 0; i < pbretObj.rowCount; i++) {
                        levelList.add(pbretObj.getFieldValueDateString(i, 0));
                    }
                }
            }
        } catch (Exception ex) {

            logger.error("Exception:", ex);
        }

        return measMap;
    }

    public PbReturnObject getAllOneviewMeasuresAlerts() {
        String qry = getResourceBundle().getString("getAllOneviewMeasueAlerts");
        PbReturnObject retObj = new PbReturnObject();
        try {
            retObj = execSelectSQL(qry);
        } catch (Exception e) {
            logger.error("Exception:", e);
        }
        return retObj;
    }

    public boolean saveInitializeReportValues(String reportId, HashMap assignedMap, String[] viewByIds) {
        boolean status = false;
        String qry = getResourceBundle().getString("updateInitialReportAssignment");
        Object[] obj = new Object[3];
        Object assRepId = "";
        String finalQuery;
        ArrayList list = new ArrayList();
        for (int i = 0; i < viewByIds.length; i++) {
//             if(assignedMap.get(viewByIds[i]) ==null) {
//                 assRepId="";
//             }else{
            assRepId = assignedMap.get(viewByIds[i]);
//             }
            obj[0] = assRepId;
            obj[1] = viewByIds[i];
            obj[2] = reportId;
            finalQuery = buildQuery(qry, obj);
            list.add(finalQuery);
        }
        status = executeMultiple(list);
        return status;
    }

    public HashMap getAssignedInitialReports(String reportId) {
        HashMap assignedMap = new HashMap();
        String query = getResourceBundle().getString("assignedInitialReports");
        Object[] obj = new Object[1];
        obj[0] = reportId;
        String finalQuery = buildQuery(query, obj);
        PbReturnObject retObj = null;
        try {
            retObj = execSelectSQL(finalQuery);
            if (retObj != null) {
                for (int i = 0; i < retObj.rowCount; i++) {
                    assignedMap.put(retObj.getFieldValueString(i, 0), retObj.getFieldValueString(i, 1));
                }
            }
        } 
        catch (SQLException ex) {
            logger.error("Exception:", ex);
        }catch (Exception e) {
            logger.error("Exception:", e);
        }        return assignedMap;
        }

    public boolean isViewByMatchedToInitialize(String reportId, String viewById) {
        boolean status = false;
        String query = "SELECT DEFAULT_VALUE FROM PRG_AR_REPORT_VIEW_BY_MASTER WHERE REPORT_ID=" + reportId;
        try {
            PbReturnObject retObj = execSelectSQL(query);
            if (retObj != null) {
                if (retObj.rowCount == 1) {
                    String tarViewById = retObj.getFieldValueString(0, 0);
                    if (viewById.equalsIgnoreCase(tarViewById)) {
                        status = true;
                    }
                }
            }
        } 
        catch (SQLException ex) {
            logger.error("Exception:", ex);
        }catch (Exception e) {
            logger.error("Exception:", e);
        }        return status;
        }

    public String deleteViewBys(ArrayList<String> viewById, ArrayList<String> viewByNames, String dropTar, String ctxPath) {
        StringBuffer graphBuffer = new StringBuffer("");
        if (viewById != null && viewByNames != null) {
            for (int i = 0; i < viewById.size(); i++) {
                graphBuffer.append(" <li class='navtitle-hover' id='" + viewByNames.get(i) + "@" + viewById.get(i) + "' style='width:auto;height:auto;color:white' >");
                graphBuffer.append("<table id='viewTab" + viewById.get(i) + "'>");
                graphBuffer.append(" <tr>");
                if (dropTar.equalsIgnoreCase("removeDim")) {
                    graphBuffer.append(" <td >");
                    graphBuffer.append("<img src=\"" + ctxPath + "/icons pinvoke/cross-small.png\" style=\"cursor:pointer\" onclick=\"javascript:deleteColumnParameters('" + viewByNames.get(i) + "@" + viewById.get(i) + "','" + viewByNames.get(i) + "')\" />");
                    graphBuffer.append("</td>");
                }
                graphBuffer.append("<td style=\"color:black\">" + viewByNames.get(i) + "</td>");
                graphBuffer.append("</tr>");
                graphBuffer.append("</table>");
                graphBuffer.append("</li>");
            }
        }
        return graphBuffer.toString();
    }
    //modified by Dinanath

    public void setOrganizationNames(Container container, String userId) {
        PbReturnObject returnObject = null;
        PbReportCollection collect1 = new PbReportCollection();
        collect1 = container.getReportCollect();
        String element_id = collect1.elementId;
        if (element_id != null && !element_id.equalsIgnoreCase("")) {
            String connectionId = "";
            String secrTable = "";
            String secrdTable = "";
            String secrdMainTab = "";
            String secrColName = "";
            String secrJoin = "";
            String secrDispCol = "";
            String secrMainCol = "";
            String label = "";
//        String companyName = "";
//        String companyId = "";//added by Dinanath
            StringBuilder companyName = new StringBuilder(1000);
            StringBuilder companyId = new StringBuilder(1000);
//        String orgCompNames = "";
            StringBuilder orgCompNames = new StringBuilder(200);
            int totalComp = 0;
            String srtUpCharVal = "";
            try {
                String qery1 = "SELECT CONNECTION_ID FROM PRG_USER_ALL_INFO_DETAILS WHERE ELEMENT_ID =" + element_id;
                returnObject = execSelectSQL(qery1);
                if (returnObject.getRowCount() > 0) {
                    connectionId = returnObject.getFieldValueString(0, 0);
                }
                String qery4 = "SELECT SETUP_CHAR_VALUE FROM PRG_GBL_SETUP_VALUES WHERE SETUP_KEY = 'MULTI_COMPANY_DISP'";
                returnObject = execSelectSQL(qery4);
                if (returnObject.getRowCount() > 0) {
                    srtUpCharVal = returnObject.getFieldValueString(0, 0);
                }
                if (!srtUpCharVal.isEmpty() && srtUpCharVal.toUpperCase().equalsIgnoreCase("YES")) {
                    String qery2 = "select * from PRG_USER_SECURE_MAPPING where CONNECTION_ID=" + connectionId;
                    returnObject = execSelectSQL(qery2);
                    if (returnObject != null && returnObject.getRowCount() >= 1) {
                        secrTable = returnObject.getFieldValueString(0, 2);
                        secrdTable = returnObject.getFieldValueString(0, 3);
                        secrdMainTab = returnObject.getFieldValueString(0, 4);
                        secrColName = returnObject.getFieldValueString(0, 5);
                        secrJoin = returnObject.getFieldValueString(0, 6);
                        secrDispCol = returnObject.getFieldValueString(0, 7);
                        secrMainCol = returnObject.getFieldValueString(0, 8);
                        label = returnObject.getFieldValueString(0, 9);
                        //String qery3 = "select " + secrTable + "." + secrDispCol + " from " + secrTable + "," + secrdTable + " where " + secrJoin + " and " + secrColName + " = " + userId + " order by 1 ";
                        String qery3 = "select " + secrTable + "." + secrDispCol + "," + secrTable + "." + secrdMainTab + " from " + secrTable + "," + secrdTable + " where " + secrJoin + " and " + secrColName + " = " + userId + " order by 1 ";
                        returnObject = execSelectSQL(qery3);
                        if (returnObject != null) {
                            for (int i = 0; i < returnObject.rowCount; i++) {
                                if (returnObject.getFieldValueString(i, 0).length() < 20) {
//                                companyName = companyName + returnObject.getFieldValueString(i, 0) + ",";
                                    companyName.append(returnObject.getFieldValueString(i, 0)).append(",");
//                                companyId = companyId + returnObject.getFieldValueString(i, 1) + ",";
                                    companyId.append(returnObject.getFieldValueString(i, 1)).append(",");
                                } else {
//                                companyName = companyName + returnObject.getFieldValueString(i, 0).substring(0, 20) + "..,";
//                                companyId = companyId + returnObject.getFieldValueString(i, 0).substring(0, 20) + "..,";
                                    companyName.append(returnObject.getFieldValueString(i, 0).substring(0, 20)).append("..,");
                                    companyId.append(returnObject.getFieldValueString(i, 0).substring(0, 20)).append("..,");
                                }
                                orgCompNames.append(returnObject.getFieldValueString(i, 0)).append(",");
//                            orgCompNames = orgCompNames + returnObject.getFieldValueString(i, 0) + ",";
                                totalComp = totalComp + 1;
                            }
                        }
                        int len = companyName.length();
                        int len2 = companyId.length();
                        int len1 = orgCompNames.length();
                        if (len > 100) {
//                        companyName = companyName.substring(0, 100);
//                        companyId = companyId.substring(0, len2 - 1);
                            companyName = new StringBuilder(companyName.substring(0, 100));
                            companyId = new StringBuilder(companyId.substring(0, len2 - 1));
                        } else {
//                        companyName = companyName.substring(0, len - 1);
//                        companyId = companyId.substring(0, len2 - 1);
                            companyName = new StringBuilder(companyName.substring(0, len - 1));
                            companyId = new StringBuilder(companyId.substring(0, len2 - 1));
                        }
//                    orgCompNames = orgCompNames.substring(0, len1 - 1);
                        orgCompNames = new StringBuilder(orgCompNames.substring(0, len1 - 1));
                    }
                }
                container.companyName = companyName.toString();
                container.companyId = companyId.toString();
                container.orgCompNames = orgCompNames.toString();
                container.totalComp = totalComp;
                container.srtUpCharVal = srtUpCharVal;

            } 
            catch (SQLException ex) {
                logger.error("Exception:", ex);
            }catch (Exception e) {
                logger.error("Exception:", e);
            }        }
            }
    //add by sumeet start

    public HashMap modifyMeasure(String reportId) {
        HashMap map = new HashMap();
        ArrayList<String> measureName = new ArrayList<String>();
        ArrayList<String> measureId = new ArrayList<String>();
        ArrayList<String> symbols = new ArrayList<String>();
        ArrayList<String> Alignment = new ArrayList<String>();
        ArrayList<String> fontcolor = new ArrayList<String>();
        ArrayList<String> bgcolor = new ArrayList<String>();
        ArrayList<String> negative_val = new ArrayList<String>();
        ArrayList<String> no_format = new ArrayList<String>();
        ArrayList<String> round = new ArrayList<String>();
        Map<String, String> fcolorsmap = new HashMap<String, String>();
        fcolorsmap = getcolors();
        PbReturnObject retObj = new PbReturnObject();
        String getMeasures = getResourceBundle().getString("getReportMeasuresName");
        String getMeasureProperties = "select symbols,Alignment,fontcolor,bgcolor,negative_val,no_format,round from prg_user_sub_folder_elements where element_id in(select element_id from prg_ar_query_detail where report_id=" + reportId + ")";
        Object[] obj = new Object[1];
        obj[0] = reportId;
        String getReportMeasures = buildQuery(getMeasures, obj);
        try {
            retObj = execSelectSQL(getReportMeasures);
            if (retObj.getRowCount() > 0) {
                for (int i = 0; i < retObj.getRowCount(); i++) {
                    measureId.add(retObj.getFieldValueString(i, 0));
                    measureName.add(retObj.getFieldValueString(i, 1));
                }
                retObj = execSelectSQL(getMeasureProperties);
                for (int i = 0; i < retObj.getRowCount(); i++) {
                    symbols.add(retObj.getFieldValueString(i, 0));
                    Alignment.add(retObj.getFieldValueString(i, 1));
                    fontcolor.add(retObj.getFieldValueString(i, 2));
                    bgcolor.add(retObj.getFieldValueString(i, 3));
                    negative_val.add(retObj.getFieldValueString(i, 4));
                    no_format.add(retObj.getFieldValueString(i, 5));
                    round.add(retObj.getFieldValueString(i, 6));
                }
                map.put("measureId", measureId);
                map.put("measureName", measureName);
                map.put("symbols", symbols);
                map.put("Alignment", Alignment);
                map.put("fontcolor", fontcolor);
                map.put("bgcolor", bgcolor);
                map.put("negative_val", negative_val);
                map.put("no_format", no_format);
                map.put("round", round);

            }
        } catch (SQLException ex) {

            logger.error("Exception:", ex);
        }
        map.put("fcolorsmap", fcolorsmap);
        return map;
    }

    public void saveModifyMeasures(String reportId, String[] Symbols, String[] Alignment, String[] Font, String[] BG, String[] Negativevalue, String[] NbrFormat, String[] Round) {
        PbReturnObject robj = new PbReturnObject();
        try {
            robj = execSelectSQL("select element_Id from prg_ar_query_detail where report_id=" + reportId);
            for (int i = 0; i < robj.rowCount; i++) {
                String sym[] = Symbols[i].split("_");
                String ali[] = Alignment[i].split("_");
                String font[] = Font[i].split("_");
                String bg[] = BG[i].split("_");
                String negative[] = Negativevalue[i].split("_");
                String nbrFormat[] = NbrFormat[i].split("_");
                String round[] = Round[i].split("_");
                String qry = "update prg_user_sub_folder_elements set symbols='" + sym[0] + "', Alignment='" + ali[0] + "', fontcolor='" + font[0] + "', bgcolor='" + bg[0] + "', negative_val='" + negative[0] + "', no_format='" + nbrFormat[0] + "', round='" + round[0] + "' where element_id='" + robj.getFieldValueInt(i, 0) + "'";
                execUpdateSQL(qry);
            }


        } catch (SQLException ex) {

            logger.error("Exception:", ex);
        } catch (Exception e) {
            logger.error("Exception:", e);
        }

    }

    public Map<String, String> getcolors() {

        Map<String, String> backgroundcolor = new HashMap<String, String>();
        backgroundcolor.put("", "");
        backgroundcolor.put("Red", "#f24040");
        backgroundcolor.put("Green", "#9ACD32");
        backgroundcolor.put("Orange", "#FF9933");
        backgroundcolor.put("Blue", "#0000FF");
        backgroundcolor.put("Aqua", "#00FFFF");
        backgroundcolor.put("Charteuse", "#7FFF00");
        backgroundcolor.put("Black", "#000000");
        backgroundcolor.put("White", "#FFFFFF");
        backgroundcolor.put("Yellow", "#FFFF00");
        backgroundcolor.put("Violet", "#EE82EE");
        backgroundcolor.put("Purple", "#800080");
        backgroundcolor.put("LightBlue", "#ADD8E6");
        backgroundcolor.put("Silver", "#C0C0C0");
        backgroundcolor.put("Olive", "#808000");
        backgroundcolor.put("Light Slate Gray", "#6D7B8D");
        backgroundcolor.put("Slate Blue", "#737CA1");
        backgroundcolor.put("Lavender", "#E3E4FA");
        backgroundcolor.put("Pink3", "#C48793");
        backgroundcolor.put("Lemon Chiffon3", "#C9C299");
        backgroundcolor.put("Khaki", "#ADA96E");
        backgroundcolor.put("Bisque", "#FFE4C4");
        backgroundcolor.put("DarkGray", "#A9A9A9");
        backgroundcolor.put("PaleGreen", "#98FB98");
        backgroundcolor.put("indian red", "#B0171F");
        backgroundcolor.put("crimson", "#DC143C");
        backgroundcolor.put("Dark Goldenrod", "#b8860b");
        backgroundcolor.put("Wheat", "#f5deb3");
        backgroundcolor.put("Dark Orchid", "#9932cc");
        backgroundcolor.put("Thistle", "#d8bfd8");
        backgroundcolor.put("Aquamarine", "#66cdaa");
        backgroundcolor.put("Magenta", "#FF00FF");
        backgroundcolor.put("lightpink", "#FFB6C1");
        backgroundcolor.put("palevioletred", "#DB7093");
        backgroundcolor.put("violetred", "#FF3E96");
        backgroundcolor.put("plum", "FFBBFF");
        backgroundcolor.put("seashell", "#EEE5DE");
        backgroundcolor.put("Rosy Brown", "#bc8f8f");
        backgroundcolor.put("Salmon", "#e9967a");
        backgroundcolor.put("Coral", "#ff7f50");
        backgroundcolor.put("Lavender Blush", "#fff0f5");
        backgroundcolor.put("Cadet Blue", "#5f9ea0");
        backgroundcolor.put("default1", "#64B2FF");
        backgroundcolor.put("default2", "#B5F950");
        backgroundcolor.put("default3", "#33E6FA");
        backgroundcolor.put("default4", "#EFE662");
        backgroundcolor.put("default5", "#E1755F");
        backgroundcolor.put("default6", "#AAC749");
        backgroundcolor.put("default7", "#867D56");
        backgroundcolor.put("default8", "#C9A0DC");
        backgroundcolor.put("default9", "#0099CC");
        backgroundcolor.put("default10", "#8E5454");
        backgroundcolor.put("default11", "#FF9900");


        return backgroundcolor;
    }

    public void modifyedMeasuresToReport(String ReportId, Container container) {
        PbReturnObject robj = new PbReturnObject();
        try {
            robj = execSelectSQL("select symbols,Alignment,fontcolor,bgcolor,negative_val,no_format,round,element_id from prg_user_sub_folder_elements where element_id in(select element_id from prg_ar_query_detail where report_id=" + ReportId + ")");
            if (robj.rowCount > 0) {
                for (int i = 0; i < robj.rowCount; i++) {
                    container.setSymbol("A_" + robj.getFieldValueString(i, 7), robj.getFieldValueString(i, 0));
                    container.setAlignment("A_" + robj.getFieldValueString(i, 7), robj.getFieldValueString(i, 1));
                    container.setFontcolor("A_" + robj.getFieldValueString(i, 7), robj.getFieldValueString(i, 2));
                    container.setBGcolor("A_" + robj.getFieldValueString(i, 7), robj.getFieldValueString(i, 3));
                    container.setNegative_val("A_" + robj.getFieldValueString(i, 7), robj.getFieldValueString(i, 4));
                    container.setNo_format("A_" + robj.getFieldValueString(i, 7), robj.getFieldValueString(i, 5));
                    container.setRound("A_" + robj.getFieldValueString(i, 7), robj.getFieldValueString(i, 6));
                }
            }
//        else{
//           container.setSymbol("A_"+robj.getFieldValueString(0, 7),"");
//           container.setAlignment("A_"+robj.getFieldValueString(0, 7),"");
//           container.setFontcolor("A_"+robj.getFieldValueString(0, 7),"");
//           container.setBGcolor("A_"+robj.getFieldValueString(0, 7),"");
//           container.setNegative_val("A_"+robj.getFieldValueString(0, 7),"");
//           container.setNo_format("A_"+robj.getFieldValueString(0, 7),"");
//           container.setRound("A_"+robj.getFieldValueString(0, 7),"");
//        }
        } 
        catch (SQLException ex) {
            logger.error("Exception:", ex);
        }catch (Exception e) {
            logger.error("Exception:", e);
        }    }
    //add by sumeet end

    public ArrayList getGraphDetails(String reportId) {
        ArrayList graphDetails = new ArrayList();
        String sql = getResourceBundle().getString("homeGraphs");
        JqplotGraphProperty jqProperty = null;
        String graphId = "";
        Object[] objArr = new Object[1];
        objArr[0] = reportId;
        try {
            PbReturnObject pbReturnObject = super.execSelectSQL(super.buildQuery(sql, objArr));
            if (pbReturnObject.getRowCount() > 0 && pbReturnObject.getFieldUnknown(0, 0) != null) {
                Gson gson = new GsonBuilder().serializeNulls().create();
                jqProperty = gson.fromJson(pbReturnObject.getFieldUnknown(0, 1), JqplotGraphProperty.class);
                graphId = pbReturnObject.getFieldValueString(0, 0);
                graphDetails.add(jqProperty.getGraphTypename());
                graphDetails.add(jqProperty.getGraphTypeId());
                graphDetails.add(graphId);
            }
        } 
        catch (SQLException ex) {
            logger.error("Exception:", ex);
        }catch (Exception ex) {
            logger.error("Exception:", ex);
        }        return graphDetails;
        }

    public void insertHomePageGraphs(String reportId, String filename, String graphId, String repName, String graphType) {
        String sql = getResourceBundle().getString("insertHomeGraphDetails");
        Object[] objArr = new Object[5];
        objArr[0] = reportId;
        objArr[1] = graphId;
        objArr[2] = filename;
        objArr[3] = repName;
        objArr[4] = graphType;
        try {
            super.execUpdateSQL(super.buildQuery(sql, objArr));
        } catch (Exception ex) {

            logger.error("Exception:", ex);
        }
    }

    public void updateGraphType(String reportId, String userId, String filename) {
        String sql = getResourceBundle().getString("insertGraphHotData");

        Object[] objArr = new Object[3];
        objArr[0] = reportId;
        objArr[1] = userId;
        objArr[2] = filename;
        try {
            super.execUpdateSQL(super.buildQuery(sql, objArr));
        } catch (Exception ex) {

            logger.error("Exception:", ex);
        }
    }

    public String getGraphHotFile(String reportId) {
        String sql = "select GRAPH_NAME from PRG_HOT_GRAPH_TABLE where REPORT_ID='" + reportId + "'";
        String graphHotName = "";
        PbReturnObject pbReturnObject = null;
        Object[] objArr = new Object[1];
        objArr[0] = reportId;
        try {
            pbReturnObject = super.execSelectSQL(sql);
            graphHotName = pbReturnObject.getFieldValueString(0, "GRAPH_NAME");
        } catch (SQLException ex) {

            logger.error("Exception:", ex);
        }
        return graphHotName;
    }

    //added by Mohit for Custom setting
    public String getCustomSetting() {
        String sql = "select SETUP_CHAR_VALUE from PRG_GBL_SETUP_VALUES where SETUP_KEY='GLOBAL_PARAMS'";
        String settingvalue = "";
        PbReturnObject pbReturnObject = null;
        //Object[] objArr = new Object[1];
        // objArr[0] = reportId;
        try {
            pbReturnObject = super.execSelectSQL(sql);
            if (pbReturnObject != null && pbReturnObject.rowCount > 0) {
                settingvalue = pbReturnObject.getFieldValueString(0, "SETUP_CHAR_VALUE");
            }
        } catch (SQLException ex) {

            logger.error("Exception:", ex);
        }
        return settingvalue;
    }

    public PbReturnObject getAllHomeGraphs(String reportId) {
        String sql = "Select * from PRG_AR_HOMEPAGE_GRAPH where report_id='" + reportId + "'";
        PbReturnObject pbReturnObject = null;
        try {
            pbReturnObject = super.execSelectSQL(sql);
        } catch (SQLException ex) {

            logger.error("Exception:", ex);
        }
        return pbReturnObject;
    }

    public void writeInsertQueries(String filepath, String ReportId) throws Exception {
        String qry = "SELECT FILE_PATH FROM  PRG_REPORT_COLLECTION_BACKUP where REPORT_ID=" + ReportId;
        PbDb db = new PbDb();
        String InsertQuery = "";
        PbReturnObject retObj = db.execSelectSQL(qry);
        if (retObj != null && retObj.getRowCount() > 0) {
            InsertQuery = "update PRG_REPORT_COLLECTION_BACKUP set FILE_PATH='" + filepath + "' where REPORT_ID=" + ReportId;
        } else {
            InsertQuery = "INSERT INTO PRG_REPORT_COLLECTION_BACKUP( REPORT_ID , FILE_PATH) VALUES(" + ReportId + ",'" + filepath + "')";
        }
        db.execInsert(InsertQuery);
    }

    public double getReportVersion(String pbReportId) {
        double version = 1.0;
        String query = "SELECT REPORT_VERSION FROM PRG_AR_REPORT_MASTER WHERE REPORT_ID=" + pbReportId;
        try {
            PbReturnObject retObj = execSelectSQL(query);
            if (retObj != null && retObj.rowCount > 0 && retObj.getFieldValueString(0, 0) != null
                    && !retObj.getFieldValueString(0, 0).isEmpty() && !retObj.getFieldValueString(0, 0).equalsIgnoreCase("null")) {
                version = Double.parseDouble(retObj.getFieldValueString(0, 0));
            }
        } 
        catch (SQLException ex) {
            logger.error("Exception:", ex);
        }catch (Exception e) {
            logger.error("Exception:", e);
        }        return version;
        }

    public String getGtAverage(String pbReportId) {
        String average = "false";
        String query = "SELECT GT_AVERAGE FROM PRG_AR_REPORT_TABLE_MASTER WHERE REPORT_ID=" + pbReportId;
        try {
            PbReturnObject retObj = execSelectSQL(query);
            if (retObj != null && retObj.rowCount > 0 && retObj.getFieldValueString(0, 0) != null
                    && !retObj.getFieldValueString(0, 0).isEmpty() && !retObj.getFieldValueString(0, 0).equalsIgnoreCase("null")) {
                average = retObj.getFieldValueString(0, 0);
            }
        } 
        catch (SQLException ex) {
            logger.error("Exception:", ex);
        }catch (Exception e) {
            logger.error("Exception:", e);
        }        return average;
        }

    public HashMap saveModifyMeasuresAttr(String reportId, String measureId, String Rename, String No_frmt, String Aggregation, String Rounding, String preffix, String suffix, String Msrtype, String dateFrmt, String datatype, String changeolderrep, Container container) throws Exception {
        boolean flag = false;
        if (Aggregation.equalsIgnoreCase("none")) {
            Aggregation = "null";
        }
        ArrayList<String> queries = new ArrayList<String>();
        PbReturnObject msrnamertrnobj = new PbReturnObject();
//         String getoldname="SELECT distinct COL_NAME from PRG_AR_REPORT_TABLE_DETAILS wHERE QRY_COL_ID IN (SELECT QRY_COL_ID FROM PRG_AR_QUERY_DETAIL WHERE ELEMENT_ID in (select ELEMENT_ID from PRG_USER_ALL_INFO_DETAILS where ELEMENT_ID="+measureId+"))";
        String getoldname = "SELECT USER_COL_DESC FROM PRG_USER_ALL_INFO_DETAILS WHERE ELEMENT_ID=" + measureId;
        msrnamertrnobj = execSelectSQL(getoldname);
        String oldname = msrnamertrnobj.getFieldValueString(0, 0);

        PbReturnObject msrretrnobj = new PbReturnObject();
        String getRefId = "select ref_element_id from prg_user_all_info_details where element_id=" + measureId;
        msrretrnobj = execSelectSQL(getRefId);
        String Ref_Element_id = msrretrnobj.getFieldValueString(0, 0);

        if (changeolderrep.equalsIgnoreCase("Y")) {

            //need to change query for priorand change and change%
            String updatemeasurename = getResourceBundle().getString("updatemeasurenamefrall");
            String finalQuery3 = null;
            Object[] obj3 = new Object[3];
            obj3[0] = oldname;
            obj3[1] = Rename;
            obj3[2] = measureId;
            finalQuery3 = buildQuery(updatemeasurename, obj3);
            queries.add(finalQuery3);


            //need to change query for priorand change and change%
            String updatedmeasurename = getResourceBundle().getString("updatedmeasurenameuserallInfoDetails");
            String finalQuery4 = null;
            Object[] obj5 = new Object[3];
            obj5[0] = oldname;
            obj5[1] = Rename;
            obj5[2] = measureId;
            finalQuery4 = buildQuery(updatedmeasurename, obj5);
            queries.add(finalQuery4);

            //need to change query based on ref_element_id and ref_element_type in (1,2)
            String updatemodifymeasureattribute = getResourceBundle().getString("updatemodifymeasureattribute");
            String finalQuery = null;
            Object[] obj = new Object[5];
            obj[0] = preffix;
            obj[1] = No_frmt;
            obj[2] = Rounding;
            obj[3] = suffix;
            obj[4] = Ref_Element_id;
            finalQuery = buildQuery(updatemodifymeasureattribute, obj);
            queries.add(finalQuery);

            String updatedmeasurenameinbusstabledetails = getResourceBundle().getString("updatedmeasurenameinbusstabledetails");
            String finalQuery7 = null;
            Object[] obj8 = new Object[3];
            obj8[0] = oldname;
            obj8[1] = Rename;
            obj8[2] = measureId;
            finalQuery7 = buildQuery(updatedmeasurenameinbusstabledetails, obj8);
            queries.add(finalQuery7);


            //need to change in PRG_SUB_FOLDER_ELEMENTS based on ref_element_id
            String updatedmeasurenameinsubfldr = getResourceBundle().getString("updatedmeasurenameinsubfldr");
            String finalQuery5 = null;
            Object[] obj6 = new Object[4];
            obj6[0] = oldname;
            obj6[1] = Rename;
            obj6[2] = datatype;
            obj6[3] = measureId;
            finalQuery5 = buildQuery(updatedmeasurenameinsubfldr, obj6);
            queries.add(finalQuery5);

            //need to split this query. first qry for updatine name and dependent measure names for all type
            //second query updating properties of that measure and also for its prior

            String updatemeasnameinTabsforAll = getResourceBundle().getString("updatemeasnameinTabsforAll");
            String finalQuery6 = null;
            Object[] obj7 = new Object[3];
            obj7[0] = oldname;
            obj7[1] = Rename;
            obj7[2] = measureId;
            finalQuery6 = buildQuery(updatemeasnameinTabsforAll, obj7);
            queries.add(finalQuery6);

            String updatemeasPropsinTabsforAll = getResourceBundle().getString("updatemeasPropsinTabsforAll");
            String finalQuery10 = null;
            Object[] obj10 = new Object[5];
            obj10[0] = Msrtype;
            obj10[1] = Rounding;
            obj10[2] = No_frmt;
            obj10[3] = preffix;
            obj10[4] = measureId;
            finalQuery10 = buildQuery(updatemeasPropsinTabsforAll, obj10);
            queries.add(finalQuery10);

//          String updatedmeasurenameinarreporttabledetails = getResourceBundle().getString("updatedmeasurenameinarreporttabledetails");
//          String finalQuery6=null;
//          Object[] obj7=new Object[7];
//          obj7[0]=oldname;
//          obj7[1]=Rename;
//          obj7[2]=Msrtype;
//          obj7[3]=Rounding;
//          obj7[4]=No_frmt;
//          obj7[5]=preffix;
//          obj7[6]=measureId;
//          finalQuery6 = buildQuery(updatedmeasurenameinarreporttabledetails, obj7);
//          queries.add(finalQuery6);

            String updateAggrmodifymsrattr = getResourceBundle().getString("updateAggrmodifymsrattr");
            String finalQuery2 = null;
            Object[] obj2 = new Object[3];
            obj2[0] = Aggregation;
            obj2[1] = Msrtype;
            obj2[2] = Ref_Element_id;
            finalQuery2 = buildQuery(updateAggrmodifymsrattr, obj2);
            queries.add(finalQuery2);

            //added by Nazneen
            String updateDateFrmtInReportDetAll = getResourceBundle().getString("updateDateFrmtInReportDetAll");
            String finalQuery11 = null;
            Object[] obj11 = new Object[2];
            obj11[0] = dateFrmt;
            obj11[1] = Ref_Element_id;
            finalQuery11 = buildQuery(updateDateFrmtInReportDetAll, obj11);
            queries.add(finalQuery11);

            String updateDateFrmtInAll = getResourceBundle().getString("updateDateFrmtInUserAllInfoForAll");
            String finalQuery12 = null;
            Object[] obj12 = new Object[3];
            obj12[0] = dateFrmt;
            obj12[1] = datatype;
            obj12[2] = Ref_Element_id;
            finalQuery12 = buildQuery(updateDateFrmtInAll, obj12);
            queries.add(finalQuery12);

            String updateDataTypeInPrgQryDetailsAll = getResourceBundle().getString("updateDataTypeInPrgQryDetailsAll");
            String finalQuery13 = null;
            Object[] obj13 = new Object[2];
            obj13[0] = datatype;
            obj13[1] = Ref_Element_id;
            finalQuery13 = buildQuery(updateDataTypeInPrgQryDetailsAll, obj13);
            queries.add(finalQuery13);
            //end of code by Nazneen
        } else {
//
//             String updatemeasurename = getResourceBundle().getString("updatemeasurenamefrall");
//          String finalQuery3=null;
//          Object[] obj3=new Object[3];
//          obj3[0]=oldname;
//          obj3[1]=Rename;
//          obj3[2]=measureId;
//          finalQuery3 = buildQuery( updatemeasurename, obj3);
//          queries.add(finalQuery3);


            String updatedmeasurename = getResourceBundle().getString("updatedmeasurenameuserallInfoDetails");
            String finalQuery4 = null;
            Object[] obj5 = new Object[3];
            obj5[0] = oldname;
            obj5[1] = Rename;
            obj5[2] = measureId;
            finalQuery4 = buildQuery(updatedmeasurename, obj5);
            queries.add(finalQuery4);

            String updatemodifymeasureattribute = getResourceBundle().getString("updatemodifymeasureattribute");
            String finalQuery = null;
            Object[] obj = new Object[5];
            obj[0] = preffix;
            obj[1] = No_frmt;
            obj[2] = Rounding;
            obj[3] = suffix;
            obj[4] = Ref_Element_id;
            finalQuery = buildQuery(updatemodifymeasureattribute, obj);
            queries.add(finalQuery);

            String updatedmeasurenameinbusstabledetails = getResourceBundle().getString("updatedmeasurenameinbusstabledetails");
            String finalQuery7 = null;
            Object[] obj8 = new Object[3];
            obj8[0] = oldname;
            obj8[1] = Rename;
            obj8[2] = measureId;
            finalQuery7 = buildQuery(updatedmeasurenameinbusstabledetails, obj8);
            queries.add(finalQuery7);

            String updatedmeasurenameinsubfldr = getResourceBundle().getString("updatedmeasurenameinsubfldr");
            String finalQuery5 = null;
            Object[] obj6 = new Object[4];
            obj6[0] = oldname;
            obj6[1] = Rename;
            obj6[2] = datatype;
            obj6[3] = measureId;
            finalQuery5 = buildQuery(updatedmeasurenameinsubfldr, obj6);
            queries.add(finalQuery5);


            String updatemeasurenameNdAggr = getResourceBundle().getString("updatemeasurenameNdAggr");
            String finalQuery9 = null;
            Object[] obj9 = new Object[5];
            obj9[0] = oldname;
            obj9[1] = Rename;
            obj9[2] = Aggregation;
            obj9[3] = measureId;
            obj9[4] = reportId;
            finalQuery9 = buildQuery(updatemeasurenameNdAggr, obj9);
            queries.add(finalQuery9);


            //need to split the query.first query name based on ref_element_id.
            //second query based on ref_element_type in (1,2) only for applying properties
            String updatemeasnameInAspecificRepforTab = getResourceBundle().getString("updatemeasnameInAspecificRepforTab");
            String finalQuery11 = null;
            Object[] obj11 = new Object[4];
            obj11[0] = oldname;
            obj11[1] = Rename;
            obj11[2] = measureId;
            obj11[3] = reportId;
            finalQuery11 = buildQuery(updatemeasnameInAspecificRepforTab, obj11);
            queries.add(finalQuery11);

            String updatemeasPropsInAspecificRepforTab = getResourceBundle().getString("updatemeasPropsInAspecificRepforTab");
            String finalQuery12 = null;
            Object[] obj12 = new Object[6];
            obj12[0] = Msrtype;
            obj12[1] = Rounding;
            obj12[2] = No_frmt;
            obj12[3] = preffix;
            obj12[4] = measureId;
            obj12[5] = reportId;
            finalQuery12 = buildQuery(updatemeasPropsInAspecificRepforTab, obj12);
            queries.add(finalQuery12);
//           String updatemeasurenameinrepTab = getResourceBundle().getString("updatemeasurenameinrepTab");
//           String finalQuery1=null;
//           Object[] obj4=new Object[8];
//           obj4[0]=oldname;
//           obj4[1]=Rename;
//           obj4[2]=Msrtype;
//           obj4[3]=Rounding;
//           obj4[4]=No_frmt;
//           obj4[5]=preffix;
//           obj4[6]=measureId;
//           obj4[7]=reportId;
//           finalQuery1 = buildQuery( updatemeasurenameinrepTab, obj4);
//           queries.add(finalQuery1);

            String updateAggrmodifymsrattr = getResourceBundle().getString("updateAggrmodifymsrattr");
            String finalQuery2 = null;
            Object[] obj2 = new Object[3];
            obj2[0] = Aggregation;
            obj2[1] = Msrtype;
            obj2[2] = Ref_Element_id;
            finalQuery2 = buildQuery(updateAggrmodifymsrattr, obj2);
            queries.add(finalQuery2);

            //added by Nazneen
            String updateDateFrmtInReportDetSpec = getResourceBundle().getString("updateDateFrmtInReportDetSpec");
            String finalQuery13 = null;
            Object[] obj13 = new Object[3];
            obj13[0] = dateFrmt;
            obj13[1] = Ref_Element_id;
            obj13[2] = reportId;
            finalQuery13 = buildQuery(updateDateFrmtInReportDetSpec, obj13);
            queries.add(finalQuery13);

            String updateDateFrmtInUserAllInfoForAll = getResourceBundle().getString("updateDateFrmtInUserAllInfoForAll");
            String finalQuery14 = null;
            Object[] obj14 = new Object[3];
            obj14[0] = dateFrmt;
            obj14[1] = datatype;
            obj14[2] = Ref_Element_id;
            finalQuery14 = buildQuery(updateDateFrmtInUserAllInfoForAll, obj14);
            queries.add(finalQuery14);

            String updateDataTypeInPrgQryDetailsSpec = getResourceBundle().getString("updateDataTypeInPrgQryDetailsSpec");
            String finalQuery15 = null;
            Object[] obj15 = new Object[3];
            obj15[0] = datatype;
            obj15[1] = Ref_Element_id;
            obj15[2] = reportId;
            finalQuery15 = buildQuery(updateDataTypeInPrgQryDetailsSpec, obj15);
            queries.add(finalQuery15);
            //end of code by Nazneen
            flag = true;
            container.setupdate(flag);
        }

        //executing all the update queries

        for (int t = 0; t < queries.size(); t++) {
            logger.info("print query" + t + queries.get(t));
            execUpdateSQL(queries.get(t));
        }

        //setting all the values for getting on runtime
        HashMap map = new HashMap();
        HashMap mapcon = new HashMap();
        HashMap<String, HashMap> map2 = new HashMap();
        HashMap<String, String> map3 = new HashMap();
        String symbols = preffix;
        String no_format = No_frmt;
        String round = Rounding;
        map3.put("elementname", Rename);
        map3.put("no_format", no_format);
        map3.put("symbols", symbols);
        map3.put("round", round);
        map3.put("suffix_symbol", suffix);
        map3.put("aggregation", Aggregation);
        map3.put("measureType", Msrtype);
        if (dateFrmt != null && !dateFrmt.equalsIgnoreCase("")) {
            map3.put("dateFormat", dateFrmt);
        }
        map2.put(measureId, map3);

        container.setmodifymeasureAttrChnge(map2);
        HashMap conmap = new HashMap();
        conmap.put(reportId, container);
        HashMap update = new HashMap();
        update.put(measureId, Aggregation);
        container.setupdateAggregation(update);
        return conmap;


    }//end of save measureattributes.

    public HashMap modifyMeasureAttr(String reportId, Container container) {
        HashMap map = new HashMap();
        ArrayList<String> measureName = new ArrayList<String>();
        ArrayList<String> measureId = new ArrayList<String>();
        ArrayList<String> symbols = new ArrayList<String>();
        ArrayList<String> measureType = new ArrayList<String>();
        ArrayList<String> col_type = new ArrayList<String>();
        ArrayList<String> aggregation = new ArrayList<String>();
        ArrayList<String> no_format = new ArrayList<String>();
        ArrayList<String> round = new ArrayList<String>();
        PbReportCollection collect = container.getReportCollect();
        ArrayList<String> queryCols = new ArrayList<String>();
        ArrayList<String> queryColNames = new ArrayList<String>();
        ArrayList<String> queryCol2 = new ArrayList<String>();
        queryCol2 = collect.reportQryElementIds;
        queryCols = container.getDisplayColumns();
        queryColNames = collect.reportQryColNames;
        int val = container.getViewByCount();
        if (container.isReportCrosstab()) {
            queryCols = collect.reportQryElementIds;
            val = 0;
        }
//        Map<String, String> fcolorsmap = new HashMap<String, String>();
        PbReturnObject retObj = new PbReturnObject();
        for (int k = val; k < queryCols.size(); k++) {
            if (!RTMeasureElement.isRunTimeMeasure(queryCols.get(k))) {
                measureId.add(queryCols.get(k).replace("A_", ""));
                for (int l = 0; l < queryCol2.size(); l++) {
                    int val2 = 0;
                    if (!container.isReportCrosstab()) {
                        val2 = k - container.getViewByCount();
                    } else {
                        val2 = k;
                    }
                    if (measureId.get(val2).equals(queryCol2.get(l))) {


                        measureName.add(queryColNames.get(l));
                    }
                }
//        String getMeasures=getResourceBundle().getString("getReportMeasuresName");
                String getMeasureProperties = "select symbols,no_format,round,user_col_type from prg_user_sub_folder_elements where element_id=" + queryCols.get(k).replace("A_", "");
                String getaggregationtype = "select aggregation_type,measure_type from prg_user_all_info_details where element_id=" + queryCols.get(k).replace("A_", "");
                Object[] obj = new Object[1];
                obj[0] = reportId;
//        String getReportMeasures=buildQuery(getMeasures, obj);

//            retObj = execSelectSQL(getReportMeasures);
//            if(retObj.getRowCount()>0){
//                for(int i=0;i<retObj.getRowCount();i++){
//                  measureId.add(retObj.getFieldValueString(i, 0));
//                  measureName.add(retObj.getFieldValueString(i, 1));
//                }

//            retObj = execSelectSQL(getReportMeasures);
//            if(retObj.getRowCount()>0){
//                for(int i=0;i<retObj.getRowCount();i++){
//                  measureId.add(retObj.getFieldValueString(i, 0));
//                  measureName.add(retObj.getFieldValueString(i, 1));
//                }


                try {
                    retObj = execSelectSQL(getMeasureProperties);

                    for (int i = 0; i < retObj.getRowCount(); i++) {
//                  symbols.add(retObj.getFieldValueString(i, 0));
//                  Alignment.add(retObj.getFieldValueString(i, 1));
//                  fontcolor.add(retObj.getFieldValueString(i, 2));
//                  bgcolor.add(retObj.getFieldValueString(i, 3));
//                  negative_val.add(retObj.getFieldValueString(i, 4));
                        symbols.add(retObj.getFieldValueString(i, 0));
                        no_format.add(retObj.getFieldValueString(i, 1));
                        round.add(retObj.getFieldValueString(i, 2));
                        col_type.add(retObj.getFieldValueString(i, 3));
                    }

                    retObj = execSelectSQL(getaggregationtype);

                    for (int i = 0; i < retObj.getRowCount(); i++) {
                        aggregation.add(retObj.getFieldValueString(i, 0));
                        measureType.add(retObj.getFieldValueString(i, 1));
                    }
                    map.put("measureId", measureId);
                    map.put("measureName", measureName);
                    map.put("symbols", symbols);
                    map.put("col_type", col_type);
                    map.put("Aggregation", aggregation);
                    map.put("measureType", measureType);
                    map.put("no_format", no_format);
                    map.put("round", round);

                } catch (SQLException ex) {

                    logger.error("Exception:", ex);
                }
            }
        }
//         container.setmodifymeasure(map);
        return map;
    }

    public HashMap modifyMeasureAttrreport(String reportId, Container container) {


        HashMap<String, HashMap> map = new HashMap();
        PbReportCollection collect = container.getReportCollect();
        ArrayList<String> queryCols = new ArrayList<String>();
        ArrayList<String> queryColNames = new ArrayList<String>();
        ArrayList<String> queryColsNew = new ArrayList<String>();
        ArrayList<String> queryColNamesNew = new ArrayList<String>();
//        HashMap summarizedmMesMap = container.getSummerizedTableHashMap();
//        Object get = summarizedmMesMap.get("summerizedQryeIds");
//        container.getTableHashMap().get("MeasuresNames");
//        queryCols = collect.reportQryElementIds;
//        queryColNames = collect.reportQryColNames;
        ArrayList<String> DisplayColumns = container.getDisplayColumns();
        boolean kpidasboard = container.isIskpidasboard();
        HashMap reportHashMap = container.getReportHashMap();
        HashMap tableHashMap = container.getTableHashMap();
        ArrayList<String> Measures = (ArrayList) tableHashMap.get("Measures");
        ArrayList<String> MeasuresNames = (ArrayList) tableHashMap.get("MeasuresNames");
        HashMap DisplayNamesMap = (HashMap) reportHashMap.get("DisplayNamesMap");
        if (kpidasboard && container.IsTimedasboard() && !(reportHashMap.isEmpty()) && DisplayNamesMap != null && !(DisplayNamesMap.isEmpty())) {
//              HashMap DisplayNamesMap =(HashMap) reportHashMap.get("DisplayNamesMap");
//        String[] toArray = (String[])DisplayNamesMap.keySet().toArray(new String[DisplayNamesMap.size()]);
//           for (int i = 0; i < toArray.length; i++) {
//               queryCols.add(toArray[i].replace("A_", ""));
//               queryColNames.add((String) DisplayNamesMap.get(toArray[i]));
//           }

            for (int i = 0; i < Measures.size(); i++) {
                queryCols.add(Measures.get(i).replace("A_", ""));
                queryColNames.add(MeasuresNames.get(i));
            }
        } else {
            queryCols = collect.reportQryElementIds;
            queryColNames = collect.reportQryColNames;
        }

//        PbReturnObject retObj=new PbReturnObject();
        PbReturnObject retObj2 = new PbReturnObject();
        PbReturnObject retObj3 = new PbReturnObject();
//        String getMeasures=getResourceBundle().getString("getReportMeasuresName");
        String getMeasureProperties = "";
        String getaggregationtype = "";
        Object[] obj = new Object[1];
        obj[0] = reportId;
//        String getReportMeasures=buildQuery(getMeasures, obj);
        try {
//            retObj = execSelectSQL(getReportMeasures);
//            if(retObj.getRowCount()>0){
            for (int i = 0; i < queryCols.size(); i++) {
                String elementid = "";
                String elementname = "";
                elementid = "A_";
                elementid += queryCols.get(i);
                elementname = queryColNames.get(i);

                getMeasureProperties = "select symbols,no_format,round,user_col_type,suffix_symbol from prg_user_sub_folder_elements where element_id =" + elementid.replace("A_", "");
                getaggregationtype = "select aggregation_type,measure_type from prg_user_all_info_details where element_id =" + elementid.replace("A_", "");
                retObj2 = execSelectSQL(getMeasureProperties);

                HashMap<String, String> map2 = new HashMap();
                for (int j = 0; j < retObj2.getRowCount(); j++) {
                    String symbols = retObj2.getFieldValueString(0, 0);
                    String no_format = retObj2.getFieldValueString(0, 1);
                    String round = retObj2.getFieldValueString(0, 2);
                    String col_type = retObj2.getFieldValueString(0, 3);
                    String suffix_symbol = retObj2.getFieldValueString(0, 4);
                    map2.put("elementname", elementname);
                    map2.put("no_format", no_format);
                    map2.put("symbols", symbols);
                    map2.put("round", round);
                    map2.put("ncol_type", col_type);
                    map2.put("suffix_symbol", suffix_symbol);
                }
//                  measureName.add(retObj.getFieldValueString(i, 1));
                retObj3 = execSelectSQL(getaggregationtype);

                for (int k = 0; k < retObj3.getRowCount(); k++) {
                    String aggregation = retObj3.getFieldValueString(0, 0);
                    String measureType = retObj3.getFieldValueString(0, 1);
                    map2.put("aggregation", aggregation);
                    map2.put("measureType", measureType);
                }

//                }

                map.put(elementid, map2);
            }



        } catch (SQLException ex) {

            logger.error("Exception:", ex);
        }
//         container.setmodifymeasure(map);
        return map;
    }

    public HashMap modifyMeasureAttr(String reportId, String elmntid, Container container) {
        HashMap map = new HashMap();
        PbReturnObject retObj = new PbReturnObject();
        PbReturnObject retObj2 = new PbReturnObject();
        PbReturnObject retObj3 = new PbReturnObject();//added by bhargavi


        try {

            String getMeasureProperties = "select symbols,user_col_type from prg_user_sub_folder_elements where element_id=" + elmntid;
            String getaggregationtype = "select aggregation_type from prg_user_all_info_details where element_id=" + elmntid;
//             retObj=execSelectSQL(getMeasureProperti            es);
//             if (retObj != null) {
//             String Symbols=retObj.getFieldValueString(0,0);
//              String Nfrmt=retObj.getFieldValueString(0,1);
//             String Rounding=retObj.getFieldValueString(0,2);
//             String datatype=retObj.getFieldValueString(0,3);
            //start of code by bhargavi for rounding measure
            String getroundingcount = "select number_format,rounding_precision,measure_type from PRG_AR_REPORT_TABLE_DETAILS where report_id=" + reportId + " "
                    + "AND QRY_COL_ID IN ( SELECT QRY_COL_ID FROM PRG_AR_QUERY_DETAIL WHERE ELEMENT_ID in (select ELEMENT_ID from PRG_USER_ALL_INFO_DETAILS where REF_ELEMENT_ID=" + elmntid + "))";
            logger.info("getroundingcount----" + getroundingcount);
            retObj = execSelectSQL(getMeasureProperties);
            retObj3 = execSelectSQL(getroundingcount);

            if (retObj != null) {
                String Symbols = retObj.getFieldValueString(0, 0);
                String datatype = retObj.getFieldValueString(0, 1);
                if (retObj3 != null) {
                    String Nfrmt = retObj3.getFieldValueString(0, 0);
                    String Rounding = retObj3.getFieldValueString(0, 1);
                    String msrType = retObj3.getFieldValueString(0, 2);
                    //end of code by bhargavi for rounding measure
                    retObj2 = execSelectSQL(getaggregationtype);

                    if (retObj2 != null) {
                        String Aggr = retObj2.getFieldValueString(0, 0);
                        if (Aggr == null) {
                            Aggr = "none";
                        }
                        if (Aggr == "" || Aggr.equalsIgnoreCase("null")) {
                            Aggr = "none";
                        }

                        String connType = "";
                        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.ORACLE)) {
                            connType = "oracle";
                        }
                        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                            connType = "mysql";
                        } else {
                            connType = "sqlserver";
                        }


                        map.put("measureId", elmntid);
                        map.put("symbols", Symbols);
                        map.put("msrType", msrType);
                        map.put("datatype", datatype);
                        map.put("Aggregation", Aggr);
                        map.put("no_format", Nfrmt);
                        map.put("round", Rounding);
                        map.put("connType", connType);
//                container.setmodifymeasure(map);
                    }
                }
            }
        } catch (SQLException ex) {

            logger.error("Exception:", ex);
        }
        return map;
    }

    public String getRoleIds(String roleId) throws SQLException {
        Connection conSource = ProgenConnection.getInstance().getConnection();
        PreparedStatement pstmtForTransterSource;
        ResultSet resultSet = null;
        PbReturnObject returnObject = null;
//        String str = "";// by prabal
        String getusers = "select distinct grp_id from prg_user_all_info_details where folder_id =?";

        Gson json = new Gson();
        List<String> grpId = new ArrayList<String>();
        Map dimmap = new HashMap();
        pstmtForTransterSource = conSource.prepareStatement(getusers);
         pstmtForTransterSource.setString(1 , roleId); 
        resultSet = pstmtForTransterSource.executeQuery();
        returnObject = new PbReturnObject(resultSet);
        if (returnObject != null) {
            String grpIds = returnObject.getFieldValueString(0, 0);
            grpId.add(grpIds);
        }
        dimmap.put("grpId", grpId);
        if (conSource != null) {//ByPrabal
            conSource.close();
        }
        if (pstmtForTransterSource != null) {//ByPrabal
            pstmtForTransterSource.close();
        }
        return json.toJson(dimmap);

    }

    public void renameelementname(String elementId, String element_name, int ReportId) {
        try {
            String updatemeasurename = getResourceBundle().getString("updatemeasurename");
            String finalQuery3 = null;
            Object[] obj3 = new Object[3];
            obj3[0] = element_name;
            obj3[1] = elementId.replace("A_", "");
            obj3[2] = ReportId;

            finalQuery3 = buildQuery(updatemeasurename, obj3);
            execUpdateSQL(finalQuery3);


            String updatedmeasurename = getResourceBundle().getString("updatedmeasurename");
            String finalQuery4 = null;
            Object[] obj5 = new Object[5];
            obj5[0] = element_name;
            obj5[1] = elementId.replace("A_", "");
            finalQuery4 = buildQuery(updatedmeasurename, obj5);
            execUpdateSQL(finalQuery4);

        } catch (Exception ex) {

            logger.error("Exception:", ex);
        }


    }

    public String savingInsightDetails(String insightId, String name, String filepath, String roleId, String userId) throws Exception {
        String qry = "SELECT FILEPATH FROM  PRG_AR_INSIGHT_WORKBENCH where INSIGHT_ID=" + insightId;
        PbDb db = new PbDb();
        PbReturnObject retObj = db.execSelectSQL(qry);
        String insightqry;
        //ArrayList al=new ArrayList();
        if (retObj != null && retObj.getRowCount() > 0) { //update insight details
            insightqry = getResourceBundle().getString("updateInsightWorkBenchDetails");
            Object[] obj = new Object[3];
            obj[0] = filepath;
            obj[1] = userId;
            obj[2] = insightId;
            String finalqry = buildQuery(insightqry, obj);
            execUpdateSQL(finalqry);
        } else { //insert insight info in to db
            insightqry = getResourceBundle().getString("insertInsightWorkBenchDetails");
            Object[] obj;
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER) || ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                obj = new Object[4];
                obj[0] = name;
                obj[1] = filepath;
                obj[2] = roleId;
                obj[3] = userId;
            } else {
                obj = new Object[5];
                obj[0] = insightId;
                obj[1] = name;
                obj[2] = filepath;
                obj[3] = roleId;
                obj[4] = userId;
            }
            String finalqry = buildQuery(insightqry, obj);
            execInsert(finalqry);
            // al.add(finalqry);
            insightqry = getResourceBundle().getString("insertInsightAssignmentDetails");
            Object[] obj1 = new Object[2];
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                String ReportIdQry = "select ident_current('PRG_AR_INSIGHT_WORKBENCH')";
                PbDb pbdb = new PbDb();
                PbReturnObject retObj1 = pbdb.execSelectSQL(ReportIdQry);
                insightId = retObj1.getFieldValueString(0, 0);
            } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                String qry1 = "SELECT LAST_INSERT_ID(INSIGHT_ID) from PRG_AR_INSIGHT_WORKBENCH order by 1 desc limit 1";
                PbReturnObject retobj = new PbReturnObject();
                retobj = super.execSelectSQL(qry1);
                insightId = retobj.getFieldValueString(0, 0);
            }
            obj1[0] = insightId;
            obj1[1] = userId;
            finalqry = buildQuery(insightqry, obj1);
            execInsert(finalqry);
            // al.add(finalqry);
            //execMultiple(al);
            //
        }
        return insightId;
    }

    public String getCustomInsightId() throws Exception {
        String CustomReportId = "";
        int reportId;
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
            reportId = getSequenceNumber("select IDENT_CURRENT('PRG_AR_INSIGHT_WORKBENCH') NEXTVAL ");
            reportId++;
            CustomReportId = String.valueOf(reportId);

        } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
            reportId = getSequenceNumber("select LAST_INSERT_ID(INSIGHT_ID) from PRG_AR_INSIGHT_WORKBENCH ");
            reportId = reportId + 1;
            CustomReportId = String.valueOf(reportId);
        } else {
            CustomReportId = String.valueOf(getSequenceNumber("select PRG_AR_INSIGHT_WORKBENCH_SEQ.nextval from dual"));
        }

        return CustomReportId;
    }

    public PbReturnObject getAllInsights(String userId) {
        PbReturnObject retObj = new PbReturnObject();
        String qry = "SELECT INSIGHT_ID,INSIGHT_NAME,ROLE_ID,CREATED_DATE FROM PRG_AR_INSIGHT_WORKBENCH WHERE INSIGHT_ID IN(SELECT INSIGHT_ID FROM PRG_INSIGHT_ASSIGNMENT WHERE USER_ID='" + userId + "')";
        //  
        try {
            retObj = executeSelectSQL(qry);
        } catch (SQLException ex) {

            logger.error("Exception:", ex);
        }
        return retObj;
    }

    public void deleteInsightWorkBench(String insightId, String userId) {
        ArrayList delList = new ArrayList();
        delList.add("delete from PRG_AR_INSIGHT_WORKBENCH where INSIGHT_ID=" + insightId);
        // delList.add("delete from PRG_INSIGHT_ASSIGNMENT where insightId="+insightId+" and USER_ID='"+userId+"'");
        delList.add("delete from PRG_INSIGHT_ASSIGNMENT where INSIGHT_ID=" + insightId);
        execMultiple(delList);

    }

    public void assignInsightToUsers(String reportId, String[] userIds) {
        String addUserInsights = getResourceBundle().getString("insertInsightAssignmentDetails");
        String finalQuery;
        PbDb pbdb = new PbDb();
        ArrayList<String> assignQueryLst = new ArrayList<String>();
        Object obj[] = null;
        boolean assigned = false;
        try {
            for (int i = 0; i < userIds.length; i++) {
                obj = new Object[2];
                obj[0] = reportId;
                obj[1] = userIds[i];
                finalQuery = pbdb.buildQuery(addUserInsights, obj);
                assignQueryLst.add(finalQuery);
            }
            assigned = pbdb.executeMultiple(assignQueryLst);
        } catch (Exception se) {
            assigned = false;
        }
        if (!assigned) {
//            ProgenLog.log(ProgenLog.SEVERE, this, "assignReportToUsers", "Assigned of Report " + reportId + " failed");
            logger.error("Assigned of Report " + reportId + " failed");
        }
    }

    public void insertQuickRefreshOption(String reportId, boolean quickRefreshEnable, String filePath, ReportSchedule schedule, boolean autorefresh) {
        String finalqry = null;
        if (autorefresh) {
            Calendar startCalendar = Calendar.getInstance();
            startCalendar.setTime(schedule.getStartDate());
            Calendar endCalendar = Calendar.getInstance();
            endCalendar.setTime(schedule.getEndDate());
            Date sdate = schedule.getStartDate();
            Date edate = schedule.getEndDate();
            schedule.setStartDate(null);
            schedule.setEndDate(null);
            List<ReportSchedule> scheduleList = new ArrayList<ReportSchedule>();
            scheduleList.add(schedule);
            //Gson gson = new Gson();
            String gsonString = gson.toJson(scheduleList);
            String startDate = "";
            String endDate = "";
            String qry = getResourceBundle().getString("updateReportMaterFileScheduler");
            Object[] obj = new Object[7];
            obj[0] = filePath;
            obj[1] = quickRefreshEnable;
            obj[2] = autorefresh;
            obj[3] = gsonString;

            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                startDate = startCalendar.get(Calendar.YEAR) + "-" + (startCalendar.get(Calendar.MONTH) + 1) + "-" + startCalendar.get(Calendar.DATE);
                endDate = endCalendar.get(Calendar.YEAR) + "-" + (endCalendar.get(Calendar.MONTH) + 1) + "-" + endCalendar.get(Calendar.DATE);
            } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                startDate = startCalendar.get(Calendar.YEAR) + "-" + (startCalendar.get(Calendar.MONTH) + 1) + "-" + startCalendar.get(Calendar.DATE);
                endDate = endCalendar.get(Calendar.YEAR) + "-" + (endCalendar.get(Calendar.MONTH) + 1) + "-" + endCalendar.get(Calendar.DATE);
            } else {
                startDate = startCalendar.get(Calendar.DATE) + "-" + (startCalendar.get(Calendar.MONTH) + 1) + "-" + startCalendar.get(Calendar.YEAR);
                endDate = endCalendar.get(Calendar.DATE) + "-" + (endCalendar.get(Calendar.MONTH) + 1) + "-" + endCalendar.get(Calendar.YEAR);
            }

            obj[4] = startDate;
            obj[5] = endDate;
            obj[6] = reportId;
            finalqry = buildQuery(qry, obj);
            try {
                execUpdateSQL(finalqry);
            } catch (Exception ex) {

                logger.error("Exception:", ex);
            }
            schedule.setStartDate(sdate);
            schedule.setEndDate(edate);
        } else {
            String qry = getResourceBundle().getString("updateReportMaterFile");
            Object[] obj = new Object[4];
            obj[0] = filePath;
            obj[1] = quickRefreshEnable;
            obj[2] = autorefresh;
            obj[3] = reportId;
            finalqry = buildQuery(qry, obj);
            try {
                execUpdateSQL(finalqry);
            } catch (Exception ex) {

                logger.error("Exception:", ex);
            }
        }

    }

    public HashMap<String, String> getQuickRefreshData(String reportId) {
        PbReturnObject retObj = new PbReturnObject();
        String qry = "select REFRESH_ENABLE,FILE_PATH from PRG_AR_REPORT_MASTER where REPORT_ID=" + reportId;
//    String filePath=null;
        HashMap<String, String> map = new HashMap<String, String>();
        try {
            retObj = executeSelectSQL(qry);
            if (retObj != null && retObj.getRowCount() > 0) {
                map.put("refreshEnable", retObj.getFieldValueString(0, 0));
                map.put("filePath", retObj.getFieldValueString(0, 1));
            }
        } catch (SQLException ex) {

            logger.error("Exception:", ex);
        }
        return map;
    }

    public PbReturnObject getAllQuickRefreshReports() {
        String qry = getResourceBundle().getString("getAllAutoRefreshReports");
        PbReturnObject retObj = new PbReturnObject();
        try {
            retObj = execSelectSQL(qry);
        } catch (Exception e) {
            logger.error("Exception:", e);
        }
        return retObj;
    }

    public void disableQuickRefreshOption(String reportId) {
        PbReturnObject retobj = null;
        String qry = "select FILE_PATH from PRG_AR_REPORT_MASTER where REPORT_ID='" + reportId + "'";
        try {
            retobj = execSelectSQL(qry);
            if (retobj != null && retobj.getRowCount() > 0) {
                String path = retobj.getFieldValueString(0, 0);
                File file = new File(path);
                if (file.exists()) {
                    file.delete();
                }
            }
        } catch (SQLException ex) {

            logger.error("Exception:", ex);
        }
        String delrefreshqry = "UPDATE PRG_AR_REPORT_MASTER SET FILE_PATH='null',REFRESH_ENABLE='false',AUTO_REFRESH='false' where REPORT_ID='" + reportId + "'";
        try {
            execUpdateSQL(delrefreshqry);
        } catch (Exception ex) {

            logger.error("Exception:", ex);
        }
    }
    //added by Nazneen

    public String checkSchedulerPermission(String scheduleId, String userType, String userId) throws SQLException {
        Connection conSource = ProgenConnection.getInstance().getConnection();
        PreparedStatement pstmtForTransterSource;
        ResultSet resultSet = null;
        PbReturnObject returnObject = null;
//        String str = "";
        String getData = "select * from PRG_AR_PERSONALIZED_REPORTS where PRG_PERSONALIZED_ID =? AND PRG_USER_ID = ?" ;
//        String jsonString = null;
        Gson json = new Gson();
        List<String> dataVal = new ArrayList<String>();
        Map dimmap = new HashMap();
        pstmtForTransterSource = conSource.prepareStatement(getData);
        pstmtForTransterSource.setString(1, scheduleId);
        pstmtForTransterSource.setString(2, userId);
        resultSet = pstmtForTransterSource.executeQuery();
        returnObject = new PbReturnObject(resultSet);
        if (userType.equalsIgnoreCase("ADMIN") || userType.equalsIgnoreCase("SUPERADMIN")) {
            dataVal.add("true");
        } else if (returnObject.getRowCount() > 0) {
            dataVal.add("true");
        } else {
            dataVal.add("false");
        }
        dimmap.put("dataVal", dataVal);
        //by Prabal
        if (resultSet != null) {
            resultSet.close();
        }

        if (conSource != null) {
            conSource.close();
        }
        if (pstmtForTransterSource != null) {
            pstmtForTransterSource.close();
        }

        return json.toJson(dimmap);

    }

    public String generateDragRowviewbysHtml(String contextPath, ArrayList<String> rowViewbyIds, ArrayList<String> rowViewbyNames) {
        StringBuffer rowviewbyBuffer = new StringBuffer("");
        for (int i = 0; i < rowViewbyIds.size(); i++) {
            rowviewbyBuffer.append("<li><img src='").append(contextPath).append("/icons pinvoke/report.png' alt=''></img>");
            rowviewbyBuffer.append("<span id='").append((String) rowViewbyIds.get(i)).append("' class='myDragTabs ui-draggable'>").append((String) rowViewbyNames.get(i)).append("</span>");
            rowviewbyBuffer.append("</li>");
        }
        return rowviewbyBuffer.toString();
    }

    public String generateDropRowViewbyHtmldata(String contextPath, ArrayList<String> hideviewLst, ArrayList<String> rowViewbyIds, ArrayList<String> rowViewbyNames) {
        StringBuffer rowviewbyBuffer = new StringBuffer("");
        if (hideviewLst != null && !hideviewLst.isEmpty()) {
            for (int i = 0; i < hideviewLst.size(); i++) {
                if (rowViewbyIds.contains(hideviewLst.get(i))) {
                    int index = rowViewbyIds.indexOf(hideviewLst.get(i));
                    rowviewbyBuffer.append("<li id='").append((String) rowViewbyIds.get(index)).append("_li").append("' style='width: 180px; height: auto; color: white;' class='navtitle-hover'>");
                    rowviewbyBuffer.append("<table id='").append((String) rowViewbyIds.get(index)).append("_table'> <tbody><tr><td><a href='javascript:deleteColumn(" + (String) rowViewbyIds.get(index) + ")' class='ui-icon ui-icon-close' ></a></td><td style='color: black;'>").append((String) rowViewbyNames.get(index)).append("</td></tr></tbody></table></li>");
                }
            }
        }
        return rowviewbyBuffer.toString();
    }
    //   start of code by Nazneen on 6Feb14 for Dimension Segment(Grouping)

    public boolean createDimensionSegmentForViewer(String reportId, String elementId, String segParmVal, String[] parent, String[] child, HttpSession session) {
        //        Step 1 : Add column to table
        String bussTableName = "";
        String bussColName = "";
        String connId = "";
        String bussTableId = "";
        String getTableId = "";
//           String bussSrcId = "";
        String dimId = "";
        String dimTabId = "";
        String getColumnnameFromCustDB = "";
        PbDb pbdb = new PbDb();
        PbReturnObject returnObj = null;
        boolean result = true;
        elementId = elementId.replace("A_", "");
        String columnType = "";
        String actualColFormula = "";
        String filePath = "";

        try {
            String getDependentEleId = "select USER_COL_TYPE,DIM_DEPENDENT_BASE_ELE_ID from PRG_USER_ALL_INFO_DETAILS where element_id =" + elementId;
            returnObj = pbdb.execSelectSQL(getDependentEleId);
            if (returnObj.rowCount > 0) {
                columnType = returnObj.getFieldValueString(0, 0);
                if (columnType.equalsIgnoreCase("CALCULATED")) {
                    filePath = getFilePath(session);
                    actualColFormula = readFormulaFromFile(elementId, filePath);
                    elementId = returnObj.getFieldValueString(0, 1);
                }
                String getBussTabName = "select buss_table_name,buss_col_name,connection_id,buss_table_id,dim_id,dim_tab_id from prg_user_all_info_details where element_id =" + elementId;
                returnObj = pbdb.execSelectSQL(getBussTabName);
                if (returnObj.rowCount > 0) {
                    bussTableName = returnObj.getFieldValueString(0, 0);
                    bussColName = returnObj.getFieldValueString(0, 1);
                    connId = returnObj.getFieldValueString(0, 2);
                    bussTableId = returnObj.getFieldValueString(0, 3);
                    dimId = returnObj.getFieldValueString(0, 4);
                    dimTabId = returnObj.getFieldValueString(0, 5);
                    //dimension segment formula
//                   String dimSegmentFormula = "";
                    StringBuilder dimSegmentFormula = new StringBuilder(1000);
                    String tempFormula = "";
//                   String childData = "";
                    StringBuilder childData = new StringBuilder(1000);
                    String bussTabColName = bussTableName + "." + bussColName;
                    for (int i = 0; i < parent.length; i++) {
                        String[] childs = child[i].toString().replace("&amp;", "&").split(";");
//                        childData = "";
                        childData = new StringBuilder("");
                        for (int s = 0; s < childs.length; s++) {
                            if (!childs[s].equalsIgnoreCase("")) {
//                                   childData += ",'" + childs[s] + "'";
                                childData.append(",'").append(childs[s]).append("'");
                            }
                        }
//                        childData = childData.substring(1);
                        childData = new StringBuilder(childData.toString().substring(1));
                        tempFormula = "CASE WHEN " + bussTabColName + " IN (" + childData.toString() + ") THEN '" + parent[i] + "' ELSE ";
//                        dimSegmentFormula += tempFormula;
                        dimSegmentFormula.append(tempFormula);
                    }
//                   dimSegmentFormula += "'OTHERS'";
                    dimSegmentFormula.append("'OTHERS'");
                    for (int i = 0; i < parent.length; i++) {
//                       dimSegmentFormula += " END";
                        dimSegmentFormula.append(" END");
                    }
                    if (columnType.equalsIgnoreCase("CALCULATED")) {
                        String oldFormula = "(" + actualColFormula + ")";
//                           dimSegmentFormula = dimSegmentFormula.replace(bussTabColName, oldFormula);
                        dimSegmentFormula = new StringBuilder(dimSegmentFormula.toString().replace(bussTabColName, oldFormula));
                    }
                    //dimension segment formula

                    getTableId = " select table_id from prg_db_master_table where table_type='Table' and connection_id=" + connId + " and table_name = '" + bussTableName + "'";
                    returnObj = null;
                    returnObj = pbdb.execSelectSQL(getTableId);
                    if (returnObj.rowCount > 0) {
                        String tableId = returnObj.getFieldValueString(0, 0);
                        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER) || ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                            getColumnnameFromCustDB = "SELECT  DATA_TYPE,CHARACTER_MAXIMUM_LENGTH FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME='" + bussTableName + "' AND COLUMN_NAME='" + bussColName + "'";
                        } else {
                            getColumnnameFromCustDB = "SELECT DATA_TYPE,DATA_LENGTH  FROM USER_TAB_COLUMNS WHERE TABLE_NAME='" + bussTableName + "' AND COLUMN_NAME='" + bussColName + "'";
                        }
                        Connection conn = null;
                        conn = new BusinessGroupDAO().getConnectionIdConnection(connId);
                        returnObj = null;
                        if (conn != null) {
                            try {
                                returnObj = pbdb.execSelectSQL(getColumnnameFromCustDB, conn);
                                if (returnObj.getRowCount() > 0) {
                                    String colName = segParmVal.replace(" ", "_");
                                    String colType = returnObj.getFieldValueString(0, 0);
                                    String colLength = returnObj.getFieldValueString(0, 1);
//                                   Step 2: Insert in DbMasterDetails
                                    result = insertColumnToDbTable(Integer.parseInt(tableId), colName, colType, colLength);
                                    if (result) {
//                                     Step 3:   save dimension member and hierarchy
                                        return result = saveDimensionMembers(dimId, dimTabId, colName, colType, tableId, bussTableName, elementId, bussTableId, dimSegmentFormula.toString(), session);
                                    } else {
                                        return false;
                                    }
                                } else {
                                    return false;
                                }
                            } catch (Exception ex) {

                                logger.error("Exception:", ex);
                            }
                        } else {
                            return false;
                        }
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } catch (SQLException ex) {

            logger.error("Exception:", ex);
            return false;
        }
        return result;
    }
//       step 2: Insert in Db Master Details

    public boolean insertColumnToDbTable(int tableId, String colName, String colType, String colLength) {
        PbDb pbdb = new PbDb();
        String insertColumnToDbTable = "";
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER) || ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
            insertColumnToDbTable = getResourceBundle().getString("insertColumnToDbTableforSQL");
        } else {
            insertColumnToDbTable = getResourceBundle().getString("insertColumnToDbTable");
        }
        ArrayList queryList = new ArrayList();
        boolean result = true;
        try {
            Object[] object = new Object[7];
            String finalQuery = "";
            object[0] = tableId;
            object[1] = colName;
            object[2] = colName;
            object[3] = colType;
            if (colType.equalsIgnoreCase("datetime") || colLength.equalsIgnoreCase("")) {
                object[4] = "null";
            } else {
                object[4] = colLength;
            }
            object[5] = "N";
            object[6] = "Y";
            finalQuery = pbdb.buildQuery(insertColumnToDbTable, object);
            queryList.add(finalQuery);
            result = pbdb.executeMultiple(queryList);
            return result;
        } catch (Exception ex) {

            logger.error("Exception:", ex);
            return false;
        }
    }
//        Step 3: Save dimension member and hierarchy

    public boolean saveDimensionMembers(String dimId, String dimTabId, String colName, String colType, String tableId, String bussTableName, String elementId, String bussTableId, String dimSegmentFormula, HttpSession session) {
        ArrayList queryList = new ArrayList();
        String sql = "";
        Connection con = null;
        Statement st2 = null;
//        Statement st3 = null;
//        ResultSet rs = null;
//        ResultSet rs1 = null;
        String Q1, Q2, Q3;
        int f = 0;
        String dbDimId = "";
        boolean flag = true;
        String columnId = "";
        String dbDimTabId = "";
        try {
            PbDb pbdb = new PbDb();
            PbReturnObject returnObj = null;
            String getDbDimId = "select DB_DIM_ID from PRG_GRP_DIM_TABLES where DIM_ID=" + dimId;
            returnObj = pbdb.execSelectSQL(getDbDimId);
            if (returnObj.getRowCount() > 0) {
                dbDimId = returnObj.getFieldValueString(0, 0);
                returnObj = null;
                String getAllDetails = "select q.tab_id,d.table_name, q.dim_tab_id from prg_qry_dim_tables q , prg_db_master_table d where q.tab_id= d.table_id and q.dim_id=" + dbDimId;
                returnObj = pbdb.execSelectSQL(getAllDetails);
                tableId = returnObj.getFieldValueString(0, 0);
                bussTableName = returnObj.getFieldValueString(0, 1);
                dbDimTabId = returnObj.getFieldValueString(0, 2);
                con = ProgenConnection.getInstance().getConnection();
                Q2 = "select column_id from prg_db_master_table_details  where table_id in (" + tableId + ")and column_id not in( select col_id from prg_qry_dim_tab_details where dim_tab_id=" + dbDimTabId + ")and is_active='Y'";
                returnObj = null;
                returnObj = pbdb.execSelectSQL(Q2);
                if (returnObj.getRowCount() > 0) {
                    for (int i = 0; i < returnObj.getRowCount(); i++) {
                        columnId = returnObj.getFieldValueString(i, 0);
                        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER) || ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                            Q3 = "insert into prg_qry_dim_tab_details (dim_tab_id,col_id,is_available,is_pk_key) values (" + dbDimTabId + "," + columnId + ",'Y','N')";
                        } else {
                            Q3 = "insert into prg_qry_dim_tab_details (dim_tab_col_id,dim_tab_id,col_id,is_available,is_pk_key) values (prg_qry_dim_tab_details_seq.nextval," + dbDimTabId + "," + columnId + ",'Y','N')";
                        }
//                    st2 = con.createStatement();
//                    f = st2.executeUpdate(Q3);
//                    if(f==0){
//                        flag = false;
//                    }
                        queryList.add(Q3);

                        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                            flag = pbdb.executeMultiple(queryList);
                        } else {
                            flag = pbdb.execMultiple(queryList);
                        }
                        //Save member
                        if (flag) {
                            queryList.clear();
                            PbReturnObject pbro = new PbReturnObject();
                            int nextval = 0;
                            returnObj = null;
                            sql = "select m.column_id,m.table_col_name,d.is_pk_key,d.is_available from prg_db_master_table_details m, prg_qry_dim_tab_details d where m.column_id= d.col_id and d.dim_tab_id=" + dbDimTabId + " and d.is_available='Y' and d.col_id=" + columnId + " order by d.is_pk_key desc,m.table_col_name ASC";
                            returnObj = pbdb.execSelectSQL(sql);
                            if (returnObj.getRowCount() > 0) {
                                String desc = returnObj.getFieldValueString(0, 1).replaceAll("_", " ");
                                String memname = returnObj.getFieldValueString(0, 1).replaceAll("_", " ");
                                String key = returnObj.getFieldValueString(0, 1);
                                String keyValue = returnObj.getFieldValueString(0, 1);
                                String all = "Y";
                                String defaultValue = "";
                                String orderBy = returnObj.getFieldValueString(0, 0);
                                String isNullValue = "NAN";
                                String denorQuery = "SELECT DISTINCT " + key + "," + keyValue + " FROM " + bussTableName;
                                //insert in qry master
                                String query = "";
                                if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER) || ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                                    query = "insert into prg_qry_dim_member ( MEMBER_NAME, DIM_ID, DIM_TAB_ID, USE_DENOM_TABLE, DENOM_TAB_ID, DENOM_QUERY,MEMBER_DESC,MEMBER_ORDER_BY,ISNULLvALUE) values('" + memname + "'," + dbDimId + "," + dbDimTabId + ",'Y'," + tableId + ",'" + denorQuery + "','" + desc + "'," + orderBy + ",'" + isNullValue + "')";
                                } else {
                                    pbro = pbdb.execSelectSQL("select PRG_QRY_DIM_MEMBER_SEQ.nextval from dual");
                                    nextval = pbro.getFieldValueInt(0, "NEXTVAL");
                                    query = "insert into prg_qry_dim_member (MEMBER_ID, MEMBER_NAME, DIM_ID, DIM_TAB_ID, USE_DENOM_TABLE, DENOM_TAB_ID, DENOM_QUERY,MEMBER_DESC,MEMBER_ORDER_BY,ISNULLvALUE) values(" + nextval + ",'" + memname + "'," + dbDimId + "," + dbDimTabId + ",'Y'," + tableId + ",'" + denorQuery + "','" + desc + "'," + orderBy + ",'" + isNullValue + "')";
                                }
                                try {
                                    queryList.add(query);
                                    //                    pbdb.execUpdateSQL(query);

                                    //insert in qry details
                                    String querydet = "";
                                    if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                                        querydet = "insert into PRG_QRY_DIM_MEMBER_DETAILS ( MEM_ID, COL_ID, COL_TYPE) values (ident_current('PRG_QRY_DIM_MEMBER')," + orderBy + ",'KEY')";
                                    } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                                        querydet = "insert into PRG_QRY_DIM_MEMBER_DETAILS ( MEM_ID, COL_ID, COL_TYPE) values ((select LAST_INSERT_ID(MEMBER_ID) FROM PRG_QRY_DIM_MEMBER ORDER BY 1 DESC LIMIT 1)," + orderBy + ",'KEY')";
                                    } else {
                                        querydet = "insert into PRG_QRY_DIM_MEMBER_DETAILS (MEM_DETAIL_ID, MEM_ID, COL_ID, COL_TYPE) values (PRG_QRY_DIM_MEMBER_DETAILS_SEQ.nextval," + nextval + "," + orderBy + ",'KEY')";
                                    }
                                    queryList.add(querydet);
                                    //              pbdb.execUpdateSQL(query);
                                    //              if (all == null) {
                                    //                  if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                                    //                      pbdb.execModifySQL("insert into PRG_QRY_DIM_MEMBER_DETAILS (MEM_ID, COL_ID, COL_TYPE,DEFAULT_VAL) values (ident_current('PRG_QRY_DIM_MEMBER')," + keyValuearr[i] + ",'VALUE','" + defaultarr[i] + "')");
                                    //                  } else if(ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                                    //                      pbdb.execModifySQL("insert into PRG_QRY_DIM_MEMBER_DETAILS (MEM_ID, COL_ID, COL_TYPE,DEFAULT_VAL) values ((select LAST_INSERT_ID(MEMBER_ID) FROM PRG_QRY_DIM_MEMBER ORDER BY 1 DESC LIMIT 1)," + keyValuearr[i] + ",'VALUE','" + defaultarr[i] + "')");
                                    //                  } else {
                                    //                      pbdb.execModifySQL("insert into PRG_QRY_DIM_MEMBER_DETAILS (MEM_DETAIL_ID, MEM_ID, COL_ID, COL_TYPE) values (PRG_QRY_DIM_MEMBER_DETAILS_SEQ.nextval," + nextval + "," + keyValuearr[i] + ",'VALUE')");
                                    //                  }
                                    //              } else {
                                    String querydets = "";
                                    if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                                        querydets = "insert into PRG_QRY_DIM_MEMBER_DETAILS ( MEM_ID, COL_ID, COL_TYPE) values (ident_current('PRG_QRY_DIM_MEMBER')," + orderBy + ",'VALUE')";
                                    } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                                        querydets = "insert into PRG_QRY_DIM_MEMBER_DETAILS ( MEM_ID, COL_ID, COL_TYPE) values ((select LAST_INSERT_ID(MEMBER_ID) FROM PRG_QRY_DIM_MEMBER ORDER BY 1 DESC LIMIT 1)," + orderBy + ",'VALUE')";
                                    } else {
                                        querydets = "insert into PRG_QRY_DIM_MEMBER_DETAILS (MEM_DETAIL_ID, MEM_ID, COL_ID, COL_TYPE) values (PRG_QRY_DIM_MEMBER_DETAILS_SEQ.nextval," + nextval + "," + orderBy + ",'VALUE')";
                                    }
                                    queryList.add(querydets);
//                                    flag = pbdb.execMultiple(queryList);
                                    if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                                        flag = pbdb.executeMultiple(queryList);
                                    } else {
                                        flag = pbdb.execMultiple(queryList);
                                    }
                                    if (flag) {
//                                        Step 4: save hierarchy
                                        flag = saveHierarchy(dbDimId, tableId, elementId);
                                        if (flag) {
//                                           Step 5: Save In Business Group
                                            flag = insertInBussGroupSrc(bussTableId, colName, tableId, colType, columnId);
                                            if (flag) {
//                                                Step 6: insert into Dimension Group
                                                flag = insertDimensionGrp(tableId, dimTabId, dimId, elementId, dbDimId, dbDimTabId);
//                                                Step 7: Truncate & Load Dim Map Table
                                                if (flag) {
                                                    flag = truncateInsertDimValues();
                                                    if (flag) {
//                                                      Step 8: MIGRATE DIMENSION MEMBER TO BUSINESS ROLE
                                                        flag = migrateDimensionMemberToRole(elementId, dimId, dimTabId, columnId, dimSegmentFormula, session);
                                                    } else {
                                                        return false;
                                                    }
                                                }
                                            } else {
                                                return false;
                                            }
                                        } else {
                                            return false;
                                        }
                                    } else {
                                        return false;
                                    }
                                } catch (Exception ex) {

                                    logger.error("Exception:", ex);
                                    return false;
                                }
                            } else {
                                return false;
                            }
                        } else {
                            return false;
                        }
                    }
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } catch (SQLException ex) {

            logger.error("Exception:", ex);
            return false;
        }
        return flag;

    }
//         Step 4: Save Hierarchy

    public boolean saveHierarchy(String dbDimId, String tableId, String elementId) {
        PbDb pbdb = new PbDb();
        PbReturnObject returnObj = null;
//        PbReturnObject pbro = new PbReturnObject();
        ArrayList queryList = new ArrayList();
        boolean flag = true;
        String memId = "";
        String relId = "";
        double relLevel = 0;
        String getMemId = "SELECT MAX(MEMBER_ID) FROM PRG_QRY_DIM_MEMBER WHERE DIM_ID=" + dbDimId;
        try {
            returnObj = pbdb.execSelectSQL(getMemId);
            if (returnObj.getRowCount() > 0) {
                memId = returnObj.getFieldValueString(0, 0);
                returnObj = null;
                String hierQry = "SELECT REL_ID, REL_NAME FROM PRG_QRY_DIM_REL where dim_id=" + dbDimId;
                returnObj = pbdb.execSelectSQL(hierQry);
                if (returnObj.getRowCount() > 0) {
                    relId = returnObj.getFieldValueString(0, 0);
//                    String levQry = "select max(REL_LEVEL) from PRG_QRY_DIM_REL_DETAILS where REL_ID=" + relId;
                    String levQry = "select rel_level from PRG_QRY_DIM_REL_DETAILS where REL_ID= " + relId
                            + " and mem_id in (select member_id from prg_qry_dim_member where dim_id= " + dbDimId
                            + " and member_order_by = (select  column_id from prg_db_master_table_details where table_id = " + tableId
                            + " and table_col_name in(select BUSS_COL_NAME from prg_user_all_info_details where element_id = " + elementId + ")))";
                    returnObj = null;
                    returnObj = pbdb.execSelectSQL(levQry);
                    if (returnObj.getRowCount() > 0) {
                        String s = returnObj.getFieldValueString(0, 0);
                        relLevel = Double.parseDouble(s);
//                        relLevel = returnObj.getFieldValueString(0, 0);
                        String newQuery = "select max(REL_LEVEL) from PRG_QRY_DIM_REL_DETAILS where REL_ID = " + relId + " AND REL_LEVEL >" + (relLevel - 1) + " AND REL_LEVEL <" + (relLevel + 1);
                        returnObj = null;
                        returnObj = pbdb.execSelectSQL(newQuery);
                        String ss = returnObj.getFieldValueString(0, 0);
                        relLevel = Double.parseDouble(ss);
                        relLevel = relLevel + 0.1;
                        String insertIntoQryDimRelDetails = "insert into PRG_QRY_DIM_REL_DETAILS (REL_ID, MEM_ID, REL_LEVEL) values (" + relId + "," + memId + "," + relLevel + ")";
                        try {
                            queryList.add(insertIntoQryDimRelDetails);
//                            flag = pbdb.execMultiple(queryList);
                            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                                flag = pbdb.executeMultiple(queryList);
                            } else {
                                flag = pbdb.execMultiple(queryList);
                            }
//                            pbdb.execModifySQL(insertIntoQryDimRelDetails);
                        } catch (Exception ex) {
                            logger.error("Exception:", ex);
                            return false;
                        }
                    }
                }
            }
        } catch (SQLException ex) {

            logger.error("Exception:", ex);
            return false;
        }
        return flag;
    }
//    Step 5: Save In Business Group

    public boolean insertInBussGroupSrc(String bussTableId, String colName, String dbTableId, String colType, String columnId) {
        boolean result = false;
        String getBussSrcId = "select BUSS_SOURCE_ID from PRG_GRP_BUSS_TABLE_SRC where buss_table_id =" + bussTableId;
        String insertBussGrpSrc = "";
        String insertBussTableDetails = "";
        String updateBussTableDetails = "";
        String bussSrcId = "";
        PbDb pbdb = new PbDb();
        PbReturnObject returnObj = null;
        PbReturnObject pbro = new PbReturnObject();
        try {
            returnObj = pbdb.execSelectSQL(getBussSrcId);
            if (returnObj.getRowCount() > 0) {
                bussSrcId = returnObj.getFieldValueString(0, 0);
                if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                    insertBussGrpSrc = getResourceBundle().getString("insertBussGrpSrcforSQL");
                    insertBussTableDetails = getResourceBundle().getString("insertInGrpDetailsforSQL");
                    updateBussTableDetails = getResourceBundle().getString("updateBussTableDetailsSQL");
                } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                    insertBussGrpSrc = getResourceBundle().getString("insertBussGrpSrcforSQL");
                    insertBussTableDetails = getResourceBundle().getString("insertInGrpDetailsforMYSQL");
                    updateBussTableDetails = getResourceBundle().getString("updateBussTableDetailsMYSQL");
                } else {
                    updateBussTableDetails = getResourceBundle().getString("updateBussTableDetails");
                    insertBussGrpSrc = getResourceBundle().getString("insertBussGrpSrc");
                    insertBussTableDetails = getResourceBundle().getString("insertInGrpDetails");
                }
                String finalQuery = "";
                ArrayList queryList = new ArrayList();
                Object object[] = new Object[6];
                Object object1[] = new Object[9];
                object[0] = bussSrcId;
                object[1] = dbTableId;
                object[2] = bussTableId;
                object[3] = columnId;
                object[4] = colName;
                object[5] = colType;
                finalQuery = pbdb.buildQuery(insertBussGrpSrc, object);
                queryList.add(finalQuery);
                object1[0] = bussTableId;
                object1[1] = colName;
                object1[2] = colType;
                object1[3] = dbTableId;
                object1[4] = columnId;
                object1[5] = "N";
                object1[6] = colName;
                object1[7] = colName;
                object1[8] = "Y";
                finalQuery = pbdb.buildQuery(insertBussTableDetails, object1);
                queryList.add(finalQuery);

                Object object2[] = new Object[1];
                object2[0] = bussTableId;
                finalQuery = pbdb.buildQuery(updateBussTableDetails, object2);
                queryList.add(finalQuery);
                result = pbdb.executeMultiple(queryList);
                if (result) {
                    queryList.clear();
                    if (!ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.ORACLE)) {
                        String getTableDtlId = "select BUSS_SRC_TABLE_DTL_ID FROM  prg_grp_buss_table_src_details where DB_COLUMN_ID = " + columnId;
                        returnObj = pbdb.execSelectSQL(getTableDtlId);
                        if (returnObj.getRowCount() > 0) {
                            String tableDtlId = returnObj.getFieldValueString(0, 0);
                            String newQry = "update prg_grp_buss_table_details set BUSS_SRC_TABLE_DTL_ID = " + tableDtlId + " WHERE DB_COLUMN_ID = " + columnId + "  AND BUSS_TABLE_ID=" + bussTableId;
                            queryList.add(newQry);
                            result = pbdb.executeMultiple(queryList);
                        }
                    }
                }

            } else {
                return false;
            }
        } catch (SQLException ex) {

            logger.error("Exception:", ex);
        }
        return result;
    }
//    Step 6: insert into Dimension Group

    private boolean insertDimensionGrp(String tabId, String dimtabId, String dimIds, String elementId, String dbDimId, String dbDimTabId) {
        boolean flag = false;
        String finalQuery = "";
        ArrayList queries = new ArrayList();
        Object[] Obj = null;
        HashMap dimTabMap = new HashMap();
        HashMap memIdMap = new HashMap();
        PbDb pbdb = new PbDb();
        PbReturnObject pbro = new PbReturnObject();
        pbro = null;
        String grpId = "";
        String getDetails = "SELECT GRP_ID FROM PRG_USER_ALL_INFO_DETAILS WHERE ELEMENT_ID=" + elementId;
        try {
            pbro = pbdb.execSelectSQL(getDetails);
            if (pbro.getRowCount() > 0) {
                grpId = pbro.getFieldValueString(0, 0);
                Obj = new Object[3];
//                Step 6-1 Insert in grp dim tables
                queries = insertGrpDimTables(dimtabId, dimIds, queries, dimTabMap, grpId, dbDimTabId);
//                flag = pbdb.execMultiple(queries);
                if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                    flag = pbdb.executeMultiple(queries);
                } else {
                    flag = pbdb.execMultiple(queries);
                }
                queries.clear();
//                 Step 6-2 Insert in grp dim member tables
                if (flag) {
                    queries = insertGrpDimMembers(tabId, dimIds, queries, dimTabMap, memIdMap, grpId, dbDimId, dimtabId, dbDimTabId);
//                    flag = pbdb.execMultiple(queries);
                    if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                        flag = pbdb.executeMultiple(queries);
                    } else {
                        flag = pbdb.execMultiple(queries);
                    }
                    queries.clear();
//                Step 6-3 Insert in grp dim relation tables
                    if (flag) {
                        queries = insertGrpDimRel(dimIds, queries, memIdMap, grpId, dbDimId);
//                        flag = pbdb.executeMultiple(queries);
                        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                            flag = pbdb.executeMultiple(queries);
                        } else {
                            flag = pbdb.execMultiple(queries);
                        }
                        queries = new ArrayList();
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            } else {
                return false;
            }

        } catch (SQLException ex) {

            logger.error("Exception:", ex);
            return false;
        }
        return flag;
    }
//    Step 6-1 Insert in grp dim tables

    private ArrayList insertGrpDimTables(String dimtabId, String dimId, ArrayList queries, HashMap dimTabMap, String grpId, String dbDimTabId) {
        int dimTabId = Integer.parseInt(dimtabId);
        int oldDimTabId = 0;
        oldDimTabId = Integer.parseInt(dbDimTabId);
        dimTabMap.put(String.valueOf(oldDimTabId), dimTabId);
//        Step 6-1-1 Insert in grp dim tables details
        queries = insertGrpDimTablesDetails(dimtabId, queries, dimTabId, oldDimTabId, grpId);
        return queries;
    }
//    Step 6-1-1 Insert in grp dim tables details

    private ArrayList insertGrpDimTablesDetails(String dimtabId, ArrayList queries, int dimTabId, int oldDimTabId, String grpId) {
        PbDb pbdb = new PbDb();
        String insertGrpDimTablesDetailsQuery = getResourceBundle().getString("insertGrpDimTablesDetails");
        String finalQuery = "";
        Object[] Obj = null;
        try {
            Obj = new Object[3];
            Obj[0] = dimTabId;
            Obj[1] = oldDimTabId;
            Obj[2] = grpId;
            finalQuery = pbdb.buildQuery(insertGrpDimTablesDetailsQuery, Obj);
            queries.add(finalQuery);
        } catch (Exception e) {
            logger.error("Exception:", e);
        }
        return queries;
    }
//    Step 6-2 Insert in grp dim member tables

    private ArrayList insertGrpDimMembers(String tabId, String dimId, ArrayList queries, HashMap dimTabMap, HashMap memIdMap, String grpId, String dbDimId, String dimTabId, String dbDimTabId) {
        PbDb pbdb = new PbDb();
        String insertGrpDimMembersQuery = getResourceBundle().getString("insertGrpDimMembers");
        String getQryDimMbrsInfoQuery = "SELECT QDM.member_name,QDM.use_denom_table,QDM.denom_tab_id,QDM.denom_query,qdm.dim_tab_id, "
                + "qdm.member_id, qdm.member_order_by, qdm.isnullvalue  "
                + " FROM PRG_QRY_DIM_TABLES QDT, PRG_QRY_DIM_MEMBER QDM WHERE qdt.dim_id= qdm.dim_id AND qdt.dim_tab_id= qdm.dim_tab_id "
                + " AND qdt.dim_id=" + dbDimId + " and QDM.member_name not in (select MEMBER_NAME from PRG_GRP_DIM_MEMBER where dim_id=" + dimId + ")";
        String finalQuery = "";
        PbReturnObject retObj = null;
        Object[] Obj = null;
        String member_name = "";
        String use_denom_table = "";
        String denom_tab_id = "";
        String denom_query = "";
        String mem_id = "";
        String memOrderBy = "";
        String mem_isNullVal = "";
        try {
            retObj = pbdb.execSelectSQL(getQryDimMbrsInfoQuery);
            int newMemId = 0;
            for (int index = 0; index < retObj.getRowCount(); index++) {
                member_name = retObj.getFieldValueString(index, 0);
                use_denom_table = retObj.getFieldValueString(index, 1);
                denom_tab_id = retObj.getFieldValueString(index, 2);
                denom_query = retObj.getFieldValueString(index, 3);
                mem_id = retObj.getFieldValueString(index, 5);
                memOrderBy = retObj.getFieldValueString(index, 6);
                mem_isNullVal = retObj.getFieldValueString(index, 7);
                if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER) || ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
//                      memIndex++;
                    Obj = new Object[10];
                    Obj[0] = member_name;
                    Obj[1] = dimId;
                    Obj[2] = dimTabId;
                    Obj[3] = use_denom_table;
                    Obj[4] = tabId;
                    Obj[5] = grpId;
                    Obj[6] = denom_query;
                    Obj[7] = member_name;
                    if (memOrderBy != null || memOrderBy != "") {
                        Obj[8] = memOrderBy;
                    } else {
                        Obj[8] = null;
                    }
                    Obj[9] = mem_isNullVal;
                } else {
                    retObj = pbdb.execSelectSQL("select PRG_GRP_DIM_MEMBER_seq.nextval from dual");
                    newMemId = retObj.getFieldValueInt(0, 0);
                    Obj = new Object[11];
                    Obj[0] = newMemId;
                    Obj[1] = member_name;
                    Obj[2] = dimId;
                    Obj[3] = dimTabId;
                    Obj[4] = use_denom_table;
                    Obj[5] = tabId;
                    Obj[6] = grpId;
                    Obj[7] = denom_query;
                    Obj[8] = member_name;
                    if (memOrderBy != null || memOrderBy != "") {
                        Obj[9] = memOrderBy;
                    } else {
                        Obj[9] = null;
                    }
                    Obj[10] = mem_isNullVal;
                }
                memIdMap.put(String.valueOf(mem_id), newMemId);
                finalQuery = pbdb.buildQuery(insertGrpDimMembersQuery, Obj);
                queries.add(finalQuery);
//                Step 6-2-1 Insert in grp dim member tables details
                queries = insertGrpDimMembersDtls(mem_id, queries, newMemId, grpId);
            }
        } 
        catch (SQLException ex) {
            logger.error("Exception:", ex);
        }catch (Exception e) {
            logger.error("Exception:", e);
        }        return queries;
        }
//    Step 6-2-1 Insert in grp dim member tables details

    private ArrayList insertGrpDimMembersDtls(String mem_id, ArrayList queries, int newMemId, String grpId) {
        PbDb pbdb = new PbDb();
        String insertGrpDimMembersDtlsQuery = getResourceBundle().getString("insertGrpDimMembersDtls");
        String finalQuery = "";
        Object[] Obj = null;
        try {
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER) || ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                Obj = new Object[2];
                Obj[0] = mem_id;
                Obj[1] = grpId;
                finalQuery = pbdb.buildQuery(insertGrpDimMembersDtlsQuery, Obj);
            } else {
                Obj = new Object[3];
                Obj[0] = newMemId;
                Obj[1] = mem_id;
                Obj[2] = grpId;
                finalQuery = pbdb.buildQuery(insertGrpDimMembersDtlsQuery, Obj);
            }
            memberId = newMemId;
            queries.add(finalQuery);
        } catch (Exception e) {
            logger.error("Exception:", e);
        }
        return queries;
    }
//     Step 6-3 Insert in grp dim relation tables

    private ArrayList insertGrpDimRel(String dimId, ArrayList queries, HashMap memIdMap, String grpId, String dbDimId) {
        PbDb pbdb = new PbDb();
        String getQryDimRelQuery = "select description, is_default, rel_name, rel_id from prg_qry_dim_rel where dim_id=" + dbDimId;
        PbReturnObject retObj = null;
        String[] relIds = null;
        try {
            retObj = pbdb.execSelectSQL(getQryDimRelQuery);
            relIds = new String[retObj.getRowCount()];
            for (int index = 0; index < retObj.getRowCount(); index++) {
                relIds[index] = retObj.getFieldValueString(index, 3);
//                 Step 6-3-1 Insert in grp dim relation tables details
                queries = insertGrpDimRelDtls(dimId, queries, relIds[index], memIdMap, grpId, dbDimId);
            }
        } catch (Exception e) {
            logger.error("Exception:", e);
        }
        return queries;
    }
//      Step 6-3-1 Insert in grp dim relation tables details

    private ArrayList insertGrpDimRelDtls(String dimId, ArrayList queries, String relId, HashMap memIdMap, String grpId, String dbDimId) {
        PbDb pbdb = new PbDb();
        String getMemIdQuery = "SELECT max(member_id) FROM prg_qry_dim_member";
//        String getQryDimRelDtlsQuery = "SELECT rel_level FROM PRG_QRY_DIM_MBR_REL_DTLS_VIEW where dim_id='&' and rel_id='&' AND rel_level NOT IN (SELECT REL_LEVEL FROM PRG_GRP_DIM_REL_DETAILS WHERE rel_id= & )";
        String getQryDimRelDtlsQuery = "SELECT rel_level FROM PRG_QRY_DIM_MBR_REL_DTLS_VIEW where mem_id = & ";
        String finalQuery = "";
        String memId = "";
        String insertGrpDimRelDtlsQuery = getResourceBundle().getString("insertGrpDimRelDtls");
        Object[] Obj2 = null;
        Object[] Obj = null;
        PbReturnObject retObj = null;
        PbReturnObject retObj1 = null;
        PbReturnObject retObj2 = null;
        try {
            retObj1 = pbdb.execSelectSQL("select REL_ID from PRG_GRP_DIM_REL where DIM_ID=" + dimId);
//            Obj = new Object[3];
//            Obj[0] = dbDimId;
//            Obj[1] = relId;
//            Obj[2] =  retObj1.getFieldValueString(0, 0);
//            finalQuery = pbdb.buildQuery(getQryDimRelDtlsQuery, Obj);
            Obj2 = new Object[2];
            Obj2[0] = dbDimId;
            Obj2[0] = relId;
            retObj2 = pbdb.execSelectSQL(getMemIdQuery);
            memId = retObj2.getFieldValueString(0, 0);
            Obj = new Object[1];
            Obj[0] = memId;
            finalQuery = pbdb.buildQuery(getQryDimRelDtlsQuery, Obj);
            retObj = pbdb.execSelectSQL(finalQuery);
            for (int i = 0; i < retObj.getRowCount(); i++) {
                String str = "select max(member_id) from PRG_GRP_DIM_MEMBER";
                retObj2 = null;
                retObj2 = pbdb.execSelectSQL(str);
                memId = retObj2.getFieldValueString(0, 0);
//                if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
//                    Obj = new Object[4];
//                    Obj[0] = retObj1.getFieldValueString(i, 0);
//                    Obj[1] = retObj.getFieldValueString(i,1);
//                    Obj[2] = relId;
//                    Obj[3] = retObj.getFieldValueString(i, 0);
//
//                    finalQuery = pbdb.buildQuery(insertGrpDimRelDtlsQuery, Obj);
//                    queries.add(finalQuery);
//                } else {
//                    Obj = new Object[4];
//                    Obj[0] = retObj1.getFieldValueString(i, 0);
//                    Obj[1] = retObj.getFieldValueString(i,1);
//                    Obj[2] = relId;
//                    Obj[3] = retObj.getFieldValueString(i, 0);
//                    finalQuery = pbdb.buildQuery(insertGrpDimRelDtlsQuery, Obj);
//                    queries.add(finalQuery);
//                }
                Obj = new Object[3];
                Obj[0] = retObj1.getFieldValueString(i, 0);
                Obj[1] = memId;
                Obj[2] = retObj.getFieldValueString(i, 0);
                finalQuery = pbdb.buildQuery(insertGrpDimRelDtlsQuery, Obj);
                queries.add(finalQuery);
            }
        } catch (Exception e) {
            logger.error("Exception:", e);
        }
        return queries;
    }

    public boolean truncateInsertDimValues() {
        PbDb pbdb = new PbDb();
        boolean flag = false;
        String truncateDimMap = "";
        ArrayList queries = new ArrayList();
        truncateDimMap = "truncate table PRG_GRP_DIM_MAP_KEY_VALUE";
        String addExistingDimValues = "";
        addExistingDimValues = getResourceBundle().getString("addExistingDimValues");
        queries.add(truncateDimMap);
        queries.add(addExistingDimValues);
        try {
//            flag = pbdb.execMultiple(queries);
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                flag = pbdb.executeMultiple(queries);
            } else {
                flag = pbdb.execMultiple(queries);
            }
        } catch (Exception ex) {

            logger.error("Exception:", ex);
            return false;
        }
        return flag;
    }
//    MIGRATE DIMENSION MEMBER TO BUSINESS ROLE
    public static int refId = 0;

    public boolean migrateDimensionMemberToRole(String elementId, String dimId, String dimtabId, String dbColumnId, String dimSegmentFormula, HttpSession session) {
        int foldDtlId = 0;
        PbDb pbDb = new PbDb();
        boolean result = false;
        PbReturnObject retObj1 = null;
        PbReturnObject pbref1 = null;
        PbReturnObject pbref2 = null;
        ArrayList alist = new ArrayList();
        String folderId = "";
        String subFolderId = "";
        String grpId = "";
        String bussColName = "";
        String bussTableId = "";
        try {
            retObj1 = execSelectSQL("select FOLDER_ID,SUB_FOLDER_ID,GRP_ID,BUSS_COL_NAME,BUSS_TABLE_ID from PRG_USER_ALL_INFO_DETAILS where ELEMENT_ID =" + elementId);
            if (retObj1.getRowCount() > 0) {
                folderId = retObj1.getFieldValueString(0, 0);
                subFolderId = retObj1.getFieldValueString(0, 1);
                grpId = retObj1.getFieldValueString(0, 2);
                bussColName = retObj1.getFieldValueString(0, 3);
                bussTableId = retObj1.getFieldValueString(0, 4);

                if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                    pbref1 = execSelectSQL("select LAST_INSERT_ID(ELEMENT_ID) from PRG_USER_SUB_FOLDER_ELEMENTS order by 1 desc limit 1");
                    refId = pbref1.getFieldValueInt(0, 0);
                } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                    pbref1 = execSelectSQL("select MAX(ELEMENT_ID) from PRG_USER_SUB_FOLDER_ELEMENTS");
                    refId = pbref1.getFieldValueInt(0, 0);
                } else {
                    foldDtlId = getSequenceNumber("select PRG_USER_FOLDER_DETAIL_SEQ.NEXTVAL from dual");
                }
                alist = addUserSubFolderTables(alist, grpId, foldDtlId, dimId, dimtabId, subFolderId, dbColumnId, bussColName);
                String updateQuery = "update PRG_USER_SUB_FOLDER_ELEMENTS set REF_ELEMENT_ID = ELEMENT_ID where buss_table_id = " + bussTableId + " and REF_ELEMENT_TYPE = 1 ";
                alist.add(updateQuery);
                result = executeMultiple(alist);
                if (result) {
                    result = partialPublishDimentionMember(bussTableId, folderId, dimSegmentFormula, elementId);
                    if (result) {
                        String eleId = "";
                        pbref2 = execSelectSQL("select MAX(ELEMENT_ID) from PRG_USER_ALL_INFO_DETAILS");
                        eleId = pbref2.getFieldValueString(0, 0);
                        writeFormulaToFile(eleId, dimSegmentFormula, session);
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            }
        } catch (Exception ex) {

            logger.error("Exception:", ex);
        }
        return result;

    }

    public ArrayList addUserSubFolderTables(ArrayList alist, String grpId, int folderDtlId, String dimId, String dimtabId, String subFolderId, String dbColumnId, String bussColName) throws Exception {
        String addUserSubFolderTablesForDimensionsQuery = getResourceBundle().getString("addUserSubFolderTablesForDimensions");
        String getBussDimInfoByGrpIdQuery = getResourceBundle().getString("getBussDimInfoByGrpId");
        String finalQuery = "";
        PbReturnObject retObj = null;
        PbReturnObject retObj2 = null;
        String subFoldTabId = "";
        int subFolderTableId = 0;
        PbDb pbDb = new PbDb();
        Object[] Obj = new Object[5];
        Obj[0] = grpId;
        Obj[1] = dimId;
        Obj[2] = dimId;
        Obj[3] = subFolderId;
        Obj[4] = dbColumnId;
        finalQuery = buildQuery(getBussDimInfoByGrpIdQuery, Obj);
        retObj = execSelectSQL(finalQuery);
        for (int i = 0; i < retObj.getRowCount(); i++) {
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
//                String str = "select ident_current('PRG_USER_SUB_FOLDER_TABLES')";
                String str = "select max(sub_folder_tab_id) from PRG_USER_SUB_FOLDER_TABLES";
                retObj2 = null;
                retObj2 = pbDb.execSelectSQL(str);
                subFolderTableId = Integer.parseInt(String.valueOf(retObj2.getFieldValueInt(0, 0)));
                subFolderTableId = subFolderTableId + 1;
                subFoldTabId = Integer.toString(subFolderTableId);
                Obj = new Object[16];
//                Obj[0] = "IDENT_CURRENT('PRG_USER_FOLDER_DETAIL')";
//                Obj[0] = subFolderTableId;
                Obj[0] = subFolderId; //SUB_FOLDER_ID
                Obj[1] = retObj.getFieldValueString(i, 3); //BUSS_TABLE_ID
                Obj[2] = "Y"; //IS_DIMENSION
                Obj[3] = "N"; //IS_FACT
                Obj[4] = "N"; //IS_BUCKET
                if (retObj.getFieldValueString(i, 1).equalsIgnoreCase("")) {//DISP_NAME
                    Obj[5] = "null";
                } else {
                    Obj[5] = retObj.getFieldValueString(i, 1);
                }
                if (retObj.getFieldValueString(i, 0).equalsIgnoreCase("")) {//DIM_ID
                    Obj[6] = "null";
                } else {
                    Obj[6] = retObj.getFieldValueString(i, 0);
                }
                if (retObj.getFieldValueString(i, 2).equalsIgnoreCase("")) {//DIM_TAB_ID
                    Obj[7] = "null";
                } else {
                    Obj[7] = retObj.getFieldValueString(i, 2);
                }
                if (retObj.getFieldValueString(i, 4).equalsIgnoreCase("")) {//DIM_NAME
                    Obj[8] = "null";
                } else {
                    Obj[8] = retObj.getFieldValueString(i, 4);
                }
                if (retObj.getFieldValueString(i, 5).equalsIgnoreCase("")) {//MEMBER_ID
                    Obj[9] = "null";
                } else {
                    Obj[9] = retObj.getFieldValueString(i, 5);
                }
                if (retObj.getFieldValueString(i, 6).equalsIgnoreCase("")) {//MEMBER_NAME
                    Obj[10] = "null";
                } else {
                    Obj[10] = retObj.getFieldValueString(i, 6);
                }
                if (retObj.getFieldValueString(i, 7).equalsIgnoreCase("")) {//USE_DENOM_TABLE
                    Obj[11] = "null";
                } else {
                    Obj[11] = retObj.getFieldValueString(i, 7);
                }
                if (retObj.getFieldValueString(i, 8).equalsIgnoreCase("")) {//DEFAULT_HIERARCHY_ID
                    Obj[12] = "null";
                } else {
                    Obj[12] = retObj.getFieldValueString(i, 8);
                }
                if (retObj.getFieldValueString(i, 9).equalsIgnoreCase("")) {//DENOM_TAB_ID
                    Obj[13] = "null";
                } else {
                    Obj[13] = retObj.getFieldValueString(i, 9);
                }
                if (retObj.getFieldValueString(i, 1).equalsIgnoreCase("")) {//TABLE_DISP_NAME
                    Obj[14] = "null";
                } else {
                    Obj[14] = retObj.getFieldValueString(i, 1);
                }
                if (retObj.getFieldValueString(i, 1).equalsIgnoreCase("")) {//TABLE_TOOLTIP_NAME
                    Obj[15] = "null";
                } else {
                    Obj[15] = retObj.getFieldValueString(i, 1);
                }
                finalQuery = buildQuery(addUserSubFolderTablesForDimensionsQuery, Obj);
            } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
//                String str = "select LAST_INSERT_ID(sub_folder_id) from PRG_USER_SUB_FOLDER_TABLES order by 1 desc limit 1";
                String str = "select max(sub_folder_tab_id) from PRG_USER_SUB_FOLDER_TABLES";
                retObj2 = null;
                retObj2 = pbDb.execSelectSQL(str);
                subFolderTableId = Integer.parseInt(String.valueOf(retObj2.getFieldValueInt(0, 0)));
                subFolderTableId = subFolderTableId + 1;
                subFoldTabId = Integer.toString(subFolderTableId);
                Obj = new Object[16];
//                Obj[0] = "(select LAST_INSERT_ID(sub_folder_id) from PRG_USER_FOLDER_DETAIL order by 1 desc limit 1)";
//                Obj[0] = subFoldTabId;
                Obj[0] = subFolderId;
                Obj[1] = retObj.getFieldValueString(i, 3);
                Obj[2] = "Y";
                Obj[3] = "N";
                Obj[4] = "N";
                if (retObj.getFieldValueString(i, 1).equalsIgnoreCase("")) {
                    Obj[5] = "null";
                } else {
                    Obj[5] = retObj.getFieldValueString(i, 1);
                }
                if (retObj.getFieldValueString(i, 0).equalsIgnoreCase("")) {
                    Obj[6] = "null";
                } else {
                    Obj[6] = retObj.getFieldValueString(i, 0);
                }
                if (retObj.getFieldValueString(i, 2).equalsIgnoreCase("")) {
                    Obj[7] = "null";
                } else {
                    Obj[7] = retObj.getFieldValueString(i, 2);
                }
                if (retObj.getFieldValueString(i, 4).equalsIgnoreCase("")) {
                    Obj[8] = "null";
                } else {
                    Obj[8] = retObj.getFieldValueString(i, 4);
                }
                if (retObj.getFieldValueString(i, 5).equalsIgnoreCase("")) {
                    Obj[9] = "null";
                } else {
                    Obj[9] = retObj.getFieldValueString(i, 5);
                }
                if (retObj.getFieldValueString(i, 6).equalsIgnoreCase("")) {
                    Obj[10] = "null";
                } else {
                    Obj[10] = retObj.getFieldValueString(i, 6);
                }
                if (retObj.getFieldValueString(i, 7).equalsIgnoreCase("")) {
                    Obj[11] = "null";
                } else {
                    Obj[11] = retObj.getFieldValueString(i, 7);
                }
                if (retObj.getFieldValueString(i, 8).equalsIgnoreCase("")) {
                    Obj[12] = "null";
                } else {
                    Obj[12] = retObj.getFieldValueString(i, 8);
                }
                if (retObj.getFieldValueString(i, 9).equalsIgnoreCase("")) {
                    Obj[13] = "null";
                } else {
                    Obj[13] = retObj.getFieldValueString(i, 9);
                }
                if (retObj.getFieldValueString(i, 1).equalsIgnoreCase("")) {
                    Obj[14] = "null";
                } else {
                    Obj[14] = retObj.getFieldValueString(i, 1);
                }
                if (retObj.getFieldValueString(i, 1).equalsIgnoreCase("")) {
                    Obj[15] = "null";
                } else {
                    Obj[15] = retObj.getFieldValueString(i, 1);
                }
                finalQuery = buildQuery(addUserSubFolderTablesForDimensionsQuery, Obj);
            } else {
                String str = "select PRG_USER_SUB_FOLDER_TABLES_SEQ.NEXTVAL from dual";
                retObj2 = null;
                retObj2 = pbDb.execSelectSQL(str);
                subFoldTabId = retObj2.getFieldValueString(0, 0);
//                retObj2 = execSelectSQL("select PRG_USER_SUB_FOLDER_TABLES_SEQ.NEXTVAL from dual");
//                subFoldTabId = retObj2.getFieldValueString(0, 0);
                Obj = new Object[17];
                Obj[0] = subFoldTabId;
                Obj[1] = subFolderId;
                Obj[2] = retObj.getFieldValueString(i, 3);
                Obj[3] = "Y";
                Obj[4] = "N";
                Obj[5] = "N";
                Obj[6] = retObj.getFieldValueString(i, 1);
                Obj[7] = retObj.getFieldValueString(i, 0);
                Obj[8] = retObj.getFieldValueString(i, 2);
                Obj[9] = retObj.getFieldValueString(i, 4);
                Obj[10] = retObj.getFieldValueString(i, 5);
                Obj[11] = retObj.getFieldValueString(i, 6);
                Obj[12] = retObj.getFieldValueString(i, 7);
                Obj[13] = retObj.getFieldValueString(i, 8);
                Obj[14] = retObj.getFieldValueString(i, 9);
                Obj[15] = retObj.getFieldValueString(i, 1);
                Obj[16] = retObj.getFieldValueString(i, 1);
                finalQuery = buildQuery(addUserSubFolderTablesForDimensionsQuery, Obj);
            }
            alist.add(finalQuery);
            alist = addUserSubFolderElements(alist, retObj.getFieldValueString(i, 3), subFolderId, String.valueOf(subFoldTabId), grpId, String.valueOf(folderDtlId), dbColumnId, bussColName);
        }
//           Insert in custom Drill
        String insertRoleCustomDrillData = null;
        Object grpObj[] = new Object[1];
        grpObj[0] = grpId;
        String getAllMemRel = getResourceBundle().getString("getAllMemRel");
        String fingetAllMemRel = buildQuery(getAllMemRel, grpObj);
        String addRoleCustomDrill = getResourceBundle().getString("getRoleCustomDrill");
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER) || ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
            insertRoleCustomDrillData = getResourceBundle().getString("addRoleCustomDrill1");
        } else {
            insertRoleCustomDrillData = getResourceBundle().getString("addRoleCustomDrill");
        }
        String finaddRoleCustomDrill = buildQuery(addRoleCustomDrill, grpObj);
        PbReturnObject roleDrillobj = execSelectSQL(finaddRoleCustomDrill);
        Vector insertable = new Vector();
        String finALl = buildQuery(addRoleCustomDrill, grpObj);
        PbReturnObject allDims = execSelectSQL(finALl);
        for (int m = 0; m < roleDrillobj.getRowCount(); m++) {
            insertable.add(roleDrillobj.getFieldValueString(m, 5));
        }
        PbReturnObject fingetAllMemRelOb = execSelectSQL(fingetAllMemRel);

        HashMap childs = new HashMap();
        HashMap dimMem = null;
        if (fingetAllMemRelOb.rowCount != 0) {
            String oldDimId = fingetAllMemRelOb.getFieldValueString(0, 0);
            for (int n = 0; n < fingetAllMemRelOb.getRowCount(); n++) {
                String memId = fingetAllMemRelOb.getFieldValueString(n, 2);
                String dimid = "";
                String childId = "";
                int next = n + 1;
                String chElementId = fingetAllMemRelOb.getFieldValueString(n, 2);
                if (next < fingetAllMemRelOb.getRowCount()) {
                    dimid = fingetAllMemRelOb.getFieldValueString(next, 3);
                    if (oldDimId.equalsIgnoreCase(dimid)) {
                        chElementId = fingetAllMemRelOb.getFieldValueString(next, 2);
                    }
                }
                oldDimId = dimid;
                childs.put(memId, chElementId);
            }
            dimMem = new HashMap();
            ArrayList det2 = new ArrayList();

            for (int n = 0; n < fingetAllMemRelOb.getRowCount(); n++) {
                String memId = fingetAllMemRelOb.getFieldValueString(n, 2);
                String dimid = fingetAllMemRelOb.getFieldValueString(n, 3);
                String chElementId = fingetAllMemRelOb.getFieldValueString(n, 2);
                if (dimMem.containsKey(dimid)) {
                    det2 = (ArrayList) dimMem.get(dimid);
                    det2.add(memId);
                    dimMem.put(dimid, det2);
                } else {
                    det2 = new ArrayList();
                    det2.add(memId);
                    dimMem.put(dimid, det2);
                }
            }
        }
        for (int n = 0; n < roleDrillobj.getRowCount(); n++) {
            String dimid = roleDrillobj.getFieldValueString(n, 0);
            String memId = roleDrillobj.getFieldValueString(n, 5);
            if (childs.containsKey(memId)) {
                String chId = childs.get(memId).toString();
                if (dimMem.containsKey(dimid)) {
                    ArrayList det = (ArrayList) dimMem.get(dimid);
                    if (!det.contains(chId)) {
                        childs.put(memId, memId);
                    }
                }
            }
        }
        ArrayList insertRoleDrillList = new ArrayList();
        for (int k = 0; k < insertable.size(); k++) {
            String memId = insertable.get(k).toString();
            String childId = memId;
            if (childs.containsKey(memId)) {
                childId = childs.get(memId).toString();
            }
            Object insertOb[] = null;
            String finaddRoleCustomDrillData = "";
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                insertOb = new Object[5];
                insertOb[0] = "null";
                insertOb[1] = "IDENT_CURRENT('PRG_USER_FOLDER_DETAIL')";
                if (memId != null) {
                    insertOb[2] = memId;
                } else {
                    insertOb[2] = "null";
                }
                if (childId != null) {
                    insertOb[3] = childId;
                } else {
                    insertOb[3] = "null";
                }
                insertOb[4] = "null";

                finaddRoleCustomDrillData = buildQuery(insertRoleCustomDrillData, insertOb);
            } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                insertOb = new Object[5];
                insertOb[0] = "null";
                if (String.valueOf(folderDtlId) != null) {
                    insertOb[1] = folderDtlId;
                } else {
                    insertOb[1] = "(select Last_Insert_Id(FOLDER_ID) from PRG_USER_FOLDER_DETAIL order by 1 desc limit 1)";
                }
                if (memId != null) {
                    insertOb[2] = memId;
                } else {
                    insertOb[2] = "null";
                }
                if (childId != null) {
                    insertOb[3] = childId;
                } else {
                    insertOb[3] = "null";
                }
                insertOb[4] = "null";

                finaddRoleCustomDrillData = buildQuery(insertRoleCustomDrillData, insertOb);
            } else {
                insertOb = new Object[5];
                insertOb[0] = "";
                insertOb[1] = folderDtlId;
                insertOb[2] = memId;
                insertOb[3] = childId;
                insertOb[4] = "";
                finaddRoleCustomDrillData = buildQuery(insertRoleCustomDrillData, insertOb);
            }
            insertRoleDrillList.add(finaddRoleCustomDrillData);

        }

        executeMultiple(insertRoleDrillList);
        return alist;
    }

    public ArrayList addUserSubFolderElements(ArrayList alist, String bussTableId, String subFolderId, String subFoldTabId, String grpId, String folderDtlId, String dbColumnId, String bussColName) throws Exception {
        String finalQuery = "";
        String getUserSubFolderElementsQuery = "";
        String addUserSubFolderElementsQuery = getResourceBundle().getString("addUserSubFolderElements");
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
//            addUserSubFolderElementsQuery = getResourceBundle().getString("addUserSubFolderElementsMysql");
            getUserSubFolderElementsQuery = getResourceBundle().getString("getUserSubFolderElements");
        }
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
            Object[] Obj = new Object[6];
//            Object[] Obj = new Object[5];
//            Obj[0] = "IDENT_CURRENT('PRG_USER_FOLDER_DETAIL')";
//            Obj[1] = "IDENT_CURRENT('PRG_USER_SUB_FOLDER_TABLES')";
//            Obj[2] = "IDENT_CURRENT('PRG_USER_SUB_FOLDER_ELEMENTS')";
//            Obj[3] = bussTableId;
//            Obj[4] = "IDENT_CURRENT('PRG_USER_SUB_FOLDER_TABLES')";
            Obj[0] = subFolderId;
            Obj[1] = bussColName;
            Obj[2] = subFoldTabId;
            Obj[3] = "IDENT_CURRENT('PRG_USER_SUB_FOLDER_ELEMENTS')";
            Obj[4] = bussTableId;
            Obj[5] = dbColumnId;
            finalQuery = buildQuery(addUserSubFolderElementsQuery, Obj);
        } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
            PbReturnObject pbref = null;
            Object[] Obj = new Object[2];
            Obj[0] = bussTableId;
            Obj[1] = dbColumnId;
            finalQuery = buildQuery(getUserSubFolderElementsQuery, Obj);
            pbref = execSelectSQL(finalQuery);
            for (int k = 0; k < pbref.getRowCount(); k++) {
                Obj = new Object[13];
                Obj[0] = "(select LAST_INSERT_ID(sub_folder_id) from PRG_USER_FOLDER_DETAIL order by 1 desc limit 1)";//
                Obj[1] = pbref.getFieldValue(k, 0);
                Obj[2] = pbref.getFieldValue(k, 1);
                Obj[3] = pbref.getFieldValue(k, 2);
//            Obj[4]=pbref.getFieldValue(k, 3);
                Obj[4] = bussColName;
                Obj[5] = pbref.getFieldValue(k, 4);
                Obj[6] = pbref.getFieldValue(k, 5);
                Obj[7] = "(select LAST_INSERT_ID(SUB_FOLDER_TAB_ID) from PRG_USER_SUB_FOLDER_TABLES order by 1 desc limit 1)";
                Obj[8] = String.valueOf(refId + 1);
                Obj[9] = pbref.getFieldValue(k, 6);
                if (pbref.getFieldValue(k, 8) == null || pbref.getFieldValue(k, 7).equals("")) {
                    Obj[10] = null;
                } else {
                    Obj[10] = pbref.getFieldValue(k, 7);
                }
                Obj[11] = 'Y';
                if (pbref.getFieldValue(k, 8) == null) {
                    Obj[12] = null;
                } else {
                    Obj[12] = pbref.getFieldValue(k, 8);
                }
                finalQuery = buildQuery(addUserSubFolderElementsQuery, Obj);
//            alist.add(finalQuery);
                refId++;
            }
        } else {
            Object[] Obj = new Object[5];
            Obj[0] = subFolderId;
            Obj[1] = bussColName;
            Obj[2] = subFoldTabId;
            Obj[3] = bussTableId;
            Obj[4] = dbColumnId;
            finalQuery = buildQuery(addUserSubFolderElementsQuery, Obj);
        }
        alist.add(finalQuery);
        return alist;
    }
//Step 9: Partial publish Dimension Members

    public boolean partialPublishDimentionMember(String busstableid, String folderid, String dimSegmentFormula, String elementId) {
        boolean flag = false;
        String finalquery = null;
        ArrayList partialpublishquerylist = new ArrayList();
        String insertinUserAllInfoDetails = getResourceBundle().getString("insertIntoUserAllInfoDetails");
        String insertintoallAdimDetails = getResourceBundle().getString("insertIntoAllAdimDetails");
        String insertintoallAdimKeyValueElements = getResourceBundle().getString("insertIntoAllAdimKeyValueElements");
        String insertintoallAdimMasterdetails = getResourceBundle().getString("insertIntoAllAdimMaster");
        String insertallDdimDetails = getResourceBundle().getString("insertAllDdimDetails");
        String insertallDdimkeyValueElement = getResourceBundle().getString("insertAllDdimkeyValueElement");
        String insertallDdimMasterDetails = getResourceBundle().getString("insertAllDdimMasterDetails");
        Object objUserAllInfo[] = new Object[4];
        objUserAllInfo[1] = elementId;
        objUserAllInfo[0] = "CALCULATED";
        objUserAllInfo[2] = folderid;
        objUserAllInfo[3] = busstableid;

        Object obj[] = new Object[2];
        obj[0] = folderid;
        obj[1] = busstableid;
        Object keyvalueeleobj[] = new Object[4];
        keyvalueeleobj[0] = folderid;
        keyvalueeleobj[1] = folderid;
        keyvalueeleobj[2] = busstableid;
        keyvalueeleobj[3] = busstableid;

        finalquery = buildQuery(insertinUserAllInfoDetails, objUserAllInfo);
        partialpublishquerylist.add(finalquery);
        finalquery = buildQuery(insertintoallAdimDetails, obj);
        partialpublishquerylist.add(finalquery);
        finalquery = buildQuery(insertintoallAdimKeyValueElements, keyvalueeleobj);
        partialpublishquerylist.add(finalquery);
        finalquery = buildQuery(insertintoallAdimMasterdetails, obj);
        partialpublishquerylist.add(finalquery);
        finalquery = buildQuery(insertallDdimDetails, obj);
        partialpublishquerylist.add(finalquery);
        finalquery = buildQuery(insertallDdimkeyValueElement, keyvalueeleobj);
        partialpublishquerylist.add(finalquery);
        finalquery = buildQuery(insertallDdimMasterDetails, obj);
        partialpublishquerylist.add(finalquery);
        flag = executeMultiple(partialpublishquerylist);

        return flag;

    }

    public void writeFormulaToFile(String eleId, String formula, HttpSession session) {
        if (session != null) {
            InputStream servletStream = session.getServletContext().getResourceAsStream("/WEB-INF/classes/cache.ccf");
            String advHtmlFileProps = "";
            String fileName = "";
            if (servletStream != null) {
                try {
                    Properties fileProps = new Properties();
                    fileProps.load(servletStream);
                    advHtmlFileProps = fileProps.getProperty("jcs.auxiliary.DC.attributes.DiskPath");
                    String folderName = "DimensionSegmentFormula";
                    String folderPath = advHtmlFileProps + File.separator + folderName;
                    File folderDir = new File(folderPath);
                    if (!folderDir.exists()) {
                        folderDir.mkdir();
                    }
                    fileName = folderDir + File.separator + "Formula_" + eleId + ".txt";
//        String fileName = "c:/usr/local/cache/DimensionFormula_" + eleId + ".txt";

                    FileWriter fileWriter = new FileWriter(fileName);
                    //wrap FileWriter in BufferedWriter.
                    BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                    // append a newline formula.
                    bufferedWriter.write(formula);
                    //close files.
                    if (bufferedWriter != null) {
                        bufferedWriter.close();
                    }
                } catch (IOException ex) {

                    logger.error("Error writing to file:", ex);
                }
            }
        } else {
            logger.error("Session Null.............");
        }
    }
//    public String readFormulaFromFile(String elementID,HttpSession session){
//        String fileName = "";
////        String fileName = "c:/usr/local/cache/DimensionFormula_" + elementID + ".txt";
//        String line = null;
//        String formula = "";
//        if (session != null) {
//            InputStream servletStream = session.getServletContext().getResourceAsStream("/WEB-INF/classes/cache.ccf");
//            String advHtmlFileProps = "";
//            Properties fileProps = new Properties();
//            if (servletStream != null) {
//                try {
//                    try {
//                        fileProps.load(servletStream);
//                    } catch (IOException ex) {
//                        logger.error("Exception:",ex);
//                    }
//                    advHtmlFileProps = fileProps.getProperty("jcs.auxiliary.DC.attributes.DiskPath");
//                    fileName = advHtmlFileProps + File.separator + "DimensionSegmentFormula" + File.separator  + "Formula_" + elementID + ".txt";
//                    FileReader fileReader = new FileReader(fileName);
//                    //wrap FileReader in BufferedReader.
//                    BufferedReader bufferedReader = new BufferedReader(fileReader);
//                    try {
//                        while ((line = bufferedReader.readLine()) != null) {
//                            formula = line;
//                        }
//                        
//                        //close files.
//                        if (bufferedReader != null) {
//                            bufferedReader.close();
//                        }
//                    } catch (IOException ex) {
//                        
//                        logger.error("Exception:",ex);
//                    }
//                } catch (FileNotFoundException ex) {
//                    
//                    logger.error("Exception:",ex);
//                }
//            }
//        }
//        return formula;
//    }

    public String getFilePath(HttpSession session) {
        String advHtmlFileProps = "";
        if (session != null) {
            InputStream servletStream = session.getServletContext().getResourceAsStream("/WEB-INF/classes/cache.ccf");
            Properties fileProps = new Properties();
            if (servletStream != null) {
                try {
                    fileProps.load(servletStream);
                    advHtmlFileProps = fileProps.getProperty("jcs.auxiliary.DC.attributes.DiskPath");
                } catch (IOException ex) {

                    logger.error("Exception:", ex);
                }
            }
        }
        return advHtmlFileProps;
    }

    public String readFormulaFromFile(String elementID, String advHtmlFileProps) {
        String fileName = "";
        String line = null;
        String formula = "";
        Properties fileProps = new Properties();
        fileName = advHtmlFileProps + File.separator + "DimensionSegmentFormula" + File.separator + "Formula_" + elementID + ".txt";
        FileReader fileReader;
        try {
            fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            try {
                while ((line = bufferedReader.readLine()) != null) {
                    formula = line;
                }
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (IOException ex) {

                logger.error("Error reading file" + fileName, ex);
            }
        } catch (FileNotFoundException ex) {

            logger.error("Exception:", ex);
        }
        return formula;
    }

    public int checkforDimensionType(String elementId) {
        PbReturnObject retObj = new PbReturnObject();
        int rowCnt = 0;
        String sql = "select USER_COL_TYPE from prg_user_all_info_details where element_id=" + elementId;
        try {
            retObj = execSelectSQL(sql);
            String userColType = retObj.getFieldValueString(0, 0);
            if (userColType.equalsIgnoreCase("CALCULATED")) {
                rowCnt = 1;
            }
        } catch (SQLException ex) {

            logger.error("Exception:", ex);
        }
        return rowCnt;
    }

//                end of code by Nazneen in Feb14 for Dimension Segment(Grouping)
//added by Nazneen for Global Parameters
    //modified by krishan
    public int saveGblParamDetails(HttpSession session, String openReportTab, String layoutVar, String reportTitleSize, String reportTitleAlign, String colorOnGrp, String piTheme, String fromDateToDate, String isYearCal, String piVersion, String reportTab, String defaultPageSize, String Filters, String Comparison, String datashow, Map<String, String> currencyAsMap, String headerFont, String headerLength, String favReportAsTag, String recentlyReportsAsTag, String customReportAsTag, String footerOption, String customTagSequence,String dateheader) throws SQLException, Exception {
        Connection conn = null;
        conn = ProgenConnection.getInstance().getConnection();
        PreparedStatement pstmtForTransfer;
        String script = "";
        Map<String, String> scriptMap = new HashMap<String, String>();
        int flag = 0;
        ResultSet resultSet = null;
        PbReturnObject retObj = null;
        String updateGblSetupVals = "";
        scriptMap.putAll(currencyAsMap);
        script = "reportTitleSize~" + reportTitleSize + ";reportTitleAlign~" + reportTitleAlign + ";colorOnGrp~" + colorOnGrp + ";piTheme~" + piTheme + ";fromDateToDate~" + fromDateToDate + ";isYearCal~" + isYearCal + ";piVersion~" + piVersion + ";reportTab~" + reportTab + ";defaultPageSize~" + defaultPageSize + ";Filters~" + Filters + ";Comparison~" + Comparison + ";datashow~" + datashow + ";layoutVar~" + layoutVar +";dateheader~"+dateheader+";favReportAsTag~"+favReportAsTag.replaceAll("\\s+","")+";openReportTab~"+openReportTab+";recentlyReports~"+recentlyReportsAsTag+";customReports~"+customReportAsTag+";Footeroption~"+footerOption+";headerfont~"+headerFont+";HEADER_LENGThS~"+headerLength;
        Object inOb1[] = new Object[1];
        inOb1[0] = script;
        String addCalQry = "";
        //added by krishan
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.ORACLE)) {
            addCalQry = "insert into PRG_GBL_SETUP_VALUES(SETUP_ID,SETUP_KEY,SETUP_CHAR_VALUE)" + " values ('&','&','&')";
        } else {
            addCalQry = "insert into PRG_GBL_SETUP_VALUES(SETUP_ID,SETUP_KEY,SETUP_CHAR_VALUE)" + " values ('&','&','&')";
        }
        Object inOb[] = new Object[3];
        inOb[0] = 3;
        inOb[1] = "GLOBAL_PARAMS";
        inOb[2] = script;
        String Query = buildQuery(addCalQry, inOb);
//        Added by Faiz Ansari
        updateHeaderTags(session.getAttribute("USERID").toString(), (headerFont + "," + headerLength));
//        End!!!
        // added by prabal
        saveNewHashMapGblParam(openReportTab, layoutVar, reportTitleSize, reportTitleAlign, colorOnGrp, piTheme, fromDateToDate, isYearCal, piVersion, reportTab, defaultPageSize, Filters, Comparison, datashow, currencyAsMap, headerFont, headerLength, favReportAsTag, recentlyReportsAsTag, customReportAsTag, footerOption, customTagSequence,dateheader);

//      updateGblSetupVals = "UPDATE PRG_GBL_SETUP_VALUES SET SETUP_CHAR_VALUE = '&', SETUP_DATE_TYPE_VALUE='"+fromDateToDate+"' WHERE SETUP_KEY='GLOBAL_PARAMS'";
        updateGblSetupVals = "UPDATE PRG_GBL_SETUP_VALUES SET SETUP_CHAR_VALUE = '&' WHERE SETUP_KEY='GLOBAL_PARAMS'";
        String q1 = buildQuery(updateGblSetupVals, inOb1);
        if (conn != null) {
            try {
                pstmtForTransfer = ProgenConnection.getInstance().getConnection().prepareStatement("select * from  PRG_GBL_SETUP_VALUES where SETUP_ID=" + 3);
                resultSet = pstmtForTransfer.executeQuery();
                if (resultSet.next()) {
                    pstmtForTransfer = conn.prepareStatement(q1);
                    pstmtForTransfer.executeUpdate();
                    flag = 1;
                } else {
                    pstmtForTransfer = conn.prepareStatement(Query);
                    pstmtForTransfer.executeUpdate();
                    flag = 1;
                }
            } catch (Exception ex) {
                logger.error("Exception:", ex);
                flag = 0;
            }
        }
        if (conn != null) {
            conn = null;
        }
        return flag;
    }
//        Added by Faiz Ansari
    public void updateHeaderTags(String userId, String data) throws Exception {
        try {
            String qry = "update  PRG_AR_USERS set HEADER_TAGS='" + data + "' where PU_ID=" + userId;
            execUpdateSQL(qry);
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
    }
//End!!!

    /**
     * *********************************************************************************
     * @Created By : Prabal Pratap Singh
     *
     * @since : 22/12/2015 @reason : For saving Global parameter into Table
     * @params : String openReportTab,String layoutVar,String
     * reportTitleSize,String reportTitleAlign,String colorOnGrp,String
     * piTheme,String fromDateToDate, String isYearCal,String piVersion,String
     * reportTab, String defaultPageSize,String Filters,String Comparison,String
     * datashow ,Map<String,String> currencyAsMap,String headerFont,String
     * headerLength,String favReportAsTag,String recentlyReportsAsTag
     * @see : com.progen.reportview.db.PbReportViewerDAO
     * @return : void
************************************************************************************
     */
    private static void saveNewHashMapGblParam(String openReportTab, String layoutVar, String reportTitleSize, String reportTitleAlign, String colorOnGrp, String piTheme, String fromDateToDate, String isYearCal, String piVersion, String reportTab, String defaultPageSize, String Filters, String Comparison, String datashow, Map<String, String> currencyAsMap, String headerFont, String headerLength, String favReportAsTag, String recentlyReportsAsTag, String customReportAsTag, String footerOption, String customTagSequence,String dateheader ) throws SQLException {
        Connection conn = null;
        conn = ProgenConnection.getInstance().getConnection();
        PreparedStatement pstmt;
        String newQuerry = "UPDATE PRG_GBL_SETUP_VALUES SET SETUP_CHAR_VALUE = ? WHERE SETUP_KEY=?";
//        Map<String,String> scriptMap=new HashMap<String,String>();
        int[] setupId = new int[]{22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 38, 39, 40, 41, 42, 43,49};
        String[] setupKey = new String[]{"SETUP_LAYOUT", "SETUP_REPORTTAB", "SETUP_REPORTTITLEALIGN", "SETUP_PITHEME", "SETUP_CURRENCYSHOWNAS", "SETUP_FROM_DATE_TO_DATE", "SETUP_COMPARISON", "SETUP_FILTERS", "SETUP_COLOR_0N_GRP", "SETUP_DATASHOW", "SETUP_REPORTTITLESIZE", "SETUP_PI_VERSION", "SETUP_IS_YEAR_CAL", "SETUP_DEFAULT_PAGE_SIZE", "FAV_REP_AS_TAG", "RECENT_REP_AS_TAG", "FOOTER_OPTION", "TAGGED_USER_ID", "CUSTOM_REP_AS_TAG", "CUSTOM_TAG_SEQUENCE","DATE_HEADER"};
        if (conn != null) {
            try {
                pstmt = conn.prepareStatement(newQuerry);
                for (int i = 0; i < setupKey.length; i++) {
                    if (setupKey[i].equalsIgnoreCase("SETUP_LAYOUT") && layoutVar != null) {
                        pstmt.setString(1, layoutVar);
                    } else if (setupKey[i].equalsIgnoreCase("SETUP_REPORTTAB") && openReportTab != null) {
                        pstmt.setString(1, openReportTab);
                    } else if (setupKey[i].equalsIgnoreCase("SETUP_REPORTTITLEALIGN") && reportTitleAlign != null) {
                        pstmt.setString(1, reportTitleAlign);
                    } else if (setupKey[i].equalsIgnoreCase("SETUP_PITHEME") && piTheme != null) {
                        pstmt.setString(1, piTheme);
                    } else if (setupKey[i].equalsIgnoreCase("SETUP_CURRENCYSHOWNAS") && (!currencyAsMap.isEmpty())) {
                        pstmt.setString(1, currencyAsMap.toString());
                    } else if (setupKey[i].equalsIgnoreCase("SETUP_FROM_DATE_TO_DATE") && fromDateToDate != null) {
                        pstmt.setString(1, fromDateToDate);
                    } else if (setupKey[i].equalsIgnoreCase("SETUP_COMPARISON") && Comparison != null) {
                        pstmt.setString(1, Comparison);
                    } else if (setupKey[i].equalsIgnoreCase("SETUP_FILTERS") && Filters != null) {
                        pstmt.setString(1, Filters);
                    } else if (setupKey[i].equalsIgnoreCase("SETUP_COLOR_0N_GRP")) {
                        pstmt.setString(1, colorOnGrp);
                    } else if (setupKey[i].equalsIgnoreCase("SETUP_DATASHOW") && colorOnGrp != null) {
                        pstmt.setString(1, datashow);
                    } else if (setupKey[i].equalsIgnoreCase("SETUP_REPORTTITLESIZE") && reportTitleSize != null) {
                        pstmt.setString(1, reportTitleSize);
                    } else if (setupKey[i].equalsIgnoreCase("SETUP_PI_VERSION") && piVersion != null) {
                        pstmt.setString(1, piVersion);
                    } else if (setupKey[i].equalsIgnoreCase("SETUP_IS_YEAR_CAL") && isYearCal != null) {
                        pstmt.setString(1, isYearCal);
                    } else if (setupKey[i].equalsIgnoreCase("SETUP_DEFAULT_PAGE_SIZE") && defaultPageSize != null) {
                        pstmt.setString(1, defaultPageSize);
                    } else if (setupKey[i].equalsIgnoreCase("FAV_REP_AS_TAG") && favReportAsTag != null) {
                        pstmt.setString(1, favReportAsTag);

                    } else if (setupKey[i].equalsIgnoreCase("RECENT_REP_AS_TAG") && recentlyReportsAsTag != null) {
                        pstmt.setString(1, recentlyReportsAsTag);

                    } else if (setupKey[i].equalsIgnoreCase("CUSTOM_REP_AS_TAG") && customReportAsTag != null) {
                        pstmt.setString(1, customReportAsTag);
                    } else if (setupKey[i].equalsIgnoreCase("FOOTER_OPTION") && footerOption != null) {
                        pstmt.setString(1, footerOption);
                    } else if (setupKey[i].equalsIgnoreCase("CUSTOM_TAG_SEQUENCE") && customTagSequence != null) {
                        pstmt.setString(1, customTagSequence);
                    }else if (setupKey[i].equalsIgnoreCase("DATE_HEADER") && dateheader != null) {
                        pstmt.setString(1, dateheader);
                    }
                    pstmt.setString(2, setupKey[i]);
                    pstmt.addBatch();
                }//End of for loop
                pstmt.executeBatch();
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (SQLException e) {
                logger.error("Exception:", e);
            } finally {
                if (conn != null) {
                    conn.close();
                }
            }
        }
    }

    public int saveGblParamtabDetails(String reportTitleSize) throws SQLException {
        Connection conn = null;
        conn = ProgenConnection.getInstance().getConnection();
        PreparedStatement pstmtForTransfer;
        String script = "";
        int flag = 0;
//        ResultSet resultSet = null;
        PbReturnObject retObj = null;
        String updateGblSetuptabVals = "";
        // script = "reportTitleSize~"+reportTitleSize+";reportTitleAlign~"+reportTitleAlign+";colorOnGrp~"+colorOnGrp+";piTheme~"+piTheme+";fromDateToDate~"+fromDateToDate+";isYearCal~"+isYearCal+";piVersion~"+piVersion;
        Object inOb[] = new Object[1];
        inOb[0] = script;
//        updateGblSetupVals = "UPDATE PRG_GBL_SETUP_VALUES SET SETUP_CHAR_VALUE = '&', SETUP_DATE_TYPE_VALUE='"+fromDateToDate+"' WHERE SETUP_KEY='GLOBAL_PARAMS'";
        updateGblSetuptabVals = "UPDATE PRG_GBL_SETUP_VALUES SET SETUP_CHAR_VALUE = '&' WHERE SETUP_KEY='GLOBAL_PARAMS'";
        String q1 = buildQuery(updateGblSetuptabVals, inOb);
        if (conn != null) {
            try {
                pstmtForTransfer = conn.prepareStatement(q1);
                pstmtForTransfer.executeUpdate();
                flag = 1;
            } catch (Exception ex) {
                logger.error("Exception:", ex);
                flag = 0;
            }
        }
        if (conn != null) {
            conn = null;
        }
        return flag;
    }

    public void setGlobalParameters(Container container) {
        PbReturnObject returnObject = null;
        String setUpCharVal = "";
        String reportTitleSize = "12";
        String reportTitleAlign = "Left";
        String colorOnGrp = "None";
        String piTheme = "blue";
        String fromDateToDate = "No";
        String isYearCal = "No";
        String piVersion = "version2014";
        String defaultPageSize = "25";
        String Filters = "1";
        String reportTab = "report"; //added by krishan
        String Comparison = "Yes";  //added by krishan
        String datashow = "usbased";
        try {
            String qery1 = "SELECT SETUP_CHAR_VALUE FROM PRG_GBL_SETUP_VALUES WHERE SETUP_KEY = 'GLOBAL_PARAMS'";
            returnObject = execSelectSQL(qery1);
            if (returnObject.getRowCount() > 0) {
                setUpCharVal = returnObject.getFieldValueString(0, 0);
            }
            if (setUpCharVal != null && !setUpCharVal.equalsIgnoreCase("") && !setUpCharVal.equalsIgnoreCase("null")) {
                String arrSetUpCharVal[] = setUpCharVal.split(";");
                for (int i = 0; i < arrSetUpCharVal.length; i++) {
                    String temp = arrSetUpCharVal[i];
                    if (temp.contains("reportTitleSize")) {
                        reportTitleSize = temp.substring(temp.indexOf("~") + 1);
                    } else if (temp.contains("reportTitleAlign")) {
                        reportTitleAlign = temp.substring(temp.indexOf("~") + 1);
                    } else if (temp.contains("colorOnGrp")) {
                        colorOnGrp = temp.substring(temp.indexOf("~") + 1);
                    } else if (temp.contains("piTheme")) {
                        piTheme = temp.substring(temp.indexOf("~") + 1).toLowerCase();
                    } else if (temp.contains("fromDateToDate")) {
                        fromDateToDate = temp.substring(temp.indexOf("~") + 1);
                    } else if (temp.contains("isYearCal")) {
                        isYearCal = temp.substring(temp.indexOf("~") + 1);
                    } else if (temp.contains("piVersion")) {
                        piVersion = temp.substring(temp.indexOf("~") + 1);
                    } else if (temp.contains("defaultPageSize")) {
                        defaultPageSize = temp.substring(temp.indexOf("~") + 1);
                    } else if (temp.contains("Filters")) {
                        Filters = temp.substring(temp.indexOf("~") + 1);
                    } else if (temp.contains("reportTab")) {                // added by krishan
                        reportTab = temp.substring(temp.indexOf("~") + 1);

                    } else if (temp.contains("Comparison")) {
                        Comparison = temp.substring(temp.indexOf("~") + 1);
                    } else if (temp.contains("datashow")) {
                        datashow = temp.substring(temp.indexOf("~") + 1);    // ended by krishan
                    }
                }
            }

            container.reportTitleSize = reportTitleSize;
            container.reportTitleAlign = reportTitleAlign;
            container.colorOnGrp = colorOnGrp;
            container.piTheme = piTheme;
            container.fromDateToDate = fromDateToDate;
            container.isYearCal = isYearCal;
            container.piVersion = piVersion;
            container.defaultPageSize = defaultPageSize;
            container.Filters = Filters;
            container.Comparison = Comparison;
            container.datashow = datashow;
        } catch (Exception e) {
            logger.error("Exception:", e);
        }

    }

    public String getGlobalParametersPiTheme() {
        PbReturnObject returnObject = null;
        String setUpCharVal = "";
        String reportTitleSize = "12";
        String reportTitleAlign = "left";
        String colorOnGrp = "none";
        String piTheme = "blue";
        String qery1 = "SELECT SETUP_CHAR_VALUE FROM PRG_GBL_SETUP_VALUES WHERE SETUP_KEY = 'GLOBAL_PARAMS'";
        try {
            returnObject = execSelectSQL(qery1);
        } catch (SQLException ex) {

            logger.error("Exception:", ex);
        }
        if (returnObject.getRowCount() > 0) {
            setUpCharVal = returnObject.getFieldValueString(0, 0);
        }
        if (setUpCharVal != null && !setUpCharVal.equalsIgnoreCase("") && !setUpCharVal.equalsIgnoreCase("null")) {
            String arrSetUpCharVal[] = setUpCharVal.split(";");
            for (int i = 0; i < arrSetUpCharVal.length; i++) {
                String temp = arrSetUpCharVal[i];
                if (temp.contains("reportTitleSize")) {
                    reportTitleSize = temp.substring(temp.indexOf("~") + 1);
                    reportTitleSize = reportTitleSize;
                } else if (temp.contains("reportTitleAlign")) {
                    reportTitleAlign = temp.substring(temp.indexOf("~") + 1);
                } else if (temp.contains("colorOnGrp")) {
                    colorOnGrp = temp.substring(temp.indexOf("~") + 1);
                } else if (temp.contains("piTheme")) {
                    piTheme = temp.substring(temp.indexOf("~") + 1).toLowerCase();
                }
            }
        }
        return piTheme;
    }

    public String getHeaderTags(String userId, HttpSession session) throws SQLException {
        PbDb pbdb = new PbDb();
        String tagQuery = "select distinct TAG_ID,TAG_NAME,TAG_DESC,FONT_SIZE,SEQUENCE_ID from PRG_TAG_MASTER "
                + " where user_id = " + userId + " order by SEQUENCE_ID";
        PbReturnObject tagObj = pbdb.execSelectSQL(tagQuery);
        String value = "";
        if (tagObj != null && tagObj.rowCount > 0) {
            value = tagObj.getFieldValueString(0, 0);
        }
        return value;

    }
    //started by nazneen for getting blocks based on clicked tag on 4Dec 2014

    public String getTagsBlocks(String userId, String tagId, HttpSession session) {
        String jsonString = null;
        PbReturnObject returnObject = null;
        Gson json = new Gson();
        List<String> tagShortDesc = new ArrayList<String>();
        List<String> tagLongDesc = new ArrayList<String>();
        List<String> reportId = new ArrayList<String>();
        List<String> tagType = new ArrayList<String>();
        List<String> tagAssignId = new ArrayList<String>();
        Map dimmap = new HashMap();
        CacheLayer cacheLayer = CacheLayer.getCacheInstance();// ByPrabal  
        try {
            if (cacheLayer.getdata("getTagsBlocks" + userId + tagId) == null) {
                String query = "Select distinct A.Report_Id,A.Tag_Short_Desc,A.Tag_Long_Desc,A.TAG_TYPE,A.TAG_ASSIGN_ID,A.SEQUENCE_ID  FROM Prg_Tag_Report_Assignment A  where  A.USER_ID =" + userId + " AND A.TAG_ID =" + tagId + " order by A.SEQUENCE_ID ";
                returnObject = super.execSelectSQL(query);
                cacheLayer.setdata("getTagsBlocks" + userId + tagId, returnObject);
            } else {
                returnObject = (PbReturnObject) cacheLayer.getdata("getTagsBlocks" + userId + tagId);
            }
//            returnObject = super.execSelectSQL(query);
            if (returnObject != null) {
                for (int i = 0; i < returnObject.getRowCount(); i++) {
                    tagShortDesc.add(returnObject.getFieldValueString(i, 1));
                    tagLongDesc.add(returnObject.getFieldValueString(i, 2));
                    reportId.add(returnObject.getFieldValueString(i, 0));
                    tagType.add(returnObject.getFieldValueString(i, 3));
                    tagAssignId.add(returnObject.getFieldValueString(i, 4));
                }
            }
            dimmap.put("tagShortDesc", tagShortDesc);
            dimmap.put("tagLongDesc", tagLongDesc);
            dimmap.put("reportId", reportId);
            dimmap.put("tagType", tagType);
            dimmap.put("tagAssignId", tagAssignId);
            if (String.valueOf(session.getAttribute("status")).equalsIgnoreCase("ok")) {
            } else {
                if (session.getAttribute("headerRepName0") != null && !session.getAttribute("headerRepName0").toString().equalsIgnoreCase("null") && !session.getAttribute("headerRepName0").toString().equalsIgnoreCase("Undefined") && session.getAttribute("headerRepName1") != null && !session.getAttribute("headerRepName1").toString().equalsIgnoreCase("null") && !session.getAttribute("headerRepName1").toString().equalsIgnoreCase("Undefined") && session.getAttribute("headerRepName2") != null && !session.getAttribute("headerRepName2").toString().equalsIgnoreCase("null") && !session.getAttribute("headerRepName2").toString().equalsIgnoreCase("Undefined")) {
                } else {
                    if (reportId.size() >= 3) {
                        for (int i = 0; i < reportId.size() && i < 3; i++) {
                            session.setAttribute("headerRepID" + i, reportId.get(i));
                            session.setAttribute("headerRepName" + i, tagShortDesc.get(i).toString());
                            session.setAttribute("headerRepTitle" + i, tagShortDesc.get(i).toString());
                            if (session.getAttribute("HEADER_LENGTH") != null && !session.getAttribute("HEADER_LENGTH").toString().equalsIgnoreCase("null") && !session.getAttribute("HEADER_LENGTH").toString().equalsIgnoreCase("")) {
                                if (tagShortDesc.get(i).toString().length() >= Integer.parseInt(session.getAttribute("HEADER_LENGTH").toString())) {
                                    session.setAttribute("headerRepName" + i, (tagShortDesc.get(i).toString()).substring(0, Integer.parseInt(session.getAttribute("HEADER_LENGTH").toString())));
                                }
                            }
                        }
                    } else {
                        for (int j = 0; j < 3; j++) {
                            session.setAttribute("headerRepID" + j, "Undefined");
                            session.setAttribute("headerRepName" + j, "Undefined");
                            session.setAttribute("headerRepTitle" + j, "Undefined");
                        }
                    }
                }
            }


            jsonString = json.toJson(dimmap);
        } catch (SQLException ex) {

            logger.error("Exception:", ex);
        }
        return jsonString;
    }
//ended by nazneen for getting blocks based on clicked tag on 4Dec 2014
//added Gopesh

    public String generateChartJson(Container container, String userId) {
        JsonGenerator jsonGenerator = new JsonGenerator();
        String chartJson = jsonGenerator.generateChartData(container, userId);
        return chartJson;
    }

    public String generateoneviewJson(Container container, String userId, String regid) {
        JsonGenerator jsonGenerator = new JsonGenerator();
        String chartJson = jsonGenerator.generateoneviewChartData(container, userId, regid);
        return chartJson;
    }
//     public String saveChartJson(Container container,String userId,String oneviewid,String regid,String oldAdvHtmlFileProps,OnceViewContainer onecontaner){
//       JsonGenerator jsonGenerator=new JsonGenerator();
//       String chartJson=jsonGenerator.saveChartData(container,userId,oneviewid,regid,oldAdvHtmlFileProps,onecontaner);
//       return chartJson;
// }

    public String addNewCharts(Container container, String reportId, String userId, String[] viewBys, String[] rowViewNamesArr, String[] measBys, String[] rowMeasNamesArr, String chartId, String[] AggType, String fromoneview, ReportObjectMeta goMeta, OneViewLetDetails detail, String InnerViewbyEleId, String filePath, String goUpdate) {
        JsonGenerator jsonGenerator = new JsonGenerator();
        String chartJson = jsonGenerator.generatedataJson1(container, userId, reportId, viewBys, rowViewNamesArr, measBys, rowMeasNamesArr, chartId, AggType, fromoneview, goMeta, detail, InnerViewbyEleId, filePath, goUpdate);
        return chartJson;
    }

    public String generateFilter(Container container, List<String> viewBys, String reportName, String reportId, String bizzRoleName, String filePath) {
        JsonGenerator jsonGenerator = new JsonGenerator();
        String chartJson = jsonGenerator.generateFilter(container, viewBys, reportName, reportId, bizzRoleName, filePath);
        return null;
    }

    public String createoneviewobject(HttpServletRequest request, HttpServletResponse response, String oneviewid, String userId) throws Exception {
        JsonGenerator jsonGenerator = new JsonGenerator();
        OnceViewContainer onecontainer = null;
        HttpSession session = request.getSession(false);
        OneViewLetDetails detail = new OneViewLetDetails();
        ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();
        OneViewBD oneViewBD = new OneViewBD();
        HashMap map = null;
        Container container = null;
        //Gson gson = new Gson();
        FileReadWrite fileReadWrite = new FileReadWrite();
        String regId = request.getParameter("regId");
        String advHtmlFileProps = (String) request.getSession(false).getAttribute("advHtmlFileProps");
        String oldAdvHtmlFileProps = (String) request.getSession(false).getAttribute("oldAdvHtmlFileProps");
        String fileName = session.getAttribute("tempFileName").toString();
        PbReportViewerBD reportViewerBD = new PbReportViewerBD();
        FileInputStream fis = new FileInputStream(advHtmlFileProps + "/" + fileName);
        ObjectInputStream ois = new ObjectInputStream(fis);
        onecontainer = (OnceViewContainer) ois.readObject();
        ois.close();
        List<OneViewLetDetails> dashletDetails = onecontainer.onviewLetdetails;
        for (int i = 0; i < dashletDetails.size(); i++) {
            detail = dashletDetails.get(i);
            String reportId = detail.getRepId();
            String repname = detail.getRepName();
            String strURL = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
            strURL = strURL + "reportViewer.do?reportBy=viewReport&REPORTID=" + reportId + "&action=open";
            request.setAttribute("url", strURL);
            request.setAttribute("REPORTID", reportId);
//           String userId = String.valueOf(session.getAttribute("USERID"));
            if (onecontainer.getFilterBusinessRole() != null && !onecontainer.getFilterBusinessRole().equalsIgnoreCase("") && detail != null && detail.getRoleId() != null && !detail.getRoleId().equalsIgnoreCase("") && onecontainer.getFilterBusinessRole().equalsIgnoreCase(detail.getRoleId()) && !detail.isOneviewReportTimeDetails()) {
                if (onecontainer.getReportParameterValues() != null && !onecontainer.getReportParameterValues().isEmpty()) {
                    request.setAttribute("reportParameterVals", onecontainer.getReportParameterValues());
                }
            }
            reportViewerBD.prepareReport(reportId, userId, request, response,false);
            map = (HashMap) session.getAttribute("PROGENTABLES");
            String bizzRoleName = detail.getRolename();
            container = (Container) map.get(reportId);
            detail.setContainer(container);
            String filePath = "/usr/local/cache/" + bizzRoleName.replaceAll("\\W", "").trim() + "/" + repname.replaceAll("\\W", "").trim() + "_" + reportId + "/" + repname.replaceAll("\\W", "").trim() + "_" + reportId + "_data.json";
            File metafile = new File(filePath);
//                ArrayList viewBysList = new ArrayList();
            if (metafile.exists()) {
                String meta = fileReadWrite.loadJSON(filePath);
                XtendReportMeta reportMeta = new XtendReportMeta();
                Type metaType = new TypeToken<XtendReportMeta>() {
                }.getType();
                Type metaType1 = new TypeToken<ArrayList>() {
                }.getType();

                reportMeta = gson.fromJson(meta, metaType);
//        List<String>  viewBysList=reportMeta.getViewByIds();
                List<String> viewBysList1 = reportMeta.getViewbys();
                PbReportCollection collect = container.getReportCollect();
                ArrayList viewbyids = new ArrayList();
// for(int i1=0;i1<viewBysList.size();i1++){
//     viewbyids.add(viewBysList.get(i1));
// }
                ArrayList nameList = new ArrayList();
                for (int i1 = 0; i1 < viewBysList1.size(); i1++) {
                    nameList.add(viewBysList1.get(i1));
                }
                ArrayList nameList1 = (ArrayList) (container.getTableHashMap().get("MeasuresNames"));
                for (Object nameList11 : nameList1) {
                    nameList.add(nameList11);
                }

                QueryExecutor qryExec = new QueryExecutor();
                ProgenDataSet pbretObj = null;
                PbReportQuery reportQuery = null;
                reportQuery = qryExec.formulateQuery(collect, userId);
                ArrayList queryCols = new ArrayList();
                reportQuery.setRowViewbyCols(viewbyids);
//        reportQuery.setQryColumns(queryCols);
                reportQuery.setColViewbyCols(queryCols);
                String query = null;
                try {
                    query = reportQuery.generateViewByQry();
                } catch (Exception ex) {
                    logger.error("Exception:", ex);
                }
                pbretObj = qryExec.executeQuery(collect, query, false);
                FileReadWrite readWrite = new FileReadWrite();
//Gson gson = new Gson();
                File file;
                String pathoneview = oldAdvHtmlFileProps + "/" + repname.replaceAll("\\W", "").trim() + "_" + reportId;
                file = new File(pathoneview);
                String path = file.getAbsolutePath();
                String fName = path + File.separator + repname.replaceAll("\\W", "").trim() + "_" + reportId + "_data.json";
                File f = new File(path);
                File file1 = new File(fName);
                f.mkdirs();

                try {
                    file1.createNewFile();
                } catch (IOException ex) {
                    logger.error("Exception:", ex);
                }
                readWrite.writeToFile(filePath, gson.toJson(reportMeta));
//     viewBysList= gson.fromJson(reportMeta.getViewbys(), metaType1

                XtendAdapter adapter = new XtendAdapter();
                adapter.createCSVForoneview(pbretObj, reportId, repname, nameList, "null", bizzRoleName);

            }
        }
        return null;
    }

    public String saveChartProp(String reportId, String reportName, String userId, XtendReportMeta reportMeta, String data, String bizzRoleName, String type, String chartType, String key, String fileLocation, boolean publish, HttpSession session, String currentPage) {
//       XtendReportMeta reportMeta = new XtendReportMeta();
        reportMeta.setReportName(reportName);
        //Gson gson = new Gson();
        String visualChartType = gson.toJson(reportMeta.getVisualChartType());
//       reportMeta.setReportId(reportId);
//       reportMeta.setUserId(userId);
//       reportMeta.setChartData(chartData);
//       reportMeta.setNumOfRecords(numOfRecords);
//       reportMeta.setLines(lines);

        Type tarType = new TypeToken<XtendReportMeta>() {
        }.getType();
        XtendAdapter adapter = new XtendAdapter();
        adapter.saveChartMeta(reportName, reportId, gson.toJson(reportMeta), data, bizzRoleName, type, chartType, key, visualChartType, fileLocation, publish, session, currentPage);
//        fileReadWrite.writeToFile("G:/"+reportName+".json", gson.toJson(reportMeta));
//        fileReadWrite.writeToFile("G:/"+reportName+"_"+reportId+"/"+reportName+".json", gson.toJson(reportMeta));

//       String chartJson=jsonGenerator.generatedataJson1(container,userId,reportId,viewBys,rowViewNamesArr);
        return "";
    }

    public String saveChartPropforoneview(String reportId, String reportName, String userId, XtendReportMeta reportMeta, String data, String oneviewid, String regid, String chartType, String key) {
        JsonGenerator jsonGenerator = new JsonGenerator();
        FileReadWrite fileReadWrite = new FileReadWrite();
//       XtendReportMeta reportMeta = new XtendReportMeta();
        reportMeta.setReportName(reportName);
        //Gson gson = new Gson();
        String visualChartType = gson.toJson(reportMeta.getVisualChartType());
//       reportMeta.setReportId(reportId);
//       reportMeta.setUserId(userId);
//       reportMeta.setChartData(chartData);
//       reportMeta.setNumOfRecords(numOfRecords);
//       reportMeta.setLines(lines);

        Type tarType = new TypeToken<XtendReportMeta>() {
        }.getType();
        XtendAdapter adapter = new XtendAdapter();
        adapter.saveoneviewChartMeta(reportName, reportId, gson.toJson(reportMeta), data, oneviewid, regid, chartType, key, visualChartType);
//        fileReadWrite.writeToFile("G:/"+reportName+".json", gson.toJson(reportMeta));
//        fileReadWrite.writeToFile("G:/"+reportName+"_"+reportId+"/"+reportName+".json", gson.toJson(reportMeta));

//       String chartJson=jsonGenerator.generatedataJson1(container,userId,reportId,viewBys,rowViewNamesArr);
        return "";
    }
    //added by Nazneen

    public String getSearchRept(String userId, String val) {
//        String query = "Select A.Report_Id,A.Tag_Long_Desc FROM Prg_Tag_Report_Assignment A,PRG_AR_REPORT_MASTER B Where A.Report_Id = B.Report_Id And A.User_Id = "+userId+"";
//        String query = "Select REPORT_ID,TAG_LONG_DESC FROM Prg_Tag_Report_Assignment WHERE User_Id = "+userId+"";
//edited by manik
        String pVal = "";
        if (val.contains("9prg3")) {
            pVal = val.replaceAll("9prg3", "%");
        } else {
            pVal = "%" + val + "%";
        }
        String query = "Select  A.Report_Id,A.Tag_Long_Desc,B.Report_Name "
                + " From Prg_Tag_Report_Assignment A,Prg_Ar_Report_Master B ,prg_tag_master C,prg_ar_user_reports D"
                + "  Where A.Report_Id = B.Report_Id and A.Tag_Type = 'R' "
                + " and A.Report_Id = D.Report_Id "
                + " and A.user_id = D.user_id "
                + "and D.user_id='" + userId + "' and"
                + " (UPPER(B.Report_Name) like UPPER('%" + pVal + "%')) group by A.Report_Id,A.Tag_Long_Desc,B.Report_Name";

//        
        PbReturnObject returnObject = new PbReturnObject();
        Gson json = new Gson();
        List<String> tagReportId = new ArrayList<String>();
//       List<String>tagId=new ArrayList<String>();
//       List<String>tagName=new ArrayList<String>();
//       List<String>tagShortDesc=new ArrayList<String>();
        List<String> tagLongDesc = new ArrayList<String>();
        List<String> reportName = new ArrayList<String>();
        Map dimmap = new HashMap();
        String jsonString = null;
        try {
            returnObject = execSelectSQL(query);
            if (returnObject != null) {
                for (int i = 0; i < returnObject.getRowCount(); i++) {

                    tagReportId.add(returnObject.getFieldValueString(i, 0));
                    //added by Manik to improve search
//                  tagId.add(returnObject.getFieldValueString(i,1));
//                  tagName.add(returnObject.getFieldValueString(i,2));
//                  tagShortDesc.add(returnObject.getFieldValueString(i,3));
                    tagLongDesc.add(returnObject.getFieldValueString(i, 1));
                    reportName.add(returnObject.getFieldValueString(i, 2));
                }
                dimmap.put("tagReportId", tagReportId);
                //added by Manik to improve search
//            dimmap.put("tagId",tagId);
//            dimmap.put("tagName",tagName);
//            dimmap.put("tagShortDesc",tagShortDesc);
                dimmap.put("tagLongDesc", tagLongDesc);
                dimmap.put("reportName", reportName);
//                
//                
                jsonString = json.toJson(dimmap);
            }
        } catch (SQLException ex) {

            logger.error("Exception:", ex);
        }
        return jsonString;
    }

    public String getReportMaster() {
        String reportMap = "";
        XtendAdapter adapter = new XtendAdapter();
        reportMap = adapter.readReportMaster();
        return reportMap;
    }

    public String getReports(String reportId, String reportName, Container container, String fromoneview, String busrolename, String fileLocation, HttpSession session, HttpServletRequest request) {
        JsonGenerator jsonGenerator = new JsonGenerator();
        String report = jsonGenerator.gerReports(reportId, reportName, container, fromoneview, busrolename, fileLocation, session, request);
        return report;
    }

    public String geroneviewcharts(String reportId, String reportName, Container container, String oneviewid, String regid) {
        JsonGenerator jsonGenerator = new JsonGenerator();
        String report = jsonGenerator.geroneviewcharts(reportId, reportName, container, oneviewid, regid);
        return report;
    }
//sandeep
    public String getoneview(HttpServletRequest request, String reportId, String reportName, Container container, String fromoneview, String busrolename, String userId, OneViewLetDetails detail) {
        JsonGenerator jsonGenerator = new JsonGenerator();
        String report = jsonGenerator.geroneview(request, reportId, reportName, container, fromoneview, busrolename, userId, detail);
        return report;
    }

    public String savextchartoneview(HttpServletRequest request, String reportId, String reportName, Container container, String fromoneview, String busrolename, String userId, OneViewLetDetails detail) {
        HttpSession session = request.getSession(false);
//     reportId = request.getParameter("graphsId");
//     reportName = request.getParameter("graphName");
//    userId = request.getParameter("usersId");
//        String numOfRecords = request.getParameter("numOfCharts");


        container = Container.getContainerFromSession(request, reportId);

//        HashMap<String,List<Map<String, String>>> currChartData = container.getChartData();

        Type dataType = new TypeToken<HashMap<String, List<Map<String, String>>>>() {
        }.getType();

        Type tarType = new TypeToken<Map<String, String>>() {
        }.getType();

        Type tarType1 = new TypeToken<Map<String, DashboardChartData>>() {
        }.getType();
        //Gson gson = new Gson();
        String bizzRoleId = container.getReportCollect().reportBizRoles[0];
        String bizzRoleName = container.getReportCollect().getReportBizRoleName(bizzRoleId);
//            Map<String, DashboardChartData> chartData = gson.fromJson(request.getParameter("chartData"), tarType1);
//            Map<String, String> lines = gson.fromJson(request.getParameter("lines"), tarType);
        PbReportViewerDAO pbdao = new PbReportViewerDAO();
        FileReadWrite fileReadWrite = new FileReadWrite();
        String data = "";
        String type = "chart";
        String key = request.getParameter("currType");
        if (request.getParameter("type") != null && request.getParameter("type").equalsIgnoreCase("advance")) {
            data = gson.toJson(container.getDbrdData());
            type = "visual";
        } else if (request.getParameter("type") != null && request.getParameter("type").equalsIgnoreCase("trend")) {
            data = gson.toJson(container.getDbrdData());
            type = "trend";
        } else {
            data = gson.toJson(container.getDbrdData());
        }
        ReportManagementDAO reportDao = new ReportManagementDAO();
        JsonGenerator jsonGenerator = new JsonGenerator();
        request.setAttribute("savelocal", "savelocal");
        String report = jsonGenerator.geroneview(request, reportId, reportName, container, fromoneview, busrolename, userId, detail);
        XtendReportMeta reportMeta = reportDao.setOneviewForm(request);
        container.setReportMeta(reportMeta);
        String chartType = reportMeta.getChartType();
//       fileReadWrite.writeToFile("/usr/local/cache/Sales/"+reportName+"_"+reportId+"_data.json", gson.toJson(currChartData, dataType));
        saveChartPropforoneview(reportId, reportName, userId, reportMeta, data, detail.getOneviewId(), detail.getNoOfViewLets(), chartType, key);
        return null;

    }
//end by sandeep
    public String getReportsT(String reportId, String reportName, Container container, String fileLocation) {
        JsonGenerator jsonGenerator = new JsonGenerator();
        String report = jsonGenerator.getReportsT(reportId, reportName, container, fileLocation);
        return report;
    }

    public String getVisuals(String reportId, String reportName, Container container, String fileLocation) {
        JsonGenerator jsonGenerator = new JsonGenerator();
        String report = jsonGenerator.getVisuals(reportId, reportName, container, fileLocation);
        return report;
    }

    public String getVisualsChange(String reportId, String reportName, Container container, String visual, String filePath) {
        JsonGenerator jsonGenerator = new JsonGenerator();
        String report = jsonGenerator.getVisualsChange(reportId, reportName, container, visual, filePath);
        return report;
    }

    public String Visualsfiltermap(String reportId, String reportName, Container container, String fileLocation) {
        String report = "false";
        String bizzRoleId = container.getReportCollect().reportBizRoles[0];
        String bizzRoleName = container.getReportCollect().getReportBizRoleName(bizzRoleId);
        //Gson gson = new Gson();
        FileReadWrite fileReadWrite = new FileReadWrite();
        String key = "";
        String infoPath = fileLocation + "/" + bizzRoleName.replaceAll("\\W", "").trim() + "/" + reportName.replaceAll("\\W", "").trim() + "_" + reportId + "/" + reportName.replaceAll("\\W", "").trim() + "_" + reportId + "_visualData.json";
        try {
            File file1 = new File(infoPath);
            if (file1.exists()) {
                String dataInfo = fileReadWrite.loadJSON(infoPath);
                Type tarInfo = new TypeToken<Map<String, String>>() {
                }.getType();
                Map<String, String> infoMap;
                infoMap = gson.fromJson(dataInfo, tarInfo);
                Set keySet = infoMap.keySet();
                Iterator itr = keySet.iterator();
                int count = 0;
                while (count < 1 && itr.hasNext()) {
                    key = itr.next().toString();
                    count++;
                }
                String goPath = fileLocation + "/analyticalobject/R_GO_" + reportId + ".json";
                String filePath = fileLocation + "/" + bizzRoleName.replaceAll("\\W", "").trim() + "/" + reportName.replaceAll("\\W", "").trim() + "_" + reportId + "/" + reportName.replaceAll("\\W", "").trim() + "_" + reportId + key + ".json";
                String metaFilePath = fileLocation + "/" + bizzRoleName.replaceAll("\\W", "").trim() + "/" + reportName.replaceAll("\\W", "").trim() + "_" + reportId + "/" + reportName.replaceAll("\\W", "").trim() + "_" + reportId + "_" + key + ".json";
//        String datafilePath = "/usr/local/cache/Sales/"+reportName+"_"+reportId+"/"+reportName+"_"+reportId+".csv";

//     try{
                File file = new File(filePath);
                File metafile = new File(metaFilePath);
                if (file.exists() && metafile.exists()) {
                    String data = fileReadWrite.loadJSON(filePath);
                    String meta = fileReadWrite.loadJSON(metaFilePath);
                    String goData = fileReadWrite.loadJSON(goPath);
                    Map map = new HashMap();
                    map.put("meta", meta);
                    map.put("data", data);
                    report = gson.toJson(map);
                }

            }
        } catch (Exception e) {
            report = "false";
        }
        return report;
    }

    public String getLocalChart(String reportId, String reportName, String chartId, Container container, String type, String fileLocation,String userId, HttpServletRequest request) {
        JsonGenerator jsonGenerator = new JsonGenerator();
        String report = jsonGenerator.getLocalChart(reportId, reportName, chartId, container, type, fileLocation, userId, request);
        return report;
    }

    public String addNewTrend(Container container, String reportId, String userId, String[] viewBys, String[] rowViewNamesArr, String[] measBys, String[] rowMeasNamesArr, String chartId) {
        JsonGenerator jsonGenerator = new JsonGenerator();
        String chartJson = jsonGenerator.generateTrenddataJson1(container, userId, reportId, viewBys, rowViewNamesArr, measBys, rowMeasNamesArr, chartId);
        return chartJson;
    }

    public String saveTrendProp(String reportId, String reportName, String userId, Map<String, DashboardChartData> chartData, String numOfRecords, Map<String, String> lines, String data, String bizzRoleName) {
        JsonGenerator jsonGenerator = new JsonGenerator();
        FileReadWrite fileReadWrite = new FileReadWrite();
        XtendReportMeta reportMeta = new XtendReportMeta();
        reportMeta.setReportName(reportName);
//       reportMeta.setReportId(reportId);
//       reportMeta.setUserId(userId);
        reportMeta.setChartData(chartData);
//       reportMeta.setNumOfRecords(numOfRecords);
//       reportMeta.setLines(lines);
        //Gson gson = new Gson();
        Type tarType = new TypeToken<XtendReportMeta>() {
        }.getType();
        XtendAdapter adapter = new XtendAdapter();
        adapter.saveTrendMeta(reportName, reportId, gson.toJson(reportMeta), data, bizzRoleName);
//        fileReadWrite.writeToFile("G:/"+reportName+".json", gson.toJson(reportMeta));
//        fileReadWrite.writeToFile("G:/"+reportName+"_"+reportId+"/"+reportName+".json", gson.toJson(reportMeta));

//       String chartJson=jsonGenerator.generatedataJson1(container,userId,reportId,viewBys,rowViewNamesArr);
        return "";
    }

    public String getTrendReports(String reportId, String reportName, Container container) {
        JsonGenerator jsonGenerator = new JsonGenerator();
        String report = jsonGenerator.getTrendReports(reportId, reportName, container);
        return report;
    }

//added by Dinanath for drag and drop reportName
    public Map getSearchRept2(String userId) {
        String query = "Select distinct TAG_ID,TAG_NAME from PRG_TAG_MASTER where USER_ID=" + userId + " ";
        PbReturnObject returnObject = new PbReturnObject();
        Gson json = new Gson();
        List<String> tagReportId = new ArrayList<String>();
        List<String> tagReportName = new ArrayList<String>();
        Map dimmap = new HashMap();
        String jsonString = null;
        try {
            returnObject = execSelectSQL(query);
            if (returnObject != null) {
                for (int i = 0; i < returnObject.getRowCount(); i++) {
                    tagReportId.add(returnObject.getFieldValueString(i, 0));
                    tagReportName.add(returnObject.getFieldValueString(i, 1));
                }
                dimmap.put("tagReportId", tagReportId);
                dimmap.put("tagReportName", tagReportName);


            }
        } catch (SQLException ex) {

            logger.error("Exception:", ex);
        }
        return dimmap;
    }
//added by Dinanath for getting the id for drag and drop dialog for SaveReportTagDesc.jsp
    public void saveTagDescofAssign(String userId, String[] tagId, String shortDesc, String longDesc, String tagType, String reportId) {
        ArrayList delList = new ArrayList();
        delList.add("delete from PRG_TAG_REPORT_ASSIGNMENT where REPORT_ID=" + reportId + " and USER_ID=" + userId + "");
        execMultiple(delList);
        String qry1 = getResourceBundle().getString("inesrtTagDescOfAssign");

//         String qry;
        String finalqry;
//         qry = "insert into prg_tag_report_assignment(TAG_ID,TAG_SHORT_DESC,TAG_LONG_DESC,REPORT_ID,USER_ID,TAG_TYPE) values('&','&','&','&','&','&')";
        for (int i = 0; i < tagId.length; i++) {
            Object[] obj = new Object[6];
            obj[0] = tagId[i];
            obj[1] = shortDesc;
            obj[2] = longDesc;
            obj[3] = reportId;
            obj[4] = userId;
            obj[5] = tagType;
            finalqry = buildQuery(qry1, obj);
            try {
                execUpdateSQL(finalqry);
            } catch (Exception ex) {

                logger.error("Exception:", ex);
            }
        }//end for
        return;
    }
//added by Dinanath for getting the id for drag and drop dialog
    public Map getTagIdOfAssignmentTable(String userId, String reportId) {


        String query = "select distinct TAG_ID from PRG_TAG_REPORT_ASSIGNMENT where REPORT_ID = " + reportId + " and USER_ID=" + userId + " and TAG_TYPE='R'";
        PbReturnObject returnObject = new PbReturnObject();
        //Gson json = new Gson();
        List<String> tagId = new ArrayList<String>();

        Map dimmap = new HashMap();
        //String jsonString = null;
        try {
            returnObject = execSelectSQL(query);
            if (returnObject != null) {
                for (int i = 0; i < returnObject.getRowCount(); i++) {
                    tagId.add(returnObject.getFieldValueString(i, 0));

                }
                dimmap.put("tagIdOfAssignmentTab", tagId);
            }
        } catch (SQLException ex) {

            logger.error("Exception:", ex);
        }
        return dimmap;
    }
//added by Dinanath for getting the id for drag and drop dialog for SaveReportTagDesc.jsp
    public Map getShortNandLongNForReport(String userId, String reportId) {


        String query = "select TAG_SHORT_DESC,TAG_LONG_DESC from PRG_TAG_REPORT_ASSIGNMENT where REPORT_ID = " + reportId + " and USER_ID=" + userId + " ";
        PbReturnObject returnObject = new PbReturnObject();
        //Gson json = new Gson();
        List<String> shortN = new ArrayList<String>();
        List<String> longN = new ArrayList<String>();

        Map dimmap = new HashMap();
        //String jsonString = null;
        try {
            returnObject = execSelectSQL(query);
            if (returnObject != null) {
                for (int i = 0; i < returnObject.getRowCount(); i++) {
                    shortN.add(returnObject.getFieldValueString(i, 0));
                    longN.add(returnObject.getFieldValueString(i, 1));

                }
                dimmap.put("tagShortNForReport", shortN);
                dimmap.put("tagLongNForReport", longN);
            }
        } catch (SQLException ex) {

            logger.error("Exception:", ex);
        }
        return dimmap;
    }
//addded by Dinanath for droppable dialog

    public String buildChangeViewBy2(ArrayList<Object> tagId, ArrayList<Object> tagByNames, String dropTar, String ctxPath) {
        StringBuffer graphBuffer = new StringBuffer("");
        if (tagId != null && tagByNames != null) {
            for (int i = 0; i < tagId.size(); i++) {
                graphBuffer.append(" <li id='ViewBy" + tagId.get(i) + "' style='width:auto;height:auto;color:white' >");
                graphBuffer.append("<table id='viewTab" + tagId.get(i) + "'>");
                graphBuffer.append(" <tr>");
                graphBuffer.append(" <td >");
                graphBuffer.append("<img src=\"" + ctxPath + "/icons pinvoke/cross-small.png\" style=\"cursor:pointer\" onclick=\"javascript:deleteColumn('ViewBy" + tagId.get(i) + "','" + dropTar + "','" + tagByNames.get(i) + "')\" />");
                graphBuffer.append("</td>");
                graphBuffer.append("<td style=\"color:black\">" + tagByNames.get(i) + "</td>");
                graphBuffer.append("</tr>");
                graphBuffer.append("</table>");
                graphBuffer.append("</li>");
            }
        }
        return graphBuffer.toString();
    }
//ended by Dinanath
    //added by Dinanath for First sequence

    public void updateTagAccordingToSequence(String tagIdforUpd, String tagSequenceIdForUpd, String userId) throws java.lang.Exception {

        String sql = getResourceBundle().getString("updateTagAccordingToSequenceId");
        Object[] obj;
        String tid[] = tagIdforUpd.split(",");
        String sid[] = tagSequenceIdForUpd.split(",");
         CacheLayer cacheLayer = CacheLayer.getCacheInstance();// ByPrabal
         PbReturnObject tagObj;
        //String tagi[]=tagId.split(",");
        for (int i = 0; i < tid.length; i++) {
            obj = new Object[3];
            String finalQuery = "";
            obj[0] = sid[i];
            obj[1] = tid[i];
            obj[2] = userId;
            finalQuery = buildQuery(sql, obj);
            try {
                execUpdateSQL(finalQuery);
            } catch (SQLException ex) {
                logger.error("Exception:", ex);
            }
        }
        try{
            String tagQuery = "select distinct TAG_ID,TAG_NAME,TAG_DESC,FONT_SIZE,SEQUENCE_ID from PRG_TAG_MASTER  where user_id = " + userId + " order by SEQUENCE_ID";
                tagObj = new PbDb().execSelectSQL(tagQuery);
                cacheLayer.setdata("getDataCall"+ userId, tagObj);
         } catch (SQLException ex) {
                logger.error("Exception:", ex);
            }
        return;

    }
    //added by Dinanath for Second sequence

    public void updateTagAccordingToSequenceSecond(String tagIdforUpd, String tagSequenceIdForUpd, String userId,String tagId) throws java.lang.Exception {

        String sql = getResourceBundle().getString("updateTagAccordingToSequenceIdSecond");
        Object[] obj;
        String tAssignid[] = tagIdforUpd.split(",");
        String seqid[] = tagSequenceIdForUpd.split(",");
        //String tagi[]=tagId.split(",");
        CacheLayer cacheLayer = CacheLayer.getCacheInstance();// ByPrabal  
        for (int i = 0; i < tAssignid.length; i++) {
            obj = new Object[3];
            String finalQuery = "";
            obj[0] = seqid[i];
            obj[1] = tAssignid[i];
            obj[2] = userId;
            finalQuery = buildQuery(sql, obj);
            try {
                execUpdateSQL(finalQuery);
            } catch (SQLException ex) {

                logger.error("Exception:", ex);
            }
        }
                cacheLayer.setdata("getTagsBlocks" + userId + tagId, null);
           
        return;

    }
    //added by dinanath for Tagging.jsp

    public void updateTagNames(String oldName, String newName, String oldDesc, String newDesc) throws java.lang.Exception {
        try {
            String sql = getResourceBundle().getString("updateTagNames");
            Object[] obj;


            obj = new Object[3];
            String finalQuery = "";
            obj[0] = newName;
            obj[1] = newDesc;
            obj[2] = oldName;

            finalQuery = buildQuery(sql, obj);
            execUpdateSQL(finalQuery);

        } catch (SQLException ex) {

            logger.error("Exception:", ex);
        }
        return;
    }
    //added by Dinanath for Tagging.jsp

    public void createTagNames(String tagName, String tagDesc) {
        String qry1 = getResourceBundle().getString("inesrtTagName");
        String finalqry;
        Object[] obj = new Object[2];
        obj[0] = tagName;
        obj[1] = tagDesc;
        finalqry = buildQuery(qry1, obj);
        //
        try {
            execUpdateSQL(finalqry);
        } catch (Exception ex) {

            logger.error("Exception:", ex);
        }

        return;
    }
    //added by Dinanath for tag report for Tagging.jsp

    public void saveTagNameWithParticularReports(String tagId, String tagShortDesc, String tagLongDesc, String reportIdforTag, String userId, String tagType) {
        String qry1 = getResourceBundle().getString("saveTagNameWithPReports");
        String finalqry;
        String tagIds[] = tagId.split(",");
        for (int i = 1; i < tagIds.length; i++) {
//                
            Object[] obj = new Object[6];
            obj[0] = tagIds[i];
            obj[1] = tagShortDesc;
            obj[2] = tagLongDesc;
            obj[3] = reportIdforTag;
            obj[4] = userId;
            obj[5] = tagType;

            finalqry = buildQuery(qry1, obj);
            //
            try {
                execUpdateSQL(finalqry);
            } catch (Exception ex) {

                logger.error("Exception:", ex);
            }
        }//end for
        return;
    }
    //added by Dinanath for Tagging.jsp

    public void updateShortNameAndDescription(String tagId, String shortN, String longN) throws java.lang.Exception {

        String sql = getResourceBundle().getString("updateShortNameAndDesc");
        Object[] obj;
        String sn[] = shortN.split(",");
        String ln[] = longN.split(",");
        String tagi[] = tagId.split(",");
        for (int i = 1; i < tagi.length; i++) {
            obj = new Object[3];
            String finalQuery = "";
            obj[0] = sn[i];
            obj[1] = ln[i];
            obj[2] = tagi[i];

            finalQuery = buildQuery(sql, obj);
            try {
                execUpdateSQL(finalQuery);

            } catch (SQLException ex) {
                logger.error("Exception:", ex);
            }
        }
        return;

    }
//added by Dinanath for storing data in session for Tagging.jsp
    public Map getTagMasterData(String userId) {
        String query = "Select  TAG_ID,TAG_NAME,TAG_DESC from PRG_TAG_MASTER";
//        
        PbReturnObject returnObject = new PbReturnObject();
//        Gson json = new Gson();
        List<String> tagReportId = new ArrayList<String>();
        List<String> tagReportName = new ArrayList<String>();
        List<String> tagDescription = new ArrayList<String>();
        Map dimmap = new HashMap();
//       String jsonString = null;
        try {
            returnObject = execSelectSQL(query);
            if (returnObject != null) {
                for (int i = 0; i < returnObject.getRowCount(); i++) {
                    tagReportId.add(returnObject.getFieldValueString(i, 0));
                    tagReportName.add(returnObject.getFieldValueString(i, 1));
                    tagDescription.add(returnObject.getFieldValueString(i, 2));
                }
                dimmap.put("tggId2", tagReportId);
                dimmap.put("tggName2", tagReportName);
                dimmap.put("tggDescription2", tagDescription);


            }
        } catch (SQLException ex) {

            logger.error("Exception:", ex);
        }
        return dimmap;
    }
//added by Dinanath for storing data in session for Tagging.jsp

    public Map getTagMasterDataSecond(String userId) {
        String query = "Select  TAG_ID,TAG_NAME,TAG_DESC from PRG_TAG_MASTER";
//        
        PbReturnObject returnObject = new PbReturnObject();
//        Gson json = new Gson();
        List<String> tagReportId = new ArrayList<String>();
        List<String> tagReportName = new ArrayList<String>();
        List<String> tagDescription = new ArrayList<String>();
        Map dimmap = new HashMap();
//       String jsonString = null;
        try {
            returnObject = execSelectSQL(query);
            if (returnObject != null) {
                for (int i = 0; i < returnObject.getRowCount(); i++) {
                    tagReportId.add(returnObject.getFieldValueString(i, 0));
                    tagReportName.add(returnObject.getFieldValueString(i, 1));
                    tagDescription.add(returnObject.getFieldValueString(i, 2));
                }
                dimmap.put("tggId", tagReportId);
                dimmap.put("tggName", tagReportName);
                dimmap.put("tggDescription", tagDescription);


            }
        } catch (SQLException ex) {

            logger.error("Exception:", ex);
        }
        return dimmap;
    }
    //added by Dinanath for storing data in session for Tagging.jsp

    public Map getReportTagData(String userId) {
        String query = " select tag_id, report_id, tag_short_desc, tag_long_desc from prg_tag_report_assignment where user_id=" + userId + "";
//        
        PbReturnObject returnObject = new PbReturnObject();
//        Gson json = new Gson();
        List<String> tagId = new ArrayList<String>();
        List<String> reportId = new ArrayList<String>();
        List<String> tagShortDescription = new ArrayList<String>();
        List<String> tagLongDescription = new ArrayList<String>();
        Map dimmap = new HashMap();
//       String jsonString = null;
        try {
            returnObject = execSelectSQL(query);
            if (returnObject != null) {
                for (int i = 0; i < returnObject.getRowCount(); i++) {
                    tagId.add(returnObject.getFieldValueString(i, 0));
                    reportId.add(returnObject.getFieldValueString(i, 1));
                    tagShortDescription.add(returnObject.getFieldValueString(i, 2));
                    tagLongDescription.add(returnObject.getFieldValueString(i, 3));
                }
                dimmap.put("tgId", tagId);
                dimmap.put("tgReportId", reportId);
                dimmap.put("tgSDescription", tagShortDescription);
                dimmap.put("tgLDescription", tagLongDescription);


            }
        } catch (SQLException ex) {

            logger.error("Exception:", ex);
        }
        return dimmap;
    }

    public Map getTagIdOfAssignmentTable2(String userId, ArrayList<Object> reportid) {
        String query = "select distinct TAG_ID from PRG_TAG_REPORT_ASSIGNMENT";

        PbReturnObject returnObject = new PbReturnObject();
        List<String> tagId = new ArrayList<String>();
        Map dimmap = new HashMap();
        try {
            returnObject = execSelectSQL(query);
            if (returnObject != null) {
                for (int i = 0; i < returnObject.getRowCount(); i++) {
                    tagId.add(returnObject.getFieldValueString(i, 0));
                }
                dimmap.put("tagIdOfAssignmentTab2", tagId);
            }
        } catch (SQLException ex) {

            logger.error("Exception:", ex);
        }
        return dimmap;
    }
//add
    //added by Dinanath Parit for save the records of shortDesc and Long Desc of drag and drop dialog

    public void saveTagDescofAssignSecond(String uId, String[] tagId, String shortDesc, String longDesc, String tagType, String[] reportId) {
//        ArrayList delList = new ArrayList();
//        delList.add("delete from PRG_TAG_REPORT_ASSIGNMENT where TAG_TYPE='" + tagType + "'");
//        execMultiple(delList);
        String qry1 = getResourceBundle().getString("inesrtTagDescOfAssignForAllReport");
        String finalqry;
//         qry = "insert into prg_tag_report_assignment(TAG_ID,TAG_SHORT_DESC,TAG_LONG_DESC,REPORT_ID,USER_ID,TAG_TYPE) values('&','&','&','&','&','&')";
        for (int i = 0; i < tagId.length; i++) {
            Object[] obj = new Object[6];
            obj[0] = tagId[i];
            obj[1] = shortDesc;
            obj[2] = longDesc;
            obj[3] = reportId[i];
            obj[4] = uId;
            obj[5] = tagType;
            finalqry = buildQuery(qry1, obj);
            logger.info("finalqry" + finalqry);
            try {
                execUpdateSQL(finalqry);
            } catch (Exception ex) {

                logger.error("Exception:", ex);
            }
        }//end for
        return;
    }
    //added by Dinanath for change the font of Tag Header in landingPage.jsp

    public void updateFontSizeChangeOfTags(String fontsize, String userId) throws java.lang.Exception {

        String sql = getResourceBundle().getString("updateFontSizeofTag");
        Object[] obj;
        String fontSizeofTag = fontsize;
        obj = new Object[2];
        String finalQuery = "";
        obj[0] = fontSizeofTag;
        obj[1] = userId;
        finalQuery = buildQuery(sql, obj);
        try {
            execUpdateSQL(finalQuery);

        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        return;

    }

public void saveGraphJson(String newReportId, String newReportName,String oldReportId, String oldReportName, String bizzRoleName,String fileLocation,String userId,String bizzRoleId) {
     JsonGenerator jsonGenerator=new JsonGenerator();
     jsonGenerator.saveGOJson(newReportId,newReportName,oldReportId,oldReportName, bizzRoleName,"goTable",fileLocation);
     jsonGenerator.savefilterJson(newReportId,newReportName,oldReportId,oldReportName, bizzRoleName,"filter",fileLocation);
     jsonGenerator.saveGraphJson(newReportId,newReportName,oldReportId,oldReportName, bizzRoleName,"graph",fileLocation);
     jsonGenerator.saveGraphSavePointFile(newReportId,oldReportId,"savepoint",fileLocation,userId,bizzRoleId);
    }

    public String updateSequence(String sequence, String roleName, String reportId, String reportName) {
        JsonGenerator jsonGenerator = new JsonGenerator();
        return jsonGenerator.updateSequence(sequence, roleName, reportId, reportName);
    }
//    added by manik
    public String getReportsLogin(String reportId, String reportName, String bizzRoleName, String fromLogin, String username, HttpSession session, String currentTab, String graphId, String chartId) {
        JsonGenerator jsonGenerator = new JsonGenerator();
        String report = jsonGenerator.gerReportsLogin(reportId, reportName, bizzRoleName, fromLogin, username, session, currentTab, graphId, chartId);
        return report;
    }

    public String getWallReports(String username, HttpSession session) {
        JsonGenerator jsonGenerator = new JsonGenerator();
        String report = jsonGenerator.getWallReports(username, session);
        return report;
    }

    public String saveChartforLogin(String username, String data) {
        XtendAdapter adapter = new XtendAdapter();
        String report = adapter.saveLoginChartMeta(username, data);
        return report;
//        adapter.saveLoginChartMeta(gson.toJson(LayoutDetails),usernameLog);
//        fileReadWrite.writeToFile("G:/"+reportName+".json", gson.toJson(reportMeta));
//        fileReadWrite.writeToFile("G:/"+reportName+"_"+reportId+"/"+reportName+".json", gson.toJson(reportMeta));

//       String chartJson=jsonGenerator.generatedataJson1(container,userId,reportId,viewBys,rowViewNamesArr);
//       return "";
    }

// added by krishan
//      public String getGlobalParametersPiReporttab(){
//        PbReturnObject returnObject = new PbReturnObject();
//        String setUpCharVal = "";
//        String reportTitleSize="";
//        String reportTitleAlign="";
//        String colorOnGrp="";
//        String piTheme="";
//        PreparedStatement pstmtForTransfer;
//        PbDb pbdb = new PbDb();
//        String reportTab="";
//        String qery1 = "SELECT * FROM PRG_GBL_SETUP_VALUES WHERE SETUP_KEY = 'GLOBAL_PARAMS'";
//           
//            ResultSet resultSet = null;
//        try {
//            pstmtForTransfer = ProgenConnection.getInstance().getConnection().prepareStatement("select * from  PRG_GBL_SETUP_VALUES where SETUP_ID="+3);
//            resultSet = pstmtForTransfer.executeQuery();
//            if(resultSet.next())
//            {
//
//                setUpCharVal=resultSet.getString(3);
//            }
//            returnObject = super.execSelectSQL(qery1);
//          }
//         catch (SQLException ex) {
//                logger.error("Exception:",ex);
//            }
//            if (returnObject.getRowCount() > 0) {
//                setUpCharVal = returnObject.getFieldValueString(0, 2);
//            }
//            if(setUpCharVal!=null && !setUpCharVal.equalsIgnoreCase("") && !setUpCharVal.equalsIgnoreCase("null")){
//                String arrSetUpCharVal[] = setUpCharVal.split(";") ;
//                    for(int i=0;i<arrSetUpCharVal.length;i++){
//                        String temp = arrSetUpCharVal[i];
//                        if(temp.contains("reportTitleSize")){
//                            reportTitleSize = temp.substring(temp.indexOf("~")+1);
//                        } else if(temp.contains("reportTitleAlign")){
//                            reportTitleAlign = temp.substring(temp.indexOf("~")+1);
//                        } else if(temp.contains("colorOnGrp")){
//                            colorOnGrp = temp.substring(temp.indexOf("~")+1);
//                        } else if(temp.contains("piTheme")){
//                            piTheme = temp.substring(temp.indexOf("~")+1);
//                        }else if(temp.contains("reportTab")){
//                            reportTab=temp.substring(temp.indexOf("~")+1);
//                        }
//                    }
//            }return  reportTab;
//    }
    public String GTKPICalculateFunction(HttpServletRequest request, String repId, String[] measId, String[] measName, String[] aggType, String chartId, String timeClause, String dimSecClause, Container container) {
        JsonGenerator jsonGenerator = new JsonGenerator();
        String action=request.getParameter("action");
        String result = null;
        try {
            if(action!=null && action.equalsIgnoreCase("template")){
                result = jsonGenerator.GTKPIForTemplate(request, repId, container);
            }
            else{
            result = jsonGenerator.GTKPICalculateFunction(request, repId, measId, measName, aggType, chartId, timeClause, dimSecClause, container);
                
            }
        } catch (SQLException ex) {
            result = "";
            logger.error("Exception:", ex);
        }
        return result;
    }

    public String buildDragableMeasure1(String reportId, String dropTar, String ctxPath, Container container, String chartId, String CheckViewbyflag) {
        PbReportCollection collect = container.getReportCollect();
        String folderName = collect.getReportBizRoleName(collect.reportBizRoles[0]);
        List<String> measureIdsList = new ArrayList<String>();
        XtendReportMeta reportMeta = new XtendReportMeta();
        reportMeta = container.getReportMeta();
        StringBuffer graphBuffer = new StringBuffer("");
        if (reportMeta != null && reportMeta.getChartData() != null) {
//                    for(String chart : charts){
            DashboardChartData chartDetails = reportMeta.getChartData().get(chartId);

            if (chartDetails != null && chartDetails.getmeasures1() != null) {
                for (int m = 0; m < chartDetails.getmeasures1().size(); m++) {
                    for (int k = 0; k < chartDetails.getMeassures().size(); k++) {
                        if (chartDetails.getMeassures().get(k).toString().equalsIgnoreCase(chartDetails.getmeasures1().get(m).toString())) {
                            measureIdsList.add(chartDetails.getMeassureIds().get(k));
                        }
                    }
                    try {

                        graphBuffer.append(" <li id='MeasBy" + measureIdsList.get(m) + "' style='width:auto;height:auto;color:white' >");
                        graphBuffer.append("<table id='viewTab" + measureIdsList.get(m) + "'>");
                        graphBuffer.append(" <tr>");
                        graphBuffer.append(" <td >");
                        chartDetails.getMeassureIds();
                        graphBuffer.append("<img src=\"" + ctxPath + "/icons pinvoke/cross-small.png\" style=\"cursor:pointer\" onclick=\"javascript:deleteColumn('MeasBy" + measureIdsList.get(m) + "','" + dropTar + "','" + chartDetails.getmeasures1().get(m) + "')\" />");
                        graphBuffer.append("</td>");
                        graphBuffer.append("<td style=\"color:black\">" + chartDetails.getmeasures1().get(m) + "</td>");
                        graphBuffer.append("</tr>");
                        graphBuffer.append("</table>");
                        graphBuffer.append("</li>");
                    } catch (Exception ex) {
                    }
                }
            }
        }
        return graphBuffer.toString();
    }

    public String buildDragableMeasure2(String reportId, String dropTar, String ctxPath, Container container, String chartId, String CheckViewbyflag) {
        PbReportCollection collect = container.getReportCollect();
        String folderName = collect.getReportBizRoleName(collect.reportBizRoles[0]);
        List<String> measureIdsList = new ArrayList<String>();
        XtendReportMeta reportMeta = new XtendReportMeta();
        reportMeta = container.getReportMeta();
        StringBuffer graphBuffer = new StringBuffer("");
        if (reportMeta != null && reportMeta.getChartData() != null) {
//                    for(String chart : charts){
            DashboardChartData chartDetails = reportMeta.getChartData().get(chartId);
            if (chartDetails != null && chartDetails.getmeasures2() != null) {
                for (int m = 0; m < chartDetails.getmeasures2().size(); m++) {
                    for (int k = 0; k < chartDetails.getMeassures().size(); k++) {
                        if (chartDetails.getMeassures().get(k).toString().equalsIgnoreCase(chartDetails.getmeasures2().get(m).toString())) {
                            measureIdsList.add(chartDetails.getMeassureIds().get(k));
                        }
                    }
                    graphBuffer.append(" <li id='MeasBy" + measureIdsList.get(m) + "' style='width:auto;height:auto;color:white' >");
                    graphBuffer.append("<table id='viewTab" + measureIdsList.get(m) + "'>");
                    graphBuffer.append(" <tr>");
                    graphBuffer.append(" <td >");
                    graphBuffer.append("<img src=\"" + ctxPath + "/icons pinvoke/cross-small.png\" style=\"cursor:pointer\" onclick=\"javascript:deleteColumn('MeasBy" + measureIdsList.get(m) + "','" + dropTar + "','" + chartDetails.getmeasures2().get(m) + "')\" />");
                    graphBuffer.append("</td>");
                    graphBuffer.append("<td style=\"color:black\">" + chartDetails.getmeasures2().get(m) + "</td>");
                    graphBuffer.append("</tr>");
                    graphBuffer.append("</table>");
                    graphBuffer.append("</li>");
                }
            }
        }
        return graphBuffer.toString();
    }
    //added by shobhit for multi dashboard on 22/09/15

    public String buildParentViewByForMultiDB(String reportId, String dropTar, String ctxPath, Container container, String chartId, String CheckViewbyflag) {
        PbReportCollection collect = container.getReportCollect();
        String folderName = collect.getReportBizRoleName(collect.reportBizRoles[0]);

        XtendReportMeta reportMeta = new XtendReportMeta();
        reportMeta = container.getReportMeta();
        if (reportMeta.getChartData().get("chart1").getIsDashboardDefined() == null || (!reportMeta.getChartData().get("chart1").getIsDashboardDefined().equals("Y"))) {
            return "";
        }
        StringBuffer graphBuffer = new StringBuffer("");
        String parentViewById = reportMeta.getParentViewBy();
        List<String> viewBys = reportMeta.getViewbys();
        List<String> viewByIds = reportMeta.getViewbysIds();
        if (!parentViewById.equals("")) {
            int i = 0;
            for (i = 0; i < viewByIds.size(); i++) {
                if (viewByIds.get(i).equals(parentViewById)) {
                    break;
                }
            }
            String parentViewBy = viewBys.get(i);
            if (!parentViewBy.equals("")) {
//            if (reportMeta != null && reportMeta.getChartData() != null) {
//                Map<String, DashboardChartData> chartData = new LinkedHashMap<String, DashboardChartData>(reportMeta.getChartData());
//                List<String> charts = new ArrayList(chartData.keySet());
//                for (String chart : charts) {
//                    DashboardChartData chartDetails = reportMeta.getChartData().get(chart);
//    //                        if(chartDetails!=null && chartDetails.getViewIds() !=null){
//                    for (int m = 0; m < chartDetails.getViewIds().size(); m++) {
                graphBuffer.append(" <li id='ViewBy" + parentViewById + "' style='width:auto;height:auto;color:white' >");
                graphBuffer.append("<table id='viewTab" + parentViewBy + "'>");
                graphBuffer.append(" <tr>");
                graphBuffer.append(" <td >");
                graphBuffer.append("<img src=\"" + ctxPath + "/icons pinvoke/cross-small.png\" style=\"cursor:pointer\" onclick=\"javascript:deleteColumn('ViewBy" + parentViewById + "','" + dropTar + "','" + parentViewBy + "')\" />");
                graphBuffer.append("</td>");
                graphBuffer.append("<td style=\"color:black\">" + parentViewBy + "</td>");
                graphBuffer.append("</tr>");
                graphBuffer.append("</table>");
                graphBuffer.append("</li>");
            }
        }
//                    }
//                }
//            }
        return graphBuffer.toString();
    }

    public String buildChildViewByForMultiDB(String reportId, String dropTar, String ctxPath, Container container, String chartId, String CheckViewbyflag) {
        PbReportCollection collect = container.getReportCollect();
        String folderName = collect.getReportBizRoleName(collect.reportBizRoles[0]);

        XtendReportMeta reportMeta = new XtendReportMeta();
        reportMeta = container.getReportMeta();
        if (reportMeta.getChartData().get("chart1").getIsDashboardDefined() == null || (!reportMeta.getChartData().get("chart1").getIsDashboardDefined().equals("Y"))) {
            return "";
        }
        StringBuffer graphBuffer = new StringBuffer("");
        List<String> childViewByIds = reportMeta.getChildViewBys();
//            if (reportMeta != null && reportMeta.getChartData() != null) {
//                Map<String, DashboardChartData> chartData = new LinkedHashMap<String, DashboardChartData>(reportMeta.getChartData());
//                List<String> charts = new ArrayList(chartData.keySet());
//                for (String chart : charts) {
//                    DashboardChartData chartDetails = reportMeta.getChartData().get(chart);
        //                        if(chartDetails!=null && chartDetails.getViewIds() !=null){
        List<String> viewBys = reportMeta.getViewbys();
        List<String> viewByIds = reportMeta.getViewbysIds();
        if (!childViewByIds.isEmpty() && childViewByIds.size() != 0) {
            for (int m = 0; m < childViewByIds.size(); m++) {
                int i = 0;
                for (i = 0; i < viewByIds.size(); i++) {
                    if (viewByIds.get(i).equals(childViewByIds.get(m))) {
                        break;
                    }
                }

                String parentViewBy = viewBys.get(i);
                graphBuffer.append(" <li id='MeasBy" + childViewByIds.get(m) + "' style='width:auto;height:auto;color:white' >");
                graphBuffer.append("<table id='viewTab" + childViewByIds.get(m) + "'>");
                graphBuffer.append(" <tr>");
                graphBuffer.append(" <td >");
                graphBuffer.append("<img src=\"" + ctxPath + "/icons pinvoke/cross-small.png\" style=\"cursor:pointer\" onclick=\"javascript:deleteColumn('MeasBy" + childViewByIds.get(m) + "','" + dropTar + "','" + childViewByIds.get(m) + "')\" />");
                graphBuffer.append("</td>");
                graphBuffer.append("<td style=\"color:black\">" + parentViewBy + "</td>");
                graphBuffer.append("</tr>");
                graphBuffer.append("</table>");
                graphBuffer.append("</li>");
            }
        }
//                }
//            }
        return graphBuffer.toString();
    }

    public String buildMeasBysForMultiDB(String reportId, String dropTar, String ctxPath, Container container, String chartId, String CheckViewbyflag) {
        PbReportCollection collect = container.getReportCollect();
        String folderName = collect.getReportBizRoleName(collect.reportBizRoles[0]);

        XtendReportMeta reportMeta = new XtendReportMeta();
        reportMeta = container.getReportMeta();
        if (reportMeta.getChartData().get("chart1").getIsDashboardDefined() == null || (!reportMeta.getChartData().get("chart1").getIsDashboardDefined().equals("Y"))) {
            return "";
        }
        StringBuffer graphBuffer = new StringBuffer("");
        List<String> childMeasByIds = reportMeta.getChildMeasBys();
//            if (reportMeta != null && reportMeta.getChartData() != null) {
//                Map<String, DashboardChartData> chartData = new LinkedHashMap<String, DashboardChartData>(reportMeta.getChartData());
//                List<String> charts = new ArrayList(chartData.keySet());
//                for (String chart : charts) {
//                    DashboardChartData chartDetails = reportMeta.getChartData().get(chart);
        //                        if(chartDetails!=null && chartDetails.getViewIds() !=null){
        List<String> measBys = reportMeta.getMeasures();
        List<String> measByIds = reportMeta.getMeasuresIds();
        if (!childMeasByIds.isEmpty() && childMeasByIds.size() != 0) {
            for (int m = 0; m < childMeasByIds.size(); m++) {
                int i = 0;
                for (i = 0; i < measByIds.size(); i++) {
                    if (measByIds.get(i).equals(childMeasByIds.get(m))) {
                        break;
                    }
                }

                String childMeasBy = measBys.get(i);
                graphBuffer.append(" <li id='MeasBy" + childMeasByIds.get(m) + "' style='width:auto;height:auto;color:white' >");
                graphBuffer.append("<table id='viewTab" + childMeasByIds.get(m) + "'>");
                graphBuffer.append(" <tr>");
                graphBuffer.append(" <td >");
                graphBuffer.append("<img src=\"" + ctxPath + "/icons pinvoke/cross-small.png\" style=\"cursor:pointer\" onclick=\"javascript:deleteColumn('MeasBy" + childMeasByIds.get(m) + "','" + dropTar + "','" + childMeasByIds.get(m) + "')\" />");
                graphBuffer.append("</td>");
                graphBuffer.append("<td style=\"color:black\">" + childMeasBy + "</td>");
                graphBuffer.append("</tr>");
                graphBuffer.append("</table>");
                graphBuffer.append("</li>");
            }
        }
//                }
//            }
        return graphBuffer.toString();
    }
    //end

    public PbReturnObject getColDisplayLabel(int user_id) {
        String query = "SELECT USER_ID,COMPANY_ID,ELEMENT_ID,DISP_LABEL,ACTUAL_FORMULA,DEFAULT_COMPANY from prg_usr_col_disp_label WHERE USER_ID=" + user_id + "";
        PbReturnObject returnObj = new PbReturnObject();

        try {
            returnObj = execSelectSQL(query);
        } catch (SQLException ex) {

            logger.error("Exception:", ex);
        }
        return returnObj;
    }

    public PbReturnObject getColDisplayCompany(int user_id) {
        String query = "SELECT DISTINCT COMPANY_ID from prg_usr_col_disp_label WHERE USER_ID=" + user_id + "";
        PbReturnObject returnObj = new PbReturnObject();

        try {
            returnObj = execSelectSQL(query);
        } catch (SQLException ex) {

            logger.error("Exception:", ex);
        }
        return returnObj;
    }

    public String buildDragableRuntimeMeasure1(String reportId, String dropTar, String ctxPath, Container container, String chartId, String CheckViewbyflag) {
        PbReportCollection collect = container.getReportCollect();
        String folderName = collect.getReportBizRoleName(collect.reportBizRoles[0]);
        List<String> measureIdsList = new ArrayList<String>();
        XtendReportMeta reportMeta = new XtendReportMeta();
        reportMeta = container.getReportMeta();
        StringBuffer graphBuffer = new StringBuffer("");
        if (reportMeta != null && reportMeta.getChartData() != null) {
//                    for(String chart : charts){
            DashboardChartData chartDetails = reportMeta.getChartData().get(chartId);

            if (chartDetails != null && chartDetails.getmeasures1() != null) {
                for (int m = 0; m < chartDetails.getmeasures1().size(); m++) {
                    measureIdsList.add(chartDetails.getmeasures1().get(m));
                    graphBuffer.append(" <li id='MeasBy" + measureIdsList.get(m) + "' style='width:auto;height:auto;color:white' >");
                    graphBuffer.append("<table id='viewTab" + measureIdsList.get(m) + "'>");
                    graphBuffer.append(" <tr>");
                    graphBuffer.append(" <td >");
                    chartDetails.getMeassureIds();
                    graphBuffer.append("<img src=\"" + ctxPath + "/icons pinvoke/cross-small.png\" style=\"cursor:pointer\" onclick=\"javascript:deleteColumn('MeasBy" + measureIdsList.get(m) + "','" + dropTar + "','" + chartDetails.getmeasures1().get(m) + "')\" />");
                    graphBuffer.append("</td>");
                    graphBuffer.append("<td style=\"color:black\">" + chartDetails.getmeasures1().get(m) + "</td>");
                    graphBuffer.append("</tr>");
                    graphBuffer.append("</table>");
                    graphBuffer.append("</li>");
                }
            }
        }
        return graphBuffer.toString();
    }

    public String buildDragableRuntimeMeasure2(String reportId, String dropTar, String ctxPath, Container container, String chartId, String CheckViewbyflag) {
        PbReportCollection collect = container.getReportCollect();
        String folderName = collect.getReportBizRoleName(collect.reportBizRoles[0]);
        List<String> measureIdsList = new ArrayList<String>();
        XtendReportMeta reportMeta = new XtendReportMeta();
        reportMeta = container.getReportMeta();
        StringBuffer graphBuffer = new StringBuffer("");
        if (reportMeta != null && reportMeta.getChartData() != null) {
//                    for(String chart : charts){
            DashboardChartData chartDetails = reportMeta.getChartData().get(chartId);
            if (chartDetails != null && chartDetails.getmeasures2() != null) {
                for (int m = 0; m < chartDetails.getmeasures2().size(); m++) {
                    measureIdsList.add(chartDetails.getmeasures2().get(m));
                    graphBuffer.append(" <li id='MeasBy" + measureIdsList.get(m) + "' style='width:auto;height:auto;color:white' >");
                    graphBuffer.append("<table id='viewTab" + measureIdsList.get(m) + "'>");
                    graphBuffer.append(" <tr>");
                    graphBuffer.append(" <td >");
                    graphBuffer.append("<img src=\"" + ctxPath + "/icons pinvoke/cross-small.png\" style=\"cursor:pointer\" onclick=\"javascript:deleteColumn('MeasBy" + measureIdsList.get(m) + "','" + dropTar + "','" + chartDetails.getmeasures2().get(m) + "')\" />");
                    graphBuffer.append("</td>");
                    graphBuffer.append("<td style=\"color:black\">" + chartDetails.getmeasures2().get(m) + "</td>");
                    graphBuffer.append("</tr>");
                    graphBuffer.append("</table>");
                    graphBuffer.append("</li>");
                }
            }
        }
        return graphBuffer.toString();
    }
    //added by krishan pratap

    public void setGlobalIcon(Container container) {
        String showiconreport = "none";
        String showicongraph = "none";
        String showicontrends = "none";
        String showiconadvancevisual = "none";
        String showall = "";
        boolean showicons = false;

        String showallcheck = "";
        String PbReportId = container.getReportId();
        PbReturnObject returnObject = null;
        String setUpCharVal = "";
        String defaulttab = "";
        String headervalue = "";
        String isDraggable = "";

        try {
            String qery1 = "SELECT SHOW_ICONS,SHOW_All,DEFAULT_TAB,SHOW_HEADER,isDraggable FROM PRG_AR_REPORT_MASTER WHERE Report_ID =" + PbReportId;
            returnObject = execSelectSQL(qery1);
            if (returnObject != null && returnObject.getRowCount() > 0) {
                setUpCharVal = returnObject.getFieldValueString(0, 0);
                showallcheck = returnObject.getFieldValueString(0, 1);
                defaulttab = returnObject.getFieldValueString(0, 2);
                headervalue = returnObject.getFieldValueString(0, 3);
                isDraggable = returnObject.getFieldValueString(0, 4);
                if (showallcheck.equalsIgnoreCase("showall")) {
                    showall = "block";

                } else if (showallcheck.equalsIgnoreCase("Notshowall")) {
                    showall = "none";
                }
                if (!(showallcheck == "false" || showallcheck == "" || showallcheck == null)) {
                    if (setUpCharVal != null && !setUpCharVal.equalsIgnoreCase("") && !setUpCharVal.equalsIgnoreCase("null")) {
                        String arrSetUpCharVal[] = setUpCharVal.split(",");
                        for (int i = 0; i < arrSetUpCharVal.length; i++) {
                            String temp = arrSetUpCharVal[i];
                            if (temp.contains("report")) {
                                showiconreport = "block";
                            } else if (temp.contains("graph")) {
                                showicongraph = "block";
                            } else if (temp.contains("trends")) {
                                showicontrends = "block";
                            } else if (temp.contains("visual")) {
                                showiconadvancevisual = "block";
                            } else if (temp.contains("icons")) {
                                showicons = true;
                            }
                        }
                        if (defaulttab.equalsIgnoreCase("Report")) {
                            showiconreport = "block";
                        } else if (defaulttab.equalsIgnoreCase("Graph")) {
                            showicongraph = "block";
                        } else if (defaulttab.equalsIgnoreCase("Trends")) {
                            showicontrends = "block";
                        } else if (defaulttab.equalsIgnoreCase("AdvanceVisuals")) {
                            showiconadvancevisual = "block";
                        }


                    }
                }
                if (headervalue != "" && headervalue != null) {
                    container.report_Headrvalue = headervalue;
                }
                if (isDraggable != "" && isDraggable != null) {
                    container.chart_isDraggable = isDraggable;
                }
                container.showall = showall;
                container.showicons = showicons;
                container.showiconreport = showiconreport;
                container.showicongraph = showicongraph;
                container.showicontrends = showicontrends;
                container.showiconadvancevisual = showiconadvancevisual;
            }

        }
        catch (SQLException ex) {
            logger.error("Exception:", ex);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
    }

    public String getAOId() throws Exception {
        String CustomReportId = "";
        int aoId;
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
            aoId = getSequenceNumber("select IDENT_CURRENT('PRG_AR_AO_MASTER') NEXTVAL ");
            aoId++;
            CustomReportId = String.valueOf(aoId);

        } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
            aoId = getSequenceNumber("SELECT  AUTO_INCREMENT\n" +
"FROM    information_schema.TABLES\n" +                                                         //added by mohit
"WHERE   (TABLE_NAME = 'PRG_AR_AO_MASTER');");
//            aoId = aoId + 1;                    
            CustomReportId = String.valueOf(aoId);
        } else {
            CustomReportId = String.valueOf(getSequenceNumber("select PRG_AR_AO_MASTER_SEQ.nextval from dual"));
        }

        return CustomReportId;
    }

    public String getAODetailsId() throws Exception {
        String CustomReportId = "";
        int reportId;
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
            reportId = getSequenceNumber("select IDENT_CURRENT('PRG_AR_AO_DETAILS') NEXTVAL ");
            reportId++;
            CustomReportId = String.valueOf(reportId);

        } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
            reportId = getSequenceNumber("SELECT  AUTO_INCREMENT\n" +
"FROM    information_schema.TABLES\n" +
"WHERE   (TABLE_NAME = 'PRG_AR_AO_DETAILS');");                          //added by mohit
//            reportId = reportId + 1;
            CustomReportId = String.valueOf(reportId);
        } else {
            CustomReportId = String.valueOf(getSequenceNumber("select PRG_AR_AO_DETAILS_SEQ.nextval from dual"));
        }

        return CustomReportId;
    }
    //Added By Amar on feb 13,2015

    public PbReturnObject getAllSchedulerAfetrEtldetails() {
        //String qry=getResourceBundle().getString("getAllSchedulerDetails");
        String qry = getResourceBundle().getString("getAllSchedulerAfrETLDetails");
        PbReturnObject retObj = new PbReturnObject();
        try {
            retObj = execSelectSQL(qry);
        } catch (Exception e) {
            logger.error("Exception:", e);
        }
        return retObj;

    }

    public PbReturnObject getAllSchedulerdetailsWithoutETL() {
        String qry = getResourceBundle().getString("getAllSchedulerDetailsNew");
        PbReturnObject retObj = new PbReturnObject();
        try {
            retObj = execSelectSQL(qry);
        } catch (Exception e) {
            logger.error("Exception:", e);
        }
        return retObj;

    }
    public PbReturnObject getAllAOSchedulerdetails() {
        String qry = getResourceBundle().getString("getAllAOSchedulerDetails");
        PbReturnObject retObj = new PbReturnObject();
        try {
            retObj = execSelectSQL(qry);
        } catch (Exception e) {
            logger.error("Exception:", e);
        }
        return retObj;

    }
    //added by Dinanath

    public PbReturnObject getAllSelectedSchedules(String schedulerIdsArr) {
        PbReturnObject retObj = new PbReturnObject();
        if (schedulerIdsArr.substring(schedulerIdsArr.length() - 1).equalsIgnoreCase(",")) {
            schedulerIdsArr = schedulerIdsArr.substring(0, schedulerIdsArr.length() - 1);
        }
        String finalqry = "Select pr.*,pu.pu_login_id as USERNAME,Pm.Table_Name as REPORTNAME From Prg_Ar_Personalized_Reports pr,Prg_Ar_Users Pu,prg_ar_report_table_master pm Where Pr.Scheduler_Details Is Not Null And PRG_PERSONALIZED_ID IN (&) And Prg_User_Id =pu.pu_id and pm.report_id=pr.prg_report_id";
        try {
            Object obj[] = new Object[1];
            obj[0] = schedulerIdsArr;
            String q = buildQuery(finalqry, obj);
            retObj = execSelectSQL(q);
        }
        catch (SQLException ex) {
            logger.error("Exception:", ex);
        }  catch (Exception ex) {
            logger.error("Exception:", ex);
        }       return retObj;
        }
    // end of code
    //added by Dinanath for getting system cpu info

    public String callingThisAtEveryXminute(int minute) {

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            public void run() {
                Date date = new Date();
                System.out.println(date);
                int cpuLoadFinal = 0;
                try {
                    Calendar currentDate = Calendar.getInstance();
                    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS");
                    String dateNow = formatter.format(currentDate.getTime());
                    logger.info("callingEvery5minute : " + dateNow);
//            System.out.println("Before Lib");
//            System.out.println("java:lib:path = " + System.getProperty("java.library.path"));
//            System.out.println("after Lib");

                    logger.info("**************************************");
                    logger.info("*** Informations about the Memory: ***");
                    logger.info("**************************************");

                    Sigar sigar = new Sigar();
                    Mem mem = null;
                    CpuPerc cpuPerc = null;
                    mem = sigar.getMem();
                    cpuPerc = sigar.getCpuPerc();

                    Object insertObj[] = new Object[12];
                    logger.info("Actual total free system memory: " + mem.getActualFree() / 1024 / 1024 + " MB");
                    insertObj[0] = mem.getActualFree() / 1024 / 1024 + " MB";
                    logger.info("Actual total used system memory: " + mem.getActualUsed() / 1024 / 1024 + " MB");
                    insertObj[1] = mem.getActualUsed() / 1024 / 1024 + " MB";
                    logger.info("Total free system memory ......: " + mem.getFree() / 1024 / 1024 + " MB");
                    insertObj[2] = mem.getFree() / 1024 / 1024 + " MB";
                    logger.info("System Random Access Memory....: " + mem.getRam() + " MB");
                    insertObj[3] = mem.getRam() + " MB";
                    logger.info("Total system memory............: " + mem.getTotal() / 1024 / 1024 + " MB");
                    insertObj[4] = mem.getTotal() / 1024 / 1024 + " MB";
                    logger.info("Total used system memory.......: " + mem.getUsed() / 1024 / 1024 + " MB");
                    insertObj[5] = mem.getUsed() / 1024 / 1024 + " MB";
                    //puPerc cpuPerc = cpu.getCpuPerc();
                    logger.info("Cpu percentage usage combined: " + cpuPerc.getCombined() * 100);
                    insertObj[6] = cpuPerc.getCombined() * 100;
                    logger.info("Cpu percentage usage system: " + cpuPerc.getSys() * 100);
                    insertObj[7] = cpuPerc.getSys() * 100;
                    logger.info("Cpu percentage usage user: " + cpuPerc.getUser() * 100);
                    insertObj[8] = cpuPerc.getUser() * 100;
                    logger.info("Free CPU percentage : " + cpuPerc.getIdle() * 100);
                    insertObj[9] = cpuPerc.getIdle() * 100;
                    cpuLoadFinal = (int) (cpuPerc.getCombined() * 100);
                    System.out.println("Cpu Load Final: " + cpuLoadFinal);
                    insertObj[10] = cpuLoadFinal;
                    logger.info("**************************************");
                    insertObj[11] = dateNow;
//            String insertQuery = getResourceBundle().getString("insertQueryofSystemMemoryInfo");
//                    String insertQuery = "insert into prg_system_memory_info(ID,Actual_total_free_sys_memory,Actual_total_used_sys_memory,Total_free_sys_memory,sys_memory,total_sys_memory,total_used_sys_memory,"
//                    + "cpu_perce_usage_combined,cpu_perce_usage_sys,cpu_perce_usage_user,free_cpu_percetange,cpu_load_final,current_system_time"
//                    + ")values(memory_id.nextval,'&','&','&','&','&','&','&','&','&','&','&','&')";

//            insertQuery = buildQuery(insertQuery, insertObj);
//
//                    int result = execUpdateSQL(insertQuery);



                } catch (java.lang.UnsatisfiedLinkError usfdle) {
                    logger.error("Exception:", usfdle);
                } catch (SigarException se) {
                    logger.error("Exception:", se);
                } catch (Exception ex) {
                    logger.error("Exception:", ex);
                }
            }
        }, 0, minute * 60 * 1000);

        return "success";
    }
//added by Dinanath for kpi chart scheduler
    public KPIAlertSchedule getKPIChartSchedulerDetails(String schedulerId) {
        String qry = "select scheduler_details,SCHEDULE_START_DATE,SCHEDULE_END_DATE from prg_ar_personalized_kpi where prg_personalized_id=&";
        Object[] obj = new Object[1];
        obj[0] = schedulerId;
        String finalqry = buildQuery(qry, obj);
        PbReturnObject retobj = new PbReturnObject();
        KPIAlertSchedule schedule = null;
        try {
            retobj = execSelectSQL(finalqry);
            if (retobj != null && retobj.getRowCount() != 0) {
                Gson json = new Gson();
                java.lang.reflect.Type type = new TypeToken<List<KPIAlertSchedule>>() {
                }.getType();
                List<KPIAlertSchedule> scheduleList = null;
                if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                    scheduleList = json.fromJson(retobj.getFieldUnknown(0, 0), type);
                } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.ORACLE)) {
                    scheduleList = json.fromJson(retobj.getFieldValueString(0, "SCHEDULER_DETAILS"), type);
                } else {
                    scheduleList = json.fromJson(retobj.getFieldValueString(0, "scheduler_details"), type);
}
                Date sdate = retobj.getFieldValueDate(0, "SCHEDULE_START_DATE");
                Date edate = retobj.getFieldValueDate(0, "SCHEDULE_END_DATE");
                if (scheduleList != null && !scheduleList.isEmpty()) {
                    for (KPIAlertSchedule schedule1 : scheduleList) {
                        schedule = schedule1;
                        schedule.setStartDate(sdate);
                        schedule.setEndDate(edate);
                    }
                }
            }
        } catch (Exception ex) {

            logger.error("Exception:", ex);
        }
        if (schedule != null) {
            return schedule;
        } else {
            return null;
        }
    }
    //added by Dinanath Parit
        public PbReturnObject getDailyKPIChartScheduler() {
        String qry = "select * from Prg_Ar_Personalized_kpi where scheduler_details is not null";
        PbReturnObject retObj = new PbReturnObject();
        try {
            retObj = execSelectSQL(qry);
        } catch (Exception e) {
            logger.error("Exception:", e);
}
        return retObj;

    }
}
