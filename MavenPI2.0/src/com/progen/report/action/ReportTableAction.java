/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.action;

import com.progen.db.ProgenDataSet;
import com.progen.query.RTMeasureElement;
import com.progen.report.bd.PbReportTableBD;
import com.progen.report.data.DataFacade;
import com.progen.reportview.bd.PbReportViewerBD;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.LookupDispatchAction;
import prg.db.Container;
import prg.db.PbDb;
import prg.db.PbReturnObject;

/**
 *
 * @author progen
 */
public class ReportTableAction extends LookupDispatchAction {

    public static Logger logger = Logger.getLogger(ReportTableAction.class);

    @Override
    protected Map getKeyMethodMap() {
        Map actionMap = new HashMap();
        actionMap.put("storeComment", "storeComment");
        actionMap.put("clearComment", "clearComment");
        actionMap.put("storeFormat", "storeFormat");
        actionMap.put("clearFormat", "clearFormat");
        actionMap.put("searchDataSet", "searchDataSet");
        actionMap.put("searchDataSetonSubToTal", "searchDataSetonSubToTal");
        actionMap.put("addRTExcelColumn", "addRTExcelColumn");
        actionMap.put("updateRTExcelColumn", "updateRTExcelColumn");
        actionMap.put("exportRTExcelColumn", "exportRTExcelColumn");
        actionMap.put("importRTExcelColumn", "importRTExcelColumn");
        actionMap.put("addRTTargetColumn", "addRTTargetColumn");
        actionMap.put("clearDataSet", "clearDataSet");
        actionMap.put("ClearSearchFilterOnSubTotal", "ClearSearchFilterOnSubTotal");
        actionMap.put("getFilterConditionIfAny", "getFilterConditionIfAny");
        actionMap.put("getAllFilterCondition", "getAllFilterCondition");
        actionMap.put("getMultiCalender", "getMultiCalender");
        return actionMap;
    }

    public ActionForward storeComment(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

        String rowStr = request.getParameter("row");
        String colStr = request.getParameter("col");
        int row = Integer.parseInt(rowStr);
        int col = Integer.parseInt(colStr);
        String commentTxt = request.getParameter("comment");
        String reportId = request.getParameter("REPORT_ID");
        PbReportTableBD reportTableBD = new PbReportTableBD();

        Container container = Container.getContainerFromSession(request, reportId);
        reportTableBD.storeComment(container, row, col, commentTxt);

        return null;
    }

    public ActionForward clearComment(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String rowStr = request.getParameter("row");
        String colStr = request.getParameter("col");
        int row = Integer.parseInt(rowStr);
        int col = Integer.parseInt(colStr);
        String reportId = request.getParameter("REPORT_ID");
        PbReportTableBD reportTableBD = new PbReportTableBD();

        Container container = Container.getContainerFromSession(request, reportId);
        reportTableBD.clearComment(container, row, col);

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
        PbReportTableBD reportTableBD = new PbReportTableBD();

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
        PbReportTableBD reportTableBD = new PbReportTableBD();

        Container container = Container.getContainerFromSession(request, reportId);
        reportTableBD.clearFormat(container, row, col);

        return null;
    }

    public ActionForward searchDataSet(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        PrintWriter out = null;
        try {
            out = response.getWriter();
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }
//        HttpSession session = request.getSession(false);
        String search = request.getParameter("search");
        String srchCol = request.getParameter("srchCol");
        String srchValue = request.getParameter("srchValue");
        String reportId = request.getParameter("reportId");

        Container container = Container.getContainerFromSession(request, reportId);
//        start of code by Nazneen for storing total row count before search filter
        DataFacade facade = new DataFacade(container);
        int GTRowCntBeforSearchFilter = container.getGTRowCntBeforSearchFilter();
        if (GTRowCntBeforSearchFilter != 0) {
            container.setGTRowCntBeforSearchFilter(GTRowCntBeforSearchFilter);
        } else {
            container.setGTRowCntBeforSearchFilter(facade.getRowCount());
        }
        container.setIsSearchFilterApplied(true);
//        end of code by Nazneen for storing total row count before search filter
        boolean columnAccepted = container.setSearchColumn(srchCol, search.trim(), srchValue.trim(), "frmTabFilter");
        if (!columnAccepted) {
            out.print("invalid");
        } else {
           //added by sruhti for logical operation after sorting and grouping
            
            String groupid = container.getGroupColumns1().toString();
           // String groupid
            if(groupid != null && !groupid.toString().isEmpty())
          groupid=container.getGroupColumns1().toString().replace("[", "").replace("]", "");
            if (groupid != null && !groupid.toString().isEmpty()) {
                container.setsearch(search);
            }//ended by sruthi
            PbReportTableBD reportTableBD = new PbReportTableBD();
            reportTableBD.searchDataSet(container);
            out.print("refresh");
        }
        return null;
    }

