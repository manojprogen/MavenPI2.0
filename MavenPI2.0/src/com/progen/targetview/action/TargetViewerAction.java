/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.targetview.action;

import com.progen.charts.ProgenChartDatasets;
import com.progen.charts.ProgenChartDisplay;
import com.progen.targetview.db.PbTargetDAO;
import com.progen.targetview.db.PbTargetViewerDb;
import com.uploadfile.excel.StrutsUploadAndSaveForm;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.LookupDispatchAction;
import org.apache.struts.upload.FormFile;
import prg.db.PbReturnObject;
import prg.db.Session;
import prg.target.sheetschceduler.ExcelScheduler;
import prg.targetparam.qdparams.PbTargetParamTable;
import prg.targetparam.qdparams.PbTargetValuesParam;
import utils.db.ProgenConnection;

/**
 *
 * @author Saurabh
 */
public class TargetViewerAction extends LookupDispatchAction {

    public static Logger logger = Logger.getLogger(TargetViewerAction.class);
    /*
     * forward name="success" path=""
     */
    private final static String SUCCESS = "success";

    public ActionForward viewTarget(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession(false);
        String targetId = "";
        String UserId = "";
        String minTimeLevel = "";
        ////////////////////////////////////////////////////////////////////////////////////////////////////////.println("targetId is:: "+targetId);
        if (request.getParameter("targetId") != null) {
            targetId = request.getParameter("targetId");
        }
        if (request.getParameter("minTimeLevel") != null) {
            minTimeLevel = request.getParameter("minTimeLevel");
        }

        //////////////////////////////////////////////////////////////////////////////////////////////////////.println("targetId is:: "+targetId);
        if (session.getAttribute("USERID") != null) {
            UserId = String.valueOf(session.getAttribute("USERID"));
        }

        PbTargetViewerDb targetViewerDb = null;
        targetViewerDb = new PbTargetViewerDb();
        targetViewerDb.prepareTarget(targetId, minTimeLevel, UserId, request, response);

        request.setAttribute("TARGETID", targetId);
        request.setAttribute("TIMELEVEL", minTimeLevel);

        return mapping.findForward("showViewTarget");
    }

    public ActionForward viewTargetForView(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession(false);
        //////////////////////////////////////////////////////////////////////////.println(" in view --------------");
        String targetId = "";
        String UserId = "";
        String minTimeLevel = "";
        ////////////////////////////////////////////////////////////////////////////////////////////////////////.println("targetId is:: "+targetId);
        if (request.getParameter("targetId") != null) {
            targetId = request.getParameter("targetId");
        }
        if (request.getParameter("minTimeLevel") != null) {
            minTimeLevel = request.getParameter("minTimeLevel");
        }

        //////////////////////////////////////////////////////////////////////////////////////////////////////.println("targetId is:: "+targetId);
        if (session.getAttribute("USERID") != null) {
            UserId = String.valueOf(session.getAttribute("USERID"));
        }

        PbTargetViewerDb targetViewerDb = null;
        targetViewerDb = new PbTargetViewerDb();
        targetViewerDb.prepareTargetForView(targetId, minTimeLevel, UserId, request, response);

        request.setAttribute("TARGETID", targetId);
        request.setAttribute("TIMELEVEL", minTimeLevel);

        return mapping.findForward("showViewTargetForView");
    }

    public ActionForward saveTarget(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession(false);
        String targetId = "";
        String UserId = "";
        String minTimeLevel = "";
        String tertiary = request.getParameter("tertiaryViewBy");
        //////////////////////////////////////////////////////////////////////////.println("tertiary is:: in save Tare " + tertiary);
        if (request.getParameter("targetId") != null) {
            targetId = request.getParameter("targetId");
        }
        if (request.getParameter("minTimeLevel") != null) {
            minTimeLevel = request.getParameter("minTimeLevel");
        }

        //////////////////////////////////////////////////////////////////////////.println("targetId is:: in save Target -- == " + targetId);
        if (session.getAttribute("USERID") != null) {
            UserId = String.valueOf(session.getAttribute("USERID"));
        }
        HashMap originalResult = new HashMap();
        ArrayList columnEdgeValuesA = new ArrayList();
        ArrayList rowEdgeValuesA = new ArrayList();
        String secAnalyze = "";
        String primaryAnalyze = "";
        String startRange = "";

        if (session.getAttribute("originalResult") != null) {
            originalResult = (HashMap) session.getAttribute("originalResult");
        }
        if (session.getAttribute("columnEdgeValuesA") != null) {
            columnEdgeValuesA = (ArrayList) session.getAttribute("columnEdgeValuesA");
        }
        if (session.getAttribute("rowEdgeValuesA") != null) {
            rowEdgeValuesA = (ArrayList) session.getAttribute("rowEdgeValuesA");
        }

        if (session.getAttribute("secAnalyze") != null) {
            secAnalyze = (String) session.getAttribute("secAnalyze");
        }
        if (session.getAttribute("primaryAnalyze") != null) {
            primaryAnalyze = (String) session.getAttribute("primaryAnalyze");
        }

        String primParamEleId = "";
        if (session.getAttribute("primParamEleId") != null) {
            primParamEleId = (String) session.getAttribute("primParamEleId");
        }

        if (session.getAttribute("startRange") != null) {
            startRange = (String) session.getAttribute("startRange");
        }


        HashMap nonAllIds = new HashMap();
        String viewByName = "";

        HashMap boxNames = new HashMap();
        Enumeration ee = request.getParameterNames();
        while (ee.hasMoreElements()) {
            String reqKey = (String) ee.nextElement();
            String val = request.getParameter(reqKey);

            // //////////////////////////////////////////////////////////////////////////.println(" reqKey-- "+reqKey);
            if (reqKey.startsWith("CBOARP")) {
                String elementId = reqKey.substring(6);
                if (val.equalsIgnoreCase("All")) {
                    nonAllIds.put(elementId, "-1");
                } else {
                    nonAllIds.put(elementId, val);
                }
            }
            if (reqKey.startsWith("CBOVIEW_BY")) {
                String k = reqKey.substring(10);
                viewByName = request.getParameter(reqKey);
            }
            long mValue = 0;
            long mValue2 = 0;
            // //////////////////////////////////////////////////////////////////////////.println(" originalResult "+originalResult);
            String reqKey2 = reqKey + ":d";
            if (originalResult.containsKey(reqKey)) {
                if (!val.equalsIgnoreCase("")) {
                    mValue = Long.parseLong(val);
                }
                String val2 = "";
                val2 = request.getParameter(reqKey2);
                //////////////////////////////////////////////////////////////////////////.println(" reqKey2 " + reqKey2);
                //////////////////////////////////////////////////////////////////////////.println(" val2 " + val2);
                if (val2 != null) {
                    if (!val2.equalsIgnoreCase("")) {
                        mValue2 = Long.parseLong(val2);
                    }
                }

                boxNames.put(reqKey, new Long(mValue));
                boxNames.put(reqKey2, mValue2);
            }
        }
        //////////////////////////////////////////////////////////////////////////.println(nonAllIds + " - boxNames " + boxNames);
        //////////////////////////////////////////////////////////////////////////.println(" startRange... " + startRange);
        //////////////////////////////////////////////////////////////////////////.println(" rowEdgeValuesA ." + rowEdgeValuesA + " columnEdgeValuesA " + columnEdgeValuesA + " viewByName.. " + viewByName);
        if (secAnalyze.equalsIgnoreCase("Time")) {
            PbTargetParamTable pTable = new PbTargetParamTable();
            PbTargetValuesParam targetParam = new PbTargetValuesParam();
            Session sess = new Session();
            PbTargetDAO pbDao = new PbTargetDAO();

            targetParam.setPeriodType(minTimeLevel);
            targetParam.setRatioComb(originalResult);
            targetParam.setMinimumTimeLevel(minTimeLevel);
            targetParam.setTargetId(targetId);
            targetParam.setNonAllIds(nonAllIds);
            // targetParam.setViewByParameter(viewByName.trim());primaryAnalyze
            //////////////////////////////////////////////////////////////////////////.println(" primaryAnalyze in action " + primaryAnalyze);
            targetParam.setViewByParameter(primaryAnalyze.trim());

            for (int i = 0; i < rowEdgeValuesA.size(); i++) {
                for (int j = 0; j < columnEdgeValuesA.size(); j++) {
                    String key = rowEdgeValuesA.get(i).toString() + ":" + columnEdgeValuesA.get(j).toString();
                    long newVal = 0;
                    long oldVal = 0;
                    String key2 = rowEdgeValuesA.get(i).toString() + ":" + columnEdgeValuesA.get(j).toString() + ":d";//tertiary
                    //////////////////////////////////////////////////////////////////////////.println(" key2-=-=- " + key2);
                    if (boxNames.containsKey(key)) {
                        newVal = ((Long) boxNames.get(key)).longValue();
                    }

                    if (primaryAnalyze.trim().equalsIgnoreCase("OverAll") || primaryAnalyze.equalsIgnoreCase("0")) {
                        if (tertiary.equalsIgnoreCase("Percentage")) {
                            if (boxNames.containsKey(key2)) {
                                newVal = ((Long) boxNames.get(key2)).longValue();
                                //////////////////////////////////////////////////////////////////////////.println(" in if 232j3 " + newVal);
                            }
                        }
                    }
                    if (originalResult.containsKey(key)) {
                        oldVal = ((Long) originalResult.get(key)).longValue();
                    }
                    //////////////////////////////////////////////////////////////////////////.println(newVal + " oldVal " + oldVal);
                    // when new is not!=0 and original update
                    if (newVal != 0 && (newVal != oldVal) && oldVal != 0) {
                        PbTargetValuesParam targetParam2 = new PbTargetValuesParam();
                        targetParam2.setMeasureVal(newVal);
                        targetParam2.setDateVal(columnEdgeValuesA.get(j).toString());
                        targetParam2.setUpdateFlag("Y");
                        targetParam2.setPrimViewByValue(rowEdgeValuesA.get(i).toString());
                        //////////////////////////////////////////////////////////////////////////////////////////////////////.println(key+" update "+newVal+" oldVal "+oldVal);
                        pTable.addRow(targetParam2);
                    }
                    // when new is not!=0 and original delete
                    if (newVal == 0 && oldVal != 0) {
                        PbTargetValuesParam targetParam2 = new PbTargetValuesParam();
                        targetParam2.setMeasureVal(newVal);
                        targetParam2.setDateVal(columnEdgeValuesA.get(j).toString());
                        targetParam2.setUpdateFlag("D");
                        targetParam2.setDeleteFlag("D");
                        targetParam2.setPrimViewByValue(rowEdgeValuesA.get(i).toString());
                        //////////////////////////////////////////////////////////////////////////////////////////////////////.println(key+" delete "+newVal+" oldVal "+oldVal);
                        pTable.addRow(targetParam2);
                    }
                    // when new is not!=0 and original 0
                    if (newVal != 0 && oldVal == 0) {
                        PbTargetValuesParam targetParam2 = new PbTargetValuesParam();
                        targetParam2.setMeasureVal(newVal);
                        targetParam2.setDateVal(columnEdgeValuesA.get(j).toString());
                        targetParam2.setUpdateFlag("N");
                        targetParam2.setPrimViewByValue(rowEdgeValuesA.get(i).toString());
                        //////////////////////////////////////////////////////////////////////////.println(key + " insert " + newVal + " oldVal " + oldVal);
                        pTable.addRow(targetParam2);
                    }
                }
            }
            sess.setObject(targetParam);
            sess.setObject(pTable);
            //////////////////////////////////////////////////////////////////////////.println(primaryAnalyze + " pTable " + pTable.getRowCount());
            if (pTable.getRowCount() > 0) {
                // pbDao.saveTargetValues(sess);
                if (primaryAnalyze.trim().equalsIgnoreCase("OverAll") || primaryAnalyze.equalsIgnoreCase("0")) {
                    pbDao.saveTargetValuesNewForOverAll(sess);
                } else {
                    pbDao.saveTargetValuesNew(sess);
                }
                if (primParamEleId.equalsIgnoreCase(primaryAnalyze)) {
                    // pbDao.saveForPrimaryParameter(sess);
                }
            }
        } else {
            PbTargetParamTable pTable = new PbTargetParamTable();
            PbTargetValuesParam targetParam = new PbTargetValuesParam();
            Session sess = new Session();
            PbTargetDAO pbDao = new PbTargetDAO();

            targetParam.setPeriodType(minTimeLevel);
            targetParam.setMinimumTimeLevel(minTimeLevel);
            targetParam.setTargetId(targetId);
            targetParam.setNonAllIds(nonAllIds);
            targetParam.setRatioComb(originalResult);
            // targetParam.setViewByParameter(viewByName.trim());primaryAnalyze
            targetParam.setPrimaryParameter(primaryAnalyze);
            targetParam.setViewByParameter(secAnalyze.trim());

            for (int i = 0; i < rowEdgeValuesA.size(); i++) {
                for (int j = 0; j < columnEdgeValuesA.size(); j++) {
                    String key = rowEdgeValuesA.get(i).toString() + ":" + columnEdgeValuesA.get(j).toString();
                    int newVal = 0;
                    int oldVal = 0;
                    if (boxNames.containsKey(key)) {
                        newVal = ((Integer) boxNames.get(key)).intValue();
                    }
                    if (originalResult.containsKey(key)) {
                        oldVal = ((Integer) originalResult.get(key)).intValue();
                    }
                    // when new is not!=0 and original update
                    if (newVal != 0 && (newVal != oldVal) && oldVal != 0) {
                        PbTargetValuesParam targetParam2 = new PbTargetValuesParam();
                        targetParam2.setMeasureVal(newVal);
                        targetParam2.setDateVal(startRange);
                        targetParam2.setUpdateFlag("Y");
                        // targetParam2.setPrimViewByValue(rowEdgeValuesA.get(i).toString());
                        targetParam2.setPrimViewByValue(columnEdgeValuesA.get(j).toString());
                        targetParam2.setPrimAnalyzeVal(rowEdgeValuesA.get(i).toString());
                        //////////////////////////////////////////////////////////////////////////////////////////////////////.println(key+" update "+newVal+" oldVal "+oldVal);
                        pTable.addRow(targetParam2);
                    }
                    // when new is not!=0 and original delete
                    if (newVal == 0 && oldVal != 0) {
                        PbTargetValuesParam targetParam2 = new PbTargetValuesParam();
                        targetParam2.setMeasureVal(newVal);
                        targetParam2.setDateVal(startRange);
                        targetParam2.setUpdateFlag("D");
                        targetParam2.setPrimAnalyzeVal(rowEdgeValuesA.get(i).toString());
                        targetParam2.setDeleteFlag("D");
                        // targetParam2.setPrimViewByValue(rowEdgeValuesA.get(i).toString());
                        targetParam2.setPrimViewByValue(columnEdgeValuesA.get(j).toString());
                        //////////////////////////////////////////////////////////////////////////////////////////////////////.println(key+" delete "+newVal+" oldVal "+oldVal);
                        pTable.addRow(targetParam2);
                    }
                    // when new is not!=0 and original 0
                    if (newVal != 0 && oldVal == 0) {
                        PbTargetValuesParam targetParam2 = new PbTargetValuesParam();
                        targetParam2.setMeasureVal(newVal);
                        targetParam2.setDateVal(startRange);
                        targetParam2.setUpdateFlag("N");
                        targetParam2.setPrimAnalyzeVal(rowEdgeValuesA.get(i).toString());
                        // targetParam2.setPrimViewByValue(rowEdgeValuesA.get(i).toString());
                        targetParam2.setPrimViewByValue(columnEdgeValuesA.get(j).toString());
                        //////////////////////////////////////////////////////////////////////////////////////////////////////.println(key+" insert "+newVal+" oldVal "+oldVal);
                        pTable.addRow(targetParam2);
                    }
                }
            }
            sess.setObject(targetParam);
            sess.setObject(pTable);
            if (pTable.getRowCount() > 0) {
                pbDao.savePrimSec(sess);
            }

        }


        PbTargetViewerDb targetViewerDb = null;
        targetViewerDb = new PbTargetViewerDb();
        targetViewerDb.prepareTarget(targetId, minTimeLevel, UserId, request, response);

        request.setAttribute("TARGETID", targetId);
        request.setAttribute("TIMELEVEL", minTimeLevel);

        return mapping.findForward("showViewTarget");
    }

