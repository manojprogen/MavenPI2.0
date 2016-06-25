/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.query;

import com.progen.contypes.GetConnectionType;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.*;
import org.apache.log4j.Logger;
import prg.db.PbDb;
import prg.db.PbReturnObject;
import utils.db.ProgenConnection;

/**
 *
 * @author Mayank
 */
public class ProgenAOQuery {

    public static Logger logger = Logger.getLogger(ProgenAOQuery.class);
    private List<String> viewBysList = new ArrayList<String>();
    private List<String> viewIdList = new ArrayList<String>();
    private List<String> colViewBysList = new ArrayList<String>();
    private ArrayList orgRowViewbyCols = new ArrayList<String>();
    private List<String> colViewIdList = new ArrayList<String>();
    private List<String> colViewIdCurrentList = new ArrayList<String>();
    private List<String> colViewIdChangeList = new ArrayList<String>();
    private List<String> colViewIdChangeListGT = new ArrayList<String>();
    private List<String> colViewIdChangeListAgg = new ArrayList<String>();
//        private List<String> colViewIdChangeCurrMeasure = new ArrayList<String>();
//        private List<String> colViewIdChangePriorMeasure = new ArrayList<String>();
    private List<String> colViewIdChangeFormula = new ArrayList<String>();
    private List<String> colViewIdPriorList = new ArrayList<String>();
    private List<String> measuresList = new ArrayList<String>();
    private List<String> measureIdsList = new ArrayList<String>();
    private List<String> measureIdsListGO = new ArrayList<String>();
    private List<String> aggregationType = new ArrayList<String>();
    private List<String> grandTotalAggType = new ArrayList<String>();
    private List<String> colAggregationType = new ArrayList<String>();
    private List<String> queryColName = new ArrayList<String>();
    private HashMap<String, List<String>> periodWiseMap = new HashMap<String, List<String>>();
    private HashMap<String, List<String>> periodWiseMapGT = new HashMap<String, List<String>>();
    private Map<String, List<String>> allInfoHaspMap = new HashMap<String, List<String>>();
    private Map<String, List<String>> allDependentsMap = new HashMap<String, List<String>>();
    private Map<String, Map<String, String>> FormulaReplaceList = new HashMap<String, Map<String, String>>();
    private String chartTimeClause = "";
    private String measureFilter = "";
    public HashMap NonViewByMap = new HashMap();
    /*
     * BUSS_TABLE_ID -- 0 BUSS_TABLE_NAME --1 BUSS_COL_NAME --2 USER_COL_NAME
     * --3, Alias ELEMENT_ID -- Key REF_ELEMENT_ID -- 4 REF_ELEMENT_TYPE --5
     * ACTUAL_COL_FORMULA -- 6 USER_COL_DESC -- 7 USER_COL_TYPE -- Summarized
     * --8 AGGREGATION_TYPE -- Like Sum, Min , Max Etc --9
     */
    private ArrayList<String> timeDetails = new ArrayList<String>();
    private HashMap<String, String> PriorElementIds = new HashMap<String, String>();
    private String innerViewByElementId;
    private String avoidProgenTime;
    private List<String> timeDetailsArray = new ArrayList<>();
    private HashMap<String, String> timeDetailMap = new HashMap<String, String>();
    private List<String> timeDetailsArrayDistinct = new ArrayList<>();
    private PbReportQueryResourceBundle resBundle = new PbReportQueryResourceBundle();
    private PbTimeRanges pbTime = new PbTimeRanges();
    private PbDb pbDb = new PbDb();
    private List<String> levelOneQuery = new ArrayList<>();
    private String[] levelOneQueryInner = null;
   // private String levelTwoQuery = new String();
    private StringBuilder levelTwoQuery = new StringBuilder();
   // private String levelThreeQuery = new String();
    private StringBuilder levelThreeQuery = new StringBuilder();
    private String levelFourQuery = new String(); // Only when we have summarized bucket
  //  private String levelFiveQuery = new String();
    private StringBuilder levelFiveQuery = new StringBuilder();
    private String userConnType = null;
    private String dataBaseHintInnerMost = "";
    private String dataBaseHintOuterMost = "";
    private String isChartTimeEnable = "";
//    private String levelOneViewInner = new String();
    private StringBuilder levelOneViewInner = new StringBuilder();
  //  private String levelOneViewInnerPrior = new String(); //
    private StringBuilder levelOneViewInnerPrior = new StringBuilder(); //
 //   private String levelTwoViewBy = new String();
    private StringBuilder levelTwoViewBy = new StringBuilder();
//    private String levelTwoGroupBy = new String();
    private StringBuilder levelTwoGroupBy = new StringBuilder();
//    private String levelThreeViewBy = new String();
    private StringBuilder levelThreeViewBy = new StringBuilder();
 //   private String levelFourViewBy = new String(); // Only when we have summarized bucket
    private StringBuilder levelFourViewBy =  new StringBuilder(); // Only when we have summarized bucket
 //   private String levelFiveViewBy = new String();
    private StringBuilder levelFiveViewBy = new StringBuilder();
//    private String levelFiveViewBy1 = new String();
    private StringBuilder levelFiveViewBy1 = new StringBuilder();
    private String AO_name;
    private String dateFlag_New;
    private String newAO_name;
  //  private String finalAOQuery = "";
    private StringBuilder finalAOQuery=new StringBuilder();
    private String filterClause = "";
    private List<String> filterElementName = new ArrayList<String>();
    private String dimSecurityClause = "";
    private String drillFormat;
    private HashMap<String, List> filterMap = new HashMap<String, List>();
    private String metaConnectionType = null;
    private String sortingType = null;
    private String chartType = null;
    private String chartRecords = null;
      public HashMap collectMulticalender = new HashMap();//added by Mohit for multicalendar

    public String generateAOQuery() throws SQLException {

       
//        getGrandTotalAggType().add("GT");
//        getGrandTotalAggType().add("PT");
//        getGrandTotalAggType().add("N");
//        getGrandTotalAggType().add("N");
//        getGrandTotalAggType().add("N");
        
        
        long start = System.currentTimeMillis();
        logger.info(viewIdList + "Calling AO Query: " + start);
        if (viewIdList != null) {
            setViewByListName();
        }
        filterElementName = new ArrayList(filterMap.keySet());
//added by Mohit for AO Calendar
         if (!collectMulticalender.isEmpty()) {
                    ArrayList Multicalender = ((ArrayList) collectMulticalender.get((collectMulticalender.keySet().toArray()[0])));
                    pbTime.timeTableName= Multicalender.get(8).toString();
                } 

        getCurrentPriorList();
        getAllmeasuresInfo();
        getAllParameterInfo();
        createMeasuresQuery();
        createViewByQuery();
        getUserConnection();
        setDatabaseHint();
        isThereAgg();
        buildAOQuery();


        //String measureQuery = getMeasureQuery();

        System.out.println("final GO Query: " + finalAOQuery);
        long end = System.currentTimeMillis();
        logger.info(" AO Query Generated: " + end);
        logger.info(" AO Query Difference: " + (start - end));
        return finalAOQuery.toString();
    }

    public void setViewByListName() {
//        if(viewIdList==null){
//            viewIdList.add("TIME");
//        }

        for (String viewIdList1 : viewIdList) {
            if (viewIdList1 != null) {
                if (timeDetails != null && timeDetails.get(1).equalsIgnoreCase("PRG_STD")) {
                    if (timeDetails != null && viewIdList1.equalsIgnoreCase("TIME") && timeDetails.get(3).equalsIgnoreCase("Month")) {
                        viewBysList.add("MONTH_YEAR");
                    } else if (timeDetails != null && viewIdList1.equalsIgnoreCase("TIME") && timeDetails.get(3).equalsIgnoreCase("Qtr")) {
                        viewBysList.add("QTR_YEAR");
                    } else if (timeDetails != null && viewIdList1.equalsIgnoreCase("TIME") && timeDetails.get(3).equalsIgnoreCase("Year")) {
                        viewBysList.add("YEAR_NAME");
                    } else {

                        viewBysList.add("A_" + viewIdList1);
                    }
                } else {
                    if (viewIdList1.equalsIgnoreCase("TIME")) {
                        viewBysList.add(viewIdList1);
                    } else {
                        viewBysList.add("A_" + viewIdList1);
                    }
                }
            }
        }
    }

    public void getCompanySecurity(String UserId) {
    }

    public void buildAOQuery() {
      //  finalAOQuery = "";
        finalAOQuery = new StringBuilder();
       // String select = " Select " + dataBaseHintInnerMost;
        StringBuilder select = new StringBuilder(" Select " + dataBaseHintInnerMost);
        for (int i = 0; i < timeDetailsArrayDistinct.size(); i++) {
            if (i == 0) {
               // select = " Select " + dataBaseHintInnerMost;
                select = new StringBuilder();
                select.append(" Select ").append(dataBaseHintInnerMost);
            } else {
                select = new StringBuilder();
                select.append(" union all Select ").append(dataBaseHintInnerMost);
            }
            //finalAOQuery += select;
            finalAOQuery.append(select);
            if (viewIdList == null || viewIdList.size() == 0) {

                //finalAOQuery += " 1 A1_KPI ";
                finalAOQuery.append(" 1 A1_KPI ");


            } else {
                if (isTypePrior(timeDetailsArrayDistinct.get(i))) {
                   // finalAOQuery += levelOneViewInnerPrior.substring(1) + " ";
                    finalAOQuery.append(levelOneViewInnerPrior.substring(1)).append(" ");

                } else {
                    //finalAOQuery += levelOneViewInner.substring(1) + " ";
                    finalAOQuery.append(levelOneViewInner.substring(1)).append(" ");
                }
            }

//            if(i==0){
//            finalAOQuery +=  levelOneQueryInner[i] + " ";
//            }else{
//                finalAOQuery +=  levelOneQueryInner[i] + " ";
//            }
//            finalAOQuery += levelOneQueryInner[i] + " ";
            finalAOQuery.append(levelOneQueryInner[i]).append(" ");
  //          finalAOQuery += " from " + getAO_name() + " ";
            finalAOQuery.append(" from ").append(getAO_name()).append(" ");
   //         finalAOQuery += " where ";
            finalAOQuery.append(" where ");
            if (avoidProgenTime.equalsIgnoreCase("Yes")) {
            } else {
                if (getIsChartTimeEnable() != null && getIsChartTimeEnable().equalsIgnoreCase("yes")) {
                   // finalAOQuery += " ddate " + getDdateClauseForChart() + " and ";
                    finalAOQuery.append(" ddate ").append(getDdateClauseForChart()).append(" and ");
                } else {

                    //finalAOQuery += " ddate " + timeDetailMap.get(timeDetailsArrayDistinct.get(i)) + " and ";
                    finalAOQuery.append(" ddate ").append(timeDetailMap.get(timeDetailsArrayDistinct.get(i))).append(" and ");
                }
            }
            //finalAOQuery += "  1 = 1  " + filterClause + dimSecurityClause;
            finalAOQuery.append("  1 = 1  ").append(filterClause).append(dimSecurityClause);

            // Added time filter
            // Add filter
        } /// End of For Loop for Inner Query
        if (viewIdList == null || viewIdList.size() == 0) {
//               finalAOQuery = " Select A1_KPI "
//                    + levelTwoQuery
//                    + "  from (" + finalAOQuery + ") Level_2 "
//                    + " group by A1_KPI "
//                    + "  ";
//            finalAOQuery = " Select A1_KPI " + levelThreeQuery + " "
//                    + " from (" + finalAOQuery + ") Level_3 ";
//            finalAOQuery = " Select " + dataBaseHintOuterMost + " A1_KPI " + levelFiveQuery + " "
//                    + " from (" + finalAOQuery + ") Level_5 ";
            StringBuilder tmpstr=new StringBuilder();
            tmpstr.append(" Select A1_KPI ").append(levelTwoQuery).append("  from (").append(finalAOQuery).append(") Level_2 ").append( " group by A1_KPI ").append( "  ");
             StringBuilder tmpstr1=new StringBuilder();
            tmpstr1.append(" Select A1_KPI ").append(levelThreeQuery).append(" " + " from (").append(tmpstr).append(") Level_3 ");
            StringBuilder tmpstr2=new StringBuilder();
            tmpstr2.append(" Select ").append(dataBaseHintOuterMost).append(" A1_KPI ").append(levelFiveQuery).append(" ").append(" from (").append(tmpstr1).append(") Level_5 ");
            finalAOQuery =tmpstr2  ;
        } else {
//            finalAOQuery = " Select " + levelTwoViewBy.substring(1) + " "
//                    + levelTwoQuery
//                    + "  from (" + finalAOQuery + ") Level_2 "
//                    + " Group by " + levelTwoGroupBy.substring(1) + " ";
//            finalAOQuery = " Select " + levelThreeViewBy.substring(1) + " " + levelThreeQuery + " "
//                    + " from (" + finalAOQuery + ") Level_3 ";
//            if (getColViewIdList() != null && !getColViewIdList().isEmpty()) {
//                finalAOQuery = " Select " + levelFiveViewBy.substring(1) + "," + levelFiveViewBy1.substring(1) + " " + levelFiveQuery + " "
//                        + " from (" + finalAOQuery + ") Level_5 order by " + levelFiveViewBy1.substring(1);
//            } else {
//                finalAOQuery = " Select " + dataBaseHintOuterMost + levelFiveViewBy.substring(1) + " " + levelFiveQuery + " "
//                        + " from (" + finalAOQuery + ") Level_5 ";
//            }
//            if (sortingType != null && chartType != null && (sortingType.equalsIgnoreCase("Value") || sortingType.equalsIgnoreCase("Alphabetic")) && chartType.equalsIgnoreCase("Table") && viewIdList != null && viewIdList.size() > 3) {
//                getUpperClauseForHigherData();
//            }
             StringBuilder tmpstr=new StringBuilder();
            tmpstr.append(" Select ").append(levelTwoViewBy.substring(1)).append(" ").append(levelTwoQuery).append("  from (").append(finalAOQuery).append(") Level_2 ").append(" Group by ").append(levelTwoGroupBy.substring(1)).append(" ");
             StringBuilder tmpstr1=new StringBuilder();
            tmpstr1.append(" Select ").append(levelThreeViewBy.substring(1)).append(" ").append(levelThreeQuery).append(" ").append(" from (").append(tmpstr).append(") Level_3 ");
            if (getColViewIdList() != null && !getColViewIdList().isEmpty()) {
                StringBuilder tmpstr2=new StringBuilder();
                tmpstr2.append(" Select ").append(levelFiveViewBy.substring(1)).append(",").append(levelFiveViewBy1.substring(1)).append(" ").append(levelFiveQuery).append(" ").append(" from (").append(tmpstr1).append(") Level_5 ").append(measureFilter).append("order by ").append(levelFiveViewBy1.substring(1));
                finalAOQuery=tmpstr2;
            } else {
                 StringBuilder tmpstr3=new StringBuilder();
                tmpstr3.append(" Select ").append(dataBaseHintOuterMost).append(levelFiveViewBy.substring(1)).append(" ").append(levelFiveQuery).append(" ").append(" from (").append(tmpstr1).append(") Level_5 ").append(measureFilter);
                finalAOQuery=tmpstr3;
            }
            if (sortingType != null && chartType != null && (sortingType.equalsIgnoreCase("Value") || sortingType.equalsIgnoreCase("Alphabetic")) && chartType.equalsIgnoreCase("Table") && viewIdList != null && viewIdList.size() > 3) {
                getUpperClauseForHigherData();
            }
        }
    }

