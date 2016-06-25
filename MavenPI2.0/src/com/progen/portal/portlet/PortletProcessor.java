/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.portal.portlet;

import com.google.common.base.Joiner;
import com.google.common.collect.ArrayListMultimap;
import com.progen.charts.GraphProperty;
import com.progen.charts.ProgenChartDisplay;
import com.progen.dashboard.DashboardTableColorGroupHelper;
import com.progen.dashboardView.bd.PbDashboardViewerBD;
import com.progen.portal.PortLetSorterHelper;
import com.progen.portal.PortletXMLHelper;
import com.progen.portal.viewer.PortalViewerBD;
import com.progen.report.KPIElement;
import com.progen.report.PbReportCollection;
import com.progen.xml.pbXmlUtilities;
import prg.db.ContainerConstants.SortOrder;
import com.progen.portal.PortLet;
import com.progen.report.PbReportMaps;
import com.progen.report.SortColumn;
import com.progen.report.charts.PbDashboardGraphDisplay;
import com.progen.report.charts.PbGraphDisplay;
import com.progen.report.query.PbReportQuery;
import com.progen.reportdesigner.bd.DashboardTemplateBD;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.text.NumberFormat;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import com.progen.reportdesigner.db.ReportTemplateDAO;
import java.io.PrintWriter;
import java.io.Reader;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeSet;
import prg.db.PbReturnObject;
import utils.db.ProgenParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import prg.db.Container;
import prg.db.PbDb;
import utils.db.ProgenConnection;
//import org.apache.catalina.connector.Request;
//import org.apache.catalina.connector.Response;
import org.apache.log4j.*;

public class PortletProcessor extends PbReportMaps {

    public static Logger logger = Logger.getLogger(PortletProcessor.class);
    private String xmlDocument;
    private ProgenParam pParam = new ProgenParam();
    private HashMap graphDeails = new HashMap();
    private HashMap metaInfo = new HashMap();
    private PbDashboardGraphDisplay pdgd = new PbDashboardGraphDisplay();
    private PbReportQuery pb = new PbReportQuery();
    private HashMap ParameterNamesHashMap = new HashMap();
    private DashboardTemplateBD dashboardTempBD = new DashboardTemplateBD();
    //modified by santhosh.kumar@progenbusiness.com
    private SAXBuilder builder = new SAXBuilder();
    private Document document = null;
    private Element root = null;
    private String displayType = null;
    private String[] performers = {"Top-10", "Bottom-10", "Top-5", "Bottom-5"};
    //public String[] graphTypesList = {"Bar", "Bar3D", "Line", "Line3D", "Stacked", "Pie", "Ring", "Pie3D"};
    private String[] graphTypesList = null;
    private ArrayList REPNames = new ArrayList();
    private ArrayList CEPNames = new ArrayList();
    private ArrayList REPList = new ArrayList();
    private ArrayList CEPList = new ArrayList();
    //end of modifications
    private Reader characterStream = null;
    private String characterString = null;
    private boolean ifFxCharts = false;
    private HttpServletRequest servletRequest = null;
    private HttpServletResponse servletResponse = null;
    HashMap GraphClassesHashMap = null;
    HashMap GraphTypesHashMap = null;
    private String whereClause;
    private String[] measureIDs;
    private String[] aggreGationType;
    private String[] measureNames;
    ArrayList<String> isUsedinPortlet = new ArrayList<String>();
    private HashMap<String, String> sortColumnsAndOrder = null;
    private HashMap kpiQuery = new LinkedHashMap();
    private HashMap kpiMasterQuery = new LinkedHashMap();
    private Document XmlDocument;
    ArrayListMultimap<String, KPIElement> kpiElementList = ArrayListMultimap.create();
    private GraphProperty portletProperty = null;
    private PortLet portLet = null;
    private String ruleOn;
    private Map<String, String> reportParams;
    private String callFrom = "";
    private PbReturnObject returnObject = new PbReturnObject();
    private boolean ispdfGenerator = false;
    private ArrayList displayLabelsForPdf = new ArrayList();
    private String drillElmentDetails = null;
    private String drillOn = null;
//    private HashMap<String,String> portletGrpMap;

    public String getCharacterString() {
        return characterString;
    }

    public void setCharacterString(String characterString) {
        this.characterString = characterString;
    }

    public Reader getCharacterStream() {
        return characterStream;
    }

    public void setCharacterStream(Reader characterStream) {
        this.characterStream = characterStream;
    }

    public Document getXmlDocument() {
        return this.XmlDocument;
    }

    public void setXmlDocument(Document XmlDocument) {
        this.XmlDocument = XmlDocument;
    }

    public void setXmlDocument(String xmlDocument) {
        this.xmlDocument = xmlDocument;
    }
    pbXmlUtilities xmUtil = new pbXmlUtilities();

    public PortletProcessor(String xmlDoc) {
        this.xmlDocument = xmlDoc;
    }

    public PortletProcessor() {
    }

    public void processDocument() throws Exception {
        if (getPortLet() != null) {
            portLet = getPortLet();
            PortletXMLHelper portletXMLHelper = portLet.getPortletXMLHelper();
            reportParameters.putAll(portletXMLHelper.getReportParameters());
//            
            reportParametersValues.putAll(portletXMLHelper.getReportParametersValues());
//            
            metaInfo = portletXMLHelper.getMetaInfo();
//            
            displayType = String.valueOf(metaInfo.get("DisplayType"));
            getParameterNamesHashMap().putAll(portletXMLHelper.getParameterNamesHashMap());

//            
            if (portLet.getPortLetTimeHelper() != null && !portLet.getPortLetTimeHelper().getPortletTime().equalsIgnoreCase("")) {
                timeDetailsMap = (HashMap<String, ArrayList<String>>) portletXMLHelper.getTimeDetailsMap().clone();
                timeDetailsMap.get("AS_OF_DATE").set(0, portLet.getPortLetTimeHelper().getPortletTime());
                timeDetailsMap.get("PRG_PERIOD_TYPE").set(0, portLet.getPortLetTimeHelper().getPortletPeriod());
                timeDetailsArray = (ArrayList) portletXMLHelper.getTimeDetailsArray().clone();
                timeDetailsArray.set(2, portLet.getPortLetTimeHelper().getPortletTime());
                timeDetailsArray.set(3, portLet.getPortLetTimeHelper().getPortletPeriod());
                timeDetailsArray.set(4, portLet.getPortLetTimeHelper().getStdComprePeriod());
            } else {
                timeDetailsMap = portletXMLHelper.getTimeDetailsMap();
                timeDetailsArray = portletXMLHelper.getTimeDetailsArray();
            }
            ArrayList<String> reportQryElementIds1 = new ArrayList<String>();
            ArrayList<String> relatedList = new ArrayList<String>();
//            
            reportRowViewbyValues = portletXMLHelper.getRowViewbyValues();
//            
            reportColViewbyValues = portletXMLHelper.getColViewbyValues();
//            
            reportQryElementIds = portletXMLHelper.getQryElementIds();
            reportQryElementIds1 = reportQryElementIds;
//            String[] valueForQryEltId = portLet.getPortletSorterHelper().getSortByColumeVal().split(",");
//            ArrayList<String>relatedList=new ArrayList<String>();
//             relatedList.addAll(Arrays.asList(valueForQryEltId));
//            
//            reportQryElementIds = relatedList;
//            
            reportQryAggregations = portletXMLHelper.getQryAggregations();
            for (int i = 0; i < reportQryAggregations.size(); i++) {
                relatedList.addAll(Arrays.asList(reportQryElementIds1.get(i)));
            }
            reportQryElementIds = relatedList;
//            if(reportQryElementIds.size()>reportQryAggregations.size()){
//                String valueForQryEltId = portletXMLHelper.getQryAggregations().get(0);
//            ArrayList<String>relatedList=new ArrayList<String>();
//            for(int i=0;i<reportQryElementIds.size();i++){
//                relatedList.add(valueForQryEltId);
//            }             
//                reportQryAggregations = relatedList;
//            }
//            
            reportQryColNames = portletXMLHelper.getQryColNames();
//            
            this.sortColumnsAndOrder = portletXMLHelper.getSortColumnsAndOrder();
//            HashMap hmap = new HashMap();
//          hmap.put("A_"+relatedList.get(0), "ASC");
//            this.sortColumnsAndOrder = hmap;
//            
            getGraphDeails().putAll(portletXMLHelper.getGraphDeails());
//            
        } else {// we have get PortletXml object and pass it else case
            reportParameters = new LinkedHashMap();
            reportParametersValues = new LinkedHashMap();
            document = getXmlDocument();
            root = document.getRootElement();
            processMetainfo(root);
            displayType = String.valueOf(metaInfo.get("DisplayType"));
            processParameters(root);
            processTimeDeatils(root);
            processViewByDetails(root);
            processQueryDetails(root);
            if (displayType.equalsIgnoreCase("KPI Graph")) {
                processGrapStatus(root);
            }

        }

        if (getMeasureIDs() != null) {
            for (int i = 0; i < measureIDs.length; i++) {
                if (!measureIDs[i].trim().equalsIgnoreCase("") && !reportQryElementIds.contains(measureIDs[i].trim())) {
                    reportQryElementIds.add(measureIDs[i]);
                    reportQryColNames.add(measureNames[i]);
                    reportQryAggregations.add(aggreGationType[i]);
                    pb.setQryColumns(reportQryElementIds);
                    pb.setWhereClause(getWhereClause());
                    if (getRuleOn() != null && getRuleOn().equalsIgnoreCase("Dimension")) {
                        reportParametersValues.putAll(reportParams);
                    }
                    isUsedinPortlet.add(measureIDs[i]);
                } else {
                    pb.setQryColumns(reportQryElementIds);
                    pb.setWhereClause(getWhereClause());
                    if (getRuleOn() != null && getRuleOn().equalsIgnoreCase("Dimension")) {
                        reportParametersValues.putAll(reportParams);
                    }
                }
            }
        } else {
            pb.setQryColumns(reportQryElementIds);
        }
        pb.setWhereClause(getWhereClause());
        if (getRuleOn() != null && getRuleOn().equalsIgnoreCase("Dimension")) {
            reportParametersValues.putAll(reportParams);
        }
        if (portLet.getReportParams() == null) {
            if (portLet.getPortletRuleHelper() != null && !portLet.getPortletRuleHelper().getRuleOn().equalsIgnoreCase("")) {
                if (portLet.getPortletRuleHelper().getRuleOn() != null && portLet.getPortletRuleHelper().getRuleOn().equalsIgnoreCase("Dimension")) {
                    if (portLet.getPortletRuleHelper().getReportParms() != null) {
                        reportParametersValues.putAll(portLet.getPortletRuleHelper().getReportParms());
                    }
                }
            }
        }
        pb.setColAggration(reportQryAggregations);
        pb.setTimeDetails(timeDetailsArray);
        pb.setRowViewbyCols(reportRowViewbyValues);
        pb.setColViewbyCols(reportColViewbyValues);
        pb.setParamValue(reportParametersValues);

    }

