package prg.targetparam.qddb;

import java.io.Serializable;
import java.util.ListResourceBundle;

public class PbTargetParamResourceBundle extends ListResourceBundle implements Serializable {

    public PbTargetParamResourceBundle() {
    }

    protected Object[][] getContents() {
        return contents;
    }
    static final Object contents[][] = {
        {"getSequenceNumber", "select &.nextval from dual"},
        // {"getTargetsListU","select * from target_master where user_id=&"},

        {"addTargetTimeLevels", "insert into targetTimeLevels(TARGET_ID,LEVEL_DET_ID,LEVEL_ID,LEVEL_NAME) values('&',TARGET_TIME_LEVEL_ID_SEQ.nextval,'&','&')"},
        {"addTargetMaster", "insert into TARGET_MASTER(TARGET_ID,MEASURE_ID,TARGET_NAME,MIN_TIME_LEVEL,USER_ID,"
            + " TARGET_DESC,TARGET_PRIM_PARAM,TARGET_START_DATE,TARGET_END_DATE,TARGET_STATUS,"
            + " TARGET_START_MONTH,TARGET_END_MONTH,TARGET_START_QTR,TARGET_END_QTR,TARGET_START_YEAR,TARGET_END_YEAR) "
            + " values('&','&','&','&','&','&','&','&','&','&','&','&','&','&','&','&')"},
        {"getRelationForMembers", "select * from prg_grp_dim_rel_details where rel_id in(select rel_id from prg_grp_dim_rel where dim_id in (select dim_id from prg_grp_dim_member where member_id in(&))"
            + " and prg_grp_dim_rel.is_default='Y') order by rel_id,rel_level"},
        {"getSequenceNumberU", "select PRG_TARGET_MASTER_SEQ.nextval from dual"},
        {"getDurationLovs", "select description,value from prg_value_lookup where key_value='&'"},
        //  {"getUserBusinessGroups","select pubg.grp_id,pgm.grp_name from prg_user_buss_grps pubg,prg_grp_master pgm where user_id=& and pgm.grp_id=pubg.grp_id"},
        {"getUserBusinessGroups", " select DISTINCT pgum.grp_id, pgm.grp_name from prg_grp_user_assignment pgum,prg_grp_master pgm where user_id=& and pgm.grp_id=pgum.grp_id"},
        {"getAllMeasures", "select measure_name,prg_measure_id from prg_target_master where business_group in(&)"},
        {"getAllMonths", "select MON_NAME as view_by, MON_CODE as view_by1 from PRG_ACN_MON_DENOM order by cyear,cmon"},
        {"getAllQtrs", "select distinct cmqtr_code as view_by,cmqtr_code as view_by1,cyear from prg_acn_mon_denom order by cyear,cmqtr_code"},
        {"getAllYears", "select distinct cyear as view_by,cyear as view_by1 from prg_acn_mon_denom order by cyear"},
        {"getMeasureParameters", "select distinct ptmm.member_id,pgdm.member_name from prg_target_measure_members ptmm,prg_grp_dim_member pgdm "
            + "where measure_id=& and pgdm.member_id=ptmm.member_id order by 1"},
        {"getParameterNames", "select distinct member_id,member_name from prg_grp_dim_member where member_id in (&) order by 1"},
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
        //{"getAddTargetParamDetails","select distinct pgdmdd.mem_id,puaid.element_id,puaid.buss_col_id,puaid.dim_id,puaid.buss_table_id,puaid.dim_tab_id,puaid.member_name from prg_user_all_info_details puaid,prg_grp_dim_member_details pgdmdd  where pgdmdd.mem_id in(&) and           puaid.buss_col_id = pgdmdd.col_id and pgdmdd.col_type='KEY' and folder_id in(select bus_folder_id from target_measure_folder where bus_group_id in(select business_group from prg_target_master where prg_measure_id=&)) order by mem_id"},

        {"getAddTargetParamDetails", "select puaid.dim_tab_id,puaid.buss_col_id,puaid.buss_col_name,puaid.dim_id,puaid.member_id mem_id,puaid.member_name,puaid.buss_table_id,puaid.element_id from prg_target_measure_members ptm, prg_user_all_info_details puaid where measure_id=& and puaid.member_id in(&) and puaid.element_id= ptm.element_id "},
        {"addParametDetails", "insert into PRG_TARGET_PARAM_DETAILS(PARAM_DISP_ID,TARGET_ID,PARAM_DISP_NAME,ELEMENT_ID,DIM_ID,DIM_TAB_ID,BUSS_TABLE,CHILD_ELEMENT_ID,DISP_SEQ_NO,DISPLAY_TYPE,DEFAULT_VALUE,REL_LEVEL) values('&','&','&','&','&','&','&','&','&','&','&','&')"}, {"addParamTime", "insert into PRG_TARGET_TIME(TAR_TIME_ID,TARGET_ID,TIME_TYPE,TIME_LEVEL) values('&','&','&','&')"}, {"addParamTimeDetails", "insert into PRG_TARGET_TIME_DETAIL(TAR_TIME_DET_ID,TAR_TIME_ID,COLUMN_NAME,COLUMN_TYPE,SEQUENCE,FORM_SEQUENCE) values('&','&','&','&','&','&')"}, {"addTargetQuery", "insert into PRG_TARGET_QUERY_DETAIL(QRY_COL_ID,COL_SEQ,COL_DESP_NAME,ELEMENT_ID,REF_ELEMENT_ID,FOLDER_ID,SUB_FOLDER_ID,TARGET_ID,COLUMN_TYPE) values('&','&','&','&','&','&','&','&','&')"}, {"addTargetViewMaster", "insert into PRG_TARGET_VIEW_MASTER(VIEW_BY_ID,TARGET_ID,VIEW_BY_SEQ,IS_ROW_EDGE,ROW_SEQ,COL_SEQ,DEFAULT_VALUE,LABEL_NAME,LABEL_TYPE) values('&','&','&','&','&','&','&','&','&')"}, {"addTargetViewDetails", "insert into PRG_TARGET_VIEW_BY_DETAILS(VIEW_BY_DTL_ID,PARAM_DISP_ID,VIEW_BY_ID,DISP_SEQ_NO) values('&','&','&','&')"}, {"getTargetsListU", "select distinct tm.*,case when min_time_level ='Day' then to_char(target_start_date,'dd-MON-yyyy') "
            + "when min_time_level ='Month' then target_start_month when min_time_level='Quarter' then target_start_qtr "
            + "else target_start_year end st_date,case when min_time_level ='Day' then to_char(target_end_date,'dd-MON-yyyy') "
            + "when min_time_level ='Month' then target_end_month when min_time_level='Quarter' then target_end_qtr "
            + "else target_end_year end end_date FROM target_master tm,prg_target_master mm where tm.measure_id= mm.prg_measure_id and "
            + "mm.business_group in (select distinct grp_id from prg_grp_user_assignment where user_id=&) order by target_id"},
        //{"getTargetsListU","select * from target_master where user_id=&"},
        /*
         * ,{"getTargetsListU","select distinct tm.*,case when min_time_level
         * ='Day' then to_char(target_start_date,'dd-MON-yyyy') " + "when
         * min_time_level ='Month' then target_start_month when
         * min_time_level='Quarter' then target_start_qtr " + "else
         * target_start_year end st_date,case when min_time_level ='Day' then
         * to_char(target_end_date,'dd-MON-yyyy') " + "when min_time_level
         * ='Month' then target_end_month when min_time_level='Quarter' then
         * target_end_qtr " + "else target_end_year end end_date FROM
         * target_master tm,prg_target_master mm, prg_user_buss_grps pub where
         * tm.measure_id= mm.prg_measure_id and " + "mm.business_group in
         * (select distinct grp_id from prg_user_buss_grps where user_id=&)
         * order by target_id"},
         */
        /*
         * {"addTargetMaster","insert into
         * TARGET_MASTER(TARGET_ID,MEASURE_ID,TARGET_NAME,MIN_TIME_LEVEL,USER_ID,"
         * + "
         * TARGET_DESC,TARGET_PRIM_PARAM,TARGET_START_DATE,TARGET_END_DATE,TARGET_STATUS)
         * " + " values('&','&','&','&','&','&','&','&','&','&')"},
         */
        {"addTargetMaster2", "insert into TARGET_MASTER(TARGET_ID,MEASURE_ID,TARGET_NAME,MIN_TIME_LEVEL,USER_ID,"
            + " TARGET_DESC,TARGET_PRIM_PARAM,TARGET_START_MONTH,TARGET_END_MONTH,TARGET_STATUS) "
            + " values('&','&','&','&','&','&','&','&','&','&')"},
        {"addTargetMaster3", "insert into TARGET_MASTER(TARGET_ID,MEASURE_ID,TARGET_NAME,MIN_TIME_LEVEL,USER_ID,"
            + " TARGET_DESC,TARGET_PRIM_PARAM,TARGET_START_QTR,TARGET_END_QTR,TARGET_STATUS) "
            + " values('&','&','&','&','&','&','&','&','&','&')"},
        {"addTargetMaster4", "insert into TARGET_MASTER(TARGET_ID,MEASURE_ID,TARGET_NAME,MIN_TIME_LEVEL,USER_ID,"
            + " TARGET_DESC,TARGET_PRIM_PARAM,TARGET_START_YEAR,TARGET_END_YEAR,TARGET_STATUS) "
            + " values('&','&','&','&','&','&','&','&','&','&')"},
        {"getSequenceNumberU", "select PRG_TARGET_MASTER_SEQ.nextval from dual"},
        {"getDurationLovs", "select description,value from prg_value_lookup where key_value='&'"},
        {"getUserBusinessGroups", "select pubg.grp_id,pgm.grp_name from prg_user_buss_grps pubg,prg_grp_master pgm where user_id=& and pgm.grp_id=pubg.grp_id"},
        {"getAllMeasures", "select measure_name,prg_measure_id from prg_target_master where business_group in(&)"},
        //12-Ocy-09
        {"getExistedTargets", "select tm.*,to_char(target_start_date,'mm/dd/yyyy') s_date,to_char(target_end_date,'mm/dd/yyyy') e_date from target_master tm,"
            + "prg_target_master ptm where tm.measure_id=ptm.prg_measure_id"},
        {"getAllMonths", "select MON_NAME as view_by, MON_CODE as view_by1 from PRG_ACN_MON_DENOM order by cyear,cmon"},
        {"getAllQtrs", "select distinct cmqtr_code as view_by,cmqtr_code as view_by1,cyear from prg_acn_mon_denom order by cyear,cmqtr_code"},
        {"getAllYears", "select distinct cyear as view_by,cyear as view_by1 from prg_acn_mon_denom order by cyear"},
        {"getMeasureParameters", "select distinct ptmm.member_id,pgdm.member_name from prg_target_measure_members ptmm,prg_grp_dim_member pgdm "
            + "where measure_id=& and pgdm.member_id=ptmm.member_id order by 1"},
        {"getParameterNames", "select distinct member_id,member_name from prg_grp_dim_member where member_id in (&) order by 1"} /*
         * {"getAddTargetParamDetails","select distinct mem_id,element_id,
         * buss_col_id,pgdmdd.col_id,puaid.dim_id,puaid.buss_table_id,puaid.dim_tab_id
         * from" + " prg_user_all_info_details puaid, prg_grp_dim_member_details
         * pgdmdd where member_id in(&) and" + " puaid.buss_col_id =
         * pgdmdd.col_id and pgdmdd.col_type='KEY' and folder_id=(select
         * folder_id from prg_user_folder where grp_id" + " in(select
         * business_group from prg_target_master where prg_measure_id=&)) order
         * by mem_id"},
         */, {"getAddTargetParamDetails", "select distinct mem_id,element_id, buss_col_id,pgdmdd.col_id,puaid.dim_id,"
            + " puaid.buss_table_id,puaid.dim_tab_id from prg_user_all_info_details puaid, prg_grp_dim_member_details "
            + " pgdmdd where member_id in(&)  and puaid.buss_col_id = pgdmdd.col_id and pgdmdd.col_type='KEY' and "
            + " folder_id=(select bus_folder_id from target_measure_folder where bus_group_id in(select business_group "
            + " from prg_target_master where prg_measure_id=&)) order by mem_id"},
        {"addParametDetails", "insert into PRG_TARGET_PARAM_DETAILS(PARAM_DISP_ID,TARGET_ID,PARAM_DISP_NAME,ELEMENT_ID,DIM_ID,DIM_TAB_ID,BUSS_TABLE,CHILD_ELEMENT_ID,DISP_SEQ_NO,DISPLAY_TYPE,DEFAULT_VALUE,REL_LEVEL) values('&','&','&','&','&','&','&','&','&','&','&','&')"}, {"addParamTime", "insert into PRG_TARGET_TIME(TAR_TIME_ID,TARGET_ID,TIME_TYPE,TIME_LEVEL) values('&','&','&','&')"}, {"addParamTimeDetails", "insert into PRG_TARGET_TIME_DETAIL(TAR_TIME_DET_ID,TAR_TIME_ID,COLUMN_NAME,COLUMN_TYPE,SEQUENCE,FORM_SEQUENCE) values('&','&','&','&','&','&')"}, {"addTargetQuery", "insert into PRG_TARGET_QUERY_DETAIL(QRY_COL_ID,COL_SEQ,COL_DESP_NAME,ELEMENT_ID,REF_ELEMENT_ID,FOLDER_ID,SUB_FOLDER_ID,TARGET_ID,COLUMN_TYPE) values('&','&','&','&','&','&','&','&','&')"}, {"addTargetViewMaster", "insert into PRG_TARGET_VIEW_MASTER(VIEW_BY_ID,TARGET_ID,VIEW_BY_SEQ,IS_ROW_EDGE,ROW_SEQ,COL_SEQ,DEFAULT_VALUE,LABEL_NAME,LABEL_TYPE) values('&','&','&','&','&','&','&','&','&')"}, {"addTargetViewDetails", "insert into PRG_TARGET_VIEW_BY_DETAILS(VIEW_BY_DTL_ID,PARAM_DISP_ID,VIEW_BY_ID,DISP_SEQ_NO) values('&','&','&','&')"}, {"getActiveAlerts", "select count(*) as num_of_active_alerts from alert_master where target_id in (&) and end_date > sysdate"} //uday
        , {"deleteLock", "delete from prg_acquire_lock where user_id=&"},
        {"getTargetMaster", "select target_master.*,case when min_time_level='Day' then to_char(target_start_date,'mm/dd/yyyy') "
            + "when min_time_level='Month' then target_start_month when min_time_level='Quarter' then target_start_qtr "
            + "else target_start_year end st_date, case when min_time_level='Day' then to_char(target_end_date,'mm/dd/yyyy')  "
            + "when min_time_level='Month' then target_end_month when min_time_level='Quarter' then target_end_qtr "
            + "else target_end_year end end_date from target_master where target_id=&"},
        {"updateTarget", "update target_master set target_name='&', target_start_date=to_date('&','mm-dd-yy'), target_end_date=to_date('&','mm-dd-yy') where target_id=&"},
        {"updateTarget2", "update target_master set target_name='&', target_start_month='&', target_end_month='&' where target_id=&"},
        {"updateTarget3", "update target_master set target_name='&', target_start_qtr='&', target_end_qtr='&' where target_id=&"},
        {"updateTarget4", "update target_master set target_name='&', target_start_year='&', target_end_year='&' where target_id=&"},
        {"deleteTargetMaster", "delete from target_master where target_id in (&)"},
        {"deleteTargetTimeMaster", "delete from prg_target_time where target_id in (&)"},
        {"deleteTargetTimeDetails", "delete from prg_target_time_detail where tar_time_id in (select distinct tar_time_id from prg_target_time where target_id in (&))"},
        {"deleteTargetViewMaster", "delete from prg_target_view_master where target_id in (&)"},
        {"deleteTargetViewDetails", "delete from prg_target_view_by_details where view_by_id in (select distinct view_by_id from prg_target_view_master where target_id in(&))"},
        {"deleteAlertMaster", "delete from alert_master where target_id in (&)"},
        //uday
        {"copyTarget", "insert into target_master (target_id,measure_id,target_name,min_time_level,user_id,target_desc,target_prim_param,"
            + "target_start_date,target_end_date,target_status) select &,measure_id,'&',"
            + "min_time_level,user_id,'&',target_prim_param,to_date('&','mm-dd-yy'),to_date('&','mm-dd-yy'),target_status from "
            + "target_master where target_id=& "},
        {"copyTarget2", "insert into target_master (target_id,measure_id,target_name,min_time_level,user_id,target_desc,target_prim_param,"
            + "target_start_month,target_end_month,target_status) select &,measure_id,'&',"
            + "min_time_level,user_id,'&',target_prim_param,'&','&',target_status from "
            + "target_master where target_id=& "},
        {"copyTarget3", "insert into target_master (target_id,measure_id,target_name,min_time_level,user_id,target_desc,target_prim_param,"
            + "target_start_qtr,target_end_qtr,target_status) select &,measure_id,'&',"
            + "min_time_level,user_id,'&',target_prim_param,'&','&',target_status from "
            + "target_master where target_id=& "},
        {"copyTarget4", "insert into target_master (target_id,measure_id,target_name,min_time_level,user_id,target_desc,target_prim_param,"
            + "target_start_year,target_end_year,target_status) select &,measure_id,'&',"
            + "min_time_level,user_id,'&',target_prim_param,'&','&',target_status from "
            + "target_master where target_id=& "},
        {"copyParamDetails", "insert into PRG_TARGET_PARAM_DETAILS(PARAM_DISP_ID,TARGET_ID,PARAM_DISP_NAME,ELEMENT_ID,DIM_ID,DIM_TAB_ID,BUSS_TABLE,CHILD_ELEMENT_ID,DISP_SEQ_NO,DISPLAY_TYPE,DEFAULT_VALUE,REL_LEVEL) "
            + "select &,&,PARAM_DISP_NAME,ELEMENT_ID,DIM_ID,DIM_TAB_ID,BUSS_TABLE,CHILD_ELEMENT_ID,DISP_SEQ_NO,DISPLAY_TYPE,DEFAULT_VALUE,REL_LEVEL from "
            + "prg_target_param_details where target_id=& and disp_seq_no=&"}, {"copyParamTime", "insert into PRG_TARGET_TIME(TAR_TIME_ID,TARGET_ID,TIME_TYPE,TIME_LEVEL) "
            + "select &,&,TIME_TYPE,TIME_LEVEL from prg_target_time where target_id=&"}, {"copyParamTimeDetails", "insert into PRG_TARGET_TIME_DETAIL(TAR_TIME_DET_ID,TAR_TIME_ID,COLUMN_NAME,COLUMN_TYPE,SEQUENCE,FORM_SEQUENCE) "
            + "select &,(select distinct tar_time_id from prg_target_time where target_id=&),COLUMN_NAME,COLUMN_TYPE,SEQUENCE,FORM_SEQUENCE from "
            + "PRG_TARGET_TIME_DETAIL where tar_time_id=(select distinct tar_time_id from prg_target_time where target_id=&) and form_sequence=&"} //,{"copyTargetQuery","insert into PRG_TARGET_QUERY_DETAIL(QRY_COL_ID,COL_SEQ,COL_DESP_NAME,ELEMENT_ID,REF_ELEMENT_ID,FOLDER_ID,SUB_FOLDER_ID,TARGET_ID,COLUMN_TYPE) values('&','&','&','&','&','&','&','&','&')"}
        , {"copyTargetViewMaster", "insert into PRG_TARGET_VIEW_MASTER(VIEW_BY_ID,TARGET_ID,VIEW_BY_SEQ,IS_ROW_EDGE,ROW_SEQ,COL_SEQ,DEFAULT_VALUE,LABEL_NAME,LABEL_TYPE) "
            + "select &,&,VIEW_BY_SEQ,IS_ROW_EDGE,ROW_SEQ,COL_SEQ,DEFAULT_VALUE,LABEL_NAME,LABEL_TYPE from PRG_TARGET_VIEW_MASTER where target_id=& and view_by_seq=&"}, {"copyTargetViewDetails", "insert into PRG_TARGET_VIEW_BY_DETAILS(VIEW_BY_DTL_ID,PARAM_DISP_ID,VIEW_BY_ID,DISP_SEQ_NO) "
            + "values(&,(select param_disp_id from prg_target_param_details where target_id=& and disp_seq_no=&),(select view_by_id from prg_target_view_master where "
            + "target_id=& and view_by_seq=&),&)"}, {"getTargetParamDetails", "select * from prg_target_param_details where target_id=&"}, {"getTargetTimeParamDetails", "select * from prg_target_time_detail where tar_time_id=(select distinct tar_time_id from prg_target_time where target_id=&)"}, {"getTargetViewByMaster", "select distinct view_by_id from prg_target_view_master where target_id=&"}, {"getTargetViewByDetails", "select * from prg_target_view_by_details where view_by_id in (select distinct view_by_id from "
            + "prg_target_view_master where target_id=& and view_by_seq=&) order by 4"}, {"deleteTargetParamDetails", "delete from prg_target_param_details where target_id in (&)"},
        {"updateTargetStatus", "update target_master set target_status='&' where target_id=&"},
        {"getMonths", "select MON_NAME as view_by, MON_CODE as view_by1 from PRG_ACN_MON_DENOM "
            + "where cm_st_date between (select distinct cm_st_date from prg_acn_mon_denom where mon_name='&') and "
            + " (select distinct cm_end_date from prg_acn_mon_denom where mon_name='&') order by cyear,cmon"},
        //{"getQuarters","select view_by,view_by as view_by1 from ( select distinct T.CQTR || '-' || T.CQ_YEAR as view_by , T.CQ_YEAR || '-' || T.CQTR as orderbycol FROM " +
        //" pr_day_denom T ) order by orderbycol"}
        {"getQuarters", "select distinct cmqtr_code as view_by,cmqtr_code as view_by1,cyear from prg_acn_mon_denom "
            + "where cmq_st_date between (select distinct cmq_st_date from prg_acn_mon_denom where cmqtr_code='&') and "
            + " (select distinct cmq_st_date from prg_acn_mon_denom where cmqtr_code='&') order by cyear,cmqtr_code"},
        {"getYears", "select distinct cyear as view_by,cyear as view_by1 from prg_acn_mon_denom "
            + "where cmy_start_date between (select distinct cmy_start_date from prg_acn_mon_denom where cyear='&') and "
            + "(select distinct cmy_start_date from prg_acn_mon_denom where cyear='&') order by cyear"},
        {"addLock", "insert into prg_acquire_lock (key_id,key_value,user_id) values(&,&,&)"},
        {"getLock", "select * from prg_acquire_lock where key_value=&"},
        {"getAllLocks", "select * from prg_acquire_lock"},
        {"getUserDetails", "select PU_LOGIN_ID from PRG_AR_USERS where PU_ID='&'"},
        {"getMeasureSequence", "select PRG_TARGET_MEASURE_SEQ.nextval from dual"},};
}