    public void createViewByQuery() {
     //   levelOneViewInner = "";
        levelOneViewInner = new StringBuilder();
       // levelOneViewInnerPrior = "";
        levelOneViewInnerPrior =new StringBuilder();
     //   levelTwoViewBy = "";
        levelTwoViewBy =new StringBuilder();
    //    levelTwoGroupBy = "";;
        levelTwoGroupBy =new StringBuilder();
   //     levelThreeViewBy = "";
        levelThreeViewBy =new StringBuilder();
        levelFourViewBy =new StringBuilder();
   //     levelFiveViewBy = "";
        levelFiveViewBy =new StringBuilder();
        if (viewIdList != null && viewIdList.size() > 0) {
            for (String viewIdListStr : viewIdList) {
                if (viewIdListStr.equalsIgnoreCase("TIME")) {
                    if (timeDetails != null && timeDetails.get(1).equalsIgnoreCase("PRG_STD")) {
                        if (timeDetails.get(3).equalsIgnoreCase("Month")) {
                          //    levelOneViewInner += ", MONTH_YEAR as MONTH_YEAR ";
                            levelOneViewInner.append(", MONTH_YEAR as MONTH_YEAR ");
                       //     levelOneViewInner += ", O_MONTH_YEAR as O_MONTH_YEAR ";
                            levelOneViewInner.append(", O_MONTH_YEAR as O_MONTH_YEAR ");
                      //      levelOneViewInnerPrior += ", MONTH_YEAR as MONTH_YEAR ";
                            levelOneViewInnerPrior.append(", MONTH_YEAR as MONTH_YEAR ");
                      //      levelOneViewInnerPrior += ", O_MONTH_YEAR as O_MONTH_YEAR ";
                            levelOneViewInnerPrior.append(", O_MONTH_YEAR as O_MONTH_YEAR ");
                       //     levelTwoViewBy += ", MONTH_YEAR as MONTH_YEAR ";
                            levelTwoViewBy.append(", MONTH_YEAR as MONTH_YEAR ");
                      //      levelTwoViewBy += ", O_MONTH_YEAR as O_MONTH_YEAR ";
                            levelTwoViewBy.append(", O_MONTH_YEAR as O_MONTH_YEAR ");
                      //      levelTwoGroupBy += ", MONTH_YEAR " + " , O_MONTH_YEAR ";
                            levelTwoGroupBy.append(", MONTH_YEAR ").append(" , O_MONTH_YEAR ");
                      //      levelThreeViewBy += ", MONTH_YEAR as MONTH_YEAR ";
                            levelThreeViewBy.append(", MONTH_YEAR as MONTH_YEAR ");
                      //      levelFourViewBy += ", MONTH_YEAR as MONTH_YEAR ";
                            levelFourViewBy.append(", MONTH_YEAR as MONTH_YEAR ");
                            levelFiveViewBy.append(", MONTH_YEAR ");
                            
                            if (getColViewIdList() != null && !getColViewIdList().isEmpty()) {
                        levelThreeViewBy.append(" , O_MONTH_YEAR  as  O_MONTH_YEAR");
                        levelFourViewBy.append(" , O_MONTH_YEAR as O_MONTH_YEAR ");
                        levelFiveViewBy1.append(", O_MONTH_YEAR");
                    }
                            
                        } else if (timeDetails.get(3).equalsIgnoreCase("Qtr")) {
                             levelOneViewInner.append(", QTR_YEAR as QTR_YEAR ");
                            levelOneViewInner.append(", O_QTR_YEAR as O_QTR_YEAR ");
                            levelOneViewInnerPrior.append(", QTR_YEAR as QTR_YEAR ");
                            levelOneViewInnerPrior.append(", O_QTR_YEAR as O_QTR_YEAR ");
                       //   levelTwoViewBy += ", QTR_YEAR as QTR_YEAR ";
                            levelTwoViewBy.append(", QTR_YEAR as QTR_YEAR ");
                       //     levelTwoViewBy += ", O_QTR_YEAR as O_QTR_YEAR ";
                            levelTwoViewBy.append(", O_QTR_YEAR as O_QTR_YEAR ");
                            levelTwoGroupBy.append(", QTR_YEAR ").append(" , O_QTR_YEAR ");
                            levelThreeViewBy.append(", QTR_YEAR as QTR_YEAR ");
                            levelFourViewBy.append(", QTR_YEAR as QTR_YEAR ");
                            levelFiveViewBy.append(", QTR_YEAR ");
                            if (getColViewIdList() != null && !getColViewIdList().isEmpty()) {
                   //     levelThreeViewBy += " , O_QTR_YEAR  as  O_QTR_YEAR";
                        levelThreeViewBy.append(" , O_QTR_YEAR  as  O_QTR_YEAR");
                   //     levelFourViewBy += " , O_QTR_YEAR as O_QTR_YEAR ";
                        levelFourViewBy.append(" , O_QTR_YEAR as O_QTR_YEAR ");
                  //      levelFiveViewBy1 += ", O_QTR_YEAR";
                        levelFiveViewBy1.append(", O_QTR_YEAR");
                    }
                        } else if (timeDetails.get(3).equalsIgnoreCase("Year")) {
                            levelOneViewInner.append(", YEAR_NAME as YEAR_NAME ");
                            levelOneViewInner.append(", O_YEAR_NAME as O_YEAR_NAME ");
                            levelOneViewInnerPrior.append(", YEAR_NAME as YEAR_NAME ");
                            levelOneViewInnerPrior.append(", O_YEAR_NAME as O_YEAR_NAME ");
                            //levelTwoViewBy += ", YEAR_NAME as YEAR_NAME ";
                            levelTwoViewBy.append(", YEAR_NAME as YEAR_NAME ");
                           // levelTwoViewBy += ", O_YEAR_NAME as O_YEAR_NAME ";
                            levelTwoViewBy.append(", O_YEAR_NAME as O_YEAR_NAME ");
                      //      levelTwoGroupBy += ", YEAR_NAME " + " , O_YEAR_NAME ";
                            levelTwoGroupBy.append(", YEAR_NAME ").append(" , O_YEAR_NAME ");
                            levelThreeViewBy.append(", YEAR_NAME as YEAR_NAME ");
                            levelFourViewBy.append(", YEAR_NAME as YEAR_NAME ");
                            levelFiveViewBy.append(", YEAR_NAME ");
                              if (getColViewIdList() != null && !getColViewIdList().isEmpty()) {
                        levelThreeViewBy.append(" , O_YEAR_NAME  as  O_YEAR_NAME");
                        levelFourViewBy.append(" , O_YEAR_NAME as O_YEAR_NAME ");
                        levelFiveViewBy1.append(", O_YEAR_NAME");
                        }
                        }

                    } else {
                        levelOneViewInner.append(", TIME as TIME ");
                        levelOneViewInner.append( ", O_TIME as O_TIME ");
                        levelOneViewInnerPrior.append(", TIME as TIME ");
                        levelOneViewInnerPrior.append(", O_TIME as O_TIME ");
                    //    levelTwoViewBy += ", TIME as TIME ";
                        levelTwoViewBy.append(", TIME as TIME ");
                   //     levelTwoViewBy += ", O_TIME as O_TIME ";
                        levelTwoViewBy.append(", O_TIME as O_TIME ");
                  //      levelTwoGroupBy += ", TIME " + " , O_TIME ";
                        levelTwoGroupBy.append(", TIME ").append(" , O_TIME ");
                        levelThreeViewBy.append(", TIME as TIME ");
                        levelFourViewBy.append(", TIME as TIME ");
                        levelFiveViewBy.append(", TIME ");
                        if (getColViewIdList() != null && !getColViewIdList().isEmpty()) {
                            levelThreeViewBy.append(", O_TIME as O_TIME ");
                            levelFourViewBy.append(", O_TIME as O_TIME ");
                            levelFiveViewBy.append(", O_TIME ");
                        }
                    }

                } else {
                    String MeasureName = "A_" + viewIdListStr;
                    String MeasureNameOrder = "O_" + viewIdListStr;
                    levelOneViewInner.append(", ").append(MeasureName).append(" as ").append(MeasureName).append(" ");
                    levelOneViewInner.append(", ").append(MeasureNameOrder).append(" as ").append(MeasureNameOrder).append(" ");
                    levelOneViewInnerPrior.append(", ").append(MeasureName).append(" as ").append(MeasureName).append(" ");
                    levelOneViewInnerPrior.append(", ").append(MeasureNameOrder).append(" as ").append(MeasureNameOrder).append(" ");
                   // levelTwoViewBy += ", " + MeasureName + " as " + MeasureName + " ";
                    levelTwoViewBy.append(", ").append(MeasureName).append(" as ").append(MeasureName).append(" ");
                  //  levelTwoViewBy += ", " + MeasureNameOrder + " as " + MeasureNameOrder + " ";
                    levelTwoViewBy.append(", ").append(MeasureNameOrder).append(" as ").append(MeasureNameOrder).append(" ");
                    levelTwoGroupBy.append(", ").append(MeasureName).append(" , ").append(MeasureNameOrder).append(" ");
                    levelThreeViewBy.append(", ").append(MeasureName).append(" as ").append(MeasureName).append(" ");
                  //  levelFourViewBy += ", " + MeasureName + " as " + MeasureName + " ";
                    levelFourViewBy.append(", ").append(MeasureName).append(" as ").append(MeasureName).append(" ");
                   // levelFiveViewBy += ", " + MeasureName + " ";
                    levelFiveViewBy.append(", ").append(MeasureName).append(" ");
                    if (getColViewIdList() != null && !getColViewIdList().isEmpty()) {
                        levelThreeViewBy.append(", ").append(MeasureNameOrder).append(" as ").append(MeasureNameOrder).append(" ");
                    //    levelFourViewBy += ", " + MeasureNameOrder + " as " + MeasureNameOrder + " ";
                        levelFourViewBy.append(", ").append(MeasureNameOrder).append(" as ").append(MeasureNameOrder).append(" ");
//               levelFiveViewBy += ", " + MeasureNameOrder + " ";
                       // levelFiveViewBy1 += "," + MeasureNameOrder + "";
                        levelFiveViewBy1.append(",").append(MeasureNameOrder).append("");
                    }
                }
            }
        }
    }

    public void createMeasuresQuery() throws SQLException {
        levelOneQueryInner = new String[timeDetailsArrayDistinct.size()];
        for (int i = 0; i < timeDetailsArrayDistinct.size(); i++) {
            levelOneQueryInner[i] = "";
            getLevelQuery(i, timeDetailsArrayDistinct.get(i));
            pbTime.elementID = removeComparisonType(measureIdsListGO.get(0));
            setTimeType(timeDetailsArrayDistinct.get(i));

        }
        addChangetoQuery();

    }

    public void getLevelQuery(int level, String periodType) {
        List<String> keys = new ArrayList(periodWiseMap.keySet());
        boolean isPrior = false;
//        System.out.println("level" + level) ;
//        System.out.println("building Query periodType ");
//        System.out.println(" keys "+ keys);
//        System.out.println(" periodWiseMap "+ periodWiseMap);
        ArrayList<String> RepeatName = new ArrayList<String>();
        for (String currPeriodType : keys) {
            isPrior = isOnlyTypePrior(currPeriodType);
            List<String> measureList = periodWiseMap.get(currPeriodType);
            List<String> measureListGT = periodWiseMapGT.get(currPeriodType);
            int looperGt=0;
            String CurrentMeasureGT ="N";
            for (String CurrentMeasure : measureList) {
                CurrentMeasure = removeComparisonType(CurrentMeasure);
                CurrentMeasureGT = measureListGT.get(looperGt);
                looperGt++;
                String alias = "";
                String elementName = "A_" + CurrentMeasure + getAdditionalPeriodSuffix(currPeriodType);
                if (!getUserColumnType(CurrentMeasure, currPeriodType).equalsIgnoreCase("Summarized")) {
                    //Handle comma for first
//                    System.out.println("currPeriodType " +currPeriodType);
//                    System.out.println("periodType " +periodType);
//                    System.out.println(" CurrentMeasure " + CurrentMeasure);

//                    System.out.println(" levelOneQueryInner[level]  " +levelOneQueryInner[level] );
                    alias = getUserColumnNameForCurrentAlias(CurrentMeasure, periodType);
                    alias = alias.replace("Prior_", "");
                    alias += getAdditionalPeriodSuffix(currPeriodType);
//                    System.out.println("alias "+ alias);
                    if (RepeatName.isEmpty() || RepeatName == null || RepeatName.contains(alias) == false) {

                        RepeatName.add(alias);

                        if (currPeriodType.equals(periodType)) {
                            levelOneQueryInner[level] += ", " + getUserColumnNameForCurrent(CurrentMeasure, periodType);

                        } else {
                            levelOneQueryInner[level] += ",  null ";
                        }
                        if (isPrior) {
                            //alias =getUserColumnNameForPrior(CurrentMeasure, periodType);
//                        alias =getUserColumnNameForCurrentAlias(CurrentMeasure, periodType);
//                        if(alias==null || alias.equals("")){
//                           alias =getUserColumnNameForCurrentAlias(CurrentMeasure, periodType);
//                        }
                            levelOneQueryInner[level] += " as  " + alias + " ";
                        } else {
                            //alias =getUserColumnNameForCurrentAlias(CurrentMeasure, periodType);
                            levelOneQueryInner[level] += " as  " + alias + " ";
                        }
//                    System.out.println(" levelOneQueryInner[level]  " +levelOneQueryInner[level] );
                        if (level == 0) {
                         //   levelTwoQuery += ", " + getUserColumnAgg(CurrentMeasure, periodType) + "(" + alias + ") " + elementName;
                            levelTwoQuery.append(", ").append(getUserColumnAgg(CurrentMeasure, periodType)).append("(").append(alias).append(") ").append(elementName);
                            //levelThreeQuery += ", " + elementName + " " + elementName;
                            levelThreeQuery.append(", ").append(elementName).append(" ").append(elementName);
                       //     levelFiveQuery += ", case when " + elementName + " is NULL then 0 else " + elementName + " end " + elementName;
                            levelFiveQuery.append(", case when ").append(elementName).append(" is NULL then 0 else ").append(elementName).append(" end ").append(elementName);
                        }
                    }
                } else {
                    if (level == 0) {
                       // levelTwoQuery += ", " + getUserColumnFormula(elementName, periodType, currPeriodType) + " " + elementName;
                        levelTwoQuery.append(", ").append(getUserColumnFormula(elementName, periodType, currPeriodType)).append(" ").append(elementName);
                        //levelThreeQuery += ", " + elementName + " " + elementName;
                        levelThreeQuery.append(", ").append(elementName).append(" ").append(elementName);
                     //   levelFiveQuery += ", case when " + elementName + " is NULL then 0 else " + elementName + " end " + elementName;
                        levelFiveQuery.append(", case when ").append(elementName).append(" is NULL then 0 else ").append(elementName).append(" end ").append(elementName);
                        if(CurrentMeasureGT.equalsIgnoreCase("PT")){
                            levelThreeQuery.append(", ( ").append(elementName).append(" *100.0) /case when sum(").append(elementName).append(") over() =0 then null else sum(").append(elementName).append(") over() end ").append(elementName).append("_PT ");
                            levelFiveQuery.append(", case when ").append(elementName).append("_PT is NULL then 0 else ").append(elementName).append("_PT end ").append(elementName).append("_PT ");
                        }else if(CurrentMeasureGT.equalsIgnoreCase("GT")){
                            levelThreeQuery.append(", ").append(" sum(").append(elementName).append(") over() ").append(elementName).append("_GT ");
                            levelFiveQuery.append(", case when ").append(elementName).append("_GT is NULL then 0 else ").append(elementName).append("_GT end ").append(elementName).append("_GT ");
                        
                    }
                }
                }

            }


        }

    }

