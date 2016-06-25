package com.progen.userlayer.db;

import java.io.Serializable;
import java.util.ListResourceBundle;

public class PbUserLayerEditResourceBundle extends ListResourceBundle implements Serializable {

    @Override
    protected Object[][] getContents() {
        return contents;
    }
    static final Object contents[][] = {
        {"addUserSubFolderTables", "insert into PRG_USER_SUB_FOLDER_TABLES(SUB_FOLDER_TAB_ID,SUB_FOLDER_ID, BUSS_TABLE_ID, IS_DIMENSION, IS_FACT, IS_BUCKET, "
            + "DISP_NAME, DIM_ID,DIM_TAB_ID ,DIM_NAME,USE_REPORT_MEMBER,USE_REPORT_DIM_MEMBER) values ('&','&','&','&','&','&','&','&','&','&','Y','Y')"}, {"addUserSubFolderTablesForDimensions", "insert into PRG_USER_SUB_FOLDER_TABLES(SUB_FOLDER_TAB_ID,SUB_FOLDER_ID, BUSS_TABLE_ID, IS_DIMENSION, IS_FACT, IS_BUCKET, "
            + "DISP_NAME, DIM_ID,DIM_TAB_ID ,DIM_NAME,MEMBER_ID,MEMBER_NAME,USE_DENOM_TABLE,DEFAULT_HIERARCHY_ID,DENOM_TAB_ID,USE_REPORT_MEMBER,USE_REPORT_DIM_MEMBER) values ('&','&','&','&','&','&','&','&','&','&','&','&','&','&','&','Y','Y')"}, {"getBussDimInfoByGrpId", "SELECT DISTINCT gd.dim_id,gdm.member_name  as disp_name,gdt.dim_tab_id,gdt.tab_id,gd.dim_name,gdm.member_id,gdm.member_name ,  gdm.use_denom_table    ,  gd.default_hierarchy_id,    gdm.denom_tab_id         FROM prg_grp_dimensions gd,prg_grp_dim_tables gdt ,  prg_grp_dim_member gdm   WHERE gd.dim_id =gdt.dim_id AND gdm.dim_id    = gd.dim_id AND gdm.dim_tab_id= gdt.dim_tab_id AND gd.grp_id ='&' and gd.dim_id in(&) ORDER BY gd.dim_id"}, {"getBussFactsInfoByGrpId", "select buss_table_id, buss_table_name from prg_grp_buss_table where buss_table_id in (&)"}, {"getExtraBussTable", "select buss_table_id from prg_grp_buss_table where grp_id=& and buss_type not in('Query') minus select "
            + " distinct buss_table_id from prg_user_sub_folder_tables where sub_folder_id in(&) minus select distinct"
            + " buss_table_id from prg_user_sub_folder_tables where sub_folder_id in(select sub_folder_id from prg_user_folder_detail where folder_id=& and sub_folder_type='Facts') "}, {"getBussBucketInfoByGrpId", "SELECT BUSS_TABLE_ID, BUSS_TABLE_NAME FROM PRG_GRP_BUSS_TABLE where is_pure_query='Y' and buss_type='Query' and  grp_id='&' and buss_table_id not in(select tab_id from prg_grp_dim_tables where dim_id in(select dim_id from prg_grp_dimensions where grp_id=&)) and db_table_id is NOT null"}, {"addUserSubFolderElementsForFacts", "insert into PRG_USER_SUB_FOLDER_ELEMENTS( ELEMENT_ID, SUB_FOLDER_ID, BUSS_TABLE_ID, BUSS_COL_ID, BUSS_COL_NAME,"
            + " USER_COL_NAME, USER_COL_DESC, USER_COL_TYPE, SUB_FOLDER_TAB_ID, REF_ELEMENT_ID, REF_ELEMENT_TYPE,DEFAULT_AGGREGATION,USE_REPORT_FLAG) "
            + "values('&','&','&','&','&','&','&','&','&','&','&','&','Y')"}, {"getElementsInfo", " SELECT & AS SUB_FOLDER_ID,a.BUSS_TABLE_ID,a.buss_column_id, a.column_name AS BUSS_COL_NAME, a.column_disp_name AS USER_COL_NAME, a.column_display_desc AS USER_COL_DESC,"
            + "a.column_type,& AS SUB_FOLDER_TAB_ID,a.default_aggregation, 1 REF_ELEMENT_TYPE, nvl(a.COLUMN_DISPLAY_DESC,a.column_disp_name ) COLUMN_DISPLAY_DESC FROM PRG_GRP_BUSS_TABLE_DETAILS a "
            + "where  a.buss_table_id='&'"}, {"addUserSubFolderElements", "INSERT INTO PRG_USER_SUB_FOLDER_ELEMENTS (ELEMENT_ID, SUB_FOLDER_ID, BUSS_TABLE_ID, BUSS_COL_ID, BUSS_COL_NAME, USER_COL_NAME,"
            + " USER_COL_DESC, USER_COL_TYPE, SUB_FOLDER_TAB_ID,REF_ELEMENT_ID,REF_ELEMENT_TYPE,DEFAULT_AGGREGATION,USE_REPORT_FLAG) SELECT (PRG_USER_SUB_FLDR_ELEMENTS_SEQ.NEXTVAL) as ELEMENT_ID,"
            + "& as SUB_FOLDER_ID,a.BUSS_TABLE_ID, a.buss_column_id, a.column_name,a.column_disp_name,a.column_display_desc, "
            + "a.column_type,& as SUB_FOLDER_TAB_ID,(PRG_USER_SUB_FLDR_ELEMENTS_SEQ.CURRVAL),1 AS REF_ELEMENT_TYPE,a.default_aggregation,'Y' FROM PRG_GRP_BUSS_TABLE_DETAILS a "
            + "where  a.buss_table_id='&'"}, {"insertExtraElementsForFacts1", "insert into PRG_USER_SUB_FOLDER_ELEMENTS( ELEMENT_ID, SUB_FOLDER_ID, BUSS_TABLE_ID, BUSS_COL_ID, BUSS_COL_NAME,USER_COL_NAME,"
            + "USER_COL_DESC, USER_COL_TYPE, SUB_FOLDER_TAB_ID, REF_ELEMENT_ID, REF_ELEMENT_TYPE,DEFAULT_AGGREGATION,USE_REPORT_FLAG) SELECT  (PRG_USER_SUB_FLDR_ELEMENTS_SEQ.NEXTVAL) as ELEMENT_ID ,"
            + "a.SUB_FOLDER_ID ,a.BUSS_TABLE_ID ,a.BUSS_COL_ID,a.BUSS_COL_NAME,b.element_type_name || '_' || a.USER_COL_NAME ,b.element_type_name || ' ' || a.USER_COL_DESC USER_COL_DESC,a.USER_COL_TYPE ,a.SUB_FOLDER_TAB_ID,  "
            + "a.ELEMENT_ID REF_ELEMENT_ID,b.element_type_id REF_ELEMENT_TYPE,a.DEFAULT_AGGREGATION,'Y' FROM PRG_USER_SUB_FOLDER_ELEMENTS  A , prg_user_element_types B "
            + "where b.element_type_name != 'Current'  "
            + "and a.DEFAULT_AGGREGATION is not null  "
            + "and a.element_id in(select max(ELEMENT_ID) from  PRG_USER_SUB_FOLDER_ELEMENTS)"} //changed by Nazneen
        //             ,{"insertExtraElementsForFacts","insert into PRG_USER_SUB_FOLDER_ELEMENTS( ELEMENT_ID, SUB_FOLDER_ID, BUSS_TABLE_ID, BUSS_COL_ID, BUSS_COL_NAME,USER_COL_NAME," +
        //                "USER_COL_DESC, USER_COL_TYPE, SUB_FOLDER_TAB_ID, REF_ELEMENT_ID, REF_ELEMENT_TYPE,DEFAULT_AGGREGATION,USE_REPORT_FLAG) SELECT  (PRG_USER_SUB_FLDR_ELEMENTS_SEQ.NEXTVAL) as ELEMENT_ID ," +
        //                "a.SUB_FOLDER_ID ,a.BUSS_TABLE_ID ,a.BUSS_COL_ID,a.BUSS_COL_NAME,b.element_type_name || '_' || a.USER_COL_NAME ,b.element_type_name || ' ' || a.USER_COL_DESC USER_COL_DESC,a.USER_COL_TYPE ,a.SUB_FOLDER_TAB_ID,  " +
        //                "a.ELEMENT_ID REF_ELEMENT_ID,b.element_type_id REF_ELEMENT_TYPE,a.DEFAULT_AGGREGATION,'Y' FROM PRG_USER_SUB_FOLDER_ELEMENTS  A , prg_user_element_types B " +
        //                "where b.element_type_name != 'Current'  " +
        //                "and a.DEFAULT_AGGREGATION is not null  " +
        //                "and a.element_id in(&)"}
        , {"insertExtraElementsForFacts", "insert into PRG_USER_SUB_FOLDER_ELEMENTS( ELEMENT_ID, SUB_FOLDER_ID, BUSS_TABLE_ID, BUSS_COL_ID, BUSS_COL_NAME,USER_COL_NAME,"
            + "USER_COL_DESC, USER_COL_TYPE, SUB_FOLDER_TAB_ID, REF_ELEMENT_ID, REF_ELEMENT_TYPE,DEFAULT_AGGREGATION,USE_REPORT_FLAG) SELECT  (PRG_USER_SUB_FLDR_ELEMENTS_SEQ.NEXTVAL) as ELEMENT_ID ,"
            + "a.SUB_FOLDER_ID ,a.BUSS_TABLE_ID ,a.BUSS_COL_ID,a.BUSS_COL_NAME,b.element_type_name || '_' || a.USER_COL_NAME ,b.element_type_name || ' ' || a.USER_COL_DESC USER_COL_DESC,a.USER_COL_TYPE ,a.SUB_FOLDER_TAB_ID,  "
            + "a.ELEMENT_ID REF_ELEMENT_ID,b.element_type_id REF_ELEMENT_TYPE,a.DEFAULT_AGGREGATION,'Y' FROM PRG_USER_SUB_FOLDER_ELEMENTS  A , prg_user_element_types B "
            + "where b.element_type_name != 'Current'  "
            + "and a.DEFAULT_AGGREGATION is not null  "
            + "AND a.BUSS_TABLE_ID       IN (&) "
            + "and a.element_id in(&)"} /*
         * ,{"getSubFoldersQ","select * from prg_user_folder_detail where
         * folder_id=&"}
         */, {"getSubFoldersQ", "select * from prg_user_folder_detail where folder_id=& and SUB_FOLDER_TYPE='Dimensions'"}, {"grpDims", "select * from prg_grp_dimensions where grp_id=&"}, {"getFolderTable", "select * from prg_user_sub_folder_tables where sub_folder_id=&"}, {"getAllMemRel", "select d.rel_id,d.mem_id, d.rel_level, pd.dim_id from prg_grp_dim_rel_details d,prg_grp_dim_rel pd where "
            + " d.rel_id in (select rel_id from prg_grp_dim_rel where dim_id in(SELECT DISTINCT gd.dim_id from prg_grp_dimensions gd,"
            + " prg_grp_dim_tables gdt,  prg_grp_dim_member gdm ,prg_grp_buss_table pgb  WHERE gd.dim_id =gdt.dim_id AND "
            + " gdm.dim_id    = gd.dim_id AND gdm.dim_tab_id= gdt.dim_tab_id AND gd.grp_id ='&' and pgb.buss_table_id=gdt.tab_id and "
            + " pgb.buss_type='Table')  and is_default='Y')  and pd.rel_id= d.rel_id order by rel_id,rel_level"}, {"getRoleCustomDrill", "SELECT DISTINCT gd.dim_id,gdm.member_name  as disp_name,gdt.dim_tab_id,gdt.tab_id,gd.dim_name,gdm.member_id,"
            + " gdm.member_name ,gdm.use_denom_table , gd.default_hierarchy_id, gdm.denom_tab_id  FROM "
            + " prg_grp_dimensions gd,prg_grp_dim_tables gdt ,  prg_grp_dim_member gdm ,prg_grp_buss_table pgb "
            + " WHERE gd.dim_id =gdt.dim_id AND gdm.dim_id    = gd.dim_id AND gdm.dim_tab_id= gdt.dim_tab_id AND "
            + "gd.grp_id ='&' and pgb.buss_table_id=gdt.tab_id and pgb.buss_type='Table' and gd.dim_id in(&) ORDER BY gd.dim_id"}, {"addRoleCustomDrill", "insert into prg_grp_role_custom_drill(CUSTOM_DRILL_ID,FOLDER_ID,SUB_FOLDER_ID,MEMBER_ID,CHILD_MEMBER_ID,DRILL_ID) values(PRG_CUST_DRILL_SEQ.nextVal,'&','&','&','&','&')"}
    };
}
