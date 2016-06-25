package com.progen.scenariodesigner.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import oracle.jdbc.OraclePreparedStatement;
import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;
import prg.db.PbDb;
import prg.db.PbReturnObject;
import utils.db.ProgenConnection;

public class ScenarioDesigner extends PbDb {

    public static Logger logger = Logger.getLogger(ScenarioDesigner.class);
    ScenarioTemplateResourceBundle resBundle = new ScenarioTemplateResourceBundle();
    private HashMap ParametersHashMap = null;
    public ArrayList params = new ArrayList();
    public ArrayList timeDetails = new ArrayList();
    public ArrayList timeRangeDetails = new ArrayList();
    public LinkedHashMap timeDimHashMap = new LinkedHashMap();
    private HashMap TableHashMap = null;
    public ArrayList REP_Elements = new ArrayList();
    public ArrayList CEP_Elements = new ArrayList();
    public ArrayList measures = new ArrayList();
    public ArrayList measuresNames = new ArrayList();
    public Set total_list = new LinkedHashSet();
    public String paramsString = "";
    public String paramString1 = "";
    public String[] MetaInfoChildValues = null;
    public String[] MeasureInfoChildValues = null;
    private String scenarioName = null;
    private String scenarioDesc = null;
    public String models = "";
    ArrayList modelList = new ArrayList();
    public ArrayList REP_Names = new ArrayList();
    // public String[] sMasterChildTags = {"scnName", "scnDisplayName","scnTimeLevel","scnStartMonth","scnEndMonth","scnStatus","histStartMonth","histEndMonth"};
    public String[] sMasterChildTags = {"scnName", "scnDesc"};
    public String[] spDetailChildTags = {"element_id", "paramDispName", "childElementId", "dimId", "dimTabId", "displayType", "relLevel", "dispSeqNo", "defaultValue"};
    //public String[] stMasterChildTags = {"timeLevel", "timeType"};
    public String[] stMasterChildTags = {"historicalStartMonth", "historicalEndMonth", "scenarioStartMonth", "scenarioEndMonth"};
    public String[] stDetailsChildTags = {"timeColName", "timeColType", "timeFormSeq", "label", "defaultValue"};
    public String[] svrMasterDataChildTags = {"viewById", "svrdefaultValue"};
    public String[] sModelTags = {"modelId", "modelName", "modelDesc", "isSeeded"};
    public String[] rqDetailsChildTags = {"elementId", "elementName", "elementAgg"};
    public String[] rqMoreInfoChildTags = {"orderCol", "orderType", "totalRecord"};
    public String[] sMeasureChildTags = {"measureId", "measureName"};
    XMLOutputter serializer = null;
    Document document = null;
    private int scenarioId = 0;
    public String getScenarioParamDetailsQuery = resBundle.getString("getScenarioParamDetails");
    public String insertScenarioXMLQuery = resBundle.getString("insertScenarioXML");
    public String getSeededModels = resBundle.getString("getSeededModels");