    public String getAdditionalPeriodSuffix(String currPeriodType) {
        if (currPeriodType.equalsIgnoreCase("Period")) {
            return "";
        }
        if (currPeriodType.equalsIgnoreCase("PriorPeriod")) {
            return "_PRIOR";
        }
//        return "";
        return "_" + currPeriodType;
    }

    public boolean isTypePrior(String Type) {
        if (Type.equalsIgnoreCase("PRIORPERIOD")
                || Type.equalsIgnoreCase("PMTD")
                || Type.equalsIgnoreCase("PYMTD")
                || Type.equalsIgnoreCase("PWTD")
                || Type.equalsIgnoreCase("PYWTD")
                || Type.equalsIgnoreCase("PQTD")
                || Type.equalsIgnoreCase("PYQTD")
                || Type.equalsIgnoreCase("PYTD")) {
            return true;
        }
        return false;
    }

    public boolean isOnlyTypePrior(String Type) {
        if (Type.equalsIgnoreCase("PriorPeriod") //                || Type.equalsIgnoreCase("PMTD")
                //                    || Type.equalsIgnoreCase("PYMTD")
                //                    || Type.equalsIgnoreCase("PQTD")
                //                    || Type.equalsIgnoreCase("PYQTD")
                //                    || Type.equalsIgnoreCase("PYTD")
                ) {
            return true;
        }
        return false;
    }

    public String getMeasureQuery() {
        for (int i = 0; i < timeDetailsArrayDistinct.size(); i++) {
            getZeroLevelQuery(timeDetailsArrayDistinct.get(i));
        }
        return "select " + levelOneQuery.toString().replace("[", "").replace("]", "");
    }

    public void getCurrentPriorList() {
        for (int loop = 0; loop < measureIdsList.size(); loop++) {
            if (timeDetailsArray.get(loop).equalsIgnoreCase("PriorPeriod")) {
                colViewIdPriorList.add(removeComparisonType(measureIdsList.get(loop)));
                addtoPeriodMap(timeDetailsArray.get(loop), measureIdsList.get(loop),getGrandTotalAggType().get(loop));
                if (timeDetailsArrayDistinct.isEmpty() || !timeDetailsArrayDistinct.contains(timeDetailsArray.get(loop))) {
                    timeDetailsArrayDistinct.add(timeDetailsArray.get(loop));
                }
            } else if (timeDetailsArray.get(loop).equalsIgnoreCase("Period")
                    || timeDetailsArray.get(loop).equalsIgnoreCase("MTD")
                    || timeDetailsArray.get(loop).equalsIgnoreCase("YMTD")
                    || timeDetailsArray.get(loop).equalsIgnoreCase("QTD")
                    || timeDetailsArray.get(loop).equalsIgnoreCase("WTD")
                    || timeDetailsArray.get(loop).equalsIgnoreCase("PWTD")
                    || timeDetailsArray.get(loop).equalsIgnoreCase("PYWTD")
                    || timeDetailsArray.get(loop).equalsIgnoreCase("YQTD")
                    || timeDetailsArray.get(loop).equalsIgnoreCase("YTD")
                    || timeDetailsArray.get(loop).equalsIgnoreCase("PMTD")
                    || timeDetailsArray.get(loop).equalsIgnoreCase("PYMTD")
                    || timeDetailsArray.get(loop).equalsIgnoreCase("PQTD")
                    || timeDetailsArray.get(loop).equalsIgnoreCase("PYQTD")
                    || timeDetailsArray.get(loop).equalsIgnoreCase("PYTD")) {
                colViewIdCurrentList.add(removeComparisonType(measureIdsList.get(loop)));
                addtoPeriodMap(timeDetailsArray.get(loop), measureIdsList.get(loop),getGrandTotalAggType().get(loop));
                if (timeDetailsArrayDistinct.isEmpty() || !timeDetailsArrayDistinct.contains(timeDetailsArray.get(loop))) {
                    timeDetailsArrayDistinct.add(timeDetailsArray.get(loop));
                }
            } else {
                colViewIdChangeList.add(removeComparisonType(measureIdsList.get(loop)));
                colViewIdChangeListGT.add(getGrandTotalAggType().get(loop));
                colViewIdChangeListAgg.add(timeDetailsArray.get(loop));
            }

        }
        updatePeriodMap();

    }

    public void updatePeriodMap() {
        for (int loop = 0; loop < colViewIdChangeList.size(); loop++) {
            String CurrPeriod = getCurrentPeriodForChg(colViewIdChangeListAgg.get(loop));
            String PriorPeriod = getPriorPeriodForChg(colViewIdChangeListAgg.get(loop));
            String measureName = removeComparisonType(colViewIdChangeList.get(loop));
            addtoPeriodMap(CurrPeriod, measureName,colViewIdChangeListGT.get(loop));
            addtoPeriodMap(PriorPeriod, measureName,colViewIdChangeListGT.get(loop));

           // String elementName = "A_" + measureName + getAdditionalPeriodSuffix(CurrPeriod);
            StringBuilder elementName=new StringBuilder();
            elementName.append("A_").append(measureName).append(getAdditionalPeriodSuffix(CurrPeriod));

           // String PriorElementName = "A_" + measureName + getAdditionalPeriodSuffix(PriorPeriod);
            StringBuilder PriorElementName=new StringBuilder();
            PriorElementName.append("A_").append(measureName).append(getAdditionalPeriodSuffix(PriorPeriod));
            StringBuilder a=new StringBuilder();
//            String a = " ((case when " + elementName + " is null then 0 else " + elementName + " end )";
//            a += " - (case when " + PriorElementName + " is null then 0 else " + PriorElementName + " end )) ";
//            if (!colViewIdChangeListAgg.get(loop).toLowerCase().contains("per")) {
//
//                colViewIdChangeFormula.add(a);
//            } else {
//                a = " (" + a + " *100.0) / (case when " + PriorElementName + " =0  then null else " + PriorElementName + " end ) ";
//                colViewIdChangeFormula.add(a);
//            }
            a.append(" ((case when ").append(elementName).append(" is null then 0 else ").append(elementName).append(" end )");
            a.append(" - (case when ").append(PriorElementName).append(" is null then 0 else ").append(PriorElementName).append(" end )) ");
            if (!colViewIdChangeListAgg.get(loop).toLowerCase().contains("per")) {

                colViewIdChangeFormula.add(a.toString());
            } else {
                StringBuilder str=new StringBuilder();
                 str.append(" (").append(a).append(" *100.0) / (case when ").append(PriorElementName).append(" =0  then null else ").append(PriorElementName).append(" end ) ");
                 colViewIdChangeFormula.add(str.toString());
            }

        }

    }

    public void addtoPeriodMap(String timePeriod, String MeasureId,String grandTotalType) {
        MeasureId = removeComparisonType(MeasureId);
//        System.out.println(" timePeriod --" + timePeriod);
//        System.out.println("MeasureId " + MeasureId);
//        System.out.println("periodWiseMap"+ periodWiseMap);
        if (periodWiseMap == null || periodWiseMap.get(timePeriod) == null) {
            ArrayList addMeasure = new ArrayList();
            addMeasure.add(MeasureId);
            periodWiseMap.put(timePeriod, addMeasure);
            ArrayList addMeasureGT = new ArrayList();
            addMeasureGT.add(grandTotalType);
            periodWiseMapGT.put(timePeriod, addMeasureGT);
        } else {
            List addMeasure = periodWiseMap.get(timePeriod);
            List addMeasureGT = periodWiseMapGT.get(timePeriod);
//                System.out.println(" addMeasure " + addMeasure);
            if (!addMeasure.contains(MeasureId)) {
                addMeasure.add(MeasureId);
                addMeasureGT.add(grandTotalType);
            }else{
                String GT = addMeasureGT.get(addMeasure.indexOf(MeasureId)).toString();
                if(GT !=grandTotalType){
                    if(GT.equalsIgnoreCase("PT")|| grandTotalType.equalsIgnoreCase("PT") ){
                        addMeasureGT.add(addMeasure.indexOf(MeasureId), "PT");
                    }else if(GT.equalsIgnoreCase("GT")|| grandTotalType.equalsIgnoreCase("GT") ){
                        addMeasureGT.add(addMeasure.indexOf(MeasureId), "GT");
            }
                }
            }
            
            periodWiseMap.put(timePeriod, addMeasure);
            periodWiseMapGT.put(timePeriod, addMeasureGT);

        }
        if (timeDetailsArrayDistinct.isEmpty() || !timeDetailsArrayDistinct.contains(timePeriod)) {
            timeDetailsArrayDistinct.add(timePeriod);
        }
//        System.out.println("periodWiseMap"+ periodWiseMap);

    }

    public void addAdditionMemberstoPeriodMap(String eleId, String eleType, String depList) {
        if (depList.equalsIgnoreCase("")) {
            return;
        }
        for (int loop = 0; loop < measureIdsList.size(); loop++) {

            if (eleId.equalsIgnoreCase(removeComparisonType(measureIdsList.get(loop)))) {
                if ((timeDetailsArray.get(loop).equalsIgnoreCase("PriorPeriod"))) {
//                    System.out.println("eleId "+eleId);
//                    System.out.println("eleType "+eleType);
//                    System.out.println("depList "+depList);
//                    System.out.println("timeDetailsArray.get(loop) in if "+timeDetailsArray.get(loop));
                    //Will add only ref type 2
                    if (eleType.equals("2")) {
                        String[] a = depList.split(",");
                        for (String a1 : a) {
                            addtoPeriodMap(timeDetailsArray.get(loop), a1,"N"); //to chnage for formula 
                        }
                    }

                } else if ((timeDetailsArray.get(loop).equalsIgnoreCase("PMTD")
                        || timeDetailsArray.get(loop).equalsIgnoreCase("PYMTD")
                        || timeDetailsArray.get(loop).equalsIgnoreCase("PWTD")
                        || timeDetailsArray.get(loop).equalsIgnoreCase("PYWTD")
                        || timeDetailsArray.get(loop).equalsIgnoreCase("PQTD")
                        || timeDetailsArray.get(loop).equalsIgnoreCase("PYQTD")
                        || timeDetailsArray.get(loop).equalsIgnoreCase("PYTD"))) {
//                    System.out.println("eleId "+eleId);
//                    System.out.println("eleType "+eleType);
//                    System.out.println("depList "+depList);
//                    System.out.println("timeDetailsArray.get(loop) in if else "+timeDetailsArray.get(loop));
                    //Will add only ref type 2
                    //if(eleType.equals("2"))
                    {
                        String[] a = depList.split(",");
                        for (String a1 : a) {
                            addtoPeriodMap(timeDetailsArray.get(loop), a1,"N");//to chnage for formula 
                        }
                    }

                } else if ((timeDetailsArray.get(loop).equalsIgnoreCase("MOM")
                        || timeDetailsArray.get(loop).equalsIgnoreCase("MOY")
                        || timeDetailsArray.get(loop).equalsIgnoreCase("MOYM")
                        || timeDetailsArray.get(loop).equalsIgnoreCase("QOQ")
                        || timeDetailsArray.get(loop).equalsIgnoreCase("QOY")
                        || timeDetailsArray.get(loop).equalsIgnoreCase("QOYM")
                        || timeDetailsArray.get(loop).equalsIgnoreCase("YOY")
                        || timeDetailsArray.get(loop).equalsIgnoreCase("MOMPer")
                        || timeDetailsArray.get(loop).equalsIgnoreCase("MOYPer")
                        || timeDetailsArray.get(loop).equalsIgnoreCase("MOYMPer")
                        || timeDetailsArray.get(loop).equalsIgnoreCase("QOQPer")
                        || timeDetailsArray.get(loop).equalsIgnoreCase("QOYPer")
                        || timeDetailsArray.get(loop).equalsIgnoreCase("QOYMPer")
                        || timeDetailsArray.get(loop).equalsIgnoreCase("YOYPer"))) {
                    {
                        String[] a = depList.split(",");
                        for (String a1 : a) {
                            addtoPeriodMap(getCurrentPeriodForChg(timeDetailsArray.get(loop)), a1,"N");//to chnage for formula 
                            addtoPeriodMap(getPriorPeriodForChg(timeDetailsArray.get(loop)), a1,"N");//to chnage for formula 
                        }
                    }
                } else {
//                    System.out.println("eleId "+eleId);
//                    System.out.println("eleType "+eleType);
//                    System.out.println("depList "+depList);
//                    System.out.println("timeDetailsArray.get(loop) in else "+timeDetailsArray.get(loop));

                    String[] a = depList.split(",");
                    for (String a1 : a) {
//                        System.out.println(" for "+timeDetailsArray.get(loop) + " ele " + a1);
                        if (!eleType.equals("2")) {
                            addtoPeriodMap(timeDetailsArray.get(loop), a1,"N");//to chnage for formula 
                        } else {
                            if (timeDetailsArray.get(loop).equalsIgnoreCase("PriorPeriod")
                                    || timeDetailsArray.get(loop).equalsIgnoreCase("Period")) {
                                addtoPeriodMap(getPrior(timeDetailsArray.get(loop)), a1,"N");//to chnage for formula 
//                                System.out.println(" Allinfo" + allInfoHaspMap);
                            } else {
                                addtoPeriodMap(timeDetailsArray.get(loop), a1,"N"); //to chnage for formula 
                            }
                        }
                    }
                }

            }
        }

    }