    public StringBuffer processProgenDataPortlet(HttpServletRequest request, HttpServletResponse response, HttpSession session, String REP, String CEP, String PortletId, String perBy, String graphType, String portalTabId, String PortletName, String currDate, String periodType) throws Exception {

        setServletRequest(request);
        setServletResponse(response);
        PbReportCollection repCollect = new PbReportCollection();
        processDocument();
        if (drillElmentDetails != null && !drillElmentDetails.equalsIgnoreCase("")) {
            reportParametersValues.put(getDrillOn(), getDrillElmentDetails());
            pb.setParamValue(reportParametersValues);
        }
        StringBuffer graphBuilder = new StringBuffer();
        GraphClassesHashMap = (HashMap) session.getAttribute("GraphClassesHashMap");
        GraphTypesHashMap = (HashMap) session.getAttribute("GraphTypesHashMap");
        graphTypesList = (String[]) (new TreeSet(GraphTypesHashMap.values())).toArray(new String[0]);
        StringBuffer tableBuffer = new StringBuffer("");
        StringBuffer portletPreviewSB = new StringBuffer();
        PbReturnObject sortPbretObj = new PbReturnObject();

        //ArrayList grpsArray = new ArrayList();
        HashMap ParametersHashMap = new HashMap();
        HashMap TableHashMap = new HashMap();
        ArrayList dets = new ArrayList();
        ArrayList detnames = new ArrayList();
//        if(portLet!=null && portLet.getPortletXMLHelper()!=null && graphType.equalsIgnoreCase("Table"))
//            portLet.getPortletXMLHelper().getMetaInfo().put("DisplayType",graphType);
        for (int i = 0; i < reportParameters.values().toArray().length; i++) {
            dets.add(reportParameters.values().toArray()[i].toString().split(",")[0].replace("[", ""));
            detnames.add(reportParameters.values().toArray()[i].toString().split(",")[1]);
        }

//            
//            
        if ((!currDate.equalsIgnoreCase("") && !(currDate.equalsIgnoreCase((String) timeDetailsArray.get(2)))) && (portLet != null && portLet.getPortLetTimeHelper() == null)) {
            timeDetailsArray.remove(2);
            timeDetailsArray.add(2, currDate);
            ArrayList dateList = (ArrayList) timeDetailsMap.get("AS_OF_DATE");
            dateList.remove(0);
            dateList.add(0, currDate);
            timeDetailsMap.put("AS_OF_DATE", dateList);
        }
        if ((periodType != null) && (!periodType.equalsIgnoreCase("") && !(periodType.equalsIgnoreCase((String) timeDetailsArray.get(3)))) && (portLet != null && portLet.getPortLetTimeHelper() == null) && !periodType.equalsIgnoreCase("undefined")) {
            timeDetailsArray.remove(3);
            timeDetailsArray.add(3, periodType);
            ArrayList dateList = (ArrayList) timeDetailsMap.get("PRG_PERIOD_TYPE");
            dateList.remove(0);
            dateList.add(0, periodType);
            timeDetailsMap.put("PRG_PERIOD_TYPE", dateList);
        }

//            
        ParametersHashMap.put("Parameters", dets);
        ParametersHashMap.put("TimeDetailstList", timeDetailsArray);
        ParametersHashMap.put("TimeDimHashMap", timeDetailsMap);
        ParametersHashMap.put("ParametersNames", detnames);
        if (REP.equalsIgnoreCase("")) {
            REPNames = new ArrayList();
            REPList = (ArrayList) reportRowViewbyValues.clone();
            for (int k = 0; k < reportRowViewbyValues.size(); k++) {
                if (getParameterNamesHashMap().get(String.valueOf(reportRowViewbyValues.get(k))) != null) {
                    REPNames.add(getParameterNamesHashMap().get(String.valueOf(reportRowViewbyValues.get(k))));
                } else {
//                        REPList.add("TIME");
                    REPNames.add("Time");
                }
            }
            TableHashMap.put("REP", REPList);
            TableHashMap.put("REPNames", REPNames);

        } else {
            REPNames = new ArrayList();
            REPList = new ArrayList();
            String[] REPStr = REP.split(",");
            for (int k = 0; k < REPStr.length; k++) {
                if (REPStr[k].equalsIgnoreCase("TIME")) {
                    REPList.add("TIME");
                } else {
                    REPList.add(REPStr[k]);
                }
                if (getParameterNamesHashMap().get(REPStr[k].trim()) != null) {
                    REPNames.add(getParameterNamesHashMap().get(REPStr[k]));
                } else {
                    REPNames.add("Time");
                }
            }
            TableHashMap.put("REP", REPList);
            TableHashMap.put("REPNames", REPNames);
        }
        if (CEP.equalsIgnoreCase("")) {
            CEPNames = new ArrayList();
            CEPList = (ArrayList) reportColViewbyValues.clone();

            for (int k = 0; k < reportColViewbyValues.size(); k++) {
                if (getParameterNamesHashMap().get(String.valueOf(reportColViewbyValues.get(k))) != null) {
                    CEPNames.add(getParameterNamesHashMap().get(String.valueOf(reportColViewbyValues.get(k))));
                } else {
//                        CEPList.add("TIME");
                    CEPNames.add("Time");
                }
                //CEPNames.add(ParameterNamesHashMap.get(String.valueOf(reportColViewbyValues.get(k))));
            }
            TableHashMap.put("CEP", CEPList);
            TableHashMap.put("CEPNames", CEPNames);

        } else {
            CEPNames = new ArrayList();
            CEPList = new ArrayList();
            String[] CEPStr = CEP.split(",");
            for (int k = 0; k < CEPStr.length; k++) {
                if (!CEPStr[k].equalsIgnoreCase("")) {
                    if (CEPStr[k].equalsIgnoreCase("TIME")) {
                        CEPList.add("TIME");
                    } else {
                        CEPList.add(CEPStr[k]);
                    }
                    if (getParameterNamesHashMap().get(CEPStr[k]) != null) {
                        CEPNames.add(getParameterNamesHashMap().get(CEPStr[k]));
                    } else {
                        CEPNames.add("Time");
                    }
                }
            }
            TableHashMap.put("CEP", CEPList);
            TableHashMap.put("CEPNames", CEPNames);
        }

        TableHashMap.put("Measures", reportQryElementIds);
        TableHashMap.put("MeasuresNames", reportQryColNames);
        TableHashMap.put("AggregationType", reportQryAggregations);
        if (displayType.equalsIgnoreCase("Table")) {
            String tableString = "No data";
            String oneViewCheck = "";
            String height = "";
            String width = "";
            oneViewCheck = (String) session.getAttribute("ONEVIEW");
            height = (String) session.getAttribute("height");
            width = (String) session.getAttribute("width");
            String temp = buildTable(ParametersHashMap, TableHashMap, PortletId, portalTabId);
            if (temp != null) {
                tableString = temp;
            }
            PortLetSorterHelper portletSorterHelper = portLet.getPortletSorterHelper();
            if (!"ONEVIEW".equals(oneViewCheck)) {
                tableBuffer.append("<div class=\"portlet-header portletHeader ui-corner-all\">");//header div starts
                tableBuffer.append("<table style='height:10px;width:100%'>");//header table

                tableBuffer.append(" <tr valign='top' align='right'>");
                tableBuffer.append(" <td valign='top' style='height:10px;width:100%'  >");
                tableBuffer.append("<table width=\"100%\">");
                tableBuffer.append("<tr>");
                tableBuffer.append("<td align=\"left\" style='color:#369;font-weight:bold'>");
                tableBuffer.append("<a href='javascript:void(0)' onclick=\"editPortletName('" + PortletId + "','" + portalTabId + "','" + PortletName + "','" + currDate + "','" + callFrom + "')\"  style='text-decoration:none' class=\"calcTitle\" title=\"Edit Name\"><font size=\"1px\"><span id='" + PortletId + "_span'><b>" + PortletName + "</b> </span></font></a>");

                tableBuffer.append("</td>");
                tableBuffer.append("<td>");
                tableBuffer.append("<a href='javascript:void(0)'  class=\"ui-icon ui-icon-disk\"  onclick=\"saveXmalOfPortlet('" + PortletId + "',document.getElementsByName('chkREP-" + PortletId + "-" + portalTabId + "'),document.getElementById('REP" + PortletId + "-" + portalTabId + "'),'" + perBy + "','" + graphType + "','" + portalTabId + "','" + currDate + "','" + callFrom + "')\"  style='text-decoration:none' class=\"calcTitle\" title=\"Save Portlet\"><font size=\"1px\"><b> </b> </font></a> &nbsp;&nbsp;");
                tableBuffer.append("<div style='display:none;width:auto;height:50px;background-color:#ffffff;overflow:auto;position:absolute;text-align:left;border:1px solid #000000;border-top-width: 0px;' id='saveXmalOfPortlet" + PortletId + "-" + portalTabId + "'>");
                tableBuffer.append("<table><tr><td>Over Write</td><td><input type='radio' name='saveRediooption" + PortletId + "-" + portalTabId + "' id='overwrite" + PortletId + "-" + portalTabId + "' value='overwrite' ></td></tr><tr><td>Save As New </td><td><input type='radio' name='saveRediooption" + PortletId + "-" + portalTabId + "' id='saveAsNew" + PortletId + "-" + portalTabId + "' value='saveAsNew' checked></td></tr></table>");
                tableBuffer.append("</div>");
                tableBuffer.append("</td>");
                tableBuffer.append("<td>");
                tableBuffer.append("<a href='javascript:void(0)' class=\"ui-icon ui-icon-scissors\" onclick=\"openFilter(" + PortletId + "," + portalTabId + ")\"  style='text-decoration:none' class=\"calcTitle\" title=\"Filter\"><font size=\"1px\"><b></b> </font></a>");
                tableBuffer.append("</td>");
                tableBuffer.append("<td align=\"right\"> ");
                tableBuffer.append("<table border=0><tr><td>&nbsp;&nbsp;</td> <td>");
                tableBuffer.append("<a href='javascript:void(0)'onclick=\"extendTable('" + PortletId + "',document.getElementById('EXT" + PortletId + "-" + portalTabId + "'),document.getElementById('extend" + PortletId + "-" + portalTabId + "'),'" + portalTabId + "','" + currDate + "')\" class=\"ui-icon ui-icon-plusthick\" style='text-decoration:none' class=\"calcTitle\" title=\"Extend Table\"><font size=\"1px\"> <b></b> </font></a>&nbsp; ");
                tableBuffer.append("<div style='display:none;width:auto;height:auto;background-color:#ffffff;overflow:auto;position:absolute;text-align:left;border:1px solid #000000;border-top-width: 0px;' id='EXT" + PortletId + "-" + portalTabId + "'>");
                tableBuffer.append("<table>");
                tableBuffer.append("<tr><td>Extendto</td><td><select name=\"extend" + PortletId + "-" + portalTabId + "\" id=\"extend" + PortletId + "-" + portalTabId + "\">");
                if (portLet.getPortletHeight() == 420) {
                    tableBuffer.append("<option value='1' selected>One</option><option value='2'>Two</option><option value='3'>Three</option></select></td><td>Portlet</td></tr>");
                } else if (portLet.getPortletHeight() == 700) {
                    tableBuffer.append("<option value='1'>One</option><option value='2' selected>Two</option><option value='3'>Three</option></select></td><td>Portlet</td></tr>");
                } else if (portLet.getPortletHeight() == 1050) {
                    tableBuffer.append("<option value='1'>One</option><option value='2'>Two</option><option value='3' selected>Three</option></select></td><td>Portlet</td></tr>");
                } else {
                    tableBuffer.append("<option value='1'>One</option><option value='2'>Two</option><option value='3'>Three</option></select></td><td>Portlet</td></tr>");
                }

                tableBuffer.append("<tr><td>&nbsp;&nbsp;</td></tr>");
                tableBuffer.append("<tr><td align='center'><input type=\"button\" class=\"navtitle-hover\" onclick=\"EXTTable('" + PortletId + "',document.getElementById('EXT" + PortletId + "-" + portalTabId + "'),document.getElementById('extend" + PortletId + "-" + portalTabId + "'),'" + portalTabId + "','" + currDate + "')\" name=\"done" + PortletId + "-" + portalTabId + "\" id=\"done" + PortletId + "-" + portalTabId + "\" value='Done'></td>");
                tableBuffer.append("<td align='center'><input type=\"button\" class=\"navtitle-hover\" onclick=\"closeTable('" + PortletId + "',document.getElementById('EXT" + PortletId + "-" + portalTabId + "'),'" + portalTabId + "')\" name=\"cancel" + PortletId + "-" + portalTabId + "\" id=\"cancel" + PortletId + "-" + portalTabId + "\" value='Cancel'></td></tr>");
                tableBuffer.append("</table>");
                tableBuffer.append(" </div>");
                tableBuffer.append("</td></tr></table></td>");
                //apply color starts
                tableBuffer.append("<td>");
                tableBuffer.append("<a href='javascript:void(0)'  class=\"ui-icon ui-icon-pencil\"  onclick=\"applyColor('" + PortletId + "')\"  style='text-decoration:none' class=\"calcTitle\" title=\"ApplyColor\"><font size=\"1px\"><b> </b> </font></a> &nbsp;&nbsp;");
                tableBuffer.append("<div style='display:none;width:auto;height:25px;background-color:#ffffff;overflow:auto;position:absolute;text-align:left;border:1px solid #000000;border-top-width: 0px;' id='colordivId" + PortletId + "'>");
                tableBuffer.append("<table><tr><td>");
                tableBuffer.append("<select id=\"measurefortable" + PortletId + "\" title=\"select measure\">");
                ArrayList Measures = null, MeasureNames = null, AggregationType = null;
                if (TableHashMap.get("Measures") != null) {
                    Measures = (ArrayList) TableHashMap.get("Measures");
                    MeasureNames = (ArrayList) TableHashMap.get("MeasuresNames");
                    AggregationType = (ArrayList) TableHashMap.get("AggregationType");
                }
                if (Measures.size() != 0 && MeasureNames.size() != 0 && AggregationType.size() != 0) {
                    for (int i = 0; i < Measures.size(); i++) {
                        //  
                        tableBuffer.append("<option value='A_" + Measures.get(i) + "," + AggregationType.get(i) + "," + MeasureNames.get(i) + "' >" + MeasureNames.get(i) + "</option>");
                    }
                }
                tableBuffer.append("</select></td><td align=\"left\">");
                tableBuffer.append("<input type=\"Button\" onclick=\"applyPortletcolor('measurefortable" + PortletId + "','" + portalTabId + "','" + PortletId + "')\" value=\"Go\" class=\"navtitle-hover\" >");
                tableBuffer.append("</td></tr></table>");
                tableBuffer.append("</div>");
                tableBuffer.append("</td>");
                //apply color ends
                //reset table start
                tableBuffer.append("<td>");
                //modified by anitha
                tableBuffer.append("<a class=\"ui-icon ui-icon-refresh\" style='text-decoration:none' href=\"javascript:resetTablePortlet('" + PortletId + "',document.getElementsByName('chkREP-" + PortletId + "-" + portalTabId + "'),document.getElementsByName('chkCEP-" + PortletId + "-" + portalTabId + "'),'" + portalTabId + "','" + currDate + "')\" title=\"Reset\"><font size=\"1px\"><b></b></font></a>");
                tableBuffer.append("</td>");
                //reset table ends
                tableBuffer.append("<td align=\"right\"> ");
                tableBuffer.append("<table border=0><tr><td>&nbsp;&nbsp;</td> ");
                tableBuffer.append("<td>");
                tableBuffer.append("<a href='javascript:void(0)' onclick=\"openTopBottomForTable('" + PortletId + "',document.getElementsByName('chkREP-" + PortletId + "-" + portalTabId + "'),document.getElementById('REP" + PortletId + "-" + portalTabId + "'),'" + perBy + "','" + graphType + "','" + portalTabId + "','" + currDate + "','" + callFrom + "')\" style='text-decoration:none'class=\"ui-icon ui-icon-triangle-2-n-s\" class=\"calcTitle\" title=\"sort\"><font size=\"1px\"> <b></b> </font></a>&nbsp;&nbsp;");
                tableBuffer.append("<div style='display:none;width:auto;background-color:#ffffff;overflow:auto;position:absolute;text-align:left;border:1px solid;border-top-width:0px' id='TopBottom" + PortletId + "-" + portalTabId + "'>");
                tableBuffer.append("<table><tr><td>");
                tableBuffer.append("<table><tr>");
                tableBuffer.append("<td>");
                if (portletSorterHelper != null && portletSorterHelper.getSortType().equalsIgnoreCase("Top")) {
                    tableBuffer.append("<input type='radio' name='sortTable" + PortletId + "-" + portalTabId + "' value=\"Top\" id='sortTable" + PortletId + "-" + portalTabId + "'checked>Top");
                } else {
                    tableBuffer.append("<input type='radio' name='sortTable" + PortletId + "-" + portalTabId + "' value=\"Top\" id='sortTable" + PortletId + "-" + portalTabId + "'>Top");
                }
                tableBuffer.append("</td>");
                tableBuffer.append("<td>");
                if (portletSorterHelper != null && portletSorterHelper.getSortType().equalsIgnoreCase("Bottom")) {
                    tableBuffer.append("<input type='radio' name='sortTable" + PortletId + "-" + portalTabId + "' value=\"Bottom\" id='sortTable" + PortletId + "-" + portalTabId + "'checked>Bottom");
                } else {
                    tableBuffer.append("<input type='radio' name='sortTable" + PortletId + "-" + portalTabId + "' value=\"Bottom\" id='sortTable" + PortletId + "-" + portalTabId + "'>Bottom");
                }
                tableBuffer.append("</td>");
                tableBuffer.append("<td>");
                if (portletSorterHelper != null && portletSorterHelper.getIsSortAll()) {
                    tableBuffer.append("<input type='checkbox' id='sortCheckbox" + PortletId + "-" + portalTabId + "' onclick=\"onChangeCheckbox('" + PortletId + "','" + portalTabId + "')\" value='true'name='sortCheckbox" + PortletId + "-" + portalTabId + "' title='Display all rows' checked>ALL");
                } else {
                    tableBuffer.append("<input type='checkbox' id='sortCheckbox" + PortletId + "-" + portalTabId + "' onclick=\"onChangeCheckbox('" + PortletId + "','" + portalTabId + "')\" value='true'name='sortCheckbox" + PortletId + "-" + portalTabId + "' title='Display all rows'>ALL");
                }
                tableBuffer.append("</td>");
                tableBuffer.append("</tr></table>");
                tableBuffer.append("</td></tr></table>");
                tableBuffer.append("<table><tr>");
                tableBuffer.append("<td><select id='sortBy" + PortletId + "-" + portalTabId + "'>");
                /*
                 * if(REPNames.size()!=0) { for(int i=0;i<REPNames.size();i++) {
                 * tableBuffer.append("<option
                 * value="+REPList.get(i)+">"+REPNames.get(i)+"</option>"); } }
                 * if(CEPNames.size()!=0) { for(int i=0;i<CEPNames.size();i++) {
                 * tableBuffer.append("<option
                 * value="+CEPList.get(i)+">"+CEPNames.get(i)+"</option>"); }
           }
                 */
                if (TableHashMap != null) {
                    ArrayList MeasVal = (ArrayList) TableHashMap.get("Measures");
                    ArrayList MeasNameList = (ArrayList) TableHashMap.get("MeasuresNames");
                    for (int i = 0; i < MeasNameList.size(); i++) {
                        if (portletSorterHelper != null && portletSorterHelper.getSortByColumeVal() == MeasVal.get(i).toString()) {
                            tableBuffer.append("<option value=" + MeasVal.get(i) + " selected>" + MeasNameList.get(i) + "</option>");
                        } else {
                            tableBuffer.append("<option value=" + MeasVal.get(i) + ">" + MeasNameList.get(i) + "</option>");
                        }
                    }
                }

                tableBuffer.append("</select></td>");
                tableBuffer.append("<td>");
                if (portletSorterHelper != null && portletSorterHelper.getIsSortAll()) {
                    tableBuffer.append("<input type='input' style='width:60px;display: none' onkeypress=\"return isNumberKey(event)\"  id='sortTextbox" + PortletId + "-" + portalTabId + "' name='sortTextbox" + PortletId + "-" + portalTabId + "'/>");
                } else if (portletSorterHelper != null && !portletSorterHelper.getIsSortAll()) {
                    tableBuffer.append("<input type='input' style='width:60px' onkeypress=\"return isNumberKey(event)\"  id='sortTextbox" + PortletId + "-" + portalTabId + "' name='sortTextbox" + PortletId + "-" + portalTabId + "' value=" + portletSorterHelper.getCountVal() + ">");
                } else {
                    tableBuffer.append("<input type='input' style='width:60px' onkeypress=\"return isNumberKey(event)\"  id='sortTextbox" + PortletId + "-" + portalTabId + "' name='sortTextbox" + PortletId + "-" + portalTabId + "'/>");
                }
                tableBuffer.append("</td>");
                tableBuffer.append("</tr></table>");
                tableBuffer.append("</td></tr></table>");
                tableBuffer.append("</div>");
                tableBuffer.append("</td>");
                tableBuffer.append("<td>");
                tableBuffer.append("<a href='javascript:void(0)' class=\"ui-icon ui-icon-arrowthickstop-1-e\" onclick=\"openTableREPPreviews('" + PortletId + "',document.getElementsByName('chkREP-" + PortletId + "-" + portalTabId + "'),document.getElementsByName('chkCEP-" + PortletId + "-" + portalTabId + "'),document.getElementById('REP" + PortletId + "-" + portalTabId + "'),'','','" + portalTabId + "','" + currDate + "')\"  style='text-decoration:none' class=\"calcTitle\" title=\"Row Edge\"><font size=\"1px\">  </font></a>&nbsp;&nbsp; ");
                tableBuffer.append("<div style='display:none;width:auto;height:auto;background-color:#ffffff;overflow:auto;position:absolute;text-align:left;border:1px solid #000000;border-top-width: 0px;' id='REP" + PortletId + "-" + portalTabId + "'>");
                tableBuffer.append("<Table>");
                for (int i = 0; i < reportParameters.values().toArray().length; i++) {
                    tableBuffer.append("<Tr valign='top'>");
                    tableBuffer.append("<Td valign='top'>");

                    if (REPList.toString().contains(reportParameters.values().toArray()[i].toString().split(",")[0].replace("[", ""))) {
                        tableBuffer.append("<input type=\"checkbox\" checked name=\"chkREP-" + PortletId + "-" + portalTabId + "\" id=\"chkREP-" + PortletId + "-" + portalTabId + "\" value='" + reportParameters.values().toArray()[i].toString().split(",")[0].replace("[", "") + "~" + reportParameters.values().toArray()[i].toString().split(",")[1].replace("[", "") + "'>");
                    } else {
                        tableBuffer.append("<input type=\"checkbox\" name=\"chkREP-" + PortletId + "-" + portalTabId + "\" id=\"chkREP-" + PortletId + "-" + portalTabId + "\" value='" + reportParameters.values().toArray()[i].toString().split(",")[0].replace("[", "") + "~" + reportParameters.values().toArray()[i].toString().split(",")[1].replace("[", "") + "'>");
                    }
                    tableBuffer.append("<b><font size=\"1px\">" + reportParameters.values().toArray()[i].toString().split(",")[1] + "<font size=\"1px\"></b>");
                    tableBuffer.append("</Td>");
                    tableBuffer.append("</Tr>");


                }
                tableBuffer.append("</Table>");//closing of inner table
                tableBuffer.append(" </div>");//inner div 1 ends
                tableBuffer.append(" </td>");//inner div 1 ends

                tableBuffer.append("<td>");
                tableBuffer.append("<a href='javascript:void(0)' class=\"ui-icon ui-icon-arrowthickstop-1-s\" onclick=\"openTableCEPPreviews('" + PortletId + "',document.getElementsByName('chkREP-" + PortletId + "-" + portalTabId + "'),document.getElementsByName('chkCEP-" + PortletId + "-" + portalTabId + "'),document.getElementById('CEP" + PortletId + "-" + portalTabId + "'),'','','" + portalTabId + "','" + currDate + "')\"  style='text-decoration:none' class=\"calcTitle\" title=\"Column Edge\"><font size=\"1px\">  </font></a>&nbsp;&nbsp; ");
                tableBuffer.append("<div style='display:none;width:auto;height:auto;background-color:#ffffff;overflow:auto;position:absolute;text-align:left;border:1px solid #000000;border-top-width: 0px;' id='CEP" + PortletId + "-" + portalTabId + "'>");
                tableBuffer.append("<Table>");
                for (int i = 0; i < reportParameters.values().toArray().length; i++) {
                    tableBuffer.append("<Tr valign='top'>");
                    tableBuffer.append("<Td valign='top'>");

                    if (CEPList.toString().contains(reportParameters.values().toArray()[i].toString().split(",")[0].replace("[", ""))) {
                        tableBuffer.append("<input type=\"checkbox\" checked name=\"chkCEP-" + PortletId + "-" + portalTabId + "\" id=\"chkCEP-" + PortletId + "-" + portalTabId + "\" value='" + reportParameters.values().toArray()[i].toString().split(",")[0].replace("[", "") + "~" + reportParameters.values().toArray()[i].toString().split(",")[1].replace("[", "") + "'>");
                    } else {
                        tableBuffer.append("<input type=\"checkbox\" name=\"chkCEP-" + PortletId + "-" + portalTabId + "\" id=\"chkCEP-" + PortletId + "-" + portalTabId + "\" value='" + reportParameters.values().toArray()[i].toString().split(",")[0].replace("[", "") + "~" + reportParameters.values().toArray()[i].toString().split(",")[1].replace("[", "") + "'>");
                    }
                    tableBuffer.append("<b><font size=\"1px\">" + reportParameters.values().toArray()[i].toString().split(",")[1] + "<font size=\"1px\"></b>");
                    tableBuffer.append("</Td>");
                    tableBuffer.append("</Tr>");


                }
                tableBuffer.append("</Table>");//closing of inner table
                tableBuffer.append(" </div>");//inner div 1 ends

                tableBuffer.append(" </td>");//inner div 1 ends
                tableBuffer.append("<td>");
                //icons pinvoke\cross.png
                //tableBuffer.append("<img ><a style='text-decoration:none' href=\"javascript:deletePortlet('" + PortletId + "','" + String.valueOf(metaInfo.get("PortletName")) + "','" + portalTabId + "')\" title='Delete '" + String.valueOf(metaInfo.get("PortletName")) + "' ><font size=\"1px\"><b>Delete</b></font></a>");
                tableBuffer.append("<a class=\"ui-icon ui-icon-trash\" style='text-decoration:none' href=\"javascript:deletePortlet('" + PortletId + "','" + String.valueOf(metaInfo.get("PortletName")) + "','" + portalTabId + "')\" title='Delete '" + String.valueOf(metaInfo.get("PortletName")) + "' ></a>");
                tableBuffer.append(" </td>");

                tableBuffer.append("</tr></table>");

                tableBuffer.append("</td> </tr></table>");

                tableBuffer.append("</Td>");
                tableBuffer.append("</tr>");
                tableBuffer.append("</table>");
                tableBuffer.append(" </div>");
            }
            if (!"ONEVIEW".equals(oneViewCheck)) {
                tableBuffer.append("<div style=\"width:415px;overflow-y:auto;overflow-x:auto\">");
            }
            tableBuffer.append(tableString);
            if (!"ONEVIEW".equals(oneViewCheck)) {
                tableBuffer.append("</div>");
            }
            //content div ends
        } else if (displayType.equalsIgnoreCase("kpi") || displayType.equalsIgnoreCase("BasicTarget")) {
            //ArrayList grpsArray = new ArrayList();
            ParametersHashMap = new HashMap();
            dets = new ArrayList();
            detnames = new ArrayList();
            StringBuffer KPIIds = new StringBuffer("");

            for (int i = 0; i < reportParameters.values().toArray().length; i++) {
                dets.add(reportParameters.values().toArray()[i].toString().split(",")[0].replace("[", ""));
                detnames.add(reportParameters.values().toArray()[i].toString().split(",")[1]);
            }

            ParametersHashMap.put("Parameters", dets);
            ParametersHashMap.put("TimeDetailstList", timeDetailsArray);
            ParametersHashMap.put("TimeDimHashMap", timeDetailsMap);
            ParametersHashMap.put("ParametersNames", detnames);

            for (int i = 0; i < reportQryElementIds.size(); i++) {
                KPIIds.append("," + String.valueOf(reportQryElementIds.get(i)));
            }
//            String tableString = dashboardTempBD.dashboardKpiPreview(KPIIds.toString().substring(1), ParametersHashMap);

            String tableString = "";

            this.getPortalKpi(KPIIds.toString().substring(1));
            tableString = this.processPortalKpi("1", PortletId, portalTabId);

            tableBuffer.append("<div class=\"portlet-header portletHeader ui-corner-all\">");//header div starts
            tableBuffer.append("<table style='height:10px;width:100%'>");//header table
            tableBuffer.append(" <tr valign='top' align='right'>");
            tableBuffer.append(" <td valign='top' style='height:10px;width:100%'  >");
            tableBuffer.append("<table width=\"100%\">");
            tableBuffer.append("<tr>");
            tableBuffer.append("<td align=\"left\" style='color:#369;font-weight:bold'>");
            tableBuffer.append("<a href='javascript:void(0)' onclick=\"editPortletName('" + PortletId + "','" + portalTabId + "','" + PortletName + "','" + currDate + "','" + callFrom + "')\"  style='text-decoration:none' class=\"calcTitle\" title=\"Edit Name\"><font size=\"1px\"><span id='" + PortletId + "_span'><b>" + PortletName + "</b> </span> </font></a>");
            //tableBuffer.append(PortletName);
            //tableBuffer.append(String.valueOf(metaInfo.get("PortletName")));
            tableBuffer.append("</td>");
            tableBuffer.append("<td align=\"right\"> ");
            tableBuffer.append("<table border=0>");
            tableBuffer.append("<tr>");
            tableBuffer.append("<td>&nbsp;&nbsp;</td>");
            tableBuffer.append("<td>");
            tableBuffer.append("<a class=\"ui-icon ui-icon-trash\" style='text-decoration:none' href=\"javascript:deletePortlet('" + PortletId + "','" + String.valueOf(metaInfo.get("PortletName")) + "','" + portalTabId + "')\" title='Delete '" + String.valueOf(metaInfo.get("PortletName")) + "' ></a>");
            tableBuffer.append(" </td>");
            tableBuffer.append("</tr>");
            tableBuffer.append("</table>");
            tableBuffer.append("</td>");

            /*
             * tableBuffer.append("<td align=\"right\"> ");
             * tableBuffer.append("<table border=0><tr><td>&nbsp;&nbsp;</td>
             * <td>"); tableBuffer.append("<a href='javascript:void(0)'
             * onclick=\"openTableREPPreviews('" + PortletId +
             * "',document.getElementsByName('chkREP-" + PortletId +
             * "'),document.getElementsByName('chkCEP-" + PortletId +
             * "'),document.getElementById('REP" + PortletId + "'),'','')\"
             * style='text-decoration:none' class=\"calcTitle\" title=\"Row
             * Edge\"><font size=\"1px\"> <b>Row Edge</b>
             * </font></a>&nbsp;&nbsp; "); tableBuffer.append("<div
             * style='display:none;width:auto;height:auto;background-color:#ffffff;overflow:auto;position:absolute;text-align:left;border:1px
             * solid #000000;border-top-width: 0px;' id='REP" + PortletId +
             * "'>");
             *
             * tableBuffer.append("<Table>"); for (int i = 0; i <
             * reportParameters.values().toArray().length; i++) { //REPList
             *
             * tableBuffer.append("<Tr valign='top'>"); tableBuffer.append("<Td
             * valign='top'>");
             *
             * if
             * (REPList.toString().contains(reportParameters.values().toArray()[i].toString().split(",")[0].replace("[",
             * ""))) { tableBuffer.append("<input type=\"checkbox\" checked
             * name=\"chkREP-" + PortletId + "\" id=\"chkREP-" + PortletId + "\"
             * value='" +
             * reportParameters.values().toArray()[i].toString().split(",")[0].replace("[",
             * "") + "~" +
             * reportParameters.values().toArray()[i].toString().split(",")[1].replace("[",
             * "") + "'>"); } else { tableBuffer.append("<input
             * type=\"checkbox\" name=\"chkREP-" + PortletId + "\" id=\"chkREP-"
             * + PortletId + "\" value='" +
             * reportParameters.values().toArray()[i].toString().split(",")[0].replace("[",
             * "") + "~" +
             * reportParameters.values().toArray()[i].toString().split(",")[1].replace("[",
             * "") + "'>"); } tableBuffer.append("<b><font size=\"1px\">" +
             * reportParameters.values().toArray()[i].toString().split(",")[1] +
             * "<font size=\"1px\"></b>"); tableBuffer.append("</Td>");
             * tableBuffer.append("</Tr>");
             *
             *
             * }
             * tableBuffer.append("</Table>");//closing of inner table
             * tableBuffer.append(" </div>");//inner div 1 ends
             *
             * tableBuffer.append(" </td> <td>");//inner div 1 ends
             * tableBuffer.append("<a href='javascript:void(0)'
             * onclick=\"openTableCEPPreviews('" + PortletId +
             * "',document.getElementsByName('chkREP-" + PortletId +
             * "'),document.getElementsByName('chkCEP-" + PortletId +
             * "'),document.getElementById('CEP" + PortletId + "'),'','')\"
             * style='text-decoration:none' class=\"calcTitle\" title=\"Column
             * Edge\"> <font size=\"1px\"><b>Column Edge</b></font> </a>");
             * tableBuffer.append("<div
             * style='display:none;width:auto;height:auto;background-color:#ffffff;overflow:auto;position:absolute;text-align:left;border:1px
             * solid #000000;border-top-width: 0px;' id='CEP" + PortletId +
             * "'>"); tableBuffer.append("<Table>");
             *
             * for (int i = 0; i < reportParameters.values().toArray().length;
             * i++) { tableBuffer.append("<Tr valign='top'>");
             * tableBuffer.append("<Td valign='top'>"); if
             * (CEPList.toString().contains(reportParameters.values().toArray()[i].toString().split(",")[0].replace("[",
             * ""))) { tableBuffer.append("<input type=\"checkbox\" checked
             * name=\"chkCEP-" + PortletId + "\" id=\"chkCEP-" + PortletId + "\"
             * value='" +
             * reportParameters.values().toArray()[i].toString().split(",")[0].replace("[",
             * "") + "~" +
             * reportParameters.values().toArray()[i].toString().split(",")[1].replace("[",
             * "") + "'>"); } else { tableBuffer.append("<input
             * type=\"checkbox\" name=\"chkCEP-" + PortletId + "\" id=\"chkCEP-"
             * + PortletId + "\" value='" +
             * reportParameters.values().toArray()[i].toString().split(",")[0].replace("[",
             * "") + "~" +
             * reportParameters.values().toArray()[i].toString().split(",")[1].replace("[",
             * "") + "'>"); }
             *
             * tableBuffer.append("<b><font size=\"1px\">" +
             * reportParameters.values().toArray()[i].toString().split(",")[1] +
             * "<font size=\"1px\"></b>"); tableBuffer.append("</Td>");
             * tableBuffer.append("</Tr>");
             *
             *
             * }
             * tableBuffer.append("</Table>");//closing of inner table
             *
             * tableBuffer.append(" </div>");//inner div 2 ends
             *
             * tableBuffer.append("<a style='text-decoration:none'
             * href=\"javascript:deletePortlet('" + PortletId + "','" +
             * String.valueOf(metaInfo.get("PortletName")) + "','" + portalTabId
             * + "')\" title='Delete '" +
             * String.valueOf(metaInfo.get("PortletName")) + "' ><font
             * size=\"1px\"><b>Delete</b></font></a>"); tableBuffer.append("
             * </td>"); tableBuffer.append("</tr>");
             * tableBuffer.append("</table>");
             *
             * tableBuffer.append("</td>");
             */


            tableBuffer.append("</tr>");
            tableBuffer.append("</table>");

            tableBuffer.append("</Td>");
            tableBuffer.append("</tr>");
            tableBuffer.append("</table>");
            tableBuffer.append(" </div>");
            //header div ends

            //content div starts
            tableBuffer.append("<div style=\"width:415px;height:300px;overflow-y:auto;overflow-x:auto\">");
            tableBuffer.append("<table width=\"100%\">");
            tableBuffer.append(" <tr valign='top'>");
            tableBuffer.append("<td valign='top' style=\"color:#369;font-weight:bold\">");
            tableBuffer.append(tableString);
            // portletPreviewSB.append(tableString);
            tableBuffer.append(" </td>");
            //tableBuffer.append(" <td valign='top' align=\"right\"> </td>");
            tableBuffer.append("</tr>");
            tableBuffer.append("</table>");
            tableBuffer.append("</div>");
            //content div ends

        } //end of code for displaying KPI Table
        else if (displayType.equalsIgnoreCase("Graph")) {
            String oneViewCheck = "";
            String height = "";
            String width = "";
            oneViewCheck = (String) session.getAttribute("ONEVIEW");
            height = (String) session.getAttribute("height");
            width = (String) session.getAttribute("width");
            PortLetSorterHelper portletSorterHelper = portLet.getPortletSorterHelper();

            PbGraphDisplay GraphDisplay = new PbGraphDisplay();
            ProgenChartDisplay[] pcharts = null;
            String graphClass = String.valueOf(getGraphDeails().get("graphClass"));
            ArrayList grpsArray = new ArrayList();
            ArrayList grpsTitlesArray = new ArrayList();
            ArrayList grpsImagesArray = new ArrayList();
            ArrayList<String> grpElementIds = new ArrayList<String>();
            ArrayList<String> repQryElementIds = new ArrayList<String>();
            ArrayList grpElementNames = new ArrayList();
            String path = "";
            String graphTitle = "";
            String viewByID = "";
            String nextViewBy = "";
            boolean setDrillforTime = false;
            LinkedHashMap repParams = new LinkedHashMap();
            //String pathZoom = "";

            String[] barChartColumnNames = null;
            String[] barChartColumnTitles = null;
            String[] pieChartColumns = null;
            String[] viewByElementIds = null;
            String[] viewByColumnNames = null;
            String sortColumeVal = "";
            String[] axis = null;
            String measureEId = "", measureEname = "";
            ParametersHashMap = new HashMap();
            //HashMap TableHashMap = new HashMap();
            dets = new ArrayList();
            detnames = new ArrayList();
            StringBuffer KPIIds = new StringBuffer("");
            String tableString = "";
            PbReturnObject pbretObj = null;

//            HashMap<String,String> portletGrpMap=null;

            for (int i = 0; i < reportParameters.values().toArray().length; i++) {
                dets.add(reportParameters.values().toArray()[i].toString().split(",")[0].replace("[", ""));
                detnames.add(reportParameters.values().toArray()[i].toString().split(",")[1]);
            }
            ParametersHashMap.put("Parameters", dets);
            ParametersHashMap.put("TimeDetailstList", timeDetailsArray);
            ParametersHashMap.put("TimeDimHashMap", timeDetailsMap);
            ParametersHashMap.put("ParametersNames", detnames);

            if (REP.equalsIgnoreCase("")) {
                grpElementIds = new ArrayList();
                REPList = reportRowViewbyValues;
                viewByElementIds = (String[]) REPList.toArray(new String[0]);
                viewByColumnNames = (String[]) REPList.toArray(new String[0]);
                REPNames = new ArrayList();
                for (int k = 0; k < REPList.size(); k++) {
                    if (REPList.get(k).toString().equalsIgnoreCase("TIME")) {
                        // REPList.add("TIME");
                        REPNames.add("Time");
                    } else {
                        REPNames.add(getParameterNamesHashMap().get(String.valueOf(REPList.get(k))));
                    }
                }
                for (int j = 0; j < REPList.size(); j++) {
                    if (String.valueOf(REPList.get(j)).equalsIgnoreCase("TIME")) {
                        grpElementIds.add("TIME");
                        grpElementNames.add(String.valueOf(REPNames.get(j)));
                        viewByColumnNames[j] = "Time";
                        viewByElementIds[j] = "TIME";
                    } else {
                        viewByElementIds[j] = "A_" + viewByElementIds[j];
                        viewByColumnNames[j] = String.valueOf(REPNames.get(j));
                        if (!(grpElementIds.contains("A_" + String.valueOf(REPList.get(j))))) {
                            grpElementIds.add("A_" + String.valueOf(REPList.get(j)));
                            grpElementNames.add(String.valueOf(REPNames.get(j)));
                        }
                    }
                }
            } else {
                grpElementIds = new ArrayList();
                REPNames = new ArrayList();
                REPList = new ArrayList();
                String[] REPStr = REP.split(",");
                viewByElementIds = (String[]) REPStr.clone();
                viewByColumnNames = (String[]) REPStr.clone();
                for (int k = 0; k < REPStr.length; k++) {
                    if (REPStr[k].equalsIgnoreCase("TIME")) {
                        REPList.add("TIME");
                        REPNames.add("Time");
                    } else {
                        REPList.add(REPStr[k]);
                        REPNames.add(getParameterNamesHashMap().get(REPStr[k]));
                    }
                }
                for (int j = 0; j < REPList.size(); j++) {
                    if (String.valueOf(REPList.get(j)).equalsIgnoreCase("TIME")) {
                        grpElementIds.add("TIME");
                        grpElementNames.add(String.valueOf(REPNames.get(j)));
                        viewByColumnNames[j] = "Time";
                        viewByElementIds[j] = "TIME";
                    } else {
                        viewByElementIds[j] = "A_" + viewByElementIds[j];
                        viewByColumnNames[j] = String.valueOf(REPNames.get(j));
                        if (!(grpElementIds.contains("A_" + String.valueOf(REPList.get(j))))) {
                            grpElementIds.add("A_" + String.valueOf(REPList.get(j)));
                            grpElementNames.add(String.valueOf(REPNames.get(j)));
                        }
                    }
                }
                pb.setRowViewbyCols(REPList);
                pb.setColViewbyCols(CEPList);
            }
//            CEPNames = new ArrayList();
//            CEPList = new ArrayList();
            TableHashMap.put("REP", REPList);
            TableHashMap.put("REPNames", REPNames);
            TableHashMap.put("CEP", CEPList);
            TableHashMap.put("CEPNames", CEPNames);
            TableHashMap.put("Measures", reportQryElementIds);
            TableHashMap.put("MeasuresNames", reportQryColNames);
            if (perBy != null) {
                if (portletSorterHelper != null) {
                    TableHashMap.put("perByVal", perBy.split("-")[0] + "-" + portletSorterHelper.getCountVal());
                }
            } else {
                TableHashMap.put("perByVal", perBy);
            }
            measureEId = Joiner.on(",").join(reportQryElementIds);
            measureEname = Joiner.on(",").join(reportQryColNames);
//            for(int i=0;i<reportQryElementIds.size();i++){
//                measureEId=measureEId+","+(String)reportQryElementIds.get(i);
//                measureEname=measureEname+","+(String)reportQryColNames.get(i);
//            }
//             pb.setDefaultSortedColumnAndOrder(this.sortColumnsAndOrder);
            pbretObj = pb.getPbReturnObjectCrossChecked(String.valueOf(reportQryElementIds.get(0)), null);
            HashMap timeInfoDetails = pb.getTimememdetails();
            portLet.setTimeInfoDetails(timeInfoDetails);
            pbretObj.resetViewSequence();
            for (int k = 0; k < reportQryElementIds.size(); k++) {
                if (CEPList.isEmpty() && !isUsedinPortlet.contains(reportQryElementIds.get(k))) {
                    grpElementIds.add("A_" + reportQryElementIds.get(k));
                    grpElementNames.add(String.valueOf(reportQryColNames.get(k)));
                }
                if (!CEPList.isEmpty()) {
                    grpElementIds.addAll(pb.crossTabNonViewBy);
                    for (int index = 0; index < pb.crossTabNonViewBy.size(); index++) {
                        grpElementNames.add(pb.crossTabNonViewByMap.get(pb.crossTabNonViewBy.get(index)));
                    }
                }
            }
            if (graphType.equalsIgnoreCase("Table")) {
                tableString = buildTable(ParametersHashMap, TableHashMap, PortletId, portalTabId);

            } else {

                if (graphType != null && (!"".equalsIgnoreCase(graphType))) {
                    graphClass = String.valueOf(GraphClassesHashMap.get(graphType));
                } else {
                    graphType = String.valueOf(getGraphDeails().get("graphType"));
                    graphClass = String.valueOf(getGraphDeails().get("graphClass"));
                }

                getGraphDeails().put("graphClassName", graphClass);
                getGraphDeails().put("graphTypeName", graphType);
                session.setAttribute("grpTypeName", graphType);

//            if(portletGrpMap==null){
//                portletGrpMap=new HashMap<String, String>();
//            }else{
//                portletGrpMap.put(PortletId, graphType);
//            }

                if ("TimeSeries".equalsIgnoreCase(graphType)) {
                    pb.isTimeSeries = true;
                } else {
                    pb.isTimeSeries = false;
                }
//            getGraphDeails().put("graphWidth", "400");//modofied on 05-03-2010
                barChartColumnNames = (String[]) grpElementIds.toArray(new String[0]);
                barChartColumnTitles = new String[grpElementNames.size()];
                ArrayList tempList = new ArrayList();
                for (int grpcount = 0; grpcount < grpElementNames.size(); grpcount++) {
//                    
                    if (grpElementNames.get(grpcount).getClass().toString().equalsIgnoreCase("class java.lang.String")) {
                        barChartColumnTitles[grpcount] = grpElementNames.get(grpcount).toString();
                    } else if (grpElementNames.get(grpcount).getClass().toString().equalsIgnoreCase("class java.util.ArrayList")) {
                        tempList = (ArrayList) ((ArrayList) grpElementNames.get(grpcount)).clone();
                        tempList.removeAll(reportQryColNames);
                        barChartColumnTitles[grpcount] = Joiner.on(",").join(tempList);
//                         barChartColumnTitles[grpcount] =Joiner.on(",").join((ArrayList)grpElementNames.get(grpcount)) ;
                    }
                }
                pieChartColumns = barChartColumnNames;
                axis = new String[barChartColumnNames.length];
                for (int i = 0; i < axis.length; i++) {
                    axis[i] = "0";
                }
                HashMap GraphHashMap = new HashMap();
                if (getPortletProperty() != null) {
                    GraphProperty graphProperty = getPortletProperty();
//                    graphProperty.setShowxAxis(Joiner.on(",").join(reportQryColNames));
                    getGraphDeails().put("GraphProperty", graphProperty);
                    getGraphDeails().put("graphLegendLoc", graphProperty.getGraphLegendLoc());
                    getGraphDeails().put("SwapColumn", graphProperty.getSwapGraphColumns());
                    getGraphDeails().put("showGT", graphProperty.getShowGT());
                    getGraphDeails().put("graphSize", graphProperty.getGrpSize());
                    getGraphDeails().put("nbrFormat", graphProperty.getNumberFormat());
                    getGraphDeails().put("graphSymbol", graphProperty.getSymbol());
                    getGraphDeails().put("graphGridLines", graphProperty.getGraphGridLines());
                    getGraphDeails().put("showMinMaxRange", graphProperty.getMinMaxRange());
                    getGraphDeails().put("graphDisplayRows", graphProperty.getGraphDisplayRows());
                    if (graphProperty.getGraphDisplayRows() == null || graphProperty.getGraphDisplayRows().equalsIgnoreCase("10")) {
                        if (portletSorterHelper != null) {
                            if (portletSorterHelper.getCountVal() == 0) {
                                getGraphDeails().put("graphDisplayRows", "All");
                            } else {
                                getGraphDeails().put("graphDisplayRows", portletSorterHelper.getCountVal());
                            }
                        }
                    }
                    if (oneViewCheck != null && !oneViewCheck.equalsIgnoreCase("") && "ONEVIEW".equals(oneViewCheck)) {
                        if (height != null && !height.equalsIgnoreCase("") && width != null && !width.equalsIgnoreCase("")) {
                            getGraphDeails().put("graphHeight", height);
                            getGraphDeails().put("graphWidth", width);
                        }
                    }
                } else {
                    GraphProperty graphProperty = new GraphProperty();
//                    graphProperty.setShowxAxis(Joiner.on(",").join(reportQryColNames));
                    getGraphDeails().put("GraphProperty", graphProperty);
                    getGraphDeails().put("graphWidth", "400");//modofied on 05-03-2010
                    if (graphProperty.getGraphDisplayRows() == null || graphProperty.getGraphDisplayRows().equalsIgnoreCase("10")) {
                        if (portletSorterHelper != null) {
                            if (portletSorterHelper.getCountVal() == 0) {
                                getGraphDeails().put("graphDisplayRows", "All");
                            } else {
                                getGraphDeails().put("graphDisplayRows", portletSorterHelper.getCountVal());
                            }
                        }
                    }
                    if (oneViewCheck != null && !oneViewCheck.equalsIgnoreCase("") && "ONEVIEW".equals(oneViewCheck)) {
                        if (height != null && !height.equalsIgnoreCase("") && width != null && !width.equalsIgnoreCase("")) {
                            getGraphDeails().put("graphHeight", height);
                            getGraphDeails().put("graphWidth", width);
                        }
                    }
                }
                GraphHashMap.put("1", getGraphDeails());
                getGraphDeails().put("barChartColumnNames", barChartColumnNames);
                getGraphDeails().put("barChartColumnTitles", barChartColumnTitles);
                getGraphDeails().put("pieChartColumns", pieChartColumns);
                getGraphDeails().put("viewByElementIds", viewByElementIds);
                getGraphDeails().put("axis", axis);

//            getGraphDeails().put("graphSize","Large");
//             GraphProperty graphProperty = new GraphProperty();
//             graphProperty.setLabelsDisplayed(true);
//              getGraphDeails().put("GraphProperty", graphProperty);
                GraphHashMap.put("graphIds", "1");
                GraphHashMap.put("isGraphsExists", "true");

                //added by santhosh.k on 05-03-2010
                GraphDisplay.setCtxPath(request.getContextPath());
                GraphDisplay.setSession(request.getSession(false));
                GraphDisplay.setResponse(response);
                GraphDisplay.setOut(response.getWriter());
                GraphDisplay.setGraphHashMap(GraphHashMap);


                if ("TimeSeries".equalsIgnoreCase(graphType)) {
                    GraphDisplay.setTimelevel("true");
                } else {
                    GraphDisplay.setTimelevel("false");
                }
                /*
                 * if (perBy == null || ("".equalsIgnoreCase(perBy))){ perBy =
                 * "Top-10"; } else { if ( this.sortColumnsAndOrder != null ) {
                 * Set<String> elementKeys = this.sortColumnsAndOrder.keySet();
                 * String order; if ( perBy.contains("Top") ) order = "DESC";
                 * else order = "ASC";
                 *
                 * for ( String element : elementKeys ) {
                 * this.sortColumnsAndOrder.put(element,order); } }
                }
                 */

//                pb.setDefaultSortedColumnAndOrder(this.sortColumnsAndOrder);
//                if(!reportQryElementIds.isEmpty())
//                pbretObj = pb.getPbReturnObject(String.valueOf(reportQryElementIds.get(0)));
//                else
//                 pbretObj=new PbReturnObject();
//                pbretObj.resetViewSequence();
                if (pbretObj.getRowCount() > 0) {
//            for (int k = 0; k < reportQryElementIds.size(); k++) {
                    if (portletSorterHelper != null) {
                        repQryElementIds.add("A_" + portletSorterHelper.getSortByColumeVal());
                    } else {
                        repQryElementIds.add("A_" + reportQryElementIds.get(0));
                    }
//            }
                    ArrayList<Integer> viewSeqList = new ArrayList<Integer>();
                    ArrayList<SortColumn> sortColumns = new ArrayList<SortColumn>();
                    SortColumn sortColumn;
                    if (pbretObj != null) {
                        int count = pbretObj.getRowCount();
                        String viewPerBy = "Top";
                        char[] sortType = new char[1];
                        char[] sortDataType = new char[1];


                        if (perBy != null && (!"".equalsIgnoreCase(perBy))) {
//                            viewPerBy = perBy.split("-")[0];
//                            count = Integer.parseInt(perBy.split("-")[1]);
                            if (portletSorterHelper != null) {
                                viewPerBy = portletSorterHelper.getSortType();
                                count = portletSorterHelper.getCountVal();
                                sortColumeVal = portletSorterHelper.getSortByColumeVal();
                                if (portletSorterHelper.getIsSortAll()) {
                                    count = pbretObj.getRowCount();
                                }
                            } else {
                                viewPerBy = perBy.split("-")[0];
                                count = Integer.parseInt(perBy.split("-")[1]);
                            }
                            if (pbretObj.getRowCount() < count) {
                                count = pbretObj.getRowCount();
                            }
                            for (String column : repQryElementIds) {
                                if (viewPerBy.equalsIgnoreCase("Top")) {
                                    sortColumn = new SortColumn(column, SortOrder.DESCENDING);
                                } else {
                                    sortColumn = new SortColumn(column, SortOrder.ASCENDING);
                                }
                                sortColumns.add(sortColumn);
                            }
                            if (REPList.get(0).toString().equalsIgnoreCase("TIME")) {
                                sortPbretObj = new PbReturnObject();
                                viewSeqList = pbretObj.getViewSequence();
                                sortPbretObj = pbretObj;
                                sortPbretObj.setViewSequence(viewSeqList);
                            } else if (viewPerBy.contains("Top")) {
                                sortPbretObj = new PbReturnObject();
                                if (CEPList.isEmpty()) {
                                    viewSeqList = pbretObj.findTopBottom(sortColumns, count);
                                } else {
                                    viewSeqList = pbretObj.getViewSequence();
                                }
                                sortPbretObj = pbretObj;
                                sortPbretObj.setViewSequence(viewSeqList);
                            } else {
                                sortPbretObj = new PbReturnObject();
                                if (CEPList.isEmpty()) {
                                    viewSeqList = pbretObj.findTopBottom(sortColumns, count);
                                } else {
                                    viewSeqList = pbretObj.getViewSequence();
                                }
                                sortPbretObj = pbretObj;
                                sortPbretObj.setViewSequence(viewSeqList);
                            }

                        } else {
                            if (portletSorterHelper != null) {
                                viewPerBy = portletSorterHelper.getSortType();
                                count = portletSorterHelper.getCountVal();
                                sortColumeVal = portletSorterHelper.getSortByColumeVal();
                                if (portletSorterHelper.getIsSortAll()) {
                                    count = pbretObj.getRowCount();
                                }
                            }
                            perBy = viewPerBy + '-' + count;
                            for (String column : repQryElementIds) {
                                sortColumn = new SortColumn(column, SortOrder.DESCENDING);
                                sortColumns.add(sortColumn);
                            }
                            if (CEPList.isEmpty()) {
                                viewSeqList = pbretObj.findTopBottom(sortColumns, count);
                            } else {
                                viewSeqList = pbretObj.getViewSequence();
                            }
                            sortPbretObj = pbretObj;
                            sortPbretObj.setViewSequence(viewSeqList);
                        }

                        //added for portlet drill
                        for (int r = 0; r < dets.size(); r++) {
                            if (dets.get(r).toString().equalsIgnoreCase("TIME")) {
                            } else {
                                repParams.put(dets.get(r), "All");
                            }
                        }
//                
//                
                        viewByID = REPList.get(0).toString();
                        if (viewByID.equalsIgnoreCase("TIME")) {
//                    setDrillforTime = true;
//                    nextViewBy="DEC-2008";
                        } else {
                            repCollect.reportIncomingParameters = repParams;
                            nextViewBy = repCollect.getChildCustomDrillforPortal(viewByID);
//                    nextViewBy=viewByID;
                        }
                        if (setDrillforTime) {
//                   nextViewBy = resolveTimeDrill(timeDetailsArray);
                        }
//end
                        GraphDisplay.setAllDispRecordsRetObj(pbretObj);
                        GraphDisplay.setCurrentDispRecordsRetObjWithGT(sortPbretObj);
                        GraphDisplay.setCurrentDispRetObjRecords(sortPbretObj);
                        GraphDisplay.setViewByElementIds(viewByElementIds);
                        GraphDisplay.setViewByColNames(viewByColumnNames);
                        //added for portlet drill
                        if (setDrillforTime) {
                            GraphDisplay.setJscal("portalViewer.do?portalBy=viewPortlet&PORTLETID=" + PortletId + "&REP=" + nextViewBy + "&CEP=" + CEP + "&perBy=" + perBy + "&gpType=" + graphType + "&portalTabId=" + portalTabId + "&currDate=" + currDate);
                        } else {
                            if (count != 0 && count < 10) {
                                GraphDisplay.setJscal("portalViewer.do?portalBy=viewPortlet&PORTLETID=" + PortletId + "&REP=" + nextViewBy + "&CEP=" + CEP + "&perBy=" + "Top-10" + "&gpType=" + graphType + "&portalTabId=" + portalTabId + "&currDate=" + currDate + "&CBOARP" + viewByID + "=");
                            } else {
                                GraphDisplay.setJscal("portalViewer.do?portalBy=viewPortlet&PORTLETID=" + PortletId + "&REP=" + nextViewBy + "&CEP=" + CEP + "&perBy=" + perBy + "&gpType=" + graphType + "&portalTabId=" + portalTabId + "&currDate=" + currDate + "&CBOARP" + viewByID + "=");
                            }
                        }
//end
                        ArrayList grpDetails = GraphDisplay.getGraphHeadersMapAtRT(null);

//                
                        if (!grpDetails.isEmpty() && grpDetails != null) {
                            path = grpDetails.get(0).toString().split(";")[0];//grpDetails[0].split(";");
                            graphTitle = grpDetails.get(1).toString().split(";")[0];
                            pcharts = (ProgenChartDisplay[]) grpDetails.get(2);
                            String paths[] = new String[1];
                            paths[0] = path;//grpDetails[0].split(";");

                            grpsImagesArray.add(pcharts[0].chartDisplay);
                            grpsTitlesArray.add(graphTitle);
                            grpsArray.add(grpsImagesArray);
                            grpsArray.add(grpsTitlesArray);
                        }
                    }
                }
            }
            if (!"ONEVIEW".equals(oneViewCheck)) {
                // header div starts
                tableBuffer.append("<div class=\"portlet-header portletHeader ui-corner-all\">");
                tableBuffer.append("<table style='height:10px;width:100%' >");//outer table1
                tableBuffer.append("<tr valign='top' align='right'>");
                tableBuffer.append("<td align=\"left\" style='color:#369;font-weight:bold;width:45%'>");
                tableBuffer.append("<a href='javascript:void(0)' onclick=\"editPortletName('" + PortletId + "','" + portalTabId + "','" + PortletName + "','" + currDate + "','" + callFrom + "')\"  style='text-decoration:none' class=\"calcTitle\" title=\"Edit Name\"><font size=\"1px\"><span id='" + PortletId + "_span'><b>" + PortletName + "</b> </span> </font></a>");
                //tableBuffer.append(PortletName);
                //tableBuffer.append(String.valueOf(metaInfo.get("PortletName")));
                tableBuffer.append("</td>");

                tableBuffer.append("<td align=\"left\" width=\"55%\"> ");
                //links code
                tableBuffer.append("<table border=0>");
                tableBuffer.append("<tr><td>&nbsp;&nbsp;</td>");
                tableBuffer.append("<td align=\"left\"> ");
                tableBuffer.append("<table border='0' align='left'>");
                tableBuffer.append("<tr>");
                tableBuffer.append("<td>");
                tableBuffer.append("<a href='javascript:void(0)' valign='top' class=\"ui-icon ui-icon-pencil\"  onclick=\"showGraphColumns('" + PortletId + "','" + portalTabId + "','" + portLet.getFolderId() + "','" + measureEId + "','" + measureEname + "')\"  style='text-decoration:none' title=\"Show Measures\"><font size=\"1px\"><b></b> </font></a>&nbsp;&nbsp;");
                tableBuffer.append("</td>");
                tableBuffer.append("<td>");
                tableBuffer.append("<a href='javascript:void(0)' valign='top' class=\"ui-icon ui-icon-clock\"  onclick=\"showTimeInfo('" + PortletId + "','" + portalTabId + "')\"  style='text-decoration:none' title=\"Time Info\"><font size=\"1px\"><b></b> </font></a>&nbsp;&nbsp;");
                tableBuffer.append("<div style='display:none;' id='timeInfo-" + PortletId + "-" + portalTabId + "'>");
                String conntype = "";
                if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                    conntype = "sqlserver";
                }
                StringBuilder sb = new StringBuilder();
                tableBuffer.append("");

                String vals = portLet.getTimeInfoDetails().get("PR_DAY_DENOM").toString();
                vals = vals.replace("[", "");
                vals = vals.replace("]", "");
                portLet.getPortletXMLHelper().getTimeDetailsArray().get(1).toString();
                String[] vals1 = vals.split(",");
                DateFormat formatter;
                Date date12;
//        container.getTimememdetails().get("PR_DAY_DENOM");
                Date date = new Date();
                String[] dates1 = new String[10];
                String[] dates = new String[10];
                Calendar ca1123 = Calendar.getInstance();
                java.util.Date d1 = new java.util.Date(ca1123.getTimeInMillis());
                if (vals1[1].contains("/")) {
                    dates = vals1[1].split("/");
                }
                if (vals1[1].contains("-")) {
                    dates1 = vals1[1].split(" ");
                    String values = dates1[1];
                    String[] repdates = values.split("-");
                    dates[2] = repdates[0];
                    dates[0] = repdates[1];
                    dates[1] = repdates[2];
                }
                //  dates[0];//month
                // dates[1];//day
                // dates[2];//year
                Calendar ca1 = Calendar.getInstance();
                String fullName = null;
                String[] datevals1 = null;
                // set(year, month, date) month 0-11
                if (dates[0] != null) {
                    ca1.set(Integer.parseInt(dates[2].substring(0, 4)), Integer.parseInt(dates[0].replace(" ", "")) - 1, Integer.parseInt(dates[1]));
                }
                java.util.Date d = new java.util.Date(ca1.getTimeInMillis());
                String partialName = new SimpleDateFormat("MMM").format(d);
                if (dates[2] != null) {
                    fullName = partialName + "'" + dates[2].substring(2, 4);
                }
                String nameToDisplay = portLet.getPortletXMLHelper().getTimeDetailsArray().get(3).toString();
                Calendar ca112 = Calendar.getInstance();
                DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
                ca112.add(Calendar.DATE, -1);
                String repUpdatedate = dateFormat.format(ca112.getTime());
                if (portLet.getPortletXMLHelper().getTimeDetailsArray().get(1).toString().equalsIgnoreCase("PRG_DATE_RANGE")) {
                    nameToDisplay = "Custom";
                }
                if (portLet.getPortletXMLHelper().getTimeDetailsArray().get(3).toString().equalsIgnoreCase("Month")) {
                    nameToDisplay = "Month";
//          partialName= new SimpleDateFormat("MMM").format(d);
                    if (dates[2] != null) {
                        fullName = partialName + "'" + dates[2].substring(2, 4);
                    }
                } else if (portLet.getPortletXMLHelper().getTimeDetailsArray().get(3).toString().equalsIgnoreCase("Qtr")) {
                    int month = Integer.parseInt(new SimpleDateFormat("M").format(d));
                    nameToDisplay = "Quarter";
                    if (month >= 1 && month <= 3) {
                        partialName = "Q4";
                    } else if (month >= 4 && month <= 6) {
                        partialName = "Q1";
                    } else if (month >= 7 && month <= 9) {
                        partialName = "Q2";
                    } else if (month >= 10 && month <= 12) {
                        partialName = "Q3";
                    }
                    if (dates[2] != null) {
                        fullName = partialName + "-" + dates[2].substring(0, 4);
                    }
                } else {
                }
                tableBuffer.append("<table><tr><td>");
                tableBuffer.append("<table><tr style=\"width:100px\"><td align=\"left\" styel=\"width:85px\">Duration</td><td>:</td><td align=\"left\" style=\"color:red ;width:15px\">" + nameToDisplay + "</td></tr>");
                if (!portLet.getPortletXMLHelper().getTimeDetailsArray().get(1).toString().equalsIgnoreCase("PRG_DATE_RANGE")) {
                    tableBuffer.append("<tr><td align=\"left\" style=\"color:red ;width:85px\">" + nameToDisplay + "</td><td>:</td><td align=\"left\" style=\"widht:15px\">" + fullName + "</td></tr>");
                }
                if (vals1[0] != null) {
                    if (!(vals1[0].equalsIgnoreCase(""))) {
                        tableBuffer.append("<tr style=\"width:100px\"><td align=\"left\" styel=\"width:85px\">From Date </td><td>:</td><td align=\"left\" style=\"widht:15px\">" + vals1[0].substring(0, 10) + "</td></tr>");
                    }
                }
                if (vals1[1] != null) {
                    if (!(vals1[0].equalsIgnoreCase(""))) {
                        tableBuffer.append("<tr style=\"width:100px\" ><td align=\"left\" styel=\"width:85px\">To Date </td><td>:</td><td align=\"left\" style=\"widht:15px\">" + vals1[1].substring(0, 11) + "</td></tr>");
                    }
                }
                String[] datevals;
                if (vals1[1] != null) {
                    if (!(vals1[1].equalsIgnoreCase(" "))) {
                        datevals1 = vals1[1].split(" ");
                        datevals = new String[10];
                        int mymonth = 0;
                        int decval = 0;
                        if (datevals1[1].contains("/")) {
                            datevals = datevals1[1].split("/");
                            mymonth = Integer.parseInt(datevals[0]);
                            decval = mymonth - 1;
                        } else {
                            String[] myvals = datevals1[1].split("-");

                            datevals[2] = myvals[0];
                            datevals[1] = myvals[2];
                            mymonth = Integer.parseInt(datevals[1]);
                            decval = mymonth - 1;
                        }

                        Calendar ca11 = Calendar.getInstance();
                        ca11.set(Integer.parseInt(datevals[2].substring(0, 4)), decval, Integer.parseInt(datevals[1]));
//        java.util.Date d1 = new java.util.Date(ca11.getTimeInMillis());
                        d = new java.util.Date(ca11.getTimeInMillis());
                        partialName = new SimpleDateFormat("MMM").format(d);
                        fullName = partialName + "'" + datevals[2].substring(2, 4);

                        if (portLet.getPortletXMLHelper().getTimeDetailsArray().get(3).toString().equalsIgnoreCase("Month")) {
                            fullName = partialName + "'" + datevals[2].substring(2, 4);
                        } else if (portLet.getPortletXMLHelper().getTimeDetailsArray().get(3).toString().equalsIgnoreCase("Qtr")) {
                            int month = Integer.parseInt(new SimpleDateFormat("M").format(d));
                            nameToDisplay = "Quarter";
                            if (month >= 1 && month <= 3) {
                                partialName = "Q4";
                            } else if (month >= 4 && month <= 6) {
                                partialName = "Q1";
                            } else if (month >= 7 && month <= 9) {
                                partialName = "Q2";
                            } else if (month >= 10 && month <= 12) {
                                partialName = "Q3";
                            }
                            fullName = partialName + "-" + datevals[2].substring(0, 4);
                        } else {
                        }
                    }
                }
                if (portLet.getPortletXMLHelper().getTimeDetailsArray().get(1).toString().equalsIgnoreCase("PRG_DATE_RANGE")) {
                    tableBuffer.append("<tr style=\"width:100px\"><td align=\"left\" styel=\"width:85px\">Compare Dates</td>");
                } else {
                    tableBuffer.append("<tr style=\"width:100px\"><td  align=\"left\" styel=\"width:85px\">Compare</td><td>:</td><td align=\"left\" style=\"widht:15px\">" + fullName + "</td></tr>");
                }
                if (!vals1[2].equalsIgnoreCase(" null")) {
                    if (vals1[2] != null) {
                        if (!(vals1[2].equalsIgnoreCase(" "))) {
                            tableBuffer.append("<tr style=\"width:100px\"><td align=\"left\" styel=\"width:85px\">From Date </td><td>:</td><td align=\"left\" style=\"widht:15px\">" + vals1[2].substring(0, 11) + "</td></tr>");
                        }
                    }
                }
                if (!vals1[3].equalsIgnoreCase(" null")) {
                    if (vals1[3] != null) {
                        if (!(vals1[3].equalsIgnoreCase(" "))) {
                            tableBuffer.append("<tr style=\"width:100px\"><td align=\"left\" styel=\"width:85px\">To Date </td><td>:</td><td align=\"left\" style=\"widht:15px\">" + vals1[3].substring(0, 11) + "</td></tr>");
                        }
                    }
                }
                tableBuffer.append("<tr style=\"width:100px\" ><td align=\"left\" styel=\"width:85px\">Updated On</td><td>:</td><td align=\"left\" style=\"widht:15px\">" + repUpdatedate + "</td></tr>");
                tableBuffer.append("</table>");
                tableBuffer.append("</td></tr></table></div>");
                tableBuffer.append("</div>");
                tableBuffer.append("</td>");
                tableBuffer.append("<td>");
                tableBuffer.append("<a href='javascript:void(0)' valign='top' class=\"ui-icon ui-icon-zoomin\" onclick=\"zoomer('zoom" + portalTabId + "-" + PortletId + "','" + portLet.getPortLetName() + "')\"  style='text-decoration:none' class=\"calcTitle\" title=\"Zoom\"><font size=\"1px\"><b></b> </font></a>&nbsp;&nbsp;");
                tableBuffer.append("</td>");
                tableBuffer.append("<td>");
                tableBuffer.append("<a href='javascript:void(0)' class=\"ui-icon ui-icon-arrowthick-2-n-s\" onclick=\"DrillToReport('" + PortletId + "','" + portalTabId + "')\"  style='text-decoration:none' class=\"calcTitle\" title=\"Drill\"><font size=\"1px\"><b></b> </font></a>&nbsp;&nbsp;");
                tableBuffer.append("</td>");
                tableBuffer.append("<td>");
                tableBuffer.append("<a  href='javascript:void(0)'class=\"ui-icon ui-icon-copy\"  onclick=\"DesignrelatedPortlets('" + PortletId + "','" + portalTabId + "')\"style='text-decoration:none' class=\"calcTitle\" title=\"Define Related Portlets\"></a>&nbsp;&nbsp;");
                tableBuffer.append("<div style='display:none;width:150px;height:auto;background-color:#ffffff;overflow:auto;position:absolute;text-align:left;border:1px solid #000000;border-top-width:0px;' id='RELATEDPORTLET-" + PortletId + "-" + portalTabId + "'>");
//             tableBuffer.append("<table>");
//             tableBuffer.append("<tr>");
//             tableBuffer.append("<td>");
//             tableBuffer.append("<a href='javascript:void(0)'  onclick=\"DesignrelatedPortlets('" + PortletId + "','" + portalTabId + "')\"  style='text-decoration:none' class=\"calcTitle\" title=\"Define related Portlets\"><font size=\"1px\"><b>DefineRelatedPortlets</b> </font></a>");
//             tableBuffer.append("</td>");
//             tableBuffer.append("</tr>");
//             tableBuffer.append("</table>");
                tableBuffer.append("</div>");
                tableBuffer.append("</td>");
                tableBuffer.append("<td>");
                tableBuffer.append("<a href='javascript:void(0)' valign='top' class=\"ui-icon ui-icon-extlink\"  onclick=\"ShowrelatedPortlets('" + PortletId + "','" + portalTabId + "')\"  style='text-decoration:none' title=\"Show related Portlets\"><font size=\"1px\"><b></b> </font></a>&nbsp;&nbsp;");
                tableBuffer.append("</td>");
                tableBuffer.append("<td>");
                tableBuffer.append("<a href='javascript:void(0)' class=\"ui-icon ui-icon-gear\" style='text-decoration:none'  title=\"Click For Options\" class=\"calcTitle\" onClick=\"getPortletOptions('" + PortletId + "','" + portalTabId + "')\"><font size=\"1px\"><b>   </b></font></a>&nbsp;&nbsp;");
                tableBuffer.append("<div style='display:none;width:150px;height:auto;background-color:#ffffff;overflow:auto;position:absolute;text-align:left;border:1px solid #000000;border-top-width:0px;' id='PortletOptions-" + PortletId + "-" + portalTabId + "'>");
                tableBuffer.append("<table border='0' align='left' valign='bottom' width=30px >");
                tableBuffer.append("<tr><td>");
                tableBuffer.append("<table><tr><td>");
                tableBuffer.append("<a href='javascript:void(0)' class=\"ui-icon ui-icon-clock\" style='text-decoration:none;' class=\"calcTitle\" title=\"Time\" onClick=\"timePortlet('" + PortletId + "','" + portalTabId + "',document.getElementsByName('chkREP-" + PortletId + "-" + portalTabId + "'),document.getElementById('REP" + PortletId + "-" + portalTabId + "'),'" + perBy + "','" + graphType + "','" + currDate + "')\"><font size=\"1px\"><b></b> </font></a></td><td><a href='javascript:void(0)'style='text-decoration:none;' class=\"calcTitle\" title=\"Time\" onClick=\"timePortlet('" + PortletId + "','" + portalTabId + "',document.getElementsByName('chkREP-" + PortletId + "-" + portalTabId + "'),document.getElementById('REP" + PortletId + "-" + portalTabId + "'),'" + perBy + "','" + graphType + "','" + currDate + "')\"><font size=\"1px\"><b>Time</b> </font></a></td></tr></table>");
//            tableBuffer.append("<div style='display:none;width:auto;height:100px;background-color:#ffffff;overflow:auto;position:absolute;text-align:left;border:1px solid #000000;border-top-width:1px;' id=\"time-"+PortletId+"-"+portalTabId+"\" >");
//            if(portLet.getPortLetTimeHelper()!=null)
//            {
//                PortLetTimeHelper portLetTimeHelper=portLet.getPortLetTimeHelper();
//                String time=portLetTimeHelper.getPortletTime();//in GUI Date is taken as time variable in coding
//                String period=portLetTimeHelper.getPortletPeriod();
//            tableBuffer.append("<table><tr><td>Date</td><td><input type='text' name='timeOption"+PortletId+"-"+portalTabId+"' style=\"width:60px;\"  id=\"timeOption"+PortletId+"-"+portalTabId+"\" value="+time+"></td></tr><tr><td>PeriodType</td><td><select id='periodType"+PortletId+"-"+portalTabId+"' name='periodType"+PortletId+"-"+portalTabId+"' title=\"PeriodType\"><option value="+period+" selected>"+period+"</option><option value='Year'>Year</option><option value='Qtr'>Qtr</option><option value='Month'>Month</option><option value='Day'>Day</option></select></td></tr><tr><td>");
//            tableBuffer.append("<select class=\"myTextbox3\" name=\"CBO_PRG_COMPARE\" id=\"CBO_PRG_COMPARE\"> <option value=\"Last Period\" selected=\"\"> Last Period </option><option value=\"Last Year\"> Last Year </option><option value=\"Period Complete\"> Period Complete </option><option value=\"Year Complete\"> Year Complete</option></select></td></tr>");
//            }else
//            {
//             tableBuffer.append("<table><tr><td>Date</td><td><input type='text' name='timeOption"+PortletId+"-"+portalTabId+"' style=\"width:60px;\"  id=\"timeOption"+PortletId+"-"+portalTabId+"\"></td></tr><tr><td>PeriodType</td><td><select id='periodType"+PortletId+"-"+portalTabId+"' name='periodType"+PortletId+"-"+portalTabId+"' title=\"PeriodType\"><option value='Year'>Year</option><option value='Qtr'>Qtr</option><option value='Month'>Month</option><option value='Day'>Day</option></select></td></tr><tr><td></td><td>");
//             tableBuffer.append("<select class=\"myTextbox3\" name=\"CBO_PRG_COMPARE\" id=\"CBO_PRG_COMPARE\"> <option value=\"Last Period\" selected=\"\"> Last Period </option><option value=\"Last Year\"> Last Year </option><option value=\"Period Complete\"> Period Complete </option><option value=\"Year Complete\"> Year Complete</option></select></td></tr>");
//            }
//             tableBuffer.append("<tr><td><input type=button class=\"navtitle-hover\" name=done id=\"done"+PortletId+"-"+portalTabId+"\" value=\"Done\" onClick=\"getTimePeriodDetails('" + PortletId + "',document.getElementsByName('chkREP-" + PortletId + "-" + portalTabId + "'),document.getElementById('REP" + PortletId + "-" + portalTabId + "'),'" + perBy + "','" + graphType + "','" + portalTabId + "','" + currDate + "')\"/></td><td><input type=button class=\"navtitle-hover\" name=Reset id=\"Reset"+PortletId+"-"+portalTabId+"\" value=\"Reset\" onClick=\"resetTimePeriodDetails('" + PortletId + "',document.getElementsByName('chkREP-" + PortletId + "-" + portalTabId + "'),document.getElementById('REP" + PortletId + "-" + portalTabId + "'),'" + perBy + "','" + graphType + "','" + portalTabId + "','" + currDate + "')\"/></td></tr></table>");
//            tableBuffer.append("</div>");
                tableBuffer.append("</td></tr>");

                tableBuffer.append("<tr><td>");
                tableBuffer.append("<table><tr><td>");
                tableBuffer.append("<a href='javascript:void(0)'  class=\"ui-icon ui-icon-disk\" onclick=\"saveXmalOfPortlet('" + PortletId + "',document.getElementsByName('chkREP-" + PortletId + "-" + portalTabId + "'),document.getElementById('REP" + PortletId + "-" + portalTabId + "'),'" + perBy + "','" + graphType + "','" + portalTabId + "','" + currDate + "','" + callFrom + "')\"  style='text-decoration:none' class=\"calcTitle\" title=\"Save Portlet\"><font size=\"1px\"><b></b> </font></a></td><td><a href='javascript:void(0)' onclick=\"saveXmalOfPortlet('" + PortletId + "',document.getElementsByName('chkREP-" + PortletId + "-" + portalTabId + "'),document.getElementById('REP" + PortletId + "-" + portalTabId + "'),'" + perBy + "','" + graphType + "','" + portalTabId + "','" + currDate + "','" + callFrom + "')\"  style='text-decoration:none' class=\"calcTitle\" title=\"Save Portlet\"><font size=\"1px\"><b>Save </b> </font></a></td></tr></table>");
                tableBuffer.append("<div style='display:none;width:110px;height:50px;background-color:#ffffff;overflow:auto;position:absolute;text-align:left;border:1px solid #000000;border-top-width: 1px;' id='saveXmalOfPortlet" + PortletId + "-" + portalTabId + "'>");
                tableBuffer.append("<table><tr><td>Over Write</td><td><input type='radio' name='saveRediooption" + PortletId + "-" + portalTabId + "' id='overwrite" + PortletId + "-" + portalTabId + "' value='overwrite' ></td></tr><tr><td>Save As New </td><td><input type='radio' name='saveRediooption" + PortletId + "-" + portalTabId + "' id='saveAsNew" + PortletId + "-" + portalTabId + "' value='saveAsNew' checked></td></tr></table>");
                tableBuffer.append("</div>");
                tableBuffer.append("</td></tr>");
                tableBuffer.append("<tr><td>");
                tableBuffer.append("<table><tr><td>");
                tableBuffer.append("<a href='javascript:void(0)' class=\"ui-icon ui-icon-pencil\" onclick=\"openGraphProperty(" + PortletId + "," + portalTabId + ",'" + graphType + "')\"  style='text-decoration:none' class=\"calcTitle\" title=\"Graph Property\"><font size=\"1px\"><b></b> </font></a> </td><td><a href='javascript:void(0)'  onclick=\"openGraphProperty(" + PortletId + "," + portalTabId + ",'" + graphType + "')\"  style='text-decoration:none' class=\"calcTitle\" title=\"Graph Property\"><font size=\"1px\"><b>GraphProperty</b> </font></a></td></tr></table>");
                tableBuffer.append("</td></tr>");
                if (!portalTabId.equalsIgnoreCase("-1")) {
                    tableBuffer.append("<tr><td>");
                    tableBuffer.append("<table><tr><td>");
                    tableBuffer.append("<a href='javascript:void(0)' class=\"ui-icon ui-icon-scissors\" onclick=\"openFilter(" + PortletId + "," + portalTabId + ")\"  style='text-decoration:none' class=\"calcTitle\" title=\"Filter\"><font size=\"1px\"><b></b> </font></a> </td><td><a href='javascript:void(0)'  onclick=\"openFilter(" + PortletId + "," + portalTabId + ")\"  style='text-decoration:none' class=\"calcTitle\" title=\"Filter\"><font size=\"1px\"><b>Filter</b> </font></a></td></tr></table>");
                    tableBuffer.append("</td></tr>");
                }
//            tableBuffer.append("<tr><td>");
//            tableBuffer.append("<table><tr><td>");
//            tableBuffer.append("<a href='javascript:void(0)' class=\"ui-icon ui-icon-zoomin\" onclick=\"zoomer('zoom"+portalTabId+"-"+PortletId+"','" + portLet.getPortLetName() + "')\"  style='text-decoration:none' class=\"calcTitle\" title=\"Zoom\"><font size=\"1px\"><b></b> </font></a> </td><td><a href='javascript:void(0)'  onclick=\"zoomer('zoom"+portalTabId+"-"+PortletId+"','" + portLet.getPortLetName() + "')\"  style='text-decoration:none' class=\"calcTitle\" title=\"Zoom\"><font size=\"1px\"><b>Zoom</b> </font></a></td></tr></table>");
//            tableBuffer.append("</td></tr>");
//            tableBuffer.append("<tr><td>");
//            tableBuffer.append("<table><tr><td>");
//            tableBuffer.append("<a href='javascript:void(0)' class=\"ui-icon ui-icon-arrowthick-2-n-s\" onclick=\"DrillToReport('" + PortletId + "','" + portalTabId + "')\"  style='text-decoration:none' class=\"calcTitle\" title=\"Drill\"><font size=\"1px\"><b></b> </font></a> </td><td><a href='javascript:void(0)'  onclick=\"DrillToReport('" + PortletId + "','" + portalTabId + "')\"  style='text-decoration:none' class=\"calcTitle\" title=\"Drill\"><font size=\"1px\"><b>Drill</b> </font></a></td></tr></table>");
//            tableBuffer.append("</td></tr>");
                // 
                //  tableBuffer.append("<a  style='text-decoration:none' href=\"javascript:deletePortlet('" + PortletId + "','" + String.valueOf(metaInfo.get("PortletName")) + "','" + portalTabId + "')\" title='Delete '" + String.valueOf(metaInfo.get("PortletName")) + "' ><font size=\"1px\"><b>Delete</b></font></a>");
                tableBuffer.append("<tr><td>");
                tableBuffer.append("<table><tr><td><a class=\"ui-icon ui-icon-trash\"  style='text-decoration:none' href=\"javascript:deletePortlet('" + PortletId + "','" + String.valueOf(metaInfo.get("PortletName")) + "','" + portalTabId + "')\" title='Delete '" + String.valueOf(metaInfo.get("PortletName")) + "' ></a></td><td><a style='text-decoration:none' href=\"javascript:deletePortlet('" + PortletId + "','" + String.valueOf(metaInfo.get("PortletName")) + "','" + portalTabId + "')\" title='Delete '" + String.valueOf(metaInfo.get("PortletName")) + "' >Delete</a></td></tr></table>");
                tableBuffer.append(" </td></tr> ");
                tableBuffer.append("</table></div></td>");
                //graph Type start
                //  tableBuffer.append("<a href='javascript:void(0)' class=\"ui-icon ui-icon-scissors\" onclick=\"openFilter("+PortletId+","+portalTabId+")\"  style='text-decoration:none' class=\"calcTitle\" title=\"Filter\"><font size=\"1px\"><b></b> </font></a> &nbsp;&nbsp;");
                tableBuffer.append("<td>");
                tableBuffer.append("<a href='javascript:void(0)' class=\"ui-icon ui-icon-image\" onclick=\"openGrpTypeDisplay(document.getElementById('GrpType" + PortletId + "-" + portalTabId + "'))\"  style='text-decoration:none' class=\"calcTitle\" title=\"Graph Types\"><font size=\"1px\"><b>Graph Types</b> </font></a> &nbsp;&nbsp;");
                tableBuffer.append("<div style='display:none;width:auto;height:200px;background-color:#ffffff;overflow:auto;position:absolute;text-align:left;border:1px solid #000000;border-top-width: 0px;' id='GrpType" + PortletId + "-" + portalTabId + "'>");
                tableBuffer.append("<Table>");
                for (int i = 0; i < graphTypesList.length; i++) {
                    tableBuffer.append("<Tr valign='top'>");
                    tableBuffer.append("<Td valign='top'>");
                    if (graphTypesList[i].equalsIgnoreCase(graphType)) {
                        tableBuffer.append("<b>" + graphTypesList[i] + "</b>");
                    } else {
                        tableBuffer.append("<a href='javascript:void(0)' onclick=\"openGrpTypePreviews('" + PortletId + "',document.getElementsByName('chkREP-" + PortletId + "-" + portalTabId + "'),'" + perBy + "','" + graphTypesList[i] + "','" + portalTabId + "','" + currDate + "')\">" + graphTypesList[i] + "</a>");
                    }
                    tableBuffer.append("</Td>");
                    tableBuffer.append("</Tr>");
                }
                tableBuffer.append("</Table>");//closing of inner table
                tableBuffer.append(" </div>");
                tableBuffer.append(" </td>");
                //graph type end
                //sort start
                tableBuffer.append("<td>");
//            tableBuffer.append("<a href='javascript:void(0)' class=\"ui-icon ui-icon-triangle-2-n-s\"  onclick=\"opentopbottomDisplay(document.getElementById('TopBottom" + PortletId + "'))\"  style='text-decoration:none' class=\"calcTitle\" title=\"Sort\"><font size=\"1px\"><b></b></font></a>&nbsp;&nbsp;");
                tableBuffer.append("<a href='javascript:void(0)' onclick=\"opentopbottomPreviews('" + PortletId + "',document.getElementsByName('chkREP-" + PortletId + "-" + portalTabId + "'),document.getElementById('REP" + PortletId + "-" + portalTabId + "'),'" + perBy + "','" + graphType + "','" + portalTabId + "','" + currDate + "','" + callFrom + "')\" style='text-decoration:none'class=\"ui-icon ui-icon-triangle-2-n-s\" class=\"calcTitle\" title=\"sort\"><font size=\"1px\"> <b></b> </font></a>&nbsp;&nbsp;");
                tableBuffer.append("<div style='display:none;width:auto;height:auto;background-color:#ffffff;overflow:auto;position:absolute;text-align:left;border:1px solid #000000;border-top-width: 0px;' id='TopBottom" + PortletId + "-" + portalTabId + "'>");
//            tableBuffer.append("<Table>");
//            for (int i = 0; i < performers.length; i++) {
//                tableBuffer.append("<Tr valign='top'>");
//                tableBuffer.append("<Td valign='top'>");
//
//                if (perBy != null) {
//                    if (performers[i].equalsIgnoreCase(perBy)) {
//                        tableBuffer.append("<b>" + performers[i] + "</b>");
//                    } else {
//                        tableBuffer.append("<a href='javascript:void(0)' onclick=\"opentopbottomPreviews('" + PortletId + "',document.getElementsByName('chkREP-" + PortletId + "-" + portalTabId + "'),'" + performers[i] + "','" + graphType + "','" + portalTabId + "','" + currDate + "')\">" + performers[i] + "</a>");
//                    }
//                } else {
//                    if (performers[i].equalsIgnoreCase("Top-10")) {
//                        tableBuffer.append("<b>" + performers[i] + "</b>");
//                    } else {
//                        tableBuffer.append("<a href='javascript:void(0)' onclick=\"opentopbottomPreviews('" + PortletId + "',document.getElementsByName('chkREP-" + PortletId + "-" + portalTabId + "'),'" + performers[i] + "'," + graphType + "','" + portalTabId + "','" + currDate + "')\">" + performers[i] + "</a>");
//                    }
//                }
//                tableBuffer.append("</Td>");
//                tableBuffer.append("</Tr>");
//            }
//            tableBuffer.append("</Table>");//closing of inner table
                //modified by anitha
                tableBuffer.append("<table><tr><td>");
                tableBuffer.append("<table><tr>");
                tableBuffer.append("<td>");
                if (portletSorterHelper != null && portletSorterHelper.getSortType().equalsIgnoreCase("Top")) {
                    tableBuffer.append("<input type='radio' name='sortTable" + PortletId + "-" + portalTabId + "' value=\"Top\" id='sortTable" + PortletId + "-" + portalTabId + "'checked>Top");
                } else {
                    tableBuffer.append("<input type='radio' name='sortTable" + PortletId + "-" + portalTabId + "' value=\"Top\" id='sortTable" + PortletId + "-" + portalTabId + "'>Top");
                }
                tableBuffer.append("</td>");
                tableBuffer.append("<td>");
                if (portletSorterHelper != null && portletSorterHelper.getSortType().equalsIgnoreCase("Bottom")) {
                    tableBuffer.append("<input type='radio' name='sortTable" + PortletId + "-" + portalTabId + "' value=\"Bottom\" id='sortTable" + PortletId + "-" + portalTabId + "'checked>Bottom");
                } else {
                    tableBuffer.append("<input type='radio' name='sortTable" + PortletId + "-" + portalTabId + "' value=\"Bottom\" id='sortTable" + PortletId + "-" + portalTabId + "'>Bottom");
                }
                tableBuffer.append("</td>");
                tableBuffer.append("<td>");
                if (portletSorterHelper != null && portletSorterHelper.getIsSortAll()) {
                    tableBuffer.append("<input type='checkbox' id='sortCheckbox" + PortletId + "-" + portalTabId + "' onclick=\"onChangeCheckbox('" + PortletId + "','" + portalTabId + "')\" value='true'name='sortCheckbox" + PortletId + "-" + portalTabId + "' title='Display all rows' checked>ALL");
                } else {
                    tableBuffer.append("<input type='checkbox' id='sortCheckbox" + PortletId + "-" + portalTabId + "' onclick=\"onChangeCheckbox('" + PortletId + "','" + portalTabId + "')\" value='true'name='sortCheckbox" + PortletId + "-" + portalTabId + "' title='Display all rows'>ALL");
                }
                tableBuffer.append("</td>");
                tableBuffer.append("</tr></table>");
                tableBuffer.append("</td></tr></table>");
                tableBuffer.append("<table><tr>");
                tableBuffer.append("<td><select id='sortBy" + PortletId + "-" + portalTabId + "'>");
                if (TableHashMap != null) {
                    ArrayList MeasVal = (ArrayList) TableHashMap.get("Measures");
                    ArrayList MeasNameList = (ArrayList) TableHashMap.get("MeasuresNames");
                    StringBuilder tempViewbys = new StringBuilder("");
                    String tempStr = null;
                    for (int i = 0; i < reportParameters.values().toArray().length; i++) {
                        tempStr = reportParameters.values().toArray()[i].toString().split(",")[0].replace("[", "").trim();
                        if (REPList.toString().contains(tempStr) || REPList.toString().toUpperCase().contains(tempStr)) {

                            tempViewbys.append("," + reportParameters.values().toArray()[i].toString().split(",")[1]);
                            //if(seqCount>0 && )

                        }
                    }
                    if (!tempViewbys.toString().trim().equalsIgnoreCase("")) {
                        MeasVal.add(REPList.get(0).toString().replace(" ", ""));
                        MeasNameList.add(tempViewbys.toString().substring(1));
                    }
                    for (int i = 0; i < MeasVal.size(); i++) {
                        if (portletSorterHelper != null && portletSorterHelper.getSortByColumeVal() == MeasVal.get(i).toString()) {
                            tableBuffer.append("<option value=" + MeasVal.get(i) + " selected>" + MeasNameList.get(i) + "</option>");
                        } else {
                            tableBuffer.append("<option value=" + MeasVal.get(i) + ">" + MeasNameList.get(i) + "</option>");
                        }
                    }
                }

                tableBuffer.append("</select></td>");
                tableBuffer.append("<td>");
                if (portletSorterHelper != null && portletSorterHelper.getIsSortAll()) {
                    tableBuffer.append("<input type='input' style='width:60px;display: none' onkeypress=\"return isNumberKey(event)\"  id='sortTextbox" + PortletId + "-" + portalTabId + "' name='sortTextbox" + PortletId + "-" + portalTabId + "'/>");
                } else if (portletSorterHelper != null && !portletSorterHelper.getIsSortAll()) {
                    tableBuffer.append("<input type='input' style='width:60px' onkeypress=\"return isNumberKey(event)\"  id='sortTextbox" + PortletId + "-" + portalTabId + "' name='sortTextbox" + PortletId + "-" + portalTabId + "' value=" + portletSorterHelper.getCountVal() + ">");
                } else {
                    tableBuffer.append("<input type='input' style='width:60px' onkeypress=\"return isNumberKey(event)\"  id='sortTextbox" + PortletId + "-" + portalTabId + "' name='sortTextbox" + PortletId + "-" + portalTabId + "'/>");
                }
                tableBuffer.append("</td>");
                tableBuffer.append("</tr></table>");
                tableBuffer.append("</td></tr></table>");
                tableBuffer.append("</div>");
                tableBuffer.append("</td>");
                //sort end
                //Row viewby start
                tableBuffer.append("<td>");
                tableBuffer.append("<a href='javascript:void(0)'  class=\"ui-icon ui-icon-arrowthickstop-1-e\" onclick=\"openREPPreviews('" + PortletId + "',document.getElementsByName('chkREP-" + PortletId + "-" + portalTabId + "'),document.getElementsByName('chkCEP-" + PortletId + "-" + portalTabId + "'),document.getElementById('REP" + PortletId + "-" + portalTabId + "'),'" + perBy + "','" + graphType + "','" + portalTabId + "','" + currDate + "')\"  style='text-decoration:none' class=\"calcTitle\" title=\"Row View By\"><font size=\"1px\"><b> </b></font></a>&nbsp;&nbsp;");
                tableBuffer.append("<div style='display:none;width:auto;height:auto;background-color:#ffffff;overflow:auto;position:absolute;text-align:left;border:1px solid #000000;border-top-width: 0px;' id='REP" + PortletId + "-" + portalTabId + "'>");
                tableBuffer.append("<Table>");
            }

            String tempStr = "";
            for (int i = 0; i < reportParameters.values().toArray().length; i++) {
                if (!"ONEVIEW".equals(oneViewCheck)) {
                    tableBuffer.append("<Tr valign='top'>");
                    tableBuffer.append("<Td valign='top'>");
                }
                tempStr = reportParameters.values().toArray()[i].toString().split(",")[0].replace("[", "").trim();
                if (REPList.toString().contains(tempStr) || REPList.toString().toUpperCase().contains(tempStr)) {
                    if (!"ONEVIEW".equals(oneViewCheck)) {
                        tableBuffer.append("<input type=\"checkbox\" checked name=\"chkREP-" + PortletId + "-" + portalTabId + "\" id=\"chkREP-" + PortletId + "-" + portalTabId + "\" value='" + reportParameters.values().toArray()[i].toString().split(",")[0].replace("[", "") + "~" + reportParameters.values().toArray()[i].toString().split(",")[1].replace("[", "") + "'>");
                    }
                } else {
                    if (!"ONEVIEW".equals(oneViewCheck)) {
                        tableBuffer.append("<input type=\"checkbox\" name=\"chkREP-" + PortletId + "-" + portalTabId + "\" id=\"chkREP-" + PortletId + "-" + portalTabId + "\" value='" + reportParameters.values().toArray()[i].toString().split(",")[0].replace("[", "") + "~" + reportParameters.values().toArray()[i].toString().split(",")[1].replace("[", "") + "'>");
                    }
                }
                if (!"ONEVIEW".equals(oneViewCheck)) {
                    tableBuffer.append("<b><font size=\"1px\">" + reportParameters.values().toArray()[i].toString().split(",")[1] + "<font size=\"1px\"></b>");
                    tableBuffer.append("</Td>");
                    tableBuffer.append("</Tr>");
                }
            }
            if (!"ONEVIEW".equals(oneViewCheck)) {
                tableBuffer.append("</Table>");//closing of inner table
                tableBuffer.append("</div>");
                tableBuffer.append("</td>");

                //Row view By end
                //column viewBy start
                tableBuffer.append("<td>");
                tableBuffer.append("<a href='javascript:void(0)'  class=\"ui-icon ui-icon-arrowthickstop-1-s\" onclick=\"openCEPPreviews('" + PortletId + "',document.getElementsByName('chkREP-" + PortletId + "-" + portalTabId + "'),document.getElementsByName('chkCEP-" + PortletId + "-" + portalTabId + "'),document.getElementById('CEP" + PortletId + "-" + portalTabId + "'),'" + perBy + "','" + graphType + "','" + portalTabId + "','" + currDate + "')\"  style='text-decoration:none' class=\"calcTitle\" title=\"Column View By\"><font size=\"1px\"><b> </b></font></a>&nbsp;&nbsp;");
                tableBuffer.append("<div style='display:none;width:auto;height:auto;background-color:#ffffff;overflow:auto;position:absolute;text-align:left;border:1px solid #000000;border-top-width: 0px;' id='CEP" + PortletId + "-" + portalTabId + "'>");
                tableBuffer.append("<Table>");
                String tempValue = "";
                for (int i = 0; i < reportParameters.values().toArray().length; i++) {
                    tableBuffer.append("<Tr valign='top'>");
                    tableBuffer.append("<Td valign='top'>");
                    tempValue = reportParameters.values().toArray()[i].toString().split(",")[0].replace("[", "").trim();
                    if (CEPList.toString().contains(tempValue) || CEPList.toString().toUpperCase().contains(tempValue)) {
                        tableBuffer.append("<input type=\"checkbox\" checked name=\"chkCEP-" + PortletId + "-" + portalTabId + "\" id=\"chkCEP-" + PortletId + "-" + portalTabId + "\" value='" + reportParameters.values().toArray()[i].toString().split(",")[0].replace("[", "") + "~" + reportParameters.values().toArray()[i].toString().split(",")[1].replace("[", "") + "'>");
                    } else {
                        tableBuffer.append("<input type=\"checkbox\" name=\"chkCEP-" + PortletId + "-" + portalTabId + "\" id=\"chkCEP-" + PortletId + "-" + portalTabId + "\" value='" + reportParameters.values().toArray()[i].toString().split(",")[0].replace("[", "") + "~" + reportParameters.values().toArray()[i].toString().split(",")[1].replace("[", "") + "'>");
                    }
                    tableBuffer.append("<b><font size=\"1px\">" + reportParameters.values().toArray()[i].toString().split(",")[1] + "<font size=\"1px\"></b>");
                    tableBuffer.append("</Td>");
                    tableBuffer.append("</Tr>");
                }
                tableBuffer.append("</Table>");//closing of inner table
                tableBuffer.append("</div>");
                tableBuffer.append("</td>");
                //column viewBy end
                //start switch to Table
                if (graphType.equalsIgnoreCase("Table")) {
                    tableBuffer.append("<td align=\"left\" id=\"Portlet-" + PortletId + "\" valign=\"top\" width=\"5%\"><a class=\"ui-icon ui-icon-calculator\" href='javascript:void(0)' onclick=\"javascript:PortletGrpTable(" + PortletId + ",'" + reportRowViewbyValues + "','" + reportColViewbyValues + "','" + perBy + "','','" + portalTabId + "','" + currDate + "')\" style='text-decoration:none' class=\"calcTitle\" title=\"Switch to Graph\"></a>");
                } else {
                    tableBuffer.append("<td align=\"left\" id=\"Portlet-" + PortletId + "\" valign=\"top\" width=\"5%\"><a class=\"ui-icon ui-icon-calculator\" href='javascript:void(0)' onclick=\"javascript:PortletGrpTable(" + PortletId + ",'" + reportRowViewbyValues + "','" + reportColViewbyValues + "','" + perBy + "','Table','" + portalTabId + "','" + currDate + "')\" style='text-decoration:none' class=\"calcTitle\" title=\"Switch to Table\"></a>");
                }
                tableBuffer.append("</td>");
                //end Switch To Table

//             //start zoomer Graph
//             tableBuffer.append("<td align=\"right\"><a style='text-decoration:none' class=\"calcTitle\" onclick=\"zoomer('zoom"+portalTabId+"-"+PortletId+"','" + portLet.getPortLetName() + "')\" href=\"javascript:void(0)\" ><span  title=\"ZOOM\"  class=\"ui-icon ui-icon-zoomin\"></span></a> ");
//             tableBuffer.append("</td>");
//            //end zoomer Graph
//
//             tableBuffer.append("<td>");
//             tableBuffer.append("<a class=\"ui-icon ui-icon-arrowthick-2-n-s\" href='javascript:void(0)' onclick=\"DrillToReport('" + PortletId + "','" + portalTabId + "')\"  style='text-decoration:none' class=\"calcTitle\" title=\"Drill To Report\"></a>");
//             tableBuffer.append("</td>");
                tableBuffer.append("<td>");
                tableBuffer.append("<a class=\"ui-icon ui-icon-refresh\" style='text-decoration:none' href=\"javascript:resetPortlet('" + PortletId + "','" + Joiner.on(",").join(reportRowViewbyValues) + "','" + reportColViewbyValues + "','" + perBy + "','" + graphType + "','" + portalTabId + "','" + currDate + "','" + sortColumeVal + "')\" title='Reset '" + String.valueOf(metaInfo.get("PortletName")) + "' ><font size=\"1px\"></font></a>&nbsp;&nbsp;");
                tableBuffer.append("</td></tr>");
                tableBuffer.append("</table>");
                tableBuffer.append("</Td>");
                tableBuffer.append("</tr>");
                tableBuffer.append("</table>");
                tableBuffer.append("</Td> </Tr></Table>");
                tableBuffer.append(" </div>");
                //header div ends
            }

            //content div starts
            if (!"ONEVIEW".equals(oneViewCheck)) {
                tableBuffer.append("<div id='zoom" + portalTabId + "-" + PortletId + "' style=\"width:100%;height:350px;overflow-y:auto;overflow-x:auto\">");
            }
            // tableBuffer.append("<div style=\"width:415px;height:350px;overflow-y:auto;overflow-x:auto\">");
//            tableBuffer.append("<table width=\"100%\">");
//            tableBuffer.append(" <tr valign='top'> ");
//            tableBuffer.append("<td valign='top' style=\"color:#369;font-weight:bold\">");

            if (pbretObj != null) {
                if (pbretObj.getRowCount() > 0) {
                    if (isIfFxCharts()) {
                        //need to introduce iframe added by santhosh.kumar@progenbusiness.com
                        tableBuffer.append("<iframe id='iframe-" + PortletId + "' frameborder='0' style='width:400px;height:295px;overflow:auto' src='PbPortletFXGraphDisp.jsp?PORTLETID=" + PortletId + "&REP=" + REP + "&CEP=" + CEP + "&perBy=" + perBy + "&graphType=" + graphType + "&PortalTabId=" + portalTabId + "'></iframe>");
                        //end of code for iframe
                    } else {
                        if (pcharts != null) {
                            if (pcharts[0].chartDisplay != null || !pcharts[0].chartDisplay.equalsIgnoreCase("")) {
                                this.portLet.setImgName(pcharts[0].mapname);
                                tableBuffer.append(pcharts[0].chartDisplay);
                                if (!"ONEVIEW".equals(oneViewCheck)) {
                                    tableBuffer.append("<br/><br/><table align='right'><tr><td><img src='" + request.getContextPath() + "/icons pinvoke/arrow-curve-270.png' alt='' /></td><td><b><font size=\"1px\" color='black'>");
                                }
                                //int seqCount=0;
                                StringBuilder tempViewbys = new StringBuilder("");
                                // String tempStr;
                                for (int i = 0; i < reportParameters.values().toArray().length; i++) {
                                    tempStr = reportParameters.values().toArray()[i].toString().split(",")[0].replace("[", "").trim();
                                    if (REPList.toString().contains(tempStr) || REPList.toString().toUpperCase().contains(tempStr)) {

                                        tempViewbys.append("," + reportParameters.values().toArray()[i].toString().split(",")[1]);
                                        //if(seqCount>0 && )

                                    }
                                }
                                if (!tempViewbys.toString().trim().equalsIgnoreCase("")) {
                                    if (!"ONEVIEW".equals(oneViewCheck)) {
                                        tableBuffer.append(tempViewbys.toString().substring(1) + "</font></b></td></tr></table>");
                                    }
                                }
                                //portletPreviewSB.append(pcharts[0].chartDisplay);
                            }
                        } else {
                            if (!"".equalsIgnoreCase(tableString)) {
                                tableBuffer.append(tableString);
                            } else {
                                tableBuffer.append("No Data to Display");
                            }
                        }
                    }
                } else {
                    tableBuffer.append("No Data to Display");
                }
            } else {
                if (!"".equalsIgnoreCase(tableString)) {
                    tableBuffer.append(tableString);
                }
            }
//            tableBuffer.append(" </td>");
//            tableBuffer.append(" <td align=\"right\"> </td>");
//            tableBuffer.append("</tr>");
//            tableBuffer.append("</table>");
            tableBuffer.append("</div>");

        } //code to display KPI Graph
        else if (displayType.equalsIgnoreCase("KPI Graph")) {
            ProgenChartDisplay pchart = new ProgenChartDisplay(400, 290);
            String kpigraphType = "";
            String oneViewCheck = "";
            String height = "";
            String width = "";
            oneViewCheck = (String) session.getAttribute("ONEVIEW");
            height = (String) session.getAttribute("height");
            width = (String) session.getAttribute("width");
            if (oneViewCheck != null && !oneViewCheck.equalsIgnoreCase("") && "ONEVIEW".equals(oneViewCheck)) {
                if (height != null && !height.equalsIgnoreCase("") && width != null && !width.equalsIgnoreCase("")) {
                    pchart = new ProgenChartDisplay(Integer.parseInt(width), Integer.parseInt(height));
                }
            }
            PrintWriter out = response.getWriter();
            String[] KPIGraphTypes = {"Meter", "Thermometer"};
            ParametersHashMap = new HashMap();
            Container container = null;
            float deviation = 0.0f;
            String actualValue = null;
            String targetValue = null;
            double needleValue = 0.0;
            ArrayList reportRowViewbyValues = null;
            ParametersHashMap.put("Parameters", dets);
            ParametersHashMap.put("TimeDetailstList", timeDetailsArray);
            ParametersHashMap.put("TimeDimHashMap", timeDetailsMap);
            ParametersHashMap.put("ParametersNames", detnames);
            if (TableHashMap != null && TableHashMap.size() != 0) {
                if (TableHashMap.get("REP") != null) {
                    reportRowViewbyValues = (ArrayList) TableHashMap.get("REP");
                }
            }
            PortletXMLHelper portletXMLHelper = null;
            if (getPortLet() != null) {
                portLet = getPortLet();
                portletXMLHelper = portLet.getPortletXMLHelper();
            }
            String measurename = null;
            ArrayList<String> elementids = portletXMLHelper.getQryElementIds();
            if (!elementids.isEmpty()) {
                String kpis = elementids.get(0);
                PortalViewerBD bd = new PortalViewerBD();
                PbReturnObject retObj = bd.getKpiretObj(kpis, ParametersHashMap, TableHashMap);
                String value1 = retObj.getFieldValueString(0, 1);
                actualValue = value1;
            }
            if (graphType != null && !"".equalsIgnoreCase(graphType)) {
                kpigraphType = graphType;
            } else {
                kpigraphType = String.valueOf(getGraphDeails().get("graphType"));
            }

            double startRange = Double.parseDouble(String.valueOf(getGraphDeails().get("startRange")));
            double endRange = Double.parseDouble(String.valueOf(getGraphDeails().get("endRange")));
            double firstBreak = Double.parseDouble(String.valueOf(getGraphDeails().get("firstBreak")));
            double secondBreak = Double.parseDouble(String.valueOf(getGraphDeails().get("secondBreak")));
            measurename = String.valueOf(getGraphDeails().get("measureName"));
            String dayTargetVal = "";
            if (portLet.getTargetVal() != null) {
                dayTargetVal = portLet.getTargetVal();
                getGraphDeails().put("daytargetVal", dayTargetVal);
            } else {
                dayTargetVal = String.valueOf(getGraphDeails().get("daytargetVal"));
            }
            if (!actualValue.equalsIgnoreCase("") && actualValue != null) {
                if (dayTargetVal != null && !dayTargetVal.equalsIgnoreCase("null")) {
                    if (timeDetailsArray.get(0).toString().equalsIgnoreCase("Day")) {
                        if (timeDetailsArray.get(1).toString().equalsIgnoreCase("PRG_DATE_RANGE")) {
                            targetValue = dayTargetVal;
                            deviation = getDeviation(actualValue, dayTargetVal);
                            needleValue = Double.parseDouble(String.valueOf(deviation));
                        } else if (timeDetailsArray.get(3).toString().equalsIgnoreCase("Day")) {
                            targetValue = dayTargetVal;
                            deviation = getDeviation(actualValue, dayTargetVal);
                            needleValue = Double.parseDouble(String.valueOf(deviation));
                        } else if (timeDetailsArray.get(3).toString().equalsIgnoreCase("Week")) {
                            targetValue = String.valueOf(Integer.parseInt(dayTargetVal) * 7);
                            deviation = getDeviation(actualValue, targetValue);
                            needleValue = Double.parseDouble(String.valueOf(deviation));
                        } else if (timeDetailsArray.get(3).toString().equalsIgnoreCase("Month")) {
                            targetValue = String.valueOf(Integer.parseInt(dayTargetVal) * 31);
                            deviation = getDeviation(actualValue, targetValue);
                            needleValue = Double.parseDouble(String.valueOf(deviation));
                        } else if (timeDetailsArray.get(3).toString().equalsIgnoreCase("Qtr")) {
                            targetValue = String.valueOf(Integer.parseInt(dayTargetVal) * 90);
                            deviation = getDeviation(actualValue, targetValue);
                            needleValue = Double.parseDouble(String.valueOf(deviation));
                        } else if (timeDetailsArray.get(3).toString().equalsIgnoreCase("Year")) {
                            targetValue = String.valueOf(Integer.parseInt(dayTargetVal) * 365);
                            deviation = getDeviation(actualValue, targetValue);
                            needleValue = Double.parseDouble(String.valueOf(deviation));
                        }
                    }
                } else {
                    needleValue = Double.parseDouble(String.valueOf(getGraphDeails().get("needleValue")));
                }
            } else {
                needleValue = Double.parseDouble(String.valueOf(getGraphDeails().get("needleValue")));
            }
            String measType = String.valueOf(getGraphDeails().get("measType"));
            String KPIIds = String.valueOf(getGraphDeails().get("KPIIds"));

            if (needleValue < startRange) {
                startRange = Math.round(needleValue - 10);
            } else if (needleValue > endRange) {
                endRange = Math.round(needleValue + 10);
            }
            // header div starts
            if (!"ONEVIEW".equals(oneViewCheck)) {
                tableBuffer.append("<div class=\"portlet-header portletHeader ui-corner-all\">");
                tableBuffer.append("<table style='height:10px;width:100%' >");//outer table1
                tableBuffer.append("<tr valign='top' align='right'>");
                tableBuffer.append("<td align=\"left\" style='color:#369;font-weight:bold'>");
                tableBuffer.append("<a href='javascript:void(0)' onclick=\"editPortletName('" + PortletId + "','" + portalTabId + "','" + PortletName + "','" + currDate + "','" + callFrom + "')\"  style='text-decoration:none' class=\"calcTitle\" title=\"Edit Name\"><font size=\"1px\"><span id='" + PortletId + "_span'><b>" + PortletName + "</b> </span> </font></a>");
                tableBuffer.append("</td>");

                //code to add additional links in future here
                tableBuffer.append("<td align=\"right\"> ");
                tableBuffer.append("<table border=0>");
                tableBuffer.append("<tr>");
                tableBuffer.append("<td>&nbsp;&nbsp;</td>");
//              tableBuffer.append("<td>");
//            tableBuffer.append("<a href='javascript:void(0)'  class=\"ui-icon ui-icon-disk\"  onclick=\"saveXmalOfPortlet('" + PortletId + "',document.getElementsByName('chkREP-" + PortletId + "-" + portalTabId + "'),document.getElementById('REP" + PortletId + "-" + portalTabId + "'),'" + perBy + "','" + graphType + "','" + portalTabId + "','" + currDate + "')\"  style='text-decoration:none' class=\"calcTitle\" title=\"Save Portlet\"><font size=\"1px\"><b> </b> </font></a> &nbsp;&nbsp;");
//            tableBuffer.append("</td>");
                tableBuffer.append("<td>");
//             //swathi changes 4 kpigraph
//            tableBuffer.append("<a href='javascript:void(0)'  class=\"ui-icon ui-icon-disk\"  onclick=\"saveXmalOfPortlet('" + PortletId + "',document.getElementsByName('chkREP-" + PortletId + "-" + portalTabId + "'),document.getElementById('REP" + PortletId + "-" + portalTabId + "'),'" + perBy + "','" + kpigraphType + "','" + portalTabId + "','" + currDate + "','"+callFrom+"')\"  style='text-decoration:none' class=\"calcTitle\" title=\"Save Portlet\"><font size=\"1px\"><b> </b> </font></a> &nbsp;&nbsp;");
//            tableBuffer.append("<div style='display:none;width:auto;height:50px;background-color:#ffffff;overflow:auto;position:absolute;text-align:left;border:1px solid #000000;border-top-width: 0px;' id='saveXmalOfPortlet" + PortletId + "-" + portalTabId + "'>");
//            tableBuffer.append("<table><tr><td>Over Write</td><td><input type='radio' name='saveRediooption" + PortletId + "-" + portalTabId + "' id='overwrite" + PortletId + "-" + portalTabId + "' value='overwrite' ></td></tr><tr><td>Save As New </td><td><input type='radio' name='saveRediooption" + PortletId + "-" + portalTabId + "' id='saveAsNew" + PortletId + "-" + portalTabId + "' value='saveAsNew' checked></td></tr></table>");
//            tableBuffer.append("</div>");
//            tableBuffer.append("</td>");
//            tableBuffer.append("<td>");

                //code to change graph type at run time

                tableBuffer.append("<a href='javascript:void(0)' onclick=\"openkpiGrpTypeDisplay(document.getElementById('GrpType" + PortletId + "-" + portalTabId + "'))\"  style='text-decoration:none' class=\"calcTitle\" title=\"Sort\"><font size=\"1px\"><b>Graph Types</b> </font></a> &nbsp;&nbsp;");
                tableBuffer.append("<div style='display:none;width:auto;height:auto;background-color:#ffffff;overflow:auto;position:absolute;text-align:left;border:1px solid #000000;border-top-width: 0px;' id='GrpType" + PortletId + "-" + portalTabId + "'>");
                tableBuffer.append("<Table>");

                for (int i = 0; i < KPIGraphTypes.length; i++) {
                    tableBuffer.append("<Tr valign='top'>");
                    tableBuffer.append("<Td valign='top'>");
                    if (KPIGraphTypes[i].equalsIgnoreCase(kpigraphType)) {
                        tableBuffer.append("<b>" + KPIGraphTypes[i] + "</b>");
                    } else {
                        tableBuffer.append("<a href='javascript:void(0)' onclick=\"openkpiGrpTypePreviews('" + PortletId + "',document.getElementsByName('kpiGrpP-" + PortletId + "-" + portalTabId + "'),'','" + KPIGraphTypes[i] + "','" + portalTabId + "')\">" + KPIGraphTypes[i] + "</a>");
                    }
                    tableBuffer.append("</Td>");
                    tableBuffer.append("</Tr>");

                }
                tableBuffer.append("</Table>");//closing of inner table
                tableBuffer.append(" </div>");

                //end of code to change graph type

                tableBuffer.append("<a class=\"ui-icon ui-icon-trash\" style='text-decoration:none' href=\"javascript:deletePortlet('" + PortletId + "','" + String.valueOf(metaInfo.get("PortletName")) + "','" + portalTabId + "')\" title='Delete '" + String.valueOf(metaInfo.get("PortletName")) + "' ></a>");
                tableBuffer.append(" </td>");
                tableBuffer.append("<td>");
                tableBuffer.append("<a class=\"ui-icon ui-icon-pencil\" style='text-decoration:none' href=\"javascript:editTarget('" + PortletId + "','" + portalTabId + "','','" + kpigraphType + "','" + measurename + "','" + dayTargetVal + "')\" title='Edit '" + String.valueOf(metaInfo.get("PortletName")) + "' ></a>");
                tableBuffer.append(" </td>");
                tableBuffer.append("<td>");
                tableBuffer.append("<a href='javascript:void(0)' class=\"ui-icon ui-icon-gear\" onclick=\"DrillToReport('" + PortletId + "','" + portalTabId + "')\"  style='text-decoration:none' class=\"calcTitle\" title=\"Drill\"></a>");
                tableBuffer.append(" </td>");
                tableBuffer.append("</tr>");
                tableBuffer.append("</table>");
                tableBuffer.append("</td>");

                //end of code to add additional links in future here

                tableBuffer.append("</tr>");
                tableBuffer.append("</table>");
                tableBuffer.append("</Td>");
                tableBuffer.append("</Tr>");
                tableBuffer.append("</Table>");
                tableBuffer.append(" </div>");
            }
            //header div ends

            //content div starts
            if (!"ONEVIEW".equals(oneViewCheck)) {
                tableBuffer.append("<div style=\"width:415px;height:300px;\">");
                tableBuffer.append("<table width=\"100%\">");
                tableBuffer.append(" <tr valign='top'> ");
                tableBuffer.append("<td valign='top' style=\"color:#369;font-weight:bold\">");
            }
            if (isIfFxCharts()) {//condition to check whether graph category is Fx charts or Jfree charts  true for Fx Charts
                //need to introduce iframe added by santhosh.kumar@progenbusiness.com
                tableBuffer.append("<iframe id='iframe-" + PortletId + "' frameborder='0' style='width:400px;height:290px;overflow:auto' src='PbPortletFXKPIGraphDisp.jsp?PORTLETID=" + PortletId + "&startRange=" + startRange + "&endRange=" + endRange + "&firstBreak=" + firstBreak + "&secondBreak=" + secondBreak + "&needleValue=" + needleValue + "&graphType=" + kpigraphType + "&PortalTabId=" + portalTabId + "'></iframe>");
                //end of code for iframe
            } else {
                pchart.setCtxPath(request.getContextPath());
                if (kpigraphType.equals("Meter")) {
                    //if(graphDeails.containsKey("graphClassName")==true && graphDeails.get("graphClassName").toString().equalsIgnoreCase("N"))
                    if (!measType.equalsIgnoreCase("null") && !measType.equalsIgnoreCase("")) {
                        if (measType.equalsIgnoreCase("Non-Standard")) {
                            pchart.setColorchange("yes");
                        } else {
                            pchart.setColorchange("no");
                        }
                    } else {
                        pchart.setColorchange("no");
                    }

                    pchart.GetMeterChart(startRange, endRange, firstBreak, secondBreak, needleValue, "", session, response, out);

                } else {
                    pchart.GetThermChart(startRange, endRange, firstBreak, secondBreak, needleValue, session, response, out);
                }
                if (pchart.chartDisplay != null && !pchart.chartDisplay.equalsIgnoreCase("")) {
                    tableBuffer.append(pchart.chartDisplay);
                    this.portLet.setImgName(pchart.mapname);
                    //portletPreviewSB.append(pchart.chartDisplay);
                }
            }
            if (!"ONEVIEW".equals(oneViewCheck)) {
                tableBuffer.append(" </td>");
                tableBuffer.append(" <td align=\"right\"> </td>");
                tableBuffer.append("</tr>");
                tableBuffer.append("</table>");
                tableBuffer.append("<Table  style=\"width:99%\"  cellpadding=\"0\" cellspacing=\"1\" id=\"tablesorter\"  class=\"tablesorter\" >");
                tableBuffer.append("<thead><tr>");
                tableBuffer.append("<th>Measure</th>");
                tableBuffer.append("<th>Target Value</th>");
                tableBuffer.append("<th>Actual Value</th>");
                tableBuffer.append("<th>Deviation(%)</th>");
                tableBuffer.append("</tr></thead>");
                tableBuffer.append("<td>" + measurename + "</td>");
                NumberFormat formatter = NumberFormat.getInstance(new Locale("en_US"));
                if (targetValue != null) {
                    tableBuffer.append("<td>" + formatter.format(Double.parseDouble(targetValue)) + "</td>");
                } else {
                    tableBuffer.append("<td></td>");
                }
                if (actualValue != null) {
                    tableBuffer.append("<td>" + formatter.format(Double.parseDouble(String.valueOf(actualValue))) + "</td>");
                } else {
                    tableBuffer.append("<td></td>");
                }
                if (deviation != 0) {
                    tableBuffer.append("<td>" + deviation + "</td>");
                } else {
                    tableBuffer.append("<td></td>");
                }
                tableBuffer.append("<tfoot></tfoot><tbody>");
                tableBuffer.append("</tbody></table>");
                tableBuffer.append("</div>");
            }

        }
        if (ispdfGenerator) {
            return graphBuilder;
        } else {
            return tableBuffer;
        }
    }

    public void processQueryDetails(Element root) {
        reportQryElementIds = new ArrayList();
        reportQryAggregations = new ArrayList();
        reportQryColNames = new ArrayList();

        List row = root.getChildren("rQuery");//Only one row as of now

        /*
         * Start of Processing of parameters
         */
        for (int i = 0; i < row.size(); i++) {//Loop for One section under portlet
            Element Companyname = (Element) row.get(i);
            List paramRow = Companyname.getChildren("rqDeatils");
            for (int j = 0; j < paramRow.size(); j++) {
                Element paramElement = (Element) paramRow.get(j);
                reportQryElementIds.add(xmUtil.getXmlTagValue(paramElement, "elementId"));
                reportQryAggregations.add(xmUtil.getXmlTagValue(paramElement, "elementAgg"));
                reportQryColNames.add(xmUtil.getXmlTagValue(paramElement, "elementName"));
            }

            if (Companyname.getChildren("rqMoreInfo") != null) {
                this.sortColumnsAndOrder = new HashMap<String, String>();
                paramRow = Companyname.getChildren("rqMoreInfo");
                String sortCol;
                String sortOrder;
                for (int j = 0; j < paramRow.size(); j++) {
                    Element paramElement = (Element) paramRow.get(j);
                    sortCol = "A_" + xmUtil.getXmlTagValue(paramElement, "orderCol");
                    sortOrder = xmUtil.getXmlTagValue(paramElement, "orderType");
                    this.sortColumnsAndOrder.put(sortCol, sortOrder);
                }
            }
            if (Companyname.getChildren("pgMaster") != null) {
                paramRow = Companyname.getChildren("pgMaster");
                for (int j = 0; j < paramRow.size(); j++) {
                    Element paramElement = (Element) paramRow.get(j);
                    getGraphDeails().put("graphName", xmUtil.getXmlTagValue(paramElement, "graphName"));
                    getGraphDeails().put("graphType", xmUtil.getXmlTagValue(paramElement, "graphType"));
                    getGraphDeails().put("graphClass", xmUtil.getXmlTagValue(paramElement, "graphClass"));
                    getGraphDeails().put("graphHeight", xmUtil.getXmlTagValue(paramElement, "graphHeight"));
                    getGraphDeails().put("graphWidth", xmUtil.getXmlTagValue(paramElement, "graphWidth"));
                    getGraphDeails().put("showLegent", xmUtil.getXmlTagValue(paramElement, "showLegent"));
                    getGraphDeails().put("startRange", xmUtil.getXmlTagValue(paramElement, "startRange"));
                    getGraphDeails().put("endRange", xmUtil.getXmlTagValue(paramElement, "endRange"));
                    getGraphDeails().put("firstBreak", xmUtil.getXmlTagValue(paramElement, "firstBreak"));
                    getGraphDeails().put("secondBreak", xmUtil.getXmlTagValue(paramElement, "secondBreak"));
                    getGraphDeails().put("needleValue", xmUtil.getXmlTagValue(paramElement, "needleValue"));
                    try {
                        if (xmUtil.getXmlTagValue(paramElement, "measType") != null) {
                            getGraphDeails().put("measType", xmUtil.getXmlTagValue(paramElement, "measType"));
                        }
                        if (xmUtil.getXmlTagValue(paramElement, "daytargetVal") != null) {
                            getGraphDeails().put("daytargetVal", xmUtil.getXmlTagValue(paramElement, "daytargetVal"));
                        }
                        if (xmUtil.getXmlTagValue(paramElement, "measureName") != null) {
                            getGraphDeails().put("measureName", xmUtil.getXmlTagValue(paramElement, "measureName"));
                        }
                    } catch (IndexOutOfBoundsException ex) {
                        logger.error("Exception",ex);
                    }
                }
            }

        }//End Loop for One section under portlet

        //details for graph

    }

    public void processParameters(Element root) {
        List row = root.getChildren("pParameter");//Only one row as of now

        /*
         * Start of Processing of parameters
         */
        for (int i = 0; i < row.size(); i++) {//Loop for One section under portlet
            Element Companyname = (Element) row.get(i);
            List paramRow = Companyname.getChildren("ppDetail");
            for (int j = 0; j < paramRow.size(); j++) {
                ArrayList paramInfo = new ArrayList();
                Element paramElement = (Element) paramRow.get(j);
                if (xmUtil.getXmlTagValue(paramElement, "element_id").equalsIgnoreCase("TIME")) {
                    paramInfo.add("TIME");
                } else {
                    paramInfo.add(xmUtil.getXmlTagValue(paramElement, "element_id"));
                }
                pParam.elementId = xmUtil.getXmlTagValue(paramElement, "element_id");
                paramInfo.add(xmUtil.getXmlTagValue(paramElement, "paramDispName"));
                paramInfo.add(xmUtil.getXmlTagValue(paramElement, "childElementId"));
                paramInfo.add(xmUtil.getXmlTagValue(paramElement, "dimId"));
                paramInfo.add(xmUtil.getXmlTagValue(paramElement, "dimTabId"));
                paramInfo.add(xmUtil.getXmlTagValue(paramElement, "displayType"));
                paramInfo.add(xmUtil.getXmlTagValue(paramElement, "relLevel"));
                paramInfo.add(xmUtil.getXmlTagValue(paramElement, "dispSeqNo"));
                paramInfo.add(xmUtil.getXmlTagValue(paramElement, "defaultValue"));
                paramInfo.add("CBOARP" + xmUtil.getXmlTagValue(paramElement, "element_id"));

                reportParameters.put(xmUtil.getXmlTagValue(paramElement, "element_id"), paramInfo);
                reportParametersValues.put(xmUtil.getXmlTagValue(paramElement, "element_id"), String.valueOf(paramInfo.get(8)));

                getParameterNamesHashMap().put(String.valueOf(xmUtil.getXmlTagValue(paramElement, "element_id")), xmUtil.getXmlTagValue(paramElement, "paramDispName").toString());
            }
        }//End Loop for One section under portlet
        /*
         * ArrayList paramInfo = new ArrayList(); paramInfo.add("Time");
         * paramInfo.add("Time"); paramInfo.add(""); paramInfo.add("");
         * paramInfo.add(""); paramInfo.add(""); paramInfo.add("");
         * paramInfo.add(""); paramInfo.add(null); paramInfo.add("CBOARPTime");
         *
         * reportParameters.put("Time", "Time");
         * reportParametersValues.put("Time",null);
         * ParameterNamesHashMap.put("Time", "Time");
         */
    }

    public void processMetainfo(Element root) {
        List row = root.getChildren("pMaster");//Only one row as of now

        /*
         * Start of Processing of parameters
         */
        for (int i = 0; i < row.size(); i++) {//Loop for One section under portlet
            Element Companyname = (Element) row.get(i);

            metaInfo.put("PortletName", xmUtil.getXmlTagValue(Companyname, "pmName"));
            metaInfo.put("DisplayName", xmUtil.getXmlTagValue(Companyname, "pmDisplayName"));
            metaInfo.put("DisplayType", xmUtil.getXmlTagValue(Companyname, "pmDisplayType"));

        }//End Loop for One section under portlet

    }

    public void processTimeDeatils(Element root) {
        timeDetailsMap = new HashMap();
        timeDetailsArray = new ArrayList();
        /*
         * Time Processing starts
         */

        List row = root.getChildren("pTime");//Only one row as of now

        /*
         * Start of Processing of parameters
         */
        for (int i = 0; i < row.size(); i++) {//Loop for section two under portlet
            Element Companyname = (Element) row.get(i);


            List timeMasterRow = Companyname.getChildren("ptMaster");
            for (int j = 0; j < timeMasterRow.size(); j++) {
                Element paramElement = (Element) timeMasterRow.get(j);
                timeDetailsArray.add(xmUtil.getXmlTagValue(paramElement, "timeLevel"));
                timeDetailsArray.add(xmUtil.getXmlTagValue(paramElement, "timeType"));
            }


            List timeDetailsRow = Companyname.getChildren("ptDetails");
            for (int j = 0; j < timeDetailsRow.size(); j++) {
                ArrayList<String> timeInfo = new ArrayList<String>();
                Element paramElement1 = (Element) timeDetailsRow.get(j);
                String temp = xmUtil.getXmlTagValue(paramElement1, "timeColType");
                String currVal = xmUtil.getXmlTagValue(paramElement1, "defaultValue");
                if (currVal == null || "".equalsIgnoreCase(currVal) || currVal.equalsIgnoreCase("Current Date")) {

                    {//default Value
                        if (temp.equalsIgnoreCase("AS_OF_DATE") || temp.equalsIgnoreCase("AS_OF_DATE1") || temp.equalsIgnoreCase("AS_OF_DATE") || temp.equalsIgnoreCase("AS_OF_DATE1")) {
                            timeInfo.add(pParam.getdateforpage());
                        } else if (temp.equalsIgnoreCase("AS_OF_MONTH") || temp.equalsIgnoreCase("AS_OF_MONTH1")) {
                            timeInfo.add(pParam.getmonthforpage());
                        } else if (temp.equalsIgnoreCase("AS_OF_YEAR") || temp.equalsIgnoreCase("AS_OF_YEAR1")) {
                            timeInfo.add(pParam.getYearforpage());
                        } else if (temp.equalsIgnoreCase("PRG_PERIOD_TYPE")) {
                            timeInfo.add("Month");
                        } else if (temp.equalsIgnoreCase("PRG_COMPARE")) {
                            timeInfo.add("Last Period");
                        }

                    }

                } else {
                    timeInfo.add(currVal);
                }

                timeInfo.add("CBO_" + temp);
                timeInfo.add(xmUtil.getXmlTagValue(paramElement1, "timeColName"));
                timeInfo.add(xmUtil.getXmlTagValue(paramElement1, "timeColSeq"));
                timeInfo.add(xmUtil.getXmlTagValue(paramElement1, "timeFormSeq"));
                timeInfo.add(currVal);
                timeInfo.add(temp);

                timeDetailsMap.put(timeInfo.get(6), timeInfo);

            }
            ArrayList timeInfo = new ArrayList();
            if (timeDetailsArray.get(0).toString().equalsIgnoreCase("Year") && timeDetailsArray.get(1).toString().equalsIgnoreCase("PRG_YEAR_CMP")) {
                timeInfo = (ArrayList) timeDetailsMap.get("AS_OF_YEAR");
                timeDetailsArray.add(timeInfo.get(0));
                timeInfo = (ArrayList) timeDetailsMap.get("AS_OF_YEAR1");
                timeDetailsArray.add(timeInfo.get(0));

            } else if (timeDetailsArray.get(0).toString().equalsIgnoreCase("DAY") && timeDetailsArray.get(1).toString().equalsIgnoreCase("PRG_STD")) {
                timeInfo = (ArrayList) timeDetailsMap.get("AS_OF_DATE");
                timeDetailsArray.add(timeInfo.get(0));
                timeInfo = (ArrayList) timeDetailsMap.get("PRG_PERIOD_TYPE");
                timeDetailsArray.add(timeInfo.get(0));
                timeInfo = (ArrayList) timeDetailsMap.get("PRG_COMPARE");
                timeDetailsArray.add(timeInfo.get(0));
            } else if (timeDetailsArray.get(0).toString().equalsIgnoreCase("MONTH") && timeDetailsArray.get(1).toString().equalsIgnoreCase("PRG_STD")) {
                timeInfo = (ArrayList) timeDetailsMap.get("AS_OF_MONTH");
                if (timeInfo.get(0) == null) {
                    timeInfo.remove(timeInfo.get(0));
                    timeInfo.add("SEP-10");
                }
                timeDetailsArray.add(timeInfo.get(0));
                timeInfo = (ArrayList) timeDetailsMap.get("PRG_PERIOD_TYPE");
                timeDetailsArray.add(timeInfo.get(0));
                timeInfo = (ArrayList) timeDetailsMap.get("PRG_COMPARE");
                timeDetailsArray.add(timeInfo.get(0));
            }

        }//End of section 2 of collection

    }

    public void processViewByDetails(Element root) {

        reportRowViewbyValues = new ArrayList();
        reportColViewbyValues = new ArrayList();
        List row = root.getChildren("pViewBy");//Only one row as of now

        /*
         * Start of Processing of parameters
         */
        for (int i = 0; i < row.size(); i++) {//Loop for One section under portlet
            Element Companyname = (Element) row.get(i);
            if (Companyname.getChildren("pvRowViewBy") != null) {
                List paramRow = Companyname.getChildren("pvRowViewBy");
                for (int j = 0; j < paramRow.size(); j++) {
                    ArrayList paramInfo = new ArrayList();
                    Element paramElement = (Element) paramRow.get(j);

                    List pViewByChild = paramElement.getChildren("pvrMasterData");
                    for (int k = 0; k < pViewByChild.size(); k++) {
                        Element pViewByChildElement = (Element) pViewByChild.get(j);

                        List pvrdefaultValuechild = pViewByChildElement.getChildren("pvrdefaultValue");
                        for (int row1 = 0; row1 < pvrdefaultValuechild.size(); row1++) {
                            Element defaultValue = (Element) pvrdefaultValuechild.get(row1);
                            reportRowViewbyValues.add(defaultValue.getText());
                        }

                        // RowViewbyValues.add(xmUtil.getXmlTagValue(pViewByChildElement, "pvrdefaultValue"));
                    }
//                    for (int k = 0; k < pViewByChild.size(); k++) {
//                        Element pViewByChildElement = (Element) pViewByChild.get(j);
//                        reportRowViewbyValues.add(xmUtil.getXmlTagValue(pViewByChildElement,"pvrdefaultValue"));
//                    }

                }
            }

            ////For col View By
            if (Companyname.getChildren("pvColViewBy") != null) {
                List paramRow = Companyname.getChildren("pvColViewBy");
                for (int j = 0; j
                        < paramRow.size(); j++) {
                    ArrayList paramInfo = new ArrayList();
                    Element paramElement = (Element) paramRow.get(j);

                    List pViewByChild = paramElement.getChildren("pvrMasterData");
                    for (int k = 0; k
                            < pViewByChild.size(); k++) {
                        Element pViewByChildElement = (Element) pViewByChild.get(j);

                        reportColViewbyValues.add(xmUtil.getXmlTagValue(pViewByChildElement, "pvrdefaultValue"));
                    }

                }
            }

        }//End Loop for One section under portlet

    }

    public HashMap getReportMetadaData() {
        return this.reportMetadaData;
    }

    public HashMap getReportParameters() {
        return this.reportParameters;
    }

    public HashMap[] getReportViewBys() {
        return this.reportViewBys;
    }

    public HashMap getReportViewByMain() {
        return this.reportViewByMain;
    }

    public String buildTable(HashMap ParametersHashMap, HashMap TableHashMap, String PortletId, String portalTabId) {
        PortLetSorterHelper portletSorterHelper = portLet.getPortletSorterHelper();
        ReportTemplateDAO DAO = new ReportTemplateDAO();
        PbReportCollection collect = new PbReportCollection();
        PbReportQuery repQuery = new PbReportQuery();
        ArrayList displayLabels = new ArrayList();
        String sortType = "Top";
        int countVal = 0;
        String[] dbColumns = new String[0];

        reportQryElementIds = new ArrayList();
        reportQryAggregations = new ArrayList();
        reportIncomingParameters = new HashMap();

        HashMap paramValues = new HashMap();
        ArrayList REP = null;
        ArrayList CEP = null;
        ArrayList Measures = null;
        ArrayList Parameters = null;

        ArrayList REPNames = null;
        ArrayList CEPNames = null;
        ArrayList ParametersNames = null;

        HashMap TimeDimHashMap = new HashMap();
        ArrayList TimeDetailstList = new ArrayList();

        PbReturnObject retObj = null;
        StringBuffer tableBuffer = new StringBuffer("");
        try {
            int endCount = 10;
            int viewByCount = 0;

            NumberFormat nFormat = NumberFormat.getInstance(Locale.US);
            nFormat.setMaximumFractionDigits(1);
            nFormat.setMinimumFractionDigits(1);

            TimeDimHashMap = (HashMap) ParametersHashMap.get("TimeDimHashMap");
            TimeDetailstList = (ArrayList) ParametersHashMap.get("TimeDetailstList");

            if (TableHashMap.get("REP") != null) {
                REP = (ArrayList) TableHashMap.get("REP");
                REPNames = (ArrayList) TableHashMap.get("REPNames");
            }
            if (getRuleOn() != null && getRuleOn().equalsIgnoreCase("Dimension")) {
                paramValues = reportParametersValues;
            }

            if (TableHashMap.get("CEP") != null) {
                CEP = (ArrayList) TableHashMap.get("CEP");
                CEPNames = (ArrayList) TableHashMap.get("CEPNames");
            }

            if (TableHashMap.get("Measures") != null) {
                Measures = (ArrayList) TableHashMap.get("Measures");
            }

            if (ParametersHashMap.get("Parameters") != null) {
                Parameters = (ArrayList) ParametersHashMap.get("Parameters");
                ParametersNames = (ArrayList) ParametersHashMap.get("ParametersNames");
            }
            HttpSession session = getServletRequest().getSession(false);
            String oneViewCheck = "";
            String height = "";
            String width = "";
            oneViewCheck = (String) session.getAttribute("ONEVIEW");
            height = (String) session.getAttribute("height");
            width = (String) session.getAttribute("width");

            if (REP == null || REP.size() == 0) {
                REP = new ArrayList();
                REPNames = new ArrayList();
                if (Parameters != null && Parameters.size() != 0) {
                    REP.add(String.valueOf(Parameters.get(0)));
                    REPNames.add(String.valueOf(ParametersNames.get(0)));
                }

            }

            if (CEP == null || CEP.size() == 0) {
                CEP = new ArrayList();
            }

            if (Measures == null) {
                Measures = new ArrayList();
            }

            if (REP.size() != 0 && Measures.size() != 0) {
                for (int i = 0; i < Parameters.size(); i++) {
                    if (paramValues.get(String.valueOf(Parameters.get(i))) == null) {
                        paramValues.put(String.valueOf(Parameters.get(i)), "All");
                        reportIncomingParameters.put("CBOAPR" + String.valueOf(Parameters.get(i)), null);
                    }
                }
                collect.reportIncomingParameters = reportIncomingParameters;
                collect.reportColViewbyValues = CEP;
                collect.reportRowViewbyValues = REP;
                collect.timeDetailsArray = TimeDetailstList;
                collect.timeDetailsMap = TimeDimHashMap;

                collect.getParamMetaDataForReportDesigner();
                for (int j = 0; j < Measures.size(); j++) {
                    if (!reportQryElementIds.contains(String.valueOf(Measures.get(j)))) {
                        reportQryElementIds.add(String.valueOf(Measures.get(j)));
                    }
                }

                if (reportQryElementIds != null && reportQryElementIds.size() != 0) {
                    reportQryAggregations = DAO.getReportQryAggregations(reportQryElementIds);

                    collect.reportQryElementIds = reportQryElementIds;
                    collect.reportQryAggregations = reportQryAggregations;

                    repQuery.setQryColumns(collect.reportQryElementIds);
                    repQuery.setColAggration(collect.reportQryAggregations);

                    repQuery.setDefaultMeasure(String.valueOf(reportQryElementIds.get(0)));
                    repQuery.setDefaultMeasureSumm(String.valueOf(reportQryAggregations.get(0)));
                }

                repQuery.setRowViewbyCols(REP);
                repQuery.setColViewbyCols(CEP);
                repQuery.setParamValue(paramValues);
                repQuery.setTimeDetails(TimeDetailstList); //assigning time details array to report query
                repQuery.setDefaultSortedColumnAndOrder(this.sortColumnsAndOrder);
                repQuery.setWhereClause(getWhereClause());

                if (reportQryElementIds != null && reportQryElementIds.size() != 0) {
                    retObj = repQuery.getPbReturnObjectCrossChecked(String.valueOf(reportQryElementIds.get(0)), null);
                    HashMap timeInfoDetails = repQuery.getTimememdetails();
                    portLet.setTimeInfoDetails(timeInfoDetails);
                    returnObject = retObj;
                    if (retObj != null) {
                        dbColumns = retObj.getColumnNames();
                    }

                    displayLabels = (ArrayList) REPNames.clone();
                    if (CEP != null && CEP.size() != 0) {
                        for (int nonViewByIndex = collect.reportRowViewbyValues.size(); nonViewByIndex
                                < (collect.reportRowViewbyValues.size() + repQuery.crossTabNonViewBy.size()); nonViewByIndex++) {
                            displayLabels.add(String.valueOf(repQuery.crossTabNonViewByMap.get(dbColumns[nonViewByIndex])));
                        }
                    } else {
                        for (int nonViewByIndex = collect.reportRowViewbyValues.size(); nonViewByIndex
                                < (collect.reportRowViewbyValues.size() + repQuery.NonViewByMap.size()); nonViewByIndex++) {
                            displayLabels.add(String.valueOf(repQuery.NonViewByMap.get(dbColumns[nonViewByIndex])));
                        }
                    }
                    viewByCount = collect.reportRowViewbyValues.size();
                }

                int count = 20;
                ArrayList<String> repQryElementIds = new ArrayList<String>();
                ArrayList<Integer> viewSeqList = new ArrayList<Integer>();
                ArrayList<SortColumn> sortColumns = new ArrayList<SortColumn>();
                SortColumn sortColumn;

                if (TableHashMap.get("perByVal") != null) {

                    PbReturnObject sortPbretObj;
                    String viewPerBy = "";
                    String perBy = String.valueOf(TableHashMap.get("perByVal"));

//                    for (int k = 0; k < reportQryElementIds.size(); k++) {
                    repQryElementIds.add("A_" + reportQryElementIds.get(0));
//                    }

                    if (retObj != null) {
                        dbColumns = retObj.getColumnNames();
                    }

                    viewPerBy = perBy.split("-")[0];
                    count = Integer.parseInt(perBy.split("-")[1]);
                    for (String column : repQryElementIds) {
                        if (viewPerBy.equalsIgnoreCase("Top")) {
                            if (!CEP.isEmpty()) {
                                sortColumn = new SortColumn("A1", SortOrder.DESCENDING);
                            } else {
                                sortColumn = new SortColumn(column, SortOrder.DESCENDING);
                            }
                        } else {
                            if (!CEP.isEmpty()) {
                                sortColumn = new SortColumn("A1", SortOrder.ASCENDING);
                            } else {
                                sortColumn = new SortColumn(column, SortOrder.ASCENDING);
                            }
                        }
                        sortColumns.add(sortColumn);
                    }
                    if (retObj.getRowCount() <= count) {
                        count = retObj.getRowCount();
                    }
                    if (viewPerBy.contains("Top")) {
                        sortPbretObj = new PbReturnObject();
                        viewSeqList = retObj.findTopBottom(sortColumns, count);
                        sortPbretObj = retObj;
                        retObj.setViewSequence(viewSeqList);
                    } else {
                        sortPbretObj = new PbReturnObject();
                        viewSeqList = retObj.findTopBottom(sortColumns, count);
                        sortPbretObj = retObj;
                        retObj.setViewSequence(viewSeqList);
                    }
                }

                if (retObj.getRowCount() > 0) {
                    dbColumns = retObj.getColumnNames();

                    /*
                     * tableBuffer.append("<div id='pager" + PortletId + "'
                     * class=\"pager\" align=\"left\" >");
                     * tableBuffer.append("<img src=\"" +
                     * getServletRequest().getContextPath() +
                     * "/tablesorter/addons/pager/icons/first.png\"
                     * class=\"first\"/>"); tableBuffer.append("<img src=\"" +
                     * getServletRequest().getContextPath() +
                     * "/tablesorter/addons/pager/icons/prev.png\"
                     * class=\"prev\"/>"); tableBuffer.append("<input
                     * type=\"text\" class=\"pagedisplay\"/>");
                     * tableBuffer.append("<img src=\"" +
                     * getServletRequest().getContextPath() +
                     * "/tablesorter/addons/pager/icons/next.png\"
                     * class=\"next\"/>"); tableBuffer.append("<img src=\"" +
                     * getServletRequest().getContextPath() +
                     * "/tablesorter/addons/pager/icons/last.png\"
                     * class=\"last\"/>"); tableBuffer.append("<select
                     * class=\"pagesize\">"); tableBuffer.append("<option
                     * value=\"5\">5</option>"); tableBuffer.append("<option
                     * selected value=\"10\">10</option>");
                     * tableBuffer.append("</select>"); tableBuffer.append("
                     * </div>");
                     */

                    StringBuffer SbHeaders = new StringBuffer("");

                    int maxWidth = 100;
                    for (int l = 0; l < displayLabels.size(); l++) {
                        int columnWidth = displayLabels.get(l).toString().length();
                        //SbHeaders.append("<th style=\"width:" + columnWidth + "px\">" + displayLabels.get(l) + "</th> ");
                        if (oneViewCheck != null && !oneViewCheck.equalsIgnoreCase("") && "ONEVIEW".equals(oneViewCheck)) {
                            if (height != null && !height.equalsIgnoreCase("") && width != null && !width.equalsIgnoreCase("")) {
                                SbHeaders.append("<th style=\"width:auto\">" + displayLabels.get(l) + "</th> ");
                            }
                        } else {
                            SbHeaders.append("<th style=\"width:auto\">" + displayLabels.get(l) + "</th> ");
                        }
                        if (l == 0) {
                            maxWidth = columnWidth;
                        } else {
                            maxWidth += columnWidth;
                        }
                    }

                    //maxWidth=maxWidth+10;

                    //tableBuffer.append("<table cellspacing=\"1\" id='tablesorter" + PortletId + "' border=\"1\" class=\"tablesorter\" width=\"" + maxWidth + "px\">");
                    if (portalTabId.equalsIgnoreCase("-2")) {
                        if (oneViewCheck != null && !oneViewCheck.equalsIgnoreCase("") && "ONEVIEW".equals(oneViewCheck)) {
                            if (height != null && !height.equalsIgnoreCase("") && width != null && !width.equalsIgnoreCase("")) {
                                tableBuffer.append("<table cellspacing=\"1\" class=\"tablesorter\" id='tablesorter" + PortletId + "-" + portalTabId + "'width=\"100px\" border=\"1\" >");
                            }
                        } else {
                            tableBuffer.append("<table cellspacing=\"1\" class=\"tablesorter\" id='tablesorter" + PortletId + "-" + portalTabId + "'width=\"100px\" border=\"1\">");
                        }
                    } else {
                        if (oneViewCheck != null && !oneViewCheck.equalsIgnoreCase("") && "ONEVIEW".equals(oneViewCheck)) {
                            if (height != null && !height.equalsIgnoreCase("") && width != null && !width.equalsIgnoreCase("")) {
                                tableBuffer.append("<table cellspacing=\"1\" class=\"tablesorter\" id='tablesorter" + PortletId + "-" + portalTabId + "'width=\"100px\" >");
                            }
                        } else {
                            tableBuffer.append("<table cellspacing=\"1\" class=\"tablesorter\" id='tablesorter" + PortletId + "-" + portalTabId + "'width=\"100px\" >");
                        }
                    }
                    tableBuffer.append("<thead>");
                    tableBuffer.append("<tr valign=\"top\">");
                    tableBuffer.append(SbHeaders.toString());
                    /*
                     * for (int k = 0; k < displayLabels.size(); k++) {
                     * tableBuffer.append("<th>" + displayLabels.get(k) + "</th>
                     * "); }
                     */
                    tableBuffer.append("</tr>");
                    tableBuffer.append("</thead>");
                    tableBuffer.append("<tfoot>");
                    tableBuffer.append("</tfoot>");
                    if (oneViewCheck != null && !oneViewCheck.equalsIgnoreCase("") && "ONEVIEW".equals(oneViewCheck)) {
                        if (height != null && !height.equalsIgnoreCase("") && width != null && !width.equalsIgnoreCase("")) {
                            tableBuffer.append("<tbody style=\"width:" + width + "px;height:" + (Integer.parseInt(height) - 60) + "px;overflow: auto;\" >");
                        }
                    } else {
                        tableBuffer.append("<tbody style='min-height:auto;min-height:360px;max-height:" + (portLet.getPortletHeight() - 160) + "px; overflow: auto;'>");
                    }
                    if (retObj.getRowCount() <= endCount) {
                        endCount = retObj.getRowCount();
                    }
                    if (portletSorterHelper != null) {
                        countVal = portletSorterHelper.getCountVal();
                        sortType = portletSorterHelper.getSortType();
                        if (portletSorterHelper.getIsSortAll()) {
                            countVal = retObj.getRowCount();
                        }
                    } else {

                        countVal = retObj.getRowCount();
                    }

                    count = countVal;




//              count=countVal;
                    //endCount = retObj.getRowCount();//modified on 05-03-2010
                    viewSeqList = new ArrayList<Integer>();
                    if (viewSeqList.isEmpty()) {

                        if (sortType.equalsIgnoreCase("Top")) {
                            if (portletSorterHelper == null) {
                                if (!Measures.isEmpty() && CEP.isEmpty()) {
                                    retObj = retObj.sort(1, "A_" + Measures.get(0).toString(), "N");
                                } else if (!CEP.isEmpty()) {
                                    String[] cols = retObj.getColumnNames();
                                    retObj = retObj.sort(1, "A1", "N");

                                }
                            } else if (portletSorterHelper != null) {
                                if (!Measures.isEmpty() && CEP.isEmpty()) {
                                    retObj = retObj.sort(1, "A_" + Measures.get(0).toString(), "N");
                                } else if (!CEP.isEmpty()) {
                                    retObj = retObj.sort(1, "A1", "N");
                                }


                            }

                        } else {
                            if (!Measures.isEmpty() && CEP.isEmpty()) {
                                retObj = retObj.sort(0, "A_" + portletSorterHelper.getSortByColumeVal(), "N");
                            } else if (!CEP.isEmpty()) {
                                retObj = retObj.sort(0, "A1", "N");
                            }


                        }
                        for (int rowId = 0; rowId < count; rowId++) {
                            tableBuffer.append("<tr>");
                            for (int colId = 0; colId < viewByCount; colId++) {
                                tableBuffer.append("<td align='left'  style=\"font-size:11px;\">");
                                tableBuffer.append(retObj.getFieldValueString(rowId, dbColumns[colId]));
                                tableBuffer.append("</td>");
                            }
                            double value;
                            for (int colId = viewByCount; colId < dbColumns.length; colId++) {
                                BigDecimal bd = retObj.getFieldValueBigDecimal(rowId, dbColumns[colId]);
                                if (bd != null) {
                                    value = bd.doubleValue();
                                    if (!portLet.getPortalTableColor().isEmpty() && !portLet.getPortalTableColor().get(0).getColorCondOper().isEmpty()) {
                                        List<DashboardTableColorGroupHelper> colorGroupHelpers = portLet.getPortalTableColor();
                                        DashboardTableColorGroupHelper colorGroupHelper = null;
                                        for (DashboardTableColorGroupHelper helper : colorGroupHelpers) {
                                            //  
                                            if (helper.getElementId().equalsIgnoreCase(dbColumns[colId].replace("A_", ""))) {
                                                colorGroupHelper = helper;
                                            }
                                        }
                                        if (colorGroupHelper != null) {
                                            PbDashboardViewerBD dashboardBD = new PbDashboardViewerBD();
                                            tableBuffer.append(dashboardBD.buildTableTD(colorGroupHelper, dbColumns[colId].replace("A_", ""), value));
                                        } else {
                                            tableBuffer.append("<td align=\"right\">").append(nFormat.format(value)).append("</td>");
                                        }
                                    } else {
                                        tableBuffer.append("<td align='right'>");
                                        tableBuffer.append("" + nFormat.format(value));//need to check for data type in future to convert it into decimal
                                        tableBuffer.append("</td>");
                                    }
                                }
                            }
                            tableBuffer.append("</tr>");
                        }
                        tableBuffer.append("</tbody>");
                        tableBuffer.append("</table>");
                    } else {

                        for (int rowId = 0; rowId < count; rowId++) {
                            tableBuffer.append("<tr valign = \"top\">");
                            tableBuffer.append("<td align='left'>");
                            tableBuffer.append(retObj.getFieldValueString(viewSeqList.get(rowId), 0));
                            tableBuffer.append("</td>");
                            tableBuffer.append("<td align='right'>");
                            if (!Measures.isEmpty() && CEP.isEmpty()) {
                                tableBuffer.append("" + nFormat.format(retObj.getFieldValueBigDecimal(viewSeqList.get(rowId), "A_" + Measures.get(0))));//need to check for data type in future to convert it into decimal
                            } else if (!CEP.isEmpty()) {
                                tableBuffer.append("" + nFormat.format(retObj.getFieldValueBigDecimal(viewSeqList.get(rowId), "A1")));//need to check for data type in future to convert it into decimal
                            }
                            tableBuffer.append("</td>");
                            tableBuffer.append("</tr>");
                        }
                        tableBuffer.append("</tbody>");
                        tableBuffer.append("</table>");
                    }
                } else {
                    tableBuffer.append("<table cellspacing=\"0\"  border=\"0\"  height=\"275px\" width=\"100%\">");
                    tableBuffer.append("<tr valign = \"middle\">");
                    tableBuffer.append("<td align='center'>");
                    tableBuffer.append("No Data to Display");
                    tableBuffer.append("</td>");
                    tableBuffer.append("</tr>");
                    tableBuffer.append("</table>");
                }
            } else {
            }
        } catch (Exception e) {
            logger.error("Exception:", e);
        }

        return tableBuffer.toString();
    }

    public boolean isIfFxCharts() {
        return ifFxCharts;
    }

    public void setIfFxCharts(boolean ifFxCharts) {
        this.ifFxCharts = ifFxCharts;
    }

    public HttpServletRequest getServletRequest() {
        return servletRequest;
    }

    public void setServletRequest(HttpServletRequest servletRequest) {
        this.servletRequest = servletRequest;
    }

    public HttpServletResponse getServletResponse() {
        return servletResponse;
    }

    public void setServletResponse(HttpServletResponse servletResponse) {
        this.servletResponse = servletResponse;
    }

    public HashMap getGraphDeails() {
        return graphDeails;
    }

    public void setGraphDeails(HashMap graphDeails) {
        this.graphDeails = graphDeails;
    }

    public HashMap getParameterNamesHashMap() {
        return ParameterNamesHashMap;
    }

    public void setParameterNamesHashMap(HashMap ParameterNamesHashMap) {
        this.ParameterNamesHashMap = ParameterNamesHashMap;
    }

    //added for timedrill in portlets
    public String TimeDrill(ArrayList timeDetailsArray) {
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
                drillUrl += "&DDrill=Y&DrillMonth=DEC-2008";

            } else if (timeDetailsArray.get(3).toString().equalsIgnoreCase("Week")) {
                drillUrl = "&CBO_PRG_PERIOD_TYPE=Day";
                drillUrl += "&DDrill=Y&DrillWeek=";
            } else {
                drillUrl = "";
                drillUrl += "&DrillDate=";
            }

        } else {
            drillUrl = "&cbodonotuser=";
        }


        return (drillUrl);
    }

