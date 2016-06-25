package com.progen.etl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import org.apache.log4j.Logger;
import prg.db.PbDb;
import prg.db.PbReturnObject;
import utils.db.ProgenConnection;

/**
 *
 * @author Sathish
 */
public class EtlUploadBd {

    private ResourceBundle resBundle;
    public static Logger logger = Logger.getLogger(EtlUploadBd.class);

    private ResourceBundle getResourceBundle() {
//        ProgenLog.log(ProgenLog.FINE, this, "getResourceBundle", "Enter getResourceBundle");
        logger.info("Enter getResourceBundle");
        if (this.resBundle == null) {
            this.resBundle = new EtlResourceBundle();
        }
//        ProgenLog.log(ProgenLog.FINE, this, "getResourceBundle", "Exit getResourceBundle");
        logger.info("Exit getResourceBundle");
        return resBundle;

    }

    public String loadData(String tabName, String startDate, String endDate) {
//        ProgenLog.log(ProgenLog.FINE, this, "loadData", "Enter loadData for "+tabName);
        logger.info("Enter loadData for: " + tabName);
        EtlLoadTable loadTable = this.getEtlLoadTable(tabName);
        DataLoader dataLoader;

        if (loadTable.getSourceType().equals(EtlLoadConstants.EXCEL_SOURCE)) {
            dataLoader = new ExcelDataLoader();
        } else {
            dataLoader = new AccessDataLoader();
        }

//        ProgenLog.log(ProgenLog.FINE, this, "loadData", "Exit loadData "+tabName);
        logger.info("Exit loadData " + tabName);
        return dataLoader.loadData(loadTable, startDate, endDate);
    }

    private EtlLoadTable getEtlLoadTable(String tabName) {
//        ProgenLog.log(ProgenLog.FINE, this, "getEtlLoadTable", "Enter getEtlLoadTable");
        logger.info("Enter getEtlLoadTable");
        EtlLoadTable loadTable = null;
        try {
            String etlLoadTabQry = this.getResourceBundle().getString("getEtlSetup");
            PbDb pbDb = new PbDb();
            Object[] qryBind = new Object[1];
            qryBind[0] = tabName;

            String finalQuery = pbDb.buildQuery(etlLoadTabQry, qryBind);
            PbReturnObject etlRetObj = pbDb.execSelectSQL(finalQuery);

            loadTable = new EtlLoadTable(etlRetObj);

        } catch (SQLException ex) {
            logger.error("Exception: ", ex);
        } catch (Exception e) {
//            ProgenLog.log(ProgenLog.SEVERE, this, "getEtlLoadTable", "Exception getEtlLoadTable " + e.getMessage());
            logger.error("Exception getEtlLoadTable: ", e);
        }
//        ProgenLog.log(ProgenLog.FINE, this, "getEtlLoadTable", "Exit getEtlLoadTable");
        logger.info("Exit getEtlLoadTable");
        return loadTable;
    }

    public boolean showDates(String tableName) {
//        ProgenLog.log(ProgenLog.FINE, this, "showDates", "Enter showDates");
        logger.info("Enter showDates");
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        String qry = "";
        boolean flag = false;
        try {
            con = ProgenConnection.getInstance().getConnection();
            stmt = con.createStatement();
            qry = "select CHECK_DATE,INCREMENTAL_DATE_LOAD from PRG_ETL_SETUP where DB_TABLE='" + tableName + "'";
            rs = stmt.executeQuery(qry);
            while (rs.next()) {
                if ((rs.getString("CHECK_DATE") != null && rs.getString("CHECK_DATE").equalsIgnoreCase("y")) || (rs.getString("INCREMENTAL_DATE_LOAD") != null && rs.getString("INCREMENTAL_DATE_LOAD").equalsIgnoreCase("y"))) {
                    flag = true;
                }
            }
            con.close();
        } catch (Exception e) {
//            ProgenLog.log(ProgenLog.SEVERE, this, "showDates", "Exception showDates " + e.getMessage());
            logger.error("Exception showDates ", e);
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
//                ProgenLog.log(ProgenLog.SEVERE, this, "showDates", "Exit showDates");
                logger.error("Exit showDates", e);
            }
        }
//        ProgenLog.log(ProgenLog.FINE, this, "showDates", "Exit showDates");
        logger.info("Exit showDates");
        return flag;
    }

    public StringBuffer getListOfTables() {
//        ProgenLog.log(ProgenLog.FINE, this, "getListOfTables", "Enter getListOfTables");
        logger.info("Enter getListOfTables");
        Connection con = null;
        StringBuffer sb = new StringBuffer("");
        try {
            Statement stmt, stmtAcc = null;
            ResultSet rsExcel = null;
            ResultSet rsAccess = null;
            con = ProgenConnection.getInstance().getConnection();
            stmt = con.createStatement();
            stmtAcc = con.createStatement();
            rsExcel = stmt.executeQuery(getResourceBundle().getString("getExcelTableList"));
            rsAccess = stmtAcc.executeQuery(getResourceBundle().getString("getAccessTableList"));
            sb.append("<option value=''>--select--</option>");
            while (rsExcel.next()) {
                sb.append("<option value='" + rsExcel.getString(1) + "'>" + rsExcel.getString(2) + "</option>");
            }
            sb.append("@");
            sb.append("<option value=''>--select--</option>");
            while (rsAccess.next()) {
                sb.append("<option value='" + rsAccess.getString(1) + "'>" + rsAccess.getString(2) + "</option>");
            }
        } catch (Exception e) {
//            ProgenLog.log(ProgenLog.SEVERE, this, "getListOfTables", "Exception getListOfTables " + e.getMessage());
            logger.error("Exception getListOfTables ", e);
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
//                ProgenLog.log(ProgenLog.SEVERE, this, "getListOfTables", "Exception getListOfTables");
                logger.error("Exception getListOfTables ", e);
            }
        }
