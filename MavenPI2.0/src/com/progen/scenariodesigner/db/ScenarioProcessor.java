package com.progen.scenariodesigner.db;

import com.progen.report.query.PbReportQuery;
import com.progen.scenarion.PbScenarioMaps;
import com.progen.xml.pbXmlUtilities;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import prg.db.PbReturnObject;
import utils.db.ProgenParam;

public class ScenarioProcessor extends PbScenarioMaps {

    public static Logger logger = Logger.getLogger(ScenarioProcessor.class);
    private String xmlDocument;
    public ProgenParam pParam = new ProgenParam();
    public HashMap metaInfo = new LinkedHashMap();
    public Document document = null;
    public Element root = null;
    public PbReportQuery pb = new PbReportQuery();
    public SAXBuilder builder = new SAXBuilder();
    pbXmlUtilities xmUtil = new pbXmlUtilities();
    private Reader characterStream = null;
    public String scenarioId = "";
    public String scenarioName = "";
    public String dimensionName = "";
    public String modelNamesStr = "";
    public String minTimeLevel = "";
    public String dimensionId = "";
    public String viewBy = "";

    public void processDocument() throws Exception {
        // //////.println("start of process document");
        scenarioParameters = new LinkedHashMap();
        scenarioParametersValues = new LinkedHashMap();
        scenarioModels = new LinkedHashMap();
        scenarioViewByMap = new LinkedHashMap();
        scenarioViewByMain = new LinkedHashMap();
        timeDetailsMap = new LinkedHashMap();
        timeDetailsArray = new ArrayList();
        scenarioSecViewByMap = new LinkedHashMap();
        ParameterNamesHashMap = new LinkedHashMap();
        scenarioTableHashMap = new LinkedHashMap();


        document = builder.build(getCharacterStream());
        root = document.getRootElement();
        processMetainfo(root);
        processParameters(root);
        processTimeDeatils(root);
        processViewByDetails(root);
        processSecondaryViewBy(root);
        processQueryDetails(root);
        processMeasureDetails(root);

        pb.setQryColumns(scenarioQryElementIds);
        pb.setColAggration(scenarioQryAggregations);
        pb.setTimeDetails(timeDetailsArray);
        pb.setRowViewbyCols(scenarioRowViewbyValues);
        pb.setColViewbyCols(scenarioColViewbyValues);
        //  //////.println("end of process document");

    }

    public void processDocument2() throws Exception {
        scenarioParameters = new LinkedHashMap();
        scenarioParametersValues = new LinkedHashMap();
        scenarioModels = new LinkedHashMap();
        scenarioViewByMap = new LinkedHashMap();
        scenarioViewByMain = new LinkedHashMap();
        timeDetailsMap = new LinkedHashMap();
        timeDetailsArray = new ArrayList();
        scenarioSecViewByMap = new LinkedHashMap();
        ParameterNamesHashMap = new LinkedHashMap();
        scenarioTableHashMap = new LinkedHashMap();


        document = builder.build(getCharacterStream());
        root = document.getRootElement();
        processMetainfo2(root);
        processParameters2(root);
        processTimeDeatils2(root);
        processViewByDetails2(root);
        processSecondaryViewBy2(root);
        processQueryDetails2(root);
        processMeasureDetails2(root);

        pb.setQryColumns(scenarioQryElementIds);
        pb.setColAggration(scenarioQryAggregations);
        pb.setTimeDetails(timeDetailsArray);
        pb.setRowViewbyCols(scenarioRowViewbyValues);
        pb.setColViewbyCols(scenarioColViewbyValues);

    }

    public void processParameters(Element root) {
        //  //////.println("start of processParameters ");
        List row = root.getChildren("sParameter");//Only one row as of now         
        /*
         * Start of Processing of parameters
         */
        for (int i = 0; i < row.size(); i++) {
            Element Companyname = (Element) row.get(i);
            List paramRow = Companyname.getChildren("spDetail");
            for (int j = 0; j < paramRow.size(); j++) {
                ArrayList paramInfo = new ArrayList();
                Element paramElement = (Element) paramRow.get(j);
                paramInfo.add(xmUtil.getXmlTagValue(paramElement, "element_id"));
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

                scenarioParameters.put(xmUtil.getXmlTagValue(paramElement, "element_id"), paramInfo);
                scenarioParametersValues.put(xmUtil.getXmlTagValue(paramElement, "element_id"), String.valueOf(paramInfo.get(8)));
                ParameterNamesHashMap.put(String.valueOf(xmUtil.getXmlTagValue(paramElement, "element_id")), xmUtil.getXmlTagValue(paramElement, "paramDispName").toString());
            }
            //////////////////////////////////////////.println.println(" in processor scenarioParameters " + scenarioParameters);
            //////////////////////////////////////////.println.println(" scenarioParametersValues " + scenarioParametersValues);
            //////////////////////////////////////////.println.println(" ParameterNamesHashMap " + ParameterNamesHashMap);

        }//End Loop for One section under portlet
        // //////.println("end of processParameters ");

    }

