/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.reportdesigner.action;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import oracle.jdbc.OraclePreparedStatement;
import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;
import prg.db.Container;
import prg.db.PbDb;
import prg.db.PbReturnObject;
import utils.db.ProgenConnection;

/**
 *
 * @author db2admin
 */
public class WhatIfDesigner extends PbDb {

    public static Logger logger = Logger.getLogger(WhatIfDesigner.class);
    WhatIfResourceBundle resBundle = new WhatIfResourceBundle();
    public Container container = null;
    public HashMap ParametersHashMap = null;
    public HashMap GraphHashMap = null;
    public String whatIfScenarioId = "";
    public String[] rMasterChildTags = {"whatIfScenarioId", "whatIfScenarioName", "whatIfScenarioDesc"};
    public String[] ppDetailChildTags = {"whatIfScenario_id", "paramDispName", "element_id", "dimId", "dimTabId", "buss_table", "childElementId", "dispSeqNo", "displayType", "defaultValue", "relLevel"};
    public String[] ptMasterChildTags = {"timeLevel", "timeType"};
    public String[] ptDetailsChildTags = {"timeColName", "timeColType", "timeColSeq", "timeFormSeq", "timeIsDisplayed", "defaultValue"};
    public String[] colpropertiestags = {"element_id", "show_total", "show_sub_total", "avg_Total", "show_over_all_max", "show_over_all_min", "show_cat_max", "show_cat_min", "display_symbol"};
    public String[] rpDetailChildTags = {"col_seq", "col_disp_name", "element_id", "ref_element_id", "folder_id", "sub_folder_id", "whatIfScenario_id", "aggregation_type", "column_type", "is_whatIf", "what-if-range"};
    public String[] graphMasterChildTags = {"graph_id", "whatIfScenario_id", "graph_name", "graph_size", "graph_type", "graph_class", "graph_order", "allow_legend", "legend_loc", "show_grid_x_axis", "show_grid_y_axis", "right_y_axis_label", "left_y_axis_label", "back_color", "font_color", "allow_link", "show_data", "graph_height", "graph_width", "row_values"};
    public String[] MetaInfoChildValues = null;
    public String paramsString = "";
    public String paramString1 = "";
    public ArrayList params = new ArrayList();
    public ArrayList paramsNames = new ArrayList();
    public ArrayList timeDetails = new ArrayList();
    public HashMap timeDimHashMap = new HashMap();
    public HashMap reportHashMap = new HashMap();
    public HashMap TableHashMap = new HashMap();
    public ArrayList REP_Elements = new ArrayList();
    public ArrayList REP_Elements_Names = new ArrayList();
    public ArrayList CEP_Elements = new ArrayList();
    public ArrayList CEP_Elements_Names = new ArrayList();
    public ArrayList measures = new ArrayList();
    public LinkedHashMap whatIfRanges = new LinkedHashMap();
    public ArrayList measuresNames = new ArrayList();
    public ArrayList grpCols = new ArrayList();
    public Set total_list = new LinkedHashSet();
    public HttpServletRequest request = null;
    public Connection connect = null;
    public Statement statement = null;
    public ResultSet resultSet = null;
    ////////////////////////////////////////////////
    //list of queries
    public String getReportParamDetailsQuery = resBundle.getString("getReportParamDetails");
    public String getReportQueryDetailsQuery = resBundle.getString("getReportQueryDetails");
    XMLOutputter serializer = null;
    Document document = null;
    public String grpIds = null;
    public String[] graphIds = null;

