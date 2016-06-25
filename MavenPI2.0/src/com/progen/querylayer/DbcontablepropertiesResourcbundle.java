/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.querylayer;

import java.io.Serializable;
import java.util.ListResourceBundle;

/**
 *
 * @author Administrator
 */
public class DbcontablepropertiesResourcbundle extends ListResourceBundle implements Serializable {

    protected Object[][] getContents() {
        return contents;
    }
    static final Object contents[][] = {
        {"gettabledata", "select TABLE_ALIAS, TABLE_NAME from prg_db_master_table where table_id in(select table_id2 from prg_db_table_rlt_master where table_id='&' union select table_id from prg_db_table_rlt_master where table_id2='&')"}, {"gettablename", "select TABLE_NAME from prg_db_master_table where table_id = '&'"}, {"gettalbedescription", "select TABLE_COL_NAME,COL_TYPE,COL_LENGTH  from prg_db_master_table_details where TABLE_ID='&'"}
    };
}
