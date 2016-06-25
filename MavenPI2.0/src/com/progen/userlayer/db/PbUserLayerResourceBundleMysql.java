/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.userlayer.db;

import java.io.Serializable;
import java.util.ListResourceBundle;

/**
 * @filename PbUserLayerResourceBundle
 *
 * @author santhosh.kumar@progenbusiness.com @date Sep 5, 2009, 4:47:14 PM
 */
public class PbUserLayerResourceBundleMysql extends ListResourceBundle implements Serializable {

    @Override
    protected Object[][] getContents() {
        return contents;
    }
    static final Object contents[][] = {
        {"addUserFolder", "INSERT INTO PRG_USER_FOLDER(FOLDER_NAME, FOLDER_DESC,FOLDER_CREATED_ON,FOLDER_UPDATED_ON,GRP_ID) VALUES ('&','&',CURDATE(),CURDATE(),&)"}, {"addUserFolderMysql", "INSERT INTO PRG_USER_FOLDER(FOLDER_NAME, FOLDER_DESC,FOLDER_CREATED_ON,FOLDER_UPDATED_ON,GRP_ID) VALUES ('&','&',CURDATE(),CURDATE(),&)"}, {"addUserFolderDetails", "insert into PRG_USER_FOLDER_DETAIL (FOLDER_ID, SUB_FOLDER_NAME, SUB_FOLDER_TYPE) "
            + " values(&,'&','&' )"} /*
         * ,{"getBussDimInfoByGrpId","SELECT DISTINCT
         * gd.dim_id,gdm.member_name,gdt.dim_tab_id,gdt.tab_id,gd.dim_name FROM
         * prg_grp_dimensions gd,prg_grp_dim_tables gdt," + " prg_grp_dim_member
         * gdm WHERE gd.dim_id=gdt.dim_id and gdm.dim_id= gd.dim_id and
         * gdm.dim_tab_id= gdt.dim_tab_id AND gd.grp_id='&' order by gd.dim_id"}
         */ //        Removed by Nazneen
        //        ,{"getBussDimInfoByGrpId","SELECT DISTINCT gd.dim_id,gdm.member_name  as disp_name,gdt.dim_tab_id,gdt.tab_id,gd.dim_name,gdm.member_id,gdm.member_name ,  gdm.use_denom_table    ,  gd.default_hierarchy_id,    gdm.denom_tab_id         FROM prg_grp_dimensions gd,   prg_grp_dim_tables gdt     ,  prg_grp_dim_member gdm   WHERE gd.dim_id =gdt.dim_id AND gdm.dim_id    = gd.dim_id  AND isnull(gdm.dim_tab_id,gdt.dim_tab_id)= gdt.dim_tab_id AND gd.grp_id     ='&' ORDER BY gd.dim_id"}
        , {"getBussDimInfoByGrpId", "SELECT DISTINCT gd.dim_id,gdm.member_name  as disp_name,gdt.dim_tab_id,gdt.tab_id,gd.dim_name,gdm.member_id,gdm.member_name ,  gdm.use_denom_table    ,  gd.default_hierarchy_id,    gdm.denom_tab_id         FROM prg_grp_dimensions gd,   prg_grp_dim_tables gdt     ,  prg_grp_dim_member gdm   WHERE gd.dim_id =gdt.dim_id AND gdm.dim_id    = gd.dim_id  AND gd.grp_id     ='&' ORDER BY gd.dim_id"}, {"getBussBucketInfoByGrpId", "SELECT BUSS_TABLE_ID, BUSS_TABLE_NAME FROM PRG_GRP_BUSS_TABLE where is_pure_query='Y' and buss_type='Query' and  grp_id='&' and db_table_id is NOT null"} /*
         * ,{"getBussFactsInfoByGrpId","select buss_table_id, buss_table_name
         * from prg_grp_buss_table where buss_table_id in ( SELECT BUSS_TABLE_ID
         * " + "FROM PRG_GRP_BUSS_TABLE minus SELECT DISTINCT TAB_ID FROM
         * PRG_GRP_DIM_TABLES ) and buss_type!='Query' and is_pure_query!='Y'
         * and grp_id='&'"}
         *
         */, {"getBussFactsInfoByGrpId", "select buss_table_id, buss_table_name from prg_grp_buss_table where buss_table_id in ( SELECT BUSS_TABLE_ID FROM PRG_GRP_BUSS_TABLE except "
            + " SELECT DISTINCT TAB_ID FROM PRG_GRP_DIM_TABLES ) and buss_type!='Query' and isnull(is_pure_query,'N')!='Y' and grp_id='&'"}, {"getBussFactsInfoByGrpIdMysql", "SELECT buss_table_id,  buss_table_name FROM prg_grp_buss_table WHERE buss_table_id IN  (SELECT BUSS_TABLE_ID  FROM PRG_GRP_BUSS_TABLE  WHERE BUSS_TABLE_ID NOT IN    (SELECT DISTINCT TAB_ID FROM PRG_GRP_DIM_TABLES    )  ) AND buss_type                 !='Query' AND ifnull(is_pure_query,'N') !='Y' AND grp_id                     ='&'"} /*
         * ,{"addUserSubFolderTables","insert into
         * PRG_USER_SUB_FOLDER_TABLES(SUB_FOLDER_TAB_ID,SUB_FOLDER_ID,
         * BUSS_TABLE_ID, IS_DIMENSION, IS_FACT, IS_BUCKET, " + "DISP_NAME,
         * DIM_ID,DIM_TAB_ID ,DIM_NAME) values
         * ('&','&','&','&','&','&','&','&','&','&' )" }
         */ //modified by susheela start 16thNov
        //,{"addUserSubFolderTables","insert into PRG_USER_SUB_FOLDER_TABLES(SUB_FOLDER_TAB_ID,SUB_FOLDER_ID, BUSS_TABLE_ID, IS_DIMENSION, IS_FACT, IS_BUCKET, " +
        //       "DISP_NAME, DIM_ID,DIM_TAB_ID ,DIM_NAME,USE_REPORT_MEMBER,USE_REPORT_DIM_MEMBER) values ('&','&','&','&','&','&','&','&','&','&','Y','Y')" }
        //added for table_disp_name and table_tooltip_name
        , {"addUserSubFolderTables", "insert into PRG_USER_SUB_FOLDER_TABLES(SUB_FOLDER_ID, BUSS_TABLE_ID, IS_DIMENSION, IS_FACT, IS_BUCKET, "
            + "DISP_NAME, DIM_ID,DIM_TAB_ID ,DIM_NAME,USE_REPORT_MEMBER,USE_REPORT_DIM_MEMBER,TABLE_DISP_NAME,TABLE_TOOLTIP_NAME) values (&,'&','&','&','&','&',&,&,'&','Y','Y','&','&')"} //modified by susheela over
        /*
         * ,{"addUserSubFolderTablesForDimensions","insert into
         * PRG_USER_SUB_FOLDER_TABLES(SUB_FOLDER_TAB_ID,SUB_FOLDER_ID,
         * BUSS_TABLE_ID, IS_DIMENSION, IS_FACT, IS_BUCKET, " + "DISP_NAME,
         * DIM_ID,DIM_TAB_ID
         * ,DIM_NAME,MEMBER_ID,MEMBER_NAME,USE_DENOM_TABLE,DEFAULT_HIERARCHY_ID,DENOM_TAB_ID)
         * values ('&','&','&','&','&','&','&','&','&','&','&','&','&','&','&'
         * )" }
         */ //modified by susheela 18thNov
        //        ,{"addUserSubFolderTablesForDimensions","insert into PRG_USER_SUB_FOLDER_TABLES(SUB_FOLDER_TAB_ID,SUB_FOLDER_ID, BUSS_TABLE_ID, IS_DIMENSION, IS_FACT, IS_BUCKET, " +
        //                "DISP_NAME, DIM_ID,DIM_TAB_ID ,DIM_NAME,MEMBER_ID,MEMBER_NAME,USE_DENOM_TABLE,DEFAULT_HIERARCHY_ID,DENOM_TAB_ID,USE_REPORT_MEMBER,USE_REPORT_DIM_MEMBER) values ('&','&','&','&','&','&','&','&','&','&','&','&','&','&','&','Y','Y')" }
        //modified by susheela over
        //modified for TABLE_DISP_NAME and TABLE_TOOLTIP_NAME
        , {"addUserSubFolderTablesForDimensions", "insert into PRG_USER_SUB_FOLDER_TABLES(SUB_FOLDER_ID, BUSS_TABLE_ID, IS_DIMENSION, IS_FACT, IS_BUCKET, "
            + "DISP_NAME, DIM_ID,DIM_TAB_ID ,DIM_NAME,MEMBER_ID,MEMBER_NAME,USE_DENOM_TABLE,DEFAULT_HIERARCHY_ID,DENOM_TAB_ID,USE_REPORT_MEMBER,USE_REPORT_DIM_MEMBER,TABLE_DISP_NAME,TABLE_TOOLTIP_NAME) values (  &,  &, '&', '&', '&', '&', &, &, '&', &, '&', '&',  & ,  &, 'Y',  'Y', '&','&' )"} //end
        // modified by sunita for mysql
        , {"addUserSubFolderElements", "INSERT INTO PRG_USER_SUB_FOLDER_ELEMENTS (SUB_FOLDER_ID, BUSS_TABLE_ID, BUSS_COL_ID, BUSS_COL_NAME, USER_COL_NAME,"
            + " USER_COL_DESC, USER_COL_TYPE, SUB_FOLDER_TAB_ID,REF_ELEMENT_ID,REF_ELEMENT_TYPE,DEFAULT_AGGREGATION,USE_REPORT_FLAG,Display_Formula) values (&,&,&,'&','&','&','&',&,&,&,'&','&','&')"}, {"getUserSubFolderElements", "SELECT a.BUSS_TABLE_ID, a.buss_column_id,a.column_name,a.column_disp_name,a.column_display_desc, "
            + " a.column_type ,1 AS REF_ELEMENT_TYPE,a.default_aggregation,a.display_formula FROM PRG_GRP_BUSS_TABLE_DETAILS a "
            + "where  a.buss_table_id='&'"} // modified by sunita over
        /*
         * ,{"addUserSubFolderElements","INSERT INTO
         * PRG_USER_SUB_FOLDER_ELEMENTS (ELEMENT_ID, SUB_FOLDER_ID,
         * BUSS_TABLE_ID, BUSS_COL_ID, BUSS_COL_NAME, USER_COL_NAME," + "
         * USER_COL_DESC, USER_COL_TYPE,
         * SUB_FOLDER_TAB_ID,REF_ELEMENT_ID,REF_ELEMENT_TYPE,DEFAULT_AGGREGATION)
         * SELECT (PRG_USER_SUB_FLDR_ELEMENTS_SEQ.NEXTVAL) as ELEMENT_ID," + "&
         * as SUB_FOLDER_ID,a.BUSS_TABLE_ID, a.buss_column_id,
         * a.column_disp_name, a.column_disp_name, a.column_disp_name, " +
         * "a.column_type,& as
         * SUB_FOLDER_TAB_ID,(PRG_USER_SUB_FLDR_ELEMENTS_SEQ.CURRVAL),1 AS
         * REF_ELEMENT_TYPE,a.default_aggregation FROM
         * PRG_GRP_BUSS_TABLE_DETAILS a " + "where a.buss_table_id='&'"}
         */ /*
         * {"addUserSubFolderElements","INSERT INTO PRG_USER_SUB_FOLDER_ELEMENTS
         * (ELEMENT_ID, SUB_FOLDER_ID, BUSS_TABLE_ID, BUSS_COL_ID,
         * BUSS_COL_NAME, USER_COL_NAME," + " USER_COL_DESC, USER_COL_TYPE,
         * SUB_FOLDER_TAB_ID,REF_ELEMENT_ID,REF_ELEMENT_TYPE) SELECT
         * (PRG_USER_SUB_FLDR_ELEMENTS_SEQ.NEXTVAL) as ELEMENT_ID," + "& as
         * SUB_FOLDER_ID,a.BUSS_TABLE_ID, a.buss_column_id, a.column_disp_name,
         * a.column_disp_name, a.column_disp_name, " + "a.column_type,& as
         * SUB_FOLDER_TAB_ID,(PRG_USER_SUB_FLDR_ELEMENTS_SEQ.CURRVAL),b.ELEMENT_TYPE_ID
         * FROM PRG_GRP_BUSS_TABLE_DETAILS a" + ", prg_user_element_types B
         * where a.buss_table_id='&'"}
         *
         */ /*
         * ,{"getElementsInfo"," SELECT & AS
         * SUB_FOLDER_ID,a.BUSS_TABLE_ID,a.buss_column_id,a.column_disp_name as
         * BUSS_COL_NAME,a.column_disp_name as USER_COL_NAME," +
         * "a.column_disp_name as USER_COL_DESC,a.column_type,& AS
         * SUB_FOLDER_TAB_ID,a.default_aggregation, 1 REF_ELEMENT_TYPE FROM
         * PRG_GRP_BUSS_TABLE_DETAILS a " + "where a.buss_table_id='&' "}
         */ //modified by susheela start 17thNov
        , {"getElementsInfo", " SELECT & AS SUB_FOLDER_ID,a.BUSS_TABLE_ID,a.buss_column_id,a.column_name   AS BUSS_COL_NAME, a.column_disp_name  AS USER_COL_NAME, a.column_display_desc AS USER_COL_DESC,"
            + "a.column_type,& AS SUB_FOLDER_TAB_ID,a.default_aggregation, 1 REF_ELEMENT_TYPE,a.reffered_elements, isnull(a.COLUMN_DISPLAY_DESC,a.column_disp_name ) COLUMN_DISPLAY_DESC,a. actual_col_formula,a.display_formula FROM PRG_GRP_BUSS_TABLE_DETAILS a "
            + "where  a.buss_table_id='&'"}, {"getElementsInfoMysql", " SELECT & AS SUB_FOLDER_ID,a.BUSS_TABLE_ID,a.buss_column_id,a.column_name   AS BUSS_COL_NAME, a.column_disp_name  AS USER_COL_NAME, a.column_display_desc AS USER_COL_DESC,"
            + "a.column_type,& AS SUB_FOLDER_TAB_ID,a.default_aggregation, 1 REF_ELEMENT_TYPE,a.reffered_elements, ifnull(a.COLUMN_DISPLAY_DESC,a.column_disp_name ) COLUMN_DISPLAY_DESC,a. actual_col_formula,a.display_formula FROM PRG_GRP_BUSS_TABLE_DETAILS a "
            + "where  a.buss_table_id='&'"} //modified by susheela over
        /*
         * ,{"getElementsInfo"," SELECT & AS
         * SUB_FOLDER_ID,a.BUSS_TABLE_ID,a.buss_column_id,a.column_disp_name,a.column_disp_name,"
         * + "a.column_disp_name,a.column_type,& AS
         * SUB_FOLDER_TAB_ID,a.default_aggregation,b.element_type_id " +
         * "REF_ELEMENT_TYPE FROM PRG_GRP_BUSS_TABLE_DETAILS a,
         * prg_user_element_types B where b.element_type_name = 'Current' and "
         * + "(a.column_type ='NUMBER' OR a.column_type ='VARCHAR2' ) and
         * a.buss_table_id='&' "}
         *
         */ /*
         * ,{"addUserSubFolderElementsForFacts","insert into
         * PRG_USER_SUB_FOLDER_ELEMENTS( ELEMENT_ID, SUB_FOLDER_ID,
         * BUSS_TABLE_ID, BUSS_COL_ID, BUSS_COL_NAME," + " USER_COL_NAME,
         * USER_COL_DESC, USER_COL_TYPE, SUB_FOLDER_TAB_ID, REF_ELEMENT_ID,
         * REF_ELEMENT_TYPE,DEFAULT_AGGREGATION ) " +
         * "values('&','&','&','&','&','&','&','&','&','&','&','&')" }
         */ //modified by susheela start
        , {"addUserSubFolderElementsForFacts", "insert into PRG_USER_SUB_FOLDER_ELEMENTS(SUB_FOLDER_ID, BUSS_TABLE_ID, BUSS_COL_ID, BUSS_COL_NAME,"
            + " USER_COL_NAME, USER_COL_DESC, USER_COL_TYPE, SUB_FOLDER_TAB_ID, REF_ELEMENT_ID, REF_ELEMENT_TYPE,DEFAULT_AGGREGATION,USE_REPORT_FLAG,reffered_elements,actual_formula,display_formula) "
            + "values(&,'&','&','&','&','&','&',&,'&','&','&','Y','&','&','&')"} //modified by susheela over
        /*
         * ,{"insertExtraElementsForFacts","insert into
         * PRG_USER_SUB_FOLDER_ELEMENTS( ELEMENT_ID, SUB_FOLDER_ID,
         * BUSS_TABLE_ID, BUSS_COL_ID, BUSS_COL_NAME,USER_COL_NAME," +
         * "USER_COL_DESC, USER_COL_TYPE, SUB_FOLDER_TAB_ID, REF_ELEMENT_ID,
         * REF_ELEMENT_TYPE,DEFAULT_AGGREGATION ) SELECT
         * (PRG_USER_SUB_FLDR_ELEMENTS_SEQ.NEXTVAL) as ELEMENT_ID ," +
         * "a.SUB_FOLDER_ID ,a.BUSS_TABLE_ID
         * ,a.BUSS_COL_ID,a.BUSS_COL_NAME,b.element_type_name || '_' ||
         * a.USER_COL_NAME ,b.element_type_name || ' '
         * ||a.USER_COL_DESC,a.USER_COL_TYPE ,a.SUB_FOLDER_TAB_ID, " +
         * "a.ELEMENT_ID REF_ELEMENT_ID,b.element_type_id
         * REF_ELEMENT_TYPE,a.DEFAULT_AGGREGATION FROM
         * PRG_USER_SUB_FOLDER_ELEMENTS A , prg_user_element_types B " + "where
         * b.element_type_name != 'Current' and " + "(a.user_col_type ='NUMBER'
         * or upper(a.user_col_type)='CALCULATED') " + "and a.element_id in(&)"}
         */ /*
         * ,{"insertExtraElementsForFacts","insert into
         * PRG_USER_SUB_FOLDER_ELEMENTS( ELEMENT_ID, SUB_FOLDER_ID,
         * BUSS_TABLE_ID, BUSS_COL_ID, BUSS_COL_NAME,USER_COL_NAME," +
         * "USER_COL_DESC, USER_COL_TYPE, SUB_FOLDER_TAB_ID, REF_ELEMENT_ID,
         * REF_ELEMENT_TYPE,DEFAULT_AGGREGATION,USE_REPORT_FLAG) SELECT
         * (PRG_USER_SUB_FLDR_ELEMENTS_SEQ.NEXTVAL) as ELEMENT_ID ," +
         * "a.SUB_FOLDER_ID ,a.BUSS_TABLE_ID
         * ,a.BUSS_COL_ID,a.BUSS_COL_NAME,b.element_type_name || '_' ||
         * a.USER_COL_NAME ,b.element_type_name || ' ' || a.USER_COL_DESC
         * USER_COL_DESC,a.USER_COL_TYPE ,a.SUB_FOLDER_TAB_ID, " + "a.ELEMENT_ID
         * REF_ELEMENT_ID,b.element_type_id
         * REF_ELEMENT_TYPE,a.DEFAULT_AGGREGATION,a.USE_REPORT_FLAG FROM
         * PRG_USER_SUB_FOLDER_ELEMENTS A , prg_user_element_types B " + "where
         * b.element_type_name != 'Current' and " + "(a.user_col_type ='NUMBER'
         * OR upper(a.user_col_type)='CALCULATED') " + "and a.element_id in(&)"}
         */ //modified by sunita
        , {"insertExtraElementsForFacts", "insert into PRG_USER_SUB_FOLDER_ELEMENTS(  SUB_FOLDER_ID, BUSS_TABLE_ID, BUSS_COL_ID, BUSS_COL_NAME,USER_COL_NAME,"
            + "USER_COL_DESC, USER_COL_TYPE, SUB_FOLDER_TAB_ID, REF_ELEMENT_ID, REF_ELEMENT_TYPE,DEFAULT_AGGREGATION,USE_REPORT_FLAG)  "
            + "SELECT a.SUB_FOLDER_ID ,a.BUSS_TABLE_ID ,a.BUSS_COL_ID,a.BUSS_COL_NAME,concat(b.element_type_name,'_',a.USER_COL_NAME)"
            + ",concat(b.element_type_name,' ',a.USER_COL_DESC) USER_COL_DESC,a.USER_COL_TYPE ,a.SUB_FOLDER_TAB_ID,a.ELEMENT_ID "
            + "REF_ELEMENT_ID,b.element_type_id REF_ELEMENT_TYPE,a.DEFAULT_AGGREGATION,'Y' FROM PRG_USER_SUB_FOLDER_ELEMENTS  A "
            + ", prg_user_element_types B where b.element_type_id != 1 and a.default_aggregation is not null "
            + "and a.default_aggregation !='' and a.default_aggregation !='null' and a.SUB_FOLDER_ID in(select sub_folder_id from prg_user_folder_detail where folder_id =(select LAST_INSERT_ID(folder_id) from PRG_USER_FOLDER order by 1 desc limit 1) and sub_folder_name like 'Facts')"} //modified by sunita over
        , {"getSubFolderIdByFolderId", "select sub_folder_id from PRG_USER_FOLDER_DETAIL where folder_id='&'"} //for deleting user folder realted tables
        , {"deleteUserFolder", "DELETE FROM PRG_USER_FOLDER WHERE folder_id='&'"}, {"deleteUserFolderDetails", "DELETE FROM PRG_USER_FOLDER_DETAIL where folder_id='&'"}, {"deleteUserSubFolderTables", "DELETE FROM PRG_USER_SUB_FOLDER_TABLES where sub_folder_id='&'"}, {"deleteUserSubFolderElements", "DELETE FROM PRG_USER_SUB_FOLDER_ELEMENTS where sub_folder_id='&'"} //end of queries for deleting from user folder tables
        //  ,{"getUserFolderList","SELECT FOLDER_ID, FOLDER_NAME, FOLDER_DESC FROM PRG_USER_FOLDER order by FOLDER_CREATED_ON, folder_updated_by"}
        , {"getUserFolderList", "SELECT f.FOLDER_ID, f.FOLDER_NAME, f.FOLDER_DESC, G.GRP_NAME,G.GRP_ID FROM PRG_USER_FOLDER  f, PRG_GRP_MASTER G"
            + " where g.grp_id=f.grp_id and g.grp_id in(select grp_id from prg_grp_master where connection_id in (&))  order by f.FOLDER_CREATED_ON, f.folder_updated_by"}, {"getUserFolderList1", "SELECT f.FOLDER_ID, f.FOLDER_NAME, f.FOLDER_DESC, G.GRP_NAME,G.GRP_ID FROM PRG_USER_FOLDER  f, PRG_GRP_MASTER G"
            + " where g.grp_id=f.grp_id and f.FOLDER_ID='&' and g.grp_id in(select grp_id from prg_grp_master where connection_id in (&))   "}, {"getUserSubFolderList", "  select sub_folder_id, sub_folder_name, sub_folder_type from PRG_USER_FOLDER_DETAIL where folder_id='&' and sub_folder_name!='Formula'"} /*
         * ,{"getUserFolderDims","SELECT DISTINCT DIM_ID, DIM_NAME FROM
         * PRG_USER_SUB_FOLDER_TABLES where is_dimension='Y' and
         * sub_folder_id='&' order by dim_name"}
         */ //modified by susheela start 16-Nov
        , {"getUserFolderDims", "SELECT DISTINCT DIM_ID, DIM_NAME,use_report_member FROM PRG_USER_SUB_FOLDER_TABLES where is_dimension='Y' and sub_folder_id='&' order by dim_name"} //modified by susheela over
        /*
         * ,{"getUserFolderDimMbrs","SELECT
         * usft.sub_folder_tab_id,gdm.member_id,gdm.member_name,usft.dim_tab_id
         * FROM PRG_USER_SUB_FOLDER_TABLES usft,PRG_GRP_DIM_MEMBER gdm " +
         * "WHERE gdm.dim_id = usft.dim_id AND gdm.dim_tab_id = usft.dim_tab_id
         * AND usft.sub_folder_id='&'AND usft.is_dimension ='Y'and " +
         * "usft.dim_id='&' ORDER BY gdm.member_name"}
         */ // ,{"getUserFolderDimMbrs"," SELECT sub_folder_tab_id, member_id , isnull(disp_name, member_name) as mem_disp_name ,dim_tab_id FROM PRG_USER_SUB_FOLDER_TABLES WHERE sub_folder_id ='&' AND is_dimension  ='Y' AND dim_id ='&' ORDER BY mem_disp_name"}
        //modified by susheela start 16th Nov
        , {"getUserFolderDimMbrs", " SELECT sub_folder_tab_id, member_id , isnull(disp_name, member_name) as mem_disp_name ,dim_tab_id,use_report_member,use_report_dim_member FROM PRG_USER_SUB_FOLDER_TABLES WHERE sub_folder_id ='&' AND is_dimension  ='Y' AND dim_id ='&' ORDER BY mem_disp_name"}, {"getUserFolderDimMbrsMysql", " SELECT sub_folder_tab_id, member_id , ifnull(disp_name, member_name) as mem_disp_name ,dim_tab_id,use_report_member,use_report_dim_member FROM PRG_USER_SUB_FOLDER_TABLES WHERE sub_folder_id ='&' AND is_dimension  ='Y' AND dim_id ='&' ORDER BY mem_disp_name"} //modified by susheela over
        //added by Nazneen
        , {"getUserFolderDimMbrs", "SELECT A.sub_folder_tab_id,A.member_id ,isnull(A.disp_name, A.member_name) AS mem_disp_name ,A.dim_tab_id,A.use_report_member,A.use_report_dim_member,b.element_id FROM PRG_USER_SUB_FOLDER_TABLES A,PRG_USER_SUB_FOLDER_ELEMENTS B,PRG_GRP_DIM_MAP_KEY_VALUE map WHERE map.key1       = B.buss_col_id and A.member_id = map.key_mem_id and A.sub_folder_tab_id = B.sub_folder_tab_id and A.sub_folder_id ='&' AND A.is_dimension    ='Y' AND A.dim_id          ='&' ORDER BY mem_disp_name "}, {"getUserFolderDimMbrsMysql", "SELECT DISTINCT A.sub_folder_tab_id,A.member_id ,ifnull(A.disp_name, A.member_name) AS mem_disp_name ,A.dim_tab_id,A.use_report_member,A.use_report_dim_member,b.element_id FROM PRG_USER_SUB_FOLDER_TABLES A,PRG_USER_SUB_FOLDER_ELEMENTS B,PRG_GRP_DIM_MAP_KEY_VALUE map WHERE map.key1       = B.buss_col_id and A.member_id = map.key_mem_id and A.sub_folder_tab_id = B.sub_folder_tab_id and A.sub_folder_id ='&' AND A.is_dimension    ='Y' AND A.dim_id          ='&' ORDER BY mem_disp_name "} //modified by Nazneen completed
        , {"getUserFolderDimMbrsCols", "SELECT ELEMENT_ID, USER_COL_NAME,USER_COL_DESC FROM PRG_USER_SUB_FOLDER_ELEMENTS where sub_folder_tab_id='&'"}, {"getUserFolderFacts", "SELECT DISTINCT sub_folder_tab_id, COALESCE(table_disp_name,disp_name) disp_name,USE_REPORT_MEMBER,COALESCE(table_tooltip_name,disp_name) tooltip_name FROM PRG_USER_SUB_FOLDER_TABLES where is_fact='Y' and sub_folder_id='&' and disp_name not in('Calculated Facts','Formula') order by disp_name "}, {"getUserFolderFactsColumns", "SELECT ELEMENT_ID,USER_COL_NAME  FROM PRG_USER_SUB_FOLDER_ELEMENTS where SUB_FOLDER_TAB_ID='&' order by ref_element_id, ref_element_type"}, {"getUserFolderBuckets", "SELECT DISTINCT sub_folder_tab_id, COALESCE(table_disp_name,disp_name) disp_name,COALESCE(table_tooltip_name,disp_name) tooltip_name FROM PRG_USER_SUB_FOLDER_TABLES where is_bucket='Y' and sub_folder_id='&' order by disp_name "}, {"getUserFolderBucketsColumns", "SELECT ELEMENT_ID,USER_COL_NAME,USER_COL_DESC  FROM PRG_USER_SUB_FOLDER_ELEMENTS where SUB_FOLDER_TAB_ID='&' order by ref_element_id, ref_element_type"} //,{"getUserFoldDimNames","SELECT DISTINCT DIM_ID,DIM_NAME FROM PRG_USER_SUB_FOLDER_TABLES where sub_folder_id='&' and is_dimension='Y' order by DIM_NAME"}
        , {"deleteUserAllInfoDetails", " DELETE FROM PRG_USER_ALL_INFO_DETAILS where folder_id in(&)"}, {"deleteUserAllDDIMKeyValEle", " DELETE FROM PRG_USER_ALL_DDIM_KEY_VAL_ELE where  key_folder_id in (&)"}, {"deleteUserAllADIMKeyValEle", " DELETE FROM PRG_USER_ALL_ADIM_KEY_VAL_ELE where  key_folder_id in (&)"}, {"deleteUserAllDDIMDetails", " DELETE FROM PRG_USER_ALL_DDIM_DETAILS where  info_folder_id in (&)"}, {"deleteUserAllDDIMMaster", " DELETE FROM PRG_USER_ALL_DDIM_MASTER where  info_folder_id in (&)"}, {"deleteUserAllADIMDetails", " DELETE FROM PRG_USER_ALL_ADIM_DETAILS where  info_folder_id in (&)"}, {"deleteUserAllADIMMaster", " DELETE FROM PRG_USER_ALL_ADIM_MASTER where  info_folder_id in (&)"} /*
         * ,{"addUserAllInfoDetails"," insert into
         * PRG_USER_ALL_INFO_DETAILS(GRP_ID ,FOLDER_ID ,FOLDER_NAME
         * ,SUB_FOLDER_ID ,SUB_FOLDER_NAME ,SUB_FOLDER_TYPE
         * ,SUB_FOLDER_TAB_ID,"+ "IS_DIMENSION ,IS_FACT ,IS_BUCKET ,DISP_NAME
         * ,DIM_ID ,DIM_TAB_ID ,DIM_NAME ,ELEMENT_ID ,BUSS_TABLE_ID ,BUSS_COL_ID
         * ,"+ " BUSS_COL_NAME ,USER_COL_NAME ,USER_COL_DESC ,USER_COL_TYPE
         * ,REF_ELEMENT_ID ,REF_ELEMENT_TYPE ,MEMBER_ID ,MEMBER_NAME
         * ,BUSS_TABLE_NAME ,CONNECTION_ID ,"+ " AGGREGATION_TYPE ,
         * ACTUAL_COL_FORMULA ) SELECT busstab.grp_id , puf.FOLDER_ID
         * ,puf.FOLDER_NAME ,pufd.SUB_FOLDER_ID ,pufd.SUB_FOLDER_NAME ,"+ "
         * pufd.SUB_FOLDER_TYPE ,pusft.SUB_FOLDER_TAB_ID ,pusft.IS_DIMENSION
         * ,pusft.IS_FACT ,pusft.IS_BUCKET ,pusft.DISP_NAME ,"+ " pusft.DIM_ID
         * ,pusft.DIM_TAB_ID ,pusft.DIM_NAME ,pusfe.ELEMENT_ID
         * ,pusfe.BUSS_TABLE_ID ,pusfe.BUSS_COL_ID ,"+ " pusfe.BUSS_COL_NAME
         * ,pusfe.USER_COL_NAME ,pusfe.USER_COL_DESC,pusfe.USER_COL_TYPE
         * ,pusfe.ref_element_id ,"+ " pusfe.ref_element_type ,pusft.member_id
         * ,pusft.member_name, busstab.buss_table_name,connTab.conn_id,
         * bussdet.default_aggregation,bussdet.ACTUAL_COL_FORMULA "+ " FROM
         * PRG_USER_FOLDER puf ,PRG_USER_FOLDER_DETAIL pufd
         * ,PRG_USER_SUB_FOLDER_TABLES pusft ,PRG_USER_SUB_FOLDER_ELEMENTS
         * pusfe,"+ " PRG_GRP_BUSS_TABLE bussTab,PRG_GRP_BUSS_TABLE_DETAILS
         * bussDet,(Select min(CONNECTION_ID) conn_id, buss_table_id "+ " from
         * prg_grp_buss_table_src group by buss_table_id) connTab WHERE
         * puf.folder_id = pufd.folder_id "+ " AND pufd.SUB_FOLDER_ID
         * =pusft.SUB_FOLDER_ID AND
         * pusfe.sub_folder_tab_id=pusft.sub_folder_tab_id "+ " and
         * busstab.buss_table_id = pusft.buss_table_id and
         * bussdet.buss_column_id = pusfe.buss_col_id "+ " and
         * connTab.buss_table_id = pusft.buss_table_id and puf.FOLDER_ID in(&)
         * ORDER BY puf.folder_id, "+ "
         * pufd.SUB_FOLDER_NAME,pusft.SUB_FOLDER_TAB_ID "}
         */ //commented for add columns TABLE_DISP_NAME and TABLE_TOOLTIP_NAME
        //modified by susheela start 18thNov
        // ,{"addUserAllInfoDetails"," insert into PRG_USER_ALL_INFO_DETAILS(GRP_ID ,FOLDER_ID ,FOLDER_NAME ,SUB_FOLDER_ID ,SUB_FOLDER_NAME ,SUB_FOLDER_TYPE ,SUB_FOLDER_TAB_ID,"+
        //   "IS_DIMENSION ,IS_FACT ,IS_BUCKET ,DISP_NAME ,DIM_ID ,DIM_TAB_ID ,DIM_NAME ,ELEMENT_ID ,BUSS_TABLE_ID ,BUSS_COL_ID ,"+
        //   " BUSS_COL_NAME ,USER_COL_NAME ,USER_COL_DESC ,USER_COL_TYPE ,REF_ELEMENT_ID ,REF_ELEMENT_TYPE ,MEMBER_ID ,MEMBER_NAME ,BUSS_TABLE_NAME ,CONNECTION_ID ,"+
        //   " AGGREGATION_TYPE , ACTUAL_COL_FORMULA,USE_REPORT_FLAG,REFFERED_ELEMENTS) SELECT busstab.grp_id , puf.FOLDER_ID ,puf.FOLDER_NAME ,pufd.SUB_FOLDER_ID ,pufd.SUB_FOLDER_NAME ,"+
        //    " pufd.SUB_FOLDER_TYPE ,pusft.SUB_FOLDER_TAB_ID ,pusft.IS_DIMENSION ,pusft.IS_FACT ,pusft.IS_BUCKET ,pusft.DISP_NAME ,"+
        //    " pusft.DIM_ID ,pusft.DIM_TAB_ID ,pusft.DIM_NAME ,pusfe.ELEMENT_ID ,pusfe.BUSS_TABLE_ID ,pusfe.BUSS_COL_ID ,"+
        //    " pusfe.BUSS_COL_NAME ,pusfe.USER_COL_NAME ,pusfe.USER_COL_DESC,pusfe.USER_COL_TYPE ,pusfe.ref_element_id ,"+
        //    " pusfe.ref_element_type ,pusft.member_id ,pusft.member_name, busstab.buss_table_name,connTab.conn_id, bussdet.default_aggregation,bussdet.ACTUAL_COL_FORMULA,pusfe.use_report_flag,bussdet.REFFERED_ELEMENTS "+
        //    " FROM PRG_USER_FOLDER puf ,PRG_USER_FOLDER_DETAIL pufd ,PRG_USER_SUB_FOLDER_TABLES pusft ,PRG_USER_SUB_FOLDER_ELEMENTS pusfe,"+
        //    " PRG_GRP_BUSS_TABLE bussTab,PRG_GRP_BUSS_TABLE_DETAILS bussDet,(Select min(CONNECTION_ID) conn_id, buss_table_id "+
        //    " from prg_grp_buss_table_src group by buss_table_id) connTab WHERE puf.folder_id = pufd.folder_id "+
        //    " AND pufd.SUB_FOLDER_ID =pusft.SUB_FOLDER_ID AND pusfe.sub_folder_tab_id=pusft.sub_folder_tab_id "+
        //    " and busstab.buss_table_id = pusft.buss_table_id and bussdet.buss_column_id = pusfe.buss_col_id "+
        //    " and connTab.buss_table_id = pusft.buss_table_id AND pusfe.buss_col_id!=0 and puf.FOLDER_ID in(&) AND  pusfe.use_report_flag='Y' AND USE_REPORT_DIM_MEMBER='Y' ORDER BY puf.folder_id, "+
        //    " pufd.SUB_FOLDER_NAME,pusft.SUB_FOLDER_TAB_ID "}
        //modified by susheela  over 18thNov
        //end
        //modified for adding TABLE_DISP_NAME and TABLE_TOOLTIP_NAME
        , {"addUserAllInfoDetails", " insert into PRG_USER_ALL_INFO_DETAILS(GRP_ID ,FOLDER_ID ,FOLDER_NAME ,SUB_FOLDER_ID ,SUB_FOLDER_NAME ,SUB_FOLDER_TYPE ,SUB_FOLDER_TAB_ID,"
            + "IS_DIMENSION ,IS_FACT ,IS_BUCKET ,DISP_NAME ,DIM_ID ,DIM_TAB_ID ,DIM_NAME ,ELEMENT_ID ,BUSS_TABLE_ID ,BUSS_COL_ID ,"
            + " BUSS_COL_NAME ,USER_COL_NAME ,USER_COL_DESC ,USER_COL_TYPE ,REF_ELEMENT_ID ,REF_ELEMENT_TYPE ,MEMBER_ID ,MEMBER_NAME ,BUSS_TABLE_NAME ,CONNECTION_ID ,"
            + " AGGREGATION_TYPE , ACTUAL_COL_FORMULA,USE_REPORT_FLAG,REFFERED_ELEMENTS,Display_Formula,TABLE_DISP_NAME,TABLE_TOOLTIP_NAME) SELECT busstab.grp_id , puf.FOLDER_ID ,puf.FOLDER_NAME ,pufd.SUB_FOLDER_ID ,pufd.SUB_FOLDER_NAME ,"
            + " pufd.SUB_FOLDER_TYPE ,pusft.SUB_FOLDER_TAB_ID ,pusft.IS_DIMENSION ,pusft.IS_FACT ,pusft.IS_BUCKET ,pusft.DISP_NAME ,"
            + " pusft.DIM_ID ,pusft.DIM_TAB_ID ,pusft.DIM_NAME ,pusfe.ELEMENT_ID ,pusfe.BUSS_TABLE_ID ,pusfe.BUSS_COL_ID ,"
            + " pusfe.BUSS_COL_NAME ,pusfe.USER_COL_NAME ,pusfe.USER_COL_DESC,pusfe.USER_COL_TYPE ,pusfe.ref_element_id ,"
            + " pusfe.ref_element_type ,pusft.member_id ,pusft.member_name, busstab.buss_table_name,connTab.conn_id, bussdet.default_aggregation,bussdet.ACTUAL_COL_FORMULA,pusfe.use_report_flag,bussdet.REFFERED_ELEMENTS,bussdet.display_formula,COALESCE(pusft.TABLE_DISP_NAME,pusft.DISP_NAME),COALESCE(pusft.TABLE_TOOLTIP_NAME,pusft.DISP_NAME) "
            + " FROM PRG_USER_FOLDER puf "
            + " ,PRG_USER_FOLDER_DETAIL pufd "
            + " ,PRG_USER_SUB_FOLDER_TABLES pusft "
            + ",PRG_USER_SUB_FOLDER_ELEMENTS pusfe,"
            + " PRG_GRP_BUSS_TABLE bussTab"
            + " ,PRG_GRP_BUSS_TABLE_DETAILS bussDet"
            + " ,(Select min(CONNECTION_ID) conn_id, buss_table_id "
            + " from prg_grp_buss_table_src group by buss_table_id) connTab "
            + "WHERE puf.folder_id = pufd.folder_id "
            + " AND pufd.SUB_FOLDER_ID =pusft.SUB_FOLDER_ID "
            + " AND pusfe.sub_folder_tab_id=pusft.sub_folder_tab_id "
            + " and busstab.buss_table_id = pusft.buss_table_id "
            + " and bussdet.buss_column_id = pusfe.buss_col_id "
            + " and connTab.buss_table_id = pusft.buss_table_id "
            + " AND pusfe.buss_col_id!=0 and puf.FOLDER_ID in(&) "
            + " AND  pusfe.use_report_flag='Y' "
            + " AND USE_REPORT_DIM_MEMBER='Y' ORDER BY puf.folder_id, "
            + " pufd.SUB_FOLDER_NAME,pusft.SUB_FOLDER_TAB_ID "}, {"addUserAllInfoDetailsMysql", " insert into PRG_USER_ALL_INFO_DETAILS(GRP_ID ,FOLDER_ID ,FOLDER_NAME ,SUB_FOLDER_ID ,SUB_FOLDER_NAME ,SUB_FOLDER_TYPE ,SUB_FOLDER_TAB_ID,"
            + "IS_DIMENSION ,IS_FACT ,IS_BUCKET ,DISP_NAME ,DIM_ID ,DIM_TAB_ID ,DIM_NAME ,ELEMENT_ID ,BUSS_TABLE_ID ,BUSS_COL_ID ,"
            + " BUSS_COL_NAME ,USER_COL_NAME ,USER_COL_DESC ,USER_COL_TYPE ,REF_ELEMENT_ID ,REF_ELEMENT_TYPE ,MEMBER_ID ,MEMBER_NAME ,BUSS_TABLE_NAME ,CONNECTION_ID ,"
            + " AGGREGATION_TYPE , ACTUAL_COL_FORMULA,USE_REPORT_FLAG,REFFERED_ELEMENTS,Display_Formula,TABLE_DISP_NAME,TABLE_TOOLTIP_NAME) SELECT busstab.grp_id , puf.FOLDER_ID ,puf.FOLDER_NAME ,pufd.SUB_FOLDER_ID ,pufd.SUB_FOLDER_NAME ,"
            + " pufd.SUB_FOLDER_TYPE ,pusft.SUB_FOLDER_TAB_ID ,pusft.IS_DIMENSION ,pusft.IS_FACT ,pusft.IS_BUCKET ,pusft.DISP_NAME ,"
            + " pusft.DIM_ID ,pusft.DIM_TAB_ID ,pusft.DIM_NAME ,pusfe.ELEMENT_ID ,pusfe.BUSS_TABLE_ID ,pusfe.BUSS_COL_ID ,"
            + " pusfe.BUSS_COL_NAME ,pusfe.USER_COL_NAME ,pusfe.USER_COL_DESC,pusfe.USER_COL_TYPE ,pusfe.ref_element_id ,"
            + " pusfe.ref_element_type ,pusft.member_id ,pusft.member_name, busstab.buss_table_name,connTab.conn_id, bussdet.default_aggregation,bussdet.ACTUAL_COL_FORMULA,pusfe.use_report_flag,bussdet.REFFERED_ELEMENTS,bussdet.display_formula,ifnull(pusft.TABLE_DISP_NAME,pusft.DISP_NAME),ifnull(pusft.TABLE_TOOLTIP_NAME,pusft.DISP_NAME) "
            + " FROM PRG_USER_FOLDER puf ,PRG_USER_FOLDER_DETAIL pufd ,PRG_USER_SUB_FOLDER_TABLES pusft ,PRG_USER_SUB_FOLDER_ELEMENTS pusfe,"
            + " PRG_GRP_BUSS_TABLE bussTab,PRG_GRP_BUSS_TABLE_DETAILS bussDet,(Select min(CONNECTION_ID) conn_id, buss_table_id "
            + " from prg_grp_buss_table_src group by buss_table_id) connTab WHERE puf.folder_id = pufd.folder_id "
            + " AND pufd.SUB_FOLDER_ID =pusft.SUB_FOLDER_ID AND pusfe.sub_folder_tab_id=pusft.sub_folder_tab_id "
            + " and busstab.buss_table_id = pusft.buss_table_id and bussdet.buss_column_id = pusfe.buss_col_id "
            + " and connTab.buss_table_id = pusft.buss_table_id AND pusfe.buss_col_id not in(0) and puf.FOLDER_ID in(&) AND  pusfe.use_report_flag='Y' AND USE_REPORT_DIM_MEMBER='Y' ORDER BY puf.folder_id, "
            + " pufd.SUB_FOLDER_NAME,pusft.SUB_FOLDER_TAB_ID "} //modified by susheela  over 18thNov
        //modified by susheela start. Put the report flag and dim filters
        /*
         * ,{"addUserAllInfoDetails"," insert into
         * PRG_USER_ALL_INFO_DETAILS(GRP_ID ,FOLDER_ID ,FOLDER_NAME
         * ,SUB_FOLDER_ID ,SUB_FOLDER_NAME ,SUB_FOLDER_TYPE
         * ,SUB_FOLDER_TAB_ID,"+ "IS_DIMENSION ,IS_FACT ,IS_BUCKET ,DISP_NAME
         * ,DIM_ID ,DIM_TAB_ID ,DIM_NAME ,ELEMENT_ID ,BUSS_TABLE_ID ,BUSS_COL_ID
         * ,"+ " BUSS_COL_NAME ,USER_COL_NAME ,USER_COL_DESC ,USER_COL_TYPE
         * ,REF_ELEMENT_ID ,REF_ELEMENT_TYPE ,MEMBER_ID ,MEMBER_NAME
         * ,BUSS_TABLE_NAME ,CONNECTION_ID ,"+ " AGGREGATION_TYPE )SELECT
         * busstab.grp_id , puf.FOLDER_ID ,puf.FOLDER_NAME ,pufd.SUB_FOLDER_ID
         * ,pufd.SUB_FOLDER_NAME ,"+ " pufd.SUB_FOLDER_TYPE
         * ,pusft.SUB_FOLDER_TAB_ID ,pusft.IS_DIMENSION ,pusft.IS_FACT
         * ,pusft.IS_BUCKET ,pusft.DISP_NAME ,"+ " pusft.DIM_ID
         * ,pusft.DIM_TAB_ID ,pusft.DIM_NAME ,pusfe.ELEMENT_ID
         * ,pusfe.BUSS_TABLE_ID ,pusfe.BUSS_COL_ID ,"+ " pusfe.BUSS_COL_NAME
         * ,pusfe.USER_COL_NAME ,pusfe.USER_COL_DESC ,pusfe.USER_COL_TYPE
         * ,pusfe.ref_element_id ,"+ " pusfe.ref_element_type ,pusft.member_id
         * ,pusft.member_name, busstab.buss_table_name,connTab.conn_id,
         * bussdet.default_aggregation "+ " FROM PRG_USER_FOLDER puf
         * ,PRG_USER_FOLDER_DETAIL pufd ,PRG_USER_SUB_FOLDER_TABLES pusft
         * ,PRG_USER_SUB_FOLDER_ELEMENTS pusfe,"+ " PRG_GRP_BUSS_TABLE
         * bussTab,PRG_GRP_BUSS_TABLE_DETAILS bussDet,(Select min(CONNECTION_ID)
         * conn_id, buss_table_id "+ " from prg_grp_buss_table_src group by
         * buss_table_id) connTab WHERE puf.folder_id = pufd.folder_id and
         * pusfe.use_report_flag='Y' "+ " AND pufd.SUB_FOLDER_ID
         * =pusft.SUB_FOLDER_ID AND
         * pusfe.sub_folder_tab_id=pusft.sub_folder_tab_id "+ " and
         * busstab.buss_table_id = pusft.buss_table_id and
         * bussdet.buss_column_id = pusfe.buss_col_id "+ " and
         * connTab.buss_table_id = pusft.buss_table_id and puf.FOLDER_ID in(&)
         * and pusft.use_report_member='Y' and pusft.use_report_dim_member='Y'
         * ORDER BY puf.folder_id, "+ "
         * pufd.SUB_FOLDER_NAME,pusft.SUB_FOLDER_TAB_ID "}
         */ //modified by susheela over
        , {"addUserAllDDIMDetails", "INSERT INTO PRG_USER_ALL_DDIM_DETAILS( INFO_FOLDER_ID, INFO_ELEMENT_ID, INFO_BUSS_COL_ID, REL_LEVEL, INFO_DIM_ID, IMFO_DIM_NAME, INFO_MEMBER_ID, INFO_MEMBER_NAME)"
            + "select info.folder_id info_folder_id, info.element_id  info_element_id, "
            + "info.buss_col_id info_buss_col_id,  COALESCE(reld.rel_level,1) rel_level, info.dim_id info_dim_id, "
            + "info.dim_name imfo_dim_name,info.member_id info_member_id,info.member_name info_member_name  "
            + "from PRG_USER_ALL_INFO_DETAILS info left outer join "
            + "  prg_grp_dim_rel relm on ( info.dim_id = relm.dim_id and relm.is_default ='Y' )"
            + " left outer join  prg_grp_dim_rel_details reld  on (info.member_id = reld.mem_id ) "
            + "where info.member_id is not null "
            + " and info.folder_id in(&)"}, {"addUserAllDDIMMaster", "INSERT INTO PRG_USER_ALL_DDIM_MASTER ( INFO_FOLDER_ID, REL_LEVEL, INFO_DIM_ID, INFO_DIM_NAME, INFO_MEMBER_ID, INFO_MEMBER_NAME ) "
            + "select distinct info.folder_id info_folder_id,COALESCE(reld.rel_level,1) rel_level,info.dim_id info_dim_id, "
            + "info.dim_name info_dim_name,info.member_id info_member_id,info.member_name info_member_name "
            + "from PRG_USER_ALL_INFO_DETAILS info left outer join "
            + "  prg_grp_dim_rel relm on (  info.dim_id = relm.dim_id and relm.is_default ='Y' ) "
            + " left outer join  prg_grp_dim_rel_details reld on(info.member_id = reld.mem_id) "
            + "where info.member_id is not null and info.folder_id in(&)"}, {"addUserAllDDIMKeyValEle", "INSERT INTO PRG_USER_ALL_DDIM_KEY_VAL_ELE (LEVEL1,KEY_ELEMENT_ID,KEY_COL_TYPE,VAL_ELEMENT_ID,KEY_BUSS_COL_ID,VAL_BUSS_COL_ID, "
            + "KEY_BUSS_COL_NAME, VAL_BUSS_COL_NAME,KEY_BUSS_TABLE_ID,VAL_BUSS_TABLE_ID, VAL_COL_TYPE,KEY_MEMBER_NAME,VAL_MEMBER_NAME, "
            + "KEY_SUB_FOLDER_ID,VAL_SUB_FOLDER_ID, KEY_SUB_FOLDER_NAME,VAL_SUB_FOLDER_NAME, KEY_DISP_NAME, KEY_DIM_ID,KEY_DIM_NAME, "
            + "VAL_DISP_NAME, VAL_DIM_ID, VAL_DIM_NAME,KEY_FOLDER_ID, KEY_FOLDER_NAME, REL_NAME,IS_DEFAULT ) "
            + "SELECT COALESCE(reld.rel_level,1) level1,key1.element_id Key_element_id,key1.USER_COL_TYPE Key_col_type, "
            + "val1.element_id val_element_id ,key1.buss_col_id key_buss_col_id ,val1.buss_col_id val_buss_col_id , "
            + "key1.buss_col_name Key_buss_col_name,val1.buss_col_name val_buss_col_name ,key1.buss_table_id Key_buss_table_id, "
            + "val1.buss_table_id val_buss_table_id ,val1.USER_COL_TYPE val_col_type,key1.member_name key_member_name , "
            + "val1.member_name val_member_name ,key1.SUB_FOLDER_id key_SUB_FOLDER_id,val1.SUB_FOLDER_id val_SUB_FOLDER_id , "
            + "key1.SUB_FOLDER_NAME key_SUB_FOLDER_NAME,val1.SUB_FOLDER_NAME val_SUB_FOLDER_NAME ,key1.disp_name key_disp_name , "
            + "key1.dim_id key_dim_id,key1.dim_name key_dim_name,val1.disp_name val_disp_name,val1.dim_id val_dim_id, "
            + "val1.dim_name val_dim_name ,key1.folder_id key_folder_id ,key1.folder_name key_folder_name, relm.rel_name, relm.is_default "
            + "FROM PRG_GRP_DIM_MAP_KEY_VALUE map "
            + " inner join PRG_USER_ALL_INFO_DETAILS key1 on (map.key1 = key1.buss_col_id  AND key1.member_id = map.key_mem_id )"
            + "  inner join PRG_USER_ALL_INFO_DETAILS val1 on ( val1.member_id = map.mem_mem_id  AND val1.buss_col_id = map.value1) "
            + "   left outer join prg_grp_dim_rel relm on (key1.dim_id = relm.dim_id   AND relm.is_default ='Y' ) "
            + " left outer join prg_grp_dim_rel_details reld on ( key1.member_id = reld.mem_id) "
            + " WHERE  key1.folder_id in(&) "
            + " AND val1.folder_id  IN(&) ORDER BY 1"}, {"addUserAllADIMDetails", "INSERT INTO PRG_USER_ALL_ADIM_DETAILS(INFO_FOLDER_ID, INFO_ELEMENT_ID, INFO_BUSS_COL_ID, REL_LEVEL, INFO_DIM_ID, INFO_DIM_NAME, INFO_MEMBER_ID, INFO_MEMBER_NAME) "
            + "select info.folder_id info_folder_id, info.element_id info_element_id, info.buss_col_id info_buss_col_id, "
            + "COALESCE(reld.rel_level,1) rel_level,info.dim_id info_dim_id,info.dim_name info_dim_name,info.member_id info_member_id, "
            + "info.member_name info_member_name "
            + "from PRG_USER_ALL_INFO_DETAILS info left outer join  "
            + "  prg_grp_dim_rel relm on ( info.dim_id = relm.dim_id ) "
            + " left outer join  prg_grp_dim_rel_details reld on ( info.member_id = reld.mem_id) "
            + " where info.folder_id in(&) and info.member_id is not null  "}, {"addUserAllAdimMaster", "INSERT INTO PRG_USER_ALL_ADIM_MASTER(REL_LEVEL, INFO_DIM_ID, INFO_DIM_NAME, INFO_MEMBER_ID, INFO_MEMBER_NAME) "
            + "select distinct  COALESCE(reld.rel_level,1) rel_level,info.dim_id info_dim_id,info.dim_name info_dim_name, "
            + "info.member_id info_member_id,info.member_name info_member_name "
            + " from PRG_USER_ALL_INFO_DETAILS info left outer join "
            + "   prg_grp_dim_rel relm on ( info.dim_id = relm.dim_id  )  "
            + "  left outer join  prg_grp_dim_rel_details reld on (info.member_id = reld.mem_id) "
            + " where info.member_id is not null "
            + " and info.folder_id in(&)"}, {"addUserAllADIMKeyValEle", "INSERT INTO  PRG_USER_ALL_ADIM_KEY_VAL_ELE( LEVEL1, KEY_ELEMENT_ID, KEY_COL_TYPE, VAL_ELEMENT_ID, KEY_BUSS_COL_ID, "
            + "VAL_BUSS_COL_ID, KEY_BUSS_COL_NAME, VAL_BUSS_COL_NAME, KEY_BUSS_TABLE_ID, VAL_BUSS_TABLE_ID, VAL_COL_TYPE, "
            + "KEY_MEMBER_NAME, VAL_MEMBER_NAME, KEY_SUB_FOLDER_NAME, VAL_SUB_FOLDER_NAME,KEY_SUB_FOLDER_ID,VAL_SUB_FOLDER_ID, KEY_DISP_NAME, KEY_DIM_ID, KEY_DIM_NAME, "
            + "VAL_DISP_NAME, VAL_DIM_ID, VAL_DIM_NAME, KEY_FOLDER_ID, KEY_FOLDER_NAME, REL_NAME, IS_DEFAULT ) "
            + "SELECT COALESCE(reld.rel_level,1) level1,key1.element_id Key_element_id,key1.USER_COL_TYPE Key_col_type, "
            + "val1.element_id val_element_id ,key1.buss_col_id key_buss_col_id ,val1.buss_col_id val_buss_col_id , "
            + "key1.buss_col_name Key_buss_col_name,val1.buss_col_name val_buss_col_name ,key1.buss_table_id Key_buss_table_id, "
            + "val1.buss_table_id val_buss_table_id ,val1.USER_COL_TYPE val_col_type,key1.member_name key_member_name , "
            + "val1.member_name val_member_name ,key1.SUB_FOLDER_NAME key_SUB_FOLDER_NAME,val1.SUB_FOLDER_NAME val_SUB_FOLDER_NAME ,key1.SUB_FOLDER_ID key_SUB_FOLDER_ID,val1.SUB_FOLDER_ID val_SUB_FOLDER_ID , "
            + "key1.disp_name key_disp_name ,key1.dim_id key_dim_id,key1.dim_name key_dim_name,val1.disp_name val_disp_name, "
            + "val1.dim_id val_dim_id,val1.dim_name val_dim_name ,key1.folder_id key_folder_id ,key1.folder_name key_folder_name "
            + ", relm.rel_name , relm.is_default "
            + " FROM PRG_GRP_DIM_MAP_KEY_VALUE map "
            + "  inner join PRG_USER_ALL_INFO_DETAILS key1 on (map.key1 = key1.buss_col_id  AND key1.member_id = map.key_mem_id) "
            + "    inner join  PRG_USER_ALL_INFO_DETAILS val1 on ( val1.member_id = map.mem_mem_id  AND val1.buss_col_id = map.value1) "
            + "  left outer join prg_grp_dim_rel relm on  (key1.dim_id = relm.dim_id)   "
            + " left outer join  prg_grp_dim_rel_details reld on (key1.member_id = reld.mem_id) "
            + " WHERE  key1.folder_id in(&) "
            + " AND val1.folder_id  IN(&) ORDER BY 1 "} //added by susheela start
        , {"getAllMemRel", "select d.REL_ID,d.MEM_ID, d.REL_LEVEL, pd.DIM_ID from prg_grp_dim_rel_details d,prg_grp_dim_rel pd where "
            + " d.rel_id in (select rel_id from prg_grp_dim_rel where dim_id in(SELECT DISTINCT gd.dim_id from prg_grp_dimensions gd,"
            + " prg_grp_dim_tables gdt,  prg_grp_dim_member gdm ,prg_grp_buss_table pgb  WHERE gd.dim_id =gdt.dim_id AND "
            + " gdm.dim_id    = gd.dim_id AND gdm.dim_tab_id= gdt.dim_tab_id AND gd.grp_id ='&' and pgb.buss_table_id=gdt.tab_id and "
            + " pgb.buss_type in('Table','Query')) and is_default='Y')  and pd.rel_id= d.rel_id order by rel_id,rel_level"}, {"getRoleCustomDrill", "SELECT DISTINCT gd.DIM_ID,gdm.MEMBER_NAME as DISP_NAME,gdt.DIM_TAB_ID,gdt.TAB_ID,gd.DIM_NAME,gdm.MEMBER_ID,"
            + " gdm.MEMBER_NAME,gdm.USE_DENOM_TABLE, gd.DEFAULT_HIERARCHY_ID, gdm.DENOM_TAB_ID FROM "
            + " PRG_GRP_DIMENSIONS gd,PRG_GRP_DIM_TABLES gdt , PRG_GRP_DIM_MEMBER gdm ,PRG_GRP_BUSS_TABLE pgb "
            + " WHERE gd.DIM_ID =gdt.DIM_ID AND gdm.DIM_ID = gd.DIM_ID AND gdm.DIM_TAB_ID= gdt.DIM_TAB_ID AND "
            + "gd.GRP_ID ='&' and pgb.BUSS_TABLE_ID=gdt.TAB_ID and pgb.BUSS_TYPE in('Table','Query') and gd.DIM_NAME!='Time' ORDER BY gd.DIM_ID"} // ,{"addRoleCustomDrill","insert into prg_grp_role_custom_drill(CUSTOM_DRILL_ID,FOLDER_ID,SUB_FOLDER_ID,MEMBER_ID,CHILD_MEMBER_ID) values(PRG_CUST_DRILL_SEQ.nextVal,'&','&','&','&')"}
        // modified by susheela 02-12-09 start
        , {"getSequenceNumber", "select &.nextval from dual"}, {"addRoleCustomDrill", "insert into prg_grp_role_custom_drill(FOLDER_ID,SUB_FOLDER_ID,MEMBER_ID,CHILD_MEMBER_ID) values(&,&,&,&)"}, {"updateCustomDrill", "update prg_grp_role_custom_drill set child_member_id=& where sub_folder_id=& and member_id=& and drill_id is null"}, {"addUserMemberFilter", "insert into prg_user_role_member_filter(USER_FILTER_ID,USER_ID,MEMBER_ID,FOLDER_ID,MEMBER_VALUE) values(USER_FILTER_ID_SEQ.nextval,'&','&','&','&')"} // modified by susheela 02-12-09 over
        , {"updateCustomDrill", "update prg_grp_role_custom_drill set child_member_id=& where sub_folder_id=& and member_id=&"} // added by susheela over
        //added by susheela start 28-11-09
        , {"checkTargetFolderForGrp", "select puf.FOLDER_NAME, tmf.BUS_FOLDER_ID,pgm.GRP_NAME,tmf.BUS_GROUP_ID from"
            + " target_measure_folder tmf, prg_user_folder puf, prg_grp_master pgm where bus_group_id "
            + " in(select grp_id from prg_user_folder where folder_id=&) and puf.folder_id= tmf.bus_folder_id and"
            + " pgm.grp_id= tmf.bus_group_id "}, {"insertTargetRole", "insert into target_measure_folder(BUS_FOLDER_ID,ONLY_FOR_TARGET,UPDATED_BY,CREATED_BY,BUS_GROUP_ID) values('&','&','&','&','&')"} // added by susheela over
        //added by bharathi reddy for roleFormula
        , {"addSubFolderTables", "INSERT INTO PRG_USER_SUB_FOLDER_TABLES(SUB_FOLDER_ID, BUSS_TABLE_ID, IS_DIMENSION, IS_FACT, IS_BUCKET, DISP_NAME,"
            + "SUB_FOLDER_TAB_ID,  USE_REPORT_MEMBER, USE_REPORT_DIM_MEMBER ) values(&,&,'N','Y','N','&',&,'Y','Y')"}, {"addSubFolderElements", "INSERT INTO PRG_USER_SUB_FOLDER_ELEMENTS(ELEMENT_ID, SUB_FOLDER_ID, BUSS_TABLE_ID, BUSS_COL_ID,"
            + "BUSS_COL_NAME, USER_COL_NAME, USER_COL_DESC, USER_COL_TYPE, SUB_FOLDER_TAB_ID,"
            + "REF_ELEMENT_ID, REF_ELEMENT_TYPE, DEFAULT_AGGREGATION, FOLDER_GROUP, USE_REPORT_FLAG, reffered_elements,ACTUAL_FORMULA) "
            + "values(&,&,&,&,'&','&','&','&',&,&,'&','&','&','&','&','&') "}, {"addUserAllInfoDets", "INSERT INTO PRG_USER_ALL_INFO_DETAILS(GRP_ID, FOLDER_ID, FOLDER_NAME, SUB_FOLDER_ID, SUB_FOLDER_NAME, "
            + "SUB_FOLDER_TYPE, SUB_FOLDER_TAB_ID, IS_DIMENSION, IS_FACT, IS_BUCKET, DISP_NAME,  "
            + " DIM_NAME, ELEMENT_ID, BUSS_TABLE_ID, BUSS_COL_ID, BUSS_COL_NAME, USER_COL_NAME,  "
            + "USER_COL_DESC, USER_COL_TYPE, REF_ELEMENT_ID, REF_ELEMENT_TYPE, MEMBER_ID, MEMBER_NAME, DENOM_QUERY,  "
            + "BUSS_TABLE_NAME, CONNECTION_ID, AGGREGATION_TYPE, ACTUAL_COL_FORMULA,reffered_elements) values "
            + "(&,&,'&',&,'&','&',&,'&','&','&','&','&',&,&,&,'&','&','&','&',&,'&',&,'&','&','&','&','&','&','&') "}, {"copyUserFolder", "INSERT INTO PRG_USER_FOLDER(FOLDER_ID, FOLDER_NAME, FOLDER_DESC,FOLDER_CREATED_ON,FOLDER_UPDATED_ON,GRP_ID) VALUES (&,'&','&',SYSDATE,SYSDATE,&)"}, {"copyUserFolderDetails", " INSERT INTO PRG_USER_FOLDER_DETAIL(FOLDER_ID, SUB_FOLDER_ID, SUB_FOLDER_NAME, SUB_FOLDER_TYPE)"
            + " select &, (PRG_USER_FOLDER_DETAIL_SEQ.NEXTVAL) as  SUB_FOLDER_ID ,SUB_FOLDER_NAME, SUB_FOLDER_TYPE from PRG_USER_FOLDER_DETAIL where folder_id=&"} //commented for adding columns table_disp_name and table_tooltip_name
        //    ,{"copyUserFolderTbales","INSERT INTO PRG_USER_SUB_FOLDER_TABLES(SUB_FOLDER_ID, BUSS_TABLE_ID, IS_DIMENSION, IS_FACT, IS_BUCKET, DISP_NAME, SUB_FOLDER_TAB_ID, DIM_ID, DIM_TAB_ID, DIM_NAME, MEMBER_ID, MEMBER_NAME, USE_DENOM_TABLE, DEFAULT_HIERARCHY_ID, DENOM_TAB_ID, USE_REPORT_MEMBER, USE_REPORT_DIM_MEMBER)"+
        //    "SELECT (select sub_folder_id from PRG_USER_FOLDER_DETAIL where folder_id=&  and sub_folder_type='&') as sub_folder_id, BUSS_TABLE_ID, IS_DIMENSION, IS_FACT, IS_BUCKET, DISP_NAME,(PRG_USER_SUB_FLDR_ELEMENTS_SEQ.NEXTVAL) as SUB_FOLDER_TAB_ID, DIM_ID, DIM_TAB_ID, DIM_NAME, MEMBER_ID, MEMBER_NAME, "+
        //    " USE_DENOM_TABLE, DEFAULT_HIERARCHY_ID, DENOM_TAB_ID, USE_REPORT_MEMBER, USE_REPORT_DIM_MEMBER FROM PRG_USER_SUB_FOLDER_TABLES "+
        //    " where USE_REPORT_MEMBER='Y' and sub_folder_id in (&)"}
        //end
        //modified for table_disp_name and table_tooltip_name
        , {"copyUserFolderTbales", "INSERT INTO PRG_USER_SUB_FOLDER_TABLES(SUB_FOLDER_ID, BUSS_TABLE_ID, IS_DIMENSION, IS_FACT, IS_BUCKET, DISP_NAME, SUB_FOLDER_TAB_ID, DIM_ID, DIM_TAB_ID, DIM_NAME, MEMBER_ID, MEMBER_NAME, USE_DENOM_TABLE, DEFAULT_HIERARCHY_ID, DENOM_TAB_ID, USE_REPORT_MEMBER, USE_REPORT_DIM_MEMBER,TABLE_DISP_NAME,TABLE_TOOLTIP_NAME)"
            + "SELECT (select sub_folder_id from PRG_USER_FOLDER_DETAIL where folder_id=&  and sub_folder_type='&') as sub_folder_id, BUSS_TABLE_ID, IS_DIMENSION, IS_FACT, IS_BUCKET, DISP_NAME,(PRG_USER_SUB_FLDR_ELEMENTS_SEQ.NEXTVAL) as SUB_FOLDER_TAB_ID, DIM_ID, DIM_TAB_ID, DIM_NAME, MEMBER_ID, MEMBER_NAME, "
            + " USE_DENOM_TABLE, DEFAULT_HIERARCHY_ID, DENOM_TAB_ID, USE_REPORT_MEMBER, USE_REPORT_DIM_MEMBER,isnull(TABLE_DISP_NAME,DISP_NAME),isnull(TABLE_TOOLTIP_NAME,DISP_NAME) FROM PRG_USER_SUB_FOLDER_TABLES "
            + " where USE_REPORT_MEMBER='Y' and sub_folder_id in (&)"}, {"copyuserFolderElements", "INSERT INTO PRG_USER_SUB_FOLDER_ELEMENTS(ELEMENT_ID, SUB_FOLDER_ID, BUSS_TABLE_ID, BUSS_COL_ID, BUSS_COL_NAME, USER_COL_NAME, USER_COL_DESC, USER_COL_TYPE, SUB_FOLDER_TAB_ID, REF_ELEMENT_ID, REF_ELEMENT_TYPE, DEFAULT_AGGREGATION, FOLDER_GROUP, USE_REPORT_FLAG, REFFERED_ELEMENTS)"
            + "SELECT (PRG_USER_SUB_FLDR_ELEMENTS_SEQ.nextval) as element_id,(select sub_folder_id from PRG_USER_FOLDER_DETAIL where folder_id=&  and sub_folder_type='&') as SUB_FOLDER_ID , BUSS_TABLE_ID, BUSS_COL_ID, BUSS_COL_NAME, USER_COL_NAME, USER_COL_DESC,"
            + "USER_COL_TYPE,& as SUB_FOLDER_TAB_ID, REF_ELEMENT_ID, REF_ELEMENT_TYPE, DEFAULT_AGGREGATION, FOLDER_GROUP,"
            + "USE_REPORT_FLAG, REFFERED_ELEMENTS FROM PRG_USER_SUB_FOLDER_ELEMENTS where sub_folder_id=& and BUSS_TABLE_ID=&"}, {"copyuserFolderElements1", "INSERT INTO PRG_USER_SUB_FOLDER_ELEMENTS(ELEMENT_ID, SUB_FOLDER_ID, BUSS_TABLE_ID, BUSS_COL_ID, BUSS_COL_NAME, USER_COL_NAME, USER_COL_DESC, USER_COL_TYPE, SUB_FOLDER_TAB_ID, REF_ELEMENT_ID, REF_ELEMENT_TYPE, DEFAULT_AGGREGATION, FOLDER_GROUP, USE_REPORT_FLAG, REFFERED_ELEMENTS)"
            + "SELECT (PRG_USER_SUB_FLDR_ELEMENTS_SEQ.nextval) as element_id,(select sub_folder_id from PRG_USER_FOLDER_DETAIL where folder_id=&  and sub_folder_type='&') as SUB_FOLDER_ID , BUSS_TABLE_ID, BUSS_COL_ID, BUSS_COL_NAME, USER_COL_NAME, USER_COL_DESC,"
            + "USER_COL_TYPE,& as SUB_FOLDER_TAB_ID, REF_ELEMENT_ID, REF_ELEMENT_TYPE, DEFAULT_AGGREGATION, FOLDER_GROUP,"
            + "USE_REPORT_FLAG, REFFERED_ELEMENTS FROM PRG_USER_SUB_FOLDER_ELEMENTS where sub_folder_id=& and BUSS_TABLE_ID=& and sub_folder_tab_id=& "}, {"copyCustomDrill", "INSERT INTO PRG_GRP_ROLE_CUSTOM_DRILL(CUSTOM_DRILL_ID, FOLDER_ID, SUB_FOLDER_ID, MEMBER_ID, CHILD_MEMBER_ID, DRILL_ID)"
            + " SELECT (PRG_CUST_DRILL_SEQ.nextval) as CUSTOM_DRILL_ID,  FOLDER_ID, (select sub_folder_id "
            + " FROM PRG_USER_FOLDER_DETAIL where FOLDER_ID=&  and SUB_FOLDER_TYPE='Dimensions') as sub_follder_id, MEMBER_ID, CHILD_MEMBER_ID, DRILL_ID "
            + " FROM PRG_GRP_ROLE_CUSTOM_DRILL where SUB_FOLDER_ID in "
            + " (select sub_folder_id  FROM PRG_USER_FOLDER_DETAIL where FOLDER_ID=&  and SUB_FOLDER_TYPE='Dimensions') "
            + " and MEMBER_ID in(select member_id from PRG_USER_SUB_FOLDER_TABLES where SUB_FOLDER_ID in"
            + " (select sub_folder_id  FROM PRG_USER_FOLDER_DETAIL where FOLDER_ID=&  and SUB_FOLDER_TYPE='Dimensions' ) and use_report_member='Y') and drill_id is null"}, {"updatecustdrill", "update table PRG_GRP_ROLE_CUSTOM_DRILL set child_member_id=& where child_member_id=&"}, {"updatecustdrill2", "update table PRG_GRP_ROLE_CUSTOM_DRILL set child_member_id=member_id where child_member_id=&"} //susheela start 11-12-09
        , {"getExtraDimsForRole", "select dim_id from prg_grp_dimensions where grp_id=& and dim_name not in('Time') minus select dim_id from prg_user_sub_folder_tables t,prg_grp_buss_table b where sub_folder_id=& and b.buss_table_id=t.buss_table_id"}, {"getCalculatedFormulas", "SELECT busstab.grp_id , puf.FOLDER_ID ,puf.FOLDER_NAME ,pufd.SUB_FOLDER_ID ,pufd.SUB_FOLDER_NAME ,"
            + " pufd.SUB_FOLDER_TYPE ,pusft.SUB_FOLDER_TAB_ID ,pusft.IS_DIMENSION ,pusft.IS_FACT ,pusft.IS_BUCKET ,pusft.DISP_NAME ,"
            + " pusft.DIM_ID ,pusft.DIM_TAB_ID ,pusft.DIM_NAME ,pusfe.ELEMENT_ID ,pusfe.BUSS_TABLE_ID ,pusfe.BUSS_COL_ID ,"
            + " pusfe.BUSS_COL_NAME ,pusfe.USER_COL_NAME ,pusfe.USER_COL_DESC,pusfe.USER_COL_TYPE ,pusfe.ref_element_id ,"
            + " pusfe.ref_element_type ,pusft.member_id ,pusft.member_name, busstab.BUSS_TABLE_NAME,connTab.conn_id, bussdet.default_aggregation,bussdet.ACTUAL_COL_FORMULA,pusfe.use_report_flag ,bussDet.REFFERED_ELEMENTS"
            + " FROM PRG_USER_FOLDER puf ,PRG_USER_FOLDER_DETAIL pufd ,PRG_USER_SUB_FOLDER_TABLES pusft ,PRG_USER_SUB_FOLDER_ELEMENTS pusfe,"
            + " PRG_GRP_BUSS_TABLE bussTab,PRG_GRP_BUSS_TABLE_DETAILS bussDet,(Select min(CONNECTION_ID) conn_id, buss_table_id "
            + " from prg_grp_buss_table_src group by buss_table_id) connTab WHERE puf.folder_id = pufd.folder_id "
            + " AND pufd.SUB_FOLDER_ID =pusft.SUB_FOLDER_ID AND pusfe.sub_folder_tab_id=pusft.sub_folder_tab_id "
            + " and busstab.buss_table_id = pusft.buss_table_id and bussdet.buss_column_id = pusfe.buss_col_id  AND pufd.sub_folder_type='Facts' "
            + " and connTab.buss_table_id = pusft.buss_table_id and pusfe.buss_col_id!=0 and puf.FOLDER_ID in(&) ORDER BY puf.folder_id, "
            + " pufd.SUB_FOLDER_NAME,pusft.SUB_FOLDER_TAB_ID "} //commented for adding columns TABLE_DISP_NAME and TABLE_TOOLTIP_NAME
        //     ,{"getFolderLevelColFormulas","insert into PRG_USER_ALL_INFO_DETAILS(GRP_ID ,FOLDER_ID ,FOLDER_NAME ,SUB_FOLDER_ID ,SUB_FOLDER_NAME ,SUB_FOLDER_TYPE ,SUB_FOLDER_TAB_ID,"+
        //            "IS_DIMENSION ,IS_FACT ,IS_BUCKET ,DISP_NAME ,DIM_ID ,DIM_TAB_ID ,DIM_NAME ,ELEMENT_ID ,BUSS_TABLE_ID ,BUSS_COL_ID ,"+
        //            " BUSS_COL_NAME ,USER_COL_NAME ,USER_COL_DESC ,USER_COL_TYPE ,REF_ELEMENT_ID ,REF_ELEMENT_TYPE ,MEMBER_ID ,MEMBER_NAME ,BUSS_TABLE_NAME ,CONNECTION_ID ,"+
        //            " AGGREGATION_TYPE , ACTUAL_COL_FORMULA,USE_REPORT_FLAG,REFFERED_ELEMENTS) SELECT busstab.grp_id , puf.FOLDER_ID ,puf.FOLDER_NAME ,pufd.SUB_FOLDER_ID ,pufd.SUB_FOLDER_NAME ,"+
        //            " pufd.SUB_FOLDER_TYPE ,pusft.SUB_FOLDER_TAB_ID ,pusft.IS_DIMENSION ,pusft.IS_FACT ,pusft.IS_BUCKET ,pusft.DISP_NAME ,"+
        //            " pusft.DIM_ID ,pusft.DIM_TAB_ID ,pusft.DIM_NAME ,pusfe.ELEMENT_ID ,pusfe.BUSS_TABLE_ID ,pusfe.BUSS_COL_ID ,"+
        //            " pusfe.BUSS_COL_NAME ,pusfe.USER_COL_NAME ,pusfe.USER_COL_DESC,pusfe.USER_COL_TYPE ,pusfe.ref_element_id ,"+
        //            " pusfe.ref_element_type ,pusft.member_id ,pusft.member_name, busstab.buss_table_name,connTab.conn_id, pusfe.default_aggregation,pusfe.ACTUAL_FORMULA,pusfe.use_report_flag,pusfe.REFFERED_ELEMENTS"+
        //            " FROM PRG_USER_FOLDER puf ,PRG_USER_FOLDER_DETAIL pufd ,PRG_USER_SUB_FOLDER_TABLES pusft ,PRG_USER_SUB_FOLDER_ELEMENTS pusfe,"+
        //            " PRG_GRP_BUSS_TABLE bussTab,(Select min(CONNECTION_ID) conn_id, buss_table_id "+
        //            " from prg_grp_buss_table_src group by buss_table_id) connTab WHERE puf.folder_id = pufd.folder_id "+
        //            " AND pufd.SUB_FOLDER_ID =pusft.SUB_FOLDER_ID AND pusfe.sub_folder_tab_id=pusft.sub_folder_tab_id "+
        //            " and busstab.buss_table_id = pusft.buss_table_id "+
        //            " and connTab.buss_table_id = pusft.buss_table_id AND  puf.FOLDER_ID in(&) and pusfe.buss_col_id=0 AND pufd.sub_folder_type='Facts'  ORDER BY puf.folder_id, "+
        //            " pufd.SUB_FOLDER_NAME,pusft.SUB_FOLDER_TAB_ID "}
        //end
        //modified for TABLE_DISP_NAME and TABLE_TOOLTIP_NAME
        , {"getFolderLevelColFormulas", "insert into PRG_USER_ALL_INFO_DETAILS(GRP_ID ,FOLDER_ID ,FOLDER_NAME ,SUB_FOLDER_ID ,SUB_FOLDER_NAME ,SUB_FOLDER_TYPE ,SUB_FOLDER_TAB_ID,"
            + "IS_DIMENSION ,IS_FACT ,IS_BUCKET ,DISP_NAME ,DIM_ID ,DIM_TAB_ID ,DIM_NAME ,ELEMENT_ID ,BUSS_TABLE_ID ,BUSS_COL_ID ,"
            + " BUSS_COL_NAME ,USER_COL_NAME ,USER_COL_DESC ,USER_COL_TYPE ,REF_ELEMENT_ID ,REF_ELEMENT_TYPE ,MEMBER_ID ,MEMBER_NAME ,BUSS_TABLE_NAME ,CONNECTION_ID ,"
            + " AGGREGATION_TYPE , ACTUAL_COL_FORMULA,USE_REPORT_FLAG,REFFERED_ELEMENTS,TABLE_DISP_NAME,TABLE_TOOLTIP_NAME) SELECT busstab.grp_id , puf.FOLDER_ID ,puf.FOLDER_NAME ,pufd.SUB_FOLDER_ID ,pufd.SUB_FOLDER_NAME ,"
            + " pufd.SUB_FOLDER_TYPE ,pusft.SUB_FOLDER_TAB_ID ,pusft.IS_DIMENSION ,pusft.IS_FACT ,pusft.IS_BUCKET ,pusft.DISP_NAME ,"
            + " pusft.DIM_ID ,pusft.DIM_TAB_ID ,pusft.DIM_NAME ,pusfe.ELEMENT_ID ,pusfe.BUSS_TABLE_ID ,pusfe.BUSS_COL_ID ,"
            + " pusfe.BUSS_COL_NAME ,pusfe.USER_COL_NAME ,pusfe.USER_COL_DESC,pusfe.USER_COL_TYPE ,pusfe.ref_element_id ,"
            + " pusfe.ref_element_type ,pusft.member_id ,pusft.member_name, busstab.buss_table_name,connTab.conn_id, pusfe.default_aggregation,pusfe.ACTUAL_FORMULA,"
            + "pusfe.use_report_flag,pusfe.REFFERED_ELEMENTS,isnull(pusft.TABLE_DISP_NAME,pusft.DISP_NAME),isnull(pusft.TABLE_TOOLTIP_NAME,pusft.DISP_NAME)"
            + " FROM PRG_USER_FOLDER puf ,PRG_USER_FOLDER_DETAIL pufd ,PRG_USER_SUB_FOLDER_TABLES pusft ,PRG_USER_SUB_FOLDER_ELEMENTS pusfe,"
            + " PRG_GRP_BUSS_TABLE bussTab,(Select min(CONNECTION_ID) conn_id, buss_table_id "
            + " from prg_grp_buss_table_src group by buss_table_id) connTab WHERE puf.folder_id = pufd.folder_id "
            + " AND pufd.SUB_FOLDER_ID =pusft.SUB_FOLDER_ID AND pusfe.sub_folder_tab_id=pusft.sub_folder_tab_id "
            + " and busstab.buss_table_id = pusft.buss_table_id "
            + " and connTab.buss_table_id = pusft.buss_table_id AND  puf.FOLDER_ID in(&) and pusfe.buss_col_id=0 AND pufd.sub_folder_type='Facts'  ORDER BY puf.folder_id, "
            + " pufd.SUB_FOLDER_NAME,pusft.SUB_FOLDER_TAB_ID "}, {"getFolderLevelColFormulasMysql", "insert into PRG_USER_ALL_INFO_DETAILS(GRP_ID ,FOLDER_ID ,FOLDER_NAME ,SUB_FOLDER_ID ,SUB_FOLDER_NAME ,SUB_FOLDER_TYPE ,SUB_FOLDER_TAB_ID,"
            + "IS_DIMENSION ,IS_FACT ,IS_BUCKET ,DISP_NAME ,DIM_ID ,DIM_TAB_ID ,DIM_NAME ,ELEMENT_ID ,BUSS_TABLE_ID ,BUSS_COL_ID ,"
            + " BUSS_COL_NAME ,USER_COL_NAME ,USER_COL_DESC ,USER_COL_TYPE ,REF_ELEMENT_ID ,REF_ELEMENT_TYPE ,MEMBER_ID ,MEMBER_NAME ,BUSS_TABLE_NAME ,CONNECTION_ID ,"
            + " AGGREGATION_TYPE , ACTUAL_COL_FORMULA,USE_REPORT_FLAG,REFFERED_ELEMENTS,TABLE_DISP_NAME,TABLE_TOOLTIP_NAME) SELECT busstab.grp_id , puf.FOLDER_ID ,puf.FOLDER_NAME ,pufd.SUB_FOLDER_ID ,pufd.SUB_FOLDER_NAME ,"
            + " pufd.SUB_FOLDER_TYPE ,pusft.SUB_FOLDER_TAB_ID ,pusft.IS_DIMENSION ,pusft.IS_FACT ,pusft.IS_BUCKET ,pusft.DISP_NAME ,"
            + " pusft.DIM_ID ,pusft.DIM_TAB_ID ,pusft.DIM_NAME ,pusfe.ELEMENT_ID ,pusfe.BUSS_TABLE_ID ,pusfe.BUSS_COL_ID ,"
            + " pusfe.BUSS_COL_NAME ,pusfe.USER_COL_NAME ,pusfe.USER_COL_DESC,pusfe.USER_COL_TYPE ,pusfe.ref_element_id ,"
            + " pusfe.ref_element_type ,pusft.member_id ,pusft.member_name, busstab.buss_table_name,connTab.conn_id, pusfe.default_aggregation,pusfe.ACTUAL_FORMULA,"
            + "pusfe.use_report_flag,pusfe.REFFERED_ELEMENTS,ifnull(pusft.TABLE_DISP_NAME,pusft.DISP_NAME),ifnull(pusft.TABLE_TOOLTIP_NAME,pusft.DISP_NAME)"
            + " FROM PRG_USER_FOLDER puf ,PRG_USER_FOLDER_DETAIL pufd ,PRG_USER_SUB_FOLDER_TABLES pusft ,PRG_USER_SUB_FOLDER_ELEMENTS pusfe,"
            + " PRG_GRP_BUSS_TABLE bussTab,(Select min(CONNECTION_ID) conn_id, buss_table_id "
            + " from prg_grp_buss_table_src group by buss_table_id) connTab WHERE puf.folder_id = pufd.folder_id "
            + " AND pufd.SUB_FOLDER_ID =pusft.SUB_FOLDER_ID AND pusfe.sub_folder_tab_id=pusft.sub_folder_tab_id "
            + " and busstab.buss_table_id = pusft.buss_table_id "
            + " and connTab.buss_table_id = pusft.buss_table_id AND  puf.FOLDER_ID in(&) and pusfe.buss_col_id=0 AND pufd.sub_folder_type='Facts'  ORDER BY puf.folder_id, "
            + " pufd.SUB_FOLDER_NAME,pusft.SUB_FOLDER_TAB_ID "}, {"getCustomFactElements", "select DISTINCT element_id,isnull(USER_COL_NAME,USER_COL_DESC),REFFERED_ELEMENTS "
            + "from PRG_USER_SUB_FOLDER_ELEMENTS where where SUB_FOLDER_TAB_ID='&' and sub_folder_type in('Facts','Formula') and use_report_flag='Y' and  (disp_name in('Formula','Calculated Facts') or disp_name is null )  order by BUSS_COL_NAME,REF_ELEMENT_TYPE"}, {"getUserFolderFactsColumns", "SELECT ELEMENT_ID,USER_COL_NAME  FROM PRG_USER_SUB_FOLDER_ELEMENTS where SUB_FOLDER_TAB_ID='&' order by ref_element_id, ref_element_type"} //added by uday
        , {"copyUser", "insert into prg_ar_users (PU_ID,PU_LOGIN_ID,PU_FIRSTNAME,PU_MIDDLENAME,PU_LASTNAME,PU_PASSWORD,PU_TYPE,PU_EMAIL,PU_CONTACTNO,PU_ADDRESS,"
            + "PU_CITY,PU_STATE,PU_COUNTRY,PU_PIN,PU_ACTIVE,PU_START_DATE,PU_END_DATE,START_PAGE,ACCOUNT_TYPE) "
            + "select &,'&',PU_FIRSTNAME,PU_MIDDLENAME,PU_LASTNAME,'&',PU_TYPE,PU_EMAIL,PU_CONTACTNO,PU_ADDRESS,"
            + "PU_CITY,PU_STATE,PU_COUNTRY,PU_PIN,PU_ACTIVE,PU_START_DATE,PU_END_DATE,START_PAGE,ACCOUNT_TYPE from prg_ar_users where pu_id=&"}, {"copyUserPrivileges", "insert into prg_ar_user_priveleges (PUR_ID,USER_ID,PRIVELEGE_ID) "
            + "select (PRG_AR_USER_PRIVI_SEQ.nextval),&,privelege_id from prg_ar_user_priveleges where user_id=&"}, {"copyReportPrivileges", "insert into PRG_AR_REPORT_PREVILAGES (PRP_ID, USER_ID, PREVILAGE_NAME) "
            + "SELECT (PRG_AR_REPORT_PREVILAGES_SEQ.nextval), &, PREVILAGE_NAME FROM PRG_AR_REPORT_PREVILAGES where user_id=&"}, {"copyTablePrivileges", "insert into PRG_AR_REPTAB_PREVILAGES (PRTP_ID, USER_ID, PREVILAGE_NAME) "
            + "SELECT (PRG_AR_REPTAB_PREVILAGES_SEQ.nextval), &, PREVILAGE_NAME FROM PRG_AR_REPTAB_PREVILAGES where user_id=&"}, {"copyGraphPrivileges", "insert into PRG_AR_REPGRP_PREVILAGES (PRGP_ID, USER_ID, PREVILAGE_NAME) "
            + "SELECT (PRG_AR_REPGRP_PREVILAGES_SEQ.nextval), &, PREVILAGE_NAME FROM PRG_AR_REPGRP_PREVILAGES where user_id=&"}, {"copyUserReports", "insert into PRG_AR_USER_REPORTS (USER_REP_ID, USER_ID, REPORT_ID, PUR_REPORT_SEQUENCE, PUR_FAV_REPORT, PUR_CUST_REPORT_NAME) "
            + "SELECT (PRG_AR_USER_REPORTS_SEQ.nextval), &, REPORT_ID, PUR_REPORT_SEQUENCE, PUR_FAV_REPORT, "
            + "PUR_CUST_REPORT_NAME FROM PRG_AR_USER_REPORTS where user_id=&"}, {"copyUserAssignment", "insert into PRG_GRP_USER_ASSIGNMENT (USER_GRP_ID, USER_ID, GRP_ID, START_DATE, END_DATE, IS_ACTIVE, USER_NAME) "
            + "SELECT (PRG_GRP_USER_ASSIGNMENT_SEQ.nextval), &, GRP_ID, START_DATE, END_DATE, IS_ACTIVE, USER_NAME FROM PRG_GRP_USER_ASSIGNMENT where user_id=&"}, {"copyUserFolderAssignment", "insert into PRG_GRP_USER_FOLDER_ASSIGNMENT (USER_ASSI_ID, USER_FOLDER_ID, USER_ID, GRP_ID, START_DATE, END_DATE) "
            + "SELECT (PRG_GRP_USER_FOLDER_ASSI_SEQ.nextval), USER_FOLDER_ID, &, GRP_ID, START_DATE, END_DATE FROM PRG_GRP_USER_FOLDER_ASSIGNMENT "
            + "where user_id=&"}, {"copyUserRoleDrill", "insert into PRG_USER_ROLE_DRILL (DRILL_SEQ_NUMBER, USER_ID, DRILL_ID, SUB_FOLDER_ID) "
            + "SELECT (USER_DRILL_ID_SEQ2.nextval), &, DRILL_ID, SUB_FOLDER_ID FROM PRG_USER_ROLE_DRILL where user_id=&"}, {"copyUserRoleMemberFilter", "insert into PRG_USER_ROLE_MEMBER_FILTER (USER_FILTER_ID, USER_ID, MEMBER_ID, FOLDER_ID, MEMBER_VALUE) "
            + "SELECT (USER_FILTER_ID_SEQ.nextval), &, MEMBER_ID, FOLDER_ID, MEMBER_VALUE FROM PRG_USER_ROLE_MEMBER_FILTER where user_id=&"}, {"copyUserStickyNote", "insert into PRG_USER_STICKYNOTE (USER_ID, REPORT_ID, STICKY_NOTES_ID, POS_LEFT, POS_TOP, POS_WIDTH, POS_HEIGHT, IS_VISIBLE, "
            + "STICKY_LABEL, S_NOTE) SELECT &, REPORT_ID, (PRG_USER_STICKYNOTE_SEQ.nextval), POS_LEFT, POS_TOP, POS_WIDTH, POS_HEIGHT, IS_VISIBLE, "
            + "STICKY_LABEL, S_NOTE FROM PRG_USER_STICKYNOTE where user_id=&"}, {"copyUserBussGroups", "insert into PRG_USER_BUSS_GRPS (USER_GRP_ID, USER_ID, GRP_ID) "
            + "SELECT (PRG_USER_BUSS_GRPS_SEQ.nextval), &, GRP_ID FROM PRG_USER_BUSS_GRPS where user_id=&"}, {"updatefolderele", "update prg_user_sub_folder_elements set use_report_flag='&' where sub_folder_tab_id = '&' "}, {"updateusrallinfodtls", "update prg_user_all_info_details set use_report_flag='&' "
            + "where element_id in (select element_id from prg_user_sub_folder_elements where sub_folder_tab_id = '&')"}, {"updateFolderPublishment", "update prg_user_folder set ispublished='&' where folder_id=&"}, {"getCommonFieldsFromTwoFolders", "select a.element_id , b.element_id ,a.FOLDER_NAME,  b.FOLDER_NAME,  a.SUB_FOLDER_NAME,  b.SUB_FOLDER_NAME,"
            + " a.SUB_FOLDER_TYPE,  b.SUB_FOLDER_TYPE,  isnull(a.DISP_NAME,'1'),  isnull(b.DISP_NAME,'1'),  isnull(a.DIM_NAME,'1'),"
            + " isnull( b.DIM_NAME,'1'),  a.BUSS_COL_NAME,  b.BUSS_COL_NAME,  a.USER_COL_NAME,  b.USER_COL_NAME,  a.USER_COL_TYPE,"
            + "  b.USER_COL_TYPE,  isnull(a.MEMBER_NAME,'0'),  isnull(b.MEMBER_NAME,'0'),  isnull(a.BUSS_TABLE_NAME,'0'),  isnull(b.BUSS_TABLE_NAME,'0')"
            + " from PRG_USER_ALL_INFO_DETAILS a, PRG_USER_ALL_INFO_DETAILS b where    a.SUB_FOLDER_NAME       =  b.SUB_FOLDER_NAME "
            + "  and  a.SUB_FOLDER_TYPE          =  b.SUB_FOLDER_TYPE    and  isnull(a.DISP_NAME,'1')       =  isnull(b.DISP_NAME,'1')   "
            + "  and  isnull(a.DIM_NAME,'1')        =  isnull(b.DIM_NAME,'1')     and  a.BUSS_COL_NAME            =  b.BUSS_COL_NAME    "
            + "  and  a.USER_COL_NAME            =  b.USER_COL_NAME     and  a.USER_COL_TYPE            =  b.USER_COL_TYPE      "
            + "  and  isnull(a.MEMBER_NAME,'0')     =  isnull(b.MEMBER_NAME,'0')    and  isnull(a.BUSS_TABLE_NAME,'0') =  isnull(b.BUSS_TABLE_NAME,'0')   "
            + "  and  a.FOLDER_ID     = & and  b.FOLDER_ID     =&  order by a.ref_element_id"}, {"getReportIdsForFolder", "select report_id from prg_ar_report_details where folder_id in(&)"}, {"getReportElementIds", "select distinct element_id from((select distinct element_id from(select DISTINCT element_id from prg_ar_query_detail where report_id=&    "
            + "  union all select DISTINCT ref_element_id from prg_ar_query_detail where report_id=&))   "
            + "  union all(select distinct element_id from prg_ar_report_param_details where report_id=&))"}, {"getCommonelesFromRepFolder", "select distinct  old_ele_id from(select a.element_id as  old_ele_id , b.element_id ,a.FOLDER_NAME,  b.FOLDER_NAME,  a.SUB_FOLDER_NAME,  b.SUB_FOLDER_NAME,"
            + " a.SUB_FOLDER_TYPE,  b.SUB_FOLDER_TYPE,  isnull(a.DISP_NAME,'1'),  isnull(b.DISP_NAME,'1'),  isnull(a.DIM_NAME,'1'),"
            + " isnull( b.DIM_NAME,'1'),  a.BUSS_COL_NAME,  b.BUSS_COL_NAME,  a.USER_COL_NAME,  b.USER_COL_NAME,  a.USER_COL_TYPE,"
            + "  b.USER_COL_TYPE,  isnull(a.MEMBER_NAME,'0'),  isnull(b.MEMBER_NAME,'0'),  isnull(a.BUSS_TABLE_NAME,'0'),  isnull(b.BUSS_TABLE_NAME,'0')"
            + " from PRG_USER_ALL_INFO_DETAILS a, PRG_USER_ALL_INFO_DETAILS b where    a.SUB_FOLDER_NAME       =  b.SUB_FOLDER_NAME "
            + "  and  a.SUB_FOLDER_TYPE          =  b.SUB_FOLDER_TYPE    and  isnull(a.DISP_NAME,'1')       =  isnull(b.DISP_NAME,'1')   "
            + "  and  isnull(a.DIM_NAME,'1')        =  isnull(b.DIM_NAME,'1')     and  a.BUSS_COL_NAME            =  b.BUSS_COL_NAME    "
            + "  and  a.USER_COL_NAME            =  b.USER_COL_NAME     and  a.USER_COL_TYPE            =  b.USER_COL_TYPE      "
            + "  and  isnull(a.MEMBER_NAME,'0')     =  isnull(b.MEMBER_NAME,'0')    and  isnull(a.BUSS_TABLE_NAME,'0') =  isnull(b.BUSS_TABLE_NAME,'0')   "
            + "  and  a.FOLDER_ID     = & and  b.FOLDER_ID     =&  order by a.ref_element_id) where old_ele_id in(&)"}, {"addReportMaster", "INSERT INTO PRG_AR_REPORT_MASTER(REPORT_ID,  REPORT_NAME,  REPORT_DESC,  REPORT_TYPE,  HELP_TEXT,  REPORT_STATUS) "
            + "    select &,  REPORT_NAME,  REPORT_DESC,  REPORT_TYPE,  HELP_TEXT,  REPORT_STATUS from PRG_AR_REPORT_MASTER where REPORT_ID=&"}, {"addReportDetails", "INSERT INTO PRG_AR_REPORT_DETAILS(REP_DTL_ID,  REPORT_ID,  BUSS_GRP_ID,  FOLDER_ID) "
            + " select PRG_AR_REPORT_DETAILS_SEQ.nextval,&,BUSS_GRP_ID,& from PRG_AR_REPORT_DETAILS where REPORT_ID=&"}, {"addReportParamDetails", "INSERT INTO PRG_AR_REPORT_PARAM_DETAILS( PARAM_DISP_ID,  REPORT_ID,  PARAM_DISP_NAME,  ELEMENT_ID,  DIM_ID,  DIM_TAB_ID,  BUSS_TABLE,"
            + " CHILD_ELEMENT_ID,  DISP_SEQ_NO,  DISPLAY_TYPE,  DEFAULT_VALUE,  REL_LEVEL,  PARAMETER_GROUPING) select PRG_AR_REPORT_PARAM_DETLS_SEQ.nextval,  &,  PARAM_DISP_NAME,  ELEMENT_ID,  DIM_ID,  DIM_TAB_ID,  BUSS_TABLE,"
            + " CHILD_ELEMENT_ID,  DISP_SEQ_NO,  DISPLAY_TYPE,  DEFAULT_VALUE,  REL_LEVEL,  PARAMETER_GROUPING from PRG_AR_REPORT_PARAM_DETAILS where REPORT_ID=&"}, {"addReportParents", "INSERT INTO PRG_AR_REPORT_PARENTS(REP_PAR_ID, REPORT_ID, PARENT_ID ) select PRG_AR_REPORT_PARENTS_SEQ.nextval, &, PARENT_ID from PRG_AR_REPORT_PARENTS where REPORT_ID=&"}, {"addReportQryDetails", "INSERT INTO PRG_AR_QUERY_DETAIL( QRY_COL_ID,  COL_SEQ,  COL_DISP_NAME,  ELEMENT_ID,  REF_ELEMENT_ID,  FOLDER_ID,  SUB_FOLDER_ID,  REPORT_ID, "
            + "  AGGREGATION_TYPE,  COLUMN_TYPE ) select PRG_AR_QUERY_DETAIL_SEQ.nextval , COL_SEQ,  COL_DISP_NAME,  ELEMENT_ID,  REF_ELEMENT_ID,  &, SUB_FOLDER_ID "
            + " , &,  AGGREGATION_TYPE,  COLUMN_TYPE from  PRG_AR_QUERY_DETAIL where report_id=&"}, {"addReportTimeMaster", " INSERT INTO PRG_AR_REPORT_TIME( REP_TIME_ID, TIME_TYPE, TIME_LEVEL, REPORT_ID) values(&,'&','&',&)"}, {"addReportTimeMasterdetails", "INSERT INTO PRG_AR_REPORT_TIME_DETAIL( REP_TIME_DTL_ID,  REP_TIME_ID,  COLUMN_NAME,  COLUMN_TYPE,  SEQUENCE,  FORM_SEQUENCE) "
            + "values(PRG_AR_REPORT_TIME_DETAIL_SEQ.nextval,&,'&','&',&,&)"}, {"addReportViewMaster", "INSERT INTO  PRG_AR_REPORT_VIEW_BY_MASTER(VIEW_BY_ID,  REPORT_ID,  VIEW_BY_SEQ,  IS_ROW_EDGE,  ROW_SEQ,  COL_SEQ,  DEFAULT_VALUE) values(&,&,&,'&',&,&,'&')"}, {"addReportViewDets", "INSERT INTO PRG_AR_REPORT_VIEW_BY_DETAILS(VIEW_BY_DTL_ID,  PARAM_DISP_ID,  VIEW_BY_ID,  DISP_SEQ_NO) values(PRG_AR_REP_VIEW_BY_DETLS_SEQ.nextval,&,&,'&')"}, {"addGraphmaster", "insert into PRG_AR_GRAPH_MASTER( GRAPH_ID,  REPORT_ID,  GRAPH_NAME,  GRAPH_FAMILY,  GRAPH_TYPE,  GRAPH_ORDER,  GRAPH_SIZE,  GRAPH_HEIGHT,  GRAPH_WIDTH,"
            + " ALLOW_LINK,  ALLOW_LABEL,  ALLOW_LEGEND,  ALLOW_TOOLTIP,  GRAPH_CLASS,  LEFT_Y_AXIS_LABEL,  RIGHT_Y_AXIS_LABEL,  X_AXIS_LABEL,"
            + " FONT_NAME,  FONT_SIZE,  FONT_COLOR,  LEGEND_LOC,  SHOW_GRID_X_AXIS,  SHOW_GRID_Y_AXIS,  BACK_COLOR,  SHOW_DATA,  ROW_VALUES,"
            + " SHOW_GT,  GRAPH_DISPLAY_ROWS ) VALUES(&,&,'&','&',&,&,&,'&','&','&','&','&','&',&,'&','&','&','&',&,'&','&','&','&','&','&','&','&','&')"}, {"addGraphDets", "INSERT INTO PRG_AR_GRAPH_DETAILS( GRAPH_COL_ID,  GRAPH_ID,  COL_NAME,  ELEMENT_ID,  COLUMN_ORDER,  QUERY_COL_ID,  AXIS)VALUES(PRG_AR_GRAPH_DETAILS_SEQ.nextval,&,'&',&,&,&,'&') "}, {"addTableMaster", "INSERT INTO PRG_AR_REPORT_TABLE_MASTER(REP_TAB_ID,  REPORT_ID,  TABLE_NAME,  TABLE_TYPE,  TABLE_SEQ,  SHOW_SUB_TOTAL,  SHOW_GRD_TOTAL,  SHOW_AVG,"
            + " SHOW_MIN,  SHOW_MAX,  SHOW_CAT_MAX,  SHOW_CAT_MIN,  APPEND_SYMBOL,  DEFAULT_SORT_COLUMN,  SHOW_ADV_SEARCH,"
            + " TABLE_DISPLAY_ROWS) values(&,&,'&','&',&,'&','&','&','&','&','&','&','&','&','&','&')"}, {"addTableDets", " INSERT INTO PRG_AR_REPORT_TABLE_DETAILS( REP_TAB_DTL_ID,  REPORT_ID,  REP_TAB_ID,  QRY_COL_ID,  DISP_FORMAT,  DISP_COLOR,  DISP_FONT,  IS_BOLD,"
            + " IS_ITALIC,  FONT_STYLE,  COL_NAME,  DISP_SEQ,  SHOW_SUB_TOTAL,  SHOW_GRD_TOTAL,  SHOW_AVG,  SHOW_MIN,"
            + " SHOW_MAX,  SHOW_CAT_MAX,  SHOW_CAT_MIN,  APPEND_SYMBOL,  COLOR_GROUP) values(PRG_AR_REPORT_TABLE_DETLS_SEQ.nextval,&,&,&,'&','&','&','&','&','&','&',&,'&','&','&','&','&','&','&','&','&')"}, {"getRelatedOldInfo", "SELECT * FROM  (SELECT a.element_id AS old_ele_id ,    b.element_id ,    a.FOLDER_NAME as old_FOLDER_NAME,    b.FOLDER_NAME,    a.SUB_FOLDER_NAME as old_SUB_FOLDER_NAME, "
            + "  b.SUB_FOLDER_NAME,    a.SUB_FOLDER_TYPE as old_SUB_FOLDER_TYPE,    b.SUB_FOLDER_TYPE,    NVL(a.DISP_NAME,'1') as old_DISP_NAME,    NVL(b.DISP_NAME,'1'),"
            + "  NVL(a.DIM_NAME,'1') as old_DIM_NAME,    NVL( b.DIM_NAME,'1'),    a.BUSS_COL_NAME as old_BUSS_COL_NAME,    b.BUSS_COL_NAME,"
            + "  a.USER_COL_NAME as old_USER_COL_NAME,    b.USER_COL_NAME,    a.USER_COL_TYPE as old_USER_COL_TYPE,    b.USER_COL_TYPE,"
            + "  NVL(a.MEMBER_NAME,'0') as old_MEMBER_NAME,    NVL(b.MEMBER_NAME,'0'),    NVL(a.BUSS_TABLE_NAME,'0') as old_BUSS_TABLE_NAME,"
            + "  NVL(b.BUSS_TABLE_NAME,'0'),     a.DIM_ID as old_DIM_ID,    b.DIM_ID,    a.DIM_TAB_ID as old_DIM_TAB_ID,    b.DIM_TAB_ID, a.FOLDER_ID as old_FOLDER_ID,   "
            + " b.FOLDER_ID,a.SUB_FOLDER_ID as old_SUB_FOLDER_ID,    b.SUB_FOLDER_ID"
            + " FROM PRG_USER_ALL_INFO_DETAILS a,    PRG_USER_ALL_INFO_DETAILS b   WHERE a.SUB_FOLDER_NAME        = b.SUB_FOLDER_NAME"
            + " AND a.SUB_FOLDER_TYPE          = b.SUB_FOLDER_TYPE   AND NVL(a.DISP_NAME,'1')       = NVL(b.DISP_NAME,'1')"
            + " AND NVL(a.DIM_NAME,'1')        = NVL(b.DIM_NAME,'1')  AND a.BUSS_COL_NAME            = b.BUSS_COL_NAME"
            + " AND a.USER_COL_NAME            = b.USER_COL_NAME  AND a.USER_COL_TYPE            = b.USER_COL_TYPE"
            + " AND NVL(a.MEMBER_NAME,'0')     = NVL(b.MEMBER_NAME,'0')   AND NVL(a.BUSS_TABLE_NAME,'0') = NVL(b.BUSS_TABLE_NAME,'0')"
            + " AND a.FOLDER_ID                = &   AND b.FOLDER_ID                =&"
            + " ORDER BY a.ref_element_id  ) WHERE old_ele_id IN(&)"}, {"getPriorMeasure", "SELECT USER_COL_DESC,REF_ELEMENT_ID,ELEMENT_ID,REF_ELEMENT_TYPE FROM PRG_USER_ALL_INFO_DETAILS WHERE REF_ELEMENT_ID=& AND REF_ELEMENT_TYPE=2"}, {"getPriorAndChangeMeasures", "select ELEMENT_ID,REF_ELEMENT_ID,REF_ELEMENT_TYPE from prg_user_all_info_details where REF_ELEMENT_ID=& and REF_ELEMENT_TYPE in(2,3,4) "}, {"getrefElementType", " SELECT REF_ELEMENT_TYPE from PRG_USER_ALL_INFO_DETAILS WHERE ELEMENT_ID=& "}, {"updatePrgUserAllinfo", "UPDATE PRG_USER_ALL_INFO_DETAILS SET BUSS_TABLE_NAME =''WHERE  USER_COL_TYPE ='summarized' AND FOLDER_ID =&"}, {"addUserFolderAssignment", "INSERT  INTO PRG_GRP_USER_FOLDER_ASSIGNMENT( USER_FOLDER_ID, USER_ID, GRP_ID, START_DATE) values(&,&,&,&)"}, {"deleteFolder", "DELETE  FROM PRG_GRP_USER_FOLDER_ASSIGNMENT where user_folder_id in (&) and user_id=&"}, {"getRprt", "select ar.report_id, pm.report_name from account_report ar,prg_ar_users pu,prg_ar_report_master pm where pu.pu_id= & and "
            + "ar.org_id=pu.account_type and pm.report_id= ar.report_id"}, {"insrtreptqry", "insert into prg_ar_user_reports(USER_ID,REPORT_ID,PUR_FAV_REPORT) values(&,&,'N')"}, {"getRpid", " SELECT REPORT_ID,FOLDER_ID FROM PRG_AR_REPORT_DETAILS where FOLDER_ID in(&)"}, {"addUserAssignment", "INSERT INTO PRG_GRP_USER_ASSIGNMENT(USER_ID, GRP_ID, START_DATE, END_DATE, IS_ACTIVE,USER_NAME,FOLDER_ID) values (&,&,&,'&','&','&',&)"}, {"isAssignedGroup", "select * from PRG_GRP_USER_ASSIGNMENT where grp_id=& and user_id=& and folder_id = &"}, {"addUserAssignmentMysql", "INSERT INTO PRG_GRP_USER_ASSIGNMENT(USER_ID, GRP_ID, START_DATE, IS_ACTIVE,USER_NAME,FOLDER_ID) values (&,&,&,'&','&',&)"} //  ,{"addExistingDimValues","insert  into PRG_GRP_DIM_MAP_KEY_VALUE SELECT key1.col_id Key1,isnull(member.col_id, key1.col_id) value1,key1.mem_id key_mem_id,isnull(member.mem_id,key1.mem_id ) mem_mem_id FROM prg_grp_dim_member_details KEY1 left outer join WHERE key1.col_type='KEY'"}
        , {"addExistingDimValues", "insert into PRG_GRP_DIM_MAP_KEY_VALUE SELECT key1.col_id Key1,isnull(member.col_id, key1.col_id) value1,key1.mem_id key_mem_id,isnull(member.mem_id,key1.mem_id ) mem_mem_id FROM prg_grp_dim_member_details KEY1 left outer join prg_grp_dim_member_details member on (key1.mem_id       = member.mem_id AND member.col_type ='VALUE') WHERE key1.col_type ='KEY'"}, {"addExistingDimValuesMysql", "INSERT INTO PRG_GRP_DIM_MAP_KEY_VALUE SELECT key1.col_id Key1,ifnull(member.col_id, key1.col_id) value1, key1.mem_id key_mem_id, ifnull(member.mem_id,key1.mem_id ) mem_mem_id FROM prg_grp_dim_member_details KEY1 LEFT OUTER JOIN prg_grp_dim_member_details member ON (key1.mem_id     = member.mem_id AND member.col_type ='VALUE') WHERE key1.col_type ='KEY'"} // added by ramesh janakuttu
        , {"insertinUserAllInfoDetails", "INSERT INTO PRG_USER_ALL_INFO_DETAILS(GRP_ID ,FOLDER_ID ,FOLDER_NAME ,SUB_FOLDER_ID ,SUB_FOLDER_NAME ,SUB_FOLDER_TYPE ,SUB_FOLDER_TAB_ID,IS_DIMENSION ,IS_FACT ,IS_BUCKET ,DISP_NAME ,DIM_ID ,DIM_TAB_ID ,"
            + "DIM_NAME ,ELEMENT_ID ,BUSS_TABLE_ID ,BUSS_COL_ID ,BUSS_COL_NAME ,USER_COL_NAME ,USER_COL_DESC ,USER_COL_TYPE ,REF_ELEMENT_ID ,REF_ELEMENT_TYPE ,MEMBER_ID ,MEMBER_NAME ,BUSS_TABLE_NAME ,CONNECTION_ID ,"
            + "AGGREGATION_TYPE ,ACTUAL_COL_FORMULA,USE_REPORT_FLAG,REFFERED_ELEMENTS,Display_Formula,TABLE_DISP_NAME,TABLE_TOOLTIP_NAME)SELECT busstab.grp_id ,puf.FOLDER_ID ,puf.FOLDER_NAME ,pufd.SUB_FOLDER_ID ,"
            + "pufd.SUB_FOLDER_NAME ,pufd.SUB_FOLDER_TYPE ,pusft.SUB_FOLDER_TAB_ID ,pusft.IS_DIMENSION ,pusft.IS_FACT ,pusft.IS_BUCKET ,pusft.DISP_NAME ,pusft.DIM_ID ,pusft.DIM_TAB_ID ,pusft.DIM_NAME ,pusfe.ELEMENT_ID ,pusfe.BUSS_TABLE_ID ,pusfe.BUSS_COL_ID ,"
            + "pusfe.BUSS_COL_NAME ,pusfe.USER_COL_NAME ,pusfe.USER_COL_DESC,pusfe.USER_COL_TYPE ,pusfe.ref_element_id ,pusfe.ref_element_type ,pusft.member_id ,pusft.member_name,busstab.buss_table_name,connTab.conn_id,"
            + "bussdet.default_aggregation,bussdet.ACTUAL_COL_FORMULA,pusfe.use_report_flag,bussdet.REFFERED_ELEMENTS,bussdet.display_formula,COALESCE(pusft.TABLE_DISP_NAME,pusft.DISP_NAME),COALESCE(pusft.TABLE_TOOLTIP_NAME,pusft.DISP_NAME) FROM PRG_USER_FOLDER puf ,"
            + "PRG_USER_FOLDER_DETAIL pufd ,PRG_USER_SUB_FOLDER_TABLES pusft ,PRG_USER_SUB_FOLDER_ELEMENTS pusfe,PRG_GRP_BUSS_TABLE bussTab,PRG_GRP_BUSS_TABLE_DETAILS bussDet ,(SELECT MIN(CONNECTION_ID) conn_id,buss_table_id FROM prg_grp_buss_table_src GROUP BY buss_table_id) connTab WHERE puf.folder_id= pufd.folder_id"
            + " AND pufd.SUB_FOLDER_ID     =pusft.SUB_FOLDER_ID AND pusfe.sub_folder_tab_id=pusft.sub_folder_tab_id AND busstab.buss_table_id  = pusft.buss_table_id AND bussdet.buss_column_id = pusfe.buss_col_id AND connTab.buss_table_id  = pusft.buss_table_id AND pusfe.buss_col_id!=0 AND puf.folder_id IN (&) AND pusft.BUSS_TABLE_ID=& AND pusfe.use_report_flag  ='Y' AND USE_REPORT_DIM_MEMBER  ='Y' ORDER BY puf.folder_id, pufd.SUB_FOLDER_NAME,pusft.SUB_FOLDER_TAB_ID"}, {"insertintoallAdimDetails", "INSERT INTO PRG_USER_ALL_ADIM_DETAILS(INFO_FOLDER_ID,INFO_ELEMENT_ID,INFO_BUSS_COL_ID,REL_LEVEL,INFO_DIM_ID,INFO_DIM_NAME,INFO_MEMBER_ID,INFO_MEMBER_NAME)SELECT info.folder_id info_folder_id,info.element_id info_element_id,info.buss_col_id info_buss_col_id,COALESCE(reld.rel_level,1) rel_level,info.dim_id info_dim_id,info.dim_name info_dim_name,info.member_id info_member_id,info.member_name info_member_name FROM PRG_USER_ALL_INFO_DETAILS info"
            + " LEFT OUTER JOIN prg_grp_dim_rel relm ON ( info.dim_id = relm.dim_id )LEFT OUTER JOIN prg_grp_dim_rel_details reld ON ( info.member_id   = reld.mem_id) WHERE info.folder_id  IN (&) and info.BUSS_TABLE_ID =& AND info.member_id IS NOT NULL"}, {"insertallDdimDetails", "INSERT INTO PRG_USER_ALL_DDIM_DETAILS(INFO_FOLDER_ID,INFO_ELEMENT_ID,INFO_BUSS_COL_ID,REL_LEVEL,INFO_DIM_ID,IMFO_DIM_NAME,INFO_MEMBER_ID,INFO_MEMBER_NAME) SELECT info.folder_id info_folder_id,info.element_id info_element_id,info.buss_col_id info_buss_col_id,isnull(reld.rel_level,1) rel_level,info.dim_id info_dim_id,info.dim_name imfo_dim_name,info.member_id info_member_id,info.member_name info_member_name FROM PRG_USER_ALL_INFO_DETAILS info"
            + " LEFT OUTER JOIN prg_grp_dim_rel relm ON ( info.dim_id = relm.dim_id ) LEFT OUTER JOIN prg_grp_dim_rel_details reld ON ( info.member_id   = reld.mem_id) WHERE  relm.is_default ='Y' AND info.member_id    IS NOT NULL AND info.folder_id IN (&) and info.BUSS_TABLE_ID =&"}, {"insertallDdimDetailsMysql", "INSERT INTO PRG_USER_ALL_DDIM_DETAILS(INFO_FOLDER_ID,INFO_ELEMENT_ID,INFO_BUSS_COL_ID,REL_LEVEL,INFO_DIM_ID,IMFO_DIM_NAME,INFO_MEMBER_ID,INFO_MEMBER_NAME) SELECT info.folder_id info_folder_id,info.element_id info_element_id,info.buss_col_id info_buss_col_id,ifnull(reld.rel_level,1) rel_level,info.dim_id info_dim_id,info.dim_name imfo_dim_name,info.member_id info_member_id,info.member_name info_member_name FROM PRG_USER_ALL_INFO_DETAILS info"
            + " LEFT OUTER JOIN prg_grp_dim_rel relm ON ( info.dim_id = relm.dim_id ) LEFT OUTER JOIN prg_grp_dim_rel_details reld ON ( info.member_id   = reld.mem_id) WHERE  relm.is_default ='Y' AND info.member_id    IS NOT NULL AND info.folder_id IN (&) and info.BUSS_TABLE_ID =&"}, {"insertintoallAdimKeyValueElements", "INSERT INTO PRG_USER_ALL_ADIM_KEY_VAL_ELE(LEVEL1,KEY_ELEMENT_ID,KEY_COL_TYPE,VAL_ELEMENT_ID,KEY_BUSS_COL_ID,VAL_BUSS_COL_ID,KEY_BUSS_COL_NAME,VAL_BUSS_COL_NAME,KEY_BUSS_TABLE_ID,VAL_BUSS_TABLE_ID,VAL_COL_TYPE,KEY_MEMBER_NAME,VAL_MEMBER_NAME,"
            + "KEY_SUB_FOLDER_NAME,VAL_SUB_FOLDER_NAME,KEY_SUB_FOLDER_ID,VAL_SUB_FOLDER_ID,KEY_DISP_NAME,KEY_DIM_ID,KEY_DIM_NAME,VAL_DISP_NAME,VAL_DIM_ID,VAL_DIM_NAME,KEY_FOLDER_ID,KEY_FOLDER_NAME,REL_NAME,IS_DEFAULT)"
            + "SELECT COALESCE(reld.rel_level,1) level1,key1.element_id Key_element_id,key1.USER_COL_TYPE Key_col_type,val1.element_id val_element_id ,key1.buss_col_id key_buss_col_id ,val1.buss_col_id val_buss_col_id ,key1.buss_col_name Key_buss_col_name,val1.buss_col_name val_buss_col_name ,key1.buss_table_id Key_buss_table_id,val1.buss_table_id val_buss_table_id ,"
            + "val1.USER_COL_TYPE val_col_type,key1.member_name key_member_name ,val1.member_name val_member_name ,key1.SUB_FOLDER_NAME key_SUB_FOLDER_NAME,val1.SUB_FOLDER_NAME val_SUB_FOLDER_NAME ,key1.SUB_FOLDER_ID key_SUB_FOLDER_ID,val1.SUB_FOLDER_ID val_SUB_FOLDER_ID ,key1.disp_name key_disp_name ,"
            + "key1.dim_id key_dim_id,key1.dim_name key_dim_name,val1.disp_name val_disp_name,val1.dim_id val_dim_id,val1.dim_name val_dim_name ,key1.folder_id key_folder_id ,key1.folder_name key_folder_name ,relm.rel_name ,relm.is_default FROM PRG_GRP_DIM_MAP_KEY_VALUE map"
            + " INNER JOIN PRG_USER_ALL_INFO_DETAILS key1 ON (map.key1= key1.buss_col_id AND key1.member_id = map.key_mem_id)INNER JOIN PRG_USER_ALL_INFO_DETAILS val1 ON ( val1.member_id  = map.mem_mem_id AND val1.buss_col_id = map.value1) LEFT OUTER JOIN prg_grp_dim_rel relm ON (key1.dim_id = relm.dim_id)"
            + "LEFT OUTER JOIN prg_grp_dim_rel_details reld ON (key1.member_id= reld.mem_id) WHERE key1.folder_id  IN (&) AND val1.folder_id IN (&) and key1.BUSS_TABLE_ID =& and val1.BUSS_TABLE_ID =& ORDER BY 1"}, {"insertallDdimkeyValueElement", "INSERT INTO PRG_USER_ALL_DDIM_KEY_VAL_ELE(LEVEL1,KEY_ELEMENT_ID,KEY_COL_TYPE,VAL_ELEMENT_ID,KEY_BUSS_COL_ID,VAL_BUSS_COL_ID,KEY_BUSS_COL_NAME,VAL_BUSS_COL_NAME,KEY_BUSS_TABLE_ID,VAL_BUSS_TABLE_ID,VAL_COL_TYPE,KEY_MEMBER_NAME,VAL_MEMBER_NAME,KEY_SUB_FOLDER_ID,"
            + "VAL_SUB_FOLDER_ID,KEY_SUB_FOLDER_NAME,VAL_SUB_FOLDER_NAME,KEY_DISP_NAME,KEY_DIM_ID,KEY_DIM_NAME,VAL_DISP_NAME,VAL_DIM_ID,VAL_DIM_NAME,KEY_FOLDER_ID,KEY_FOLDER_NAME,REL_NAME,IS_DEFAULT) SELECT isnull(reld.rel_level,1) level1,"
            + "key1.element_id Key_element_id,key1.USER_COL_TYPE Key_col_type,val1.ref_element_id val_element_id ,key1.buss_col_id key_buss_col_id ,val1.buss_col_id val_buss_col_id ,key1.buss_col_name Key_buss_col_name,val1.buss_col_name val_buss_col_name ,key1.buss_table_id Key_buss_table_id,val1.buss_table_id val_buss_table_id ,val1.USER_COL_TYPE val_col_type,key1.member_name key_member_name ,val1.member_name val_member_name ,key1.SUB_FOLDER_id key_SUB_FOLDER_id,"
            + "val1.SUB_FOLDER_id val_SUB_FOLDER_id ,key1.SUB_FOLDER_NAME key_SUB_FOLDER_NAME,val1.SUB_FOLDER_NAME val_SUB_FOLDER_NAME ,key1.disp_name key_disp_name ,key1.dim_id key_dim_id,key1.dim_name key_dim_name,val1.disp_name val_disp_name,val1.dim_id val_dim_id,val1.dim_name val_dim_name ,key1.folder_id key_folder_id ,key1.folder_name key_folder_name,relm.rel_name,relm.is_default FROM  PRG_GRP_DIM_MAP_KEY_VALUE map"
            + " INNER JOIN PRG_USER_ALL_INFO_DETAILS key1 ON (map.key1= key1.buss_col_id AND key1.member_id = map.key_mem_id) INNER JOIN PRG_USER_ALL_INFO_DETAILS val1 ON ( val1.member_id  = map.mem_mem_id AND val1.buss_col_id = map.value1)"
            + "LEFT OUTER JOIN prg_grp_dim_rel relm ON (key1.dim_id = relm.dim_id and  relm.is_default ='Y') LEFT OUTER JOIN prg_grp_dim_rel_details reld ON (key1.member_id = reld.mem_id) where  key1.folder_id    IN (&) AND val1.folder_id IN (&) and key1.BUSS_TABLE_ID =& and val1.BUSS_TABLE_ID =& ORDER BY 1"}, {"insertallDdimkeyValueElementMysql", "INSERT INTO PRG_USER_ALL_DDIM_KEY_VAL_ELE(LEVEL1,KEY_ELEMENT_ID,KEY_COL_TYPE,VAL_ELEMENT_ID,KEY_BUSS_COL_ID,VAL_BUSS_COL_ID,KEY_BUSS_COL_NAME,VAL_BUSS_COL_NAME,KEY_BUSS_TABLE_ID,VAL_BUSS_TABLE_ID,VAL_COL_TYPE,KEY_MEMBER_NAME,VAL_MEMBER_NAME,KEY_SUB_FOLDER_ID,"
            + "VAL_SUB_FOLDER_ID,KEY_SUB_FOLDER_NAME,VAL_SUB_FOLDER_NAME,KEY_DISP_NAME,KEY_DIM_ID,KEY_DIM_NAME,VAL_DISP_NAME,VAL_DIM_ID,VAL_DIM_NAME,KEY_FOLDER_ID,KEY_FOLDER_NAME,REL_NAME,IS_DEFAULT) SELECT ifnull(reld.rel_level,1) level1,"
            + "key1.element_id Key_element_id,key1.USER_COL_TYPE Key_col_type,val1.ref_element_id val_element_id ,key1.buss_col_id key_buss_col_id ,val1.buss_col_id val_buss_col_id ,key1.buss_col_name Key_buss_col_name,val1.buss_col_name val_buss_col_name ,key1.buss_table_id Key_buss_table_id,val1.buss_table_id val_buss_table_id ,val1.USER_COL_TYPE val_col_type,key1.member_name key_member_name ,val1.member_name val_member_name ,key1.SUB_FOLDER_id key_SUB_FOLDER_id,"
            + "val1.SUB_FOLDER_id val_SUB_FOLDER_id ,key1.SUB_FOLDER_NAME key_SUB_FOLDER_NAME,val1.SUB_FOLDER_NAME val_SUB_FOLDER_NAME ,key1.disp_name key_disp_name ,key1.dim_id key_dim_id,key1.dim_name key_dim_name,val1.disp_name val_disp_name,val1.dim_id val_dim_id,val1.dim_name val_dim_name ,key1.folder_id key_folder_id ,key1.folder_name key_folder_name,relm.rel_name,relm.is_default FROM  PRG_GRP_DIM_MAP_KEY_VALUE map"
            + " INNER JOIN PRG_USER_ALL_INFO_DETAILS key1 ON (map.key1= key1.buss_col_id AND key1.member_id = map.key_mem_id) INNER JOIN PRG_USER_ALL_INFO_DETAILS val1 ON ( val1.member_id  = map.mem_mem_id AND val1.buss_col_id = map.value1)"
            + "LEFT OUTER JOIN prg_grp_dim_rel relm ON (key1.dim_id = relm.dim_id and  relm.is_default ='Y') LEFT OUTER JOIN prg_grp_dim_rel_details reld ON (key1.member_id = reld.mem_id) where  key1.folder_id    IN (&) AND val1.folder_id IN (&) and key1.BUSS_TABLE_ID =& and val1.BUSS_TABLE_ID =& ORDER BY 1"}, {"insertintoallAdimMasterdetails", "INSERT INTO PRG_USER_ALL_ADIM_MASTER(REL_LEVEL,INFO_DIM_ID,INFO_DIM_NAME,INFO_MEMBER_ID,INFO_MEMBER_NAME) SELECT DISTINCT COALESCE(reld.rel_level,1) rel_level,info.dim_id info_dim_id,info.dim_name info_dim_name,info.member_id info_member_id,info.member_name info_member_name FROM PRG_USER_ALL_INFO_DETAILS info LEFT OUTER JOIN prg_grp_dim_rel relm"
            + " ON ( info.dim_id = relm.dim_id ) LEFT OUTER JOIN prg_grp_dim_rel_details reld ON (info.member_id = reld.mem_id) WHERE info.member_id IS NOT NULL AND info.folder_id  IN (&) and info.BUSS_TABLE_ID =&"}, {"insertallDdimMasterDetails", "INSERT INTO PRG_USER_ALL_DDIM_MASTER(INFO_FOLDER_ID,REL_LEVEL,INFO_DIM_ID,INFO_DIM_NAME,INFO_MEMBER_ID,INFO_MEMBER_NAME) SELECT DISTINCT info.folder_id info_folder_id,COALESCE(reld.rel_level,1) rel_level,info.dim_id info_dim_id,info.dim_name info_dim_name,info.member_id info_member_id,info.member_name info_member_name FROM PRG_USER_ALL_INFO_DETAILS info"
            + " LEFT OUTER JOIN prg_grp_dim_rel relm ON ( info.dim_id= relm.dim_id AND relm.is_default ='Y' ) LEFT OUTER JOIN prg_grp_dim_rel_details reld ON(info.member_id= reld.mem_id) WHERE info.member_id IS NOT NULL AND info.folder_id    IN (&) and info.BUSS_TABLE_ID =&"} //added by anil
        , {"getAllFactsNew", "select DISTINCT sub_folder_tab_id,isnull(table_disp_name,disp_name)as table_name,isnull(table_tooltip_name,disp_name) tol_tip_name from PRG_USER_ALL_INFO_DETAILS where folder_id in (&) and sub_folder_type in('Facts') and disp_name is not null and disp_name not in('Calculated Facts','Formula')"} //added by anil
        , {"getFactElements", "select DISTINCT element_id,isnull(USER_COL_DESC, USER_COL_NAME) USER_COL_NAME,ref_element_id as column_name,BUSS_COL_NAME, REF_ELEMENT_TYPE,REFFERED_ELEMENTS,disp_name,display_formula   "
            + "from PRG_USER_ALL_INFO_DETAILS where sub_folder_tab_id = '&' and sub_folder_type in('Facts','Formula') and use_report_flag='Y'  order by BUSS_COL_NAME,REF_ELEMENT_TYPE"}, {"insertinuserDenomSecInfoSql", "insert into PRG_USER_DENOM_SEC_INFO(ID,GRP_ID,DIM_ID,FOLDER_ID,SUB_FOLDER_ID,SUB_FOLDER_TAB_ID,DIM_TAB_ID,USER_ID,ELEMENT_ID,CLAUSE_ID,BASE_ELEMENT_ID,BASE_BUSS_TAB_ID,BASE_BUSS_COL_NAME,ELEMENT_BUSS_TAB_ID,ELEMENT_BUSS_COL_NAME) values(IDENT_CURRENT('PRG_USER_DENOM_SEC_INFO'),&,&,&,&,&,&,&,&,&,&,&,&,'&',&,'&')"} //added by sunita
        , {"getSubFolderDetails", "SELECT BUSS_TABLE_ID,SUB_FOLDER_ID FROM prg_user_sub_folder_tables WHERE SUB_FOLDER_TAB_ID = &"} //             , {"addUserSubFolderElements", "INSERT INTO PRG_USER_SUB_FOLDER_ELEMENTS ( SUB_FOLDER_ID, BUSS_TABLE_ID, BUSS_COL_ID, BUSS_COL_NAME, USER_COL_NAME,"
        //            + " USER_COL_DESC, USER_COL_TYPE, SUB_FOLDER_TAB_ID,REF_ELEMENT_ID,REF_ELEMENT_TYPE,DEFAULT_AGGREGATION,USE_REPORT_FLAG,Display_Formula) SELECT "
        //            + "& as SUB_FOLDER_ID,a.BUSS_TABLE_ID, a.buss_column_id, a.column_name,a.column_disp_name,a.column_display_desc,"
        //            + "a.column_type,& as SUB_FOLDER_TAB_ID,IDENT_CURRENT('PRG_USER_SUB_FOLDER_ELEMENTS'),1 AS REF_ELEMENT_TYPE,a.default_aggregation,'Y',a.display_formula FROM PRG_GRP_BUSS_TABLE_DETAILS a "
        //            + "where  a.buss_table_id='&'"}
        , {"deleteElementDetails", "DELETE FROM PRG_USER_SUB_FOLDER_ELEMENTS WHERE BUSS_COL_ID IN (SELECT BUSS_COLUMN_ID FROM PRG_GRP_BUSS_TABLE_DETAILS WHERE BUSS_TABLE_ID = & ) AND SUB_FOLDER_TAB_ID = &  "}, {"subFolderEleDetailsQuery", "INSERT INTO PRG_USER_SUB_FOLDER_ELEMENTS (sub_folder_id,buss_table_id,buss_col_id,buss_col_name,user_col_name,user_col_desc,user_col_type"
            + ",sub_folder_tab_id,ref_element_id,ref_element_type,default_aggregation,folder_group,use_report_flag,reffered_elements,actual_formula,display_formula,ref_dim_tab_id) (SELECT SUB_FOLDER_ID,BUSS_TABLE_ID,BUSS_COL_ID,BUSS_COL_NAME, "
            + "'&'+ BUSS_COL_NAME AS USER_COL_NAME,  '&'+ BUSS_COL_NAME AS USER_COL_DESC,USER_COL_TYPE,SUB_FOLDER_TAB_ID,ELEMENT_ID AS REF_ELEMENT_ID,& AS REF_ELEMENT_TYPE,DEFAULT_AGGREGATION,FOLDER_GROUP, "
            + "USE_REPORT_FLAG,REFFERED_ELEMENTS,ACTUAL_FORMULA,DISPLAY_FORMULA,REF_DIM_TAB_ID FROM PRG_USER_SUB_FOLDER_ELEMENTS  WHERE USER_COL_TYPE ='NUMBER' AND BUSS_TABLE_ID =& AND SUB_FOLDER_TAB_ID =&  AND REF_ELEMENT_TYPE=1)"}, {"deleteFromAllInfo", "DELETE FROM PRG_USER_ALL_INFO_DETAILS WHERE BUSS_TABLE_ID=& AND SUB_FOLDER_ID=& AND SUB_FOLDER_TAB_ID=&"}, {"insertintoAllInfo", "INSERT INTO PRG_USER_ALL_INFO_DETAILS (GRP_ID,FOLDER_ID,FOLDER_NAME,SUB_FOLDER_ID,SUB_FOLDER_NAME,SUB_FOLDER_TYPE,SUB_FOLDER_TAB_ID,IS_DIMENSION,IS_FACT,IS_BUCKET,DISP_NAME,DIM_ID,DIM_TAB_ID,DIM_NAME,ELEMENT_ID,BUSS_TABLE_ID,BUSS_COL_ID,BUSS_COL_NAME,USER_COL_NAME,USER_COL_DESC,"
            + "USER_COL_TYPE,REF_ELEMENT_ID,REF_ELEMENT_TYPE,MEMBER_ID,MEMBER_NAME,BUSS_TABLE_NAME ,CONNECTION_ID,AGGREGATION_TYPE,ACTUAL_COL_FORMULA,USE_REPORT_FLAG,REFFERED_ELEMENTS,Display_Formula,TABLE_DISP_NAME,TABLE_TOOLTIP_NAME)SELECT busstab.grp_id,puf.FOLDER_ID,puf.FOLDER_NAME,"
            + "pufd.SUB_FOLDER_ID,pufd.SUB_FOLDER_NAME,pufd.SUB_FOLDER_TYPE,pusft.SUB_FOLDER_TAB_ID,pusft.IS_DIMENSION,pusft.IS_FACT,pusft.IS_BUCKET,pusft.DISP_NAME,pusft.DIM_ID,pusft.DIM_TAB_ID,pusft.DIM_NAME,pusfe.ELEMENT_ID,pusfe.BUSS_TABLE_ID,pusfe.BUSS_COL_ID,pusfe.BUSS_COL_NAME,"
            + "pusfe.USER_COL_NAME,pusfe.USER_COL_DESC,pusfe.USER_COL_TYPE ,pusfe.ref_element_id ,pusfe.ref_element_type,pusft.member_id ,pusft.member_name,busstab.buss_table_name,connTab.conn_id,bussdet.default_aggregation,bussdet.ACTUAL_COL_FORMULA,pusfe.use_report_flag,bussdet.REFFERED_ELEMENTS,"
            + "bussdet.display_formula,ISNULL(pusft.TABLE_DISP_NAME,pusft.DISP_NAME),ISNULL(pusft.TABLE_TOOLTIP_NAME,pusft.DISP_NAME)FROM PRG_USER_FOLDER puf , PRG_USER_FOLDER_DETAIL pufd , PRG_USER_SUB_FOLDER_TABLES pusft , PRG_USER_SUB_FOLDER_ELEMENTS pusfe, PRG_GRP_BUSS_TABLE bussTab,PRG_GRP_BUSS_TABLE_DETAILS bussDet,"
            + "(SELECT MIN(CONNECTION_ID) conn_id,buss_table_id FROM prg_grp_buss_table_src  GROUP BY buss_table_id ) connTab WHERE puf.folder_id  = pufd.folder_id AND pufd.SUB_FOLDER_ID     =pusft.SUB_FOLDER_ID AND pusfe.sub_folder_tab_id=pusft.sub_folder_tab_id AND busstab.buss_table_id  = pusft.buss_table_id"
            + " AND bussdet.buss_column_id = pusfe.buss_col_id AND connTab.buss_table_id  = pusft.buss_table_id AND pusfe.buss_col_id!=0 AND pusft.BUSS_TABLE_ID = & AND pufd.SUB_FOLDER_ID =& AND PUSFT.SUB_FOLDER_TAB_ID=& AND pusfe.use_report_flag='Y'"}, {"insertintoAllInfo1", "INSERT INTO PRG_USER_ALL_INFO_DETAILS(GRP_ID,FOLDER_ID,FOLDER_NAME,SUB_FOLDER_ID,SUB_FOLDER_NAME,SUB_FOLDER_TYPE,SUB_FOLDER_TAB_ID,IS_DIMENSION,    IS_FACT,IS_BUCKET,DISP_NAME,DIM_ID,DIM_TAB_ID,DIM_NAME,ELEMENT_ID,BUSS_TABLE_ID,BUSS_COL_ID,BUSS_COL_NAME,USER_COL_NAME,USER_COL_DESC ,"
            + "USER_COL_TYPE,REF_ELEMENT_ID,REF_ELEMENT_TYPE,MEMBER_ID,MEMBER_NAME,BUSS_TABLE_NAME,CONNECTION_ID,AGGREGATION_TYPE,ACTUAL_COL_FORMULA,"
            + "USE_REPORT_FLAG,REFFERED_ELEMENTS,Display_Formula,TABLE_DISP_NAME,TABLE_TOOLTIP_NAME ) SELECT busstab.grp_id,puf.FOLDER_ID,puf.FOLDER_NAME ,"
            + "pufd.SUB_FOLDER_ID,pufd.SUB_FOLDER_NAME,pufd.SUB_FOLDER_TYPE,pusft.SUB_FOLDER_TAB_ID,pusft.IS_DIMENSION,pusft.IS_FACT,pusft.IS_BUCKET,pusft.DISP_NAME,pusft.DIM_ID,pusft.DIM_TAB_ID,pusft.DIM_NAME,pusfe.ELEMENT_ID,pusfe.BUSS_TABLE_ID,pusfe.BUSS_COL_ID,pusfe.BUSS_COL_NAME ,"
            + "pusfe.USER_COL_NAME,pusfe.USER_COL_DESC,pusfe.USER_COL_TYPE,pusfe.ref_element_id,pusfe.ref_element_type,pusft.member_id,pusft.member_name,busstab.buss_table_name,connTab.conn_id,bussdet.default_aggregation,bussdet.ACTUAL_COL_FORMULA,pusfe.use_report_flag,bussdet.REFFERED_ELEMENTS,"
            + "bussdet.display_formula,ISNULL(pusft.TABLE_DISP_NAME,pusft.DISP_NAME),ISNULL(pusft.TABLE_TOOLTIP_NAME,pusft.DISP_NAME) FROM PRG_USER_FOLDER puf ,PRG_USER_FOLDER_DETAIL pufd,PRG_USER_SUB_FOLDER_TABLES pusft,PRG_GRP_BUSS_TABLE bussTab,PRG_GRP_BUSS_TABLE_DETAILS bussDet,"
            + "(SELECT MIN(CONNECTION_ID) conn_id,buss_table_id FROM prg_grp_buss_table_src group by buss_table_id) connTab, PRG_USER_SUB_FOLDER_ELEMENTS pusfe   LEFT JOIN PRG_USER_ALL_INFO_DETAILS B ON pusfe.ELEMENT_ID = B.ELEMENT_ID where B.ELEMENT_ID is NULL AND puf.folder_id        = pufd.folder_id AND pufd.SUB_FOLDER_ID     =pusft.SUB_FOLDER_ID AND pusfe.sub_folder_tab_id=pusft.sub_folder_tab_id AND busstab.buss_table_id  = pusft.buss_table_id"
            + " AND bussdet.buss_column_id = pusfe.buss_col_id AND connTab.buss_table_id  = pusft.buss_table_id AND pusfe.buss_col_id!     =0 AND puf.folder_id  IN(&) AND pusft.BUSS_TABLE_ID = & AND pufd.SUB_FOLDER_ID =& AND PUSFT.SUB_FOLDER_TAB_ID=& AND pusfe.use_report_flag='Y' AND USE_REPORT_DIM_MEMBER='Y' ORDER BY puf.folder_id,  pufd.SUB_FOLDER_NAME,  pusft.SUB_FOLDER_TAB_ID "} //added by anil
        , {"saveFavReport1", "update prg_ar_user_reports set pur_fav_report = 'Y' WHERE report_id in (&) AND user_id=&"}, {"saveFavReport2", "update prg_ar_user_reports set pur_fav_report = 'N' WHERE report_id not in (&) AND user_id=&"}, {"getFavReport", "SELECT DISTINCT b.REPORT_ID,a.REPORT_NAME,a.REPORT_TYPE,b.PUR_REPORT_SEQUENCE FROM PRG_AR_REPORT_MASTER a,PRG_AR_USER_REPORTS b"
            + " WHERE b.USER_ID=& AND a.REPORT_ID=b.REPORT_ID AND b.PUR_FAV_REPORT='Y' order by b.PUR_REPORT_SEQUENCE"} //addded by Nazneen for Publish a Role
        , {"insertPublishInUserAllInfoDetails", "INSERT INTO PRG_USER_ALL_INFO_DETAILS (GRP_ID ,FOLDER_ID ,FOLDER_NAME , SUB_FOLDER_ID , SUB_FOLDER_NAME , SUB_FOLDER_TYPE , SUB_FOLDER_TAB_ID,  IS_DIMENSION ,  IS_FACT ,  IS_BUCKET , DISP_NAME , DIM_ID , DIM_TAB_ID ,"
            + "  DIM_NAME , ELEMENT_ID , BUSS_TABLE_ID ,BUSS_COL_ID ,BUSS_COL_NAME , USER_COL_NAME ,USER_COL_DESC , USER_COL_TYPE , REF_ELEMENT_ID , REF_ELEMENT_TYPE , MEMBER_ID ,MEMBER_NAME , BUSS_TABLE_NAME ,  CONNECTION_ID ,"
            + " AGGREGATION_TYPE , ACTUAL_COL_FORMULA,  USE_REPORT_FLAG, REFFERED_ELEMENTS, Display_Formula, TABLE_DISP_NAME,  TABLE_TOOLTIP_NAME ) SELECT busstab.grp_id ,puf.FOLDER_ID ,puf.FOLDER_NAME ,pufd.SUB_FOLDER_ID ,pufd.SUB_FOLDER_NAME ,pufd.SUB_FOLDER_TYPE ,pusft.SUB_FOLDER_TAB_ID ,pusft.IS_DIMENSION ,pusft.IS_FACT ,pusft.IS_BUCKET ,"
            + "pusft.DISP_NAME ,pusft.DIM_ID ,pusft.DIM_TAB_ID ,pusft.DIM_NAME ,pusfe.ELEMENT_ID ,pusfe.BUSS_TABLE_ID ,pusfe.BUSS_COL_ID ,pusfe.BUSS_COL_NAME ,pusfe.USER_COL_NAME ,pusfe.USER_COL_DESC,pusfe.USER_COL_TYPE ,pusfe.ref_element_id ,pusfe.ref_element_type ,pusft.member_id ,pusft.member_name,busstab.buss_table_name,connTab.conn_id,"
            + "bussdet.default_aggregation,bussdet.ACTUAL_COL_FORMULA,pusfe.use_report_flag,bussdet.REFFERED_ELEMENTS,bussdet.display_formula, COALESCE(pusft.TABLE_DISP_NAME,pusft.DISP_NAME),COALESCE(pusft.TABLE_TOOLTIP_NAME,pusft.DISP_NAME)"
            + " FROM PRG_USER_FOLDER puf , PRG_USER_FOLDER_DETAIL pufd , PRG_USER_SUB_FOLDER_TABLES pusft ,PRG_USER_SUB_FOLDER_ELEMENTS pusfe,PRG_GRP_BUSS_TABLE bussTab, PRG_GRP_BUSS_TABLE_DETAILS bussDet, (SELECT MIN(CONNECTION_ID) conn_id,   buss_table_id"
            + " FROM prg_grp_buss_table_src GROUP BY buss_table_id  ) connTab WHERE puf.folder_id        = pufd.folder_id AND pufd.SUB_FOLDER_ID     =pusft.SUB_FOLDER_ID AND pusfe.sub_folder_tab_id=pusft.sub_folder_tab_id AND busstab.buss_table_id  = pusft.buss_table_id AND bussdet.buss_column_id = pusfe.buss_col_id "
            + "AND connTab.buss_table_id  = pusft.buss_table_id AND pusfe.buss_col_id!     =0 AND puf.folder_id  IN(&) AND pusfe.use_report_flag='Y' AND USE_REPORT_DIM_MEMBER='Y' ORDER BY puf.folder_id,  pufd.SUB_FOLDER_NAME,  pusft.SUB_FOLDER_TAB_ID "}, {"insertPublishInAllAdimDetails", "INSERT INTO PRG_USER_ALL_ADIM_DETAILS  (INFO_FOLDER_ID, INFO_ELEMENT_ID,INFO_BUSS_COL_ID,REL_LEVEL, INFO_DIM_ID, INFO_DIM_NAME,  INFO_MEMBER_ID,  INFO_MEMBER_NAME) "
            + " SELECT info.folder_id info_folder_id, info.element_id info_element_id,info.buss_col_id info_buss_col_id, COALESCE(reld.rel_level,1) rel_level,info.dim_id info_dim_id, info.dim_name info_dim_name,info.member_id info_member_id,info.member_name info_member_name "
            + " FROM PRG_USER_ALL_INFO_DETAILS info , prg_grp_dim_rel relm , prg_grp_dim_rel_details reld WHERE info.member_id = reld.mem_id(+) AND info.dim_id      = relm.dim_id(+) AND info.member_id  IS NOT NULL AND info.folder_id  IN(&)"}, {"insertPublishInAllAdimKeyValEle", "INSERT INTO PRG_USER_ALL_ADIM_KEY_VAL_ELE ( LEVEL1, KEY_ELEMENT_ID, KEY_COL_TYPE, VAL_ELEMENT_ID, KEY_BUSS_COL_ID,  VAL_BUSS_COL_ID,  KEY_BUSS_COL_NAME,  VAL_BUSS_COL_NAME,  KEY_BUSS_TABLE_ID,  VAL_BUSS_TABLE_ID,  VAL_COL_TYPE,  KEY_MEMBER_NAME,  VAL_MEMBER_NAME,"
            + "  KEY_SUB_FOLDER_NAME,  VAL_SUB_FOLDER_NAME, KEY_SUB_FOLDER_ID,VAL_SUB_FOLDER_ID, KEY_DISP_NAME, KEY_DIM_ID, KEY_DIM_NAME, VAL_DISP_NAME, VAL_DIM_ID, VAL_DIM_NAME, KEY_FOLDER_ID, KEY_FOLDER_NAME,  REL_NAME,  IS_DEFAULT) "
            + " SELECT COALESCE(reld.rel_level,1) level1,key1.element_id Key_element_id, key1.USER_COL_TYPE Key_col_type,key1.ref_element_id val_element_id ,key1.buss_col_id key_buss_col_id ,key1.buss_col_id val_buss_col_id , key1.buss_col_name Key_buss_col_name, key1.buss_col_name val_buss_col_name ,key1.buss_table_id Key_buss_table_id,key1.buss_table_id val_buss_table_id ,key1.USER_COL_TYPE val_col_type,key1.member_name key_member_name ,"
            + "key1.member_name val_member_name ,key1.SUB_FOLDER_NAME key_SUB_FOLDER_NAME, key1.SUB_FOLDER_NAME val_SUB_FOLDER_NAME , key1.SUB_FOLDER_ID key_SUB_FOLDER_ID, key1.SUB_FOLDER_ID val_SUB_FOLDER_ID ,key1.disp_name key_disp_name , key1.dim_id key_dim_id,key1.dim_name key_dim_name,key1.disp_name val_disp_name, key1.dim_id val_dim_id,key1.dim_name val_dim_name ,"
            + "key1.folder_id key_folder_id , key1.folder_name key_folder_name , relm.rel_name , relm.is_default FROM PRG_GRP_DIM_MAP_KEY_VALUE map ,PRG_USER_ALL_INFO_DETAILS key1 ,prg_grp_dim_rel relm ,prg_grp_dim_rel_details reld "
            + " WHERE map.key1       = key1.buss_col_id AND key1.member_id   = map.key_mem_id AND key1.member_id   = reld.mem_id(+) AND key1.dim_id      = relm.dim_id(+) AND key1.folder_id  IN(&) "}, {"insertPublishInAllAdimMaster", "INSERT INTO PRG_USER_ALL_ADIM_MASTER  (INFO_FOLDER_ID,  REL_LEVEL, INFO_DIM_ID, INFO_DIM_NAME, INFO_MEMBER_ID,INFO_MEMBER_NAME) SELECT DISTINCT info.folder_id info_folder_id,COALESCE(reld.rel_level,1) rel_level,info.dim_id info_dim_id,info.dim_name info_dim_name,info.member_id info_member_id,info.member_name info_member_name "
            + "FROM PRG_USER_ALL_INFO_DETAILS info , prg_grp_dim_rel relm , prg_grp_dim_rel_details reld WHERE info.member_id = reld.mem_id(+) AND info.dim_id      = relm.dim_id(+) AND info.member_id  IS NOT NULL AND info.folder_id  IN(&)"}, {"insertPublishInAllDdimDetails", "INSERT INTO PRG_USER_ALL_DDIM_DETAILS  (    INFO_FOLDER_ID,    INFO_ELEMENT_ID,    INFO_BUSS_COL_ID,    REL_LEVEL,    INFO_DIM_ID,    IMFO_DIM_NAME,    INFO_MEMBER_ID,    INFO_MEMBER_NAME  ) "
            + " SELECT info.folder_id info_folder_id,  info.element_id info_element_id,  info.buss_col_id info_buss_col_id,  ifnull(reld.rel_level,1) rel_level,  info.dim_id info_dim_id,  info.dim_name imfo_dim_name,  info.member_id info_member_id,  info.member_name info_member_name "
            + " FROM PRG_USER_ALL_INFO_DETAILS info ,  prg_grp_dim_rel relm ,  prg_grp_dim_rel_details reld WHERE info.member_id   = reld.mem_id(+) AND info.dim_id        = relm.dim_id(+) AND relm.is_default(+) ='Y' "
            + " AND info.member_id    IS NOT NULL AND info.folder_id    IN(&)"}, {"insertPublishInAllDdimKeyValEle", "INSERT INTO PRG_USER_ALL_DDIM_KEY_VAL_ELE  (    LEVEL1,    KEY_ELEMENT_ID,    KEY_COL_TYPE,    VAL_ELEMENT_ID,    KEY_BUSS_COL_ID,    VAL_BUSS_COL_ID,    KEY_BUSS_COL_NAME,    VAL_BUSS_COL_NAME,    KEY_BUSS_TABLE_ID,    VAL_BUSS_TABLE_ID,    VAL_COL_TYPE,"
            + "    KEY_MEMBER_NAME,    VAL_MEMBER_NAME,    KEY_SUB_FOLDER_ID,    VAL_SUB_FOLDER_ID,    KEY_SUB_FOLDER_NAME,    VAL_SUB_FOLDER_NAME,    KEY_DISP_NAME,    KEY_DIM_ID,    KEY_DIM_NAME,    VAL_DISP_NAME,    VAL_DIM_ID,    VAL_DIM_NAME,    KEY_FOLDER_ID,    KEY_FOLDER_NAME,    REL_NAME,    IS_DEFAULT  ) "
            + " SELECT ifnull(reld.rel_level,1) level1,  key1.element_id Key_element_id,  key1.USER_COL_TYPE Key_col_type,  key1.ref_element_id val_element_id ,  key1.buss_col_id key_buss_col_id ,  key1.buss_col_id val_buss_col_id ,  key1.buss_col_name Key_buss_col_name,  key1.buss_col_name val_buss_col_name ,  key1.buss_table_id Key_buss_table_id,  key1.buss_table_id val_buss_table_id ,  key1.USER_COL_TYPE val_col_type,  key1.member_name key_member_name ,  key1.member_name val_member_name ,  key1.SUB_FOLDER_id key_SUB_FOLDER_id,"
            + "  key1.SUB_FOLDER_id val_SUB_FOLDER_id ,  key1.SUB_FOLDER_NAME key_SUB_FOLDER_NAME,  key1.SUB_FOLDER_NAME val_SUB_FOLDER_NAME ,  key1.disp_name key_disp_name ,  key1.dim_id key_dim_id,  key1.dim_name key_dim_name,  key1.disp_name val_disp_name,  key1.dim_id val_dim_id,  key1.dim_name val_dim_name ,  key1.folder_id key_folder_id ,  key1.folder_name key_folder_name,  relm.rel_name,  relm.is_default "
            + " FROM PRG_GRP_DIM_MAP_KEY_VALUE map ,  PRG_USER_ALL_INFO_DETAILS key1 ,  prg_grp_dim_rel relm ,  prg_grp_dim_rel_details reld "
            + " WHERE map.key1         = key1.buss_col_id AND key1.member_id     = map.key_mem_id AND key1.member_id     = reld.mem_id(+) AND key1.dim_id        = relm.dim_id(+) AND relm.is_default(+) ='Y' AND key1.folder_id    IN(&)"}, {"insertPublishInUserAllDdimMaster", "INSERT INTO PRG_USER_ALL_DDIM_MASTER  (    INFO_FOLDER_ID,    REL_LEVEL,    INFO_DIM_ID,    INFO_DIM_NAME,    INFO_MEMBER_ID,    INFO_MEMBER_NAME  ) "
            + " SELECT DISTINCT info.folder_id info_folder_id,  COALESCE(reld.rel_level,1) rel_level,  info.dim_id info_dim_id,  info.dim_name info_dim_name,  info.member_id info_member_id,  info.member_name info_member_name "
            + " FROM PRG_USER_ALL_INFO_DETAILS info ,  prg_grp_dim_rel relm ,  prg_grp_dim_rel_details reld WHERE info.member_id   = reld.mem_id(+) AND info.dim_id        = relm.dim_id(+) AND relm.is_default(+) ='Y' AND info.member_id    IS NOT NULL "
            + " AND info.folder_id    IN(&)"}, {"UpdateInUserFolder", "update prg_user_folder set ispublished='Y' where folder_id=&"}, {"UpdateRoleDetails", "update prg_user_folder set FOLDER_NAME='&' , FOLDER_DESC = '&' where folder_id = &"} //end of code by nazneen for Publish a Role
        //added by Nazneen for Dimension re-publish
        //For Dimensions
        , {"deleteDimPublishUserAllInfoDetails", "delete from PRG_USER_ALL_INFO_DETAILS where BUSS_TABLE_ID = & "}, {"deleteDimPublishAllADIMDetails", "delete from PRG_USER_ALL_ADIM_DETAILS where INFO_DIM_ID = & "}, {"deleteDimPublishAllADIMKeyValEle", "delete from PRG_USER_ALL_ADIM_KEY_VAL_ELE where KEY_BUSS_TABLE_ID = & "}, {"deleteDimPublishAllADIMMaster", "delete from  PRG_USER_ALL_ADIM_MASTER where INFO_DIM_ID = & "}, {"deleteDimPublishAllDDIMDetails", "delete from PRG_USER_ALL_DDIM_DETAILS where INFO_DIM_ID = & "}, {"deleteDimPublishAllDDIMKeyValEle", "delete from PRG_USER_ALL_DDIM_KEY_VAL_ELE where KEY_BUSS_TABLE_ID = & "}, {"deleteDimPublishAllDDIMMaster", "delete from PRG_USER_ALL_DDIM_MASTER where INFO_DIM_ID = & "} //added by krishan pratap
        , {"renameQryDim", "update prg_qry_dimensions set dimension_name='&', dimension_desc='&' where dimension_id=&"}, {"renameGrpDim", "update PRG_GRP_DIMENSIONS set DIM_NAME ='&', DIM_DESC='&' where QRY_DIM_ID=&"}, {"renameRepDim", "update PRG_USER_SUB_FOLDER_TABLES set DIM_NAME ='&' where dim_id in (select dim_id from prg_grp_dimensions where qry_dim_id='&')"}
    };
}
