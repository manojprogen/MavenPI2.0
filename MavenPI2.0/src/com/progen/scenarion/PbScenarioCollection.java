package com.progen.scenarion;

import com.progen.report.query.PbReportQuery;
import com.progen.scenario.display.DisplayScenarioParameters;
import com.progen.scenariodesigner.db.ScenarioProcessor;
import com.progen.xml.pbXmlUtilities;
import java.io.Reader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import prg.db.PbReturnObject;
import utils.db.ProgenConnection;
import utils.db.ProgenParam;

public class PbScenarioCollection extends PbScenarioMaps {

    public static Logger logger = Logger.getLogger(PbScenarioCollection.class);
    public String scenarioId;
    public String scenarioName = "";
    public String dimensionName = "";
    public String dimensionId = "";
    public String modelNamesStr = "";
    public String minTimeLevel = "";
    public String drillUrl = "";
    public String columnEdgeDrillUrl = "";
    public String primAnalyzeBy = "";
    public String secAnalyzeBy = "";
    public String userId = null;
    public String newCustomModelId = "";
    public String flag = "";
    public String scnMonths = "";
    public String viewBy = "";
    public Document document = null;
    public Element root = null;
    public SAXBuilder builder = new SAXBuilder();
    pbXmlUtilities xmUtil = new pbXmlUtilities();
    private Reader characterStream = null;
    PbScenarioCollectionResourceBundle resBundle = new PbScenarioCollectionResourceBundle();
    public static LinkedHashMap scenarioViewbyMainTemp = new LinkedHashMap();
    public static HashMap scenarioParamsTemp = new HashMap();
    public static LinkedHashMap timeDetsMapTemp = new LinkedHashMap();
    public static String drillurlTemp = "";
    public static LinkedHashMap scenarioModelsTemp = new LinkedHashMap();
    public static ArrayList timeDetsArrayList = new ArrayList();
    public static ArrayList NonViewByList = new ArrayList();
    public static HashMap NonViewByMap = new HashMap();
    public static PbReturnObject combinedRetObj = null;
    public PbReturnObject simpleRetObj = null;
//ScenarioProcessor scp = new ScenarioProcessor();
    public static HashMap normalHm = new HashMap();

    public String getMinTimeLevel() {
        return minTimeLevel;
    }

    /**
     * @param minTimeLevel the minTimeLevel to set
     */
    public void setMinTimeLevel(String minTimeLevel) {
        this.minTimeLevel = minTimeLevel;
    }

    /**
     * @return the drillUrl
     */
    public String getDrillUrl() {
        return drillUrl;
    }

    /**
     * @param drillUrl the drillUrl to set
     */
    public void setDrillUrl(String drillUrl) {
        this.drillUrl = drillUrl;
    }

    /**
     * @return the columnEdgeDrillUrl
     */
    public String getColumnEdgeDrillUrl() {
        return columnEdgeDrillUrl;
    }

    /**
     * @param columnEdgeDrillUrl the columnEdgeDrillUrl to set
     */
    public void setColumnEdgeDrillUrl(String columnEdgeDrillUrl) {
        this.columnEdgeDrillUrl = columnEdgeDrillUrl;
    }

    /**
     * @return the primAnalyzeBy
     */
    public String getPrimAnalyzeBy() {
        return primAnalyzeBy;
    }

    /**
     * @param primAnalyzeBy the primAnalyzeBy to set
     */
    public void setPrimAnalyzeBy(String primAnalyzeBy) {
        this.primAnalyzeBy = primAnalyzeBy;
    }

    /**
     * @return the secAnalyzeBy
     */
    public String getSecAnalyzeBy() {
        return secAnalyzeBy;
    }

    /**
     * @param secAnalyzeBy the secAnalyzeBy to set
     */
    public void setSecAnalyzeBy(String secAnalyzeBy) {
        this.secAnalyzeBy = secAnalyzeBy;
    }

    /**
     * @return the scenarioId
     */
    public String getScenarioId() {
        return scenarioId;
    }

    /**
     * @param scenarioId the scenarioId to set
     */
    public void setScenarioId(String scenarioId) {
        this.scenarioId = scenarioId;
    }

    public PbReturnObject setScenarioViewByQuery(String key, HashMap hm, String currElementId, HashMap paramMap, String flag) {
//        String str = "";
        PbReturnObject pbro = new PbReturnObject();

        if (key.equalsIgnoreCase("CBOVIEW_BY_NAME")) {
            String cols[] = {"ELEMENT_ID", "PARAM_DISP_NAME", "REL_LEVEL"};
            pbro.setColumnNames(cols);
            String mapKey[] = (String[]) paramMap.keySet().toArray(new String[0]);
            for (int m = 0; m < mapKey.length; m++) {
                String ekey = mapKey[m];
                ArrayList al = (ArrayList) paramMap.get(ekey);
                String pDispName = al.get(1).toString();
                String level = al.get(6).toString();
                pbro.setFieldValueString("ELEMENT_ID", ekey);
                pbro.setFieldValueString("PARAM_DISP_NAME", pDispName);
                pbro.setFieldValueString("REL_LEVEL", level);
                pbro.addRow();
            }
            primAnalyzeBy = currElementId;
        }
        if (key.equalsIgnoreCase("CBOVIEW_BY_NAME1")) {

            String cols[] = {"MODEL_ID", "MODEL_NAME"};
            pbro.setColumnNames(cols);
            String mapKey[] = (String[]) paramMap.keySet().toArray(new String[0]);
            for (int m = 0; m < mapKey.length; m++) {
                String ekey = mapKey[m];
                ArrayList al = (ArrayList) paramMap.get(ekey);
                String modName = al.get(1).toString();
                pbro.setFieldValueString("MODEL_ID", ekey);
                pbro.setFieldValueString("MODEL_NAME", modName);
                pbro.addRow();
            }
            if (flag.equalsIgnoreCase("analyzeScenario")) {
                pbro.setFieldValueString("MODEL_ID", "-1");
                pbro.setFieldValueString("MODEL_NAME", "Custom Model");
                pbro.addRow();
            }
        }
        pbro.writeString();
        return pbro;
    }