    public void processParameters2(Element root) {
        List row = root.getChildren("sParameter");//Only one row as of now
        /*
         * Start of Processing of parameters
         */
        for (int i = 0; i < row.size(); i++) {
            Element Companyname = (Element) row.get(i);
            List paramRow = Companyname.getChildren("spDetail");
            for (int j = 0; j < paramRow.size(); j++) {

                Element paramElement = (Element) paramRow.get(j);
                if (this.dimensionId.equalsIgnoreCase(xmUtil.getXmlTagValue(paramElement, "dimId"))) {
                    ArrayList paramInfo = new ArrayList();
                    paramInfo.add(xmUtil.getXmlTagValue(paramElement, "element_id"));
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

                    scenarioParameters.put(xmUtil.getXmlTagValue(paramElement, "element_id"), paramInfo);
                    scenarioParametersValues.put(xmUtil.getXmlTagValue(paramElement, "element_id"), String.valueOf(paramInfo.get(8)));
                    ParameterNamesHashMap.put(String.valueOf(xmUtil.getXmlTagValue(paramElement, "element_id")), xmUtil.getXmlTagValue(paramElement, "paramDispName").toString());

                }
            }
            //////////////////////////////////////////.println.println(" in processor scenarioParameters " + scenarioParameters);
            //////////////////////////////////////////.println.println(" scenarioParametersValues " + scenarioParametersValues);
            //////////////////////////////////////////.println.println(" ParameterNamesHashMap " + ParameterNamesHashMap);

        }//End Loop for One section under portlet

    }

    public Reader getCharacterStream() {
        return characterStream;
    }

    public void processMetainfo(Element root) {
        List row = root.getChildren("pMaster");//Only one row as of now

        /*
         * Start of Processing of parameters
         */
        for (int i = 0; i < row.size(); i++) {
            Element Companyname = (Element) row.get(i);

            metaInfo.put("ScenarioName", xmUtil.getXmlTagValue(Companyname, "scnName"));
            metaInfo.put("ScenarioDesc", xmUtil.getXmlTagValue(Companyname, "scnDesc"));

        }//End Loop for One section under portlet
        // //////.println("end of processMetainfo");
    }

    public void processMetainfo2(Element root) {
        List row = root.getChildren("pMaster");//Only one row as of now

        /*
         * Start of Processing of parameters
         */
        for (int i = 0; i
                < row.size(); i++) {
            Element Companyname = (Element) row.get(i);

            metaInfo.put("ScenarioName", xmUtil.getXmlTagValue(Companyname, "scnName"));
            metaInfo.put("ScenarioDesc", xmUtil.getXmlTagValue(Companyname, "scnDesc"));

        }//End Loop for One section under portlet

    }

