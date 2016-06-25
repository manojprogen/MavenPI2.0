package com.progen.scenarioview.action;

import com.progen.report.query.PbCrossTabQuery;
import com.progen.report.query.PbReportQuery;
import com.progen.scenario.display.DisplayScenarioParameters;
import com.progen.scenariodesigner.db.PbScenarioViewerBD;
import com.progen.scenariodesigner.db.ScenarioTemplateDAO;
import com.progen.scenarion.PbScenarioCollection;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.LookupDispatchAction;
import prg.db.PbReturnObject;
import prg.db.Session;
import prg.scenario.client.PbScenarioManager;
import prg.scenario.db.PbScenarioDb;
import prg.scenario.param.PbScenarioParamVals;

public class ScenarioViewerAction extends LookupDispatchAction {

    public static Logger logger = Logger.getLogger(ScenarioViewerAction.class);
    private final static String SUCCESS = "success";
    ScenarioTemplateDAO scnTemplateDAO = new ScenarioTemplateDAO();
    PbScenarioCollection scnCollect = new PbScenarioCollection();
    DisplayScenarioParameters disp = new DisplayScenarioParameters();

    protected Map getKeyMethodMap() {
        Map map = new HashMap();
        map.put("viewScenario", "viewScenario");
        map.put("upDateScenario", "upDateScenario");
        map.put("deleteScenario", "deleteScenario");
        map.put("addScenarioModelMaster", "addScenarioModelMaster");
        map.put("analyzeScenario", "analyzeScenario");
        map.put("getModelBasisValues", "getModelBasisValues");
        map.put("getCustomModelMonthsList", "getCustomModelMonthsList");
        map.put("saveCustomModel", "saveCustomModel");
        map.put("updateScenarioRating", "updateScenarioRating");
        map.put("compareScenario", "compareScenario");

        map.put("viewerNoramlQuery", "viewerNoramlQuery");
        map.put("saveBudgetModel", "saveBudgetModel");
        map.put("calcuForMethod", "calcuForMethod");
        return map;
    }

