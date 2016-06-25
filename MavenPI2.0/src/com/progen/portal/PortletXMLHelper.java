/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.portal;

import com.progen.xml.pbXmlUtilities;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import org.jdom.Document;
import org.jdom.Element;
import utils.db.ProgenParam;

/**
 *
 * @author progen
 */
public class PortletXMLHelper implements Serializable {

    private Document portletXmlDocument;
    private ArrayList<String> ElementIds = new ArrayList<String>();
    private ArrayList<String> Aggregations = new ArrayList<String>();
    private ArrayList timeDetailsArray = new ArrayList();
    private ArrayList<String> RowViewbyValues = new ArrayList<String>();
    private ArrayList<String> ColViewbyValues = new ArrayList<String>();
    private Element root = null;
    private HashMap metaInfo = new HashMap();
    private pbXmlUtilities xmUtil = new pbXmlUtilities();
    private ProgenParam pParam = new ProgenParam();
    private LinkedHashMap<String, ArrayList<String>> reportParameters = new LinkedHashMap<String, ArrayList<String>>();
    private LinkedHashMap<String, String> reportParametersValues = new LinkedHashMap<String, String>();
    private HashMap parameterNamesHashMap = new HashMap();
    private HashMap<String, ArrayList<String>> timeDetailsMap;
    private ArrayList<String> QryElementIds = new ArrayList<String>();
    private ArrayList<String> QryAggregations;
    private ArrayList<String> QryColNames;
    private HashMap<String, String> sortColumnsAndOrder = null;
    private HashMap graphDeails = new HashMap();

    public PortletXMLHelper(Document document) {
        this.portletXmlDocument = document;
        processDocument();
    }

    public void processDocument() {
        if (portletXmlDocument != null) {
            root = portletXmlDocument.getRootElement();
            processMetainfo(root);
            processParameters(root);
            processTimeDeatils(root);
            processViewByDetails(root);
            processQueryDetails(root);
        }

    }

