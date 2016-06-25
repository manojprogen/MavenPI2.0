/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.etl;

import java.sql.*;
import java.util.ArrayList;
import org.apache.log4j.Logger;
import prg.db.PbReturnObject;
import utils.db.ProgenConnection;

/**
 *
 * @author Administrator
 */
public abstract class DataLoader {

    public static Logger logger = Logger.getLogger(DataLoader.class);

    public abstract String loadData(EtlLoadTable loadTable, String startDate, String endDate);
    //  public abstract PbReturnObject extractData(EtlLoadTable loadTable, String stdate1, String enddate1, int fromRow, int toRow);
    protected Connection con;

    public Connection getConnection(Integer conId) {
//        ProgenLog.log(ProgenLog.FINE, this, "getConnection", "Enter getConnection");
        logger.info("Enter getConnection");
        String connectionId = conId.toString();
        //  ConnectionDAO connDAO = new ConnectionDAO();
        Connection con = ProgenConnection.getInstance().getConnectionByConId(connectionId);
//        ProgenLog.log(ProgenLog.FINE, this, "getConnection", "Exit getConnection");
        logger.info("Exit getConnection");
        return con;
    }

    public String deleteData(EtlLoadTable loadTable, String startDate, String endDate) {
//        ProgenLog.log(ProgenLog.FINE, this, "deleteData", "Enter delete data " + loadTable.getLoadTable());
        logger.info("Enter delete data " + loadTable.getLoadTable());
        String delQry;
        if (con == null) {
            return EtlLoadConstants.CONNECTION_ERROR;
        }
        if (!loadTable.isIncrementalLoad() || loadTable.isIncrementalDateLoad()) {
            if (!loadTable.isIncrementalDateLoad()) {
                delQry = "delete from " + loadTable.getLoadTable();
            } else {
                delQry = "delete from " + loadTable.getLoadTable() + " where " + loadTable.getIncrementalDateColumn() + " between '"
                        + startDate + "' and '" + endDate + "'";
            }
            try {
//                ProgenLog.log(ProgenLog.FINE, this, "deleteData", "Delete Query " + delQry);
                logger.info("Delete Query " + delQry);
                Statement stmt = con.createStatement();
                stmt.executeUpdate(delQry);
                stmt.close();
            } catch (SQLException e) {
//                ProgenLog.log(ProgenLog.SEVERE, this, "deleteData", "Delete Query Failed " + e.getMessage());
                logger.error("Delete Query Failed " + e);
                return EtlLoadConstants.DELETE_DATA_FAILED;
            }
        }
//        ProgenLog.log(ProgenLog.FINE, this, "deleteData", "Exit Delete data Success");
        logger.info("Exit Delete data Success");
        return EtlLoadConstants.DELETE_DATA_SUCCESS;

    }

    protected String updateLastLoadTime(EtlLoadTable loadTable) {
//        ProgenLog.log(ProgenLog.FINE, this, "updateLastLoadTime", "Enter updateLastLoadTime");
        logger.info("Enter updateLastLoadTime");
        Connection metaConn = null;
        try {
            metaConn = ProgenConnection.getInstance().getConnection();
            Statement stmtDtUpdate = metaConn.createStatement();
            String stmtDtUpdateQry = "update PRG_ETL_SETUP set LAST_LOAD_DATE=(SELECT GETDATE()) where DB_TABLE='" + loadTable.getLoadTable() + "'";
            stmtDtUpdate.executeUpdate(stmtDtUpdateQry);
            stmtDtUpdate.close();
            stmtDtUpdate = null;

            metaConn.close();
            metaConn = null;

        } catch (SQLException ex) {
//            ProgenLog.log(ProgenLog.SEVERE, this, "updateLastLoadTime", "Exception " + ex.getMessage());
            logger.error("Exception ", ex);
        } finally {
            try {
                if (metaConn != null) {
                    metaConn.close();
                }
            } catch (SQLException e) {
            }
        }
//        ProgenLog.log(ProgenLog.FINE, this, "updateLastLoadTime", "Exit updateLastLoadTime");
        logger.info("Exit updateLastLoadTime");
        return EtlLoadConstants.UPDATE_LOAD_DATE_SUCCESS;
    }

