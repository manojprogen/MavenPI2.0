package prg.scenario.db;

import java.sql.Connection;
import java.util.ArrayList;
import org.apache.log4j.Logger;
import prg.db.PbDb;
import prg.db.PbReturnObject;
import prg.db.Session;
import prg.scenario.param.PbScenarioParamVals;
import utils.db.ProgenConnection;

public class PbScenarioDb extends PbDb {

    PbScenarioResourceBundle resBundle = new PbScenarioResourceBundle();
    public static Logger logger = Logger.getLogger(PbScenarioDb.class);

    public PbReturnObject getScenarioList(Session ppmp) throws Exception {
        PbReturnObject pbro = new PbReturnObject();
        String query = "";//resBundle.getString("getScenarioList");
        PbScenarioParamVals scnPVal = (PbScenarioParamVals) ppmp.getObject("prg.scenario.param.PbScenarioParamVals");
        query = " select sm.scenario_id,sm.scenario_name,sm.scenario_desc,smm.scn_model_name,sm.scenario_time_level,"
                + " sm.scenario_start_month, sm.scenario_end_month,sm.scenario_status,smm.context_path, scenario_rating,SMM.DIMENSION_ID,d.dim_name,scn_model_id,non_all_combo from scenario_master sm,scenario_model_master smm ,  "
                + " prg_grp_dimensions d where user_id=" + scnPVal.getUserId() + " and sm.scenario_id = smm.scenario_id(+) and smm.dimension_id= d.dim_id(+) order by 2";

        /*
         * , d.dim_name " select
         * sm.scenario_id,sm.scenario_name,sm.scenario_desc,smm.scn_model_name,sm.scenario_time_level,"
         * + " sm.scenario_start_month,
         * sm.scenario_end_month,sm.scenario_status,smm.context_path||'/pbviewscenariomodel.jsp?'||'scenarioId='||smm.scenario_id
         * ||chr(38) ||'scnModelId='||
         * smm.scn_model_id||chr(38)||replace(smm.new_url,',','&') viewVal,
         * nvl(smM.scenario_rating,' ') scenario_rating,SMM.DIMENSION_ID from
         * scenario_master sm,scenario_model_master smm , " + "
         * prg_grp_dimensions d where user_id="+scnPVal.getUserId()+" and
         * sm.scenario_id = smm.scenario_id(+) and smm.dimension_id=
         * d.dim_id(+)";
         */
        //////////////////////////////////////


        Object obj[] = new Object[1];
        obj[0] = scnPVal.getUserId();
        //////////////////////////////////////

        String finalQuery = "";
        finalQuery = buildQuery(query, obj);
        //////////////////////////////////////

        try {
            pbro = execSelectSQL(query);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }

        return pbro;
    }

    public void updateScenarioStatus(Session ppmp) throws Exception {
        String query = resBundle.getString("updateScenarioStatus");
        //////////////////////////////////////

        PbScenarioParamVals scnPVal = (PbScenarioParamVals) ppmp.getObject("prg.scenario.param.PbScenarioParamVals");
        Object obj[] = new Object[2];
        obj[0] = scnPVal.getScenarioStatus();
        obj[1] = Integer.valueOf(scnPVal.getScenarioId());

        String finalQuery = null;
        finalQuery = buildQuery(query, obj);
        //////////////////////////////////////

        try {
            execModifySQL(finalQuery);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }
    }

    public void updateScenarioRating(Session ppmp) throws Exception {
        String query = resBundle.getString("updateScenarioRating");
        //////////////////////////////////////

        PbScenarioParamVals scnPVal = (PbScenarioParamVals) ppmp.getObject("prg.scenario.param.PbScenarioParamVals");
        Object obj[] = new Object[4];
        obj[0] = scnPVal.getScenarioRating();
        obj[1] = scnPVal.getScenarioId();
        obj[2] = scnPVal.getModelName();
        obj[3] = Integer.valueOf(scnPVal.getDimensionId());

        String finalQuery = null;
        finalQuery = buildQuery(query, obj);
        //////////////////////////////////////

        try {
            execModifySQL(finalQuery);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }

    }

    public void addModelMaster(Session sess) throws Exception {
        PbScenarioParamVals scnPVal = (PbScenarioParamVals) sess.getObject("prg.scenario.param.PbScenarioParamVals");
        String nonAllCombo = scnPVal.getNonAllCombo();
        ArrayList insertQ = new ArrayList();
        String newUrl = scnPVal.getNewUrl();
        String scenarioId = scnPVal.getScenarioId();
        String dimensionId = scnPVal.getDimensionId();
        String modelName = scnPVal.getModelName();
        ArrayList inserQueries = new ArrayList();
        String viewBy = scnPVal.getParameterName();
        String path = scnPVal.getPath();
        String budgedispName = scnPVal.getbudgedispName();
        String scenarioName = "";
        scenarioName = scnPVal.getScenarioName();

        boolean insertFlag = false;
        //////////////////////////////////////

        String checkAlreadyQuery = "";
        checkAlreadyQuery = "select * from scenario_model_master where scn_model_name='" + modelName.trim() + "' and dimension_id=" + dimensionId + " and scenario_id=" + scenarioId;
        //////////////////////////////////////
        PbReturnObject alData = execSelectSQL(checkAlreadyQuery);
        if (alData.getRowCount() > 0) {
            insertFlag = true;
        }
        //////////////////////////////////////
        String context_path = "/ScenarioViewerAction.do?scenarioParam=viewScenario&scenarioId=" + scenarioId + "&scenarioName=" + scenarioName;
        Object scnSeqObj[] = new Object[1];
        scnSeqObj[0] = "SCENARIO_MOD_SEQ";
        String scnQuery = resBundle.getString("getSequenceNumber");
        String finalModQuery = buildQuery(scnQuery, scnSeqObj);


        String addScenarioMaster = resBundle.getString("addScenarioModelMaster");
        if (insertFlag == false) {
            int new_mod_id = getSequenceNumber(finalModQuery);
            //////////////////////////////////////
            Object insertModMaster[] = new Object[9];
            insertModMaster[0] = Integer.valueOf(new_mod_id);
            insertModMaster[1] = modelName;
            insertModMaster[2] = Integer.valueOf(scenarioId);
            insertModMaster[3] = nonAllCombo;
            insertModMaster[4] = viewBy;
            insertModMaster[5] = newUrl;
            insertModMaster[6] = Integer.valueOf(dimensionId);
            insertModMaster[7] = context_path;
            insertModMaster[8] = budgedispName;

            String finalQueryMod = buildQuery(addScenarioMaster, insertModMaster);
            //
            insertQ.add(finalQueryMod);
            try {
                executeMultiple(insertQ);
            } catch (Exception s) {
                logger.error("Exception: ", s);
            }
        }
    }