    public void createDocument(HashMap parametersMap, HashMap reportMap, HashMap TableHashMap1, HashMap graphHashMap, String whatIfScnId, String isWhatIfReport) throws Exception {
        try {

            ParametersHashMap = parametersMap;
            TableHashMap = TableHashMap1;
            reportHashMap = reportMap;
            GraphHashMap = graphHashMap;
            whatIfScenarioId = whatIfScnId;


            OraclePreparedStatement opstmt = null;
            if (ParametersHashMap != null) {
                if (ParametersHashMap.get("Parameters") != null) {
                    params = (ArrayList) ParametersHashMap.get("Parameters");
                    paramsNames = (ArrayList) ParametersHashMap.get("ParametersNames");
                }
                if (ParametersHashMap.get("TimeDetailstList") != null) {
                    timeDetails = (ArrayList) ParametersHashMap.get("TimeDetailstList");
                }
                if (ParametersHashMap.get("TimeDimHashMap") != null) {
                    timeDimHashMap = (HashMap) ParametersHashMap.get("TimeDimHashMap");
                }
            }



            if (TableHashMap != null) {
                if (TableHashMap.get("REP") != null) {
                    REP_Elements = (ArrayList) TableHashMap.get("REP");
                    REP_Elements_Names = (ArrayList) TableHashMap.get("REPNames");
                } else {
                    if (params != null && params.size() != 0) {
                        REP_Elements.add(params.get(0));
                        REP_Elements_Names.add(paramsNames.get(0));
                    }
                }

                if (TableHashMap.get("CEP") != null) {
                    CEP_Elements = (ArrayList) TableHashMap.get("CEP");
                    CEP_Elements_Names = (ArrayList) TableHashMap.get("CEPNames");
                }


                if (TableHashMap.get("Measures") != null && TableHashMap.get("MeasuresNames") != null) {
                    measures = (ArrayList) TableHashMap.get("Measures");
                    measuresNames = (ArrayList) TableHashMap.get("MeasuresNames");
                }

                if (TableHashMap.get("whatIfRanges") != null) {
                    whatIfRanges = (LinkedHashMap) TableHashMap.get("whatIfRanges");
                } else {
                    for (int i = 0; i < measures.size(); i++) {
                        whatIfRanges.put((String) measures.get(i), "1.0");
                    }
                }

            }

            total_list = new LinkedHashSet();

            for (int i = 0; i < params.size(); i++) {
                paramsString = paramsString + "," + String.valueOf(params.get(i)).replace("A_", "");
            }
            if (!(paramsString.equalsIgnoreCase(""))) {
                paramsString = paramsString.substring(1);
            }

            for (int i = 0; i < params.size(); i++) {
                paramString1 = paramString1 + "," + String.valueOf(params.get(i)).replace("A_", "") + "," + (i + 1);
            }

            if (!(paramString1.equalsIgnoreCase(""))) {
                paramString1 = paramString1.substring(1);
            }

            if (GraphHashMap.get("graphIds") != null) {
                grpIds = (String) GraphHashMap.get("graphIds");
                graphIds = grpIds.split(",");
            }
            //Element root = new Element("WatIfReport");
            Element root = new Element("WhatIfScenario");
            //root.setAttribute("version", "1.00001");
            //root.setText("New Root");

            root = buildMetaInfo(root);
            root = buildParameters(root);
            root = buildTimeDetails(root);
            root = buildQueryDetails(root);
            root = buildGraphDetails(root);

            document = new Document(root);
            serializer = new XMLOutputter();
            Connection con = ProgenConnection.getInstance().getConnection();
            String query1 = "INSERT INTO PRG_AR_WATIF_REPORTS(REPORT_ID, REPORT_NAME, REPORT_DESC, REPORT_TYPE, HELP_TEXT, REPORT_STATUS, REPORT_DETAILS) values(?,?,?,?,?,?,?)";
            opstmt = (OraclePreparedStatement) con.prepareStatement(query1);
            opstmt.setString(1, whatIfScenarioId);
            opstmt.setString(2, String.valueOf(reportHashMap.get("whatIfScenarioName")));
            opstmt.setString(3, String.valueOf(reportHashMap.get("whatIfScenarioDesc")));
            opstmt.setStringForClob(4, "R");
            opstmt.setString(5, String.valueOf(reportHashMap.get("whatIfScenarioDesc")));
            opstmt.setString(6, "Y");
            opstmt.setStringForClob(7, serializer.outputString(document));
//            int rows = opstmt.executeUpdate();
            opstmt.executeUpdate();
            if (con != null) {
                con.close();
            }
        } catch (Exception e) {
            logger.error("Exception:", e);
        }
    }

