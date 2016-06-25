package com.progen.scenariodesigner.db;

import com.progen.report.query.PbReportQuery;
import com.progen.scenario.display.DisplayScenarioParameters;
import com.progen.scenarion.PbScenarioCollection;
import com.progen.scenarion.PbScenarioRequestParameters;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import oracle.jdbc.OracleCallableStatement;
import org.apache.log4j.Logger;
import prg.db.Container;
import prg.db.PbDb;
import prg.db.PbReturnObject;
import utils.db.ProgenConnection;

public class PbScenarioViewerBD {

    public static Logger logger = Logger.getLogger(PbScenarioViewerBD.class);
    public String newCustomModelId = "";
    public String flag = "";
    private String ParameterBpName;
    private String anlyName;
    private String SecondaryViewByText;

    public String getSecondaryViewByText() {
        return SecondaryViewByText;
    }

    public void setSecondaryViewByText(String SecondaryViewByText) {
        this.SecondaryViewByText = SecondaryViewByText;
    }

    public void setParameterBpName(String parameterName) {
        this.ParameterBpName = parameterName;
    }

    /**
     * @return the scenarioStatus
     */
    public String getParameterBpName() {
        return ParameterBpName;
    }

    public void setParameteranlyName(String anlyName) {
        this.anlyName = anlyName;
    }

    /**
     * @return the scenarioStatus
     */
    public String getParameteranlyName() {
        return anlyName;
    }

