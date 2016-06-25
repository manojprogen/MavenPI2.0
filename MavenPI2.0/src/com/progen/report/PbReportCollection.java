/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report;

import com.google.common.base.Joiner;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.progen.connection.ConnectionDAO;
import com.progen.connection.ConnectionMetadata;
import com.progen.db.ProgenDataSet;
import com.progen.report.drill.DrillMaps;
import com.progen.reportdesigner.db.ReportTemplateDAO;
import com.progen.userlayer.db.LogReadWriter;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import org.apache.log4j.Logger;
import prg.db.Container;
import prg.db.PbDb;
import prg.db.PbReturnObject;
import utils.db.ProgenConnection;
import utils.db.ProgenParam;

/**
 * @filename PbReportCollection
 *
 * @author santhosh.kumar@progenbusiness.com @date Sep 12, 2009, 7:17:07 PM
 */
public class PbReportCollection extends PbReportMaps implements Serializable, Cloneable {

    public static Logger logger = Logger.getLogger(PbReportCollection.class);
    //PbReportCollectionResourceBundle resBundle;// = new PbReportCollectionResourceBundle();
    public String reportId;
    public Boolean isparent;
    public String parentRepId;
//    public String drillUrl = "";
//    public String colDrillUrl = "";
    public String elementId = null;
    public ArrayList paramValueList = new ArrayList();
    public String overrideRowViewByDimId;
    public Map<String, String> dateoptions = new HashMap<String, String>();
    public Map<String, String> dateSubStringValues = new HashMap<String, String>();
    public Map<String, String> indicatorMeasures = new HashMap<String, String>();
    public Map<String, String> scriptIndicators = new HashMap<String, String>();
    public Map<String, String> timeConversion = new HashMap<String, String>();
    private HashMap nonViewByMap = new HashMap();
    private List<String> customSequence = new ArrayList<String>();
    private HashMap<String, ArrayList<String>> transposeFormatMap = new HashMap<String, ArrayList<String>>();
    private HashMap<String, ArrayList<String>> goalseek = new HashMap<String, ArrayList<String>>();
    private HashMap<String, ArrayList<String>> percentColValues = new HashMap<String, ArrayList<String>>();
    private HashMap<String, HashMap<String, ArrayList<String>>> goalSeekBasicandAdhoc = new HashMap<String, HashMap<String, ArrayList<String>>>();
    private HashMap<String, ArrayList<String>> goalandPercent = new HashMap<String, ArrayList<String>>();
    private List<BigDecimal> minmaxavgValues = new ArrayList<BigDecimal>();
    private List<String> viewByValues = new ArrayList<String>();
    private String groupName;
    private String elemntId;
    private List<String> globalValues = new ArrayList<String>();
    private HashMap<String, ArrayList<String>> timeBaseInvidualper = new HashMap<String, ArrayList<String>>();
    private HashMap<String, ArrayList<String>> measurValues = new HashMap<String, ArrayList<String>>();
    private HashMap<String, HashMap<String, ArrayList<String>>> goalSeekTimeIndvidual = new HashMap<String, HashMap<String, ArrayList<String>>>();
    private boolean design;
    private List<String> prodAndViewName = new ArrayList<String>();
    public Map<String, String> scriptAligns = new HashMap<String, String>();
    public Map<String, String> measureTypes = new HashMap<String, String>();
    public Map<String, String> measureAligns = new HashMap<String, String>();
    public HashMap timememdetails = new HashMap();
    String testForPrev = "";
    public HashMap<String, ArrayList<String>> advanceParam = new HashMap<String, ArrayList<String>>();
    public String overrideViewBys1;
    //boolean overrideViewBys1 = true;
    public HashMap resetParamHashmap = new HashMap();
    public HashMap<String, ArrayList> nonViewByMapNew = new HashMap<String, ArrayList>();
    public ArrayList finalcrosstaborder = new ArrayList();
    public Map< String, String[]> crosstabelements = new HashMap<String, String[]>();
    public HashMap<String, String[]> crosstabhashmap = new HashMap<String, String[]>();
    private boolean isOLAPGraph;
    private LinkedList drillValueList = new LinkedList();
    private HashMap<String, LinkedList> drillValuesMap = new HashMap<String, LinkedList>();
    public Map<String, String> dateFormatt = new HashMap<String, String>();
    public HashMap<String, String> reportDrillMap = new HashMap<String, String>();
    public HashMap<String, ArrayList<String>> summerizedTableHashMap = new HashMap<String, ArrayList<String>>();
    public String msrDrillReportSelection = null;
    private HashMap aggrigationMap = new HashMap();
    private boolean checkOverWrite;
    private double reportVersion;
    private double currentRepVersion;
    public HashMap<String, String> crosstabmeasureHashMap = new HashMap<String, String>();
//      private boolean lockDataset = false;
    public HashMap dependentviewbyIdQry = new HashMap();//for locking the dataset
    public HashMap<String, String> lockdatasetmap = new HashMap();
    private String repQry = null;
    public Boolean lockDrill = false;
    public String fromlockedrep = null;
    public String lockedElem = null;
    private String reportAdvHtmlFileProps;
    private HashMap<String, String> refreshQryMap = new HashMap<String, String>();
    private HashMap<String, ProgenDataSet> refreshReturnObjectMap = new HashMap<String, ProgenDataSet>();
    private HashMap<String, String> crosstabMsrMap = new HashMap<String, String>();
    private static final long serialVersionUID = 75264711556568L;
    private ArrayList<String> hideMeasures = new ArrayList<String>();
    public boolean isTopBottomTableEnable;
    public HashMap<String, String> TopBottomTableHashMap = new HashMap<String, String>();
    public Map<String, String> subTotalDeviation = new HashMap<String, String>();
    public Map<String, String> gtCTAvgType = new HashMap<String, String>();
    private ArrayList<String> hideViewbys = new ArrayList<String>();
    private boolean isExcelimportEnable;
    private boolean isglobalparamkpi;
    public String importExcelFilePath;
    private LogReadWriter log4coll = null;
    private boolean logFlag = false;
    public String timePeriodsList;
    public String Qtrtype;
    public String ReportLayout = "";//added by mohit for kpi and none
    public HashMap<String, String> crosscolmap1 = new HashMap<String, String>();
    public HashMap collectMulticalender = new HashMap();//added by sruthi for multicalendar
    public HashMap<String, String> reportDrillMaptooltip = new HashMap<String, String>();   // added by krishan pratap
    public String AOId = "";
    public HashMap<String, ArrayList<String>> initializeDefaultFilter = new HashMap<String, ArrayList<String>>();

    public HashMap<String, ArrayList<String>> getAdvanceParam() {
        return advanceParam;
    }

    public void setAdvanceParam(HashMap<String, ArrayList<String>> advanceParam) {
        this.advanceParam = advanceParam;
    }