    public ActionForward subTotalSearchDataSet(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        PrintWriter out = null;
        try {
            out = response.getWriter();
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }
//        HttpSession session = request.getSession(false);
        String search = request.getParameter("search");
        String srchCol = request.getParameter("srchCol");
        String srchValue = request.getParameter("srchValue");
        String reportId = request.getParameter("reportId");

        Container container = Container.getContainerFromSession(request, reportId);
//        start of code by Nazneen for storing total row count before search filter
//        DataFacade facade = new DataFacade(container);
//        int GTRowCntBeforSearchFilter = container.getGTRowCntBeforSearchFilter();
//        if(GTRowCntBeforSearchFilter!=0){
//            container.setGTRowCntBeforSearchFilter(GTRowCntBeforSearchFilter);
//        } else { container.setGTRowCntBeforSearchFilter(facade.getRowCount()); }
        container.setIsSubTotalSearchFilterApplied(true);
//        end of code by Nazneen for storing total row count before search filter
        boolean columnAccepted = container.setSearchColumn(srchCol, search.trim(), srchValue.trim(), "frmTabFilter");
        if (!columnAccepted) {
            out.print("invalid");
        } else {
//            PbReportTableBD reportTableBD = new PbReportTableBD();
//            reportTableBD.searchDataSet(container);
//            out.print("refresh");
        }
        return null;
    }

    public ActionForward addRTTargetColumn(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String newColName = request.getParameter("columnName");
        String reportId = request.getParameter("REPORT_ID");
        String colNumber = request.getParameter("col");
        int col = Integer.parseInt(colNumber);

        Container container = Container.getContainerFromSession(request, reportId);
        PbReportTableBD repTableBd = new PbReportTableBD();
        PbReportViewerBD repViewBd = new PbReportViewerBD();
        String colName = container.getDisplayColumns().get(col);

        repViewBd.addRunTimeColumn(container, RTMeasureElement.EXCEL_TARGET_COLUMN, colName, newColName);
        repTableBd.addExcelColumn(container, col, newColName);

        String measId = container.getDisplayColumns().get(col + 1);
        container.addRTTargetColumn(measId);
        return null;
    }

    public ActionForward addRTExcelColumn(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String newColName = request.getParameter("columnName");
        String reportId = request.getParameter("REPORT_ID");
        String colNumber = request.getParameter("col");
        int col = Integer.parseInt(colNumber);

        Container container = Container.getContainerFromSession(request, reportId);
        PbReportTableBD repTableBd = new PbReportTableBD();
        PbReportViewerBD repViewBd = new PbReportViewerBD();
        String colName = container.getDisplayColumns().get(col);

        repViewBd.addRunTimeColumn(container, RTMeasureElement.EXCEL_COLUMN, colName, newColName);
        repTableBd.addExcelColumn(container, col, newColName);

        return null;
    }

    public ActionForward updateRTExcelColumn(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String reportId = request.getParameter("REPORT_ID");
        String colNumber = request.getParameter("col");
        int col = Integer.parseInt(colNumber);
        String data = request.getParameter("data");
        String fromRow = request.getParameter("fromRow");
        String toRow = request.getParameter("toRow");
        int from = Integer.parseInt(fromRow);
        int to = Integer.parseInt(toRow);

        Container container = Container.getContainerFromSession(request, reportId);
        PbReportTableBD repTableBd = new PbReportTableBD();

        repTableBd.updateExcelColumn(container, col, data, from, to);

        return null;
    }

    public ActionForward exportRTExcelColumn(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String reportId = request.getParameter("REPORT_ID");
        Container container = Container.getContainerFromSession(request, reportId);
        PbReportTableBD repTableBd = new PbReportTableBD();

        try {
            ServletOutputStream out = response.getOutputStream();
            response.setContentType("application/vnd.ms-excel");
            repTableBd.exportRTExcelColumns(container, out);
            out.flush();
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }
        return null;
    }

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
                PbReportTableBD repTableBd = new PbReportTableBD();
                boolean success = repTableBd.importRTExcelColumns(container, bis);

