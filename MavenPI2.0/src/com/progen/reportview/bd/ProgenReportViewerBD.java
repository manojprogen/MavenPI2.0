/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/*
 * Document    : ProgenReportViewerBD.java for Business logics and relocating the function of PbReportViewerBD.java
 * Created on  : 27 Jan, 2016, 11:00 AM
 * Author      : Mohit Gupta
 * Organization: Progen Business Solution
 */
package com.progen.reportview.bd;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.progen.report.*;
import com.progen.report.colorgroup.ColorCode;
import com.progen.report.colorgroup.ColorCodeBuilder;
import com.progen.report.colorgroup.ColorCodeRule;
import com.progen.report.colorgroup.ColorGroup;
import com.progen.report.data.DataFacade;
import com.progen.reportview.db.JsonGenerator;
import com.progen.reportview.db.PbReportViewerDAO;
import com.progen.reportview.db.ProgenReportViewerDAO;
import com.progen.scheduler.ReportSchedule;
import com.progen.xml.pbXmlUtilities;
import com.sun.org.apache.xerces.internal.parsers.DOMParser;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.StringReader;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;
import prg.db.Container;
import prg.db.ContainerConstants.SortOrder;
import prg.db.PbDb;
import prg.db.PbReturnObject;
import utils.db.ProgenConnection;
import utils.db.ProgenParam;

public class ProgenReportViewerBD extends PbDb {

    public static Logger logger = Logger.getLogger(ProgenReportViewerBD.class);
    PbReportViewerDAO reportViewerDAO = new PbReportViewerDAO();
    // Function of PbReportViewerBD.java relocated here
    //Added by amar to read snapshot