//for kpitable
    public void getPortalKpi(String elementIds) throws Exception {

        String finalQuery = "";
        PbReturnObject retObj = null;
        String[] dbColNames = null;
        String[] kpiCols = new String[elementIds.length()];
        String kpiOrder = " Case ";
        PbDb db = new PbDb();
        kpiCols = elementIds.split(",");
        for (int i = kpiCols.length - 1; i >= 0; i--) {
            kpiOrder += " when ELEMENT_ID = " + kpiCols[i] + " then " + (i + 1);
        }
        kpiOrder += " else 100000 end ";

        String sqlstr = "";
        sqlstr += " SELECT ELEMENT_ID,AGGREGATION_TYPE,case when USER_COL_DESC is null then USER_COL_NAME else USER_COL_DESC end USER_COL_NAME FROM PRG_USER_ALL_INFO_DETAILS ";
        sqlstr += " where ELEMENT_ID in ( " + elementIds + " )";
        sqlstr += " order by " + kpiOrder + " ";

        finalQuery = sqlstr;

        retObj = db.execSelectSQL(finalQuery);

        dbColNames = retObj.getColumnNames();

        for (int i = 0; i < retObj.getRowCount(); i++) {
            ArrayList kpiDetails = new ArrayList();

            kpiDetails.add(retObj.getFieldValueString(i, dbColNames[0]));
            kpiDetails.add(retObj.getFieldValueString(i, dbColNames[1]));
            kpiDetails.add(i);
            kpiDetails.add(retObj.getFieldValueString(i, dbColNames[2]));
            kpiDetails.add("KPI");
            kpiDetails.add(1);
            kpiDetails.add("0");
            kpiQuery.put(kpiDetails.get(0).toString(), kpiDetails);
        }
        kpiMasterQuery.put("1", kpiQuery);
    }

    public String processPortalKpi(String kpiMasterId, String PortletId, String portalTabId) throws Exception {

        PbReportQuery repQuery = new PbReportQuery();
        PbReturnObject pbretObj = new PbReturnObject();

        kpiQuery = (LinkedHashMap) kpiMasterQuery.get(kpiMasterId);
        String[] a1 = (String[]) (kpiQuery.keySet()).toArray(new String[0]);

        ArrayList QueryCols = new ArrayList();
        ArrayList QueryAggs = new ArrayList();
        ArrayList kpiElements = new ArrayList();
        String[] elements = new String[a1.length];
        PbReturnObject retObj = null;
        PbDb db = new PbDb();
        String str = "";
        String sqlstr = "";
        String finalQuery = "";
        StringBuilder dispKpi = new StringBuilder();
        String[] ColNames = null;

        for (int j = 0; j < a1.length; j++) {
            ArrayList kpi = (ArrayList) kpiQuery.get(a1[j]);
            kpiElements.add(kpi.get(0).toString());
            elements[j] = kpi.get(0).toString();
        }

        dispKpi.append("<Table width=\"99%;height:auto\" cellpadding=\"1\" cellspacing=\"1\" id=\"tablesorter\"  border=\"1\" class=\"tablesorter\" >");
        dispKpi.append("<thead align=\"center\">");
        dispKpi.append("<tr>");
        if (displayType.equalsIgnoreCase("kpi")) {
            dispKpi.append("<th width=\"150px\"><strong>KPI</strong></th>");
            dispKpi.append("<th width=\"150px\"><strong>Value</strong></th>");
        } else {
            dispKpi.append("<th width=\"150px\"><strong>KPI</strong></th>");
            dispKpi.append("<th width=\"150px\"><strong>Value</strong></th>");
            dispKpi.append("<th width=\"150px\"><strong>Target</strong></th>");
            dispKpi.append("<th width=\"150px\"><strong>Deviation</strong></th>");
            dispKpi.append("<th width=\"150px\"><strong>Deviation%</strong></th>");
        }
        dispKpi.append("</tr></thead>");
        dispKpi.append("<tfoot></tfoot><tbody>");

        for (int i = 0; i < a1.length; i++) {
            ArrayList kpi = (ArrayList) kpiQuery.get(a1[i]);
            QueryCols = new ArrayList();
            QueryAggs = new ArrayList();
            retObj = null;
            sqlstr = "";
            {
                sqlstr += "select ELEMENT_ID , REF_ELEMENT_ID , REF_ELEMENT_TYPE , AGGREGATION_TYPE ";
                sqlstr += " from ";
                sqlstr += " PRG_USER_ALL_INFO_DETAILS ";
                sqlstr += " where ELEMENT_ID=" + kpi.get(0).toString() + " ";
                sqlstr += " OR REF_ELEMENT_ID = " + kpi.get(0).toString() + " ";
                sqlstr += " order by REF_ELEMENT_TYPE asc ";
                //////////////////////////////.println("sqlstr in processkpi is: " + sqlstr);
                finalQuery = sqlstr;
                retObj = db.execSelectSQL(finalQuery);
                if (retObj != null && retObj.getRowCount() > 0) {
                    ColNames = retObj.getColumnNames();
                    for (int looper = 0; looper < retObj.getRowCount(); looper++) {
                        KPIElement kpiElem = new KPIElement();
                        String refElementId = retObj.getFieldValueString(looper, ColNames[1]);
                        kpiElem.setElementId(retObj.getFieldValueString(looper, ColNames[0]));
                        kpiElem.setRefElementId(refElementId);
                        kpiElem.setRefElementType(retObj.getFieldValueString(looper, ColNames[2]));
                        kpiElem.setAggregationType(retObj.getFieldValueString(looper, ColNames[3]));
                        kpiElementList.put(refElementId, kpiElem);

                        QueryCols.add(retObj.getFieldValueString(looper, ColNames[0]));//Query columns
                        QueryAggs.add(retObj.getFieldValueString(looper, ColNames[3])); //query Aggration

                    }
                } else {
                    QueryCols.add(kpi.get(0).toString());//Query columns
                    QueryAggs.add(kpi.get(1).toString()); //query Aggration
                }
            }
            repQuery = new PbReportQuery();
            repQuery.setRowViewbyCols(reportRowViewbyValues);
            repQuery.setColViewbyCols(new ArrayList());
            repQuery.setQryColumns(QueryCols);
            repQuery.setColAggration(QueryAggs);
            repQuery.setTimeDetails(timeDetailsArray);
            repQuery.setDefaultMeasure(String.valueOf(QueryCols.get(0)));
            repQuery.setDefaultMeasureSumm(String.valueOf(QueryAggs.get(0)));
            repQuery.isKpi = true;

            pbretObj = repQuery.getPbReturnObject(String.valueOf(QueryCols.get(0)));//report kpi query which returns current,prior ,change and change%
            str += this.displayPortalKpi(pbretObj, kpi.get(0).toString(), kpi.get(3).toString(), kpi.get(6).toString(), kpiMasterId, PortletId, portalTabId);
        }
        dispKpi.append(str);
        dispKpi.append("</tbody></Table>");
        repQuery.isKpi = false;
        return dispKpi.toString();
    }

    public String displayPortalKpi(PbReturnObject retObjQry, String ElementId, String ElementName, String ReportId, String kpiMasterid, String PortletId, String portalTabId) throws Exception {

        String[] COLORS = {
            "#0095B6",
            "#9ACD32",
            "#007BA7",
            "#0095B6",
            "#78866B",
            "#00FFFF",
            "#0067A5",
            "#9ACD32",
            "#FEFE33",
            "#4F7942",
            "#CFB53B",
            "#006600",
            "#003300",
            //"#cc9700", //
            "#ecddoo",};
        int tableWidth = 200;
        int sum = 0;
        double currVal = 0.0;
        double priorVal = 0.0;
        String fontColor = "#fff";
        StringBuffer kpiBarChart = new StringBuffer();
        String value = "";
        String target = "--";
        String Dev = "--";
        String DevPer = "0.0";

        List<KPIElement> kpiElements = kpiElementList.get(ElementId);

        StringBuffer dispKpi = new StringBuffer();
        dispKpi.append("<Tr><Td style=\"border-width: 0px\"><strong>" + ElementName + "</strong></Td>");
        for (int i = 0; i < kpiElements.size(); i++) {
            if (retObjQry.getRowCount() > 0) {
                if (kpiElements.size() > 1) {
                    String temp = "A_" + kpiElements.get(i).getElementId();
                    String type = kpiElements.get(i).getRefElementType();
                    if ((retObjQry.getFieldValueString(0, temp)) != null && !("".equalsIgnoreCase(retObjQry.getFieldValueString(0, temp)))) {

                        if (type.equalsIgnoreCase("1")) {
                            currVal = Double.parseDouble((retObjQry.getFieldValueString(0, temp)));
                            ////////////.println("");
                            double[] valArray = {currVal};
                            double[] celWidth = new double[valArray.length];
                            for (int j = 0; j < valArray.length; j++) {
                                sum += (valArray[j]);
                            }

                            double widthFl = 0;
                            for (int k = 0; k < valArray.length; k++) {
                                widthFl = ((tableWidth * (valArray[k])) / sum);
                                // celWidth[k]=
                                celWidth[k] = ((tableWidth * (valArray[k])) / sum);
                                celWidth[k] = Math.round(celWidth[k]);
                            }
                            kpiBarChart.append("<Table cellpadding=\"0\" cellspacing=\"0\" style=\"width:100%\" ><Tr>");
                            for (int kpi = 0; kpi < valArray.length; kpi++) {
                                value = retObjQry.getModifiedNumber(new BigDecimal(valArray[kpi]));
                                if (celWidth[kpi] == 0.0) {
                                    value = "0.0";
                                    COLORS[kpi] = "#FFFFFF";
                                    fontColor = "#369";
                                }
                                kpiBarChart.append("<Td style=\"width:" + celWidth[kpi] + "px;height:6px;font-size:10px;font-family:VERDANA;color:" + fontColor + ";background-color:" + COLORS[kpi] + " ;padding:6px;valign=middle\" align=\"center\" >" + value + "</Td>");
                                //}
                            }
                            kpiBarChart.append("</Tr></Table>");
                            dispKpi.append("<Td align=\"center\" style=\"border-width: 0px\" padding=\"0px\">" + value + "</Td>");
                        }
                    }
                }
            }
        }
        if (displayType.equalsIgnoreCase("BasicTarget")) {
            dispKpi.append("<Td align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" onclick=\"changeTargetValue('" + PortletId + "','" + portalTabId + "','" + ElementId + "','" + kpiMasterid + "','" + ReportId + "')\">" + target + "</Td>");
            dispKpi.append("<Td align=\"center\" padding=\"0px\">" + Dev + "</Td>");
            dispKpi.append("<Td align=\"center\" padding=\"0px\">" + DevPer + "%" + "</Td>");
        }
        dispKpi.append("</Tr>");
        return dispKpi.toString();
    }

    public String getWhereClause() {
        return whereClause;
    }

    public void setWhereClause(String whereClause) {
        this.whereClause = whereClause;
    }

    public String[] getMeasureIDs() {
        return measureIDs;
    }

    public void setMeasureIDs(String[] measureIDs) {
        this.measureIDs = measureIDs;
    }

    public String[] getAggreGationType() {
        return aggreGationType;
    }

    public void setAggreGationType(String[] aggreGationType) {
        this.aggreGationType = aggreGationType;
    }

    public String[] getMeasureNames() {
        return measureNames;
    }

    public void setMeasureNames(String[] measureNames) {
        this.measureNames = measureNames;
    }

    public GraphProperty getPortletProperty() {
        return portletProperty;
    }

    public void setPortletProperty(GraphProperty portletProperty) {
        this.portletProperty = portletProperty;
    }

    public PortLet getPortLet() {
        return portLet;
    }

    public void setPortLet(PortLet portLet) {
        this.portLet = portLet;
    }

    public void processGrapStatus(Element root) {
        String graphClassName = "";
        List row = root.getChildren("KiGraphStatus");//Only one row as of now

        /*
         * Start of Processing of parameters
         */
        for (int i = 0; i < row.size(); i++) {//Loop for One section under portlet
            Element Companyname = (Element) row.get(i);
            graphClassName = xmUtil.getXmlTagValue(Companyname, "status");

        }//End Loop for One section under portlet
        getGraphDeails().put("graphClassName", graphClassName);
    }

    public String getRuleOn() {
        return ruleOn;
    }

    public void setRuleOn(String ruleOn) {
        this.ruleOn = ruleOn;
    }

    /**
     * @param reportParams the reportParams to set
     */
    public void setReportParams(Map<String, String> reportParams) {
        this.reportParams = reportParams;
    }

    public void setCallFrom(String callFrom) {
        this.callFrom = callFrom;
    }

    public PbReturnObject getReturnObject() {
        return returnObject;
    }

    public void setReturnObject(PbReturnObject returnObject) {
        this.returnObject = returnObject;
    }