                bis.close();
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }
        request.setAttribute("tabId", reportId);
        return mapping.findForward("gotoPbDisplay");
    }

    public ActionForward clearDataSet(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        PrintWriter out = null;
        try {
            out = response.getWriter();
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }
//        HttpSession session = request.getSession(false);
        String srchCol = request.getParameter("srchCol");
        String reportId = request.getParameter("reportId");
        Container container = Container.getContainerFromSession(request, reportId);
        ArrayList<String> srchColumns = container.getSearchColumns();
        ArrayList<String> srchCondition = container.getSearchConditions();
        ArrayList<Object> srchValue = container.getSearchValues();
        ProgenDataSet retObj = container.getRetObj();

        if (srchColumns != null && srchColumns.contains(srchCol)) {
            int colindex = srchColumns.indexOf(srchCol);
            srchColumns.remove(colindex);
            srchCondition.remove(colindex);
            srchValue.remove(colindex);
            container.resetTopBottom();
            retObj.resetViewSequence();
            container.setRetObj(retObj);
            container.setGrret(retObj);

            PbReportTableBD reportTableBD = new PbReportTableBD();
            reportTableBD.searchDataSet(container);
            out.print("refresh");

        }

        return null;
    }

    /*
     * Code by Govardhan Code for setting the subtotal searchfilters to continer
     * and will be used in tablebodydisplay to filter data based on subTotal
     */
    public ActionForward searchDataSetonSubToTal(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        PrintWriter out = null;
        try {
            out = response.getWriter();
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }
//        HttpSession session = request.getSession(false);
        String search = request.getParameter("search");
        String srchCol = request.getParameter("srchCol");
        String srchValue = request.getParameter("srchValue");
        String reportId = request.getParameter("reportId");

        Container container = Container.getContainerFromSession(request, reportId);

        DataFacade facade = new DataFacade(container);

        container.setIsSubTotalSearchFilterApplied(true);

        boolean columnAccepted = container.setSubToTalSearchColumn(srchCol, search.trim(), srchValue.trim(), "frmTabFilter");

        return null;
    }

    public ActionForward ClearSearchFilterOnSubTotal(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        PrintWriter out = null;
        try {
            out = response.getWriter();
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }
//        HttpSession session = request.getSession(false);
//        String search = request.getParameter("search");
        String srchCol = request.getParameter("srchCol");
//        String srchValue = request.getParameter("srchValue");
        String reportId = request.getParameter("reportId");
        Container container = Container.getContainerFromSession(request, reportId);
        DataFacade facade = new DataFacade(container);
//        boolean columnAccepted = container.ClearSearchFilterOnSubTotal(srchCol);
        container.ClearSearchFilterOnSubTotal(srchCol);

        return null;
    }

