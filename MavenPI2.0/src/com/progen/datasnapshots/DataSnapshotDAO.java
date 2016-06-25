/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.datasnapshots;

import com.progen.db.ProgenDataSet;
import com.progen.report.PbReportCollection;
import com.progen.report.query.DataSetHelper;
import com.progen.report.query.PbReportQuery;
import java.io.*;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.ResourceBundle;
import javax.servlet.http.HttpServletRequest;
import net.sourceforge.jtds.jdbc.ClobImpl;
import oracle.jdbc.OraclePreparedStatement;
import oracle.sql.CLOB;
import org.apache.log4j.Logger;
import prg.db.Container;
import prg.db.PbDb;
import prg.db.PbReturnObject;
import utils.db.ProgenConnection;

/**
 *
 * @author Administrator
 */
public class DataSnapshotDAO extends PbDb {

    ResourceBundle resourceBundle;
    public static Logger logger = Logger.getLogger(DataSnapshotDAO.class);

    private ResourceBundle getResourceBundle() {
        if (this.resourceBundle == null) {
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                resourceBundle = new DataSnapshotResourceBundleSqlServer();
            } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                resourceBundle = new DataSnapshotResourceBundleMysql();
            } else {

                resourceBundle = new DataSnapshotResourceBundle();

            }
        }
        return resourceBundle;

    }

    public void delSnapShots(String deletesnapshotids) {


        //String sqlQuery = resBundle.getString("getAllreps");
        String sqlQuery = getResourceBundle().getString("deleteSnapShots");
        Object obj[] = new Object[1];
        obj[0] = deletesnapshotids;
        String finalQuery;
        finalQuery = buildQuery(sqlQuery, obj);
        try {
            execUpdateSQL(finalQuery);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }
    }

    public int insertDataSnapshot(DataSnapshot snapShot, int userId, String htmlStatus) {
        Object[] obj = null;
        String finalQuery = null;
        String selectQuery = getResourceBundle().getString("getDataSnapShotId");
        String insertQuery = getResourceBundle().getString("insertDataSnapShots");

        int snapShotId = -1;
        try {
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                obj = new Object[7];
                obj[0] = snapShot.getSnapShotName();
                obj[1] = snapShot.getReportId();
                obj[2] = snapShot.getRefreshInterval();
                obj[3] = userId;
                obj[4] = userId;
                obj[5] = "not_generated";
                obj[6] = htmlStatus;
                finalQuery = super.buildQuery(insertQuery, obj);
                snapShotId = super.insertAndGetSequenceInSQLSERVER(finalQuery, "PRG_AR_DATA_SNAPSHOTS");


            } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                obj = new Object[7];
                obj[0] = snapShot.getSnapShotName();
                obj[1] = snapShot.getReportId();
                obj[2] = snapShot.getRefreshInterval();
                obj[3] = userId;
                obj[4] = userId;
                obj[5] = "not_generated";
                obj[6] = htmlStatus;
                finalQuery = super.buildQuery(insertQuery, obj);
                snapShotId = super.insertAndGetSequenceInMySql(finalQuery, "PRG_AR_DATA_SNAPSHOTS", "DATA_SNAPSHOT_ID");


            } else {


                snapShotId = super.getSequenceNumber(selectQuery);
//            

                obj = new Object[8];
                obj[0] = snapShotId;
                obj[1] = snapShot.getSnapShotName();
                obj[2] = snapShot.getReportId();
                obj[3] = snapShot.getRefreshInterval();
                obj[4] = userId;
                obj[5] = userId;
                obj[6] = "not_generated";
                obj[7] = htmlStatus;
                finalQuery = super.buildQuery(insertQuery, obj);
                super.execModifySQL(finalQuery);

            }
        } catch (Exception ex) {
            snapShotId = -1;
//            ProgenLog.log(ProgenLog.SEVERE, this, "insertDataSnapshot", "insertDataSnapshot Failed " + ex.getMessage());
            logger.error("insertDataSnapshot Failed ", ex);
        }

        return snapShotId;
    }

    public Date getCurrentDate() {
        Date currDate = null;
        String finalQuery = getResourceBundle().getString("getSysdate");
        PbDb pbDb = new PbDb();
        PbReturnObject retObj = new PbReturnObject();
        try {
            retObj = pbDb.execSelectSQL(finalQuery);
            currDate = retObj.getFieldValueTimestamp(0, "SYSDATE");

        } catch (SQLException ex) {
            logger.error("Exception: ", ex);
        }
        return currDate;

    }

    public Clob updateSnapshotHtmlView(int snapShotId, Reader reader, String fromOption) {
        ClobImpl clob = null;
        String clob1 = null;
        String htmlViewQry = "";
        String htmlViewQuery = "";
        if (fromOption.equalsIgnoreCase("fromHtml")) {
            htmlViewQry = getResourceBundle().getString("updateHtmlView1");
        } else {
            htmlViewQry = getResourceBundle().getString("updateHeadlineView1");
        }
        String finalQuery;
        PbDb pbDb = new PbDb();
        Object[] obj = new Object[1];
        obj[0] = snapShotId;
        finalQuery = pbDb.buildQuery(htmlViewQry, obj);
        if (fromOption.equalsIgnoreCase("fromHtml")) {
            htmlViewQuery = getResourceBundle().getString("getHtmlViewClob");
        } else {
            htmlViewQuery = getResourceBundle().getString("getHeadlineClob");
        }

        htmlViewQuery = super.buildQuery(htmlViewQuery, new Object[]{snapShotId});
        Connection conn = null;
        OraclePreparedStatement opstmt = null;
        PreparedStatement sqlstmt = null;
        CLOB clobLoc = null;
        try {
            pbDb.execModifySQL(finalQuery);

        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }


        try {
            PbReturnObject retObj = super.executeSelectSQL(htmlViewQuery);
            char[] cbuf;
            int offset = 0;
            int len = 5196;
            int toRead = -1;
            Writer writer = null;
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.ORACLE)) {
                {
                    if (retObj.getRowCount() > 0) {
                        if (fromOption.equalsIgnoreCase("fromHtml")) {
                            clobLoc = (CLOB) retObj.getFieldValueOracleClob(0, "DATA_HTML_VIEW");
                        } else {
                            clobLoc = (CLOB) retObj.getFieldValueOracleClob(0, "HEADLINE_DATA");
                        }
                        //clobLoc = CLOB.empty_lob();
                        clobLoc.truncate(clobLoc.length());
                        writer = clobLoc.setCharacterStream(1);
                    }
                }
            } else {
                if (retObj.getRowCount() > 0) {
                    if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {

                        if (fromOption.equalsIgnoreCase("fromHtml")) {
                            clob1 = retObj.getFieldUnknown(0, 0);
                        } else {
                            clob1 = retObj.getFieldUnknown(0, 0);
                        }
                        writer = new FileWriter(clob1);
                    } else {
                        if (fromOption.equalsIgnoreCase("fromHtml")) {
                            clob = (ClobImpl) retObj.getFieldValueClob(0, "DATA_HTML_VIEW");
                        } else {
                            clob = (ClobImpl) retObj.getFieldValueClob(0, "HEADLINE_DATA");
                        }
                        writer = clob.setCharacterStream(1);
                    }
                }
            }

            do {
                cbuf = new char[len];
                toRead = reader.read(cbuf, offset, len);
                writer.write(cbuf);
                writer.flush();
                if (toRead == -1) {
                    break;
                }
            } while (true);


            if (writer != null) {
                writer.close();
            }
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.ORACLE)) {

                if (clobLoc != null) {
                    conn = ProgenConnection.getInstance().getConnection();
                    String updateQuery = "";
                    if (fromOption.equalsIgnoreCase("fromHtml")) {
                        updateQuery = getResourceBundle().getString("updateHtmlView");
                    } else {
                        updateQuery = getResourceBundle().getString("updateHeadlineView");
                    }
                    opstmt = (OraclePreparedStatement) conn.prepareStatement(updateQuery);
                    opstmt.setCLOB(1, clobLoc);
                    opstmt.setInt(2, snapShotId);
                    opstmt.executeUpdate();
                    conn.commit();
                    opstmt.close();
                    opstmt = null;
                    conn.close();
                    conn = null;
                }
            } else {
                if (clob != null) {

                    conn = ProgenConnection.getInstance().getConnection();
                    String updateQuery = "";
                    if (fromOption.equalsIgnoreCase("fromHtml")) {
                        updateQuery = getResourceBundle().getString("updateHtmlView");
                    } else {
                        updateQuery = getResourceBundle().getString("updateHeadlineView");
                    }
                    sqlstmt = (PreparedStatement) conn.prepareStatement(updateQuery);
                    sqlstmt.setClob(1, clob);
                    sqlstmt.setInt(2, snapShotId);
                    sqlstmt.executeUpdate();
                    //conn.commit();
                    sqlstmt.close();
                    sqlstmt = null;
                    conn.close();
                    conn = null;
                }

                if (clob1 != null) {

                    conn = ProgenConnection.getInstance().getConnection();
                    String updateQuery = "";
                    if (fromOption.equalsIgnoreCase("fromHtml")) {
                        updateQuery = getResourceBundle().getString("updateHtmlView");
                    } else {
                        updateQuery = getResourceBundle().getString("updateHeadlineView");
                    }
                    sqlstmt = (PreparedStatement) conn.prepareStatement(updateQuery);
                    sqlstmt.setString(1, clob1);
//                    sqlstmt.setClob(1, clob);
                    sqlstmt.setInt(2, snapShotId);
                    sqlstmt.executeUpdate();
                    //conn.commit();
                    sqlstmt.close();
                    sqlstmt = null;
                    conn.close();
                    conn = null;
                }
            }

        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        } finally {
            try {
                if (opstmt != null) {
                    opstmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
            }
        }

        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
            return clob;
        } else {
            return clobLoc;
        }
    }

    public String updateSnapShotHtmlViewString(int snapShotId, Reader reader, String fromOption) {

        String clob1 = null;
        String htmlViewQry = "";
        String htmlViewQuery = "";
        if (fromOption.equalsIgnoreCase("fromHtml")) {
            htmlViewQry = getResourceBundle().getString("updateHtmlView1");
        } else {
            htmlViewQry = getResourceBundle().getString("updateHeadlineView1");
        }
        String finalQuery;
        PbDb pbDb = new PbDb();
        Object[] obj = new Object[1];
        obj[0] = snapShotId;
        finalQuery = pbDb.buildQuery(htmlViewQry, obj);
        if (fromOption.equalsIgnoreCase("fromHtml")) {
            htmlViewQuery = getResourceBundle().getString("getHtmlViewClob");
        } else {
            htmlViewQuery = getResourceBundle().getString("getHeadlineClob");
        }

        htmlViewQuery = super.buildQuery(htmlViewQuery, new Object[]{snapShotId});
        Connection conn = null;
        OraclePreparedStatement opstmt = null;
        PreparedStatement sqlstmt = null;
        CLOB clobLoc = null;
        try {
            pbDb.execModifySQL(finalQuery);

        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }


        try {
            PbReturnObject retObj = super.executeSelectSQL(htmlViewQuery);
            char[] cbuf;
            int offset = 0;
            int len = 5196;
            int toRead = -1;
            Writer writer = null;
            if (retObj.getRowCount() > 0) {
                if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                    if (fromOption.equalsIgnoreCase("fromHtml")) {
                        clob1 = retObj.getFieldUnknown(0, 0);
                    } else {
                        clob1 = retObj.getFieldUnknown(0, 0);
                    }
                }
                writer = new FileWriter(clob1);
            }

            do {
                cbuf = new char[len];
                toRead = reader.read(cbuf, offset, len);
                writer.write(cbuf);
                writer.flush();
                if (toRead == -1) {
                    break;
                }
            } while (true);
            if (writer != null) {
                writer.close();
            }
            if (clob1 != null) {
                conn = ProgenConnection.getInstance().getConnection();
                String updateQuery = "";
                if (fromOption.equalsIgnoreCase("fromHtml")) {
                    updateQuery = getResourceBundle().getString("updateHtmlView");
                } else {
                    updateQuery = getResourceBundle().getString("updateHeadlineView");
                }
                sqlstmt = (PreparedStatement) conn.prepareStatement(updateQuery);
                sqlstmt.setString(1, clob1);
//                    sqlstmt.setClob(1, clob);
                sqlstmt.setInt(2, snapShotId);
                sqlstmt.executeUpdate();
                //conn.commit();
                sqlstmt.close();
                sqlstmt = null;
                conn.close();
                conn = null;
            }


        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        } finally {
            try {
                if (opstmt != null) {
                    opstmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
            }
        }
        return clob1;
    }

    public DataSnapshot openDataSnapshot(int snapShotId) {
        DataSnapshot snapShot = new DataSnapshot();
        String openQuery = getResourceBundle().getString("openDataSnapshotHtml");
        PbReturnObject snapShotResultSet = new PbReturnObject();
        PbDb pbDb = new PbDb();
        Object[] bind = new Object[1];
        bind[0] = snapShotId;
        String finalQuery = pbDb.buildQuery(openQuery, bind);

        try {
//             ProgenLog.log(ProgenLog.FINE, this, "openDataSnapshotDao getting xmlString", "Exit");
            logger.info("getting xmlString Exit");
            snapShotResultSet = pbDb.execSelectSQL(finalQuery);
//             ProgenLog.log(ProgenLog.FINE, this, "openDataSnapshotDao", "Exit");
            logger.info("Exit");
            if (snapShotResultSet.getRowCount() > 0) {
                snapShot.setSnapShotId(snapShotResultSet.getFieldValueInt(0, "DATA_SNAPSHOT_ID"));
                snapShot.setSnapShotName(snapShotResultSet.getFieldValueString(0, "DATA_SNAPSHOT_NAME"));
                snapShot.setReportId(snapShotResultSet.getFieldValueString(0, "REPORT_ID"));
                snapShot.setRefreshDate(snapShotResultSet.getFieldValueTimestamp(0, "LAST_REFRESHED"));
                snapShot.setRefreshInterval(snapShotResultSet.getFieldValueInt(0, "REFRESH_INTERVAL"));
                if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                    snapShot.setHtmlViewString(snapShotResultSet.getFieldUnknown(0, 6));
                } else {
                    snapShot.setHtmlView((Clob) snapShotResultSet.getFieldValueClob(0, "DATA_HTML_VIEW"));
                }
            }

        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }

        return snapShot;

    }

    public String getHeaderTitleForSnapshot() {
        String finalQuery = getResourceBundle().getString("getHeaderTitle");
        String headerTitle = null;
        try {
            PbReturnObject retObj = execSelectSQL(finalQuery);
            if (retObj.getRowCount() != 0) {
                headerTitle = retObj.getFieldValueString(0, 0);
            } else {
                headerTitle = "Progen Business Solutions";
            }

        } catch (SQLException ex) {
        }
        return headerTitle;
    }

    public String getAdvancedHtmlReturnObjectFile(Container container, String userId, String advHtmlFileProps) {
        PbReportCollection collect = container.getReportCollect();
        String advancedHtml = "";
        String reportId = collect.reportId;
        ArrayList<String> colViewbys = new ArrayList<String>();
        ArrayList<String> rowViewbys = collect.reportParamIds;
        ArrayList qryCols = container.getTableMeasure();
        ArrayList<String> qryAggrs = new ArrayList<String>();
        ArrayList timeDetailsArray = collect.timeDetailsArray;
        AdvancedHtmlData advancedHtmlData = new AdvancedHtmlData();
        ArrayList<String> columnIds = new ArrayList<String>();
        ArrayList columnNames = new ArrayList();
        ArrayList<String> dimensionIds = new ArrayList<String>();
        ArrayList dimensionNames = new ArrayList();
        ArrayList<String> measureIds = new ArrayList<String>();
        ArrayList measureNames = new ArrayList();


        Map dataHelper = DataSetHelper.getAggregationMap(qryCols);
        for (int i = 0; i < qryCols.size(); i++) {
            String aggType = dataHelper.get(qryCols.get(i)).toString();
            qryAggrs.add(aggType);

        }

        ProgenDataSet retObj = new PbReturnObject();
        PbReportQuery repQuery = new PbReportQuery();
        repQuery.setColViewbyCols(colViewbys);
        repQuery.setRowViewbyCols(rowViewbys);
        repQuery.setParamValue(collect.reportParametersValues);
        repQuery.setQryColumns(qryCols);
        repQuery.setColAggration(qryAggrs);
        repQuery.setTimeDetails(timeDetailsArray);
        retObj = repQuery.getPbReturnObject((String) qryCols.get(0));
        advancedHtmlData.setReturnObject(retObj);
        for (int k = 0; k < rowViewbys.size(); k++) {
            columnIds.add("A_" + rowViewbys.get(k));
            columnNames.add(collect.reportParamNames.get(k));
            dimensionIds.add("A_" + rowViewbys.get(k));
            dimensionNames.add(collect.reportParamNames.get(k));
        }
        for (int l = 0; l < container.getTableMeasure().size(); l++) {
            columnIds.add("A_" + container.getTableMeasure().get(l));
            columnNames.add(container.getTableMeasureNames().get(l));
            measureIds.add("A_" + container.getTableMeasure().get(l));
            measureNames.add(container.getTableMeasureNames().get(l));
        }
        advancedHtmlData.setColumnIds(columnIds);
        advancedHtmlData.setColumnNames(columnNames);
        advancedHtmlData.setDimensionIds(dimensionIds);
        advancedHtmlData.setDimensionNames(dimensionNames);
        advancedHtmlData.setMeasureIds(measureIds);
        advancedHtmlData.setMeasureNames(measureNames);
        advancedHtml = "ADV_HTML_" + reportId + "_" + System.currentTimeMillis();
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(advHtmlFileProps + "/" + advancedHtml + ".txt");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(advancedHtmlData);
            oos.flush();
            oos.close();
//            FileInputStream fis = new FileInputStream("/home/progen/returnobject.txt");
//            ObjectInputStream ois = new ObjectInputStream(fis);
//            retObj=null;
//            retObj=     (PbReturnObject) ois.readObject();
//            ois.close();
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }
        return advancedHtml;
        //            FileOutputStream fos = new FileOutputStream("/home/progen/returnobject.txt");