    public void processTimeDeatils(Element root) {
        //  //////.println("start of processTimeDeatils ");

        timeDetailsMap = (LinkedHashMap) new LinkedHashMap();
        timeDetailsArray = new ArrayList();
        /*
         * Time Processing starts
         */

        List row = root.getChildren("sTime");//Only one row as of now
        /*
         * Start of Processing of parameters
         */
        for (int i = 0; i < row.size(); i++) {//Loop for section two under portlet
            Element Companyname = (Element) row.get(i);
            List timeMasterRow = Companyname.getChildren("stMaster");
            for (int j = 0; j < timeMasterRow.size(); j++) {
                Element paramElement = (Element) timeMasterRow.get(j);
                timeDetailsArray.add(xmUtil.getXmlTagValue(paramElement, "historicalStartMonth"));
                timeDetailsArray.add(xmUtil.getXmlTagValue(paramElement, "historicalEndMonth"));
                timeDetailsArray.add(xmUtil.getXmlTagValue(paramElement, "scenarioStartMonth"));
                timeDetailsArray.add(xmUtil.getXmlTagValue(paramElement, "scenarioEndMonth"));
            }

            List timeDetailsRow = Companyname.getChildren("stDetails");
            for (int j = 0; j < timeDetailsRow.size(); j++) {
                ArrayList timeInfo = new ArrayList();
                Element paramElement1 = (Element) timeDetailsRow.get(j);
                String temp = xmUtil.getXmlTagValue(paramElement1, "timeColType");
                //  //////.println("temp----"+temp);
                if (temp.equalsIgnoreCase("AS_OF_MONTH") || temp.equalsIgnoreCase("AS_OF_MONTH1")) {
                    timeInfo.add(timeDetailsArray.get(0));
                    timeInfo.add("CBO_" + temp);
                    timeInfo.add("Month");
                    timeInfo.add(xmUtil.getXmlTagValue(paramElement1, "timeColName"));
                    timeInfo.add(xmUtil.getXmlTagValue(paramElement1, "timeFormSeq"));
                    timeInfo.add("Month");
                    timeInfo.add(temp);
                    timeInfo.add(xmUtil.getXmlTagValue(paramElement1, "label"));
                } else if (temp.equalsIgnoreCase("AS_OF_YEAR")) {
                    timeInfo.add(timeDetailsArray.get(0));
                    timeInfo.add("CBO_" + temp);
                    timeInfo.add("From Year");
                    timeInfo.add(xmUtil.getXmlTagValue(paramElement1, "timeColName"));
                    timeInfo.add(xmUtil.getXmlTagValue(paramElement1, "timeFormSeq"));
                    timeInfo.add("Year");
                    timeInfo.add(temp);
                    timeInfo.add(xmUtil.getXmlTagValue(paramElement1, "label"));
                } else if (temp.equalsIgnoreCase("AS_OF_YEAR1")) {
                    timeInfo.add(timeDetailsArray.get(1));
                    timeInfo.add("CBO_" + temp);
                    timeInfo.add("To Year");
                    timeInfo.add(xmUtil.getXmlTagValue(paramElement1, "timeColName"));
                    timeInfo.add(xmUtil.getXmlTagValue(paramElement1, "timeFormSeq"));
                    timeInfo.add("Year");
                    timeInfo.add(temp);
                    timeInfo.add(xmUtil.getXmlTagValue(paramElement1, "label"));
                } else if (temp.equalsIgnoreCase("PRG_PERIOD_TYPE")) {
                    timeInfo.add("Month");
                    timeInfo.add("CBO_" + temp);
                    timeInfo.add("Aggregation");
                    timeInfo.add(xmUtil.getXmlTagValue(paramElement1, "timeColName"));
                    timeInfo.add(xmUtil.getXmlTagValue(paramElement1, "timeFormSeq"));
                    timeInfo.add("Month");
                    timeInfo.add(temp);
                    timeInfo.add(xmUtil.getXmlTagValue(paramElement1, "label"));
                }
                timeDetailsMap.put(timeInfo.get(6), timeInfo);

            }

        }//End of section 2 of collection
        //////////////////////////////////////////.println.println(" timeDetailsArray in sc p " + timeDetailsArray);
        //////////////////////////////////////////.println.println(" timeDetailsMap " + timeDetailsMap);
        // //////.println("end of processTimeDeatils");
    }