    public Container readSnapShotXMLFromFileSystem(Container container, String snapShotId, PbReportCollection collect, String filePath, String reportId2) throws Exception {
        SAXBuilder builder = new SAXBuilder();
        pbXmlUtilities xmUtil = new pbXmlUtilities();
        Document document = null;
        Element root = null;
//        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
//            String clob = reportViewerDAO.readSnapShotString(container, snapShotId);
//            DOMParser parser = new DOMParser();
//            document = builder.build(new StringReader(clob));
//        } else {
//            Clob clob = reportViewerDAO.readSnapShotXML(container, snapShotId);
//            document = builder.build(clob.getCharacterStream());
//        }
        // This code  added by amar to read snapshot from FileSystem
        //filePath = filePath.replace("\\","/");
        FileInputStream fileIn = new FileInputStream(filePath);
        ObjectInputStream in = new ObjectInputStream(fileIn);
        String clob = in.readObject().toString();
        DOMParser parser = new DOMParser();
        document = builder.build(new StringReader(clob));
        in.close();
        fileIn.close();
        //end of code by Amar
        ReportSchedule schedule = reportViewerDAO.getSchedulerDetails(snapShotId);
        ProgenParam pParam = new ProgenParam();
        //document = builder.build(clob.getCharacterStream());
        root = document.getRootElement();
        Element schedRepVersionElem = root.getChild("Report_Version");
        double schedRepVersion = schedRepVersionElem != null ? Double.parseDouble(schedRepVersionElem.getValue()) : 1.0;
        HashMap GraphHashMap = container.getGraphHashMap();
        HashMap TableHashMap = container.getTableHashMap();
        HashMap ParametersHashMap = container.getParametersHashMap();
        int viewByCount = (container.getViewByCount());
        ArrayList displayColumns = new ArrayList();
        ArrayList displayLabels = new ArrayList();
        ArrayList originalColumns = new ArrayList();
        ArrayList displayTypes = new ArrayList();
        ArrayList dataTypes = new ArrayList();
        ArrayList links = new ArrayList();
        ArrayList signs = new ArrayList();
        ArrayList reportQueryAggregations = new ArrayList();
        ArrayList reportQryElementIds = new ArrayList();
        ArrayList reportQueryColumnNames = new ArrayList();
        ArrayList Measures = new ArrayList();
        ArrayList MeasureNames = new ArrayList();
        LinkedHashMap<String, String> grphMeasMap;// = HashBiMap.create();
        if (schedule == null) {// only for snapshot code
            //building graph
            if (root.getChild("graphs") != null) {
                List graphs = root.getChildren("graphs");//Only one row as of now
                //for (int i = 0; i < graphs.size(); i++) {
                Element graph = (Element) graphs.get(0);// as we know there are only one graphs tag
                List graphList = graph.getChildren("graph");
                StringBuffer AllGraphIds = new StringBuffer("");
                ArrayList AllGraphColumns = new ArrayList();
                HashMap[] graphMapDetails = new HashMap[graphList.size()];
                for (int j = 0; j < graphList.size(); j++) {
                    grphMeasMap = new LinkedHashMap<String, String>();
                    graphMapDetails[j] = new HashMap();
                    Element graphEle = (Element) graphList.get(j);
                    String graphId = xmUtil.getXmlTagValue(graphEle, "graphId");
                    AllGraphIds.append("," + graphId);
                    graphMapDetails[j].put("graphId", xmUtil.getXmlTagValue(graphEle, "graphId"));
                    graphMapDetails[j].put("graphName", xmUtil.getXmlTagValue(graphEle, "graphName"));
                    graphMapDetails[j].put("graphWidth", xmUtil.getXmlTagValue(graphEle, "graphWidth"));
                    graphMapDetails[j].put("graphHeight", xmUtil.getXmlTagValue(graphEle, "graphHeight"));
                    graphMapDetails[j].put("graphClassName", xmUtil.getXmlTagValue(graphEle, "graphClassName"));
                    graphMapDetails[j].put("graphTypeName", xmUtil.getXmlTagValue(graphEle, "graphTypeName"));
                    graphMapDetails[j].put("graphSize", xmUtil.getXmlTagValue(graphEle, "graphSize"));
                    graphMapDetails[j].put("SwapColumn", xmUtil.getXmlTagValue(graphEle, "SwapColumn"));
                    graphMapDetails[j].put("graphLegend", xmUtil.getXmlTagValue(graphEle, "graphLegend"));
                    graphMapDetails[j].put("graphLegendLoc", xmUtil.getXmlTagValue(graphEle, "graphLegendLoc"));
                    graphMapDetails[j].put("graphshowX", xmUtil.getXmlTagValue(graphEle, "graphshowX"));
                    graphMapDetails[j].put("graphshowY", xmUtil.getXmlTagValue(graphEle, "graphshowY"));
                    graphMapDetails[j].put("graphLYaxislabel", xmUtil.getXmlTagValue(graphEle, "graphLYaxislabel"));
                    graphMapDetails[j].put("graphRYaxislabel", xmUtil.getXmlTagValue(graphEle, "graphRYaxislabel"));
                    graphMapDetails[j].put("graphDrill", xmUtil.getXmlTagValue(graphEle, "graphDrill"));
                    graphMapDetails[j].put("graphBcolor", xmUtil.getXmlTagValue(graphEle, "graphBcolor"));
                    graphMapDetails[j].put("graphFcolor", xmUtil.getXmlTagValue(graphEle, "graphFcolor"));
                    graphMapDetails[j].put("graphData", xmUtil.getXmlTagValue(graphEle, "graphData"));
                    graphMapDetails[j].put("showGT", xmUtil.getXmlTagValue(graphEle, "showGT"));
                    graphMapDetails[j].put("measureNamePosition", xmUtil.getXmlTagValue(graphEle, "measureNamePosition"));
                    ArrayList viewByElementIds = new ArrayList();
                    Set<String> measEleSet = new LinkedHashSet<String>();
                    Set<String> measNameSet = new LinkedHashSet<String>();
                    //need to modify this reading by using arraylist
                    if (graphEle.getChild("viewByElementIds") != null) {
                        List viewByElementIdsList = ((Element) graphEle.getChildren("viewByElementIds").get(0)).getChildren("viewByElementId");

                        for (int k = 0; k < viewByElementIdsList.size(); k++) {
                            viewByElementIds.add(((Element) viewByElementIdsList.get(k)).getText());
                        }
                        graphMapDetails[j].put("viewByElementIds", (String[]) viewByElementIds.toArray(new String[0]));
                    }
                    if (graphEle.getChild("barChartColumnNames") != null) {
                        List barChartColumnNameList = ((Element) graphEle.getChildren("barChartColumnNames").get(0)).getChildren("barChartColumnName");
                        ArrayList barChartColumnNames = new ArrayList();
                        for (int k = 0; k < barChartColumnNameList.size(); k++) {
                            barChartColumnNames.add(((Element) barChartColumnNameList.get(k)).getText());
                            if (k >= viewByCount) {
                                if (!AllGraphColumns.contains(((Element) barChartColumnNameList.get(k)).getText())) {
                                    AllGraphColumns.add(((Element) barChartColumnNameList.get(k)).getText());
                                }
                                measEleSet.add(((Element) barChartColumnNameList.get(k)).getText());
                            }
                        }
                        graphMapDetails[j].put("barChartColumnNames", (String[]) barChartColumnNames.toArray(new String[0]));
                        graphMapDetails[j].put("pieChartColumns", (String[]) barChartColumnNames.toArray(new String[0]));
                    }
                    if (graphEle.getChild("barChartColumnTitles") != null) {
                        List barChartColumnTitleList = ((Element) graphEle.getChildren("barChartColumnTitles").get(0)).getChildren("barChartColumnTitle");
                        ArrayList barChartColumnTitles = new ArrayList();
                        for (int k = 0; k < barChartColumnTitleList.size(); k++) {
                            barChartColumnTitles.add(((Element) barChartColumnTitleList.get(k)).getText());
                            if (k >= viewByCount) {
                                measNameSet.add(((Element) barChartColumnTitleList.get(k)).getText());
                            }
                        }
                        graphMapDetails[j].put("barChartColumnTitles", (String[]) barChartColumnTitles.toArray(new String[0]));
                    }

                    if (graphEle.getChild("barChartColumnNames1") != null) {
                        List barChartColumnName1List = ((Element) graphEle.getChildren("barChartColumnNames1").get(0)).getChildren("barChartColumnName1");
                        ArrayList barChartColumnNames1 = new ArrayList();
                        for (int k = 0; k < barChartColumnName1List.size(); k++) {
                            barChartColumnNames1.add(((Element) barChartColumnName1List.get(k)).getText());
                        }
                        graphMapDetails[j].put("barChartColumnNames1", (String[]) barChartColumnNames1.toArray(new String[0]));
                    }
                    if (graphEle.getChild("barChartColumnTitles1") != null) {
                        List barChartColumnTitle1List = ((Element) graphEle.getChildren("barChartColumnTitles1").get(0)).getChildren("barChartColumnTitle1");
                        ArrayList barChartColumnTitles1 = new ArrayList();
                        for (int k = 0; k < barChartColumnTitle1List.size(); k++) {
                            barChartColumnTitles1.add(((Element) barChartColumnTitle1List.get(k)).getText());
                        }
                        graphMapDetails[j].put("barChartColumnTitles1", (String[]) barChartColumnTitles1.toArray(new String[0]));
                    }
                    if (graphEle.getChild("barChartColumnNames2") != null) {
                        List barChartColumnName2List = ((Element) graphEle.getChildren("barChartColumnNames2").get(0)).getChildren("barChartColumnName2");
                        ArrayList barChartColumnNames2 = new ArrayList();
                        for (int k = 0; k < barChartColumnName2List.size(); k++) {
                            barChartColumnNames2.add(((Element) barChartColumnName2List.get(k)).getText());
                        }

                        graphMapDetails[j].put("barChartColumnNames2", (String[]) barChartColumnNames2.toArray(new String[0]));
                    }
                    if (graphEle.getChild("barChartColumnTitles2") != null) {
                        List barChartColumnTitle2List = ((Element) graphEle.getChildren("barChartColumnTitles2").get(0)).getChildren("barChartColumnTitle2");
                        ArrayList barChartColumnTitles2 = new ArrayList();
                        for (int k = 0; k < barChartColumnTitle2List.size(); k++) {
                            barChartColumnTitles2.add(((Element) barChartColumnTitle2List.get(k)).getText());
                        }

                        graphMapDetails[j].put("barChartColumnTitles2", (String[]) barChartColumnTitles2.toArray(new String[0]));
                    }

                    if (graphEle.getChild("axiss") != null) {
                        List axisList = ((Element) graphEle.getChildren("axiss").get(0)).getChildren("axis");
                        ArrayList axis = new ArrayList();
                        for (int k = 0; k < axisList.size(); k++) {
                            axis.add(((Element) axisList.get(k)).getText());
                        }
                        graphMapDetails[j].put("axis", (String[]) axis.toArray(new String[0]));
                    }

                    if (graphEle.getChild("RowValuesList") != null) {
                        List RowValues = ((Element) graphEle.getChildren("RowValuesList").get(0)).getChildren("RowValues");
                        ArrayList RowValuesList = new ArrayList();

                        for (int k = 0; k < RowValues.size(); k++) {
                            RowValuesList.add(((Element) RowValues.get(k)).getText());
                        }
                        graphMapDetails[j].put("RowValuesList", RowValuesList);
                    }
                    Iterator<String> measNameIter = measNameSet.iterator();
                    for (String measEle : measEleSet) {
                        grphMeasMap.put(measEle, measNameIter.next());
                    }
                    graphMapDetails[j].put("graphMeasures", grphMeasMap);

                    GraphHashMap.put(graphId, graphMapDetails[j]);
                }
                GraphHashMap.put("AllGraphColumns", AllGraphColumns);
                if (AllGraphIds.toString().equalsIgnoreCase("")) {
                    GraphHashMap.put("graphIds", AllGraphIds.toString());
                } else {
                    GraphHashMap.put("graphIds", AllGraphIds.substring(1));
                }
                //}
            }
        }
        if (root.getChild("progen_viewbys") != null) {
            List viewByLst = root.getChildren("progen_viewbys");
            Element viewByElements = null;
            List rowEdgeParamList = null;
            List colEdgeParamList = null;
            Element repIdEle = null;
            List list;
            Element ele;
            ArrayList<String> rowViewByLst = new ArrayList<String>();
            ArrayList<String> colViewByLst = new ArrayList<String>();
            for (int i = 0; i < viewByLst.size(); i++) {
                viewByElements = (Element) viewByLst.get(i);
                rowEdgeParamList = viewByElements.getChildren("REP");
                repIdEle = (Element) rowEdgeParamList.get(0);
                list = repIdEle.getChildren("REP_id");
                for (int j = 0; j < list.size(); j++) {
                    repIdEle = (Element) list.get(j);
                    rowViewByLst.add(repIdEle.getText());
                }
                //added by @swathi
                if (viewByElements.getChildren("CEP") != null && !viewByElements.getChildren("CEP").isEmpty()) {
                    colEdgeParamList = viewByElements.getChildren("CEP");
                    Element cepIdEle = (Element) colEdgeParamList.get(0);
                    List ceplist = cepIdEle.getChildren("CEP_id");

                    for (int j = 0; j < ceplist.size(); j++) {
                        cepIdEle = (Element) ceplist.get(j);
                        colViewByLst.add(cepIdEle.getText());
                    }
                }
            }
            collect.reportRowViewbyValues = rowViewByLst;
            collect.reportColViewbyValues = colViewByLst;
            container.setColumnViewByCount(String.valueOf(collect.reportColViewbyValues.size()));
        }
        HashMap<String, ArrayList<String>> timeDSMap = new HashMap<String, ArrayList<String>>();
        ArrayList timeDsArray = new ArrayList();
//        System.out.println("collect.timeDetailsMap******"+collect.timeDetailsMap);
//        System.out.println("collect.timeDetailsArray"+collect.timeDetailsArray);
        timeDSMap = collect.timeDetailsMap;
        timeDsArray = collect.timeDetailsArray;
        Gson gson = new Gson();
        Type tarType = new TypeToken<List<String>>() {
        }.getType();
        if (root.getChild("progen_parameter") != null) {
            List progenParamLst = root.getChildren("progen_parameter");
            Element viewEle = null;
            List urlList = null;
            Element urlEle = null;
            Element progenParamEle = (Element) progenParamLst.get(0);
            List paramMasterLst = progenParamEle.getChildren("Parameter_Master");
            Element paramMasterEle = (Element) paramMasterLst.get(0);
            List paramDetailLst = paramMasterEle.getChildren("Parameter_Detail");
            Element paramDetailEle;
            ArrayList<String> values;
            String elementId;
            String elementName = "";
            Map<String, List> inMap = new HashMap<String, List>();
            Map<String, List> notInMap = new HashMap<String, List>();
            Map<String, List> likeMap = new HashMap<String, List>();
            Map<String, List> notLikeMap = new HashMap<String, List>();
            for (int i = 0; i < paramDetailLst.size(); i++) {
                paramDetailEle = (Element) paramDetailLst.get(i);
                values = new ArrayList<String>();
                elementId = xmUtil.getXmlTagValue(paramDetailEle, "element_id");
                values.add(xmUtil.getXmlTagValue(paramDetailEle, "defaultValue"));
                elementName = xmUtil.getXmlTagValue(paramDetailEle, "paramDispName");
                if (!"Time".equalsIgnoreCase(elementId)) {
                    List<String> defaulValList = null;
                    try {
                        defaulValList = gson.fromJson(xmUtil.getXmlTagValue(paramDetailEle, "defaultValue"), tarType);
                    } catch (com.google.gson.JsonParseException e) {
                        String normalString = xmUtil.getXmlTagValue(paramDetailEle, "defaultValue");
                        String[] splitedStr = normalString.split(",");
                        defaulValList = Arrays.asList(splitedStr);
                    }
//                    collect. setParameters(elementId,  elementName, values);
                    collect.reportIncomingParameters.put("CBOARP" + xmUtil.getXmlTagValue(paramDetailEle, "element_id"), defaulValList);
                    collect.reportParametersValues.put(xmUtil.getXmlTagValue(paramDetailEle, "element_id"), defaulValList); //xmUtil.getXmlTagValue(paramDetailEle, "defaultValue")
                    ArrayList parameter = new ArrayList();
                    //parameter.addAll(Arrays.asList(xmUtil.getXmlTagValue(paramDetailEle, "reportParameters").split(",")));
                    String[] param = xmUtil.getXmlTagValue(paramDetailEle, "reportParameters").split(",");
                    for (int j = 0; j < 8; j++) {
                        if (j < param.length) {
                            parameter.add(param[j]);
                        }
                    }
                    parameter.add(defaulValList);
                    parameter.add("CBOARP" + xmUtil.getXmlTagValue(paramDetailEle, "element_id"));
                    String filterType = param[param.length - 1].trim();
                    String elemId = xmUtil.getXmlTagValue(paramDetailEle, "element_id");
                    if (schedRepVersion < 1.1 && progenParamEle.getChild("NOTIN") == null
                            && progenParamEle.getChild("LIKE") == null
                            && progenParamEle.getChild("NOTLIKE") == null) {
                        if (filterType.equalsIgnoreCase("INCLUDED") || filterType.equalsIgnoreCase("NOT_SELECTED")
                                || filterType.equalsIgnoreCase("IN")) {
                            inMap.put(elemId, defaulValList);
                        }
                        if (filterType.equalsIgnoreCase("EXCLUDED")
                                || filterType.equalsIgnoreCase("NOT IN")
                                || filterType.equalsIgnoreCase("NOTIN")) {
                            notInMap.put(elemId, defaulValList);
                        }
                        if (filterType.equalsIgnoreCase("LIKE")) {
                            likeMap.put(elemId, defaulValList);

                        }
                        if (filterType.equalsIgnoreCase("NOT LIKE") || filterType.equalsIgnoreCase("NOTLIKE")) {
                            notLikeMap.put(elemId, defaulValList);
                        }
                    }
                    parameter.add(param[param.length - 1].trim());
                    collect.reportParameters.put(xmUtil.getXmlTagValue(paramDetailEle, "element_id"), parameter);
                }
            }
            collect.updateCollection(false);

            /*
             * added by srikanth.p
             */
            Type mapTarType = new TypeToken<Map<String, List<String>>>() {
            }.getType();
            if (schedRepVersion >= 1.1 || (progenParamEle.getChild("IN") != null
                    && progenParamEle.getChild("NOTIN") != null
                    && progenParamEle.getChild("LIKE") != null
                    && progenParamEle.getChild("NOTLIKE") != null)) {
                Element inElem = progenParamEle.getChild("IN");
                String jsonStr = inElem.getValue();
                inMap = gson.fromJson(jsonStr, mapTarType);
                Element notInElem = progenParamEle.getChild("NOTIN");
                jsonStr = notInElem.getValue();
                notInMap = gson.fromJson(jsonStr, mapTarType);
                Element likeElem = progenParamEle.getChild("LIKE");
                jsonStr = likeElem.getValue();
                likeMap = gson.fromJson(jsonStr, mapTarType);
                Element NotLikeElem = progenParamEle.getChild("NOTLIKE");
                jsonStr = NotLikeElem.getValue();
                notLikeMap = gson.fromJson(jsonStr, mapTarType);
            }
//            HashMap<String,HashMap<String,List>> operatorFilters=new HashMap<String,HashMap<String,List>>();
            collect.operatorFilters.put("IN", (HashMap<String, List>) inMap);
            collect.operatorFilters.put("NOTIN", (HashMap<String, List>) notInMap);
            collect.operatorFilters.put("LIKE", (HashMap<String, List>) likeMap);
            collect.operatorFilters.put("NOTLIKE", (HashMap<String, List>) notLikeMap);
//            collect.operatorFilters=operatorFilters;
        }
        if (root.getChild("progen_table") != null) {
            List tables = root.getChildren("progen_table");//Only one row as of now
            HashMap ReportHashMap = container.getReportHashMap();
            Element tableEle = (Element) tables.get(0);
            container.setNetTotalReq(Boolean.parseBoolean(xmUtil.getXmlTagValue(tableEle, "show_sub_total")));
            container.setGrandTotalReq(Boolean.parseBoolean(xmUtil.getXmlTagValue(tableEle, "show_grd_total")));
            container.setAvgTotalReq(Boolean.parseBoolean(xmUtil.getXmlTagValue(tableEle, "show_avg")));
            container.setCatMaxValueReq(Boolean.parseBoolean(xmUtil.getXmlTagValue(tableEle, "show_cat_max")));
            container.setCatMinValueReq(Boolean.parseBoolean(xmUtil.getXmlTagValue(tableEle, "show_cat_min")));
            container.setOverAllMaxValueReq(Boolean.parseBoolean(xmUtil.getXmlTagValue(tableEle, "show_max")));
            container.setOverAllMinValueReq(Boolean.parseBoolean(xmUtil.getXmlTagValue(tableEle, "show_min")));
            container.setHideTable(Boolean.parseBoolean(xmUtil.getXmlTagValue(tableEle, "is_hide_table")));
            container.setHideMsrHeading(Boolean.parseBoolean(xmUtil.getXmlTagValue(tableEle, "Is_hide_msr_Heading")));//by ram
            //Added by Amar on Sep 23, 2015
            try {
                container.setCrosstabGrandTotalDisplayPosition(xmUtil.getXmlTagValue(tableEle, "CRGT_POS"));
            } catch (Exception ep) {
            }
            //end of code
//added by Dinanath for color group with  excel template schedular download
            try {
                //if (schedule == null) {

                if (tableEle.getChild("ColorGroup") != null) {
                    List colorGroupingList = tableEle.getChildren("ColorGroup");
                    Element colorGroupEle = (Element) colorGroupingList.get(0);
                    List groupList = colorGroupEle.getChildren("Group");
                    Element groupEle = null;
                    List measLst = null;
                    Element measEle = null;
                    List crossMeasLst = null;
                    Element crossMeasEle = null;
                    String measure;
                    String crossTabMes;
                    List colorCodeRulesLst = null;
                    Element colorCodeRulesEle = null;
                    Document colorDocument = null;
                    List repParamLst = null;
                    Element repParamEle = null;
                    String colorCodes[] = null;
                    String operators[] = null;
                    String stValues[] = null;
                    String endValues[] = null;
                    XMLOutputter outPutter = new XMLOutputter();

                    ColorCodeBuilder colorBuilder = new ColorCodeBuilder();
                    HashMap colorMap = new HashMap();
                    String colorXml;
                    Document paramDoc = null;
                    String paramXml;
                    repParamLst = colorGroupEle.getChildren("ReportParameters");
                    if (repParamLst != null && repParamLst.size() > 0) {
                        repParamEle = (Element) repParamLst.get(0);

                        LinkedHashMap<String, String> paramMap = this.parseReportParamXML(repParamEle);

                        ReportParameter repParam = new ReportParameter();
                        ColorGroup colorGroup = container.getColorGroup();
                        repParam.setReportParameters1(paramMap);
                        ColorCode colorCode;

                        List rowViewBysLst = null;
                        Element rowViewBysEle = null;
                        List rowViews = null;
                        Element rowViewEle = null;

                        List colViews = null;
                        Element colViewEle = null;

                        List colViewBysLst = null;
                        Element colViewBysEle = null;

                        ArrayList<String> rowLst = new ArrayList<String>();
                        ArrayList<String> colLst = new ArrayList<String>();

                        rowViewBysLst = colorGroupEle.getChildren("RowViewBys");
                        rowViewBysEle = (Element) rowViewBysLst.get(0);
                        rowViews = rowViewBysEle.getChildren("RowViewBy");
                        for (int r = 0; r < rowViews.size(); r++) {
                            rowViewEle = (Element) rowViews.get(r);
                            rowLst.add(rowViewEle.getText());
                        }
                        colViewBysLst = colorGroupEle.getChildren("ColViewBys");
                        colViewBysEle = (Element) colViewBysLst.get(0);
                        colViews = colViewBysEle.getChildren("ColViewBy");
                        for (int c = 0; c < colViews.size(); c++) {
                            colViewEle = (Element) colViews.get(c);
                            colLst.add(rowViewEle.getText());
                        }

                        repParam.setViewBys(rowLst, colLst);

                        for (int l = 0; l < groupList.size(); l++) {
                            groupEle = (Element) groupList.get(l);
                            measLst = groupEle.getChildren("Measure");
                            measEle = (Element) measLst.get(0);
                            measure = measEle.getText();
                            crossMeasLst = groupEle.getChildren("CrossTabMeasure");
                            crossMeasEle = (Element) crossMeasLst.get(0);
                            crossTabMes = crossMeasEle.getText();
                            colorCodeRulesLst = groupEle.getChildren("ColorCodeRules");
                            colorMap = this.parseColorCodeXML((Element) colorCodeRulesLst.get(0));
//           String Rowviewby=colorCodeRulesLst.get(0).viewBy;
                            //added by Dinanath for color group
                            ArrayList<String> a1 = container.getReportCollect().reportRowViewbyValues;
                            StringBuffer ViewbyColumnBf = new StringBuffer();
                            for (int c = 0; c < a1.size(); c++) {
                                ViewbyColumnBf.append(a1.get(c));
                            }
                            String ViewbyColumn = ViewbyColumnBf.toString();
                            colorCode = colorGroup.createColorCode(measure, repParam, crossTabMes, ViewbyColumn);
                            if (colorMap != null && colorMap.size() > 0) {
                                colorCodes = (String[]) colorMap.get("colorCodes");
                                operators = (String[]) colorMap.get("operators");
                                stValues = (String[]) colorMap.get("sValues");
                                endValues = (String[]) colorMap.get("eValues");
                                ArrayList<ColorCodeRule> colorCodeRuleLst = ColorCodeBuilder.buildColorCode(Arrays.asList(colorCodes), Arrays.asList(operators), Arrays.asList(stValues), Arrays.asList(endValues), 1, ViewbyColumn, true);

                                colorCode.setColorCodeRule(colorCodeRuleLst);
                            }
                            // colorGroup.addRuleToColorCode(colorCode, colorMap);
                        }
                        //added by Dinanath for color gorup
//            PbReportViewerBD pb=new PbReportViewerBD();
//            String reportIdForColor=pb.getReportId1();
                        ColorCodeBuilder ccb = new ColorCodeBuilder();
                        ColorGroup colorCodeRuleLst1 = ccb.buildColorCode(reportId2, container);
                        //endded by Dinanath
                        container.setColorGroup(colorGroup);
                        repParam.addObserver(colorGroup);
                        repParam.notifyObserversOfUpdate(container.getNoOfDays());
                    }
                }

                // }
            } catch (Exception e) {
                logger.error("Exception: ", e);
            }
//endded by Dinanath
            if (tableEle.getChild("display_columns") != null) {
                List display_columnsList = ((Element) tableEle.getChildren("display_columns").get(0)).getChildren("display_column");
                for (int k = 0; k < display_columnsList.size(); k++) {
                    displayColumns.add(((Element) display_columnsList.get(k)).getText());
                }
                container.setDisplayColumns(displayColumns);
            }
            // code written by swathi
            if (tableEle.getChild("display_labels") != null) {
                List display_labelsList = ((Element) tableEle.getChildren("display_labels").get(0)).getChildren("display_label");
                int dimCount = viewByCount;
                if (!collect.reportColViewbyValues.isEmpty() && collect.reportColViewbyValues != null) {
                    for (int k = 0; k < dimCount; k++) {
                        displayLabels.add(((Element) display_labelsList.get(k)).getText());
                    }
                    for (int k = dimCount; k < display_labelsList.size(); k++) {
                        ArrayList al = new ArrayList();
                        al.add(((Element) display_labelsList.get(k)).getText().replace("[", "").replace("]", ""));
                        displayLabels.add(al);
                    }
                } else {
                    for (int k = 0; k < display_labelsList.size(); k++) {
                        displayLabels.add(((Element) display_labelsList.get(k)).getText());
                    }
                }
                container.setDisplayLabels(displayLabels);
            }
            if (tableEle.getChild("original_columns") != null) {

                List original_columnsList = ((Element) tableEle.getChildren("original_columns").get(0)).getChildren("original_column");

                for (int k = 0; k < original_columnsList.size(); k++) {
                    originalColumns.add(((Element) original_columnsList.get(k)).getText());
                }
                container.setOriginalColumns(originalColumns);
            }
            if (tableEle.getChild("display_types") != null) {

                List display_typesList = ((Element) tableEle.getChildren("display_types").get(0)).getChildren("display_type");

                for (int k = 0; k < display_typesList.size(); k++) {
                    displayTypes.add(((Element) display_typesList.get(k)).getText());
                }
                container.setDisplayTypes(displayTypes);
            }
            if (tableEle.getChild("data_types") != null) {

                List data_typesList = ((Element) tableEle.getChildren("data_types").get(0)).getChildren("data_type");

                for (int k = 0; k < data_typesList.size(); k++) {
                    dataTypes.add(((Element) data_typesList.get(k)).getText());
                }
                container.setDataTypes(dataTypes);
            }

            if (tableEle.getChild("links") != null) {
                List linksList = ((Element) tableEle.getChildren("links").get(0)).getChildren("link");

                for (int k = 0; k < linksList.size(); k++) {
                    links.add(((Element) linksList.get(k)).getText());
                }
                container.setLinks(links);

            }
            if (tableEle.getChild("report_query_elementsIds") != null) {
                List report_query_elementsList = ((Element) tableEle.getChildren("report_query_elementsIds").get(0)).getChildren("report_query_elementsId");

                for (int k = 0; k < report_query_elementsList.size(); k++) {
                    reportQryElementIds.add(((Element) report_query_elementsList.get(k)).getText().trim());
                }
                ReportHashMap.put("reportQryElementIds", reportQryElementIds);
                container.setTableMeasure(reportQryElementIds);
            }
            if (tableEle.getChild("report_query_aggregations") != null) {
                List report_query_aggregationsList = ((Element) tableEle.getChildren("report_query_aggregations").get(0)).getChildren("report_query_aggregation");

                for (int k = 0; k < report_query_aggregationsList.size(); k++) {
                    reportQueryAggregations.add(((Element) report_query_aggregationsList.get(k)).getText());
                }
                ReportHashMap.put("reportQryAggregations", reportQueryAggregations);
            }
            if (tableEle.getChild("report_query_colnames") != null) {
                //((Element) tableEle.getChildren("report_query_colnames").get(0)).getAttributeValue("");
                //
                List report_query_colnamesList = ((Element) tableEle.getChildren("report_query_colnames").get(0)).getChildren("report_query_colname");

                for (int k = 0; k < report_query_colnamesList.size(); k++) {
                    reportQueryColumnNames.add(((Element) report_query_colnamesList.get(k)).getText());
                }
                ReportHashMap.put("reportQryColNames", reportQueryColumnNames);
                container.setTableMeasureNames(reportQueryColumnNames);
            }
            //code written by swati purpose of tablefilters
            if (tableEle.getChild("TopBottom") != null) {
                List TopBottomList = null;
                List TopBottomTypeList = null;
                Element TopBottomEle = null;
                TopBottomList = tableEle.getChildren("TopBottom");
                TopBottomEle = (Element) TopBottomList.get(0);
                TopBottomTypeList = TopBottomEle.getChildren("TopBottomType");
                if (TopBottomTypeList != null && TopBottomTypeList.size() > 0) {
                    Element TopBottomTypeEle = (Element) TopBottomTypeList.get(0);
                    List TopBottomModeList = TopBottomEle.getChildren("TopBottomMode");
                    Element TopBottomModeEle = (Element) TopBottomModeList.get(0);
                    List NoOfRowsList = TopBottomEle.getChildren("NoOfRows");
                    Element NoOfRowsEle = (Element) NoOfRowsList.get(0);
                    List MeasColumnList = TopBottomEle.getChildren("MeasColumn");
                    Element MeasColumnEle = (Element) MeasColumnList.get(0);
                    container.setTopBottomColumn(TopBottomTypeEle.getText(), TopBottomModeEle.getText(), Integer.parseInt(NoOfRowsEle.getText()));
                    // 
                    if (TopBottomTypeEle.getText().equalsIgnoreCase("TopRows")) {
                        container.setSortColumn(MeasColumnEle.getText(), SortOrder.DESCENDING);
                        container.setSortColumn(MeasColumnEle.getText(), "1");
                        container.setSortColumnTopBottom(MeasColumnEle.getText(), "1");
                    } else {
                        container.setSortColumn(MeasColumnEle.getText(), SortOrder.ASCENDING);
                        container.setSortColumn(MeasColumnEle.getText(), "0");
                        container.setSortColumnTopBottom(MeasColumnEle.getText(), "0");
                    }
                }
            }
            if (tableEle.getChild("TableSearch") != null) {
                ArrayList<String> searchColumns = new ArrayList<String>();
                ArrayList<String> searchCondition = new ArrayList<String>();
                ArrayList<Object> searchValue = new ArrayList<Object>();
                List SearchList = null;
                SearchList = ((Element) tableEle.getChildren("TableSearch").get(0)).getChildren("Search");
                for (int i = 0; i < SearchList.size(); i++) {
                    Element SrchColEle = (Element) SearchList.get(i);
                    String searchCol = ((Element) SrchColEle.getChildren("Column").get(0)).getText();
                    String searchCond = ((Element) SrchColEle.getChildren("Condition").get(0)).getText();
                    String searchVal = ((Element) SrchColEle.getChildren("Value").get(0)).getText();
                    searchColumns.add(searchCol);
                    searchCondition.add(searchCond);
                    searchValue.add(searchVal);
//                   
//                   
                }
                if (!searchColumns.isEmpty()) {
                    int index = 0;
                    for (String srchcolumn : searchColumns) {
                        container.setSearchColumn(srchcolumn, searchCondition.get(index), searchValue.get(index).toString(), null);
                        index++;
                    }
                }
            }
            if (tableEle.getChild("Sort") != null) {
                List SortList = null;
                SortList = ((Element) tableEle.getChildren("Sort").get(0)).getChildren("SortColumn");
                HashSet<String> sortColumns = new HashSet<String>();
                ArrayList<String> sortTypes = new ArrayList<String>();
                for (int i = 0; i < SortList.size(); i++) {
                    Element SortEle = (Element) SortList.get(i);
                    String sortColumn = ((Element) SortEle.getChildren("ColumnName").get(0)).getText();
                    String sortType = ((Element) SortEle.getChildren("SortType").get(0)).getText();
                    sortColumns.add(sortColumn);
                    sortTypes.add(sortType);
                }
                if (!sortColumns.isEmpty()) {
                    int index = 0;
                    for (String sortColumn : sortColumns) {
                        container.setSortColumn(sortColumn, sortTypes.get(index));
                        index++;
                    }
                }
            }
            //added by santhosh.k on 10-03-2010 for Parameter Grouping
            if (tableEle.getChild("Parameter-Groupings") != null) {
                HashMap ParameterGroupingMap = new HashMap();
                List ParameterGroupings = tableEle.getChildren("Parameter-Groupings");

                if (ParameterGroupings != null && ParameterGroupings.size() != 0) {
                    for (int l = 0; l < ParameterGroupings.size(); l++) {//ofcourse we know there wil only one Parameter-Groupings tags
                        Element ParameterGroupsEle = (Element) ParameterGroupings.get(0);
                        List ParameterGroupingList = ParameterGroupsEle.getChildren("Parameter-Grouping");

                        for (int m = 0; m < ParameterGroupingList.size(); m++) {
                            HashMap SingleParameterGroupingMap = new HashMap();//for each <Parameter-Grouping> corresponds to a HashMap
                            Element ParameterGroupingEle = (Element) ParameterGroupingList.get(m);
                            List ParameterGroupList = ParameterGroupingEle.getChildren("Parameter-Group");
                            for (int n = 0; n < ParameterGroupList.size(); n++) {
                                Element ParameterGroupEle = (Element) ParameterGroupList.get(n);
                                SingleParameterGroupingMap.put(ParameterGroupEle.getAttribute("name").getValue(), ParameterGroupEle.getText());
                            }
                            ParameterGroupingMap.put(ParameterGroupingEle.getAttribute("elementId").getValue(), SingleParameterGroupingMap);
                            ParameterGroupingMap.put(ParameterGroupingEle.getAttribute("elementId").getValue() + "_GroupName", ParameterGroupingEle.getAttribute("groupName").getValue());

                        }
                    }
                }
                container.setParameterGroupAnalysisHashMap(ParameterGroupingMap);
            }
            //end of code added for Parameter grouping
            // Code Added by amar to get Hybrid report Details
            if (tableEle.getChild("HybridReport") != null) {
                List hybridElement = tableEle.getChildren("HybridReport");
                Element isSumEnable = (Element) hybridElement.get(0);
                Element isHybEnable = (Element) isSumEnable.getContent().get(0);
                if (isHybEnable.getContent(0).getValue().equalsIgnoreCase("true")) {
                    String isSummEnable = isHybEnable.getContent(0).getValue();
                    container.setSummarizedMeasuresEnabled(Boolean.valueOf(isSummEnable));
                    Element isHybHashMap = (Element) isSumEnable.getContent().get(1);
                    Type mapTarType = new TypeToken<Map<String, List<String>>>() {
                    }.getType();
                    if (isHybHashMap.getContent(0).getValue() != null) {
                        String hsMap = isHybHashMap.getContent(0).getValue();
                        String hMap = hsMap.replace("=", ":");
                        HashMap<String, ArrayList<String>> hybTableHashMap = new HashMap<String, ArrayList<String>>();
                        hybTableHashMap = gson.fromJson(hMap, mapTarType);
                        container.setSummerizedTableHashMap(hybTableHashMap);
                    }
                } else {
                    String isSuEnable = isHybEnable.getContent(0).getValue();
                    container.setSummarizedMeasuresEnabled(Boolean.valueOf(isSuEnable));
                }
            }
            // end of code
            if (Integer.parseInt(container.getColumnViewByCount()) == 0) {//not cross tab report
                for (int m = viewByCount; m < displayColumns.size(); m++) {
                    Measures.add(String.valueOf(displayColumns.get(m)));
                    MeasureNames.add(displayLabels.get(m));
                }
                TableHashMap.put("Measures", Measures);
                TableHashMap.put("MeasuresNames", MeasureNames);
            } else {
                for (int m = 0; m < reportQryElementIds.size(); m++) {
                    Measures.add(String.valueOf(reportQryElementIds.get(m)));
                }
                TableHashMap.put("Measures", Measures);
                TableHashMap.put("MeasuresNames", reportQueryColumnNames);
            }
            //}
            HashMap timeDetailsMap = new HashMap();
            ArrayList timeDetailsArray = new ArrayList();
            /*
             * Time Processing starts
             */
            List row = root.getChildren("progen_time");//Only one row as of now
            /*
             * Start of Processing of parameters
             */
            for (int i = 0; i < row.size(); i++) {//Loop for section two under portlet
                Element Companyname = (Element) row.get(i);

                List timeMasterRow = Companyname.getChildren("time_master");
                for (int j = 0; j < timeMasterRow.size(); j++) {
                    Element paramElement = (Element) timeMasterRow.get(j);
                    timeDetailsArray.add(xmUtil.getXmlTagValue(paramElement, "timeLevel"));
                    timeDetailsArray.add(xmUtil.getXmlTagValue(paramElement, "timeType"));
                }
                if (timeDetailsArray.get(1).toString().equalsIgnoreCase("PRG_STD")) {
                    List timeDetailsRow = Companyname.getChildren("timeDetails");
                    for (int j = 0; j < timeDetailsRow.size(); j++) {
                        ArrayList timeInfo = new ArrayList();
                        Element paramElement1 = (Element) timeDetailsRow.get(j);
                        String temp = xmUtil.getXmlTagValue(paramElement1, "timeColType");
                        // // ////.println("temp==" + temp);
                        String currVal = xmUtil.getXmlTagValue(paramElement1, "defaultValue");
                        String realDate = xmUtil.getXmlTagValue(paramElement1, "realDate");
                        String temprealDate = realDate;
                        if (currVal.equalsIgnoreCase("sysdate")) {
                            currVal = "Current Date";
                        } else if (currVal.equalsIgnoreCase("dynamicDate")) { //purpose of dynamic headlines(report builds with system date)
//                        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
//                        Date date = new Date();
//                        realDate=(dateFormat.format(date.getTime()));
                            //purpose of dynamic headlines(report builds with system date)
                            //                        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                            //                        Date date = new Date();
                            //                        realDate=(dateFormat.format(date.getTime()));
                            realDate = pParam.getDefaultSystemTime(null, null, 0);
                            ;
                        }
                        if (schedule == null) { //for snapshot code
                            if (currVal == null || "".equalsIgnoreCase(currVal) || currVal.equalsIgnoreCase("Current Date")) {

                                {//default Value
                                    if (temp.equalsIgnoreCase("AS_OF_DATE") || temp.equalsIgnoreCase("AS_OF_DATE1") || temp.equalsIgnoreCase("AS_OF_DATE") || temp.equalsIgnoreCase("AS_OF_DATE1")) {
                                        timeInfo.add(pParam.getdateforpage());
                                    } else if (temp.equalsIgnoreCase("AS_OF_MONTH") || temp.equalsIgnoreCase("AS_OF_MONTH1")) {
                                        timeInfo.add(pParam.getmonthforpage());
                                    } else if (temp.equalsIgnoreCase("AS_OF_YEAR") || temp.equalsIgnoreCase("AS_OF_YEAR1")) {
                                        timeInfo.add(pParam.getYearforpage());
                                    } else if (temp.equalsIgnoreCase("PRG_PERIOD_TYPE")) {
                                        timeInfo.add(temprealDate);
                                        // timeInfo.add("Month");
                                    } else if (temp.equalsIgnoreCase("PRG_COMPARE")) {
                                        timeInfo.add(temprealDate);
                                        //timeInfo.add("Last Period");
                                    }
                                }
                            } else if (temp.equalsIgnoreCase("AS_OF_DATE") || temp.equalsIgnoreCase("AS_OF_DATE1") || temp.equalsIgnoreCase("AS_OF_DATE") || temp.equalsIgnoreCase("AS_OF_DATE1")) {
                                timeInfo.add(realDate);
                            } else if (temp.equalsIgnoreCase("AS_OF_MONTH") || temp.equalsIgnoreCase("AS_OF_MONTH1")) {
                                timeInfo.add(realDate);
                            } else if (temp.equalsIgnoreCase("AS_OF_YEAR") || temp.equalsIgnoreCase("AS_OF_YEAR1")) {
                                timeInfo.add(realDate);
                            } else if (temp.equalsIgnoreCase("PRG_PERIOD_TYPE")) {
                                timeInfo.add(temprealDate);
                                //  timeInfo.add("Month");
                            } else if (temp.equalsIgnoreCase("PRG_COMPARE")) {
                                timeInfo.add(temprealDate);
                                // timeInfo.add("Last Period");
                            }
                        } else {
                            if (schedule.getDataSelection() != null) {
                                SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                                Date date = new Date();
                                if (schedule.getDataSelection().equals("Current Day")) {
                                    realDate = (dateFormat.format(date.getTime()));
                                } else if (schedule.getDataSelection().equals("Previous Day")) {
                                    int MILLIS_IN_DAY = 1000 * 60 * 60 * 24;
                                    realDate = (dateFormat.format(date.getTime() - MILLIS_IN_DAY));
                                }
                                // 
                            }
                            if (temp.equalsIgnoreCase("AS_OF_DATE") || temp.equalsIgnoreCase("AS_OF_DATE1") || temp.equalsIgnoreCase("AS_OF_DATE") || temp.equalsIgnoreCase("AS_OF_DATE1")) {
                                timeInfo.add(realDate);
                            } else if (temp.equalsIgnoreCase("AS_OF_MONTH") || temp.equalsIgnoreCase("AS_OF_MONTH1")) {
                                timeInfo.add(realDate);
                            } else if (temp.equalsIgnoreCase("AS_OF_YEAR") || temp.equalsIgnoreCase("AS_OF_YEAR1")) {
                                timeInfo.add(realDate);
                            } else if (temp.equalsIgnoreCase("PRG_PERIOD_TYPE")) {
                                timeInfo.add(temprealDate);
                                // 
                                //timeInfo.add("Month");
                            } else if (temp.equalsIgnoreCase("PRG_COMPARE")) {
                                timeInfo.add(temprealDate);
                                /// 
                                //timeInfo.add("Last Period");
                            }
                        }
                        timeInfo.add("CBO_" + temp);
                        timeInfo.add(xmUtil.getXmlTagValue(paramElement1, "timeColName"));
                        timeInfo.add(xmUtil.getXmlTagValue(paramElement1, "timeColSeq"));
                        timeInfo.add(xmUtil.getXmlTagValue(paramElement1, "timeFormSeq"));
                        if (currVal == null || "".equalsIgnoreCase(currVal) || currVal.equalsIgnoreCase("Current Date")) {
                            timeInfo.add(currVal);
                        } else {
                            timeInfo.add(realDate);
                        }
                        timeInfo.add(temp);

                        timeDetailsMap.put(timeInfo.get(6), timeInfo);
                    }
                    // // ////.println("timeDetailsMap===" + timeDetailsMap);
                    ArrayList timeInfo = new ArrayList();
                    if (timeDetailsArray.get(0).toString().equalsIgnoreCase("Year") && timeDetailsArray.get(1).toString().equalsIgnoreCase("PRG_YEAR_CMP")) {
                        timeInfo = (ArrayList) timeDetailsMap.get("AS_OF_YEAR");
                        timeDetailsArray.add(timeInfo.get(0));
                        timeInfo = (ArrayList) timeDetailsMap.get("AS_OF_YEAR1");
                        timeDetailsArray.add(timeInfo.get(0));
                    } else if (timeDetailsArray.get(0).toString().equalsIgnoreCase("DAY") && timeDetailsArray.get(1).toString().equalsIgnoreCase("PRG_STD")) {
                        timeInfo = (ArrayList) timeDetailsMap.get("AS_OF_DATE");
                        if (timeDetailsMap.get("AS_OF_DATE") != null) {
                            timeDetailsArray.add(timeInfo.get(0));
                        } else {
                            timeDetailsArray.add(timeDsArray.get(2)); //for report without date pupose
                        }
                        timeInfo = (ArrayList) timeDetailsMap.get("PRG_PERIOD_TYPE");
                        if (timeDetailsMap.get("PRG_PERIOD_TYPE") != null) {
                            timeDetailsArray.add(timeInfo.get(0));
                        } else {
                            timeDetailsArray.add(timeDsArray.get(3)); //for report without date pupose
                        }
                        if (timeDetailsMap.get("PRG_COMPARE") != null) {
                            timeInfo = (ArrayList) timeDetailsMap.get("PRG_COMPARE");
                            timeDetailsArray.add(timeInfo.get(0));
                        } else {
                            timeDetailsArray.add("Last Period");
                        }
                        //timeInfo = (ArrayList) timeDetailsMap.get("PRG_COMPARE");
                        //timeDetailsArray.add(timeInfo.get(0));
                    } else if (timeDetailsArray.get(0).toString().equalsIgnoreCase("MONTH") && timeDetailsArray.get(1).toString().equalsIgnoreCase("PRG_STD")) {
                        timeInfo = (ArrayList) timeDetailsMap.get("AS_OF_MONTH");
                        timeDetailsArray.add(timeInfo.get(0));
                        timeInfo = (ArrayList) timeDetailsMap.get("PRG_PERIOD_TYPE");
                        timeDetailsArray.add(timeInfo.get(0));

                        timeInfo = (ArrayList) timeDetailsMap.get("PRG_COMPARE");
                        timeDetailsArray.add(timeInfo.get(0));
                    }
                }//End of section 2 of collection
                else {
                    // 
                    collect.timeDetailsMap = timeDSMap;
                    collect.timeDetailsArray = timeDsArray;
                    timeDetailsArray = timeDsArray;
                    timeDetailsMap = timeDSMap;
                }
                // // ////.println("param hashmap=before==" + ParametersHashMap);
                ParametersHashMap.put("TimeDetailstList", timeDetailsArray);
                ParametersHashMap.put("TimeDimHashMap", timeDetailsMap);
            }
            // // ////.println("param hashmap=after==" + ParametersHashMap);
            container.setParametersHashMap(ParametersHashMap);
            container.setTableHashMap(TableHashMap);
        }
        //build  meta info
        return container;
    }

