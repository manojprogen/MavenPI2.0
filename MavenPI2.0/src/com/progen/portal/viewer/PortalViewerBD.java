/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.portal.viewer;

import com.google.common.collect.Iterables;
import com.google.gson.Gson;
import com.progen.dashboardView.db.DashboardViewerDAO;
import com.progen.portal.*;
import com.progen.portal.portlet.PortletProcessor;
import com.progen.report.KPIElement;
import com.progen.report.PbReportCollection;
import com.progen.report.display.util.NumberFormatter;
import com.progen.report.query.PbReportQuery;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;
import prg.db.Container;
import prg.db.PbDb;
import prg.db.PbReturnObject;
import utils.db.ProgenConnection;
import utils.db.ProgenParam;

/**
 *
 * @author Santhosh.k
 */
public class PortalViewerBD extends PbDb {

    public static Logger logger = Logger.getLogger(PortalViewerBD.class);
    private boolean isFxCharts = false;
    ResourceBundle resBundle;
    private SAXBuilder builder = new SAXBuilder();
    private Document document = null;
    private Element root = null;
    private String callFrom = "";
    private String drillElmentDetails = null;
    private String dirllOn = null;

    private ResourceBundle getResourceBundle() {
        if (this.resBundle == null) {
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                resBundle = new PortalViewerResBundleSqlServer();
            } else {
                resBundle = new PortalViewerResourceBundle();
            }
        }

        return resBundle;
    }

//    PortalViewerResourceBundle resBundle = (PortalViewerResourceBundle) ResourceBundle.getBundle("com.progen.portal.viewer.PortalViewerResourceBundle");
    //old code to delete start
    public void preparePortal(String portalId, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        HashMap tabs = new HashMap();
        ArrayList tabCount = new ArrayList();
        try {

            String getTabs = "SELECT distinct PORTAL_TAB_ID, PORTAL_TAB_NAME, PORTAL_ORDER FROM PRG_PORTAL_TAB_MASTER where PORTAL_ID=" + portalId + " order by PORTAL_TAB_ORDER";
            PbReturnObject pbrotabs = execSelectSQL(getTabs);

            for (int k = 0; k < pbrotabs.getRowCount(); k++) {
                ArrayList portletName = new ArrayList();
                ArrayList portletTabId = new ArrayList();
                ArrayList grpTitles = new ArrayList();
                ArrayList grpImagesList = new ArrayList();
                ArrayList xmlStr = new ArrayList();
                HashMap tabDetails = new HashMap();
                tabCount.add(pbrotabs.getFieldValueInt(k, 0));
                tabDetails.put("Id", pbrotabs.getFieldValueString(k, 0));
                tabDetails.put("Name", pbrotabs.getFieldValueString(k, 1));
                tabs.put(pbrotabs.getFieldValueInt(k, 0), tabDetails);

            }
            request.setAttribute("getTabsList", tabs);
            request.setAttribute("tabCount", tabCount);

        } catch (Exception e) {
            logger.error("Exception:", e);
        }
    }
    //old code to delete end
    //OLD COCE FOR DELETE START
