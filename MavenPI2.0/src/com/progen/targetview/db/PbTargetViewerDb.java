package com.progen.targetview.db;

import com.progen.target.PbTargetCollection;
import com.progen.target.PbTargetRequestParameter;
import com.progen.target.display.DisplayTargetParameters;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;

public class PbTargetViewerDb {

    private Object collectionObj;
    public static Logger logger = Logger.getLogger(PbTargetViewerDb.class);

    public void prepareTarget(String pbTargetId, String minTimeLevel, String pbUserId, HttpServletRequest request, HttpServletResponse response) {
//        HttpSession session = request.getSession(true);
        HttpSession session = request.getSession(false);
        DisplayTargetParameters disp = null;
        PbTargetCollection collect = null;
        PbTargetRequestParameter targetReqParams = null;

        String targetParamSectionDisplay = "";


        try {
            if (session != null) {

                LinkedHashMap timeDimMap = new LinkedHashMap();
                ArrayList dateArray = new ArrayList();
                ArrayList periodTypeArray = new ArrayList();



                session.setAttribute("TARGETID", pbTargetId);
                session.setAttribute("TIMELEVEL", minTimeLevel);
                session.setAttribute("userid", pbUserId);
                session.setAttribute("oldTargetId", pbTargetId);

                targetReqParams = new PbTargetRequestParameter(request);
                collect = new PbTargetCollection();
                disp = new DisplayTargetParameters();

                targetReqParams.setParametersHashMap();
                collect.targetId = pbTargetId;
                disp.tarId = pbTargetId;
                collect.minTimeLevel = minTimeLevel;
                collect.targetIncomingParameters = targetReqParams.requestParamValues;

                //collect.timeDetailsArray = timeDetails;

                collect.timeDetailsMap = timeDimMap;

                collect.getParamMetaData();
                //////////////////////////////////////////////////////////////////////////.println(minTimeLevel+"- minTimeLevel timeDimMap-.. "+collect.timeDetailsMap);
                //////////////////////////.println(" collect.targetIncomingParameters "+collect.targetIncomingParameters);
                //////////////////////////////////////////////////////////////////////////.println(" collect.timeDetailsArray "+collect.timeDetailsArray);

                //setCollectionObj(collect);
                String pType = collect.timeDetailsArray.get(4).toString();
                ArrayList newTimeArray = collect.timeDetailsArray;
                LinkedHashMap newTimeMap = collect.timeDetailsMap;
                //////////////////////////////////////////////////////////////////////////.println(" pType "+pType+" minTimeLevel=-== "+minTimeLevel);
                if (pType.equalsIgnoreCase("Month")) {
                    newTimeArray = new ArrayList();
                    ArrayList al = collect.getSelectedMonths(pType, collect.timeDetailsArray.get(2).toString(), collect.timeDetailsArray.get(3).toString(), collect.targetIncomingParameters, minTimeLevel);

                    newTimeArray.add(0, collect.timeDetailsArray.get(4).toString());
                    newTimeArray.add("");
                    if (collect.targetIncomingParameters.containsKey("CBO_AS_OF_MONTH")) {
                        newTimeArray.add(collect.targetIncomingParameters.get("CBO_AS_OF_MONTH"));
                    } else {
                        newTimeArray.add(al.get(0));
                    }
                    if (collect.targetIncomingParameters.containsKey("CBO_AS_OF_MONTH")) {
                        newTimeArray.add(collect.targetIncomingParameters.get("CBO_AS_OF_MONTH1"));
                    } else {
                        newTimeArray.add(al.get(1));
                    }
                    newTimeArray.add(collect.timeDetailsArray.get(4).toString());

                    ArrayList stList = new ArrayList();
                    if (collect.targetIncomingParameters.containsKey("CBO_AS_OF_MONTH")) {
                        stList.add(collect.targetIncomingParameters.get("CBO_AS_OF_MONTH"));
                    } else {
                        stList.add(al.get(0).toString());
                    }
                    stList.add("CBO_AS_OF_MONTH");
                    stList.add("From Month");
                    stList.add("");
                    stList.add(collect.timeDetailsArray.get(4).toString());
                    if (collect.targetIncomingParameters.containsKey("CBO_AS_OF_MONTH")) {
                        stList.add(collect.targetIncomingParameters.get("CBO_AS_OF_MONTH"));
                    } else {
                        stList.add(al.get(0).toString());
                    }
                    stList.add("AS_OF_MONTH");
                    newTimeMap.put("AS_OF_MONTH", stList);

                    ArrayList endList = new ArrayList();
                    if (collect.targetIncomingParameters.containsKey("CBO_AS_OF_MONTH1")) {
                        endList.add(collect.targetIncomingParameters.get("CBO_AS_OF_MONTH1"));
                    } else {
                        endList.add(al.get(1).toString());
                    }
                    endList.add("CBO_AS_OF_MONTH1");
                    endList.add("To Month");
                    endList.add("");
                    endList.add(collect.timeDetailsArray.get(4).toString());
                    if (collect.targetIncomingParameters.containsKey("CBO_AS_OF_MONTH1")) {
                        endList.add(collect.targetIncomingParameters.get("CBO_AS_OF_MONTH1"));
                    } else {
                        endList.add(al.get(1).toString());
                    }
                    endList.add("AS_OF_MONTH1");
                    newTimeMap.put("AS_OF_MONTH1", endList);

                    collect.timeDetailsArray = newTimeArray;
                    if (collect.timeDetailsMap.containsKey("AS_OF_YEAR")) {
                        collect.timeDetailsMap.remove("AS_OF_YEAR");
                    }
                    if (collect.timeDetailsMap.containsKey("AS_OF_YEAR1")) {
                        collect.timeDetailsMap.remove("AS_OF_YEAR1");
                    }
                    //////////////////////////////////////////////////////////////////////////.println(" newTimeArray "+newTimeArray);
                    LinkedHashMap hm = collect.timeDetailsMap;

                    ArrayList perioList = (ArrayList) collect.timeDetailsMap.get("PRG_PERIOD");
                    hm = new LinkedHashMap();
                    hm.put("AS_OF_MONTH", stList);
                    hm.put("AS_OF_MONTH1", endList);
                    hm.put("PRG_PERIOD", perioList);

                    collect.timeDetailsArray = newTimeArray;
                    collect.timeDetailsMap = hm;
                    //////////////////////////////////////////////////////////////////////////.println(collect.timeDetailsArray+" updated fdf the timeArray "+collect.timeDetailsMap);
                } else if (pType.equalsIgnoreCase("Day")) {
                    newTimeArray = new ArrayList();
                    //////////////////////////////////////////////////////////////////////////.println(" in day ");
                    ArrayList al = collect.getSelectedMonths("Day", collect.timeDetailsArray.get(2).toString(), collect.timeDetailsArray.get(3).toString(), collect.targetIncomingParameters, minTimeLevel);

                    newTimeArray.add(0, collect.timeDetailsArray.get(4).toString());
                    newTimeArray.add("");
                    if (collect.targetIncomingParameters.containsKey("CBO_AS_OF_DATE")) {
                        newTimeArray.add(collect.targetIncomingParameters.get("CBO_AS_OF_DATE"));
                    } else {
                        newTimeArray.add(al.get(0));
                    }
                    if (collect.targetIncomingParameters.containsKey("CBO_AS_OF_DATE")) {
                        newTimeArray.add(collect.targetIncomingParameters.get("CBO_AS_OF_DATE1"));
                    } else {
                        newTimeArray.add(al.get(1));
                    }
                    newTimeArray.add(collect.timeDetailsArray.get(4).toString());

                    ArrayList stList = new ArrayList();
                    if (collect.targetIncomingParameters.containsKey("CBO_AS_OF_DATE")) {
                        stList.add(collect.targetIncomingParameters.get("CBO_AS_OF_DATE"));
                    } else {
                        stList.add(al.get(0).toString());
                    }
                    stList.add("CBO_AS_OF_DATE");
                    stList.add("From Day");
                    stList.add("");
                    stList.add(collect.timeDetailsArray.get(4).toString());
                    if (collect.targetIncomingParameters.containsKey("CBO_AS_OF_DATE")) {
                        stList.add(collect.targetIncomingParameters.get("CBO_AS_OF_DATE"));
                    } else {
                        stList.add(al.get(0).toString());
                    }
                    stList.add("AS_OF_DATE");
                    newTimeMap.put("AS_OF_DATE", stList);

                    ArrayList endList = new ArrayList();
                    if (collect.targetIncomingParameters.containsKey("CBO_AS_OF_DATE1")) {
                        endList.add(collect.targetIncomingParameters.get("CBO_AS_OF_DATE1"));
                    } else {
                        endList.add(al.get(1).toString());
                    }
                    endList.add("CBO_AS_OF_DATE1");
                    endList.add("To Day");
                    endList.add("");
                    endList.add(collect.timeDetailsArray.get(4).toString());
                    if (collect.targetIncomingParameters.containsKey("CBO_AS_OF_DATE1")) {
                        endList.add(collect.targetIncomingParameters.get("CBO_AS_OF_DATE1"));
                    } else {
                        endList.add(al.get(1).toString());
                    }
                    endList.add("AS_OF_DATE1");
                    newTimeMap.put("AS_OF_DATE1", endList);

                    collect.timeDetailsArray = newTimeArray;
                    if (collect.timeDetailsMap.containsKey("AS_OF_MONTH")) {
                        collect.timeDetailsMap.remove("AS_OF_MONTH");
                    }
                    if (collect.timeDetailsMap.containsKey("AS_OF_MONTH1")) {
                        collect.timeDetailsMap.remove("AS_OF_MONTH1");
                    }
                    //////////////////////////////////////////////////////////////////////////.println(" newTimeArray "+newTimeArray);
                    LinkedHashMap hm = collect.timeDetailsMap;

                    ArrayList perioList = (ArrayList) collect.timeDetailsMap.get("PRG_PERIOD");
                    hm = new LinkedHashMap();
                    hm.put("AS_OF_DATE", stList);
                    hm.put("AS_OF_DATE1", endList);
                    hm.put("PRG_PERIOD", perioList);

                    collect.timeDetailsArray = newTimeArray;
                    collect.timeDetailsMap = hm;
                    //////////////////////////////////////////////////////////////////////////.println(collect.timeDetailsArray+" updated 79789 day the timeArray "+collect.timeDetailsMap);
                } else if (pType.equalsIgnoreCase("Qtr") || pType.equalsIgnoreCase("Quarter")) {
                    //////////////////////////////////////////////////////////////////////////.println(" in qtr ");
                    newTimeArray = new ArrayList();
                    ArrayList al = collect.getSelectedMonths(pType, collect.timeDetailsArray.get(2).toString(), collect.timeDetailsArray.get(3).toString(), collect.targetIncomingParameters, minTimeLevel);
                    //////////////////////////////////////////////////////////////////////////.println(" al "+al);
                    newTimeArray.add(0, collect.timeDetailsArray.get(4).toString());
                    newTimeArray.add("");
                    if (collect.targetIncomingParameters.containsKey("CBO_AS_OF_QTR")) {
                        newTimeArray.add(collect.targetIncomingParameters.get("CBO_AS_OF_QTR"));
                    } else {
                        newTimeArray.add(al.get(0));
                    }
                    if (collect.targetIncomingParameters.containsKey("CBO_AS_OF_QTR")) {
                        newTimeArray.add(collect.targetIncomingParameters.get("CBO_AS_OF_QTR1"));
                    } else {
                        newTimeArray.add(al.get(1));
                    }
                    newTimeArray.add(collect.timeDetailsArray.get(4).toString());

                    ArrayList stList = new ArrayList();
                    if (collect.targetIncomingParameters.containsKey("CBO_AS_OF_QTR")) {
                        stList.add(collect.targetIncomingParameters.get("CBO_AS_OF_QTR"));
                    } else {
                        stList.add(al.get(0).toString());
                    }
                    stList.add("CBO_AS_OF_QTR");
                    stList.add("From Quarter");
                    stList.add("");
                    stList.add(collect.timeDetailsArray.get(4).toString());
                    if (collect.targetIncomingParameters.containsKey("CBO_AS_OF_QTR")) {
                        stList.add(collect.targetIncomingParameters.get("CBO_AS_OF_QTR"));
                    } else {
                        stList.add(al.get(0).toString());
                    }
                    stList.add("AS_OF_QTR");
                    newTimeMap.put("AS_OF_QTR", stList);

                    ArrayList endList = new ArrayList();
                    if (collect.targetIncomingParameters.containsKey("CBO_AS_OF_QTR1")) {
                        endList.add(collect.targetIncomingParameters.get("CBO_AS_OF_QTR1"));
                    } else {
                        endList.add(al.get(1).toString());
                    }
                    endList.add("CBO_AS_OF_QTR1");
                    endList.add("To Quarter");
                    endList.add("");
                    endList.add(collect.timeDetailsArray.get(4).toString());
                    if (collect.targetIncomingParameters.containsKey("CBO_AS_OF_QTR1")) {
                        endList.add(collect.targetIncomingParameters.get("CBO_AS_OF_QTR1"));
                    } else {
                        endList.add(al.get(1).toString());
                    }
                    endList.add("AS_OF_QTR1");
                    newTimeMap.put("AS_OF_QTR1", endList);

                    collect.timeDetailsArray = newTimeArray;
                    if (collect.timeDetailsMap.containsKey("AS_OF_YEAR")) {
                        collect.timeDetailsMap.remove("AS_OF_YEAR");
                    }
                    if (collect.timeDetailsMap.containsKey("AS_OF_YEAR1")) {
                        collect.timeDetailsMap.remove("AS_OF_YEAR1");
                    }
                    //////////////////////////////////////////////////////////////////////////.println(" newTimeArray "+newTimeArray);
                    LinkedHashMap hm = collect.timeDetailsMap;

                    ArrayList perioList = (ArrayList) collect.timeDetailsMap.get("PRG_PERIOD");
                    hm = new LinkedHashMap();
                    hm.put("AS_OF_QTR", stList);
                    hm.put("AS_OF_QTR1", endList);
                    hm.put("PRG_PERIOD", perioList);

                    collect.timeDetailsArray = newTimeArray;
                    collect.timeDetailsMap = hm;
                    //////////////////////////////////////////////////////////////////////////.println(collect.timeDetailsArray+" updated the timeArray "+collect.timeDetailsMap);
                    if (collect.targetIncomingParameters.containsKey("CBO_AS_OF_YEAR")) {
                        collect.targetIncomingParameters.remove("CBO_AS_OF_YEAR");
                    }
                    if (collect.targetIncomingParameters.containsKey("CBO_AS_OF_YEAR1")) {
                        collect.targetIncomingParameters.remove("CBO_AS_OF_YEAR1");
                    }
                    collect.targetIncomingParameters.put("CBO_AS_OF_QTR", stList.get(0));
                    collect.targetIncomingParameters.put("CBO_AS_OF_QTR1", endList.get(0));

                }

                targetParamSectionDisplay = disp.displayTimeParams(collect.timeDetailsMap, collect.timeDetailsArray, collect.targetIncomingParameters);

                targetParamSectionDisplay += disp.displayParams(collect.targetParameters, disp.tarId);

                targetParamSectionDisplay += disp.displayViewBys(collect.targetViewByMain, collect.targetParameters);



                String targetDataRegionDisplay = "";

                HashMap totalDetails = new HashMap();

                disp.RowEdgeQuery(collect.targetViewByMain);
                //////////////////////////.println(" collect.targetViewByMain --="+collect.targetViewByMain);
                //////////////////////////////////////////////////////////////////////////.println(" collect.timeDetailsArray-./. "+collect.timeDetailsArray);
                //////////////////////////////////////////////////////////////////////////.println(" basisValues- -=- = "+collect.basisValues);
                disp.ColumnEdgeQuery(collect.targetViewByMain, collect.timeDetailsArray);

                //////////////////////////////////////////////////////////////////////////.println(collect.drillUrl+" collect.columnEdgeDrillUrl "+collect.columnEdgeDrillUrl);
                totalDetails = disp.displayDataRegion(collect.targetViewByMain, collect.timeDetailsArray, collect.targetParametersValues, collect.drillUrl, collect.columnEdgeDrillUrl, collect.targetParameters, collect.basisValues);

                targetDataRegionDisplay = (String) totalDetails.get("displayDataRegion");
                ArrayList rowEdgeValues = new ArrayList();
                ArrayList columnEdgeValues = new ArrayList();
                HashMap originalResult = new HashMap();
                rowEdgeValues = (ArrayList) totalDetails.get("rowEdgeValues");
                columnEdgeValues = (ArrayList) totalDetails.get("colEdgeValues");
                originalResult = (HashMap) totalDetails.get("originalResult");
                String overAllMessage = (String) totalDetails.get("overAllMessage");
                String errorMessage = (String) totalDetails.get("errorMessage");
                String primaryAnalyze = (String) totalDetails.get("primaryAnalyze");
                String secAnalyze = (String) totalDetails.get("secAnalyze");
                String startRange = (String) totalDetails.get("startRange");
                String primParamEleId = (String) totalDetails.get("primParamEleId");
                String periodType = (String) totalDetails.get("periodType");
                String minTimeLev = (String) totalDetails.get("minTimeLevel");
                String endRange = (String) totalDetails.get("endRange");
                String startPeriod = (String) totalDetails.get("startPeriod"); //endPeriod
                String endPeriod = (String) totalDetails.get("endPeriod");
                String dateMeassage = (String) totalDetails.get("dateMeassage");
                String measureName = (String) totalDetails.get("measureName");
                String freezeButton = (String) totalDetails.get("freezeButton");
                String periodMsg = (String) totalDetails.get("periodMsg");
                String targetEndDate = (String) totalDetails.get("targetEndDate");
                String targetStartDate = (String) totalDetails.get("targetStartDate");
                HashMap RestrictingTotal = (HashMap) totalDetails.get("RestrictingTotal");

                ////////added on 15-02-10
                String dataQuery = (String) totalDetails.get("dataQuery");
                String dataQuery2 = (String) totalDetails.get("dataQuery2");
                String parentDataQ = (String) totalDetails.get("parentDataQ");
                String parentDataQ2 = (String) totalDetails.get("parentDataQ2");


                String colDrillUrl = (String) totalDetails.get("colDrillUrl");
                String basisVal = (String) totalDetails.get("basisVal");
                String maxTimeLevel = (String) totalDetails.get("maxTimeLevel");

                request.setAttribute("colDrillUrl", colDrillUrl);
                request.setAttribute("targetParamSectionDisplay", targetParamSectionDisplay);
                request.setAttribute("targetDataRegionDisplay", targetDataRegionDisplay);
                request.setAttribute("rowEdgeValues", rowEdgeValues);
                request.setAttribute("columnEdgeValues", columnEdgeValues);
                request.setAttribute("originalResult", originalResult);
                request.setAttribute("overAllMessage", overAllMessage);
                request.setAttribute("errorMessage", errorMessage);
                request.setAttribute("secAnalyze", secAnalyze);
                request.setAttribute("primaryAnalyze", primaryAnalyze);
                request.setAttribute("startRange", startRange);
                request.setAttribute("primParamEleId", primParamEleId);
                request.setAttribute("periodType", periodType);
                request.setAttribute("minTimeLevel", minTimeLev);
                request.setAttribute("endRange", endRange);
                request.setAttribute("endPeriod", endPeriod);
                request.setAttribute("startPeriod", startPeriod);
                request.setAttribute("dateMeassage", dateMeassage);
                request.setAttribute("measureName", measureName);
                request.setAttribute("freezeButton", freezeButton);
                request.setAttribute("periodMsg", periodMsg);
                request.setAttribute("targetStartDate", targetStartDate);
                request.setAttribute("targetEndDate", targetEndDate);
                request.setAttribute("basisVal", basisVal);
                request.setAttribute("RestrictingTotal", RestrictingTotal);
                request.setAttribute("maxTimeLevel", maxTimeLevel);

                ////////added on 15-02-10
                request.setAttribute("dataQuery", dataQuery);
                request.setAttribute("dataQuery2", dataQuery2);
                request.setAttribute("parentDataQ", parentDataQ);
                request.setAttribute("parentDataQ2", parentDataQ2);

                ArrayList defDates = new ArrayList();//disp.getDefaultTargetDatesList(pbTargetId,minTimeLevel);
                request.setAttribute("defDates", defDates);

                //request.setAttribute("rowEdgeValues",rowEdgeValues);
                //request.setAttribute("columnEdgeValues",columnEdgeValues);
                //added on 8th jan
                // HashMap viewByMap=disp.getDataViewByStatus(periodType,startRange);
                //request.setAttribute("viewByMap",viewByMap);

            } else {
                ////////////////////////////////////////////////////////////////////////////////////////////////////////.println("Session is null or session is expired in prepareReport of PbReportViewerBD");
            }
        } catch (Exception exp) {
            ////////////////////////////////////////////////////////////////////////////////////////////////////////.println("Exception in  prepareTarget of PbTargetViewerDb");
            logger.error("Exception:", exp);
        }
    }

