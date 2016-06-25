/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.querylayer;

import java.io.Serializable;
import java.util.ListResourceBundle;

public class DimensionResourceBundle extends ListResourceBundle implements Serializable {

    protected Object[][] getContents() {
        return contents;
    }
    static final Object contents[][] = {
        {"addParentColumn", " ALTER TABLE & ADD  &  &(&) "}, {"updateParentColumnValues", "UPDATE  & SET &='&' WHERE & in(&)"}, {"updateParentColumnValuesothers", "UPDATE  & SET &='&' WHERE & not in(&)"}, {"addDbTableDets", "INSERT INTO PRG_DB_MASTER_TABLE_DETAILS(COLUMN_ID, TABLE_ID, TABLE_COL_NAME, TABLE_COL_CUST_NAME, COL_TYPE, COL_LENGTH, IS_PRIMARY_KEY, IS_ACTIVE) values(&,&,'&','&','&',&,'&','&')"}, {"addDimtabDets", "INSERT INTO PRG_QRY_DIM_TAB_DETAILS(DIM_TAB_COL_ID, DIM_TAB_ID, COL_ID, IS_AVAILABLE, IS_PK_KEY) values(&,&,&,'&','&')"}, {"addDimMember", "INSERT INTO PRG_QRY_DIM_MEMBER(MEMBER_ID, MEMBER_NAME, DIM_ID, DIM_TAB_ID, USE_DENOM_TABLE, DENOM_TAB_ID, DENOM_QUERY, MEMBER_DESC) values(&,'&',&,&,'&',&,'&','&')"}, {"addDimMemDets", "INSERT INTO PRG_QRY_DIM_MEMBER_DETAILS(MEM_DETAIL_ID, MEM_ID, COL_ID, COL_TYPE) values(&,&,&,'&')"}, {"updateRelLevel", "update prg_qry_dim_rel_details set rel_level= rel_level+1 where rel_id=& and rel_level >=&"}, {"addDimRelDets", "INSERT INTO PRG_QRY_DIM_REL_DETAILS(REL_ID, MEM_ID, REL_LEVEL) values(&,&,&)"}, {"addNewTable", "Create table &(&)"}, {"insertParentColumnValues", "insert into & (id,&,parent) values(&,'&','&')"}, {"adddbTable", "INSERT INTO  PRG_DB_MASTER_TABLE(CONNECTION_ID, USER_SCHEMA, TABLE_ID, TABLE_NAME, TABLE_ALIAS, TABLE_TYPE, CREATED_BY, UPDATED_BY, CREATED_ON, UPDATED_ON, DB_QUERY) "
            + "values(&,'&',&,'&','&','&','&','&',&,&,'&')"}, {"addDimTable", "INSERT INTO PRG_QRY_DIM_TABLES(DIM_TAB_ID, DIM_ID, TAB_ID) values(&,&,&) "}
    };
}