    public ActionForward viewScenario(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {

        HttpSession session = request.getSession(false);
        String scenarioId = "";
        String scenarioName = "";
        String newScnrFlag = "";
        String flag = "";
        String UserId = "";
        PbScenarioViewerBD scenarioViewerBD = null;
        String url = request.getRequestURL().toString();
        String minTimeLevel = "";
        String msrId = null;

        PbReturnObject normRetObj = new PbReturnObject();

        if (session != null) {

            try {

                DisplayScenarioParameters.setResponse(response);
                DisplayScenarioParameters.setSession(session);


                if (request.getParameter("scenarioId") != null) {
                    scenarioId = request.getParameter("scenarioId");
                } else if (request.getAttribute("scenarioId") != null) {
                    scenarioId = String.valueOf(request.getAttribute("scenarioId"));
                }
                if (request.getParameter("scenarioName") != null) {
                    scenarioName = request.getParameter("scenarioName");
                } else if (request.getAttribute("scenarioName") != null) {
                    scenarioName = String.valueOf(request.getAttribute("scenarioName"));
                }
                if (request.getParameter("newScnrFlag") != null) {
                    newScnrFlag = request.getParameter("newScnrFlag");
                }
                if (session.getAttribute("USERID") != null) {
                    UserId = String.valueOf(session.getAttribute("USERID"));
                }

                flag = request.getParameter("flag");
                session.setAttribute("combinedFlag", flag);
                scenarioViewerBD = new PbScenarioViewerBD();


                ////
                if (newScnrFlag.trim().equalsIgnoreCase("Y") || newScnrFlag.trim() == "Y" || "Y".trim().equalsIgnoreCase(newScnrFlag)) {
                    // //
                    scnCollect.setDrillurlTemp(null);
                    scnCollect.setCombinedRetObj(new PbReturnObject());
                    scnCollect.setNonViewByList(new ArrayList());
                    scnCollect.setNonViewByMap(new HashMap());
                    scnCollect.setNormalHm(new HashMap());
                    scnCollect.setScenarioModelsTemp(new LinkedHashMap());
                    scnCollect.setScenarioParamsTemp(new HashMap());
                    scnCollect.setScenarioViewbyMainTemp(new LinkedHashMap());
                    scnCollect.setTimeDetsArrayList(new ArrayList());
                    scnCollect.setTimeDetsMapTemp(new LinkedHashMap());
                    disp.setNonViewByList(new ArrayList());
                    disp.setNonViewByMap(new HashMap());
                    disp.setNormalHM(new HashMap());
                    disp.setNormalRetObj(new PbReturnObject());
//                    session.setAttribute("normalHm","null");
                }

                msrId = scnTemplateDAO.getScenarioDetsbyScenarioId(scenarioId);
                //  //
                session.setAttribute("msrId", msrId.trim());
                if (msrId != null && msrId != "" && !"".equalsIgnoreCase(msrId) && !msrId.equalsIgnoreCase("null") && !msrId.equalsIgnoreCase("")) {

                    if (session.getAttribute("msrId") != null && session.getAttribute("msrId") != "") {
                        //  //
                        //  //
                        //  //
                        session.setAttribute("Msrs", msrId);
                    } else {
                        //  //
                        // //
                        msrId = String.valueOf(session.getAttribute("Msrs"));
                    }
                    normRetObj = scenarioViewerBD.getNormalRetObj(msrId, scenarioId, scenarioName, UserId, request);
                    session.getAttribute("normalHm");
                    disp.setNormalHM((HashMap) session.getAttribute("normalHm"));
                    disp.setNormalRetObj(normRetObj);
                    // //
                }
                // //

                if (disp.getNormalHM().size() > 0) {
//                    viewerNoramlQuery(mapping, form, request, response);
                    normRetObj = scenarioViewerBD.getNormalRetObj(String.valueOf(session.getAttribute("Msrs")), scenarioId, scenarioName, UserId, request);
                    session.getAttribute("normalHm");
                    disp.setNormalHM((HashMap) session.getAttribute("normalHm"));
                    disp.setNormalRetObj(normRetObj);
                    //  //
                }


                scenarioViewerBD.prepareScenario(scenarioId, scenarioName, minTimeLevel, UserId, flag, request, response);
                request.setAttribute("scenarioId", scenarioId);
                request.setAttribute("scenarioName", scenarioName);


                return mapping.findForward("displayscenario");

            } catch (Exception exp) {
                logger.error("Exception:", exp);
                return mapping.findForward("exceptionPage");
            }
        } else {
            return mapping.findForward("sessionExpired");
        }
    }

    public ActionForward upDateScenario(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {

        HttpSession session = request.getSession(false);
        if (session != null) {
            try {
                String SCENARIO_NAME = request.getParameter("scenarioName");
//                //
                String SCENARIO_DESC = request.getParameter("scenarioDescription");
//                //
                String HISTORICAL_ST_MONTH = request.getParameter("histStartMonth");
//                //
                String HISTORICAL_END_MONTH = request.getParameter("histEndMonth");
//                //
                String SCENARIO_START_MONTH = request.getParameter("scenarioStartMonth");
//                //
                String SCENARIO_END_MONTH = request.getParameter("scenarioEndMonth");
//                //
                String SCENARIO_ID = request.getParameter("scenarioID");
//                //

                String[] userList = new String[7];
                userList[0] = SCENARIO_NAME;
                userList[1] = SCENARIO_DESC;
                userList[2] = HISTORICAL_ST_MONTH;
                userList[3] = HISTORICAL_END_MONTH;
                userList[4] = SCENARIO_START_MONTH;
                userList[5] = SCENARIO_END_MONTH;
                userList[6] = SCENARIO_ID;

                scnTemplateDAO.upDateScenario(userList);

            } catch (Exception ex) {
                logger.error("Exception:", ex);
            }
            return null;
        } else {
            return mapping.findForward("sessionExpired");
        }
    }

    public ActionForward deleteScenario(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {

        HttpSession session = request.getSession(false);
        String scenarioIds = "";
        String dimNames = "";
        String modelNames = "";

        if (session != null) {
            try {
                scenarioIds = request.getParameter("scenarioIds");
                dimNames = request.getParameter("dimNames");
                modelNames = request.getParameter("modelNames");



                scnTemplateDAO.deleteScenario(scenarioIds, dimNames, modelNames);

            } catch (Exception ex) {
                logger.error("Exception:", ex);
            }
            return null;
        } else {
            return mapping.findForward("sessionExpired");
        }
    }

    public ActionForward addScenarioModelMaster(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession(false);
        try {
            String path = request.getContextPath();
            PbScenarioParamVals insertM = new PbScenarioParamVals();
            PbScenarioManager scnManager = new PbScenarioManager();
            Session scnSession = new Session();
            String NewUrl = (String) session.getAttribute("NewUrl");
            String NONALLCOMBO = (String) session.getAttribute("NONALLCOMBO");
            String scenarioId = (String) session.getAttribute("scenarioId");
            String viewByName = (String) session.getAttribute("viewByName");
            String modelNameId = (String) session.getAttribute("modelName");
            String dimensionId = (String) session.getAttribute("dimensionId");
            String scenarioName = (String) request.getParameter("scenarioName");
            String completeURL = (String) session.getAttribute("completeURL");
            String bpNames = (String) request.getParameter("bpNames");
            String budgedispName = (String) request.getParameter("budgedispName");
            String expectedGrowth = (String) request.getParameter("expectedGrowth");
            String forecastFinalGrowth = (String) request.getParameter("forecastFinalGrowth");
            String funName = request.getParameter("funName");
            String bpVarStr = request.getParameter("bpVarStr");
            String anlyName = request.getParameter("anlyName");
//            //
            String[] bpnamesArr = bpNames.split(",");
            String[] expectedGrowthArray = expectedGrowth.split(",");
            String[] forecastFinalGrowthArra = forecastFinalGrowth.split(",");
            ArrayList parmList = new ArrayList();
            parmList.add(scenarioId);
            parmList.add(bpnamesArr);
            parmList.add(expectedGrowthArray);
            parmList.add(forecastFinalGrowthArra);
            parmList.add(budgedispName);
            parmList.add(bpVarStr);
            parmList.add(funName);
            parmList.add(anlyName);

            NONALLCOMBO = completeURL;
//            //
            // //
            String Msrs = "";
            if (session.getAttribute("Msrs") != null) {
                Msrs = (String) session.getAttribute("Msrs");
            }
            // //
            if (Msrs != null || Msrs != "" || !"".equalsIgnoreCase(Msrs)) {
                scnTemplateDAO.saveNewMeasure(scenarioId, Msrs);
            }

            // //
            // //

            String modelName = "";
            if (modelNameId.equalsIgnoreCase("Last Month")) {
                modelName = "Last Month";//Sce1";
            } else if (modelNameId.equalsIgnoreCase("Last Two Years Average")) {
                modelName = "Last Two Years Average";//"Sce2";
            } else if (modelNameId.equalsIgnoreCase("Last Three Years Average")) {
                modelName = "Last Three Years Average";//Sce3";
            } else if (modelNameId.equalsIgnoreCase("Last Six Years Average")) {
                modelName = "Last Six Years Average";//Sce6";
            } else if (modelNameId.equalsIgnoreCase("Last Nine Years Average")) {
                modelName = "Last Nine Years Average";//Sce9";
            } else if (modelNameId.equalsIgnoreCase("Last Twelve Years Average")) {
                modelName = "Last Twelve Years Average";//Sce12";
            } else if (modelNameId.equalsIgnoreCase("Last Two Years Average Growth")) {
                modelName = "Last Two Years Average Growth";
            } else if (modelNameId.equalsIgnoreCase("Last Three Years Average Growth")) {
                modelName = "Last Three Years Average Growth";
            } else if (modelNameId.equalsIgnoreCase("Last Four Years Average Growth")) {
                modelName = "Last Four Years Average Growth";
            } else if (modelNameId.equalsIgnoreCase("Last Six Years Average Growth")) {
                modelName = "Last Six Years Average Growth";
            } else {
                modelName = modelNameId;
            }
            if (modelName.equalsIgnoreCase("Budgeting")) {
                if (funName != null) {

                    PbScenarioDb pbScenarioDb = new PbScenarioDb();
                    pbScenarioDb.saveScnBudetingDetails(parmList);
                } else {
                    insertM.setNewUrl(NewUrl);
                    insertM.setNonAllCombo(NONALLCOMBO);
                    insertM.setParameterName(viewByName.trim());
                    insertM.setModelName(modelName);
                    insertM.setScenarioId(scenarioId);
                    insertM.setDimensionId(dimensionId);
                    insertM.setScenarioName(scenarioName);
                    insertM.setPath(path);
                    insertM.setbudgedispName(budgedispName);
                    scnSession.setObject(insertM);
                    scnManager.addModelMaster(scnSession);

                    //19-sept-09
                    insertM.setScenarioStatus("Saved");
                    scnSession.setObject(insertM);
                    scnManager.updateScenarioStatus(scnSession);

                    insertM.setScenarioRating("Assign Rating");
                    scnSession.setObject(insertM);
                    scnManager.updateScenarioRating(scnSession);
                    PbScenarioDb pbScenarioDb = new PbScenarioDb();
                    pbScenarioDb.saveScnBudetingDetails(parmList);


                }


            } else {
                insertM.setNewUrl(NewUrl);
                insertM.setNonAllCombo(NONALLCOMBO);
                insertM.setParameterName(viewByName.trim());
                insertM.setModelName(modelName);
                insertM.setScenarioId(scenarioId);
                insertM.setDimensionId(dimensionId);
                insertM.setScenarioName(scenarioName);
                insertM.setPath(path);
                insertM.setbudgedispName(budgedispName);
                scnSession.setObject(insertM);
                scnManager.addModelMaster(scnSession);

                //19-sept-09
                insertM.setScenarioStatus("Saved");
                scnSession.setObject(insertM);
                scnManager.updateScenarioStatus(scnSession);

                insertM.setScenarioRating("Assign Rating");
                scnSession.setObject(insertM);
                scnManager.updateScenarioRating(scnSession);

            }
            session.removeAttribute("expGrowth");

        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        PrintWriter out = response.getWriter();
        out.println("1");
        return null;
    }

    public ActionForward analyzeScenario(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {

        HttpSession session = request.getSession(false);

        String scenarioId = "";
        String scenarioName = "";
        String flag = "";
        String UserId = "";
        PbScenarioViewerBD scenarioViewerBD = null;
        String url = request.getRequestURL().toString();
        String minTimeLevel = "";
        String newCustomModelId = "";

        String newScnrFlag = "";
        PbReturnObject normRetObj = new PbReturnObject();
        String msrId = null;
        String bpName = request.getParameter("CBOARP76605");
        String anlName = request.getParameter("anlyName3Text");
        String secondaryViewByText = request.getParameter("secondaryViewByText");

//             //
        if (session != null) {
            try {
                DisplayScenarioParameters.setResponse(response);
                DisplayScenarioParameters.setSession(session);

                if (request.getParameter("scenarioId") != null) {
                    scenarioId = request.getParameter("scenarioId");
                } else if (request.getAttribute("scenarioId") != null) {
                    scenarioId = String.valueOf(request.getAttribute("scenarioId"));
                }

                if (request.getParameter("scenarioName") != null) {
                    scenarioName = request.getParameter("scenarioName");
                } else if (request.getAttribute("scenarioName") != null) {
                    scenarioName = String.valueOf(request.getAttribute("scenarioName"));
                }

                if (request.getParameter("flag") != null) {
                    flag = request.getParameter("flag");
                }

                if (request.getParameter("newCustomModelId") != null) {
                    newCustomModelId = request.getParameter("newCustomModelId");
                }

                if (session.getAttribute("USERID") != null) {
                    UserId = String.valueOf(session.getAttribute("USERID"));
                }

                if (request.getParameter("newScnrFlag") != null) {
                    newScnrFlag = request.getParameter("newScnrFlag");
                }

                scenarioViewerBD = new PbScenarioViewerBD();
                scenarioViewerBD.newCustomModelId = newCustomModelId;
                scenarioViewerBD.setSecondaryViewByText(secondaryViewByText);
                scenarioViewerBD.flag = flag;

                //  //
                if (newScnrFlag.trim().equalsIgnoreCase("Y") || newScnrFlag.trim() == "Y" || "Y".trim().equalsIgnoreCase(newScnrFlag)) {
                    //  //
                    scnCollect.setDrillurlTemp(null);
                    scnCollect.setCombinedRetObj(new PbReturnObject());
                    scnCollect.setNonViewByList(new ArrayList());
                    scnCollect.setNonViewByMap(new HashMap());
                    scnCollect.setNormalHm(new HashMap());
                    scnCollect.setScenarioModelsTemp(new LinkedHashMap());
                    scnCollect.setScenarioParamsTemp(new HashMap());
                    scnCollect.setScenarioViewbyMainTemp(new LinkedHashMap());
                    scnCollect.setTimeDetsArrayList(new ArrayList());
                    scnCollect.setTimeDetsMapTemp(new LinkedHashMap());
                    disp.setNonViewByList(new ArrayList());
                    disp.setNonViewByMap(new HashMap());
                    disp.setNormalHM(new HashMap());
                    disp.setNormalRetObj(new PbReturnObject());
                    session.removeAttribute("modelName");
//                    session.removeAttribute("expGrowth");
//                    session.setAttribute("normalHm","null");
                }
                msrId = scnTemplateDAO.getScenarioDetsbyScenarioId(scenarioId);
                //  //
                session.setAttribute("msrId", msrId.trim());
                if (msrId != null && msrId != "" && !"".equalsIgnoreCase(msrId) && !msrId.equalsIgnoreCase("null") && !msrId.equalsIgnoreCase("")) {

                    if (session.getAttribute("msrId") != null && session.getAttribute("msrId") != "") {
                        //  //
                        //  //
                        //  //
                        session.setAttribute("Msrs", msrId);
                    } else {
                        //  //
                        // //
                        msrId = String.valueOf(session.getAttribute("Msrs"));
                    }
                    normRetObj = scenarioViewerBD.getNormalRetObj(msrId, scenarioId, scenarioName, UserId, request);
                    session.getAttribute("normalHm");
                    disp.setNormalHM((HashMap) session.getAttribute("normalHm"));
                    disp.setNormalRetObj(normRetObj);
                    // //
                }
//                msrId = "";
                // //
//                disp.setNormalHM(new HashMap());
                // //
//                if (msrId == null || msrId == "" || "".equalsIgnoreCase(msrId) || msrId.equalsIgnoreCase("")) {
                if (disp.getNormalHM().size() > 0) {
//                    viewerNoramlQuery(mapping, form, request, response);
                    normRetObj = scenarioViewerBD.getNormalRetObj(String.valueOf(session.getAttribute("Msrs")), scenarioId, scenarioName, UserId, request);
                    session.getAttribute("normalHm");
                    disp.setNormalHM((HashMap) session.getAttribute("normalHm"));
                    disp.setNormalRetObj(normRetObj);
                    //  //
                }
//                }

                scenarioViewerBD.setParameterBpName(bpName);
                scenarioViewerBD.setParameteranlyName(anlName);

                scenarioViewerBD.prepareScenario(scenarioId, scenarioName, minTimeLevel, UserId, flag, request, response);
                request.setAttribute("scenarioId", scenarioId);
                request.setAttribute("scenarioName", scenarioName);
                session.setAttribute("scenarioName", scenarioName);
                session.setAttribute("minTimeLevel", minTimeLevel);
                session.setAttribute("flag", flag);


                return mapping.findForward("analyzeScenario");

            } catch (Exception exp) {
                logger.error("Exception:", exp);
                return mapping.findForward("exceptionPage");
            }
        } else {
            return mapping.findForward("sessionExpired");
        }
    }

    public ActionForward getModelBasisValues(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {

        HttpSession session = request.getSession(false);
        if (session != null) {
            try {
                PrintWriter out = response.getWriter();
                ArrayList modelBasisValues = scnTemplateDAO.getModelBasisValues();
                for (int i = 0; i < modelBasisValues.size(); i++) {

                    out.println((String) modelBasisValues.get(i));
                }

            } catch (Exception ex) {
                logger.error("Exception:", ex);
            }
            return null;
        } else {
            return mapping.findForward("sessionExpired");
        }
    }

    public ActionForward getCustomModelMonthsList(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {

        HttpSession session = request.getSession(false);
        String historicalStartMonth = "";
        String historicalEndMonth = "";
        if (session != null) {
            try {
                historicalStartMonth = request.getParameter("historicalStartMonth");
                historicalEndMonth = request.getParameter("historicalEndMonth");
                // //
                PrintWriter out = response.getWriter();
                ArrayList monthsList = scnTemplateDAO.getCustomModelMonthsList(historicalStartMonth, historicalEndMonth);
                // //
                for (int i = 0; i < monthsList.size(); i++) {

                    out.println((String) monthsList.get(i));
                }

            } catch (Exception ex) {
                logger.error("Exception:", ex);
            }
            return null;
        } else {
            return mapping.findForward("sessionExpired");
        }
    }

    public ActionForward saveCustomModel(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {

        HttpSession session = request.getSession(false);
        String scenarioId = "";
        String modelName = "";
        String modelBasis = "";
        String custModelMonthsStr = "";
        String weightsStr = "";
        String customModelId = "";
        String[] weights = {""};
        // //
        if (session != null) {
            try {
                scenarioId = request.getParameter("scenarioId");
                modelName = request.getParameter("modelName");
                modelBasis = request.getParameter("modelBasis");
                custModelMonthsStr = request.getParameter("custModelMonthsStr");
                weightsStr = request.getParameter("weightsStr");

                String[] custModelMonths = custModelMonthsStr.split(",");
                if (modelBasis.equalsIgnoreCase("Weighted Average")) {
                    weights = weightsStr.split(",");
                }
                //   //
                //   //

                customModelId = scnTemplateDAO.saveCustomModel(scenarioId, modelName, modelBasis, custModelMonths, weights);
                PrintWriter out = response.getWriter();
                out.println(customModelId);

            } catch (Exception ex) {
                logger.error("Exception:", ex);
            }
            return null;
        } else {
            return mapping.findForward("sessionExpired");
        }
    }

    public ActionForward updateScenarioRating(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {

        HttpSession session = request.getSession(false);
        String scenarioId = "";
        String modelName = "";
        String dimensionId = "";
        String scenarioRating = "";

        if (session != null) {
            try {
                scenarioRating = request.getParameter("scenarioRating");
                scenarioId = request.getParameter("scenarioId");
                modelName = request.getParameter("modelName");
                dimensionId = request.getParameter("dimensionId");


                if (scenarioRating.equalsIgnoreCase("One")) {
                    scenarioRating = "*";
                } else if (scenarioRating.equalsIgnoreCase("Two")) {
                    scenarioRating = "* *";
                } else if (scenarioRating.equalsIgnoreCase("Three")) {
                    scenarioRating = "* * *";
                } else if (scenarioRating.equalsIgnoreCase("Four")) {
                    scenarioRating = "* * * *";
                } else if (scenarioRating.equalsIgnoreCase("Five")) {
                    scenarioRating = "* * * * *";
                } else if (scenarioRating.equalsIgnoreCase("No")) {
                    scenarioRating = "Assign Rating";
                }

                scnTemplateDAO.updateScenarioRating(scenarioRating, scenarioId, modelName, dimensionId);

            } catch (Exception ex) {
                logger.error("Exception:", ex);
            }
            return null;
        } else {
            return mapping.findForward("sessionExpired");
        }
    }

    public ActionForward compareScenario(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {

        HttpSession session = request.getSession(false);
        String scenarioId = "";
        String modelNames = "";
        String dimNames = "";
        String flag = "";
        String timeLevel = "";
        PbScenarioViewerBD scenarioViewerBD = null;
        String scnIds = "";
        String scnNames = "";
        String UserId = "";
        String scenarioIdsStr = "";
        String scenarioNamesStr = "";
        String modelNamesWithQuotes = "";
        String scnMonths = "";
        String dimNamesStr = "";
        String[] tempIds = {""};
        String[] tempNames = {""};
        String[] tempDimNames = {""};
        String[] tempModelNames = {""};

        if (session != null) {
            try {
                if (request.getParameter("scnIds") != null) {
                    scnIds = request.getParameter("scnIds");
                }
                if (request.getParameter("scnNames") != null) {
                    scnNames = request.getParameter("scnNames");
                }
                if (request.getParameter("modelNames") != null) {
                    modelNames = request.getParameter("modelNames");
                }
                if (request.getParameter("dimNames") != null) {
                    dimNames = request.getParameter("dimNames");
                }
                if (request.getParameter("flag") != null) {
                    flag = request.getParameter("flag");
                }
                if (request.getParameter("timeLevel") != null) {
                    timeLevel = request.getParameter("timeLevel");
                }
                if (request.getParameter("scnMonths") != null) {
                    scnMonths = request.getParameter("scnMonths");
                }

                if (session.getAttribute("USERID") != null) {
                    UserId = String.valueOf(session.getAttribute("USERID"));
                }



                if (!scnIds.equalsIgnoreCase("")) {
                    tempIds = scnIds.split(",");
                    // //
                    if (tempIds.length >= 2) {
                        for (int i = 0; i < tempIds.length - 1; i++) {
                            if (tempIds[i].equalsIgnoreCase(tempIds[i + 1])) {
                                scenarioIdsStr = tempIds[i];
                            }
                        }
                    } else {
                        scenarioIdsStr = scnIds;
                    }
                }
                // //

                if (!scnNames.equalsIgnoreCase("")) {
                    tempNames = scnNames.split(",");
                    // //
                    if (tempNames.length >= 2) {
                        for (int i = 0; i < tempNames.length - 1; i++) {
                            if (tempNames[i].equalsIgnoreCase(tempNames[i + 1])) {
                                scenarioNamesStr = tempNames[i];
                            }
                        }
                    } else {
                        scenarioNamesStr = scnNames;
                    }
                }
                // //



                if (!dimNames.equalsIgnoreCase("")) {
                    tempDimNames = dimNames.split(",");
                    //  //
                    if (tempDimNames.length >= 2) {
                        for (int i = 0; i < tempDimNames.length - 1; i++) {
                            if (tempDimNames[i].equalsIgnoreCase(tempDimNames[i + 1])) {
                                dimNamesStr = tempDimNames[i];
                            }
                        }
                    } else {
                        dimNamesStr = dimNames;
                    }
                }
                // //

                if (!modelNames.equalsIgnoreCase("")) {
                    tempModelNames = modelNames.split(",");
                    for (int i = 0; i < tempModelNames.length; i++) {
                        if (modelNamesWithQuotes.equalsIgnoreCase("")) {
                            modelNamesWithQuotes = "'" + tempModelNames[i] + "'";
                        } else {
                            modelNamesWithQuotes = modelNamesWithQuotes + "," + "'" + tempModelNames[i] + "'";
                        }
                    }
                }

                scenarioViewerBD = new PbScenarioViewerBD();
                scenarioViewerBD.compareScenario(scenarioIdsStr, scenarioNamesStr, dimNamesStr, modelNamesWithQuotes, timeLevel, scnMonths, UserId, flag, request, response);

                request.setAttribute("scnIds", scenarioIdsStr);
                request.setAttribute("scnNames", scenarioNamesStr);
                request.setAttribute("modelNames", modelNames);
                request.setAttribute("dimNames", dimNames);
                request.setAttribute("timeLevel", timeLevel);
                request.setAttribute("scnMonths", scnMonths);


                return mapping.findForward("compareScenario");


            } catch (Exception ex) {
                logger.error("Exception:", ex);
                return mapping.findForward("exceptionPage");
            }
        } else {
            return mapping.findForward("sessionExpired");
        }
    }

    /*
     * public ActionForward viewerNoramlQuery(ActionMapping mapping, ActionForm
     * form, HttpServletRequest request, HttpServletResponse response) throws
     * java.lang.Exception {
     *
     * HttpSession session = request.getSession(false); String Msrs = ""; String
     * scenarioName = ""; String scenarioId = ""; String UserId = "";
     * PbReturnObject normRetObj = null; ArrayList msrList = new ArrayList();
     * ArrayList viewByList = new ArrayList(); String tableDisplay = "";
     * ArrayList graphArray = new ArrayList(); String
     * scenarioParamSectionDisplay = ""; DisplayScenarioParameters disp = null;
     * HashMap combinedTable = new HashMap(); String combinedFlag = ""; String
     * combinedTabledisp = "";
     *
     * if (session != null) { try {
     *
     * if (request.getParameter("Msrs") != null) { Msrs =
     * request.getParameter("Msrs"); } if (request.getParameter("scenarioId") !=
     * null) { scenarioId = request.getParameter("scenarioId"); } if
     * (request.getParameter("scenarioName") != null) { scenarioName =
     * request.getParameter("scenarioName"); } if
     * (session.getAttribute("USERID") != null) { UserId =
     * String.valueOf(session.getAttribute("USERID")); } if
     * (session.getAttribute("combinedFlag") != null) { combinedFlag =
     * String.valueOf(session.getAttribute("combinedFlag")); }
     *
     *
     * String[] msrStr = Msrs.split(","); for (int i = 0; i < msrStr.length;
     * i++) { msrList.add(msrStr[i]); } String viewById =
     * scnTemplateDAO.getviewByIdbyScenarioId(scenarioId); String[] viewByStr =
     * viewById.split(","); for (int j = 0; j < viewByStr.length; j++) {
     * viewByList.add(viewByStr[j]); } combinedTabledisp =
     * scnCollect.getNormalRetObj(Msrs, scenarioId, request);
     * request.setAttribute("normalRetObj", normRetObj); PbScenarioViewerBD
     * scnviewBD = new PbScenarioViewerBD(); PbCrossTabQuery crossTabQry = new
     * PbCrossTabQuery(); PbReportQuery repQry = new PbReportQuery();
     *
     * tableDisplay = (String) combinedTable.get("result"); graphArray =
     * (ArrayList) combinedTable.get("graphArray"); String viewByName = (String)
     * combinedTable.get("viewByName"); String modelId = (String)
     * combinedTable.get("modelId"); ArrayList histMonths = (ArrayList)
     * combinedTable.get("histMonths"); String dimensionId = (String)
     * combinedTable.get("dimensionId"); request.setAttribute("viewByName",
     * viewByName); request.setAttribute("modelId", modelId);
     * request.setAttribute("histMonths", histMonths);
     * request.setAttribute("dimensionId", dimensionId);
     * request.setAttribute("timeDetailsArray",
     * scnCollect.getTimeDetsArrayList());
     * request.setAttribute("crossTabNonViewByMap",
     * scnCollect.getNonViewByMap()); request.setAttribute("crossTabNonViewBy",
     * scnCollect.getNonViewByList()); request.setAttribute("crossTabRetObj",
     * scnCollect.getCombinedRetObj()); disp = new DisplayScenarioParameters();
     *
     * scenarioParamSectionDisplay =
     * disp.displayTimeParams2(scnCollect.getTimeDetsMapTemp(),
     * scnCollect.getTimeDetsArrayList(), combinedFlag, scenarioId);
     * scenarioParamSectionDisplay +=
     * disp.displayParams(scnCollect.getScenarioParamsTemp(), scenarioId);
     * scenarioParamSectionDisplay +=
     * disp.displayViewBys(scnCollect.getScenarioViewbyMainTemp(),
     * scnCollect.getScenarioParamsTemp(), scnCollect.getScenarioModelsTemp(),
     * combinedFlag);
     *
     * request.setAttribute("scenarioParamSectionDisplay",
     * scenarioParamSectionDisplay);
     *
     * request.setAttribute("scenarioTableScetionDisplay", tableDisplay);
     * request.setAttribute("scenarioCombineTableScetionDisplay",
     * combinedTabledisp);
     *
     *
     * // Container.setTabledisplay(combinedTabledisp);
     * request.setAttribute("scenarioCombineTableFlag", "Y");
     *
     * request.setAttribute("graphArray", graphArray);
     *
     * request.setAttribute("scenarioId", scenarioId);
     * request.setAttribute("scenarioName", scenarioName);
     * //response.sendRedirect("/Scenario/jsps/pbDisplayScenario.jsp");
     *
     * return mapping.findForward("displayscenario");
     *
     * // PrintWriter write=response.getWriter(); //
     * write.println(combinedTabledisp); // // return null;
     *
     *
     * } catch (Exception ex) { logger.error("Exception:",ex); return
     * mapping.findForward("exceptionPage"); } } else { return
     * mapping.findForward("sessionExpired"); }
    }
     */
    public ActionForward viewerNoramlQuery(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {

        HttpSession session = request.getSession(false);
        String Msrs = "";
        String scenarioName = "";
        String scenarioId = "";
        String UserId = "";
        PbReturnObject normRetObj = new PbReturnObject();
        ArrayList msrList = new ArrayList();
        ArrayList viewByList = new ArrayList();
        String tableDisplay = "";
        ArrayList graphArray = new ArrayList();
        String scenarioParamSectionDisplay = "";
        DisplayScenarioParameters disp = null;
        HashMap combinedTable = new HashMap();
        String combinedFlag = "";
        String combinedTabledisp = "";
        PbScenarioViewerBD scenarioViewerBD = null;

        if (session != null) {
            try {

                if (request.getParameter("Msrs") != null) {
                    Msrs = request.getParameter("Msrs");
                    session.setAttribute("Msrs", Msrs);
                } else {
                    Msrs = String.valueOf(session.getAttribute("Msrs"));
                }

                if (request.getParameter("scenarioId") != null) {
                    scenarioId = request.getParameter("scenarioId");
                    session.setAttribute("scenarioId", scenarioId);
                } else {
                    scenarioId = String.valueOf(session.getAttribute("scenarioId"));
                }
                if (request.getParameter("scenarioName") != null) {
                    scenarioName = request.getParameter("scenarioName");
                    session.setAttribute("scenarioName", scenarioName);
                } else {
                    scenarioName = String.valueOf(session.getAttribute("scenarioName"));
                }
                if (session.getAttribute("USERID") != null) {
                    UserId = String.valueOf(session.getAttribute("USERID"));
                }
                if (session.getAttribute("combinedFlag") != null) {
                    combinedFlag = String.valueOf(session.getAttribute("combinedFlag"));
                }


                String[] msrStr = Msrs.split(",");
//                for (int i = 0; i < msrStr.length; i++) {
//                    msrList.add(msrStr[i]);
//                }
                msrList.add(msrStr[0]);
                String viewById = scnTemplateDAO.getviewByIdbyScenarioId(scenarioId);
                String[] viewByStr = viewById.split(",");
                for (int j = 0; j < viewByStr.length; j++) {
                    viewByList.add(viewByStr[j]);
                }
                scenarioViewerBD = new PbScenarioViewerBD();
                disp = new DisplayScenarioParameters();
                normRetObj = scenarioViewerBD.getNormalRetObj(Msrs, scenarioId, scenarioName, UserId, request);
                request.setAttribute("normalRetObj", normRetObj);
                if (session.getAttribute("normalHm") != null) {
                    session.getAttribute("normalHm");
                    disp.setNormalHM((HashMap) session.getAttribute("normalHm"));
                    disp.setNormalRetObj(normRetObj);
                    // //
                }


                PbScenarioViewerBD scnviewBD = new PbScenarioViewerBD();
                PbCrossTabQuery crossTabQry = new PbCrossTabQuery();
                PbReportQuery repQry = new PbReportQuery();

                tableDisplay = (String) combinedTable.get("result");
                graphArray = (ArrayList) combinedTable.get("graphArray");
                String viewByName = (String) combinedTable.get("viewByName");
                String modelId = (String) combinedTable.get("modelId");
                ArrayList histMonths = (ArrayList) combinedTable.get("histMonths");
                String dimensionId = (String) combinedTable.get("dimensionId");
                request.setAttribute("viewByName", viewByName);
                request.setAttribute("modelId", modelId);
                request.setAttribute("histMonths", histMonths);
                request.setAttribute("dimensionId", dimensionId);
                request.setAttribute("timeDetailsArray", scnCollect.getTimeDetsArrayList());
                request.setAttribute("crossTabNonViewByMap", scnCollect.getNonViewByMap());
                request.setAttribute("crossTabNonViewBy", scnCollect.getNonViewByList());
                request.setAttribute("crossTabRetObj", scnCollect.getCombinedRetObj());
                disp = new DisplayScenarioParameters();

                scenarioParamSectionDisplay = disp.displayTimeParams2(scnCollect.getTimeDetsMapTemp(), scnCollect.getTimeDetsArrayList(), combinedFlag, scenarioId);
                scenarioParamSectionDisplay += disp.displayParams(scnCollect.getScenarioParamsTemp(), scenarioId);
                scenarioParamSectionDisplay += disp.displayViewBys(scnCollect.getScenarioViewbyMainTemp(), scnCollect.getScenarioParamsTemp(), scnCollect.getScenarioModelsTemp(), combinedFlag);

                request.setAttribute("scenarioParamSectionDisplay", scenarioParamSectionDisplay);

//                //
//                //


                request.setAttribute("scenarioTableScetionDisplay", tableDisplay);
                request.setAttribute("scenarioCombineTableScetionDisplay", combinedTabledisp);

                // //
//                Container.setTabledisplay(combinedTabledisp);
                request.setAttribute("scenarioCombineTableFlag", "Y");
                // //
                request.setAttribute("graphArray", graphArray);

                request.setAttribute("scenarioId", scenarioId);
                request.setAttribute("scenarioName", scenarioName);
                //response.sendRedirect("/Scenario/jsps/pbDisplayScenario.jsp");

                return mapping.findForward("displayscenario");

//                PrintWriter write=response.getWriter();
//                write.println(combinedTabledisp);
//
//                return null;


            } catch (Exception ex) {
                logger.error("Exception:", ex);
                return mapping.findForward("exceptionPage");
            }
        } else {
            return mapping.findForward("sessionExpired");
        }
    }

    public ActionForward saveBudgetModel(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {

        HttpSession session = request.getSession(false);
        String scenarioId = "";
        String modelName = "";
        String expGrowth = "";
        String newrowId = "";
        String expRowGrowth = "";
        String scenarioName = "";
        String expGrowthVal = "";
        ArrayList expGrowthList = new ArrayList();
        ArrayList GrowthapplyList = new ArrayList();
        String userId = "";
        String flag = "";
        String minTimelevel = "";
        String GrowthapplyStr = "";
        String[] weights = {""};
        PbScenarioViewerBD scenarioViewerBD = new PbScenarioViewerBD();
        String analyzeBy;
        String secondaryViewByText = "";

        // //
        if (session != null) {
            String NewUrl = (String) session.getAttribute("NewUrl");
//              //
            try {
                DisplayScenarioParameters.setResponse(response);
                DisplayScenarioParameters.setSession(session);

                scenarioId = request.getParameter("scenarioId");
                modelName = request.getParameter("modelName");
                expGrowth = request.getParameter("expGrowth");
                newrowId = request.getParameter("newrowId");
                expRowGrowth = request.getParameter("expRowGrowth");
                expGrowthVal = request.getParameter("expArray");
                GrowthapplyStr = request.getParameter("GrowthapplyStr");
                analyzeBy = request.getParameter("analyzeBy");
                secondaryViewByText = request.getParameter("secondaryViewByText");

                String[] GrowthapplyVal = GrowthapplyStr.split(",");
                for (int gr = 0; gr < GrowthapplyVal.length; gr++) {
                    GrowthapplyList.add(GrowthapplyVal[gr]);
                }
                if (!expGrowthVal.equalsIgnoreCase(null) && !"".equalsIgnoreCase(expGrowthVal)) {
                    String[] expGrowthStr = expGrowthVal.split(",");
                    for (int i = 0; i < expGrowthStr.length; i++) {
                        expGrowthList.add(i, expGrowthStr[i]);
                    }
                }
                if (session.getAttribute("scenarioName") != null) {
                    scenarioName = String.valueOf(session.getAttribute("scenarioName"));
                }
                if (session.getAttribute("flag") != null) {
                    flag = String.valueOf(session.getAttribute("flag"));
                }
                if (session.getAttribute("minTimeLevel") != null) {
                    minTimelevel = String.valueOf(session.getAttribute("minTimeLevel"));
                }
                if (session.getAttribute("USERID") != null) {
                    userId = String.valueOf(session.getAttribute("USERID"));
                }


                session.setAttribute("modelName", "Budgeting");
//                session.setAttribute("modelName", modelName);
                session.setAttribute("expGrowth", expGrowth);
                session.setAttribute("expRowGrowth", expRowGrowth);
                session.setAttribute("newrowId", newrowId);
                session.setAttribute("expGrowthList", expGrowthList);
                session.setAttribute("GrowthapplyList", GrowthapplyList);
//                PrintWriter out = response.getWriter();
//                out.println(customModelId);
                scenarioViewerBD.setParameteranlyName(analyzeBy);
                scenarioViewerBD.setSecondaryViewByText(secondaryViewByText);

                scenarioViewerBD.prepareScenario(scenarioId, scenarioName, minTimelevel, userId, flag, request, response);

                request.setAttribute("scenarioId", scenarioId);
                request.setAttribute("scenarioName", scenarioName);
            } catch (Exception ex) {
                logger.error("Exception:", ex);
            }
//            return null;
            return mapping.findForward("analyzeScenario");
        } else {
            return mapping.findForward("sessionExpired");
        }
    }

    public ActionForward calcuForMethod(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {

        String objVal = request.getParameter("objVal");
        String tdval = request.getParameter("tdval");
        BigDecimal bd = null;

//          //.println("objval is : "+objVal);
//          //.println("tdval is : "+tdval);
        double finalVal = ((100 + Double.parseDouble(objVal)) / 100) * Double.parseDouble(tdval);
        bd = new BigDecimal(finalVal);
        bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
        finalVal = bd.doubleValue();
//        finalVal = Math.round(finalVal, 2);
//          //.println("finalval is : "+finalVal);
        PrintWriter out = response.getWriter();
        out.print(finalVal);
        return null;

    }
    //
}
