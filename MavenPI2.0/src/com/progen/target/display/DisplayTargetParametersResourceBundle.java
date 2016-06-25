package com.progen.target.display;

import java.io.Serializable;
import java.util.ListResourceBundle;

public class DisplayTargetParametersResourceBundle extends ListResourceBundle implements Serializable {

    @Override
    protected Object[][] getContents() {
        return contents;
    }
    static final Object contents[][] = {
        {"getParameterQuery1", "select distinct BUSS_COL_NAME , BUSS_TABLE_ID , DISP_NAME from PRG_USER_ALL_INFO_DETAILS "
            + "where element_id ='&'"},
        {"getBussColNames", "select DISTINCT element_id, buss_col_name from prg_user_all_info_details where folder_id in(select bus_folder_id from "
            + " target_measure_folder where bus_group_id=(select business_group from prg_target_master where prg_measure_id "
            + " in(select measure_id from target_master WHERE target_master.target_id=&)))"},
        {"getTargetTable", "select target_table_id,pgbt.buss_table_name,ptm.measure_name from prg_target_master ptm,prg_grp_buss_table pgbt where prg_measure_id in (select measure_id from target_master WHERE target_master.target_id=&) and pgbt.buss_table_id = ptm.target_table_id"}, {"getDispNames", "select distinct element_id,param_disp_name from prg_target_param_details where target_id=&"}, {"getParentElementId", "select * from prg_target_param_details where target_id=& and dim_id in (select dim_id from prg_target_param_details where element_id=&) order by rel_level"}, {"getTargetPrimParam", "select * from target_master where target_id=&"}, {"getAllElementsInfo", "select * from prg_target_param_details where target_id=& order by dim_id,rel_level"}, {"getParents", "select * from prg_target_param_details where target_id=& and dim_id in(select prg_target_param_details.dim_id from prg_target_param_details where element_id=&) order by dim_id,rel_level desc"}, {"generateViewByQry2", "select distinct BUSS_TABLE_ID,BUSS_TABLE_NAME,BUSS_COL_NAME,USER_COL_NAME,denom_query,ELEMENT_ID,"
            + "REF_ELEMENT_ID, REF_ELEMENT_TYPE from prg_user_all_info_details  where ELEMENT_ID in (&)  order by BUSS_TABLE_ID,"
            + " REF_ELEMENT_ID, REF_ELEMENT_TYPE "}, {"targetTimeQ", "select to_char(target_start_date,'mm/dd/yyyy') start_date,to_char(target_end_date,'mm/dd/yyyy') end_date, target_start_month,target_end_month, "
            + "target_start_qtr, target_end_qtr,target_start_year, target_end_year from target_master where target_id =&"}
    // ,{"getAllElementsInfo","select DISTINCT d1.element_id element_id,d1.dim_id, d2.element_id  parent_id, d1.rel_level,d1.param_disp_name from prg_target_param_details d1, prg_target_param_details d2 where d1.target_id=& and d1.element_id = d2.child_element_id(+) order by d1.dim_id, d1.rel_level"}
    };
}
