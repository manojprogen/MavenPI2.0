/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.query;

import java.io.Serializable;
import java.util.ListResourceBundle;

/**
 *
 * @author db2admin
 */
public class PbCrossTabQueryResourceBundle extends ListResourceBundle implements Serializable {

    protected Object[][] getContents() {
        return contents;
    }
    static final Object contents[][] = {
        {"getConnectionIdByElementId", "select DISTINCT a.USER_NAME,  a.PASSWORD,  a.SERVER,  a.SERVICE_ID,  a.SERVICE_NAME,  a.PORT, a.DSN_NAME, a.DB_CONNECTION_TYPE  "
            + "from PRG_USER_CONNECTIONS a,PRG_USER_ALL_INFO_DETAILS b  where a.connection_id=b.connection_id and b.element_id='&'"}
    };
}
