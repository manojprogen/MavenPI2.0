package com.progen.scenariodesigner.db;

import com.progen.scenario.display.DisplayScenarioParameters;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.log4j.Logger;
import prg.db.PbDb;
import prg.db.PbReturnObject;
import prg.db.Session;

public class ScenarioTemplateDAO extends PbDb {

    public static Logger logger = Logger.getLogger(ScenarioTemplateDAO.class);
    ScenarioTemplateResourceBundle resBundle = new ScenarioTemplateResourceBundle();

    public String getScenarioDims(String foldersIds) {
        PbReturnObject retObj = null;
        String finalQuery = null;
        String[] colNames = null;
        Object obj[] = new Object[1];
        if (foldersIds != null && !"".equalsIgnoreCase(foldersIds)) {
            obj[0] = foldersIds;
        } else {
            obj[0] = "''";
        }
        String dimId = "";
        String dimName = "";
        String subFolderId = "";
        String sql = resBundle.getString("getScenarioDims");

        StringBuffer outerBuffer = new StringBuffer("");
        try {
            finalQuery = buildQuery(sql, obj);
            //////////////////////////////////////////.println.println(" finalQuery - " + finalQuery);
            retObj = execSelectSQL(finalQuery);
            colNames = retObj.getColumnNames();

            String minTimeLevel = "3";
            if (minTimeLevel.equals("5")) {
                outerBuffer.append("<li class='closed' id='timeDimension-Period Basis'>");
                outerBuffer.append("<img src='icons pinvoke/Timesetup.gif'></img>");
                outerBuffer.append("<span id='elmnt-Time Dimension-Period Basis' style='font-family:verdana;font-size:8pt'>Time-Period Basis</span>");
                outerBuffer.append("</li>");
                outerBuffer.append("<li class='closed' id='timeDimension-Range Basis'>");
                outerBuffer.append("<img src='icons pinvoke/Timesetup.gif'></img>");
                outerBuffer.append("<span id='elmnt-Time Dimension-Range Basis' style='font-family:verdana;font-size:8pt'>Time-Range Basis</span>");
                outerBuffer.append("</li>");
            } else if (minTimeLevel.equals("4")) {
                outerBuffer.append("<li class='closed' id='timeDimension-Week Basis'>");
                outerBuffer.append("<img src='icons pinvoke/Timesetup.gif'></img>");
                outerBuffer.append("<span id='elmnt-Time Dimension-Week Basis' style='font-family:verdana;font-size:8pt'>Time-Week Range Basis</span>");
                outerBuffer.append("</li>");
            } else if (minTimeLevel.equals("3")) {
                outerBuffer.append("<li class='closed' id='timeDimension-Month Basis'>");
                outerBuffer.append("<img src='icons pinvoke/Timesetup.gif'></img>");
                outerBuffer.append("<span id='elmnt-Time Dimension-Month Basis' style='font-family:verdana;font-size:8pt'>Time-Month Range Basis</span>");
                outerBuffer.append("</li>");
                outerBuffer.append("<li class='closed' id='timeDimension-Year Basis'>");
                outerBuffer.append("<img src='icons pinvoke/Timesetup.gif'></img>");
                outerBuffer.append("<span id='elmnt-Time Dimension-Year Basis' style='font-family:verdana;font-size:8pt'>Time-Year Range Basis</span>");
                outerBuffer.append("</li>");
            } else if (minTimeLevel.equals("2")) {
                outerBuffer.append("<li class='closed' id='timeDimension-Quarter Basis'>");
                outerBuffer.append("<img src='icons pinvoke/Timesetup.gif'></img>");
                outerBuffer.append("<span id='elmnt-Time Dimension-Quarter Basis' style='font-family:verdana;font-size:8pt'>Time-Quarter Range Basis</span>");
                outerBuffer.append("</li>");
            } else if (minTimeLevel.equals("1")) {
                outerBuffer.append("<li class='closed' id='timeDimension-Year Basis'>");
                outerBuffer.append("<img src='icons pinvoke/Timesetup.gif'></img>");
                outerBuffer.append("<span id='elmnt-Time Dimension-Year Basis' style='font-family:verdana;font-size:8pt'>Time-Year Range Basis</span>");
                outerBuffer.append("</li>");
            }

            for (int i = 0; i < retObj.getRowCount(); i++) {
                dimId = retObj.getFieldValueString(i, colNames[0]);
                dimName = retObj.getFieldValueString(i, colNames[1]);
                subFolderId = retObj.getFieldValueString(i, colNames[2]);

                outerBuffer.append("<li class='closed' id='" + dimId + "'>");
                outerBuffer.append("<img src='icons pinvoke/Dimension.png'></img>");
                outerBuffer.append("<span style='font-family:verdana;font-size:8pt'>" + dimName + "</span>");
                outerBuffer.append("<ul id='dimName-" + dimName + "'>");

                outerBuffer.append(getScenarioDimsMbrs(subFolderId, dimId));

                outerBuffer.append("</ul>");
                outerBuffer.append("</li>");
            }
        } catch (Exception exception) {
            logger.error("Exception:", exception);
        }
        return outerBuffer.toString();
    }

