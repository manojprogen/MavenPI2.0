/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.display;

/**
 *
 * @author db2admin
 */
import java.io.Serializable;
import java.util.ListResourceBundle;

public class DisplayParametersResourceBundle extends ListResourceBundle implements Serializable {

    protected Object[][] getContents() {
        return contents;
    }
    static final Object contents[][] = {
        {"getParameterQuery1", "select BUSS_COL_NAME , BUSS_TABLE_ID , DISP_NAME from PRG_USER_ALL_INFO_DETAILS "
            + "where element_id ='&'"}, {"getParameterQuery2", "select d.BUSS_COL_NAME , d.BUSS_TABLE_ID ,d.DISP_NAME,m.DB_QUERY,d.USER_COL_TYPE,d.BUSS_TABLE_NAME,d.BUSS_COL_NAME from PRG_USER_ALL_INFO_DETAILS d,"
            + "prg_grp_buss_table m where d.element_id ='&' and d.BUSS_TABLE_ID =m.BUSS_TABLE_ID"}
    };
}