//    public void preparePortalTab(String portalTabId, String portalTabName, String currDate, HttpServletRequest request) throws SQLException {
//
//        HashMap tabs = new HashMap();
//        ArrayList tabCount = new ArrayList();
//        StringBuffer tableBuffer = new StringBuffer("");
//        Connection con = null;
//        PortletProcessor pt = null;
//        String xmlDoc = null;
//        ArrayList imgArrayList = null;
//        ResultSet rs = null;
//        Statement st = null;
//        try {
//            con = ProgenConnection.getInstance().getConnection();
//            ArrayList portletName = new ArrayList();
//            ArrayList portletTabId = new ArrayList();
//            ArrayList cols = new ArrayList();
//            ArrayList seqs = new ArrayList();
//            ArrayList grpTitles = new ArrayList();
//            ArrayList grpImagesList = new ArrayList();
//            ArrayList grpcolumnnum = new ArrayList();
//            ArrayList grpseqnum = new ArrayList();
//            ArrayList portletid = new ArrayList();
//            HashMap tabDetails = new HashMap();
//            tabCount.add(portalTabId);
//            tabDetails.put("Id", portalTabId);
//            tabDetails.put("Name", portalTabName);
//            String getXmlQuery = " SELECT m.portlet_id, m.portlet_name,d.PORTLET_TAB_ID,d.column_num,d.sequence_num from PRG_PORTLETS_MASTER m,PRG_PORTAL_TAB_DETAILS d where d.PORTLET_TAB_ID=" + portalTabId + " and d.PORTLET_ID=m.PORTLET_ID ORDER by d.column_num,d.sequence_num";
////            
//            st = con.createStatement();
//            rs = st.executeQuery(getXmlQuery);
//            while (rs.next()) {
//                portletid.add(rs.getString(1));
//                portletName.add(rs.getString(2));
//                portletTabId.add(rs.getString(3));
//                cols.add(rs.getString(4));
//                seqs.add(rs.getString(5));
//            }
//            if (st != null) {
//                st.close();
//            }
//            if (rs != null) {
//                rs.close();
//            }
//            if (con != null) {
//                con.close();
//            }
//            tableBuffer.append("<div id='PortalColumn1_" + portalTabId + "' class=\"dropTabs column \">");
//
//            //tableBuffer.append("<td width=\"33%\" id=\"PortalColumn1\" valign=\"top\" class=\"column\">");
//
//            for (int k1 = 0; k1 < portletid.size(); k1++) {
//
//                if (String.valueOf(cols.get(k1)).equalsIgnoreCase("1")) {
//
//                    tableBuffer.append("<div  id=\"portlet-" + portletid.get(k1) + "\" class=\"portlet ui-widget ui-widget-content ui-helper-clearfix ui-corner-all\" style=\"width:420px;height:350px\">");
//                    tableBuffer.append("<div class=\"portlet-header portletHeader ui-corner-all\">");
//                    tableBuffer.append(portletName.get(k1));
//                    tableBuffer.append(" </div>");
//                    tableBuffer.append("<script type='text/javascript'>");
//                    tableBuffer.append("getPortletDetails('" + portletid.get(k1) + "','','','Top-10','','" + portalTabId + "','" + currDate + "');");
//                    tableBuffer.append(" </script>");
//                    tableBuffer.append(" </div>");
//                    // tableBuffer.append(" <br>");
//                }
//            }
//            tableBuffer.append("</div>");
//            //tableBuffer.append(" </td>");
//            //tableBuffer.append("<td width=\"33%\" id=\"PortalColumn2\" valign=\"top\" class=\"column\">");
//            tableBuffer.append("<div id='PortalColumn2_" + portalTabId + "' class=\"dropTabs column \">");
//            for (int k1 = 0; k1 < portletid.size(); k1++) {
//
//                if (String.valueOf(cols.get(k1)).equalsIgnoreCase("2")) {
//
//                    tableBuffer.append("<div  id=\"portlet-" + portletid.get(k1) + "\" class=\"portlet ui-widget ui-widget-content ui-helper-clearfix ui-corner-all\" style=\"width:420px;height:350px\">");
//                    tableBuffer.append("<div class=\"portlet-header portletHeader ui-corner-all\">");
//                    tableBuffer.append(portletName.get(k1));
//                    tableBuffer.append(" </div>");
//                    tableBuffer.append("<script type='text/javascript'>");
//                    tableBuffer.append("getPortletDetails('" + portletid.get(k1) + "','','','Top-10','','" + portalTabId + "','" + currDate + "');");
//
//                    tableBuffer.append(" </script>");
//                    tableBuffer.append(" </div>");
//                    //tableBuffer.append(" <br>");
//                }
//            }
//            tableBuffer.append("</div>");
//            //tableBuffer.append(" </td>");
//            //tableBuffer.append("<td width=\"33%\" id=\"PortalColumn3\" valign=\"top\" class=\"column\">");
//            tableBuffer.append("<div  id='PortalColumn3_" + portalTabId + "' class=\"dropTabs column \">");
//            for (int k1 = 0; k1 < portletid.size(); k1++) {
//
//                if (String.valueOf(cols.get(k1)).equalsIgnoreCase("3")) {
//
//                    tableBuffer.append("<div id=\"portlet-" + portletid.get(k1) + "\" class=\"portlet ui-widget ui-widget-content ui-helper-clearfix ui-corner-all\" style=\"width:420px;height:350px\">");
//                    tableBuffer.append("<div class=\"portlet-header portletHeader ui-corner-all\">");
//                    tableBuffer.append(portletName.get(k1));
//                    tableBuffer.append(" </div>");
//                    tableBuffer.append("<script type='text/javascript'>");
//
//                    tableBuffer.append("getPortletDetails('" + portletid.get(k1) + "','','','Top-10','','" + portalTabId + "','" + currDate + "');");
//                    tableBuffer.append(" </script>");
//                    tableBuffer.append(" </div>");
//                    // tableBuffer.append(" <br>");
//                }
//            }
//            tableBuffer.append("</div>");
//            tableBuffer.append("<div  id='PortalColumn4_" + portalTabId + "' class=\"dropTabs column \" style='width:0px'></div>");
//            //tableBuffer.append(" </td>");
//            //tableBuffer.append("<td width=\"0%\" valign=\"top\" class=\"column\" style=\"overflow:auto\"></td>");
//            request.setAttribute("portletID", portletid);
//            request.setAttribute("portalTabContent", tableBuffer);
//            request.setAttribute("portalTabId", portalTabId);
//            request.setAttribute("portalTabName", portalTabName);
//
//        } catch (Exception e) {
//            logger.error("Exception:",e);
//        } finally {
//            if (rs != null) {
//                rs.close();
//            }
//            if (con != null) {
//                con.close();
//            }
//        }
//
//    }
////OLD COCE FOR DELETE END

    public void preparePortalTab(int portalTabId, String currDate, HttpServletRequest request, String periodType) throws SQLException {

        HttpSession session = request.getSession(false);
        Portal portal = null;
        StringBuilder tableBuffer = new StringBuilder("");
        StringBuilder column1Builder = new StringBuilder("");
        StringBuilder column2Builder = new StringBuilder("");
        StringBuilder column3Builder = new StringBuilder("");
        ArrayList<Portal> portals = new ArrayList<Portal>();
        if (session != null) {
            portals = (ArrayList<Portal>) session.getAttribute("PORTALS");
            Iterator<Portal> moduleIter = Iterables.filter(portals, Portal.getAccessPortalPredicate(portalTabId)).iterator();
            if (moduleIter.hasNext()) {
                portal = moduleIter.next();
            }
        }
        List<PortLet> portLets = portal.getPortlets();
        column1Builder.append("<div id='PortalColumn1_" + portalTabId + "' class=\"dropTabs column \">");
        column2Builder.append("<div id='PortalColumn2_" + portalTabId + "' class=\"dropTabs column \">");
        column3Builder.append("<div id='PortalColumn3_" + portalTabId + "' class=\"dropTabs column \">");
        PortletXMLHelper portletXMLHelper = null;
        for (PortLet portLet : portLets) {
            portletXMLHelper = portLet.getPortletXMLHelper();
            if (portLet.getColumnNumber() == 1) {
                if (portletXMLHelper.getPortletRepType().equalsIgnoreCase("Table")) {
                    column1Builder.append("<div  id=\"portlet-" + portalTabId + "-" + portLet.getPortLetId() + "\" class=\"myDragTabs portlet ui-widget ui-widget-content ui-helper-clearfix ui-corner-all\" style=\"width:420px;border-color:white;height:" + portLet.getPortletHeight() + "px;overflow: auto;\">");
                } else {
                    column1Builder.append("<div  id=\"portlet-" + portalTabId + "-" + portLet.getPortLetId() + "\" class=\"myDragTabs portlet ui-widget ui-widget-content ui-helper-clearfix ui-corner-all\" style=\"width:420px;border-color:white;height:400px\">");
                }
                column1Builder.append("<div class=\"portlet-header portletHeader ui-corner-all\">");
                column1Builder.append(portLet.getPortLetName());
                column1Builder.append(" </div>");
                column1Builder.append("<script type='text/javascript'>");
                column1Builder.append("getPortletDetails('" + portLet.getPortLetId() + "','','','" + portLet.getSortOrder() + "','','" + portalTabId + "','" + currDate + "','" + periodType + "','');");
                column1Builder.append(" </script>");
                column1Builder.append(" </div>");
            } else if (portLet.getColumnNumber() == 2) {
                if (portletXMLHelper.getPortletRepType().equalsIgnoreCase("Table")) {
                    column2Builder.append("<div  id=\"portlet-" + portalTabId + "-" + portLet.getPortLetId() + "\" class=\"myDragTabs portlet ui-widget ui-widget-content ui-helper-clearfix ui-corner-all\" style=\"width:420px;border-color:white;height:" + portLet.getPortletHeight() + "px;overflow: auto;\">");
                } else {
                    column2Builder.append("<div  id=\"portlet-" + portalTabId + "-" + portLet.getPortLetId() + "\" class=\"myDragTabs portlet ui-widget ui-widget-content ui-helper-clearfix ui-corner-all\" style=\"width:420px;border-color:white;height:400px\">");
                }
                column2Builder.append("<div class=\"portlet-header portletHeader ui-corner-all\">");
                column2Builder.append(portLet.getPortLetName());
                column2Builder.append(" </div>");
                column2Builder.append("<script type='text/javascript'>");
                column2Builder.append("getPortletDetails('" + portLet.getPortLetId() + "','','','" + portLet.getSortOrder() + "','','" + portalTabId + "','" + currDate + "','" + periodType + "','');");
                column2Builder.append(" </script>");
                column2Builder.append(" </div>");
            } else if (portLet.getColumnNumber() == 3) {
                if (portletXMLHelper.getPortletRepType().equalsIgnoreCase("Table")) {
                    column3Builder.append("<div  id=\"portlet-" + portalTabId + "-" + portLet.getPortLetId() + "\" class=\"myDragTabs portlet ui-widget ui-widget-content ui-helper-clearfix ui-corner-all\" style=\"width:420px;border-color:white;height:" + portLet.getPortletHeight() + "px;overflow: auto;\">");
                } else {
                    column3Builder.append("<div  id=\"portlet-" + portalTabId + "-" + portLet.getPortLetId() + "\" class=\"myDragTabs portlet ui-widget ui-widget-content ui-helper-clearfix ui-corner-all\" style=\"width:420px;border-color:white;height:400px\">");
                }
                column3Builder.append("<div class=\"portlet-header portletHeader ui-corner-all\">");
                column3Builder.append(portLet.getPortLetName());
                column3Builder.append(" </div>");
                column3Builder.append("<script type='text/javascript'>");
                column3Builder.append("getPortletDetails('" + portLet.getPortLetId() + "','','','" + portLet.getSortOrder() + "','','" + portalTabId + "','" + currDate + "','" + periodType + "','');");
                column3Builder.append(" </script>");
                column3Builder.append(" </div>");


            }

        }
        column1Builder.append("</div>");
        column2Builder.append("</div>");
        column3Builder.append("</div>");
        tableBuffer.append(column1Builder.toString()).append(column2Builder).append(column3Builder);
        tableBuffer.append("<div  id='PortalColumn4_" + portalTabId + "' class=\"column dropTabs\" style='width:0px'></div>");
        request.setAttribute("portalTabContent", tableBuffer);
        request.setAttribute("portalTabId", portal.getPortalID());
        request.setAttribute("portalTabName", portal.getPortalName());
    }

    public void preparePortletContent(String portletId, HttpServletRequest request, HttpServletResponse response, String REP, String CEP, String perBy, String gpType, String currDate, String portalTabId, String periodType) throws SQLException {

        HttpSession session = request.getSession(false);
        ArrayList<Portal> portals = new ArrayList<Portal>();
        if (session != null) {
            portals = (ArrayList<Portal>) session.getAttribute("PORTALS");
        }
        Portal portal = null;
        PortLet portLet = null;
        Iterator<PortLet> moduleIter = null;
        for (Portal port : portals) {
            moduleIter = Iterables.filter(port.getPortlets(), PortLet.getAccessPortletPredicate(Integer.parseInt(portletId))).iterator();
            if (moduleIter.hasNext()) {
                portLet = moduleIter.next();
                portal = port;
                if (perBy.equalsIgnoreCase("") && portLet.getSortOrder().equalsIgnoreCase("")) {
                    portLet.setSortOrder(perBy);
                } else if (!perBy.equalsIgnoreCase("")) {
                    portLet.setSortOrder(perBy);
                }
                if (portal.getPortalID() == Integer.parseInt(portalTabId)) {
                    break;
                }
            }
        }
        if (perBy.equalsIgnoreCase("") && portLet != null) {
            perBy = portLet.getSortOrder();
        }
        ArrayList tabCount = new ArrayList();
        StringBuffer tableBuffer = new StringBuffer("");
        PortletProcessor portalProcessor = new PortletProcessor();
        portalProcessor.setIfFxCharts(isIsFxCharts());
        ProgenParam pparam = new ProgenParam();
        if (portLet != null && portLet.getWhereClause() != null) {
            portalProcessor.setWhereClause(portLet.getWhereClause());
        }
        if (portLet != null && portLet.getRuleOn() != null) {
            portalProcessor.setRuleOn(portLet.getRuleOn());
        }
        if (portLet != null && portLet.getReportParams() != null) {
            portalProcessor.setReportParams(portLet.getReportParams());
        }
        if (portLet != null && portLet.getMeasureID() != null) {
            portalProcessor.setMeasureIDs(portLet.getMeasureID());
        }
        if (portLet != null && portLet.getAggreGationType() != null) {
            portalProcessor.setAggreGationType(portLet.getAggreGationType());
        }
        if (portLet != null && portLet.getMeasureNames() != null) {
            portalProcessor.setMeasureNames(portLet.getMeasureNames());
        }
        if (portLet != null && portLet.getGraphProperty() != null) {
            portalProcessor.setPortletProperty(portLet.getGraphProperty());
        }
        if (drillElmentDetails != null) {
            portalProcessor.setDrillElmentDetails(getDrillElmentDetails());
        }
        if (dirllOn != null) {
            portalProcessor.setDrillOn(dirllOn);
        }
        try {
            String portletType = "";
            tabCount.add(portletId);

            String value = "";
            String valu = "";
            String mont = "";
            String CurrValue = "";
            String ddformat = null;
            if (session.getAttribute("dateFormat") != null) {
                ddformat = session.getAttribute("dateFormat").toString();
            }
            String fromModule = "";
            fromModule = (String) session.getAttribute("ONEVIEW");

            if (currDate.equalsIgnoreCase("undefined") || currDate == "undefined" || currDate == "") {
                if (session.getAttribute("dateCurr") != null) {
                    currDate = (String) session.getAttribute("dateCurr");
                } else {
                    currDate = pparam.getdateforpage();
                }
                if (!"ONEVIEW".equals(fromModule)) {
                    value = currDate;
                    if (ddformat == null && !currDate.equalsIgnoreCase("")) {
                        int slashval = value.indexOf("/");
                        int slashLast = value.lastIndexOf("/");
                        valu = value.substring(0, slashval);
                        mont = value.substring(slashval + 1, slashLast + 1);
                        CurrValue = mont.concat(valu).concat(value.substring(slashLast));
                        currDate = CurrValue;
                    } else if (ddformat != null && ddformat.equalsIgnoreCase("dd/mm/yy")) {
                        int slashval = value.indexOf("/");
                        int slashLast = value.lastIndexOf("/");
                        valu = value.substring(0, slashval);
                        mont = value.substring(slashval + 1, slashLast + 1);
                        CurrValue = mont.concat(valu).concat(value.substring(slashLast));
                        currDate = CurrValue;
                    }
                }
            }
//                if(!"ONEVIEW".equals(fromModule)){
//                if(ddformat==null && !currDate.equalsIgnoreCase("")){
//                     int slashval=value.indexOf("/");
//                     int slashLast=value.lastIndexOf("/");
//                     valu=value.substring(0, slashval);
//                     mont=value.substring(slashval+1, slashLast+1);
//                     CurrValue=mont.concat(valu).concat(value.substring(slashLast));
//                   currDate= CurrValue;
//                }
//                else if(ddformat!=null && ddformat.equalsIgnoreCase("dd/mm/yy")){
//                     int slashval=value.indexOf("/");
//                     int slashLast=value.lastIndexOf("/");
//                     valu=value.substring(0, slashval);
//                     mont=value.substring(slashval+1, slashLast+1);
//                     CurrValue=mont.concat(valu).concat(value.substring(slashLast));
//                   currDate= CurrValue;
//                }
//                }
//                else if(ddformat!=null && ddformat.equalsIgnoreCase("mm/dd/yy")){
//                     int slashval=value.indexOf("/");
//                     int slashLast=value.lastIndexOf("/");
//                     valu=value.substring(0, slashval);
//                     mont=value.substring(slashval+1, slashLast+1);
//                     CurrValue=mont.concat(valu).concat(value.substring(slashLast));
//                   currDate= CurrValue;
//                }
            if (portLet != null && portLet.getPortletType() != null) {
                portletType = portLet.getPortletType();
            }
            if (portletType != null && portLet != null && portLet.getXMLDocument() != null) {
                if (portLet.getXMLDocument() != null && portletType.equalsIgnoreCase("I")) {
                    portalProcessor.setXmlDocument(portLet.getXMLDocument());
                    portalProcessor.setPortLet(portLet);
                    if (!portalTabId.equalsIgnoreCase("-1")) {
                        tableBuffer = portalProcessor.processProgenDataPortlet(request, response, session, REP, CEP, portletId, portLet.getSortOrder(), gpType, Integer.toString(portal.getPortalID()), portLet.getPortLetName(), currDate, periodType);
                    } else {
                        tableBuffer = portalProcessor.processProgenDataPortlet(request, response, session, REP, CEP, portletId, portLet.getSortOrder(), gpType, Integer.toString(-1), portLet.getPortLetName(), currDate, periodType);
                    }
                } else if (portLet.getXmlString() != null && portletType.equalsIgnoreCase("E")) {

                    tableBuffer.append("<div class=\"portlet-header portletHeader ui-corner-all\">");//header div starts
                    tableBuffer.append("<table style='height:10px'>");//header table

                    tableBuffer.append(" <Tr valign='top'>");
                    //tableBuffer.append("<td align='left' style='color:#369;font-weight:bold'>" + rs.getString(2) + "</td>");
                    tableBuffer.append("<a href='javascript:void(0)' onclick=\"editPortletName('" + portletId + "','" + portal.getPortalID() + "','" + portLet.getPortLetName() + "')\"  style='text-decoration:none' class=\"calcTitle\" title=\"Edit Name\"><font size=\"1px\"><span id='" + portal.getPortalID() + "_span'><b>" + portLet.getPortLetName() + "</b> </span></font></a>");

                    tableBuffer.append("<td align='right'><a style='text-decoration:none' href=\"javascript:deletePortlet('" + portletId + "','" + portLet.getPortLetName() + "','" + portal.getPortalID() + "')\" title='Delete '" + portLet.getPortLetName() + "' ><font size=\"1px\"><b>Delete</b></font></a>");

                    tableBuffer.append("</Tr>");
                    tableBuffer.append("</table>");
                    tableBuffer.append("</div>");

                    tableBuffer.append("<div style=\"width:410px;height:330px;overflow-y:auto;overflow-x:auto\">");
                    tableBuffer.append("<table width=\"100%\">");
                    tableBuffer.append(" <tr valign='top'>");
                    tableBuffer.append("<td valign='top' style=\"color:#369;font-weight:bold\">");
                    tableBuffer.append("<iframe id='iframe-" + portletId + "' frameborder=0 style='width:100%;height:300px;overflow:auto' src='pbExternalPortlet.jsp?portletId=" + portletId + "'></iframe>");

                    tableBuffer.append(" </td>");
                    //tableBuffer.append(" <td valign='top' align=\"right\"> </td>");
                    tableBuffer.append("</tr>");
                    tableBuffer.append("</table>");
                    tableBuffer.append("</div>");
                    tableBuffer.append("</Table>");//closing of inner table
                    tableBuffer.append(" </div>");//inner div 1 ends
                } else {
                    tableBuffer = new StringBuffer("");

                    //tableBuffer.append("<div id=\"portlet-" + portletId + "\" class=\"portlet ui-widget ui-widget-content ui-helper-clearfix ui-corner-all\">");

                    //header div sytarts
                    tableBuffer.append("<div class=\"portlet-header portletHeader ui-corner-all\" >");
                    tableBuffer.append("<table  style='height:10px;width:100%'>");
                    tableBuffer.append("<tr valign='top' align='right'>");
                    tableBuffer.append("<td align=\"left\" style='color:#369;font-weight:bold'>");
                    //tableBuffer.append(rs.getString(2));
                    tableBuffer.append("<a href='javascript:void(0)' onclick=\"editPortletName('" + portletId + "','" + portal.getPortalID() + "','" + portLet.getPortLetName() + "')\"  style='text-decoration:none' class=\"calcTitle\" title=\"Edit Name\"><font size=\"1px\"><span id='" + portal.getPortalID() + "_span'><b>" + portLet.getPortLetName() + "</b> </span> </font></a>");

                    tableBuffer.append("</td>");
                    tableBuffer.append("<td align=\"right\"> ");
                    //links code
                    tableBuffer.append("<table border='0'>");
                    tableBuffer.append("<tr><td>&nbsp;&nbsp;</td>");
                    tableBuffer.append("<td align='right'>");
                    tableBuffer.append("<a style='text-decoration:none' href='javascript:void(0)' onclick=\"openTablesDiv('Table-" + portletId + "')\" title='Table'><font size=\"1px\"><b>Table</font></a>");
                    tableBuffer.append("<div style='display:none;width:auto;height:auto;background-color:#ffffff;overflow:auto;position:absolute;text-align:left;border:1px solid #000000;border-top-width: 0px;' id ='Table-" + portletId + "'>");

                    tableBuffer.append("<Table>");
                    tableBuffer.append("<Tr valign='top'>");
                    tableBuffer.append("<Td valign='top'>");
                    tableBuffer.append("<a style='text-decoration:none' href=\"javascript:getPortletTableTemplate('" + portletId + "','" + portLet.getPortLetName() + "','" + portLet.getPortLetDes() + "','" + portal.getPortalID() + "')\" title='Standard table'><font size=\"1px\"><b>Standard Table</font></a>");
                    tableBuffer.append("</Td>");
                    tableBuffer.append("</Tr>");
                    tableBuffer.append("<Tr>");
                    tableBuffer.append("<Td>");
                    tableBuffer.append("<a style='text-decoration:none' href=\"javascript:getPortletKPITableTemplate('" + portletId + "','" + portLet.getPortLetName() + "','" + portLet.getPortLetDes() + "','" + portal.getPortalID() + "')\" title='kpi'><font size=\"1px\"><b>kpi</font></a>");
                    tableBuffer.append("</Td>");
                    tableBuffer.append("</Tr>");
                    tableBuffer.append("<Tr>");
                    tableBuffer.append("<Td>");
                    tableBuffer.append("<a style='text-decoration:none' href=\"javascript:getPortletKPITableTemplate('" + portletId + "','" + portLet.getPortLetName() + "','" + portLet.getPortLetDes() + "','" + portal.getPortalID() + "')\" title='BasicTarget'><font size=\"1px\"><b>BasicTarget</font></a>");
                    tableBuffer.append("</Td>");
                    tableBuffer.append("</Tr>");
                    tableBuffer.append("</Table>");

                    tableBuffer.append("</div>");
                    tableBuffer.append("</td>&nbsp;");

                    tableBuffer.append("<td align='right'>");
                    tableBuffer.append("<a style='text-decoration:none' href='javascript:void(0)' onclick=\"openGraphsDiv('Graph-" + portletId + "')\" title='Graph'><font size=\"1px\"><b>Graph</font></a>");
                    tableBuffer.append("<div style='display:none;width:auto;height:auto;background-color:#ffffff;overflow:auto;position:absolute;text-align:left;border:1px solid #000000;border-top-width: 0px;' id ='Graph-" + portletId + "'>");

                    tableBuffer.append("<Table>");
                    tableBuffer.append("<Tr valign='top'>");
                    tableBuffer.append("<Td valign='top'>");
                    tableBuffer.append("<a style='text-decoration:none' href=\"javascript:getPortletGraphTemplate('" + portletId + "','" + portLet.getPortLetName() + "','" + portLet.getPortLetDes() + "','" + portal.getPortalID() + "')\" title='Standard Graph'><font size=\"1px\"><b>Standard Graph</font></a>");
                    tableBuffer.append("</Td>");
                    tableBuffer.append("</Tr>");
                    tableBuffer.append("<Tr>");
                    tableBuffer.append("<Td>");
                    tableBuffer.append("<a style='text-decoration:none' href=\"javascript:getPortletKPIGraphTemplate('" + portletId + "','" + portLet.getPortLetName() + "','" + portLet.getPortLetDes() + "','" + portal.getPortalID() + "')\" title='KPI Graph'><font size=\"1px\"><b>KPI Graph</font></a>");
                    tableBuffer.append("</Td>");
                    tableBuffer.append("</Tr>");
                    tableBuffer.append("</Table>");
                    tableBuffer.append("</div>");


                    tableBuffer.append("</td>&nbsp");
                    tableBuffer.append("<td align='right'>");
                    tableBuffer.append("<a style='text-decoration:none' href=\"javascript:checkGedgets('" + portletId + "','" + portLet.getPortLetName() + "','" + portLet.getPortLetDes() + "','" + portal.getPortalID() + "')\" title='Gadgets'><font size=\"1px\"><b>Gadgets</b></font></a>");
                    tableBuffer.append("</td>&nbsp;");
                    tableBuffer.append("<td align='right'>");
                    tableBuffer.append("<a style='text-decoration:none' href=\"javascript:deletePortlet('" + portletId + "','" + portLet.getPortLetName() + "','" + portal.getPortalID() + "')\" title='Delete '" + portLet.getPortLetName() + "' ><font size=\"1px\"><b>Delete</b></font></a>");
                    tableBuffer.append("<div style='display:none;width:auto;height:auto;background-color:#ffffff;overflow:auto;position:absolute;text-align:left;border:1px solid #000000;border-top-width: 0px;' id ='Gedget-" + portletId + "'>");
                    tableBuffer.append("<Table>");
                    tableBuffer.append("<Tr valign='top'>");
                    tableBuffer.append("<Td valign='top'>");
                    tableBuffer.append("<input type='radio' name='Gedget" + portletId + "' id='Gedgetc" + portletId + "' value='Clock'");
                    tableBuffer.append("<b><font size='2px'>Clock<font size='2px'></b>");
                    tableBuffer.append("</Td>");
                    tableBuffer.append("</Tr>");
                    tableBuffer.append("<Tr>");
                    tableBuffer.append("<Td>");
                    tableBuffer.append("<input type='radio' name='Gedget" + portletId + "' id='Gedgetc" + portletId + "' value='Calender'");
                    tableBuffer.append("<b><font size='2px'>Calender<font size='2px'></b>");
                    tableBuffer.append("</Td>");
                    tableBuffer.append("</Tr>");
                    tableBuffer.append("<Tr>");
                    tableBuffer.append("<Td>");
                    tableBuffer.append("<input type='radio' name='Gedget" + portletId + "' id='Gedgetc" + portletId + "' value='Msgbox'");
                    tableBuffer.append("<b><font size='2px'>Message Box<font size='2px'></b>");
                    tableBuffer.append("</Td>");
                    tableBuffer.append("</Tr>");
                    tableBuffer.append("</Table>");
                    tableBuffer.append("</div>");//closing of gedget div
                    tableBuffer.append("</td>&nbsp;");

                    tableBuffer.append("</td>");
                    tableBuffer.append("</tr>");
                    tableBuffer.append("</table>");
                    //links code ends here

                    tableBuffer.append("</td>");
                    tableBuffer.append("</tr>");
                    tableBuffer.append("</table>");
                    tableBuffer.append(" </div>");
                    //header div ends

                    //content div starts

                    tableBuffer.append("<div style=\"width:410px;height:330px;overflow-y:auto;overflow-x:auto\">");
                    tableBuffer.append("<table width=\"100%\">");
                    tableBuffer.append("<tr valign='top'>");
                    tableBuffer.append("<td valign='top' align='left'></td>");
                    tableBuffer.append("<td valign='top' align=\"right\"> </td>");
                    tableBuffer.append("</tr>");
                    tableBuffer.append("</table>");
                    tableBuffer.append("</div>");
                }
            }



            response.getWriter().print(tableBuffer.toString());
        } catch (Exception e) {
            logger.error("Exception:", e);
        }
    }

    public boolean savePortletOrder(HashMap hmap, String[] keys, String tabId, HttpServletRequest request) {
        String sqlStr = "update PRG_PORTAL_PORTLETS_ASSIGN set column_num='&', sequence_num='&' where PORTLET_ID='&' and PORTL_ID='&'";
        ArrayList alist = new ArrayList();
        Object[] values = new Object[4];
        String finalQuery = "";
        String[] PortletIds = null;
        boolean result = false;
        List<Portal> portals = (List<Portal>) request.getSession(false).getAttribute("PORTALS");
        List<PortLet> portlets = null;
        List<PortLet> portlets1 = new ArrayList<PortLet>();
        PortLet portLet = null;

        for (int i = 0; i < keys.length; i++) {
            values[0] = keys[i];
            values[3] = tabId;
            if (hmap.get(keys[i]) != null) {
                PortletIds = String.valueOf(hmap.get(keys[i])).replace("portlet-" + tabId + "-", "").split(",");
                for (int j = 0; j < PortletIds.length; j++) {
                    if (PortletIds[j] != null && !"".equalsIgnoreCase(PortletIds[j])) {
                        for (Portal portal : portals) {
                            portlets = portal.getPortlets();
                            Iterator<PortLet> moduleIter = Iterables.filter(portlets, PortLet.getAccessPortletPredicate(Integer.parseInt(PortletIds[j]))).iterator();
                            if (moduleIter.hasNext()) {
                                portLet = moduleIter.next();
                                break;
                            }

                        }
                        values[1] = (j + 1);
                        values[2] = PortletIds[j];
                        finalQuery = buildQuery(sqlStr, values);
                        alist.add(finalQuery);
                        portLet.setColumnNumber(Integer.parseInt(keys[i]));
                        portLet.setSeqnumber(j + 1);
                        portlets1.add(portLet);
                    }
                }
            }

        }
        for (Portal portal : portals) {
            Iterator<Portal> moduleIter = Iterables.filter(portals, Portal.getAccessPortalPredicate(Integer.parseInt(tabId))).iterator();
            if (moduleIter.hasNext()) {
                portal = moduleIter.next();
                portal.setPortlets(portlets1);
                break;
            }
        }
        if (alist.size() != 0) {
            try {
                result = executeMultiple(alist);
            } catch (Exception exp) {
                logger.error("Exception:", exp);

            }


        }
        return result;
    }

    public boolean deletePortlet(String portletId, String portalTabId) {
        String sqlStr = "delete from PRG_PORTAL_PORTLETS_ASSIGN  WHERE  PORTLET_ID=& and PORTL_ID=&";
        String delePortlet = "DELETE FROM PRG_USER_PORTLETS_MASTER WHERE PORTAL_ID=" + portalTabId + "  AND PORTLET_ID=" + portletId;

        ArrayList alist = new ArrayList();
        String finalQuery = "";
        boolean result = false;
        Object[] values = new Object[2];

        values[0] = portletId;
        values[1] = portalTabId;
        finalQuery = buildQuery(sqlStr, values);
        alist.add(finalQuery);
        alist.add(delePortlet);
        if (!alist.isEmpty()) {
            try {

                result = executeMultiple(alist);
            } catch (Exception exp) {
                logger.error("Exception:", exp);
            }
        }
        return result;

    }

    public String displayKPIGraphRegion(String KPIS, Container container) {
        StringBuffer graphBuffer = new StringBuffer("");

        PbReportQuery repQuery = new PbReportQuery();
        PbReturnObject pbretObj = new PbReturnObject();

        PbReturnObject retObj = null;


        HashMap ParametersMap = null;
        HashMap TableMap = null;
        ArrayList Parameters = null;
        ArrayList TimeDetails = null;
        ArrayList reportRowViewbyValues = null;
        ArrayList reportColViewbyValues = new ArrayList();
        ArrayList qryColumns = null;
        String query = getResourceBundle().getString("displayKPIGraphRegion");
        Object[] values = new Object[1];
        values[0] = KPIS;
        ArrayList columnAggr = new ArrayList();
        long diffDays = 0;

        try {
            query = buildQuery(query, values);
            retObj = execSelectSQL(query);
            ParametersMap = container.getParametersHashMap();
            TableMap = container.getTableHashMap();

            Parameters = (ArrayList) ParametersMap.get("Parameters");
            TimeDetails = (ArrayList) ParametersMap.get("TimeDetailstList");

            if (TableMap != null && TableMap.size() != 0) {
                if (TableMap.get("REP") != null) {
                    reportRowViewbyValues = (ArrayList) TableMap.get("REP");
                }
            }
            if (reportRowViewbyValues == null) {
                reportRowViewbyValues = new ArrayList();
                reportRowViewbyValues.add(Parameters.get(0));
            }
            qryColumns = new ArrayList();
            qryColumns.add(KPIS);

            columnAggr.add(retObj.getFieldValueString(0, 0));
            repQuery.setRowViewbyCols(reportRowViewbyValues);
            repQuery.setColViewbyCols(reportColViewbyValues);

            repQuery.setQryColumns(qryColumns);
            repQuery.setColAggration(columnAggr);
            repQuery.setTimeDetails(TimeDetails);

            repQuery.setDefaultMeasure(KPIS);
            repQuery.setDefaultMeasureSumm(String.valueOf(retObj.getFieldValueString(0, 0)));
            repQuery.isKpi = true;
            pbretObj = repQuery.getPbReturnObject(KPIS);
            diffDays = getDayDifference(ParametersMap);
            long perDay = 0;

            if (pbretObj != null) {
                perDay = (Long.parseLong(String.valueOf(pbretObj.getFieldValueInt(0, 1)))) / diffDays;
                //start build of kpi graph regions
                graphBuffer.append("<input type='hidden' name='needleStr' id='needleStr' value='" + pbretObj.getFieldValueInt(0, 1) + "'> ");
                graphBuffer.append("<input type='hidden' name='measName' id='measName' value='" + retObj.getFieldValueString(0, 2) + "'> ");
                graphBuffer.append("<input type='hidden' name='noOfDays' id='noOfDays' value='" + String.valueOf(diffDays) + "'> ");
                graphBuffer.append("<input type='hidden' name='perDay' id='perDay' value='" + String.valueOf(perDay) + "'> ");

                graphBuffer.append("<table style='width:100%;'  align='center' >");
                graphBuffer.append("<tr>");
                graphBuffer.append("<td>Graph Type</td>");
                graphBuffer.append("<td>");
                graphBuffer.append("<Select name=\"kpigrpTypes\" STYLE=\"width:150px\" class=\"myTextbox3\" id=\"kpigrpTypes\" >");
                graphBuffer.append("<Option  selected  value=\"Meter\">Meter</Option>");
                graphBuffer.append("<Option value=\"Thermometer\">Thermometer</Option>");
                graphBuffer.append("</Select>");
                graphBuffer.append("</td>");
                graphBuffer.append("</tr>");
                graphBuffer.append("<Tr>");
                graphBuffer.append("<Td id='needleValue'><font size=\"1px\"><b>" + retObj.getFieldValueString(0, 2) + "</b> Value is : " + pbretObj.getFieldValueInt(0, 1) + "</font></Td>");
                graphBuffer.append(" </Tr>");
                graphBuffer.append("</table>");

                graphBuffer.append("<table style='width:100%;' border='solid black ' align='center' >");
                graphBuffer.append("<Tr>");
                graphBuffer.append("<td ><font size='1px'><b>Define Risk Ranges</b></font></td>");
                graphBuffer.append("<Td><font size='1px'><b>Operator</b></font></Td>");
                graphBuffer.append("<Td id='devId'><font size='1px'><b>Deviation % </b></font></Td>");
                graphBuffer.append(" </Tr>");
                graphBuffer.append(" <Tr>");
                graphBuffer.append("<Td id='r1Text'>High Risk</Td>");
                graphBuffer.append("<Td>");
                graphBuffer.append("<Select name='highRisk' STYLE='width:100px' class='myTextbox3' id='highRisk' onchange='addHighRisk(this)'>");
                graphBuffer.append(" <Option value=\">\">></Option>");
                graphBuffer.append(" <Option value=\"<\"><</Option>");
                graphBuffer.append("<Option value=\">=\">>=</Option>");
                graphBuffer.append(" <Option value=\"<=\"><=</Option>");
                graphBuffer.append("<Option value=\"=\">=</Option>");
                graphBuffer.append("<Option value=\"between\" selected>between</Option>");
                graphBuffer.append("</Select>");
                graphBuffer.append("</Td>");
                graphBuffer.append("<Td>");
                graphBuffer.append("<div id='singleRisk'>");
                graphBuffer.append("<Input type='text'  class='myTextbox3' name='box1h' id='box1h' style='width:62px;display:none' onkeyup='populateValue()'>");
                graphBuffer.append("</div>");
                graphBuffer.append("<div id='doubleRisk'>");
                graphBuffer.append("<Input type='text'  class='myTextbox3' name='box1' id='box1' style='width:62px' >");
                graphBuffer.append("<Input type='text'  class='myTextbox3' name='box2' id='box2' style='width:62px' onkeyup='populateValuedh()'>");
                graphBuffer.append("</div>");
                graphBuffer.append("</Td>");
                graphBuffer.append("</Tr>");
                graphBuffer.append("<Tr>");
                graphBuffer.append("<Td >Medium Risk</Td>");
                graphBuffer.append("<Td>");
                graphBuffer.append("<Select name='mediumRisk' STYLE='width:100px' class='myTextbox3' id='mediumRisk' onchange='addMediumRisk(this)'>");
                graphBuffer.append("<Option value=\"between\" selected>between</Option>");
                graphBuffer.append("<Option value=\">\">></Option>");
                graphBuffer.append("<Option value=\"<\"><</Option>");
                graphBuffer.append("<Option value=\">=\">>=</Option>");
                graphBuffer.append("<Option value=\"<=\"><=</Option>");
                graphBuffer.append("<Option value=\"=\">=</Option>");

                graphBuffer.append(" </Select>");
                graphBuffer.append("</Td>");
                graphBuffer.append("<Td>");
                graphBuffer.append("<div id='mediumsingleRisk'>");
                graphBuffer.append("<Input type='text'  class='myTextbox3' name='mediumbox1m' id='mediumbox1m' style='width:62px;display:none' onkeyup='populateValuem()'>");
                graphBuffer.append("</div>");
                graphBuffer.append("<div id='mediumdoubleRisk'>");
                graphBuffer.append("<Input type='text'  class='myTextbox3'  name='mediumbox1' id='mediumbox1' style='width:62px;'>");
                graphBuffer.append("<Input type='text'  class='myTextbox3' name='mediumbox2' id='mediumbox2' style='width:62px' onkeyup='populateValuedm()'>");
                graphBuffer.append("</div>");

                graphBuffer.append("</Td>");
                graphBuffer.append("</Tr>");
                graphBuffer.append("<Tr>");
                graphBuffer.append("<Td id='r2Text'>Low Risk</Td>");
                graphBuffer.append("<Td>");
                graphBuffer.append("<Select name='lowRisk' STYLE='width:100px' class='myTextbox3' id='lowRisk' onchange='addLowRisk(this)'>");
                graphBuffer.append("<Option value=\"<\"><</Option>");
                graphBuffer.append("<Option value=\">\">></Option>");

                graphBuffer.append("<Option value=\">=\">>=</Option>");
                graphBuffer.append("<Option value=\"<=\"><=</Option>");
                graphBuffer.append("<Option value=\"=\">=</Option>");
                graphBuffer.append("<Option value=\"between\" selected>between</Option>");
                graphBuffer.append("</Select>");
                graphBuffer.append("  </Td>");
                graphBuffer.append("<Td>");
                graphBuffer.append("<div id='lowsingleRisk'>");
                graphBuffer.append("<Input type='text'  class='myTextbox3'  name='lowbox1l' id='lowbox1l' style='width:62px;display:none' >");
                graphBuffer.append("</div>");
                graphBuffer.append("<div id='lowdoubleRisk'>");
                graphBuffer.append("<Input type='text'  class='myTextbox3'   name='lowbox1' id='lowbox1' style='width:62px;' >");
                graphBuffer.append("<Input type='text'  class='myTextbox3' name='lowbox2' id='lowbox2' style='width:62px' >");
                graphBuffer.append("</div>");
                graphBuffer.append(" </Td>");
                graphBuffer.append("</Tr>");
                graphBuffer.append("</table>");

                //end of building kpi graph region

            }
        } catch (Exception e) {
            logger.error("Exception:", e);
        }

        return graphBuffer.toString();
    }

    public boolean updatePortletName(String portalTabId, String portletId, String portletName, String portletDesc) {
        String sqlStr = getResourceBundle().getString("updatePortletName");
        String updatePortletNameinDesiner = getResourceBundle().getString("updatePortletNameinDesiner");
        ArrayList alist = new ArrayList();
        String finalQuery = "";
        boolean result = false;
        if (portalTabId.equalsIgnoreCase("-1")) {
            Object[] values = new Object[3];
            values[0] = portletName;
            values[1] = portletDesc;
            values[2] = portletId;
            finalQuery = buildQuery(updatePortletNameinDesiner, values);
            alist.add(finalQuery);
        } else {


            Object[] values = new Object[4];
            values[0] = portletName;
            values[1] = portletDesc;
            values[2] = portletId;
            values[3] = portalTabId;
            finalQuery = buildQuery(sqlStr, values);
            alist.add(finalQuery);


        }
        if (alist.size() != 0) {
            try {
                result = executeMultiple(alist);
                alist = null;
                finalQuery = null;
                sqlStr = null;
            } catch (Exception exp) {
                logger.error("Exception:", exp);
            }
        }
        return result;

    }

    public boolean deletePortals(String portalIds, String userId) throws Exception {
        boolean flag = false;
        ArrayList queryList = new ArrayList();
        String sqlStr = getResourceBundle().getString("deletePortals");
        String deleteFromportlets = getResourceBundle().getString("deleteFromportlets");
        String finalQuery = null;
        Object[] values = new Object[1];
        values[0] = portalIds;
        //values[1] = userId;
        finalQuery = buildQuery(sqlStr, values);
        queryList.add(finalQuery);
        finalQuery = buildQuery(deleteFromportlets, values);
        //////.println("finalQuery is "+finalQuery);
        flag = executeMultiple(queryList);

        return flag;
    }

    public boolean isIsFxCharts() {
        return isFxCharts;
    }

    public void setIsFxCharts(boolean isFxCharts) {
        this.isFxCharts = isFxCharts;
    }

    public boolean updatePortalTimeDetails(String portalTabId, String currDate) {
        PortletProcessor pprocess = new PortletProcessor();
        ArrayList prevTimeDetsArray = new ArrayList();
        HashMap<String, ArrayList<String>> prevTimeDetsMap = new HashMap<String, ArrayList<String>>();
        boolean result = false;
        String getXmlQuery = "";
        Statement st = null;
        ResultSet rs = null;
        Reader characterStream = null;
        String characterString = null;
        Connection con = null;
        try {
//            con = ProgenConnection.getInstance().getConnection();
//            getXmlQuery = "select a.xml_path, nvl(b.portlet_name,a.portlet_name) portlet_name,nvl(b.portlet_desc,a.portlet_desc) portlet_desc, b.portlet_tab_id,a.portlet_type from PRG_PORTLETS_MASTER a , prg_portal_tab_details b where a.portlet_id= b.portlet_id and a.portlet_id='" + portletId + "'";

//            st = con.createStatement();
//            rs = st.executeQuery(getXmlQuery);
//            while (rs.next()) {
//                if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
//                    if (rs.getString(1) != null) {
//                        characterString = rs.getString(1);
////                        
//                    } else {
//                        characterString = null;
//                    }
//                } else {
//                    if (rs.getClob(1) != null) {
//                        characterStream = rs.getClob(1).getCharacterStream();
//                        //////.println("rs.getClob(1) for " + rs.getString(2) + "  is " + rs.getString(1));
//                    } else {
//                        characterStream = null;
//                    }
//                }
//                }


            document = builder.build(new ByteArrayInputStream(pprocess.getCharacterString().getBytes()));
            root = document.getRootElement();
            pprocess.processTimeDeatils(null);
            prevTimeDetsMap = pprocess.timeDetailsMap;

            prevTimeDetsArray.remove(2);
            prevTimeDetsArray.add(2, currDate);
            ArrayList dateList = (ArrayList) prevTimeDetsMap.get("AS_OF_DATE");
            dateList.remove(0);
            dateList.add(0, currDate);
            prevTimeDetsMap.put("AS_OF_DATE", dateList);

            result = true;
        } catch (Exception exp) {
            result = false;
            logger.error("Exception:", exp);
        }
        return result;

    }

    public boolean setSortOrder(String portletId, String sortlevel) {
        String setSortOrder = getResourceBundle().getString("setSortOrder");
        Object object[] = new Object[2];
        object[0] = sortlevel;
        object[1] = portletId;
        boolean checkUpdate = false;
        String finalQuery = "";
        finalQuery = super.buildQuery(setSortOrder, object);
        try {
            super.execModifySQL(finalQuery);
            checkUpdate = true;
        } catch (Exception ex) {
            checkUpdate = false;
            logger.error("Exception:", ex);
        }
        return checkUpdate;
    }

    public String getKPIPreview(String kpis, PbReportCollection collect) {
        ArrayList queryCols = new ArrayList();
        ArrayList queryAggs = new ArrayList();

        String[] kpiArr = kpis.split(",");
        List<String> elemIds = Arrays.asList(kpiArr);
        List<KPIElement> kpiElements = new DashboardViewerDAO().getKPIElements(elemIds, new HashMap<String, String>());
        for (KPIElement elem : kpiElements) {
            queryCols.add(elem.getElementId());
            queryAggs.add(elem.getAggregationType());
        }
        PbReportQuery repQuery = new PbReportQuery();
        PbReturnObject pbretObj = new PbReturnObject();
        repQuery.setRowViewbyCols(new ArrayList());
        repQuery.setColViewbyCols(new ArrayList());
        repQuery.setQryColumns(queryCols);
        repQuery.setColAggration(queryAggs);
        repQuery.setTimeDetails(collect.timeDetailsArray);
        repQuery.setDefaultMeasure(String.valueOf(queryCols.get(0)));
        repQuery.setDefaultMeasureSumm(String.valueOf(queryAggs.get(0)));
        repQuery.isKpi = true;

        pbretObj = repQuery.getPbReturnObject(String.valueOf(queryCols.get(0)));//report kpi query which returns current,prior ,change and change%

        StringBuilder dispKpi = new StringBuilder();
        dispKpi.append("<Table width=\"99%\"  cellpadding=\"0\" cellspacing=\"1\" id=\"tablesorter\" class=\"tablesorter\" border=\"1\" >");
        dispKpi.append("<thead><tr>");
        dispKpi.append("<th ><strong>KPI</strong></th>");
        dispKpi.append("<th ><strong>Value</strong></th>");
        dispKpi.append("</tr></thead>");
        dispKpi.append("<tfoot></tfoot><tbody>");

        for (KPIElement elem : kpiElements) {
            if (!elem.getRefElementType().equals("1")) {
                continue;
            }
            String elementName = elem.getElementName();
            BigDecimal value = pbretObj.getFieldValueBigDecimal(0, "A_" + elem.getElementId());
            if (value == null) {
                value = BigDecimal.ZERO;
            }
            String dispVal = NumberFormatter.getModifiedNumber(value, "", -1);
            String bgColor = "#FFFFFF";
            String fontColor = "#369";
            dispKpi.append("<Tr><td><strong>" + elementName + "</strong></td>");
            dispKpi.append("<td style=\"height:6px;font-size:10px;font-family:VERDANA;color:" + fontColor + ";background-color:" + bgColor + " ;padding:6px;valign=middle\" align=\"center\" >" + dispVal + "</td>");
            dispKpi.append("</Tr>");
        }
        dispKpi.append("</tbody></Table>");
        return dispKpi.toString();
    }

    public void deletePortlets(String portletIds) {
        ArrayList<String> queries = new ArrayList<String>();
        String query = "delete from PRG_BASE_PORTLETS_MASTER where portlet_id in (" + portletIds + ")";
        queries.add(query);
        query = "delete from PRG_USER_PORTLETS_MASTER where PORTLET_ID in (" + portletIds + ")";
        queries.add(query);
        query = "delete from PRG_PORTAL_PORTLETS_ASSIGN where PORTLET_ID in (" + portletIds + ")";

        PbDb pbdb = new PbDb();
        pbdb.executeMultiple(queries);
    }

    public String preparePortletPreview(String portletID, HttpServletRequest request, HttpServletResponse response, String portalID) {
        PortletProcessor portalProcessor = new PortletProcessor();
        HttpSession session = request.getSession();
        Document document = null;
        SAXBuilder builder = new SAXBuilder();
        portalProcessor.setIfFxCharts(false);
        portalProcessor.setCallFrom(callFrom);
        ProgenParam pparam = new ProgenParam();
        String currDate = "undefined";
        StringBuffer portletPreviewBuffer = new StringBuffer();
        if (currDate.equalsIgnoreCase("undefined") || (currDate == null ? "undefined" == null : currDate.equals("undefined"))) {
            if (request.getSession().getAttribute("dateCurr") != null) {
                currDate = (String) request.getSession().getAttribute("dateCurr");
            } else {
                currDate = pparam.getdateforpage();
            }
        }
        PortalDAO portalDAO = new PortalDAO();
        PbReturnObject portletsReturnObject = portalDAO.getPortletsFormDesign(portletID);
        //   tableBuffer = portalProcessor.processProgenDataPortlet                (request, response, session, REP, CEP, portletId, portLet.getSortOrder(), gpType,Integer.toString(portal.getPortalID()), portLet.getPortLetName(), currDate);
        //  portletPreviewBuilder =portalProcessor.processProgenDataPortlet(request, response, request.getSession(), "", "", portletID,"Top-10"," ","design",portletsReturnObject.getFieldValueString(0,"PORTLET_NAME"), currDate);
        if (portletsReturnObject.getFieldValueClobString(0, "XML_PATH") != null) {

            try {
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(portletsReturnObject.getFieldValueClobString(0, "XML_PATH").trim().getBytes());
                document = builder.build(byteArrayInputStream);
            } catch (JDOMException ex) {
                logger.error("Exception:", ex);
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
            portalProcessor.setXmlDocument(document);


        }
        try {
            portletPreviewBuffer = portalProcessor.processProgenDataPortlet(request, response, session, "", "", portletID, "Top-10", "", portalID, portletsReturnObject.getFieldValueString(0, "PORTLET_NAME"), currDate, "");
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        return portletPreviewBuffer.toString();
    }

    public boolean portletAssign(String Portletid, String portalId, String portletHeight, String userId) {
        PortalDAO portalDAO = new PortalDAO();
        boolean status = portalDAO.isPortletAssign(Portletid, portalId, userId);
        if (status) {
            return false;
        } else {
            return portalDAO.portletAssign(Portletid, portalId, portletHeight, userId);
        }
    }

    public String getPortletProperty(String portalID, String portletID, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        ArrayList<Portal> portals = new ArrayList<Portal>();
        if (session != null) {
            portals = (ArrayList<Portal>) session.getAttribute("PORTALS");
        }
        Portal portal = null;
        PortLet portLet = null;
        Iterator<PortLet> moduleIter = null;
        for (Portal port : portals) {
            moduleIter = Iterables.filter(port.getPortlets(), PortLet.getAccessPortletPredicate(Integer.parseInt(portletID))).iterator();
            if (moduleIter.hasNext()) {
                portLet = moduleIter.next();
                portal = port;
                if (portal.getPortalID() == Integer.parseInt(portalID)) {
                    break;
                }
            }
        }
        Gson gson = new Gson();
        String propertiesJson = "";
        if (portLet.getGraphProperty() != null) {
            propertiesJson = gson.toJson(portLet.getGraphProperty());
        }
        return propertiesJson;
    }

    public boolean saveXmalOfPortlet(ArrayList<String> paramList, PortLet portLet, String userId, HashMap GraphClassesHashMap, String saveStatus, String nameofNewPortlet, String sortlevel) {
        boolean status = false;
        SavePortletXML savePortletXML = new SavePortletXML();
        savePortletXML.setGraphClassesHashMap(GraphClassesHashMap);
        String[] REP = paramList.get(2).split(",");
        String[] CEP = paramList.get(3).split(",");
        ArrayList<String> REPList = new ArrayList<String>();
        ArrayList<String> CEPList = new ArrayList<String>();
        REPList.addAll(Arrays.asList(REP));
        for (String cep : CEP) {
            if (!cep.equalsIgnoreCase("")) {
                CEPList.addAll(Arrays.asList(cep));
            }
        }

        portLet.setPortLetName(nameofNewPortlet);
        portLet.setPortLetDes(nameofNewPortlet);
        savePortletXML.setCEP_Elements(CEPList);
        savePortletXML.setPortLet(portLet);
        savePortletXML.setGraphType(paramList.get(4));
        StringBuilder paramsStringBuilder = new StringBuilder();
        StringBuilder paramString1Builder = new StringBuilder();
        StringBuilder paramString2Builder = new StringBuilder();
        paramString2Builder.append(" case ");
        String paramsString = "";
        String paramString1 = "";
        String paramString2 = "";
        XMLOutputter serializer = null;
        Set<String> parmKeys = portLet.getPortletXMLHelper().getReportParameters().keySet();
        int i = 0;
        for (String keyStr : parmKeys) {
            if (!keyStr.equalsIgnoreCase("TIME")) {
                paramsStringBuilder.append(",").append(keyStr.trim().replace("A_", ""));
                paramString1Builder.append(",").append(keyStr.replace("A_", "")).append(",").append(i + 1);
                paramString2Builder.append(" when element_id =" + keyStr.replace("A_", "") + " then " + (i + 1) + " ");
                i++;
            }
        }
        paramString2Builder.append(" else 10000 end ");
        if (!(paramsStringBuilder.toString().equalsIgnoreCase(""))) {
            paramsString = paramsStringBuilder.toString().substring(1);
            //swathi changes 4 kpigraph
            if (paramList.get(4).equals("Meter") || paramList.get(4).equals("Thermometer")) {
                REPList.clear();
                String[] parameters = paramsString.split(",");
                REPList.add(parameters[0]);
            }
        }
        if (!(paramString1Builder.toString().equalsIgnoreCase(""))) {
            paramString1 = paramString1Builder.toString().substring(1);
        }
        if (!(paramString2Builder.toString().equalsIgnoreCase(""))) {
            paramString2 = paramString2Builder.toString().substring(1);
        }
        savePortletXML.setParamString1(paramString1);
        savePortletXML.setParamString2(paramString2);
        savePortletXML.setParamsString(paramsString);
        savePortletXML.setREP_Elements(REPList);//swathi changes 4 kpigraph

        Element rootElement = new Element("portlet");
        rootElement.setAttribute("version", "1.00001");
        rootElement.setText("New Root");
        rootElement = savePortletXML.buildMetaInfo(rootElement);
        rootElement = savePortletXML.buildParameters(rootElement);
        rootElement = savePortletXML.buildTimeDetails(rootElement);
        rootElement = savePortletXML.buildViewByDetails(rootElement);
        try {
            rootElement = savePortletXML.buildQueryDetails(rootElement);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }

        document = new Document(rootElement);
        serializer = new XMLOutputter();
        String portletXML = serializer.outputString(document).toString().replaceAll("[\r\n]+", "");
        PortalDAO portalDAO = new PortalDAO();
        try {
            status = portalDAO.saveXmalOfPortlet(portletXML, portLet, userId, paramList.get(0), saveStatus, nameofNewPortlet, sortlevel);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }

        return status;
    }

    public void setCallFrom(String callFrom) {
        this.callFrom = callFrom;
    }

    public boolean extendTablePortlet(String portalTabId, String portletId, int selectedvalue) {

        String sqlqry = getResourceBundle().getString("extendTablePortlet");

        String finalQuery = "";
        boolean result = false;
        int res = 0;
        if (portalTabId.equalsIgnoreCase("-1")) {
        } else {

            Object[] values = new Object[3];
            values[0] = selectedvalue;
            values[1] = portletId;
            values[2] = portalTabId;
            finalQuery = buildQuery(sqlqry, values);
        }

        try {
            res = execUpdateSQL(finalQuery);
            //             
            finalQuery = null;
            sqlqry = null;
        } catch (Exception exp) {
            logger.error("Exception:", exp);
        }
//         
        if (res > 0) {
            result = true;
        }
        return result;


    }

    public long getDayDifference(HashMap ParametersHashMap) {
        long diffDays = 0;
        ArrayList timeArray = new ArrayList();
        HashMap timeDetsMap = new HashMap();
        if (ParametersHashMap.get("TimeDetailstList") != null) {
            timeArray = (ArrayList) ParametersHashMap.get("TimeDetailstList");
        }
        if (ParametersHashMap.get("TimeDimHashMap") != null) {
            timeDetsMap = (HashMap) ParametersHashMap.get("TimeDimHashMap");
        }


        if (timeArray.get(1).toString().equalsIgnoreCase("PRG_STD")) {
            ArrayList month = (ArrayList) timeDetsMap.get("AS_OF_DATE");
            String dateSql = "";
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                dateSql = "select datepart(dd, convert(datetime,'" + month.get(0) + "',101))";
            } else {
                dateSql = "select TO_NUMBER(TO_CHAR((TO_DATE('" + month.get(0) + "','MM/DD/YYYY')),'DD')) FROM DUAL";
            }
            //////////////////////.println("datesql is: "+dateSql);
            PbDb pbdb = new PbDb();
            PbReturnObject retObj = null;
            try {
                retObj = pbdb.execSelectSQL(dateSql);
            } catch (SQLException ex) {
                logger.error("Exception:", ex);
            }
            if (retObj != null && retObj.getRowCount() > 0) {
                diffDays = retObj.getFieldValueInt(0, 0);

            }
        } else {
            ArrayList fromDate = (ArrayList) timeDetsMap.get("AS_OF_DATE1");
            ArrayList toDate = (ArrayList) timeDetsMap.get("AS_OF_DATE2");
            DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
            Date fromdt = null;
            Date todt = null;
            try {
                fromdt = (Date) df.parse(String.valueOf(fromDate.get(0)));
                todt = (Date) df.parse(String.valueOf(toDate.get(0)));
            } catch (ParseException ex) {
                logger.error("Exception:", ex);
            }
            //////////////.println("fromdt  is: " + fromdt);
            //////////////.println("todt  is: " + todt);
            diffDays = (todt.getTime() - fromdt.getTime()) / (24 * 60 * 60 * 1000);
        }
        return diffDays;
    }

    public String getMinMaxAvgValueOfElement(HttpServletRequest request, HttpServletResponse response, String portletId, String portalId, String columnname) {
        HttpSession session = request.getSession(false);
        List<Portal> portals = (List<Portal>) session.getAttribute("PORTALS");
        Portal tempPortalForName = Iterables.find(portals, Portal.getAccessPortalPredicate(Integer.parseInt(portalId)));
        PortLet portlet = Iterables.find(tempPortalForName.getPortlets(), PortLet.getAccessPortletPredicate(Integer.parseInt(portletId)));
        PortletProcessor portalProcessor = new PortletProcessor();
        portalProcessor.setIfFxCharts(false);
        ProgenParam pparam = new ProgenParam();
        if (portlet.getWhereClause() != null) {
            portalProcessor.setWhereClause(portlet.getWhereClause());
        }
        if (portlet.getRuleOn() != null) {
            portalProcessor.setRuleOn(portlet.getRuleOn());
        }
        if (portlet.getReportParams() != null) {
            portalProcessor.setReportParams(portlet.getReportParams());
        }
        portalProcessor.setMeasureIDs(portlet.getMeasureID());
        portalProcessor.setAggreGationType(portlet.getAggreGationType());
        portalProcessor.setMeasureNames(portlet.getMeasureNames());
        if (portlet.getGraphProperty() != null) {
            portalProcessor.setPortletProperty(portlet.getGraphProperty());
        }
        if (portlet.getXMLDocument() != null && portlet.getPortletType().equalsIgnoreCase("I")) {
            portalProcessor.setXmlDocument(portlet.getXMLDocument());
            portalProcessor.setPortLet(portlet);
        }
        try {
            portalProcessor.processDataSet("");
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        PbReturnObject returnObject = portalProcessor.getReturnObject();

        returnObject.setProcessGT(true);
        returnObject.prepareObject(returnObject);
        HashMap map = new HashMap();
        map.put("MaxValue", returnObject.getColumnMaximumValue(columnname));
        map.put("MinValue", returnObject.getColumnMinimumValue(columnname));
        map.put("AvgValue", returnObject.getColumnAverageValue(columnname));

        Gson gson = new Gson();
        String jsonString = null;
        if (!map.isEmpty()) {
            jsonString = gson.toJson(map);
        }

        return jsonString;

    }

    /**
     * @return the drillElmentDetails
     */
    public String getDrillElmentDetails() {
        return drillElmentDetails;
    }

    /**
     * @param drillElmentDetails the drillElmentDetails to set
     */
    public void setDrillElmentDetails(String drillElmentDetails) {
        this.drillElmentDetails = drillElmentDetails;
    }

    /**
     * @return the dirllOn
     */
    public String getDirllOn() {
        return dirllOn;
    }

    /**
     * @param dirllOn the dirllOn to set
     */
    public void setDirllOn(String dirllOn) {
        this.dirllOn = dirllOn;
    }
    //for getting the kpireturn object

    public PbReturnObject getKpiretObj(String Kpis, HashMap ParameterHashMap, HashMap TableHashMap) {
        PbReturnObject pbretObj = new PbReturnObject();
        PbReturnObject retObj = null;
        PbReportQuery repQuery = new PbReportQuery();

        HashMap ParametersMap = null;
        HashMap TableMap = null;
        ArrayList Parameters = null;
        ArrayList TimeDetails = null;
        ArrayList reportRowViewbyValues = null;
        ArrayList reportColViewbyValues = new ArrayList();
        ArrayList qryColumns = null;
        String query = getResourceBundle().getString("displayKPIGraphRegion");
        Object[] values = new Object[1];
        values[0] = Kpis;
        ArrayList columnAggr = new ArrayList();
        long diffDays = 0;

        try {
            query = buildQuery(query, values);
            retObj = execSelectSQL(query);
            ParametersMap = ParameterHashMap;
            TableMap = TableHashMap;

            Parameters = (ArrayList) ParametersMap.get("Parameters");
            TimeDetails = (ArrayList) ParametersMap.get("TimeDetailstList");

            if (TableMap != null && TableMap.size() != 0) {
                if (TableMap.get("REP") != null) {
                    reportRowViewbyValues = (ArrayList) TableMap.get("REP");
                }
            }
            if (reportRowViewbyValues == null) {
                reportRowViewbyValues = new ArrayList();
                reportRowViewbyValues.add(Parameters.get(0));
            }
            qryColumns = new ArrayList();
            qryColumns.add(Kpis);

            columnAggr.add(retObj.getFieldValueString(0, 0));
            repQuery.setRowViewbyCols(reportRowViewbyValues);
            repQuery.setColViewbyCols(reportColViewbyValues);

            repQuery.setQryColumns(qryColumns);
            repQuery.setColAggration(columnAggr);
            repQuery.setTimeDetails(TimeDetails);

            repQuery.setDefaultMeasure(Kpis);
            repQuery.setDefaultMeasureSumm(String.valueOf(retObj.getFieldValueString(0, 0)));
            repQuery.isKpi = true;
            pbretObj = repQuery.getPbReturnObject(Kpis);
            diffDays = getDayDifference(ParametersMap);

        } catch (Exception e) {
            logger.error("Exception:", e);
        }


        return pbretObj;
    }
}
