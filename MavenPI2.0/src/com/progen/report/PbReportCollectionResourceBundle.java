/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report;

import java.io.Serializable;
import java.util.ListResourceBundle;

/**
 *
 * @author db2admin
 */
public class PbReportCollectionResourceBundle extends ListResourceBundle implements Serializable {

    protected Object[][] getContents() {
        return contents;
    }
    static final Object contents[][] = {
        {"getParamMetaDataQuery1", "select ELEMENT_ID,PARAM_DISP_NAME,CHILD_ELEMENT_ID,DIM_ID,DIM_TAB_ID,"
            + "DISPLAY_TYPE,REL_LEVEL,DISP_SEQ_NO,DEFAULT_VALUE,PARAMETER_TYPE,DATE_TIME_OPTION,DATA_SUBSTRING,DATE_FORMAT,NOT_IN,LIKE_V,NOT_LIKE   from PRG_AR_REPORT_PARAM_DETAILS "
            + " where REPORT_ID = '&'  order by DISP_SEQ_NO,PARAM_DISP_NAME"}//modified by santhosh.k on 09-03-2010 for the purpose of parameter grouping
        , {"getParamMetaDataQuery2", " SELECT M.VIEW_BY_ID,  M.REPORT_ID, M.VIEW_BY_SEQ, M.ROW_SEQ,M.COL_SEQ, D.PARAM_DISP_ID , P.PARAM_DISP_NAME,P.ELEMENT_ID, "
            + "P.DIM_ID,  P.DIM_TAB_ID,P.BUSS_TABLE,P.CHILD_ELEMENT_ID, D.DISP_SEQ_NO ,M.DEFAULT_VALUE,M.CUSTOM_SEQ,M.HIDE_VIEBY FROM PRG_AR_REPORT_VIEW_BY_MASTER M , "
            + "PRG_AR_REPORT_VIEW_BY_DETAILS  D  , PRG_AR_REPORT_PARAM_DETAILS P where m.view_by_id = d.view_by_id and p.PARAM_DISP_ID = d. PARAM_DISP_ID and "
            + "m.report_id = '&' order by d.view_by_id, m.col_seq , m.row_seq, d.DISP_SEQ_NO  "}, {"getReportQueryInfo", " SELECT a.ELEMENT_ID, a.AGGREGATION_TYPE,  a.COL_DISP_NAME, b.report_name,b.report_desc,b.map_enabled,a.COLUMN_TYPE, b.custom_seq, b.transpose_format,b.goalseek_targetvalue,b.goalpercent_value,b.GOAL_TIME_INDIVAL,NEW_PRODUCT_LIST,b.REPORT_SELCTION_REPORT_DRILL,a.TYPE,c.REF_DIM_TAB_ID FROM PRG_AR_QUERY_DETAIL a,PRG_AR_REPORT_MASTER b,PRG_USER_ALL_INFO_DETAILS c WHERE a.report_id= b.report_id and a.ELEMENT_ID = c.ELEMENT_ID and b.report_id='&' AND a.SUMMERIZED_MSR_ENABLE='false' ORDER BY  a.qry_col_id, a.REF_ELEMENT_ID, a.ELEMENT_ID"} /*
         * , {"getReportQueryInfo", "SELECT ELEMENT_ID,
         * AGGREGATION_TYPE,COL_DISP_NAME FROM PRG_AR_QUERY_DETAIL WHERE
         * report_id='&' ORDER BY REF_ELEMENT_ID,ELEMENT_ID "}
         */ //       modifed by krishan for tooltip
        , {"getReportTableInfo", "SELECT parqd.element_id,PARTD. COL_NAME,CASE WHEN parqd.column_type='VARCHAR2' THEN 'C' WHEN parqd.column_type='NUMBER' THEN 'N' "
            + " when upper(parqd.column_type) = 'CALCULATED' THEN 'N' when upper(parqd.column_type) = 'SUMMARIZED' then 'N'   WHEN upper(parqd.column_type) = 'SUMMARISED'  "
            + "  THEN 'N'    WHEN upper(parqd.column_type) = 'CALUCULATED'    THEN 'N' when upper(parqd.column_type) = 'TIMECALUCULATED' THEN 'N' "
            + " WHEN parqd.column_type='DATE' THEN 'D' ELSE 'C' END AS COLUMN_TYPE,'T' as COLUMN_DISP_TYPES,PARTD.QRY_COL_ID  ,PARTD.SHOW_GRD_TOTAL "
            + ",PARTD.SHOW_SUB_TOTAL ,PARTD.SHOW_AVG ,PARTD.SHOW_MIN,PARTD.SHOW_MAX ,PARTD.SHOW_CAT_MIN,PARTD.SHOW_CAT_MAX,PARTD.APPEND_SYMBOL,PARTD.color_group,PARTD.dependent_cols,PARTD.run_time_formula "
            + "  ,PARTD.NUMBER_FORMAT,PARTD.ROUNDING_PRECISION, PARTD.RUNTIME_MEASURE_ID, PARTD.SHOW_INDICATOR, PARTD.SCRIPT_INDICATOR , PARTD.SCRIPT_ALIGN ,PARTD.MEASURE_TYPE , PARTD.MEASURE_ALIGN ,PARTD.TIME_CONVERSION,PARTD.DATE_TIME_OPTION,PARTD.DATA_SUBSTRING,PARTD.DATE_FORMAT,PARTD.REPORT_DRILL_ASSIGN_REPORT,PARTD.MULTI_REPORT_DRILL_ASSIGN,PARTD.HIDE_MSR,PARTD.SUBTOTAL_DEVIATION,PARTD.SUMMERIZED_MSR_ENABLE,PARTD.AVGZEROCOUNTTYPE,PARTD.CT_GT_AGG_TYPE,PARTD.NUM_FORMATE,PARQD.drill_message FROM PRG_AR_REPORT_TABLE_DETAILS PARTD , PRG_AR_QUERY_DETAIL PARQD WHERE parqd.qry_col_id(+) = partd.qry_col_id "
            + " AND partd.report_id='&' ORDER BY PARTD.disp_seq, PARTD.QRY_COL_ID  "} //changed order by becasue %wise meas don't have qry_col_id so were pushed to last always
        , {"getParameterTimeInfo", "select m.rep_time_id , m.time_level , m.time_type , d.column_type, d.column_name, d.sequence, d.form_sequence ,to_char(d.default_date,'MM/DD/YYYY'),d.default_value"
            + " from prg_ar_report_time m , prg_ar_report_time_detail d where m.rep_time_id = d.rep_time_id "
            + "and m.report_id='&' order by d.sequence "}, {"getConnectionIdByElementId", "select DISTINCT a.USER_NAME,  a.PASSWORD,  a.SERVER,  a.SERVICE_ID,  a.SERVICE_NAME,  a.PORT, a.DSN_NAME, a.DB_CONNECTION_TYPE  "
            + "from PRG_USER_CONNECTIONS a,PRG_USER_ALL_INFO_DETAILS b  where a.connection_id=b.connection_id and b.element_id='&'"}, {"getReportBizRoles", "select b.folder_id, b.folder_name from PRG_AR_REPORT_DETAILS a ,PRG_USER_FOLDER b where a.folder_id= b.folder_id and a.report_id='&'"}, {"getReportBizRoleName", "select folder_name from PRG_USER_FOLDER where folder_id='&'"}, {"getReportAOId", "select AO_ID, REP_CRETAED_ON from PRG_AR_REPORT_DETAILS where report_id='&'"} //addeed by santhosh.k on 05/01/2010 for getting report table master info for the purpose of table properties
        , {"getReportTableMasterInfo", "SELECT REP_TAB_ID, REPORT_ID, TABLE_NAME, TABLE_TYPE, TABLE_SEQ,case when SHOW_GRD_TOTAL='Y' then 'true' else 'false' END AS SHOW_GRD_TOTAL"
            + ",case when SHOW_SUB_TOTAL='Y' then 'true' else 'false'END AS SHOW_SUB_TOTAL,case when SHOW_AVG='Y' then 'true' else 'false'END AS SHOW_AVG"
            + ",case when SHOW_MAX='Y' then 'true' else 'false'END AS SHOW_MAX,case when SHOW_MIN='Y' then 'true' else 'false'END AS SHOW_MIN"
            + ",case when SHOW_CAT_MAX='Y' then 'true' else 'false'END AS SHOW_CAT_MAX,case when SHOW_CAT_MIN='Y' then 'true' else 'false'END AS SHOW_CAT_MIN,APPEND_SYMBOL,"
            + " DEFAULT_SORT_COLUMN,CASE WHEN SHOW_ADV_SEARCH='Y' then 'true' else 'false' end as SHOW_ADV_SEARCH,TABLE_DISPLAY_ROWS,TABLE_PROPERTIES "
            + ",case when DRILL_ACROSS='Y' then 'true' else 'false'END AS DRILL_ACROSS,case when DRILL_MEASURE='Y' then 'true' else 'false' END AS DRILL_MEASURE,case when SHOW_CATAVG='Y' then 'true' else 'false' END AS SHOW_CATAVG,SUMMERIZED_TABLE_MEASURE,CROSSTAB_MEASURE_MAP,GT_AVERAGE,TOPBOTTOM_VAL,SOURCETYPE,EXCEL_FILE_PATH FROM PRG_AR_REPORT_TABLE_MASTER where report_id='&' "}, {"selectWhatifQuery", "select * from PRG_AR_WHATIFS where report_id =&"}, {"getReportViewBys", "SELECT VIEW_BY_ID,VIEW_BY_SEQ, IS_ROW_EDGE, ROW_SEQ, COL_SEQ, DEFAULT_VALUE FROM PRG_AR_REPORT_VIEW_BY_MASTER WHERE REPORT_ID=& ORDER BY VIEW_BY_ID"}, {"getReportTimeMaster", "SELECT REP_TIME_ID,TIME_TYPE,TIME_LEVEL,REPORT_ID FROM PRG_AR_REPORT_TIME WHERE REPORT_ID=&"}, {"getReportTimeDetail", "SELECT REP_TIME_DTL_ID,COLUMN_NAME,COLUMN_TYPE,SEQUENCE,FORM_SEQUENCE,DEFAULT_DATE FROM PRG_AR_REPORT_TIME_DETAIL WHERE REP_TIME_ID=&"}, {"getRefDimTabId", "select REF_DIM_TAB_ID from PRG_USER_ALL_INFO_DETAILS where ELEMENT_ID='&' AND REF_DIM_TAB_ID is not null"}
    };
}
