/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.handsontable.action;

import com.progen.handsontable.bd.HandsonTableBD;
import com.progen.handsontable.bd.HandsontableDetails;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.LookupDispatchAction;
import prg.db.Container;
import prg.db.PbReturnObject;

/**
 *
 * @author progen
 */
public class HandsonTableAction extends LookupDispatchAction {

    public static Logger logger = Logger.getLogger(HandsonTableAction.class);

    protected Map getKeyMethodMap() {
        Map map = new HashMap();
        map.put("getReportTableHeaders", "getReportTableHeaders");
        map.put("buildFormulaColumn", "buildFormulaColumn");
        map.put("removeHandsontableSession", "removeHandsontableSession");
        map.put("builColumnFilter", "builColumnFilter");
        map.put("applyingColFilter", "applyingColFilter");
        map.put("getReportFormula", "getReportFormula");
        return map;
    }

    public ActionForward getReportTableHeaders(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(false);
        String hotFilePath = (String) session.getAttribute("advHtmlFileProps");
        HashMap map = null;
        Container container = null;
        String reportId = request.getParameter("reportId");
        String[] rowDataList = request.getParameterValues("handsonData");
//           String data = request.getParameter("data");
        String calheaders = request.getParameter("calheaders");
        String[] headerNames = calheaders.split(",");
//        String[] rowDataList = rowList.split(",");

        map = (HashMap) session.getAttribute("PROGENTABLES");
        container = (Container) map.get(reportId);
        String folderId = container.getFolderIdsForFact();
        String userId = session.getAttribute("USERID").toString();
        String reportName = container.getReportDesc();

        List tableMeasers = new ArrayList();
        List handsontableData = new ArrayList();
//           tableMeasers = container.getDisplayLabels();
        handsontableData.addAll(Arrays.asList(rowDataList));
        tableMeasers.addAll(Arrays.asList(headerNames));

        HandsonTableBD hotBd = new HandsonTableBD();
        Map<String, HandsontableDetails> handsontableFormula = (HashMap<String, HandsontableDetails>) session.getAttribute("HOTFORMULA");
        HandsontableDetails hotDetails = new HandsontableDetails();
        HashMap formaulMap = null;
        if (handsontableFormula != null) {
            hotDetails = handsontableFormula.get(reportId);
            if (hotDetails != null) {
                formaulMap = hotDetails.formulaMap;
            }
        }

        hotDetails = new HandsontableDetails();
        hotDetails.setReportId(reportId);
        hotDetails.setReportName(reportName);
        hotDetails.setUserId(userId);
        hotDetails.measureNames.addAll(tableMeasers);
        hotDetails.reportTableData.addAll(handsontableData);
        hotDetails.formulaMap = formaulMap;

        String hotFileName = "";
        PbReturnObject retObj = null;

        retObj = hotBd.getHandsonTableData(reportId, userId);

        if (retObj.rowCount == 0) {
            InputStream servletStream = session.getServletContext().getResourceAsStream("/WEB-INF/classes/cache.ccf");
            if (servletStream != null) {
                try {
                    Properties fileProps = new Properties();
                    fileProps.load(servletStream);
                    hotFilePath = fileProps.getProperty("jcs.auxiliary.DC.attributes.DiskPath");
                } catch (Exception e) {
                    logger.error("Exception:", e);
                }
            }
            String folderName = "HandsonTable" + "_" + reportName + "_" + new SimpleDateFormat("MMM-dd-yyyy").format(Calendar.getInstance().getTime());
            String folderPath = hotFilePath + File.separator + folderName;
            File folderDir = new File(folderPath);
            if (!folderDir.exists()) {
                folderDir.mkdir();
                hotFilePath = folderPath;
            }
            hotFileName = "HOT" + "_" + reportId + "_" + System.currentTimeMillis() + ".txt";
        } else {
            hotFileName = retObj.getFieldValueString(0, "FILE_NAME");
            hotFilePath = retObj.getFieldValueString(0, "FILEPATH_FOLDER");
        }
        FileOutputStream fos1 = new FileOutputStream(hotFilePath + "/" + hotFileName);
        ObjectOutputStream oos1 = new ObjectOutputStream(fos1);
        oos1.writeObject(hotDetails);
        oos1.flush();
        oos1.close();
        if (retObj.rowCount == 0) {
            hotBd.insertHandsonTableData(reportId, reportName, userId, folderId, hotFileName, hotFilePath);
        }
        return null;
    }