    public ArrayList getSelectedDates() {
        ArrayList selectedDates = new ArrayList();

        try {
            Connection con = ProgenConnection.getInstance().getConnection();
            String query="select to_char(target_start_date,'mm/dd/yyyy') as start_date,to_char(target_end_date,'mm/dd/yyyy') as end_date,"
                    + "min_time_level  from target_master where target_Id='?')";
            PreparedStatement st = con.prepareStatement(query);
            st.setString(1,this.scenarioId );
            PbReturnObject targetDates = new PbReturnObject(st.executeQuery());
     
            if (st != null) {
                st.close();
            }
            if (con != null) {
                con.close();
            }

            selectedDates.add(setStringValue(targetDates.getFieldValue(0, 0)));
            selectedDates.add(setStringValue(targetDates.getFieldValue(0, 1)));
            selectedDates.add(setStringValue(targetDates.getFieldValue(0, 2)));

        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        return selectedDates;
    }

    public ArrayList getSelectedMonths() {
        ArrayList selectedMonths = new ArrayList();
        try {
            Connection con = ProgenConnection.getInstance().getConnection();
            String query="select historical_st_month, historical_end_month,scenario_time_level from scenario_master where scenario_id='?'";
            PreparedStatement st = con.prepareStatement(query);
            st.setString(1,  this.scenarioId );
            PbReturnObject scnMonths = new PbReturnObject( st.executeQuery(query));
          
            if (st != null) {
                st.close();
            }
            if (con != null) {
                con.close();
            }

            selectedMonths.add(setStringValue(scnMonths.getFieldValue(0, 0)));
            selectedMonths.add(setStringValue(scnMonths.getFieldValue(0, 1)));
            selectedMonths.add(setStringValue(scnMonths.getFieldValue(0, 2)));


        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        return selectedMonths;
    }

    public ArrayList getSelectedQtrs() {
        ArrayList selectedQtrs = new ArrayList();
        try {
            Connection con = ProgenConnection.getInstance().getConnection();
            String query="select target_start_qtr,target_end_qtr,min_time_level  from target_master where target_Id='?'";
            PreparedStatement st = con.prepareStatement(query);
            st.setString(1,  this.scenarioId  );
            PbReturnObject targetQtrs = new PbReturnObject(st.executeQuery(query));
           
            if (st != null) {
                st.close();
            }
            if (con != null) {
                con.close();
            }

            selectedQtrs.add(setStringValue(targetQtrs.getFieldValue(0, 0)));
            selectedQtrs.add(setStringValue(targetQtrs.getFieldValue(0, 1)));
            selectedQtrs.add(setStringValue(targetQtrs.getFieldValue(0, 2)));


        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        return selectedQtrs;
    }

    public String setStringValue(Object obj) {
        if (obj == null) {
            return (null);
        } else {
            return (obj.toString());
        }
    }

    public ArrayList getSelectedYears() {
        ArrayList selectedYears = new ArrayList();
        try {
            Connection con = ProgenConnection.getInstance().getConnection();
            
            String query="select historical_st_month, historical_end_month,scenario_time_level from scenario_master where scenario_id='? '";
            PreparedStatement st = con.prepareStatement(query);
            st.setString(1,  this.scenarioId  );
            PbReturnObject targetYears = new PbReturnObject(st.executeQuery(query));
           
            if (st != null) {
                st.close();
            }
            if (con != null) {
                con.close();
            }

            selectedYears.add(setStringValue(targetYears.getFieldValue(0, 0)));
            selectedYears.add(setStringValue(targetYears.getFieldValue(0, 1)));
            selectedYears.add(setStringValue(targetYears.getFieldValue(0, 2)));


        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        return selectedYears;
    }

    public ArrayList getSelectedYears2(String scenarioId) {
        ArrayList selectedYears = new ArrayList();
        try {
            Connection con = ProgenConnection.getInstance().getConnection();
            String query="select historical_st_month, historical_end_month,scenario_time_level from scenario_master where scenario_id='?'";
            PreparedStatement st = con.prepareStatement(query);
            st.setString(1,  scenarioId  );
            PbReturnObject targetYears = new PbReturnObject(st.executeQuery(query));
            
            if (st != null) {
                st.close();
            }
            if (con != null) {
                con.close();
            }

            selectedYears.add(setStringValue(targetYears.getFieldValue(0, 0)));
            selectedYears.add(setStringValue(targetYears.getFieldValue(0, 1)));
            selectedYears.add(setStringValue(targetYears.getFieldValue(0, 2)));


        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        return selectedYears;
    }

    @Override
    public HashMap getScenarioMetaData() {
        return this.getScenarioMetaData();
    }

    @Override
    public HashMap getScenarioParameters() {
        return this.scenarioParameters;
    }

    @Override
    public HashMap[] getScenarioViewBys() {
        return this.scenarioViewBys;
    }

    @Override
    public LinkedHashMap getScenarioViewByMain() {
        return this.scenarioViewByMain;
    }

    public Reader getCharacterStream() {
        return characterStream;
    }

    public void getParamMetaData() throws Exception {

        PbReturnObject retObj = null;
        Object[] Obj = null;
        String[] colNames;
        String temp = "";
        String finalQuery = "";
        scenarioParameters = new HashMap();
        scenarioParametersValues = new HashMap();
        scenarioModels = new LinkedHashMap();
        scenarioTableHashMap = new LinkedHashMap();
        this.completeUrl = "";

        //This will get parameters hashMap if Scenario Id is provided
        /**
         * Code for Parameter Hash Map *
         */
        String sqlstr = "";
        sqlstr = resBundle.getString("getScenarioDetails");
        Obj = new Object[1];
        Obj[0] = this.scenarioId;

        finalQuery = buildQuery(sqlstr, Obj);
        retObj = execSelectSQL(finalQuery);
        Connection con = ProgenConnection.getInstance().getConnection();
        Statement st = con.createStatement();
        Reader characterStream = null;
        ResultSet rs = st.executeQuery(finalQuery);
        while (rs.next()) {
            if (rs.getClob("xml_path") != null) {
                characterStream = rs.getClob("xml_path").getCharacterStream();
            } else {
                characterStream = null;
            }
        }
        if(rs!=null){
                rs.close();
            }
            if(st!=null){
                st.close();
            }
        if (con != null) {
            con.close();
        }
        ScenarioProcessor scp = new ScenarioProcessor();
        scp.setCharacterStream(characterStream);
        scp.scenarioId = this.scenarioId;
        scp.processDocument();

        scenarioParameters = scp.scenarioParameters;
        scenarioModels = scp.scenarioModels;
        ParameterNamesHashMap = scp.ParameterNamesHashMap;
        scenarioTableHashMap = scp.scenarioTableHashMap;
        scenarioParametersValues = scp.scenarioParametersValues;
        setScenarioModelsTemp(scenarioModels);
        String p[] = (String[]) scenarioParameters.keySet().toArray(new String[0]);
        for (int m = 0; m < p.length; m++) {
            String key = "CBOARP" + p[m];
            String key2 = p[m];
            if (scenarioParametersValues.containsKey(key2)) {

                if (scenarioIncomingParameters.containsKey(key)) {
                    String val = (String) scenarioIncomingParameters.get(key);
                    if (!val.equalsIgnoreCase("All")) {
                        //scenarioParametersValues.remove(key2);
                        scenarioParametersValues.put(key2, val);
                        ArrayList al2 = (ArrayList) scenarioParameters.get(key2);
                        al2.remove(8);
                        al2.add(8, val);
                        //scenarioParameters.remove(key2);
                        scenarioParameters.put(key2, al2);
                    }
                }
            }
        }

        colNames = retObj.getColumnNames();
//        int psize = retObj.getRowCount();
        String viewByID = "";
//        String viewByName = "";
        String secViewByID = "-1";
        String currViewBy = "";
        String nextViewBy = "";

        //boolean flag = false;

        scenarioViewByMain = new LinkedHashMap();
        scenarioViewByOrder = new LinkedHashMap();
        scenarioRowViewbyValues = new ArrayList();
        scenarioColViewbyValues = new ArrayList();

        scenarioRowViewbyValues = scp.scenarioRowViewbyValues;
        scenarioColViewbyValues = scp.scenarioColViewbyValues;
        scenarioViewByMain = scp.scenarioViewByMain;
        scenarioViewByOrder = scp.scenarioViewByOrder;

        String scnParamKeySet[] = (String[]) scenarioParameters.keySet().toArray(new String[0]);
        for (int i = 0; i < scnParamKeySet.length; i++) {
            ArrayList valuesArrayList = (ArrayList) scenarioParameters.get(scnParamKeySet[i]);
            if (scenarioIncomingParameters.get(valuesArrayList.get(9)) != null) {
                this.completeUrl += ";" + valuesArrayList.get(9) + "=" + scenarioIncomingParameters.get(valuesArrayList.get(9));
            } else {
                this.completeUrl += ";" + valuesArrayList.get(9) + "=" + valuesArrayList.get(8);
            }
        }



        HashMap scnViewByMap = new HashMap();
        scnViewByMap = scp.getScenarioViewByMap();
        String defV = (String) scnViewByMap.get(0);
        String defArr[] = (String[]) scnViewByMap.keySet().toArray(new String[0]);
        for (int m = 0; m < defArr.length; m++) {
            defV = defArr[m];
        }


        HashMap viewByDet = scp.getScenarioViewByMap();

        String val[] = (String[]) scenarioRowViewbyValues.toArray(new String[0]);
        ArrayList paramDet = new ArrayList();
        for (int looper = 0; looper < val.length; looper++) {
            ArrayList viewByDetList = new ArrayList();
            if (viewByDet.containsKey(val[looper])) {
                viewByDetList = (ArrayList) viewByDet.get(val[looper]);
            }
            //flag = true;
            ArrayList viewByArrayList = new ArrayList();
            viewByID = viewByDetList.get(1).toString();
            if (scnViewByMap.containsKey(val[looper])) {
                viewByArrayList.add("CBO" + "VIEW_BY_NAME");
            }
            viewByArrayList.add("CBOVIEW_BY" + viewByID);
            viewByArrayList.add("Analyze By");



            if (scenarioIncomingParameters.get("CBOVIEW_BY" + viewByID) != null) {
                scenarioRowViewbyValues = new ArrayList();
                scenarioRowViewbyValues.add(scenarioIncomingParameters.get("CBOVIEW_BY" + viewByID));
                viewByArrayList.add(scenarioIncomingParameters.get("CBOVIEW_BY" + viewByID));
                paramDet = (ArrayList) scenarioParameters.get(scenarioIncomingParameters.get("CBOVIEW_BY" + viewByID));
                viewByArrayList.add(paramDet.get(1));
                this.completeUrl += ";" + viewByArrayList.get(1) + "=" + viewByArrayList.get(3);
            } else {
                viewByArrayList.add(defV);
                paramDet = (ArrayList) scenarioParameters.get(defV);
                viewByArrayList.add(paramDet.get(1));
                this.completeUrl += ";" + viewByArrayList.get(1) + "=" + viewByArrayList.get(3);
            }
            scenarioViewByMain.put(viewByID, viewByArrayList);


            if (looper == 0) {
                if (scenarioIncomingParameters.get("CBOVIEW_BY" + viewByID) != null) {
                    currViewBy = scenarioIncomingParameters.get("CBOVIEW_BY" + viewByID).toString();
                }

                nextViewBy = getChildElementId(currViewBy, scenarioId, scenarioParameters);
                if (nextViewBy != null) {
                    drillUrl += "&CBOVIEW_BY" + viewByID + "=" + nextViewBy + "";
                }
                String dVal = "";

                if (scenarioIncomingParameters.containsKey("CBOARP" + currViewBy)) {
                    dVal = scenarioIncomingParameters.get("CBOARP" + currViewBy).toString();
                }

                drillUrl += "&CBOARP" + currViewBy + "=";
            }
        }


        //code for building Model drop-down
        ArrayList secViewByArrayList = new ArrayList();
        String modelId = "";
        secViewByArrayList.add("CBO" + "VIEW_BY_NAME1");
        secViewByArrayList.add("CBOVIEW_BY" + secViewByID);
        secViewByArrayList.add("Model");
        if (scenarioIncomingParameters.get("CBOVIEW_BY" + secViewByID) != null) {
            modelId = (String) scenarioIncomingParameters.get("CBOVIEW_BY" + secViewByID);
            if (modelId.equalsIgnoreCase("-1")) {
                paramDet = (ArrayList) scenarioModels.get(newCustomModelId);
                //   //
                if (paramDet == null) {
//                    secViewByArrayList.add("0");
//                    secViewByArrayList.add("--select--");
                    secViewByArrayList.add("1");
                    secViewByArrayList.add("Last Two Years Average");
                } else {
                    secViewByArrayList.add(newCustomModelId);
                    secViewByArrayList.add(paramDet.get(1));
                }
            } else {
                secViewByArrayList.add(scenarioIncomingParameters.get("CBOVIEW_BY" + secViewByID));
                paramDet = (ArrayList) scenarioModels.get(scenarioIncomingParameters.get("CBOVIEW_BY" + secViewByID));
                //   //
                secViewByArrayList.add(paramDet.get(1));
            }

            this.completeUrl += ";" + secViewByArrayList.get(1) + "=" + secViewByArrayList.get(3);
        } else {
//            secViewByArrayList.add("0");
//            secViewByArrayList.add("--select--");
            secViewByArrayList.add("1");
            secViewByArrayList.add("Last Two Years Average");
            this.completeUrl += ";" + secViewByArrayList.get(1) + "=" + secViewByArrayList.get(3);
        }
        scenarioViewByMain.put(secViewByID, secViewByArrayList);
        // end of code


        /////////////Code for Time Setup/////////
        setScenarioViewbyMainTemp(scenarioViewByMain);
        setScenarioParamsTemp(scenarioParameters);

        sqlstr = "";
        try {
            finalQuery = buildQuery(sqlstr, Obj);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }

        ProgenParam pParam = new ProgenParam();
        timeDetailsMap = new LinkedHashMap();
        timeDetailsArray = new ArrayList();
        timeDetailsMap = scp.timeDetailsMap;
        timeDetailsArray = scp.timeDetailsArray;


        setTimeDetsMapTemp(timeDetailsMap);
        setDrillurlTemp(drillUrl);

        String timeDet[] = (String[]) timeDetailsMap.keySet().toArray(new String[0]);
        for (int i = 0; i < timeDet.length; i++) {
            ArrayList timeInfo = (ArrayList) timeDetailsMap.get(timeDet[i]);
            temp = timeDet[i];
            if (scenarioIncomingParameters.get(("CBO_" + temp)) != null) {
                if (temp.equalsIgnoreCase("AS_OF_MONTH")) {
                    timeInfo.set(0, scenarioIncomingParameters.get(("CBO_" + temp)));
                    this.completeUrl += ";" + timeInfo.get(1) + "=" + timeInfo.get(0);
                } else if (temp.equalsIgnoreCase("AS_OF_MONTH1")) {
                    timeInfo.set(0, scenarioIncomingParameters.get(("CBO_" + temp)));
                    this.completeUrl += ";" + timeInfo.get(1) + "=" + timeInfo.get(0);
                } else if (temp.equalsIgnoreCase("PRG_PERIOD_TYPE")) {
                    timeInfo.set(0, scenarioIncomingParameters.get(("CBO_" + temp)));
                    this.completeUrl += ";" + timeInfo.get(1) + "=" + timeInfo.get(0);
                } else if (temp.equalsIgnoreCase("AS_OF_YEAR")) {
                    timeInfo.set(0, scenarioIncomingParameters.get(("CBO_" + temp)));
                    this.completeUrl += ";" + timeInfo.get(1) + "=" + timeInfo.get(0);
                } else if (temp.equalsIgnoreCase("AS_OF_YEAR1")) {
                    timeInfo.set(0, scenarioIncomingParameters.get(("CBO_" + temp)));
                    this.completeUrl += ";" + timeInfo.get(1) + "=" + timeInfo.get(0);
                }
            } else {
                if (temp.equalsIgnoreCase("AS_OF_MONTH")) {
                    timeInfo.set(0, timeDetailsArray.get(0));
                    this.completeUrl += ";" + timeInfo.get(1) + "=" + timeDetailsArray.get(0);
                } else if (temp.equalsIgnoreCase("AS_OF_MONTH1")) {
                    timeInfo.set(0, timeDetailsArray.get(1));
                    this.completeUrl += ";" + timeInfo.get(1) + "=" + timeDetailsArray.get(1);
                } else if (temp.equalsIgnoreCase("PRG_PERIOD_TYPE")) {
                    timeInfo.set(0, timeDetailsArray.get(0));
                    this.completeUrl += ";" + timeInfo.get(1) + "=" + timeInfo.get(0);
                } else if (temp.equalsIgnoreCase("AS_OF_YEAR")) {
                    timeInfo.set(0, timeDetailsArray.get(0));
                    this.completeUrl += ";" + timeInfo.get(1) + "=" + timeDetailsArray.get(0);
                } else if (temp.equalsIgnoreCase("AS_OF_YEAR1")) {
                    timeInfo.set(0, timeDetailsArray.get(1));
                    this.completeUrl += ";" + timeInfo.get(1) + "=" + timeDetailsArray.get(1);
                }
            }
            timeDetailsMap.put(temp, timeInfo);
        }
    }

    public void getParamMetaData2() throws Exception {

        PbReturnObject retObj = null;
        Object[] Obj = null;
        String[] colNames;
        String temp = "";
        String finalQuery = "";
        scenarioParameters = new HashMap();
        scenarioParametersValues = new HashMap();
        scenarioModels = new LinkedHashMap();
        scenarioTableHashMap = new LinkedHashMap();
        this.completeUrl = "";

        //This will get parameters hashMap if Scenario Id is provided
        // Code for Parameter Hash Map 
        String sqlstr = "";
        sqlstr = resBundle.getString("getScenarioDetails");
        Obj = new Object[1];
        Obj[0] = this.scenarioId;

        finalQuery = buildQuery(sqlstr, Obj);
        retObj = execSelectSQL(finalQuery);
        Connection con = ProgenConnection.getInstance().getConnection();
        Statement st = con.createStatement();
        Reader characterStream = null;
        ResultSet rs = st.executeQuery(finalQuery);
        while (rs.next()) {
            if (rs.getClob("xml_path") != null) {
                characterStream = rs.getClob("xml_path").getCharacterStream();
            } else {
                characterStream = null;
            }
        }
        if(rs!=null){
                rs.close();
            }
            if(st!=null){
                st.close();
            }
        con.close();
        String query = "select * from scenario_model_master where scenario_id=" + this.scenarioId + " and scn_model_name in(" + this.modelNamesStr + ")";
        PbReturnObject pbro = execSelectSQL(query);
        dimensionId = String.valueOf(pbro.getFieldValueInt(0, 7));
        viewBy = pbro.getFieldValueString(0, 4);


        ScenarioProcessor scp = new ScenarioProcessor();
        scp.setCharacterStream(characterStream);
        scp.scenarioId = this.scenarioId;
        scp.scenarioName = this.scenarioName;
        scp.dimensionName = this.dimensionName;
        scp.modelNamesStr = this.modelNamesStr;
        scp.minTimeLevel = this.minTimeLevel;
        scp.dimensionId = this.dimensionId;
        scp.viewBy = this.viewBy;

        scp.processDocument2();

        scenarioParameters = scp.scenarioParameters;
        scenarioModels = scp.scenarioModels;
        ParameterNamesHashMap = scp.ParameterNamesHashMap;
        scenarioTableHashMap = scp.scenarioTableHashMap;
        scenarioParametersValues = scp.scenarioParametersValues;

        String p[] = (String[]) scenarioParameters.keySet().toArray(new String[0]);
        for (int m = 0; m < p.length; m++) {
            String key = "CBOARP" + p[m];
            String key2 = p[m];
            //  //
            if (scenarioParametersValues.containsKey(key2)) {

                if (scenarioIncomingParameters.containsKey(key)) {
                    //     //
                    String val = (String) scenarioIncomingParameters.get(key);
                    //    //
                    if (!val.equalsIgnoreCase("All")) {
                        //       //
                        //scenarioParametersValues.remove(key2);
                        scenarioParametersValues.put(key2, val);
                        ArrayList al2 = (ArrayList) scenarioParameters.get(key2);
                        al2.remove(8);
                        al2.add(8, val);
                        //scenarioParameters.remove(key2);
                        scenarioParameters.put(key2, al2);
                    }
                }
            }
        }
        colNames = retObj.getColumnNames();
        int psize = retObj.getRowCount();
        String viewByID = "";
        String viewByName = "";
        String secViewByID = "-1";
        String currViewBy = "";
        String nextViewBy = "";

        //boolean flag = false;

        scenarioViewByMain = new LinkedHashMap();
        scenarioViewByOrder = new LinkedHashMap();
        scenarioRowViewbyValues = new ArrayList();
        scenarioColViewbyValues = new ArrayList();
        scenarioRowViewbyValues = scp.scenarioRowViewbyValues;
        scenarioColViewbyValues = scp.scenarioColViewbyValues;
        scenarioViewByMain = scp.scenarioViewByMain;
        scenarioViewByOrder = scp.scenarioViewByOrder;

        String scnParamKeySet[] = (String[]) scenarioParameters.keySet().toArray(new String[0]);
        for (int i = 0; i < scnParamKeySet.length; i++) {
            ArrayList valuesArrayList = (ArrayList) scenarioParameters.get(scnParamKeySet[i]);
            if (scenarioIncomingParameters.get(valuesArrayList.get(9)) != null) {
                this.completeUrl += ";" + valuesArrayList.get(9) + "=" + scenarioIncomingParameters.get(valuesArrayList.get(9));
            } else {
                this.completeUrl += ";" + valuesArrayList.get(9) + "=" + valuesArrayList.get(8);
            }
        }

        HashMap scnViewByMap = new HashMap();
        scnViewByMap = scp.getScenarioViewByMap();
        String defV = (String) scnViewByMap.get(0);
        String defArr[] = (String[]) scnViewByMap.keySet().toArray(new String[0]);
        for (int m = 0; m < defArr.length; m++) {
            defV = defArr[m];
        }


        HashMap viewByDet = scp.getScenarioViewByMap();

        String val[] = (String[]) scenarioRowViewbyValues.toArray(new String[0]);
        ArrayList paramDet = new ArrayList();
        for (int looper = 0; looper < val.length; looper++) {
            ArrayList viewByDetList = new ArrayList();
            if (viewByDet.containsKey(val[looper])) {
                viewByDetList = (ArrayList) viewByDet.get(val[looper]);
            }
            //flag = true;
            ArrayList viewByArrayList = new ArrayList();
            viewByID = viewByDetList.get(1).toString();
            if (scnViewByMap.containsKey(val[looper])) {
                viewByArrayList.add("CBO" + "VIEW_BY_NAME");
            }
            viewByArrayList.add("CBOVIEW_BY" + viewByID);
            viewByArrayList.add("Analyze By");



            if (scenarioIncomingParameters.get("CBOVIEW_BY" + viewByID) != null) {
                scenarioRowViewbyValues = new ArrayList();
                scenarioRowViewbyValues.add(scenarioIncomingParameters.get("CBOVIEW_BY" + viewByID));
                viewByArrayList.add(scenarioIncomingParameters.get("CBOVIEW_BY" + viewByID));
                paramDet = (ArrayList) scenarioParameters.get(scenarioIncomingParameters.get("CBOVIEW_BY" + viewByID));
                viewByArrayList.add(paramDet.get(1));
                this.completeUrl += ";" + viewByArrayList.get(1) + "=" + viewByArrayList.get(3);
            } else {
                viewByArrayList.add(defV);
                paramDet = (ArrayList) scenarioParameters.get(defV);
                viewByArrayList.add(paramDet.get(1));
                this.completeUrl += ";" + viewByArrayList.get(1) + "=" + viewByArrayList.get(3);
            }

            scenarioViewByMain.put(viewByID, viewByArrayList);


            if (looper == 0) {
                if (scenarioIncomingParameters.get("CBOVIEW_BY" + viewByID) != null) {
                    currViewBy = scenarioIncomingParameters.get("CBOVIEW_BY" + viewByID).toString();
                }

                nextViewBy = getChildElementId(currViewBy, scenarioId, scenarioParameters);
                if (nextViewBy != null) {
                    drillUrl += "&CBOVIEW_BY" + viewByID + "=" + nextViewBy + "";
                }
                String dVal = "";

                if (scenarioIncomingParameters.containsKey("CBOARP" + currViewBy)) {
                    dVal = scenarioIncomingParameters.get("CBOARP" + currViewBy).toString();
                }

                drillUrl += "&CBOARP" + currViewBy + "=";
            }
        }


        //code for building Model drop-down
        ArrayList secViewByArrayList = new ArrayList();
        String modelId = "";
        secViewByArrayList.add("CBO" + "VIEW_BY_NAME1");
        secViewByArrayList.add("CBOVIEW_BY" + secViewByID);
        secViewByArrayList.add("Model");
        if (scenarioIncomingParameters.get("CBOVIEW_BY" + secViewByID) != null) {
            modelId = (String) scenarioIncomingParameters.get("CBOVIEW_BY" + secViewByID);
            // //
            if (modelId.equalsIgnoreCase("-1")) {
                secViewByArrayList.add(newCustomModelId);
                paramDet = (ArrayList) scenarioModels.get(newCustomModelId);
                //  //
                secViewByArrayList.add(paramDet.get(1));
            } else {
                secViewByArrayList.add(scenarioIncomingParameters.get("CBOVIEW_BY" + secViewByID));
                paramDet = (ArrayList) scenarioModels.get(scenarioIncomingParameters.get("CBOVIEW_BY" + secViewByID));
                //  //
                secViewByArrayList.add(paramDet.get(1));
            }

            this.completeUrl += ";" + secViewByArrayList.get(1) + "=" + secViewByArrayList.get(3);
        } else {
            secViewByArrayList.add("0");
            secViewByArrayList.add("--select--");
            this.completeUrl += ";" + secViewByArrayList.get(1) + "=" + secViewByArrayList.get(3);
        }
        scenarioViewByMain.put(secViewByID, secViewByArrayList);
        // end of code

        /////////////Code for Time Setup/////////

        sqlstr = "";
        try {
            finalQuery = buildQuery(sqlstr, Obj);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }

        ProgenParam pParam = new ProgenParam();
        timeDetailsMap = new LinkedHashMap();
        timeDetailsArray = new ArrayList();
        timeDetailsMap = scp.timeDetailsMap;
        timeDetailsArray = scp.timeDetailsArray;

        String timeDet[] = (String[]) timeDetailsMap.keySet().toArray(new String[0]);
        for (int i = 0; i < timeDet.length; i++) {
            ArrayList timeInfo = (ArrayList) timeDetailsMap.get(timeDet[i]);
            temp = timeDet[i];
            if (scenarioIncomingParameters.get(("CBO_" + temp)) != null) {
                if (temp.equalsIgnoreCase("AS_OF_MONTH")) {
                    timeInfo.set(0, scenarioIncomingParameters.get(("CBO_" + temp)));
                    this.completeUrl += ";" + timeInfo.get(1) + "=" + timeInfo.get(0);
                } else if (temp.equalsIgnoreCase("AS_OF_MONTH1")) {
                    timeInfo.set(0, scenarioIncomingParameters.get(("CBO_" + temp)));
                    this.completeUrl += ";" + timeInfo.get(1) + "=" + timeInfo.get(0);
                } else if (temp.equalsIgnoreCase("PRG_PERIOD_TYPE")) {
                    timeInfo.set(0, scenarioIncomingParameters.get(("CBO_" + temp)));
                    this.completeUrl += ";" + timeInfo.get(1) + "=" + timeInfo.get(0);
                } else if (temp.equalsIgnoreCase("AS_OF_YEAR")) {
                    timeInfo.set(0, scenarioIncomingParameters.get(("CBO_" + temp)));
                    this.completeUrl += ";" + timeInfo.get(1) + "=" + timeInfo.get(0);
                } else if (temp.equalsIgnoreCase("AS_OF_YEAR1")) {
                    timeInfo.set(0, scenarioIncomingParameters.get(("CBO_" + temp)));
                    this.completeUrl += ";" + timeInfo.get(1) + "=" + timeInfo.get(0);
                }
            } else {
                if (temp.equalsIgnoreCase("AS_OF_MONTH")) {
                    timeInfo.set(0, timeDetailsArray.get(2));
                    this.completeUrl += ";" + timeInfo.get(1) + "=" + timeDetailsArray.get(2);
                } else if (temp.equalsIgnoreCase("AS_OF_MONTH1")) {
                    timeInfo.set(0, timeDetailsArray.get(3));
                    this.completeUrl += ";" + timeInfo.get(1) + "=" + timeDetailsArray.get(3);
                } else if (temp.equalsIgnoreCase("PRG_PERIOD_TYPE")) {
                    //timeInfo.set(0, timeDetailsArray.get(2));
                    this.completeUrl += ";" + timeInfo.get(1) + "=" + timeInfo.get(0);
                } else if (temp.equalsIgnoreCase("AS_OF_YEAR")) {
                    timeInfo.set(0, timeDetailsArray.get(2));
                    this.completeUrl += ";" + timeInfo.get(1) + "=" + timeDetailsArray.get(2);
                } else if (temp.equalsIgnoreCase("AS_OF_YEAR1")) {
                    timeInfo.set(0, timeDetailsArray.get(3));
                    this.completeUrl += ";" + timeInfo.get(1) + "=" + timeDetailsArray.get(3);
                }
            }
            timeDetailsMap.put(temp, timeInfo);
        }

    }

    public String getParameterDispName(String paramElementId) {
        String paramName = "";
        if (scenarioParameters != null) {
            ArrayList paramInfo = (ArrayList) scenarioParameters.get(paramElementId);
            if (paramInfo != null) {
                paramName = String.valueOf(paramInfo.get(1));
            }
        }
        return paramName;
    }

    public String getChildElementId(String elementId) {
        ArrayList paraInfo = new ArrayList();
        String NextElementId = null;
        paraInfo = (ArrayList) scenarioParameters.get(elementId);
        if (paraInfo != null) {
            if (paraInfo.get(2) != null) {
                NextElementId = paraInfo.get(2).toString();
            }
        }
        return (NextElementId);
    }

    public String getChildElementId(String elementId, String scenarioId, HashMap scenarioParameters) {
        String elelevel = "";
        ArrayList al = new ArrayList();
        String NextElementId = null;
        String dimId = "";
        if (scenarioParameters.containsKey(elementId)) {
            al = (ArrayList) scenarioParameters.get(elementId);
            elelevel = al.get(6).toString();
            dimId = al.get(3).toString();
        }
        String childLevel = "";
        String arr[] = (String[]) scenarioParameters.keySet().toArray(new String[0]);
        String childDim = "";
        String childL = "";
        if (!elementId.equalsIgnoreCase("TIME")) {
            for (int m = 0; m < arr.length; m++) {
                al = (ArrayList) scenarioParameters.get(arr[m]);
                childDim = al.get(3).toString();
                childL = al.get(6).toString();
                if (childDim.equalsIgnoreCase(dimId)) {
                    int chLevel = Integer.parseInt(childL);
                    if (chLevel == (Integer.parseInt(elelevel) + 1)) {
                        NextElementId = arr[m];
                    }
                }
            }
        }
        ArrayList paraInfo = new ArrayList();

        PbReturnObject retObj = null;
        String[] colNames;
        /*
         * if (!elementId.equalsIgnoreCase("TIME")) { String sqlstr = ""; sqlstr
         * = ""; sqlstr = "select element_id,child_element_id, param_disp_name
         * from prg_target_param_details where target_id="+targetId+" and
         * element_id="+elementId; // try { retObj = execSelectSQL(sqlstr);
         *
         * } catch (Exception ex) { logger.error("Exception:",ex); }
         *
         * colNames = retObj.getColumnNames(); int psize = retObj.getRowCount();
         *
         * if (psize > 0) { NextElementId =
         * retObj.getFieldValueString(0,"CHILD_ELEMENT_ID"); } }
         */



        return (NextElementId);
    }

    @Override
    public HashMap getScenarioViewByMap() {
        return this.scenarioViewByMap;
    }

    @Override
    public HashMap getScenarioSecViewByMap() {
        return this.scenarioSecViewByMap;
    }

    public HashMap getParentElementId(String elementId, String scenarioId) {
        ArrayList paraInfo = new ArrayList();
        ArrayList parentBussTab = new ArrayList();
        HashMap parentBussCol = new HashMap();
        HashMap details = new HashMap();
        String NextElementId = null;
        PbReturnObject retObj = null;
        String[] colNames;
        if (!elementId.equalsIgnoreCase("TIME")) {
            String sqlstr = "";
            sqlstr += " select KEY_ELEMENT_ID , LEVEL1,key_buss_table_id,key_buss_col_name from ";
            sqlstr += " PRG_USER_ALL_ADIM_KEY_VAL_ELE a, ";
            sqlstr += " PRG_AR_REPORT_PARAM_DETAILS b ";
            sqlstr += " where a.KEY_ELEMENT_ID = b.element_id ";
            sqlstr += " and KEY_DIM_ID in ( ";
            sqlstr += " SELECT DIM_ID ";
            sqlstr += " FROM PRG_USER_ALL_INFO_DETAILS ";
            sqlstr += " where ELEMENT_ID =" + elementId + ") ";

            sqlstr += " and LEVEL1 < ( ";
            sqlstr += "  SELECT min(LEVEL1) ";
            sqlstr += " FROM PRG_USER_ALL_ADIM_KEY_VAL_ELE";
            sqlstr += "  WHERE KEY_ELEMENT_ID =" + elementId + ") ";

            sqlstr += " and b.report_id= " + scenarioId + " ";
            sqlstr += " and ELEMENT_ID !=" + elementId + " ";
            sqlstr += " order by 2 ";

            try {
                retObj = execSelectSQL(sqlstr);

            } catch (SQLException ex) {
                logger.error("Exception:", ex);
            }

            colNames = retObj.getColumnNames();
            int psize = retObj.getRowCount();

            if (psize > 0) {
                NextElementId = retObj.getFieldValueString(0, colNames[0]);
            }
            for (int m = 0; m < retObj.getRowCount(); m++) {
                paraInfo.add(retObj.getFieldValueString(m, 0));
                if (!parentBussTab.contains(retObj.getFieldValueString(m, "KEY_BUSS_TABLE_ID"))) {
                    parentBussTab.add(retObj.getFieldValueString(m, "KEY_BUSS_TABLE_ID"));
                }
                parentBussCol.put(retObj.getFieldValueString(m, 0), retObj.getFieldValueString(m, "KEY_BUSS_COL_NAME"));
            }
        }

        details.put("ParentBussTables", parentBussTab);
        details.put("ParentList", paraInfo);
        details.put("ParentBussCol", parentBussCol);
        return details;
    }

    public String getNormalRetObj(String msrCol, String scenarioId, HttpServletRequest request) {

        PbReturnObject retObj = null;
        PbReturnObject aggrRetObj = null;
        PbReturnObject scnrRetObj = null;
        PbReturnObject normalRetObj = null;
        String sqlstr = "";
        String ViewbySqlstr = "";
        String finalQuery = "";
        ArrayList msrList = new ArrayList();
        ArrayList aggrList = new ArrayList();
        String rowViewById = "";
        String combinedSql = "";
        ArrayList RowViewbyValues = new ArrayList();
        ArrayList ColViewbyValues = new ArrayList();
        ArrayList timeDetsArray = new ArrayList();
        ArrayList NonViewByList = new ArrayList();
        HashMap NonViewByMap = new HashMap();
        PbReturnObject combinedRetObj = null;
        ArrayList crossTabMsrList = new ArrayList();
        ArrayList crossTabAggrList = new ArrayList();
        ArrayList crossTabNonViewByList = new ArrayList();
        ArrayList timeDetsArrayList = new ArrayList();
        HashMap combinedTable = new HashMap();
        String tableDisplay = "";
        ArrayList graphArray = new ArrayList();
        String scenarioParamSectionDisplay = "";
        DisplayScenarioParameters disp = null;
        try {
//            getParamMetaData();
            String[] msrStr = msrCol.split(",");
            for (int i = 0; i < msrStr.length; i++) {
                msrList.add(msrStr[i]);
            }
            sqlstr = "";
            sqlstr += "select REF_ELEMENT_TYPE,AGGREGATION_TYPE from prg_user_all_info_details ";
            sqlstr += " where ELEMENT_ID=" + msrList.get(0).toString() + " ";
            sqlstr += " OR REF_ELEMENT_ID = " + msrList.get(0).toString() + " ";
            sqlstr += " order by REF_ELEMENT_TYPE asc ";
            finalQuery = sqlstr;
            aggrRetObj = execSelectSQL(finalQuery);
            if (aggrRetObj.getRowCount() > 0) {
                for (int j = 0; j < aggrRetObj.getRowCount(); j++) {
                    aggrList.add(aggrRetObj.getFieldValueString(j, "AGGREGATION_TYPE")); //query Aggration
                }
            }

            ScenarioProcessor scp = new ScenarioProcessor();
            PbReportQuery repQuery = new PbReportQuery();

            String sqlstr1 = "";
            sqlstr1 = resBundle.getString("getScenarioDetails");
            Object[] Obj = new Object[1];
            Obj[0] = scenarioId;

            finalQuery = buildQuery(sqlstr1, Obj);
            //  //
            retObj = execSelectSQL(finalQuery);
            Connection con = ProgenConnection.getInstance().getConnection();
            Statement st = con.createStatement();
            Reader characterStream = null;
            ResultSet rs = st.executeQuery(finalQuery);
            while (rs.next()) {
                if (rs.getClob("xml_path") != null) {
                    characterStream = rs.getClob("xml_path").getCharacterStream();
                } else {
                    characterStream = null;
                }
            }
            if(rs!=null){
                rs.close();
            }
            if(st!=null){
                st.close();
            }
            con.close();
            document = builder.build(characterStream);
            root = document.getRootElement();
            List row = root.getChildren("sViewBy");//Only one row as of now
            /*
             * Start of Processing of parameters
             */
            for (int i = 0; i < row.size(); i++) {//Loop for One section under portlet
                Element Companyname = (Element) row.get(i);
                if (Companyname.getChildren("svRowViewBy") != null) {
                    List paramRow = Companyname.getChildren("svRowViewBy");
                    for (int j = 0; j < paramRow.size(); j++) {
                        Element paramElement = (Element) paramRow.get(j);
                        List pViewByChild = paramElement.getChildren("svrMasterData");
                        for (int k = 0; k < pViewByChild.size(); k++) {
                            Element pViewByChildElement = (Element) pViewByChild.get(k);
                            RowViewbyValues.add(xmUtil.getXmlTagValue(pViewByChildElement, "svrdefaultValue"));
                        }

                    }
                }

                ColViewbyValues.add("Time");
            }
            List crossTabrow = root.getChildren("rQuery");//Only one row as of now
            /*
             * Start of Processing of parameters
             */
            for (int i = 0; i < crossTabrow.size(); i++) {//Loop for One section under portlet
                Element Companyname = (Element) crossTabrow.get(i);
                if (Companyname.getChildren("rqDetails") != null) {
                    List paramRow = Companyname.getChildren("rqDetails");
                    for (int j = 0; j < paramRow.size(); j++) {
                        Element paramElement = (Element) paramRow.get(j);
                        crossTabMsrList.add(xmUtil.getXmlTagValue(paramElement, "elementId"));
                        crossTabAggrList.add(xmUtil.getXmlTagValue(paramElement, "elementAgg"));
                    }
                }
            }

            List timeRow = root.getChildren("sTime");//Only one row as of now
            /*
             * Start of Processing of parameters
             */
            for (int i = 0; i < timeRow.size(); i++) {//Loop for One section under portlet
                Element Companyname = (Element) timeRow.get(i);
                if (Companyname.getChildren("stMaster") != null) {
                    List paramRow = Companyname.getChildren("stMaster");
                    for (int j = 0; j < paramRow.size(); j++) {
                        Element paramElement = (Element) paramRow.get(j);
                        //  //
                        timeDetsArray.add("Day");
                        timeDetsArray.add("PRG_YEAR_RANGE");
                        timeDetsArray.add(xmUtil.getXmlTagValue(paramElement, "historicalStartMonth"));
                        timeDetsArray.add(xmUtil.getXmlTagValue(paramElement, "historicalEndMonth"));
                        timeDetsArray.add(xmUtil.getXmlTagValue(paramElement, "scenarioStartMonth"));
                        timeDetsArray.add(xmUtil.getXmlTagValue(paramElement, "scenarioEndMonth"));
                        timeDetsArrayList.add(xmUtil.getXmlTagValue(paramElement, "historicalStartMonth"));
                        timeDetsArrayList.add(xmUtil.getXmlTagValue(paramElement, "historicalEndMonth"));
                        timeDetsArrayList.add(xmUtil.getXmlTagValue(paramElement, "scenarioStartMonth"));
                        timeDetsArrayList.add(xmUtil.getXmlTagValue(paramElement, "scenarioEndMonth"));
                    }
                }
            }

            setTimeDetsArrayList(timeDetsArrayList);
            repQuery.setRowViewbyCols(RowViewbyValues);
//            repQuery.setColViewbyCols(ColViewbyValues);
            repQuery.setQryColumns(msrList);
            repQuery.setColAggration(aggrList);
            repQuery.setTimeDetails(timeDetsArray);
            repQuery.setDefaultMeasure(String.valueOf(msrList.get(0)));
            repQuery.setDefaultMeasureSumm(String.valueOf(aggrList.get(0)));
            repQuery.generateViewByQry();

            normalRetObj = repQuery.getPbReturnObject(String.valueOf(msrList.get(0)));
            ArrayList normList = new ArrayList();
            for (int i = 0; i < normalRetObj.getRowCount(); i++) {
                for (int j = 1; j < normalRetObj.getColumnCount(); j++) {
                    normList.add(normalRetObj.getFieldValueString(i, j));
                }
                normalHm.put(normalRetObj.getFieldValueString(i, 0), normList);
                normList = new ArrayList();
            }
            setNormalHm(normalHm);
            request.setAttribute("normalHm", getNormalHm());
            PbReportQuery crossTabQry = new PbReportQuery();
            crossTabQry.setRowViewbyCols(RowViewbyValues);
            crossTabQry.setColViewbyCols(ColViewbyValues);
            crossTabQry.setQryColumns(crossTabMsrList);
            crossTabQry.setColAggration(crossTabAggrList);
            crossTabQry.setTimeDetails(timeDetsArray);
            crossTabQry.setDefaultMeasure(String.valueOf(crossTabMsrList.get(0)));
            crossTabQry.setDefaultMeasureSumm(String.valueOf(crossTabAggrList.get(0)));
            crossTabQry.generateViewByQry();


            //combinedSql = crossTabQry.mergeSqlandCrossTabSql(repQuery.generateViewByQry(), crossTabQry.generateViewByQry(), RowViewbyValues, msrList, crossTabQry.crossTabNonViewBy, true, crossTabQry.crossTabViewByList, aggrList, String.valueOf(crossTabAggrList.get(0)));
            //ProgenConnection conn = new ProgenConnection();
            //conn.getCustomerConn();
            //combinedRetObj = (execSelectSQL(combinedSql, conn.getCustomerConn()));

            setCombinedRetObj(normalRetObj);
//            this.setSimpleRetObj(normalRetObj);
            String[] normalNonViewbys = (String[]) repQuery.NonViewByMap.keySet().toArray(new String[0]);
            String[] crosstabNonViewbys = (String[]) crossTabQry.crossTabNonViewByMap.keySet().toArray(new String[0]);
            for (int i = 0; i < normalNonViewbys.length; i++) {
                NonViewByList.add(normalNonViewbys[i]);
            }
//            for (int i = 0; i < crosstabNonViewbys.length; i++) {
//                NonViewByList.add(crosstabNonViewbys[i]);
//            }
//            for (int k = 0; k < crossTabQry.crossTabNonViewByMap.size(); k++) {
//                NonViewByMap.put(crosstabNonViewbys[k], crossTabQry.crossTabNonViewByMap.get(crosstabNonViewbys[k]));
//            }
            for (int k = 0; k < repQuery.NonViewByMap.size(); k++) {
                NonViewByMap.put(normalNonViewbys[k], repQuery.NonViewByMap.get(normalNonViewbys[k]));
            }
            setNonViewByList(NonViewByList);
            setNonViewByMap(NonViewByMap);

//            tableDisplay = "<table id='tablesorter' class='tablesorter' width='100%' >";
//            tableDisplay += "<thead><tr>";
//            tableDisplay += "<th style='font-weight:bold'>Regional Office</th>";
//            for (int k = 0; k < crossTabQry.crossTabNonViewByMap.size(); k++) {
//                tableDisplay += "<th style='font-weight:bold'>" + crossTabQry.crossTabNonViewByMap.get(crosstabNonViewbys[k]) + "</th>";
//            }
//            for (int k = 0; k < repQuery.NonViewByMap.size(); k++) {
//                tableDisplay += "<th style='font-weight:bold'>" + repQuery.NonViewByMap.get(normalNonViewbys[k]) + "</th>";
//            }
//            tableDisplay += "</tr></thead><tbody>";
//            if (combinedRetObj.getRowCount() > 0) {
//                for (int ret = 0; ret < combinedRetObj.getRowCount(); ret++) {
//                    tableDisplay += "<tr class='even'>";
//                    for (int col = 0; col < combinedRetObj.getColumnCount(); col++) {
//                        tableDisplay += "<td><a href='javascript:submitDrill('&CBOARP='"+combinedRetObj.getFieldValueString(ret, col) +")'>"+combinedRetObj.getFieldValueString(ret, col) + "</td>";
//                    }
//                    tableDisplay += "</tr>";
//                }
//                tableDisplay+="</tbody></table>";
//            }
//
            DisplayScenarioParameters dispScnParam = new DisplayScenarioParameters();

            // combinedTable = dispScnParam.getScenarioTable(getNonViewByList(), normalRetObj, scenarioId, timeDetsArray, String.valueOf(RowViewbyValues.get(0)), repQuery.NonViewByMap, getScenarioViewbyMainTemp(), getTimeDetsMapTemp(), getScenarioParamsTemp(), getDrillurlTemp());

//            tableDisplay = (String) combinedTable.get("result");
//                graphArray = (ArrayList) combinedTable.get("graphArray");
//                String viewByName = (String) combinedTable.get("viewByName");
//                String modelId = (String) combinedTable.get("modelId");
//                ArrayList histMonths = (ArrayList) combinedTable.get("histMonths");
//                String dimensionId = (String) combinedTable.get("dimensionId");
//                request.setAttribute("viewByName", viewByName);
//                request.setAttribute("modelId", modelId);
//                request.setAttribute("histMonths", histMonths);
//                request.setAttribute("dimensionId", dimensionId);
            //                request.setAttribute("timeDetailsArray", timeDetsArrayList);
//                request.setAttribute("crossTabNonViewByMap", NonViewByMap);
//                request.setAttribute("crossTabNonViewBy", NonViewByList);
//                request.setAttribute("crossTabRetObj", combinedRetObj);
//                disp = new DisplayScenarioParameters();
//
//                scenarioParamSectionDisplay = disp.displayTimeParams(getTimeDetsMapTemp(), timeDetsArrayList, flag);
//                scenarioParamSectionDisplay += disp.displayParams(getScenarioParamsTemp(), scenarioId);
//                scenarioParamSectionDisplay += disp.displayViewBys(getScenarioViewbyMainTemp(), getScenarioParamsTemp(), getScenarioModelsTemp(), flag);
//
//                request.setAttribute("scenarioParamSectionDisplay", scenarioParamSectionDisplay);
//
//                request.setAttribute("scenarioTableScetionDisplay", tableDisplay);
//                //
//                request.setAttribute("graphArray", graphArray);
//
//                request.setAttribute("scenarioId", scenarioId);
//                request.setAttribute("scenarioName", scenarioName);
////                return mapping.findForward("displayscenario");

            //retObj = repQuery.getPbReturnObject(String.valueOf(msrList.get(0)));
            ////
//            for (int k = 0; k < retObj.getRowCount(); k++) {
//                //
//
//            }
        } catch (Exception exp) {
            logger.error("Exception:", exp);
        }

        return tableDisplay;
    }

    public static HashMap getScenarioParamsTemp() {
        return scenarioParamsTemp;
    }

    public static void setScenarioParamsTemp(HashMap aScenarioParamsTemp) {
        scenarioParamsTemp = aScenarioParamsTemp;
    }

    public static String getDrillurlTemp() {
        return drillurlTemp;
    }

    public static void setDrillurlTemp(String aDrillurlTemp) {
        drillurlTemp = aDrillurlTemp;
    }

    public static LinkedHashMap getScenarioViewbyMainTemp() {
        return scenarioViewbyMainTemp;
    }

    public static void setScenarioViewbyMainTemp(LinkedHashMap aScenarioViewbyMainTemp) {
        scenarioViewbyMainTemp = aScenarioViewbyMainTemp;
    }

    public static LinkedHashMap getTimeDetsMapTemp() {
        return timeDetsMapTemp;
    }

    public static void setTimeDetsMapTemp(LinkedHashMap aTimeDetsMapTemp) {
        timeDetsMapTemp = aTimeDetsMapTemp;
    }

    public static LinkedHashMap getScenarioModelsTemp() {
        return scenarioModelsTemp;
    }

    public static void setScenarioModelsTemp(LinkedHashMap aScenarioModelsTemp) {
        scenarioModelsTemp = aScenarioModelsTemp;
    }

    public static ArrayList getTimeDetsArrayList() {
        return timeDetsArrayList;
    }

    public static void setTimeDetsArrayList(ArrayList aTimeDetsArrayList) {
        timeDetsArrayList = aTimeDetsArrayList;
    }

    public static ArrayList getNonViewByList() {
        return NonViewByList;
    }

    public static void setNonViewByList(ArrayList aNonViewByList) {
        NonViewByList = aNonViewByList;
    }

    public static HashMap getNonViewByMap() {
        return NonViewByMap;
    }

    public static void setNonViewByMap(HashMap aNonViewByMap) {
        NonViewByMap = aNonViewByMap;
    }

    public static PbReturnObject getCombinedRetObj() {
        return combinedRetObj;
    }

    public static void setCombinedRetObj(PbReturnObject aCombinedRetObj) {
        combinedRetObj = aCombinedRetObj;
    }

    public static HashMap getNormalHm() {
        return normalHm;
    }

    public static void setNormalHm(HashMap aNormalHm) {
        normalHm = aNormalHm;
    }

    public PbReturnObject getSimpleRetObj() {
        return simpleRetObj;
    }

    public void setSimpleRetObj(PbReturnObject simpleRetObj) {
        this.simpleRetObj = simpleRetObj;
    }
}
