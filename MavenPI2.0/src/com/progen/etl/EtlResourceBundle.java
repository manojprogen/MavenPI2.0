/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.etl;

import java.util.ListResourceBundle;

/**
 *
 * @author Administrator
 */
public class EtlResourceBundle extends ListResourceBundle {

    protected Object[][] getContents() {
        return contents;
    }
    static final Object contents[][] = {
        {"getEtlSetup", "select * from PRG_ETL_SETUP where DB_TABLE = '&'"},
        {"getExcelTableList", "Select DB_TABLE,DB_TABLE_DSP_NAME from PRG_ETL_SETUP where ETL_SOURCE = 'Excel'"},
        {"getAccessTableList", "Select DB_TABLE,DB_TABLE_DSP_NAME from PRG_ETL_SETUP where ETL_SOURCE = 'Access'"},
        {"lastLoadTime", "select LAST_LOAD_DATE from PRG_ETL_SETUP where DB_TABLE='&'"},
        {"checkDates", "select CONNECTION_ID,INCR_DATE_COLUMN,INCREMENTAL_DATE_LOAD,CHECK_DATE from PRG_ETL_SETUP WHERE DB_TABLE='&'"}
    };
}
