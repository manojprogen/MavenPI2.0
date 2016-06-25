/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import prg.db.PbReturnObject;

/**
 *
 * @author progen
 */
public class ImportExcelDetail implements Serializable {

    private static final long serialVersionUID = 95264711556227981L;
    private String reportId;
    private ArrayList<PbReturnObject> returnObject = new ArrayList<PbReturnObject>();
    private ArrayList<String> excelMeasures = new ArrayList<String>();   //excel measures 
    private ArrayList<String> excelMeasureLables = new ArrayList<String>();  // excel display col names
    private ArrayList<String> excelViewbys = new ArrayList<String>(); //excel viewbys
    private HashMap<String, String> excelColumnNames = new HashMap<String, String>();  //maintains original col name with display col name
    private HashMap<String, HashMap<String, String>> finalExcelColNameMap = new HashMap<String, HashMap<String, String>>(); // maintains original col name with entire list of that col
    private HashMap<String, String> excelDimMapping = new HashMap<String, String>(); //Excel colname with mapping dim of reprt
    private HashMap<String, String> repToExcelMapping = new HashMap<String, String>(); //mapping of report rowviewby with to excel colname

    /**
     * @return the returnObject
     */
    public ArrayList<PbReturnObject> getReturnObject() {
        return returnObject;
    }

    /**
     * @param returnObject the returnObject to set
     */
    public void setReturnObject(ArrayList<PbReturnObject> returnObject) {
        this.returnObject = returnObject;
    }

    /**
     * @return the excelMeasures
     */
    public ArrayList<String> getExcelMeasures() {
        return excelMeasures;
    }

    /**
     * @param excelMeasures the excelMeasures to set
     */
    public void setExcelMeasures(ArrayList<String> excelMeasures) {
        this.excelMeasures = excelMeasures;
    }

    /**
     * @return the excelColsMap
     */
    public HashMap<String, String> getExcelColumnNames() {
        return excelColumnNames;
    }

    /**
     * @param excelColsMap the excelColsMap to set
     */
    public void setExcelColumnNames(HashMap<String, String> excelColsMap) {
        this.excelColumnNames = excelColsMap;
    }

    /**
     * @return the ColNameMap
     */
    public HashMap<String, String> getFinalExcelColNameMap(String colName) {
        return finalExcelColNameMap.get(colName);
    }

    /**
     * @param ColNameMap the ColNameMap to set
     */
    public void setFinalExcelColNameMap(String colName, HashMap<String, String> excelColMap) {
        this.finalExcelColNameMap.put(colName, excelColMap);
    }

    /**
     * @return the excelMeasureLables
     */
    public ArrayList<String> getExcelMeasureLables() {
        return excelMeasureLables;
    }

    /**
     * @param excelMeasureLables the excelMeasureLables to set
     */
    public void setExcelMeasureLables(ArrayList<String> excelMeasureLables) {
        this.excelMeasureLables = excelMeasureLables;
    }

    /**
     * @return the excelDimMapping
     */
    public HashMap<String, String> getExcelDimMapping() {
        return excelDimMapping;
    }

    /**
     * @param excelDimMapping the excelDimMapping to set
     */
    public void setExcelDimMapping(HashMap<String, String> excelDimMapping) {
        this.excelDimMapping = excelDimMapping;
    }

    /**
     * @return the excelViewbys
     */
    public ArrayList<String> getExcelViewbys() {
        return excelViewbys;
    }

    /**
     * @param excelViewbys the excelViewbys to set
     */
    public void setExcelViewbys(ArrayList<String> excelViewbys) {
        this.excelViewbys = excelViewbys;
    }

    /**
     * @return the repToExcelMapping
     */
    public HashMap<String, String> getRepToExcelMapping() {
        return repToExcelMapping;
    }

    /**
     * @param repToExcelMapping the repToExcelMapping to set
     */
    public void setRepToExcelMapping(HashMap<String, String> repToExcelMapping) {
        this.repToExcelMapping = repToExcelMapping;
    }
}