//    public void setMinMacAVgValues( HttpServletRequest request, HttpServletResponse response, HttpSession session, String REP, String CEP, String PortletId, String perBy, String graphType, String portalTabId, String PortletName, String currDate){
//        try {
//            this.processProgenDataPortlet(request, response, session, REP, CEP, PortletId, perBy, graphType, portalTabId, PortletName, currDate);
//        } catch (Exception ex) {
//            logger.error("Exception:",ex);
//        }
//    }
    public void processDataSet(String currDate) throws Exception {

        processDocument();
        HashMap ParametersHashMap = new HashMap();
        HashMap TableHashMap = new HashMap();
        ArrayList dets = new ArrayList();
        ArrayList detnames = new ArrayList();
        for (int i = 0; i < reportParameters.values().toArray().length; i++) {
            dets.add(reportParameters.values().toArray()[i].toString().split(",")[0].replace("[", ""));
            detnames.add(reportParameters.values().toArray()[i].toString().split(",")[1]);
        }

//            
//            
        if (!currDate.equalsIgnoreCase("") && !(currDate.equalsIgnoreCase((String) timeDetailsArray.get(2)))) {
            timeDetailsArray.remove(2);
            timeDetailsArray.add(2, currDate);
            ArrayList dateList = (ArrayList) timeDetailsMap.get("AS_OF_DATE");
            dateList.remove(0);
            dateList.add(0, currDate);
            timeDetailsMap.put("AS_OF_DATE", dateList);
        }

//            
        ParametersHashMap.put("Parameters", dets);
        ParametersHashMap.put("TimeDetailstList", timeDetailsArray);
        ParametersHashMap.put("TimeDimHashMap", timeDetailsMap);
        ParametersHashMap.put("ParametersNames", detnames);

        REPNames = new ArrayList();
        REPList = (ArrayList) reportRowViewbyValues.clone();
        for (int k = 0; k < reportRowViewbyValues.size(); k++) {
            if (getParameterNamesHashMap().get(String.valueOf(reportRowViewbyValues.get(k))) != null) {
                REPNames.add(getParameterNamesHashMap().get(String.valueOf(reportRowViewbyValues.get(k))));
            } else {
//                        REPList.add("TIME");
                REPNames.add("Time");
            }
        }
        TableHashMap.put("REP", REPList);
        TableHashMap.put("REPNames", REPNames);

        TableHashMap.put("REP", REPList);
        TableHashMap.put("REPNames", REPNames);

        CEPNames = new ArrayList();
        CEPList = new ArrayList();
        CEPNames = new ArrayList();
        CEPList = (ArrayList) reportColViewbyValues.clone();

        for (int k = 0; k < reportColViewbyValues.size(); k++) {
            if (getParameterNamesHashMap().get(String.valueOf(reportColViewbyValues.get(k))) != null) {
                CEPNames.add(getParameterNamesHashMap().get(String.valueOf(reportColViewbyValues.get(k))));
            } else {
//                        CEPList.add("TIME");
                CEPNames.add("Time");
            }
            //CEPNames.add(ParameterNamesHashMap.get(String.valueOf(reportColViewbyValues.get(k))));
        }
        TableHashMap.put("CEP", CEPList);
        TableHashMap.put("CEPNames", CEPNames);



        TableHashMap.put("Measures", reportQryElementIds);
        TableHashMap.put("MeasuresNames", reportQryColNames);

        ReportTemplateDAO DAO = new ReportTemplateDAO();
        PbReportCollection collect = new PbReportCollection();
        PbReportQuery repQuery = new PbReportQuery();
        ArrayList displayLabels = new ArrayList();
        String sortType = "Top";
        int countVal = 0;
        String[] dbColumns = new String[0];

        reportQryElementIds = new ArrayList();
        reportQryAggregations = new ArrayList();
        reportIncomingParameters = new HashMap();

        HashMap paramValues = new HashMap();
        ArrayList REP = null;
        ArrayList CEP = null;
        ArrayList Measures = null;
        ArrayList Parameters = null;


        ArrayList ParametersNames = null;

        HashMap TimeDimHashMap = new HashMap();
        ArrayList TimeDetailstList = new ArrayList();

        PbReturnObject retObj = null;

        NumberFormat nFormat = NumberFormat.getInstance(Locale.US);
        nFormat.setMaximumFractionDigits(1);
        nFormat.setMinimumFractionDigits(1);

        TimeDimHashMap = (HashMap) ParametersHashMap.get("TimeDimHashMap");
        TimeDetailstList = (ArrayList) ParametersHashMap.get("TimeDetailstList");

        if (TableHashMap.get("REP") != null) {
            REP = (ArrayList) TableHashMap.get("REP");
            REPNames = (ArrayList) TableHashMap.get("REPNames");
        }

        if (TableHashMap.get("CEP") != null) {
            CEP = (ArrayList) TableHashMap.get("CEP");
            CEPNames = (ArrayList) TableHashMap.get("CEPNames");
        }

        if (TableHashMap.get("Measures") != null) {
            Measures = (ArrayList) TableHashMap.get("Measures");
        }

        if (ParametersHashMap.get("Parameters") != null) {
            Parameters = (ArrayList) ParametersHashMap.get("Parameters");
            ParametersNames = (ArrayList) ParametersHashMap.get("ParametersNames");
        }

        if (REP == null || REP.isEmpty()) {
            REP = new ArrayList();
            REPNames = new ArrayList();
            if (Parameters != null && !Parameters.isEmpty()) {
                REP.add(String.valueOf(Parameters.get(0)));
                REPNames.add(String.valueOf(ParametersNames.get(0)));
            }

        }
        if (CEP == null || CEP.isEmpty()) {
            CEP = new ArrayList();
        }

        if (Measures == null) {
            Measures = new ArrayList();
        }

        if (!REP.isEmpty() && Measures.size() != 0) {
            for (int i = 0; i < Parameters.size(); i++) {
                if (paramValues.get(String.valueOf(Parameters.get(i))) == null) {
                    paramValues.put(String.valueOf(Parameters.get(i)), "All");
                    reportIncomingParameters.put("CBOAPR" + String.valueOf(Parameters.get(i)), null);
                }
            }
            collect.reportIncomingParameters = reportIncomingParameters;
            collect.reportColViewbyValues = CEP;
            collect.reportRowViewbyValues = REP;
            collect.timeDetailsArray = TimeDetailstList;
            collect.timeDetailsMap = TimeDimHashMap;

            collect.getParamMetaDataForReportDesigner();
            for (int j = 0; j < Measures.size(); j++) {
                if (!reportQryElementIds.contains(String.valueOf(Measures.get(j)))) {
                    reportQryElementIds.add(String.valueOf(Measures.get(j)));
                }
            }

            if (reportQryElementIds != null && !reportQryElementIds.isEmpty()) {
                reportQryAggregations = DAO.getReportQryAggregations(reportQryElementIds);

                collect.reportQryElementIds = reportQryElementIds;
                collect.reportQryAggregations = reportQryAggregations;

                repQuery.setQryColumns(collect.reportQryElementIds);
                repQuery.setColAggration(collect.reportQryAggregations);

                repQuery.setDefaultMeasure(String.valueOf(reportQryElementIds.get(0)));
                repQuery.setDefaultMeasureSumm(String.valueOf(reportQryAggregations.get(0)));
            }

            repQuery.setRowViewbyCols(REP);
            repQuery.setColViewbyCols(CEP);
            repQuery.setParamValue(paramValues);
            repQuery.setTimeDetails(TimeDetailstList); //assigning time details array to report query
            repQuery.setDefaultSortedColumnAndOrder(this.sortColumnsAndOrder);

            if (reportQryElementIds != null && !reportQryElementIds.isEmpty()) {
                retObj = repQuery.getPbReturnObjectCrossChecked(String.valueOf(reportQryElementIds.get(0)), null);
                HashMap timeInfoDetails = repQuery.getTimememdetails();
                portLet.setTimeInfoDetails(timeInfoDetails);
                returnObject = retObj;

            }
            dbColumns = retObj.getColumnNames();
            displayLabels = (ArrayList) REPNames.clone();
            if (CEP != null && CEP.size() != 0) {
                for (int nonViewByIndex = collect.reportRowViewbyValues.size(); nonViewByIndex
                        < (collect.reportRowViewbyValues.size() + repQuery.crossTabNonViewBy.size()); nonViewByIndex++) {
                    displayLabels.add(String.valueOf(repQuery.crossTabNonViewByMap.get(dbColumns[nonViewByIndex])));
                }
            } else {
                for (int nonViewByIndex = collect.reportRowViewbyValues.size(); nonViewByIndex
                        < (collect.reportRowViewbyValues.size() + repQuery.NonViewByMap.size()); nonViewByIndex++) {
                    displayLabels.add(String.valueOf(repQuery.NonViewByMap.get(dbColumns[nonViewByIndex])));
                }
            }
            displayLabelsForPdf.clear();
            displayLabelsForPdf.addAll(displayLabels);
        }

    }

    public void setIspdfGenerator(boolean ispdfGenerator) {
        this.ispdfGenerator = ispdfGenerator;
    }

    /**
     * @return the displayLabelsForPdf
     */
    public ArrayList getDisplayLabelsForPdf() {
        return displayLabelsForPdf;
    }

    public String getDrillElmentDetails() {
        return drillElmentDetails;
    }

    public void setDrillElmentDetails(String drillElmentDetails) {
        this.drillElmentDetails = drillElmentDetails;
    }

    /**
     * @return the drillOn
     */
    public String getDrillOn() {
        return drillOn;
    }

    /**
     * @param drillOn the drillOn to set
     */
    public void setDrillOn(String drillOn) {
        this.drillOn = drillOn;
    }

    public float getDeviation(String actulaValue, String targetValue) {

        Double deviation = (((double) ((Double.parseDouble(actulaValue) - Double.parseDouble(targetValue))) / Double.parseDouble(targetValue)) * 100);

        double tempDev = deviation;

        deviation = Math.abs(deviation);

        double d = tempDev;//deviation[i];
        float p = (float) Math.pow(10, 2);
        double Rval2 = 0;
        Rval2 = d * p;
        float tmp = Math.round(Rval2);
        float finalDev = tmp / p;

        return finalDev;
    }
}