    //Added by amar for GO Scheduler
    public void updateReportForGraphSchedule(String reportId, String schedulerId, String userId, String contentType, String schedulerName) {
        try {
            PbReportViewerBD prvBD = new PbReportViewerBD();
            Container container = new Container();
            container = prvBD.generateContainer(reportId, schedulerId, userId, schedulerName);
            String filePath = File.separator + "usr" + File.separator + "local" + File.separator + "cache" + File.separator + "analyticalobject" + File.separator + "R_GO_" + reportId + ".json";
            File datafile = new File(filePath);
            String jsonData = "";
            Gson gson = new Gson();
            if (datafile.exists()) {
                FileReadWrite fileReadWrite = new FileReadWrite();
                jsonData = fileReadWrite.loadJSON(filePath);
                ReportObjectMeta roMeta = new ReportObjectMeta();
                ReportObjectMeta roMeta1 = new ReportObjectMeta();
                roMeta = gson.fromJson(jsonData, ReportObjectMeta.class);
                List<String> viewByLists = roMeta.getViewIds();
                List<String> viewByNames = roMeta.getViewNames();
                List<String> measIds = roMeta.getMeasIds();
                List<String> measnames = roMeta.getMeasNames();
                List<String> aggType = roMeta.getAggregations();
                String[] viewBys = viewByLists.toArray(new String[0]);
                String[] viewByName = viewByNames.toArray(new String[0]);
                String[] measrIds = measIds.toArray(new String[0]);
                String[] measrNames = measnames.toArray(new String[0]);
                String[] aggTypes = aggType.toArray(new String[0]);
                JsonGenerator jsonGenerator = new JsonGenerator();
                jsonGenerator.generatedataJson1(container, userId, reportId, viewBys, viewByName, measrIds, measrNames, null, aggTypes, null, roMeta1, null, "", "", "");

                String bizzRoleId = container.getReportCollect().reportBizRoles[0];
                String bizzRoleName = container.getReportCollect().getReportBizRoleName(bizzRoleId);
                String reportName = container.getReportCollect().reportName;
                String filePath1 = "/usr/local/cache/" + bizzRoleName.replaceAll("\\W", "").trim() + "/" + reportName.replaceAll("\\W", "").trim() + "_" + reportId + "/" + reportName.replaceAll("\\W", "").trim() + "_" + reportId + "_data.json";
                XtendReportMeta reportMeta = gson.fromJson(fileReadWrite.loadJSON(filePath1), XtendReportMeta.class);
                ReportManagementDAO reportDAO = new ReportManagementDAO();
                String data = reportDAO.getChartsDataForGO(reportMeta, reportId);
                fileReadWrite.writeToFile("/usr/local/cache/" + bizzRoleName.replaceAll("\\W", "").trim() + "/" + reportName.replaceAll("\\W", "").trim() + "_" + reportId + "/" + reportName.replaceAll("\\W", "").trim() + "_" + reportId + ".json", data);
            }
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
            //return null;
        }
    }
//end of code

