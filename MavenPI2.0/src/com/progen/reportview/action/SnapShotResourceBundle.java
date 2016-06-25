/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.reportview.action;

import java.io.Serializable;
import java.util.ListResourceBundle;

/**
 *
 * @author Saurabh
 */
public class SnapShotResourceBundle extends ListResourceBundle implements Serializable {

    protected Object[][] getContents() {
        return contents;
    }
    static final Object contents[][] = {
        {"getReportParamDetails", "select   element_id,disp_name,'' childElementId, dim_id, dim_tab_id, 'C' displayType,'' as relLevel,'All'  defaultValue,'N' isDisplay "
            + "from prg_user_all_info_details  where element_id in (&) order by decode (element_id,&)"}, {"deletesnapshot", "delete from prg_ar_personalized_reports where PRG_PERSONALIZED_ID='&'"}, {"insertsnapshot", "insert into prg_ar_personalized_reports"
            + " (PRG_PERSONALIZED_ID,PRG_USER_ID,PRG_REPORT_ID,PRG_REPORT_CUST_NAME,PRG_REPORT_PARAMETERS,PRG_REPORT_CONTEXT_PATH,PRG_CREATED_BY,SNAP_URL,MODULE_TYPE)"
            + "  values (prg_personalised_seq.nextval,?,?,?,?,?,?,?,?)"}, {"insertHeadline", "insert into prg_ar_personalized_reports"
            + " (PRG_PERSONALIZED_ID,PRG_USER_ID,PRG_REPORT_ID,PRG_REPORT_HEADLINE,PRG_REPORT_PARAMETERS,PRG_REPORT_CONTEXT_PATH,PRG_CREATED_BY,SNAP_URL)"
            + "  values (prg_personalised_seq.nextval,?,?,?,?,?,?,?)"}, {"getReportHeadlines", "select PRG_PERSONALIZED_ID,PRG_REPORT_HEADLINE,PRG_REPORT_ID from PRG_AR_PERSONALIZED_REPORTS where PRG_REPORT_HEADLINE is not null"}, {"updateSchedulerParamDetails", "update prg_ar_personalized_reports set PRG_USER_ID=?,PRG_REPORT_PARAMETERS=?,PRG_REPORT_CONTEXT_PATH=?,SNAP_URL=? where PRG_PERSONALIZED_ID=? and PRG_REPORT_ID=?"}
    };
}
