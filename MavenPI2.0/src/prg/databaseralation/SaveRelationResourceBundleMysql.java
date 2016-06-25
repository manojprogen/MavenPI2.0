/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prg.databaseralation;

import java.io.Serializable;
import java.util.ListResourceBundle;

/**
 * @filename SaveRelationResourceBundle
 *
 * @author sreekanth.k@progenbusiness.com @date july 5, 2010, 1:32:06 PM
 */
public class SaveRelationResourceBundleMysql extends ListResourceBundle implements Serializable {

    @Override
    protected Object[][] getContents() {
        return contents;
    }
    static final Object contents[][] = {
        {"insertRelationsQuery", "insert into PRG_DB_TABLE_RLT_DETAILS(RELATIONSHIP_ID,P_TABLE_ID,P_COL_ID1,S_TABLE_ID,S_COL_ID1,JOIN_TYPE,"
            + "JOIN_OPERATOR,ACTUAL_CLAUSE,P_COL_ID2,S_COL_ID2) values('&','&','&','&','&','&','&','&',&,&)"}, {"getRltMasterIdQuery", "select RELATIONSHIP_ID from PRG_DB_TABLE_RLT_MASTER where TABLE_ID in ('&','&') and TABLE_ID2 in ('&','&')"}, {"insertDbTabRelMasterQuery", "insert into PRG_DB_TABLE_RLT_MASTER(TABLE_ID,TABLE_ID2,ACTUAL_CLAUSE,CLAUSE_TYPE)"
            + " values(&,&,'&','inner')"}, {"getSavedRelationsQuery", "SELECT RELATIONSHIP_DETAIL_ID,ACTUAL_CLAUSE FROM  PRG_DB_TABLE_RLT_DETAILS WHERE RELATIONSHIP_ID IN "
            + "(select RELATIONSHIP_ID from PRG_DB_TABLE_RLT_MASTER where TABLE_ID in (&) and TABLE_ID2 in (&))"}, {"updateDbTabRelMaster", "update PRG_DB_TABLE_RLT_MASTER set ACTUAL_CLAUSE='&'"
            + "where RELATIONSHIP_ID=&"}, {"getRltMasterIdQueryexist", "select RELATIONSHIP_ID,ACTUAL_CLAUSE from PRG_DB_TABLE_RLT_MASTER where TABLE_ID in ('&','&') and TABLE_ID2 in ('&','&')"}, {"relationIdquery", " select ident_current('PRG_DB_TABLE_RLT_MASTER')"}, {"relationIdquery1", " select LAST_INSERT_ID(RELATIONSHIP_ID) FROM PRG_DB_TABLE_RLT_MASTER ORDER BY 1 DESC LIMIT 1"}
    };
}