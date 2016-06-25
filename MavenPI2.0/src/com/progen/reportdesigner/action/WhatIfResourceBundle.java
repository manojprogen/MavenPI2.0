/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.reportdesigner.action;

import java.io.Serializable;
import java.util.ListResourceBundle;

/**
 * /**
 *
 * @author Saurabh
 */
public class WhatIfResourceBundle extends ListResourceBundle implements Serializable {

    protected Object[][] getContents() {
        return contents;
    }
    static final Object contents[][] = {
        {"getReportParamDetails", "select   disp_name,element_id,dim_id, dim_tab_id,buss_table_id,'' childElementId,'' dispseq,  'MultiSelectBox(With All)' displayType,'All'  defaultValue,'' as relLevel "
            + "from prg_user_all_info_details  where element_id in (&) order by decode (element_id,&)"}, {"getReportQueryDetails", "select  USER_COL_DESC,element_id,REF_ELEMENT_ID,FOLDER_ID,SUB_FOLDER_ID,AGGREGATION_TYPE,USER_COL_TYPE "
            + "from prg_user_all_info_details  where element_id in (&)"}
    };
}