    public void createDocument() throws Exception {
        try {
            Connection connection = ProgenConnection.getInstance().getConnection();
            OraclePreparedStatement opstmt = null;
            if (getParametersHashMap() != null) {
                if (getParametersHashMap().get("Parameters") != null) {
                    params = (ArrayList) getParametersHashMap().get("Parameters");
                }
                if (getParametersHashMap().get("TimeDetailstList") != null) {
                    timeDetails = (ArrayList) getParametersHashMap().get("TimeDetailstList");
                }
                if (getParametersHashMap().get("TimeDimHashMap") != null) {
                    timeDimHashMap = (LinkedHashMap) (HashMap) getParametersHashMap().get("TimeDimHashMap");
                }
                if (getParametersHashMap().get("TimeRangeDetails") != null) {
                    timeRangeDetails = (ArrayList) getParametersHashMap().get("TimeRangeDetails");
                }
                if (getParametersHashMap().get("SeededModels") != null) {
                    modelList = (ArrayList) getParametersHashMap().get("SeededModels");
                }
            }
            if (getTableHashMap() != null) {
                if (getTableHashMap().get("REP") != null) {
                    REP_Elements = (ArrayList) getTableHashMap().get("REP");
                } else {
                    if (params != null && params.size() != 0) {
                        REP_Elements.add(params.get(0));
                    }
                }
                if (getTableHashMap().get("CEP") != null) {
                    CEP_Elements = (ArrayList) getTableHashMap().get("CEP");
                }
                if (getTableHashMap().get("Measures") != null && getTableHashMap().get("MeasuresNames") != null) {
                    measures = (ArrayList) getTableHashMap().get("Measures");
                    measuresNames = (ArrayList) getTableHashMap().get("MeasuresNames");
                }

            }

            total_list = new LinkedHashSet();
            // //////.println("params are : "+params);
            ////////.println("paramsString before is : "+paramsString);
            for (int i = 0; i < params.size(); i++) {
                paramsString = paramsString + "," + String.valueOf(params.get(i)).replace("A_", "");
            }
            if (!(paramsString.equalsIgnoreCase(""))) {
                paramsString = paramsString.substring(1);
            }
            // //////.println("paramstring after is : "+paramsString);

            for (int i = 0; i < params.size(); i++) {
                paramString1 = paramString1 + "," + String.valueOf(params.get(i)).replace("A_", "") + "," + (i + 1);
            }

            if (!(paramString1.equalsIgnoreCase(""))) {
                paramString1 = paramString1.substring(1);
            }

            for (int i = 0; i < modelList.size(); i++) {
                models = models + "," + String.valueOf(modelList.get(i));
            }
            if (!(models.equalsIgnoreCase(""))) {
                models = models.substring(1);
            }

            Element root = new Element("scenario");
            root.setAttribute("version", "1.00001");
            root.setText("New Root");

            root = buildMetaInfo(root);
            root = buildParameters(root);
            root = buildTimeDetails(root);
            root = buildViewByDetails(root);
            root = buildSecViewByDetails(root);
            root = buildQueryDetails(root);
            root = buildMeasureDetails(root);

            document = new Document(root);
            serializer = new XMLOutputter();

            //////////////////////////////////////////.println.println(" serializer.outputString(document) " + serializer.outputString(document));

            opstmt = (OraclePreparedStatement) connection.prepareStatement(insertScenarioXMLQuery);
            opstmt.setInt(2, getScenarioId());
            opstmt.setStringForClob(1, serializer.outputString(document));

            int rows = opstmt.executeUpdate();

            if (connection != null) {
                connection.close();
            }
            if (opstmt != null) {
                opstmt.close();
            }

        } catch (Exception e) {
            logger.error("Exception:", e);
        }
    }

    public Element buildMetaInfo(Element root) {
        MetaInfoChildValues = new String[2];
        MetaInfoChildValues[0] = getScenarioName();
        MetaInfoChildValues[1] = getScenarioDesc();



        Element pMasterEle = new Element("sMaster");
        Element child = null;

        for (int i = 0; i < sMasterChildTags.length; i++) {
            child = new Element(sMasterChildTags[i]);
            child.setText(MetaInfoChildValues[i]);
            pMasterEle.addContent(child);
        }
        root.addContent(pMasterEle);

        return root;
    }

    public Element buildMeasureDetails(Element root) {
        MeasureInfoChildValues = new String[2];
        MeasureInfoChildValues[0] = (String) measures.get(0);
        MeasureInfoChildValues[1] = (String) measuresNames.get(0);

        Element pMasterEle = new Element("sMeasure");
        Element child = null;

        for (int i = 0; i < sMeasureChildTags.length; i++) {
            child = new Element(sMeasureChildTags[i]);
            child.setText(MeasureInfoChildValues[i]);
            pMasterEle.addContent(child);
        }
        root.addContent(pMasterEle);
        // //////.println(" end of buildMeasureDetails");

        return root;
    }