    public PbReturnObject getScenarioMaster(Session ppmp) throws Exception {
        PbReturnObject pbro = null;
        String query = resBundle.getString("getScenarioMaster");
        //////////////////////////////////////

        PbScenarioParamVals scnPVal = (PbScenarioParamVals) ppmp.getObject("prg.scenario.param.PbScenarioParamVals");
        Object obj[] = new Object[1];
        obj[0] = Integer.valueOf(scnPVal.getScenarioId());

        String finalQuery = null;
        finalQuery = buildQuery(query, obj);
        //////////////////////////////////////

        try {
            pbro = execSelectSQL(finalQuery);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }

        return pbro;
    }

    public PbReturnObject getAllSavedSeededModels(Session ppmp) throws Exception {
        PbReturnObject pbro = null;
        String query = resBundle.getString("getAllSavedSeededModels");
        //////////////////////////////////////

        PbScenarioParamVals scnPVal = (PbScenarioParamVals) ppmp.getObject("prg.scenario.param.PbScenarioParamVals");
        Object obj[] = new Object[1];
        obj[0] = Integer.valueOf(scnPVal.getScenarioId());

        String finalQuery = null;
        finalQuery = buildQuery(query, obj);
        //////////////////////////////////////

        try {
            pbro = execSelectSQL(finalQuery);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }

        return pbro;
    }

    public void saveScnBudetingDetails(ArrayList parmList) throws Exception {
        String saveScnBudetingDetailsQuery = resBundle.getString("saveScnBudetingDetails");
        String saveScnBudetingDetailsAll = resBundle.getString("saveScnBudetingDetailsAll");
        String saveScnBudetingDetailsQuery1 = resBundle.getString("saveScnBudetingDetails1");
        String[] expectedGrowthArray = (String[]) parmList.get(2);
        String[] forecastFinalGrowthArra = (String[]) parmList.get(3);
        String[] bpnamesArr = (String[]) parmList.get(1);
        Object[] objects = new Object[4];


        String finalQuery = "";
        String deleteQuery = "";
        ArrayList queryList = new ArrayList();

        Connection connection = null;
        try {
            if (parmList.get(7).toString().equalsIgnoreCase("BP")) {
                for (int i = 0; i < expectedGrowthArray.length; i++) {
                    objects[0] = forecastFinalGrowthArra[i];
                    objects[1] = expectedGrowthArray[i];
                    objects[2] = bpnamesArr[i];
                    objects[3] = parmList.get(0);

                    finalQuery = buildQuery(saveScnBudetingDetailsQuery1, objects);
                    queryList.add(finalQuery);
                    if (queryList.size() >= 100) {
                        connection = ProgenConnection.getInstance().getConnection();
                        executeMultiple(queryList, connection);
                        queryList = new ArrayList();
                        connection.close();
                    }
                }

            } else if (parmList.get(7).toString().equalsIgnoreCase("Item")) {
                for (int i = 0; i < expectedGrowthArray.length; i++) {
                    objects[0] = forecastFinalGrowthArra[i];
                    objects[1] = expectedGrowthArray[i];
                    /*
                     * if ( "All".equalsIgnoreCase(bpnamesArr[i]) ) { objects[2]
                     * = parmList.get(0); finalQuery =
                     * buildQuery(saveScnBudetingDetailsAll, objects);
                     * ////.println("All update"); } else
                    {
                     */
                    objects[2] = bpnamesArr[i];
                    objects[3] = parmList.get(0);
                    finalQuery = buildQuery(saveScnBudetingDetailsQuery, objects);
                    //   ////.println(bpnamesArr[i]+"update");
                    // }

                    // finalQuery = buildQuery(saveScnBudetingDetailsQuery, objects);
                    queryList.add(finalQuery);
                    if (queryList.size() >= 100) {
                        connection = ProgenConnection.getInstance().getConnection();
                        executeMultiple(queryList, connection);
                        queryList = new ArrayList();
                        connection.close();
                    }
                }

            }

            if (queryList.size() < 100) {
                connection = ProgenConnection.getInstance().getConnection();

                //
                executeMultiple(queryList, connection);
                queryList = new ArrayList();
                connection.close();
            }
        } catch (Exception e) {

            logger.error("Exception: ", e);

        }

    }
}