//        ProgenLog.log(ProgenLog.FINE, this, "getListOfTables", "Exit getListOfTables");
        logger.info("Exit getListOfTables");
        return sb;
    }

    public String lastUploadTime(String tableName) {
        String lastLoadTime = "";
        try {
            String dtQry = this.getResourceBundle().getString("lastLoadTime");
            PbDb pbDb = new PbDb();
            Object[] qryBind = new Object[1];
            qryBind[0] = tableName;
            String finalQuery = pbDb.buildQuery(dtQry, qryBind);
            PbReturnObject etlRetObj = pbDb.execSelectSQL(finalQuery);
            if (etlRetObj.getFieldValueString(0, "INCREMENTAL_LOAD") != null) {
                lastLoadTime = etlRetObj.getFieldValueString(0, "LAST_LOAD_DATE");
            } else {
                lastLoadTime = "";
            }
        } catch (Exception e) {
//            ProgenLog.log(ProgenLog.SEVERE, this, "lastUploadTime", "Exception lastUploadTime " + e.getMessage());
            logger.error("Exception lastUploadTime ", e);
        }
//        ProgenLog.log(ProgenLog.FINE, this, "lastUploadTime", "Exit lastUploadTime");
        logger.info("Exit lastUploadTime");
        return lastLoadTime;
    }

    public String checkDates(String tableName, String stDate, String endDate) {
//        ProgenLog.log(ProgenLog.FINE, this, "checkDates", "Enter checkDates");
        logger.info("Enter checkDates");
        Statement stmt = null;
        ResultSet rs = null;
        String qry2;
        String connId = null;
        String incDateCol = null;
        String checkDate = null;
        String incrDateLoad;
        String dateValidateColumn = null;
        Connection connTwo = null;
        String message = "";
        try {
            String checkDtQry = this.getResourceBundle().getString("checkDates");
            PbDb pbDb = new PbDb();
            Object[] qryBind = new Object[1];
            qryBind[0] = tableName;
            String finalQuery = pbDb.buildQuery(checkDtQry, qryBind);
            PbReturnObject etlRetObj = pbDb.execSelectSQL(finalQuery);
            //  con = ProgenConnection.getInstance().getConnection();
            //  stmt = con.createStatement();
            //  qry1 = "select CONNECTION_ID,INCR_DATE_COLUMN,INCREMENTAL_DATE_LOAD,CHECK_DATE from PRG_ETL_SETUP WHERE DB_TABLE='" + tableName + "'";
            //  rs = stmt.executeQuery(qry1);
            if (etlRetObj.getRowCount() > 0) {
                connId = etlRetObj.getFieldValueString(0, "CONNECTION_ID");
                incDateCol = etlRetObj.getFieldValueString(0, "INCR_DATE_COLUMN");
                checkDate = etlRetObj.getFieldValueString(0, "CHECK_DATE");
                incrDateLoad = etlRetObj.getFieldValueString(0, "INCREMENTAL_DATE_LOAD");
                if (incrDateLoad != null && "y".equalsIgnoreCase(incrDateLoad)) {
                    dateValidateColumn = incDateCol;
                } else if (checkDate != null && checkDate.equalsIgnoreCase("y")) {
                    dateValidateColumn = "end_date";
                }
            }
            if (dateValidateColumn != null) {
                connTwo = ProgenConnection.getInstance().getConnectionByConId(connId);
                qry2 = "select count(*) from  " + tableName + " where " + dateValidateColumn + " between '" + stDate + "' and '" + endDate + "'";
                stmt = connTwo.createStatement();
                rs = stmt.executeQuery(qry2);
                if (rs.next()) {
                    int count = rs.getInt(1);
                    if (count > 0) {
                        if ("y".equalsIgnoreCase(checkDate)) {
                            message = EtlLoadConstants.CANNOT_UPLOAD;
                        } else {
                            message = EtlLoadConstants.CONFIRM_UPLOAD;
                        }
                    }
                }
                rs.close();
                stmt.close();
                rs = null;
                stmt = null;
                connTwo.close();
                connTwo = null;
            }

        } catch (Exception ex) {
//            ProgenLog.log(ProgenLog.SEVERE, this, "checkDates", "Exception checkDates " + ex.getMessage());
            logger.error("Exception checkDates ", ex);
        } finally {
            try {
                if (connTwo != null) {
                    connTwo.close();
                }
            } catch (Exception e) {
//                ProgenLog.log(ProgenLog.SEVERE, this, "checkDates", "Exception checkDates " + e.getMessage());
                logger.error("Exception checkDates ", e);
            }
        }
//        ProgenLog.log(ProgenLog.FINE, this, "checkDates", "Exit checkDates");
        logger.info("Exit checkDates");
        return message;
    }
}