    private String getMetaConnectionType() {
        if (metaConnectionType == null || metaConnectionType.equals("")) {
            metaConnectionType = ProgenConnection.getInstance().getDatabaseType();
        }
        return metaConnectionType;
    }

    public String getDependentElements(String qryWhereId) throws SQLException {
        //String currList = ArrayToCommaString(colViewIdCurrentList);
        String priorList = ArrayToCommaString(colViewIdPriorList);
        priorList = removeComparisonType(priorList);
        if (priorList.equalsIgnoreCase("")) {
            priorList = "-1";
        }
       // String getreferredelementIds = "";
        StringBuilder getreferredelementIds=new StringBuilder();
        String referredelementIds = "";
        String connType = getMetaConnectionType();
        if (connType != null && !connType.equalsIgnoreCase("")) {
            if (connType.equalsIgnoreCase("oracle")) {
//                getreferredelementIds = "select REFFERED_ELEMENTS,REF_ELEMENT_ID,REF_ELEMENT_TYPE,ELEMENT_ID,1 FLAG  from prg_user_all_info_details  "
//                        + "where ( ELEMENT_ID in(" + qryWhereId + ") "
//                        + " and REFFERED_ELEMENTS is not null)"
//                        + " union select REFFERED_ELEMENTS,REF_ELEMENT_ID,REF_ELEMENT_TYPE,ELEMENT_ID,1 FLAG from prg_user_all_info_details  "
//                        + "where ( REF_ELEMENT_TYPE= 2  "
//                        + " and REF_ELEMENT_ID in(" + priorList + ") )"
//                        + " and REFFERED_ELEMENTS is not null "
//                        + " union select ELEMENT_ID || '',REF_ELEMENT_ID,REF_ELEMENT_TYPE,ELEMENT_ID,0 FLAG from prg_user_all_info_details  "
//                        + "where ( REF_ELEMENT_TYPE= 2  "
//                        + " and REF_ELEMENT_ID in(" + priorList + ") )"
//                        + " and REFFERED_ELEMENTS is null";
              getreferredelementIds.append("select REFFERED_ELEMENTS,REF_ELEMENT_ID,REF_ELEMENT_TYPE,ELEMENT_ID,1 FLAG  from prg_user_all_info_details  ");
              getreferredelementIds.append("where ( ELEMENT_ID in(").append(qryWhereId).append(") ").append(" and REFFERED_ELEMENTS is not null)");
              getreferredelementIds.append(" union select REFFERED_ELEMENTS,REF_ELEMENT_ID,REF_ELEMENT_TYPE,ELEMENT_ID,1 FLAG from prg_user_all_info_details  ");
              getreferredelementIds.append("where ( REF_ELEMENT_TYPE= 2  ").append(" and REF_ELEMENT_ID in(").append(priorList).append(") )").append(" and REFFERED_ELEMENTS is not null ");
              getreferredelementIds.append(" union select ELEMENT_ID || '',REF_ELEMENT_ID,REF_ELEMENT_TYPE,ELEMENT_ID,0 FLAG from prg_user_all_info_details  ").append("where ( REF_ELEMENT_TYPE= 2  ");
              getreferredelementIds.append(" and REF_ELEMENT_ID in(").append(priorList).append(") )").append(" and REFFERED_ELEMENTS is null");

            } else if (connType.equalsIgnoreCase("MySql")) {
//                getreferredelementIds = "select REFFERED_ELEMENTS,REF_ELEMENT_ID,REF_ELEMENT_TYPE,ELEMENT_ID,1 FLAG  from prg_user_all_info_details  "
//                        + "where ( ELEMENT_ID in(" + qryWhereId + ") "
//                        + " and REFFERED_ELEMENTS is not null)"
//                        + " union select REFFERED_ELEMENTS,REF_ELEMENT_ID,REF_ELEMENT_TYPE,ELEMENT_ID,1 FLAG from prg_user_all_info_details  "
//                        + "where ( REF_ELEMENT_TYPE= 2  "
//                        + " and REF_ELEMENT_ID in(" + priorList + ") )"
//                        + " and REFFERED_ELEMENTS is not null "
//                        + " union select ELEMENT_ID,REF_ELEMENT_ID,REF_ELEMENT_TYPE,ELEMENT_ID,0 FLAG from prg_user_all_info_details  "
//                        + "where ( REF_ELEMENT_TYPE= 2  "
//                        + " and REF_ELEMENT_ID in(" + priorList + ") )"
//                        + " and REFFERED_ELEMENTS is null";
                getreferredelementIds.append("select REFFERED_ELEMENTS,REF_ELEMENT_ID,REF_ELEMENT_TYPE,ELEMENT_ID,1 FLAG  from prg_user_all_info_details  ");
                getreferredelementIds.append("where ( ELEMENT_ID in(").append(qryWhereId).append(") ").append(" and REFFERED_ELEMENTS is not null)");
                getreferredelementIds.append(" union select REFFERED_ELEMENTS,REF_ELEMENT_ID,REF_ELEMENT_TYPE,ELEMENT_ID,1 FLAG from prg_user_all_info_details  ");
                getreferredelementIds.append("where ( REF_ELEMENT_TYPE= 2  ").append(" and REF_ELEMENT_ID in(").append(priorList).append(") )").append(" and REFFERED_ELEMENTS is not null ");
                getreferredelementIds.append(" union select cast(ELEMENT_ID as char) ELEMENT_ID,REF_ELEMENT_ID,REF_ELEMENT_TYPE,ELEMENT_ID,0 FLAG from prg_user_all_info_details  ");
                getreferredelementIds.append("where ( REF_ELEMENT_TYPE= 2  ").append(" and REF_ELEMENT_ID in(").append(priorList).append(") )").append(" and REFFERED_ELEMENTS is null");

            } else if (connType.trim().equalsIgnoreCase("SqlServer")) {
//                getreferredelementIds = "select REFFERED_ELEMENTS,REF_ELEMENT_ID,REF_ELEMENT_TYPE,ELEMENT_ID,1 FLAG  from prg_user_all_info_details  "
//                        + "where ( ELEMENT_ID in(" + qryWhereId + ") "
//                        + " and REFFERED_ELEMENTS is not null)"
//                        + " union select REFFERED_ELEMENTS,REF_ELEMENT_ID,REF_ELEMENT_TYPE,ELEMENT_ID,1 FLAG from prg_user_all_info_details  "
//                        + "where ( REF_ELEMENT_TYPE= 2  "
//                        + " and REF_ELEMENT_ID in(" + priorList + ") )"
//                        + " and REFFERED_ELEMENTS is not null "
//                        + " union select cast(ELEMENT_ID as varchar),REF_ELEMENT_ID,REF_ELEMENT_TYPE,ELEMENT_ID,0 FLAG from prg_user_all_info_details  "
//                        + "where ( REF_ELEMENT_TYPE= 2  "
//                        + " and REF_ELEMENT_ID in(" + priorList + ") )"
//                        + " and REFFERED_ELEMENTS is null";
                getreferredelementIds.append("select REFFERED_ELEMENTS,REF_ELEMENT_ID,REF_ELEMENT_TYPE,ELEMENT_ID,1 FLAG  from prg_user_all_info_details  ");
                getreferredelementIds.append("where ( ELEMENT_ID in(").append(qryWhereId).append(") ").append(" and REFFERED_ELEMENTS is not null)");
                getreferredelementIds.append(" union select REFFERED_ELEMENTS,REF_ELEMENT_ID,REF_ELEMENT_TYPE,ELEMENT_ID,1 FLAG from prg_user_all_info_details  ");
                getreferredelementIds.append("where ( REF_ELEMENT_TYPE= 2  ").append(" and REF_ELEMENT_ID in(").append(priorList).append(") )").append(" and REFFERED_ELEMENTS is not null ");
                getreferredelementIds.append(" union select cast(ELEMENT_ID as varchar),REF_ELEMENT_ID,REF_ELEMENT_TYPE,ELEMENT_ID,0 FLAG from prg_user_all_info_details  ");
                getreferredelementIds.append("where ( REF_ELEMENT_TYPE= 2  ").append(" and REF_ELEMENT_ID in(").append(priorList).append(") )").append(" and REFFERED_ELEMENTS is null");
            }
        }

        PbReturnObject retObj;
        String colNames[] = null;
        int psize = 0;
//            
        retObj = pbDb.execSelectSQL(getreferredelementIds.toString());
        if (retObj != null && retObj.getRowCount() > 0) {
            colNames = retObj.getColumnNames();
            psize = retObj.getRowCount();

        }
        for (int looper = 0; looper < psize; looper++) {


            referredelementIds = retObj.getFieldValueString(looper, colNames[0]);

            if (retObj.getFieldValueString(looper, colNames[0]).equalsIgnoreCase("")) {
                qryWhereId += referredelementIds;
            } else {
                qryWhereId += ", " + referredelementIds;

            }
            if (referredelementIds != null && !referredelementIds.equalsIgnoreCase("")) {
                List<String> alldplist = new ArrayList<String>(Arrays.asList(referredelementIds.split(",")));
                allDependentsMap.put(retObj.getFieldValueString(looper, colNames[3]), alldplist);
            }
            if (!retObj.getFieldValueString(looper, colNames[4]).equalsIgnoreCase("0")) {
                addAdditionMemberstoPeriodMap(retObj.getFieldValueString(looper, colNames[1]),
                        retObj.getFieldValueString(looper, colNames[2]),
                        retObj.getFieldValueString(looper, colNames[0]));
            }
            if (!retObj.getFieldValueString(looper, colNames[1]).equals(retObj.getFieldValueString(looper, colNames[3]))) {
                PriorElementIds.put(retObj.getFieldValueString(looper, colNames[1]),
                        retObj.getFieldValueString(looper, colNames[3]));
            }
            //// Called Code add measure to Period, MTD , Prior List

        }
        return qryWhereId;

    }

    public void getAllmeasuresInfo() throws SQLException {  //// main till now
        PbReturnObject retObj;
        String colNames[];
        int psize;
        String sqlstr = resBundle.getString("generateViewByQry2");//generateCrossTabMeasure
        /// Might Need to Add Code to get prior element id
        if (measureIdsList != null && measureIdsList.size() > 0) {
            String qryWhereId = ArrayToCommaString(measureIdsList);
            qryWhereId = removeComparisonType(qryWhereId);
            qryWhereId = getDependentElements(qryWhereId);
            Object[] Obj = null;
            Obj = new Object[3];
            Obj[0] = qryWhereId;
            Obj[1] = qryWhereId;
            Obj[2] = qryWhereId;
            String finalQuery = pbDb.buildQuery(sqlstr, Obj);
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
            for (int looper = 0; looper < psize; looper++) {
                // qryWhereId = ", " + retObj.getFieldValueString(looper, colNames[0]);
                /// Create Hash Map of current and Prior Measures
                ArrayList allInfoList = new ArrayList();
                allInfoList.add(retObj.getFieldValueString(looper, colNames[0]));
                allInfoList.add(retObj.getFieldValueString(looper, colNames[1]));
                allInfoList.add(retObj.getFieldValueString(looper, colNames[2]));
                allInfoList.add(retObj.getFieldValueString(looper, colNames[3]));
                allInfoList.add(retObj.getFieldValueString(looper, colNames[6]));
                allInfoList.add(retObj.getFieldValueString(looper, colNames[7]));
                allInfoList.add(retObj.getFieldValueString(looper, colNames[8]));
                allInfoList.add(retObj.getFieldValueString(looper, colNames[9]));
                allInfoList.add(retObj.getFieldValueString(looper, colNames[10]));
                allInfoList.add(retObj.getFieldValueString(looper, colNames[11]));
                allInfoHaspMap.put(retObj.getFieldValueString(looper, colNames[5]), allInfoList);
                /**
                 * * Are we getting prior
                 */
                if (measureIdsList.contains(retObj.getFieldValueString(looper, colNames[5]))) {

                    NonViewByMap.put("A_" + retObj.getFieldValueString(looper, colNames[5]), retObj.getFieldValueString(looper, colNames[9]));
                }
            }
        }
    }

    public String ArrayToCommaString(List<String> measureIdsList) {
      //  String collecror = "";
        StringBuilder collecror =new StringBuilder();
        if (measureIdsList == null || measureIdsList.isEmpty()) {
        } else {
            for (int loop = 0; loop < measureIdsList.size(); loop++) {
                if (loop == 0) {
                   // collecror += measureIdsList.get(loop);
                    collecror.append(measureIdsList.get(loop));
                } else {
                    collecror.append(", ").append(measureIdsList.get(loop));
                }
            }
        }

        return collecror.toString();
    }

    public String ArrayToCommaStringQuote(List<String> measureIdsList) {
       // String collecror = "";
        StringBuilder collecror = new StringBuilder();
        for (int loop = 0; loop < measureIdsList.size(); loop++) {
            if (loop == 0) {
               // collecror += "'" + measureIdsList.get(loop) + "' ";
                collecror.append("'").append(measureIdsList.get(loop)).append("' ");
            } else {
                collecror.append(", '").append(measureIdsList.get(loop)).append("' ");
            }
        }

        return collecror.toString();
    }

    public void getCustomPeriodSummarizedFormula() {
        /// Need to Change formula for MTD,QTD,YTD,PYMTD Etc
    }