    public ActionForward buildFormulaColumn(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(false);
        String headerName = request.getParameter("headerName");
        String reportId = request.getParameter("reportId");
        String operator = request.getParameter("operator");
        String selectedMe = request.getParameter("selectedMe");
        String[] rowDataList = request.getParameterValues("handsonData");
        String calheaders = request.getParameter("calheaders");
        String operatorType = request.getParameter("operatorType");
        String numberValueId = request.getParameter("numberValueId");
        String[] headerNames = calheaders.split(",");
        ArrayList<String> measureNumbers = new ArrayList<String>();
        measureNumbers.addAll(Arrays.asList(selectedMe.split(",")));

        HashMap formulaMap = new HashMap();
        formulaMap.put("headerName", headerName);
        formulaMap.put("operator", operator);
        formulaMap.put("selectedMe", measureNumbers);
        formulaMap.put("numberValueId", numberValueId);
        formulaMap.put("operatorType", operatorType);

        String userId = session.getAttribute("USERID").toString();
        String reportName = "";
        String hotFileName = "";
        String hotFilePath = "";
        // String reportId=request.getParameter("reportId");
        String columnNum = request.getParameter("columnNum");
        PbReturnObject retObj = null;
        HandsonTableBD hotBd1 = new HandsonTableBD();
        retObj = hotBd1.getHandsonTableData(reportId, userId);
        hotFileName = retObj.getFieldValueString(0, "FILE_NAME");
        hotFilePath = retObj.getFieldValueString(0, "FILEPATH_FOLDER");
        FileInputStream fos1 = new FileInputStream(hotFilePath + "/" + hotFileName);
        ObjectInputStream oos1 = new ObjectInputStream(fos1);
        HandsontableDetails hotDetails1 = (HandsontableDetails) oos1.readObject();
        oos1.close();
        HashMap prevFormulaMap = new HashMap();
        if (hotDetails1 != null && hotDetails1.formulaMap != null) {
            prevFormulaMap = hotDetails1.formulaMap;
        }

        Map<String, HandsontableDetails> handsontableFormula = new HashMap<String, HandsontableDetails>();

        ArrayList<String> tableMeasers = new ArrayList<String>();
        List handsontableData = new ArrayList();
        tableMeasers.addAll(Arrays.asList(headerNames));
        int sizeof = tableMeasers.size();

        HandsonTableBD hotBd = new HandsonTableBD();

        handsontableData = hotBd.getHandsonTableFormulaData(operator, rowDataList, measureNumbers, sizeof, headerName, operatorType, numberValueId);

//       if(measureNumbers.size()<=7){
        HandsontableDetails hotDetails = new HandsontableDetails();
        hotDetails.setReportId(reportId);
        hotDetails.setReportName(reportName);
        hotDetails.setUserId(userId);
        hotDetails.measureNames.addAll(tableMeasers);
        hotDetails.reportTableData.addAll(handsontableData);
        if (prevFormulaMap != null) {
            hotDetails.formulaMap = prevFormulaMap;
        }
        hotDetails.formulaMap.put(headerName, formulaMap);
        handsontableFormula.put(reportId, hotDetails);

        session.setAttribute("HOTFORMULA", handsontableFormula);
//       }
        response.getWriter().print("success");
        return null;
    }