//            ObjectOutputStream oos = new ObjectOutputStream(fos);
//            oos.writeObject(retObj);
//            ProgenLog.log(ProgenLog.FINE, this, "openDataSnapshot writing retObj to file ", "Exit");
//            oos.flush();
//            oos.close();
//            FileInputStream fis = new FileInputStream("/home/progen/returnobject.txt");
//            ObjectInputStream ois = new ObjectInputStream(fis);
//            retObj=null;
//            retObj=     (PbReturnObject) ois.readObject();
//                        ProgenLog.log(ProgenLog.FINE, this, "openDataSnapshot reading retObj to file ", "Exit");
//
//
//
//             ois.close();

    }

    public void updateAdvancedHtmlFileName(String xmlSnapshot, int snapShotId) {

        String sqlQuery = getResourceBundle().getString("updateAdvSnapshotFileName");
        String finalQuery = "";
        Object[] values = new Object[2];
        values[0] = xmlSnapshot;
        values[1] = snapShotId;
        finalQuery = buildQuery(sqlQuery, values);
        try {
            execModifySQL(finalQuery);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }

    }

    public String getAdvancedHtmlFileName(int snapShotInt) {

        String sqlQuery = getResourceBundle().getString("getAdvSnapshotFileName");
        String finalQuery = "";
        String fileName = "";
        Object[] values = new Object[1];
        PbReturnObject retObj = new PbReturnObject();
        values[0] = snapShotInt;
        finalQuery = buildQuery(sqlQuery, values);
        try {
            retObj = execSelectSQL(finalQuery);
            fileName = retObj.getFieldValueString(0, "ADVANCED_HTML_FILE_NAME");
        } catch (SQLException ex) {
            logger.error("Exception: ", ex);
        }
        return fileName;

    }

    public int insertHeadline(String headlinename, String reportId, int rowCount, String userId) {
        String finalQuery = null;
        String selectQuery = getResourceBundle().getString("getHeadlineId");
        String insertQuery = getResourceBundle().getString("insertHeadlines");
        int getHeadlineId = -1;
        String qry = getResourceBundle().getString("insertHeadlinesAssignment");
        String assignidqry = getResourceBundle().getString("getAssignId");

        try {

            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {

                Object[] obj = new Object[4];
                obj[0] = "not_generated";
                obj[1] = headlinename;
                obj[2] = reportId;
                if (rowCount == 0) {
                    obj[3] = "Empty";
                } else {
                    obj[3] = "Non-Empty";
                }
                finalQuery = buildQuery(insertQuery, obj);
                getHeadlineId = super.insertAndGetSequenceInSQLSERVER(finalQuery, "PRG_AR_HEADLINES");
                Object[] obj1 = new Object[2];
                obj1[0] = getHeadlineId;
                obj1[1] = userId;
                String finalqry = buildQuery(qry, obj1);
                execModifySQL(finalqry);
                //super.insertAndGetSequenceInSQLSERVER(finalqry, "PRG_HEADLINES_ASSIGNMENT");

            } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {

                Object[] obj = new Object[4];
                obj[0] = "not_generated";
                obj[1] = headlinename;
                obj[2] = reportId;
                if (rowCount == 0) {
                    obj[3] = "Empty";
                } else {
                    obj[3] = "Non-Empty";
                }
                finalQuery = buildQuery(insertQuery, obj);
                getHeadlineId = super.insertAndGetSequenceInMySql(finalQuery, "PRG_AR_HEADLINES", "HEADLINE_ID");
                Object[] obj1 = new Object[2];
                obj1[0] = getHeadlineId;
                obj1[1] = userId;
                String finalqry = buildQuery(qry, obj1);
                execModifySQL(finalqry);
                //super.insertAndGetSequenceInSQLSERVER(finalqry, "PRG_HEADLINES_ASSIGNMENT");

            } else {

                getHeadlineId = super.getSequenceNumber(selectQuery);
                Object[] obj = new Object[5];
                obj[0] = getHeadlineId;
                obj[1] = "not_generated";
                obj[2] = headlinename;
                obj[3] = reportId;
                if (rowCount == 0) {
                    obj[4] = "Empty";
                } else {
                    obj[4] = "Non-Empty";
                }
                finalQuery = buildQuery(insertQuery, obj);
                execModifySQL(finalQuery);
                int assignId;
                Object[] obj1 = new Object[3];
                assignId = super.getSequenceNumber(assignidqry);
                obj1[0] = assignId;
                obj1[1] = getHeadlineId;
                obj1[2] = userId;
                String finalqry = buildQuery(qry, obj1);
                execModifySQL(finalqry);
            }

        } catch (Exception ex) {
            getHeadlineId = -1;
            logger.error("Exception: ", ex);
        }
        return getHeadlineId;
    }

    public String getReportHeadlines(String isEdit, HttpServletRequest request) { //plz send data of head lines to your jsp and create table there only .
        String userId = (String) request.getSession(false).getAttribute("USERID");
        String checkedHeadlines = (String) request.getAttribute("checkedHeadlines");
        String qry;
        String sqlQuery = null;
        if (checkedHeadlines != null && !checkedHeadlines.equals("")) {
            qry = getResourceBundle().getString("getCheckedHeadlines");
            Object[] obj = new Object[1];
            obj[0] = checkedHeadlines;
            sqlQuery = buildQuery(qry, obj);
//          System.out.println("getCheckedHeadlines"+sqlQuery);
            logger.info("getCheckedHeadlines: " + sqlQuery);
        } else {
            qry = getResourceBundle().getString("getHeadlinesbasedonUser");
            Object[] obj = new Object[1];
            obj[0] = userId;
            sqlQuery = buildQuery(qry, obj);
        }


        PbReturnObject retObj = new PbReturnObject();
        StringBuilder tableBuilder = new StringBuilder();
        tableBuilder.append("<table id='tablesorter' class='tablesorter'  style='border-collapse: collapse;width:100%'>");
        if (checkedHeadlines != null && !checkedHeadlines.equals("")) {
            tableBuilder.append("<tbody>");
        } else {
            tableBuilder.append("<thead><th colspan='2' class='myhead'>Headlines</th></thead><tbody>");
        }
        try {
            retObj = super.execSelectSQL(sqlQuery);
            if (retObj.getRowCount() > 0) {
                for (int i = 0; i < retObj.getRowCount(); i++) {
                    tableBuilder.append("<tr id='").append(retObj.getFieldValueString(i, "HEADLINE_ID")).append("'><td class='collapsible' rowspan='2' width='5%'><a class='collapsed' id='").append(retObj.getFieldValueString(i, "HEADLINE_ID")).append(",").append(retObj.getFieldValueString(i, "REPORT_ID")).append("' onclick='getHeadlineData(this.id)'>");
                    tableBuilder.append("</a></td><td width='95%'><font color='#336699' class='headlinestyle'>");

                    tableBuilder.append(retObj.getFieldValueString(i, "HEADLINE_NAME")).append("</font></td>");
                    if (isEdit.equalsIgnoreCase("true")) {
                        tableBuilder.append("<td><a class='ui-icon ui-icon-trash' style='text-decoration:none' onclick=\"deleteHeadline('").append(retObj.getFieldValueString(i, "HEADLINE_ID")).append("')\"></td>");
                    }
                    // tableBuilder.append("<td><a onclick=\"mailHeadline('").append(retObj.getFieldValueString(i, "HEADLINE_ID")).append("','").append(retObj.getFieldValueString(i, "HEADLINE_NAME")).append("')\"><img src='icons pinvoke/mail.png'/></a></td>");
                    tableBuilder.append("</tr><tr class='expand-child'><td id='").append(retObj.getFieldValueString(i, "HEADLINE_ID")).append("td").append("' style='display:none' colspan='3'><div id='").append(retObj.getFieldValueString(i, "HEADLINE_ID")).append("div");
                    tableBuilder.append("'></div>");
                    tableBuilder.append("<div id='").append(retObj.getFieldValueString(i, "HEADLINE_ID")).append("prgBar").append("'></div>");
                    tableBuilder.append("</td></tr>");
                }
            }
        } catch (SQLException ex) {
            logger.error("Exception: ", ex);
        }

        tableBuilder.append("</tbody></table>");
        return tableBuilder.toString();
    }

    public Clob getHeadlineData(String headlineId) {
        String sqlQuery = getResourceBundle().getString("getHeadlineData");
        PbReturnObject retObj = new PbReturnObject();
        Object[] obj = new Object[1];
        obj[0] = headlineId;
        Clob clob = null;
        String finalQuery = buildQuery(sqlQuery, obj);
        try {
            retObj = execSelectSQL(finalQuery);
            if (retObj.getRowCount() > 0) //               if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
            //                   String clob1=retObj.getFieldUnknown(0, 0);
            //               }
            //            else{
            {
                clob = (Clob) retObj.getFieldValueClob(0, "HEADLINE_DATA");
            }
//                }
//             snapShot.setHtmlView((Clob) snapShotResultSet.getFieldValueClob(0, "DATA_HTML_VIEW"));

        } catch (SQLException ex) {
            logger.error("Exception: ", ex);
        }
        return clob;
    }

    public String getHeadlineDataString(String headlineId) {
        String sqlQuery = getResourceBundle().getString("getHeadlineData");
        PbReturnObject retObj = new PbReturnObject();
        Object[] obj = new Object[1];
        obj[0] = headlineId;
        String clobString = null;
        String finalQuery = buildQuery(sqlQuery, obj);
        try {
            retObj = execSelectSQL(finalQuery);
            if (retObj.getRowCount() > 0) {
                clobString = (String) retObj.getFieldUnknown(0, 0);
            }

        } catch (SQLException ex) {
            logger.error("Exception: ", ex);
        }
        return clobString;
    }

    public String getHeadlineName(String headlineId) {
        String sqlQuery = getResourceBundle().getString("getHeadlineName");
        PbReturnObject retObj = new PbReturnObject();
        Object[] obj = new Object[1];
        obj[0] = headlineId;
        String headlinename = null;
        String finalQuery = buildQuery(sqlQuery, obj);
        try {
            retObj = execSelectSQL(finalQuery);
            if (retObj.getRowCount() > 0) {
                headlinename = (String) retObj.getFieldValueString(0, "HEADLINE_NAME");
            }

            //snapShot.setHtmlView((Clob) snapShotResultSet.getFieldValueClob(0, "DATA_HTML_VIEW"));

        } catch (SQLException ex) {
            logger.error("Exception: ", ex);
        }
        return headlinename;
    }

    public void deleteHeadline(String headlineId) {
        String sqlQuery = getResourceBundle().getString("deleteHeadline");
        Object[] values = new Object[1];
        values[0] = headlineId;
        String finalQuery = buildQuery(sqlQuery, values);
        try {
            execModifySQL(finalQuery);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }


    }

    public String getReportHeadlinesforMail(String userId) { //plz send data of head lines to your jsp and create table there only .
        String sqlQuery = getResourceBundle().getString("getHeadlines");
        Object[] obj = new Object[1];
        obj[0] = userId;
        String finalQuery = buildQuery(sqlQuery, obj);
        PbReturnObject retObj = new PbReturnObject();
        StringBuilder tableBuilder = new StringBuilder();
        try {
            retObj = super.execSelectSQL(finalQuery);
            tableBuilder.append("<table>");
            for (int i = 0; i < retObj.getRowCount(); i++) {
                tableBuilder.append("<tr id='").append(retObj.getFieldValueString(i, "HEADLINE_ID")).append("'><td><input id='").append(retObj.getFieldValueString(i, "HEADLINE_ID")).append("' type='checkbox' value='").append(retObj.getFieldValueString(i, "HEADLINE_ID")).append("'></td><td>");
                tableBuilder.append(retObj.getFieldValueString(i, "HEADLINE_NAME")).append("<input type='hidden' id='headline_name' name='headline_name' value='").append(retObj.getFieldValueString(i, "HEADLINE_NAME")).append("'</td></tr>");
            }
            tableBuilder.append("</table><br><table align='center'><tr><td><input class='navtitle-hover' type='button' onclick='nextStepOfMail()' value='Next' style='width: auto'></td></tr></table>");

        } catch (SQLException ex) {
            logger.error("Exception: ", ex);
        }
        return tableBuilder.toString();
    }

    public String getHeadlinesforEmail(String headlines) { //plz send data of head lines to your jsp and create table there only .
        String sqlQuery = getResourceBundle().getString("getEmailHeadlines");
        PbReturnObject retObj = new PbReturnObject();
        StringBuilder emailBuilder = new StringBuilder();
        Object[] obj = new Object[1];
        obj[0] = headlines;
        String finalQuery = buildQuery(sqlQuery, obj);
        emailBuilder.append("<table>");
        try {
            retObj = execSelectSQL(finalQuery);
            for (int i = 0; i < retObj.getRowCount(); i++) {
                emailBuilder.append("<tr><td id='").append(retObj.getFieldValueString(i, "HEADLINE_ID")).append("'><font style='font-weight:bold;font-family: verdana;color:#336699;size:12px'>").append("Mail to:");
                emailBuilder.append("</font></td><td  width='150px'><textarea rows='' cols='' style='height:40px;width:300px' id='").append(retObj.getFieldValueString(i, "HEADLINE_ID")).append("'/></td></tr>");

            }
            emailBuilder.append("</table>");
            emailBuilder.append("<input type='hidden' id='headlinemailid' value='").append(headlines).append("'/>");
        } catch (SQLException ex) {
            logger.error("Exception: ", ex);
        }
        return emailBuilder.toString();
    }

    public String getDataStatus(String headlineId) {

        String sqlQry = getResourceBundle().getString("getDataStatus");
        Object[] obj = new Object[1];
        PbReturnObject retObj = new PbReturnObject();
        String dataStatus = "";
        obj[0] = headlineId;
        String finalQry = buildQuery(sqlQry, obj);
        try {
            retObj = execSelectSQL(finalQry);
            if (retObj.getRowCount() > 0) {
                dataStatus = retObj.getFieldValueString(0, 0);
            }
        } catch (SQLException ex) {
            logger.error("Exception: ", ex);
        }
        return dataStatus;

    }

    public void shareHealinestoUsers(String headlineId, String[] users) {

        String qry = getResourceBundle().getString("insertHeadlinesAssignment");
        String assignidqry = getResourceBundle().getString("getAssignId");
        int assignId;
        try {
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                for (int i = 0; i < users.length; i++) {
                    Object[] obj = new Object[2];
                    obj[0] = headlineId;
                    obj[1] = users[i];
                    String finalqry = buildQuery(qry, obj);
                    execModifySQL(finalqry);
                    assignId = super.insertAndGetSequenceInSQLSERVER(finalqry, "PRG_HEADLINES_ASSIGNMENT");
                }
            } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                for (int i = 0; i < users.length; i++) {
                    Object[] obj = new Object[2];
                    obj[0] = headlineId;
                    obj[1] = users[i];
                    String finalqry = buildQuery(qry, obj);
                    execModifySQL(finalqry);
                    assignId = super.insertAndGetSequenceInMySql(finalqry, "PRG_HEADLINES_ASSIGNMENT", "ASSIGN_ID");
                }
            } else {
                Object[] obj = new Object[3];
                for (int i = 0; i < users.length; i++) {
                    assignId = super.getSequenceNumber(assignidqry);
                    obj[0] = assignId;
                    obj[1] = headlineId;
                    obj[2] = users[i];
                    String finalqry = buildQuery(qry, obj);
                    execModifySQL(finalqry);
                }

            }
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }
    }

    public String getDynamicHeadlines(String userId) {
        String qry;
        String sqlQuery = null;
        PbReturnObject retObj = new PbReturnObject();
        StringBuilder tableBuilder = new StringBuilder();
        tableBuilder.append("<table id='tablesorter' class='tablesorter'  style='border-collapse: collapse;width:100%'>");
        tableBuilder.append("<thead><th colspan='2' class='myhead'>Dynaimc Headlines</th></thead><tbody>");
        qry = getResourceBundle().getString("getDynamicHeadlines");
        Object[] obj = new Object[1];
        obj[0] = userId;
        sqlQuery = buildQuery(qry, obj);
        try {
            retObj = super.execSelectSQL(sqlQuery);
            if (retObj.getRowCount() > 0) {
                for (int i = 0; i < retObj.getRowCount(); i++) {
                    tableBuilder.append("<tr id='").append(retObj.getFieldValueString(i, 0)).append("'><td class='collapsible' rowspan='2' width='5%'><a class='collapsed' id='").append(retObj.getFieldValueString(i, 0)).append(",").append(retObj.getFieldValueString(i, 1)).append("' onclick='getHeadlineData(this.id,").append('"' + retObj.getFieldValueString(i, 2) + '"').append(")'>");
                    tableBuilder.append("</a></td><td width='95%'><font color='#336699' class='headlinestyle'><a href='javascript:openReport(").append(retObj.getFieldValueString(i, 0)).append(",").append(retObj.getFieldValueString(i, 1)).append(",").append('"' + retObj.getFieldUnknown(i, 3).replace(";", "&") + '"').append(")'>");
                    tableBuilder.append(retObj.getFieldValueString(i, 2)).append("</a></font></td>");
                    tableBuilder.append("<td><a class='ui-icon ui-icon-trash' style='text-decoration:none' onclick=\"deleteHeadline('").append(retObj.getFieldValueString(i, 0)).append("')\"></td>");
                    // tableBuilder.append("<td><a onclick=\"mailHeadline('").append(retObj.getFieldValueString(i, "HEADLINE_ID")).append("','").append(retObj.getFieldValueString(i, "HEADLINE_NAME")).append("')\"><img src='icons pinvoke/mail.png'/></a></td>");
                    tableBuilder.append("</tr><tr class='expand-child'><td id='").append(retObj.getFieldValueString(i, 0)).append("td").append("' style='display:none' colspan='3'><div id='").append(retObj.getFieldValueString(i, 0)).append("div");
                    tableBuilder.append("'></div>");
                    tableBuilder.append("<div id='").append(retObj.getFieldValueString(i, 0)).append("prgBar").append("'></div>");
                    tableBuilder.append("</td></tr>");
                }
            }
        } catch (SQLException ex) {
            logger.error("Exception: ", ex);
        }

        tableBuilder.append("</tbody></table>");
        return tableBuilder.toString();


    }
    // code written by swati

    public void DynamicheadlineAssignment(String headlineId, String userId) {
        String qry = getResourceBundle().getString("insertHeadlinesAssignment");
        String assignidqry = getResourceBundle().getString("getAssignId");



        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER) || ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
            try {
                Object[] obj = new Object[2];
                obj[0] = headlineId;
                obj[1] = userId;
                String finalqry = buildQuery(qry, obj);
                execModifySQL(finalqry);
            } catch (Exception ex) {
                logger.error("Exception: ", ex);
            }

        } else {
            try {
                int assignId;
                Object[] obj1 = new Object[3];
                assignId = super.getSequenceNumber(assignidqry);
                obj1[0] = assignId;
                obj1[1] = headlineId;
                obj1[2] = userId;
                String finalqry = buildQuery(qry, obj1);
                execModifySQL(finalqry);
            } catch (Exception ex) {
                logger.error("Exception: ", ex);
            }
        }
    }

    public String checkReportAccess(String reportId, String userId) {
        PbReturnObject retObj = new PbReturnObject();
        String qry = "select count(*) from prg_ar_user_reports pu,prg_ar_report_master ar where pu.user_id =" + userId + " and pu.report_id= ar.report_id and pu.report_id=" + reportId;
        String accessreport = "invalid";
        try {
            retObj = super.execSelectSQL(qry);
            if (retObj.getRowCount() > 0) {
                accessreport = "valid";
            }
        } catch (SQLException ex) {
            logger.error("Exception: ", ex);
        }
        return accessreport;

    }
}
