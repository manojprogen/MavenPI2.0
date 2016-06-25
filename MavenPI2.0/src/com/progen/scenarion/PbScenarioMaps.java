package com.progen.scenarion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import prg.db.PbDb;

public abstract class PbScenarioMaps extends PbDb {

    public HashMap scenarioMetaData;
    public HashMap scenarioParameters;
    public HashMap scenarioIncomingParameters;
    public HashMap[] scenarioViewBys;
    public LinkedHashMap scenarioViewByMain;
    public ArrayList scenarioRowViewbys;
    public ArrayList scenarioColViewbys;
    public ArrayList scenarioRowViewbyValues;
    public ArrayList scenarioColViewbyValues;
    public ArrayList scenarioQryElementIds;
    public ArrayList scenarioQryAggregations;
    public ArrayList scenarioQryColNames;
    public ArrayList tableElementIds;
    public ArrayList tableColNames;
    public ArrayList tableColTypes;
    public ArrayList tableColDispTypes;
    public LinkedHashMap timeDetailsMap;
    public ArrayList timeDetailsArray;
    public HashMap scenarioParametersValues;
    public String completeUrl;
    public LinkedHashMap scenarioViewByOrder;
    public String resetPath;
    public LinkedHashMap scenarioModels;
    public HashMap scenarioViewByMap;
    public HashMap scenarioSecViewByMap;
    public String ctxPath;
    public LinkedHashMap ParameterNamesHashMap = null;
    public ArrayList scenarioMeasureIds;
    public ArrayList scenarioMeasureNames;
    public LinkedHashMap scenarioTableHashMap = null;

    public abstract HashMap getScenarioMetaData();

    public abstract HashMap getScenarioParameters();

    public abstract HashMap[] getScenarioViewBys();

    public abstract LinkedHashMap getScenarioViewByMain();

    public abstract HashMap getScenarioViewByMap();

    public abstract HashMap getScenarioSecViewByMap();
//    public ArrayList getScenarioRowViewbyValues() {
//        return scenarioRowViewbyValues;
//    }
//
//    public ArrayList getScenarioColViewbyValues() {
//        return scenarioColViewbyValues;
//    }
//
//    public void setScenarioRowViewbyValues(ArrayList scenarioRowViewbyValues) {
//        this.scenarioRowViewbyValues = scenarioRowViewbyValues;
//    }
//
//    public void setScenarioColViewbyValues(ArrayList scenarioColViewbyValues) {
//        this.scenarioColViewbyValues = scenarioColViewbyValues;
//    }
}
