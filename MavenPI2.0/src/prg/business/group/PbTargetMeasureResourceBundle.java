/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prg.business.group;

import java.io.Serializable;
import java.util.ListResourceBundle;

/**
 *
 * @author Saurabh
 */
public class PbTargetMeasureResourceBundle extends ListResourceBundle implements Serializable {

    protected Object[][] getContents() {
        return contents;
    }
    static final Object contents[][] = {
        {"getBusinessGroupFactsList", "select buss_table_id, buss_table_name,db_table_id from  prg_grp_buss_table where buss_table_id in (select buss_table_id from prg_grp_buss_table where grp_id=&  and buss_type!='Query' minus SELECT DISTINCT TAB_ID "
            + " FROM PRG_GRP_DIM_TABLES gdt,PRG_GRP_DIMENSIONS gd where gdt.dim_id= gd.dim_id and gd.grp_id=&) and buss_type='Table'"} //  ,{"getTargetDimMembersGroup","select member_id, member_name from prg_grp_dim_member where member_id in(select mem_id from prg_grp_dim_member_details where col_id in(select buss_col_id from prg_user_all_info_details where folder_id in(select folder_id from prg_user_folder where grp_id=&)))"}
        //  ,{"getTargetDimMembersGroup","select member_id, member_name from prg_target_measure_dim where member_id in(select mem_id from prg_grp_dim_member_details where col_id in(select buss_col_id from prg_user_all_info_details where folder_id in(select bus_folder_id from target_measure_folder where bus_group_id=&)))"}
        , {"generateViewByQry1", "select distinct BUSS_TABLE_ID,BUSS_TABLE_NAME,BUSS_COL_NAME,USER_COL_NAME,denom_query,ELEMENT_ID,"
            + "REF_ELEMENT_ID, REF_ELEMENT_TYPE from prg_user_all_info_details  where ELEMENT_ID in (&)  order by BUSS_TABLE_ID,"
            + " REF_ELEMENT_ID, REF_ELEMENT_TYPE "}, {"getMeasureCols", "select buss_column_id, column_name from PRG_GRP_BUSS_TABLE_DETAILS where buss_table_id=& and COLUMN_TYPE in('NUMBER')"}, {"getTargetDims", "select dim_id, dim_tab_id from prg_grp_dim_tables where tab_id=&"}, {"getTargetColIds", "select buss_column_id, column_name, column_type from prg_grp_buss_table_details where buss_column_id "
            + " in(select col_id from prg_grp_dim_member_details WHERE col_id in(select col_id from prg_grp_dim_tab_details where "
            + " dim_tab_id in (select dim_tab_id from prg_grp_dim_tables where tab_id in (&))))"}, {"getSequenceNumber", "select &.nextval from dual"}, {"getMeasureTabNames", "select column_name from user_tab_cols where table_name='&'"}, {"addTargetMeasureMaster", " insert into PRG_TARGET_MASTER(prg_measure_id,buss_original_table_id,business_group,target_table_id,measure_name) values('&','&','&','&','&')"}, {"addTargetMeasureDetails", " insert into PRG_TARGET_MASTER_DETAILS(prg_measure_id,prg_measure_id_details,target_table_id,target_table_name) values('&','&','&','&')"}, {"addTargetMasterColDetails", " insert into target_master_col_details(prg_measure_id_details,prg_measure_col_details,col_id,col_name,column_type,buss_table_id,table_type,target_table_id,selected_measure) values('&','&','&','&','&','&','&','&','&')"}, {"getSavedTargetMeasure", "select buss_original_table_id, pgbt.buss_table_name from prg_target_master ptm,prg_grp_buss_table pgbt where business_group=& and pgbt.buss_table_id= ptm.buss_original_table_id"}, {"getSavedMeasure", "select DISTINCT ptm.buss_original_table_id,col_id, col_name, pgbt.buss_table_name from target_master_col_details tmcd,prg_grp_buss_table pgbt ,prg_target_master ptm where "
            + " prg_measure_id_details in(select prg_measure_id_details from prg_target_master_details where"
            + " prg_target_master_details.prg_measure_id in(select prg_measure_id from prg_target_master where "
            + "prg_target_master.business_group=&)) and pgbt.buss_table_id=ptm.buss_original_table_id and tmcd.selected_measure='Y'"} // ,{"getTargetDimMembersGroup","select member_id, member_name from prg_grp_dim_member where member_id in(select mem_id from prg_grp_dim_member_details where col_id in(select buss_col_id from prg_user_all_info_details where folder_id in(select folder_id from prg_user_folder where grp_id=&)))"}
        /*
         * ,{"getTargetDimMembersGroup","select member_id, member_name from
         * prg_grp_dim_member where member_id in(select mem_id from
         * prg_grp_dim_member_details where col_id in(select buss_col_id from
         * prg_user_all_info_details where folder_id in(select bus_folder_id
         * from " + " target_measure_folder where bus_group_id=&)))"}
         */ /*
         * ,{"getTargetDimMembersGroup","SELECT DISTINCT K.VAL_ELEMENT_ID,
         * K.VAL_DISP_NAME,m.info_member_id ,k.key_dim_id, k.key_buss_col_name,
         * k.val_buss_col_id,k.val_member_name FROM
         * PRG_USER_ALL_DDIM_KEY_VAL_ELE K, prg_user_all_ddim_master m
         * ,prg_grp_buss_table p " + " where k.key_FOLDER_id=(select
         * bus_folder_id from target_measure_folder where bus_group_id=&) and "
         * + " m.info_dim_id=K.KEY_DIM_ID and K.VAL_DISP_NAME=m.info_member_name
         * and p.buss_table_id= k.key_buss_table_id and p.buss_type='Table'
         * order by k.key_dim_id,k.val_element_id "}
         */ //changed on 28-11-09
        , {"getTargetDimMembersGroup", "select pgdm.member_id, pgdm.member_name, pgdm.dim_tab_id,pgdt.tab_id,pgbt.buss_table_name, pgdt.dim_id "
            + " from prg_grp_dim_member pgdm,prg_grp_dim_tables pgdt, prg_grp_buss_table pgbt where pgdm.dim_id "
            + " in(select dim_id from prg_grp_dimensions where grp_id=&) and pgdt.dim_tab_id=pgdm.dim_tab_id and "
            + " pgbt.buss_table_id=pgdt.tab_id and pgbt.buss_type not in 'Query' order by pgdt.dim_id , pgdm.member_name"}, {"getTargetDimMembers", "select member_id,member_name from prg_grp_dim_member where dim_id in(select dim_id from prg_grp_dim_tables where tab_id in(&))"}, {"addTargetTableInDbMaster", "insert into prg_db_master_table(CONNECTION_ID,USER_SCHEMA,TABLE_ID,TABLE_NAME,TABLE_ALIAS,TABLE_TYPE,CREATED_BY,UPDATED_BY,CREATED_ON,UPDATED_ON,DB_QUERY) values('&','&','&','&','&','&','&','&','&','&','&')"}, {"addTargetTableInDbDetails", " insert into prg_db_master_table_details(column_id,table_id, table_col_name, table_col_cust_name, col_type, col_length, is_primary_key, is_active) values('&','&','&','&','&','&','&','&')"}, {"getTargetMemberNames", "select member_id, member_name from prg_grp_dim_member where member_id in (&)"} // ,{"getDimMemberDet","select distinct buss_column_id,column_name, column_type, column_disp_name from prg_grp_buss_table_details where buss_column_id in(select col_id from prg_grp_dim_tab_details where dim_tab_id in(select dim_tab_id from prg_grp_dim_member where member_id in (&)))"}
        // ,{"getDimMemberDet","select buss_col_id buss_column_id, buss_col_name column_name, user_col_type column_type, user_col_name column_disp_name from prg_user_all_info_details where  element_id in (&)"}
        /*
         * ,{"getDimMemberDet","select * from prg_grp_dim_member_details
         * pgdm,prg_grp_buss_table_details pgbt where mem_id" + " in(select
         * pgdm.member_id from prg_grp_dim_member pgdm,prg_grp_dim_tables pgdt,
         * prg_grp_buss_table pgbt where" + " pgdm.dim_id in(select dim_id from
         * prg_grp_dimensions where grp_id=&) and
         * pgdt.dim_tab_id=pgdm.dim_tab_id and" + "
         * pgbt.buss_table_id=pgdt.tab_id and pgbt.buss_type not in 'Query') and
         * col_type='VALUE' and " + " pgbt.buss_column_id=pgdm.col_id"}
         */, {"getDimMemberDet", "select * from prg_grp_dim_member_details pgdm,prg_grp_buss_table_details pgbt,prg_grp_buss_table pgb where mem_id"
            + " in(select pgdm.member_id from prg_grp_dim_member pgdm,prg_grp_dim_tables pgdt, prg_grp_buss_table pgbt where"
            + " pgdm.dim_id in(select dim_id from prg_grp_dimensions where grp_id=&) and pgdt.dim_tab_id=pgdm.dim_tab_id and"
            + " pgbt.buss_table_id=pgdt.tab_id and pgbt.buss_type not in 'Query') and col_type='VALUE' and "
            + " pgbt.buss_column_id=pgdm.col_id and pgb.grp_id=& and pgbt.buss_table_id=pgb.buss_table_id"}, {"getKeyMembers", "select col_id, col_type from prg_grp_dim_member_details where mem_id in(&) and col_type='KEY'"}, {"addTargetTabInBussTable", "insert into PRG_GRP_BUSS_TABLE(buss_table_id,buss_table_name,buss_desc,buss_type,no_of_nodes,is_pure_query,db_level_hint,db_table_id,grp_id,db_query) values('&','&','&','&','&','&','&','&','&','&')"}, {"addTargetTabInBussSrcTable", "insert into PRG_GRP_BUSS_TABLE_SRC(BUSS_SOURCE_ID,BUSS_TABLE_ID,CONNECTION_ID,DB_TABLE_ID,SOURCE_TYPE,DB_HINT,DB_TABLE_NAME) values('&','&','&','&','&','&','&')"}, {"addTargetTabInBussSrcDetailsTable", "insert into prg_grp_buss_table_src_details(BUSS_SRC_TABLE_DTL_ID,BUSS_SRC_ID,DB_TABLE_ID,BUSS_TABLE_ID,DB_COLUMN_ID,COLUMN_ALIAS,COLUMN_TYPE) values('&','&','&','&','&','&','&')"}, {"addBussTableDetails", "insert into PRG_GRP_BUSS_TABLE_DETAILS(BUSS_COLUMN_ID,BUSS_TABLE_ID,COLUMN_NAME,DB_TABLE_ID,DB_COLUMN_ID,BUSS_SRC_TABLE_DTL_ID,IS_UNION, COLUMN_TYPE,ACTUAL_COL_FORMULA,ACTUAL_COL_FORMULA1,DEFAULT_AGGREGATION,BUCKET_ATTACHED,COLUMN_DISP_NAME) values('&','&','&','&','&','&','&','&','&','&','&','&','&')"} // ,{"getMeasureTableCols","select buss_column_id, column_name, buss_column_id, pgdd.buss_table_id, pgbt.buss_table_name from PRG_GRP_BUSS_TABLE_DETAILS pgdd,prg_grp_buss_table pgbt where pgdd.buss_table_id in(&) and pgbt.buss_table_id= pgdd.buss_table_id order by pgdd.buss_table_id"}
        , {"getMeasureTableCols", "select buss_column_id, column_name, buss_column_id, pgdd.buss_table_id, pgbt.buss_table_name,pgdm.col_id from PRG_GRP_BUSS_TABLE_DETAILS pgdd,"
            + " prg_grp_buss_table pgbt,prg_grp_dim_member_details pgdm where pgdd.buss_table_id in(&) and "
            + " pgbt.buss_table_id= pgdd.buss_table_id and pgdm.col_id = buss_column_id and col_type='KEY' order by pgdd.buss_table_id"}, {"addBussTabRelMaster", "insert into PRG_GRP_BUSS_TABLE_RLT_MASTER(BUSS_RELATION_ID,BUSS_TABLE_ID,BUSS_TABLE_ID2,REALTIONSHIP_TYPE,ACTUAL_CLAUSE,ACTUAL_CLAUSE1,CLAUSE_TYPE) values('&','&','&','&','&','&','&')"}, {"addBussTabRelDetails", "insert into PRG_GRP_BUSS_TABLE_RLT_DETAILS(BUSS_RELATIONSHIP_DETAIL_ID,BUSS_RELATIONSHIP_ID,P_BUSS_TABLE_ID,P_BUSS_COL_ID1,S_BUSS_TABLE_ID,S_BUSS_COL_ID1,JOIN_TYPE,JOIN_OPERATOR,ACTUAL_CLAUSE,P_BUSS_COL_ID2,S_BUSS_COL_ID2) values('&','&','&','&','&','&','&','&','&','&','&')"}, {"addTargetMeasureMembers", "insert into prg_target_measure_members(MEASURE_MEMBER_DETAIL,MEASURE_ID,MEMBER_ID,element_id) values('&','&','&','&')"} //to enter the formula
        , {"addFormulaBussMater", "INSERT INTO PRG_GRP_BUSS_TABLE(BUSS_TABLE_ID, BUSS_TABLE_NAME, BUSS_DESC, BUSS_TYPE, NO_OF_NODES, IS_PURE_QUERY,DB_QUERY,"
            + "GRP_ID)values(&,'&','&','&',&,'&','&',&) "}, {"addFormulaSrc", "INSERT INTO  PRG_GRP_BUSS_TABLE_SRC(BUSS_SOURCE_ID, BUSS_TABLE_ID, DB_TABLE_ID, SOURCE_TYPE ,DB_TABLE_NAME,CONNECTION_ID,DB_QUERY) "
            + "values(&,&,&,'&','&',&,'&')"}, {"addFormulasrcDetails", "INSERT INTO PRG_GRP_BUSS_TABLE_SRC_DETAILS(BUSS_SRC_TABLE_DTL_ID, BUSS_SRC_ID, DB_TABLE_ID, BUSS_TABLE_ID, "
            + "COLUMN_ALIAS, COLUMN_TYPE,DB_COLUMN_ID) values(&,&,&,"
            + "&,'&','&',&)"}, {"addFormulaBussDetails", "INSERT INTO PRG_GRP_BUSS_TABLE_DETAILS(BUSS_COLUMN_ID, BUSS_TABLE_ID, COLUMN_NAME, DB_TABLE_ID, "
            + "BUSS_SRC_TABLE_DTL_ID,COLUMN_TYPE, BUCKET_ATTACHED, COLUMN_DISP_NAME,DB_COLUMN_ID,is_union,actual_col_formula,DEFAULT_AGGREGATION,COLUMN_DISPLAY_DESC,REFFERED_ELEMENTS,ROLE_FLAG)values(&,&,"
            + "'&',&,&,'&',&,'&',&,'&','&','&','&','&','&')"}
    };
}