    //public Document document = null;
    // public Element root = null;
    private ResourceBundle getResourceBundle() {
        ResourceBundle resBndle = null;
        if (resBndle == null) {
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                resBndle = new PbReportCollectionResBunSqlServer();
            } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                resBndle = new PbReportCollectionResourceBundleMySql();
            } else {
                resBndle = new PbReportCollectionResourceBundle();
            }
        }
        return resBndle;
    }

    public HashMap getNumberFormat() {
        return this.numberFormat;
    }

    public HashMap getRoundingPrecision() {
        return this.RoundingPrecision;
    }

    public boolean isRowViewBy(String viewId) {
        ArrayList<String> viewByDtls = reportViewByOrder.get(viewId);
        if ("Row View By".equalsIgnoreCase(viewByDtls.get(0))) {
            return true;
        } else {
            return false;
        }

    }

    public void updateCollection(boolean overrideViewBys) throws ParseException {
        isTimeDrill = false;
        String paramExcludedIncluded = "NOT_SELECTED";
        this.setCompleteUrl("");
        boolean excludedParam = false;
        completeUrl = "";
        //This will get parameters hashMap if Report Id is provided

        Set paramEleIds = reportParameters.keySet();
        Iterator paramEleIter = paramEleIds.iterator();
        String paramElement;
        List<String> parameterValue;
        ArrayList paramInfo;
        paramValueList.clear();
        Gson gson = new Gson();
        Type tarType = new TypeToken<List<String>>() {
        }.getType();
        while (paramEleIter.hasNext()) {
            excludedParam = false;
            paramElement = (String) paramEleIter.next();
            paramInfo = (ArrayList) reportParameters.get(paramElement);
            parameterValue = (List<String>) reportIncomingParameters.get("CBOARP" + paramElement);
            if (parameterValue == null) {
                if (paramInfo.get(8) != null && !paramInfo.get(8).equals("")) {
                    parameterValue = (List<String>) paramInfo.get(8);
                }
            }
            if (reportIncomingParameters.get("AdhocDrill" + paramElement) != null
                    && !"".equals(((String) reportIncomingParameters.get("AdhocDrill" + paramElement)).trim()) && "Y".equals(((String) reportIncomingParameters.get("AdhocDrill" + paramElement)).trim())) {
                excludedParam = false;
                paramInfo.set(10, "INCLUDED");
                // 

            } else if (reportIncomingParameters.get("CBOARP" + paramElement + "_excbox") != null
                    && !"".equals(((String) reportIncomingParameters.get("CBOARP" + paramElement + "_excbox")).trim())) {
                excludedParam = true;
            }

            if (paramInfo.get(10).equals("NOT_SELECTED")) {
                if (excludedParam) {
                    paramExcludedIncluded = "EXCLUDED";
                } else if ((paramInfo.get(8).toString()).equalsIgnoreCase(parameterValue.toString())) {
                    paramExcludedIncluded = "NOT_SELECTED";
                } else {
                    paramExcludedIncluded = "INCLUDED";
                }
            } else {
                paramExcludedIncluded = paramInfo.get(10).toString();
            }

//added by sruthi for filters apostafy symbol
            for (int i = 0; i < parameterValue.size(); i++) {
                String datavalue = parameterValue.get(i).toString();
                if (datavalue.contains("'")) {
                    int index = parameterValue.indexOf(datavalue);
                    parameterValue.set(index, datavalue.replace("'", "''"));
                }
            }
            for (int i = 0; i < parameterValue.size(); i++) {
                String datavalue1 = parameterValue.get(i).toString();
                if (datavalue1.contains("u0026")) {
                    int index1 = parameterValue.indexOf(datavalue1);
                    parameterValue.set(index1, datavalue1.replace("u0026", "&"));
                }
            }
            //ended by sruthi

//Added By Ram
            String viewByEleIds = paramEleIds.toString().substring(1, paramEleIds.toString().length() - 1);
            ReportTemplateDAO rto = new ReportTemplateDAO();
            PbReturnObject retObject = null;
            retObject = rto.getLookupData(viewByEleIds);
            HashMap<String, String> lookup = new HashMap<String, String>();
            if (retObject != null) {
                for (int i = 0; i < retObject.getRowCount(); i++) {
                    lookup.put(retObject.getFieldValueString(i, 1), retObject.getFieldValueString(i, 0));
                }
            }
            if (!parameterValue.get(0).equalsIgnoreCase("All") && !parameterValue.isEmpty()) {
                for (int kk = 0; kk < parameterValue.size(); kk++) {
                    if (lookup.containsKey(parameterValue.get(kk))) {
                        parameterValue.set(kk, lookup.get(parameterValue.get(kk)));
                    }
                }
            }
//Ended By Ram
            paramInfo.set(8, parameterValue);
            paramValueList.add(paramInfo.get(1) + ":" + parameterValue);
            paramInfo.set(9, "CBOARP" + paramElement);
            paramInfo.set(10, paramExcludedIncluded);
            reportParameters.put(paramElement, paramInfo);
            reportParametersValues.put(paramElement, paramInfo.get(8));
            this.completeUrl += ";" + String.valueOf(paramInfo.get(9)) + "=" + gson.toJson(paramInfo.get(8), tarType); //String.valueOf(paramInfo.get(8)
            HashMap inMap = operatorFilters.get("IN");
            if (inMap != null) {
                if (reportIncomingParameters.get("reportDrill") != null && reportIncomingParameters.get("reportDrill").toString().equalsIgnoreCase("Y")) {
                    List<String> savefilter = (List<String>) inMap.get(paramElement);
                    if (savefilter.size() >= 1 && !savefilter.get(0).toString().equalsIgnoreCase("All")) {

                        if (!parameterValue.get(0).toString().equalsIgnoreCase("All")) {
                            for (int l = 0; l < parameterValue.size(); l++) {
                                if (savefilter.contains(parameterValue.get(l))) {
                                } else {
                                    savefilter.add(parameterValue.get(l));
                                }
                            }
                            parameterValue = savefilter;
                        } else {
                            parameterValue = savefilter;
                        }

                    }
                }
                inMap.put(paramElement, parameterValue);
                operatorFilters.put("IN", inMap);
            } else {
                inMap = new HashMap();
                inMap.put(paramElement, parameterValue);
                operatorFilters.put("IN", inMap);
            }

        }

        ArrayList<String> rowviewbys = new ArrayList<String>();
        String nextViewBy = "";

        if (overrideViewBys) {
            boolean viewbysAvailable = false;

            Set<String> repViewByIds = reportViewByMain.keySet();
            String viewByElementId;
            boolean colDrillSet = false;

            for (String viewById : repViewByIds) {
                if (reportIncomingParameters.get("CBOVIEW_BY" + viewById) != null) {
                    rowviewbys.add((String) reportIncomingParameters.get("CBOVIEW_BY" + viewById));
                    viewbysAvailable = true;
                }
            }

            if (viewbysAvailable) {
                reportRowViewbyValues.clear();

                if (reportColViewbys != null) {
                    reportColViewbys.clear();
                }
                reportColViewbyValues.clear();
                isTimeDrill = false;

                for (String viewById : repViewByIds) {
                    viewByElementId = (String) reportIncomingParameters.get("CBOVIEW_BY" + viewById);
                    if (reportIncomingParameters.get("CBOVIEW_BY" + viewById) != null) {
                        if (isRowViewBy(viewById)) {
                            reportRowViewbyValues.add((String) reportIncomingParameters.get("CBOVIEW_BY" + viewById));
                        } else {

                            reportColViewbyValues.add((String) reportIncomingParameters.get("CBOVIEW_BY" + viewById));
                            reportColViewbys.add("A_" + reportIncomingParameters.get("CBOVIEW_BY" + viewById));
                        }
                        ArrayList<String> viewBysList = reportViewByMain.get(viewById);
                        viewBysList.set(2, viewByElementId);

                        this.completeUrl += ";" + "CBOVIEW_BY" + viewById + "=" + viewByElementId;

                    }
                }
            }
        }
        ArrayList<String> timeInfo;

        Set<String> timeKeySet = timeDetailsMap.keySet();

//        
        for (String timeType : timeKeySet) {
            timeInfo = timeDetailsMap.get(timeType);
            if (timeType.equalsIgnoreCase("AS_OF_DATE") && (reportIncomingParameters.get("DDrill") != null && reportIncomingParameters.get("DDrill").toString().equalsIgnoreCase("Y"))) {
                if ((reportIncomingParameters.get("DrillYear") != null)) {
                    isTimeDrill = true;
                    timeInfo.set(0, getYearDrill(reportIncomingParameters.get("DrillYear").toString()));
                } else if ((reportIncomingParameters.get("DrillQtr") != null)) {
                    isTimeDrill = true;
                    timeInfo.set(0, getQtrDrill(reportIncomingParameters.get("DrillQtr").toString()));
                } else if ((reportIncomingParameters.get("DrillMonth") != null)) {
                    isTimeDrill = true;
                    timeInfo.set(0, getMonthDrill(reportIncomingParameters.get("DrillMonth").toString()));
                } else if ((reportIncomingParameters.get("DrillWeek") != null)) {
                    isTimeDrill = true;
                    timeInfo.set(0, getWeekDrill(reportIncomingParameters.get("DrillWeek").toString()));
                } else if ((reportIncomingParameters.get("DrillDate") != null)) {
                    isTimeDrill = true;
                    timeInfo.set(0, reportIncomingParameters.get("DrillDate").toString().replace("-", "/"));
                } else {
                    if (reportIncomingParameters.get(("CBO_" + timeType)) != null) {
                        timeInfo.set(0, (String) reportIncomingParameters.get(("CBO_" + timeType)));
                    }
                }
            } else {
                // 
                if (reportIncomingParameters.get("DDrillAcross") != null && reportIncomingParameters.get("DDrillAcross").toString().equalsIgnoreCase("Y")) {
                } else {
                    if (reportIncomingParameters.get(("CBO_" + timeType)) != null && (reportIncomingParameters.get("isToggle") == null || ((String) reportIncomingParameters.get("isToggle")).isEmpty())) {
                        timeInfo.set(0, (String) reportIncomingParameters.get(("CBO_" + timeType)));
                    }
                }
            }
        }

        String value = "";
        String valu = "";
        String mont = "";
        String CurrValue = "";
//                

        if (dateFormat == null) {

            if (reportIncomingParameters.get("CBO_AS_OF_DATE") != null && reportIncomingParameters.get("CBO_AS_OF_DATE") != "" && !isTimeDrill && (reportIncomingParameters.get("isToggle") == null || ((String) reportIncomingParameters.get("isToggle")).isEmpty())) {
                value = (String) reportIncomingParameters.get("CBO_AS_OF_DATE");
                if (value != null) {
                    valu = value.substring(0, 2);
                    mont = value.substring(3, 5);
                    CurrValue = mont.concat("/").concat(valu).concat(value.substring(5));
                    if (timeDetailsArray.get(1).toString().equalsIgnoreCase("PRG_STD") && !CurrValue.isEmpty()) {
                        timeDetailsMap.get("AS_OF_DATE").set(0, CurrValue);
                    }
                }
            }
            //periodate
            if (reportIncomingParameters.get("perioddate") != null && reportIncomingParameters.get("datetext") != "") {
                value = (String) reportIncomingParameters.get("perioddate");
                if (!value.contains("/") && !value.isEmpty()) {
                    String formatdate = parseDate(value);
                    valu = formatdate.substring(0, 2);
                    mont = formatdate.substring(3, 5);
                    CurrValue = mont.concat("/").concat(valu).concat(formatdate.substring(5));

                }
                if (timeDetailsArray.get(1).toString().equalsIgnoreCase("PRG_STD") && !CurrValue.isEmpty()) {

                    timeDetailsMap.get("AS_OF_DATE").set(0, CurrValue);
                }
            }
            ////
            if (timeDetailsMap.get("AS_OF_DATE1") != null && (reportIncomingParameters.get("isToggle") == null || ((String) reportIncomingParameters.get("isToggle")).isEmpty())) {
                value = (String) reportIncomingParameters.get("CBO_AS_OF_DATE1");
                if (value != null) {
                    valu = value.substring(0, 2);
                    mont = value.substring(3, 5);
                    CurrValue = mont.concat("/").concat(valu).concat(value.substring(5));
                    timeDetailsMap.get("AS_OF_DATE1").set(0, CurrValue);
                }
            }
            //fromdate
            if (reportIncomingParameters.get("fromdate") != null && reportIncomingParameters.get("datetext") != "") {
                value = (String) reportIncomingParameters.get("fromdate");
                if (!value.contains("/") && !value.isEmpty()) {
                    String formatdate = parseDate(value);
                    valu = formatdate.substring(0, 2);
                    mont = formatdate.substring(3, 5);
                    CurrValue = mont.concat("/").concat(valu).concat(formatdate.substring(5));


                    if (timeDetailsMap.get("AS_OF_DATE1") != null && !CurrValue.isEmpty()) {
                        timeDetailsMap.get("AS_OF_DATE1").set(0, CurrValue);
                    }
                }
            }
            //
            if (timeDetailsMap.containsKey("AS_OF_DATE2") && reportIncomingParameters.get("CBO_AS_OF_DATE2") != null && reportIncomingParameters.get("CBO_AS_OF_DATE2") != "" && (reportIncomingParameters.get("isToggle") == null || ((String) reportIncomingParameters.get("isToggle")).isEmpty())) {
                value = (String) reportIncomingParameters.get("CBO_AS_OF_DATE2");
                if (value != null) {
                    valu = value.substring(0, 2);
                    mont = value.substring(3, 5);
                    CurrValue = mont.concat("/").concat(valu).concat(value.substring(5));
                    timeDetailsMap.get("AS_OF_DATE2").set(0, CurrValue);
                }
            }
            //todate
            if (reportIncomingParameters.get("todate") != null && reportIncomingParameters.get("datetext") != "") {
                value = (String) reportIncomingParameters.get("todate");
                if (!value.contains("/") && !value.isEmpty()) {
                    String formatdate = parseDate(value);
                    valu = formatdate.substring(0, 2);
                    mont = formatdate.substring(3, 5);
                    CurrValue = mont.concat("/").concat(valu).concat(formatdate.substring(5));
                    if (timeDetailsMap.get("AS_OF_DATE2") != null && !CurrValue.isEmpty()) {
                        timeDetailsMap.get("AS_OF_DATE2").set(0, CurrValue);
                    }
                }
            }
            ///
            if (timeDetailsMap.containsKey("CMP_AS_OF_DATE1") && reportIncomingParameters.get("CBO_CMP_AS_OF_DATE1") != null && reportIncomingParameters.get("CBO_CMP_AS_OF_DATE1") != "" && (reportIncomingParameters.get("isToggle") == null || ((String) reportIncomingParameters.get("isToggle")).isEmpty())) {
                value = (String) reportIncomingParameters.get("CBO_CMP_AS_OF_DATE1");
                if (value != null) {
                    valu = value.substring(0, 2);
                    mont = value.substring(3, 5);
                    CurrValue = mont.concat("/").concat(valu).concat(value.substring(5));
                    if (timeDetailsMap.get("CMP_AS_OF_DATE1") != null) {
                        timeDetailsMap.get("CMP_AS_OF_DATE1").set(0, CurrValue);
                    }
                }
            }
            //comparefrom
            if (reportIncomingParameters.get("comparefrom") != null && reportIncomingParameters.get("datetext") != "") {
                value = (String) reportIncomingParameters.get("comparefrom");
                if (!value.contains("/") && !value.isEmpty()) {
                    String formatdate = parseDate(value);
                    valu = formatdate.substring(0, 2);
                    mont = formatdate.substring(3, 5);
                    CurrValue = mont.concat("/").concat(valu).concat(formatdate.substring(5));


                    if (timeDetailsMap.get("CMP_AS_OF_DATE1") != null && !CurrValue.isEmpty()) {
                        timeDetailsMap.get("CMP_AS_OF_DATE1").set(0, CurrValue);
                    }
                }
            }
            //
            if (timeDetailsMap.containsKey("CMP_AS_OF_DATE2") && reportIncomingParameters.get("CBO_CMP_AS_OF_DATE2") != null && reportIncomingParameters.get("CBO_CMP_AS_OF_DATE2") != "" && (reportIncomingParameters.get("isToggle") == null || ((String) reportIncomingParameters.get("isToggle")).isEmpty())) {
                value = (String) reportIncomingParameters.get("CBO_CMP_AS_OF_DATE2");
                if (value != null) {
                    valu = value.substring(0, 2);
                    mont = value.substring(3, 5);
                    CurrValue = mont.concat("/").concat(valu).concat(value.substring(5));
                    if (timeDetailsMap.get("CMP_AS_OF_DATE2") != null && !CurrValue.isEmpty()) {
                        timeDetailsMap.get("CMP_AS_OF_DATE2").set(0, CurrValue);
                    }
                }
            }

            //compareto
            if (timeDetailsMap.containsKey("CMP_AS_OF_DATE2") && reportIncomingParameters.get("compareto") != null && reportIncomingParameters.get("datetext") != "") {
                value = (String) reportIncomingParameters.get("compareto");
                if (!value.contains("/") && !value.isEmpty()) {
                    String formatdate = parseDate(value);
                    valu = formatdate.substring(0, 2);
                    mont = formatdate.substring(3, 5);
                    CurrValue = mont.concat("/").concat(valu).concat(formatdate.substring(5));


                    if (timeDetailsMap.get("CMP_AS_OF_DATE2") != null && !CurrValue.isEmpty()) {
                        timeDetailsMap.get("CMP_AS_OF_DATE2").set(0, CurrValue);
                    }
                }
            }

            ////
        } else if (dateFormat.equalsIgnoreCase("dd/mm/yy")) {
            if (reportIncomingParameters.get("CBO_AS_OF_DATE") != null) {
                value = (String) reportIncomingParameters.get("CBO_AS_OF_DATE");
                valu = value.substring(0, 2);
                mont = value.substring(3, 5);
                CurrValue = mont.concat("/").concat(valu).concat(value.substring(5));
                timeDetailsMap.get("AS_OF_DATE").set(0, CurrValue);
            }
            if (timeDetailsMap.get("AS_OF_DATE1") != null) {
                value = (String) reportIncomingParameters.get("CBO_AS_OF_DATE1");
                valu = value.substring(0, 2);
                mont = value.substring(3, 5);
                CurrValue = mont.concat("/").concat(valu).concat(value.substring(5));
                timeDetailsMap.get("AS_OF_DATE1").set(0, CurrValue);
            }
            if (reportIncomingParameters.get("CBO_AS_OF_DATE2") != null) {
                value = (String) reportIncomingParameters.get("CBO_AS_OF_DATE2");
                valu = value.substring(0, 2);
                mont = value.substring(3, 5);
                CurrValue = mont.concat("/").concat(valu).concat(value.substring(5));
                timeDetailsMap.get("AS_OF_DATE2").set(0, CurrValue);
            }
            if (reportIncomingParameters.get("CBO_CMP_AS_OF_DATE1") != null) {
                value = (String) reportIncomingParameters.get("CBO_CMP_AS_OF_DATE1");
                valu = value.substring(0, 2);
                mont = value.substring(3, 5);
                CurrValue = mont.concat("/").concat(valu).concat(value.substring(5));
                timeDetailsMap.get("CMP_AS_OF_DATE1").set(0, CurrValue);
            }
            if (reportIncomingParameters.get("CBO_CMP_AS_OF_DATE2") != null) {
                value = (String) reportIncomingParameters.get("CBO_CMP_AS_OF_DATE2");
                valu = value.substring(0, 2);
                mont = value.substring(3, 5);
                CurrValue = mont.concat("/").concat(valu).concat(value.substring(5));
                timeDetailsMap.get("CMP_AS_OF_DATE2").set(0, CurrValue);
            }
        }


        if (!timeDetailsMap.isEmpty()) {
            if (timeDetailsArray.get(0).toString().equalsIgnoreCase("Year") && timeDetailsArray.get(1).toString().equalsIgnoreCase("PRG_YEAR_CMP")) {
                timeInfo = timeDetailsMap.get("AS_OF_YEAR");
                timeDetailsArray.set(2, timeInfo.get(0));
                timeInfo = timeDetailsMap.get("AS_OF_YEAR1");
                if (timeInfo != null) {
                    timeDetailsArray.set(3, timeInfo.get(0));
                } else {
                    timeDetailsArray.set(3, null);
                }

            } else if (timeDetailsArray.get(0).toString().equalsIgnoreCase("DAY") && timeDetailsArray.get(1).toString().equalsIgnoreCase("PRG_COHORT")) {
                timeInfo = timeDetailsMap.get("AS_OF_DATE1");
                timeDetailsArray.set(2, timeInfo.get(0));

                timeInfo = timeDetailsMap.get("AS_OF_DATE2");
                timeDetailsArray.set(3, timeInfo.get(0));
                if (timeDetailsMap.get("CMP_AS_OF_DATE1") != null) {
                    timeInfo = timeDetailsMap.get("CMP_AS_OF_DATE1");
                    timeDetailsArray.set(4, timeInfo.get(0));
                } else {
                    timeDetailsArray.set(4, null);
                }
                if (timeDetailsMap.get("CMP_AS_OF_DATE2") != null) {
                    timeInfo = timeDetailsMap.get("CMP_AS_OF_DATE2");
                    timeDetailsArray.set(5, timeInfo.get(0));

                } else {
                    timeDetailsArray.set(5, null);
                }
                if (timeDetailsMap.get("PRG_PERIOD_TYPE") != null) {
                    timeInfo = timeDetailsMap.get("PRG_PERIOD_TYPE");
                    timeDetailsArray.set(6, timeInfo.get(0));
                } else {
                    timeDetailsArray.set(6, "PERIOD");
                }
            } else if (timeDetailsArray.get(0).toString().equalsIgnoreCase("DAY") && timeDetailsArray.get(1).toString().equalsIgnoreCase("PRG_DATE_RANGE")) {
                timeInfo = timeDetailsMap.get("AS_OF_DATE1");
                timeDetailsArray.set(2, timeInfo.get(0));

                timeInfo = timeDetailsMap.get("AS_OF_DATE2");
                timeDetailsArray.set(3, timeInfo.get(0));
                if (timeDetailsMap.get("CMP_AS_OF_DATE1") != null) {
                    timeInfo = timeDetailsMap.get("CMP_AS_OF_DATE1");
                    timeDetailsArray.set(4, timeInfo.get(0));
                } else {
                    timeDetailsArray.set(4, null);
                }
                if (timeDetailsMap.get("CMP_AS_OF_DATE2") != null) {
                    timeInfo = timeDetailsMap.get("CMP_AS_OF_DATE2");
                    timeDetailsArray.set(5, timeInfo.get(0));

                } else {
                    timeDetailsArray.set(5, null);
                }
            } else if (timeDetailsArray.get(0).toString().equalsIgnoreCase("DAY") && timeDetailsArray.get(1).toString().equalsIgnoreCase("PRG_MONTH_RANGE")) {
                timeInfo = timeDetailsMap.get("AS_OF_DMONTH1");
                timeDetailsArray.set(2, timeInfo.get(0));
                timeInfo = timeDetailsMap.get("AS_OF_DMONTH2");
                timeDetailsArray.set(3, timeInfo.get(0));
                if (timeDetailsMap.get("CMP_AS_OF_DMONTH1") != null) {
                    timeInfo = timeDetailsMap.get("CMP_AS_OF_DMONTH1");
                    timeDetailsArray.set(4, timeInfo.get(0));
                } else {
                    timeDetailsArray.set(4, null);
                }
                if (timeDetailsMap.get("CMP_AS_OF_DMONTH2") != null) {
                    timeInfo = timeDetailsMap.get("CMP_AS_OF_DMONTH2");
                    timeDetailsArray.set(5, timeInfo.get(0));

                } else {
                    timeDetailsArray.set(5, null);
                }

            } else if (timeDetailsArray.get(0).toString().equalsIgnoreCase("DAY") && timeDetailsArray.get(1).toString().equalsIgnoreCase("PRG_QUARTER_RANGE")) {
                timeInfo = timeDetailsMap.get("AS_OF_DQUARTER1");
                timeDetailsArray.set(2, timeInfo.get(0));
                timeInfo = timeDetailsMap.get("AS_OF_DQUARTER2");
                timeDetailsArray.set(3, timeInfo.get(0));
                if (timeDetailsMap.get("CMP_AS_OF_DQUARTER1") != null) {
                    timeInfo = timeDetailsMap.get("CMP_AS_OF_DQUARTER1");
                    timeDetailsArray.set(4, timeInfo.get(0));
                } else {
                    timeDetailsArray.set(4, null);
                }
                if (timeDetailsMap.get("CMP_AS_OF_DQUARTER2") != null) {
                    timeInfo = timeDetailsMap.get("CMP_AS_OF_DQUARTER2");
                    timeDetailsArray.set(5, timeInfo.get(0));
                } else {
                    timeDetailsArray.set(5, null);
                }

            } else if (timeDetailsArray.get(0).toString().equalsIgnoreCase("DAY") && timeDetailsArray.get(1).toString().equalsIgnoreCase("PRG_YEAR_RANGE")) {
                timeInfo = timeDetailsMap.get("AS_OF_DYEAR1");
                timeDetailsArray.set(2, timeInfo.get(0));
                timeInfo = timeDetailsMap.get("AS_OF_DYEAR2");
                timeDetailsArray.set(3, timeInfo.get(0));
                if (timeDetailsMap.get("CMP_AS_OF_DYEAR1") != null) {
                    timeInfo = timeDetailsMap.get("CMP_AS_OF_DYEAR1");
                    timeDetailsArray.set(4, timeInfo.get(0));
                } else {
                    timeDetailsArray.set(4, null);
                }
                if (timeDetailsMap.get("CMP_AS_OF_DYEAR2") != null) {
                    timeInfo = (ArrayList) timeDetailsMap.get("CMP_AS_OF_DYEAR2");
                    timeDetailsArray.set(5, timeInfo.get(0));

                } else {
                    timeDetailsArray.set(5, null);
                }

            } else if (timeDetailsArray.get(0).toString().equalsIgnoreCase("DAY") && timeDetailsArray.get(1).toString().equalsIgnoreCase("PRG_DAY_ROLLING")) {
                timeInfo = timeDetailsMap.get("AS_OF_DATE");
                timeDetailsArray.set(2, timeInfo.get(0));

                timeInfo = timeDetailsMap.get("PRG_DAY_ROLLING");
                timeDetailsArray.set(3, timeInfo.get(0));


            } else if (timeDetailsArray.get(0).toString().equalsIgnoreCase("DAY") && timeDetailsArray.get(1).toString().equalsIgnoreCase("PRG_STD")) {
                timeInfo = timeDetailsMap.get("AS_OF_DATE");
                timeDetailsArray.set(2, timeInfo.get(0));

                timeInfo = timeDetailsMap.get("PRG_PERIOD_TYPE");

                timeDetailsArray.set(3, timeInfo.get(0));
                if (reportIncomingParameters.get("reportDrill") != null && reportIncomingParameters.get("reportDrill").toString().equalsIgnoreCase("Y") && reportIncomingParameters.get("CBO_PRG_PERIOD_TYPE") != null) {
                    timeDetailsArray.set(3, (String) reportIncomingParameters.get(("CBO_PRG_PERIOD_TYPE")));
                }
                if (timeDetailsMap.get("PRG_COMPARE") != null) {
                    timeInfo = timeDetailsMap.get("PRG_COMPARE");
                    timeDetailsArray.set(4, timeInfo.get(0));
                    if (reportIncomingParameters.get("gCompq") != null) {
                        timeDetailsArray.set(4, (String) reportIncomingParameters.get("gCompq"));
                    }
                } else {
                    timeDetailsArray.set(4, "Last Period");
                }
            } else if (timeDetailsArray.get(0).toString().equalsIgnoreCase("Month") && timeDetailsArray.get(1).toString().equalsIgnoreCase("PRG_MONTH_CMP")) {
                timeInfo = timeDetailsMap.get("AS_OF_MONTH");
                timeDetailsArray.set(2, timeInfo.get(0));

                timeInfo = timeDetailsMap.get("AS_OF_MONTH1");
                timeDetailsArray.set(3, timeInfo.get(0));

            } else if (timeDetailsArray.get(0).toString().equalsIgnoreCase("MONTH") && timeDetailsArray.get(1).toString().equalsIgnoreCase("PRG_STD")) {
                timeInfo = timeDetailsMap.get("AS_OF_MONTH");
                timeDetailsArray.set(2, timeInfo.get(0));

                timeInfo = timeDetailsMap.get("PRG_PERIOD_TYPE");
                timeDetailsArray.set(3, timeInfo.get(0));

                if (timeDetailsMap.get("PRG_COMPARE") != null) {
                    timeInfo = timeDetailsMap.get("PRG_COMPARE");
                    timeDetailsArray.set(4, timeInfo.get(0));
                } else {
                    timeDetailsArray.set(4, "Last Period");
                }
            }
        }


        this.resetPath = ctxPath + "/reportViewer.do?reportBy=viewReport&REPORTID=" + this.reportId + "&action=reset";
        this.completeUrl = ctxPath + "/reportViewer.do?reportBy=viewReport;REPORTID=" + this.reportId + this.completeUrl;
//        
    }

    public void getParamMetaData(boolean isOpenEvent) throws Exception {
        HashMap defaultFilterMap = initializeDefaultFilter;
        PbReturnObject retObj = null;
        Object[] Obj = null;
        String[] colNames;
        String temp;
        String finalQuery = "";
        isTimeDrill = false;
        PbDb db = new PbDb();
        //reportParameters = new LinkedHashMap();
        reportParametersValues = new LinkedHashMap<String, String>();
        reportParamIds = new ArrayList();
        reportParamNames = new ArrayList();
        String paramExcludedIncluded = "NOT_SELECTED";
        this.setCompleteUrl("");
        //This will get parameters hashMap if Report Id is provided
        String sqlstr = "";
        try {
            sqlstr = getResourceBundle().getString("getParamMetaDataQuery1");//modified getParamMetaDataQuery1 by sanhtosh.k on 09-03-2010 for the purpose of building Parameter grouping
            Obj = new Object[1];
            Obj[0] = this.reportId;
            finalQuery = db.buildQuery(sqlstr, Obj);
            retObj = db.execSelectSQL(finalQuery);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        colNames = retObj.getColumnNames();
        int psize = retObj.getRowCount();
        if (psize > 0) {
            this.elementId = retObj.getFieldValueString(0, colNames[0]);
            /*
             * added by srikanth.P for multipe filters with any symbol:start
             */
            Gson gson = new Gson();
            Type typeList = new TypeToken<List<String>>() {
            }.getType();

            /*
             * End
             */
            for (int looper = 0; looper < psize; looper++) {

                paramExcludedIncluded = retObj.getFieldValueString(looper, colNames[9]);
                ArrayList paramInfo = new ArrayList();
                paramInfo.add(retObj.getFieldValueString(looper, colNames[0]));
                paramInfo.add(retObj.getFieldValueString(looper, colNames[1]));
                paramInfo.add(retObj.getFieldValueString(looper, colNames[2]));
                paramInfo.add(retObj.getFieldValueString(looper, colNames[3]));
                paramInfo.add(retObj.getFieldValueString(looper, colNames[4]));
                paramInfo.add(retObj.getFieldValueString(looper, colNames[5]));
                paramInfo.add(retObj.getFieldValueString(looper, colNames[6]));
                paramInfo.add(retObj.getFieldValueString(looper, colNames[7]));


                List<String> fList = null;
                if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                    try {
                        fList = gson.fromJson(retObj.getFieldValueClobString(looper, colNames[8]), typeList);
                        if (defaultFilterMap.containsKey(retObj.getFieldValueString(looper, colNames[0]))) {
                            fList = (ArrayList) defaultFilterMap.get(retObj.getFieldValueString(looper, colNames[0]));    // by ram
                        }
                        //added by sruhti for Ampersand symbol in parameter
                        for (int i = 0; i < fList.size(); i++) {
                            String datavalue = fList.get(i).toString();
                            if (datavalue.contains("u0026")) {
                                int index = fList.indexOf(datavalue);
                                fList.set(index, datavalue.replace("u0026", "&"));
                            }
                        } //ended by sruthi
                    } catch (com.google.gson.JsonParseException e) {
                        String normalString = retObj.getFieldValueClobString(looper, colNames[8]);
                        String[] splitedStr = normalString.split(",");
                        fList = Arrays.asList(splitedStr);

                    }

                } else {
                    try {
                        fList = gson.fromJson(retObj.getFieldUnknown(looper, 8), typeList);
                        if (defaultFilterMap.containsKey(retObj.getFieldValueString(looper, colNames[0]))) {
                            fList = (ArrayList) defaultFilterMap.get(retObj.getFieldValueString(looper, colNames[0]));    // by ram
                        }
                        //added by sruthi for Ampersand symbol in parameter
                        for (int i = 0; i < fList.size(); i++) {
                            String datavalue1 = fList.get(i).toString();
                            if (datavalue1.contains("u0026")) {
                                int index1 = fList.indexOf(datavalue1);
                                fList.set(index1, datavalue1.replace("u0026", "&"));
                            }
                        }
                        //ended by sruthi
                    } catch (com.google.gson.JsonParseException e) {
                        String normalString = retObj.getFieldUnknown(looper, 8);
                        String[] splitedStr = normalString.split(",");
                        fList = Arrays.asList(splitedStr);
                    }
                }

                if (paramExcludedIncluded != null && (paramExcludedIncluded.equalsIgnoreCase("NOT_SELECTED") || paramExcludedIncluded.equalsIgnoreCase("INCLUDED") || paramExcludedIncluded.equalsIgnoreCase("IN"))) {
                    paramInfo.add(fList);
                } else if (paramExcludedIncluded != null && paramExcludedIncluded == "" && fList != null) {
                    paramInfo.add(fList);
                } else {
                    fList = new ArrayList<String>();
                    fList.add("All");
                    paramInfo.add(fList);
                }

                if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                    try {
                        fList = gson.fromJson(retObj.getFieldValueClobString(looper, colNames[8]), typeList);
                        if (defaultFilterMap.containsKey(retObj.getFieldValueString(looper, colNames[0]))) {
                            fList = (ArrayList) defaultFilterMap.get(retObj.getFieldValueString(looper, colNames[0]));             // by ram
                        }
                        //added by sruthi for Ampersand symbol in parameter
                        for (int i = 0; i < fList.size(); i++) {
                            String datavalue = fList.get(i).toString();
                            if (datavalue.contains("u0026")) {
                                int index = fList.indexOf(datavalue);
                                fList.set(index, datavalue.replace("u0026", "&"));
                            }
                        }
                        //ended by sruthi
                    } catch (com.google.gson.JsonParseException e) {
                        String normalString = retObj.getFieldValueClobString(looper, colNames[8]);
                        String[] splitedStr = normalString.split(",");
                        fList = Arrays.asList(splitedStr);
                    }
                    if (paramExcludedIncluded != null && (paramExcludedIncluded.equalsIgnoreCase("NOT_SELECTED") || paramExcludedIncluded.equalsIgnoreCase("INCLUDED") || paramExcludedIncluded.equalsIgnoreCase("IN"))) {
                    } else {
                        fList = new ArrayList<String>();
                        fList.add("All");
                    }
                    paramValueList.add(retObj.getFieldValueString(looper, colNames[1]) + ":" + fList);
                } else {
                    try {
                        fList = gson.fromJson(retObj.getFieldUnknown(looper, 8), typeList);
                        if (defaultFilterMap.containsKey(retObj.getFieldValueString(looper, colNames[0]))) {
                            fList = (ArrayList) defaultFilterMap.get(retObj.getFieldValueString(looper, colNames[0]));    // by ram
                        }
                        //added by sruhti for Ampersand symbol in parameter
                        for (int i = 0; i < fList.size(); i++) {
                            String datavalue1 = fList.get(i).toString();
                            if (datavalue1.contains("u0026")) {
                                int index1 = fList.indexOf(datavalue1);
                                fList.set(index1, datavalue1.replace("u0026", "&"));
                            }
                        }
                        //ended by sruthi
                    } catch (com.google.gson.JsonParseException e) {
                        String normalString = retObj.getFieldUnknown(looper, 8);
                        String[] splitedStr = normalString.split(",");
                        fList = Arrays.asList(splitedStr);
                    }
                    if (paramExcludedIncluded != null && (paramExcludedIncluded.equalsIgnoreCase("NOT_SELECTED") || paramExcludedIncluded.equalsIgnoreCase("INCLUDED") || paramExcludedIncluded.equalsIgnoreCase("IN"))) {
                    } else {
                        fList = new ArrayList<String>();
                        fList.add("All");
                    }
                    paramValueList.add(retObj.getFieldValueString(looper, colNames[1]) + ":" + fList);
                }


                if (paramExcludedIncluded.equals("")) {
                    paramExcludedIncluded = "NOT_SELECTED";
                }

                paramInfo.add("CBOARP" + retObj.getFieldValueString(looper, colNames[0]));
                paramInfo.add(paramExcludedIncluded);
                this.setCompleteUrl(this.getCompleteUrl() + ";" + String.valueOf(paramInfo.get(9)) + "=" + gson.toJson(paramInfo.get(8), typeList)); //String.valueOf(paramInfo.get(8))

                reportParameters.put(retObj.getFieldValueString(looper, colNames[0]), paramInfo);
                reportParametersValues.put(retObj.getFieldValueString(looper, colNames[0]), paramInfo.get(8));
                reportParamIds.add(retObj.getFieldValueString(looper, colNames[0]));
                reportParamNames.add(retObj.getFieldValueString(looper, colNames[1]));
                resetParamHashmap.put(retObj.getFieldValueString(looper, colNames[0]), retObj.getFieldValueString(looper, colNames[9]));

                if (retObj.getFieldValueString(looper, colNames[10]) != null) {
                    dateoptions.put("A_" + retObj.getFieldValueString(looper, colNames[0]), retObj.getFieldValueString(looper, colNames[10]));
                }
                if (retObj.getFieldValueString(looper, colNames[11]) != null) {
                    dateSubStringValues.put("A_" + retObj.getFieldValueString(looper, colNames[0]), retObj.getFieldValueString(looper, colNames[11]));
                }
                if (retObj.getFieldValueString(looper, colNames[12]) != null) {
                    dateFormatt.put("A_" + retObj.getFieldValueString(looper, colNames[0]), retObj.getFieldValueString(looper, colNames[12]));
                }


            }
            HashMap inMap = new HashMap();
            HashMap notInMap = new HashMap();
            HashMap likeMap = new HashMap();
            HashMap notLikeMap = new HashMap();
            List<String> valulist = null;

            for (int i = 0; i < psize; i++) {
                paramExcludedIncluded = retObj.getFieldValueString(i, colNames[9]);
                if (paramExcludedIncluded.equals("")) {
                    paramExcludedIncluded = "NOT_SELECTED";
                }

                if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                    if (retObj.getFieldValueClobString(i, colNames[8]) != null) {
                        try {
                            valulist = gson.fromJson(retObj.getFieldValueClobString(i, colNames[8]), typeList);
                            if (defaultFilterMap.containsKey(retObj.getFieldValueString(i, colNames[0]))) {
                                valulist = (ArrayList) defaultFilterMap.get(retObj.getFieldValueString(i, colNames[0]));                 // by ram
                            }
                            //added by sruthi for Ampersand symbol in parameter
                            for (int j = 0; j < valulist.size(); j++) {
                                String datavalue1 = valulist.get(j).toString();
                                if (datavalue1.contains("u0026")) {
                                    int index1 = valulist.indexOf(datavalue1);
                                    valulist.set(index1, datavalue1.replace("u0026", "&"));
                                }
                            }//ended by sruhti
                        } catch (com.google.gson.JsonParseException e) {
                            String normalString = retObj.getFieldValueClobString(i, colNames[8]);
                            String[] splitedStr = normalString.split(",");
                            valulist = Arrays.asList(splitedStr);
                        }

                    }

                } else {
                    try {
                        valulist = gson.fromJson(retObj.getFieldUnknown(i, 8), typeList);
                        if (defaultFilterMap.containsKey(retObj.getFieldValueString(i, colNames[0]))) {
                            valulist = (ArrayList) defaultFilterMap.get(retObj.getFieldValueString(i, colNames[0]));                 // by ram
                        }
                        //added by sruthi for Ampersand symbol in parameter
                        for (int j = 0; j < valulist.size(); j++) {
                            String datavalue1 = valulist.get(j).toString();
                            if (datavalue1.contains("u0026")) {
                                int index1 = valulist.indexOf(datavalue1);
                                valulist.set(index1, datavalue1.replace("u0026", "&"));
                            }
                        }//ended by sruthi
                    } catch (com.google.gson.JsonParseException e) {
                        String normalString = retObj.getFieldUnknown(i, 8);
                        String[] splitedStr = normalString.split(",");
                        valulist = Arrays.asList(splitedStr);
                    }
                }
                if (reportVersion < currentRepVersion) {
                    if (paramExcludedIncluded.equalsIgnoreCase("INCLUDED") || paramExcludedIncluded.equalsIgnoreCase("NOT_SELECTED")
                            || paramExcludedIncluded.equalsIgnoreCase("IN")) {
                        inMap.put(retObj.getFieldValueString(i, colNames[0]), valulist);
                    }
                    if (paramExcludedIncluded.equalsIgnoreCase("EXCLUDED")
                            || paramExcludedIncluded.equalsIgnoreCase("NOT IN")
                            || paramExcludedIncluded.equalsIgnoreCase("NOTIN")) {
                        notInMap.put(retObj.getFieldValueString(i, colNames[0]), valulist);

                    }
                    if (paramExcludedIncluded.equalsIgnoreCase("LIKE")) {
                        likeMap.put(retObj.getFieldValueString(i, colNames[0]), valulist);

                    }
                    if (paramExcludedIncluded.equalsIgnoreCase("NOT LIKE") || paramExcludedIncluded.equalsIgnoreCase("NOTLIKE")) {
                        notLikeMap.put(retObj.getFieldValueString(i, colNames[0]), valulist);
                    }

                } else {
                    inMap.put(retObj.getFieldValueString(i, colNames[0]), valulist);
                }
                if (retObj.getFieldValueString(i, colNames[13]) != null && !retObj.getFieldValueString(i, colNames[13]).isEmpty()) {
                    valulist = gson.fromJson(retObj.getFieldValueString(i, 13), typeList);
                    notInMap.put(retObj.getFieldValueString(i, colNames[0]), valulist);

                }
                if (retObj.getFieldValueString(i, colNames[14]) != null && !retObj.getFieldValueString(i, colNames[14]).isEmpty()) {
                    valulist = gson.fromJson(retObj.getFieldValueString(i, 14), typeList);
                    likeMap.put(retObj.getFieldValueString(i, colNames[0]), valulist);

                }
                if (retObj.getFieldValueString(i, colNames[15]) != null && !retObj.getFieldValueString(i, colNames[15]).isEmpty()) {
                    valulist = gson.fromJson(retObj.getFieldValueString(i, 15), typeList);
                    notLikeMap.put(retObj.getFieldValueString(i, colNames[0]), valulist);
                }
            }
            operatorFilters.put("IN", inMap);
            operatorFilters.put("NOTIN", notInMap);
            operatorFilters.put("LIKE", likeMap);
            operatorFilters.put("NOTLIKE", notLikeMap);


        }
        if (!reportParameters.isEmpty()) {
            this.initializeConnectionSettings(elementId);
        }
        String viewByID = "";
        String viewBySeq = "";
        try {
            sqlstr = getResourceBundle().getString("getParamMetaDataQuery2");
            Obj = new Object[1];
            Obj[0] = this.reportId;

            finalQuery = db.buildQuery(sqlstr, Obj);


            retObj = db.execSelectSQL(finalQuery);

        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        Vector rowviewbys = new Vector();

        colNames = retObj.getColumnNames();
        psize = retObj.getRowCount();
        int j = 0;
        ArrayList viewHashList = new ArrayList();


        if (psize > 0) {
            for (int looper = 0; looper < psize; looper++) {
                rowviewbys.add(retObj.getFieldValueString(looper, colNames[13]));
                String crossTabelementstr = retObj.getFieldValueString(looper, colNames[14]);
                if (crossTabelementstr != null && !crossTabelementstr.equalsIgnoreCase("")) {
                    String[] crosstabelements = crossTabelementstr.split(",");
                    this.setCrosstabelements(crosstabelements, retObj.getFieldValueString(looper, colNames[13]));
                }

            }
        } else {
            rowviewbys.add("Time");
        }

        if (psize > 0) {
            reportViewByOrder = new LinkedHashMap<String, ArrayList<String>>();
            reportRowViewbyValues = new ArrayList<String>();
            reportColViewbyValues = new ArrayList<String>();
            reportColViewbys = new ArrayList<String>();
            for (int looper = 0; looper < psize; looper++) {
                if (looper == 0) {
                    viewByID = retObj.getFieldValueString(0, colNames[0]);
                    viewHashList = new ArrayList<String>();
                    viewHashList.add(retObj.getFieldValueString(0, colNames[0]));//view By id//0
                    viewHashList.add(retObj.getFieldValueString(0, colNames[13]));// Default Value//1
                    if (retObj.getFieldValueInt(0, colNames[4]) == -1) {
                        if (overrideRowViewByDimId != null) {
                            reportRowViewbyValues.add(overrideRowViewByDimId);
                        } else {
                            if (reportIncomingParameters.get("CBOVIEW_BY" + viewByID) == null) {
                                reportRowViewbyValues.add(retObj.getFieldValueString(0, colNames[13]));
                            } else {
                                reportRowViewbyValues.add((String) reportIncomingParameters.get("CBOVIEW_BY" + viewByID));
                            }
                        }
                        if (retObj.getFieldValueString(0, colNames[15]) != null && !retObj.getFieldValueString(0, colNames[15]).equals("") && retObj.getFieldValueString(0, colNames[15]).equalsIgnoreCase("true")) {
                            hideViewbys.add(retObj.getFieldValueString(0, colNames[13]));
                        }
//                        hideViewbys.add(temp);
                        ArrayList test = new ArrayList();
                        test.add("Row View By");
                        test.add(retObj.getFieldValueString(looper, colNames[3]));
                        reportViewByOrder.put(viewByID, test);
                    }
                    if (retObj.getFieldValueInt(0, colNames[3]) == -1) {
                        reportColViewbyValues.add(retObj.getFieldValueString(0, colNames[13]));
                        reportColViewbys.add("A_" + retObj.getFieldValueString(0, colNames[13]));
                        ArrayList test = new ArrayList();
                        test.add("Col View By");
                        test.add(retObj.getFieldValueString(looper, colNames[4]));
                        reportViewByOrder.put(viewByID, test);
                    }
                    viewHashList.add(reportIncomingParameters.get("CBOVIEW_BY" + viewByID));//Curr Value//2

                    viewHashList.add(retObj.getFieldValueString(0, colNames[7]));// Added first value//3
                } else if (!viewByID.equalsIgnoreCase(retObj.getFieldValueString(looper, colNames[0]))) {
                    viewHashList.add("Time");

                    if (viewHashList.get(2) != null) {
                        this.completeUrl += ";" + "CBOVIEW_BY" + viewByID + "=" + viewHashList.get(2).toString();
                    } else {
                        this.completeUrl += ";" + "CBOVIEW_BY" + viewByID + "=" + viewHashList.get(1).toString();
                    }
                    reportViewByMain.put(viewByID, viewHashList);
                    viewByID = retObj.getFieldValueString(looper, colNames[0]);

                    viewHashList = new ArrayList();
                    viewHashList.add(retObj.getFieldValueString(looper, colNames[0]));//view By id
                    viewHashList.add(retObj.getFieldValueString(looper, colNames[13]));// Default Value
                    if (retObj.getFieldValueInt(looper, colNames[4]) == -1) {
                        if (overrideRowViewByDimId == null) {
                            reportRowViewbyValues.add(retObj.getFieldValueString(looper, colNames[13]));
                            ArrayList test = new ArrayList();
                            test.add("Row View By");
                            test.add(retObj.getFieldValueString(looper, colNames[3]));
                            reportViewByOrder.put(viewByID, test);
                            if (retObj.getFieldValueString(looper, colNames[15]) != null && !retObj.getFieldValueString(looper, colNames[15]).equals("") && retObj.getFieldValueString(looper, colNames[15]).equalsIgnoreCase("true")) {
                                hideViewbys.add(retObj.getFieldValueString(looper, colNames[13]));
                            }
                        }
                    }
                    if (retObj.getFieldValueInt(looper, colNames[3]) == -1) {
                        reportColViewbyValues.add(retObj.getFieldValueString(looper, colNames[13]));
                        reportColViewbys.add("A_" + retObj.getFieldValueString(looper, colNames[13]));
                        ArrayList test = new ArrayList();
                        test.add("Col View By");
                        test.add(retObj.getFieldValueString(looper, colNames[4]));
                        reportViewByOrder.put(viewByID, test);
                    }
                    viewHashList.add(reportIncomingParameters.get("CBOVIEW_BY" + viewByID));//Curr Value
                    viewHashList.add(retObj.getFieldValueString(looper, colNames[7]));// Added first value

                } else {
                    viewHashList.add(retObj.getFieldValueString(looper, colNames[7]));// Added other value
                }
            }

            viewHashList.add("Time");
            if (viewHashList.get(2) != null) {
                this.completeUrl += ";" + "CBOVIEW_BY" + viewByID + "=" + viewHashList.get(2).toString();
            } else {
                this.completeUrl += ";" + "CBOVIEW_BY" + viewByID + "=" + viewHashList.get(1).toString();
            }

            reportViewByMain.put(viewByID, viewHashList);
            kpireportViewByMain.put("global", viewHashList);
        } else {//No Parameter Other then time
            sqlstr = " SELECT M.VIEW_BY_ID,  M.REPORT_ID, M.VIEW_BY_SEQ, M.ROW_SEQ,M.COL_SEQ ,M.DEFAULT_VALUE "
                    + " FROM PRG_AR_REPORT_VIEW_BY_MASTER M where m.report_id = " + this.reportId + " order by  m.col_seq , m.row_seq ";

            try {
//                sqlstr = getResourceBundle().getString("getParamMetaDataQuery2");
//                Obj = new Object[1];
//                Obj[0] = this.reportId;

                finalQuery = sqlstr;
                retObj = db.execSelectSQL(finalQuery);

            } catch (Exception ex) {
                logger.error("Exception:", ex);
            }

            psize = retObj.getRowCount();
            colNames = retObj.getColumnNames();
            if (psize > 0) {
                viewByID = retObj.getFieldValueString(0, colNames[0]);
                reportViewByMain = new HashMap<String, ArrayList<String>>();
                reportViewByOrder = new LinkedHashMap<String, ArrayList<String>>();
                reportIncomingParameters.get("CBOVIEW_BY" + (retObj.getFieldValueString(0, colNames[0])));
                reportRowViewbyValues = new ArrayList<String>();
                reportColViewbyValues = new ArrayList<String>();
                reportColViewbys = new ArrayList<String>();
                viewHashList = new ArrayList<String>();
                viewHashList.add(retObj.getFieldValueString(0, colNames[0]));//view By id//0
                viewHashList.add(retObj.getFieldValueString(0, colNames[5]));// Default Value//1

                if (retObj.getFieldValueInt(0, colNames[4]) == -1) {
                    reportRowViewbyValues.add(retObj.getFieldValueString(0, colNames[5]));
                    ArrayList test = new ArrayList();
                    test.add("Row View By");
                    test.add(retObj.getFieldValueString(0, colNames[3]));
                    reportViewByOrder.put(viewByID, test);
                }
                viewHashList.add(reportIncomingParameters.get("CBOVIEW_BY" + viewByID));//Curr Value//2
                viewHashList.add("Time");// Added first value//3Is also the first and last view by

                // viewHashList.add("Time");// Added first value//3

                reportViewByMain.put(viewByID, viewHashList);

            }



        }

        sqlstr = "";

        try {
            sqlstr = getResourceBundle().getString("getParameterTimeInfo");
            Obj = new Object[1];
            Obj[0] = this.reportId;

            finalQuery = db.buildQuery(sqlstr, Obj);
            //////.println("finalquery of getparamtimeinfo is : "+finalQuery);
            retObj = db.execSelectSQL(finalQuery);

        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        colNames = retObj.getColumnNames();
        psize = retObj.getRowCount();
        ProgenParam pParam = new ProgenParam();

        timeDetailsMap = new LinkedHashMap<String, ArrayList<String>>();
        timeDetailsArray = new ArrayList();

        if (psize > 0) {
            timeDetailsArray.add(retObj.getFieldValueString(0, colNames[1]));
            timeDetailsArray.add(retObj.getFieldValueString(0, colNames[2]));
            for (int looper = 0; looper < psize; looper++) {
                ArrayList<String> timeInfo = new ArrayList<String>();
                temp = (retObj.getFieldValueString(looper, colNames[3])).trim();

                if (temp.equalsIgnoreCase("AS_OF_DATE")) {
                    if ("".equals(retObj.getFieldValueString(looper, 7))) {
//                            if (null==(retObj.getFieldValueDateString(0, 7))) {
                        if ((retObj.getFieldValueString(looper, colNames[8]) == null ? "" != null : !retObj.getFieldValueString(looper, colNames[8]).equals(""))) {
                            if (!retObj.getFieldValueString(looper, colNames[8]).equalsIgnoreCase("Month")) {
                                if (!retObj.getFieldValueString(looper, colNames[8]).equalsIgnoreCase("Qtr")) {
                                    if (!retObj.getFieldValueString(looper, colNames[8]).equalsIgnoreCase("Year")) {
                                        if (retObj.getFieldValueString(looper, colNames[8]).equalsIgnoreCase("Yesterday")) {
                                            timeInfo.add(pParam.getcurrentdateforpage(1));
                                        } else if (retObj.getFieldValueString(looper, colNames[8]).equalsIgnoreCase("Tomorrow")) {
                                            timeInfo.add(pParam.getcurrentdateforpage(-1));
                                        } else if (retObj.getFieldValueString(looper, colNames[8]).contains("newSysDate")) {
                                            timeInfo.add(pParam.getcurrentdateforpage(retObj.getFieldValueString(looper, colNames[8])));
                                        } else if (retObj.getFieldValueString(looper, colNames[8]).contains("globalDate")) {
                                            timeInfo.add(pParam.getcurrentdateforpage(retObj.getFieldValueString(looper, colNames[8])));
                                        } else if (retObj.getFieldValueString(looper, colNames[8]).contains("systemDate")) {
                                            timeInfo.add(pParam.getdateforpage());
                                        } else {
                                            timeInfo.add(pParam.getdateforpage());
                                        }
                                    } else {
                                        timeInfo.add(pParam.getdateforpage());
                                    }
                                } else {
                                    timeInfo.add(pParam.getdateforpage());
                                }
                            } else {
                                timeInfo.add(pParam.getdateforpage());
                            }
                        } else {
                            timeInfo.add(pParam.getdateforpage());
                        }

                    } else {
                        String date = retObj.getFieldValueString(looper, 7);
                        timeInfo.add(date);
                    }
                } else if (temp.equalsIgnoreCase("AS_OF_DATE1")) {
                    if ("".equals(retObj.getFieldValueString(looper, 7))) {
                        if ((retObj.getFieldValueString(looper, colNames[8]) == null ? "" != null : !retObj.getFieldValueString(looper, colNames[8]).equals(""))) {
                            if (retObj.getFieldValueString(looper, colNames[8]).equalsIgnoreCase("fromyestrday")) {
                                timeInfo.add(pParam.getcurrentdateforpage(1));
                            } else if (retObj.getFieldValueString(looper, colNames[8]).equalsIgnoreCase("fromtomorow")) {
                                timeInfo.add(pParam.getcurrentdateforpage(-1));
                            } else if (retObj.getFieldValueString(looper, colNames[8]).contains("fromSysDate")) {
                                timeInfo.add(pParam.getcurrentdateforpage(retObj.getFieldValueString(looper, colNames[8])));
                            } else if (retObj.getFieldValueString(looper, colNames[8]).contains("fromglobalDate")) {
                                timeInfo.add(pParam.getcurrentdateforpage(retObj.getFieldValueString(looper, colNames[8])));
                            } else {
                                timeInfo.add(pParam.getdateforpage(30));
                            }
                        }
//                            if (null==(retObj.getFieldValueDateString(0, 7))) {
                    } else {
                        String date = retObj.getFieldValueString(looper, 7);
                        timeInfo.add(date);
                    }

                } else if (temp.equalsIgnoreCase("AS_OF_DATE2")) {
                    if ("".equals(retObj.getFieldValueString(looper, 7))) {
                        if ((retObj.getFieldValueString(looper, colNames[8]) == null ? "" != null : !retObj.getFieldValueString(looper, colNames[8]).equals(""))) {
                            if (retObj.getFieldValueString(looper, colNames[8]).equalsIgnoreCase("toyestrday")) {
                                timeInfo.add(pParam.getcurrentdateforpage(1));
                            } else if (retObj.getFieldValueString(looper, colNames[8]).equalsIgnoreCase("totomorow")) {
                                timeInfo.add(pParam.getcurrentdateforpage(-1));
                            } else if (retObj.getFieldValueString(looper, colNames[8]).contains("toSystDate")) {
                                timeInfo.add(pParam.getcurrentdateforpage(retObj.getFieldValueString(looper, colNames[8])));
                            } else if (retObj.getFieldValueString(looper, colNames[8]).contains("toglobalDdate")) {
                                timeInfo.add(pParam.getcurrentdateforpage(retObj.getFieldValueString(looper, colNames[8])));
                            } else {
                                timeInfo.add(pParam.getdateforpage(1));
                            }
                        }
//                            if (null==(retObj.getFieldValueDateString(0, 7))) {
                    } else {
                        String date = retObj.getFieldValueString(looper, 7);
                        timeInfo.add(date);
                    }

                } else if (temp.equalsIgnoreCase("CMP_AS_OF_DATE1")) {
                    if (retObj.getFieldValueString(looper, colNames[2]) == null || (!retObj.getFieldValueString(looper, colNames[2]).equalsIgnoreCase("PRG_COHORT"))) {
                        String date = "";
                        date = retObj.getFieldValueString(looper, 7);
                        if (!date.equalsIgnoreCase("") && date != null) {
                            timeInfo.add(date);
                        } else {
                            if ((retObj.getFieldValueString(looper, colNames[8]) == null ? "" != null : !retObj.getFieldValueString(looper, colNames[8]).equals(""))) {
                                if (retObj.getFieldValueString(looper, colNames[8]).equalsIgnoreCase("CmpFrmyestrday")) {
                                    timeInfo.add(pParam.getcurrentdateforpage(1));
                                } else if (retObj.getFieldValueString(looper, colNames[8]).equalsIgnoreCase("CmpFrmtomorow")) {
                                    timeInfo.add(pParam.getcurrentdateforpage(-1));
                                } else if (retObj.getFieldValueString(looper, colNames[8]).contains("CmpFrmSysDate")) {
                                    timeInfo.add(pParam.getcurrentdateforpage(retObj.getFieldValueString(looper, colNames[8])));
                                } else if (retObj.getFieldValueString(looper, colNames[8]).contains("CmpFrmglobalDate")) {
                                    timeInfo.add(pParam.getcurrentdateforpage(retObj.getFieldValueString(looper, colNames[8])));
                                } else {
                                    timeInfo.add(pParam.getdateforpage(60));
                                }
                            }
                        }
                    } else {
                        timeInfo.add(pParam.getdateforpage(30));
                    }
                } else if (temp.equalsIgnoreCase("CMP_AS_OF_DATE2")) {
                    if (retObj.getFieldValueString(looper, colNames[2]) == null || (!retObj.getFieldValueString(looper, colNames[2]).equalsIgnoreCase("PRG_COHORT"))) {
                        String date = "";
                        date = retObj.getFieldValueString(looper, 7);
                        if (!date.equalsIgnoreCase("") && date != null) {
                            timeInfo.add(date);
                        } else {
                            if ((retObj.getFieldValueString(looper, colNames[8]) == null ? "" != null : !retObj.getFieldValueString(looper, colNames[8]).equals(""))) {
                                if (retObj.getFieldValueString(looper, colNames[8]).equalsIgnoreCase("cmptoyestrday")) {
                                    timeInfo.add(pParam.getcurrentdateforpage(1));
                                } else if (retObj.getFieldValueString(looper, colNames[8]).equalsIgnoreCase("cmptotomorow")) {
                                    timeInfo.add(pParam.getcurrentdateforpage(-1));
                                } else if (retObj.getFieldValueString(looper, colNames[8]).contains("cmptoSysDate")) {
                                    timeInfo.add(pParam.getcurrentdateforpage(retObj.getFieldValueString(looper, colNames[8])));
                                } else if (retObj.getFieldValueString(looper, colNames[8]).contains("cmptoglobalDate")) {
                                    timeInfo.add(pParam.getcurrentdateforpage(retObj.getFieldValueString(looper, colNames[8])));
                                } else {
                                    timeInfo.add(pParam.getdateforpage(60));
                                }
                            }
                        }
                    } else {
                        timeInfo.add(pParam.getdateforpage(-365));
                    }
                } else if (temp.equalsIgnoreCase("AS_OF_DMONTH1")) {
                    timeInfo.add(pParam.getmonthforpage("30"));
                } else if (temp.equalsIgnoreCase("AS_OF_DMONTH2")) {
                    timeInfo.add(pParam.getmonthforpage("0"));
                } else if (temp.equalsIgnoreCase("CMP_AS_OF_DMONTH1") || temp.equalsIgnoreCase("CMP_AS_OF_DMONTH2")) {
                    timeInfo.add(pParam.getmonthforpage("62"));
                } else if (temp.equalsIgnoreCase("CMP_AS_OF_DMONTH2")) {
                    timeInfo.add(pParam.getmonthforpage("31"));
                } else if (temp.equalsIgnoreCase("AS_OF_DQUARTER1")) {
                    timeInfo.add(pParam.getQtrforpage());
                } else if (temp.equalsIgnoreCase("AS_OF_DQUARTER2")) {
                    timeInfo.add(pParam.getQtrforpage());
                } else if (temp.equalsIgnoreCase("CMP_AS_OF_DQUARTER1") || temp.equalsIgnoreCase("CMP_AS_OF_DQUARTER2")) {
                    timeInfo.add(pParam.getQtrforpage());
                } else if (temp.equalsIgnoreCase("CMP_AS_OF_DQUARTER2")) {
                    timeInfo.add(pParam.getQtrforpage());
                } else if (temp.equalsIgnoreCase("AS_OF_DYEAR1")) {
                    timeInfo.add(pParam.getYearforpage("366"));
                } else if (temp.equalsIgnoreCase("AS_OF_DYEAR2")) {
                    timeInfo.add(pParam.getYearforpage());
                } else if (temp.equalsIgnoreCase("CMP_AS_OF_DYEAR1")) {
                    timeInfo.add(pParam.getYearforpage("734"));
                } else if (temp.equalsIgnoreCase("CMP_AS_OF_DYEAR2")) {
                    timeInfo.add(pParam.getYearforpage("367"));
                } else if (temp.equalsIgnoreCase("AS_OF_MONTH") || temp.equalsIgnoreCase("AS_OF_MONTH1")) {
                    timeInfo.add(pParam.getmonthforpage());
                } else if (temp.equalsIgnoreCase("AS_OF_YEAR") || temp.equalsIgnoreCase("AS_OF_YEAR1")) {
                    timeInfo.add(pParam.getYearforpage());
                } else if (temp.equalsIgnoreCase("PRG_PERIOD_TYPE")) {
                    if ((retObj.getFieldValueString(looper, colNames[8]) == null ? "" != null : !retObj.getFieldValueString(looper, colNames[8]).equals(""))) {
                        timeInfo.add(retObj.getFieldValueString(looper, colNames[8]));
                    } else {
                        timeInfo.add("Month");
                    }
                } else if (temp.equalsIgnoreCase("PRG_DAY_ROLLING")) {
                    timeInfo.add("Last 30 Days");
                } else if (temp.equalsIgnoreCase("PRG_COMPARE")) {
                    if ((retObj.getFieldValueString(looper, colNames[8]) == null ? "" != null : !retObj.getFieldValueString(looper, colNames[8]).equals(""))) {
                        if (retObj.getFieldValueString(looper, colNames[8]).equalsIgnoreCase("Month")) {
                            timeInfo.add("Last Period");
                        } else {
                            timeInfo.add(retObj.getFieldValueString(looper, colNames[8]));
                        }
                    } else {
                        timeInfo.add("Last Period");
                    }
                }

                timeInfo.add("CBO_" + temp);
                timeInfo.add(retObj.getFieldValueString(looper, colNames[4]));
                timeInfo.add(retObj.getFieldValueString(looper, colNames[5]));
                timeInfo.add(retObj.getFieldValueString(looper, colNames[6]));
                timeInfo.add(timeInfo.get(0));

                timeInfo.add(temp);
                timeDetailsMap.put(timeInfo.get(6), timeInfo);
            }
        }
        //////.println("timeDetailsArray is : "+timeDetailsArray);
        ArrayList timeInfo = new ArrayList();
        //////////////Time Calc
        if (!timeDetailsArray.isEmpty()) {
            if (timeDetailsArray.get(0).toString().equalsIgnoreCase("Year") && timeDetailsArray.get(1).toString().equalsIgnoreCase("PRG_YEAR_CMP")) {
                timeInfo = (ArrayList) timeDetailsMap.get("AS_OF_YEAR");
                timeDetailsArray.add(timeInfo.get(0));
                timeInfo = (ArrayList) timeDetailsMap.get("AS_OF_YEAR1");
                if (timeInfo != null) {
                    timeDetailsArray.add(timeInfo.get(0));
                } else {
                    timeDetailsArray.add(null);
                }

            } else if (timeDetailsArray.get(0).toString().equalsIgnoreCase("DAY") && timeDetailsArray.get(1).toString().equalsIgnoreCase("PRG_COHORT")) {
                timeInfo = timeDetailsMap.get("AS_OF_DATE1");
                timeDetailsArray.add(timeInfo.get(0));

                timeInfo = timeDetailsMap.get("AS_OF_DATE2");
                timeDetailsArray.add(timeInfo.get(0));
                if (timeDetailsMap.get("CMP_AS_OF_DATE1") != null) {
                    timeInfo = timeDetailsMap.get("CMP_AS_OF_DATE1");
                    timeDetailsArray.add(timeInfo.get(0));
                } else {
                    timeDetailsArray.add(null);
                }
                if (timeDetailsMap.get("CMP_AS_OF_DATE2") != null) {
                    timeInfo = timeDetailsMap.get("CMP_AS_OF_DATE2");
                    timeDetailsArray.add(timeInfo.get(0));

                } else {
                    timeDetailsArray.add(null);
                }
                if (timeDetailsMap.get("PRG_PERIOD_TYPE") != null) {
                    timeInfo = timeDetailsMap.get("PRG_PERIOD_TYPE");
                    timeDetailsArray.add(timeInfo.get(0));
                } else {
                    timeDetailsArray.add("PERIOD");
                }
            } else if (timeDetailsArray.get(0).toString().equalsIgnoreCase("DAY") && timeDetailsArray.get(1).toString().equalsIgnoreCase("PRG_DATE_RANGE")) {
                timeInfo = (ArrayList) timeDetailsMap.get("AS_OF_DATE1");
                timeDetailsArray.add(timeInfo.get(0));

                timeInfo = (ArrayList) timeDetailsMap.get("AS_OF_DATE2");
                timeDetailsArray.add(timeInfo.get(0));
                if (timeDetailsMap.get("CMP_AS_OF_DATE1") != null) {
                    timeInfo = (ArrayList) timeDetailsMap.get("CMP_AS_OF_DATE1");
                    timeDetailsArray.add(timeInfo.get(0));
                } else {
                    timeDetailsArray.add(null);
                }
                if (timeDetailsMap.get("CMP_AS_OF_DATE2") != null) {
                    timeInfo = (ArrayList) timeDetailsMap.get("CMP_AS_OF_DATE2");
                    timeDetailsArray.add(timeInfo.get(0));

                } else {
                    timeDetailsArray.add(null);
                }
            } else if (timeDetailsArray.get(0).toString().equalsIgnoreCase("DAY") && timeDetailsArray.get(1).toString().equalsIgnoreCase("PRG_MONTH_RANGE")) {
                timeInfo = (ArrayList) timeDetailsMap.get("AS_OF_DMONTH1");
                timeDetailsArray.add(timeInfo.get(0));
                timeInfo = (ArrayList) timeDetailsMap.get("AS_OF_DMONTH2");
                timeDetailsArray.add(timeInfo.get(0));
                if (timeDetailsMap.get("CMP_AS_OF_DMONTH1") != null) {
                    timeInfo = (ArrayList) timeDetailsMap.get("CMP_AS_OF_DMONTH1");
                    timeDetailsArray.add(timeInfo.get(0));
                } else {
                    timeDetailsArray.add(null);
                }
                if (timeDetailsMap.get("CMP_AS_OF_DMONTH2") != null) {
                    timeInfo = (ArrayList) timeDetailsMap.get("CMP_AS_OF_DMONTH2");
                    timeDetailsArray.add(timeInfo.get(0));

                } else {
                    timeDetailsArray.add(null);
                }

            } else if (timeDetailsArray.get(0).toString().equalsIgnoreCase("DAY") && timeDetailsArray.get(1).toString().equalsIgnoreCase("PRG_QUARTER_RANGE")) {
                timeInfo = (ArrayList) timeDetailsMap.get("AS_OF_DQUARTER1");
                timeDetailsArray.add(timeInfo.get(0));
                timeInfo = (ArrayList) timeDetailsMap.get("AS_OF_DQUARTER2");
                timeDetailsArray.add(timeInfo.get(0));
                if (timeDetailsMap.get("CMP_AS_OF_DQUARTER1") != null) {
                    timeInfo = (ArrayList) timeDetailsMap.get("CMP_AS_OF_DQUARTER1");
                    timeDetailsArray.add(timeInfo.get(0));
                } else {
                    timeDetailsArray.add(null);
                }
                if (timeDetailsMap.get("CMP_AS_OF_DQUARTER2") != null) {
                    timeInfo = (ArrayList) timeDetailsMap.get("CMP_AS_OF_DQUARTER2");
                    timeDetailsArray.add(timeInfo.get(0));
                } else {
                    timeDetailsArray.add(null);
                }

            } else if (timeDetailsArray.get(0).toString().equalsIgnoreCase("DAY") && timeDetailsArray.get(1).toString().equalsIgnoreCase("PRG_YEAR_RANGE")) {
                timeInfo = (ArrayList) timeDetailsMap.get("AS_OF_DYEAR1");
                timeDetailsArray.add(timeInfo.get(0));
                timeInfo = (ArrayList) timeDetailsMap.get("AS_OF_DYEAR2");
                timeDetailsArray.add(timeInfo.get(0));
                if (timeDetailsMap.get("CMP_AS_OF_DYEAR1") != null) {
                    timeInfo = (ArrayList) timeDetailsMap.get("CMP_AS_OF_DYEAR1");
                    timeDetailsArray.add(timeInfo.get(0));
                } else {
                    timeDetailsArray.add(null);
                }
                if (timeDetailsMap.get("CMP_AS_OF_DYEAR2") != null) {
                    timeInfo = (ArrayList) timeDetailsMap.get("CMP_AS_OF_DYEAR2");
                    timeDetailsArray.add(timeInfo.get(0));

                } else {
                    timeDetailsArray.add(null);
                }

            } else if (timeDetailsArray.get(0).toString().equalsIgnoreCase("DAY") && timeDetailsArray.get(1).toString().equalsIgnoreCase("PRG_DAY_ROLLING")) {
                timeInfo = (ArrayList) timeDetailsMap.get("AS_OF_DATE");
                timeDetailsArray.add(timeInfo.get(0));

                timeInfo = (ArrayList) timeDetailsMap.get("PRG_DAY_ROLLING");
                timeDetailsArray.add(timeInfo.get(0));


            } else if (timeDetailsArray.get(0).toString().equalsIgnoreCase("DAY") && timeDetailsArray.get(1).toString().equalsIgnoreCase("PRG_STD")) {
                timeInfo = (ArrayList) timeDetailsMap.get("AS_OF_DATE");
                timeDetailsArray.add(timeInfo.get(0));

                timeInfo = (ArrayList) timeDetailsMap.get("PRG_PERIOD_TYPE");
                timeDetailsArray.add(timeInfo.get(0));

                if (timeDetailsMap.get("PRG_COMPARE") != null) {
                    timeInfo = (ArrayList) timeDetailsMap.get("PRG_COMPARE");
                    timeDetailsArray.add(timeInfo.get(0));
                } else {
                    timeDetailsArray.add("Last Period");
                }
            } else if (timeDetailsArray.get(0).toString().equalsIgnoreCase("Month") && timeDetailsArray.get(1).toString().equalsIgnoreCase("PRG_MONTH_CMP")) {
                timeInfo = (ArrayList) timeDetailsMap.get("AS_OF_MONTH");
                timeDetailsArray.add(timeInfo.get(0));

                timeInfo = (ArrayList) timeDetailsMap.get("AS_OF_MONTH1");
                timeDetailsArray.add(timeInfo.get(0));

            } else if (timeDetailsArray.get(0).toString().equalsIgnoreCase("MONTH") && timeDetailsArray.get(1).toString().equalsIgnoreCase("PRG_STD")) {
                timeInfo = (ArrayList) timeDetailsMap.get("AS_OF_MONTH");
                timeDetailsArray.add(timeInfo.get(0));

                timeInfo = (ArrayList) timeDetailsMap.get("PRG_PERIOD_TYPE");
                timeDetailsArray.add(timeInfo.get(0));

                if (timeDetailsMap.get("PRG_COMPARE") != null) {
                    timeInfo = (ArrayList) timeDetailsMap.get("PRG_COMPARE");
                    timeDetailsArray.add(timeInfo.get(0));
                } else {
                    timeDetailsArray.add("Last Period");
                }
            }



        } else {////Time Query Failed as of now only because of prgorn non Standard time
            //ie Proge Time is not used .
            //We will set day level time , but we should find the time levele from time master table
            avoidProgenTime();
        }
        ////End of time details
        this.resetPath = ctxPath + "/reportViewer.do?reportBy=viewReport&REPORTID=" + this.reportId + "&action=reset";

        if (isOpenEvent) {
            //NOW INITIALIZING REPORT QUERY METADATA only for reports other than cross tab
            getReportQueryInfo();
            //NOW INITIALIZING REPORT TABLE METADATA only for reports other than cross tab
            getReportTableInfo();
            //code to retrive Report Business Roles which are selected at the time of report designing
            getReportBizRoles();
        }
        if (reportIncomingParameters.get("reportDrill") != null && reportIncomingParameters.get("reportDrill").toString().equalsIgnoreCase("Y")) {
        } else {
            if (reportIncomingParameters != null && !reportIncomingParameters.isEmpty()) {

                // Update the parameters if there's any in reportIncomingParameters
                Set paramEleIds = reportParameters.keySet();
                Iterator paramEleIter = paramEleIds.iterator();
                String paramElement;
                List<String> parameterValue;

                while (paramEleIter.hasNext()) {
                    paramElement = (String) paramEleIter.next();
                    parameterValue = (List<String>) reportIncomingParameters.get("CBOARP" + paramElement);

                    if (parameterValue != null && !parameterValue.isEmpty()) {
                        List<String> paramValsList = parameterValue;

                        if (parameterValue != null && !parameterValue.isEmpty() && !parameterValue.contains("All")) {
                            updateParameterDefaults(paramElement, paramValsList, false);
                        }
                        // changed by swathi for the purpose of saveasnew include issue
//                    updateParameterDefaults(paramElement, paramValsList, false);
                    }
                }
            }
        }
        if (!reportParameters.isEmpty()) {
            this.initializeConnectionSettings(elementId);
        } else if (!reportQryElementIds.isEmpty()) {
            this.initializeConnectionSettings((String) reportQryElementIds.get(0));
        }
        this.completeUrl = ctxPath + "/reportViewer.do?reportBy=viewReport;REPORTID=" + this.reportId + this.completeUrl;

//        if(overrideViewBys1 !=null &&!this.overrideViewBys1.equalsIgnoreCase("") && this.overrideViewBys1 != null && this.overrideViewBys1.equalsIgnoreCase("isChecked")){
//            isTimeDrill = false;
//        String paramExcludedIncluded1 = "NOT_SELECTED";
//        this.setCompleteUrl("");
//        boolean excludedParam = false;
//        completeUrl = "";
//        //This will get parameters hashMap if Report Id is provided
//
//        Set paramEleIds = reportParameters.keySet();
//        Iterator paramEleIter = paramEleIds.iterator();
//        String paramElement;
//        String parameterValue;
//        ArrayList paramInfo;
//        paramValueList.clear();
//        while ( paramEleIter.hasNext() )
//        {
//            excludedParam = false;
//            paramElement = (String) paramEleIter.next();
//            paramInfo = (ArrayList) reportParameters.get(paramElement);
//            parameterValue = (String)reportIncomingParameters.get("CBOARP" + paramElement);
//            if(parameterValue==null){
//               if(paramInfo.get(8)!=null && !paramInfo.get(8).equals("")) {
//                   parameterValue = paramInfo.get(8).toString();
//               }
//            }
//            if ( reportIncomingParameters.get("CBOARP"+paramElement+"_excbox") != null
//                && ! "".equals(((String)reportIncomingParameters.get("CBOARP"+paramElement+"_excbox")).trim()) )
//                excludedParam = true;
//
//            if ( paramInfo.get(10).equals("NOT_SELECTED") )
//            {
//                if ( excludedParam )
//                    paramExcludedIncluded1 = "EXCLUDED";
//                else if (((String)paramInfo.get(8)).equalsIgnoreCase(parameterValue) )
//                    paramExcludedIncluded1 = "NOT_SELECTED";
//                else
//                    paramExcludedIncluded1 = "INCLUDED";
//            }
//            else
//                paramExcludedIncluded1 = paramInfo.get(10).toString();
//
//            paramInfo.set(8, parameterValue);
//            paramValueList.add(paramInfo.get(1) + ":" + parameterValue);
//            paramInfo.set(9,"CBOARP" + paramElement);
//            paramInfo.set(10,paramExcludedIncluded1);
//            reportParameters.put(paramElement, paramInfo);
//            reportParametersValues.put(paramElement, String.valueOf(paramInfo.get(8)));
//            this.completeUrl += ";" + String.valueOf(paramInfo.get(9)) + "=" + String.valueOf(paramInfo.get(8));
//        }
//
//
//        ArrayList<String> rowviewbys1 = new ArrayList<String>();
//        String nextViewBy = "";
//
//        if(!this.overrideViewBys1.equalsIgnoreCase("") && this.overrideViewBys1 != null && this.overrideViewBys1.equalsIgnoreCase("isChecked"))
//        {
//            boolean viewbysAvailable = false;
//
//            Set<String> repViewByIds = reportViewByMain.keySet();
//            String viewByElementId;
//            boolean colDrillSet = false;
//
//            for( String viewById : repViewByIds )
//                if (reportIncomingParameters.get("CBOVIEW_BY" + viewById) != null){
//                    rowviewbys1.add(reportIncomingParameters.get("CBOVIEW_BY" + viewById));
//                    viewbysAvailable = true;
//                }
//
//            if (viewbysAvailable){
//                reportRowViewbyValues.clear();
//                reportColViewbyValues.clear();
//                if(reportColViewbys!=null)
//                    reportColViewbys.clear();
//                isTimeDrill = false;
//
//                for( String viewById : repViewByIds )
//                {
//                    viewByElementId = reportIncomingParameters.get("CBOVIEW_BY" + viewById);
//                    if (reportIncomingParameters.get("CBOVIEW_BY" + viewById) != null)
//                    {
//                        if ( isRowViewBy(viewById) )
//                        {
//                            reportRowViewbyValues.add(reportIncomingParameters.get("CBOVIEW_BY" + viewById));
//                        }
//                        else
//                        {
//                            reportColViewbyValues.add(reportIncomingParameters.get("CBOVIEW_BY" + viewById));
//                            reportColViewbys.add("A_"+reportIncomingParameters.get("CBOVIEW_BY" + viewById));
//                        }
//                        ArrayList<String> viewBysList = reportViewByMain.get(viewById);
//                        viewBysList.set(2, viewByElementId);
//
//                        this.completeUrl += ";" + "CBOVIEW_BY" + viewById + "=" + viewByElementId;
//
//                    }
//                }
//            }
//        }
//         ArrayList<String> timeInfo1;
//
//        Set<String> timeKeySet = timeDetailsMap.keySet();
//
//
//        for ( String timeType : timeKeySet )
//        {
//            timeInfo1 = timeDetailsMap.get(timeType);
//            if (timeType.equalsIgnoreCase("AS_OF_DATE") && (reportIncomingParameters.get("DDrill") != null && reportIncomingParameters.get("DDrill").toString().equalsIgnoreCase("Y")))
//            {
//                if ((reportIncomingParameters.get("DrillYear") != null)) {
//                    isTimeDrill = true;
//                    timeInfo1.set(0,getYearDrill(reportIncomingParameters.get("DrillYear").toString()));
//                } else if ((reportIncomingParameters.get("DrillQtr") != null)) {
//                    isTimeDrill = true;
//                    timeInfo1.set(0,getQtrDrill(reportIncomingParameters.get("DrillQtr").toString()));
//                } else if ((reportIncomingParameters.get("DrillMonth") != null)) {
//                    isTimeDrill = true;
//                    timeInfo1.set(0,getMonthDrill(reportIncomingParameters.get("DrillMonth").toString()));
//                } else if ((reportIncomingParameters.get("DrillWeek") != null)) {
//                    isTimeDrill = true;
//                    timeInfo1.set(0,getWeekDrill(reportIncomingParameters.get("DrillWeek").toString()));
//                } else {
//                    timeInfo1.set(0,reportIncomingParameters.get(("CBO_" + timeType)));
//                }
//             }
//            else{
//               // 
//               if(reportIncomingParameters.get(("CBO_" + timeType))!=null)
//               timeInfo1.set(0,reportIncomingParameters.get(("CBO_" + timeType)));
//        }
//        }
//
//                String value="";
//                String valu="";
//                String mont="";
//                String CurrValue="";
//                
//
//        if(dateFormat==null){
//
//          if(timeDetailsMap.get("AS_OF_DATE")!=null){
//                value=timeDetailsMap.get("AS_OF_DATE").get(0);
//                if(value!=null){
//                valu=value.substring(0, 2);
//                mont=value.substring(3, 5);
//                CurrValue=mont.concat("/").concat(valu).concat(value.substring(5));
//                timeDetailsMap.get("AS_OF_DATE").set(0, CurrValue);
//            }
//          }
//          //periodate
////                 if(reportIncomingParameters.get("perioddate")!=null && reportIncomingParameters.get("datetext")!="" ){
////             value=reportIncomingParameters.get("perioddate");
////               if(!value.contains("/") && !value.isEmpty() )
////             {
////              String  formatdate=parseDate(value);
////                valu=formatdate.substring(0, 2);
////                mont=formatdate.substring(3, 5);
////                CurrValue=mont.concat("/").concat(valu).concat(formatdate.substring(5));
////
////            }
////
////
////                timeDetailsMap.get("AS_OF_DATE").set(0, CurrValue);
////        }
//          ////
//          if(timeDetailsMap.get("AS_OF_DATE1")!=null){
//                 value=timeDetailsMap.get("AS_OF_DATE1").get(0);
//                 if(value!=null){
//                valu=value.substring(0, 2);
//                mont=value.substring(3, 5);
//                CurrValue=mont.concat("/").concat(valu).concat(value.substring(5));
//                timeDetailsMap.get("AS_OF_DATE1").set(0, CurrValue);
//              }
//          }
//          //fromdate
////          if(timeDetailsMap.get("fromdate")!=null ){
////             value=timeDetailsMap.get("fromdate").get(0);
////             if(!value.contains("/")&& !value.isEmpty() )
////             {
////              String  formatdate=parseDate(value);
////                valu=formatdate.substring(0, 2);
////                mont=formatdate.substring(3, 5);
////                CurrValue=mont.concat("/").concat(valu).concat(formatdate.substring(5));
////                 }
////
////
////                timeDetailsMap.get("AS_OF_DATE1").set(0, CurrValue);
////        }
//          //
//          if(timeDetailsMap.get("CBO_AS_OF_DATE2")!=null){
//               value=timeDetailsMap.get("CBO_AS_OF_DATE2").get(0);
//               if(value!=null){
//                valu=value.substring(0, 2);
//                mont=value.substring(3, 5);
//                CurrValue=mont.concat("/").concat(valu).concat(value.substring(5));
//                timeDetailsMap.get("AS_OF_DATE2").set(0, CurrValue);
//              }
//          }
////          //todate
////               if(timeDetailsMap.get("todate")!=null && reportIncomingParameters.get("datetext")!=""){
////             value=reportIncomingParameters.get("todate");
////             if(!value.contains("/")&& !value.isEmpty() )
////             {
////              String  formatdate=parseDate(value);
////                valu=formatdate.substring(0, 2);
////                mont=formatdate.substring(3, 5);
////                CurrValue=mont.concat("/").concat(valu).concat(formatdate.substring(5));
////              }
////                timeDetailsMap.get("AS_OF_DATE2").set(0, CurrValue);
////        }
////          ///
//          if(timeDetailsMap.get("CBO_CMP_AS_OF_DATE1")!=null){
//               value=timeDetailsMap.get("CBO_CMP_AS_OF_DATE1").get(0);
//               if(value!=null){
//                valu=value.substring(0, 2);
//                mont=value.substring(3, 5);
//                CurrValue=mont.concat("/").concat(valu).concat(value.substring(5));
//                timeDetailsMap.get("CMP_AS_OF_DATE1").set(0, CurrValue);
//              }
//          }
////          //comparefrom
////                  if(reportIncomingParameters.get("comparefrom")!=null && reportIncomingParameters.get("datetext")!=""){
////             value=reportIncomingParameters.get("comparefrom");
////             if(!value.contains("/")&& !value.isEmpty() )
////             {
////              String  formatdate=parseDate(value);
////                valu=formatdate.substring(0, 2);
////                mont=formatdate.substring(3, 5);
////                CurrValue=mont.concat("/").concat(valu).concat(formatdate.substring(5));
////              }
////
////
////                timeDetailsMap.get("CMP_AS_OF_DATE1").set(0, CurrValue);
////        }
//          //
//          if(timeDetailsMap.get("CBO_CMP_AS_OF_DATE2")!=null){
//               value=timeDetailsMap.get("CBO_CMP_AS_OF_DATE2").get(0);
//               if(value!=null){
//                valu=value.substring(0, 2);
//                mont=value.substring(3, 5);
//                CurrValue=mont.concat("/").concat(valu).concat(value.substring(5));
//                timeDetailsMap.get("CMP_AS_OF_DATE2").set(0, CurrValue);
//              }
//          }
//
//                //compareto
////                if(reportIncomingParameters.get("compareto")!=null && reportIncomingParameters.get("datetext")!=""){
////             value=reportIncomingParameters.get("compareto");
////             if(!value.contains("/")&& !value.isEmpty() )
////             {
////              String  formatdate=parseDate(value);
////                valu=formatdate.substring(0, 2);
////                mont=formatdate.substring(3, 5);
////                CurrValue=mont.concat("/").concat(valu).concat(formatdate.substring(5));
////        }
////
////
////                timeDetailsMap.get("CMP_AS_OF_DATE2").set(0, CurrValue);
////        }
//
//                ////
//        }
//        else if(dateFormat.equalsIgnoreCase("dd/mm/yy")){
//             if(timeDetailsMap.get("CBO_AS_OF_DATE")!=null){
//                value=timeDetailsMap.get("CBO_AS_OF_DATE").get(0);
//                valu=value.substring(0, 2);
//                mont=value.substring(3, 5);
//                CurrValue=mont.concat("/").concat(valu).concat(value.substring(5));
//                timeDetailsMap.get("AS_OF_DATE").set(0, CurrValue);
//            }
//          if(timeDetailsMap.get("AS_OF_DATE1")!=null){
//                 value=timeDetailsMap.get("CBO_AS_OF_DATE1").get(0);
//                valu=value.substring(0, 2);
//                mont=value.substring(3, 5);
//                CurrValue=mont.concat("/").concat(valu).concat(value.substring(5));
//                timeDetailsMap.get("AS_OF_DATE1").set(0, CurrValue);
//              }
//          if(timeDetailsMap.get("CBO_AS_OF_DATE2")!=null){
//               value=timeDetailsMap.get("CBO_AS_OF_DATE2").get(0);
//                valu=value.substring(0, 2);
//                mont=value.substring(3, 5);
//                CurrValue=mont.concat("/").concat(valu).concat(value.substring(5));
//                timeDetailsMap.get("AS_OF_DATE2").set(0, CurrValue);
//              }
//          if(timeDetailsMap.get("CBO_CMP_AS_OF_DATE1")!=null){
//               value=timeDetailsMap.get("CBO_CMP_AS_OF_DATE1").get(0);
//                valu=value.substring(0, 2);
//                mont=value.substring(3, 5);
//                CurrValue=mont.concat("/").concat(valu).concat(value.substring(5));
//                timeDetailsMap.get("CMP_AS_OF_DATE1").set(0, CurrValue);
//              }
//          if(timeDetailsMap.get("CBO_CMP_AS_OF_DATE2")!=null){
//               value=timeDetailsMap.get("CBO_CMP_AS_OF_DATE2").get(0);
//                valu=value.substring(0, 2);
//                mont=value.substring(3, 5);
//                CurrValue=mont.concat("/").concat(valu).concat(value.substring(5));
//                timeDetailsMap.get("CMP_AS_OF_DATE2").set(0, CurrValue);
//              }
//        }
//
//
//        if ( ! timeDetailsMap.isEmpty() )
//        {
//             if (timeDetailsArray.get(0).toString().equalsIgnoreCase("Year") && timeDetailsArray.get(1).toString().equalsIgnoreCase("PRG_YEAR_CMP")) {
//                timeInfo1 = timeDetailsMap.get("AS_OF_YEAR");
//                timeDetailsArray.set(2,timeInfo1.get(0));
//                timeInfo1 =  timeDetailsMap.get("AS_OF_YEAR1");
//                if (timeInfo1 != null) {
//                    timeDetailsArray.set(3,timeInfo1.get(0));
//                } else {
//                    timeDetailsArray.set(3,null);
//                }
//
//            }else if (timeDetailsArray.get(0).toString().equalsIgnoreCase("DAY") && timeDetailsArray.get(1).toString().equalsIgnoreCase("PRG_COHORT")) {
//                timeInfo1 = timeDetailsMap.get("AS_OF_DATE1");
//                timeDetailsArray.set(2,timeInfo1.get(0));
//
//                timeInfo1 = timeDetailsMap.get("AS_OF_DATE2");
//                timeDetailsArray.set(3,timeInfo1.get(0));
//                if (timeDetailsMap.get("CMP_AS_OF_DATE1") != null) {
//                    timeInfo1 = timeDetailsMap.get("CMP_AS_OF_DATE1");
//                    timeDetailsArray.set(4,timeInfo1.get(0));
//                } else {
//                    timeDetailsArray.set(4,null);
//                }
//                if (timeDetailsMap.get("CMP_AS_OF_DATE2") != null) {
//                    timeInfo1 = timeDetailsMap.get("CMP_AS_OF_DATE2");
//                    timeDetailsArray.set(5,timeInfo1.get(0));
//
//                } else {
//                    timeDetailsArray.set(5,null);
//                }
//                if(timeDetailsMap.get("PRG_PERIOD_TYPE")!=null){
//                    timeInfo1 =  timeDetailsMap.get("PRG_PERIOD_TYPE");
//                    timeDetailsArray.set(6,timeInfo1.get(0));
//                }else{
//                    timeDetailsArray.set(6,"PERIOD");
//                }
//            }
//             else if (timeDetailsArray.get(0).toString().equalsIgnoreCase("DAY") && timeDetailsArray.get(1).toString().equalsIgnoreCase("PRG_DATE_RANGE")) {
//                timeInfo1 = timeDetailsMap.get("AS_OF_DATE1");
//                timeDetailsArray.set(2,timeInfo1.get(0));
//
//                timeInfo1 = timeDetailsMap.get("AS_OF_DATE2");
//                timeDetailsArray.set(3,timeInfo1.get(0));
//                if (timeDetailsMap.get("CMP_AS_OF_DATE1") != null) {
//                    timeInfo1 = timeDetailsMap.get("CMP_AS_OF_DATE1");
//                    timeDetailsArray.set(4,timeInfo1.get(0));
//                } else {
//                    timeDetailsArray.set(4,null);
//                }
//                if (timeDetailsMap.get("CMP_AS_OF_DATE2") != null) {
//                    timeInfo1 = timeDetailsMap.get("CMP_AS_OF_DATE2");
//                    timeDetailsArray.set(5,timeInfo1.get(0));
//
//                }
//               else {
//                    timeDetailsArray.set(5,null);
//                }
//            } else if (timeDetailsArray.get(0).toString().equalsIgnoreCase("DAY") && timeDetailsArray.get(1).toString().equalsIgnoreCase("PRG_MONTH_RANGE")) {
//                timeInfo1 =  timeDetailsMap.get("AS_OF_DMONTH1");
//                timeDetailsArray.set(2,timeInfo1.get(0));
//                timeInfo1 =  timeDetailsMap.get("AS_OF_DMONTH2");
//                timeDetailsArray.set(3,timeInfo1.get(0));
//                if (timeDetailsMap.get("CMP_AS_OF_DMONTH1") != null) {
//                    timeInfo1 =  timeDetailsMap.get("CMP_AS_OF_DMONTH1");
//                    timeDetailsArray.set(4,timeInfo1.get(0));
//                } else {
//                    timeDetailsArray.set(4,null);
//                }
//                if (timeDetailsMap.get("CMP_AS_OF_DMONTH2") != null) {
//                    timeInfo1 =  timeDetailsMap.get("CMP_AS_OF_DMONTH2");
//                    timeDetailsArray.set(5,timeInfo1.get(0));
//
//                } else {
//                    timeDetailsArray.set(5,null);
//                }
//
//            } else if (timeDetailsArray.get(0).toString().equalsIgnoreCase("DAY") && timeDetailsArray.get(1).toString().equalsIgnoreCase("PRG_QUARTER_RANGE")) {
//                timeInfo1 =  timeDetailsMap.get("AS_OF_DQUARTER1");
//                timeDetailsArray.set(2,timeInfo1.get(0));
//                timeInfo1 =  timeDetailsMap.get("AS_OF_DQUARTER2");
//                timeDetailsArray.set(3,timeInfo1.get(0));
//                if (timeDetailsMap.get("CMP_AS_OF_DQUARTER1") != null) {
//                    timeInfo1 =  timeDetailsMap.get("CMP_AS_OF_DQUARTER1");
//                    timeDetailsArray.set(4,timeInfo1.get(0));
//                } else {
//                    timeDetailsArray.set(4,null);
//                }
//                if (timeDetailsMap.get("CMP_AS_OF_DQUARTER2") != null) {
//                    timeInfo1 =  timeDetailsMap.get("CMP_AS_OF_DQUARTER2");
//                    timeDetailsArray.set(5,timeInfo1.get(0));
//                } else {
//                    timeDetailsArray.set(5,null);
//                }
//
//            } else if (timeDetailsArray.get(0).toString().equalsIgnoreCase("DAY") && timeDetailsArray.get(1).toString().equalsIgnoreCase("PRG_YEAR_RANGE")) {
//                timeInfo1 =  timeDetailsMap.get("AS_OF_DYEAR1");
//                timeDetailsArray.set(2,timeInfo1.get(0));
//                timeInfo1 =  timeDetailsMap.get("AS_OF_DYEAR2");
//                timeDetailsArray.set(3,timeInfo1.get(0));
//                if (timeDetailsMap.get("CMP_AS_OF_DYEAR1") != null) {
//                    timeInfo1 =  timeDetailsMap.get("CMP_AS_OF_DYEAR1");
//                    timeDetailsArray.set(4,timeInfo1.get(0));
//                } else {
//                    timeDetailsArray.set(4,null);
//                }
//                if (timeDetailsMap.get("CMP_AS_OF_DYEAR2") != null) {
//                    timeInfo1 = (ArrayList) timeDetailsMap.get("CMP_AS_OF_DYEAR2");
//                    timeDetailsArray.set(5,timeInfo1.get(0));
//
//                } else {
//                    timeDetailsArray.set(5,null);
//                }
//
//            } else if (timeDetailsArray.get(0).toString().equalsIgnoreCase("DAY") && timeDetailsArray.get(1).toString().equalsIgnoreCase("PRG_DAY_ROLLING")) {
//                timeInfo1 = timeDetailsMap.get("AS_OF_DATE");
//                timeDetailsArray.set(2,timeInfo1.get(0));
//
//                timeInfo1 =  timeDetailsMap.get("PRG_DAY_ROLLING");
//                timeDetailsArray.set(3,timeInfo1.get(0));
//
//
//            } else if (timeDetailsArray.get(0).toString().equalsIgnoreCase("DAY") && timeDetailsArray.get(1).toString().equalsIgnoreCase("PRG_STD")) {
//                timeInfo1 =  timeDetailsMap.get("AS_OF_DATE");
//                timeDetailsArray.set(2,timeInfo1.get(0));
//
//                timeInfo1 =  timeDetailsMap.get("PRG_PERIOD_TYPE");
//                timeDetailsArray.set(3,timeInfo1.get(0));
//
//                if (timeDetailsMap.get("PRG_COMPARE") != null) {
//                    timeInfo1 =  timeDetailsMap.get("PRG_COMPARE");
//                    timeDetailsArray.set(4,timeInfo1.get(0));
//                } else {
//                    timeDetailsArray.set(4,"Last Period");
//                }
//            } else if (timeDetailsArray.get(0).toString().equalsIgnoreCase("Month") && timeDetailsArray.get(1).toString().equalsIgnoreCase("PRG_MONTH_CMP")) {
//                timeInfo1 =  timeDetailsMap.get("AS_OF_MONTH");
//                timeDetailsArray.set(2,timeInfo1.get(0));
//
//                timeInfo1 =  timeDetailsMap.get("AS_OF_MONTH1");
//                timeDetailsArray.set(3,timeInfo1.get(0));
//
//            } else if (timeDetailsArray.get(0).toString().equalsIgnoreCase("MONTH") && timeDetailsArray.get(1).toString().equalsIgnoreCase("PRG_STD")) {
//                timeInfo1 =  timeDetailsMap.get("AS_OF_MONTH");
//                timeDetailsArray.set(2,timeInfo1.get(0));
//
//                timeInfo1 =  timeDetailsMap.get("PRG_PERIOD_TYPE");
//                timeDetailsArray.set(3,timeInfo1.get(0));
//
//                if (timeDetailsMap.get("PRG_COMPARE") != null) {
//                    timeInfo1 =  timeDetailsMap.get("PRG_COMPARE");
//                    timeDetailsArray.set(4,timeInfo1.get(0));
//                } else {
//                    timeDetailsArray.set(4,"Last Period");
//                }
//            }
//        }
//
//
//        this.resetPath = ctxPath + "/reportViewer.do?reportBy=viewReport&REPORTID=" + this.reportId + "&action=reset";
//        this.completeUrl = ctxPath + "/reportViewer.do?reportBy=viewReport;REPORTID=" + this.reportId + this.completeUrl;
//        }

    }///end of public void getParamMetaData()

    public String resolveTimeDrill(ArrayList timeDetailsArray) {
        String drillUrl = "";
        String sqlstr = "";

        if (timeDetailsArray.get(0).toString().equalsIgnoreCase("DAY") && timeDetailsArray.get(1).toString().equalsIgnoreCase("PRG_STD")) {

            if (timeDetailsArray.get(3).toString().equalsIgnoreCase("YEAR")) {

                drillUrl = "";
                drillUrl += "&CBO_PRG_PERIOD_TYPE=Qtr";
                drillUrl += "&DDrill=Y&DrillYear=";


            } else if (timeDetailsArray.get(3).toString().equalsIgnoreCase("QTR")) {

                drillUrl = "";
                drillUrl += "&CBO_PRG_PERIOD_TYPE=Month";
                drillUrl += "&DDrill=Y&DrillQtr=";

            } else if (timeDetailsArray.get(3).toString().equalsIgnoreCase("MONTH")) {

                drillUrl = "";
                drillUrl += "&CBO_PRG_PERIOD_TYPE=Day";
                drillUrl += "&DDrill=Y&DrillMonth=";

            } else if (timeDetailsArray.get(3).toString().equalsIgnoreCase("Week")) {
                drillUrl = "&CBO_PRG_PERIOD_TYPE=Day";
                drillUrl += "&DDrill=Y&DrillWeek=";
            } else {
                drillUrl = "";
                drillUrl += "&DrillDate=";
            }

            //("AS_OF_DATE");
            //

            //("PRG_PERIOD_TYPE");


            //("PRG_COMPARE");

        } else {
            drillUrl = "&cbodonotuser=";
        }


        return (drillUrl);
    }

    public String getYearDrill(String getYear) {
        PbReturnObject retObj = null;
        Object[] Obj = null;
        String result = null;
        Connection conn = null;
        String[] colNames;
        String finalQuery = "";
        PbDb db = new PbDb();
        getYear = getYear.replace("]", "").replace("[", "").replace("\"", "");
//        getConnectionType gettypeofconn = new getConnectionType();
//        String connType = gettypeofconn.getConTypeByElementId(elementId);
        if (ProgenConnection.SQL_SERVER.equalsIgnoreCase(connectionType)) {
            finalQuery = " Select convert(varchar,cy_end_date,110) ed_date from pr_day_denom where CY_CUST_NAME = '" + getYear + "' ";

        } else if (ProgenConnection.MYSQL.equalsIgnoreCase(connectionType)) {
            finalQuery = " Select date_format(cy_end_date,'%m/%d/%Y') ed_date from pr_day_denom where CM_CUST_NAME = '" + getYear + "' ";
        } else {
            finalQuery = " Select to_char(cy_end_date,'MM/DD/YYYY') ed_date from pr_day_denom where CY_CUST_NAME = '" + getYear + "' ";

        }
        try {


            conn = getConnection(connectionId);//pass element id to get connection
            retObj = db.execSelectSQL(finalQuery, conn);

        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }

        colNames = retObj.getColumnNames();
        int psize = retObj.getRowCount();

        if (psize > 0) {
            result = retObj.getFieldValueString(0, colNames[0]);
        }



        return result;
    }

    public String getQtrDrill(String getYear) {
        PbReturnObject retObj = null;
        Object[] Obj = null;
        String result = null;
        String[] colNames;
        Connection conn = null;
        String finalQuery = "";
        PbDb db = new PbDb();
        getYear = getYear.replace("]", "").replace("[", "").replace("\"", "");
//        getConnectionType gettypeofconn = new getConnectionType();
//        String connType = gettypeofconn.getConTypeByElementId(elementId);
        if (ProgenConnection.SQL_SERVER.equalsIgnoreCase(connectionType)) {
            finalQuery = " Select convert(varchar,cq_end_date,110) ed_date from pr_day_denom where CQ_CUST_NAME= '" + getYear + "' ";

        } else if (ProgenConnection.MYSQL.equalsIgnoreCase(connectionType)) {
            finalQuery = " Select date_format(cq_end_date,'%m/%d/%Y') ed_date from pr_day_denom where CM_CUST_NAME = '" + getYear + "' ";
        } else {
            finalQuery = " Select to_char(cq_end_date,'MM/DD/YYYY') ed_date from pr_day_denom where CQ_CUST_NAME = '" + getYear + "' ";

        }

        //String finalQuery = " Select to_char(cq_end_date,'MM/DD/YYYY') ed_date from pr_day_denom where CQ_CUST_NAME = '" + getYear + "' ";
        try {


            conn = getConnection(connectionId);//pass element id to get connection
            retObj = db.execSelectSQL(finalQuery, conn);

        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }

        colNames = retObj.getColumnNames();
        int psize = retObj.getRowCount();

        if (psize > 0) {
            result = retObj.getFieldValueString(0, colNames[0]);
        }



        return result;
    }

    public String getMonthDrill(String getYear) {
        PbReturnObject retObj = null;
        Object[] Obj = null;
        String result = null;
        String[] colNames = null;
        Connection conn = null;
        String finalQuery = "";
        PbDb db = new PbDb();
        int psize = 0;
        getYear = getYear.replace("]", "").replace("[", "").replace("\"", "");


        if (ProgenConnection.SQL_SERVER.equalsIgnoreCase(connectionType)) {
            finalQuery = " Select convert(varchar,cm_end_date,110) ed_date from pr_day_denom where CM_CUST_NAME = '" + getYear + "' ";

        } else if (ProgenConnection.MYSQL.equalsIgnoreCase(connectionType)) {
            finalQuery = " Select date_format(cm_end_date,'%m/%d/%Y') ed_date from pr_day_denom where CM_CUST_NAME = '" + getYear + "' ";
        } else {
            finalQuery = " Select to_char(cm_end_date,'MM/DD/YYYY') ed_date from pr_day_denom where CM_CUST_NAME = '" + getYear + "' ";

        }
        //String finalQuery = " Select to_char(cm_end_date,'MM/DD/YYYY') ed_date from pr_day_denom where CM_CUST_NAME = '" + getYear + "' ";
//        
        try {


            conn = getConnection(connectionId);//pass element id to get connection
            retObj = db.execSelectSQL(finalQuery, conn);

        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        //Modified By Mayank
        if (retObj != null) {
            colNames = retObj.getColumnNames();

            psize = retObj.getRowCount();
            result = retObj.getFieldValueString(0, colNames[0]);
        }



        return result;
    }

    public String getMonthDrillForOneview(String getYear, String elemId) {
        PbReturnObject retObj = null;
        Object[] Obj = null;
        String result = null;
        String[] colNames;
        Connection conn = null;
        String finalQuery = "";
        PbDb db = new PbDb();
        getYear = getYear.replace("]", "").replace("[", "").replace("\"", "");


        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
            finalQuery = " Select convert(varchar,cm_end_date,110) ed_date from pr_day_denom where CM_CUST_NAME = '" + getYear + "' ";

        } else if (ProgenConnection.MYSQL.equalsIgnoreCase(connectionType)) {
            finalQuery = " Select date_format(cm_end_date,'%m/%d/%Y') ed_date from pr_day_denom where CM_CUST_NAME = '" + getYear + "' ";
        } else {
            finalQuery = " Select to_char(cm_end_date,'MM/DD/YYYY') ed_date from pr_day_denom where CM_CUST_NAME = '" + getYear + "' ";

        }
        //String finalQuery = " Select to_char(cm_end_date,'MM/DD/YYYY') ed_date from pr_day_denom where CM_CUST_NAME = '" + getYear + "' ";
//        
        try {


            conn = getConnection(elemId);//pass element id to get connection
            retObj = db.execSelectSQL(finalQuery, conn);

        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }

        colNames = retObj.getColumnNames();
        int psize = retObj.getRowCount();

        if (psize > 0) {
            result = retObj.getFieldValueString(0, colNames[0]);
        }



        return result;
    }

    public String getWeekDrill(String getYear) {
        PbReturnObject retObj = null;
        Object[] Obj = null;
        String result = null;
        String[] colNames;
        Connection conn = null;
        String finalQuery = "";
        PbDb db = new PbDb();
        getYear = getYear.replace("]", "").replace("[", "").replace("\"", "");
        if (ProgenConnection.SQL_SERVER.equalsIgnoreCase(connectionType)) {
            finalQuery = " Select convert(varchar,cw_end_date,110) ed_date from pr_day_denom where CW_CUST_NAME = '" + getYear + "' ";

        } else if (ProgenConnection.MYSQL.equalsIgnoreCase(connectionType)) {
            finalQuery = " Select date_format(cw_end_date,'%m/%d/%Y') ed_date from pr_day_denom where CM_CUST_NAME = '" + getYear + "' ";
        } else {
            finalQuery = " Select to_char(cw_end_date,'MM/DD/YYYY') ed_date from pr_day_denom where CW_CUST_NAME = '" + getYear + "' ";

        }
        //String finalQuery = " Select to_char(cw_end_date,'MM/DD/YYYY') ed_date from pr_day_denom where CW_CUST_NAME = '" + getYear + "' ";
        try {


            conn = getConnection(connectionId);//pass element id to get connection
            retObj = db.execSelectSQL(finalQuery, conn);

        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }

        colNames = retObj.getColumnNames();
        int psize = retObj.getRowCount();

        if (psize > 0) {
            result = retObj.getFieldValueString(0, colNames[0]);
        }



        return result;
    }

    public void getReportQueryInfo() {//added by santhosh.kumar@progenbusiness.com
        String sqlstr = "";
        Object[] Obj = null;
        String finalQuery = "";
        PbReturnObject retObj = null;
        String[] dbColNames = null;
        String mapEnabled = null;
        String customSeq = null;
        String transposeFormat = null;
        String targetValue = null;
        String goalpercent = null;
        String goalTimeIndiv = null;
        String newProdValue = null;
        PbDb db = new PbDb();
        Gson gson = new Gson();
        try {
            sqlstr = getResourceBundle().getString("getReportQueryInfo");
            Obj = new Object[1];
            Obj[0] = this.reportId;

            finalQuery = db.buildQuery(sqlstr, Obj);

            retObj = db.execSelectSQL(finalQuery);

            reportQryElementIds = new ArrayList<String>();
            reportQryAggregations = new ArrayList<String>();
            reportQryColNames = new ArrayList<String>();

            dbColNames = retObj.getColumnNames();
            timePeriodsList = retObj.getFieldValueString(0, dbColNames[14]);

            for (int i = 0; i < retObj.getRowCount(); i++) {
                if (reportQryElementIds != null && !reportQryElementIds.contains(retObj.getFieldValueString(i, dbColNames[0]))) {
                    reportQryElementIds.add(retObj.getFieldValueString(i, dbColNames[0]));
                    reportQryAggregations.add(retObj.getFieldValueString(i, dbColNames[1]));
                    reportQryColNames.add(retObj.getFieldValueString(i, dbColNames[2]));
                    reportName = retObj.getFieldValueString(i, dbColNames[3]);
                    reportDesc = retObj.getFieldValueString(i, dbColNames[4]);
                    mapEnabled = retObj.getFieldValueString(i, dbColNames[5]);

                    if ((mapEnabled == null) || ("".equalsIgnoreCase(mapEnabled)) || ("Y".equalsIgnoreCase(mapEnabled))) {
                        isMapEnabled = true;
                    } else {
                        isMapEnabled = false;
                    }
                    if (reportQryColTypes == null) {
                        reportQryColTypes = new ArrayList<String>();
                    }

                    // reportQryColTypes.add(retObj.getFieldValueString(i, dbColNames[6]));
                    String formula = retObj.getFieldValueString(i, dbColNames[6]);
                    if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                        customSeq = retObj.getFieldValueClobString(i, dbColNames[7]);
                        transposeFormat = retObj.getFieldValueClobString(i, dbColNames[8]);
                        targetValue = retObj.getFieldValueClobString(i, dbColNames[9]);
                        goalpercent = retObj.getFieldValueClobString(i, dbColNames[10]);
                        goalTimeIndiv = retObj.getFieldValueClobString(i, dbColNames[11]);
                    } else {
                        customSeq = retObj.getFieldUnknown(i, 7);
                        transposeFormat = retObj.getFieldUnknown(i, 8);
                        targetValue = retObj.getFieldUnknown(i, 9);
                        goalpercent = retObj.getFieldUnknown(i, 10);
                        goalTimeIndiv = retObj.getFieldUnknown(i, 11);
                    }


                    newProdValue = retObj.getFieldValueString(i, dbColNames[12]);
                    msrDrillReportSelection = retObj.getFieldValueString(i, dbColNames[13]);

                    if (customSeq != null) {
                        this.customSequence = gson.fromJson(customSeq, new TypeToken<List<String>>() {
                        }.getType());
                    }
                    if (transposeFormat != null) {
                        this.transposeFormatMap = gson.fromJson(transposeFormat, new TypeToken<HashMap<String, ArrayList<String>>>() {
                        }.getType());
                    }
                    //modified by anitha for performance improval while opening report
                    // isFormulaMeasure.add(isColumnTypeAFormula(formula,retObj.getFieldValueString(i, dbColNames[0])));
                    if (formula.equalsIgnoreCase("calculated") || formula.equalsIgnoreCase("summarised") || formula.equalsIgnoreCase("summarized")) {
                        if (retObj.getFieldValueString(i, dbColNames[15]) == null || retObj.getFieldValueString(i, dbColNames[15]).equalsIgnoreCase("") || retObj.getFieldValueString(i, dbColNames[15]).equalsIgnoreCase("null")) {
                            isFormulaMeasure.add(true);
                        } else {
                            isFormulaMeasure.add(false);
                        }
                    } else {
                        isFormulaMeasure.add(false);
                    }
                    //end of code by anitha for performance improval while opening report
                    if (targetValue != null && !targetValue.isEmpty()) {
                        this.goalSeekBasicandAdhoc = gson.fromJson(targetValue, new TypeToken<HashMap<String, HashMap<String, ArrayList<String>>>>() {
                        }.getType());
                    }
                    if (goalSeekBasicandAdhoc.get("Basic") != null) {
                        goalseek.putAll(goalSeekBasicandAdhoc.get("Basic"));
                    }
                    if (goalSeekBasicandAdhoc.get("Adhoc") != null) {
                        percentColValues.putAll(goalSeekBasicandAdhoc.get("Adhoc"));
                    }

                    if (goalpercent != null) {
                        this.goalandPercent = gson.fromJson(goalpercent, new TypeToken<HashMap<String, ArrayList<String>>>() {
                        }.getType());
                    }

                    if (goalTimeIndiv != null && !goalTimeIndiv.isEmpty()) {
                        this.goalSeekTimeIndvidual = gson.fromJson(goalTimeIndiv, new TypeToken<HashMap<String, HashMap<String, ArrayList<String>>>>() {
                        }.getType());
                    }
                    if (goalSeekTimeIndvidual.get("IndvPercentvals") != null) {
                        timeBaseInvidualper.putAll(goalSeekTimeIndvidual.get("IndvPercentvals"));
                    }
                    if (goalSeekTimeIndvidual.get("IndvMeasvalues") != null) {
                        measurValues.putAll(goalSeekTimeIndvidual.get("IndvMeasvalues"));
                    }

                    if (newProdValue != null) {
                        this.prodAndViewName = gson.fromJson(newProdValue, new TypeToken<List<String>>() {
                        }.getType());
                    }
                }

            }

        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
    }

    private boolean isColumnTypeAFormula(String colType, String elementId) {
        String sqlQuery = getResourceBundle().getString("getRefDimTabId");
        String finalQuery;
        PbReturnObject retObject = new PbReturnObject();
        PbDb db = new PbDb();
        Object[] values = new Object[1];
        values[0] = elementId;
        finalQuery = db.buildQuery(sqlQuery, values);
        boolean flag = false;
        if (colType.equalsIgnoreCase("calculated") || colType.equalsIgnoreCase("summarised") || colType.equalsIgnoreCase("summarized")) {
            try {
                retObject = db.execSelectSQL(finalQuery);

            } catch (SQLException ex) {
                logger.error("Exception:", ex);
            }
            if (retObject!=null &&retObject.getRowCount() == 0) {
                flag = true;
            }
        } else {
            flag = false;
        }

        return flag;
    }

    public void updateReportQueryInfo(ArrayList originalReportQryAggregations, ArrayList originalReportQryColNames, ArrayList originalReportQryColTypes) {
        // reportQryElementIds.clear();
        reportQryAggregations.clear();
        reportQryColNames.clear();
        isFormulaMeasure.clear();
        String formula = "";

        // reportQryElementIds.addAll(originalReportQryElementIds);
        reportQryAggregations.addAll(originalReportQryAggregations);
        reportQryColNames.addAll(originalReportQryColNames);
        for (int i = 0; i < originalReportQryColTypes.size(); i++) {
            formula = originalReportQryColTypes.get(i).toString();
            if (i < reportQryElementIds.size())//added by dinanath
            {
                isFormulaMeasure.add(isColumnTypeAFormula(formula, reportQryElementIds.get(i)));
            }
        }
    }
    //used to get report table info i.e, what are the measures

    public void getReportTableInfo() {
        String sqlstr = "";
        Object[] Obj = null;
        String finalQuery = "";
        PbReturnObject retObj = null;
        String[] dbColNames = null;
        PbDb db = new PbDb();
        String summaerizedtablemsr = null;
        String crosstabrenamemeasure = null;
        Gson gson = new Gson();
        String tableType;
        try {

            //for table master level info
            sqlstr = getResourceBundle().getString("getReportTableMasterInfo");
            Obj = new Object[1];
            Obj[0] = this.reportId;

            finalQuery = db.buildQuery(sqlstr, Obj);
            //
            retObj = db.execSelectSQL(finalQuery);

            if (retObj != null && retObj.getRowCount() > 0) {
                dbColNames = retObj.getColumnNames();
                tableType = retObj.getFieldValueString(0, dbColNames[3]);
                if (tableType.equalsIgnoreCase("TopBottomTable")) {
                    isTopBottomTableEnable = true;
                } else {
                    isTopBottomTableEnable = false;
                }
                showTableTotals = Boolean.parseBoolean(retObj.getFieldValueString(0, dbColNames[5]));
                showTableSubTotals = Boolean.parseBoolean(retObj.getFieldValueString(0, dbColNames[6]));
                showTableAvg = Boolean.parseBoolean(retObj.getFieldValueString(0, dbColNames[7]));
                showTableMax = Boolean.parseBoolean(retObj.getFieldValueString(0, dbColNames[8]));
                showTableMin = Boolean.parseBoolean(retObj.getFieldValueString(0, dbColNames[9]));
                showTableCatMax = Boolean.parseBoolean(retObj.getFieldValueString(0, dbColNames[10]));
                showTableCatMin = Boolean.parseBoolean(retObj.getFieldValueString(0, dbColNames[11]));
                tableSymbols = retObj.getFieldValueString(0, dbColNames[12]);
                showAdvSearch = Boolean.parseBoolean(retObj.getFieldValueString(0, dbColNames[14]));
                drillAcrossSupported = Boolean.parseBoolean(retObj.getFieldValueString(0, dbColNames[17]));
                drillMeasure = Boolean.parseBoolean(retObj.getFieldValueString(0, dbColNames[18]));
                showTableCatAvg = Boolean.parseBoolean(retObj.getFieldValueString(0, dbColNames[19]));

                if (tableSymbols == null) {
                    tableSymbols = "";
                }
                if (RoundingPrecision == null) {
                    RoundingPrecision = new HashMap<String, Integer>();
                }

                defaultSortedColumn = retObj.getFieldValueString(0, dbColNames[13]);
                if (defaultSortedColumn == null) {
                    defaultSortedColumn = "";
                }
                tableDisplayRows = retObj.getFieldValueString(0, dbColNames[15]);
                if (tableDisplayRows == null) {
                    tableDisplayRows = "10";
                }
                if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                    if (retObj.getFieldValueClobString(0, dbColNames[16]) != null) {
                        tablePropertiesXML = new StringBuilder(retObj.getFieldValueClobString(0, dbColNames[16]));
                    } else {
                        tablePropertiesXML = new StringBuilder("<TableProperty></TableProperty>");
                    }
                } else {
                    if (retObj.getFieldUnknown(0, 16) != null) {
                        tablePropertiesXML = new StringBuilder(retObj.getFieldUnknown(0, 16));
                    } else {
                        tablePropertiesXML = new StringBuilder("<TableProperty></TableProperty>");
                    }
                }


                if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                    summaerizedtablemsr = retObj.getFieldUnknown(0, 20);
                } else {
                    summaerizedtablemsr = retObj.getFieldValueClobString(0, dbColNames[20]);
                }
                if (summaerizedtablemsr != null && !summaerizedtablemsr.equalsIgnoreCase("false") && !summaerizedtablemsr.toString().equalsIgnoreCase("null") && !summaerizedtablemsr.toString().equalsIgnoreCase("") && !summaerizedtablemsr.toString().equalsIgnoreCase(" ")) {
                    this.summerizedTableHashMap = gson.fromJson(summaerizedtablemsr, new TypeToken<HashMap<String, ArrayList<String>>>() {
                    }.getType());
                }
//            }
                if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                    crosstabrenamemeasure = retObj.getFieldUnknown(0, 21);
                } else {
                    crosstabrenamemeasure = retObj.getFieldValueClobString(0, dbColNames[21]);
                }
                if (crosstabrenamemeasure != null) {
                    this.crosstabmeasureHashMap = gson.fromJson(crosstabrenamemeasure, new TypeToken<HashMap<String, String>>() {
                    }.getType());
                }
                String topbottomMap = retObj.getFieldValueString(0, dbColNames[23]);
                if (topbottomMap != null && !topbottomMap.equalsIgnoreCase("false")) {
                    this.TopBottomTableHashMap = gson.fromJson(topbottomMap, new TypeToken<HashMap<String, String>>() {
                    }.getType());
                }

                if (retObj.getFieldValueString(0, dbColNames[24]) != null && retObj.getFieldValueString(0, dbColNames[24]).equalsIgnoreCase("fromExcel")) {
                    this.setIsExcelimportEnable(true);
                } else {
                    this.setIsExcelimportEnable(false);
                }
                if (this.isExcelimportEnable) {
                    this.importExcelFilePath = retObj.getFieldValueString(0, dbColNames[25]);
                }

            }
            //for table column level info
            sqlstr = getResourceBundle().getString("getReportTableInfo");
            Obj = new Object[1];
            Obj[0] = this.reportId;
            finalQuery = db.buildQuery(sqlstr, Obj);