    public void processMetainfo(Element root) {
        List row = root.getChildren("pMaster");//Only one row as of now

        /*
         * Start of Processing of parameters
         */
        for (int i = 0; i < row.size(); i++) {//Loop for One section under portlet
            Element Companyname = (Element) row.get(i);

            getMetaInfo().put("PortletName", xmUtil.getXmlTagValue(Companyname, "pmName"));
            getMetaInfo().put("DisplayName", xmUtil.getXmlTagValue(Companyname, "pmDisplayName"));
            getMetaInfo().put("DisplayType", xmUtil.getXmlTagValue(Companyname, "pmDisplayType"));
            if (getMetaInfo().get("PortletName").toString().equalsIgnoreCase("Sales Qty Distribution")) {
                //  
            }

        }//End Loop for One section under portlet

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

                getReportParameters().put(xmUtil.getXmlTagValue(paramElement, "element_id"), paramInfo);
                getReportParametersValues().put(xmUtil.getXmlTagValue(paramElement, "element_id"), String.valueOf(paramInfo.get(8)));

                getParameterNamesHashMap().put(String.valueOf(xmUtil.getXmlTagValue(paramElement, "element_id")), xmUtil.getXmlTagValue(paramElement, "paramDispName").toString());
            }
        }
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
            if (timeMasterRow != null && !timeMasterRow.isEmpty()) {
                for (int j = 0; j < timeMasterRow.size(); j++) {
                    Element paramElement = (Element) timeMasterRow.get(j);
                    timeDetailsArray.add(xmUtil.getXmlTagValue(paramElement, "timeLevel"));
                    timeDetailsArray.add(xmUtil.getXmlTagValue(paramElement, "timeType"));
                }
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
                            timeInfo.add(getpParam().getdateforpage());
                        } else if (temp.equalsIgnoreCase("AS_OF_MONTH") || temp.equalsIgnoreCase("AS_OF_MONTH1")) {
                            timeInfo.add(getpParam().getmonthforpage());
                        } else if (temp.equalsIgnoreCase("AS_OF_YEAR") || temp.equalsIgnoreCase("AS_OF_YEAR1")) {
                            timeInfo.add(getpParam().getYearforpage());
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
                if (timeInfo.size() >= 7) {
                    getTimeDetailsMap().put(timeInfo.get(6), timeInfo);
                }

            }
            ArrayList timeInfo = new ArrayList();
            if (timeDetailsArray.get(0).toString().equalsIgnoreCase("Year") && timeDetailsArray.get(1).toString().equalsIgnoreCase("PRG_YEAR_CMP")) {
                timeInfo = (ArrayList) getTimeDetailsMap().get("AS_OF_YEAR");
                timeDetailsArray.add(timeInfo.get(0));
                timeInfo = (ArrayList) getTimeDetailsMap().get("AS_OF_YEAR1");
                timeDetailsArray.add(timeInfo.get(0));

            } else if (timeDetailsArray.get(0).toString().equalsIgnoreCase("DAY") && timeDetailsArray.get(1).toString().equalsIgnoreCase("PRG_STD")) {
                timeInfo = (ArrayList) getTimeDetailsMap().get("AS_OF_DATE");
                if (timeInfo != null && timeInfo.get(0) != null) {
                    timeDetailsArray.add(timeInfo.get(0));
                }
                timeInfo = (ArrayList) getTimeDetailsMap().get("PRG_PERIOD_TYPE");
                if (timeInfo != null && timeInfo.get(0) != null) {
                    timeDetailsArray.add(timeInfo.get(0));
                }
                timeInfo = (ArrayList) getTimeDetailsMap().get("PRG_COMPARE");
                if (timeInfo != null && timeInfo.get(0) != null) {
                    timeDetailsArray.add(timeInfo.get(0));
                }
            } else if (timeDetailsArray.get(0).toString().equalsIgnoreCase("MONTH") && timeDetailsArray.get(1).toString().equalsIgnoreCase("PRG_STD")) {
                timeInfo = (ArrayList) getTimeDetailsMap().get("AS_OF_MONTH");
                if (timeInfo.get(0) == null) {
                    timeInfo.remove(timeInfo.get(0));
                    timeInfo.add("SEP-10");
                }
                timeDetailsArray.add(timeInfo.get(0));
                timeInfo = (ArrayList) getTimeDetailsMap().get("PRG_PERIOD_TYPE");
                timeDetailsArray.add(timeInfo.get(0));
                timeInfo = (ArrayList) getTimeDetailsMap().get("PRG_COMPARE");
                timeDetailsArray.add(timeInfo.get(0));
            }

        }//End of section 2 of collection

    }

    public void processViewByDetails(Element root) {

        RowViewbyValues = new ArrayList();
        ColViewbyValues = new ArrayList();
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
                            RowViewbyValues.add(defaultValue.getText());
                        }

                        // RowViewbyValues.add(xmUtil.getXmlTagValue(pViewByChildElement, "pvrdefaultValue"));
                    }

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

                        ColViewbyValues.add(xmUtil.getXmlTagValue(pViewByChildElement, "pvrdefaultValue"));
                    }

                }
            }

        }//End Loop for One section under portlet

    }

    public void processQueryDetails(Element root) {
        QryElementIds = new ArrayList();
        QryAggregations = new ArrayList();
        QryColNames = new ArrayList();

        List row = root.getChildren("rQuery");//Only one row as of now

        /*
         * Start of Processing of parameters
         */
        for (int i = 0; i < row.size(); i++) {//Loop for One section under portlet
            Element Companyname = (Element) row.get(i);
            List paramRow = Companyname.getChildren("rqDeatils");
            for (int j = 0; j < paramRow.size(); j++) {
                Element paramElement = (Element) paramRow.get(j);
                getQryElementIds().add(xmUtil.getXmlTagValue(paramElement, "elementId"));
                getQryAggregations().add(xmUtil.getXmlTagValue(paramElement, "elementAgg"));
                getQryColNames().add(xmUtil.getXmlTagValue(paramElement, "elementName"));
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
                    this.getSortColumnsAndOrder().put(sortCol, sortOrder);
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
                    }
                }
            }

        }//End Loop for One section under portlet

        //details for graph

    }

    public ArrayList<String> getElementIds() {
        return ElementIds;
    }

    public ArrayList<String> getAggregations() {
        return Aggregations;
    }

    public ArrayList getTimeDetailsArray() {
        return timeDetailsArray;
    }

    public ArrayList<String> getRowViewbyValues() {
        return RowViewbyValues;
    }

    public ArrayList<String> getColViewbyValues() {
        return ColViewbyValues;
    }

    public ProgenParam getpParam() {
        return pParam;
    }

    public LinkedHashMap<String, ArrayList<String>> getReportParameters() {
        return reportParameters;
    }

    public LinkedHashMap<String, String> getReportParametersValues() {
        return reportParametersValues;
    }

    public HashMap getParameterNamesHashMap() {
        return parameterNamesHashMap;
    }

    public HashMap<String, ArrayList<String>> getTimeDetailsMap() {
        return timeDetailsMap;
    }

    public ArrayList<String> getQryElementIds() {
        return QryElementIds;
    }

    public ArrayList<String> getQryAggregations() {
        return QryAggregations;
    }

    public ArrayList<String> getQryColNames() {
        return QryColNames;
    }

    public HashMap<String, String> getSortColumnsAndOrder() {
        return sortColumnsAndOrder;
    }

    public HashMap getGraphDeails() {
        return graphDeails;
    }

    public HashMap getMetaInfo() {
        return metaInfo;
    }

    public String getPortletRepType() {
        return metaInfo.get("DisplayType").toString();
    }
}
