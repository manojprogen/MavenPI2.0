package com.progen.targetview.db;

import java.io.Serializable;
import java.util.ListResourceBundle;

public class PbTargetResourceBundle extends ListResourceBundle implements Serializable {

    @Override
    protected Object[][] getContents() {
        return contents;
    }
    static final Object contents[][] = {
        {"getSequenceNumber", "select &.nextval from dual"},
        {"getTargetTable", "select target_table_id,pgbt.buss_table_name,ptm.measure_name from prg_target_master ptm,prg_grp_buss_table"
            + " pgbt where prg_measure_id in (select measure_id from target_master WHERE target_master.target_id=&) "
            + " and pgbt.buss_table_id = ptm.target_table_id"},
        {"getElementIdsBussCols", "select distinct ptpd.element_id,param_disp_name,puaid.buss_col_name,target_id from prg_target_param_details "
            + " ptpd, prg_user_all_info_details puaid where target_id=& and ptpd.element_id = puaid.element_id "}, {"getTargetParamsInfo", "select * from prg_target_param_details where target_id=&"}, {"getTargetsListU", "select * from target_master where user_id=&"},
        {"addTargetMaster", "insert into TARGET_MASTER(TARGET_ID,MEASURE_ID,TARGET_NAME,MIN_TIME_LEVEL,USER_ID,"
            + " TARGET_DESC,TARGET_PRIM_PARAM,TARGET_START_DATE,TARGET_END_DATE,TARGET_STATUS,"
            + " TARGET_START_MONTH,TARGET_END_MONTH,TARGET_START_QTR,TARGET_END_QTR,TARGET_START_YEAR,TARGET_END_YEAR) "
            + " values('&','&','&','&','&','&','&','&','&','&','&','&','&','&','&','&')"},
        {"getRelationForMembers", "select * from prg_grp_dim_rel_details where rel_id in(select rel_id from prg_grp_dim_rel where dim_id in (select dim_id from prg_grp_dim_member where member_id in(&))"
            + " and prg_grp_dim_rel.is_default='Y') order by rel_id,rel_level"},
        {"getSequenceNumberU", "select PRG_TARGET_MASTER_SEQ.nextval from dual"},
        {"getDurationLovs", "select description,value from prg_value_lookup where key_value='&'"},
        {"getUserBusinessGroups", "select pubg.grp_id,pgm.grp_name from prg_user_buss_grps pubg,prg_grp_master pgm where user_id=& and pgm.grp_id=pubg.grp_id"},
        {"getAllMeasures", "select measure_name,prg_measure_id from prg_target_master where business_group in(&)"},
        {"getAllMonths", "select MON_NAME as view_by, MON_CODE as view_by1 from PRG_ACN_MON_DENOM order by cyear,cmon"},
        {"getAllQtrs", "select distinct cmqtr_code as view_by,cmqtr_code as view_by1,cyear from prg_acn_mon_denom order by cyear,cmqtr_code"},
        {"getAllYears", "select distinct cyear as view_by,cyear as view_by1 from prg_acn_mon_denom order by cyear"},
        {"getMeasureParameters", "select distinct ptmm.member_id,pgdm.member_name from prg_target_measure_members ptmm,prg_grp_dim_member pgdm "
            + "where measure_id=& and pgdm.member_id=ptmm.member_id order by 1"},
        {"getParameterNames", "select distinct member_id,member_name from prg_grp_dim_member where member_id in (&) order by 1"},
        {"getOtherDimElements", "select * from prg_target_param_details where target_id=& and dim_id not in(select dim_id from prg_target_param_details where "
            + " element_id=(select target_prim_param from target_master where target_id=&)) order by dim_id, rel_level"},
        /*
         * {"getAddTargetParamDetails","select distinct mem_id,element_id,
         * buss_col_id,pgdmdd.col_id,puaid.dim_id,puaid.buss_table_id,puaid.dim_tab_id
         * from" + " prg_user_all_info_details puaid, prg_grp_dim_member_details
         * pgdmdd where member_id in(&) and" + " puaid.buss_col_id =
         * pgdmdd.col_id and pgdmdd.col_type='KEY' and folder_id=(select
         * folder_id from prg_user_folder where grp_id" + " in(select
         * business_group from prg_target_master where prg_measure_id=&)) order
         * by mem_id"},
         */
        /*
         * {"getAddTargetParamDetails","select distinct mem_id,element_id,
         * buss_col_id,pgdmdd.col_id,puaid.dim_id,puaid.buss_table_id,puaid.dim_tab_id,"
         * + "pdm.member_name from prg_user_all_info_details puaid,
         * prg_grp_dim_member_details pgdmdd, prg_grp_dim_member pdm where" + "
         * pdm.member_id in(&) and puaid.buss_col_id = pgdmdd.col_id and
         * pgdmdd.col_type='KEY' and folder_id " + "in(select bus_folder_id from
         * target_measure_folder where bus_group_id in(select business_group
         * from prg_target_master" + " where prg_measure_id=&)) and
         * pdm.member_id=pgdmdd.mem_id order by mem_id"},
         */
        {"getAddTargetParamDetails", "select distinct pgdmdd.mem_id,puaid.element_id,puaid.buss_col_id,puaid.dim_id,puaid.buss_table_id,puaid.dim_tab_id,puaid.member_name from prg_user_all_info_details puaid,prg_grp_dim_member_details pgdmdd  where pgdmdd.mem_id in(&) and           puaid.buss_col_id = pgdmdd.col_id and pgdmdd.col_type='KEY' and folder_id in(select bus_folder_id from target_measure_folder where bus_group_id in(select business_group from prg_target_master where prg_measure_id=&)) order by mem_id"},
        {"addParametDetails", "insert into PRG_TARGET_PARAM_DETAILS(PARAM_DISP_ID,TARGET_ID,PARAM_DISP_NAME,ELEMENT_ID,DIM_ID,DIM_TAB_ID,BUSS_TABLE,CHILD_ELEMENT_ID,DISP_SEQ_NO,DISPLAY_TYPE,DEFAULT_VALUE,REL_LEVEL) values('&','&','&','&','&','&','&','&','&','&','&','&')"}, {"addParamTime", "insert into PRG_TARGET_TIME(TAR_TIME_ID,TARGET_ID,TIME_TYPE,TIME_LEVEL) values('&','&','&','&')"}, {"addParamTimeDetails", "insert into PRG_TARGET_TIME_DETAIL(TAR_TIME_DET_ID,TAR_TIME_ID,COLUMN_NAME,COLUMN_TYPE,SEQUENCE,FORM_SEQUENCE) values('&','&','&','&','&','&')"}, {"addTargetQuery", "insert into PRG_TARGET_QUERY_DETAIL(QRY_COL_ID,COL_SEQ,COL_DESP_NAME,ELEMENT_ID,REF_ELEMENT_ID,FOLDER_ID,SUB_FOLDER_ID,TARGET_ID,COLUMN_TYPE) values('&','&','&','&','&','&','&','&','&')"}, {"addTargetViewMaster", "insert into PRG_TARGET_VIEW_MASTER(VIEW_BY_ID,TARGET_ID,VIEW_BY_SEQ,IS_ROW_EDGE,ROW_SEQ,COL_SEQ,DEFAULT_VALUE,LABEL_NAME,LABEL_TYPE) values('&','&','&','&','&','&','&','&','&')"}, {"addTargetViewDetails", "insert into PRG_TARGET_VIEW_BY_DETAILS(VIEW_BY_DTL_ID,PARAM_DISP_ID,VIEW_BY_ID,DISP_SEQ_NO) values('&','&','&','&')"}
    };
}
