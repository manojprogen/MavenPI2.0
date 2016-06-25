/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.rulesHelp;

import com.google.common.collect.Iterables;
import com.progen.portal.PortLet;
import com.progen.portal.Portal;
import com.progen.portal.PortalDAO;
import com.progen.portal.PortletXMLHelper;
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

public class RulesHelpAction extends LookupDispatchAction {

    public static Logger logger = Logger.getLogger(RulesHelpAction.class);
    /*
     * forward name="success" path=""
     */
    private final static String SUCCESS = "success";
    public LinkedHashMap<String, ArrayList<String>> reportParameters = new LinkedHashMap<String, ArrayList<String>>();

    /**
     * Provides the mapping from resource key to method name.
     *
     * @return Resource key / method name map.
     */
    protected Map getKeyMethodMap() {
        Map map = new HashMap();
        map.put("getDimentionsforRule", "getDimentionsforRule");
        map.put("getMeasureForRule", "getMeasureForRule");
        map.put("saveRule", "saveRule");
        map.put("getFilterDetails", "getFilterDetails");
        map.put("deleteRule", "deleteRule");
        map.put("getruleDimMembers", "getruleDimMembers");

        return map;
    }

    public ActionForward getDimentionsforRule(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        String fromModule = request.getParameter("fromModule");
        HttpSession session = request.getSession(false);
        PrintWriter printWriter = response.getWriter();
        String userId = (String) session.getAttribute("USERID");
        if (fromModule.equalsIgnoreCase("PORTAL")) {
            String portletId = request.getParameter("portletId");
            List<Portal> portals = (List<Portal>) session.getAttribute("PORTALS");
            StringBuilder outerBuffer = new StringBuilder("");
            PortletXMLHelper portletXMLHelper;
            ArrayList dets = new ArrayList();
            ArrayList detnames = new ArrayList();
            List<PortLet> portlets = null;
            PortLet portLet = null;
            for (Portal portal : portals) {
                portlets = portal.getPortlets();
                Iterator<PortLet> moduleIter = Iterables.filter(portlets, PortLet.getAccessPortletPredicate(Integer.parseInt(portletId))).iterator();
                if (moduleIter.hasNext()) {
                    portLet = moduleIter.next();
                    break;
                }

            }
            portletXMLHelper = portLet.getPortletXMLHelper();
            reportParameters.putAll(portletXMLHelper.getReportParameters());
            for (int i = 0; i < reportParameters.size(); i++) {
                dets.add(reportParameters.values().toArray()[i].toString().split(",")[0].replace("[", ""));
                detnames.add(reportParameters.values().toArray()[i].toString().split(",")[1]);
            }
            for (int i = 0; i < dets.size(); i++) {

                if (!dets.get(i).toString().equalsIgnoreCase("Time")) {
                    outerBuffer.append("<li class='navtitle-hover DimensionULClass' id='" + dets.get(i) + "'  style='width: 200px; height: auto; color: white;'>");
                    outerBuffer.append("<table><tr><td style='color: black;'>" + detnames.get(i) + "</td></tr></table>");
                    outerBuffer.append("</li>");
                }
            }
//              int folderId=portLet.getFolderId();
//         RulesHelpBD helpBD=new RulesHelpBD();
//         String dimentions=helpBD.getDimentionsforRule(folderId);
//         printWriter.print(dimentions);              
            printWriter.print(outerBuffer.toString());
        } else {
            String dinentionIDs = "";
            String dimentionNames = "";
            if (request.getParameter("dinentionIDs") != null) {
                dinentionIDs = request.getParameter("dinentionIDs");
            }
            if (request.getParameter("dimentionNames") != null) {
                dimentionNames = request.getParameter("dimentionNames");
            }
            String[] dimeIDs = dinentionIDs.split(",");
            String[] dimNames = dimentionNames.split(",");
            RulesHelpBD rulesHelpBD = new RulesHelpBD();
            String dimentions = rulesHelpBD.getDimentionsforRule(dimeIDs, dimNames);
            printWriter.print(dimentions);
            //write here your code for ur's rule
        }
        return mapping.findForward(SUCCESS);
    }