//Start of code By Govardhan for getting Subtotal searchfilters if any...
    public ActionForward getFilterConditionIfAny(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

        PrintWriter out = null;
        try {
            out = response.getWriter();
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }
//        HttpSession session = request.getSession(false);
        PbReportViewerBD repViewerBd = new PbReportViewerBD();
        String srchCol = request.getParameter("srchCol");
        String reportId = request.getParameter("reportId");
        Container container = Container.getContainerFromSession(request, reportId);
        DataFacade facade = new DataFacade(container);

        String st = repViewerBd.getAppliedFilterOnSubTotal(reportId, srchCol, container);
        out.print(st);
        return null;
    }
    //end of Code BY Govardhan.P

    public ActionForward getAllFilterCondition(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

        PrintWriter out = null;
        try {
            out = response.getWriter();
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }
//        HttpSession session = request.getSession(false);
        PbReportViewerBD repViewerBd = new PbReportViewerBD();

        String reportId = request.getParameter("reportId");
        Container container = Container.getContainerFromSession(request, reportId);
        DataFacade facade = new DataFacade(container);

        String st = repViewerBd.getAppliedFilterOnSubTotal(reportId, container);
        out.print(st);
        return null;
    }

    public ActionForward getMultiCalender(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws SQLException {
        HttpSession session = request.getSession(false);
        String reportId = request.getParameter("reportId");
// String elements = request.getParameter("elements");
// String tableid = request.getParameter("tableid");
        String factvalue = request.getParameter("factvalue").replace(",,", ",");
        String selectedfact = request.getParameter("selectedfact").replace(",,", ",");
        String daydenom = request.getParameter("daydenom");
// String checkedfacts=request.getParameter("checkedfacts");
        String truncate = request.getParameter("radioarr").replace(",,", ",");
        boolean multiflage = true;
        String[] arrselected = selectedfact.split(",");
        String[] arrfacts = factvalue.split(",");
        String[] truncatearr = truncate.split(",");
        ArrayList<String> factlist = new ArrayList<String>();
        ArrayList<String> factnames = new ArrayList<String>();
        ArrayList<String> truncatelist = new ArrayList<String>();
        for (String s : arrselected) {
            if (!s.equalsIgnoreCase("")) {
                factlist.add(s);
            }
        }
        for (String s1 : arrfacts) {
            if (!s1.equalsIgnoreCase("")) {
                factnames.add(s1);
            }
        }
        for (String s3 : truncatearr) {
            if (!s3.equalsIgnoreCase("")) {
                truncatelist.add(s3);
            }
        }
        // ArrayList<String> factdetails=new ArrayList<String>();
// PbReportViewerBD repViewerBd = new PbReportViewerBD();
//  HashMap multidatecalendar=new HashMap();
        HashMap multicalendardata = new HashMap();
// HashMap measurefacts=new HashMap();
//         String elemetids="";
//        StringBuilder elemetids=new StringBuilder();
        Container container = Container.getContainerFromSession(request, reportId);
//        DataFacade facade = new DataFacade(container);
//        ArrayList<String> measureid = new ArrayList<String>();
//         measureid=(ArrayList) container.getTableMeasure();
//             for(int j=0;j<measureid.size();j++)
////	elemetids=elemetids+","+measureid.get(j);
//             elemetids.append(",").append(measureid.get(j));
//         if (!(elemetids.toString().equalsIgnoreCase(""))) {
////            elemetids = elemetids.substring(1);
//              elemetids=new StringBuilder("");
//        }
        PbDb pbdbfact = new PbDb();
        // for(int qn=0;qn<measureid.size();qn++){
        //  ArrayList<String> elementarr=new ArrayList<String>();
//             String query="select element_id, DISP_NAME from prg_user_all_info_details where element_id ="+measureid.get(qn);
//        PbReturnObject pbro1=pbdbfact.execSelectSQL(query);
//              if (pbro1.getRowCount() != 0) {
//                   String elementid="";
//                    String displayname="";
//                    String displayfactkey="";
//                   for(int km=0;km<pbro1.getRowCount();km++){
//                        ArrayList<String> elementarr=new ArrayList<String>();
//                  elementid= pbro1.getFieldValueString(km,0);
//                  displayname=pbro1.getFieldValueString(km,1);
//                 }
//                  // elementarr.add(elementid);
//                  // measurefacts.put(displayname,elementarr);
//                  }

        // measurefacts.put(displayname,elementarr);


        // }
        String querytable = "select CALENDER_ID,DENOM_TABLE from PRG_CALENDER_SETUP where CALENDER_NAME='" + daydenom + "'";
        PbReturnObject pbro1 = pbdbfact.execSelectSQL(querytable);
        String calenderid = "";
        String denomtable = "";
        if (pbro1.getRowCount() != 0) {
            for (int km = 0; km < pbro1.getRowCount(); km++) {
                calenderid = pbro1.getFieldValueString(km, 0);
                denomtable = pbro1.getFieldValueString(km, 1);
            }
        }
        ArrayList<String> busstableid = new ArrayList<String>();
        for (int kn = 0; kn < factlist.size(); kn++) {
            String queryfact = "select DISP_NAME,BUSS_TABLE_ID,BUSS_COL_NAME,USER_COL_DESC,USER_COL_TYPE,USER_COL_NAME from prg_user_all_info_details where USER_COL_DESC='" + factlist.get(kn) + "'and DISP_NAME='" + factnames.get(kn) + "' and USER_COL_TYPE in('datetime','date')";
            PbReturnObject pbro = pbdbfact.execSelectSQL(queryfact);
            if (pbro.getRowCount() != 0) {
                for (int k = 0; k < pbro.getRowCount(); k++) {
                    ArrayList<String> factdetails = new ArrayList<String>();
                    String DISP_NAME = pbro.getFieldValueString(k, 0);
                    String buss_table_id = pbro.getFieldValueString(k, 1);
                    String bus_column_name = pbro.getFieldValueString(k, 2);
                    String user_column_desc = pbro.getFieldValueString(k, 3);
                    String user_column_type = pbro.getFieldValueString(k, 4);
                    String user_column_name = pbro.getFieldValueString(k, 5);
                    factdetails.add(DISP_NAME);
                    factdetails.add(bus_column_name);
                    factdetails.add(user_column_desc);
                    factdetails.add(user_column_type);
                    factdetails.add(user_column_name);
                    busstableid.add(buss_table_id);
                    factdetails.add(daydenom);
                    factdetails.add(truncatelist.get(kn));
                    factdetails.add(calenderid);
                    factdetails.add(denomtable);
                    multicalendardata.put(buss_table_id, factdetails);
                }
                //factdetails.add(daydenom);
                //multicalendardata.put(tableid,factdetails);
            }
        }
//multidatecalendar.put(reportId,multicalendardata);

        container.setMultiCalendarHashMap(multicalendardata);
        container.setMultiCalendarFlag(multiflage);
        session.setAttribute("Multi_Calendar_Details", multicalendardata);

        return null;
    }
}