    public String insertData(EtlLoadTable loadTable, PbReturnObject rowData, String startDate, String endDate) {
//        ProgenLog.log(ProgenLog.FINE, this, "insertData", "Enter insertData");
        logger.info("Enter insertData");
        ArrayList<String> qryList = new ArrayList<String>();
        int[] batchUpdate = null;
        PreparedStatement pStatement = null;
        try {
            // con=getConnection(loadTable.getConnectionId());
            //con.setAutoCommit(false);
            if (con == null) {
                return EtlLoadConstants.CONNECTION_ERROR;
            }
            String qry = "select * from " + loadTable.getLoadTable() + "";

            int count = 0;
            Statement stmt = con.createStatement();
            Statement stmtBatch = con.createStatement();
            ResultSet rs = stmt.executeQuery(qry);
            ResultSetMetaData rsmd = rs.getMetaData();

            int colCount = rsmd.getColumnCount();
            String colTypes[] = new String[colCount];
            for (int c = 0; c < colTypes.length; c++) {
                colTypes[c] = rsmd.getColumnTypeName(c + 1);
            }
            StringBuffer qryinsert = new StringBuffer("");
            qryinsert.append("insert into " + loadTable.getLoadTable() + " values(");
            for (int y = 1; y <= colCount; y++) {
                if (y == (colCount)) {
                    qryinsert.append("? )");
                } else {
                    qryinsert.append("?,");
                }
            }
            pStatement = con.prepareStatement(qryinsert.toString());
            for (int row = 0; row < rowData.getRowCount(); row++) {
                // queryBuffer.append("insert into " + loadTable.getLoadTable() + " values(");
                for (int col = 0; col < colCount; col++) {
                    // 
                    // 
                    if (row == 2882) {
                        logger.info("value: " + rowData.getFieldValueString(row, col));
                    }
                    if (rowData.getFieldValueString(row, col) == null || rowData.getFieldValueString(row, col).equalsIgnoreCase("")) {
                        pStatement.setNull((col + 1), Types.NULL);
                    } else {
                        if (colTypes[col].equalsIgnoreCase("VARCHAR")) {
                            //queryBuffer.append("'" + rowData.getFieldValue(row, col).toString().replace("'", " ") + "'");
                            pStatement.setString((col + 1), rowData.getFieldValueString(row, col));
                        } else if (colTypes[col].equalsIgnoreCase("NUMERIC")) {
                            //queryBuffer.append("" + rowData.getFieldValue(row, col).toString().replace("'", "").replace("@", "").replace("#", "").replace("$", "").replace("%", "") + "");

                            pStatement.setString((col + 1), rowData.getFieldValueString(row, col));
                        } else if (colTypes[col].equalsIgnoreCase("FLOAT")) {
                            //queryBuffer.append("" + rowData.getFieldValue(row, col).toString().replace("'", "").replace("@", "").replace("#", "").replace("$", "").replace("%", "") + "");

                            pStatement.setFloat((col + 1), Float.parseFloat(rowData.getFieldValue(row, col).toString()));
                        } else if (colTypes[col].equalsIgnoreCase("DATETIME") && rsmd.getColumnName(col + 1).equalsIgnoreCase("st_date")) {
                            //queryBuffer.append("(convert(datetime," + rowData.getFieldValue(row, col) + ",100))");
                            pStatement.setString((col + 1), rowData.getFieldValue(row, col).toString().replace("'", " "));
                        } else if (colTypes[col].equalsIgnoreCase("DATETIME") && rsmd.getColumnName(col + 1).equalsIgnoreCase("end_date")) {
                            //queryBuffer.append("(convert(datetime," + rowData.getFieldValue(row, col) + ",100))");
                            pStatement.setString((col + 1), rowData.getFieldValue(row, col).toString().replace("'", " "));
                        } else if (colTypes[col].equalsIgnoreCase("DATETIME")) {
                            // queryBuffer.append("(convert(datetime,'" + rowData.getFieldValue(row, col) + "',103))");
                            pStatement.setString((col + 1), rowData.getFieldValueString(row, col));
                        } else {
                            pStatement.setString((col + 1), rowData.getFieldValueString(row, col));
                        }
                    }
                }
                pStatement.addBatch();
                count++;
                if (count == EtlLoadConstants.BATCH_SIZE) {
                    pStatement.executeBatch();
                    count = 0;
                    pStatement.clearBatch();
                }
            }
            if (count > 0) {
                pStatement.executeBatch();
                count = 0;
                pStatement.clearBatch();

            }
            pStatement.close();
            pStatement = null;
//            ProgenLog.log(ProgenLog.FINE, this, "insertData", "Exit insertData");
            logger.info("Exit insertData");
            return EtlLoadConstants.UPLOAD_SUCCESS;
        } catch (BatchUpdateException e) {
//            ProgenLog.log(ProgenLog.SEVERE, this, "loadData", "Insertion Failed BatchUpdateException " + e.getMessage());
            logger.error("Insertion Failed BatchUpdateException ", e);
            return EtlLoadConstants.INSERT_FAILED;
        } catch (SQLException e) {
//            ProgenLog.log(ProgenLog.SEVERE, this, "loadData", "Insertion Failed SQLException " + e.getMessage());
            logger.error("Insertion Failed SQLException ", e);
            return EtlLoadConstants.INSERT_FAILED;
        } finally {
            try {
                if (pStatement != null) {
                    pStatement.close();
                }
            } catch (SQLException e) {
            }
        }
    }

