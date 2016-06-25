/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.etl;

//import com.progen.log.ProgenLog;
import java.util.Date;
import org.apache.log4j.Logger;
import prg.db.PbReturnObject;

/**
 *
 * @author Administrator
 */
public class EtlLoadTable {

    private int etlSetupId;
    private int connectionId;
    private String dbTable;
    private String dbTableDispName;
    private String etlSource;
    private String etlFilePath;
    private boolean incrementalLoad;
    private boolean incrementalDateLoad;
    private boolean checkDateLoad;
    private String incrementDateCol;
    private Date lastLoadDate;
    public static Logger logger = Logger.getLogger(EtlLoadTable.class);

    public EtlLoadTable(PbReturnObject etlLoad) {
//        ProgenLog.log(ProgenLog.FINE, this, "EtlLoadTable", "Enter EtlLoadTable");
        logger.info("Enter EtlLoadTable");
        this.etlSetupId = etlLoad.getFieldValueInt(0, "ETL_SETUP_ID");
        this.connectionId = etlLoad.getFieldValueInt(0, "CONNECTION_ID");
        this.dbTable = etlLoad.getFieldValueString(0, "DB_TABLE");
        this.dbTableDispName = etlLoad.getFieldValueString(0, "DB_TABLE_DSP_NAME");
        this.etlSource = etlLoad.getFieldValueString(0, "ETL_SOURCE");
        this.etlFilePath = etlLoad.getFieldValueString(0, "ETL_FILE_PATH");
        if (etlLoad.getFieldValueString(0, "INCREMENTAL_LOAD") != null && "Y".equalsIgnoreCase(etlLoad.getFieldValueString(0, "INCREMENTAL_LOAD"))) {
            this.incrementalLoad = true;
        } else {
            this.incrementalLoad = false;
        }

        if (etlLoad.getFieldValueString(0, "CHECK_DATE") != null && "Y".equalsIgnoreCase(etlLoad.getFieldValueString(0, "CHECK_DATE"))) {
            this.checkDateLoad = true;
        } else {
            this.checkDateLoad = false;
        }

        if (etlLoad.getFieldValueString(0, "INCREMENTAL_DATE_LOAD") != null && "Y".equalsIgnoreCase(etlLoad.getFieldValueString(0, "INCREMENTAL_DATE_LOAD"))) {
            this.incrementalDateLoad = true;
        } else {
            this.incrementalDateLoad = false;
        }

        this.incrementDateCol = etlLoad.getFieldValueString(0, "INCR_DATE_COLUMN");
        this.lastLoadDate = etlLoad.getFieldValueDate(0, "LAST_LOAD_DATE");
//        ProgenLog.log(ProgenLog.FINE, this, "EtlLoadTable", "Exit EtlLoadTable");
        logger.info("Exit EtlLoadTable");
    }

    public int getConnectionId() {
        return this.connectionId;
    }

    public int getEtlSetupId() {
        return this.etlSetupId;
    }

    public boolean isIncrementalLoad() {
        return this.incrementalLoad;
    }

    public boolean isIncrementalDateLoad() {
        return this.incrementalDateLoad;
    }

    public boolean isCheckDateLoad() {
        return this.checkDateLoad;
    }

    public Date getLastUploadDate() {
        return this.lastLoadDate;
    }

    public String getIncrementalDateColumn() {
        return this.incrementDateCol;
    }

    public String getLoadTable() {
        return this.dbTable;
    }

    public String getLoadTableDisplayName() {
        return this.dbTableDispName;
    }

    public String getSourceType() {
        return this.etlSource;
    }

    public String getEtlFilePath() {
        return this.etlFilePath;
    }
}