    public String getScenarioDimsMbrs(String subFolderId, String dimId) {
        PbReturnObject retObj = null;
        String finalQuery = null;
        String[] colNames = null;
        Object obj[] = new Object[2];
        obj[0] = subFolderId;
        obj[1] = dimId;
        String MbrId = "";
        String MbrName = "";
        String elementid = "";
        String sql = resBundle.getString("getScenarioDimsMbrs");
        StringBuffer outerBuffer = new StringBuffer("");
        try {

            finalQuery = buildQuery(sql, obj);
            retObj = execSelectSQL(finalQuery);
            colNames = retObj.getColumnNames();
            for (int i = 0; i < retObj.getRowCount(); i++) {
                MbrId = retObj.getFieldValueString(i, colNames[2]);
                MbrName = retObj.getFieldValueString(i, colNames[1]);
                elementid = retObj.getFieldValueString(i, colNames[0]);
                outerBuffer.append("<li class='closed' id='" + MbrId + "'>");
                outerBuffer.append("<img src='icons pinvoke/hirarechy.png'></img>");
                outerBuffer.append("<span id='elmnt-" + elementid + "' style='font-family:verdana;font-size:8pt'>" + MbrName + "</span>");

                outerBuffer.append("</li>");
            }
        } catch (Exception exception) {
            logger.error("Exception:", exception);
        }
        return outerBuffer.toString();
    }

    public String checkScenarioName(String scenarioName) throws Exception {
        String status = "";
        String sql = resBundle.getString("getAllScenarioNames");
        //////////////////////////////////////////.println.println(" sql " + sql);
        PbReturnObject pbro = execSelectSQL(sql);
        String localSName = "";
        for (int p = 0; p < pbro.getRowCount(); p++) {
            localSName = pbro.getFieldValueString(p, "SCENARIO_NAME");
            if (localSName.equalsIgnoreCase(scenarioName)) {
                status = "exists";
            } else {
                status = "";
            }
            if (status.equalsIgnoreCase("exists")) {
                break;
            }
        }
        return status;
    }

    public String getUserFoldersByUserId(String userId) {
        PbReturnObject retObj = null;
        String finalQuery = null;
        String[] colNames = null;
        Object obj[] = new Object[1];
        obj[0] = userId;


        String FolderId = "";
        String FolderName = "";
        String getUserFoldersByUserIdQuery = resBundle.getString("getUserFoldersByUserId");

        StringBuffer outerBuffer = new StringBuffer("");

        try {

            finalQuery = buildQuery(getUserFoldersByUserIdQuery, obj);
            //////////////////////////////////////////.println.println(" finalQuery user folr " + finalQuery);
            retObj = execSelectSQL(finalQuery);
            colNames = retObj.getColumnNames();
            for (int i = 0; i < retObj.getRowCount(); i++) {
                FolderId = retObj.getFieldValueString(i, colNames[0]);
                FolderName = retObj.getFieldValueString(i, colNames[1]);
                outerBuffer.append("<li class='closed' id='" + FolderId + "'>");
                outerBuffer.append("<input type='checkbox' name='userfldsList' id='" + FolderId + "' onclick='javascript:getUserDims()' ><span><font size='1px' face='verdana'><b>" + FolderName + "</b></font></span>");
                outerBuffer.append("</li>");
            }
        } catch (Exception exception) {
            logger.error("Exception:", exception);
        }
        return outerBuffer.toString();
    }