    public ActionForward removeHandsontableSession(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(false);
        session.removeAttribute("HOTFORMULA");
        return null;
    }

    public ActionForward builColumnFilter(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(false);
        String reportId = request.getParameter("reportId");
        int selectedMe = Integer.parseInt(request.getParameter("selectedMe"));
        String[] rowDataList = request.getParameterValues("handsonData");
        int calheaders = Integer.parseInt(request.getParameter("calheaders"));

        ArrayList allValues = new ArrayList();
        HandsonTableBD hotBd = new HandsonTableBD();
        allValues = hotBd.getHotMeasureValues(selectedMe, calheaders, rowDataList);
        String[] operators = {"<", ">", "<=", ">=", "=", "!=", "<>"};
        StringBuilder filterValues = new StringBuilder();
        filterValues.append("<form name='hotFilterDetails' id='hotFilterDetailsId'><center><table>");
        filterValues.append("<tr><td>Maximum Value:</td>").append("<td><input type='text' id='maxValueId' value='" + allValues.get(0) + "' readonly></td><tr>");
        filterValues.append("<tr><td>Average Value:</td>").append("<td><input type='text' id='maxValueId' value='" + allValues.get(1) + "' readonly></td><tr>");
        filterValues.append("<tr><td>Minimum Value:</td>").append("<td><input type='text' id='maxValueId' value='" + allValues.get(2) + "' readonly></td><tr></table></center><br>");
        filterValues.append("<center><table><tr><td>When Value</td>");
        filterValues.append("<td><select id='operatorValueId' name='operatorValue' style='width:50px' onchange=\"operatorOnchange()\"");
        for (int i = 0; i < operators.length; i++) {
            filterValues.append("<option value='" + operators[i] + "'>").append(operators[i]).append("</option>");
        }
        filterValues.append("</select></td>");
        filterValues.append("<td id='firstValueOnlyId' style='display:'';'><input type='text' id='firstValueId' name='firstValue' value='' size='15' onkeypress=\"return isNumberKey(event)\"></td>");
        filterValues.append("<td id='firstValue1Id' style='display:none;'><input type='text' id='firstValId' name='firstValue1' value='' size='15' onkeypress=\"return isNumberKey(event)\"></td>").append("<td id='secondValueId' style='display:none;'><input type='text' id='secondValId' name='secondValue2' value='' size='15' onkeypress=\"return isNumberKey(event)\"></td>");
        filterValues.append("</tr></table></center><br>");
        filterValues.append("<center><table>");
        filterValues.append("<tr><td><input type='button' name='' value='Done' onclick=\"applayFiltering()\"></td><tr>");
        filterValues.append("</table></center></form>");

        response.getWriter().print(filterValues.toString());
        return null;
    }

    public ActionForward applyingColFilter(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(false);
        String reportId = request.getParameter("reportId");
        String[] rowDataList = request.getParameterValues("handsonData");
        String calheaders = request.getParameter("calheaders");
        String operator = request.getParameter("operator");
        String firstvalOnly = request.getParameter("firstvalOnly");
        String firstVal = request.getParameter("firstVal");
        String secondVal = request.getParameter("secondVal");
        String measureNo = request.getParameter("globalVal");
        ArrayList<String> tableMeasers = new ArrayList<String>();
        String[] headerNames = calheaders.split(",");
        tableMeasers.addAll(Arrays.asList(headerNames));
        double firstone = 0.0;
        double first = 0.0;
        double second = 0.0;
        if (firstvalOnly != null && !firstvalOnly.equalsIgnoreCase("")) {
            firstone = Double.parseDouble(firstvalOnly);
        }
        if (firstVal != null && !firstVal.equalsIgnoreCase("")) {
            first = Double.parseDouble(firstVal);
        }
        if (secondVal != null && !secondVal.equalsIgnoreCase("")) {
            second = Double.parseDouble(secondVal);
        }
        List handsonFilter = new ArrayList();
        String userId = session.getAttribute("USERID").toString();
        String reportName = "";
        Map<String, HandsontableDetails> handsontableFormula = new HashMap<String, HandsontableDetails>();
        HandsonTableBD hotBd = new HandsonTableBD();
        handsonFilter = hotBd.getFilteredData(operator, firstone, first, second, measureNo, rowDataList, tableMeasers.size());
        HandsontableDetails hotDetails = new HandsontableDetails();
        hotDetails.setReportId(reportId);
        hotDetails.setReportName(reportName);
        hotDetails.setUserId(userId);
        hotDetails.measureNames.addAll(tableMeasers);
        hotDetails.reportTableData.addAll(handsonFilter);
        handsontableFormula.put(reportId, hotDetails);

        session.setAttribute("HOTFORMULA", handsontableFormula);

        return null;
    }

