/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.portal;

import com.progen.portal.portlet.PortletDesignerResourceBundle;
import com.progen.portal.portlet.PortletDesignerSqlResBundle;
import java.util.*;
import org.apache.log4j.Logger;
import org.jdom.Element;
import prg.db.PbDb;
import prg.db.PbReturnObject;
import utils.db.ProgenConnection;

/**
 *
 * @author progen
 */
public class SavePortletXML extends PbDb {

    public static Logger logger = Logger.getLogger(SavePortletXML.class);
    private PortLet portLet = null;
    public String[] pMasterChildTags = {"pmName", "pmDisplayName", "pmDisplayType"};
    public String[] ppDetailChildTags = {"element_id", "paramDispName", "childElementId", "dimId", "dimTabId", "displayType", "relLevel", "dispSeqNo", "defaultValue", "isDisplay"};
    public String[] ptDetailsChildTags = {"timeColName", "timeColType", "timeColSeq", "timeFormSeq", "timeIsDisplayed", "defaultValue"};
    public String[] ptMasterChildTags = {"timeLevel", "timeType"};
    public String[] pvrMasterDataChildTags = {"pvrdefaultValue"};
    public String[] pgMasterChildTags = {"graphName", "graphType", "graphClass", "graphHeight", "graphWidth", "showLegent", "startRange", "endRange", "firstBreak", "secondBreak", "needleValue"};
    public String[] rqDetailsChildTags = {"elementId", "elementName", "elementAgg", "graphAxis"};
    public String[] rqMoreInfoChildTags = {"orderCol", "orderType", "totalRecord"};
    private String paramsString = "";
    private String paramString1 = "";
    private String paramString2 = "";
    ResourceBundle resBundle = null;
    private ArrayList<String> REP_Elements = new ArrayList<String>();
    private ArrayList<String> CEP_Elements = new ArrayList<String>();
    private String graphType = "";
    private HashMap GraphClassesHashMap = null;