    /**
     * @return the viewBysList
     */
    public List<String> getViewBysList() {
        return viewBysList;
    }

    /**
     * @param viewBysList the viewBysList to set
     */
    public void setViewBysList(List<String> viewBysList) {
        this.viewBysList = viewBysList;
    }

    /**
     * @return the viewIdList
     */
    public List<String> getViewIdList() {
        return viewIdList;
    }

    /**
     * @param viewIdList the viewIdList to set
     */
    public void setViewIdList(List<String> viewIdList) {
        this.viewIdList = viewIdList;
    }

    /**
     * @return the colViewBysList
     */
    public List<String> getColViewBysList() {
        return colViewBysList;
    }

    /**
     * @param colViewBysList the colViewBysList to set
     */
    public void setColViewBysList(List<String> colViewBysList) {
        this.colViewBysList = colViewBysList;
    }

    /**
     * @return the colViewIdList
     */
    public List<String> getColViewIdList() {
        return colViewIdList;
    }

    /**
     * @param colViewIdList the colViewIdList to set
     */
    public void setColViewIdList(List<String> colViewIdList) {
        this.colViewIdList = colViewIdList;
    }

    /**
     * @return the measuresList
     */
    public List<String> getMeasuresList() {
        return measuresList;
    }

    /**
     * @param measuresList the measuresList to set
     */
    public void setMeasuresList(List<String> measuresList) {
        this.measuresList = measuresList;
    }

    /**
     * @return the measureIdsList
     */
    public List<String> getMeasureIdsList() {
        return measureIdsList;
    }

    /**
     * @param measureIdsList the measureIdsList to set
     */
    public void setMeasureIdsList(List<String> measureIdsList) {
        this.measureIdsList = measureIdsList;
    }

    /**
     * @return the aggregationType
     */
    public List<String> getAggregationType() {
        return aggregationType;
    }

    /**
     * @param aggregationType the aggregationType to set
     */
    public void setAggregationType(List<String> aggregationType) {
        this.aggregationType = aggregationType;
    }

    /**
     * @return the queryColName
     */
    public List<String> getQueryColName() {
        return queryColName;
    }

    /**
     * @param queryColName the queryColName to set
     */
    public void setQueryColName(List<String> queryColName) {
        this.queryColName = queryColName;
    }

    /**
     * @return the innerViewByElementId
     */
    public String getInnerViewByElementId() {
        return innerViewByElementId;
    }

    /**
     * @param innerViewByElementId the innerViewByElementId to set
     */
    public void setInnerViewByElementId(String innerViewByElementId) {
        this.innerViewByElementId = innerViewByElementId;
    }

    /**
     * @return the avoidProgenTime
     */
    public String getAvoidProgenTime() {
        return avoidProgenTime;
    }

    /**
     * @param avoidProgenTime the avoidProgenTime to set
     */
    public void setAvoidProgenTime(String avoidProgenTime) {
        this.avoidProgenTime = avoidProgenTime;
    }

    /**
     * @return the timeDetailsArray
     */
    public List<String> getTimeDetailsArray() {
        return timeDetailsArray;
    }

    /**
     * @param timeDetailsArray the timeDetailsArray to set
     */
    public void setTimeDetailsArray(List<String> timeDetailsArray) {
        this.timeDetailsArray = timeDetailsArray;
    }

    /**
     * @return the colAggregationType
     */
    public List<String> getColAggregationType() {
        return colAggregationType;
    }

    /**
     * @param colAggregationType the colAggregationType to set
     */
    public void setColAggregationType(List<String> colAggregationType) {
        this.colAggregationType = colAggregationType;
    }

    public static void main(String[] args) {
        ProgenAOQuery objQuery = new ProgenAOQuery();
        try {
            objQuery.generateAOQuery();
        } catch (SQLException ex) {
//            Logger.getLogger(ProgenAOQuery.class.getName()).log(Level.SEVERE, null, ex);
            logger.error("Exception", ex);
        }
    }

    private void getZeroLevelQuery(String period_Type) {

        List<String> keys = new ArrayList(periodWiseMap.keySet());
//                     List<String> infoKey = new ArrayList(allInfoHaspMap.keySet());
        List<String> column_name = new ArrayList<String>();
        if (period_Type.equalsIgnoreCase("PERIOD")) {
            for (String set : keys) {
                if (set.equalsIgnoreCase("PERIOD")) {
                    levelOneQuery.add(getUserColumnNameForCurrent(periodWiseMap.get(set).get(0), period_Type) + " as " + getUserColumnNameForCurrent(periodWiseMap.get(set).get(0), period_Type));
                } else if (set.equalsIgnoreCase("PRIORPERIOD")) {
                    levelOneQuery.add(null + " as " + getUserColumnNameForPrior(periodWiseMap.get(set).get(0), period_Type));
                }

//                     levelOneQuery.add(entry.getValue().get(3)+ " as " + entry.getValue().get(3)+ "");

            }
        } else if (period_Type.equalsIgnoreCase("PRIORPERIOD")) {
            for (String set : keys) {
                if (set.equalsIgnoreCase("PERIOD")) {
                    levelOneQuery.add(null + " as " + getUserColumnNameForCurrent(periodWiseMap.get(set).get(0), period_Type));
                } else if (set.equalsIgnoreCase("PRIORPERIOD")) {
                    levelOneQuery.add(getUserColumnNameForCurrent(periodWiseMap.get(set).get(0), period_Type) + " as " + getUserColumnNameForPrior(periodWiseMap.get(set).get(0), period_Type));
                }

//                     levelOneQuery.add(entry.getValue().get(3)+ " as " + entry.getValue().get(3)+ "");

            }
        }


    }

    private String getUserColumnNameForCurrent(String element_Id, String period_Type) {
        element_Id = removeComparisonType(element_Id);
        String column_name = "";
        if (element_Id.equals(allInfoHaspMap.get(element_Id).get(4))) {
            column_name = allInfoHaspMap.get(element_Id).get(3);
        } else {
            column_name = getUserColumnNameForCurrent(allInfoHaspMap.get(element_Id).get(4), period_Type);
        }
//              column_name = allInfoHaspMap.get(element_Id).get(3);

//        System.out.println(" Ref_type" + allInfoHaspMap.get(element_Id).get(4));
//        System.out.println(" Ref_type" + allInfoHaspMap.get(element_Id).get(5));
        return column_name;
    }

    private String getUserColumnNameForCurrentAlias(String element_Id, String period_Type) {
        element_Id = removeComparisonType(element_Id);
        String column_name = "";

        column_name = allInfoHaspMap.get(element_Id).get(3);

//        System.out.println(" Ref_type" + allInfoHaspMap.get(element_Id).get(4));
//        System.out.println(" Ref_type" + allInfoHaspMap.get(element_Id).get(5));
        return column_name;
    }

    private String getUserColumnType(String element_Id, String period_Type) {
        element_Id = removeComparisonType(element_Id);
        String column_name = allInfoHaspMap.get(element_Id).get(8);
        return column_name;
    }

    private String getUserColumnFormula(String element_Id, String period_Type, String currPeriodType) {
        element_Id = removeComparisonType(element_Id.replaceAll("A_", ""));


        String column_name = allInfoHaspMap.get(element_Id.replaceAll("A_", "")).get(6);
//        System.out.println("currPeriodType::"+currPeriodType);
//     if(!(period_Type.equalsIgnoreCase("PERIOD") || period_Type.equalsIgnoreCase("PRIORPERIOD") ))
//     {
        //added by Mohit @ 03/MAR/2016
        if(allDependentsMap.get(element_Id.replaceAll("A_", ""))!=null){
        for (int i = 0; i < allDependentsMap.get(element_Id.replaceAll("A_", "")).size(); i++) {
            column_name = column_name.replaceAll(allInfoHaspMap.get(allDependentsMap.get(element_Id.replaceAll("A_", "")).get(i)).get(3),
                    allInfoHaspMap.get(allDependentsMap.get(element_Id.replaceAll("A_", "")).get(i)).get(3) + getAdditionalPeriodSuffix(currPeriodType));


        }
        }
//     }

        return column_name;
    }

    private String getUserColumnAgg(String element_Id, String period_Type) {
        element_Id = removeComparisonType(element_Id);
        String column_name = allInfoHaspMap.get(element_Id.replaceAll("A_", "")).get(9);
        if (column_name.equalsIgnoreCase("COUNT")
                || column_name.equalsIgnoreCase("COUNTDISTINCT")) {
            column_name = "SUM";
        }
        return column_name;
    }

    private String getUserColumnNameForPrior(String element_Id, String period_Type) {
        element_Id = removeComparisonType(element_Id);

        String prior_Id = "";
        String column_name = "";
        try {

            prior_Id = PriorElementIds.get(element_Id);
            column_name = allInfoHaspMap.get(prior_Id).get(3);
        } catch (Exception e) {
            return null;
        }
        return column_name;
    }
//start of code by bhargavi

    public void addRowViewBytoOriginalViewby() {
        if (viewIdList != null && viewIdList.size() > 0) {
            for (int i = 0; i < viewIdList.size(); i++) {
                orgRowViewbyCols.add(viewIdList.get(i));
            }
        }
    }
    //end of code by bhargavi

