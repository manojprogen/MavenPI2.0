package com.progen.target;

import java.io.Serializable;
import java.util.ListResourceBundle;

public class PbTargetCollectionResourceBundle extends ListResourceBundle implements Serializable {

    @Override
    protected Object[][] getContents() {
        return contents;
    }
    static final Object contents[][] = {
        {"getTargetParamMetaDataQuery1", "select ELEMENT_ID,PARAM_DISP_NAME,CHILD_ELEMENT_ID,DIM_ID,DIM_TAB_ID,"
            + "DISPLAY_TYPE,REL_LEVEL,DISP_SEQ_NO,DEFAULT_VALUE   from PRG_TARGET_PARAM_DETAILS "
            + "where TARGET_ID=& order by DISP_SEQ_NO"},
        {"getTargetParamMetaDataQuery2", "select vm.view_by_id,td.target_id,vm.view_by_seq,vm.row_seq,vm.col_seq,VM.LABEL_NAME, "
            + "vm.label_type,vd.param_disp_id,td.param_disp_name,td.element_id,td.dim_id,"
            + "td.dim_tab_id,td.buss_table,td.child_element_id,vd.disp_seq_no,vm.default_value from PRG_TARGET_VIEW_MASTER vm,"
            + "PRG_TARGET_VIEW_BY_DETAILS  vd,PRG_TARGET_PARAM_DETAILS td where vm.view_by_id = vd.view_by_id and td.PARAM_DISP_ID = vd.PARAM_DISP_ID and "
            + "td.target_id=& order by vd.view_by_id, vm.col_seq , vm.row_seq, vd.DISP_SEQ_NO"},
        //{"getTargetParamMetaDataQuery2","select distinct label_name,label_type from prg_target_view_master where target_id=&"},
        {"getTargetParameterTimeInfo", "select m.tar_time_id , m.time_level , m.time_type , d.column_type, d.column_name, d.sequence, d.form_sequence "
            + " from prg_target_time m , prg_target_time_detail d where m.tar_time_id = d.tar_time_id "
            + "and m.target_id=& order by d.form_sequence "}
    };
}