    public void processTimeDeatils2(Element root) {
        timeDetailsMap = (LinkedHashMap) new LinkedHashMap();
        timeDetailsArray = new ArrayList();
        /*
         * Time Processing starts
         */

        List row = root.getChildren("sTime");//Only one row as of now
        /*
         * Start of Processing of parameters
         */
        for (int i = 0; i < row.size(); i++) {//Loop for section two under portlet
            Element Companyname = (Element) row.get(i);
            List timeMasterRow = Companyname.getChildren("stMaster");
            for (int j = 0; j < timeMasterRow.size(); j++) {
                Element paramElement = (Element) timeMasterRow.get(j);
                timeDetailsArray.add(xmUtil.getXmlTagValue(paramElement, "historicalStartMonth"));
                timeDetailsArray.add(xmUtil.getXmlTagValue(paramElement, "historicalEndMonth"));
                timeDetailsArray.add(xmUtil.getXmlTagValue(paramElement, "scenarioStartMonth"));
                timeDetailsArray.add(xmUtil.getXmlTagValue(paramElement, "scenarioEndMonth"));
            }

            List timeDetailsRow = Companyname.getChildren("stDetails");
            for (int j = 0; j < timeDetailsRow.size(); j++) {
                ArrayList timeInfo = new ArrayList();
                Element paramElement1 = (Element) timeDetailsRow.get(j);
                String temp = xmUtil.getXmlTagValue(paramElement1, "timeColType");

                if (temp.equalsIgnoreCase("AS_OF_MONTH") || temp.equalsIgnoreCase("AS_OF_MONTH1")) {
                    if (temp.equalsIgnoreCase("AS_OF_MONTH")) {
                        timeInfo.add(timeDetailsArray.get(2));
                    } else {
                        timeInfo.add(timeDetailsArray.get(3));
                    }
                    timeInfo.add("CBO_" + temp);
                    timeInfo.add("Month");
                    timeInfo.add(xmUtil.getXmlTagValue(paramElement1, "timeColName"));
                    timeInfo.add(xmUtil.getXmlTagValue(paramElement1, "timeFormSeq"));
                    timeInfo.add("Month");
                    timeInfo.add(temp);
                    timeInfo.add(xmUtil.getXmlTagValue(paramElement1, "label"));
                } else if (temp.equalsIgnoreCase("PRG_PERIOD_TYPE")) {
                    timeInfo.add("Month");
                    timeInfo.add("CBO_" + temp);
                    timeInfo.add("Aggregation");
                    timeInfo.add(xmUtil.getXmlTagValue(paramElement1, "timeColName"));
                    timeInfo.add(xmUtil.getXmlTagValue(paramElement1, "timeFormSeq"));
                    timeInfo.add("Month");
                    timeInfo.add(temp);
                    timeInfo.add(xmUtil.getXmlTagValue(paramElement1, "label"));
                } else if (temp.equalsIgnoreCase("AS_OF_YEAR")) {
                    //  //////.println("timeDetailsArray.get(0)---"+timeDetailsArray.get(0));
                    timeInfo.add(timeDetailsArray.get(0));
                    timeInfo.add("CBO_" + temp);
                    timeInfo.add("Year");
                    timeInfo.add(xmUtil.getXmlTagValue(paramElement1, "timeColName"));
                    timeInfo.add(xmUtil.getXmlTagValue(paramElement1, "timeFormSeq"));
                    timeInfo.add("Year");
                    timeInfo.add(temp);
                    timeInfo.add(xmUtil.getXmlTagValue(paramElement1, "label"));
                } else if (temp.equalsIgnoreCase("AS_OF_YEAR1")) {
                    //   //////.println("timeDetailsArray.get(1)---"+timeDetailsArray.get(1));

                    timeInfo.add(timeDetailsArray.get(1));
                    timeInfo.add("CBO_" + temp);
                    timeInfo.add("Year");
                    timeInfo.add(xmUtil.getXmlTagValue(paramElement1, "timeColName"));
                    timeInfo.add(xmUtil.getXmlTagValue(paramElement1, "timeFormSeq"));
                    timeInfo.add("Year");
                    timeInfo.add(temp);
                    timeInfo.add(xmUtil.getXmlTagValue(paramElement1, "label"));
                }
                timeDetailsMap.put(timeInfo.get(6), timeInfo);

            }

        }//End of section 2 of collection
        //////////////////////////////////////////.println.println(" timeDetailsArray in sc p " + timeDetailsArray);
        //////////////////////////////////////////.println.println(" timeDetailsMap " + timeDetailsMap);

    }