    public ActionForward allocateTarget(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession(false);
        String targetId = "";
        String UserId = "";
        String minTimeLevel = "";
        ////////////////////////////////////////////////////////////////////////////////////////////////////////.println("targetId is:: "+targetId);
        if (request.getParameter("targetId") != null) {
            targetId = request.getParameter("targetId");
        }
        if (request.getParameter("minTimeLevel") != null) {
            minTimeLevel = request.getParameter("minTimeLevel");
        }

        //////////////////////////////////////////////////////////////////////////////////////////////////////.println("targetId is:: in save "+targetId);
        if (session.getAttribute("USERID") != null) {
            UserId = String.valueOf(session.getAttribute("USERID"));
        }

        HashMap originalResult = new HashMap();
        HashMap nonAllIds = new HashMap();
        originalResult = (HashMap) session.getAttribute("originalResult");
        Enumeration ee = request.getParameterNames();
        HashMap boxNames = new HashMap();
        while (ee.hasMoreElements()) {
            String reqKey = (String) ee.nextElement();
            String val = request.getParameter(reqKey);
            if (reqKey.startsWith("CBOARP")) {
                String elementId = reqKey.substring(6);
                if (val.equalsIgnoreCase("All")) {
                    nonAllIds.put(elementId, "-1");
                } else {
                    nonAllIds.put(elementId, val);
                }
            }

            int mValue = 0;
            if (originalResult.containsKey(reqKey)) {
                if (!val.equalsIgnoreCase("")) {
                    mValue = Integer.parseInt(val);
                }
                boxNames.put(reqKey, new Integer(mValue));
            }
        }
        //////////////////////////////////////////////////////////////////////////////////////////////////////.println(" minTimeLevel -- "+minTimeLevel);
        String secAnalyze = "";
        String primaryAnalyze = "";
        String startPeriod = "";
        String endPeriod = "";
        String periodType = "";
        String primParamEleId = "";
        //////////////////////////////////////////////////////////////////////////////////////////////////////.println(targetId+" startRange "+startPeriod+" endRange-- "+endPeriod);
        ArrayList defDates = (ArrayList) session.getAttribute("defDates");
        startPeriod = (String) session.getAttribute("startPeriod");
        endPeriod = (String) session.getAttribute("endPeriod");
        periodType = (String) session.getAttribute("periodType");
        if (session.getAttribute("primaryAnalyze") != null) {
            primaryAnalyze = (String) session.getAttribute("primaryAnalyze");
        }
        if (session.getAttribute("primParamEleId") != null) {
            primParamEleId = (String) session.getAttribute("primParamEleId");
        }



        //////////////////////////////////////////////////////////////////////////////////////////////////////.println(" defDates - "+defDates);
        PbTargetDAO pbDao = new PbTargetDAO();
        PbReturnObject pbro = pbDao.getTimeQueryForAllocation(minTimeLevel, periodType, startPeriod, endPeriod);
        HashMap individualDatesGroup = new HashMap();
        //////////////////////////////////////////////////////////////////////////////////////////////////////.println(" periodType -- "+periodType);
        int count = 0;
        if (pbro != null) {
            count = pbro.getRowCount();
            //////////////////////////////////////////////////////////////////////////////////////////////////////.println("in if -- == "+count);
            for (int i = 0; i < pbro.getRowCount(); i++) {
                //////////////////////////////////////////////////////////////////////////////////////////////////////.println("here -- .. "+pbro.getFieldValueString(i,0));
                if (!periodType.equalsIgnoreCase("Quarter")) {
                    String dateVal = pbro.getFieldValueString(i, 0);
                    String upperDate = pbro.getFieldValueString(i, 1);
                    if (minTimeLevel.equalsIgnoreCase("Quarter")) {
                        dateVal = pbro.getFieldValueString(i, 0) + "-" + pbro.getFieldValueString(i, 1);
                    }
                    ArrayList allDates = new ArrayList();
                    if (individualDatesGroup.containsKey(upperDate)) {
                        if (defDates.contains(dateVal)) {
                            allDates = (ArrayList) individualDatesGroup.get(upperDate);
                            allDates.add(dateVal);
                            //////////////////////////////////////////////////////////////////////////////////////////////////////.println("dateVal - "+dateVal);
                            individualDatesGroup.put(upperDate, allDates);
                        }
                    } else {
                        if (defDates.contains(dateVal)) {
                            allDates.add(dateVal);
                            individualDatesGroup.put(upperDate, allDates);
                            //////////////////////////////////////////////////////////////////////////////////////////////////////.println("dateVal - .. "+dateVal);
                        }
                    }
                } else if (periodType.equalsIgnoreCase("Quarter")) {
                    //////////////////////////////////////////////////////////////////////////////////////////////////////.println("in else --");
                    String dateVal = pbro.getFieldValueString(i, 0);//+"-"+pb.getFieldValueString(i,1);
                    String upperDate = pbro.getFieldValueString(i, 1) + "-" + pbro.getFieldValueString(i, 2);
                    ArrayList allDates = new ArrayList();
                    //////////////////////////////////////////////////////////////////////////////////////////////////////.println(dateVal+" ..in else-----"+upperDate);
                    if (individualDatesGroup.containsKey(upperDate)) {
                        if (defDates.contains(dateVal)) {
                            allDates = (ArrayList) individualDatesGroup.get(upperDate);
                            allDates.add(dateVal);
                            individualDatesGroup.put(upperDate, allDates);
                            //////////////////////////////////////////////////////////////////////////////////////////////////////.println(allDates+" - in else-----"+upperDate);
                        }
                    } else {
                        if (defDates.contains(dateVal)) {
                            allDates.add(dateVal);
                            individualDatesGroup.put(upperDate, allDates);
                            //////////////////////////////////////////////////////////////////////////////////////////////////////.println("dateVal - "+dateVal);
                        }

                    }
                }
            }
        }
        //////////////////////////////////////////////////////////////////////////////////////////////////////.println(" individualDatesGroup - "+individualDatesGroup);

        PbTargetValuesParam targetParam = new PbTargetValuesParam();
        Session sess = new Session();
        targetParam.setTargetId(targetId);
        //////////////////////////////////////////////////////////////////////////////////////////////////////.println(" targetId .."+targetId);
        sess.setObject(targetParam);
        PbReturnObject all = pbDao.getTargetDetails(sess);
        PbReturnObject tarDet = (PbReturnObject) all.getObject("TargetDet");
        PbReturnObject tarColDet = (PbReturnObject) all.getObject("TargetCols");

        HashMap paramCols = new HashMap();
        HashMap paramEleNames = new HashMap();
        for (int m = 0; m < tarColDet.getRowCount(); m++) {
            paramCols.put(tarColDet.getFieldValueString(m, "ELEMENT_ID"), tarColDet.getFieldValueString(m, "BUSS_COL_NAME"));
            paramEleNames.put(tarColDet.getFieldValueString(m, "ELEMENT_ID"), tarColDet.getFieldValueDateString(m, "PARAM_DISP_NAME"));
        }
        Connection con = ProgenConnection.getInstance().getConnection();
        Connection con2 = ProgenConnection.getInstance().getConnection();

        String tableName = tarDet.getFieldValueString(0, "BUSS_TABLE_NAME");
        String measureName2 = tarDet.getFieldValueString(0, "MEASURE_NAME");
        String measureName = measureName2.trim().replace(" ", "_");

        String lastNonZero = "";
        HashMap lastNonZ = new HashMap();
        ArrayList param_val = new ArrayList();
        ArrayList dateValues = new ArrayList();
        if (session.getAttribute("columnEdgeValuesA") != null) {
            dateValues = (ArrayList) session.getAttribute("columnEdgeValuesA");
        }
        if (session.getAttribute("rowEdgeValuesA") != null) {
            param_val = (ArrayList) session.getAttribute("rowEdgeValuesA");
        }

        String query = "";
        //////////////////////////////////////////////////////////////////////////////////////////////////////.println(" boxNames .. "+boxNames);
        String paramName = param_val.get(0).toString();
        String paramReplacedVal = "";
        /*
         * new ResultSet()
         */;
        HashMap ratioComb = new HashMap();
        //////////////////////////////////////////////////////////////////////////////////////////////////////.println(paramCols+" primaryAnalyze --"+primaryAnalyze);
        String primColumn = paramCols.get(primaryAnalyze).toString();

        for (int n = 1; n < param_val.size(); n++) {
            for (int m = 0; m < dateValues.size(); m++) {

                if (individualDatesGroup.containsKey(dateValues.get(m))) {
                    ArrayList updates = (ArrayList) individualDatesGroup.get(dateValues.get(m));
                    int counts = updates.size();

                    int individualDateVal = 0;
                    HashMap copy = new HashMap();
                    /// //////////////////////////////////////////////////////////////////////////////////////////////////////.println(" ----- "+param_val.get(n)+" - -- "+dateValues.get(m));
                    if (boxNames.containsKey(param_val.get(n) + ":" + dateValues.get(m))) {
                        if (originalResult.containsKey(param_val.get(n) + ":" + dateValues.get(m))) {
                            int original = ((Integer) originalResult.get(param_val.get(n) + ":" + dateValues.get(m))).intValue();
                            int newVal = ((Integer) (boxNames.get(param_val.get(n) + ":" + dateValues.get(m)))).intValue();

                            Object originalF = originalResult.get(param_val.get(n) + ":" + dateValues.get(m));
                            Object newValF = boxNames.get(param_val.get(n) + ":" + dateValues.get(m));
                            double oriF = Double.parseDouble(originalF.toString());
                            double newF = Double.parseDouble(newValF.toString());
                            // //////////////////////////////////////////////////////////////////////////////////////////////////////.println("originalF - "+originalF+" newValF -"+newValF);
                            query = "";
                            double divisionVal = 0;
                            divisionVal = (double) ((newF - oriF) / oriF);
                            float ratio = (float) (1 + divisionVal);
                            // //////////////////////////////////////////////////////////////////////////////////////////////////////.println(divisionVal+" divisionVal - ratio - "+ratio+" original -"+original+" newVal - "+newVal);
                            if ((original != newVal) && (newVal != 0)) {
                                int totalCount = ((Integer) (boxNames.get(param_val.get(n) + ":" + dateValues.get(m)))).intValue();

                                for (int y = 0; y < updates.size(); y++) {
                                    int oldV = 0;
                                    if (minTimeLevel.equalsIgnoreCase("Day")) {
                                        query = "select " + measureName.trim() + " from " + tableName.trim() + " where target_id=" + targetId + " and t_date=to_date('" + updates.get(y) + "','dd-mm-yy') and viewby='" + paramName + "' and " + primColumn + "='" + param_val.get(n) + "'";//con.prepareStatement("select ");
                                        if (!paramReplacedVal.equalsIgnoreCase("")) {
                                            query = "select " + measureName.trim() + " from " + tableName.trim() + " where target_id=" + targetId + " and t_date=to_date('" + updates.get(y) + "','dd-mm-yy') and viewby='" + paramName + "' and " + primColumn + "='" + param_val.get(n) + "' and " + paramReplacedVal;//con.prepareStatement("select ");
                                        }
                                    } else if (minTimeLevel.equalsIgnoreCase("Month")) {
                                        query = "select " + measureName.trim() + " from " + tableName.trim() + " where target_id=" + targetId + " and t_month='" + updates.get(y) + "' and viewby='" + paramName + "' and " + primColumn + "='" + param_val.get(n) + "'";//con.prepareStatement("select ");
                                        if (!paramReplacedVal.equalsIgnoreCase("")) {
                                            query = "select " + measureName.trim() + " from " + tableName.trim() + " where target_id=" + targetId + " and t_month='" + updates.get(y) + "' and viewby='" + paramName + "' and " + primColumn + "='" + param_val.get(n) + "' and " + paramReplacedVal;//con.prepareStatement("select ");
                                        }
                                    } else if (minTimeLevel.equalsIgnoreCase("Quarter")) {
                                        query = "select " + measureName.trim() + " from " + tableName.trim() + " where target_id=" + targetId + " and t_qtr='" + updates.get(y) + "' and viewby='" + paramName + "' and " + primColumn + "='" + param_val.get(n) + "'";//con.prepareStatement("select ");
                                        if (!paramReplacedVal.equalsIgnoreCase("")) {
                                            query = "select " + measureName.trim() + " from " + tableName.trim() + " where target_id=" + targetId + " and t_qtr='" + updates.get(y) + "' and viewby='" + paramName + "' and " + primColumn + "='" + param_val.get(n) + "' and " + paramReplacedVal;//con.prepareStatement("select ");
                                        }
                                    }
                                    //////////////////////////////////////////////////////////////////////////////////////////////////////.println(" query-"+query);
                                    PreparedStatement ps = con2.prepareStatement(query);
                                    ResultSet data = ps.executeQuery(query);
                                    PbReturnObject pb = new PbReturnObject(data);

                                    if (pb.getRowCount() > 0) {
                                        oldV = pb.getFieldValueInt(0, 0);
                                    }

                                    //////////////////////////////////////////////////////////////////////////////////////////////////////.println(param_val.get(n).toString()+" oldV "+oldV);
                                    if (oldV != 0) {
                                        lastNonZero = updates.get(y).toString();
                                        lastNonZ.put(param_val.get(n).toString(), lastNonZero);
                                    }
                                    float newV = (oldV * ratio);
                                    int newM = (int) Math.floor(newV);//Math.round(newV);
                                    //////////////////////////////////////////////////////////////////////////////////.println("oldV - "+oldV+" newM -- "+newM);
                                    ratioComb.put(param_val.get(n) + ":" + updates.get(y), Integer.valueOf(oldV));
                                    String dateV = "";
                                    String dateValue = "";

                                }
                            }

                        }
                    }
                }
            }
        }

        //////////////////////////////////////////////////////////////////////////////////////////////////////.println("ratioComb .. "+ratioComb);
        //////////////////////////////////////////////////////////////////////////////////.println(" originalResult "+originalResult);
        //////////////////////////////////////////////////////////////////////////////////.println(" boxNames "+boxNames);
        int totals = 0;
        PreparedStatement ps = null;
        PreparedStatement ps2 = null;
        ratioComb = new HashMap();
        ResultSet rsDate = null;
        PbTargetParamTable pTable = new PbTargetParamTable();
        for (int n = 0; n < param_val.size(); n++) {
            for (int m = 0; m < dateValues.size(); m++) {

                if (individualDatesGroup.containsKey(dateValues.get(m))) {
                    ArrayList updates = (ArrayList) individualDatesGroup.get(dateValues.get(m));
                    //totals=0;
                    if (boxNames.containsKey(param_val.get(n) + ":" + dateValues.get(m))) {
                        if (originalResult.containsKey(param_val.get(n) + ":" + dateValues.get(m))) {
                            int original = ((Integer) originalResult.get(param_val.get(n) + ":" + dateValues.get(m))).intValue();
                            int newVal = ((Integer) (boxNames.get(param_val.get(n) + ":" + dateValues.get(m)))).intValue();

                            Object originalF = originalResult.get(param_val.get(n) + ":" + dateValues.get(m));
                            Object newValF = boxNames.get(param_val.get(n) + ":" + dateValues.get(m));
                            double oriF = Double.parseDouble(originalF.toString());
                            double newF = Double.parseDouble(newValF.toString());
                            // //////////////////////////////////////////////////////////////////////////////////////////////////////.println("originalF - "+originalF+" newValF -"+newValF);

                            double divisionVal = 0;
                            divisionVal = (double) ((newF - oriF) / oriF);
                            float ratio = (float) (1 + divisionVal);
                            //////////////////////////////////////////////////////////////////////////////////////////////////////.println(" -ratio"+ratio);
                            //////////////////////////////////////////////////////////////////////////////////.println(" key "+param_val.get(n) + ":" + dateValues.get(m));
                            //////////////////////////////////////////////////////////////////////////////////.println(divisionVal+" divisionVal - ratio - "+ratio+" original -"+original+" newVal - "+newVal);
                            if ((original != newVal) && (newVal != 0)) {
                                int totalCount = ((Integer) (boxNames.get(param_val.get(n) + ":" + dateValues.get(m)))).intValue();

                                for (int y = 0; y < updates.size(); y++) {
                                    if (minTimeLevel.equalsIgnoreCase("Day")) {
                                        query = "select " + measureName.trim() + " from " + tableName.trim() + " where target_id=" + targetId + " and t_date=to_date('" + updates.get(y) + "','dd-mm-yy') and viewby='" + paramName + "' and " + primColumn + "='" + param_val.get(n) + "'";//con.prepareStatement("select ");
                                        if (!paramReplacedVal.equalsIgnoreCase("")) {
                                            query = "select " + measureName.trim() + " from " + tableName.trim() + " where target_id=" + targetId + " and t_date=to_date('" + updates.get(y) + "','dd-mm-yy') and viewby='" + paramName + "' and " + primColumn + "='" + param_val.get(n) + "' and " + paramReplacedVal;//con.prepareStatement("select ");
                                        }
                                    } else if (minTimeLevel.equalsIgnoreCase("Month")) {
                                        query = "select " + measureName.trim() + " from " + tableName.trim() + " where target_id=" + targetId + " and t_month='" + updates.get(y) + "' and viewby='" + paramName + "' and " + primColumn + "='" + param_val.get(n) + "'";//con.prepareStatement("select ");
                                        if (!paramReplacedVal.equalsIgnoreCase("")) {
                                            query = "select " + measureName.trim() + " from " + tableName.trim() + " where target_id=" + targetId + " and t_month='" + updates.get(y) + "' and viewby='" + paramName + "' and " + primColumn + "='" + param_val.get(n) + "' and " + paramReplacedVal;//con.prepareStatement("select ");
                                        }
                                    } else if (minTimeLevel.equalsIgnoreCase("Quarter")) {
                                        query = "select " + measureName.trim() + " from " + tableName.trim() + " where target_id=" + targetId + " and t_qtr='" + updates.get(y) + "' and viewby='" + paramName + "' and " + primColumn + "='" + param_val.get(n) + "'";//con.prepareStatement("select ");
                                        if (!paramReplacedVal.equalsIgnoreCase("")) {
                                            query = "select " + measureName.trim() + " from " + tableName.trim() + " where target_id=" + targetId + " and t_qtr='" + updates.get(y) + "' and viewby='" + paramName + "' and " + primColumn + "='" + param_val.get(n) + "' and " + paramReplacedVal;//con.prepareStatement("select ");
                                        }
                                    }
                                    ps = null;
                                    ps = con2.prepareStatement(query);
                                    ResultSet data = ps.executeQuery(query);
                                    int oldV = 0;
                                    while (data.next()) {
                                        oldV = data.getInt(1);
                                    }
                                    //////////////////////////////////////////////////////////////////////////////////////////////////////.println(updates.get(y)+" ==oldV "+oldV);
                                    float newV = (oldV * ratio);
                                    int newM = (int) Math.floor(newV);//Math.round(newV);
                                    //////////////////////////////////////////////////////////////////////////////////////////////////////.println("oldV - "+oldV+" newM -- "+newM);
                                    // ratioComb.put("val["+param_val.get(n)+":"+updates.get(y)+"]",Integer.valueOf(oldV));
                                    String dateV = "";
                                    String dateValue = "";
                                    if (minTimeLevel.equalsIgnoreCase("Day")) {
                                        dateV = "select to_char(to_date('" + updates.get(y) + "','dd-mm-yy'),'dd-mm-yy') from dual";
                                        ps2 = con.prepareStatement(dateV);
                                        rsDate = ps2.executeQuery();

                                        while (rsDate.next()) {
                                            dateValue = rsDate.getString(1);
                                        }
                                        ratioComb.put(param_val.get(n) + ":" + dateValue, Integer.valueOf(oldV));
                                    } else if (minTimeLevel.equalsIgnoreCase("Month")) {
                                        ratioComb.put(param_val.get(n) + ":" + updates.get(y), Integer.valueOf(oldV));
                                    }

                                    ratioComb.put(param_val.get(n) + ":" + updates.get(y), Integer.valueOf(oldV));

                                    float v = 0;
                                    /*
                                     * if(y==updates.size()-1) {
                                     * v=(float)(newF-totals); //
                                     * //////////////////////////////////////////////////////////////////////////////////////////////////////.println("
                                     * v -..///- "+v); newM = (int)v; }
                                     */
                                    if (lastNonZero.equalsIgnoreCase(updates.get(y).toString())) {
                                        v = (float) (newF - totals);
                                    }

                                    String k = param_val.get(n).toString();
                                    if (lastNonZ.containsKey(k)) {
                                        String dt = lastNonZ.get(k).toString();
                                        //////////////////////////////////////////////////////////////////////////////////////////////////////.println("dt --- "+dt);
                                        if (updates.get(y).toString().equalsIgnoreCase(dt)) {
                                            v = (float) (newF - totals);

                                            //////////////////////////////////////////////////////////////////////////////////////////////////////.println(" v -..///- //.."+v);
                                            newM = (int) v;
                                            totals = totals - newM;
                                            //////////////////////////////////////////////////////////////////////////////////////////////////////.println("new addition newM .."+newM);
                                            PbTargetValuesParam tParams = new PbTargetValuesParam();
                                            tParams.setMeasureVal(newM);
                                            tParams.setPrimViewByValue(param_val.get(n).toString());
                                            if (!minTimeLevel.equalsIgnoreCase("Day")) {
                                                tParams.setDateVal(updates.get(y).toString());
                                            } else {
                                                tParams.setDateVal(dateValue);
                                            }
                                            tParams.setUpdateFlag("Y");
                                            //float ratio=
                                            //////////////////////////////////////////////////////////////////////////////////////////////////////.println(newM+" ");
                                            //  pTable.addRow(tParams);
                                        }
                                        //////////////////////////////////////////////////////////////////////////////////////////////////////.println(" totals-- "+totals);
                                        if (oldV != 0) {
                                            PbTargetValuesParam tParams = new PbTargetValuesParam();
                                            tParams.setMeasureVal(newM);
                                            tParams.setPrimViewByValue(param_val.get(n).toString());
                                            //  tParams.setDateVal(updates.get(y).toString());
                                            if (!minTimeLevel.equalsIgnoreCase("Day")) {
                                                tParams.setDateVal(updates.get(y).toString());
                                            } else {
                                                tParams.setDateVal(dateValue);
                                            }
                                            tParams.setUpdateFlag("Y");
                                            //float ratio=
                                            //////////////////////////////////////////////////////////////////////////////////////////////////////.println(updates.get(y).toString()+" ... "+newM+"--- "+param_val.get(n).toString());
                                            pTable.addRow(tParams);
                                        }
                                        totals = totals + newM;
                                        // ratioComb.put(param_val.get(n).toString()+":"+updates.get(y).toString(),Integer.valueOf(newM));

                                    }

                                }
                            } else if (newVal == 0 && original != 0) {
                                for (int y = 0; y < updates.size(); y++) {
                                    PbTargetValuesParam tParams = new PbTargetValuesParam();
                                    tParams.setMeasureVal(0);
                                    tParams.setPrimViewByValue(param_val.get(n).toString());
                                    tParams.setUpdateFlag("D");
                                    if (minTimeLevel.equalsIgnoreCase("Day")) {
                                        String dateValue = "";
                                        String dateV = "select to_char(to_date('" + updates.get(y) + "','dd-mm-yy'),'dd-mm-yy') from dual";
                                        ps2 = con.prepareStatement(dateV);
                                        rsDate = ps2.executeQuery();

                                        while (rsDate.next()) {
                                            dateValue = rsDate.getString(1);
                                        }
                                        tParams.setDateVal(dateValue);
                                        //////////////////////////////////////////////////////////////////////////////////.println(" dateValue "+dateValue);
                                    }
                                    tParams.setDateVal(updates.get(y).toString());

                                    pTable.addRow(tParams);
                                    //////////////////////////////////////////////////////////////////////////////////.println(" in delete--- "+updates.get(y).toString());
                                }
                            }

                        }
                    }
                }
            }
        }
        //////////////////////////////////////////////////////////////////////////////////.println(" ratioComb "+ratioComb);
        PbTargetValuesParam targetParam2 = new PbTargetValuesParam();
        targetParam2.setPeriodType(periodType);
        targetParam2.setRatioComb(ratioComb);
        //targetParam2.setRatioComb(originalResult);
        targetParam2.setMinimumTimeLevel(minTimeLevel);
        targetParam2.setTargetId(targetId);
        targetParam2.setNonAllIds(nonAllIds);
        // targetParam.setViewByParameter(viewByName.trim());primaryAnalyze
        targetParam2.setViewByParameter(primaryAnalyze.trim());
        //////////////////////////////////////////////////////////////////////////////////////////////////////.println(" tParams ."+pTable.getRowCount());
        //////////////////////////////////////////////////////////////////////////////////////////////////////.println(" ratioComb-- "+ratioComb);
        sess = new Session();
        sess.setObject(pTable);
        sess.setObject(targetParam2);
        pbDao.saveTargetValues(sess);
        if (primParamEleId.equalsIgnoreCase(primaryAnalyze)) {
            //////////////////////////////////////////////////////////////////////////////////.println(" in if...");
            pbDao.saveForPrimaryParameter(sess);
        }

        if (ps2 != null) {
            ps2.close();
        }
        if (ps != null) {
            ps.close();
        }
        if (rsDate != null) {
            rsDate.close();
        }
        if (con != null) {
            con.close();
        }
        //////////////////////////////////////////////////////////////////////////////////////////////////////.println(lastNonZ+"..lastNonZ ");

        //////////////////////////////////////////////////////////////////////////////////////////////////////.println(" ratioComb //"+ratioComb);
        //////////////////////////////////////////////////////////////////////////////////////////////////////.println(" individualDatesGroup.. "+individualDatesGroup);

        //////////////////////////////////////////////////////////////////////////////////////////////////////.println(" defDates in action.. "+defDates);


        request.setAttribute("TARGETID", targetId);
        request.setAttribute("TIMELEVEL", minTimeLevel);
        PbTargetViewerDb targetViewerDb = null;
        targetViewerDb = new PbTargetViewerDb();
        targetViewerDb.prepareTarget(targetId, minTimeLevel, UserId, request, response);

        return mapping.findForward("showViewTarget");
        //return null;
    }

