/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.etl;

//import com.progen.log.ProgenLog;
import java.sql.*;
import java.util.ArrayList;
import org.apache.log4j.Logger;
import prg.db.PbReturnObject;

/**
 *
 * @author Administrator
 */
public class AccessDataLoader extends DataLoader {

    Connection accessConn = null;
    ResultSet accessRS = null;
    public static Logger logger = Logger.getLogger(AccessDataLoader.class);

    public Connection getAccessConn(String path) {
//        ProgenLog.log(ProgenLog.FINE, this, "getConnection", "Enter getConnection");
        logger.info("Enter getConnection");
        try {
            if (accessConn == null) {
                Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
                String database = "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ=" + path;
                accessConn = DriverManager.getConnection(database, "", "");
            }

        } catch (Exception e) {
//            ProgenLog.log(ProgenLog.SEVERE, this, "getConnection", "Exception " + e.getMessage());
            logger.error("Exception " + e);
            return null;
        }
//        ProgenLog.log(ProgenLog.FINE, this, "getConnection", "Exit getConnection");
        logger.info("Exit getConnection");
        return accessConn;
    }

    public String loadData(EtlLoadTable loadTable, String startDate, String endDate) {
//        ProgenLog.log(ProgenLog.FINE, this, "loadData", "Enter loadData");
        logger.info("Enter loadData");

        String uploadStatus = EtlLoadConstants.INSERT_FAILED;
        String updateStatus;
        boolean isTableThere = false;
        try {
            isTableThere = this.validateMDB(loadTable);
            if (isTableThere == false) {
                return loadTable.getLoadTable() + EtlLoadConstants.ACCESS_TABLE_NOT_FOUND + loadTable.getEtlFilePath();
            }

            this.con = super.getConnection(loadTable.getConnectionId());
            if (con == null) {
                return EtlLoadConstants.CONNECTION_ERROR;
            }
            con.setAutoCommit(false);
            //first delete data
            super.deleteData(loadTable, startDate, endDate);
            PbReturnObject accessData;
            int rowsToUpload = this.rowsToUpload(loadTable);
            int toRow;

            //initalize the record set
            Statement stmt = getAccessConn(loadTable.getEtlFilePath()).createStatement();//ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            String qry = "select * from " + loadTable.getLoadTable() + "";
            this.accessRS = stmt.executeQuery(qry);
            uploadStatus = insertData(loadTable, accessRS, startDate, endDate);
            if (uploadStatus.equals(EtlLoadConstants.UPLOAD_SUCCESS)) {
                updateStatus = updateLastLoadTime(loadTable);
                con.commit();
            }

            if (this.accessConn != null) {
                accessConn.close();
                accessConn = null;
            }
            con.close();
            con = null;
        } catch (SQLException e) {
//            ProgenLog.log(ProgenLog.SEVERE, this, "loadData", "Exception loadData" + e.getMessage());
            logger.error("Exception loadData " + e.getMessage());

        } finally {
            try {
                if (con != null) {
                    con.close();
                }
                if (this.accessConn != null) {
                    accessConn.close();
                }
            } catch (SQLException e) {
//                 ProgenLog.log(ProgenLog.SEVERE, this, "loadData", "Exit loadData");
                logger.error("Exit loadData" + e);
            }
        }
//        ProgenLog.log(ProgenLog.FINE, this, "loadData", "Exit loadData");
        logger.info("Exit loadData");
        return uploadStatus;
    }

    public PbReturnObject extractData(EtlLoadTable loadTable, String startDate, String endDate, int fromRow, int toRow) {
//        ProgenLog.log(ProgenLog.FINE, this, "extractData", "Enter extractData");
        logger.info("Enter extractData");
        PbReturnObject pbro = new PbReturnObject();
        Connection accConn = null;
        String qry = "";
        ResultSetMetaData rsmd = null;
        String colNames[] = null;
        int rowCount = 0;
        try {
            accConn = this.getAccessConn(loadTable.getEtlFilePath());
            if (accConn == null) {
                return null;
            }
            rsmd = this.accessRS.getMetaData();
            colNames = new String[rsmd.getColumnCount()];
            for (int i = 0; i < colNames.length; i++) {
                colNames[i] = rsmd.getColumnName(i + 1);
            }
            pbro.setColumnNames(colNames);
            this.accessRS.absolute(fromRow);
            for (int j = fromRow; j <= toRow; j++) {
                for (int i = 0; i < rsmd.getColumnCount(); i++) {
                    pbro.setFieldValue(colNames[i], this.accessRS.getString(colNames[i]));
                }
                if (loadTable.isCheckDateLoad()) {
                    pbro.setFieldValue("st_date", startDate);
                    pbro.setFieldValue("end_date", endDate);
                }
                pbro.addRow();
                rowCount++;
                this.accessRS.next();
            }
        } catch (Exception e) {
//            ProgenLog.log(ProgenLog.SEVERE, this, "extractData", "Exception extractData" + e.getMessage());
            logger.error("Exception extractData", e);
            pbro = null;

        }
//        ProgenLog.log(ProgenLog.FINE, this, "extractData", "Exit extractData");
        logger.info("Exit extractData");
        return pbro;
    }