    public void prepareScenario(String pbScenarioId, String scenarioName, String minTimeLevel, String pbUserId, String flag, HttpServletRequest request, HttpServletResponse response) {
//        HttpSession session = request.getSession(true);
        HttpSession session = request.getSession(false);
        DisplayScenarioParameters disp = null;
        PbScenarioCollection collect = null;
        PbScenarioRequestParameters scnReqParams = null;
        PbReportQuery repQuery = null;

        String scenarioParamSectionDisplay = "";
        HashMap map = new HashMap();
        Container container = null;
        HashMap ParametersHashMap = null;
        HashMap TableHashMap = null;
        HashMap TimeRangeMap = null;
        ////
        String queryBd = "";
        PbDb pbdb = new PbDb();
        HashMap expGrowthHm = new HashMap();
        HashMap expFinalGrowthHm = new HashMap();
        HashMap existexpGrowthHm = new HashMap();
        try {
            if (session != null) {
                if (session.getAttribute("SCENARIOTAB") != null) {
                    map = (HashMap) session.getAttribute("SCENARIOTAB");
                } else {
                    map = new HashMap();
                }
                if (map.get(scenarioName) != null) {
                    container = (Container) map.get(scenarioName);
                } else {
                    container = new Container();
                }

                container.setScenarioId(pbScenarioId);
                container.setScenarioName(scenarioName);

                //  //

                //  //

                ParametersHashMap = container.getParametersHashMap();
                TableHashMap = container.getTableHashMap();
                TimeRangeMap = container.getScenarioTimeRangeMap();

                if (ParametersHashMap == null) {
                    ParametersHashMap = new HashMap();
                }
                if (TableHashMap == null) {
                    TableHashMap = new HashMap();
                }
                if (TimeRangeMap == null) {
                    TimeRangeMap = new HashMap();
                }

//                //
//                //
//                //

                ////////////////// 
                session.setAttribute("scenarioId", pbScenarioId);
                session.setAttribute("TIMELEVEL", minTimeLevel);
                session.setAttribute("userid", pbUserId);
                session.setAttribute("OLDSCENARIOID", pbScenarioId);
                /////////////////

                scnReqParams = new PbScenarioRequestParameters(request);
                collect = new PbScenarioCollection();
                disp = new DisplayScenarioParameters();
                repQuery = new PbReportQuery();

                scnReqParams.setScenarioParametersHashMap();

                collect.scenarioId = pbScenarioId;
                collect.userId = pbUserId;
                collect.scenarioIncomingParameters = scnReqParams.requestParamValues;
                collect.ctxPath = request.getContextPath();
                collect.newCustomModelId = this.newCustomModelId;
                collect.flag = this.flag;
                //collect.timeDetailsArray = (ArrayList) ParametersHashMap.get("TimeRangeDetails");

                collect.getParamMetaData();

                disp.ScenarioId = pbScenarioId;
                scenarioParamSectionDisplay = disp.displayTimeParams(collect.timeDetailsMap, collect.timeDetailsArray, flag);
                scenarioParamSectionDisplay += disp.displayParams(collect.scenarioParameters, disp.ScenarioId);
                scenarioParamSectionDisplay += disp.displayViewBys(collect.scenarioViewByMain, collect.scenarioParameters, collect.scenarioModels, flag);

                request.setAttribute("scenarioParamSectionDisplay", scenarioParamSectionDisplay);
                request.setAttribute("completeURL", collect.completeUrl);

                disp.RowEdgeQuery(collect.scenarioViewByMain);
                disp.ColumnEdgeQuery(collect.scenarioViewByMain, collect.timeDetailsArray);
                TableHashMap = collect.scenarioTableHashMap;

                // repQuery.
                repQuery.setRowViewbyCols(collect.scenarioRowViewbyValues);
                //////////////////////////////////////
                repQuery.setColViewbyCols(collect.scenarioColViewbyValues);

                ArrayList reportQryElementIds = new ArrayList();
                reportQryElementIds = (ArrayList) TableHashMap.get("Measures");
                ArrayList reportQryAggregations = new ArrayList();
                reportQryAggregations.add("SUM");
                ArrayList timeA = new ArrayList();
                // timeA=(ArrayList)collect.timeDetailsMap.get("PRG_PERIOD_TYPE");
                ScenarioTemplateDAO tempDAO = new ScenarioTemplateDAO();
                String lastDate = tempDAO.getMonthLastDate((String) collect.timeDetailsArray.get(1));
                //////////////////////////////////////
                // //
//                timeA.add(0, "Day");
//                timeA.add(1, "PRG_STD");
//                timeA.add(2, lastDate);
//                timeA.add(3, "Month");
//                timeA.add(4, "Year");

                timeA = new ArrayList();
                timeA.add(0, "Day");
                timeA.add(1, "PRG_YEAR_RANGE");
                timeA.add(2, collect.timeDetailsArray.get(0).toString());
                timeA.add(3, collect.timeDetailsArray.get(1).toString());
                timeA.add(4, collect.timeDetailsArray.get(2).toString());
                timeA.add(5, collect.timeDetailsArray.get(3).toString());

                // //
                repQuery.setTimeDetails(timeA);
                ArrayList qryCols = new ArrayList();
                qryCols.add(reportQryElementIds.get(0).toString());
                repQuery.setQryColumns(qryCols);
                repQuery.setColAggration(reportQryAggregations);
                repQuery.setDefaultMeasure(reportQryElementIds.get(0).toString());
                repQuery.setDefaultMeasureSumm(reportQryAggregations.get(0).toString());
                repQuery.setParamValue(collect.scenarioParametersValues);

                ///////////////////////////////////
                PbReturnObject pbro2 = repQuery.getPbReturnObject((String) collect.scenarioRowViewbyValues.get(0));
//                //
                HashMap colViewMap = new HashMap();
                ArrayList colNames = new ArrayList();
                colViewMap = repQuery.crossTabNonViewByMap;
                colNames = repQuery.crossTabNonViewBy;
                //  //
                //  //
                HashMap all = new HashMap();
                ArrayList graphArray = new ArrayList();
                String tableDisplay = "";
                String expGrowth = "";
                String modelName = "";
                String newrowId = "";
                String expRowGrowth = "";
                ArrayList expGrowthList = new ArrayList();
                ArrayList GrowthapplyList = new ArrayList();
                String GrowthApply = "";
                String BPexpGrowth = "";

                expGrowth = String.valueOf(session.getAttribute("expGrowth"));
                //
                modelName = String.valueOf(session.getAttribute("modelName"));
                newrowId = String.valueOf(session.getAttribute("newrowId"));
                expGrowthList = (ArrayList) (session.getAttribute("expGrowthList"));
                GrowthapplyList = (ArrayList) (session.getAttribute("GrowthapplyList"));
                GrowthApply = String.valueOf(session.getAttribute("GrowthapplyList"));
                GrowthApply = GrowthApply.replace("[", "").replace("]", "");
                //
                //
//                //////.println("GrowthApply in viewerbd is : " + GrowthApply);
                if (modelName.equalsIgnoreCase("Budgeting")) {
                    try {
                        Connection con = ProgenConnection.getInstance().getConnection();
                        OracleCallableStatement cstmt = (OracleCallableStatement) con.prepareCall("begin create_scenario_data(:1); end;");
                        cstmt.setInt(1, Integer.parseInt(pbScenarioId));
                        cstmt.execute();
                        cstmt.close();
                        con.close();
                    } catch (SQLException e) {
                        logger.error("Exception:", e);
                    }


                    if (anlyName != null) {

                        if (anlyName.equalsIgnoreCase("BP")) {
                            queryBd = "select round(avg(exp_growth),2) EXP_GROWTH,round(avg(final_growth),2) FINAL_GROWTH, bpname from MACL_SALES_BUDGET_TARGET  where SCENARIO_ID =" + pbScenarioId + " group by bpname";
                            //queryBd = "select avg (exp_growth) ,bpname,final_growth from MACL_SALES_BUDGET_TARGET  where SCENARIO_ID ="+pbScenarioId+" group by bpname,final_growth order by bpname";
                            //
                            PbReturnObject returnObject = new PbReturnObject();
                            Connection connection = ProgenConnection.getInstance().getConnection();
                            returnObject = pbdb.execSelectSQL(queryBd, connection);
                            connection.close();
                            for (int count = 0; count < returnObject.getRowCount(); count++) {
                                if (session.getAttribute("expGrowth") != null) {
                                    //
                                    BPexpGrowth = expGrowth;
                                    session.setAttribute("BPexpGrowth", BPexpGrowth);
                                    if (GrowthApply.equalsIgnoreCase("applynotonegative")) {
                                        expGrowthHm.put(returnObject.getFieldValue(count, "BPNAME"), expGrowth);
                                        existexpGrowthHm.put(returnObject.getFieldValue(count, "BPNAME"), returnObject.getFieldValue(count, "EXP_GROWTH"));
                                    } else {
                                        expGrowthHm.put(returnObject.getFieldValue(count, "BPNAME"), expGrowth);
                                    }

//                                 existexpGrowthHm.put(returnObject.getFieldValue(count,"BPNAME"), returnObject.getFieldValue(count,"EXP_GROWTH"));
                                } else {
                                    //
                                    expGrowthHm.put(returnObject.getFieldValue(count, "BPNAME"), returnObject.getFieldValue(count, "EXP_GROWTH"));
                                    expFinalGrowthHm.put(returnObject.getFieldValue(count, "BPNAME"), returnObject.getFieldValue(count, "FINAL_GROWTH"));
                                }


                            }
                        } else if (anlyName.equalsIgnoreCase("Item")) {
                            queryBd = "select  item,exp_growth,final_growth  from MACL_SALES_BUDGET_TARGET where scenario_id=" + pbScenarioId;
                            //
                            PbReturnObject returnObject = new PbReturnObject();
                            Connection connection = ProgenConnection.getInstance().getConnection();
                            returnObject = pbdb.execSelectSQL(queryBd, connection);
                            connection.close();
                            for (int count = 0; count < returnObject.getRowCount(); count++) {

                                if (session.getAttribute("expGrowth") != null) {
                                    if (GrowthApply.equalsIgnoreCase("applynotonegative")) {
                                        expGrowthHm.put(returnObject.getFieldValue(count, "ITEM"), expGrowth);
                                        existexpGrowthHm.put(returnObject.getFieldValue(count, "ITEM"), session.getAttribute("BPexpGrowth"));
                                    } else {
                                        expGrowthHm.put(returnObject.getFieldValue(count, "ITEM"), expGrowth);
                                    }

//                                 existexpGrowthHm.put(returnObject.getFieldValue(count,"ITEM"), returnObject.getFieldValue(count,"EXP_GROWTH"));
                                } else {

                                    expGrowthHm.put(returnObject.getFieldValue(count, "ITEM"), returnObject.getFieldValue(count, "EXP_GROWTH"));
                                    expFinalGrowthHm.put(returnObject.getFieldValue(count, "ITEM"), returnObject.getFieldValue(count, "FINAL_GROWTH"));
                                }
                                //expGrowthHm.put(returnObject.getFieldValue(count, "ITEM"), returnObject.getFieldValue(count, "EXP_GROWTH"));

                            }
                        }
                        // session.removeAttribute("expGrowth");
                    }
                }




                if (SecondaryViewByText == null || !SecondaryViewByText.equalsIgnoreCase("Budgeting")) {
                    all = disp.getScenarioTable(repQuery.crossTabNonViewBy, pbro2, pbScenarioId, collect.timeDetailsArray, collect.scenarioRowViewbyValues.get(0).toString(), repQuery.crossTabNonViewByMap, collect.scenarioViewByMain, collect.timeDetailsMap, collect.scenarioParameters, collect.drillUrl);
                } else {

                    if (modelName.equalsIgnoreCase("Budgeting")) {


                        newrowId = "";
                        expGrowthList = new ArrayList();

                    }
                    expRowGrowth = String.valueOf(session.getAttribute("expRowGrowth"));
                    expGrowthList = (ArrayList) (session.getAttribute("expGrowthList"));
                    GrowthapplyList = (ArrayList) (session.getAttribute("GrowthapplyList"));

                    all = disp.getBudgetScenarioTable(repQuery.crossTabNonViewBy, pbro2, pbScenarioId, collect.timeDetailsArray, collect.scenarioRowViewbyValues.get(0).toString(), repQuery.crossTabNonViewByMap, collect.scenarioViewByMain, collect.timeDetailsMap, collect.scenarioParameters, collect.drillUrl, expGrowth, modelName, newrowId, expRowGrowth, expGrowthList, GrowthapplyList, expGrowthHm, anlyName, expFinalGrowthHm, existexpGrowthHm);


                }

                tableDisplay = (String) all.get("result");
                graphArray = (ArrayList) all.get("graphArray");
                String viewByName = (String) all.get("viewByName");
                String modelId = (String) all.get("modelId");
                ArrayList histMonths = (ArrayList) all.get("histMonths");
                String dimensionId = (String) all.get("dimensionId");
                request.setAttribute("viewByName", viewByName);
                request.setAttribute("modelId", modelId);
                request.setAttribute("histMonths", histMonths);
                request.setAttribute("dimensionId", dimensionId);
                request.setAttribute("timeDetailsArray", collect.timeDetailsArray);
                request.setAttribute("crossTabNonViewByMap", repQuery.crossTabNonViewByMap);
                request.setAttribute("crossTabNonViewBy", repQuery.crossTabNonViewBy);
                request.setAttribute("crossTabRetObj", pbro2);


//                //
//                //


                request.setAttribute("scenarioTableScetionDisplay", tableDisplay);
                request.setAttribute("scenarioCombineTableScetionDisplay", request.getAttribute("scenarioCombineTableScetionDisplay"));
                // //
                request.setAttribute("graphArray", graphArray);



            } else {
                ////////////////////////////////////////////////////////////////////
            }
        } catch (Exception exp) {
            logger.error("Exception:", exp);
        }
    }

