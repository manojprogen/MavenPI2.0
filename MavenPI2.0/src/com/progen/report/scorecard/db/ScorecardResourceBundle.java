/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.scorecard.db;

import java.io.Serializable;
import java.util.ListResourceBundle;

/**
 *
 * @author progen
 */
public class ScorecardResourceBundle extends ListResourceBundle implements Serializable {

    @Override
    protected Object[][] getContents() {
        return contents;
    }
    static final Object contents[][] = {
        {"getScoreCards", "select master.scard_id, master.scard_name, master.scard_area, master.no_of_days, master.lighttype,"
            + " member.scard_mem_id,member.scard_contribution, member.scard_seq,"
            + " kpi.element_id, kpi.prior_element_id, kpi.change_element_id, kpi.changep_element_id, kpi.scard_basis, kpi.element_name,"
            + " member.scard_type, member.child_scard_id, master.folder_id, kpi.dimension_id, kpi.dimension_value, member.period,"
            + " kpi.target_element_id,kpi.TARGET_TYPE,kpi.TARGET_VALUE ,master.TARGET_SCORE from"
            + " prg_ar_scard_master master, "
            + " prg_ar_scard_member member,"
            + " prg_ar_scard_member_kpi kpi"
            + " where"
            + "  (kpi.scard_basis is null or kpi.scard_basis not in('AdHoc')) and "
            + " master.scard_id = member.scard_id"
            + " and member.scard_mem_id = kpi.scard_mem_id (+)"
            + " and master.scard_id in (&) order by master.scard_id, member.scard_seq"},
        {"getUserFoldersByUserId", "SELECT FOLDER_ID,FOLDER_NAME,GRP_ID FROM PRG_USER_FOLDER WHERE folder_id IN(SELECT user_folder_id "
            + "FROM prg_grp_user_folder_assignment WHERE user_id='&' ) order by FOLDER_NAME asc"},
        {"saveScardDetails", "Insert into PRG_AR_SCARD_MASTER(SCARD_ID,SCARD_NAME,SCARD_AREA,NO_OF_DAYS,CREATED_BY,UPDATED_BY,CREATE_DATE,UPDATE_DATE,LIGHTTYPE,FOLDER_ID,TARGET_SCORE) "
            + " values(&,'&','&',&,&,&,&,&,'&','&',&)"},
        {"saveScardMemDetails", "Insert into PRG_AR_SCARD_MEMBER(SCARD_ID,SCARD_MEM_ID,SCARD_TYPE,SCARD_CONTRIBUTION,SCARD_SEQ,UNITS,PERIOD,CHILD_SCARD_ID)"
            + " values(&,&,'&',&,&,'&','&',&)"},
        {"saveScardMemKpiDetails", "Insert into PRG_AR_SCARD_MEMBER_KPI(SCARD_MEMBER_KPI_ID,SCARD_ID,SCARD_MEM_ID,ELEMENT_ID,SCARD_BASIS,ELEMENT_NAME,PRIOR_ELEMENT_ID,CHANGE_ELEMENT_ID,CHANGEP_ELEMENT_ID) "
            + "values(&,&,&,&,'&','&',&,&,&)"},
        {"saveScardMemKpiRules", "Insert into PRG_AR_SCARD_MEMBER_KPI_RULE(SCARD_MEMBER_KPI_RULE_ID,SCARD_DEATILS_ID,SCARD_ID,SCARD_MEM_ID,SCARD_MEM_VALUE_ST,SCARD_MEM_VALUE_END,SCARD_SCORE,OPERATOR) "
            + " values(&,&,&,&,&,&,&,'&')"},
        {"saveScardtoUser", "Insert into PRG_AR_SCARD_USERS(SCARD_ID,USER_ID) values(&,&)"},
        {"saveScardColor", "Insert into PRG_AR_SCORECARD_LIGHTS(SCARDID,SCORE_START,SCORE_END,LIGHT_COLOR,SCARD_LIGHTS_ID,OPERATOR) "
            + " values(&,&,&,'&',&,'&')"},
        {"getPriorChangeElements", "select ELEMENT_ID , REF_ELEMENT_ID , REF_ELEMENT_TYPE , AGGREGATION_TYPE  from  PRG_USER_ALL_INFO_DETAILS  where ELEMENT_ID in (&) OR REF_ELEMENT_ID in (&) order by REF_ELEMENT_TYPE asc"},
        {"editScardMaster", "select SCARD_NAME,SCARD_AREA,LIGHTTYPE,FOLDER_ID,TARGET_SCORE from PRG_AR_SCARD_MASTER where SCARD_ID=& "},
        {"editScardMemberKpi", "select ELEMENT_ID,SCARD_BASIS,ELEMENT_NAME,DIMENSION_ID,DIMENSION_VALUE,TARGET_ELEMENT_ID,TARGET_ELEMENT_NAME,TARGET_TYPE,TARGET_VALUE from PRG_AR_SCARD_MEMBER_KPI where SCARD_ID=& "},
        {"editScardMemberDetails", "select SCARD_TYPE,SCARD_CONTRIBUTION,UNITS,PERIOD,CHILD_SCARD_ID from PRG_AR_SCARD_MEMBER where SCARD_ID=& order by SCARD_MEM_ID"},
        {"getMemberIds", "select distinct SCARD_MEM_ID from PRG_AR_SCARD_MEMBER_KPI_RULE where SCARD_ID=& order by SCARD_MEM_ID"},
        {"editScardMemberKpiRules", "select SCARD_MEM_ID,SCARD_MEM_VALUE_ST,SCARD_MEM_VALUE_END,SCARD_SCORE,OPERATOR from PRG_AR_SCARD_MEMBER_KPI_RULE where SCARD_ID=& and SCARD_MEM_ID=& order by SCARD_MEMBER_KPI_RULE_ID"},
        {"updateScardMaster", "update PRG_AR_SCARD_MASTER set SCARD_NAME='&',SCARD_AREA='&',LIGHTTYPE='&',UPDATE_DATE=& ,TARGET_SCORE=& where SCARD_ID=& "},
        {"editScardLights", "select LIGHT_COLOR,SCORE_START,SCORE_END,OPERATOR from PRG_AR_SCORECARD_LIGHTS where SCARDID=&"},
        {"getScorecardNames", "select SCARD_NAME from PRG_AR_SCARD_MASTER"},
        {"saveScardMemKpiDetailsForDimension", "Insert into PRG_AR_SCARD_MEMBER_KPI(SCARD_MEMBER_KPI_ID,SCARD_ID,SCARD_MEM_ID,ELEMENT_ID,SCARD_BASIS,ELEMENT_NAME,PRIOR_ELEMENT_ID,CHANGE_ELEMENT_ID,CHANGEP_ELEMENT_ID,DIMENSION_ID,DIMENSION_VALUE,TARGET_ELEMENT_ID,TARGET_ELEMENT_NAME,TARGET_TYPE,TARGET_VALUE) "
            + "values(&,&,&,&,'&','&',&,&,&,&,'&',&,'&','&',&)"},
        //     {"getScoreCardList","Select SCARD_ID,SCARD_NAME from PRG_AR_SCARD_MASTER where CREATED_BY=&"},
        {"getScoreCardList", "Select SCARD_ID,SCARD_NAME from PRG_AR_SCARD_MASTER where SCARD_ID IN (SELECT SCARD_ID FROM PRG_AR_SCARD_USERS WHERE USER_ID=&)"},
        {"getChildScardIds", "select CHILD_SCARD_ID from PRG_AR_SCARD_MEMBER where SCARD_ID=& "},
        {"getDimensions", "SELECT COALESCE(reld.rel_level,1) level1,"
            + " key1.element_id KEY_ELEMENT_ID,key1.USER_COL_TYPE Key_col_type,val1.element_id val_element_id,"
            + "key1.buss_col_id KEY_BUSS_COL_ID,val1.buss_col_id val_buss_col_id,key1.buss_col_name KEY_BUSS_COL_NAME,"
            + " val1.buss_col_name val_buss_col_name,key1.buss_table_id KEY_BUSS_TABLE_ID,val1.buss_table_id val_buss_table_id,"
            + "val1.USER_COL_TYPE val_col_type,key1.member_name key_member_name,val1.member_name val_member_name,"
            + "key1.SUB_FOLDER_NAME key_SUB_FOLDER_NAME,val1.SUB_FOLDER_NAME val_SUB_FOLDER_NAME,"
            + " key1.SUB_FOLDER_ID key_SUB_FOLDER_ID,val1.SUB_FOLDER_ID val_SUB_FOLDER_ID,key1.disp_name key_disp_name,"
            + " key1.dim_id key_dim_id,key1.dim_name key_dim_name,val1.disp_name val_disp_name,val1.dim_id val_dim_id,"
            + "val1.dim_name val_dim_name,key1.folder_id key_folder_id ,key1.folder_name key_folder_name ,"
            + " relm.rel_name ,relm.is_default,key1.BUSS_TABLE_NAME FROM PRG_GRP_DIM_MAP_KEY_VALUE map "
            + " INNER JOIN PRG_USER_ALL_INFO_DETAILS key1 ON (map.key1= key1.buss_col_id AND key1.member_id = map.key_mem_id)"
            + " INNER JOIN PRG_USER_ALL_INFO_DETAILS val1 ON ( val1.member_id  = map.mem_mem_id AND val1.buss_col_id = map.value1)"
            + " LEFT OUTER JOIN prg_grp_dim_rel relm ON (key1.dim_id = relm.dim_id) LEFT OUTER JOIN prg_grp_dim_rel_details reld"
            + " ON (key1.member_id = reld.mem_id) WHERE key1.folder_id IN (&) and  key1.dim_name != 'Time' ORDER BY 1"},
        {"shareScorecardsQry", "insert into PRG_AR_SCARD_USERS(SCARD_ID,USER_ID) values(&,&)"},
        {"editCheckQry", "select SCARD_ID from PRG_AR_SCARD_MASTER where SCARD_ID=& and CREATED_BY=&"},
        {"getOriginalMeasQry", "select  member.scard_mem_id,member.scard_contribution, member.scard_seq,"
            + " kpi.element_id, kpi.prior_element_id, kpi.change_element_id, kpi.changep_element_id, kpi.scard_basis, kpi.element_name, member.child_scard_id"
            + " from"
            + " prg_ar_scard_master master,"
            + " prg_ar_scard_member member,"
            + " prg_ar_scard_member_kpi kpi"
            + " where"
            + " (kpi.scard_basis is null or kpi.scard_basis NOT IN('AdHoc')) and "
            + " master.scard_id = member.scard_id"
            + " and member.scard_mem_id = kpi.scard_mem_id (+)"
            + " and master.scard_id in (&) order by master.scard_id, member.scard_seq"},
        {"getAdHocMeasQry", "select  member.scard_mem_id,member.scard_contribution, member.scard_seq,"
            + " kpi.element_id, kpi.prior_element_id, kpi.change_element_id, kpi.changep_element_id, kpi.scard_basis, kpi.element_name"
            + " from"
            + " prg_ar_scard_master master,"
            + " prg_ar_scard_member member,"
            + " prg_ar_scard_member_kpi kpi"
            + " where"
            + "  kpi.scard_basis  in('AdHoc') and "
            + " master.scard_id = member.scard_id"
            + " and member.scard_mem_id = kpi.scard_mem_id (+)"
            + " and master.scard_id in (&) order by master.scard_id, member.scard_seq"},
        {"updateMemWeightage", "update prg_ar_scard_member set scard_contribution=& where scard_mem_id=&"},
        {"insertAdhocMemQry", "insert into prg_ar_scard_member(SCARD_ID,SCARD_MEM_ID,SCARD_CONTRIBUTION) values(&,&,&)"},
        {"insertAdhocMemKpiQry", "insert into PRG_AR_SCARD_MEMBER_KPI(SCARD_MEMBER_KPI_ID,SCARD_ID,SCARD_MEM_ID,SCARD_BASIS,ELEMENT_NAME) "
            + " values(PRG_AR_SCARD_MEMBER_KPI_SEQ.nextval,&,&,'&','&')"}
    };
}
