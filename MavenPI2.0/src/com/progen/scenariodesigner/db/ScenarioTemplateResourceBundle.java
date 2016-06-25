package com.progen.scenariodesigner.db;

import java.io.Serializable;
import java.util.ListResourceBundle;

public class ScenarioTemplateResourceBundle extends ListResourceBundle implements Serializable {

    @Override
    protected Object[][] getContents() {
        return contents;
    }
    static final Object contents[][] = {
        {"getScenarioDims", "select DISTINCT  pusft.dim_id,pusft.dim_name, min(pufd.sub_folder_id) over(partition by pusft.dim_id) subFolder_id from "
            + "PRG_USER_SUB_FOLDER_TABLES pusft, PRG_USER_FOLDER_DETAIL pufd where pusft.sub_folder_id =pufd.sub_folder_id and pufd.sub_folder_type='Dimensions'"
            + " and pufd.folder_id in (&) and pusft.dim_name!='Time'"}, {"getScenarioDimsMbrs", "  SELECT DISTINCT K.VAL_ELEMENT_ID, K.VAL_DISP_NAME,m.info_member_id "
            + " FROM PRG_USER_ALL_DDIM_KEY_VAL_ELE K, prg_user_all_ddim_master m   where k.val_SUB_FOLDER_id='&' AND K.KEY_DIM_ID='&' "
            + "and  m.info_dim_id=K.KEY_DIM_ID and K.VAL_DISP_NAME=m.info_member_name"}, {"getAllScenarioNames", "select * from scenario_master"}, {"getUserFoldersByUserId", " SELECT distinct folder_Id,FOLDER_NAME,grp_id FROM PRG_USER_FOLDER WHERE grp_id in(select distinct grp_id from prg_grp_user_assignment where user_id=&) "}, {"getFacts", "select DISTINCT sub_folder_tab_id,NVL(disp_name,sub_folder_type) from PRG_USER_ALL_INFO_DETAILS where folder_id in (&) and sub_folder_type in('Facts','Formula')"}, {"getFactElements", "select DISTINCT element_id,nvl(USER_COL_DESC, USER_COL_NAME),ref_element_id as column_name,BUSS_COL_NAME, REF_ELEMENT_TYPE "
            + "from PRG_USER_ALL_INFO_DETAILS where folder_id in (&) and sub_folder_tab_id = '&' and sub_folder_type in('Facts','Formula') and use_report_flag='Y'  order by BUSS_COL_NAME,REF_ELEMENT_TYPE"}, {"getAllMonths", "select mon_name from prg_acn_mon_denom"}, {"getAllYears", "select distinct CWYEAR from PR_DAY_DENOM"}, {"getAllSeededModels", "select * from scenario_seeded_models where MODEL_TYPE='year'"}, {"getSequenceNumber", "select &.nextval from dual"}, {"getScenarioParamDetails", "select   element_id,disp_name,'' childElementId, dim_id, dim_tab_id, 'C' displayType,'' as relLevel,'All'  defaultValue,'N' isDisplay "
            + "from prg_user_all_info_details  where element_id in (&) order by decode (element_id,&)"}, {"insertScenarioXML", "update SCENARIO_MASTER set xml_path=? where scenario_id=?"}, {"getSeededModels", "select 1 as MODEL_ID,'Last Two Years Average' as MODEL_NAME,'Last Two Years Average' as MODEL_DESC,'N' as IS_SEEDED from dual "
            + "UNION ALL select MODEL_ID,MODEL_NAME,MODEL_DESC,IS_SEEDED from scenario_seeded_models where model_id in(&) "
            + "order by MODEL_ID"}, {"getRelLevels", "select * from prg_user_all_adim_key_val_ele where key_element_id in(&) order by val_dim_id,level1"}, {"insertScenarioMaster", "insert into scenario_master(QRY_ELEMENT_ID,MEASURE_NAME,SCENARIO_ID,SCENARIO_NAME,SCENARIO_DESC,SCENARIO_TIME_LEVEL,SCENARIO_START_MONTH, "
            + " SCENARIO_END_MONTH,SCENARIO_START_DATE,SCENARIO_END_DATE,USER_ID,HISTORICAL_ST_MONTH,HISTORICAL_END_MONTH,SCENARIO_STATUS,BUSS_ROLE)"
            + " values('&','&','&','&','&','&','&','&','&','&','&','&','&','&','&')"}, {"getEditScenario", "select SCENARIO_NAME,SCENARIO_DESC,HISTORICAL_ST_MONTH,HISTORICAL_END_MONTH,SCENARIO_START_MONTH,SCENARIO_END_MONTH FROM scenario_master where SCENARIO_ID ='&'"}, {"upDateScenario", "update scenario_master set scenario_name='&',scenario_desc='&',historical_st_month='&',historical_end_month='&',scenario_start_month='&',"
            + "scenario_end_month='&' where scenario_id='&'"}, {"deleteScenarioMaster", "delete from scenario_master where scenario_id in (&)"}, {"deleteScenarioModelMaster", "delete from scenario_model_master where scenario_id in (&) and scn_model_name in (&)"}, {"getScstrtMonths", "select mon_name from prg_acn_mon_denom where cm_st_date >"
            + "(select cm_st_date from prg_acn_mon_denom where mon_name=(select HISTORICAL_END_MONTH FROM scenario_master where SCENARIO_ID ='&'))"}, {"deleteCustomModelMaster", "delete from prg_custom_model_master where scenario_id in (&) and custom_model_name in (&)"}, {"deleteCustomModelDetails", "delete from prg_custom_model_details where custom_model_id in (select custom_model_id from prg_custom_model_master where "
            + "scenario_id in (&) and custom_model_name in (&))"}, {"updateScenarioRating", "update scenario_model_master set scenario_rating='&' where scenario_id=& and scn_model_name='&' and dimension_id=&"}, {"getFolderIdbyScenarioId", "select buss_role from scenario_master where scenario_id=&"}, {"getviewByIdbyScenarioId", "select qry_element_id from scenario_master where scenario_id=&"}, {"getScenarioDetsbyScenarioId", "select * from scenario_master where scenario_id=&"}, {"addNewMeasure", "update SCENARIO_MASTER set CALC_ELEMENT_ID='&' where SCENARIO_ID='&'"}
    };
}
