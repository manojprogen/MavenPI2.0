/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.reportview.action;

import java.io.Serializable;
import java.util.ListResourceBundle;

/**
 * /**
 *
 * @author Saurabh
 */
public class CustomMeasureResourceBundle extends ListResourceBundle implements Serializable {

    @Override
    protected Object[][] getContents() {
        return contents;
    }
    static final Object contents[][] = {
        {"addUserFolderDetail", "INSERT INTO  PRG_USER_FOLDER_DETAIL(FOLDER_ID, SUB_FOLDER_ID, SUB_FOLDER_NAME, SUB_FOLDER_TYPE) values(&,&,'&','&')"}, {"addSubFolderElements", "INSERT INTO PRG_USER_SUB_FOLDER_ELEMENTS(ELEMENT_ID, SUB_FOLDER_ID, BUSS_TABLE_ID, BUSS_COL_ID,"
            + "BUSS_COL_NAME, USER_COL_NAME, USER_COL_DESC, USER_COL_TYPE, SUB_FOLDER_TAB_ID,"
            + "REF_ELEMENT_ID, REF_ELEMENT_TYPE, DEFAULT_AGGREGATION, FOLDER_GROUP, USE_REPORT_FLAG, reffered_elements,display_formula) "
            + "values(&,&,&,&,'&','&','&','&',&,&,'&','&','&','&','&','&') "} //        ,{"updateSubFolderElements","UPDATE PRG_USER_SUB_FOLDER_ELEMENTS SET BUSS_COL_NAME='&',USER_COL_NAME ='&',USER_COL_DESC='&',USER_COL_TYPE='&',DEFAULT_AGGREGATION='&',reffered_elements='&',display_formula='&' where ELEMENT_ID=&"}
        //      modified by Nazneen
        //        ,{"updateSubFolderElements","UPDATE PRG_USER_SUB_FOLDER_ELEMENTS SET BUSS_COL_NAME='&',USER_COL_NAME ='&',USER_COL_DESC='&',USER_COL_TYPE='&',DEFAULT_AGGREGATION='&',reffered_elements='&',display_formula='&' where REF_ELEMENT_ID=& AND REF_ELEMENT_TYPE=&"}
        , {"updateSubFolderElements", "UPDATE PRG_USER_SUB_FOLDER_ELEMENTS SET USER_COL_DESC='&',USER_COL_TYPE='&',DEFAULT_AGGREGATION='&',reffered_elements='&',display_formula='&' where REF_ELEMENT_ID=& AND REF_ELEMENT_TYPE=&"}, {"addUserAllInfoDets", "INSERT INTO PRG_USER_ALL_INFO_DETAILS(GRP_ID, FOLDER_ID, FOLDER_NAME, SUB_FOLDER_ID, SUB_FOLDER_NAME, "
            + "SUB_FOLDER_TYPE, SUB_FOLDER_TAB_ID, IS_DIMENSION, IS_FACT, IS_BUCKET, DISP_NAME, QRY_DIM_ID,  "
            + "DIM_ID, DIM_TAB_ID, DIM_NAME, ELEMENT_ID, BUSS_TABLE_ID, BUSS_COL_ID, BUSS_COL_NAME, USER_COL_NAME,  "
            + "USER_COL_DESC, USER_COL_TYPE, REF_ELEMENT_ID, REF_ELEMENT_TYPE, MEMBER_ID, MEMBER_NAME, DENOM_QUERY,  "
            + "BUSS_TABLE_NAME, CONNECTION_ID, AGGREGATION_TYPE, ACTUAL_COL_FORMULA,reffered_elements,display_formula,TABLE_DISP_NAME,TABLE_TOOLTIP_NAME,PREPOST,ref_dim_tab_id,ref_dim_element_id) values "
            + "(&,&,'&',&,'&','&',&,'&','&','&','&',&,&,&,'&',&,&,&,'&','&','&','&',&,'&',&,'&','&','&','&','&','&','&','&','&','&','&','&','&') "} //        ,{"updateUserAllInfoDetails","UPDATE PRG_USER_ALL_INFO_DETAILS SET BUSS_COL_NAME='&',USER_COL_NAME='&',USER_COL_DESC='&',USER_COL_TYPE='&',AGGREGATION_TYPE='&',ACTUAL_COL_FORMULA='&',reffered_elements='&',display_formula='&' where ELEMENT_ID=&"}
        //modified by Nazneen
        //        ,{"updateUserAllInfoDetails","UPDATE PRG_USER_ALL_INFO_DETAILS SET BUSS_COL_NAME='&',USER_COL_NAME='&',USER_COL_DESC='&',USER_COL_TYPE='&',AGGREGATION_TYPE='&',ACTUAL_COL_FORMULA='&',reffered_elements='&',display_formula='&' where REF_ELEMENT_ID=& AND REF_ELEMENT_TYPE=&"}
        , {"updateUserAllInfoDetails", "UPDATE PRG_USER_ALL_INFO_DETAILS SET USER_COL_DESC='&',USER_COL_TYPE='&',AGGREGATION_TYPE='&',ACTUAL_COL_FORMULA='&',reffered_elements='&',display_formula='&',prepost='&' where REF_ELEMENT_ID=& AND REF_ELEMENT_TYPE=&"} //added by Nazneen
        , {"getDBTableColumn", "SELECT b.db_table_name, c.table_col_name, a.USER_COL_TYPE,a.ACTUAL_COL_FORMULA FROM PRG_USER_ALL_INFO_DETAILS a, prg_grp_buss_table_master_view b,"
            + " prg_grp_buss_table_dtls_view c WHERE a.ELEMENT_ID = & and c.buss_table_id = a.buss_table_id and b.buss_table_id = a.buss_table_id"
            + " and c.buss_column_id = a.buss_col_id"}
    };
}
