/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.scenariodesigner.action;

import com.progen.scenariodesigner.db.PbScenarioViewerBD;
import com.progen.scenariodesigner.db.ScenarioDesigner;
import com.progen.scenariodesigner.db.ScenarioParam;
import com.progen.scenariodesigner.db.ScenarioTemplateDAO;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.LookupDispatchAction;
import prg.db.Container;
import prg.db.PbDb;
import prg.db.PbReturnObject;
import prg.db.Session;
import utils.db.ProgenParam;

public class ScenarioTemplateAction extends LookupDispatchAction {

    public static Logger logger = Logger.getLogger(ScenarioTemplateAction.class);
    private final static String SUCCESS = "success";
    ScenarioTemplateDAO scnTemplateDAO = new ScenarioTemplateDAO();

    /**
     * Provides the mapping from resource key to method name.
     *
     * @return Resource key / method name map.
     */
    protected Map getKeyMethodMap() {
        Map map = new HashMap();
        map.put("checkScenarioName", "checkScenarioName");
        map.put("goToScenarioDesigner", "goToScenarioDesigner");
        map.put("getScenarioDims", "getScenarioDims");
        map.put("buildScenarioParams", "buildScenarioParams");
        map.put("buildScenarioTable", "buildScenarioTable");
        map.put("getScenarioMeasure", "getScenarioMeasure");
        map.put("getScenarioTimeRange", "getScenarioTimeRange");
        map.put("saveScenarioTimeRange", "saveScenarioTimeRange");
        map.put("dispParams", "dispParams");
        map.put("dispTable", "dispTable");
        map.put("getScenarioSeededModels", "getScenarioSeededModels");
        map.put("saveScenarioSeededModels", "saveScenarioSeededModels");
        map.put("saveScenario", "saveScenario");
        map.put("viewScenario", "viewScenario");
        map.put("saveScenarioTimeRangeForYear", "saveScenarioTimeRangeForYear");

        map.put("getViewerScenarioMeasure", "getViewerScenarioMeasure");
        return map;
    }

