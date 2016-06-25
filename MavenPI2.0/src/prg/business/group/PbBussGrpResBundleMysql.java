/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prg.business.group;

/**
 *
 * @author Administrator
 */
/**
 * @filename PbDataDisplayResourceBundle
 *
 * @author santhosh.kumar@progenbusiness.com @date Aug 17, 2009, 6:51:38 PM
 */
import java.io.Serializable;
import java.util.ListResourceBundle;

public class PbBussGrpResBundleMysql extends ListResourceBundle implements Serializable {

    protected Object[][] getContents() {
        return contents;
    }
    static final Object contents[][] = {
        //added by santhosh.kumar@progenbusiness.com on 01/09/09

        //added by santhosh.kumar@progenbusiness.com on 26/08/09 for inserting into buss relation master and details table
        {"getDbMasterRelations", "SELECT RELATIONSHIP_ID FROM PRG_DB_TABLE_RLT_MASTER WHERE table_id IN (&) OR table_id2 IN (&) "}, {"insertBussMasterRelations", "insert into PRG_GRP_BUSS_TABLE_RLT_MASTER (BUSS_TABLE_ID, BUSS_TABLE_ID2, REALTIONSHIP_TYPE, "
            + "ACTUAL_CLAUSE,ACTUAL_CLAUSE1,CLAUSE_TYPE)  select mv.buss_table_id  , "
            + "mv1.buss_table_id,dbm.realtionship_type,dbm.actual_clause,dbm.actual_clause1,dbm.clause_type  from   prg_db_table_rlt_master  dbm , "
            + "prg_grp_buss_table_master_view mv  , prg_grp_buss_table_master_view mv1 where mv.db_table_id = dbm.table_id  and "
            + "mv1.db_table_id = dbm.table_id2  and dbm.relationship_id='&'"}, {"getBussTableColumns", "select BUSS_TABLE_ID, BUSS_COL_ID,ACTUAL_COL_FORMULA from prg_user_all_info_details where element_id = & "}, {"getMasterTableDtls", "select DB_TABLE_ID,DB_TABLE_NAME,CONNECTION_ID,GRP_ID from prg_grp_buss_table_master_view where buss_table_id = &"}, {"getMasterTableColDtls", "select DB_COLUMN_ID,COLUMN_TYPE,TABLE_COL_NAME from prg_grp_buss_table_dtls_view where buss_table_id = & and buss_column_id = &"}, {"insertBussDetailsRelations", " INSERT INTO PRG_GRP_BUSS_TABLE_RLT_DETAILS (BUSS_RELATIONSHIP_ID,P_BUSS_TABLE_ID,"
            + " P_BUSS_COL_ID1,P_BUSS_COL_ID2,S_BUSS_TABLE_ID,S_BUSS_COL_ID1,S_BUSS_COL_ID2,JOIN_TYPE,JOIN_OPERATOR,ACTUAL_CLAUSE ) "
            + " SELECT IDENT_CURRENT('PRG_GRP_BUSS_TABLE_RLT_MASTER') AS BUSS_RELATIONSHIP_ID, "
            + "  v1.buss_table_id  AS P_BUSS_TABLE_ID  ,v1.buss_column_id AS P_BUSS_COL_ID1, CASE WHEN rlt.p_col_id2 IS NULL THEN NULL ELSE v2.buss_column_id END"
            + " P_BUSS_COL_ID2, v3.buss_table_id  AS S_BUSS_TABLE_ID,v3.buss_column_id AS S_BUSS_COL_ID1 ,CASE  WHEN rlt.s_col_id2 IS NULL  THEN NULL ELSE "
            + "v4.buss_column_id  END S_BUSS_COL_ID2,rlt.JOIN_TYPE ,rlt.JOIN_OPERATOR ,rlt.ACTUAL_CLAUSE FROM PRG_DB_TABLE_RLT_DETAILS rlt,"
            + "prg_grp_buss_table_dtls_view v1 , prg_grp_buss_table_dtls_view v2 ,prg_grp_buss_table_dtls_view v3 ,prg_grp_buss_table_dtls_view v4  "
            + " WHERE rlt.relationship_id IN   (&) AND rlt.p_table_id = v1.db_table_id AND rlt.p_col_id1  = v1.db_column_id AND rlt.p_table_id = v2.db_table_id"
            + " AND CASE WHEN rlt.p_col_id2 IS NULL THEN rlt.p_col_id1  ELSE rlt.p_col_id2   END = v2.db_column_id AND rlt.s_table_id = v3.db_table_id"
            + " AND rlt.s_col_id1  = v3.db_column_id AND rlt.s_table_id = v4.db_table_id AND   CASE  WHEN rlt.s_col_id2 IS NULL THEN rlt.s_col_id1   "
            + "  ELSE rlt.s_col_id2   END = v4.db_column_id AND v1.grp_id ='&' AND v2.grp_id ='&' AND v3.grp_id ='&' AND v4.grp_id ='&' "} //          changed by Naznen
        //          ,{"insertBussDetailsRelationsMysql"," INSERT INTO PRG_GRP_BUSS_TABLE_RLT_DETAILS (BUSS_RELATIONSHIP_ID,P_BUSS_TABLE_ID," +
        //                  " P_BUSS_COL_ID1,P_BUSS_COL_ID2,S_BUSS_TABLE_ID,S_BUSS_COL_ID1,S_BUSS_COL_ID2,JOIN_TYPE,JOIN_OPERATOR,ACTUAL_CLAUSE ) " +
        //                  " SELECT (select Last_Insert_Id(BUSS_RELATION_ID) from PRG_GRP_BUSS_TABLE_RLT_MASTER order by 1 desc limit 1) AS BUSS_RELATIONSHIP_ID, " +
        //                  "  v1.buss_table_id  AS P_BUSS_TABLE_ID  ,v1.buss_column_id AS P_BUSS_COL_ID1, CASE WHEN rlt.p_col_id2 IS NULL THEN NULL ELSE v2.buss_column_id END" +
        //                  " P_BUSS_COL_ID2, v3.buss_table_id  AS S_BUSS_TABLE_ID,v3.buss_column_id AS S_BUSS_COL_ID1 ,CASE  WHEN rlt.s_col_id2 IS NULL  THEN NULL ELSE " +
        //                  "v4.buss_column_id  END S_BUSS_COL_ID2,rlt.JOIN_TYPE ,rlt.JOIN_OPERATOR ,rlt.ACTUAL_CLAUSE FROM PRG_DB_TABLE_RLT_DETAILS rlt," +
        //                  "prg_grp_buss_table_dtls_view v1 , prg_grp_buss_table_dtls_view v2 ,prg_grp_buss_table_dtls_view v3 ,prg_grp_buss_table_dtls_view v4  " +
        //                  " WHERE rlt.relationship_id IN   (&) AND rlt.p_table_id = v1.db_table_id AND rlt.p_col_id1  = v1.db_column_id AND rlt.p_table_id = v2.db_table_id" +
        //                  " AND CASE WHEN rlt.p_col_id2 IS NULL THEN rlt.p_col_id1  ELSE rlt.p_col_id2   END = v2.db_column_id AND rlt.s_table_id = v3.db_table_id" +
        //                  " AND rlt.s_col_id1  = v3.db_column_id AND rlt.s_table_id = v4.db_table_id AND   CASE  WHEN rlt.s_col_id2 IS NULL THEN rlt.s_col_id1   " +
        //                  "  ELSE rlt.s_col_id2   END = v4.db_column_id AND v1.grp_id ='&' AND v2.grp_id ='&' AND v3.grp_id ='&' AND v4.grp_id ='&' "}
        , {"insertBussDetailsRelationsMysql", "INSERT INTO PRG_GRP_BUSS_TABLE_RLT_DETAILS (BUSS_RELATIONSHIP_ID,P_BUSS_TABLE_ID,"
            + " P_BUSS_COL_ID1,P_BUSS_COL_ID2,S_BUSS_TABLE_ID,S_BUSS_COL_ID1,S_BUSS_COL_ID2,JOIN_TYPE,JOIN_OPERATOR,ACTUAL_CLAUSE ) "
            + " SELECT (select Last_Insert_Id(BUSS_RELATION_ID) from PRG_GRP_BUSS_TABLE_RLT_MASTER order by 1 desc limit 1) AS BUSS_RELATIONSHIP_ID,"
            + "  v1.buss_table_id  AS P_BUSS_TABLE_ID  ,v1.buss_column_id AS P_BUSS_COL_ID1, CASE WHEN rlt.p_col_id2 IS NULL THEN NULL ELSE v2.buss_column_id END"
            + " P_BUSS_COL_ID2, v3.buss_table_id  AS S_BUSS_TABLE_ID,v3.buss_column_id AS S_BUSS_COL_ID1 ,CASE  WHEN rlt.s_col_id2 IS NULL  THEN NULL ELSE "
            + "v4.buss_column_id  END S_BUSS_COL_ID2,rlt.JOIN_TYPE ,rlt.JOIN_OPERATOR ,rlt.ACTUAL_CLAUSE "
            + "FROM PRG_DB_TABLE_RLT_DETAILS rlt "
            + " inner Join prg_grp_buss_table_dtls_view v1 on (rlt.p_table_id = v1.db_table_id AND rlt.p_col_id1 = v1.db_column_id) "
            + " left join prg_grp_buss_table_dtls_view v2 on (rlt.p_table_id = v2.db_table_id and rlt.p_col_id2 = v2.db_column_id AND v2.grp_id ='&' ) "
            + " inner Join prg_grp_buss_table_dtls_view v3 on (rlt.s_table_id = v3.db_table_id AND rlt.s_col_id1 = v3.db_column_id) "
            + " left join prg_grp_buss_table_dtls_view v4 on (rlt.s_table_id = v4.db_table_id and rlt.s_col_id2 = v4.db_column_id AND v4.grp_id ='&') "
            + " WHERE rlt.relationship_id IN (&) AND v1.grp_id ='&' AND v3.grp_id ='&'"}, {"getConnectionId", "SELECT  PUC.USER_NAME, PUC.PASSWORD, PUC.SERVER, PUC.SERVICE_ID, PUC.SERVICE_NAME, PUC.PORT,PUC.DSN_NAME, PUC.DB_CONNECTION_TYPE  FROM PRG_USER_CONNECTIONS PUC,prg_db_master_table PDMT WHERE PDMT.connection_id=PUC.connection_id AND pdmt.table_id=&"}, {"getBussTabIdsByDim", "SELECT GBTMV.BUSS_TABLE_ID,QDT.tab_id AS DB_TABLE_ID, QD.dimension_name, QD.dimension_desc  "
            + "FROM PRG_QRY_DIM_TABLES as QDT left outer join PRG_GRP_BUSS_TABLE_MASTER_VIEW as GBTMV on(GBTMV.db_table_id = QDT.tab_id and GBTMV.grp_id='&') "
            + "inner join PRG_QRY_DIMENSIONS QD on (QD.dimension_id =QDT.dim_id)  where QD.dimension_id =&"}, {"getBussTabIdsByDBTabIds", "SELECT GBTMV.BUSS_TABLE_ID, mt.table_id AS DB_TABLE_ID, GBTMV.grp_id FROM   prg_db_master_table as mt left outer join PRG_GRP_BUSS_TABLE_MASTER_VIEW as GBTMV on GBTMV.db_table_id= mt.table_id "
            + "  AND GBTMV.grp_id='&'  where mt.table_id  =& "}, {"insertBussTable", "insert into PRG_GRP_BUSS_TABLE(BUSS_TABLE_NAME, BUSS_DESC, BUSS_TYPE, NO_OF_NODES, DB_TABLE_ID, GRP_ID,TABLE_DISP_NAME,TABLE_TOOLTIP_NAME) "
            + " SELECT TABLE_NAME,TABLE_NAME,TABLE_TYPE,& as NO_OF_NODES,TABLE_ID,& as GRP_ID,TABLE_NAME,TABLE_NAME "
            + "FROM PRG_DB_MASTER_TABLE where TABLE_ID=&"}, {"getCurrentBussTabId", "select max(buss_table_id) from PRG_GRP_BUSS_TABLE"}, {"insertBussSrcTable", "INSERT INTO PRG_GRP_BUSS_TABLE_SRC  (BUSS_TABLE_ID ,CONNECTION_ID ,DB_TABLE_ID,SOURCE_TYPE) "
            + "SELECT (IDENT_CURRENT('PRG_GRP_BUSS_TABLE')),connection_id,table_id ,table_type  FROM prg_db_master_table "
            + "WHERE table_id= & "}, {"insertBussSrcTableMysql", "INSERT INTO PRG_GRP_BUSS_TABLE_SRC  (BUSS_TABLE_ID ,CONNECTION_ID ,DB_TABLE_ID,SOURCE_TYPE) "
            + "SELECT (select (BUSS_TABLE_ID) from PRG_GRP_BUSS_TABLE order by 1 Desc limit 1),connection_id,table_id ,table_type  FROM prg_db_master_table "
            + "WHERE table_id= & "} //modified by bharu on 12-11-09
        , {"insertBussSrcTableDetails", "insert into PRG_GRP_BUSS_TABLE_SRC_DETAILS (BUSS_SRC_ID,DB_TABLE_ID, BUSS_TABLE_ID,DB_COLUMN_ID,"
            + "COLUMN_ALIAS,COLUMN_TYPE) "
            + "SELECT (SELECT LAST_INSERT_ID(BUSS_SOURCE_ID) FROM PRG_GRP_BUSS_TABLE_SRC ORDER BY 1 DESC LIMIT 1),table_id,(SELECT LAST_INSERT_ID(BUSS_TABLE_ID) FROM PRG_GRP_BUSS_TABLE ORDER BY 1 DESC LIMIT 1),column_id, "
            + "table_col_name , "
            + "col_type FROM prg_db_master_table_details WHERE table_id=& and is_active='Y'"} //added by sunita
        , {"insertBussSrcTable1", "INSERT INTO PRG_GRP_BUSS_TABLE_SRC  (BUSS_TABLE_ID ,CONNECTION_ID ,DB_TABLE_ID,SOURCE_TYPE) "
            + "SELECT (select LAST_INSERT_ID(BUSS_TABLE_ID) FROM PRG_GRP_BUSS_TABLE ORDER BY 1 DESC LIMIT 1),connection_id,table_id ,table_type  FROM prg_db_master_table "
            + "WHERE table_id= & "}, {"insertBussSrcTableDetails1", "insert into PRG_GRP_BUSS_TABLE_SRC_DETAILS (BUSS_SRC_ID,DB_TABLE_ID, BUSS_TABLE_ID,DB_COLUMN_ID,"
            + "COLUMN_ALIAS,COLUMN_TYPE) "
            + "SELECT (select LAST_INSERT_ID(BUSS_SOURCE_ID) FROM PRG_GRP_BUSS_TABLE_SRC ORDER BY 1 DESC LIMIT 1),table_id,(select LAST_INSERT_ID(BUSS_TABLE_ID) FROM PRG_GRP_BUSS_TABLE ORDER BY 1 DESC LIMIT 1),column_id, "
            + "table_col_name , "
            + "col_type FROM prg_db_master_table_details WHERE table_id=& and is_active='Y'"}, {"updateBussTableDetails1", "update prg_grp_buss_table_details A set A.column_disp_name = concat('B_', cast(A.buss_column_id AS char)) where A.BUSS_TABLE_ID in (SELECT B.BUSS_TABLE_ID FROM PRG_GRP_BUSS_TABLE B WHERE B.GRP_ID = &)"}, {"getBusinessGroupDimTableList1", "select gdt.tab_id as buss_table_id, IFNULL(gbt.table_disp_name,gbt.buss_table_name) buss_table_name, gdt.dim_id, gdt.dim_tab_id,IFNULL(gbt.table_tooltip_name,gbt.buss_table_name) buss_table_name from prg_grp_dim_tables gdt,prg_grp_buss_table gbt where gdt.tab_id= gbt.buss_table_id and gdt.dim_id=&"} /*
         * ,{"insertBussTableDetails","insert into PRG_GRP_BUSS_TABLE_DETAILS
         * (BUSS_COLUMN_ID, BUSS_TABLE_ID, COLUMN_NAME, DB_TABLE_ID,
         * DB_COLUMN_ID, " + "BUSS_SRC_TABLE_DTL_ID," + " IS_UNION, COLUMN_TYPE,
         * ACTUAL_COL_FORMULA,ACTUAL_COL_FORMULA1, DEFAULT_AGGREGATION,
         * BUCKET_ATTACHED, COLUMN_DISP_NAME) " + " SELECT
         * (PRG_GRP_BUSS_TABLE_DETAILS_seq.nextval),BUSS_TABLE_ID,COLUMN_ALIAS,
         * DB_TABLE_ID, DB_COLUMN_ID, BUSS_SRC_TABLE_DTL_ID,'N',COLUMN_TYPE," +
         * " '', '', '', '', COLUMN_ALIAS FROM PRG_GRP_BUSS_TABLE_SRC_DETAILS
         * WHERE DB_TABLE_ID=&"}
         */ // modified by susheela start
        /*
         * ,{"insertBussTableDetails","insert into PRG_GRP_BUSS_TABLE_DETAILS
         * (BUSS_COLUMN_ID, BUSS_TABLE_ID, COLUMN_NAME, DB_TABLE_ID,
         * DB_COLUMN_ID,BUSS_SRC_TABLE_DTL_ID,IS_UNION, " + " COLUMN_TYPE,
         * ACTUAL_COL_FORMULA,ACTUAL_COL_FORMULA1, DEFAULT_AGGREGATION,
         * BUCKET_ATTACHED, COLUMN_DISP_NAME,column_display_desc," + "
         * role_flag) SELECT
         * (PRG_GRP_BUSS_TABLE_DETAILS_seq.nextval),BUSS_TABLE_ID,COLUMN_ALIAS,
         * DB_TABLE_ID, DB_COLUMN_ID," + "
         * BUSS_SRC_TABLE_DTL_ID,'N',COLUMN_TYPE,'', '',
         * decode(COLUMN_TYPE,'NUMBER','SUM'), '',
         * COLUMN_ALIAS,replace(initcap(COLUMN_ALIAS),'_',' '), " + " case when
         * column_type='NUMBER' then 'Y' else 'N' end FROM
         * PRG_GRP_BUSS_TABLE_SRC_DETAILS WHERE DB_TABLE_ID=&"}
         */ // modified by susheela start 18thNov
        /*
         * ,{"insertBussTableDetails","insert into PRG_GRP_BUSS_TABLE_DETAILS
         * (BUSS_COLUMN_ID, BUSS_TABLE_ID, COLUMN_NAME, DB_TABLE_ID,
         * DB_COLUMN_ID,BUSS_SRC_TABLE_DTL_ID,IS_UNION, " + " COLUMN_TYPE,
         * ACTUAL_COL_FORMULA,ACTUAL_COL_FORMULA1, DEFAULT_AGGREGATION,
         * BUCKET_ATTACHED, COLUMN_DISP_NAME,column_display_desc," + "
         * role_flag) SELECT
         * (PRG_GRP_BUSS_TABLE_DETAILS_seq.nextval),BUSS_TABLE_ID,COLUMN_ALIAS,
         * DB_TABLE_ID, DB_COLUMN_ID," + "
         * BUSS_SRC_TABLE_DTL_ID,'N',COLUMN_TYPE,'', '', case when
         * column_type='NUMBER' then 'SUM' else '' end, '',
         * COLUMN_ALIAS,replace(initcap(COLUMN_ALIAS),'_',' '), " + " case when
         * column_type='NUMBER' then 'Y' else 'N' end FROM
         * PRG_GRP_BUSS_TABLE_SRC_DETAILS WHERE DB_TABLE_ID=&"}
         */, {"insertBussTableDetails", "insert into PRG_GRP_BUSS_TABLE_DETAILS (BUSS_TABLE_ID, COLUMN_NAME, DB_TABLE_ID, DB_COLUMN_ID,BUSS_SRC_TABLE_DTL_ID,IS_UNION, "
            + " COLUMN_TYPE, ACTUAL_COL_FORMULA,ACTUAL_COL_FORMULA1, DEFAULT_AGGREGATION, BUCKET_ATTACHED, COLUMN_DISP_NAME,column_display_desc,"
            + " role_flag) SELECT d.BUSS_TABLE_ID,d.COLUMN_ALIAS, d.DB_TABLE_ID,  d.DB_COLUMN_ID, "
            + " d.BUSS_SRC_TABLE_DTL_ID,'N',d.COLUMN_TYPE,'',  '', case when d.column_type='NUMBER' then 'SUM' else '' end, '', "
            + " d.COLUMN_ALIAS,replace(d.COLUMN_ALIAS,'_',' '),  case when d.column_type='NUMBER' then 'Y'  else 'N' "
            + " end  FROM PRG_GRP_BUSS_TABLE_SRC_DETAILS d,prg_grp_buss_table m WHERE d.DB_TABLE_ID=&  and m.grp_id in (&)"
            + " and d.buss_table_id= m.buss_table_id "} // modified by susheela over
        // modified by susheela over
        , {"insertGrpDim", "insert into PRG_GRP_DIMENSIONS ( DIM_NAME, DIM_DESC, DIM_ACTIVE,GRP_ID,QRY_DIM_ID) SELECT  "
            + " DIMENSION_NAME, DIMENSION_DESC, PRG_DIM_ACTIVE,& as GRP_ID,& AS QRY_DIM_ID FROM PRG_QRY_DIMENSIONS where dimension_id=& "}, {"getQryDimTables", "select BTMV.buss_table_id ,qdt.dim_tab_id from PRG_QRY_DIM_TABLES QDT, PRG_GRP_BUSS_TABLE_MASTER_VIEW BTMV WHERE "
            + "BTMV.db_table_id= qdt.tab_id AND QDT.dim_id=& and BTMV.grp_id=& "} /*
         * ,{"insertGrpDimTables"," insert into PRG_GRP_DIM_TABLES (DIM_TAB_ID,
         * DIM_ID, TAB_ID) select & as DIM_TAB_ID," +
         * "(PRG_GRP_DIMENSIONS_SEQ.currval),BTMV.buss_table_id from
         * PRG_QRY_DIM_TABLES QDT,PRG_GRP_BUSS_TABLE_MASTER_VIEW BTMV " + "WHERE
         * BTMV.db_table_id= qdt.tab_id AND QDT.dim_id=& and
         * btmv.buss_table_id=&"}
         */ //modified by susheela start
        , {"insertGrpDimTables", "  insert into PRG_GRP_DIM_TABLES ( DIM_ID, TAB_ID,DB_DIM_ID,DB_TAB_ID)  select "
            + "ident_current('PRG_GRP_DIMENSIONS'),BTMV.buss_table_id,QDT.dim_id DB_DIM_ID,QDT.tab_id DB_TAB_ID from PRG_QRY_DIM_TABLES QDT,PRG_GRP_BUSS_TABLE_MASTER_VIEW BTMV "
            + "WHERE BTMV.db_table_id= qdt.tab_id AND QDT.dim_id=& and btmv.buss_table_id=&"} //modified by susheela over
        //modified by sunita for mysql
        , {"insertGrpDimTables1", "  insert into PRG_GRP_DIM_TABLES ( DIM_ID, TAB_ID,DB_DIM_ID,DB_TAB_ID)  select "
            + "(select LAST_INSERT_ID(DIM_ID) FROM PRG_GRP_DIMENSIONS ORDER BY 1 DESC LIMIT 1),BTMV.buss_table_id,QDT.dim_id as DB_DIM_ID,QDT.tab_id as DB_TAB_ID from PRG_QRY_DIM_TABLES QDT,PRG_GRP_BUSS_TABLE_MASTER_VIEW BTMV "
            + "WHERE BTMV.db_table_id= qdt.tab_id AND QDT.dim_id=& and btmv.buss_table_id=&"}, {"insertGrpDimTablesDetails1", "INSERT INTO PRG_GRP_DIM_TAB_DETAILS(DIM_TAB_ID, COL_ID, IS_AVAILABLE, IS_PK_KEY) "
            + "SELECT (select LAST_INSERT_ID(DIM_TAB_ID) FROM PRG_GRP_DIM_TABLES ORDER BY 1 DESC LIMIT 1) AS DIM_TAB_ID,btdv.buss_column_id, "
            + "dtd.is_available AS COL_ID, dtd.is_pk_key FROM PRG_GRP_BUSS_TABLE_DTLS_VIEW BTDV,PRG_QRY_DIM_TAB_DETAILS DTD "
            + "WHERE btdv.db_column_id= dtd.col_id AND dtd.dim_tab_id=& and dtd.is_available='Y' and BTDV.grp_id=&"} //modified by bharu on 12-11-09
        , {"insertGrpDimTablesDetails", "INSERT INTO PRG_GRP_DIM_TAB_DETAILS(DIM_TAB_ID, COL_ID, IS_AVAILABLE, IS_PK_KEY) "
            + "SELECT IDENT_CURRENT('PRG_GRP_DIM_TABLES') AS DIM_TAB_ID,btdv.buss_column_id, "
            + "dtd.is_available AS COL_ID, dtd.is_pk_key FROM PRG_GRP_BUSS_TABLE_DTLS_VIEW BTDV,PRG_QRY_DIM_TAB_DETAILS DTD "
            + "WHERE btdv.db_column_id= dtd.col_id AND dtd.dim_tab_id=& and dtd.is_available='Y' and BTDV.grp_id=&"}, {"getQryDimMbrsInfo", "SELECT QDM.member_name,QDM.use_denom_table,QDM.denom_tab_id,QDM.denom_query,qdm.dim_tab_id, qdm.member_id, qdm.member_order_by, qdm.isnullvalue "
            + "FROM PRG_QRY_DIM_TABLES QDT, PRG_QRY_DIM_MEMBER QDM WHERE qdt.dim_id= qdm.dim_id AND qdt.dim_tab_id= qdm.dim_tab_id AND qdt.dim_id=&"} /*
         * ,{"insertGrpDimMembers"," INSERT INTO PRG_GRP_DIM_MEMBER (MEMBER_ID,
         * MEMBER_NAME, DIM_ID, DIM_TAB_ID, USE_DENOM_TABLE, DENOM_TAB_ID,
         * DENOM_QUERY," + "MEMBER_DESC)
         * VALUES(&,'&',(PRG_GRP_DIMENSIONS_SEQ.CURRVAL),&,'&',(SELECT
         * buss_table_id " + "FROM prg_grp_buss_table_master_view WHERE
         * db_table_id='&'),'&', '&' )"}
         */ // modified by susheela start
        //commented by veena
        //          ,{"insertGrpDimMembers"," INSERT INTO PRG_GRP_DIM_MEMBER ( MEMBER_NAME, DIM_ID, DIM_TAB_ID, USE_DENOM_TABLE, DENOM_TAB_ID, DENOM_QUERY," +
        //                  "MEMBER_DESC,MEMBER_ORDER_BY,ISNULLVALUE) VALUES('&',ident_current('PRG_GRP_DIMENSIONS'),&,'&',(SELECT  buss_table_id " +
        //                  "FROM prg_grp_buss_table_master_view WHERE db_table_id='&' and grp_id=&),'&', '&',&,'&' )"}
        // modified by susheela over
        //modified by veena for inserting dimesion on sql server
        , {"insertGrpDimMembers", "INSERT INTO PRG_GRP_DIM_MEMBER ( MEMBER_NAME, DIM_ID, DIM_TAB_ID, USE_DENOM_TABLE, DENOM_TAB_ID, DENOM_QUERY,"
            + "MEMBER_DESC,MEMBER_ORDER_BY,ISNULLVALUE) SELECT '&',ident_current('PRG_GRP_DIMENSIONS'),&,'&',(SELECT  buss_table_id "
            + "FROM prg_grp_buss_table_master_view WHERE db_table_id='&' and grp_id=&),'&', '&',&,'&' "} //modifications over
        //modified by sunita for inserting dimesion on mysql
        , {"insertGrpDimMembers1", "INSERT INTO PRG_GRP_DIM_MEMBER ( MEMBER_NAME, DIM_ID, DIM_TAB_ID, USE_DENOM_TABLE, DENOM_TAB_ID, DENOM_QUERY,"
            + "MEMBER_DESC,MEMBER_ORDER_BY,ISNULLVALUE) SELECT '&',(select LAST_INSERT_ID(DIM_ID) FROM PRG_GRP_DIMENSIONS ORDER BY 1 DESC LIMIT 1),&,'&',(SELECT  buss_table_id "
            + "FROM prg_grp_buss_table_master_view WHERE db_table_id='&' and grp_id=&),'&', '&',&,'&' "}, {"insertGrpDimMembersDtls1", "insert into PRG_GRP_DIM_MEMBER_DETAILS(MEM_ID, COL_ID, COL_TYPE)"
            + " select (select LAST_INSERT_ID(MEMBER_ID) FROM PRG_GRP_DIM_MEMBER ORDER BY 1 DESC LIMIT 1) as mem_id,btdv.buss_column_id,"
            + "dmd.col_type FROM PRG_QRY_DIM_MEMBER_DETAILS dmd, prg_grp_buss_table_dtls_view btdv where dmd.col_id= btdv.db_column_id and  dmd.mem_id=& and btdv.grp_id=&"}, {"insertGrpDimRel1", "insert into PRG_GRP_DIM_REL (DIM_ID, DESCRIPTION, IS_DEFAULT, REL_NAME) select "
            + "(select LAST_INSERT_ID(DIM_ID) FROM PRG_GRP_DIMENSIONS ORDER BY 1 DESC LIMIT 1),description, is_default, rel_name from prg_qry_dim_rel where dim_id='&' and rel_id='&'  "}, {"insertGrpDimRelDtls1", "INSERT INTO PRG_GRP_DIM_REL_DETAILS(REL_ID, MEM_ID, REL_LEVEL) SELECT (select LAST_INSERT_ID(REL_ID) FROM PRG_GRP_DIM_REL ORDER BY 1 DESC LIMIT 1) AS REL_ID,&,rel_level "
            + "FROM PRG_QRY_DIM_REL_DETAILS where rel_id='&' and mem_id='&'"} //modifications over
        , {"insertGrpDimMembersDtls", "insert into PRG_GRP_DIM_MEMBER_DETAILS(MEM_ID, COL_ID, COL_TYPE)"
            + " select ident_current('PRG_GRP_DIM_MEMBER') as mem_id,btdv.buss_column_id,"
            + "dmd.col_type FROM PRG_QRY_DIM_MEMBER_DETAILS dmd, prg_grp_buss_table_dtls_view btdv where dmd.col_id= btdv.db_column_id and  dmd.mem_id=& and btdv.grp_id=&"}, {"getQryDimRel", "select DESCRIPTION, IS_DEFAULT, REL_NAME, REL_ID from prg_qry_dim_rel where DIM_ID='&'"}, {"insertGrpDimRel", "insert into PRG_GRP_DIM_REL (DIM_ID, DESCRIPTION, IS_DEFAULT, REL_NAME) select "
            + "ident_current('PRG_GRP_DIMENSIONS'),description, is_default, rel_name from prg_qry_dim_rel where dim_id='&' and rel_id='&'  "}, {"getQryDimRelDtls", "SELECT rel_level, mem_id FROM PRG_QRY_DIM_MBR_REL_DTLS_VIEW where dim_id='&' and rel_id='&'"}, {"insertGrpDimRelDtls", "INSERT INTO PRG_GRP_DIM_REL_DETAILS(REL_ID, MEM_ID, REL_LEVEL) SELECT IDENT_CURRENT('PRG_GRP_DIM_REL') AS REL_ID,&,rel_level "
            + "FROM PRG_QRY_DIM_REL_DETAILS where rel_id='&' and mem_id='&'"} //end of queries added by santhosh.kumar@progenbusiness.com
        , {"getBussGrpId", "SELECT PRG_GRP_MASTER_SEQ.NEXTVAL FROM DUAL"}, {"createBussGrp", "insert into PRG_GRP_MASTER(GRP_DESC,GRP_NAME,CONNECTION_ID) values('&','&',&)"}, {"getAllTables", ""} // ,{"getBusinessGroupList", "select grp_id, grp_name  from prg_grp_master"}
        , {"getBusinessGroupDimList", "SELECT DIM_ID, DIM_NAME,DIM_ACTIVE, DEFAULT_HIERARCHY_ID FROM PRG_GRP_DIMENSIONS where grp_id=& order by DIM_ID desc"}, {"getBusinessGroupDimTableList", "select gdt.tab_id as buss_table_id, ISNULL(gbt.table_disp_name,gbt.buss_table_name) buss_table_name, gdt.dim_id, gdt.dim_tab_id,ISNULL(gbt.table_tooltip_name,gbt.buss_table_name) buss_table_name from prg_grp_dim_tables gdt,prg_grp_buss_table gbt where gdt.tab_id= gbt.buss_table_id and gdt.dim_id=&"}, {"getBusinessGroupDimTableListMysql", "select gdt.tab_id as buss_table_id, IfNULL(gbt.table_disp_name,gbt.buss_table_name) buss_table_name, gdt.dim_id, gdt.dim_tab_id,IfNULL(gbt.table_tooltip_name,gbt.buss_table_name) buss_table_name from prg_grp_dim_tables gdt,prg_grp_buss_table gbt where gdt.tab_id= gbt.buss_table_id and gdt.dim_id=&"}, {"getBusinessGroupDimTableColList", "SELECT gbtd.buss_column_id, case when gbtd.column_display_desc is null then  upper(REPLACE(gbtd.column_name,'_',' ')) else gbtd.column_display_desc end as column_disp_name ,"
            + "  gdtd.is_pk_key, gdtd.is_available,gbtd.db_column_id, gbtd.column_type FROM PRG_GRP_DIM_TABLES gdt, PRG_GRP_DIM_TAB_DETAILS gdtd, PRG_GRP_BUSS_TABLE_DETAILS gbtd"
            + " WHERE gdt.dim_tab_id=gdtd.dim_tab_id AND gdtd.col_id = gbtd.buss_column_id AND gdt.dim_id      ='&' AND gdt.tab_id      ='&' "} //,{"getBusinessGroupDimTableColList", " select distinct m.buss_column_id,m.column_name,d.is_pk_key,d.is_available,d.col_id,m.column_type from prg_grp_dim_tab_details d, prg_grp_buss_table_details m where m.db_column_id= d.col_id and m.buss_table_id=&"}
        , {"getBusinessGroupDimMemList", "select member_id, member_name from prg_grp_dim_member where dim_id=&"}, {"getBusinessGroupDimHieList", "select rel_id, rel_name from prg_grp_dim_rel where dim_id=&"}, {"getBusinessGroupDimHieColList", "select m.member_id,m.member_name from prg_grp_dim_member m, prg_grp_dim_rel_details d where m.member_id= d.mem_id and d.rel_id=& order by d.rel_level"}, {"getBusinessGroupAllTablesList", "select BUSS_TABLE_ID, COALESCE(TABLE_DISP_NAME,BUSS_TABLE_NAME) BUSS_TABLE_NAME, DB_TABLE_ID,COALESCE(TABLE_TOOLTIP_NAME,BUSS_TABLE_NAME) BUSS_TABLE_TOOLTIP_NAME from prg_grp_buss_table where grp_id=& and buss_type!='Query' and buss_table_name!='PROGEN_TIME' "}, {"getBusinessGroupAllTablesColList", " SELECT btd.BUSS_COLUMN_ID,btd.COLUMN_NAME,btd.DB_COLUMN_ID,"
            + " btd.column_type ,btd.db_table_id, mstr.TABLE_NAME FROM "
            + " PRG_GRP_BUSS_TABLE_DETAILS btd,PRG_GRP_BUSS_TABLE bt,"
            + " prg_db_master_table mstr  WHERE mstr.TABLE_ID= btd.db_table_id and btd.buss_table_id=bt.buss_table_id and  btd.buss_table_id='&' "
            + " ORDER BY btd.db_table_id"}, {"getBusinessGroupFactsList1", "select distinct buss_table_id, IFNULL(buss_table_name,buss_table_name) buss_table_name,IFNULL(db_table_id,'0') as db_table_id,IFNULL(table_tooltip_name,buss_table_name) buss_table_tooltip_name from  prg_grp_buss_table where buss_table_id in (select buss_table_id from prg_grp_buss_table where grp_id=&  and buss_type!='Query' )and buss_table_id not in ( SELECT DISTINCT TAB_ID FROM PRG_GRP_DIM_TABLES gdt,PRG_GRP_DIMENSIONS gd where gdt.dim_id= gd.dim_id and gd.grp_id=&) and buss_table_name!='Calculated Facts' "}, {"getBusinessGroupFactsList", "select distinct buss_table_id, ISNULL(buss_table_name,buss_table_name) buss_table_name,ISNULL(db_table_id,'0') as db_table_id,ISNULL(table_tooltip_name,buss_table_name) buss_table_tooltip_name from  prg_grp_buss_table where buss_table_id in (select buss_table_id from prg_grp_buss_table where grp_id=&  and buss_type!='Query' EXCEPT SELECT DISTINCT TAB_ID FROM PRG_GRP_DIM_TABLES gdt,PRG_GRP_DIMENSIONS gd where gdt.dim_id= gd.dim_id and gd.grp_id=&) and buss_table_name!='Calculated Facts' "} // ,{"getBusinessGroupFactsColList", "SELECT BUSS_COLUMN_ID, COLUMN_NAME, DB_COLUMN_ID,column_type FROM PRG_GRP_BUSS_TABLE_DETAILS where buss_table_id=&"}
        , {"getBusinessGroupFactsColList", "SELECT BUSS_COLUMN_ID, COLUMN_NAME, DB_COLUMN_ID,column_type,role_flag,display_formula FROM PRG_GRP_BUSS_TABLE_DETAILS where buss_table_id=&"}, {"getALlLayerTableNames", "SELECT  buss_table_name, source_table_name, db_table_name FROM PRG_GRP_BUSS_TABLE_MASTER_VIEW WHERE buss_table_id=&"}, {"getALlLayerColNames", " SELECT distinct business_col_name, scr_column_name, table_col_name,business_disp_name FROM PRG_GRP_BUSS_TABLE_DTLS_VIEW WHERE buss_table_id=& and upper(column_type) not in ('TIMECALCULATED','SUMMARIZED','CALCULATED','TIMECALUCULATED')"}, {"getRelatedBussTables", "SELECT BUSS_TABLE_ID, BUSS_TABLE_NAME FROM PRG_GRP_BUSS_TABLE where buss_table_id in (select BUSS_TABLE_ID from PRG_GRP_BUSS_TABLE_RLT_MASTER  where BUSS_TABLE_ID2 in (&) union all select BUSS_TABLE_ID2 from PRG_GRP_BUSS_TABLE_RLT_MASTER where BUSS_TABLE_ID in (&) ) and grp_id=&  order by BUSS_TABLE_ID"}, {"getBucketList", "SELECT bucket_id, bucket_name, bucket_desc FROM PRG_GRP_BUCKET_MASTER WHERE grp_id='&' order by bucket_id "}, {"getBucketColList", "SELECT BUCKET_COL_ID, BUCKET_DISP_VALUE, BUCKET_DISP_CUST_VALUE, ORDER_SEQ, START_LIMIT, END_LIMIT FROM PRG_GRP_BUCKET_DETAILS "
            + "where bucket_id=&"}, {"getBussTableConnDetails", "select puc.user_name,puc.password,puc.server,puc.service_id,puc.service_name,puc.port,puc.dsn_name,puc.db_connection_type,puc.dbname FROM PRG_USER_CONNECTIONS puc,"
            + " prg_db_master_table pdmt, prg_grp_buss_table pgbt, prg_grp_buss_table_src pgbts where puc.connection_id= pdmt.connection_id and "
            + "pgbt.buss_table_id= pgbts.buss_table_id and pgbts.db_table_id=pdmt.table_id and pgbt.buss_table_id='&'"}, {"addTabsToSrc", " SELECT p.table_name, p.table_id, d.column_id, d.table_col_name   FROM prg_db_master_table p,  prg_db_master_table q,PRG_DB_MASTER_TABLE_DETAILS d  WHERE q.connection_id= p.connection_id and d.table_id= p.table_id AND q.table_id=& AND p.table_id NOT    IN  ( SELECT DB_TABLE_ID FROM prg_grp_buss_table WHERE grp_id=& and DB_TABLE_ID!=& ) order by decode(p.table_id,&,1),p.table_id "} //for adding to time dimension
        , {"addTimeDimBussMater", "INSERT INTO PRG_GRP_BUSS_TABLE( BUSS_TABLE_NAME, BUSS_DESC, BUSS_TYPE, NO_OF_NODES, IS_PURE_QUERY,DB_QUERY,"
            + "GRP_ID,REF_TABLE_NAME)values('&','&','&',&,'Y','&',&,'&') "}, {"addTimeDimSrc", "INSERT INTO  PRG_GRP_BUSS_TABLE_SRC(BUSS_TABLE_ID, SOURCE_TYPE,CONNECTION_ID,DB_QUERY) "
            + "values((select Last_Insert_id(BUSS_TABLE_ID) from PRG_GRP_BUSS_TABLE order by 1 desc limit 1),'&',&,'&')"}, {"addTimeDimSrcDetails", "INSERT INTO PRG_GRP_BUSS_TABLE_SRC_DETAILS( BUSS_SRC_ID, BUSS_TABLE_ID, "
            + "COLUMN_ALIAS, COLUMN_TYPE) values(&,"
            + "(select Last_Insert_Id(BUSS_TABLE_ID) from PRG_GRP_BUSS_TABLE order by 1 desc limit 1),'&','&')"}, {"addTimeDimBussDetails", "INSERT INTO PRG_GRP_BUSS_TABLE_DETAILS(BUSS_TABLE_ID, COLUMN_NAME ,"
            + "BUSS_SRC_TABLE_DTL_ID,COLUMN_TYPE, COLUMN_DISP_NAME)values((select Last_Insert_Id(BUSS_TABLE_ID) from PRG_GRP_BUSS_TABLE order by 1 desc limit 1),"
            + "'&',&,'&','&')"}, {"addTimeDimDimension", "INSERT INTO PRG_GRP_DIMENSIONS(DIM_NAME, DIM_DESC, DIM_ACTIVE,GRP_ID)values('&','&','&',&)"}, {"addTimeDimTables", "INSERT INTO PRG_GRP_DIM_TABLES( DIM_ID, TAB_ID)values((select Last_Insert_id(DIM_ID) from PRG_GRP_DIMENSIONS order by 1 desc limit 1),&)"}, {"addTimeDimTableDetails", "INSERT INTO PRG_GRP_DIM_TAB_DETAILS(  DIM_TAB_ID, COL_ID, IS_AVAILABLE, IS_PK_KEY )"
            + "values((select Last_Insert_Id(DIM_TAB_ID) from PRG_GRP_DIM_TABLES order by 1 desc limit 1),&,'&','&')"}, {"addTimeDimMembers", "INSERT INTO  PRG_GRP_DIM_MEMBER( MEMBER_NAME, DIM_ID, DIM_TAB_ID, USE_DENOM_TABLE, DENOM_TAB_ID, DENOM_QUERY, MEMBER_DESC )"
            + "values('&',(select Last_Insert_Id(DIM_ID) from PRG_GRP_DIMENSIONS order by 1 desc limit 1),(select Last_Insert_Id(DIM_TAB_ID) from PRG_GRP_DIM_TABLES order by 1 desc limit 1),'&',(select Last_Insert_Id(BUSS_TABLE_ID) from PRG_GRP_BUSS_TABLE order by 1 desc limit 1),'&','&')"}, {"addTimeDimMemberDetails", "INSERT INTO PRG_GRP_DIM_MEMBER_DETAILS( MEM_ID, COL_ID, COL_TYPE, DEFAULT_VAL) values(&,&,'&','&')"}, {"addTimeDimRlt", "INSERT INTO PRG_GRP_DIM_REL( DIM_ID, DESCRIPTION, IS_DEFAULT, REL_NAME) values((select Last_Insert_Id(DIM_ID) from PRG_GRP_DIMENSIONS order by 1 desc limit 1),'&','&','&') "}, {"addTimeDimRltDetails", "INSERT INTO PRG_GRP_DIM_REL_DETAILS(REL_ID, MEM_ID, REL_LEVEL) select (select Last_Insert_Id(rel_id) from PRG_GRP_DIM_REL order by 1 desc limit 1),&,& "}, {"addTimeDimInfo", "INSERT INTO PRG_TIME_DIM_INFO(BUSINESS_TABLE_ID, MAIN_FACT_ID, MAIN_FACT_COL_ID, MIN_LEVEL,IS_MAIN,IS_AS_OF_DATE_JOIN,BUSINESS_COL_ID)"
            + " values((select Last_Insert_Id(BUSS_TABLE_ID) from PRG_GRP_BUSS_TABLE order by 1 desc limit 1),&,&,&,'&','&',&)"}, {"addTimeDimInfo1", "INSERT INTO PRG_TIME_DIM_INFO(BUSINESS_TABLE_ID, MAIN_FACT_ID, MAIN_FACT_COL_ID, MIN_LEVEL,IS_MAIN,IS_AS_OF_DATE_JOIN,BUSINESS_COL_ID)"
            + " values(&,&,&,&,'&','&',&)"}//for time Dimention 2nd time
        , {"addTimeDimBussRltDetails", "INSERT INTO PRG_TIME_DIM_INFO_RLT_DETAILS(BUSS_RELATIONSHIP_ID, P_BUSS_TABLE_ID, P_BUSS_COL_ID1, S_BUSS_TABLE_ID, S_BUSS_COL_ID1, JOIN_TYPE, JOIN_OPERATOR, ACTUAL_CLAUSE,S_COL1_NAME,S_COL1_FORMAT) "
            + "values((select Last_Insert_Id(time_dim_id) from PRG_TIME_DIM_INFO order by 1 desc limit 1),&,&,(select Last_Insert_Id(BUSS_TABLE_ID) from PRG_GRP_BUSS_TABLE order by 1 desc limit 1),&,'&','&','&','&','&')"}, {"addTimeDimBussRltDetails1", "INSERT INTO PRG_TIME_DIM_INFO_RLT_DETAILS(BUSS_RELATIONSHIP_ID, P_BUSS_TABLE_ID, P_BUSS_COL_ID1, S_BUSS_TABLE_ID, S_BUSS_COL_ID1, JOIN_TYPE, JOIN_OPERATOR, ACTUAL_CLAUSE,S_COL1_NAME) "
            + "values(&,&,&,&,&,'&','&','&','&')"}//for time Dimention 2nd time
        , {"getBusinessTableConnectionId", "select puc.connection_id,puc.user_name,puc.dbname FROM PRG_USER_CONNECTIONS puc, prg_db_master_table pdmt, prg_grp_buss_table pgbt, prg_grp_buss_table_src pgbts where puc.connection_id= pdmt.connection_id and pgbt.buss_table_id= pgbts.buss_table_id and pgbts.db_table_id=pdmt.table_id and pgbt.buss_table_id=&"} //add these for bucket
        , {"addBucketSrc", "INSERT INTO  PRG_GRP_BUSS_TABLE_SRC( BUSS_TABLE_ID, DB_TABLE_ID, SOURCE_TYPE ,DB_TABLE_NAME,CONNECTION_ID,DB_QUERY) "
            + "values((select Last_Insert_Id(BUSS_TABLE_ID) from PRG_GRP_BUSS_TABLE order by 1 desc limit 1),(select Last_Insert_Id(TABLE_ID) from prg_db_master_table order by 1 desc limit 1),'&','&',&,'&')"}, {"addBucketsrcDetails", "INSERT INTO PRG_GRP_BUSS_TABLE_SRC_DETAILS( BUSS_SRC_ID, DB_TABLE_ID, BUSS_TABLE_ID, "
            + "COLUMN_ALIAS, COLUMN_TYPE,DB_COLUMN_ID) values((select Last_Insert_Id(BUSS_SOURCE_ID) from PRG_GRP_BUSS_TABLE_SRC order by 1 desc limit 1),(select Last_Insert_Id(TABLE_ID) from prg_db_master_table order by 1 desc limit 1),"
            + "(SELECT LAST_INSERT_ID(BUSS_TABLE_ID) FROM PRG_GRP_BUSS_TABLE ORDER BY 1 DESC LIMIT 1),'&','&',&)"}, {"addBucketBussDetails", "INSERT INTO PRG_GRP_BUSS_TABLE_DETAILS( BUSS_TABLE_ID, COLUMN_NAME, DB_TABLE_ID, "
            + "BUSS_SRC_TABLE_DTL_ID,COLUMN_TYPE, BUCKET_ATTACHED, COLUMN_DISP_NAME,DB_COLUMN_ID)values((select Last_Insert_Id(BUSS_TABLE_ID) from PRG_GRP_BUSS_TABLE order by 1 desc limit 1),"
            + "'&',(select Last_Insert_Id(TABLE_ID) from prg_db_master_table order by 1 desc limit 1),&,'&',&,'&',&)"}, {"addBucketdbMaster", "insert into prg_db_master_table(user_schema,connection_id,table_name,table_alias,table_type,db_query) "
            + "values('&',&,'&','&','Query','&')"}, {"addBucketdbDetails", "insert into prg_db_master_table_details(table_id,table_col_name,table_col_cust_name,col_type,col_length,IS_PRIMARY_KEY) "
            + "values(( select Last_Insert_iD(TABLE_ID) FROM prg_db_master_table ORDER BY 1 DESC LIMIT 1),'&','&','&',&,'&')"}, {"addBucketBussRltMaster", "INSERT INTO PRG_GRP_BUSS_TABLE_RLT_MASTER (BUSS_TABLE_ID, BUSS_TABLE_ID2, REALTIONSHIP_TYPE, ACTUAL_CLAUSE, CLAUSE_TYPE ) values(&,(select Last_Insert_Id(BUSS_TABLE_ID) from PRG_GRP_BUSS_TABLE order by 1 desc limit 1),&,'&','&')"}, {"addBucketBussRltMasterDtl", "INSERT INTO PRG_GRP_BUSS_TABLE_RLT_DETAILS(BUSS_RELATIONSHIP_ID, P_BUSS_TABLE_ID, P_BUSS_COL_ID1, S_BUSS_TABLE_ID, S_BUSS_COL_ID1, JOIN_TYPE, JOIN_OPERATOR, ACTUAL_CLAUSE,S_BUSS_COL_ID2) values((select Last_Insert_Id(BUSS_RELATION_ID) from PRG_GRP_BUSS_TABLE_RLT_MASTER order by 1 desc limit 1),&,&,(select Last_Insert_Id(BUSS_TABLE_ID) from PRG_GRP_BUSS_TABLE order by 1 desc limit 1),&,'&','&','&',&)"}, {"getBucketConnection", "SELECT distinct dt.connection_id,c.user_name FROM PRG_GRP_BUSS_TABLE b, prg_db_master_table dt, prg_user_connections c where b.grp_id=& and b.buss_type='Query' and b.is_pure_query='Y' and c.connection_id= dt.connection_id and b.db_table_id= dt.table_id"}, {"getConnectionIdDetails", "select user_name,password,server,service_id,service_name,port,dsn_name,db_connection_type,dbname FROM PRG_USER_CONNECTIONS where connection_id=&"}, {"bucketTablesDbCheck", "select tname from tab where tname in ('PRG_GRP_BUCKET_MASTER','PRG_GRP_BUCKET_DETAILS')"} // ,{"addBucketBussMater","INSERT INTO PRG_GRP_BUSS_TABLE(BUSS_TABLE_ID, BUSS_TABLE_NAME, BUSS_DESC, BUSS_TYPE, NO_OF_NODES, IS_PURE_QUERY,DB_TABLE_ID," +
        //         "GRP_ID,DB_QUERY)values(PRG_GRP_BUSS_TABLE_SEQ.nextval,'&','&','&',&,'Y',prg_database_master_seq.currval,&,'&') "}
        , {"addBucketBussMater", "INSERT INTO PRG_GRP_BUSS_TABLE( BUSS_TABLE_NAME, BUSS_DESC, BUSS_TYPE, NO_OF_NODES, IS_PURE_QUERY,DB_TABLE_ID,"
            + "GRP_ID,DB_QUERY)values('&','&','&',&,'Y',(SELECT Last_Insert_Id(table_id) from prg_db_master_table order by 1 desc limit 1),&,'&') "} //added newly from sk code
        , {"getDBTableId", "select bsrc.DB_TABLE_ID FROM prg_grp_buss_table bt,PRG_GRP_BUSS_TABLE_SRC bsrc where bt.buss_table_id=bsrc.buss_table_id and bt.buss_table_id='&'"}, {"updateBussTabs", "update PRG_GRP_BUSS_TABLE set NO_OF_NODES=(NO_OF_NODES+'&'),DB_TABLE_ID='' where BUSS_TABLE_ID='&'"}, {"insertSubBussSrcTable", "INSERT INTO PRG_GRP_BUSS_TABLE_SRC  (BUSS_SOURCE_ID,BUSS_TABLE_ID ,CONNECTION_ID ,DB_TABLE_ID,SOURCE_TYPE) "
            + " SELECT (PRG_GRP_BUSS_TABLE_SRC_seq.nextval),'&',connection_id,table_id ,table_type  FROM prg_db_master_table "
            + "WHERE table_id='&'"}, {"insertSubBussSrcTableDetails", "insert into PRG_GRP_BUSS_TABLE_SRC_DETAILS (BUSS_SRC_TABLE_DTL_ID,BUSS_SRC_ID,DB_TABLE_ID, BUSS_TABLE_ID,DB_COLUMN_ID,"
            + "COLUMN_ALIAS,COLUMN_TYPE) "
            + "  SELECT (PRG_GRP_BUSS_TAB_SRC_DTLS_SEQ.NEXTVAL),(PRG_GRP_BUSS_TABLE_SRC_seq.currval),table_id,'&',column_id, "
            + "table_col_name , "
            + "col_type FROM prg_db_master_table_details WHERE table_id='&' "}, {"insertSrcBussTableDetails", "insert into PRG_GRP_BUSS_TABLE_DETAILS (BUSS_COLUMN_ID, BUSS_TABLE_ID, COLUMN_NAME, DB_TABLE_ID, DB_COLUMN_ID, "
            + "BUSS_SRC_TABLE_DTL_ID,"
            + " IS_UNION, COLUMN_TYPE, ACTUAL_COL_FORMULA,ACTUAL_COL_FORMULA1, DEFAULT_AGGREGATION, BUCKET_ATTACHED, COLUMN_DISP_NAME) "
            + " select (PRG_GRP_BUSS_TABLE_DETAILS_seq.nextval),BUSS_TABLE_ID,COLUMN_ALIAS,  DB_TABLE_ID,  DB_COLUMN_ID,  BUSS_SRC_TABLE_DTL_ID,'N',COLUMN_TYPE,"
            + "  '',  '',  '', '',  COLUMN_ALIAS FROM PRG_GRP_BUSS_TABLE_SRC_DETAILS  WHERE BUSS_TABLE_ID='&' and DB_TABLE_ID='&'"}, {"getBussSrcRelId", "select  BUSS_RELATIONSHIP_ID from PRG_GRP_BUSS_TBL_SRC_RLT_MSTR where TABLE_ID in ('&','&') and TABLE_ID2 in ('&','&') and BUSS_TABLE_ID in ('&')"}, {"insertRelMstrSrcQry", "insert into PRG_GRP_BUSS_TBL_SRC_RLT_MSTR (BUSS_RELATIONSHIP_ID, TABLE_ID, TABLE_ID2, REALTIONSHIP_TYPE, ACTUAL_CLAUSE, CLAUSE_TYPE,BUSS_TABLE_ID)"
            + " values(PRG_BUSS_TBL_SRC_RLT_MSTR_SEQ.nextval,'&','&','&','&','&','&')"}, {"insertRelDtlSrcQry", "insert into PRG_GRP_BUSS_TABLE_SRC_RLT (BUSS_RELATIONSHIP_DTL_ID, "
            + "TABLE_ID, COLUMN_ID, TABLE_ID2, COLUMN_ID2,COLUMN_ID3, JOIN_TYPE, "
            + " JOIN_OPERATOR, ACTUAL_CLAUSE,BUSS_RELATIONSHIP_ID) values(PRG_GRP_BUSS_TABLE_SRC_RLT_SEQ.nextval,"
            + "'&','&','&','&','&','&','&','&','&')"}, {"updatePrevTblrel", "update prg_grp_buss_table_src_rlt set join_type='&', join_operator='&' , actual_clause='&' where column_id in('&','&') and column_id2 in('&','&')"
            + " and BUSS_RELATIONSHIP_ID in (select BUSS_RELATIONSHIP_ID  from PRG_GRP_BUSS_TBL_SRC_RLT_MSTR where table_id in ('&','&') and table_id2('&','&') )"}, {"grpTableIds", " SELECT table_id FROM prg_db_master_table WHERE table_id NOT IN "
            + "(SELECT distinct btd.DB_TABLE_ID FROM prg_grp_buss_table_details btd,"
            + "prg_grp_buss_table bt WHERE "
            + "btd.BUSS_TABLE_ID=bt.BUSS_TABLE_ID AND bt.GRP_ID='&')"}, {"updateBussTblRltMstr", " update PRG_GRP_BUSS_TBL_SRC_RLT_MSTR set ACTUAL_CLAUSE=ACTUAL_CLAUSE || '&' where TABLE_ID in ('&','&') and TABLE_ID2 in ('&','&') and BUSS_TABLE_ID in ('&')"}, {"getTimeDimColIds", " SELECT BUSS_COLUMN_ID, BUSS_TABLE_ID, COLUMN_NAME FROM PRG_GRP_BUSS_TABLE_DETAILS where BUSS_TABLE_ID=& and COLUMN_NAME in(&)"}, {"deleteAlreadyExistedTimeDimColDetails", "delete from PRG_TIME_DIM_INFO_RLT_DETAILS where BUSS_RELATIONSHIP_ID in('&')"} //to add bussTable paths
        //,{"addPathMaps","INSERT INTO PRG_GRP_BUSS_TABLE_MAPS(BUSS_FACT_ID, BUSS_TABLE_ID, BUSS_TABLE_PATH) VALUES(&,&,'&')"}
        , {"addPathMaps", "INSERT INTO PRG_GRP_BUSS_TABLE_MAPS(BUSS_FACT_ID, BUSS_TABLE_ID, BUSS_TABLE_PATH,BUSS_TABLE_MAP_ID) VALUES(&,&,'&',PRG_QRY_TABLE_LEVSEQ.nextval)"}, {"addUserAssignment", "INSERT INTO PRG_GRP_USER_ASSIGNMENT( USER_ID, GRP_ID, START_DATE, END_DATE, IS_ACTIVE,USER_NAME) values (&,&,&,'&','&','&')"}, {"deleteUserAssignment", " delete from PRG_GRP_USER_ASSIGNMENT where GRP_ID=&"} //start by uday
        , {"getRelatedTablesList", "select distinct buss_table_id,buss_table_name from prg_grp_buss_table where buss_table_id in "
            + "(select distinct buss_table_id2 from prg_grp_buss_table_rlt_master where buss_table_id=& UNION ALL "
            + "select distinct buss_table_id from prg_grp_buss_table_rlt_master where buss_table_id2=& UNION ALL "
            + "select buss_table_id from prg_grp_buss_table where buss_table_id=&) and grp_id=(select grp_id from prg_grp_buss_table where buss_table_id=&) order by buss_table_id"},
        {"getRelatedTableColumns", "select distinct buss_column_id, column_name from prg_grp_buss_table_details where buss_table_id=&"},
        /*
         * {"getRelationIds","select distinct buss_relation_id from
         * prg_grp_buss_table_rlt_master where buss_table_id in " + "(select
         * distinct buss_table_id from prg_grp_buss_table_rlt_master where
         * buss_table_id2=&) " + "union ALL select distinct buss_relation_id
         * from prg_grp_buss_table_rlt_master where buss_table_id2 in " +
         * "(select distinct buss_table_id2 FROM prg_grp_buss_table_rlt_master
         * where buss_table_id=&) order by buss_relation_id"}
         */
        {"getTableRelationDetails", "select distinct d.buss_relationship_id,d.buss_relationship_detail_id, d.p_buss_table_id, m.buss_table_name P_TABLE_NAME,"
            + "d.p_buss_col_id1,d.s_buss_table_id,n.buss_table_name S_TABLE_NAME,d.s_buss_col_id1,d.join_type,d.join_operator "
            + "from prg_grp_buss_table_rlt_details d,prg_grp_buss_table m,prg_grp_buss_table n where buss_relationship_id in "
            + "(select distinct buss_relation_id from prg_grp_buss_table_rlt_master where buss_table_id in "
            + "(select distinct buss_table_id from prg_grp_buss_table_rlt_master where buss_table_id2=&) "
            + "union ALL select distinct buss_relation_id from prg_grp_buss_table_rlt_master where buss_table_id2 in "
            + "(select distinct buss_table_id2 FROM prg_grp_buss_table_rlt_master where buss_table_id=&) "
            + ") and d.p_buss_table_id=m.buss_table_id and d.s_buss_table_id=n.buss_table_id  and m.grp_id=& order by 1"} //susheela 04-12-09 start
        , {"updateBussTableRltMaster", "update prg_grp_buss_table_rlt_master set realtionship_type=&, actual_clause='&', clause_type='&' where buss_relation_id=&"}, {"updateBussTableRltDetails", "update prg_grp_buss_table_rlt_details set join_operator='&', actual_clause='&',join_type='&' where buss_relationship_id=& "
            + "and buss_relationship_detail_id=&"} //end by uday
        , {"addUserFolderAssignment", "INSERT  INTO PRG_GRP_USER_FOLDER_ASSIGNMENT(USER_ASSI_ID, USER_FOLDER_ID, USER_ID, GRP_ID, START_DATE, END_DATE) values(&,&,&,&,&,'&')"}, {"deleteUserFolderAssignment", " delete from PRG_GRP_USER_FOLDER_ASSIGNMENT where USER_ID in (&) and GRP_ID=&"} //new enhancement Qd Queries
        , {"getBusinessRolesList", "SELECT FOLDER_ID, FOLDER_NAME, GRP_ID FROM PRG_USER_FOLDER where grp_Id in(&)"}, {"getBusinessGroupList", "select grp_id, grp_name  from prg_grp_master where connection_id in (&)"} // added by susheela
        , {"AddParentChildLevels", "insert into prg_buss_table_level(buss_details_id,child_buss_table_id,parent_buss_table_id,table_level) values(PRG_QRY_TABLE_LEVSEQ.nextval,'&','&','&')"}, {"getParentTables", "select parent_buss_table_id,child_buss_table_id, table_level from prg_buss_table_level where child_buss_table_id in(&) order by parent_buss_table_id,child_buss_table_id"} //added by susheela start
        , {"getSavedTargetMeasure", "select buss_original_table_id, pgbt.buss_table_name from prg_target_master ptm,prg_grp_buss_table pgbt where business_group=& and pgbt.buss_table_id= ptm.buss_original_table_id"}, {"getSavedMeasure", "select DISTINCT ptm.buss_original_table_id,col_id, col_name, pgbt.buss_table_name from target_master_col_details tmcd,prg_grp_buss_table pgbt ,prg_target_master ptm where "
            + " prg_measure_id_details in(select prg_measure_id_details from prg_target_master_details where"
            + " prg_target_master_details.prg_measure_id in(select prg_measure_id from prg_target_master where "
            + "prg_target_master.business_group=&)) and pgbt.buss_table_id=ptm.buss_original_table_id and tmcd.selected_measure='Y'"} //added by susheela over
        // added by praveen start
        , {"insrtbkttabdetails", "insert into PRG_GRP_DIM_TAB_DETAILS(DIM_TAB_ID,COL_ID,IS_AVAILABLE,IS_PK_KEY) VALUES((SELECT lAST_iNSERT_iD(DIM_TAB_ID) FROM prg_grp_dim_tables ORDER BY 1 DESC LIMIT 1),'&','Y','N')"}, {"insrtbktdimtables", "insert into prg_grp_dim_tables(DIM_ID,TAB_ID,DB_DIM_ID,DB_TAB_ID) VALUES((select Last_Insert_Id(DIM_ID) from PRG_GRP_DIMENSIONS order by 1 desc limit 1),(select Last_Insert_Id(BUSS_TABLE_ID) from PRG_GRP_BUSS_TABLE order by 1 desc limit 1),null,null)"}, {"insertbktgrpdims", "insert into PRG_GRP_DIMENSIONS(DIM_NAME,DIM_DESC,DIM_ACTIVE,DEFAULT_HIERARCHY_ID,GRP_ID,QRY_DIM_ID) VALUES('&','&',null,null,'&',null)"}, {"insrtdimrel", "insert into prg_grp_dim_rel(DIM_ID,DESCRIPTION,IS_DEFAULT,REL_NAME) values((select Last_Insert_Id(DIM_ID) from PRG_GRP_DIMENSIONS order by 1 desc limit 1),'&','Y','&')"}, {"insrtreldetails", "insert into PRG_GRP_DIM_REL_DETAILS(REL_ID,MEM_ID,REL_LEVEL) values((select Last_Insert_Id(rel_id) from prg_grp_dim_rel order by 1 desc limit 1),(select Last_Insert_Id(member_id) from prg_grp_dim_member order by 1 desc limit 1),'&')"}, {"insrtprgdimmember", "insert into prg_grp_dim_member(MEMBER_NAME,DIM_ID,DIM_TAB_ID,USE_DENOM_TABLE,DENOM_QUERY,MEMBER_DESC,MEMBER_ORDER_BY_NAME )VALUES('&',(select Last_Insert_Id(DIM_ID) from PRG_GRP_DIMENSIONS order by 1 desc limit 1),(select Last_Insert_Id(DIM_TAB_ID) from prg_grp_dim_tables order by 1 desc limit 1),'N',null,'&','&')"}, {"inserdimmemdetails1", "insert into prg_grp_dim_member_details(MEM_ID,COL_ID,COL_TYPE,DEFAULT_VAL) VALUES((select Last_Insert_Id(member_id) from prg_grp_dim_member order by 1 desc limit 1),&,'KEY',null)"}, {"inserdimmemdetails2", "insert into prg_grp_dim_member_details(MEM_ID,COL_ID,COL_TYPE,DEFAULT_VAL) VALUES((select Last_Insert_Id(member_id) from prg_grp_dim_member order by 1 desc limit 1),&,'VALUE',null)"}, {"inserdimmemdetails3", "insert into prg_grp_dim_member_details(MEM_ID,COL_ID,COL_TYPE,DEFAULT_VAL) VALUES((select Last_Insert_Id(member_id) from prg_grp_dim_member order by 1 desc limit 1),(select Last_Insert_Id(BUSS_COLUMN_ID) from PRG_GRP_BUSS_TABLE_DETAILS order by 1 desc limit 1),'KEY',null)"}, {"inserdimmemdetails4", "insert into prg_grp_dim_member_details(MEM_ID,COL_ID,COL_TYPE,DEFAULT_VAL) VALUES((select Last_Insert_Id(member_id) from prg_grp_dim_member order by 1 desc limit 1),(select Last_Insert_Id(BUSS_COLUMN_ID) from PRG_GRP_BUSS_TABLE_DETAILS order by 1 desc limit 1),'VALUE',null)"} // added by praveen over
        //for formula at bus grp
        , {"addFormulaBussMater", "INSERT INTO PRG_GRP_BUSS_TABLE(BUSS_TABLE_NAME, BUSS_DESC, BUSS_TYPE, NO_OF_NODES, IS_PURE_QUERY,DB_QUERY,"
            + "GRP_ID)values('&','&','&',&,'&','&',&) "}, {"addFormulaSrc", "INSERT INTO  PRG_GRP_BUSS_TABLE_SRC(BUSS_SOURCE_ID, BUSS_TABLE_ID, DB_TABLE_ID, SOURCE_TYPE ,DB_TABLE_NAME,CONNECTION_ID,DB_QUERY) "
            + "values(&,&,&,'&','&',&,'&')"}, {"addFormulasrcDetails", "INSERT INTO PRG_GRP_BUSS_TABLE_SRC_DETAILS( BUSS_SRC_ID, DB_TABLE_ID, BUSS_TABLE_ID, "
            + "COLUMN_ALIAS, COLUMN_TYPE,DB_COLUMN_ID) values(&,&,"
            + "&,'&','&',&)"}, {"addFormulaBussDetails", "INSERT INTO PRG_GRP_BUSS_TABLE_DETAILS(BUSS_TABLE_ID, COLUMN_NAME, DB_TABLE_ID, "
            + "BUSS_SRC_TABLE_DTL_ID,COLUMN_TYPE, BUCKET_ATTACHED, COLUMN_DISP_NAME,DB_COLUMN_ID,is_union,actual_col_formula,DEFAULT_AGGREGATION,COLUMN_DISPLAY_DESC,REFFERED_ELEMENTS,ROLE_FLAG,DISPLAY_FORMULA)values(&,"
            + "'&',&,&,'&',&,'&',&,'&','&','&','&','&','&','&')"}, {"addParamFormulaBussDetails", "INSERT INTO PRG_GRP_BUSS_TABLE_DETAILS(BUSS_COLUMN_ID, BUSS_TABLE_ID, COLUMN_NAME, DB_TABLE_ID, "
            + "BUSS_SRC_TABLE_DTL_ID,COLUMN_TYPE, BUCKET_ATTACHED, COLUMN_DISP_NAME,DB_COLUMN_ID,is_union,actual_col_formula,DEFAULT_AGGREGATION,COLUMN_DISPLAY_DESC,REFFERED_ELEMENTS,ROLE_FLAG)values(&,&,"
            + "'&',&,&,'&',&,'&',&,'&','&','&','&','&','&')"} //susheela start 07-12-09
        , {"getGroupDimDeleteStatus", "select * from prg_user_all_adim_master where info_folder_id in (select folder_id from prg_user_folder where grp_id in(&)) and info_dim_id=&"}, {"getRelatedReportsForGroup", "select * from prg_ar_report_details where folder_id in(select folder_id from prg_user_folder where grp_id=&)"}, {"getBussSrcForDimTable", "select * from prg_grp_buss_table_src s,prg_grp_buss_table b where s.buss_table_id in(select tab_id from prg_grp_dim_tables where dim_id=&) and b.grp_id=& and b.buss_table_id= s.buss_table_id"}, {"getDimBussTabMasterRelation1", "select * from prg_grp_buss_table_rlt_master m,prg_grp_buss_table b where m.buss_table_id "
            + " in(select tab_id from prg_grp_dim_tables where dim_id=&)  and b.grp_id=& and b.buss_table_id= m.buss_table_id"}, {"getDimBussTabMasterRelation2", "select * from prg_grp_buss_table_rlt_master m,prg_grp_buss_table b where m.buss_table_id2 "
            + " in(select tab_id from prg_grp_dim_tables where dim_id=&)  and b.grp_id=& and b.buss_table_id= m.buss_table_id2"}, {"deleteDimTabSrcMaster", "delete from prg_grp_buss_table_src  where buss_source_id in(&)"}, {"deleteDimTabSrcDetails", "delete from prg_grp_buss_table_src_details  where buss_src_id in(&)"}, {"deleteGrpBussTabDetails", "delete from  prg_grp_buss_table_details where buss_table_id in(&)"}, {"deleteGrpDimBussTable", "delete from prg_grp_buss_table where buss_table_id in(select tab_id from prg_grp_dim_tables where dim_id=&) and grp_id=&"}, {"deleteGrpDimMemberDetails", "delete from prg_grp_dim_member_details where mem_id in(select member_id from  prg_grp_dim_member where dim_id=&)"}, {"deleteGrpDimMember", "delete from prg_grp_dim_member where dim_id=&"}, {"deleteGrpDimTabDetails", "delete from prg_grp_dim_tab_details where dim_tab_id in(select dim_tab_id from prg_grp_dim_tables where dim_id=&)"}, {"deleteGrpDimTable", "delete from prg_grp_dim_tables where dim_id=&"}, {"deleteGrpDimension", "delete from prg_grp_dimensions where dim_id=& and grp_id=&"}, {"deleteCustomRoleDrillForDim", "delete from prg_grp_role_custom_drill where sub_folder_id in( select sub_folder_id from prg_user_folder_detail "
            + " where folder_id in(select folder_id from prg_user_folder where grp_id=&)) and member_id in(select distinct  "
            + "member_id from prg_user_sub_folder_tables where dim_id=&) and sub_folder_id in(select sub_folder_id from"
            + " prg_user_folder_detail where folder_id in(select folder_id from prg_user_folder where grp_id=&))"}, {"getUpdatableDrill", "select * from prg_grp_role_custom_drill where sub_folder_id in( select sub_folder_id from prg_user_folder_detail"
            + " where folder_id in(select folder_id from prg_user_folder where grp_id=&)) and child_member_id in(select distinct "
            + " info_member_id from prg_user_all_adim_details where info_dim_id=& and  info_folder_id in(select folder_id from prg_user_folder"
            + " where grp_id=&))"}, {"getDeletedDimsMemberForDrill", "select distinct info_member_id from prg_user_all_adim_details where info_dim_id=& and info_folder_id in(select folder_id from prg_user_folder where grp_id=&)"}, {"deleteAdimKeyValEle", "delete  from prg_user_all_adim_key_val_ele where key_element_id in (select info_element_id from prg_user_all_adim_details where info_dim_id=& and "
            + " info_folder_id in(select folder_id from prg_user_folder where grp_id=&))"}, {"deleteUserAllAdimDetails", "delete from prg_user_all_adim_details where info_dim_id=& and info_folder_id in(select folder_id from prg_user_folder where grp_id=&)"}, {"deleteUserAllAdimMaster", "delete from prg_user_all_adim_master where info_dim_id=& and info_folder_id in(select folder_id from prg_user_folder where grp_id=&)"}, {"deleteDDimKeyValEle", "delete from prg_user_all_ddim_key_val_ele where key_dim_id=& and key_folder_id in(select folder_id from prg_user_folder where grp_id=&)"}, {"deleteDDimDetails", "delete from prg_user_all_ddim_details where info_dim_id=& and  info_folder_id in(select folder_id from prg_user_folder where grp_id=&)"}, {"deleteDdimMaster", "delete from prg_user_all_ddim_master where info_dim_id=& and info_folder_id in(select folder_id from prg_user_folder where grp_id=&)"}, {"deleteUserAllInfoDetails", "delete from prg_user_all_info_details where buss_table_id in(&) and grp_id=& and folder_id in(select folder_id from prg_user_folder where grp_id=&)"}, {"deleteUserFolderTableDim", "delete  from prg_user_sub_folder_tables where buss_table_id in(&)"}, {"deleteUserFolderTableDimMem", "delete  from prg_user_sub_folder_tables where  dim_id=& and sub_folder_id in( select sub_folder_id from prg_user_folder_detail  where folder_id in(select folder_id from prg_user_folder where grp_id=&))"}, {"deleteUserSubFolderElements", "delete from prg_user_sub_folder_elements where buss_table_id in(&)"} //added by susheela start 11-12-09
        , {"getExtraDimsForRole", "select dim_id from prg_grp_dimensions where grp_id=& and dim_name not in('Time') and dim_id not in (select dim_id from prg_user_sub_folder_tables t,prg_grp_buss_table b where sub_folder_id in(select sub_folder_id from prg_user_folder_detail where folder_id=&) and b.buss_table_id=t.buss_table_id)"}, {"getExtraBussTable", "select buss_table_id from prg_grp_buss_table where grp_id=& and buss_table_id not in(  select "
            + " distinct buss_table_id from prg_user_sub_folder_tables where sub_folder_id "
            + " in(select sub_folder_id from prg_user_folder_detail where folder_id=&)) "} //added by bharathi for formula display
        , {"getBusinessGroupFactsformulaList1", "select distinct buss_table_id, buss_table_name,IFNULL(db_table_id,'0') as db_table_id from  prg_grp_buss_table where buss_table_id in (select buss_table_id from prg_grp_buss_table where grp_id=&  and buss_type!='Query') and buss_table_id not in( SELECT DISTINCT TAB_ID FROM PRG_GRP_DIM_TABLES gdt,PRG_GRP_DIMENSIONS gd where gdt.dim_id= gd.dim_id and gd.grp_id=&) and buss_table_name='Calculated Facts' "}, {"getBusinessGroupFactsformulaList", "select distinct buss_table_id, buss_table_name,ISNULL(db_table_id,'0') as db_table_id from  prg_grp_buss_table where buss_table_id in (select buss_table_id from prg_grp_buss_table where grp_id=&  and buss_type!='Query'  EXCEPT SELECT DISTINCT TAB_ID FROM PRG_GRP_DIM_TABLES gdt,PRG_GRP_DIMENSIONS gd where gdt.dim_id= gd.dim_id and gd.grp_id=&) and buss_table_name='Calculated Facts' "}, {"getBusinessGroupFactsformulaColList", "SELECT BUSS_COLUMN_ID, COLUMN_NAME, DB_COLUMN_ID,column_type,role_flag,REFFERED_ELEMENTS,display_formula FROM PRG_GRP_BUSS_TABLE_DETAILS where buss_table_id=&"} //added by susheela on 28-12-09
        , {"getAddedTargetMeas", " select * from prg_target_master where business_group=&"}, {"getAddedTargets", "select * from target_master where measure_id=&"}, {"getMasterTableIdDetails", "SELECT pdmt.table_id FROM   prg_db_master_table pdmt,  prg_grp_buss_table pgbt,  prg_grp_buss_table_src pgbts WHERE pgbt.buss_table_id = pgbts.buss_table_id AND pgbts.db_table_id  =pdmt.table_id AND pgbt.buss_table_id ='&'"}, {"querybucketDetails", "select BUCKET_ID,BUCKET_NAME,BUSS_COL_ID from PRG_GRP_BUCKET_MASTER where GRP_ID = &"}, {"getBucketChildDetailsQry", "select BUCKET_COL_ID,BUCKET_DISP_VALUE,BUCKET_DISP_CUST_VALUE,ORDER_SEQ,START_LIMIT,END_LIMIT from PRG_GRP_BUCKET_DETAILS where BUCKET_ID=&"}, {"deleteBktDetails", "delete from PRG_GRP_BUCKET_DETAILS where BUCKET_ID =&"}, {"updatebktDetailsQueryS", "INSERT INTO PRG_GRP_BUCKET_DETAILS ( BUCKET_DISP_VALUE, BUCKET_DISP_CUST_VALUE, ORDER_SEQ, START_LIMIT, END_LIMIT, CREATED_BY, CREATED_ON, UPDATED_BY, UPDATED_ON,  BUCKET_ID )values ('&','&',&,&,&,&,&,&,&,&)"}, {"updateBktMaster", "update PRG_GRP_BUCKET_MASTER  set BUCKET_NAME='&' ,BUCKET_DESC ='&' where GRP_ID= & and BUCKET_ID = &"}, {"selectDbTableID", "SELECT PGBTDV.BUSINESS_COL_NAME,PGBTMV.CONNECTION_ID, PGBTMV.DB_TABLE_NAME FROM PRG_GRP_BUSS_TABLE_MASTER_VIEW PGBTMV , PRG_GRP_BUSS_TABLE_DTLS_VIEW PGBTDV "
            + " WHERE PGBTMV.GRP_ID=PGBTDV.GRP_ID AND PGBTMV.DB_TABLE_ID=PGBTDV.DB_TABLE_ID  AND PGBTDV.GRP_ID=& AND PGBTDV.BUSS_COLUMN_ID=&"}, {"selMaxAndMinValues", "select max(&) as COL_MAXMUM,min(&) as COL_MINMUM ,AVG(&) as COL_AVG from &"}, {"getBusinessColumnDetails", "select BUSSTBL.BUSS_TABLE_ID,BUSSTBL.BUSS_TABLE_NAME, BUSSTBLDTLS.BUSS_COLUMN_ID,BUSSTBLDTLS.COLUMN_NAME, BUSSTBLDTLS.COLUMN_TYPE,BUSSTBLDTLS.ACTUAL_COL_FORMULA,BUSSTBLDTLS.DEFAULT_AGGREGATION,BUSSTBLDTLS.COLUMN_DISP_NAME,BUSSTBLDTLS.COLUMN_DISPLAY_DESC,BUSSTBLDTLS.REFFERED_ELEMENTS from PRG_GRP_BUSS_TABLE BUSSTBL, PRG_GRP_BUSS_TABLE_DETAILS BUSSTBLDTLS "
            + "WHERE BUSSTBL.BUSS_TABLE_ID = BUSSTBLDTLS.BUSS_TABLE_ID AND BUSSTBLDTLS.BUSS_COLUMN_ID IN (&)"}, {"getBusinessTableSrcId", "select BUSS_SOURCE_ID from PRG_GRP_BUSS_TABLE_SRC "
            + "WHERE BUSS_TABLE_ID IN (&)"}, {"checkCalculatedFolderExists", "select BUSS_SOURCE_ID, BUSS_TABLE_ID  from prg_grp_buss_table_src  where BUSS_TABLE_ID in (select BUSS_TABLE_ID from prg_grp_buss_table where buss_table_name='Calculated Facts' and grp_id=&)"}, {"getBussGrpDetails", "SELECT BUSS_COLUMN_ID ,COLUMN_NAME FROM PRG_GRP_BUSS_TABLE_DETAILS WHERE BUSS_TABLE_ID =&"}, {"updateBussTableDetails", "update prg_grp_buss_table_details set column_disp_name = 'B_' + CAST((buss_column_id) as varchar(256)) where BUSS_TABLE_ID in (SELECT BUSS_TABLE_ID FROM PRG_GRP_BUSS_TABLE  WHERE GRP_ID = &)"},
        {"getallDetails", "select BD.*,BT.* from PRG_GRP_BUSS_TABLE_DETAILS BD ,PRG_GRP_BUSS_TABLE BT  where BD.BUSS_COLUMN_ID='&' AND BT.BUSS_TABLE_ID=BD.BUSS_TABLE_ID"},
        {"insertQuickFormula", "INSERT INTO prg_grp_buss_table_details (buss_table_id,COLUMN_NAME,DB_TABLE_ID,DB_COLUMN_ID,column_type,actual_col_formula,DEFAULT_AGGREGATION,COLUMN_DISP_NAME,COLUMN_DISPLAY_DESC,ROLE_FLAG,base_column_id) values('&','&','&','&','&','&','&','B_' + CAST((IDENT_CURRENT('PRG_GRP_BUSS_TABLE_DETAILS')) as varchar(256)),'&','&','&')"},
        {"insertQuickFormulaMysql", "INSERT INTO prg_grp_buss_table_details (buss_table_id,COLUMN_NAME,DB_TABLE_ID,DB_COLUMN_ID,column_type,actual_col_formula,DEFAULT_AGGREGATION,COLUMN_DISP_NAME,COLUMN_DISPLAY_DESC,ROLE_FLAG,base_column_id) values('&','&','&','&','&','&','&',concat('B_',CAST((&) as char(256))),'&','&','&')"},
        {"getSeq", "select PRG_GRP_BUSS_TABLE_DETAILS_seq.nextval from dual"},
        {"saveTragetDetails", "CREATE TABLE  & ( TARGET_DURATION   datetime ,TARGET_MEMBERS  VARCHAR2(4000 BYTE),TARGET_VALUE NUMBER )"}, {"saveTragetDetailsinsertquery", "INSERT INTO & ('&','&',&)"}, {"getCustomerTableDetails", "SELECT PB.COLUMN_NAME,PD.*,PT.BUSS_TABLE_NAME FROM PRG_GRP_BUSS_TABLE_DETAILS PB ,PRG_GRP_DIM_MEMBER_DETAILS PD,PRG_GRP_BUSS_TABLE PT WHERE PD.MEM_ID=& AND PD.COL_ID  =PB.BUSS_COLUMN_ID AND PD.COL_TYPE='KEY' AND PT.BUSS_TABLE_ID =PB.BUSS_TABLE_ID AND PT.GRP_ID=&"}, {"getDetails", "SELECT A.BUSS_COLUMN_ID,MAX(A.default_aggregation) default_aggregation ,MAX(AVG_col_formula) AVG_col_formula,MAX(MIN_col_formula) MIN_col_formula ,MAX(MAX_col_formula) MAX_col_formula ,MAX(SUM_col_formula) SUM_col_formula,MAX(CNT_col_formula) CNT_col_formula,MAX(CNTD_col_formula) CNTD_col_formula,c.buss_table_name,c.db_table_id,db_column_id,column_name,column_disp_name,column_type,column_display_desc,role_flag FROM(SELECT (CASE WHEN base_column_id *1 IS NULL THEN BUSS_COLUMN_ID ELSE base_column_id *1 END) BUSS_COLUMN_ID,(CASE WHEN base_column_id *1 IS NULL THEN default_aggregation ELSE NULL END) default_aggregation,(CASE WHEN base_column_id *1 IS NOT NULL AND upper(default_aggregation)='AVG' THEN 'Y' ELSE NULL END) AVG_col_formula ,( CASE WHEN base_column_id *1 IS NOT NULL AND upper(default_aggregation)='MIN' THEN 'Y' ELSE NULL END) MIN_col_formula ,(CASE WHEN base_column_id *1 IS NOT NULL AND upper(default_aggregation)='MAX' THEN 'Y' ELSE NULL END) MAX_col_formula ,(CASE WHEN base_column_id *1 IS NOT NULL AND upper(default_aggregation)='SUM' THEN 'Y' ELSE NULL END) SUM_col_formula ,(CASE WHEN base_column_id *1 IS NOT NULL AND upper(default_aggregation)='COUNT' THEN 'Y' ELSE NULL END) CNT_col_formula ,(CASE WHEN base_column_id *1 IS NOT NULL AND upper(default_aggregation)='COUNTDISTINCT' THEN 'Y' ELSE NULL END) CNTD_col_formula FROM prg_grp_buss_table_details) A,prg_grp_buss_table_details B,prg_grp_buss_table c WHERE A.BUSS_COLUMN_ID= B.BUSS_COLUMN_ID and B.buss_table_id = C.buss_table_id AND B.buss_table_id = '&' AND column_type NOT IN('TIMECALCULATED','TIMECALUCULATED','TIMESUMMARISED','TIMESUMMARIZED')GROUP BY A.BUSS_COLUMN_ID,c.buss_table_name,c.db_table_id,db_column_id,column_name,column_disp_name,column_type,column_display_desc,role_flag ORDER BY column_name "},
        {"getgroupnames", "select distinct flexi_group from prg_flexi_segment_supp where flexi_table = '&'"},
        {"getflexitablenames", "select * from PRG_FLEXI_SEGMENTATION"}, {"getBusinessGroupDimList1", "SELECT   D.DIM_ID,D.DIM_NAME,DD.MEMBER_ID,DD.MEMBER_NAME FROM PRG_GRP_DIMENSIONS D ,PRG_GRP_DIM_MEMBER DD,PRG_GRP_DIM_TABLES DT WHERE D.GRP_ID=& AND DD.DIM_ID =D.DIM_ID AND DT.DIM_ID=DD.DIM_ID AND DT.TAB_ID IN (SELECT DISTINCT BUSS_TABLE_ID2 FROM PRG_GRP_BUSS_TABLE_RLT_MASTER WHERE BUSS_TABLE_ID =& UNION ALL SELECT DISTINCT BUSS_TABLE_ID FROM PRG_GRP_BUSS_TABLE_RLT_MASTER WHERE BUSS_TABLE_ID2 =&)"}
    };
}