//            
            retObj = db.execSelectSQL(finalQuery);
            tableElementIds = new ArrayList();
            tableColNames = new ArrayList();
            tableColTypes = new ArrayList();
            tableColDispTypes = new ArrayList();
//            columnSignType = new ArrayList();
            if (columnProperties == null) {
                columnProperties = new HashMap();
            }
            if (numberFormat == null) {
                numberFormat = new HashMap();
            }
            for (int i = 0; i < reportRowViewbyValues.size(); i++) {
                if (String.valueOf(reportRowViewbyValues.get(i)).equalsIgnoreCase("Time")) {
                    tableElementIds.add(String.valueOf(reportRowViewbyValues.get(i)));
                    tableColTypes.add("C");
                } else {
                    tableElementIds.add("A_" + String.valueOf(reportRowViewbyValues.get(i)));
                    tableColTypes.add("C");
                }
                if (i == 0) {
                    tableColDispTypes.add("H");//T for Text,H for Hyperlink
                } else {
                    tableColDispTypes.add("T");//T for Text,H for Hyperlink
                }
                if (String.valueOf(reportRowViewbyValues.get(i)).equalsIgnoreCase("Time")) {
                    tableColNames.add("Time");
                } else {
                    tableColNames.add(getParameterDispName(String.valueOf(reportRowViewbyValues.get(i))));
                }
            }

            dbColNames = retObj.getColumnNames();
            //ColorCodeMap

            //percent wise column changes
            int whatIfTrgetCounter = 1;
            ArrayList<String> summerizedQryeIds = new ArrayList<String>();
            ArrayList<String> summerizedQryColNames = new ArrayList<String>();

            for (int i = 0; i < retObj.getRowCount(); i++) {
                ArrayList singleColProp = new ArrayList();
                if (Boolean.parseBoolean(retObj.getFieldValueString(i, dbColNames[32]))) {
                    summerizedQryeIds.add(retObj.getFieldValueString(i, dbColNames[0]));
                    summerizedQryColNames.add(retObj.getFieldValueString(i, dbColNames[1]));
                } else if ("".equals(retObj.getFieldValueString(i, dbColNames[0]))) {
                    //col 14 will be element id of parent col and 15 will be formula type percentwise
//                    if(retObj.getFieldValueString(i, dbColNames[15]).equalsIgnoreCase(RTMeasureElement.WHATIFTARGET.getColumnType()) ){
//                     tableElementIds.add("A_WF_TRG_ "+whatIfTrgetCounter+ retObj.getFieldValueString(i, dbColNames[15]));
//                     whatIfTrgetCounter++;
//                    tableColTypes.add("N");
//                    }else{
                    if ("".equalsIgnoreCase(retObj.getFieldValueString(i, dbColNames[18]))) {
                        tableElementIds.add("A_" + retObj.getFieldValueString(i, dbColNames[14]) + retObj.getFieldValueString(i, dbColNames[15]));
                    } else {
                        tableElementIds.add(retObj.getFieldValueString(i, dbColNames[18]));
                    }
                    tableColTypes.add("N");

                    tableColNames.add(retObj.getFieldValueString(i, dbColNames[1]));
                    tableColDispTypes.add(retObj.getFieldValueString(i, dbColNames[3]));
                    // }
                    //hardcoding it to N
                } else {
                    tableElementIds.add("A_" + retObj.getFieldValueString(i, dbColNames[0]));
                    tableColTypes.add(retObj.getFieldValueString(i, dbColNames[2]));

                    tableColNames.add(retObj.getFieldValueString(i, dbColNames[1]));

                    tableColDispTypes.add(retObj.getFieldValueString(i, dbColNames[3]));
                }
//                tableColNames.add(retObj.getFieldValueString(i, dbColNames[1]));
//
//                tableColDispTypes.add(retObj.getFieldValueString(i, dbColNames[3]));
                singleColProp.add(retObj.getFieldValueString(i, dbColNames[5]));//indicates show Total 0
                singleColProp.add(retObj.getFieldValueString(i, dbColNames[6]));//indicates show sub Total 1
                singleColProp.add(retObj.getFieldValueString(i, dbColNames[7]));//indicates show avg Total 2
                singleColProp.add(retObj.getFieldValueString(i, dbColNames[9]));//indicates show  over all max 3
                singleColProp.add(retObj.getFieldValueString(i, dbColNames[8]));//indicates show over all min 4
                singleColProp.add(retObj.getFieldValueString(i, dbColNames[11]));//indicates show cat max 5
                singleColProp.add(retObj.getFieldValueString(i, dbColNames[10]));//indicates show cat min 6
                singleColProp.add(retObj.getFieldValueString(i, dbColNames[12]));

                if (retObj.getFieldValueString(i, dbColNames[20]) != null) {
                    singleColProp.add(retObj.getFieldValueString(i, dbColNames[20]));//indicates display symbol 7
                } else {
                    singleColProp.add(" ");
                }
                singleColProp.add(retObj.getFieldValueString(i, dbColNames[35]));//added by sruthi for nmuberformate
                if (retObj.getFieldValueString(i, dbColNames[19]) == null) {
                    indicatorMeasures.put("A_" + retObj.getFieldValueString(i, dbColNames[0]), "N");
                } else {
                    indicatorMeasures.put("A_" + retObj.getFieldValueString(i, dbColNames[0]), retObj.getFieldValueString(i, dbColNames[19]));
                }

                if (retObj.getFieldValueString(i, dbColNames[20]) == null) {
                    scriptIndicators.put("A_" + retObj.getFieldValueString(i, dbColNames[0]), "N");
                } else {
                    scriptIndicators.put("A_" + retObj.getFieldValueString(i, dbColNames[0]), retObj.getFieldValueString(i, dbColNames[20]));
                }

                if (retObj.getFieldValueString(i, dbColNames[21]) != null) {
                    scriptAligns.put("A_" + retObj.getFieldValueString(i, dbColNames[0]), retObj.getFieldValueString(i, dbColNames[21]));
                }

                if (retObj.getFieldValueString(i, dbColNames[22]) != null) {
                    measureTypes.put("A_" + retObj.getFieldValueString(i, dbColNames[0]), retObj.getFieldValueString(i, dbColNames[22]));
                }

                if (retObj.getFieldValueString(i, dbColNames[23]) != null) {
                    measureAligns.put("A_" + retObj.getFieldValueString(i, dbColNames[0]), retObj.getFieldValueString(i, dbColNames[23]));
                }

                if (retObj.getFieldValueString(i, dbColNames[24]) != null) {
                    timeConversion.put("A_" + retObj.getFieldValueString(i, dbColNames[0]), retObj.getFieldValueString(i, dbColNames[24]));
                }
                if (retObj.getFieldValueString(i, dbColNames[25]) != null) {
                    dateoptions.put("A_" + retObj.getFieldValueString(i, dbColNames[0]), retObj.getFieldValueString(i, dbColNames[25]));
                }
                if (retObj.getFieldValueString(i, dbColNames[26]) != null) {
                    dateSubStringValues.put("A_" + retObj.getFieldValueString(i, dbColNames[0]), retObj.getFieldValueString(i, dbColNames[26]));
                }
                if ("".equals(retObj.getFieldValueString(i, dbColNames[0]))) {
                    columnProperties.put("A_" + retObj.getFieldValueString(i, dbColNames[14]) + retObj.getFieldValueString(i, dbColNames[15]), singleColProp);
                } else {
                    columnProperties.put("A_" + retObj.getFieldValueString(i, dbColNames[0]), singleColProp);
                }
                if (retObj.getFieldValueString(i, dbColNames[16]) != null && !retObj.getFieldValueString(i, dbColNames[16]).equals("")) {
                    numberFormat.put("A_" + retObj.getFieldValueString(i, dbColNames[0]), retObj.getFieldValueString(i, dbColNames[16]));
                }
                if (retObj.getFieldValueString(i, dbColNames[17]) != null && !retObj.getFieldValueString(i, dbColNames[17]).equals("")) {
                    RoundingPrecision.put("A_" + retObj.getFieldValueString(i, dbColNames[0]), retObj.getFieldValueInt(i, dbColNames[17]));
                }
                if (retObj.getFieldValueString(i, dbColNames[27]) != null && !retObj.getFieldValueString(i, dbColNames[27]).equals("")) {
                    dateFormatt.put("A_" + retObj.getFieldValueString(i, dbColNames[0]), retObj.getFieldValueString(i, dbColNames[27]));
                }
                if (msrDrillReportSelection != null && msrDrillReportSelection.equalsIgnoreCase("multi report")) {
                    if (retObj.getFieldValueString(i, dbColNames[29]) != null && !retObj.getFieldValueString(i, dbColNames[29]).equals("")) {
                        reportDrillMap.put("A_" + retObj.getFieldValueString(i, dbColNames[0]), retObj.getFieldValueString(i, dbColNames[29]));
                    }
                } else {
                    if (retObj.getFieldValueString(i, dbColNames[28]) != null && !retObj.getFieldValueString(i, dbColNames[28]).equals("")) {
                        reportDrillMap.put("A_" + retObj.getFieldValueString(i, dbColNames[0]), retObj.getFieldValueString(i, dbColNames[28]));
                    }
                }
                //added by krishan pratap fortooltip
                if (retObj.getFieldValueString(i, dbColNames[36]) != null && !retObj.getFieldValueString(i, dbColNames[36]).equals("")) {
                    reportDrillMaptooltip.put("A_" + retObj.getFieldValueString(i, dbColNames[0]), retObj.getFieldValueString(i, dbColNames[36]));
                    String name = retObj.getFieldValueString(i, dbColNames[36]);
                    //  
                }

                if (retObj.getFieldValueString(i, "HIDE_MSR") != null && !retObj.getFieldValueString(i, "HIDE_MSR").equals("") && retObj.getFieldValueString(i, "HIDE_MSR").equalsIgnoreCase("true")) {
                    hideMeasures.add(retObj.getFieldValueString(i, dbColNames[0]));
                }
                //start of code by Nazneen for sub total deviation
                if (retObj.getFieldValueString(i, dbColNames[31]) != null && !retObj.getFieldValueString(i, dbColNames[31]).equals("")) {
                    subTotalDeviation.put("A_" + retObj.getFieldValueString(i, dbColNames[0]), retObj.getFieldValueString(i, dbColNames[31]));
                }
                //end of code by Nazneen for sub total deviation
                if (retObj.getFieldValueString(i, dbColNames[33]) != null && retObj.getFieldValueString(i, dbColNames[33]).equals("")) {
                    //start of code by bhargavi to handle null value
                    if (retObj.getFieldValueString(i, dbColNames[33]).equals("")) {
                        String dexclude = retObj.getFieldValueString(i, dbColNames[33]);
                        dexclude = "Exclude 0";
                        crosscolmap1.put("A_" + retObj.getFieldValueString(i, dbColNames[0]), dexclude);
                    }
                } else {
                    crosscolmap1.put("A_" + retObj.getFieldValueString(i, dbColNames[0]), retObj.getFieldValueString(i, dbColNames[33]));
                }
                //end of code by bhargavi
                //start of code by Nazneen for GT as Avg for Sum Measures
                if (retObj.getFieldValueString(i, dbColNames[34]) != null && !retObj.getFieldValueString(i, dbColNames[34]).equals("")) {
                    gtCTAvgType.put("A_" + retObj.getFieldValueString(i, dbColNames[0]), retObj.getFieldValueString(i, dbColNames[34]));
                }
                //end of code by Nazneen for GT as Avg for Sum Measures
//
//                String test = retObj.getFieldValueString(i, dbColNames[1]);
//                if (test.toUpperCase().contains("CHANGE")) {
////                    columnSignType.add(test);
//                    columnSignType.add("A_" + retObj.getFieldValueString(i, dbColNames[0]));
//                }
//                ColorCodeMap = buildColorCodeXML(retObj.getFieldValueClobString(i, dbColNames[13]), ColorCodeMap, "A_" + retObj.getFieldValueString(i, dbColNames[0]));


                //write code to read color code xml and build ColorCodeMap
            }
            if (summerizedQryeIds != null && !summerizedQryeIds.isEmpty()) {
                ArrayList<String> summerizedQryAggregations = new ArrayList<String>();
                ArrayList<String> summerizedQryColTypes = new ArrayList<String>();
                ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();
                summerizedQryAggregations = reportTemplateDAO.getReportQryAggregations((ArrayList<String>) summerizedQryeIds);
//                    summerizedReportQryColNames = reportTemplateDAO.getReportQryColNames();
                summerizedQryColTypes = reportTemplateDAO.getReportQryColTypes();
                if (summerizedTableHashMap != null) {
                    this.summerizedTableHashMap.put("summerizedQryeIds", summerizedQryeIds);
                    this.summerizedTableHashMap.put("summerizedQryAggregations", summerizedQryAggregations);
                    this.summerizedTableHashMap.put("summerizedQryColNames", summerizedQryColNames);
                    this.summerizedTableHashMap.put("summerizedQryColTypes", summerizedQryColTypes);
                }
            }
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
    }

    public String getParameterDispName(String paramElementId) {
        String paramName = "";
        if (reportParameters != null) {
            ArrayList paramInfo = (ArrayList) reportParameters.get(paramElementId);
            if (paramInfo != null) {
                paramName = String.valueOf(paramInfo.get(1));
            }
        }
        return paramName;
    }

    public void getParamMetaDataForReportDesigner() {

        PbReturnObject retObj = null;
        Object[] Obj = null;
        String[] colNames;
        String temp;
        String finalQuery = "";
        reportParameters = new LinkedHashMap();
        PbDb db = new PbDb();
        //This will get parameters hashMap if Report Id is provided

        String sqlstr = "";

        try {
            sqlstr = getResourceBundle().getString("getParamMetaDataQuery1");
            Obj = new Object[1];
            if (this.reportId == null || this.reportId.equals("")) {
                Obj[0] = "0";
            } else {
                Obj[0] = this.reportId;
            }
            finalQuery = db.buildQuery(sqlstr, Obj);

            retObj = db.execSelectSQL(finalQuery);

        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        colNames = retObj.getColumnNames();
        int psize = retObj.getRowCount();

        if (psize > 0) {
            for (int looper = 0; looper < psize; looper++) {
                ArrayList paramInfo = new ArrayList();
                paramInfo.add(retObj.getFieldValueString(looper, colNames[0]));
                paramInfo.add(retObj.getFieldValueString(looper, colNames[1]));
                paramInfo.add(retObj.getFieldValueString(looper, colNames[2]));
                paramInfo.add(retObj.getFieldValueString(looper, colNames[3]));
                paramInfo.add(retObj.getFieldValueString(looper, colNames[4]));
                paramInfo.add(retObj.getFieldValueString(looper, colNames[5]));
                paramInfo.add(retObj.getFieldValueString(looper, colNames[6]));
                paramInfo.add(retObj.getFieldValueString(looper, colNames[7]));
                if (reportIncomingParameters.get(("CBOARP" + (retObj.getFieldValueString(looper, colNames[0])))) == null) {
                    temp = ("CBOARP" + retObj.getFieldValueString(looper, colNames[0])).trim();
                    if (temp.equalsIgnoreCase("AS_OF_DATE") || temp.equalsIgnoreCase("AS_OF_DATE1") || temp.equalsIgnoreCase("AS_OF_DATE") || temp.equalsIgnoreCase("AS_OF_DATE1") || temp.equalsIgnoreCase("AS_OF_MONTH") || temp.equalsIgnoreCase("AS_OF_MONTH1")) {
                        //Set time valuesBut we have seprate hashMap for Time
                    } else {
                        paramInfo.add(retObj.getFieldValueString(looper, colNames[8]));
                    }
                } else {
                    paramInfo.add(retObj.getFieldValueString(looper, String.valueOf(reportIncomingParameters.get("CBOARP" + (retObj.getFieldValueString(looper, colNames[0]))))));
                }

                ////Adding CBONAME --CBOAPP for Report Parameters
                paramInfo.add("CBOARP" + retObj.getFieldValueString(looper, colNames[0]));

                // Adding Data to HashMap
                reportParameters.put(retObj.getFieldValueString(looper, colNames[0]), paramInfo);

                ///Making of Drill url,PassUrl etc pending

            }
        }
        //This will get parameters hashMap if Report Id is provided
        /**
         * Code for Parameter Hash Map
         */
        String viewByID = "";
        try {
            sqlstr = getResourceBundle().getString("getParamMetaDataQuery2");
            Obj = new Object[1];
            if (!(this.reportId == null || this.reportId.trim().equals(""))) {
                Obj[0] = this.reportId;
            } else {
                Obj[0] = "0";
            }

            finalQuery = db.buildQuery(sqlstr, Obj);

            retObj = db.execSelectSQL(finalQuery);

        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }

        if (retObj != null) {
            colNames = retObj.getColumnNames();
            psize = retObj.getRowCount();
        } else {
            psize = 0;
        }

        int j = 0;
        ArrayList viewHashList = new ArrayList();
        int totalView = 1;
        if (psize > 0) {
            reportViewByMain = new HashMap();
            reportIncomingParameters.get("CBOVIEW_BY" + (retObj.getFieldValueString(0, colNames[0])));
            reportRowViewbyValues = new ArrayList();
            reportColViewbyValues = new ArrayList();
            for (int looper = 0; looper < psize; looper++) {
                if (looper == 0) {
                    viewByID = retObj.getFieldValueString(0, colNames[0]);
                    viewHashList = new ArrayList();
                    viewHashList.add(retObj.getFieldValueString(0, colNames[0]));//view By id
                    viewHashList.add(retObj.getFieldValueString(0, colNames[13]));// Default Value
                    if (retObj.getFieldValueInt(0, colNames[3]) == -1) {
                        reportRowViewbyValues.add(retObj.getFieldValueString(0, colNames[6]));
                    }
                    if (retObj.getFieldValueInt(0, colNames[2]) == -1) {
                        reportColViewbyValues.add(retObj.getFieldValueString(0, colNames[6]));
                    }
                    viewHashList.add(reportIncomingParameters.get("CBOVIEW_BY" + viewByID));//Curr Value
                    viewHashList.add(retObj.getFieldValueString(0, colNames[6]));// Added first value

                } else if (!viewByID.equalsIgnoreCase(retObj.getFieldValueString(looper, colNames[0]))) {
                    reportViewByMain.put(viewByID, viewHashList);
                    viewByID = retObj.getFieldValueString(looper, colNames[0]);
                    viewHashList = new ArrayList();
                    viewHashList.add(retObj.getFieldValueString(looper, colNames[0]));//view By id
                    viewHashList.add(retObj.getFieldValueString(looper, colNames[13]));// Default Value
                    if (retObj.getFieldValueInt(looper, colNames[3]) == -1) {
                        reportRowViewbyValues.add(retObj.getFieldValueString(looper, colNames[6]));
                    }
                    if (retObj.getFieldValueInt(looper, colNames[2]) == -1) {
                        reportColViewbyValues.add(retObj.getFieldValueString(looper, colNames[6]));
                    }
                    viewHashList.add(reportIncomingParameters.get("CBOVIEW_BY" + viewByID));//Curr Value
                    viewHashList.add(retObj.getFieldValueString(looper, colNames[6]));// Added first value

                } else {
                    viewHashList.add(retObj.getFieldValueString(looper, colNames[6]));// Added other value
                }
            }
            reportViewByMain.put(viewByID, viewHashList);
        }

    }///end of public void getParamMetaData()

    public String getChildElementId(String elementId) {
        ArrayList paraInfo = new ArrayList();
        String NextElementId = null;
        paraInfo = (ArrayList) reportParameters.get(elementId);
        if (paraInfo != null) {
            if (paraInfo.get(2) != null) {
                NextElementId = paraInfo.get(2).toString();
            }
        }
        return (NextElementId);
    }

    //added by susheela on19thNov start
  /*
     * public String getChildCustomDrill(String elementId, String ReportId,
     * HashMap reportParametersValues) throws Exception {
     *
     * String childId = ""; String allEleIds = ""; Set keySet =
     * reportParametersValues.keySet(); String keyArr[] = (String[])
     * keySet.toArray(new String[0]); for (int p = 0; p < keyArr.length; p++) {
     * allEleIds = allEleIds + "," + "'" + keyArr[p] + "'"; } allEleIds =
     * allEleIds.substring(1);
     *
     *
     * String childMem = ""; if (!elementId.equalsIgnoreCase("")) {
     *
     * String getMemQ = "Select * from prg_user_all_ddim_details where
     * info_element_id=" + elementId; PbReturnObject detailsObj =
     * execSelectSQL(getMemQ); String memId = ""; String folderId = ""; memId =
     * detailsObj.getFieldValueString(0, "INFO_MEMBER_ID"); String folderQ =
     * "select * from prg_ar_report_details where report_id=" + ReportId;
     * PbReturnObject folderObj = execSelectSQL(folderQ); folderId =
     * folderObj.getFieldValueString(0, "FOLDER_ID"); String localMemId = "";
     * String childQ = "select * from prg_grp_role_custom_drill where
     * sub_folder_id in(select sub_folder_id from prg_user_folder_detail where
     * folder_id=" + folderId + ")"; PbReturnObject chilObj =
     * execSelectSQL(childQ); for (int t = 0; t < chilObj.getRowCount(); t++) {
     * localMemId = chilObj.getFieldValueString(t, "MEMBER_ID"); if
     * (memId.equalsIgnoreCase(localMemId)) { childMem =
     * chilObj.getFieldValueString(t, "CHILD_MEMBER_ID"); } } String
     * otherREportElements = "Select * from prg_user_all_ddim_details where
     * info_element_id in(" + allEleIds + ")";
     *
     * PbReturnObject allEleInfo = execSelectSQL(otherREportElements); HashMap
     * eleMembers = new HashMap(); for (int j = 0; j < allEleInfo.getRowCount();
     * j++) { eleMembers.put(allEleInfo.getFieldValueString(j,
     * "INFO_MEMBER_ID"), allEleInfo.getFieldValueString(j, "INFO_ELEMENT_ID"));
     * }
     *
     *
     * if (eleMembers.containsKey(childMem)) { childId = (String)
     * eleMembers.get(childMem); } else { childId = null; } } else { childId =
     * null;
     *
     * }
     * return childId; }
     */
    //added by susheela on19thNov over
    //modified by susheela 03-12-09 start
  /*
     * public String getChildCustomDrill(String elementId, String ReportId,
     * HashMap reportParametersValues, String userId) throws Exception {
     *
     * String userDrillQ = "select * from PRG_USER_ROLE_DRILL where
     * sub_folder_id in (select sub_folder_id from prg_user_folder_detail where
     * folder_id=(select folder_ID from prg_ar_report_details where report_id="
     * + ReportId + ")) and user_id=" + userId; PbReturnObject userDrillObj =
     * execSelectSQL(userDrillQ); String childId = ""; String allEleIds = "";
     * Set keySet = reportParametersValues.keySet(); String keyArr[] =
     * (String[]) keySet.toArray(new String[0]); for (int p = 0; p <
     * keyArr.length; p++) { allEleIds = allEleIds + "," + "'" + keyArr[p] +
     * "'"; } allEleIds = allEleIds.substring(1);
     *
     *
     * String childMem = ""; if (!elementId.equalsIgnoreCase("")) {
     *
     * String getMemQ = "Select * from prg_user_all_ddim_details where
     * info_element_id=" + elementId; PbReturnObject detailsObj =
     * execSelectSQL(getMemQ); String memId = ""; String folderId = ""; memId =
     * detailsObj.getFieldValueString(0, "INFO_MEMBER_ID"); String folderQ =
     * "select * from prg_ar_report_details where report_id=" + ReportId;
     * PbReturnObject folderObj = execSelectSQL(folderQ); folderId =
     * folderObj.getFieldValueString(0, "FOLDER_ID"); String localMemId = "";
     * String childQ = "select * from prg_grp_role_custom_drill where
     * sub_folder_id in(select sub_folder_id from prg_user_folder_detail where
     * folder_id=" + folderId + ")";
     *
     * String userDrillQMem = "select * from prg_grp_role_custom_drill where
     * drill_id in(select drill_id from PRG_USER_ROLE_DRILL where sub_folder_id
     * in (select sub_folder_id from prg_user_folder_detail where
     * folder_id=(select folder_ID from prg_ar_report_details where report_id="
     * + reportId + ") ) and user_id=" + userId + ")";
     *
     * PbReturnObject chilObj = new PbReturnObject(); if
     * (userDrillObj.getRowCount() == 0) { chilObj = execSelectSQL(childQ);
     *
     * } else { chilObj = execSelectSQL(userDrillQMem);
     *
     * }
     *
     * for (int t = 0; t < chilObj.getRowCount(); t++) { localMemId =
     * chilObj.getFieldValueString(t, "MEMBER_ID"); if
     * (memId.equalsIgnoreCase(localMemId)) { childMem =
     * chilObj.getFieldValueString(t, "CHILD_MEMBER_ID"); } } String
     * otherREportElements = "Select * from prg_user_all_ddim_details where
     * info_element_id in(" + allEleIds + ")";
     *
     * PbReturnObject allEleInfo = execSelectSQL(otherREportElements); HashMap
     * eleMembers = new HashMap(); for (int j = 0; j < allEleInfo.getRowCount();
     * j++) { eleMembers.put(allEleInfo.getFieldValueString(j,
     * "INFO_MEMBER_ID"), allEleInfo.getFieldValueString(j, "INFO_ELEMENT_ID"));
     * }
     *
     *
     * if (eleMembers.containsKey(childMem)) { childId = (String)
     * eleMembers.get(childMem); } else { childId = null; } } else { childId =
     * null;
     *
     * }
     * return childId; }
     */
//    //added by susheela on19thNov over
//    //modified by susheela 6-02-2010 start
//    public String getChildCustomDrill(String elementId, String ReportId, HashMap reportParametersValues, String userId) {
//        String userReportDrillQ = "select * from prg_report_custom_drill where report_id=" + ReportId + " and user_id=" + userId;
//        String childId = "";
//        try
//        {
//
//            PbReturnObject userDrillObj = new PbReturnObject();
//            if (userDrillObj.getRowCount() == 0) {
//                userDrillObj = execSelectSQL(userReportDrillQ);
//            }
//
//            String userDrillQ = "select * from PRG_USER_ROLE_DRILL where sub_folder_id in (select sub_folder_id from prg_user_folder_detail where folder_id=(select folder_ID from prg_ar_report_details where report_id=" + ReportId + ")) and user_id=" + userId;
//            //if(userDrillObj.getRowCount()==0)
//            // userDrillObj=execSelectSQL(userDrillQ);
//            String allEleIds = "";
//
//            Set keySet = reportParametersValues.keySet();
//            String keyArr[] = (String[]) keySet.toArray(new String[0]);
//            for (int p = 0; p < keyArr.length; p++) {
//                allEleIds = allEleIds + "," + "'" + keyArr[p] + "'";
//            }
//            allEleIds = allEleIds.substring(1);
//
//
//            String childMem = "";
//            if (!elementId.equalsIgnoreCase("")) {
//
//                elementId = elementId.replace("_G", "");//added by santhosh.k on 05-03-2010
//                elementId = elementId.replace("_B", "");
//                String getMemQ = "Select * from prg_user_all_ddim_details where info_element_id=" + elementId;
//    //            ////.println("getMemQ==="+getMemQ);
//                PbReturnObject detailsObj = execSelectSQL(getMemQ);
//                String memId = "";
//                String folderId = "";
//                memId = detailsObj.getFieldValueString(0, "INFO_MEMBER_ID");
//                String folderQ = "select * from prg_ar_report_details where report_id=" + ReportId;
//                PbReturnObject folderObj = execSelectSQL(folderQ);
//                folderId = folderObj.getFieldValueString(0, "FOLDER_ID");
//                String localMemId = "";
//                String childQ = "select * from prg_grp_role_custom_drill where sub_folder_id in(select sub_folder_id from prg_user_folder_detail where folder_id=" + folderId + ")";
//
//                String userDrillQMem = "select * from prg_grp_role_custom_drill where drill_id in(select drill_id from PRG_USER_ROLE_DRILL where sub_folder_id in (select sub_folder_id from prg_user_folder_detail where folder_id=(select folder_ID from prg_ar_report_details where report_id=" + reportId + ") ) and user_id=" + userId + ")";
//
//                PbReturnObject chilObj = new PbReturnObject();
//                if (chilObj.getRowCount() == 0) {
//
//                    chilObj = execSelectSQL(userReportDrillQ);
//                }
//                if (chilObj.getRowCount() == 0) {
//
//                    chilObj = execSelectSQL(childQ);
//
//                }
//                if (chilObj.getRowCount() == 0) {
//
//                    chilObj = execSelectSQL(userDrillQMem);
//                }
//
//
//                for (int t = 0; t < chilObj.getRowCount(); t++) {
//                    localMemId = chilObj.getFieldValueString(t, "MEMBER_ID");
//                    if (memId.equalsIgnoreCase(localMemId)) {
//                        childMem = chilObj.getFieldValueString(t, "CHILD_MEMBER_ID");
//                    }
//                }
//
//                String otherREportElements = "Select * from prg_user_all_ddim_details where info_element_id in(" + allEleIds + ")";
//
//                PbReturnObject allEleInfo = execSelectSQL(otherREportElements);
//                HashMap eleMembers = new HashMap();
//                for (int j = 0; j < allEleInfo.getRowCount(); j++) {
//                    eleMembers.put(allEleInfo.getFieldValueString(j, "INFO_MEMBER_ID"), allEleInfo.getFieldValueString(j, "INFO_ELEMENT_ID"));
//                }
//
//
//
//                if (eleMembers.containsKey(childMem)) {
//
//                    childId = (String) eleMembers.get(childMem);
//                } else {
//
//                    childId = null;
//                }
//            } else {
//
//                childId = null;
//
//            }
//        }
//        catch(SQLException e){}
//        return childId;
//    }
    //added by susheela 6-02-2010 over
    public String getChildElementId(String elementId, String ReportId) {
        ArrayList paraInfo = new ArrayList();
        String NextElementId = null;
        PbReturnObject retObj = null;
        String[] colNames;
        PbDb db = new PbDb();
        if (!elementId.equalsIgnoreCase("TIME")) {
            String sqlstr = "";
            sqlstr += " select KEY_ELEMENT_ID , LEVEL1 from ";
            sqlstr += " PRG_USER_ALL_ADIM_KEY_VAL_ELE a, ";
            sqlstr += " PRG_AR_REPORT_PARAM_DETAILS b ";
            sqlstr += " where a.KEY_ELEMENT_ID = b.element_id ";
            sqlstr += " and KEY_DIM_ID in ( ";
            sqlstr += " SELECT DIM_ID ";
            sqlstr += " FROM PRG_USER_ALL_INFO_DETAILS ";
            sqlstr += " where ELEMENT_ID =" + elementId + ") ";

            sqlstr += " and LEVEL1 > ( ";
            sqlstr += "  SELECT min(LEVEL1) ";
            sqlstr += " FROM PRG_USER_ALL_ADIM_KEY_VAL_ELE";
            sqlstr += "  WHERE KEY_ELEMENT_ID =" + elementId + ") ";

            sqlstr += " and b.report_id= " + ReportId + " ";
            sqlstr += " and ELEMENT_ID !=" + elementId + " ";
            sqlstr += " order by 2 ";


            try {
                retObj = db.execSelectSQL(sqlstr);

            } catch (Exception ex) {
                logger.error("Exception:", ex);
            }

            colNames = retObj.getColumnNames();
            int psize = retObj.getRowCount();

            if (psize > 0) {
                NextElementId = retObj.getFieldValueString(0, colNames[0]);
            }
        }



        return (NextElementId);
    }

    // added by susheela start
    public HashMap getParentElementId(String elementId, String ReportId) {
        ArrayList paraInfo = new ArrayList();
        ArrayList parentBussTab = new ArrayList();
        HashMap parentBussCol = new HashMap();
        HashMap details = new HashMap();
        String NextElementId = null;
        PbReturnObject retObj = null;
        String[] colNames;
        PbDb db = new PbDb();
        if (!elementId.equalsIgnoreCase("TIME")) {
            String sqlstr =
                    //modified by Nazneen on 18 April 2014
                    //            sqlstr = "SELECT ELEMENT_ID ,  BUSS_TABLE_ID    ,  BUSS_COL_NAME from PRG_USER_ALL_INFO_DETAILS WHERE dim_id "
                    //                    + " in( SELECT dim_id from PRG_USER_ALL_INFO_DETAILS WHERE element_id in(" + elementId + ") and  element_id "
                    //                    + " in(select element_id from prg_ar_report_param_details where report_id=" + ReportId + ")) and   "
                    //                    + " element_id in(select element_id from prg_ar_report_param_details where report_id=" + ReportId + ")";

                    sqlstr = "SELECT A.ELEMENT_ID ,  BUSS_TABLE_ID ,  BUSS_COL_NAME FROM PRG_USER_ALL_INFO_DETAILS A,"
                    + " (SELECT dim_id  FROM PRG_USER_ALL_INFO_DETAILS  WHERE element_id IN(" + elementId + ") AND element_id   IN"
                    + " (SELECT element_id FROM prg_ar_report_param_details WHERE report_id=" + ReportId + ")) B,"
                    + " (SELECT element_id FROM prg_ar_report_param_details WHERE report_id=" + ReportId + ") c"
                    + "  where A.dim_id = b.dim_id  and A.element_id =c.element_id ";
            try {

                retObj = db.execSelectSQL(sqlstr);

            } catch (Exception ex) {
                logger.error("Exception:", ex);
            }

            colNames = retObj.getColumnNames();
            int psize = retObj.getRowCount();

            if (psize > 0) {
                NextElementId = retObj.getFieldValueString(0, colNames[0]);
            }
            for (int m = 0; m < retObj.getRowCount(); m++) {

                if (!elementId.equalsIgnoreCase(retObj.getFieldValueString(m, "ELEMENT_ID"))) {
                    paraInfo.add(retObj.getFieldValueString(m, 0));
                    if (!parentBussTab.contains(retObj.getFieldValueString(m, "BUSS_TABLE_ID"))) {
                        parentBussTab.add(retObj.getFieldValueString(m, "BUSS_TABLE_ID"));
                    }
                    parentBussCol.put(retObj.getFieldValueString(m, 0), retObj.getFieldValueString(m, "BUSS_COL_NAME"));
                }
            }
        }

        details.put("ParentBussTables", parentBussTab);
        details.put("ParentList", paraInfo);
        details.put("ParentBussCol", parentBussCol);
        return details;
    }


    /*
     * public HashMap getParentElementId(String elementId, String ReportId) {
     * ArrayList paraInfo = new ArrayList(); ArrayList parentBussTab = new
     * ArrayList(); HashMap parentBussCol = new HashMap(); HashMap details = new
     * HashMap(); String NextElementId = null; PbReturnObject retObj = null;
     * String[] colNames; if (!elementId.equalsIgnoreCase("TIME")) { String
     * sqlstr = ""; sqlstr += " select KEY_ELEMENT_ID ,
     * LEVEL1,key_buss_table_id,key_buss_col_name from "; sqlstr += "
     * PRG_USER_ALL_ADIM_KEY_VAL_ELE a, "; sqlstr += "
     * PRG_AR_REPORT_PARAM_DETAILS b "; sqlstr += " where a.KEY_ELEMENT_ID =
     * b.element_id "; sqlstr += " and KEY_DIM_ID in ( "; sqlstr += " SELECT
     * DIM_ID "; sqlstr += " FROM PRG_USER_ALL_INFO_DETAILS "; sqlstr += " where
     * ELEMENT_ID =" + elementId + ") ";
     *
     * sqlstr += " and LEVEL1 < ( "; sqlstr += " SELECT min(LEVEL1) "; sqlstr +=
     * " FROM PRG_USER_ALL_ADIM_KEY_VAL_ELE"; sqlstr += " WHERE KEY_ELEMENT_ID
     * =" + elementId + ") ";
     *
     * sqlstr += " and b.report_id= " + ReportId + " "; sqlstr += " and
     * ELEMENT_ID !=" + elementId + " "; sqlstr += " order by 2 ";
     *
     *
     * try { retObj = execSelectSQL(sqlstr);
     *
     * } catch (Exception ex) { logger.error("Exception:",ex); }
     *
     * colNames = retObj.getColumnNames(); int psize = retObj.getRowCount();
     *
     * if (psize > 0) { NextElementId = retObj.getFieldValueString(0,
     * colNames[0]); } for (int m = 0; m < retObj.getRowCount(); m++) {
     * paraInfo.add(retObj.getFieldValueString(m, 0)); if
     * (!parentBussTab.contains(retObj.getFieldValueString(m,
     * "KEY_BUSS_TABLE_ID"))) { parentBussTab.add(retObj.getFieldValueString(m,
     * "KEY_BUSS_TABLE_ID")); } parentBussCol.put(retObj.getFieldValueString(m,
     * 0), retObj.getFieldValueString(m, "KEY_BUSS_COL_NAME")); } }
     *
     * details.put("ParentBussTables", parentBussTab); details.put("ParentList",
     * paraInfo); details.put("ParentBussCol", parentBussCol); return details; }
     */
    boolean ValidateParamValues(Object o) {
        boolean isValid = true;
        /*
         * if (o == null || o.toString().equals("") ||
         * o.toString().equalsIgnoreCase("All")) { isValid = false;
        }
         */
        if (o == null || ((List) o).contains("All")) {
            isValid = false;
        }
        return (isValid);
    }

    String getOracleInClause(Object o) {
        return ("'" + o.toString().replace(",", "','") + "'");
    }

    String getClauseForList(List<String> list) {
        StringBuilder result = new StringBuilder();
        for (String str : list) {
            result.append(",'").append(str).append("'");
        }
        if (result.length() > 0) {
            return result.substring(1);
        } else {
            return result.toString();
        }
    }

    public String getWhereClause(HashMap hm, ArrayList viewByCols) throws Exception {
        Vector parameterTable = new Vector();
        PbDb pbDb = new PbDb();
//        StringBuilder ParameterId =new StringBuilder();
        String ParameterId = null;
        String paramWhereClause = "";
        String selfParamWhereClause = "";
        String sqlstr = "";
        String finalQuery = "";
        {//where Clause Columns
            String[] a1 = (String[]) (hm.keySet()).toArray(new String[0]);
            for (int i = 0; i < a1.length; i++) {
                List filterList = (List) hm.get(a1[i]);
                if (ValidateParamValues(hm.get(a1[i]))) {
                    if (ParameterId == null || ParameterId.toString().equalsIgnoreCase("")) {

//                        for(int j=0;j<filterList.size();j++){
//                            ParameterId.append(",'").append(filterList.get(j)).append("'");
//                        }
                        ParameterId = (a1[i]).toString();
                    } else {
                        ParameterId += "," + (a1[i]).toString();
//                        ParameterId .append(",").append("All");
                    }
                }
            }
            if (ParameterId != null && !ParameterId.toString().equalsIgnoreCase("")) {

                // sqlstr = resundle.getString("generateViewByQry2");
                sqlstr = "select distinct BUSS_TABLE_ID,BUSS_TABLE_NAME,BUSS_COL_NAME,USER_COL_NAME,denom_query,ELEMENT_ID,REF_ELEMENT_ID, REF_ELEMENT_TYPE from prg_user_all_info_details  where ELEMENT_ID in (&)  order by BUSS_TABLE_ID,REF_ELEMENT_ID, REF_ELEMENT_TYPE ";
                Object Obj[] = new Object[1];
                Obj[0] = ParameterId;

                finalQuery = pbDb.buildQuery(sqlstr, Obj);

                //
                PbReturnObject retObj = pbDb.execSelectSQL(finalQuery);

                String colNames[] = retObj.getColumnNames();
                int psize = 0;
                psize = retObj.getRowCount();
                if (psize > 0) {
                    //Looping twice
                    //Loop 1 find the fact and current and prior cols
                    //loop 2 build query

                    for (int i = 0; i < psize; i++) {
                        //ViewByMap.put(retObj.getFieldValue(i, colNames[5]), retObj.getFieldValue(i, colNames[1]) + "." + retObj.getFieldValue(i, colNames[2]));
                        //ViewByMapCol.put(retObj.getFieldValue(i, colNames[5]), retObj.getFieldValue(i, colNames[2]));
                        paramWhereClause += " and " + retObj.getFieldValueString(i, colNames[1]) + "." + retObj.getFieldValueString(i, colNames[2]);
                        paramWhereClause += " in (" + getClauseForList((List<String>) hm.get(retObj.getFieldValueString(i, colNames[5]))) + ") ";
                        if (!(viewByCols == null || viewByCols.size() == 0)) {
                            if (retObj.getFieldValueString(i, colNames[5]).equalsIgnoreCase(viewByCols.get(0).toString())) {

                                selfParamWhereClause += " and " + retObj.getFieldValueString(i, colNames[1]) + "." + retObj.getFieldValueString(i, colNames[2]);
                                selfParamWhereClause += " in (" + getOracleInClause(hm.get(retObj.getFieldValueString(i, colNames[5]))) + ") ";

                            }
                        }
                        if (parameterTable.isEmpty()) {
                            parameterTable.add(retObj.getFieldValueString(i, colNames[0]));
                        } else if (!parameterTable.contains(retObj.getFieldValueString(i, colNames[0]))) {
                            parameterTable.add(retObj.getFieldValueString(i, colNames[0]));
                        }
                    }
                }
            }
        }

        return paramWhereClause;

    }

    // added by susheela over
    public String getElementName(String elementId) {
        ArrayList paraInfo = new ArrayList();
        String NextElementId = null;
        if (elementId != null && elementId.equalsIgnoreCase("Time")) {
            NextElementId = "Time";
        } else {
            paraInfo = (ArrayList) reportParameters.get(elementId);
            if (paraInfo != null) {
                if (paraInfo.get(1) != null) {
                    NextElementId = paraInfo.get(1).toString();
                }
            }

        }

        return (NextElementId);
    }

    private Connection getConnection(String elementId) {

        Connection connection = null;
        try {
            connection = ProgenConnection.getInstance().getConnectionForElement(elementId);
        } catch (Exception e) {
            logger.error("Exception:", e);
        }
        return connection;
    }

    private Connection getConnection(Integer connId) {

        Connection connection = null;
        try {
            connection = ProgenConnection.getInstance().getConnectionByConId(connId.toString());
        } catch (Exception e) {
            logger.error("Exception:", e);
        }
        return connection;
    }

    private void initializeConnectionSettings(String elementId) {
        ConnectionDAO connHelper = new ConnectionDAO();
        ConnectionMetadata connMeta = connHelper.getConnectionForElement(elementId);
        connectionId = connMeta.getConnId();
        connectionType = connMeta.getDbType();
    }

    public void getReportBizRoles() {
        String sqlstr = "";
        String sqlstrAO = "";
        Object[] Obj = null;
        String finalQuery = "";
        String finalQueryAO = "";
        PbReturnObject retObj = null;
        PbReturnObject retObjAO = null;
        String[] dbColNames = null;
        PbDb db = new PbDb();

        try {
            sqlstr = getResourceBundle().getString("getReportBizRoles");
            sqlstrAO = getResourceBundle().getString("getReportAOId");
            Obj = new Object[1];
            Obj[0] = this.reportId;
            finalQuery = db.buildQuery(sqlstr, Obj);
            finalQueryAO = db.buildQuery(sqlstrAO, Obj);

            retObj = db.execSelectSQL(finalQuery);
            retObjAO = db.execSelectSQL(finalQueryAO);
            if (retObj != null && retObj.getRowCount() != 0) {
                reportBizRoles = new String[retObj.getRowCount()];
                dbColNames = retObj.getColumnNames();
                for (int i = 0; i < retObj.getRowCount(); i++) {
                    reportBizRoles[i] = retObj.getFieldValueString(i, dbColNames[0]);
                    this.AOId = retObjAO.getFieldValueString(0, 0);
                }
            } else {
                this.AOId = retObjAO.getFieldValueString(0, 0);
                reportBizRoles = new String[0];
            }
            retObj = db.execSelectSQL(finalQueryAO);
        } catch (Exception exp) {
            logger.error("Exception:", exp);
        }
    }

    public String getReportBizRoleName(String roleID) {
        String folderName = "";
        String sqlstr = "";
        Object[] Obj = null;
        String finalQuery = "";
        PbReturnObject retObj = null;
        String[] dbColNames = null;
        PbDb db = new PbDb();

        try {
            sqlstr = getResourceBundle().getString("getReportBizRoleName");
            Obj = new Object[1];
            Obj[0] = roleID;
            finalQuery = db.buildQuery(sqlstr, Obj);

            retObj = db.execSelectSQL(finalQuery);
            folderName = retObj.getFieldValueString(0, 0);
        } catch (Exception exp) {
            logger.error("Exception:", exp);
        }
        return folderName;
    }

    //written for drill structure in portalgraphs on 21sep
    public String getChildCustomDrillforPortal(String elementId) throws Exception {
        String childId = "";
        String allEleIds = "";
        HashMap paramVals = new HashMap();
        PbDb db = new PbDb();
        paramVals = reportIncomingParameters;
        Set keySet = paramVals.keySet();
        String keyArr[] = (String[]) keySet.toArray(new String[0]);
        allEleIds = Joiner.on(",").join(keySet);
//        for (int p = 0; p < keyArr.length; p++) {
//            allEleIds = allEleIds + "," + "'" + keyArr[p] + "'";
//        }
//        allEleIds = allEleIds.substring(1);


        String childMem = "";
        if (!elementId.equalsIgnoreCase("")) {

            elementId = elementId.replace("_G", "");//added by santhosh.k on 05-03-2010
            elementId = elementId.replace("_B", "");
            String getMemQ = "Select * from prg_user_all_ddim_details where info_element_id=" + elementId;
//            ////.println("getMemQ==="+getMemQ);
            PbReturnObject detailsObj = db.execSelectSQL(getMemQ);
            if (detailsObj.getRowCount() > 0) {
                String memId = "";
                String folderId = "";
                memId = detailsObj.getFieldValueString(0, "INFO_MEMBER_ID");
                String localMemId = "";
                String userDrillQMem = "select * from prg_grp_role_custom_drill where member_id=" + memId;

                PbReturnObject chilObj = new PbReturnObject();
                chilObj = db.execSelectSQL(userDrillQMem);


                for (int t = 0; t < chilObj.getRowCount(); t++) {
                    localMemId = chilObj.getFieldValueString(t, "MEMBER_ID");
                    if (memId.equalsIgnoreCase(localMemId)) {
                        childMem = chilObj.getFieldValueString(t, "CHILD_MEMBER_ID");
                    }
                }

                String otherREportElements = "Select * from prg_user_all_ddim_details where info_element_id in(" + allEleIds + ")";

                PbReturnObject allEleInfo = db.execSelectSQL(otherREportElements);
                HashMap eleMembers = new HashMap();
                for (int j = 0; j < allEleInfo.getRowCount(); j++) {
                    eleMembers.put(allEleInfo.getFieldValueString(j, "INFO_MEMBER_ID"), allEleInfo.getFieldValueString(j, "INFO_ELEMENT_ID"));
                }



                if (eleMembers.containsKey(childMem)) {

                    childId = (String) eleMembers.get(childMem);
                } else {

                    childId = null;
                }
            }
        } else {

            childId = null;

        }
        return childId;
    }

    private HashMap<String, String> getDrillMap(String userId) {
        DrillMaps drillMapHelper = new DrillMaps();
        if (this.drillMap == null) {
            if ("TIME".equalsIgnoreCase(reportRowViewbyValues.get(0)) && !reportColViewbyValues.isEmpty()) {
                this.drillMap = drillMapHelper.getDrillForReport(reportId, userId, this.reportColViewbyValues.get(0),
                        this.reportParametersValues);
            } else {
                this.drillMap = drillMapHelper.getDrillForReport(reportId, userId, this.reportRowViewbyValues.get(0),
                        this.reportParametersValues);
            }
        }
        return drillMap;
    }

    public ArrayList<String> getRowViewDrillUrl(String userId) {
        return this.getDrillUrls(userId, reportRowViewbyValues, reportParametersValues.keySet());
    }

    public ArrayList<String> getColumnViewDrillUrl(String userId) {
        return this.getDrillUrls(userId, this.reportColViewbyValues, reportParametersValues.keySet());
    }

    private ArrayList<String> getDrillUrls(String userId, ArrayList<String> viewByValues, Set<String> parameterElements) {
        ArrayList<String> drillUrls = new ArrayList<String>();
        String url = "";
        if (viewByValues.isEmpty()) {
            return drillUrls;
        }

        if (viewByValues.get(0).equalsIgnoreCase("Time")) {
            url = resolveTimeDrill(this.timeDetailsArray);
        } else {
            HashMap<String, String> drillHelpMap = this.getDrillMap(userId);
            String presentViewBy = viewByValues.get(0);
            String nextViewBy;
            String viewById = "";
            int shouldLoop = 0;
            ArrayList<String> viewByList = new ArrayList<String>(this.reportColViewbyValues.size() + this.reportRowViewbyValues.size());

            Set<String> viewByIdSet = reportViewByMain.keySet();
            for (String viewByIdKey : viewByIdSet) {
                String elementIdForViewId = reportViewByMain.get(viewByIdKey).get(2);
                //if reportIncomingParameters is null then take default value
                if (elementIdForViewId == null) {
                    elementIdForViewId = reportViewByMain.get(viewByIdKey).get(1);
                }
                viewById = viewByIdKey;
                if (viewByValues.get(0).equalsIgnoreCase(elementIdForViewId)) {
                    break;
                }
            }
            for (String viewByElementId : this.reportColViewbyValues) {
                viewByList.add(viewByElementId);
            }

            for (String viewByElementId : this.reportRowViewbyValues) {
                viewByList.add(viewByElementId);
            }

            Set<String> drillElementKeys = drillHelpMap.keySet();
            do {
                nextViewBy = drillHelpMap.get(presentViewBy);
                presentViewBy = nextViewBy;
                shouldLoop++;
                if (shouldLoop > drillElementKeys.size()) {
                    break;
                }
            } while (nextViewBy != null && (viewByList.contains(nextViewBy) || !parameterElements.contains(nextViewBy)));
            if (viewByList.contains(nextViewBy) || !parameterElements.contains(nextViewBy)) {
                nextViewBy = null;
            }
            if (nextViewBy != null && (!viewByList.contains(nextViewBy))) {
                url += "&CBOVIEW_BY" + viewById + "=" + nextViewBy + "";
            }
            url += "&CBOARP" + viewByValues.get(0) + "=";
        }
        drillUrls.add(ctxPath + "/reportViewer.do?reportBy=viewReport&REPORTID=" + this.reportId + url);
        for (int i = 1; i < viewByValues.size(); i++) {
            drillUrls.add("");
        }
        return drillUrls;
    }

    public void setRowViewBys(ArrayList<String> rowViewBys) {
        this.reportRowViewbyValues = rowViewBys;
    }

    public void setColumnViewBys(ArrayList<String> colViewBys) {
        this.reportColViewbyValues = colViewBys;
    }

    public void setParameters(String elementId, String colName, Iterable<String> defaultValues) {
        if (reportParameters == null) {
            reportParameters = new LinkedHashMap<String, ArrayList<String>>();
        }

        if (reportParametersValues == null) {
            reportParametersValues = new LinkedHashMap<String, String>();
        }

        ArrayList paramInfo = new ArrayList();

        paramInfo.add(elementId); //elementId
        paramInfo.add(colName); //Dimension Name
        paramInfo.add(null); //child_element_id
        paramInfo.add(null); //dim_id
        paramInfo.add(null); //dim_tab_id
        paramInfo.add(null); //display_Type
        paramInfo.add(null); //rel_level
        paramInfo.add(null);//disp_seq_no
        String defaultValue = "";
        List<String> defaultValueList = new ArrayList<String>();
        for (String value : defaultValues) {
            defaultValueList.add(value);
        }
        if (defaultValueList.isEmpty()) {
            defaultValueList.add("All");
        }
        /*
         * defaultValueList.add("srikanth,sri-told~that&u"); Gson gson=new
         * Gson(); Type tartype=new TypeToken<List<String>>() {}.getType();
         * String val=gson.toJson(defaultValueList,tartype);
         */

        /*
         * if ( ! "".equals(defaultValue) ) defaultValue =
         * defaultValue.substring(1); else defaultValue = "All";
         */
        paramInfo.add(defaultValueList);//default_value
        paramInfo.add("CBOARP" + elementId);
        paramInfo.add("IN"); //NOT_SELECTED
        HashMap inMap = operatorFilters.get("IN");
        if (inMap == null) {
            inMap = new HashMap();
            operatorFilters.put("IN", inMap);
        }
        inMap.put(elementId, defaultValueList);
        reportParameters.put(elementId, paramInfo);

        reportParametersValues.put(elementId, defaultValueList);
    }

    public void setParameters(String elementId, List<String> paramValueList) {
//        ArrayList<String> paramValueLst = new ArrayList<String>();
//        paramValueLst.add(paramValue);
        this.setParameters(elementId, "", paramValueList);
    }

    public void setReportQueryColumns(String elementId, String colName, String agg, String ColType, String originalColType) {
        testForPrev = colName;
        if (reportQryElementIds.isEmpty()) {
            reportQryElementIds = new ArrayList<String>();
            reportQryAggregations = new ArrayList<String>();
            reportQryColNames = new ArrayList<String>();
            reportQryColTypes = new ArrayList<String>();
            isFormulaMeasure = new ArrayList<Boolean>();
            this.initializeConnectionSettings(elementId);

        }
        if (!reportQryElementIds.contains(elementId) && !reportQryColNames.contains(colName)) {
            reportQryElementIds.add(elementId);
            reportQryAggregations.add(agg);
            reportQryColNames.add(colName);
            reportQryColTypes.add(ColType);
            isFormulaMeasure.add(isColumnTypeAFormula(originalColType, elementId));
        }
    }

    public void setTimeDetails(String timeLevel, String timeType, ArrayList<String> timeColTypes, Iterable<String> defaultValues) {
        ProgenParam pParam = new ProgenParam();
        timeDetailsArray = new ArrayList<String>();
        timeDetailsMap = new LinkedHashMap<String, ArrayList<String>>();

        ArrayList<String> timeInfo = new ArrayList<String>();

        timeDetailsArray.add(timeLevel);
        timeDetailsArray.add(timeType);

        Integer sequence = 1;
        for (String timeColType : timeColTypes) {
            timeInfo = new ArrayList<String>();
            if ("AS_OF_DATE".equals(timeColType)) {
                //timeDetailsArray.add(pParam.getdateforpage().toString());
                timeInfo.add(pParam.getdateforpage().toString()); //Date
                timeInfo.add("CBO_" + timeColType); //CBO_Column_Type
                timeInfo.add("DATE"); //Column_Name
                timeInfo.add(sequence.toString()); //Sequence
                timeInfo.add(sequence.toString()); //form_sequence
                timeInfo.add(null); //rep_time_id
                timeInfo.add(timeColType); //column_type
                timeDetailsMap.put(timeColType, timeInfo);
            } else if ("PRG_PERIOD_TYPE".equals(timeColType)) {
                timeInfo.add("Month");
                timeInfo.add("CBO_" + timeColType); //CBO_Column_Type
                timeInfo.add("DATE"); //Column_Name
                timeInfo.add(sequence.toString()); //Sequence
                timeInfo.add(sequence.toString()); //form_sequence
                timeInfo.add(null); //rep_time_id
                timeInfo.add(timeColType); //column_type
                timeDetailsMap.put(timeColType, timeInfo);
            }
            sequence++;
        }

        if (timeDetailsArray.get(0).toString().equalsIgnoreCase("DAY") && timeDetailsArray.get(1).toString().equalsIgnoreCase("PRG_STD")) {
            timeInfo = (ArrayList) timeDetailsMap.get("AS_OF_DATE");
            timeDetailsArray.add(timeInfo.get(0));

            timeInfo = (ArrayList) timeDetailsMap.get("PRG_PERIOD_TYPE");
            timeDetailsArray.add(timeInfo.get(0));

            if (timeDetailsMap.get("PRG_COMPARE") != null) {
                timeInfo = (ArrayList) timeDetailsMap.get("PRG_COMPARE");
                timeDetailsArray.add(timeInfo.get(0));
            } else {
                timeDetailsArray.add("Last Period");
            }
        }
    }

    public void initializeReportTableCols() {
        if (tableElementIds == null) {
            tableElementIds = new ArrayList<String>();
            tableColTypes = new ArrayList<String>();
            tableColDispTypes = new ArrayList<String>();
            tableColNames = new ArrayList<String>();
        }
        if (!tableColNames.contains(testForPrev)) {
            for (int i = 0; i < reportRowViewbyValues.size(); i++) {
                if (String.valueOf(reportRowViewbyValues.get(i)).equalsIgnoreCase("Time")) {
                    tableElementIds.add(String.valueOf(reportRowViewbyValues.get(i)));
                    tableColTypes.add("C");
                } else {
                    tableElementIds.add("A_" + String.valueOf(reportRowViewbyValues.get(i)));
                    tableColTypes.add("C");
                }
                if (i == 0) {
                    tableColDispTypes.add("H");//T for Text,H for Hyperlink
                } else {
                    tableColDispTypes.add("T");//T for Text,H for Hyperlink
                }
                if (String.valueOf(reportRowViewbyValues.get(i)).equalsIgnoreCase("Time")) {
                    tableColNames.add("Time");
                } else {
                    tableColNames.add(getParameterDispName(String.valueOf(reportRowViewbyValues.get(i))));
                }
            }
        }

        if (reportQryElementIds != null && !tableColNames.contains(testForPrev)) {
            for (int i = 0; i < reportQryElementIds.size(); i++) {
                tableElementIds.add("A_" + reportQryElementIds.get(i));
                tableColTypes.add(reportQryColTypes.get(i));//N
                tableColDispTypes.add("T");
                tableColNames.add(reportQryColNames.get(i));
            }
        }
        tableDisplayRows = "25";
        showTableTotals = true;
        showTableSubTotals = false;
        showAdvSearch = true;
    }

    public void setReportBizRole(Integer userFolderId) {
        if (reportBizRoles == null) {
            reportBizRoles = new String[1];
        }
        reportBizRoles[0] = userFolderId.toString();
    }
    //start of code by Bhargavi to maintain ao id

    public void setReportAoId(String userAoId) {
        if (ReportAoId == null) {
            ReportAoId = new String[1];
        }
        ReportAoId[0] = userAoId;
    }
//end of code by bhargavi

    public ArrayList<String> getTimeDetailsForQuery(String timeLevel) {
        ProgenParam pParam = new ProgenParam();
        ArrayList<String> timeDetails = new ArrayList();
        if ("MONTH".equalsIgnoreCase(timeLevel)) {
            timeDetails.add("Day");
            timeDetails.add("PRG_STD");
            timeDetails.add(pParam.getdateforpage().toString());
            timeDetails.add("Month");
            timeDetails.add("Last Period");
        } else {
            timeDetails.add("Day");
            timeDetails.add("PRG_STD");
            timeDetails.add(pParam.getdateforpage().toString());
            timeDetails.add("Qtr");
            timeDetails.add("Last Period");
        }
        return timeDetails;
    }

    @Override
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException ex) {
            logger.error("Exception:", ex);
            return this;
        }
    }

    public Iterable<String> formulatePossibleRowViewBys() {
        Set<String> possibleViewBys = new HashSet<String>();
        if (!reportParameters.isEmpty()) {
            Set<String> potentialViewBys = reportParameters.keySet();

            for (String viewBy : potentialViewBys) {
                if (!reportRowViewbyValues.contains(viewBy) && !reportColViewbyValues.contains(viewBy)) {
                    possibleViewBys.add(viewBy);
                }
            }
        }
        return possibleViewBys;
    }

    public boolean isTimeInitialized() {
        return (!(timeDetailsArray == null || timeDetailsArray.isEmpty()));
    }

    public void avoidProgenTime() {
        ProgenParam pParam = new ProgenParam();
        avoidProgenTime = true;
        timeDetailsArray = new ArrayList<String>();
        timeDetailsArray.add("Day");
        timeDetailsArray.add("PRG_STD");
        timeDetailsArray.add(pParam.getdateforpage().toString());
        timeDetailsArray.add("Month");
        timeDetailsArray.add("Last Period");
        timeDetailsMap = new LinkedHashMap<String, ArrayList<String>>();
    }

    public void updateParameterDefaults(String elementId, Iterable<String> values, boolean isExcluded) {
        if (!reportParameters.isEmpty()) {
            ArrayList paramInfo = reportParameters.get(elementId);
            if (paramInfo != null) {
                String paramStatus = isExcluded ? "EXCLUDED" : "INCLUDED";
//                String defaultValues = Joiner.on(",").join(values);
                paramInfo.set(8, values);
                paramInfo.set(10, paramStatus);
                reportParametersValues.put(elementId, values);
                if (paramStatus.equalsIgnoreCase("INCLUDED")) {
                    Map inMap = operatorFilters.get("IN");
                    inMap.put(elementId, values);
                }
            }
        }
    }

    public List<String> getDefaultValue(String paramId) {
        List<String> paramDefaultvalue = null;
        if (!(reportParameters.isEmpty())) {
            ArrayList parameterDetails = reportParameters.get(paramId);
            if (parameterDetails != null) {
                paramDefaultvalue = (List<String>) parameterDetails.get(8);
            }
        }
        return paramDefaultvalue;
    }

    public String getParameterStatus(String paramId) {
        String paramStatus = "";
        if (!(reportParameters.isEmpty())) {
            ArrayList<String> parameterDetails = (ArrayList<String>) reportParameters.get(paramId);
            paramStatus = parameterDetails.get(10);
        }
        return paramStatus;
    }

    public boolean isParameterValueSet(String paramId) {
        if (!reportParameters.isEmpty()) {
            ArrayList<String> parameterDetails = (ArrayList<String>) reportParameters.get(paramId);
            String status = parameterDetails.get(10);
            return (status.equals("NOT_SELECTED") ? false : true);
        }
        return false;
    }

    public void removeParameter(String paramId) {
        if (!reportParameters.isEmpty()) {
            reportParameters.remove(paramId);
            reportParametersValues.remove(paramId);

        }

    }

    public void resetParameterDefaults(String elementId) {
        if (!reportParameters.isEmpty()) {
            ArrayList<String> paramInfo = reportParameters.get(elementId);
            if (paramInfo != null) {
                String paramStatus = "NOT_SELECTED";
                String defaultValues = "All";
                paramInfo.set(8, defaultValues);
                paramInfo.set(10, paramStatus);
                reportParametersValues.put(elementId, defaultValues);
            }
        }
    }

    public void resetAllParameters() {
        if (!reportParameters.isEmpty()) {
            reportParameters = new LinkedHashMap<String, ArrayList<String>>();
            reportParametersValues = new LinkedHashMap<String, String>();
        }
    }

    public void updateTimeDefault(String timeColumnType, String dateValue) {
        ArrayList<String> timeInfo = timeDetailsMap.get(timeColumnType);
        timeInfo.set(0, dateValue);
    }

    public Iterable<String> getMeasures() {
        ArrayList<String> measures = new ArrayList<String>();
        for (String measure : reportQryElementIds) {
            measures.add(measure);
        }
        return measures;
    }

    public Iterable<String> getDimensions() {
        ArrayList<String> dimensions = new ArrayList<String>();
        for (String dimension : reportParameters.keySet()) {
            dimensions.add(dimension);
        }
        return dimensions;
    }

    public String getTimeDefault(String timeColumnType) {

        ArrayList<String> timeInfo = timeDetailsMap.get(timeColumnType);
        return timeInfo.get(0);

    }

    public boolean isFormulaMeasure(String elementId) {
        for (String element : reportQryElementIds) {
            if (elementId.equalsIgnoreCase(element)) {
                if (reportQryElementIds.indexOf(element) >= isFormulaMeasure.size()) {
                    return false;
                } else {
                    return isFormulaMeasure.get(reportQryElementIds.indexOf(element));
                }
            }
        }

        return false;
    }

    public HashMap getNonViewByMap() {
        return nonViewByMap;
    }

    public void setNonViewByMap(HashMap nonViewByMap) {
        this.nonViewByMap = nonViewByMap;
    }

    public List<String> getCustomSequence() {
        return customSequence;
    }

    public void setCustomSequence(List<String> customSequence) {
        this.customSequence = customSequence;
    }

    public HashMap<String, ArrayList<String>> getTransposeFormatMap() {
        return transposeFormatMap;
    }

    public void setTransposeFormatMap(HashMap<String, ArrayList<String>> transposeFormatMap) {
        this.transposeFormatMap = transposeFormatMap;
    }

    public HashMap<String, ArrayList<String>> getPercentColValues() {
        return percentColValues;
    }

    public void setPercentColValues(HashMap<String, ArrayList<String>> percentColValues) {
        this.percentColValues = percentColValues;
    }

    public HashMap<String, ArrayList<String>> getGoalseek() {
        return goalseek;
    }

    public void setGoalseek(HashMap<String, ArrayList<String>> goalseek) {
        this.goalseek = goalseek;
    }

    public HashMap<String, HashMap<String, ArrayList<String>>> getGoalSeekBasicandAdhoc() {
        return goalSeekBasicandAdhoc;
    }

    public void setGoalSeekBasicandAdhoc(HashMap<String, HashMap<String, ArrayList<String>>> goalSeekBasicandAdhoc) {
        this.goalSeekBasicandAdhoc = goalSeekBasicandAdhoc;
    }

    public HashMap<String, ArrayList<String>> getGoalandPercent() {
        return goalandPercent;
    }

    public void setGoalandPercent(HashMap<String, ArrayList<String>> goalandPercent) {
        this.goalandPercent = goalandPercent;
    }

    public List<BigDecimal> getMinmaxavgValues() {
        return minmaxavgValues;
    }

    public void setMinmaxavgValues(List<BigDecimal> minmaxavgValues) {
        this.minmaxavgValues = minmaxavgValues;
    }

    public List<String> getViewByValues() {
        return viewByValues;
    }

    public void setViewByValues(List<String> viewByValues) {
        this.viewByValues = viewByValues;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getElemntId() {
        return elemntId;
    }

    public void setElemntId(String elemntId) {
        this.elemntId = elemntId;
    }

    public List<String> getGlobalValues() {
        return globalValues;
    }

    public void setGlobalValues(List<String> globalValues) {
        this.globalValues = globalValues;
    }

    public boolean isDesign() {
        return design;
    }

    public void setDesign(boolean design) {
        this.design = design;
    }

    public HashMap<String, ArrayList<String>> getTimeBaseInvidualper() {
        return timeBaseInvidualper;
    }

    public void setTimeBaseInvidual(HashMap<String, ArrayList<String>> timeBaseInvidual) {
        this.timeBaseInvidualper = timeBaseInvidual;
    }

    public HashMap<String, ArrayList<String>> getMeasurValues() {
        return measurValues;
    }

    public void setCurrentValues(HashMap<String, ArrayList<String>> currentValues) {
        this.measurValues = currentValues;
    }

    public HashMap<String, HashMap<String, ArrayList<String>>> getGoalSeekTimeIndvidual() {
        return goalSeekTimeIndvidual;
    }

    public void setGoalSeekTimeIndvidual(HashMap<String, HashMap<String, ArrayList<String>>> goalSeekTimeIndvidual) {
        this.goalSeekTimeIndvidual = goalSeekTimeIndvidual;
    }

    public List<String> getProdAndViewName() {
        return prodAndViewName;
    }

    public void setProdAndViewName(List<String> prodAndViewName) {
        this.prodAndViewName = prodAndViewName;
    }

    public HashMap getTimememdetails() {
        return timememdetails;
    }

    public void setTimememdetails(HashMap timememdetails) {
        this.timememdetails = timememdetails;
    }

    public HashMap getRepDrillMap(String userId) {
        DrillMaps drillMapHelper = new DrillMaps();

        this.repdrillMap = drillMapHelper.getRepDrillForReport(reportId, userId, this.reportRowViewbyValues.get(0), this.reportParametersValues);

        return repdrillMap;
    }

    public String parseDate(String date) throws ParseException {
        DateFormat formatter = new SimpleDateFormat("dd-MMM-yy");
        String formatdate = null;
        Date date1;
        if (date != null) {
            String parsedate = date.substring(date.indexOf(",") + 1).replace(",", "-");
            date1 = (Date) formatter.parse(parsedate);
            SimpleDateFormat sdfDestination = new SimpleDateFormat();
            sdfDestination.applyPattern("dd/MM/yyyy");
            formatdate = sdfDestination.format(date1);
        }
        return formatdate;
    }

    public HashMap<String, ArrayList> getNonViewByMapNew() {
        return nonViewByMapNew;
    }

    public void setNonViewByMapNew(HashMap<String, ArrayList> nonViewByMapNew) {
        this.nonViewByMapNew = nonViewByMapNew;
    }

    public ArrayList getFinalcrosstaborder() {
        return finalcrosstaborder;
    }

    public void setFinalcrosstaborder(ArrayList finalcrosstaborder) {
        this.finalcrosstaborder = finalcrosstaborder;
    }

    public String[] getCrosstabelements(String colviewby) {
        return this.crosstabelements.get(colviewby);
    }

    /**
     * @param crosstabelements the crosstabelements to set
     */
    public void setCrosstabelements(String[] crosstabelements, String colviewby) {
        this.crosstabelements.put(colviewby, crosstabelements);
    }

    public boolean isIsOLAPGraph() {
        return isOLAPGraph;
    }

    /**
     * @param isOLAPGraph the isOLAPGraph to set
     */
    public void setIsOLAPGraph(boolean isOLAPGraph) {
        this.isOLAPGraph = isOLAPGraph;
    }

    /**
     * @return the drillValuesMap
     */
    public HashMap<String, LinkedList> getDrillValuesMap() {
        return drillValuesMap;
    }

    /**
     * @param drillValuesMap the drillValuesMap to set
     */
    public void setDrillValuesMap(HashMap<String, LinkedList> drillValuesMap) {
        this.drillValuesMap = drillValuesMap;
    }

    public void mapAggrigations() {
        for (int i = 0; i < this.reportQryElementIds.size() && i < reportQryAggregations.size(); i++) {
            aggrigationMap.put("A_" + reportQryElementIds.get(i), reportQryAggregations.get(i));
        }
    }

    public HashMap getAggrigationMap() {
        return aggrigationMap;
    }
    /*
     * added by srikanth.p for ReportDrillPopUp It takes the parentContainer
     * updates the child Collection
     */

    public void updateCollection(Container parentContainer, PbReportCollection collection) {
        PbReportCollection parentCollection = parentContainer.getReportCollect();

        ArrayList valueList = new ArrayList();
        for (Object paremId : collection.reportParamIds) {
            List<String> paramValue = (List<String>) reportParametersValues.get(paremId);
            if (parentCollection.reportParamIds.contains(paremId)) {
                if (((List<String>) collection.reportParametersValues.get(paremId)).contains("All")) {
                    List<String> parentParamVal = (List<String>) parentCollection.reportParametersValues.get(paremId);
                    if (!parentParamVal.contains("All")) {
                        collection.reportParametersValues.put(paremId.toString(), parentParamVal);
                        collection.resetParamHashmap.put(paremId, parentCollection.resetParamHashmap.get(paremId));
                        collection.reportParameters.put(paremId.toString(), parentCollection.reportParameters.get(paremId));
                        paramValue = parentParamVal;
                    }
                }
            }
            valueList.add(paramValue);
        }
        ArrayList changedParamValList = new ArrayList();
        for (int i = 0; i < collection.reportParamIds.size(); i++) {
            String value = collection.reportParamNames.get(i) + ":" + valueList.get(i);
            changedParamValList.add(value);
        }
        collection.paramValueList = changedParamValList;
        collection.timeDetailsArray = parentCollection.timeDetailsArray;
        collection.timeDetailsMap = parentCollection.timeDetailsMap;
    }

    //added by nazneen for Cross Dim Sec
    public String getCrossDimEleDetails(String elementIds, HashMap valuesMap) {
//       String val = "";
        StringBuilder val = new StringBuilder();
        String secElementIds[] = elementIds.split(",");
        if (secElementIds.length > 0) {
            for (int p = 0; p < secElementIds.length; p++) {
                String parId = secElementIds[p];
                if (valuesMap.containsKey(parId)) {
                    List<String> paramValues = (List<String>) valuesMap.get(parId);
                    if (!paramValues.contains("All")) {
                        for (String parVal : paramValues) {
//                           val = val + ",'" + parVal +"'";
                            val.append(",'").append(parVal).append("'");
                        }
                    }
                }
            }
            int len = val.length();
            if (len > 0) {
//                val = val.substring(1,len);
                val = new StringBuilder(val.substring(1, len));
            }
        }
        return val.toString();
    }

    public boolean isCheckOverWrite() {
        return checkOverWrite;
    }

    public void setCheckOverWrite(boolean checkOverWrite) {
        this.checkOverWrite = checkOverWrite;
    }

    public List getInValList(String paramId) {
        List inValList = operatorFilters.get("IN").get(paramId);
        return inValList;//!= null ? inValList : new ArrayList();
    }

    public List<String> getNotInValList(String paramId) {
        List notInValList = operatorFilters.get("NOTIN") != null ? operatorFilters.get("NOTIN").get(paramId) : null;
        return notInValList;// != null ? notInValList : new ArrayList();
    }

    public List<String> getLikeValList(String paramId) {
        List likeValList = operatorFilters.get("LIKE") != null ? operatorFilters.get("LIKE").get(paramId) : null;
        return likeValList;// != null ? likeValList : new ArrayList();
    }

    public List<String> getNotLikeValList(String paramId) {
        List notLikeValList = operatorFilters.get("NOTLIKE") != null ? operatorFilters.get("NOTLIKE").get(paramId) : null;
        return notLikeValList;// != null ? notLikeValList : new ArrayList();
    }

    /**
     * @return the reportVersion
     */
    public double getReportVersion() {
        return reportVersion;
    }

    /**
     * @param reportVersion the reportVersion to set
     */
    public void setReportVersion(double reportVersion) {
        this.reportVersion = reportVersion;
    }

    /**
     * @return the currentRepVersion
     */
    public double getCurrentRepVersion() {
        return currentRepVersion;
    }

    /**
     * @param currentRepVersion the currentRepVersion to set
     */
    public void setCurrentRepVersion(double currentRepVersion) {
        this.currentRepVersion = currentRepVersion;
    }
//
//    public boolean isLockDataset() {
//        return lockDataset;
//    }
//
//    public void setLockDataset(boolean lockDataset) {
//        this.lockDataset = lockDataset;
//    }

    public HashMap getDependentviewbyIdQry() {
        return dependentviewbyIdQry;
    }

    public void setDependentviewbyIdQry(HashMap dependentviewbyIdQry) {
        this.dependentviewbyIdQry = dependentviewbyIdQry;
    }

    public String getRepQry() {
        return repQry;
    }

    public void setRepQry(String repQry) {
        this.repQry = repQry;
    }

    /**
     * @return the reportAdvHtmlFileProps
     */
    public String getReportAdvHtmlFileProps() {
        return reportAdvHtmlFileProps;
    }

    /**
     * @param reportAdvHtmlFileProps the reportAdvHtmlFileProps to set
     */
    public void setReportAdvHtmlFileProps(String reportAdvHtmlFileProps) {
        this.reportAdvHtmlFileProps = reportAdvHtmlFileProps;
    }

    public PbReturnObject getParmetersData(String reportId) {
        String sqlstr = "";
        Object[] Obj;
        String finalQuery;
        PbDb db = new PbDb();
        PbReturnObject retObj = null;
        try {
            sqlstr = getResourceBundle().getString("getParamMetaDataQuery1");//modified getParamMetaDataQuery1 by sanhtosh.k on 09-03-2010 for the purpose of building Parameter grouping
            Obj = new Object[1];
            Obj[0] = reportId;
            finalQuery = db.buildQuery(sqlstr, Obj);
//            
            retObj = db.execSelectSQL(finalQuery);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        return retObj;
    }

    /**
     * @return the refreshQryMap
     */
    public String getRefreshQryMap(String viewbyId) {
        return (String) refreshQryMap.get(viewbyId);
    }

    /**
     * @param refreshQryMap the refreshQryMap to set
     */
    public void setRefreshQryMap(String viewbyId, String reportQry) {
        this.refreshQryMap.put(viewbyId, reportQry);
    }

    /**
     * @return the refreshReturnObjectMap
     */
    public ProgenDataSet getRefreshReturnObjectMap(String viewbyId) {
        return (ProgenDataSet) refreshReturnObjectMap.get(viewbyId);
    }

    /**
     * @param refreshReturnObjectMap the refreshReturnObjectMap to set
     */
    public void setRefreshReturnObjectMap(String viewbyId, ProgenDataSet retObj) {
        this.refreshReturnObjectMap.put(viewbyId, retObj);
    }

    public ArrayList quickRefreshDate(String reportId) {
        String sqlstr = "";
        Object[] Obj;
        String finalQuery;
        PbDb db = new PbDb();
        PbReturnObject retObj = null;
        String[] colNames;
        String temp;
        int psize = 0;
        ArrayList timeDetailsArray = new ArrayList();
        try {
            sqlstr = getResourceBundle().getString("getParameterTimeInfo");
            Obj = new Object[1];
            Obj[0] = reportId;

            finalQuery = db.buildQuery(sqlstr, Obj);
            //////.println("finalquery of getparamtimeinfo is : "+finalQuery);
            retObj = db.execSelectSQL(finalQuery);

        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        colNames = retObj.getColumnNames();
        psize = retObj.getRowCount();
        ProgenParam pParam = new ProgenParam();

        HashMap<String, ArrayList<String>> timeDetailsMap = new LinkedHashMap<String, ArrayList<String>>();


        if (psize > 0) {
            timeDetailsArray.add(retObj.getFieldValueString(0, colNames[1]));
            timeDetailsArray.add(retObj.getFieldValueString(0, colNames[2]));
            for (int looper = 0; looper < psize; looper++) {
                ArrayList<String> timeInfo = new ArrayList<String>();
                temp = (retObj.getFieldValueString(looper, colNames[3])).trim();

                if (temp.equalsIgnoreCase("AS_OF_DATE")) {
                    if ("".equals(retObj.getFieldValueString(looper, 7))) {
//                            if (null==(retObj.getFieldValueDateString(0, 7))) {
                        if ((retObj.getFieldValueString(looper, colNames[8]) == null ? "" != null : !retObj.getFieldValueString(looper, colNames[8]).equals(""))) {
                            if (!retObj.getFieldValueString(looper, colNames[8]).equalsIgnoreCase("Month")) {
                                if (!retObj.getFieldValueString(looper, colNames[8]).equalsIgnoreCase("Qtr")) {
                                    if (!retObj.getFieldValueString(looper, colNames[8]).equalsIgnoreCase("Year")) {
                                        if (retObj.getFieldValueString(looper, colNames[8]).equalsIgnoreCase("Yesterday")) {
                                            timeInfo.add(pParam.getcurrentdateforpage(1));
                                        } else if (retObj.getFieldValueString(looper, colNames[8]).equalsIgnoreCase("Tomorrow")) {
                                            timeInfo.add(pParam.getcurrentdateforpage(-1));
                                        } else if (retObj.getFieldValueString(looper, colNames[8]).contains("newSysDate")) {
                                            timeInfo.add(pParam.getcurrentdateforpage(retObj.getFieldValueString(looper, colNames[8])));
                                        } else if (retObj.getFieldValueString(looper, colNames[8]).contains("globalDate")) {
                                            timeInfo.add(pParam.getcurrentdateforpage(retObj.getFieldValueString(looper, colNames[8])));
                                        } else if (retObj.getFieldValueString(looper, colNames[8]).contains("systemDate")) {
                                            timeInfo.add(pParam.getdateforpage());
                                        } else {
                                            timeInfo.add(pParam.getdateforpage());
                                        }
                                    } else {
                                        timeInfo.add(pParam.getdateforpage());
                                    }
                                } else {
                                    timeInfo.add(pParam.getdateforpage());
                                }
                            } else {
                                timeInfo.add(pParam.getdateforpage());
                            }
                        } else {
                            timeInfo.add(pParam.getdateforpage());
                        }

                    } else {
                        String date = retObj.getFieldValueString(looper, 7);
                        timeInfo.add(date);
                    }
                } else if (temp.equalsIgnoreCase("AS_OF_DATE1")) {
                    if ("".equals(retObj.getFieldValueString(looper, 7))) {
                        if ((retObj.getFieldValueString(looper, colNames[8]) == null ? "" != null : !retObj.getFieldValueString(looper, colNames[8]).equals(""))) {
                            if (retObj.getFieldValueString(looper, colNames[8]).equalsIgnoreCase("fromyestrday")) {
                                timeInfo.add(pParam.getcurrentdateforpage(1));
                            } else if (retObj.getFieldValueString(looper, colNames[8]).equalsIgnoreCase("fromtomorow")) {
                                timeInfo.add(pParam.getcurrentdateforpage(-1));
                            } else if (retObj.getFieldValueString(looper, colNames[8]).contains("fromSysDate")) {
                                timeInfo.add(pParam.getcurrentdateforpage(retObj.getFieldValueString(looper, colNames[8])));
                            } else if (retObj.getFieldValueString(looper, colNames[8]).contains("fromglobalDate")) {
                                timeInfo.add(pParam.getcurrentdateforpage(retObj.getFieldValueString(looper, colNames[8])));
                            } else {
                                timeInfo.add(pParam.getdateforpage(30));
                            }
                        }
//                            if (null==(retObj.getFieldValueDateString(0, 7))) {
                    } else {
                        String date = retObj.getFieldValueString(looper, 7);
                        timeInfo.add(date);
                    }

                } else if (temp.equalsIgnoreCase("AS_OF_DATE2")) {
                    if ("".equals(retObj.getFieldValueString(looper, 7))) {
                        if ((retObj.getFieldValueString(looper, colNames[8]) == null ? "" != null : !retObj.getFieldValueString(looper, colNames[8]).equals(""))) {
                            if (retObj.getFieldValueString(looper, colNames[8]).equalsIgnoreCase("toyestrday")) {
                                timeInfo.add(pParam.getcurrentdateforpage(1));
                            } else if (retObj.getFieldValueString(looper, colNames[8]).equalsIgnoreCase("totomorow")) {
                                timeInfo.add(pParam.getcurrentdateforpage(-1));
                            } else if (retObj.getFieldValueString(looper, colNames[8]).contains("toSystDate")) {
                                timeInfo.add(pParam.getcurrentdateforpage(retObj.getFieldValueString(looper, colNames[8])));
                            } else if (retObj.getFieldValueString(looper, colNames[8]).contains("toglobalDdate")) {
                                timeInfo.add(pParam.getcurrentdateforpage(retObj.getFieldValueString(looper, colNames[8])));
                            } else {
                                timeInfo.add(pParam.getdateforpage(1));
                            }
                        }
//                            if (null==(retObj.getFieldValueDateString(0, 7))) {
                    } else {
                        String date = retObj.getFieldValueString(looper, 7);
                        timeInfo.add(date);
                    }

                } else if (temp.equalsIgnoreCase("CMP_AS_OF_DATE1")) {
                    if (retObj.getFieldValueString(looper, colNames[2]) == null || (!retObj.getFieldValueString(looper, colNames[2]).equalsIgnoreCase("PRG_COHORT"))) {
                        String date = "";
                        date = retObj.getFieldValueString(looper, 7);
                        if (!date.equalsIgnoreCase("") && date != null) {
                            timeInfo.add(date);
                        } else {
                            if ((retObj.getFieldValueString(looper, colNames[8]) == null ? "" != null : !retObj.getFieldValueString(looper, colNames[8]).equals(""))) {
                                if (retObj.getFieldValueString(looper, colNames[8]).equalsIgnoreCase("CmpFrmyestrday")) {
                                    timeInfo.add(pParam.getcurrentdateforpage(1));
                                } else if (retObj.getFieldValueString(looper, colNames[8]).equalsIgnoreCase("CmpFrmtomorow")) {
                                    timeInfo.add(pParam.getcurrentdateforpage(-1));
                                } else if (retObj.getFieldValueString(looper, colNames[8]).contains("CmpFrmSysDate")) {
                                    timeInfo.add(pParam.getcurrentdateforpage(retObj.getFieldValueString(looper, colNames[8])));
                                } else if (retObj.getFieldValueString(looper, colNames[8]).contains("CmpFrmglobalDate")) {
                                    timeInfo.add(pParam.getcurrentdateforpage(retObj.getFieldValueString(looper, colNames[8])));
                                } else {
                                    timeInfo.add(pParam.getdateforpage(60));
                                }
                            }
                        }
                    } else {
                        timeInfo.add(pParam.getdateforpage(30));
                    }
                } else if (temp.equalsIgnoreCase("CMP_AS_OF_DATE2")) {
                    if (retObj.getFieldValueString(looper, colNames[2]) == null || (!retObj.getFieldValueString(looper, colNames[2]).equalsIgnoreCase("PRG_COHORT"))) {
                        String date = "";
                        date = retObj.getFieldValueString(looper, 7);
                        if (!date.equalsIgnoreCase("") && date != null) {
                            timeInfo.add(date);
                        } else {
                            if ((retObj.getFieldValueString(looper, colNames[8]) == null ? "" != null : !retObj.getFieldValueString(looper, colNames[8]).equals(""))) {
                                if (retObj.getFieldValueString(looper, colNames[8]).equalsIgnoreCase("cmptoyestrday")) {
                                    timeInfo.add(pParam.getcurrentdateforpage(1));
                                } else if (retObj.getFieldValueString(looper, colNames[8]).equalsIgnoreCase("cmptotomorow")) {
                                    timeInfo.add(pParam.getcurrentdateforpage(-1));
                                } else if (retObj.getFieldValueString(looper, colNames[8]).contains("cmptoSysDate")) {
                                    timeInfo.add(pParam.getcurrentdateforpage(retObj.getFieldValueString(looper, colNames[8])));
                                } else if (retObj.getFieldValueString(looper, colNames[8]).contains("cmptoglobalDate")) {
                                    timeInfo.add(pParam.getcurrentdateforpage(retObj.getFieldValueString(looper, colNames[8])));
                                } else {
                                    timeInfo.add(pParam.getdateforpage(60));
                                }
                            }
                        }
                    } else {
                        timeInfo.add(pParam.getdateforpage(-365));
                    }
                } else if (temp.equalsIgnoreCase("AS_OF_DMONTH1")) {
                    timeInfo.add(pParam.getmonthforpage("30"));
                } else if (temp.equalsIgnoreCase("AS_OF_DMONTH2")) {
                    timeInfo.add(pParam.getmonthforpage("0"));
                } else if (temp.equalsIgnoreCase("CMP_AS_OF_DMONTH1") || temp.equalsIgnoreCase("CMP_AS_OF_DMONTH2")) {
                    timeInfo.add(pParam.getmonthforpage("62"));
                } else if (temp.equalsIgnoreCase("CMP_AS_OF_DMONTH2")) {
                    timeInfo.add(pParam.getmonthforpage("31"));
                } else if (temp.equalsIgnoreCase("AS_OF_DQUARTER1")) {
                    timeInfo.add(pParam.getQtrforpage());
                } else if (temp.equalsIgnoreCase("AS_OF_DQUARTER2")) {
                    timeInfo.add(pParam.getQtrforpage());
                } else if (temp.equalsIgnoreCase("CMP_AS_OF_DQUARTER1") || temp.equalsIgnoreCase("CMP_AS_OF_DQUARTER2")) {
                    timeInfo.add(pParam.getQtrforpage());
                } else if (temp.equalsIgnoreCase("CMP_AS_OF_DQUARTER2")) {
                    timeInfo.add(pParam.getQtrforpage());
                } else if (temp.equalsIgnoreCase("AS_OF_DYEAR1")) {
                    timeInfo.add(pParam.getYearforpage("366"));
                } else if (temp.equalsIgnoreCase("AS_OF_DYEAR2")) {
                    timeInfo.add(pParam.getYearforpage());
                } else if (temp.equalsIgnoreCase("CMP_AS_OF_DYEAR1")) {
                    timeInfo.add(pParam.getYearforpage("734"));
                } else if (temp.equalsIgnoreCase("CMP_AS_OF_DYEAR2")) {
                    timeInfo.add(pParam.getYearforpage("367"));
                } else if (temp.equalsIgnoreCase("AS_OF_MONTH") || temp.equalsIgnoreCase("AS_OF_MONTH1")) {
                    timeInfo.add(pParam.getmonthforpage());
                } else if (temp.equalsIgnoreCase("AS_OF_YEAR") || temp.equalsIgnoreCase("AS_OF_YEAR1")) {
                    timeInfo.add(pParam.getYearforpage());
                } else if (temp.equalsIgnoreCase("PRG_PERIOD_TYPE")) {
                    if ((retObj.getFieldValueString(looper, colNames[8]) == null ? "" != null : !retObj.getFieldValueString(looper, colNames[8]).equals(""))) {
                        timeInfo.add(retObj.getFieldValueString(looper, colNames[8]));
                    } else {
                        timeInfo.add("Month");
                    }
                } else if (temp.equalsIgnoreCase("PRG_DAY_ROLLING")) {
                    timeInfo.add("Last 30 Days");
                } else if (temp.equalsIgnoreCase("PRG_COMPARE")) {
                    if ((retObj.getFieldValueString(looper, colNames[8]) == null ? "" != null : !retObj.getFieldValueString(looper, colNames[8]).equals(""))) {
                        if (retObj.getFieldValueString(looper, colNames[8]).equalsIgnoreCase("Month")) {
                            timeInfo.add("Last Period");
                        } else {
                            timeInfo.add(retObj.getFieldValueString(looper, colNames[8]));
                        }
                    } else {
                        timeInfo.add("Last Period");
                    }
                }

                timeInfo.add("CBO_" + temp);
                timeInfo.add(retObj.getFieldValueString(looper, colNames[4]));
                timeInfo.add(retObj.getFieldValueString(looper, colNames[5]));
                timeInfo.add(retObj.getFieldValueString(looper, colNames[6]));
                timeInfo.add(timeInfo.get(0));

                timeInfo.add(temp);
                timeDetailsMap.put(timeInfo.get(6), timeInfo);
            }
        }
        //////.println("timeDetailsArray is : "+timeDetailsArray);
        ArrayList timeInfo = new ArrayList();
        //////////////Time Calc
        if (!timeDetailsArray.isEmpty()) {
            if (timeDetailsArray.get(0).toString().equalsIgnoreCase("Year") && timeDetailsArray.get(1).toString().equalsIgnoreCase("PRG_YEAR_CMP")) {
                timeInfo = (ArrayList) timeDetailsMap.get("AS_OF_YEAR");
                timeDetailsArray.add(timeInfo.get(0));
                timeInfo = (ArrayList) timeDetailsMap.get("AS_OF_YEAR1");
                if (timeInfo != null) {
                    timeDetailsArray.add(timeInfo.get(0));
                } else {
                    timeDetailsArray.add(null);
                }

            } else if (timeDetailsArray.get(0).toString().equalsIgnoreCase("DAY") && timeDetailsArray.get(1).toString().equalsIgnoreCase("PRG_COHORT")) {
                timeInfo = timeDetailsMap.get("AS_OF_DATE1");
                timeDetailsArray.add(timeInfo.get(0));

                timeInfo = timeDetailsMap.get("AS_OF_DATE2");
                timeDetailsArray.add(timeInfo.get(0));
                if (timeDetailsMap.get("CMP_AS_OF_DATE1") != null) {
                    timeInfo = timeDetailsMap.get("CMP_AS_OF_DATE1");
                    timeDetailsArray.add(timeInfo.get(0));
                } else {
                    timeDetailsArray.add(null);
                }
                if (timeDetailsMap.get("CMP_AS_OF_DATE2") != null) {
                    timeInfo = timeDetailsMap.get("CMP_AS_OF_DATE2");
                    timeDetailsArray.add(timeInfo.get(0));

                } else {
                    timeDetailsArray.add(null);
                }
                if (timeDetailsMap.get("PRG_PERIOD_TYPE") != null) {
                    timeInfo = timeDetailsMap.get("PRG_PERIOD_TYPE");
                    timeDetailsArray.add(timeInfo.get(0));
                } else {
                    timeDetailsArray.add("PERIOD");
                }
            } else if (timeDetailsArray.get(0).toString().equalsIgnoreCase("DAY") && timeDetailsArray.get(1).toString().equalsIgnoreCase("PRG_DATE_RANGE")) {
                timeInfo = (ArrayList) timeDetailsMap.get("AS_OF_DATE1");
                timeDetailsArray.add(timeInfo.get(0));

                timeInfo = (ArrayList) timeDetailsMap.get("AS_OF_DATE2");
                timeDetailsArray.add(timeInfo.get(0));
                if (timeDetailsMap.get("CMP_AS_OF_DATE1") != null) {
                    timeInfo = (ArrayList) timeDetailsMap.get("CMP_AS_OF_DATE1");
                    timeDetailsArray.add(timeInfo.get(0));
                } else {
                    timeDetailsArray.add(null);
                }
                if (timeDetailsMap.get("CMP_AS_OF_DATE2") != null) {
                    timeInfo = (ArrayList) timeDetailsMap.get("CMP_AS_OF_DATE2");
                    timeDetailsArray.add(timeInfo.get(0));

                } else {
                    timeDetailsArray.add(null);
                }
            } else if (timeDetailsArray.get(0).toString().equalsIgnoreCase("DAY") && timeDetailsArray.get(1).toString().equalsIgnoreCase("PRG_MONTH_RANGE")) {
                timeInfo = (ArrayList) timeDetailsMap.get("AS_OF_DMONTH1");
                timeDetailsArray.add(timeInfo.get(0));
                timeInfo = (ArrayList) timeDetailsMap.get("AS_OF_DMONTH2");
                timeDetailsArray.add(timeInfo.get(0));
                if (timeDetailsMap.get("CMP_AS_OF_DMONTH1") != null) {
                    timeInfo = (ArrayList) timeDetailsMap.get("CMP_AS_OF_DMONTH1");
                    timeDetailsArray.add(timeInfo.get(0));
                } else {
                    timeDetailsArray.add(null);
                }
                if (timeDetailsMap.get("CMP_AS_OF_DMONTH2") != null) {
                    timeInfo = (ArrayList) timeDetailsMap.get("CMP_AS_OF_DMONTH2");
                    timeDetailsArray.add(timeInfo.get(0));

                } else {
                    timeDetailsArray.add(null);
                }

            } else if (timeDetailsArray.get(0).toString().equalsIgnoreCase("DAY") && timeDetailsArray.get(1).toString().equalsIgnoreCase("PRG_QUARTER_RANGE")) {
                timeInfo = (ArrayList) timeDetailsMap.get("AS_OF_DQUARTER1");
                timeDetailsArray.add(timeInfo.get(0));
                timeInfo = (ArrayList) timeDetailsMap.get("AS_OF_DQUARTER2");
                timeDetailsArray.add(timeInfo.get(0));
                if (timeDetailsMap.get("CMP_AS_OF_DQUARTER1") != null) {
                    timeInfo = (ArrayList) timeDetailsMap.get("CMP_AS_OF_DQUARTER1");
                    timeDetailsArray.add(timeInfo.get(0));
                } else {
                    timeDetailsArray.add(null);
                }
                if (timeDetailsMap.get("CMP_AS_OF_DQUARTER2") != null) {
                    timeInfo = (ArrayList) timeDetailsMap.get("CMP_AS_OF_DQUARTER2");
                    timeDetailsArray.add(timeInfo.get(0));
                } else {
                    timeDetailsArray.add(null);
                }

            } else if (timeDetailsArray.get(0).toString().equalsIgnoreCase("DAY") && timeDetailsArray.get(1).toString().equalsIgnoreCase("PRG_YEAR_RANGE")) {
                timeInfo = (ArrayList) timeDetailsMap.get("AS_OF_DYEAR1");
                timeDetailsArray.add(timeInfo.get(0));
                timeInfo = (ArrayList) timeDetailsMap.get("AS_OF_DYEAR2");
                timeDetailsArray.add(timeInfo.get(0));
                if (timeDetailsMap.get("CMP_AS_OF_DYEAR1") != null) {
                    timeInfo = (ArrayList) timeDetailsMap.get("CMP_AS_OF_DYEAR1");
                    timeDetailsArray.add(timeInfo.get(0));
                } else {
                    timeDetailsArray.add(null);
                }
                if (timeDetailsMap.get("CMP_AS_OF_DYEAR2") != null) {
                    timeInfo = (ArrayList) timeDetailsMap.get("CMP_AS_OF_DYEAR2");
                    timeDetailsArray.add(timeInfo.get(0));

                } else {
                    timeDetailsArray.add(null);
                }

            } else if (timeDetailsArray.get(0).toString().equalsIgnoreCase("DAY") && timeDetailsArray.get(1).toString().equalsIgnoreCase("PRG_DAY_ROLLING")) {
                timeInfo = (ArrayList) timeDetailsMap.get("AS_OF_DATE");
                timeDetailsArray.add(timeInfo.get(0));

                timeInfo = (ArrayList) timeDetailsMap.get("PRG_DAY_ROLLING");
                timeDetailsArray.add(timeInfo.get(0));


            } else if (timeDetailsArray.get(0).toString().equalsIgnoreCase("DAY") && timeDetailsArray.get(1).toString().equalsIgnoreCase("PRG_STD")) {
                timeInfo = (ArrayList) timeDetailsMap.get("AS_OF_DATE");
                timeDetailsArray.add(timeInfo.get(0));

                timeInfo = (ArrayList) timeDetailsMap.get("PRG_PERIOD_TYPE");
                timeDetailsArray.add(timeInfo.get(0));

                if (timeDetailsMap.get("PRG_COMPARE") != null) {
                    timeInfo = (ArrayList) timeDetailsMap.get("PRG_COMPARE");
                    timeDetailsArray.add(timeInfo.get(0));
                } else {
                    timeDetailsArray.add("Last Period");
                }
            } else if (timeDetailsArray.get(0).toString().equalsIgnoreCase("Month") && timeDetailsArray.get(1).toString().equalsIgnoreCase("PRG_MONTH_CMP")) {
                timeInfo = (ArrayList) timeDetailsMap.get("AS_OF_MONTH");
                timeDetailsArray.add(timeInfo.get(0));

                timeInfo = (ArrayList) timeDetailsMap.get("AS_OF_MONTH1");
                timeDetailsArray.add(timeInfo.get(0));

            } else if (timeDetailsArray.get(0).toString().equalsIgnoreCase("MONTH") && timeDetailsArray.get(1).toString().equalsIgnoreCase("PRG_STD")) {
                timeInfo = (ArrayList) timeDetailsMap.get("AS_OF_MONTH");
                timeDetailsArray.add(timeInfo.get(0));

                timeInfo = (ArrayList) timeDetailsMap.get("PRG_PERIOD_TYPE");
                timeDetailsArray.add(timeInfo.get(0));

                if (timeDetailsMap.get("PRG_COMPARE") != null) {
                    timeInfo = (ArrayList) timeDetailsMap.get("PRG_COMPARE");
                    timeDetailsArray.add(timeInfo.get(0));
                } else {
                    timeDetailsArray.add("Last Period");
                }
            }



        } else {////Time Query Failed as of now only because of prgorn non Standard time
            //ie Proge Time is not used .
            //We will set day level time , but we should find the time levele from time master table
            avoidProgenTime();
        }
        return timeDetailsArray;
    }

    /**
     * @return the crosstabMsrMap
     */
    public HashMap<String, String> getCrosstabMsrMap() {
        return crosstabMsrMap;
    }

    /**
     * @param crosstabMsrMap the crosstabMsrMap to set
     */
    public void setCrosstabMsrMap(HashMap<String, String> crosstabMsrMap) {
        this.crosstabMsrMap = crosstabMsrMap;
    }

    /**
     * @return the hideMeasures
     */
    public ArrayList<String> getHideMeasures() {
        return hideMeasures;
    }

    /**
     * @param hideMeasures the hideMeasures to set
     */
    public void setHideMeasures(ArrayList<String> hideMeasures) {
        this.hideMeasures = hideMeasures;
    }

    /**
     * @return the hideViewbys
     */
    public ArrayList<String> getHideViewbys() {
        return hideViewbys;
    }

    /**
     * @param hideViewbys the hideViewbys to set
     */
    public void setHideViewbys(ArrayList<String> hideViewbys) {
        this.hideViewbys = hideViewbys;
    }

    /**
     * @return the isExcelimportEnable
     */
    public boolean isIsExcelimportEnable() {
        return isExcelimportEnable;
    }

    /**
     * @param isExcelimportEnable the isExcelimportEnable to set
     */
    public void setIsExcelimportEnable(boolean isExcelimportEnable) {
        this.isExcelimportEnable = isExcelimportEnable;
    }
    // Code added by Amar

    public void setLogReadWriterObject(LogReadWriter log4u) {
        this.log4coll = log4u;
    }

    public LogReadWriter getLogReadWriterObject() {
        return this.log4coll;
    }

    public boolean getLogBoolean() {
        return this.logFlag;
    }

    public void setLogBoolean(boolean log) {
        this.logFlag = log;
    }
    //End of code

    public boolean isglobalparamkpi() {
        return isglobalparamkpi;
    }

    public void setisglobalparamkpi(boolean isglobalparamkpi) {
        this.isglobalparamkpi = isglobalparamkpi;
    }
//added by sruthi for multicalendar for reports

    public void setcollectMulticalender(HashMap collectMulticalender) {
        this.collectMulticalender = collectMulticalender;
    }

    public HashMap getcollectMulticalender() {
        return this.collectMulticalender;
    }
//ended by sruthi

    public static String toJavascriptArray(ArrayList<String> arrLst) {
        String[] arr = new String[arrLst.size()];
        arr = arrLst.toArray(arr);
        StringBuffer sb = new StringBuffer();
        sb.append("[");
        for (int i = 0; i < arr.length; i++) {
            sb.append("\"").append(arr[i]).append("\"");
            if (i + 1 < arr.length) {
                sb.append(",");
            }
        }
        sb.append("]");
        return sb.toString();
    }
}//End of class