    public String getUserFolderMinTimeLevel(String foldersIds) throws Exception {

        String minTimeLevel = "3";

        /*
         * String qry = resBundle.getString("getUserFolderMinTimeLevel");
         *
         *
         * Object obj[] = new Object[1]; if (foldersIds != null &&
         * !"".equalsIgnoreCase(foldersIds)) { obj[0] = foldersIds; } else {
         * obj[0] = "''"; }
         *
         * String finalQry = buildQuery(qry, obj);
         * //////////////////////////////////////////.println.println(" finalQry
         * time "+finalQry);
         *
         * PbReturnObject pbro = execSelectSQL(finalQry);
         *
         * minTimeLevel = String.valueOf(pbro.getFieldValueInt(0, "MIN_LEVEL"));
         */



        return minTimeLevel;
    }

    public String getMeasures(String foldersIds) {
        PbReturnObject retObj = null;
        String finalQuery = null;
        String[] colNames = null;
        Object obj[] = new Object[1];
        obj[0] = foldersIds;
        String factName = "";
        String subFolderTabid = "";
        String userFolderIds = foldersIds;
        String sql = resBundle.getString("getFacts");
        StringBuffer outerBuffer = new StringBuffer("");
        try {
            finalQuery = buildQuery(sql, obj);
            //////////////////////////////////////////.println.println(" finalQuery meas " + finalQuery);
            retObj = execSelectSQL(finalQuery);
            colNames = retObj.getColumnNames();
            for (int i = 0; i < retObj.getRowCount(); i++) {
                factName = retObj.getFieldValueString(i, colNames[1]);
                subFolderTabid = retObj.getFieldValueString(i, colNames[0]);
                outerBuffer.append("<li class='closed' id='" + factName + i + "'>");
                outerBuffer.append("<img src='icons pinvoke/table.png'></img>");
                outerBuffer.append("<span style='font-family:verdana;font-size:8pt'>" + factName + "</span>");
                outerBuffer.append("<ul id='factName-" + factName + "'>");

                outerBuffer.append(getMeasureElements(userFolderIds, subFolderTabid));

                outerBuffer.append("</ul>");
                outerBuffer.append("</li>");
            }
        } catch (Exception exception) {
            logger.error("Exception:", exception);
        }
        return outerBuffer.toString();
    }

    public String getMeasureElements(String userFolderIds, String subFolderTabid) {
        PbReturnObject retObj = null;
        String finalQuery = null;
        String[] colNames = null;
        Object obj[] = new Object[2];
        obj[0] = userFolderIds;
        obj[1] = subFolderTabid;
        String ElementId = "";
        String ElementName = "";
        String colId = "";
        String sql = resBundle.getString("getFactElements");
        StringBuffer outerBuffer = new StringBuffer("");
        try {
            finalQuery = buildQuery(sql, obj);
            //////////////////////////////////////////.println.println(" finalQuery ggg  " + finalQuery);

            retObj = execSelectSQL(finalQuery);
            colNames = retObj.getColumnNames();
            for (int i = 0; i < retObj.getRowCount(); i++) {
                ElementId = retObj.getFieldValueString(i, colNames[0]);
                ElementName = retObj.getFieldValueString(i, colNames[1]);
                //colId = retObj.getFieldValueString(i, colNames[2]);

                outerBuffer.append("<li class='closed' id='" + ElementId + "'>");
                outerBuffer.append("<img src='icons pinvoke/report.png'></img>");
                outerBuffer.append("<span id='" + ElementId + "' style='font-family:verdana;font-size:8pt'>" + ElementName + "</span>");
                outerBuffer.append("</li>");
            }
        } catch (Exception exception) {
            logger.error("Exception:", exception);
        }
        return outerBuffer.toString();
    }

