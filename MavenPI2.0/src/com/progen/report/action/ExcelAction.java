/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.action;

import com.progen.query.RTMeasureElement;
import com.progen.report.bd.ExcelBD;
import com.progen.reportview.bd.PbReportViewerBD;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.LookupDispatchAction;
import prg.db.Container;

/**
 *
 * @author progen
 */
public class ExcelAction extends LookupDispatchAction {

    public static Logger logger = Logger.getLogger(ExcelAction.class);

    @Override
    protected Map getKeyMethodMap() {
        Map actionMap = new HashMap();
        actionMap.put("storeComment", "storeComment");
        actionMap.put("clearComment", "clearComment");
        actionMap.put("storeFormat", "storeFormat");
        actionMap.put("clearFormat", "clearFormat");
        actionMap.put("addRTExcelColumn", "addRTExcelColumn");
        actionMap.put("updateRTExcelColumn", "updateRTExcelColumn");
        actionMap.put("exportRTExcelColumn", "exportRTExcelColumn");
        actionMap.put("importRTExcelColumn", "importRTExcelColumn");
        actionMap.put("addRTTargetColumn", "addRTTargetColumn");
        actionMap.put("storeValue", "storeValue");
        actionMap.put("clearValue", "clearValue");
        actionMap.put("copyCells", "copyCells");
        actionMap.put("pasteCells", "pasteCells");
        return actionMap;
    }

    public ActionForward storeComment(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

        String rowStr = request.getParameter("row");
        String colStr = request.getParameter("col");
        int row = Integer.parseInt(rowStr);
        int col = Integer.parseInt(colStr);
        String commentTxt = request.getParameter("comment");
        String reportId = request.getParameter("REPORT_ID");
        ExcelBD bd = new ExcelBD();
        HttpSession session = request.getSession();
        String userId = (String) session.getAttribute("USERID");

        Container container = Container.getContainerFromSession(request, reportId);
        bd.storeComment(userId, container, row, col, commentTxt);

        return null;
    }

    public ActionForward clearComment(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String rowStr = request.getParameter("row");
        String colStr = request.getParameter("col");
        int row = Integer.parseInt(rowStr);
        int col = Integer.parseInt(colStr);
        String reportId = request.getParameter("REPORT_ID");
        ExcelBD bd = new ExcelBD();

        Container container = Container.getContainerFromSession(request, reportId);
        bd.clearComment(container, row, col);

        return null;
    }

    public ActionForward storeFormat(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String rowStr = request.getParameter("row");
        String colStr = request.getParameter("col");
        int row = Integer.parseInt(rowStr);
        int col = Integer.parseInt(colStr);
        String bgColor = request.getParameter("bgColor");
        String fontColor = request.getParameter("fontColor");

        bgColor = "#" + bgColor;
        fontColor = "#" + fontColor;

        String reportId = request.getParameter("REPORT_ID");
        ExcelBD reportTableBD = new ExcelBD();

        Container container = Container.getContainerFromSession(request, reportId);
        reportTableBD.storeFormat(container, row, col, bgColor, fontColor);

        return null;
    }

    public ActionForward clearFormat(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String rowStr = request.getParameter("row");
        String colStr = request.getParameter("col");
        int row = Integer.parseInt(rowStr);
        int col = Integer.parseInt(colStr);

        String reportId = request.getParameter("REPORT_ID");
        ExcelBD reportTableBD = new ExcelBD();

        Container container = Container.getContainerFromSession(request, reportId);
        reportTableBD.clearFormat(container, row, col);

        return null;
    }

    public ActionForward addRTExcelColumn(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String newColName = request.getParameter("columnName");
        String reportId = request.getParameter("REPORT_ID");
        String colNumber = request.getParameter("col");
        int col = Integer.parseInt(colNumber);

        Container container = Container.getContainerFromSession(request, reportId);
        ExcelBD bd = new ExcelBD();
        PbReportViewerBD repViewBd = new PbReportViewerBD();
        String colName = container.getDisplayColumns().get(col);

        repViewBd.addRunTimeColumn(container, RTMeasureElement.EXCEL_COLUMN, colName, newColName);
        String measId = container.getDisplayColumns().get(col + 1);
        bd.addExcelColumn(container, col, measId, newColName);

        return null;
    }

    public ActionForward updateRTExcelColumn(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String reportId = request.getParameter("REPORT_ID");
        Container container = Container.getContainerFromSession(request, reportId);
        ExcelBD bd = new ExcelBD();
        bd.updateExcelColumns(container);

        return null;
    }