    //added by sruthi for hideGtzero
    public void hideGtMeasure(Container container, ArrayList reportQryElementIds, ArrayList reportQryAggregations) {
        ArrayList<String> measList = new ArrayList<String>();
        ArrayList<String> gtmeasurezero = new ArrayList<String>();
        ArrayList<String> gtmeasurezero1 = new ArrayList<String>();
        BigDecimal value = null;
        BigDecimal value1 = null;
        PbReturnObject retobj = null;
        String tempFormula = "";
        String refferedElements = "";
        String userColType = "";
        String refElementType = "";
        String aggType = "";
        DataFacade facade = new DataFacade(container);
        PbDb pbdb = new PbDb();
        //  gtmeasurezero=container.gethidegtzero();
        gtmeasurezero1 = container.gethidegtzero1();
        ArrayList<String> prevhide = new ArrayList<String>();
        prevhide = container.getReportCollect().getHideMeasures();
        for (int i = 0; i < reportQryElementIds.size(); i++) {
            String qry = "select  ACTUAL_COL_FORMULA,REFFERED_ELEMENTS,USER_COL_TYPE,REF_ELEMENT_TYPE,AGGREGATION_TYPE from PRG_USER_ALL_INFO_DETAILS where ELEMENT_ID =" + reportQryElementIds.get(i);
            try {
                retobj = pbdb.execSelectSQL(qry);
            } catch (SQLException ex) {
                logger.error("Exception: ", ex);
            }
            if (retobj != null && retobj.getRowCount() > 0) {
                tempFormula = retobj.getFieldValueString(0, 0);
                refferedElements = retobj.getFieldValueString(0, 1);
                userColType = retobj.getFieldValueString(0, 2);
                refElementType = retobj.getFieldValueString(0, 3);
                aggType = retobj.getFieldValueString(0, 4);
                tempFormula = tempFormula.replace("SUM", "").replace("AVG", "").replace("MIN", "").replace("MAX", "").replace("COUNT", "").replace("COUNTDISTINCT", "");
                if (aggType.equalsIgnoreCase("avg") && userColType.equalsIgnoreCase("SUMMARIZED")) {
                    String refEleArray[] = refferedElements.split(",");
                    int len = refEleArray.length;
                    int flag = 1;
                    String mysqlString = "";
                    for (int j = 0; j < len; j++) {
                        String elementId = refEleArray[j];
                        String getBussColName = "select USER_COL_NAME,AGGREGATION_TYPE from PRG_USER_ALL_INFO_DETAILS where ELEMENT_ID =" + elementId;
                        PbReturnObject retobj1 = null;
                        try {
                            retobj1 = pbdb.execSelectSQL(getBussColName);
                        } catch (SQLException ex) {
                            logger.error("Exception: ", ex);
                        }
                        if (retobj1 != null && retobj1.getRowCount() > 0) {
                            String bussColName = retobj1.getFieldValueString(0, 0);
                            String aggrType = retobj1.getFieldValueString(0, 1);
                            if (tempFormula.toUpperCase().contains(bussColName.toUpperCase())) {
                                String newEleID = "A_" + elementId;
                                tempFormula = tempFormula.toUpperCase().replace(bussColName.toUpperCase(), newEleID);
                                BigDecimal grandTotalValueForEle = null;
                                grandTotalValueForEle = facade.getColumnGrandTotalValue(newEleID);
                                if (grandTotalValueForEle == null) {
                                    flag = 0;
                                }
                                if (flag == 1) {
                                    grandTotalValueForEle = grandTotalValueForEle.setScale(2, RoundingMode.CEILING);
                                    if (aggrType.equalsIgnoreCase("AVG") || aggrType.toUpperCase().contains("AVG")) {
                                        grandTotalValueForEle = grandTotalValueForEle.divide(new BigDecimal(facade.getRowCount()), RoundingMode.HALF_UP);
                                    }
                                    if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                                        mysqlString = mysqlString + "," + grandTotalValueForEle + " AS " + newEleID;
                                    } else {
                                        tempFormula = tempFormula.replace(newEleID, grandTotalValueForEle.toString());
                                    }
                                }
                            }
                        }
                    }
                    String formula = "";
                    if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                        tempFormula = "SELECT " + tempFormula;
                    } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                        mysqlString = mysqlString.substring(1);
                        tempFormula = "SELECT " + tempFormula + " FROM (SELECT " + mysqlString + ") A";
                    } else {
                        tempFormula = "SELECT " + tempFormula + " FROM DUAL";
                    }
                    PbReturnObject retobj2 = null;
                    try {
                        retobj2 = pbdb.execSelectSQL(tempFormula);
                    } catch (SQLException ex) {
                        logger.error("Exception: ", ex);
                    }
                    if (retobj2 != null && retobj2.getRowCount() > 0) {
                        formula = retobj2.getFieldValueString(0, 0);
                        if (formula.equalsIgnoreCase("")) {
                            formula = "0";
                        }
                        // BigDecimal bg=new  BigDecimal("9873428.0") ;
                        BigDecimal subTotalVal = new BigDecimal(formula);
                        subTotalVal = subTotalVal.setScale(2, RoundingMode.CEILING);
                        // boolean b1=subTotalVal.equals(bg);
                        String measure = (String) (reportQryElementIds.get(i));
                        if (prevhide != null || prevhide.isEmpty()) {
                            if (!prevhide.contains(measure)) {
//                               if(subTotalVal.equals("0.0")){
//                                    subTotalVal=BigDecimal.ZERO;
//                                     }
                                if (subTotalVal.compareTo(BigDecimal.ZERO) == 0) {
                                    measList.add(measure);
                                }
                            }
                        }
                        if (prevhide != null || prevhide.isEmpty()) {
                            if (prevhide.contains(measure)) {
                                if (gtmeasurezero1.contains(measure) && gtmeasurezero1 != null) {
//                                if(subTotalVal.equals("0.0")){
//                                    subTotalVal=BigDecimal.ZERO;
//                                     }
                                    if (subTotalVal.compareTo(BigDecimal.ZERO) == 0) {
                                        measList.add(measure);
                                    }
                                } else {
                                    measList.add(measure);
                                }

                            }
                        }
                        container.getReportCollect().setHideMeasures(measList);
                    } else {
                        // BigDecimal bg3=new  BigDecimal("59583.0") ;
                        String measure1 = "A_" + (String) reportQryElementIds.get(i);
                        value1 = facade.getColumnGrandTotalValue(measure1);
                        //  boolean b2=value1.equals(bg3);
                        String measure = (String) (reportQryElementIds.get(i));
                        if (prevhide != null || prevhide.isEmpty()) {
                            if (!prevhide.contains(measure)) {
//                              if(value1.equals("0.0")){
//                                    value1=BigDecimal.ZERO;
//                                     }
                                if (value1.compareTo(BigDecimal.ZERO) == 0) {
                                    measList.add(measure);
                                }
                            }
                        }
                        if (prevhide != null || prevhide.isEmpty()) {
                            if (prevhide.contains(measure)) {
                                if (gtmeasurezero1.contains(measure) && gtmeasurezero1 != null) {
//                                  if(value1.equals("0.0")){
//                                    value1=BigDecimal.ZERO;
//                                     }
                                    if (value1.compareTo(BigDecimal.ZERO) == 0) {
                                        measList.add(measure);
                                    }
                                } else {
                                    measList.add(measure);
                                }

                            }
                        }
                        container.getReportCollect().setHideMeasures(measList);
                    }
                } else {
                    // BigDecimal bg2=new  BigDecimal("59583.0") ;
                    String measure1 = "A_" + (String) reportQryElementIds.get(i);
                    value = facade.getColumnGrandTotalValue(measure1);
                    int flag = 0;
                    // boolean b2=value.equals(bg2);
                    String measure = (String) (reportQryElementIds.get(i));
                    if (prevhide != null || prevhide.isEmpty()) {
                        if (!prevhide.contains(measure)) {
//                                if(value.equals("0.0")){
//                                    value=BigDecimal.ZERO;
//                                     }
                            if (value.compareTo(BigDecimal.ZERO) == 0) {
                                measList.add(measure);
                            }
                        }
                    }
                    if (prevhide != null || prevhide.isEmpty()) {
                        if (prevhide.contains(measure)) {
                            if (gtmeasurezero1.contains(measure) && gtmeasurezero1 != null) {
//                                 if(value.equals("0.0")){
//                                    value=BigDecimal.ZERO;
//                                     }
                                if (value.compareTo(BigDecimal.ZERO) == 0) {
                                    measList.add(measure);
                                }
                            } else {
                                measList.add(measure);
                            }

                        }
                    }
                    container.getReportCollect().setHideMeasures(measList);
                }
            }
        }

    }//ended by sruthi

    public void getAllChangePercenrMeasures(PbReturnObject pbretObj, Container container) {
        String selectedList = "MTD,PMTD,PYMTD,QTD,PQTD,PYQTD,YTD,PYTD";
//            rowEdgeParams = request.getParameter("rowEdgeParams");
//            colEdgeParams = request.getParameter("colEdgeParams");
        ArrayList<String> Fulllist = container.getAllColumns();
//        selectedList="MTD,PMTD,PYMTD,QTD,PQTD,PYQTD,YTD,PYTD";
        String[] splitselectedList = selectedList.split(",");
        String Allchange = "CPMTD,C%PMTD,CPYMTD,C%PYMTD,CPQTD,C%PQTD,CPYQTD,C%PYQTD,CPYTD,C%PYTD";
        String[] splitAllchange = Allchange.split(",");
        //String[] splitFulllist = Fulllist.split(",");
        ArrayList<String> currentTimeperiods = new ArrayList<>();
        ArrayList<String> CurrentAllchanges = new ArrayList<>();
        ArrayList<String> allcolumns = new ArrayList<>();
        boolean b = true;
        outerloop:
        for (int i = 0; i < Fulllist.size(); i++) {
            allcolumns.add(Fulllist.get(i));
            for (int j = 0; j < splitselectedList.length; j++) {

                if (Fulllist.get(i).equalsIgnoreCase(splitselectedList[j])) {
                    currentTimeperiods.add(Fulllist.get(i));
                    continue outerloop;
                }
            }
            for (int j = 0; j < splitAllchange.length; j++) {
                if (Fulllist.get(i).equalsIgnoreCase(splitAllchange[j])) {
                    CurrentAllchanges.add(Fulllist.get(i));
                    continue outerloop;
                }
            }
        }
        ArrayList<String> changeColumns = CurrentAllchanges;
        ArrayList<String> DisplayColumns = container.getDisplayColumns();
        ArrayList<String> measureIds = new ArrayList<>();
        ArrayList<String> DisplayLabels1 = new ArrayList<>();
        String[] columnTypes = pbretObj.getColumnTypes();
        for (int MsrsLngth = 0; MsrsLngth < DisplayColumns.size(); MsrsLngth++) {
            if (!columnTypes[MsrsLngth].equalsIgnoreCase("VARCHAR") && !columnTypes[MsrsLngth].equalsIgnoreCase("CHAR")) {
                measureIds.add(DisplayColumns.get(MsrsLngth));
//DisplayLabels1.add(DisplayLabels.get(MsrsLngth));
            }
        }
//    String Allchange ="CPMTD,C%PMTD,CPYMTD,C%PYMTD,CPQTD,C%PQTD,CPYQTD,C%PYQTD,CPYTD,C%PYTD";
//         String Allchangearr[] =Allchange.split(",");
        HashMap<String, BigDecimal> AllChangeMap = new HashMap<String, BigDecimal>();
//              ArrayList<String> a3 = null;
//             List<String> l3 = Arrays.<String>asList(Allchangearr);
//           a3= new ArrayList<>(l3);
//            container.setChangeColumns(a3);
        BigDecimal b1;
        BigDecimal b2;
        BigDecimal b3;
        for (int j = 0; j < CurrentAllchanges.size(); j++) {

            if (changeColumns.get(j).equalsIgnoreCase("CPMTD")) {
                for (int i = 0; i < measureIds.size(); i++) {
                    b1 = pbretObj.getFieldValueBigDecimal(0, measureIds.get(i) + "_MTD");
                    b2 = pbretObj.getFieldValueBigDecimal(0, measureIds.get(i) + "_PMTD");
//                             (b1.subtract(b2));
//                           d=(Double.parseDouble(s1)-Double.parseDouble(s2));
                    AllChangeMap.put(measureIds.get(i) + "_CPMTD", (b1.subtract(b2)).setScale(2, RoundingMode.CEILING));
//                              AllChangeMap.put(measureIds.get(i)+"_CPMTD", (b1.subtract(b2)));
                }
            } else if (changeColumns.get(j).equalsIgnoreCase("C%PMTD")) {
                for (int i = 0; i < measureIds.size(); i++) {

                    b1 = pbretObj.getFieldValueBigDecimal(0, measureIds.get(i) + "_MTD");
                    b2 = pbretObj.getFieldValueBigDecimal(0, measureIds.get(i) + "_PMTD");
                    if (b2.equals(new BigDecimal("0")) || b2.equals(new BigDecimal("0.0")) || b2.equals(new BigDecimal("0.00"))) {
                        AllChangeMap.put(measureIds.get(i) + "_C%PMTD", new BigDecimal("0"));
                    } else {
                        b3 = (((b1.subtract(b2)).multiply(new BigDecimal("100"))).divide(b2, 2, RoundingMode.CEILING));
                        AllChangeMap.put(measureIds.get(i) + "_C%PMTD", b3);
                    }
//                           d=((Double.parseDouble(pbretObj.getFieldValue(0, "A_"+measureIds.get(i)+"_MTD").toString())
//                                   -Double.parseDouble(pbretObj.getFieldValue(0, "A_"+measureIds.get(i)+"_PMTD").toString()))
//                                   /(Double.parseDouble(pbretObj.getFieldValue(0, "A_"+measureIds.get(i)+"_PMTD").toString())))*100;
                }
            } else if (changeColumns.get(j).equalsIgnoreCase("CPYMTD")) {
                for (int i = 0; i < measureIds.size(); i++) {
                    b1 = pbretObj.getFieldValueBigDecimal(0, measureIds.get(i) + "_MTD");
                    b2 = pbretObj.getFieldValueBigDecimal(0, measureIds.get(i) + "_PYMTD");
                    AllChangeMap.put(measureIds.get(i) + "_CPYMTD", (b1.subtract(b2)).setScale(2, RoundingMode.CEILING));
//                            d=(Double.parseDouble(pbretObj.getFieldValue(0, "A_"+measureIds.get(i)+"_MTD").toString())
//                                   -Double.parseDouble(pbretObj.getFieldValue(0, "A_"+measureIds.get(i)+"_PYMTD").toString()));
//
//                           AllChangeMap.put("A_"+measureIds.get(i)+"_CPYMTD",d);
                }
            } else if (changeColumns.get(j).equalsIgnoreCase("C%PYMTD")) {
                for (int i = 0; i < measureIds.size(); i++) {
                    b1 = pbretObj.getFieldValueBigDecimal(0, measureIds.get(i) + "_MTD");
                    b2 = pbretObj.getFieldValueBigDecimal(0, measureIds.get(i) + "_PYMTD");
                    if (b2.equals(new BigDecimal("0")) || b2.equals(new BigDecimal("0.0")) || b2.equals(new BigDecimal("0.00"))) {
                        AllChangeMap.put(measureIds.get(i) + "_C%PYMTD", new BigDecimal("0"));
                    } else {
                        b3 = (((b1.subtract(b2)).multiply(new BigDecimal("100"))).divide(b2, 2, RoundingMode.CEILING));
                        AllChangeMap.put(measureIds.get(i) + "_C%PYMTD", b3);
                    }
//                            d=((Double.parseDouble(pbretObj.getFieldValue(0, "A_"+measureIds.get(i)+"_MTD").toString())
//                                   -Double.parseDouble(pbretObj.getFieldValue(0, "A_"+measureIds.get(i)+"_PYMTD").toString()))
//                                   /(Double.parseDouble(pbretObj.getFieldValue(0, "A_"+measureIds.get(i)+"_PYMTD").toString())))*100;
//                           AllChangeMap.put("A_"+measureIds.get(i)+"_C%PYMTD",d);
                }
            } else if (changeColumns.get(j).equalsIgnoreCase("CPQTD")) {
                for (int i = 0; i < measureIds.size(); i++) {
                    b1 = pbretObj.getFieldValueBigDecimal(0, measureIds.get(i) + "_QTD");
                    b2 = pbretObj.getFieldValueBigDecimal(0, measureIds.get(i) + "_PQTD");
                    AllChangeMap.put(measureIds.get(i) + "_CPQTD", (b1.subtract(b2)).setScale(2, RoundingMode.CEILING));
//                           d=(Double.parseDouble(pbretObj.getFieldValue(0, "A_"+measureIds.get(i)+"_QTD").toString())
//                                   -Double.parseDouble(pbretObj.getFieldValue(0, "A_"+measureIds.get(i)+"_PQTD").toString()));
//                           AllChangeMap.put("A_"+measureIds.get(i)+"_CPQTD",d);
                }
            } else if (changeColumns.get(j).equalsIgnoreCase("C%PQTD")) {
                for (int i = 0; i < measureIds.size(); i++) {
                    b1 = pbretObj.getFieldValueBigDecimal(0, measureIds.get(i) + "_QTD");
                    b2 = pbretObj.getFieldValueBigDecimal(0, measureIds.get(i) + "_PQTD");
                    if (b2.equals(new BigDecimal("0")) || b2.equals(new BigDecimal("0.0")) || b2.equals(new BigDecimal("0.00"))) {
                        AllChangeMap.put(measureIds.get(i) + "_C%PQTD", new BigDecimal("0"));
                    } else {
                        b3 = (((b1.subtract(b2)).multiply(new BigDecimal("100"))).divide(b2, 2, RoundingMode.CEILING));
                        AllChangeMap.put(measureIds.get(i) + "_C%PQTD", b3);
                    }
                    //                            d=((Double.parseDouble(pbretObj.getFieldValue(0, "A_"+measureIds.get(i)+"_QTD").toString())
//                                   -Double.parseDouble(pbretObj.getFieldValue(0, "A_"+measureIds.get(i)+"_PQTD").toString()))
//                                   /(Double.parseDouble(pbretObj.getFieldValue(0, "A_"+measureIds.get(i)+"_PQTD").toString())))*100;
//                           AllChangeMap.put("A_"+measureIds.get(i)+"_C%PQTD",d);
                }
            } else if (changeColumns.get(j).equalsIgnoreCase("CPYQTD")) {
                for (int i = 0; i < measureIds.size(); i++) {
                    b1 = pbretObj.getFieldValueBigDecimal(0, measureIds.get(i) + "_QTD");
                    b2 = pbretObj.getFieldValueBigDecimal(0, measureIds.get(i) + "_PYQTD");
                    AllChangeMap.put(measureIds.get(i) + "_CPYQTD", (b1.subtract(b2)).setScale(2, RoundingMode.CEILING));
//                           d=(Double.parseDouble(pbretObj.getFieldValue(0, "A_"+measureIds.get(i)+"_QTD").toString())
//                                   -Double.parseDouble(pbretObj.getFieldValue(0, "A_"+measureIds.get(i)+"_PYQTD").toString()));
//
//                           AllChangeMap.put("A_"+measureIds.get(i)+"_CPYQTD",d);
                }
            } else if (changeColumns.get(j).equalsIgnoreCase("C%PYQTD")) {
                for (int i = 0; i < measureIds.size(); i++) {
                    b1 = pbretObj.getFieldValueBigDecimal(0, measureIds.get(i) + "_QTD");
                    b2 = pbretObj.getFieldValueBigDecimal(0, measureIds.get(i) + "_PYQTD");
                    if (b2.equals(new BigDecimal("0")) || b2.equals(new BigDecimal("0.0")) || b2.equals(new BigDecimal("0.00"))) {
                        AllChangeMap.put(measureIds.get(i) + "_C%PYQTD", new BigDecimal("0"));
                    } else {
                        b3 = (((b1.subtract(b2)).multiply(new BigDecimal("100"))).divide(b2, 2, RoundingMode.CEILING));
                        AllChangeMap.put(measureIds.get(i) + "_C%PYQTD", b3);
                    }
                }
            } else if (changeColumns.get(j).equalsIgnoreCase("CPYTD")) {
                for (int i = 0; i < measureIds.size(); i++) {

                    b1 = pbretObj.getFieldValueBigDecimal(0, measureIds.get(i) + "_YTD");
                    b2 = pbretObj.getFieldValueBigDecimal(0, measureIds.get(i) + "_PYTD");
                    AllChangeMap.put(measureIds.get(i) + "_CPYTD", (b1.subtract(b2)).setScale(2, RoundingMode.CEILING));
                }
            } else if (changeColumns.get(j).equalsIgnoreCase("C%PYTD")) {
                for (int i = 0; i < measureIds.size(); i++) {
                    b1 = pbretObj.getFieldValueBigDecimal(0, measureIds.get(i) + "_YTD");
                    b2 = pbretObj.getFieldValueBigDecimal(0, measureIds.get(i) + "_PYTD");
                    if (b2.equals(new BigDecimal("0")) || b2.equals(new BigDecimal("0.0")) || b2.equals(new BigDecimal("0.00"))) {
                        AllChangeMap.put(measureIds.get(i) + "_C%PYTD", new BigDecimal("0"));
                    } else {
                        b3 = (((b1.subtract(b2)).multiply(new BigDecimal("100"))).divide(b2, 2, RoundingMode.CEILING));
                        AllChangeMap.put(measureIds.get(i) + "_C%PYTD", b3);
                    }
//                           d=((Double.parseDouble(pbretObj.getFieldValue(0, "A_"+measureIds.get(i)+"_YTD").toString())
//                                   -Double.parseDouble(pbretObj.getFieldValue(0, "A_"+measureIds.get(i)+"_PYTD").toString()))
//                                   /(Double.parseDouble(pbretObj.getFieldValue(0, "A_"+measureIds.get(i)+"_PYTD").toString())))*100;
//                           AllChangeMap.put("A_"+measureIds.get(i)+"_C%PYTD",d);
                }
            }
        }
//         container.setTimeDBRetObj(pbretObj);
        container.setallMesChange(AllChangeMap);
    }

    public PbReturnObject getAllHeadersName(PbReportCollection reportCollect) {
        String date = ((ArrayList<String>) reportCollect.timeDetailsArray).get(2);
        Connection connection = null;
        PbReturnObject pbro = null;
        String query = "";
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
//     query=" SELECT cm_cust_name,pm_cust_name,pym_cust_name,cq_cust_name,lq_cust_name,lyq_cust_name,cy_cust_name,ly_cust_name from pr_day_denom  WHERE ddate = convert(datetime,'"+date+" ',120)";
            query = "select * from pr_day_denom  WHERE ddate = convert(datetime,'" + date + " ',120)";

        } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {

//query=" SELECT cm_cust_name,pm_cust_name,pym_cust_name,cq_cust_name,lq_cust_name,lyq_cust_name,cy_cust_name,ly_cust_name from pr_day_denom  WHERE ddate = str_to_date('"+date+" ','%m/%d/%Y')";
            query = "select * from pr_day_denom  WHERE ddate = str_to_date('" + date + " ','%m/%d/%Y')";
        } else {
//query=" SELECT cm_cust_name,pm_cust_name,pym_cust_name,cq_cust_name,lq_cust_name,lyq_cust_name,cy_cust_name,ly_cust_name from pr_day_denom  WHERE ddate = to_date('"+date+" ','mm/dd/yyyy')";
            query = "select * from pr_day_denom  WHERE ddate = to_date('" + date + " ','mm/dd/yyyy')";
        }
        try {
            connection = ProgenConnection.getInstance().getConnectionForElement(reportCollect.reportQryElementIds.get(0));
            pbro = new PbDb().execSelectSQL(query, connection);
        } catch (Exception e) {
            logger.error("Exception: ", e);
        }
        int rowCount = pbro.getRowCount();

        return pbro;

    }

    public void MultiCalendar(Container container, String reportid) throws SQLException {
        String query = "select  BUSS_TABLE_ID,DISP_NAME,BUSS_COL_NAME,USER_COL_DESC,USER_COL_TYPE,USER_COL_NAME,CAL_COL_TYPE ,IS_DEFAULT,CALENDER_ID,DENOM_TABLE from PRG_AR_ALTERNATE_DATA where REPORT_ID=" + reportid;
        PbReturnObject pbro = null;
        HashMap multicalendardata = new HashMap();
        HashMap multidatecalendar = new HashMap();
        PbDb pbdb1 = new PbDb();
        pbro = pbdb1.execSelectSQL(query);
        if (pbro.getRowCount() != 0) {
            for (int q = 0; q < pbro.getRowCount(); q++) {
                ArrayList<String> factdetails = new ArrayList<String>();
                String buss_table_id = pbro.getFieldValueString(q, 0);
                String DISP_NAME = pbro.getFieldValueString(q, 1);
                String bus_column_name = pbro.getFieldValueString(q, 2);
                String user_column_desc = pbro.getFieldValueString(q, 3);
                String user_column_type = pbro.getFieldValueString(q, 4);
                String user_column_name = pbro.getFieldValueString(q, 5);
                String cal_col_type = pbro.getFieldValueString(q, 6);
                String truncate = pbro.getFieldValueString(q, 7);
                String calenderid = pbro.getFieldValueString(q, 8);
                String calendername = pbro.getFieldValueString(q, 9);
                factdetails.add(DISP_NAME);
                factdetails.add(bus_column_name);
                factdetails.add(user_column_desc);
                factdetails.add(user_column_type);
                factdetails.add(user_column_name);
                factdetails.add(cal_col_type);
                factdetails.add(truncate);
                factdetails.add(calenderid);
                factdetails.add(calendername);
                multicalendardata.put(buss_table_id, factdetails);
            }
        }
        // multidatecalendar.put(multicalendardata);
        container.setMultiCalendarHashMap(multicalendardata);
    }

    // end of Function of PbReportViewerBD.java relocating
    public HashMap<String, String> createCustomMeasureForGraphs(String elementId, String reportId) {
        ProgenReportViewerDAO dao = new ProgenReportViewerDAO();
        HashMap<String, String> measureInfo = dao.createCustomMeasureForGraphs(elementId, reportId);
        return measureInfo;
    }

    public LinkedHashMap parseReportParamXML(Element repEle) {
        LinkedHashMap paramHashMap = new LinkedHashMap();
        Document paramDocument;
        Element root = null;
        SAXBuilder builder = new SAXBuilder();
        try {
            // root = paramDocument.getRootElement();
            List reportParameters = repEle.getChildren("ReportParameter");
            for (int i = 0; i < reportParameters.size(); i++) {
                Element repParam = (Element) reportParameters.get(i);
                List elementList = repParam.getChildren("ElementId");
                List valueList = repParam.getChildren("Value");
                Element element = (Element) elementList.get(0);
                Element value = (Element) valueList.get(0);
                paramHashMap.put(element.getText(), value.getText());
            }
        } catch (Exception e) {
            logger.error("Exception: ", e);
        }
        return paramHashMap;
    }

    public HashMap parseColorCodeXML(Element colorCodeEle) {
        HashMap colorMap = new HashMap();
        Document colorDocument;
        Element root = null;
        SAXBuilder builder = new SAXBuilder();
        try {
            // colorDocument = builder.build(new ByteArrayInputStream(colorCodeXml.toString().getBytes()));
            //  root = colorDocument.getRootElement();
            List colorRules = colorCodeEle.getChildren("ColorCodeRule");
            String[] colorCodes = new String[colorRules.size()];
            String[] operators = new String[colorRules.size()];
            String[] sValues = new String[colorRules.size()];
            String[] eValues = new String[colorRules.size()];
            for (int i = 0; i < colorRules.size(); i++) {
                Element colorEle = (Element) colorRules.get(i);
                List colorCodeList = colorEle.getChildren("ColorCode");
                List operatorList = colorEle.getChildren("Operator");
                List strtValueList = colorEle.getChildren("StrtValue");
                List endValueList = colorEle.getChildren("EndValue");
                Element colorCode = (Element) colorCodeList.get(0);
                Element operator = (Element) operatorList.get(0);
                Element startValue = (Element) strtValueList.get(0);
                colorCodes[i] = colorCode.getText();
                operators[i] = operator.getText();
                sValues[i] = startValue.getText();
                Element endValue = null;
                if (endValueList != null && endValueList.size() != 0) {
                    endValue = (Element) endValueList.get(0);
                    eValues[i] = endValue.getText();
                }
            }
            colorMap.put("colorCodes", colorCodes);
            colorMap.put("operators", operators);
            colorMap.put("sValues", sValues);
            colorMap.put("eValues", eValues);
            colorMap.put("eValues", eValues);

        } catch (Exception e) {
            logger.error("Exception: ", e);
        }
        return colorMap;
    }
}
