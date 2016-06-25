package com.progen.target;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import org.apache.log4j.Logger;
import prg.db.PbReturnObject;
import utils.db.ProgenConnection;
import utils.db.ProgenParam;

public class PbTargetCollection extends PbTargetMaps {

    public static Logger logger = Logger.getLogger(PbTargetCollection.class);
    PbTargetCollectionResourceBundle resBundle = new PbTargetCollectionResourceBundle();
    public String targetId;
    public String minTimeLevel;
    public String drillUrl = "";
    public String columnEdgeDrillUrl = "";
    public String primAnalyzeBy = "";
    public ArrayList basisValues = null;

    @Override
    public HashMap getTargetMetaData() {
        return this.targetMetaData;
    }

    @Override
    public HashMap getTargetParameters() {
        return this.targetParameters;
    }

    @Override
    public HashMap[] getTargetViewBys() {
        return this.targetViewBys;
    }

    @Override
    public LinkedHashMap getTargetViewByMain() {
        return this.targetViewByMain;
    }

    public void getParamMetaData() {

        PbReturnObject retObj = null;
        Object[] Obj = null;
        String[] colNames;
        String temp;
        String finalQuery = "";
        targetParameters = new HashMap();
        targetParametersValues = new HashMap();
        this.completeUrl = "";


        //This will get parameters hashMap if Target Id is provided
        /**
         * Code for Parameter Hash Map
         */
        String sqlstr = "";

        try {
            sqlstr = resBundle.getString("getTargetParamMetaDataQuery1");

            Obj = new Object[1];
            Obj[0] = this.targetId;

            finalQuery = buildQuery(sqlstr, Obj);
            retObj = execSelectSQL(finalQuery);

        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }

        colNames = retObj.getColumnNames();
        int psize = retObj.getRowCount();
        if (psize > 0) {
            for (int looper = 0; looper < psize; looper++) {
                ArrayList paramInfo = new ArrayList();
                paramInfo.add(retObj.getFieldValueString(looper, colNames[0]));
                paramInfo.add(retObj.getFieldValueString(looper, colNames[1]));
                paramInfo.add(retObj.getFieldValueString(looper, colNames[2]));
                paramInfo.add(retObj.getFieldValueString(looper, colNames[3]));
                paramInfo.add(retObj.getFieldValueString(looper, colNames[4]));
                paramInfo.add(retObj.getFieldValueString(looper, colNames[5]));
                paramInfo.add(retObj.getFieldValueString(looper, colNames[6]));
                paramInfo.add(retObj.getFieldValueString(looper, colNames[7]));
                //paramInfo.add(retObj.getFieldValueString(looper, colNames[8]));
   if (targetIncomingParameters.get(("CBOARP" + (retObj.getFieldValueString(looper, colNames[0])))) == null) {
                    paramInfo.add(retObj.getFieldValueString(looper, colNames[8]));
                } else {
                paramInfo.add(String.valueOf(targetIncomingParameters.get("CBOARP" + (retObj.getFieldValueString(looper, colNames[0])))));
                }
                ////Adding CBONAME --CBOAPP for target Parameters
                paramInfo.add("CBOARP" + retObj.getFieldValueString(looper, colNames[0]));
                this.setCompleteUrl(this.getCompleteUrl() + "&" + String.valueOf(paramInfo.get(9)) + "=" + String.valueOf(paramInfo.get(8)));
                // Adding Data to HashMap
                targetParameters.put(retObj.getFieldValueString(looper, colNames[0]), paramInfo);
                targetParametersValues.put(retObj.getFieldValueString(looper, colNames[0]), String.valueOf(paramInfo.get(8)));
  }

        }
    //This will get parameters hashMap if target Id is provided
        /**
         * Code for Parameter Hash Map
         */
        String viewByID = "";
        String viewByName = "";
        try {
            sqlstr = resBundle.getString("getTargetParamMetaDataQuery2");
            Obj = new Object[1];
            Obj[0] = this.targetId;

            finalQuery = buildQuery(sqlstr, Obj);
          retObj = execSelectSQL(finalQuery);

        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }

        colNames = retObj.getColumnNames();
        psize = retObj.getRowCount();
        int j = 0;
        ArrayList viewHashList = new ArrayList();
        int count = 0;

        String currViewBy = "";
        String nextViewBy = "";
        boolean isFirstRowViewBy = false;

        int totalView = 1;
        HashMap viewByHashMap = new HashMap();
        boolean flag = false;
        if (psize > 0) {

            targetViewByMain = new LinkedHashMap();
            targetViewByOrder = new LinkedHashMap();
            targetIncomingParameters.get("CBOVIEW_BY" + (retObj.getFieldValueString(0, colNames[0])));
            targetRowViewbyValues = new ArrayList();
            targetColViewbyValues = new ArrayList();
            for (int looper = 0; looper < psize; looper++) {
               if (retObj.getFieldValueString(looper, colNames[6]).trim().equalsIgnoreCase("VIEW_BY_NAME")) {
                    flag = true;
                    ArrayList viewByArrayList = new ArrayList();
                    viewByID = retObj.getFieldValueString(looper, colNames[0]);
                    viewByName = retObj.getFieldValueString(looper, colNames[6]);
                    currViewBy = retObj.getFieldValueString(0, colNames[15]).toString();
                    viewByArrayList.add("CBO" + retObj.getFieldValueString(looper, colNames[6]));
                    viewByArrayList.add("CBOVIEW_BY" + retObj.getFieldValueString(looper, colNames[0]));
                    viewByArrayList.add(retObj.getFieldValueString(looper, colNames[5]));

                    if (targetIncomingParameters.get("CBOVIEW_BY" + viewByID) != null) {
                        targetRowViewbyValues.add(targetIncomingParameters.get("CBOVIEW_BY" + viewByID));
                        viewByArrayList.add(targetIncomingParameters.get("CBOVIEW_BY" + viewByID));
                    } else {
                        targetRowViewbyValues.add(retObj.getFieldValueString(0, colNames[15]));
                        viewByArrayList.add(getPrimaryTargetParameter());
                    }

                    if (targetIncomingParameters.get("CBOVIEW_BY" + viewByID) != null) {

                        String[] a1 = (String[]) (targetParameters.keySet()).toArray(new String[0]);
                        for (int i = 0; i < a1.length; i++) {
                            ArrayList a = (ArrayList) targetParameters.get(a1[i]);
                            if (a1[i].equalsIgnoreCase((String) targetIncomingParameters.get("CBOVIEW_BY" + viewByID))) {
                                viewByArrayList.add(a.get(1).toString());
                            }
                        }
                    } else {
                        String[] a1 = (String[]) (targetParameters.keySet()).toArray(new String[0]);
                        for (int i = 0; i < a1.length; i++) {
                            ArrayList a = (ArrayList) targetParameters.get(a1[i]);
                            if (a1[i].equalsIgnoreCase(retObj.getFieldValueString(looper, colNames[15]))) {
                                viewByArrayList.add(a.get(1).toString());
                            }
                        }
                    }
                    //viewByArrayList.add(getPrimaryTargetParameter());

                    targetViewByMain.put(viewByID, viewByArrayList);


                    if (looper == 0) {
                        if (targetIncomingParameters.get("CBOVIEW_BY" + viewByID) != null) {
                            currViewBy = targetIncomingParameters.get("CBOVIEW_BY" + viewByID).toString();
                        }

                        nextViewBy = getChildElementId(currViewBy, targetId);
                        if (nextViewBy != null) {
                            drillUrl += "&CBOVIEW_BY" + viewByID + "=" + nextViewBy + "";
                        }
                        String dVal = "";
                        if (targetIncomingParameters.containsKey("CBOARP" + currViewBy)) {
                            dVal = targetIncomingParameters.get("CBOARP" + currViewBy).toString();
                        }
                        drillUrl += "&CBOARP" + currViewBy + "=";
                    }

                } else if (retObj.getFieldValueString(looper, colNames[6]).trim().equalsIgnoreCase("VIEW_BY_NAME1")) {
                    ArrayList viewByArrayList = new ArrayList();
                    viewByID = retObj.getFieldValueString(looper, colNames[0]);
                    viewByName = retObj.getFieldValueString(looper, colNames[6]);

                    viewByArrayList.add("CBO" + retObj.getFieldValueString(looper, colNames[6]));
                    viewByArrayList.add("CBOVIEW_BY" + retObj.getFieldValueString(looper, colNames[0]));
                    viewByArrayList.add(retObj.getFieldValueString(looper, colNames[5]));
                    //viewByArrayList.add(retObj.getFieldValueString(looper, colNames[15]));

                    if (targetIncomingParameters.get("CBOVIEW_BY" + viewByID) != null) {

                        targetColViewbyValues.add(targetIncomingParameters.get("CBOVIEW_BY" + viewByID));
                        viewByArrayList.add(targetIncomingParameters.get("CBOVIEW_BY" + viewByID));
                    } else {
                        targetColViewbyValues.add(retObj.getFieldValueString(looper, colNames[15]));
                        viewByArrayList.add(retObj.getFieldValueString(looper, colNames[15]));
                    }

                    if (targetIncomingParameters.get("CBOVIEW_BY" + viewByID) != null) {
                        if (targetIncomingParameters.get("CBOVIEW_BY" + viewByID).toString().equalsIgnoreCase("Time")) {
                            viewByArrayList.add("Time");
                        } else {
                            String[] a1 = (String[]) (targetParameters.keySet()).toArray(new String[0]);
                            for (int i = 0; i < a1.length; i++) {
                                ArrayList a = (ArrayList) targetParameters.get(a1[i]);
                                if (a1[i].equalsIgnoreCase((String) targetIncomingParameters.get("CBOVIEW_BY" + viewByID))) {
                                    viewByArrayList.add(a.get(1).toString());
                                }
                            }
                        }
                    } else {
                        viewByArrayList.add(retObj.getFieldValueString(looper, colNames[15]));
                    }

                    if (count == 0) {
                        if (targetIncomingParameters.get("CBOVIEW_BY" + viewByID) != null) {
                            currViewBy = targetIncomingParameters.get("CBOVIEW_BY" + viewByID).toString();
                        }
                        if (!currViewBy.equalsIgnoreCase("Time")) {
                            nextViewBy = getChildElementId(currViewBy, targetId);
                            if (nextViewBy != null) {
                                columnEdgeDrillUrl += "&CBOVIEW_BY" + viewByID + "=" + nextViewBy + "";
                            }
                            String dVal = "";
                            if (targetIncomingParameters.containsKey("CBOARP" + currViewBy)) {
                                dVal = targetIncomingParameters.get("CBOARP" + currViewBy).toString();
                            }
                            columnEdgeDrillUrl += "&CBOARP" + currViewBy + "=";
                        }
                    }

                    targetViewByMain.put(viewByID, viewByArrayList);
                    count++;

                }
    }

        }
 basisValues = new ArrayList();
        ArrayList viewByArrayList = new ArrayList();
        String basisQ = "select * from prg_target_view_master where target_id=" + this.targetId + " and label_name='On Basis'";
        PbReturnObject retObj2 = new PbReturnObject();
        try {
            retObj2 = execSelectSQL(basisQ);
        } catch (SQLException s) {
            logger.error("Exception:", s);
        }
        viewByID = retObj2.getFieldValueString(0, 0);
        viewByName = retObj2.getFieldValueString(0, 1);
        viewByArrayList.add("CBO" + retObj2.getFieldValueString(0, 8));
        viewByArrayList.add("CBOVIEW_BY" + retObj2.getFieldValueString(0, 0));
        viewByArrayList.add(retObj2.getFieldValueString(0, 7));
        //viewByArrayList.add(retObj.getFieldValueString(looper, colNames[15]));

        if (targetIncomingParameters.get("CBOVIEW_BY" + viewByID) != null) {
            basisValues.add(targetIncomingParameters.get("CBOVIEW_BY" + viewByID));
            viewByArrayList.add(targetIncomingParameters.get("CBOVIEW_BY" + viewByID));
        } else {
            basisValues.add("CBOVIEW_BY" + retObj2.getFieldValueString(0, 0));
            viewByArrayList.add(retObj2.getFieldValueString(0, 6));
        }

        if (targetIncomingParameters.get("CBOVIEW_BY" + viewByID) != null) {
            if (targetIncomingParameters.get("CBOVIEW_BY" + viewByID).toString().equalsIgnoreCase("Absolute")) {
                viewByArrayList.add("Absolute");
            } else {
                viewByArrayList.add("Percentage");
            }
        } else {
            viewByArrayList.add("Absolute");
        }
        targetViewByMain.put(viewByID, viewByArrayList);
          /////////////Code for Time Setup/////////

        sqlstr = "";

        try {
            sqlstr = resBundle.getString("getTargetParameterTimeInfo");
            Obj = new Object[1];
            Obj[0] = this.targetId;
            finalQuery = buildQuery(sqlstr, Obj);
            retObj = execSelectSQL(finalQuery);

        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        colNames = retObj.getColumnNames();
        psize = retObj.getRowCount();
        ProgenParam pParam = new ProgenParam();
        timeDetailsMap = new LinkedHashMap();
        timeDetailsArray = new ArrayList();

        String perioType = "";
        if (targetIncomingParameters.containsKey("CBO_PRG_PERIOD")) {
            perioType = targetIncomingParameters.get("CBO_PRG_PERIOD").toString();
        }

        if (psize > 0) {
            timeDetailsArray.add(retObj.getFieldValueString(0, colNames[1]));
            timeDetailsArray.add(retObj.getFieldValueString(0, colNames[2]));
            for (int looper = 0; looper < psize; looper++) {
                ArrayList timeInfo = new ArrayList();
                if (targetIncomingParameters.get(("CBO_" + (retObj.getFieldValueString(looper, colNames[3])))) == null) {
                    temp = (retObj.getFieldValueString(looper, colNames[3])).trim();
                    {//default Value
                        if (retObj.getFieldValueString(looper, colNames[1]).equalsIgnoreCase("Day")) {
                            ArrayList selectedDates = getSelectedDates();
                            if (temp.equalsIgnoreCase("AS_OF_DATE")) {
                                timeInfo.add((String) selectedDates.get(0));
                            } else if (temp.equalsIgnoreCase("AS_OF_DATE1")) {
                                timeInfo.add((String) selectedDates.get(1));
                            } else if (temp.equalsIgnoreCase("PRG_PERIOD")) {
                                timeInfo.add((String) selectedDates.get(2));
                            }
                        } else if (retObj.getFieldValueString(looper, colNames[1]).equalsIgnoreCase("Month")) {
                            ArrayList selectedMonths = getSelectedMonths();
                            if (temp.equalsIgnoreCase("AS_OF_MONTH")) {
                                timeInfo.add((String) selectedMonths.get(0));
                            } else if (temp.equalsIgnoreCase("AS_OF_MONTH1")) {
                                timeInfo.add((String) selectedMonths.get(1));
                            } else if (temp.equalsIgnoreCase("PRG_PERIOD")) {
                                timeInfo.add((String) selectedMonths.get(2));
                            }
                        } else if (retObj.getFieldValueString(looper, colNames[1]).equalsIgnoreCase("Quarter") || retObj.getFieldValueString(looper, colNames[1]).equalsIgnoreCase("Qtr")) {
                            ArrayList selectedQtrs = getSelectedQtrs();
                            if (temp.equalsIgnoreCase("AS_OF_QTR")) {
                                timeInfo.add((String) selectedQtrs.get(0));
                            } else if (temp.equalsIgnoreCase("AS_OF_QTR1")) {
                                timeInfo.add((String) selectedQtrs.get(1));
                            } else if (temp.equalsIgnoreCase("PRG_PERIOD")) {
                                timeInfo.add((String) selectedQtrs.get(2));
                            }
                        } else if (retObj.getFieldValueString(looper, colNames[1]).equalsIgnoreCase("Year")) {
                            ArrayList selectedYears = getSelectedYears();
                            if (temp.equalsIgnoreCase("AS_OF_YEAR")) {
                                timeInfo.add((String) selectedYears.get(0));
                            } else if (temp.equalsIgnoreCase("AS_OF_YEAR1")) {
                                timeInfo.add((String) selectedYears.get(1));
                            } else if (temp.equalsIgnoreCase("PRG_PERIOD")) {
                                timeInfo.add((String) selectedYears.get(2));
                            }
                        }
                    }

                } else {
                    temp = (retObj.getFieldValueString(looper, colNames[3])).trim();
                    timeInfo.add(targetIncomingParameters.get("CBO_" + (retObj.getFieldValueString(looper, colNames[3]))));
                }
                timeInfo.add("CBO_" + temp);
                timeInfo.add(retObj.getFieldValueString(looper, colNames[4]));
                timeInfo.add(retObj.getFieldValueString(looper, colNames[5]));
                timeInfo.add(retObj.getFieldValueString(looper, colNames[6]));
                timeInfo.add(timeInfo.get(0));

                timeInfo.add(temp);
                timeDetailsArray.add(timeInfo.get(0));
                timeDetailsMap.put(timeInfo.get(6), timeInfo);


            }
        }
        if (targetColViewbyValues.get(0).toString().equalsIgnoreCase("Time")) {
            String nextTime = null;
            String currentTime = "";
            ArrayList currentList = new ArrayList();
            currentList = (ArrayList) timeDetailsMap.get("PRG_PERIOD");
            currentTime = currentList.get(0).toString();
            nextTime = getChildTimeId(currentTime, targetId);
            columnEdgeDrillUrl += "&CBO_PRG_PERIOD=" + nextTime + "&CBO_AS_OF_" + currentTime.toUpperCase() + "=~&CBO_AS_OF_" + currentTime.toUpperCase() + "1=~";
            }
       

    }

    public String getParameterDispName(String paramElementId) {
        String paramName = "";
        if (targetParameters != null) {
            ArrayList paramInfo = (ArrayList) targetParameters.get(paramElementId);
            if (paramInfo != null) {
                paramName = String.valueOf(paramInfo.get(1));
            }
        }
        return paramName;
    }

    public String getChildElementId(String elementId) {
        ArrayList paraInfo = new ArrayList();
        String NextElementId = null;
        paraInfo = (ArrayList) targetParameters.get(elementId);
        if (paraInfo != null) {
            if (paraInfo.get(2) != null) {
                NextElementId = paraInfo.get(2).toString();
            }
        }
        return (NextElementId);
    }

    public String setTargetViewByQuery(String key, HashMap hm, String currElementId) {
        String str = "";
        if (key.equalsIgnoreCase("CBOVIEW_BY_NAME")) {
            //str = "select distinct element_id,param_disp_name from prg_target_param_details where target_id="+this.targetId;
            //  str = "select distinct element_id,param_disp_name,rel_level from prg_target_param_details where target_id="+this.targetId+" and dim_id in (select distinct dim_id from prg_target_param_details where " +
            //  "element_id="+currElementId+") order by rel_level";
            str = "select distinct element_id,param_disp_name,rel_level from prg_target_param_details where target_id=" + this.targetId + " union select 0,'Overall target',1 from dual";
            primAnalyzeBy = currElementId;
        }
        if (key.equalsIgnoreCase("CBOVIEW_BY_NAME1")) {
            str = " select 'Time' view_by,'Time' view_by1 from dual union all select distinct to_char(element_id),param_disp_name from "
                    + "prg_target_param_details where target_id=" + this.targetId;// and dim_id not in (select distinct dim_id from prg_target_param_details " +
            // "where element_id="+primAnalyzeBy+")";
        }
        if (key.equalsIgnoreCase("CBOVIEW_BY_NAME2")) {
            str = "select 'Absolute' view_by,'Absolute' view_by1 from dual union   select 'Percentage' view_by,'Percentage' view_by1 from dual";
        }
        return str;
    }

    public ArrayList getSelectedDates() {
        ArrayList selectedDates = new ArrayList();
        // targetId = getTargetId();
        try {
            Connection con = ProgenConnection.getInstance().getConnection();
//            Statement st = con.createStatement();
            String query="select to_char(target_start_date,'mm/dd/yyyy') as start_date,to_char(target_end_date,'mm/dd/yyyy') as end_date,"
                    + "min_time_level  from target_master where target_Id='?'";
//            ResultSet rs = st.executeQuery(query);
            PreparedStatement st=con.prepareStatement(query);  // ByPrabal for avoiding sql Injection
            st.setString(1,  this.targetId );
             ResultSet rs =st.executeQuery();
            PbReturnObject targetDates = new PbReturnObject(rs);
            if (rs != null) {
                rs.close();
            }
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
        // targetId = getTargetId();
        try {
            Connection con = ProgenConnection.getInstance().getConnection();
            String qry="select * from target_master where target_Id='?'";
            PreparedStatement st = con.prepareStatement(resetPath);
            st.setString(1,  this.targetId );
            // ResultSet rs=st.executeQuery("select target_start_month,target_end_month," +
            //         "min_time_level  from target_master where target_Id='"+this.targetId+"'");
            ResultSet rs = st.executeQuery();
     PbReturnObject targetMonths = new PbReturnObject(rs);
            if (rs != null) {
                rs.close();
            }
            if (st != null) {
                st.close();
            }
            if (con != null) {
                con.close();
            }

            String timeLevel = targetMonths.getFieldValueString(0, "MIN_TIME_LEVEL");

            if (timeLevel.equalsIgnoreCase("Month")) {
                selectedMonths.add(setStringValue(targetMonths.getFieldValueString(0, "TARGET_START_MONTH")));
                selectedMonths.add(setStringValue(targetMonths.getFieldValueString(0, "TARGET_END_MONTH")));
                selectedMonths.add(setStringValue(targetMonths.getFieldValueString(0, "MIN_TIME_LEVEL")));
            } else if (timeLevel.equalsIgnoreCase("Year")) {
        String startMQ = "select MON_NAME from prg_acn_mon_denom where cyear=" + targetMonths.getFieldValueString(0, "TARGET_START_YEAR") + " order by py_mon";
                String endMQ = "select MON_NAME from prg_acn_mon_denom where cyear=" + targetMonths.getFieldValueString(0, "TARGET_END_YEAR") + " order by py_mon";
                PbReturnObject startObj = execSelectSQL(startMQ);
                PbReturnObject endObj = execSelectSQL(endMQ);
                selectedMonths = new ArrayList();
                selectedMonths.add(startObj.getFieldValueString(0, 0));
                selectedMonths.add(endObj.getFieldValueString(endObj.getRowCount() - 1, 0));
                selectedMonths.add(setStringValue(targetMonths.getFieldValueString(0, "MIN_TIME_LEVEL")));

            } else if (timeLevel.equalsIgnoreCase("Qtr") || timeLevel.equalsIgnoreCase("Quarter")) {
                String startMQ = "select distinct mon_name from prg_acn_mon_denom where cmqtr_code='" + targetMonths.getFieldValueString(0, "TARGET_START_QTR") + "'";
                String endMQ = "select distinct mon_name from prg_acn_mon_denom where cmqtr_code='" + targetMonths.getFieldValueString(0, "TARGET_END_QTR") + "'";
                PbReturnObject startObj = execSelectSQL(startMQ);
                PbReturnObject endObj = execSelectSQL(endMQ);
                selectedMonths = new ArrayList();
                selectedMonths.add(startObj.getFieldValueString(0, 0));
                selectedMonths.add(endObj.getFieldValueString(endObj.getRowCount() - 1, 0));
                selectedMonths.add(setStringValue(targetMonths.getFieldValueString(0, "MIN_TIME_LEVEL")));

            }

        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        return selectedMonths;
    }

    public ArrayList getSelectedMonths(String pType, String stDate, String endDate, HashMap targetIncomingParameters, String preTimeLevel) {
        ArrayList selectedMonths = new ArrayList();
        // targetId = getTargetId();
        try {
            Connection con = ProgenConnection.getInstance().getConnection();
//            Statement st = con.createStatement();
            String query="select * from target_master where target_Id='?'";
            PreparedStatement st = con.prepareStatement(query);
            st.setString(1, this.targetId );
            // ResultSet rs=st.executeQuery("select target_start_month,target_end_month," +
            //         "min_time_level  from target_master where target_Id='"+this.targetId+"'");
            ResultSet rs = st.executeQuery();
      PbReturnObject targetMonths = new PbReturnObject(rs);
            if (rs != null) {
                rs.close();
            }
            if (st != null) {
                st.close();
            }
            if (con != null) {
                con.close();
            }

            String timeLevel = targetMonths.getFieldValueString(0, "MIN_TIME_LEVEL");
            if (timeLevel.equalsIgnoreCase("Month")) {
                if (pType.equalsIgnoreCase("Month")) {
                    selectedMonths.add(setStringValue(targetMonths.getFieldValueString(0, "TARGET_START_MONTH")));
                    selectedMonths.add(setStringValue(targetMonths.getFieldValueString(0, "TARGET_END_MONTH")));
                    selectedMonths.add(setStringValue(targetMonths.getFieldValueString(0, "MIN_TIME_LEVEL")));
                }
                if (pType.equalsIgnoreCase("Day")) {
                    String startMQ = "";
                    String endMQ = "";
                    if (targetIncomingParameters.containsKey("CBO_AS_OF_MONTH")) {
                        startMQ = " select to_char(ddate,'mm/dd/yyyy') from pr_day_denom where to_char(ddate,'MON-yy')='" + targetIncomingParameters.get("CBO_AS_OF_MONTH") + "' order by daysofyear";
                        endMQ = startMQ;
                    } else {
                        startMQ = "select distinct to_char(cm_st_date,'mm/dd/yyyy')  from prg_acn_mon_denom where mon_name='" + targetMonths.getFieldValueString(0, "TARGET_START_MONTH") + "'";
                        endMQ = "select distinct to_char(cm_end_date,'mm/dd/yyyy')  from prg_acn_mon_denom where mon_name='" + targetMonths.getFieldValueString(0, "TARGET_END_MONTH") + "'";
                    }
                    PbReturnObject startObj = execSelectSQL(startMQ);
                    PbReturnObject endObj = execSelectSQL(endMQ);
                    selectedMonths = new ArrayList();
                    selectedMonths.add(startObj.getFieldValueString(0, 0));
                    selectedMonths.add(endObj.getFieldValueString(endObj.getRowCount() - 1, 0));
                    selectedMonths.add(setStringValue(targetMonths.getFieldValueString(0, "MIN_TIME_LEVEL")));
                }
            } else if (timeLevel.equalsIgnoreCase("Year")) {
                if (pType.equalsIgnoreCase("Month")) {
                    String startMQ = "";
                    if (!stDate.equalsIgnoreCase("")) {
                        startMQ = "select MON_NAME from prg_acn_mon_denom where cyear=" + targetMonths.getFieldValueString(0, "TARGET_START_YEAR") + " order by py_mon";
                    } else {
                        startMQ = "select MON_NAME from prg_acn_mon_denom where cyear=" + stDate + " order by py_mon";
                    }
                    String endMQ = "";
                    if (!endDate.equalsIgnoreCase("")) {
                        endMQ = "select MON_NAME from prg_acn_mon_denom where cyear=" + endDate + " order by py_mon";
                    } else {
                        endMQ = "select MON_NAME from prg_acn_mon_denom where cyear=" + targetMonths.getFieldValueString(0, "TARGET_END_YEAR") + " order by py_mon";
                    }
                    if (targetIncomingParameters.containsKey("CBO_AS_OF_" + preTimeLevel.toUpperCase())) {
                        stDate = (String) targetIncomingParameters.get("CBO_AS_OF_" + preTimeLevel.toUpperCase());
                        endDate = (String) targetIncomingParameters.get("CBO_AS_OF_" + preTimeLevel.toUpperCase());
                        if (preTimeLevel.equalsIgnoreCase("QTR") && pType.equalsIgnoreCase("Month")) {
                            if (stDate.contains("Q")) {
                                stDate = stDate.substring(1);
                            }
                            if (endDate.contains("Q")) {
                                endDate = endDate.substring(1);
                            }

                            startMQ = "select mon_name from prg_acn_mon_denom where cmqtr_code='Q" + stDate + "'";
                            endMQ = "select mon_name from prg_acn_mon_denom where cmqtr_code='Q" + endDate + "'";

                        }
                    }

                    PbReturnObject startObj = execSelectSQL(startMQ);
                    PbReturnObject endObj = execSelectSQL(endMQ);
                    selectedMonths = new ArrayList();
                    selectedMonths.add(startObj.getFieldValueString(0, 0));
                    selectedMonths.add(endObj.getFieldValueString(endObj.getRowCount() - 1, 0));
                    selectedMonths.add(setStringValue(targetMonths.getFieldValueString(0, "MIN_TIME_LEVEL")));
                } else if (pType.equalsIgnoreCase("Day")) {
                    String startMQ = "";
                      String endMQ = "";
                    //if(!stDate.equalsIgnoreCase(""))
                    if (targetIncomingParameters.containsKey("CBO_AS_OF_MONTH")) {

                        startMQ = " select to_char(ddate,'mm/dd/yyyy') from pr_day_denom where to_char(ddate,'MON-yy')='" + targetIncomingParameters.get("CBO_AS_OF_MONTH") + "' order by daysofyear";
                        endMQ = startMQ;
                    } else {
                        startMQ = "select to_char(ddate,'mm/dd/yyyy') from pr_day_denom where cyear=" + targetMonths.getFieldValueString(0, "TARGET_START_YEAR");
                        endMQ = "select to_char(ddate,'mm/dd/yyyy') from pr_day_denom where cyear=" + targetMonths.getFieldValueString(0, "TARGET_END_YEAR");
                    }
                    PbReturnObject startObj = execSelectSQL(startMQ);
                    PbReturnObject endObj = execSelectSQL(endMQ);
                    selectedMonths = new ArrayList();
                    selectedMonths.add(startObj.getFieldValueString(0, 0));
                    selectedMonths.add(endObj.getFieldValueString(endObj.getRowCount() - 1, 0));
                    selectedMonths.add(setStringValue(targetMonths.getFieldValueString(0, "MIN_TIME_LEVEL")));
                } else if (pType.equalsIgnoreCase("Qtr") || pType.equalsIgnoreCase("Quarter")) {
                    String startMQ = "";
                    String endMQ = "";
                    if (targetIncomingParameters.containsKey("CBO_AS_OF_YEAR")) {
                        startMQ = "select DISTINCT cmqtr_code, pmyqtr from prg_acn_mon_denom where cyear=" + stDate + " order by pmyqtr";
                        endMQ = startMQ;
                    } else {
                        startMQ = "select DISTINCT cmqtr_code, pmyqtr from prg_acn_mon_denom where cyear=" + targetMonths.getFieldValueString(0, "TARGET_START_YEAR") + " order by pmyqtr";
                        endMQ = "select DISTINCT cmqtr_code, pmyqtr from prg_acn_mon_denom where cyear=" + targetMonths.getFieldValueString(0, "TARGET_END_YEAR") + " order by pmyqtr";
                    }
                    PbReturnObject startObj = execSelectSQL(startMQ);
                    PbReturnObject endObj = execSelectSQL(endMQ);
                    selectedMonths = new ArrayList();
                    selectedMonths.add(startObj.getFieldValueString(0, 0));
                    selectedMonths.add(endObj.getFieldValueString(endObj.getRowCount() - 1, 0));
                    selectedMonths.add(setStringValue(targetMonths.getFieldValueString(0, "MIN_TIME_LEVEL")));
                }
            } else if (timeLevel.equalsIgnoreCase("Qtr") || timeLevel.equalsIgnoreCase("Quarter")) {
                if (pType.equalsIgnoreCase("Quarter") || pType.equalsIgnoreCase("Qtr")) {
                    selectedMonths.add(setStringValue(targetMonths.getFieldValueString(0, "TARGET_START_QTR")));
                    selectedMonths.add(setStringValue(targetMonths.getFieldValueString(0, "TARGET_END_QTR")));
                    selectedMonths.add(setStringValue(targetMonths.getFieldValueString(0, "MIN_TIME_LEVEL")));
                }
                if (pType.equalsIgnoreCase("Month")) {
                    String startMQ = "";
                    String endMQ = "";
                    if (!stDate.equalsIgnoreCase("")) {
                        startMQ = "select distinct mon_name from prg_acn_mon_denom where cmqtr_code='" + targetMonths.getFieldValueString(0, "TARGET_START_QTR") + "'";
                    } else {
                        startMQ = "select distinct mon_name from prg_acn_mon_denom where cmqtr_code='" + stDate + "'";
                    }
                    if (!endDate.equalsIgnoreCase("")) {
                        endMQ = "select distinct mon_name from prg_acn_mon_denom where cmqtr_code='" + endDate + "'";
                    } else {
                        endMQ = "select distinct mon_name from prg_acn_mon_denom where cmqtr_code=" + targetMonths.getFieldValueString(0, "TARGET_END_QTR") + "";
                    }
                    if (targetIncomingParameters.containsKey("CBO_AS_OF_" + preTimeLevel.toUpperCase())) {

                        stDate = (String) targetIncomingParameters.get("CBO_AS_OF_" + preTimeLevel.toUpperCase());
                        endDate = (String) targetIncomingParameters.get("CBO_AS_OF_" + preTimeLevel.toUpperCase());
                        if (preTimeLevel.equalsIgnoreCase("QTR") && pType.equalsIgnoreCase("Month")) {
                            startMQ = "select mon_name from prg_acn_mon_denom where cmqtr_code='Q" + stDate + "'";
                            endMQ = "select mon_name from prg_acn_mon_denom where cmqtr_code='Q" + endDate + "'";
                        }
                        PbReturnObject startObj = execSelectSQL(startMQ);
                        PbReturnObject endObj = execSelectSQL(endMQ);
                        selectedMonths = new ArrayList();
                        selectedMonths.add(startObj.getFieldValueString(0, 0));
                        selectedMonths.add(endObj.getFieldValueString(endObj.getRowCount() - 1, 0));
                        selectedMonths.add(setStringValue(targetMonths.getFieldValueString(0, "MIN_TIME_LEVEL")));
                    }
                }
                if (pType.equalsIgnoreCase("Day")) {
                    String startMQ = "";
                    String endMQ = "";
                    //if(!stDate.equalsIgnoreCase(""))
                    if (targetIncomingParameters.containsKey("CBO_AS_OF_MONTH")) {

                        startMQ = " select to_char(ddate,'mm/dd/yyyy') from pr_day_denom where to_char(ddate,'MON-yy')='" + targetIncomingParameters.get("CBO_AS_OF_MONTH") + "' order by daysofyear";
                        endMQ = startMQ;
                    } else {
                        startMQ = "select distinct to_char(cmq_st_date,'mm/dd/yyyy') from prg_acn_mon_denom where cmqtr_code='" + targetMonths.getFieldValueString(0, "TARGET_START_QTR");
                        endMQ = "select distinct to_char(cmq_end_date,'mm/dd/yyyy') from prg_acn_mon_denom where cmqtr_code='=" + targetMonths.getFieldValueString(0, "TARGET_END_QTR");
                    }
                    PbReturnObject startObj = execSelectSQL(startMQ);
                    PbReturnObject endObj = execSelectSQL(endMQ);
                    selectedMonths = new ArrayList();
                    selectedMonths.add(startObj.getFieldValueString(0, 0));
                    selectedMonths.add(endObj.getFieldValueString(endObj.getRowCount() - 1, 0));
                    selectedMonths.add(setStringValue(targetMonths.getFieldValueString(0, "MIN_TIME_LEVEL")));
                }
            } else if (timeLevel.equalsIgnoreCase("Day")) {
                selectedMonths = new ArrayList();
                String sDateQ = "select to_char(ddate,'mm/dd/yyyy') from pr_day_denom where "
                        + "ddate=(select target_start_date from target_master where target_id=" + this.targetId + ")";
                String eDateQ = "select to_char(ddate,'mm/dd/yyyy') from pr_day_denom where "
                        + "ddate=(select target_end_date from target_master where target_id=" + this.targetId + ")";
PbReturnObject sDateObj = execSelectSQL(sDateQ);
                PbReturnObject eDateObj = execSelectSQL(eDateQ);

                selectedMonths.add(sDateObj.getFieldValueString(0, 0));
                selectedMonths.add(eDateObj.getFieldValueString(eDateObj.getRowCount() - 1, 0));
                selectedMonths.add(setStringValue(targetMonths.getFieldValueString(0, "MIN_TIME_LEVEL")));
            }

        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        return selectedMonths;
    }

    public ArrayList getSelectedQtrs() {
        ArrayList selectedQtrs = new ArrayList();
        // targetId = getTargetId();
        try {
            Connection con = ProgenConnection.getInstance().getConnection();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select target_start_qtr,target_end_qtr,"
                    + "min_time_level  from target_master where target_Id='" + this.targetId + "'");
           PbReturnObject targetQtrs = new PbReturnObject(rs);
            if (rs != null) {
                rs.close();
            }
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

    public ArrayList getSelectedYears() {
        ArrayList selectedYears = new ArrayList();
        // targetId = getTargetId();
        try {
            Connection con = ProgenConnection.getInstance().getConnection();
//            Statement st = con.createStatement();
//            ResultSet rs = st.executeQuery("select target_start_year,target_end_year,"
//                    + "min_time_level  from target_master where target_Id='" + this.targetId + "'");
PreparedStatement st=con.prepareStatement("select target_start_year,target_end_year,min_time_level  from target_master where target_Id='?'");  
st.setString(1, this.targetId );
ResultSet rs=st.executeQuery();  
            PbReturnObject targetYears = new PbReturnObject(rs);
            if (rs != null) {
                rs.close();
            }
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

    public String getPrimaryTargetParameter() {
        String getTargetPrimParam = "";
        try {
            Connection con = ProgenConnection.getInstance().getConnection();
//            Statement st = con.createStatement();
            PreparedStatement st=con.prepareStatement("select distinct target_prim_param from target_master where target_Id='?'");  
st.setString(1, this.targetId );
ResultSet rs=st.executeQuery(); 
//            ResultSet rs = st.executeQuery();
            PbReturnObject targetPrimParam = new PbReturnObject(rs);
            if (rs != null) {
                rs.close();
            }
            if (st != null) {
                st.close();
            }
            if (con != null) {
                con.close();
            }

            getTargetPrimParam = (setStringValue(targetPrimParam.getFieldValue(0, 0)));

        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        return getTargetPrimParam;
    }

    public String setStringValue(Object obj) {
        if (obj == null) {
            return (null);
        } else {
            return (obj.toString());
        }
    }

    public String getChildElementId(String elementId, String targetId) {
        ArrayList paraInfo = new ArrayList();
        String NextElementId = null;
        PbReturnObject retObj = null;
        String[] colNames;
        if (!elementId.equalsIgnoreCase("TIME")) {
            if (!elementId.equalsIgnoreCase("Overall Target")) {
                String sqlstr = "";

                sqlstr = "";// cant be change due to PbReturnObject class firing query
                sqlstr = "select element_id,child_element_id, param_disp_name from prg_target_param_details where target_id=" + targetId + " and element_id=" + elementId;
                try {
                    retObj = execSelectSQL(sqlstr);

                } catch (SQLException ex) {
                    logger.error("Exception:", ex);
                }

                colNames = retObj.getColumnNames();
                int psize = retObj.getRowCount();

                if (psize > 0) {
                    NextElementId = retObj.getFieldValueString(0, "CHILD_ELEMENT_ID");
                }
            }
        }



        return (NextElementId);
    }

    public String getChildTimeId(String periodType, String targetId) {
        ArrayList paraInfo = new ArrayList();
        String NextElementId = null;
        PbReturnObject retObj = null;
        String[] colNames;
        String sqlstr = "";
        sqlstr = "";
        sqlstr = "select level_id,level_name from targettimelevels where target_id=" + targetId + " order by level_id";
        ArrayList timeList = new ArrayList();
        int n = 0;
        try {
            retObj = execSelectSQL(sqlstr);
            for (int i = 0; i < retObj.getRowCount(); i++) {
                timeList.add(retObj.getFieldValueString(i, "LEVEL_NAME"));
                if (retObj.getFieldValueString(i, "LEVEL_NAME").equalsIgnoreCase(periodType)) {
                    n = i + 1;
                }
            }

        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }

        colNames = retObj.getColumnNames();
        int psize = retObj.getRowCount();

        if (psize > 0) {
            if (n < retObj.getRowCount()) {
                NextElementId = retObj.getFieldValueString(n, "LEVEL_NAME");
            }
        }
return (NextElementId);
    }

    public String getParentTimeId(String periodType, String targetId) {
        ArrayList paraInfo = new ArrayList();
        String NextElementId = null;
        PbReturnObject retObj = null;
        String[] colNames;
        String sqlstr = "";
        sqlstr = "select level_id,level_name from targettimelevels where target_id=" + targetId + " order by level_id";
        ArrayList timeList = new ArrayList();
        int n = 0;
        try {
            retObj = execSelectSQL(sqlstr);
            for (int i = 0; i < retObj.getRowCount(); i++) {
                timeList.add(retObj.getFieldValueString(i, "LEVEL_NAME"));
                if (retObj.getFieldValueString(i, "LEVEL_NAME").equalsIgnoreCase(periodType)) {
                    if (i != 0) {
                        n = i - 1;
                    }
                }
            }
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }

        colNames = retObj.getColumnNames();
        int psize = retObj.getRowCount();

        if (psize > 0) {
            if (n < retObj.getRowCount()) {
                NextElementId = retObj.getFieldValueString(n, "LEVEL_NAME");
            }
        }

        return (NextElementId);
    }
}
