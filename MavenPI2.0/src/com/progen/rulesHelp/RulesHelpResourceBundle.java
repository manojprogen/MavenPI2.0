/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.rulesHelp;

import java.io.Serializable;
import java.util.ListResourceBundle;

/**
 *
 * @author progen
 */
class RulesHelpResourceBundle extends ListResourceBundle implements Serializable {

    @Override
    protected Object[][] getContents() {
        return contents;
    }
    static final Object contents[][] = {
        {"getDimentions", "SELECT COALESCE(reld.rel_level,1) level1,key1.element_id KEY_ELEMENT_ID,key1.USER_COL_TYPE Key_col_type,val1.element_id val_element_id,key1.buss_col_id KEY_BUSS_COL_ID,val1.buss_col_id val_buss_col_id,key1.buss_col_name"
            + " KEY_BUSS_COL_NAME,val1.buss_col_name val_buss_col_name,key1.buss_table_id KEY_BUSS_TABLE_ID,val1.buss_table_id val_buss_table_id,val1.USER_COL_TYPE val_col_type,key1.member_name key_member_name,val1.member_name val_member_name,"
            + "key1.SUB_FOLDER_NAME key_SUB_FOLDER_NAME,val1.SUB_FOLDER_NAME val_SUB_FOLDER_NAME,key1.SUB_FOLDER_ID key_SUB_FOLDER_ID,val1.SUB_FOLDER_ID val_SUB_FOLDER_ID,key1.disp_name key_disp_name,key1.dim_id key_dim_id,key1.dim_name key_dim_name,"
            + "val1.disp_name val_disp_name, val1.dim_id val_dim_id, val1.dim_name val_dim_name,key1.folder_id key_folder_id ,key1.folder_name key_folder_name ,relm.rel_name ,relm.is_default, key1.BUSS_TABLE_NAME  FROM PRG_GRP_DIM_MAP_KEY_VALUE map "
            + "INNER JOIN PRG_USER_ALL_INFO_DETAILS key1 ON (map.key1 = key1.buss_col_id AND key1.member_id = map.key_mem_id) INNER JOIN PRG_USER_ALL_INFO_DETAILS val1 ON ( val1.member_id  = map.mem_mem_id AND val1.buss_col_id = map.value1) "
            + "LEFT OUTER JOIN prg_grp_dim_rel relm ON (key1.dim_id = relm.dim_id) LEFT OUTER JOIN prg_grp_dim_rel_details reld ON (key1.member_id    = reld.mem_id) WHERE key1.folder_id IN(&)AND key1.dim_name !='Time' AND key1.SUB_FOLDER_ID = val1.SUB_FOLDER_ID ORDER BY 1"}, {"saveRule", "  INSERT INTO PRG_USER_RULES VALUES(PRG_USER_RULES_SEQ.NEXTVAL,'&','&','&','&','&')"}, {"getFilterDetails", "SELECT * FROM PRG_USER_RULES WHERE REF_ID=& "}, {"getruleDimMembers", "SELECT distinct COALESCE(reld.rel_level,1) level1,key1.element_id KEY_ELEMENT_ID,key1.buss_col_name,key1.BUSS_TABLE_NAME FROM PRG_GRP_DIM_MAP_KEY_VALUE map"
            + " INNER JOIN PRG_USER_ALL_INFO_DETAILS key1 ON (map.key1 = key1.buss_col_id AND key1.member_id = map.key_mem_id) INNER JOIN PRG_USER_ALL_INFO_DETAILS val1 ON ( val1.member_id  = map.mem_mem_id "
            + "AND val1.buss_col_id = map.value1) LEFT OUTER JOIN prg_grp_dim_rel relm ON (key1.dim_id = relm.dim_id) LEFT OUTER JOIN prg_grp_dim_rel_details reld ON (key1.member_id = reld.mem_id) WHERE key1.element_id=&"}, {"getDimValues", "SELECT distinct & FROM &"}
    };
}
