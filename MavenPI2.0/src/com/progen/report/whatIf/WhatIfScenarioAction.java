/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.whatIf;

import com.google.common.base.Joiner;
import com.progen.query.RTMeasureElement;
import com.progen.reportdesigner.db.ReportTemplateDAO;
import java.io.IOException;
import java.io.PrintWriter;
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

/**
 *
 * @author sreekanth
 */
public class WhatIfScenarioAction extends LookupDispatchAction {

    public static Logger logger = Logger.getLogger(WhatIfScenarioAction.class);
    /*
     * forward name="success" path=""
     */
    private final static String SUCCESS = "success";

    protected Map getKeyMethodMap() {
        Map map = new HashMap();


        map.put("setWhatIfMeasures", "setWhatIfMeasures");
        map.put("whatIfPreViewAndSave", "whatIfPreViewAndSave");
        map.put("addWhatIfTargetMeasure", "addWhatIfTargetMeasure");
        map.put("editWhatIfTargetMeasure", "editWhatIfTargetMeasure");
        map.put("deleteWhatIfTargetMeasure", "deleteWhatIfTargetMeasure");
        map.put("renameWhatIfTargetMeasure", "renameWhatIfTargetMeasure");
        map.put("getWhatIfdetails", "getWhatIfdetails");
        map.put("editSenSitivity", "editSenSitivity");
        map.put("removeSenSitivity", "removeSenSitivity");
        map.put("clearWhatIf", "clearWhatIf");
        return map;
    }

    public ActionForward setWhatIfMeasures(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) {

        Container container = null;
        HashMap map = null;
        String reportId = request.getParameter("PbReportId");
        String measureListStr = request.getParameter("selectedMeasuresliast");
        String sensitivityFactor = request.getParameter("sensitivityFactor");
        String dependentMeasures = request.getParameter("dependentMeasures");
        String isStandardWhatifMeasures = request.getParameter("isStandardWhatifMeasures");
        String isStandardDependentMeasures = request.getParameter("isStandardDependentMeasures");
//        
//        
        List<String> isStddWhatifMeasuresList = Arrays.asList(isStandardWhatifMeasures.split(","));
        List<String> isStddDependentMeasuresList = Arrays.asList(isStandardDependentMeasures.split(","));
        HashMap<String, String> stdNonStdDetails = new HashMap<String, String>();
        HashMap<String, Double> senFactorHashMap = new HashMap();
        if (!sensitivityFactor.equalsIgnoreCase("0")) {
            sensitivityFactor = sensitivityFactor.substring(1);
            String[] senFaStrings = sensitivityFactor.split(",");
            for (String str : senFaStrings) {
                String[] tempStrings = str.split("~");
                senFactorHashMap.put(tempStrings[1], Double.parseDouble(tempStrings[0]));
            }
        }

//        
        String[] measureArray;
        String[] dependentMeasurArr;

        if (measureListStr.equals("")) {
            measureArray = new String[0];


        } else {
            measureArray = measureListStr.split(",");

        }
        if (dependentMeasures.equalsIgnoreCase("")) {
            dependentMeasurArr = null;
        } else {
            dependentMeasurArr = dependentMeasures.split(",");
        }

        ArrayList<String> measurelist = new ArrayList<String>();
        measurelist.addAll(Arrays.asList(measureArray));
        ArrayList<String> dependantMeasureList = new ArrayList<String>();
        if (dependentMeasurArr != null) {
            dependantMeasureList.addAll(Arrays.asList(dependentMeasurArr));
        }
        int i = 0;
        for (String WFmeasure : measurelist) {
            stdNonStdDetails.put(WFmeasure, isStddWhatifMeasuresList.get(i));
            i++;
        }
        i = 0;
        for (String depMeasure : dependantMeasureList) {
            stdNonStdDetails.put(depMeasure, isStddDependentMeasuresList.get(i));
            i++;
        }
        WhatIfScenarioBD whatIfScenarioBD = new WhatIfScenarioBD();
        PrintWriter out = null;
        try {
            out = response.getWriter();
            HttpSession session = request.getSession(false);
            if (session != null) {
                if (session.getAttribute("PROGENTABLES") != null) {
                    map = (HashMap) session.getAttribute("PROGENTABLES");
                    if (map.get(reportId) != null) {
                        container = (Container) map.get(reportId);
                    }
                }
            }
            if (container != null) {
                whatIfScenarioBD.setStdNonStdDetails(stdNonStdDetails);
                boolean needToUpdateContainer = whatIfScenarioBD.initializeWhatIf(container, measurelist, dependantMeasureList);
                if (senFactorHashMap.size() > 0) {
                    whatIfScenarioBD.updateSensitivity(container, senFactorHashMap);
                    needToUpdateContainer = true;
                }
                if (needToUpdateContainer) {
                    whatIfScenarioBD.performWhatIfInContainer(container);
                }
            }
            out.print("refresh");

        } catch (Exception e) {
            logger.error("Exception:", e);
        }


//addRunTimeColumn
        return null;
    }