    private int rowsToUpload(EtlLoadTable loadTable) {
//        ProgenLog.log(ProgenLog.FINE, this, "rowsToUpload", "Enter rowsToUpload");
        logger.info("Enter rowsToUpload");
        int rowCount = 0;
        Connection accConn = null;
        String qry = "";
        ResultSet rs = null;
        Statement stmt = null;
        accConn = this.getAccessConn(loadTable.getEtlFilePath());
        if (accConn == null) {
            return rowCount;
        }
        try {
            stmt = accConn.createStatement();
            qry = "select count(*) from " + loadTable.getLoadTable() + "";
            rs = stmt.executeQuery(qry);
            if (rs.next()) {
                rowCount = rs.getInt(1);
            }
        } catch (SQLException ex) {
//            ProgenLog.log(ProgenLog.SEVERE, this, "rowsToUpload", "Exception rowsToUpload " + ex.getMessage());
            logger.error("Exception rowsToUpload ", ex);
            rowCount = 0;
        }
//        ProgenLog.log(ProgenLog.FINE, this, "rowsToUpload", "Exit rowsToUpload");
        logger.info("Exit rowsToUpload");
        return rowCount;
    }

//    public String loadData(EtlLoadTable loadTable, String startDate, String endDate) {
//        String uploadStatus = EtlLoadConstants.INSERT_FAILED;
//        String updateStatus;
//        boolean isTableThere = false;
//        try {
//            isTableThere = this.validateMDB(loadTable);
//            if (isTableThere == false) {
//                return loadTable.getLoadTable() + " " + EtlLoadConstants.ACCESS_TABLE_NOT_FOUND + loadTable.getEtlFilePath();
//            }
//            uploadStatus = this.extractDataForAccess(loadTable, startDate, endDate);
//            // if (accessData != null) {
//            //  uploadStatus = insertData(loadTable, accessData, startDate, endDate);
//            if (uploadStatus.equals(EtlLoadConstants.UPLOAD_SUCCESS)) {
//                updateStatus = updateLastLoadTime(loadTable);
//            }
////            }
////            else
////                return EtlLoadConstants.ACCESS_TABLE_NOT_FOUND+ loadTable.getEtlFilePath();
//
//
//        } catch (Exception e) {
//        } finally {
//        }
//
//        return uploadStatus;
//    }
//    public String extractDataForAccess(EtlLoadTable loadTable, String startDate, String endDate) {
//        PbReturnObject pbro = new PbReturnObject();
//        Connection accConn = null;
//        String qry = "";
//        ResultSet rs = null;
//        Statement stmt = null;
//        ResultSetMetaData rsmd = null;
//        String colNames[] = null;
//        int rowCount = 0;
//        String uploadStatus = EtlLoadConstants.INSERT_FAILED;
//        try {
//            accConn = this.getAccessConn(loadTable.getEtlFilePath());
//            if (accConn == null) {
//                return null;
//            }
//            stmt = accConn.createStatement();
//            qry = "select * from " + loadTable.getLoadTable() + "";
//            rs = stmt.executeQuery(qry);
//            rsmd = rs.getMetaData();
//
//            colNames = new String[rsmd.getColumnCount()];
//            for (int i = 0; i < colNames.length; i++) {
//                colNames[i] = rsmd.getColumnName(i + 1);
//            }
//            pbro.setColumnNames(colNames);
//            while (rs.next()) {
//                for (int i = 0; i < rsmd.getColumnCount(); i++) {
//                    pbro.setFieldValue(colNames[i], rs.getString(colNames[i]));
//                }
//                if (loadTable.isCheckDateLoad()) {
//                    pbro.setFieldValue("st_date", startDate);
//                    pbro.setFieldValue("end_date", endDate);
//                }
//                pbro.addRow();
//                rowCount++;
//                if (rowCount == EtlLoadConstants.MAX_ROW_COUNT) {
//                    uploadStatus = insertData(loadTable, pbro, startDate, endDate);
//                    pbro = new PbReturnObject();
//                    rowCount = 0;
//                }
//
//            }
//            if (rowCount > 0) {
//                uploadStatus = insertData(loadTable, pbro, startDate, endDate);
//             }
//
//        } catch (Exception e) {
//            pbro = null;
//
//        }
//        return uploadStatus;
//    }
    public boolean validateMDB(EtlLoadTable loadTable) {
//        ProgenLog.log(ProgenLog.FINE, this, "validateMDB", "Enter validateMDB");
        logger.info("Enter validateMDB");
        boolean isValid = false;
        DatabaseMetaData dbmd = null;
        String type[] = {"TABLE"};
        ArrayList tableNamesList = new ArrayList();
        try {
            Connection accConn = this.getAccessConn(loadTable.getEtlFilePath());
            dbmd = accConn.getMetaData();
            ResultSet rs = dbmd.getTables(null, null, "%", type);
            while (rs.next()) {
                tableNamesList.add(rs.getString(3).replace(" ", "_").toUpperCase());
            }
            if (tableNamesList.contains(loadTable.getLoadTable().toUpperCase())) {
                isValid = true;
            }
        } catch (SQLException ex) {
            isValid = false;
//            ProgenLog.log(ProgenLog.SEVERE, this, "validateMDB", "Exception validateMDB "+ex.getMessage());
            logger.error("Exception validateMDB ", ex);
        }
//        ProgenLog.log(ProgenLog.FINE, this, "validateMDB", "Exit validateMDB");
        logger.info("Exit validateMDB");
        return isValid;
    }
}