    public void compareScenario(String pbScenarioId, String scenarioName, String dimNamesStr, String modelNamesWithQuotes, String timeLevel, String scnMonths, String pbUserId, String flag, HttpServletRequest request, HttpServletResponse response) {
//        HttpSession session = request.getSession(true);
        HttpSession session = request.getSession(false);
        DisplayScenarioParameters disp = null;
        PbScenarioCollection collect = null;
        PbScenarioRequestParameters scnReqParams = null;
        PbReportQuery repQuery = null;

        String scenarioParamSectionDisplay = "";
        HashMap map = new HashMap();
        Container container = null;
        HashMap ParametersHashMap = null;
        HashMap TableHashMap = null;
        HashMap TimeRangeMap = null;

        try {
            if (session != null) {
                if (session.getAttribute("SCENARIOTAB") != null) {
                    map = (HashMap) session.getAttribute("SCENARIOTAB");
                } else {
                    map = new HashMap();
                }
                if (map.get(scenarioName) != null) {
                    container = (Container) map.get(scenarioName);
                } else {
                    container = new Container();
                }

                container.setScenarioId(pbScenarioId);
                container.setScenarioName(scenarioName);

                //////////////////////////////////////
                //////////////////////////////////////

                ParametersHashMap = container.getParametersHashMap();
                TableHashMap = container.getTableHashMap();
                TimeRangeMap = container.getScenarioTimeRangeMap();

                if (ParametersHashMap == null) {
                    ParametersHashMap = new HashMap();
                }
                if (TableHashMap == null) {
                    TableHashMap = new HashMap();
                }
                if (TimeRangeMap == null) {
                    TimeRangeMap = new HashMap();
                }



                scnReqParams = new PbScenarioRequestParameters(request);
                collect = new PbScenarioCollection();
                disp = new DisplayScenarioParameters();
                repQuery = new PbReportQuery();

                scnReqParams.setScenarioParametersHashMap();

                collect.scenarioId = pbScenarioId;
                collect.userId = pbUserId;
                collect.scenarioIncomingParameters = scnReqParams.requestParamValues;
                collect.ctxPath = request.getContextPath();
                collect.flag = this.flag;
                collect.scenarioName = scenarioName;
                collect.dimensionName = dimNamesStr;
                collect.modelNamesStr = modelNamesWithQuotes;
                collect.minTimeLevel = timeLevel;
                collect.scnMonths = scnMonths;
                //collect.timeDetailsArray = (ArrayList) ParametersHashMap.get("TimeRangeDetails");

                collect.getParamMetaData2();


                disp.ScenarioId = pbScenarioId;
                scenarioParamSectionDisplay = disp.displayTimeParams(collect.timeDetailsMap, collect.timeDetailsArray, flag);
                scenarioParamSectionDisplay += disp.displayParams(collect.scenarioParameters, disp.ScenarioId);
                scenarioParamSectionDisplay += disp.displayViewBys(collect.scenarioViewByMain, collect.scenarioParameters, collect.scenarioModels, flag);

                request.setAttribute("scenarioParamSectionDisplay", scenarioParamSectionDisplay);
                request.setAttribute("completeURL", collect.completeUrl);

                disp.RowEdgeQuery(collect.scenarioViewByMain);
                disp.ColumnEdgeQuery(collect.scenarioViewByMain, collect.timeDetailsArray);
                TableHashMap = collect.scenarioTableHashMap;
                //////////////////////////////////////
                //////////////////////////////////////

                /////////////////////////////////

                // repQuery.
                repQuery.setRowViewbyCols(collect.scenarioRowViewbyValues);
                //////////////////////////////////////
                repQuery.setColViewbyCols(collect.scenarioColViewbyValues);

                ArrayList reportQryElementIds = new ArrayList();
                reportQryElementIds = (ArrayList) TableHashMap.get("Measures");
                // //
                ArrayList reportQryAggregations = new ArrayList();
                reportQryAggregations.add("SUM");
                ArrayList timeA = new ArrayList();
                // timeA=(ArrayList)collect.timeDetailsMap.get("PRG_PERIOD_TYPE");
                ScenarioTemplateDAO tempDAO = new ScenarioTemplateDAO();
                String lastDate = tempDAO.getMonthLastDate((String) collect.timeDetailsArray.get(1));
                //////////////////////////////////////
//                timeA.add(0, "Day");
//                timeA.add(1, "PRG_STD");
//                timeA.add(2, lastDate);
//                timeA.add(3, "Month");
//                timeA.add(4, "Year");
                timeA.add(0, "Day");
                timeA.add(1, "PRG_YEAR_RANGE");
                timeA.add(2, collect.timeDetailsArray.get(0).toString());
                timeA.add(3, collect.timeDetailsArray.get(1).toString());
                timeA.add(4, collect.timeDetailsArray.get(2).toString());
                timeA.add(5, collect.timeDetailsArray.get(3).toString());

                //////////////////////////////////////
                repQuery.setTimeDetails(timeA);
                ArrayList qryCols = new ArrayList();
                qryCols.add(reportQryElementIds.get(0).toString());
                repQuery.setQryColumns(qryCols);
                repQuery.setColAggration(reportQryAggregations);
                repQuery.setDefaultMeasure(reportQryElementIds.get(0).toString());
                repQuery.setDefaultMeasureSumm(reportQryAggregations.get(0).toString());
                repQuery.setParamValue(collect.scenarioParametersValues);

                ///////////////////////////////////
                PbReturnObject pbro2 = repQuery.getPbReturnObject((String) collect.scenarioRowViewbyValues.get(0));
                HashMap colViewMap = new HashMap();
                ArrayList colNames = new ArrayList();
                colViewMap = repQuery.crossTabNonViewByMap;
                colNames = repQuery.crossTabNonViewBy;
                HashMap all = new HashMap();
                ArrayList graphArray = new ArrayList();
                String tableDisplay = "";

                //////////////////////////////////////
                // //
//                pbro2.writeString();


                disp.viewerCollect = collect;
                all = disp.getCompareScenarioTable(colNames, pbro2, pbScenarioId, collect.timeDetailsArray, collect.scenarioRowViewbyValues.get(0).toString(), repQuery.crossTabNonViewByMap, collect.scenarioViewByMain, collect.scenarioModels, collect.scenarioParameters, collect.drillUrl);
                // //
                /*
                 * tableDisplay = (String) all.get("result"); graphArray =
                 * (ArrayList) all.get("graphArray"); String viewByName =
                 * (String) all.get("viewByName"); String modelId = (String)
                 * all.get("modelId"); ArrayList histMonths = (ArrayList)
                 * all.get("histMonths"); String dimensionId = (String)
                 * all.get("dimensionId"); request.setAttribute("viewByName",
                 * viewByName); request.setAttribute("modelId", modelId);
                 * request.setAttribute("histMonths", histMonths);
                 * request.setAttribute("dimensionId", dimensionId);
                 * request.setAttribute("timeDetailsArray",
                 * collect.timeDetailsArray);
                 *
                 *
                 *
                 * request.setAttribute("scenarioTableScetionDisplay",
                 * tableDisplay); request.setAttribute("graphArray",
                 * graphArray);
                 */
                String compareTableResult = (String) all.get("compareTableResult");
                //  //
                request.setAttribute("compareTableResult", compareTableResult);


            } else {
                ////////////////////////////////////////////////////////////////////
            }
        } catch (Exception exp) {
            logger.error("Exception:", exp);
        }
    }

