/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.metadata;

import java.io.Serializable;
import java.util.ListResourceBundle;

/**
 *
 * @author progen
 */
public class MetadataResourceBundle extends ListResourceBundle implements Serializable {

    @Override
    protected Object[][] getContents() {
        return contents;
    }
    static final Object contents[][] = {
        {"getUserFolderList", "SELECT f.FOLDER_ID, f.FOLDER_NAME, f.FOLDER_DESC, g.GRP_NAME,G.GRP_ID FROM PRG_USER_FOLDER  f, PRG_GRP_MASTER g"
            + " where g.grp_id=f.grp_id and g.grp_id in(select grp_id from prg_grp_master where connection_id in (&))  order by f.FOLDER_CREATED_ON, f.folder_updated_by"},
        {"getUserSubFolderList", "  select SUB_FOLDER_ID,SUB_FOLDER_NAME,SUB_FOLDER_TYPE from PRG_USER_FOLDER_DETAIL where folder_id='&' AND sub_folder_name ='Dimensions'"}, {"getUserFolderDims", "SELECT DISTINCT DIM_ID, DIM_NAME,use_report_member FROM PRG_USER_SUB_FOLDER_TABLES where is_dimension='Y' and sub_folder_id='&' order by dim_name"}, {"getUserFolderDimMbrs", " SELECT SUB_FOLDER_TAB_ID,MEMBER_ID,case when DISP_NAME is null then MEMBER_NAME  when DISP_NAME =''     then MEMBER_NAME  else DISP_NAME end AS MEM_DISP_NAME,DIM_TAB_ID,use_report_member,use_report_dim_member FROM PRG_USER_SUB_FOLDER_TABLES WHERE sub_folder_id ='&' AND is_dimension  ='Y' AND dim_id ='&' ORDER BY mem_disp_name"} //          ,{"getMemberDetails","SELECT SE.ELEMENT_ID,SE.BUSS_TABLE_ID,BT.BUSS_TABLE_NAME,SE.BUSS_COL_ID,SE.BUSS_COL_NAME  FROM PRG_USER_SUB_FOLDER_TABLES SF, PRG_USER_SUB_FOLDER_ELEMENTS SE,PRG_GRP_BUSS_TABLE BT WHERE SF.SUB_FOLDER_ID=SE.SUB_FOLDER_ID"
        //                  + " AND SF.SUB_FOLDER_TAB_ID=SE.SUB_FOLDER_TAB_ID AND SF.BUSS_TABLE_ID  =SE.BUSS_TABLE_ID AND SF.MEMBER_ID  = & AND SF.DIM_ID = & AND SE.SUB_FOLDER_ID =& AND SF.BUSS_TABLE_ID =BT.BUSS_TABLE_ID "}
        , {"filterMembersValues", "SELECT USER_FILTER_ID,USER_ID ,MEMBER_VALUE FROM PRG_USER_ROLE_MEMBER_FILTER WHERE DIM_ID =& AND MEMBER_ID = & "}, {"filterRoleMembersValues", "SELECT ROLE_FILTER_ID,MEMBER_ID,FOLDER_ID,MEMBER_VALUE FROM PRG_ROLE_MEMBER_FILTER WHERE FOLDER_ID =(SELECT FOLDER_ID FROM PRG_USER_FOLDER_DETAIL where SUB_FOLDER_ID = & ) AND MEMBER_ID = &"}, {"getMemberDetails", " SELECT COALESCE(reld.rel_level,1) level1,key1.element_id KEY_ELEMENT_ID,key1.USER_COL_TYPE Key_col_type,val1.element_id val_element_id,key1.buss_col_id KEY_BUSS_COL_ID,val1.buss_col_id val_buss_col_id,key1.buss_col_name KEY_BUSS_COL_NAME,"
            + "val1.buss_col_name val_buss_col_name,key1.buss_table_id KEY_BUSS_TABLE_ID,val1.buss_table_id val_buss_table_id,val1.USER_COL_TYPE val_col_type,key1.member_name key_member_name,val1.member_name val_member_name,key1.SUB_FOLDER_NAME key_SUB_FOLDER_NAME,"
            + "  val1.SUB_FOLDER_NAME val_SUB_FOLDER_NAME,key1.SUB_FOLDER_ID key_SUB_FOLDER_ID,val1.SUB_FOLDER_ID val_SUB_FOLDER_ID,key1.disp_name key_disp_name,key1.dim_id key_dim_id,key1.dim_name key_dim_name,val1.disp_name val_disp_name,val1.dim_id val_dim_id,"
            + "val1.dim_name val_dim_name,  key1.folder_id key_folder_id ,key1.folder_name key_folder_name ,relm.rel_name ,relm.is_default,key1.BUSS_TABLE_NAME FROM PRG_GRP_DIM_MAP_KEY_VALUE map INNER JOIN PRG_USER_ALL_INFO_DETAILS key1 ON (map.key1 = key1.buss_col_id AND"
            + " key1.member_id = map.key_mem_id) INNER JOIN PRG_USER_ALL_INFO_DETAILS val1 ON ( val1.member_id  = map.mem_mem_id AND val1.buss_col_id = map.value1) LEFT OUTER JOIN prg_grp_dim_rel relm ON (key1.dim_id = relm.dim_id) LEFT OUTER JOIN "
            + "prg_grp_dim_rel_details reld ON (key1.member_id    = reld.mem_id) WHERE key1.folder_id IN( &)AND val1.folder_id IN( &)  AND key1.member_id IN(&) ORDER BY 1 "}
    };
}