    //  // whatIfPreViewAndSave
    public ActionForward whatIfPreViewAndSave(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) {
        String PbReportId = request.getParameter("PbReportId");
        String whatIfMeasureslist = request.getParameter("whatIfMeasuresliast");
        String whatIfRanges = request.getParameter("whatIfRanges");
        String buttCheck = request.getParameter("buttCheck");
        PrintWriter out = null;
        String[] whatIfMesStrings = whatIfMeasureslist.split(",");
        ArrayList<String> whatIfMesList = new ArrayList<String>();
        ArrayList<Double> whatIfRangesList = new ArrayList<Double>();
        String[] whatIfRangesArray = whatIfRanges.split(",");
        WhatIfScenarioBD whatIfScenarioBD = new WhatIfScenarioBD();
        Container container = null;
        HashMap map = null;
        try {
            HttpSession session = request.getSession(false);
            if (session != null) {
                if (session.getAttribute("PROGENTABLES") != null) {
                    map = (HashMap) session.getAttribute("PROGENTABLES");
                    if (map.get(PbReportId) != null) {
                        container = (Container) map.get(PbReportId);
                    }
                }
            }
            whatIfMesList.addAll(Arrays.asList(whatIfMesStrings));
            for (String range : whatIfRangesArray) {
                whatIfRangesList.add(Double.parseDouble(range));
            }
            whatIfScenarioBD.updateSlidersAndPerformWhatIf(container, whatIfMesList, whatIfRangesList);
            out = response.getWriter();

            if (buttCheck.equalsIgnoreCase("preview")) {
                out.print("1");
            } else {
                out.print("0");
            }
        } catch (Exception e) {
            logger.error("Exception:", e);
        }


//addRunTimeColumn
        return null;
    }

//    public ActionForward getProgenTableMeasures(ActionMapping mapping,
//            ActionForm form,
//            HttpServletRequest request,
//            HttpServletResponse response) throws java.lang.Exception {
//        
//        HttpSession session = request.getSession(false);
//        HashMap map = null;
//        String reportId = request.getParameter("reportId");
//        Container container = null;
//        HashMap tableHM = new HashMap();
//        ArrayList measuresList = new ArrayList();
//        ArrayList measuresNameList = new ArrayList();
//        WhatIfScenerioDAO whatIfScenerioDAO = new WhatIfScenerioDAO();
//        String resultMeasureStr = "";
////        PrintWriter out = response.getWriter();
//        try {
//            if (session != null) {
//                if (session.getAttribute("PROGENTABLES") != null) {
//                    map = (HashMap) session.getAttribute("PROGENTABLES");
//                    if (map.get(reportId) != null) {
//                        container = (Container) map.get(reportId);
//                        tableHM = container.getTableHashMap();
//                        measuresList = (ArrayList) tableHM.get("Measures");
//                        measuresNameList = (ArrayList) tableHM.get("MeasuresNames");
//                        String measuresListStr = measuresList.toString().replace("[", "").replace("]", "");
//                        String measuresNameListStr = measuresNameList.toString().replace("[", "").replace("]", "");
//
////                        resultMeasureStr=whatIfScenerioDAO.getProgenTableMeasures(measuresList,measuresNameList);
//                        
////                        out.print(measuresListStr + "~" + measuresNameListStr);
//                    }
//                }
//            }
//        } catch (Exception e) {
//            logger.error("Exception:",e);
//        }
//
//        return null;
//    }
    public ActionForward addWhatIfTargetMeasure(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

        HttpSession session = request.getSession(false);
        String PbReportId = request.getParameter("PbReportId");
        String whatIfTargetMeasuresname = request.getParameter("whatIfTargetMeasuresname");
        String whatIftrgtFormula = request.getParameter("whatIftrgtFormula");
        whatIftrgtFormula = whatIftrgtFormula.replace("~", "+");
        whatIftrgtFormula = whatIftrgtFormula.replace("^", "%");
        Container container = null;
        HashMap map = null;
        PrintWriter out = null;
//        
//        
//        
        try {
            out = response.getWriter();
            WhatIfScenarioBD scenarioBD = new WhatIfScenarioBD();
            if (session != null) {
                map = (HashMap) session.getAttribute("PROGENTABLES");
                if (map.get(PbReportId) != null) {
                    container = (Container) map.get(PbReportId);
                    if (container != null) {
                        scenarioBD.addTargetMeasure(container, whatIfTargetMeasuresname, whatIftrgtFormula);
                    }
                }
            }
            String dataJson = scenarioBD.editWhatIfTargetMeasure(container);
            out.print(dataJson);
        } catch (Exception e) {
            logger.error("Exception:", e);
        }

        return null;
    }

