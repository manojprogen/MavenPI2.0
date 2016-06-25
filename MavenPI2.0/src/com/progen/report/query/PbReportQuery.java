package com.progen.report.query;

import com.google.common.collect.ArrayListMultimap;
import com.google.gson.Gson;
import com.progen.contypes.GetConnectionType;
import com.progen.datadisplay.db.PbDataDisplayBeanDb;
import com.progen.db.ProgenDataSet;
import com.progen.metadata.Cube;
import com.progen.metadata.CubeInterface;
import com.progen.report.FileReadWrite;
import com.progen.report.PbReportCollection;
import com.progen.report.ReportObjectMeta;
import com.progen.report.ReportObjectQuery;
import com.progen.report.display.DisplayParameters;
import com.progen.reportdesigner.db.ReportTemplateDAO;
import com.progen.reportview.db.PbReportViewerDAO;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.MathContext;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.mbertoli.jfep.ExpressionNode;
import org.mbertoli.jfep.Parser;
import prg.business.group.BusinessGroupDAO;
import prg.business.group.SaveBucketsBD;
import prg.db.Container;
import prg.db.PbDb;
import prg.db.PbReturnObject;
import utils.db.ProgenConnection;

public class PbReportQuery implements Serializable {

    public static Logger logger = Logger.getLogger(PbReportQuery.class);
    private ArrayList qryColumns = new ArrayList();//need to supply
    private ArrayList colAggration = new ArrayList();//need to supply
    private ArrayList rowViewbyCols;//need to supply
    private ArrayList measBys;//need to supply
    private ArrayList measBysNames;//need to supply
    private ArrayList aggregationType;//need to supply
    private ArrayList orgRowViewbyCols;//need to supply
    private ArrayList ColViewbyCols; //need to supply
    private HashMap factandTimetable = new LinkedHashMap();
    private HashMap TimetableCurrClause = new LinkedHashMap();
    private HashMap TimetableftClause = new LinkedHashMap();
    private HashMap TimetablePriorClause = new LinkedHashMap();
    private HashMap TimetableOtherValues = new LinkedHashMap();
    String defaultMeasure = "";//need to supply
    String defaultMeasureSumm = "";//need to supply
    ArrayList timeDetails = new ArrayList();
    HashMap qryColAgg = new HashMap();
    HashMap paramValue = new HashMap();//need to supply
    HashMap excludedParameters = new HashMap();//need to supply
    HashMap qryRefAgg = new HashMap();
    String TimetableId = new String();
    String newTimetableName = new String();
    String TimetableDateColName = new String();
    boolean isDateTrueforNonDt = false;
    ArrayList timedimTable = new ArrayList();
    String currTimeClause = new String();
    String priorTimeClause = new String();
    public boolean isTimeDrill = false;
    PbReportQueryResourceBundle resBundle = new PbReportQueryResourceBundle();
    PbDb pbDb = new PbDb();
    public ArrayList crossTabNonViewBy = new ArrayList();
    public ArrayList crossTabViewByList = new ArrayList();
    public HashMap crossTabNonViewByMap = new HashMap();
    public HashMap NonViewByMap = new HashMap();
    public HashMap timeTableCols = new LinkedHashMap();
    public HashMap timeTableCalTable = new LinkedHashMap();
    public HashMap timeTableDateTable = new LinkedHashMap();
    public HashMap timeTableIndex = new LinkedHashMap();
    public HashMap timeTableColInsql = new LinkedHashMap();
    public HashMap timeTableDays = new LinkedHashMap();
    public boolean isKpi = false;
    public boolean isCohort = false;
    private boolean needPrior = false;
    HashMap altFact = new HashMap();
    //added by susheela start 03-12-09
    private String userId = null;
    PbTimeRanges pbTime = new PbTimeRanges();
    public boolean isTimeSeries = false;
    String qryType = "";//moved to public area
    //added by susheela over 03-12-09
    private String bizRoles = null;// added by santhosh.kumar@progenbusiness.com on 03/12/2009
    private String defaultSortedColumn = null;// added by santhosh.kumar@progenbusiness.com on 05/01/2010
    private String reportId = null;
    private boolean isCompanyValid = false;
    private HashMap ParameterGroupAnalysisHashMap = new HashMap();
    private HashMap bucketAnalysisHashMap = new HashMap();
    private HashMap isSummFact = new HashMap();
    private HashMap isNonTimeSummFact = new HashMap();
    private HashMap summFactLevel = new HashMap();
    private HashMap summFactName = new HashMap();
    private HashMap summFactAllParam = new HashMap();
    private ArrayList summFactAllArray = new ArrayList();
    public boolean isBucketNewViewBy = false;
    public HashMap parameterType = new HashMap();
    public HashMap parameterQuery = new HashMap();
    public boolean isBucketCrossTab = false;
    public boolean isBucketReplace = false;
    public ArrayList bucketColViewbys = new ArrayList();
    public String bucketReplaceIndex = "0";
    public String bucketName = "";
    private String bucketInnerTablesql = "";
    private String bucketWhereClause = "";
    private String bucketelementId = "";
    private boolean repQryisCustomCrossTab = false;
    public boolean isCrossTabBucket = false;
    private boolean selectiveParameters = false;
    private String finalNormalQuery = "";
    private String timeLevel = null;
    HashMap OraFactHintMap = new HashMap();
    public boolean avoidProGenTime = false;
    public boolean forcedAvoidProGenTime = false;
    private HashMap<String, String> sortColAndOrder;
    private ArrayListMultimap<Integer, Integer> colSpanMap;
    private String crosstabGrandTotalDisplay;
    private String crosstabSubTotalDisplay;
    private String reportQuery;
    private int noOfDays;
    private Map<String, Integer> trendNoOfDays;
    private String whereClause;
    public HashMap timememdetails = new HashMap();
    public HashMap resetParamHashmap = new HashMap();
    public boolean isTypeDate = false;
    public boolean isInnerViewBy = false;
    String typeDate = "";
    String clause = "";
    private boolean isReportAccess = true;
    private boolean ismultiPeriodsexists;
    private ArrayList multiViewBys = new ArrayList();
    private HashMap<String, List<String>> inMap = new HashMap<String, List<String>>();
    private HashMap<String, List<String>> notInMap = new HashMap<String, List<String>>();
    private HashMap<String, List<String>> likeMap = new HashMap<String, List<String>>();
    private HashMap<String, List<String>> notLikeMap = new HashMap<String, List<String>>();
    public String gbl_calander_var = "";
    private static final long serialVersionUID = 752647345562459L;
    public String filePath = "";
    String Segmentformula = "";
    String userColType = "";
    String eleId = "";
    //Start of code by Nazneen on 18Jan14 for Analytical Object
    public String finalSql_AO = "";
    public String osql_AO = "";
    public String OViewByCol_AO = new String();
    public String OViewByColGrp_AO = new String();
    public String OorderByCol_AO = new String();
    public String omsql_AO = "";
    public String OmViewByCol_AO = new String();
    public String OmorderByCol_AO = new String();
    public String ColOrderByCol_AO = new String();
    public String osqlGroup_AO = null;
    public String finalViewByCol_AO = new String();
    public String finalSqlNew_AO = new String();
    public String oWrapper_AO = "";
    public String tableName_AO = "";
    public String filters_AO = "";
    public boolean isAOEnable = false;
    public boolean isAOReport = false;
    public boolean isScheduleAO = false;
    public String aoTableId = null;
    public String aoSdate = "";
    public String aoEdate = "";
    public String aoTimePeriod = "daily";
    public HashMap<String, List> inMaps = null;
    public HashMap<String, List> inMapsnew = null;
    public HashMap<String, List> notInMaps = null;
    public HashMap<String, List> likeMaps = null;
    public HashMap<String, List> notLikeMaps = null;
    public ArrayList reportQryElementIds = new ArrayList();
    public String startDate_AO = "";
    public String endDate_AO = "";
    public String timeLevel_AO = "";
    //end of code by Nazneen on 18Jan14 for Analytical Object
    public boolean isDimSegOnSumm = false;
    public String isSummBucket = "N";
    public String finalSqlSummarized_AO = "";
    public String finalSummarizedGrpBy_AO = "";
    public boolean isQueryForGO = false;
    public boolean isQueryForAO = false;
    public String InnerViewbyEleId = "";
    public String fullQueryReport = "";//added by sruthi to display the full query
    public boolean isUngroupedMeasure = false;//added by Mohit for ungrouped measures
    public HashMap collectMulticalender = new HashMap();//added by sruthi for multicalendar
    public HashMap<String, ArrayList> TableDateCalenderMap = new HashMap();
    public boolean needZeroRowQuery = false;
    public String goTimeType = "";

    public boolean isNeedZeroRowQuery() {
        return needZeroRowQuery;
    }

    public void setNeedZeroRowQuery(boolean needZeroRowQuery) {
        this.needZeroRowQuery = needZeroRowQuery;
    }

    public PbReportQuery() {
        //timeDetails.add("Day");
        //timeDetails.add("PRG_STD");
        //timeDetails.add("09/24/2009");
        //timeDetails.add("Month");
        //timeDetails.add("Last Period");
        //timedimTable.add("Day");
    }

    public ArrayList getOrgRowViewbyCols() {
        return orgRowViewbyCols;
    }

    public ArrayList getColViewbyCols() {
        return ColViewbyCols;
    }

    public void setColViewbyCols(ArrayList ColViewbyCols) {
        this.ColViewbyCols = (ArrayList) ColViewbyCols.clone();
    }

    public ArrayList getColAggration() {
        return colAggration;
    }

    public void setColAggration(ArrayList colAggration) {
        this.colAggration = colAggration;
    }

    public ArrayList getQryColumns() {
        return qryColumns;
    }

    public void setQryColumns(ArrayList qryColumns) {
        this.qryColumns = qryColumns;
    }

    public ArrayList getRowViewbyCols() {
        return rowViewbyCols;
    }

    public void setRowViewbyCols(ArrayList rowViewbyCols) {
        this.rowViewbyCols = (ArrayList) rowViewbyCols.clone();
    }

    public ArrayList getmeasBys() {
        return measBys;
    }

    public void setmeasBys(ArrayList measBys) {
        this.measBys = (ArrayList) measBys.clone();
    }

    public ArrayList getmeasBysNames() {
        return measBysNames;
    }

    public void setmeasBysNames(ArrayList measBysNames) {
        this.measBysNames = (ArrayList) measBysNames.clone();
    }

    public ArrayList getaggregationType() {
        return aggregationType;
    }

    public void setaggregationType(ArrayList aggregationType) {
        this.aggregationType = (ArrayList) aggregationType.clone();
    }

    public ArrayList getTimeDetails() {
        return timeDetails;
    }

    public void setTimeDetails(ArrayList timeDetails) {
        this.timeDetails = timeDetails;
    }

    public HashMap getParamValue() {
        return paramValue;
    }

    public void setParamValue(HashMap paramValue) {
        this.paramValue = (HashMap) paramValue.clone();
    }

    public HashMap getExcludedParameters() {
        return excludedParameters;
    }

    public void setExcludedParameters(HashMap excludedParameters) {
        this.excludedParameters = excludedParameters;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
//added by mohit @ 21 dec 2015

    public void setUngroupedMeasure() {
        if (getColAggration() != null && getColAggration().size() > 0) {
            for (int agg1 = 0; agg1 < getColAggration().size(); agg1++) {
                if (getColAggration().get(agg1) == null
                        || getColAggration().get(agg1).toString().equalsIgnoreCase("NULL")) {
                    isUngroupedMeasure = true;

                }
            }
        }
    }

    public void setQueryType() {
        if (getRowViewbyCols() != null && getColViewbyCols() != null) {
            if ((!getRowViewbyCols().isEmpty()) && getColViewbyCols().isEmpty()) {
                qryType = "Simple";
            }
        }
    }

    public void addRowViewBytoOriginalViewby() {
        if (getRowViewbyCols() != null && getRowViewbyCols().size() > 0) {
            for (int cp = 0; cp < getRowViewbyCols().size(); cp++) {
                orgRowViewbyCols.add(rowViewbyCols.get(cp));
            }
        }
    }

    public void addInnerviewToRowViewBy(ArrayList innerViewBy) {
        if (innerViewBy != null && innerViewBy.size() > 0) {
            isInnerViewBy = true;
            for (int iLoop = 0; iLoop < innerViewBy.size(); iLoop++) {
                if ((!rowViewbyCols.contains(innerViewBy.get(iLoop))) && (!ColViewbyCols.contains(innerViewBy.get(iLoop)))) {
                    rowViewbyCols.add(innerViewBy.get(iLoop));
                } else {
                    innerViewBy.remove(iLoop);
                }

            }

        }
    }
    /*
     * ArrayList allTablesforReplace = new ArrayList(); HashMap<String,
     * ArrayList> allTablesforReplaceColumns = new HashMap(); HashMap<String,
     * ArrayList> allTablesforReplacementTables= new HashMap();
     *
     */

    public void addAllTablesforReplace(ArrayList allTablesforReplace, String tableName) {
        logger.info(" called for Table to add " + allTablesforReplace);
        if (allTablesforReplace.isEmpty()) {
            allTablesforReplace.add(tableName);
        } else {
            if (!allTablesforReplace.contains(tableName)) {
                allTablesforReplace.add(tableName);
            }
        }
        logger.info("allTablesforReplace " + allTablesforReplace);
    }

    public void addAllTablesforReplaceColumns(HashMap<String, ArrayList> allTablesforReplaceColumns, String tableName, String columnName) {
        ArrayList addTableColumns = new ArrayList();
        if (allTablesforReplaceColumns.isEmpty()) {
            addTableColumns.add(columnName);
            allTablesforReplaceColumns.put(tableName, addTableColumns);
        } else {
            if (allTablesforReplaceColumns.containsKey(tableName)) {
                addTableColumns = allTablesforReplaceColumns.get(tableName);
                if (!addTableColumns.contains(columnName)) {
                    addTableColumns.add(columnName);
                    allTablesforReplaceColumns.put(tableName, addTableColumns);
                }
            } else {
                addTableColumns.add(columnName);
                allTablesforReplaceColumns.put(tableName, addTableColumns);
            }

        }

    }
    //Added by Mohit

    public void setIsCohort() {
        if (timeDetails.get(0).toString().equalsIgnoreCase("Day") && timeDetails.get(1).toString().equalsIgnoreCase("PRG_COHORT")) {
            isCohort = true;
        }
    }

    public void setnoOfDays() {
        if (qryColumns != null && qryColumns.size() > 0) {
            pbTime.elementID = this.qryColumns.get(0).toString();
            try {
                resolveTimeReference(resolveGroupTimeTable(this.qryColumns.get(0).toString())); //valid only when we have single tim dim table
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            this.noOfDays = pbTime.daysDiff;
        }
    }

    public String generateViewByQry() throws Exception {
        String qryWhereId = "";
        String viewBywhere = null;
        String sqlstr = "";
        String finalQuery = "";
        String colNames[];
        PbReturnObject retObj;
        //   String omsql = null;//cbram
        StringBuilder omsql = null;
        String omkpisql = null;
        //  String osql = null;//cbram
        StringBuilder osql = null;
        //     String osqlGroup = null;//cbram
        StringBuilder osqlGroup = null;
        String bucketOuterQuerynonViewCols = "";
        String bucketOuterQuerynonViewColsGroup = "";
        String[] insql = new String[1];
        String[] pinsql = new String[1];
        String[] insqlGroup = new String[1];
        String[] pinsqlGrou = new String[1];
        Vector facts = new Vector();
        Vector factName = new Vector();
        Vector dimensions = new Vector();
        HashMap CurrCols = new HashMap();
        HashMap PriorCols = new HashMap();
        HashMap ViewByMap = new HashMap();
        HashMap ViewByMap1 = new HashMap();
        HashMap ViewByMapCol = new HashMap();
        DisplayParameters dispParam = new DisplayParameters();
        String colViewQuery = new String();
        String colWhereClause = "";
        String colColumnName = null;
        //    String finalSql = "";//cbram
        StringBuilder finalSql = new StringBuilder();
        //       String fullQuery="";//added by sruthi to display the full query//cbram
        StringBuilder fullQuery = new StringBuilder();
        //     String finalSqlNew = "";//cbram
        StringBuilder finalSqlNew = new StringBuilder();
        String finalSqlSummarized = "";
        String finalSummarizedGrpBy = "";
        boolean CrossTab = false;
        boolean isTrend = false;
        int psize = 0;
        String[] viewinSql = null;
        String viewByCol = null;
        String viewByColNew = null;
        String groupByCol = new String();
        String additionalgroupByCol = new String();
        String groupByColNew = new String();
        String pviewByCol = null;
        String pviewByColNew = null;
        String pgroupByCol = new String();
        String pgroupByColNew = new String();
        String OViewByCol = new String();
        String OViewByColNew = new String();
        String OViewByColNewGrp = new String();
        String OmViewByCol = new String();
        String OmViewByColNew = new String();
        String ColOrderByCol = new String();
        String ColOrderByColNew = new String();
        String finalViewByCol = new String();
        String finalViewByColNew = new String();
        String bucketWhereClause = "";
        String kpiVal = "KPI";
        String summAdditinalClause = "";
        boolean summtrend = false;


        String orderByCol = new String();
        String orderByColNew = new String();
        String porderByCol = new String();
        String porderByColNew = new String();
        String addorderByCol = new String();
        String addorderByColNew = new String();
        String OorderByCol = new String();
        String OorderByColNew = new String();
        String OmorderByCol = new String();
        String OmorderByColNew = new String();
        int totalRowViewBy = 0;

        /////
        String viewByColTarget = null;
        String viewByColTargetNew = null;
        String groupByColTarget = null;
        String orderByColTarget = null;
        String orderByColTargetNew = null;
        HashMap tabTypes = new HashMap();
        HashMap TagetColLookup = new HashMap();
        Vector parameterTargetTable = new Vector();
        HashMap targetTableNames = new HashMap();
        HashMap targetTableNamesParamClause = new HashMap();
        HashMap ParameterGroupAnalysisHashMap = new HashMap();
        HashMap bucketAnalysisHashMap = new HashMap();
        ArrayList bucketQueryeleList = new ArrayList();
        HashMap<String, ArrayList> factRelatedTablesMap = new HashMap();
        HashMap<String, String> factSummrizationTablesClause = new HashMap();
        String replaceIndex = "";
        String bucketReplaceQry = "";
        String BucketviewByCol = "";
        String BucketviewByColNew = "";   //Added by Amar
        String OraFactHint = "";
        String BucketorderByCol = "";
        String BucketorderByColNew = "";
        String BucketgroupByCol = "";
        String BucketOViewByCol = "";
        String BucketOViewByColNew = "";  // Added By Amar
        String BucketouterCoNames = "";
        String BucketouterCoNamesNew = "";   // Added by Amar
        String BucketouterorderCoNames = "";
        String BucketouterorderCoNamesNew = "";
        String BucketOorderByCol = "";
        String BucketOmViewByCol = "";
        String BucketOmorderByCol = "";
        String BucketOorderByColNew = "";
        String BucketOmViewByColNew = "";
        String BucketOmorderByColNew = "";
        /////////  for target fact start
        String BucketfinalViewByCol = "";
        String BucketfinalViewByColNew = "";
        String BucketviewByColTarget = "";
        String BucketviewByColTargetNew = "";
        String BucketorderByColTarget = "";
        String BucketorderByColTargetNew = "";
        String BucketgroupByColTarget = "";
        ArrayList allTablesforReplace = new ArrayList();
        HashMap<String, ArrayList> allTablesforReplaceColumns = new HashMap();
        HashMap<String, HashMap> allTablesforReplacementTables = new HashMap();

        String[] additionalGOViewBy = {"DDATE", "WEEK_DAY_NAME", "MONTH_NAME", "MONTH_SHORT_NAME", "QTR_NAME", "YEAR_NAME", "MONTH_YEAR", "QTR_YEAR_INT", "QTR_YEAR"};
        String additionalViewBy = "";
        String additionalOrderBy = "";
        //String factQueryEx = "inner join ( select * from PR_DAY_EX_INFO ) GO_TIME";
        String factQueryEx = "inner join PR_DAY_EX_INFO GO_TIME";
//        ProgenLog.log(ProgenLog.FINE, this, "generateViewByQry", "Enter Method");
        logger.info("Enter Method");
        ArrayList innerViewBy = getInnerViewBy(reportId);
        if (isQueryForGO) {
            if (InnerViewbyEleId != null && !InnerViewbyEleId.equalsIgnoreCase("null") && !InnerViewbyEleId.equalsIgnoreCase("") && !InnerViewbyEleId.equalsIgnoreCase("NONE")) {
                innerViewBy.add(InnerViewbyEleId);
            }
        }

        String finalSqlAO = "";
        GetConnectionType gettypeofconn = new GetConnectionType();
        String userConnType = null;
        userConnType = getuserConnType(qryColumns, getRowViewbyCols());  // added by Mayank on 19- Aug
if(!isScheduleAO){
        setIsCohort();
        addSecurityClause();
}
        Object[] Obj = null;


        totalRowViewBy = getTotalRowViewBy(getRowViewbyCols(), isInnerViewBy); // added by mayank on 19 Aug
        if (totalRowViewBy == 0) {
            kpiVal = "Total Value";
        }


        setUngroupedMeasure();

        orgRowViewbyCols = new ArrayList();
        addRowViewBytoOriginalViewby();

        addInnerviewToRowViewBy(innerViewBy);


        ParameterGroupAnalysisHashMap = (HashMap) getParameterGroupAnalysisHashMap();
        bucketAnalysisHashMap = (HashMap) getBucketAnalysisHashMap();

        String buckettotoueterwraper = "";

        setQueryType();//added by Mohit
        if (getColViewbyCols() != null && !getColViewbyCols().isEmpty()) {
            for (int i = 0; i < getColViewbyCols().size(); i++) {
                if (getColViewbyCols().get(i).toString().equalsIgnoreCase("Time")) {
                    qryType = "Trend";
                    isTrend = true;
                } else {
                    colWhereClause = getColViewbyCols().get(i).toString();
                }
                CrossTab = false; //Amit Made cross Tab False
                rowViewbyCols.add(getColViewbyCols().get(i).toString());
            }
        }
        if (getRowViewbyCols() != null && getRowViewbyCols().size() != 0) {
            for (int i = 0; i < getRowViewbyCols().size(); i++) {
                if (getRowViewbyCols().get(i) != null) {
                    if (getRowViewbyCols().get(i).toString().equalsIgnoreCase("Time") || getRowViewbyCols().get(i).toString().contains("ExGO")) {
                        totalRowViewBy--;
                        if (!isKpi || isInnerViewBy) {
                            qryType = "Trend";
                        }
                        if (isInnerViewBy && totalRowViewBy < 1) {
                            totalRowViewBy++;
                        }
                    } else {
                        if (viewBywhere == null) {
                            viewBywhere = getRowViewbyCols().get(i).toString();
                        } else {
                            viewBywhere += ", " + getRowViewbyCols().get(i).toString();
                        }
                        summFactAllArray.add(getRowViewbyCols().get(i).toString());
                    }
                }
            }
        }
        // added by Mayank on 24 August
        addElementIdToSummFactArray(likeMap, notLikeMap, parameterQuery, excludedParameters, inMap, notInMap);

        //////////v///////New Maps ////////////////////////////

        // added by Mayank 20August
        // addElementIdToSummFactArray(likeMap,notLikeMap,parameterQuery,excludedParameters);


        HashMap bussTablIds = new HashMap();
        HashMap mems = new HashMap();
        String filterClause = "";
        HashMap memberFilters = new HashMap();
        ArrayList filterMemIds = new ArrayList();
        if (!(getParamValue() == null || getParamValue().isEmpty())) {
            //   String reportFilterQ = "";//cbram
            StringBuilder reportFilterQ = new StringBuilder();
            if (getReportId() != null) {
                //          reportFilterQ = "select MEMBER_ID,MEMBER_VALUE from prg_ar_parameter_security where report_id=" + getReportId();
                reportFilterQ.append("select MEMBER_ID,MEMBER_VALUE from prg_ar_parameter_security where report_id=").append(getReportId());
            }

            PbReturnObject filterObj = new PbReturnObject();
            if (getReportId() != null) {
                //         filterObj = pbDb.execSelectSQL(reportFilterQ);//cbram
                filterObj = pbDb.execSelectSQL(reportFilterQ.toString());
            }
            for (int m = 0; m < filterObj.getRowCount(); m++) {
                String memId2 = (filterObj.getFieldValueString(m, 0));
                String prevClause = "";
                String newClause = "";
                String totalClause = "";
                if (memberFilters.containsKey(memId2)) {
                    prevClause = (String) memberFilters.get(memId2);
                    if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                        newClause = filterObj.getFieldUnknown(m, 1);
                    } else {
                        newClause = filterObj.getFieldValueClobString(m, "MEMBER_VALUE");
                    }
                    totalClause = prevClause + "," + newClause;
                } else {
                    if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                        memberFilters.put(filterObj.getFieldValueString(m, 0), filterObj.getFieldUnknown(m, 1));
                    } else {
                        memberFilters.put(filterObj.getFieldValueString(m, 0), filterObj.getFieldValueClobString(m, "MEMBER_VALUE"));
                    }
                    filterMemIds.add(filterObj.getFieldValueString(m, 0));
                }

            }
            if (isCompanyValid) {

                //   String filterQueryAccount = "select MEMBER_ID,MEMBER_VALUE from prg_account_member_filter where account_no in(select account_type from prg_ar_users where pu_id=" + getUserId() + ") ";//cbram
                StringBuilder filterQueryAccount = new StringBuilder();
                filterQueryAccount.append("select MEMBER_ID,MEMBER_VALUE from prg_account_member_filter where account_no in(select account_type from prg_ar_users where pu_id=").append(getUserId()).append(")");
                if (filterObj.getRowCount() == 0) {
                    filterObj = pbDb.execSelectSQL(filterQueryAccount.toString());
                    for (int m = 0; m < filterObj.getRowCount(); m++) {
                        String memId2 = (filterObj.getFieldValueString(m, 0));
                        String prevClause = "";
                        String newClause = "";
                        String totalClause = "";
                        if (memberFilters.containsKey(memId2)) {
                            prevClause = (String) memberFilters.get(memId2);
                            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                                newClause = filterObj.getFieldUnknown(m, 1);
                            } else {
                                newClause = filterObj.getFieldValueClobString(m, "MEMBER_VALUE");
                            }
                            totalClause = prevClause + "," + newClause;
                        } else {
                            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                                memberFilters.put(filterObj.getFieldValueString(m, 0), filterObj.getFieldUnknown(m, 1));
                            } else {
                                memberFilters.put(filterObj.getFieldValueString(m, 0), filterObj.getFieldValueClobString(m, "MEMBER_VALUE"));
                            }
                            filterMemIds.add(filterObj.getFieldValueString(m, 0));
                        }

                    }

                }
            }

            CubeInterface cubeInterface = new CubeInterface();
            Cube cube = null;
            HashMap filterMembersAtUserHM = new HashMap();
            HashMap filterMembersAtRoleHM = new HashMap();
            if (getBizRoles() != null) {
                if (cube != null) {
                    if (getUserId() != null) {
                        filterMembersAtUserHM = cube.getWhereClauseMembers(Integer.parseInt(getUserId()));
                    }
                }
            }
            Set<String> memBerKeysSet = filterMembersAtUserHM.keySet();
            for (String keyStr : memBerKeysSet) {
                String prevClause = "";
                String newClause = "";
                String totalClause = "";
                if (memberFilters.containsKey(keyStr)) {
                    prevClause = (String) filterMembersAtUserHM.get(keyStr);
                    newClause = (String) memberFilters.get(keyStr);
                    totalClause = prevClause + "," + newClause;
                } else {
                    memberFilters.put(keyStr, (String) filterMembersAtUserHM.get(keyStr));
                    if (!filterMemIds.contains(keyStr)) {
                        filterMemIds.add(keyStr);
                    }
                }

            }
//
            if (cube != null) {
                filterMembersAtRoleHM = cube.getWhereClauseMembers(-1);
            }
            memBerKeysSet = filterMembersAtRoleHM.keySet();
//
            for (String keyStr : memBerKeysSet) {
                String prevClause = "";
                String newClause = "";
                String totalClause = "";
                if (memberFilters.containsKey(keyStr)) {
//
                    prevClause = (String) filterMembersAtRoleHM.get(keyStr);
                    newClause = (String) memberFilters.get(keyStr);
                    totalClause = prevClause + "," + newClause;
                } else {
//
                    memberFilters.put(keyStr, (String) filterMembersAtRoleHM.get(keyStr));
                    if (!filterMemIds.contains(keyStr)) {
                        filterMemIds.add(keyStr);
                    }
                }


            }

            String allEleIds = "";
            String eleIds[] = null;

            eleIds = (String[]) getParamValue().keySet().toArray(new String[0]);

            for (int m = 0; m < eleIds.length; m++) {
                if (!eleIds[m].equalsIgnoreCase("Time")) {
                    allEleIds = allEleIds + "," + eleIds[m].replace("elmnt-", "").trim();
                }
            }
            allEleIds = allEleIds.substring(1);
            if (!(allEleIds == null || allEleIds.equals("") || allEleIds.equalsIgnoreCase("NULL") || allEleIds == "")) {

//                String memQ = "select INFO_MEMBER_ID,INFO_ELEMENT_ID from prg_user_all_adim_details where info_element_id in (" + allEleIds + ")";                                   //cbram
//                String tabQ = "select ELEMENT_ID,BUSS_TABLE_ID,BUSS_COL_ID,BUSS_TABLE_NAME,BUSS_COL_NAME from prg_user_all_info_details  where ELEMENT_ID in (" + allEleIds + ")";   //cbram
                StringBuilder memQ = new StringBuilder();
                StringBuilder tabQ = new StringBuilder();
                memQ.append("select INFO_MEMBER_ID,INFO_ELEMENT_ID from prg_user_all_adim_details where info_element_id in (").append(allEleIds).append(")");
                tabQ.append("select ELEMENT_ID,BUSS_TABLE_ID,BUSS_COL_ID,BUSS_TABLE_NAME,BUSS_COL_NAME from prg_user_all_info_details  where ELEMENT_ID in (").append(allEleIds).append(")");
                PbReturnObject memObj = pbDb.execSelectSQL(memQ.toString());
                PbReturnObject tabObj = pbDb.execSelectSQL(tabQ.toString());
                HashMap membersEle = new HashMap();
                for (int m = 0; m < memObj.getRowCount(); m++) {
                    String memId = memObj.getFieldValueString(m, 0);
                    if (filterMemIds.contains(memId)) {
                        membersEle.put(memObj.getFieldValueString(m, 1), memId);
                        mems.put(memId, memObj.getFieldValueString(m, 1));
                        summFactAllArray.add(memObj.getFieldValueString(m, 1));
                    }
                }
                for (int m = 0; m < tabObj.getRowCount(); m++) {
                    String eleId = tabObj.getFieldValueString(m, 0);
                    ArrayList dets = new ArrayList();
                    dets.add(0, tabObj.getFieldValueString(m, 1));
                    dets.add(1, tabObj.getFieldValueString(m, 2));
                    dets.add(2, tabObj.getFieldValueString(m, 3));
                    dets.add(3, tabObj.getFieldValueString(m, 4));
                    if (membersEle.containsKey(eleId)) {
                        bussTablIds.put(eleId, dets);
                    }
                }

            }

        }

        Vector onlyViewbys = new Vector();//added by bharathi reddy to restrict more columns when drag formula

        if (getQryColumns() != null && !getQryColumns().isEmpty()) {
            for (int i = 0; i < getQryColumns().size(); i++) {
                if (i == 0) {
                    if (getDefaultMeasure() == null) {
                        setDefaultMeasure(getQryColumns().get(i).toString());
                    }
                }
                qryWhereId = qryWhereId + "," + getQryColumns().get(i).toString();///11jan create a vector

                onlyViewbys.add(i, getQryColumns().get(i).toString());
                qryColAgg.put(String.valueOf(getQryColumns().get(i)), getColAggration().get(i));
            }
        }

        if (!(qryWhereId.equalsIgnoreCase(""))) {
            qryWhereId = qryWhereId.substring(1);
        }
        String additionalWhere = "";

        //added by bharathi reddy on 11-01-10 for getting reffered elements

        // ProgenLog.log(ProgenLog.FINE, this, "generateViewByQry", "getreferredelementIds");
        //   String getreferredelementIds = "select REFFERED_ELEMENTS from prg_user_all_info_details  where ELEMENT_ID in(" + qryWhereId + ") and REFFERED_ELEMENTS is not null";//cbram
        StringBuilder getreferredelementIds = new StringBuilder();
        getreferredelementIds.append("select REFFERED_ELEMENTS from prg_user_all_info_details  where ELEMENT_ID in(").append(qryWhereId).append(") and REFFERED_ELEMENTS is not null");
        PbReturnObject refelementspbro = new PbReturnObject();
        if (qryColumns != null && qryColumns.size() > 0) {
            refelementspbro = pbDb.execSelectSQL(getreferredelementIds.toString());
        }
        // ProgenLog.log(ProgenLog.FINE, this, "generateViewByQry", "getreferredelementIds qry fired");
        if (refelementspbro != null) {
            for (int i = 0; i < refelementspbro.getRowCount(); i++) {
                if (!(refelementspbro.getFieldValueString(i, 0).trim().equals("")
                        || refelementspbro.getFieldValueString(i, 0).trim().equalsIgnoreCase("NULL"))) {
                    additionalWhere += "," + refelementspbro.getFieldValueString(i, 0);
                }
            }
        }
        if (!additionalWhere.trim().equalsIgnoreCase("")) {
            additionalWhere = additionalWhere.substring(1);
        }
        if (!additionalWhere.equals("")) {

            qryWhereId += " , " + additionalWhere;
        }
//        if (qryColumns != null && qryColumns.size() > 0) {
//            pbTime.elementID = this.qryColumns.get(0).toString();
//
//            resolveTimeReference(resolveGroupTimeTable(this.qryColumns.get(0).toString()));//valid only when we have single tim dim table
//            this.noOfDays = pbTime.daysDiff;
//        }
        //comment by Mohit bcz of size problem
        setnoOfDays();

        if (isBucketCrossTab) {
            CrossTab = true;
        }
        if (CrossTab) {
            sqlstr = resBundle.getString("generateViewByQry1");
            Obj = new Object[1];
            Obj[0] = colWhereClause;

            //
            if (!("".equals(colWhereClause) || colWhereClause == null || colWhereClause.equalsIgnoreCase("NULL"))) {
                finalQuery = pbDb.buildQuery(sqlstr, Obj);
//
                retObj = pbDb.execSelectSQL(finalQuery);

                colNames = retObj.getColumnNames();

                psize = retObj.getRowCount();
                if (psize > 0) {
                    //Add column view bys for non time qrys

                    if (retObj.getFieldValue(0, colNames[8]) == null) {
                        colColumnName = retObj.getFieldValueString(0, colNames[1]) + "." + retObj.getFieldValueString(0, colNames[2]);
                    } else {
                        colColumnName = "( " + retObj.getFieldValueString(0, colNames[8]) + " ) ";
                    }

                    if (dimensions.isEmpty()) {
                        dimensions.add(retObj.getFieldValueString(0, colNames[0]));
                    } else if (!dimensions.contains(retObj.getFieldValueString(0, colNames[0]))) {
                        dimensions.add(retObj.getFieldValueString(0, colNames[0]));
                    }
                    /*
                     *
                     * ArrayList allTablesforReplace = new ArrayList();
                     * HashMap<String, ArrayList> allTablesforReplaceColumns =
                     * new HashMap(); HashMap<String, ArrayList>
                     * allTablesforReplacementTables= new HashMap();
                     */
                    logger.info(" line 942 for DIM");
                    addAllTablesforReplace(allTablesforReplace, retObj.getFieldValueString(0, colNames[1]));
                    //
                    addAllTablesforReplaceColumns(allTablesforReplaceColumns, retObj.getFieldValueString(0, colNames[1]), retObj.getFieldValueString(0, colNames[2]));
                    //
                }
            }

        }
        if (totalRowViewBy > 0) {
            sqlstr = resBundle.getString("generateViewByQry1");
            Obj = new Object[1];
            Obj[0] = viewBywhere;

            finalQuery = pbDb.buildQuery(sqlstr, Obj);
            retObj = pbDb.execSelectSQL(finalQuery);

            colNames = retObj.getColumnNames();

            psize = retObj.getRowCount();
            if (psize > 0) {
                for (int i = 0; i < psize; i++) {
                    eleId = retObj.getFieldValueString(i, colNames[5]);
                    userColType = retObj.getFieldValueString(i, colNames[10]);
                    if (userColType != null && !userColType.equalsIgnoreCase("") && !userColType.equalsIgnoreCase("null") && userColType.equalsIgnoreCase("CALCULATED")) {
                        PbReportViewerDAO bd = new PbReportViewerDAO();
                        Segmentformula = bd.readFormulaFromFile(eleId, this.filePath);
                        if (userColType != null && !Segmentformula.equalsIgnoreCase("") && !userColType.equalsIgnoreCase("null")) {
                            ViewByMap.put(String.valueOf(retObj.getFieldValueInt(i, colNames[5])), Segmentformula);
                        } else {
                            ViewByMap.put(String.valueOf(retObj.getFieldValueInt(i, colNames[5])), retObj.getFieldValueString(i, colNames[1]) + "." + retObj.getFieldValueString(i, colNames[2]));
                        }
                    } else {
                        ViewByMap.put(String.valueOf(retObj.getFieldValueInt(i, colNames[5])), retObj.getFieldValueString(i, colNames[1]) + "." + retObj.getFieldValueString(i, colNames[2]));
                    }
                    ViewByMap1.put(String.valueOf(retObj.getFieldValueInt(i, colNames[5])), retObj.getFieldValueString(i, colNames[2]));
                    ViewByMapCol.put(String.valueOf(retObj.getFieldValueInt(i, colNames[5])), retObj.getFieldValueString(i, colNames[2]));

                    if (dimensions.isEmpty()) {
                        dimensions.add(retObj.getFieldValueString(i, colNames[0]));
                    } else if (!dimensions.contains(retObj.getFieldValueString(i, colNames[0]))) {
                        dimensions.add(retObj.getFieldValueString(i, colNames[0]));
                    }

                    addAllTablesforReplace(allTablesforReplace, retObj.getFieldValueString(i, colNames[1]));
                    addAllTablesforReplaceColumns(allTablesforReplaceColumns, retObj.getFieldValueString(i, colNames[1]), retObj.getFieldValueString(i, colNames[2]));

                    TagetColLookup.put(retObj.getFieldValueString(i, colNames[5]), String.valueOf(retObj.getFieldValue(i, colNames[11])));

                }
            }
        }
        //       String tabTypeQ ="SELECT distinct A.BUSS_TABLE_ID,A.BUSS_TYPE,A.BUSS_TABLE_NAME FROM prg_grp_buss_table A, prg_user_all_info_details  B where A.buss_table_id = b.BUSS_TABLE_ID and B.ELEMENT_ID IN (" + qryWhereId + ")";//cbram
        StringBuilder tabTypeQ = new StringBuilder();
        tabTypeQ.append("SELECT distinct A.BUSS_TABLE_ID,A.BUSS_TYPE,A.BUSS_TABLE_NAME FROM prg_grp_buss_table A, prg_user_all_info_details  B where A.buss_table_id = b.BUSS_TABLE_ID and B.ELEMENT_ID IN (").append(qryWhereId).append(")");
        PbReturnObject tabObj1 = new PbReturnObject();
        if (qryWhereId != null && qryWhereId.length() > 0) {
            tabObj1 = pbDb.execSelectSQL(tabTypeQ.toString());
        }
        if (tabObj1 != null) {
            for (int g = 0; g < tabObj1.getRowCount(); g++) {
                tabTypes.put(tabObj1.getFieldValueString(g, 0), tabObj1.getFieldValueString(g, 1));
                targetTableNames.put(tabObj1.getFieldValueString(g, 0), tabObj1.getFieldValueString(g, 2));
                if (tabObj1.getFieldValueString(g, 1).trim().equalsIgnoreCase("Target Table")) {
                    parameterTargetTable.add(tabObj1.getFieldValueString(g, 0));
                }
            }
        }
        String bucketgroupByCol = "";
        HashMap dependentFactMap = new HashMap();
        ArrayList<String> dependentFact = new ArrayList<String>();
        ArrayList<String> dependentElement = new ArrayList<String>();
        if (!CrossTab) {
            sqlstr = resBundle.getString("generateViewByQry2");//generateCrossTabMeasure
            Obj = new Object[3];
            Obj[0] = qryWhereId;
            Obj[1] = qryWhereId;
            Obj[2] = qryWhereId;
            finalQuery = pbDb.buildQuery(sqlstr, Obj);

            String CurrFact = "";
            String OldFact = "";
            if (qryWhereId != null && qryWhereId.length() > 0) {
                retObj = pbDb.execSelectSQL(finalQuery);
            } else {
                retObj = new PbReturnObject();
            }
            if (retObj != null && retObj.getRowCount() > 0) {
                colNames = retObj.getColumnNames();
                psize = retObj.getRowCount();

            } else {
                psize = 0;
                colNames = new String[1];
            }

            //
            if (psize > 0) {
                if (!(retObj.getFieldValue(0, colNames[0]) == null || retObj.getFieldValueString(0, colNames[0]).equalsIgnoreCase("") || retObj.getFieldValueString(0, colNames[0]).equalsIgnoreCase("0"))) {
                    OldFact = (retObj.getFieldValueString(0, colNames[0]));
                }

                for (int i = 0; i < psize; i++) {
                    if (!(retObj.getFieldValue(i, colNames[0]) == null || retObj.getFieldValueString(i, colNames[0]).equalsIgnoreCase("") || retObj.getFieldValueString(i, colNames[0]).equalsIgnoreCase("0"))) {
                        CurrFact = (retObj.getFieldValueString(i, colNames[0]));


                        if (!CurrFact.equalsIgnoreCase(OldFact)) {

                            ArrayList addtofact = (ArrayList) summFactAllArray.clone();
                            if (dependentFact != null && dependentFact.size() > 0) {
                                for (int depi = 0; depi < dependentFact.size(); depi++) {
                                    addtofact.add(dependentElement.get(depi));
                                }

                            }
                            summFactAllParam.put(OldFact, addtofact);
                            if (dependentFact != null && dependentFact.size() > 0) {
                                if (!(retObj.getFieldValue(i, colNames[0]) == null || retObj.getFieldValueString(i, colNames[0]).equalsIgnoreCase("") || retObj.getFieldValueString(i, colNames[0]).equalsIgnoreCase("0"))) {
                                    dependentFactMap.put(OldFact, dependentFact);
                                    dependentFact = new ArrayList<String>();
                                    dependentElement = new ArrayList<String>();
                                }

                            }

                            OldFact = CurrFact;
                        }

                    }
                    if (!(retObj.getFieldValue(i, colNames[0]) == null || retObj.getFieldValueString(i, colNames[0]).equalsIgnoreCase("") || retObj.getFieldValueString(i, colNames[0]).equalsIgnoreCase("0"))) {
                        if (!(retObj.getFieldValue(i, colNames[1]) == null || retObj.getFieldValueString(i, colNames[1]).equalsIgnoreCase("") || retObj.getFieldValueString(i, colNames[1]).equalsIgnoreCase("0"))) {

                            summFactName.put(retObj.getFieldValueString(i, colNames[0]), retObj.getFieldValueString(i, colNames[1]));
                        }


                    }
                    if (!dependentFact.contains(retObj.getFieldValueString(i, colNames[12]))) {
                        if (!(retObj.getFieldValue(i, colNames[12]) == null || retObj.getFieldValueString(i, colNames[12]).equalsIgnoreCase("") || retObj.getFieldValueString(i, colNames[12]).equalsIgnoreCase("0"))) {
                            dependentFact.add(retObj.getFieldValueString(i, colNames[12]));
                            dependentElement.add(retObj.getFieldValueString(i, colNames[21]));
                            getTableAndColumnName(retObj.getFieldValueString(i, colNames[21]), allTablesforReplace, allTablesforReplaceColumns);
                        }

                    }
                    /*
                     * addAllTablesforReplace(allTablesforReplace,
                     * retObj.getFieldValueString(0, colNames[1]));
                     * addAllTablesforReplaceColumns(allTablesforReplaceColumns,
                     * retObj.getFieldValueString(0, colNames[1]),
                     * retObj.getFieldValueString(0, colNames[2]));
                     *
                     */
                    // generateViewByQry1.....
                    if (facts.isEmpty()) {
                        if (!(retObj.getFieldValue(i, colNames[0]) == null || retObj.getFieldValueString(i, colNames[0]).equalsIgnoreCase("") || retObj.getFieldValueString(i, colNames[0]).equalsIgnoreCase("0"))) {
                            facts.add(retObj.getFieldValueString(i, colNames[0]));
                            factName.add(retObj.getFieldValueString(i, colNames[1]));
                        }

                    } else {
                        //
                        if (!facts.contains(retObj.getFieldValueString(i, colNames[0]))) {
                            if (!(retObj.getFieldValue(i, colNames[0]) == null || retObj.getFieldValueString(i, colNames[0]).equalsIgnoreCase("") || retObj.getFieldValueString(i, colNames[0]).equalsIgnoreCase("0"))) {
                                facts.add(retObj.getFieldValueString(i, colNames[0]));
                                factName.add(retObj.getFieldValueString(i, colNames[1]));
                            }


                        }
                    }

                    if ((retObj.getFieldValueInt(i, colNames[7])) == 1) {
                        CurrCols.put(retObj.getFieldValueString(i, colNames[6]), formatString(retObj.getFieldValueString(i, colNames[3])));
                    } else if (((retObj.getFieldValueInt(i, colNames[7])) == 2)) {
                        PriorCols.put(retObj.getFieldValueString(i, colNames[6]), formatString(retObj.getFieldValueString(i, colNames[3])));

                    }
                    if (qryColAgg.get(retObj.getFieldValueString(i, colNames[5])) != null) {
                        qryRefAgg.put(retObj.getFieldValueString(i, colNames[6]), qryColAgg.get(retObj.getFieldValueString(i, colNames[5])));
                    } else {
                        qryRefAgg.put(retObj.getFieldValueString(i, colNames[6]), retObj.getFieldValueString(i, colNames[11]));
                    }
                    if (onlyViewbys.contains(retObj.getFieldValueString(i, colNames[5]))) {

                        NonViewByMap.put("A_" + retObj.getFieldValueString(i, colNames[5]), retObj.getFieldValueString(i, colNames[9]));//Bharathi// Take only from vector
                    }

                    /////////Code for timeSmm
                    if (!(retObj.getFieldValue(i, colNames[13]) == null
                            || "".equals(retObj.getFieldValueString(i, colNames[13]))
                            || "NULL".equalsIgnoreCase(retObj.getFieldValueString(i, colNames[13])))) {
                        if (!(retObj.getFieldValue(i, colNames[0]) == null || retObj.getFieldValueString(i, colNames[0]).equalsIgnoreCase("") || retObj.getFieldValueString(i, colNames[0]).equalsIgnoreCase("0"))) {
                            isSummFact.put(retObj.getFieldValueString(i, colNames[0]), retObj.getFieldValueString(i, colNames[13]));
                            summFactLevel.put(retObj.getFieldValueString(i, colNames[0]), retObj.getFieldValueString(i, colNames[14]));


                        }


                    }

                    /////////Code for timeSmm
                    if (!(retObj.getFieldValue(i, colNames[15]) == null
                            || "".equals(retObj.getFieldValueString(i, colNames[15]))
                            || "NULL".equalsIgnoreCase(retObj.getFieldValueString(i, colNames[15])))) {
                        if (!(retObj.getFieldValue(i, colNames[0]) == null || retObj.getFieldValueString(i, colNames[0]).equalsIgnoreCase("") || retObj.getFieldValueString(i, colNames[0]).equalsIgnoreCase("0"))) {
                            isNonTimeSummFact.put(retObj.getFieldValueString(i, colNames[0]), retObj.getFieldValueString(i, colNames[1]));

                        }


                    }

                }////////For Loop Ends here
                ////Copying the fact if it not copied
                ArrayList addtofact = (ArrayList) summFactAllArray.clone();

                if (dependentFact != null && dependentFact.size() > 0) {
                    if (!(OldFact == null || OldFact.equalsIgnoreCase("") || OldFact.equalsIgnoreCase("0"))) {
                        dependentFactMap.put(OldFact, dependentFact);
                        {
                            for (int depi = 0; depi < dependentFact.size(); depi++) {
                                addtofact.add(dependentElement.get(depi).toString());
                            }

                        }
                        ///dependentFact = new ArrayList<String>(); //
                    }

                }
                summFactAllParam.put(OldFact, addtofact);
                addtofact = null;
                //        omsql = new String();//cbram
                omsql = new StringBuilder();
                omkpisql = "";
                //      osql = new String();//cbram
                osql = new StringBuilder();
                //     osqlGroup = "";//cbram
                osqlGroup = new StringBuilder();
                insql = new String[facts.size()];
                pinsql = new String[facts.size()];
                insqlGroup = new String[facts.size()];
                pinsqlGrou = new String[facts.size()];
                String aggFormula = "";
                String aggFormula2 = "";
                int j = 0;
                int dummyNum = 1;
                //
                String currFact = facts.get(j).toString();


                for (int i = 0; i < psize; i++) {
                    String bucketinsql = "";
                    String bucketinsqlGroup = "";
                    if (i == 0) {
                        currFact = retObj.getFieldValueString(i, colNames[1]).toString();
                        for (int k = 0; k < facts.size(); k++) {
                            insql[k] = "";
                            pinsql[k] = "";
                            insqlGroup[k] = "";
                            pinsqlGrou[k] = "";
                            bucketinsql = "";
                            bucketinsqlGroup = "";
                        }
                    } else {
                        if (!(retObj.getFieldValue(i, colNames[1]) == null || retObj.getFieldValueString(i, colNames[1]).equalsIgnoreCase("") || retObj.getFieldValueString(i, colNames[1]).equalsIgnoreCase("0"))) {

                            if (!retObj.getFieldValueString(i, colNames[1]).equalsIgnoreCase(currFact)) {
                                if (!(currFact == null || currFact.equalsIgnoreCase("") || currFact.equalsIgnoreCase("0"))) {
                                    j++;
                                }
                                currFact = retObj.getFieldValueString(i, colNames[1]);

                            }

                        }

                    }

                    String temp = formatString(retObj.getFieldValueString(i, colNames[3]));
                    aggFormula = getAggregation(retObj.getFieldValueString(i, colNames[5]), retObj.getFieldValueString(i, colNames[6]));
                    if (!(aggFormula == null || "".equals(aggFormula.trim()) || "NULL".equalsIgnoreCase(aggFormula))) {
                        aggFormula2 = getLevelFormula(aggFormula, facts.size());
                    }

                    if (aggFormula == null || "".equals(aggFormula.trim()) || "NULL".equalsIgnoreCase(aggFormula)) {
                        for (int k = 0; k < facts.size(); k++) {
                            bucketinsql = "";
                            if (k == j) {
                                if (!(retObj.getFieldValue(i, colNames[8]) == null || retObj.getFieldValueString(i, colNames[8]).trim().equals("")
                                        || retObj.getFieldValueString(i, colNames[8]).trim().equalsIgnoreCase("NULL"))) {

                                    String mTemp = retObj.getFieldValueString(i, colNames[8]);
                                    mTemp = getmTempString(mTemp, userConnType);  // added by Mayank to shorten the code on Sep-29

                                    int mInd = mTemp.indexOf("<!") + 2;
                                    int nInd = mTemp.indexOf("!>");
                                    String GetCurrVal = "";
                                    if (mInd > 1) {
                                        GetCurrVal = mTemp.substring(mInd, nInd);
                                    }




                                    if (mInd > 1) {
                                        mTemp = mTemp.replace("<!" + GetCurrVal + "!>", "'" + getParamValueforCurr(GetCurrVal) + "'");
                                    }

                                    if (mTemp.equalsIgnoreCase("PMAX") || mTemp.equalsIgnoreCase("PMIN")) {
                                        String var1 = "MAX";
                                        if (mTemp.equalsIgnoreCase("PMIN")) {
                                            var1 = "MIN";
                                        }
                                        insql[k] += " , " + var1 + "(" + retObj.getFieldValueString(i, colNames[1]) + "." + retObj.getFieldValueString(i, colNames[2]) + ")   " + " as " + temp + " ";
                                        bucketinsql += ", " + var1 + "(" + retObj.getFieldValueString(i, colNames[1]) + "." + retObj.getFieldValueString(i, colNames[2]) + ")  " + " as " + temp + " ";
                                        pinsql[k] += " , " + var1 + "(" + retObj.getFieldValueString(i, colNames[1]) + "." + retObj.getFieldValueString(i, colNames[2]) + ")   " + " as " + temp + " ";


                                    } else {
                                        insql[k] += " ,   " + mTemp + " as " + temp + " ";
                                        bucketinsql += " ,  " + mTemp + " ";
                                        pinsql[k] += " , " + mTemp + " as " + temp + " ";
                                        {
                                            insql[k] += " , count(" + mTemp + ")   " + " as OPO_" + dummyNum + " ";
                                            bucketinsql += ", count(" + mTemp + ")  " + " as OPO_" + dummyNum + " ";
                                            pinsql[k] += " , count(" + mTemp + ")   " + " as OPO_" + dummyNum + " ";


                                        }



                                        insqlGroup[k] += " , (" + mTemp + " ) ";
                                        bucketinsqlGroup += ", (" + mTemp + ") ";
                                    }

                                } else {

                                    if (!(retObj.getFieldValue(i, colNames[1]) == null || retObj.getFieldValueString(i, colNames[1]).equalsIgnoreCase("")
                                            || retObj.getFieldValueString(i, colNames[1]).equalsIgnoreCase("0"))) {
                                        insql[k] += " , " + retObj.getFieldValueString(i, colNames[1]) + "." + retObj.getFieldValueString(i, colNames[2]) + " as " + temp + " ";
                                        bucketinsql += ", " + retObj.getFieldValueString(i, colNames[1]) + "." + retObj.getFieldValueString(i, colNames[2]) + " as " + temp + " ";



                                        pinsql[k] += " , " + retObj.getFieldValueString(i, colNames[1]) + "." + retObj.getFieldValueString(i, colNames[2]) + " as " + temp + " ";

                                        {
                                            insql[k] += " , count(" + retObj.getFieldValueString(i, colNames[1]) + "." + retObj.getFieldValueString(i, colNames[2]) + ")   " + " as OPO_" + dummyNum + " ";
                                            bucketinsql += ", count(" + retObj.getFieldValueString(i, colNames[1]) + "." + retObj.getFieldValueString(i, colNames[2]) + ")  " + " as OPO_" + dummyNum + " ";
                                            pinsql[k] += " , count(" + retObj.getFieldValueString(i, colNames[1]) + "." + retObj.getFieldValueString(i, colNames[2]) + ")   " + " as OPO_" + dummyNum + " ";


                                        }



                                        insqlGroup[k] += " , " + retObj.getFieldValueString(i, colNames[1]) + "." + retObj.getFieldValueString(i, colNames[2]) + "  ";
                                        bucketinsqlGroup += ", " + retObj.getFieldValueString(i, colNames[1]) + "." + retObj.getFieldValueString(i, colNames[2]) + " ";

                                        ///////Prior will also group and no need prior group

                                    } else {
                                        insql[k] += " ,null  " + temp + " ";
                                        bucketinsql += ",null " + temp + " ";
                                        pinsql[k] += " ,null as " + temp + " ";
                                        {
                                            insql[k] += " ,null*0  " + " as OPO_" + dummyNum + " ";
                                            bucketinsql += ",null*0  " + " as OPO_" + dummyNum + " ";
                                            pinsql[k] += " ,null*0  " + " as OPO_" + dummyNum + " ";

                                        }

                                    }

                                }


                                dummyNum++;



                            } else {
                                insql[k] += " , null as " + temp + " ";
                                bucketinsql += ",null ";
                                pinsql[k] += " ,null as " + temp + " ";
                                String mTemp = retObj.getFieldValueString(i, colNames[8]);
                                if (!(mTemp.equalsIgnoreCase("PMAX") || mTemp.equalsIgnoreCase("PMIN"))) {
                                    insql[k] += " ,null*0  " + " as OPO_" + dummyNum + " ";
                                    bucketinsql += ",null*0  " + " as OPO_" + dummyNum + " ";
                                    pinsql[k] += " ,null*0  " + " as OPO_" + dummyNum + " ";

                                }


                            }

                        }

                        if (!(retObj.getFieldValue(i, colNames[1]) == null || retObj.getFieldValueString(i, colNames[1]).equalsIgnoreCase("") || retObj.getFieldValueString(i, colNames[1]).equalsIgnoreCase("0"))) {
                            if (onlyViewbys.contains(retObj.getFieldValueString(i, colNames[5]))) {
                                String mTemp = retObj.getFieldValueString(i, colNames[8]);
                                if (mTemp.equalsIgnoreCase("PMAX") || mTemp.equalsIgnoreCase("PMIN")) {
                                    String var1 = "MAX";
                                    if (mTemp.equalsIgnoreCase("PMIN")) {
                                        var1 = "MIN";
                                    }
                                    //  osql += " ,  "+var1 +"(" + temp + ") as " + temp + " "; //cbRam
                                    osql.append(" ,  ").append(var1).append("(").append(temp).append(") as ").append(temp).append(" ");

                                } else {
                                    //            osql += " ,  " + temp + " as " + temp + " ";//cbram
                                    osql.append(" ,  ").append(temp).append(" as ").append(temp).append(" ");
                                    {
                                        //            osql += " , count( " + temp + ")  " + " as OPO_" + dummyNum + " ";//cbram
                                        osql.append(" , count( ").append(temp).append(")  ").append(" as OPO_").append(dummyNum).append(" ");
                                    }
                                    bucketOuterQuerynonViewCols += "," + temp;

                                    //         osqlGroup += " ,   " + temp + " ";//cbram
                                    osqlGroup.append(" ,   ").append(temp).append(" ");
                                    bucketOuterQuerynonViewColsGroup += "," + temp;


                                }
                                dummyNum++;

                            }
                        } else {
                            //         osql += " ,"  +retObj.getFieldValueString(i, colNames[8]) +  " as " + temp + " ";//cbram
                            osql.append(" ,").append(retObj.getFieldValueString(i, colNames[8])).append(" as ").append(temp).append(" ");
                            {
                                //                    osql += " , count( " + retObj.getFieldValueString(i, colNames[8]) + ")  " + " as OPO_" + dummyNum + " ";//cbram
                                osql.append(" , count( ").append(retObj.getFieldValueString(i, colNames[8])).append(")  ").append(" as OPO_").append(dummyNum).append(" ");
                            }
                            //        osqlGroup += " ,   " + retObj.getFieldValueString(i, colNames[8]) + " ";//cbram
                            osqlGroup.append(" ,   ").append(retObj.getFieldValueString(i, colNames[8])).append(" ");
                            bucketOuterQuerynonViewCols += "," + temp;
                            dummyNum++;

                        }




                        if (onlyViewbys.contains(retObj.getFieldValueString(i, colNames[5]))) {
                            //          omsql += ", " + temp + " as A_" + retObj.getFieldValueString(i, colNames[5]) + " ";//cbram
                            omsql.append(", ").append(temp).append(" as A_").append(retObj.getFieldValueString(i, colNames[5])).append(" ");
                        }
                        if (onlyViewbys.contains(retObj.getFieldValueString(i, colNames[5]))) {
                            omkpisql += ", MAX( A_" + retObj.getFieldValueString(i, colNames[5]) + ") as A_" + retObj.getFieldValueString(i, colNames[5]) + " ";
                        }


                    } else if ((retObj.getFieldValueInt(i, colNames[7])) == 1) {
                        //
                        for (int k = 0; k < facts.size(); k++) {
                            bucketinsql = "";
                            if (k == j) {
                                if (retObj.getFieldValue(i, colNames[8]) == null
                                        || retObj.getFieldValueString(i, colNames[8]).trim().equals("")
                                        || retObj.getFieldValueString(i, colNames[8]).trim().equalsIgnoreCase("NULL")) {

                                    insql[k] += " , " + aggFormula + " " + retObj.getFieldValueString(i, colNames[1]) + "." + retObj.getFieldValueString(i, colNames[2]) + ") as " + temp + " ";
                                    bucketinsql += " , " + aggFormula + " " + retObj.getFieldValueString(i, colNames[1]) + "." + retObj.getFieldValueString(i, colNames[2]) + ") ";
                                } else {


                                    if (!(retObj.getFieldValue(i, colNames[1]) == null
                                            || retObj.getFieldValueString(i, colNames[1]).equalsIgnoreCase("")
                                            || retObj.getFieldValueString(i, colNames[1]).equalsIgnoreCase("0"))) {
                                        String mTemp = retObj.getFieldValueString(i, colNames[8]);
                                        if (pbTime.st_d != null && pbTime.st_d.trim().length() > 0) {
                                            if (qryType.equalsIgnoreCase("Trend")) {
                                                if (timeDetails.get(0).toString().equalsIgnoreCase("Day")) {
                                                    if (timeDetails.get(1).toString().equalsIgnoreCase("PRG_STD")
                                                            || timeDetails.get(1).toString().equalsIgnoreCase("PRG_COHORT")) {
                                                        int cPos = 3;
                                                        if (timeDetails.get(1).toString().equalsIgnoreCase("PRG_STD")) {
                                                            cPos = 3;
                                                        } else if (timeDetails.get(1).toString().equalsIgnoreCase("PRG_COHORT")) {
                                                            cPos = 6;
                                                        }
                                                        mTemp = replaceDataWithProGenTime(mTemp, timeDetails.get(cPos).toString(), userConnType);
                                                    } else {
                                                        mTemp = mTemp.replace("@PROGENTIME@@ST_DATE", "PROGEN_TIME.DDATE");
                                                        mTemp = mTemp.replace("@PROGENTIME@@ED_DATE", "PROGEN_TIME.DDATE");

                                                    }
                                                } else {
                                                    mTemp = getProgenTimeReplaces(userConnType, mTemp);
                                                }

                                            } else {
                                                mTemp = getProgenTimeReplaces(userConnType, mTemp);
                                            }
                                            if (userConnType.equalsIgnoreCase("ORACLE")) {

                                                mTemp = mTemp.replace("@PROGENTIME@@ED_DATE", "to_date('" + pbTime.ed_d + "','mm/dd/yyyy Hh24:mi:ss ')");
                                            } else {
                                                mTemp = mTemp.replace("@PROGENTIME@@ED_DATE", "convert(datetime,'" + pbTime.ed_d + "',120)");
                                            }
                                        }
                                        System.out.println("mTemp removed" + mTemp);
                                        int mInd = mTemp.indexOf("<!") + 2;
                                        int nInd = mTemp.indexOf("!>");
                                        String GetCurrVal = "";
                                        if (mInd > 1) {
                                            GetCurrVal = mTemp.substring(mInd, nInd);
                                        }




                                        if (mInd > 1) {
                                            mTemp = mTemp.replace("<!" + GetCurrVal + "!>", "'" + getParamValueforCurr(GetCurrVal) + "'");
                                        }


                                        if (retObj.getFieldValueString(i, colNames[10]).equalsIgnoreCase("CALCULATED")
                                                || retObj.getFieldValueString(i, colNames[10]).equalsIgnoreCase("VARCHAR")
                                                || retObj.getFieldValueString(i, colNames[10]).equalsIgnoreCase("VARCHAR2")
                                                || retObj.getFieldValueString(i, colNames[10]).equalsIgnoreCase("DATE")
                                                || retObj.getFieldValueString(i, colNames[10]).equalsIgnoreCase("DATETIME")
                                                || retObj.getFieldValueString(i, colNames[10]).equalsIgnoreCase("NUMBER")) {
                                            insql[k] += " , " + aggFormula + " " + mTemp + ") as " + temp + " ";
                                            bucketinsql += " , " + aggFormula + " " + mTemp + ") ";
                                        } else if (retObj.getFieldValueString(i, colNames[10]).equalsIgnoreCase("TIMECALUCULATED")) {
                                            insql[k] += " ,null  " + temp + " ";
                                            bucketinsql += ",null " + temp + " ";
                                            //////Code for Timebased Formula
                                            {
                                                String inqry = "";
                                                String summdays = "";
                                                for (int timeI = 0; timeI < psize; timeI++) {
                                                    if (i != timeI) {
                                                        String aggFormula12 = getAggregation(retObj.getFieldValueString(timeI, colNames[5]), retObj.getFieldValueString(i, colNames[6]));
                                                        if (aggFormula12 == null || "".equals(aggFormula.trim()) || "NULL".equalsIgnoreCase(aggFormula12)) {
                                                            if (retObj.getFieldValueString(timeI, colNames[0]) != null && retObj.getFieldValueString(timeI, colNames[0]).equals(retObj.getFieldValueString(i, colNames[0]))) {
                                                                inqry += " ,    " + retObj.getFieldValueString(timeI, colNames[1]) + "." + retObj.getFieldValueString(timeI, colNames[2]);
                                                                inqry += " , count(" + retObj.getFieldValueString(timeI, colNames[1]) + "." + retObj.getFieldValueString(timeI, colNames[2]) + ")   ";
                                                            } else {
                                                                inqry += " , null   ";
                                                                inqry += " , null *0  ";
                                                            }


                                                        } else {
                                                            inqry += " , null *0  ";
                                                        }
                                                    } else {
                                                        String tempAgg = aggFormula;
                                                        int cInd = tempAgg.indexOf("#");
                                                        summdays = tempAgg.substring(cInd + 1);
                                                        aggFormula = tempAgg.substring(0, cInd);
                                                        aggFormula = getOracleFormula(aggFormula);
                                                        aggFormula2 = getLevelFormula(aggFormula, facts.size());
                                                        inqry += " , " + aggFormula + " " + retObj.getFieldValueString(i, colNames[8]) + ") as " + temp + " ";

                                                    }


                                                }
                                                timeTableCols.put(retObj.getFieldValueString(i, colNames[5]) + "-" + retObj.getFieldValueString(i, colNames[0]), retObj.getFieldValueString(i, colNames[0]));
                                                timeTableIndex.put(retObj.getFieldValueString(i, colNames[0]), k);
                                                timeTableColInsql.put(retObj.getFieldValueString(i, colNames[5]) + "-" + retObj.getFieldValueString(i, colNames[0]), inqry);
                                                timeTableDays.put(retObj.getFieldValueString(i, colNames[5]) + "-" + retObj.getFieldValueString(i, colNames[0]), summdays);
                                                if (!(retObj.getFieldValue(i, colNames[22]) == null || retObj.getFieldValueString(i, colNames[22]).trim().equals("")
                                                        || retObj.getFieldValueString(i, colNames[22]).trim().equalsIgnoreCase("NULL"))) {
                                                    timeTableCalTable.put(retObj.getFieldValueString(i, colNames[5]) + "-" + retObj.getFieldValueString(i, colNames[0]), retObj.getFieldValueString(i, colNames[22]));
                                                } else {
                                                    timeTableCalTable.put(retObj.getFieldValueString(i, colNames[5]) + "-" + retObj.getFieldValueString(i, colNames[0]), "NO_CAL_TABLE");
                                                }
                                                if (!(retObj.getFieldValue(i, colNames[24]) == null || retObj.getFieldValueString(i, colNames[24]).trim().equals("")
                                                        || retObj.getFieldValueString(i, colNames[24]).trim().equalsIgnoreCase("NULL"))) {
                                                    if (!(retObj.getFieldValue(i, colNames[26]) == null || retObj.getFieldValueString(i, colNames[26]).trim().equals("")
                                                            || retObj.getFieldValueString(i, colNames[26]).trim().equalsIgnoreCase("NULL"))) {
                                                        if (retObj.getFieldValueString(i, colNames[26]).trim().equalsIgnoreCase("DATETIME")) {
                                                            timeTableDateTable.put(retObj.getFieldValueString(i, colNames[5]) + "-" + retObj.getFieldValueString(i, colNames[0]), " trunc(" + retObj.getFieldValueString(i, colNames[24]) + " ) ");
                                                        } else {
                                                            timeTableDateTable.put(retObj.getFieldValueString(i, colNames[5]) + "-" + retObj.getFieldValueString(i, colNames[0]), retObj.getFieldValueString(i, colNames[24]));
                                                        }

                                                    } else {
                                                        timeTableDateTable.put(retObj.getFieldValueString(i, colNames[5]) + "-" + retObj.getFieldValueString(i, colNames[0]), retObj.getFieldValueString(i, colNames[24]));
                                                    }

                                                } else {
                                                    timeTableDateTable.put(retObj.getFieldValueString(i, colNames[5]) + "-" + retObj.getFieldValueString(i, colNames[0]), "NO_DATE_USED");
                                                }



                                            }
                                        } else {
                                            String tempor = mTemp.replace("COUNTDISTINCT(", "count( DISTINCT ");
                                            insql[k] += " , ( " + tempor + ") as " + temp + " ";
                                            bucketinsql += " , ( " + tempor + ") ";

                                        }
                                    } else {


                                        insql[k] += " ,null*0  " + temp + " ";
                                        bucketinsql += ",null*0 " + temp + " ";
                                    }



                                }


                                pinsql[k] += " ,null*0 as " + temp + " ";

                            } else {
                                insql[k] += " , null*0 as " + temp + " ";
                                bucketinsql += ",null*0 ";
                                pinsql[k] += " ,null*0 as " + temp + " ";

                            }

                        }

                        if (!(retObj.getFieldValue(i, colNames[1]) == null
                                || retObj.getFieldValueString(i, colNames[1]).equalsIgnoreCase("")
                                || retObj.getFieldValueString(i, colNames[1]).equalsIgnoreCase("0"))) {
                            if (onlyViewbys.contains(retObj.getFieldValueString(i, colNames[5]))) {
                                //         osql += " ,  " + aggFormula2 + " " + temp + ") as " + temp + " ";//cbram
                                osql.append(" ,  ").append(aggFormula2).append(" ").append(temp).append(") as ").append(temp).append(" ");
                                bucketOuterQuerynonViewCols += "," + temp;
                            }
                        } else {

                            if (retObj.getFieldValueString(i, colNames[10]).equalsIgnoreCase("CALCULATED")) {
                                if (onlyViewbys.contains(retObj.getFieldValueString(i, colNames[5]))) {
                                    //               osql += " , " + aggFormula + " " + retObj.getFieldValueString(i, colNames[8]) + ") as " + temp + " ";//cbram
                                    osql.append(" , ").append(aggFormula).append(" ").append(retObj.getFieldValueString(i, colNames[8])).append(") as ").append(temp).append(" ");
                                    bucketOuterQuerynonViewCols += "," + temp;

                                    String tmp = getProgenTimeReplaces(userConnType, osql.toString());
                                    osql = new StringBuilder();
                                    osql.append(tmp);
                                }
                            } else {
                                String tempor = retObj.getFieldValueString(i, colNames[8]).replace("COUNTDISTINCT(", "count( DISTINCT ");
                                //if (onlyViewbys.contains(retObj.getFieldValueString(i, colNames[5])))
                                {
                                    //       osql += " , ( " + tempor + ") as " + temp + " ";//cbram
                                    osql.append(" , ( ").append(tempor).append(") as ").append(temp).append(" ");
                                    bucketOuterQuerynonViewCols += "," + temp;
                                }

                            }

                        }




                        if (onlyViewbys.contains(retObj.getFieldValueString(i, colNames[5]))) {
                            if (isKpi) {
                                //             omsql += ", " + temp + "  as A_" + retObj.getFieldValueString(i, colNames[5]) + " ";//cbram
                                omsql.append(", ").append(temp).append("  as A_").append(retObj.getFieldValueString(i, colNames[5])).append(" ");
                            } else //                omsql += ", nvl(1.0*" + temp + ",0.0) as A_" + retObj.getFieldValueString(i, colNames[5]) + " ";//cbram
                            {
                                omsql.append(", nvl(1.0*").append(temp).append(",0.0) as A_").append(retObj.getFieldValueString(i, colNames[5])).append(" ");
                            }
                        }
                        if (onlyViewbys.contains(retObj.getFieldValueString(i, colNames[5]))) {
                            omkpisql += ", sum( A_" + retObj.getFieldValueString(i, colNames[5]) + ") as A_" + retObj.getFieldValueString(i, colNames[5]) + " ";
                        }
                    } else if ((retObj.getFieldValueInt(i, colNames[7])) == 2) {
                        needPrior = true;

                        for (int k = 0; k < facts.size(); k++) {
                            if (k == j) {
                                if (retObj.getFieldValue(i, colNames[8]) == null || retObj.getFieldValueString(i, colNames[8]).trim().equals("")
                                        || retObj.getFieldValueString(i, colNames[8]).trim().equalsIgnoreCase("NULL")) {
                                    pinsql[k] += " , " + aggFormula + " " + retObj.getFieldValueString(i, colNames[1]) + "." + retObj.getFieldValueString(i, colNames[2]) + ") as " + temp + " ";
                                    bucketinsql += " , ( " + retObj.getFieldValueString(i, colNames[1]) + "." + retObj.getFieldValueString(i, colNames[2]) + ")";

                                } else {
                                    if (!(retObj.getFieldValue(i, colNames[1]) == null || retObj.getFieldValueString(i, colNames[1]).equalsIgnoreCase("") || retObj.getFieldValueString(i, colNames[1]).equalsIgnoreCase("0"))) {
                                        String mTemp = retObj.getFieldValueString(i, colNames[8]);
                                        if (pbTime.st_d != null && pbTime.st_d.trim().length() > 0) {
                                            if (qryType.equalsIgnoreCase("Trend")) {
                                                if (timeDetails.get(0).toString().equalsIgnoreCase("Day")) {
                                                    if (timeDetails.get(1).toString().equalsIgnoreCase("PRG_STD")
                                                            || timeDetails.get(1).toString().equalsIgnoreCase("PRG_COHORT")) {
                                                        int cPos = 3;
                                                        if (timeDetails.get(1).toString().equalsIgnoreCase("PRG_STD")) {
                                                            cPos = 3;
                                                        } else if (timeDetails.get(1).toString().equalsIgnoreCase("PRG_COHORT")) {
                                                            cPos = 6;
                                                        }
                                                        mTemp = replaceDataWithProGenTime(mTemp, timeDetails.get(cPos).toString(), userConnType);
                                                    } else {
                                                        mTemp = mTemp.replace("@PROGENTIME@@ST_DATE", "(PROGEN_TIME.DDATE -1)");
                                                        mTemp = mTemp.replace("@PROGENTIME@@ED_DATE", "(PROGEN_TIME.DDATE -1)");

                                                    }
                                                } else {
                                                    mTemp = getProgenTimeReplaces(userConnType, mTemp);
                                                }

                                            } else {
                                                mTemp = getProgenTimeReplaces(userConnType, mTemp);

                                            }

                                        }

                                        if (retObj.getFieldValueString(i, colNames[10]).equalsIgnoreCase("CALCULATED")) {
                                            pinsql[k] += " , " + aggFormula + " " + mTemp + ") as " + temp + " ";
                                            bucketinsql += " , ( " + mTemp + ") ";
                                        } else {
                                            String tempor = mTemp.replace("COUNTDISTINCT(", "count( DISTINCT ");
                                            pinsql[k] += " , ( " + tempor + ") as " + temp + " ";
                                            bucketinsql += " , ( " + tempor + ") ";

                                        }

                                    } else {
                                        pinsql[k] += " ,null  " + temp + " ";
                                        bucketinsql += ",null " + temp + " ";
                                    }

                                }

                                insql[k] += " ,null as " + temp + " ";

                            } else {
                                insql[k] += " , null*0 as " + temp + " ";
                                pinsql[k] += " ,null*0 as " + temp + " ";
                                bucketinsql += " null*0 ";

                            }


                        }


                        if (!(retObj.getFieldValue(i, colNames[1]) == null
                                || retObj.getFieldValueString(i, colNames[1]).equalsIgnoreCase("")
                                || retObj.getFieldValueString(i, colNames[1]).equalsIgnoreCase("0"))) {
                            if (onlyViewbys.contains(retObj.getFieldValueString(i, colNames[5]))) {
                                //       osql += " ,  " + aggFormula2 + " " + temp + ") as " + temp + " ";//cbram
                                osql.append(" ,  ").append(aggFormula2).append(" ").append(temp).append(") as ").append(temp).append(" ");
                                bucketOuterQuerynonViewCols += "," + temp;

                            }
                        } else {

                            if (retObj.getFieldValueString(i, colNames[10]).equalsIgnoreCase("CALCULATED")) {
                                if (onlyViewbys.contains(retObj.getFieldValueString(i, colNames[5]))) {
                                    //                       osql += " , " + aggFormula + " " + retObj.getFieldValueString(i, colNames[8]) + ") as " + temp + " ";//cbram
                                    osql.append(" , ").append(aggFormula).append(" ").append(retObj.getFieldValueString(i, colNames[8])).append(") as ").append(temp).append(" ");
                                    bucketOuterQuerynonViewCols += "," + temp;
                                    String tmp = getProgenTimeReplaces(userConnType, osql.toString());
                                    osql = new StringBuilder();
                                    osql.append(tmp);
                                }
                            } else {
                                String tempor = retObj.getFieldValueString(i, colNames[8]).replace("COUNTDISTINCT(", "count( DISTINCT ");
                                //if (onlyViewbys.contains(retObj.getFieldValueString(i, colNames[5])))
                                {
                                    //         osql += " , ( " + tempor + ") as " + temp + " ";//cbram
                                    osql.append(" , ( ").append(tempor).append(") as ").append(temp).append(" ");
                                    bucketOuterQuerynonViewCols += "," + temp;
                                }

                            }

                        }

                        if (onlyViewbys.contains(retObj.getFieldValueString(i, colNames[5]))) {
                            //                    omsql += ", nvl(1.0*" + temp + ",0.0) as A_" + retObj.getFieldValueString(i, colNames[5]) + " ";//cbram
                            omsql.append(", nvl(1.0*").append(temp).append(",0.0) as A_").append(retObj.getFieldValueString(i, colNames[5])).append(" ");
                        }
                        if (onlyViewbys.contains(retObj.getFieldValueString(i, colNames[5]))) {
                            omkpisql += ", sum( A_" + retObj.getFieldValueString(i, colNames[5]) + ") as A_" + retObj.getFieldValueString(i, colNames[5]) + " ";
                        }


                    } else if ((retObj.getFieldValueInt(i, colNames[7])) == 3) {
                        needPrior = true;
                        //
                        for (int k = 0; k < facts.size(); k++) {
                            if (k == j) {
                                pinsql[k] += " ,null*0 as " + temp + " ";
                                insql[k] += " ,null*0 as " + temp + " ";

                            } else {
                                insql[k] += " , null*0 as " + temp + " ";
                                pinsql[k] += " ,null*0 as " + temp + " ";

                            }



                        }
                        if (!retObj.getFieldValueString(i, colNames[10]).equalsIgnoreCase("SUMMARIZED")) {
                            if (onlyViewbys.contains(retObj.getFieldValueString(i, colNames[5]))) {
                                //                    osql += " ,  ( nvl(1.0* " + aggFormula2 + " " + CurrCols.get(retObj.getFieldValueString(i, colNames[6])) + "),0) - nvl(" + aggFormula2 + " " + PriorCols.get(retObj.getFieldValueString(i, colNames[6])) + "),0)) as " + temp + " ";//cbram
                                osql.append(" ,  ( nvl(1.0* ").append(aggFormula2).append(" ").append(CurrCols.get(retObj.getFieldValueString(i, colNames[6]))).append("),0) - nvl(").append(aggFormula2).append(" ").append(PriorCols.get(retObj.getFieldValueString(i, colNames[6]))).append("),0)) as ").append(temp).append(" ");
                                bucketOuterQuerynonViewCols += "," + temp;
                            }

                            if (onlyViewbys.contains(retObj.getFieldValueString(i, colNames[5]))) {
                                if (isKpi) {
                                    //           omsql += ",  " + temp + "  as  A_" + retObj.getFieldValueString(i, colNames[5]) + " ";//cbram
                                    omsql.append(",  ").append(temp).append("  as  A_").append(retObj.getFieldValueString(i, colNames[5])).append(" ");
                                } else //          omsql += ", nvl(1.0*" + temp + ",0.0) as  A_" + retObj.getFieldValueString(i, colNames[5]) + " ";//cbram
                                {
                                    omsql.append(", nvl(1.0*").append(temp).append(",0.0) as  A_").append(retObj.getFieldValueString(i, colNames[5])).append(" ");
                                }
                            }

                            if (onlyViewbys.contains(retObj.getFieldValueString(i, colNames[5]))) {
                                omkpisql += ", sum( A_" + retObj.getFieldValueString(i, colNames[5]) + ") as A_" + retObj.getFieldValueString(i, colNames[5]) + " ";
                            }

                        } else {
                            if (onlyViewbys.contains(retObj.getFieldValueString(i, colNames[5]))) {
                                //        osql += " ,  NULL  as " + temp + " ";//cbram
                                osql.append(" ,  NULL  as ").append(temp).append(" ");
                                bucketOuterQuerynonViewCols += "," + temp;
                            }

                            if (onlyViewbys.contains(retObj.getFieldValueString(i, colNames[5]))) {
                                //                     omsql += ", nvl(1.0*" + CurrCols.get(retObj.getFieldValueString(i, colNames[6])) + ",0.0) - nvl(1.0*" + PriorCols.get(retObj.getFieldValueString(i, colNames[6])) + ",0.0) as  A_" + retObj.getFieldValueString(i, colNames[5]) + " ";//cbram
                                omsql.append(", nvl(1.0*").append(CurrCols.get(retObj.getFieldValueString(i, colNames[6]))).append(",0.0) - nvl(1.0*").append(PriorCols.get(retObj.getFieldValueString(i, colNames[6]))).append(",0.0) as  A_").append(retObj.getFieldValueString(i, colNames[5])).append(" ");
                            }

                            if (onlyViewbys.contains(retObj.getFieldValueString(i, colNames[5]))) {
                                omkpisql += ", sum( A_" + retObj.getFieldValueString(i, colNames[5]) + ") as A_" + retObj.getFieldValueString(i, colNames[5]) + " ";
                            }

                        }

                    } else if ((retObj.getFieldValueInt(i, colNames[7])) == 4) {
                        needPrior = true;
                        //
                        for (int k = 0; k < facts.size(); k++) {
                            if (k == j) {
                                pinsql[k] += " ,null*0 as " + temp + " ";
                                insql[k] += " ,null*0 as " + temp + " ";

                            } else {
                                insql[k] += " , null*0 as " + temp + " ";
                                pinsql[k] += " ,null*0 as " + temp + " ";

                            }

                        }
                        if (!retObj.getFieldValueString(i, colNames[10]).equalsIgnoreCase("SUMMARIZED")) {
                            if (onlyViewbys.contains(retObj.getFieldValueString(i, colNames[5]))) {
                                //                      osql += " ,  ( ( nvl(" + aggFormula2 + " " + CurrCols.get(retObj.getFieldValueString(i, colNames[6])) + "),0) - nvl(" + aggFormula2 + " " + PriorCols.get(retObj.getFieldValueString(i, colNames[6])) + "),0))  ";//cbram
                                osql.append(" ,  ( ( nvl(").append(aggFormula2).append(" ").append(CurrCols.get(retObj.getFieldValueString(i, colNames[6]))).append("),0) - nvl(").append(aggFormula2).append(" ").append(PriorCols.get(retObj.getFieldValueString(i, colNames[6]))).append("),0))  ");

                            }
                            if (onlyViewbys.contains(retObj.getFieldValueString(i, colNames[5]))) {
                                //                       osql += "  )*100.0/ (Case when " + aggFormula2 + " " + PriorCols.get(retObj.getFieldValueString(i, colNames[6])) + ")  =0 then null else " + aggFormula2 + " " + PriorCols.get(retObj.getFieldValueString(i, colNames[6])) + ")  end  ) as " + temp + " ";//cbram
                                osql.append("  )*100.0/ (Case when ").append(aggFormula2).append(" ").append(PriorCols.get(retObj.getFieldValueString(i, colNames[6]))).append(")  =0 then null else ").append(aggFormula2).append(" ").append(PriorCols.get(retObj.getFieldValueString(i, colNames[6]))).append(")  end  ) as ").append(temp).append(" ");
                                bucketOuterQuerynonViewCols += ", " + temp;//---------------------
                            }
                            if (onlyViewbys.contains(retObj.getFieldValueString(i, colNames[5]))) {
                                //          omsql += " , nvl(1.0*" + temp + ",0.0) as  A_" + retObj.getFieldValueString(i, colNames[5]) + " ";//cbram
                                omsql.append(" , nvl(1.0*").append(temp).append(",0.0) as  A_").append(retObj.getFieldValueString(i, colNames[5])).append(" ");
                            }
                            if (onlyViewbys.contains(retObj.getFieldValueString(i, colNames[5]))) {
                                omkpisql += ", sum( A_" + retObj.getFieldValueString(i, colNames[5]) + ") as A_" + retObj.getFieldValueString(i, colNames[5]) + " ";
                            }

                        } else {
                            if (onlyViewbys.contains(retObj.getFieldValueString(i, colNames[5]))) {
                                //           osql += " ,  null  ";//cbram
                                osql.append(" ,  null  ");

                            }
                            if (onlyViewbys.contains(retObj.getFieldValueString(i, colNames[5]))) {
                                //           osql += "  as " + temp + " ";//cbram
                                osql.append("  as ").append(temp).append(" ");
                                bucketOuterQuerynonViewCols += ", " + temp;
                            }
                            if (onlyViewbys.contains(retObj.getFieldValueString(i, colNames[5]))) {
                                //          omsql += " , nvl( ( nvl(1.0*" + CurrCols.get(retObj.getFieldValueString(i, colNames[6])) + " ,0.0) - nvl(" + PriorCols.get(retObj.getFieldValueString(i, colNames[6])) + ",0.0) ) "  //cbram
                                //                + " *100.0/ (Case when  " + PriorCols.get(retObj.getFieldValueString(i, colNames[6])) + "  =0 then null else  " + PriorCols.get(retObj.getFieldValueString(i, colNames[6])) + "  end  ) ,0.0) as  A_" + retObj.getFieldValueString(i, colNames[5]) + " ";//cbram
                                omsql.append(" , nvl( ( nvl(1.0*").append(CurrCols.get(retObj.getFieldValueString(i, colNames[6]))).append(" ,0.0) - nvl(").append(PriorCols.get(retObj.getFieldValueString(i, colNames[6]))).append(",0.0) ) ");
                                omsql.append(" *100.0/ (Case when  ").append(PriorCols.get(retObj.getFieldValueString(i, colNames[6]))).append("  =0 then null else  ").append(PriorCols.get(retObj.getFieldValueString(i, colNames[6]))).append("  end  ) ,0.0) as  A_").append(retObj.getFieldValueString(i, colNames[5])).append(" ");
                            }
                            if (onlyViewbys.contains(retObj.getFieldValueString(i, colNames[5]))) {
                                omkpisql += ", sum( A_" + retObj.getFieldValueString(i, colNames[5]) + ") as A_" + retObj.getFieldValueString(i, colNames[5]) + " ";
                            }

                        }
                    } else {
                        //
                        for (int k = 0; k < facts.size(); k++) {
                            if (k == j) {
                                insql[k] += " , " + aggFormula + " " + retObj.getFieldValueString(i, colNames[1]) + "." + retObj.getFieldValueString(i, colNames[2]) + ") as " + temp + " ";
                                pinsql[k] += " ,null*0 as " + temp + " ";

                            } else {
                                insql[k] += " , null*0 as " + temp + " ";
                                pinsql[k] += " ,null*0 as " + temp + " ";

                            }

                        }

                        if (onlyViewbys.contains(retObj.getFieldValueString(i, colNames[5]))) {
                            //              osql += " ,  " + aggFormula2 + " " + temp + ") as " + temp + " ";//cbram
                            osql.append(" ,  ").append(aggFormula2).append(" ").append(temp).append(") as ").append(temp).append(" ");
                            bucketOuterQuerynonViewCols += "," + temp;
                        }
                        if (onlyViewbys.contains(retObj.getFieldValueString(i, colNames[5]))) {
                            if (isKpi) {
                                //              omsql += ",  " + temp + "  as \"" + retObj.getFieldValueString(i, colNames[5]) + "\" ";//cbram
                                omsql.append(",  ").append(temp).append("  as \"").append(retObj.getFieldValueString(i, colNames[5])).append("\" ");
                            } else //        omsql += ", nvl(1.0*" + temp + ",0.0) as \"" + retObj.getFieldValueString(i, colNames[5]) + "\" ";//cbram
                            {
                                omsql.append(", nvl(1.0*").append(temp).append(",0.0) as \"").append(retObj.getFieldValueString(i, colNames[5])).append("\" ");
                            }
                        }
                        if (onlyViewbys.contains(retObj.getFieldValueString(i, colNames[5]))) {
                            omkpisql += ", sum( A_" + retObj.getFieldValueString(i, colNames[5]) + ") as A_" + retObj.getFieldValueString(i, colNames[5]) + " ";
                        }

                    }
                }//For Loop of Column Data
            }

            viewinSql = new String[facts.size()];
            viewByCol = null;
            viewByColNew = null;
            groupByCol = new String();
            pviewByCol = null;
            pviewByColNew = null;
            groupByCol = new String();
            groupByColNew = new String();
            pgroupByCol = new String();
            pgroupByColNew = new String();
            OViewByCol = new String();
            OViewByColNew = new String();
            OViewByColNewGrp = new String();
            OViewByCol_AO = new String();
            OmViewByCol = new String();
            OmViewByColNew = new String();
            finalViewByCol = new String();
            finalViewByColNew = new String();

            orderByCol = new String();
            OorderByCol = new String();
            OmorderByCol = new String();
            orderByColNew = new String();
            OorderByColNew = new String();
            OmorderByColNew = new String();
            viewByColTarget = new String();
            viewByColTargetNew = new String();
            groupByColTarget = new String();
            orderByColTarget = new String();
            orderByColTargetNew = new String();
            if (!isKpi || (isInnerViewBy)) {
                ////////Get View by clause for simple report

                ArrayList rowviews = getRowViewbyCols();
                if (rowviews != null && rowviews.get(0).toString().equalsIgnoreCase("TIME")) {
                    if (ismultiPeriodsexists) {
                        for (int k = 0; k < multiViewBys.size(); k++) {
                            rowviews.add(multiViewBys.get(k));
                        }
                    }
                }
                for (int i = 0; i < rowviews.size(); i++) {
                    String SummBucketFormula = "";
                    if (!rowviews.get(i).toString().equalsIgnoreCase("Time")
                            && !rowviews.get(i).toString().equalsIgnoreCase("Year")
                            && !rowviews.get(i).toString().equalsIgnoreCase("WEEK")
                            && !rowviews.get(i).toString().equalsIgnoreCase("DAY")
                            && !rowviews.get(i).toString().equalsIgnoreCase("MONTH")
                            && !rowviews.get(i).toString().equalsIgnoreCase("QTR")) {
                        SummBucketFormula = getSummBucketFormula(rowviews.get(i).toString());
                    }
                    if (rowviews.get(i) != null) {
                        if (rowviews.get(i).toString().equalsIgnoreCase("Time")
                                || rowviews.get(i).toString().equalsIgnoreCase("Year")
                                || rowviews.get(i).toString().equalsIgnoreCase("WEEK")
                                || rowviews.get(i).toString().equalsIgnoreCase("DAY")
                                || rowviews.get(i).toString().equalsIgnoreCase("MONTH")
                                || rowviews.get(i).toString().equalsIgnoreCase("QTR")) {
                            ArrayList alist = new ArrayList();
                            if (timeDetails.get(0).toString().equalsIgnoreCase("Day") && (timeDetails.get(1).toString().equalsIgnoreCase("PRG_STD")) || timeDetails.get(1).toString().equalsIgnoreCase("PRG_COHORT")) {
                                if (isTimeSeries) {
                                    timeDetails.add(3, "DAY");
                                }
                                if (timeDetails.get(1).toString().equalsIgnoreCase("PRG_COHORT")) {
                                    alist = pbTime.getTimeViewbycols(timeDetails.get(0).toString(), timeDetails.get(6).toString(), "PERIOD", timeDetails.get(2).toString());
                                } else {
                                    if (ismultiPeriodsexists) {
                                        if (i == 0) {
                                            alist = pbTime.getTimeViewbycols(timeDetails.get(0).toString(), timeDetails.get(3).toString(), timeDetails.get(4).toString(), timeDetails.get(2).toString());
                                        } else {
                                            alist = pbTime.getTimeViewbycols(timeDetails.get(0).toString(), rowviews.get(i).toString(), timeDetails.get(4).toString(), timeDetails.get(2).toString());
                                        }
                                    } else {
                                        alist = pbTime.getTimeViewbycols(timeDetails.get(0).toString(), timeDetails.get(3).toString(), timeDetails.get(4).toString(), timeDetails.get(2).toString());
                                    }

                                }
                            } else {//Need changes for day level

                                alist = pbTime.getTimeViewbycols(timeDetails, isTimeSeries);
                            }
                            if (viewByCol == null) {
                                viewByCol = alist.get(0) + " as  VIEWBY" + (i + 1) + " ";
                                viewByColNew = alist.get(0) + " as TIME ";    // Added by Amar
                                orderByCol = alist.get(1) + " as ORDER" + (i + 1) + " ";
                                orderByColNew = alist.get(1) + " as O_TIME ";
                                groupByCol = alist.get(0) + " , " + alist.get(1) + " ";

                                pviewByCol = alist.get(2) + " as  VIEWBY" + (i + 1) + " ";
                                pviewByColNew = alist.get(2) + " as TIME ";     // Added by Amar
                                porderByCol = alist.get(3) + " as ORDER" + (i + 1) + " ";
                                porderByColNew = alist.get(3) + " as O_TIME";
                                pgroupByCol = alist.get(2) + " , " + alist.get(3) + " ";

                                if (innerViewBy != null && (!innerViewBy.contains(rowviews.get(i).toString()))) {
                                    //
                                    //

                                    if (!isSummBucket.equalsIgnoreCase("Y")) {
                                        OViewByCol = "VIEWBY" + (i + 1) + "";
                                        OViewByColNew = "TIME ";
                                        OViewByColNewGrp = "TIME ";
                                        BucketouterCoNames = "VIEWBY" + (i + 1) + "";
                                        BucketouterCoNamesNew = "TIME ";

                                        OorderByCol = "ORDER" + (i + 1) + "";
                                        OorderByColNew = "O_TIME ";
                                        BucketouterorderCoNames = "ORDER" + (i + 1) + "";
                                        BucketouterorderCoNamesNew = "TIME ";
                                        OmViewByCol = "VIEWBY" + (i + 1) + " as TIME ";
                                        OmViewByColNew = "A_" + alist.get(0) + " as TIME ";
                                        finalViewByCol = " TIME as \"TIME\" ";
                                        finalViewByColNew = " TIME as \"TIME\" ";
                                        if (i < (getRowViewbyCols().size() - getColViewbyCols().size())) {
                                            OmorderByCol = "ORDER" + (i + 1) + "";
                                            OmorderByColNew = "O_TIME";
                                        } else {
                                            if (ColOrderByCol == null || ColOrderByCol.trim().equals("")) {
                                                ColOrderByCol = "ORDER" + (i + 1) + "";
                                                ColOrderByColNew = "O_TIME";
                                            } else {
                                                ColOrderByCol += " , ORDER" + (i + 1) + "";
                                                ColOrderByColNew += " , O_TIME";
                                            }
                                        }
                                    } else {
                                        OViewByCol = "(" + SummBucketFormula + ") AS VIEWBY" + (i + 1) + "";
                                        OViewByColNew = "TIME ";
                                        OViewByColNewGrp = "TIME ";
                                        BucketouterCoNames = "VIEWBY" + (i + 1) + "";
                                        BucketouterCoNamesNew = "TIME ";

                                        OorderByCol = "ORDER" + (i + 1) + "";
                                        OorderByColNew = "O_TIME ";
                                        BucketouterorderCoNames = "ORDER" + (i + 1) + "";
                                        BucketouterorderCoNamesNew = "TIME ";
                                        OmViewByCol = "VIEWBY" + (i + 1) + " as TIME ";
                                        OmViewByColNew = "A_" + alist.get(0) + " as TIME ";
                                        finalViewByCol = " TIME as \"TIME\" ";
                                        finalViewByColNew = " TIME as \"TIME\" ";
                                        if (i < (getRowViewbyCols().size() - getColViewbyCols().size())) {
                                            OmorderByCol = "ORDER" + (i + 1) + "";
                                            OmorderByColNew = "O_TIME";
                                        } else {
                                            if (ColOrderByCol == null || ColOrderByCol.trim().equals("")) {
                                                ColOrderByCol = "ORDER" + (i + 1) + "";
                                                ColOrderByColNew = "O_TIME";
                                            } else {
                                                ColOrderByCol += " , ORDER" + (i + 1) + "";
                                                ColOrderByColNew += " , O_TIME";
                                            }
                                        }
                                    }
                                }

                                /////////  for target fact start
                                viewByColTarget = alist.get(0) + " as  VIEWBY" + (i + 1) + " ";
                                viewByColTargetNew = alist.get(0) + " as  TIME";   //Added by Amar
                                orderByColTarget = alist.get(1) + " as ORDER" + (i + 1) + " ";
                                orderByColTarget = alist.get(1) + " as O_TIME ";
                                groupByColTarget = alist.get(0) + " , " + alist.get(1) + " ";

                                /////////  for target fact

                            } else {
                                viewByCol += " , " + alist.get(0) + " as  VIEWBY" + (i + 1) + " ";
                                viewByColNew += " , " + alist.get(0) + " as  TIME";
                                orderByCol += " , " + alist.get(1) + " as ORDER" + (i + 1) + " ";
                                orderByColNew += " , " + alist.get(1) + " as O_TIME ";
                                groupByCol += " , " + alist.get(0) + " , " + alist.get(1) + " ";

                                pviewByCol += " , " + alist.get(2) + " as  VIEWBY" + (i + 1) + " ";
                                pviewByColNew += " , " + alist.get(2) + " as  TIME ";
                                porderByCol += " , " + alist.get(3) + " as ORDER" + (i + 1) + " ";
                                porderByColNew += " , " + alist.get(3) + " as O_TIME";
                                pgroupByCol += " , " + alist.get(2) + " , " + alist.get(3) + " ";

                                if (innerViewBy != null && (!innerViewBy.contains(getRowViewbyCols().get(i).toString()))) {

                                    if (!isSummBucket.equalsIgnoreCase("Y")) {
                                        OViewByCol += " , " + "VIEWBY" + (i + 1) + "";
                                        OViewByColNew += " , " + "TIME";             // Added by Amar
                                        OViewByColNewGrp += " , " + "TIME";             // Added by Amar
                                        BucketouterCoNames += " , " + "VIEWBY" + (i + 1) + "";
                                        BucketouterCoNamesNew += " , TIME";      // Added by Amar
                                        BucketouterorderCoNames += " , " + "ORDER" + (i + 1) + "";
                                        BucketouterorderCoNamesNew += " , " + "O_TIME";
                                        OorderByCol += " , " + "ORDER" + (i + 1) + "";
                                        OorderByColNew += " , " + "O_TIME";

                                        if (ismultiPeriodsexists) {
                                            OmViewByCol += " , " + "VIEWBY" + (i + 1) + " as TIME_" + i;
                                            OmViewByColNew += " , " + "TIME" + i + " as TIME" + i;
                                            String time = "TIME_" + i;
                                            finalViewByCol += " , TIME_" + i + " as " + "\"" + time + "\"";
                                            finalViewByColNew += " , TIME" + i + " as TIME" + "\"" + i + "\"";
                                        } else {
                                            OmViewByCol += " , " + "VIEWBY" + (i + 1) + " as TIME ";
                                            OmViewByColNew += " , TIME  as TIME ";
                                            finalViewByCol += " , TIME as \"TIME\" ";
                                            finalViewByColNew += " , TIME as \"TIME\" ";
                                        }
                                        if (i < (getRowViewbyCols().size() - getColViewbyCols().size())) {
                                            OmorderByCol += " , " + "ORDER" + (i + 1) + "";
                                            OmorderByColNew += " , " + "O_" + alist.get(1) + "";
                                        } else {
                                            if (ColOrderByCol == null || ColOrderByCol.trim().equals("")) {
                                                ColOrderByCol = "ORDER" + (i + 1) + "";
                                                ColOrderByColNew = "O_TIME";
                                            } else {
                                                ColOrderByCol += " , ORDER" + (i + 1) + "";
                                                ColOrderByColNew += " , O_TIME";
                                            }
                                        }
                                    } else {
                                        OViewByCol += " , (" + SummBucketFormula + ") AS VIEWBY" + (i + 1) + "";
                                        OViewByColNew += " , " + "TIME";             // Added by Amar
                                        OViewByColNewGrp += " , " + "TIME";             // Added by Amar
                                        BucketouterCoNames += " , " + "VIEWBY" + (i + 1) + "";
                                        BucketouterCoNamesNew += " , TIME";      // Added by Amar
                                        BucketouterorderCoNames += " , " + "ORDER" + (i + 1) + "";
                                        BucketouterorderCoNamesNew += " , " + "O_TIME";
                                        OorderByCol += " , " + "ORDER" + (i + 1) + "";
                                        OorderByColNew += " , " + "O_TIME";

                                        if (ismultiPeriodsexists) {
                                            OmViewByCol += " , " + "VIEWBY" + (i + 1) + " as TIME_" + i;
                                            OmViewByColNew += " , " + "TIME" + i + " as TIME" + i;
                                            String time = "TIME_" + i;
                                            finalViewByCol += " , TIME_" + i + " as " + "\"" + time + "\"";
                                            finalViewByColNew += " , TIME" + i + " as TIME" + "\"" + i + "\"";
                                        } else {
                                            OmViewByCol += " , " + "VIEWBY" + (i + 1) + " as TIME ";
                                            OmViewByColNew += " , TIME  as TIME ";
                                            finalViewByCol += " , TIME as \"TIME\" ";
                                            finalViewByColNew += " , TIME as \"TIME\" ";
                                        }
                                        if (i < (getRowViewbyCols().size() - getColViewbyCols().size())) {
                                            OmorderByCol += " , " + "ORDER" + (i + 1) + "";
                                            OmorderByColNew += " , " + "O_" + alist.get(1) + "";
                                        } else {
                                            if (ColOrderByCol == null || ColOrderByCol.trim().equals("")) {
                                                ColOrderByCol = "ORDER" + (i + 1) + "";
                                                ColOrderByColNew = "O_TIME";
                                            } else {
                                                ColOrderByCol += " , ORDER" + (i + 1) + "";
                                                ColOrderByColNew += " , O_TIME";
                                            }
                                        }
                                    }

                                }


                                ///////////  for target fact start
                                viewByColTarget += " , " + alist.get(0) + " as  VIEWBY" + (i + 1) + " ";
                                viewByColTargetNew += " , " + alist.get(0) + " as  A_" + alist.get(0) + " "; // Added By Amar
                                orderByColTarget += " , " + alist.get(1) + " as ORDER" + (i + 1) + " ";
                                orderByColTargetNew += " , " + alist.get(1) + " as O_" + alist.get(1) + " ";
                                groupByColTarget += " , " + alist.get(0) + " , " + alist.get(1) + " ";

                                ////////////  for target fact
                            }
                        } else {
                            //
                            if (viewByCol == null) {


                                // if (!(i == Integer.parseInt(bucketReplaceIndex) && !(replaceIndex.equalsIgnoreCase("all")) && (isBucketReplace))) {
                                if (!isSummBucket.equalsIgnoreCase("Y")) {
                                    viewByCol = ViewByMap.get(String.valueOf(getRowViewbyCols().get(i))) + " as  VIEWBY" + (i + 1) + " ";
                                    viewByColNew = ViewByMap.get(String.valueOf(getRowViewbyCols().get(i))) + " as  A_" + String.valueOf(getRowViewbyCols().get(i)) + " ";
                                    orderByCol = ViewByMap.get(String.valueOf(getRowViewbyCols().get(i))) + " as ORDER" + (i + 1) + " ";
                                    orderByColNew = ViewByMap.get(String.valueOf(getRowViewbyCols().get(i))) + " as O_" + String.valueOf(getRowViewbyCols().get(i)) + " ";
                                    addorderByCol = ViewByMap.get(String.valueOf(getRowViewbyCols().get(i))) + " ";
                                    addorderByColNew = ViewByMap.get(String.valueOf(getRowViewbyCols().get(i))) + " ";
                                    groupByCol = ViewByMap.get(String.valueOf(getRowViewbyCols().get(i))) + " ";

                                    pviewByCol = ViewByMap.get(String.valueOf(getRowViewbyCols().get(i))) + " as  VIEWBY" + (i + 1) + " ";
                                    pviewByColNew = ViewByMap.get(String.valueOf(getRowViewbyCols().get(i))) + " as  A_" + String.valueOf(getRowViewbyCols().get(i)) + " ";
                                    porderByCol = ViewByMap.get(String.valueOf(getRowViewbyCols().get(i))) + " as ORDER" + (i + 1) + " ";
                                    porderByColNew = ViewByMap.get(String.valueOf(getRowViewbyCols().get(i))) + " as O_" + String.valueOf(getRowViewbyCols().get(i)) + " ";
                                    // addorderByCol = ViewByMap.get(String.valueOf(getRowViewbyCols().get(i))) +" " ;
                                    pgroupByCol = ViewByMap.get(String.valueOf(getRowViewbyCols().get(i))) + " ";
                                } else {
                                    viewByCol = " 'KPI' " + " AS VIEWBY" + (i + 1) + " ";
                                    viewByColNew = " 'KPI' " + " as  A_" + String.valueOf(getRowViewbyCols().get(i)) + " ";
                                    orderByCol = " 'KPI' " + " AS ORDER" + (i + 1) + " ";
                                    orderByColNew = " 'KPI' " + " AS O_" + String.valueOf(getRowViewbyCols().get(i)) + " ";
                                    addorderByCol = "ORDER" + (i + 1) + " ";
                                    addorderByColNew = ViewByMap.get(String.valueOf(getRowViewbyCols().get(i))) + " ";
                                    groupByCol = " 'KPI' ";

                                    pviewByCol = " 'KPI' " + " AS VIEWBY" + (i + 1) + " ";
                                    pviewByColNew = " 'KPI' as  A_" + String.valueOf(getRowViewbyCols().get(i)) + " ";
                                    porderByCol = " 'KPI' " + " AS ORDER" + (i + 1) + " ";
                                    porderByColNew = " 'KPI' as O_" + String.valueOf(getRowViewbyCols().get(i)) + " ";
                                    // addorderByCol = ViewByMap.get(String.valueOf(getRowViewbyCols().get(i))) +" " ;
                                    pgroupByCol = " 'KPI'";
                                }

                                if (innerViewBy != null && (!innerViewBy.contains(getRowViewbyCols().get(i).toString()))) {
                                    if (!isSummBucket.equalsIgnoreCase("Y")) {
                                        OViewByCol = "VIEWBY" + (i + 1) + "";
                                        OViewByColNew = "A_" + String.valueOf(getRowViewbyCols().get(i));
                                        OViewByColNewGrp = "A_" + String.valueOf(getRowViewbyCols().get(i));
                                        BucketouterCoNames = "VIEWBY" + (i + 1) + "";
                                        BucketouterCoNamesNew = "A_" + String.valueOf(getRowViewbyCols().get(i));
                                        OorderByCol = "ORDER" + (i + 1) + "";
                                        OorderByColNew = "O_" + String.valueOf(getRowViewbyCols().get(i)) + "";
                                        BucketouterorderCoNames = "ORDER" + (i + 1) + "";
                                        BucketouterorderCoNamesNew = "O_" + String.valueOf(getRowViewbyCols().get(i)) + "";

                                        OmViewByCol = "VIEWBY" + (i + 1) + " as A_" + String.valueOf(getRowViewbyCols().get(i)) + "";
                                        OmViewByColNew = "VIEWBY" + (i + 1) + " as A_" + String.valueOf(getRowViewbyCols().get(i)) + "";
                                        finalViewByCol = "  A_" + String.valueOf(getRowViewbyCols().get(i)) + " as " + "  \"A_" + String.valueOf(getRowViewbyCols().get(i)) + "\" ";
                                        finalViewByColNew = "  A_" + String.valueOf(getRowViewbyCols().get(i)) + " as " + "  \"A_" + String.valueOf(getRowViewbyCols().get(i)) + "\" ";
                                        if (i < (getRowViewbyCols().size() - getColViewbyCols().size())) {
                                            OmorderByCol = "ORDER" + (i + 1) + "";
                                            OmorderByColNew = "O_" + String.valueOf(getRowViewbyCols().get(i)) + "";
//                                    OmorderByColNew = "O_" + String.valueOf(getRowViewbyCols().get(i)) + "";
                                        } else {
                                            if (ColOrderByCol == null || ColOrderByCol.trim().equals("")) {
                                                ColOrderByCol = "ORDER" + (i + 1) + "";
                                                ColOrderByColNew = "O_" + String.valueOf(getRowViewbyCols().get(i)) + "";
//                                        ColOrderByColNew = "O_" + (i + 1) + "";
                                            } else {
                                                ColOrderByCol += " , ORDER" + (i + 1) + "";
                                                ColOrderByColNew += " , O_" + String.valueOf(getRowViewbyCols().get(i)) + "";
//                                        ColOrderByColNew += " , O_" + String.valueOf(getRowViewbyCols().get(i)) + "";
                                            }

                                        }
                                    } else {
                                        OViewByCol = "(" + SummBucketFormula + ") AS VIEWBY" + (i + 1) + "";
                                        OViewByColNew = "(" + SummBucketFormula + ") AS A_" + String.valueOf(getRowViewbyCols().get(i));
                                        OViewByColNewGrp = "(" + SummBucketFormula + ") ";
                                        BucketouterCoNames = "VIEWBY" + (i + 1) + "";
                                        BucketouterCoNamesNew = "A_" + String.valueOf(getRowViewbyCols().get(i));
                                        OorderByCol = "ORDER" + (i + 1) + "";
                                        OorderByColNew = "O_" + String.valueOf(getRowViewbyCols().get(i)) + "";
                                        BucketouterorderCoNames = "ORDER" + (i + 1) + "";
                                        BucketouterorderCoNamesNew = "O_" + String.valueOf(getRowViewbyCols().get(i)) + "";

                                        OmViewByCol = "VIEWBY" + (i + 1) + " as A_" + String.valueOf(getRowViewbyCols().get(i)) + "";
                                        OmViewByColNew = "VIEWBY" + (i + 1) + " as A_" + String.valueOf(getRowViewbyCols().get(i)) + "";
                                        finalViewByCol = "  A_" + String.valueOf(getRowViewbyCols().get(i)) + " as " + "  \"A_" + String.valueOf(getRowViewbyCols().get(i)) + "\" ";
                                        finalViewByColNew = "  A_" + String.valueOf(getRowViewbyCols().get(i)) + " as " + "  \"A_" + String.valueOf(getRowViewbyCols().get(i)) + "\" ";
                                        if (i < (getRowViewbyCols().size() - getColViewbyCols().size())) {
                                            OmorderByCol = "ORDER" + (i + 1) + "";
                                            OmorderByColNew = "O_" + String.valueOf(getRowViewbyCols().get(i)) + "";
//                                    OmorderByColNew = "O_" + String.valueOf(getRowViewbyCols().get(i)) + "";
                                        } else {
                                            if (ColOrderByCol == null || ColOrderByCol.trim().equals("")) {
                                                ColOrderByCol = "ORDER" + (i + 1) + "";
                                                ColOrderByColNew = "O_" + String.valueOf(getRowViewbyCols().get(i)) + "";
//                                        ColOrderByColNew = "O_" + (i + 1) + "";
                                            } else {
                                                ColOrderByCol += " , ORDER" + (i + 1) + "";
                                                ColOrderByColNew += " , O_" + String.valueOf(getRowViewbyCols().get(i)) + "";
//                                        ColOrderByColNew += " , O_" + String.valueOf(getRowViewbyCols().get(i)) + "";
                                            }

                                        }
                                    }

                                }
                                viewByColTarget = " TARGET_MEMBERS as  VIEWBY" + (i + 1) + " ";
                                viewByColTargetNew = " TARGET_MEMBERS as  A_" + String.valueOf(getRowViewbyCols().get(i)) + " ";   // Added By Amar
                                orderByColTarget = " TARGET_MEMBERS as ORDER" + (i + 1) + " ";
                                orderByColTargetNew = " TARGET_MEMBERS as O_" + String.valueOf(getRowViewbyCols().get(i)) + " ";
                                groupByColTarget = "  TARGET_MEMBERS ";
                                if (isBucketNewViewBy) {
                                    if (i == (getRowViewbyCols().size() - 1)) {

                                        if (bucketQueryeleList != null && bucketQueryeleList.size() > 0) {
                                            for (int i1 = 0, j = i + 2; i1 < bucketQueryeleList.size(); i1++, j++) {
                                                if (!String.valueOf(ViewByMap.get(String.valueOf(bucketQueryeleList.get(i1)))).equalsIgnoreCase("") && (ViewByMap.get(String.valueOf(bucketQueryeleList.get(i1))) != null)) {
                                                    BucketviewByCol = ViewByMap.get(String.valueOf(bucketQueryeleList.get(i1))) + " as  VIEWBY" + j + " ";
                                                    BucketviewByColNew = ViewByMap.get(String.valueOf(bucketQueryeleList.get(i1))) + " as  A_" + String.valueOf(bucketQueryeleList.get(i1)) + " ";
                                                    BucketorderByCol = ViewByMap.get(String.valueOf(bucketQueryeleList.get(i1))) + " as ORDER" + j + " ";
                                                    BucketorderByColNew = ViewByMap.get(String.valueOf(bucketQueryeleList.get(i1))) + " as O_" + String.valueOf(bucketQueryeleList.get(i1)) + " ";
                                                    BucketgroupByCol = ViewByMap.get(String.valueOf(bucketQueryeleList.get(i1))) + " ";
                                                    BucketOViewByCol = "VIEWBY" + j + "";
                                                    BucketOViewByColNew = "A_" + String.valueOf(bucketQueryeleList.get(i1)) + "";
                                                    BucketOorderByCol = "ORDER" + j + "";
                                                    BucketOorderByColNew = "O_" + String.valueOf(bucketQueryeleList.get(i1)) + "";
                                                    BucketOmViewByCol = "VIEWBY" + j + " as A_" + String.valueOf(bucketQueryeleList.get(i1)) + " ";
                                                    BucketfinalViewByCol = "  A_" + String.valueOf(bucketQueryeleList.get(i1)) + " ";
                                                    if (i < (getRowViewbyCols().size() - getColViewbyCols().size())) {
                                                        BucketOmorderByCol = "ORDER" + j + "";
                                                        BucketOmorderByColNew = "O_" + String.valueOf(bucketQueryeleList.get(i1)) + "";
                                                    }
                                                    BucketviewByColTarget = ViewByMapCol.get(String.valueOf(bucketQueryeleList.get(i1))) + " as  VIEWBY" + j + " ";
                                                    BucketviewByColTargetNew = ViewByMapCol.get(String.valueOf(bucketQueryeleList.get(i1))) + " as  A_" + String.valueOf(bucketQueryeleList.get(i1)) + " ";
                                                    BucketorderByColTarget = ViewByMapCol.get(String.valueOf(bucketQueryeleList.get(i1))) + " as ORDER" + j + " ";
                                                    BucketorderByColTargetNew = ViewByMapCol.get(String.valueOf(bucketQueryeleList.get(i1))) + " as O_" + String.valueOf(bucketQueryeleList.get(i1)) + " ";
                                                    BucketgroupByColTarget = ViewByMapCol.get(String.valueOf(bucketQueryeleList.get(i1))) + " ";
                                                }
                                                if (!BucketviewByCol.equalsIgnoreCase("")) {
                                                    viewByCol += " , " + BucketviewByCol;
                                                    viewByColNew += " , " + BucketviewByCol;
                                                    orderByCol += " , " + BucketorderByCol;
                                                    orderByColNew += " , " + BucketorderByColNew;
                                                    addorderByCol += " , " + BucketorderByCol;
                                                    addorderByColNew += " , " + BucketorderByColNew;
                                                    groupByCol += " , " + BucketgroupByCol;
                                                    pviewByCol += " , " + BucketviewByCol;
                                                    pviewByColNew += " , " + BucketviewByCol;
                                                    porderByCol += " , " + BucketorderByCol;
                                                    porderByColNew += " , " + BucketorderByColNew;
                                                    pgroupByCol += " , " + BucketgroupByCol;
                                                    if (innerViewBy != null && (!innerViewBy.contains(getRowViewbyCols().get(i).toString()))) {
                                                        OViewByCol += " ," + BucketOViewByCol;
                                                        OViewByColNew += " ," + BucketOViewByCol;
                                                        OViewByColNewGrp += " ," + BucketOViewByCol;
                                                        BucketouterCoNames += " ," + BucketOViewByCol;
                                                        OorderByCol += " , " + BucketOorderByCol;
                                                        OorderByColNew += " , " + BucketOorderByColNew;
                                                        BucketouterorderCoNames += "ORDER" + (i + 1) + "";
                                                        BucketouterorderCoNamesNew += "O_" + getRowViewbyCols().get(i) + "";
                                                        OmViewByCol += " ," + BucketOmViewByCol;
                                                        OmViewByColNew += " ," + BucketOmViewByCol;
                                                        finalViewByCol += ", " + BucketfinalViewByCol;
                                                        finalViewByColNew += ", " + BucketfinalViewByCol;
                                                        if (i < (getRowViewbyCols().size() - getColViewbyCols().size())) {
                                                            OmorderByCol += " , " + BucketOmorderByCol;
                                                            OmorderByColNew += " , " + BucketOmorderByColNew;
                                                        } else {
                                                            if (ColOrderByCol == null || ColOrderByCol.trim().equals("")) {
                                                                ColOrderByCol = BucketOmorderByCol;
                                                                ColOrderByColNew = BucketOmorderByColNew;
                                                            } else {
                                                                ColOrderByCol += " , " + BucketOmorderByCol;
                                                                ColOrderByColNew += " , " + BucketOmorderByColNew;
                                                            }

                                                        }
                                                    }
                                                    viewByColTarget += " , " + BucketviewByColTarget;
                                                    viewByColTargetNew += " , " + BucketviewByColTargetNew;  // Added By Amar
                                                    orderByColTarget += " , " + BucketorderByColTarget;
                                                    orderByColTargetNew += " , " + BucketorderByColTargetNew;
                                                    groupByColTarget += " , " + BucketgroupByColTarget;
                                                }
                                            }
                                        }
                                    }
                                }

                            } else {

                                if (!isSummBucket.equalsIgnoreCase("Y")) {
                                    viewByCol += " , " + ViewByMap.get(String.valueOf(getRowViewbyCols().get(i))) + " as  VIEWBY" + (i + 1) + " ";
                                    viewByColNew += " , " + ViewByMap.get(String.valueOf(getRowViewbyCols().get(i))) + " as  A_" + String.valueOf(getRowViewbyCols().get(i)) + " ";
                                    orderByCol += " , " + ViewByMap.get(String.valueOf(getRowViewbyCols().get(i))) + " as ORDER" + (i + 1) + " ";
                                    orderByColNew += " , " + ViewByMap.get(String.valueOf(getRowViewbyCols().get(i))) + " as O_" + String.valueOf(getRowViewbyCols().get(i)) + " ";
                                    addorderByCol += " , " + ViewByMap.get(String.valueOf(getRowViewbyCols().get(i)));
                                    addorderByColNew += " , " + ViewByMap.get(String.valueOf(getRowViewbyCols().get(i)));
                                    groupByCol += " , " + ViewByMap.get(String.valueOf(getRowViewbyCols().get(i))) + " ";

                                    pviewByCol += " , " + ViewByMap.get(String.valueOf(getRowViewbyCols().get(i))) + " as  VIEWBY" + (i + 1) + " ";
                                    pviewByColNew += " , " + ViewByMap.get(String.valueOf(getRowViewbyCols().get(i))) + " as  A_" + String.valueOf(getRowViewbyCols().get(i)) + " ";
                                    porderByCol += " , " + ViewByMap.get(String.valueOf(getRowViewbyCols().get(i))) + " as ORDER" + (i + 1) + " ";
                                    porderByColNew += " , " + ViewByMap.get(String.valueOf(getRowViewbyCols().get(i))) + " as O_" + String.valueOf(getRowViewbyCols().get(i)) + " ";
                                   pgroupByCol += " , " + ViewByMap.get(String.valueOf(getRowViewbyCols().get(i))) + " ";
                                } else {
                                    viewByCol += " ,'KPI' " + " AS VIEWBY" + (i + 1) + " ";
                                    viewByColNew += " ,'KPI' " + " as  A_" + String.valueOf(getRowViewbyCols().get(i)) + " ";
                                    orderByCol += " ,'KPI' " + " AS ORDER" + (i + 1) + " ";
                                    orderByColNew += " ,'KPI' " + " as O_" + String.valueOf(getRowViewbyCols().get(i)) + " ";
//                                    addorderByCol += ",ORDER" + (i + 1) + " ";   mayank
                                    addorderByCol += " ,'KPI' ";
                                    groupByCol += " ,'KPI' ";

                                    pviewByCol += " ,'KPI' " + " AS VIEWBY" + (i + 1) + " ";
                                    porderByCol += " ,'KPI' " + " AS ORDER" + (i + 1) + " ";
                                    pgroupByCol += " ,'KPI'";
                                }


                                if (innerViewBy != null && (!innerViewBy.contains(getRowViewbyCols().get(i).toString()))) {

                                    if (!isSummBucket.equalsIgnoreCase("Y")) {
                                        OViewByCol += " , VIEWBY" + (i + 1) + "";
                                        OViewByColNew += " , A_" + String.valueOf(getRowViewbyCols().get(i)) + "";
                                        OViewByColNewGrp += " , A_" + String.valueOf(getRowViewbyCols().get(i)) + "";
                                        BucketouterCoNames += " , VIEWBY" + (i + 1) + "";
                                        BucketouterCoNamesNew += " , A_" + String.valueOf(getRowViewbyCols().get(i)) + "";
                                        BucketouterorderCoNames += " , ORDER" + (i + 1) + "";
                                        BucketouterorderCoNamesNew += " , O_" + String.valueOf(getRowViewbyCols().get(i)) + "";
                                        OorderByCol += " , ORDER" + (i + 1) + "";
                                        OorderByColNew += " , O_" + String.valueOf(getRowViewbyCols().get(i)) + "";

                                        OmViewByCol += " , VIEWBY" + (i + 1) + " as A_" + String.valueOf(getRowViewbyCols().get(i)) + "";
                                        OmViewByColNew += " , A_" + String.valueOf(getRowViewbyCols().get(i)) + " as A_" + String.valueOf(getRowViewbyCols().get(i)) + "";
                                        finalViewByCol += " , A_" + String.valueOf(getRowViewbyCols().get(i)) + " " + " as  \"A_" + String.valueOf(getRowViewbyCols().get(i)) + "\" ";
                                        finalViewByColNew += " , A_" + String.valueOf(getRowViewbyCols().get(i)) + " " + " as  \"A_" + String.valueOf(getRowViewbyCols().get(i)) + "\" ";
                                        if (i < (getRowViewbyCols().size() - getColViewbyCols().size())) {
                                            OmorderByCol += " , ORDER" + (i + 1) + "";
                                            OmorderByColNew += " , O_" + String.valueOf(getRowViewbyCols().get(i)) + "";
                                        } else {
                                            if (ColOrderByCol == null || ColOrderByCol.trim().equals("")) {
                                                ColOrderByCol = "ORDER" + (i + 1) + "";
                                                ColOrderByColNew = "O_" + String.valueOf(getRowViewbyCols().get(i)) + "";
                                            } else {
                                                ColOrderByCol += " , ORDER" + (i + 1) + "";
                                                ColOrderByColNew += " , O_" + String.valueOf(getRowViewbyCols().get(i)) + "";
                                            }
                                        }
                                    } else {
                                        OViewByCol += " , (" + SummBucketFormula + ") AS VIEWBY" + (i + 1) + "";
                                        OViewByColNew += " , (" + SummBucketFormula + ") AS A_" + String.valueOf(getRowViewbyCols().get(i)) + "";
                                        OViewByColNewGrp += " , (" + SummBucketFormula + ")";
                                        BucketouterCoNames += " , VIEWBY" + (i + 1) + "";
                                        BucketouterCoNamesNew += " , A_" + String.valueOf(getRowViewbyCols().get(i)) + "";
                                        BucketouterorderCoNames += " , ORDER" + (i + 1) + "";
                                        BucketouterorderCoNamesNew += " , O_" + String.valueOf(getRowViewbyCols().get(i)) + "";
                                        OorderByCol += " , ORDER" + (i + 1) + "";
                                        OorderByColNew += " , O_" + String.valueOf(getRowViewbyCols().get(i)) + "";

                                        OmViewByCol += " , VIEWBY" + (i + 1) + " as A_" + String.valueOf(getRowViewbyCols().get(i)) + "";
                                        OmViewByColNew += " , A_" + String.valueOf(getRowViewbyCols().get(i)) + " as A_" + String.valueOf(getRowViewbyCols().get(i)) + "";
                                        finalViewByCol += " , A_" + String.valueOf(getRowViewbyCols().get(i)) + " " + " as  \"A_" + String.valueOf(getRowViewbyCols().get(i)) + "\" ";
                                        finalViewByColNew += " , A_" + String.valueOf(getRowViewbyCols().get(i)) + " " + " as  \"A_" + String.valueOf(getRowViewbyCols().get(i)) + "\" ";
                                        if (i < (getRowViewbyCols().size() - getColViewbyCols().size())) {
                                            OmorderByCol += " , ORDER" + (i + 1) + "";
                                            OmorderByColNew += " , O_" + String.valueOf(getRowViewbyCols().get(i)) + "";
                                        } else {
                                            if (ColOrderByCol == null || ColOrderByCol.trim().equals("")) {
                                                ColOrderByCol = "ORDER" + (i + 1) + "";
                                                ColOrderByColNew = "O_" + String.valueOf(getRowViewbyCols().get(i)) + "";
                                            } else {
                                                ColOrderByCol += " , ORDER" + (i + 1) + "";
                                                ColOrderByColNew += " , O_" + String.valueOf(getRowViewbyCols().get(i)) + "";
                                            }
                                        }
                                    }


                                }
                                viewByColTarget += " , TARGET_MEMBERS as  VIEWBY" + (i + 1) + " ";
                                viewByColTargetNew += " , TARGET_MEMBERS as  A_" + String.valueOf(getRowViewbyCols().get(i)) + " ";
                                orderByColTarget += " , TARGET_MEMBERS as ORDER" + (i + 1) + " ";
                                orderByColTargetNew += " , TARGET_MEMBERS as O_" + String.valueOf(getRowViewbyCols().get(i)) + " ";
                                groupByColTarget += " , TARGET_MEMBERS " + ViewByMapCol.get(String.valueOf(getRowViewbyCols().get(i))) + " ";
                                if (isBucketNewViewBy) {
                                    if (i == (getRowViewbyCols().size() - 1)) {

                                        if (bucketQueryeleList != null && bucketQueryeleList.size() > 0) {
                                            for (int i1 = 0, j = i + 2; i1 < bucketQueryeleList.size(); i1++, j++) {

                                                if (!String.valueOf(ViewByMap.get(String.valueOf(bucketQueryeleList.get(i1)))).equalsIgnoreCase("") && (ViewByMap.get(String.valueOf(bucketQueryeleList.get(i1))) != null)) {

                                                    BucketviewByCol = ViewByMap.get(String.valueOf(bucketQueryeleList.get(i1))) + " as  VIEWBY" + j + " ";
                                                    BucketviewByColNew = ViewByMap.get(String.valueOf(bucketQueryeleList.get(i1))) + " as  A_" + String.valueOf(bucketQueryeleList.get(i1)) + " ";
                                                    BucketorderByCol = ViewByMap.get(String.valueOf(bucketQueryeleList.get(i1))) + " as ORDER" + j + " ";
                                                    BucketorderByColNew = ViewByMap.get(String.valueOf(bucketQueryeleList.get(i1))) + " as O_" + String.valueOf(bucketQueryeleList.get(i1)) + " ";
                                                    BucketgroupByCol = ViewByMap.get(String.valueOf(bucketQueryeleList.get(i1))) + " ";

                                                    BucketOViewByCol = "VIEWBY" + j + "";
                                                    BucketOViewByCol = "A_" + String.valueOf(bucketQueryeleList.get(i1)) + "";
                                                    BucketOorderByCol = "ORDER" + j + "";
                                                    BucketOorderByCol = "O_" + String.valueOf(bucketQueryeleList.get(i1)) + "";

                                                    BucketOmViewByCol = "VIEWBY" + j + " as A_" + String.valueOf(bucketQueryeleList.get(i1)) + " ";
                                                    BucketOmViewByColNew = "A_" + String.valueOf(bucketQueryeleList.get(i1)) + " as A_" + String.valueOf(bucketQueryeleList.get(i1)) + " ";
                                                    BucketfinalViewByCol = "   A_" + String.valueOf(bucketQueryeleList.get(i1)) + " ";
                                                    BucketfinalViewByColNew = "   A_" + String.valueOf(bucketQueryeleList.get(i1)) + " ";
                                                    if (i < (getRowViewbyCols().size() - getColViewbyCols().size())) {
                                                        BucketOmorderByCol = "ORDER" + j + "";
                                                        BucketOmorderByColNew = "O_" + String.valueOf(bucketQueryeleList.get(i1)) + "";
                                                    }

                                                    BucketviewByColTarget = ViewByMapCol.get(String.valueOf(bucketQueryeleList.get(i1))) + " as  VIEWBY" + j + " ";
                                                    BucketorderByColTarget = ViewByMapCol.get(String.valueOf(bucketQueryeleList.get(i1))) + " as ORDER" + j + " ";
                                                    BucketviewByColTargetNew = ViewByMapCol.get(String.valueOf(bucketQueryeleList.get(i1))) + " as  A_" + String.valueOf(bucketQueryeleList.get(i1)) + " ";
                                                    BucketorderByColTargetNew = ViewByMapCol.get(String.valueOf(bucketQueryeleList.get(i1))) + " as O_" + String.valueOf(bucketQueryeleList.get(i1)) + " ";
                                                    BucketgroupByColTarget = ViewByMapCol.get(String.valueOf(bucketQueryeleList.get(i1))) + " ";
                                                }
                                                if (!BucketviewByCol.equalsIgnoreCase("")) {


                                                    viewByCol += " , " + BucketviewByCol;
                                                    viewByColNew += " , " + BucketviewByColNew;
                                                    orderByCol += " , " + BucketorderByCol;
                                                    orderByColNew += " , " + BucketorderByColNew;
                                                    addorderByCol += " , " + BucketorderByCol;
                                                    addorderByColNew += " , " + BucketorderByColNew;
                                                    groupByCol += " , " + BucketgroupByCol;

                                                    if (innerViewBy != null && (!innerViewBy.contains(getRowViewbyCols().get(i).toString()))) {
                                                        OViewByCol += " ," + BucketOViewByCol;
                                                        OViewByColNew += " ," + BucketOViewByColNew;
                                                        OViewByColNewGrp += " ," + BucketOViewByColNew;
                                                        BucketouterCoNames += " ," + BucketOViewByCol;
                                                        BucketouterCoNamesNew += " ," + BucketOViewByColNew;
                                                        BucketouterorderCoNames += " , " + BucketOorderByCol;
                                                        BucketouterorderCoNamesNew += " , " + BucketOorderByColNew;
                                                        OorderByCol += " , " + BucketOorderByCol;
                                                        OorderByColNew += " , " + BucketOorderByColNew;

                                                        OmViewByCol += " ," + BucketOmViewByCol;
                                                        OmViewByColNew += " ," + BucketOmViewByColNew;
                                                        finalViewByCol += ", " + BucketfinalViewByCol;
                                                        finalViewByColNew += ", " + BucketfinalViewByColNew;
                                                        if (i < (getRowViewbyCols().size() - getColViewbyCols().size())) {
                                                            OmorderByCol += " , " + BucketOmorderByCol;
                                                            OmorderByColNew += " , " + BucketOmorderByColNew;
                                                        } else {
                                                            if (ColOrderByCol == null || ColOrderByCol.trim().equals("")) {
                                                                ColOrderByCol = "  " + BucketOmorderByCol;
                                                                ColOrderByColNew = "  " + BucketOmorderByColNew;
                                                            } else {
                                                                ColOrderByCol += " , " + BucketOmorderByCol;
                                                                ColOrderByColNew += " , " + BucketOmorderByColNew;
                                                            }
                                                        }
                                                    }

                                                    //  ColOrderByCol += " , " + BucketOmorderByCol;
                                                    viewByColTarget += " , " + BucketviewByColTarget;
                                                    viewByColTargetNew += " , " + BucketviewByColTargetNew;  //Added by Amar
                                                    orderByColTarget += " , " + BucketorderByColTarget;
                                                    orderByColTargetNew += " , " + BucketorderByColTargetNew;
                                                    groupByColTarget += " , " + BucketgroupByColTarget;


                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

            }/// end of getting view by columns
            else {
                viewByCol = " '" + kpiVal + "' as KPI ";
                viewByColNew = " '" + kpiVal + "' as KPI ";
                pviewByCol = " '" + kpiVal + "' as KPI ";    //Added by Amar
                pviewByColNew = " '" + kpiVal + "' as KPI "; //Added by Amar

                groupByCol = " ";
                OViewByCol = "";
                OViewByColNew = "";
                OViewByColNewGrp = "";
                OmViewByCol = " ";
                OmViewByColNew = " ";
                finalViewByCol = "";
                finalViewByColNew = "";

                orderByCol = " '" + kpiVal + "' as KPI1 ";
                orderByColNew = " '" + kpiVal + "' as KPI1 ";

                porderByCol = " '" + kpiVal + "' as KPI1 ";   //Added by Amar
                porderByColNew = " '" + kpiVal + "' as KPI1 "; //Added by AMar
                OorderByCol = "";
                OorderByColNew = "";
                OmorderByCol = "";
                OmorderByColNew = "";
                ////////////// for target fact start
                viewByColTarget = " '" + kpiVal + "' as KPI";
                viewByColTargetNew = " '" + kpiVal + "' as KPI";
                groupByColTarget = " ";
                orderByColTarget = " '" + kpiVal + "' as KPI1  ";
                orderByColTargetNew = " '" + kpiVal + "' as KPI1  ";

            }

            if (isKpi && isInnerViewBy) {
                //viewByCol = " '" + kpiVal + "' as KPI ";
                //groupByCol = " ";
                OViewByCol = "";
                OViewByColNew = "";
                OViewByColNewGrp = "";
                OmViewByCol = " ";
                OmViewByColNew = " ";
                finalViewByCol = "";
                finalViewByColNew = "";

                orderByCol = " '" + kpiVal + "' as KPI1 ";
                orderByColNew = " '" + kpiVal + "' as KPI1 ";
                porderByCol = " '" + kpiVal + "' as KPI1 ";
                porderByColNew = " '" + kpiVal + "' as KPI1 ";
                OorderByCol = "";
                OorderByColNew = "";
                OmorderByCol = "";
                OmorderByColNew = "";
                ////////////// for target fact start
                // viewByColTarget = " '" + kpiVal + "' as KPI";
                groupByColTarget = " ";
                orderByColTarget = " '" + kpiVal + "' as KPI1  ";
                orderByColTargetNew = " '" + kpiVal + "' as KPI1  ";

            }


// mayank to add additional view by
            if (isQueryForGO || isQueryForAO || isAOEnable || isScheduleAO) {
                for (int k = 0; k < additionalGOViewBy.length; k++) {
                    if (additionalViewBy.equalsIgnoreCase("")) {
                        additionalViewBy += "GO_TIME." + additionalGOViewBy[k] + " as " + additionalGOViewBy[k];
                        additionalOrderBy += "GO_TIME." + additionalGOViewBy[k] + " as " + "O_" + additionalGOViewBy[k];
                        additionalgroupByCol += "GO_TIME." + additionalGOViewBy[k];
                    } else {
                        additionalViewBy += "," + "GO_TIME." + additionalGOViewBy[k] + " as " + additionalGOViewBy[k];
                        additionalOrderBy += "," + "GO_TIME." + additionalGOViewBy[k] + " as " + "O_" + additionalGOViewBy[k];
                        additionalgroupByCol += " , " + "GO_TIME." + additionalGOViewBy[k];
                    }
                }
            }


        }////end of cross tab if

        /*
         * Place where time code can be moved
         */



        //Get data for non view by Columns{


        Vector parameterTable = new Vector();
        String ParameterId = null;
        String paramWhereClause = "";
        String onlyParamWhereClause = "";
        HashMap onlyParamWhereMap = new HashMap();
        String selfParamWhereClause = "";

        /////////////////// for target fact start
        String targetParameterId = null;
        String targetParamWhereClause = "";
        String selftargetParamWhereClause = "";
        ArrayList targetColumns = new ArrayList();
        //////////////////// for target fact
        String target[] = (String[]) tabTypes.keySet().toArray(new String[0]);

        for (int j = 0; j < target.length; j++) {
            String tabT = target[j];
            String tableType = (String) tabTypes.get(tabT);
            String targetTabName = (String) targetTableNames.get(tabT);
            if (!tableType.equalsIgnoreCase("Target Table")) {//where Clause Columns
                String[] a1 = (String[]) (getParamValue().keySet()).toArray(new String[0]);
                //
                for (int i = 0; i < a1.length; i++) {
                    if (ValidateParamValues(getParamValue().get(a1[i]))) {
                        if (ParameterId == null) {
                            ParameterId = (a1[i]).toString();
                        } else {
                            ParameterId += "," + (a1[i]).toString();
                        }
                    }
                }
                if (excludedParameters != null && excludedParameters.size() > 0) {
                    String[] a2 = (String[]) (excludedParameters.keySet()).toArray(new String[0]);
                    for (int i = 0; i < a2.length; i++) {
                        if (ValidateParamValues(excludedParameters.get(a2[i]))) {
                            if (ParameterId == null) {
                                ParameterId = (a2[i]).toString();
                            } else {
                                ParameterId += "," + (a2[i]).toString();
                            }
                        }
                    }

                }
                ///////////////////////////////Adding data from New Maps
                if (inMap != null && inMap.size() > 0) {
                    String[] a2 = (String[]) (inMap.keySet()).toArray(new String[0]);
                    for (int i = 0; i < a2.length; i++) {
                        if (ValidateParamValues(inMap.get(a2[i]))) {
                            if (ParameterId == null) {
                                ParameterId = (a2[i]).toString();
                            } else {
                                ParameterId += "," + (a2[i]).toString();
                            }
                        }
                    }

                }

                if (notInMap != null && notInMap.size() > 0) {
                    String[] a2 = (String[]) (notInMap.keySet()).toArray(new String[0]);
                    for (int i = 0; i < a2.length; i++) {
                        if (ValidateParamValues(notInMap.get(a2[i]))) {
                            if (ParameterId == null) {
                                ParameterId = (a2[i]).toString();
                            } else {
                                ParameterId += "," + (a2[i]).toString();
                            }
                        }
                    }

                }

                if (likeMap != null && likeMap.size() > 0) {
                    String[] a2 = (String[]) (likeMap.keySet()).toArray(new String[0]);
                    for (int i = 0; i < a2.length; i++) {
                        if (ValidateParamValues(likeMap.get(a2[i]))) {
                            if (ParameterId == null) {
                                ParameterId = (a2[i]).toString();
                            } else {
                                ParameterId += "," + (a2[i]).toString();
                            }
                        }
                    }

                }
                if (notLikeMap != null && notLikeMap.size() > 0) {
                    String[] a2 = (String[]) (notLikeMap.keySet()).toArray(new String[0]);
                    for (int i = 0; i < a2.length; i++) {
                        if (ValidateParamValues(notLikeMap.get(a2[i]))) {
                            if (ParameterId == null) {
                                ParameterId = (a2[i]).toString();
                            } else {
                                ParameterId += "," + (a2[i]).toString();
                            }
                        }
                    }

                }

                if (parameterQuery != null && parameterQuery.size() > 0) {
                    String[] a2 = (String[]) (parameterQuery.keySet()).toArray(new String[0]);
                    for (int i = 0; i < a2.length; i++) {
                        {
                            if (ParameterId == null) {
                                ParameterId = (a2[i]).toString();
                            } else {
                                ParameterId += "," + (a2[i]).toString();
                            }
                        }
                    }

                }

                if (ParameterId != null) {

                    sqlstr = resBundle.getString("generateViewByQry1");
                    //
                    Obj = new Object[1];
                    Obj[0] = ParameterId;
                    finalQuery = pbDb.buildQuery(sqlstr, Obj);
                    //
                    retObj = pbDb.execSelectSQL(finalQuery);

                    colNames = retObj.getColumnNames();

                    psize = retObj.getRowCount();
                    if (psize > 0) {
                        //Looping twice
                        //Loop 1 find the fact and current and prior cols
                        //loop 2 build query
                        paramWhereClause += " and  ( 1 = 1 ";
                        selfParamWhereClause += " and  ( 1 = 1 ";
                        targetParamWhereClause += " and  ( 1 = 1 ";
                        boolean isFirstOr = false;
                        for (int i = 0; i < psize; i++) {
                            eleId = retObj.getFieldValueString(i, colNames[5]);
                            userColType = retObj.getFieldValueString(i, colNames[10]);
                            if (userColType != null && !userColType.equalsIgnoreCase("") && !userColType.equalsIgnoreCase("null") && userColType.equalsIgnoreCase("CALCULATED")) {
                                PbReportViewerDAO bd = new PbReportViewerDAO();
                                Segmentformula = bd.readFormulaFromFile(eleId, this.filePath);
                            }
                            String paramCond = "in";// code written by swathi
                            if (excludedParameters != null && excludedParameters.size() > 0 && excludedParameters.get(retObj.getFieldValueString(i, colNames[5])) != null) {
                                if (resetParamHashmap != null && !resetParamHashmap.isEmpty() && resetParamHashmap.get(retObj.getFieldValueString(i, colNames[5])) != null) {
                                    if (resetParamHashmap.get(retObj.getFieldValueString(i, colNames[5])).equals("LIKE")) {
                                        paramCond = "like";
                                    } else if (resetParamHashmap.get(retObj.getFieldValueString(i, colNames[5])).equals("NOT LIKE")) {
                                        paramCond = "not like";
                                    } else {
                                        paramCond = "not in";
                                    }
                                } else {
                                    paramCond = "not in";
                                }
                            } else {
                                if (resetParamHashmap != null && !resetParamHashmap.isEmpty() && resetParamHashmap.get(retObj.getFieldValueString(i, colNames[5])) != null) {
                                    if (resetParamHashmap.get(retObj.getFieldValueString(i, colNames[5])).equals("LIKE")) {
                                        paramCond = "like";
                                    } else if (resetParamHashmap.get(retObj.getFieldValueString(i, colNames[5])).equals("NOT LIKE")) {
                                        paramCond = "not like";
                                    } else {
                                        paramCond = "in";
                                    }
                                } else {
                                    paramCond = "in";
                                }
                            }

                            onlyParamWhereClause += " ";
                            String parameterAndOr = " and ";
                            if (retObj.getFieldValueString(i, colNames[5]) != null) {
                                if (parameterType != null) {
                                    if (parameterType.get(retObj.getFieldValueString(i, colNames[5])) != null
                                            && !parameterType.get(retObj.getFieldValueString(i, colNames[5])).toString().trim().equals("")) {
                                        parameterAndOr = " " + parameterType.get(retObj.getFieldValueString(i, colNames[5])) + " ";
                                    }
                                }
                            }
                            if (!retObj.getFieldValueString(i, colNames[5]).equalsIgnoreCase("33517")) { // change code --Amit
                                onlyParamWhereClause = "";
                                if (parameterQuery != null && parameterQuery.size() > 0 && parameterQuery.get(retObj.getFieldValueString(i, colNames[5])) != null) {
                                    String paramCond1 = " in ";
                                    String qryForInCLause = " Select A_" + retObj.getFieldValueString(i, colNames[5]) + " from ( "
                                            + parameterQuery.get(retObj.getFieldValueString(i, colNames[5])) + " ) forSql1 ";
                                    if (userColType != null && !userColType.equalsIgnoreCase("") && !userColType.equalsIgnoreCase("null") && userColType.equalsIgnoreCase("CALCULATED")) {
                                        if (Segmentformula != null && !Segmentformula.equalsIgnoreCase("") && !Segmentformula.equalsIgnoreCase("null")) {
                                            paramWhereClause += parameterAndOr + Segmentformula;
                                            paramWhereClause += "  " + paramCond1 + " ( " + qryForInCLause + ") ";
                                            onlyParamWhereClause = parameterAndOr + Segmentformula;
                                            onlyParamWhereClause += "  " + paramCond1 + " (" + qryForInCLause + ") ";

                                            targetParamWhereClause += parameterAndOr + "  TARGET_MEMBERS ";
                                            targetParamWhereClause += "  " + paramCond1 + " (" + qryForInCLause + ") ";
                                        } else {
                                            paramWhereClause += parameterAndOr + retObj.getFieldValueString(i, colNames[1]) + "." + retObj.getFieldValueString(i, colNames[2]);
                                            paramWhereClause += "  " + paramCond1 + " ( " + qryForInCLause + ") ";
                                            onlyParamWhereClause = parameterAndOr + retObj.getFieldValueString(i, colNames[1]) + "." + retObj.getFieldValueString(i, colNames[2]);
                                            onlyParamWhereClause += "  " + paramCond1 + " (" + qryForInCLause + ") ";

                                            targetParamWhereClause += parameterAndOr + "  TARGET_MEMBERS ";
                                            targetParamWhereClause += "  " + paramCond1 + " (" + qryForInCLause + ") ";
                                        }
                                    } else {
                                        paramWhereClause += parameterAndOr + retObj.getFieldValueString(i, colNames[1]) + "." + retObj.getFieldValueString(i, colNames[2]);
                                        paramWhereClause += "  " + paramCond1 + " ( " + qryForInCLause + ") ";
                                        onlyParamWhereClause = parameterAndOr + retObj.getFieldValueString(i, colNames[1]) + "." + retObj.getFieldValueString(i, colNames[2]);
                                        onlyParamWhereClause += "  " + paramCond1 + " (" + qryForInCLause + ") ";

                                        targetParamWhereClause += parameterAndOr + "  TARGET_MEMBERS ";
                                        targetParamWhereClause += "  " + paramCond1 + " (" + qryForInCLause + ") ";
                                    }
                                } else if (excludedParameters != null && excludedParameters.size() > 0 && excludedParameters.get(retObj.getFieldValueString(i, colNames[5])) != null
                                        && inMap == null) {

                                    if (userColType != null && !userColType.equalsIgnoreCase("") && !userColType.equalsIgnoreCase("null") && userColType.equalsIgnoreCase("CALCULATED")) {
                                        if (Segmentformula != null && !Segmentformula.equalsIgnoreCase("") && !Segmentformula.equalsIgnoreCase("null")) {
                                            paramWhereClause += parameterAndOr + Segmentformula;
                                            paramWhereClause += " " + paramCond + " (" + getOracleInClause(excludedParameters.get(retObj.getFieldValueString(i, colNames[5]))) + ") ";
                                            onlyParamWhereClause = parameterAndOr + Segmentformula;
                                            onlyParamWhereClause += " " + paramCond + " (" + getOracleInClause(excludedParameters.get(retObj.getFieldValueString(i, colNames[5]))) + ") ";

                                            targetParamWhereClause += parameterAndOr + "  TARGET_MEMBERS ";
                                            targetParamWhereClause += " " + paramCond + " (" + getOracleInClause(excludedParameters.get(retObj.getFieldValueString(i, colNames[5]))) + ") ";
                                        } else {
                                            paramWhereClause += parameterAndOr + retObj.getFieldValueString(i, colNames[1]) + "." + retObj.getFieldValueString(i, colNames[2]);
                                            paramWhereClause += " " + paramCond + " (" + getOracleInClause(excludedParameters.get(retObj.getFieldValueString(i, colNames[5]))) + ") ";
                                            onlyParamWhereClause = parameterAndOr + retObj.getFieldValueString(i, colNames[1]) + "." + retObj.getFieldValueString(i, colNames[2]);
                                            onlyParamWhereClause += " " + paramCond + " (" + getOracleInClause(excludedParameters.get(retObj.getFieldValueString(i, colNames[5]))) + ") ";

                                            targetParamWhereClause += parameterAndOr + "  TARGET_MEMBERS ";
                                            targetParamWhereClause += " " + paramCond + " (" + getOracleInClause(excludedParameters.get(retObj.getFieldValueString(i, colNames[5]))) + ") ";
                                        }
                                    } else {
                                        paramWhereClause += parameterAndOr + retObj.getFieldValueString(i, colNames[1]) + "." + retObj.getFieldValueString(i, colNames[2]);
                                        paramWhereClause += " " + paramCond + " (" + getOracleInClause(excludedParameters.get(retObj.getFieldValueString(i, colNames[5]))) + ") ";
                                        onlyParamWhereClause = parameterAndOr + retObj.getFieldValueString(i, colNames[1]) + "." + retObj.getFieldValueString(i, colNames[2]);
                                        onlyParamWhereClause += " " + paramCond + " (" + getOracleInClause(excludedParameters.get(retObj.getFieldValueString(i, colNames[5]))) + ") ";

                                        targetParamWhereClause += parameterAndOr + "  TARGET_MEMBERS ";
                                        targetParamWhereClause += " " + paramCond + " (" + getOracleInClause(excludedParameters.get(retObj.getFieldValueString(i, colNames[5]))) + ") ";
                                    }

                                } else {
                                    if (getParamValue() != null && getParamValue().size() > 0 && getParamValue().get(retObj.getFieldValueString(i, colNames[5])) != null
                                            && inMap == null) {
                                        {
                                            if (userColType != null && !userColType.equalsIgnoreCase("") && !userColType.equalsIgnoreCase("null") && userColType.equalsIgnoreCase("CALCULATED")) {
                                                if (Segmentformula != null && !Segmentformula.equalsIgnoreCase("") && !Segmentformula.equalsIgnoreCase("null")) {
                                                    paramWhereClause += parameterAndOr + Segmentformula;
                                                    paramWhereClause += " " + paramCond + " (" + getOracleInClause(getParamValue().get(retObj.getFieldValueString(i, colNames[5]))) + ") ";
                                                    onlyParamWhereClause += parameterAndOr + Segmentformula;
                                                    onlyParamWhereClause += " " + paramCond + " (" + getOracleInClause(getParamValue().get(retObj.getFieldValueString(i, colNames[5]))) + ") ";
                                                    targetParamWhereClause += parameterAndOr + "  TARGET_MEMBERS ";
                                                    targetParamWhereClause += " " + paramCond + " (" + getOracleInClause(getParamValue().get(retObj.getFieldValueString(i, colNames[5]))) + ") ";
                                                } else {
                                                    paramWhereClause += parameterAndOr + retObj.getFieldValueString(i, colNames[1]) + "." + retObj.getFieldValueString(i, colNames[2]);
                                                    paramWhereClause += " " + paramCond + " (" + getOracleInClause(getParamValue().get(retObj.getFieldValueString(i, colNames[5]))) + ") ";
                                                    onlyParamWhereClause += parameterAndOr + retObj.getFieldValueString(i, colNames[1]) + "." + retObj.getFieldValueString(i, colNames[2]);
                                                    onlyParamWhereClause += " " + paramCond + " (" + getOracleInClause(getParamValue().get(retObj.getFieldValueString(i, colNames[5]))) + ") ";
                                                    targetParamWhereClause += parameterAndOr + "  TARGET_MEMBERS ";
                                                    targetParamWhereClause += " " + paramCond + " (" + getOracleInClause(getParamValue().get(retObj.getFieldValueString(i, colNames[5]))) + ") ";
                                                }
                                            } else {
                                                paramWhereClause += parameterAndOr + retObj.getFieldValueString(i, colNames[1]) + "." + retObj.getFieldValueString(i, colNames[2]);
                                                paramWhereClause += " " + paramCond + " (" + getOracleInClause(getParamValue().get(retObj.getFieldValueString(i, colNames[5]))) + ") ";
                                                onlyParamWhereClause += parameterAndOr + retObj.getFieldValueString(i, colNames[1]) + "." + retObj.getFieldValueString(i, colNames[2]);
                                                onlyParamWhereClause += " " + paramCond + " (" + getOracleInClause(getParamValue().get(retObj.getFieldValueString(i, colNames[5]))) + ") ";
                                                targetParamWhereClause += parameterAndOr + "  TARGET_MEMBERS ";
                                                targetParamWhereClause += " " + paramCond + " (" + getOracleInClause(getParamValue().get(retObj.getFieldValueString(i, colNames[5]))) + ") ";
                                            }
                                        }

                                    }


                                }


                                if (inMap != null && inMap.size() > 0 && inMap.get(retObj.getFieldValueString(i, colNames[5])) != null
                                        && !((List) inMap.get(retObj.getFieldValueString(i, colNames[5]))).isEmpty()
                                        && !getOracleInClause(inMap.get(retObj.getFieldValueString(i, colNames[5]))).equalsIgnoreCase("'All'")) {
                                    {
                                        paramCond = " in ";
                                        if (userColType != null && !userColType.equalsIgnoreCase("") && !userColType.equalsIgnoreCase("null") && userColType.equalsIgnoreCase("CALCULATED")) {
                                            if (Segmentformula != null && !Segmentformula.equalsIgnoreCase("") && !Segmentformula.equalsIgnoreCase("null")) {
                                                paramWhereClause += parameterAndOr + Segmentformula;
                                                paramWhereClause += " " + paramCond + " (" + getOracleInClause(inMap.get(retObj.getFieldValueString(i, colNames[5]))) + ") ";
                                                onlyParamWhereClause += parameterAndOr + Segmentformula;
                                                onlyParamWhereClause += " " + paramCond + " (" + getOracleInClause(inMap.get(retObj.getFieldValueString(i, colNames[5]))) + ") ";
                                                targetParamWhereClause += parameterAndOr + "  TARGET_MEMBERS ";
                                                targetParamWhereClause += " " + paramCond + " (" + getOracleInClause(inMap.get(retObj.getFieldValueString(i, colNames[5]))) + ") ";
                                            } else {
                                                paramWhereClause += parameterAndOr + retObj.getFieldValueString(i, colNames[1]) + "." + retObj.getFieldValueString(i, colNames[2]);
                                                paramWhereClause += " " + paramCond + " (" + getOracleInClause(inMap.get(retObj.getFieldValueString(i, colNames[5]))) + ") ";
                                                onlyParamWhereClause += parameterAndOr + retObj.getFieldValueString(i, colNames[1]) + "." + retObj.getFieldValueString(i, colNames[2]);
                                                onlyParamWhereClause += " " + paramCond + " (" + getOracleInClause(inMap.get(retObj.getFieldValueString(i, colNames[5]))) + ") ";
                                                targetParamWhereClause += parameterAndOr + "  TARGET_MEMBERS ";
                                                targetParamWhereClause += " " + paramCond + " (" + getOracleInClause(inMap.get(retObj.getFieldValueString(i, colNames[5]))) + ") ";
                                            }
                                        } else {
                                            paramWhereClause += parameterAndOr + retObj.getFieldValueString(i, colNames[1]) + "." + retObj.getFieldValueString(i, colNames[2]);
                                            paramWhereClause += " " + paramCond + " (" + getOracleInClause(inMap.get(retObj.getFieldValueString(i, colNames[5]))) + ") ";
                                            onlyParamWhereClause += parameterAndOr + retObj.getFieldValueString(i, colNames[1]) + "." + retObj.getFieldValueString(i, colNames[2]);
                                            onlyParamWhereClause += " " + paramCond + " (" + getOracleInClause(inMap.get(retObj.getFieldValueString(i, colNames[5]))) + ") ";
                                            targetParamWhereClause += parameterAndOr + "  TARGET_MEMBERS ";
                                            targetParamWhereClause += " " + paramCond + " (" + getOracleInClause(inMap.get(retObj.getFieldValueString(i, colNames[5]))) + ") ";
                                        }
                                    }

                                }
                                if (notInMap != null && notInMap.size() > 0 && notInMap.get(retObj.getFieldValueString(i, colNames[5])) != null
                                        && !((List) notInMap.get(retObj.getFieldValueString(i, colNames[5]))).isEmpty()
                                        && !getOracleInClause(notInMap.get(retObj.getFieldValueString(i, colNames[5]))).equalsIgnoreCase("'All'")) {
                                    {
                                        paramCond = " not In ";
                                        if (userColType != null && !userColType.equalsIgnoreCase("") && !userColType.equalsIgnoreCase("null") && userColType.equalsIgnoreCase("CALCULATED")) {
                                            if (Segmentformula != null && !Segmentformula.equalsIgnoreCase("") && !Segmentformula.equalsIgnoreCase("null")) {
                                                paramWhereClause += parameterAndOr + Segmentformula;
                                                paramWhereClause += " " + paramCond + " (" + getOracleInClause(notInMap.get(retObj.getFieldValueString(i, colNames[5]))) + ") ";
                                                onlyParamWhereClause += parameterAndOr + Segmentformula;
                                                onlyParamWhereClause += " " + paramCond + " (" + getOracleInClause(notInMap.get(retObj.getFieldValueString(i, colNames[5]))) + ") ";
                                                targetParamWhereClause += parameterAndOr + "  TARGET_MEMBERS ";
                                                targetParamWhereClause += " " + paramCond + " (" + getOracleInClause(notInMap.get(retObj.getFieldValueString(i, colNames[5]))) + ") ";
                                            } else {
                                                paramWhereClause += parameterAndOr + retObj.getFieldValueString(i, colNames[1]) + "." + retObj.getFieldValueString(i, colNames[2]);
                                                paramWhereClause += " " + paramCond + " (" + getOracleInClause(notInMap.get(retObj.getFieldValueString(i, colNames[5]))) + ") ";
                                                onlyParamWhereClause += parameterAndOr + retObj.getFieldValueString(i, colNames[1]) + "." + retObj.getFieldValueString(i, colNames[2]);
                                                onlyParamWhereClause += " " + paramCond + " (" + getOracleInClause(notInMap.get(retObj.getFieldValueString(i, colNames[5]))) + ") ";
                                                targetParamWhereClause += parameterAndOr + "  TARGET_MEMBERS ";
                                                targetParamWhereClause += " " + paramCond + " (" + getOracleInClause(notInMap.get(retObj.getFieldValueString(i, colNames[5]))) + ") ";
                                            }
                                        } else {
                                            paramWhereClause += parameterAndOr + retObj.getFieldValueString(i, colNames[1]) + "." + retObj.getFieldValueString(i, colNames[2]);
                                            paramWhereClause += " " + paramCond + " (" + getOracleInClause(notInMap.get(retObj.getFieldValueString(i, colNames[5]))) + ") ";
                                            onlyParamWhereClause += parameterAndOr + retObj.getFieldValueString(i, colNames[1]) + "." + retObj.getFieldValueString(i, colNames[2]);
                                            onlyParamWhereClause += " " + paramCond + " (" + getOracleInClause(notInMap.get(retObj.getFieldValueString(i, colNames[5]))) + ") ";
                                            targetParamWhereClause += parameterAndOr + "  TARGET_MEMBERS ";
                                            targetParamWhereClause += " " + paramCond + " (" + getOracleInClause(notInMap.get(retObj.getFieldValueString(i, colNames[5]))) + ") ";
                                        }
                                    }

                                }
                                if (likeMap != null && likeMap.size() > 0 && likeMap.get(retObj.getFieldValueString(i, colNames[5])) != null
                                        && !((List) likeMap.get(retObj.getFieldValueString(i, colNames[5]))).isEmpty()) {
                                    {
                                        paramCond = " like ";
                                        if (userColType != null && !userColType.equalsIgnoreCase("") && !userColType.equalsIgnoreCase("null") && userColType.equalsIgnoreCase("CALCULATED")) {
                                            if (Segmentformula != null && !Segmentformula.equalsIgnoreCase("") && !Segmentformula.equalsIgnoreCase("null")) {
                                                paramWhereClause += parameterAndOr + " ( " + Segmentformula;
                                                paramWhereClause += " " + paramCond + " (" + getOracleLikeNotlikeClause("like", Segmentformula, likeMap.get(retObj.getFieldValueString(i, colNames[5]))) + ") ) ";
                                                onlyParamWhereClause += parameterAndOr + " ( " + Segmentformula;
                                                onlyParamWhereClause += " " + paramCond + " (" + getOracleLikeNotlikeClause("like", Segmentformula, likeMap.get(retObj.getFieldValueString(i, colNames[5]))) + ") ) ";
                                                targetParamWhereClause += parameterAndOr + " ( " + "  TARGET_MEMBERS ";
                                                targetParamWhereClause += " " + paramCond + " (" + getOracleLikeNotlikeClause("like", Segmentformula, likeMap.get(retObj.getFieldValueString(i, colNames[5]))) + ") ) ";
                                            } else {
                                                paramWhereClause += parameterAndOr + " ( " + retObj.getFieldValueString(i, colNames[1]) + "." + retObj.getFieldValueString(i, colNames[2]);
                                                paramWhereClause += " " + paramCond + " (" + getOracleLikeNotlikeClause("like", retObj.getFieldValueString(i, colNames[1]) + "." + retObj.getFieldValueString(i, colNames[2]), likeMap.get(retObj.getFieldValueString(i, colNames[5]))) + ") ) ";
                                                onlyParamWhereClause += parameterAndOr + " ( " + retObj.getFieldValueString(i, colNames[1]) + "." + retObj.getFieldValueString(i, colNames[2]);
                                                onlyParamWhereClause += " " + paramCond + " (" + getOracleLikeNotlikeClause("like", retObj.getFieldValueString(i, colNames[1]) + "." + retObj.getFieldValueString(i, colNames[2]), likeMap.get(retObj.getFieldValueString(i, colNames[5]))) + ") ) ";
                                                targetParamWhereClause += parameterAndOr + " ( " + "  TARGET_MEMBERS ";
                                                targetParamWhereClause += " " + paramCond + " (" + getOracleLikeNotlikeClause("like", retObj.getFieldValueString(i, colNames[1]) + "." + retObj.getFieldValueString(i, colNames[2]), likeMap.get(retObj.getFieldValueString(i, colNames[5]))) + ") ) ";
                                            }
                                        } else {
                                            paramWhereClause += parameterAndOr + " ( " + retObj.getFieldValueString(i, colNames[1]) + "." + retObj.getFieldValueString(i, colNames[2]);
                                            paramWhereClause += " " + paramCond + " (" + getOracleLikeNotlikeClause("like", retObj.getFieldValueString(i, colNames[1]) + "." + retObj.getFieldValueString(i, colNames[2]), likeMap.get(retObj.getFieldValueString(i, colNames[5]))) + ") ) ";
                                            onlyParamWhereClause += parameterAndOr + " ( " + retObj.getFieldValueString(i, colNames[1]) + "." + retObj.getFieldValueString(i, colNames[2]);
                                            onlyParamWhereClause += " " + paramCond + " (" + getOracleLikeNotlikeClause("like", retObj.getFieldValueString(i, colNames[1]) + "." + retObj.getFieldValueString(i, colNames[2]), likeMap.get(retObj.getFieldValueString(i, colNames[5]))) + ") ) ";
                                            targetParamWhereClause += parameterAndOr + " ( " + "  TARGET_MEMBERS ";
                                            targetParamWhereClause += " " + paramCond + " (" + getOracleLikeNotlikeClause("like", retObj.getFieldValueString(i, colNames[1]) + "." + retObj.getFieldValueString(i, colNames[2]), likeMap.get(retObj.getFieldValueString(i, colNames[5]))) + ") ) ";
                                        }
                                    }

                                }
                                if (notLikeMap != null && notLikeMap.size() > 0 && notLikeMap.get(retObj.getFieldValueString(i, colNames[5])) != null
                                        && !((List) notLikeMap.get(retObj.getFieldValueString(i, colNames[5]))).isEmpty()) {
                                    {
                                        paramCond = " not like ";
                                        if (userColType != null && !userColType.equalsIgnoreCase("") && !userColType.equalsIgnoreCase("null") && userColType.equalsIgnoreCase("CALCULATED")) {
                                            if (Segmentformula != null && !Segmentformula.equalsIgnoreCase("") && !Segmentformula.equalsIgnoreCase("null")) {
                                                paramWhereClause += parameterAndOr + " ( " + Segmentformula;
                                                paramWhereClause += " " + paramCond + " (" + getOracleLikeNotlikeClause("not like", Segmentformula, notLikeMap.get(retObj.getFieldValueString(i, colNames[5]))) + ") ) ";
                                                onlyParamWhereClause += parameterAndOr + " ( " + Segmentformula;
                                                onlyParamWhereClause += " " + paramCond + " (" + getOracleLikeNotlikeClause("not like", Segmentformula, notLikeMap.get(retObj.getFieldValueString(i, colNames[5]))) + ") ) ";
                                                targetParamWhereClause += parameterAndOr + " ( " + "  TARGET_MEMBERS ";
                                                targetParamWhereClause += " " + paramCond + " (" + getOracleLikeNotlikeClause("not like", Segmentformula, notLikeMap.get(retObj.getFieldValueString(i, colNames[5]))) + ") ) ";
                                            } else {
                                                paramWhereClause += parameterAndOr + " ( " + retObj.getFieldValueString(i, colNames[1]) + "." + retObj.getFieldValueString(i, colNames[2]);
                                                paramWhereClause += " " + paramCond + " (" + getOracleLikeNotlikeClause("not like", retObj.getFieldValueString(i, colNames[1]) + "." + retObj.getFieldValueString(i, colNames[2]), notLikeMap.get(retObj.getFieldValueString(i, colNames[5]))) + ") ) ";
                                                onlyParamWhereClause += parameterAndOr + " ( " + retObj.getFieldValueString(i, colNames[1]) + "." + retObj.getFieldValueString(i, colNames[2]);
                                                onlyParamWhereClause += " " + paramCond + " (" + getOracleLikeNotlikeClause("not like", retObj.getFieldValueString(i, colNames[1]) + "." + retObj.getFieldValueString(i, colNames[2]), notLikeMap.get(retObj.getFieldValueString(i, colNames[5]))) + ") ) ";
                                                targetParamWhereClause += parameterAndOr + " ( " + "  TARGET_MEMBERS ";
                                                targetParamWhereClause += " " + paramCond + " (" + getOracleLikeNotlikeClause("not like", retObj.getFieldValueString(i, colNames[1]) + "." + retObj.getFieldValueString(i, colNames[2]), notLikeMap.get(retObj.getFieldValueString(i, colNames[5]))) + ") ) ";
                                            }
                                        } else {
                                            paramWhereClause += parameterAndOr + " ( " + retObj.getFieldValueString(i, colNames[1]) + "." + retObj.getFieldValueString(i, colNames[2]);
                                            paramWhereClause += " " + paramCond + " (" + getOracleLikeNotlikeClause("not like", retObj.getFieldValueString(i, colNames[1]) + "." + retObj.getFieldValueString(i, colNames[2]), notLikeMap.get(retObj.getFieldValueString(i, colNames[5]))) + ") ) ";
                                            onlyParamWhereClause += parameterAndOr + " ( " + retObj.getFieldValueString(i, colNames[1]) + "." + retObj.getFieldValueString(i, colNames[2]);
                                            onlyParamWhereClause += " " + paramCond + " (" + getOracleLikeNotlikeClause("not like", retObj.getFieldValueString(i, colNames[1]) + "." + retObj.getFieldValueString(i, colNames[2]), notLikeMap.get(retObj.getFieldValueString(i, colNames[5]))) + ") ) ";
                                            targetParamWhereClause += parameterAndOr + " ( " + "  TARGET_MEMBERS ";
                                            targetParamWhereClause += " " + paramCond + " (" + getOracleLikeNotlikeClause("not like", retObj.getFieldValueString(i, colNames[1]) + "." + retObj.getFieldValueString(i, colNames[2]), notLikeMap.get(retObj.getFieldValueString(i, colNames[5]))) + ") ) ";
                                        }
                                    }

                                }
                            }


                            if (!(getColViewbyCols() == null || getColViewbyCols().size() == 0)) {
                                if (retObj.getFieldValueString(i, colNames[5]).equalsIgnoreCase(getColViewbyCols().get(0).toString())) {

                                    if (excludedParameters != null && excludedParameters.size() > 0 && excludedParameters.get(retObj.getFieldValueString(i, colNames[5])) != null) {
                                        if (userColType != null && !userColType.equalsIgnoreCase("") && !userColType.equalsIgnoreCase("null") && userColType.equalsIgnoreCase("CALCULATED")) {
                                            if (Segmentformula != null && !Segmentformula.equalsIgnoreCase("") && !Segmentformula.equalsIgnoreCase("null")) {
                                                selfParamWhereClause += parameterAndOr + Segmentformula;
                                                selfParamWhereClause += " " + paramCond + " (" + getOracleInClause(excludedParameters.get(retObj.getFieldValueString(i, colNames[5]))) + ") ";
                                            } else {
                                                selfParamWhereClause += parameterAndOr + retObj.getFieldValueString(i, colNames[1]) + "." + retObj.getFieldValueString(i, colNames[2]);
                                                selfParamWhereClause += " " + paramCond + " (" + getOracleInClause(excludedParameters.get(retObj.getFieldValueString(i, colNames[5]))) + ") ";
                                            }
                                        } else {
                                            selfParamWhereClause += parameterAndOr + retObj.getFieldValueString(i, colNames[1]) + "." + retObj.getFieldValueString(i, colNames[2]);
                                            selfParamWhereClause += " " + paramCond + " (" + getOracleInClause(excludedParameters.get(retObj.getFieldValueString(i, colNames[5]))) + ") ";
                                        }
                                    } else {
                                        if (getParamValue() != null && getParamValue().size() > 0 && getParamValue().get(retObj.getFieldValueString(i, colNames[5])) != null) {
                                            if (userColType != null && !userColType.equalsIgnoreCase("") && !userColType.equalsIgnoreCase("null") && userColType.equalsIgnoreCase("CALCULATED")) {
                                                if (Segmentformula != null && !Segmentformula.equalsIgnoreCase("") && !Segmentformula.equalsIgnoreCase("null")) {
                                                    selfParamWhereClause += parameterAndOr + Segmentformula;
                                                    selfParamWhereClause += " " + paramCond + " (" + getOracleInClause(getParamValue().get(retObj.getFieldValueString(i, colNames[5]))) + ") ";
                                                } else {
                                                    selfParamWhereClause += parameterAndOr + retObj.getFieldValueString(i, colNames[1]) + "." + retObj.getFieldValueString(i, colNames[2]);
                                                    selfParamWhereClause += " " + paramCond + " (" + getOracleInClause(getParamValue().get(retObj.getFieldValueString(i, colNames[5]))) + ") ";
                                                }
                                            } else {
                                                selfParamWhereClause += parameterAndOr + retObj.getFieldValueString(i, colNames[1]) + "." + retObj.getFieldValueString(i, colNames[2]);
                                                selfParamWhereClause += " " + paramCond + " (" + getOracleInClause(getParamValue().get(retObj.getFieldValueString(i, colNames[5]))) + ") ";
                                            }
                                        }
                                    }


                                    if (inMap != null && inMap.size() > 0 && inMap.get(retObj.getFieldValueString(i, colNames[5])) != null) {
                                        {
                                            paramCond = " in ";
                                            if (userColType != null && !userColType.equalsIgnoreCase("") && !userColType.equalsIgnoreCase("null") && userColType.equalsIgnoreCase("CALCULATED")) {
                                                if (Segmentformula != null && !Segmentformula.equalsIgnoreCase("") && !Segmentformula.equalsIgnoreCase("null")) {
                                                    selfParamWhereClause += parameterAndOr + Segmentformula;
                                                    selfParamWhereClause += " " + paramCond + " (" + getOracleInClause(inMap.get(retObj.getFieldValueString(i, colNames[5]))) + ") ";
                                                } else {
                                                    selfParamWhereClause += parameterAndOr + retObj.getFieldValueString(i, colNames[1]) + "." + retObj.getFieldValueString(i, colNames[2]);
                                                    selfParamWhereClause += " " + paramCond + " (" + getOracleInClause(inMap.get(retObj.getFieldValueString(i, colNames[5]))) + ") ";
                                                }
                                            } else {
                                                selfParamWhereClause += parameterAndOr + retObj.getFieldValueString(i, colNames[1]) + "." + retObj.getFieldValueString(i, colNames[2]);
                                                selfParamWhereClause += " " + paramCond + " (" + getOracleInClause(inMap.get(retObj.getFieldValueString(i, colNames[5]))) + ") ";
                                            }
                                        }

                                    }
                                    if (notInMap != null && notInMap.size() > 0 && notInMap.get(retObj.getFieldValueString(i, colNames[5])) != null) {
                                        {
                                            paramCond = " not In ";
                                            if (userColType != null && !userColType.equalsIgnoreCase("") && !userColType.equalsIgnoreCase("null") && userColType.equalsIgnoreCase("CALCULATED")) {
                                                if (Segmentformula != null && !Segmentformula.equalsIgnoreCase("") && !Segmentformula.equalsIgnoreCase("null")) {
                                                    selfParamWhereClause += parameterAndOr + Segmentformula;
                                                    selfParamWhereClause += " " + paramCond + " (" + getOracleInClause(notInMap.get(retObj.getFieldValueString(i, colNames[5]))) + ") ";
                                                } else {
                                                    selfParamWhereClause += parameterAndOr + retObj.getFieldValueString(i, colNames[1]) + "." + retObj.getFieldValueString(i, colNames[2]);
                                                    selfParamWhereClause += " " + paramCond + " (" + getOracleInClause(notInMap.get(retObj.getFieldValueString(i, colNames[5]))) + ") ";
                                                }
                                            } else {
                                                selfParamWhereClause += parameterAndOr + retObj.getFieldValueString(i, colNames[1]) + "." + retObj.getFieldValueString(i, colNames[2]);
                                                selfParamWhereClause += " " + paramCond + " (" + getOracleInClause(notInMap.get(retObj.getFieldValueString(i, colNames[5]))) + ") ";
                                            }
                                        }

                                    }
                                    if (likeMap != null && likeMap.size() > 0 && likeMap.get(retObj.getFieldValueString(i, colNames[5])) != null) {
                                        {
                                            paramCond = " like ";
                                            if (userColType != null && !userColType.equalsIgnoreCase("") && !userColType.equalsIgnoreCase("null") && userColType.equalsIgnoreCase("CALCULATED")) {
                                                if (Segmentformula != null && !Segmentformula.equalsIgnoreCase("") && !Segmentformula.equalsIgnoreCase("null")) {
                                                    selfParamWhereClause += parameterAndOr + Segmentformula;
                                                    selfParamWhereClause += " " + paramCond + " (" + getOracleInClause(likeMap.get(retObj.getFieldValueString(i, colNames[5]))) + ") ";
                                                } else {
                                                    selfParamWhereClause += parameterAndOr + retObj.getFieldValueString(i, colNames[1]) + "." + retObj.getFieldValueString(i, colNames[2]);
                                                    selfParamWhereClause += " " + paramCond + " (" + getOracleInClause(likeMap.get(retObj.getFieldValueString(i, colNames[5]))) + ") ";
                                                }
                                            } else {
                                                selfParamWhereClause += parameterAndOr + retObj.getFieldValueString(i, colNames[1]) + "." + retObj.getFieldValueString(i, colNames[2]);
                                                selfParamWhereClause += " " + paramCond + " (" + getOracleInClause(likeMap.get(retObj.getFieldValueString(i, colNames[5]))) + ") ";
                                            }
                                        }

                                    }
                                    if (notLikeMap != null && notLikeMap.size() > 0 && notLikeMap.get(retObj.getFieldValueString(i, colNames[5])) != null) {
                                        {
                                            if (userColType != null && !userColType.equalsIgnoreCase("") && !userColType.equalsIgnoreCase("null") && userColType.equalsIgnoreCase("CALCULATED")) {
                                                if (Segmentformula != null && !Segmentformula.equalsIgnoreCase("") && !Segmentformula.equalsIgnoreCase("null")) {
                                                    selfParamWhereClause += parameterAndOr + Segmentformula;
                                                    selfParamWhereClause += " " + paramCond + " (" + getOracleInClause(notLikeMap.get(retObj.getFieldValueString(i, colNames[5]))) + ") ";
                                                } else {
                                                    selfParamWhereClause += parameterAndOr + retObj.getFieldValueString(i, colNames[1]) + "." + retObj.getFieldValueString(i, colNames[2]);
                                                    selfParamWhereClause += " " + paramCond + " (" + getOracleInClause(notLikeMap.get(retObj.getFieldValueString(i, colNames[5]))) + ") ";
                                                }
                                            } else {
                                                selfParamWhereClause += parameterAndOr + retObj.getFieldValueString(i, colNames[1]) + "." + retObj.getFieldValueString(i, colNames[2]);
                                                selfParamWhereClause += " " + paramCond + " (" + getOracleInClause(notLikeMap.get(retObj.getFieldValueString(i, colNames[5]))) + ") ";
                                            }
                                        }

                                    }


                                }
                            }

                            if (i == (psize - 1)) {
                                paramWhereClause = paramWhereClause + " ) ";

                                targetParamWhereClause = targetParamWhereClause + " ) ";
                                selfParamWhereClause = selfParamWhereClause + " ) ";
                            }

                            if (parameterTable.isEmpty()) {
                                parameterTable.add(retObj.getFieldValueString(i, colNames[0]));
                            } else if (!parameterTable.contains(retObj.getFieldValueString(i, colNames[0]))) {
                                parameterTable.add(retObj.getFieldValueString(i, colNames[0]));
                            }

                            addAllTablesforReplace(allTablesforReplace, retObj.getFieldValueString(0, colNames[1]));
                            addAllTablesforReplaceColumns(allTablesforReplaceColumns, retObj.getFieldValueString(0, colNames[1]), retObj.getFieldValueString(0, colNames[2]));

                            if (onlyParamWhereMap == null || (!onlyParamWhereMap.containsKey(retObj.getFieldValueString(i, colNames[0])))) {
                                onlyParamWhereMap.put(retObj.getFieldValueString(i, colNames[0]), onlyParamWhereClause);
                                if (parameterAndOr.toUpperCase().contains("OR ")) {
                                    isFirstOr = true;
                                }
                            } else {
                                if (!onlyParamWhereMap.get(retObj.getFieldValueString(i, colNames[0])).toString().contains(onlyParamWhereClause)) {
                                    if (isFirstOr) {
                                        onlyParamWhereMap.put(retObj.getFieldValueString(i, colNames[0]), onlyParamWhereClause + onlyParamWhereMap.get(retObj.getFieldValueString(i, colNames[0])).toString());
                                        isFirstOr = false;
                                    } else {
                                        onlyParamWhereMap.put(retObj.getFieldValueString(i, colNames[0]), onlyParamWhereMap.get(retObj.getFieldValueString(i, colNames[0])).toString() + onlyParamWhereClause);
                                    }
                                }

                            }///End
                        }///End For
                    }
                }
            } else {//This is for taget fact////////////////////////////////////



                {//where Clause Columns
                    String[] a1 = (String[]) (getParamValue().keySet()).toArray(new String[0]);
                    for (int i = 0; i < a1.length; i++) {
                        targetParameterId += "," + (a1[i]).toString();

                    }
                    if (targetParameterId != null) {
                    }
                }
            }
        }

        String crossTabCol = new String();
        groupByColNew = groupByCol;
        pgroupByColNew = pgroupByCol;
        if (orderByCol.toUpperCase().contains("PRG_DAY_DETAILS")) {
            addorderByCol = changeForTimeInfo(addorderByCol);
            addorderByColNew = changeForTimeInfo(addorderByColNew);
            orderByCol = changeForTimeInfo(orderByCol);
            orderByColNew = changeForTimeInfo(orderByColNew);
            porderByCol = changeForTimeInfo(porderByCol);
            porderByColNew = changeForTimeInfo(porderByColNew);
            porderByCol = changeForOTimeInfo(porderByCol);    //Added by Amar
            porderByColNew = changeForOTimeInfo(porderByColNew);  //Added by Amar
            pviewByCol = changeForPTimeInfo(pviewByCol);
            pviewByColNew = changeForPTimeInfo(pviewByColNew);
            groupByCol += " , " + addorderByCol;
            pgroupByCol += " , " + addorderByCol;
            groupByColNew += " , " + addorderByColNew;
            pgroupByColNew += " , " + addorderByColNew;
        }
        pgroupByCol = changeForOTimeInfo(changeForPTimeInfo(pgroupByCol));
        pgroupByColNew = changeForOTimeInfo(changeForPTimeInfo(pgroupByColNew));
        if (orderByCol.toUpperCase().contains("PRG_DAY_DETAILS") || qryType.equalsIgnoreCase("Trend")) {
            summtrend = true;
        }

        if (!CrossTab) {
            if (facts == null || facts.size() == 0) {
                facts.add("0");
                tabTypes.put("0", "Fact");
            }
            ArrayList[] allTables = new ArrayList[facts.size()];
            ArrayList[] timeClause = new ArrayList[facts.size()];
            ArrayList[] timeHaveDate = new ArrayList[facts.size()];
            ArrayList[] timetable = new ArrayList[facts.size()];

            String[] factzqueries = new String[facts.size()];
            BusinessGroupDAO bgdao = new BusinessGroupDAO();
            String otherTable = null;//It should be for each fact

            for (int i = 0; i < facts.size(); i++) {
                //////////////for target fact start
                String tabType = "";
                tabType = tabTypes.get(facts.get(i)).toString();
                //////////////for target fact

                if (allTables[i] == null) {
                    allTables[i] = new ArrayList();
                    timeClause[i] = new ArrayList();
                    timetable[i] = new ArrayList();
                    timeHaveDate[i] = new ArrayList();
                }
                allTables[i].add(facts.get(i));

                /*
                 * Changes done to implement new time dim changes
                 */
                String timeCol = getTimeColforFact(facts.get(i).toString());
                if (factandTimetable == null || !(factandTimetable.containsKey(facts.get(i).toString()))) {
                    factandTimetable.put(facts.get(i).toString(), newTimetableName);
                }

                /*
                 * Clause moved out from below
                 */


                if (isDateTrueforNonDt) {

                    //getTimeColforFact(facts.get(i).toString());//Moved out do not remove
                    timeClause[i].add(" and " + this.TimetableDateColName);
                } else {
                    timeClause[i].add(" and " + timeCol);
                    // timeClause[i].add(" and " + getTimeColforFact(facts.get(i).toString()));//Time col moved out do not remove
                }


                if (timeCol != null) {
                    timeHaveDate[i].add("Y");// If we do not have time we will not put time clause
                } else {
                    timeHaveDate[i].add("N");
                }
                timetable[i].add(TimetableId);

                //////////////for target fact start
                if (!tabType.equalsIgnoreCase("Target Table")) {
                    for (int j = 0; j < dimensions.size(); j++) {
//
                        if (otherTable == null) {
                            otherTable = dimensions.get(j).toString();
                        } else {
                            otherTable += "," + dimensions.get(j).toString();
                        }
                        //For test
                        allTables[i].add(dimensions.get(j));
                    }
                }
                if (!tabType.equalsIgnoreCase("Target Table")) {
                    for (int j = 0; j < parameterTable.size(); j++) {
                        if (otherTable == null) {
                            otherTable = parameterTable.get(j).toString();
                        } else {
                            otherTable += "," + parameterTable.get(j).toString();
                        }
                        allTables[i].add(parameterTable.get(j));
                    }
                    dependentFact = new ArrayList<String>();
                    dependentFact = (ArrayList) dependentFactMap.get(facts.get(i).toString());
                    if (dependentFact != null) {
                        for (int j = 0; j < dependentFact.size(); j++) {
                            if (otherTable == null) {
                                otherTable = dependentFact.get(j);
                            } else {
                                otherTable += "," + dependentFact.get(j);
                            }
                            allTables[i].add(dependentFact.get(j));
                        }
                    }
                }
                //added by susheela start 03-12-09

                String memId = "";
                String eleId = "";
                String tableId = "";
                String colId = "";
                ArrayList al2 = new ArrayList();
                String values = "";
                String tabName = "";
                String colName = "";
                for (int m = 0; m < filterMemIds.size(); m++) {
                    memId = filterMemIds.get(m).toString();
                    if (mems.containsKey(memId)) {
                        eleId = mems.get(memId).toString();
                    }

                    if (bussTablIds.containsKey(eleId)) {
                        al2 = (ArrayList) bussTablIds.get(eleId);
                        tableId = al2.get(0).toString();
                        colId = al2.get(1).toString();
                        tabName = al2.get(2).toString();
                        colName = al2.get(3).toString();
                        values = memberFilters.get(memId).toString();
                        //susheela modified on 11-12-09
                        values = values.replace("'", "''");
                        values = "'" + values + "'";
                        values = values.replace(",", "','");
                        values = values.replace("'||chr(38)||'", "||chr(38)||");

                        filterClause = filterClause + " and " + tabName + "." + colName + " in(" + values + ")";
                        // Amit Added new to support Agg Code -- Code is Doubtful..
                        otherTable += "," + al2.get(0).toString();

                        addAllTablesforReplace(allTablesforReplace, tabName);
                        addAllTablesforReplaceColumns(allTablesforReplaceColumns, tabName, colName);
                    }

                }


                String arrTabs[] = (String[]) bussTablIds.keySet().toArray(new String[0]);
                for (int m = 0; m < arrTabs.length; m++) {
                    ArrayList al = (ArrayList) bussTablIds.get(arrTabs[m]);
                    allTables[i].add(al.get(0).toString());
                }
                //added by susheela over 03-12-09

///Do not take out the commented code below
/// Need to add code to get related table to fact
// As per bharti's code
                ArrayList tempList = getAllTable(otherTable, facts.get(i).toString());
                for (int j = 0; j < tempList.size(); j++) {
                    logger.info("new table" + tempList.get(j));
                }
// allTables[i].add(tempList.get(j));--add this in above loop
// }
//
                if (qryType.equalsIgnoreCase("Trend") || isDateTrueforNonDt) {
                    //bgdao.istrendSupport = true;
                    allTables[i].add(TimetableId);


                }
                if (!bucketInnerTablesql.equalsIgnoreCase("")) {
                    bgdao.isCustomBucket = true;
                    bgdao.customBucketTablesql = bucketInnerTablesql;
                }
                boolean issummFact = false;
                if (isSummFact != null && isSummFact.get(facts.get(i)) != null) {
                    issummFact = true;
                }
                if (isNonTimeSummFact != null && isNonTimeSummFact.get(facts.get(i)) != null) {
                    summAdditinalClause = exploreSummrizationInfo(facts.get(i).toString(), (ArrayList) summFactAllParam.get(facts.get(i)));
                    ////
                    issummFact = true;
                }
                if (altFact != null) {
                    bgdao.altFact = altFact;
                }
                bgdao.isummSupport = issummFact;
                if (!(getRowViewbyCols() != null && getRowViewbyCols().size() == 1 && getRowViewbyCols().get(0).toString().equalsIgnoreCase("TIME"))) {
                    updateAggregateTableList(factName.get(i).toString(), ArrayToCommaString(allTablesforReplace), allTablesforReplace, allTablesforReplaceColumns, allTablesforReplacementTables, "Date");
                }
                bgdao.altFact = allTablesforReplacementTables.get(factName.get(i));
                factzqueries[i] = bgdao.viewBussDataWithouCol(allTables[i]) + " ";
                //added by Mohit for multicalender

                if (!collectMulticalender.isEmpty()) {
                    ArrayList Multicalender = ((ArrayList) collectMulticalender.get((collectMulticalender.keySet().toArray()[0])));
                    factzqueries[i] = factzqueries[i].replaceAll("PR_DAY_DENOM", Multicalender.get(8).toString()).replaceAll("pr_day_denom", Multicalender.get(8).toString());
                } else if (gbl_calander_var != null && !gbl_calander_var.equalsIgnoreCase("null") && !gbl_calander_var.equalsIgnoreCase("")) {
                    factzqueries[i] = factzqueries[i].replaceAll("PR_DAY_DENOM", gbl_calander_var).replaceAll("pr_day_denom", gbl_calander_var);

                } //added by mohit for multicalender


                factRelatedTablesMap.put(facts.get(i).toString(), bgdao.FinalBussTableIdRlt);
                factSummrizationTablesClause.put(facts.get(i).toString(), summAdditinalClause);
                if (!bucketelementId.equalsIgnoreCase("")) {
                    if (i == 0) {
                        String buckettableList = getBussTableNames(allTables[i]);
                        bucketWhereClause = bucketWhereclause(bucketelementId, buckettableList);

                        if (!bucketWhereClause.equalsIgnoreCase("")) {
                            bucketWhereClause = "  " + bucketWhereClause + "  BETWEEN  start_Limit  AND end_Limit ";

                        }
                        if (!bucketWhereClause.equalsIgnoreCase("")) {
                            bucketWhereClause = " " + bucketWhereClause;
                        }
                    }
                }
            }

            if (userConnType == null) {
                userConnType = getuserConnType(qryColumns, getRowViewbyCols());  // added by Mayank on 19- Aug
            }
            //  if (osql == null || "NULL".equalsIgnoreCase(osql)) {//cbram
            if (osql == null || "NULL".equalsIgnoreCase(osql.toString())) {
                //      osql = "";//cbram
                osql = new StringBuilder();
            }
            //      if (omsql == null || "NULL".equalsIgnoreCase(omsql)) {//cbram
            if (omsql == null || "NULL".equalsIgnoreCase(omsql.toString())) {
                //         omsql = "";//cbram
                omsql = new StringBuilder();
            }
            //         if (osqlGroup == null || "NULL".equalsIgnoreCase(osqlGroup)) {//cbram
            if (osqlGroup == null || "NULL".equalsIgnoreCase(osqlGroup.toString())) {
                //  osqlGroup = "";//cbram
                osqlGroup = new StringBuilder();
            }

            /*
             * Code for Generating Query , Code will execute for All the facts
             * one by one And Generate Query for Current prior This will not
             * generate code for Time based formaula Code below this for will do
             * that
             *
             *
             */
            for (int i = 0; i < facts.size(); i++) {
                //////////Chagning code for parameter
                // Making it fact dependent
                //boolean isfirstOr =false;
                paramWhereClause = "";
                boolean lastHasOr = false;
                ArrayList checkForParam = new ArrayList();
                if (factRelatedTablesMap != null && factRelatedTablesMap.size() > 0) {
                    if (factRelatedTablesMap.get(facts.get(i).toString()) != null) {
                        checkForParam = factRelatedTablesMap.get(facts.get(i).toString());
                        if (onlyParamWhereMap != null && onlyParamWhereMap.size() > 0) {
                            if (checkForParam != null && checkForParam.size() > 0) {
                                for (int processP = 0; processP < checkForParam.size(); processP++) {
                                    if (onlyParamWhereMap.get(checkForParam.get(processP).toString()) != null) {
                                        {
                                            if (lastHasOr) {
                                                paramWhereClause = onlyParamWhereMap.get(checkForParam.get(processP).toString()) + paramWhereClause;
                                            } else {
                                                paramWhereClause += onlyParamWhereMap.get(checkForParam.get(processP).toString());
                                            }

                                            //
                                            if (onlyParamWhereMap.get(checkForParam.get(processP).toString()).toString().toUpperCase().contains("OR ")) {
                                                lastHasOr = true;
                                            } else {
                                                lastHasOr = false;
                                            }

                                        }

                                    }
                                }
                            }
                        }



                    }
                }//dimention filter code will come here k.sreekanth@progenbusiness.com
                //paramWhereClause = " and ( 1=1 " +paramWhereClause;
                //paramWhereClause += "  ) ";
                paramWhereClause = setParameterClause(paramWhereClause);
                //////////////for target fact start
                String tabType = "";

                //////07-12-09
                String targetTabName = "";
                if (targetTableNames != null && targetTableNames.size() > 0) {
                    targetTabName = (String) targetTableNames.get(facts.get(i)).toString();
                }



                tabType = tabTypes.get(facts.get(i)).toString();
                //////////////for target fact
                if (insql[i] == null || "NULL".equalsIgnoreCase(insql[i])) {

                    insql[i] = "";
                }
                if (pinsql[i] == null || "NULL".equalsIgnoreCase(pinsql[i])) {
                    pinsql[i] = "";
                }
                // insqlGroup[i]
                if (insqlGroup[i] == null || "NULL".equalsIgnoreCase(insqlGroup[i])) {
                    insqlGroup[i] = "";
                }
                boolean issummFact = false;
                if (isSummFact != null && isSummFact.get(facts.get(i)) != null) {
                    issummFact = true;
                }
                OraFactHint = "";
                if (userConnType.equalsIgnoreCase("ORACLE") && issummFact) {
                    if (OraFactHintMap != null && OraFactHintMap.get(facts.get(i).toString()) != null) {
                        OraFactHint = "/*+ " + OraFactHintMap.get(facts.get(i).toString()).toString() + " */ ";
                    } else {
                        OraFactHint = "/*+ PARALLEL(" + summFactName.get(facts.get(i).toString()) + ",6) */ ";
                    }
                }
                clause = "";
                isTypeDate = false;
                typeDate = "";
                clause = "";
                ArrayList aList = new ArrayList();
                aList = getFactFilter(reportId, facts.get(i).toString());
                typeDate = aList.get(1).toString();
                if (typeDate.equalsIgnoreCase("Y")) {
                    isTypeDate = true;
                }
                clause = aList.get(0).toString();
                String factSecurityClause = "";
                factSecurityClause = getSecFactFilter(getUserId(), reportId, facts.get(i).toString());
                // Adding summrization to fact security
                factSecurityClause += factSummrizationTablesClause.get(facts.get(i).toString());
                String timeFactJoin = "";
                if (i == 0) {

                    if (qryType.equalsIgnoreCase("Trend") || isDateTrueforNonDt || issummFact) {
                        getTimeJoinforFact(facts.get(i).toString(), timetable[i].get(0).toString());
                    }
                    if ((!avoidProGenTime || !forcedAvoidProGenTime) && (qryType.equalsIgnoreCase("Trend") || isDateTrueforNonDt)) {

                        if (!tabType.equalsIgnoreCase("Target Table")) {
                            if (userConnType.equalsIgnoreCase("Sqlserver")) {
                                timeFactJoin += " on ( " + this.currTimeClause.replace("trunc(", "convert(date, ") + " ) ";
                            } else {//(@@PROGEN_TIME_SUMM@@) summTab0001
                                if (!isCohort) {
                                    timeFactJoin += " on ( " + this.currTimeClause + " ) ";
                                } else {
                                    timeFactJoin += " on ( PROGEN_TIME.ddate " + TimetableftClause.get(factandTimetable.get(facts.get(i).toString()).toString()).toString() + " )  ";
                                }
                            }
                            //Time clause commented uncomment later
                        }
                    }


                    if ((!avoidProGenTime || !forcedAvoidProGenTime) && timeHaveDate[i].get(0).toString().equals("Y") && !isTypeDate) {
                        if (issummFact) {
                            ArrayList timeInparam = (ArrayList) TimetableOtherValues.get(factandTimetable.get(facts.get(i).toString()));
                            String timeQry = pbTime.summTableCalculation(timeInparam.get(0).toString(), timeInparam.get(1).toString(), summtrend, timeLevel, summFactLevel.get(facts.get(i).toString()).toString());


                            if (timeQry == null || "".equals(timeQry)) {
                            } else {
                                timeFactJoin += " inner join (@@PROGEN_TIME_SUMM@@) summTab0001 ";
                                timeFactJoin = timeFactJoin.replace("@@PROGEN_TIME_SUMM@@", timeQry);
                                timeFactJoin += " on ( 1=1 " + timeClause[i].get(0) + " between summTab0001.T001_st_date and summTab0001.T001_end_date and " + summFactName.get(facts.get(i).toString()) + ".TIME_SUMM_FLAG = summTab0001.TIME_SUMM_FLAG " + " ) "; // TimetableCurrClause.get(factandTimetable.get(facts.get(i).toString()).toString()).toString();


                            }
                        }
                    }
//                    //////////////for target fact start
                    if (tabType.equalsIgnoreCase("Target Table")) {
                        //       finalSql += " select " + viewByColTarget + " , " + orderByColTarget + insql[i] + factzqueries[i] + timeFactJoin+ " where 1= 1" + targetParamWhereClause+ clause + factSecurityClause;//cbram
                        finalSql.append(" select ").append(viewByColTarget).append(" , ").append(orderByColTarget).append(insql[i]).append(factzqueries[i]).append(timeFactJoin).append(" where 1= 1").append(targetParamWhereClause).append(clause).append(factSecurityClause);
//                          finalSqlNew += " select " + viewByColTargetNew + " , " + orderByColTargetNew + insql[i] + factzqueries[i] + timeFactJoin+ factQueryEx + timeFactJoin.replaceAll("PROGEN_TIME", "GO_TIME") +  " where 1= 1" + targetParamWhereClause+ clause + factSecurityClause; // commented by mayank
                        finalSqlNew.append("select").append(viewByColTargetNew).append(",").append(orderByColTargetNew).append(insql[i]).append(factzqueries[i]).append(timeFactJoin).append(factQueryEx).append(timeFactJoin.replaceAll("PROGEN_TIME", "GO_TIME")).append("where 1= 1").append(targetParamWhereClause).append(clause);
                        //       finalSqlNew += " select " + viewByColTargetNew + " , " + orderByColTargetNew + insql[i] + factzqueries[i] + timeFactJoin+ factQueryEx + timeFactJoin.replaceAll("PROGEN_TIME", "GO_TIME") +  " where 1= 1" + targetParamWhereClause+ clause ;  cbRam

                    } else {
                        int no = StringUtils.countMatches(viewByCol, "VIEWBY");
                        int viewByLength = viewByCol.toUpperCase().split("VIEWBY").length - 1;
                        int orderByLength = viewByCol.toUpperCase().split("ORDER").length - 1;
                        String viewColArr[] = viewByCol.split(",");
                        String orderColArr[] = orderByCol.split(",");
                        if (isDimSegOnSumm) {
                            if (!isQueryForGO) {
                                for (int k = 0; k < viewByLength; k++) {
                                    finalSqlSummarized += ",VIEWBY" + (k + 1);
                                    finalSummarizedGrpBy += ",VIEWBY" + (k + 1);
                                }
                                for (int k = 0; k < viewByLength; k++) {
                                    finalSqlSummarized += ",ORDER" + (k + 1);
                                    finalSummarizedGrpBy += ",ORDER" + (k + 1);
                                }
                            } else {
                                for (int k = 0; k < rowViewbyCols.size(); k++) {
                                    finalSqlSummarized += ",A_" + rowViewbyCols.get(k);
                                    finalSummarizedGrpBy += ",A_" + rowViewbyCols.get(k);
                                }
                                for (int k = 0; k < viewByLength; k++) {
                                    finalSqlSummarized += ",O_" + rowViewbyCols.get(k);
                                    finalSummarizedGrpBy += ",O_" + rowViewbyCols.get(k);
                                }
                            }
                            finalSqlSummarized = finalSqlSummarized.substring(1);
                            finalSummarizedGrpBy = finalSummarizedGrpBy.substring(1);
                            finalSqlSummarized = getFinalSqlSummarized(insql[i].toString(), pinsql, finalSqlSummarized);//Nazneen
                        }
                        //        finalSql += " select " + OraFactHint + viewByCol + " , " + orderByCol;//cbram
                        finalSql.append(" select ").append(OraFactHint).append(viewByCol).append(" , ").append(orderByCol);
                        // finalSqlNew += " select " + OraFactHint + viewByColNew + " , " + additionalViewBy + " , " + orderByColNew +  " , " +additionalOrderBy; // cbram
                        finalSqlNew.append(" select ").append(OraFactHint).append(viewByColNew).append(" , ").append(additionalViewBy).append(" , ").append(orderByColNew).append(" , ").append(additionalOrderBy);
                        if (!bucketWhereClause.equalsIgnoreCase("")) {
                            if (isBucketNewViewBy) {
                                //           finalSql += "," + bucketName + "_" + reportId + ".start_Limit as start_Limit," + bucketName + "_" + reportId + ".end_Limit as end_Limit ";//cbram
                                finalSql.append(",").append(bucketName).append("_").append(reportId).append(".start_Limit as start_Limit,").append(bucketName).append("_").append(reportId).append(".end_Limit as end_Limit ");
                                //             finalSqlNew += "," + bucketName + "_" + reportId + ".start_Limit as start_Limit," + bucketName + "_" + reportId + ".end_Limit as end_Limit ";//cbram
                                finalSqlNew.append(" , ").append(bucketName).append("_").append(reportId).append(".start_Limit as start_Limit,").append(bucketName).append("_").append(reportId).append(".end_Limit as end_Limit ");
                            }
                        }


                        //finalSql += insql[i] +  "";// getFactFilter(reportId,facts.get(i).toString()).get(0);//cbram
                        finalSql.append(insql[i]).append("");
                        //  finalSqlNew += insql[i] +  "";// getFactFilter(reportId,facts.get(i).toString()).get(0);  cbram
                        finalSqlNew.append(insql[i]);
                        {
                            String mTemp = factzqueries[i] + timeFactJoin + " where 1= 1 " + paramWhereClause + factSecurityClause;
                            String mTempGO = factzqueries[i] + timeFactJoin + factQueryEx + " on ( 1=1 " + timeClause[i].get(0) + " = GO_TIME.DDATE " + ") where 1= 1 " + paramWhereClause;
                            mTemp = getProgenTimeReplaces(userConnType, mTemp);
                            mTempGO = getProgenTimeReplaces(userConnType, mTempGO);
                            //  finalSql += mTemp;//cbram
                            finalSql.append(mTemp);
                            //     finalSqlNew += mTempGO; cbram
                            finalSqlNew.append(mTempGO);
                        }
                        if (typeDate.contains("Y") && !qryType.equalsIgnoreCase("Trend")) {
                            isTypeDate = true;
                        } else {
                        }


                    }
                    //////////////for target fact

                    //////////////for target fact 07-12-09factandTimetable
                    if (tabType.equalsIgnoreCase("Target Table")) {

                        if (timeHaveDate[i].get(0).toString().equals("Y")) {

                            if (TimetableCurrClause.get(factandTimetable.get(facts.get(i).toString()).toString()).toString() != null) {
                                //    finalSql += timeClause[i].get(0) + TimetableCurrClause.get(factandTimetable.get(facts.get(i).toString()).toString()).toString();//cbram
                                finalSql.append(timeClause[i].get(0)).append(TimetableCurrClause.get(factandTimetable.get(facts.get(i).toString()).toString()).toString());
                                //      finalSqlNew += timeClause[i].get(0) + TimetableCurrClause.get(factandTimetable.get(facts.get(i).toString()).toString()).toString();  cbram
                                finalSqlNew.append(timeClause[i].get(0)).append(TimetableCurrClause.get(factandTimetable.get(facts.get(i).toString()).toString()).toString());
                            } else {
                                //   finalSql += timeClause[i].get(0) + pbTime.d_clu;//cbram
                                finalSql.append(timeClause[i].get(0)).append(pbTime.d_clu);
                                //   finalSqlNew += timeClause[i].get(0) + pbTime.d_clu;// cbram
                                finalSqlNew.append(timeClause[i].get(0) + pbTime.d_clu);
                            }
                            //finalSql += "  and  " + targetTabName + "." + pbTime.d_tar_clu;//commented
                        }
                    } else if (!avoidProGenTime && timeHaveDate[i].get(0).toString().equals("Y") && !isTypeDate) {
                        //finalSql += timeClause[i].get(0) + pbTime.d_clu;//Time clause commented uncomment later //timeHaveDate
                        if (issummFact) {
                            // timeClause[i].get(0) + TimetableOtherValues.get(factandTimetable.get(facts.get(i).toString()).toString()).toString()
                            ArrayList timeInparam = (ArrayList) TimetableOtherValues.get(factandTimetable.get(facts.get(i).toString()));
                            String timeQry = pbTime.summTableCalculation(timeInparam.get(0).toString(), timeInparam.get(1).toString(), summtrend, timeLevel, summFactLevel.get(facts.get(i).toString()).toString());
//                            finalSql += " inner join (@@PROGEN_TIME_SUMM@@) summTab0001 ";
//
                            if (timeQry == null || "".equals(timeQry)) {
                                String tempStr = finalSql.toString();
                                tempStr = tempStr.replace(", (@@PROGEN_TIME_SUMM@@) summTab0001", "");
                                StringBuilder strbld = new StringBuilder(tempStr);
                                finalSql = strbld;
                                //   finalSql += timeClause[i].get(0) + TimetableCurrClause.get(factandTimetable.get(facts.get(i).toString()).toString()).toString();//cbram
                                finalSql.append(timeClause[i].get(0)).append(TimetableCurrClause.get(factandTimetable.get(facts.get(i).toString()).toString()).toString());
                                String tempString = finalSqlNew.toString();
                                tempString = tempString.replace(", (@@PROGEN_TIME_SUMM@@) summTab0001", "");
                                StringBuilder str = new StringBuilder(tempString);
                                finalSqlNew = str;
                                //      finalSqlNew += timeClause[i].get(0) + TimetableCurrClause.get(factandTimetable.get(facts.get(i).toString()).toString()).toString();//cbram
                                finalSqlNew.append(timeClause[i].get(0)).append(TimetableCurrClause.get(factandTimetable.get(facts.get(i).toString()).toString()).toString());

                            }
                        } else {
                            if (TimetableCurrClause.get(factandTimetable.get(facts.get(i).toString()).toString().toLowerCase()) != null) {
                                //          finalSql += timeClause[i].get(0) + TimetableCurrClause.get(factandTimetable.get(facts.get(i).toString()).toString().toLowerCase()).toString();  //cbram
                                finalSql.append(timeClause[i].get(0)).append(TimetableCurrClause.get(factandTimetable.get(facts.get(i).toString()).toString().toLowerCase()).toString());
                                //           finalSqlNew += timeClause[i].get(0) + TimetableCurrClause.get(factandTimetable.get(facts.get(i).toString()).toString().toLowerCase()).toString();//cbram
                                finalSqlNew.append(timeClause[i].get(0)).append(TimetableCurrClause.get(factandTimetable.get(facts.get(i).toString()).toString().toLowerCase()).toString());
                            } else {
                                //       finalSql += timeClause[i].get(0) + TimetableCurrClause.get(factandTimetable.get(facts.get(i).toString()).toString()).toString();//cbram
                                finalSql.append(timeClause[i].get(0)).append(TimetableCurrClause.get(factandTimetable.get(facts.get(i).toString()).toString()).toString());
                                //  finalSqlNew += timeClause[i].get(0) + TimetableCurrClause.get(factandTimetable.get(facts.get(i).toString()).toString()).toString();//cbram
                                finalSqlNew.append(timeClause[i].get(0)).append(TimetableCurrClause.get(factandTimetable.get(facts.get(i).toString()).toString()).toString());
                            }
                        }
                    } else if (isTypeDate) {

                        //  finalSql += getDesc(factandTimetable.get(facts.get(i).toString()).toString(),"C",clause,userConnType);//cbram
                        finalSql.append(getDesc(factandTimetable.get(facts.get(i).toString()).toString(), "C", clause, userConnType));
                        //  finalSqlNew += getDesc(factandTimetable.get(facts.get(i).toString()).toString(),"C",clause,userConnType);//cbram
                        finalSqlNew.append(getDesc(factandTimetable.get(facts.get(i).toString()).toString(), "C", clause, userConnType));
                    }
                    //  finalSql += getDesc(factandTimetable.get(facts.get(i).toString()).toString(),"C",clause,userConnType);//cbram
                    finalSql.append(getDesc(factandTimetable.get(facts.get(i).toString()).toString(), "C", clause, userConnType));
                    //       finalSqlNew += getDesc(factandTimetable.get(facts.get(i).toString()).toString(),"C",clause,userConnType); //cbram
                    finalSqlNew.append(getDesc(factandTimetable.get(facts.get(i).toString()).toString(), "C", clause, userConnType));



                    //added by susheela start 03-12-09
                    //   finalSql += filterClause;//cbram
                    finalSql.append(filterClause);
                    //              finalSqlNew += filterClause; //cbram
                    finalSqlNew.append(filterClause);
                    //added by susheela over 03-12-09
                    //////////////for target fact start

                    if (tabType.equalsIgnoreCase("Target Table")) {
                        if (!isKpi || isInnerViewBy) {
                            //    finalSql += " group by " + groupByColTarget;//cbram
                            finalSql.append(" group by ").append(groupByColTarget);
                            //            finalSqlNew += " group by " + groupByColTarget + " , " + additionalgroupByCol;//cbram
                            finalSqlNew.append(" group by ").append(groupByColTarget).append(" , ").append(additionalgroupByCol);
                        }
                    } else {

                        if (!bucketWhereClause.equalsIgnoreCase("")) {
                            if (!isKpi || isInnerViewBy) {
                                //       finalSql += " group by " + groupByCol + insqlGroup[i]; //cbram
                                finalSql.append(" group by ").append(groupByCol).append(insqlGroup[i]);
                                //  finalSqlNew += " group by " + groupByCol + " , " + additionalgroupByCol + insqlGroup[i];//cbram
                                finalSqlNew.append(" group by ").append(groupByCol).append(" , ").append(additionalgroupByCol).append(insqlGroup[i]);
                            } else if (isUngroupedMeasure) {
                                if (insqlGroup[i].startsWith(",") || insqlGroup[i].startsWith(" ,")) {
                                    insqlGroup[i] = insqlGroup[i].substring(insqlGroup[i].indexOf(",") + 1);
                                }

                                //     finalSql += " group by " + insqlGroup[i]; //cbram
                                finalSql.append(" group by ").append(insqlGroup[i]);
                                //  finalSqlNew += " group by "   + insqlGroup[i]; //cbram
                                finalSqlNew.append(" group by ").append(insqlGroup[i]);

                            }
//|| isBucketReplace
                            if (isBucketNewViewBy) {
                                //  finalSql += "," + bucketName + "_" + reportId + ".start_Limit ," + bucketName + "_" + reportId + ".end_Limit ";//cbram
                                finalSql.append(",").append(bucketName).append("_").append(reportId).append(".start_Limit ,").append(bucketName).append("_").append(reportId).append(".end_Limit ");
                                //              finalSqlNew += "," + bucketName + "_" + reportId + ".start_Limit ," + bucketName + "_" + reportId + ".end_Limit ";// cbram
                                finalSqlNew.append(",").append(bucketName).append("_").append(reportId).append(".start_Limit ,").append(bucketName).append("_").append(reportId).append(".end_Limit ");
                                //            finalSql = " select " + OViewByCol + " , " + OorderByCol + " " + bucketOuterQuerynonViewCols + " from (" + finalSql + ") O1 where " + bucketWhereClause;//") group by "+OViewByCol; //cbram
                                StringBuilder queryStr = new StringBuilder();
                                queryStr.append(" select ").append(OViewByCol).append(" , ").append(OorderByCol).append(" ").append(bucketOuterQuerynonViewCols).append(" from (").append(finalSql).append(") O1 where ").append(bucketWhereClause);
                                finalSql = queryStr;
                                //       finalSqlNew = " select " + OViewByColNew + " , " + OorderByColNew + " " + bucketOuterQuerynonViewCols + " from (" + finalSqlNew + ") O1 where " + bucketWhereClause;//") group by "+OViewByCol;  //cbram
                                StringBuilder queryStr1 = new StringBuilder();
                                queryStr1.append(" select ").append(OViewByColNew).append(" , ").append(OorderByColNew).append(" ").append(bucketOuterQuerynonViewCols).append(" from (").append(finalSqlNew).append(") O1 where ").append(bucketWhereClause);
                                finalSqlNew = queryStr1;
                            }
                        } else {
                            if (!isKpi || isInnerViewBy) {
                                //       finalSql += " group by " + groupByCol + insqlGroup[i];//cbram
                                finalSql.append(" group by ").append(groupByCol).append(insqlGroup[i]);
                                //   finalSqlNew += " group by " +  groupByCol + " , " + additionalgroupByCol  + insqlGroup[i];//cbram
                                finalSqlNew.append(" group by ").append(groupByCol).append(" , ").append(additionalgroupByCol).append(insqlGroup[i]);
                            } else if (isUngroupedMeasure) {
                                if (insqlGroup[i].startsWith(",") || insqlGroup[i].startsWith(" ,")) {
                                    insqlGroup[i] = insqlGroup[i].substring(insqlGroup[i].indexOf(",") + 1);
                                }

                                //     finalSql += " group by "  + insqlGroup[i]; //cbram
                                finalSql.append(" group by ").append(insqlGroup[i]);
                                //     finalSqlNew += " group by "  + insqlGroup[i]; //cbram
                                finalSqlNew.append(" group by ").append(insqlGroup[i]);
                            }
                        }
                    }
                    if (needPrior) {

                        timeFactJoin = "";
                        if ((!avoidProGenTime || !forcedAvoidProGenTime) && (qryType.equalsIgnoreCase("Trend") || isDateTrueforNonDt)) {
                            if (!tabType.equalsIgnoreCase("Target Table")) {
                                if (userConnType.equalsIgnoreCase("Sqlserver")) {
                                    timeFactJoin += " on ( " + this.priorTimeClause.replace("trunc(", "convert(date, ") + " ) ";
                                } else {
                                    if (!isCohort) {
                                        timeFactJoin += " on ( " + this.priorTimeClause + " ) ";
                                    } else {
                                        timeFactJoin += " on ( PROGEN_TIME.ddate " + TimetableftClause.get(factandTimetable.get(facts.get(i).toString()).toString()).toString() + " ) ";
                                    }

                                }
                            }
                        }
                        if ((!avoidProGenTime || !forcedAvoidProGenTime) && timeHaveDate[i].get(0).toString().equals("Y") && !isTypeDate) {
                            //finalSql += timeClause[i].get(0) + pbTime.d_clu;//Time clause commented uncomment later //timeHaveDate
                            if (issummFact) {
                                //
                                // timeClause[i].get(0) + TimetableOtherValues.get(factandTimetable.get(facts.get(i).toString()).toString()).toString()
                                ArrayList timeInparam = (ArrayList) TimetableOtherValues.get(factandTimetable.get(facts.get(i).toString()));
                                String timeQry = pbTime.summTableCalculation(timeInparam.get(0).toString(), timeInparam.get(1).toString(), summtrend, timeLevel, summFactLevel.get(facts.get(i).toString()).toString());


                                if (timeQry == null || "".equals(timeQry)) {
                                } else {
                                    timeFactJoin += " inner join (@@PROGEN_TIME_SUMM@@) summTab0001 ";
                                    timeFactJoin = timeFactJoin.replace("@@PROGEN_TIME_SUMM@@", timeQry);
                                    timeFactJoin += " on ( 1=1 " + timeClause[i].get(0) + " between summTab0001.T001_st_date and summTab0001.T001_end_date and " + summFactName.get(facts.get(i).toString()) + ".TIME_SUMM_FLAG = summTab0001.TIME_SUMM_FLAG " + " ) "; // TimetableCurrClause.get(factandTimetable.get(facts.get(i).toString()).toString()).toString();


                                }
                            }
                        }
                        if (tabType.equalsIgnoreCase("Target Table")) {
                            //      finalSql += " union all select " + viewByColTarget + " , " + orderByColTarget + " "; //cbram
                            finalSql.append(" union all select ").append(viewByColTarget).append(" , ").append(orderByColTarget).append(" ");
                            //      finalSqlNew += " union all select " + viewByColTargetNew + " , " + additionalViewBy + " , " + orderByColTargetNew + " , " + additionalOrderBy + " "; //cbram
                            finalSqlNew.append(" union all select ").append(viewByColTargetNew).append(" , ").append(additionalViewBy).append(" , ").append(orderByColTargetNew).append(" , ").append(additionalOrderBy).append(" ");
                        } else {
                            if (qryType.equalsIgnoreCase("Trend") || isDateTrueforNonDt) {
                                //     finalSql += " union all select " + OraFactHint + pviewByCol + " , " + porderByCol + " ";//cbram
                                finalSql.append(" union all select ").append(OraFactHint).append(pviewByCol).append(" , ").append(porderByCol).append(" ");
                                //    finalSqlNew += " union all select " + OraFactHint + pviewByColNew + " , " + additionalViewBy + " , " + porderByColNew + " , " + additionalOrderBy + " ";  //cbram
                                finalSqlNew.append(" union all select ").append(OraFactHint).append(pviewByColNew).append(" , ").append(additionalViewBy).append(" , ").append(porderByColNew).append(" , ").append(additionalOrderBy).append(" ");
                            } else {
                                //     finalSql += " union all select " + OraFactHint + pviewByCol + " , " + porderByCol + " ";//cbram
                                finalSql.append(" union all select ").append(OraFactHint).append(pviewByCol).append(" , ").append(porderByCol).append(" ");
                                //       finalSqlNew += " union all select " + OraFactHint + pviewByColNew + "  , " + additionalViewBy + " , " + porderByCol + " , " + additionalOrderBy + " ";//cbram
                                finalSqlNew.append(" union all select ").append(OraFactHint).append(pviewByColNew).append("  , ").append(additionalViewBy).append(" , ").append(porderByCol).append(" , ").append(additionalOrderBy).append(" ");
                            }

                        }

                        if (tabType.equalsIgnoreCase("Target Table")) {

                            //           finalSql += pinsql[i] + factzqueries[i] + timeFactJoin +" where 1=1 " + targetParamWhereClause + clause + factSecurityClause; // + targetTableNamesParamClause.get(targetTabName);//targetParamWhereClause;//cbram
                            finalSql.append(pinsql[i]).append(factzqueries[i]).append(timeFactJoin).append(" where 1=1 ").append(targetParamWhereClause).append(clause).append(factSecurityClause);
                            //  finalSqlNew += pinsql[i] + factzqueries[i] + timeFactJoin + factQueryEx + timeFactJoin.replaceAll("PROGEN_TIME", "GO_TIME") +" where 1=1 " + targetParamWhereClause + clause ; // + targetTableNamesParamClause.get(targetTabName);//targetParamWhereClause;  //cbram
                            finalSqlNew.append(pinsql[i]).append(factzqueries[i]).append(timeFactJoin).append(factQueryEx).append(timeFactJoin.replaceAll("PROGEN_TIME", "GO_TIME")).append(" where 1=1 ").append(targetParamWhereClause).append(clause);
                        } else {
                            //    finalSql += pinsql[i] +  "" ;//cbram
                            finalSql.append(pinsql[i]).append("");
                            //  finalSqlNew += pinsql[i] +  "" ;  //cbram
                            finalSqlNew.append(pinsql[i]).append("");
                            {
                                String mTemp = factzqueries[i] + timeFactJoin + " where 1=1 " + paramWhereClause + factSecurityClause;
                                String mTempGO = factzqueries[i] + timeFactJoin + factQueryEx + timeFactJoin.replaceAll("PROGEN_TIME", "GO_TIME") + " where 1= 1 " + paramWhereClause;
                                if (timeFactJoin.equalsIgnoreCase("")) {
                                    mTempGO = factzqueries[i] + timeFactJoin + factQueryEx + " on ( 1=1 " + timeClause[i].get(0) + " = GO_TIME.DDATE " + ") where 1= 1 " + paramWhereClause;
                                }
                                mTemp = getProgenTimeReplaces(userConnType, mTemp);
                                mTempGO = getProgenTimeReplaces(userConnType, mTempGO);
                                //       finalSql += mTemp; //cbram
                                finalSql.append(mTemp);
                                //    finalSqlNew += mTempGO; //cbram
                                finalSqlNew.append(mTempGO);

                            }
                            if (typeDate.contains("Y") && !qryType.equalsIgnoreCase("Trend")) {
                                isTypeDate = true;
                            } else {
                            }

                        }
                        //////////////for target fact

                        ////// 07-12-09
                        if (tabType.equalsIgnoreCase("Target Table")) {

                            if (timeHaveDate[i].get(0).toString().equals("Y")) {
                                if (TimetablePriorClause.get(factandTimetable.get(facts.get(i).toString()).toString()).toString() != null) {
                                    //          finalSql += timeClause[i].get(0) + TimetablePriorClause.get(factandTimetable.get(facts.get(i).toString()).toString()).toString();//cbram
                                    finalSql.append(timeClause[i].get(0)).append(TimetablePriorClause.get(factandTimetable.get(facts.get(i).toString()).toString()).toString());
                                    //  finalSqlNew += timeClause[i].get(0) + TimetablePriorClause.get(factandTimetable.get(facts.get(i).toString()).toString()).toString();//cbram
                                    finalSqlNew.append(timeClause[i].get(0)).append(TimetablePriorClause.get(factandTimetable.get(facts.get(i).toString()).toString()).toString());
                                } else {
                                    //   finalSql += timeClause[i].get(0) + pbTime.d_clu;//
                                    finalSql.append(timeClause[i].get(0)).append(pbTime.d_clu);
                                    //        finalSqlNew += timeClause[i].get(0) + pbTime.d_clu;//cbram
                                    finalSqlNew.append(timeClause[i].get(0)).append(pbTime.d_clu);
                                }
                            }
                        } else if (!avoidProGenTime && timeHaveDate[i].get(0).toString().equals("Y") && !isTypeDate) {
                            if (issummFact) {
                                ArrayList timeInparam = (ArrayList) TimetableOtherValues.get(factandTimetable.get(facts.get(i).toString()));
                                String timeQry = pbTime.summTableCalculation(timeInparam.get(2).toString(), timeInparam.get(3).toString(), summtrend, timeLevel, summFactLevel.get(facts.get(i).toString()).toString());
                                if (timeQry == null || "".equals(timeQry)) {
                                    String tempStr = finalSql.toString();
                                    tempStr = tempStr.replace(", (@@PROGEN_TIME_SUMM@@) summTab0001", "");
                                    StringBuilder strbld = new StringBuilder(tempStr);
                                    finalSql = strbld;
                                    //  finalSqlNew = finalSql.replace(", (@@PROGEN_TIME_SUMM@@) summTab0001", "");//cbram
                                    finalSqlNew.append(finalSql.toString().replace(", (@@PROGEN_TIME_SUMM@@) summTab0001", ""));
                                    //         finalSql += timeClause[i].get(0) + TimetablePriorClause.get(factandTimetable.get(facts.get(i).toString()).toString()).toString();//cbram
                                    finalSql.append(timeClause[i].get(0)).append(TimetablePriorClause.get(factandTimetable.get(facts.get(i).toString()).toString()).toString());
                                    //  finalSqlNew += timeClause[i].get(0) + TimetablePriorClause.get(factandTimetable.get(facts.get(i).toString()).toString()).toString(); //cbram
                                    finalSqlNew.append(timeClause[i].get(0)).append(TimetablePriorClause.get(factandTimetable.get(facts.get(i).toString()).toString()).toString());

                                }
                            } else {
                                if (TimetablePriorClause.get(factandTimetable.get(facts.get(i).toString()).toString().toLowerCase()) != null) {
                                    //             finalSql += timeClause[i].get(0) + TimetablePriorClause.get(factandTimetable.get(facts.get(i).toString()).toString().toLowerCase()).toString();//cbram
                                    finalSql.append(timeClause[i].get(0)).append(TimetablePriorClause.get(factandTimetable.get(facts.get(i).toString()).toString().toLowerCase()).toString());
                                    //  finalSqlNew += timeClause[i].get(0) + TimetablePriorClause.get(factandTimetable.get(facts.get(i).toString()).toString().toLowerCase()).toString();//cbram
                                    finalSqlNew.append(timeClause[i].get(0)).append(TimetablePriorClause.get(factandTimetable.get(facts.get(i).toString()).toString().toLowerCase()).toString());
                                } else {
                                    //    finalSql += timeClause[i].get(0) + TimetablePriorClause.get(factandTimetable.get(facts.get(i).toString()).toString()).toString();//cbram
                                    finalSql.append(timeClause[i].get(0)).append(TimetablePriorClause.get(factandTimetable.get(facts.get(i).toString()).toString()).toString());
                                    //    finalSqlNew += timeClause[i].get(0) + TimetablePriorClause.get(factandTimetable.get(facts.get(i).toString()).toString()).toString(); //cbram
                                    finalSqlNew.append(timeClause[i].get(0)).append(TimetablePriorClause.get(factandTimetable.get(facts.get(i).toString()).toString()).toString());
                                }
                            }

                        } else if (isTypeDate) {
                            //    finalSql += getDesc(factandTimetable.get(facts.get(i).toString()).toString(),"P",clause,userConnType);//cbram
                            finalSql.append(getDesc(factandTimetable.get(facts.get(i).toString()).toString(), "P", clause, userConnType));
                            //   finalSqlNew += getDesc(factandTimetable.get(facts.get(i).toString()).toString(),"P",clause,userConnType); //cbram
                            finalSqlNew.append(getDesc(factandTimetable.get(facts.get(i).toString()).toString(), "P", clause, userConnType));
                        }

                        //    finalSql += getDesc(factandTimetable.get(facts.get(i).toString()).toString(),"P",clause,userConnType);//cbram
                        finalSql.append(getDesc(factandTimetable.get(facts.get(i).toString()).toString(), "P", clause, userConnType));
                        //   finalSqlNew += getDesc(factandTimetable.get(facts.get(i).toString()).toString(),"P",clause,userConnType);//cbram
                        finalSqlNew.append(getDesc(factandTimetable.get(facts.get(i).toString()).toString(), "P", clause, userConnType));

                        //added by susheela start 03-12-09
                        //   finalSql += filterClause;//cbram
                        finalSql.append(filterClause);
                        //   finalSqlNew += filterClause; //cbram
                        finalSqlNew.append(filterClause);
                        //added by susheela over 03-12-09

                        //////////////for target fact start
                        if (tabType.equalsIgnoreCase("Target Table")) {
                            if (!isKpi || isInnerViewBy) {
                                //       finalSql += " group by " + groupByColTarget;//cbram
                                finalSql.append(" group by ").append(groupByColTarget);
                                //  finalSqlNew += " group by " + groupByColTarget + " , " + additionalgroupByCol;  //cbram
                                finalSqlNew.append(" group by ").append(groupByColTarget).append(" , ").append(additionalgroupByCol);
                            }
                        } else {
                            if (!isKpi || isInnerViewBy) {
                                if (qryType.equalsIgnoreCase("Trend") || isDateTrueforNonDt) {
                                    //       finalSql += " group by " + pgroupByCol + insqlGroup[i];//cbram
                                    finalSql.append(" group by ").append(pgroupByCol).append(insqlGroup[i]);
                                    //                finalSqlNew += " group by " + pgroupByColNew + " , " + additionalgroupByCol + insqlGroup[i];//cbram
                                    finalSqlNew.append(" group by ").append(pgroupByColNew).append(" , ").append(additionalgroupByCol).append(insqlGroup[i]);

                                } else {
                                    //     finalSql += " group by " + pgroupByCol + insqlGroup[i];//cbram
                                    finalSql.append(" group by ").append(pgroupByCol).append(insqlGroup[i]);
                                    //       finalSqlNew += " group by " + pgroupByColNew + " , " + additionalgroupByCol + insqlGroup[i];  //cbram
                                    finalSqlNew.append(" group by ").append(pgroupByColNew).append(" , ").append(additionalgroupByCol).append(insqlGroup[i]);
                                }
                            } else if (isUngroupedMeasure) {

                                if (insqlGroup[i].startsWith(",") || insqlGroup[i].startsWith(" ,")) {
                                    insqlGroup[i] = insqlGroup[i].substring(insqlGroup[i].indexOf(",") + 1);
                                }
                                //      finalSql += " group by " + insqlGroup[i];//cbram
                                finalSql.append(" group by ").append(insqlGroup[i]);
                                //   finalSqlNew += " group by " + insqlGroup[i]; //cbram
                                finalSqlNew.append(" group by ").append(insqlGroup[i]);

                            }

                        }
                        //////////////for target fact
                    }




                } else {
                    if (qryType.equalsIgnoreCase("Trend") || isDateTrueforNonDt || issummFact) {
                        getTimeJoinforFact(facts.get(i).toString(), timetable[i].get(0).toString());
                    }
                    timeFactJoin = "";
                    if ((!avoidProGenTime || !forcedAvoidProGenTime) && (qryType.equalsIgnoreCase("Trend") || isDateTrueforNonDt)) {
                        if (!tabType.equalsIgnoreCase("Target Table")) {
                            if (!isCohort) {
                                timeFactJoin += " on ( " + this.currTimeClause + " ) ";
                            } else {
                                timeFactJoin += " on ( PROGEN_TIME.ddate " + TimetableftClause.get(factandTimetable.get(facts.get(i).toString()).toString()).toString() + " ) ";
                            }

                        }
                    }

                    if ((!avoidProGenTime || !forcedAvoidProGenTime) && timeHaveDate[i].get(0).toString().equals("Y") && !isTypeDate) {
                        //finalSql += timeClause[i].get(0) + pbTime.d_clu;//Time clause commented uncomment later //timeHaveDate
                        if (issummFact) {
                            ArrayList timeInparam = (ArrayList) TimetableOtherValues.get(factandTimetable.get(facts.get(i).toString()));
                            String timeQry = pbTime.summTableCalculation(timeInparam.get(0).toString(), timeInparam.get(1).toString(), summtrend, timeLevel, summFactLevel.get(facts.get(i).toString()).toString());


                            if (timeQry == null || "".equals(timeQry)) {
                            } else {
                                timeFactJoin += " inner join (@@PROGEN_TIME_SUMM@@) summTab0001 ";
                                timeFactJoin = timeFactJoin.replace("@@PROGEN_TIME_SUMM@@", timeQry);
                                timeFactJoin += " on ( 1=1 " + timeClause[i].get(0) + " between summTab0001.T001_st_date and summTab0001.T001_end_date and " + summFactName.get(facts.get(i).toString()) + ".TIME_SUMM_FLAG = summTab0001.TIME_SUMM_FLAG " + " ) "; // TimetableCurrClause.get(factandTimetable.get(facts.get(i).toString()).toString()).toString();


                            }
                        }
                    }
                    finalSqlSummarized = getFinalSqlSummarizedReplaced(insql[i], pinsql, finalSqlSummarized);


                    //////////////for target fact start
                    if (tabType.equalsIgnoreCase("Target Table")) {
                        //  finalSql += " union all " + " select " + viewByColTarget + " , " + orderByColTarget + insql[i] + factzqueries[i] + timeFactJoin +" where 1= 1 " + targetParamWhereClause + clause + factSecurityClause;//cbram
                        finalSql.append(" union all ").append(" select ").append(viewByColTarget).append(" , ").append(orderByColTarget).append(insql[i]).append(factzqueries[i]).append(timeFactJoin).append(" where 1= 1 ").append(targetParamWhereClause).append(clause).append(factSecurityClause);
                        //  finalSqlNew += " union all " + " select " + viewByColTargetNew + " , " + orderByColTarget + insql[i] + factzqueries[i] + timeFactJoin +" where 1= 1 " + targetParamWhereClause + clause ; //cbram
                        finalSqlNew.append(" union all ").append(" select ").append(viewByColTargetNew).append(" , ").append(orderByColTarget).append(insql[i]).append(factzqueries[i]).append(timeFactJoin).append(" where 1= 1 ").append(targetParamWhereClause).append(clause);
                    } else {
//                        finalSql += " union all " + " select " + OraFactHint + viewByCol + " , " + orderByCol + insql[i] + factzqueries[i] + " " + paramWhereClause + getFactFilter(reportId,facts.get(i).toString()).get(0);
                        //           finalSql += " union all " + " select " + OraFactHint + viewByCol + " , " + orderByCol + insql[i] +  "";//cbram
                        finalSql.append(" union all ").append(" select ").append(OraFactHint).append(viewByCol).append(" , ").append(orderByCol).append(insql[i]).append("");
                        //  finalSqlNew += " union all " + " select " + OraFactHint + viewByColNew + " , " + additionalViewBy + " , " + orderByCol + " , " + additionalOrderBy + insql[i] +  ""; //cbram
                        finalSqlNew.append(" union all ").append(" select ").append(OraFactHint).append(viewByColNew).append(" , ").append(additionalViewBy).append(" , ").append(orderByCol).append(" , ").append(additionalOrderBy).append(insql[i]).append("");
                        {
                            String mTemp = factzqueries[i] + timeFactJoin + " where 1 =1 " + paramWhereClause + factSecurityClause;
                            String mTempGO = factzqueries[i] + timeFactJoin + factQueryEx + timeFactJoin.replaceAll("PROGEN_TIME", "GO_TIME") + " where 1= 1 " + paramWhereClause;
                            if (timeFactJoin.equalsIgnoreCase("")) {
                                mTempGO = factzqueries[i] + timeFactJoin + factQueryEx + " on ( 1=1 " + timeClause[i].get(0) + " = GO_TIME.DDATE " + ") where 1= 1 " + paramWhereClause;
                            }
                            mTemp = getProgenTimeReplaces(userConnType, mTemp);
                            mTempGO = getProgenTimeReplaces(userConnType, mTempGO);
                            //         finalSql += mTemp;//cbram
                            finalSql.append(mTemp);
                            //   finalSqlNew += mTempGO;//cbram
                            finalSqlNew.append(mTempGO);
                        }
                        if (typeDate.contains("Y") && !qryType.equalsIgnoreCase("Trend")) {
                            isTypeDate = true;
                        } else {
                        }
                    }

                    //////////////for target fact
                    //modified on 11jan-10 susheela start
                    if (tabType.equalsIgnoreCase("Target Table")) {

                        if (timeHaveDate[i].get(0).toString().equals("Y")) {
                            //             finalSql += timeClause[i].get(0) + TimetableCurrClause.get(factandTimetable.get(facts.get(i).toString()).toString()).toString(); //cbram
                            finalSql.append(timeClause[i].get(0)).append(TimetableCurrClause.get(factandTimetable.get(facts.get(i).toString()).toString()).toString());
                            //       finalSqlNew += timeClause[i].get(0) + TimetableCurrClause.get(factandTimetable.get(facts.get(i).toString()).toString()).toString();//cbram
                            finalSqlNew.append(timeClause[i].get(0) + TimetableCurrClause.get(factandTimetable.get(facts.get(i).toString()).toString()).toString());
                        }
                    } else if (!avoidProGenTime && timeHaveDate[i].get(0).toString().equals("Y") && !isTypeDate) {
                        if (issummFact) {
                            ArrayList timeInparam = (ArrayList) TimetableOtherValues.get(factandTimetable.get(facts.get(i).toString()));

                            String timeQry = pbTime.summTableCalculation(timeInparam.get(0).toString(), timeInparam.get(1).toString(), summtrend, timeLevel, summFactLevel.get(facts.get(i).toString()).toString());
                            if (timeQry == null || "".equals(timeQry)) {
                                String tempStr = finalSql.toString();
                                tempStr = tempStr.replace(", (@@PROGEN_TIME_SUMM@@) summTab0001", "");
                                StringBuilder strbld = new StringBuilder(tempStr);
                                finalSql = strbld;
                                //finalSql += timeClause[i].get(0) + TimetableCurrClause.get(factandTimetable.get(facts.get(i).toString()).toString()).toString();//cbram
                                finalSql.append(timeClause[i].get(0)).append(TimetableCurrClause.get(factandTimetable.get(facts.get(i).toString()).toString()).toString());
                                String tempString = finalSqlNew.toString();
                                tempString = tempString.replace(", (@@PROGEN_TIME_SUMM@@) summTab0001", "");
                                StringBuilder str = new StringBuilder(tempString);
                                finalSqlNew = str;
                                //finalSqlNew += timeClause[i].get(0) + TimetableCurrClause.get(factandTimetable.get(facts.get(i).toString()).toString()).toString();//cbram
                                finalSqlNew.append(timeClause[i].get(0) + TimetableCurrClause.get(factandTimetable.get(facts.get(i).toString()).toString()).toString());
                            }
                        } else {
                            if (TimetableCurrClause.get(factandTimetable.get(facts.get(i).toString()).toString().toLowerCase()) != null) {
                                //             finalSql += timeClause[i].get(0) + TimetableCurrClause.get(factandTimetable.get(facts.get(i).toString()).toString().toLowerCase()).toString(); //cbram
                                finalSql.append(timeClause[i].get(0)).append(TimetableCurrClause.get(factandTimetable.get(facts.get(i).toString()).toString().toLowerCase()).toString());
                                //finalSqlNew += timeClause[i].get(0) + TimetableCurrClause.get(factandTimetable.get(facts.get(i).toString()).toString().toLowerCase()).toString();//cbram
                                finalSqlNew.append(timeClause[i].get(0) + TimetableCurrClause.get(factandTimetable.get(facts.get(i).toString()).toString().toLowerCase()).toString());
                            } else {
                                //      finalSql += timeClause[i].get(0) + TimetableCurrClause.get(factandTimetable.get(facts.get(i).toString()).toString()).toString();//cbram
                                finalSql.append(timeClause[i].get(0)).append(TimetableCurrClause.get(factandTimetable.get(facts.get(i).toString()).toString()).toString());
                                //  finalSqlNew += timeClause[i].get(0) + TimetableCurrClause.get(factandTimetable.get(facts.get(i).toString()).toString()).toString(); //cbram
                                finalSqlNew.append(timeClause[i].get(0) + TimetableCurrClause.get(factandTimetable.get(facts.get(i).toString()).toString()).toString());
                            }
                        }
                    } else if (isTypeDate) {

                        //     finalSql += getDesc(factandTimetable.get(facts.get(i).toString()).toString(),"C",clause,userConnType);//cbram
                        finalSql.append(getDesc(factandTimetable.get(facts.get(i).toString()).toString(), "C", clause, userConnType));
                        //finalSqlNew += getDesc(factandTimetable.get(facts.get(i).toString()).toString(),"C",clause,userConnType);//cbram
                        finalSqlNew.append(getDesc(factandTimetable.get(facts.get(i).toString()).toString(), "C", clause, userConnType));

                    }

                    //       finalSql += getDesc(factandTimetable.get(facts.get(i).toString()).toString(),"C",clause,userConnType);//cbram
                    finalSql.append(getDesc(factandTimetable.get(facts.get(i).toString()).toString(), "C", clause, userConnType));
                    //finalSqlNew += getDesc(factandTimetable.get(facts.get(i).toString()).toString(),"C",clause,userConnType); //cbram
                    finalSqlNew.append(getDesc(factandTimetable.get(facts.get(i).toString()).toString(), "C", clause, userConnType));
                    //modified on 11jan-10 susheela over



                    //added by susheela start 03-12-09
                    //      finalSql += filterClause;//cbram
                    finalSql.append(filterClause);
                    //finalSqlNew += filterClause;//cbram
                    finalSqlNew.append(filterClause);
                    //added by susheela over 03-12-09

                    //////////////for target fact start
                    if (tabType.equalsIgnoreCase("Target Table")) {
                        if (!isKpi || isInnerViewBy) {
                            //         finalSql += " group by " + groupByColTarget;//cbram
                            finalSql.append(" group by ").append(groupByColTarget);
                            //   finalSqlNew += " group by " + groupByColTarget;//cbram
                            finalSqlNew.append(" group by ").append(groupByColTarget);
                        }
                    } else {
                        if (!isKpi || isInnerViewBy) {
                            //      finalSql += " group by " + groupByCol + insqlGroup[i];//cbram
                            finalSql.append(" group by ").append(groupByCol).append(insqlGroup[i]);
                            //         finalSqlNew += " group by " + groupByCol  + " , " + additionalgroupByCol + insqlGroup[i];//cbram
                            finalSqlNew.append(" group by ").append(groupByCol).append(" , ").append(additionalgroupByCol).append(insqlGroup[i]);
                        } else if (isUngroupedMeasure) {
                            if (insqlGroup[i].startsWith(",") || insqlGroup[i].startsWith(" ,")) {
                                insqlGroup[i] = insqlGroup[i].substring(insqlGroup[i].indexOf(",") + 1);
                            }
                            //finalSql += " group by "  + insqlGroup[i];//cbram
                            finalSql.append(" group by ").append(insqlGroup[i]);
                            //    finalSqlNew += " group by "   + insqlGroup[i];//cbram
                            finalSqlNew.append(" group by ").append(insqlGroup[i]);

                        }
                    }

                    if (needPrior) {
                        if (tabType.equalsIgnoreCase("Target Table")) {
                            //          finalSql += " union all select " + viewByColTarget + " , " + orderByColTarget + " ";//cbram
                            finalSql.append(" union all select ").append(viewByColTarget).append(" , ").append(orderByColTarget).append(" ");
                            //  finalSqlNew += " union all select " + viewByColTargetNew + " , " + orderByColTargetNew + " ";//cbram
                            finalSqlNew.append(" union all select ").append(viewByColTargetNew).append(" , ").append(orderByColTargetNew).append(" ");
                        } else {
                            if (qryType.equalsIgnoreCase("Trend") || isDateTrueforNonDt) {
                                //              finalSql += " union all select " + OraFactHint + pviewByCol + " , " + porderByCol + " ";//cbram
                                finalSql.append(" union all select ").append(OraFactHint).append(pviewByCol).append(" , ").append(porderByCol).append(" ");
                                //      finalSqlNew += " union all select " + OraFactHint + pviewByColNew + " , " + porderByColNew + " ";//cbram
                                finalSqlNew.append(" union all select ").append(OraFactHint).append(pviewByColNew).append(" , ").append(porderByColNew).append(" ");

                            } else {
                                //     finalSql += " union all select " + OraFactHint + pviewByCol + " , " + porderByCol + " ";//cbram
                                finalSql.append(" union all select ").append(OraFactHint).append(pviewByCol).append(" , ").append(porderByCol).append(" ");
                                //   finalSqlNew += " union all select " + OraFactHint + pviewByColNew + " , " + porderByColNew + " ";//cbram
                                finalSqlNew.append(" union all select ").append(OraFactHint).append(pviewByColNew).append(" , ").append(porderByColNew).append(" ");
                            }

                        }

                        tabType = tabTypes.get(facts.get(i)).toString();
                        timeFactJoin = "";
                        if ((!avoidProGenTime || !forcedAvoidProGenTime) && qryType.equalsIgnoreCase("Trend")) {
                            if (!tabType.equalsIgnoreCase("Target Table")) {
                                if (!isCohort) {
                                    timeFactJoin += " on ( " + this.priorTimeClause + " ) ";
                                } else {
                                    timeFactJoin += " on ( PROGEN_TIME.ddate " + TimetableftClause.get(factandTimetable.get(facts.get(i).toString()).toString()).toString() + " ) ";
                                }


                            }
                        }

                        if ((!avoidProGenTime || !forcedAvoidProGenTime) && timeHaveDate[i].get(0).toString().equals("Y") && !isTypeDate) {
                            if (issummFact) {
                                ArrayList timeInparam = (ArrayList) TimetableOtherValues.get(factandTimetable.get(facts.get(i).toString()));
                                String timeQry = pbTime.summTableCalculation(timeInparam.get(0).toString(), timeInparam.get(1).toString(), summtrend, timeLevel, summFactLevel.get(facts.get(i).toString()).toString());


                                if (timeQry == null || "".equals(timeQry)) {
                                } else {
                                    timeFactJoin += " inner join (@@PROGEN_TIME_SUMM@@) summTab0001 ";
                                    timeFactJoin = timeFactJoin.replace("@@PROGEN_TIME_SUMM@@", timeQry);
                                    timeFactJoin += " on ( 1=1 " + timeClause[i].get(0) + " between summTab0001.T001_st_date and summTab0001.T001_end_date and " + summFactName.get(facts.get(i).toString()) + ".TIME_SUMM_FLAG = summTab0001.TIME_SUMM_FLAG " + " ) "; // TimetableCurrClause.get(factandTimetable.get(facts.get(i).toString()).toString()).toString();


                                }
                            }
                        }


                        if (tabType.equalsIgnoreCase("Target Table")) {

                            //          finalSql += pinsql[i] + factzqueries[i]+timeFactJoin+ " where 1=1  " + targetParamWhereClause + clause + factSecurityClause; //+ targetTableNamesParamClause.get(targetTabName);//targetParamWhereClause;//cbram
                            finalSql.append(pinsql[i]).append(factzqueries[i]).append(timeFactJoin).append(" where 1=1  ").append(targetParamWhereClause).append(clause).append(factSecurityClause);
                            //         finalSqlNew += pinsql[i] + factzqueries[i]+timeFactJoin+ " where 1=1  " + targetParamWhereClause + clause ; //+ targetTableNamesParamClause.get(targetTabName);//targetParamWhereClause;//cbram
                            finalSqlNew.append(pinsql[i]).append(factzqueries[i]).append(timeFactJoin).append(" where 1=1  ").append(targetParamWhereClause).append(clause);
                        } else {
                            //     finalSql += pinsql[i] +  "";//cbram
                            finalSql.append(pinsql[i]).append("");
                            //  finalSqlNew += pinsql[i] +  "";//cbram
                            finalSqlNew.append(pinsql[i]).append("");
                            {
                                String mTemp = factzqueries[i] + timeFactJoin + " where 1=1  " + paramWhereClause + factSecurityClause;
                                String mTempGO = factzqueries[i] + timeFactJoin + " where 1=1  " + paramWhereClause;
                                mTemp = getProgenTimeReplaces(userConnType, mTemp);
                                mTempGO = getProgenTimeReplaces(userConnType, mTemp);
                                //   finalSql += mTemp;//cbram
                                finalSql.append(mTemp);
                                //    finalSqlNew += mTempGO;//cbram
                                finalSqlNew.append(mTempGO);

                            }
                            if (typeDate.contains("Y") && !qryType.equalsIgnoreCase("Trend")) {
                                isTypeDate = true;
                            } else {
                            }
                        }

                        //modified on 11jan-10 susheela start
                        if (tabType.equalsIgnoreCase("Target Table")) {

                            if (timeHaveDate[i].get(0).toString().equals("Y")) {
                                //finalSql += "  and  " + targetTabName + "." + pbTime.p_tar_clu;
                                //      finalSql += timeClause[i].get(0) + TimetablePriorClause.get(factandTimetable.get(facts.get(i).toString()).toString()).toString();
                                finalSql.append(timeClause[i].get(0)).append(TimetablePriorClause.get(factandTimetable.get(facts.get(i).toString()).toString()).toString());
                                //       finalSqlNew += timeClause[i].get(0) + TimetablePriorClause.get(factandTimetable.get(facts.get(i).toString()).toString()).toString();//cbram
                                finalSqlNew.append(timeClause[i].get(0)).append(TimetablePriorClause.get(factandTimetable.get(facts.get(i).toString()).toString()).toString());
                            }
                        } else if (!avoidProGenTime && timeHaveDate[i].get(0).toString().equals("Y") && !isTypeDate) {
                            if (issummFact) {

                                ArrayList timeInparam = (ArrayList) TimetableOtherValues.get(factandTimetable.get(facts.get(i).toString()));
                                String timeQry = pbTime.summTableCalculation(timeInparam.get(2).toString(), timeInparam.get(3).toString(), summtrend, timeLevel, summFactLevel.get(facts.get(i).toString()).toString());
                                if (timeQry == null || "".equals(timeQry)) {
                                    String tempStr = finalSql.toString();
                                    tempStr = tempStr.replace(", (@@PROGEN_TIME_SUMM@@) summTab0001", "");
                                    StringBuilder strbld = new StringBuilder(tempStr);
                                    finalSql = strbld;
                                    //    finalSql += timeClause[i].get(0) + TimetablePriorClause.get(factandTimetable.get(facts.get(i).toString()).toString()).toString();//cbram
                                    finalSql.append(timeClause[i].get(0)).append(TimetablePriorClause.get(factandTimetable.get(facts.get(i).toString()).toString()).toString());
                                    String tempString = finalSqlNew.toString();
                                    tempString = tempString.replace(", (@@PROGEN_TIME_SUMM@@) summTab0001", "");
                                    StringBuilder str = new StringBuilder(tempString);
                                    finalSqlNew = str;
                                    //                finalSqlNew += timeClause[i].get(0) + TimetablePriorClause.get(factandTimetable.get(facts.get(i).toString()).toString()).toString();//cbram
                                    finalSqlNew.append(timeClause[i].get(0)).append(TimetablePriorClause.get(factandTimetable.get(facts.get(i).toString()).toString()).toString());
                                }
                                String tempStr = finalSql.toString();
                                tempStr = tempStr.replace("@@PROGEN_TIME_SUMM@@", timeQry);
                                StringBuilder strbld = new StringBuilder(tempStr);
                                finalSql = strbld;
                                //           finalSql += timeClause[i].get(0) + " between summTab0001.T001_st_date and summTab0001.T001_end_date and " + summFactName.get(facts.get(i).toString()) + ".TIME_SUMM_FLAG = summTab0001.TIME_SUMM_FLAG"; // TimetableCurrClause.get(factandTimetable.get(facts.get(i).toString()).toString()).toString();//cbram
                                finalSql.append(timeClause[i].get(0)).append(" between summTab0001.T001_st_date and summTab0001.T001_end_date and ").append(summFactName.get(facts.get(i).toString())).append(".TIME_SUMM_FLAG = summTab0001.TIME_SUMM_FLAG");
                                String tempString = finalSqlNew.toString();
                                tempString = tempString.replace("@@PROGEN_TIME_SUMM@@", timeQry);
                                StringBuilder str = new StringBuilder(tempString);
                                finalSqlNew = str;
                                //  finalSqlNew += timeClause[i].get(0) + " between summTab0001.T001_st_date and summTab0001.T001_end_date and " + summFactName.get(facts.get(i).toString()) + ".TIME_SUMM_FLAG = summTab0001.TIME_SUMM_FLAG"; // TimetableCurrClause.get(factandTimetable.get(facts.get(i).toString()).toString()).toString();//cbram
                                finalSqlNew.append(timeClause[i].get(0)).append(" between summTab0001.T001_st_date and summTab0001.T001_end_date and ").append(summFactName.get(facts.get(i).toString()));
                            } else {
                                if (facts.get(i) != null && factandTimetable.get(facts.get(i).toString()) != null && TimetablePriorClause.get(factandTimetable.get(facts.get(i).toString()).toString()) != null) //           finalSql += timeClause[i].get(0) + TimetablePriorClause.get(factandTimetable.get(facts.get(i).toString()).toString()).toString();//cbram
                                {
                                    finalSql.append(timeClause[i].get(0)).append(TimetablePriorClause.get(factandTimetable.get(facts.get(i).toString()).toString()).toString());
                                }
                                //    finalSqlNew += timeClause[i].get(0) + TimetablePriorClause.get(factandTimetable.get(facts.get(i).toString()).toString()).toString(); //cbram
                                finalSqlNew.append(timeClause[i].get(0)).append(TimetablePriorClause.get(factandTimetable.get(facts.get(i).toString()).toString()).toString());
                            }
                        } else if (isTypeDate) {
                            //     finalSql += getDesc(factandTimetable.get(facts.get(i).toString()).toString(),"P",clause,userConnType);
                            finalSql.append(getDesc(factandTimetable.get(facts.get(i).toString()).toString(), "P", clause, userConnType));
                            //        finalSqlNew += getDesc(factandTimetable.get(facts.get(i).toString()).toString(),"P",clause,userConnType);//cbram
                            finalSqlNew.append(getDesc(factandTimetable.get(facts.get(i).toString()).toString(), "P", clause, userConnType));
                        }

                        //   finalSql += getDesc(factandTimetable.get(facts.get(i).toString()).toString(),"P",clause,userConnType);//cbram
                        finalSql.append(getDesc(factandTimetable.get(facts.get(i).toString()).toString(), "P", clause, userConnType));
                        //   finalSqlNew += getDesc(factandTimetable.get(facts.get(i).toString()).toString(),"P",clause,userConnType);//cbram
                        finalSqlNew.append(getDesc(factandTimetable.get(facts.get(i).toString()).toString(), "P", clause, userConnType));

                        //modified on 11jan-10 susheela over
                        //added by susheela start 03-12-09
                        //     finalSql += filterClause;//cbram
                        finalSql.append(filterClause);
                        //   finalSqlNew += filterClause;//cbram
                        finalSqlNew.append(filterClause);
                        //added by susheela over 03-12-09


                        //////////////for target fact start

                        if (tabType.equalsIgnoreCase("Target Table")) {
                            if (!isKpi || isInnerViewBy) {
                                //     finalSql += " group by " + groupByColTarget;//cbram
                                finalSql.append(" group by ").append(groupByColTarget);
                                //    finalSqlNew += " group by " + groupByColTarget;  //cbram
                                finalSqlNew.append(" group by ").append(groupByColTarget);
                            }
                        } else {
                            if (!isKpi || isInnerViewBy) {
                                if (qryType.equalsIgnoreCase("Trend") || isDateTrueforNonDt) {
//
                                    //   finalSql += " group by " + pgroupByCol + insqlGroup[i];//cbram
                                    finalSql.append(" group by ").append(pgroupByCol).append(insqlGroup[i]);
                                    //          finalSqlNew += " group by " + pgroupByCol + insqlGroup[i];//cbram
                                    finalSqlNew.append(" group by ").append(pgroupByCol).append(insqlGroup[i]);
                                } else {
                                    // finalSql += " group by " + pgroupByCol + insqlGroup[i];
                                    // finalSqlNew += " group by " + pgroupByColNew + " , " + additionalgroupByCol + insqlGroup[i];
                                    // finalSql += " group by "  + pinsqlGrou[i];
                                    // finalSqlNew += " group by " +  pinsqlGrou[i];
                                    //     finalSql += " group by " + pgroupByCol + pinsqlGrou[i];//cbram
                                    finalSql.append(" group by ").append(pgroupByCol).append(pinsqlGrou[i]);
                                    //    finalSqlNew += " group by " + pgroupByColNew + " , " + additionalgroupByCol + pinsqlGrou[i];//cbram
                                    finalSqlNew.append(" group by ").append(pgroupByColNew).append(" , ").append(additionalgroupByCol).append(pinsqlGrou[i]);

                                }

                            }
                        }
                    }
                    //////////////for target fact
                }
            }
            //comment by mohit for multicalender
//            if(gbl_calander_var!=null && !gbl_calander_var.equalsIgnoreCase("null") && !gbl_calander_var.equalsIgnoreCase("")){
//                finalSql = finalSql.replace("PR_DAY_DENOM", gbl_calander_var);
//                finalSqlNew = finalSqlNew.replace("PR_DAY_DENOM", gbl_calander_var);
//            }
            ////////////////****************Code for time based formula ********///////////////////
            String[] a1 = (String[]) (timeTableCols.keySet()).toArray(new String[0]);
            //      String finalsql1 = new String();//cbram
            StringBuilder finalsql1 = new StringBuilder();
            //     String finalsql1New = new String();//cbram
            StringBuilder finalsql1New = new StringBuilder();
            for (int timeI = 0; timeI < a1.length; timeI++) {

                String tabId = String.valueOf(timeTableCols.get(a1[timeI]));

                int tablendex = Integer.parseInt(String.valueOf(timeTableIndex.get(tabId)));

                boolean issummFact = false;
                if (isSummFact != null && isSummFact.get(facts.get(tablendex)) != null) {
                    issummFact = true;
                }

                paramWhereClause = " ";
                boolean lastHasOr = false;
                ArrayList checkForParam = new ArrayList();
                if (factRelatedTablesMap != null && factRelatedTablesMap.size() > 0) {
                    if (factRelatedTablesMap.get(facts.get(tablendex).toString()) != null) {
                        checkForParam = factRelatedTablesMap.get(facts.get(tablendex).toString());
                        if (onlyParamWhereMap != null && onlyParamWhereMap.size() > 0) {
                            if (checkForParam != null && checkForParam.size() > 0) {
                                for (int processP = 0; processP < checkForParam.size(); processP++) {
                                    if (onlyParamWhereMap.get(checkForParam.get(processP).toString()) != null) {
                                        if (lastHasOr) {
                                            paramWhereClause = onlyParamWhereMap.get(checkForParam.get(processP).toString()) + paramWhereClause;
                                        } else {
                                            paramWhereClause += onlyParamWhereMap.get(checkForParam.get(processP).toString());
                                        }

                                        if (onlyParamWhereMap.get(checkForParam.get(processP).toString()).toString().toUpperCase().contains("OR ")) {
                                            lastHasOr = true;
                                        } else {
                                            lastHasOr = false;
                                        }

                                    }
                                }
                            }
                        }
                    }
                }
                paramWhereClause = " and ( 1=1 " + paramWhereClause;
                paramWhereClause += "  ) ";
                String factSecurityClause = "";
                factSecurityClause = getSecFactFilter(getUserId(), reportId, facts.get(tablendex).toString());
                factSecurityClause += factSummrizationTablesClause.get(facts.get(tablendex).toString());
                OraFactHint = "";
                if (userConnType.equalsIgnoreCase("ORACLE") && issummFact) {
                    OraFactHint = "/*+ PARALLEL(" + summFactName.get(facts.get(tablendex).toString()) + ",6) */ ";
                }
//                if(qryType.equalsIgnoreCase("Trend")){
//                    getTrendTime(clause,userConnType);
//                }
                String insql1 = String.valueOf(timeTableColInsql.get(a1[timeI]));
//                finalsql1 += " union all " + " select " + OraFactHint + viewByCol + " , " + orderByCol + insql1 + factzqueries[tablendex] + " " + paramWhereClause + clause;
                //     finalsql1 += " union all " + " select " + OraFactHint + viewByCol + " , " + orderByCol + insql1 +  "" ;//cbram
                finalsql1.append(" union all ").append(" select ").append(OraFactHint).append(viewByCol).append(" , ").append(orderByCol).append(insql1).append("");
                //finalsql1New += " union all " + " select " + OraFactHint + viewByColNew + " , " + additionalViewBy + " , " + orderByColNew + " , " + additionalOrderBy + insql1 +  "" ;//cbram
                finalsql1New.append(" union all ").append(" select ").append(OraFactHint).append(viewByColNew).append(" , ").append(additionalViewBy).append(" , ").append(orderByColNew).append(" , ").append(additionalOrderBy).append(insql1).append("");



                //if (timeHaveDate[i].get(0).toString().equals("Y")) {
                //finalSql += timeClause[i].get(0) + pbTime.d_clu;//Time clause commented uncomment later
                //    finalSql += timeClause[i].get(0) + TimetableCurrClause.get(factandTimetable.get(facts.get(i).toString()).toString()).toString() ;
                // }New time code
                String timeTabName = String.valueOf(timeTableCalTable.get(a1[timeI]));
                String timeAlternateDate = String.valueOf(timeTableDateTable.get(a1[timeI]));
                if (timeTabName.equals("NO_CAL_TABLE")) {
                    pbTime.timeTableName = factandTimetable.get(facts.get(tablendex).toString()).toString();
                } else {
                    pbTime.timeTableName = timeTabName;
                }



                if (!collectMulticalender.isEmpty()) {
                    ArrayList Multicalender = ((ArrayList) collectMulticalender.get((collectMulticalender.keySet().toArray()[0])));
                    pbTime.timeTableName = Multicalender.get(8).toString();
                } else if (gbl_calander_var != null && !gbl_calander_var.equalsIgnoreCase("null") && !gbl_calander_var.equalsIgnoreCase("")) {
                    pbTime.timeTableName = gbl_calander_var;
                } //added by mohit for multicalender
                String timeClu1 = timeClause[tablendex].get(0).toString();
                if (!timeAlternateDate.equals("NO_DATE_USED")) {
                    timeClu1 = " and " + timeAlternateDate;
                }
                if (timeTableDays.get(a1[timeI]).toString().contains("DIS") || (timeAlternateDate.equals("NO_DATE_USED"))) {
                    timeClu1 = getTruncDateClause(timeClu1, userConnType);
                }
                String tRange = pbTime.setRange(timeDetails, String.valueOf(timeTableDays.get(a1[timeI])));


                {
                    String mTemp = factzqueries[tablendex] + " where 1=1 " + paramWhereClause + factSecurityClause;
                    String mTempGO = factzqueries[tablendex] + factQueryEx + " where 1=1 " + paramWhereClause;
                    mTemp = getProgenTimeReplaces(userConnType, mTemp);
                    mTempGO = getProgenTimeReplaces(userConnType, mTempGO);
                    //       finalsql1 += mTemp;//cbram
                    finalsql1.append(mTemp);
                    //    finalsql1New += mTempGO; //cbram
                    finalsql1New.append(mTempGO);

                }

                if (!isTypeDate) {
                    //   finalsql1 +=  clause;//cbram
                    finalsql1.append(clause);
                    //finalsql1New +=  clause;//cbram
                    finalsql1New.append(clause);
                }
                if (issummFact) {

                    //    finalsql1 += " , (@@PROGEN_TIME_SUMM@@) summTab0001 ";//cbram
                    finalsql1.append(" , (@@PROGEN_TIME_SUMM@@) summTab0001 ");
                    //         finalsql1New += " , (@@PROGEN_TIME_SUMM@@) summTab0001 ";//cbram
                    finalsql1New.append(" , (@@PROGEN_TIME_SUMM@@) summTab0001 ");
                    String timeQry = pbTime.summTableCalculation(pbTime.st_d, pbTime.ed_d, summtrend, pbTime.rollingLevel, summFactLevel.get(facts.get(tablendex).toString()).toString());

                    if (timeQry == null || "".equals(timeQry)) {
                        String tempStr = finalsql1.toString();
                        tempStr = tempStr.replace(", (@@PROGEN_TIME_SUMM@@) summTab0001", "");
                        StringBuilder strbld = new StringBuilder(tempStr);
                        finalsql1 = strbld;
                        //     finalsql1 += timeClu1 + tRange;//cbram
                        finalsql1.append(timeClu1).append(tRange);
                        String tempString = finalSqlNew.toString();
                        tempString = tempString.replace(", (@@PROGEN_TIME_SUMM@@) summTab0001", "");
                        StringBuilder str = new StringBuilder(tempString);
                        finalsql1New = str;
                        //  finalsql1New += timeClu1 + tRange;//cbram
                        finalsql1New.append(timeClu1).append(tRange);

                    } else {
                        String tempStr = finalSql.toString();
                        tempStr = tempStr.replace(", (@@PROGEN_TIME_SUMM@@) summTab0001", "");
                        StringBuilder strbld = new StringBuilder(tempStr);
                        finalsql1 = strbld;
                        //   finalsql1 += timeClu1 + " between summTab0001.T001_st_date and summTab0001.T001_end_date and " + summFactName.get(facts.get(tablendex).toString()) + ".TIME_SUMM_FLAG = summTab0001.TIME_SUMM_FLAG"; // TimetableCurrClause.get(factandTimetable.get(facts.get(i).toString()).toString()).toString();//cbram
                        finalsql1.append(timeClu1).append(" between summTab0001.T001_st_date and summTab0001.T001_end_date and ").append(summFactName.get(facts.get(tablendex).toString())).append(".TIME_SUMM_FLAG = summTab0001.TIME_SUMM_FLAG");
                        String tempString = finalSqlNew.toString();
                        tempString = tempString.replace("@@PROGEN_TIME_SUMM@@", timeQry);
                        StringBuilder str = new StringBuilder(tempString);
                        finalsql1New = str;

                        //                 finalsql1New += timeClu1 + " between summTab0001.T001_st_date and summTab0001.T001_end_date and " + summFactName.get(facts.get(tablendex).toString()) + ".TIME_SUMM_FLAG = summTab0001.TIME_SUMM_FLAG"; // TimetableCurrClause.get(factandTimetable.get(facts.get(i).toString()).toString()).toString();//cbram
                        finalsql1New.append(timeClu1).append(" between summTab0001.T001_st_date and summTab0001.T001_end_date and ").append(summFactName.get(facts.get(tablendex).toString())).append(".TIME_SUMM_FLAG = summTab0001.TIME_SUMM_FLAG");
                    }
                } else if (isTypeDate) {
                    //  finalsql1 += getDesc("@@TIME_BASED_FORMULA@@","C",clause,userConnType);//cbram
                    finalsql1.append(getDesc("@@TIME_BASED_FORMULA@@", "C", clause, userConnType));
                    //        finalsql1New += getDesc("@@TIME_BASED_FORMULA@@","C",clause,userConnType); //cbram
                    finalsql1New.append(getDesc("@@TIME_BASED_FORMULA@@", "C", clause, userConnType));
                } else {
                    //   finalsql1 += timeClu1 + tRange;//cbram
                    finalsql1.append(timeClu1).append(tRange);
                    // finalsql1New += timeClu1 + tRange;//cbram
                    finalsql1New.append(timeClu1).append(tRange);
                }

                //           finalsql1 += getDesc("@@TIME_BASED_FORMULA@@","C",clause,userConnType);//cbram
                finalsql1.append(getDesc("@@TIME_BASED_FORMULA@@", "C", clause, userConnType));
                //    finalsql1New += getDesc("@@TIME_BASED_FORMULA@@","C",clause,userConnType);//cbram
                finalsql1New.append(getDesc("@@TIME_BASED_FORMULA@@", "C", clause, userConnType));

                //     finalsql1 += filterClause;//cbram
                finalsql1.append(filterClause);
                //    finalsql1New += filterClause; //cbram
                finalsql1New.append(filterClause);
                if (!isKpi || isInnerViewBy) {
                    //     finalsql1 += " group by " + groupByCol + insqlGroup[tablendex];//cbram
                    finalsql1.append(" group by ").append(groupByCol).append(insqlGroup[tablendex]);
                    //     finalsql1New += " group by " + groupByCol + " , " + additionalgroupByCol + insqlGroup[tablendex];//cbram
                    finalsql1New.append(" group by ").append(groupByCol).append(" , ").append(additionalgroupByCol).append(insqlGroup[tablendex]);
                } else if (isUngroupedMeasure) {
                    if (insqlGroup[tablendex].startsWith(",") || insqlGroup[tablendex].startsWith(" ,")) {
                        insqlGroup[tablendex] = insqlGroup[tablendex].substring(insqlGroup[tablendex].indexOf(",") + 1);
                    }
                    //	 finalsql1 += " group by "  + insqlGroup[tablendex];//cbram
                    finalsql1.append(" group by ").append(insqlGroup[tablendex]);
                    //     finalsql1New += " group by " + insqlGroup[tablendex];//cbram
                    finalsql1New.append(" group by ").append(insqlGroup[tablendex]);
                }


            }
            //    finalSql += finalsql1;//cbram
            finalSql.append(finalsql1);
            //  finalSqlNew += finalsql1New;//cbram
            finalSqlNew.append(finalsql1New);

            String oWrapper = "";
            if (isKpi) {
                //   if(osqlGroup.startsWith(" ,") || osqlGroup.startsWith(","))//cbram
                if (osqlGroup.toString().startsWith(" ,") || osqlGroup.toString().startsWith(",")) {
                    //       osqlGroup=osqlGroup.substring(osqlGroup.indexOf(",")+1);//cbram
                    String tempString = osqlGroup.toString().substring(osqlGroup.toString().indexOf(",") + 1);
                    StringBuilder str = new StringBuilder(tempString);
                    osqlGroup = str;
                }
                if (isUngroupedMeasure) {
                    //             finalSql = " select '" + kpiVal + "' A1 " + osql + " from ( " + finalSql + " ) O3  group by " + osqlGroup;//cbram
                    StringBuilder str1 = new StringBuilder();
                    str1.append(" select '").append(kpiVal).append("' A1 ").append(osql).append(" from ( ").append(finalSql).append(" ) O3  group by ").append(osqlGroup);
                    //             finalSql = " select A1 " + omsql + " from ( " + finalSql + " ) O4  " ;//cbram
                    StringBuilder str2 = new StringBuilder();
                    str2.append(" select A1 ").append(omsql).append(" from ( ").append(str1).append(" ) O4  ");
                    finalSql = str2;
                    //     finalSqlNew = " select '" + kpiVal + "' A1 " + osql + " from ( " + finalSqlNew + " ) O3  group by " + osqlGroup ;//cbram
                    StringBuilder str3 = new StringBuilder();
                    str3.append(" select '").append(kpiVal).append("' A1 ").append(osql).append(" from ( ").append(finalSqlNew).append(" ) O3  group by ").append(osqlGroup);
                    //      finalSqlNew = " select A1 " + omsql + " from ( " + finalSqlNew + " ) O4 ";//cbram
                    StringBuilder str4 = new StringBuilder();
                    str4.append(" select A1 ").append(omsql).append(" from ( ").append(str3).append(" ) O4 ");
                    finalSqlNew = str4;
                } else {
                    //         finalSql = " select '" + kpiVal + "' A1 " + osql + " from ( " + finalSql + " ) O3  ";//cbram
                    StringBuilder str1 = new StringBuilder();
                    str1.append(" select '").append(kpiVal).append("' A1 ").append(osql).append(" from ( ").append(finalSql).append(" ) O3  ");
                    //       finalSql = " select A1 " + omsql + " from ( " + finalSql + " ) O4 ";//cbram
                    StringBuilder str2 = new StringBuilder();
                    str2.append(" select A1 ").append(osql).append(" from ( ").append(str1).append(" ) O4 ");
                    finalSql = str2;
                    //  finalSqlNew = " select '" + kpiVal + "' A1 " + osql + " from ( " + finalSqlNew + " ) O3  ";//cbram
                    StringBuilder str3 = new StringBuilder();
                    str3.append(" select '").append(kpiVal).append("' A1 ").append(osql).append(" from ( ").append(finalSqlNew).append(" ) O3  ");
                    //   finalSqlNew = " select A1 " + omsql + " from ( " + finalSqlNew + " ) O4 ";//cbram
                    StringBuilder str4 = new StringBuilder();
                    str4.append(" select A1 ").append(omsql).append(" from ( ").append(str3).append(" ) O4 ");
                    finalSqlNew = str4;
                }
            } else {
                if (qryColumns != null) {
                    for (int looper = 0; looper < qryColumns.size(); looper++) {
                        oWrapper += " , A_" + qryColumns.get(looper).toString().replace(".0", "") + " as " + "  \"A_" + qryColumns.get(looper).toString().replace(".0", "") + "\" ";
                    }

                }

                //     osql_AO = osql;//cbram
                osql_AO = osql.toString();
                OViewByCol_AO = OViewByColNew;
                OViewByColGrp_AO = OViewByColNewGrp;
                OorderByCol_AO = OorderByColNew;
                //        finalSql_AO = finalSql;//cbram
                finalSql_AO = finalSql.toString();
//                OViewByCol_AO = OViewByCol;
                OmViewByCol_AO = OmViewByCol;
                OmorderByCol_AO = OmorderByColNew;
                ColOrderByCol_AO = ColOrderByColNew;
                //          omsql_AO = omsql;//cbram
                omsql_AO = omsql.toString();
                osqlGroup_AO = osqlGroup.toString();
                finalViewByCol_AO = finalViewByCol;
                oWrapper_AO = oWrapper;
                //         finalSqlNew_AO = finalSqlNew;//cbram
                finalSqlNew_AO = finalSqlNew.toString();
                startDate_AO = pbTime.st_d;
                endDate_AO = pbTime.ed_d;
                timeLevel_AO = timeDetails.get(0).toString();
                // main inner sql to be saved as AO or GO tables in DB
                replaceSpecialCharsAccToDb(userConnType);
                fullQuery = finalSql;//added by sruthi to display the full query
                ReportObjectQuery repObjQuery = new ReportObjectQuery();
                repObjQuery.reportQryElementIds = this.reportQryElementIds;
                File datafile;
                if (isAOReport || aoTableId != null) {
                    datafile = new File(this.filePath + "/analyticalobject/M_AO_" + aoTableId + ".json");
                } else {
                    datafile = new File(this.filePath + "/analyticalobject/R_AO_" + reportId + ".json");
                }
                if (datafile.exists()) {
                    isAOEnable = true;
                } else {
                    isAOEnable = false;
                }
                if (isAOEnable) {
                    finalSqlAO = getFinalSqlAOQuery(repObjQuery);
//                    String filterClauses = "";
//                    filterClauses = repObjQuery.getFilterClause(inMaps, notInMaps, likeMaps, notLikeMaps);
//                    setDataToObjectQuery();
//                                    if(isAOReport)
//                   finalSqlAO = repObjQuery.getObjectQueryForAOReport(aoTableId,filterClauses,rowViewbyCols,ColViewbyCols,startDate_AO,endDate_AO,timeLevel_AO);
//                                    else
//                            finalSqlAO = repObjQuery.getObjectQueryAO(reportId,filterClauses,rowViewbyCols,ColViewbyCols,startDate_AO,endDate_AO,timeLevel_AO);
                    if (!finalSqlAO.equalsIgnoreCase("") && !finalSqlAO.equalsIgnoreCase("noData")) {
                        //    finalSql = finalSqlAO ;//cbram
                        String tempStr = finalSqlAO;
                        StringBuilder strbld = new StringBuilder(tempStr);
                        finalSql = strbld;
                    } else {
                        isAOEnable = false;
                    }
                }
                if (isDimSegOnSumm && isQueryForGO) {
                    finalSqlSummarized_AO = finalSqlSummarized;
                    finalSummarizedGrpBy_AO = finalSummarizedGrpBy;
                }
                if (!isAOEnable || finalSqlAO.equalsIgnoreCase("") || finalSqlAO.equalsIgnoreCase("noData")) {
                    isAOEnable = false;
                    String OViewByColNew1 = OViewByCol;
                    String OorderByColNew1 = OorderByCol;
                    OViewByColNew1 = replaceUnwantedCharsOViewByCol(OViewByColNew1);
                    OorderByColNew1 = replaceUnwantedCharsOorderByCol(OorderByColNew1);

                    if (getColViewbyCols() != null && getColViewbyCols().size() > 0) {
                        StringBuilder newStr = new StringBuilder();
                        if (isDimSegOnSumm) {
                            //            finalSql = " select " + finalSqlSummarized +" FROM ( " + finalSql + " ) SGP group by " + finalSummarizedGrpBy +" " ;//cbram

                            newStr.append(" select ").append(finalSqlSummarized).append(" FROM ( ").append(finalSql).append(" ) SGP group by ").append(finalSummarizedGrpBy).append(" ");
                            finalSql = newStr;
                            //finalSql = " select " + OViewByCol + " , " + OorderByCol + osql + " from ( " + finalSql + " ) O5 group by " + OViewByColNew1 + " , " + OorderByColNew1 + " " + osqlGroup + " ";//cbram
                            StringBuilder newStr1 = new StringBuilder();
                            newStr1.append(" select ").append(OViewByCol).append(" , ").append(OorderByCol).append(osql).append(" from ( ").append(finalSql).append(" ) O5 group by ").append(OViewByColNew1).append(" , ").append(OorderByColNew1).append(" ").append(osqlGroup).append(" ");
                            finalSql = newStr1;
                        } else {
                            //         finalSql = " select " + OViewByCol + " , " + OorderByCol + osql + " from ( " + finalSql + " ) O5 group by " + OViewByCol + " , " + OorderByCol + " " + osqlGroup + " ";//cbram

                            newStr.append(" select ").append(OViewByCol).append(" , ").append(OorderByCol).append(osql).append(" from ( ").append(finalSql).append(" ) O5 group by ").append(OViewByCol).append(" , ").append(OorderByCol).append(" ").append(osqlGroup).append(" ");
                            finalSql = newStr;
                        }
                        //           finalSql = " select " + OmViewByCol + " , " + OmorderByCol + " , " + ColOrderByCol + omsql + " from ( " + finalSql + " ) O6  ";//cbram
                        StringBuilder newStr1 = new StringBuilder();
                        newStr1.append(" select ").append(OmViewByCol).append(" , ").append(OmorderByCol).append(" , ").append(ColOrderByCol).append(omsql).append(" from ( ").append(finalSql).append(" ) O6  ");
                        finalSql = newStr1;
                    } else {
                        StringBuilder newStr = new StringBuilder();
                        if (isDimSegOnSumm) {
                            //              finalSql = " select " + finalSqlSummarized +"  FROM ( " + finalSql + " ) SGP group by " + finalSummarizedGrpBy +" " ;//cbram
                            newStr.append(" select ").append(finalSqlSummarized).append("  FROM ( ").append(finalSql).append(" ) SGP group by ").append(finalSummarizedGrpBy).append(" ");
                            finalSql = newStr;
                            //            finalSql = " select " + OViewByCol + " , " + OorderByCol + osql + " from ( " + finalSql + " ) O7  group by " + OViewByColNew1 + " , " + OorderByColNew1 + " " + osqlGroup + "  ";// //+ " order by " + OmorderByCol;//cbram
                            StringBuilder newStr1 = new StringBuilder();
                            newStr1.append(" select ").append(OViewByCol).append(" , ").append(OorderByCol).append(osql).append(" from ( ").append(finalSql).append(" ) O7  group by ").append(OViewByColNew1).append(" , ").append(OorderByColNew1).append(" ").append(osqlGroup).append("  ");
                            finalSql = newStr1;
                        } else {
                            //   finalSql = " select " + OViewByCol + " , " + OorderByCol + osql + " from ( " + finalSql + " ) O7  group by " + OViewByCol + " , " + OorderByCol + " " + osqlGroup + "  ";// //+ " order by " + OmorderByCol;//cbram
                            newStr.append(" select ").append(OViewByCol).append(" , ").append(OorderByCol).append(osql).append(" from ( ").append(finalSql).append(" ) O7  group by ").append(OViewByCol).append(" , ").append(OorderByCol).append(" ").append(osqlGroup).append("  ");
                            finalSql = newStr;
                        }

                        //           finalSql = " select " + OmViewByCol + " , " + OmorderByCol + omsql + " from ( " + finalSql + " ) O7_1 ";//cbram
                        StringBuilder finalSql1 = new StringBuilder();
                        finalSql1.append(" select ").append(OmViewByCol).append(" , ").append(OmorderByCol).append(omsql).append(" from ( ").append(finalSql).append(" ) O7_1 ");
                        finalSql = finalSql1;
                    }


                    if (!(getColViewbyCols() != null && getColViewbyCols().size() > 0)) {
                        if (getRowViewbyCols() != null && getRowViewbyCols().size() > 0) {
                            //                 finalSql = " select " + finalViewByCol + oWrapper + " from ( " + finalSql + " ) OT1 order by  " + OmorderByCol;//cbram

                            StringBuilder finalSql1 = new StringBuilder();
                            finalSql1.append(" select ").append(finalViewByCol).append(oWrapper).append(" from ( ").append(finalSql).append(" ) OT1 order by  ").append(OmorderByCol);
                            finalSql = finalSql1;
                        } else if (orderByCol.contains("PRG_DAY_DETAILS")) {
                            //             finalSql = " select " + finalViewByCol + finalViewByCol + " from ( " + finalSql + " ) OT1 order by  " + OmorderByCol;//cbram
                            StringBuilder queryStr = new StringBuilder();
                            queryStr.append(" select ").append(finalViewByCol).append(finalViewByCol).append(" from ( ").append(finalSql).append(" ) OT1 order by  ").append(OmorderByCol);
                            finalSql = queryStr;
                        } else {
                            if (defaultSortedColumn != null && !"".equalsIgnoreCase(defaultSortedColumn)) {
                                StringBuilder queryStr = new StringBuilder();
                                if (!bucketInnerTablesql.equalsIgnoreCase("")) {
                                    //                      finalSql = " select " + finalViewByCol + oWrapper + " from ( " + finalSql + " ) O8 ";//cbram
                                    queryStr.append(" select ").append(finalViewByCol).append(oWrapper).append(" from ( ").append(finalSql).append(" ) O8 ");
                                    finalSql = queryStr;
                                } else {
                                    //                finalSql = " select " + finalViewByCol + oWrapper + " from ( " + finalSql + " ) O9 order by " + defaultSortedColumn + " desc ";//cbram
                                    queryStr.append(" select ").append(finalViewByCol).append(oWrapper).append(" from ( ").append(finalSql).append(" ) O9 order by ").append(defaultSortedColumn).append(" desc ");
                                    finalSql = queryStr;
                                }

                            } else {
                                StringBuilder queryStr = new StringBuilder();
                                if (!bucketInnerTablesql.equalsIgnoreCase("")) {
                                    //              finalSql = " select " + finalViewByCol + oWrapper + " from ( " + finalSql + " ) O10  ";//cbram
                                    queryStr.append(" select ").append(finalViewByCol).append(oWrapper).append(" from ( ").append(finalSql).append(" ) O10  ");
                                    finalSql = queryStr;
                                } else {
                                    if (ParameterGroupAnalysisHashMap != null && ParameterGroupAnalysisHashMap.size() != 0) {
                                        //finalSql = " select " + finalViewByCol + "," + OmorderByCol + oWrapper + " from ( " + finalSql + " ) O11 "; //order by " + (getRowViewbyCols().size() + 1) + " desc//cbram
                                        queryStr.append(" select ").append(finalViewByCol).append(",").append(OmorderByCol).append(oWrapper).append(" from ( ").append(finalSql).append(" ) O11 ");
                                        finalSql = queryStr;
                                    } else {
                                        //                finalSql = " select " + finalViewByCol + oWrapper + " from ( " + finalSql + " ) O11 "; //order by " + (getRowViewbyCols().size() + 1) + " desc//cbram
                                        queryStr.append(" select ").append(finalViewByCol).append(oWrapper).append(" from ( ").append(finalSql).append(" ) O11 ");
                                        finalSql = queryStr;
                                    }
                                }
                            }
                        }

                    }
                }
                if (isAOEnable) //added by sruthi to display full query
                {
                    String OViewByColNew1 = OViewByCol;
                    String OorderByColNew1 = OorderByCol;
                    OViewByColNew1 = replaceUnwantedCharsOViewByCol(OViewByColNew1);
                    OorderByColNew1 = replaceUnwantedCharsOorderByCol(OorderByColNew1);

                    if (getColViewbyCols() != null && getColViewbyCols().size() > 0) {
                        StringBuilder strQuery = new StringBuilder();
                        if (isDimSegOnSumm) {
                            //                fullQuery = " select " + finalSqlSummarized +" FROM ( " + fullQuery + " ) SGP group by " + finalSummarizedGrpBy +" " ;//cbram
                            strQuery.append(" select ").append(finalSqlSummarized).append(" FROM ( ").append(fullQuery).append(" ) SGP group by ").append(finalSummarizedGrpBy).append(" ");
                            //                 fullQuery = " select " + OViewByCol + " , " + OorderByCol + osql + " from ( " + fullQuery + " ) O5 group by " + OViewByColNew1 + " , " + OorderByColNew1 + " " + osqlGroup + " ";//cbram
                            StringBuilder strQuery1 = new StringBuilder();
                            strQuery1.append(" select ").append(OViewByCol).append(" , ").append(OorderByCol).append(osql).append(" from ( ").append(strQuery).append(" ) O5 group by ").append(OViewByColNew1).append(" , ").append(OorderByColNew1).append(" ").append(osqlGroup).append(" ");
                            fullQuery = strQuery1;
                        } else {
                            //               fullQuery = " select " + OViewByCol + " , " + OorderByCol + osql + " from ( " + fullQuery + " ) O5 group by " + OViewByCol + " , " + OorderByCol + " " + osqlGroup + " ";//cbram
                            strQuery.append(" select ").append(OViewByCol).append(" , ").append(OorderByCol).append(osql).append(" from ( ").append(fullQuery).append(" ) O5 group by ").append(OViewByCol).append(" , ").append(OorderByCol).append(" ").append(osqlGroup).append(" ");
                            fullQuery = strQuery;
                        }
                        //            fullQuery = " select " + OmViewByCol + " , " + OmorderByCol + " , " + ColOrderByCol + omsql + " from ( " + fullQuery + " ) O6  ";//cbram
                        StringBuilder strQuery1 = new StringBuilder();
                        strQuery1.append(" select ").append(OmViewByCol).append(" , ").append(OmorderByCol).append(" , ").append(ColOrderByCol).append(omsql).append(" from ( ").append(fullQuery).append(" ) O6  ");
                        fullQuery = strQuery1;
                    } else {
                        StringBuilder strQuery = new StringBuilder();
                        if (isDimSegOnSumm) {
                            //                fullQuery = " select " + finalSqlSummarized +"  FROM ( " + fullQuery + " ) SGP group by " + finalSummarizedGrpBy +" " ;//cbram
                            strQuery.append(" select ").append(finalSqlSummarized).append("  FROM ( ").append(fullQuery).append(" ) SGP group by ").append(finalSummarizedGrpBy).append(" ");
                            //               fullQuery = " select " + OViewByCol + " , " + OorderByCol + osql + " from ( " + fullQuery + " ) O7  group by " + OViewByColNew1 + " , " + OorderByColNew1 + " " + osqlGroup + "  ";// //+ " order by " + OmorderByCol;//cbram
                            StringBuilder strQuery1 = new StringBuilder();
                            strQuery1.append(" select ").append(OViewByCol).append(" , ").append(OorderByCol).append(osql).append(" from ( ").append(strQuery).append(" ) O7  group by ").append(OViewByColNew1).append(" , ").append(OorderByColNew1).append(" ").append(osqlGroup).append("  ");
                            fullQuery = strQuery1;
                        } else {

                            //            fullQuery = " select " + OViewByCol + " , " + OorderByCol + osql + " from ( " + fullQuery + " ) O7  group by " + OViewByCol + " , " + OorderByCol + " " + osqlGroup + "  ";// //+ " order by " + OmorderByCol;//cbram
                            strQuery.append(" select ").append(OViewByCol).append(" , ").append(OorderByCol).append(osql).append(" from ( ").append(fullQuery).append(" ) O7  group by ").append(OViewByCol).append(" , ").append(OorderByCol).append(" ").append(osqlGroup).append("  ");
                            fullQuery = strQuery;
                        }

                        //         fullQuery = " select " + OmViewByCol + " , " + OmorderByCol + omsql + " from ( " + fullQuery + " ) O7_1 ";//cbram
                        StringBuilder strQuery1 = new StringBuilder();
                        strQuery1.append(" select ").append(OmViewByCol).append(" , ").append(OmorderByCol).append(omsql).append(" from ( ").append(fullQuery).append(" ) O7_1 ");
                        fullQuery = strQuery1;
                    }
                    if (!(getColViewbyCols() != null && getColViewbyCols().size() > 0)) {
                        if (getRowViewbyCols() != null && getRowViewbyCols().size() > 0) {
                            //           fullQuery = " select " + finalViewByCol + oWrapper + " from ( " + fullQuery + " ) OT1 order by  " + OmorderByCol;//cbram
                            StringBuilder strQuery = new StringBuilder();
                            strQuery.append(" select ").append(finalViewByCol).append(oWrapper).append(" from ( ").append(fullQuery).append(" ) OT1 order by  ").append(OmorderByCol);
                            fullQuery = strQuery;
                        } else if (orderByCol.contains("PRG_DAY_DETAILS")) {
                            //            fullQuery = " select " + finalViewByCol + oWrapper + " from ( " + fullQuery + " ) OT1 order by  " + OmorderByCol;//cbram
                            StringBuilder strQuery = new StringBuilder();
                            strQuery.append(" select ").append(finalViewByCol).append(oWrapper).append(" from ( ").append(fullQuery).append(" ) OT1 order by  ").append(OmorderByCol);
                            fullQuery = strQuery;
                        } else {
                            if (defaultSortedColumn != null && !"".equalsIgnoreCase(defaultSortedColumn)) {
                                StringBuilder strQuery = new StringBuilder();
                                if (!bucketInnerTablesql.equalsIgnoreCase("")) {
                                    //      fullQuery = " select " + finalViewByCol + oWrapper + " from ( " + fullQuery + " ) O8 ";//cbram
                                    strQuery.append(" select ").append(finalViewByCol).append(oWrapper).append(" from ( ").append(fullQuery).append(" ) O8 ");
                                    fullQuery = strQuery;
                                } else {
                                    //           fullQuery = " select " + finalViewByCol + oWrapper + " from ( " + fullQuery + " ) O9 order by " + defaultSortedColumn + " desc ";
                                    strQuery.append(" select ").append(finalViewByCol).append(oWrapper).append(" from ( ").append(fullQuery).append(" ) O9 order by ").append(defaultSortedColumn).append(" desc ");
                                    fullQuery = strQuery;
                                }

                            } else {
                                StringBuilder strQuery = new StringBuilder();
                                if (!bucketInnerTablesql.equalsIgnoreCase("")) {
                                    //              fullQuery = " select " + finalViewByCol + oWrapper + " from ( " + fullQuery + " ) O10  ";//cbram
                                    strQuery.append(" select ").append(finalViewByCol).append(oWrapper).append(" from ( ").append(fullQuery).append(" ) O10  ");
                                    fullQuery = strQuery;
                                } else {
                                    if (ParameterGroupAnalysisHashMap != null && ParameterGroupAnalysisHashMap.size() != 0) {
                                        //                fullQuery = " select " + finalViewByCol + "," + OmorderByCol + oWrapper + " from ( " + fullQuery + " ) O11 "; //order by " + (getRowViewbyCols().size() + 1) + " desc//cbram
                                        strQuery.append(" select ").append(finalViewByCol).append(",").append(OmorderByCol).append(oWrapper).append(" from ( ").append(fullQuery).append(" ) O11 ");
                                        fullQuery = strQuery;

                                    } else {
                                        //            fullQuery = " select " + finalViewByCol + oWrapper + " from ( " + fullQuery + " ) O11 "; //order by " + (getRowViewbyCols().size() + 1) + " desc//cbram
                                        strQuery.append(" select ").append(finalViewByCol).append(oWrapper).append(" from ( ").append(fullQuery).append(" ) O11 ");
                                        fullQuery = strQuery;
                                    }
                                }
                            }
                        }

                    }
                }//ended by sruthi
            }
        }

        if (!CrossTab) {
            if (ParameterGroupAnalysisHashMap != null && ParameterGroupAnalysisHashMap.size() != 0) {
                String finalViewByColarr[] = finalViewByCol.split(",");
                String totoueterwraper = "";
                String grpoWrapper = "";

                for (int j = 0; j < finalViewByColarr.length; j++) {
                    String grpViewBycol = finalViewByColarr[j].replaceAll("\"", " ");
                    grpViewBycol = grpViewBycol.trim();
                    grpViewBycol = grpViewBycol.substring(2);
                    HashMap viewgrpMap = (HashMap) ParameterGroupAnalysisHashMap.get(grpViewBycol);
                    int count = 0;

                    if (viewgrpMap != null) {
                        Set s = viewgrpMap.keySet();
                        String keys[] = (String[]) s.toArray(new String[0]);
                        String grpcase = " case ";
                        for (int i = 0; i < keys.length; i++) {
                            if (!keys[i].equalsIgnoreCase("others")) {
                                grpcase += " when " + finalViewByColarr[j] + " in( " + String.valueOf(viewgrpMap.get(keys[i])) + ") then '" + keys[i] + "'";
                            }
                        }
                        grpcase += " else 'others' end as A_" + grpViewBycol + "_G ";
                        if (count == 0) {
                            for (int looper = 0; looper < qryColumns.size(); looper++) {
                                grpoWrapper += " , A_" + qryColumns.get(looper).toString();
                                count = 1;
                            }
                        }
                        if (!grpcase.equalsIgnoreCase("")) {
                            totoueterwraper += "," + grpcase;
                        }
                    }
                }
                if (!totoueterwraper.equalsIgnoreCase("")) {
                    totoueterwraper = totoueterwraper.substring(1);
                    StringBuilder queryStr = new StringBuilder();
                    if (!(getColViewbyCols() != null && getColViewbyCols().size() > 0)) {
                        if (isAOEnable) {
                            //          fullQuery = " select " + finalViewByCol + grpoWrapper + " ," + totoueterwraper + " from (" + fullQuery + ") O13 ";//cbram
                            StringBuilder str = new StringBuilder();
                            str.append(" select ").append(finalViewByCol).append(grpoWrapper).append(" ,").append(totoueterwraper).append(" from (").append(fullQuery).append(") O13 ");
                            fullQuery = str;
                        }
                        //              finalSql = " select " + finalViewByCol + grpoWrapper + " ," + totoueterwraper + " from (" + finalSql + ") O13 ";//cbram
                        queryStr.append(" select ").append(finalViewByCol).append(grpoWrapper).append(" ,").append(totoueterwraper).append(" from (").append(finalSql).append(") O13 ");
                        finalSql = queryStr;
                    } else {
                        if (isAOEnable) {//added by sruthi to display full query
                            //             fullQuery = " select " + finalViewByCol + grpoWrapper + " ," + totoueterwraper + ", " + OmorderByCol + " from (" + fullQuery + ") O13 ";//cbram
                            StringBuilder str = new StringBuilder();
                            str.append(" select ").append(finalViewByCol).append(grpoWrapper).append(" ,").append(totoueterwraper).append(", ").append(OmorderByCol).append(" from (").append(fullQuery).append(") O13 ");
                            finalSql = str;
                        }
                        //              finalSql = " select " + finalViewByCol + grpoWrapper + " ," + totoueterwraper + ", " + OmorderByCol + " from (" + finalSql + ") O13 ";//cbram
                        queryStr.append(" select ").append(finalViewByCol).append(grpoWrapper).append(" ,").append(totoueterwraper).append(", ").append(OmorderByCol).append(" from (").append(finalSql).append(") O13 ");
                        finalSql = queryStr;
                    }
                }
            }
        }


        if (isBucketReplace) {
            String finalViewByColarr[] = finalViewByCol.split(",");
            finalViewByCol = "";
            for (int j = 0; j < finalViewByColarr.length; j++) {
                if (Integer.parseInt(bucketReplaceIndex) == j) {
                    finalViewByCol += "," + bucketReplaceQry;
                } else {
                    finalViewByCol += "," + finalViewByCol;
                }
                if (j == (finalViewByColarr.length - 1)) {
                    if (finalViewByCol.startsWith(",")) {
                        finalViewByCol = finalViewByCol.substring(1);
                    }
                }
            }

            for (int looper = 0; looper < qryColumns.size(); looper++) {
                finalViewByCol += " , A_" + qryColumns.get(looper).toString() + " as \"A_" + qryColumns.get(looper).toString() + "\" ";

            }
            if (isAOEnable) {//added by sruthi to display full query
                //          fullQuery = "select " + finalViewByCol + " from (" + fullQuery + ") O12 ";//cbram
                StringBuilder str = new StringBuilder();
                str.append("select ").append(finalViewByCol).append(" from (").append(fullQuery).append(") O12 ");
                fullQuery = str;
            }
            //   finalSql = "select " + finalViewByCol + " from (" + finalSql + ") O12 ";//cbram
            StringBuilder queryStr = new StringBuilder();
            queryStr.append("select ").append(finalViewByCol).append(" from (").append(finalSql).append(") O12 ");
            finalSql = queryStr;
        }


        //Amit putiing new cross tab code
        if (getColViewbyCols() != null && getColViewbyCols().size() > 0) {
            if (String.valueOf(getColViewbyCols().get(0)).equalsIgnoreCase("Time")) {
                if (timeDetails.get(0).toString().equalsIgnoreCase("Day") && (timeDetails.get(1).toString().equalsIgnoreCase("PRG_STD")
                        || timeDetails.get(1).toString().equalsIgnoreCase("PRG_COHORT"))) {
                    if (timeDetails.get(1).toString().equalsIgnoreCase("PRG_COHORT")) {
                        colViewQuery = pbTime.getTimeViewbyQuery(timeDetails.get(0).toString(), timeDetails.get(6).toString(), "PERIOD", timeDetails.get(2).toString());
                    } else {
                        colViewQuery = pbTime.getTimeViewbyQuery(timeDetails.get(0).toString(), timeDetails.get(3).toString(), timeDetails.get(4).toString(), timeDetails.get(2).toString());
                    }

                } else { //Need changes for day level
                    colViewQuery = pbTime.getTimeViewbyQuery(timeDetails);
                }
                //  cbt.setIsheaderTrend(true);
            }
            if (isAOEnable) {  //added by sruthi to display full query
                //   fullQuery =  fullQuery + " order by " +ColOrderByCol;//cbram
                StringBuilder str = new StringBuilder();
                str.append(fullQuery).append(" order by ").append(ColOrderByCol);
                fullQuery = str;
            } //ended by sruthi
            //       finalSql =  finalSql + " order by " +ColOrderByCol;//cbram
            StringBuilder queryStr = new StringBuilder();
            queryStr.append(finalSql).append(" order by ").append(ColOrderByCol);
            finalSql = queryStr;
        }
//finalSql = finalSql.replace(".QTR_YEAR_INT", ".QRT_YEAR_INT"); //by Nazneen on 18th April 2015//cbram
        String tempStr = finalSql.toString();
        tempStr = tempStr.replace(".QTR_YEAR_INT", ".QRT_YEAR_INT");
        StringBuilder strbld = new StringBuilder(tempStr);
        finalSql = strbld;
        if (!isKpi) {
            if (isAOEnable) {
                //added by sruthi to display full query
                //             String  fullQuery1=fullQuery;//
                String fullQuery1 = fullQuery.toString();
                setfullQuery(fullQuery1);//ended by sruthi
                //    finalSql = finalSqlAO;//cbram
                String tempString = finalSqlAO;
                StringBuilder stringbld = new StringBuilder(tempString);
                finalSql = stringbld;
            }
        }
        //       finalSql = replaceSpecialSymbolAccToDB(userConnType,finalSql);//cbram
        String temp = replaceSpecialSymbolAccToDB(userConnType, finalSql.toString());
        StringBuilder stringbld = new StringBuilder(temp);
        finalSql = stringbld;

        //setFinalNormalQuery(finalSql);//cbram
        setFinalNormalQuery(finalSql.toString());
        //     this.reportQuery = finalSql;//cbram
        this.reportQuery = finalSql.toString();
        ///Removing inner View Bys
        if (innerViewBy != null && innerViewBy.size() > 0) {
            for (int iLoop = 0; iLoop < innerViewBy.size(); iLoop++) {
                if (rowViewbyCols.contains(innerViewBy.get(iLoop))) {
                    rowViewbyCols.remove(innerViewBy.remove(iLoop));
                }
            }

        }
        //    return replaceWhereCondition(finalSql);
        return replaceWhereCondition(finalSql.toString());
    }//End of Function generateViewByQry

    private String setParameterClause(String paramWhereClause) {
        //   String paramWhereClause1 = "";//cbram
        StringBuilder paramWhereClause1 = new StringBuilder();
        //     paramWhereClause1 = " and ( ";//cbram
        paramWhereClause1.append(" and ( ");
        if (needZeroRowQuery) {
            //  paramWhereClause1 += " 1=2 ";//cbram
            paramWhereClause1.append(" 1=2 ");
        } else {
            //     paramWhereClause1 += " 1=1 ";//cbram
            paramWhereClause1.append(" 1=1 ");
        }

        //     paramWhereClause1 += paramWhereClause;//cbram
        paramWhereClause1.append(paramWhereClause);
        //     paramWhereClause1 += "  ) ";//cbram
        paramWhereClause1.append("  ) ");
        //      return paramWhereClause1;//cbram
        return paramWhereClause1.toString();
    }

    private String replaceWhereCondition(String finalSql) {
        if (getWhereClause() != null && !getWhereClause().equalsIgnoreCase("")) {
            if (finalSql.toUpperCase().contains("ORDER BY")) {
                finalSql = finalSql.replace("order by", " WHERE " + getWhereClause() + " ORDER BY");
            } else {
                finalSql = finalSql + " WHERE " + getWhereClause();
            }

            finalSql = finalSql.replace("@@PROGEN_GBL_VAR@@USER_ID", userId);
            return finalSql;
        } else {
            if (finalSql.contains("@@PROGEN_GBL_VAR@@USER_ID")) {
                finalSql = finalSql.replace("@@PROGEN_GBL_VAR@@USER_ID", userId);
            }
            return finalSql;
        }

    }

    private String getTruncDateClause(String Clasue1, String userConnType) {
        String modifiedClause = Clasue1.toUpperCase();
        modifiedClause = modifiedClause.replace("AND ", "");
        if (userConnType.equalsIgnoreCase("Sqlserver")) {
            modifiedClause = "and convert(date," + modifiedClause + " ) ";

        } else if (userConnType.equalsIgnoreCase("Oracle")) {
//            modifiedClause = "and trunc(" + modifiedClause + " ) ";
            modifiedClause = "and (" + modifiedClause + " ) ";
        } else if (userConnType.equalsIgnoreCase("Postgres")) {
            modifiedClause = "and truncdate(" + modifiedClause + " ) ";


        } else if (userConnType.equalsIgnoreCase("mysql")) {
//            modifiedClause = "and date(" + modifiedClause + " ) ";
            modifiedClause = "and (" + modifiedClause + " ) ";


        }


        return (modifiedClause);
    }

    private String resolveGroupTimeTable(String elementId) throws SQLException {
        PbReturnObject retObj;
        String[] colNames = null;
        String colName = null;
        String minLevel = null;
        String bussTimeTableName = "pr_day_denom";

//        String sqlstr = "";                                                                           //cbram
//        sqlstr += "select ref_table_name from prg_grp_buss_table where BUSS_TABLE_ID = "
//                + " (  select business_table_id from PRG_TIME_DIM_INFO "
//                + " where MAIN_FACT_ID in "
//                + " (select BUSS_TABLE_ID "
//                + " from PRG_USER_ALL_INFO_DETAILS "
//                + " where element_id =  " +elementId+" ) )  "
//                + " and ref_table_name is not NULL  ";
        StringBuilder sqlstr = new StringBuilder();
        sqlstr.append("select ref_table_name from prg_grp_buss_table where BUSS_TABLE_ID =(  select business_table_id from PRG_TIME_DIM_INFO where MAIN_FACT_ID in (select BUSS_TABLE_ID from PRG_USER_ALL_INFO_DETAILS where element_id = ");
        sqlstr.append(elementId).append(" ) )  ").append(" and ref_table_name is not NULL  ");
//
        //retObj = pbDb.execSelectSQL(sqlstr);//cbram
        retObj = pbDb.execSelectSQL(sqlstr.toString());

        colNames = retObj.getColumnNames();

        //Vector facts = new Vector();
        // HashMap CurrCols = new HashMap();
        // HashMap PriorCols = new HashMap();

        int psize = retObj.getRowCount();

        if (psize > 0) {
            //Looping twice
            //Loop 1 find the fact and current and prior cols
            //loop 2 build query
            if (!collectMulticalender.isEmpty()) {
                ArrayList Multicalender = ((ArrayList) collectMulticalender.get((collectMulticalender.keySet().toArray()[0])));
//                ArrayList Multicalender  = (ArrayList) collectMulticalender.get(keySet.get(0));
                bussTimeTableName = colName = Multicalender.get(8).toString();
                newTimetableName = colName = Multicalender.get(8).toString();
//                ArrayList Multicalender = (ArrayList)collectMulticalender.get(factId);
            } else {
                if (gbl_calander_var != null && !gbl_calander_var.equalsIgnoreCase("null") && !gbl_calander_var.equalsIgnoreCase("")) {
                    bussTimeTableName = gbl_calander_var;
                    newTimetableName = gbl_calander_var;
                } else if (retObj.getFieldValue(0, colNames[0]) != null) {
                    bussTimeTableName = colName = retObj.getFieldValueString(0, colNames[0]);
                    newTimetableName = colName = retObj.getFieldValueString(0, colNames[0]);
                }

                //TimetableId = retObj.getFieldValueString(0, colNames[1]);

            }
        }
//
        return (bussTimeTableName);

    }

    private void resolveTimeReference(String timeDimTable) throws SQLException {

//        ProgenLog.log(ProgenLog.FINE, this, "resolveTimeReference", "Enter ");
        logger.info("Enter Method");
        pbTime.timeTableName = timeDimTable;
        String timeForGO = "";
        //added by Nazneen for multi calander

        if (!collectMulticalender.isEmpty()) {
            ArrayList Multicalender = ((ArrayList) collectMulticalender.get((collectMulticalender.keySet().toArray()[0])));
            pbTime.timeTableName = Multicalender.get(8).toString();

        } else if (gbl_calander_var != null && !gbl_calander_var.equalsIgnoreCase("null") && !gbl_calander_var.equalsIgnoreCase("")) {
            pbTime.timeTableName = gbl_calander_var;
        }
        if (timeDetails.get(0).toString().equalsIgnoreCase("Day")) {
            if (timeDetails.get(1).toString().equalsIgnoreCase("PRG_STD")) {
                if (!qryType.equalsIgnoreCase("Trend") || (isKpi) || isQueryForAO || isScheduleAO) {
                    pbTime.setRange(timeDetails.get(3).toString(), timeDetails.get(4).toString(), timeDetails.get(2).toString());
                    // pbTime.setTargetRange(userId, OorderByCol, osql);
                    pbTime.setTargetRange(timeDetails.get(3).toString(), timeDetails.get(4).toString(), timeDetails.get(2).toString());
                    if (!TimetableCurrClause.containsKey(TimetableCurrClause)) {
                        TimetableCurrClause.put(timeDimTable, pbTime.d_clu);
                        TimetablePriorClause.put(timeDimTable, pbTime.pd_clu);
                        TimetableftClause.put(timeDimTable, pbTime.ft_clu);
                        {
                            ArrayList timeotherval = new ArrayList();
                            timeotherval.add(pbTime.st_d);
                            timeotherval.add(pbTime.ed_d);
                            timeotherval.add(pbTime.p_st_d);
                            timeotherval.add(pbTime.p_ed_d);
                            timeotherval.add(pbTime.ft_st_d);
                            timeotherval.add(pbTime.ft_ed_d);
                            TimetableOtherValues.put(timeDimTable, timeotherval);

                        }
                    }
                    this.setTimememdetails(TimetableOtherValues);

                } else {
                    if (isQueryForGO && !avoidProGenTime && !goTimeType.equalsIgnoreCase("ReportTime")) {
                        timeForGO = pbTime.setRange(timeDetails, goTimeType);
                    }
//                    else{
                    timeLevel = timeDetails.get(3).toString();
                    pbTime.setTrendRange(timeDetails.get(3).toString(), timeDetails.get(4).toString(), timeDetails.get(2).toString(), isTimeDrill);
                    pbTime.setTargetTrendRange(timeDetails.get(3).toString(), timeDetails.get(4).toString(), timeDetails.get(2).toString());
                    trendNoOfDays = pbTime.trendNoOfDays;
                }

            } else {
                //if(timeDetails.get(1).toString().equalsIgnoreCase("PRG_DATE_RANGE"))
                if (!qryType.equalsIgnoreCase("Trend") || isScheduleAO) {
                    pbTime.setRange(timeDetails);
                    if (timeDetails.get(1).toString().equalsIgnoreCase("PRG_DATE_RANGE")) {
                    }
                    if (timeDetails.get(1).toString().equalsIgnoreCase("PRG_Month_RANGE")) {
                        timeLevel = "Month";
                    }
                } else {
                    //pbTime.setTrendRange(timeDetails.get(3).toString(), timeDetails.get(4).toString(), timeDetails.get(2).toString(), isTimeDrill);
                    pbTime.setRange(timeDetails);
                    timeLevel = "Day";
                }

            }
        } else if (timeDetails.get(0).toString().equalsIgnoreCase("Month")) {
            if (timeDetails.get(1).toString().equalsIgnoreCase("PRG_STD")) {
                String mon = new String();
                if (timeDetails.get(2) == null) {
                    mon = "NOV-08";
                } else {
                    mon = timeDetails.get(2).toString();
                }
                if (!qryType.equalsIgnoreCase("Trend")) {

                    pbTime.setMonthRange(String.valueOf(timeDetails.get(3)), timeDetails.get(4).toString(), mon);
                    //////07-12-09
                    pbTime.setTargetRange(timeDetails.get(3).toString(), timeDetails.get(4).toString(), timeDetails.get(2).toString());

                } else {
                    pbTime.setMonthTrendRange(String.valueOf(timeDetails.get(3)), timeDetails.get(4).toString(), mon);
                    pbTime.setTargetTrendRange(timeDetails.get(3).toString(), timeDetails.get(4).toString(), timeDetails.get(2).toString());

                }
                isDateTrueforNonDt = true;
            }
            if (timeDetails.get(1).toString().equalsIgnoreCase("PRG_MONTH_CMP")) {
                if (!qryType.equalsIgnoreCase("Trend")) {
                    pbTime.setStdMonthRange(String.valueOf(timeDetails.get(2)), String.valueOf(timeDetails.get(3)));
                } else {
                    pbTime.setStdMonthTrendRange(String.valueOf(timeDetails.get(2)), String.valueOf(timeDetails.get(3)));
                    isDateTrueforNonDt = true;
                }

            }
        } else if (timeDetails.get(0).toString().equalsIgnoreCase("YEAR")) {
            if (timeDetails.get(1).toString().equalsIgnoreCase("PRG_YEAR_CMP")) {

                if (!qryType.equalsIgnoreCase("Trend")) {
                    pbTime.setStdYearRange(String.valueOf(timeDetails.get(2)), String.valueOf(timeDetails.get(3)));
                    //////07-12-09
                    //pbTime.setTargetRange(timeDetails.get(3).toString(), timeDetails.get(4).toString(), timeDetails.get(2).toString());

                } else {
                    pbTime.setStdYearTrendRange(String.valueOf(timeDetails.get(2)), String.valueOf(timeDetails.get(3)));

                }


            }
        }

        if (!TimetableCurrClause.containsKey(TimetableCurrClause)) {
            if (isQueryForGO && !avoidProGenTime && !goTimeType.equalsIgnoreCase("ReportTime")) {
                TimetableCurrClause.put(timeDimTable, timeForGO);
            } else {

                TimetableCurrClause.put(timeDimTable, pbTime.d_clu);
            }
            TimetablePriorClause.put(timeDimTable, pbTime.pd_clu);
            TimetableftClause.put(timeDimTable, pbTime.ft_clu);
            {
                ArrayList timeotherval = new ArrayList();
                timeotherval.add(pbTime.st_d);
                timeotherval.add(pbTime.ed_d);
                timeotherval.add(pbTime.p_st_d);
                timeotherval.add(pbTime.p_ed_d);
                timeotherval.add(pbTime.ft_st_d);
                timeotherval.add(pbTime.ft_ed_d);
                TimetableOtherValues.put(timeDimTable, timeotherval);

            }
        }
        this.setTimememdetails(TimetableOtherValues);
//        ProgenLog.log(ProgenLog.FINE, this, "resolveTimeReference", "Exit ");
        logger.info("Exit");

    }

    private String getParamValueforCurr(String ParamId) {
        String val = String.valueOf(getParamValue().get(ParamId));
        if (val == null || val.equalsIgnoreCase("NULL")) {
            val = "ALL";
        }
        return (val);

    }

    private ArrayList getAllTable(String tables, String fact) throws Exception {
        ArrayList allTable = new ArrayList();

        PbReturnObject retObj = null;
        String[] colNames;
        String[] allTabs;


        String allTables = "";
        //   String sqlstr;//cbram
        StringBuilder sqlstr = new StringBuilder();
        //     String finalQuery;//cbram
        StringBuilder finalQuery = new StringBuilder();

//        sqlstr = "select BUSS_TABLE_PATH from PRG_GRP_BUSS_TABLE_MAPS where BUSS_FACT_ID = " + fact;   //cbram
//        sqlstr += " and BUSS_TABLE_ID in( " + tables + " ) ";                                          //cbram
        sqlstr.append("select BUSS_TABLE_PATH from PRG_GRP_BUSS_TABLE_MAPS where BUSS_FACT_ID = ").append(fact).append(" and BUSS_TABLE_ID in( ").append(tables).append(" ) ");



        //finalQuery = pbDb.buildQuery(sqlstr, Obj);
        finalQuery = sqlstr;

        //   retObj = pbDb.execSelectSQL(finalQuery);//cbram
        retObj = pbDb.execSelectSQL(finalQuery.toString());

        if (retObj != null && retObj.getRowCount() > 0) {
            colNames = retObj.getColumnNames();
            int psize = retObj.getRowCount();
            if (psize > 0) {
                for (int looper = 0; looper < psize; looper++) {
                    allTables += "," + retObj.getFieldValueString(looper, colNames[0]);
                    //mainTable.add(retObj.getFieldValueString(looper, colNames[0]));
                }

                allTables = allTables.substring(1);
            } else {
                allTables = tables;
            }


            return (StringtoArray(allTables));
        }




        return (StringtoArray(tables + ","));
    }

    private ArrayList StringtoArray(String str) {
        ArrayList A = new ArrayList();
        String[] c = str.split(",");
        for (int i = 0; i < c.length; i++) {
            A.add(c[i]);
        }
        return (A);
    }

    private String changeForTimeInfo(String orderByClause) {
        orderByClause = orderByClause.toUpperCase();

        if (orderByClause.contains("PRG_DAY_DETAILS.WEEK_DAY_NAME")
                || orderByClause.contains("PRG_DAY_DETAILS.DAY_OF_WEEK")
                || orderByClause.contains("PRG_DAY_DETAILS.MONTH_DAY")
                || orderByClause.contains("PRG_DAY_DETAILS.DAYSOFYEAR")
                || orderByClause.contains("PRG_DAY_DETAILS.WEEK_OF_YEAR")
                || orderByClause.contains("PRG_DAY_DETAILS.WEEK_OF_MONTH")) {
            timeLevel = "Day";
        } else if (orderByClause.contains("PRG_DAY_DETAILS.MONTH_NAME")
                || orderByClause.contains("PRG_DAY_DETAILS.MONTH_SHORT_NAME")
                || orderByClause.contains("PRG_DAY_DETAILS.MONTH_YEAR")
                || orderByClause.contains("PRG_DAY_DETAILS.MONTH_ST_DATE")) {
            if (timeLevel == null) {
                timeLevel = "Month";
            } else if (timeLevel.equalsIgnoreCase("Day")) {
                timeLevel = "Day";
            } else if (timeLevel.equalsIgnoreCase("Week")) {
                timeLevel = "Day";
            } else {
                timeLevel = "Month";
            }
        } else if (orderByClause.contains("PRG_DAY_DETAILS.QTR_NAME")
                || orderByClause.contains("PRG_DAY_DETAILS.QTR_YEAR")
                || orderByClause.contains("PRG_DAY_DETAILS.QTR_YEAR_INT")) {
            if (timeLevel == null) {
                timeLevel = "Qtr";
            } else if (timeLevel.equalsIgnoreCase("Day")) {
                timeLevel = "Day";
            } else if (timeLevel.equalsIgnoreCase("Week")) {
                timeLevel = "Day";
            } else if (timeLevel.equalsIgnoreCase("Month")) {
                timeLevel = "Month";
            } else {
                timeLevel = "Qtr";
            }
        } else if (orderByClause.contains("PRG_DAY_DETAILS.YEAR_TEXT_NAME")
                || orderByClause.contains("PRG_DAY_DETAILS.YEAR_NAME")) {
            ////
            if (timeLevel == null) {
                timeLevel = "Year";
            } else if (timeLevel.equalsIgnoreCase("Day")) {
                timeLevel = "Day";
            } else if (timeLevel.equalsIgnoreCase("Week")) {
                timeLevel = "Day";
            } else if (timeLevel.equalsIgnoreCase("Month")) {
                timeLevel = "Month";
            } else if (timeLevel.equalsIgnoreCase("QTR_NAME")) {
                timeLevel = "Qtr";
            } else {
                timeLevel = "Year";
            }
        }
        orderByClause = orderByClause.replace("PRG_DAY_DETAILS.WEEK_DAY_NAME", "PRG_DAY_DETAILS.DAY_OF_WEEK");
        orderByClause = orderByClause.replace("PRG_DAY_DETAILS.MONTH_NAME", "PRG_DAY_DETAILS.MONTH_OF_YEAR");
        orderByClause = orderByClause.replace("PRG_DAY_DETAILS.MONTH_SHORT_NAME", "PRG_DAY_DETAILS.MONTH_OF_YEAR");
        orderByClause = orderByClause.replace("PRG_DAY_DETAILS.MONTH_YEAR", "PRG_DAY_DETAILS.MONTH_ST_DATE");
        orderByClause = orderByClause.replace("PRG_DAY_DETAILS.QTR_NAME", "PRG_DAY_DETAILS.QTR_OF_YEAR");
        orderByClause = orderByClause.replace("PRG_DAY_DETAILS.QTR_YEAR", "PRG_DAY_DETAILS.QTR_YEAR_INT");
        orderByClause = orderByClause.replace("PRG_DAY_DETAILS.YEAR_TEXT_NAME", "PRG_DAY_DETAILS.YEAR_NAME");
        orderByClause = orderByClause.replace("PRG_DAY_DETAILS.CUSTOM_DATE_FORMAT1", "PRG_DAY_DETAILS.DDATE");
        orderByClause = orderByClause.replace("PRG_DAY_DETAILS.CUSTOM_DATE_FORMAT2", "PRG_DAY_DETAILS.DDATE");
//        orderByClause = orderByClause.replace("PRG_DAY_DETAILS.CUSTOM_DATE_FORMAT3", "PRG_DAY_DETAILS.DDATE");
        orderByClause = orderByClause.replace("PRG_DAY_DETAILS.CUSTOM_DATE_FORMAT4", "PRG_DAY_DETAILS.DDATE");
        return (orderByClause);
    }

    String getAggregation(Object selfID, Object refID) {

        String formula = null;
        if (qryColAgg.get(selfID) != null) {
            formula = (qryColAgg.get(selfID).toString());
        } else if (qryRefAgg.get(selfID) != null) {
            formula = (qryRefAgg.get(selfID).toString());
        } else if (qryColAgg.get(refID) != null) {
            formula = (qryColAgg.get(refID).toString());
        } else if (qryRefAgg.get(refID) != null) {
            formula = (qryRefAgg.get(refID).toString());
        }

        return (getOracleFormula(formula));
    }

    String formatString(String str) {
        String s1 = new String();
        s1 = str.replace(" ", "").replace("'", "").replace("-", "").replace("%", "p").replace("(", "_").replace(")", "_");
        return (s1);
    }

    String getOracleFormula(String formula) {
        if (!(formula == null || "".equals(formula.trim()) || "NULL".equalsIgnoreCase(formula))) {
            String f1 = formula.trim().toUpperCase();
            if (f1.equalsIgnoreCase("COUNTDISTINCT")) {

                return (" count( distinct ");
            }
            return (formula + "( ");
        }
        return (formula);
    }

    String getOracleFormula2d(String formula) {
        String f1 = formula.trim().toUpperCase();
        if (f1.trim().equalsIgnoreCase("COUNTDISTINCT")) {
            return (" count");
        }
        return (formula + "");
    }

    String getLevelFormula(String formula, int noFacts) {
        String newFormula = formula.replace("distinct", "").replace("(", "").trim();
        newFormula = newFormula.replace(" ", "");
        if (newFormula.toUpperCase().contains("COUNT")) {
            newFormula = (" sum(");
        } else if (newFormula.toUpperCase().contains("AVG")) {
            //newFormula = "Avg(" + noFacts + " * ";
            newFormula = "Avg( 1 * ";
        } else {
            newFormula = formula;
        }
        return (newFormula);
    }

    boolean ValidateParamValues(Object o) {
        boolean isValid = true;
        if (o instanceof List) {
            if (o == null || ((List<String>) o).contains("All") || ((List<String>) o).contains("ALL")
                    || ((List<String>) o).isEmpty()) {
                isValid = false;
            }
        } else {
            if (o == null || o.toString().equalsIgnoreCase("All") || o.toString().equalsIgnoreCase("null")
                    || o.toString().equalsIgnoreCase("")) {
                isValid = false;
            }
        }
        return (isValid);
    }

    String getOracleInClause(Object o) {

        if (o instanceof List) {
            StringBuilder result = new StringBuilder();
            for (Object obj : (List) o) {
                result.append(",'").append(obj.toString()).append("'");
            }
            return (result.length() > 0 ? result.toString().substring(1) : "");
        } else {
            return ("'" + o.toString().replace(",", "','") + "'");
        }

    }

    String getOracleLikeNotlikeClause(String isLike, String tableColumn, Object o) {
        if (o instanceof List) {
            int i = 0;
            StringBuilder result = new StringBuilder();
            for (Object obj : (List) o) {
                if (i == 0) {
                    result.append("  ").append("'").append(obj.toString()).append("'");
                } else {
                    result.append(") OR ").append(tableColumn).append(" ").append(isLike).append(" ( ").append("'").append(obj.toString()).append("'");
                }

                i++;
            }
            return (result.length() > 0 ? result.toString().substring(1) : "");
        } else {
            return ("'" + o.toString().replace(",", "','") + "'");
        }

    }

    String getOracleInClauseTarget(Object o) {
        if (o instanceof List) {
            StringBuilder result = new StringBuilder();
            for (Object obj : (List) o) {
                result.append(",'").append(obj.toString()).append("'");
            }
            return (result.length() > 0 ? result.toString().substring(1) : "");
        } else {
            return ("'" + o.toString().replace(",", "','") + "'");
        }
    }

    public String getTimeColforFact(String factId) throws Exception {
        PbReturnObject retObj;
        String[] colNames = null;
        String colName = null;
        String minLevel = null;
        //       String sqlstr = "";                                                                                   //cbram
//        sqlstr += "select MD.BUSS_COL_NAME, M.BUSINESS_TABLE_ID, M.MIN_LEVEL, B.BUSS_TABLE_NAME ";
//        sqlstr += " , nvl(B.REF_TABLE_NAME,'PR_DAY_DENOM') REF_TABLE_NAME ";
//        sqlstr += " from PRG_USER_SUB_FOLDER_ELEMENTS MD ";
//        sqlstr += " , PRG_TIME_DIM_INFO M ";
//        sqlstr += " , PRG_GRP_BUSS_TABLE B ";
//        sqlstr += " where m.MAIN_FACT_ID =     MD.BUSS_TABLE_ID ";
//        sqlstr += " and m.MAIN_FACT_COL_ID = MD.buss_COL_ID ";
//        sqlstr += " and MD.BUSS_TABLE_ID =     B.BUSS_TABLE_ID ";
//        sqlstr += " and MAIN_FACT_ID = " + factId + " ";

        StringBuilder sqlstr = new StringBuilder();
        sqlstr.append("select MD.BUSS_COL_NAME, M.BUSINESS_TABLE_ID, M.MIN_LEVEL, B.BUSS_TABLE_NAME, nvl(B.REF_TABLE_NAME,'PR_DAY_DENOM') REF_TABLE_NAME from PRG_USER_SUB_FOLDER_ELEMENTS MD, PRG_TIME_DIM_INFO M");
        sqlstr.append(", PRG_GRP_BUSS_TABLE B  where m.MAIN_FACT_ID =     MD.BUSS_TABLE_ID and m.MAIN_FACT_COL_ID = MD.buss_COL_ID and MD.BUSS_TABLE_ID = B.BUSS_TABLE_ID and MAIN_FACT_ID = ").append(factId).append(" ");
        String tmpstr = sqlstr.toString();
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
            //   sqlstr = sqlstr.replace("nvl", "COALESCE");//cbram
            tmpstr = tmpstr.replace("nvl", "COALESCE");
        } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
            //  sqlstr = sqlstr.replace("IFNULL", "COALESCE");//cbram
            tmpstr = tmpstr.replace("IFNULL", "COALESCE");
        }
        if (!ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.ORACLE)) {
            //    sqlstr = sqlstr.replace("nvl", "COALESCE");//cbram
            tmpstr = tmpstr.replace("nvl", "COALESCE");

        }
        //  retObj = pbDb.execSelectSQL(sqlstr);//cbram
        retObj = pbDb.execSelectSQL(tmpstr);
        colNames = retObj.getColumnNames();
        int psize = retObj.getRowCount();

        if (psize > 0) {
            //Looping twice
            //Loop 1 find the fact and current and prior cols
            //loop 2 build query
            //Added by Mohit for dynamic fact change
            if (!collectMulticalender.isEmpty() && collectMulticalender.get(factId) != null) {
                ArrayList Multicalender = (ArrayList) collectMulticalender.get(factId);
                if (Multicalender.get(1) != null) {
                    colName = Multicalender.get(0) + "." + Multicalender.get(1);
                    if (TableDateCalenderMap == null) {
                        ArrayList timeList = new ArrayList();
                        timeList.add(Multicalender.get(1));
                        timeList.add(Multicalender.get(8));
                        timeList.add(retObj.getFieldValueString(0, colNames[1]));

                        TableDateCalenderMap.put(Multicalender.get(8).toString(), timeList);
                    } else if (!TableDateCalenderMap.containsKey(Multicalender.get(8).toString())) {
                        ArrayList timeList = new ArrayList();
                        timeList.add(Multicalender.get(1));
                        timeList.add(Multicalender.get(8));
                        timeList.add(retObj.getFieldValueString(0, colNames[1]));
                    }
                } else {
                    colName = Multicalender.get(1).toString();
                }
                TimetableId = retObj.getFieldValueString(0, colNames[1]);
                newTimetableName = Multicalender.get(8).toString();

            } else {
                String localgbl_calander_var = "";
                if (gbl_calander_var != null && !gbl_calander_var.equalsIgnoreCase("null") && !gbl_calander_var.equalsIgnoreCase("")) {
                    localgbl_calander_var = gbl_calander_var;
//            newTimetableName= gbl_calander_var;
                } else {
                    localgbl_calander_var = retObj.getFieldValueString(0, colNames[4]);
                }
                if (retObj.getFieldValue(0, colNames[3]) != null) {
                    colName = retObj.getFieldValueString(0, colNames[3]) + "." + retObj.getFieldValueString(0, colNames[0]);
                    if (TableDateCalenderMap == null) {
                        ArrayList timeList = new ArrayList();
                        timeList.add(retObj.getFieldValueString(0, colNames[0]));
                        timeList.add(localgbl_calander_var);
                        timeList.add(retObj.getFieldValueString(0, colNames[1]));

                        TableDateCalenderMap.put(localgbl_calander_var, timeList);
                    } else if (!TableDateCalenderMap.containsKey(localgbl_calander_var)) {
                        ArrayList timeList = new ArrayList();
                        timeList.add(retObj.getFieldValueString(0, colNames[0]));
                        timeList.add(localgbl_calander_var);
                        timeList.add(retObj.getFieldValueString(0, colNames[1]));
                    }
                } else {
                    colName = retObj.getFieldValueString(0, colNames[0]);
                }
                TimetableId = retObj.getFieldValueString(0, colNames[1]);
                if (newTimetableName == null || newTimetableName.equals("") || newTimetableName.equalsIgnoreCase("null")) {
                    newTimetableName = localgbl_calander_var;

                }
            }


            minLevel = retObj.getFieldValueString(0, colNames[2]).trim();


            if (minLevel.equalsIgnoreCase("3")) {
                this.TimetableDateColName = " PROGEN_TIME.CM_ST_DATE ";
            }



        }
        return (colName);
    }

    private void getTimeJoinforFact(String factId, String TimeTable) throws Exception {
        //
        PbReturnObject retObj;
        String[] colNames = null;
        String colName = null;

//        String sqlstr = "";                                                    //cbram
//        sqlstr += "select ACTUAL_CLAUSE , S_COL1_NAME, S_COL2_NAME ";
//        sqlstr += " ";
//        sqlstr += " from PRG_TIME_DIM_INFO_RLT_DETAILS M ";
//        sqlstr += " where ";
//        sqlstr += " m.P_BUSS_TABLE_ID = " + factId + " ";
//        sqlstr += " and m.S_BUSS_TABLE_ID = " + TimeTable + " ";

        StringBuilder sqlstr = new StringBuilder();
        sqlstr.append("select ACTUAL_CLAUSE , S_COL1_NAME, S_COL2_NAME").append(" ").append(" from PRG_TIME_DIM_INFO_RLT_DETAILS M where m.P_BUSS_TABLE_ID = ");
        sqlstr.append(factId).append(" ").append("and m.S_BUSS_TABLE_ID = ").append(TimeTable).append(" ");

        //      retObj = pbDb.execSelectSQL(sqlstr);//cbram
        retObj = pbDb.execSelectSQL(sqlstr.toString());

        colNames = retObj.getColumnNames();

        //Vector facts = new Vector();
        // HashMap CurrCols = new HashMap();
        // HashMap PriorCols = new HashMap();

        int psize = retObj.getRowCount();

        if (psize > 0) {
            //Looping twice
            //Loop 1 find the fact and current and prior cols
            //loop 2 build query


            currTimeClause = retObj.getFieldValueString(0, colNames[0]);
            priorTimeClause = retObj.getFieldValueString(0, colNames[0]);
            //Added by Mohit for dynamic fact change
            if (!collectMulticalender.isEmpty() && collectMulticalender.get(factId) != null) {
                ArrayList Multicalender = (ArrayList) collectMulticalender.get(factId);
                String substring = currTimeClause.substring(currTimeClause.split("=")[0].indexOf("(") + 1, currTimeClause.split("=")[0].indexOf(")"));
                currTimeClause = currTimeClause.replace(substring, Multicalender.get(0) + "." + Multicalender.get(1));
                priorTimeClause = priorTimeClause.replace(substring, Multicalender.get(0) + "." + Multicalender.get(1));
            }

            if (retObj.getFieldValueString(0, colNames[1]) != null) {
                if (retObj.getFieldValueString(0, colNames[1]).equalsIgnoreCase("DDATE")) {
                    if (timeDetails.get(0).toString().equalsIgnoreCase("Day")) {
                        if (timeDetails.get(1).toString().equalsIgnoreCase("PRG_STD")) {
                            if (timeDetails.get(4).toString().equalsIgnoreCase("PERIOD")
                                    || timeDetails.get(4).toString().equalsIgnoreCase("LAST PERIOD")
                                    || timeDetails.get(4).toString().equalsIgnoreCase("PERIOD COMPLETE")
                                    || timeDetails.get(4).toString().equalsIgnoreCase("LAST PERIOD COMPLETE")
                                    || timeDetails.get(4).toString().equalsIgnoreCase("LAST DAY")
                                    || timeDetails.get(4).toString().equalsIgnoreCase("LAST WEEK")
                                    || timeDetails.get(4).toString().equalsIgnoreCase("LAST QTR")
                                    || timeDetails.get(4).toString().equalsIgnoreCase("LAST MONTH")
                                    || timeDetails.get(4).toString().equalsIgnoreCase("COMPLETE LAST MONTH")
                                    || timeDetails.get(4).toString().equalsIgnoreCase("COMPLETE LAST QTR")
                                    || timeDetails.get(4).toString().equalsIgnoreCase("SAME MONTH LAST YEAR")
                                    || timeDetails.get(4).toString().equalsIgnoreCase("COMPLETE SAME MONTH LAST YEAR")) {
                                String s = priorTimeClause.replace("DDATE", "DDATE");
                                priorTimeClause = s;

                            } else {
                                String s = priorTimeClause.replace("DDATE", "DDATE");
                                priorTimeClause = s;
                            }

                        }
                    } else {
                        String s = priorTimeClause.replace("DDATE", "LY_DAY");
                        priorTimeClause = s;
                    }



                } else if (retObj.getFieldValueString(0, colNames[1]).equalsIgnoreCase("CW_CUST_NAME")) {

                    String s = priorTimeClause.replace("CW_CUST_NAME", "LYW_CUST_NAME");
                    priorTimeClause = s;
                } else if (retObj.getFieldValueString(0, colNames[1]).equalsIgnoreCase("CMONTH")) {

                    String s = priorTimeClause.replace("CMONTH", "PYM_CUST_NAME");
                    priorTimeClause = s;
                } else if (retObj.getFieldValueString(0, colNames[1]).equalsIgnoreCase("CM_CUST_NAME")) {

//                    String s = priorTimeClause.replace("CM_CUST_NAME", "PM_CUST_NAME");
                    String s = priorTimeClause.replace("CM_CUST_NAME", "CM_CUST_NAME");
                    priorTimeClause = s;
                } else if (retObj.getFieldValueString(0, colNames[1]).equalsIgnoreCase("CYEAR")) {

                    String s = priorTimeClause.replace("CYEAR", "LY_CUST_NAME");
                    priorTimeClause = s;
                }



            }

        }
    }

    public PbReturnObject getPbReturnObject12() {
        PbReturnObject retObj = null;
        Connection conn = null;
        PbDataDisplayBeanDb bean = new PbDataDisplayBeanDb();
        try {

            // System.out.println("generateViewByQry()" + generateViewByQry());
            String sql = generateViewByQry();
            conn = bean.getConnection("11");
            retObj = pbDb.execSelectSQL(sql, conn);

        } catch (Exception exp) {
            logger.error("Exception:", exp);
        }
        return retObj;
    }

    public String getBussTableByElementId(String elementId) {
        PbReturnObject retObj = null;
        Connection conn = null;
        return "";
    }

    public PbReturnObject getPbReturnObject(String elementId, String sortString) {
        PbReturnObject retObj = null;
        Connection conn = null;
        try {

            //conn = getConnection(elementId);
            // System.out.println("connnn" + conn);
            String sql = generateViewByQry();
            StringBuilder sql1 = null;
            if (!sql.toUpperCase().contains("ORDER BY")) {
                sql1 = new StringBuilder(sql);
                //   sql = sql + " order by " + sortString;//cbram
                sql1.append(sql1).append(" order by ").append(sortString);
            }
            //     setFinalNormalQuery(sql);//cbram
            setFinalNormalQuery(sql1.toString());
            conn = getConnection(elementId);
            //  retObj = pbDb.execSelectSQL(sql, conn);//cbram
            retObj = pbDb.execSelectSQL(sql1.toString(), conn);

        } catch (Exception exp) {
            logger.error("Exception:", exp);
        } finally {
//            if (conn != null) {
//                try {
//                    conn.close();
//                } catch (SQLException ex) {
//                    logger.error("Exception:",ex);
//                }
//            }
        }
        return retObj;
    }

    public ProgenDataSet getProgenDataSet(String elementId, String sql, boolean poiDataSet) {
        Connection conn = null;
        ProgenDataSet dataSet = null;
        try {
            logger.info(" sql from ProgenDataSet " + sql);
            setFinalNormalQuery(sql);
            conn = getConnection(elementId);
            //modified by anitha
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.ORACLE)) {
                ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();
                ArrayList<String> sessionQueryLst = new ArrayList<String>();
                try {
                    sessionQueryLst = reportTemplateDAO.getSessionQueriesForOracle();
                } catch (Exception e) {
                    logger.error("Exception:", e);
                }
                dataSet = pbDb.execSelectORACLE(sql, conn, poiDataSet, sessionQueryLst);
            } else {
                dataSet = pbDb.execSelectSQL(sql, conn, poiDataSet);
            }
        } catch (Exception exp) {
            logger.error("Exception:", exp);
        }
        return dataSet;
    }

    public PbReturnObject getPbReturnObject(String elementId) {
        PbReturnObject retObj = null;
        Connection conn = null;
        try {

            //conn = getConnection(elementId);
            // System.out.println("connnn" + conn);
            String sql = generateViewByQry();
            StringBuilder sql1 = null;
            String tempString = "";
            if (this.sortColAndOrder == null) {
                if ((sql.contains("ORDER BY") || sql.contains("order by"))) {
                    sql1 = new StringBuilder(sql);
                    //          sql = sql + " ORDER BY ";//cbram
                    sql1.append(" ORDER BY ");
                    Set<String> elementIds = this.sortColAndOrder.keySet();
                    for (String element : elementIds) {
                        //      sql = sql + element + " " + this.sortColAndOrder.get(element) + ",";//cbram
                        sql1.append(element).append(" ").append(this.sortColAndOrder.get(element)).append(",");
                    }
                    tempString = sql1.toString();
                    //        sql = sql.substring(0, sql.length() - 1);//cbram
                    tempString = tempString.substring(0, tempString.length() - 1);
                }
            }
//
            //setFinalNormalQuery(sql);//cbram
            setFinalNormalQuery(tempString);
            // //////.println("sql in getPbReturnObject is ::::::::: " + sql);
            conn = getConnection(elementId);
            //       retObj = pbDb.execSelectSQL(sql, conn);//cbram
            retObj = pbDb.execSelectSQL(tempString, conn);

        } catch (Exception exp) {
            logger.error("Exception:", exp);
        } finally {
//            if (conn != null) {
//                try {
//                    conn.close();
//                } catch (SQLException ex) {
//                    logger.error("Exception:",ex);
//                }
//            }
        }
        return retObj;
    }

    public PbReturnObject getPbReturnObject1(String elementId) {
        PbReturnObject retObj = null;
        Connection conn = null;
        try {

            //conn = getConnection(elementId);
            // //////.println("connnn" + conn);
            String sql = generateViewByQry();

            if (this.sortColAndOrder != null) {
                if (!(sql.contains("ORDER BY") || sql.contains("order by"))) {
                    sql = sql + " ORDER BY ";
                    Set<String> elementIds = this.sortColAndOrder.keySet();
                    for (String element : elementIds) {
                        sql = sql + element + " " + this.sortColAndOrder.get(element) + ",";
                    }
                    sql = sql.substring(0, sql.length() - 1);
                }
            }
//            System.out.println("FinalSQL______"+sql);
            setFinalNormalQuery(sql);
            // //////.println("sql in getPbReturnObject is ::::::::: " + sql);
            conn = getConnection(elementId);
            retObj = pbDb.execSelectSQL(sql, conn);

        } catch (Exception exp) {
            exp.printStackTrace();
        } finally {
//            if (conn != null) {
//                try {
//                    conn.close();
//                } catch (SQLException ex) {
//                    ex.printStackTrace();
//                }
//            }
        }
        return retObj;
    }

    public PbReturnObject getPbReturnObjectCrossChecked(String elementId, Container container) {
        PbReturnObject retObj = null;
        Connection conn = null;
//
        try {

            //conn = getConnection(elementId);
            // System.out.println("connnn" + conn);
            String sql = generateViewByQry();
            StringBuilder sql1 = null;
            String tempString = "";
            if (this.sortColAndOrder != null) {
                if (!(sql.contains("ORDER BY") || sql.contains("order by"))) {
                    sql1 = new StringBuilder(sql);
                    //       sql = sql + " ORDER BY ";//cbram
                    sql1.append(" ORDER BY ");
                    Set<String> elementIds = this.sortColAndOrder.keySet();
                    for (String element : elementIds) {
                        //          sql = sql + element + " " + this.sortColAndOrder.get(element) + ",";//cbram
                        sql1.append(element).append(" ").append(this.sortColAndOrder.get(element)).append(",");
                    }
                    tempString = sql1.toString();
                    //       sql = sql.substring(0, sql.length() - 1);//cbram
                    tempString = tempString.substring(0, tempString.length() - 1);
                }
            }
//
            //       setFinalNormalQuery(sql);//cbram
            setFinalNormalQuery(tempString);
            // //////.println("sql in getPbReturnObject is ::::::::: " + sql);
            conn = getConnection(elementId);
            //    retObj = pbDb.execSelectSQL(sql, conn);//cbram
            retObj = pbDb.execSelectSQL(tempString, conn);
            //
            if (getColViewbyCols() != null && getColViewbyCols().size() > 0) {
                //
                retObj.rowViewBys = getOrgRowViewbyCols();
                retObj.ColViewBys = getColViewbyCols();
                retObj.totalViewBys = getRowViewbyCols().size();
                retObj.totalOrderbys = getRowViewbyCols().size();
                retObj.rowViewCount = getOrgRowViewbyCols().size();
                retObj.colViewCount = getColViewbyCols().size();
                retObj.nonViewInput = NonViewByMap;
                retObj.Qrycolumns = getQryColumns();
                retObj.meausreOnCol = true;
                retObj.MeasurePos = getColViewbyCols().size();
                retObj.isGTNone = true;
                retObj.isSTNone = true;
                PbReturnObject newCrossRetObj = retObj.transposeReturnObject();
                //retObj = null;
                retObj = newCrossRetObj;
//
//
//

                retObj.rowCount = newCrossRetObj.rowCount;
                retObj.rowViewBys = getOrgRowViewbyCols();
                retObj.ColViewBys = getColViewbyCols();
                retObj.totalViewBys = getRowViewbyCols().size();
                retObj.totalOrderbys = getRowViewbyCols().size();
                retObj.rowViewCount = getOrgRowViewbyCols().size();
                retObj.colViewCount = getColViewbyCols().size();
                retObj.nonViewInput = NonViewByMap;
                retObj.Qrycolumns = getQryColumns();
                retObj.meausreOnCol = true;
                retObj.MeasurePos = getColViewbyCols().size();
                retObj.isGTNone = true;
                retObj.isSTNone = true;
//                retObj.resetViewSequence();
                //retObj.prepareObject(retObj);
                this.crossTabNonViewByMap = newCrossRetObj.nonViewByMapNew;
                this.crossTabNonViewBy = newCrossRetObj.CrossTabfinalOrder;

//
//
//

            }

        } catch (Exception exp) {
            logger.error("Exception:", exp);
        } finally {
//            if (conn != null) {
//                try {
//                    conn.close();
//                } catch (SQLException ex) {
//                    logger.error("Exception:",ex);
//                }
//            }
        }
        return retObj;
    }

//    public String getFactFilter(String reportId, String tableId) throws SQLException{
    public ArrayList getFactFilter(String reportId, String tableId) throws SQLException {
        String tabFilter = "";
        String filterTypeDate = "";
//        ProgenLog.log(ProgenLog.FINE, this, "getFactFilter", "Enter ");
        logger.info("Enter");
        PbReturnObject retObj;
        String[] colNames = null;
        ArrayList aList = new ArrayList();


        //      String sqlstr = "";                                                                     //cbram
//        sqlstr += "select FILTER_FORMULA1 , FILTER_NAME ,TYPE_DATE ";
//        sqlstr += " ";
//        sqlstr += " from PRG_AR_FACT_FILTER  ";
//        sqlstr += "  M ";
//        sqlstr += " where BUSS_TABLE_ID = ( " + tableId + ") and REPORT_ID = ( " + reportId + ") ";
//        sqlstr += " union all select FILTER_FORMULA1 , FILTER_NAME ,TYPE_DATE ";
//        sqlstr += " ";
//        sqlstr += " from PRG_AR_FACT_FILTER  ";
//        sqlstr += "  M ";
//        sqlstr += " where BUSS_TABLE_ID = ( " + tableId + ") and REPORT_ID = -1 ";
//        sqlstr += "  and folder_id in (select folder_id from PRG_AR_REPORT_DETAILS "
//                + " where report_id = "+ reportId +") ";

        StringBuilder sqlstr = new StringBuilder();
        sqlstr.append("select FILTER_FORMULA1 , FILTER_NAME ,TYPE_DATE ").append(" ").append(" from PRG_AR_FACT_FILTER   M where BUSS_TABLE_ID = ( ");
        sqlstr.append(tableId).append(") and REPORT_ID = ( ").append(reportId).append(") ").append(" union all select FILTER_FORMULA1 , FILTER_NAME ,TYPE_DATE ").append(" ").append(" from PRG_AR_FACT_FILTER  ").append("  M ");
        sqlstr.append(" where BUSS_TABLE_ID = ( ").append(tableId).append(") and REPORT_ID = -1 ").append("  and folder_id in (select folder_id from PRG_AR_REPORT_DETAILS ").append(" where report_id = ").append(reportId).append(") ");

//

        retObj = pbDb.execSelectSQL(sqlstr.toString());

        int psize = retObj.getRowCount();

        if (psize > 0) {
            colNames = retObj.getColumnNames();
            String filterFromTable = null;

            //Looping twice
            //Loop 1 find the fact and current and prior cols
            //loop 2 build query
            for (int looper = 0; looper < psize; looper++) {
                if (retObj.getFieldValue(looper, 0) != null && !retObj.getFieldValueString(looper, 0).trim().equals("")) {
                    if (looper == 0) {
                        if (qryType.equalsIgnoreCase("Trend") && retObj.getFieldValueString(looper, colNames[2]).contains("Y")) {
                            filterFromTable = " ( 1= 1 ";
                            filterTypeDate = "N";
                        } else {
                            filterFromTable = " ( " + retObj.getFieldValueString(looper, colNames[0]);
                            filterTypeDate = retObj.getFieldValueString(looper, colNames[2]);
                        }

                    } else {
                        if (!(qryType.equalsIgnoreCase("Trend") && retObj.getFieldValueString(looper, colNames[2]).contains("Y"))) {
                            filterFromTable += " ) and ( " + retObj.getFieldValueString(looper, colNames[0]);
                            filterTypeDate += "," + retObj.getFieldValueString(looper, colNames[2]);
                        }
                    }
                }
                //
            }
            if (filterFromTable != null && !filterFromTable.trim().equals("")) {
                filterFromTable = " and " + filterFromTable + " ) ";
                tabFilter = filterFromTable;
                aList.add(tabFilter);
            } else {
                aList.add(tabFilter);
            }

            if (filterTypeDate != null && !filterTypeDate.trim().equals("")) {
                aList.add(filterTypeDate);
            } else {
                aList.add(filterTypeDate);
            }
//


        } else {
            aList.add(tabFilter);
            aList.add("N");
        }
//        ProgenLog.log(ProgenLog.FINE, this, "getFactFilter", "Exit " + tabFilter);
        logger.info("Exit " + tabFilter);
        return (aList);
//        return (tabFilter);
    }
    //Started by Nazneen

    public String getDesc(String tableId, String type, String clause, String connType) {

        if (tableId.equalsIgnoreCase("@@TIME_BASED_FORMULA@@")) {
            if (type.equalsIgnoreCase("C")) {
                if (connType.equalsIgnoreCase("ORACLE")) {
                    clause = clause.replace("@PROGENTIME@@ST_DATE", "to_date('" + pbTime.st_d1 + "','mm/dd/yyyy Hh24:mi:ss ')");
                    clause = clause.replace("@PROGENTIME@@ED_DATE", "to_date('" + pbTime.ed_d1 + "','mm/dd/yyyy Hh24:mi:ss ')");
                } else if (connType.equalsIgnoreCase("mysql")) {
                    clause = clause.replace("@PROGENTIME@@ST_DATE", "str_to_date('" + pbTime.st_d1 + "','%m/%d/%Y %H:%i:%s ')");
                    clause = clause.replace("@PROGENTIME@@ED_DATE", "str_to_date('" + pbTime.ed_d1 + "','%m/%d/%Y %H:%i:%s ')");
                } else if (connType.equalsIgnoreCase("postgress")) {
                    clause = clause.replace("@PROGENTIME@@ST_DATE", "to_timestamp('" + pbTime.st_d1 + "','%m/%d/%Y %H:%i:%s ')");
                    clause = clause.replace("@PROGENTIME@@ED_DATE", "to_timestamp('" + pbTime.ed_d1 + "','%m/%d/%Y %H:%i:%s ')");
                } else {
                    clause = clause.replace("@PROGENTIME@@ST_DATE", "convert(datetime,'" + pbTime.st_d1 + "',120)");
                    clause = clause.replace("@PROGENTIME@@ED_DATE", "convert(datetime,'" + pbTime.ed_d1 + "',120)");
                }
            }

        } else if (tableId == null || TimetableOtherValues == null || TimetableOtherValues.get(tableId) == null || ((ArrayList) TimetableOtherValues.get(tableId)).size() == 0) {
            if (type.equalsIgnoreCase("C")) {

                if (connType.equalsIgnoreCase("ORACLE")) {
                    clause = clause.replace("@PROGENTIME@@ST_DATE", "to_date('" + pbTime.st_d + "','mm/dd/yyyy Hh24:mi:ss ')");
                    clause = clause.replace("@PROGENTIME@@ED_DATE", "to_date('" + pbTime.ed_d + "','mm/dd/yyyy Hh24:mi:ss ')");
                } else if (connType.equalsIgnoreCase("mysql")) {
                    clause = clause.replace("@PROGENTIME@@ST_DATE", "str_to_date('" + pbTime.st_d + "','%m/%d/%Y %H:%i:%s ')");
                    clause = clause.replace("@PROGENTIME@@ED_DATE", "str_to_date('" + pbTime.ed_d + "','%m/%d/%Y %H:%i:%s ')");
                } else if (connType.equalsIgnoreCase("postgress")) {
                    clause = clause.replace("@PROGENTIME@@ST_DATE", "to_timestamp('" + pbTime.st_d + "','%m/%d/%Y %H:%i:%s ')");
                    clause = clause.replace("@PROGENTIME@@ED_DATE", "to_timestamp('" + pbTime.ed_d + "','%m/%d/%Y %H:%i:%s ')");
                } else {
                    clause = clause.replace("@PROGENTIME@@ST_DATE", "convert(datetime,'" + pbTime.st_d + "',120)");
                    clause = clause.replace("@PROGENTIME@@ED_DATE", "convert(datetime,'" + pbTime.ed_d + "',120)");
                }
            } else {
                if (connType.equalsIgnoreCase("ORACLE")) {
                    clause = clause.replace("@PROGENTIME@@ST_DATE", "to_date('" + pbTime.p_st_d + "','mm/dd/yyyy Hh24:mi:ss ')");
                    clause = clause.replace("@PROGENTIME@@ED_DATE", "to_date('" + pbTime.p_ed_d + "','mm/dd/yyyy Hh24:mi:ss ')");
                } else if (connType.equalsIgnoreCase("mysql")) {
                    clause = clause.replace("@PROGENTIME@@ST_DATE", "str_to_date('" + pbTime.p_st_d + "','%m/%d/%Y %H:%i:%s ')");
                    clause = clause.replace("@PROGENTIME@@ED_DATE", "str_to_date('" + pbTime.p_ed_d + "','%m/%d/%Y %H:%i:%s ')");
                } else if (connType.equalsIgnoreCase("postgress")) {
                    clause = clause.replace("@PROGENTIME@@ST_DATE", "to_timestamp('" + pbTime.p_st_d + "','%m/%d/%Y %H:%i:%s ')");
                    clause = clause.replace("@PROGENTIME@@ED_DATE", "to_timestamp('" + pbTime.p_ed_d + "','%m/%d/%Y %H:%i:%s ')");
                } else {
                    clause = clause.replace("@PROGENTIME@@ST_DATE", "convert(datetime,'" + pbTime.p_st_d + "',120)");
                    clause = clause.replace("@PROGENTIME@@ED_DATE", "convert(datetime,'" + pbTime.p_ed_d + "',120)");
                }
            }
        } else {
            ArrayList timeInVal = (ArrayList) TimetableOtherValues.get(tableId);
            if (type.equalsIgnoreCase("C")) {
                if (connType.equalsIgnoreCase("ORACLE")) {
                    clause = clause.replace("@PROGENTIME@@ST_DATE", "to_date('" + timeInVal.get(0) + "','mm/dd/yyyy Hh24:mi:ss ')");
                    clause = clause.replace("@PROGENTIME@@ED_DATE", "to_date('" + timeInVal.get(1) + "','mm/dd/yyyy Hh24:mi:ss ')");
                } else if (connType.equalsIgnoreCase("mysql")) {
                    clause = clause.replace("@PROGENTIME@@ST_DATE", "str_to_date('" + timeInVal.get(0) + "','%m/%d/%Y %H:%i:%s ')");
                    clause = clause.replace("@PROGENTIME@@ED_DATE", "str_to_date('" + timeInVal.get(1) + "','%m/%d/%Y %H:%i:%s ')");
                } else if (connType.equalsIgnoreCase("postgress")) {
                    clause = clause.replace("@PROGENTIME@@ST_DATE", "to_timestamp('" + timeInVal.get(0) + "','%m/%d/%Y %H:%i:%s ')");
                    clause = clause.replace("@PROGENTIME@@ED_DATE", "to_timestamp('" + timeInVal.get(1) + "','%m/%d/%Y %H:%i:%s ')");
                } else {
                    clause = clause.replace("@PROGENTIME@@ST_DATE", "convert(datetime,'" + timeInVal.get(0) + "',120)");
                    clause = clause.replace("@PROGENTIME@@ED_DATE", "convert(datetime,'" + timeInVal.get(1) + "',120)");
                }
            } else {
                if (connType.equalsIgnoreCase("ORACLE")) {
                    clause = clause.replace("@PROGENTIME@@ST_DATE", "to_date('" + timeInVal.get(2) + "','mm/dd/yyyy Hh24:mi:ss ')");
                    clause = clause.replace("@PROGENTIME@@ED_DATE", "to_date('" + timeInVal.get(3) + "','mm/dd/yyyy Hh24:mi:ss ')");
                } else if (connType.equalsIgnoreCase("mysql")) {
                    clause = clause.replace("@PROGENTIME@@ST_DATE", "str_to_date('" + timeInVal.get(2) + "','%m/%d/%Y %H:%i:%s ')");
                    clause = clause.replace("@PROGENTIME@@ED_DATE", "str_to_date('" + timeInVal.get(3) + "','%m/%d/%Y %H:%i:%s ')");
                } else if (connType.equalsIgnoreCase("postgress")) {
                    clause = clause.replace("@PROGENTIME@@ST_DATE", "to_timestamp('" + timeInVal.get(2) + "','%m/%d/%Y %H:%i:%s ')");
                    clause = clause.replace("@PROGENTIME@@ED_DATE", "to_timestamp('" + timeInVal.get(3) + "','%m/%d/%Y %H:%i:%s ')");
                } else {
                    clause = clause.replace("@PROGENTIME@@ST_DATE", "convert(datetime,'" + timeInVal.get(2) + "',120)");
                    clause = clause.replace("@PROGENTIME@@ED_DATE", "convert(datetime,'" + timeInVal.get(3) + "',120)");
                }
            }
        }
        return clause;
    }

    public String getSecFactFilter(String userId, String reportId, String tableId) throws SQLException {
        String tabFilter = "";
        String filterTypeDate = "";
//        ProgenLog.log(ProgenLog.FINE, this, "getSecFactFilter", "Enter ");
        logger.info("Enter ");
        PbReturnObject retObj;
        String[] colNames = null;
        String aList = "";


//        String sqlstr = "";                                                               //cbram
//        sqlstr += "select sec_clause_1 ,sec_clause_2,sec_clause_3,sec_clause_4,sec_clause_5, buss_tab_col_name ,sec_key_name ";
//        sqlstr += " ";
//        sqlstr += " from PRG_SEC_GRP_ROLE_USER_VAR  ";
//        sqlstr += "  M ";
//        sqlstr += " where buss_table_id = ( " + tableId + ") and folder_id = -1 "; //Add folder sepcific code too

        StringBuilder sqlstr = new StringBuilder();
        sqlstr.append("select sec_clause_1 ,sec_clause_2,sec_clause_3,sec_clause_4,sec_clause_5, buss_tab_col_name ,sec_key_name ");
        sqlstr.append(" ").append(" from PRG_SEC_GRP_ROLE_USER_VAR   M where buss_table_id = ( ").append(tableId).append(") and folder_id = -1 ");//Add folder sepcific code too
//

        //    retObj = pbDb.execSelectSQL(sqlstr);//cbram
        retObj = pbDb.execSelectSQL(sqlstr.toString());

        int psize = retObj.getRowCount();

        if (psize > 0) {
            colNames = retObj.getColumnNames();
            String filterFromTable = null;

            //Looping twice
            //Loop 1 find the fact and current and prior cols
            //loop 2 build query
            for (int looper = 0; looper < psize; looper++) {
                if (retObj.getFieldValue(looper, 0) != null && !retObj.getFieldValueString(looper, 0).trim().equals("")) {
                    if (looper == 0) {
                        filterFromTable = " ( 1= 1  and ";
                        filterFromTable = filterFromTable + retObj.getFieldValueString(looper, colNames[5]);
                        filterFromTable = filterFromTable + " in ( " + retObj.getFieldValueString(looper, colNames[0]);
                        filterFromTable = filterFromTable + " ) ";

                    } else {

                        filterFromTable = " and " + filterFromTable + retObj.getFieldValueString(looper, colNames[5]);
                        filterFromTable = filterFromTable + " in ( " + retObj.getFieldValueString(looper, colNames[0]);
                        filterFromTable = filterFromTable + " ) ";

                    }
                }
                //
            }
            if (filterFromTable != null && !filterFromTable.trim().equals("")) {
                filterFromTable = " and " + filterFromTable + " ) ";
                tabFilter = filterFromTable;
                tabFilter = tabFilter.replace("@@PROGEN_GBL_VAR@@USER_ID", userId);
                //
                //
                aList = (tabFilter);
            } else {
                aList = aList;
            }


//


        } else {
            aList = aList + (tabFilter);
        }
//        ProgenLog.log(ProgenLog.FINE, this, "getSecFactFilter", "Exit " + tabFilter);
        logger.info("Exit " + tabFilter);
        return (aList);
//        return (tabFilter);
    }

    public String getTrendTime(String mTemp, String userConnType) {

        if (qryType.equalsIgnoreCase("Trend")) {
            if (timeDetails.get(0).toString().equalsIgnoreCase("Day")) {
                if (timeDetails.get(1).toString().equalsIgnoreCase("PRG_STD") || timeDetails.get(1).toString().equalsIgnoreCase("PRG_COHORT")) {
                    int cPos = 3;
                    if (timeDetails.get(1).toString().equalsIgnoreCase("PRG_STD")) {
                        cPos = 3;
                    } else if (timeDetails.get(1).toString().equalsIgnoreCase("PRG_COHORT")) {
                        cPos = 6;
                    }
                    if (timeDetails.get(cPos).toString().equalsIgnoreCase("Week")) {
                        mTemp = mTemp.replace("@PROGENTIME@@ST_DATE", "PROGEN_TIME.PW_ST_DATE");
                        mTemp = mTemp.replace("@PROGENTIME@@ED_DATE", "PROGEN_TIME.PW_END_DATE");
                    } else if (timeDetails.get(cPos).toString().equalsIgnoreCase("Month")) {
                        mTemp = mTemp.replace("@PROGENTIME@@ST_DATE", "PROGEN_TIME.PM_ST_DATE");
                        mTemp = mTemp.replace("@PROGENTIME@@ED_DATE", "PROGEN_TIME.PM_END_DATE");
                    } else if (timeDetails.get(cPos).toString().equalsIgnoreCase("Qtr")) {
                        mTemp = mTemp.replace("@PROGENTIME@@ST_DATE", "PROGEN_TIME.LQ_ST_DATE");
                        mTemp = mTemp.replace("@PROGENTIME@@ED_DATE", "PROGEN_TIME.LQ_END_DATE");
                    } else if (timeDetails.get(cPos).toString().equalsIgnoreCase("Year")) {
                        mTemp = mTemp.replace("@PROGENTIME@@ST_DATE", "PROGEN_TIME.LY_ST_DATE");
                        mTemp = mTemp.replace("@PROGENTIME@@ED_DATE", "PROGEN_TIME.LY_END_DATE");
                    } else {
                        mTemp = mTemp.replace("@PROGENTIME@@ST_DATE", "(PROGEN_TIME.DDATE -1)");
                        mTemp = mTemp.replace("@PROGENTIME@@ED_DATE", "(PROGEN_TIME.DDATE -1)");
                    }
                } else {
                    mTemp = mTemp.replace("@PROGENTIME@@ST_DATE", "(PROGEN_TIME.DDATE -1)");
                    mTemp = mTemp.replace("@PROGENTIME@@ED_DATE", "(PROGEN_TIME.DDATE -1)");
                }
            } else {
                if (userConnType.equalsIgnoreCase("ORACLE")) {
                    mTemp = mTemp.replace("@PROGENTIME@@ST_DATE", "to_date('" + pbTime.p_st_d + "','mm/dd/yyyy Hh24:mi:ss ')");
                    mTemp = mTemp.replace("@PROGENTIME@@ED_DATE", "to_date('" + pbTime.p_ed_d + "','mm/dd/yyyy Hh24:mi:ss ')");
                } else if (userConnType.equalsIgnoreCase("mysql")) {
                    mTemp = mTemp.replace("@PROGENTIME@@ST_DATE", "str_to_date('" + pbTime.p_st_d + "','%m/%d/%Y %H:%i:%s ')");
                    mTemp = mTemp.replace("@PROGENTIME@@ED_DATE", "str_to_date('" + pbTime.p_ed_d + "','%m/%d/%Y %H:%i:%s ')");
                } else {
                    mTemp = mTemp.replace("@PROGENTIME@@ST_DATE", "convert(datetime,'" + pbTime.p_st_d + "',120)");
                    mTemp = mTemp.replace("@PROGENTIME@@ED_DATE", "convert(datetime,'" + pbTime.p_ed_d + "',120)");
                }
            }
        } else {
            if (userConnType.equalsIgnoreCase("ORACLE")) {
                mTemp = mTemp.replace("@PROGENTIME@@ST_DATE", "to_date('" + pbTime.p_st_d + "','mm/dd/yyyy Hh24:mi:ss ')");
                mTemp = mTemp.replace("@PROGENTIME@@ED_DATE", "to_date('" + pbTime.p_ed_d + "','mm/dd/yyyy Hh24:mi:ss ')");
            } else if (userConnType.equalsIgnoreCase("mysql")) {
                mTemp = mTemp.replace("@PROGENTIME@@ST_DATE", "str_to_date('" + pbTime.p_st_d + "','%m/%d/%Y %H:%i:%s ')");
                mTemp = mTemp.replace("@PROGENTIME@@ED_DATE", "str_to_date('" + pbTime.p_ed_d + "','%m/%d/%Y %H:%i:%s ')");
            } else {
                mTemp = mTemp.replace("@PROGENTIME@@ST_DATE", "convert(datetime,'" + pbTime.p_st_d + "',120)");
                mTemp = mTemp.replace("@PROGENTIME@@ED_DATE", "convert(datetime,'" + pbTime.p_ed_d + "',120)");
            }
        }
        return mTemp;
    }

    //end of Nazneen Code
    public PbReturnObject getPbReturnObjectWithFlag(String elementId) {
        PbReturnObject retObj = null;
        Connection conn = null;
        try {
//            ProgenLog.log(ProgenLog.FINE, this, "getPbReturnObjectWithFlag", "Entered calling generateViewByQry");
            String sql = generateViewByQry();
            //session.setAttribute("sqlStr", sql);
            this.reportQuery = sql;
            // System.out.println("sql in getPbReturnObject iwthflag is " + sql);
//            ProgenLog.log(ProgenLog.FINE, this, "getPbReturnObjectWithFlag", "generateViewByQry " + sql);
            conn = getConnection(elementId);
            retObj = pbDb.execSelectSQLWithFlag(sql, conn);
//            ProgenLog.log(ProgenLog.FINE, this, "getPbReturnObjectWithFlag", "Return");
        } catch (Exception exp) {
            logger.error("Exception", exp);
        } finally {
//
//            if (conn != null) {
//                try {
//                    conn.close();
//                } catch (SQLException ex) {
//                    logger.error("Exception:",ex);
//                }
//            }
        }
        return retObj;
    }

    public PbReturnObject getPbReturnObjectWithFlag(String elementId, String sql) {
        PbReturnObject retObj = null;
        Connection conn = null;
        try {
//            ProgenLog.log(ProgenLog.FINE, this, "getPbReturnObjectWithFlag", "Entered calling getPbReturnObjectWithFlag");
            //session.setAttribute("sqlStr", sql);
            this.reportQuery = sql;
            // System.out.println("sql in getPbReturnObject iwthflag is " + sql);
//            ProgenLog.log(ProgenLog.FINE, this, "getPbReturnObjectWithFlag", "Execute " + sql);
            conn = getConnection(elementId);
            retObj = pbDb.execSelectSQLWithFlag(sql, conn);
//            ProgenLog.log(ProgenLog.FINE, this, "getPbReturnObjectWithFlag", "Return");
            logger.info("Return");
        } catch (Exception exp) {
            logger.error("Exception", exp);
        } finally {
//
//            if (conn != null) {
//                try {
//                    conn.close();
//                } catch (SQLException ex) {
//                    logger.error("Exception:",ex);
//                }
//            }
        }
        return retObj;
    }

    public String getGeneratedQuery() {
        return this.reportQuery;
    }

    //added by k
    public PbReturnObject getPbReturnObjectWithFlag1(String elementId) {
        PbReturnObject retObj = null;
        Connection conn = null;
        try {
//            ProgenLog.log(ProgenLog.FINE, this, "getPbReturnObjectWithFlag1", "Entered-- calling generateViewByQry");

            String sql = generateViewByQry();
            //  System.out.println("outputquery" + sql);
            conn = getConnection(elementId);
//            ProgenLog.log(ProgenLog.FINE, this, "getPbReturnObjectWithFlag1", "sql is: " + sql);
            retObj = pbDb.execSelectSQLWithFlag(sql, conn);

//            ProgenLog.log(ProgenLog.FINE, this, "getPbReturnObjectWithFlag1", "Exit");

        } catch (Exception exp) {
            logger.error("Exception", exp);
        } finally {
//            if (conn != null) {
//                try {
//                    conn.close();
//                } catch (SQLException ex) {
//                    logger.error("Exception:",ex);
//                }
//            }
        }
        return retObj;
    }

    /*
     *
     * ArrayList allTablesforReplace = new ArrayList(); HashMap<String,
     * ArrayList> allTablesforReplaceColumns = new HashMap(); HashMap<String,
     * ArrayList> allTablesforReplacementTables= new HashMap();
     *
     */
    public String ArrayToCommaString(List<String> measureIdsList) {
        String collecror = "";
        for (int loop = 0; loop < measureIdsList.size(); loop++) {
            if (loop == 0) {
                collecror += measureIdsList.get(loop);
            } else {
                collecror += ", " + measureIdsList.get(loop);
            }
        }

        return collecror;
    }

    public String ArrayToCommaString(List<String> measureIdsList, String Enclosed) {
        String collecror = "";
        for (int loop = 0; loop < measureIdsList.size(); loop++) {
            if (loop == 0) {
                collecror += Enclosed + measureIdsList.get(loop).trim() + Enclosed;
            } else {
                collecror += ", " + Enclosed + measureIdsList.get(loop).trim() + Enclosed;
            }
        }
        logger.info("collecror---------------" + collecror);
        return collecror;
    }

    public String getDBMode(String Quo, String Div, String DB) {
        //      String clause = "";//cbram
        StringBuilder clause = new StringBuilder();
        if (DB.equalsIgnoreCase("SqlServer")) {
            //   clause =" (" + Quo + "%" + Div + ")";//cbram
            clause.append(" (").append(Quo).append("%").append(Div).append(")");
        } else if (DB.equalsIgnoreCase("ORACLE")) {
            //    clause =" mod(" + Quo + "," + Div + ")";//cbram
            clause.append(" mod(").append(Quo).append(",").append(Div).append(")");
        } else if (DB.equalsIgnoreCase("Mysql")) {
            //       clause =" mod(" + Quo + "," + Div + ")";  //cbram
            clause.append(" mod(").append(Quo).append(",").append(Div).append(")");
        }
        //  return clause;//cbram
        return clause.toString();
    }

    public void updateAggregateTableList(String factTable, String tableList, ArrayList allTablesforReplace, HashMap<String, ArrayList> allTablesforReplaceColumns, HashMap<String, HashMap> allTablesforReplacementTables, String Date) throws SQLException, Exception {
        PbReturnObject retObj;
        PbReturnObject retObj1;
        String[] colNames = null;
        String colName = null;
        String minLevel = null;
        ArrayList distinctTableList = new ArrayList();
        ArrayList<Integer> distinctTableListMuti = new ArrayList();
        ArrayList<Integer> distinctTableListMaxAgg = new ArrayList();
        ArrayList<String> AllTableColumns = new ArrayList();
        int finalNumber = 0;
        String newTableList = "'-1'";
        boolean getMaxTable = false;
             String additionalClause = "" ;
//        StringBuilder additionalClause = new StringBuilder();
        String maxTableAggNumber = "99999999";
        String finalFactName = factTable;
        HashMap newTables = new HashMap();
        String connType = ProgenConnection.getInstance().getDatabaseType();

//        System.out.println(" factTable "+ factTable);
//        System.out.println(" tableList "+ tableList);
//        System.out.println(" allTablesforReplace "+ allTablesforReplace);
//        System.out.println(" allTablesforReplaceColumns "+ allTablesforReplaceColumns);

        logger.info("allTablesforReplace" + allTablesforReplace);
        logger.info("allTablesforReplacementTables" + allTablesforReplacementTables);
        logger.info("tableList" + tableList); // Do we need to use this
        logger.info("factTable" + factTable);



        // Do we need to use this

        String sqlstr = "";

        sqlstr += "select Dim_table_name, Agg_dim_id, company_id, Sequnece_no, Mutiple_no , Sequnece_no*Mutiple_no Agg_pos, Division_no ";
        sqlstr += " ";
        sqlstr += " from prg_agg_master  ";
        sqlstr += "  M ";
        sqlstr += " where fact_table_name = ( '" + factTable + "') ";
        sqlstr += " and Dim_table_name in ( " + ArrayToCommaString(allTablesforReplace, "'")  + ") ";
        //sqlstr += " and (company_id = ( '" + tableList + "') or company_id) is null ";
        // sqlstr += " and Agg_date = ( '" + Date + "') "; -- Will Enable Later

//        StringBuilder sqlstr = new StringBuilder();
//        sqlstr.append("select Dim_table_name, Agg_dim_id, company_id, Sequnece_no, Mutiple_no , Sequnece_no*Mutiple_no Agg_pos, Division_no ").append(" ").append(" from prg_agg_master  ");
//        sqlstr.append("  M ").append(" where fact_table_name = ( '").append(factTable).append("') ").append(" and Dim_table_name in ( ").append(ArrayToCommaString(allTablesforReplace, "'")).append(") ");
//        ;
        System.out.println(" updateAggregateTableList " + sqlstr);

        logger.info("First Query " + sqlstr);
            retObj = pbDb.execSelectSQL(sqlstr);
//        retObj = pbDb.execSelectSQL(sqlstr.toString());

        colNames = retObj.getColumnNames();


        int MaxNum = 9;
        int psize = retObj.getRowCount();
        if (psize == 0) {
            getMaxTable = true;
        }

        if (psize > 0 && psize >= allTablesforReplace.size()) {
            //Looping twice
            //Loop 1 find the fact and current and prior cols
            //loop 2 build query
            for (int looper = 0; looper < psize; looper++) {
                if (distinctTableList == null) {
                    logger.info(" I" + looper + " In First");
                    distinctTableList.add(retObj.getFieldValueString(looper, colNames[0]));
                    distinctTableListMuti.add(retObj.getFieldValueInt(looper, colNames[4]));

                    if (allTablesforReplaceColumns.get(retObj.getFieldValueString(looper, colNames[0])) != null) {
                        AllTableColumns = allTablesforReplaceColumns.get(retObj.getFieldValueString(looper, colNames[0]));
                        MaxNum = getMaxNumforEachTable(AllTableColumns, retObj.getFieldValueString(looper, colNames[0]), retObj.getFieldValueString(looper, colNames[1]));
                        distinctTableListMaxAgg.add(MaxNum);
                        logger.info(" I" + looper + " MaxNum " + MaxNum);
                        //distinctTableListMuti.add(retObj.getFieldValueInt(looper, colNames[4]));
                        finalNumber += retObj.getFieldValueInt(looper, colNames[4]) * MaxNum;
                        logger.info(" I" + looper + " finalNumber " + finalNumber);
                    } else {
                        getMaxTable = true;
                        break;
                    }
                    //additionalClause += " and  mod(Agg_number, "+retObj.getFieldValueInt(looper, colNames[4]) + ")"
//                    additionalClause += " and (( mod(Agg_number,"+retObj.getFieldValueInt(looper, colNames[6]) + ")"
//                            + " -  mod(Agg_number,"+retObj.getFieldValueInt(looper, colNames[4]) + ")) "
//                            //       + " /  (Agg_number%"+retObj.getFieldValueInt(looper, colNames[4]) + ")) "
//                            + " /  ("+retObj.getFieldValueInt(looper, colNames[4]) + ")) "
//                            + " > = " + MaxNum + " ";
                            additionalClause +=  " and ((( "+getDBMode("Agg_number",retObj.getFieldValueString(looper, colNames[6]),connType)+")"
                            + " -  "
                            + " ( "+getDBMode("Agg_number",retObj.getFieldValueString(looper, colNames[4]),connType)+")"
                            + ") "
                            + " /  ("+retObj.getFieldValueInt(looper, colNames[4]) + ")) "
                            + " > = " + MaxNum + " ";

//                    additionalClause.append(" and ((( ").append(getDBMode("Agg_number", retObj.getFieldValueString(looper, colNames[6]), connType)).append(")").append(" -  ");
//                    additionalClause.append(" ( ").append(getDBMode("Agg_number", retObj.getFieldValueString(looper, colNames[4]), connType)).append(")").append(") ");
//                    additionalClause.append(" /  (").append(retObj.getFieldValueInt(looper, colNames[4])).append(")) ").append(" > = ").append(MaxNum).append(" ");
                    System.out.println(" Mutti " + retObj.getFieldValueInt(looper, colNames[4]));
                    System.out.println(" MaxNum " + MaxNum);

                } else if (!distinctTableList.contains(retObj.getFieldValueString(looper, colNames[0]))) {
                    logger.info(" I" + looper + " Second " + finalNumber);
                    distinctTableList.add(retObj.getFieldValueString(looper, colNames[0]));
                    distinctTableListMuti.add(retObj.getFieldValueInt(looper, colNames[4]));
                    if (allTablesforReplaceColumns.get(retObj.getFieldValueString(looper, colNames[0])) != null) {
                        AllTableColumns = allTablesforReplaceColumns.get(retObj.getFieldValueString(looper, colNames[0]));
                        MaxNum = getMaxNumforEachTable(AllTableColumns, retObj.getFieldValueString(looper, colNames[0]), retObj.getFieldValueString(looper, colNames[1]));
                        distinctTableListMaxAgg.add(MaxNum);
                        logger.info(" I" + looper + " MaxNum " + MaxNum);
                        //distinctTableListMuti.add(retObj.getFieldValueInt(looper, colNames[4]));
                        finalNumber += retObj.getFieldValueInt(looper, colNames[4]) * MaxNum;
                        logger.info(" I" + looper + " finalNumber " + finalNumber);
                    } else {
                        getMaxTable = true;
                        break;
                    }
                    //additionalClause += " and  mod(Agg_number, "+retObj.getFieldValueInt(looper, colNames[4]) + ")"
//                    additionalClause += " and (( mod(Agg_number,"+retObj.getFieldValueInt(looper, colNames[6]) + ")"
//                            + " -  mod(Agg_number,"+retObj.getFieldValueInt(looper, colNames[4]) + ")) "
//                     //       + " /  (Agg_number%"+retObj.getFieldValueInt(looper, colNames[4]) + ")) "
//                            + " /  ("+retObj.getFieldValueInt(looper, colNames[4]) + ")) "
//                            + " > = " + MaxNum + " ";
                            additionalClause +=  " and ((( "+getDBMode("Agg_number",retObj.getFieldValueString(looper, colNames[6]),connType)+")"
                            + " -  "
                            + " ( "+getDBMode("Agg_number",retObj.getFieldValueString(looper, colNames[4]),connType)+")"
                            + ") "
                            + " /  ("+retObj.getFieldValueInt(looper, colNames[4]) + ")) "
                            + " > = " + MaxNum + " ";

//                    additionalClause.append(" and ((( ").append(getDBMode("Agg_number", retObj.getFieldValueString(looper, colNames[6]), connType)).append(")").append(" -  ");
//                    additionalClause.append(" ( ").append(getDBMode("Agg_number", retObj.getFieldValueString(looper, colNames[4]), connType)).append(")");
//                    additionalClause.append(") ").append(" /  (").append(retObj.getFieldValueInt(looper, colNames[4])).append(")) ").append(" > = ").append(MaxNum).append(" ");
                    System.out.println(" Mutti " + retObj.getFieldValueInt(looper, colNames[4]));
                    System.out.println(" MaxNum " + MaxNum);
                    //distinctTableListMuti.add(retObj.getFieldValueInt(looper, colNames[4]));
                }
                newTableList += " ,'" + retObj.getFieldValueString(looper, colNames[0]) + "' ";

            }
            //if(retObj.getFieldValue(0, colNames[3])!=null)

        } else {
            getMaxTable = true;
            logger.info(" Failed size is less record are 0" + psize);
        }
        logger.info("getMaxTable " + getMaxTable);

        ////////////////   Call Code for each Table
              sqlstr = "";//cbram
//        sqlstr = new StringBuilder();
        if (getMaxTable) {
            sqlstr += "select Agg_number,Agg_choose_table, fact_table_name, Agg_choose_id  ";
            sqlstr += " ";
            sqlstr += " from prg_choose_agg  ";
            sqlstr += "  M ";
            sqlstr += " where fact_table_name = ( '" + factTable + "') ";
           //sqlstr += " and (company_id = ( '" + tableList + "') or company_id) is null ";
            sqlstr += " and Agg_Number in ( select max(Agg_Number) ";
            sqlstr += " from prg_choose_agg  ";
            sqlstr += "  M ";
            sqlstr += " where fact_table_name = ( '" + factTable + "') ";
           //sqlstr += " and (company_id = ( '" + tableList + "') or company_id) is null ";
            sqlstr += " )";

//            sqlstr.append("select Agg_number,Agg_choose_table, fact_table_name, Agg_choose_id  ").append(" ").append(" from prg_choose_agg  ").append("  M ").append(" where fact_table_name = ( '");
//            sqlstr.append(factTable).append("') ").append(" and Agg_Number in ( select max(Agg_Number) ");
//            sqlstr.append(factTable).append("') ").append(" )");

        } else {                                   //cbram
            sqlstr += "select Agg_number,Agg_choose_table, fact_table_name, Agg_choose_id  ";
            sqlstr += " ";
            sqlstr += " from prg_choose_agg  ";
            sqlstr += "  M ";
            sqlstr += " where fact_table_name = ( '" + factTable + "') ";
           //sqlstr += " and (company_id = ( '" + tableList + "') or company_id) is null ";
            sqlstr += " and Agg_Number in ( select min(Agg_Number) ";
            sqlstr += " from prg_choose_agg  ";
            sqlstr += "  M ";
            sqlstr += " where fact_table_name = ( '" + factTable + "') ";
           //sqlstr += " and (company_id = ( '" + tableList + "') or company_id) is null ";
            sqlstr +=  " and Agg_number >= " + finalNumber + additionalClause + " " ;
            sqlstr += " )";
//            sqlstr.append("select Agg_number,Agg_choose_table, fact_table_name, Agg_choose_id  ").append(" ").append(" from prg_choose_agg  ").append("  M ").append(" where fact_table_name = ( '").append(factTable).append("') ");
//            sqlstr.append(" and Agg_Number in ( select min(Agg_Number) ").append(" from prg_choose_agg  ").append("  M ").append(" where fact_table_name = ( '").append("  M ").append(" where fact_table_name = ( '");
//            sqlstr.append(factTable).append("') ").append(" and Agg_number >= ").append(finalNumber).append(additionalClause).append(" ").append(" )");
        }
        ;
        logger.info("Second  Query " + sqlstr.toString());
        logger.info("Second  Query >>>>>>>>>>>>>> " + sqlstr.toString());




        //  retObj1 = pbDb.execSelectSQL(sqlstr);//cbram
        retObj1 = pbDb.execSelectSQL(sqlstr.toString());

        colNames = retObj1.getColumnNames();



        int psize1 = retObj1.getRowCount();
        String AggHeaderId = new String();
        if (psize1 > 0) {
            //Looping twice
            //Loop 1 find the fact and current and prior cols
            //loop 2 build query
            for (int looper = 0; looper < psize1; looper++) {

                maxTableAggNumber = (retObj1.getFieldValueString(looper, colNames[0]));
                finalFactName = (retObj1.getFieldValueString(looper, colNames[1]));
                AggHeaderId = (retObj1.getFieldValueString(looper, colNames[3]));
            }
            //if(retObj.getFieldValue(0, colNames[3])!=null)



        }


        newTables.put(factTable, finalFactName);

        addAllTablesforReplacementTables(allTablesforReplacementTables, newTables, factTable);

        if (getMaxTable) {
            return;
        }
        getFinalListtoreplace(allTablesforReplacementTables, maxTableAggNumber, factTable, AggHeaderId);

        logger.info(" Out factTable " + factTable);
        logger.info(" Out tableList " + tableList);
        logger.info(" Out allTablesforReplace " + allTablesforReplace);
        logger.info(" Out allTablesforReplaceColumns " + allTablesforReplaceColumns);

    }

    public void getFinalListtoreplace(HashMap<String, HashMap> allTablesforReplacementTables, String maxTableAggNumber, String finalFactName, String AggHeaderId) throws SQLException {

        // ProgenLog.log(ProgenLog.FINE, this, "getAdditionWhereClause", "Enter ");
        logger.info(" finalFactName " + finalFactName);
        logger.info(" AggHeaderId " + AggHeaderId);
        logger.info(" maxTableAggNumber " + maxTableAggNumber);
        logger.info(" allTablesforReplacementTables " + allTablesforReplacementTables);
        PbReturnObject retObj;
        String[] colNames = null;
        int colName = 9;
        String minLevel = null;
        String TableColumnNames = "'-1'";
        HashMap newTables = new HashMap();


        String sqlstr = "";
        sqlstr += "select Dim_table , Dim_Choose_table , Agg_choose_detail_id, fact_table_name  ";
        sqlstr += " ";
        sqlstr += " from prg_choose_agg_details  ";
        sqlstr += "  M ";
        sqlstr += " where Agg_choose_id in ( " + AggHeaderId + ") ";
        sqlstr += " and fact_table_name in ( '" + finalFactName + "')  ";
        sqlstr += " and Agg_number = ( '" + maxTableAggNumber + "')  ";

//        StringBuilder sqlstr = new StringBuilder();
//        sqlstr.append("select Dim_table , Dim_Choose_table , Agg_choose_detail_id, fact_table_name  ").append(" ").append(" from prg_choose_agg_details  ");
//        sqlstr.append("  M ").append(" where Agg_choose_id in ( ").append(AggHeaderId).append(") ").append(" and fact_table_name in ( '").append(finalFactName).append("')  ");
//        sqlstr.append(" and Agg_number = ( '").append(maxTableAggNumber).append("')  ");
        System.out.println(" getFinalListtoreplace sqlstr " + sqlstr);


        logger.info("Third Query " + sqlstr);
        retObj = pbDb.execSelectSQL(sqlstr.toString());

        colNames = retObj.getColumnNames();



        int psize = retObj.getRowCount();

        if (psize > 0) {
            //Looping twice
            //Loop 1 find the fact and current and prior cols
            //loop 2 build query
            for (int looper = 0; looper < psize; looper++) {

                newTables.put(retObj.getFieldValueString(looper, colNames[0]), retObj.getFieldValueString(looper, colNames[1]));

            }
            //if(retObj.getFieldValue(0, colNames[3])!=null)
            logger.info(" calling Insert for Hash Map");
            addAllTablesforReplacementTables(allTablesforReplacementTables, newTables, finalFactName);

        }

        logger.info("allTablesforReplacementTables" + allTablesforReplacementTables);
//        ProgenLog.log(ProgenLog.FINE, this, "getFinalListtoreplace", "Exit " + allTablesforReplacementTables);
        logger.info("Exit " + allTablesforReplacementTables);

    }

    public void addAllTablesforReplacementTables(HashMap<String, HashMap> allTablesforReplacementTables, HashMap addTables, String factTable) {
        logger.info(" From add to hash " + addTables);
        if (allTablesforReplacementTables == null || allTablesforReplacementTables.isEmpty()) {
            allTablesforReplacementTables.put(factTable, addTables);
        } else if (allTablesforReplacementTables.get(factTable) != null) {
            HashMap a = allTablesforReplacementTables.get(factTable);

            List<String> keys = new ArrayList(addTables.keySet());
            for (String key : keys) {
                a.put(key, addTables.get(key));
            }

            allTablesforReplacementTables.put(factTable, a);
        } //a.put(factTable, addTables);
        logger.info("From Function allTablesforReplacementTables" + allTablesforReplacementTables);
    }
//allTablesforReplacementTables.put(factTable,addTables);

    public int getMaxNumforEachTable(ArrayList Columns, String TableName, String DimTabID) throws Exception {

//        ProgenLog.log(ProgenLog.FINE, this, "getMaxNumforEachTable", "Enter ");
        logger.info("Enter ");
        PbReturnObject retObj;
        String[] colNames = null;
        int colName = 9;
        String minLevel = null;
        String TableColumnNames = "'-1'";
        logger.info(" In Max ");
        for (int k = 0; k < Columns.size(); k++) {
            TableColumnNames += " ,'" + Columns.get(k) + "' ";
        }

//        String sqlstr = "";                                         //cbram
//        sqlstr += "select max(Assigned_level) Assigned_level  ";
//        sqlstr += " ";
//        sqlstr += " from prg_agg_details  ";
//        sqlstr += "  M ";
//        sqlstr += " where Agg_dim_id in ( '" + DimTabID + "') ";
//        sqlstr += " and table_name in ( '" + TableName + "')  ";
//        sqlstr += " and column_name  in ( " + TableColumnNames + ")  ";

        StringBuilder sqlstr = new StringBuilder();
        sqlstr.append("select max(Assigned_level) Assigned_level  ").append(" ").append(" from prg_agg_details  ").append("  M ").append(" where Agg_dim_id in ( '");
        sqlstr.append(DimTabID).append("') ").append(" and table_name in ( '").append(TableName).append("')  ").append(" and column_name  in ( ").append(TableColumnNames).append(")  ");

        logger.info(" Max Table to replace " + sqlstr);

        //    retObj = pbDb.execSelectSQL(sqlstr);//cbram
        retObj = pbDb.execSelectSQL(sqlstr.toString());
        colNames = retObj.getColumnNames();



        int psize = retObj.getRowCount();
        logger.info(" psize for max Num " + psize);
        if (psize > 0) {
            //Looping twice
            //Loop 1 find the fact and current and prior cols
            //loop 2 build query
            for (int looper = 0; looper < psize; looper++) {
                logger.info("System value '" + retObj.getFieldValueString(0, colNames[0]) + "' ");
                if ((retObj.getFieldValueString(0, colNames[0]) != null
                        && !retObj.getFieldValueString(0, colNames[0]).equals(""))) {
                    colName = retObj.getFieldValueInt(0, colNames[0]);
                } else {
                    colName = 9;
                }
            }
            //if(retObj.getFieldValue(0, colNames[3])!=null)



        } else {
            colName = 9;
        }
        logger.info("Max col Number " + colName);
//        ProgenLog.log(ProgenLog.FINE, this, "getAdditionWhereClause", "Exit " + colName);
        logger.info("Exit " + colName);
        return (colName);
    }

    String getAdditionWhereClause(String whereClause) throws Exception {

//        ProgenLog.log(ProgenLog.FINE, this, "getAdditionWhereClause", "Enter ");
        logger.info("Enter ");
        PbReturnObject retObj;
        String[] colNames = null;
        String colName = null;
        String minLevel = null;

//        String sqlstr = "";                                          //cbram
//        sqlstr += "select REFFERED_ELEMENTS, ELEMENT_ID ";
//        sqlstr += " ";
//        sqlstr += " from PRG_USER_ALL_INFO_DETAILS  ";
//        sqlstr += "  M ";
//        sqlstr += " where ELEMENT_ID in ( " + whereClause + ") and REFFERED_ELEMENTS is not null ";

        StringBuilder sqlstr = new StringBuilder();
        sqlstr.append("select REFFERED_ELEMENTS, ELEMENT_ID ").append(" ").append(" from PRG_USER_ALL_INFO_DETAILS  ").append("  M ").append(" where ELEMENT_ID in ( ");
        sqlstr.append(whereClause).append(") and REFFERED_ELEMENTS is not null ");
        //       retObj = pbDb.execSelectSQL(sqlstr);//cbram
        retObj = pbDb.execSelectSQL(sqlstr.toString());
        colNames = retObj.getColumnNames();



        int psize = retObj.getRowCount();

        if (psize > 0) {
            //Looping twice
            //Loop 1 find the fact and current and prior cols
            //loop 2 build query
            for (int looper = 0; looper < psize; looper++) {
                if (looper == 0) {
                    colName = " " + retObj.getFieldValueString(0, colNames[0]);
                } else {
                    colName += " , " + retObj.getFieldValueString(0, colNames[0]);
                }
            }
            //if(retObj.getFieldValue(0, colNames[3])!=null)



        }

//        ProgenLog.log(ProgenLog.FINE, this, "getAdditionWhereClause", "Exit " + colName);
        logger.info("Exit " + colName);
        return (colName);
    }

    public Connection getConnection(String elementId) {
        Connection connection = null;
        try {
            connection = ProgenConnection.getInstance().getConnectionForElement(elementId);
        } catch (Exception e) {
            logger.error("Exception:", e);
        }
        return connection;
    }

    public void getTimeRange(String TimeFact) throws SQLException {
        //Code to get timeTablename using fact id

//        ProgenLog.log(ProgenLog.FINE, this, "getTimeRange", "Enter getTimeRange");
        logger.info("Enter getTimeRange");
        if (timeDetails.get(0).toString().equalsIgnoreCase("Day")) {
            if (timeDetails.get(1).toString().equalsIgnoreCase("PRG_STD")) {
                if (!qryType.equalsIgnoreCase("Trend")) {
                    pbTime.setRange(timeDetails.get(3).toString(), timeDetails.get(4).toString(), timeDetails.get(2).toString());
                } else {
                    pbTime.setTrendRange(timeDetails.get(3).toString(), timeDetails.get(4).toString(), timeDetails.get(2).toString(), isTimeDrill);

                }

            } else {
                //if(timeDetails.get(1).toString().equalsIgnoreCase("PRG_DATE_RANGE"))
                if (!qryType.equalsIgnoreCase("Trend")) {
                    pbTime.setRange(timeDetails);
                } else {
                    //pbTime.setTrendRange(timeDetails.get(3).toString(), timeDetails.get(4).toString(), timeDetails.get(2).toString(), isTimeDrill);
                    pbTime.setRange(timeDetails);
                }

            }
        } else if (timeDetails.get(0).toString().equalsIgnoreCase("Month")) {
            if (timeDetails.get(1).toString().equalsIgnoreCase("PRG_STD")) {
                String mon = new String();
                if (timeDetails.get(2) == null) {
                    mon = "NOV-08";
                } else {
                    mon = timeDetails.get(2).toString();
                }
                if (!qryType.equalsIgnoreCase("Trend")) {

                    pbTime.setMonthRange(String.valueOf(timeDetails.get(3)), timeDetails.get(4).toString(), mon);
                    //////07-12-09
                    pbTime.setTargetRange(timeDetails.get(3).toString(), timeDetails.get(4).toString(), timeDetails.get(2).toString());

                } else {
                    pbTime.setMonthTrendRange(String.valueOf(timeDetails.get(3)), timeDetails.get(4).toString(), mon);
                    pbTime.setTargetTrendRange(timeDetails.get(3).toString(), timeDetails.get(4).toString(), timeDetails.get(2).toString());
                }
                isDateTrueforNonDt = true;
            }
            if (timeDetails.get(1).toString().equalsIgnoreCase("PRG_MONTH_CMP")) {
                if (!qryType.equalsIgnoreCase("Trend")) {
                    pbTime.setStdMonthRange(String.valueOf(timeDetails.get(2)), String.valueOf(timeDetails.get(3)));
                } else {
                    pbTime.setStdMonthTrendRange(String.valueOf(timeDetails.get(2)), String.valueOf(timeDetails.get(3)));
                    isDateTrueforNonDt = true;
                }

            }
        } else if (timeDetails.get(0).toString().equalsIgnoreCase("YEAR")) {
            if (timeDetails.get(1).toString().equalsIgnoreCase("PRG_YEAR_CMP")) {

                if (!qryType.equalsIgnoreCase("Trend")) {
                    pbTime.setStdYearRange(String.valueOf(timeDetails.get(2)), String.valueOf(timeDetails.get(3)));
                    //////07-12-09
                    //pbTime.setTargetRange(timeDetails.get(3).toString(), timeDetails.get(4).toString(), timeDetails.get(2).toString());

                } else {
                    pbTime.setStdYearTrendRange(String.valueOf(timeDetails.get(2)), String.valueOf(timeDetails.get(3)));
                    pbTime.setTargetTrendRange(timeDetails.get(3).toString(), timeDetails.get(4).toString(), timeDetails.get(2).toString());

                }


            }
        }
//        ProgenLog.log(ProgenLog.FINE, this, "getTimeRange", "Exit");
        logger.info("Exit ");
    }

    public String getDefaultMeasure() {
        return defaultMeasure;
    }

    public void setDefaultMeasure(String defaultMeasure) {
        this.defaultMeasure = defaultMeasure;
    }

    public String getDefaultMeasureSumm() {
        return defaultMeasureSumm;
    }

    public void setDefaultMeasureSumm(String defaultMeasureSumm) {
        this.defaultMeasureSumm = defaultMeasureSumm;
    }

    public void getTimeViewByName(String AggType) {
    }

    /*
     * public static void main(String[] a) { ArrayList alist = new ArrayList();
     *
     * alist.add("1");
    }
     */
    public String exploreSummrizationInfo(String FactId, ArrayList Parameters) throws Exception {
        String whereClause = null;
        ArrayList dimIds = new ArrayList();
//        ArrayList dimTables = new ArrayList();
//        ArrayList ElementsIds = new ArrayList();
//        ArrayList ElementsLevel = new ArrayList();
//        ArrayList ElementsLevelCol = new ArrayList();
        ArrayList ElementsFactLevel = new ArrayList();
        ArrayList ElementsMaxFactLevel = new ArrayList();
//        ArrayList ElementsLevelFactCol = new ArrayList();
        String factWhere = "";
        String dimWhere = "";
        String factName = new String();
        String factCombinedkey = new String();
        //String timelevelsumm=null;
        boolean onlyTime = false;
        boolean hasTime = false;
        if (Parameters != null && Parameters.size() > 0) { //if 1
            for (int i = 0; i < Parameters.size(); i++) {/// for 1
                if (whereClause == null) {
                    whereClause = Parameters.get(i).toString();
                } else {
                    whereClause += " , " + Parameters.get(i).toString();
                }

            } ///end of for 1
        }//End of If 1
//        String sqlstr = "";                                                     //cbram
//        sqlstr += " SELECT  FACT_ID,                                                          ";
//        sqlstr += "   DIM_ID,                                                                 ";
//        sqlstr += "   ORDER_OF_DIM_IN_SUMM,                                                   ";
//        sqlstr += "   Max(DIM_MEM_KEY_LEVEL_IN_DIM) DIM_MEM_KEY_LEVEL_IN_DIM,                 ";
//        sqlstr += "   Max(DIM_MEM_FACT_LEVEL_IN_DIM) DIM_MEM_FACT_LEVEL_IN_DIM,               ";
//        sqlstr += "   FACT_COMBINED_KEY_NAME                                                  ";
//        sqlstr += " FROM PRG_USER_SUMM_DETAILS                                                ";
//        sqlstr += " where  FACT_ID =" + FactId;
//        sqlstr += " group by FACT_ID, DIM_ID, ORDER_OF_DIM_IN_SUMM, FACT_COMBINED_KEY_NAME    ";
//        sqlstr += " order by ORDER_OF_DIM_IN_SUMM                    ";

        StringBuilder sqlstr = new StringBuilder();
        sqlstr.append(" SELECT  FACT_ID,DIM_ID,ORDER_OF_DIM_IN_SUMM,Max(DIM_MEM_KEY_LEVEL_IN_DIM) DIM_MEM_KEY_LEVEL_IN_DIM,Max(DIM_MEM_FACT_LEVEL_IN_DIM) DIM_MEM_FACT_LEVEL_IN_DIM,");
        sqlstr.append(" FACT_COMBINED_KEY_NAME FROM PRG_USER_SUMM_DETAILS where  FACT_ID =").append(FactId).append(" group by FACT_ID, DIM_ID, ORDER_OF_DIM_IN_SUMM, FACT_COMBINED_KEY_NAME order by ORDER_OF_DIM_IN_SUMM   ");

        //       PbReturnObject retObj = pbDb.execSelectSQL(sqlstr);//cbram
        PbReturnObject retObj = pbDb.execSelectSQL(sqlstr.toString());
        String[] colNames = retObj.getColumnNames();
        //Vector facts = new Vector();
        // HashMap CurrCols = new HashMap();
        // HashMap PriorCols = new HashMap();
        int psize = retObj.getRowCount();

        if (psize > 0) {


            for (int i = 0; i < psize; i++) {
                if (!retObj.getFieldValueString(i, 1).equals("-1")) {
                    dimIds.add(retObj.getFieldValueString(i, 1));
                    ElementsMaxFactLevel.add(retObj.getFieldValueString(i, 4));
                    ElementsFactLevel.add("0");

                } else {
                    hasTime = true;
                }

            }

        }

        //
        //
        //

        if (hasTime) {
            if (whereClause == null) {
                whereClause = "-1";
            } else {
                whereClause += " , -1 ";
            }

        }
//        sqlstr = "";                                                          //cbram
//
//        sqlstr += " SELECT SUMM_KEY_ID, FACT_ID,FACT_BUSS_TABLE_NAME, DIM_ID, ORDER_OF_DIM_IN_SUMM, DIM_MEM_ELEMENT_ID,DIM_BUSS_TABLE_NAME, DIM_MEM_KEY_FIELD_ELE_ID, DIM_MEM_KEY_FIELD_NAME,       ";
//        sqlstr += "   DIM_MEM_KEY_LEVEL_IN_DIM, DIM_MEM_FACT_FIELD_ELE_ID, DIM_MEM_FACT_FIELD_NAME, DIM_MEM_FACT_LEVEL_IN_DIM, FACT_COMBINED_KEY_ELE_ID,   ";
//        sqlstr += "   FACT_COMBINED_KEY_NAME                                                                                                               ";
//        sqlstr += " FROM PRG_USER_SUMM_DETAILS    ";
//        sqlstr += " where  FACT_ID =" + FactId;
//        sqlstr += " and DIM_MEM_ELEMENT_ID in (    " + whereClause + ")  order by ORDER_OF_DIM_IN_SUMM,DIM_ID,DIM_MEM_FACT_LEVEL_IN_DIM desc ";

        sqlstr = new StringBuilder();
        sqlstr.append(" SELECT SUMM_KEY_ID, FACT_ID,FACT_BUSS_TABLE_NAME, DIM_ID, ORDER_OF_DIM_IN_SUMM, DIM_MEM_ELEMENT_ID,DIM_BUSS_TABLE_NAME, DIM_MEM_KEY_FIELD_ELE_ID, DIM_MEM_KEY_FIELD_NAME,       ");
        sqlstr.append("   DIM_MEM_KEY_LEVEL_IN_DIM, DIM_MEM_FACT_FIELD_ELE_ID, DIM_MEM_FACT_FIELD_NAME, DIM_MEM_FACT_LEVEL_IN_DIM, FACT_COMBINED_KEY_ELE_ID,   ");
        sqlstr.append("   FACT_COMBINED_KEY_NAME FROM PRG_USER_SUMM_DETAILS where  FACT_ID =").append(FactId).append(" and DIM_MEM_ELEMENT_ID in (    ").append(whereClause).append(")  order by ORDER_OF_DIM_IN_SUMM,DIM_ID,DIM_MEM_FACT_LEVEL_IN_DIM desc ");

        String CurrDim = "";
        String OldDim = "";

        retObj = null;
        //      retObj = pbDb.execSelectSQL(sqlstr);//cbram
        retObj = pbDb.execSelectSQL(sqlstr.toString());
        if (retObj != null) {

            psize = retObj.getRowCount();

        } else {
            psize = 0;
        }

        if (psize > 0) {


            CurrDim = retObj.getFieldValueString(0, 3);
            OldDim = retObj.getFieldValueString(0, 3);


            factName = retObj.getFieldValueString(0, 2);
            factCombinedkey = retObj.getFieldValueString(0, 14);

            if (!CurrDim.equals("-1")) {
                //
                ElementsFactLevel.remove(dimIds.indexOf(retObj.getFieldValueString(0, 3)));
                ElementsFactLevel.add(dimIds.indexOf(retObj.getFieldValueString(0, 3)), retObj.getFieldValueString(0, 12));



                if (!(retObj.getFieldValue(0, 11) == null
                        || "".equals(retObj.getFieldValueString(0, 11))
                        || "NULL".equalsIgnoreCase(retObj.getFieldValueString(0, 11)))) {
                    factWhere += " and " + retObj.getFieldValueString(0, 2) + "." + retObj.getFieldValueString(0, 11);
                    factWhere += " = " + retObj.getFieldValueString(0, 12) + " ";


                }

                if (!(retObj.getFieldValue(0, 8) == null
                        || "".equals(retObj.getFieldValueString(0, 8))
                        || "NULL".equalsIgnoreCase(retObj.getFieldValueString(0, 8)))) {

                    dimWhere += " and " + retObj.getFieldValueString(0, 6) + "." + retObj.getFieldValueString(0, 8);
                    dimWhere += " = " + retObj.getFieldValueString(0, 9) + " ";

                }

            }
            for (int i = 0; i < psize; i++) {
                CurrDim = retObj.getFieldValueString(i, 3);
                if (!OldDim.equals(CurrDim)) {
                    if (!CurrDim.equals("-1")) {
                        ElementsFactLevel.remove(dimIds.indexOf(retObj.getFieldValueString(i, 3)));
                        ElementsFactLevel.add(dimIds.indexOf(retObj.getFieldValueString(i, 3)), retObj.getFieldValueString(i, 12));

                        if (!(retObj.getFieldValue(0, 11) == null
                                || "".equals(retObj.getFieldValueString(0, 11))
                                || "NULL".equalsIgnoreCase(retObj.getFieldValueString(0, 11)))) {
                            factWhere += " and " + retObj.getFieldValueString(i, 2) + "." + retObj.getFieldValueString(i, 11);
                            factWhere += " = " + retObj.getFieldValueString(i, 12) + " ";
                        }
                        //
                        if (!(retObj.getFieldValue(i, 8) == null
                                || "".equals(retObj.getFieldValueString(i, 8))
                                || "NULL".equalsIgnoreCase(retObj.getFieldValueString(i, 8)))) {

                            dimWhere += " and " + retObj.getFieldValueString(i, 6) + "." + retObj.getFieldValueString(i, 8);
                            dimWhere += " = " + retObj.getFieldValueString(i, 9) + " ";

                        }

                        OldDim = retObj.getFieldValueString(i, 3);
                    }
                }

            }

            //code to find key value



        } else {
            if (!hasTime) {
                ElementsFactLevel = (ArrayList) ElementsMaxFactLevel.clone();
            } else {
                onlyTime = true;
            }


        }


        ////Code dependent on database oracle specific
//        sqlstr = "";                                                            //cbram
//
//        sqlstr += " select fact_combined_key,FACT_HINT from prg_user_fact_summ_info where fact_id =  " + FactId;
//        sqlstr += "   ";
//        sqlstr += "   ";
//        for (int i = dimIds.size() - 1, j = 1; i >= 0; i--, j = j * 10) {
//            sqlstr += " and trunc(mod(fact_combined_key," + j * 10 + ")/" + j + ") >=  " + ElementsFactLevel.get(i).toString();
//        }
//        sqlstr += "  order by fact_combined_key ";

        sqlstr = new StringBuilder();
        sqlstr.append(" select fact_combined_key,FACT_HINT from prg_user_fact_summ_info where fact_id =  ").append(FactId).append("   ").append("   ");
        for (int i = dimIds.size() - 1, j = 1; i >= 0; i--, j = j * 10) {
            sqlstr.append(" and trunc(mod(fact_combined_key,").append(j * 10).append(")/").append(j).append(") >=  ").append(ElementsFactLevel.get(i).toString());
        }
        sqlstr.append("  order by fact_combined_key ");
        retObj = null;
        //     retObj = pbDb.execSelectSQL(sqlstr);//cbram
        retObj = pbDb.execSelectSQL(sqlstr.toString());
        if (retObj != null) {

            psize = retObj.getRowCount();

        } else {
            psize = 0;
        }

        if (psize > 0) {
            factWhere += " and " + factName + "." + factCombinedkey + " = " + retObj.getFieldValueString(0, 0);
            OraFactHintMap.put(FactId, retObj.getFieldValueString(0, 1));
        }

        return (factWhere + dimWhere);
    }

    public String getUserId() {
        return userId;
    }

    public String mergeSqlandCrossTabSql(String normalSql, String CrossTabSql, ArrayList rowviewBy, ArrayList colList, ArrayList crossTabNonViewBy1, boolean isCrosstabfirst, ArrayList crossTabViewByList, ArrayList ColListSumm, String CrossTabSumm) {

//        // System.out.println("normalSql in mergeSqlandCrossTabSql is : " + normalSql);
//        // System.out.println("CrossTabSql in mergeSqlandCrossTabSql is : " + CrossTabSql);
        String combinedSql = "";
        String finalViewBy = null;
        String crossfinalViewBy = null;
        String finalColList = null;
        String finalNormalColList = null;
        String finalCrossTabColList = null;

        for (int i = 0; i < rowviewBy.size(); i++) {
            if (finalViewBy == null) {
                finalViewBy = " A_" + rowviewBy.get(i).toString();
            } else {
                finalViewBy += " , A_" + rowviewBy.get(i).toString();
            }

            if (crossfinalViewBy == null) {
                crossfinalViewBy = " " + crossTabViewByList.get(i).toString() + " as A_" + rowviewBy.get(i).toString();
            } else {
                crossfinalViewBy += " , " + crossTabViewByList.get(i).toString() + " as A_" + rowviewBy.get(i).toString();
            }
        }
        if (isCrosstabfirst) {

            for (int i = 0; i < crossTabNonViewBy1.size(); i++) {
                if (finalColList == null) {
                    finalColList = " , " + getOracleFormula(CrossTabSumm) + crossTabNonViewBy1.get(i).toString() + " ) AS " + crossTabNonViewBy1.get(i).toString();
                    finalCrossTabColList = " ," + crossTabNonViewBy1.get(i).toString();
                    finalNormalColList = " ," + " null ";
                } else {
                    finalColList += " ," + getOracleFormula(CrossTabSumm) + crossTabNonViewBy1.get(i).toString() + " ) AS " + crossTabNonViewBy1.get(i).toString();
                    finalCrossTabColList += " ," + crossTabNonViewBy1.get(i).toString();
                    finalNormalColList += " , null ";
                }

            }
            for (int i = 0; i < colList.size(); i++) {
                if (finalColList == null) {
                    finalColList = " , " + getOracleFormula(ColListSumm.get(i).toString()) + " A_" + colList.get(i).toString() + ") AS A_" + colList.get(i).toString();
                    finalNormalColList = " , A_" + colList.get(i).toString();
                    finalCrossTabColList = " , null " + "A_" + colList.get(i).toString();
                } else {
                    finalColList += " , " + getOracleFormula(ColListSumm.get(i).toString()) + " A_" + colList.get(i).toString() + ") AS A_" + colList.get(i).toString();
                    finalNormalColList += " , A_" + colList.get(i).toString();
                    finalCrossTabColList += " , null " + "A_" + colList.get(i).toString();
                }

            }

        } else {

            for (int i = 0; i < colList.size(); i++) {
                if (finalColList == null) {
                    finalColList = " , " + getOracleFormula(ColListSumm.get(i).toString()) + " A_" + colList.get(i).toString() + ") AS A_" + colList.get(i).toString();
                    finalNormalColList = " , A_" + colList.get(i).toString();
                    finalCrossTabColList = " , null A_" + colList.get(i).toString();
                } else {
                    finalColList += " ," + getOracleFormula(ColListSumm.get(i).toString()) + " A_" + colList.get(i).toString() + ") AS A_" + colList.get(i).toString();
                    finalNormalColList += " , A_" + colList.get(i).toString();
                    finalCrossTabColList += " , null A_" + colList.get(i).toString();
                }

            }

            for (int i = 0; i < crossTabNonViewBy1.size(); i++) {
                if (finalColList == null) {
                    finalColList = " ," + getOracleFormula(CrossTabSumm) + crossTabNonViewBy1.get(i).toString() + ") AS " + crossTabNonViewBy1.get(i).toString();
                    finalCrossTabColList = " ," + crossTabNonViewBy1.get(i).toString();
                    finalNormalColList = " , null " + crossTabNonViewBy1.get(i).toString();
                    ;
                } else {
                    finalColList += " ," + getOracleFormula(CrossTabSumm) + crossTabNonViewBy1.get(i).toString() + ") AS " + crossTabNonViewBy1.get(i).toString();
                    finalCrossTabColList += " ," + crossTabNonViewBy1.get(i).toString();
                    finalNormalColList += " , null " + crossTabNonViewBy1.get(i).toString();
                }

            }

        }///End of column collection

//Join Queries
        combinedSql = "Select " + finalViewBy + finalColList;
        combinedSql += " from ( ";
        if (isCrosstabfirst) {
            combinedSql += " select " + crossfinalViewBy + finalCrossTabColList + " from ( ";
            combinedSql += " " + CrossTabSql;
            combinedSql += " ) union all ";
            combinedSql += " select " + finalViewBy + finalNormalColList + " from ( "; //
            combinedSql += " " + normalSql;
            combinedSql += " ) ";
        } else {

            combinedSql += " select " + finalViewBy + finalNormalColList + " from ( "; //
            combinedSql += " " + normalSql;
            combinedSql += " ) union all ";
            combinedSql += " select " + crossfinalViewBy + finalCrossTabColList + " from ( ";
            combinedSql += " " + CrossTabSql;
            combinedSql += " ) ";
        }


        combinedSql += " ) group by " + finalViewBy;


//        // System.out.println("combinedSql is : " + combinedSql);

        return combinedSql;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getBizRoles() {
        return bizRoles;
    }

    public void setBizRoles(String bizRoles) {
        this.bizRoles = bizRoles;
    }

    public String getDefaultSortedColumn() {
        return defaultSortedColumn;
    }

    public void setDefaultSortedColumn(String defaultSortedColumn) {
        this.defaultSortedColumn = defaultSortedColumn;
    }

    public void setDefaultSortedColumnAndOrder(HashMap<String, String> sortColAndOrder) {
        this.sortColAndOrder = sortColAndOrder;
    }

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public HashMap getParameterGroupAnalysisHashMap() {
        return ParameterGroupAnalysisHashMap;
    }

    public void setParameterGroupAnalysisHashMap(HashMap ParameterGroupAnalysisHashMap) {
        this.ParameterGroupAnalysisHashMap = ParameterGroupAnalysisHashMap;
    }

    public HashMap getBucketAnalysisHashMap() {
        return bucketAnalysisHashMap;
    }

    public void setBucketAnalysisHashMap(HashMap bucketAnalysisHashMap) {
        this.bucketAnalysisHashMap = bucketAnalysisHashMap;
    }

    public ArrayList<String> getInnerViewBy(String reportId) throws SQLException {
        String Idslist = "";
        ArrayList<String> innerViewByList = new ArrayList<String>();

        String sql = "SELECT  ELEMENT_ID from PRG_AR_ADDITIONAL_VIEWBY where report_Id in(" + reportId + ")";
        PbReturnObject pbro = new PbDb().execSelectSQL(sql);

        if (pbro.getRowCount() > 0 && pbro != null) {
            for (int i = 0; i < pbro.getRowCount(); i++) {
                innerViewByList.add(pbro.getFieldValueString(i, 0));
            }

        }
        return innerViewByList;
    }

    public String bucketWhereclause(String bucketElementId, String tableList) throws Exception {

        String sqlstr = resBundle.getString("generateViewByQry2");//generateCrossTabMeasure
        Object Obj[] = new Object[2];
        Obj[0] = bucketElementId;
        Obj[1] = bucketElementId;
        String finalQuery = pbDb.buildQuery(sqlstr, Obj);
//        // System.out.println("final Query===" + finalQuery);
        PbReturnObject retObj = pbDb.execSelectSQL(finalQuery);
        String[] colNames = retObj.getColumnNames();
        //Vector facts = new Vector();
        // HashMap CurrCols = new HashMap();
        // HashMap PriorCols = new HashMap();
        int psize = retObj.getRowCount();
        //
        String bucketinsql = "";
        String temp = "";
        if (psize > 0) {
            //Looping twice
            //Loop 1 find the fact and current and prior cols
            //loop 2 build query

            for (int i = 0; i < psize; i++) {
                temp = formatString(retObj.getFieldValueString(i, colNames[3]));
                System.out.println("aggFormula-" + retObj.getFieldValueString(i, colNames[5]) + "==" + retObj.getFieldValueString(i, colNames[6]));
                String aggFormula = getAggregation(retObj.getFieldValueString(i, colNames[5]), retObj.getFieldValueString(i, colNames[6]));
                System.out.println("aggFormula==" + aggFormula);
                // String aggFormula2 = getLevelFormula(aggFormula, facts.size());
                //not needed starts here
                if (retObj.getFieldValue(i, colNames[8]) == null) {
                    bucketinsql += " , " + aggFormula + " " + retObj.getFieldValueString(i, colNames[1]) + "." + retObj.getFieldValueString(i, colNames[2]) + ") ";
                } else {


                    if (!(retObj.getFieldValue(i, colNames[1]) == null || retObj.getFieldValueString(i, colNames[1]).equalsIgnoreCase("") || retObj.getFieldValueString(i, colNames[1]).equalsIgnoreCase("0"))) {
                        String mTemp = retObj.getFieldValueString(i, colNames[8]);
                        int mInd = mTemp.indexOf("<!") + 2;
                        int nInd = mTemp.indexOf("!>");
                        String GetCurrVal = "";
                        if (mInd > 1) {
                            GetCurrVal = mTemp.substring(mInd, nInd);
                        }




                        if (mInd > 1) {
                            mTemp = mTemp.replace("<!" + GetCurrVal + "!>", "'" + getParamValueforCurr(GetCurrVal) + "'");
                        }


                        if (retObj.getFieldValueString(i, colNames[10]).equalsIgnoreCase("CALCULATED")) {

                            bucketinsql += " , " + aggFormula + " " + mTemp + ") ";
                        } else if (retObj.getFieldValueString(i, colNames[10]).equalsIgnoreCase("TIMECALUCULATED")) {

                            bucketinsql += ",null " + temp + " ";
                            //////Code for Timebased Formula
                            {
                                String inqry = "";
                                String summdays = "";
                                for (int timeI = 0; timeI < psize; timeI++) {
                                    if (i != timeI) {
                                        inqry += " , null   ";
                                    } else {
                                        System.out.println(" measure" + retObj.getFieldValueString(i, colNames[8]));
                                        System.out.println("aggFormula" + aggFormula);
                                        String tempAgg = aggFormula;
                                        int cInd = tempAgg.indexOf("#");
                                        System.out.println("cInd" + cInd);
                                        summdays = tempAgg.substring(cInd + 1);
                                        aggFormula = tempAgg.substring(0, cInd);
                                        aggFormula = getOracleFormula(aggFormula);
                                        // aggFormula2 = getLevelFormula(aggFormula, facts.size());
                                        inqry += " , " + aggFormula + " " + retObj.getFieldValueString(i, colNames[8]) + ") as " + temp + " ";

                                    }

                                }

                            }
                        } else {
                            String tempor = mTemp.replace("COUNTDISTINCT(", "count( DISTINCT ");
                            bucketinsql += " , ( " + tempor + ") ";

                        }
                    } else {
                        bucketinsql += ",null " + temp + " ";
                    }



                }//not needed ends
//                // System.out.println("retObj.getFieldValueString(i, colNames[8])===" + retObj.getFieldValueString(i, colNames[8]));

                String colName = retObj.getFieldValueString(i, colNames[8]);
                if (colName.equalsIgnoreCase("")) {
                    colName = retObj.getFieldValueString(i, colNames[2]);
                }
//                // System.out.println("bucketinsql==" + bucketinsql);

                if (bucketinsql.toUpperCase().indexOf("COUNT") >= 0) {
                    bucketinsql = bucketinsql;

                } else {
                    if (colName.indexOf(".") >= 0) {
                        bucketinsql = colName;
                    } else {
                        bucketinsql = retObj.getFieldValueString(i, colNames[1]) + "." + colName;
                    }
                }
            }


            logger.info("bucketinsql in new method==" + bucketinsql);
        }



        bucketinsql = temp;
        if (!bucketinsql.equalsIgnoreCase("")) {
            bucketinsql = temp;
            /*
             * if(bucketinsql.trim().startsWith(",")){
             * bucketinsql=bucketinsql.trim().substring(1); bucketinsql=" (
             * select "+bucketinsql+" from "+tableList+" )";
            }
             */

        }
//        // System.out.println("------------------------------------------------------------------------------------------------");
//        // System.out.println("bucketinsql=========" + bucketinsql);
        return bucketinsql;
    }

    public String getBussTableNames(ArrayList busIdslist) throws Exception {
        String Idslist = "";
        String busstabList = "";
        if (busIdslist.size() > 0) {
            for (int i = 0; i < busIdslist.size(); i++) {
                if (!(String.valueOf(busIdslist.get(i)).equalsIgnoreCase("")) && !(String.valueOf(busIdslist.get(i)).equalsIgnoreCase("null"))) {
                    Idslist += "," + busIdslist.get(i);
                }
            }
        }
        if (!Idslist.trim().equalsIgnoreCase("")) {
            if (Idslist.trim().startsWith(",")) {
                Idslist = Idslist.substring(1);
            }
            String sql = "SELECT  BUSS_TABLE_NAME from PRG_GRP_BUSS_TABLE where BUSS_TABLE_ID in(" + Idslist + ")";
            PbReturnObject pbro = new PbDb().execSelectSQL(sql);

            if (pbro.getRowCount() > 0 && pbro != null) {
                for (int i = 0; i < pbro.getRowCount(); i++) {
                    busstabList += "," + pbro.getFieldValueString(i, 0);
                }
                if (!busstabList.trim().equalsIgnoreCase("")) {
                    if (busstabList.trim().startsWith(",")) {
                        busstabList = busstabList.substring(1);
                    }
                }
            }
        }
        return busstabList;
    }

    //added by uday on 6-mar-2010
    public PbReturnObject getWhatIfPbReturnObjectWithFlag(String elementId, HashMap TableHashMap, HashMap ReportHashMap) {
        PbReturnObject retObj = null;
        Connection conn = null;
        String[] columnNames = null;
        String[] columnTypes = null;
        int[] columnSizes = null;
        //int[] columnTypesInt = null;
        BigDecimal grandTotals[] = null;
        BigDecimal avgTotals[] = null;
        BigDecimal max[] = null;
        BigDecimal min[] = null;
        boolean processGT = false;
        TreeMap columnOverAllMaximums = null;
        TreeMap columnOverAllMinimums = null;
        TreeMap columnAverages = null;
        TreeMap columnGrandTotals = null;
        TreeMap rowGrandTotals = null;
        LinkedHashMap whatIfRanges = null;
        HashMap AllHashMap = null;
        HashMap NonAllHashMap = null;
        ArrayList NonAllNames = null;
        HashMap NonAllNamesHashMap = null;
        ArrayList rowEdgeParamValues = null;
        PbReturnObject prObj = null;
        PbReturnObject formulaPbro = null;
        HashMap sliderDetailsHashMap = null;
        ArrayList Measures = null;
        String custMeasureElementId = null;
        HashMap CustomMeasureFormulaHashMap = null;
        ArrayList formulaDetails = null;
        String refferedElements = null;
        String[] refferedElementsArray = null;
        String aggregationType = null;
        String displayFormula = null;
        HashMap DisplayNamesMap = null;
        String[] DisplayNamesMapKeys = null;
        StringBuffer finalFormula = null;
        ArrayList AllREPValues = null;
        HashMap tempHashMap = null;
        //Formula f = null;
        //Variant res = null;

        try {

            String sql = generateViewByQry();
            int q = 0;
            conn = getConnection(elementId);
            retObj = pbDb.execSelectSQLWithFlag(sql, conn);

            prObj = new PbReturnObject();

            //retObj = pbDb.execSelectSQLWithFlag(sql, conn);

            columnNames = retObj.getColumnNames();
            columnTypes = retObj.getColumnTypes();
            columnSizes = retObj.getColumnSizes();
            processGT = retObj.isProcessGT();
            int count = retObj.getRowCount();

            for (int i = 0; i < count; i++) {
                if (rowEdgeParamValues == null) {
                    rowEdgeParamValues = new ArrayList();
                    rowEdgeParamValues.add(String.valueOf(retObj.getFieldValueString(i, 0)));
                } else {
                    rowEdgeParamValues.add(String.valueOf(retObj.getFieldValueString(i, 0)));
                }
            }
            TableHashMap.put("rowEdgeParamValues", rowEdgeParamValues);



            String sliderName = null;
            String sliderValue = null;
            ArrayList newColumnNames = new ArrayList();
            ArrayList newColumnTypes = new ArrayList();
            ArrayList newColumnSizes = new ArrayList();
            //ArrayList newColumnTypesInt = new ArrayList();


            double tempValue = 0;
            double sliderValueDouble = 0;
            ArrayList REP = null;
            String[] whatIfRangesKeys = null;
            LinkedHashMap REPWhatIfRangeHashMap = null;
            LinkedHashMap NewREPWhatIfRangeHashMap = null;

            whatIfRanges = (LinkedHashMap) TableHashMap.get("whatIfRanges");
            whatIfRangesKeys = (String[]) whatIfRanges.keySet().toArray(new String[0]);
//            // System.out.println("whatIfRangesKeys len is:: " + whatIfRangesKeys.length);
            REP = (ArrayList) TableHashMap.get("REP");
            rowEdgeParamValues = (ArrayList) TableHashMap.get("rowEdgeParamValues");
            Measures = (ArrayList) TableHashMap.get("Measures");

            if (!TableHashMap.containsKey("AllREPValues")) {
                tempHashMap = new HashMap();
                tempHashMap.put(String.valueOf(REP.get(0)), rowEdgeParamValues);
                TableHashMap.put("AllREPValues", tempHashMap);
            } else {
                tempHashMap = (HashMap) TableHashMap.get("AllREPValues");
                if (!tempHashMap.containsKey(REP.get(0))) {
                    tempHashMap.put(String.valueOf(REP.get(0)), rowEdgeParamValues);
                } else {
                }
                TableHashMap.put("AllREPValues", tempHashMap);
            }

            if (TableHashMap.get("NonAllNames") == null) {
                NonAllNamesHashMap = new HashMap();
            } else {
                NonAllNamesHashMap = (HashMap) TableHashMap.get("NonAllNames");
            }

            if (NonAllNamesHashMap.get(REP.get(0)) == null) {
                NonAllNames = new ArrayList();
            } else {
                NonAllNames = (ArrayList) NonAllNamesHashMap.get(REP.get(0));
            }

            for (int i = 0; i < columnNames.length; i++) {
                if (!(columnTypes[i].toUpperCase().equalsIgnoreCase("NUMBER"))) {
                    newColumnNames.add(columnNames[i]);
                    newColumnTypes.add("VARCHAR2");
                    newColumnSizes.add(columnSizes[i]);
                    //newColumnTypesInt.add(columnTypesInt[i]);
                } else if (columnTypes[i].toUpperCase().equalsIgnoreCase("NUMBER")) {
                    newColumnNames.add(columnNames[i]);
                    newColumnNames.add(columnNames[i] + "_P");
                    newColumnTypes.add("NUMBER");
                    newColumnTypes.add("NUMBER");
                    newColumnSizes.add(columnSizes[i]);
                    newColumnSizes.add(columnSizes[i]);
                    //newColumnTypesInt.add(columnTypesInt[i]);
                    //newColumnTypesInt.add(columnTypesInt[i]);
                }

                if (Measures.contains(columnNames[i].replace("A_", "") + "CM")) {
                    if (CustomMeasureFormulaHashMap == null) {
                        CustomMeasureFormulaHashMap = new HashMap();
                    }
                    formulaDetails = new ArrayList();
                    custMeasureElementId = columnNames[i].replace("A_", "");
                    formulaPbro = getCustomMeasureFormula(custMeasureElementId);
                    for (int j = 0; j < formulaPbro.getRowCount(); j++) {
                        refferedElements = formulaPbro.getFieldValueString(j, "REFFERED_ELEMENTS");
                        aggregationType = formulaPbro.getFieldValueString(j, "AGGREGATION_TYPE");
                        displayFormula = formulaPbro.getFieldValueString(j, "DISPLAY_FORMULA");
                        formulaDetails.add(refferedElements);
                        formulaDetails.add(aggregationType);
                        formulaDetails.add(displayFormula);
                        CustomMeasureFormulaHashMap.put(custMeasureElementId, formulaDetails);
                    }
                    formulaDetails = null;
                    refferedElements = null;
                    aggregationType = null;
                    displayFormula = null;
                }
            }

//            // System.out.println("CustomMeasureFormulaHashMap is:: " + CustomMeasureFormulaHashMap);
            String[] newColumnNamesArray = new String[newColumnNames.size()];
            String[] newColumnTypesArray = new String[newColumnTypes.size()];
            int[] newColumnSizesArray = new int[newColumnSizes.size()];
            //int[] newColumnTypesIntArray = new int[newColumnTypesInt.size()];
//            // System.out.println("newColumnNames.size()-REP.size() is:: " + (newColumnNames.size() - REP.size()));
            grandTotals = new BigDecimal[newColumnNames.size() - REP.size()];
            avgTotals = new BigDecimal[newColumnNames.size() - REP.size()];
            max = new BigDecimal[newColumnNames.size() - REP.size()];
            min = new BigDecimal[newColumnNames.size() - REP.size()];
            columnOverAllMaximums = new TreeMap();
            columnOverAllMinimums = new TreeMap();
            columnGrandTotals = new TreeMap();
            columnAverages = new TreeMap();
            rowGrandTotals = new TreeMap();

            for (int i = 0; i < newColumnNames.size(); i++) {
                newColumnNamesArray[i] = (String) newColumnNames.get(i);
                newColumnTypesArray[i] = (String) newColumnTypes.get(i);
                newColumnSizesArray[i] = ((Integer) newColumnSizes.get(i)).intValue();
                //newColumnTypesIntArray[i] = ((Integer) newColumnTypesInt.get(i)).intValue();
            }

            for (int i = 0; i < (newColumnNames.size() - REP.size()); i++) {
                grandTotals[i] = new BigDecimal("0");
                avgTotals[i] = new BigDecimal("0");
                max[i] = new BigDecimal("0");
                min[i] = new BigDecimal("0");
            }

            //set new columns
            prObj.setColumnNames(newColumnNamesArray);
            prObj.setColumnTypes(newColumnTypesArray);
            prObj.setColumnSizes(newColumnSizesArray);
            //prObj.setColumnTypesInt(newColumnTypesIntArray);
//            // System.out.println("columnNames.length in report query is:::: " + columnNames.length);


            for (int i = 0; i < count; i++) {
                BigDecimal RowGrandTotal = new BigDecimal("0");
                BigDecimal dividend = new BigDecimal(String.valueOf(count));
                HashMap ProjectedValues = new HashMap();
                for (int j = 0; j < columnNames.length; j++) {
//                    // System.out.println("columnTypes[" + j + "] is::: " + columnTypes[j]);
                    BigDecimal bdecimal = null;
                    if (!columnTypes[j].equalsIgnoreCase("NUMBER")) {
                        q = 0;
                        /*
                         * if (processGT) { bdecimal = new BigDecimal("0"); //
                         * System.out.println("bdecimal in report query is:::: "
                         * + bdecimal); if (count == 0) { grandTotals[j] =
                         * bdecimal; max[j] = bdecimal; min[j] = bdecimal; }
                         * else { grandTotals[j] = grandTotals[j].add(bdecimal);
                         * max[j] = max[j].max(bdecimal); min[j] =
                         * min[j].min(bdecimal); } if (j == 0) { RowGrandTotal =
                         * bdecimal; } else { RowGrandTotal =
                         * RowGrandTotal.add(bdecimal); } bdecimal = null;
                         * columnOverAllMaximums.put(columnNames[j], max[j]);
                         * columnOverAllMinimums.put(columnNames[j], min[j]);
                         * columnGrandTotals.put(columnNames[j],
                         * grandTotals[j]); avgTotals[j] =
                         * grandTotals[j].divide(dividend,
                         * MathContext.DECIMAL64);
                         * columnAverages.put(columnNames[j], avgTotals[j]); }
                         */
                        prObj.setFieldValueString(columnNames[j], String.valueOf(rowEdgeParamValues.get(i)));

                    } else if (columnTypes[j].equalsIgnoreCase("NUMBER")) {
                        if (!(Measures.contains(columnNames[j].replace("A_", "") + "CM"))) {
                            //For Actual Values
//                            // System.out.println("doesnt contain CM " + j);
                            //q++;
                            prObj.setFieldValueString(columnNames[j], retObj.getFieldValueString(i, j));
                            if (processGT) {
                                bdecimal = new BigDecimal(String.valueOf(retObj.getFieldValueString(i, j)));
//                                // System.out.println("bdecimal in report query  is:::: " + bdecimal);
                                if (i == 0) {
                                    grandTotals[q] = bdecimal;
                                    max[q] = bdecimal;
                                    min[q] = bdecimal;
//                                    // System.out.println("min[q] whn count is 0 is::: " + min[q]);
                                } else {
                                    grandTotals[q] = grandTotals[q].add(bdecimal);
                                    max[q] = max[q].max(bdecimal);
                                    min[q] = min[q].min(bdecimal);
//                                    // System.out.println("min[q] is::: " + min[q]);
                                }
                                if (j == 0) {
                                    RowGrandTotal = bdecimal;
                                } else {
                                    RowGrandTotal = RowGrandTotal.add(bdecimal);
                                }
                                bdecimal = null;
                                columnOverAllMaximums.put(columnNames[j], max[q]);
                                columnOverAllMinimums.put(columnNames[j], min[q]);
                                columnGrandTotals.put(columnNames[j], grandTotals[q]);
                                avgTotals[q] = grandTotals[q].divide(dividend, MathContext.DECIMAL64);
                                columnAverages.put(columnNames[j], avgTotals[q]);
                            }

                            //end of Actual Values

                            //For Projected Values
                            q++;

                            if (TableHashMap.containsKey(String.valueOf(REP.get(0)))) {
                                sliderDetailsHashMap = (HashMap) TableHashMap.get(String.valueOf(REP.get(0)));
                                if (sliderDetailsHashMap.containsKey("All")) {
                                    AllHashMap = (HashMap) sliderDetailsHashMap.get("All");
                                    sliderName = columnNames[j].replace("A_", "") + "Â©" + String.valueOf(rowEdgeParamValues.get(i));
//                                    // System.out.println("sliderName is::: " + sliderName);
                                    if (AllHashMap.containsKey(sliderName)) {
                                        sliderValue = String.valueOf(AllHashMap.get(sliderName));
//                                        // System.out.println("sliderValue All is:: " + sliderValue);
                                        sliderValueDouble = Double.parseDouble(sliderValue);
                                        tempValue = Double.parseDouble(retObj.getFieldValueString(i, j)) * sliderValueDouble;
//                                        // System.out.println("tempValue All is:: " + tempValue);
                                    } else {
                                        if (NonAllNames.size() > 0) {
                                            for (int k = 0; k < NonAllNames.size(); k++) {
                                                if (sliderDetailsHashMap.containsKey(NonAllNames.get(k))) {
                                                    NonAllHashMap = (HashMap) sliderDetailsHashMap.get(NonAllNames.get(k));
                                                    //sliderName = columnNames[j].replace("A_", "") + "Â©" + String.valueOf(rowEdgeParamValues.get(i));
                                                    if (NonAllHashMap.containsKey(sliderName)) {
                                                        sliderValue = String.valueOf(NonAllHashMap.get(sliderName));
//                                                        // System.out.println("sliderValue NonAll is:: " + sliderValue);
                                                        sliderValueDouble = Double.parseDouble(sliderValue);
                                                        tempValue = Double.parseDouble(retObj.getFieldValueString(i, j)) * sliderValueDouble;
//                                                        // System.out.println("tempValue NonAll is:: " + tempValue);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            } else {
//                                // System.out.println("whatIfRanges are:: " + whatIfRanges);
//                                // System.out.println("REP.get(0) is::: " + String.valueOf(REP.get(0)));
                                if (whatIfRanges.containsKey(String.valueOf(REP.get(0)))) {
                                    // System.out.println("in if already there ");
                                    REPWhatIfRangeHashMap = (LinkedHashMap) whatIfRanges.get(REP.get(0));
                                    // System.out.println("REPWhatIfRangeHashMap is:: " + REPWhatIfRangeHashMap);
                                    sliderValueDouble = Double.parseDouble(String.valueOf(REPWhatIfRangeHashMap.get(columnNames[j].replace("A_", "") + "Â©All")));
                                    // System.out.println("sliderValueDouble is:: " + sliderValueDouble);
                                    tempValue = Double.parseDouble(retObj.getFieldValueString(i, j)) * sliderValueDouble;
                                } else {
                                    // System.out.println("in else not there ");
                                    if (NewREPWhatIfRangeHashMap == null) {
                                        NewREPWhatIfRangeHashMap = new LinkedHashMap();
                                    }
                                    REPWhatIfRangeHashMap = (LinkedHashMap) whatIfRanges.get(whatIfRangesKeys[0]);
                                    if (REPWhatIfRangeHashMap.containsKey(columnNames[j].replace("A_", "") + "Â©All")) {
                                        sliderValueDouble = Double.parseDouble(String.valueOf(REPWhatIfRangeHashMap.get(columnNames[j].replace("A_", "") + "Â©All")));
                                        tempValue = Double.parseDouble(retObj.getFieldValueString(i, j)) * sliderValueDouble;
                                        NewREPWhatIfRangeHashMap.put(columnNames[j].replace("A_", "") + "Â©All", REPWhatIfRangeHashMap.get(columnNames[j].replace("A_", "") + "Â©All"));
                                    }
                                }
                            }
                            prObj.setFieldValueString(columnNames[j] + "_P", String.valueOf(tempValue));

                            ProjectedValues.put(columnNames[j] + "_P", String.valueOf(tempValue));


                            if (processGT) {
                                bdecimal = new BigDecimal(String.valueOf(tempValue));
                                if (i == 0) {
                                    grandTotals[q] = bdecimal;
                                    max[q] = bdecimal;
                                    min[q] = bdecimal;
                                } else {
                                    grandTotals[q] = grandTotals[q].add(bdecimal);
                                    max[q] = max[q].max(bdecimal);
                                    min[q] = min[q].min(bdecimal);
                                }
                                if (j == 0) {
                                    RowGrandTotal = bdecimal;
                                } else {
                                    RowGrandTotal = RowGrandTotal.add(bdecimal);
                                }
                                bdecimal = null;
                                columnOverAllMaximums.put(columnNames[j] + "_P", max[q]);
                                columnOverAllMinimums.put(columnNames[j] + "_P", min[q]);
                                columnGrandTotals.put(columnNames[j] + "_P", grandTotals[q]);
                                avgTotals[q] = grandTotals[q].divide(dividend, MathContext.DECIMAL64);
                                columnAverages.put(columnNames[j] + "_P", avgTotals[q]);
                            }
                            q++;
                        }//end of Projected Values
                        //For Custom Measure Formula
                        else if (Measures.contains(columnNames[j].replace("A_", "") + "CM")) {
                            // System.out.println("For Custom Measures");
                            //For Custom Measure Actual Values
                            //q++;
                            prObj.setFieldValueString(columnNames[j], retObj.getFieldValueString(i, j));
                            if (processGT) {
                                bdecimal = new BigDecimal(String.valueOf(retObj.getFieldValueString(i, j)));
                                // System.out.println("bdecimal in report query  is:::: " + bdecimal);
                                if (i == 0) {
                                    grandTotals[q] = bdecimal;
                                    max[q] = bdecimal;
                                    min[q] = bdecimal;
                                } else {
                                    grandTotals[q] = grandTotals[q].add(bdecimal);
                                    max[q] = max[q].max(bdecimal);
                                    min[q] = min[q].min(bdecimal);
                                }
                                if (j == 0) {
                                    RowGrandTotal = bdecimal;
                                } else {
                                    RowGrandTotal = RowGrandTotal.add(bdecimal);
                                }
                                bdecimal = null;
                                columnOverAllMaximums.put(columnNames[j], max[q]);
                                columnOverAllMinimums.put(columnNames[j], min[q]);
                                columnGrandTotals.put(columnNames[j], grandTotals[q]);
                                avgTotals[q] = grandTotals[q].divide(dividend, MathContext.DECIMAL64);
                                columnAverages.put(columnNames[j], avgTotals[q]);
                            }

                            //For Custom Measure Projected Values
                            q++;

                            custMeasureElementId = columnNames[j].replace("A_", "");
                            if (custMeasureElementId != null) {
                                if (CustomMeasureFormulaHashMap.get(custMeasureElementId) != null) {
                                    formulaDetails = (ArrayList) CustomMeasureFormulaHashMap.get(custMeasureElementId);
                                    refferedElements = String.valueOf(formulaDetails.get(0));
                                    aggregationType = String.valueOf(formulaDetails.get(1));
                                    displayFormula = String.valueOf(formulaDetails.get(2));
                                    refferedElementsArray = refferedElements.split(",");
                                    DisplayNamesMap = (HashMap) ReportHashMap.get("DisplayNamesMap");
                                    DisplayNamesMapKeys = (String[]) DisplayNamesMap.keySet().toArray(new String[0]);
                                    //tempValue = 0.0;
                                    for (int k = 0; k < DisplayNamesMapKeys.length; k++) {
                                        if (displayFormula.contains(String.valueOf(DisplayNamesMap.get(DisplayNamesMapKeys[k])))) {
                                            displayFormula = displayFormula.replace(String.valueOf(DisplayNamesMap.get(DisplayNamesMapKeys[k])), String.valueOf(ProjectedValues.get(DisplayNamesMapKeys[k] + "_P")));
                                        }
                                    }
                                    // System.out.println("finalFormula is::: " + displayFormula);
                                    //f = new Formula(displayFormula);
                                    //res = f.evaluate();
                                    //tempValue = res.getDoubleValue();

                                    Parser parser = new Parser(displayFormula);
                                    ExpressionNode root = parser.getTree();
                                    tempValue = root.getValue();
                                }
                            }
                            prObj.setFieldValueString(columnNames[j] + "_P", String.valueOf(tempValue));
                            ProjectedValues.put(columnNames[j] + "_P", String.valueOf(tempValue));

                            if (processGT) {
                                bdecimal = new BigDecimal(String.valueOf(tempValue));
                                if (i == 0) {
                                    grandTotals[q] = bdecimal;
                                    max[q] = bdecimal;
                                    min[q] = bdecimal;
                                } else {
                                    grandTotals[q] = grandTotals[q].add(bdecimal);
                                    max[q] = max[q].max(bdecimal);
                                    min[q] = min[q].min(bdecimal);
                                }
                                if (j == 0) {
                                    RowGrandTotal = bdecimal;
                                } else {
                                    RowGrandTotal = RowGrandTotal.add(bdecimal);
                                }
                                bdecimal = null;
                                columnOverAllMaximums.put(columnNames[j] + "_P", max[q]);
                                columnOverAllMinimums.put(columnNames[j] + "_P", min[q]);
                                columnGrandTotals.put(columnNames[j] + "_P", grandTotals[q]);
                                avgTotals[q] = grandTotals[q].divide(dividend, MathContext.DECIMAL64);
                                columnAverages.put(columnNames[j] + "_P", avgTotals[q]);
                            }
                            q++;
                            // System.out.println("ProjectedValues is:: " + ProjectedValues);
                        }
                    }
                }
                if (processGT) {
                    rowGrandTotals.put("RowGrandTotal_" + count, RowGrandTotal);
                }
                prObj.addRow();
            }

            prObj.setAvgTotals(avgTotals);
            prObj.setColumnAverages(columnAverages);
            // System.out.println("columnGrandTotals in report query is:: " + columnGrandTotals);
            prObj.setColumnGrandTotals(columnGrandTotals);
            prObj.setColumnOverAllMaximums(columnOverAllMaximums);
            prObj.setColumnOverAllMinimums(columnOverAllMinimums);
            prObj.setGrandTotals(grandTotals);
            prObj.setMax(max);
            prObj.setMin(min);
            prObj.setRowGrandTotals(rowGrandTotals);




            if (NewREPWhatIfRangeHashMap != null) {
                whatIfRanges.put(REP.get(0), NewREPWhatIfRangeHashMap);
            }
            TableHashMap.put("whatIfRanges", whatIfRanges);

        } catch (Exception exp) {
            logger.error("Exception:", exp);
        } finally {
//            if (conn != null) {
//                try {
//                    conn.close();
//                } catch (SQLException ex) {
//                    logger.error("Exception:",ex);
//                }
//            }
        }
        return prObj;
    }

    public PbReturnObject getCustomMeasureFormula(String custMeasureElementId) {
        String finalSQL = null;
        PbReturnObject pbro = null;

        try {
            finalSQL = "select * from prg_user_all_info_details where element_id=" + custMeasureElementId;
            pbro = pbDb.execSelectSQL(finalSQL);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return pbro;
    }

    public boolean isSelectiveParameters() {
        return selectiveParameters;
    }

    public void setSelectiveParameters(boolean selectiveParameters) {
        this.selectiveParameters = selectiveParameters;
    }

    public PbReturnObject getPbReturnObject(String elementId, HttpSession session) {
        Container container = null;
        HashMap map = (HashMap) session.getAttribute("PROGENTABLES");
        container = (Container) map.get(getReportId());

        PbReturnObject retObj = null;
        Connection conn = null;
        try {


            // System.out.println("connnn" + conn);
            String sql = generateViewByQry();
            container.setSqlStr(sql);
            session.setAttribute("sqlStr", sql);
            ////.println("sql in getPbReturnObject is \t" + sql);
            conn = getConnection(elementId);
            retObj = pbDb.execSelectSQL(sql, conn);
            this.reportQuery = sql;

        } catch (Exception exp) {
            logger.error("Exception:", exp);
        } finally {
//            if (conn != null) {
//                try {
//                    conn.close();
//                } catch (SQLException ex) {
//                    logger.error("Exception:",ex);
//                }
//            }
        }
        return retObj;
    }

    public String getFinalNormalQuery() {
        return finalNormalQuery;
    }

    public void setFinalNormalQuery(String finalNormalQuery) {
        this.finalNormalQuery = finalNormalQuery;
    }
//added by sruthi to display the full query

    public void setfullQuery(String fullQueryData) {

        this.fullQueryReport = fullQueryData;
    }

    public String getfullQuery() {

        return fullQueryReport;
    }//ended by sruthi

    public PbReturnObject getPbReturnObjectDashGrp(String elementId) {
        PbReturnObject retObj = null;
        Connection conn = null;
        //
        try {

            //conn = getConnection(elementId);
            // System.out.println("connnn" + conn);
            String sql = generateViewByQry();

            if (!sql.contains("ORDER BY") || !sql.contains("order by")) {
                sql = sql + " Order by 2 desc";
            }

//           //
            setFinalNormalQuery(sql);
            // System.out.println("sql in getPbReturnObject is ::::::::: " + sql);
            conn = getConnection(elementId);
            retObj = pbDb.execSelectSQL(sql, conn);

        } catch (Exception exp) {
            logger.error("Exception:", exp);
        } finally {
//            if (conn != null) {
//                try {
//                    conn.close();
//                } catch (SQLException ex) {
//                    logger.error("Exception:",ex);
//                }
//            }
        }
        return retObj;
    }

    public ArrayListMultimap<Integer, Integer> getCrosstabColumnSpan() {
        return this.colSpanMap;
    }

    public void setGrandTotalSubTotalDisplayPosition(String grandTotalPosition, String subTotalPosition) {
        this.crosstabGrandTotalDisplay = grandTotalPosition;
        this.crosstabSubTotalDisplay = subTotalPosition;
    }

    public int getNoOfDays() {
        return this.noOfDays;
    }

    public Map<String, Integer> getTrendNoOfDays() {
        return this.trendNoOfDays;
    }

    public String getWhereClause() {
        return whereClause;
    }

    public void setWhereClause(String whereClause) {
        this.whereClause = whereClause;
    }

    public HashMap getTimememdetails() {
        return timememdetails;
    }

    public void setTimememdetails(HashMap timememdetails) {
        this.timememdetails = timememdetails;
    }

    public boolean isReportAccess() {
        return isReportAccess;
    }

    public void setIsReportAccess(boolean isReportAccess) {
        this.isReportAccess = isReportAccess;
    }
    //sandeep for setting security values for dashboard.
    //public void setsecurityvalues(Container container) throws SQLException{
    //     PbReportCollection collect = new PbReportCollection();
    //      if (container != null) {
    //       collect = container.getReportCollect();
    //       collect.operatorFilters.put("IN",inMaps);
    //      }

// }
    public void addSecurityClause() throws SQLException {
//        String SecCluase = "select MEMBER_VALUE,element_Id from PRG_USER_ROLE_MEMBER_FILTER "                        //cbram
//                + " where user_id = " + userId + " and folder_id in (select folder_id from PRG_AR_REPORT_DETAILS "
//                + " where report_id = "+ reportId +")";
        StringBuilder SecCluase = new StringBuilder();
        SecCluase.append("select MEMBER_VALUE,element_Id from PRG_USER_ROLE_MEMBER_FILTER   where user_id = ").append(userId).append(" and folder_id in (select folder_id from PRG_AR_REPORT_DETAILS ");
        SecCluase.append(" where report_id = ").append(reportId).append(")");
        //  ProgenLog.log(ProgenLog.FINE, this, "getSecFactFilter", "Enter ");
        logger.info("Enter");
        PbReturnObject retObj;
        String[] colNames = null;
        String aList = "";
        String SecValueList = "";
//
        //      retObj = pbDb.execSelectSQL(SecCluase);//cbram
        retObj = pbDb.execSelectSQL(SecCluase.toString());
        int psize = retObj.getRowCount();
//
        if (psize > 0) {
            colNames = retObj.getColumnNames();
            String filterFromTable = null;
            //Looping twice
            //Loop 1 find the fact and current and prior cols
            //loop 2 build query
            for (int looper = 0; looper < psize; looper++) {
                if (retObj.getFieldValue(looper, 0) != null && !retObj.getFieldValueString(looper, 0).trim().equals("")) {
                    if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                        SecValueList = retObj.getFieldUnknown(looper, 0).toString().replace("'", "");
                    } else {
                        SecValueList = retObj.getFieldValueClobString(looper, "MEMBER_VALUE").toString().replace("'", "");
                    }
                    String[] tempSecList = SecValueList.split(",");
                    List<String> secList = new ArrayList<String>();
                    for (String str : tempSecList) {
                        secList.add(str.trim());
//
                    }
//
//
//
//
                    if (inMap.get(retObj.getFieldValueString(looper, 1)) != null && !(((List<String>) inMap.get(retObj.getFieldValueString(looper, 1))).contains("All"))) {
                        /*
                         * Object defaultVal=
                         * paramValue.get(retObj.getFieldValueString(looper,
                         * 1)); String actualParamValue="";
                         */
                        String actualParamValue = inMap.get(retObj.getFieldValueString(looper, 1)).toString();

//                     String[] A = actualParamValue.split(",");
                        List<String> actulaValList = (List<String>) inMap.get(retObj.getFieldValueString(looper, 1));
                        //
                        if (actulaValList != null && actulaValList.size() > 0) {
                            for (int i = 0; i < actulaValList.size(); i++) {
                                if (!secList.contains(actulaValList.get(i))) {
//
                                    //
                                    actulaValList.set(i, "-99");
//                                 actualParamValue=actualParamValue.replace(A[i], "-99");
                                }
                            }
                        }
//
//                    inMap.put(retObj.getFieldValueString(looper, 1), actualParamValue);
                        inMap.put(retObj.getFieldValueString(looper, 1), actulaValList);
                        inMapsnew.put(retObj.getFieldValueString(looper, 1), actulaValList);
                    } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                        inMap.put(retObj.getFieldValueString(looper, 1), secList);
                        inMapsnew.put(retObj.getFieldValueString(looper, 1), secList);
                    } else {
                        inMap.put(retObj.getFieldValueString(looper, 1), secList);
                        inMapsnew.put(retObj.getFieldValueString(looper, 1), secList);
                    }
                }
//
//
//
            }
//
        }
//        ProgenLog.log(ProgenLog.FINE, this, "getSecFactFilter", "Exit " + "addSecurityClause");
        logger.info("Exit addSecurityClause");
    }

    /**
     * @return the multiViewBy
     */
    public boolean isMultiViewBy() {
        return this.ismultiPeriodsexists;
    }

    /**
     * @param multiViewBy the multiViewBy to set
     */
    public void setMultiViewBy(boolean multiViewBy) {
        this.ismultiPeriodsexists = multiViewBy;
    }

    /**
     * @return the multiViewBys
     */
    public ArrayList getMultiViewBys() {
        return multiViewBys;
    }

    /**
     * @param multiViewBys the multiViewBys to set
     */
    public void addMultiViewBys(Object viewby) {
        this.multiViewBys.add(viewby);
    }

    /**
     * @return the inMap
     */
    public HashMap getInMap() {
        return inMap;
    }

    /**
     * @param inMap the inMap to set
     */
    public void setInMap(HashMap inMap) {
        this.inMap = (HashMap<String, List<String>>) inMap.clone();
    }

    /**
     * @return the notInMap
     */
    public HashMap getNotInMap() {
        return notInMap;
    }

    /**
     * @param notInMap the notInMap to set
     */
    public void setNotInMap(HashMap notInMap) {
        this.notInMap = notInMap;
    }

    /**
     * @return the likeMap
     */
    public HashMap getLikeMap() {
        return likeMap;
    }

    /**
     * @param likeMap the likeMap to set
     */
    public void setLikeMap(HashMap likeMap) {
        this.likeMap = likeMap;
    }

    /**
     * @return the notLikeMap
     */
    public HashMap getNotLikeMap() {
        return notLikeMap;
    }

    /**
     * @param notLikeMap the notLikeMap to set
     */
    public void setNotLikeMap(HashMap notLikeMap) {
        this.notLikeMap = notLikeMap;
    }
    //added by Nazneen for finding column is summarized

    public String getSummBucketFormula(String elementId) {
        PbDb pbdb = new PbDb();
        PbReturnObject pbRtn = null;
        String dimId = "";
        String SummBucketFormula = "";
        String getUserColType = "select IS_SUMMARIZED_BUCKET,DIM_ID from prg_user_all_info_details where element_id in (" + elementId + ")";
        try {
            pbRtn = pbdb.execSelectSQL(getUserColType);
            if (pbRtn != null && pbRtn.getRowCount() > 0) {
                isSummBucket = pbRtn.getFieldValueString(0, 0);
                dimId = pbRtn.getFieldValueString(0, 1);
                if (isSummBucket.equalsIgnoreCase("Y")) {
                    isDimSegOnSumm = true;
                    SaveBucketsBD bd = new SaveBucketsBD();
                    SummBucketFormula = bd.readFormulaFromFile(dimId, this.filePath);
                }
            }
        } catch (SQLException ex) {
            logger.error("Exception", ex);
        }
        return SummBucketFormula;
    }
    /*
     *
     * ArrayList allTablesforReplace = new ArrayList(); HashMap<String,
     * ArrayList> allTablesforReplaceColumns = new HashMap(); HashMap<String,
     * ArrayList> allTablesforReplacementTables= new HashMap();
     */

    public void getTableAndColumnName(String elementId, ArrayList allTablesforReplace, HashMap<String, ArrayList> allTablesforReplaceColumns) {
        PbDb pbdb = new PbDb();
        PbReturnObject pbRtn = null;
        String table_name = "";
        String column_name = "";
        String getUserColType = "select buss_table_name, buss_col_name, SUB_FOLDER_NAME  from prg_user_all_info_details where element_id in (" + elementId + ")";
        try {
            pbRtn = pbdb.execSelectSQL(getUserColType);
            if (pbRtn != null && pbRtn.getRowCount() > 0) {
                table_name = pbRtn.getFieldValueString(0, 0);
                column_name = pbRtn.getFieldValueString(0, 1);


                if (!pbRtn.getFieldValueString(0, 2).equalsIgnoreCase("FACTS")) {

                    addAllTablesforReplace(allTablesforReplace, table_name);
                    addAllTablesforReplaceColumns(allTablesforReplaceColumns, table_name, column_name);
                }

            }
        } catch (SQLException ex) {
            logger.error("Exception", ex);
        }

        // return SummBucketFormula;
    }
//added by Nazneen for replacing nvl and trun from finalsql

    public void replaceSpecialCharsAccToDb(String userConnType) {
        if (userConnType.equalsIgnoreCase("Sqlserver")) {
            finalSqlNew_AO = finalSqlNew_AO.replace("nvl(", "COALESCE(");
            finalSqlNew_AO = finalSqlNew_AO.replace("Nvl(", "COALESCE(");
            finalSqlNew_AO = finalSqlNew_AO.replace("NVL(", "COALESCE(");
            omsql_AO = omsql_AO.replace("nvl(", "COALESCE(");
            omsql_AO = omsql_AO.replace("Nvl(", "COALESCE(");
            omsql_AO = omsql_AO.replace("NVL(", "COALESCE(");

            finalSqlNew_AO = finalSqlNew_AO.replace("trunc(", "convert(date,");
            finalSqlNew_AO = finalSqlNew_AO.replace("Trunc(", "convert(date,");
            finalSqlNew_AO = finalSqlNew_AO.replace("TRUNC(", "convert(date,");
            omsql_AO = omsql_AO.replace("trunc(", "convert(date,");
            omsql_AO = omsql_AO.replace("Trunc(", "convert(date,");
            omsql_AO = omsql_AO.replace("TRUNC(", "convert(date,");
        }
        if (userConnType.equalsIgnoreCase("Postgres") || userConnType.equalsIgnoreCase("PostgreSQL")) {
            finalSqlNew_AO = finalSqlNew_AO.replace("nvl(", "COALESCE(");
            finalSqlNew_AO = finalSqlNew_AO.replace("Nvl(", "COALESCE(");
            finalSqlNew_AO = finalSqlNew_AO.replace("NVL(", "COALESCE(");

            finalSqlNew_AO = finalSqlNew_AO.replace("trunc(", "date_trunc('day',");
            finalSqlNew_AO = finalSqlNew_AO.replace("Trunc(", "date_trunc('day',");
            finalSqlNew_AO = finalSqlNew_AO.replace("TRUNC(", "date_trunc('day',");

            omsql_AO = omsql_AO.replace("nvl(", "COALESCE(");
            omsql_AO = omsql_AO.replace("Nvl(", "COALESCE(");
            omsql_AO = omsql_AO.replace("NVL(", "COALESCE(");

            finalSqlNew_AO = finalSqlNew_AO.replace("trunc(", "date_trunc('day',");
            finalSqlNew_AO = finalSqlNew_AO.replace("Trunc(", "date_trunc('day',");
            finalSqlNew_AO = finalSqlNew_AO.replace("TRUNC(", "date_trunc('day',");
        }
        if (userConnType.equalsIgnoreCase("mysql")) {
            finalSqlNew_AO = finalSqlNew_AO.replace("nvl(", "ifNULL(");
            finalSqlNew_AO = finalSqlNew_AO.replace("Nvl(", "ifNULL(");
            finalSqlNew_AO = finalSqlNew_AO.replace("NVL(", "ifNULL(");

            finalSqlNew_AO = finalSqlNew_AO.replace("trunc(", "(");
            finalSqlNew_AO = finalSqlNew_AO.replace("Trunc(", "(");
            finalSqlNew_AO = finalSqlNew_AO.replace("TRUNC(", "(");

            omsql_AO = omsql_AO.replace("nvl(", "ifNULL(");
            omsql_AO = omsql_AO.replace("Nvl(", "ifNULL(");
            omsql_AO = omsql_AO.replace("NVL(", "ifNULL(");

            omsql_AO = omsql_AO.replace("trunc(", "(");
            omsql_AO = omsql_AO.replace("Trunc(", "(");
            omsql_AO = omsql_AO.replace("TRUNC(", "(");
        }
    }
    //added by Nazneen for replacing nvl and trun from finalsql

    public String replaceSpecialSymbolAccToDB(String userConnType, String finalSql) {
        if (userConnType.equalsIgnoreCase("Sqlserver")) {
            finalSql = finalSql.replace("nvl(", "COALESCE(");
            finalSql = finalSql.replace("Nvl(", "COALESCE(");
            finalSql = finalSql.replace("NVL(", "COALESCE(");

            finalSql = finalSql.replace("trunc(", "convert(date,");
            finalSql = finalSql.replace("Trunc(", "convert(date,");
            finalSql = finalSql.replace("TRUNC(", "convert(date,");
        }
        if (userConnType.equalsIgnoreCase("Postgres") || userConnType.equalsIgnoreCase("PostgreSQL")) {//
            finalSql = finalSql.replace("nvl(", "COALESCE(");
            finalSql = finalSql.replace("Nvl(", "COALESCE(");
            finalSql = finalSql.replace("NVL(", "COALESCE(");

            finalSql = finalSql.replace("trunc(", "date_trunc('day',");
            finalSql = finalSql.replace("Trunc(", "date_trunc('day',");
            finalSql = finalSql.replace("TRUNC(", "date_trunc('day',");
        }
        if (userConnType.equalsIgnoreCase("mysql")) {
            finalSql = finalSql.replace("nvl(", "ifNULL(");
            finalSql = finalSql.replace("Nvl(", "ifNULL(");
            finalSql = finalSql.replace("NVL(", "ifNULL(");

            finalSql = finalSql.replace("trunc(", "(");
            finalSql = finalSql.replace("Trunc(", "(");
            finalSql = finalSql.replace("TRUNC(", "(");
        }
        return finalSql;
    }

    public String replaceUnwantedCharsOViewByCol(String OViewByColNew1) {
        OViewByColNew1 = OViewByColNew1.replace("SUM(", "(").replace("AVG(", "(").replace("MIN(", "(").replace("MAX(", "(").replace("COUNT(", "(").replace("COUNTDISTINCT(", "(");
        OViewByColNew1 = OViewByColNew1.replace("AS VIEWBY1", "").replace("AS VIEWBY2", "").replace("AS VIEWBY3", "").replace("AS VIEWBY4", "").replace("AS VIEWBY5", "").replace("AS VIEWBY6", "");
        OViewByColNew1 = OViewByColNew1.replace("AS ORDER1", "").replace("AS ORDER2", "").replace("AS ORDER3", "").replace("AS ORDER4", "").replace("AS ORDER5", "").replace("AS ORDER6", "");
        return OViewByColNew1;
    }

    public String replaceUnwantedCharsOorderByCol(String OorderByColNew1) {
        OorderByColNew1 = OorderByColNew1.replace("SUM(", "(").replace("AVG(", "(").replace("MIN(", "(").replace("MAX(", "(").replace("COUNT(", "(").replace("COUNTDISTINCT(", "(");
        OorderByColNew1 = OorderByColNew1.replace("AS VIEWBY1", "").replace("AS VIEWBY2", "").replace("AS VIEWBY3", "").replace("AS VIEWBY4", "").replace("AS VIEWBY5", "").replace("AS VIEWBY6", "");
        OorderByColNew1 = OorderByColNew1.replace("AS ORDER1", "").replace("AS ORDER2", "").replace("AS ORDER3", "").replace("AS ORDER4", "").replace("AS ORDER5", "").replace("AS ORDER6", "");
        return OorderByColNew1;
    }
    //added by Nazneen for replacing progentime based on conn type

    public String getProgenTimeReplaces(String userConnType, String mTemp) {
        if (userConnType.equalsIgnoreCase("ORACLE")) {
            mTemp = mTemp.replace("@PROGENTIME@@ST_DATE", "to_date('" + pbTime.st_d + "','mm/dd/yyyy Hh24:mi:ss ')");
            mTemp = mTemp.replace("@PROGENTIME@@ED_DATE", "to_date('" + pbTime.ed_d + "','mm/dd/yyyy Hh24:mi:ss ')");
        } else if (userConnType.equalsIgnoreCase("mysql")) {
            mTemp = mTemp.replace("@PROGENTIME@@ST_DATE", "str_to_date('" + pbTime.st_d + "','%m/%d/%Y %H:%i:%s ')");
            mTemp = mTemp.replace("@PROGENTIME@@ED_DATE", "str_to_date('" + pbTime.ed_d + "','%m/%d/%Y %H:%i:%s ')");
        } else {
            mTemp = mTemp.replace("@PROGENTIME@@ST_DATE", "convert(datetime,'" + pbTime.st_d + "',120)");
            mTemp = mTemp.replace("@PROGENTIME@@ED_DATE", "convert(datetime,'" + pbTime.ed_d + "',120)");

        }
        return mTemp;
    }
    //added by Nazneen for replacing progentime based on timedetails

    public String replaceDataWithProGenTime(String mTemp, String timeDetails, String userConnType) {
        String endDate = "";
        //added by Mohit for replacing progentime based on timedetails
        if (userConnType.equalsIgnoreCase("ORACLE")) {
            endDate = "to_date('" + pbTime.ed_d + "','mm/dd/yyyy Hh24:mi:ss ')";
        } else if (userConnType.equalsIgnoreCase("mysql")) {

            endDate = "str_to_date('" + pbTime.ed_d + "','%m/%d/%Y %H:%i:%s ')";
        } else {

            endDate = "convert(datetime,'" + pbTime.ed_d + "',120))";
        }


        if (timeDetails.equalsIgnoreCase("Week")) {
            mTemp = mTemp.replace("@PROGENTIME@@ST_DATE", "PROGEN_TIME.CW_ST_DATE");
            mTemp = mTemp.replace("@PROGENTIME@@ED_DATE", "(CASE WHEN PROGEN_TIME.CW_END_DATE >" + endDate + " THEN " + endDate + " ELSE PROGEN_TIME.CW_END_DATE END)");

        } else if (timeDetails.equalsIgnoreCase("Month")) {
            mTemp = mTemp.replace("@PROGENTIME@@ST_DATE", "PROGEN_TIME.CM_ST_DATE");
            mTemp = mTemp.replace("@PROGENTIME@@ED_DATE", "(CASE WHEN PROGEN_TIME.CM_END_DATE >" + endDate + " THEN " + endDate + " ELSE PROGEN_TIME.CM_END_DATE END)");


        } else if (timeDetails.equalsIgnoreCase("Qtr")) {
            mTemp = mTemp.replace("@PROGENTIME@@ST_DATE", "PROGEN_TIME.CQ_ST_DATE");
            mTemp = mTemp.replace("@PROGENTIME@@ED_DATE", "(CASE WHEN PROGEN_TIME.CQ_END_DATE >" + endDate + " THEN " + endDate + " ELSE PROGEN_TIME.CQ_END_DATE END)");


        } else if (timeDetails.equalsIgnoreCase("Year")) {
            mTemp = mTemp.replace("@PROGENTIME@@ST_DATE", "PROGEN_TIME.CY_ST_DATE");
            mTemp = mTemp.replace("@PROGENTIME@@ED_DATE", "(CASE WHEN PROGEN_TIME.CY_END_DATE >" + endDate + " THEN " + endDate + " ELSE PROGEN_TIME.CY_END_DATE END)");


        } else {
            mTemp = mTemp.replace("@PROGENTIME@@ST_DATE", "PROGEN_TIME.DDATE");
            mTemp = mTemp.replace("@PROGENTIME@@ED_DATE", "PROGEN_TIME.DDATE");
        }
        return mTemp;
    }
    //added by nazneen on 11 March 2015 for the purpose of summarized measures bucket new layer sql

    public String getFinalSqlSummarized(String insql, String[] pinsql, String finalSqlSummarized) {
        String allColNamesArr[] = insql.split(",");
        for (int k = 0; k < allColNamesArr.length; k++) {
            String allColNames = allColNamesArr[k].toString();
            if (allColNames.contains("B_")) {
                int valIndex1 = allColNames.indexOf("B_");
                String valTemp2 = allColNames.substring(valIndex1);
                for (int l = 0; l < pinsql.length; l++) {
                    String allColNamesArr1[] = pinsql[l].toString().split(",");
                    for (int m = 0; m < allColNamesArr1.length; m++) {
                        if (allColNames.contains("NULL") || allColNames.contains("null")) {
                            if (allColNamesArr1[m].toString().contains(valTemp2) && !allColNamesArr1[m].toString().contains("NULL")) {
                                allColNames = allColNamesArr1[m].toString();
                            }
                        }
                    }
                }
            }
            if (allColNames.contains(" as ")) {
                String colNameArr[] = allColNames.split(" as ");
                if (allColNames.contains("(")) {
                    int index0 = allColNames.indexOf("(");
                    int index1 = allColNames.lastIndexOf(")");
                    String valIndex = allColNames.substring(index0 + 1, index1);
                    finalSqlSummarized += "," + allColNames.replace(valIndex, colNameArr[1]) + "";
                } else {
                    int index0 = allColNames.indexOf(" as ");
                    finalSqlSummarized += "," + allColNames.toString();
                }
            } else if (!allColNames.equalsIgnoreCase(" ")) {
                finalSqlSummarized += "," + allColNamesArr[k].toString();
            }
        }
        return finalSqlSummarized;
    }
    //added by nazneen on 11 March 2015 for the purpose of summarized measures bucket new layer sql--prior and change %

    public String getFinalSqlSummarizedReplaced(String insql, String[] pinsql, String finalSqlSummarized) {
        String allColNamesArr[] = insql.split(",");
        for (int k = 0; k < allColNamesArr.length; k++) {
            String valTemp2 = "";
            String allColNames = allColNamesArr[k].toString();
            if (allColNames.contains("B_")) {
                int valIndex1 = allColNames.indexOf("B_");
                valTemp2 = allColNames.substring(valIndex1);
                for (int l = 0; l < pinsql.length; l++) {
                    String allColNamesArr1[] = pinsql[l].toString().split(",");
                    for (int m = 0; m < allColNamesArr1.length; m++) {
                        if (allColNames.toUpperCase().contains("NULL")) {
                            if (allColNamesArr1[m].toString().contains(valTemp2) && !allColNamesArr1[m].toString().contains("NULL")) {
                                allColNames = allColNamesArr1[m].toString();
                            }
                        }
                    }
                }
                if (!allColNames.toUpperCase().contains("NULL")) {
                    String replaceStr = "NULL[*]0 AS " + valTemp2.toUpperCase().trim();
                    String replaceStr1 = "NULL[*]0 " + valTemp2.toUpperCase().trim();
                    finalSqlSummarized = finalSqlSummarized.toUpperCase();
                    if (finalSqlSummarized.contains(valTemp2)) {
                        if (allColNames.contains(" as ")) {
                            String colNameArr[] = allColNames.split(" as ");
                            if (allColNames.contains("(")) {
                                int index0 = allColNames.indexOf("(");
                                int index1 = allColNames.lastIndexOf(")");
                                String valIndex = allColNames.substring(index0 + 1, index1);
                                String token = allColNames.replace(valIndex, colNameArr[1]) + "";
                                token = token.toUpperCase();
                                finalSqlSummarized = finalSqlSummarized.replaceAll(replaceStr, token).replaceAll(replaceStr1, token);
                            } else {
                                finalSqlSummarized = finalSqlSummarized.replaceAll(replaceStr, allColNames.toUpperCase()).replaceAll(replaceStr1, allColNames.toUpperCase());
                            }
                        } else if (!allColNames.equalsIgnoreCase(" ")) {
                            finalSqlSummarized = finalSqlSummarized.replaceAll(replaceStr, allColNames.toUpperCase()).replaceAll(replaceStr1, allColNames.toUpperCase());
                        }
                    }
                }
            }
        }
        return finalSqlSummarized;
    }
    //by nazneen to set the object meta details in json
    //modified by mayank for enabling calender time on reports

    public void setDataToObjectQuery() {
        ReportObjectMeta roMeta = new ReportObjectMeta();
        String st_Date = startDate_AO;
        String end_Date = endDate_AO;
        String timeLevel_AO = timeDetails.get(0).toString();;
        String dateValues_AO = "stDate~" + st_Date + ";endDate~" + end_Date + ";timeLevel_AO~" + timeLevel_AO;
        roMeta.setOsql_AO(osql_AO);
        roMeta.setOViewByCol_AO(OViewByCol_AO);
        roMeta.setOorderByCol_AO(OorderByCol_AO);
        roMeta.setOmsql_AO(omsql_AO);
        roMeta.setOmViewByCol_AO(OmViewByCol_AO);
        roMeta.setOmorderByCol_AO(OmorderByCol_AO);
        roMeta.setColOrderByCol_AO(ColOrderByCol_AO);
        roMeta.setOsqlGroup_AO(osqlGroup_AO);
        roMeta.setFinalViewByCol_AO(finalViewByCol_AO);
        roMeta.setoWrapper_AO(oWrapper_AO);
        roMeta.setTableName_AO(tableName_AO);
        roMeta.setFilters_AO(filters_AO);
        roMeta.setIsAOEnable(isAOEnable);
        roMeta.setFinalSqlNew_AO(finalSqlNew_AO);
        roMeta.setDateValues_AO(dateValues_AO);
        Gson gson = new Gson();
        FileReadWrite fileReadWrite = new FileReadWrite();
        File file;
        file = new File(this.filePath+"/analyticalobject");
        String path = file.getAbsolutePath();
        String fName = path + File.separator + "R_AO_" + reportId + ".json";
        File f = new File(path);
        File file1 = new File(fName);
        f.mkdirs();
        File datafile = new File(this.filePath+"/analyticalobject/R_AO_" + reportId + ".json");
        if (!datafile.exists()) {
            try {
                datafile.createNewFile();
            } catch (IOException ex) {
                logger.error("Exception", ex);
            }
        }
        fileReadWrite.writeToFile(this.filePath+"/analyticalobject/R_AO_" + reportId + ".json", gson.toJson(roMeta));
    }
    // added by Mayank to get User Connection Type

    private String getuserConnType(ArrayList qryColumns, ArrayList rowViewbyCols) {
        String userConnType = null;
        GetConnectionType gettypeofconn = new GetConnectionType();
        if (qryColumns != null && qryColumns.size() > 0) {
            userConnType = gettypeofconn.getConTypeByElementId(this.qryColumns.get(0).toString());
        } else if (getRowViewbyCols() != null && getRowViewbyCols().size() > 0) {
            userConnType = gettypeofconn.getConTypeByElementId(getRowViewbyCols().get(0).toString());
        }
        return userConnType;
    }
    // added by Mayank

    private int getTotalRowViewBy(ArrayList rowViewbyCols, boolean innerViewBy) {
        int totalRowViewBy = 0;
        if (getRowViewbyCols() == null || getRowViewbyCols().isEmpty()) {
            totalRowViewBy = 0;
            isKpi = true;

        }
        if (getColViewbyCols() != null && getRowViewbyCols() != null) {
            totalRowViewBy = getRowViewbyCols().size() + getColViewbyCols().size();
        } else if (getRowViewbyCols() != null && getRowViewbyCols().size() > 0) {
            totalRowViewBy = getRowViewbyCols().size();
        } else if (isInnerViewBy) {
            totalRowViewBy = getRowViewbyCols().size();
        } else {
            totalRowViewBy = 0;
            isKpi = true;
        }
        return totalRowViewBy;
    }
// added by Mayank

    private void setGlobalBucketVariable(String isNewViewBy, String isCrossTab, String replaceIndex) {
        if (isNewViewBy.equalsIgnoreCase("Y")) {
            isBucketNewViewBy = true;
            isBucketCrossTab = false;
            isBucketReplace = false;
        } else if (isCrossTab.equalsIgnoreCase("Y")) {
            isBucketNewViewBy = false;
            isBucketCrossTab = true;
            isBucketReplace = false;
            //CrossTab = true;

        } else {
            isBucketNewViewBy = false;
            isBucketCrossTab = false;
            isBucketReplace = true;
            if (replaceIndex.equalsIgnoreCase("all")) {
                bucketReplaceIndex = "0";
            } else {
                bucketReplaceIndex = replaceIndex;
            }
        }
    }
    // added by Mayank

    private void addElementIdToSummFactArray(HashMap<String, List<String>> likeMap, HashMap<String, List<String>> notLikeMap, HashMap parameterQuery, HashMap excludedParameters, HashMap<String, List<String>> inMap, HashMap<String, List<String>> notInMap) {
        if (likeMap != null && likeMap.size() > 0) {
            String eleIds[] = (String[]) likeMap.keySet().toArray(new String[0]);


            for (int m = 0; m < eleIds.length; m++) {
                if (!eleIds[m].equalsIgnoreCase("Time")) {
                    if (ValidateParamValues(likeMap.get(eleIds[m]))) {
                        if (!(summFactAllArray != null && (!summFactAllArray.isEmpty()) && (summFactAllArray.contains(eleIds[m])))) {
                            summFactAllArray.add(eleIds[m]);
                        }
                    }
                }

            }

        }

        if (notLikeMap != null && notLikeMap.size() > 0) {
            String eleIds[] = (String[]) notLikeMap.keySet().toArray(new String[0]);


            for (int m = 0; m < eleIds.length; m++) {
                if (!eleIds[m].equalsIgnoreCase("Time")) {
                    if (ValidateParamValues(notLikeMap.get(eleIds[m]))) {
                        if (summFactAllArray != null && (!summFactAllArray.isEmpty()) && (!summFactAllArray.contains(eleIds[m]))) {
                            summFactAllArray.add(eleIds[m]);
                        }
                    }
                }

            }

        }

        if (parameterQuery != null && parameterQuery.size() > 0) {
            String eleIds[] = (String[]) parameterQuery.keySet().toArray(new String[0]);


            for (int m = 0; m < eleIds.length; m++) {
                if (!eleIds[m].equalsIgnoreCase("Time")) {
                    {
                        summFactAllArray.add(eleIds[m]);
                    }
                }

            }

        }

        if (getParamValue() != null && getParamValue().size() > 0) {
            String eleIds[] = (String[]) getParamValue().keySet().toArray(new String[0]);


            for (int m = 0; m < eleIds.length; m++) {
                if (!eleIds[m].equalsIgnoreCase("Time")) {
                    if (ValidateParamValues(getParamValue().get(eleIds[m]))) {
                        summFactAllArray.add(eleIds[m]);
                    }
                }

            }

        }

        if (excludedParameters != null && excludedParameters.size() > 0) {
            String eleIds[] = (String[]) excludedParameters.keySet().toArray(new String[0]);


            for (int m = 0; m < eleIds.length; m++) {
                if (!eleIds[m].equalsIgnoreCase("Time")) {
                    if (ValidateParamValues(excludedParameters.get(eleIds[m]))) {
                        if (!(summFactAllArray != null && (!summFactAllArray.isEmpty()) && (summFactAllArray.contains(eleIds[m])))) {
                            summFactAllArray.add(eleIds[m]);
                        }
                    }
                }

            }

        }

        if (inMap != null && inMap.size() > 0) {
            String eleIds[] = (String[]) inMap.keySet().toArray(new String[0]);


            for (int m = 0; m < eleIds.length; m++) {
                if (!eleIds[m].equalsIgnoreCase("Time")) {
                    if (ValidateParamValues(inMap.get(eleIds[m]))) {
                        if (!(summFactAllArray != null && (!summFactAllArray.isEmpty()) && (summFactAllArray.contains(eleIds[m])))) {
                            summFactAllArray.add(eleIds[m]);
                        }
                    }
                }

            }

        }

        if (notInMap != null && notInMap.size() > 0) {
            String eleIds[] = (String[]) notInMap.keySet().toArray(new String[0]);


            for (int m = 0; m < eleIds.length; m++) {
                if (!eleIds[m].equalsIgnoreCase("Time")) {
                    if (ValidateParamValues(notInMap.get(eleIds[m]))) {
                        if (!(summFactAllArray != null && (!summFactAllArray.isEmpty()) && (summFactAllArray.contains(eleIds[m])))) {
                            summFactAllArray.add(eleIds[m]);
                        }
                    }
                }

            }

        }
    }
    //Function Added by Amar to change prior on Sep 15,2015

    private String changeForPTimeInfo(String orderByClause) {
        orderByClause = orderByClause.toUpperCase();

//        if (orderByClause.contains("PRG_DAY_DETAILS.WEEK_DAY_NAME")
//                || orderByClause.contains("PRG_DAY_DETAILS.DAY_OF_WEEK")
//                || orderByClause.contains("PRG_DAY_DETAILS.MONTH_DAY")
//                || orderByClause.contains("PRG_DAY_DETAILS.DAYSOFYEAR")
//                || orderByClause.contains("PRG_DAY_DETAILS.WEEK_OF_YEAR")
//                || orderByClause.contains("PRG_DAY_DETAILS.WEEK_OF_MONTH")) {
//            timeLevel = "Day";
//        } else if (orderByClause.contains("PRG_DAY_DETAILS.MONTH_NAME")
//                || orderByClause.contains("PRG_DAY_DETAILS.MONTH_SHORT_NAME")
//                ||orderByClause.contains("PRG_DAY_DETAILS.MONTH_YEAR")
//                || orderByClause.contains("PRG_DAY_DETAILS.MONTH_ST_DATE")) {
//            if (timeLevel == null) {
//                timeLevel = "Month";
//            } else if (timeLevel.equalsIgnoreCase("Day")) {
//                timeLevel = "Day";
//            } else if (timeLevel.equalsIgnoreCase("Week")) {
//                timeLevel = "Day";
//            } else {
//                timeLevel = "Month";
//            }
//        } else if (orderByClause.contains("PRG_DAY_DETAILS.QTR_NAME")
//                || orderByClause.contains("PRG_DAY_DETAILS.QTR_YEAR")
//                || orderByClause.contains("PRG_DAY_DETAILS.QTR_YEAR_INT")) {
//            if (timeLevel == null) {
//                timeLevel = "Qtr";
//            } else if (timeLevel.equalsIgnoreCase("Day")) {
//                timeLevel = "Day";
//            } else if (timeLevel.equalsIgnoreCase("Week")) {
//                timeLevel = "Day";
//            } else if (timeLevel.equalsIgnoreCase("Month")) {
//                timeLevel = "Month";
//            } else {
//                timeLevel = "Qtr";
//            }
//        } else if (orderByClause.contains("PRG_DAY_DETAILS.YEAR_TEXT_NAME")
//                || orderByClause.contains("PRG_DAY_DETAILS.YEAR_NAME")) {
//            ////
//            if (timeLevel == null) {
//                timeLevel = "Year";
//            } else if (timeLevel.equalsIgnoreCase("Day")) {
//                timeLevel = "Day";
//            } else if (timeLevel.equalsIgnoreCase("Week")) {
//                timeLevel = "Day";
//            } else if (timeLevel.equalsIgnoreCase("Month")) {
//                timeLevel = "Month";
//            } else if (timeLevel.equalsIgnoreCase("QTR_NAME")) {
//                timeLevel = "Qtr";
//            } else {
//                timeLevel = "Year";
//            }
//        }
        orderByClause = orderByClause.replace("PRG_DAY_DETAILS.WEEK_DAY_NAME", "PRG_DAY_DETAILS.DAY_OF_WEEK");
        orderByClause = orderByClause.replace("PRG_DAY_DETAILS.MONTH_NAME", "PRG_DAY_DETAILS.MONTH_OF_YEAR");
        orderByClause = orderByClause.replace("PRG_DAY_DETAILS.MONTH_SHORT_NAME", "PRG_DAY_DETAILS.MONTH_OF_YEAR");
        orderByClause = orderByClause.replace("PRG_DAY_DETAILS.MONTH_YEAR", "PRG_DAY_DETAILS.NYM_CUST_NAME");
        orderByClause = orderByClause.replace("PRG_DAY_DETAILS.QTR_NAME", "PRG_DAY_DETAILS.QTR_OF_YEAR");
        orderByClause = orderByClause.replace("PRG_DAY_DETAILS.QTR_YEAR", "PRG_DAY_DETAILS.NYQ_CUST_NAME");
        orderByClause = orderByClause.replace("PRG_DAY_DETAILS.YEAR_TEXT_NAME", "PRG_DAY_DETAILS.YEAR_NAME");
        orderByClause = orderByClause.replace("PRG_DAY_DETAILS.CUSTOM_DATE_FORMAT1", "PRG_DAY_DETAILS.DDATE");
        orderByClause = orderByClause.replace("PRG_DAY_DETAILS.CUSTOM_DATE_FORMAT2", "PRG_DAY_DETAILS.DDATE");
//        orderByClause = orderByClause.replace("PRG_DAY_DETAILS.CUSTOM_DATE_FORMAT3", "PRG_DAY_DETAILS.DDATE");
        orderByClause = orderByClause.replace("PRG_DAY_DETAILS.CUSTOM_DATE_FORMAT4", "PRG_DAY_DETAILS.DDATE");
        orderByClause = orderByClause.replace("PRG_DAY_DETAILS.YEAR_NAME", "PRG_DAY_DETAILS.NY_CUST_NAME");







        return (orderByClause);
    }
    //Function Added by Amar to change prior on Sep 15,2015

    private String changeForOTimeInfo(String orderByClause) {
        orderByClause = orderByClause.toUpperCase();


        orderByClause = orderByClause.replace("PRG_DAY_DETAILS.MONTH_ST_DATE", "PRG_DAY_DETAILS.NYM_ST_DATE");
        //orderByClause = orderByClause.replace("PRG_DAY_DETAILS.QTR_NAME", "PRG_DAY_DETAILS.QTR_OF_YEAR");
        orderByClause = orderByClause.replace("PRG_DAY_DETAILS.QTR_YEAR_INT", "PRG_DAY_DETAILS.NYQ_ST_DATE");
        orderByClause = orderByClause.replace("PRG_DAY_DETAILS.YEAR_NAME", "PRG_DAY_DETAILS.NY_CUST_NAME");







        return (orderByClause);
    }

    private String getmTempString(String mTemp, String userConnType) {
        if (pbTime.st_d != null && pbTime.st_d.trim().length() > 0) {
            if (qryType.equalsIgnoreCase("Trend")) {
                if (timeDetails.get(0).toString().equalsIgnoreCase("Day")) {
                    if (timeDetails.get(1).toString().equalsIgnoreCase("PRG_STD")
                            || timeDetails.get(1).toString().equalsIgnoreCase("PRG_COHORT")) {
                        int cPos = 3;
                        if (timeDetails.get(1).toString().equalsIgnoreCase("PRG_STD")) {
                            cPos = 3;
                        } else if (timeDetails.get(1).toString().equalsIgnoreCase("PRG_COHORT")) {
                            cPos = 6;
                        }
                        mTemp = replaceDataWithProGenTime(mTemp, timeDetails.get(cPos).toString(), userConnType);
                    } else {
                        mTemp = mTemp.replace("@PROGENTIME@@ST_DATE", "PROGEN_TIME.DDATE");
                        mTemp = mTemp.replace("@PROGENTIME@@ED_DATE", "PROGEN_TIME.DDATE");

                    }
                } else {
                    mTemp = getProgenTimeReplaces(userConnType, mTemp);
                }

            } else {
                mTemp = getProgenTimeReplaces(userConnType, mTemp);
            }
            if (userConnType.equalsIgnoreCase("ORACLE")) {

                mTemp = mTemp.replace("@PROGENTIME@@ED_DATE", "to_date('" + pbTime.ed_d + "','mm/dd/yyyy Hh24:mi:ss ')");
            } else {
                mTemp = mTemp.replace("@PROGENTIME@@ED_DATE", "convert(datetime,'" + pbTime.ed_d + "',120)");
            }
        }
        return mTemp;
    }

    public String getFinalSqlAOQuery(ReportObjectQuery repObjQuery) {
        String filterClauses = "";
        String finalSqlAO = "";
        //  ReportObjectQuery repObjQuery = new ReportObjectQuery();
        filterClauses = repObjQuery.getFilterClause(inMaps, notInMaps, likeMaps, notLikeMaps);
        repObjQuery.filePath=this.filePath;
        setDataToObjectQuery();
        if (isAOReport || aoTableId != null) {
            finalSqlAO = repObjQuery.getObjectQueryForAOReport(aoTableId, reportId, filterClauses, rowViewbyCols, ColViewbyCols, startDate_AO, endDate_AO, timeLevel_AO);
        } else {
            finalSqlAO = repObjQuery.getObjectQueryAO(reportId, filterClauses, rowViewbyCols, ColViewbyCols, startDate_AO, endDate_AO, timeLevel_AO);
        }
        logger.info(" getFinalSqlAOQuery:::::" + finalSqlAO);
        return finalSqlAO;
    }
    //Start of code by Bhargavi

    public String generateQryForAO(Container container) {
        ProgenAOQuery objQuery = new ProgenAOQuery();
        ReportObjectQuery repObjQuery = new ReportObjectQuery();
        String query = "";
        String filterClause = "";
        PbReportCollection collect = null;
        HashMap<String, List> inMap = new HashMap<>();
        HashMap<String, List> notInMap = new HashMap<>();
        HashMap<String, List> likeMap = new HashMap<>();
        HashMap<String, List> notLikeMap = new HashMap<>();
        HashMap<String, List> filtermapFromSet = new HashMap<>();
        //ArrayList<String> measureType = new ArrayList<String>();
        ArrayList<String> timeDetails = new ArrayList<>();
        String aoId = container.AOId;
        timeDetails = container.getTimeDetailsArray();
        collect = container.getReportCollect();

        if (collect.operatorFilters != null && !collect.operatorFilters.isEmpty()) {

            if (collect.operatorFilters.get("IN") != null) {
                inMap = collect.operatorFilters.get("IN");
            }
            if (collect.operatorFilters.get("NOTIN") != null) {
                notInMap = collect.operatorFilters.get("NOTIN");
            }
            if (collect.operatorFilters.get("LIKE") != null) {
                likeMap = collect.operatorFilters.get("LIKE");
            }
            if (collect.operatorFilters.get("NOTLIKE") != null) {
                notLikeMap = collect.operatorFilters.get("NOTLIKE");
            }
        }
        filterClause = repObjQuery.getFilterClause(inMap, notInMap, likeMap, notLikeMap);
        List<String> gtMeasList = new ArrayList<String>();
        for(int i=0;i<collect.reportQryElementIds.size();i++){
            gtMeasList.add(i,"N");
        }
        objQuery.setAO_name("M_AO_" + aoId);
        objQuery.setTimeDetails(timeDetails);
        objQuery.setViewIdList((List<String>) collect.reportRowViewbyValues.clone());
        objQuery.setColViewIdList(collect.reportColViewbyValues);
        objQuery.setMeasureIdsList(collect.reportQryElementIds);
        objQuery.setMeasureIdsListGO(collect.reportQryElementIds);
        objQuery.setGrandTotalAggType(gtMeasList);
        setTimeTypeForAOMeasure(collect, objQuery);
        objQuery.setFilterClause(filterClause);
        objQuery.setFilterMap(filtermapFromSet);
        objQuery.setAggregationType(collect.reportQryAggregations);
        objQuery.setInnerViewByElementId("NONE");
        objQuery.collectMulticalender=collectMulticalender;
//                objQuery.setTimeDetailsArray(measureType);
        orgRowViewbyCols = new ArrayList();
        addRowViewBytoOriginalViewby();
        setnoOfDays();
        if (getColViewbyCols() != null && !getColViewbyCols().isEmpty()) {
            for (int i = 0; i < getColViewbyCols().size(); i++) {
                if (getColViewbyCols().get(i).toString().equalsIgnoreCase("Time")) {
                    qryType = "Trend";
                }
 //                   isTrend = true;
//                } else {
//                    colWhereClause = getColViewbyCols().get(i).toString();
//                }
//                CrossTab = false;
                rowViewbyCols.add(getColViewbyCols().get(i).toString());
            }
        }



        try {
            query = objQuery.generateAOQuery();
            NonViewByMap = objQuery.NonViewByMap;
        } catch (SQLException ex) {
            logger.error("Exception", ex);
        }
        return query;
    }

    private void setTimeTypeForAOMeasure(PbReportCollection collect, ProgenAOQuery objQuery) {
        ArrayList<String> measureType = new ArrayList<String>();
        for (int i = 0; i < collect.reportQryElementIds.size(); i++) {
            String measureId = collect.reportQryElementIds.get(i);
            if (measureId.contains("_PRIOR")) {
                measureType.add("PRIORPERIOD");
            } else if (measureId.contains("_MTD")) {
                measureType.add("MTD");
            } else if (measureId.contains("_PMTD")) {
                measureType.add("PMTD");
            } else if (measureId.contains("_PYMTD")) {
                measureType.add("PYMTD");
            } else if (measureId.contains("_QTD")) {
                measureType.add("QTD");
            } else if (measureId.contains("_PQTD")) {
                measureType.add("PQTD");
            } else if (measureId.contains("_PYQTD")) {
                measureType.add("PYQTD");
            } else if (measureId.contains("_YTD")) {
                measureType.add("YTD");
            } else if (measureId.contains("_PYTD")) {
                measureType.add("PYTD");
            } else if (measureId.contains("_MOMPer")) {
                measureType.add("MOMPer");
            } else if (measureId.contains("_MOYMPer")) {
                measureType.add("MOYMPer");
            } else if (measureId.contains("_QOQPer")) {
                measureType.add("QOQPer");
            } else if (measureId.contains("_QOYQPer")) {
                measureType.add("QOYQPer");
            } else if (measureId.contains("_YOYPer")) {
                measureType.add("YOYPer");
            } else if (measureId.contains("_WOWPer")) {
                measureType.add("WOWPer");
            } else if (measureId.contains("_WOYWPer")) {
                measureType.add("WOYWPer");
            } else if (measureId.contains("_MOM")) {
                measureType.add("MOM");
            } else if (measureId.contains("_MOYM")) {
                measureType.add("MOYM");
            } else if (measureId.contains("_QOQ")) {
                measureType.add("QOQ");
            } else if (measureId.contains("_QOYQ")) {
                measureType.add("QOYQ");
            } else if (measureId.contains("_YOY")) {
                measureType.add("YOY");
            } else if (measureId.contains("_WTD")) {
                measureType.add("WTD");
            } else if (measureId.contains("_PWTD")) {
                measureType.add("PWTD");
            } else if (measureId.contains("_PYWTD")) {
                measureType.add("PYWTD");
            } else if (measureId.contains("_WOW")) {
                measureType.add("WOW");
            } else if (measureId.contains("_WOYW")) {
                measureType.add("WOYW");
            } else {
                measureType.add("PERIOD");
            }
        }
        objQuery.setTimeDetailsArray(measureType);
    }
    //end of code by Bhargavi
}