    public String insertData(EtlLoadTable loadTable, ResultSet rsAcc, String startDate, String endDate) {
//        ProgenLog.log(ProgenLog.FINE, this, "insertData", "Enter insertData");
        logger.info("Enter insertData");
        Statement stmtSql;
        ResultSet rsSql;
        PreparedStatement pStatement = null;
        StringBuffer qryinsert = new StringBuffer();
        if (con == null) {
            return EtlLoadConstants.CONNECTION_ERROR;
        }
        try {
            // con = getConnection(loadTable.getConnectionId());
            ResultSetMetaData rsmdSql = rsAcc.getMetaData();
            //ResultSetMetaData rsmdAcc = rsAcc.getMetaData();
            int columnCount = rsmdSql.getColumnCount();
            qryinsert.append("insert into " + loadTable.getLoadTable() + " values(");
            for (int y = 1; y <= columnCount; y++) {
                if (y == (columnCount)) {
                    qryinsert.append("? )");
                } else {
                    qryinsert.append("?,");
                }
            }
            pStatement = con.prepareStatement(qryinsert.toString());
            int count = 0;

            while (rsAcc.next()) {
                for (int y = 1; y <= columnCount; y++) {
                    // 
                    if (rsmdSql.getColumnTypeName(y).equalsIgnoreCase("FLOAT") && rsmdSql.getColumnName(y).replace(" ", "_").trim().equalsIgnoreCase("Cheque_No")) {
                        pStatement.setString(y, null);
                    } else {
                        if (rsmdSql.getColumnTypeName(y).equalsIgnoreCase("NUMERIC")) {
                            pStatement.setString(y, rsAcc.getString(y));
                        } else if (rsmdSql.getColumnTypeName(y).equalsIgnoreCase("FLOAT")) {
                            pStatement.setString(y, rsAcc.getString(y));
                        } else if (rsmdSql.getColumnTypeName(y).equalsIgnoreCase("BOOLEAN")) {
                            pStatement.setString(y, rsAcc.getString(y));
                        } else if (rsmdSql.getColumnTypeName(y).equalsIgnoreCase("CURRENCY")) {
                            pStatement.setString(y, rsAcc.getString(y));
                        } else if (rsmdSql.getColumnTypeName(y).equalsIgnoreCase("DOUBLE")) {
                            pStatement.setString(y, rsAcc.getString(y));
                        } else if (rsmdSql.getColumnTypeName(y).equalsIgnoreCase("LONGBINARY")) {
                            pStatement.setString(y, rsAcc.getString(y));
                        } else if (rsmdSql.getColumnTypeName(y).equalsIgnoreCase("MEMO")) {
                            pStatement.setString(y, rsAcc.getString(y));
                        } else if (rsmdSql.getColumnTypeName(y).equalsIgnoreCase("SINGLE")) {
                            pStatement.setString(y, rsAcc.getString(y));
                        } else if (rsmdSql.getColumnTypeName(y).equalsIgnoreCase("DATETIME")) {
                            //pStatement.setString(y, "convert(datetime,'"+rsAcc.getString(y)+"',105)");
                            pStatement.setString(y, rsAcc.getString(y));
                        } else if (rsmdSql.getColumnTypeName(y).equalsIgnoreCase("VARCHAR")) {
                            pStatement.setString(y, rsAcc.getString(y));
                        } else {
                            pStatement.setString(y, rsAcc.getString(y));
                        }
                    }
                }
                pStatement.addBatch();
                count++;
                if (count == EtlLoadConstants.BATCH_SIZE) {
                    //
                    pStatement.executeBatch();
                    count = 0;
                    pStatement.clearBatch();
                    //
                }
            }
            if (count > 0) {
                pStatement.executeBatch();
                count = 0;
                pStatement.clearBatch();

            }
            pStatement.close();
            pStatement = null;
//            ProgenLog.log(ProgenLog.FINE, this, "insertData", "Exit insertData");
            logger.info("Exit insertData");
            return EtlLoadConstants.UPLOAD_SUCCESS;
        } catch (BatchUpdateException e) {
//            ProgenLog.log(ProgenLog.SEVERE, this, "insertData", "Insertion Failed BatchUpdateException " + e.getMessage());
            logger.error("Insertion Failed BatchUpdateException ", e);
            return EtlLoadConstants.INSERT_FAILED;
        } catch (SQLException e) {
//            ProgenLog.log(ProgenLog.SEVERE, this, "insertData", "Insertion Failed SQLException " + e.getMessage());
            logger.error("Insertion Failed SQLException ", e);
            return EtlLoadConstants.INSERT_FAILED;
        } finally {
            try {
                if (pStatement != null) {
                    pStatement.close();
                }
            } catch (SQLException e) {
            }
        }
    }
}
