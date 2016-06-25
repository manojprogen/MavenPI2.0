package com.progen.targetview.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import prg.db.*;
import java.util.ResourceBundle;
import org.jdom.Element;
import prg.targetparam.qdparams.PbTargetParamTable;
import prg.targetparam.qdparams.PbTargetValuesParam;
import utils.db.ProgenConnection;
import org.apache.log4j.*;

public class PbTargetDAO extends PbDb {

    PbTargetResourceBundle resBundle = (PbTargetResourceBundle) ResourceBundle.getBundle("com.progen.targetview.db.PbTargetResourceBundle");
    public static Logger logger = Logger.getLogger(PbTargetDAO.class);
    // to save the data when any paramter is selected in the parimary view by and time is there in secondary

    public void savePrimSecNew(Session sess) throws Exception {
        Connection con = ProgenConnection.getInstance().getConnection();
        ResultSet rsData;
        PreparedStatement ps;
        ps = null;
        rsData = null;
        PbTargetParamTable pTable = (PbTargetParamTable) sess.getObject("prg.targetparam.qdparams.PbTargetParamTable");
        PbTargetValuesParam targetParam = (PbTargetValuesParam) sess.getObject("prg.targetparam.qdparams.PbTargetValuesParam");
        String targetId = targetParam.getTargetId();
        String periodType = targetParam.getPeriodType();
//    String minTimeLevel = targetParam.getMinimumTimeLevel();
        HashMap nonAllIds = targetParam.getNonAllIds();
        String viewBy = targetParam.getViewByParameter();
//    String primParameterElementId =targetParam.getPrimaryParameter();
        ArrayList deleteQueries = new ArrayList();
        ArrayList updates = new ArrayList();
        String viewByElementId = "";
        String viewByColName = "";
        String nonAllDataClause = "";
        HashMap ratioComb = targetParam.getRatioComb();
        String colViewByParameter = targetParam.getSecViewByParameter();//setSecViewByParameter

        Object relSeqObj[] = new Object[1];
        relSeqObj[0] = "PRG_TARGET_DATA_SEQ";
        String factQuery = resBundle.getString("getSequenceNumber");
//   ArrayList delList = new ArrayList();
        String finalfactQuery = buildQuery(factQuery, relSeqObj);

        ArrayList insertQList = new ArrayList();
        Object tarOb[] = new Object[1];
        tarOb[0] = targetId;
        String getTargetTable = resBundle.getString("getTargetTable");
        String fingetTargetTable = buildQuery(getTargetTable, tarOb);
        PbReturnObject targetTabOb = execSelectSQL(fingetTargetTable);
        String targetTable = targetTabOb.getFieldValueString(0, "BUSS_TABLE_NAME");
        String measureName2 = targetTabOb.getFieldValueString(0, "MEASURE_NAME");
        String measureName = measureName2.trim().replace(" ", "_");

        String getElementIdsBussCols = resBundle.getString("getElementIdsBussCols");
        String fingetElementIdsBussCols = buildQuery(getElementIdsBussCols, tarOb);
        PbReturnObject bussColOb = execSelectSQL(fingetElementIdsBussCols);
        HashMap elementIds = new HashMap();
        String viewByName = "";
        bussColOb.writeString();
        String colDispName = "";
        String secColName = "";
        String secViewByElementId = "";
        for (int p = 0; p < bussColOb.getRowCount(); p++) {
            elementIds.put(bussColOb.getFieldValueString(p, "ELEMENT_ID"), bussColOb.getFieldValueString(p, "BUSS_COL_NAME"));
            //if(viewBy.equalsIgnoreCase(bussColOb.getFieldValueString(p,"PARAM_DISP_NAME"))){
            if (viewBy.equalsIgnoreCase(bussColOb.getFieldValueString(p, "ELEMENT_ID"))) {
                viewByElementId = bussColOb.getFieldValueString(p, "ELEMENT_ID");
                viewByColName = bussColOb.getFieldValueString(p, "BUSS_COL_NAME");
                viewByName = bussColOb.getFieldValueString(p, "PARAM_DISP_NAME");
                ////////////////////////////////////////////////////////////////////////////////////////////////////////.println(viewByElementId+" kk .. "+viewByColName+" .** "+viewByName);
            }
            if (colViewByParameter.equalsIgnoreCase(bussColOb.getFieldValueString(p, "ELEMENT_ID"))) {
                secViewByElementId = bussColOb.getFieldValueString(p, "ELEMENT_ID");
                secColName = bussColOb.getFieldValueString(p, "BUSS_COL_NAME");
                colDispName = bussColOb.getFieldValueString(p, "PARAM_DISP_NAME");
            }
        }

        String nonAll[] = (String[]) (nonAllIds.keySet()).toArray(new String[0]);
        HashMap nonAllNew = new HashMap();
        for (int y = 0; y < nonAll.length; y++) {
            String eleId = nonAll[y];
            String value = nonAllIds.get(eleId).toString();
            if (!value.equalsIgnoreCase("-1") && !value.equalsIgnoreCase("-1")) {
                nonAllNew.put(eleId, value);
            }
        }
        String nonAllStr[] = (String[]) (nonAllNew.keySet()).toArray(new String[0]);

        for (int y = 0; y < nonAllStr.length; y++) {
            String eleId = nonAllStr[y];
            String colName = elementIds.get(eleId).toString();
            String value = nonAllIds.get(eleId).toString();
            if (y == 0) {
                nonAllDataClause = " and " + colName + "='" + value + "'";
            } else if (y == 1) {
                nonAllDataClause = nonAllDataClause + "  " + colName + "='" + value + "'";
            } else {
                nonAllDataClause = nonAllDataClause + " and " + colName + "='" + value + "'";
            }
        }

        int obSize = 0;
        // to store all the param columns in ordered manner
        ArrayList allParamsValues = new ArrayList();
        allParamsValues.add("TARGET_ID");
        allParamsValues.add("T_DATE");
        allParamsValues.add("T_MONTH");
        allParamsValues.add("T_QTR");
        allParamsValues.add("T_YEAR");

        String insertQ = "";
        obSize = bussColOb.getRowCount();
        int totalCount = obSize + 9;

        for (int i = 0; i < bussColOb.getRowCount(); i++) {
            allParamsValues.add(bussColOb.getFieldValueString(i, 2).trim());
            if (i == 0 && (bussColOb.getRowCount() > 1)) {
                insertQ = insertQ + bussColOb.getFieldValueString(i, 2).trim() + ",";
            } else if ((i == 0) && (bussColOb.getRowCount() == 1)) {
                insertQ = insertQ + bussColOb.getFieldValueString(i, 2).trim();
            } else if (i == (bussColOb.getRowCount() - 1)) {
                insertQ = insertQ + bussColOb.getFieldValueString(i, 2).trim();
            } else {
                insertQ = insertQ + bussColOb.getFieldValueString(i, 2).trim() + ",";
            }
            // allValsMap.put(allParamObj.getFieldValueString(i,"PARAMETER_NAME").trim(),"All");
        }

        allParamsValues.add(measureName.trim().toUpperCase());
        allParamsValues.add("VIEWBY");

        allParamsValues.add("FACT_ID");
        allParamsValues.add("SECVIEWBY");

        String replaceVals = "";
        for (int y = 0; y < totalCount; y++) {
            if (y == 0 && (totalCount > 1)) {
                replaceVals = replaceVals + "'&'" + ",";
            } else if ((y == 0) && (totalCount == 1)) {
                replaceVals = replaceVals + "'&'";
            } else if (y == totalCount - 1) {
                replaceVals = replaceVals + "'&'";
            } else {
                replaceVals = replaceVals + "'&'" + ",";
            }

        }
        // to get the dimensions.Its members  and all info
        String getTargetParamsInfo = resBundle.getString("getTargetParamsInfo");
        String fingetTargetParamsInfo = buildQuery(getTargetParamsInfo, tarOb);
        PbReturnObject paramInfoOb = execSelectSQL(fingetTargetParamsInfo);
//         HashMap dimInfo = new HashMap();
        HashMap memBersInfo = new HashMap();
//         HashMap allElements = new HashMap();

        for (int p = 0; p < paramInfoOb.getRowCount(); p++) {
//             String dimId = paramInfoOb.getFieldValueString(p,"DIM_ID");
            String bussColName = "";
            if (elementIds.containsKey(paramInfoOb.getFieldValueString(p, "ELEMENT_ID"))) {
                bussColName = elementIds.get(paramInfoOb.getFieldValueString(p, "ELEMENT_ID")).toString();
            }
            ArrayList al = new ArrayList();
            al.add(0, paramInfoOb.getFieldValueString(p, "PARAM_DISP_NAME")); //name
            al.add(1, bussColName); //buss COl Name
            al.add(2, paramInfoOb.getFieldValueString(p, "CHILD_ELEMENT_ID")); // child id
            al.add(3, paramInfoOb.getFieldValueString(p, "REL_LEVEL")); //level
            memBersInfo.put(paramInfoOb.getFieldValueString(p, "ELEMENT_ID"), al);
        }
        //  String updateQuery = "update "+targetTable.trim()+" set "+measureName.trim().toUpperCase()+"=&"+where ;
        String updateQuery = "";// "update & set &=& where target_id=& "+nonAllDataClause+" and viewby=&";
        //Starting default parameters
        String initialParams = "insert into " + targetTable.trim() + "(TARGET_ID,T_DATE,T_MONTH,T_QTR,T_YEAR," + insertQ;
        String finalParameters = initialParams + "," + measureName.trim() + "," + " VIEWBY,FACT_ID,SECVIEWBY) values(" + replaceVals + ")";
        ////////////////////////////////////////////////////////////////////////////.println("finalParameters - "+finalParameters);
//       String secViewByName="";
        //      String colDispName="";
        // String secColName="";
        // String secViewByElementId="";
        //    if(ele)

        for (int i = 0; i < pTable.getRowCount(); i++) {

            PbTargetValuesParam targetParam2 = (PbTargetValuesParam) pTable.getRow(i);
            long measureVal = targetParam2.getMeasureVal();
            String updateFlag = targetParam2.getUpdateFlag();
            String dat = targetParam2.getDateVal();
            String paramVal = targetParam2.getPrimViewByValue();
            HashMap allValsMap = new HashMap();
            String primparameterVal = targetParam2.getPrimAnalyzeVal();
            String primAnalyzeVal = targetParam2.getPrimViewByValue();
            String primCol = "";

            if (elementIds.containsKey(viewByElementId)) {
                primCol = elementIds.get(viewByElementId).toString();
            }

            String secCol = "";
            if (elementIds.containsKey(colViewByParameter)) {
                secCol = elementIds.get(colViewByParameter).toString();
            }

            String key = paramVal + ":" + dat;
            float ratioN = 0;
            int oldValue = 0;
            if (ratioComb.containsKey(key)) {
                oldValue = ((Integer) ratioComb.get(key)).intValue();
                float divisionVal = (float) (measureVal - oldValue) / oldValue;
                ratioN = (float) 1 + divisionVal;
            }

            String[] a1 = (String[]) (nonAllIds.keySet()).toArray(new String[0]);
            for (int k = 0; k < a1.length; k++) {
                String eleId = a1[k];
                String pVal = nonAllIds.get(eleId).toString();
                if (elementIds.containsKey(eleId)) {
                    String colName = elementIds.get(eleId).toString();
                    if (!allValsMap.containsKey(colName)) {
                        allValsMap.put(colName, pVal);
                    }
                }
            }
            allValsMap.put("TARGET_ID", targetId);
            allValsMap.put(primCol, primparameterVal);
            allValsMap.put(secCol, primAnalyzeVal);
            // allValsMap.put(colName,paramVal);
            //insert
            if (updateFlag.equalsIgnoreCase("N")) {
                //allValsMap.put(viewByColName.trim(),paramVal);
                // allValsMap.put(primCol,paramVal);
                String tQuery = "";
                String t_year = "";//dat.substring(7);
                String t_Qtr = "";
                String t_mon = "";
                String datVal = "";
                if (periodType.equalsIgnoreCase("Day")) {
                    tQuery = "SELECT DISTINCT CQTR,CYEAR,to_char(ddate,'MON-yy') CMON ,to_char(ddate,'dd-MON-yy') dd  FROM pr_day_denom  WHERE to_char(DDATE,'mm/dd/yyyy')= '" + dat + "'";
                    PbReturnObject timeOb = execSelectSQL(tQuery);
                    // t_year = timeOb.getFieldValueString(0,"CYEAR");
                    // t_Qtr = timeOb.getFieldValueString(0,"CQTR")+"-"+timeOb.getFieldValueString(0,"CYEAR");
                    //t_mon = timeOb.getFieldValueString(0,"CMON");
                    datVal = timeOb.getFieldValueString(0, "DD");
                } else if (periodType.equalsIgnoreCase("Month")) {
                    tQuery = "select distinct  CQTR,CYEAR,to_char(CM_ST_DATE,'MON-yy') CMON from pr_day_denom where to_date(CM_ST_DATE,'dd-mon-yy')=to_date('" + dat + "','mm-yy')";
                    PbReturnObject timeOb = execSelectSQL(tQuery);
                    // t_year = timeOb.getFieldValueString(0,"CYEAR");
                    // t_Qtr = timeOb.getFieldValueString(0,"CQTR")+"-"+timeOb.getFieldValueString(0,"CYEAR");
                    t_mon = timeOb.getFieldValueString(0, "CMON");
                } else if (periodType.equalsIgnoreCase("Quarter")) {
                    tQuery = "select distinct CQTR,CYEAR,CMONTH,to_date(CM_ST_DATE,'dd-mon-yy') DD from pr_day_denom where to_date(CM_ST_DATE,'dd-mon-yy')=to_date('" + dat + "','dd-mon-yy')";
                    t_Qtr = dat.substring(1);
                } else if (periodType.equalsIgnoreCase("Year")) {
                    tQuery = "select distinct CQTR,CYEAR,CMONTH,to_date(CM_ST_DATE,'dd-mon-yy') DD from pr_day_denom where to_date(CM_ST_DATE,'dd-mon-yy')=to_date('" + dat + "','dd-mon-yy')";
                    t_year = dat;
                }

                int fact_id = getSequenceNumber(finalfactQuery);
//                allValsMap.put(measureName.trim().toUpperCase(), "" + measureVal + "");
                allValsMap.put(measureName.trim().toUpperCase(), String.valueOf(measureVal));
                allValsMap.put("TARGET_ID", "" + targetId + "");
                allValsMap.put("T_DATE", datVal);
                allValsMap.put("T_MONTH", t_mon);
                allValsMap.put("T_QTR", t_Qtr);
                allValsMap.put("T_YEAR", t_year);
//                allValsMap.put("FACT_ID", "" + fact_id + "");
                allValsMap.put("FACT_ID", String.valueOf(fact_id));
                allValsMap.put("VIEWBY", viewByName.trim());//colDispName
                allValsMap.put("SECVIEWBY", colDispName.trim());
                Object insertObjDy[] = new Object[allParamsValues.size()];
                for (int t = 0; t < allParamsValues.size(); t++) {
                    String colNames = (String) allParamsValues.get(t);
                    if (allValsMap.containsKey(colNames)) {
                        if ((String) allValsMap.get(colNames) == null) {
                            insertObjDy[t] = "All";
                        } else if (allValsMap.get(colNames) != null) {
                            insertObjDy[t] = (String) allValsMap.get(colNames);
                        } else {
                            insertObjDy[t] = "";
                        }
                    }
                }
                String finalInsertDyn = buildQuery(finalParameters, insertObjDy);
                insertQList.add(finalInsertDyn);
            } else if (updateFlag.equalsIgnoreCase("Y")) {
                String childDataQuery = "";
                String childElementId = "";
                String childEleName = "";
                ArrayList childDet = new ArrayList();
                String currentName = "";

                String parentName = "";
                if (memBersInfo.containsKey(viewByElementId)) {
                    ArrayList alDet = (ArrayList) memBersInfo.get(viewByElementId);
                    childElementId = alDet.get(2).toString();
                    currentName = alDet.get(0).toString();
                    if (memBersInfo.containsKey(childElementId)) {
                        childEleName = alDet.get(2).toString();
                        childDet = (ArrayList) memBersInfo.get(childElementId);
                        childEleName = childDet.get(0).toString();
                    }
                }
                HashMap updateVals = new HashMap();
                String updateQuery2 = "";
                //"update "+targetTable.trim()+" set "+measureName.trim().toUpperCase()+"=& where target_id="+targetId+" "+nonAllDataClause;
                if (periodType.equalsIgnoreCase("Month")) {
                    //updateQuery="update & set &=& where target_id='&' "+nonAllDataClause+" and viewby='&' and t_month='&' and "+elementIds.get(viewByElementId.trim()).toString()+"='"+paramVal+"' and secviewby='"+colDispName.trim()+"'";
                    updateQuery = "update & set &=& where target_id='&' " + nonAllDataClause + " and viewby='" + viewByName.trim() + "' and t_month='&' and " + primCol + "='" + primparameterVal + "' and " + secCol + "='" + primAnalyzeVal + "' and secviewby='" + colDispName.trim() + "'";
                    updateQuery2 = "update & set &=& where target_id='&' " + nonAllDataClause + " and viewby='" + primAnalyzeVal + "' and t_month='&' and " + primCol + "='" + primparameterVal + "' and " + secCol + "='" + viewByName.trim() + "' and secviewby='" + colDispName.trim() + "'";

                } else if (periodType.equalsIgnoreCase("Quarter") || periodType.equalsIgnoreCase("Qtr")) {
                    updateQuery = "update & set &=& where target_id='&' " + nonAllDataClause + " and viewby='" + viewByName.trim() + "' and t_qtr='&' and " + primCol + "='" + primparameterVal + "' and " + secCol + "='" + primAnalyzeVal + "' and secviewby='" + colDispName.trim() + "'";
                    updateQuery2 = "update & set &=& where target_id='&' " + nonAllDataClause + " and viewby='" + primAnalyzeVal + "' and t_qtr='&' and " + primCol + "='" + primparameterVal + "' and " + secCol + "='" + viewByName.trim() + "' and secviewby='" + colDispName.trim() + "'";

                } else if (periodType.equalsIgnoreCase("Year")) {
                    // allValsMap.put(primCol,primparameterVal);
                    //allValsMap.put(secCol,primAnalyzeVal);
                    updateQuery = "update & set &=& where target_id='&' " + nonAllDataClause + " and viewby='" + viewByName.trim() + "' and t_year='&' and " + primCol + "='" + primparameterVal + "' and " + secCol + "='" + primAnalyzeVal + "' and secviewby='" + colDispName.trim() + "'";
                    updateQuery2 = "update & set &=& where target_id='&' " + nonAllDataClause + " and viewby='" + primAnalyzeVal + "' and t_year='&' and " + primCol + "='" + primparameterVal + "' and " + secCol + "='" + viewByName.trim() + "' and secviewby='" + colDispName.trim() + "'";
                } else if (periodType.equalsIgnoreCase("Day")) {
                    updateQuery = "update & set &=& where target_id='&' " + nonAllDataClause + " and viewby='&' and t_date=to_date('&','mm-dd-yy') and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "' and secviewby='" + colDispName.trim() + "'";
                }
                //memBersInfo
                ////////////////////////////////////////////////////////////////////////////////////////////////////////.println(" memBersInfo "+memBersInfo);
                ////////////////////////////////////////////////////////////////////////////////////////////////////////.println(childEleName+" viewByElementId. "+viewByElementId);
                ArrayList alreadyUpdateFact = new ArrayList();

                Object update[] = new Object[5];
                update[0] = targetTable.trim();
                update[1] = measureName.trim().toUpperCase();
                update[2] = new Long(measureVal);
                update[3] = targetId;

                if (periodType.equalsIgnoreCase("Quarter")) {
                    update[4] = dat.substring(1);
                } else {
                    update[4] = dat;
                }
                String finupdateQuery = buildQuery(updateQuery, update);
                finupdateQuery = finupdateQuery + nonAllDataClause;
                ////////////////////////////////////////////////////////////////////////////.println(" finupdateQuery "+finupdateQuery);
                updateVals.put(paramVal, new Long(measureVal));

                String finupdateQuery2 = buildQuery(updateQuery2, update);
                insertQList.add(finupdateQuery);
                insertQList.add(finupdateQuery2);
                ////////////////////////////////////////////////////////////////////////////////////////////////////////.println(" periodType "+periodType);
                // as long as child id is not -1 loop
                PbReturnObject childDataOb = new PbReturnObject();
                if (!childElementId.equalsIgnoreCase("-1")) {
                    if (periodType.equalsIgnoreCase("Month")) {
                        childDataQuery = "select " + elementIds.get(childElementId.trim()).toString() + "," + measureName.trim() + ",fact_id from " + targetTable.trim() + " where target_id=" + targetId + " and t_month='" + dat + "' and viewby='" + childEleName + "' and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                        // execSelectSQL(childDataQuery);
                        ////////////////////////////////////////////////////////////////////////////////////////////////////////.println(" childDataQuery.. "+childDataQuery);
                    } else if (periodType.equalsIgnoreCase("Day")) {
                        childDataQuery = "select " + elementIds.get(childElementId.trim()).toString() + "," + measureName.trim() + ",fact_id from " + targetTable.trim() + " where target_id=" + targetId + " and t_date=to_date(to_char('" + dat + "'),'mm-dd-yy') and viewby='" + childEleName + "' and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                        // execSelectSQL(childDataQuery);
                        ////////////////////////////////////////////////////////////////////////////////////////////////////////.println(" childDataQuery... "+childDataQuery);
                    } else if (periodType.equalsIgnoreCase("Quarter")) {
                        childDataQuery = "select " + elementIds.get(childElementId.trim()).toString() + "," + measureName.trim() + ",fact_id from " + targetTable.trim() + " where target_id=" + targetId + " and t_qtr='" + dat.substring(1) + "' and viewby='" + childEleName + "' and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                        // execSelectSQL(childDataQuery);

                    } else if (periodType.equalsIgnoreCase("Year")) {
                        childDataQuery = "select " + elementIds.get(childElementId.trim()).toString() + "," + measureName.trim() + ",fact_id from " + targetTable.trim() + " where target_id=" + targetId + " and t_year='" + dat + "' and viewby='" + childEleName + "' and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                        // execSelectSQL(childDataQuery);
                        ////////////////////////////////////////////////////////////////////////////////////////////////////////.println(" childDataQuery.. "+childDataQuery);
                    }
                    ps = con.prepareStatement(childDataQuery);
                    rsData = ps.executeQuery();
                    // childDataOb = execSelectSQL(childDataQuery);
                    childDataOb = new PbReturnObject(rsData);
                }
                int totalR = 0;
                int allTot = 0;
                for (int m = 0; m < childDataOb.getRowCount(); m++) {
                    totalR = totalR + childDataOb.getFieldValueInt(m, measureName.trim().toUpperCase());
                }
                ////////////////////////////////////////////////////////////////////////////////////////////////////////.println(measureVal+" measureVal totalR "+totalR);
                //get the change in ratio
                float ratio = 0;
                float divisionVal = 0;
                // float ratioN=0;

                divisionVal = (float) (measureVal - totalR) / totalR;

                ////////////////////////////////////////////////////////////////////////////////////////////////////////.println(ratioN+" divisionVal "+divisionVal);
                if (ratioN == 0) {
                    ratioN = 1 + divisionVal;
                }
                //  if(totalR==0)
                //     ratioN = 1;
                ////////////////////////////////////////////////////////////////////////////////////////////////////////.println(periodType+" ratioN ...../ "+ratioN);
                while (!childElementId.equalsIgnoreCase("-1")) {

                    if (periodType.equalsIgnoreCase("Month")) {
                        childDataQuery = "select " + elementIds.get(childElementId.trim()).toString() + "," + measureName.trim() + ",fact_id from " + targetTable.trim() + " where target_id=" + targetId + " and t_month='" + dat + "' and viewby='" + childEleName + "' and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                        //childDataOb=execSelectSQL(childDataQuery);
                        ps = con.prepareStatement(childDataQuery);
                        rsData = ps.executeQuery();
                        childDataOb = new PbReturnObject(rsData);
                        ////////////////////////////////////////////////////////////////////////////////////////////////////////.println(" childDataQuery.. "+childDataQuery);
                    }
                    if (periodType.equalsIgnoreCase("Day")) {
                        childDataQuery = "select " + elementIds.get(childElementId.trim()).toString() + "," + measureName.trim() + ",fact_id from " + targetTable.trim() + " where target_id=" + targetId + " and t_date=to_date(to_char('" + dat + "'),'mm-dd-yy') and viewby='" + childEleName + "' and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                        // childDataOb=execSelectSQL(childDataQuery);
                        ps = con.prepareStatement(childDataQuery);
                        rsData = ps.executeQuery();
                        childDataOb = new PbReturnObject(rsData);
                        ////////////////////////////////////////////////////////////////////////////////////////////////////////.println(" childDataQuery.. "+childDataQuery);
                    }
                    if (periodType.equalsIgnoreCase("Quarter")) {
                        childDataQuery = "select " + elementIds.get(childElementId.trim()).toString() + "," + measureName.trim() + ",fact_id from " + targetTable.trim() + " where target_id=" + targetId + " and t_qtr='" + dat.substring(1) + "' and viewby='" + childEleName + "' and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                        // childDataOb=execSelectSQL(childDataQuery);
                        ps = con.prepareStatement(childDataQuery);
                        rsData = ps.executeQuery();
                        childDataOb = new PbReturnObject(rsData);
                        ////////////////////////////////////////////////////////////////////////////////////////////////////////.println(" childDataQuery.. "+childDataQuery);
                    }
                    if (periodType.equalsIgnoreCase("Year")) {
                        childDataQuery = "select " + elementIds.get(childElementId.trim()).toString() + "," + measureName.trim() + ",fact_id from " + targetTable.trim() + " where target_id=" + targetId + " and t_year='" + dat + "' and viewby='" + childEleName + "' and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                        // childDataOb=execSelectSQL(childDataQuery);
                        ps = con.prepareStatement(childDataQuery);
                        rsData = ps.executeQuery();
                        childDataOb = new PbReturnObject(rsData);
                        ////////////////////////////////////////////////////////////////////////////////////////////////////////.println(" childDataQuery.. "+childDataQuery);
                    }

                    // for updation start
                    int chilCounts = childDataOb.getRowCount();
                    if (chilCounts == 0) {
                        chilCounts = 1;
                    }
                    long individualCount = 1;
                    individualCount = measureVal / chilCounts;
                    for (int m = 0; m < childDataOb.getRowCount(); m++) {
                        int newVal = 0;
                        if (totalR != 0) {
                            ratio = measureVal / totalR;
                            ratio = (float) childDataOb.getFieldValueInt(m, measureName.trim().toUpperCase()) / totalR;
                            newVal = (int) (ratio * measureVal);
                        }
                        int newM = (int) Math.floor(ratioN * childDataOb.getFieldValueInt(m, measureName.trim().toUpperCase()));//;Math.round(ratioN * chilNos.getFieldValueInt(m, measureName.trim().toUpperCase()));

                        allTot = allTot + newM;
                        //////////////////////////////////////////////////////////////////////////////////////////////////.print("  --------- "+childDataOb.getFieldValueInt(m,"FACT_ID")+"....");
                        String fact_id = childDataOb.getFieldValueString(m, "FACT_ID");
                        String updateT = "update " + targetTable.trim() + " set " + measureName.trim() + "=& where fact_id=&";
                        Object updateChilds[] = new Object[2];
                        updateChilds[0] = Integer.valueOf(newM);
                        updateChilds[1] = fact_id;
                        updateVals.put(childDataOb.getFieldValueString(m, 0), Integer.valueOf(newVal));//individualCount));
                        String finalupdateT = buildQuery(updateT, updateChilds);

                        if (!alreadyUpdateFact.contains(Integer.valueOf(childDataOb.getFieldValueInt(m, "FACT_ID")))) {
                            finalupdateT = buildQuery(updateT, updateChilds);
                            alreadyUpdateFact.add(Integer.valueOf(childDataOb.getFieldValueInt(m, "FACT_ID")));
                            updates.add(finalupdateT);
                        }

                    }
                    //for updation over
                    if (memBersInfo.containsKey(childElementId)) {
                        childDet = (ArrayList) memBersInfo.get(childElementId);
                        childElementId = childDet.get(2).toString();
                        childEleName = childDet.get(0).toString();
                        parentName = currentName;
                        ////////////////////////////////////////////////////////////////////////////////////////////////////////.println(childEleName+" childEleName "+parentName+" childElementId "+childElementId);
                    }
                    //currentName=childEleName;
                }
                ////////////////////////////////////////////////////////////////////////////////////////////////////////.println(" updateVals "+updateVals);

            } else if (updateFlag.equalsIgnoreCase("D")) {
                ////////////////////////////////////////////////////////////////////////////////////////////////////////.println(" in delete.. ");
                String deleteQuery = "";
                String deleteQuery2 = "";
                if (periodType.equalsIgnoreCase("Month")) {
                    deleteQuery = "delete from " + targetTable.trim() + " where target_id=" + targetId + " and viewby='&' and t_month='&' " + nonAllDataClause + " and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                } else if (periodType.equalsIgnoreCase("Day")) {
                    deleteQuery = "delete from " + targetTable.trim() + " where target_id=" + targetId + " and viewby='&' and t_date=to_date('&','mm-dd-yy') " + nonAllDataClause + " and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                } else if (periodType.equalsIgnoreCase("Quarter")) {
                    deleteQuery = "delete from " + targetTable.trim() + " where target_id=" + targetId + " and viewby='&' and t_qtr='&' " + nonAllDataClause + " and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                } else if (periodType.equalsIgnoreCase("Year")) {
                    deleteQuery = "delete from " + targetTable.trim() + " where target_id=" + targetId + " and viewby='" + viewByName.trim() + "' and t_year='&' " + nonAllDataClause + " and " + elementIds.get(viewByElementId.trim()).toString() + "='" + primparameterVal + "' and secviewby='" + colDispName.trim() + "' and " + secCol + "='" + primAnalyzeVal + "'";
                    deleteQuery2 = "delete from " + targetTable.trim() + " where target_id=" + targetId + " and viewby='" + colDispName.trim() + "' and t_year='&' " + nonAllDataClause + " and " + elementIds.get(viewByElementId.trim()).toString() + "='" + primparameterVal + "' and secviewby='" + viewByName.trim() + "'  and " + secCol + "='" + primAnalyzeVal + "'";

                }

                Object delOb[] = new Object[1];
                //  delOb[0] = viewByName.trim();
                delOb[0] = dat;
                String finDeleteQ = buildQuery(deleteQuery, delOb);
                String finDelQ2 = buildQuery(deleteQuery2, delOb);
                deleteQueries.add(finDeleteQ);
                deleteQueries.add(finDelQ2);
                String childDataQuery = "";
                String childElementId = "";
                String childEleName = "";
                ArrayList childDet = new ArrayList();
                String currentName = "";

                String parentName = "";

            }

        }

        try {
            executeMultiple(deleteQueries, con);
            executeMultiple(insertQList, con);
            executeMultiple(updates, con);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        if (rsData != null) {
            rsData.close();
        }
        if (ps != null) {
            ps.close();
        }
        if (con != null) {
            con.close();
        }

    }

    public void saveTargetValuesNewForOverAll(Session sess) throws Exception {
        Connection con = ProgenConnection.getInstance().getConnection();
//            ResultSet rsData;
//            PreparedStatement ps;
//            ps=null;
//            rsData=null;

        PbTargetParamTable pTable = (PbTargetParamTable) sess.getObject("prg.targetparam.qdparams.PbTargetParamTable");
        PbTargetValuesParam targetParam = (PbTargetValuesParam) sess.getObject("prg.targetparam.qdparams.PbTargetValuesParam");
        String targetId = targetParam.getTargetId();
        String periodType = targetParam.getPeriodType();
        String minTimeLevel = targetParam.getMinimumTimeLevel();
        HashMap nonAllIds = targetParam.getNonAllIds();
        String viewBy = targetParam.getViewByParameter();
        ArrayList deleteQueries = new ArrayList();
//            HashMap ratioComb = targetParam.getRatioComb();
//            ArrayList updates = new ArrayList();
        String viewByElementId = "";
        String viewByColName = "";
        String nonAllDataClause = "";

        Object relSeqObj[] = new Object[1];
        relSeqObj[0] = "PRG_TARGET_DATA_SEQ";
        String factQuery = resBundle.getString("getSequenceNumber");
//           ArrayList delList = new ArrayList();
        String finalfactQuery = buildQuery(factQuery, relSeqObj);

        ArrayList insertQList = new ArrayList();
        Object tarOb[] = new Object[1];
        tarOb[0] = targetId;
        String getTargetTable = resBundle.getString("getTargetTable");
        String fingetTargetTable = buildQuery(getTargetTable, tarOb);
        PbReturnObject targetTabOb = execSelectSQL(fingetTargetTable);
        String targetTable = targetTabOb.getFieldValueString(0, "BUSS_TABLE_NAME");
        String measureName2 = targetTabOb.getFieldValueString(0, "MEASURE_NAME");
        String measureName = measureName2.trim().replace(" ", "_");

        String getElementIdsBussCols = resBundle.getString("getElementIdsBussCols");
        String fingetElementIdsBussCols = buildQuery(getElementIdsBussCols, tarOb);
        PbReturnObject bussColOb = execSelectSQL(fingetElementIdsBussCols);
        HashMap elementIds = new HashMap();
        String viewByName = "";
        bussColOb.writeString();
        for (int p = 0; p < bussColOb.getRowCount(); p++) {
            elementIds.put(bussColOb.getFieldValueString(p, "ELEMENT_ID"), bussColOb.getFieldValueString(p, "BUSS_COL_NAME"));
            //if(viewBy.equalsIgnoreCase(bussColOb.getFieldValueString(p,"PARAM_DISP_NAME"))){
            if (viewBy.equalsIgnoreCase(bussColOb.getFieldValueString(p, "ELEMENT_ID"))) {
                viewByElementId = bussColOb.getFieldValueString(p, "ELEMENT_ID");
                viewByColName = bussColOb.getFieldValueString(p, "BUSS_COL_NAME");
                viewByName = bussColOb.getFieldValueString(p, "PARAM_DISP_NAME");
            }
        }

        String nonAll[] = (String[]) (nonAllIds.keySet()).toArray(new String[0]);
        HashMap nonAllNew = new HashMap();
        for (int y = 0; y < nonAll.length; y++) {
            String eleId = nonAll[y];
            String value = nonAllIds.get(eleId).toString();
            if (!value.equalsIgnoreCase("-1") && !value.equalsIgnoreCase("All")) {
                nonAllNew.put(eleId, value);
            }
        }
        String nonAllStr[] = (String[]) (nonAllNew.keySet()).toArray(new String[0]);
        for (int y = 0; y < nonAllStr.length; y++) {
            String eleId = nonAllStr[y];
            String colName = "";
            if (elementIds.containsKey(eleId)) {
                elementIds.get(eleId).toString();
                String value = nonAllIds.get(eleId).toString();
                if (y == 0) {
                    nonAllDataClause = " and " + colName + "='" + value + "'";
                } else if (y == 1) {
                    nonAllDataClause = nonAllDataClause + " and " + colName + "='" + value + "'";
                } else {
                    nonAllDataClause = nonAllDataClause + " and " + colName + "='" + value + "'";
                }
            }
        }
        int obSize = 0;

        // to store all the param columns in ordered manner
        ArrayList allParamsValues = new ArrayList();
        allParamsValues.add("TARGET_ID");
        allParamsValues.add("T_DATE");
        allParamsValues.add("T_MONTH");
        allParamsValues.add("T_QTR");
        allParamsValues.add("T_YEAR");

        String insertQ = "";
        obSize = bussColOb.getRowCount();
        int totalCount = obSize + 8;

        for (int i = 0; i < bussColOb.getRowCount(); i++) {
            allParamsValues.add(bussColOb.getFieldValueString(i, 2).trim());
            if (i == 0 && (bussColOb.getRowCount() > 1)) {
                insertQ = insertQ + bussColOb.getFieldValueString(i, 2).trim() + ",";
            } else if ((i == 0) && (bussColOb.getRowCount() == 1)) {
                insertQ = insertQ + bussColOb.getFieldValueString(i, 2).trim();
            } else if (i == (bussColOb.getRowCount() - 1)) {
                insertQ = insertQ + bussColOb.getFieldValueString(i, 2).trim();
            } else {
                insertQ = insertQ + bussColOb.getFieldValueString(i, 2).trim() + ",";
            }

        }

        allParamsValues.add(measureName.trim().toUpperCase());
        allParamsValues.add("VIEWBY");
        allParamsValues.add("FACT_ID");

        String replaceVals = "";
        for (int y = 0; y < totalCount; y++) {
            if (y == 0 && (totalCount > 1)) {
                replaceVals = replaceVals + "'&'" + ",";
            } else if ((y == 0) && (totalCount == 1)) {
                replaceVals = replaceVals + "'&'";
            } else if (y == totalCount - 1) {
                replaceVals = replaceVals + "'&'";
            } else {
                replaceVals = replaceVals + "'&'" + ",";
            }

        }
        // to get the dimensions.Its members  and all info
        String getTargetParamsInfo = resBundle.getString("getTargetParamsInfo");
        String fingetTargetParamsInfo = buildQuery(getTargetParamsInfo, tarOb);
        PbReturnObject paramInfoOb = execSelectSQL(fingetTargetParamsInfo);
//          HashMap dimInfo = new HashMap();
        HashMap memBersInfo = new HashMap();
//         HashMap allElements = new HashMap();

        for (int p = 0; p < paramInfoOb.getRowCount(); p++) {
//             String dimId = paramInfoOb.getFieldValueString(p,"DIM_ID");
            String bussColName = "";
            if (elementIds.containsKey(paramInfoOb.getFieldValueString(p, "ELEMENT_ID"))) {
                bussColName = elementIds.get(paramInfoOb.getFieldValueString(p, "ELEMENT_ID")).toString();
            }
            ArrayList al = new ArrayList();
            al.add(0, paramInfoOb.getFieldValueString(p, "PARAM_DISP_NAME")); //name
            al.add(1, bussColName); //buss COl Name
            al.add(2, paramInfoOb.getFieldValueString(p, "CHILD_ELEMENT_ID")); // child id
            al.add(3, paramInfoOb.getFieldValueString(p, "REL_LEVEL")); //level
            memBersInfo.put(paramInfoOb.getFieldValueString(p, "ELEMENT_ID"), al);
        }
        //  String updateQuery = "update "+targetTable.trim()+" set "+measureName.trim().toUpperCase()+"=&"+where ;
        String updateQuery = "";// "update & set &=& where target_id=& "+nonAllDataClause+" and viewby=&";
        //Starting default parameters
        String initialParams = "insert into " + targetTable.trim() + "(TARGET_ID,T_DATE,T_MONTH,T_QTR,T_YEAR," + insertQ;
        String finalParameters = initialParams + "," + measureName.trim() + "," + " VIEWBY,FACT_ID) values(" + replaceVals + ")";
        for (int i = 0; i < pTable.getRowCount(); i++) {
            PbTargetValuesParam targetParam2 = (PbTargetValuesParam) pTable.getRow(i);
            long measureVal = targetParam2.getMeasureVal();
            String updateFlag = targetParam2.getUpdateFlag();
            String dat = targetParam2.getDateVal();
            String paramVal = targetParam2.getPrimViewByValue();
            HashMap allValsMap = new HashMap();
            String key = paramVal + ":" + dat;
            allValsMap.put("TARGET_ID", targetId);
            if (updateFlag.equalsIgnoreCase("N")) {
                allValsMap.put(viewByColName.trim(), paramVal);
                String tQuery = "";
                String t_year = "";//dat.substring(7);
                String t_Qtr = "";
                String t_mon = "";
                String datVal = "";
                if (periodType.equalsIgnoreCase("Day")) {
                    tQuery = "SELECT DISTINCT CQTR,CYEAR,to_char(ddate,'MON-yy') CMON ,to_char(ddate,'dd-MON-yy') dd  FROM pr_day_denom  WHERE to_char(DDATE,'dd-mm-yy')= '" + dat + "'";
                    PbReturnObject timeOb = execSelectSQL(tQuery);
                    // t_year = timeOb.getFieldValueString(0,"CYEAR");
                    // t_Qtr = timeOb.getFieldValueString(0,"CQTR")+"-"+timeOb.getFieldValueString(0,"CYEAR");
                    // t_mon = timeOb.getFieldValueString(0,"CMON");
                    datVal = timeOb.getFieldValueString(0, "DD");
                } else if (periodType.equalsIgnoreCase("Month")) {
                    tQuery = "select distinct  CQTR,CYEAR,to_char(CM_ST_DATE,'MON-yy') CMON from pr_day_denom where to_date(CM_ST_DATE,'dd-mon-yy')=to_date('" + dat + "','mm-yy')";
                    PbReturnObject timeOb = execSelectSQL(tQuery);
                    // t_year = timeOb.getFieldValueString(0,"CYEAR");
                    // t_Qtr = timeOb.getFieldValueString(0,"CQTR")+"-"+timeOb.getFieldValueString(0,"CYEAR");
                    t_mon = timeOb.getFieldValueString(0, "CMON");
                } else if (periodType.equalsIgnoreCase("Quarter") || periodType.equalsIgnoreCase("Qtr")) {
                    tQuery = "select distinct CQTR,CYEAR,CMONTH,to_date(CM_ST_DATE,'dd-mon-yy') DD from pr_day_denom where to_date(CM_ST_DATE,'dd-mon-yy')=to_date('" + dat + "','dd-mon-yy')";
                    t_Qtr = dat;
                    // t_year = dat.substring(2);
                } else if (periodType.equalsIgnoreCase("Year")) {
                    tQuery = "select distinct CQTR,CYEAR,CMONTH,to_date(CM_ST_DATE,'dd-mon-yy') DD from pr_day_denom where to_date(CM_ST_DATE,'dd-mon-yy')=to_date('" + dat + "','dd-mon-yy')";
                    t_year = dat;
                }
                if (viewBy.equalsIgnoreCase("0")) {
                    viewByName = "OverAll";
                }
                int fact_id = getSequenceNumber(finalfactQuery);

//                allValsMap.put(measureName.trim().toUpperCase(), "" + measureVal + "");
                allValsMap.put(measureName.trim().toUpperCase(), String.valueOf(measureVal));
//                allValsMap.put("TARGET_ID", "" + targetId + "");
                allValsMap.put("TARGET_ID", String.valueOf(targetId));
                allValsMap.put("T_DATE", datVal);
                allValsMap.put("T_MONTH", t_mon);
                allValsMap.put("T_QTR", t_Qtr);
                allValsMap.put("T_YEAR", t_year);
//                allValsMap.put("FACT_ID", "" + fact_id + "");
                allValsMap.put("FACT_ID", String.valueOf(fact_id));
                allValsMap.put("VIEWBY", viewByName.trim());

                Object insertObjDy[] = new Object[allParamsValues.size()];
                for (int t = 0; t < allParamsValues.size(); t++) {
                    String colNames = (String) allParamsValues.get(t);
                    if (allValsMap.containsKey(colNames)) {
                        if ((String) allValsMap.get(colNames) == null) {
                            insertObjDy[t] = "All";
                        } else if (allValsMap.get(colNames) != null) {
                            insertObjDy[t] = (String) allValsMap.get(colNames);
                        } else {
                            insertObjDy[t] = "";
                        }
                    } else {
                        insertObjDy[t] = "-1";
                    }
                }
                String finalInsertDyn = buildQuery(finalParameters, insertObjDy);
                insertQList.add(finalInsertDyn);
            } else if (updateFlag.equalsIgnoreCase("Y")) {
//                String childDataQuery = "";
                String childElementId = "";
                String childEleName = "";
                ArrayList childDet = new ArrayList();
                String currentName = "";
//                String parentName = "";
                if (memBersInfo.containsKey(viewByElementId)) {
                    ArrayList alDet = (ArrayList) memBersInfo.get(viewByElementId);
                    childElementId = alDet.get(2).toString();
                    currentName = alDet.get(0).toString();

                    if (memBersInfo.containsKey(childElementId)) {
                        childEleName = alDet.get(2).toString();
                        childDet = (ArrayList) memBersInfo.get(childElementId);
                        childEleName = childDet.get(0).toString();
                    }
                }

                HashMap updateVals = new HashMap();
                //"update "+targetTable.trim()+" set "+measureName.trim().toUpperCase()+"=& where target_id="+targetId+" "+nonAllDataClause;
                if (periodType.equalsIgnoreCase("Month")) {
                    if (minTimeLevel.equalsIgnoreCase("Month")) {
                        updateQuery = "update & set &=& where target_id='&'  and viewby='OverAll' and t_month='&'";
                    } // updateQuery="update & set &=& where target_id='&' "+nonAllDataClause+" and viewby='&' and t_month='&' and "+elementIds.get(viewByElementId.trim()).toString()+"='"+paramVal+"'";
                    else if (minTimeLevel.equalsIgnoreCase("Day")) {
                        updateQuery = "update & set &=& where target_id='&' " + nonAllDataClause + " and viewby='&' and t_date=to_date('&','dd-mm-yy') and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                    }

                } else if (periodType.equalsIgnoreCase("Day")) {
                    updateQuery = "update & set &=& where target_id='&'  and viewby='OverAll' and t_date=to_char('&','dd-mm-yy')";

                    // updateQuery="update & set &=& where target_id='&' "+nonAllDataClause+" and viewby='&' and t_date=to_date('&','dd-mm-yy') and "+elementIds.get(viewByElementId.trim()).toString()+"='"+paramVal+"'";
                } else if (periodType.equalsIgnoreCase("Quarter") || periodType.equalsIgnoreCase("Qtr")) {
                    if (minTimeLevel.equalsIgnoreCase("Quarter") || minTimeLevel.equalsIgnoreCase("Qtr")) {
                        updateQuery = "update & set &=& where target_id='&'  and viewby='OverAll' and t_qtr='&'";
                    } else if (minTimeLevel.equalsIgnoreCase("Month")) {
                        updateQuery = "update & set &=& where target_id='&' " + nonAllDataClause + " and viewby='&' and t_month='&' and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                    } else if (minTimeLevel.equalsIgnoreCase("Day")) {
                        updateQuery = "update & set &=& where target_id='&' " + nonAllDataClause + " and viewby='&' and t_date=to_date('&','dd-mm-yy') and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                    }

                } else if (periodType.equalsIgnoreCase("Year")) {
                    if (minTimeLevel.equalsIgnoreCase("Year")) {
                        updateQuery = "update & set &=& where target_id='&'  and viewby='OverAll' and t_year='&'";
                    } else if (minTimeLevel.equalsIgnoreCase("Quarter")) {
                        updateQuery = "update & set &=& where target_id='&' " + nonAllDataClause + " and viewby='&' and t_qtr='&' and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                    } else if (minTimeLevel.equalsIgnoreCase("Month")) {
                        updateQuery = "update & set &=& where target_id='&' " + nonAllDataClause + " and viewby='&' and t_month='&' and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                    } else if (minTimeLevel.equalsIgnoreCase("Day")) {
                        updateQuery = "update & set &=& where target_id='&' " + nonAllDataClause + " and viewby='&' and t_date=to_date('&','dd-mm-yy') and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                    }

                }
                //memBersInfo
//                       ArrayList alreadyUpdateFact = new ArrayList();

                Object update[] = new Object[5];
                update[0] = targetTable.trim();
                update[1] = measureName.trim().toUpperCase();
                update[2] = new Long(measureVal);
                update[3] = targetId;
                update[4] = dat;
                String finupdateQuery = buildQuery(updateQuery, update);
                // finupdateQuery = finupdateQuery;//+nonAllDataClause;
                updateVals.put(paramVal, new Long(measureVal));
                insertQList.add(finupdateQuery);

            } else if (updateFlag.equalsIgnoreCase("D")) {
                String deleteQuery = "";
                if (periodType.equalsIgnoreCase("Month")) {
                    if (minTimeLevel.equalsIgnoreCase("Month")) {
                        deleteQuery = "delete from " + targetTable.trim() + " where target_id=" + targetId + " and viewby='OverAll' and t_month='" + dat + "'";
                    }
                    if (minTimeLevel.equalsIgnoreCase("Day")) {
                        deleteQuery = "delete from " + targetTable.trim() + " where target_id=" + targetId + " and viewby='&' and t_date=to_date('&','dd-mm-yy') " + nonAllDataClause + " and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                    }

                } else if (periodType.equalsIgnoreCase("Day")) {
                    deleteQuery = "delete from " + targetTable.trim() + " where target_id=" + targetId + " and viewby='OverAll' and t_date=to_date('" + dat + "','dd-mm-yy')";
                }
                // deleteQuery = "delete from "+targetTable.trim()+" where target_id="+targetId+" and viewby='OverAll' and t_year='"+dat+"'";

                if (periodType.equalsIgnoreCase("Quarter")) {
                    if (minTimeLevel.equalsIgnoreCase("Quarter")) {
                        deleteQuery = "delete from " + targetTable.trim() + " where target_id=" + targetId + " and viewby='OverAll' and t_qtr='" + dat + "'";
                    }
                    if (minTimeLevel.equalsIgnoreCase("Month")) {
                        deleteQuery = "delete from " + targetTable.trim() + " where target_id=" + targetId + " and viewby='&' and t_month='&' " + nonAllDataClause + " and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                    }
                    if (minTimeLevel.equalsIgnoreCase("Day")) {
                        deleteQuery = "delete from " + targetTable.trim() + " where target_id=" + targetId + " and viewby='&' and t_date=to_date('&','dd-mm-yy') " + nonAllDataClause + " and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                    }

                }
                if (periodType.equalsIgnoreCase("Year")) {
                    if (minTimeLevel.equalsIgnoreCase("Year")) {
                        deleteQuery = "delete from " + targetTable.trim() + " where target_id=" + targetId + " and viewby='OverAll' and t_year='" + dat + "'";
                    }
                    if (minTimeLevel.equalsIgnoreCase("Quarter")) {
                        deleteQuery = "delete from " + targetTable.trim() + " where target_id=" + targetId + " and viewby='&' and t_qtr='&' " + nonAllDataClause + " and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                    }
                    if (minTimeLevel.equalsIgnoreCase("Month")) {
                        deleteQuery = "delete from " + targetTable.trim() + " where target_id=" + targetId + " and viewby='&' and t_month='&' " + nonAllDataClause + " and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                    }
                    if (minTimeLevel.equalsIgnoreCase("Day")) {
                        deleteQuery = "delete from " + targetTable.trim() + " where target_id=" + targetId + " and viewby='&' and t_date=to_date('&','dd-mm-yy') " + nonAllDataClause + " and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                    }

                }

                deleteQueries.add(deleteQuery);

            }

        }

        try {
            executeMultiple(insertQList, con);
            executeMultiple(deleteQueries, con);
        } catch (Exception d) {
            logger.error("Exception:", d);
        }

    }

    public void saveTargetValuesNew(Session sess) throws Exception {
        Connection con = ProgenConnection.getInstance().getConnection();
        ResultSet rsData;
        PreparedStatement ps;
        ps = null;
        rsData = null;

        PbTargetParamTable pTable = (PbTargetParamTable) sess.getObject("prg.targetparam.qdparams.PbTargetParamTable");
        PbTargetValuesParam targetParam = (PbTargetValuesParam) sess.getObject("prg.targetparam.qdparams.PbTargetValuesParam");
        String targetId = targetParam.getTargetId();
        String periodType = targetParam.getPeriodType();
        String minTimeLevel = targetParam.getMinimumTimeLevel();
        HashMap nonAllIds = targetParam.getNonAllIds();
        String viewBy = targetParam.getViewByParameter();
        ArrayList deleteQueries = new ArrayList();
        HashMap ratioComb = targetParam.getRatioComb();
        ArrayList updates = new ArrayList();
        String viewByElementId = "";
        String viewByColName = "";
        String nonAllDataClause = "";

        Object relSeqObj[] = new Object[1];
        relSeqObj[0] = "PRG_TARGET_DATA_SEQ";
        String factQuery = resBundle.getString("getSequenceNumber");
        ArrayList delList = new ArrayList();
        String finalfactQuery = buildQuery(factQuery, relSeqObj);

        ArrayList insertQList = new ArrayList();
        Object tarOb[] = new Object[1];
        tarOb[0] = targetId;
        String getTargetTable = resBundle.getString("getTargetTable");
        String fingetTargetTable = buildQuery(getTargetTable, tarOb);
        PbReturnObject targetTabOb = execSelectSQL(fingetTargetTable);
        String targetTable = targetTabOb.getFieldValueString(0, "BUSS_TABLE_NAME");
        String measureName2 = targetTabOb.getFieldValueString(0, "MEASURE_NAME");
        String measureName = measureName2.trim().replace(" ", "_");
        String getElementIdsBussCols = resBundle.getString("getElementIdsBussCols");
        String fingetElementIdsBussCols = buildQuery(getElementIdsBussCols, tarOb);
        PbReturnObject bussColOb = execSelectSQL(fingetElementIdsBussCols);
        HashMap elementIds = new HashMap();
        String viewByName = "";
        bussColOb.writeString();
        for (int p = 0; p < bussColOb.getRowCount(); p++) {
            elementIds.put(bussColOb.getFieldValueString(p, "ELEMENT_ID"), bussColOb.getFieldValueString(p, "BUSS_COL_NAME"));
            //if(viewBy.equalsIgnoreCase(bussColOb.getFieldValueString(p,"PARAM_DISP_NAME"))){
            if (viewBy.equalsIgnoreCase(bussColOb.getFieldValueString(p, "ELEMENT_ID"))) {
                viewByElementId = bussColOb.getFieldValueString(p, "ELEMENT_ID");
                viewByColName = bussColOb.getFieldValueString(p, "BUSS_COL_NAME");
                viewByName = bussColOb.getFieldValueString(p, "PARAM_DISP_NAME");
            }
        }

        String nonAll[] = (String[]) (nonAllIds.keySet()).toArray(new String[0]);
        HashMap allValsMap = new HashMap();
        HashMap nonAllNew = new HashMap();
        for (int y = 0; y < nonAll.length; y++) {
            String eleId = nonAll[y];
            String value = nonAllIds.get(eleId).toString();
            if (!value.equalsIgnoreCase("-1") && !value.equalsIgnoreCase("All")) {
                nonAllNew.put(eleId, value);
                if (elementIds.containsKey(eleId)) {
                    String colName = (String) elementIds.get(eleId);
                    allValsMap.put(colName, value);
                }
            }
        }
        String nonAllStr[] = (String[]) (nonAllNew.keySet()).toArray(new String[0]);
        for (int y = 0; y < nonAllStr.length; y++) {
            String eleId = nonAllStr[y];
            String colName = elementIds.get(eleId).toString();
            String value = nonAllIds.get(eleId).toString();
            if (y == 0) {
                nonAllDataClause = " and " + colName + "='" + value + "'";
            } else if (y == 1) {
                nonAllDataClause = nonAllDataClause + " and " + colName + "='" + value + "'";
            } else {
                nonAllDataClause = nonAllDataClause + " and " + colName + "='" + value + "'";
            }
        }
        int obSize = 0;

        // to store all the param columns in ordered manner
        ArrayList allParamsValues = new ArrayList();
        allParamsValues.add("TARGET_ID");
        allParamsValues.add("T_DATE");
        allParamsValues.add("T_MONTH");
        allParamsValues.add("T_QTR");
        allParamsValues.add("T_YEAR");

        String insertQ = "";
        obSize = bussColOb.getRowCount();
        int totalCount = obSize + 8;

        for (int i = 0; i < bussColOb.getRowCount(); i++) {
            allParamsValues.add(bussColOb.getFieldValueString(i, 2).trim());
            if (i == 0 && (bussColOb.getRowCount() > 1)) {
                insertQ = insertQ + bussColOb.getFieldValueString(i, 2).trim() + ",";
            } else if ((i == 0) && (bussColOb.getRowCount() == 1)) {
                insertQ = insertQ + bussColOb.getFieldValueString(i, 2).trim();
            } else if (i == (bussColOb.getRowCount() - 1)) {
                insertQ = insertQ + bussColOb.getFieldValueString(i, 2).trim();
            } else {
                insertQ = insertQ + bussColOb.getFieldValueString(i, 2).trim() + ",";
            }

        }
        allParamsValues.add(measureName.trim().toUpperCase());
        allParamsValues.add("VIEWBY");
        allParamsValues.add("FACT_ID");

        String replaceVals = "";
        for (int y = 0; y < totalCount; y++) {
            if (y == 0 && (totalCount > 1)) {
                replaceVals = replaceVals + "'&'" + ",";
            } else if ((y == 0) && (totalCount == 1)) {
                replaceVals = replaceVals + "'&'";
            } else if (y == totalCount - 1) {
                replaceVals = replaceVals + "'&'";
            } else {
                replaceVals = replaceVals + "'&'" + ",";
            }

        }
        // to get the dimensions.Its members  and all info
        String getTargetParamsInfo = resBundle.getString("getTargetParamsInfo");
        String fingetTargetParamsInfo = buildQuery(getTargetParamsInfo, tarOb);
        PbReturnObject paramInfoOb = execSelectSQL(fingetTargetParamsInfo);
        //  ////////////////////////////////////////////////////////////////////////////////////////////////////////.println(" fingetTargetParamsInfo "+fingetTargetParamsInfo);
        HashMap dimInfo = new HashMap();
        HashMap memBersInfo = new HashMap();
        HashMap allElements = new HashMap();

        for (int p = 0; p < paramInfoOb.getRowCount(); p++) {
            String dimId = paramInfoOb.getFieldValueString(p, "DIM_ID");
            String bussColName = "";
            if (elementIds.containsKey(paramInfoOb.getFieldValueString(p, "ELEMENT_ID"))) {
                bussColName = elementIds.get(paramInfoOb.getFieldValueString(p, "ELEMENT_ID")).toString();
            }
            ArrayList al = new ArrayList();
            al.add(0, paramInfoOb.getFieldValueString(p, "PARAM_DISP_NAME")); //name
            al.add(1, bussColName); //buss COl Name
            al.add(2, paramInfoOb.getFieldValueString(p, "CHILD_ELEMENT_ID")); // child id
            al.add(3, paramInfoOb.getFieldValueString(p, "REL_LEVEL")); //level
            memBersInfo.put(paramInfoOb.getFieldValueString(p, "ELEMENT_ID"), al);
        }
        //  String updateQuery = "update "+targetTable.trim()+" set "+measureName.trim().toUpperCase()+"=&"+where ;
        String updateQuery = "";// "update & set &=& where target_id=& "+nonAllDataClause+" and viewby=&";
        //Starting default parameters
        String initialParams = "insert into " + targetTable.trim() + "(TARGET_ID,T_DATE,T_MONTH,T_QTR,T_YEAR," + insertQ;
        String finalParameters = initialParams + "," + measureName.trim() + "," + " VIEWBY,FACT_ID) values(" + replaceVals + ")";
        for (int i = 0; i < pTable.getRowCount(); i++) {
            PbTargetValuesParam targetParam2 = (PbTargetValuesParam) pTable.getRow(i);
            long measureVal = targetParam2.getMeasureVal();
            String updateFlag = targetParam2.getUpdateFlag();
            String dat = targetParam2.getDateVal();
            String paramVal = targetParam2.getPrimViewByValue();
            String key = paramVal + ":" + dat;
            float ratioN = 0;
            long oldValue = 0;
            if (ratioComb.containsKey(key)) {
                oldValue = ((Long) ratioComb.get(key)).longValue();
                float divisionVal = (float) (measureVal - oldValue) / oldValue;
                ratioN = (float) 1 + divisionVal;
            }

            allValsMap.put("TARGET_ID", targetId);
            if (updateFlag.equalsIgnoreCase("N")) {
                allValsMap.put(viewByColName.trim(), paramVal);
                String tQuery = "";
                String t_year = "";//dat.substring(7);
                String t_Qtr = "";
                String t_mon = "";
                String datVal = "";
                if (periodType.equalsIgnoreCase("Day")) {
                    tQuery = "SELECT DISTINCT CQTR,CYEAR,to_char(ddate,'MON-yy') CMON ,to_char(ddate,'dd-MON-yy') dd  FROM pr_day_denom  WHERE to_char(DDATE,'dd-mm-yy')= '" + dat + "'";
                    PbReturnObject timeOb = execSelectSQL(tQuery);
                    // t_year = timeOb.getFieldValueString(0,"CYEAR");
                    // t_Qtr = timeOb.getFieldValueString(0,"CQTR")+"-"+timeOb.getFieldValueString(0,"CYEAR");
                    // t_mon = timeOb.getFieldValueString(0,"CMON");
                    datVal = timeOb.getFieldValueString(0, "DD");
                } else if (periodType.equalsIgnoreCase("Month")) {
                    tQuery = "select distinct  CQTR,CYEAR,to_char(CM_ST_DATE,'MON-yy') CMON from pr_day_denom where to_date(CM_ST_DATE,'dd-mon-yy')=to_date('" + dat + "','mm-yy')";
                    PbReturnObject timeOb = execSelectSQL(tQuery);
                    // t_year = timeOb.getFieldValueString(0,"CYEAR");
                    // t_Qtr = timeOb.getFieldValueString(0,"CQTR")+"-"+timeOb.getFieldValueString(0,"CYEAR");
                    t_mon = timeOb.getFieldValueString(0, "CMON");
                } else if (periodType.equalsIgnoreCase("Quarter") || periodType.equalsIgnoreCase("Qtr")) {
                    tQuery = "select distinct CQTR,CYEAR,CMONTH,to_date(CM_ST_DATE,'dd-mon-yy') DD from pr_day_denom where to_date(CM_ST_DATE,'dd-mon-yy')=to_date('" + dat + "','dd-mon-yy')";
                    t_Qtr = dat;
                    // t_year = dat.substring(2);
                } else if (periodType.equalsIgnoreCase("Year")) {
                    tQuery = "select distinct CQTR,CYEAR,CMONTH,to_date(CM_ST_DATE,'dd-mon-yy') DD from pr_day_denom where to_date(CM_ST_DATE,'dd-mon-yy')=to_date('" + dat + "','dd-mon-yy')";
                    t_year = dat;
                }
                if (viewBy.equalsIgnoreCase("0")) {
                    viewByName = "OverAll";
                }
                int fact_id = getSequenceNumber(finalfactQuery);

//                allValsMap.put(measureName.trim().toUpperCase(), "" + measureVal + "");
                allValsMap.put(measureName.trim().toUpperCase(), String.valueOf(measureVal));
                allValsMap.put("TARGET_ID", String.valueOf(targetId));
//                allValsMap.put("TARGET_ID", "" + targetId + "");
                allValsMap.put("T_DATE", datVal);
                allValsMap.put("T_MONTH", t_mon);
                allValsMap.put("T_QTR", t_Qtr);
                allValsMap.put("T_YEAR", t_year);
//                allValsMap.put("FACT_ID", "" + fact_id + "");
                allValsMap.put("FACT_ID", String.valueOf(fact_id));
                allValsMap.put("VIEWBY", viewByName.trim());

                Object insertObjDy[] = new Object[allParamsValues.size()];
                for (int t = 0; t < allParamsValues.size(); t++) {
                    String colNames = (String) allParamsValues.get(t);
                    if (allValsMap.containsKey(colNames)) {
                        if ((String) allValsMap.get(colNames) == null) {
                            insertObjDy[t] = "All";
                        } else if (allValsMap.get(colNames) != null) {
                            insertObjDy[t] = (String) allValsMap.get(colNames);
                        } else {
                            insertObjDy[t] = "";
                        }
                    } else {
                        insertObjDy[t] = "-1";
                    }
                }
                String finalInsertDyn = buildQuery(finalParameters, insertObjDy);
                insertQList.add(finalInsertDyn);
            } else if (updateFlag.equalsIgnoreCase("Y")) {
                String childDataQuery = "";
                String childElementId = "";
                String childEleName = "";
                ArrayList childDet = new ArrayList();
                String currentName = "";

                String parentName = "";
                if (memBersInfo.containsKey(viewByElementId)) {
                    ArrayList alDet = (ArrayList) memBersInfo.get(viewByElementId);
                    childElementId = alDet.get(2).toString();
                    currentName = alDet.get(0).toString();

                    if (memBersInfo.containsKey(childElementId)) {
                        childEleName = alDet.get(2).toString();
                        childDet = (ArrayList) memBersInfo.get(childElementId);
                        childEleName = childDet.get(0).toString();
                    }
                }

                HashMap updateVals = new HashMap();
                //"update "+targetTable.trim()+" set "+measureName.trim().toUpperCase()+"=& where target_id="+targetId+" "+nonAllDataClause;
                if (periodType.equalsIgnoreCase("Month")) {
                    if (minTimeLevel.equalsIgnoreCase("Month")) {
                        updateQuery = "update & set &=& where target_id='&' " + nonAllDataClause + " and viewby='&' and t_month='&' and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                    } else if (minTimeLevel.equalsIgnoreCase("Day")) {
                        updateQuery = "update & set &=& where target_id='&' " + nonAllDataClause + " and viewby='&' and t_date=to_date('&','dd-mm-yy') and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                    }

                } else if (periodType.equalsIgnoreCase("Day")) {
                    updateQuery = "update & set &=& where target_id='&' " + nonAllDataClause + " and viewby='&' and t_date=to_date('&','dd-mm-yy') and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                } else if (periodType.equalsIgnoreCase("Quarter") || periodType.equalsIgnoreCase("Qtr")) {
                    if (minTimeLevel.equalsIgnoreCase("Quarter") || minTimeLevel.equalsIgnoreCase("Qtr")) {
                        updateQuery = "update & set &=& where target_id='&' " + nonAllDataClause + " and viewby='&' and t_qtr='&' and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                    } else if (minTimeLevel.equalsIgnoreCase("Month")) {
                        updateQuery = "update & set &=& where target_id='&' " + nonAllDataClause + " and viewby='&' and t_month='&' and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                    } else if (minTimeLevel.equalsIgnoreCase("Day")) {
                        updateQuery = "update & set &=& where target_id='&' " + nonAllDataClause + " and viewby='&' and t_date=to_date('&','dd-mm-yy') and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                    }

                } else if (periodType.equalsIgnoreCase("Year")) {
                    if (minTimeLevel.equalsIgnoreCase("Year")) {
                        updateQuery = "update & set &=& where target_id='&' " + nonAllDataClause + " and viewby='&' and t_year='&' and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                    } else if (minTimeLevel.equalsIgnoreCase("Quarter")) {
                        updateQuery = "update & set &=& where target_id='&' " + nonAllDataClause + " and viewby='&' and t_qtr='&' and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                    } else if (minTimeLevel.equalsIgnoreCase("Month")) {
                        updateQuery = "update & set &=& where target_id='&' " + nonAllDataClause + " and viewby='&' and t_month='&' and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                    } else if (minTimeLevel.equalsIgnoreCase("Day")) {
                        updateQuery = "update & set &=& where target_id='&' " + nonAllDataClause + " and viewby='&' and t_date=to_date('&','dd-mm-yy') and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                    }

                }
                //memBersInfo
                ArrayList alreadyUpdateFact = new ArrayList();
                Object update[] = new Object[6];
                update[0] = targetTable.trim();
                update[1] = measureName.trim().toUpperCase();
                update[2] = new Long(measureVal);
                update[3] = targetId;
                update[4] = viewByName.trim();
                update[5] = dat;
                String finupdateQuery = buildQuery(updateQuery, update);
                // finupdateQuery = finupdateQuery;//+nonAllDataClause;
                updateVals.put(paramVal, new Long(measureVal));
                insertQList.add(finupdateQuery);
                // as long as child id is not -1 loop
                PbReturnObject childDataOb = new PbReturnObject();
                if (!childElementId.equalsIgnoreCase("-1")) {
                    if (periodType.equalsIgnoreCase("Month")) {
                        if (minTimeLevel.equalsIgnoreCase("Month")) {
                            childDataQuery = "select " + elementIds.get(childElementId.trim()).toString() + "," + measureName.trim() + ",fact_id from " + targetTable.trim() + " where target_id=" + targetId + " and t_month='" + dat + "' and viewby='" + childEleName + "' and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                        } else if (minTimeLevel.equalsIgnoreCase("Day")) {
                            childDataQuery = "select " + elementIds.get(childElementId.trim()).toString() + "," + measureName.trim() + ",fact_id from " + targetTable.trim() + " where target_id=" + targetId + " and t_date=to_date('" + dat + "','dd-mm-yy') and viewby='" + childEleName + "' and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                        }
                        // execSelectSQL(childDataQuery);
                    } else if (periodType.equalsIgnoreCase("Day")) {
                        if (minTimeLevel.equalsIgnoreCase("Day")) {
                            childDataQuery = "select " + elementIds.get(childElementId.trim()).toString() + "," + measureName.trim() + ",fact_id from " + targetTable.trim() + " where target_id=" + targetId + " and t_date=to_date('" + dat + "','dd-mm-yy') and viewby='" + childEleName + "' and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                        }
                        // execSelectSQL(childDataQuery);
                    }
                    if (periodType.equalsIgnoreCase("Quarter")) {
                        if (minTimeLevel.equalsIgnoreCase("Quarter")) {
                            childDataQuery = "select " + elementIds.get(childElementId.trim()).toString() + "," + measureName.trim() + ",fact_id from " + targetTable.trim() + " where target_id=" + targetId + " and t_qtr='" + dat + "' and viewby='" + childEleName + "' and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                        } else if (minTimeLevel.equalsIgnoreCase("Month")) {
                            childDataQuery = "select " + elementIds.get(childElementId.trim()).toString() + "," + measureName.trim() + ",fact_id from " + targetTable.trim() + " where target_id=" + targetId + " and t_month='" + dat + "' and viewby='" + childEleName + "' and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                        } else if (minTimeLevel.equalsIgnoreCase("Day")) {
                            childDataQuery = "select " + elementIds.get(childElementId.trim()).toString() + "," + measureName.trim() + ",fact_id from " + targetTable.trim() + " where target_id=" + targetId + " and t_date=to_date('" + dat + "','dd-mm-yy') and viewby='" + childEleName + "' and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                        }
                        //  execSelectSQL(childDataQuery);
                    }
                    if (periodType.equalsIgnoreCase("Year")) {
                        if (minTimeLevel.equalsIgnoreCase("Year")) {
                            childDataQuery = "select " + elementIds.get(childElementId.trim()).toString() + "," + measureName.trim() + ",fact_id from " + targetTable.trim() + " where target_id=" + targetId + " and t_year='" + dat + "' and viewby='" + childEleName + "' and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                        } else if (minTimeLevel.equalsIgnoreCase("Quarter")) {
                            childDataQuery = "select " + elementIds.get(childElementId.trim()).toString() + "," + measureName.trim() + ",fact_id from " + targetTable.trim() + " where target_id=" + targetId + " and t_qtr='" + dat + "' and viewby='" + childEleName + "' and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                        } else if (minTimeLevel.equalsIgnoreCase("Month")) {
                            childDataQuery = "select " + elementIds.get(childElementId.trim()).toString() + "," + measureName.trim() + ",fact_id from " + targetTable.trim() + " where target_id=" + targetId + " and t_month='" + dat + "' and viewby='" + childEleName + "' and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                        } else if (minTimeLevel.equalsIgnoreCase("Day")) {
                            childDataQuery = "select " + elementIds.get(childElementId.trim()).toString() + "," + measureName.trim() + ",fact_id from " + targetTable.trim() + " where target_id=" + targetId + " and t_date=to_date('" + dat + "','dd-mm-yy') and viewby='" + childEleName + "' and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                        }
                        // execSelectSQL(childDataQuery);
                    }
                    ps = null;
                    rsData = null;
                    ps = con.prepareStatement(childDataQuery);
                    rsData = ps.executeQuery();
                    childDataOb = new PbReturnObject(rsData);
                    //childDataOb = execSelectSQL(childDataQuery);
                }

                long totalR = 0;
                long allTot = 0;
                for (int m = 0; m < childDataOb.getRowCount(); m++) {
                    totalR = totalR + childDataOb.getFieldValueInt(m, measureName.trim().toUpperCase());
                }
                //get the change in ratio
                float ratio = 0;
                float divisionVal = 0;
                //  float ratioN=0;

                divisionVal = (float) (measureVal - totalR) / totalR;

                if (ratioN == 0) {
                    ratioN = 1 + divisionVal;
                }

                String temoparyParElement = childElementId;
                boolean skipFlag = false;
                while (skipFlag == false || !(childElementId.equalsIgnoreCase("-1"))) {
                    if (childElementId.equalsIgnoreCase("-1")) {
                        skipFlag = true;
                    }
                    if (periodType.equalsIgnoreCase("Month")) {
                        if (minTimeLevel.equalsIgnoreCase("Month")) {
                            childDataQuery = "select " + measureName.trim() + ",fact_id from " + targetTable.trim() + " where target_id=" + targetId + " and t_month='" + dat + "' and viewby='" + childEleName + "' and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                        } else if (minTimeLevel.equalsIgnoreCase("Day")) {
                            childDataQuery = "select " + measureName.trim() + ",fact_id from " + targetTable.trim() + " where target_id=" + targetId + " and t_date=to_date('" + dat + "','dd-mm-yy') and viewby='" + childEleName + "' and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                        }

                        // childDataOb=execSelectSQL(childDataQuery);
                        ps = null;
                        rsData = null;
                        ps = con.prepareStatement(childDataQuery);
                        rsData = ps.executeQuery();
                        childDataOb = new PbReturnObject(rsData);
                    }
                    if (periodType.equalsIgnoreCase("Day")) {
                        if (minTimeLevel.equalsIgnoreCase("Day")) {
                            childDataQuery = "select " + measureName.trim() + ",fact_id from " + targetTable.trim() + " where target_id=" + targetId + " and t_date=to_date('" + dat + "','dd-mm-yy') and viewby='" + childEleName + "' and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                        }
                        // childDataOb=execSelectSQL(childDataQuery);
                        ps = null;
                        rsData = null;
                        ps = con.prepareStatement(childDataQuery);
                        rsData = ps.executeQuery();
                        childDataOb = new PbReturnObject(rsData);
                    }
                    if (periodType.equalsIgnoreCase("Quarter")) {
                        if (minTimeLevel.equalsIgnoreCase("Month")) {
                            childDataQuery = "select " + measureName.trim() + ",fact_id from " + targetTable.trim() + " where target_id=" + targetId + " and t_month='" + dat + "' and viewby='" + childEleName + "' and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                        } else if (minTimeLevel.equalsIgnoreCase("Day")) {
                            childDataQuery = "select " + measureName.trim() + ",fact_id from " + targetTable.trim() + " where target_id=" + targetId + " and t_date=to_date('" + dat + "','dd-mm-yy') and viewby='" + childEleName + "' and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                        } else if (minTimeLevel.equalsIgnoreCase("Quarter")) {
                            childDataQuery = "select " + measureName.trim() + ",fact_id from " + targetTable.trim() + " where target_id=" + targetId + " and t_qtr='" + dat + "' and viewby='" + childEleName + "' and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                        }

                        // childDataOb=execSelectSQL(childDataQuery);
                        ps = null;
                        rsData = null;
                        ps = con.prepareStatement(childDataQuery);
                        rsData = ps.executeQuery();
                        childDataOb = new PbReturnObject(rsData);

                    }
                    if (periodType.equalsIgnoreCase("Year")) {
                        if (minTimeLevel.equalsIgnoreCase("Year")) {
                            childDataQuery = "select " + measureName.trim() + ",fact_id from " + targetTable.trim() + " where target_id=" + targetId + " and t_year='" + dat + "' and viewby='" + childEleName + "' and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                        } else if (minTimeLevel.equalsIgnoreCase("Month")) {
                            childDataQuery = "select " + measureName.trim() + ",fact_id from " + targetTable.trim() + " where target_id=" + targetId + " and t_month='" + dat + "' and viewby='" + childEleName + "' and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                        } else if (minTimeLevel.equalsIgnoreCase("Day")) {
                            childDataQuery = "select " + measureName.trim() + ",fact_id from " + targetTable.trim() + " where target_id=" + targetId + " and t_date=to_date('" + dat + "','dd-mm-yy') and viewby='" + childEleName + "' and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                        } else if (minTimeLevel.equalsIgnoreCase("Quarter")) {
                            childDataQuery = "select " + measureName.trim() + ",fact_id from " + targetTable.trim() + " where target_id=" + targetId + " and t_qtr='" + dat + "' and viewby='" + childEleName + "' and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                        }

                        // childDataOb=execSelectSQL(childDataQuery);
                        ps = null;
                        rsData = null;
                        ps = con.prepareStatement(childDataQuery);
                        rsData = ps.executeQuery();
                        childDataOb = new PbReturnObject(rsData);

                    }

                    // for updation start
                    int chilCounts = childDataOb.getRowCount();
                    if (chilCounts == 0) {
                        chilCounts = 1;
                    }
                    long individualCount = 1;
                    individualCount = measureVal / chilCounts;
                    for (int m = 0; m < childDataOb.getRowCount(); m++) {
                        long newVal = 0;
                        if (totalR != 0) {
                            ratio = measureVal / totalR;
                            ratio = (float) childDataOb.getFieldValueInt(m, measureName.trim().toUpperCase()) / totalR;
                            newVal = (long) (ratio * measureVal);
                        }
                        //int newM = (int)Math.floor(ratioN * childDataOb.getFieldValueInt(m, measureName.trim().toUpperCase()));//;Math.round(ratioN * chilNos.getFieldValueInt(m, measureName.trim().toUpperCase()));
                        int newM = (int) Math.round(ratioN * childDataOb.getFieldValueInt(m, measureName.trim().toUpperCase()));//;Math.round(ratioN * chilNos.getFieldValueInt(m, measureName.trim().toUpperCase()));

                        allTot = allTot + newM;
                        //////////////////////////////////////////////////////////////////////////////////////////////////.print("  --------- "+childDataOb.getFieldValueInt(m,"FACT_ID")+"....");
                        String fact_id = childDataOb.getFieldValueString(m, "FACT_ID");
                        String updateT = "update " + targetTable.trim() + " set " + measureName.trim() + "=& where fact_id=&";
                        Object updateChilds[] = new Object[2];
                        updateChilds[0] = Integer.valueOf(newM);
                        updateChilds[1] = fact_id;
                        updateVals.put(childDataOb.getFieldValueString(m, 0), new Long(newVal));//individualCount));
                        String finalupdateT = buildQuery(updateT, updateChilds);

                        if (!alreadyUpdateFact.contains(Integer.valueOf(childDataOb.getFieldValueInt(m, "FACT_ID")))) {
                            finalupdateT = buildQuery(updateT, updateChilds);
                            alreadyUpdateFact.add(Integer.valueOf(childDataOb.getFieldValueInt(m, "FACT_ID")));
                            updates.add(finalupdateT);
                        }

                    }
                    //for updation over
                    if (memBersInfo.containsKey(childElementId)) {
                        childDet = (ArrayList) memBersInfo.get(childElementId);
                        childElementId = childDet.get(2).toString();
                        childEleName = childDet.get(0).toString();
                        parentName = currentName;
                        temoparyParElement = childElementId;
                    }
                    // childElementId=temoparyParElement;
                    //currentName=childEleName;
                }

            } else if (updateFlag.equalsIgnoreCase("D")) {
                String deleteQuery = "";
                if (periodType.equalsIgnoreCase("Month")) {
                    if (minTimeLevel.equalsIgnoreCase("Month")) {
                        deleteQuery = "delete from " + targetTable.trim() + " where target_id=" + targetId + " and viewby='&' and t_month='&' " + nonAllDataClause + " and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                    }
                    if (minTimeLevel.equalsIgnoreCase("Day")) {
                        deleteQuery = "delete from " + targetTable.trim() + " where target_id=" + targetId + " and viewby='&' and t_date=to_date('&','dd-mm-yy') " + nonAllDataClause + " and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                    }

                } else if (periodType.equalsIgnoreCase("Day")) {
                    deleteQuery = "delete from " + targetTable.trim() + " where target_id=" + targetId + " and viewby='&' and t_date=to_date('&','dd-mm-yy') " + nonAllDataClause + " and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                }
                if (periodType.equalsIgnoreCase("Quarter")) {
                    if (minTimeLevel.equalsIgnoreCase("Quarter")) {
                        deleteQuery = "delete from " + targetTable.trim() + " where target_id=" + targetId + " and viewby='&' and t_qtr='&' " + nonAllDataClause + " and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                    }
                    if (minTimeLevel.equalsIgnoreCase("Month")) {
                        deleteQuery = "delete from " + targetTable.trim() + " where target_id=" + targetId + " and viewby='&' and t_month='&' " + nonAllDataClause + " and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                    }
                    if (minTimeLevel.equalsIgnoreCase("Day")) {
                        deleteQuery = "delete from " + targetTable.trim() + " where target_id=" + targetId + " and viewby='&' and t_date=to_date('&','dd-mm-yy') " + nonAllDataClause + " and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                    }

                }
                if (periodType.equalsIgnoreCase("Year")) {
                    if (minTimeLevel.equalsIgnoreCase("Year")) {
                        deleteQuery = "delete from " + targetTable.trim() + " where target_id=" + targetId + " and viewby='&' and t_year='&' " + nonAllDataClause + " and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                    }
                    if (minTimeLevel.equalsIgnoreCase("Quarter")) {
                        deleteQuery = "delete from " + targetTable.trim() + " where target_id=" + targetId + " and viewby='&' and t_qtr='&' " + nonAllDataClause + " and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                    }
                    if (minTimeLevel.equalsIgnoreCase("Month")) {
                        deleteQuery = "delete from " + targetTable.trim() + " where target_id=" + targetId + " and viewby='&' and t_month='&' " + nonAllDataClause + " and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                    }
                    if (minTimeLevel.equalsIgnoreCase("Day")) {
                        deleteQuery = "delete from " + targetTable.trim() + " where target_id=" + targetId + " and viewby='&' and t_date=to_date('&','dd-mm-yy') " + nonAllDataClause + " and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                    }

                }
                Object delOb[] = new Object[2];
                delOb[0] = viewByName.trim();
                delOb[1] = dat;
                String finDeleteQ = buildQuery(deleteQuery, delOb);
                deleteQueries.add(finDeleteQ);

                String childDataQuery = "";
                String childElementId = "";
                String childEleName = "";
                ArrayList childDet = new ArrayList();
                String currentName = "";

                String parentName = "";
                if (memBersInfo.containsKey(viewByElementId)) {
                    ArrayList alDet = (ArrayList) memBersInfo.get(viewByElementId);
                    childElementId = alDet.get(2).toString();
                    currentName = alDet.get(0).toString();

                    if (memBersInfo.containsKey(childElementId)) {
                        childEleName = alDet.get(2).toString();
                        childDet = (ArrayList) memBersInfo.get(childElementId);
                        childEleName = childDet.get(0).toString();

                    }
                }
                if (periodType.equalsIgnoreCase("Month")) {
                    childDataQuery = "select " + elementIds.get(childElementId.trim()).toString() + "," + measureName.trim() + ",fact_id from " + targetTable.trim() + " where target_id=" + targetId + " and t_month='" + dat + "' and viewby='" + childEleName + "' and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                }
                PbReturnObject childDataOb = new PbReturnObject();
                boolean skipFlag = false;
                while (!childElementId.equalsIgnoreCase("-1") || skipFlag == false) {
                    if (childElementId.equalsIgnoreCase("-1")) {
                        skipFlag = true;
                    }
                    if (periodType.equalsIgnoreCase("Month")) {
                        if (minTimeLevel.equalsIgnoreCase("Month")) {
                            childDataQuery = "select " + measureName.trim() + ",fact_id from " + targetTable.trim() + " where target_id=" + targetId + " and t_month='" + dat + "' and viewby='" + childEleName + "' and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                        } else if (minTimeLevel.equalsIgnoreCase("Day")) {
                            childDataQuery = "select " + measureName.trim() + ",fact_id from " + targetTable.trim() + " where target_id=" + targetId + " and t_date=to_date('" + dat + "','dd-mm-yy') and viewby='" + childEleName + "' and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                        }

                        // childDataOb=execSelectSQL(childDataQuery);
                        ps = null;
                        rsData = null;
                        ps = con.prepareStatement(childDataQuery);
                        rsData = ps.executeQuery();
                        childDataOb = new PbReturnObject(rsData);

                    } else if (periodType.equalsIgnoreCase("Quarter")) {
                        if (minTimeLevel.equalsIgnoreCase("Month")) {
                            childDataQuery = "select " + measureName.trim() + ",fact_id from " + targetTable.trim() + " where target_id=" + targetId + " and t_month='" + dat + "' and viewby='" + childEleName + "' and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                        } else if (minTimeLevel.equalsIgnoreCase("Day")) {
                            childDataQuery = "select " + measureName.trim() + ",fact_id from " + targetTable.trim() + " where target_id=" + targetId + " and t_date=to_date('" + dat + "','dd-mm-yy') and viewby='" + childEleName + "' and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                        } else if (minTimeLevel.equalsIgnoreCase("Quarter")) {
                            childDataQuery = "select " + measureName.trim() + ",fact_id from " + targetTable.trim() + " where target_id=" + targetId + " and t_qtr='" + dat + "' and viewby='" + childEleName + "' and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                        }

                        // childDataOb=execSelectSQL(childDataQuery);
                        ps = null;
                        rsData = null;
                        ps = con.prepareStatement(childDataQuery);
                        rsData = ps.executeQuery();
                        childDataOb = new PbReturnObject(rsData);

                    } else if (periodType.equalsIgnoreCase("Year")) {
                        if (minTimeLevel.equalsIgnoreCase("Month")) {
                            childDataQuery = "select " + measureName.trim() + ",fact_id from " + targetTable.trim() + " where target_id=" + targetId + " and t_month='" + dat + "' and viewby='" + childEleName + "' and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                        } else if (minTimeLevel.equalsIgnoreCase("Day")) {
                            childDataQuery = "select " + measureName.trim() + ",fact_id from " + targetTable.trim() + " where target_id=" + targetId + " and t_date=to_date('" + dat + "','dd-mm-yy') and viewby='" + childEleName + "' and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                        } else if (minTimeLevel.equalsIgnoreCase("Quarter")) {
                            childDataQuery = "select " + measureName.trim() + ",fact_id from " + targetTable.trim() + " where target_id=" + targetId + " and t_qtr='" + dat + "' and viewby='" + childEleName + "' and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                        } else if (minTimeLevel.equalsIgnoreCase("Year")) {
                            childDataQuery = "select " + measureName.trim() + ",fact_id from " + targetTable.trim() + " where target_id=" + targetId + " and t_year='" + dat + "' and viewby='" + childEleName + "' and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                        }
                        // childDataOb=execSelectSQL(childDataQuery);
                        ps = null;
                        rsData = null;
                        ps = con.prepareStatement(childDataQuery);
                        rsData = ps.executeQuery();
                        childDataOb = new PbReturnObject(rsData);

                    } else if (periodType.equalsIgnoreCase("Day")) {
                        if (minTimeLevel.equalsIgnoreCase("Day")) {
                            childDataQuery = "select " + measureName.trim() + ",fact_id from " + targetTable.trim() + " where target_id=" + targetId + " and t_date=to_date('" + dat + "','dd-mm-yy') and viewby='" + childEleName + "' and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                            // childDataOb=execSelectSQL(childDataQuery);
                            ps = null;
                            rsData = null;
                            ps = con.prepareStatement(childDataQuery);
                            rsData = ps.executeQuery();
                            childDataOb = new PbReturnObject(rsData);
                        }
                    }
                    if (memBersInfo.containsKey(childElementId)) {
                        childDet = (ArrayList) memBersInfo.get(childElementId);
                        childElementId = childDet.get(2).toString();
                        childEleName = childDet.get(0).toString();
                        parentName = currentName;
                    }
                    String deleteChilds = "";
                    deleteChilds = "delete from " + targetTable.trim() + " where fact_id=&";
                    for (int l = 0; l < childDataOb.getRowCount(); l++) {
                        String delFact_id = childDataOb.getFieldValueString(l, "FACT_ID");
                        Object fact[] = new Object[1];
                        fact[0] = delFact_id;
                        String findeleteChilds = buildQuery(deleteChilds, fact);
                        deleteQueries.add(findeleteChilds);
                    }
                    //currentName=childEleName;
                }

            }

        }

        try {
            executeMultiple(insertQList, con);
            executeMultiple(updates, con);
            executeMultiple(deleteQueries, con);
        } catch (Exception d) {
            logger.error("Exception:", d);
        }

    }

    public void saveTargetValues(Session sess) throws Exception {
        Connection con = ProgenConnection.getInstance().getConnection();
        ResultSet rsData;
        PreparedStatement ps;
        ps = null;
        rsData = null;

        PbTargetParamTable pTable = (PbTargetParamTable) sess.getObject("prg.targetparam.qdparams.PbTargetParamTable");
        PbTargetValuesParam targetParam = (PbTargetValuesParam) sess.getObject("prg.targetparam.qdparams.PbTargetValuesParam");
        String targetId = targetParam.getTargetId();
        String periodType = targetParam.getPeriodType();
        String minTimeLevel = targetParam.getMinimumTimeLevel();
        HashMap nonAllIds = targetParam.getNonAllIds();
        String viewBy = targetParam.getViewByParameter();
        ArrayList deleteQueries = new ArrayList();
        HashMap ratioComb = targetParam.getRatioComb();
        ArrayList updates = new ArrayList();
        String viewByElementId = "";
        String viewByColName = "";
        String nonAllDataClause = "";
        Object relSeqObj[] = new Object[1];
        relSeqObj[0] = "PRG_TARGET_DATA_SEQ";
        String factQuery = resBundle.getString("getSequenceNumber");
        ArrayList delList = new ArrayList();
        String finalfactQuery = buildQuery(factQuery, relSeqObj);

        ArrayList insertQList = new ArrayList();
        Object tarOb[] = new Object[1];
        tarOb[0] = targetId;
        String getTargetTable = resBundle.getString("getTargetTable");
        String fingetTargetTable = buildQuery(getTargetTable, tarOb);
        PbReturnObject targetTabOb = execSelectSQL(fingetTargetTable);
        String targetTable = targetTabOb.getFieldValueString(0, "BUSS_TABLE_NAME");
        String measureName2 = targetTabOb.getFieldValueString(0, "MEASURE_NAME");
        String measureName = measureName2.trim().replace(" ", "_");

        String getElementIdsBussCols = resBundle.getString("getElementIdsBussCols");
        String fingetElementIdsBussCols = buildQuery(getElementIdsBussCols, tarOb);
        PbReturnObject bussColOb = execSelectSQL(fingetElementIdsBussCols);
        HashMap elementIds = new HashMap();
        String viewByName = "";
        bussColOb.writeString();
        for (int p = 0; p < bussColOb.getRowCount(); p++) {
            elementIds.put(bussColOb.getFieldValueString(p, "ELEMENT_ID"), bussColOb.getFieldValueString(p, "BUSS_COL_NAME"));
            //if(viewBy.equalsIgnoreCase(bussColOb.getFieldValueString(p,"PARAM_DISP_NAME"))){
            if (viewBy.equalsIgnoreCase(bussColOb.getFieldValueString(p, "ELEMENT_ID"))) {
                viewByElementId = bussColOb.getFieldValueString(p, "ELEMENT_ID");
                viewByColName = bussColOb.getFieldValueString(p, "BUSS_COL_NAME");
                viewByName = bussColOb.getFieldValueString(p, "PARAM_DISP_NAME");
            }
        }

        String nonAll[] = (String[]) (nonAllIds.keySet()).toArray(new String[0]);
        HashMap nonAllNew = new HashMap();
        for (int y = 0; y < nonAll.length; y++) {
            String eleId = nonAll[y];
            String value = nonAllIds.get(eleId).toString();
            if (!value.equalsIgnoreCase("-1") && !value.equalsIgnoreCase("All")) {
                nonAllNew.put(eleId, value);
            }
        }
        String nonAllStr[] = (String[]) (nonAllNew.keySet()).toArray(new String[0]);
        for (int y = 0; y < nonAllStr.length; y++) {
            String eleId = nonAllStr[y];
            String colName = elementIds.get(eleId).toString();
            String value = nonAllIds.get(eleId).toString();
            if (y == 0) {
                nonAllDataClause = " and " + colName + "='" + value + "'";
            } else if (y == 1) {
                nonAllDataClause = nonAllDataClause + " and " + colName + "='" + value + "'";
            } else {
                nonAllDataClause = nonAllDataClause + " and " + colName + "='" + value + "'";
            }
        }
        int obSize = 0;
        // to store all the param columns in ordered manner
        ArrayList allParamsValues = new ArrayList();
        allParamsValues.add("TARGET_ID");
        allParamsValues.add("T_DATE");
        allParamsValues.add("T_MONTH");
        allParamsValues.add("T_QTR");
        allParamsValues.add("T_YEAR");

        String insertQ = "";
        obSize = bussColOb.getRowCount();
        int totalCount = obSize + 8;

        for (int i = 0; i < bussColOb.getRowCount(); i++) {
            allParamsValues.add(bussColOb.getFieldValueString(i, 2).trim());
            if (i == 0 && (bussColOb.getRowCount() > 1)) {
                insertQ = insertQ + bussColOb.getFieldValueString(i, 2).trim() + ",";
            } else if ((i == 0) && (bussColOb.getRowCount() == 1)) {
                insertQ = insertQ + bussColOb.getFieldValueString(i, 2).trim();
            } else if (i == (bussColOb.getRowCount() - 1)) {
                insertQ = insertQ + bussColOb.getFieldValueString(i, 2).trim();
            } else {
                insertQ = insertQ + bussColOb.getFieldValueString(i, 2).trim() + ",";
            }
            // allValsMap.put(allParamObj.getFieldValueString(i,"PARAMETER_NAME").trim(),"All");
        }

        allParamsValues.add(measureName.trim().toUpperCase());
        allParamsValues.add("VIEWBY");
        allParamsValues.add("FACT_ID");

        String replaceVals = "";
        for (int y = 0; y < totalCount; y++) {
            if (y == 0 && (totalCount > 1)) {
                replaceVals = replaceVals + "'&'" + ",";
            } else if ((y == 0) && (totalCount == 1)) {
                replaceVals = replaceVals + "'&'";
            } else if (y == totalCount - 1) {
                replaceVals = replaceVals + "'&'";
            } else {
                replaceVals = replaceVals + "'&'" + ",";
            }

        }
        // to get the dimensions.Its members  and all info
        String getTargetParamsInfo = resBundle.getString("getTargetParamsInfo");
        String fingetTargetParamsInfo = buildQuery(getTargetParamsInfo, tarOb);
        PbReturnObject paramInfoOb = execSelectSQL(fingetTargetParamsInfo);
        HashMap dimInfo = new HashMap();
        HashMap memBersInfo = new HashMap();
        HashMap allElements = new HashMap();

        for (int p = 0; p < paramInfoOb.getRowCount(); p++) {
            String dimId = paramInfoOb.getFieldValueString(p, "DIM_ID");
            String bussColName = "";
            if (elementIds.containsKey(paramInfoOb.getFieldValueString(p, "ELEMENT_ID"))) {
                bussColName = elementIds.get(paramInfoOb.getFieldValueString(p, "ELEMENT_ID")).toString();
            }
            ArrayList al = new ArrayList();
            al.add(0, paramInfoOb.getFieldValueString(p, "PARAM_DISP_NAME")); //name
            al.add(1, bussColName); //buss COl Name
            al.add(2, paramInfoOb.getFieldValueString(p, "CHILD_ELEMENT_ID")); // child id
            al.add(3, paramInfoOb.getFieldValueString(p, "REL_LEVEL")); //level
            memBersInfo.put(paramInfoOb.getFieldValueString(p, "ELEMENT_ID"), al);
        }
        //  String updateQuery = "update "+targetTable.trim()+" set "+measureName.trim().toUpperCase()+"=&"+where ;
        String updateQuery = "";// "update & set &=& where target_id=& "+nonAllDataClause+" and viewby=&";
        //Starting default parameters
        String initialParams = "insert into " + targetTable.trim() + "(TARGET_ID,T_DATE,T_MONTH,T_QTR,T_YEAR," + insertQ;
        String finalParameters = initialParams + "," + measureName.trim() + "," + " VIEWBY,FACT_ID) values(" + replaceVals + ")";
        for (int i = 0; i < pTable.getRowCount(); i++) {
            PbTargetValuesParam targetParam2 = (PbTargetValuesParam) pTable.getRow(i);
            long measureVal = targetParam2.getMeasureVal();
            String updateFlag = targetParam2.getUpdateFlag();
            String dat = targetParam2.getDateVal();
            String paramVal = targetParam2.getPrimViewByValue();
            HashMap allValsMap = new HashMap();
            String key = paramVal + ":" + dat;
            String[] a1 = (String[]) (nonAllIds.keySet()).toArray(new String[0]);
            float ratioN = 0;
            int oldValue = 0;
            if (ratioComb.containsKey(key)) {
                oldValue = ((Integer) ratioComb.get(key)).intValue();
                float divisionVal = (float) (measureVal - oldValue) / oldValue;
                ratioN = (float) 1 + divisionVal;
            }
            for (int k = 0; k < a1.length; k++) {
                String eleId = a1[k];
                String pVal = nonAllIds.get(eleId).toString();
                if (elementIds.containsKey(eleId)) {
                    String colName = elementIds.get(eleId).toString();
                    if (!allValsMap.containsKey(colName)) {
                        allValsMap.put(colName, pVal);
                    }
                }
            }
            allValsMap.put("TARGET_ID", targetId);
            // allValsMap.put(colName,paramVal);
            //insert
            if (updateFlag.equalsIgnoreCase("N")) {
                allValsMap.put(viewByColName.trim(), paramVal);
                String tQuery = "";
                String t_year = "";//dat.substring(7);
                String t_Qtr = "";
                String t_mon = "";
                String datVal = "";
                if (periodType.equalsIgnoreCase("Day")) {
                    tQuery = "SELECT DISTINCT CQTR,CYEAR,to_char(ddate,'MON-yy') CMON ,to_char(ddate,'dd-MON-yy') dd  FROM pr_day_denom  WHERE to_char(DDATE,'dd-mm-yy')= '" + dat + "'";
                    PbReturnObject timeOb = execSelectSQL(tQuery);
                    t_year = timeOb.getFieldValueString(0, "CYEAR");
                    t_Qtr = timeOb.getFieldValueString(0, "CQTR") + "-" + timeOb.getFieldValueString(0, "CYEAR");
                    t_mon = timeOb.getFieldValueString(0, "CMON");
                    datVal = timeOb.getFieldValueString(0, "DD");
                } else if (periodType.equalsIgnoreCase("Month")) {
                    tQuery = "select distinct  CQTR,CYEAR,to_char(CM_ST_DATE,'MON-yy') CMON from pr_day_denom where to_date(CM_ST_DATE,'dd-mon-yy')=to_date('" + dat + "','mm-yy')";
                    PbReturnObject timeOb = execSelectSQL(tQuery);
                    t_year = timeOb.getFieldValueString(0, "CYEAR");
                    t_Qtr = timeOb.getFieldValueString(0, "CQTR") + "-" + timeOb.getFieldValueString(0, "CYEAR");
                    t_mon = timeOb.getFieldValueString(0, "CMON");
                } else if (periodType.equalsIgnoreCase("Quarter")) {
                    tQuery = "select distinct CQTR,CYEAR,CMONTH,to_date(CM_ST_DATE,'dd-mon-yy') DD from pr_day_denom where to_date(CM_ST_DATE,'dd-mon-yy')=to_date('" + dat + "','dd-mon-yy')";
                    t_Qtr = dat;
                    t_year = dat.substring(2);
                } else if (periodType.equalsIgnoreCase("Year")) {
                    tQuery = "select distinct CQTR,CYEAR,CMONTH,to_date(CM_ST_DATE,'dd-mon-yy') DD from pr_day_denom where to_date(CM_ST_DATE,'dd-mon-yy')=to_date('" + dat + "','dd-mon-yy')";
                    t_year = dat;
                }
                int fact_id = getSequenceNumber(finalfactQuery);
//                allValsMap.put(measureName.trim().toUpperCase(), "" + measureVal + "");
                allValsMap.put(measureName.trim().toUpperCase(), String.valueOf(measureVal));
//                allValsMap.put("TARGET_ID", "" + targetId + "");
                allValsMap.put("TARGET_ID", String.valueOf(targetId));
                allValsMap.put("T_DATE", datVal);
                allValsMap.put("T_MONTH", t_mon);
                allValsMap.put("T_QTR", t_Qtr);
                allValsMap.put("T_YEAR", t_year);
//                allValsMap.put("FACT_ID", "" + fact_id + "");
                allValsMap.put("FACT_ID", String.valueOf(fact_id));
                allValsMap.put("VIEWBY", viewByName.trim());

                Object insertObjDy[] = new Object[allParamsValues.size()];
                for (int t = 0; t < allParamsValues.size(); t++) {
                    String colNames = (String) allParamsValues.get(t);
                    if (allValsMap.containsKey(colNames)) {
                        if ((String) allValsMap.get(colNames) == null) {
                            insertObjDy[t] = "All";
                        } else if (allValsMap.get(colNames) != null) {
                            insertObjDy[t] = (String) allValsMap.get(colNames);
                        } else {
                            insertObjDy[t] = "";
                        }
                    }
                }
                String finalInsertDyn = buildQuery(finalParameters, insertObjDy);
                insertQList.add(finalInsertDyn);
            } else if (updateFlag.equalsIgnoreCase("Y")) {
                String childDataQuery = "";
                String childElementId = "";
                String childEleName = "";
                ArrayList childDet = new ArrayList();
                String currentName = "";

                String parentName = "";
                if (memBersInfo.containsKey(viewByElementId)) {
                    ArrayList alDet = (ArrayList) memBersInfo.get(viewByElementId);
                    childElementId = alDet.get(2).toString();
                    currentName = alDet.get(0).toString();

                    if (memBersInfo.containsKey(childElementId)) {
                        childEleName = alDet.get(2).toString();
                        childDet = (ArrayList) memBersInfo.get(childElementId);
                        childEleName = childDet.get(0).toString();
                    }
                }

                HashMap updateVals = new HashMap();
                //"update "+targetTable.trim()+" set "+measureName.trim().toUpperCase()+"=& where target_id="+targetId+" "+nonAllDataClause;
                if (periodType.equalsIgnoreCase("Month")) {
                    if (minTimeLevel.equalsIgnoreCase("Month")) {
                        updateQuery = "update & set &=& where target_id='&' " + nonAllDataClause + " and viewby='&' and t_month='&' and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                    } else if (minTimeLevel.equalsIgnoreCase("Day")) {
                        updateQuery = "update & set &=& where target_id='&' " + nonAllDataClause + " and viewby='&' and t_date=to_date('&','dd-mm-yy') and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                    }

                } else if (periodType.equalsIgnoreCase("Day")) {
                    updateQuery = "update & set &=& where target_id='&' " + nonAllDataClause + " and viewby='&' and t_date=to_date('&','dd-mm-yy') and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                } else if (periodType.equalsIgnoreCase("Quarter")) {
                    if (minTimeLevel.equalsIgnoreCase("Quarter")) {
                        updateQuery = "update & set &=& where target_id='&' " + nonAllDataClause + " and viewby='&' and t_qtr='&' and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                    } else if (minTimeLevel.equalsIgnoreCase("Month")) {
                        updateQuery = "update & set &=& where target_id='&' " + nonAllDataClause + " and viewby='&' and t_month='&' and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                    } else if (minTimeLevel.equalsIgnoreCase("Day")) {
                        updateQuery = "update & set &=& where target_id='&' " + nonAllDataClause + " and viewby='&' and t_date=to_date('&','dd-mm-yy') and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                    }

                } else if (periodType.equalsIgnoreCase("Year")) {
                    if (minTimeLevel.equalsIgnoreCase("Year")) {
                        updateQuery = "update & set &=& where target_id='&' " + nonAllDataClause + " and viewby='&' and t_year='&' and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                    } else if (minTimeLevel.equalsIgnoreCase("Quarter")) {
                        updateQuery = "update & set &=& where target_id='&' " + nonAllDataClause + " and viewby='&' and t_qtr='&' and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                    } else if (minTimeLevel.equalsIgnoreCase("Month")) {
                        updateQuery = "update & set &=& where target_id='&' " + nonAllDataClause + " and viewby='&' and t_month='&' and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                    } else if (minTimeLevel.equalsIgnoreCase("Day")) {
                        updateQuery = "update & set &=& where target_id='&' " + nonAllDataClause + " and viewby='&' and t_date=to_date('&','dd-mm-yy') and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                    }

                }
                //memBersInfo
                ArrayList alreadyUpdateFact = new ArrayList();
                Object update[] = new Object[6];
                update[0] = targetTable.trim();
                update[1] = measureName.trim().toUpperCase();
                update[2] = new Long(measureVal);
                update[3] = targetId;
                update[4] = viewByName.trim();
                update[5] = dat;
                String finupdateQuery = buildQuery(updateQuery, update);
                // finupdateQuery = finupdateQuery;//+nonAllDataClause;
                updateVals.put(paramVal, new Long(measureVal));
                insertQList.add(finupdateQuery);
                // as long as child id is not -1 loop
                PbReturnObject childDataOb = new PbReturnObject();
                if (!childElementId.equalsIgnoreCase("-1")) {
                    if (periodType.equalsIgnoreCase("Month")) {
                        if (minTimeLevel.equalsIgnoreCase("Month")) {
                            childDataQuery = "select " + elementIds.get(childElementId.trim()).toString() + "," + measureName.trim() + ",fact_id from " + targetTable.trim() + " where target_id=" + targetId + " and t_month='" + dat + "' and viewby='" + childEleName + "' and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                        } else if (minTimeLevel.equalsIgnoreCase("Day")) {
                            childDataQuery = "select " + elementIds.get(childElementId.trim()).toString() + "," + measureName.trim() + ",fact_id from " + targetTable.trim() + " where target_id=" + targetId + " and t_date=to_date('" + dat + "','dd-mm-yy') and viewby='" + childEleName + "' and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                        }
                        // execSelectSQL(childDataQuery);
                    } else if (periodType.equalsIgnoreCase("Day")) {
                        if (minTimeLevel.equalsIgnoreCase("Day")) {
                            childDataQuery = "select " + elementIds.get(childElementId.trim()).toString() + "," + measureName.trim() + ",fact_id from " + targetTable.trim() + " where target_id=" + targetId + " and t_date=to_date('" + dat + "','dd-mm-yy') and viewby='" + childEleName + "' and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                        }
                        // execSelectSQL(childDataQuery);
                    }
                    if (periodType.equalsIgnoreCase("Quarter")) {
                        if (minTimeLevel.equalsIgnoreCase("Quarter")) {
                            childDataQuery = "select " + elementIds.get(childElementId.trim()).toString() + "," + measureName.trim() + ",fact_id from " + targetTable.trim() + " where target_id=" + targetId + " and t_qtr='" + dat + "' and viewby='" + childEleName + "' and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                        } else if (minTimeLevel.equalsIgnoreCase("Month")) {
                            childDataQuery = "select " + elementIds.get(childElementId.trim()).toString() + "," + measureName.trim() + ",fact_id from " + targetTable.trim() + " where target_id=" + targetId + " and t_month='" + dat + "' and viewby='" + childEleName + "' and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                        } else if (minTimeLevel.equalsIgnoreCase("Day")) {
                            childDataQuery = "select " + elementIds.get(childElementId.trim()).toString() + "," + measureName.trim() + ",fact_id from " + targetTable.trim() + " where target_id=" + targetId + " and t_date=to_date('" + dat + "','dd-mm-yy') and viewby='" + childEleName + "' and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                        }
                        //  execSelectSQL(childDataQuery);
                    }
                    if (periodType.equalsIgnoreCase("Year")) {
                        if (minTimeLevel.equalsIgnoreCase("Year")) {
                            childDataQuery = "select " + elementIds.get(childElementId.trim()).toString() + "," + measureName.trim() + ",fact_id from " + targetTable.trim() + " where target_id=" + targetId + " and t_year='" + dat + "' and viewby='" + childEleName + "' and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                        } else if (minTimeLevel.equalsIgnoreCase("Quarter")) {
                            childDataQuery = "select " + elementIds.get(childElementId.trim()).toString() + "," + measureName.trim() + ",fact_id from " + targetTable.trim() + " where target_id=" + targetId + " and t_qtr='" + dat + "' and viewby='" + childEleName + "' and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                        } else if (minTimeLevel.equalsIgnoreCase("Month")) {
                            childDataQuery = "select " + elementIds.get(childElementId.trim()).toString() + "," + measureName.trim() + ",fact_id from " + targetTable.trim() + " where target_id=" + targetId + " and t_month='" + dat + "' and viewby='" + childEleName + "' and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                        } else if (minTimeLevel.equalsIgnoreCase("Day")) {
                            childDataQuery = "select " + elementIds.get(childElementId.trim()).toString() + "," + measureName.trim() + ",fact_id from " + targetTable.trim() + " where target_id=" + targetId + " and t_date=to_date('" + dat + "','dd-mm-yy') and viewby='" + childEleName + "' and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                        }
                        // execSelectSQL(childDataQuery);
                    }
                    ps = null;
                    rsData = null;
                    ps = con.prepareStatement(childDataQuery);
                    rsData = ps.executeQuery();
                    childDataOb = new PbReturnObject(rsData);
                    //childDataOb = execSelectSQL(childDataQuery);
                }

                int totalR = 0;
                int allTot = 0;
                for (int m = 0; m < childDataOb.getRowCount(); m++) {
                    totalR = totalR + childDataOb.getFieldValueInt(m, measureName.trim().toUpperCase());
                }
                //get the change in ratio
                float ratio = 0;
                float divisionVal = 0;
                //  float ratioN=0;

                divisionVal = (float) (measureVal - totalR) / totalR;
                if (ratioN == 0) {
                    ratioN = 1 + divisionVal;
                }
                //if(totalR==0)
                //   ratioN = 1;
                // childElementId = viewByElementId;
                String temoparyParElement = childElementId;
                boolean skipFlag = false;
                while (skipFlag == false || !(childElementId.equalsIgnoreCase("-1"))) {
                    if (childElementId.equalsIgnoreCase("-1")) {
                        skipFlag = true;
                    }
                    if (periodType.equalsIgnoreCase("Month")) {
                        if (minTimeLevel.equalsIgnoreCase("Month")) {
                            childDataQuery = "select " + measureName.trim() + ",fact_id from " + targetTable.trim() + " where target_id=" + targetId + " and t_month='" + dat + "' and viewby='" + childEleName + "' and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                        } else if (minTimeLevel.equalsIgnoreCase("Day")) {
                            childDataQuery = "select " + measureName.trim() + ",fact_id from " + targetTable.trim() + " where target_id=" + targetId + " and t_date=to_date('" + dat + "','dd-mm-yy') and viewby='" + childEleName + "' and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                        }

                        // childDataOb=execSelectSQL(childDataQuery);
                        ps = null;
                        rsData = null;
                        ps = con.prepareStatement(childDataQuery);
                        rsData = ps.executeQuery();
                        childDataOb = new PbReturnObject(rsData);
                    }
                    if (periodType.equalsIgnoreCase("Day")) {
                        if (minTimeLevel.equalsIgnoreCase("Day")) {
                            childDataQuery = "select " + measureName.trim() + ",fact_id from " + targetTable.trim() + " where target_id=" + targetId + " and t_date=to_date('" + dat + "','dd-mm-yy') and viewby='" + childEleName + "' and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                        }
                        ////////////////////////////////////////////////////////////////////////////////////////////////////////.println(" childDataQuery.-''. "+childDataQuery);
                        // childDataOb=execSelectSQL(childDataQuery);
                        ps = null;
                        rsData = null;
                        ps = con.prepareStatement(childDataQuery);
                        rsData = ps.executeQuery();
                        childDataOb = new PbReturnObject(rsData);
                    }
                    if (periodType.equalsIgnoreCase("Quarter")) {
                        ////////////////////////////////////////////////////////////////////////////////////////////////////////.println(" here---");
                        if (minTimeLevel.equalsIgnoreCase("Month")) {
                            childDataQuery = "select " + measureName.trim() + ",fact_id from " + targetTable.trim() + " where target_id=" + targetId + " and t_month='" + dat + "' and viewby='" + childEleName + "' and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                        } else if (minTimeLevel.equalsIgnoreCase("Day")) {
                            childDataQuery = "select " + measureName.trim() + ",fact_id from " + targetTable.trim() + " where target_id=" + targetId + " and t_date=to_date('" + dat + "','dd-mm-yy') and viewby='" + childEleName + "' and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                        } else if (minTimeLevel.equalsIgnoreCase("Quarter")) {
                            childDataQuery = "select " + measureName.trim() + ",fact_id from " + targetTable.trim() + " where target_id=" + targetId + " and t_qtr='" + dat + "' and viewby='" + childEleName + "' and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                        }

                        ////////////////////////////////////////////////////////////////////////////////////////////////////////.println(" childDataQuery.-. "+childDataQuery);
                        // childDataOb=execSelectSQL(childDataQuery);
                        ps = null;
                        rsData = null;
                        ps = con.prepareStatement(childDataQuery);
                        rsData = ps.executeQuery();
                        childDataOb = new PbReturnObject(rsData);

                    }
                    if (periodType.equalsIgnoreCase("Year")) {
                        if (minTimeLevel.equalsIgnoreCase("Year")) {
                            childDataQuery = "select " + measureName.trim() + ",fact_id from " + targetTable.trim() + " where target_id=" + targetId + " and t_year='" + dat + "' and viewby='" + childEleName + "' and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                        } else if (minTimeLevel.equalsIgnoreCase("Month")) {
                            childDataQuery = "select " + measureName.trim() + ",fact_id from " + targetTable.trim() + " where target_id=" + targetId + " and t_month='" + dat + "' and viewby='" + childEleName + "' and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                        } else if (minTimeLevel.equalsIgnoreCase("Day")) {
                            childDataQuery = "select " + measureName.trim() + ",fact_id from " + targetTable.trim() + " where target_id=" + targetId + " and t_date=to_date('" + dat + "','dd-mm-yy') and viewby='" + childEleName + "' and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                        } else if (minTimeLevel.equalsIgnoreCase("Quarter")) {
                            childDataQuery = "select " + measureName.trim() + ",fact_id from " + targetTable.trim() + " where target_id=" + targetId + " and t_qtr='" + dat + "' and viewby='" + childEleName + "' and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                        }

                        ////////////////////////////////////////////////////////////////////////////////////////////////////////.println(" childDataQuery.-. "+childDataQuery);
                        // childDataOb=execSelectSQL(childDataQuery);
                        ps = null;
                        rsData = null;
                        ps = con.prepareStatement(childDataQuery);
                        rsData = ps.executeQuery();
                        childDataOb = new PbReturnObject(rsData);

                    }

                    // for updation start
                    int chilCounts = childDataOb.getRowCount();
                    if (chilCounts == 0) {
                        chilCounts = 1;
                    }
                    long individualCount = 1;
                    individualCount = measureVal / chilCounts;
                    for (int m = 0; m < childDataOb.getRowCount(); m++) {
                        int newVal = 0;
                        if (totalR != 0) {
                            ratio = measureVal / totalR;
                            ratio = (float) childDataOb.getFieldValueInt(m, measureName.trim().toUpperCase()) / totalR;
                            newVal = (int) (ratio * measureVal);
                        }
                        ////////////////////////////////////////////////////////////////////////////////////////////////////////.println(childDataOb.getFieldValueInt(m, measureName.trim().toUpperCase())+"---------/././ new "+ratioN * childDataOb.getFieldValueInt(m, measureName.trim().toUpperCase()));
                        //int newM = (int)Math.floor(ratioN * childDataOb.getFieldValueInt(m, measureName.trim().toUpperCase()));//;Math.round(ratioN * chilNos.getFieldValueInt(m, measureName.trim().toUpperCase()));
                        int newM = (int) Math.round(ratioN * childDataOb.getFieldValueInt(m, measureName.trim().toUpperCase()));//;Math.round(ratioN * chilNos.getFieldValueInt(m, measureName.trim().toUpperCase()));

                        allTot = allTot + newM;
                        //////////////////////////////////////////////////////////////////////////////////////////////////.print("  --------- "+childDataOb.getFieldValueInt(m,"FACT_ID")+"....");
                        String fact_id = childDataOb.getFieldValueString(m, "FACT_ID");
                        String updateT = "update " + targetTable.trim() + " set " + measureName.trim() + "=& where fact_id=&";
                        Object updateChilds[] = new Object[2];
                        updateChilds[0] = Integer.valueOf(newM);
                        updateChilds[1] = fact_id;
                        updateVals.put(childDataOb.getFieldValueString(m, 0), Integer.valueOf(newVal));//individualCount));
                        String finalupdateT = buildQuery(updateT, updateChilds);

                        if (!alreadyUpdateFact.contains(Integer.valueOf(childDataOb.getFieldValueInt(m, "FACT_ID")))) {
                            finalupdateT = buildQuery(updateT, updateChilds);
                            alreadyUpdateFact.add(Integer.valueOf(childDataOb.getFieldValueInt(m, "FACT_ID")));
                            updates.add(finalupdateT);
                        }

                    }
                    //for updation over
                    if (memBersInfo.containsKey(childElementId)) {
                        childDet = (ArrayList) memBersInfo.get(childElementId);
                        childElementId = childDet.get(2).toString();
                        childEleName = childDet.get(0).toString();
                        parentName = currentName;
                        //////////////////////////////////////////////////////////////////////////////////////////////////////////.println(skipFlag+" .... "+childEleName+" childEleName "+parentName+" childElementId "+childElementId);
                        temoparyParElement = childElementId;
                    }
                    // childElementId=temoparyParElement;
                    //currentName=childEleName;
                }
                //  ////////////////////////////////////////////////////////////////////////////////////////////////////////.println(" updateVals "+updateVals);

            } else if (updateFlag.equalsIgnoreCase("D")) {
                String deleteQuery = "";
                if (periodType.equalsIgnoreCase("Month")) {
                    if (minTimeLevel.equalsIgnoreCase("Month")) {
                        deleteQuery = "delete from " + targetTable.trim() + " where target_id=" + targetId + " and viewby='&' and t_month='&' " + nonAllDataClause + " and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                    }
                    if (minTimeLevel.equalsIgnoreCase("Day")) {
                        deleteQuery = "delete from " + targetTable.trim() + " where target_id=" + targetId + " and viewby='&' and t_date=to_date('&','dd-mm-yy') " + nonAllDataClause + " and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                    }

                } else if (periodType.equalsIgnoreCase("Day")) {
                    deleteQuery = "delete from " + targetTable.trim() + " where target_id=" + targetId + " and viewby='&' and t_date=to_date('&','dd-mm-yy') " + nonAllDataClause + " and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                }
                if (periodType.equalsIgnoreCase("Quarter")) {
                    if (minTimeLevel.equalsIgnoreCase("Quarter")) {
                        deleteQuery = "delete from " + targetTable.trim() + " where target_id=" + targetId + " and viewby='&' and t_qtr='&' " + nonAllDataClause + " and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                    }
                    if (minTimeLevel.equalsIgnoreCase("Month")) {
                        deleteQuery = "delete from " + targetTable.trim() + " where target_id=" + targetId + " and viewby='&' and t_month='&' " + nonAllDataClause + " and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                    }
                    if (minTimeLevel.equalsIgnoreCase("Day")) {
                        deleteQuery = "delete from " + targetTable.trim() + " where target_id=" + targetId + " and viewby='&' and t_date=to_date('&','dd-mm-yy') " + nonAllDataClause + " and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                    }

                }
                if (periodType.equalsIgnoreCase("Year")) {
                    if (minTimeLevel.equalsIgnoreCase("Year")) {
                        deleteQuery = "delete from " + targetTable.trim() + " where target_id=" + targetId + " and viewby='&' and t_year='&' " + nonAllDataClause + " and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                    }
                    if (minTimeLevel.equalsIgnoreCase("Quarter")) {
                        deleteQuery = "delete from " + targetTable.trim() + " where target_id=" + targetId + " and viewby='&' and t_qtr='&' " + nonAllDataClause + " and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                    }
                    if (minTimeLevel.equalsIgnoreCase("Month")) {
                        deleteQuery = "delete from " + targetTable.trim() + " where target_id=" + targetId + " and viewby='&' and t_month='&' " + nonAllDataClause + " and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                    }
                    if (minTimeLevel.equalsIgnoreCase("Day")) {
                        deleteQuery = "delete from " + targetTable.trim() + " where target_id=" + targetId + " and viewby='&' and t_date=to_date('&','dd-mm-yy') " + nonAllDataClause + " and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                    }

                }
                Object delOb[] = new Object[2];
                delOb[0] = viewByName.trim();
                delOb[1] = dat;
                String finDeleteQ = buildQuery(deleteQuery, delOb);
                ////////////////////////////////////////////////////////////////////////////////////////////////////////.println(" deleteQuery// "+finDeleteQ);
                deleteQueries.add(finDeleteQ);

                String childDataQuery = "";
                String childElementId = "";
                String childEleName = "";
                ArrayList childDet = new ArrayList();
                String currentName = "";

                String parentName = "";
                //////////////////////////////////////////////////////////////////////////////////////////////////////////.println(" viewByElementId . "+viewByElementId);
                if (memBersInfo.containsKey(viewByElementId)) {
                    ArrayList alDet = (ArrayList) memBersInfo.get(viewByElementId);
                    childElementId = alDet.get(2).toString();
                    currentName = alDet.get(0).toString();

                    if (memBersInfo.containsKey(childElementId)) {
                        childEleName = alDet.get(2).toString();
                        childDet = (ArrayList) memBersInfo.get(childElementId);
                        childEleName = childDet.get(0).toString();

                    }
                }
                if (periodType.equalsIgnoreCase("Month")) {
                    childDataQuery = "select " + elementIds.get(childElementId.trim()).toString() + "," + measureName.trim() + ",fact_id from " + targetTable.trim() + " where target_id=" + targetId + " and t_month='" + dat + "' and viewby='" + childEleName + "' and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                }
                PbReturnObject childDataOb = new PbReturnObject();
                boolean skipFlag = false;
                while (!childElementId.equalsIgnoreCase("-1") || skipFlag == false) {
                    if (childElementId.equalsIgnoreCase("-1")) {
                        skipFlag = true;
                    }
                    if (periodType.equalsIgnoreCase("Month")) {
                        if (minTimeLevel.equalsIgnoreCase("Month")) {
                            childDataQuery = "select " + measureName.trim() + ",fact_id from " + targetTable.trim() + " where target_id=" + targetId + " and t_month='" + dat + "' and viewby='" + childEleName + "' and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                        } else if (minTimeLevel.equalsIgnoreCase("Day")) {
                            childDataQuery = "select " + measureName.trim() + ",fact_id from " + targetTable.trim() + " where target_id=" + targetId + " and t_date=to_date('" + dat + "','dd-mm-yy') and viewby='" + childEleName + "' and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                        }

                        ////////////////////////////////////////////////////////////////////////////////////////////////////////.println(" childDataQuery.-. "+childDataQuery);
                        // childDataOb=execSelectSQL(childDataQuery);
                        ps = null;
                        rsData = null;
                        ps = con.prepareStatement(childDataQuery);
                        rsData = ps.executeQuery();
                        childDataOb = new PbReturnObject(rsData);

                    } else if (periodType.equalsIgnoreCase("Quarter")) {
                        if (minTimeLevel.equalsIgnoreCase("Month")) {
                            childDataQuery = "select " + measureName.trim() + ",fact_id from " + targetTable.trim() + " where target_id=" + targetId + " and t_month='" + dat + "' and viewby='" + childEleName + "' and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                        } else if (minTimeLevel.equalsIgnoreCase("Day")) {
                            childDataQuery = "select " + measureName.trim() + ",fact_id from " + targetTable.trim() + " where target_id=" + targetId + " and t_date=to_date('" + dat + "','dd-mm-yy') and viewby='" + childEleName + "' and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                        } else if (minTimeLevel.equalsIgnoreCase("Quarter")) {
                            childDataQuery = "select " + measureName.trim() + ",fact_id from " + targetTable.trim() + " where target_id=" + targetId + " and t_qtr='" + dat + "' and viewby='" + childEleName + "' and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                        }

                        ////////////////////////////////////////////////////////////////////////////////////////////////////////.println(" childDataQuery.-. "+childDataQuery);
                        // childDataOb=execSelectSQL(childDataQuery);
                        ps = null;
                        rsData = null;
                        ps = con.prepareStatement(childDataQuery);
                        rsData = ps.executeQuery();
                        childDataOb = new PbReturnObject(rsData);

                    } else if (periodType.equalsIgnoreCase("Year")) {
                        if (minTimeLevel.equalsIgnoreCase("Month")) {
                            childDataQuery = "select " + measureName.trim() + ",fact_id from " + targetTable.trim() + " where target_id=" + targetId + " and t_month='" + dat + "' and viewby='" + childEleName + "' and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                        } else if (minTimeLevel.equalsIgnoreCase("Day")) {
                            childDataQuery = "select " + measureName.trim() + ",fact_id from " + targetTable.trim() + " where target_id=" + targetId + " and t_date=to_date('" + dat + "','dd-mm-yy') and viewby='" + childEleName + "' and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                        } else if (minTimeLevel.equalsIgnoreCase("Quarter")) {
                            childDataQuery = "select " + measureName.trim() + ",fact_id from " + targetTable.trim() + " where target_id=" + targetId + " and t_qtr='" + dat + "' and viewby='" + childEleName + "' and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                        } else if (minTimeLevel.equalsIgnoreCase("Year")) {
                            childDataQuery = "select " + measureName.trim() + ",fact_id from " + targetTable.trim() + " where target_id=" + targetId + " and t_year='" + dat + "' and viewby='" + childEleName + "' and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                        }

                        ////////////////////////////////////////////////////////////////////////////////////////////////////////.println(" childDataQuery.-. "+childDataQuery);
                        // childDataOb=execSelectSQL(childDataQuery);
                        ps = null;
                        rsData = null;
                        ps = con.prepareStatement(childDataQuery);
                        rsData = ps.executeQuery();
                        childDataOb = new PbReturnObject(rsData);

                    } else if (periodType.equalsIgnoreCase("Day")) {
                        if (minTimeLevel.equalsIgnoreCase("Day")) {
                            childDataQuery = "select " + measureName.trim() + ",fact_id from " + targetTable.trim() + " where target_id=" + targetId + " and t_date=to_date('" + dat + "','dd-mm-yy') and viewby='" + childEleName + "' and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                            // childDataOb=execSelectSQL(childDataQuery);
                            ps = null;
                            rsData = null;
                            ps = con.prepareStatement(childDataQuery);
                            rsData = ps.executeQuery();
                            childDataOb = new PbReturnObject(rsData);
                        }
                    }
                    if (memBersInfo.containsKey(childElementId)) {
                        childDet = (ArrayList) memBersInfo.get(childElementId);
                        childElementId = childDet.get(2).toString();
                        childEleName = childDet.get(0).toString();
                        parentName = currentName;
                    }
                    String deleteChilds = "";
                    deleteChilds = "delete from " + targetTable.trim() + " where fact_id=&";
                    for (int l = 0; l < childDataOb.getRowCount(); l++) {
                        String delFact_id = childDataOb.getFieldValueString(l, "FACT_ID");
                        Object fact[] = new Object[1];
                        fact[0] = delFact_id;
                        String findeleteChilds = buildQuery(deleteChilds, fact);
                        deleteQueries.add(findeleteChilds);
                    }
                    //currentName=childEleName;
                }

            }
        }

        try {
            executeMultiple(deleteQueries, con);
            executeMultiple(insertQList, con);
            executeMultiple(updates, con);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        if (rsData != null) {
            rsData.close();
        }
        if (ps != null) {
            ps.close();
        }
        if (con != null) {
            con.close();
        }
    }
// to save the data when any paramter is selected in the parimary view by and time is not there in secondary

    public void savePrimSec(Session sess) throws Exception {
        Connection con = ProgenConnection.getInstance().getConnection();
        ResultSet rsData;
        PreparedStatement ps;
        ps = null;
        rsData = null;
        PbTargetParamTable pTable = (PbTargetParamTable) sess.getObject("prg.targetparam.qdparams.PbTargetParamTable");
        PbTargetValuesParam targetParam = (PbTargetValuesParam) sess.getObject("prg.targetparam.qdparams.PbTargetValuesParam");
        String targetId = targetParam.getTargetId();
        String periodType = targetParam.getPeriodType();
        String minTimeLevel = targetParam.getMinimumTimeLevel();
        HashMap nonAllIds = targetParam.getNonAllIds();
        String viewBy = targetParam.getViewByParameter();
        String primParameterElementId = targetParam.getPrimaryParameter();
        ArrayList deleteQueries = new ArrayList();
        ArrayList updates = new ArrayList();
        String viewByElementId = "";
        String viewByColName = "";
        String nonAllDataClause = "";
        HashMap ratioComb = targetParam.getRatioComb();

        Object relSeqObj[] = new Object[1];
        relSeqObj[0] = "PRG_TARGET_DATA_SEQ";
        String factQuery = resBundle.getString("getSequenceNumber");
//   ArrayList delList = new ArrayList();
        String finalfactQuery = buildQuery(factQuery, relSeqObj);

        ArrayList insertQList = new ArrayList();
        Object tarOb[] = new Object[1];
        tarOb[0] = targetId;
        String getTargetTable = resBundle.getString("getTargetTable");
        String fingetTargetTable = buildQuery(getTargetTable, tarOb);
        PbReturnObject targetTabOb = execSelectSQL(fingetTargetTable);
        String targetTable = targetTabOb.getFieldValueString(0, "BUSS_TABLE_NAME");
        String measureName2 = targetTabOb.getFieldValueString(0, "MEASURE_NAME");
        String measureName = measureName2.trim().replace(" ", "_");

        String getElementIdsBussCols = resBundle.getString("getElementIdsBussCols");
        String fingetElementIdsBussCols = buildQuery(getElementIdsBussCols, tarOb);
        PbReturnObject bussColOb = execSelectSQL(fingetElementIdsBussCols);
        HashMap elementIds = new HashMap();
        String viewByName = "";
        bussColOb.writeString();
        for (int p = 0; p < bussColOb.getRowCount(); p++) {
            elementIds.put(bussColOb.getFieldValueString(p, "ELEMENT_ID"), bussColOb.getFieldValueString(p, "BUSS_COL_NAME"));
            //if(viewBy.equalsIgnoreCase(bussColOb.getFieldValueString(p,"PARAM_DISP_NAME"))){
            if (viewBy.equalsIgnoreCase(bussColOb.getFieldValueString(p, "ELEMENT_ID"))) {
                viewByElementId = bussColOb.getFieldValueString(p, "ELEMENT_ID");
                viewByColName = bussColOb.getFieldValueString(p, "BUSS_COL_NAME");
                viewByName = bussColOb.getFieldValueString(p, "PARAM_DISP_NAME");
            }
        }

        String nonAll[] = (String[]) (nonAllIds.keySet()).toArray(new String[0]);
        HashMap nonAllNew = new HashMap();
        for (int y = 0; y < nonAll.length; y++) {
            String eleId = nonAll[y];
            String value = nonAllIds.get(eleId).toString();
            if (!value.equalsIgnoreCase("-1") && !value.equalsIgnoreCase("-1")) {
                nonAllNew.put(eleId, value);
            }
        }
        String nonAllStr[] = (String[]) (nonAllNew.keySet()).toArray(new String[0]);
        for (int y = 0; y < nonAllStr.length; y++) {
            String eleId = nonAllStr[y];
            String colName = elementIds.get(eleId).toString();
            String value = nonAllIds.get(eleId).toString();
            if (y == 0) {
                nonAllDataClause = " and " + colName + "='" + value + "'";
            } else if (y == 1) {
                nonAllDataClause = nonAllDataClause + "  " + colName + "='" + value + "'";
            } else {
                nonAllDataClause = nonAllDataClause + " and " + colName + "='" + value + "'";
            }
        }

        int obSize = 0;
        // to store all the param columns in ordered manner
        ArrayList allParamsValues = new ArrayList();
        allParamsValues.add("TARGET_ID");
        allParamsValues.add("T_DATE");
        allParamsValues.add("T_MONTH");
        allParamsValues.add("T_QTR");
        allParamsValues.add("T_YEAR");

        String insertQ = "";
        obSize = bussColOb.getRowCount();
        int totalCount = obSize + 8;

        for (int i = 0; i < bussColOb.getRowCount(); i++) {
            allParamsValues.add(bussColOb.getFieldValueString(i, 2).trim());
            if (i == 0 && (bussColOb.getRowCount() > 1)) {
                insertQ = insertQ + bussColOb.getFieldValueString(i, 2).trim() + ",";
            } else if ((i == 0) && (bussColOb.getRowCount() == 1)) {
                insertQ = insertQ + bussColOb.getFieldValueString(i, 2).trim();
            } else if (i == (bussColOb.getRowCount() - 1)) {
                insertQ = insertQ + bussColOb.getFieldValueString(i, 2).trim();
            } else {
                insertQ = insertQ + bussColOb.getFieldValueString(i, 2).trim() + ",";
            }
            // allValsMap.put(allParamObj.getFieldValueString(i,"PARAMETER_NAME").trim(),"All");
        }

        allParamsValues.add(measureName.trim().toUpperCase());
        allParamsValues.add("VIEWBY");
        allParamsValues.add("FACT_ID");

        String replaceVals = "";
        for (int y = 0; y < totalCount; y++) {
            if (y == 0 && (totalCount > 1)) {
                replaceVals = replaceVals + "'&'" + ",";
            } else if ((y == 0) && (totalCount == 1)) {
                replaceVals = replaceVals + "'&'";
            } else if (y == totalCount - 1) {
                replaceVals = replaceVals + "'&'";
            } else {
                replaceVals = replaceVals + "'&'" + ",";
            }

        }
        // to get the dimensions.Its members  and all info
        String getTargetParamsInfo = resBundle.getString("getTargetParamsInfo");
        String fingetTargetParamsInfo = buildQuery(getTargetParamsInfo, tarOb);
        PbReturnObject paramInfoOb = execSelectSQL(fingetTargetParamsInfo);
        HashMap dimInfo = new HashMap();
        HashMap memBersInfo = new HashMap();
        HashMap allElements = new HashMap();

        for (int p = 0; p < paramInfoOb.getRowCount(); p++) {
            String dimId = paramInfoOb.getFieldValueString(p, "DIM_ID");
            String bussColName = "";
            if (elementIds.containsKey(paramInfoOb.getFieldValueString(p, "ELEMENT_ID"))) {
                bussColName = elementIds.get(paramInfoOb.getFieldValueString(p, "ELEMENT_ID")).toString();
            }
            ArrayList al = new ArrayList();
            al.add(0, paramInfoOb.getFieldValueString(p, "PARAM_DISP_NAME")); //name
            al.add(1, bussColName); //buss COl Name
            al.add(2, paramInfoOb.getFieldValueString(p, "CHILD_ELEMENT_ID")); // child id
            al.add(3, paramInfoOb.getFieldValueString(p, "REL_LEVEL")); //level
            memBersInfo.put(paramInfoOb.getFieldValueString(p, "ELEMENT_ID"), al);
        }
        //  String updateQuery = "update "+targetTable.trim()+" set "+measureName.trim().toUpperCase()+"=&"+where ;
        String updateQuery = "";// "update & set &=& where target_id=& "+nonAllDataClause+" and viewby=&";
        //Starting default parameters
        String initialParams = "insert into " + targetTable.trim() + "(TARGET_ID,T_DATE,T_MONTH,T_QTR,T_YEAR," + insertQ;
        String finalParameters = initialParams + "," + measureName.trim() + "," + " VIEWBY,FACT_ID) values(" + replaceVals + ")";

        for (int i = 0; i < pTable.getRowCount(); i++) {

            PbTargetValuesParam targetParam2 = (PbTargetValuesParam) pTable.getRow(i);
            long measureVal = targetParam2.getMeasureVal();
            String updateFlag = targetParam2.getUpdateFlag();
            String dat = targetParam2.getDateVal();
            String paramVal = targetParam2.getPrimViewByValue();
            HashMap allValsMap = new HashMap();
            String primparameterVal = targetParam2.getPrimAnalyzeVal();
            String primCol = "";

            if (elementIds.containsKey(primParameterElementId)) {
                primCol = elementIds.get(primParameterElementId).toString();
            }

            String key = paramVal + ":" + dat;
            float ratioN = 0;
            int oldValue = 0;
            if (ratioComb.containsKey(key)) {
                oldValue = ((Integer) ratioComb.get(key)).intValue();
                float divisionVal = (float) (measureVal - oldValue) / oldValue;
                ratioN = (float) 1 + divisionVal;
            }

            String[] a1 = (String[]) (nonAllIds.keySet()).toArray(new String[0]);
            for (int k = 0; k < a1.length; k++) {
                String eleId = a1[k];
                String pVal = nonAllIds.get(eleId).toString();
                if (elementIds.containsKey(eleId)) {
                    String colName = elementIds.get(eleId).toString();
                    if (!allValsMap.containsKey(colName)) {
                        allValsMap.put(colName, pVal);
                    }
                }
            }
            allValsMap.put("TARGET_ID", targetId);
            allValsMap.put(primCol, primparameterVal);
            // allValsMap.put(colName,paramVal);
            if (updateFlag.equalsIgnoreCase("N")) {
                allValsMap.put(viewByColName.trim(), paramVal);
                String tQuery = "";
                String t_year = "";//dat.substring(7);
                String t_Qtr = "";
                String t_mon = "";
                String datVal = "";
                if (periodType.equalsIgnoreCase("Day")) {
                    tQuery = "SELECT DISTINCT CQTR,CYEAR,to_char(ddate,'MON-yy') CMON ,to_char(ddate,'dd-MON-yy') dd  FROM pr_day_denom  WHERE to_char(DDATE,'mm/dd/yyyy')= '" + dat + "'";
                    PbReturnObject timeOb = execSelectSQL(tQuery);
                    t_year = timeOb.getFieldValueString(0, "CYEAR");
                    t_Qtr = timeOb.getFieldValueString(0, "CQTR") + "-" + timeOb.getFieldValueString(0, "CYEAR");
                    t_mon = timeOb.getFieldValueString(0, "CMON");
                    datVal = timeOb.getFieldValueString(0, "DD");
                } else if (periodType.equalsIgnoreCase("Month")) {
                    tQuery = "select distinct  CQTR,CYEAR,to_char(CM_ST_DATE,'MON-yy') CMON from pr_day_denom where to_date(CM_ST_DATE,'dd-mon-yy')=to_date('" + dat + "','mm-yy')";
                    PbReturnObject timeOb = execSelectSQL(tQuery);
                    t_year = timeOb.getFieldValueString(0, "CYEAR");
                    t_Qtr = timeOb.getFieldValueString(0, "CQTR") + "-" + timeOb.getFieldValueString(0, "CYEAR");
                    t_mon = timeOb.getFieldValueString(0, "CMON");
                } else if (periodType.equalsIgnoreCase("Quarter")) {
                    tQuery = "select distinct CQTR,CYEAR,CMONTH,to_date(CM_ST_DATE,'dd-mon-yy') DD from pr_day_denom where to_date(CM_ST_DATE,'dd-mon-yy')=to_date('" + dat + "','dd-mon-yy')";
                    t_Qtr = dat.substring(1);
                    t_year = dat.substring(3);
                } else if (periodType.equalsIgnoreCase("Year")) {
                    tQuery = "select distinct CQTR,CYEAR,CMONTH,to_date(CM_ST_DATE,'dd-mon-yy') DD from pr_day_denom where to_date(CM_ST_DATE,'dd-mon-yy')=to_date('" + dat + "','dd-mon-yy')";
                    t_year = dat;
                }

                int fact_id = getSequenceNumber(finalfactQuery);
//                allValsMap.put(measureName.trim().toUpperCase(), "" + measureVal + "");
                allValsMap.put(measureName.trim().toUpperCase(), String.valueOf(measureVal));
                allValsMap.put("TARGET_ID", String.valueOf(targetId));
//                allValsMap.put("TARGET_ID", "" + targetId + "");
                allValsMap.put("T_DATE", datVal);
                allValsMap.put("T_MONTH", t_mon);
                allValsMap.put("T_QTR", t_Qtr);
                allValsMap.put("T_YEAR", t_year);
//                allValsMap.put("FACT_ID", "" + fact_id + "");
                allValsMap.put("FACT_ID", String.valueOf(fact_id));
                allValsMap.put("VIEWBY", viewByName.trim());

                Object insertObjDy[] = new Object[allParamsValues.size()];
                for (int t = 0; t < allParamsValues.size(); t++) {
                    String colNames = (String) allParamsValues.get(t);
                    if (allValsMap.containsKey(colNames)) {
                        if ((String) allValsMap.get(colNames) == null) {
                            insertObjDy[t] = "All";
                        } else if (allValsMap.get(colNames) != null) {
                            insertObjDy[t] = (String) allValsMap.get(colNames);
                        } else {
                            insertObjDy[t] = "";
                        }
                    }
                }
                String finalInsertDyn = buildQuery(finalParameters, insertObjDy);
                insertQList.add(finalInsertDyn);
            } else if (updateFlag.equalsIgnoreCase("Y")) {
                String childDataQuery = "";
                String childElementId = "";
                String childEleName = "";
                ArrayList childDet = new ArrayList();
                String currentName = "";

                String parentName = "";
                if (memBersInfo.containsKey(viewByElementId)) {
                    ArrayList alDet = (ArrayList) memBersInfo.get(viewByElementId);
                    childElementId = alDet.get(2).toString();
                    currentName = alDet.get(0).toString();
                    if (memBersInfo.containsKey(childElementId)) {
                        childEleName = alDet.get(2).toString();
                        childDet = (ArrayList) memBersInfo.get(childElementId);
                        childEleName = childDet.get(0).toString();
                    }
                }
                HashMap updateVals = new HashMap();
                //"update "+targetTable.trim()+" set "+measureName.trim().toUpperCase()+"=& where target_id="+targetId+" "+nonAllDataClause;
                if (periodType.equalsIgnoreCase("Month")) {
                    updateQuery = "update & set &=& where target_id='&' " + nonAllDataClause + " and viewby='&' and t_month='&' and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";

                } else if (periodType.equalsIgnoreCase("Quarter")) {
                    updateQuery = "update & set &=& where target_id='&' " + nonAllDataClause + " and viewby='&' and t_qtr='&' and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";

                } else if (periodType.equalsIgnoreCase("Year")) {
                    updateQuery = "update & set &=& where target_id='&' " + nonAllDataClause + " and viewby='&' and t_year='&' and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";

                } else if (periodType.equalsIgnoreCase("Day")) {
                    updateQuery = "update & set &=& where target_id='&' " + nonAllDataClause + " and viewby='&' and t_date=to_date('&','mm-dd-yy') and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                }
                //memBersInfo
                ArrayList alreadyUpdateFact = new ArrayList();

                Object update[] = new Object[6];
                update[0] = targetTable.trim();
                update[1] = measureName.trim().toUpperCase();
                update[2] = new Long(measureVal);
                update[3] = targetId;
                update[4] = viewByName.trim();
                if (periodType.equalsIgnoreCase("Quarter")) {
                    update[5] = dat.substring(1);
                } else {
                    update[5] = dat;
                }
                String finupdateQuery = buildQuery(updateQuery, update);
                finupdateQuery = finupdateQuery + nonAllDataClause;
                updateVals.put(paramVal, new Long(measureVal));
                insertQList.add(finupdateQuery);
                // as long as child id is not -1 loop
                PbReturnObject childDataOb = new PbReturnObject();
                if (!childElementId.equalsIgnoreCase("-1")) {
                    if (periodType.equalsIgnoreCase("Month")) {
                        childDataQuery = "select " + elementIds.get(childElementId.trim()).toString() + "," + measureName.trim() + ",fact_id from " + targetTable.trim() + " where target_id=" + targetId + " and t_month='" + dat + "' and viewby='" + childEleName + "' and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                        // execSelectSQL(childDataQuery);
                    } else if (periodType.equalsIgnoreCase("Day")) {
                        childDataQuery = "select " + elementIds.get(childElementId.trim()).toString() + "," + measureName.trim() + ",fact_id from " + targetTable.trim() + " where target_id=" + targetId + " and t_date=to_date(to_char('" + dat + "'),'mm-dd-yy') and viewby='" + childEleName + "' and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                        // execSelectSQL(childDataQuery);
                    } else if (periodType.equalsIgnoreCase("Quarter")) {
                        childDataQuery = "select " + elementIds.get(childElementId.trim()).toString() + "," + measureName.trim() + ",fact_id from " + targetTable.trim() + " where target_id=" + targetId + " and t_qtr='" + dat.substring(1) + "' and viewby='" + childEleName + "' and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                        // execSelectSQL(childDataQuery);

                    } else if (periodType.equalsIgnoreCase("Year")) {
                        childDataQuery = "select " + elementIds.get(childElementId.trim()).toString() + "," + measureName.trim() + ",fact_id from " + targetTable.trim() + " where target_id=" + targetId + " and t_year='" + dat + "' and viewby='" + childEleName + "' and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                        // execSelectSQL(childDataQuery);
                    }
                    ps = con.prepareStatement(childDataQuery);
                    rsData = ps.executeQuery();
                    // childDataOb = execSelectSQL(childDataQuery);
                    childDataOb = new PbReturnObject(rsData);
                }
                int totalR = 0;
                int allTot = 0;
                for (int m = 0; m < childDataOb.getRowCount(); m++) {
                    totalR = totalR + childDataOb.getFieldValueInt(m, measureName.trim().toUpperCase());
                }
                //get the change in ratio
                float ratio = 0;
                float divisionVal = 0;
                // float ratioN=0;

                divisionVal = (float) (measureVal - totalR) / totalR;
                if (ratioN == 0) {
                    ratioN = 1 + divisionVal;
                }
                //  if(totalR==0)
                //     ratioN = 1;
                while (!childElementId.equalsIgnoreCase("-1")) {

                    if (periodType.equalsIgnoreCase("Month")) {
                        childDataQuery = "select " + elementIds.get(childElementId.trim()).toString() + "," + measureName.trim() + ",fact_id from " + targetTable.trim() + " where target_id=" + targetId + " and t_month='" + dat + "' and viewby='" + childEleName + "' and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                        //childDataOb=execSelectSQL(childDataQuery);
                        ps = con.prepareStatement(childDataQuery);
                        rsData = ps.executeQuery();
                        childDataOb = new PbReturnObject(rsData);
                    }
                    if (periodType.equalsIgnoreCase("Day")) {
                        childDataQuery = "select " + elementIds.get(childElementId.trim()).toString() + "," + measureName.trim() + ",fact_id from " + targetTable.trim() + " where target_id=" + targetId + " and t_date=to_date(to_char('" + dat + "'),'mm-dd-yy') and viewby='" + childEleName + "' and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                        // childDataOb=execSelectSQL(childDataQuery);
                        ps = con.prepareStatement(childDataQuery);
                        rsData = ps.executeQuery();
                        childDataOb = new PbReturnObject(rsData);
                    }
                    if (periodType.equalsIgnoreCase("Quarter")) {
                        childDataQuery = "select " + elementIds.get(childElementId.trim()).toString() + "," + measureName.trim() + ",fact_id from " + targetTable.trim() + " where target_id=" + targetId + " and t_qtr='" + dat.substring(1) + "' and viewby='" + childEleName + "' and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                        // childDataOb=execSelectSQL(childDataQuery);
                        ps = con.prepareStatement(childDataQuery);
                        rsData = ps.executeQuery();
                        childDataOb = new PbReturnObject(rsData);
                    }
                    if (periodType.equalsIgnoreCase("Year")) {
                        childDataQuery = "select " + elementIds.get(childElementId.trim()).toString() + "," + measureName.trim() + ",fact_id from " + targetTable.trim() + " where target_id=" + targetId + " and t_year='" + dat + "' and viewby='" + childEleName + "' and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                        // childDataOb=execSelectSQL(childDataQuery);
                        ps = con.prepareStatement(childDataQuery);
                        rsData = ps.executeQuery();
                        childDataOb = new PbReturnObject(rsData);
                    }

                    // for updation start
                    int chilCounts = childDataOb.getRowCount();
                    if (chilCounts == 0) {
                        chilCounts = 1;
                    }
                    long individualCount = 1;
                    individualCount = measureVal / chilCounts;
                    for (int m = 0; m < childDataOb.getRowCount(); m++) {
                        int newVal = 0;
                        if (totalR != 0) {
                            ratio = measureVal / totalR;
                            ratio = (float) childDataOb.getFieldValueInt(m, measureName.trim().toUpperCase()) / totalR;
                            newVal = (int) (ratio * measureVal);
                        }
                        int newM = (int) Math.floor(ratioN * childDataOb.getFieldValueInt(m, measureName.trim().toUpperCase()));//;Math.round(ratioN * chilNos.getFieldValueInt(m, measureName.trim().toUpperCase()));

                        allTot = allTot + newM;
                        String fact_id = childDataOb.getFieldValueString(m, "FACT_ID");
                        String updateT = "update " + targetTable.trim() + " set " + measureName.trim() + "=& where fact_id=&";
                        Object updateChilds[] = new Object[2];
                        updateChilds[0] = Integer.valueOf(newM);
                        updateChilds[1] = fact_id;
                        updateVals.put(childDataOb.getFieldValueString(m, 0), Integer.valueOf(newVal));//individualCount));
                        String finalupdateT = buildQuery(updateT, updateChilds);

                        if (!alreadyUpdateFact.contains(Integer.valueOf(childDataOb.getFieldValueInt(m, "FACT_ID")))) {
                            finalupdateT = buildQuery(updateT, updateChilds);
                            alreadyUpdateFact.add(Integer.valueOf(childDataOb.getFieldValueInt(m, "FACT_ID")));
                            updates.add(finalupdateT);
                        }

                    }
                    //for updation over
                    if (memBersInfo.containsKey(childElementId)) {
                        childDet = (ArrayList) memBersInfo.get(childElementId);
                        childElementId = childDet.get(2).toString();
                        childEleName = childDet.get(0).toString();
                        parentName = currentName;
                    }
                    //currentName=childEleName;
                }

            } else if (updateFlag.equalsIgnoreCase("D")) {
                String deleteQuery = "";
                if (periodType.equalsIgnoreCase("Month")) {
                    deleteQuery = "delete from " + targetTable.trim() + " where target_id=" + targetId + " and viewby='&' and t_month='&' " + nonAllDataClause + " and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                } else if (periodType.equalsIgnoreCase("Day")) {
                    deleteQuery = "delete from " + targetTable.trim() + " where target_id=" + targetId + " and viewby='&' and t_date=to_date('&','mm-dd-yy') " + nonAllDataClause + " and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                } else if (periodType.equalsIgnoreCase("Quarter")) {
                    deleteQuery = "delete from " + targetTable.trim() + " where target_id=" + targetId + " and viewby='&' and t_qtr='&' " + nonAllDataClause + " and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                } else if (periodType.equalsIgnoreCase("Year")) {
                    deleteQuery = "delete from " + targetTable.trim() + " where target_id=" + targetId + " and viewby='&' and t_year='&' " + nonAllDataClause + " and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                }

                Object delOb[] = new Object[2];
                delOb[0] = viewByName.trim();
                delOb[1] = dat;
                String finDeleteQ = buildQuery(deleteQuery, delOb);
                deleteQueries.add(finDeleteQ);
                String childDataQuery = "";
                String childElementId = "";
                String childEleName = "";
                ArrayList childDet = new ArrayList();
                String currentName = "";

                String parentName = "";
                if (memBersInfo.containsKey(viewByElementId)) {
                    ArrayList alDet = (ArrayList) memBersInfo.get(viewByElementId);
                    childElementId = alDet.get(2).toString();
                    currentName = alDet.get(0).toString();
                    if (memBersInfo.containsKey(childElementId)) {
                        childEleName = alDet.get(2).toString();
                        childDet = (ArrayList) memBersInfo.get(childElementId);
                        childEleName = childDet.get(0).toString();
                    }
                }
                PbReturnObject childDataOb = new PbReturnObject();
                while (!childElementId.equalsIgnoreCase("-1")) {
                    if (periodType.equalsIgnoreCase("Month")) {
                        childDataQuery = "select fact_id," + elementIds.get(childElementId.trim()).toString() + "," + measureName.trim() + ",fact_id from " + targetTable.trim() + " where target_id=" + targetId + " and t_month='" + dat + "' and viewby='" + childEleName + "' and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                        // childDataOb=execSelectSQL(childDataQuery);
                        ps = con.prepareStatement(childDataQuery);
                        rsData = ps.executeQuery();
                        childDataOb = new PbReturnObject(rsData);
                    }
                    if (periodType.equalsIgnoreCase("Day")) {
                        childDataQuery = "select fact_id," + elementIds.get(childElementId.trim()).toString() + "," + measureName.trim() + ",fact_id from " + targetTable.trim() + " where target_id=" + targetId + " and t_date=to_date(to_char('" + dat + "'),'mm-dd-yy') and viewby='" + childEleName + "' and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                        // childDataOb=execSelectSQL(childDataQuery);
                        ps = con.prepareStatement(childDataQuery);
                        rsData = ps.executeQuery();
                        childDataOb = new PbReturnObject(rsData);
                    }
                    if (periodType.equalsIgnoreCase("Quarter")) {
                        childDataQuery = "select fact_id," + elementIds.get(childElementId.trim()).toString() + "," + measureName.trim() + ",fact_id from " + targetTable.trim() + " where target_id=" + targetId + " and t_qtr='" + dat.substring(1) + "' and viewby='" + childEleName + "' and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                        //childDataOb=execSelectSQL(childDataQuery);
                        ps = con.prepareStatement(childDataQuery);
                        rsData = ps.executeQuery();
                        childDataOb = new PbReturnObject(rsData);
                    }
                    if (periodType.equalsIgnoreCase("Year")) {
                        childDataQuery = "select fact_id," + elementIds.get(childElementId.trim()).toString() + "," + measureName.trim() + ",fact_id from " + targetTable.trim() + " where target_id=" + targetId + " and t_year='" + dat + "' and viewby='" + childEleName + "' and " + elementIds.get(viewByElementId.trim()).toString() + "='" + paramVal + "'";
                        // childDataOb=execSelectSQL(childDataQuery);
                        ps = con.prepareStatement(childDataQuery);
                        rsData = ps.executeQuery();
                        childDataOb = new PbReturnObject(rsData);
                    }
                    if (memBersInfo.containsKey(childElementId)) {
                        childDet = (ArrayList) memBersInfo.get(childElementId);
                        childElementId = childDet.get(2).toString();
                        childEleName = childDet.get(0).toString();
                        parentName = currentName;
                    }
                    String delQ = "";
                    delQ = "delete from " + targetTable.trim() + " where fact_id=&";
                    for (int d = 0; d < childDataOb.getRowCount(); d++) {
                        String fact = childDataOb.getFieldValueString(d, "FACT_ID");
                        Object factOb[] = new Object[1];
                        factOb[0] = fact;
                        String findelQ = buildQuery(delQ, factOb);
                        deleteQueries.add(findelQ);
                    }
                }

            }

        }

        try {
            executeMultiple(deleteQueries, con);
            executeMultiple(insertQList, con);
            executeMultiple(updates, con);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        if (rsData != null) {
            rsData.close();
        }
        if (ps != null) {
            ps.close();
        }
        if (con != null) {
            con.close();
        }

    }
// to modify the data when any primary paramter is selected in the parimary view by and time is there in secondary

    public void saveForPrimaryParameter(Session sess) throws Exception {
        Connection con = ProgenConnection.getInstance().getConnection();
        ResultSet rsData;
        PreparedStatement ps;
        ps = null;
        rsData = null;
        PbTargetParamTable pTable = (PbTargetParamTable) sess.getObject("prg.targetparam.qdparams.PbTargetParamTable");
        PbTargetValuesParam targetParam = (PbTargetValuesParam) sess.getObject("prg.targetparam.qdparams.PbTargetValuesParam");
        String targetId = targetParam.getTargetId();
        String periodType = targetParam.getPeriodType();
        String minTimeLevel = targetParam.getMinimumTimeLevel();
        HashMap nonAllIds = targetParam.getNonAllIds();
        String viewBy = targetParam.getViewByParameter();
        String primParameterElementId = targetParam.getPrimaryParameter();
        HashMap ratioComb = targetParam.getRatioComb();
        ArrayList deleteQueries = new ArrayList();
        ArrayList updates = new ArrayList();
        String viewByElementId = "";
        String viewByColName = "";
        String nonAllDataClause = "";
        ArrayList updatList = new ArrayList();
        ArrayList deleteList = new ArrayList();
        Object relSeqObj[] = new Object[1];
        relSeqObj[0] = "PRG_TARGET_DATA_SEQ";
        String factQuery = resBundle.getString("getSequenceNumber");
        ArrayList delList = new ArrayList();
        String finalfactQuery = buildQuery(factQuery, relSeqObj);

        ArrayList insertQList = new ArrayList();
        Object tarOb[] = new Object[1];
        tarOb[0] = targetId;
        String getTargetTable = resBundle.getString("getTargetTable");
        String fingetTargetTable = buildQuery(getTargetTable, tarOb);
        PbReturnObject targetTabOb = execSelectSQL(fingetTargetTable);
        String targetTable = targetTabOb.getFieldValueString(0, "BUSS_TABLE_NAME");
        String measureName2 = targetTabOb.getFieldValueString(0, "MEASURE_NAME");
        String measureName = measureName2.trim().replace(" ", "_");

        String getElementIdsBussCols = resBundle.getString("getElementIdsBussCols");
        String fingetElementIdsBussCols = buildQuery(getElementIdsBussCols, tarOb);
        PbReturnObject bussColOb = execSelectSQL(fingetElementIdsBussCols);
        HashMap elementIds = new HashMap();
        String viewByName = "";
        bussColOb.writeString();
        for (int p = 0; p < bussColOb.getRowCount(); p++) {
            elementIds.put(bussColOb.getFieldValueString(p, "ELEMENT_ID"), bussColOb.getFieldValueString(p, "BUSS_COL_NAME"));
            //if(viewBy.equalsIgnoreCase(bussColOb.getFieldValueString(p,"PARAM_DISP_NAME"))){
            if (viewBy.equalsIgnoreCase(bussColOb.getFieldValueString(p, "ELEMENT_ID"))) {
                viewByElementId = bussColOb.getFieldValueString(p, "ELEMENT_ID");
                viewByColName = bussColOb.getFieldValueString(p, "BUSS_COL_NAME");
                viewByName = bussColOb.getFieldValueString(p, "PARAM_DISP_NAME");
            }
        }

        String nonAll[] = (String[]) (nonAllIds.keySet()).toArray(new String[0]);
        HashMap nonAllNew = new HashMap();
        for (int y = 0; y < nonAll.length; y++) {
            String eleId = nonAll[y];
            String value = nonAllIds.get(eleId).toString();
            if (!value.equalsIgnoreCase("-1") && !value.equalsIgnoreCase("All")) {
                nonAllNew.put(eleId, value);
            }
        }
        String nonAllStr[] = (String[]) (nonAllNew.keySet()).toArray(new String[0]);
        for (int y = 0; y < nonAllStr.length; y++) {
            String eleId = nonAllStr[y];
            String colName = elementIds.get(eleId).toString();
            String value = nonAllIds.get(eleId).toString();
            if (y == 0) {
                nonAllDataClause = " and " + colName + "='" + value + "'";
            } else if (y == 1) {
                nonAllDataClause = nonAllDataClause + "  " + colName + "='" + value + "'";
            } else {
                nonAllDataClause = nonAllDataClause + " and " + colName + "='" + value + "'";
            }
        }

        // to get the dimensions.Its members  and all info
        String getTargetParamsInfo = resBundle.getString("getTargetParamsInfo");
        String fingetTargetParamsInfo = buildQuery(getTargetParamsInfo, tarOb);
        PbReturnObject paramInfoOb = execSelectSQL(fingetTargetParamsInfo);
        HashMap dimInfo = new HashMap();
        HashMap memBersInfo = new HashMap();
        HashMap allElements = new HashMap();

        for (int p = 0; p < paramInfoOb.getRowCount(); p++) {
            String dimId = paramInfoOb.getFieldValueString(p, "DIM_ID");
            String bussColName = "";
            if (elementIds.containsKey(paramInfoOb.getFieldValueString(p, "ELEMENT_ID"))) {
                bussColName = elementIds.get(paramInfoOb.getFieldValueString(p, "ELEMENT_ID")).toString();
            }
            ArrayList al = new ArrayList();
            al.add(0, paramInfoOb.getFieldValueString(p, "PARAM_DISP_NAME")); //name
            al.add(1, bussColName); //buss COl Name
            al.add(2, paramInfoOb.getFieldValueString(p, "CHILD_ELEMENT_ID")); // child id
            al.add(3, paramInfoOb.getFieldValueString(p, "REL_LEVEL")); //level
            memBersInfo.put(paramInfoOb.getFieldValueString(p, "ELEMENT_ID"), al);
        }

        String getOtherDimElements = resBundle.getString("getOtherDimElements");
        Object tarObj[] = new Object[2];
        tarObj[0] = targetId;
        tarObj[1] = targetId;
        String fingetOtherDimElements = buildQuery(getOtherDimElements, tarObj);
        HashMap otherDimMap = new HashMap();
        HashMap otherDimElements = new HashMap();

        PbReturnObject otherDimOb = execSelectSQL(fingetOtherDimElements);
        for (int p = 0; p < otherDimOb.getRowCount(); p++) {
            ArrayList det = new ArrayList();
            String dimId = otherDimOb.getFieldValueString(p, "DIM_ID");
            if (otherDimMap.containsKey(dimId)) {
                det = (ArrayList) otherDimMap.get(dimId);
            }
            det.add(otherDimOb.getFieldValueString(p, "ELEMENT_ID"));
            otherDimMap.put(dimId, det);

        }
        String[] otherDims = (String[]) (otherDimMap.keySet()).toArray(new String[0]);

        for (int i = 0; i < pTable.getRowCount(); i++) {
            PbTargetValuesParam targetParam2 = (PbTargetValuesParam) pTable.getRow(i);
            long measureVal = targetParam2.getMeasureVal();
            String updateFlag = targetParam2.getUpdateFlag();
            String dat = targetParam2.getDateVal();
            String paramVal = targetParam2.getPrimViewByValue();
            HashMap allValsMap = new HashMap();
            // String primparameterVal = targetParam2.getPrimAnalyzeVal();
            String primCol = "";
            String key = paramVal + ":" + dat;

            float ratioN = 0;
            boolean flagUpdateR = false;
            int oldValue = 0;
            if (ratioComb.containsKey(key)) {
                oldValue = ((Integer) ratioComb.get(key)).intValue();
                float divisionVal = (float) (measureVal - oldValue) / oldValue;
                ratioN = (float) 1 + divisionVal;
            }
            // loop throuht the other dimesnsions
            for (int m = 0; m < otherDims.length; m++) {
                ArrayList dimDetails = (ArrayList) otherDimMap.get(otherDims[m]);
                // loop through the memnbers of the dimension 
                for (int n = 0; n < dimDetails.size(); n++) {
                    String elementId = dimDetails.get(n).toString();
                    String pTotalQty = "";
                    String elementName = "";
                    String elementCol = "";
                    if (memBersInfo.containsKey(elementId)) {
                        ArrayList elementDetails = (ArrayList) memBersInfo.get(elementId);
                        elementName = elementDetails.get(0).toString();
                        elementCol = elementDetails.get(1).toString();
                    }
                    if (minTimeLevel.equalsIgnoreCase("Month")) {
                        pTotalQty = "select " + measureName.trim() + ", fact_id from " + targetTable.trim() + " where viewby='" + elementName.trim() + "' and t_month='" + dat + "' and target_id='" + targetId + "' and " + viewByColName + "='" + paramVal + "'";
                    } else if (minTimeLevel.equalsIgnoreCase("Day")) {
                        pTotalQty = "select " + measureName.trim() + ", fact_id from " + targetTable.trim() + " where viewby='" + elementName.trim() + "' and t_date=to_date('" + dat + "','dd-mm-yy') and target_id='" + targetId + "' and " + viewByColName + "='" + paramVal + "'";
                    } else if (minTimeLevel.equalsIgnoreCase("Quarter")) {
                        pTotalQty = "select " + measureName.trim() + ", fact_id from " + targetTable.trim() + " where viewby='" + elementName.trim() + "' and t_qtr='" + dat + "' and target_id='" + targetId + "' and " + viewByColName + "='" + paramVal + "'";
                    } else if (minTimeLevel.equalsIgnoreCase("Year")) {
                        pTotalQty = "select " + measureName.trim() + ", fact_id from " + targetTable.trim() + " where viewby='" + elementName.trim() + "' and t_year='" + dat + "' and target_id='" + targetId + "' and " + viewByColName + "='" + paramVal + "'";
                    }

                    int newVal = 0;
                    String updateQ = "";
                    //PbReturnObject updatObj=execSelectSQL(pTotalQty);
                    ps = con.prepareStatement(pTotalQty);
                    rsData = ps.executeQuery();
                    PbReturnObject updatObj = new PbReturnObject(rsData);

                    if (updateFlag.equalsIgnoreCase("Y")) {
                        updateQ = "update " + targetTable.trim() + " set " + measureName.trim() + "=& where fact_id=&";

                        for (int k = 0; k < updatObj.getRowCount(); k++) {
                            int newM = Math.round(ratioN * updatObj.getFieldValueInt(k, measureName.trim().toUpperCase()));
                            Object updatOb[] = new Object[2];
                            String fact_id = updatObj.getFieldValueString(k, "FACT_ID");
                            updatOb[0] = Integer.valueOf(newM);
                            updatOb[1] = fact_id;
                            String finupdateQ = buildQuery(updateQ, updatOb);
                            updatList.add(finupdateQ);
                        }
                    } else if (updateFlag.equalsIgnoreCase("D")) {
                        String deletq = "delete from " + targetTable.trim() + " where fact_id=&";
                        for (int k = 0; k < updatObj.getRowCount(); k++) {
                            Object deleteOb[] = new Object[1];
                            String fact_id = updatObj.getFieldValueString(k, "FACT_ID");
                            deleteOb[0] = fact_id;
                            String findeletq = buildQuery(deletq, deleteOb);
                            deleteList.add(findeletq);
                        }
                    }
                }
            }
        }

        try {
            executeMultiple(deleteList, con);
            executeMultiple(updatList, con);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        if (rsData != null) {
            rsData.close();
        }
        if (ps != null) {
            ps.close();
        }
        if (con != null) {
            con.close();
        }

    }

    public PbReturnObject getTargetDetails(Session sess) throws Exception {
        PbTargetValuesParam targetParam = (PbTargetValuesParam) sess.getObject("prg.targetparam.qdparams.PbTargetValuesParam");
        String targetId = targetParam.getTargetId();
        PbReturnObject all = new PbReturnObject();
        PbReturnObject TargetDet = new PbReturnObject();
        PbReturnObject TargetCols = new PbReturnObject();
        String getTargetTable = resBundle.getString("getTargetTable");
        Object tarOb[] = new Object[1];
        tarOb[0] = targetId;
        String fingetTargetTable = buildQuery(getTargetTable, tarOb);
        TargetDet = execSelectSQL(fingetTargetTable);
        String getElementIdsBussCols = resBundle.getString("getElementIdsBussCols");
        String fingetElementIdsBussCols = buildQuery(getElementIdsBussCols, tarOb);
        TargetCols = execSelectSQL(fingetElementIdsBussCols);

        all.setObject("TargetCols", TargetCols);
        all.setObject("TargetDet", TargetDet);
        return all;
    }

    public PbReturnObject getTimeQueryForAllocation(String minLimeLevel, String periodType, String startRange, String endRange) throws Exception {
        PbReturnObject defDates = new PbReturnObject();
        String tQuery = "";
        String dat1 = startRange;
        String dat2 = endRange;

        if (periodType.equalsIgnoreCase("Year")) {
            if (minLimeLevel.equalsIgnoreCase("Month")) {
                tQuery = "select distinct mon_name,cyear from PRG_ACN_MON_DENOM where CYEAR between '" + dat1 + "' and '" + dat2 + "'";
            } else if (minLimeLevel.equalsIgnoreCase("Quarter")) {
                tQuery = "select distinct PMQTR,cyear  from PRG_ACN_MON_DENOM where CYEAR between '" + dat1 + "' and '" + dat2 + "' order by 2,1";
            } else if (minLimeLevel.equalsIgnoreCase("Day")) {
                tQuery = "select distinct to_char(ddate,'dd-MON-yy'),cyear from pr_day_denom where cyear BETWEEN '" + dat1 + "' and '" + dat2 + "' order by 1";
            }

        } else if (periodType.equalsIgnoreCase("Month")) {
            if (minLimeLevel.equalsIgnoreCase("Day")) {
                tQuery = "select distinct to_char(ddate,'dd-MON-yy'),to_char(ddate,'MON-yy'),ddate from pr_day_denom where to_char(ddate,'MON-yy') in('" + dat1 + "','" + dat2 + "') order by 3";
            }

            // tQuery = "select distinct ddate,to_char(ddate,'MON-yy') from pr_day_denom where to_char(ddate,'MON-yy') BETWEEN '"+dat1+"' and '"+dat1+"' order by 1";
        } else if (periodType.equalsIgnoreCase("Quarter")) {
            String stYear = "";
            String endYear = "";
            String stQtr = "";
            String endQtr = "";
            stQtr = dat1.substring(0, 1);
            endQtr = dat2.substring(0, 1);
            stYear = dat1.substring(2);
            endYear = dat2.substring(2);

            if (minLimeLevel.equalsIgnoreCase("Day")) {
                tQuery = "select distinct to_char(ddate,'dd-MON-yy'),cqtr,cyear from pr_day_denom where cqtr BETWEEN '" + stQtr + "' and '" + endQtr + "' and cyear between '" + stYear + "' and '" + endYear + "' order by 1";
            } else if (minLimeLevel.equalsIgnoreCase("Month")) {
                //tQuery = "select distinct to_char(ddate,'MON-yy'),cqtr,cyear from pr_day_denom where cqtr BETWEEN '"+stQtr+"' and '"+endQtr+"' and cyear between '"+stYear+"' and '"+endYear+"' order by 3,1,2";
                tQuery = "select distinct to_char(ddate,'MON-yy'),cqtr,cyear, cmonth from pr_day_denom where cqtr between " + stQtr + " and " + endQtr + " and cyear between '" + stYear + "' and '" + endYear + "' order by 3,2,4";
                //(Integer.parseInt(stQtr)>Integer.parseInt(endQtr))&&
                if ((Integer.parseInt(stYear) < Integer.parseInt(endYear))) {
                    tQuery = "select distinct to_char(ddate,'MON-yy'),cqtr,cyear,cmonth from pr_day_denom where cqtr>=" + stQtr + " and cyear=" + stYear + " or cqtr<=" + endQtr + " and cyear=" + endYear + " order by 3,2,4";
                }
            }
        }
        defDates = execSelectSQL(tQuery);
        return defDates;
    }

    public PbReturnObject getExcelFileDetail(String fileName) throws Exception {
        PbReturnObject all = new PbReturnObject();
        String query = "select * from TARGET_EXCELMASTER where filename='" + fileName + "'";
        all = execSelectSQL(query);
        return all;
    }

    public void processQueryDetails(Element root) {
//        ArrayList scenarioQryElementIds = new ArrayList();
//        ArrayList scenarioQryAggregations = new ArrayList();
//        ArrayList scenarioQryColNames = new ArrayList();

        List row = root.getChildren("ViewDetail");//Only one row as of now

        /*
         * Start of Processing of parameters
         */
        for (int i = 0; i
                < row.size(); i++) {//Loop for One section under portlet
            Element Companyname = (Element) row.get(i);

            List paramRow = Companyname.getChildren("secViewVal");
            for (int j = 0; j
                    < paramRow.size(); j++) {

                Element paramElement = (Element) paramRow.get(j);

                // scenarioQryElementIds.add(xmUtil.getXmlTagValue(paramElement, "elementId"));
                //  scenarioQryAggregations.add(xmUtil.getXmlTagValue(paramElement, "elementAgg"));
                // scenarioQryColNames.add(xmUtil.getXmlTagValue(paramElement, "elementName"));
            }

        }//End Loop for One section under portlet

        //details for graph
    }

    public String getUploadStatus(String excelName) throws Exception {
        String status = "";
        String Query = "  select * from excelSheet_uplaod where excel_sheet_name='" + excelName + "'";
        PbReturnObject pbro = execSelectSQL(Query);
        if (pbro.getRowCount() > 0) {
            status = "uploaded";
        }
        return status;
    }
}