    public PbReturnObject getAllMonths() throws Exception {
        PbReturnObject pbro = null;
        String query = resBundle.getString("getAllMonths");
        //////////////////////////////////////////.println.println("getAllMonths query iss:: " + query);

        try {
            pbro = execSelectSQL(query);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }

        return pbro;
    }

    public PbReturnObject getAllYears() throws Exception {
        PbReturnObject pbro = null;
        String query = resBundle.getString("getAllYears");
        //////////////////////////////////////////.println.println("getAllMonths query iss:: " + query);

        try {
            pbro = execSelectSQL(query);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }

        return pbro;
    }

    public PbReturnObject getSeededModels() throws Exception {
        PbReturnObject pbro = null;
        String query = resBundle.getString("getAllSeededModels");
        //////////////////////////////////////////.println.println("getSeededModels query iss:: " + query);

        try {
            pbro = execSelectSQL(query);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }

        return pbro;
    }

    public void addScenarioMaster(Session sess) throws Exception {
        ScenarioParam scnParam = (ScenarioParam) sess.getObject("com.progen.scenariodesigner.db.ScenarioParam");
        ArrayList al = new ArrayList();
        String insertScenarioMaster = resBundle.getString("insertScenarioMaster");
        Object insertObj[] = new Object[15];

        insertObj[0] = scnParam.getQueryElementId();
        insertObj[1] = scnParam.getMeasureName();
        insertObj[2] = scnParam.getScenarioId();
        insertObj[3] = scnParam.getScenarioName();
        insertObj[4] = scnParam.getScenarioDesc();
        insertObj[5] = scnParam.getScenarioMinTimeLevel();
        // //////.println("scnParam.getScenarioMinTimeLevel()--"+scnParam.getScenarioMinTimeLevel());
        if (scnParam.getScenarioMinTimeLevel().equalsIgnoreCase("Month")) {
            insertObj[6] = scnParam.getScenarioStartTime();
            insertObj[7] = scnParam.getScenarioEndTime();
            insertObj[8] = "";
            insertObj[9] = "";
        } else if (scnParam.getScenarioMinTimeLevel().equalsIgnoreCase("Day")) {
            insertObj[6] = scnParam.getScenarioStartTime();
            insertObj[7] = scnParam.getScenarioEndTime();
            insertObj[8] = "";
            insertObj[9] = "";
        }

        insertObj[10] = scnParam.getUserId();
        insertObj[11] = scnParam.getScenarioHistStartTime();
        insertObj[12] = scnParam.getScenarioHistEndTime();
        insertObj[13] = scnParam.getScnStatus();
        insertObj[14] = scnParam.getBussRole();
        String fininsertScenarioMaster = buildQuery(insertScenarioMaster, insertObj);
        // //////.println("fininsertScenarioMaster---"+fininsertScenarioMaster);
        //////////////////////////////////////////.println.println(" fininsertScenarioMaster " + fininsertScenarioMaster);
        al.add(fininsertScenarioMaster);
        try {
            executeMultiple(al);
        } catch (Exception e) {
            logger.error("Exception:", e);
        }
    }

    public PbReturnObject getEditScenario(String ScId) throws Exception {
        String getEditScenarioquery = resBundle.getString("getEditScenario");
        //////////////////////////////////////////.println.println("scid in dao ----" + ScId);
        Object[] Obj = new Object[1];
        Obj[0] = ScId;
        String finalQuery = "";
        PbReturnObject pbro = null;

        try {
            finalQuery = buildQuery(getEditScenarioquery, Obj);
            //////////////////////////////////////////.println.println("final Query ==================" + finalQuery);
            pbro = execSelectSQL(finalQuery);

        } catch (Exception e) {
            logger.error("Exception:", e);
        }
        return pbro;

    }

