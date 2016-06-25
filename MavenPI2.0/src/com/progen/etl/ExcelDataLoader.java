/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.etl;

//import com.progen.log.ProgenLog;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import jxl.Cell;
import jxl.CellType;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.read.biff.BlankCell;
import org.apache.log4j.Logger;
import prg.db.PbReturnObject;

/**
 *
 * @author Administrator
 */
public class ExcelDataLoader extends DataLoader {

    private Workbook wb;
    public static Logger logger = Logger.getLogger(ExcelDataLoader.class);

    public String loadData(EtlLoadTable loadTable, String startDate, String endDate) {
//        ProgenLog.log(ProgenLog.FINE, this, "loadData", "Enter loadData");
        logger.info("Enter loadData");
        if (loadTable.getLoadTable().equalsIgnoreCase("Channel_Target") || loadTable.getLoadTable().equalsIgnoreCase("Sub_Channel_Target") || loadTable.getLoadTable().equalsIgnoreCase("Sale_Hierarchy_Target")) {
            return loadTargetData(loadTable);
        }
        String uploadStatus = EtlLoadConstants.INSERT_FAILED;
        String updateStatus;
        try {

            this.con = super.getConnection(loadTable.getConnectionId());
            if (con == null) {
                return EtlLoadConstants.CONNECTION_ERROR;
            }
            this.wb = this.loadWorkbook(loadTable.getEtlFilePath());
            if (this.wb == null) {
                return EtlLoadConstants.EXCEL_CANNOT_OPEN_WORKBOOK + " " + loadTable.getEtlFilePath();
            }

            uploadStatus = this.validateSheetWithTable(loadTable, wb.getSheet(0));
            if (EtlLoadConstants.EXCEL_SHEET_VALID.equals(uploadStatus)) {
                con.setAutoCommit(false);
                int rowsToUpload = this.rowsToUpload(wb.getSheet(0));
                int toRow;
                //first delete data
                super.deleteData(loadTable, startDate, endDate);
                PbReturnObject excelData;

                for (int i = 1; i <= rowsToUpload; i += EtlLoadConstants.EXTRACT_BATCH_SIZE) {
                    uploadStatus = EtlLoadConstants.INSERT_FAILED;
                    if (rowsToUpload < i + EtlLoadConstants.EXTRACT_BATCH_SIZE) {
                        toRow = rowsToUpload;
                    } else {
                        toRow = i + EtlLoadConstants.EXTRACT_BATCH_SIZE - 1;
                    }
                    excelData = this.extractData(loadTable, startDate, endDate, i, toRow);
                    if (excelData != null) {
                        uploadStatus = insertData(loadTable, excelData, startDate, endDate);
                        if (uploadStatus.equals(EtlLoadConstants.UPLOAD_SUCCESS)) {
                            continue;
                        } else {
                            break;
                        }
                    }
                }
                if (uploadStatus.equals(EtlLoadConstants.UPLOAD_SUCCESS)) {
                    updateStatus = updateLastLoadTime(loadTable);
                    con.commit();
                }
            }
            con.close();
            con = null;
        } catch (SQLException e) {
//            ProgenLog.log(ProgenLog.SEVERE, this, "loadData", "Exception loadData " + e.getMessage());
            logger.error("Exception loadData ", e);
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
//                ProgenLog.log(ProgenLog.SEVERE, this, "loadData", "Exception loadData " + e.getMessage());
                logger.error("Exception loadData ", e);
            }
        }
//        ProgenLog.log(ProgenLog.FINE, this, "loadData", "Exit loadData ");
        logger.info("Exit loadData ");
        return uploadStatus;
    }

    public String loadTargetData(EtlLoadTable loadTable) {
//        ProgenLog.log(ProgenLog.FINE, this, "loadTargetData", "Enter loadTargetData ");
        logger.info("Enter loadTargetData ");
        this.con = super.getConnection(loadTable.getConnectionId());
        if (con == null) {
            return EtlLoadConstants.CONNECTION_ERROR;
        }
        this.wb = this.loadWorkbook(loadTable.getEtlFilePath());
        if (wb == null) {
            return EtlLoadConstants.EXCEL_CANNOT_OPEN_WORKBOOK;
        }
        Sheet dataSheet = wb.getSheet(0);
        int sheetColumns;
        PreparedStatement pStmt = null;
        Cell cell;
        CellType type;
        String qry[] = new String[6];
        qry[0] = "INSERT INTO PRG_FACT_MEASURE_DET_39(TARGET_ID,Sub_Channel_Name,Channel_Name,VIEWBY,SECVIEWBY,TARGETM_RLS_Login_policy_no) VALUES(?,?,?,?,?,?)";
        qry[1] = "INSERT INTO PRG_FACT_MEASURE_DET_40(TARGET_ID,Sub_Channel_Name,Channel_Name,VIEWBY,SECVIEWBY,TARGETM_RLS_Login_NBI) VALUES(?,?,?,?,?,?)";
        qry[2] = "INSERT INTO PRG_FACT_MEASURE_DET_41(TARGET_ID,Sub_Channel_Name,Channel_Name,VIEWBY,SECVIEWBY,TARGETM_policy_no) VALUES(?,?,?,?,?,?)";
        qry[3] = "INSERT INTO PRG_FACT_MEASURE_DET_42(TARGET_ID,Sub_Channel_Name,Channel_Name,VIEWBY,SECVIEWBY,TARGETM_NBI) VALUES(?,?,?,?,?,?)";
        qry[4] = "INSERT INTO PRG_FACT_MEASURE_DET_43(TARGET_ID,Sub_Channel_Name,Channel_Name,VIEWBY,SECVIEWBY,TARGETM_WNB_Premium) VALUES(?,?,?,?,?,?)";
        qry[5] = "INSERT INTO PRG_FACT_MEASURE_DET_44(TARGET_ID,Sub_Channel_Name,Channel_Name,VIEWBY,SECVIEWBY,TARGETM_NBI_Commitment) VALUES(?,?,?,?,?,?)";

        if (loadTable.getLoadTable().equalsIgnoreCase("Channel_Target")) {
            try {
                con.setAutoCommit(false);
                for (int i = 0; i < qry.length; i++) {
                    pStmt = con.prepareStatement(qry[i]);
                    for (int j = 1; j < dataSheet.getRows(); j++) {

                        pStmt.setInt(1, 39 + i);
                        pStmt.setNull(2, Types.NULL);
                        if (dataSheet.getCell(0, j).getType() != CellType.EMPTY) {
                            pStmt.setString(3, dataSheet.getCell(0, j).getContents());
                        } else {
                            pStmt.setNull(3, Types.NULL);
                        }
                        pStmt.setString(4, EtlLoadConstants.VIEW_BY_CHANNEL);
                        pStmt.setNull(5, Types.NULL);
                        if (dataSheet.getCell(i + 1, j).getType() != CellType.EMPTY) {
                            pStmt.setString(6, dataSheet.getCell(i + 1, j).getContents());
                        } else {
                            pStmt.setNull(6, Types.NULL);
                        }

                        pStmt.addBatch();
                    }
                    pStmt.executeBatch();
                    pStmt.clearBatch();
                }
                con.commit();
                pStmt = null;
                con.close();
            } catch (Exception e) {
//                ProgenLog.log(ProgenLog.SEVERE, this, "loadTargetData", "Exception loadTargetData " + e.getMessage());
                logger.error("Exception loadTargetData ", e);
                return EtlLoadConstants.INSERTION_ERROR;
            }
        } else if (loadTable.getLoadTable().equalsIgnoreCase("Sub_Channel_Target")) {
            try {
                con.setAutoCommit(false);
                for (int i = 0; i < qry.length; i++) {
                    pStmt = con.prepareStatement(qry[i]);
                    for (int j = 1; j < dataSheet.getRows(); j++) {
                        //  
                        pStmt.setInt(1, 39 + i);
                        if (dataSheet.getCell(0, j).getType() != CellType.EMPTY) {
                            pStmt.setString(2, dataSheet.getCell(0, j).getContents());
                        } else {
                            pStmt.setNull(2, Types.NULL);
                        }
                        if (dataSheet.getCell(1, j).getType() != CellType.EMPTY) {
                            pStmt.setString(3, dataSheet.getCell(1, j).getContents());
                        } else {
                            pStmt.setNull(3, Types.NULL);
                        }
                        pStmt.setString(4, EtlLoadConstants.VIEW_BY_CHANNEL);
                        pStmt.setString(5, EtlLoadConstants.VIEW_BY_SUB_CHANNEL);
                        if (dataSheet.getCell(i + 2, j).getType() != CellType.EMPTY) {
                            pStmt.setString(6, dataSheet.getCell(i + 2, j).getContents());
                        } else {
                            pStmt.setNull(6, Types.NULL);
                        }
                        pStmt.addBatch();
                    }
                    pStmt.executeBatch();
                    pStmt.clearBatch();
                }
                con.commit();
                pStmt.close();
                pStmt = null;
                con.close();
            } catch (Exception e) {
//                ProgenLog.log(ProgenLog.SEVERE, this, "loadTargetData", "Exception loadTargetData " + e.getMessage());
                logger.error("Exception loadTargetData ", e);
                return EtlLoadConstants.INSERTION_ERROR;
            }
        } else if (loadTable.getLoadTable().equalsIgnoreCase("Sale_Hierarchy_Target")) {
            String qry1[] = new String[8];
            qry1[0] = "INSERT INTO PRG_FACT_MEASURE_DET_39(TARGET_ID,VIEWBY,RSM_Name,MOM_Name,MOA_Name,Agent_Name,TARGETM_RLS_Login_policy_no) VALUES(?,?,?,?,?,?,?)";
            qry1[1] = "INSERT INTO PRG_FACT_MEASURE_DET_40(TARGET_ID,VIEWBY,RSM_Name,MOM_Name,MOA_Name,Agent_Name,TARGETM_RLS_Login_NBI) VALUES(?,?,?,?,?,?,?)";
            qry1[2] = "INSERT INTO PRG_FACT_MEASURE_DET_41(TARGET_ID,VIEWBY,RSM_Name,MOM_Name,MOA_Name,Agent_Name,TARGETM_policy_no) VALUES(?,?,?,?,?,?,?)";
            qry1[3] = "INSERT INTO PRG_FACT_MEASURE_DET_42(TARGET_ID,VIEWBY,RSM_Name,MOM_Name,MOA_Name,Agent_Name,TARGETM_NBI) VALUES(?,?,?,?,?,?,?)";
            qry1[4] = "INSERT INTO PRG_FACT_MEASURE_DET_43(TARGET_ID,VIEWBY,RSM_Name,MOM_Name,MOA_Name,Agent_Name,TARGETM_WNB_Premium) VALUES(?,?,?,?,?,?,?)";
            qry1[5] = "INSERT INTO PRG_FACT_MEASURE_DET_44(TARGET_ID,VIEWBY,RSM_Name,MOM_Name,MOA_Name,Agent_Name,TARGETM_NBI_Commitment) VALUES(?,?,?,?,?,?,?)";
            qry1[6] = "INSERT INTO PRG_FACT_MEASURE_DET_53(TARGET_ID,VIEWBY,RSM_Name,MOM_Name,MOA_Name,Agent_Name,TARGETM_Y1_ren_Coll_premium) VALUES(?,?,?,?,?,?,?)";
            qry1[7] = "INSERT INTO PRG_FACT_MEASURE_DET_54(TARGET_ID,VIEWBY,RSM_Name,MOM_Name,MOA_Name,Agent_Name,TARGETM_Y2_ren_Coll_premium) VALUES(?,?,?,?,?,?,?)";
            try {
                con.setAutoCommit(false);
                for (int i = 0; i < qry1.length; i++) {
                    pStmt = con.prepareStatement(qry1[i]);
                    for (int j = 1; j < dataSheet.getRows(); j++) {
                        //  
                        if (i == 6) {
                            pStmt.setInt(1, 53);
                        }
                        if (i == 7) {
                            pStmt.setInt(1, 54);
                        } else {
                            pStmt.setInt(1, 39 + i);
                        }

                        pStmt.setString(2, EtlLoadConstants.VIEW_BY_AGENT);
                        if (dataSheet.getCell(0, j).getType() != CellType.EMPTY) {
                            pStmt.setString(3, dataSheet.getCell(0, j).getContents());
                        } else {
                            pStmt.setNull(3, Types.NULL);
                        }
                        if (dataSheet.getCell(1, j).getType() != CellType.EMPTY) {
                            pStmt.setString(4, dataSheet.getCell(1, j).getContents());
                        } else {
                            pStmt.setNull(4, Types.NULL);
                        }
                        if (dataSheet.getCell(2, j).getType() != CellType.EMPTY) {
                            pStmt.setString(5, dataSheet.getCell(2, j).getContents());
                        } else {
                            pStmt.setNull(5, Types.NULL);
                        }
                        if (dataSheet.getCell(3, j).getType() != CellType.EMPTY) {
                            pStmt.setString(6, dataSheet.getCell(3, j).getContents());
                        } else {
                            pStmt.setNull(6, Types.NULL);
                        }
                        if (dataSheet.getCell(i + 4, j).getType() != CellType.EMPTY) {
                            pStmt.setString(7, dataSheet.getCell(i + 4, j).getContents());
                        } else {
                            pStmt.setNull(7, Types.NULL);
                        }

                        pStmt.addBatch();
                    }
                    pStmt.executeBatch();
                    pStmt.clearBatch();
                }
                con.commit();
                pStmt.close();
                pStmt = null;
                con.close();
            } catch (Exception e) {
//                ProgenLog.log(ProgenLog.SEVERE, this, "loadTargetData", "Exception loadTargetData " + e.getMessage());
                logger.error("Exception loadTargetData ", e);
                return EtlLoadConstants.INSERTION_ERROR;
            }


        }
//        ProgenLog.log(ProgenLog.FINE, this, "loadTargetData", "Exit loadTargetData");
        logger.info("Exit loadTargetData");
        return EtlLoadConstants.UPLOAD_SUCCESS;
    }

    private int rowsToUpload(Sheet dataSheet) {
        return dataSheet.getRows();
    }

    public PbReturnObject extractData(EtlLoadTable loadTable, String stdate1, String enddate1, int fromRow, int toRow) {
//        ProgenLog.log(ProgenLog.FINE, this, "extractData", "Enter extractData");
        logger.info("Enter extractData");
        Sheet dataSheet = wb.getSheet(0);
        PbReturnObject pbro = new PbReturnObject();
        String qry = "select * from " + loadTable.getLoadTable();
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(qry);
            ResultSetMetaData rsmd = rs.getMetaData();
            int colCount = rsmd.getColumnCount();
            String colNames[] = new String[colCount];
            for (int i = 0; i < colCount; i++) {
                colNames[i] = rsmd.getColumnName(i + 1);
            }
            pbro.setColumnNames(colNames);
            //  
            for (int j = fromRow; j < toRow; j++) {
                for (int i = 0; i < dataSheet.getColumns(); i++) {
                    Cell cell = dataSheet.getCell(i, j);
                    CellType type = cell.getType();
                    // 
                    pbro.setFieldValue(colNames[i], dataSheet.getCell(i, j).getContents());
                }

                if (loadTable.isCheckDateLoad()) {
                    pbro.setFieldValue("st_date", stdate1);
                    pbro.setFieldValue("end_date", enddate1);
                }
                pbro.addRow();
            }
        } catch (Exception e) {
//            ProgenLog.log(ProgenLog.SEVERE, this, "extractData", "Exception extractData " + e.getMessage());
            logger.error("Exception extractData ", e);
            pbro = null;
        }
