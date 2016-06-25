package com.progen.scenarion;

import java.io.Serializable;
import java.util.ListResourceBundle;

public class PbScenarioCollectionResourceBundle extends ListResourceBundle implements Serializable {

    @Override
    protected Object[][] getContents() {
        return contents;
    }
    static final Object contents[][] = {
        {"getScenarioDetails", "select * from scenario_master where scenario_id=& "}, {"getScenarioModelMaster", "select * from scenario_model_master where scenario_id=& and scn_model_name in(&)"}
    };
}
