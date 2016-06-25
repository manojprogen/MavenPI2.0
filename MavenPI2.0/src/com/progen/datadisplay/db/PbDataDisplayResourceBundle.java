/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.datadisplay.db;

/**
 * @filename PbDataDisplayResourceBundle
 *
 * @author santhosh.kumar@progenbusiness.com @date Aug 17, 2009, 6:51:38 PM
 */
import java.io.Serializable;
import java.util.ListResourceBundle;

public class PbDataDisplayResourceBundle extends ListResourceBundle implements Serializable {

    protected Object[][] getContents() {
        return contents;
    }
    static final Object contents[][] = {
        {"getConnectionId", "SELECT  PUC.USER_NAME, PUC.PASSWORD, PUC.SERVER, PUC.SERVICE_ID, PUC.SERVICE_NAME, PUC.PORT,PUC.DSN_NAME, PUC.DB_CONNECTION_TYPE,PUC.DBNAME   FROM PRG_USER_CONNECTIONS PUC,prg_db_master_table PDMT WHERE PDMT.connection_id=PUC.connection_id AND pdmt.table_id=&"}, {"getDataDisplayColumns", "select PMT.table_name, PMTD.table_col_name FROM PRG_DB_MASTER_TABLE PMT,PRG_DB_MASTER_TABLE_DETAILS PMTD "
            + "where pmt.table_id= pmtd.table_id  AND pmtd.table_id IN (&) order by decode(pmtd.table_id "}, {"getDataDisplay", " select & from & where rownum<=100"}, {"getDataDisplay1", " select & from & where "}, {"getDataDisplaySQL", " select TOP 200 & from & where"}, {"getRelatedTables", "select distinct table_id, table_name from PRG_DB_MASTER_TABLE where table_id in(select table_id from PRG_DB_TABLE_RLT_MASTER "
            + "where table_id2 in (&) union all select table_id2 from PRG_DB_TABLE_RLT_MASTER where table_id in (&) ) order by table_id"}, {"buildTableConds", "SELECT NVL(ACTUAL_CLAUSE,' ') || NVL(ACTUAL_CLAUSE1,' ')  FROM PRG_DB_TABLE_RLT_MASTER WHERE "} //added by santhosh.kumar@progenbusiness.com on 26/08/09 for inserting into buss relation master and details table
        , {"getDbMasterRelations", "SELECT RELATIONSHIP_ID, TABLE_ID, TABLE_ID2, REALTIONSHIP_TYPE, ACTUAL_CLAUSE, ACTUAL_CLAUSE1, CLAUSE_TYPE "
            + "FROM PRG_DB_TABLE_RLT_MASTER WHERE table_id IN (SELECT DB_TABLE_ID FROM PRG_GRP_BUSS_TABLE_MASTER_VIEW) "
            + "OR table_id2 IN (SELECT DB_TABLE_ID FROM PRG_GRP_BUSS_TABLE_MASTER_VIEW)"}, {"insertBussMasterRelations", "insert into PRG_GRP_BUSS_TABLE_RLT_MASTER (BUSS_RELATION_ID, BUSS_TABLE_ID, BUSS_TABLE_ID2, REALTIONSHIP_TYPE, ACTUAL_CLAUSE, ACTUAL_CLAUSE1,CLAUSE_TYPE)"
            + " values(PRG_GRP_BUSS_TAB_RLT_MSTR_SEQ.NEXTVAL,(select buss_table_id  from PRG_GRP_BUSS_TABLE_MASTER_VIEW where db_table_id=&),"
            + "(select buss_table_id  from PRG_GRP_BUSS_TABLE_MASTER_VIEW where db_table_id=&),&,'&','&','&')"}, {"insertBussDetailsRelations", " INSERT INTO PRG_GRP_BUSS_TABLE_RLT_DETAILS (BUSS_RELATIONSHIP_ID,BUSS_RELATIONSHIP_DETAIL_ID,P_BUSS_TABLE_ID, "
            + "P_BUSS_COL_ID1,P_BUSS_COL_ID2,S_BUSS_TABLE_ID,S_BUSS_COL_ID1,S_BUSS_COL_ID2,JOIN_TYPE,JOIN_OPERATOR,ACTUAL_CLAUSE )   "
            + " SELECT (PRG_GRP_BUSS_TAB_RLT_MSTR_SEQ.CURRVAL)  AS \"BUSS_RELATIONSHIP_ID\",(PRG_GRP_BUSS_TAB_RLT_DTLS_SEQ.NEXTVAL) AS \"BUSS_RELATIONSHIP_DETAIL_ID\","
            + "(SELECT buss_table_id FROM PRG_GRP_BUSS_TABLE_MASTER_VIEW WHERE db_table_id=P_TABLE_ID) AS \"BUSS_TABLE_ID1\", "
            + "(SELECT BUSS_COLUMN_ID    FROM PRG_GRP_BUSS_TABLE_DTLS_VIEW  WHERE db_table_id IN (P_TABLE_ID) AND db_column_id IN (P_COL_ID1)) AS \"P_BUSS_COL_ID1\","
            + "(SELECT BUSS_COLUMN_ID    FROM PRG_GRP_BUSS_TABLE_DTLS_VIEW  WHERE db_table_id IN (P_TABLE_ID) AND db_column_id IN (P_COL_ID2))  AS \"P_BUSS_COL_ID2\","
            + "  (SELECT buss_table_id FROM PRG_GRP_BUSS_TABLE_MASTER_VIEW WHERE db_table_id=S_TABLE_ID) AS \"BUSS_TABLE_ID1\","
            + "(SELECT BUSS_COLUMN_ID    FROM PRG_GRP_BUSS_TABLE_DTLS_VIEW  WHERE db_table_id IN (S_TABLE_ID) AND db_column_id IN (S_COL_ID1)) AS \"S_BUSS_COL_ID1\","
            + "  (SELECT BUSS_COLUMN_ID    FROM PRG_GRP_BUSS_TABLE_DTLS_VIEW  WHERE db_table_id IN (S_TABLE_ID) AND db_column_id IN (S_COL_ID2)) AS \"S_BUSS_COL_ID2\","
            + "JOIN_TYPE ,JOIN_OPERATOR,ACTUAL_CLAUSE  FROM PRG_DB_TABLE_RLT_DETAILS WHERE relationship_id=&"}, {"getNetworkConnectionDetails", "SELECT CONNECTION_NAME, USER_NAME, PASSWORD, SERVER, SERVICE_ID, SERVICE_NAME, PORT,  DSN_NAME, DB_CONNECTION_TYPE, DBNAME  FROM PRG_USER_CONNECTIONS where connection_id='&'"}
    };
}