//        ProgenLog.log(ProgenLog.FINE, this, "extractData", "Exit extractData");
        logger.info("Exit extractData");
        return pbro;
    }

    private Workbook loadWorkbook(String path) {
//        ProgenLog.log(ProgenLog.FINE, this, "loadWorkbook", "Enter loadWorkbook");
        logger.info("Enter loadWorkbook");
        Workbook workbook;
        try {
            File inputWorkbook = new File(path);
            workbook = Workbook.getWorkbook(inputWorkbook);

        } catch (IOException ex) {
//            ProgenLog.log(ProgenLog.SEVERE, this, "loadWorkbook", "Exception loadWorkbook " + ex.getMessage());
            logger.error("Exception loadWorkbook ", ex);
            workbook = null;
        } catch (BiffException ex) {
//            ProgenLog.log(ProgenLog.SEVERE, this, "loadWorkbook", "Exception loadWorkbook " + ex.getMessage());
            logger.error("Exception loadWorkbook ", ex);
            workbook = null;
        }
//        ProgenLog.log(ProgenLog.FINE, this, "loadWorkbook", "Exit loadWorkbook");
        logger.info("Exit loadWorkbook");
        return workbook;
    }

    private ResultSetMetaData getLoadTableMetadata(String tableName, int connectionId) {
//        ProgenLog.log(ProgenLog.FINE, this, "getLoadTableMetadata", "Enter getLoadTableMetadata");
        logger.info("Enter getLoadTableMetadata");
        ResultSet rs;
        ResultSetMetaData rsmd;
        Statement stmt;
        String qry = "select * from " + tableName + "";
        try {
            stmt = con.createStatement();
            rs = stmt.executeQuery(qry);
            rsmd = rs.getMetaData();
        } catch (SQLException e) {
//            ProgenLog.log(ProgenLog.SEVERE, this, "getLoadTableMetadata", "Exception getLoadTableMetadata");
            logger.error("Exception getLoadTableMetadata ", e);
            return null;
        }
//        ProgenLog.log(ProgenLog.FINE, this, "getLoadTableMetadata", "Exit getLoadTableMetadata");
        logger.info("Exit getLoadTableMetadata");
        return rsmd;
    }

    private String validateSheetWithTable(EtlLoadTable loadTable, Sheet dataSheet) {
//        ProgenLog.log(ProgenLog.FINE, this, "validateSheetWithTable", "Enter validateSheetWithTable");
        logger.info("Enter validateSheetWithTable");
        ResultSetMetaData rsmd = this.getLoadTableMetadata(loadTable.getLoadTable(), loadTable.getConnectionId());
        int colCountSheet;
        int colCountTable;
        if (rsmd == null) {
            return EtlLoadConstants.EXCEL_CANNOT_VALIDATE_SHEET + " " + loadTable.getLoadTable();
        }
        colCountSheet = dataSheet.getColumns();
        try {
            colCountTable = rsmd.getColumnCount();
        } catch (SQLException ex) {
//            ProgenLog.log(ProgenLog.SEVERE, this, "validateSheetWithTable", "Exception validateSheetWithTable " + ex.getMessage());
            logger.error("Exception validateSheetWithTable ", ex);
            return EtlLoadConstants.EXCEL_CANNOT_VALIDATE_SHEET + " " + loadTable.getLoadTable();
        }

        if (loadTable.isCheckDateLoad()) {
            colCountSheet += 2;
        }

        if (colCountSheet == colCountTable) {
            if (loadTable.isCheckDateLoad()) {
                colCountSheet -= 2;
            }
//            ProgenLog.log(ProgenLog.FINE, this, "validateSheetWithTable", "Exit validateSheetWithTable");
            logger.info("Exit validateSheetWithTable");
            return this.validateSheetColumnsWithTable(rsmd, dataSheet, colCountSheet);
        } else {
//            ProgenLog.log(ProgenLog.FINE, this, "validateSheetWithTable", "Exit validateSheetWithTable");
            logger.info("Exit validateSheetWithTable");
            return EtlLoadConstants.EXCEL_COLUMN_COUNT_VALIDATE_SHEET + " " + loadTable;
        }
    }

    private boolean doesColumnHaveSingleDT(Sheet sheet, int column) {
//        ProgenLog.log(ProgenLog.FINE, this, "doesColumnHaveSingleDT", "Enter doesColumnHaveSingleDT");
        logger.info("Enter doesColumnHaveSingleDT");
        CellType oldCellType = CellType.EMPTY;
        CellType cellType;
        Cell dataCell;
        for (int i = 1; i < sheet.getRows(); i++) {
            dataCell = sheet.getCell(column, i);
            if (!(dataCell instanceof BlankCell)) {
                cellType = dataCell.getType();
                if (cellType != CellType.EMPTY) {
                    if (oldCellType == CellType.EMPTY) {
                        oldCellType = cellType;
                    } else if (!oldCellType.equals(cellType)) {
                        return false;
                    }
                }
            }
        }
//        ProgenLog.log(ProgenLog.FINE, this, "doesColumnHaveSingleDT", "Exit doesColumnHaveSingleDT");
        logger.info("Exit doesColumnHaveSingleDT");
        return true;
    }

    private String validateSheetColumnsWithTable(ResultSetMetaData rsMetadata, Sheet dataSheet, int colCount) {
//        ProgenLog.log(ProgenLog.FINE, this, "validateSheetColumnsWithTable", "Enter validateSheetColumnsWithTable");
        logger.info("Enter validateSheetColumnsWithTable");
        boolean isSheetValid = true;
        int i;
        try {
            for (i = 0; i < colCount; i++) {
                if (doesColumnHaveSingleDT(dataSheet, i) == false) {
                    return EtlLoadConstants.EXCEL_COLUMN_INVALID + " " + rsMetadata.getColumnName(i + 1);
                }

                if (rsMetadata.getColumnTypeName(i + 1).equalsIgnoreCase("VARCHAR")) {
                    if (getColumnDataType(dataSheet, i) != CellType.EMPTY && getColumnDataType(dataSheet, i) != CellType.LABEL) {
                        isSheetValid = false;
                    }
                } else if (rsMetadata.getColumnTypeName(i + 1).equalsIgnoreCase("NUMERIC")) {
                    if (getColumnDataType(dataSheet, i) != CellType.EMPTY && getColumnDataType(dataSheet, i) != CellType.NUMBER) {
                        isSheetValid = false;
                    }
                } else if (rsMetadata.getColumnTypeName(i + 1).equalsIgnoreCase("DATETIME")) {
                    if (getColumnDataType(dataSheet, i) != CellType.EMPTY && getColumnDataType(dataSheet, i) != CellType.DATE) {
                        isSheetValid = false;
                    }
                }
                if (isSheetValid == false) {
                    break;
                }
            }

            if (!isSheetValid) {
                return EtlLoadConstants.EXCEL_COLUMN_DATA_TYPE_INVALID + " " + rsMetadata.getColumnName(i + 1);
            }
        } catch (SQLException e) {
            isSheetValid = false;
//            ProgenLog.log(ProgenLog.SEVERE, this, "validateSheetColumnsWithTable", "Exception validateSheetColumnsWithTable " + e.getMessage());
            logger.error("Exception validateSheetColumnsWithTable ", e);
            return EtlLoadConstants.EXCEL_CANNOT_VALIDATE_SHEET;
        }
//        ProgenLog.log(ProgenLog.FINE, this, "validateSheetColumnsWithTable", "Exit validateSheetColumnsWithTable");
        logger.info("Exit validateSheetColumnsWithTable");
        return EtlLoadConstants.EXCEL_SHEET_VALID;
    }

    private CellType getColumnDataType(Sheet sheet, int column) {
//        ProgenLog.log(ProgenLog.FINE, this, "getColumnDataType", "Enter getColumnDataType");
        logger.info("Enter getColumnDataType");
        CellType cellType = CellType.EMPTY;
        Cell dataCell;
        for (int i = 1; i < sheet.getRows(); i++) {
            dataCell = sheet.getCell(column, i);
            if (!(dataCell instanceof BlankCell)) {
                cellType = dataCell.getType();
                break;
            }
        }
//        ProgenLog.log(ProgenLog.FINE, this, "getColumnDataType", "Exit getColumnDataType");
        logger.info("Exit getColumnDataType");
        return cellType;
    }
}