    public Element buildParameters(Element root) throws Exception {

        PbReturnObject retObj = null;
        String[] dbColumns = null;
        String finalQuery = "";
        String[] Obj = new String[2];
        Obj[0] = paramsString;
        Obj[1] = paramString1;

        Element pParameterEle = new Element("sParameter");
        Element ppDetailEle = null;
        Element child = null;
        int count = 1;

        String getRelLevels = resBundle.getString("getRelLevels");
        Object eleObj[] = new Object[1];
        eleObj[0] = paramsString;
        String fingetRelLevels = buildQuery(getRelLevels, eleObj);
        HashMap childs = new HashMap();
        //  //////.println(" fingetRelLevels " + fingetRelLevels);
        PbReturnObject chObj = execSelectSQL(fingetRelLevels);
        ArrayList dim = new ArrayList();
        for (int j = 0; j < chObj.getRowCount(); j++) {
            //  if(dim.contains(!chObj.getFieldValueString(j,"VAL_DIM_ID"))
            //        dim.add(dim);
            childs.put(chObj.getFieldValueString(j, "KEY_ELEMENT_ID"), chObj.getFieldValueString(j, "LEVEL1"));
        }
        try {

            finalQuery = buildQuery(getScenarioParamDetailsQuery, Obj);
            //    //////.println("finalQuery in buildParameters paramDet is " + finalQuery);
            retObj = execSelectSQL(finalQuery);

            //added by santhosh.kumar@porgenbusiness.com on 03/12/2009
           /*
             * if (timeDetails != null && timeDetails.size() != 0 &&
             * timeDimHashMap != null && timeDimHashMap.size() != 0) {
             * ppDetailEle = new Element("spDetail"); Obj = new String[10];
             * Obj[0] = "Time"; Obj[1] = "Time"; Obj[2] = ""; Obj[3] = "";
             * Obj[4] = ""; Obj[5] = "C"; Obj[6] = ""; Obj[7] =
             * String.valueOf((1)); Obj[8] = null; Obj[9] = "N";
             *
             * for (int j = 0; j < spDetailChildTags.length; j++) { child = new
             * Element(spDetailChildTags[j]); child.setText(Obj[j]);
             * ppDetailEle.addContent(child); }
             * pParameterEle.addContent(ppDetailEle); count = 2; }
             */

            if (retObj != null && retObj.getRowCount() != 0) {
                dbColumns = retObj.getColumnNames();
                for (int i = 0; i < retObj.getRowCount(); i++) {
                    ppDetailEle = new Element("spDetail");
                    Obj = new String[10];
                    Obj[0] = retObj.getFieldValueString(i, dbColumns[0]);
                    Obj[1] = retObj.getFieldValueString(i, dbColumns[1]);
                    Obj[2] = retObj.getFieldValueString(i, dbColumns[2]);
                    Obj[3] = retObj.getFieldValueString(i, dbColumns[3]);
                    Obj[4] = retObj.getFieldValueString(i, dbColumns[4]);
                    Obj[5] = "MultiSelectBox(With All)";
                    String level = "";
                    if (childs.containsKey(retObj.getFieldValueString(i, dbColumns[0]))) {
                        level = (String) childs.get(retObj.getFieldValueString(i, dbColumns[0]));
                    }
                    //    //////.println("level--"+level);
                    Obj[6] = level;
                    Obj[7] = String.valueOf((i + count));
                    Obj[8] = retObj.getFieldValueString(i, dbColumns[7]);
                    Obj[9] = retObj.getFieldValueString(i, dbColumns[8]);

                    for (int j = 0; j < spDetailChildTags.length; j++) {
                        child = new Element(spDetailChildTags[j]);
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

        Element pTimeEle = new Element("sTime");
        Element ptMasterEle = new Element("stMaster");
        Element ptDetailsEle = null;
        Element child = null;
        ArrayList timeDetailsFromHashMap = null;
        //  //////.println("timeRangeDetails is : "+timeRangeDetails);
        try {
            if (timeRangeDetails != null && timeRangeDetails.size() != 0) {
                Obj = new String[4];
                Obj[0] = String.valueOf(timeRangeDetails.get(0));
                Obj[1] = String.valueOf(timeRangeDetails.get(1));
                Obj[2] = String.valueOf(timeRangeDetails.get(2));
                Obj[3] = String.valueOf(timeRangeDetails.get(3));

                for (int i = 0; i < stMasterChildTags.length; i++) {
                    child = new Element(stMasterChildTags[i]);
                    child.setText(Obj[i]);
                    ptMasterEle.addContent(child);
                }
                pTimeEle.addContent(ptMasterEle);
            }

            //    //////.println("timeDimHashMap is : "+timeDimHashMap);

            if (timeDimHashMap != null) {
                Set details = timeDimHashMap.keySet();
                Iterator it = details.iterator();
                int l = 1;
                while (it.hasNext()) {
                    Obj = new String[5];
                    String key = (String) it.next();
                    //     //////.println(" time key " + key);

                    timeDetailsFromHashMap = (ArrayList) timeDimHashMap.get(key);
                    //    //////.println(" timeDetailsFromHashMap " + timeDetailsFromHashMap);
                    String label = "";
                    if (timeDetailsFromHashMap.size() > 7) {
                        label = String.valueOf(timeDetailsFromHashMap.get(7));
                    }
                    if (label == null || label.equalsIgnoreCase("null")) {
                        label = String.valueOf(timeDetailsFromHashMap.get(2));
                    }
                    //    //////.println(" label./- " + label);
                    Obj[0] = String.valueOf(timeDetailsFromHashMap.get(4));
                    Obj[1] = key;
//                    Obj[2] = "" + l + "";
                    Obj[2] = String.valueOf(l );
                    Obj[3] = label;
                    if (key.equalsIgnoreCase("PRG_PERIOD_TYPE")) {
                        Obj[4] = String.valueOf(timeDetailsFromHashMap.get(0));
                    } else {
                        Obj[4] = (String) timeRangeDetails.get(0);
                    }
                    l++;

                    ptDetailsEle = new Element("stDetails");

                    for (int i = 0; i < stDetailsChildTags.length; i++) {
                        child = new Element(stDetailsChildTags[i]);
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

    public Element buildViewByDetails(Element root) throws Exception {
        String[] Obj = null;
        Element sViewByEle = new Element("sViewBy");
        Element svrdefaultValueEle = null;
        Element svRowViewByEle = null;
        Element svColViewByEle = null;
        Element svrMasterDataEle = null;
        String viewById = "";
        String viewIdQ = "select SCENARIO_VIEW_ID_SEQ.nextval from dual";
        PbReturnObject idObj = execSelectSQL(viewIdQ);
        int id = idObj.getFieldValueInt(0, 0);
//        viewById = "" + id + "";
        viewById = Integer.toString(id);
        //  String viewByName=REP_Names.get(0);
        if (REP_Elements != null && REP_Elements.size() != 0) {
            svRowViewByEle = new Element("svRowViewBy");
            svrMasterDataEle = new Element("svrMasterData");
            for (int i = 0; i < REP_Elements.size(); i++) {
                Obj = new String[2];
                Obj[0] = viewById;
                //Obj[1] = String.valueOf((i + 1));
                //Obj[2] = String.valueOf((i + 1));
                Obj[1] = String.valueOf(REP_Elements.get(i));

                for (int j = 0; j < svrMasterDataChildTags.length; j++) {
                    svrdefaultValueEle = new Element(svrMasterDataChildTags[j]);
                    svrdefaultValueEle.setText(String.valueOf(Obj[j]));
                    svrMasterDataEle.addContent(svrdefaultValueEle);
                }
            }
            svRowViewByEle.addContent(svrMasterDataEle);
            sViewByEle.addContent(svRowViewByEle);
        }
        if (CEP_Elements != null && CEP_Elements.size() != 0) {
            svColViewByEle = new Element("svColViewBy");
            svrMasterDataEle = new Element("svrMasterData");
            for (int i = 0; i < CEP_Elements.size(); i++) {
                Obj = new String[1];
                //Obj[0] = reportId;
                //Obj[1] = String.valueOf((i + 1));
                //Obj[2] = String.valueOf((j + 1));
                Obj[0] = String.valueOf(CEP_Elements.get(i));

                for (int j = 0; j < svrMasterDataChildTags.length; j++) {
                    svrdefaultValueEle = new Element(svrMasterDataChildTags[j]);
                    svrdefaultValueEle.setText(String.valueOf(Obj[j]));
                    svrMasterDataEle.addContent(svrdefaultValueEle);
                }
            }
            svColViewByEle.addContent(svrMasterDataEle);
            sViewByEle.addContent(svColViewByEle);
        }
        root.addContent(sViewByEle);
        return root;
    }

    public Element buildQueryDetails(Element root) throws Exception {
        String[] Obj = null;
        Element rQueryEle = new Element("rQuery");
        Element pgMasterEle = null;
        Element rqDetailsEle = null;
        Element rqMoreInfoEle = null;
        Element child = null;

        String[] barChartColumnNames = null;
        String[] barChartColumnTitles = null;
        String[] viewBys = null;
        String[] axis = null;

        String aggTypeQuery = null;
        PbReturnObject grppbro = null;

        // if (displayType != null) {
        //    if (displayType.equalsIgnoreCase("Table") || displayType.equalsIgnoreCase("KPI Table")) {
        if (measures != null && measures.size() != 0) {
            //for (int i = 0; i < measures.size(); i++) {
            barChartColumnNames = (String[]) measures.toArray(new String[0]);
            barChartColumnTitles = (String[]) measuresNames.toArray(new String[0]);

            for (int k = 0; k < barChartColumnNames.length; k++) {
                rqDetailsEle = new Element("rqDetails");

                Obj = new String[4];

                Obj[0] = barChartColumnNames[k].replace("A_", "");
                Obj[1] = barChartColumnTitles[k].replace("A_", "");
                aggTypeQuery = "select nvl(aggregation_type,'SUM' ) from prg_user_all_info_details where element_id in (" + Obj[0] + ")";
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
            Obj[0] = barChartColumnNames[0];
            Obj[1] = "ASC";
            Obj[2] = "10";

            for (int l = 0; l < rqMoreInfoChildTags.length; l++) {
                child = new Element(rqMoreInfoChildTags[l]);
                child.setText(Obj[l]);
                rqMoreInfoEle.addContent(child);
            }
            // rQueryEle.addContent(rqMoreInfoEle);
            //}
        }
        // }
        //}
        root.addContent(rQueryEle);
        return root;
    }

    public Element buildSecViewByDetails(Element root) {

        PbReturnObject retObj = null;
        String[] dbColumns = null;
        String finalQuery = "";
        String[] Obj = new String[2];
        Obj[0] = models;


        Element pParameterEle = new Element("sModel");
        Element ppDetailEle = null;
        Element child = null;
        int count = 1;

        try {

            finalQuery = buildQuery(getSeededModels, Obj);
            retObj = execSelectSQL(finalQuery);

            if (retObj != null && retObj.getRowCount() != 0) {
                dbColumns = retObj.getColumnNames();
                for (int i = 0; i < retObj.getRowCount(); i++) {
                    ppDetailEle = new Element("smodDetail");
                    Obj = new String[10];
                    Obj[0] = retObj.getFieldValueString(i, dbColumns[0]);
                    Obj[1] = retObj.getFieldValueString(i, dbColumns[1]);
                    Obj[2] = retObj.getFieldValueString(i, dbColumns[2]);
                    Obj[3] = retObj.getFieldValueString(i, dbColumns[3]);

                    for (int j = 0; j < sModelTags.length; j++) {
                        child = new Element(sModelTags[j]);
                        child.setText(Obj[j]);
                        ppDetailEle.addContent(child);
                    }
                    pParameterEle.addContent(ppDetailEle);
                }
                root.addContent(pParameterEle);
            }

        } catch (SQLException exp) {
            logger.error("Exception:", exp);
        }catch (NullPointerException exp) {
            logger.error("Exception:", exp);
        }
        return root;
    }

    public HashMap getParametersHashMap() {
        return ParametersHashMap;
    }

    public void setParametersHashMap(HashMap ParametersHashMap) {
        this.ParametersHashMap = ParametersHashMap;
    }

    /**
     * @return the TableHashMap
     */
    public HashMap getTableHashMap() {
        return TableHashMap;
    }

    /**
     * @param TableHashMap the TableHashMap to set
     */
    public void setTableHashMap(HashMap TableHashMap) {
        this.TableHashMap = TableHashMap;
    }

    /**
     * @return the scenarioName
     */
    public String getScenarioName() {
        return scenarioName;
    }

    /**
     * @param scenarioName the scenarioName to set
     */
    public void setScenarioName(String scenarioName) {
        this.scenarioName = scenarioName;
    }

    /**
     * @return the scenarioDesc
     */
    public String getScenarioDesc() {
        return scenarioDesc;
    }

    /**
     * @param scenarioDesc the scenarioDesc to set
     */
    public void setScenarioDesc(String scenarioDesc) {
        this.scenarioDesc = scenarioDesc;
    }

    /**
     * @return the scenarioId
     */
    public int getScenarioId() {
        return scenarioId;
    }

    /**
     * @param scenarioId the scenarioId to set
     */
    public void setScenarioId(int scenarioId) {
        this.scenarioId = scenarioId;
    }

    public int generateScnId() throws Exception {
        int seq = 0;
        String seQ = resBundle.getString("getSequenceNumber");
        Object relSeqObj[] = new Object[1];
        relSeqObj[0] = "SCENARIO_ID_SEQ";
        String finalseQ = buildQuery(seQ, relSeqObj);
        seq = getSequenceNumber(finalseQ);

        return seq;
    }
}