    public PbReturnObject getNormalRetObj(String msrs, String scnrId, String scenarioName, String pbUserId, HttpServletRequest request) {
//        HttpSession session = request.getSession(true);
        HttpSession session = request.getSession(false);
        DisplayScenarioParameters disp = null;
        PbScenarioCollection collect = null;
        PbScenarioRequestParameters scnReqParams = null;
        PbReportQuery repQuery = null;
        PbDb pbdb = new PbDb();

        String scenarioParamSectionDisplay = "";
        String minTimeLevel = "";
        scnReqParams = new PbScenarioRequestParameters(request);
        collect = new PbScenarioCollection();
        disp = new DisplayScenarioParameters();
        repQuery = new PbReportQuery();

        scnReqParams.setScenarioParametersHashMap();
        HashMap map = new HashMap();
        Container container = null;
        HashMap ParametersHashMap = null;
        HashMap TableHashMap = null;
        HashMap TimeRangeMap = null;
        ArrayList nonViewByList = new ArrayList();
        ArrayList msrList = new ArrayList();
        String sqlstr = "";
        String finalQuery = "";
        PbReturnObject aggrRetObj = new PbReturnObject();
        ArrayList aggrList = new ArrayList();
        HashMap normalHm = new HashMap();
        HashMap NonViewByMap = new HashMap();
        PbReturnObject pbro2 = new PbReturnObject();

        try {
            if (session != null) {
                if (session.getAttribute("SCENARIOTAB") != null) {
                    map = (HashMap) session.getAttribute("SCENARIOTAB");
                } else {
                    map = new HashMap();
                }
                if (map.get(scenarioName) != null) {
                    container = (Container) map.get(scenarioName);
                } else {
                    container = new Container();
                }

                container.setScenarioId(scnrId);
                container.setScenarioName(scenarioName);

                //  //

                //  //

                ParametersHashMap = container.getParametersHashMap();
                TableHashMap = container.getTableHashMap();
                TimeRangeMap = container.getScenarioTimeRangeMap();

                if (ParametersHashMap == null) {
                    ParametersHashMap = new HashMap();
                }
                if (TableHashMap == null) {
                    TableHashMap = new HashMap();
                }
                if (TimeRangeMap == null) {
                    TimeRangeMap = new HashMap();
                }



                //////////////////
                session.setAttribute("scenarioId", scnrId);
                session.setAttribute("TIMELEVEL", minTimeLevel);
                session.setAttribute("userid", pbUserId);
                session.setAttribute("OLDSCENARIOID", scnrId);

                collect.scenarioId = scnrId;
                collect.userId = pbUserId;
                collect.scenarioIncomingParameters = scnReqParams.requestParamValues;
                collect.ctxPath = request.getContextPath();
                collect.newCustomModelId = this.newCustomModelId;
                collect.flag = this.flag;
                //collect.timeDetailsArray = (ArrayList) ParametersHashMap.get("TimeRangeDetails");

                collect.getParamMetaData();


                disp.ScenarioId = scnrId;
                scenarioParamSectionDisplay = disp.displayTimeParams(collect.timeDetailsMap, collect.timeDetailsArray, flag);
                scenarioParamSectionDisplay += disp.displayParams(collect.scenarioParameters, disp.ScenarioId);
                scenarioParamSectionDisplay += disp.displayViewBys(collect.scenarioViewByMain, collect.scenarioParameters, collect.scenarioModels, flag);

                request.setAttribute("scenarioParamSectionDisplay", scenarioParamSectionDisplay);
                request.setAttribute("completeURL", collect.completeUrl);

                disp.RowEdgeQuery(collect.scenarioViewByMain);
                disp.ColumnEdgeQuery(collect.scenarioViewByMain, collect.timeDetailsArray);
                TableHashMap = collect.scenarioTableHashMap;


                // repQuery.
                //   //
                repQuery.setRowViewbyCols(collect.scenarioRowViewbyValues);
                //////////////////////////////////////
//                repQuery.setColViewbyCols(collect.scenarioColViewbyValues);

//                ArrayList reportQryElementIds = new ArrayList();
//                reportQryElementIds = (ArrayList) TableHashMap.get("Measures");
//                ArrayList reportQryAggregations = new ArrayList();
//                reportQryAggregations.add("SUM");

                ArrayList timeA = new ArrayList();
                // timeA=(ArrayList)collect.timeDetailsMap.get("PRG_PERIOD_TYPE");
                ScenarioTemplateDAO tempDAO = new ScenarioTemplateDAO();
                String lastDate = tempDAO.getMonthLastDate((String) collect.timeDetailsArray.get(1));
                //////////////////////////////////////
                // //
//                timeA.add(0, "Day");
//                timeA.add(1, "PRG_STD");
//                timeA.add(2, lastDate);
//                timeA.add(3, "Month");
//                timeA.add(4, "Year");

                timeA = new ArrayList();
                timeA.add(0, "Day");
                timeA.add(1, "PRG_YEAR_RANGE");
                timeA.add(2, collect.timeDetailsArray.get(0).toString());
                timeA.add(3, collect.timeDetailsArray.get(1).toString());
                timeA.add(4, collect.timeDetailsArray.get(2).toString());
                timeA.add(5, collect.timeDetailsArray.get(3).toString());

                //  //

                nonViewByList = new ArrayList();
                //   //
                String[] msrStr = msrs.split(",");
//                for (int i = 0; i < msrStr.length; i++) {
//                    msrList.add(msrStr[0]);
//                }
                msrList.add(msrStr[0]);
                //   //
//                for (int view = 0; view < repQuery.crossTabNonViewBy.size(); view++) {
//                    nonViewByList.add(repQuery.crossTabNonViewBy.get(view));
//                }
                for (int view1 = 0; view1 < msrList.size(); view1++) {
                    nonViewByList.add(msrList.get(view1));
                }

                //  //
                sqlstr = "";
                sqlstr += "select REF_ELEMENT_TYPE,AGGREGATION_TYPE from prg_user_all_info_details ";
                sqlstr += " where ELEMENT_ID=" + msrList.get(0).toString() + " ";
                sqlstr += " OR REF_ELEMENT_ID = " + msrList.get(0).toString() + " ";
                sqlstr += " order by REF_ELEMENT_TYPE asc ";
                ////////////
                finalQuery = sqlstr;
                aggrRetObj = pbdb.execSelectSQL(finalQuery);
                if (aggrRetObj.getRowCount() > 0) {
                    for (int j = 0; j < aggrRetObj.getRowCount(); j++) {
                        aggrList.add(aggrRetObj.getFieldValueString(j, "AGGREGATION_TYPE")); //query Aggration
                    }
                }
                repQuery.setTimeDetails(timeA);
                repQuery.setQryColumns(msrList);
                repQuery.setColAggration(aggrList);
                repQuery.setDefaultMeasure(msrList.get(0).toString());
                repQuery.setDefaultMeasureSumm(aggrList.get(0).toString());
                repQuery.setParamValue(collect.scenarioParametersValues);
                repQuery.generateViewByQry();

                ///////////////////////////////////
                pbro2 = repQuery.getPbReturnObject(msrList.get(0).toString());
                //   //
//                normalHm.put("normalRetObj", pbro2);
                //    //
                String[] normalNonViewbys = (String[]) repQuery.NonViewByMap.keySet().toArray(new String[0]);
                for (int k = 0; k < repQuery.NonViewByMap.size(); k++) {
                    NonViewByMap.put(normalNonViewbys[k], repQuery.NonViewByMap.get(normalNonViewbys[k]));
                }

                ArrayList normList = new ArrayList();
                for (int i = 0; i < pbro2.getRowCount(); i++) {
                    for (int j = 1; j < pbro2.getColumnCount(); j++) {
                        normList.add(pbro2.getFieldValueString(i, j));
                    }
                    normalHm.put(pbro2.getFieldValueString(i, 0), normList);
                    normList = new ArrayList();
                }
                session.setAttribute("normalHm", normalHm);
                disp.setNormalHM(normalHm);
                disp.setNormalRetObj(pbro2);
                disp.setNonViewByMap(NonViewByMap);
                disp.setNonViewByList(nonViewByList);
                //  //
//                HashMap colViewMap = new HashMap();
//                ArrayList colNames = new ArrayList();
//                colViewMap = repQuery.crossTabNonViewByMap;
//                colNames = repQuery.crossTabNonViewBy;
//                //
//                //
//                HashMap all = new HashMap();
//                ArrayList graphArray = new ArrayList();
//                String tableDisplay = "";
//
//                //
//                //
////                all = disp.getScenarioTable(nonViewByList, pbro2, scnrId, collect.timeDetailsArray, collect.scenarioRowViewbyValues.get(0).toString(), repQuery.crossTabNonViewByMap, collect.scenarioViewByMain, collect.timeDetailsMap, collect.scenarioParameters, collect.drillUrl);
//
//                //

//                tableDisplay = (String) all.get("result");
//                graphArray = (ArrayList) all.get("graphArray");
//                String viewByName = (String) all.get("viewByName");
//                String modelId = (String) all.get("modelId");
//                ArrayList histMonths = (ArrayList) all.get("histMonths");
//                String dimensionId = (String) all.get("dimensionId");
//                request.setAttribute("viewByName", viewByName);
//                request.setAttribute("modelId", modelId);
//                request.setAttribute("histMonths", histMonths);
//                request.setAttribute("dimensionId", dimensionId);
//                request.setAttribute("timeDetailsArray", collect.timeDetailsArray);
//                request.setAttribute("crossTabNonViewByMap", repQuery.crossTabNonViewByMap);
//                request.setAttribute("crossTabNonViewBy", repQuery.crossTabNonViewBy);
//                request.setAttribute("crossTabRetObj", pbro2);
//
//
//                //
//                //
//
//
//                request.setAttribute("scenarioTableScetionDisplay", tableDisplay);
//                request.setAttribute("scenarioCombineTableScetionDisplay", request.getAttribute("scenarioCombineTableScetionDisplay"));
//                //
//                request.setAttribute("graphArray", graphArray);
            } else {
                ////////////////////////////////////////////////////////////////////
            }

        } catch (Exception exp) {
            logger.error("Exception:", exp);
        }
        return pbro2;
    }
}