    public ActionForward checkScenarioName(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {

        HttpSession session = request.getSession(false);
        String scenarioName = request.getParameter("scenarioName");
       String result = scnTemplateDAO.checkScenarioName(scenarioName);
        if (session != null) {
            PrintWriter out = response.getWriter();
            out.print(result);
            return null;
        } else {
            return mapping.findForward("sessionExpired");
        }
    }

    public ActionForward goToScenarioDesigner(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession(false);
        HashMap ParametersHashMap = null;
        HashMap TableHashMap = null;
        HashMap GraphHashMap = null;
        HashMap ScenarioHashMap = null;
        HashMap GraphTypesHashMap;
        HashMap GraphSizesHashMap;
        HashMap GraphClassesHashMap;
        HashMap GraphSizesDtlsHashMap;
        HashMap map = null;
        PbReturnObject graphTypes = null;
        PbReturnObject graphSizes = null;
        String scenarioId = null;
        Container container = null;
        String scenarioName = null;
        String scenarioDesc = null;
        PbReturnObject graphTypesRetObj = null;
        PbReturnObject graphSizesRetObj = null;
        String UserId = "";
        String url = request.getRequestURL().toString();

        ArrayList SeededModels = new ArrayList();
        PbReturnObject pbro = scnTemplateDAO.getSeededModels();
        for (int m = 0; m < pbro.getRowCount(); m++) {
            SeededModels.add(pbro.getFieldValueString(m, "MODEL_ID"));
        }


        if (session != null) {
            try {
                scenarioName = request.getParameter("scenarioName");
                scenarioDesc = request.getParameter("scenarioDesc");
                UserId = String.valueOf(session.getAttribute("USERID"));
                url = url + "?scnTemplateParam=goToScenarioDesigner&scenarioName=" + scenarioName + "&scenarioDesc=" + scenarioDesc;
                request.setAttribute("scenariodesignerurl", url);


                if (session.getAttribute("SCENARIOTAB") != null) {
                    map = (HashMap) session.getAttribute("SCENARIOTAB");
                    if (map.get(scenarioName) != null) {
                        container = (Container) map.get(scenarioName);
                    } else {
                        container = new Container();
                    }
                } else {
                    container = new Container();
                }
                ParametersHashMap = container.getParametersHashMap();
                TableHashMap = container.getTableHashMap();
                GraphHashMap = container.getGraphHashMap();
                ScenarioHashMap = container.getScenarioHashMap();

                if (ParametersHashMap == null) {
                    ParametersHashMap = new HashMap();
                }
                if (TableHashMap == null) {
                    TableHashMap = new HashMap();
                }
                if (GraphHashMap == null) {
                    GraphHashMap = new HashMap();
                }
                if (ScenarioHashMap == null) {
                    ScenarioHashMap = new HashMap();
                }
                ScenarioHashMap.put("scenarioName", scenarioName);
                ScenarioHashMap.put("scenarioDesc", scenarioDesc);
                ParametersHashMap.put("SeededModels", SeededModels);
                String result = scnTemplateDAO.getUserFoldersByUserId(UserId);
                request.setAttribute("ScenarioId", scenarioId);
                request.setAttribute("UserFlds", result);
                request.setAttribute("scenarioName", scenarioName);
                request.setAttribute("scenarioDesc", scenarioDesc);
                container.setScenarioName(scenarioName);
                container.setScenarioDesc(scenarioDesc);
                container.setParametersHashMap(ParametersHashMap);
                container.setSessionContextScenario(session, container, scenarioName);

            } catch (Exception exception) {
                logger.error("Exception:", exception);
                return mapping.findForward("exceptionPage");
            }
            return mapping.findForward(SUCCESS);
        } else {
            return mapping.findForward("sessionExpired");
        }
    }

    public ActionForward getScenarioDims(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {

        HttpSession session = request.getSession(false);
        HashMap map = new HashMap();
        Container container = null;
        String foldersIds = "";
        String scenarioName = "";
        if (session != null) {
            map = (HashMap) session.getAttribute("SCENARIOTAB");
            scenarioName = request.getParameter("scenarioName");
            container = (Container) map.get(scenarioName);
            HashMap ParametersHashMap = container.getParametersHashMap();
            foldersIds = request.getParameter("foldersIds");
            ParametersHashMap.put("UserFolderIds", foldersIds);
            String result = scnTemplateDAO.getScenarioDims(foldersIds);
            PrintWriter out = response.getWriter();
            out.print(result);
            container.setParametersHashMap(ParametersHashMap);
            return null;
        } else {
            return mapping.findForward("sessionExpired");
        }
    }

    public ActionForward buildScenarioParams(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {

        HttpSession session = request.getSession(false);
        ProgenParam pParam = new ProgenParam();
        String foldersIds = null;
        String minTimeLevel = null;
        String dimId = null;
        String scenarioName = "";
        String scenarioDesc = "";
        HashMap map = new HashMap();
        Container container = new Container();
        String paramIds = null;
        String paramNames = null;
        String timeparams = null;
        String[] parameterIds = null;
        String[] parameterNames = null;
        String[] timeparameterIds = null;
        ArrayList Parameters = new ArrayList();
        ArrayList ParametersNames = new ArrayList();
        ArrayList timeParameters = new ArrayList();
        HashMap ParametersHashMap;
        if (session != null) {
            map = (HashMap) session.getAttribute("SCENARIOTAB");
            scenarioName = request.getParameter("scenarioName");
            scenarioDesc = request.getParameter("scenarioDesc");

            container = (Container) map.get(scenarioName);

            foldersIds = request.getParameter("foldersIds");
            dimId = request.getParameter("dimId");

            //if (container != null) {
            //if (container.getScenarioHashMap() != null) {
            ParametersHashMap = container.getParametersHashMap();
            //}
            // }
            if (ParametersHashMap == null) {
                ParametersHashMap = new HashMap();
            }
            ParametersHashMap.put("UserFolderIds", foldersIds);//added by santhosh.kumar@progenbusiness.com on 03/12/2009 for getting Report Biz Roles
            minTimeLevel = scnTemplateDAO.getUserFolderMinTimeLevel(foldersIds);
  paramIds = request.getParameter("params");
            paramNames = request.getParameter("paramNames");
            timeparams = request.getParameter("timeparams");
            //update Param hashmap in session
            if (paramIds != null) {
                parameterIds = paramIds.split(",");
                parameterNames = paramNames.split(",");
                for (int paramCount = 0; paramCount < parameterIds.length; paramCount++) {
                    Parameters.add(parameterIds[paramCount]);
                    ParametersNames.add(parameterNames[paramCount]);
                }
            }

            if (timeparams != null) {
                timeparameterIds = timeparams.split(",");
                for (int timeparamCount = 0; timeparamCount < timeparameterIds.length; timeparamCount++) {
                    timeParameters.add(timeparameterIds[timeparamCount]);
                }
            }
            ParametersHashMap.put("Parameters", Parameters);
            ParametersHashMap.put("ParametersNames", ParametersNames);//newly added on 15-10-2009 by santhosh.kumar@progenbusiness.com
            ParametersHashMap.put("timeParameters", timeParameters);


            Date date = new Date();
            String DATE_FORMAT = "MM/dd/yyyy";
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);


            LinkedHashMap timeDimMap = new LinkedHashMap();
            ArrayList timeDetails = new ArrayList();
            ArrayList dateArray = new ArrayList();
            ArrayList weekArray = new ArrayList();
            ArrayList asofWeekArray = new ArrayList();
            ArrayList monthArray = new ArrayList();
            ArrayList monthArray2 = new ArrayList();
            ArrayList asofMonthArray = new ArrayList();
            ArrayList qtrArray = new ArrayList();
            ArrayList asofQtrArray = new ArrayList();
            ArrayList yearArray = new ArrayList();
            ArrayList asofYrArray = new ArrayList();
            ArrayList periodTypeArray = new ArrayList();
            ArrayList compareArray = new ArrayList();
            //  //
            if (dimId.equalsIgnoreCase("Time-Month Range Basis") || dimId.equalsIgnoreCase("Time-Year Range Basis")) {
                if (timeParameters != null) {
                    for (int time = 0; time < timeParameters.size(); time++) {
                        if (minTimeLevel.equals("3")) {
                            if (dimId.equalsIgnoreCase("Time-Month Range Basis")) {
                                if (timeParameters.get(time).toString().equalsIgnoreCase("AS_OF_MONTH")) {
                                    monthArray.add(null);
                                    monthArray.add("CBO_AS_OF_MONTH");
                                    monthArray.add("MONTH");
                                    monthArray.add("1");
                                    monthArray.add("1");
                                    monthArray.add(null);
                                    monthArray.add("AS_OF_MONTH");
                                    monthArray.add("From Month");
                                    timeDimMap.put("AS_OF_MONTH", monthArray);
                                } else if (timeParameters.get(time).toString().equalsIgnoreCase("AS_OF_MONTH1")) {
                                    monthArray2.add(null);
                                    monthArray2.add("CBO_AS_OF_MONTH1");
                                    monthArray2.add("MONTH1");
                                    monthArray2.add("2");
                                    monthArray2.add("2");
                                    monthArray2.add(null);
                                    monthArray2.add("AS_OF_MONTH1");
                                    monthArray2.add("To Month");
                                    timeDimMap.put("AS_OF_MONTH1", monthArray2);
                                } else if (timeParameters.get(time).toString().equalsIgnoreCase("PRG_PERIOD_TYPE")) {
                                    periodTypeArray.add("MONTH");
                                    periodTypeArray.add("CBO_PRG_PERIOD_TYPE");
                                    periodTypeArray.add("AGGREGATION");
                                    periodTypeArray.add("3");
                                    periodTypeArray.add("3");
                                    periodTypeArray.add("MONTH");
                                    periodTypeArray.add("PRG_PERIOD_TYPE");
                                    periodTypeArray.add("Period");
                                    timeDimMap.put("PRG_PERIOD_TYPE", periodTypeArray);
                                }
                                timeDetails.add("Month");
                                timeDetails.add("PRG_STD");
                                timeDetails.add(null);
                                timeDetails.add("Month");
                                timeDetails.add("Last Period");
                            } else if (dimId.equalsIgnoreCase("Time-Year Range Basis")) {
                                if (timeParameters.get(time).toString().equalsIgnoreCase("AS_OF_YEAR")) {
                                    yearArray.add(String.valueOf(pParam.getYearforpage()));
                                    yearArray.add("CBO_AS_OF_YEAR");
                                    yearArray.add("From Year");
                                    yearArray.add("1");
                                    yearArray.add("1");
                                    yearArray.add(null);
                                    yearArray.add("AS_OF_YEAR");
                                    timeDimMap.put("AS_OF_YEAR", yearArray);
                                } else if (timeParameters.get(time).toString().equalsIgnoreCase("AS_OF_YEAR1")) {
                                    asofYrArray.add(String.valueOf(pParam.getYearforpage()));
                                    asofYrArray.add("CBO_AS_OF_YEAR1");
                                    asofYrArray.add("To Year");
                                    asofYrArray.add("2");
                                    asofYrArray.add("2");
                                    asofYrArray.add(null);
                                    asofYrArray.add("AS_OF_YEAR1");
                                    timeDimMap.put("AS_OF_YEAR1", asofYrArray);
                                }
                                timeDetails.add("Day");
                                timeDetails.add("PRG_YEAR_RANGE");
                                timeDetails.add(pParam.getYearforpage().toString());//added on 28-11-09
                                timeDetails.add(null);
                                timeDetails.add(null);
                                timeDetails.add(null);
                            }
                        }
                    }
                }
                ParametersHashMap.put("TimeDimHashMap", timeDimMap);
                ParametersHashMap.put("TimeDetailstList", timeDetails);
            }
            container.setParametersHashMap(ParametersHashMap);
            container.setScenarioDesc(scenarioDesc);
            container.setScenarioName(scenarioName);
            container.setSessionContextScenario(session, container, scenarioName);
            return null;
        } else {
            return mapping.findForward("sessionExpired");
        }
    }

    public ActionForward dispParams(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {

        HttpSession session = request.getSession(false);
        HashMap map = new HashMap();
        Container container = null;
        String scenarioName = "";
        String params = null;
        HashMap ParametersHashMap = null;
        String result = null;
        PrintWriter out = null;
        if (session != null) {
            try {
                scenarioName = request.getParameter("scenarioName");
                map = (HashMap) session.getAttribute("SCENARIOTAB");
                container = (Container) map.get(scenarioName);
                params = request.getParameter("params");
                ParametersHashMap = container.getParametersHashMap();
               result = scnTemplateDAO.dispParameters(params, ParametersHashMap);
                out = response.getWriter();
                out.print(result);
                return null;
            } catch (Exception e) {
                logger.error("Exception:", e);
                return mapping.findForward("exceptionPage");
            }
        } else {
            return mapping.findForward("sessionExpired");
        }
    }

    public ActionForward dispTable(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {

        HttpSession session = request.getSession(false);
        HashMap map = new HashMap();
        Container container = null;
        String scenarioName = "";
        String params = null;
        HashMap ParametersHashMap = null;
        HashMap TableHashMap = null;
        String result = null;
        PrintWriter out = null;
        if (session != null) {
            try {
                scenarioName = request.getParameter("scenarioName");
                map = (HashMap) session.getAttribute("SCENARIOTAB");
                container = (Container) map.get(scenarioName);
                ParametersHashMap = container.getParametersHashMap();
                TableHashMap = container.getTableHashMap();
                result = scnTemplateDAO.dispTable(ParametersHashMap, TableHashMap);
                out = response.getWriter();
                out.print(result);
                return null;
            } catch (Exception e) {
                logger.error("Exception:", e);
                return mapping.findForward("exceptionPage");
            }
        } else {
            return mapping.findForward("sessionExpired");
        }
    }

    public ActionForward buildScenarioTable(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {

        HttpSession session = request.getSession(false);
        String scenarioName = "";
        HashMap map = new HashMap();
        Container container = null;
        String buildTableChange = null;
        String Measures = null;
        String rowEdgeParams = null;
        String colEdgeParams = null;
        String MeasureNames = null;
        String rowEdgeParamsNames = null;
        String colEdgeParamsNames = null;
        HashMap TableHashMap = null;
        HashMap GraphHashMap = null;
        HashMap ParametersHashMap = null;
        ArrayList Parameters = null;
        ArrayList ParametersNames = null;
        TreeMap TableProperties = null;
        String defaultSortedCol = null;
        String[] chkTtlValues = null;
        String[] chkSubTtlValues = null;
        String[] chkAvgValues = null;
        String[] selSymbolsValues = null;
        if (session != null) {
            map = (HashMap) session.getAttribute("SCENARIOTAB");
            scenarioName = request.getParameter("scenarioName");
            container = (Container) map.get(scenarioName);
            if (request.getParameter("buildTableChange") != null) {
                buildTableChange = request.getParameter("buildTableChange");
            }
            if (request.getParameter("Msrs") != null) {
                Measures = request.getParameter("Msrs");
            }
            if (request.getParameter("MsrsNames") != null) {
                MeasureNames = request.getParameter("MsrsNames");
            }
            if (request.getParameter("rowEdgeParams") != null) {
                rowEdgeParams = request.getParameter("rowEdgeParams");
            }
            if (request.getParameter("colEdgeParams") != null) {
                colEdgeParams = request.getParameter("colEdgeParams");
            }
            if (request.getParameter("rowEdgeParamsNames") != null) {
                rowEdgeParamsNames = request.getParameter("rowEdgeParamsNames");
            }
            if (request.getParameter("colEdgeParamsNames") != null) {
                colEdgeParamsNames = request.getParameter("colEdgeParamsNames");
            }
 ParametersHashMap = container.getParametersHashMap();
            TableHashMap = container.getTableHashMap();


            if (ParametersHashMap.get("ParametersNames") != null) {
                Parameters = (ArrayList) ParametersHashMap.get("Parameters");
                ParametersNames = (ArrayList) ParametersHashMap.get("ParametersNames");
            }
            if (TableHashMap == null) {
                TableHashMap = new HashMap();
            }

            try {
                if (TableHashMap != null) {
                    if (buildTableChange != null && buildTableChange.equalsIgnoreCase("REP")) {
                       String[] REP = rowEdgeParams.split(",");
                        String[] REPNames = rowEdgeParamsNames.split(",");
                        ArrayList rowEdgeArray = new ArrayList();
                        ArrayList rowEdgeNameArray = new ArrayList();

                        for (int i = 0; i < REP.length; i++) {
                            if (!(REP[i].equalsIgnoreCase(""))) {
                                rowEdgeArray.add(REP[i]);
                                rowEdgeNameArray.add(REPNames[i]);
                            }
                        }
                        TableHashMap.put("REP", rowEdgeArray);
                        TableHashMap.put("REPNames", rowEdgeNameArray);


                    } else if (buildTableChange != null && buildTableChange.equalsIgnoreCase("CEP")) {
                        String[] CEP = colEdgeParams.split(",");
                        String[] CEPNames = colEdgeParamsNames.split(",");
                        ArrayList colEdgeArray = new ArrayList();
                        ArrayList colEdgeNameArray = new ArrayList();

                        for (int i = 0; i < CEP.length; i++) {
                            if (!(CEP[i].equalsIgnoreCase(""))) {
                                colEdgeArray.add(CEP[i]);
                                colEdgeNameArray.add(CEPNames[i]);
                            }
                        }
                        TableHashMap.put("CEP", colEdgeArray);
                        TableHashMap.put("CEPNames", colEdgeNameArray);

                    } else if (buildTableChange != null && buildTableChange.equalsIgnoreCase("Measures") && Measures != null && MeasureNames != null) {

                        String[] Msrs = Measures.split(",");
                        String[] MsrNames = MeasureNames.split(",");

                        ArrayList measureArray = new ArrayList();
                        ArrayList measureNames = new ArrayList();
                        for (int i = 0; i < Msrs.length; i++) {
                            if (!(Msrs[i].equalsIgnoreCase(""))) {
                                measureArray.add(Msrs[i]);
                                measureNames.add(MsrNames[i]);
                            }
                        }
                        TableHashMap.put("Measures", measureArray);
                        TableHashMap.put("MeasuresNames", measureNames);
                    }
                }

                /*
                 * ArrayList measureArray = new ArrayList();
                 * ////////////////////////////////////// if (Measures != null)
                 * { if (Measures.length() > 1) { String[] Msrs =
                 * Measures.split(","); for (int i = 0; i < Msrs.length; i++) {
                 * if (!(Msrs[i].equalsIgnoreCase(""))) {
                 * measureArray.add(Msrs[i]); } } } }
                 * ////////////////////////////////////// if
                 * (measureArray.size() > 0) { TableHashMap.put("Measures",
                 * measureArray); TableHashMap.put("MeasuresNames",
                 * measureArray); }
                 */

                container.setTableHashMap(TableHashMap);
                return null;

            } catch (Exception exp) {
                logger.error("Exception:", exp);
                return mapping.findForward("exceptionPage");
            }
        } else {
            return mapping.findForward("sessionExpired");
        }

    }

    public ActionForward getScenarioMeasure(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession(false);
        String foldersIds = "";
        String result = "";
        Container container = null;
        String scenarioName = "";
        String scenarioDesc = "";

        HashMap map = new HashMap();
        if (session != null) {
            map = (HashMap) session.getAttribute("SCENARIOTAB");
            scenarioName = request.getParameter("scenarioName");
            scenarioDesc = request.getParameter("scenarioDesc");
            foldersIds = request.getParameter("foldersIds");
              result = scnTemplateDAO.getMeasures(foldersIds);
            request.setAttribute("Measures", result);
            container = (Container) map.get(scenarioName);
            container.setScenarioName(scenarioName);
            container.setScenarioDesc(scenarioDesc);
            request.setAttribute("scenarioName", scenarioName);
            request.setAttribute("scenarioDesc", scenarioDesc);
            return mapping.findForward("scenariomeasures");
        } else {
            return mapping.findForward("sessionExpired");
        }
    }

    public ActionForward getScenarioTimeRange(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession(false);

        String result = "";
        Container container = null;
        String scenarioName = "";
        String scenarioDesc = "";
        String scenarioStartMonth = "";
        String scenarioEndMonth = "";
        String historicalStartMonth = "";
        String historicalEndMonth = "";
        HashMap map = new HashMap();
        if (session != null) {
            map = (HashMap) session.getAttribute("SCENARIOTAB");
            scenarioName = request.getParameter("scenarioName");
            scenarioDesc = request.getParameter("scenarioDesc");
            if (request.getParameter("scenarioStartMonth") != null) {
                scenarioStartMonth = request.getParameter("scenarioStartMonth");
            }
            if (request.getParameter("scenarioEndMonth") != null) {
                scenarioEndMonth = request.getParameter("scenarioEndMonth");
            }
            if (request.getParameter("historicalStartMonth") != null) {
                historicalStartMonth = request.getParameter("historicalStartMonth");
            }
            if (request.getParameter("historicalEndMonth") != null) {
                historicalEndMonth = request.getParameter("historicalEndMonth");
            }
            container = (Container) map.get(scenarioName);
            container.setScenarioName(scenarioName);
            container.setScenarioDesc(scenarioDesc);
            request.setAttribute("scenarioName", scenarioName);
            request.setAttribute("scenarioDesc", scenarioDesc);
            request.setAttribute("scenarioStartMonth", scenarioStartMonth);
            request.setAttribute("scenarioEndMonth", scenarioEndMonth);
            request.setAttribute("historicalStartMonth", historicalStartMonth);
            request.setAttribute("historicalEndMonth", historicalEndMonth);
            return mapping.findForward("scenarioTimeRange");
        } else {
            return mapping.findForward("sessionExpired");
        }
    }

    public ActionForward saveScenarioTimeRange(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession(false);
        String result = "";
        Container container = null;
        String scenarioName = request.getParameter("scenarioName");
        String histEndMonth = request.getParameter("histEndMonth");
        String histStartMonth = request.getParameter("histStartMonth");
        String scenarioStartMonth = request.getParameter("scenarioStartMonth");
        String scenarioEndMonth = request.getParameter("scenarioEndMonth");

        ArrayList histTimeRange = new ArrayList();
        ArrayList scenarioTimeRange = new ArrayList();
        histTimeRange.add(0, histStartMonth);
        histTimeRange.add(1, histEndMonth);
        scenarioTimeRange.add(0, scenarioStartMonth);
        scenarioTimeRange.add(1, scenarioEndMonth);
        PrintWriter out = response.getWriter();

        HashMap map = new HashMap();
        HashMap TimeRangeMap = null;
        HashMap ParametersHashMap = null;
        ArrayList timeDetails = new ArrayList();

        if (session != null) {
            map = (HashMap) session.getAttribute("SCENARIOTAB");
            container = (Container) map.get(scenarioName);
            TimeRangeMap = container.getScenarioTimeRangeMap();
            ParametersHashMap = container.getParametersHashMap();
            if (TimeRangeMap == null) {
                TimeRangeMap = new HashMap();
            }
            timeDetails.add(histStartMonth);
            timeDetails.add(histEndMonth);
            timeDetails.add(scenarioStartMonth);
            timeDetails.add(scenarioEndMonth);
            ParametersHashMap.put("TimeRangeDetails", timeDetails);
            TimeRangeMap.put("scenarioTimeRange", scenarioTimeRange);
            TimeRangeMap.put("histTimeRange", histTimeRange);
            request.setAttribute("scenarioName", scenarioName);
            container.setScenarioTimeRangeMap(TimeRangeMap);
            container.setSessionContextScenario(session, container, scenarioName);

            out.println(histStartMonth);
            out.println(histEndMonth);
            out.println(scenarioStartMonth);
            out.println(scenarioEndMonth);
            //out.print("1");
            return null;
        } else {
            return mapping.findForward("sessionExpired");
        }
    }

    public ActionForward saveScenarioTimeRangeForYear(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession(false);
        String result = "";
        Container container = null;
        String scenarioName = request.getParameter("scenarioName");
        String histEndYear = request.getParameter("histEndYear");
        String histStartYear = request.getParameter("histStartYear");
        String scenarioStartYear = request.getParameter("scenarioStartYear");
        String scenarioEndYear = request.getParameter("scenarioEndYear");

        ArrayList histTimeRange = new ArrayList();
        ArrayList scenarioTimeRange = new ArrayList();
        histTimeRange.add(0, histStartYear);
        histTimeRange.add(1, histEndYear);
        scenarioTimeRange.add(0, scenarioStartYear);
        scenarioTimeRange.add(1, scenarioEndYear);
        PrintWriter out = response.getWriter();

        HashMap map = new HashMap();
        HashMap TimeRangeMap = null;
        HashMap ParametersHashMap = null;
        ArrayList timeDetails = new ArrayList();

        if (session != null) {
            map = (HashMap) session.getAttribute("SCENARIOTAB");
            container = (Container) map.get(scenarioName);
            TimeRangeMap = container.getScenarioTimeRangeMap();
            ParametersHashMap = container.getParametersHashMap();
            if (TimeRangeMap == null) {
                TimeRangeMap = new HashMap();
            }
            timeDetails.add(histStartYear);
            timeDetails.add(histEndYear);
            timeDetails.add(scenarioStartYear);
            timeDetails.add(scenarioEndYear);
            ParametersHashMap.put("TimeRangeDetails", timeDetails);
            TimeRangeMap.put("scenarioTimeRange", scenarioTimeRange);
            TimeRangeMap.put("histTimeRange", histTimeRange);
            request.setAttribute("scenarioName", scenarioName);
            container.setScenarioTimeRangeMap(TimeRangeMap);
            container.setSessionContextScenario(session, container, scenarioName);

            out.println(histStartYear);
            out.println(histEndYear);
            out.println(scenarioStartYear);
            out.println(scenarioEndYear);
            //out.print("1");
            return null;
        } else {
            return mapping.findForward("sessionExpired");
        }
    }

    public ActionForward getScenarioSeededModels(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession(false);

        String result = "";
        String scenarioName = "";
        String selectedSeededModels = "";
        HashMap map = new HashMap();
        Container container = null;

        if (session != null) {
            map = (HashMap) session.getAttribute("SCENARIOTAB");
            scenarioName = request.getParameter("scenarioName");
            selectedSeededModels = request.getParameter("selectedSeededModels");
            container = (Container) map.get(scenarioName);
            container.setScenarioName(scenarioName);

            request.setAttribute("scenarioName", scenarioName);
            request.setAttribute("selectedSeededModels", selectedSeededModels);

            return mapping.findForward("scenarioSeededModels");
        } else {
            return mapping.findForward("sessionExpired");
        }
    }

    public ActionForward saveScenarioSeededModels(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession(false);
        String result = "";
        Container container = null;
        String scenarioName = request.getParameter("scenarioName");
        String totalUrl = request.getParameter("totalUrl");
        if (totalUrl.length() > 1) {
            totalUrl = totalUrl.substring(1);
        }
        String sModels[] = totalUrl.split(",");
        ArrayList selectedSeededModels = new ArrayList();
        for (int y = 0; y < sModels.length; y++) {
            selectedSeededModels.add(sModels[y]);
        }
        PrintWriter out = response.getWriter();
        HashMap map = new HashMap();
        HashMap ParametersHashMap = new HashMap();
        if (session != null) {
            map = (HashMap) session.getAttribute("SCENARIOTAB");
            container = (Container) map.get(scenarioName);
            ParametersHashMap = container.getParametersHashMap();
            if (ParametersHashMap == null) {
                ParametersHashMap = new HashMap();
            }
            ParametersHashMap.put("SeededModels", selectedSeededModels);
            container.setScenarioHashMap(ParametersHashMap);
            // container.setSessionContextScenario(session,container,scenarioName);
            out.print(totalUrl);
            return null;
        } else {
            return mapping.findForward("sessionExpired");
        }
    }

    public ActionForward saveScenario(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession(false);
        String scenarioName = "";
        String scenarioDesc = "";
        String flag = "";

        HashMap map = new HashMap();
        HashMap ParametersHashMap = new HashMap();
        ArrayList Parameters = new ArrayList();
        ArrayList ParametersNames = new ArrayList();
        Container container = null;

        HashMap TimeRangeMap = new HashMap();
        ArrayList histTimeRange = new ArrayList();
        ArrayList scenarioTimeRange = new ArrayList();
        HashMap TableHashMap = null;
        LinkedHashMap TimeDimHashMap = new LinkedHashMap();
        ArrayList TimeDetailstList = new ArrayList();
        ArrayList repList = new ArrayList();
        String UserId = "";
        ArrayList measureArray = new ArrayList();
        ArrayList timeRangeDetails = new ArrayList();
        ArrayList timeRangeForYearAL = new ArrayList();


        if (session != null) {
            map = (HashMap) session.getAttribute("SCENARIOTAB");
            UserId = String.valueOf(session.getAttribute("USERID"));

            scenarioName = request.getParameter("scenarioName");
            scenarioDesc = request.getParameter("scenarioDesc");
            flag = request.getParameter("flag");

            container = (Container) map.get(scenarioName);
            ParametersHashMap = container.getParametersHashMap();
            TableHashMap = container.getTableHashMap();
            TimeRangeMap = container.getScenarioTimeRangeMap();
            String UserFolderIds = "";
 if (ParametersHashMap.get("ParametersNames") != null) {
                Parameters = (ArrayList) ParametersHashMap.get("Parameters");
                ParametersNames = (ArrayList) ParametersHashMap.get("ParametersNames");
                TimeDimHashMap = (LinkedHashMap) (HashMap) ParametersHashMap.get("TimeDimHashMap");
                TimeDetailstList = (ArrayList) ParametersHashMap.get("TimeDetailstList");
                repList = (ArrayList) ParametersHashMap.get("REP");
                ArrayList seededModels = (ArrayList) ParametersHashMap.get("SeededModels");
                UserFolderIds = (String) ParametersHashMap.get("UserFolderIds");
                timeRangeDetails = (ArrayList) ParametersHashMap.get("TimeRangeDetails");
            }
            if (TimeRangeMap.get("histTimeRange") != null) {
                histTimeRange = (ArrayList) TimeRangeMap.get("histTimeRange");
                scenarioTimeRange = (ArrayList) TimeRangeMap.get("scenarioTimeRange");
            }
            String qryEle = "";
            if (TableHashMap.get("Measures") != null) {
                measureArray = (ArrayList) TableHashMap.get("Measures");
                for (int y = 0; y < measureArray.size(); y++) {
                    qryEle = measureArray.get(y).toString();
                }
            }
            // insert in scenario master
            ScenarioDesigner scDeg = new ScenarioDesigner();
            int scenarioId = scDeg.generateScnId();
            String mNameQ = "select * from prg_user_sub_folder_elements where element_id in(" + qryEle + ")";
            PbDb pbdb = new PbDb();
            String mName = "";
            PbReturnObject mObj = pbdb.execSelectSQL(mNameQ);
            mName = mObj.getFieldValueString(0, "USER_COL_DESC");
            ScenarioParam scParam = new ScenarioParam();

            scParam.setScenarioDesc(scenarioDesc);
            scParam.setScenarioId(scenarioId);
            scParam.setQueryElementId(qryEle);
            scParam.setMeasureName(mName);
            scParam.setScenarioName(scenarioName);
            scParam.setBussRole(UserFolderIds);
            scParam.setScnStatus("Created");
            scParam.setUserId(UserId);
            scParam.setScenarioHistStartTime(histTimeRange.get(0).toString());
            scParam.setScenarioHistEndTime(histTimeRange.get(1).toString()); //scenarioTimeRange
            scParam.setScenarioStartTime(scenarioTimeRange.get(0).toString());
            scParam.setScenarioEndTime(scenarioTimeRange.get(1).toString());
           if (TimeDetailstList.get(0).toString().equalsIgnoreCase("Day") && TimeDetailstList.get(1).toString().equalsIgnoreCase("PRG_YEAR_RANGE")) {
                scParam.setScenarioMinTimeLevel("Day");
            } else if (TimeDetailstList.get(0).toString().equalsIgnoreCase("Month")) {
                scParam.setScenarioMinTimeLevel("Month");
            }
            ScenarioTemplateDAO scDao = new ScenarioTemplateDAO();
            Session sess = new Session();
            sess.setObject(scParam);
            scDao.addScenarioMaster(sess);
            scDeg.setParametersHashMap(ParametersHashMap);
            scDeg.setTableHashMap(TableHashMap);
            scDeg.setScenarioId(scenarioId);
            scDeg.setScenarioName(scenarioName);
            scDeg.setScenarioDesc(scenarioDesc);
            scDeg.createDocument();
            PbScenarioViewerBD scenarioViewerBD = new PbScenarioViewerBD();
            scenarioViewerBD = new PbScenarioViewerBD();
            scenarioViewerBD.prepareScenario(Integer.toString(scenarioId) , scenarioName,  TimeDetailstList.get(0).toString() , UserId, flag, request, response);
            request.setAttribute("scenarioName", scenarioName);
            request.setAttribute("scenarioId", scenarioId);
            return mapping.findForward("analyzeScenario");

        } else {
            return mapping.findForward("sessionExpired");
        }
    }

    public ActionForward getViewerScenarioMeasure(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession(false);
        String foldersIds = "";
        String result = "";
        Container container = null;
        String scenarioId = "";
        String scenarioName = "";
        String scenarioDesc = "";
        PbReturnObject retObj = null;
        HashMap map = new HashMap();
        if (session != null) {

//            map = (HashMap) session.getAttribute("SCENARIOTAB");
            scenarioId = request.getParameter("scenarioId");
            scenarioName = request.getParameter("scenarioName");
            scenarioDesc = request.getParameter("scenarioDesc");
            retObj = scnTemplateDAO.getFolderIdbyScenarioId(scenarioId);
            if (retObj.getRowCount() > 0) {
                foldersIds = retObj.getFieldValueString(0, 0);
                result = scnTemplateDAO.getMeasures(foldersIds);
            }
            request.setAttribute("Measures", result);
//            container = (Container) map.get(scenarioName);
//            container.setScenarioName(scenarioName);
//            container.setScenarioDesc(scenarioDesc);
            request.setAttribute("scenarioId", scenarioId);
            request.setAttribute("scenarioName", scenarioName);
            request.setAttribute("scenarioDesc", scenarioDesc);
            return mapping.findForward("scenarioViewermeasures");
        } else {
            return mapping.findForward("sessionExpired");
        }
    }
}
