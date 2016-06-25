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
public class CustomMeasureResourceBundleSqlServer extends ListResourceBundle implements Serializable {

    @Override
    protected Object[][] getContents() {
        return contents;
    }
    static final Object contents[][] = {
        {"addUserFolderDetail", "INSERT INTO  PRG_USER_FOLDER_DETAIL(FOLDER_ID, SUB_FOLDER_ID, SUB_FOLDER_NAME, SUB_FOLDER_TYPE) values(&,&,'&','&')"}, {"addSubFolderElements", "INSERT INTO PRG_USER_SUB_FOLDER_ELEMENTS(SUB_FOLDER_ID, BUSS_TABLE_ID, BUSS_COL_ID,"
            + "BUSS_COL_NAME, USER_COL_NAME, USER_COL_DESC, USER_COL_TYPE, SUB_FOLDER_TAB_ID,"
            + "REF_ELEMENT_ID, REF_ELEMENT_TYPE, DEFAULT_AGGREGATION, USE_REPORT_FLAG, reffered_elements,display_formula) "
            + "values(&,&,&,'&','&','&','&',&,&,'&','&','&','&','&') "} //        ,{"updateSubFolderElements","UPDATE PRG_USER_SUB_FOLDER_ELEMENTS SET BUSS_COL_NAME='&',USER_COL_NAME ='&',USER_COL_DESC='&',USER_COL_TYPE='&',DEFAULT_AGGREGATION='&',reffered_elements='&',display_formula='&' where ELEMENT_ID=&"}
        //      //modified by Nazneen
        //        ,{"updateSubFolderElements","UPDATE PRG_USER_SUB_FOLDER_ELEMENTS SET BUSS_COL_NAME='&',USER_COL_NAME ='&',USER_COL_DESC='&',USER_COL_TYPE='&',DEFAULT_AGGREGATION='&',reffered_elements='&',display_formula='&' where REF_ELEMENT_ID=& AND REF_ELEMENT_TYPE=&"}
        , {"updateSubFolderElements", "UPDATE PRG_USER_SUB_FOLDER_ELEMENTS SET USER_COL_DESC='&',USER_COL_TYPE='&',DEFAULT_AGGREGATION='&',reffered_elements='&',display_formula='&' where REF_ELEMENT_ID=& AND REF_ELEMENT_TYPE=&"}, {"addUserAllInfoDets", "INSERT INTO PRG_USER_ALL_INFO_DETAILS(GRP_ID, FOLDER_ID, FOLDER_NAME, SUB_FOLDER_ID, SUB_FOLDER_NAME, "
            + "SUB_FOLDER_TYPE, SUB_FOLDER_TAB_ID, IS_DIMENSION, IS_FACT, IS_BUCKET, QRY_DIM_ID,  "
            + "DIM_ID, DIM_TAB_ID, ELEMENT_ID, BUSS_TABLE_ID, BUSS_COL_ID, BUSS_COL_NAME, USER_COL_NAME,  "
            + "USER_COL_DESC, USER_COL_TYPE, REF_ELEMENT_ID, REF_ELEMENT_TYPE, MEMBER_ID,   "
            + "BUSS_TABLE_NAME, CONNECTION_ID, AGGREGATION_TYPE, ACTUAL_COL_FORMULA,reffered_elements,display_formula,USE_REPORT_FLAG,DISP_NAME,TABLE_DISP_NAME,TABLE_TOOLTIP_NAME,PREPOST) values "
            + "(&,&,'&',&,'&','&',&,'&','&','&',&,&,&,&,&,&,'&','&','&','&',&,'&',&,'&',&,'&','&','&','&','&','&','&','&','&') "} //        ,{"updateUserAllInfoDetails","UPDATE PRG_USER_ALL_INFO_DETAILS SET BUSS_COL_NAME='&',USER_COL_NAME='&',USER_COL_DESC='&',USER_COL_TYPE='&',AGGREGATION_TYPE='&',ACTUAL_COL_FORMULA='&',reffered_elements='&',display_formula='&' where ELEMENT_ID=&"}
        //modified by Nazneen
        , {"updateUserAllInfoDetails", "UPDATE PRG_USER_ALL_INFO_DETAILS SET USER_COL_DESC='&',USER_COL_TYPE='&',AGGREGATION_TYPE='&',ACTUAL_COL_FORMULA='&',reffered_elements='&',display_formula='&',prepost='&' where REF_ELEMENT_ID=& AND REF_ELEMENT_TYPE=&"} //        ,{"getRefElId","SELECT IDENT_CURRENT('PRG_USER_SUB_FOLDER_ELEMENTS')"}
        //added by Nazneen
        , {"getRefElId", "SELECT REF_ELEMENT_ID FROM PRG_USER_SUB_FOLDER_ELEMENTS WHERE ELEMENT_ID= IDENT_CURRENT('PRG_USER_SUB_FOLDER_ELEMENTS')"} //            ,{"getUpdateForPrior","update PRG_USER_ALL_INFO_DETAILS set actual_col_formula = '&', reffered_elements = '&' where ref_element_id=& and ref_element_type =2"}
        //            ,{"getUpdateForChange","update PRG_USER_ALL_INFO_DETAILS set reffered_elements = '&' where ref_element_id=& and ref_element_type in(3,4)"}
        //added by nazneen
        , {"getDBTableColumn", "SELECT b.DB_TABLE_NAME, c.TABLE_COL_NAME,a.USER_COL_TYPE,a.ACTUAL_COL_FORMULA  FROM PRG_USER_ALL_INFO_DETAILS a, prg_grp_buss_table_master_view b,"
            + " prg_grp_buss_table_dtls_view c WHERE a.ELEMENT_ID = & and c.BUSS_TABLE_ID = a.BUSS_TABLE_ID and b.BUSS_TABLE_ID = a.BUSS_TABLE_ID"
            + " and c.BUSS_COLUMN_ID = a.BUSS_COL_ID"}
    };
}
