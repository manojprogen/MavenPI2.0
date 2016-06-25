package com.progen.scenarioview.db;

import org.apache.log4j.Logger;
import prg.db.PbDb;
import prg.db.PbReturnObject;
import prg.db.Session;

public class ScenarioDb extends PbDb {

    public static Logger logger = Logger.getLogger(ScenarioDb.class);

    public PbReturnObject getCustModelForScenario(Session ppmp) throws Exception {
        PbReturnObject pbro = null;

        PbScenarioParamVals scnPVal = (PbScenarioParamVals) ppmp.getObject("com.progen.scenarioview.db.PbScenarioParamVals");
        String scenarioId = scnPVal.getScenarioId();

        String finalQuery = "select custom_model_id,custom_model_name, custom_model_type, model_basis, weight_type from prg_custom_model_master where scenario_id=" + scenarioId;

        // //.println("getModelIds final query is: "+finalQuery);

        try {
            pbro = execSelectSQL(finalQuery);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }

        return pbro;
    }

    public PbReturnObject getCustModelDet(Session ppmp) throws Exception {
        PbReturnObject pbro = null;

        PbScenarioParamVals scnPVal = (PbScenarioParamVals) ppmp.getObject("com.progen.scenarioview.db.PbScenarioParamVals");
        // String custModIds =scnPVal.getCustomModelId();
        String scenarioId = scnPVal.getScenarioId();
        //String query = "select model_details_id,model_details_id, custom_model_months, custom_model_weights  from prg_custom_model_details where custom_model_id in(select custom_model_id from prg_custom_model_master where scenario_id="+scenarioId+")";
        String query = "select d.model_details_id, custom_model_months, custom_model_weights, m.custom_model_name  from prg_custom_model_details d,prg_custom_model_master m where d.custom_model_id in(select custom_model_id from prg_custom_model_master where scenario_id=" + scenarioId + ") and m.custom_model_id= d.custom_model_id order by m.custom_model_name";
        // //.println("query . "+query);
        try {
            pbro = execSelectSQL(query);
        } catch (Exception c) {
            logger.error("Exception:", c);
        }
        return pbro;
    }
}