    public Element buildMetaInfo(Element root) {
        MetaInfoChildValues = new String[3];
        MetaInfoChildValues[0] = whatIfScenarioId;
        MetaInfoChildValues[1] = String.valueOf(reportHashMap.get("whatIfScenarioName"));
        MetaInfoChildValues[2] = String.valueOf(reportHashMap.get("whatIfScenarioDesc"));

        Element pMasterEle = new Element("Meta_Data");
        Element child = null;

        for (int i = 0; i < rMasterChildTags.length; i++) {
            child = new Element(rMasterChildTags[i]);
            child.setText(MetaInfoChildValues[i]);
            pMasterEle.addContent(child);
        }

        child = new Element("UserFolderIds");
        child.setText(String.valueOf(ParametersHashMap.get("UserFolderIds")));
        pMasterEle.addContent(child);
        root.addContent(pMasterEle);

        return root;
    }

    public Element buildParameters(Element root) {

        PbReturnObject retObj = null;
        String[] dbColumns = null;
        String finalQuery = "";
        String[] Obj = new String[2];
        Obj[0] = paramsString;
        Obj[1] = paramString1;

        Element pParameterEle = new Element("pParameter");
        Element ppDetailEle = null;
        Element child = null;
//        int count = 1;
        try {

            finalQuery = buildQuery(getReportParamDetailsQuery, Obj);
            retObj = execSelectSQL(finalQuery);
            if (retObj != null && retObj.getRowCount() != 0) {
                dbColumns = retObj.getColumnNames();
                for (int i = 0; i < retObj.getRowCount(); i++) {
                    ppDetailEle = new Element("ppDetail");
                    Obj = new String[11];
                    Obj[0] = whatIfScenarioId;
                    Obj[1] = retObj.getFieldValueString(i, dbColumns[0]);
                    Obj[2] = retObj.getFieldValueString(i, dbColumns[1]);
                    Obj[3] = retObj.getFieldValueString(i, dbColumns[2]);
                    Obj[4] = retObj.getFieldValueString(i, dbColumns[3]);
                    Obj[5] = retObj.getFieldValueString(i, dbColumns[4]);
                    Obj[6] = retObj.getFieldValueString(i, dbColumns[5]);
                    Obj[7] = retObj.getFieldValueString(i, dbColumns[6]);
                    Obj[8] = retObj.getFieldValueString(i, dbColumns[7]);
                    Obj[9] = retObj.getFieldValueString(i, dbColumns[8]);
                    Obj[10] = retObj.getFieldValueString(i, dbColumns[9]);
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
            if (timeDetails != null && timeDetails.size() != 0) {
                Obj = new String[2];
                Obj[0] = String.valueOf(timeDetails.get(0));
                Obj[1] = String.valueOf(timeDetails.get(1));

                for (int i = 0; i < ptMasterChildTags.length; i++) {
                    child = new Element(ptMasterChildTags[i]);
                    child.setText(Obj[i]);
                    ptMasterEle.addContent(child);
                }
                pTimeEle.addContent(ptMasterEle);
            }
            if (timeDimHashMap != null) {
                Set details = timeDimHashMap.keySet();
                Iterator it = details.iterator();
                while (it.hasNext()) {
                    Obj = new String[6];
                    String key = (String) it.next();

                    timeDetailsFromHashMap = (ArrayList) timeDimHashMap.get(key);


                    Obj[0] = String.valueOf(timeDetailsFromHashMap.get(2));
                    Obj[1] = key;
                    Obj[2] = String.valueOf(timeDetailsFromHashMap.get(3));
                    Obj[3] = String.valueOf(timeDetailsFromHashMap.get(4));
                    Obj[4] = "N";
                    Obj[5] = "Current Date";

                    ptDetailsEle = new Element("ptDetails");

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

    public Element buildQueryDetails(Element root) throws Exception {
        String[] Obj = null;
        Element viewBysEle = new Element("progen_viewbys");
        Element table = new Element("progen_table");

//        Element graph = new Element("graphs");
//        Element measuresEle = null;
        Element child = null;

        String[] barChartColumnNames = null;
        String[] barChartColumnTitles = null;
        String[] dispcols = null;
        String[] oricols = null;
        String[] rep = null;
        String[] cep = null;
        String[] viewBys = null;
        String[] axis = null;

//        String aggTypeQuery = null;
//        PbReturnObject grppbro = null;
        String viewById = "";
        //code to rep elements
        if (REP_Elements != null && REP_Elements.size() != 0) {
            rep = (String[]) REP_Elements.toArray(new String[0]);
            String repp[] = (String[]) REP_Elements_Names.toArray(new String[0]);
            for (int k = 0; k < rep.length; k++) {
                Element REPele = new Element("REP");
                Element REPeleid1 = new Element("Seq_no");
                REPeleid1.setText(String.valueOf(k + 1));
                REPele.addContent(REPeleid1);
                Element REPeleid = new Element("REP_id");
                REPeleid.setText(rep[k]);
                REPele.addContent(REPeleid);
                Element REPelename = new Element("REP_name");
                REPelename.setText(repp[k]);
                REPele.addContent(REPelename);
                Element REPelename1 = new Element("viewBy_id");

                String viewIdQ = "select PRG_AR_REPORT_PARAM_DETLS_SEQ.nextval from dual";
                PbReturnObject idObj = execSelectSQL(viewIdQ);
                int id = idObj.getFieldValueInt(0, 0);
                viewById = "" + id + "";
                REPelename1.setText(viewById);
                REPele.addContent(REPelename1);
                viewBysEle.addContent(REPele);
            }
        }

        //for cross tab
        /*
         * if (CEP_Elements != null && CEP_Elements.size() != 0) { cep =
         * (String[]) CEP_Elements.toArray(new String[0]); String repp[] =
         * (String[]) CEP_Elements_Names.toArray(new String[0]); for (int k = 0;
         * k < cep.length; k++) { Element CEPele = new Element("CEP"); Element
         * CEPeleid1 = new Element("Seq_no"); CEPeleid1.setText(String.valueOf(k
         * + 1)); CEPele.addContent(CEPeleid1); Element CEPeleid = new
         * Element("CEP_id"); CEPeleid.setText(rep[k]);
         * CEPele.addContent(CEPeleid); Element CEPelename = new
         * Element("CEP_name"); CEPelename.setText(repp[k]);
         * CEPele.addContent(CEPelename); Element CEPelename1 = new
         * Element("viewBy_id");
         *
         * String viewIdQ = "select PRG_AR_REPORT_PARAM_DETLS_SEQ.nextval from
         * dual"; PbReturnObject idObj = execSelectSQL(viewIdQ); int id =
         * idObj.getFieldValueInt(0, 0); viewById = "" + id + "";
         * CEPelename1.setText(viewById); CEPele.addContent(CEPelename1);
         * viewBysEle.addContent(CEPele); } }
         */
        //code to add table details

        HashMap TableProperties = (HashMap) TableHashMap.get("TableProperties");
        if (TableProperties != null) {
            String DefaultSortedColumn = String.valueOf(TableProperties.get("DefaultSortedColumn"));
            String ShowTotalValues = String.valueOf(TableProperties.get("ShowTotalValues"));
            String ShowSubTotalValues = String.valueOf(TableProperties.get("ShowSubTotalValues"));
            String ShowAvgValues = String.valueOf(TableProperties.get("ShowAvgValues"));
            String ShowOvrAllMaxValues = String.valueOf(TableProperties.get("ShowOvrAllMaxValues"));
            String ShowOvrAllMinValues = String.valueOf(TableProperties.get("ShowOvrAllMinValues"));
            String ShowCatMaxValues = String.valueOf(TableProperties.get("ShowCatMaxValues"));
            String ShowCatMinValues = String.valueOf(TableProperties.get("ShowCatMinValues"));
            String ColumnSymbols = String.valueOf(TableProperties.get("ColumnSymbols"));

            Element DefaultSortedColumnEle = new Element("DefaultSortedColumn");
            DefaultSortedColumnEle.setText(String.valueOf(DefaultSortedColumn));
            table.addContent(DefaultSortedColumnEle);


            Element showTotaldetsEle = new Element("ShowTotalValues");
            showTotaldetsEle.setText(String.valueOf(ShowTotalValues));
            table.addContent(showTotaldetsEle);

            Element ShowSubTotalValuesEle = new Element("ShowSubTotalValues");
            ShowSubTotalValuesEle.setText(String.valueOf(ShowSubTotalValues));
            table.addContent(ShowSubTotalValuesEle);

            Element ShowAvgValuesEle = new Element("ShowAvgValues");
            ShowAvgValuesEle.setText(String.valueOf(ShowAvgValues));
            table.addContent(ShowAvgValuesEle);

            Element ShowOvrAllMaxValuesEle = new Element("ShowOvrAllMaxValues");
            ShowOvrAllMaxValuesEle.setText(String.valueOf(ShowOvrAllMaxValues));
            table.addContent(ShowOvrAllMaxValuesEle);

            Element ShowOvrAllMinValuesEle = new Element("ShowOvrAllMinValues");
            ShowOvrAllMinValuesEle.setText(String.valueOf(ShowOvrAllMinValues));
            table.addContent(ShowOvrAllMinValuesEle);

            Element ShowCatMaxValuesEle = new Element("ShowCatMaxValues");
            ShowCatMaxValuesEle.setText(String.valueOf(ShowCatMaxValues));
            table.addContent(ShowCatMaxValuesEle);

            Element ShowCatMinValuesEle = new Element("ShowCatMinValues");
            ShowCatMinValuesEle.setText(String.valueOf(ShowCatMinValues));
            table.addContent(ShowCatMinValuesEle);

            Element ColumnSymbolsEle = new Element("ColumnSymbols");
            ColumnSymbolsEle.setText(String.valueOf(ColumnSymbols));
            table.addContent(ColumnSymbolsEle);

            HashMap columnProperties = (HashMap) TableProperties.get("ColumnProperties");
            String[] rep1 = null;

            if (measures != null && measures.size() != 0) {
                rep1 = (String[]) measures.toArray(new String[0]);
                for (int k = 0; k < rep1.length; k++) {
                    Element ColumnPropertiesEle = new Element("ColumnProperties");
                    ArrayList colprops = (ArrayList) columnProperties.get("A_" + rep1[k]);
                    ////////.println("colprops---" + colprops);
                    child = new Element(colpropertiestags[0]);
                    child.setText(String.valueOf(rep1[k]));
                    ColumnPropertiesEle.addContent(child);
                    for (int i = 0; i < colpropertiestags.length - 1; i++) {
                        ////////.println("--colpr----" + i + "===" + colprops.get(i));
                        child = new Element(colpropertiestags[i + 1]);
                        child.setText(String.valueOf(colprops.get(i)));
                        ColumnPropertiesEle.addContent(child);
                    }
                    table.addContent(ColumnPropertiesEle);
                }
            }

            ////////.println("whatIfRanges in whatIfDesigner are::::: "+whatIfRanges);
            if (measures != null && measures.size() != 0) {
                rep1 = (String[]) measures.toArray(new String[0]);
                String[] Obj1 = new String[1];

                Element rqmater = new Element("RqMaster");
                for (int k = 0; k < rep1.length; k++) {
                    ////////.println(" rep1[k]==" + rep1[k]);
                    Obj1[0] = rep1[k];
                    String finalQuery = buildQuery(getReportQueryDetailsQuery, Obj1);
                    ////////.println("finalQuery in buildParameters is " + finalQuery);
                    PbReturnObject retObj = execSelectSQL(finalQuery);
                    String dbColumns[] = null;
                    Element rqdetail = new Element("RqDetails");
                    if (retObj != null && retObj.getRowCount() != 0) {
                        dbColumns = retObj.getColumnNames();
                        ////////.println("retObj.getRowCount() is:::::  udayyyyy " + retObj.getRowCount());
                        for (int i = 0; i < retObj.getRowCount(); i++) {

                            Obj = new String[11];
                            Obj[0] = String.valueOf(k + 1);
                            Obj[1] = retObj.getFieldValueString(i, dbColumns[0]);
                            Obj[2] = retObj.getFieldValueString(i, dbColumns[1]);
                            Obj[3] = retObj.getFieldValueString(i, dbColumns[2]);
                            Obj[4] = retObj.getFieldValueString(i, dbColumns[3]);
                            Obj[5] = retObj.getFieldValueString(i, dbColumns[4]);
                            Obj[6] = whatIfScenarioId;
                            Obj[7] = retObj.getFieldValueString(i, dbColumns[5]);
                            Obj[8] = retObj.getFieldValueString(i, dbColumns[6]);
                            Obj[9] = "Y";
                            Obj[10] = "1.0";

                            for (int j = 0; j < rpDetailChildTags.length; j++) {
                                child = new Element(rpDetailChildTags[j]);
                                child.setText(Obj[j]);
                                rqdetail.addContent(child);
                            }
                        }
                        rqmater.addContent(rqdetail);
                    }
                }
                root.addContent(rqmater);
            }
        }
        root.addContent(viewBysEle);
        root.addContent(table);
        return root;
    }

    public Element buildGraphDetails(Element root) throws Exception {

        Element pGraphEle = new Element("Graphs");
        Element pGraphMasterEle = null;
        Element seriesListEle = null;
//        Element xValues = null;
//        Element yValues = null;
        Element child = null;
        String Obj[] = null;
//        int count = 1;

        String[] barChartColumnNames = null;
        String[] pieChartColumnNames = null;
        String[] barChartColumnTitles = null;
        String[] viewByElementIds = null;
        String[] axis = null;
        HashMap graphDetails = null;

        try {

            for (int h = 0; h < graphIds.length; h++) {
                pGraphMasterEle = new Element("Graph");

                if (GraphHashMap.get(graphIds[h]) != null) {
                    graphDetails = (HashMap) GraphHashMap.get(graphIds[h]);

                    Obj = new String[20];
                    Obj[0] = graphIds[h];
                    Obj[1] = whatIfScenarioId;
                    if (graphDetails.get("graphName") != null) {
                        Obj[2] = (String) graphDetails.get("graphName");
                    } else {
                        Obj[2] = "";
                    }
                    if (graphDetails.get("graphSize") != null) {
                        Obj[3] = (String) graphDetails.get("graphSize");
                    } else {
                        Obj[3] = "";
                    }
                    if (graphDetails.get("graphTypeName") != null) {
                        Obj[4] = (String) graphDetails.get("graphTypeName");
                    } else {
                        Obj[4] = "";
                    }
                    if (graphDetails.get("graphClassName") != null) {
                        Obj[5] = (String) graphDetails.get("graphClassName");
                    } else {
                        Obj[5] = "";
                    }
                    if (graphDetails.get("graphId") != null) {
                        Obj[6] = (String) graphDetails.get("graphId");
                    } else {
                        Obj[6] = "";
                    }
                    if (graphDetails.get("graphLegend") != null) {
                        Obj[7] = (String) graphDetails.get("graphLegend");
                    } else {
                        Obj[7] = "";
                    }
                    if (graphDetails.get("graphLegendLoc") != null) {
                        Obj[8] = (String) graphDetails.get("graphLegendLoc");
                    } else {
                        Obj[8] = "";
                    }

                    Obj[9] = "Y";
                    Obj[10] = "Y";
                    if (graphDetails.get("graphRYaxislabel") != null) {
                        Obj[11] = (String) graphDetails.get("graphRYaxislabel");
                    } else {
                        Obj[11] = "";
                    }
                    if (graphDetails.get("graphLYaxislabel") != null) {
                        Obj[12] = (String) graphDetails.get("graphLYaxislabel");
                    } else {
                        Obj[12] = "";
                    }
                    if (graphDetails.get("graphBcolor") != null) {
                        Obj[13] = (String) graphDetails.get("graphBcolor");
                    } else {
                        Obj[13] = "";
                    }
                    if (graphDetails.get("graphFcolor") != null) {
                        Obj[14] = (String) graphDetails.get("graphFcolor");
                    } else {
                        Obj[14] = "";
                    }
                    if (graphDetails.get("graphDrill") != null) {
                        Obj[15] = (String) graphDetails.get("graphDrill");
                    } else {
                        Obj[15] = "";
                    }
                    if (graphDetails.get("graphData") != null) {
                        Obj[16] = (String) graphDetails.get("graphData");
                    } else {
                        Obj[16] = "";
                    }
                    if (graphDetails.get("graphHeight") != null) {
                        Obj[17] = (String) graphDetails.get("graphHeight");
                    } else {
                        Obj[17] = "";
                    }
                    if (graphDetails.get("graphWidth") != null) {
                        Obj[18] = (String) graphDetails.get("graphWidth");
                    } else {
                        Obj[18] = "";
                    }
                    Obj[19] = "";
                }

                for (int j = 0; j < graphMasterChildTags.length; j++) {
                    child = new Element(graphMasterChildTags[j]);
                    child.setText(Obj[j]);
                    pGraphMasterEle.addContent(child);
                }



                //Adding barchart_column_names
                seriesListEle = new Element("barChartColumnNames");
                barChartColumnNames = (String[]) graphDetails.get("barChartColumnNames");
                for (int j = 0; j < barChartColumnNames.length; j++) {
                    child = new Element("barChart_column_name");
                    child.setText(barChartColumnNames[j]);
                    seriesListEle.addContent(child);
                }
                pGraphMasterEle.addContent(seriesListEle);

                //adding pieChartColumn names
                seriesListEle = new Element("pieChartColumnNames");
                pieChartColumnNames = (String[]) graphDetails.get("pieChartColumns");
                for (int j = 0; j < pieChartColumnNames.length; j++) {
                    child = new Element("pieChart_column_name");
                    child.setText(pieChartColumnNames[j]);
                    seriesListEle.addContent(child);
                }
                pGraphMasterEle.addContent(seriesListEle);


                //Adding barchart_column_titles
                seriesListEle = new Element("barChartColumnTitles");
                barChartColumnTitles = (String[]) graphDetails.get("barChartColumnTitles");
                for (int j = 0; j < barChartColumnNames.length; j++) {
                    child = new Element("barChart_column_title");
                    child.setText(barChartColumnTitles[j]);
                    seriesListEle.addContent(child);
                }
                pGraphMasterEle.addContent(seriesListEle);

                //Adding viewBy_element_ids
                seriesListEle = new Element("viewByElementIds");
                viewByElementIds = (String[]) graphDetails.get("viewByElementIds");
                for (int j = 0; j < viewByElementIds.length; j++) {
                    child = new Element("viewBy_element_id");
                    ////////.println("viewByElementIds[j] in buildGraphDetails is::: "+viewByElementIds[j]);
                    child.setText(viewByElementIds[j]);
                    seriesListEle.addContent(child);
                }
                pGraphMasterEle.addContent(seriesListEle);
                pGraphEle.addContent(pGraphMasterEle);

            }
            root.addContent(pGraphEle);

        } catch (Exception exp) {
            logger.error("Exception:", exp);
        }
        return root;
    }
}
