package prg.scenario.db;

import java.io.Serializable;
import java.util.ListResourceBundle;

public class PbScenarioResourceBundle extends ListResourceBundle implements Serializable {

    protected Object[][] getContents() {
        return contents;
    }
    static final Object contents[][] = {
        {"getSequenceNumber", "select &.nextval from dual"},
        {"addScenarioModelMaster", "insert into scenario_model_master(scn_model_id,scn_model_name,scenario_id,non_all_combo,viewby,new_url,dimension_id,context_path,MODEL_DISP_NAME) values('&','&','&','&','&','&','&','&','&')"},
        {"updateScenarioRating", "update scenario_MODEL_master set scenario_rating='&' where scenario_id=& AND SCN_MODEL_NAME='&' and dimension_id='&'"},
        {"updateScenarioMaster", "update scenario_master set scenario_name='&',scenario_desc='&',historical_st_month='&',historical_end_month='&',scenario_start_month='&',"
            + "scenario_end_month='&' where scenario_id=&"},
        {"updateScenarioStatus", "update scenario_master set scenario_status='&' where scenario_id=&"},
        {"getScenarioMaster", "select * from scenario_master where scenario_id=&"},
        {"getAllSavedSeededModels", "select * from scenario_model_master where scenario_id=&"},
        {"saveScnBudetingDetails1", "update MACL_SALES_BUDGET_TARGET set final_growth='&',exp_growth='&' where bpname='&' AND scenario_id= &"},
        {"saveScnBudetingDetails", "update MACL_SALES_BUDGET_TARGET set final_growth='&',exp_growth='&' where item='&' AND scenario_id= &"},
        {"saveScnBudetingDetailsAll", "update MACL_SALES_BUDGET_TARGET set final_growth='&',exp_growth='&' where scenario_id= &"}
    };
}