    public void prepareTargetForView(String pbTargetId, String minTimeLevel, String pbUserId, HttpServletRequest request, HttpServletResponse response) {
//       HttpSession session = request.getSession(true);
        HttpSession session = request.getSession(false);
        DisplayTargetParameters disp = null;
        PbTargetCollection collect = null;
        PbTargetRequestParameter targetReqParams = null;

        String targetParamSectionDisplay = "";


        try {
            if (session != null) {

                LinkedHashMap timeDimMap = new LinkedHashMap();
                ArrayList dateArray = new ArrayList();
                ArrayList periodTypeArray = new ArrayList();



                session.setAttribute("TARGETID", pbTargetId);
                session.setAttribute("TIMELEVEL", minTimeLevel);
                session.setAttribute("userid", pbUserId);
                session.setAttribute("oldTargetId", pbTargetId);

                targetReqParams = new PbTargetRequestParameter(request);
                collect = new PbTargetCollection();
                disp = new DisplayTargetParameters();

                targetReqParams.setParametersHashMap();
                collect.targetId = pbTargetId;
                disp.tarId = pbTargetId;
                collect.minTimeLevel = minTimeLevel;
                collect.targetIncomingParameters = targetReqParams.requestParamValues;

                //collect.timeDetailsArray = timeDetails;

                collect.timeDetailsMap = timeDimMap;

                collect.getParamMetaData();
                //////////////////////////////////////////////////////////////////////////.println(" timeDimMap-.. "+collect.timeDetailsMap);
                //////////////////////////////////////////////////////////////////////////.println(" collect.targetIncomingParameters "+collect.targetIncomingParameters);
                //////////////////////////////////////////////////////////////////////////.println(" collect.timeDetailsArray "+collect.timeDetailsArray);

                //setCollectionObj(collect);
                String pType = collect.timeDetailsArray.get(4).toString();
                ArrayList newTimeArray = collect.timeDetailsArray;
                LinkedHashMap newTimeMap = collect.timeDetailsMap;
                if (pType.equalsIgnoreCase("Month")) {
                    newTimeArray = new ArrayList();
                    ArrayList al = collect.getSelectedMonths(pType, collect.timeDetailsArray.get(2).toString(), collect.timeDetailsArray.get(3).toString(), collect.targetIncomingParameters, minTimeLevel);

                    newTimeArray.add(0, collect.timeDetailsArray.get(4).toString());
                    newTimeArray.add("");
                    if (collect.targetIncomingParameters.containsKey("CBO_AS_OF_MONTH")) {
                        newTimeArray.add(collect.targetIncomingParameters.get("CBO_AS_OF_MONTH"));
                    } else {
                        newTimeArray.add(al.get(0));
                    }
                    if (collect.targetIncomingParameters.containsKey("CBO_AS_OF_MONTH")) {
                        newTimeArray.add(collect.targetIncomingParameters.get("CBO_AS_OF_MONTH1"));
                    } else {
                        newTimeArray.add(al.get(1));
                    }
                    newTimeArray.add(collect.timeDetailsArray.get(4).toString());

                    ArrayList stList = new ArrayList();
                    if (collect.targetIncomingParameters.containsKey("CBO_AS_OF_MONTH")) {
                        stList.add(collect.targetIncomingParameters.get("CBO_AS_OF_MONTH"));
                    } else {
                        stList.add(al.get(0).toString());
                    }
                    stList.add("CBO_AS_OF_MONTH");
                    stList.add("From Month");
                    stList.add("");
                    stList.add(collect.timeDetailsArray.get(4).toString());
                    if (collect.targetIncomingParameters.containsKey("CBO_AS_OF_MONTH")) {
                        stList.add(collect.targetIncomingParameters.get("CBO_AS_OF_MONTH"));
                    } else {
                        stList.add(al.get(0).toString());
                    }
                    stList.add("AS_OF_MONTH");
                    newTimeMap.put("AS_OF_MONTH", stList);

                    ArrayList endList = new ArrayList();
                    if (collect.targetIncomingParameters.containsKey("CBO_AS_OF_MONTH1")) {
                        endList.add(collect.targetIncomingParameters.get("CBO_AS_OF_MONTH1"));
                    } else {
                        endList.add(al.get(1).toString());
                    }
                    endList.add("CBO_AS_OF_MONTH1");
                    endList.add("To Month");
                    endList.add("");
                    endList.add(collect.timeDetailsArray.get(4).toString());
                    if (collect.targetIncomingParameters.containsKey("CBO_AS_OF_MONTH1")) {
                        endList.add(collect.targetIncomingParameters.get("CBO_AS_OF_MONTH1"));
                    } else {
                        endList.add(al.get(1).toString());
                    }
                    endList.add("AS_OF_MONTH1");
                    newTimeMap.put("AS_OF_MONTH1", endList);

                    collect.timeDetailsArray = newTimeArray;
                    if (collect.timeDetailsMap.containsKey("AS_OF_YEAR")) {
                        collect.timeDetailsMap.remove("AS_OF_YEAR");
                    }
                    if (collect.timeDetailsMap.containsKey("AS_OF_YEAR1")) {
                        collect.timeDetailsMap.remove("AS_OF_YEAR1");
                    }
                    //////////////////////////////////////////////////////////////////////////.println(" newTimeArray "+newTimeArray);
                    LinkedHashMap hm = collect.timeDetailsMap;

                    ArrayList perioList = (ArrayList) collect.timeDetailsMap.get("PRG_PERIOD");
                    hm = new LinkedHashMap();
                    hm.put("AS_OF_MONTH", stList);
                    hm.put("AS_OF_MONTH1", endList);
                    hm.put("PRG_PERIOD", perioList);

                    collect.timeDetailsArray = newTimeArray;
                    collect.timeDetailsMap = hm;
                    //////////////////////////////////////////////////////////////////////////.println(collect.timeDetailsArray+" updated fdf the timeArray "+collect.timeDetailsMap);
                } else if (pType.equalsIgnoreCase("Day")) {
                    newTimeArray = new ArrayList();
                    //////////////////////////////////////////////////////////////////////////.println(" in day ");
                    ArrayList al = collect.getSelectedMonths("Day", collect.timeDetailsArray.get(2).toString(), collect.timeDetailsArray.get(3).toString(), collect.targetIncomingParameters, minTimeLevel);

                    newTimeArray.add(0, collect.timeDetailsArray.get(4).toString());
                    newTimeArray.add("");
                    if (collect.targetIncomingParameters.containsKey("CBO_AS_OF_DATE")) {
                        newTimeArray.add(collect.targetIncomingParameters.get("CBO_AS_OF_DATE"));
                    } else {
                        newTimeArray.add(al.get(0));
                    }
                    if (collect.targetIncomingParameters.containsKey("CBO_AS_OF_DATE")) {
                        newTimeArray.add(collect.targetIncomingParameters.get("CBO_AS_OF_DATE1"));
                    } else {
                        newTimeArray.add(al.get(1));
                    }
                    newTimeArray.add(collect.timeDetailsArray.get(4).toString());

                    ArrayList stList = new ArrayList();
                    if (collect.targetIncomingParameters.containsKey("CBO_AS_OF_DATE")) {
                        stList.add(collect.targetIncomingParameters.get("CBO_AS_OF_DATE"));
                    } else {
                        stList.add(al.get(0).toString());
                    }
                    stList.add("CBO_AS_OF_DATE");
                    stList.add("From Day");
                    stList.add("");
                    stList.add(collect.timeDetailsArray.get(4).toString());
                    if (collect.targetIncomingParameters.containsKey("CBO_AS_OF_DATE")) {
                        stList.add(collect.targetIncomingParameters.get("CBO_AS_OF_DATE"));
                    } else {
                        stList.add(al.get(0).toString());
                    }
                    stList.add("AS_OF_DATE");
                    newTimeMap.put("AS_OF_DATE", stList);

                    ArrayList endList = new ArrayList();
                    if (collect.targetIncomingParameters.containsKey("CBO_AS_OF_DATE1")) {
                        endList.add(collect.targetIncomingParameters.get("CBO_AS_OF_DATE1"));
                    } else {
                        endList.add(al.get(1).toString());
                    }
                    endList.add("CBO_AS_OF_DATE1");
                    endList.add("To Day");
                    endList.add("");
                    endList.add(collect.timeDetailsArray.get(4).toString());
                    if (collect.targetIncomingParameters.containsKey("CBO_AS_OF_DATE1")) {
                        endList.add(collect.targetIncomingParameters.get("CBO_AS_OF_DATE1"));
                    } else {
                        endList.add(al.get(1).toString());
                    }
                    endList.add("AS_OF_DATE1");
                    newTimeMap.put("AS_OF_DATE1", endList);

                    collect.timeDetailsArray = newTimeArray;
                    if (collect.timeDetailsMap.containsKey("AS_OF_MONTH")) {
                        collect.timeDetailsMap.remove("AS_OF_MONTH");
                    }
                    if (collect.timeDetailsMap.containsKey("AS_OF_MONTH1")) {
                        collect.timeDetailsMap.remove("AS_OF_MONTH1");
                    }
                    //////////////////////////////////////////////////////////////////////////.println(" newTimeArray "+newTimeArray);
                    LinkedHashMap hm = collect.timeDetailsMap;

                    ArrayList perioList = (ArrayList) collect.timeDetailsMap.get("PRG_PERIOD");
                    hm = new LinkedHashMap();
                    hm.put("AS_OF_DATE", stList);
                    hm.put("AS_OF_DATE1", endList);
                    hm.put("PRG_PERIOD", perioList);

                    collect.timeDetailsArray = newTimeArray;
                    collect.timeDetailsMap = hm;
                    //////////////////////////////////////////////////////////////////////////.println(collect.timeDetailsArray+" updated 79789 day the timeArray "+collect.timeDetailsMap);
                } else if (pType.equalsIgnoreCase("Qtr")) {
                    //////////////////////////////////////////////////////////////////////////.println(" in qtr ");
                    newTimeArray = new ArrayList();
                    ArrayList al = collect.getSelectedMonths(pType, collect.timeDetailsArray.get(2).toString(), collect.timeDetailsArray.get(3).toString(), collect.targetIncomingParameters, minTimeLevel);

                    newTimeArray.add(0, collect.timeDetailsArray.get(4).toString());
                    newTimeArray.add("");
                    if (collect.targetIncomingParameters.containsKey("CBO_AS_OF_QTR")) {
                        newTimeArray.add(collect.targetIncomingParameters.get("CBO_AS_OF_QTR"));
                    } else {
                        newTimeArray.add(al.get(0));
                    }
                    if (collect.targetIncomingParameters.containsKey("CBO_AS_OF_QTR")) {
                        newTimeArray.add(collect.targetIncomingParameters.get("CBO_AS_OF_QTR1"));
                    } else {
                        newTimeArray.add(al.get(1));
                    }
                    newTimeArray.add(collect.timeDetailsArray.get(4).toString());

                    ArrayList stList = new ArrayList();
                    if (collect.targetIncomingParameters.containsKey("CBO_AS_OF_QTR")) {
                        stList.add(collect.targetIncomingParameters.get("CBO_AS_OF_QTR"));
                    } else {
                        stList.add(al.get(0).toString());
                    }
                    stList.add("CBO_AS_OF_QTR");
                    stList.add("From Quarter");
                    stList.add("");
                    stList.add(collect.timeDetailsArray.get(4).toString());
                    if (collect.targetIncomingParameters.containsKey("CBO_AS_OF_QTR")) {
                        stList.add(collect.targetIncomingParameters.get("CBO_AS_OF_QTR"));
                    } else {
                        stList.add(al.get(0).toString());
                    }
                    stList.add("AS_OF_QTR");
                    newTimeMap.put("AS_OF_QTR", stList);

                    ArrayList endList = new ArrayList();
                    if (collect.targetIncomingParameters.containsKey("CBO_AS_OF_QTR1")) {
                        endList.add(collect.targetIncomingParameters.get("CBO_AS_OF_QTR1"));
                    } else {
                        endList.add(al.get(1).toString());
                    }
                    endList.add("CBO_AS_OF_QTR1");
                    endList.add("To Quarter");
                    endList.add("");
                    endList.add(collect.timeDetailsArray.get(4).toString());
                    if (collect.targetIncomingParameters.containsKey("CBO_AS_OF_QTR1")) {
                        endList.add(collect.targetIncomingParameters.get("CBO_AS_OF_QTR1"));
                    } else {
                        endList.add(al.get(1).toString());
                    }
                    endList.add("AS_OF_QTR1");
                    newTimeMap.put("AS_OF_QTR1", endList);

                    collect.timeDetailsArray = newTimeArray;
                    if (collect.timeDetailsMap.containsKey("AS_OF_YEAR")) {
                        collect.timeDetailsMap.remove("AS_OF_YEAR");
                    }
                    if (collect.timeDetailsMap.containsKey("AS_OF_YEAR1")) {
                        collect.timeDetailsMap.remove("AS_OF_YEAR1");
                    }
                    //////////////////////////////////////////////////////////////////////////.println(" newTimeArray "+newTimeArray);
                    LinkedHashMap hm = collect.timeDetailsMap;

                    ArrayList perioList = (ArrayList) collect.timeDetailsMap.get("PRG_PERIOD");
                    hm = new LinkedHashMap();
                    hm.put("AS_OF_QTR", stList);
                    hm.put("AS_OF_QTR1", endList);
                    hm.put("PRG_PERIOD", perioList);

                    collect.timeDetailsArray = newTimeArray;
                    collect.timeDetailsMap = hm;
                    //////////////////////////////////////////////////////////////////////////.println(collect.timeDetailsArray+" updated the timeArray "+collect.timeDetailsMap);
                    if (collect.targetIncomingParameters.containsKey("CBO_AS_OF_YEAR")) {
                        collect.targetIncomingParameters.remove("CBO_AS_OF_YEAR");
                    }
                    if (collect.targetIncomingParameters.containsKey("CBO_AS_OF_YEAR1")) {
                        collect.targetIncomingParameters.remove("CBO_AS_OF_YEAR1");
                    }
                    collect.targetIncomingParameters.put("CBO_AS_OF_QTR", stList.get(0));
                    collect.targetIncomingParameters.put("CBO_AS_OF_QTR1", endList.get(0));

                }

                targetParamSectionDisplay = disp.displayTimeParams(collect.timeDetailsMap, collect.timeDetailsArray, collect.targetIncomingParameters);

                targetParamSectionDisplay += disp.displayParams(collect.targetParameters, disp.tarId);

                targetParamSectionDisplay += disp.displayViewBys(collect.targetViewByMain, collect.targetParameters);



                String targetDataRegionDisplay = "";

                HashMap totalDetails = new HashMap();

                disp.RowEdgeQuery(collect.targetViewByMain);
                //////////////////////////////////////////////////////////////////////////.println(" collect.timeDetailsArray-./. "+collect.timeDetailsArray);
                //////////////////////////////////////////////////////////////////////////.println(" basisValues- -=- = "+collect.basisValues);
                disp.ColumnEdgeQuery(collect.targetViewByMain, collect.timeDetailsArray);

                //////////////////////////////////////////////////////////////////////////.println(collect.drillUrl+" collect.columnEdgeDrillUrl "+collect.columnEdgeDrillUrl);
                totalDetails = disp.displayDataRegionForView(collect.targetViewByMain, collect.timeDetailsArray, collect.targetParametersValues, collect.drillUrl, collect.columnEdgeDrillUrl, collect.targetParameters, collect.basisValues);

                targetDataRegionDisplay = (String) totalDetails.get("displayDataRegion");
                ArrayList rowEdgeValues = new ArrayList();
                ArrayList columnEdgeValues = new ArrayList();
                HashMap originalResult = new HashMap();
                rowEdgeValues = (ArrayList) totalDetails.get("rowEdgeValues");
                columnEdgeValues = (ArrayList) totalDetails.get("colEdgeValues");
                originalResult = (HashMap) totalDetails.get("originalResult");
                String overAllMessage = (String) totalDetails.get("overAllMessage");
                String errorMessage = (String) totalDetails.get("errorMessage");
                String primaryAnalyze = (String) totalDetails.get("primaryAnalyze");
                String secAnalyze = (String) totalDetails.get("secAnalyze");
                String startRange = (String) totalDetails.get("startRange");
                String primParamEleId = (String) totalDetails.get("primParamEleId");
                String periodType = (String) totalDetails.get("periodType");
                String minTimeLev = (String) totalDetails.get("minTimeLevel");
                String endRange = (String) totalDetails.get("endRange");
                String startPeriod = (String) totalDetails.get("startPeriod"); //endPeriod
                String endPeriod = (String) totalDetails.get("endPeriod");
                String dateMeassage = (String) totalDetails.get("dateMeassage");
                String measureName = (String) totalDetails.get("measureName");
                String freezeButton = (String) totalDetails.get("freezeButton");
                String periodMsg = (String) totalDetails.get("periodMsg");
                String targetEndDate = (String) totalDetails.get("targetEndDate");
                String targetStartDate = (String) totalDetails.get("targetStartDate");
                HashMap RestrictingTotal = (HashMap) totalDetails.get("RestrictingTotal");

                String colDrillUrl = (String) totalDetails.get("colDrillUrl");
                String basisVal = (String) totalDetails.get("basisVal");
                String maxTimeLevel = (String) totalDetails.get("maxTimeLevel");

                request.setAttribute("colDrillUrl", colDrillUrl);
                request.setAttribute("targetParamSectionDisplay", targetParamSectionDisplay);
                request.setAttribute("targetDataRegionDisplay", targetDataRegionDisplay);
                request.setAttribute("rowEdgeValues", rowEdgeValues);
                request.setAttribute("columnEdgeValues", columnEdgeValues);
                request.setAttribute("originalResult", originalResult);
                request.setAttribute("overAllMessage", overAllMessage);
                request.setAttribute("errorMessage", errorMessage);
                request.setAttribute("secAnalyze", secAnalyze);
                request.setAttribute("primaryAnalyze", primaryAnalyze);
                request.setAttribute("startRange", startRange);
                request.setAttribute("primParamEleId", primParamEleId);
                request.setAttribute("periodType", periodType);
                request.setAttribute("minTimeLevel", minTimeLev);
                request.setAttribute("endRange", endRange);
                request.setAttribute("endPeriod", endPeriod);
                request.setAttribute("startPeriod", startPeriod);
                request.setAttribute("dateMeassage", dateMeassage);
                request.setAttribute("measureName", measureName);
                request.setAttribute("freezeButton", freezeButton);
                request.setAttribute("periodMsg", periodMsg);
                request.setAttribute("targetStartDate", targetStartDate);
                request.setAttribute("targetEndDate", targetEndDate);
                request.setAttribute("basisVal", basisVal);
                request.setAttribute("RestrictingTotal", RestrictingTotal);
                request.setAttribute("maxTimeLevel", maxTimeLevel);

                ArrayList defDates = new ArrayList();//disp.getDefaultTargetDatesList(pbTargetId,minTimeLevel);
                request.setAttribute("defDates", defDates);

                //added on 8th jan
                HashMap viewByMap = disp.getDataViewByStatus(periodType, startRange);
                request.setAttribute("viewByMap", viewByMap);

                //request.setAttribute("rowEdgeValues",rowEdgeValues);
                //request.setAttribute("columnEdgeValues",columnEdgeValues);

            } else {
                ////////////////////////////////////////////////////////////////////////////////////////////////////////.println("Session is null or session is expired in prepareReport of PbReportViewerBD");
            }
        } catch (Exception exp) {
            ////////////////////////////////////////////////////////////////////////////////////////////////////////.println("Exception in  prepareTarget of PbTargetViewerDb");
            logger.error("Exception:", exp);
        }
    }

    /**
     * @return the collectionObj
     */
    public Object getCollectionObj() {
        return collectionObj;
    }

    /**
     * @param collectionObj the collectionObj to set
     */
    public void setCollectionObj(Object collectionObj) {
        this.collectionObj = collectionObj;
    }
}