    public void upDateScenario(String[] userList) {
        ArrayList alist = new ArrayList();
        String upDateScenarioQuery = resBundle.getString("upDateScenario");

        String finalQuery = "";
        try {
            finalQuery = buildQuery(upDateScenarioQuery, userList);
            //////////////////////////////////////////.println.println(" finalQuery ================" + finalQuery);
            execModifySQL(finalQuery);
        } catch (Exception exception) {
            logger.error("Exception:", exception);

        }
    }

    public void deleteScenario(String scenarioIds, String dimNames, String modelNames) {

        ArrayList alist = new ArrayList();

        String deleteScenarioMasterQuery = resBundle.getString("deleteScenarioMaster");
        String deleteScenarioModelMasterQuery = resBundle.getString("deleteScenarioModelMaster");
        String deleteCustomModelMaster = resBundle.getString("deleteCustomModelMaster");
        String deleteCustomModelDetails = resBundle.getString("deleteCustomModelDetails");

        String finalQuery = "";
        ArrayList queries = new ArrayList();

        try {

            Object[] Obj = new Object[1];
            Obj[0] = scenarioIds;
            finalQuery = buildQuery(deleteScenarioMasterQuery, Obj);
            //////////////////////////////////////////.println.println("finalQuery is:: "+finalQuery);
            queries.add(finalQuery);

            String[] modelNamesArray = modelNames.split(",");
            String modelNamesStrWithQuotes = "";
            for (int i = 0; i < modelNamesArray.length; i++) {
                if (modelNamesStrWithQuotes.equalsIgnoreCase("")) {
                    modelNamesStrWithQuotes = "'" + modelNamesArray[i] + "'";
                } else {
                    modelNamesStrWithQuotes = modelNamesStrWithQuotes + "," + "'" + modelNamesArray[i] + "'";
                }
            }

            Object[] Obj1 = new Object[2];
            Obj1[0] = scenarioIds;
            Obj1[1] = modelNamesStrWithQuotes;
            finalQuery = buildQuery(deleteScenarioModelMasterQuery, Obj1);
            //////////////////////////////////////////.println.println("finalQuery is:: "+finalQuery);
            queries.add(finalQuery);

            Object[] Obj2 = new Object[2];
            Obj2[0] = scenarioIds;
            Obj2[1] = modelNamesStrWithQuotes;
            finalQuery = buildQuery(deleteCustomModelMaster, Obj2);
            //////////////////////////////////////////.println.println("finalQuery is:: "+finalQuery);
            queries.add(finalQuery);

            Object[] Obj3 = new Object[2];
            Obj3[0] = scenarioIds;
            Obj3[1] = modelNamesStrWithQuotes;
            finalQuery = buildQuery(deleteCustomModelDetails, Obj3);
            //////////////////////////////////////////.println.println("finalQuery is:: "+finalQuery);
            queries.add(finalQuery);

            executeMultiple(queries);

        } catch (Exception exception) {
            logger.error("Exception:", exception);

        }
    }

    public PbReturnObject getScstrtMonths(String ScId) throws Exception {
        PbReturnObject pbret = null;
        String query = resBundle.getString("getScstrtMonths");
        //////////////////////////////////////////.println.println("getScstrtMonths query iss::======== " + query);
        String finalquery = "";
        Object[] Obj = new Object[1];
        Obj[0] = ScId;
        try {
            finalquery = buildQuery(query, Obj);
            pbret = execSelectSQL(finalquery);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }

        return pbret;
    }