    public ActionForward getGraph(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession(false);
        String targetId = "";
        String UserId = "";
        String minTimeLevel = "";
        ////////////////////////////////////////////////////////////////////////////////////////////////////////.println("targetId is:: "+targetId);
        if (request.getParameter("targetId") != null) {
            targetId = request.getParameter("targetId");
        }
        if (request.getParameter("minTimeLevel") != null) {
            minTimeLevel = request.getParameter("minTimeLevel");
        }

        //////////////////////////////////////////////////////////////////////////////////////////////////////.println("targetId is::-- "+targetId);
        if (session.getAttribute("USERID") != null) {
            UserId = String.valueOf(session.getAttribute("USERID"));
        }

        String graphPath = "";
        ArrayList columnEdgeValuesA = new ArrayList();
        ArrayList rowEdgeValuesA = new ArrayList();
        String measureName = "";
        request.setAttribute("TARGETID", targetId);
        if (session.getAttribute("columnEdgeValuesA") != null) {
            columnEdgeValuesA = (ArrayList) session.getAttribute("columnEdgeValuesA");
        }
        if (session.getAttribute("rowEdgeValuesA") != null) {
            rowEdgeValuesA = (ArrayList) session.getAttribute("rowEdgeValuesA");
        }
        HashMap boxNames = new HashMap();

        if (session.getAttribute("measureName") != null) {
            measureName = (String) session.getAttribute("measureName");
        }

        Enumeration ee = request.getParameterNames();
        //////////////////////////////////////////////////////////////////////////.println(rowEdgeValuesA+" columnEdgeValuesA "+columnEdgeValuesA);
        while (ee.hasMoreElements()) {
            String reqKey = (String) ee.nextElement();
            String val = request.getParameter(reqKey);
            boxNames.put(reqKey, val);
        }

        //////////////////////////////////////////////////////////////////////////.println(" boxNames --"+boxNames);
        String paramName = rowEdgeValuesA.get(0).toString();
        String[] temp = (String[]) columnEdgeValuesA.toArray(new String[0]);

        String barChartColNames[] = new String[temp.length + 1];
        String barChartColTitles[] = new String[temp.length + 1];
        String[] pieChartColumns = new String[temp.length + 1];
        String viewByColumns[] = new String[1];

        barChartColNames[0] = paramName;
        barChartColTitles[0] = paramName;
        pieChartColumns[0] = paramName;
        viewByColumns[0] = paramName;

        for (int index = 1; index < barChartColNames.length; index++) {
            barChartColNames[index] = temp[index - 1];
            barChartColTitles[index] = temp[index - 1];
            pieChartColumns[index] = temp[index - 1];
        }

        for (int i = 0; i < rowEdgeValuesA.size(); i++) {
            for (int j = 0; j < columnEdgeValuesA.size(); j++) {
                String key = rowEdgeValuesA.get(i).toString() + ":" + columnEdgeValuesA.get(j).toString();
                long newVal = 0;
                if (boxNames.containsKey(key)) {
                    newVal = Long.parseLong((String) boxNames.get(key));
                }
            }
        }
        ArrayList graphArray = new ArrayList();


        PbReturnObject retObj = new PbReturnObject();
        for (int n = 1; n < rowEdgeValuesA.size(); n++) {
            for (int m = 0; m < columnEdgeValuesA.size(); m++) {
                retObj.setFieldValue(columnEdgeValuesA.get(m).toString(), ee);
            }

        }





        for (int n = 1; n < rowEdgeValuesA.size(); n++) {
            HashMap hm = new HashMap();
            hm.put(paramName, rowEdgeValuesA.get(n));
            for (int m = 0; m < columnEdgeValuesA.size(); m++) {
                String key = rowEdgeValuesA.get(n) + ":" + columnEdgeValuesA.get(m);
                if (boxNames.containsKey(key)) {
                    Long val = Long.parseLong(boxNames.get(key).toString());
                    hm.put(columnEdgeValuesA.get(m), new Long(val));
                } else {
                    hm.put(columnEdgeValuesA.get(m), new Long(0));
                }
            }
            graphArray.add(hm);
        }

        //////////////////////////.println(measureName + " measureName graphArray " + graphArray);

        ProgenChartDatasets cDataSet = new ProgenChartDatasets();
        cDataSet.setBarChartColumnNames(barChartColTitles);
        cDataSet.setBarChartColumnTitles(barChartColTitles);
        cDataSet.setViewByColumns(viewByColumns);
        cDataSet.setPieChartColumns(pieChartColumns);

        ProgenChartDisplay chartDisplay = new ProgenChartDisplay(600, 250);
        chartDisplay.setCtxPath(request.getContextPath());
        PrintWriter out = response.getWriter();
        chartDisplay.setAlist(graphArray);
        chartDisplay.setGraph(cDataSet);
        chartDisplay.setChartType("bar");
        chartDisplay.setGrplyaxislabel(measureName);
        chartDisplay.setFunName("");
        chartDisplay.setSession(session);
        chartDisplay.setResponse(response);
        chartDisplay.setOut(out);
        //  String path = chartDisplay.GetCategoryAxisChart(graphArray, cDataSet, "line", "Revenue", " ", session, response, out);
        String path = chartDisplay.GetCategoryAxisChart();

        //////////////////////////.println(" path- " + path);
        String totalPath = "";
        totalPath = "<img align=\"middle\"   src=\"" + request.getContextPath() + "/" + path + "\" border=0 > </img>";
        //////////////////////////.println("totalPath is " + totalPath);

        out.print(totalPath);
        return null;
    }