    public ActionForward editWhatIfTargetMeasure(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        PrintWriter out = null;
        HttpSession session = request.getSession(false);
        String reportId = request.getParameter("reportId");
        // 
        Container container = null;
        ArrayList<String> trgMesList = new ArrayList<String>();
        String trgtStr = "";
        try {
            out = response.getWriter();
            if (request.getSession(false) != null) {
                HashMap map = (HashMap) session.getAttribute("PROGENTABLES");
                if (map != null) {
                    container = (Container) map.get(reportId);
                }
            }
            WhatIfScenarioBD scenarioBD = new WhatIfScenarioBD();
            trgtStr = scenarioBD.editWhatIfTargetMeasure(container);

//            
            out.print(trgtStr);

        } catch (IOException ex) {
            logger.error("Exception:", ex);
        } finally {
            out.close();
        }
        return null;
    }

    public ActionForward deleteWhatIfTargetMeasure(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

        HttpSession session = request.getSession(false);
        String reportId = request.getParameter("reportID");
        String trgtId = request.getParameter("trgtName");
        Container container = null;
        PrintWriter out = null;
        try {
            out = response.getWriter();
            if (request.getSession(false) != null) {
                HashMap map = (HashMap) session.getAttribute("PROGENTABLES");
                if (map != null) {
                    container = (Container) map.get(reportId);
                    WhatIfScenarioBD scenarioBD = new WhatIfScenarioBD();
                    Iterable<String> targetMeasureLst = scenarioBD.removeWhatIfTargetMeasure(container, trgtId);
                    String targetMeasures = Joiner.on(",").join(targetMeasureLst);
                    out.print(targetMeasures);
                }
            }
        } catch (Exception e) {
            logger.error("Exception:", e);
        }

        return null;
    }

    public ActionForward renameWhatIfTargetMeasure(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

        String reportId = request.getParameter("reportId");
        String targetMeasId = request.getParameter("oldTrgtName");
        String measLabel = request.getParameter("newTrgtName");
        HttpSession session = request.getSession(false);
        PrintWriter out = null;
        Container container = null;
        try {
            out = response.getWriter();
            if (request.getSession(false) != null) {
                HashMap map = (HashMap) session.getAttribute("PROGENTABLES");
                container = (Container) map.get(reportId);
                WhatIfScenarioBD scenarioBD = new WhatIfScenarioBD();
                scenarioBD.renameWhatIfTargetMeasure(targetMeasId, measLabel, container);

            }
        } catch (Exception e) {
        }

        return null;
    }

