/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.xlupdateAction;

import java.io.Serializable;
import java.util.ListResourceBundle;

/**
 *
 * @author Administrator
 */
public class ConnectionResourceBundle extends ListResourceBundle implements Serializable {

    @Override
    protected Object[][] getContents() {
        return contents;
    }
    static final Object contents[][] = {
        {"Downloadexl", "select &,& FROM & "}, {"getColumns", "select * from user_tab_cols where table_name=upper('&') order by column_name"}, {"getColumnsvalues", "select & FROM PRG_KNP_COMP_DIM1"}, {"upDateColumns", "update &  set & = '&' WHERE & ='&'"}
    };
}