    public ActionForward getMeasureForRule(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        String fromModule = request.getParameter("fromModule");
        HttpSession session = request.getSession(false);
        PrintWriter printWriter = response.getWriter();
        String userId = (String) session.getAttribute("USERID");
        if (fromModule.equalsIgnoreCase("PORTAL")) {
            String portletId = request.getParameter("portletId");
            List<Portal> portals = (List<Portal>) session.getAttribute("PORTALS");
            List<PortLet> portlets = null;
            PortLet portLet = null;
            for (Portal portal : portals) {
                portlets = portal.getPortlets();
                Iterator<PortLet> moduleIter = Iterables.filter(portlets, PortLet.getAccessPortletPredicate(Integer.parseInt(portletId))).iterator();
                if (moduleIter.hasNext()) {
                    portLet = moduleIter.next();
                    break;
                }

            }
            int folderId = portLet.getFolderId();
            RulesHelpBD rulesHelpBD = new RulesHelpBD();
            String measureLis = rulesHelpBD.getMeasureForRule(folderId, request.getContextPath());

            printWriter.print(measureLis);
        }
        // ReportTemplateDAO
        return mapping.findForward(SUCCESS);
    }

    public ActionForward saveRule(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        PrintWriter printWriter = null;
        String userID = (String) request.getSession(false).getAttribute("USERID");
        String ruleType = request.getParameter("ruleType");
        String refId = request.getParameter("refId");
        String ruleName = request.getParameter("ruleName");
        String ruleDesc = request.getParameter("ruleDesc");
        String actualRuleString = request.getParameter("actualRuleArray");
        String displayRuleString = request.getParameter("displayRuleArray");
        String measureIds = request.getParameter("measureIds");
        String ruleOn = request.getParameter("ruleOn");
        String dimIDs = request.getParameter("dimIDs");
        String dimMembers = request.getParameter("dimMembersArray");

        ArrayList<String> paramList = new ArrayList<String>();
        paramList.add(ruleType);
        paramList.add(refId);
        paramList.add(ruleName);
        paramList.add(ruleDesc);
        paramList.add(actualRuleString);
        paramList.add(displayRuleString);
        paramList.add(measureIds);
        paramList.add(ruleOn);
        paramList.add(dimIDs);
        paramList.add(dimMembers);
        RulesHelpBD rulesHelpBD = new RulesHelpBD();
        boolean status = rulesHelpBD.saveRule(paramList);
        if (status == true && ruleType.equalsIgnoreCase("PORTAL")) {
            List<Portal> portals = (List<Portal>) request.getSession(false).getAttribute("PORTALS");
            PortLet portLet = null;
            Portal portal1 = null;
            int portalID = 0;
            for (Portal portal : portals) {
                ArrayList<PortLet> portLets = (ArrayList<PortLet>) portal.getPortlets();
                Iterator<PortLet> moduleIter = Iterables.filter(portLets, PortLet.getAccessPortletPredicate(Integer.parseInt(refId))).iterator();
                if (moduleIter.hasNext()) {
                    portLet = moduleIter.next();
                    portalID = portal.getPortalID();
                    portal1 = portal;
                    portal.setFilterflag(true);
                    break;
                }

            }
            PortalDAO portalDAO = new PortalDAO();
            List<PortLet> portLets = portalDAO.buildPortlet(portalID, Integer.parseInt(userID));
            portal1.setPortlets(portLets);
        }
        printWriter = response.getWriter();
        printWriter.print(status);
        return null;
    }

    public ActionForward getFilterDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        PrintWriter printWriter = null;
        String refid = request.getParameter("refID");
        RulesHelpBD rulesHelpBD = new RulesHelpBD();
        String filterJson = rulesHelpBD.getFilterDetails(refid);
        printWriter = response.getWriter();
        printWriter.print(filterJson);
        return null;
    }

    public ActionForward deleteRule(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        PrintWriter printWriter = null;
        String refid = request.getParameter("refId");
        String ruleType = request.getParameter("ruleType");
        RulesHelpBD rulesHelpBD = new RulesHelpBD();
        boolean status = rulesHelpBD.deleteRule(refid, ruleType);
        if (status == true && ruleType.equalsIgnoreCase("PORTAL")) {
            List<Portal> portals = (List<Portal>) request.getSession(false).getAttribute("PORTALS");
            PortLet portLet = null;
            for (Portal portal : portals) {
                ArrayList<PortLet> portLets = (ArrayList<PortLet>) portal.getPortlets();
                Iterator<PortLet> moduleIter = Iterables.filter(portLets, PortLet.getAccessPortletPredicate(Integer.parseInt(refid))).iterator();
                if (moduleIter.hasNext()) {
                    portLet = moduleIter.next();
                    portLet.setRuleOn("");
                    break;
                }

            }

            portLet.setWhereClause("");

        }
        printWriter = response.getWriter();
        printWriter.print(status);
        return null;
    }

    public ActionForward getruleDimMembers(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String element_id = request.getParameter("elementID");
        String path = request.getParameter("path");
        RulesHelpBD rulesHelpBD = new RulesHelpBD();
        String dimmemberHtml = rulesHelpBD.getruleDimMembers(element_id, path);
        try {
            response.getWriter().print(dimmemberHtml);
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }
        return null;
    }
}