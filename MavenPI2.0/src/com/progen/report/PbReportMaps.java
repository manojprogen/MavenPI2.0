/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import utils.db.ProgenConnection;

/**
 * @filename PbReportMaps
 *
 * @author santhosh.kumar@progenbusiness.com @date Sep 12, 2009, 7:13:05 PM
 */
public abstract class PbReportMaps implements Serializable, Cloneable {

    private static final long serialVersionUID = 752666711556228L;
    public HashMap reportMetadaData;
    public LinkedHashMap<String, ArrayList<String>> reportParameters = new LinkedHashMap<String, ArrayList<String>>();
    public LinkedHashMap reportParametersValues = new LinkedHashMap();
    public HashMap reportIncomingParameters = new HashMap();//From Request or from Trackers/Alert<Key,Value>
    public HashMap[] reportViewBys;
    public HashMap<String, ArrayList<String>> reportViewByMain = new LinkedHashMap<String, ArrayList<String>>();
    public HashMap<String, ArrayList<String>> kpireportViewByMain = new LinkedHashMap<String, ArrayList<String>>();
    public LinkedHashMap<String, ArrayList<String>> reportViewByOrder;
    public HashMap<String, ArrayList<String>> timeDetailsMap;
    public ArrayList timeDetailsArray;
    public ArrayList<String> reportRowViewbys;
    public ArrayList<String> reportColViewbys;
    public ArrayList<String> reportRowViewbyValues = new ArrayList<String>(); 
    public ArrayList<String> reportRowViewbyValuesNames = new ArrayList<String>();  // added by krishan pratap
    public ArrayList<String> reportColViewbyValues = new ArrayList<String>();
    public ArrayList<String> reportQryElementIds = new ArrayList<String>();
    public ArrayList<String> reportQryAggregations;
    public ArrayList<String> reportQryColNames;
    public ArrayList<String> reportQryColTypes = new ArrayList<String>();
    public ArrayList reportParamIds;
    public ArrayList reportParamNames;
    public ArrayList<String> tableElementIds;//only table measure element ids
    public ArrayList tableColNames;//only table measure col disp names
    public ArrayList tableColTypes;//only table measure col types
    public ArrayList tableColDispTypes;//only table measure col disp types
    public ArrayList<Boolean> isFormulaMeasure = new ArrayList<Boolean>();
//    public ArrayList columnSignType;
    public String completeUrl;
    public String reportName;
    public String reportDesc;
    public String ctxPath;
    public String resetPath;
    public boolean isTimeDrill;
    public String[] reportBizRoles = null;
    public String[] ReportAoId = null;
    //########################################### for table level properties
    public boolean showTableTotals = false;
    public boolean showTableSubTotals = false;
    public boolean showTableAvg = false;
    public boolean showTableMax = false;
    public boolean showTableMin = false;
    public boolean showTableCatMax = false;
    public boolean showTableCatMin = false;
    public String tableSymbols = "";
    public String defaultSortedColumn = "";
    public String tableDisplayRows = "";
    public boolean showAdvSearch = false;//added by santhosh.k on 01-03-2010
    public boolean drillAcrossSupported;
    public boolean drillMeasure = false;
    public boolean showTableCatAvg = false;
    //// for column level properties
   /*
     * public ArrayList showColumnTotals; public ArrayList showColumnSubTotals;
     * public ArrayList showColumnAvg; public ArrayList showColumnMax; public
     * ArrayList showColumnMin; public ArrayList showColumnCatMax; public
     * ArrayList showColumnCatMin; public ArrayList showColumnSymbols;
     */
    public boolean avoidProgenTime = false;
    public HashMap columnProperties = new HashMap();
    public boolean isMapEnabled = false;
    //added as part of refactoring and unifying reportQuery code
    public String crosstabGTDisplayPos;
    public String crosstabSTDisplayPos;
    public boolean isTimeSeries = false;
    public boolean isKpi = false;
    public int connectionId = -1;
    public String connectionType = ProgenConnection.ORACLE;
    public String dateFormat;
    //added as part of refactoring and unifying reportQuery code
//    public HashMap ColorCodeMap = new HashMap();//added bby santhosh.k on 26-02-2010
    //public HashMap ParameterGroupingMap=null;//added by santhosh.k on 09-03-2010 for the purpose of Parameter grouping
    public HashMap numberFormat;
    public HashMap<String, Integer> RoundingPrecision;
//    public  ArrayList<String> whatifMeasuresist=new ArrayList<String>();
//    public  ArrayList<Double> whatifSliderValueList = new ArrayList<Double>();
    // public StringBuilder whatIfScenarioXML;
    public StringBuilder tablePropertiesXML;
    //##########################################
    protected HashMap<String, String> drillMap;
    protected HashMap repdrillMap;
    //added by Amar
    public String aoName;
    public String aoDesc;
    //end of code
    public HashMap<String, HashMap<String, List>> operatorFilters = new HashMap<String, HashMap<String, List>>();

    public HashMap getReportMetadaData() {
        return this.reportMetadaData;
    }

    public HashMap getReportParameters() {
        return this.reportParameters;
    }

    public HashMap[] getReportViewBys() {
        return this.reportViewBys;
    }

    public HashMap getReportViewByMain() {
        return this.reportViewByMain;
    }

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

    public HashMap<String, String> getDrillMap() {
        return this.drillMap;
    }

    public void setDrillMap(HashMap<String, String> drillMap) {
        this.drillMap = drillMap;
    }

    /*
     * public ArrayList getShowColumnTotals() { return showColumnTotals; }
     *
     * public void setShowColumnTotals(ArrayList showColumnTotals) {
     * this.showColumnTotals = showColumnTotals; }
     *
     * public ArrayList getShowColumnSubTotals() { return showColumnSubTotals; }
     *
     * public void setShowColumnSubTotals(ArrayList showColumnSubTotals) {
     * this.showColumnSubTotals = showColumnSubTotals; }
     *
     * public ArrayList getShowColumnAvg() { return showColumnAvg; }
     *
     * public void setShowColumnAvg(ArrayList showColumnAvg) {
     * this.showColumnAvg = showColumnAvg; }
     *
     * public ArrayList getShowColumnMax() { return showColumnMax; }
     *
     * public void setShowColumnMax(ArrayList showColumnMax) {
     * this.showColumnMax = showColumnMax; }
     *
     * public ArrayList getShowColumnMin() { return showColumnMin; }
     *
     * public void setShowColumnMin(ArrayList showColumnMin) {
     * this.showColumnMin = showColumnMin; }
     *
     * public ArrayList getShowColumnCatMax() { return showColumnCatMax; }
     *
     * public void setShowColumnCatMax(ArrayList showColumnCatMax) {
     * this.showColumnCatMax = showColumnCatMax; }
     *
     * public ArrayList getShowColumnCatMin() { return showColumnCatMin; }
     *
     * public void setShowColumnCatMin(ArrayList showColumnCatMin) {
     * this.showColumnCatMin = showColumnCatMin; }
     *
     * public ArrayList getShowColumnSymbols() { return showColumnSymbols; }
     *
     * public void setShowColumnSymbols(ArrayList showColumnSymbols) {
     * this.showColumnSymbols = showColumnSymbols; }
     */
}