    /*
     * public ActionForward exportRTExcelColumn(ActionMapping mapping,
     * ActionForm form, HttpServletRequest request, HttpServletResponse
     * response){ String reportId=request.getParameter("REPORT_ID"); Container
     * container = Container.getContainerFromSession(request, reportId);
     * PbReportTableBD repTableBd = new PbReportTableBD();
     *
     * try { ServletOutputStream out = response.getOutputStream();
     * response.setContentType("application/vnd.ms-excel");
     * repTableBd.exportRTExcelColumns(container, out); out.flush(); } catch
     * (IOException ex) { logger.error("Exception:",ex); } return null; }
     */
    public ActionForward importRTExcelColumn(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

        String contentType = request.getContentType();
        String reportId = request.getParameter("reportId");
        if (contentType != null && contentType.contains("multipart/form-data")) {
            try {
                DataInputStream in = new DataInputStream(request.getInputStream());

                //we are taking the length of Content type data
                int formDataLength = request.getContentLength();
                byte dataBytes[] = new byte[formDataLength];
                int byteRead = 0;
                int totalBytesRead = 0;

                while (totalBytesRead < formDataLength) {
                    byteRead = in.read(dataBytes, totalBytesRead, formDataLength);
                    totalBytesRead += byteRead;
                }

                String file = new String(dataBytes);
                //for saving the file name
                String saveFile = file.substring(file.indexOf("filename=\"") + 10);
                saveFile = saveFile.substring(0, saveFile.indexOf("\n"));
                saveFile = saveFile.substring(saveFile.lastIndexOf("\\") + 1, saveFile.indexOf("\""));
                int lastIndex = contentType.lastIndexOf("=");

                String boundary = contentType.substring(lastIndex + 1, contentType.length());
                int pos;
                //extracting the index of file
                pos = file.indexOf("filename=\"");
                pos = file.indexOf("\n", pos) + 1;
                pos = file.indexOf("\n", pos) + 1;
                pos = file.indexOf("\n", pos) + 1;
                int boundaryLocation = file.indexOf(boundary, pos) - 4;
                int startPos = ((file.substring(0, pos)).getBytes()).length;
                int endPos = ((file.substring(0, boundaryLocation)).getBytes()).length;
                endPos = boundaryLocation;      //Need to check if there's an issue

                byte[] fileData = new byte[endPos - startPos];
                int index = 0;
                for (int i = startPos; i < endPos; i++) {
                    fileData[index++] = dataBytes[i];
                }

                ByteArrayInputStream bis = new ByteArrayInputStream(fileData);
                Container container = Container.getContainerFromSession(request, reportId);
                ExcelBD bd = new ExcelBD();
                boolean success = bd.importRTExcelColumns(container, bis);

                bis.close();
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }
        request.setAttribute("tabId", reportId);
        return mapping.findForward("gotoPbDisplay");
    }

    public ActionForward addRTTargetColumn(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String reportId = request.getParameter("REPORT_ID");
        String elementId = request.getParameter("elementId");
        Container container = Container.getContainerFromSession(request, reportId);
        String targetElementId = elementId + RTMeasureElement.EXCELTARGETCOLUMN.getColumnType();

        PrintWriter out = null;
        try {
            out = response.getWriter();
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }

        if (container.getDisplayColumns().contains(targetElementId)) {
        } else {
            ExcelBD bd = new ExcelBD();
            PbReportViewerBD repViewBd = new PbReportViewerBD();
            ArrayList<String> dispCols = container.getDisplayColumns();
            int index = dispCols.indexOf(elementId);

            repViewBd.addRunTimeColumn(container, RTMeasureElement.EXCEL_TARGET_COLUMN, elementId, null);

            String measId = container.getDisplayColumns().get(index + 1);
            String newColName = (String) container.getDisplayLabels().get(index + 1);
            bd.addExcelColumn(container, index, measId, newColName);

            container.addRTTargetColumn(measId);
            out.println("success");
        }
        return null;
    }

    public ActionForward clearValue(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String rowStr = request.getParameter("row");
        String colStr = request.getParameter("col");
        String address = request.getParameter("cellAddress");
        int row = Integer.parseInt(rowStr);
        int col = Integer.parseInt(colStr);

        String reportId = request.getParameter("REPORT_ID");
        ExcelBD bd = new ExcelBD();
        HttpSession session = request.getSession();
        String userId = (String) session.getAttribute("USERID");

        Container container = Container.getContainerFromSession(request, reportId);
        String retVal = bd.clearValue(container, row, col, address);

        PrintWriter out = null;
        try {
            out = response.getWriter();
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }
        out.println(retVal);

        return null;
    }

    public ActionForward storeValue(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String rowStr = request.getParameter("row");

        String colStr = request.getParameter("col");
        String address = request.getParameter("cellAddress");
        int row = Integer.parseInt(rowStr);
        int col = Integer.parseInt(colStr);
        String formulaVal = request.getParameter("formulaVal");
        String reportId = request.getParameter("REPORT_ID");
        ExcelBD bd = new ExcelBD();
        HttpSession session = request.getSession();
        String userId = (String) session.getAttribute("USERID");

        Container container = Container.getContainerFromSession(request, reportId);
        if (!formulaVal.toUpperCase().contains(address.toUpperCase())) {
            String retVal = bd.updateValue(container, row, col, address, formulaVal);
//        
            PrintWriter out = null;
            try {
                out = response.getWriter();
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
            out.println(retVal);
        }

        return null;
    }

    public ActionForward copyCells(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String reportId = request.getParameter("REPORT_ID");
        String address = request.getParameter("cellAddress");
        ExcelBD bd = new ExcelBD();
        HttpSession session = request.getSession();
        String userId = (String) session.getAttribute("USERID");

        Container container = Container.getContainerFromSession(request, reportId);

        bd.copyCells(container, address);
        return null;
    }

    public ActionForward pasteCells(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String reportId = request.getParameter("REPORT_ID");
        String address = request.getParameter("cellAddress");
        ExcelBD bd = new ExcelBD();
        HttpSession session = request.getSession();
        String userId = (String) session.getAttribute("USERID");
        Container container = Container.getContainerFromSession(request, reportId);
        String retVal = bd.pasteCells(container, address);

        PrintWriter out = null;
        try {
            out = response.getWriter();
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }
        out.println(retVal);

        return null;
    }
}