    //added by uday on 18th jan 2010
    public String buildTableColumns(String[] MeasureIds, String[] MeasureNames, StringBuffer prevColumns) {
        StringBuffer graphBuffer = new StringBuffer("");

        if (MeasureIds != null && MeasureNames != null) {
            for (int i = 0; i < MeasureIds.length; i++) {
                graphBuffer.append(" <li id='Msr" + MeasureIds[i].replace("A_", "") + "' style='width:auto;height:auto;color:white' class='navtitle-hover'>");
                graphBuffer.append("<table id='Tab" + MeasureIds[i].replace("A_", "") + "'>");
                graphBuffer.append("<tr>");
                graphBuffer.append("<td>");
                graphBuffer.append("<a href=\"javascript:deleteMeasure('Msr" + MeasureIds[i].replace("A_", "") + "')\" class=\"ui-icon ui-icon-close\"></a>");
                graphBuffer.append("</td>");
                graphBuffer.append("<td style=\"color:black\">" + MeasureNames[i] + "</td>");
                graphBuffer.append("</tr>");
                graphBuffer.append("</table>");
                graphBuffer.append("</li>");
            }
        }
        return graphBuffer.toString();
    }

    public String dispParameters(String parameters, HashMap ParametersHashMap) {
        DisplayScenarioParameters disp = new DisplayScenarioParameters();
        ArrayList dispParams = new ArrayList();
        String[] paramIds = null;
        HashMap TimeDimHashMap = null;
        ArrayList TimeDimList = new ArrayList();
        String temp = "";
        try {
            TimeDimHashMap = (HashMap) ParametersHashMap.get("TimeDimHashMap");
            TimeDimList = (ArrayList) ParametersHashMap.get("timeParameters");
            paramIds = parameters.split(",");
            HashMap paramValues = new HashMap();
            for (int i = 0; i < paramIds.length; i++) {
                dispParams.add(paramIds[i]);
                paramValues.put(paramIds[i], "All");
            }

            temp = disp.displayParamwithTime(dispParams, TimeDimHashMap);
        } catch (Exception exp) {
            logger.error("Exception:", exp);
        }
        return temp;
    }

    public String dispTable(HashMap ParametersHashMap, HashMap TableHashMap) {
        DisplayScenarioParameters disp = new DisplayScenarioParameters();


        String temp = "";
        try {

            //////////////////////////////////////////.println.println("ParametersHashMap in dispTable is:: "+ParametersHashMap);
            //////////////////////////////////////////.println.println("TableHashMap in dispTable is:: "+TableHashMap);
            temp = disp.displayTable(ParametersHashMap, TableHashMap);

        } catch (Exception exp) {
            logger.error("Exception:", exp);
        }
        return temp;
    }

