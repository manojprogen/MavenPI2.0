package com.progen.target;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import prg.db.PbDb;

public abstract class PbTargetMaps extends PbDb {

    public HashMap targetMetaData;
    public HashMap targetParameters;
    public HashMap targetIncomingParameters;//From Request or from Trackers/Alert<Key,Value>
    public HashMap[] targetViewBys;
    public LinkedHashMap targetViewByMain;
    public ArrayList targetRowViewbys;
    public ArrayList targetColViewbys;
    public ArrayList targetRowViewbyValues;
    public ArrayList targetColViewbyValues;
    public ArrayList targetQryElementIds;
    public ArrayList targetQryAggregations;
    public ArrayList targetQryColNames;
    public ArrayList tableElementIds;//only table measure element ids
    public ArrayList tableColNames;//only table measure col disp names
    public ArrayList tableColTypes;//only table measure col types
    public ArrayList tableColDispTypes;//only table measure col disp types
    public LinkedHashMap timeDetailsMap;
    public ArrayList timeDetailsArray;
    public HashMap targetParametersValues;
    public String completeUrl;
    public LinkedHashMap targetViewByOrder;
    public String resetPath;
    public ArrayList basisValues;

    public abstract HashMap getTargetMetaData();

    public abstract HashMap getTargetParameters();

    public abstract HashMap[] getTargetViewBys();

    public abstract HashMap getTargetViewByMain();

    /**
     * @return the completeUrl
     */
    public String getCompleteUrl() {
        return completeUrl;
    }

    /**
     * @param completeUrl the completeUrl to set
     */
    public void setCompleteUrl(String completeUrl) {
        this.completeUrl = completeUrl;
    }
}
