/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prg.business.group;

import java.io.Serializable;
import java.util.ListResourceBundle;

public class Deletingbusrolesresourcebundle extends ListResourceBundle implements Serializable {

    protected Object[][] getContents() {
        return contents;
    }
    static final Object contents[][] = {
        {"getuserfolders", "select * from prg_grp_user_assignment where user_id='&' and grp_id='&'"}, {"getfolderids", "select case when b.FOLDER_ID is null then 0 else b.FOLDER_ID end FOLDER_ID from prg_grp_user_assignment a left outer join prg_user_folder b on (a.grp_id = b.grp_id) where   a.user_id ='&'"}, {"getreportids", "select report_id from prg_ar_report_details where folder_id in(&)"} //---To Delete Selected Business Roles---//
        , {"deletebusroles1", "delete from prg_user_all_ddim_master where info_folder_id in(select folder_id from prg_user_folder where grp_id='&')"}, {"deletebusroles2", "delete from PRG_USER_ALL_DDIM_KEY_VAL_ELE where key_folder_id in(select folder_id from prg_user_folder where grp_id='&')"}, {"deletebusroles3", "delete from prg_user_all_ddim_details where INFO_FOLDER_ID in(select folder_id from prg_user_folder where grp_id='&')"}, {"deletebusroles4", "delete from prg_user_all_adim_master where INFO_FOLDER_ID in(select folder_id from prg_user_folder where grp_id='&')"}, {"deletebusroles5", "delete from prg_user_all_adim_key_val_ele where key_folder_id in(select folder_id from prg_user_folder where grp_id='&')"}, {"deletebusroles6", "delete from prg_user_all_adim_details where info_folder_id in(select folder_id from prg_user_folder where grp_id='&')"}, {"deletebusroles7", "delete from prg_user_all_info_details where folder_id in(select folder_id from prg_user_folder where grp_id='&')"}, {"deletebusroles8", "delete from prg_user_sub_folder_tables where sub_folder_id in(select folder_id from prg_user_folder where grp_id='&')"}, {"deletebusroles9", "delete from prg_grp_role_custom_drill where sub_folder_id in( select sub_folder_id from prg_user_folder_detail where sub_folder_id in(select folder_id from prg_user_folder where grp_id='&'))"}, {"deletebusroles10", "delete from prg_user_folder_detail where sub_folder_id in(select folder_id from prg_user_folder where grp_id='&')"}, {"deletebusroles11", "delete from prg_user_sub_folder_elements where sub_folder_id in(select sub_folder_id from prg_user_folder_detail where folder_id in(select folder_id from prg_user_folder where grp_id='&'))"} /*
         * ,{"deletebusroles12","delete from prg_grp_buss_table_details where
         * buss_table_id in(select buss_table_id FROM prg_grp_buss_table where
         * grp_id = '&')"} ,{"deletebusroles13","delete from
         * prg_grp_buss_table_src where buss_table_id in(select buss_table_id
         * FROM prg_grp_buss_table where grp_id = '&')"}
         * ,{"deletebusroles14","delete from prg_grp_buss_table_src_details
         * where buss_table_id in(select buss_table_id FROM prg_grp_buss_table
         * where grp_id = '&')"}
         */, {"deletebusroles12", "delete from prg_grp_dim_member where dim_id in(select dim_id from prg_grp_dimensions where grp_id = '&')"}, {"deletebusroles13", "delete from prg_grp_dim_member_details where mem_id in(select dim_id from prg_grp_dimensions where grp_id = '&')"}, {"deletebusroles14", "delete from prg_grp_dim_rel_details where rel_id in(select rel_id from prg_grp_dim_rel where dim_id in(select dim_id from prg_grp_dimensions where grp_id = '&'))"}, {"deletebusroles15", "delete from prg_grp_dim_rel where dim_id in(select dim_id from prg_grp_dimensions where grp_id = '&')"}, {"deletebusroles16", "delete from prg_grp_dim_tab_details where dim_tab_id in(select tab_id from prg_grp_dim_tables where dim_id in(select dim_id from prg_grp_dimensions where grp_id = '&'))"}, {"deletebusroles17", "delete from prg_grp_dim_tables where dim_id in(select dim_id from prg_grp_dimensions where grp_id = '&')"}, {"deletebusroles18", "delete from prg_grp_user_assignment where grp_id='&'"}, {"deletebusroles19", "delete from prg_grp_dimensions where grp_id = '&'"} //   ,{"deletebusroles22","delete from prg_grp_buss_table where grp_id='&'"}
        ///----To delete Reports
        , {"deletebusreports1", "delete from prg_ar_query_detail where report_id in(select folder_id from prg_user_folder where grp_id='&')"}, {"deletebusreports2", "delete from prg_ar_user_reports where report_id in(select folder_id from prg_user_folder where grp_id='&')"}, {"deletebusreports3", "delete from prg_ar_report_view_by_details where view_by_id in(select folder_id from prg_user_folder where grp_id='&')"}, {"deletebusreports4", "delete from prg_ar_report_view_by_master where view_by_id in(select folder_id from prg_user_folder where grp_id='&')"}, {"deletebusreports5", "delete from prg_ar_report_time_detail where rep_time_id in(select folder_id from prg_user_folder where grp_id='&')"}, {"deletebusreports6", "delete from prg_ar_report_time where rep_time_id in(select folder_id from prg_user_folder where grp_id='&')"}, {"deletebusreports7", "delete from prg_ar_report_table_master where report_id in(select folder_id from prg_user_folder where grp_id='&')"}, {"deletebusreports8", "delete from prg_ar_report_table_details where report_id in(select folder_id from prg_user_folder where grp_id='&')"}, {"deletebusreports9", "delete from prg_ar_report_parents where report_id in(select folder_id from prg_user_folder where grp_id='&')"}, {"deletebusreports10", "delete from prg_ar_report_param_details where report_id in(select folder_id from prg_user_folder where grp_id='&')"}, {"deletebusreports11", "delete from prg_ar_report_master where report_id in(select folder_id from prg_user_folder where grp_id='&')"}, {"deletebusreports12", "delete from prg_ar_report_details where folder_id in(select folder_id from prg_user_folder where grp_id='&')"} ///-----To delete Business group tables
        , {"deletebussgrp1", "delete from prg_grp_buss_table_details where buss_table_id in(select buss_table_id FROM prg_grp_buss_table where grp_id = '&')"}, {"deletebussgrp2", "delete from prg_grp_buss_table_src where buss_table_id in(select buss_table_id FROM prg_grp_buss_table where grp_id = '&')"}, {"deletebussgrp3", "delete from prg_grp_buss_table_src_details where buss_table_id in(select buss_table_id FROM prg_grp_buss_table where grp_id = '&')"}, {"deletebussgrp4", "delete from prg_grp_buss_table where grp_id='&'"}, {"deletebussgrp5", "delete from prg_grp_master where grp_id='&'"}, {"deletebussgrp6", "delete from prg_user_folder where grp_id='&'"}
//,{"deletebusroles23",""}
//   ,{"deletebusroles24","delete from prg_grp_master where grp_id='&'"}
//   ,{"deletebusroles25","delete from prg_user_folder where grp_id='&'"}
//
    // added by praveen over
    };
}