    public String getMonthLastDate(String str) {
        // //////.println("str--------"+str);
        String lastDate = "";
        PbReturnObject pbro = null;
//        String query = "select to_char(cm_end_date,'mm/dd/yyyy') end_date from prg_acn_mon_denom where mon_name='&'";
        String query = "select distinct to_char(CY_END_DATE,'mm/dd/yyyy') end_date from PR_DAY_DENOM where CWYEAR='&'";
        String finalQuery = "";
        Object[] Obj = new Object[1];
        Obj[0] = str;
        try {
            finalQuery = buildQuery(query, Obj);
            //  //////.println("finalQuery34545 " + finalQuery);
            pbro = execSelectSQL(finalQuery);
            lastDate = pbro.getFieldValueString(0, 0);
            //////////////////////////////////////////.println.println("lastDate is:: " + lastDate);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        return lastDate;

    }

    public ArrayList getModelBasisValues() {
        PbReturnObject pbro = null;
        String finalQuery = "select * from scenario_model_basis order by basis_id";
        ArrayList modelBasisValues = new ArrayList();
        String temp = "";
        try {
            pbro = execSelectSQL(finalQuery);
            int count = pbro.getRowCount();
            for (int i = 0; i < count; i++) {
                temp = pbro.getFieldValueString(i, 1);
                modelBasisValues.add(temp.trim());
            }
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        return modelBasisValues;

    }

    public ArrayList getCustomModelMonthsList(String historicalStartMonth, String historicalEndMonth) {
        PbReturnObject pbro = null;
//        String finalQuery = "select mon_name from prg_acn_mon_denom where cm_st_date between (select cm_st_date from prg_acn_mon_denom where mon_name='" + historicalStartMonth + "') " +
//                "and (select cm_st_date from prg_acn_mon_denom where mon_name='" + historicalEndMonth + "')";
        String finalQuery = "select distinct cyear from pr_day_denom where cyear between " + historicalStartMonth + " and  " + historicalEndMonth + " order by cyear";
        ArrayList monthsList = new ArrayList();
        String temp = "";
        try {
            pbro = execSelectSQL(finalQuery);
            int count = pbro.getRowCount();
            for (int i = 0; i < count; i++) {
                temp = pbro.getFieldValueString(i, 0);
                monthsList.add(temp.trim());
            }
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        return monthsList;

    }

    public String saveCustomModel(String scenarioId, String modelName, String modelBasis, String[] custModelMonths, String[] weights) {
        PbReturnObject pbro = new PbReturnObject();
        String customModelId = "";
        String seqNumber = "";
        String finalQuery = "";
        ArrayList queries = new ArrayList();

        try {
            //code to insert into prg_custom_model_master table
            /*
             * String Query = "insert into PRG_CUSTOM_MODEL_MASTER
             * (CUSTOM_MODEL_ID, CUSTOM_MODEL_NAME, SCENARIO_ID,
             * CUSTOM_MODEL_TYPE, MODEL_BASIS) " + "values((select
             * decode(max(CUSTOM_MODEL_ID),NULL,9,max(CUSTOM_MODEL_ID)+1) as
             * CUSTOM_MODEL_ID from PRG_CUSTOM_MODEL_MASTER where
             * scenario_id='&')," + "'&',&,'&','&')";
             */
            String Query = "insert into PRG_CUSTOM_MODEL_MASTER (CUSTOM_MODEL_ID, CUSTOM_MODEL_NAME, SCENARIO_ID, CUSTOM_MODEL_TYPE, MODEL_BASIS) "
                    + "values((select decode(max(CUSTOM_MODEL_ID),NULL,9,max(CUSTOM_MODEL_ID)+1) as CUSTOM_MODEL_ID from PRG_CUSTOM_MODEL_MASTER),"
                    + "'&',&,'&','&')";

            Object[] obj = new Object[4];
            obj[0] = modelName;
            obj[1] = scenarioId;
            obj[2] = "N";
            obj[3] = modelBasis;

            finalQuery = buildQuery(Query, obj);
            //////////////////////////////////////////.println.println("finalQuery is:: " + finalQuery);
            execModifySQL(finalQuery);

            String masterIdQry = "select max(CUSTOM_MODEL_ID) as CUSTOM_MODEL_ID from PRG_CUSTOM_MODEL_MASTER where scenario_id='" + scenarioId + "'";
            //String masterIdQry = "select max(CUSTOM_MODEL_ID) as CUSTOM_MODEL_ID from PRG_CUSTOM_MODEL_MASTER";
            PbReturnObject ret = execSelectSQL(masterIdQry);
            customModelId = ret.getFieldValueString(0, 0);

            //code to insert into prg_custom_model_details table

            String seq = "select PRG_CUSTOM_MODEL_DETAILS_SEQ.nextval from dual";
            String detailsId = "";
            String detailsQuery = "insert into PRG_CUSTOM_MODEL_DETAILS (MODEL_DETAILS_ID, CUSTOM_MODEL_ID, CUSTOM_MODEL_MONTHS, CUSTOM_MODEL_WEIGHTS) "
                    + "values(&,&,'&','&')";
            String finalDetailsQuery = "";
            Object[] object = new Object[4];

            for (int i = 0; i < custModelMonths.length; i++) {

                pbro = execSelectSQL(seq);
                detailsId = pbro.getFieldValueString(0, 0);
                object[0] = detailsId;
                object[1] = customModelId;
                object[2] = custModelMonths[i];
                if (modelBasis.equalsIgnoreCase("Weighted Average")) {
                    object[3] = weights[i];
                } else {
                    object[3] = "";
                }


                finalDetailsQuery = buildQuery(detailsQuery, object);
                queries.add(finalDetailsQuery);
            }

            executeMultiple(queries);

        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        return customModelId;
    }

    public void updateScenarioRating(String scenarioRating, String scenarioId, String modelName, String dimensionId) {

        String updateScenarioRating = resBundle.getString("updateScenarioRating");
        String finalQuery = "";

        try {

            Object[] Obj = new Object[4];
            Obj[0] = scenarioRating;
            Obj[1] = scenarioId;
            Obj[2] = modelName;
            Obj[3] = dimensionId;

            finalQuery = buildQuery(updateScenarioRating, Obj);
            //////////////////////////////////////////.println.println("finalQuery is:: "+finalQuery);
            execModifySQL(finalQuery);

        } catch (Exception exception) {
            logger.error("Exception:", exception);

        }
    }

    public PbReturnObject getFolderIdbyScenarioId(String scenarioId) {

        String getFolderIdbyScenarioIdQry = resBundle.getString("getFolderIdbyScenarioId");
        String finalQuery = "";
        PbReturnObject pbro = null;
        try {

            Object[] Obj = new Object[1];
            Obj[0] = scenarioId;

            finalQuery = buildQuery(getFolderIdbyScenarioIdQry, Obj);
            //////////////////////////////////////////.println.println("finalQuery is:: "+finalQuery);
            pbro = execSelectSQL(finalQuery);

        } catch (Exception exception) {
            logger.error("Exception:", exception);

        }
        return pbro;
    }

    public String getviewByIdbyScenarioId(String scenarioId) {

        String getviewByIdbyScenarioIdQry = resBundle.getString("getviewByIdbyScenarioId");
        String finalQuery = "";
        PbReturnObject pbro = null;
        String viewbyid = "";
        try {

            Object[] Obj = new Object[1];
            Obj[0] = scenarioId;

            finalQuery = buildQuery(getviewByIdbyScenarioIdQry, Obj);
            //////////////////////////////////////////.println.println("finalQuery is:: "+finalQuery);
            pbro = execSelectSQL(finalQuery);
            if (pbro.getRowCount() > 0) {
                viewbyid = pbro.getFieldValueString(0, 0);
            }

        } catch (Exception exception) {
            logger.error("Exception:", exception);

        }
        return viewbyid;
    }

    public String getScenarioDetsbyScenarioId(String scenarioId) {

        String getScenarioDetsbyScenarioIdQry = resBundle.getString("getScenarioDetsbyScenarioId");
        String finalQuery = "";
        PbReturnObject pbro = null;
        String viewbyid = "";
        try {

            Object[] Obj = new Object[1];
            Obj[0] = scenarioId;

            finalQuery = buildQuery(getScenarioDetsbyScenarioIdQry, Obj);
            // //////.println("finalQuery is:: "+finalQuery);
            pbro = execSelectSQL(finalQuery);
            if (pbro.getRowCount() > 0) {
                viewbyid = pbro.getFieldValueString(0, "CALC_ELEMENT_ID");
            }

        } catch (Exception exception) {
            logger.error("Exception:", exception);

        }
        return viewbyid;
    }

    public void saveNewMeasure(String scenarioId, String Msrs) {
        String addNewMeasureQry = resBundle.getString("addNewMeasure");
        String finalQuery = "";
        PbReturnObject pbro = null;
        String viewbyid = "";
        try {

            Object[] Obj = new Object[2];
            Obj[0] = Msrs;
            Obj[1] = scenarioId;
            finalQuery = buildQuery(addNewMeasureQry, Obj);
            // //////.println("finalQuery is:: "+finalQuery);
            execUpdateSQL(finalQuery);


        } catch (Exception exception) {
            logger.error("Exception:", exception);

        }
    }
}
