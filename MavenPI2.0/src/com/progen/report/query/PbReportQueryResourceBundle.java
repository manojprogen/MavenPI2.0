/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.query;

import java.io.Serializable;
import java.util.ListResourceBundle;

/**
 * @filename PbReportQueryResourceBundle
 *
 * @author santhosh.kumar@progenbusiness.com @date Sep 16, 2009, 12:33:07 PM
 */
public class PbReportQueryResourceBundle extends ListResourceBundle implements Serializable {

    protected Object[][] getContents() {
        return contents;
    }
    static final Object contents[][] = {
        {"generateViewByQry1", "select distinct BUSS_TABLE_ID,BUSS_TABLE_NAME,BUSS_COL_NAME,USER_COL_NAME,denom_query,ELEMENT_ID,"
            + "REF_ELEMENT_ID, REF_ELEMENT_TYPE , ACTUAL_COL_FORMULA, USER_COL_DESC,USER_COL_TYPE,member_name , DIM_ID,  DIM_TAB_ID,  DIM_NAME,   MEMBER_ID from prg_user_all_info_details  where ELEMENT_ID in (&)  order by BUSS_TABLE_ID,"
            + " REF_ELEMENT_ID, REF_ELEMENT_TYPE "} /*
         * , {"generateViewByQry2", "select distinct
         * BUSS_TABLE_ID,BUSS_TABLE_NAME, BUSS_COL_NAME, USER_COL_NAME,
         * denom_query, ELEMENT_ID," + "REF_ELEMENT_ID,REF_ELEMENT_TYPE,
         * ACTUAL_COL_FORMULA, USER_COL_DESC, USER_COL_TYPE from
         * prg_user_all_info_details where ( ELEMENT_ID in (& ) or ELEMENT_ID in
         * " + "( select distinct REF_ELEMENT_ID from prg_user_all_info_details
         * where ELEMENT_ID in (& ))) and USE_REPORT_FLAG = 'Y' order by
         * BUSS_TABLE_ID," + " REF_ELEMENT_ID, REF_ELEMENT_TYPE"}
        *
         */, {"generateViewByQry2", "select  BUSS_TABLE_ID,BUSS_TABLE_NAME, BUSS_COL_NAME, USER_COL_NAME, denom_query, ELEMENT_ID,"
            + "REF_ELEMENT_ID,REF_ELEMENT_TYPE, ACTUAL_COL_FORMULA, USER_COL_DESC, USER_COL_TYPE ,AGGREGATION_TYPE, ref_dim_tab_id,IS_TIME_SUMM  ,TIME_SUMM_LEVEL,SUMM_OTHER_DIM  , DIM_ID,  DIM_TAB_ID,  DIM_NAME,   MEMBER_ID,   MEMBER_NAME , REF_DIM_ELEMENT_ID , CALANDER_TABLE_NAME ,  CALANDER_NAME , DATE_DETAILS,DATE_ELEMENTID,DATE_OPTION from ( select  BUSS_TABLE_ID,BUSS_TABLE_NAME, BUSS_COL_NAME, USER_COL_NAME, denom_query, ELEMENT_ID,"
            + "REF_ELEMENT_ID,REF_ELEMENT_TYPE, ACTUAL_COL_FORMULA, USER_COL_DESC, USER_COL_TYPE,AGGREGATION_TYPE ,ref_dim_tab_id,IS_TIME_SUMM  ,TIME_SUMM_LEVEL,SUMM_OTHER_DIM  , DIM_ID,  DIM_TAB_ID,  DIM_NAME,   MEMBER_ID,   MEMBER_NAME, REF_DIM_ELEMENT_ID , CALANDER_TABLE_NAME ,  CALANDER_NAME , DATE_DETAILS,DATE_ELEMENTID,DATE_OPTION  from prg_user_all_info_details  where ELEMENT_ID in (& ) "
            + " union select  BUSS_TABLE_ID,BUSS_TABLE_NAME, BUSS_COL_NAME, USER_COL_NAME, denom_query, ELEMENT_ID,"
            + "REF_ELEMENT_ID,REF_ELEMENT_TYPE, ACTUAL_COL_FORMULA, USER_COL_DESC, USER_COL_TYPE,AGGREGATION_TYPE,ref_dim_tab_id,IS_TIME_SUMM  ,TIME_SUMM_LEVEL,SUMM_OTHER_DIM  , DIM_ID,  DIM_TAB_ID,  DIM_NAME,   MEMBER_ID,   MEMBER_NAME, REF_DIM_ELEMENT_ID , CALANDER_TABLE_NAME ,  CALANDER_NAME , DATE_DETAILS,DATE_ELEMENTID,DATE_OPTION  from prg_user_all_info_details where ELEMENT_ID in "
            + "( select distinct REF_ELEMENT_ID from  prg_user_all_info_details  where ELEMENT_ID in (& )) "
            + " union select  BUSS_TABLE_ID,BUSS_TABLE_NAME, BUSS_COL_NAME, USER_COL_NAME, denom_query, ELEMENT_ID,"
            + "REF_ELEMENT_ID,REF_ELEMENT_TYPE, ACTUAL_COL_FORMULA, USER_COL_DESC, USER_COL_TYPE,AGGREGATION_TYPE,ref_dim_tab_id,IS_TIME_SUMM  ,TIME_SUMM_LEVEL,SUMM_OTHER_DIM  , DIM_ID,  DIM_TAB_ID,  DIM_NAME,   MEMBER_ID,   MEMBER_NAME , REF_DIM_ELEMENT_ID , CALANDER_TABLE_NAME ,  CALANDER_NAME , DATE_DETAILS,DATE_ELEMENTID,DATE_OPTION  from prg_user_all_info_details where ELEMENT_ID in "
            + "( select ELEMENT_ID from prg_user_all_info_details  where REF_ELEMENT_TYPE =2 and  REF_ELEMENT_ID in ( select distinct REF_ELEMENT_ID from  prg_user_all_info_details  where ELEMENT_ID in (& ) and REF_ELEMENT_TYPE in (3,4))) "
            + ") ou2 order by BUSS_TABLE_ID,"
            + " REF_ELEMENT_ID, REF_ELEMENT_TYPE"}, {"getConnectionIdByElementId", "select DISTINCT a.USER_NAME,  a.PASSWORD,  a.SERVER,  a.SERVICE_ID,  a.SERVICE_NAME,  a.PORT, a.DSN_NAME, a.DB_CONNECTION_TYPE  "
            + "from PRG_USER_CONNECTIONS a,PRG_USER_ALL_INFO_DETAILS b  where a.connection_id=b.connection_id and b.element_id='&'"}, {"generateCrossTabMeasure", "select distinct BUSS_TABLE_ID,BUSS_TABLE_NAME, BUSS_COL_NAME, USER_COL_NAME, denom_query, ELEMENT_ID,REF_ELEMENT_ID,"
            + "REF_ELEMENT_TYPE , ACTUAL_COL_FORMULA, USER_COL_DESC,AGGREGATION_TYPE from prg_user_all_info_details  where ELEMENT_ID in (& )   order by BUSS_TABLE_ID, REF_ELEMENT_ID, REF_ELEMENT_TYPE"}
    };
}