    public ActionForward getReportFormula(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String hotFileName = "";
        String hotFilePath = "";

        HttpSession session = request.getSession(false);
        if (session != null) {
            String reportId = request.getParameter("reportId");
            String userId = session.getAttribute("USERID").toString();
            String columnNum = request.getParameter("columnNum");
            PbReturnObject retObj = null;
            HandsonTableBD hotBd = new HandsonTableBD();
            retObj = hotBd.getHandsonTableData(reportId, userId);
            hotFileName = retObj.getFieldValueString(0, "FILE_NAME");
            hotFilePath = retObj.getFieldValueString(0, "FILEPATH_FOLDER");
            FileInputStream fos1 = new FileInputStream(hotFilePath + "/" + hotFileName);
            ObjectInputStream oos1 = new ObjectInputStream(fos1);
            HandsontableDetails hotDetails = (HandsontableDetails) oos1.readObject();
            oos1.close();

            List measureNames = hotDetails.measureNames;
            List<String> selMeasureNames = new ArrayList<String>();
            String formulaName;
            if (measureNames != null) {
                formulaName = (String) measureNames.get(Integer.parseInt(columnNum));

                HashMap formulaMap = hotDetails.formulaMap != null ? hotDetails.formulaMap.get(formulaName.trim()) : null;

                if (formulaMap != null) {
                    String headerName = (String) formulaMap.get("headerName");
                    String operator = (String) formulaMap.get("operator");
                    List<String> selectedMe = (List<String>) formulaMap.get("selectedMe");
                    String numberValueId = (String) formulaMap.get("numberValueId");
                    String operatorType = (String) formulaMap.get("operatorType");
                    for (String str : selectedMe) {
                        selMeasureNames.add((String) measureNames.get(Integer.parseInt(str)));
                    }
                    StringBuilder formula = new StringBuilder();
                    formula.append("(");
                    for (int i = 0; i < selMeasureNames.size(); i++) {
                        formula.append(selMeasureNames.get(i));
                        if (i < (selMeasureNames.size() - 1)) {
                            formula.append(operator);
                        }
                    }
                    formula.append(")");
                    formula.append(operatorType);
                    formula.append(numberValueId);

                    StringBuilder formulaValues = new StringBuilder();
                    formulaValues.append("<table><tr><td>Header Name</td><td><input id='ColumnName' type='text' name='headerName' value='" + headerName + "' ></td></tr></table><br>");
                    formulaValues.append("<table><tr>Formula:</tr></table>");
                    formulaValues.append("<table><tr><td><textarea id='formulaId' rows='3' style='width: 99%; height: 100%;' cols='60' readonly='' name='' value='" + formula.toString() + "'>" + formula.toString() + "</textarea></td></tr></table><br>");
                    formulaValues.append("<center><table><tr><td><input type='button'class='navtitle-hover' name='' value='Done' onclick='showFormulaHandsOnTable()'></td></tr></table></center>");
                    response.getWriter().print(formulaValues.toString());

                }
            }
        }
        return null;
    }
}