    public ActionForward getExcelSheet(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession(false);
        String UserId = "";
        String targetId = "";
        if (session.getAttribute("USERID") != null) {
            UserId = String.valueOf(session.getAttribute("USERID"));
        }
        if (session.getAttribute("targetId") != null) {
            targetId = String.valueOf(session.getAttribute("targetId"));
        }

        StrutsUploadAndSaveForm myForm = (StrutsUploadAndSaveForm) form;

        // Process the FormFile
        FormFile myFile = myForm.getTheFile();
        FileOutputStream fos = null;
        //////////////////////////////////////////////////////////////////////////.println("myfile in action --... " + myFile);
        String contentType = myFile.getContentType();
        //Get the file name
        String oldfileName = myFile.getFileName();
        int fileSize = myFile.getFileSize();
        byte[] fileData = myFile.getFileData();

//        for (int i = 0; i < fileData.length; i++) {
//            //////////////////////////////////////////////////////////////////////////.println("fileData\t" + fileData.toString());
//        }
        String newfileName = String.valueOf(Calendar.getInstance().getTimeInMillis());
        //////////////////////////////////////////////////////////////////////////.println("File path is--0-w0-ew0-e0-w0e " + getServlet().getServletContext().getRealPath("/") + newfileName + ".xls");
        File tempFile = new File(getServlet().getServletContext().getRealPath("/") + newfileName + ".xls");
        if (!tempFile.exists()) {
            tempFile.createNewFile();
            fos = new FileOutputStream(tempFile);
            fos.write(fileData);
        } else {
            fos = new FileOutputStream(tempFile);
            fos.write(fileData);
        }
        //////////////////////////////////////////////////////////////////////////.println("tempFile created");

        if (tempFile.exists()) {
            //////////////////////////////////////////////////////////////////////////.println("File exits or newly created ");
//            //////////////////////////////////////////////////////////////////////////.println(" File deleted is " + tempFile.delete());
        }
        //////////////////////////////////////////////////////////////////////////.println("tempFile is. for quartz... " + tempFile);
        ExcelScheduler es = new ExcelScheduler(targetId, UserId, myFile.toString(), tempFile.toString());
        //ts.trackersendMail();


        /*
         * response.setContentType("application/x-download");
         * response.setHeader("Content-Disposition",
         * "attachment;filename=downnload.xls");
         *
         * response.getOutputStream().write(fileData);
         */

        /*
         * StrutsUploadAndSaveForm myForm = (StrutsUploadAndSaveForm)form;
         *
         * // Process the FormFile FormFile myFile = myForm.getTheFile();
         * //////////////////////////////////////////////////////////////////////////.println("myfile
         * " + myFile); FileOutputStream fos = null;
         * //////////////////////////////////////////////////////////////////////////.println("myfile"
         * + myFile); String contentType = myFile.getContentType(); //Get the
         * file name String oldfileName = myFile.getFileName(); //int fileSize =
         * myFile.getFileSize(); byte[] fileData = myFile.getFileData(); String
         * newfileName =
         * String.valueOf(Calendar.getInstance().getTimeInMillis());
         * //////////////////////////////////////////////////////////////////////////.println("File
         * path is " + getServlet().getServletContext().getRealPath("/") +
         * newfileName + ".xls"); File tempFile = new
         * File(getServlet().getServletContext().getRealPath("/") + newfileName
         * + ".xls");
         *
         * if (!tempFile.exists()) { tempFile.createNewFile(); fos = new
         * FileOutputStream(tempFile); fos.write(fileData); } else { fos = new
         * FileOutputStream(tempFile); fos.write(fileData); }
         *
         *
         *
         *
         * // if (!tempFile.exists()) { // tempFile.createNewFile(); // }
         * //////////////////////////////////////////////////////////////////////////.println("tempFile
         * created ");
         *
         * if (tempFile.exists()) {
         * //////////////////////////////////////////////////////////////////////////.println("File
         * exits or newly created "); //
         * //////////////////////////////////////////////////////////////////////////.println("
         * File deleted is " + tempFile.delete());
         *
         * }
         */

        //////////////////////////////////////////////////////////////////////////.println("tempFile is ----..." + tempFile);


        /*
         * request.setAttribute("fileName", oldfileName);
         *
         *
         * //return mapping.findForward("success");
         * //////////////////////////////////////////////////////////////////////////.println("
         * newfileName l;l ;l "+newfileName);
         *
         * HashMap excelvalues = new HashMap(); HttpSession session =
         * request.getSession(false);
         *
         * ReadXLSheet rxl = new ReadXLSheet();
         * //////////////////////////////////////////////////////////////////////////.println("
         * before sjkdsjkdj "+newfileName); HashMap all = (HashMap)
         * rxl.init(newfileName); excelvalues = (HashMap)
         * all.get("excelvalues");
         *
         *
         * String filName = "";
         * //////////////////////////////////////////////////////////////////////////.println("
         * excelvalues in mm " + excelvalues); PbDb pbdb = new PbDb(); ArrayList
         * al = new ArrayList(); String insertStatusQ = "insert into
         * excelSheet_uplaod(UPLOADNUMBER,EXCEL_SHEET_NAME,UPLOAD_TIME,STATUS)
         * values(excelSheet_uplaod_ID_SEQ.nextval,'" + filName +
         * "',sysdate,'Uploaded')";
         * //////////////////////////////////////////////////////////////////////////.println("
         * insertStatusQ " + insertStatusQ); al.add(insertStatusQ); try {
         * pbdb.executeMultiple(al); } catch (Exception e) {
         * logger.error("Exception:",e); } //ArrayList hskeylist=(ArrayList)
         * all.get("hskeylist"); //
         * //////////////////////////////////////////////////////////////////////////.println(excelvalues+"
         * in excel read "+hskeylist);
         *
         * String UserId = ""; String minTimeLevel = "";
         *
         * PbTargetDAO tDao = new PbTargetDAO(); String targetId = "";
         * PbReturnObject fileDet = tDao.getExcelFileDetail(filName); String
         * colEdVals = ""; String rowEdVals = ""; String primA = ""; String secA
         * = ""; String sRange = ""; String nonAll = ""; String colViewByElement
         * = "";
         *
         * if (fileDet.getRowCount() > 0) { targetId =
         * fileDet.getFieldValueString(0, "TARGET_ID"); minTimeLevel =
         * fileDet.getFieldValueString(0, "PERIODTYPE"); colEdVals =
         * fileDet.getFieldValueClobString(0, "COLUMNVIEWBYVALUES"); rowEdVals =
         * fileDet.getFieldValueClobString(0, "ROWVIEWBYVALUES"); primA =
         * fileDet.getFieldValueString(0, "PRIMARYVIEWBY"); secA =
         * fileDet.getFieldValueString(0, "SECVIEWBY"); sRange =
         * fileDet.getFieldValueString(0, "TIMEVAL"); nonAll =
         * fileDet.getFieldValueClobString(0, "NONALLVALUE");
         *
         * }
         * String startRange = sRange; colViewByElement = secA;
         *
         * //////////////////////////////////////////////////////////////////////////.println("targetId
         * is:: in save " + targetId); if (session.getAttribute("USERID") !=
         * null) { UserId = String.valueOf(session.getAttribute("USERID")); }
         * HashMap originalResult = new HashMap(); ArrayList columnEdgeValuesA =
         * new ArrayList(); ArrayList rowEdgeValuesA = new ArrayList(); String
         * secAnalyze = secA; String primaryAnalyze = "";
         *
         * String colEdValsArr[] = colEdVals.split("~"); String rowEdValsArr[] =
         * rowEdVals.split("~"); for (int y = 0; y < colEdValsArr.length; y++) {
         * columnEdgeValuesA.add(colEdValsArr[y]); }
         *
         * for (int y = 0; y < rowEdValsArr.length; y++) {
         * rowEdgeValuesA.add(rowEdValsArr[y]); }
         *
         * String dataQuery = ""; String viewByElement = primA; String
         * periodType = minTimeLevel; String overallT = ""; String targetQ =
         * "select target_table_id,pgbt.buss_table_name,ptm.measure_name from
         * prg_target_master ptm," + " prg_grp_buss_table pgbt where
         * prg_measure_id in (select measure_id from target_master WHERE " + "
         * target_master.target_id=" + targetId + ") and pgbt.buss_table_id =
         * ptm.target_table_id";
         * //////////////////////////////////////////////////////////////////////////.println("
         * targetQ " + targetQ);
         *
         * PbReturnObject pbro = pbdb.execSelectSQL(targetQ); String measureName
         * = pbro.getFieldValueString(0, "MEASURE_NAME"); String targetTable =
         * pbro.getFieldValueString(0, "BUSS_TABLE_NAME"); String viewName = "";
         * //////////////////////////////////////////////////////////////////////////.println("
         * primA " + primA); if (primA.equalsIgnoreCase("0")) { viewName =
         * "OverAll"; } String allParametrsFilterClause = "";
         * //////////////////////////////////////////////////////////////////////////.println(periodType
         * + "-=-=-=-=-=--=- viewByElement " + viewByElement + "
         * colViewByElement " + colViewByElement); HashMap elementCols = new
         * HashMap(); PbReturnObject pbro2 = new PbReturnObject(); String
         * getBussColNames = "select DISTINCT element_id, buss_col_name from
         * prg_user_all_info_details where folder_id " + " in(select
         * bus_folder_id from target_measure_folder where bus_group_id=(select
         * business_group from prg_target_master" + " where prg_measure_id
         * in(select measure_id from target_master WHERE
         * target_master.target_id=&)))";
         *
         * Object tarOb[] = new Object[1]; tarOb[0] = targetId; String
         * fingetBussColNames = pbdb.buildQuery(getBussColNames, tarOb);
         * //////////////////////////////////////////////////////////////////////////.println("
         * fingetBussColNames " + fingetBussColNames); pbro2 =
         * pbdb.execSelectSQL(fingetBussColNames); for (int i = 0; i <
         * pbro2.getRowCount(); i++) {
         * elementCols.put(pbro2.getFieldValueString(i, "ELEMENT_ID"),
         * pbro2.getFieldValueString(i, "BUSS_COL_NAME")); //
         * viewByNames.put(pbro.getFieldValueString(i,"ELEMENT_ID"),pbro.getFieldValueString(i,"PARAM_DISP_NAME"));
         * }
         * //////////////////////////////////////////////////////////////////////////.println("
         * elementCols " + elementCols); HashMap viewByNames = new HashMap();
         * String getDispNames = "select distinct element_id,param_disp_name
         * from prg_target_param_details where target_id=&"; String
         * fingetDispNames = pbdb.buildQuery(getDispNames, tarOb);
         * //////////////////////////////////////////////////////////////////////////.println("
         * fingetDispNames " + fingetDispNames); PbReturnObject pbroDispNames =
         * pbdb.execSelectSQL(fingetDispNames); for (int m = 0; m <
         * pbroDispNames.getRowCount(); m++) {
         * viewByNames.put(pbroDispNames.getFieldValueString(m, "ELEMENT_ID"),
         * pbroDispNames.getFieldValueString(m, "PARAM_DISP_NAME")); }
         * //////////////////////////////////////////////////////////////////////////.println("
         * viewByNames.//./ " + viewByNames + " nonAll " + nonAll); HashMap
         * nonAllIds = new HashMap(); String viewByName = ""; String nonAllArr[]
         * = nonAll.split("~"); for (int m = 0; m < nonAllArr.length; m++) {
         * String val[] = nonAllArr[m].split("="); nonAllIds.put(val[0],
         * val[1]); }
         * //////////////////////////////////////////////////////////////////////////.println("
         * nonAllIds " + nonAllIds); for (int l = 0; l < nonAllArr.length; l++)
         * { String val[] = nonAllArr[l].split("="); String a = (String)
         * nonAllIds.get(val[0]);
         * //////////////////////////////////////////////////////////////////////////.println(nonAllArr[l]
         * + " a " + a); if (!a.equalsIgnoreCase("-1")) { if
         * (!a.equalsIgnoreCase("All")) { allParametrsFilterClause =
         * allParametrsFilterClause + " and " + elementCols.get(val[0]) + "='" +
         * a + "'"; } } }
         * //////////////////////////////////////////////////////////////////////////.println("
         * allParametrsFilterClause " + allParametrsFilterClause + " secA " +
         * secA); if (viewByNames.containsKey(viewByElement)) { overallT =
         * (String) elementCols.get(viewByElement); viewName = (String)
         * viewByNames.get(viewByElement); }
         *
         * String secCol = ""; String secName = ""; if
         * (viewByNames.containsKey(secA)) { secCol = (String)
         * elementCols.get(secA); secName = (String) viewByNames.get(secA); }
         * String dataQuery2 = "";
         * //////////////////////////////////////////////////////////////////////////.println("
         * viewByElement in excel " + viewByElement + " secAnalyze " +
         * secAnalyze); if (!viewByElement.equalsIgnoreCase("0")) { if
         * (secAnalyze.equalsIgnoreCase("Time")) {
         * //////////////////////////////////////////////////////////////////////////.println("
         * in if 1 --"); if (periodType.equalsIgnoreCase("Day")) { dataQuery =
         * "select " + overallT + ",sum(" + measureName.trim() +
         * "),to_char(t_date,'dd-mm-yy') from " + targetTable.trim() + " where
         * target_id='" + targetId + "' and viewby='" + viewName + "' " +
         * allParametrsFilterClause + " and t_date is not null group by " +
         * overallT + ",to_char(t_date,'dd-mm-yy') order by " + overallT +
         * ",to_char(t_date,'dd-mm-yy')"; } else if
         * (periodType.equalsIgnoreCase("Month")) { dataQuery = "select " +
         * overallT + ",sum(" + measureName.trim() + "),t_month from " +
         * targetTable.trim() + " where target_id='" + targetId + "' and
         * viewby='" + viewName + "' " + allParametrsFilterClause + " and
         * t_month is not null group by " + overallT + ",t_month order by " +
         * overallT + ",t_month"; } //dataQuery ="select
         * "+elementCols.get(viewByElement.trim()).toString()+",sum("+measureName.trim()+"),t_month
         * from "+targetTable.trim()+" where target_id='"+targetId+"' and
         * viewby='"+viewByNames.get(viewByElement.trim()).toString()+"'"+allParametrsFilterClause+"
         * group by "+elementCols.get(viewByElement.trim()).toString()+",t_month
         * order by
         * "+elementCols.get(viewByElement.trim()).toString()+",t_month"; else
         * if (periodType.equalsIgnoreCase("Quarter")) { dataQuery = "select " +
         * overallT + ",sum(" + measureName.trim() + "),t_qtr from " +
         * targetTable.trim() + " where target_id='" + targetId + "' and t_qtr
         * is not null " + allParametrsFilterClause + " " + " and viewby='" +
         * viewName + "' group by " + overallT + ", t_qtr order by " + overallT
         * + ",t_qtr"; } else if (periodType.equalsIgnoreCase("Year")) {
         * dataQuery = "select " + overallT + ",sum(" + measureName.trim() +
         * "),t_year from " + targetTable.trim() + " where target_id='" +
         * targetId + "' and viewby='" + viewName + "' " +
         * allParametrsFilterClause + " and t_year is not null group by " +
         * overallT + ",t_year order by " + overallT + ",t_year"; } } else { if
         * (periodType.equalsIgnoreCase("Year")) {
         * //////////////////////////////////////////////////////////////////////////.println("
         * in if year ");
         * //////////////////////////////////////////////////////////////////////////.println(elementCols
         * + " viewByElement " + viewByElement + " colViewByElement " +
         * colViewByElement + " startRange " + startRange); //dataQuery ="select
         * "+overallT+",sum("+measureName.trim()+"),t_year from
         * "+targetTable.trim()+" where target_id='"+targetId+"' and
         * viewby='"+viewName+"' and secviewby='' "+ allParametrsFilterClause+"
         * and t_year is not null and t_year='"+startRange+"' group by
         * "+overallT+",t_year order by "+overallT+",t_year"; dataQuery =
         * "select " + elementCols.get(viewByElement.trim()).toString() +
         * ",sum(" + measureName.trim() + ")," +
         * elementCols.get(colViewByElement.trim()).toString() + " from " +
         * targetTable.trim() + " where target_id='" + targetId + "' and
         * viewby='" + viewByNames.get(viewByElement.trim()).toString() + "' and
         * secviewby='" + viewByNames.get(colViewByElement.trim()).toString() +
         * "' and t_year='" + startRange + "' " + allParametrsFilterClause + "
         * group by " + elementCols.get(viewByElement.trim()).toString() + "," +
         * elementCols.get(colViewByElement.trim()).toString() + " order by " +
         * elementCols.get(viewByElement.trim()).toString() + "," +
         * elementCols.get(colViewByElement.trim()).toString(); dataQuery2 =
         * "select " + elementCols.get(viewByElement.trim()).toString() +
         * ",sum(" + measureName.trim() + ")," +
         * elementCols.get(colViewByElement.trim()).toString() + " from " +
         * targetTable.trim() + " where target_id='" + targetId + "' and
         * viewby='" + viewByNames.get(colViewByElement.trim()).toString() + "'
         * and secviewby='" + viewByNames.get(viewByElement.trim()).toString() +
         * "' and t_year='" + startRange + "' " + allParametrsFilterClause + "
         * group by " + elementCols.get(viewByElement.trim()).toString() + "," +
         * elementCols.get(colViewByElement.trim()).toString() + " order by " +
         * elementCols.get(viewByElement.trim()).toString() + "," +
         * elementCols.get(colViewByElement.trim()).toString(); }
         *
         * }
         * } else { if (viewByElement.equalsIgnoreCase("0")) {
         * //////////////////////////////////////////////////////////////////////////.println(periodType
         * + " in if overall "); ArrayList paramVals = new ArrayList();
         * paramVals.add("OverAll Target"); paramVals.add("OverAll"); }
         *
         * if (periodType.equalsIgnoreCase("Day")) { dataQuery = "select sum(" +
         * measureName.trim() + "),to_char(t_date,'dd-mm-yy') from " +
         * targetTable.trim() + " where target_id='" + targetId + "' and
         * viewby='OverAll' and t_date is not null group by
         * to_char(t_date,'dd-mm-yy') order by to_char(t_date,'dd-mm-yy')"; }
         * else if (periodType.equalsIgnoreCase("Month")) { dataQuery = "select
         * sum(" + measureName.trim() + "),t_month from " + targetTable.trim() +
         * " where target_id='" + targetId + "' and viewby='OverAll' and t_month
         * is not null group by t_month order by t_month"; } else if
         * (periodType.equalsIgnoreCase("Quarter") ||
         * periodType.equalsIgnoreCase("QTR")) { dataQuery = "select sum(" +
         * measureName.trim() + "),t_qtr from " + targetTable.trim() + " where
         * target_id='" + targetId + "' and viewby='OverAll' and t_qtr is not
         * null group by t_qtr order by t_qtr"; } else if
         * (periodType.equalsIgnoreCase("Year")) { dataQuery = "select sum(" +
         * measureName.trim() + "),t_year from " + targetTable.trim() + " where
         * target_id='" + targetId + "' and viewby='OverAll' and t_year is not
         * null group by t_year order by t_year"; }
         *
         * }
         * //////////////////////////////////////////////////////////////////////////.println("
         * dataQuery in excelupload " + dataQuery);
         * //////////////////////////////////////////////////////////////////////////.println("
         * dataQuery2 in excelupload " + dataQuery2); Connection con =
         * ProgenConnection.getCustomerConn(); //
         * if(elementCols.containsKey(viewByElement.trim())) // {
         * PreparedStatement ps = con.prepareStatement(dataQuery); ResultSet rs
         * = ps.executeQuery(); // } PbReturnObject allD = new PbReturnObject();
         * if (rs != null) { allD = new PbReturnObject(rs); } HashMap primH =
         * new HashMap(); String oldPv = ""; String newPv = ""; HashMap allV =
         * new HashMap(); if (!viewByElement.equalsIgnoreCase("0")) { for (int n
         * = 0; n < allD.getRowCount(); n++) { oldPv =
         * allD.getFieldValueString(n, 0); //
         * //////////////////////////////////////////////////////////////////////////////////////////////////////.println("
         * oldPv "+oldPv); if (newPv.equalsIgnoreCase("")) { newPv = oldPv; }
         * else if (!newPv.trim().equalsIgnoreCase(oldPv.trim()) &&
         * newPv.length() > 0) { allV.put(newPv, primH); primH = new HashMap();
         * newPv = oldPv; } String dat = allD.getFieldValueString(n, 2); long
         * mValue = Long.parseLong(allD.getFieldValueString(n, 1));
         * primH.put(dat, new Long(mValue)); if (n == (allD.getRowCount() - 1))
         * { allV.put(oldPv, primH); } //
         * //////////////////////////////////////////////////////////////////////////////////////////////////////.println("
         * primH "+primH); } } else { oldPv = "OverAll";
         * //////////////////////////////////////////////////////////////////////////.println("
         * in overall hm ");
         * //////////////////////////////////////////////////////////////////////////.println("
         * count " + allD.getRowCount()); primH = new HashMap(); for (int n = 0;
         * n < allD.getRowCount(); n++) {
         * //////////////////////////////////////////////////////////////////////////.println("
         * in allD"); if (newPv.equalsIgnoreCase("")) { newPv = oldPv; }
         *
         * String dat = allD.getFieldValueString(n, 1); long mValue =
         * Long.parseLong(allD.getFieldValueString(n, 0)); primH.put(dat, new
         * Long(mValue)); allV.put(oldPv, primH);
         *
         * }
         * }
         * //////////////////////////////////////////////////////////////////////////.println("
         * allV in upload " + allV);
         * //////////////////////////////////////////////////////////////////////////.println("
         * rowEdgeValuesA " + rowEdgeValuesA);
         *
         * for (int x = 1; x < rowEdgeValuesA.size(); x++) { String pVal =
         * rowEdgeValuesA.get(x).toString(); HashMap dt = new HashMap(); if
         * (allV.containsKey(pVal)) { dt = (HashMap) allV.get(pVal); } for (int
         * y = 0; y < columnEdgeValuesA.size(); y++) { String timeV =
         * columnEdgeValuesA.get(y).toString(); long mV = 0; if
         * (dt.containsKey(timeV)) { mV = ((Long) dt.get(timeV)).longValue(); }
         * String value = ""; if (mV != 0) { value = "" + mV + ""; }
         * originalResult.put(rowEdgeValuesA.get(x) + ":" + timeV, new
         * Long(mV)); } }
         *
         *
         *
         * //////////////////////////////////////////////////////////////////////////.println("originalResult-.
         * is:: " + originalResult);
         *
         *
         *
         * primaryAnalyze = primA; secAnalyze = secA; startRange = sRange;
         *
         *
         *
         *
         * String primParamEleId = ""; if
         * (session.getAttribute("primParamEleId") != null) { primParamEleId =
         * (String) session.getAttribute("primParamEleId"); }
         *
         *
         *
         * HashMap boxNames = new HashMap(); Enumeration ee =
         * request.getParameterNames(); while (ee.hasMoreElements()) { String
         * reqKey = (String) ee.nextElement(); String val =
         * request.getParameter(reqKey); if (reqKey.startsWith("CBOARP")) {
         * String elementId = reqKey.substring(6); if
         * (val.equalsIgnoreCase("All")) { nonAllIds.put(elementId, "-1"); }
         * else { nonAllIds.put(elementId, val); } } if
         * (reqKey.startsWith("CBOVIEW_BY")) { String k = reqKey.substring(10);
         * viewByName = request.getParameter(reqKey); } long mValue = 0;
         * //////////////////////////////////////////////////////////////////////////.println("
         * originalResult " + originalResult); if
         * (originalResult.containsKey(reqKey)) { if (!val.equalsIgnoreCase(""))
         * { mValue = Long.parseLong(val); } boxNames.put(reqKey, new
         * Long(mValue)); } } // boxNames=excelvalues; String allBox[] =
         * (String[]) excelvalues.keySet().toArray(new String[0]); String k =
         * ""; String v = ""; long value = 0; for (int m = 0; m < allBox.length;
         * m++) { k = allBox[m]; value = 0; v = (String) excelvalues.get(k); if
         * (!v.equalsIgnoreCase("")) { value = Long.parseLong(v); }
         * boxNames.put(k.trim(), new Long(value)); }
         * //////////////////////////////////////////////////////////////////////////.println(nonAllIds
         * + " - boxNames " + boxNames);
         * //////////////////////////////////////////////////////////////////////////.println("
         * startRange... " + startRange);
         * //////////////////////////////////////////////////////////////////////////.println("
         * rowEdgeValuesA ." + rowEdgeValuesA + " columnEdgeValuesA " +
         * columnEdgeValuesA + " viewByName.. " + viewByName); if
         * (secAnalyze.equalsIgnoreCase("Time")) { PbTargetParamTable pTable =
         * new PbTargetParamTable(); PbTargetValuesParam targetParam = new
         * PbTargetValuesParam(); Session sess = new Session(); PbTargetDAO
         * pbDao = new PbTargetDAO();
         *
         * targetParam.setPeriodType(minTimeLevel);
         * targetParam.setRatioComb(originalResult);
         * targetParam.setMinimumTimeLevel(minTimeLevel);
         * targetParam.setTargetId(targetId);
         * targetParam.setNonAllIds(nonAllIds); //
         * targetParam.setViewByParameter(viewByName.trim());primaryAnalyze
         * //////////////////////////////////////////////////////////////////////////.println("
         * primaryAnalyze in action " + primaryAnalyze);
         * targetParam.setViewByParameter(primaryAnalyze.trim());
         *
         * for (int i = 0; i < rowEdgeValuesA.size(); i++) { for (int j = 0; j <
         * columnEdgeValuesA.size(); j++) { String key =
         * rowEdgeValuesA.get(i).toString().trim() + ":" +
         * columnEdgeValuesA.get(j).toString().trim(); long newVal = 0; long
         * oldVal = 0; if (boxNames.containsKey(key)) { newVal = ((Long)
         * boxNames.get(key)).longValue(); } if
         * (originalResult.containsKey(key)) { oldVal = ((Long)
         * originalResult.get(key)).longValue(); }
         * //////////////////////////////////////////////////////////////////////////.println(newVal
         * + " oldVal " + oldVal); // when new is not!=0 and original update if
         * (newVal != 0 && (newVal != oldVal) && oldVal != 0) {
         * PbTargetValuesParam targetParam2 = new PbTargetValuesParam();
         * targetParam2.setMeasureVal(newVal);
         * targetParam2.setDateVal(columnEdgeValuesA.get(j).toString().trim());
         * targetParam2.setUpdateFlag("Y");
         * targetParam2.setPrimViewByValue(rowEdgeValuesA.get(i).toString().trim());
         * //////////////////////////////////////////////////////////////////////////.println(key
         * + " update " + newVal + " oldVal " + oldVal);
         * pTable.addRow(targetParam2); } // when new is not!=0 and original
         * delete if (newVal == 0 && oldVal != 0) { PbTargetValuesParam
         * targetParam2 = new PbTargetValuesParam();
         * targetParam2.setMeasureVal(newVal);
         * targetParam2.setDateVal(columnEdgeValuesA.get(j).toString());
         * targetParam2.setUpdateFlag("D"); targetParam2.setDeleteFlag("D");
         * targetParam2.setPrimViewByValue(rowEdgeValuesA.get(i).toString());
         * //////////////////////////////////////////////////////////////////////////.println(key
         * + " delete " + newVal + " oldVal " + oldVal);
         * pTable.addRow(targetParam2); } // when new is not!=0 and original 0
         * if (newVal != 0 && oldVal == 0) { PbTargetValuesParam targetParam2 =
         * new PbTargetValuesParam(); targetParam2.setMeasureVal(newVal);
         * targetParam2.setDateVal(columnEdgeValuesA.get(j).toString());
         * targetParam2.setUpdateFlag("N");
         * targetParam2.setPrimViewByValue(rowEdgeValuesA.get(i).toString());
         * //////////////////////////////////////////////////////////////////////////.println(key
         * + " insert " + newVal + " oldVal " + oldVal);
         * pTable.addRow(targetParam2); } } } sess.setObject(targetParam);
         * sess.setObject(pTable);
         * //////////////////////////////////////////////////////////////////////////.println("
         * pTable " + pTable.getRowCount() + " -- " + primaryAnalyze); if
         * (pTable.getRowCount() > 0) { // pbDao.saveTargetValues(sess); if
         * (primaryAnalyze.trim().equalsIgnoreCase("OverAll")) {
         * //////////////////////////////////////////////////////////////////////////.println("
         * in overall save-. "); pbDao.saveTargetValuesNewForOverAll(sess);
         *
         * } else {
         * //////////////////////////////////////////////////////////////////////////.println("
         * in other save-. "); pbDao.saveTargetValuesNew(sess);
         *
         * }
         * if (primParamEleId.equalsIgnoreCase(primaryAnalyze)) { //
         * pbDao.saveForPrimaryParameter(sess); } } } else { PbTargetParamTable
         * pTable = new PbTargetParamTable(); PbTargetValuesParam targetParam =
         * new PbTargetValuesParam(); Session sess = new Session(); PbTargetDAO
         * pbDao = new PbTargetDAO();
         *
         * targetParam.setPeriodType(minTimeLevel);
         * targetParam.setMinimumTimeLevel(minTimeLevel);
         * targetParam.setTargetId(targetId);
         * targetParam.setNonAllIds(nonAllIds);
         * targetParam.setRatioComb(originalResult); //
         * targetParam.setViewByParameter(viewByName.trim());primaryAnalyze
         * targetParam.setPrimaryParameter(primaryAnalyze);
         * targetParam.setSecViewByParameter(colViewByElement);
         * targetParam.setViewByParameter(viewByElement);
         * //////////////////////////////////////////////////////////////////////////.println("
         * originalResult " + originalResult);
         *
         * for (int i = 0; i < rowEdgeValuesA.size(); i++) { for (int j = 0; j <
         * columnEdgeValuesA.size(); j++) { String key =
         * rowEdgeValuesA.get(i).toString().trim() + ":" +
         * columnEdgeValuesA.get(j).toString().trim(); long newVal = 0; long
         * oldVal = 0; if (boxNames.containsKey(key)) { newVal = ((Long)
         * boxNames.get(key)).longValue(); } if
         * (originalResult.containsKey(key)) { oldVal = ((Long)
         * originalResult.get(key)).longValue(); }
         * //////////////////////////////////////////////////////////////////////////.println(newVal
         * + " oldVal-- " + oldVal + " ;;; " + key); // when new is not!=0 and
         * original update if (newVal != 0 && (newVal != oldVal) && oldVal != 0)
         * { PbTargetValuesParam targetParam2 = new PbTargetValuesParam();
         * targetParam2.setMeasureVal(newVal);
         * targetParam2.setDateVal(startRange); targetParam2.setUpdateFlag("Y");
         * // targetParam2.setPrimViewByValue(rowEdgeValuesA.get(i).toString());
         * targetParam2.setPrimViewByValue(columnEdgeValuesA.get(j).toString().trim());
         * targetParam2.setPrimAnalyzeVal(rowEdgeValuesA.get(i).toString().trim());
         * //////////////////////////////////////////////////////////////////////////.println(key
         * + " update " + newVal + " oldVal " + oldVal);
         * pTable.addRow(targetParam2); } // when new is not!=0 and original
         * delete if (newVal == 0 && oldVal != 0) { PbTargetValuesParam
         * targetParam2 = new PbTargetValuesParam();
         * targetParam2.setMeasureVal(newVal);
         * targetParam2.setDateVal(startRange); targetParam2.setUpdateFlag("D");
         * targetParam2.setPrimAnalyzeVal(rowEdgeValuesA.get(i).toString().trim());
         * targetParam2.setDeleteFlag("D"); //
         * targetParam2.setPrimViewByValue(rowEdgeValuesA.get(i).toString());
         * targetParam2.setPrimViewByValue(columnEdgeValuesA.get(j).toString().trim());
         * //////////////////////////////////////////////////////////////////////////.println(key
         * + " delete " + newVal + " oldVal " + oldVal);
         * pTable.addRow(targetParam2); } // when new is not!=0 and original 0
         * if (newVal != 0 && oldVal == 0) { PbTargetValuesParam targetParam2 =
         * new PbTargetValuesParam(); targetParam2.setMeasureVal(newVal);
         * targetParam2.setDateVal(startRange); targetParam2.setUpdateFlag("N");
         * targetParam2.setPrimAnalyzeVal(rowEdgeValuesA.get(i).toString().trim());
         * // targetParam2.setPrimViewByValue(rowEdgeValuesA.get(i).toString());
         * targetParam2.setPrimViewByValue(columnEdgeValuesA.get(j).toString().trim());
         * //////////////////////////////////////////////////////////////////////////.println(key
         * + " insert-- " + newVal + " oldVal " + oldVal);
         * pTable.addRow(targetParam2); } } } sess.setObject(targetParam);
         * sess.setObject(pTable);
         * //////////////////////////////////////////////////////////////////////////.println("
         * pTable,.,.,;; " + pTable.getRowCount()); if (pTable.getRowCount() >
         * 0) { pbDao.savePrimSecNew(sess); }
         *
         *
         * }
         *
         * //This is the code to deleted the uploaded file
         * response.setContentType("application/x-download");
         * response.setHeader("Content-Disposition",
         * "attachment;filename=downnload.xls");
         * response.getOutputStream().write(fileData);
         */
        return null;
    }

    public ActionForward checkToGetSheet(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {




        PrintWriter out = response.getWriter();
        String excelpath = request.getParameter("excelname");
        //////////////////////////////////////////////////////////////////////////.println(" excelpath in check upload st " + excelpath);
        PbTargetDAO pDao = new PbTargetDAO();
        String status = pDao.getUploadStatus(excelpath);

        out.print(status);
        out.println("hello");
//        //////////////////////////////////////////////////////////////////////////.println(" status -- " + status);

        return null;
    }

    /**
     * Provides the mapping from resource key to method name.
     *
     * @return Resource key / method name map.
     */
    protected Map getKeyMethodMap() {
        //////////////////////////////////////////////////////////////////////////////////////////////////////.println(" in map.. //");
        Map map = new HashMap();
        map.put("viewTarget", "viewTarget");
        map.put("saveTarget", "saveTarget"); //getGraph allocateTarget viewTargetForView
        map.put("getGraph", "getGraph");
        map.put("allocateTarget", "allocateTarget");
        map.put("viewTargetForView", "viewTargetForView");
        map.put("getExcelSheet", "getExcelSheet");
        map.put("checkToGetSheet", "checkToGetSheet");
        return map;
    }
}