    private void getAllParameterInfo() throws SQLException {
        PbReturnObject retObj;
        String colNames[];
        int psize;
        String sqlstr = resBundle.getString("generateViewByQry1");
        //start of code by bhargavi   
        addRowViewBytoOriginalViewby();// set original view by list

        if (getColViewIdList() != null && !getColViewIdList().isEmpty()) {// add column view by to viewby id list
            for (int i = 0; i < getColViewIdList().size(); i++) {

                viewIdList.add(getColViewIdList().get(i).toString());
            }
        }
        //end of code by bhargavi   
        String qryWhereId = ArrayToCommaString(viewIdList);
//            qryWhereId = getDependentElements(qryWhereId);
        Object[] Obj = null;
        Obj = new Object[1];
        Obj[0] = qryWhereId;
        String finalQuery = pbDb.buildQuery(sqlstr, Obj);
        if (qryWhereId != null && qryWhereId.length() > 0 && !qryWhereId.equalsIgnoreCase("TIME") && !qryWhereId.toUpperCase().contains("TIME")) {
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
        for (int looper = 0; looper < psize; looper++) {
            // qryWhereId = ", " + retObj.getFieldValueString(looper, colNames[0]);
            /// Create Hash Map of parameter
                /*
             * BussTableID -- 0 Buss_tableName -- 1 Buss_colName -- 2
             * User_colName -- 3 element_id -- 5 ref_element_id --- 6
             * actual_colformula -- 8 user_colDesc --- 9 dim_name --- 14
             */
            ArrayList allInfoList = new ArrayList();
            allInfoList.add(retObj.getFieldValueString(looper, colNames[0]));
            allInfoList.add(retObj.getFieldValueString(looper, colNames[1]));
            allInfoList.add(retObj.getFieldValueString(looper, colNames[2]));
            allInfoList.add(retObj.getFieldValueString(looper, colNames[3]));
            allInfoList.add(retObj.getFieldValueString(looper, colNames[5]));
            allInfoList.add(retObj.getFieldValueString(looper, colNames[6]));
            allInfoList.add(retObj.getFieldValueString(looper, colNames[8]));
            allInfoList.add(retObj.getFieldValueString(looper, colNames[9]));
            allInfoList.add(retObj.getFieldValueString(looper, colNames[14]));
            allInfoHaspMap.put(retObj.getFieldValueString(looper, colNames[5]), allInfoList);
            /**
             * * Are we getting dimension detail
             */
        }
    }

    private String getCompareClause(String element_Id, String timeType) {
        PbTimeRanges pbTime = new PbTimeRanges();
        String timeClause = "";
        String timeClause1 = "";
        String changeFlag = "false";

        String PRG_PERIOD_TYPE = "";
        String PRG_COMPARE = "";
        String date = "";
        if (getTimeDetails().get(1).toString().equalsIgnoreCase("PRG_STD")) {
            if (timeType != null && (timeType.equalsIgnoreCase("MOM") || timeType.equalsIgnoreCase("MTD"))) {
                PRG_PERIOD_TYPE = "Month";
                PRG_COMPARE = "Last Month";
                date = getTimeDetails().get(2).toString();
                pbTime.elementID = element_Id;
                try {
                    pbTime.setRange(PRG_PERIOD_TYPE, PRG_COMPARE, date);
                } catch (Exception e) {
                }
                timeClause = "and DDATE " + pbTime.d_clu;
            } else if (timeType != null && (timeType.equalsIgnoreCase("PMOM") || timeType.equalsIgnoreCase("PMTD"))) {
                PRG_PERIOD_TYPE = "Month";
                PRG_COMPARE = "Last Month";
                date = getTimeDetails().get(2).toString();
                pbTime.elementID = element_Id;
                try {
                    pbTime.setRange(PRG_PERIOD_TYPE, PRG_COMPARE, date);
                } catch (Exception e) {
                }
                timeClause = "and DDATE " + pbTime.pd_clu;
            } else if (timeType != null && (timeType.equalsIgnoreCase("PQOQ") || timeType.equalsIgnoreCase("PQTD"))) {
                PRG_PERIOD_TYPE = "Qtr";
                PRG_COMPARE = "Last Qtr";
                date = getTimeDetails().get(2).toString();
                pbTime.elementID = element_Id;
                try {
                    pbTime.setRange(PRG_PERIOD_TYPE, PRG_COMPARE, date);
                } catch (Exception e) {
                }
                timeClause = "and DDATE " + pbTime.pd_clu;
            } else if (timeType != null && (timeType.equalsIgnoreCase("QOQ") || timeType.equalsIgnoreCase("QTD"))) {
                PRG_PERIOD_TYPE = "Qtr";
                PRG_COMPARE = "Last Qtr";
                date = getTimeDetails().get(2).toString();
                pbTime.elementID = element_Id;
                try {
                    pbTime.setRange(PRG_PERIOD_TYPE, PRG_COMPARE, date);
                } catch (Exception e) {
                }
                timeClause = "and DDATE " + pbTime.d_clu;
            } else if (timeType != null && (timeType.equalsIgnoreCase("MOY") || timeType.equalsIgnoreCase("PYMTD"))) {
                PRG_PERIOD_TYPE = "Month";
                PRG_COMPARE = "Same Month Last Year";
                date = getTimeDetails().get(2).toString();
                pbTime.elementID = element_Id;
                try {
                    pbTime.setRange(PRG_PERIOD_TYPE, PRG_COMPARE, date);
                } catch (Exception e) {
                }
                timeClause = "and DDATE " + pbTime.pd_clu;
            } else if (timeType != null && (timeType.equalsIgnoreCase("QOY") || timeType.equalsIgnoreCase("PYQTD"))) {
                PRG_PERIOD_TYPE = "Qtr";
                PRG_COMPARE = "Same Qtr Last Year";
                date = getTimeDetails().get(2).toString();
                pbTime.elementID = element_Id;
                try {
                    pbTime.setRange(PRG_PERIOD_TYPE, PRG_COMPARE, date);
                } catch (Exception e) {
                }
                timeClause = "and DDATE " + pbTime.pd_clu;
            } else if (timeType != null && (timeType.equalsIgnoreCase("YOY") || timeType.equalsIgnoreCase("YTD"))) {
                PRG_PERIOD_TYPE = "Year";
                PRG_COMPARE = "Last Year";
                date = getTimeDetails().get(2).toString();
                pbTime.elementID = element_Id;
                try {
                    pbTime.setRange(PRG_PERIOD_TYPE, PRG_COMPARE, date);
                } catch (Exception e) {
                }
                timeClause = "and DDATE " + pbTime.d_clu;
            } else if (timeType != null && (timeType.equalsIgnoreCase("PYOY") || timeType.equalsIgnoreCase("PYTD"))) {
                PRG_PERIOD_TYPE = "Year";
                PRG_COMPARE = "Last Year";
                date = getTimeDetails().get(2).toString();
                pbTime.elementID = element_Id;
                try {
                    pbTime.setRange(PRG_PERIOD_TYPE, PRG_COMPARE, date);
                } catch (Exception e) {
                }
                timeClause = "and DDATE " + pbTime.pd_clu;
            }
        } /**
         * Compare Clause For Change Begin *
         */
        else if (getTimeDetails().get(1).toString().equalsIgnoreCase("PRG_STD") && changeFlag.equalsIgnoreCase("true")) {

            if (timeType != null && (timeType.equalsIgnoreCase("MOM") || timeType.equalsIgnoreCase("MOMPer"))) {
                PRG_PERIOD_TYPE = "Month";
                PRG_COMPARE = "Last Month";
                date = getTimeDetails().get(2).toString();
                pbTime.elementID = element_Id;
                try {
                    pbTime.setRange(PRG_PERIOD_TYPE, PRG_COMPARE, date);
                } catch (Exception e) {
                }
                timeClause = "and DDATE " + pbTime.d_clu;
                timeClause1 = "and DDATE " + pbTime.pd_clu;
            } else if (timeType != null && (timeType.equalsIgnoreCase("MOYM") || timeType.equalsIgnoreCase("MOYMPer"))) {
                PRG_PERIOD_TYPE = "Month";
                PRG_COMPARE = "Same Month Last Year";
                date = getTimeDetails().get(2).toString();
                pbTime.elementID = element_Id;
                try {
                    pbTime.setRange(PRG_PERIOD_TYPE, PRG_COMPARE, date);
                } catch (Exception e) {
                }
                timeClause = "and DDATE " + pbTime.d_clu;
                timeClause1 = "and DDATE " + pbTime.pd_clu;

            } else if (timeType != null && (timeType.equalsIgnoreCase("QOQ") || timeType.equalsIgnoreCase("QOQPer"))) {
                PRG_PERIOD_TYPE = "Qtr";
                PRG_COMPARE = "Last Qtr";
                date = getTimeDetails().get(2).toString();
                pbTime.elementID = element_Id;
                try {
                    pbTime.setRange(PRG_PERIOD_TYPE, PRG_COMPARE, date);
                } catch (Exception e) {
                }
                timeClause = "and DDATE " + pbTime.d_clu;
                timeClause1 = "and DDATE " + pbTime.pd_clu;

            } else if (timeType != null && (timeType.equalsIgnoreCase("QOYQ") || timeType.equalsIgnoreCase("QOYQPer"))) {
                PRG_PERIOD_TYPE = "Qtr";
                PRG_COMPARE = "Same Qtr Last Year";
                date = getTimeDetails().get(2).toString();
                pbTime.elementID = element_Id;
                try {
                    pbTime.setRange(PRG_PERIOD_TYPE, PRG_COMPARE, date);
                } catch (Exception e) {
                }
                timeClause = "and DDATE " + pbTime.d_clu;
                timeClause1 = "and DDATE " + pbTime.pd_clu;

            } else if (timeType != null && (timeType.equalsIgnoreCase("YOY") || timeType.equalsIgnoreCase("YOYPer"))) {
                PRG_PERIOD_TYPE = "Year";
                PRG_COMPARE = "Last Year";
                date = getTimeDetails().get(2).toString();
                pbTime.elementID = element_Id;
                try {
                    pbTime.setRange(PRG_PERIOD_TYPE, PRG_COMPARE, date);
                } catch (Exception e) {
                }
                timeClause = "and DDATE " + pbTime.d_clu;
                timeClause1 = "and DDATE " + pbTime.pd_clu;

            }

        }

        return timeClause;
    }

//        }
    private void setTimeType(String timeType) throws SQLException {
        String avoidTimeClause = getAvoidProgenTime();
        if (avoidTimeClause == null) {
            avoidTimeClause = "No";
            setAvoidProgenTime(avoidTimeClause);
        }
        if (getTimeDetails() != null && getTimeDetails().get(1).toString().equalsIgnoreCase("PRG_DATE_RANGE")) {

            if (timeType.equalsIgnoreCase("PERIOD") && avoidTimeClause.equalsIgnoreCase("Yes")) {
//            pbTime.setRange(getTimeDetails());
                timeDetailMap.put(timeType, "");
            } else if (timeType.equalsIgnoreCase("PERIOD") && !avoidTimeClause.equalsIgnoreCase("Yes")) {
                pbTime.date_type=getDateFlag_New();
                pbTime.setRange(getTimeDetails());
                timeDetailMap.put(timeType, pbTime.d_clu);
            } else if (timeType.equalsIgnoreCase("PRIORPERIOD")) {
                pbTime.date_type=getDateFlag_New();
                pbTime.setRange(getTimeDetails().get(3).toString(), getTimeDetails().get(4).toString(), getTimeDetails().get(2).toString());
                timeDetailMap.put(timeType, pbTime.pd_clu);
            } else {
                pbTime.date_type=getDateFlag_New();
                timeDetailMap.put(timeType, pbTime.setRange(getTimeDetails(), timeType));

            }
        } else {
//          System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++"+getTimeDetails().toString());
            if (timeType.equalsIgnoreCase("PERIOD") && avoidTimeClause.equalsIgnoreCase("Yes")) {
//            pbTime.setRange(getTimeDetails().get(3).toString(), getTimeDetails().get(4).toString(), getTimeDetails().get(2).toString());
                timeDetailMap.put(timeType, "");
            } else if (timeType.equalsIgnoreCase("PERIOD") && !avoidTimeClause.equalsIgnoreCase("Yes")) {
               pbTime.date_type=getDateFlag_New();
                pbTime.setRange(getTimeDetails().get(3).toString(), getTimeDetails().get(4).toString(), getTimeDetails().get(2).toString());
                timeDetailMap.put(timeType, pbTime.d_clu);
            } else if (timeType.equalsIgnoreCase("PRIORPERIOD")) {
                pbTime.date_type=getDateFlag_New();
                pbTime.setRange(getTimeDetails().get(3).toString(), getTimeDetails().get(4).toString(), getTimeDetails().get(2).toString());
                timeDetailMap.put(timeType, pbTime.pd_clu);
            } else {
                pbTime.date_type=getDateFlag_New();
                timeDetailMap.put(timeType, pbTime.setRange(getTimeDetails(), timeType + "("));

            }
        }

    }

    /**
     * @return the AO_name
     */
    public String getAO_name() {
        return AO_name;
    }

    /**
     * @param AO_name the AO_name to set
     */
    public void setAO_name(String AO_name) {
        this.AO_name = AO_name;
    }

    /**
     * @return the timeDetails
     */
    public ArrayList<String> getTimeDetails() {
        return timeDetails;
    }

    /**
     * @param timeDetails the timeDetails to set
     */
    public void setTimeDetails(ArrayList<String> timeDetails) {
        this.timeDetails = timeDetails;
    }

    /**
     * @return the filterClause
     */
    public String getFilterClause() {
        return filterClause;
    }

    /**
     * @param filterClause the filterClause to set
     */
    public void setFilterClause(String filterClause) {
        this.filterClause = filterClause;
    }

    /**
     * @return the dimSecurityClause
     */
    public String getDimSecurityClause() {
        return dimSecurityClause;
    }

    /**
     * @param dimSecurityClause the dimSecurityClause to set
     */
    public void setDimSecurityClause(String dimSecurityClause) {
        this.dimSecurityClause = dimSecurityClause;
    }

    public String removeComparisonType(String qryWhereId) {
        String[] split = qryWhereId.split(",");
       // qryWhereId = "";
        StringBuilder qryWhereId1=new StringBuilder();
        for (int i = 0; i < split.length; i++) {
            try {
                split[i] = split[i].substring(0, split[i].indexOf("_"));
                if (i == split.length - 1) {
                   // qryWhereId += split[i];
                    qryWhereId1.append(split[i]);
                } else {
                    qryWhereId1.append(split[i]).append(" ,");
                }
            } catch (Exception e) {
                if (i == split.length - 1) {
                    qryWhereId1.append(split[i]);
                } else {
                    qryWhereId1.append(split[i]).append(" ,");
                }
            }
        }


        return qryWhereId1.toString();
    }

    /**
     * @return the measureIdsListGO
     */
    public List<String> getMeasureIdsListGO() {
        return measureIdsListGO;
    }

    /**
     * @param measureIdsListGO the measureIdsListGO to set
     */
    public void setMeasureIdsListGO(List<String> measureIdsListGO) {
        this.measureIdsListGO = measureIdsListGO;
    }

    private String getCurrentPeriodForChg(String period) {
        String currPeriod = "Period";
        if (period.equalsIgnoreCase("Change")) {
            currPeriod = "Period";
        } else if (period.equalsIgnoreCase("ChangePer")) {
            currPeriod = "Period";
        } else if (period.equalsIgnoreCase("MOM")) {
            currPeriod = "MTD";
        }else if (period.equalsIgnoreCase("WOW")) {
            currPeriod = "WTD";
        } 
        else if (period.equalsIgnoreCase("WOWPer")) {
            currPeriod = "WTD";
        }
        else if (period.equalsIgnoreCase("WOYWPer")) {
            currPeriod = "WTD";
        }
        else if (period.equalsIgnoreCase("WOYW")) {
            currPeriod = "WTD";
        }
        else if (period.equalsIgnoreCase("MOMPer")) {
            currPeriod = "MTD";
        } else if (period.equalsIgnoreCase("MOYM")) {
            currPeriod = "MTD";
        } else if (period.equalsIgnoreCase("MOYMPer")) {
            currPeriod = "MTD";
        } else if (period.equalsIgnoreCase("QOQ")) {
            currPeriod = "QTD";
        } else if (period.equalsIgnoreCase("QOQPer")) {
            currPeriod = "QTD";
        } else if (period.equalsIgnoreCase("QOYQ")) {
            currPeriod = "QTD";
        } else if (period.equalsIgnoreCase("QOYQPer")) {
            currPeriod = "QTD";
        } else if (period.equalsIgnoreCase("YOY")) {
            currPeriod = "YTD";
        } else if (period.equalsIgnoreCase("YOYPer")) {
            currPeriod = "YTD";
        }

        return currPeriod;
    }

    private String getPriorPeriodForChg(String period) {
        String currPeriod = "PriorPeriod";
        if (period.equalsIgnoreCase("Change")) {
            currPeriod = "PriorPeriod";
        } else if (period.equalsIgnoreCase("ChangePer")) {
            currPeriod = "PriorPeriod";
        } else if (period.equalsIgnoreCase("MOM")) {
            currPeriod = "PMTD";
        } else if (period.equalsIgnoreCase("MOMPer")) {
            currPeriod = "PMTD";
        } else if (period.equalsIgnoreCase("MOYM")) {
            currPeriod = "PYMTD";
        } else if (period.equalsIgnoreCase("MOYMPer")) {
            currPeriod = "PYMTD";
        }else if (period.equalsIgnoreCase("WOW")) {
            currPeriod = "PWTD";
        } else if (period.equalsIgnoreCase("WOWPer")) {
            currPeriod = "PWTD";
        } else if (period.equalsIgnoreCase("WOYW")) {
            currPeriod = "PYWTD";
        } else if (period.equalsIgnoreCase("WOYWPer")) {
            currPeriod = "PYWTD";
        } 
        
        else if (period.equalsIgnoreCase("QOQ")) {
            currPeriod = "PQTD";
        } else if (period.equalsIgnoreCase("QOYQPer")) {
            currPeriod = "PQTD";
        } else if (period.equalsIgnoreCase("QOYQ")) {
            currPeriod = "PYQTD";
        } else if (period.equalsIgnoreCase("QOYPer")) {
            currPeriod = "PYQTD";
        } else if (period.equalsIgnoreCase("YOY")) {
            currPeriod = "PYTD";
        } else if (period.equalsIgnoreCase("YOYPer")) {
            currPeriod = "PYTD";
        }
        return currPeriod;
    }

    private void addChangetoQuery() {
        for (int loop = 0; loop < colViewIdChangeList.size(); loop++) {
            String measure = removeComparisonType(colViewIdChangeList.get(loop));
            String MeasureName = "A_" + measure + "_" + colViewIdChangeListAgg.get(loop);
            //levelThreeQuery += ", " + colViewIdChangeFormula.get(loop) + " " + MeasureName;
            levelThreeQuery.append(", ").append(colViewIdChangeFormula.get(loop)).append(" ").append(MeasureName);
            //levelFiveQuery += ", " + MeasureName;
            levelFiveQuery.append(", ").append(MeasureName);

        }


//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public ArrayList combineListSize() {
        ArrayList<String> combine = new ArrayList<String>();
        for (String viewBysList1 : viewBysList) {

            if (combine.isEmpty() || combine.indexOf(viewBysList1) == -1) {
                combine.add(viewBysList1);
            }
        }
        for (String filterElementName1 : filterElementName) {
            if (filterElementName1.contains("A_")) {
                filterElementName1 = filterElementName1;
            } else {
                filterElementName1 = "A_" + filterElementName1;
            }

            if (combine.isEmpty() || combine.indexOf(filterElementName1) == -1) {

                combine.add(filterElementName1);
            }
        }
        return combine;
    }

    public void isThereAgg() throws SQLException {
        String elementList = new String();
        PbReturnObject retObj;
        PbReturnObject retObj1;
        String[] colNames = null;
        String colName = null;
        String minLevel = null;
        ArrayList distinctTableList = new ArrayList();
        ArrayList<Integer> distinctTableListMuti = new ArrayList();
        ArrayList<Integer> distinctTableListMaxAgg = new ArrayList();
        ArrayList<String> AllTableColumns = new ArrayList();
        BigDecimal finalNumber = BigDecimal.ZERO;
        String newTableList = "'-1'";
        boolean getMaxTable = false;
        String additionalClause = "";
        String maxTableAggNumber = "99999999";
        HashMap newTables = new HashMap();
        ArrayList<String> combine = combineListSize();
        if (combine.indexOf("TIME") != -1) {
            combine.remove(combine.indexOf("TIME"));
        }
        if (combine.indexOf("MONTH_YEAR") != -1) {
            combine.remove(combine.indexOf("MONTH_YEAR"));
        }
        if (combine.indexOf("A_MONTH_YEAR") != -1) {
            combine.remove(combine.indexOf("A_MONTH_YEAR"));
        }
        if (combine.indexOf("A_QTR_YEAR") != -1) {
            combine.remove(combine.indexOf("A_QTR_YEAR"));
        }
        if (combine.indexOf("A_YEAR_NAME") != -1) {
            combine.remove(combine.indexOf("A_YEAR_NAME"));
        }
        if (combine.indexOf("QTR_YEAR") != -1) {
            combine.remove(combine.indexOf("QTR_YEAR"));
        }
        if (combine.indexOf("YEAR_NAME") != -1) {
            combine.remove(combine.indexOf("YEAR_NAME"));
        }
        int MaxSize = combine.size();
        String factTable = new String();
        String connType = getMetaConnectionType();


        elementList = ArrayToCommaStringQuote(combine);




    //    String sqlstr = "";
        StringBuilder sqlstr=new StringBuilder();
        if (!(combine.isEmpty() || combine.size() == 0)) {



//            sqlstr += "select AO_Name, AO_column_name, company_id, Place_value, multiple_no , Place_value*multiple_no Agg_pos, Division_no ";
//            sqlstr += " ";
//            sqlstr += " from AO_Agg_master  ";
//            sqlstr += "  M ";
//            sqlstr += " where AO_column_name in  ( " + elementList + ") ";
//            sqlstr += " and AO_Name in ( '" + AO_name + "') ";

            sqlstr.append("select AO_Name, AO_column_name, company_id, Place_value, multiple_no , Place_value*multiple_no Agg_pos, Division_no ");
            sqlstr.append(" ").append(" from AO_Agg_master  ").append("  M ").append(" where AO_column_name in  ( ").append(elementList).append(") ");
            sqlstr.append(" and AO_Name in ( '").append(AO_name).append("') ");

            //sqlstr += " and (company_id = ( '" + tableList + "') or company_id) is null ";
            // sqlstr += " and Agg_date = ( '" + Date + "') "; -- Will Enable Later
            ;



            retObj = pbDb.execSelectSQL(sqlstr.toString());

            colNames = retObj.getColumnNames();


            BigDecimal MaxNum = BigDecimal.ONE;
            int psize = retObj.getRowCount();



            if (psize == 0) {
                getMaxTable = true;
            }

            if (psize > 0 && psize >= MaxSize) {
                //Looping twice
                //Loop 1 find the fact and current and prior cols
                //loop 2 build query
                for (int looper = 0; looper < psize; looper++) {
                    if (distinctTableList == null) {
                        distinctTableList.add(retObj.getFieldValueString(looper, colNames[0]));
                        distinctTableListMuti.add(retObj.getFieldValueInt(looper, colNames[4]));
                        factTable = retObj.getFieldValueString(looper, colNames[0]);

                        if (combine.contains(retObj.getFieldValueString(looper, colNames[2])) == true) {
                            //AllTableColumns = allTablesforReplaceColumns.get(retObj.getFieldValueString(looper, colNames[0]));
                            MaxNum = retObj.getFieldValueBigDecimal(looper, colNames[3]);

                            //distinctTableListMuti.add(retObj.getFieldValueInt(looper, colNames[4]));
                            finalNumber = finalNumber.add(retObj.getFieldValueBigDecimal(looper, colNames[5]).multiply(MaxNum));

                            //finalNumber +=  * MaxNum;
                        } else {
                            getMaxTable = true;
                            break;
                        }
                        //additionalClause += " and  mod(Agg_number, "+retObj.getFieldValueInt(looper, colNames[4]) + ")"

                        additionalClause += " and ((( " + getDBMode("AO_agg_Number", retObj.getFieldValueString(looper, colNames[6]), connType) + ")"
                                + " -  "
                                + " ( " + getDBMode("AO_agg_Number", retObj.getFieldValueString(looper, colNames[4]), connType) + ")"
                                + ") "
                                + " /  (" + retObj.getFieldValueBigDecimal(looper, colNames[4]) + ")) "
                                + " >= " + MaxNum + " ";
                    } else {
                        distinctTableList.add(retObj.getFieldValueString(looper, colNames[0]));
                        distinctTableListMuti.add(retObj.getFieldValueInt(looper, colNames[4]));
                        if (combine.contains(retObj.getFieldValueString(looper, colNames[1])) == true) {
                            //AllTableColumns = allTablesforReplaceColumns.get(retObj.getFieldValueString(looper, colNames[0]));
                            MaxNum = retObj.getFieldValueBigDecimal(looper, colNames[3]);


                            //distinctTableListMuti.add(retObj.getFieldValueInt(looper, colNames[4]));
                            finalNumber = finalNumber.add(retObj.getFieldValueBigDecimal(looper, colNames[5]).multiply(MaxNum));


                        } else {
                            getMaxTable = true;
                            break;
                        }
                        //additionalClause += " and  mod(Agg_number, "+retObj.getFieldValueInt(looper, colNames[4]) + ")"
                        additionalClause += " and ((( " + getDBMode("AO_agg_Number", retObj.getFieldValueString(looper, colNames[6]), connType) + ")"
                                + " -  "
                                + " ( " + getDBMode("AO_agg_Number", retObj.getFieldValueString(looper, colNames[4]), connType) + ")"
                                + ") "
                                + " /  (" + retObj.getFieldValueBigDecimal(looper, colNames[4]) + ")) "
                                + " >= " + MaxNum + " ";
                        //distinctTableListMuti.add(retObj.getFieldValueInt(looper, colNames[4]));
                    }
                    newTableList += " ,'" + retObj.getFieldValueString(looper, colNames[0]) + "' ";

                }
            } else {
                getMaxTable = true;

            }
            if (getMaxTable == true) {

                newAO_name = AO_name;
                return;
            }
        } // If size we have some filter and view by
        else {
            finalNumber = BigDecimal.ZERO;
        }

//        sqlstr = "";
        sqlstr=new StringBuilder();
//        sqlstr += "select AO_agg_Number,new_AO_name, AO_Agg_master_id, AO_Agg_details_id , Is_time_agg ";
//        sqlstr += " ";
//        sqlstr += " from AO_Agg_details  ";
//        sqlstr += "  M ";
//        sqlstr += " where AO_Name = ( '" + AO_name + "') ";
//        //sqlstr += " and (company_id = ( '" + tableList + "') or company_id) is null ";
//        sqlstr += " and AO_agg_number in ( select min(AO_agg_number) ";
//        sqlstr += " from AO_Agg_details  ";
//        sqlstr += "  M ";
//        sqlstr += " where AO_Name = ( '" + AO_name + "') ";
//        //sqlstr += " and (company_id = ( '" + tableList + "') or company_id) is null ";
//        sqlstr += " and AO_agg_number >= " + finalNumber + additionalClause + " ";
//        if (!avoidProgenTime.equalsIgnoreCase("Yes")) {
//            sqlstr += " and Is_time_agg = 'Y' ";
//        }
//        sqlstr += " ) order by 5 desc ";

sqlstr.append("select AO_agg_Number,new_AO_name, AO_Agg_master_id, AO_Agg_details_id , Is_time_agg ").append(" ").append(" from AO_Agg_details  ").append("  M ");
sqlstr.append(" where AO_Name = ( '").append(AO_name).append("') ").append(" and AO_agg_number in ( select min(AO_agg_number) ").append(" from AO_Agg_details  ");
sqlstr.append("  M ").append(" where AO_Name = ( '").append(AO_name).append("') ").append(" and AO_agg_number >= ").append(finalNumber).append(additionalClause).append(" ");
        if (!avoidProgenTime.equalsIgnoreCase("Yes")) {
            sqlstr.append(" and Is_time_agg = 'Y' ");
        }
sqlstr.append(" ) order by 5 desc ");

        retObj1 = pbDb.execSelectSQL(sqlstr.toString());

        colNames = retObj1.getColumnNames();



        int psize1 = retObj1.getRowCount();
        String AggHeaderId = new String();
        if (psize1 == 0) {
            newAO_name = AO_name;
            return;
        }
        if (psize1 > 0) {
            //Looping twice
            //Loop 1 find the fact and current and prior cols
            //loop 2 build query
            for (int looper = 0; looper < psize1; looper++) {

                maxTableAggNumber = (retObj1.getFieldValueString(looper, colNames[0]));
                newAO_name = (retObj1.getFieldValueString(looper, colNames[1]));
                AggHeaderId = (retObj1.getFieldValueString(looper, colNames[3]));
            }

        }
        if (newAO_name != null && !newAO_name.equalsIgnoreCase("")) {
            setAO_name(newAO_name);
        }




    }

    public String aoReplace(String AOName, ArrayList parameterFilter) throws SQLException {
        String NewAOName = AOName;
        if (parameterFilter.indexOf("TIME") != -1) {
            parameterFilter.remove(parameterFilter.indexOf("TIME"));
        }
        if (parameterFilter.indexOf("MONTH_YEAR") != -1) {
            parameterFilter.remove(parameterFilter.indexOf("MONTH_YEAR"));
        }
        if (parameterFilter.indexOf("A_MONTH_YEAR") != -1) {
            parameterFilter.remove(parameterFilter.indexOf("A_MONTH_YEAR"));
        }
        if (parameterFilter.indexOf("QTR_YEAR") != -1) {
            parameterFilter.remove(parameterFilter.indexOf("QTR_YEAR"));
        }
        if (parameterFilter.indexOf("A_QTR_YEAR") != -1) {
            parameterFilter.remove(parameterFilter.indexOf("A_QTR_YEAR"));
        }
        if (parameterFilter.indexOf("YEAR_NAME") != -1) {
            parameterFilter.remove(parameterFilter.indexOf("YEAR_NAME"));
        }
        if (parameterFilter.indexOf("A_YEAR_NAME") != -1) {
            parameterFilter.remove(parameterFilter.indexOf("A_YEAR_NAME"));
        }
        String elementList = new String();
        PbReturnObject retObj;
        PbReturnObject retObj1;
        String[] colNames = null;
        String colName = null;
        String minLevel = null;
        ArrayList distinctTableList = new ArrayList();
        ArrayList<Integer> distinctTableListMuti = new ArrayList();
        ArrayList<Integer> distinctTableListMaxAgg = new ArrayList();
        ArrayList<String> AllTableColumns = new ArrayList();
        BigDecimal finalNumber = BigDecimal.ZERO;
        String newTableList = "'-1'";
        boolean getMaxTable = false;
        //String additionalClause = "";
        StringBuilder additionalClause = new StringBuilder();
        String maxTableAggNumber = "99999999";
        HashMap newTables = new HashMap();
        ArrayList<String> combine = new ArrayList<String>();

        String factTable = new String();
        String connType = getMetaConnectionType();

        if (!parameterFilter.isEmpty()) {
            for (int looper = 0; looper < parameterFilter.size(); looper++) {
                if (parameterFilter.get(looper).toString().contains("A_")) {
                    combine.add(parameterFilter.get(looper).toString());
                } else {
                    combine.add("A_" + parameterFilter.get(looper).toString());
                }

            }
        }
        int MaxSize = combine.size();
        elementList = ArrayToCommaStringQuote(combine);

//        String sqlstr = "";
        StringBuilder sqlstr=new StringBuilder();

        if (!(combine.isEmpty() || combine.size() == 0)) {



//            sqlstr += "select AO_Name, AO_column_name, company_id, Place_value, multiple_no , Place_value*multiple_no Agg_pos, Division_no ";
//            sqlstr += " ";
//            sqlstr += " from AO_Agg_master  ";
//            sqlstr += "  M ";
//            sqlstr += " where AO_column_name in  ( " + elementList + ") ";
//            sqlstr += " and AO_Name in ( '" + AOName + "') ";

            sqlstr.append("select AO_Name, AO_column_name, company_id, Place_value, multiple_no , Place_value*multiple_no Agg_pos, Division_no ");
            sqlstr.append(" ").append(" from AO_Agg_master  ").append("  M ").append(" where AO_column_name in  ( ").append(elementList).append(") ");
            sqlstr.append(" and AO_Name in ( '").append(AOName).append("') ");

            //sqlstr += " and (company_id = ( '" + tableList + "') or company_id) is null ";
            // sqlstr += " and Agg_date = ( '" + Date + "') "; -- Will Enable Later
            ;



            retObj = pbDb.execSelectSQL(sqlstr.toString());

            colNames = retObj.getColumnNames();


            BigDecimal MaxNum = BigDecimal.ONE;
            int psize = retObj.getRowCount();

            if (psize == 0) {
                getMaxTable = true;
            }

            if (psize > 0 && psize >= MaxSize) {
                //Looping twice
                //Loop 1 find the fact and current and prior cols
                //loop 2 build query
                for (int looper = 0; looper < psize; looper++) {
                    if (distinctTableList == null) {
                        distinctTableList.add(retObj.getFieldValueString(looper, colNames[0]));
                        distinctTableListMuti.add(retObj.getFieldValueInt(looper, colNames[4]));
                        factTable = retObj.getFieldValueString(looper, colNames[0]);

                        if (combine.contains(retObj.getFieldValueString(looper, colNames[2])) == true) {
                            //AllTableColumns = allTablesforReplaceColumns.get(retObj.getFieldValueString(looper, colNames[0]));
                            MaxNum = retObj.getFieldValueBigDecimal(looper, colNames[3]);


                            //distinctTableListMuti.add(retObj.getFieldValueInt(looper, colNames[4]));
                            finalNumber = finalNumber.add(retObj.getFieldValueBigDecimal(looper, colNames[5]).multiply(MaxNum));

                        } else {
                            getMaxTable = true;
                            break;
                        }
                        //additionalClause += " and  mod(Agg_number, "+retObj.getFieldValueInt(looper, colNames[4]) + ")"
//                        additionalClause += " and ((( " + getDBMode("AO_agg_Number", retObj.getFieldValueString(looper, colNames[6]), connType) + ")"
//                                + " -  "
//                                + " ( " + getDBMode("AO_agg_Number", retObj.getFieldValueString(looper, colNames[4]), connType) + ")"
//                                + ") "
//                                + " /  (" + retObj.getFieldValueBigDecimal(looper, colNames[4]) + ")) "
//                                + " > = " + MaxNum + " ";
                        additionalClause.append(" and ((( ").append(getDBMode("AO_agg_Number", retObj.getFieldValueString(looper, colNames[6]), connType)).append(")").append(" -  ");
                        additionalClause.append(" ( ").append(getDBMode("AO_agg_Number", retObj.getFieldValueString(looper, colNames[4]), connType)).append(")").append(") ").append(" /  (").append(retObj.getFieldValueBigDecimal(looper, colNames[4])).append(")) ");
                        additionalClause.append(" >= ").append(MaxNum).append(" ");
                    } else {
                        distinctTableList.add(retObj.getFieldValueString(looper, colNames[0]));
                        distinctTableListMuti.add(retObj.getFieldValueInt(looper, colNames[4]));
                        if (combine.contains(retObj.getFieldValueString(looper, colNames[1])) == true) {
                            //AllTableColumns = allTablesforReplaceColumns.get(retObj.getFieldValueString(looper, colNames[0]));
                            MaxNum = retObj.getFieldValueBigDecimal(looper, colNames[3]);


                            //distinctTableListMuti.add(retObj.getFieldValueInt(looper, colNames[4]));
                            finalNumber = finalNumber.add(retObj.getFieldValueBigDecimal(looper, colNames[5]).multiply(MaxNum));

                        } else {
                            getMaxTable = true;
                            break;
                        }
                        //additionalClause += " and  mod(Agg_number, "+retObj.getFieldValueInt(looper, colNames[4]) + ")"
                        additionalClause.append(" and ((( ").append(getDBMode("AO_agg_Number", retObj.getFieldValueString(looper, colNames[6]), connType)).append(")");
                                additionalClause.append(" -  ");
                                additionalClause.append(" ( ").append(getDBMode("AO_agg_Number", retObj.getFieldValueString(looper, colNames[4]), connType)).append(")");
                                additionalClause.append(") ");
                                additionalClause.append(" /  (").append(retObj.getFieldValueBigDecimal(looper, colNames[4])).append(")) ");
                                additionalClause.append(" >= ").append(MaxNum).append(" ");
                        //distinctTableListMuti.add(retObj.getFieldValueInt(looper, colNames[4]));
                    }
                    newTableList += " ,'" + retObj.getFieldValueString(looper, colNames[0]) + "' ";

                }
            } else {
                getMaxTable = true;

            }
            if (getMaxTable == true) {

                NewAOName = AOName;
                return NewAOName;
            }
        } // If size we have some filter and view by
        else {
            finalNumber = BigDecimal.ZERO;
        }
     //   sqlstr = "";
        sqlstr=new StringBuilder();
//        sqlstr += "select AO_agg_Number,new_AO_name, AO_Agg_master_id, AO_Agg_details_id , Is_time_agg ";
//        sqlstr += " ";
//        sqlstr += " from AO_Agg_details  ";
//        sqlstr += "  M ";
//        sqlstr += " where AO_Name = ( '" + AOName + "') ";
//        //sqlstr += " and (company_id = ( '" + tableList + "') or company_id) is null ";
//        sqlstr += " and AO_agg_number in ( select min(AO_agg_number) ";
//        sqlstr += " from AO_Agg_details  ";
//        sqlstr += "  M ";
//        sqlstr += " where AO_Name = ( '" + AOName + "') ";
//        //sqlstr += " and (company_id = ( '" + tableList + "') or company_id) is null ";
//        sqlstr += " and AO_agg_number >= " + finalNumber + additionalClause + " ";
//        if (avoidProgenTime != null && !avoidProgenTime.equalsIgnoreCase("Yes")) {
//            sqlstr += " and Is_time_agg = 'Y' ";
//        }
//        sqlstr += " ) order by 5 desc ";

      sqlstr.append("select AO_agg_Number,new_AO_name, AO_Agg_master_id, AO_Agg_details_id , Is_time_agg ").append(" ").append(" from AO_Agg_details  ");
      sqlstr.append("  M ").append(" where AO_Name = ( '").append(AOName).append("') ").append(" and AO_agg_number in ( select min(AO_agg_number) ").append(" from AO_Agg_details  ");
      sqlstr.append("  M ").append(" where AO_Name = ( '").append(AOName).append("') ").append(" and AO_agg_number >= ").append(finalNumber).append(additionalClause).append(" ");
        if (avoidProgenTime != null && !avoidProgenTime.equalsIgnoreCase("Yes")) {
            sqlstr.append(" and Is_time_agg = 'Y' ");
        }
sqlstr.append(" ) order by 5 desc ");






        retObj1 = pbDb.execSelectSQL(sqlstr.toString());

        colNames = retObj1.getColumnNames();



        int psize1 = retObj1.getRowCount();
        String AggHeaderId = new String();
        if (psize1 == 0) {
            NewAOName = AOName;
            return NewAOName;
        }
        if (psize1 > 0) {
            //Looping twice
            //Loop 1 find the fact and current and prior cols
            //loop 2 build query
            for (int looper = 0; looper < psize1; looper++) {

                maxTableAggNumber = (retObj1.getFieldValueString(looper, colNames[0]));
                NewAOName = (retObj1.getFieldValueString(looper, colNames[1]));
                AggHeaderId = (retObj1.getFieldValueString(looper, colNames[3]));
            }

        }
//        if(NewAOName!=null && !NewAOName.equalsIgnoreCase("")){
//            setAO_name(NewAOName);
//        }





        return (NewAOName);
    }

    public String getDBMode(String Quo, String Div, String DB) {
        String clause = "";
        if (DB.equalsIgnoreCase("SqlServer")) {
            clause = " (" + Quo + "%" + Div + ")";
        } else if (DB.equalsIgnoreCase("ORACLE")) {
            clause = " mod(" + Quo + "," + Div + ")";
        } else if (DB.equalsIgnoreCase("Mysql")) {
            clause = " mod(" + Quo + "," + Div + ")";
        }
        return clause;
    }

    /**
     * @return the drillFormat
     */
    public String getDrillFormat() {
        return drillFormat;
    }

    /**
     * @param drillFormat the drillFormat to set
     */
    public void setDrillFormat(String drillFormat) {
        this.drillFormat = drillFormat;
    }

    /**
     * @return the filtermapFromSet
     */
    public HashMap<String, List> getFilterMap() {
        return filterMap;
    }

    /**
     * @param filtermapFromSet the filtermapFromSet to set
     */
    public void setFilterMap(HashMap<String, List> filterMap) {
        this.filterMap = filterMap;
    }

    private void getUserConnection() {
        if (userConnType == null) {
            userConnType = getuserConnType();  // added by Mayank on 19- Aug
        }
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private String getuserConnType() {
        String userConnType = null;






        GetConnectionType gettypeofconn = new GetConnectionType();
        if (measureIdsList != null && measureIdsList.size() > 0) {
            userConnType = gettypeofconn.getConTypeByElementId(removeComparisonType(measureIdsList.get(0).toString()));//_PRIOR]
        } else if (measuresList != null && measuresList.size() > 0) {
            userConnType = gettypeofconn.getConTypeByElementId(removeComparisonType(measuresList.get(0).toString()));//_PRIOR]
        } else if (measureIdsListGO != null && measureIdsListGO.size() > 0) {
            userConnType = gettypeofconn.getConTypeByElementId(removeComparisonType(measureIdsListGO.get(0).toString()));
        } else if (viewIdList != null && viewIdList.size() > 0) {
            if (!viewIdList.get(0).equals("TIME")) {
                userConnType = gettypeofconn.getConTypeByElementId(viewIdList.get(0));
            } else if (viewIdList.size() >= 1 && !viewIdList.get(1).equals("TIME")) {
                userConnType = gettypeofconn.getConTypeByElementId(viewIdList.get(1));
            }
        }
        if (userConnType == null) {
            userConnType = "UNKNOWN";
        }

        return userConnType;
    }

    private void setDatabaseHint() {

        if (userConnType.equalsIgnoreCase("ORACLE")) {
            dataBaseHintInnerMost = " /*+ PUSH_SUBQ */  ";
            dataBaseHintOuterMost = " /*+ CURSOR_SHARING_EXACT RESULT_CACHE */ ";
        }
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @return the chartTimeClause
     */
    public String getChartTimeClause() {
        return chartTimeClause;
    }

    /**
     * @param chartTimeClause the chartTimeClause to set
     */
    public void setChartTimeClause(String chartTimeClause) {
        this.chartTimeClause = chartTimeClause;
    }

    /**
     * @return the isChartTimeEnable
     */
    public String getIsChartTimeEnable() {
        return isChartTimeEnable;
    }

    /**
     * @param isChartTimeEnable the isChartTimeEnable to set
     */
    public void setIsChartTimeEnable(String isChartTimeEnable) {
        this.isChartTimeEnable = isChartTimeEnable;
    }

    private String getDdateClauseForChart() {
        if (chartTimeClause != null && !chartTimeClause.equalsIgnoreCase("")) {
            try {
                return pbTime.setRange(getTimeDetails(), getChartTimeClause() + "(");
            } catch (SQLException ex) {
                logger.error("Exception occured ", ex);
            }
        }
        return "";
    }

    /**
     * @return the sortingType
     */
    public String getSortingType() {
        return sortingType;
    }

    /**
     * @param sortingType the sortingType to set
     */
    public void setSortingType(String sortingType) {
        this.sortingType = sortingType;
    }

    /**
     * @return the chartType
     */
    public String getChartType() {
        return chartType;
    }

    /**
     * @param chartType the chartType to set
     */
    public void setChartType(String chartType) {
        this.chartType = chartType;
    }

    /**
     * @return the chartRecords
     */
    public String getChartRecords() {
        return chartRecords;
    }

    /**
     * @param chartRecords the chartRecords to set
     */
    public void setChartRecords(String chartRecords) {
        this.chartRecords = chartRecords;
    }

    private void getUpperClauseForHigherData() {
        if (sortingType != null && chartType != null && sortingType.equalsIgnoreCase("Value") && chartType.equalsIgnoreCase("Table") && viewIdList != null && viewIdList.size() > 3) {
//                finalAOQuery = finalAOQuery + " order by " + (viewIdList.size()+1) + " desc ) "
//                        + " where rownum <= "+ chartRecords ;     
         //   finalAOQuery = "select * from ( " + finalAOQuery + " order by " + (viewIdList.size() + 1) + " desc ) " + " where rownum <= " + chartRecords;
            StringBuilder str=new StringBuilder();
            str.append("select * from ( ").append(finalAOQuery).append(" order by ").append(viewIdList.size() + 1).append(" desc ) " + " where rownum <= ").append(chartRecords);
            finalAOQuery=str;
        } else if (sortingType != null && chartType != null && sortingType.equalsIgnoreCase("Alphabetic") && chartType.equalsIgnoreCase("Table") && viewIdList != null && viewIdList.size() > 3) {
//                finalAOQuery = finalAOQuery + " order by " + (viewIdList.size()) + " desc ) "
//                        + " where rownum <= "+ chartRecords ;     
        //    finalAOQuery = "select * from ( " + finalAOQuery + " order by " + viewIdList.size() + " desc ) " + " where rownum <= " + chartRecords;
             StringBuilder str1=new StringBuilder();
            str1.append("select * from ( ").append(finalAOQuery).append(" order by ").append(viewIdList.size()).append(" desc ) " + " where rownum <= ").append(chartRecords);
            finalAOQuery=str1;
        }
    }

    private String getPrior(String getPeriod) {
        if (getPeriod.equalsIgnoreCase("Period")) {
            return "PRIORPERIOD";
        }
        if (getPeriod.equalsIgnoreCase("MTD")) {
            return "PMTD";
        }
        if (getPeriod.equalsIgnoreCase("WTD")) {
            return "PWTD";
        }
        
        if (getPeriod.equalsIgnoreCase("YMTD")) {
            return "PYMTD";
        }
        if (getPeriod.equalsIgnoreCase("QTD")) {
            return "PQTD";
        }
        if (getPeriod.equalsIgnoreCase("YQTD")) {
            return "PYQTD";
        }
        if (getPeriod.equalsIgnoreCase("YTD")) {
            return "PYTD";
        }
        return "PriorPeriod";
        /*
         * throw new UnsupportedOperationException("Not supported yet."); //To
         * change body of generated methods, choose Tools | Templates.
         * if(timeDetailsArray.get(loop).equalsIgnoreCase("Period") ||
         * timeDetailsArray.get(loop).equalsIgnoreCase("MTD") ||
         * timeDetailsArray.get(loop).equalsIgnoreCase("YMTD") ||
         * timeDetailsArray.get(loop).equalsIgnoreCase("QTD") ||
         * timeDetailsArray.get(loop).equalsIgnoreCase("YQTD") ||
         * timeDetailsArray.get(loop).equalsIgnoreCase("YTD")
         *
         *
         *
         */
    }

    /**
     * @return the grandTotalAggType
     */
    public List<String> getGrandTotalAggType() {
        return grandTotalAggType;
    }

    /**
     * @param grandTotalAggType the grandTotalAggType to set
     */
    public void setGrandTotalAggType(List<String> grandTotalAggType) {
        this.grandTotalAggType = grandTotalAggType;
    }

    /**
     * @return the measureFilter
     */
    public String getMeasureFilter() {
        return measureFilter;
}

    /**
     * @param measureFilter the measureFilter to set
     */
    public void setMeasureFilter(String measureFilter) {
        this.measureFilter = measureFilter;
    }

    /**
     * @return the dateFlag_New
     */
    public String getDateFlag_New() {
        return dateFlag_New;
}

    /**
     * @param dateFlag_New the dateFlag_New to set
     */
    public void setDateFlag_New(String dateFlag_New) {
        this.dateFlag_New = dateFlag_New;
    }
}