    public void processSecondaryViewBy(Element root) {
        List row = root.getChildren("sModel");//Only one row as of now

        /*
         * Start of Processing of parameters
         */
        for (int i = 0; i < row.size(); i++) {//Loop for One section under portlet
            Element Companyname = (Element) row.get(i);

            List paramRow = Companyname.getChildren("smodDetail");
            for (int j = 0; j < paramRow.size(); j++) {
                ArrayList paramInfo = new ArrayList();
                Element paramElement = (Element) paramRow.get(j);
                paramInfo.add(xmUtil.getXmlTagValue(paramElement, "modelId"));
                paramInfo.add(xmUtil.getXmlTagValue(paramElement, "modelName"));
                paramInfo.add(xmUtil.getXmlTagValue(paramElement, "modelDesc"));
                paramInfo.add(xmUtil.getXmlTagValue(paramElement, "isSeeded"));
                //////////////////////////////////////////.println.println(" paramInfo mod " + paramInfo);

                scenarioModels.put(xmUtil.getXmlTagValue(paramElement, "modelId"), paramInfo);
            }
        }//End Loop for One section under portlet
        //////////////////////////////////////////.println.println("before scenarioModels is:: "+scenarioModels);
        // Get Non-Seeded Models and update ScenarioModels LinkedHashMap
        String qry = "select * from prg_custom_model_master where scenario_id=" + this.scenarioId + " order by 1";
        String nonSeededModelId = "";
        String nonSeededModelName = "";
        String nonSeededModelDesc = "";
        String isSeeded = "N";

        try {
            //////////////////////////////////////////.println.println("qry is:: "+qry);
            PbReturnObject retObject = execSelectSQL(qry);
            int count = retObject.getRowCount();
            if (count > 0) {
                for (int i = 0; i < count; i++) {
                    ArrayList nonSeededModels = new ArrayList();
                    nonSeededModelId = String.valueOf(retObject.getFieldValueInt(i, 0));
                    nonSeededModelName = retObject.getFieldValueString(i, 1);
                    nonSeededModelDesc = retObject.getFieldValueString(i, 1);
                    nonSeededModels.add(nonSeededModelId);
                    nonSeededModels.add(nonSeededModelName);
                    nonSeededModels.add(nonSeededModelDesc);
                    nonSeededModels.add(isSeeded);
                    scenarioModels.put(nonSeededModelId, nonSeededModels);
                }
            }
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        //////////////////////////////////////////.println.println("updated scenarioModels is:: " + scenarioModels);

    }

    public void processSecondaryViewBy2(Element root) {
        this.modelNamesStr = this.modelNamesStr.replace("'", "");
        String[] modelNamesArray = this.modelNamesStr.split(",");
        ArrayList modelNamesArrayList = new ArrayList();
        for (int i = 0; i < modelNamesArray.length; i++) {
            modelNamesArrayList.add(modelNamesArray[i]);
        }
        //////////////////////////////////////////.println.println("modelNamesArrayList is::::: "+modelNamesArrayList);
        List row = root.getChildren("sModel");//Only one row as of now

        /*
         * Start of Processing of parameters
         */
        for (int i = 0; i < row.size(); i++) {//Loop for One section under portlet
            Element Companyname = (Element) row.get(i);

            List paramRow = Companyname.getChildren("smodDetail");
            for (int j = 0; j < paramRow.size(); j++) {

                Element paramElement = (Element) paramRow.get(j);
                if (j == 0) {
                    ArrayList paramInfo = new ArrayList();
                    paramInfo.add(xmUtil.getXmlTagValue(paramElement, "modelId"));
                    paramInfo.add(xmUtil.getXmlTagValue(paramElement, "modelName"));
                    paramInfo.add(xmUtil.getXmlTagValue(paramElement, "modelDesc"));
                    paramInfo.add(xmUtil.getXmlTagValue(paramElement, "isSeeded"));
                    //////////////////////////////////////////.println.println(" paramInfo mod " + paramInfo);

                    scenarioModels.put(xmUtil.getXmlTagValue(paramElement, "modelId"), paramInfo);
                }
                if (modelNamesArrayList.contains(xmUtil.getXmlTagValue(paramElement, "modelName"))) {
                    ArrayList paramInfo = new ArrayList();
                    paramInfo.add(xmUtil.getXmlTagValue(paramElement, "modelId"));
                    paramInfo.add(xmUtil.getXmlTagValue(paramElement, "modelName"));
                    paramInfo.add(xmUtil.getXmlTagValue(paramElement, "modelDesc"));
                    paramInfo.add(xmUtil.getXmlTagValue(paramElement, "isSeeded"));
                    //////////////////////////////////////////.println.println(" paramInfo mod " + paramInfo);

                    scenarioModels.put(xmUtil.getXmlTagValue(paramElement, "modelId"), paramInfo);
                }
            }
        }//End Loop for One section under portlet
        //////////////////////////////////////////.println.println("before scenarioModels is:: "+scenarioModels);


        // Get Non-Seeded Models and update ScenarioModels LinkedHashMap
        String qry = "select * from prg_custom_model_master where scenario_id=" + this.scenarioId + " order by 1";
        String nonSeededModelId = "";
        String nonSeededModelName = "";
        String nonSeededModelDesc = "";
        String isSeeded = "N";

        try {
            //////////////////////////////////////////.println.println("qry is:: "+qry);
            PbReturnObject retObject = execSelectSQL(qry);
            int count = retObject.getRowCount();
            if (count > 0) {
                for (int i = 0; i < count; i++) {
                    if (modelNamesArrayList.contains(retObject.getFieldValueString(i, 1))) {
                        ArrayList nonSeededModels = new ArrayList();
                        nonSeededModelId = String.valueOf(retObject.getFieldValueInt(i, 0));
                        nonSeededModelName = retObject.getFieldValueString(i, 1);
                        nonSeededModelDesc = retObject.getFieldValueString(i, 1);
                        nonSeededModels.add(nonSeededModelId);
                        nonSeededModels.add(nonSeededModelName);
                        nonSeededModels.add(nonSeededModelDesc);
                        nonSeededModels.add(isSeeded);
                        scenarioModels.put(nonSeededModelId, nonSeededModels);
                    }
                }
            }
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        //////////////////////////////////////////.println.println("updated scenarioModels is:: " + scenarioModels);

    }

    public void processQueryDetails(Element root) {
        scenarioQryElementIds = new ArrayList();
        scenarioQryAggregations = new ArrayList();
        scenarioQryColNames = new ArrayList();

        List row = root.getChildren("rQuery");//Only one row as of now

        /*
         * Start of Processing of parameters
         */
        for (int i = 0; i
                < row.size(); i++) {//Loop for One section under portlet
            Element Companyname = (Element) row.get(i);


            List paramRow = Companyname.getChildren("rqDeatils");
            for (int j = 0; j
                    < paramRow.size(); j++) {

                Element paramElement = (Element) paramRow.get(j);

                scenarioQryElementIds.add(xmUtil.getXmlTagValue(paramElement, "elementId"));
                scenarioQryAggregations.add(xmUtil.getXmlTagValue(paramElement, "elementAgg"));
                scenarioQryColNames.add(xmUtil.getXmlTagValue(paramElement, "elementName"));

            }

        }//End Loop for One section under portlet

        //details for graph

    }

    public void processQueryDetails2(Element root) {
        scenarioQryElementIds = new ArrayList();
        scenarioQryAggregations = new ArrayList();
        scenarioQryColNames = new ArrayList();

        List row = root.getChildren("rQuery");//Only one row as of now

        /*
         * Start of Processing of parameters
         */
        for (int i = 0; i
                < row.size(); i++) {//Loop for One section under portlet
            Element Companyname = (Element) row.get(i);


            List paramRow = Companyname.getChildren("rqDeatils");
            for (int j = 0; j
                    < paramRow.size(); j++) {

                Element paramElement = (Element) paramRow.get(j);

                scenarioQryElementIds.add(xmUtil.getXmlTagValue(paramElement, "elementId"));
                scenarioQryAggregations.add(xmUtil.getXmlTagValue(paramElement, "elementAgg"));
                scenarioQryColNames.add(xmUtil.getXmlTagValue(paramElement, "elementName"));

            }

        }//End Loop for One section under portlet

        //details for graph

    }

    public void processMeasureDetails(Element root) {
        scenarioMeasureIds = new ArrayList();
        scenarioMeasureNames = new ArrayList();

        List row = root.getChildren("sMeasure");//Only one row as of now

        /*
         * Start of Processing of parameters
         */
        for (int i = 0; i < row.size(); i++) {//Loop for One section under portlet

            Element paramElement = (Element) row.get(i);

            scenarioMeasureIds.add(xmUtil.getXmlTagValue(paramElement, "measureId"));
            scenarioMeasureNames.add(xmUtil.getXmlTagValue(paramElement, "measureName"));
            scenarioTableHashMap.put("Measures", scenarioMeasureIds);
            scenarioTableHashMap.put("MeasuresNames", scenarioMeasureNames);
        }

    }

    public void processMeasureDetails2(Element root) {
        scenarioMeasureIds = new ArrayList();
        scenarioMeasureNames = new ArrayList();

        List row = root.getChildren("sMeasure");//Only one row as of now

        /*
         * Start of Processing of parameters
         */
        for (int i = 0; i < row.size(); i++) {//Loop for One section under portlet

            Element paramElement = (Element) row.get(i);

            scenarioMeasureIds.add(xmUtil.getXmlTagValue(paramElement, "measureId"));
            scenarioMeasureNames.add(xmUtil.getXmlTagValue(paramElement, "measureName"));
            scenarioTableHashMap.put("Measures", scenarioMeasureIds);
            scenarioTableHashMap.put("MeasuresNames", scenarioMeasureNames);
        }

    }

    public void processViewByDetails(Element root) {

        scenarioRowViewbyValues = new ArrayList();
        scenarioColViewbyValues = new ArrayList();
        scenarioViewByMap = new HashMap();
        List row = root.getChildren("sViewBy");//Only one row as of now

        /*
         * Start of Processing of parameters
         */
        for (int i = 0; i < row.size(); i++) {//Loop for One section under portlet
            Element Companyname = (Element) row.get(i);


            if (Companyname.getChildren("svRowViewBy") != null) {
                List paramRow = Companyname.getChildren("svRowViewBy");
                for (int j = 0; j < paramRow.size(); j++) {
                    ArrayList paramInfo = new ArrayList();
                    Element paramElement = (Element) paramRow.get(j);

                    List pViewByChild = paramElement.getChildren("svrMasterData");
                    for (int k = 0; k < pViewByChild.size(); k++) {
                        Element pViewByChildElement = (Element) pViewByChild.get(j);
                        paramInfo.add(xmUtil.getXmlTagValue(pViewByChildElement, "svrdefaultValue"));
                        paramInfo.add(xmUtil.getXmlTagValue(pViewByChildElement, "viewById"));

                        scenarioRowViewbyValues.add(xmUtil.getXmlTagValue(pViewByChildElement, "svrdefaultValue"));
                        scenarioViewByMap.put(xmUtil.getXmlTagValue(pViewByChildElement, "svrdefaultValue"), paramInfo);
                    }

                }
            }

            ////For col View By
            scenarioColViewbyValues.add("Time");

            // //////.println("scenarioRowViewbyValues in 1 is : "+scenarioRowViewbyValues);
            /*
             * if (Companyname.getChildren("pvColViewBy") != null) { List
             * paramRow = Companyname.getChildren("pvColViewBy"); for (int j =
             * 0; j < paramRow.size(); j++) { ArrayList paramInfo = new
             * ArrayList(); Element paramElement = (Element) paramRow.get(j);
             *
             * List pViewByChild = paramElement.getChildren("pvrMasterData");
             * for (int k = 0; k < pViewByChild.size(); k++) { Element
             * pViewByChildElement = (Element) pViewByChild.get(j);
             *
             * scenarioColViewbyValues.add(xmUtil.getXmlTagValue(pViewByChildElement,
             * "pvrdefaultValue")); }
             *
             * }
             * }
             */

        }//End Loop for One section under portlet

    }

    public void processViewByDetails2(Element root) {

        scenarioRowViewbyValues = new ArrayList();
        scenarioColViewbyValues = new ArrayList();
        scenarioViewByMap = new HashMap();
        List row = root.getChildren("sViewBy");//Only one row as of now

        /*
         * Start of Processing of parameters
         */
        for (int i = 0; i < row.size(); i++) {//Loop for One section under portlet
            Element Companyname = (Element) row.get(i);
            if (Companyname.getChildren("svRowViewBy") != null) {
                List paramRow = Companyname.getChildren("svRowViewBy");
                for (int j = 0; j < paramRow.size(); j++) {
                    ArrayList paramInfo = new ArrayList();
                    Element paramElement = (Element) paramRow.get(j);
                    List pViewByChild = paramElement.getChildren("svrMasterData");
                    for (int k = 0; k < pViewByChild.size(); k++) {
                        Element pViewByChildElement = (Element) pViewByChild.get(j);
                        paramInfo.add(xmUtil.getXmlTagValue(pViewByChildElement, "svrdefaultValue"));
                        paramInfo.add(xmUtil.getXmlTagValue(pViewByChildElement, "viewById"));
                        scenarioRowViewbyValues.add(xmUtil.getXmlTagValue(pViewByChildElement, "svrdefaultValue"));
                        scenarioViewByMap.put(xmUtil.getXmlTagValue(pViewByChildElement, "svrdefaultValue"), paramInfo);
                    }
                }
            }
            ////For col View By
            scenarioColViewbyValues.add("Time");
////////.println("scenarioRowViewbyValues in 2 is : "+scenarioRowViewbyValues);
            /*
             * if (Companyname.getChildren("pvColViewBy") != null) { List
             * paramRow = Companyname.getChildren("pvColViewBy"); for (int j =
             * 0; j < paramRow.size(); j++) { ArrayList paramInfo = new
             * ArrayList(); Element paramElement = (Element) paramRow.get(j);
             *
             * List pViewByChild = paramElement.getChildren("pvrMasterData");
             * for (int k = 0; k < pViewByChild.size(); k++) { Element
             * pViewByChildElement = (Element) pViewByChild.get(j);
             *
             * scenarioColViewbyValues.add(xmUtil.getXmlTagValue(pViewByChildElement,
             * "pvrdefaultValue")); }
             *
             * }
             * }
             */

        }//End Loop for One section under portlet

    }

    public void processSecViewByDetails(Element root) {
        scenarioColViewbyValues = new ArrayList();
        scenarioSecViewByMap = new HashMap();
        List row = root.getChildren("sViewBy");//Only one row as of now

        /*
         * Start of Processing of parameters
         */
        for (int i = 0; i
                < row.size(); i++) {//Loop for One section under portlet
            Element Companyname = (Element) row.get(i);


            if (Companyname.getChildren("svRowViewBy") != null) {
                List paramRow = Companyname.getChildren("svRowViewBy");
                for (int j = 0; j
                        < paramRow.size(); j++) {
                    ArrayList paramInfo = new ArrayList();
                    Element paramElement = (Element) paramRow.get(j);

                    List pViewByChild = paramElement.getChildren("svrMasterData");
                    for (int k = 0; k
                            < pViewByChild.size(); k++) {
                        Element pViewByChildElement = (Element) pViewByChild.get(j);
                        paramInfo.add(xmUtil.getXmlTagValue(pViewByChildElement, "svrdefaultValue"));
                        paramInfo.add(xmUtil.getXmlTagValue(pViewByChildElement, "viewById"));

                        scenarioRowViewbyValues.add(xmUtil.getXmlTagValue(pViewByChildElement, "svrdefaultValue"));
                        scenarioViewByMap.put(xmUtil.getXmlTagValue(pViewByChildElement, "svrdefaultValue"), paramInfo);
                    }

                }
            }

            ////For col View By
          /*
             * if (Companyname.getChildren("pvColViewBy") != null) { List
             * paramRow = Companyname.getChildren("pvColViewBy"); for (int j =
             * 0; j < paramRow.size(); j++) { ArrayList paramInfo = new
             * ArrayList(); Element paramElement = (Element) paramRow.get(j);
             *
             * List pViewByChild = paramElement.getChildren("pvrMasterData");
             * for (int k = 0; k < pViewByChild.size(); k++) { Element
             * pViewByChildElement = (Element) pViewByChild.get(j);
             *
             * scenarioColViewbyValues.add(xmUtil.getXmlTagValue(pViewByChildElement,
             * "pvrdefaultValue")); }
             *
             * }
             * }
             */

        }//End Loop for One section under portlet

    }

    public String getXmlDocument() {
        return xmlDocument;
    }

    /**
     * @param xmlDocument the xmlDocument to set
     */
    public void setXmlDocument(String xmlDocument) {
        this.xmlDocument = xmlDocument;
    }

    /**
     * @param characterStream the characterStream to set
     */
    public void setCharacterStream(Reader characterStream) {
        this.characterStream = characterStream;
    }

    public HashMap getScenarioMetaData() {
        return this.scenarioMetaData;
    }

    public HashMap getScenarioParameters() {
        return this.scenarioParameters;
    }

    public HashMap[] getScenarioViewBys() {
        return this.scenarioViewBys;
    }

    public LinkedHashMap getScenarioViewByMain() {
        return this.scenarioViewByMain;
    }

    public HashMap getScenarioViewByMap() {
        return this.scenarioViewByMap;
    }

    public LinkedHashMap getTimeDetailsMap() {
        return this.timeDetailsMap;
    }

    public ArrayList getTimeDetailsArray() {
        return this.timeDetailsArray;
    }

    @Override
    public HashMap getScenarioSecViewByMap() {
        return this.scenarioSecViewByMap;
    }
}