    public ActionForward getWhatIfdetails(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String reportID = request.getParameter("PbReportId");
        PrintWriter out = null;
        HttpSession session = request.getSession(false);
        Container container = null;
        String datajson = "";
        try {
            out = response.getWriter();
            WhatIfScenarioBD scenarioBD = new WhatIfScenarioBD();

            if (request.getSession(false) != null) {
                HashMap map = (HashMap) session.getAttribute("PROGENTABLES");
                container = (Container) map.get(reportID);
                datajson = scenarioBD.getWhatIfdetails(container);

            }
            out.print(datajson);
        } catch (Exception e) {
            logger.error("Exception:", e);
        }


//         scenarioBD.getWhatIfMembers

        return null;
    }

    public ActionForward getWhatIfMembers(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        WhatIfScenarioBD scenarioBD = new WhatIfScenarioBD();
//         scenarioBD.getWhatIfMembers

        return null;
    }
    //getWhatIfdetails

    public ActionForward editSenSitivity(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String reportId = request.getParameter("repID");
        WhatIfScenarioBD scenarioBD = new WhatIfScenarioBD();
        Container container = null;
        PrintWriter out = null;
        try {
            out = response.getWriter();
            container = Container.getContainerFromSession(request, reportId);
            String senSitivityFactors = scenarioBD.editSenSitivity(container);
            out.print(senSitivityFactors);
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        } catch (NullPointerException exception) {
            logger.error("Exception:", exception);
        }
        return null;
    }

    public ActionForward removeSenSitivity(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

        String reportId = request.getParameter("repID");
        String keyfactor = request.getParameter("keyValue");

        Container container = null;
        container = Container.getContainerFromSession(request, reportId);
        WhatIfScenario whatIfScenario = null;
        WhatIfSensitivity sensitivity = null;
        if (container.getWhatIfScenario() != null) {
            whatIfScenario = container.getWhatIfScenario();
            sensitivity = whatIfScenario.getWhatIfSensitivity();
            sensitivity.removeSensitivity(keyfactor);
        }

        return null;
    }

    public ArrayList selectMeasuresForWhatIf(HttpServletRequest request, String PbReportId) {
        HttpSession session = request.getSession(false);
        HashMap map = null;
        Container container = null;
        ArrayList measuresList = new ArrayList();
        ArrayList measuresNameList = new ArrayList();

        ArrayList returnList = new ArrayList();
        ArrayList<String> selctedMesures = null;
        WhatIfScenario wfScenario = null;

        try {
            if (session != null) {
                if (session.getAttribute("PROGENTABLES") != null) {
                    map = (HashMap) session.getAttribute("PROGENTABLES");
                    if (map.get(PbReportId) != null) {
                        container = (Container) map.get(PbReportId);
                        if (container != null) {

                            measuresList = container.getTableDisplayMeasures();
                            measuresNameList = container.getReportMeasureNames();
                            for (int i = 0; i < measuresList.size(); i++) {
                                if (RTMeasureElement.isRunTimeMeasure((String) measuresList.get(i))) {
                                    measuresList.remove(i);
                                    measuresNameList.remove(i);
                                    i = 0;
                                }
                            }
                            wfScenario = container.getWhatIfScenario();
                            if (wfScenario != null) {
                                selctedMesures = wfScenario.getWhatIfMeasureList();

                            }
                        }

                        returnList.add(measuresList);
                        returnList.add(measuresNameList);
                        returnList.add(selctedMesures);

                    }
                }
            }
        } catch (Exception e) {
            logger.error("Exception:", e);
        }

        return returnList;
    }
    //kiran

    public ActionForward clearWhatIf(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String reportId = request.getParameter("reportId");
        HttpSession session = request.getSession(false);
        Container container = null;
        HashMap map = new HashMap();
        map = (HashMap) session.getAttribute("PROGENTABLES");
        container = (Container) map.get(reportId);
        ReportTemplateDAO rdao = new ReportTemplateDAO();
        rdao.clearWhatIf(reportId);
        container.setWhatIfScenario(null);
        return null;
    }
}