    private ResourceBundle getResourceBundle() {
        if (resBundle == null) {
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) //                resBundle = (PortletDesignerResourceBundle) ResourceBundle.getBundle("com.progen.portal.portlet.PortletDesignerSqlResBundle");
            {
                resBundle = new PortletDesignerSqlResBundle();
            } else //                resBundle =  (PortletDesignerResourceBundle) ResourceBundle.getBundle("com.progen.portal.portlet.PortletDesignerResourceBundle");
            {
                resBundle = new PortletDesignerResourceBundle();
            }
        }
        return resBundle;
    }

    public Element buildMetaInfo(Element root) {
        String[] MetaInfoChildValues = new String[3];
        MetaInfoChildValues[0] = portLet.getPortLetName();
        MetaInfoChildValues[1] = portLet.getPortLetDes();
        MetaInfoChildValues[2] = (String) portLet.getPortletXMLHelper().getMetaInfo().get("DisplayType");


        Element pMasterEle = new Element("pMaster");
        Element child = null;

        for (int i = 0; i < pMasterChildTags.length; i++) {
            child = new Element(pMasterChildTags[i]);
            child.setText(MetaInfoChildValues[i]);
            pMasterEle.addContent(child);
        }
        root.addContent(pMasterEle);

        return root;
    }

    public Element buildParameters(Element root) {
        String getReportParamDetailsQuery = getResourceBundle().getString("getReportParamDetails");

        PbReturnObject retObj = null;
        String[] dbColumns = null;
        String finalQuery = "";
        String[] Obj = new String[2];

        Element pParameterEle = new Element("pParameter");
        Element ppDetailEle = null;
        Element child = null;
        int count = 1;


        try {

            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                Obj[0] = paramsString;
                Obj[1] = paramString2;
            } else {
                Obj[0] = paramsString;
                Obj[1] = paramString1;
            }

            finalQuery = buildQuery(getReportParamDetailsQuery, Obj);

            retObj = execSelectSQL(finalQuery);

            //added by santhosh.kumar@porgenbusiness.com on 03/12/2009
            if (portLet.getPortletXMLHelper().getTimeDetailsArray() != null && !portLet.getPortletXMLHelper().getTimeDetailsArray().isEmpty() && portLet.getPortletXMLHelper().getTimeDetailsMap() != null && !portLet.getPortletXMLHelper().getTimeDetailsMap().isEmpty()) {
                ppDetailEle = new Element("ppDetail");
                Obj = new String[10];
                Obj[0] = "Time";
                Obj[1] = "Time";
                Obj[2] = "";
                Obj[3] = "";
                Obj[4] = "";
                Obj[5] = "C";
                Obj[6] = "";
                Obj[7] = String.valueOf((1));
                Obj[8] = null;
                Obj[9] = "N";

                for (int j = 0; j < ppDetailChildTags.length; j++) {
                    child = new Element(ppDetailChildTags[j]);
                    child.setText(Obj[j]);
                    ppDetailEle.addContent(child);
                }
                pParameterEle.addContent(ppDetailEle);
                count = 2;
            }

            if (retObj != null && retObj.getRowCount() != 0) {
                dbColumns = retObj.getColumnNames();
                for (int i = 0; i < retObj.getRowCount(); i++) {
                    ppDetailEle = new Element("ppDetail");
                    Obj = new String[10];
                    Obj[0] = retObj.getFieldValueString(i, dbColumns[0]);
                    Obj[1] = retObj.getFieldValueString(i, dbColumns[1]);
                    Obj[2] = retObj.getFieldValueString(i, dbColumns[2]);
                    Obj[3] = retObj.getFieldValueString(i, dbColumns[3]);
                    Obj[4] = retObj.getFieldValueString(i, dbColumns[4]);
                    Obj[5] = retObj.getFieldValueString(i, dbColumns[5]);
                    Obj[6] = retObj.getFieldValueString(i, dbColumns[6]);
                    Obj[7] = String.valueOf((i + count));
                    Obj[8] = retObj.getFieldValueString(i, dbColumns[7]);
                    Obj[9] = retObj.getFieldValueString(i, dbColumns[8]);

                    for (int j = 0; j < ppDetailChildTags.length; j++) {
                        child = new Element(ppDetailChildTags[j]);
                        child.setText(Obj[j]);
                        ppDetailEle.addContent(child);
                    }
                    pParameterEle.addContent(ppDetailEle);
                }
                root.addContent(pParameterEle);
            }

        } catch (Exception exp) {
            logger.error("Exception:", exp);
        }
        return root;
    }

    public Element buildTimeDetails(Element root) {
        String[] Obj = null;

        Element pTimeEle = new Element("pTime");
        Element ptMasterEle = new Element("ptMaster");
        Element ptDetailsEle = null;
        Element child = null;
        ArrayList timeDetailsFromHashMap = null;

        try {
            if (portLet.getPortletXMLHelper().getTimeDetailsArray() != null && !portLet.getPortletXMLHelper().getTimeDetailsArray().isEmpty()) {
                Obj = new String[2];
                Obj[0] = String.valueOf(portLet.getPortletXMLHelper().getTimeDetailsArray().get(0));
                Obj[1] = String.valueOf(portLet.getPortletXMLHelper().getTimeDetailsArray().get(1));

                for (int i = 0; i < ptMasterChildTags.length; i++) {
                    child = new Element(ptMasterChildTags[i]);
                    child.setText(Obj[i]);
                    ptMasterEle.addContent(child);
                }
                pTimeEle.addContent(ptMasterEle);
            }
            if (portLet.getPortletXMLHelper().getTimeDetailsMap() != null) {
                Set details = portLet.getPortletXMLHelper().getTimeDetailsMap().keySet();
                Iterator it = details.iterator();
                while (it.hasNext()) {
                    Obj = new String[6];
                    String key = (String) it.next();

                    timeDetailsFromHashMap = (ArrayList) portLet.getPortletXMLHelper().getTimeDetailsMap().get(key);


                    Obj[0] = String.valueOf(timeDetailsFromHashMap.get(2));
                    Obj[1] = key;
                    Obj[2] = String.valueOf(timeDetailsFromHashMap.get(3));
                    Obj[3] = String.valueOf(timeDetailsFromHashMap.get(4));
                    Obj[4] = "N";
                    Obj[5] = "Current Date";

                    ptDetailsEle = new Element("ptDetails");
                    // String[] ptDetailsChildTags = {"timeColName", "timeColType", "timeColSeq", "timeFormSeq", "timeIsDisplayed", "defaultValue"};

                    for (int i = 0; i < ptDetailsChildTags.length; i++) {
                        child = new Element(ptDetailsChildTags[i]);
                        child.setText(Obj[i]);
                        ptDetailsEle.addContent(child);

                    }
                    pTimeEle.addContent(ptDetailsEle);

                    timeDetailsFromHashMap = null;
                    Obj = null;
                }
            }
            root.addContent(pTimeEle);

        } catch (Exception exp) {
            logger.error("Exception:", exp);
        }
        return root;
    }

    public Element buildViewByDetails(Element root) {
        String[] Obj = null;
        Element pViewByEle = new Element("pViewBy");
        Element pvrdefaultValueEle = null;

        Element pvRowViewByEle = null;
        Element pvColViewByEle = null;
        Element pvrMasterDataEle = null;


        if (REP_Elements != null && REP_Elements.size() != 0) {
            pvRowViewByEle = new Element("pvRowViewBy");
            pvrMasterDataEle = new Element("pvrMasterData");
            for (int i = 0; i < REP_Elements.size(); i++) {
                Obj = new String[1];
                Obj[0] = String.valueOf(REP_Elements.get(i));

                for (int j = 0; j < pvrMasterDataChildTags.length; j++) {
                    pvrdefaultValueEle = new Element(pvrMasterDataChildTags[j]);
                    pvrdefaultValueEle.setText(String.valueOf(Obj[j]));
                    pvrMasterDataEle.addContent(pvrdefaultValueEle);
                }
            }
            pvRowViewByEle.addContent(pvrMasterDataEle);
            pViewByEle.addContent(pvRowViewByEle);
        }


        if (CEP_Elements != null && CEP_Elements.size() != 0) {
            pvColViewByEle = new Element("pvColViewBy");
            pvrMasterDataEle = new Element("pvrMasterData");
            for (int i = 0; i < CEP_Elements.size(); i++) {
                Obj = new String[1];
                //Obj[0] = reportId;
                //Obj[1] = String.valueOf((i + 1));
                //Obj[2] = String.valueOf((j + 1));
                Obj[0] = String.valueOf(CEP_Elements.get(i));

                for (int j = 0; j < pvrMasterDataChildTags.length; j++) {
                    pvrdefaultValueEle = new Element(pvrMasterDataChildTags[j]);
                    pvrdefaultValueEle.setText(String.valueOf(Obj[j]));
                    pvrMasterDataEle.addContent(pvrdefaultValueEle);
                }
            }
            pvColViewByEle.addContent(pvrMasterDataEle);
            pViewByEle.addContent(pvColViewByEle);
        }
        root.addContent(pViewByEle);
        return root;
    }

    public Element buildQueryDetails(Element root) throws Exception {
        String[] Obj = null;
        Element rQueryEle = new Element("rQuery");
        Element pgMasterEle = null;
        Element rqDetailsEle = null;
        Element rqMoreInfoEle = null;
        Element child = null;

        ArrayList<String> barChartColumnNames = new ArrayList<String>();
        ArrayList<String> barChartColumnTitles = new ArrayList<String>();
        String[] viewBys = null;
        String[] axis = null;
        barChartColumnNames.addAll(REP_Elements);
        if (!CEP_Elements.isEmpty()) {
            barChartColumnNames.addAll(CEP_Elements);
        }
        for (String str : barChartColumnNames) {
            if (str.equalsIgnoreCase("TIME")) {
                str = "Time";
            }
            if (!str.equalsIgnoreCase("")) {
                barChartColumnTitles.add(portLet.getPortletXMLHelper().getReportParameters().get(str.trim()).get(1));
            }
        }

        barChartColumnNames.addAll(portLet.getPortletXMLHelper().getQryElementIds());
        barChartColumnTitles.addAll(portLet.getPortletXMLHelper().getQryColNames());
        String aggTypeQuery = null;
        PbReturnObject grppbro = null;
        String displayType = (String) portLet.getPortletXMLHelper().getMetaInfo().get("DisplayType");
        ArrayList<String> measures = new ArrayList<String>();
        ArrayList<String> measuresNames = new ArrayList<String>();
        if (!portLet.getPortletXMLHelper().getQryElementIds().isEmpty()) {
            for (String Str : portLet.getPortletXMLHelper().getQryElementIds()) {
                measures.add("A_" + Str.trim());
            }
            for (String colNames : portLet.getPortletXMLHelper().getQryColNames()) {
                measuresNames.add(colNames);
            }
        }

        if (displayType != null) {
            if (displayType.equalsIgnoreCase("Table") || displayType.equalsIgnoreCase("kpi") || displayType.equalsIgnoreCase("BasicTarget")) {
                if (measures != null && !measures.isEmpty()) {
                    //for (int i = 0; i < measures.size(); i++) {
                    barChartColumnNames = measures;
                    barChartColumnTitles = measuresNames;

                    for (int k = 0; k < barChartColumnNames.size(); k++) {
                        rqDetailsEle = new Element("rqDeatils");

                        Obj = new String[4];

                        Obj[0] = barChartColumnNames.get(k).replace("A_", "");
                        Obj[1] = barChartColumnTitles.get(k).replace("A_", "");
                        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                            aggTypeQuery = "select isnull(aggregation_type,'SUM' ) from prg_user_all_info_details where element_id in (" + Obj[0] + ")";
                        } else {
                            aggTypeQuery = "select nvl(aggregation_type,'SUM' ) from prg_user_all_info_details where element_id in (" + Obj[0] + ")";
                        }

                        grppbro = execSelectSQL(aggTypeQuery);
                        Obj[2] = grppbro.getFieldValueString(0, 0);
                        //Obj[2] = "SUM";
                        Obj[3] = "0";

                        for (int l = 0; l < rqDetailsChildTags.length; l++) {
                            child = new Element(rqDetailsChildTags[l]);
                            child.setText(Obj[l]);
                            rqDetailsEle.addContent(child);
                        }
                        rQueryEle.addContent(rqDetailsEle);
                    }

                    rqMoreInfoEle = new Element("rqMoreInfo");
                    Obj = new String[3];
                    Obj[0] = barChartColumnNames.get(0).replace("A_", "");
                    Obj[1] = "ASC";
                    Obj[2] = "10";

                    for (int l = 0; l < rqMoreInfoChildTags.length; l++) {
                        child = new Element(rqMoreInfoChildTags[l]);
                        child.setText(Obj[l]);
                        rqMoreInfoEle.addContent(child);
                    }
                    rQueryEle.addContent(rqMoreInfoEle);
                    //}
                }
            } else if (displayType.equalsIgnoreCase("Graph") || displayType.equalsIgnoreCase("KPI Graph")) {

                pgMasterEle = new Element("pgMaster");


                HashMap graphDetails = portLet.getPortletXMLHelper().getGraphDeails();
                if (!graphType.equalsIgnoreCase("")) {
                    graphDetails.put("graphType", graphType);
                }
                graphDetails.put("graphClass", GraphClassesHashMap.get(graphType.trim()));
                Obj = new String[13];
                Obj[0] = String.valueOf(graphDetails.get("graphName"));
                Obj[1] = String.valueOf(graphDetails.get("graphType"));

                Obj[2] = String.valueOf(graphDetails.get("graphClass"));
                Obj[3] = String.valueOf(graphDetails.get("graphHeight"));
                //Obj[4] = String.valueOf(graphDetails.get("graphWidth"));
                Obj[4] = "400";
                Obj[5] = "Y";


                //add more tage for KPI graph
                if (graphDetails.get("startRange") != null) {
                    Obj[6] = String.valueOf(graphDetails.get("startRange"));
                } else {
                    Obj[6] = "";
                }
                if (graphDetails.get("endRange") != null) {
                    Obj[7] = String.valueOf(graphDetails.get("endRange"));
                } else {
                    Obj[7] = "";
                }
                if (graphDetails.get("firstBreak") != null) {
                    Obj[8] = String.valueOf(graphDetails.get("firstBreak"));
                } else {
                    Obj[8] = "";
                }
                if (graphDetails.get("secondBreak") != null) {
                    Obj[9] = String.valueOf(graphDetails.get("secondBreak"));
                } else {
                    Obj[9] = "";
                }
                if (graphDetails.get("needleValue") != null) {
                    Obj[10] = String.valueOf(graphDetails.get("needleValue"));
                } else {
                    Obj[10] = "";
                }
                if (graphDetails.get("daytargetVal") != null) {
                    Obj[11] = String.valueOf(graphDetails.get("daytargetVal"));
                } else {
                    Obj[11] = "";
                }
                if (graphDetails.get("measureName") != null) {
                    Obj[12] = String.valueOf(graphDetails.get("measureName"));
                } else {
                    Obj[12] = "";
                }

                for (int j = 0; j < pgMasterChildTags.length; j++) {
                    child = new Element(pgMasterChildTags[j]);
                    child.setText(Obj[j]);
                    pgMasterEle.addContent(child);
                }
                rQueryEle.addContent(pgMasterEle);
                viewBys = (String[]) graphDetails.get("viewByElementIds");
                if (viewBys == null) {
//                            viewBys=new String[getParametersHashMap().get("RowViewByIds").toString()];
                    viewBys = REP_Elements.toString().split(",");
                }
                axis = (String[]) graphDetails.get("axis");
                if (axis == null) {
                    axis = new String[barChartColumnNames.size()];
                }
                for (int k = viewBys.length; k < barChartColumnNames.size(); k++) {
                    if (CEP_Elements.isEmpty() || getStatusForInsertInXMLDoc(barChartColumnNames.get(k), measures)) {
                        rqDetailsEle = new Element("rqDeatils");
                        Obj = new String[4];
                        Obj[0] = barChartColumnNames.get(k).replace("A_", "");
                        Obj[1] = barChartColumnTitles.get(k);
                        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                            aggTypeQuery = "select isnull(aggregation_type,'SUM' ) from prg_user_all_info_details where element_id in (" + Obj[0] + ")";
                        } else {
                            aggTypeQuery = "select nvl(aggregation_type,'SUM' ) from prg_user_all_info_details where element_id in (" + Obj[0] + ")";
                        }

                        grppbro = execSelectSQL(aggTypeQuery);
                        Obj[2] = grppbro.getFieldValueString(0, 0);

                        //Obj[2] = "SUM";

                        Obj[3] = axis[k - viewBys.length];

                        for (int l = 0; l < rqDetailsChildTags.length; l++) {
                            child = new Element(rqDetailsChildTags[l]);
                            child.setText(Obj[l]);
                            rqDetailsEle.addContent(child);
                        }
                        rQueryEle.addContent(rqDetailsEle);
                    }
                }

                rqMoreInfoEle = new Element("rqMoreInfo");
                Obj = new String[3];
                Obj[0] = barChartColumnNames.get(viewBys.length).replace("A_", "");
                Obj[1] = "ASC";
                Obj[2] = "10";

                for (int l = 0; l < rqMoreInfoChildTags.length; l++) {
                    child = new Element(rqMoreInfoChildTags[l]);
                    child.setText(Obj[l]);
                    rqMoreInfoEle.addContent(child);
                }
                rQueryEle.addContent(rqMoreInfoEle);


            }
        }
        root.addContent(rQueryEle);
        return root;
    }

    public void setPortLet(PortLet portLet) {
        this.portLet = portLet;
    }

    public void setParamsString(String paramsString) {
        this.paramsString = paramsString;
    }

    public void setParamString1(String paramString1) {
        this.paramString1 = paramString1;
    }

    public void setParamString2(String paramString2) {
        this.paramString2 = paramString2;
    }

    public void setREP_Elements(ArrayList<String> REP_Elements) {
        this.REP_Elements = REP_Elements;
    }

    public void setCEP_Elements(ArrayList<String> CEP_Elements) {
        this.CEP_Elements = CEP_Elements;
    }

    /**
     * @param graphType the graphType to set
     */
    public void setGraphType(String graphType) {
        this.graphType = graphType;
    }

    /**
     * @param GraphClassesHashMap the GraphClassesHashMap to set
     */
    public void setGraphClassesHashMap(HashMap GraphClassesHashMap) {
        this.GraphClassesHashMap = GraphClassesHashMap;
    }

    private boolean getStatusForInsertInXMLDoc(String barchartColumnName, ArrayList<String> measures) {
        boolean status = false;
        for (String measure : measures) {
            if (measure.replace("A_", "").equalsIgnoreCase(barchartColumnName.replace("A_", ""))) {
                status = true;
                break;
            }

        }
        return status;
    }
}
