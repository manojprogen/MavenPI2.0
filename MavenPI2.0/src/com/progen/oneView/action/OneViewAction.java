/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.oneView.action;

import com.google.common.collect.ArrayListMultimap;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.itextpdf.text.BadElementException;
import com.progen.charts.JqplotGraphProperty;
import com.progen.charts.ProgenJQPlotGraph;
import com.progen.dashboardView.action.PbDashboardViewerAction;
import com.progen.dashboardView.bd.PbDashboardViewerBD;
import com.progen.dashboardView.db.DashboardViewerDAO;
import com.progen.datasnapshots.DataSnapshotGenerator;
import com.progen.db.ProgenDataSet;
import com.progen.graph.GraphBuilder;
import com.progen.jqplot.ProGenJqPlotProperties;
import com.progen.oneView.bd.OneViewBD;
import com.progen.query.RunTimeMeasure;
import com.progen.report.bd.PbReportTableBD;
import com.progen.report.data.DataFacade;
import com.progen.report.display.util.NumberFormatter;
import com.progen.report.entities.KPI;
import com.progen.report.entities.KPIComment;
import com.progen.report.kpi.KPIBuilder;
import com.progen.report.*;
import com.progen.report.query.PbReportQuery;
import com.progen.report.util.stat.StatUtil;
import com.progen.reportdesigner.bd.DashboardTemplateBD;
import com.progen.reportdesigner.bd.ReportTemplateBD;
import com.progen.reportdesigner.db.DashboardTemplateDAO;
import com.progen.reportdesigner.db.ReportTemplateDAO;
import com.progen.reportview.action.ReportViewerAction;
import com.progen.reportview.bd.PbReportViewerBD;
import com.progen.reportview.db.CreateKPIFromReport;
import com.progen.reportview.db.PbReportViewerDAO;
import com.progen.scheduler.ReportSchedule;
import com.progen.scheduler.SchedulerBD;
import com.progen.scheduler.tracker.TrackerCondition;
import com.progen.userlayer.action.GenerateDragAndDrophtml;
import com.progen.userlayer.db.UserLayerDAO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.LookupDispatchAction;
import org.json.simple.JSONObject;
import prg.db.*;
import prg.util.PbOneViewPdfDriver;
import utils.db.ProgenConnection;
import utils.db.ProgenParam;

/**
 * added by srikanth.p
 *
 * @author progen
 */
public class OneViewAction extends LookupDispatchAction {

    /*
     * forward name="success" path=""
     */
    private final static String SUCCESS = "success";
    PbOneViewPdfDriver pdfdriver = new PbOneViewPdfDriver();
    public static Logger logger = Logger.getLogger(OneViewAction.class);

    /**
     * Provides the mapping from resource key to method name.
     *
     * @return Resource key / method name map.
     */
    protected Map getKeyMethodMap() {
        Map map = new HashMap();
        map.put("buildOneViewTemplate", "buildOneViewTemplate");
        map.put("oneViewNote", "oneViewNote");
        map.put("appendOneViewNote", "appendOneViewNote");
        map.put("saveOneViewReg", "saveOneViewReg");
        map.put("versionCheck", "versionCheck");
        map.put("saveCanvasImage", "saveCanvasImage");
        map.put("getAssignedGraphDetails", "getAssignedGraphDetails");
        map.put("buildOneViewPdf", "buildOneViewPdf");
        map.put("saveEachReg", "saveEachReg");
        map.put("getDimesions", "getDimesions");
        map.put("applyGlobalFilter", "applyGlobalFilter");
        map.put("getFilterDetails", "getFilterDetails");
        map.put("resetGlobalFilter", "resetGlobalFilter");
        map.put("oneviewMeasureAlerts", "oneviewMeasureAlerts");
        map.put("getTimeDetails", "getTimeDetails");
        map.put("getAdditionalInfo", "getAdditionalInfo");
        map.put("buildDialChart", "buildDialChart");
        map.put("zoomDialChart", "zoomDialChart");
        map.put("buildExixtentMeasure", "buildExixtentMeasure");

        map.put("renameOneviewBys", "renameOneviewBys");
        map.put("commentsData", "commentsData");
        map.put("getCommentData", "getCommentData");
        map.put("measureOptionVale", "measureOptionVale");
        map.put("getMeasureOptionData", "getMeasureOptionData");
        map.put("GetDashBoardNames", "GetDashBoardNames");
        map.put("getMeasureGraph", "getMeasureGraph");
        map.put("deleteOneview", "deleteOneview");
        map.put("oneViewRenaming", "oneViewRenaming");
        map.put("gerateOneviewPdf", "gerateOneviewPdf");
        map.put("setNewTypeMeasure", "setNewTypeMeasure");
        map.put("drillForRegionName", "drillForRegionName");
        map.put("getDashboardKpis", "getDashboardKpis");
        map.put("oneviewAndReportTimeDeatails", "oneviewAndReportTimeDeatails");

        map.put("oneviewCompAndNoComp", "oneviewCompAndNoComp");
        map.put("refreshOneView", "refreshOneView");
        map.put("getNavigationOptions", "getNavigationOptions");
        map.put("getRelatedMeasures", "getRelatedMeasures");
        map.put("getOneViewAssignment", "getOneViewAssignment");
        map.put("assignOneView", "assignOneView");
        map.put("getRegDate", "getRegDate");
        map.put("getOneViewSequence", "getOneViewSequence");
        map.put("saveViewSequence", "saveViewSequence");
        map.put("getComplexKPIGraphinoneview", "getComplexKPIGraphinoneview");
        map.put("viewerRegions", "viewerRegions");
        map.put("mergeOneViewColumns", "mergeOneViewColumns");
        map.put("mergeOneViewRows", "mergeOneViewRows");
        map.put("getOneViewMeasureValue", "getOneViewMeasureValue");
        map.put("getingOneviews", "getingOneviews");
        map.put("getReportTables", "getReportTables");
        map.put("getReportGraphs", "getReportGraphs");
        map.put("refreshOneVIewReg", "refreshOneVIewReg");
        map.put("getMeasureGraphForTrends", "getMeasureGraphForTrends");
        map.put("getMeasuresForRole", "getMeasuresForRole");
        map.put("setDimensionDetails", "setDimensionDetails");
        map.put("getOverlayTrends", "getOverlayTrends");
        map.put("getSingleDrillTrend", "getSingleDrillTrend");
        map.put("NoOfRegions", "NoOfRegions");
        map.put("enableCustomTimeMeasure", "enableCustomTimeMeasure");
        map.put("getCustomTimeMsrDetails", "getCustomTimeMsrDetails");
        map.put("buildOneviewTemplateGraph", "buildOneviewTemplateGraph");
        map.put("changeBusinessTemplateGraphColumn", "changeBusinessTemplateGraphColumn");
        map.put("changeBusinessTemplateRowViewby", "changeBusinessTemplateRowViewby");
        map.put("getOneviewBusinessRole", "getOneviewBusinessRole");
        map.put("buildMesureBasedTemplateData", "buildMesureBasedTemplateData");
        map.put("getContainerFromSession", "getContainerFromSession");
        map.put("getonename", "getonename");
        map.put("saveoneviewjson", "saveoneviewjson");
        map.put("measuresecurity", "measuresecurity");
        map.put("deleteregion", "deleteregion");
        return map;
    }

    public ActionForward oneViewNote(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(false);
        PrintWriter out = response.getWriter();
        ServletContext context = this.getServlet().getServletContext();
        HashMap map = null;
        OnceViewContainer onecontainer = new OnceViewContainer();
        String noteName = request.getParameter("name");
        String divId = request.getParameter("divId");
        String oneViewIdValue = request.getParameter("oneViewIdValue");
        String busroleId = request.getParameter("busroleId");
        int width = Integer.parseInt(request.getParameter("width"));
        int height = Integer.parseInt(request.getParameter("height"));
//        int colSp = Integer.parseInt(request.getParameter("colSp"));
//        int rowSp = Integer.parseInt(request.getParameter("rowSp"));
        String advHtmlFileProps = (String) request.getSession(false).getAttribute("advHtmlFileProps");
        OneViewBD oneViewBd = new OneViewBD();
        String result = "";
        if (session != null) {

//           detail.setColSpan(colSp);
//           detail.setRowSpan(rowSp);
//           onecontainer.addDashletDetail(detail);
            boolean desing = false;
            OneViewLetDetails detail = new OneViewLetDetails();


            map = (HashMap) session.getAttribute("ONEVIEWDETAILS");
            if (map != null) {
                onecontainer = (OnceViewContainer) map.get(oneViewIdValue);

                //OneViewLetDetails detail = new OneViewLetDetails();
                detail = onecontainer.onviewLetdetails.get(Integer.parseInt(request.getParameter("divId")));
                detail.setReptype("notes");
                detail.setRepName(noteName);
                detail.setWidth(width);
                detail.setHeight(height);
                detail.setRoleId(busroleId);

//                result="<div id='note-"+divId+"' style='color:#d0d0d0;font-size:25pt;position :absolute;font-family:verdana;width:"+width+"px; height:"+height+"px;'><p align='center'>Notes</p></div>";
                result = oneViewBd.getOneViewNotesData(onecontainer, divId, detail, true);
            } else {
                ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();
//              onecontainer = reportTemplateDAO.getOneViewData(oneViewIdValue);
                //  String fileName = reportTemplateDAO.getOneviewFileName(oneViewIdValue);
                String fileName = session.getAttribute("tempFileName").toString();
                FileInputStream fis2 = new FileInputStream(advHtmlFileProps + "/" + fileName);
                ObjectInputStream ois2 = new ObjectInputStream(fis2);
                onecontainer = (OnceViewContainer) ois2.readObject();
                ois2.close();
                detail = onecontainer.onviewLetdetails.get(Integer.parseInt(request.getParameter("divId")));
                detail.setReptype("notes");
                detail.setRepName(noteName);
                detail.setWidth(width);
                detail.setHeight(height);
                detail.setRoleId(busroleId);
                HashMap tempRegHashMap = onecontainer.getTempRegHashMap();
                String tempRegFileName = (String) tempRegHashMap.get(Integer.parseInt(detail.getNoOfViewLets()));
                if (tempRegFileName == null) {
                    tempRegFileName = "InnerRegionDetails" + onecontainer.oneviewId + "_" + detail.getNoOfViewLets() + "_" + session.getId() + "_" + System.currentTimeMillis() + ".txt";
                    tempRegHashMap.put(Integer.parseInt(detail.getNoOfViewLets()), tempRegFileName);
                }

                result = oneViewBd.getOneViewNotesData(onecontainer, divId, detail, true);
                OneViewBD oneViewBD = new OneViewBD();
                oneViewBD.writeOneviewRegData(onecontainer, result, detail.getNoOfViewLets(), request);
                FileOutputStream fos = new FileOutputStream(advHtmlFileProps + "/" + fileName);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(onecontainer);
                oos.flush();
                oos.close();

//                result="<div id='note-"+divId+"' style='color:#d0d0d0;font-size:25pt;position :absolute;font-family:verdana;width:"+width+"px; height:"+height+"px;'><p align='center'>Notes</p></div>";
            }
            out.print(result);

        }
        return null;
    }

    public ActionForward appendOneViewNote(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(false);
        String oneViewId = request.getParameter("oneViewId");
        String viewletId = request.getParameter("viewletId");
//           String width=request.getParameter("width");
//           String height=request.getParameter("height");
        String note = request.getParameter("note");
        String noteWriter = request.getParameter("noteWriter");
        String isAll = request.getParameter("isAll");
        String index = request.getParameter("index");
        String isDelete = request.getParameter("isDelete");
        HashMap<String, ArrayList> noteMap = new HashMap<String, ArrayList>();
        OnceViewContainer onecontainer = new OnceViewContainer();
        OneViewBD oneViewBd = new OneViewBD();

        HashMap map = null;
        String advHtmlFileProps = (String) request.getSession(false).getAttribute("advHtmlFileProps");
        String result = "";
        if (session != null) {
            OneViewLetDetails detail = new OneViewLetDetails();
            map = (HashMap) session.getAttribute("ONEVIEWDETAILS");
            String userName = session.getAttribute("USERNAME").toString(); //noteWriter
            if (map != null) {
                onecontainer = (OnceViewContainer) map.get(oneViewId);
                detail = onecontainer.onviewLetdetails.get(Integer.parseInt(request.getParameter("viewletId")));
                if (!noteWriter.equalsIgnoreCase("") && !noteWriter.equalsIgnoreCase("undefine")) {
                    detail.setRepName(noteWriter);
                }
                noteMap = detail.noteMap;
                if (isDelete != null && isDelete.equalsIgnoreCase("true")) {
                    ArrayList<String> notes = noteMap.get(userName);
                    if (isAll != null && isAll.equalsIgnoreCase("true")) {
                        noteMap.remove(userName);
                    } else {
                        int removeAt = Integer.parseInt(index);
                        notes.remove(removeAt);
                    }

                } else {
                    if (!noteMap.isEmpty()) {
                        ArrayList<String> notes = noteMap.get(userName);
                        if (notes != null && !notes.isEmpty()) {
                            notes.add(note);
                        } else {
                            notes = new ArrayList<String>();
                            notes.add(note);
                            noteMap.put(userName, notes);
                        }
                    } else {
                        ArrayList<String> notes = new ArrayList<String>();
                        notes.add(note);
                        noteMap.put(userName, notes);
                    }
                }
            } else {
                ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();

//              onecontainer = reportTemplateDAO.getOneViewData(oneViewIdValue);
                //  String fileName = reportTemplateDAO.getOneviewFileName(oneViewIdValue);
                String fileName = session.getAttribute("tempFileName").toString();
                FileInputStream fis2 = new FileInputStream(advHtmlFileProps + "/" + fileName);
                ObjectInputStream ois2 = new ObjectInputStream(fis2);
                onecontainer = (OnceViewContainer) ois2.readObject();
                ois2.close();
                detail = onecontainer.onviewLetdetails.get(Integer.parseInt(request.getParameter("viewletId")));
                if (!noteWriter.equalsIgnoreCase("") && !noteWriter.equalsIgnoreCase("undefine")) {
                    detail.setRepName(noteWriter);
                }
                noteMap = detail.noteMap;
                if (isDelete != null && isDelete.equalsIgnoreCase("true")) {
                    ArrayList<String> notes = noteMap.get(userName);
                    if (isAll != null && isAll.equalsIgnoreCase("true")) {
                        noteMap.remove(userName);
                    } else {
                        int removeAt = Integer.parseInt(index);
                        notes.remove(removeAt);
                    }

                } else {
                    if (!noteMap.isEmpty()) {
                        ArrayList<String> notes = noteMap.get(userName);
                        if (notes != null && !notes.isEmpty()) {
                            notes.add(note);
                        } else {
                            notes = new ArrayList<String>();
                            notes.add(note);
                            noteMap.put(userName, notes);
                        }
                    } else {
                        ArrayList<String> notes = new ArrayList<String>();
                        notes.add(note);
                        noteMap.put(userName, notes);
                    }
                }
                String result1 = "";
                result1 = oneViewBd.getOneViewNotesData(onecontainer, oneViewId, detail, true);
                oneViewBd.writeOneviewRegData(onecontainer, result1, detail.getNoOfViewLets(), request);
                result = oneViewBd.getOneViewNotesData(onecontainer, oneViewId, detail, false);
                FileOutputStream fos = new FileOutputStream(advHtmlFileProps + "/" + fileName);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(onecontainer);
                oos.flush();
                oos.close();
            }
            response.getWriter().print(result);
        }
        return null;
    }

    public ActionForward buildOneViewTemplate(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(false);
        String templateName = request.getParameter("name");
        String divId = request.getParameter("divId");
        String oneViewIdValue = request.getParameter("oneViewIdValue");
        String busroleId = request.getParameter("busroleId");
        int width = Integer.parseInt(request.getParameter("width"));
        int height = Integer.parseInt(request.getParameter("height"));
        OnceViewContainer onecontainer = new OnceViewContainer();
        OneViewBD oneViewBd = new OneViewBD();

        HashMap map = null;
        String advHtmlFileProps = (String) request.getSession(false).getAttribute("advHtmlFileProps");
        String result = "";
        if (session != null) {
            OneViewLetDetails detail = new OneViewLetDetails();
            map = (HashMap) session.getAttribute("ONEVIEWDETAILS");
            if (map != null) {
                onecontainer = (OnceViewContainer) map.get(oneViewIdValue);
                detail = onecontainer.onviewLetdetails.get(Integer.parseInt(divId));
                detail = onecontainer.onviewLetdetails.get(Integer.parseInt(request.getParameter("divId")));
                detail.setReptype("template");
                detail.setRepName(templateName);
                detail.setWidth(width);
                detail.setHeight(height);
                detail.setRoleId(busroleId);
                result = oneViewBd.getOneViewTemplateDesign(onecontainer, oneViewIdValue, detail);
                response.getWriter().print(result);
            }
        }
        return null;
    }

    /**
     * Saving The OneView by using Region File in Viewer ...added by Anil
     */
    public ActionForward saveOneViewReg(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        PbDb pbdb = new PbDb();
        HttpSession session = request.getSession(false);
        String oneviewID = request.getParameter("oneViewIdValue");
        String date = request.getParameter("date");
        String duration = request.getParameter("duration");
        String compare = request.getParameter("compare");
        String currInnerWidth = request.getParameter("innerWidth");
        String action = request.getParameter("action");
        String iconVisibility = request.getParameter("icons");
        OnceViewContainer onecontainer = null;
        OnceViewContainer onecontainer1 = null;
        List<String> tiemdeatilsArray = null;
        PbReturnObject retObj = null;
        PbReturnObject retObj1 = null;
        String onefileName = null;
        String oneVersion = "";
        String tempdir = "";
        String regString = "";
        String fileName = null;
        String checkFile = "";
        String filePath = null;
        boolean isFileExists = false;
        HashMap tempRegHashMap = new HashMap();
        HashMap regHashMap = new HashMap();
        PbReturnObject securityfilters = null;
        ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();
        OneViewBD oneViewBD = new OneViewBD();

        retObj = reportTemplateDAO.getOneviewFileNam(oneviewID);
        String USERID = (String) session.getAttribute("USERID");
        String query = "select FILE_NAME,FILE_PATH from prg_ar_oneview_assignment where oneview_id=" + oneviewID + " and user_id =" + USERID;
        retObj1 = pbdb.execSelectSQL(query);

        onefileName = retObj.getFieldValueString(0, 1);
        oneVersion = retObj.getFieldValueString(0, 9);
        filePath = retObj.getFieldValueString(0, 11);
//  commented by kruthika to update reports  assigned to the analyzer
//        if(onefileName.equalsIgnoreCase("") || onefileName.equalsIgnoreCase(null) || filePath.equalsIgnoreCase("") || filePath.equalsIgnoreCase(null)){
//
//            if((retObj1.getFieldValueString(0, 0)).isEmpty()){
//        onefileName = retObj.getFieldValueString(0, 1);
//        oneVersion=retObj.getFieldValueString(0, 9);
//        filePath=retObj.getFieldValueString(0, 11);
//           }
//        else{
//        filePath=retObj1.getFieldValueString(0, 1);
//       if(filePath.equalsIgnoreCase("")){
//        onefileName = retObj.getFieldValueString(0,1);
//        oneVersion=retObj.getFieldValueString(0, 9);
//        filePath=retObj.getFieldValueString(0, 11);
//        }
//       else{
//        onefileName=retObj1.getFieldValueString(0, 0);
//        oneVersion=retObj.getFieldValueString(0, 9);
//        filePath=retObj1.getFieldValueString(0, 1);//.getFieldValueString(0, "FILEPATH");
//           }
//        }
//           }


        String oneviewtypedate = (String) session.getAttribute("oneviewdatetype");
        Map<String, List<String>> allFilters = (Map<String, List<String>>) session.getAttribute("allFilters");
        Map<String, List<String>> allFiltersnames = (Map<String, List<String>>) session.getAttribute("allFiltersnames");
        ArrayList viewbygblname = (ArrayList) session.getAttribute("viewbynames");
        ArrayList parameterlist = (ArrayList) session.getAttribute("parameterlist");
        String advHtmlFileProps = (String) request.getSession(false).getAttribute("advHtmlFileProps");
        String oldAdvHtmlFileProps = (String) request.getSession(false).getAttribute("oldAdvHtmlFileProps");

        if (filePath != null && Float.parseFloat(oneVersion) > (float) 2.0) {
//            session.removeAttribute("oldAdvHtmlFileProps");
            session.setAttribute("oldAdvHtmlFileProps", filePath);
            oldAdvHtmlFileProps = filePath;
        } else {
            java.io.InputStream servletStream = session.getServletContext().getResourceAsStream("/WEB-INF/classes/cache.ccf");
            if (servletStream != null) {
                try {
                    Properties fileProps = new Properties();
                    fileProps.load(servletStream);
                    oldAdvHtmlFileProps = fileProps.getProperty("jcs.auxiliary.DC.attributes.DiskPath");
                    session.setAttribute("oldAdvHtmlFileProps", oldAdvHtmlFileProps);
                } catch (Exception e) {
                    logger.error("Exception:", e);
                }
            }
        }
        StringBuilder finalStringVal = new StringBuilder();
        StringBuilder finalStringVal1 = new StringBuilder();
        HashMap<String, String> mapglobal = new HashMap<String, String>();
        String settingType = "";
        boolean isEveryTimeUpdate = false;
        File file1 = null;
        if (action != null && action.equalsIgnoreCase("open")) {
            session.removeAttribute("fromreport");
            file1 = new File(oldAdvHtmlFileProps + "/" + onefileName);
            if (file1.exists()) {
                FileInputStream fis = new FileInputStream(oldAdvHtmlFileProps + "/" + onefileName);
                ObjectInputStream ois = new ObjectInputStream(fis);
                onecontainer1 = (OnceViewContainer) ois.readObject();
                ois.close();
                /*
                 * if(!oneVersion.equalsIgnoreCase("2.0")){
                 * oneViewBD.insertOneviewRegData(onecontainer1,oneviewID,request);
                 * File file2 = new File(oldAdvHtmlFileProps+"/"+onefileName);
                 * if(file2.exists()){ FileInputStream fis2 = new
                 * FileInputStream(oldAdvHtmlFileProps+"/"+onefileName);
                 * ObjectInputStream ois2 = new ObjectInputStream(fis2);
                 * onecontainer1 = (OnceViewContainer) ois2.readObject();
                 * ois2.close(); }
           }
                 */
//            if( onecontainer1.isisfromschedule()){
// String realDate="";
//                      if (onecontainer1.getDataSelection() != null) {
//                                SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
//                                Date date1 = new Date();
//                                if (onecontainer1.getDataSelection().equals("Current Day")) {
//                                    realDate = (dateFormat.format(date1.getTime()));
//                                } else if (onecontainer1.getDataSelection().equals("Previous Day")) {
//                                    int MILLIS_IN_DAY = 1000 * 60 * 60 * 24;
//                                    realDate = (dateFormat.format(date1.getTime() - MILLIS_IN_DAY));
//                                }
//                                onecontainer1.timedetails.set(2, realDate);
//                                // 
//                            }
//            }
                if (iconVisibility != null && (iconVisibility.equalsIgnoreCase("visible") || iconVisibility.equalsIgnoreCase("hide"))) {
                    onecontainer1.setIconVisibility(iconVisibility);
                }
                regHashMap = onecontainer1.getRegHashMap();
                tempdir = System.getProperty("java.io.tmpdir");
                session.setAttribute("advHtmlFileProps", tempdir);



                advHtmlFileProps = session.getAttribute("advHtmlFileProps").toString();
                fileName = "OneviewDetails" + oneviewID + "_" + session.getId() + "_" + System.currentTimeMillis() + ".txt";
                session.setAttribute("tempFileName", fileName);
                onecontainer1.setContainerFileName(fileName);
                if (regHashMap != null && !regHashMap.isEmpty()) {

                    for (int i = 0; i < onecontainer1.onviewLetdetails.size(); i++) {
                        if (regHashMap.get(i) != null) {
                            String regFileName = regHashMap.get(i).toString();
                            file1 = new File(oldAdvHtmlFileProps + "/" + regFileName);
                            if (file1.exists()) {
                                FileInputStream fisR = new FileInputStream(oldAdvHtmlFileProps + "/" + regFileName);
                                ObjectInputStream oisR = new ObjectInputStream(fisR);
                                regString = (String) oisR.readObject();
                                oisR.close();
                            }
                            regFileName = "InnerRegionDetails" + oneviewID + "_" + i + "_" + session.getId() + "_" + System.currentTimeMillis() + ".txt";
                            FileOutputStream fosR = new FileOutputStream(tempdir + "/" + regFileName);
                            ObjectOutputStream oosR = new ObjectOutputStream(fosR);
                            oosR.writeObject(regString);
                            oosR.flush();
                            oosR.close();
                            tempRegHashMap.put(i, regFileName);
                        }
                    }
                    onecontainer1.SetTempRegHashMap(tempRegHashMap);
                }




                isFileExists = true;
                /*
                 * below code is added for settings of oneview
                 */

                if (onecontainer1 != null) {
                    settingType = onecontainer1.getSettingType();
                    isEveryTimeUpdate = onecontainer1.isEveryTimeUpdate();

                    //added by srikanth.p for settings in oneView
                    if (settingType != null && !settingType.equalsIgnoreCase("undefined")) {

                        if (onecontainer1.getSettingType() != null && duration == null && date == null && compare == null) {
                            ProgenParam pParam = new ProgenParam();
                            settingType = onecontainer1.getSettingType();
                            String settingDate = onecontainer1.getSettingDate();
                            String finalDate = "";
                            List<String> timeDetails = onecontainer1.timedetails;
                            if (settingType.equalsIgnoreCase("YesterDay")) {
                                finalDate = pParam.getcurrentdateforpage(1);
                            }
                            if (settingType.equalsIgnoreCase("ToDay")) {
                                finalDate = pParam.getcurrentdateforpage(0);
                            }
                            if (settingType.equalsIgnoreCase("Tomorrow")) {
                                finalDate = pParam.getcurrentdateforpage(-1);
                            }
                            if (settingType.equalsIgnoreCase("EveryTime")) {
                            }
                            if (onecontainer1.timedetails.isEmpty()) {
//                ProgenParam paramdate = new ProgenParam();
//                String globalDate = paramdate.getdateforpage();
                                onecontainer1.addTimeDetails("Day");
                                onecontainer1.addTimeDetails("PRG_STD");
                                onecontainer1.addTimeDetails(finalDate);
                                onecontainer1.addTimeDetails("Month");
                                onecontainer1.addTimeDetails("Last Period");
                                action = "run";
                            } else {
                                if (!timeDetails.get(2).equalsIgnoreCase(finalDate)) {
                                    timeDetails.add(2, finalDate);
                                    timeDetails.remove(3);
                                    action = "run";
                                }
                                if (isEveryTimeUpdate) {
                                    action = "run";
                                }
                            }
                        }
                    }
                }
                FileOutputStream fos = new FileOutputStream(tempdir + "/" + fileName);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(onecontainer1);
                oos.flush();
                oos.close();
            } else {
                checkFile = "The File Does Not Exist";
            }

            String securityqry = "select MEMBER_VALUE,element_Id from PRG_USER_ROLE_MEMBER_FILTER  where user_id = " + USERID;
            securityfilters = (PbReturnObject) session.getAttribute("securityfilters");
//if(securityfilters!=null){
//
//}else{
            securityfilters = pbdb.execSelectSQL(securityqry);
            if (securityfilters != null && securityfilters.getRowCount() > 0) {
                session.setAttribute("isseurity", "true");
            }

//        }
            session.setAttribute("securityfilters", securityfilters);
        } else {
            fileName = session.getAttribute("tempFileName").toString();
            file1 = new File(advHtmlFileProps + "/" + fileName);
            if (file1.exists()) {
                FileInputStream fis = new FileInputStream(advHtmlFileProps + "/" + fileName);
                ObjectInputStream ois = new ObjectInputStream(fis);
                onecontainer1 = (OnceViewContainer) ois.readObject();
                ois.close();
                if (action != null && action.equalsIgnoreCase("save")) {
                    if (allFilters != null) {
                        onecontainer1.setallFilters(allFilters);
                        onecontainer1.setallFiltersnames(allFiltersnames);
                    }
                }
            }
            if ((oneVersion != null && oneVersion.equalsIgnoreCase("2.5")) || onecontainer1.getOneViewType() != null && (onecontainer1.getOneViewType().equalsIgnoreCase("Business TemplateView") || onecontainer1.getOneViewType().equalsIgnoreCase("Measure Based Business Template"))) {
            } else {
                PbReportViewerBD pbrepBd = new PbReportViewerBD();
                onecontainer1 = pbrepBd.deleteIfEmptyDetails(onecontainer1);
                onecontainer1 = pbrepBd.deleteIfEmptyDetails(onecontainer1);
                onecontainer1 = pbrepBd.deleteIfEmptyDetails(onecontainer1);
                onecontainer1 = pbrepBd.deleteIfEmptyDetails(onecontainer1);
                onecontainer1 = pbrepBd.deleteIfEmptyDetails(onecontainer1);
                onecontainer1 = pbrepBd.deleteIfEmptyDetails(onecontainer1);
                onecontainer1 = pbrepBd.deleteIfEmptyDetails(onecontainer1);
                onecontainer1 = pbrepBd.deleteIfEmptyDetails(onecontainer1);
                onecontainer1 = pbrepBd.deleteIfEmptyDetails(onecontainer1);
                onecontainer1 = pbrepBd.deleteIfEmptyDetails(onecontainer1);
                onecontainer1 = pbrepBd.deleteIfEmptyDetails(onecontainer1);
                onecontainer1 = pbrepBd.deleteIfEmptyDetails(onecontainer1);
                onecontainer1 = pbrepBd.deleteIfEmptyDetails(onecontainer1);
                onecontainer1 = pbrepBd.deleteIfEmptyDetails(onecontainer1);
                onecontainer1 = pbrepBd.deleteIfEmptyDetails(onecontainer1);
            }
            HashMap tempRegFileMap = onecontainer1.getTempRegHashMap();
            HashMap tempararyMap = new HashMap();
//         for(int i=0;i<onecontainer1.onviewLetdetails.size();i++){
//             String tempFile=tempRegFileMap.get(Integer.parseInt(onecontainer1.onviewLetdetails.get(i).getNoOfViewLets())).toString();
//             onecontainer1.onviewLetdetails.get(i).setNoOfViewLets(Integer.toString(i));
//             tempararyMap.put(i,tempFile);
//         }
            tempRegFileMap = null;
            onecontainer1.SetTempRegHashMap(tempararyMap);
            isFileExists = true;
        }
        if (isFileExists) {
            if (oneviewtypedate != null && oneviewtypedate.equalsIgnoreCase("true")) {
                if (action != null && action.equalsIgnoreCase("save")) {
                    if (allFilters != null) {
                        onecontainer1.setallFilters(allFilters);
                        onecontainer1.setallFiltersnames(allFiltersnames);
                    }
                } else {
                    if (duration != null && date != null && compare != null) {
                        if (!onecontainer1.timedetails.isEmpty()) {
                            onecontainer1.timedetails.clear();
                        }
                        onecontainer1.addTimeDetails("Day");
                        onecontainer1.addTimeDetails("PRG_STD");
                        String value = "";
                        String valu = "";
                        String mont = "";
                        String CurrValue = "";
                        value = date;

                        SimpleDateFormat format1 = new SimpleDateFormat("EEE,dd,MMM,yyyy");
                        SimpleDateFormat format2 = new SimpleDateFormat("dd/MM/yyyy");
                        Date date1 = null;

                        date1 = format1.parse(value.toString().trim());

                        value = format2.format(date1);
                        valu = value.substring(0, 2);
                        mont = value.substring(3, 5);
                        CurrValue = mont.concat("/").concat(valu).concat(value.substring(5));
                        onecontainer1.addTimeDetails(CurrValue);
                        onecontainer1.addTimeDetails(duration);
                        onecontainer1.addTimeDetails(compare);

                        advHtmlFileProps = session.getAttribute("advHtmlFileProps").toString();
                        FileOutputStream fos = new FileOutputStream(advHtmlFileProps + "/" + fileName);
                        ObjectOutputStream oos = new ObjectOutputStream(fos);
                        oos.writeObject(onecontainer1);
                        oos.flush();
                        oos.close();

                    }
                }
            } else {
                if (oneviewtypedate != null && (oneviewtypedate.equalsIgnoreCase("true") || oneviewtypedate.equalsIgnoreCase("false"))) {
                    if (action != null && action.equalsIgnoreCase("save")) {
                    } else {
                        if (duration != null && date != null && compare != null) {
                            if (!onecontainer1.timedetails.isEmpty()) {
                                onecontainer1.timedetails.clear();
                            }
                            onecontainer1.addTimeDetails("Day");
                            onecontainer1.addTimeDetails("PRG_STD");
                            String value = "";
                            String valu = "";
                            String mont = "";
                            String CurrValue = "";
                            value = date;

                            SimpleDateFormat format1 = new SimpleDateFormat("EEE,dd,MMM,yyyy");
                            SimpleDateFormat format2 = new SimpleDateFormat("dd/MM/yyyy");
                            Date date1 = null;

                            date1 = format1.parse(value.toString().trim());

                            value = format2.format(date1);
                            valu = value.substring(0, 2);
                            mont = value.substring(3, 5);
                            CurrValue = mont.concat("/").concat(valu).concat(value.substring(5));
                            onecontainer1.addTimeDetails(CurrValue);
                            onecontainer1.addTimeDetails(duration);
                            onecontainer1.addTimeDetails(compare);

                            advHtmlFileProps = session.getAttribute("advHtmlFileProps").toString();
                            FileOutputStream fos = new FileOutputStream(advHtmlFileProps + "/" + fileName);
                            ObjectOutputStream oos = new ObjectOutputStream(fos);
                            oos.writeObject(onecontainer1);
                            oos.flush();
                            oos.close();

                        }
                    }
                }
            }
            session.setAttribute("oneviewID", oneviewID);
//          onecontainer = OnceViewContainer.getContainerFromSession(request, oneviewID);
            if (session.getAttribute("tempFileName") != null) {
                FileInputStream fis1 = new FileInputStream(advHtmlFileProps + "/" + fileName);
                ObjectInputStream ois1 = new ObjectInputStream(fis1);
                onecontainer = (OnceViewContainer) ois1.readObject();
                ois1.close();
            }
            //sandeep
            if (action != null && action.equalsIgnoreCase("save")) {
                if (allFilters != null) {
                    onecontainer.setallFilters(allFilters);
                    onecontainer.setallFiltersnames(allFiltersnames);
                }
                if (viewbygblname != null) {

                    onecontainer.setviewbygblname(viewbygblname);
                    onecontainer.setparameterlist(parameterlist);
                }
                Gson gson = new Gson();
                List<OneViewLetDetails> oneviewletDetails = onecontainer.onviewLetdetails;
                if (request.getParameter("filters1") != null) {
                    boolean flag = false;
                    if ((request.getParameter("isNewReport") != null && request.getParameter("isNewReport").equalsIgnoreCase("true"))) {
                        flag = true;
                    } else {
                        flag = false;
                    }
                    if (!flag) {
                        Type tarType1111 = new TypeToken<Map<String, List<String>>>() {
                        }.getType();
                        Map<String, List<String>> map1 = gson.fromJson(request.getParameter("filters1"), tarType1111);
                        onecontainer.setFilterMap(map1);
                    }
                }
//   for (int i = 0; i < oneviewletDetails.size(); i++) {
//                    OneViewLetDetails detail = oneviewletDetails.get(i);
// String regionid=detail.getNoOfViewLets();
// String reportName=detail.getRepName();
// String reportId=detail.getRepId();
////  if( !detail.isOneviewReportTimeDetails()){
//      Map<String,String> dataMapgblsave = new HashMap<String,String>();
//if(request.getSession(false).getAttribute("dataMapgblsave")!=null){
// dataMapgblsave = (Map<String, String>) request.getSession(false).getAttribute("dataMapgblsave");
////              }
//      String data=dataMapgblsave.get(regionid);
//  FileReadWrite fileReadWrite = new FileReadWrite();
// File datafile = new File("/usr/local/cache/OneviewGO/oneview_"+oneviewID+"/oneview_"+oneviewID+"_"+regionid+"/"+reportName.replaceAll("\\W", "").trim()+"_"+reportId+".json");
//       if (datafile.exists() && data!=null) {
//           fileReadWrite.writeToFile("/usr/local/cache/OneviewGO/oneview_"+oneviewID+"/oneview_"+oneviewID+"_"+regionid+"/"+reportName.replaceAll("\\W", "").trim()+"_"+reportId+".json", data);
//                }
//  }
//            }
//  request.getSession(false).removeAttribute("dataMapgblsave");
            }
            //end sandeep
            int originCurrVal = onecontainer.width;
            String CurrValue = null;
            String valu = null;
            String value = null;
            if (oneviewtypedate != null && oneviewtypedate.equalsIgnoreCase("true")) {
                if (action != null && action.equalsIgnoreCase("save")) {
                } else {
                    if (!onecontainer.timedetails.isEmpty()) {
                        tiemdeatilsArray = onecontainer.timedetails;
                        request.setAttribute("OneviewTiemDetails", tiemdeatilsArray);
                        value = "";
                        valu = "";
                        String mont = "";
                        CurrValue = "";
                        value = tiemdeatilsArray.get(2);
                        valu = value.substring(0, 2);
                        mont = value.substring(3, 5);
                        CurrValue = mont.concat("/").concat(valu).concat(value.substring(5));
                        value = tiemdeatilsArray.get(3);
                        valu = tiemdeatilsArray.get(4);
                    } else {
                        ProgenParam paramdate = new ProgenParam();
                        value = "";
                        valu = "";
                        String mont = "";
                        CurrValue = "";
                        value = paramdate.getdateforpage();
                        valu = value.substring(0, 2);
                        mont = value.substring(3, 5);
                        CurrValue = mont.concat("/").concat(valu).concat(value.substring(5));
                        value = "Month";
                        valu = "Last Period";
                    }
                }
            } else {
                if (oneviewtypedate != null && (oneviewtypedate.equalsIgnoreCase("true") || oneviewtypedate.equalsIgnoreCase("false"))) {
                    if (action != null && action.equalsIgnoreCase("save")) {
                    } else {
                        if (!onecontainer.timedetails.isEmpty()) {
                            tiemdeatilsArray = onecontainer.timedetails;
                            request.setAttribute("OneviewTiemDetails", tiemdeatilsArray);
                            value = "";
                            valu = "";
                            String mont = "";
                            CurrValue = "";
                            value = tiemdeatilsArray.get(2);
                            valu = value.substring(0, 2);
                            mont = value.substring(3, 5);
                            CurrValue = mont.concat("/").concat(valu).concat(value.substring(5));
                            value = tiemdeatilsArray.get(3);
                            valu = tiemdeatilsArray.get(4);
                        } else {
                            ProgenParam paramdate = new ProgenParam();
                            value = "";
                            valu = "";
                            String mont = "";
                            CurrValue = "";
                            value = paramdate.getdateforpage();
                            valu = value.substring(0, 2);
                            mont = value.substring(3, 5);
                            CurrValue = mont.concat("/").concat(valu).concat(value.substring(5));
                            value = "Month";
                            valu = "Last Period";
                        }

                    }
                }
            }
            if (Float.parseFloat(oneVersion) < (float) 2.0) {
                String regionFile = reportTemplateDAO.getRegionOneview(oneviewID);
                File file = new File(oldAdvHtmlFileProps + "/" + regionFile);
                if (file.exists()) {
                    FileInputStream fis3 = new FileInputStream(oldAdvHtmlFileProps + "/" + regionFile);
                    ObjectInputStream ois3 = new ObjectInputStream(fis3);
                    String v = (String) ois3.readObject();
                    StringBuilder testBuild = new StringBuilder(v);
                    response.getWriter().print(testBuild.toString());
                }
            } else {
                List<OneViewLetDetails> oneviewletDetails = onecontainer.onviewLetdetails;
                HashMap tempRegFileMap = onecontainer.getTempRegHashMap();
                HashMap regFileMap = onecontainer.getTempRegHashMap();

                ArrayList<Integer> al = new ArrayList();
                int rowIndex = -1;
                boolean flag = false;

                if (oneviewletDetails != null && !oneviewletDetails.isEmpty()) {
                    if (Float.parseFloat(oneVersion) == (float) 2.5) {
//                finalStringVal.append("<div  align=\"left\" style=\"width:100%\">");
                        finalStringVal.append("<iframe  scrolling='yes'  height='100%' width='100%' frameborder='0' id='regionTableIdgrid'></iframe>");
                    } else {
                        finalStringVal.append("<div  align=\"left\" style=\"width:100%\">");
                        finalStringVal.append("<table align='right'><tr><td style='display:none;'>").append("<input type='text' id='dateIdValue' name='' value='" + CurrValue + "'></input>").append("</td>").append("<td style='display:none;'>").append("<input type='text' id='durationId' name='' value='" + value + "'></input>").append("</td>").append("<td style='display:none;'>").append("<input type='text' id='comareWithId' name='' value='" + valu + "'></input>").append("</td><td><input type='text' id='oneSetting_EveryTime' value='" + isEveryTimeUpdate + "'style='display:none;'></input></td><td><input type='text' id='oneSetting_Hidden' name='' value='" + settingType + "'style='display:none;'></input></td></tr></table>"); //<td id =\"addreginView\" ><a class=\"ui-icon ui-icon-plusthick\"  href=\"javascript:void(0)\" title=\"Add Region\" onclick=\"addRegioninViewer("+oneviewID+")\" ></a></td>

                        if (onecontainer.getOneViewType() != null && (onecontainer.getOneViewType().equalsIgnoreCase("Business TemplateView") || onecontainer.getOneViewType().equalsIgnoreCase("Measure Based Business Template"))) {
                            finalStringVal.append(oneViewBD.generateBusinessTemplateView(request, response, onecontainer));
                            Container container = onecontainer.getContainer();
                            container.setSessionContext(session, container);
                        } else {
                            ArrayListMultimap<Integer, Integer> rowinfo = ArrayListMultimap.create();
                            for (int i = 0; i < oneviewletDetails.size(); i++) {
                                OneViewLetDetails detail = oneviewletDetails.get(i);
                                Container container = null;
                                String repid = detail.getRepId();
                                container = detail.getContainer();
                                HashMap map = new HashMap();
                                map.put(repid, container);
//                map=map1;
                                session.setAttribute("PROGENTABLES", map);

                                if (detail != null && detail.getRoleId() != null) {
                                    oneViewBD.checkRoleAssign(oneviewID, detail.getRoleId());
                                }
                                rowinfo.put(detail.getRow(), detail.getCol());
                                int curModWidth = 0;
                                if (currInnerWidth != null && !currInnerWidth.equalsIgnoreCase("") && originCurrVal > 0) {
                                    curModWidth = (Integer.parseInt(currInnerWidth) * detail.getWidth()) / originCurrVal;
                                    detail.setWidth(curModWidth);
                                }
                            }
                            if (currInnerWidth != null && !currInnerWidth.equalsIgnoreCase("") && originCurrVal > 0) {
                                onecontainer.width = Integer.parseInt(currInnerWidth);
                            }
                            for (int count = 0; count < rowinfo.keySet().size(); count++) {
                                int buildTable = 0;
                                rowIndex++;
                                int colNum = 0;
                                int rowNum = 0;
                                int rowSpanNum = 0;
                                int colSpanNum = 0;
                                List<Integer> dashlets = rowinfo.get(rowIndex);
                                al.add(rowIndex);
                                int numOfDashlets = dashlets.size();
                                int numOfCols = numOfDashlets;
                                for (int p = 0; p < oneviewletDetails.size(); p++) {
                                    OneViewLetDetails detail = oneviewletDetails.get(p);
                                    int row = detail.getRow();
                                    int col = detail.getCol();
                                    int rowSpan = detail.getRowSpan();
                                    int colSpan = detail.getColSpan();
                                    if (row == rowIndex) {
                                        if (rowSpan == 1) {
                                            colNum = col;
                                            rowNum = row;
                                            rowSpanNum = rowSpan;
                                            colSpanNum = colSpan;
                                            if (colSpan > 1) {
                                                numOfCols = numOfCols + (colSpan - 1);
                                            }
                                            buildTable = 1;
                                            flag = false;
                                        } else if (rowSpan > 1) {
                                            colNum = col;
                                            rowNum = row;
                                            rowSpanNum = rowSpan;
                                            colSpanNum = colSpan;

                                            if (colSpan > 1) {
                                                numOfCols = numOfCols + (colSpan - 1);
                                            }
                                            flag = true;
                                        }
                                        if (flag == true) {
                                            buildTable = 2;
                                            break;
                                        }
                                    }
                                }
                                if (buildTable == 1) {
                                    finalStringVal.append("<table style='table-layout: fixed; width: 100%; border-spacing: 15px;'>");
                                    for (int i = 0; i < oneviewletDetails.size(); i++) {
                                        OneViewLetDetails detail = oneviewletDetails.get(i);
                                        int row = detail.getRow();
                                        int col = detail.getCol();

                                        if (row == rowNum) {
                                            if (col == 0) {
                                                finalStringVal.append("<tr width=100%>");
                                                finalStringVal.append("<td id='" + detail.getNoOfViewLets() + "' width='" + detail.getWidth() + "px' style='height:" + detail.getHeight() + "px;' rowspan='" + detail.getRowSpan() + "' colspan='" + detail.getColSpan() + "'>");
                                                if (action.equalsIgnoreCase("open")) {
                                                    if (securityfilters != null && securityfilters.getRowCount() > 0 && detail.getReptype().equalsIgnoreCase("measures")) {
                                                        String result = oneViewBD.buildRegionData(request, response, onecontainer, detail);
                                                        finalStringVal.append(result);
                                                    } else {
                                                        File file = new File(oldAdvHtmlFileProps + "/" + regHashMap.get(i));
                                                        if (file.exists()) {
                                                            FileInputStream fis3 = new FileInputStream(oldAdvHtmlFileProps + "/" + regHashMap.get(i));
                                                            ObjectInputStream ois3 = new ObjectInputStream(fis3);
                                                            String v = (String) ois3.readObject();
                                                            finalStringVal.append(v);
                                                        }
                                                    }
                                                } else if (action.equalsIgnoreCase("run")) {
                                                    String result = oneViewBD.buildRegionData(request, response, onecontainer, detail);
                                                    oneViewBD.writeOneviewRegData(onecontainer, result, detail.getNoOfViewLets(), request);
                                                    finalStringVal.append(result);
                                                } else {
                                                    File file = new File(advHtmlFileProps + "/" + tempRegFileMap.get(i));
                                                    if (file.exists()) {
                                                        FileInputStream fis3 = new FileInputStream(advHtmlFileProps + "/" + tempRegFileMap.get(i));
                                                        ObjectInputStream ois3 = new ObjectInputStream(fis3);
                                                        String v = (String) ois3.readObject();
                                                        oneViewBD.response = response;
                                                        v = oneViewBD.saveOneviewRegData(onecontainer, v, detail.getNoOfViewLets(), request);
                                                        finalStringVal.append(v);
                                                    }
                                                }
                                                finalStringVal.append("</td>");
                                            } else {
                                                finalStringVal.append("<td id='" + detail.getNoOfViewLets() + "' width='" + detail.getWidth() + "px' style='height:" + detail.getHeight() + "px;' rowspan='" + detail.getRowSpan() + "' colspan='" + detail.getColSpan() + "'>");
                                                if (action.equalsIgnoreCase("open")) {
                                                    if (securityfilters != null && securityfilters.getRowCount() > 0 && detail.getReptype().equalsIgnoreCase("measures")) {
                                                        String result = oneViewBD.buildRegionData(request, response, onecontainer, detail);
                                                        finalStringVal.append(result);
                                                    } else {
                                                        File file = new File(oldAdvHtmlFileProps + "/" + regHashMap.get(i));
                                                        if (file.exists()) {
                                                            FileInputStream fis3 = new FileInputStream(oldAdvHtmlFileProps + "/" + regHashMap.get(i));
                                                            ObjectInputStream ois3 = new ObjectInputStream(fis3);
                                                            String v = (String) ois3.readObject();
                                                            finalStringVal.append(v);
                                                        }
                                                    }
                                                } else if (action.equalsIgnoreCase("run")) {
                                                    String result = oneViewBD.buildRegionData(request, response, onecontainer, detail);
                                                    oneViewBD.writeOneviewRegData(onecontainer, result, detail.getNoOfViewLets(), request);
                                                    finalStringVal.append(result);
                                                } else {
                                                    File file = new File(advHtmlFileProps + "/" + tempRegFileMap.get(i));
                                                    if (file.exists()) {
                                                        FileInputStream fis3 = new FileInputStream(advHtmlFileProps + "/" + tempRegFileMap.get(i));
                                                        ObjectInputStream ois3 = new ObjectInputStream(fis3);
                                                        String v = (String) ois3.readObject();
                                                        oneViewBD.response = response;
                                                        v = oneViewBD.saveOneviewRegData(onecontainer, v, detail.getNoOfViewLets(), request);
                                                        finalStringVal.append(v);
                                                    }
                                                }
                                                finalStringVal.append("</td>");
                                            }
                                        }
                                    }
                                    finalStringVal.append("</tr></table>");
                                    al = new ArrayList();
                                } else if (buildTable == 2) {
                                    int startTableRowNum = rowNum;
                                    int maxRows = rowNum + rowSpanNum;
                                    int endTableRowNum = maxRows;
                                    int newRowNum = rowNum;
                                    int newRowSpanNum = rowSpanNum;
                                    int newColNum = colNum;
                                    int newColSpanNum = colSpanNum;
                                    int newMaxRows = maxRows;
                                    for (int k = rowNum; k < maxRows; k++) {
                                        for (int n = 0; n < oneviewletDetails.size(); n++) //loop-^295
                                        {
                                            OneViewLetDetails detail = oneviewletDetails.get(n);
                                            int row = detail.getRow();
                                            int col = detail.getCol();
                                            int rowSpan = detail.getRowSpan();
                                            int colSpan = detail.getColSpan();

                                            if (row >= rowNum && row < maxRows) {
                                                if (row != rowNum || col != colNum) {
                                                    if (rowSpan > 1) {
                                                        if (colSpan > 1) {
                                                            numOfCols = numOfCols + (colSpan - 1);
                                                        }
                                                        newRowNum = row;
                                                        newColNum = col;
                                                        newRowSpanNum = rowSpan;
                                                        newColSpanNum = colSpan;
                                                        newMaxRows = newRowNum + newRowSpanNum;
                                                        //                                    flag=true;
                                                    } else {
                                                        if (colSpan > 1) {
                                                            numOfCols = numOfCols + (colSpan - 1);
                                                        }
                                                    }

                                                    if (newMaxRows > maxRows) {
                                                        maxRows = newMaxRows;
                                                        rowNum = newRowNum;
                                                        colNum = newColNum;
                                                        rowSpanNum = newRowSpanNum;
                                                        colSpanNum = newColSpanNum;
                                                        endTableRowNum = newMaxRows;
                                                        flag = true;
                                                    } else {
                                                        flag = false;
                                                    }
                                                    if (flag == true) {
                                                        break;
                                                    }
                                                }
                                            }
                                        }
                                        if (flag == false) {
                                            k++;
                                        }
                                    }

                                    finalStringVal.append("<table style='table-layout: fixed; width: 100%; border-spacing: 15px;'>");
                                    int prevRow = 0;
                                    for (int i = 0; i < oneviewletDetails.size(); i++) {
                                        OneViewLetDetails detail = oneviewletDetails.get(i);
                                        int row = detail.getRow();
                                        int col = detail.getCol();

                                        if (row >= startTableRowNum && row < endTableRowNum) {
                                            if (row == startTableRowNum && col == 0) {
                                                prevRow = row;
                                                finalStringVal.append("<tr width=100%>");
                                                finalStringVal.append("<td id='" + detail.getNoOfViewLets() + "' width='" + detail.getWidth() + "px' style='height:" + detail.getHeight() + "px;' rowspan='" + detail.getRowSpan() + "' colspan='" + detail.getColSpan() + "'>");
                                                if (action.equalsIgnoreCase("open")) {
                                                    if (securityfilters != null && securityfilters.getRowCount() > 0 && detail.getReptype().equalsIgnoreCase("measures")) {
                                                        String result = oneViewBD.buildRegionData(request, response, onecontainer, detail);
                                                        finalStringVal.append(result);
                                                    } else {
                                                        File file = new File(oldAdvHtmlFileProps + "/" + regHashMap.get(i));
                                                        if (file.exists()) {
                                                            FileInputStream fis3 = new FileInputStream(oldAdvHtmlFileProps + "/" + regHashMap.get(i));
                                                            ObjectInputStream ois3 = new ObjectInputStream(fis3);
                                                            String v = (String) ois3.readObject();
                                                            finalStringVal.append(v);
                                                        }
                                                    }
                                                } else if (action.equalsIgnoreCase("run")) {
                                                    String result = oneViewBD.buildRegionData(request, response, onecontainer, detail);
                                                    oneViewBD.writeOneviewRegData(onecontainer, result, detail.getNoOfViewLets(), request);
                                                    finalStringVal.append(result);
                                                } else {
                                                    File file = new File(advHtmlFileProps + "/" + tempRegFileMap.get(i));
                                                    if (file.exists()) {
                                                        FileInputStream fis3 = new FileInputStream(advHtmlFileProps + "/" + tempRegFileMap.get(i));
                                                        ObjectInputStream ois3 = new ObjectInputStream(fis3);
                                                        String v = (String) ois3.readObject();
                                                        oneViewBD.response = response;
                                                        v = oneViewBD.saveOneviewRegData(onecontainer, v, detail.getNoOfViewLets(), request);
                                                        finalStringVal.append(v);
                                                    }
                                                }
                                                finalStringVal.append("</td>");
                                            } else if (row == prevRow) {
                                                finalStringVal.append("<td id='" + detail.getNoOfViewLets() + "' width='" + detail.getWidth() + "px' style='height:" + detail.getHeight() + "px;' rowspan='" + detail.getRowSpan() + "' colspan='" + detail.getColSpan() + "'>");
                                                if (action.equalsIgnoreCase("open")) {
                                                    if (securityfilters != null && securityfilters.getRowCount() > 0 && detail.getReptype().equalsIgnoreCase("measures")) {
                                                        String result = oneViewBD.buildRegionData(request, response, onecontainer, detail);
                                                        finalStringVal.append(result);
                                                    } else {
                                                        File file = new File(oldAdvHtmlFileProps + "/" + regHashMap.get(i));
                                                        if (file.exists()) {
                                                            FileInputStream fis3 = new FileInputStream(oldAdvHtmlFileProps + "/" + regHashMap.get(i));
                                                            ObjectInputStream ois3 = new ObjectInputStream(fis3);
                                                            String v = (String) ois3.readObject();
                                                            finalStringVal.append(v);
                                                        }
                                                    }
                                                } else if (action.equalsIgnoreCase("run")) {
                                                    String result = oneViewBD.buildRegionData(request, response, onecontainer, detail);
                                                    oneViewBD.writeOneviewRegData(onecontainer, result, detail.getNoOfViewLets(), request);
                                                    finalStringVal.append(result);
                                                } else {
                                                    File file = new File(advHtmlFileProps + "/" + tempRegFileMap.get(i));
                                                    if (file.exists()) {
                                                        FileInputStream fis3 = new FileInputStream(advHtmlFileProps + "/" + tempRegFileMap.get(i));
                                                        ObjectInputStream ois3 = new ObjectInputStream(fis3);
                                                        String v = (String) ois3.readObject();
                                                        oneViewBD.response = response;
                                                        v = oneViewBD.saveOneviewRegData(onecontainer, v, detail.getNoOfViewLets(), request);
                                                        finalStringVal.append(v);
                                                    }
                                                }
                                                finalStringVal.append("</td>");
                                            } else {
                                                finalStringVal.append("</tr>");
                                                prevRow = row;
                                                finalStringVal.append("<tr width=100%>");
                                                if (col == 0) {
                                                    finalStringVal.append("<td id='" + detail.getNoOfViewLets() + "' width='" + detail.getWidth() + "px' style='height:" + detail.getHeight() + "px;' rowspan='" + detail.getRowSpan() + "' colspan='" + detail.getColSpan() + "'>");
                                                    if (action.equalsIgnoreCase("open")) {
                                                        if (securityfilters != null && securityfilters.getRowCount() > 0 && detail.getReptype().equalsIgnoreCase("measures")) {
                                                            String result = oneViewBD.buildRegionData(request, response, onecontainer, detail);
                                                            finalStringVal.append(result);
                                                        } else {
                                                            File file = new File(oldAdvHtmlFileProps + "/" + regHashMap.get(i));
                                                            if (file.exists()) {
                                                                FileInputStream fis3 = new FileInputStream(oldAdvHtmlFileProps + "/" + regHashMap.get(i));
                                                                ObjectInputStream ois3 = new ObjectInputStream(fis3);
                                                                String v = (String) ois3.readObject();
                                                                finalStringVal.append(v);
                                                            }
                                                        }
                                                    } else if (action.equalsIgnoreCase("run")) {
                                                        String result = oneViewBD.buildRegionData(request, response, onecontainer, detail);
                                                        oneViewBD.writeOneviewRegData(onecontainer, result, detail.getNoOfViewLets(), request);
                                                        finalStringVal.append(result);
                                                    } else {
                                                        File file = new File(advHtmlFileProps + "/" + tempRegFileMap.get(i));
                                                        if (file.exists()) {
                                                            FileInputStream fis3 = new FileInputStream(advHtmlFileProps + "/" + tempRegFileMap.get(i));
                                                            ObjectInputStream ois3 = new ObjectInputStream(fis3);
                                                            String v = (String) ois3.readObject();
                                                            oneViewBD.response = response;
                                                            v = oneViewBD.saveOneviewRegData(onecontainer, v, detail.getNoOfViewLets(), request);
                                                            finalStringVal.append(v);
                                                        }
                                                    }
                                                    finalStringVal.append("</td>");
                                                } else {
                                                    finalStringVal.append("<td id='" + detail.getNoOfViewLets() + "' width='" + detail.getWidth() + "px' style='height:" + detail.getHeight() + "px;' rowspan='" + detail.getRowSpan() + "' colspan='" + detail.getColSpan() + "'>");
                                                    if (action.equalsIgnoreCase("open")) {
                                                        if (securityfilters != null && securityfilters.getRowCount() > 0 && detail.getReptype().equalsIgnoreCase("measures")) {
                                                            String result = oneViewBD.buildRegionData(request, response, onecontainer, detail);
                                                            finalStringVal.append(result);
                                                        } else {
                                                            File file = new File(oldAdvHtmlFileProps + "/" + regHashMap.get(i));
                                                            if (file.exists()) {
                                                                FileInputStream fis3 = new FileInputStream(oldAdvHtmlFileProps + "/" + regHashMap.get(i));
                                                                ObjectInputStream ois3 = new ObjectInputStream(fis3);
                                                                String v = (String) ois3.readObject();
                                                                finalStringVal.append(v);
                                                            }
                                                        }
                                                    } else if (action.equalsIgnoreCase("run")) {
                                                        String result = oneViewBD.buildRegionData(request, response, onecontainer, detail);
                                                        oneViewBD.writeOneviewRegData(onecontainer, result, detail.getNoOfViewLets(), request);
                                                        finalStringVal.append(result);
                                                    } else {
                                                        File file = new File(advHtmlFileProps + "/" + tempRegFileMap.get(i));
                                                        if (file.exists()) {
                                                            FileInputStream fis3 = new FileInputStream(advHtmlFileProps + "/" + tempRegFileMap.get(i));
                                                            ObjectInputStream ois3 = new ObjectInputStream(fis3);
                                                            String v = (String) ois3.readObject();
                                                            oneViewBD.response = response;
                                                            v = oneViewBD.saveOneviewRegData(onecontainer, v, detail.getNoOfViewLets(), request);
                                                            finalStringVal.append(v);
                                                        }
                                                    }
                                                    finalStringVal.append("</td>");
                                                }
                                            }
                                        }
                                    }
                                    finalStringVal.append("</tr></table>");
                                    rowIndex = +(endTableRowNum - 1);
                                }
                            }
                        }
                    }
//        finalStringVal.append("<table align='right'><tr><td style='display:none;'>").append("<input type='text' id='dateIdValue' name='' value='" + CurrValue + "'></input>").append("</td>").append("<td style='display:none;'>").append("<input type='text' id='durationId' name='' value='" + value + "'></input>").append("</td>").append("<td style='display:none;'>").append("<input type='text' id='comareWithId' name='' value='" + valu + "'></input>").append("</td><td><input type='text' id='oneSetting_EveryTime' value='" +isEveryTimeUpdate+ "'style='display:none;'></input></td><td><input type='text' id='oneSetting_Hidden' name='' value='" +settingType+ "'style='display:none;'></input></td></tr></table>"); //<td id =\"addreginView\" ><a class=\"ui-icon ui-icon-plusthick\"  href=\"javascript:void(0)\" title=\"Add Region\" onclick=\"addRegioninViewer("+oneviewID+")\" ></a></td>
                    finalStringVal.append("</div>");
                }

                if (!action.equalsIgnoreCase("open") || (onecontainer1.getSettingType() != null && action.equalsIgnoreCase("run") && !onecontainer1.isEveryTimeUpdate())) {

                    String conFileName = reportTemplateDAO.getOneviewFileName(oneviewID);
                    FileOutputStream fos1 = new FileOutputStream(oldAdvHtmlFileProps + "/" + conFileName);
                    ObjectOutputStream oos1 = new ObjectOutputStream(fos1);
                    oos1.writeObject(onecontainer);
                    oos1.flush();
                    oos1.close();
                    fos1.close();
                    String regionFile = null;
                    regionFile = reportTemplateDAO.getRegionOneview(oneviewID);
                    FileOutputStream fos = new FileOutputStream(oldAdvHtmlFileProps + "/" + regionFile);
                    ObjectOutputStream oos = new ObjectOutputStream(fos);
                    oos.writeObject(finalStringVal.toString());
                    oos.flush();
                    oos.close();
                    fos.close();
                    fileName = session.getAttribute("tempFileName").toString();
                    FileOutputStream fosLocal = new FileOutputStream(advHtmlFileProps + "/" + fileName);
                    ObjectOutputStream oosLoca = new ObjectOutputStream(fosLocal);
                    oosLoca.writeObject(onecontainer);
                    oosLoca.flush();
                    oosLoca.close();
                    fosLocal.close();
                    if (onecontainer1.getSettingType() != null && action.equalsIgnoreCase("run") && !onecontainer1.isEveryTimeUpdate()) {
                        for (int i = 0; i < onecontainer.getTempRegHashMap().size(); i++) {
                            String innerData = (String) oneViewBD.readFileDetails(advHtmlFileProps, onecontainer.getTempRegHashMap().get(i).toString());
                            oneViewBD.writeFileDetails(oldAdvHtmlFileProps, onecontainer.getRegHashMap().get(i).toString(), innerData);
                        }
                    }
                }
            }
        } else {
            finalStringVal.append("The File Does not Exist in this System");
        }
        response.getWriter().print(finalStringVal.toString());

        return null;
    }

    /**
     * Check the One View Version and Returns The Version added by ANIL
     */
    public ActionForward versionCheck(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String oneviewID = request.getParameter("oneViewIdValue");
        PbReturnObject retObj = null;
        ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();
        retObj = reportTemplateDAO.getOneviewFileNam(oneviewID);
        String oneVersion = retObj.getFieldValueString(0, 9);
        StringBuilder oneVersionBuilder = new StringBuilder();
        oneVersionBuilder.append("{ oneVersion: [\"").append(oneVersion).append("\"]}");
        response.getWriter().print(oneVersionBuilder.toString());
        return null;
    }

    public ActionForward saveCanvasImage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(false);
        String encodedImage = request.getParameter("base64data");
        String oneViewId = request.getParameter("oneViewId");
        String viewletId = request.getParameter("viewletId");

        String imageCode = encodedImage.replace("data:image/png;base64,", "");
        byte[] decodedData = new sun.misc.BASE64Decoder().decodeBuffer(imageCode);
        ByteArrayInputStream in = new ByteArrayInputStream(decodedData);
        BufferedImage convertImage = ImageIO.read(in);
        String imgpath = System.getProperty("java.io.tmpdir") + "/" + viewletId + "_img.png";
//        String path="C:"+File.separator+"Documents and Settings"+File.separator+"Administrator"+File.separator+"Desktop"+File.separator+viewletId+"_img.png";
//        String imgpath="/home/progen/Desktop/"+viewletId+"img_.png";
//        File file=new File(path);
        File file1 = new File(imgpath);
//        FileOutputStream fos=new FileOutputStream(file);
//        fos.write(decodedData);
        ImageIO.write(convertImage, "png", file1);
        in.close();






        return null;

//        HttpSession session = request.getSession(false);
//        String encodedImage=request.getParameter("base64data");
//        String oneViewId=request.getParameter("oneViewId");
//        String viewletId=request.getParameter("viewletId");
//
//        String imageCode=encodedImage.replace("data:image/png;base64,", "");
//        byte[] decodedData = new sun.misc.BASE64Decoder().decodeBuffer(imageCode);
//        ByteArrayInputStream in = new ByteArrayInputStream(decodedData);
//        BufferedImage convertImage = ImageIO.read(in);
//        //Image img=convertImage;
//        //pdfdriver.getPdfImage(img);
//        int type = convertImage.getType() == 0? BufferedImage.TYPE_INT_ARGB : convertImage.getType();
//        BufferedImage resizeImagePng = resizeImage(convertImage, type);
//        //String path="C:/Users/Progen/Desktop/"+viewletId+"_.txt";
//        String imgpath = System.getProperty("java.io.tmpdir")+viewletId+"img_.png";
//       // String imgpath="C:/Users/Progen/Desktop/"+viewletId+"img_.png";
//        //File file=new File(path);
//        File file1=new File(imgpath);
//        //FileOutputStream fos=new FileOutputStream(file);
//        //fos.write(decodedData);
//        ImageIO.write(convertImage, "png", file1);
//        //
//
//        return null;
    }

    public ActionForward getAssignedGraphDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(false);
        if (session != null) {
            try {
                ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();
                OnceViewContainer onecontainer = null;
                OneViewBD viewBd = new OneViewBD();
                String oneViewId = request.getParameter("oneViewId");
                String oldAdvHtmlFileProps = (String) request.getSession(false).getAttribute("oldAdvHtmlFileProps");
                String fileName = reportTemplateDAO.getOneviewFileName(oneViewId);
                FileInputStream fis2 = new FileInputStream(oldAdvHtmlFileProps + "/" + fileName);
                ObjectInputStream ois2 = new ObjectInputStream(fis2);
                onecontainer = (OnceViewContainer) ois2.readObject();
                ois2.close();
                List<OneViewLetDetails> viewLetDetails = onecontainer.onviewLetdetails;
                ArrayList details = new ArrayList();
                String version = "1.0";
                String finalString = "";
                version = viewBd.getOneViewVersion(oneViewId);
                if (Float.parseFloat(version) >= 2.0) {
                    for (OneViewLetDetails viewlet : viewLetDetails) {
                        if (viewlet.getReptype().equalsIgnoreCase("repGraph")) {
                            details.add(viewlet.getNoOfViewLets());
                        }
                        if (viewlet.getReptype().equalsIgnoreCase("measures") && viewlet.isTrendGraph()) {
                            details.add(viewlet.getNoOfViewLets());
                        }
                        if (viewlet.getReptype().equalsIgnoreCase("repKpis") && viewlet.getKpiType().equalsIgnoreCase("KPIGraph")) {
                            details.add(viewlet.getNoOfViewLets());
                        }
                    }
                    Gson gson = new Gson();
                    String jsonString = gson.toJson(details);
                    finalString = "{details:" + jsonString + "}";
                } else {
                    finalString = "Not Support";
                }

                response.getWriter().print(finalString);
            } catch (Exception e) {
                logger.error("Exception:", e);
            }
        }
        return null;
    }

    public ActionForward buildOneViewPdf(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(false);
        if (session != null) {
            ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();
            String oneViewId = request.getParameter("oneViewId");
            String oldAdvHtmlFileProps = (String) request.getSession(false).getAttribute("oldAdvHtmlFileProps");
            String fileName = reportTemplateDAO.getOneviewFileName(oneViewId);
            OneViewBD oneViewBd = new OneViewBD();
            oneViewBd.setFileName(fileName);
            oneViewBd.setGlobalPath(oldAdvHtmlFileProps);
            oneViewBd.request = request;
            oneViewBd.response = response;
            oneViewBd.mapping = mapping;
            oneViewBd.form = form;
            oneViewBd.userId = session.getAttribute("USERID").toString();
            String pdfName = oneViewBd.buildOneViewPdf(oneViewId);
            session.setAttribute("OneviewName", pdfName);
//              session.setAttribute("dlType","oneviewPdf");

        }
        return null;
//        return mapping.findForward("pbDownload");

    }

    public ActionForward saveEachReg(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(false);
        String oneviewID = request.getParameter("oneViewIdValue");
        String chartname = request.getParameter("chartname");
        String idArr = request.getParameter("idArr");
        String drillviewby = request.getParameter("drillviewby");
        String action = request.getParameter("action");
        String busrolename = request.getParameter("busrolename");
        PbReturnObject retObj = null;
        OnceViewContainer onecontainer = null;
        OneViewLetDetails detail = new OneViewLetDetails();
        ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();
        OneViewBD oneViewBD = new OneViewBD();
        String regId = request.getParameter("regId");

        //kruthika
        String graphtoreport = request.getParameter("graphtoreport");

        String advHtmlFileProps = (String) request.getSession(false).getAttribute("advHtmlFileProps");
        String oldAdvHtmlFileProps = (String) request.getSession(false).getAttribute("oldAdvHtmlFileProps");
        String fileName = session.getAttribute("tempFileName").toString();

        FileInputStream fis = new FileInputStream(advHtmlFileProps + "/" + fileName);
        ObjectInputStream ois = new ObjectInputStream(fis);
        onecontainer = (OnceViewContainer) ois.readObject();
        ois.close();
        detail = onecontainer.onviewLetdetails.get(Integer.parseInt(regId));
        if ((graphtoreport != null && graphtoreport.equalsIgnoreCase("true")) || (graphtoreport != null && graphtoreport.equalsIgnoreCase("true"))) {
            detail.setgraphtoreport(graphtoreport);
        }
        String repid = detail.getRepId();
        String result = "";
        HashMap tempRegFileMap = onecontainer.getTempRegHashMap();
        File file1 = new File(advHtmlFileProps + "/" + tempRegFileMap.get(Integer.parseInt(regId)));

        if (file1.exists()) {
            try {
                oneViewBD.form = form;
                oneViewBD.mapping = mapping;
                request.setAttribute("OneviewTiemDetails", onecontainer.timedetails);
                //sandeep
                detail.setchartname(chartname);
                detail.setchartdrills(idArr);
                detail.setchartrefreshdrills(idArr);
                detail.setdrillviewby(drillviewby);
                if (detail.getReptype() != null && detail.getReptype().equalsIgnoreCase("template")) {
                    detail.setRepName("Template");
                }
                //end
                result = oneViewBD.buildRegionData(request, response, onecontainer, detail);
                //sandeep
                if (result != null && (result == null ? "" != null : !result.equals(""))) {
                } else {
                    detail.setReptype("template");
                    detail.setRepName("Template");
                    result = oneViewBD.buildRegionData(request, response, onecontainer, detail);
                }
                //end
                oneViewBD.writeOneviewRegData(onecontainer, result, detail.getNoOfViewLets(), request); /*
                 * local save
                 */
                /*
                 * else{ FileInputStream fis3 = new
                 * FileInputStream(advHtmlFileProps+"/"
                 * +tempRegFileMap.get(Integer.parseInt(regId)));
                 * ObjectInputStream ois3 = new ObjectInputStream(fis3); result
                 * = (String) ois3.readObject(); ois3.close();
}
                 */

                oneViewBD.saveOneviewRegData(onecontainer, result, detail.getNoOfViewLets(), request); /*
                 * global save
                 */
                if (detail != null && detail.getRoleId() != null) {
                    oneViewBD.checkRoleAssign(oneviewID, detail.getRoleId());
                }

                String conFileName = reportTemplateDAO.getOneviewFileName(oneviewID);
                // fileName = retObj.getFieldValueString(0, "ONEVIEW_FILE");
                FileOutputStream fos = new FileOutputStream(oldAdvHtmlFileProps + "/" + conFileName);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(onecontainer);
                oos.flush();
                oos.close();
                if (detail.getReptype().equalsIgnoreCase("repGraph")) {
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("result", result);
                    map.put("repid", repid);
                    Container container;
                    PbReportViewerDAO dao = new PbReportViewerDAO();
                    container = Container.getContainerFromSession(request, repid);
                    String userId = String.valueOf(session.getAttribute("USERID"));
                    if (!detail.isOneviewReportTimeDetails()) {
                        dao.savextchartoneview(request, repid, detail.getRepName(), container, "true", busrolename, userId, detail);
                    }
                    Gson json = new Gson();
                    String jsonString = json.toJson(map);
                    PrintWriter out = response.getWriter();
                    out.print(jsonString);
                } else {
                    response.getWriter().print(result);
                }
            } catch (Exception e) {
                response.getWriter().print("Region data not saved Problem occured...!!");
                logger.error("Exception:", e);
            }

        }

        return null;
    }

    public ActionForward getDimesions(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String result = "";
        HttpSession session = request.getSession(false);
        String oneviewID = request.getParameter("oneviewID");
        String advHtmlFileProps = (String) request.getSession(false).getAttribute("advHtmlFileProps");
        OneViewLetDetails detail = new OneViewLetDetails();
        OnceViewContainer onecontainer = new OnceViewContainer();
        ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();
        String fileName = session.getAttribute("tempFileName").toString();
        FileInputStream fis2 = new FileInputStream(advHtmlFileProps + "/" + fileName);
        ObjectInputStream ois2 = new ObjectInputStream(fis2);
        onecontainer = (OnceViewContainer) ois2.readObject();
        ois2.close();
        onecontainer.getFilterBusinessRole();
        onecontainer.getReportParameterValues();
        if (session != null) {
            String userID = session.getAttribute("USERID").toString();
            result = new OneViewBD().getDimensions(userID);
        }
        StringBuilder strngbulder = new StringBuilder();
        strngbulder.append("{ Dimensions:[");
        strngbulder.append("\'").append(result).append("\'").append("],");
        strngbulder.append("roleId:[");
        strngbulder.append("\"").append(onecontainer.getFilterBusinessRole()).append("\"").append("],");
        strngbulder.append("globalparameterVals:[");
        strngbulder.append("\"").append(onecontainer.getReportParameterValues()).append("\"").append("]}");

        response.getWriter().print(strngbulder.toString());
        return null;
    }

    public ActionForward applyGlobalFilter(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String finalFilter = request.getParameter("finalFilter");
        String oneviewID = request.getParameter("oneviewID");
        String selectedMeas = request.getParameter("selectedMeas");
        String BisiessRole = request.getParameter("BisiessRole");
        HttpSession session = request.getSession(false);
        String advHtmlFileProps = (String) request.getSession(false).getAttribute("advHtmlFileProps");
        OneViewLetDetails detail = new OneViewLetDetails();
        OnceViewContainer onecontainer = new OnceViewContainer();
        ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();
        //
        String fileName = session.getAttribute("tempFileName").toString();
        FileInputStream fis2 = new FileInputStream(advHtmlFileProps + "/" + fileName);
        ObjectInputStream ois2 = new ObjectInputStream(fis2);
        onecontainer = (OnceViewContainer) ois2.readObject();
        ois2.close();
        LinkedHashMap reportParams = new LinkedHashMap();
        List<String> finalList = new ArrayList<String>();
        finalList.add(finalFilter);
        reportParams.put(selectedMeas, finalList);
        onecontainer.setReportParameterValues(reportParams);
        onecontainer.setFilterBusinessRole(BisiessRole);

        FileOutputStream fos = new FileOutputStream(advHtmlFileProps + "/" + fileName);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(onecontainer);
        oos.flush();
        oos.close();
        return null;
    }

    public ActionForward getFilterDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(false);
        String advHtmlFileProps = (String) request.getSession(false).getAttribute("advHtmlFileProps");
        OneViewLetDetails detail = new OneViewLetDetails();
        OnceViewContainer onecontainer = new OnceViewContainer();
        ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();
        //
        String fileName = session.getAttribute("tempFileName").toString();
        FileInputStream fis2 = new FileInputStream(advHtmlFileProps + "/" + fileName);
        ObjectInputStream ois2 = new ObjectInputStream(fis2);
        onecontainer = (OnceViewContainer) ois2.readObject();
        ois2.close();
        onecontainer.getFilterBusinessRole();
        onecontainer.getReportParameterValues();
        StringBuilder strngbulder = new StringBuilder();
        strngbulder.append("{ roleId:[");
        strngbulder.append("\"").append(onecontainer.getFilterBusinessRole()).append("\"").append("],");
        strngbulder.append("globalparameterVals:[");
        strngbulder.append("\"").append(onecontainer.getReportParameterValues()).append("\"").append("]}");

        response.getWriter().print(strngbulder.toString());
        return null;
    }

    public ActionForward resetGlobalFilter(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(false);
        String advHtmlFileProps = (String) request.getSession(false).getAttribute("advHtmlFileProps");
        OneViewLetDetails detail = new OneViewLetDetails();
        OnceViewContainer onecontainer = new OnceViewContainer();
        ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();
        //
        String fileName = session.getAttribute("tempFileName").toString();
        FileInputStream fis2 = new FileInputStream(advHtmlFileProps + "/" + fileName);
        ObjectInputStream ois2 = new ObjectInputStream(fis2);
        onecontainer = (OnceViewContainer) ois2.readObject();
        ois2.close();
        LinkedHashMap reportParams = new LinkedHashMap();
        onecontainer.setReportParameterValues(reportParams);
        onecontainer.setFilterBusinessRole(null);

        FileOutputStream fos = new FileOutputStream(advHtmlFileProps + "/" + fileName);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(onecontainer);
        oos.flush();
        oos.close();
        return null;
    }

    public ActionForward oneviewMeasureAlerts(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession(false);

        String innerdivTable = request.getParameter("innerdivTable");
        String[] innerValues = innerdivTable.split(",");
        ArrayList oneviewDetails = new ArrayList();

        oneviewDetails.addAll(Arrays.asList(innerValues));

        String elemId = request.getParameter("eleId");
        String roleId = request.getParameter("roleId");
        String measName = request.getParameter("measName");
        String yearly = request.getParameter("Year");
        String quartly = request.getParameter("Qtr");
        String monthly = request.getParameter("Month");
        String weekly = request.getParameter("Week");
        String daily = request.getParameter("Day");

        List<String> attacheDetails = new ArrayList<String>();

        if (yearly != null && !yearly.equalsIgnoreCase("")) {
            attacheDetails.add(yearly);
        }
        if (quartly != null && !quartly.equalsIgnoreCase("")) {
            attacheDetails.add(quartly);
        }
        if (monthly != null && !monthly.equalsIgnoreCase("")) {
            attacheDetails.add(monthly);
        }
        if (weekly != null && !weekly.equalsIgnoreCase("")) {
            attacheDetails.add(weekly);
        }
        if (daily != null && !daily.equalsIgnoreCase("")) {
            attacheDetails.add(daily);
        }

        String schedulerName = request.getParameter("scheduleName");
        String mailIds = request.getParameter("usertextarea");
        String[] condOperator = request.getParameterValues("condOp");
        String[] condStrtVal = request.getParameterValues("sCondVal");
        String[] condEndVal = request.getParameterValues("eCondVal");
        String[] condMail = request.getParameterValues("condMail");
        String[] tagValues = request.getParameterValues("tagValues");
        String absolutOrPercent = request.getParameter("absolutOrPercent");
        String targetValue = request.getParameter("targetValue");
        String percentValue = request.getParameter("percentValue");
        String measureValueCurrVal = request.getParameter("measureValueCurrVal");

        HashMap<Integer, TrackerCondition> trackerCondMap = new HashMap<Integer, TrackerCondition>();
        TrackerCondition trackerCondition = null;
        String oneviewId = oneviewDetails.get(1).toString();
        String oneviewRegionId = oneviewDetails.get(0).toString();
        for (int i = 0; i < condMail.length; i++) {
            if (!condMail[i].equalsIgnoreCase("") && !condStrtVal[i].equalsIgnoreCase("")) {
                trackerCondition = new TrackerCondition();
                trackerCondition.setViewByValue(oneviewId);
                if (targetValue != null && !targetValue.equals("")) {
                    trackerCondition.setTargetValue(Double.parseDouble(targetValue));
                }
                if (percentValue != null && !percentValue.equals("")) {
                    trackerCondition.setDeviationPerVal(Double.parseDouble(percentValue));
                }
                trackerCondition.setOperator(condOperator[i]);
                if (condStrtVal[i] != null && !condStrtVal[i].equals("")) {
                    trackerCondition.setMeasureStartValue(Double.parseDouble(condStrtVal[i]));
                }
                if (condOperator[i].equalsIgnoreCase("<>")) {
                    trackerCondition.setMeasureEndValue(Double.parseDouble(condEndVal[i]));
                }
                trackerCondition.setTagType(schedulerName.concat(":" + tagValues[i]));
                trackerCondition.setViewByValue(absolutOrPercent);

                trackerCondition.setMailIds(condMail[i]);
                trackerCondMap.put(i, trackerCondition);
            }
        }

        String contentType = "H";
        // String contentType=request.getParameter("fileType");
        String startDate = request.getParameter("startdate");
        String endDate = request.getParameter("enddate");
        String periodType = request.getParameter("Data");
        String hrs = request.getParameter("hrs");
        String mins = request.getParameter("mins");
        String frequency = request.getParameter("frequency");
        String particularDay = request.getParameter("particularDay");
        String particularHour = request.getParameter("particularHour");
        String monthParticularDay = request.getParameter("monthParticularDay");
        String alertDateType = request.getParameter("alertDateType");
        String oneveiwDate = request.getParameter("oneveiwDate");
        String checkforCustDate = request.getParameter("checkforCustDate");
        String fromGlobVal = request.getParameter("fromGlobVal");
        String fromSysVal = request.getParameter("fromSysVal");
        String fromSysSign = request.getParameter("fromSysSign");
        String globalSign = request.getParameter("globalSign");

        String alertDate = "";

        String scheduledTime = hrs.concat(":").concat(mins);
        String userId = String.valueOf(request.getSession(false).getAttribute("USERID"));
        Date sDate, eDate;
        DateFormat formatter;
        String value = "";
        String valu = "";
        String mont = "";
        String CurrValue = "";
        String value1 = "";
        String valu1 = "";
        String mont1 = "";
        String CurrValue1 = "";
        value = startDate;
        int slashval = value.indexOf("/");
        int slashLast = value.lastIndexOf("/");
        valu = value.substring(0, slashval);
        mont = value.substring(slashval + 1, slashLast + 1);
        CurrValue = mont.concat(valu).concat(value.substring(slashLast));
        startDate = CurrValue;

        value1 = endDate;
        int slashval1 = value1.indexOf("/");
        int slashLast1 = value1.lastIndexOf("/");
        valu1 = value1.substring(0, slashval1);
        mont1 = value1.substring(slashval1 + 1, slashLast1 + 1);
        CurrValue1 = mont1.concat(valu1).concat(value1.substring(slashLast1));
        endDate = CurrValue1;

        formatter = new SimpleDateFormat("MM/dd/yyyy");
        sDate = formatter.parse(startDate);
        eDate = formatter.parse(endDate);

        OneViewBD oneviewBd = new OneViewBD();
        OneViewLetDetails oneviewletDetails = new OneViewLetDetails();
        OnceViewContainer onceviewDetails = new OnceViewContainer();
        PbReturnObject retObj = null;
        retObj = oneviewBd.getOneviewMeasureOptions(oneviewId);

        FileInputStream fis = new FileInputStream(retObj.getFieldValueString(0, "FILEPATH") + "/" + retObj.getFieldValueString(0, "ONEVIEW_FILE"));
        ObjectInputStream ois = new ObjectInputStream(fis);
        onceviewDetails = (OnceViewContainer) ois.readObject();
        ois.close();
        oneviewletDetails = onceviewDetails.onviewLetdetails.get(Integer.parseInt(oneviewDetails.get(0).toString()));
        String prefix = oneviewletDetails.getPrefixValue();
        String sufix = oneviewletDetails.getSuffixValue();
        String numberFrmt = oneviewletDetails.getFormatVal();
        String roundVal = oneviewletDetails.getRoundVal();

        oneveiwDate = onceviewDetails.timedetails.get(2);
        String sysdate = "";
        String globdate = "";
        Date systemDate, globalDate;
        ProgenParam pParam = new ProgenParam();
        if (alertDateType.equalsIgnoreCase("yesterdate")) {
            alertDate = pParam.getcurrentdateforpage(1);
        }
        if (alertDateType.equalsIgnoreCase("todaydate")) {
            alertDate = pParam.getcurrentdateforpage(0);
        }
        if (alertDateType.equalsIgnoreCase("oneviewdate")) {
            alertDate = oneveiwDate;
        }
        if (alertDateType.equalsIgnoreCase("customdate")) {
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat();
            sdf.applyPattern("MM/dd/yyyy");
            sysdate = sdf.format(date);
            systemDate = sdf.parse(sysdate);
            globalDate = sdf.parse(pParam.getdateforpage());
            globdate = pParam.getdateforpage();
            if (checkforCustDate.equalsIgnoreCase("sysDate")) {
                if (!fromSysVal.equalsIgnoreCase("")) {
                    int val = 0;
                    val = Integer.parseInt(fromSysVal);
                    Calendar startCalendar = Calendar.getInstance();
                    startCalendar.setTime(systemDate);
                    if (fromSysSign.equalsIgnoreCase("+")) {
                        startCalendar.add(Calendar.DATE, +val);
                    } else {
                        startCalendar.add(Calendar.DATE, -val);
                    }
                    alertDate = (startCalendar.get(Calendar.MONTH) + 1) + "/" + startCalendar.get(Calendar.DATE) + "/" + startCalendar.get(Calendar.YEAR);
                } else {
                    alertDate = sysdate;
                }
            } else {
                if (!fromGlobVal.equalsIgnoreCase("")) {
                    int val = 0;
                    val = Integer.parseInt(fromGlobVal);
                    Calendar startCalendar = Calendar.getInstance();
                    startCalendar.setTime(globalDate);
                    if (globalSign.equalsIgnoreCase("+")) {
                        startCalendar.add(Calendar.DATE, +val);
                    } else {
                        startCalendar.add(Calendar.DATE, -val);
                    }
                    alertDate = (startCalendar.get(Calendar.MONTH) + 1) + "/" + startCalendar.get(Calendar.DATE) + "/" + startCalendar.get(Calendar.YEAR);
                } else {
                    alertDate = globdate;
                }
            }
        }
        List<String> dataSelectionTypes = new ArrayList<String>();
        dataSelectionTypes.add(prefix);
        dataSelectionTypes.add(sufix);
        dataSelectionTypes.add(numberFrmt);
        dataSelectionTypes.add(roundVal);
        boolean testConditon = false;
        if (mailIds != null && !mailIds.equalsIgnoreCase("")) {
            testConditon = true;
        }
        ReportSchedule schedule = new ReportSchedule();

        schedule.setSchedulerName(schedulerName);

        schedule.setMeasureValueCurrVal(measureValueCurrVal);

        schedule.setReportmailIds(mailIds);
        schedule.setForConditonalTest(testConditon);
        schedule.setTargetVal(targetValue);
        schedule.setDeviationVal(percentValue);
        schedule.setContenType(contentType);
        schedule.setReportScheduledId(Integer.parseInt(elemId));
        schedule.setScheduledTime(scheduledTime);
        schedule.setFrequency(frequency);
        schedule.setStartDate(sDate);
        schedule.setEndDate(eDate);
        schedule.setFolderId(roleId);
        schedule.setUserId(userId);
        schedule.setElementID(elemId);
        schedule.setDataSelection(periodType);
        schedule.setViewByName(measName);
        schedule.setAlertDateType(alertDateType);
        schedule.setAlertDate(alertDate);
        schedule.setMeasureAlertsConditions(trackerCondMap);
        schedule.setOneviewMeasureOptions(dataSelectionTypes);
        schedule.setFromOneview(true);
        schedule.setUserId((request.getSession(false).getAttribute("USERID")).toString());
        if (frequency.equalsIgnoreCase("Weekly")) {
            schedule.setParticularDay(particularDay);
        } else if (frequency.equalsIgnoreCase("Monthly")) {
            schedule.setParticularDay(monthParticularDay);
        } else if (frequency.equalsIgnoreCase("Hourly")) {
            schedule.setParticularDay(particularHour);
        }

        schedule.setDataSelectionTypes(attacheDetails);

        if (condMail.length > 0 && condStrtVal.length > 0) {
            oneviewBd.insertOneviewMesureAlerts(oneviewId, userId, schedule, oneviewRegionId);
            SchedulerBD bd = new SchedulerBD();
            bd.scheduleReport(schedule, false);
        }
        return null;
    }

    public ActionForward getAdditionalInfo(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String measId = request.getParameter("measId");
        String measName = request.getParameter("measName");
        String oneViewId = request.getParameter("oneViewId");
        String roleId = request.getParameter("roleId");
        String currVal = request.getParameter("currVal");
        String priorVal = request.getParameter("priorVal");
        String changePer = request.getParameter("changePer");
        String repId = request.getParameter("repId");
        String regionId = request.getParameter("regionId");
        String context = request.getParameter("context");
        HttpSession session = request.getSession(false);
        PbReturnObject pbretObjForTime = new PbReturnObject();
        PbReturnObject pbretObjForTimeOnly = new PbReturnObject();
        String[] monthName = {"JAN", "FEB",
            "MAR", "APR", "MAY", "JUN", "JUL",
            "AUG", "SEP", "OCT", "NOV",
            "DEC"};

        String advHtmlFileProps = (String) request.getSession(false).getAttribute("advHtmlFileProps");
        //
        OneViewLetDetails detail = new OneViewLetDetails();
        OnceViewContainer onecontainer = new OnceViewContainer();
        List<String> tiemdeatilsArray = null;

        ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();
//                onecontainer = reportTemplateDAO.getOneViewData(oneviewId);

        // String fileName = reportTemplateDAO.getOneviewFileName(oneViewId);
        String fileName = request.getSession(false).getAttribute("tempFileName").toString();
        FileInputStream fis2 = new FileInputStream(advHtmlFileProps + "/" + fileName);
        ObjectInputStream ois2 = new ObjectInputStream(fis2);
        onecontainer = (OnceViewContainer) ois2.readObject();
        ois2.close();

        detail = onecontainer.onviewLetdetails.get(Integer.parseInt(regionId));
        if (onecontainer.timedetails != null && !onecontainer.timedetails.isEmpty()) {
            tiemdeatilsArray = onecontainer.timedetails;
        }
        ArrayList arl = new ArrayList();
        ArrayList arl1 = new ArrayList();
        ArrayList arl2 = new ArrayList();

        if (onecontainer != null && onecontainer.timedetails != null && !onecontainer.timedetails.isEmpty()) {
            arl = (ArrayList) onecontainer.timedetails;
            arl1.addAll(onecontainer.timedetails);
            arl2.addAll(onecontainer.timedetails);
        } else {
            arl.add("Day");
            arl.add("PRG_STD");
            ProgenParam pramnam = new ProgenParam();
            String date = pramnam.getdateforpage();
            arl.add(date);
            arl.add("Month");
            arl.add("Last Period");

            arl1.add("Day");
            arl1.add("PRG_STD");
            arl1.add(date);
            arl1.add("Month");
            arl1.add("Last Period");

            arl2.addAll(arl1);
        }
        arl1.set(3, "Month");

        String userId = String.valueOf(session.getAttribute("USERID"));
        ArrayList rowview = new ArrayList();
        rowview.add("TIME");
        PbReportQuery timequery = new PbReportQuery();
        ArrayList QueryCols = new ArrayList();
        ArrayList QueryAggs = new ArrayList();
        List<String> measureIdVal = new ArrayList<String>();
        measureIdVal.add(measId);
        DashboardViewerDAO dao = new DashboardViewerDAO();

        List<KPIElement> kpiElements = dao.getKPIElements(measureIdVal, new HashMap<String, String>());
        if (kpiElements != null) {
            for (KPIElement elem : kpiElements) {
                if (elem.getElementName() != null) {
                    QueryCols.add(elem.getElementId());
                }
                QueryAggs.add(elem.getAggregationType());
            }
        }
        StringBuilder kpiheadsbuilder = new StringBuilder();
        if (QueryCols.size() == 4) {

            timequery.setRowViewbyCols(rowview);
            timequery.setColViewbyCols(new ArrayList());
            timequery.setQryColumns(QueryCols);
            timequery.setColAggration(QueryAggs);
            timequery.setTimeDetails(arl1);
            timequery.setDefaultMeasure(String.valueOf(QueryCols.get(0)));
            timequery.setDefaultMeasureSumm(String.valueOf(QueryAggs.get(0)));
            timequery.setBizRoles(roleId);
            timequery.setUserId(userId);
            timequery.isTimeSeries = false;
            timequery.isTimeDrill = false;


            String query = null;
            arl1.set(3, "Month");
            pbretObjForTimeOnly = timequery.getPbReturnObject(String.valueOf(QueryCols.get(0)));
            ArrayList arlist = new ArrayList();
            ArrayList datelist = new ArrayList();
            if (pbretObjForTimeOnly != null && pbretObjForTimeOnly.getRowCount() > 0) {
                if (pbretObjForTimeOnly.getRowCount() > 12) {
                    for (int i = 1; i < pbretObjForTimeOnly.getRowCount(); i++) {
                        double datavalue = Double.parseDouble((pbretObjForTimeOnly.getFieldValueString(i, ("A_" + measId))));
                        String dateVal = pbretObjForTimeOnly.getFieldValueString(i, ("TIME"));
                        int decimalPlaces = 0;
                        BigDecimal curval = new BigDecimal(datavalue);
                        curval = curval.setScale(decimalPlaces, BigDecimal.ROUND_HALF_UP);
                        NumberFormat formatter = NumberFormat.getInstance(new Locale("en_US"));
                        arlist.add(formatter.format(curval));
                        datelist.add(dateVal);
                    }
                } else {
                    for (int i = 0; i < pbretObjForTimeOnly.getRowCount(); i++) {
                        double datavalue = Double.parseDouble((pbretObjForTimeOnly.getFieldValueString(i, ("A_" + measId))));
                        String dateVal = pbretObjForTimeOnly.getFieldValueString(i, ("TIME"));
                        int decimalPlaces = 0;
                        BigDecimal curval = new BigDecimal(datavalue);
                        curval = curval.setScale(decimalPlaces, BigDecimal.ROUND_HALF_UP);
                        NumberFormat formatter = NumberFormat.getInstance(new Locale("en_US"));
                        arlist.add(formatter.format(curval));
                        datelist.add(dateVal);
                    }
                }
            }
            String valu = "";
            String monthNam = "";
            ArrayList arlistforthree = new ArrayList();
            ArrayList datelistforthree = new ArrayList();
            for (int i = 0; i < monthName.length; i++) {
                String fulldt = arl.get(2).toString();
                if (fulldt.contains("/")) {
                    int monthval = fulldt.indexOf("/");
                    valu = fulldt.substring(0, monthval);
                } else {
                    int monthval = fulldt.indexOf("-");
                    valu = fulldt.substring(0, monthval);
                }
                if (!valu.equalsIgnoreCase("") && Integer.parseInt(valu) == i) {
                    monthNam = monthName[i - 1];
                }
            }
            String val14 = "";
            if (datelist.size() > 0 && datelist.size() > 4 && !monthNam.equalsIgnoreCase("") && monthNam.equalsIgnoreCase(datelist.get(datelist.size() - 1).toString().substring(0, 3))) {
                arlistforthree.add(arlist.get(arlist.size() - 2));
                arlistforthree.add(arlist.get(arlist.size() - 3));
                arlistforthree.add(arlist.get(arlist.size() - 4));
                datelistforthree.add(datelist.get(datelist.size() - 2));
                datelistforthree.add(datelist.get(datelist.size() - 3));
                datelistforthree.add(datelist.get(datelist.size() - 4));
            } else if (datelist.size() > 0 && arlist.size() > 3) {
                arlistforthree.add(arlist.get(arlist.size() - 1));
                arlistforthree.add(arlist.get(arlist.size() - 2));
                arlistforthree.add(arlist.get(arlist.size() - 3));
                datelistforthree.add(datelist.get(datelist.size() - 1));
                datelistforthree.add(datelist.get(datelist.size() - 2));
                datelistforthree.add(datelist.get(datelist.size() - 3));
            } else if (datelist.size() > 0) {
                for (int i1 = 0; i1 < arlist.size(); i1++) {
                    arlistforthree.add(arlist.get(i1));
                    datelistforthree.add(datelist.get(i1));
                }
            }
            long avgVal = 0;
            for (int i = 0; i < arlistforthree.size(); i++) {
                avgVal = avgVal + Long.parseLong(arlistforthree.get(i).toString().replaceAll(",", ""));
            }
            if (avgVal != 0 && arlistforthree != null && arlistforthree.size() > 0) {
                avgVal = avgVal / arlistforthree.size();
            }

            Object maxVal = "";
            Object minVal = "";
            String minDateVal = "";
            String maxDateVal = "";

            int maxV = 0;
            int minV = 0;
            if (arlist != null && arlist.size() > 0) {
                long first = Long.parseLong(arlist.get(0).toString().replaceAll(",", ""));
                for (int i0 = 1; i0 < arlist.size(); i0++) {
                    if (Long.parseLong(arlist.get(i0).toString().replaceAll(",", "")) > first) {
                        first = Long.parseLong(arlist.get(i0).toString().replaceAll(",", ""));
                        maxV = i0;
                    }
                }
                long first1 = Long.parseLong(arlist.get(0).toString().replaceAll(",", ""));
                for (int i1 = 1; i1 < arlist.size(); i1++) {
                    if (Long.parseLong(arlist.get(i1).toString().replaceAll(",", "")) < first1) {
                        first1 = Long.parseLong(arlist.get(i1).toString().replaceAll(",", ""));
                        minV = i1;
                    }
                }
                maxVal = arlist.get(maxV);
                minVal = arlist.get(minV);
                for (int i = 0; i < arlist.size(); i++) {
                    if (maxVal == arlist.get(i)) {
                        maxDateVal = datelist.get(i).toString();
                    }
                    if (minVal == arlist.get(i)) {
                        minDateVal = datelist.get(i).toString();
                    }

                }
            }

            String monthfontColor = "";
            String yearfontColor = "";
            String monthIcon = "";
            String yearIcon = "";

            //for 3months data
            kpiheadsbuilder.append("<table style='table-layout:fixed; width:100%; border-spacing: 15px;'><tr>");
            if (arlistforthree.size() > 0 && arlistforthree != null) {
                for (int i = 0; i < arlistforthree.size(); i++) {
                    double lastthreemon = Double.parseDouble(arlistforthree.get(i).toString().replaceAll(",", ""));
                    int decimalPlaces = 0;
                    BigDecimal lastthreemoncurval = new BigDecimal(lastthreemon);
                    lastthreemoncurval = lastthreemoncurval.setScale(decimalPlaces, BigDecimal.ROUND_HALF_UP);
                    String formatVal = "";
                    String lastthreemoncurvalcurvalm = NumberFormatter.getModifiedNumber(lastthreemoncurval, detail.getFormatVal(), Integer.parseInt(detail.getRoundVal()));
                    if (lastthreemoncurvalcurvalm.contains("M")) {
                        lastthreemoncurvalcurvalm = lastthreemoncurvalcurvalm.replace("M", "");
                        formatVal = "Mn";
                    } else if (lastthreemoncurvalcurvalm.contains("L")) {
                        lastthreemoncurvalcurvalm = lastthreemoncurvalcurvalm.replace("L", "");
                        formatVal = "Lkh";
                    } else if (lastthreemoncurvalcurvalm.contains("C")) {
                        lastthreemoncurvalcurvalm = lastthreemoncurvalcurvalm.replace("C", "");
                        formatVal = "Crs";
                    } else if (lastthreemoncurvalcurvalm.contains("K")) {
                        lastthreemoncurvalcurvalm = lastthreemoncurvalcurvalm.replace("K", "");
                        formatVal = "K";
                    }
                    kpiheadsbuilder.append("<td width='140%'><table style='margin-left: 10px;'><tr><td class='myDesignClass'>" + datelistforthree.get(i) + "</td></tr></table><div class='ui-tabs ui-widget ui-widget-content ui-corner-all' style='border-top:blank; border-bottom:medium hidden; border-left:medium hidden; border-right:medium hidden;border-color:LightGrey;width:100% ; height:100% ;margin-left: 10px; margin-right: 10px;'><table><tr><td style='font-size:14pt;color:#369;'>" + lastthreemoncurvalcurvalm + " <sub>" + formatVal + "</sub></td></tr></table></div></td>");
                }
            }
            kpiheadsbuilder.append("</tr></table>");

            //for min value
            if (minVal != null && minVal != "") {
                BigDecimal minBigDecimal = new BigDecimal(Long.parseLong(minVal.toString().replaceAll(",", "")));
                String formatVal = "";
                String minValm = NumberFormatter.getModifiedNumber(minBigDecimal, detail.getFormatVal(), Integer.parseInt(detail.getRoundVal()));
                if (minValm.contains("M")) {
                    minValm = minValm.replace("M", "");
                    formatVal = "Mn";
                } else if (minValm.contains("L")) {
                    minValm = minValm.replace("L", "");
                    formatVal = "Lkh";
                } else if (minValm.contains("C")) {
                    minValm = minValm.replace("C", "");
                    formatVal = "Crs";
                } else if (minValm.contains("K")) {
                    minValm = minValm.replace("K", "");
                    formatVal = "K";
                }
                kpiheadsbuilder.append("<table style='table-layout:fixed; width:100%; border-spacing: 15px;'><tr><td width='140%'><table style='margin-left: 10px;'><tr><td class='myDesignClass' style='white-space:nowrap;'>Min (" + minDateVal + ")</td></tr></table><div class='ui-tabs ui-widget ui-widget-content ui-corner-all' style='border-top:blank; border-bottom:medium hidden; border-left:medium hidden; border-right:medium hidden;border-color:LightGrey;width:100% ; height:100% ;margin-left: 10px; margin-right: 10px;'><table><tr><td style='font-size:14pt;color:#369;'>" + minValm + " <sub>" + formatVal + "</sub></td></tr></table></div></td>");
            } else {
                kpiheadsbuilder.append("<table style='table-layout:fixed; width:100%; border-spacing: 15px;'><tr><td width='140%'><table style='margin-left: 10px;'><tr><td class='myDesignClass'>Min</td></tr></table><div class='ui-tabs ui-widget ui-widget-content ui-corner-all' style='border-top:blank; border-bottom:medium hidden; border-left:medium hidden; border-right:medium hidden;border-color:LightGrey;width:100% ; height:100% ;margin-left: 10px; margin-right: 10px;'><table><tr><td style='font-size:14pt'>--</td></tr></table></div></td>");
            }

            //for max value
            if (maxVal != null && maxVal != "") {
                BigDecimal maxBigDecimal = new BigDecimal(Long.parseLong(maxVal.toString().replaceAll(",", "")));
                String formatVal = "";
                String maxValm = NumberFormatter.getModifiedNumber(maxBigDecimal, detail.getFormatVal(), Integer.parseInt(detail.getRoundVal()));
                if (maxValm.contains("M")) {
                    maxValm = maxValm.replace("M", "");
                    formatVal = "Mn";
                } else if (maxValm.contains("L")) {
                    maxValm = maxValm.replace("L", "");
                    formatVal = "Lkh";
                } else if (maxValm.contains("C")) {
                    maxValm = maxValm.replace("C", "");
                    formatVal = "Crs";
                } else if (maxValm.contains("K")) {
                    maxValm = maxValm.replace("K", "");
                    formatVal = "K";
                }
                kpiheadsbuilder.append("<td width='140%'><table style='margin-left: 10px;'><tr><td class='myDesignClass' style='white-space:nowrap;'>Max (" + maxDateVal + ")</td></tr></table><div class='ui-tabs ui-widget ui-widget-content ui-corner-all' style='border-top:blank; border-bottom:medium hidden; border-left:medium hidden; border-right:medium hidden;border-color:LightGrey;width:100% ; height:100% ;margin-left: 10px; margin-right: 10px;'><table><tr><td style='font-size:14pt;color:#369;'>" + maxValm + " <sub>" + formatVal + "</sub></td></tr></table></div></td>");
            } else {
                kpiheadsbuilder.append("<td width='140%'><table style='margin-left: 10px;'><tr><td class='myDesignClass'>Max</td></tr></table><div class='ui-tabs ui-widget ui-widget-content ui-corner-all' style='border-top:blank; border-bottom:medium hidden; border-left:medium hidden; border-right:medium hidden;border-color:LightGrey;width:100% ; height:100% ;margin-left: 10px; margin-right: 10px;'><table><tr><td style='font-size:14pt'>--</td></tr></table></div></td>");
            }

            //empty td
            //kpiheadsbuilder.append("<td width='140%'><table style='margin-left: 10px;'><tr><td class='myDesignClass'></td></tr></table><div class='ui-tabs ui-widget ui-widget-content ui-corner-all' style='border-top:medium hidden; border-bottom:medium hidden; border-left:medium hidden; border-right:medium hidden;border-color:LightGrey;width:100% ; height:100% ;margin-left: 10px; margin-right: 10px;'><table><tr><td style='font-size:14pt'></td></tr></table></div></td></tr></table>");

            //for avg og last 3 months
            BigDecimal maxBigDecimal = new BigDecimal(avgVal);
            String formatVal = "";
            String avgValm = NumberFormatter.getModifiedNumber(maxBigDecimal, detail.getFormatVal(), Integer.parseInt(detail.getRoundVal()));
            if (avgValm.contains("M")) {
                avgValm = avgValm.replace("M", "");
                formatVal = "Mn";
            } else if (avgValm.contains("L")) {
                avgValm = avgValm.replace("L", "");
                formatVal = "Lkh";
            } else if (avgValm.contains("C")) {
                avgValm = avgValm.replace("C", "");
                formatVal = "Crs";
            } else if (avgValm.contains("K")) {
                avgValm = avgValm.replace("K", "");
                formatVal = "K";
            }
            kpiheadsbuilder.append("<td width='140%'><table style='margin-left: 10px;'><tr><td class='myDesignClass'>Average Value</td></tr></table><div class='ui-tabs ui-widget ui-widget-content ui-corner-all' style='border-top:blank;; border-bottom:medium hidden; border-left:medium hidden; border-right:medium hidden;border-color:LightGrey;width:100% ; height:100% ;margin-left: 10px; margin-right: 10px;'><table><tr><td style='font-size:14pt;color:#369;'>" + avgValm + "<sub>" + formatVal + "</sub></td></tr></table></div></td></tr></table>");
        }

        PrintWriter out = response.getWriter();
        out.print(kpiheadsbuilder.toString());

        return null;
    }

    public ActionForward getTimeDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(false);
        String oneViewId = request.getParameter("oneViewId");
        OneViewBD viewBd = new OneViewBD();
        if (session != null) {
            String fileProperties = session.getAttribute("advHtmlFileProps").toString();
            String fileName = session.getAttribute("tempFileName").toString();
            OnceViewContainer oneContainer = (OnceViewContainer) viewBd.readFileDetails(fileProperties, fileName);
            String date = "";
            String duration = "";
            String compare = "";
            String dateStr = "";
            StringBuffer result = new StringBuffer();
            if (oneContainer != null) {
                List timeDetails = oneContainer.timedetails;
                if (!timeDetails.isEmpty()) {
                    dateStr = timeDetails.get(2).toString();
                    date = dateStr.substring(0, 2).concat("/").concat(dateStr.substring(3, 5)).concat(dateStr.substring(5));
                    duration = timeDetails.get(3).toString();
                    compare = timeDetails.get(4).toString();
                }
                result.append("<table align='right'><tr><td style='display:none;'>").append("<input type='text' id='dateIdValue' name='' value='" + date + "'></input>").append("</td>").append("<td style='display:none;'>").append("<input type='text' id='durationId' name='' value='" + duration + "'></input>").append("</td>").append("<td style='display:none;'>").append("<input type='text' id='comareWithId' name='' value='" + compare + "'></input>").append("</td><td><input type='text' id='oneSetting_EveryTime' value='" + oneContainer.isEveryTimeUpdate() + "'style='display:none;'></input></td><td><input type='text' id='oneSetting_Hidden' name='' value='" + oneContainer.getSettingType() + "'style='display:none;'></input></td></tr></table>");
            }
            response.getWriter().print(result);
        }
        return null;
    }

    public ActionForward buildDialChart(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(false);
        if (session != null) {
            String oneViewId = request.getParameter("oneViewId");
            String regionId = request.getParameter("regionId");
            String dialMapStr = request.getParameter("dialMap");
            boolean isDialChart = Boolean.parseBoolean(request.getParameter(""));
            String targetVal = request.getParameter("targetVal").replace(",", "");
            String dialMeasureBase = request.getParameter("dialMeasureBase");
            String measureType = request.getParameter("measureType");
            String currVal = request.getParameter("currVal");
            boolean day1 = Boolean.parseBoolean(request.getParameter("day1"));
            OneViewBD viewBd = new OneViewBD();
            StringBuilder dialString = new StringBuilder();
            String changePer = "0";

            String advHtmlFileProps = (String) session.getAttribute("advHtmlFileProps");
            String fileName = session.getAttribute("tempFileName").toString();
            OnceViewContainer oneContainer = (OnceViewContainer) viewBd.readFileDetails(advHtmlFileProps, fileName);
            OneViewLetDetails viewlet = oneContainer.onviewLetdetails.get(Integer.parseInt(regionId));

            LinkedHashMap dialMap = new LinkedHashMap();
            dialMapStr = dialMapStr.substring(1, dialMapStr.length() - 1);
            dialMapStr = dialMapStr.replace("\"", "");
            String[] keyList = dialMapStr.split(",");
            ArrayList detailList;
            for (int i = 0; i < keyList.length; i++) {
                detailList = new ArrayList();
                String[] innerList = keyList[i].split(":");
                String[] details = innerList[1].split("~");
                for (int j = 0; j < details.length; j++) {
                    detailList.add(details[j].trim());
                }
                dialMap.put(innerList[0].trim(), detailList);
            }
            viewlet.setDialChart(true);
            viewlet.setDialMap(dialMap);
            viewlet.setDialMeasureBase(dialMeasureBase);
            if (measureType != null) {
                viewlet.setMeasType(measureType);
            }
            int days = 1;
//            if(day1){
//            days=viewBd.getOneViewDays(oneContainer);
//            }
            if (targetVal != null) {
                viewlet.setTargetValPerDay(Double.parseDouble(targetVal));
            }
//             dialString.append("<div id='chart-"+regionId+"' style='height:"+viewlet.getHeight()+"px;width:"+viewlet.getWidth()+"px;' onclick='zoomMeasureDialChart(\""+changePer+"\",\""+regionId+"\",\""+viewlet.getRepName()+"\")' ></div>");
            dialString.append(viewBd.getMeasureMeterGraph(Double.parseDouble(changePer), viewlet.getHeight(), viewlet.getWidth(), dialMap, regionId, viewlet, false, days, Double.parseDouble(currVal)));
            viewBd.writeFileDetails(advHtmlFileProps, fileName, oneContainer);
            response.getWriter().print(dialString.toString());
        }
        return null;
    }

    public ActionForward zoomDialChart(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(false);
        if (session != null) {
            String oneViewId = request.getParameter("oneViewId");
            String regionId = request.getParameter("regionId");
            int height = Integer.parseInt(request.getParameter("height"));
            int width = Integer.parseInt(request.getParameter("width"));
            String changePer = request.getParameter("changePer");
            String currVal = request.getParameter("currVal");
            OneViewBD viewBd = new OneViewBD();
            String advHtmlFileProps = (String) session.getAttribute("advHtmlFileProps");
            String fileName = session.getAttribute("tempFileName").toString();
            OnceViewContainer oneContainer = (OnceViewContainer) viewBd.readFileDetails(advHtmlFileProps, fileName);
            OneViewLetDetails viewlet = oneContainer.onviewLetdetails.get(Integer.parseInt(regionId));
            LinkedHashMap dialMap = viewlet.getDialMap();
            StringBuilder dialString = new StringBuilder();
            int days = viewBd.getOneViewDays(oneContainer);

            dialString.append(viewBd.getMeasureMeterGraph(Double.parseDouble(changePer), height, width, dialMap, "zoom_" + regionId, viewlet, true, days, Double.parseDouble(currVal)));
            response.getWriter().print(dialString.toString());
        }
        return null;
    }

    public ActionForward buildExixtentMeasure(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(false);
        if (session != null) {
            String oneViewId = request.getParameter("oneViewId");
            String regionId = request.getParameter("regionId");
            boolean isDialChart = Boolean.parseBoolean(request.getParameter("isDialChart"));
            OneViewBD viewBd = new OneViewBD();
            try {
                String advHtmlFileProps = (String) session.getAttribute("advHtmlFileProps");
                String fileName = session.getAttribute("tempFileName").toString();
                OnceViewContainer oneContainer = (OnceViewContainer) viewBd.readFileDetails(advHtmlFileProps, fileName);
                OneViewLetDetails viewlet = oneContainer.onviewLetdetails.get(Integer.parseInt(regionId));
                List<String> timeDetails = oneContainer.timedetails;
                request.setAttribute("OneviewTiemDetails", timeDetails);
                viewlet.setDialChart(isDialChart);
                viewBd.form = form;
                viewBd.mapping = mapping;
                request.setAttribute("OneviewTiemDetails", oneContainer.timedetails);
                String result = viewBd.buildRegionData(request, response, oneContainer, viewlet);
                viewBd.writeFileDetails(advHtmlFileProps, fileName, oneContainer);
                response.getWriter().print(result);
            } catch (Exception e) {
                logger.error("Exception:", e);
            }

        }
        return null;
    }
//     public ActionForward setTableListToContainer(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
//       String repId = request.getParameter("repId");
//       String tabLst = request.getParameter("tabLst");
//       String[] tabListArray = null;
//       ArrayList alsit = new ArrayList();
//       Container container = null;
//       if(tabLst != null && !tabLst.equalsIgnoreCase("")){
//          tabListArray =  tabLst.split(",");
//          for(int i=0;i<tabListArray.length;i++){
//              alsit.add(tabListArray[i]);
//          }
//       }
//       container = Container.getContainerFromSession(request, repId);
//       container.setTableList(alsit);
//       return null;
//   }

    public ActionForward renameOneviewBys(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession(false);
        String advHtmlFileProps = (String) request.getSession(false).getAttribute("advHtmlFileProps");
        HashMap map = null;
        OnceViewContainer onecontainer = new OnceViewContainer();
        String gaphName = request.getParameter("rename");
        String divId = request.getParameter("colNo");
        String oneViewIdValue = request.getParameter("oneViewIdValue");
        if (session != null) {

            map = (HashMap) session.getAttribute("ONEVIEWDETAILS");
            if (map != null) {
                onecontainer = (OnceViewContainer) map.get(oneViewIdValue);

                OneViewLetDetails detail = new OneViewLetDetails();
                detail = onecontainer.onviewLetdetails.get(Integer.parseInt(request.getParameter("colNo")));
                detail.setRepName(gaphName);
//                ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();
//                String fileName = reportTemplateDAO.getOneviewFileName(oneViewIdValue);
//                FileOutputStream fos = new FileOutputStream(advHtmlFileProps+"/"+fileName);
//                ObjectOutputStream oos = new ObjectOutputStream(fos);
//                oos.writeObject(onecontainer);
//                oos.flush();
//                oos.close();


            } else {
                OneViewLetDetails detail = null;

                ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();
//                onecontainer = reportTemplateDAO.getOneViewData(oneViewIdValue);
                // String fileName = reportTemplateDAO.getOneviewFileName(oneViewIdValue);
                String fileName = session.getAttribute("tempFileName").toString();
                FileInputStream fis2 = new FileInputStream(advHtmlFileProps + "/" + fileName);
                ObjectInputStream ois2 = new ObjectInputStream(fis2);
                onecontainer = (OnceViewContainer) ois2.readObject();
                ois2.close();
                detail = onecontainer.onviewLetdetails.get(Integer.parseInt(request.getParameter("colNo")));
                detail.setRepName(gaphName);
//                reportTemplateDAO.updateOneviewData(onecontainer, oneViewIdValue);
                StringBuilder finalStringVal = new StringBuilder();
                DashboardTemplateBD dashboardTemplateBD = new DashboardTemplateBD();
                OneViewBD oneViewBD = new OneViewBD();
                if (detail.getPbReturnObject() == null) {
                    detail.setMeasurOprFlag(false);
                } else {
                    detail.setMeasurOprFlag(true);
                }
                /*
                 * String result = ""; if
                 * (detail.getReptype().toString().equalsIgnoreCase("measures"))
                 * { result = dashboardTemplateBD.getMeasureDetailsData(request,
                 * response, session,onecontainer, detail); }else if
                 * (detail.getReptype().toString().equalsIgnoreCase("repKpis"))
                 * { PbDashboardViewerAction genDashForOneview = new
                 * PbDashboardViewerAction(); request.setAttribute("REPORTID",
                 * detail.getRepId()); request.setAttribute("OneviewTest",
                 * true);
                 * request.setAttribute("dashletId",detail.getKpiDashLetNo());
                 * genDashForOneview.viewDashboard(mapping, form, request,
                 * response); result =
                 * dashboardTemplateBD.getOneviewKpisData(request, response,
                 * session, detail); }else if
                 * (detail.getReptype().toString().equalsIgnoreCase("repTable"))
                 * { result = dashboardTemplateBD.getTableDetailsData(request,
                 * response, session, detail,onecontainer.timedetails); }else
                 * if(detail.getReptype().toString().equalsIgnoreCase("repGraph")){
                 * result = dashboardTemplateBD.getGraphDetailsData(request,
                 * response, session, detail,onecontainer.timedetails); }else if
                 * (detail.getReptype().toString().equalsIgnoreCase("headLine"))
                 * { result =
                 * dashboardTemplateBD.getHeadLinehDetailsData(request,
                 * response, session, detail); }else if
                 * (detail.getReptype().toString().equalsIgnoreCase("notes")) {
                 * result =
                 * oneViewBD.getOneViewNotesData(onecontainer,oneViewIdValue,detail,true);
                 * }if
                 * (detail.getReptype().toString().equalsIgnoreCase("complexkpi"))
                 * { result = dashboardTemplateBD.getComplexKpisData(request,
                 * response, session,
                 * detail,oneViewIdValue,Integer.parseInt(request.getParameter("colNo")));
                 * }
                 * oneViewBD.writeOneviewRegData(onecontainer,result,detail.getNoOfViewLets(),request);
                 */




                FileOutputStream fos = new FileOutputStream(advHtmlFileProps + "/" + fileName);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(onecontainer);
                oos.flush();
                oos.close();
            }


        }
        return null;
    }

    public ActionForward commentsData(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession(false);
        String advHtmlFileProps = (String) request.getSession(false).getAttribute("advHtmlFileProps");
        HashMap map = null;
        OnceViewContainer onecontainer = new OnceViewContainer();
        String commentData = request.getParameter("commentData");
        String divId = request.getParameter("colNo");
        String oneViewIdValue = request.getParameter("oneViewIdValue");
        String date = null;
//        String date = request.getParameter("date");
        Calendar currentDate = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        date = formatter.format(currentDate.getTime());
        if (session != null) {

            map = (HashMap) session.getAttribute("ONEVIEWDETAILS");
            KPIComment measureComments = new KPIComment();
            if (map != null) {
                onecontainer = (OnceViewContainer) map.get(oneViewIdValue);
                ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();

                OneViewLetDetails detail = new OneViewLetDetails();
                detail = onecontainer.onviewLetdetails.get(Integer.parseInt(request.getParameter("colNo")));

                measureComments.setElementId(request.getParameter("colNo"));
                measureComments.setUserId(session.getAttribute("USERID").toString());
                measureComments.setUserName(reportTemplateDAO.getUserName(session.getAttribute("USERID").toString()));
                measureComments.setCommentDate(formatter.parse(date));
                measureComments.setComment(commentData);
                detail.addMeasureComments(measureComments);

            } else {
                OneViewLetDetails detail = new OneViewLetDetails();

                ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();
//                onecontainer = reportTemplateDAO.getOneViewData(oneViewIdValue);
                // String fileName = reportTemplateDAO.getOneviewFileName(oneViewIdValue);
                String fileName = request.getSession(false).getAttribute("tempFileName").toString();
                FileInputStream fis2 = new FileInputStream(advHtmlFileProps + "/" + fileName);
                ObjectInputStream ois2 = new ObjectInputStream(fis2);
                onecontainer = (OnceViewContainer) ois2.readObject();
                ois2.close();

                detail = onecontainer.onviewLetdetails.get(Integer.parseInt(request.getParameter("colNo")));
                measureComments.setElementId(request.getParameter("colNo"));
                measureComments.setUserId(session.getAttribute("USERID").toString());
                measureComments.setUserName(reportTemplateDAO.getUserName(session.getAttribute("USERID").toString()));
                measureComments.setCommentDate(formatter.parse(date));
                measureComments.setComment(commentData);
                detail.addMeasureComments(measureComments);
//                reportTemplateDAO.updateOneviewData(onecontainer, oneViewIdValue);
                FileOutputStream fos = new FileOutputStream(advHtmlFileProps + "/" + fileName);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(onecontainer);
                oos.flush();
                oos.close();
            }


        }
        return null;
    }
    //Surender

    public ActionForward getCommentData(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession(false);
        OnceViewContainer onecontainer = new OnceViewContainer();
        int divId = Integer.parseInt(request.getParameter("colNo"));
        String oneViewIdValue = request.getParameter("oneViewIdValue");
        String commeData = request.getParameter("commentData");
        String datevale = request.getParameter("date");
        String advHtmlFileProps = (String) request.getSession(false).getAttribute("advHtmlFileProps");
        StringBuilder strngbulder = new StringBuilder();
        strngbulder.append("{ MeasurComments:[");
        HashMap map = null;
        map = (HashMap) session.getAttribute("ONEVIEWDETAILS");
        if (map != null) {
            onecontainer = (OnceViewContainer) map.get(oneViewIdValue);

            OneViewLetDetails detail = new OneViewLetDetails();
            detail = onecontainer.onviewLetdetails.get(Integer.parseInt(request.getParameter("colNo")));
            if (detail.getCommentDate() != null) {
                strngbulder.append("\"").append(detail.getCommentDate()).append("\"").append(",").append("\"").append(detail.getCommentData()).append("\"").append(",").append("\"").append(detail.getUserName()).append("\"").append("]}");
                response.getWriter().print(strngbulder.toString());
            } else {
                strngbulder.append("\"").append("No Comments").append("\"").append("]}");
                response.getWriter().print(strngbulder.toString());
            }
        } else {
            ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();
//            onecontainer = reportTemplateDAO.getOneViewData(oneViewIdValue);

            // String fileName = reportTemplateDAO.getOneviewFileName(oneViewIdValue);
            String fileName = session.getAttribute("tempFileName").toString();
            FileInputStream fis2 = new FileInputStream(advHtmlFileProps + "/" + fileName);
            ObjectInputStream ois2 = new ObjectInputStream(fis2);
            onecontainer = (OnceViewContainer) ois2.readObject();
            ois2.close();

            String date = null;
            String commentData = null;
            String userName = null;

            List<OneViewLetDetails> dashletDetails = onecontainer.onviewLetdetails;

            date = dashletDetails.get(divId).getCommentDate();
            if (date != null) {
                commentData = dashletDetails.get(divId).getCommentData();
                userName = dashletDetails.get(divId).getUserName();

                strngbulder.append("\"").append(date).append("\"").append(",").append("\"").append(commentData).append("\"").append(",").append("\"").append(userName).append("\"").append("]}");
                response.getWriter().print(strngbulder.toString());
            } else {
                strngbulder.append("\"").append("No Comments").append("\"").append("]}");
                response.getWriter().print(strngbulder.toString());
            }
        }

        return null;
    }
    //Surender

    public ActionForward measureOptionVale(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession(false);
        String advHtmlFileProps = (String) request.getSession(false).getAttribute("advHtmlFileProps");
        HashMap map = null;
        OnceViewContainer onecontainer = new OnceViewContainer();
        String divId = request.getParameter("colNo");
        String oneViewIdValue = request.getParameter("oneViewIdValue");
        String roundVal = request.getParameter("roundVal");
        String numbrFrmt = request.getParameter("numbrFrmt");
        String measType = request.getParameter("measType");
        String currentVal = request.getParameter("currentVal");
        String measrueId = request.getParameter("measrueId");
        String prefixSelect = request.getParameter("prefixSelect");
        String suffixSelect = request.getParameter("suffixSelect");
        String customizePrefix = request.getParameter("customizePrefix");
        String customizeSuffix = request.getParameter("customizeSuffix");
        String measurName = request.getParameter("measrureName");
        String proiorValue = request.getParameter("proiorValue");
        String trendColor = request.getParameter("trendColor");
        //String fontColor=request.getParameter("fontColor");
        String measureColor = request.getParameter("measureColor");
        String islogicalColorApplied = request.getParameter("islogicalColorApplied");
        String colorMap = request.getParameter("colorMap");
        BigDecimal curVal = new BigDecimal(Double.parseDouble(currentVal.replace(",", "")));
        String priorVa = null;
        String value = NumberFormatter.getModifiedNumber(curVal, numbrFrmt, Integer.parseInt(roundVal));
        if (proiorValue != null && !proiorValue.equalsIgnoreCase("") && !proiorValue.equalsIgnoreCase("undefined")) {
            BigDecimal priorVal = new BigDecimal(Double.parseDouble(proiorValue.replace(",", "")));
            priorVa = NumberFormatter.getModifiedNumber(priorVal, numbrFrmt, Integer.parseInt(roundVal));
        }

        HashMap rangeColorMap = new HashMap();
        if (islogicalColorApplied != null && Boolean.parseBoolean(islogicalColorApplied)) {
            colorMap = colorMap.substring(1, colorMap.length() - 1);
            colorMap = colorMap.replace("\"", "");
            String[] colorsList = colorMap.split(",");
            ArrayList detailList;
            for (int i = 0; i < colorsList.length; i++) {
                detailList = new ArrayList();
                String[] innerList = colorsList[i].split(":");
                String[] details = innerList[1].split("~");
                for (int j = 0; j < details.length; j++) {
                    detailList.add(details[j].trim());
                }
                rangeColorMap.put(innerList[0].trim(), detailList);
            }
        }


        StringBuilder strbuldr = new StringBuilder();
        strbuldr.append("{MeasureCurrPriorValue :[").append("\"").append(value).append("\"").append(",").append("\"").append(priorVa).append("\"").append("]}");
        if (session != null) {

            map = (HashMap) session.getAttribute("ONEVIEWDETAILS");
            if (map != null) {
                onecontainer = (OnceViewContainer) map.get(oneViewIdValue);

                OneViewLetDetails detail = new OneViewLetDetails();
                detail = onecontainer.onviewLetdetails.get(Integer.parseInt(divId));
                detail.setRoundVal(roundVal);
                detail.setFormatVal(numbrFrmt);
                detail.setMeasType(measType);
                detail.setCurrValue(value);
                detail.setPriorValue(priorVa);
                detail.setTrendColor(trendColor);
                detail.setMeasureColor(measureColor);
                detail.setPrefixValue(prefixSelect);
                detail.setSuffixValue(suffixSelect);
                detail.setCustomizePrefix(customizePrefix);
                detail.setCustomizeSuffix(customizeSuffix);
//                ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();
//                String fileName = reportTemplateDAO.getOneviewFileName(oneViewIdValue);
//                FileOutputStream fos = new FileOutputStream(advHtmlFileProps+"/"+fileName);
//                ObjectOutputStream oos = new ObjectOutputStream(fos);
//                oos.writeObject(onecontainer);
//                oos.flush();
//                oos.close();
            } else {
                OneViewLetDetails detail = null;

                ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();
                // String fileName = reportTemplateDAO.getOneviewFileName(oneViewIdValue);
                String fileName = request.getSession(false).getAttribute("tempFileName").toString();
                FileInputStream fis2 = new FileInputStream(advHtmlFileProps + "/" + fileName);
                ObjectInputStream ois2 = new ObjectInputStream(fis2);
                onecontainer = (OnceViewContainer) ois2.readObject();
                ois2.close();
//                onecontainer = reportTemplateDAO.getOneViewData(oneViewIdValue);
                detail = onecontainer.onviewLetdetails.get(Integer.parseInt(divId));
                detail.setRoundVal(roundVal);
                detail.setFormatVal(numbrFrmt);
                detail.setMeasType(measType);
                detail.setCurrValue(value);
                detail.setPriorValue(priorVa);
                detail.setTrendColor(trendColor);
                //detail.setFontColor(fontColor);
                detail.setMeasureColor(measureColor);
                detail.setPrefixValue(prefixSelect);
                detail.setSuffixValue(suffixSelect);
                detail.setCustomizePrefix(customizePrefix);
                detail.setCustomizeSuffix(customizeSuffix);
                if (islogicalColorApplied != null) {
                    detail.setLogicalColor(Boolean.parseBoolean(islogicalColorApplied));
                    detail.setRangeColorMap(rangeColorMap);
                }

//                reportTemplateDAO.updateOneviewData(onecontainer, oneViewIdValue);
                DashboardTemplateBD dashboardTemplateBD = new DashboardTemplateBD();
                OneViewBD oneViewBD = new OneViewBD();
                if (detail.getPbReturnObject() == null) {
                    detail.setMeasurOprFlag(false);
                } else {
                    detail.setMeasurOprFlag(true);
                }
                /*
                 * String result = ""; if
                 * (detail.getReptype().toString().equalsIgnoreCase("measures"))
                 * { result = dashboardTemplateBD.getMeasureDetailsData(request,
                 * response, session,onecontainer, detail); } if
                 * (detail.getReptype().toString().equalsIgnoreCase("complexkpi"))
                 * { result = dashboardTemplateBD.getComplexKpisData(request,
                 * response, session,
                 * detail,oneViewIdValue,Integer.parseInt(request.getParameter("colNo")));
                 * }
                 * oneViewBD.writeOneviewRegData(onecontainer,result,detail.getNoOfViewLets(),request);
                 */

                FileOutputStream fos = new FileOutputStream(advHtmlFileProps + "/" + fileName);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(onecontainer);
                oos.flush();
                oos.close();
            }

        }
        response.getWriter().print(strbuldr.toString());
        return null;
    }

    public ActionForward getMeasureOptionData(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession(false);
        String advHtmlFileProps = (String) request.getSession(false).getAttribute("advHtmlFileProps");
        HashMap map = null;
        OnceViewContainer onecontainer = new OnceViewContainer();
        String divId = request.getParameter("colNo");
        String oneViewIdValue = request.getParameter("oneViewIdValue");
        String roundVal = request.getParameter("roundVal");
        String numbrFrmt = request.getParameter("numbrFrmt");
        String measType = request.getParameter("measType");
        String currentVal = request.getParameter("currentVal");
        StringBuilder strngbulder = new StringBuilder();
//        strngbulder.append("{ MeasureOptions:[");
        JSONObject jsonMap = new JSONObject();
        ArrayList detailList = new ArrayList();
        if (session != null) {

            map = (HashMap) session.getAttribute("ONEVIEWDETAILS");
            if (map != null) {
                onecontainer = (OnceViewContainer) map.get(oneViewIdValue);

                OneViewLetDetails detail = new OneViewLetDetails();
                detail = onecontainer.onviewLetdetails.get(Integer.parseInt(divId));
                if (detail.getRoundVal() != null) {
                    detailList.add(detail.getRoundVal());
                    detailList.add(detail.getFormatVal());
                    detailList.add(detail.getMeasType());
                    detailList.add(detail.getTrendColor());
                    detailList.add(detail.getCustomizePrefix());
                    detailList.add(detail.getPrefixValue());
                    detailList.add(detail.getCustomizeSuffix());
                    detailList.add(detail.getSuffixValue());
                    /*
                     * strngbulder.append("\"").append(detail.getRoundVal()).append("\"").append(",");
                     * strngbulder.append("\"").append(detail.getFormatVal()).append("\"").append(",");
                     * strngbulder.append("\"").append(detail.getMeasType()).append("\"").append(",");
                     * strngbulder.append("\"").append(detail.getTrendColor()).append("\"").append(",");
                     * strngbulder.append("\"").append(detail.getCustomizePrefix()).append("\"").append(",");
                     * strngbulder.append("\"").append(detail.getPrefixValue()).append("\"").append(",");
                     * strngbulder.append("\"").append(detail.getCustomizeSuffix()).append("\"").append(",");
                        strngbulder.append("\"").append(detail.getSuffixValue()).append("\"").append("]}");
                     */

                } else {
                    detailList.add("No Values");
                    /*
                     * strngbulder.append("\"").append("No
                     * Values").append("\"").append("]}");
                    response.getWriter().print(strngbulder.toString());
                     */
                }
                jsonMap.put("MeasureOptions", detailList);
                response.getWriter().print(jsonMap);
            } else {
                OneViewLetDetails detail = null;

                ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();
//                onecontainer = reportTemplateDAO.getOneViewData(oneViewIdValue);

                // String fileName = reportTemplateDAO.getOneviewFileName(oneViewIdValue);
                String fileName = session.getAttribute("tempFileName").toString();
                FileInputStream fis2 = new FileInputStream(advHtmlFileProps + "/" + fileName);
                ObjectInputStream ois2 = new ObjectInputStream(fis2);
                onecontainer = (OnceViewContainer) ois2.readObject();
                ois2.close();

                OneViewBD viewBd = new OneViewBD();
                int days = viewBd.getOneViewDays(onecontainer);


                detail = onecontainer.onviewLetdetails.get(Integer.parseInt(divId));
                if (detail.getRoundVal() != null) {
                    detailList.add(detail.getRoundVal());
                    detailList.add(detail.getFormatVal());
                    detailList.add(detail.getMeasType());
                    detailList.add(detail.getTrendColor());
                    detailList.add(detail.getCustomizePrefix());
                    detailList.add(detail.getPrefixValue());
                    detailList.add(detail.getCustomizeSuffix());
                    detailList.add(detail.getSuffixValue());
                    detailList.add(days);
                    detailList.add(detail.isLogicalColor());
                    detailList.add(detail.isDialChart());
                    if (detail.isDialChart()) {
                        detailList.add(detail.getDialMeasureBase());
                        detailList.add(detail.getTargetValPerDay());
                    }
                    jsonMap.put("MeasureOptions", detailList);

                    /*
                     * strngbulder.append("\"").append(detail.getRoundVal()).append("\"").append(",");
                     * strngbulder.append("\"").append(detail.getFormatVal()).append("\"").append(",");
                     * strngbulder.append("\"").append(detail.getMeasType()).append("\"").append(",");
                     * strngbulder.append("\"").append(detail.getTrendColor()).append("\"").append(",");
                     * strngbulder.append("\"").append(detail.getCustomizePrefix()).append("\"").append(",");
                     * strngbulder.append("\"").append(detail.getPrefixValue()).append("\"").append(",");
                     * strngbulder.append("\"").append(detail.getCustomizeSuffix()).append("\"").append(",");
                     * strngbulder.append("\"").append(detail.getSuffixValue()).append("\"").append(",");
                     * strngbulder.append("\"").append(days).append("\"").append(",");
                        strngbulder.append("\"").append(detail.isLogicalColor()).append("\"").append("]}");
                     */
                    if (detail.isLogicalColor()) {
                        jsonMap.putAll(detail.getRangeColorMap());
                    }
                    if (detail.isDialChart()) {
                        HashMap dialMap = detail.getDialMap();
                        jsonMap.putAll(dialMap);
                    }

//                    response.getWriter().print(strngbulder.toString());
                } else {
                    detailList.add("No Values");
//                    strngbulder.append("\"").append("No Values").append("\"").append("]}");
//                    response.getWriter().print(strngbulder.toString());
                }
                //
                response.getWriter().print(jsonMap.toString());
            }
        }
        return null;
    }

    public ActionForward deleteOneview(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String oneviewId = request.getParameter("oneviewId");
        String userType = request.getParameter("userType");
        String userId = request.getParameter("userId");
        DashboardTemplateDAO dao = new DashboardTemplateDAO();
        dao.deleteOneview(oneviewId, userType, userId, request);
        return null;

    }
//Surender

    public ActionForward GetDashBoardNames(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String folderId = request.getParameter("foldersIds");
        String graphId = request.getParameter("graphid");
        String graphName = request.getParameter("newGraphName");
        boolean fromOneview = Boolean.parseBoolean(request.getParameter("fromOneview"));
        StringBuilder sb = new StringBuilder();
        DashboardTemplateDAO dao = new DashboardTemplateDAO();
        String[] columnNames = null;
        PbReturnObject retObj = dao.getGroupKpiDrillDashBoards(null, folderId);
        columnNames = retObj.getColumnNames();
        sb.append("<tr>");
        sb.append("<td class='myHead'>");
        if (fromOneview) {
            sb.append("Dashboard");
        } else {
            sb.append(graphName);
        }
        sb.append("</td>");
        sb.append("<td>");
        sb.append("<select id='selectReportgr" + graphId.replace(",", "_") + "' width='45%' name='selectReportforGraph'> ");
        for (int i = 0; i < retObj.getRowCount(); i++) {
            sb.append("<option value='" + retObj.getFieldValueString(i, columnNames[0]) + "'>");
            sb.append(retObj.getFieldValueString(i, columnNames[1]));
            sb.append("</option>");
        }
        sb.append("</select>");
        sb.append("</td>");
        sb.append("</tr>");
        sb.append("~" + graphId.replace(",", "_"));
        PrintWriter out = response.getWriter();
        out.print(sb);
        return null;

    }
//Surender

    public ActionForward getMeasureGraph(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException, ClassNotFoundException {
        HttpSession session = request.getSession(false);
        String measId = request.getParameter("measId");
        String measName = request.getParameter("measName");
        String oneViewId = request.getParameter("oneViewId");
        String roleId = request.getParameter("roleId");
        String prevYearComp = request.getParameter("prevYearComp");
        StringBuilder sb = new StringBuilder();
        DashboardViewerDAO dao = new DashboardViewerDAO();
        PbReturnObject pbretObjForTime = new PbReturnObject();
        String imgdata = "";
        String advHtmlFileProps = (String) request.getSession(false).getAttribute("advHtmlFileProps");

        ArrayList arl2 = new ArrayList();
        ArrayList rowview = new ArrayList();
        rowview.add("TIME");
        PbReportQuery timequery = new PbReportQuery();
        ArrayList QueryCols = new ArrayList();
        ArrayList QueryAggs = new ArrayList();
        List<String> measureIdVal = new ArrayList<String>();
        measureIdVal.add(measId);


        String userId = String.valueOf(session.getAttribute("USERID"));
        List<KPIElement> kpiElements = dao.getKPIElements(measureIdVal, new HashMap<String, String>());
        if (kpiElements != null) {
            for (KPIElement elem : kpiElements) {
                if (elem.getElementName() != null) {
                    QueryCols.add(elem.getElementId());
                }
                QueryAggs.add(elem.getAggregationType());
            }
        }

//            timequery.setQryColumns(QueryCols);
//            timequery.setColAggration(QueryAggs);
//            timequery.setParamValue(dashboardcollect.reportParametersValues);
//            timequery.setParamValue(dashboardcollect.reportParametersValues);
        OnceViewContainer onecontainer1 = null;
        String fileName = null;
        PbReturnObject retObj = null;
        ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();
        retObj = reportTemplateDAO.testForExisting();
        ArrayList<String> va = new ArrayList<String>();
        for (int i = 0; i < retObj.getRowCount(); i++) {
            va.add(retObj.getFieldValueString(i, "ONEVIEWID"));
        }
        if (!va.isEmpty() && va.contains(oneViewId)) {
            retObj = reportTemplateDAO.getOneviewFileNam(oneViewId);
            fileName = request.getSession(false).getAttribute("tempFileName").toString();
            FileInputStream fis = new FileInputStream(advHtmlFileProps + "/" + fileName);
            ObjectInputStream ois = new ObjectInputStream(fis);
            onecontainer1 = (OnceViewContainer) ois.readObject();
            ois.close();
        }
//        onecontainer1 = reportTemplateDAO.getOneViewData(oneViewId);
        ArrayList arl = new ArrayList();

        if (onecontainer1 != null && !onecontainer1.timedetails.isEmpty()) {
            arl = (ArrayList) onecontainer1.timedetails;
            arl2.addAll(arl);
        } else {
            arl.add("Day");
            arl.add("PRG_STD");
            ProgenParam pramnam = new ProgenParam();
            String date = pramnam.getdateforpage();
            arl.add(date);
            arl.add("Month");
            arl.add("Last Period");
            arl2.addAll(arl);
        }

        if (arl2 != null && !arl2.isEmpty() && prevYearComp != null && !prevYearComp.isEmpty() && prevYearComp.equalsIgnoreCase("true")) {
            arl2.set(4, "Last Year");
            //
            if (QueryCols.size() > 1) {
                measureIdVal.add(String.valueOf(QueryCols.get(1)));
                //
            }
        }

//                timequery.setRowViewbyCols(new ArrayList());
//                repQuery.setParamValue(collect.reportParametersValues);
        timequery.setRowViewbyCols(rowview);
        timequery.setColViewbyCols(new ArrayList());
        timequery.setColViewbyCols(new ArrayList());
        timequery.setQryColumns(QueryCols);
        timequery.setColAggration(QueryAggs);
        timequery.setTimeDetails(arl2);
        timequery.setDefaultMeasure(String.valueOf(QueryCols.get(0)));
        timequery.setDefaultMeasureSumm(String.valueOf(QueryAggs.get(0)));
        timequery.isTimeSeries = false;
//                repQuery.setReportId(collect.reportId);
        timequery.setBizRoles(roleId);
        timequery.setUserId(userId);
        pbretObjForTime = timequery.getPbReturnObject(String.valueOf(QueryCols.get(0)));
        StringBuilder imgtest = new StringBuilder();
        ProgenJQPlotGraph jqGraph = new ProgenJQPlotGraph();
        jqGraph.trendType = "montFormat";
        jqGraph.setChartId(oneViewId);
        jqGraph.setChartId(oneViewId);
        jqGraph.setGraphType("Line");//
        imgtest.append("<table  align='left' ><tr><td><a href='javascript:void(0)' onclick='measureSelection(" + roleId + "," + oneViewId + ")'>Select Parameters</a></td><td margin-left='100px'><input id='DrillTrend' type='radio' name='DrillTrend' onclick='DrillTrend()' checked>Drill&nbsp;&nbsp;<input id='overLayTrend' name='DrillTrend' onclick='DrillTrend()'  type='radio'>Overlay&nbsp;&nbsp;<input id='ChangeTrend' name='DrillTrend' onclick='DrillTrend()'  type='radio'>Change&nbsp;&nbsp;<input id='ChangePrcntTrend' name='DrillTrend' onclick='DrillTrend()'  type='radio'>Change%</td>");
        if (prevYearComp != null && !prevYearComp.isEmpty() && prevYearComp.equalsIgnoreCase("true")) {
            imgtest.append("<td width='100px'></td><td>Prev. Year Comparaision: <select id='prevYearComp' ><option value='Yes' onclick='prevYearComparisionYes()'>Yes</optiong><option value='No' onclick='prevYearComparisionNo()'>No</optiong></select></td>");
        } else {
            imgtest.append("<td width='100px'></td><td>Prev. Year Comparaision: <select id='prevYearComp' ><option value='No' onclick='prevYearComparisionNo()'>No</optiong><option value='Yes' onclick='prevYearComparisionYes()'>Yes</optiong></select></td>");
        }
        imgtest.append("</tr></table>");
        imgtest.append("<div id='chart-" + oneViewId + "' style='width:600px; height:400px;top:25px;'></div>");

        String datavaluesstr = "";
        String keyvaluesstr = "";
        String barchartcolumntitlesstr = "";
        String viewbycolumnsstr = "";

        imgtest.append("<script>$(\".jqplot-highlighter-tooltip, .jqplot-canvasOverlay-tooltip \").css('font-size', '1em');");
        imgtest.append(jqGraph.getTrendGraph(pbretObjForTime, (ArrayList) measureIdVal, rowview, prevYearComp, measId, roleId));
//          imgtest.append("$(\"#chart-"+oneViewId+"\").bind(\"jqplotDataClick\", function(ev, seriesIndex, pointIndex, plot) {");
//          imgtest.append("alert(plot.data[seriesIndex][pointIndex]);");
//          imgtest.append("relatedMeasuresForDrill('" + measId + "','" + oneViewId + "','" + roleId + "');");
//          imgtest.append("});");
        imgtest.append("</script>");
//        String datavaluesstr = "";
//        String keyvaluesstr = "";
//        String barchartcolumntitlesstr = "";
//        String viewbycolumnsstr = "";
//
//
//
//        if ((pbretObjForTime != null && pbretObjForTime.rowCount > 0)) {
//            ProgenChartDatasets graph = new ProgenChartDatasets();
//            String[] barchartcolumnnames = new String[2];
//            String[] barchartcolumntitles = new String[2];
//            String[] viewbycolumns = new String[1];
//            String[] viewbycolumnTies = new String[1];
//
//            barchartcolumnnames[0] = "Time";
//            barchartcolumnnames[1] = "A_" + measId;
//            barchartcolumntitles[0] = "Time";
//            barchartcolumntitles[1] = measName;
//            viewbycolumns[0] = "TIME";
//            viewbycolumnTies[0] = "Time";
//
//            for (int index = 0; index < pbretObjForTime.getRowCount(); index++) {
//                barchartcolumntitlesstr = barchartcolumntitlesstr + measName + ",";
//                datavaluesstr = datavaluesstr + pbretObjForTime.getFieldValueString(index, "A_" + measId) + ",";
//                String str = pbretObjForTime.getFieldValueString(index, viewbycolumns[0]);
//                str = str.replaceAll("-", "/");
//                keyvaluesstr = keyvaluesstr + str + ",";
//                viewbycolumnsstr = viewbycolumnsstr + "Time" + ",";
//            }
//
//            String[] viewbycolumnss = viewbycolumnsstr.split(",");
//            String[] barchartcolumntitless = barchartcolumntitlesstr.split(",");
//            String[] keyvaluess = keyvaluesstr.split(",");
//            String[] datavalues = datavaluesstr.split(",");
//
////            graph.setBarChartColumnNames(barchartcolumnnames);
////            graph.setViewByColumns(viewbycolumns);
////            graph.setBarChartColumnTitles(barchartcolumntitles);
////            graph.setPieChartColumns(barchartcolumnnames);
////            graph.setTimeLevel("Day");
////            graph.setDisplayGraphRows("All");
////
////            ProgenChartDisplay pchart = new ProgenChartDisplay(600,400);
////            pchart.setCtxPath(request.getContextPath());
////            pchart.setGraph(graph);
////            pchart.setSession(session);
////            pchart.setResponse(response);
//////            if(response!=null){
////            pchart.setOut(response.getWriter());
////            pchart.setRetObj(pbretObjForTime);
////            pchart.setChartType("line");
////            pchart.GetTimeSeriesChart();
//
//            PbGraphDisplay pbDisplay = new PbGraphDisplay();
//            ArrayList grpDetails = new ArrayList();
//            ProgenChartDisplay[] pcharts = null;
//            String reportId = null;
//            String graphId = null;
//            DashletDetail dashlet = new DashletDetail();
//            GraphReport graphDetails = new GraphReport();
//            GraphProperty graphPro = new GraphProperty();
//            graphPro.setBarChartColumnNames1(barchartcolumnnames);
//            graphPro.setBarChartColumnNames2(viewbycolumns);
//            graphPro.setBarChartColumnTitles1(barchartcolumntitles);
//            graphPro.setBarChartColumnTitles2(viewbycolumnTies);
//            ProgenParam pramnam = new ProgenParam();
//            String date = pramnam.getdateforpage();
//            graphPro.setFromDate((String) arl.get(2));
//            graphPro.setStackedType("absStacked");
//            graphPro.setToDate("Day");
//
//
//
//            List<QueryDetail> queryDetail = new ArrayList<QueryDetail>();
//            QueryDetail queryDetais = new QueryDetail();
//            queryDetais.setElementId(String.valueOf(QueryCols.get(0)));
//            queryDetais.setAggregationType(String.valueOf(QueryAggs.get(0)));
//            queryDetais.setDisplayName(measName);
//            queryDetais.setColumnType("NUMBER");
//            queryDetail.add(queryDetais);
//            graphDetails.setQueryDetails(queryDetail);
//
//            graphDetails.setGraphWidth("600");
//            graphDetails.setGraphHeight("400");
//            graphDetails.setGraphClass(7);
//            graphDetails.setGraphSize(1);
//            graphDetails.setShowXAxisGrid(true);
//            graphDetails.setShowYAxisGrid(true);
//            graphDetails.setLeftYAxisLabel("");
//            graphDetails.setRightYAxisLabel("");
//            graphDetails.setBackgroundColor("");
//            graphDetails.setShowData(true);
//            if(pbretObjForTime != null && pbretObjForTime.getRowCount()>0){
//                graphDetails.setDisplayRows(String.valueOf(pbretObjForTime.getRowCount()));
//            }
//            graphDetails.setLinkAllowed(true);
//            graphDetails.setLegendLocation("Bottom");
//            graphDetails.setGraphTypeName("Line");
//            graphDetails.setGraphClassName("Category");
//            graphDetails.setGraphSizeName("Medium");
//            graphDetails.setGraphName(measName);
//            graphDetails.setAxis("0");
//            graphDetails.setGraphProperty(graphPro);
//            graphDetails.setTimeSeries(true);
//            GraphProperty graphProp = new GraphProperty();
//                graphProp.setEndValue(10);
//                graphProp.setStartValue(0);
//                graphProp.setNumberFormat("");
//                graphProp.setSymbol("");
//                graphProp.setSwapGraphColumns("true");
//            graphDetails.setGraphProperty(graphProp);
//
//            dashlet.setRefReportId(reportId);
//            dashlet.setGraphId(graphId);
//            dashlet.setKpiMasterId("0");
//            dashlet.setDashBoardDetailId("0");
//            dashlet.setDisplaySequence(0);
//            dashlet.setDisplayType(DashboardConstants.GRAPH_REPORT);
//            dashlet.setDashletName(measName);
//            dashlet.setKpiType(null);
//            dashlet.setReportDetails(graphDetails);
//
////             pbDisplay.setReportId(reportId);
//            pbDisplay.setCurrentDispRetObjRecords(pbretObjForTime);
//            pbDisplay.setCurrentDispRecordsRetObjWithGT(pbretObjForTime);
//            pbDisplay.setAllDispRecordsRetObj(pbretObjForTime);
//            pbDisplay.setOut(response.getWriter());
//            pbDisplay.setResponse(response);
//            pbDisplay.setViewByElementIds(viewbycolumns);
//            pbDisplay.setViewByColNames(viewbycolumns);
//            pbDisplay.setJscal(null);
//            pbDisplay.setCtxPath(request.getContextPath());
//            pbDisplay.setShowGT("N");
//            pbDisplay.setSession(session);
//            grpDetails = pbDisplay.getDashboardGraphHeadersNew(reportId, graphId, dashlet);
//            //            grpDetails = pbDisplay.getGraphByGraphId(reportId, graphId, ParametersMap);
//
//            if (grpDetails != null && !grpDetails.isEmpty()) {
//                pcharts = (ProgenChartDisplay[]) grpDetails.get(2);
//                if (pcharts != null && pcharts.length != 0) {
//                    imgdata = pcharts[0].chartDisplay;
//                }
//            }
//
//
////            pchart.GetKPITimeSeriesChart();
////            pchart.GetKPITimeSeriesChartZoom(viewbycolumnss, barchartcolumntitless, keyvaluess, datavalues);
////            }
////            imgdata=pchart.chartDisplay;
//        }
//        StringBuilder imgtest = new StringBuilder();
//
////          int inxexof = imgdata.lastIndexOf("</div>");
////          imgtest.append("<table><tr><td>").append(imgdata.substring(inxexof+6)).append("</td></tr></table>");
////          String value=imgtest.toString();
////          String subString = value.substring(value.indexOf("<table>"));
        PrintWriter out = response.getWriter();
        out.print(imgtest.toString());
        return null;

    }

    public ActionForward oneViewRenaming(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String oneviewId = request.getParameter("oneviewId");
        String onviewName = request.getParameter("onviewName");
        ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();
        reportTemplateDAO.oneviewRenaming(oneviewId, onviewName);
        return null;

    }
    //Surender

    public ActionForward gerateOneviewPdf(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, Exception {
        String oneviewID = request.getParameter("oneviewId");
        String onviewName = request.getParameter("OneviewName");
        String advHtmlFileProps = (String) request.getSession(false).getAttribute("advHtmlFileProps");

        HttpSession session = request.getSession(false);
        OnceViewContainer onecontainer = null;

        String regionFile = null;
        List<String> tiemdeatilsArray = null;
        String fileName = null;
        PbReturnObject retObj = null;

        ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();
        retObj = reportTemplateDAO.testForExisting();
        ArrayList<String> va = new ArrayList<String>();
        for (int i = 0; i < retObj.getRowCount(); i++) {
            va.add(retObj.getFieldValueString(i, "ONEVIEWID"));
        }
        if (va.contains(oneviewID)) {
            retObj = reportTemplateDAO.getOneviewFileNam(oneviewID);
            fileName = retObj.getFieldValueString(0, "ONEVIEW_FILE");
            FileInputStream fis = new FileInputStream(advHtmlFileProps + "/" + fileName);
            ObjectInputStream ois = new ObjectInputStream(fis);
            onecontainer = (OnceViewContainer) ois.readObject();
            ois.close();
            regionFile = reportTemplateDAO.getRegionOneview(oneviewID);
        }
        StringBuilder testBuild = new StringBuilder();
        if (regionFile != null) {
            FileInputStream fis3 = new FileInputStream(advHtmlFileProps + "/" + regionFile);
            ObjectInputStream ois3 = new ObjectInputStream(fis3);
            String v = (String) ois3.readObject();
            testBuild.append(v);
            if (!onecontainer.timedetails.isEmpty()) {
                tiemdeatilsArray = onecontainer.timedetails;
//                request.setAttribute("OneviewTiemDetails", tiemdeatilsArray);
            }
            List<OneViewLetDetails> dashletDetails = onecontainer.onviewLetdetails;
            for (int i = 0; i < dashletDetails.size(); i++) {
                OneViewLetDetails detail = dashletDetails.get(i);
                if (detail.getReptype().toString().equalsIgnoreCase("repGraph")) {
                    DashboardTemplateBD dashboardTemplateBD = new DashboardTemplateBD();
                    String result = "";
                    String grapHtest = "graphTest";
                    request.setAttribute("graphTest", grapHtest);

                    result = dashboardTemplateBD.getGraphDetailsData(request, response, session, detail, tiemdeatilsArray, onecontainer);
                    String val = request.getContextPath() + "/servlet/DisplayChart?filename=" + detail.graphDetails.get(detail.getNoOfViewLets());
                    int end = val.length();
                    int start = testBuild.indexOf(request.getContextPath() + "/servlet/DisplayChart?filename=" + detail.graphDetails.get(detail.getNoOfViewLets()));
                    testBuild.replace(start, start + end, System.getProperty("java.io.tmpdir") + "/" + result);


                }
                if (detail.getKpiType() != null && (detail.getKpiType().equalsIgnoreCase("KPIGraph"))) {
                    DashboardTemplateBD dashboardTemplateBD = new DashboardTemplateBD();
                    String result = "";
                    String grapHtest = "kpiGraph";
                    request.setAttribute("kpiGraph", grapHtest);

                    PbDashboardViewerBD dashboardViewerBD = new PbDashboardViewerBD();
                    Container container = null;
                    container = new Container();
                    pbDashboardCollection collect = new pbDashboardCollection();
                    container.setReportCollect(collect);
                    container.setReportId(detail.getRepId());
                    container.setTableId(detail.getRepId());
                    container.setDbrdName(detail.getRepName());
                    container.setDbrdDesc(detail.getRepName());
                    container.setSessionContext(session, container);
                    dashboardViewerBD.setIsFxCharts(false);
                    dashboardViewerBD.setHttpSession(session);
                    dashboardViewerBD.setDashBoardId(detail.getRepId());
                    dashboardViewerBD.setUserId(session.getAttribute("USERID").toString());
                    dashboardViewerBD.setServletRequest(request);
                    dashboardViewerBD.setServletResponse(response);
                    dashboardViewerBD.setContainer(container);
                    dashboardViewerBD.displayDataForOneviewKpis(request, null, session.getAttribute("USERID").toString());
                    container.setSessionContext(session, container);

                    result = dashboardTemplateBD.getOneviewKpisData(request, response, session, detail, onecontainer);
                    if (result != null) {
                        String toolIndex = "#" + detail.kpiGraphDetails.get(detail.getNoOfViewLets());
                        int end1 = toolIndex.length();
                        int start1 = testBuild.indexOf("#" + detail.kpiGraphDetails.get(detail.getNoOfViewLets()));
                        testBuild.replace(start1, start1 + end1, "#" + result);

                        String val = request.getContextPath() + "/servlet/DisplayChart?filename=" + detail.kpiGraphDetails.get(detail.getNoOfViewLets());
                        int end = val.length();
                        int start = testBuild.indexOf(request.getContextPath() + "/servlet/DisplayChart?filename=" + detail.kpiGraphDetails.get(detail.getNoOfViewLets()));
                        testBuild.replace(start, start + end, System.getProperty("java.io.tmpdir") + "/" + result);
                    }
//            String redValue = request.getContextPath() + "/images/Red Arrow.jpeg";
//            String greenVal = request.getContextPath() + "/images/Green Arrow.jpg";
//            int endred = redValue.length();
//            int endgreen = greenVal.length();
//
//                int startgreen = testBuild.indexOf(request.getContextPath() + "/images/Green Arrow.jpg");
//                if(startgreen!=-1)
//                testBuild.replace(startgreen, startgreen+endgreen, System.getProperty("java.io.tmpdir")+"/Green Arrow.jpg" );
//
//                int startred = testBuild.indexOf(request.getContextPath() + "/images/Red Arrow.jpeg");
//                 if(startred!=-1)
//                testBuild.replace(startred, startred+endred, System.getProperty("java.io.tmpdir")+"/Red Arrow.jpeg" );
//
//            String comments = "Add/View Comments";
//            int endCommets = comments.length();
//
//                int startgre = testBuild.indexOf("Add/View Comments");
//                if(startgre!=-1)
//                testBuild.replace(startgre, startgre+endCommets, "");

                }
            }
//              String comments = "Add/View Comments";
//            int endCommets = comments.length();
//
//                int startgre = testBuild.indexOf("Add/View Comments");
//                if(startgre!=-1)
//                testBuild.replace(startgre, startgre+endCommets, "");

            ois3.close();
        } else {
            testBuild.append("No Data");
        }
//             FileOutputStream fileStream = new FileOutputStream("/home/progen/Desktop/SampleTest.pdf");
//              Client client = new Client("SurenderMaddi", "5b2de2e0407159095d6eae97e84a33f5");
//
////                ByteArrayOutputStream memStream  = new ByteArrayOutputStream();
//               client.setPageWidth("175mm");
//                client.setNoModify(true);
//                client.setNoCopy(true);
//                client.enableHyperlinks(false);
//                client.enableImages(true);
//                client.setPageHeight("-1");
////                  client.convertHtml("/home/progen/Desktop/Testfile.zip", fileStream);
//////                  client.convertFile("/home/progen/Desktop/Testfile.zip", fileStream);
//                client.convertHtml(testBuild.toString(), fileStream);
//                fileStream.close();
//
//
////            client.convertURI("http://www.progenbusiness.com/", fileStream);
////           fileStream.close();


        DataSnapshotGenerator reportTemplate = new DataSnapshotGenerator();
        String filename = reportTemplate.htmlgernaration(oneviewID, onviewName, testBuild.toString());
        session.setAttribute("OneviewName", filename);
        return null;

    }

    public ActionForward setNewTypeMeasure(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException, ClassNotFoundException, FileNotFoundException, BadElementException {
        HttpSession session = request.getSession(false);
        String oneviewId = request.getParameter("oneviewId");
        String regionId = request.getParameter("regionId");
        String displayType = request.getParameter("displayType");
        boolean newType = Boolean.parseBoolean(request.getParameter("newType"));
        HashMap map = null;
        String advHtmlFileProps = (String) request.getSession(false).getAttribute("advHtmlFileProps");
        OnceViewContainer onecontainer = new OnceViewContainer();
        if (session != null) {
            map = (HashMap) session.getAttribute("ONEVIEWDETAILS");
            if (map != null) {
                onecontainer = (OnceViewContainer) map.get(oneviewId);

                OneViewLetDetails detail = new OneViewLetDetails();
                detail = onecontainer.onviewLetdetails.get(Integer.parseInt(request.getParameter("regionId")));
                detail.setNewTypeMeasure(newType);
                detail.setDisplayType(displayType);
            } else {
                OneViewLetDetails detail = new OneViewLetDetails();

                ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();
//                onecontainer = reportTemplateDAO.getOneViewData(oneviewId);

                String fileName = request.getSession(false).getAttribute("tempFileName").toString();
                FileInputStream fis2 = new FileInputStream(advHtmlFileProps + "/" + fileName);
                ObjectInputStream ois2 = new ObjectInputStream(fis2);
                onecontainer = (OnceViewContainer) ois2.readObject();
                ois2.close();
                detail = onecontainer.onviewLetdetails.get(Integer.parseInt(regionId));
                detail.setNewTypeMeasure(newType);
                detail.setDisplayType(displayType);
//                reportTemplateDAO.updateOneviewData(onecontainer, oneviewId);
                DashboardTemplateBD dashboardTemplateBD = new DashboardTemplateBD();
//                 OneViewBD oneViewBD = new OneViewBD();
                if (detail.getPbReturnObject() == null) {
                    detail.setMeasurOprFlag(true);
                } else {
                    detail.setMeasurOprFlag(false);
                }
                /*
                 * String result = ""; result =
                 * dashboardTemplateBD.getMeasureDetailsData(request, response,
                 * session,onecontainer, detail);
                 * oneViewBD.writeOneviewRegData(onecontainer,result,detail.getNoOfViewLets(),request);
                 */



                FileOutputStream fos = new FileOutputStream(advHtmlFileProps + "/" + fileName);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(onecontainer);
                oos.flush();
                oos.close();
            }

        }
        return null;
    }

    public ActionForward drillForRegionName(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession(false);
        String oneviewForDrillId = "";
        oneviewForDrillId = request.getParameter("oneviewForDrillId");
        String setDrill = request.getParameter("setDrill");
        String regIdValue = request.getParameter("regIdValue");
        String reportId = request.getParameter("REPORTID");
        String repType = request.getParameter("REPTYPE");
        String graphtoreport = request.getParameter("graphtoreport");
        HashMap map = null;
        String result1 = "";
        String advHtmlFileProps = (String) request.getSession(false).getAttribute("advHtmlFileProps");
//        if(session.getAttribute("MeasureDrillTest")!=null){
//            HashMap <String ,ArrayList<String>> drillMap = null;
//            drillMap = (HashMap<String, ArrayList<String>>) session.getAttribute("MeasureDrillTest");
//             ArrayList<String> values = new ArrayList<String>();
//             values.add(oneviewForDrillId);
//             values.add(setDrill);
//             drillMap.put(regIdValue, values);
//          }
//        else{
//        ArrayList<String> values = new ArrayList<String>();
//        values.add(oneviewForDrillId);
//        values.add(setDrill);
//        HashMap <String ,ArrayList<String>> drillMap = new HashMap<String, ArrayList<String>>();
//        drillMap.put(regIdValue, values);
//        session.setAttribute("MeasureDrillTest", drillMap);
//        }
        OnceViewContainer onecontainer = new OnceViewContainer();
        if (session != null) {
            map = (HashMap) session.getAttribute("ONEVIEWDETAILS");
            if (map != null) {
                onecontainer = (OnceViewContainer) map.get(oneviewForDrillId);

                OneViewLetDetails detail = new OneViewLetDetails();
                detail = onecontainer.onviewLetdetails.get(Integer.parseInt(request.getParameter("regIdValue")));
                detail.setMeasurDrill(setDrill);
                detail.setgraphtoreport(graphtoreport);

//                ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();
//                String fileName = reportTemplateDAO.getOneviewFileName(oneviewForDrillId);
//                 FileOutputStream fos = new FileOutputStream(advHtmlFileProps+"/"+fileName);
//                ObjectOutputStream oos = new ObjectOutputStream(fos);
//                oos.writeObject(onecontainer);
//                oos.flush();
//                oos.close();
            } else {
                OneViewLetDetails detail = new OneViewLetDetails();

                ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();
//                  onecontainer = reportTemplateDAO.getOneViewData(oneviewForDrillId);

                // String fileName = reportTemplateDAO.getOneviewFileName(oneviewForDrillId);
                String fileName = session.getAttribute("tempFileName").toString();
                FileInputStream fis2 = new FileInputStream(advHtmlFileProps + "/" + fileName);
                ObjectInputStream ois2 = new ObjectInputStream(fis2);
                onecontainer = (OnceViewContainer) ois2.readObject();
                ois2.close();
//                String fileName = onecontainer.getContainerFileName();
//                String fileName = onecontainer.getContainerFileName();

                detail = onecontainer.onviewLetdetails.get(Integer.parseInt(regIdValue));
                detail.setMeasurDrill(setDrill);
                DashboardTemplateBD dashboardTemplateBD = new DashboardTemplateBD();
                OneViewBD oneViewBD = new OneViewBD();
                if (detail.getPbReturnObject() == null) {
                    detail.setMeasurOprFlag(false);
                } else {
                    detail.setMeasurOprFlag(true);
                }
                //added by srikanth.p for ahowing thumnails graphs at measure
                detail.setAssignedReportId(reportId);
//                  reportTemplateDAO.updateOneviewData(onecontainer, oneviewForDrillId);
                if (reportId != null && repType != null && repType.equalsIgnoreCase("R")) {
                    DashboardViewerDAO dao = new DashboardViewerDAO();
                    String result = dao.getGraphsOnReport(reportId, detail);
                    response.getWriter().print(result);
                }



                /*
                 * if
                 * (detail.getReptype().toString().equalsIgnoreCase("measures"))
                 * { result1 =
                 * dashboardTemplateBD.getMeasureDetailsData(request, response,
                 * session,onecontainer, detail); }else if
                 * (detail.getReptype().toString().equalsIgnoreCase("repKpis"))
                 * { PbDashboardViewerAction genDashForOneview = new
                 * PbDashboardViewerAction(); request.setAttribute("REPORTID",
                 * detail.getRepId()); request.setAttribute("OneviewTest",
                 * true);
                 * request.setAttribute("dashletId",detail.getKpiDashLetNo());
                 * genDashForOneview.viewDashboard(mapping, form, request,
                 * response); result1 =
                 * dashboardTemplateBD.getOneviewKpisData(request, response,
                 * session, detail); }else if
                 * (detail.getReptype().toString().equalsIgnoreCase("repTable"))
                 * { result1 = dashboardTemplateBD.getTableDetailsData(request,
                 * response, session, detail,onecontainer.timedetails); }else
                 * if(detail.getReptype().toString().equalsIgnoreCase("repGraph")){
                 * result1 = dashboardTemplateBD.getGraphDetailsData(request,
                 * response, session, detail,onecontainer.timedetails); }else if
                 * (detail.getReptype().toString().equalsIgnoreCase("headLine"))
                 * { result1 =
                 * dashboardTemplateBD.getHeadLinehDetailsData(request,
                 * response, session, detail); }if
                 * (detail.getReptype().toString().equalsIgnoreCase("complexkpi"))
                 * { result1 = dashboardTemplateBD.getComplexKpisData(request,
                 * response, session,
                 * detail,oneviewForDrillId,Integer.parseInt(regIdValue)); }
                 * oneViewBD.writeOneviewRegData(onecontainer,result1,detail.getNoOfViewLets(),request);
                 */


                FileOutputStream fos = new FileOutputStream(advHtmlFileProps + "/" + fileName);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(onecontainer);
                oos.flush();
                oos.close();

            }

        }

        return null;
    }
    //Surender

    public ActionForward getDashboardKpis(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException, ClassNotFoundException, Exception {
        HttpSession session = request.getSession(false);
        DashboardTemplateBD dashboardTemplateBD = new DashboardTemplateBD();
        dashboardTemplateBD.setServletRequest(request);
        PrintWriter out = response.getWriter();
        String advHtmlFileProps = (String) request.getSession(false).getAttribute("advHtmlFileProps");
        HashMap map = null;
        OnceViewContainer onecontainer = new OnceViewContainer();
        String reportId = request.getParameter("reportId");
        String gaphName = request.getParameter("name");
        String oneViewIdValue = request.getParameter("oneViewIdValue");
        String repType = request.getParameter("repType");
        String busroleId = request.getParameter("busroleId");
        String dashletNo = request.getParameter("dashletNo");
        String kpiMasterid = request.getParameter("kpiMasterId");
        String kpiTypes = request.getParameter("kpiTypes");
        int width = Integer.parseInt(request.getParameter("width"));
        int height = Integer.parseInt(request.getParameter("height"));

        String result = "";
        if (session != null) {
            map = (HashMap) session.getAttribute("ONEVIEWDETAILS");
            if (map != null) {
                onecontainer = (OnceViewContainer) map.get(oneViewIdValue);

                OneViewLetDetails detail = new OneViewLetDetails();
                detail = onecontainer.onviewLetdetails.get(Integer.parseInt(request.getParameter("divId")));
                detail.setRepId(reportId);
                detail.setRepName(gaphName);
                detail.setReptype(repType);
                detail.setWidth(width);
                detail.setHeight(height);
                detail.setRoleId(busroleId);
                detail.setKpiType(kpiTypes);
                detail.setKpiDashLetNo(dashletNo);
                detail.setKpiMasterId(kpiMasterid);
                if (detail.getMeasurDrill() != null) {
                    detail.setMeasurDrill(null);
                }
            } else {
                OneViewLetDetails detail = null;

                ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();
//              onecontainer = reportTemplateDAO.getOneViewData(oneViewIdValue);
                //  String fileName = reportTemplateDAO.getOneviewFileName(oneViewIdValue);
                String fileName = session.getAttribute("tempFileName").toString();
                FileInputStream fis2 = new FileInputStream(advHtmlFileProps + "/" + fileName);
                ObjectInputStream ois2 = new ObjectInputStream(fis2);
                onecontainer = (OnceViewContainer) ois2.readObject();
                ois2.close();

                detail = onecontainer.onviewLetdetails.get(Integer.parseInt(request.getParameter("divId")));
                detail.setRepId(reportId);
                detail.setRepName(gaphName);
                detail.setReptype(repType);
                detail.setRoleId(busroleId);
                detail.setKpiType(kpiTypes);
                detail.setWidth(width);
                detail.setHeight(height);
                detail.setKpiDashLetNo(dashletNo);
                detail.setKpiMasterId(kpiMasterid);
                if (detail.getMeasurDrill() != null) {
                    detail.setMeasurDrill(null);
                }
//              detail.graphDetails.put(request.getParameter("divId"),image);
//             reportTemplateDAO.updateOneviewData(onecontainer,oneViewIdValue);

                HashMap tempRegHashMap = onecontainer.getTempRegHashMap();
                String tempRegFileName = (String) tempRegHashMap.get(Integer.parseInt(detail.getNoOfViewLets()));
                if (tempRegFileName == null) {
                    tempRegFileName = "InnerRegionDetails" + onecontainer.oneviewId + "_" + detail.getNoOfViewLets() + "_" + session.getId() + "_" + System.currentTimeMillis() + ".txt";
                    tempRegHashMap.put(Integer.parseInt(detail.getNoOfViewLets()), tempRegFileName);
                }
                PbDashboardViewerAction genDashForOneview = new PbDashboardViewerAction();
                OneViewBD oneViewBD = new OneViewBD();
                request.setAttribute("REPORTID", detail.getRepId());
                request.setAttribute("OneviewTest", true);
                request.setAttribute("dashletId", detail.getKpiDashLetNo());
                if (onecontainer.getFilterBusinessRole() != null && !onecontainer.getFilterBusinessRole().equalsIgnoreCase("") && detail != null && detail.getRoleId() != null && !detail.getRoleId().equalsIgnoreCase("") && onecontainer.getFilterBusinessRole().equalsIgnoreCase(detail.getRoleId())) {
                    if (onecontainer.getReportParameterValues() != null && !onecontainer.getReportParameterValues().isEmpty()) {
                        request.setAttribute("reportParameterVals", (LinkedHashMap) onecontainer.getReportParameterValues());
                    }
                }
                genDashForOneview.viewDashboard(mapping, form, request, response);
                String result1 = "";
                result1 = dashboardTemplateBD.getOneviewKpisData(request, response, session, detail, onecontainer);
                oneViewBD.writeOneviewRegData(onecontainer, result1, detail.getNoOfViewLets(), request);
                FileOutputStream fos = new FileOutputStream(advHtmlFileProps + "/" + fileName);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(onecontainer);
                oos.flush();
                oos.close();
            }

            Container container = null;
            String dashBoardId = null;
            String kpiDrill = null;
            pbDashboardCollection collect = null;
            KPIBuilder kpibuilder = new KPIBuilder();
            String fromDesigner = null;
            String editDbrd = null;
            PbReportRequestParameter reportReqParams = null;

            HashMap DBKPIHashMap = null;

            if (session != null && session.getAttribute("USERID") != null && session.getAttribute("PROGENTABLES") != null) {
                dashBoardId = request.getParameter("reportId");
//            kpiMasterId = request.getParameter("divId");
                kpiDrill = "N";
                map = (HashMap) session.getAttribute("PROGENTABLES");
                fromDesigner = request.getParameter("fromDesigner");
                editDbrd = request.getParameter("editDbrd");

                boolean createForDesigner = false;
                if (fromDesigner != null && "true".equalsIgnoreCase(fromDesigner)) {
                    createForDesigner = true;
                }

                container = (Container) map.get(dashBoardId);
                collect = (pbDashboardCollection) container.getReportCollect();
//            collect.reportIncomingParameters = container.getRepReqParamsHashMap();
                if (kpiTypes.equalsIgnoreCase("Basic") || kpiTypes.equalsIgnoreCase("Target") || kpiTypes.equalsIgnoreCase("Standard") || kpiTypes.equalsIgnoreCase("MultiPeriod") || kpiTypes.equalsIgnoreCase("MultiPeriodCurrentPrior") || kpiTypes.equalsIgnoreCase("BasicTarget")) {
                    String userId = (String) session.getAttribute("USERID");
                    DBKPIHashMap = container.getDBKPIHashMap();


                    collect.setDBKPIHashMap(DBKPIHashMap);
                    collect.reportId = dashBoardId;//here reportId is DashBoard Id
                    collect.ctxPath = request.getContextPath();
                    collect.setServletRequest(request);
                    collect.setServletResponse(response);
                    collect.setSession(session);

                    DashletDetail detail = collect.getDashletDetail(dashletNo);
                    collect.setOneviewCheckForKpis(true);
                    collect.setOneViewWidth(request.getParameter("width"));
                    if (!detail.getKpiType().equalsIgnoreCase("Complexkpi")) {
                        result = kpibuilder.processSingleKpi(container, kpiMasterid, collect.kpiQuery, kpiDrill, dashletNo, dashBoardId, createForDesigner, collect, userId, editDbrd);
                    } else {
                        KPI kpiDetails = (KPI) detail.getReportDetails();
                        List<String> a1 = kpiDetails.getElementIds();
                        String ElemntIds[] = new String[a1.size()];
                        for (int i = 0; i < a1.size(); i++) {
                            ElemntIds[i] = a1.get(i);
                        }
                        request.setAttribute("oneviewTest", true);
                        DashboardTemplateDAO dao = new DashboardTemplateDAO();
                        result = dao.getBuildCreateKPI(ElemntIds, request, dashBoardId);
                    }
                } else {
                    reportReqParams = new PbReportRequestParameter(request);
                    reportReqParams.setParametersHashMap();
                    GraphBuilder graphBuilder = new GraphBuilder();
                    graphBuilder.setRequest(request);
                    graphBuilder.setResponse(response);
                    graphBuilder.setFxCharts(false);
                    collect.setOneviewCheckForKpis(true);
                    collect.setOneViewWidth(request.getParameter("width"));
                    collect.setOneViewHeight(request.getParameter("height"));

                    result = graphBuilder.displayGraphs(container, dashletNo, request.getContextPath(), createForDesigner, editDbrd);
                }
            }
            out.print(result);

            return null;
        } else {
            return null;
        }
    }

    public ActionForward oneviewAndReportTimeDeatails(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException, ClassNotFoundException {
        String regionId1 = request.getParameter("regionId");
        String oneviewId = request.getParameter("oneviewId");
        boolean oneviewTime = Boolean.parseBoolean(request.getParameter("oneviewTime"));
        String advHtmlFileProps = (String) request.getSession(false).getAttribute("advHtmlFileProps");
        OneViewLetDetails detail = new OneViewLetDetails();
        OnceViewContainer onecontainer = new OnceViewContainer();

        ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();
//                onecontainer = reportTemplateDAO.getOneViewData(oneviewId);

        //String fileName = reportTemplateDAO.getOneviewFileName(oneviewId);
        String fileName = request.getSession(false).getAttribute("tempFileName").toString();
        FileInputStream fis2 = new FileInputStream(advHtmlFileProps + "/" + fileName);
        ObjectInputStream ois2 = new ObjectInputStream(fis2);
        onecontainer = (OnceViewContainer) ois2.readObject();
        ois2.close();

//                detail = onecontainer.onviewLetdetails.get(Integer.parseInt(regionId));
        List<OneViewLetDetails> oneviewletDetails = onecontainer.onviewLetdetails;
        for (int i = 0; i < oneviewletDetails.size(); i++) {
            detail = oneviewletDetails.get(i);
            String regionid = detail.getNoOfViewLets();
            if (regionid != null && regionid.equalsIgnoreCase(regionId1)) {

                detail.setOneviewReportTimeDetails(oneviewTime);
            }
        }
//                reportTemplateDAO.updateOneviewData(onecontainer, oneviewId);
        FileOutputStream fos = new FileOutputStream(advHtmlFileProps + "/" + fileName);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(onecontainer);
        oos.flush();
        oos.close();
        return null;
    }

    public ActionForward oneviewCompAndNoComp(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException, ClassNotFoundException {
        String regionId = request.getParameter("regionId");
        String oneviewId = request.getParameter("oneviewId");
        boolean oneviewMeasureCompare = Boolean.parseBoolean(request.getParameter("oneviewCompare"));
        String advHtmlFileProps = (String) request.getSession(false).getAttribute("advHtmlFileProps");
        OneViewLetDetails detail = new OneViewLetDetails();
        OnceViewContainer onecontainer = new OnceViewContainer();

        ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();
//                onecontainer = reportTemplateDAO.getOneViewData(oneviewId);

        //String fileName = reportTemplateDAO.getOneviewFileName(oneviewId);
        String fileName = request.getSession(false).getAttribute("tempFileName").toString();
        FileInputStream fis2 = new FileInputStream(advHtmlFileProps + "/" + fileName);
        ObjectInputStream ois2 = new ObjectInputStream(fis2);
        onecontainer = (OnceViewContainer) ois2.readObject();
        ois2.close();

        detail = onecontainer.onviewLetdetails.get(Integer.parseInt(regionId));
        detail.setOneviewMeasureCompare(oneviewMeasureCompare);
//                reportTemplateDAO.updateOneviewData(onecontainer, oneviewId);
        FileOutputStream fos = new FileOutputStream(advHtmlFileProps + "/" + fileName);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(onecontainer);
        oos.flush();
        oos.close();
        return null;
    }

    public ActionForward refreshOneView(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(false);
        OnceViewContainer onecontainer1 = null;
        String oneViewId = request.getParameter("oneViewIdValue");
        String oldAdvHtmlFileProps = (String) request.getSession(false).getAttribute("oldAdvHtmlFileProps");
        String advHtmlFileProps = (String) request.getSession(false).getAttribute("advHtmlFileProps");
        ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();
        String fileName = reportTemplateDAO.getOneviewFileName(oneViewId);
        File file = null;
        file = new File(oldAdvHtmlFileProps + "/" + fileName);
        if (file.exists()) {
            FileInputStream fis = new FileInputStream(oldAdvHtmlFileProps + "/" + fileName);
            ObjectInputStream ois = new ObjectInputStream(fis);
            onecontainer1 = (OnceViewContainer) ois.readObject();
            ois.close();
        }
        fileName = session.getAttribute("tempFileName").toString();
        FileOutputStream fos = new FileOutputStream(advHtmlFileProps + "/" + fileName);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(onecontainer1);
        oos.flush();
        oos.close();
        String refreshString = (String) session.getAttribute("refreshString");
        response.getWriter().print(refreshString);
        return null;
    }

    public ActionForward getNavigationOptions(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String measId = request.getParameter("measId");
        String measName = request.getParameter("measName");
        String oneViewId = request.getParameter("oneViewId");
        String roleId = request.getParameter("roleId");
        String currVal = request.getParameter("currVal");
        String priorVal = request.getParameter("priorVal");
        String changePer = request.getParameter("changePer");
        String repId = request.getParameter("repId");
        String regionId = request.getParameter("regionId");
        String context = request.getParameter("context");
        HttpSession session = request.getSession(false);
        PbReturnObject pbretObjForTime = new PbReturnObject();

        String advHtmlFileProps = (String) request.getSession(false).getAttribute("advHtmlFileProps");
        //
        OneViewLetDetails detail = new OneViewLetDetails();
        OnceViewContainer onecontainer = new OnceViewContainer();
        List<String> tiemdeatilsArray = null;

        ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();
//                onecontainer = reportTemplateDAO.getOneViewData(oneviewId);

        // String fileName = reportTemplateDAO.getOneviewFileName(oneViewId);
        String fileName = request.getSession(false).getAttribute("tempFileName").toString();
        FileInputStream fis2 = new FileInputStream(advHtmlFileProps + "/" + fileName);
        ObjectInputStream ois2 = new ObjectInputStream(fis2);
        onecontainer = (OnceViewContainer) ois2.readObject();
        ois2.close();

        detail = onecontainer.onviewLetdetails.get(Integer.parseInt(regionId));
        if (onecontainer.timedetails != null && !onecontainer.timedetails.isEmpty()) {
            tiemdeatilsArray = onecontainer.timedetails;
        }
        ArrayList arl = new ArrayList();
        ArrayList arl1 = new ArrayList();
        ArrayList arl2 = new ArrayList();

        if (onecontainer != null && onecontainer.timedetails != null && !onecontainer.timedetails.isEmpty()) {
            arl = (ArrayList) onecontainer.timedetails;
            arl1.addAll(onecontainer.timedetails);
            arl2.addAll(onecontainer.timedetails);
        } else {
            arl.add("Day");
            arl.add("PRG_STD");
            ProgenParam pramnam = new ProgenParam();
            String date = pramnam.getdateforpage();
            arl.add(date);
            arl.add("Month");
            arl.add("Last Period");

            arl1.add("Day");
            arl1.add("PRG_STD");
            arl1.add(date);
            arl1.add("Month");
            arl1.add("Last Period");

            arl2.addAll(arl1);
        }
        arl1.set(3, "Month");

        String userId = String.valueOf(session.getAttribute("USERID"));
        ArrayList rowview = new ArrayList();
        rowview.add("TIME");
        ArrayList QueryCols = new ArrayList();
        ArrayList QueryAggs = new ArrayList();
        List<String> measureIdVal = new ArrayList<String>();
        measureIdVal.add(measId);
        DashboardViewerDAO dao = new DashboardViewerDAO();

        List<KPIElement> kpiElements = dao.getKPIElements(measureIdVal, new HashMap<String, String>());
        if (kpiElements != null) {
            for (KPIElement elem : kpiElements) {
                if (elem.getElementName() != null) {
                    QueryCols.add(elem.getElementId());
                }
                QueryAggs.add(elem.getAggregationType());
            }
        }
        StringBuilder kpiheadsbuilder = new StringBuilder();
        kpiheadsbuilder.append("<table align='right'><tr><td id=\"additionalInfoTd\"><a href='javascript:void(0)' onclick=\"getAdditionalData('" + regionId + "','" + oneViewId + "','" + currVal + "','" + changePer + "','" + measId + "','" + measName + "','" + priorVal + "','" + roleId + "')\">Additional Info.</a></td></tr></table>");
        if (QueryCols.size() == 4) {

            PbReportQuery repQuery = new PbReportQuery();

            //repQuery.setParamValue(collect.reportParametersValues);//Added by k
            repQuery.setRowViewbyCols(new ArrayList());
            repQuery.setColViewbyCols(new ArrayList());
            repQuery.setQryColumns(QueryCols);
            repQuery.setColAggration(QueryAggs);
            repQuery.setTimeDetails(arl);
            repQuery.setDefaultMeasure(String.valueOf(QueryCols.get(0)));
            repQuery.setDefaultMeasureSumm(String.valueOf(QueryAggs.get(0)));
            repQuery.isKpi = true;
            repQuery.setBizRoles(roleId);
            repQuery.setUserId(userId);

            MultiPeriodKPI multiPeriodKPI = new MultiPeriodKPI();
            ArrayList<String> multiperiods = new ArrayList<String>();
            multiperiods.add("Year");
            multiperiods.add("Qtr");
            multiperiods.add("Month");

//                multiperiods.add("Day");
//        if(collect.dashletDetails.get(0).getKpiType().equalsIgnoreCase("MultiPeriod")){
            PbReturnObject kpiMulitiObject = new PbReturnObject();
            ArrayList tempTimeDetailsArrayList = (ArrayList) arl2.clone();
            for (String str : multiperiods) {
                tempTimeDetailsArrayList.set(3, str);
                repQuery.setTimeDetails(tempTimeDetailsArrayList);
                kpiMulitiObject = repQuery.getPbReturnObjectWithFlag(String.valueOf(QueryCols.get(0)));
                if (str.equalsIgnoreCase("Year")) {
                    multiPeriodKPI.setYearObject(kpiMulitiObject);
                } else if (str.equalsIgnoreCase("Qtr")) {
                    multiPeriodKPI.setQuarterObject(kpiMulitiObject);
                } else if (str.equalsIgnoreCase("Month")) {
                    multiPeriodKPI.setMonthObject(kpiMulitiObject);
                }
//                        else if (str.equalsIgnoreCase("Day")) {
//                            multiPeriodKPI.setDayObject(kpiMulitiObject);
//                        }
            }
            detail.setMultiPeriodKPI(multiPeriodKPI);
            repQuery.setTimeDetails(arl);

            String query = null;
            arl1.set(3, "Month");
//                pbretObjForTime = repQuery.getPbReturnObject(String.valueOf(QueryCols.get(0)));
            String val14 = "";

            String monthfontColor = "";
            String yearfontColor = "";
            String monthIcon = "";
            String yearIcon = "";
            if (multiPeriodKPI != null && multiPeriodKPI.getMonthObject() != null && Math.round(Double.parseDouble(multiPeriodKPI.getMonthObject().getFieldValueString(0, 4))) > 0) {
                monthIcon = context + "/images/Green Arrow.jpg";
                monthfontColor = "green";
            } else if (multiPeriodKPI != null && multiPeriodKPI.getMonthObject() != null && Math.round(Double.parseDouble(multiPeriodKPI.getMonthObject().getFieldValueString(0, 4))) < 0) {
                monthIcon = context + "/images/Red Arrow.jpeg";
                monthfontColor = "red";
            } else {
                monthIcon = "";
            }

            if (multiPeriodKPI != null && multiPeriodKPI.getYearObject() != null && Math.round(Double.parseDouble(multiPeriodKPI.getYearObject().getFieldValueString(0, 4))) > 0) {
                yearIcon = context + "/images/Green Arrow.jpg";
                yearfontColor = "green";
            } else if (multiPeriodKPI != null && multiPeriodKPI.getYearObject() != null && Math.round(Double.parseDouble(multiPeriodKPI.getYearObject().getFieldValueString(0, 4))) < 0) {
                yearIcon = context + "/images/Red Arrow.jpeg";
                yearfontColor = "red";
            } else {
                yearIcon = "";
            }
            NumberFormat formatter = NumberFormat.getInstance(new Locale("en_US"));
            //kpiheadsbuilder.append("<table style='table-layout:fixed; width:100%; border-spacing: 15px;'><tr>");

            //for MTD
            if (!multiPeriodKPI.getMonthObject().getFieldValueString(0, 1).equalsIgnoreCase("")) {
                double mtd = Double.parseDouble(multiPeriodKPI.getMonthObject().getFieldValueString(0, 1));
                int decimalPlaces = 0;
                BigDecimal mtdcurval = new BigDecimal(mtd);
                mtdcurval = mtdcurval.setScale(decimalPlaces, BigDecimal.ROUND_HALF_UP);
                String formatVal = "";
                String mtdcurvalm = NumberFormatter.getModifiedNumber(mtdcurval, detail.getFormatVal(), Integer.parseInt(detail.getRoundVal()));
                if (mtdcurvalm.contains("M")) {
                    mtdcurvalm = mtdcurvalm.replace("M", "");
                    formatVal = "Mn";
                } else if (mtdcurvalm.contains("L")) {
                    mtdcurvalm = mtdcurvalm.replace("L", "");
                    formatVal = "Lkh";
                } else if (mtdcurvalm.contains("C")) {
                    mtdcurvalm = mtdcurvalm.replace("C", "");
                    formatVal = "Crs";
                } else if (mtdcurvalm.contains("K")) {
                    mtdcurvalm = mtdcurvalm.replace("K", "");
                    formatVal = "K";
                }
                kpiheadsbuilder.append("<table style='table-layout:fixed; width:100%; border-spacing: 15px;'><tr><td width='140%'><table style='margin-left: 10px;'><tr><td class='myDesignClass'>MTD</td></tr></table><div class='ui-tabs ui-widget ui-widget-content ui-corner-all' style='border-top:blank; border-bottom:medium hidden; border-left:medium hidden; border-right:medium hidden;border-color:LightGrey;width:100% ; height:100% ;margin-left: 10px; margin-right: 10px;'><table><tr><td style='font-size:14pt;color:#369;'>" + mtdcurvalm + " <sub>" + formatVal + "</sub></td></tr></table></div></td>");
            } else {
                kpiheadsbuilder.append("<table style='table-layout:fixed; width:100%; border-spacing: 15px;'><tr><td width='140%'><table style='margin-left: 10px;'><tr><td class='myDesignClass'>MTD</td></tr></table><div class='ui-tabs ui-widget ui-widget-content ui-corner-all' style='border-top:blank; border-bottom:medium hidden; border-left:medium hidden; border-right:medium hidden;border-color:LightGrey;width:100% ; height:100% ;margin-left: 10px; margin-right: 10px;'><table><tr><td style='font-size:14pt'>--</td></tr></table></div></td>");
            }

            //for QTD
            if (!multiPeriodKPI.getQuarterObject().getFieldValueString(0, 1).equalsIgnoreCase("")) {
                double qtd = Double.parseDouble(multiPeriodKPI.getQuarterObject().getFieldValueString(0, 1));
                int decimalPlaces = 0;
                BigDecimal qtdcurval = new BigDecimal(qtd);
                qtdcurval = qtdcurval.setScale(decimalPlaces, BigDecimal.ROUND_HALF_UP);
                String formatVal = "";
                String qtdcurvalm = NumberFormatter.getModifiedNumber(qtdcurval, detail.getFormatVal(), Integer.parseInt(detail.getRoundVal()));
                if (qtdcurvalm.contains("M")) {
                    qtdcurvalm = qtdcurvalm.replace("M", "");
                    formatVal = "Mn";
                } else if (qtdcurvalm.contains("L")) {
                    qtdcurvalm = qtdcurvalm.replace("L", "");
                    formatVal = "Lkh";
                } else if (qtdcurvalm.contains("C")) {
                    qtdcurvalm = qtdcurvalm.replace("C", "");
                    formatVal = "Crs";
                } else if (qtdcurvalm.contains("K")) {
                    qtdcurvalm = qtdcurvalm.replace("K", "");
                    formatVal = "K";
                }
                kpiheadsbuilder.append("<td width='140%'><table style='margin-left: 10px;'><tr><td class='myDesignClass'>QTD</td></tr></table><div class='ui-tabs ui-widget ui-widget-content ui-corner-all' style='border-top:blank; border-bottom:medium hidden; border-left:medium hidden; border-right:medium hidden;border-color:LightGrey;width:100% ; height:100% ;margin-left: 10px; margin-right: 10px;'><table><tr><td style='font-size:14pt;color:#369;'>" + qtdcurvalm + " <sub>" + formatVal + "</sub></td></tr></table></div></td>");
            } else {
                kpiheadsbuilder.append("<td width='140%'><table style='margin-left: 10px;'><tr><td class='myDesignClass'>QTD</td></tr></table><div class='ui-tabs ui-widget ui-widget-content ui-corner-all' style='border-top:blank; border-bottom:medium hidden; border-left:medium hidden; border-right:medium hidden;border-color:LightGrey;width:100% ; height:100% ;margin-left: 10px; margin-right: 10px;'><table><tr><td style='font-size:14pt'>--</td></tr></table></div></td>");
            }

            //for YTD
            if (!multiPeriodKPI.getYearObject().getFieldValueString(0, 1).equalsIgnoreCase("")) {
                double ytd = Double.parseDouble(multiPeriodKPI.getYearObject().getFieldValueString(0, 1));
                int decimalPlaces = 0;
                BigDecimal ytdcurval = new BigDecimal(ytd);
                ytdcurval = ytdcurval.setScale(decimalPlaces, BigDecimal.ROUND_HALF_UP);
                String formatVal = "";
                String ytdcurvalm = NumberFormatter.getModifiedNumber(ytdcurval, detail.getFormatVal(), Integer.parseInt(detail.getRoundVal()));
                if (ytdcurvalm.contains("M")) {
                    ytdcurvalm = ytdcurvalm.replace("M", "");
                    formatVal = "Mn";
                } else if (ytdcurvalm.contains("L")) {
                    ytdcurvalm = ytdcurvalm.replace("L", "");
                    formatVal = "Lkh";
                } else if (ytdcurvalm.contains("C")) {
                    ytdcurvalm = ytdcurvalm.replace("C", "");
                    formatVal = "Crs";
                } else if (ytdcurvalm.contains("K")) {
                    ytdcurvalm = ytdcurvalm.replace("K", "");
                    formatVal = "K";
                }
                kpiheadsbuilder.append("<td width='140%'><table style='margin-left: 10px;'><tr><td class='myDesignClass'>YTD</td></tr></table><div class='ui-tabs ui-widget ui-widget-content ui-corner-all' style='border-top:blank; border-bottom:medium hidden; border-left:medium hidden; border-right:medium hidden;border-color:LightGrey;width:100% ; height:100% ;margin-left: 10px; margin-right: 10px;'><table><tr><td style='font-size:14pt;color:#369;'>" + ytdcurvalm + " <sub>" + formatVal + "</sub></td></tr></table></div></td></tr></table>");
            } else {
                kpiheadsbuilder.append("<td width='140%'><table style='margin-left: 10px;'><tr><td class='myDesignClass'>YTD</td></tr></table><div class='ui-tabs ui-widget ui-widget-content ui-corner-all' style='border-top:blank; border-bottom:medium hidden; border-left:medium hidden; border-right:medium hidden;border-color:LightGrey;width:100% ; height:100% ;margin-left: 10px; margin-right: 10px;'><table><tr><td style='font-size:14pt'>--</td></tr></table></div></td></tr></table>");
            }


            //for MOM
//                if(!multiPeriodKPI.getMonthObject().getFieldValueString(0, 2).equalsIgnoreCase("")){
//                    double mom = Double.parseDouble(multiPeriodKPI.getMonthObject().getFieldValueString(0, 2));
//                    int decimalPlaces = 0;
//                    BigDecimal momcurval = new BigDecimal(mom);
//                    momcurval = momcurval.setScale(decimalPlaces, BigDecimal.ROUND_HALF_UP);
//                    String formatVal = "";
//                    String momcurvalm = NumberFormatter.getModifiedNumber(momcurval, detail.getFormatVal(), Integer.parseInt(detail.getRoundVal()));
//                    if(momcurvalm.contains("M")){
//                        momcurvalm = momcurvalm.replace("M", "");
//                        formatVal = "Mn";
//                    }else if(momcurvalm.contains("L")){
//                        momcurvalm = momcurvalm.replace("L", "");
//                        formatVal = "Lkh";
//                    }else if(momcurvalm.contains("C")){
//                        momcurvalm = momcurvalm.replace("C", "");
//                        formatVal = "Crs";
//                    }else if(momcurvalm.contains("K")){
//                        momcurvalm = momcurvalm.replace("K", "");
//                        formatVal = "K";
//                    }
//                    kpiheadsbuilder.append("<table style='table-layout:fixed; width:100%; border-spacing: 15px;'><tr><td width='140%'><table style='margin-left: 10px;'><tr><td class='myDesignClass'>MOM</td></tr></table><div class='ui-tabs ui-widget ui-widget-content ui-corner-all' style='border-top:blank; border-bottom:medium hidden; border-left:medium hidden; border-right:medium hidden;border-color:LightGrey;width:100% ; height:100% ;margin-left: 10px; margin-right: 10px;'><table><tr><td style='font-size:14pt'>"+momcurvalm+ " <sub>" + formatVal + "</sub></td></tr></table></div></td>");
//                }else
//                    kpiheadsbuilder.append("<table style='table-layout:fixed; width:100%; border-spacing: 15px;'><tr><td width='140%'><table style='margin-left: 10px;'><tr><td class='myDesignClass'>MOM</td></tr></table><div class='ui-tabs ui-widget ui-widget-content ui-corner-all' style='border-top:blank; border-bottom:medium hidden; border-left:medium hidden; border-right:medium hidden;border-color:LightGrey;width:100% ; height:100% ;margin-left: 10px; margin-right: 10px;'><table><tr><td style='font-size:14pt'>--</td></tr></table></div></td>");

            //for Change%(MOM)
            if (!multiPeriodKPI.getMonthObject().getFieldValueString(0, 4).equalsIgnoreCase("")) {
                double momc = Double.parseDouble(multiPeriodKPI.getMonthObject().getFieldValueString(0, 4));
                int decimalPlaces = 0;
                BigDecimal momccurval = new BigDecimal(momc);
                momccurval = momccurval.setScale(decimalPlaces, BigDecimal.ROUND_HALF_UP);
                String roundmom = detail.getRoundVal();
                if (roundmom != null && Integer.parseInt(roundmom) == 0) {
                    roundmom = "1";
                }
                String momcurvalm = NumberFormatter.getModifiedNumber(momccurval, "", Integer.parseInt(roundmom));
                kpiheadsbuilder.append("<table style='table-layout:fixed; width:100%; border-spacing: 15px;'><tr><td width='140%'><table style='margin-left: 10px;'><tr><td class='myDesignClass' style='white-space:nowrap;'>Change% (MOM)</td></tr></table><div class='ui-tabs ui-widget ui-widget-content ui-corner-all' style='border-top:blank; border-bottom:medium hidden; border-left:medium hidden; border-right:medium hidden;border-color:LightGrey;width:100% ; height:100% ;margin-left: 10px; margin-right: 10px;'><table><tr><td style='font-size:14pt;color:" + monthfontColor + "'>" + momcurvalm + "% <sub></sub></td><td colspan='1' align='right'><img width='20px' height='20px' src='" + monthIcon + "'></img></td></tr></table></div></td>");
            } else {
                kpiheadsbuilder.append("<table style='table-layout:fixed; width:100%; border-spacing: 15px;'><tr><td width='140%'><table style='margin-left: 10px;'><tr><td class='myDesignClass' style='white-space:nowrap;'>Change% (MOM)</td></tr></table><div class='ui-tabs ui-widget ui-widget-content ui-corner-all' style='border-top:blank; border-bottom:medium hidden; border-left:medium hidden; border-right:medium hidden;border-color:LightGrey;width:100% ; height:100% ;margin-left: 10px; margin-right: 10px;'><table><tr><td></td><td style='font-size:14pt'>--</td></tr></table></div></td>");
            }

            //for YOY
            if (!multiPeriodKPI.getYearObject().getFieldValueString(0, 2).equalsIgnoreCase("")) {
                double yoy = Double.parseDouble(multiPeriodKPI.getYearObject().getFieldValueString(0, 2));
                int decimalPlaces = 0;
                BigDecimal yoycurval = new BigDecimal(yoy);
                yoycurval = yoycurval.setScale(decimalPlaces, BigDecimal.ROUND_HALF_UP);
                String formatVal = "";
                String yoycurvalm = NumberFormatter.getModifiedNumber(yoycurval, detail.getFormatVal(), Integer.parseInt(detail.getRoundVal()));
                if (yoycurvalm.contains("M")) {
                    yoycurvalm = yoycurvalm.replace("M", "");
                    formatVal = "Mn";
                } else if (yoycurvalm.contains("L")) {
                    yoycurvalm = yoycurvalm.replace("L", "");
                    formatVal = "Lkh";
                } else if (yoycurvalm.contains("C")) {
                    yoycurvalm = yoycurvalm.replace("C", "");
                    formatVal = "Crs";
                } else if (yoycurvalm.contains("K")) {
                    yoycurvalm = yoycurvalm.replace("K", "");
                    formatVal = "K";
                }
                //
                if (yoycurvalm != null && Float.parseFloat(yoycurvalm) == 0) {
                    kpiheadsbuilder.append("<td width='140%'><table style='margin-left: 10px;'><tr><td class='myDesignClass'>YOY</td></tr></table><div class='ui-tabs ui-widget ui-widget-content ui-corner-all' style='border-top:blank; border-bottom:medium hidden; border-left:medium hidden; border-right:medium hidden;border-color:LightGrey;width:100% ; height:100% ;margin-left: 10px; margin-right: 10px;'><table><tr><td style='font-size:14pt;color:#369;'>--</td></tr></table></div></td>");
                } else {
                    kpiheadsbuilder.append("<td width='140%'><table style='margin-left: 10px;'><tr><td class='myDesignClass'>YOY</td></tr></table><div class='ui-tabs ui-widget ui-widget-content ui-corner-all' style='border-top:blank; border-bottom:medium hidden; border-left:medium hidden; border-right:medium hidden;border-color:LightGrey;width:100% ; height:100% ;margin-left: 10px; margin-right: 10px;'><table><tr><td style='font-size:14pt;color:#369;'>" + yoycurvalm + " <sub>" + formatVal + "</sub></td></tr></table></div></td>");
                }
            } else {
                kpiheadsbuilder.append("<td width='140%'><table style='margin-left: 10px;'><tr><td class='myDesignClass'>YOY</td></tr></table><div class='ui-tabs ui-widget ui-widget-content ui-corner-all' style='border-top:blank; border-bottom:medium hidden; border-left:medium hidden; border-right:medium hidden;border-color:LightGrey;width:100% ; height:100% ;margin-left: 10px; margin-right: 10px;'><table><tr><td style='font-size:14pt'>--</td></tr></table></div></td>");
            }

            //for change%(YOY)
            if (!multiPeriodKPI.getYearObject().getFieldValueString(0, 4).equalsIgnoreCase("")) {
                double yoyc = Double.parseDouble(multiPeriodKPI.getYearObject().getFieldValueString(0, 4));
                int decimalPlaces = 0;
                BigDecimal yoyccurval = new BigDecimal(yoyc);
                yoyccurval = yoyccurval.setScale(decimalPlaces, BigDecimal.ROUND_HALF_UP);
                String roundyoy = detail.getRoundVal();
                if (roundyoy != null && Integer.parseInt(roundyoy) == 0) {
                    roundyoy = "1";
                }
                String yoyccurvalm = NumberFormatter.getModifiedNumber(yoyccurval, "", Integer.parseInt(roundyoy));
                kpiheadsbuilder.append("<td width='140%'><table style='margin-left: 10px;'><tr><td class='myDesignClass' style='white-space:nowrap;'>Change% (YOY)</td></tr></table><div class='ui-tabs ui-widget ui-widget-content ui-corner-all' style='border-top:blank; border-bottom:medium hidden; border-left:medium hidden; border-right:medium hidden;border-color:LightGrey;width:100% ; height:100% ;margin-left: 10px; margin-right: 10px;'><table><tr><td style='font-size:14pt;color:" + yearfontColor + "'>" + yoyccurvalm + "%</td><td colspan='1' align='right'><img width='20px' height='20px' src='" + yearIcon + "'></img></td></tr></table></div></td></tr></table>");
            } else {
                kpiheadsbuilder.append("<td width='140%'><table style='margin-left: 10px;'><tr><td class='myDesignClass' style='white-space:nowrap;'>Change% (YOY)</td></tr></table><div class='ui-tabs ui-widget ui-widget-content ui-corner-all' style='border-top:blank; border-bottom:medium hidden; border-left:medium hidden; border-right:medium hidden;border-color:LightGrey;width:100% ; height:100% ;margin-left: 10px; margin-right: 10px;'><table><tr><td style='font-size:14pt'>--</td></tr></table></div></td></tr></table>");
            }

            kpiheadsbuilder.append("<center><table><tr><td id='loadingTd' style='display:none;'><div><img id='loadImg1' src='" + request.getContextPath() + "/images/ajax-loader.gif' align='center'  width='30px' height='30px'  style='position:absolute;' /></div></td></tr></table></center>");

            //empty td
            //kpiheadsbuilder.append("<td width='140%'><table style='margin-left: 10px;'><tr><td class='myDesignClass'></td></tr></table><div class='ui-tabs ui-widget ui-widget-content ui-corner-all' style='border-top:medium hidden; border-bottom:medium hidden; border-left:medium hidden; border-right:medium hidden;border-color:LightGrey;width:100% ; height:100% ;margin-left: 10px; margin-right: 10px;'><table><tr><td style='font-size:14pt'></td></tr></table></div></td></tr></table>");
        } else {
        }

        PrintWriter out = response.getWriter();
        out.print(kpiheadsbuilder.toString());

        return null;
    }

    public ActionForward getRelatedMeasures(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(false);
        String measId = request.getParameter("measId");
        String oneViewId = request.getParameter("oneViewId");
        String regionId = request.getParameter("regionId");
        String roleId = request.getParameter("roleId");
        List<String> measureIdVal = new ArrayList<String>();
        ArrayList QueryCols = new ArrayList();
        ArrayList QueryAggs = new ArrayList();
        DashboardViewerDAO dao = new DashboardViewerDAO();
        String userId = String.valueOf(session.getAttribute("USERID"));
        PbReturnObject pbretObjForTime = null;
        StringBuilder kpiheadsbuilder = new StringBuilder();

        String advHtmlFileProps = (String) request.getSession(false).getAttribute("advHtmlFileProps");
        OneViewLetDetails detail = new OneViewLetDetails();
        OnceViewContainer onecontainer = new OnceViewContainer();
        List<String> tiemdeatilsArray = null;

        ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();
//                onecontainer = reportTemplateDAO.getOneViewData(oneviewId);

        // String fileName = reportTemplateDAO.getOneviewFileName(oneViewId);
        String fileName = request.getSession(false).getAttribute("tempFileName").toString();
        FileInputStream fis2 = new FileInputStream(advHtmlFileProps + "/" + fileName);
        ObjectInputStream ois2 = new ObjectInputStream(fis2);
        onecontainer = (OnceViewContainer) ois2.readObject();
        ois2.close();

        detail = onecontainer.onviewLetdetails.get(Integer.parseInt(regionId));
        if (onecontainer.timedetails != null && !onecontainer.timedetails.isEmpty()) {
            tiemdeatilsArray = onecontainer.timedetails;
        }
        ArrayList alist = new ArrayList();
        alist = (ArrayList) tiemdeatilsArray;
        if (measId != null && !measId.equalsIgnoreCase("")) {
            DashboardViewerDAO ddao = new DashboardViewerDAO();
            ArrayList relatedMeasReult = ddao.getRelatedMeasures(measId);
            if (relatedMeasReult != null && !relatedMeasReult.isEmpty()) {
                String measIds = (String) relatedMeasReult.get(0);
                String measNames = (String) relatedMeasReult.get(1);
                String refTypes = (String) relatedMeasReult.get(2);
                String[] relatedMeasReultArr = measIds.split(",");
                String[] relatedMeasNamesArr = measNames.split(",");
                String[] refTypesArr = refTypes.split(",");
                for (int i = 0; i < relatedMeasReultArr.length; i++) {
                    measureIdVal.add(relatedMeasReultArr[i]);
                }

                List<KPIElement> kpiElements = dao.getKPIElements(measureIdVal, new HashMap<String, String>());
                if (kpiElements != null) {
                    for (KPIElement elem : kpiElements) {
                        if (elem.getElementName() != null) {
                            QueryCols.add(elem.getElementId());
                        }
                        QueryAggs.add(elem.getAggregationType());
                    }
                }
                PbReportQuery timequery = new PbReportQuery();
                timequery.setRowViewbyCols(new ArrayList());
                timequery.setColViewbyCols(new ArrayList());
                timequery.setColViewbyCols(new ArrayList());
                timequery.setQryColumns(QueryCols);
                timequery.setColAggration(QueryAggs);
                timequery.setTimeDetails(alist);
                timequery.setDefaultMeasure(String.valueOf(QueryCols.get(0)));
                timequery.setDefaultMeasureSumm(String.valueOf(QueryAggs.get(0)));
                timequery.isKpi = true;
                timequery.setBizRoles(roleId);
                timequery.setUserId(userId);

                pbretObjForTime = timequery.getPbReturnObject(String.valueOf(QueryCols.get(0)));
                if (pbretObjForTime != null) {
                    NumberFormat formatter = NumberFormat.getInstance(new Locale("en_US"));
                    ArrayList valArray = new ArrayList();
                    for (int j = 0; j < measureIdVal.size(); j++) {
                        double currVal = 0.0;
                        if (!pbretObjForTime.getFieldValueString(0, ("A_" + measureIdVal.get(j)).toString()).equalsIgnoreCase("")) {
                            currVal = Double.parseDouble((pbretObjForTime.getFieldValueString(0, ("A_" + measureIdVal.get(j)).toString())));
                        }
                        int decimalPlaces = 0;
                        BigDecimal curval = new BigDecimal(currVal);
                        BigDecimal val = curval;
                        curval = curval.setScale(decimalPlaces, BigDecimal.ROUND_HALF_UP);
                        valArray.add(curval);
                    }
                    int j1 = 0;
                    int j2 = 0;
                    int i = 0;
                    for (i = 0; i < measureIdVal.size(); i++) {
                        j2 = i + 3;
                        kpiheadsbuilder.append("<table style='table-layout:fixed; width:100%; border-spacing: 15px;'><tr>");
                        for (j1 = i; j1 < j2; j1++) {
                            if (valArray.size() > j1) {
                                BigDecimal maxBigDecimal = new BigDecimal(Integer.parseInt(valArray.get(j1).toString().replaceAll(",", "")));
                                String formatVal = "";
                                String maxValm = NumberFormatter.getModifiedNumber(maxBigDecimal, detail.getFormatVal(), Integer.parseInt(detail.getRoundVal()));
                                if (maxValm.contains("M")) {
                                    maxValm = maxValm.replace("M", "");
                                    formatVal = "Mn";
                                } else if (maxValm.contains("L")) {
                                    maxValm = maxValm.replace("L", "");
                                    formatVal = "Lkh";
                                } else if (maxValm.contains("C")) {
                                    maxValm = maxValm.replace("C", "");
                                    formatVal = "Crs";
                                } else if (maxValm.contains("K")) {
                                    maxValm = maxValm.replace("K", "");
                                    formatVal = "K";
                                }
                                if (!refTypesArr[j1].equalsIgnoreCase("") && Integer.parseInt(refTypesArr[j1]) != 4) {
                                    kpiheadsbuilder.append("<td width='140%'><table style='margin-left: 10px;'><tr><td class='myDesignClass' style='white-space:nowrap;'>" + relatedMeasNamesArr[j1] + "</td></tr></table><div class='ui-tabs ui-widget ui-widget-content ui-corner-all' style='border-top:blank; border-bottom:medium hidden; border-left:medium hidden; border-right:medium hidden;border-color:LightGrey;width:100% ; height:100% ;margin-left: 10px; margin-right: 10px;'><table><tr><td style='font-size:14pt;color:#369;'>" + maxValm + "<sub>" + formatVal + "</sub></td></tr></table></div></td>");
                                } else {
                                    kpiheadsbuilder.append("<td width='140%'><table style='margin-left: 10px;'><tr><td class='myDesignClass' style='white-space:nowrap;'>" + relatedMeasNamesArr[j1] + "</td></tr></table><div class='ui-tabs ui-widget ui-widget-content ui-corner-all' style='border-top:blank; border-bottom:medium hidden; border-left:medium hidden; border-right:medium hidden;border-color:LightGrey;width:100% ; height:100% ;margin-left: 10px; margin-right: 10px;'><table><tr><td style='font-size:14pt;color:#369;'>" + maxValm + "%</td></tr></table></div></td>");
                                }
                            } else {
                                kpiheadsbuilder.append("<td width='140%'><table style='margin-left: 10px;'><tr><td class='myDesignClass' style='white-space:nowrap;'></td></tr></table><div class='ui-tabs ui-widget ui-widget-content ui-corner-all' style='border-top:medium hidden; border-bottom:medium hidden; border-left:medium hidden; border-right:medium hidden;border-color:LightGrey;width:100% ; height:100% ;margin-left: 10px; margin-right: 10px;'><table><tr><td style='font-size:14pt;color:#369;'></td></tr></table></div></td>");
                            }
                        }
                        i = j1 - 1;
                        kpiheadsbuilder.append("</tr></table></div></td>");
                    }
                }
            }
        }
        PrintWriter out = response.getWriter();
        out.print(kpiheadsbuilder.toString());
        return null;
    }

    public ActionForward getOneViewAssignment(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(false);
        String useId = String.valueOf(session.getAttribute("USERID"));
        String oneviewId = request.getParameter("oneviewId");
        String onviewName = request.getParameter("onviewName");
        String roleIds = request.getParameter("roleIds");
        String olduserId = request.getParameter("olduserId");
        PrintWriter out = response.getWriter();
        String[] count = roleIds.split(",");
        ReportTemplateDAO dao = new ReportTemplateDAO();
        ArrayList userIds = dao.getOneViewAssignment(roleIds, count.length);
        ArrayList droppedList = dao.getAssignedUsersforView(oneviewId);
        ArrayList droppedListIds = dao.getAssignedUsersforViewIds(oneviewId);
        String username = "";
        String userId = "";
        String[] usernames = null;
        String[] userIdsArr = null;
        if (!userIds.isEmpty()) {
            username = (String) userIds.get(1);
            userId = (String) userIds.get(0);
            usernames = username.split(",");
            userIdsArr = userId.split(",");
            ArrayList<String> dragableArrayList = new ArrayList<String>();
            ArrayList<String> userNamesList = new ArrayList<String>();
            if (userIdsArr != null && usernames != null) {
                dragableArrayList.addAll(Arrays.asList(userIdsArr));
                userNamesList.addAll(Arrays.asList(usernames));
            }
            GenerateDragAndDrophtml dragAndDrophtml = new GenerateDragAndDrophtml("Drag KPI from here", "Drop KPI here", droppedListIds, dragableArrayList, request.getContextPath());
            dragAndDrophtml.setDragableListNames(userNamesList);
            dragAndDrophtml.setDropedmesNames(droppedList);

            String htmlJson = dragAndDrophtml.getDragAndDropDiv();
            out.print(htmlJson);
        } else {
            out.print("");
        }
        return null;
    }

    public ActionForward assignOneView(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String oneviewId = request.getParameter("oneviewId");
        String onviewName = request.getParameter("onviewName");
        String userIdArray = request.getParameter("userIdArray");
        String fileName = request.getParameter("fileName");
        HttpSession session = request.getSession(false);
        String filePath = (String) session.getAttribute("filePath");
        //String oneviewRegionFileName = "OneviewRegionDetails"+oneviewId+"_Analyzer_"+System.currentTimeMillis()+".txt";

        String[] userIds = userIdArray.split(",");
        ReportTemplateDAO dao = new ReportTemplateDAO();
        String result = "";
        //saveOneViewReg
        dao.deletePrevioueUsers(oneviewId);
        for (int i = 0; i < userIds.length; i++) {
            com.progen.users.UserLayerDAO udao = new com.progen.users.UserLayerDAO();
            String userType = udao.getUserTypeForFeatures(Integer.parseInt(userIds[i]));
            if (!userType.equalsIgnoreCase("ANALYZER")) {
                fileName = null;
            } else {
                fileName = request.getParameter("fileName");
            }
            result = dao.assignOneView(oneviewId, userIds[i], fileName, filePath);
        }
        PrintWriter out = response.getWriter();
        out.print(result);
        return null;
    }

    public ActionForward getRegDate(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException, ClassNotFoundException, Exception {
        HttpSession session = request.getSession(false);
        StringBuilder dateString = new StringBuilder();
        String oneviewId = request.getParameter("oneViewIdValue");
        String regDate = request.getParameter("regDate");
        String type = request.getParameter("type");
        String regId = request.getParameter("regId");
        int width = Integer.parseInt(request.getParameter("width"));
        int height = Integer.parseInt(request.getParameter("height"));
        String advHtmlFileProps = (String) request.getSession(false).getAttribute("advHtmlFileProps");
        DashboardTemplateBD dashboardTemplateBD = new DashboardTemplateBD();
        OnceViewContainer onecontainer = new OnceViewContainer();
        OneViewLetDetails detail = null;
        OneViewBD oneViewBD = new OneViewBD();


        // ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();
//              onecontainer = reportTemplateDAO.getOneViewData(oneViewIdValue);
        //  String fileName = reportTemplateDAO.getOneviewFileName(oneViewIdValue);
        String fileName = session.getAttribute("tempFileName").toString();
        FileInputStream fis2 = new FileInputStream(advHtmlFileProps + "/" + fileName);
        ObjectInputStream ois2 = new ObjectInputStream(fis2);
        onecontainer = (OnceViewContainer) ois2.readObject();
        ois2.close();

        detail = onecontainer.onviewLetdetails.get(Integer.parseInt(regId));
        detail.setRepName(type);
        detail.setReptype(type);
        detail.setWidth(width);
        detail.setHeight(height);
        detail.setRegDate(regDate);

        String measueId = detail.getRepId();
        detail.setPrevMeasId(measueId);


        String result = dashboardTemplateBD.getOneviewRegDate(request, response, session, onecontainer, detail, regDate);
        oneViewBD.writeOneviewRegData(onecontainer, result, detail.getNoOfViewLets(), request);
        FileOutputStream fos = new FileOutputStream(advHtmlFileProps + "/" + fileName);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(onecontainer);
        oos.flush();
        oos.close();

        response.getWriter().print(result);
        return null;
    }

    public ActionForward getOneViewSequence(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(false);
        String useId = String.valueOf(session.getAttribute("USERID"));
        ReportTemplateDAO dao = new ReportTemplateDAO();
        PbReturnObject retOjb = null;
        PbReturnObject retOjb1 = null;
        PbReturnObject retOjb2 = null;
        PbReturnObject retOjb3 = null;
        retOjb = dao.getAllOneViewBys(useId);
        retOjb1 = dao.getOneViewsBasedOnSequence(useId);
        if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.MYSQL)) {
            retOjb2 = dao.getOneViewsBasedOnSequence1(useId);
            retOjb3 = dao.getOneViewsBasedOnSequencemysql(useId);
        }
        ArrayList<String> viewByNames = new ArrayList<String>();
        ArrayList<String> viewByIds = new ArrayList<String>();

        ArrayList<String> viewByNamesseq = null;
        ArrayList<String> viewByIdsseq = null;
        for (int i = 0; i < retOjb.rowCount; i++) {
            viewByIds.add(Integer.toString(retOjb.getFieldValueInt(i, "ONEVIEWID")));
            viewByNames.add(retOjb.getFieldValueString(i, "ONEVIEW_NAME"));
        }
        if (retOjb1.getRowCount() > 0) {
            viewByNamesseq = new ArrayList<String>();
            viewByIdsseq = new ArrayList<String>();
            for (int i = 0; i < retOjb1.rowCount; i++) {
                viewByIdsseq.add(Integer.toString(retOjb1.getFieldValueInt(i, "ONEVIEWID")));
                viewByNamesseq.add(retOjb1.getFieldValueString(i, "ONEVIEW_NAME"));
            }
        } else {
            if (retOjb2 != null && retOjb2.getRowCount() > 0) {
                viewByNamesseq = new ArrayList<String>();
                viewByIdsseq = new ArrayList<String>();
                for (int j = 0; j < retOjb3.rowCount; j++) {
                    for (int i = 0; i < retOjb2.rowCount; i++) {
                        String oneid = Integer.toString(retOjb3.getFieldValueInt(j, 0));
                        String oneid1 = Integer.toString(retOjb2.getFieldValueInt(i, "ONEVIEWID"));
                        if (oneid == null ? oneid1 == null : oneid.equals(oneid1)) {
                            viewByIdsseq.add(Integer.toString(retOjb2.getFieldValueInt(i, "ONEVIEWID")));
                            viewByNamesseq.add(retOjb2.getFieldValueString(i, "ONEVIEW_NAME"));
                        }
                    }
                }
            }
        }
        GenerateDragAndDrophtml dragAndDrophtml = new GenerateDragAndDrophtml("Drag KPI from here", "Drop KPI here", viewByIdsseq, viewByIds, request.getContextPath());
        dragAndDrophtml.setDragableListNames(viewByNames);
        if (retOjb1.getRowCount() > 0) {
            dragAndDrophtml.setDropedmesNames(viewByNamesseq);
        }
        if (retOjb2 != null && retOjb2.getRowCount() > 0) {
            dragAndDrophtml.setDropedmesNames(viewByNamesseq);
        }

        String htmlJson = dragAndDrophtml.getDragAndDropDiv();
        PrintWriter out = response.getWriter();
        out.print(htmlJson);
        return null;
    }

    public ActionForward saveViewSequence(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(false);
        String userId = (String) session.getAttribute("USERID");
        String userIdArray = request.getParameter("userIdArray");
        String[] idArray = userIdArray.split(",");
        ReportTemplateDAO dao = new ReportTemplateDAO();
        dao.updateTabSequences(userId);
        boolean tabDetail = dao.saveOneViewSequence(idArray, userId);
        PrintWriter out = response.getWriter();
        out.print(tabDetail);
        return null;
    }

    public ActionForward getComplexKPIGraphinoneview(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(false);
        String measId = request.getParameter("measId");
        //String measName = request.getParameter("measName");
        String oneViewId = request.getParameter("oneViewId");
        String roleId = request.getParameter("roleId");
        String viewLetId = request.getParameter("viewLetId");
        StringBuilder sb = new StringBuilder();
        ArrayList dataSeq = null;
        DashboardViewerDAO dao = new DashboardViewerDAO();
        PbReportTableBD reportTableBD = new PbReportTableBD();
        PbReturnObject pbretObjForTime = new PbReturnObject();
        CreateKPIFromReport kPIFromReport = new CreateKPIFromReport();
        String imgdata = "";
        String formatType = "";
        String formatValue = "";
        List<ProgenDataSet> retObjList = new ArrayList<ProgenDataSet>();
        UserLayerDAO userLayerDAO = new UserLayerDAO();
        String advHtmlFileProps = (String) request.getSession(false).getAttribute("advHtmlFileProps");

        ArrayList rowview = new ArrayList();
        rowview.add("TIME");
        PbReportQuery timequery = new PbReportQuery();
        ArrayList QueryCols = new ArrayList();
        ArrayList QueryAggs = new ArrayList();
        List<String> measureIdVal = new ArrayList<String>();
        ArrayList measureIdVal1 = new ArrayList();
        measureIdVal.add(measId);


        String userId = String.valueOf(session.getAttribute("USERID"));
        OnceViewContainer onecontainer1 = null;
        String fileName = null;
        PbReturnObject retObj = null;
        ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();
        retObj = reportTemplateDAO.testForExisting();
        ArrayList<String> va = new ArrayList<String>();
        OneViewLetDetails detail = null;
        for (int i = 0; i < retObj.getRowCount(); i++) {
            va.add(retObj.getFieldValueString(i, "ONEVIEWID"));
        }
        if (!va.isEmpty() && va.contains(oneViewId)) {
            retObj = reportTemplateDAO.getOneviewFileNam(oneViewId);
            fileName = request.getSession(false).getAttribute("tempFileName").toString();
            FileInputStream fis = new FileInputStream(advHtmlFileProps + "/" + fileName);
            ObjectInputStream ois = new ObjectInputStream(fis);
            onecontainer1 = (OnceViewContainer) ois.readObject();
            ois.close();
        } else {
            fileName = request.getSession(false).getAttribute("tempFileName").toString();
            FileInputStream fis = new FileInputStream(advHtmlFileProps + "/" + fileName);
            ObjectInputStream ois = new ObjectInputStream(fis);
            onecontainer1 = (OnceViewContainer) ois.readObject();
            ois.close();
        }
        //
        detail = onecontainer1.onviewLetdetails.get(Integer.parseInt(viewLetId));
        kPIFromReport = detail.getKpiFromReport();
        ArrayList arl = new ArrayList();
        String priorId = userLayerDAO.getPriorMeasure(kPIFromReport.getMeasureId().replace("A_", ""));
//        for(int i=0;i<detail.getComplexrowviewbys().size();i++){
//            measureIdVal1.add(detail.getComplexrowviewbys().get(i));
//        }
        if (priorId != "") {
            measureIdVal1.add(kPIFromReport.getMeasureId().replace("A_", ""));
            List<KPIElement> kpiElements = dao.getKPIElements(measureIdVal1, new HashMap<String, String>());
            if (kpiElements != null) {
                for (KPIElement elem : kpiElements) {
                    if (elem.getElementName() != null) {
                        QueryCols.add(elem.getElementId());
                    }
                    QueryAggs.add(elem.getAggregationType());
                }
            }
            if (QueryCols != null && !QueryCols.isEmpty()) {
                for (int i = 0; i < QueryCols.size(); i++) {
                    if (priorId.equalsIgnoreCase(QueryCols.get(i).toString())) {
                        detail.getComplexreportQryCols().add(QueryCols.get(i));
                        detail.getComplexqryAggregations().add(QueryAggs.get(i));
                    }
                }
            }
        }
        if (onecontainer1 != null && !onecontainer1.timedetails.isEmpty()) {
            arl = (ArrayList) onecontainer1.timedetails;
        } else {
            arl.add("Day");
            arl.add("PRG_STD");
            ProgenParam pramnam = new ProgenParam();
            String date = pramnam.getdateforpage();
            arl.add(date);
            arl.add("Month");
            arl.add("Last Period");
        }
        String prefixValue = "";
        String suffixValue = "";
        if (detail.getPrefixValue() != null) {
            prefixValue = detail.getPrefixValue();
        }
        if (detail.getSuffixValue() != null) {
            suffixValue = detail.getSuffixValue();
        }
        if (detail.getSuffixValue() != null) {
            if (detail.getSuffixValue().equalsIgnoreCase("K")) {
                suffixValue = "K";
            } else if (detail.getSuffixValue().equalsIgnoreCase("L")) {
                suffixValue = "Lkh";
            } else if (detail.getSuffixValue().equalsIgnoreCase("M")) {
                suffixValue = "Mn";
            } else if (detail.getSuffixValue().equalsIgnoreCase("Cr")) {
                suffixValue = "Crs";
            }
        }
        if (detail.getFormatVal() != null && detail.getFormatVal().equalsIgnoreCase("K")) {
            formatType = "K";
            formatValue = "K";
        }
        if (detail.getFormatVal() != null && detail.getFormatVal().equalsIgnoreCase("M")) {
            formatType = "M";
            formatValue = "Mn";
        }
        if (detail.getFormatVal() != null && detail.getFormatVal().equalsIgnoreCase("L")) {
            formatType = "L";
            formatValue = "Lkh";
        }
        if (detail.getFormatVal() != null && detail.getFormatVal().equalsIgnoreCase("Cr")) {
            formatType = "Cr";
            formatValue = "Crs";
        }
        timequery.setRowViewbyCols(detail.getComplexrowviewbys());
        timequery.setColViewbyCols(new ArrayList());
        timequery.setParamValue((HashMap) kPIFromReport.getReportParametersValues());
        timequery.setQryColumns(detail.getComplexreportQryCols());
        timequery.setColAggration(detail.getComplexqryAggregations());
        timequery.setTimeDetails(arl);
//        timequery.setDefaultMeasure(String.valueOf(detail.getComplexreportQryCols().get(0)));
//        timequery.setDefaultMeasureSumm(String.valueOf(detail.getComplexqryAggregations().get(0)));
//        timequery.isTimeSeries = false;
//                repQuery.setReportId(collect.reportId);
        timequery.setBizRoles(roleId);
        timequery.setUserId(userId);
        pbretObjForTime = timequery.getPbReturnObject(String.valueOf(detail.getComplexreportQryCols().get(0)));
        pbretObjForTime.resetViewSequence();
        ArrayList sortColumns = null;
        if (kPIFromReport.getSortColumns() != null) {
            sortColumns = new ArrayList(Arrays.asList(kPIFromReport.getSortColumns()));
        }

        reportTableBD.searchDataSet(kPIFromReport, pbretObjForTime);
        if (kPIFromReport.isTopBottomSet()) {
            if (kPIFromReport.getTopBtmMode().equalsIgnoreCase("TopBottomAbsRows")) {
                dataSeq = pbretObjForTime.findTopBottom(sortColumns, kPIFromReport.getSortTypes(), kPIFromReport.getTopBtmCount());
            } else {
                dataSeq = pbretObjForTime.findTopBottomPercentWise(sortColumns, kPIFromReport.getSortTypes(), kPIFromReport.getTopBtmCount());
            }
            pbretObjForTime.resetViewSequence();
            pbretObjForTime.setViewSequence(dataSeq);
        }
        // RTMeasureElement.RANK;
        retObjList.add(pbretObjForTime);
        StringBuilder imgtest = new StringBuilder();
        ArrayList alist1 = new ArrayList();
        ArrayList alist2 = new ArrayList();
        ArrayList alist3 = new ArrayList();
        ArrayList alist4 = new ArrayList();
        alist1 = pbretObjForTime.retrieveDataBasedOnViewSeq(kPIFromReport.getMeasureId());
        if (priorId != "") {
            alist3 = pbretObjForTime.retrieveDataBasedOnViewSeq("A_" + priorId);
        }
        if (formatType != null && formatValue != null && detail != null && detail.getFormatVal() != null && detail.getRoundVal() != null && !detail.getRoundVal().equalsIgnoreCase("")) {
            for (int i = 0; i < alist1.size(); i++) {
                String value1 = NumberFormatter.getModifiedNumber(new BigDecimal(alist1.get(i).toString()), detail.getFormatVal(), Integer.parseInt(detail.getRoundVal()));
                alist2.add(value1);
            }
        } else {
            for (int i = 0; i < alist1.size(); i++) {
                String value1 = NumberFormatter.getModifiedNumber(new BigDecimal(alist1.get(i).toString()), "", 0);
                alist2.add(value1);
            }
        }
        RunTimeMeasure rtm = new RunTimeMeasure(alist1);
        ArrayList<BigDecimal> runTimeMeasData = null;
        pbretObjForTime.setRuntimeMeasure(kPIFromReport.getMeasureId(), alist1);
        runTimeMeasData = StatUtil.STAT_HELPER.Rank(pbretObjForTime.retrieveDataBasedOnViewSeq(kPIFromReport.getMeasureId()));
        RunTimeMeasure rtMeasure = new RunTimeMeasure(runTimeMeasData);

        RunTimeMeasure rtm1 = new RunTimeMeasure(alist3);
        ArrayList<BigDecimal> runTimeMeasData1 = null;
        pbretObjForTime.setRuntimeMeasure("A_" + priorId, alist3);
        runTimeMeasData1 = StatUtil.STAT_HELPER.Rank(pbretObjForTime.retrieveDataBasedOnViewSeq("A_" + priorId));
        String color = "";
        for (int k = 0; k < runTimeMeasData1.size(); k++) {
            if (runTimeMeasData1.get(k).compareTo(runTimeMeasData.get(k)) > 0) {
                color = "green";
            } else if (runTimeMeasData1.get(k).compareTo(runTimeMeasData.get(k)) < 0) {
                color = "red";
            } else {
                color = "";
            }
            alist4.add(color);
        }
//                pbretObjForTime.setRunTimeMeasureData(kPIFromReport.getMeasureId(), null);
        //
        imgtest.append("<table style='width:90%;height:90%'><tr><td>");
        imgtest.append("<table style='height:10%;'><tr><td style='width:20%;'></td><td align='right' style='width:1%;white-space:nowrap;'><input type='radio' name='compType' id='rankCheck' onclick=\"selectRanksOrVals(" + measId + "," + oneViewId + ",'" + roleId + "'," + viewLetId + ",'')\" checked>Rank Basis </td><td align='right' style='white-space:nowrap;'><input type='radio' id='valCheck' onclick=\"selectRanksOrVals(" + measId + "," + oneViewId + ",'" + roleId + "'," + viewLetId + ",'')\" name='compType'> Period Basis</td></tr></table><table id='firstTD1' style='height:90%'>");
        if (prefixValue == null) {
            prefixValue = "";
        }
        if (suffixValue == null) {
            suffixValue = "";
        }
        for (int i = 0; i < alist2.size(); i++) {
            // 
//                    String tooltipval = "<table><tr></tr><tr><td>Rank</td></tr><tr><td>Value</td></tr></table>";
//                    tooltipval+="<table><tr><td>Current:</td></tr><tr><td>"+runTimeMeasData.get(i)+"</td></tr><tr><td>"+alist1.get(i) +"</td></tr></table>";
//                    tooltipval+="<table><tr><td>Prior:</td></tr><tr><td>"+runTimeMeasData1.get(i) +"</td></tr><tr><td>"+alist3.get(i) +"</td></tr></table>";
            String tooltipval = "Current Rank:" + runTimeMeasData.get(i) + ", Prior Rank:" + runTimeMeasData1.get(i) + "";
            if (alist4 != null && !alist4.isEmpty() && String.valueOf(alist4.get(i)).equalsIgnoreCase("green")) {
                imgtest.append("<tr><td class='myhead' style='color:#369;font-size:11pt;background-color:white;width:100%;'>" + pbretObjForTime.getFieldValueStringBasedOnViewSeq(i, "A_" + detail.getComplexrowviewbys().get(0)) + "</td><td width='2%'></td><td style='color:#369;font-size:12pt'>" + prefixValue + alist2.get(i).toString().replace(formatType, "") + "<span style='font-size:7pt;'>" + suffixValue + "</span></td><td><img id='tdImage'  title='" + tooltipval + "' src='" + request.getContextPath() + "/stylesheets/images/positive.GIF'/></td></tr>");
            } else if (alist4 != null && !alist4.isEmpty() && String.valueOf(alist4.get(i)).equalsIgnoreCase("red")) {
                imgtest.append("<tr><td class='myhead' style='color:#369;font-size:11pt;background-color:white;width:100%;'>" + pbretObjForTime.getFieldValueStringBasedOnViewSeq(i, "A_" + detail.getComplexrowviewbys().get(0)) + "</td><td width='2%'></td><td style='color:#369;font-size:12pt'>" + prefixValue + alist2.get(i).toString().replace(formatType, "") + "<span style='font-size:7pt;'>" + suffixValue + "</span></td><td><img id='tdImage' title='" + tooltipval + "' src='" + request.getContextPath() + "/stylesheets/images/negative.gif'/></td></tr>");
            } else {
                imgtest.append("<tr><td class='myhead' style='color:#369;font-size:11pt;background-color:white;width:100%;'>" + pbretObjForTime.getFieldValueStringBasedOnViewSeq(i, "A_" + detail.getComplexrowviewbys().get(0)) + "</td><td width='2%'></td><td style='color:#369;font-size:12pt'>" + prefixValue + alist2.get(i).toString().replace(formatType, "") + "<span style='font-size:7pt;'>" + suffixValue + "</span></td></tr>");
            }
            imgtest.append("<tr></tr><tr></tr>");
        }
        imgtest.append("</table></td><td width='50' valign='top' align='center'></td><td style='border-left: 1px solid LightGrey;'></td><td width='30'></td><td width='40%' valign='top'><table><tr><td style='white-space:nowrap;'><a href='javascript:void(0)' onclick=\"getDataOnComparisonClick(" + measId + "," + oneViewId + ",'" + roleId + "'," + viewLetId + ",'lastmonth')\">Last Month</a></td><td width='15%'></td><td><a href='javascript:void(0)' onclick=\"getDataOnComparisonClick(" + measId + "," + oneViewId + ",'" + roleId + "'," + viewLetId + ",'qtr')\">Qtr</a></td><td width='15%'></td><td><a href='javascript:void(0)' onclick=\"getDataOnComparisonClick(" + measId + "," + oneViewId + ",'" + roleId + "'," + viewLetId + ",'year')\">Year</a></td></tr></table><table style='height:100%;width:100%;padding-bottom:7%;'><tr><td valign='center' id='compareTD'></td></tr></table></td></tr>");
        imgtest.append("</table>");


        PrintWriter out = response.getWriter();
        out.print(imgtest.toString());
        return null;
    }

    public ActionForward viewerRegions(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        //String dashlets=detail.getNoO
        HttpSession session = request.getSession(false);
        String oneViewId = request.getParameter("oneViewId");
        String advHtmlFileProps = (String) request.getSession(false).getAttribute("advHtmlFileProps");
        OneViewLetDetails detail = new OneViewLetDetails();
        OnceViewContainer onecontainer = new OnceViewContainer();
        ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();


        String fileName = session.getAttribute("tempFileName").toString();
        FileInputStream fis2 = new FileInputStream(advHtmlFileProps + "/" + fileName);
        ObjectInputStream ois2 = new ObjectInputStream(fis2);
        onecontainer = (OnceViewContainer) ois2.readObject();
        ois2.close();
        int dashlets = onecontainer.onviewLetdetails.size();
        String height = onecontainer.height;
        if (onecontainer.height == null) {
            height = "80";
        }
        int rws = onecontainer.getRows();
        StringBuilder F = new StringBuilder();
        F.append("{dashlets:\"").append(dashlets).append("\",height:\"").append(height).append("\",viewerfrom:\"viewer\",rws:\"").append(rws).append("\"}");
        response.getWriter().print(F);
        return null;
    }

    public ActionForward mergeOneViewColumns(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession(false);
        String oneViewIdValue = request.getParameter("oneViewIdValue");
        String dashletId = request.getParameter("dashlet");
        String colSpan = request.getParameter("colSpan");
//        String delDashletId=request.getParameter("delDashletId");
        String widthval = request.getParameter("widthval");
        String from = request.getParameter("from");
        Container container = null;
        OnceViewContainer onecontainer = null;
        File file = null;
        if (from != null && from.equalsIgnoreCase("viewer")) {
            String advHtmlFileProps = (String) request.getSession(false).getAttribute("advHtmlFileProps");

            ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();
            String fileName = reportTemplateDAO.getOneviewFileName(oneViewIdValue);
            file = new File(advHtmlFileProps + "/" + fileName);
            if (file.exists()) {
                FileInputStream fis2 = new FileInputStream(advHtmlFileProps + "/" + fileName);
                ObjectInputStream ois2 = new ObjectInputStream(fis2);
                onecontainer = (OnceViewContainer) ois2.readObject();
                ois2.close();
            } else {
                onecontainer = new OnceViewContainer();
                HashMap map = new HashMap();
                map = (HashMap) session.getAttribute("ONEVIEWDETAILS");
                if (map == null) {
                    advHtmlFileProps = (String) request.getSession(false).getAttribute("advHtmlFileProps");

                    fileName = session.getAttribute("tempFileName").toString();
                    FileInputStream fis2 = new FileInputStream(advHtmlFileProps + "/" + fileName);
                    ObjectInputStream ois2 = new ObjectInputStream(fis2);
                    onecontainer = (OnceViewContainer) ois2.readObject();
                    ois2.close();
                } else {
                    onecontainer = (OnceViewContainer) map.get(oneViewIdValue);
                }
            }
            List<OneViewLetDetails> dashletDetails = onecontainer.onviewLetdetails;
            for (int i = 0; i < dashletDetails.size(); i++) {
                OneViewLetDetails detail = dashletDetails.get(i);
                String dashlet = detail.getNoOfViewLets();
                if (dashlet.equals(dashletId)) {
                    detail.setColSpan(Integer.parseInt(colSpan));
                    detail.setWidth(Integer.parseInt(widthval));
                }
                //            if(dashlet.equals(delDashletId))
                //            {
                //                dashletDetails.remove(i);
                //            }
            }

            FileOutputStream fos = new FileOutputStream(advHtmlFileProps + "/" + fileName);
            ObjectOutputStream oosR = new ObjectOutputStream(fos);
            oosR.writeObject(onecontainer);
            oosR.flush();
            oosR.close();
            fos.close();
            //detail=(OneViewLetDetails) onecontainer.onviewLetdetails;
        } else {
            onecontainer = new OnceViewContainer();
            HashMap map = new HashMap();
            map = (HashMap) session.getAttribute("ONEVIEWDETAILS");
            onecontainer = (OnceViewContainer) map.get(oneViewIdValue);
            List<OneViewLetDetails> dashletDetails = onecontainer.onviewLetdetails;
            for (int i = 0; i < dashletDetails.size(); i++) {
                OneViewLetDetails detail = dashletDetails.get(i);
                String dashlet = detail.getNoOfViewLets();
                if (dashlet.equals(dashletId)) {
                    detail.setColSpan(Integer.parseInt(colSpan));
                    detail.setWidth(Integer.parseInt(widthval));
                }
//            if(dashlet.equals(delDashletId))
//            {
//                dashletDetails.remove(i);
//            }
            }
        }


        return null;
    }
//Surender

    public ActionForward getingOneviews(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession(false);
        String oneviewID = request.getParameter("oneViewIdValue");
        String date = request.getParameter("date");
        String daterange = request.getParameter("date1");
        String duration = request.getParameter("duration");
        String duration1 = request.getParameter("duration1");
        String compare = request.getParameter("compare");
        String currInnerWidth = request.getParameter("innerWidth");
        String action1 = request.getParameter("action1");
        String action = request.getParameter("action");
        String isdefult = request.getParameter("isdefult");

        request.setAttribute("homeFlag", false);
        request.setAttribute("oneViewId", 0);
        request.setAttribute("oneviewname", "");
        OnceViewContainer onecontainer = null;
        OnceViewContainer onecontainer1 = null;
        OneViewLetDetails oneviewLet = null;
        String regFileName = "";
        HashMap tempRegHashMap = new HashMap();
        List<String> tiemdeatilsArray = null;
        String advHtmlFileProps = (String) request.getSession(false).getAttribute("advHtmlFileProps");
        String oldAdvHtmlFileProps = (String) request.getSession(false).getAttribute("oldAdvHtmlFileProps");
        ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();
        OneViewBD oneViewBD = new OneViewBD();
        PbDb pbdb = new PbDb();
//        onecontainer1 = reportTemplateDAO.getOneViewData(oneviewID);
        PbReturnObject retObj = null;
        String GlobalfileName = null;
        String localfileName = null;
        String oneVersion = "";
        String tempdir = "";
        String comparefrom = "";
        String compareto = "";
        String regString = "";
        String oneviewtype = (String) session.getAttribute("oneviewdatetype");
        if (oneviewtype != null && oneviewtype.equalsIgnoreCase("flase")) {
            comparefrom = request.getParameter("comparefrom");
            compareto = request.getParameter("compareto");

        }
        retObj = reportTemplateDAO.testForExisting();
        ArrayList<String> va = new ArrayList<String>();
        for (int i = 0; i < retObj.getRowCount(); i++) {
            va.add(retObj.getFieldValueString(i, 0));
        }
        if (va.contains(oneviewID)) {
            retObj = reportTemplateDAO.getOneviewFileNam(oneviewID);
            if (retObj != null && retObj.rowCount > 0) {
                GlobalfileName = retObj.getFieldValueString(0, 1);
                oneVersion = retObj.getFieldValueString(0, 9);
            }


            File file = null;
            if (Float.parseFloat(oneVersion) < (float) 2.1) {
                file = new File(oldAdvHtmlFileProps + "/" + GlobalfileName);
                if (file.exists()) {
                    FileInputStream fis = new FileInputStream(oldAdvHtmlFileProps + "/" + GlobalfileName);
                    ObjectInputStream ois = new ObjectInputStream(fis);
                    onecontainer1 = (OnceViewContainer) ois.readObject();
                    ois.close();


                    /*
                     * belowe code is create the directory for oneview to plavce
                     * the inner files
                     */
                    String folderName = onecontainer1.oneviewName + "_" + onecontainer1.oneviewId + "_" + new SimpleDateFormat("mm-dd-yy").format(new Date());
                    String folderPath = oldAdvHtmlFileProps + "/" + folderName;//File.separator
                    File folderDir = new File(folderPath);
                    if (!folderDir.exists()) {
                        folderDir.mkdir();
                        session.removeAttribute("oldAdvHtmlFileProps");
                        session.setAttribute("oldAdvHtmlFileProps", folderPath);
                        oldAdvHtmlFileProps = folderPath;
                    }  /*
                     * dirctory created
                     */



                    oneViewBD.insertOneviewRegData(onecontainer1, oneviewID, request);
                    File file1 = new File(oldAdvHtmlFileProps + "/" + GlobalfileName);
                    if (file.exists()) {
                        FileInputStream fis2 = new FileInputStream(oldAdvHtmlFileProps + "/" + GlobalfileName);
                        ObjectInputStream ois2 = new ObjectInputStream(fis2);
                        onecontainer1 = (OnceViewContainer) ois2.readObject();
                        ois2.close();
                    }

                    tempdir = System.getProperty("java.io.tmpdir");
                    session.setAttribute("advHtmlFileProps", tempdir);
                    localfileName = "OneviewDetails" + oneviewID + "_" + session.getId() + "_" + System.currentTimeMillis() + ".txt";
                    session.setAttribute("tempFileName", localfileName);
                    onecontainer1.setContainerFileName(localfileName);

                    for (int i = 0; i < onecontainer1.onviewLetdetails.size(); i++) {
                        regFileName = onecontainer1.getRegHashMap().get(i).toString();
                        file = new File(oldAdvHtmlFileProps + "/" + regFileName);
                        if (file.exists()) {
                            FileInputStream fisR = new FileInputStream(oldAdvHtmlFileProps + "/" + regFileName);
                            ObjectInputStream oisR = new ObjectInputStream(fisR);
                            regString = (String) oisR.readObject();
                            oisR.close();
                        }
                        regFileName = "InnerRegionDetails" + oneviewID + "_" + i + "_" + session.getId() + "_" + System.currentTimeMillis() + ".txt";
                        FileOutputStream fosR = new FileOutputStream(tempdir + "/" + regFileName);
                        ObjectOutputStream oosR = new ObjectOutputStream(fosR);
                        oosR.writeObject(regString);
                        oosR.flush();
                        oosR.close();
                        tempRegHashMap.put(i, regFileName);
                    }
                    onecontainer1.SetTempRegHashMap(tempRegHashMap);

                    FileOutputStream fos = new FileOutputStream(tempdir + "/" + localfileName);
                    ObjectOutputStream oos = new ObjectOutputStream(fos);
                    oos.writeObject(onecontainer1);
                    oos.flush();
                    oos.close();
                    FileOutputStream Savefos = new FileOutputStream(oldAdvHtmlFileProps + "/" + GlobalfileName);
                    ObjectOutputStream Saveoos = new ObjectOutputStream(Savefos);
                    Saveoos.writeObject(onecontainer1);
                    Saveoos.flush();
                    Saveoos.close();
                }

            } else {
                localfileName = session.getAttribute("tempFileName").toString();
                file = new File(advHtmlFileProps + "/" + localfileName);
                if (file.exists()) {
                    FileInputStream fis = new FileInputStream(advHtmlFileProps + "/" + localfileName);
                    ObjectInputStream ois = new ObjectInputStream(fis);
                    onecontainer1 = (OnceViewContainer) ois.readObject();
                    ois.close();
                    //onecontainer1.setContainerFileName(fileName);
                    //   tempdir = System.getProperty("java.io.tmpdir");
                    // session.setAttribute("oldadvHtmlFileProps",advHtmlFileProps);
                    // session.setAttribute("advHtmlFileProps",tempdir);

                }
            }
            advHtmlFileProps = (String) request.getSession(false).getAttribute("advHtmlFileProps");
            if (session.getAttribute("tempFileName") != null) {
                localfileName = session.getAttribute("tempFileName").toString();
                file = new File(advHtmlFileProps + "/" + localfileName);
                if (file.exists()) {
                    FileInputStream fis = new FileInputStream(advHtmlFileProps + "/" + localfileName);
                    ObjectInputStream ois = new ObjectInputStream(fis);
                    onecontainer1 = (OnceViewContainer) ois.readObject();
                    ois.close();
                }
            }
        }
        boolean gotest = false;
        String settingType = "";
        boolean isEveryTimeUpdate = false;
        if (onecontainer1 != null) {
            settingType = onecontainer1.getSettingType();
            isEveryTimeUpdate = onecontainer1.isEveryTimeUpdate();
        }
// sandeep code for date(range and standard basis)
        String CurrVal = "";
        String cfvalue = "";
        String ctvalue = "";
//        if(!oneVersion.equalsIgnoreCase("2.5")){
//String CurrVal = "";
        if (action != null && action.equalsIgnoreCase("datetoggle")) {
            gotest = true;
        } else {
            if (onecontainer1.timedetails.get(1).equalsIgnoreCase("PRG_STD")) {
                if (!onecontainer1.timedetails.isEmpty()) {
                    onecontainer1.timedetails.clear();
                }
                onecontainer1.addTimeDetails("Day");
                onecontainer1.addTimeDetails("PRG_STD");
                String value = "";
                String valu = "";
                String mont = "";
                String CurrValue = "";
                value = date;
                SimpleDateFormat format1 = new SimpleDateFormat("EEE,dd,MMM,yyyy");
                SimpleDateFormat format = new SimpleDateFormat("EEE,dd,MMM,yy");
                SimpleDateFormat format2 = new SimpleDateFormat("MM/dd/yyyy");
                Date date1 = null;
                if (isdefult != null && isdefult.equalsIgnoreCase("true")) {
                    value = value.replace("'", ",");
                    date1 = format.parse(value.toString().trim());
                    value = format2.format(date1);
                } else {
                    date1 = format1.parse(value.toString().trim());
                    value = format2.format(date1);
                }
                valu = value.substring(0, 2);
                mont = value.substring(3, 5);
                CurrValue = mont.concat("/").concat(valu).concat(value.substring(5));
                onecontainer1.addTimeDetails(value);
                onecontainer1.addTimeDetails(duration);
                onecontainer1.addTimeDetails(compare);
//            reportTemplateDAO.updateOneviewData(onecontainer1, oneviewID);
                if (oneVersion.equalsIgnoreCase("1.1")) {
                    advHtmlFileProps = session.getAttribute("advHtmlFileProps").toString();
                    FileOutputStream fos = new FileOutputStream(advHtmlFileProps + "/" + localfileName);
                    ObjectOutputStream oos = new ObjectOutputStream(fos);
                    oos.writeObject(onecontainer1);
                    oos.flush();
                    oos.close();

                }

                FileOutputStream fos = new FileOutputStream(advHtmlFileProps + "/" + localfileName);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(onecontainer1);
                oos.flush();
                oos.close();
                gotest = true;
            } else {
                if (duration != null && date != null && compare != null) {
                    if (!onecontainer1.timedetails.isEmpty()) {
                        onecontainer1.timedetails.clear();
                    }
                    onecontainer1.addTimeDetails("Day");
                    onecontainer1.addTimeDetails("PRG_DATE_RANGE");
                    String value = "";
                    String valu = "";
                    String mont = "";
                    String CurrValue = "";
                    value = daterange;

                    Calendar ca1 = Calendar.getInstance();

                    SimpleDateFormat format1 = new SimpleDateFormat("EEE,dd,MMM,yyyy");
                    SimpleDateFormat format = new SimpleDateFormat("EEE,dd,MMM,yy");
                    SimpleDateFormat format2 = new SimpleDateFormat("MM/dd/yyyy");
                    Date date1 = null;
                    Date date2 = null;
                    Date date3 = null;
                    Date date4 = null;
                    if (duration1 == null ? "" != null : !duration1.equals("")) {
                        if (isdefult != null && isdefult.equalsIgnoreCase("true")) {
                            value = value.replace("'", ",");
                            duration1 = duration1.replace("'", ",");
                            date1 = format.parse(value.toString().trim());
                            date2 = format.parse(duration1.toString().trim());

                        } else if (isdefult != null && isdefult.equalsIgnoreCase("date1")) {
                            value = value.replace("'", ",");
                            date1 = format.parse(value.toString().trim());
                            date2 = format1.parse(duration1.toString().trim());
                        } else if (isdefult != null && isdefult.equalsIgnoreCase("duration1")) {
                            duration1 = duration1.replace("'", ",");
                            date1 = format1.parse(value.toString().trim());
                            date2 = format.parse(duration1.toString().trim());
                        } else {
                            date1 = format1.parse(value.toString().trim());
                            date2 = format1.parse(duration1.toString().trim());

                        }
                        if (oneviewtype != null && oneviewtype.equalsIgnoreCase("flase")) {
                            date3 = format1.parse(comparefrom.toString().trim());
                            date4 = format1.parse(compareto.toString().trim());
                        }
                    } else {
                    }

                    value = format2.format(date1);
                    duration = format2.format(date2);
                    valu = value.substring(0, 2);
                    mont = value.substring(3, 5);
                    CurrValue = mont.concat("/").concat(valu).concat(value.substring(5));
                    CurrVal = value;

                    onecontainer1.addTimeDetails(CurrVal);
                    onecontainer1.addTimeDetails(duration);
                    if (oneviewtype != null && oneviewtype.equalsIgnoreCase("flase")) {
                        cfvalue = format2.format(date3);
                        ctvalue = format2.format(date4);
                        onecontainer1.addTimeDetails(cfvalue);
                        onecontainer1.addTimeDetails(ctvalue);
                    }
                    gotest = true;

                }
            }
        }
//    }else{
//
//        if (duration != null && date != null && compare != null) {
//            if (!onecontainer1.timedetails.isEmpty()) {
//                onecontainer1.timedetails.clear();
//            }
//            onecontainer1.addTimeDetails("Day");
//            onecontainer1.addTimeDetails("PRG_STD");
//            String value = "";
//            String valu = "";
//            String mont = "";
//            String CurrValue = "";
//            value = date;
//            valu = value.substring(0, 2);
//            mont = value.substring(3, 5);
//            CurrValue = mont.concat("/").concat(valu).concat(value.substring(5));
//            onecontainer1.addTimeDetails(CurrValue);
//            onecontainer1.addTimeDetails(duration);
//            onecontainer1.addTimeDetails(compare);
////            reportTemplateDAO.updateOneviewData(onecontainer1, oneviewID);
//            if(oneVersion.equalsIgnoreCase("1.1")){
//                advHtmlFileProps=session.getAttribute("advHtmlFileProps").toString();
//            FileOutputStream fos = new FileOutputStream(advHtmlFileProps+"/"+localfileName);
//            ObjectOutputStream oos = new ObjectOutputStream(fos);
//            oos.writeObject(onecontainer1);
//            oos.flush();
//            oos.close();
//
//            }
//
//            FileOutputStream fos = new FileOutputStream(advHtmlFileProps+"/"+localfileName);
//            ObjectOutputStream oos = new ObjectOutputStream(fos);
//            oos.writeObject(onecontainer1);
//            oos.flush();
//            oos.close();
//
////            String tempdir = System.getProperty("java.io.tmpdir");
////            FileOutputStream tmpStream = new FileOutputStream(tempdir);
//            gotest = true;
//
//        }
//
//
//    }
//        onecontainer = reportTemplateDAO.getOneViewData(oneviewID);

        String regionFile = null;
        String fileName1 = null;
        regionFile = reportTemplateDAO.getRegionOneview(oneviewID);
        if (va.contains(oneviewID)) {
            retObj = reportTemplateDAO.getOneviewFileNam(oneviewID);
            // fileName1 = retObj.getFieldValueString(0, "ONEVIEW_FILE");
            if (session.getAttribute("tempFileName") != null) {
                fileName1 = session.getAttribute("tempFileName").toString();
                File file = new File(advHtmlFileProps + "/" + fileName1);
                if (file.exists()) {
                    FileInputStream fis1 = new FileInputStream(advHtmlFileProps + "/" + fileName1);
                    ObjectInputStream ois1 = new ObjectInputStream(fis1);
                    ObjectStreamClass.lookup(OneViewLetDetails.class).getSerialVersionUID();
                    onecontainer = (OnceViewContainer) ois1.readObject();
                    ois1.close();
                    onecontainer.setContainerFileName(localfileName);
                }

            }
        }


        StringBuilder finalStringVal = new StringBuilder();
        String CurrValue = null;
        String valu = null;
        String value = null;
        //added by srikanth.p

//        HashMap map=(HashMap) session.getAttribute("ONEVIEWDETAILS");
//        if(onecontainer !=null){
//        if(map ==null ){
//            map=new HashMap();
//            map.put(oneviewID,onecontainer );
//            session.setAttribute("ONEVIEWDETAILS", map);
//        }
//         else{
//
//                    map.put(oneviewID,onecontainer );
//                    session.setAttribute("ONEVIEWDETAILS", map);
//         }
//        }
        //ended here by srikanth.p

        if (gotest) {
            List<OneViewLetDetails> oneviewletDetails = onecontainer.onviewLetdetails;
            int originCurrVal = onecontainer.width;
            if (!onecontainer.timedetails.isEmpty()) {
                tiemdeatilsArray = onecontainer.timedetails;
                if (oneviewtype != null && oneviewtype.equalsIgnoreCase("true")) {
//                     onecontainer.timedetails=  onecontainer1.timedetails;
//                     tiemdeatilsArray = onecontainer.timedetails;
                    if (action != null && action.equalsIgnoreCase("datetoggle")) {
                    } else {
                        if (duration != null && duration != "" && date != null && compare != null) {
                        } else {
                            request.setAttribute("OneviewTiemDetails", tiemdeatilsArray);
                            value = "";
                            valu = "";
                            String mont = "";
                            CurrValue = "";
                            value = tiemdeatilsArray.get(2);
                            valu = value.substring(0, 2);
                            mont = value.substring(3, 5);
                            CurrValue = mont.concat("/").concat(valu).concat(value.substring(5));
                            value = tiemdeatilsArray.get(3);
                            valu = tiemdeatilsArray.get(4);
                        }
                    }
                } else {
                    request.setAttribute("OneviewTiemDetails", tiemdeatilsArray);
                    value = "";
                    valu = "";
                    String mont = "";
                    CurrValue = "";
                    value = tiemdeatilsArray.get(2);
                    valu = value.substring(0, 2);
                    mont = value.substring(3, 5);
                    CurrValue = mont.concat("/").concat(valu).concat(value.substring(5));
                    value = tiemdeatilsArray.get(3);
                    valu = tiemdeatilsArray.get(4);

                }
            } else {
                ProgenParam paramdate = new ProgenParam();
                value = "";
                valu = "";
                String mont = "";
                CurrValue = "";
                value = paramdate.getdateforpage();
                valu = value.substring(0, 2);
                mont = value.substring(3, 5);
                CurrValue = mont.concat("/").concat(valu).concat(value.substring(5));
                value = "Month";
                valu = "Last Period";

            }



            StringBuilder dashBuilder = new StringBuilder("");
            int rowIndex = -1;

//           List<DashletDetail> dashletDetails = collect.dashletDetails;
            boolean flag = false;
//        int buildTable = 0;
            String kpiType = "";
            ArrayList<Integer> al = new ArrayList();
            String transpose = onecontainer1.getistrasnspose();
            if (transpose != null && transpose.equalsIgnoreCase("true")) {
                request.setAttribute("transpose", transpose);
            }
            if (!oneVersion.equalsIgnoreCase("2.5")) {
            } else {
                if (oneviewletDetails != null && !oneviewletDetails.isEmpty()) {
                    finalStringVal.append("<div  align=\"left\" style=\"width:100%\">");
                    finalStringVal.append("<table align='right'><tr><td><input type='text' id='oneSetting_EveryTime' value='" + isEveryTimeUpdate + "'style='display:none;'></input></td><td><input type='text' id='oneSetting_Hidden' name='' value='" + settingType + "'style='display:none;'></input></td><td style='display:none;'>").append("<input type='text' id='dateIdValue' name='' value='" + CurrValue + "'></input>").append("</td>").append("<td style='display:none;'>").append("<input type='text' id='durationId' name='' value='" + value + "'></input>").append("</td>").append("<td style='display:none;'>").append("<input type='text' id='comareWithId' name='' value='" + valu + "'></input>").append("</td></tr></table>");

                    //finalStringVal.append("<table align='right'><tr><td id =\"addreginView\" style='display:none;'><a class=\"ui-icon ui-icon-plusthick\"  href=\"javascript:void(0)\" title=\"Add Region\" onclick=\"addRegioninViewer("+oneviewID+")\" ></a></td><td style='display:none;'>").append("<input type='text' id='dateIdValue' name='' value='" + CurrValue + "'></input>").append("</td>").append("<td style='display:none;'>").append("<input type='text' id='durationId' name='' value='" + value + "'></input>").append("</td>").append("<td style='display:none;'>").append("<input type='text' id='comareWithId' name='' value='" + valu + "'></input>").append("</td></tr></table>");
//            finalStringVal.append("<tr>");
//           finalStringVal.append("<td rowsapn='"+dashletDetails.get(p).getRowSpan()+"' colspan='"+dashletDetails.get(p).getColSpan()+"'>");
                    if (onecontainer.getOneViewType() != null && (onecontainer.getOneViewType().equalsIgnoreCase("Business TemplateView") || onecontainer.getOneViewType().equalsIgnoreCase("Measure Based Business Template"))) {
                        finalStringVal.append(oneViewBD.buildBusinessTemplateReturnObject(request, response, onecontainer));
//                finalStringVal.append(oneViewBD.generateBusinessTemplateView(request, response, onecontainer));
                    } else {
                        String issettimemesure = "";
                        ArrayListMultimap<Integer, Integer> rowinfo = ArrayListMultimap.create();
                        for (int i = 0; i < oneviewletDetails.size(); i++) {
                            OneViewLetDetails detail = oneviewletDetails.get(i);
                            rowinfo.put(detail.getRow(), detail.getCol());
//                    int currWidth = Integer.parseInt(currInnerWidth)/oneviewletDetails.size();
//                    detail.setWidth(currWidth);
                            int curModWidth = 0;
                            if (currInnerWidth != null && !currInnerWidth.equalsIgnoreCase("") && originCurrVal > 0) {
                                curModWidth = (Integer.parseInt(currInnerWidth) * detail.getWidth()) / originCurrVal;
                                detail.setWidth(curModWidth);
                            }
                            String reptype = detail.getReptype();
                            if (reptype != null && reptype.equalsIgnoreCase("repGraph") && oneviewtype != null && oneviewtype.equalsIgnoreCase("true")) {
                                ArrayList<String> timeInfo = null;
                                HashMap map1 = new HashMap();
                                String userId = String.valueOf(session.getAttribute("USERID"));
                                Container container = null;
                                container = detail.getContainer();
                                PbReportCollection collect = new PbReportCollection();
// PbReportCollection collect=new PbReportCollection();
                                if (container != null) {
                                    collect = container.getReportCollect();
                                } else {
//        HashMap map = null;
                                    String reportId = detail.getRepId();
                                    PbReportViewerBD reportViewerBD = new PbReportViewerBD();
                                    request.setAttribute("REPORTID", reportId);
                                    reportViewerBD.prepareReport(reportId, userId, request, response,false);
                                    map1 = (HashMap) session.getAttribute("PROGENTABLES");

                                    container = (Container) map1.get(reportId);
                                    collect = container.getReportCollect();
                                }
                                timeInfo = collect.timeDetailsArray;
                                if (issettimemesure != null && issettimemesure.equalsIgnoreCase("true")) {
                                } else {
                                    if (timeInfo != null && timeInfo.get(1).toString().equalsIgnoreCase("PRG_DATE_RANGE")) {
                                        onecontainer1.timedetails.add(4, timeInfo.get(4).toString());
                                        onecontainer1.timedetails.add(5, timeInfo.get(5).toString());
                                        issettimemesure = "true";
                                        onecontainer.timedetails = onecontainer1.timedetails;
                                    }
                                }
                            }
                        }
                        if (currInnerWidth != null && !currInnerWidth.equalsIgnoreCase("") && originCurrVal > 0) {
                            onecontainer.width = Integer.parseInt(currInnerWidth);
                        }

                        for (int count = 0; count < rowinfo.keySet().size(); count++) {
                            int buildTable = 0;
//                if(rowIndex<rowinfo.keySet().size()-1)
                            rowIndex++;
                            int colNum = 0;
                            int rowNum = 0;
                            int rowSpanNum = 0;
                            int colSpanNum = 0;
                            List<Integer> dashlets = rowinfo.get(rowIndex);
//                while (dashlets.isEmpty()) {
//                    rowIndex++;
//                    dashlets = rowinfo.get(rowIndex);
//                }
                            al.add(rowIndex);
                            int numOfDashlets = dashlets.size();
                            int numOfCols = numOfDashlets;
                            for (int p = 0; p < oneviewletDetails.size(); p++) {
                                OneViewLetDetails detail = oneviewletDetails.get(p);
                                int row = detail.getRow();
                                int col = detail.getCol();
                                int rowSpan = detail.getRowSpan();
                                int colSpan = detail.getColSpan();
                                if (row == rowIndex) {

                                    if (rowSpan == 1) {
                                        colNum = col;
                                        rowNum = row;
                                        rowSpanNum = rowSpan;
                                        colSpanNum = colSpan;
                                        if (colSpan > 1) {
                                            numOfCols = numOfCols + (colSpan - 1);
                                        }
                                        buildTable = 1;
                                        flag = false;
                                    } else if (rowSpan > 1) {
                                        colNum = col;
                                        rowNum = row;
                                        rowSpanNum = rowSpan;
                                        colSpanNum = colSpan;
                                        if (colSpan > 1) {
                                            numOfCols = numOfCols + (colSpan - 1);
                                        }

                                        flag = true;
                                    }
                                    if (flag == true) {
                                        buildTable = 2;
                                        break;
                                    }
                                }
                            }

                            if (buildTable == 1) {
                                finalStringVal.append("<table style='table-layout: fixed; width: 100%; border-spacing: 15px;'>");
                                for (int i = 0; i < oneviewletDetails.size(); i++) {
                                    OneViewLetDetails detail = oneviewletDetails.get(i);
                                    int row = detail.getRow();
                                    int col = detail.getCol();
                                    detail.setPbReturnObject(null);
                                    String result = "";
                                    if (row == rowNum) {
                                        if (col == 0) {
                                            finalStringVal.append("<tr width=100%>");
//                                    if (!onecontainer.timedetails.isEmpty() && !detail.isOneviewReportTimeDetails()) {
//                                                    tiemdeatilsArray = onecontainer.timedetails;
//                                                    request.setAttribute("OneviewTiemDetails", tiemdeatilsArray);
//                                        }
//                                      if(!oneVersion.equalsIgnoreCase("2.5")){
                                            oneViewBD.datefromtotoggle(request, response, onecontainer1, onecontainer, detail, oneviewID, action, duration, CurrVal, localfileName, oneVersion, cfvalue, ctvalue);
//                                }
                                            if (detail.getReptype() != null && detail.getReptype().equalsIgnoreCase("template")) {
                                                detail.setRepName("Template");
                                            }
                                            result = oneViewBD.buildRegionData(request, response, onecontainer, detail);
                                            if (result != null && (result == null ? "" != null : !result.equals(""))) {
                                            } else {
                                                detail.setReptype("template");
                                                detail.setRepName("Template");
                                                result = oneViewBD.buildRegionData(request, response, onecontainer, detail);
                                            }
                                            if (onecontainer.timedetails != null && !onecontainer.timedetails.isEmpty()) {
                                            } else {
                                                HashMap mapp = new HashMap();
                                                mapp = (HashMap) session.getAttribute("temearry");
                                                ArrayList timedetails = (ArrayList) mapp.get("timeInfo");
                                                onecontainer.timedetails = timedetails;
                                            }
//                                }else{
//                             result = oneViewBD.buildRegionData(request, response, onecontainer, detail);
//
//                                }
                                            oneViewBD.writeOneviewRegData(onecontainer, result, detail.getNoOfViewLets(), request);
                                            if (Float.parseFloat(oneVersion) < (float) 2.1) {
                                                oneViewBD.saveOneviewRegData(onecontainer, result, detail.getNoOfViewLets(), request);
                                            }
                                            finalStringVal.append("<td id='" + detail.getNoOfViewLets() + "' width='" + detail.getWidth() + "px' style='height:" + detail.getHeight() + "px;' rowspan='" + detail.getRowSpan() + "' colspan='" + detail.getColSpan() + "'>");
                                            finalStringVal.append(result);
                                            finalStringVal.append("</td>");


                                        } else {

//                                    if (!onecontainer.timedetails.isEmpty() && !detail.isOneviewReportTimeDetails()) {
//                                                    tiemdeatilsArray = onecontainer.timedetails;
//                                                    request.setAttribute("OneviewTiemDetails", tiemdeatilsArray);
//                                        }
//                                       if(!oneVersion.equalsIgnoreCase("2.5")){
                                            oneViewBD.datefromtotoggle(request, response, onecontainer1, onecontainer, detail, oneviewID, action, duration, CurrVal, localfileName, oneVersion, cfvalue, ctvalue);
//                                }
                                            if (detail.getReptype() != null && detail.getReptype().equalsIgnoreCase("template")) {
                                                detail.setRepName("Template");
                                            }
                                            result = oneViewBD.buildRegionData(request, response, onecontainer, detail);
                                            if (result != null && (result == null ? "" != null : !result.equals(""))) {
                                            } else {
                                                detail.setReptype("template");
                                                detail.setRepName("Template");
                                                result = oneViewBD.buildRegionData(request, response, onecontainer, detail);
                                            }
                                            if (onecontainer.timedetails != null && !onecontainer.timedetails.isEmpty()) {
                                            } else {
                                                HashMap map12 = new HashMap();
                                                map12 = (HashMap) session.getAttribute("temearry");
                                                ArrayList timedetails = (ArrayList) map12.get("timeInfo");
                                                onecontainer.timedetails = timedetails;
                                            }
//                                }else{
//                                    result = oneViewBD.buildRegionData(request, response, onecontainer, detail);
//                            }
                                            oneViewBD.writeOneviewRegData(onecontainer, result, detail.getNoOfViewLets(), request);
                                            if (Float.parseFloat(oneVersion) < (float) 2.1) {
                                                oneViewBD.saveOneviewRegData(onecontainer, result, detail.getNoOfViewLets(), request);
                                            }
                                            finalStringVal.append("<td id='" + detail.getNoOfViewLets() + "' width='" + detail.getWidth() + "px' style='height:" + detail.getHeight() + "px;' rowspan='" + detail.getRowSpan() + "' colspan='" + detail.getColSpan() + "'>");
                                            finalStringVal.append(result);
                                            finalStringVal.append("</td>");

                                        }

                                    }
                                }
                                finalStringVal.append("</tr></table>");
                                al = new ArrayList();
                            } else if (buildTable == 2) {
                                int startTableRowNum = rowNum;
                                int maxRows = rowNum + rowSpanNum;
                                int endTableRowNum = maxRows;
                                int newRowNum = rowNum;
                                int newRowSpanNum = rowSpanNum;
                                int newColNum = colNum;
                                int newColSpanNum = colSpanNum;
                                int newMaxRows = maxRows;
                                for (int k = rowNum; k < maxRows; k++) {
                                    for (int n = 0; n < oneviewletDetails.size(); n++) //loop-^295
                                    {
                                        OneViewLetDetails detail = oneviewletDetails.get(n);
                                        int row = detail.getRow();
                                        int col = detail.getCol();
                                        int rowSpan = detail.getRowSpan();
                                        int colSpan = detail.getColSpan();
                                        detail.setPbReturnObject(null);

                                        if (row >= rowNum && row < maxRows) {
                                            if (row != rowNum || col != colNum) {
                                                if (rowSpan > 1) {
                                                    if (colSpan > 1) {
                                                        numOfCols = numOfCols + (colSpan - 1);
                                                    }
                                                    newRowNum = row;
                                                    newColNum = col;
                                                    newRowSpanNum = rowSpan;
                                                    newColSpanNum = colSpan;
                                                    newMaxRows = newRowNum + newRowSpanNum;
                                                    //                                    flag=true;
                                                } else {
                                                    if (colSpan > 1) {
                                                        numOfCols = numOfCols + (colSpan - 1);
                                                    }
                                                }

                                                if (newMaxRows > maxRows) {
                                                    maxRows = newMaxRows;
                                                    rowNum = newRowNum;
                                                    colNum = newColNum;
                                                    rowSpanNum = newRowSpanNum;
                                                    colSpanNum = newColSpanNum;
                                                    endTableRowNum = newMaxRows;
                                                    flag = true;
                                                } else {
                                                    flag = false;
                                                }
                                                if (flag == true) {
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                    if (flag == false) {
                                        k++;
                                    }
                                }

                                finalStringVal.append("<table style='table-layout: fixed; width: 100%; border-spacing: 15px;'>");
                                int prevRow = 0;
                                for (int i = 0; i < oneviewletDetails.size(); i++) {
                                    OneViewLetDetails detail = oneviewletDetails.get(i);
                                    int row = detail.getRow();
                                    int col = detail.getCol();
//                            int rowSpan = detail.getRowSpan();
//                            int colSpan = detail.getColSpan();
//                            String dashletId = detail.getRepId();

//                            // % wise width for dashlets
//                            double width = 100;
//                            if (numOfCols == 1) {
//                                width = 100;
//                            } else if (numOfCols == 2) {
//                                width = 50;
//                                width = width * colSpan;
//                            } else if (numOfCols == 3) {
//                                width = 33;
//                                width = width * colSpan;
//                            } else if (numOfCols == 4) {
//                                width = 25;
//                                width = width * colSpan;
//                            }
                                    if (row >= startTableRowNum && row < endTableRowNum) {
                                        if (row == startTableRowNum && col == 0) {
                                            prevRow = row;
                                            finalStringVal.append("<tr width=100%>");
// if(!oneVersion.equalsIgnoreCase("2.5")){
                                            oneViewBD.datefromtotoggle(request, response, onecontainer1, onecontainer, detail, oneviewID, action, duration, CurrVal, localfileName, oneVersion, cfvalue, ctvalue);
//                                }
                                            FileOutputStream fos = new FileOutputStream(advHtmlFileProps + "/" + localfileName);
                                            ObjectOutputStream oos = new ObjectOutputStream(fos);
                                            oos.writeObject(onecontainer1);
                                            oos.flush();
                                            oos.close();
                                            FileOutputStream fos1 = new FileOutputStream(advHtmlFileProps + "/" + localfileName);
                                            ObjectOutputStream oos1 = new ObjectOutputStream(fos1);
                                            oos1.writeObject(onecontainer);
                                            oos1.flush();
                                            oos1.close();
//                                    }
                                            if (detail.getReptype() != null && detail.getReptype().equalsIgnoreCase("template")) {
                                                detail.setRepName("Template");
                                            }
                                            String result = oneViewBD.buildRegionData(request, response, onecontainer, detail);
                                            if (result != null && (result == null ? "" != null : !result.equals(""))) {
                                            } else {
                                                detail.setReptype("template");
                                                detail.setRepName("Template");
                                                result = oneViewBD.buildRegionData(request, response, onecontainer, detail);
                                            }
                                            oneViewBD.writeOneviewRegData(onecontainer, result, detail.getNoOfViewLets(), request);
                                            if (Float.parseFloat(oneVersion) < (float) 2.1) {
                                                oneViewBD.saveOneviewRegData(onecontainer, result, detail.getNoOfViewLets(), request);
                                            }
                                            finalStringVal.append("<td id='" + detail.getNoOfViewLets() + "' width='" + detail.getWidth() + "px' style='height:" + detail.getHeight() + "px;' rowspan='" + detail.getRowSpan() + "' colspan='" + detail.getColSpan() + "'>");
                                            finalStringVal.append(result);
                                            finalStringVal.append("</td>");

                                        } else if (row == prevRow) {

                                            String result = "";
//                                    if (!onecontainer.timedetails.isEmpty() && !detail.isOneviewReportTimeDetails()) {
//                                                    tiemdeatilsArray = onecontainer.timedetails;
//                                                    request.setAttribute("OneviewTiemDetails", tiemdeatilsArray);
//                                        }
// if(!oneVersion.equalsIgnoreCase("2.5")){
                                            oneViewBD.datefromtotoggle(request, response, onecontainer1, onecontainer, detail, oneviewID, action, duration, CurrVal, localfileName, oneVersion, cfvalue, ctvalue);
//                                }
//         if(action!=null && action.equalsIgnoreCase("datetoggle")){
//             action="GO";
//         }
                                            session.setAttribute("temearry1", onecontainer.timedetails);
                                            if (detail.getReptype() != null && detail.getReptype().equalsIgnoreCase("template")) {
                                                detail.setRepName("Template");
                                            }
                                            result = oneViewBD.buildRegionData(request, response, onecontainer, detail);
                                            if (result != null && (result == null ? "" != null : !result.equals(""))) {
                                            } else {
                                                detail.setReptype("template");
                                                detail.setRepName("Template");
                                                result = oneViewBD.buildRegionData(request, response, onecontainer, detail);
                                            }
                                            if (onecontainer.timedetails != null && !onecontainer.timedetails.isEmpty()) {
                                            } else {
//                                  HashMap map = new HashMap();
                                                ArrayList timedetails = (ArrayList) session.getAttribute("temearry1");
//        ArrayList timedetails=    (ArrayList) map.get("timeInfo");
                                                onecontainer.timedetails = timedetails;
                                            }
//                                }else{
//                                   result = oneViewBD.buildRegionData(request, response, onecontainer, detail);
//                                }
                                            oneViewBD.writeOneviewRegData(onecontainer, result, detail.getNoOfViewLets(), request);
                                            if (Float.parseFloat(oneVersion) < (float) 2.1) {
                                                oneViewBD.saveOneviewRegData(onecontainer, result, detail.getNoOfViewLets(), request);
                                            }
                                            finalStringVal.append("<td id='" + detail.getNoOfViewLets() + "' width='" + detail.getWidth() + "px' style='height:" + detail.getHeight() + "px;' rowspan='" + detail.getRowSpan() + "' colspan='" + detail.getColSpan() + "'>");
                                            finalStringVal.append(result);
                                            finalStringVal.append("</td>");

                                        } else {
                                            finalStringVal.append("</tr>");
                                            prevRow = row;
                                            finalStringVal.append("<tr width=100%>");
                                            if (col == 0) {
                                                String result = "";


//                                    if (!onecontainer.timedetails.isEmpty() && !detail.isOneviewReportTimeDetails()) {
//                                                    tiemdeatilsArray = onecontainer.timedetails;
//                                                    request.setAttribute("OneviewTiemDetails", tiemdeatilsArray);
//                                        }
// if(!oneVersion.equalsIgnoreCase("2.5")){
                                                oneViewBD.datefromtotoggle(request, response, onecontainer1, onecontainer, detail, oneviewID, action, duration, CurrVal, localfileName, oneVersion, cfvalue, ctvalue);
//                                }

                                                if (detail.getReptype() != null && detail.getReptype().equalsIgnoreCase("template")) {
                                                    detail.setRepName("Template");
                                                }
                                                result = oneViewBD.buildRegionData(request, response, onecontainer, detail);
                                                if (result != null && (result == null ? "" != null : !result.equals(""))) {
                                                } else {
                                                    detail.setReptype("template");
                                                    detail.setRepName("Template");
                                                    result = oneViewBD.buildRegionData(request, response, onecontainer, detail);
                                                }
                                                if (onecontainer.timedetails != null && !onecontainer.timedetails.isEmpty()) {
                                                } else {
                                                    HashMap map2 = new HashMap();
                                                    map2 = (HashMap) session.getAttribute("temearry");
                                                    ArrayList timedetails = (ArrayList) map2.get("timeInfo");
                                                    onecontainer.timedetails = timedetails;
                                                }
//                                        }else{
//                                            result = oneViewBD.buildRegionData(request, response, onecontainer, detail);
//                                        }
                                                oneViewBD.writeOneviewRegData(onecontainer, result, detail.getNoOfViewLets(), request);
                                                if (Float.parseFloat(oneVersion) < (float) 2.1) {
                                                    oneViewBD.saveOneviewRegData(onecontainer, result, detail.getNoOfViewLets(), request);
                                                }
                                                finalStringVal.append("<td id='" + detail.getNoOfViewLets() + "' width='" + detail.getWidth() + "px' style='height:" + detail.getHeight() + "px;' rowspan='" + detail.getRowSpan() + "' colspan='" + detail.getColSpan() + "'>");
                                                finalStringVal.append(result);
                                                finalStringVal.append("</td>");
                                            } else {

                                                String result = "";
//                                    if (!onecontainer.timedetails.isEmpty() && !detail.isOneviewReportTimeDetails()) {
//                                                    tiemdeatilsArray = onecontainer.timedetails;
//                                                    request.setAttribute("OneviewTiemDetails", tiemdeatilsArray);
//                                        }
// if(!oneVersion.equalsIgnoreCase("2.5")){
                                                oneViewBD.datefromtotoggle(request, response, onecontainer1, onecontainer, detail, oneviewID, action, duration, CurrVal, localfileName, oneVersion, cfvalue, ctvalue);
//                                }
                                                if (detail.getReptype() != null && detail.getReptype().equalsIgnoreCase("template")) {
                                                    detail.setRepName("Template");
                                                }
                                                result = oneViewBD.buildRegionData(request, response, onecontainer, detail);
                                                if (result != null && (result == null ? "" != null : !result.equals(""))) {
                                                } else {
                                                    detail.setReptype("template");
                                                    detail.setRepName("Template");
                                                    result = oneViewBD.buildRegionData(request, response, onecontainer, detail);
                                                }
                                                if (onecontainer.timedetails != null && !onecontainer.timedetails.isEmpty()) {
                                                } else {
                                                    HashMap maap = new HashMap();
                                                    maap = (HashMap) session.getAttribute("temearry");
                                                    ArrayList timedetails = (ArrayList) maap.get("timeInfo");
                                                    onecontainer.timedetails = timedetails;
                                                }
//                                        }else{
//                                            result = oneViewBD.buildRegionData(request, response, onecontainer, detail);
//                                        }
                                                oneViewBD.writeOneviewRegData(onecontainer, result, detail.getNoOfViewLets(), request);
                                                if (Float.parseFloat(oneVersion) < (float) 2.1) {
                                                    oneViewBD.saveOneviewRegData(onecontainer, result, detail.getNoOfViewLets(), request);
                                                }
                                                finalStringVal.append("<td id='" + detail.getNoOfViewLets() + "' width='" + detail.getWidth() + "px' style='height:" + detail.getHeight() + "px;' rowspan='" + detail.getRowSpan() + "' colspan='" + detail.getColSpan() + "'>");
                                                finalStringVal.append(result);
                                                finalStringVal.append("</td>");
                                            }
                                        }
                                    }
                                }
                                finalStringVal.append("</tr></table>");
                                rowIndex = +(endTableRowNum - 1);
                            }
                        }

                    }
                }
            }
//            finalStringVal.append("<table align='right'><tr><td><input type='text' id='oneSetting_EveryTime' value='" +isEveryTimeUpdate+ "'style='display:none;'></input></td><td><input type='text' id='oneSetting_Hidden' name='' value='" +settingType+ "'style='display:none;'></input></td><td style='display:none;'>").append("<input type='text' id='dateIdValue' name='' value='" + CurrValue + "'></input>").append("</td>").append("<td style='display:none;'>").append("<input type='text' id='durationId' name='' value='" + value + "'></input>").append("</td>").append("<td style='display:none;'>").append("<input type='text' id='comareWithId' name='' value='" + valu + "'></input>").append("</td></tr></table>");
            finalStringVal.append("</div>");


            FileOutputStream fos2 = new FileOutputStream(advHtmlFileProps + "/" + localfileName);
            ObjectOutputStream oos2 = new ObjectOutputStream(fos2);
            oos2.writeObject(onecontainer);
            oos2.flush();
            oos2.close();
            if (Float.parseFloat(oneVersion) < 2.1f) {
                String conFileName = reportTemplateDAO.getOneviewFileName(oneviewID);
                FileOutputStream fos1 = new FileOutputStream(oldAdvHtmlFileProps + "/" + conFileName);
                ObjectOutputStream oos1 = new ObjectOutputStream(fos1);
                oos1.writeObject(onecontainer);
                oos1.flush();
                oos1.close();
                fos1.close();
                regionFile = reportTemplateDAO.getRegionOneview(oneviewID);
                FileOutputStream fos = new FileOutputStream(oldAdvHtmlFileProps + "/" + regionFile);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(finalStringVal.toString());
                oos.flush();
                oos.close();
                fos.close();
                String fileName = session.getAttribute("tempFileName").toString();
                FileOutputStream fosLocal = new FileOutputStream(advHtmlFileProps + "/" + fileName);
                ObjectOutputStream oosLoca = new ObjectOutputStream(fosLocal);
                oosLoca.writeObject(onecontainer);
                oosLoca.flush();
                oosLoca.close();
                fosLocal.close();
            }

            response.getWriter().print(finalStringVal.toString());
            if (Float.parseFloat(oneVersion) < (float) 2.1) {
                DashboardTemplateDAO dao = new DashboardTemplateDAO();
                dao.updateOneViewDetails(oneviewID, oldAdvHtmlFileProps, session.getAttribute("OneViewVersion").toString());
            }

        } else if (regionFile != null) {
            File file = new File(oldAdvHtmlFileProps + "/" + regionFile);
            if (file.exists()) {
                FileInputStream fis3 = new FileInputStream(oldAdvHtmlFileProps + "/" + regionFile);
                ObjectInputStream ois3 = new ObjectInputStream(fis3);
                String v = (String) ois3.readObject();
                StringBuilder testBuild = new StringBuilder(v);
                if (!onecontainer.timedetails.isEmpty()) {
                    tiemdeatilsArray = onecontainer.timedetails;
//                           request.setAttribute("OneviewTiemDetails", tiemdeatilsArray);
                }

                List<OneViewLetDetails> dashletDetails = onecontainer.onviewLetdetails;
                for (int i = 0; i < dashletDetails.size(); i++) {
                    OneViewLetDetails detail = dashletDetails.get(i);
                    if (detail.getReptype().toString().equalsIgnoreCase("repGraph") && !detail.getGraphType().equalsIgnoreCase("JQPlot")) {
                        DashboardTemplateBD dashboardTemplateBD = new DashboardTemplateBD();

                        String grapHtest = "graphTest";
                        request.setAttribute("graphTest", grapHtest);
                        String result = "";
                        result = dashboardTemplateBD.getGraphDetailsData(request, response, session, detail, tiemdeatilsArray, onecontainer);
                        if (result != null) {
                            String toolIndex = "#" + detail.graphDetails.get(detail.getNoOfViewLets());
                            int end1 = toolIndex.length();
                            int start1 = testBuild.indexOf("#" + detail.graphDetails.get(detail.getNoOfViewLets()));
                            testBuild.replace(start1, start1 + end1, "#" + result);

                            String val = "/servlet/DisplayChart?filename=" + detail.graphDetails.get(detail.getNoOfViewLets());
                            int end = val.length();
                            int start = testBuild.indexOf("/servlet/DisplayChart?filename=" + detail.graphDetails.get(detail.getNoOfViewLets()));
                            testBuild.replace(start, start + end, "/servlet/DisplayChart?filename=" + result);
                        }

                    }
                    if (detail.getKpiType() != null && (detail.getKpiType().equalsIgnoreCase("KPIGraph") && false)) {
                        DashboardTemplateBD dashboardTemplateBD = new DashboardTemplateBD();
                        String result = "";
                        String grapHtest = "kpiGraph";
                        request.setAttribute("kpiGraph", grapHtest);

                        PbDashboardViewerBD dashboardViewerBD = new PbDashboardViewerBD();
                        Container container = null;
                        container = new Container();
                        pbDashboardCollection collect = new pbDashboardCollection();
                        container.setReportCollect(collect);
                        container.setReportId(detail.getRepId());
                        container.setTableId(detail.getRepId());
                        container.setDbrdName(detail.getRepName());
                        container.setDbrdDesc(detail.getRepName());
                        container.setSessionContext(session, container);
                        dashboardViewerBD.setIsFxCharts(false);
                        dashboardViewerBD.setHttpSession(session);
                        dashboardViewerBD.setDashBoardId(detail.getRepId());
                        dashboardViewerBD.setUserId(session.getAttribute("USERID").toString());
                        dashboardViewerBD.setServletRequest(request);
                        dashboardViewerBD.setServletResponse(response);
                        dashboardViewerBD.setContainer(container);
                        dashboardViewerBD.displayDataForOneviewKpis(request, null, session.getAttribute("USERID").toString());
                        container.setSessionContext(session, container);

                        result = dashboardTemplateBD.getOneviewKpisData(request, response, session, detail, onecontainer);
                        if (result != null) {
                            String toolIndex = "#" + detail.kpiGraphDetails.get(detail.getNoOfViewLets());
                            int end1 = toolIndex.length();
                            int start1 = testBuild.indexOf("#" + detail.kpiGraphDetails.get(detail.getNoOfViewLets()));
                            testBuild.replace(start1, start1 + end1, "#" + result);

                            String val = "/servlet/DisplayChart?filename=" + detail.kpiGraphDetails.get(detail.getNoOfViewLets());
                            int end = val.length();
                            int start = testBuild.indexOf("/servlet/DisplayChart?filename=" + detail.kpiGraphDetails.get(detail.getNoOfViewLets()));
                            testBuild.replace(start, start + end, "/servlet/DisplayChart?filename=" + result);
//                    onecontainer.setRefresh(testBuild.toString());
                        }
                    }
                }
                session.setAttribute("refreshString", testBuild.toString());
                ois3.close();
                response.getWriter().print(testBuild.toString());
            } else {
                response.getWriter().print("The File Does not Exist in this System");
            }
        } else {
            response.getWriter().print("No Data");
        }
        session.removeAttribute("isfirstdatereport");
//      session.removeAttribute("PROGENTABLES");
        return null;
    }
    //Surender
    //Surender

    public ActionForward mergeOneViewRows(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession(false);
        String oneViewIdValue = request.getParameter("oneViewIdValue");
        String dashletId = request.getParameter("dashlet");
        String rowSpan = request.getParameter("rowSpan");
        String height = request.getParameter("height").replace("px", "");
        String from = request.getParameter("from");
//        String delDashletId=request.getParameter("delDashletId");
        Container container = null;
        OnceViewContainer onecontainer = null;
        File file = null;
        if (from != null && from.equalsIgnoreCase("viewer")) {
            String advHtmlFileProps = (String) request.getSession(false).getAttribute("advHtmlFileProps");

            ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();
            String fileName = reportTemplateDAO.getOneviewFileName(oneViewIdValue);
            file = new File(advHtmlFileProps + "/" + fileName);
            if (file.exists()) {
                FileInputStream fis2 = new FileInputStream(advHtmlFileProps + "/" + fileName);
                ObjectInputStream ois2 = new ObjectInputStream(fis2);
                onecontainer = (OnceViewContainer) ois2.readObject();
                ois2.close();
            } else {
                onecontainer = new OnceViewContainer();
                HashMap map = new HashMap();
                map = (HashMap) session.getAttribute("ONEVIEWDETAILS");
                if (map == null) {
                    advHtmlFileProps = (String) request.getSession(false).getAttribute("advHtmlFileProps");

                    fileName = session.getAttribute("tempFileName").toString();
                    FileInputStream fis2 = new FileInputStream(advHtmlFileProps + "/" + fileName);
                    ObjectInputStream ois2 = new ObjectInputStream(fis2);
                    onecontainer = (OnceViewContainer) ois2.readObject();
                    ois2.close();
                } else {
                    onecontainer = (OnceViewContainer) map.get(oneViewIdValue);
                }
            }
            List<OneViewLetDetails> dashletDetails = onecontainer.onviewLetdetails;
            for (int i = 0; i < dashletDetails.size(); i++) {
                OneViewLetDetails detail = dashletDetails.get(i);
                String dashlet = detail.getNoOfViewLets();
                if (dashlet.equals(dashletId)) {
                    detail.setRowSpan(Integer.parseInt(rowSpan));
                    detail.setHeight(Integer.parseInt(height));
                }
            }
            FileOutputStream fos = new FileOutputStream(advHtmlFileProps + "/" + fileName);
            ObjectOutputStream oosR = new ObjectOutputStream(fos);
            oosR.writeObject(onecontainer);
            oosR.flush();
            oosR.close();
            fos.close();

        } else {
            onecontainer = new OnceViewContainer();
            HashMap map = new HashMap();
            map = (HashMap) session.getAttribute("ONEVIEWDETAILS");
            onecontainer = (OnceViewContainer) map.get(oneViewIdValue);
            List<OneViewLetDetails> dashletDetails = onecontainer.onviewLetdetails;
            for (int i = 0; i < dashletDetails.size(); i++) {
                OneViewLetDetails detail = dashletDetails.get(i);
                String dashlet = detail.getNoOfViewLets();
                if (dashlet.equals(dashletId)) {
                    detail.setRowSpan(Integer.parseInt(rowSpan));
                    detail.setHeight(Integer.parseInt(height));
                }
//            if(dashlet.equals(delDashletId))
//            {
//                
//                dashletDetails.remove(i);
//            }
            }
        }


        return null;
    }
    //Surender

    public ActionForward getOneViewMeasureValue(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession(false);
        String measueName = request.getParameter("measureName");
        String measueId = request.getParameter("measureId");
        String height = request.getParameter("height");
        String width = request.getParameter("width");
        String advHtmlFileProps = (String) request.getSession(false).getAttribute("advHtmlFileProps");
        String oneViewId = request.getParameter("oneViewIdValue");
        String colSp = request.getParameter("colSp");
        String rowSp = request.getParameter("rowSp");
        String measureType = request.getParameter("measureType");
        String busrole = request.getParameter("busroleId");
        int viewletId = Integer.parseInt(request.getParameter("divId"));
        String oneviewtypedate = (String) session.getAttribute("oneviewdatetype");
        DashboardViewerDAO dao = new DashboardViewerDAO();
        PbDashboardViewerBD viewerBd = new PbDashboardViewerBD();
        List<String> measureIdVal = new ArrayList<String>();
        measureIdVal.add(measueId);
        ArrayList QueryCols = new ArrayList();
        ArrayList QueryAggs = new ArrayList();
        PbReturnObject pbretObjTime = null;
        StringBuilder mestext = new StringBuilder();

        HashMap oneviewHashMap = null;
        OnceViewContainer onecontainer = new OnceViewContainer();
        oneviewHashMap = (HashMap) session.getAttribute("ONEVIEWDETAILS");
        OneViewLetDetails detail = null;
        if (oneviewHashMap != null) {
            onecontainer = (OnceViewContainer) oneviewHashMap.get(oneViewId);
            detail = new OneViewLetDetails();
            detail = onecontainer.onviewLetdetails.get(Integer.parseInt(request.getParameter("divId")));
            detail.setRepId(measueId);
//           detail.setGrapId(graphId);
            detail.setRepName(measueName);
            detail.setReptype(measureType);
            detail.setRoleId(busrole);
            detail.setWidth(Integer.parseInt(width));
            detail.setHeight(Integer.parseInt(height));
            detail.setNewTypeMeasure(false);
            detail.setCurrValue(null);
            detail.setPriorValue(null);

            DashboardTemplateDAO daofrmsrattr = new DashboardTemplateDAO();

            HashMap<String, String> modifyMeasureAttr = daofrmsrattr.getModifyMeasureattr(measueId);
            if (modifyMeasureAttr != null) {
                String round = modifyMeasureAttr.get("round");
                if (round == null || round.equalsIgnoreCase("")) {
                    round = "0";
                }
                detail.setRoundVal(round);
                String symbols = modifyMeasureAttr.get("symbols");
                if (symbols == null) {
                    symbols = "";
                }
                detail.setPrefixValue(symbols);
                String no_format = modifyMeasureAttr.get("no_format");
                if (no_format == null) {
                    no_format = "";
                }
                detail.setFormatVal(no_format);
                String suffix_symbol = modifyMeasureAttr.get("suffix_symbol");
                if (suffix_symbol == null || suffix_symbol.equalsIgnoreCase("NONE")) {
                    suffix_symbol = "";
                }
                detail.setSuffixValue(suffix_symbol);
                String measureType1 = modifyMeasureAttr.get("measureType");
                if (measureType1.equalsIgnoreCase("none") || measureType1.equalsIgnoreCase("")) {
                    measureType1 = "Standard";
                }
                detail.setMeasType(measureType1);
            } else {


                detail.setRoundVal("0");
                detail.setMeasType("Standard");
                detail.setFormatVal("");
            }
            detail.setDisplayType(null);
            detail.setUserId(null);
            detail.setUserName(null);
            detail.setCommentData(null);
            detail.setTrendGraph(false);
            if (detail.getMeasurDrill() != null) {
                detail.setMeasurDrill(null);
            }
        } else {


            ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();
//                onecontainer = reportTemplateDAO.getOneViewData(oneViewId);
            // String fileName = reportTemplateDAO.getOneviewFileName(oneViewId);
            String fileName = session.getAttribute("tempFileName").toString();
            FileInputStream fis2 = new FileInputStream(advHtmlFileProps + "/" + fileName);
            ObjectInputStream ois2 = new ObjectInputStream(fis2);
            onecontainer = (OnceViewContainer) ois2.readObject();
            ois2.close();
            detail = onecontainer.onviewLetdetails.get(Integer.parseInt(request.getParameter("divId")));
            detail.setRepId(measueId);
//           detail.setGrapId(graphId);
            detail.setRepName(measueName);
            detail.setReptype(measureType);
            detail.setNewTypeMeasure(false);
            detail.setCurrValue(null);
            detail.setPriorValue(null);
            DashboardTemplateDAO daofrmsrattr = new DashboardTemplateDAO();
            HashMap<String, String> modifyMeasureAttr = daofrmsrattr.getModifyMeasureattr(measueId);
            if (modifyMeasureAttr != null) {
                String round = modifyMeasureAttr.get("round");
                if (round == null || round.equalsIgnoreCase("")) {
                    round = "0";
                }
                detail.setRoundVal(round);
                String symbols = modifyMeasureAttr.get("symbols");
                if (symbols == null) {
                    symbols = "";
                }
                detail.setPrefixValue(symbols);
                String no_format = modifyMeasureAttr.get("no_format");
                if (no_format == null) {
                    no_format = "";
                }
                detail.setFormatVal(no_format);
                String suffix_symbol = modifyMeasureAttr.get("suffix_symbol");
                if (suffix_symbol == null || suffix_symbol.equalsIgnoreCase("NONE") || suffix_symbol.equalsIgnoreCase("")) {
                    suffix_symbol = "";
                }
                detail.setSuffixValue(suffix_symbol);
                String measureType1 = modifyMeasureAttr.get("measureType");
                if (measureType1.equalsIgnoreCase("none") || measureType1.equalsIgnoreCase("")) {
                    measureType1 = "Standard";
                }
                detail.setMeasType(measureType1);
            } else {


                detail.setRoundVal("0");
                detail.setMeasType("Standard");
                detail.setFormatVal("");
            }
//            detail.setRoundVal("0");
//            detail.setMeasType("Standard");
//            detail.setFormatVal("");
            detail.setUserId(null);
            detail.setUserName(null);
            detail.setDisplayType(null);
            detail.setCommentData(null);
            detail.setTrendGraph(false);
            detail.setRoleId(busrole);
            detail.setHeight(Integer.parseInt(height));
            detail.setWidth(Integer.parseInt(width));
            detail.setRoleId(busrole);
            if (detail.getMeasurDrill() != null) {
                detail.setMeasurDrill(null);
            }
            HashMap tempRegHashMap = onecontainer.getTempRegHashMap();
            if (tempRegHashMap != null) {
                String tempRegFileName = (String) tempRegHashMap.get(Integer.parseInt(detail.getNoOfViewLets()));
                if (tempRegFileName == null) {
                    tempRegFileName = "InnerRegionDetails" + oneViewId + "_" + viewletId + "_" + session.getId() + "_" + System.currentTimeMillis() + ".txt";
                    tempRegHashMap.put(viewletId, tempRegFileName);
                }
            }
//              reportTemplateDAO.updateOneviewData(onecontainer,oneViewId);
            DashboardTemplateBD dashboardTemplateBD = new DashboardTemplateBD();
            OneViewBD oneViewBD = new OneViewBD();
            String result = "";
            request.setAttribute("isaddingmeasure", "true");
            result = dashboardTemplateBD.getMeasureDetailsData(request, response, session, onecontainer, detail);
            oneViewBD.writeOneviewRegData(onecontainer, result, detail.getNoOfViewLets(), request);
            FileOutputStream fos = new FileOutputStream(advHtmlFileProps + "/" + fileName);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(onecontainer);
            oos.flush();
            oos.close();
        }

//           detail.setHeight(Integer.parseInt(colSp));
//           detail.setHeight(Integer.parseInt(rowSp));
        if (onecontainer.timedetails != null && !onecontainer.timedetails.isEmpty()) {
            request.setAttribute("OneviewTiemDetails", onecontainer.timedetails);
        }

        double currVal = 0.0;
        double priorVal = 0.0;
        double changePer = 0.0;
        if (session != null && !request.getParameter("divId").equalsIgnoreCase("undefined")) {
            String userId = String.valueOf(session.getAttribute("USERID"));
            List<KPIElement> kpiElements = dao.getKPIElements(measureIdVal, new HashMap<String, String>());
            if (kpiElements != null) {
                for (KPIElement elem : kpiElements) {
                    if (elem.getElementName() != null) {
                        QueryCols.add(elem.getElementId());
                    }
                    QueryAggs.add(elem.getAggregationType());
                }
            }
            if (!String.valueOf(QueryAggs.get(0)).equalsIgnoreCase("null")) {
                pbretObjTime = detail.getPbReturnObject();
//                pbretObjTime = dao.getReturnObjectForOneView(QueryCols, QueryAggs, userId, busrole, request,onecontainer,detail);
            }
//           String filePath = "/usr/local/cache/oneviewmesureGO";
//             File file = new File(filePath);
//              String path = file.getAbsolutePath();
//              File f = new File(path);
//             if(file.exists()){
//
//             }else{
//                     f.mkdirs();
//             }
//              filePath = path+File.separator+onecontainer.oneviewName+"_"+onecontainer.oneviewId;
//               File file1 = new File(filePath);
//              if(file1.exists()){
// filePath = filePath+File.separator+onecontainer.oneviewName+"_"+detail.getNoOfViewLets()+"_"+measueId;
//   File measureGO = new File(filePath);
//      if(measureGO.exists()){
//
//      }else{
//         measureGO.createNewFile();
//      }
//             }else{
//                    file1.mkdirs();
//                     filePath = filePath+File.separator+onecontainer.oneviewName+"_"+detail.getNoOfViewLets()+"_"+measueId;
//                     file1 = new File(filePath);
//                      file1.createNewFile();
//            }
//                     HashMap<String,HashMap<String,String>> chartData12 = new HashMap<String,HashMap<String,String>>();
//                     HashMap<String,String> valuesmeasure = new HashMap<String,String>();
//                    ArrayList chartJson = new ArrayList();
//                     currVal = Double.parseDouble((pbretObjTime.getFieldValueString(0, ("A_" + QueryCols.get(0)).toString())));
//                      priorVal = Double.parseDouble((pbretObjTime.getFieldValueString(0, ("A_" + QueryCols.get(1)).toString())));
//                      changePer = Double.parseDouble((pbretObjTime.getFieldValueString(0, ("A_" + QueryCols.get(3)).toString())));
//                       chartJson.add(currVal);
//                       chartJson.add(priorVal);
//                       chartJson.add(changePer);
//
//                       valuesmeasure.put("currVal", String.valueOf(currVal));
//                       valuesmeasure.put("priorVal", String.valueOf(priorVal));
//                       valuesmeasure.put("changePer", String.valueOf(changePer));
// Gson gson = new Gson();
//  FileReadWrite fileReadWrite = new FileReadWrite();
//                      chartData12.put("region"+detail.getNoOfViewLets(),valuesmeasure);
//                       String  ab = gson.toJson(chartData12);
//
//        fileReadWrite.writeToFile(filePath,ab);

//            String curVal=pbretObjTime.getFieldValueString(0, ("A_"+QueryCols.get(0)).toString());
//            String priorVal=pbretObjTime.getFieldValueString(0, ("A_"+QueryCols.get(1)).toString());
            if (QueryCols.size() == 4) {
                if (pbretObjTime.getFieldValueString(0, ("A_" + QueryCols.get(1)).toString()) != null && !pbretObjTime.getFieldValueString(0, ("A_" + QueryCols.get(0)).toString()).equalsIgnoreCase("")) {
                    currVal = Double.parseDouble((pbretObjTime.getFieldValueString(0, ("A_" + QueryCols.get(0)).toString())));
                }
                if (pbretObjTime.getFieldValueString(0, ("A_" + QueryCols.get(1)).toString()) != null && !pbretObjTime.getFieldValueString(0, ("A_" + QueryCols.get(1)).toString()).equalsIgnoreCase("")) {
                    priorVal = Double.parseDouble((pbretObjTime.getFieldValueString(0, ("A_" + QueryCols.get(1)).toString())));
                }
                if (pbretObjTime.getFieldValueString(0, ("A_" + QueryCols.get(3)).toString()) != null && !pbretObjTime.getFieldValueString(0, ("A_" + QueryCols.get(3)).toString()).equalsIgnoreCase("")) {
                    changePer = Double.parseDouble((pbretObjTime.getFieldValueString(0, ("A_" + QueryCols.get(3)).toString())));
                }
                int decimalPlaces = 1;
                BigDecimal curval = new BigDecimal(currVal);
                BigDecimal prior = new BigDecimal(priorVal);
                BigDecimal chper = new BigDecimal(changePer);
                curval = curval.setScale(decimalPlaces, BigDecimal.ROUND_HALF_UP);
                prior = prior.setScale(decimalPlaces, BigDecimal.ROUND_HALF_UP);
                chper = chper.setScale(decimalPlaces, BigDecimal.ROUND_HALF_UP);
                NumberFormat formatter = NumberFormat.getInstance(new Locale("en_US"));
                String context = request.getContextPath();
                String currValue = formatter.format(curval);
                String priorValue = formatter.format(prior);
                String chengPer = formatter.format(chper);
                String currWidth = "";
                String priorwidth = "";
                String value = "";
                if (currVal > priorVal) {
                    currWidth = "100";
                    value = Integer.toString((int) currVal);
                    if (!value.substring(0, 1).equalsIgnoreCase("-")) {
                        priorwidth = Integer.toString((int) (priorVal / currVal * 100));
                    } else {
                        priorwidth = Integer.toString((int) (currVal / priorVal * 100)).replace("-", "");
                    }
                } else if (currVal == 0.0 && priorVal == 0.0) {
                    priorwidth = "0";
                    currWidth = "0";
                } else if (currVal == priorVal) {
                    priorwidth = "100";
                    currWidth = "100";
                } else if (currVal < 0.0 && priorVal == 0.0) {
                    priorwidth = "100";
                    value = Integer.toString((int) currVal);
                    if (!value.substring(0, 1).equalsIgnoreCase("-")) {
                        currWidth = Integer.toString((int) (currVal / priorVal * 100));
                    } else {
                        currWidth = Integer.toString((int) (priorVal / currVal * 100)).replace("-", "");
                    }
                } else {
                    priorwidth = "100";
                    value = Integer.toString((int) currVal);
                    if (!value.substring(0, 1).equalsIgnoreCase("-")) {
                        currWidth = Integer.toString((int) (currVal / priorVal * 100));
                    } else {
                        currWidth = Integer.toString((int) (priorVal / currVal * 100)).replace("-", "");
                    }
                }
//            StringBuilder measureText = new StringBuilder();
//            measureText.append("{ MeasureValues :[");
//            measureText.append("\"").append(currValue).append("\"").append(",");
//            measureText.append("\"").append(priorValue).append("\"").append(",");
//            measureText.append("\"").append(chengPer).append("\"").append("] }");
                //"+Integer.parseInt(width)/2+"
                int val = Integer.parseInt(request.getParameter("divId"));
//           if(currVal>priorVal){
//                    if(changePer>0.0)
//                            mestext.append("<center><table align='center' style='overflow:auto;' height='"+Integer.parseInt(height)+"px' width='"+Integer.parseInt(width)+"px'><tr><td id='currVal"+val+"' colspan='1'  style='font-size:20pt'  >"+formatter.format(curval)+"</td><td colspan='1' align='right'><img  style='border-left:medium hidden;border-top:medium hidden;' width='80px' height='80px' id='imgId"+val+"' src=\""+context+"/images/Red Arrow.jpeg\"  onmouseout=\"return nd()\" onmouseover=\"return overlib('("+measueName+" , Prior "+measueName+"="+formatter.format(curval)+"  ,  "+formatter.format(prior)+")')\"/></td></tr><tr><td colspan='2' height='"+Integer.parseInt(height)/2+"px' width='"+Integer.parseInt(width)+"px' align='left'><table ><tr><td  width='"+(Integer.parseInt(width)-120)+"px'></td><td id='addCommentsId' style='' width='100px'><a title='Add Comments' onclick=\"measureComments('"+Integer.parseInt(request.getParameter("divId"))+"','"+oneViewId+"')\" href='javascript:void(0)'>Add Comments</a></td><td id='viewCommentsId' style='display:none;' width='3%'><a onclick=\"measureComments('"+Integer.parseInt(request.getParameter("divId"))+"','"+oneViewId+"')\" title='ViewComments' href='javascript:void(0)'>View Comments</a></td><td width='3%'><a class='ui-icon ui-icon-image'  style='text-decoration: none;' onclick=\"measureOptions('"+Integer.parseInt(request.getParameter("divId"))+"','"+oneViewId+"','"+formatter.format(curval)+"')\" title='MeasuresOptions' href='javascript:void(0)'></a></td></tr></table><table width='"+Integer.parseInt(width)+"px'  border='1'  style='border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;'><tr><td id='measureDescId"+val+"' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000'>"+measueName+" Increased by "+chper+"%</td><td style='display:none'><input type='text' id='measureValue"+val+"' name='' value='"+measueName + "Increased by  " + chper + "%' ></td></tr></table></td></tr></table></center>");
//                    else
//                        mestext.append("<center><table align='center' style='overflow:auto;' height='"+Integer.parseInt(height)+"px' width='"+Integer.parseInt(width)+"px'><tr><td id='currVal"+val+"' colspan='1'  style='font-size:20pt'  >"+formatter.format(curval)+"</td><td colspan='1' align='right'><img  style='border-left:medium hidden;border-top:medium hidden;' width='80px' height='80px' id='imgId"+val+"' src=\""+context+"/images/Green Arrow.jpg\"  onmouseout=\"return nd()\" onmouseover=\"return overlib('("+measueName+" , Prior "+measueName+"="+formatter.format(curval)+"  ,  "+formatter.format(prior)+")')\"/></td></tr><tr><td colspan='2' height='"+Integer.parseInt(height)/2+"px' width='"+Integer.parseInt(width)+"px' align='left'><table ><tr><td  width='"+(Integer.parseInt(width)-120)+"px'></td><td id='addCommentsId' style='' width='100px'><a title='Add Comments' onclick=\"measureComments('"+Integer.parseInt(request.getParameter("divId"))+"','"+oneViewId+"')\" href='javascript:void(0)'>Add Comments</a></td><td id='viewCommentsId' style='display:none;' width='3%'><a onclick=\"measureComments('"+Integer.parseInt(request.getParameter("divId"))+"','"+oneViewId+"')\" title='ViewComments' href='javascript:void(0)'>View Comments</a></td><td width='3%'><a class='ui-icon ui-icon-image'  style='text-decoration: none;' onclick=\"measureOptions('"+Integer.parseInt(request.getParameter("divId"))+"','"+oneViewId+"','"+formatter.format(curval)+"')\" title='MeasuresOptions' href='javascript:void(0)'></a></td></tr></table><table width='"+Integer.parseInt(width)+"px'  border='1'  style='border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;'><tr><td id='measureDescId"+val+"' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000'>"+measueName+" Increased by "+chper+"%</td><td style='display:none'><input type='text' id='measureValue"+val+"' name='' value='"+measueName + "Increased by  " + chper + "%' ></td></tr></table></td></tr></table></center>");
//                 }
//            else{
                if (changePer == 0.0) {
//                     mestext.append("<center><table align='' style='overflow:auto;' height='" + Integer.parseInt(height) + "px' width='" + Integer.parseInt(width) + "px'><tr id='dynamicMeasId" + val + "' style='display:none'><td align=''><table><tr><td><table><tr><td  >MTD</td></tr></table></td><td><table><tr><td width='" + currWidth + "'  style='color: rgb(255, 255, 255); background-color: rgb(0, 149, 182);'></td><td id='currValue" + val + "' >" + formatter.format(curval) + "</td></tr></table></td></tr><tr><td><table><tr><td  >Last Period</td></tr></table></td><td><table><tr><td width='" + priorwidth + "'  style='color: rgb(255, 255, 255); background-color: rgb(154, 205, 50);'></td><td id='priorValue" + val + "' '>" + formatter.format(prior) + "</td></tr></table></td></tr></table></td></tr><tr id='measureTypeId" + val + "' style='display: ;'><td id='currVal" + val + "' colspan='1'   style='font-size:30pt' ><a onclick=\"getMeasureGraph('" + measueId + "','" + measueName + "','" + oneViewId + "','" + busrole + "')\" href='javascript:void(0)'>" + formatter.format(curval) + "</a></td><td colspan='1' align='right'></td></tr><tr><td colspan='2' height='" + Integer.parseInt(height) / 2 + "px' width='" + Integer.parseInt(width) + "px' align='left'><table width='" + Integer.parseInt(width) + "px'><tr><td width='" + Integer.parseInt(width) + "px'></td><td id='addCommentsId' style='' width=''><a title='Add/View Comments' onclick=\"measureComments('" + Integer.parseInt(request.getParameter("divId")) + "','" + oneViewId + "')\" href='javascript:void(0)' class='ui-icon ui-icon-comment'></a></td><td ><a class='ui-icon ui-icon-image'  style='text-decoration: none;' onclick=\"measureOptions('" + Integer.parseInt(request.getParameter("divId")) + "','" + oneViewId + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + measueId + "','" + measueName + "','" + formatter.format(prior) + "')\" title='MeasuresOptions' href='javascript:void(0)'></a></td><td><a class='ui-icon ui-icon-disk'  style='text-decoration: none;' onclick=\"taggleNewMeasures('" + Integer.parseInt(request.getParameter("divId")) + "','" + oneViewId + "','" + formatter.format(curval) + "','" + formatter.format(prior) + "','" + measueId + "','" + measueName + "')\" title='NewMeasureType' href='javascript:void(0)'></a></td></tr></table><table width='" + Integer.parseInt(width) + "px' border='1' style='border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;'><tr><td id='measureDescId" + val + "' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;font-size:8pt;'>" + measueName + " Compare With Last Period</td><td style='display:none'><input type='text' id='measureValue" + val + "' name='' value='" + measueName + " Compare With Last Period' ></td></tr></table></td></tr></table></center>");
//                     mestext.append("<center><table align='' style='overflow:auto;' height='" + Integer.parseInt(height) + "px' width='" + Integer.parseInt(width) + "px'><tr id='dynamicMeasId" + val + "' style='display:none'><td align=''><table><tr><td><table><tr><td  >MTD</td></tr></table></td><td><table><tr><td width='" + currWidth + "'  style='color: rgb(255, 255, 255); background-color: rgb(0, 149, 182);'></td><td id='currValue" + val + "' >" + formatter.format(curval) + "</td></tr></table></td></tr><tr><td><table><tr><td  >Last Period</td></tr></table></td><td><table><tr><td width='" + priorwidth + "'  style='color: rgb(255, 255, 255); background-color: rgb(154, 205, 50);'></td><td id='priorValue" + val + "' '>" + formatter.format(prior) + "</td></tr></table></td></tr></table></td></tr><tr id='measureTypeId" + val + "' style='display: ;'><td id='currVal" + val + "' colspan='1'   style='font-size:25pt' ><a onclick=\"getMeasureGraph('" + measueId + "','" + measueName + "','" + oneViewId + "','" + busrole + "')\" href='javascript:void(0)'>" + formatter.format(curval) + "</a></td><td colspan='1' align='right'></td></tr><tr><td colspan='2' height='" + Integer.parseInt(height) + "px' width='" + Integer.parseInt(width) + "px' align='left'><table width='" + Integer.parseInt(width) + "px'><tr><td width='" + Integer.parseInt(width) + "px'></td><td id='addCommentsId' style='' width=''><a title='Add/View Comments' onclick=\"measureComments('" + Integer.parseInt(request.getParameter("divId")) + "','" + oneViewId + "')\" href='javascript:void(0)' class='ui-icon ui-icon-comment'></a></td><td ><a class='ui-icon ui-icon-image'  style='text-decoration: none;' onclick=\"measureOptions('" + Integer.parseInt(request.getParameter("divId")) + "','" + oneViewId + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + measueId + "','" + measueName + "','" + formatter.format(prior) + "')\" title='MeasuresOptions' href='javascript:void(0)'></a></td><td><a class='ui-icon ui-icon-disk'  style='text-decoration: none;' onclick=\"taggleNewMeasures('" + Integer.parseInt(request.getParameter("divId")) + "','" + oneViewId + "','" + formatter.format(curval) + "','" + formatter.format(prior) + "','" + measueId + "','" + measueName + "')\" title='NewMeasureType' href='javascript:void(0)'></a></td></tr></table><table width='" + Integer.parseInt(width) + "px' border='1' style='border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;'><tr><td id='measureDescId" + val + "' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;font-size:8pt;'></td></tr></table></td></tr></table></center>");
                    if (oneviewtypedate != null && oneviewtypedate.equalsIgnoreCase("true")) {
                        mestext.append("<center><table align='' style='overflow:auto;' height='" + Integer.parseInt(height) + "px' ><tr id='dynamicMeasId" + val + "' style='display:none'><td align=''><table><tr><td><table><tr><td  >MTD</td></tr></table></td><td><table><tr><td width='" + currWidth + "'  style='color: rgb(255, 255, 255); background-color: rgb(0, 149, 182);'></td><td id='currValue" + val + "' >" + formatter.format(curval) + "</td></tr></table></td></tr><tr><td><table><tr><td  >Last Period</td></tr></table></td><td><table><tr><td width='" + priorwidth + "'  style='color: rgb(255, 255, 255); background-color: rgb(154, 205, 50);'></td><td id='priorValue" + val + "' '>" + formatter.format(prior) + "</td></tr></table></td></tr></table></td></tr><tr id='measureTypeId" + val + "' style='display: ;'><td id='currVal" + val + "' colspan='1'   style='font-size:18pt' ><a onclick=\"getMeasureGraph('" + measueId + "','" + measueName + "','" + oneViewId + "','" + busrole + "')\" href='javascript:void(0)'>" + formatter.format(curval) + "</a></td><td colspan='1' align='right'></td></tr><tr><td colspan='2' height='" + Integer.parseInt(height) + "px' width='" + Integer.parseInt(width) + "px' align='left'><table width='" + Integer.parseInt(width) + "px'><tr><td width='" + Integer.parseInt(width) + "px'></td><td style='display:none;'><a class='ui-icon ui-icon-image'  style='text-decoration: none;' onclick=\"measureOptions('" + Integer.parseInt(request.getParameter("divId")) + "','" + oneViewId + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + measueId + "','" + measueName + "','" + formatter.format(prior) + "')\" title='MeasuresOptions' href='javascript:void(0)'></a></td><td  style='display:none;'><a class='ui-icon ui-icon-disk'  style='text-decoration: none;' onclick=\"taggleNewMeasures('" + Integer.parseInt(request.getParameter("divId")) + "','" + oneViewId + "','" + formatter.format(curval) + "','" + formatter.format(prior) + "','" + measueId + "','" + measueName + "')\" title='NewMeasureType' href='javascript:void(0)'></a></td></tr></table><table width='" + Integer.parseInt(width) + "px' border='1' style='border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;'><tr><td id='measureDescId" + val + "' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;font-size:8pt;'></td></tr></table></td></tr></table></center>");
                    } else {
                        mestext.append("<center><table align='' style='overflow:auto;' height='" + Integer.parseInt(height) + "px' ><tr id='dynamicMeasId" + val + "' style='display:none'><td align=''><table><tr><td><table><tr><td  >MTD</td></tr></table></td><td><table><tr><td width='" + currWidth + "'  style='color: rgb(255, 255, 255); background-color: rgb(0, 149, 182);'></td><td id='currValue" + val + "' >" + formatter.format(curval) + "</td></tr></table></td></tr><tr><td><table><tr><td  >Last Period</td></tr></table></td><td><table><tr><td width='" + priorwidth + "'  style='color: rgb(255, 255, 255); background-color: rgb(154, 205, 50);'></td><td id='priorValue" + val + "' '>" + formatter.format(prior) + "</td></tr></table></td></tr></table></td></tr><tr id='measureTypeId" + val + "' style='display: ;'><td id='currVal" + val + "' colspan='1'   style='font-size:18pt' ><a onclick=\"getMeasureGraph('" + measueId + "','" + measueName + "','" + oneViewId + "','" + busrole + "')\" href='javascript:void(0)'>" + formatter.format(curval) + "</a></td><td colspan='1' align='right'></td></tr><tr><td colspan='2' height='" + Integer.parseInt(height) + "px' width='" + Integer.parseInt(width) + "px' align='left'><table width='" + Integer.parseInt(width) + "px'><tr><td width='" + Integer.parseInt(width) + "px'></td><td ><a class='ui-icon ui-icon-image'  style='text-decoration: none;' onclick=\"measureOptions('" + Integer.parseInt(request.getParameter("divId")) + "','" + oneViewId + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + measueId + "','" + measueName + "','" + formatter.format(prior) + "')\" title='MeasuresOptions' href='javascript:void(0)'></a></td><td><a class='ui-icon ui-icon-disk'  style='text-decoration: none;' onclick=\"taggleNewMeasures('" + Integer.parseInt(request.getParameter("divId")) + "','" + oneViewId + "','" + formatter.format(curval) + "','" + formatter.format(prior) + "','" + measueId + "','" + measueName + "')\" title='NewMeasureType' href='javascript:void(0)'></a></td></tr></table><table width='" + Integer.parseInt(width) + "px' border='1' style='border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;'><tr><td id='measureDescId" + val + "' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;font-size:8pt;'></td></tr></table></td></tr></table></center>");
                    }

                } else if (currVal == priorVal) {
//                    mestext.append("<center><table align='' style='overflow:auto;' height='" + Integer.parseInt(height) + "px' width='" + Integer.parseInt(width) + "px'><tr id='dynamicMeasId" + val + "' style='display:none'><td align=''><table><tr><td><table><tr><td  >MTD</td></tr></table></td><td><table><tr><td width='" + currWidth + "'  style='color: rgb(255, 255, 255); background-color: rgb(0, 149, 182);'></td><td id='currValue" + val + "' >" + formatter.format(curval) + "</td></tr></table></td></tr><tr><td><table><tr><td  >Last Period</td></tr></table></td><td><table><tr><td width='" + priorwidth + "'  style='color: rgb(255, 255, 255); background-color: rgb(154, 205, 50);'></td><td id='priorValue" + val + "' '>" + formatter.format(prior) + "</td></tr></table></td></tr></table></td></tr><tr id='measureTypeId" + val + "' style='display: ;'><td id='currVal" + val + "' colspan='1'   style='font-size:30pt' ><a onclick=\"getMeasureGraph('" + measueId + "','" + measueName + "','" + oneViewId + "','" + busrole + "')\" href='javascript:void(0)'>" + formatter.format(curval) + "</a></td><td colspan='1' align='right'></td></tr><tr><td colspan='2' height='" + Integer.parseInt(height) / 2 + "px' width='" + Integer.parseInt(width) + "px' align='left'><table width='" + Integer.parseInt(width) + "px'><tr><td width='" + Integer.parseInt(width) + "px'></td><td id='addCommentsId' style='' width=''><a title='Add/View Comments' onclick=\"measureComments('" + Integer.parseInt(request.getParameter("divId")) + "','" + oneViewId + "')\" href='javascript:void(0)' class='ui-icon ui-icon-comment'></a></td><td ><a class='ui-icon ui-icon-image'  style='text-decoration: none;' onclick=\"measureOptions('" + Integer.parseInt(request.getParameter("divId")) + "','" + oneViewId + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + measueId + "','" + measueName + "','" + formatter.format(prior) + "')\" title='MeasuresOptions' href='javascript:void(0)'></a></td><td><a class='ui-icon ui-icon-disk'  style='text-decoration: none;' onclick=\"taggleNewMeasures('" + Integer.parseInt(request.getParameter("divId")) + "','" + oneViewId + "','" + formatter.format(curval) + "','" + formatter.format(prior) + "','" + measueId + "','" + measueName + "')\" title='NewMeasureType' href='javascript:void(0)'></a></td></tr></table><table width='" + Integer.parseInt(width) + "px' border='1' style='border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;'><tr><td id='measureDescId" + val + "' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;font-size:8pt;'>" + measueName + " Compare With Last Period</td><td style='display:none'><input type='text' id='measureValue" + val + "' name='' value='" + measueName + " Compare With Last Period' ></td></tr></table></td></tr></table></center>");
//                    mestext.append("<center><table align='' style='overflow:auto;' height='" + Integer.parseInt(height) + "px' width='" + Integer.parseInt(width) + "px'><tr id='dynamicMeasId" + val + "' style='display:none'><td align=''><table><tr><td><table><tr><td  >MTD</td></tr></table></td><td><table><tr><td width='" + currWidth + "'  style='color: rgb(255, 255, 255); background-color: rgb(0, 149, 182);'></td><td id='currValue" + val + "' >" + formatter.format(curval) + "</td></tr></table></td></tr><tr><td><table><tr><td  >Last Period</td></tr></table></td><td><table><tr><td width='" + priorwidth + "'  style='color: rgb(255, 255, 255); background-color: rgb(154, 205, 50);'></td><td id='priorValue" + val + "' '>" + formatter.format(prior) + "</td></tr></table></td></tr></table></td></tr><tr id='measureTypeId" + val + "' style='display: ;'><td id='currVal" + val + "' colspan='1'   style='font-size:25pt' ><a onclick=\"getMeasureGraph('" + measueId + "','" + measueName + "','" + oneViewId + "','" + busrole + "')\" href='javascript:void(0)'>" + formatter.format(curval) + "</a></td><td colspan='1' align='right'></td></tr><tr><td colspan='2' height='" + Integer.parseInt(height) + "px' width='" + Integer.parseInt(width) + "px' align='left'><table width='" + Integer.parseInt(width) + "px'><tr><td width='" + Integer.parseInt(width) + "px'></td><td id='addCommentsId' style='' width=''><a title='Add/View Comments' onclick=\"measureComments('" + Integer.parseInt(request.getParameter("divId")) + "','" + oneViewId + "')\" href='javascript:void(0)' class='ui-icon ui-icon-comment'></a></td><td ><a class='ui-icon ui-icon-image'  style='text-decoration: none;' onclick=\"measureOptions('" + Integer.parseInt(request.getParameter("divId")) + "','" + oneViewId + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + measueId + "','" + measueName + "','" + formatter.format(prior) + "')\" title='MeasuresOptions' href='javascript:void(0)'></a></td><td><a class='ui-icon ui-icon-disk'  style='text-decoration: none;' onclick=\"taggleNewMeasures('" + Integer.parseInt(request.getParameter("divId")) + "','" + oneViewId + "','" + formatter.format(curval) + "','" + formatter.format(prior) + "','" + measueId + "','" + measueName + "')\" title='NewMeasureType' href='javascript:void(0)'></a></td></tr></table><table width='" + Integer.parseInt(width) + "px' border='1' style='border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;'><tr><td id='measureDescId" + val + "' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;font-size:8pt;'></td></tr></table></td></tr></table></center>");
                    mestext.append("<center><table align='' style='overflow:auto;' height='" + Integer.parseInt(height) + "px' ><tr id='dynamicMeasId" + val + "' style='display:none'><td align=''><table><tr><td><table><tr><td  >MTD</td></tr></table></td><td><table><tr><td width='" + currWidth + "'  style='color: rgb(255, 255, 255); background-color: rgb(0, 149, 182);'></td><td id='currValue" + val + "' >" + formatter.format(curval) + "</td></tr></table></td></tr><tr><td><table><tr><td  >Last Period</td></tr></table></td><td><table><tr><td width='" + priorwidth + "'  style='color: rgb(255, 255, 255); background-color: rgb(154, 205, 50);'></td><td id='priorValue" + val + "' '>" + formatter.format(prior) + "</td></tr></table></td></tr></table></td></tr><tr id='measureTypeId" + val + "' style='display: ;'><td id='currVal" + val + "' colspan='1'   style='font-size:18pt' ><a onclick=\"getMeasureGraph('" + measueId + "','" + measueName + "','" + oneViewId + "','" + busrole + "')\" href='javascript:void(0)'>" + formatter.format(curval) + "</a></td><td colspan='1' align='right'></td></tr><tr><td colspan='2' height='" + Integer.parseInt(height) + "px' width='" + Integer.parseInt(width) + "px' align='left'><table width='" + Integer.parseInt(width) + "px'><tr><td width='" + Integer.parseInt(width) + "px'></td><td  style='display:none;'><a class='ui-icon ui-icon-image'  style='text-decoration: none;' onclick=\"measureOptions('" + Integer.parseInt(request.getParameter("divId")) + "','" + oneViewId + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + measueId + "','" + measueName + "','" + formatter.format(prior) + "')\" title='MeasuresOptions' href='javascript:void(0)'></a></td><td  style='display:none;'><a class='ui-icon ui-icon-disk'  style='text-decoration: none;' onclick=\"taggleNewMeasures('" + Integer.parseInt(request.getParameter("divId")) + "','" + oneViewId + "','" + formatter.format(curval) + "','" + formatter.format(prior) + "','" + measueId + "','" + measueName + "')\" title='NewMeasureType' href='javascript:void(0)'></a></td></tr></table><table width='" + Integer.parseInt(width) + "px' border='1' style='border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;'><tr><td id='measureDescId" + val + "' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;font-size:8pt;'></td></tr></table></td></tr></table></center>");
                    mestext.append("<center><table align='' style='overflow:auto;' height='" + Integer.parseInt(height) + "px' ><tr id='dynamicMeasId" + val + "' style='display:none'><td align=''><table><tr><td><table><tr><td  >MTD</td></tr></table></td><td><table><tr><td width='" + currWidth + "'  style='color: rgb(255, 255, 255); background-color: rgb(0, 149, 182);'></td><td id='currValue" + val + "' >" + formatter.format(curval) + "</td></tr></table></td></tr><tr><td><table><tr><td  >Last Period</td></tr></table></td><td><table><tr><td width='" + priorwidth + "'  style='color: rgb(255, 255, 255); background-color: rgb(154, 205, 50);'></td><td id='priorValue" + val + "' '>" + formatter.format(prior) + "</td></tr></table></td></tr></table></td></tr><tr id='measureTypeId" + val + "' style='display: ;'><td id='currVal" + val + "' colspan='1'   style='font-size:18pt' ><a onclick=\"getMeasureGraph('" + measueId + "','" + measueName + "','" + oneViewId + "','" + busrole + "')\" href='javascript:void(0)'>" + formatter.format(curval) + "</a></td><td colspan='1' align='right'></td></tr><tr><td colspan='2' height='" + Integer.parseInt(height) + "px' width='" + Integer.parseInt(width) + "px' align='left'><table width='" + Integer.parseInt(width) + "px'><tr><td width='" + Integer.parseInt(width) + "px'></td><td ><a class='ui-icon ui-icon-image'  style='text-decoration: none;' onclick=\"measureOptions('" + Integer.parseInt(request.getParameter("divId")) + "','" + oneViewId + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + measueId + "','" + measueName + "','" + formatter.format(prior) + "')\" title='MeasuresOptions' href='javascript:void(0)'></a></td><td><a class='ui-icon ui-icon-disk'  style='text-decoration: none;' onclick=\"taggleNewMeasures('" + Integer.parseInt(request.getParameter("divId")) + "','" + oneViewId + "','" + formatter.format(curval) + "','" + formatter.format(prior) + "','" + measueId + "','" + measueName + "')\" title='NewMeasureType' href='javascript:void(0)'></a></td></tr></table><table width='" + Integer.parseInt(width) + "px' border='1' style='border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;'><tr><td id='measureDescId" + val + "' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;font-size:8pt;'></td></tr></table></td></tr></table></center>");

                } else if (changePer < 0.0) {
//                    mestext.append("<center><table align='center' style='overflow:auto;' height='" + Integer.parseInt(height) + "px' width='" + Integer.parseInt(width) + "px'><tr id='dynamicMeasId" + val + "' style='display:none'><td align='center'><table><tr><td><table><tr><td width='70'>MTD</td></tr></table></td><td><table><tr><td width='" + currWidth + "' style='color: rgb(255, 255, 255); background-color: rgb(0, 149, 182);'></td><td id='currValue" + val + "'>" + formatter.format(curval) + "</td></tr></table></td></tr><tr><td><table><tr><td width='70'>Last Period</td></tr></table></td><td><table><tr><td width='" + priorwidth + "' style='color: rgb(255, 255, 255); background-color: rgb(154, 205, 50);'></td><td id='priorValue" + val + "'>" + formatter.format(prior) + "</td></tr></table></td></tr></table></td></tr><tr id='measureTypeId" + val + "' style='display: ;'><td id='currVal" + val + "' colspan='1'   style='font-size:18pt' ><a onclick=\"getMeasureGraph('" + measueId + "','" + measueName + "','" + oneViewId + "','" + busrole + "')\" href='javascript:void(0)'>" + formatter.format(curval) + "</a></td><td colspan='1' align='right'><img  style='border-left:medium hidden;border-top:medium hidden;' width='80px' height='80px' id='imgId" + val + "' src=\"" + context + "/images/Red Arrow.jpeg\"  onmouseout=\"return nd()\" onmouseover=\"return overlib('(" + measueName + " , Prior " + measueName + "=" + formatter.format(curval) + "  ,  " + formatter.format(prior) + ")')\"/></td></tr><tr><td colspan='2' height='" + Integer.parseInt(height) / 2 + "px' width='" + Integer.parseInt(width) + "px' align='left'><table width='" + Integer.parseInt(width) + "px'><tr><td width='" + Integer.parseInt(width) + "px'></td><td id='addCommentsId' style='' width=''><a title='Add/View Comments' onclick=\"measureComments('" + Integer.parseInt(request.getParameter("divId")) + "','" + oneViewId + "')\" href='javascript:void(0)' class='ui-icon ui-icon-comment'></a></td><td ><a class='ui-icon ui-icon-image'  style='text-decoration: none;' onclick=\"measureOptions('" + Integer.parseInt(request.getParameter("divId")) + "','" + oneViewId + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + measueId + "','" + measueName + "','" + formatter.format(prior) + "')\" title='MeasuresOptions' href='javascript:void(0)'></a></td><td><a class='ui-icon ui-icon-disk'  style='text-decoration: none;' onclick=\"taggleNewMeasures('" + Integer.parseInt(request.getParameter("divId")) + "','" + oneViewId + "','" + formatter.format(curval) + "','" + formatter.format(prior) + "','" + measueId + "','" + measueName + "')\" title='NewMeasureType' href='javascript:void(0)'></a></td></tr></table><table width='" + Integer.parseInt(width) + "px' border='1' style='border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;'><tr><td id='measureDescId" + val + "' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#FF0000;font-size:8pt;'>" + measueName + " Decreased by " + chper + "% Compare With Last Period</td><td style='display:none'><input type='text' id='measureValue" + val + "' name='' value='" + measueName + "Decreased by  " + chper + "% Compare With Last Period' ></td></tr></table></td></tr></table></center>");
//                    mestext.append("<center><table align='' style='overflow:auto;' height='" + Integer.parseInt(height) + "px' width='" + Integer.parseInt(width) + "px'><tr id='dynamicMeasId" + val + "' style='display:none'><td align=''><table><tr><td><table><tr><td  '>MTD</td></tr></table></td><td><table><tr><td width='" + currWidth + "'  style='color: rgb(255, 255, 255); background-color: rgb(0, 149, 182);'></td><td id='currValue" + val + "' >" + formatter.format(curval) + "</td></tr></table></td></tr><tr><td><table><tr><td  >Last Period</td></tr></table></td><td><table><tr><td width='" + priorwidth + "'  style='color: rgb(255, 255, 255); background-color: rgb(154, 205, 50);'></td><td id='priorValue" + val + "' '>" + formatter.format(prior) + "</td></tr></table></td></tr></table></td></tr><tr id='measureTypeId" + val + "' style='display: ;'><td id='currVal" + val + "' colspan='1'   style='font-size:30pt' ><a onclick=\"getMeasureGraph('" + measueId + "','" + measueName + "','" + oneViewId + "','" + busrole + "')\" href='javascript:void(0)' style='color:#FF0000;'>" + formatter.format(curval) + "</a></td><td colspan='1' align='right'></td></tr><tr><td colspan='2' height='" + Integer.parseInt(height) / 2 + "px' width='" + Integer.parseInt(width) + "px' align='left'><table width='" + Integer.parseInt(width) + "px'><tr><td width='" + Integer.parseInt(width) + "px'></td><td id='addCommentsId' style='' width=''><a title='Add/View Comments' onclick=\"measureComments('" + Integer.parseInt(request.getParameter("divId")) + "','" + oneViewId + "')\" href='javascript:void(0)' class='ui-icon ui-icon-comment'></a></td><td ><a class='ui-icon ui-icon-image'  style='text-decoration: none;' onclick=\"measureOptions('" + Integer.parseInt(request.getParameter("divId")) + "','" + oneViewId + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + measueId + "','" + measueName + "','" + formatter.format(prior) + "')\" title='MeasuresOptions' href='javascript:void(0)'></a></td><td><a class='ui-icon ui-icon-disk'  style='text-decoration: none;' onclick=\"taggleNewMeasures('" + Integer.parseInt(request.getParameter("divId")) + "','" + oneViewId + "','" + formatter.format(curval) + "','" + formatter.format(prior) + "','" + measueId + "','" + measueName + "')\" title='NewMeasureType' href='javascript:void(0)'></a></td></tr></table><table width='" + Integer.parseInt(width) + "px' border='1' style='border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;'><tr><td id='measureDescId" + val + "' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#FF0000;font-size:8pt;'>" + measueName + " Decreased by " + chper + "% Compare With Last Period</td><td style='display:none'><input type='text' id='measureValue" + val + "' name='' value='" + measueName + "Decreased by  " + chper + "% Compare With Last Period' ></td></tr></table></td></tr></table></center>");
//                    mestext.append("<center><table align='' style='overflow:auto;' height='" + Integer.parseInt(height) + "px' width='" + Integer.parseInt(width) + "px'><tr id='dynamicMeasId" + val + "' style='display:none'><td align=''><table><tr><td><table><tr><td  '>MTD</td></tr></table></td><td><table><tr><td width='" + currWidth + "'  style='color: rgb(255, 255, 255); background-color: rgb(0, 149, 182);'></td><td id='currValue" + val + "' >" + formatter.format(curval) + "</td></tr></table></td></tr><tr><td><table><tr><td  >Last Period</td></tr></table></td><td><table><tr><td width='" + priorwidth + "'  style='color: rgb(255, 255, 255); background-color: rgb(154, 205, 50);'></td><td id='priorValue" + val + "' '>" + formatter.format(prior) + "</td></tr></table></td></tr></table></td></tr><tr id='measureTypeId" + val + "' style='display: ;'><td id='currVal" + val + "' colspan='1'   style='font-size:25pt' ><a onclick=\"getMeasureGraph('" + measueId + "','" + measueName + "','" + oneViewId + "','" + busrole + "')\" href='javascript:void(0)' >" + formatter.format(curval) + "</a></td><td colspan='1' align='right'></td></tr><tr><td colspan='2' height='" + Integer.parseInt(height)  + "px' width='" + Integer.parseInt(width) + "px' align='left'><table width='" + Integer.parseInt(width) + "px'><tr><td width='" + Integer.parseInt(width) + "px'></td><td id='addCommentsId' style='' width=''><a title='Add/View Comments' onclick=\"measureComments('" + Integer.parseInt(request.getParameter("divId")) + "','" + oneViewId + "')\" href='javascript:void(0)' class='ui-icon ui-icon-comment'></a></td><td ><a class='ui-icon ui-icon-image'  style='text-decoration: none;' onclick=\"measureOptions('" + Integer.parseInt(request.getParameter("divId")) + "','" + oneViewId + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + measueId + "','" + measueName + "','" + formatter.format(prior) + "')\" title='MeasuresOptions' href='javascript:void(0)'></a></td><td><a class='ui-icon ui-icon-disk'  style='text-decoration: none;' onclick=\"taggleNewMeasures('" + Integer.parseInt(request.getParameter("divId")) + "','" + oneViewId + "','" + formatter.format(curval) + "','" + formatter.format(prior) + "','" + measueId + "','" + measueName + "')\" title='NewMeasureType' href='javascript:void(0)'></a></td></tr></table><table width='" + Integer.parseInt(width) + "px' border='1' style='border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;'><tr><td id='measureDescId" + val + "' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;font-size:8pt;'></td></tr></table></td></tr></table></center>");
                    if (oneviewtypedate != null && oneviewtypedate.equalsIgnoreCase("true")) {
                        mestext.append("<center><table align='' style='overflow:auto;' height='" + Integer.parseInt(height) + "px' ><tr id='dynamicMeasId" + val + "' style='display:none'><td align=''><table><tr><td><table><tr><td  '>MTD</td></tr></table></td><td><table><tr><td width='" + currWidth + "'  style='color: rgb(255, 255, 255); background-color: rgb(0, 149, 182);'></td><td id='currValue" + val + "' >" + formatter.format(curval) + "</td></tr></table></td></tr><tr><td><table><tr><td  >Last Period</td></tr></table></td><td><table><tr><td width='" + priorwidth + "'  style='color: rgb(255, 255, 255); background-color: rgb(154, 205, 50);'></td><td id='priorValue" + val + "' '>" + formatter.format(prior) + "</td></tr></table></td></tr></table></td></tr><tr id='measureTypeId" + val + "' style='display: ;'><td id='currVal" + val + "' colspan='1'   style='font-size:18pt' ><a onclick=\"getMeasureGraph('" + measueId + "','" + measueName + "','" + oneViewId + "','" + busrole + "')\" href='javascript:void(0)' >" + formatter.format(curval) + "</a></td><td colspan='1' align='right'></td></tr><tr><td colspan='2' height='" + Integer.parseInt(height) + "px' width='" + Integer.parseInt(width) + "px' align='left'><table width='" + Integer.parseInt(width) + "px'><tr><td width='" + Integer.parseInt(width) + "px'></td><td  style='display:none;'><a class='ui-icon ui-icon-image'  style='text-decoration: none;' onclick=\"measureOptions('" + Integer.parseInt(request.getParameter("divId")) + "','" + oneViewId + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + measueId + "','" + measueName + "','" + formatter.format(prior) + "')\" title='MeasuresOptions' href='javascript:void(0)'></a></td><td  style='display:none;'><a class='ui-icon ui-icon-disk'  style='text-decoration: none;' onclick=\"taggleNewMeasures('" + Integer.parseInt(request.getParameter("divId")) + "','" + oneViewId + "','" + formatter.format(curval) + "','" + formatter.format(prior) + "','" + measueId + "','" + measueName + "')\" title='NewMeasureType' href='javascript:void(0)'></a></td></tr></table><table width='" + Integer.parseInt(width) + "px' border='1' style='border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;'><tr><td id='measureDescId" + val + "' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;font-size:8pt;'></td></tr></table></td></tr></table></center>");
                    } else {
                        mestext.append("<center><table align='' style='overflow:auto;' height='" + Integer.parseInt(height) + "px' ><tr id='dynamicMeasId" + val + "' style='display:none'><td align=''><table><tr><td><table><tr><td  '>MTD</td></tr></table></td><td><table><tr><td width='" + currWidth + "'  style='color: rgb(255, 255, 255); background-color: rgb(0, 149, 182);'></td><td id='currValue" + val + "' >" + formatter.format(curval) + "</td></tr></table></td></tr><tr><td><table><tr><td  >Last Period</td></tr></table></td><td><table><tr><td width='" + priorwidth + "'  style='color: rgb(255, 255, 255); background-color: rgb(154, 205, 50);'></td><td id='priorValue" + val + "' '>" + formatter.format(prior) + "</td></tr></table></td></tr></table></td></tr><tr id='measureTypeId" + val + "' style='display: ;'><td id='currVal" + val + "' colspan='1'   style='font-size:18pt' ><a onclick=\"getMeasureGraph('" + measueId + "','" + measueName + "','" + oneViewId + "','" + busrole + "')\" href='javascript:void(0)' >" + formatter.format(curval) + "</a></td><td colspan='1' align='right'></td></tr><tr><td colspan='2' height='" + Integer.parseInt(height) + "px' width='" + Integer.parseInt(width) + "px' align='left'><table width='" + Integer.parseInt(width) + "px'><tr><td width='" + Integer.parseInt(width) + "px'></td><td ><a class='ui-icon ui-icon-image'  style='text-decoration: none;' onclick=\"measureOptions('" + Integer.parseInt(request.getParameter("divId")) + "','" + oneViewId + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + measueId + "','" + measueName + "','" + formatter.format(prior) + "')\" title='MeasuresOptions' href='javascript:void(0)'></a></td><td><a class='ui-icon ui-icon-disk'  style='text-decoration: none;' onclick=\"taggleNewMeasures('" + Integer.parseInt(request.getParameter("divId")) + "','" + oneViewId + "','" + formatter.format(curval) + "','" + formatter.format(prior) + "','" + measueId + "','" + measueName + "')\" title='NewMeasureType' href='javascript:void(0)'></a></td></tr></table><table width='" + Integer.parseInt(width) + "px' border='1' style='border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;'><tr><td id='measureDescId" + val + "' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;font-size:8pt;'></td></tr></table></td></tr></table></center>");
                    }

                } else {
//                    mestext.append("<center><table align='center' style='overflow:auto;' height='" + Integer.parseInt(height) + "px' width='" + Integer.parseInt(width) + "px'><tr id='dynamicMeasId" + val + "' style='display:none'><td align='center'><table><tr><td><table><tr><td width='70'>MTD</td></tr></table></td><td><table><tr><td width='" + currWidth + "' style='color: rgb(255, 255, 255); background-color: rgb(0, 149, 182);'></td><td id='currValue" + val + "'>" + formatter.format(curval) + "</td></tr></table></td></tr><tr><td><table><tr><td width='70'>Last Period</td></tr></table></td><td><table><tr><td width='" + priorwidth + "' style='color: rgb(255, 255, 255); background-color: rgb(154, 205, 50);'></td><td id='priorValue" + val + "'>" + formatter.format(prior) + "</td></tr></table></td></tr></table></td></tr><tr id='measureTypeId" + val + "' style='display: ;'><td id='currVal" + val + "' colspan='1'   style='font-size:18pt' ><a onclick=\"getMeasureGraph('" + measueId + "','" + measueName + "','" + oneViewId + "','" + busrole + "')\" href='javascript:void(0)'>" + formatter.format(curval) + "</a></td><td colspan='1' align='right'><img  style='border-left:medium hidden;border-top:medium hidden;' width='80px' height='80px' id='imgId" + val + "' src=\"" + context + "/images/Green Arrow.jpg\"  onmouseout=\"return nd()\" onmouseover=\"return overlib('(" + measueName + " , Prior " + measueName + "=" + formatter.format(curval) + "  ,  " + formatter.format(prior) + ")')\"/></td></tr><tr><td colspan='2' height='" + Integer.parseInt(height) / 2 + "px' width='" + Integer.parseInt(width) + "px' align='left'><table width='" + Integer.parseInt(width) + "px'><tr><td width='" + Integer.parseInt(width) + "px'></td><td id='addCommentsId' style='' width=''><a title='Add/View Comments' onclick=\"measureComments('" + Integer.parseInt(request.getParameter("divId")) + "','" + oneViewId + "')\" href='javascript:void(0)' class='ui-icon ui-icon-comment'></a></td><td ><a class='ui-icon ui-icon-image'  style='text-decoration: none;' onclick=\"measureOptions('" + Integer.parseInt(request.getParameter("divId")) + "','" + oneViewId + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + measueId + "','" + measueName + "','" + formatter.format(prior) + "')\" title='MeasuresOptions' href='javascript:void(0)'></a></td><td><a class='ui-icon ui-icon-disk'  style='text-decoration: none;' onclick=\"taggleNewMeasures('" + Integer.parseInt(request.getParameter("divId")) + "','" + oneViewId + "','" + formatter.format(curval) + "','" + formatter.format(prior) + "','" + measueId + "','" + measueName + "')\" title='NewMeasureType' href='javascript:void(0)'></a></td></tr></table><table width='" + Integer.parseInt(width) + "px' border='1' style='border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;'><tr><td id='measureDescId" + val + "' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:8pt;'>" + measueName + " Increased by " + chper + "% Compare With Last Period</td><td style='display:none'><input type='text' id='measureValue" + val + "' name='' value='" + measueName + "Increased by  " + chper + "% Compare With Last Period' ></td></tr></table></td></tr></table></center>");
//                    mestext.append("<center><table align='' style='overflow:auto;' height='" + Integer.parseInt(height) + "px' width='" + Integer.parseInt(width) + "px'><tr id='dynamicMeasId" + val + "' style='display:none'><td align=''><table><tr><td><table><tr><td  >MTD</td></tr></table></td><td><table><tr><td width='" + currWidth + "'  style='color: rgb(255, 255, 255); background-color: rgb(0, 149, 182);'></td><td id='currValue" + val + "' >" + formatter.format(curval) + "</td></tr></table></td></tr><tr><td><table><tr><td >Last Period</td></tr></table></td><td><table><tr><td width='" + priorwidth + "'  style='color: rgb(255, 255, 255); background-color: rgb(154, 205, 50);'></td><td id='priorValue" + val + "' '>" + formatter.format(prior) + "</td></tr></table></td></tr></table></td></tr><tr id='measureTypeId" + val + "' style='display: ;'><td id='currVal" + val + "' colspan='1'   style='font-size:30pt' ><a onclick=\"getMeasureGraph('" + measueId + "','" + measueName + "','" + oneViewId + "','" + busrole + "')\" href='javascript:void(0)' style='color:#008000;'>" + formatter.format(curval) + "</a></td><td colspan='1' align='right'></td></tr><tr><td colspan='2' height='" + Integer.parseInt(height) / 2 + "px' width='" + Integer.parseInt(width) + "px' align='left'><table width='" + Integer.parseInt(width) + "px'><tr><td width='" + Integer.parseInt(width) + "px'></td><td id='addCommentsId' style='' width=''><a title='Add/View Comments' onclick=\"measureComments('" + Integer.parseInt(request.getParameter("divId")) + "','" + oneViewId + "')\" href='javascript:void(0)' class='ui-icon ui-icon-comment'></a></td><td ><a class='ui-icon ui-icon-image'  style='text-decoration: none;' onclick=\"measureOptions('" + Integer.parseInt(request.getParameter("divId")) + "','" + oneViewId + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + measueId + "','" + measueName + "','" + formatter.format(prior) + "')\" title='MeasuresOptions' href='javascript:void(0)'></a></td><td><a class='ui-icon ui-icon-disk'  style='text-decoration: none;' onclick=\"taggleNewMeasures('" + Integer.parseInt(request.getParameter("divId")) + "','" + oneViewId + "','" + formatter.format(curval) + "','" + formatter.format(prior) + "','" + measueId + "','" + measueName + "')\" title='NewMeasureType' href='javascript:void(0)'></a></td></tr></table><table width='" + Integer.parseInt(width) + "px' border='1' style='border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;'><tr><td id='measureDescId" + val + "' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:8pt;'>" + measueName + " Increased by " + chper + "% Compare With Last Period</td><td style='display:none'><input type='text' id='measureValue" + val + "' name='' value='" + measueName + "Increased by  " + chper + "% Compare With Last Period' ></td></tr></table></td></tr></table></center>");
//                    mestext.append("<center><table align='' style='overflow:auto;' height='" + Integer.parseInt(height) + "px' width='" + Integer.parseInt(width) + "px'><tr id='dynamicMeasId" + val + "' style='display:none'><td align=''><table><tr><td><table><tr><td  >MTD</td></tr></table></td><td><table><tr><td width='" + currWidth + "'  style='color: rgb(255, 255, 255); background-color: rgb(0, 149, 182);'></td><td id='currValue" + val + "' >" + formatter.format(curval) + "</td></tr></table></td></tr><tr><td><table><tr><td >Last Period</td></tr></table></td><td><table><tr><td width='" + priorwidth + "'  style='color: rgb(255, 255, 255); background-color: rgb(154, 205, 50);'></td><td id='priorValue" + val + "' '>" + formatter.format(prior) + "</td></tr></table></td></tr></table></td></tr><tr id='measureTypeId" + val + "' style='display: ;'><td id='currVal" + val + "' colspan='1'   style='font-size:25pt' ><a onclick=\"getMeasureGraph('" + measueId + "','" + measueName + "','" + oneViewId + "','" + busrole + "')\" href='javascript:void(0)' >" + formatter.format(curval) + "</a></td><td colspan='1' align='right'></td></tr><tr><td colspan='2' height='" + Integer.parseInt(height) + "px' width='" + Integer.parseInt(width) + "px' align='left'><table width='" + Integer.parseInt(width) + "px'><tr><td width='" + Integer.parseInt(width) + "px'></td><td id='addCommentsId' style='' width=''><a title='Add/View Comments' onclick=\"measureComments('" + Integer.parseInt(request.getParameter("divId")) + "','" + oneViewId + "')\" href='javascript:void(0)' class='ui-icon ui-icon-comment'></a></td><td ><a class='ui-icon ui-icon-image'  style='text-decoration: none;' onclick=\"measureOptions('" + Integer.parseInt(request.getParameter("divId")) + "','" + oneViewId + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + measueId + "','" + measueName + "','" + formatter.format(prior) + "')\" title='MeasuresOptions' href='javascript:void(0)'></a></td><td><a class='ui-icon ui-icon-disk'  style='text-decoration: none;' onclick=\"taggleNewMeasures('" + Integer.parseInt(request.getParameter("divId")) + "','" + oneViewId + "','" + formatter.format(curval) + "','" + formatter.format(prior) + "','" + measueId + "','" + measueName + "')\" title='NewMeasureType' href='javascript:void(0)'></a></td></tr></table><table width='" + Integer.parseInt(width) + "px' border='1' style='border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;'><tr><td id='measureDescId" + val + "' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;font-size:8pt;'></td></tr></table></td></tr></table></center>");
                    if (oneviewtypedate != null && oneviewtypedate.equalsIgnoreCase("true")) {
                        mestext.append("<center><table align='' style='overflow:auto;' height='" + Integer.parseInt(height) + "px' ><tr id='dynamicMeasId" + val + "' style='display:none'><td align=''><table><tr><td><table><tr><td  >MTD</td></tr></table></td><td><table><tr><td width='" + currWidth + "'  style='color: rgb(255, 255, 255); background-color: rgb(0, 149, 182);'></td><td id='currValue" + val + "' >" + formatter.format(curval) + "</td></tr></table></td></tr><tr><td><table><tr><td >Last Period</td></tr></table></td><td><table><tr><td width='" + priorwidth + "'  style='color: rgb(255, 255, 255); background-color: rgb(154, 205, 50);'></td><td id='priorValue" + val + "' '>" + formatter.format(prior) + "</td></tr></table></td></tr></table></td></tr><tr id='measureTypeId" + val + "' style='display: ;'><td id='currVal" + val + "' colspan='1'   style='font-size:18pt' ><a onclick=\"getMeasureGraph('" + measueId + "','" + measueName + "','" + oneViewId + "','" + busrole + "')\" href='javascript:void(0)' >" + formatter.format(curval) + "</a></td><td colspan='1' align='right'></td></tr><tr><td colspan='2' height='" + Integer.parseInt(height) + "px' width='" + Integer.parseInt(width) + "px' align='left'><table width='" + Integer.parseInt(width) + "px'><tr><td width='" + Integer.parseInt(width) + "px'></td><td  style='display:none;'><a class='ui-icon ui-icon-image'  style='text-decoration: none;' onclick=\"measureOptions('" + Integer.parseInt(request.getParameter("divId")) + "','" + oneViewId + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + measueId + "','" + measueName + "','" + formatter.format(prior) + "')\" title='MeasuresOptions' href='javascript:void(0)'></a></td><td  style='display:none;'><a class='ui-icon ui-icon-disk'  style='text-decoration: none;' onclick=\"taggleNewMeasures('" + Integer.parseInt(request.getParameter("divId")) + "','" + oneViewId + "','" + formatter.format(curval) + "','" + formatter.format(prior) + "','" + measueId + "','" + measueName + "')\" title='NewMeasureType' href='javascript:void(0)'></a></td></tr></table><table width='" + Integer.parseInt(width) + "px' border='1' style='border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;'><tr><td id='measureDescId" + val + "' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;font-size:8pt;'></td></tr></table></td></tr></table></center>");
                    } else {
                        mestext.append("<center><table align='' style='overflow:auto;' height='" + Integer.parseInt(height) + "px' ><tr id='dynamicMeasId" + val + "' style='display:none'><td align=''><table><tr><td><table><tr><td  >MTD</td></tr></table></td><td><table><tr><td width='" + currWidth + "'  style='color: rgb(255, 255, 255); background-color: rgb(0, 149, 182);'></td><td id='currValue" + val + "' >" + formatter.format(curval) + "</td></tr></table></td></tr><tr><td><table><tr><td >Last Period</td></tr></table></td><td><table><tr><td width='" + priorwidth + "'  style='color: rgb(255, 255, 255); background-color: rgb(154, 205, 50);'></td><td id='priorValue" + val + "' '>" + formatter.format(prior) + "</td></tr></table></td></tr></table></td></tr><tr id='measureTypeId" + val + "' style='display: ;'><td id='currVal" + val + "' colspan='1'   style='font-size:18pt' ><a onclick=\"getMeasureGraph('" + measueId + "','" + measueName + "','" + oneViewId + "','" + busrole + "')\" href='javascript:void(0)' >" + formatter.format(curval) + "</a></td><td colspan='1' align='right'></td></tr><tr><td colspan='2' height='" + Integer.parseInt(height) + "px' width='" + Integer.parseInt(width) + "px' align='left'><table width='" + Integer.parseInt(width) + "px'><tr><td width='" + Integer.parseInt(width) + "px'></td><td ><a class='ui-icon ui-icon-image'  style='text-decoration: none;' onclick=\"measureOptions('" + Integer.parseInt(request.getParameter("divId")) + "','" + oneViewId + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + measueId + "','" + measueName + "','" + formatter.format(prior) + "')\" title='MeasuresOptions' href='javascript:void(0)'></a></td><td><a class='ui-icon ui-icon-disk'  style='text-decoration: none;' onclick=\"taggleNewMeasures('" + Integer.parseInt(request.getParameter("divId")) + "','" + oneViewId + "','" + formatter.format(curval) + "','" + formatter.format(prior) + "','" + measueId + "','" + measueName + "')\" title='NewMeasureType' href='javascript:void(0)'></a></td></tr></table><table width='" + Integer.parseInt(width) + "px' border='1' style='border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;'><tr><td id='measureDescId" + val + "' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;font-size:8pt;'></td></tr></table></td></tr></table></center>");
                    }

                }
//                }


                response.getWriter().print(mestext.toString());
                return null;
            } else if (pbretObjTime != null) {
                currVal = Double.parseDouble((pbretObjTime.getFieldValueString(0, ("A_" + QueryCols.get(0)).toString())));
                BigDecimal curval = new BigDecimal(currVal);
                int decimalPlaces = 1;
                curval = curval.setScale(decimalPlaces, BigDecimal.ROUND_HALF_UP);
                NumberFormat formatter = NumberFormat.getInstance(new Locale("en_US"));
                String context = request.getContextPath();
                int val = Integer.parseInt(request.getParameter("divId"));
//                mestext.append("<center><table align='' style='overflow:auto;' height='" + Integer.parseInt(height) + "px' width='" + Integer.parseInt(width) + "px'><tr id='dynamicMeasId" + val + "' style='display:none'><td align=''><table><tr><td><table><tr><td >MTD</td></tr></table></td><td><table><tr><td width='100' style='color: rgb(255, 255, 255); background-color: rgb(0, 149, 182);' ></td><td id='currValue" + val + "' >" + formatter.format(curval) + "</td></tr></table></td></tr></table></td></tr><tr id='measureTypeId" + val + "' style='display: ;'><td id='currVal" + val + "' colspan='1'   style='font-size:30pt' ><a onclick=\"getMeasureGraph('" + measueId + "','" + measueName + "','" + oneViewId + "','" + busrole + "')\" href='javascript:void(0)'>" + formatter.format(curval) + "</a></td></tr><tr><td colspan='2' height='" + Integer.parseInt(height) / 2 + "px' width='" + Integer.parseInt(width) + "px' align='left'><table width='" + Integer.parseInt(width) + "px'><tr><td width='" + Integer.parseInt(width) + "px'></td><td id='addCommentsId' style='' width=''><a title='Add/View Comments' onclick=\"measureComments('" + Integer.parseInt(request.getParameter("divId")) + "','" + oneViewId + "')\" href='javascript:void(0)' class='ui-icon ui-icon-comment'></a></td><td ><a class='ui-icon ui-icon-image'  style='text-decoration: none;' onclick=\"measureOptions('" + Integer.parseInt(request.getParameter("divId")) + "','" + oneViewId + "','" + formatter.format(curval) + "','','" + measueId + "','" + measueName + "')\" title='MeasuresOptions' href='javascript:void(0)'></a></td><td><a class='ui-icon ui-icon-disk'  style='text-decoration: none;' onclick=\"taggleNewMeasures('" + Integer.parseInt(request.getParameter("divId")) + "','" + oneViewId + "','" + formatter.format(curval) + "','','" + measueId + "','" + measueName + "')\" title='NewMeasureType' href='javascript:void(0)'></a></td></tr></table><table width='" + Integer.parseInt(width) + "px' border='1' style='border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;'><tr><td id='measureDescId" + val + "' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#000000;font-size:8pt;'>" + measueName + " Compare With Last Period</td><td style='display:none'><input type='text' id='measureValue" + val + "' name='' value='" + measueName + " Compare With Last Period' ></td></tr></table></td></tr></table></center>");
//                mestext.append("<center><table align='' style='overflow:auto;' height='" + Integer.parseInt(height) + "px' width='" + Integer.parseInt(width) + "px'><tr id='dynamicMeasId" + val + "' style='display:none'><td align=''><table><tr><td><table><tr><td >MTD</td></tr></table></td><td><table><tr><td width='100' style='color: rgb(255, 255, 255); background-color: rgb(0, 149, 182);' ></td><td id='currValue" + val + "' >" + formatter.format(curval) + "</td></tr></table></td></tr></table></td></tr><tr id='measureTypeId" + val + "' style='display: ;'><td id='currVal" + val + "' colspan='1'   style='font-size:25pt' ><a onclick=\"getMeasureGraph('" + measueId + "','" + measueName + "','" + oneViewId + "','" + busrole + "')\" href='javascript:void(0)'>" + formatter.format(curval) + "</a></td></tr><tr><td colspan='2' height='" + Integer.parseInt(height)  + "px' width='" + Integer.parseInt(width) + "px' align='left'><table width='" + Integer.parseInt(width) + "px'><tr><td width='" + Integer.parseInt(width) + "px'></td><td id='addCommentsId' style='' width=''><a title='Add/View Comments' onclick=\"measureComments('" + Integer.parseInt(request.getParameter("divId")) + "','" + oneViewId + "')\" href='javascript:void(0)' class='ui-icon ui-icon-comment'></a></td><td ><a class='ui-icon ui-icon-image'  style='text-decoration: none;' onclick=\"measureOptions('" + Integer.parseInt(request.getParameter("divId")) + "','" + oneViewId + "','" + formatter.format(curval) + "','','" + measueId + "','" + measueName + "')\" title='MeasuresOptions' href='javascript:void(0)'></a></td><td><a class='ui-icon ui-icon-disk'  style='text-decoration: none;' onclick=\"taggleNewMeasures('" + Integer.parseInt(request.getParameter("divId")) + "','" + oneViewId + "','" + formatter.format(curval) + "','','" + measueId + "','" + measueName + "')\" title='NewMeasureType' href='javascript:void(0)'></a></td></tr></table><table width='" + Integer.parseInt(width) + "px' border='1' style='border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;'><tr><td id='measureDescId" + val + "' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#000000;font-size:8pt;'></td></tr></table></td></tr></table></center>");
                if (oneviewtypedate != null && oneviewtypedate.equalsIgnoreCase("true")) {
                    mestext.append("<center><table align='' style='overflow:auto;' height='" + Integer.parseInt(height) + "px' width='" + Integer.parseInt(width) + "px'><tr id='dynamicMeasId" + val + "' style='display:none'><td align=''><table><tr><td><table><tr><td >MTD</td></tr></table></td><td><table><tr><td width='100' style='color: rgb(255, 255, 255); background-color: rgb(0, 149, 182);' ></td><td id='currValue" + val + "' >" + formatter.format(curval) + "</td></tr></table></td></tr></table></td></tr><tr id='measureTypeId" + val + "' style='display: ;'><td id='currVal" + val + "' colspan='1'   style='font-size:18pt' ><a onclick=\"getMeasureGraph('" + measueId + "','" + measueName + "','" + oneViewId + "','" + busrole + "')\" href='javascript:void(0)'>" + formatter.format(curval) + "</a></td></tr><tr><td colspan='2' height='" + Integer.parseInt(height) + "px' width='" + Integer.parseInt(width) + "px' align='left'><table width='" + Integer.parseInt(width) + "px'><tr><td width='" + Integer.parseInt(width) + "px'></td><td style='display:none;' ><a class='ui-icon ui-icon-image'  style='text-decoration: none;' onclick=\"measureOptions('" + Integer.parseInt(request.getParameter("divId")) + "','" + oneViewId + "','" + formatter.format(curval) + "','','" + measueId + "','" + measueName + "')\" title='MeasuresOptions' href='javascript:void(0)'></a></td><td style='display:none;'><a class='ui-icon ui-icon-disk'  style='text-decoration: none;' onclick=\"taggleNewMeasures('" + Integer.parseInt(request.getParameter("divId")) + "','" + oneViewId + "','" + formatter.format(curval) + "','','" + measueId + "','" + measueName + "')\" title='NewMeasureType' href='javascript:void(0)'></a></td></tr></table><table width='" + Integer.parseInt(width) + "px' border='1' style='border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;'><tr><td id='measureDescId" + val + "' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#000000;font-size:8pt;'></td></tr></table></td></tr></table></center>");
                } else {
                    mestext.append("<center><table align='' style='overflow:auto;' height='" + Integer.parseInt(height) + "px' width='" + Integer.parseInt(width) + "px'><tr id='dynamicMeasId" + val + "' style='display:none'><td align=''><table><tr><td><table><tr><td >MTD</td></tr></table></td><td><table><tr><td width='100' style='color: rgb(255, 255, 255); background-color: rgb(0, 149, 182);' ></td><td id='currValue" + val + "' >" + formatter.format(curval) + "</td></tr></table></td></tr></table></td></tr><tr id='measureTypeId" + val + "' style='display: ;'><td id='currVal" + val + "' colspan='1'   style='font-size:18pt' ><a onclick=\"getMeasureGraph('" + measueId + "','" + measueName + "','" + oneViewId + "','" + busrole + "')\" href='javascript:void(0)'>" + formatter.format(curval) + "</a></td></tr><tr><td colspan='2' height='" + Integer.parseInt(height) + "px' width='" + Integer.parseInt(width) + "px' align='left'><table width='" + Integer.parseInt(width) + "px'><tr><td width='" + Integer.parseInt(width) + "px'></td><td ><a class='ui-icon ui-icon-image'  style='text-decoration: none;' onclick=\"measureOptions('" + Integer.parseInt(request.getParameter("divId")) + "','" + oneViewId + "','" + formatter.format(curval) + "','','" + measueId + "','" + measueName + "')\" title='MeasuresOptions' href='javascript:void(0)'></a></td><td><a class='ui-icon ui-icon-disk'  style='text-decoration: none;' onclick=\"taggleNewMeasures('" + Integer.parseInt(request.getParameter("divId")) + "','" + oneViewId + "','" + formatter.format(curval) + "','','" + measueId + "','" + measueName + "')\" title='NewMeasureType' href='javascript:void(0)'></a></td></tr></table><table width='" + Integer.parseInt(width) + "px' border='1' style='border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;'><tr><td id='measureDescId" + val + "' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#000000;font-size:8pt;'></td></tr></table></td></tr></table></center>");
                }

                response.getWriter().print(mestext.toString());
                return null;
            } else {
                response.getWriter().print("ApplyCurrent");
                return null;
            }
        } else if (measueId != null && measueName != null) {
            double measValue = 0.0;
            String userId = String.valueOf(session.getAttribute("USERID"));
            List<KPIElement> kpiElements = dao.getKPIElements(measureIdVal, new HashMap<String, String>());
            if (kpiElements != null) {
                for (KPIElement elem : kpiElements) {
                    if (elem.getElementName() != null) {
                        QueryCols.add(elem.getElementId());
                    }
                    QueryAggs.add(elem.getAggregationType());
                }
            }
            pbretObjTime = dao.getReturnObjectForOneView(QueryCols, QueryAggs, userId, busrole, request, onecontainer, detail);
            if (pbretObjTime != null) {
                measValue = Double.parseDouble((pbretObjTime.getFieldValueString(0, ("A_" + QueryCols.get(0)).toString())));
            }
            BigDecimal curval = new BigDecimal(measValue);
            NumberFormat formatter = NumberFormat.getInstance(new Locale("en_US"));
            String currValue = formatter.format(curval);
            response.getWriter().print(currValue);
            return null;
        } else {
            return mapping.findForward("sessionExpired");
        }
    }
    //Surender

    public ActionForward getReportTables(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession(false);
        DashboardTemplateBD dashboardTemplateBD = new DashboardTemplateBD();
        dashboardTemplateBD.setServletRequest(request);
        PrintWriter out = response.getWriter();
        ServletContext context = this.getServlet().getServletContext();
        String advHtmlFileProps = (String) request.getSession(false).getAttribute("advHtmlFileProps");
        HashMap map = null;
        Container container = null;
        HashMap oneviewHashMap = null;
        String reportId = request.getParameter("reportId");

        String height = request.getParameter("height");
        String width = request.getParameter("width");
        String oneViewIdValue = request.getParameter("oneViewIdValue");
        String repType = request.getParameter("repTable");
        String reportName = request.getParameter("name");
        String busroleId = request.getParameter("busroleId");
        String istranseposse = request.getParameter("istranseposse");
        int widt = Integer.parseInt(width);
        int heigh = Integer.parseInt(height);
        int viewletId = Integer.parseInt(request.getParameter("divId"));
//        int colSp = Integer.parseInt(request.getParameter("colSp"));
//        int rowSp = Integer.parseInt(request.getParameter("rowSp"));

//       if(session!=null){
        OneViewLetDetails detail = null;
        if (istranseposse != null && istranseposse.equalsIgnoreCase("true")) {
            session.setAttribute("istranseposse", istranseposse);
        }
        OnceViewContainer onecontainer = new OnceViewContainer();
        oneviewHashMap = (HashMap) session.getAttribute("ONEVIEWDETAILS");
        if (oneviewHashMap != null) {
            onecontainer = (OnceViewContainer) oneviewHashMap.get(oneViewIdValue);
//            OneViewLetDetails detail = new OneViewLetDetails();
            detail = onecontainer.onviewLetdetails.get(Integer.parseInt(request.getParameter("divId")));
            detail.setRepId(reportId);
//           detail.setGrapId(graphId);
            detail.setRepName(reportName);
            detail.setReptype(repType);
            detail.setWidth(widt);
            detail.setHeight(heigh);
            detail.setRoleId(busroleId);
            if (detail.getMeasurDrill() != null) {
                detail.setMeasurDrill(null);
            }
//           detail.setColSpan(colSp);
//           detail.setRowSpan(rowSp);
//           onecontainer.addDashletDetail(detail);
        } else {
//            OneViewLetDetails detail = null;

            ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();
//        onecontainer = reportTemplateDAO.getOneViewData(oneViewIdValue);
            // String fileName = reportTemplateDAO.getOneviewFileName(oneViewIdValue);
            String fileName = request.getSession(false).getAttribute("tempFileName").toString();
            FileInputStream fis2 = new FileInputStream(advHtmlFileProps + "/" + fileName);
            ObjectInputStream ois2 = new ObjectInputStream(fis2);
            onecontainer = (OnceViewContainer) ois2.readObject();
            ois2.close();
            detail = onecontainer.onviewLetdetails.get(Integer.parseInt(request.getParameter("divId")));
            detail.setRepId(reportId);
//           detail.setGrapId(graphId);
            detail.setRepName(reportName);
            detail.setWidth(widt);
            detail.setReptype(repType);
            detail.setRoleId(busroleId);
            detail.setHeight(heigh);
            if (detail.getMeasurDrill() != null) {
                detail.setMeasurDrill(null);
            }
//            reportTemplateDAO.updateOneviewData(onecontainer,oneViewIdValue);
            HashMap tempRegHashMap = onecontainer.getTempRegHashMap();
            String tempRegFileName = (String) tempRegHashMap.get(Integer.parseInt(detail.getNoOfViewLets()));
            if (tempRegFileName == null) {
                tempRegFileName = "InnerRegionDetails" + onecontainer.oneviewId + "_" + viewletId + "_" + session.getId() + "_" + System.currentTimeMillis() + ".txt";
                tempRegHashMap.put(viewletId, tempRegFileName);
            }
            OneViewBD oneViewBD = new OneViewBD();
            String result = "";
            result = dashboardTemplateBD.getTableDetailsData(request, response, session, detail, onecontainer.timedetails, onecontainer);
            oneViewBD.writeOneviewRegData(onecontainer, result, detail.getNoOfViewLets(), request);
            FileOutputStream fos = new FileOutputStream(advHtmlFileProps + "/" + fileName);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(onecontainer);
            oos.flush();
            oos.close();

        }

        DataSnapshotGenerator generateRepTable = new DataSnapshotGenerator();
        StringBuilder result = new StringBuilder();

        String userId = String.valueOf(session.getAttribute("USERID"));
        PbReportViewerBD reportViewerBD = new PbReportViewerBD();
        String strURL = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
        strURL = strURL + "reportViewer.do?reportBy=viewReport&REPORTID=" + reportId + "&action=open";
        request.setAttribute("url", strURL);
        request.setAttribute("REPORTID", reportId);
        reportViewerBD.prepareReport(reportId, userId, request, response,false);
        map = (HashMap) session.getAttribute("PROGENTABLES");

        container = (Container) map.get(reportId);
        detail.setContainer(container);
//            result.append("<table >");
//            result.append("<table width=\"").append(width).append("px").append("\" ") height=\"" + height + "px\">").append(">");
        if (container.isTopBottomTableEnable()) {
            result = generateRepTable.generateTopBottomTable(container, userId, width, width);
        } else {
            if (istranseposse != null && istranseposse.equalsIgnoreCase("true")) {
                container.setfromBKP(istranseposse);
            }
            result = generateRepTable.generateReportTable(container, userId, height, width);
        }
//            result.append("</table>");
        out.print(result.toString());
        return null;
    }
    //Surender

    public ActionForward getReportGraphs(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession(false);
        DashboardTemplateBD dashboardTemplateBD = new DashboardTemplateBD();
        dashboardTemplateBD.setServletRequest(request);
        PrintWriter out = response.getWriter();
        ServletContext context = this.getServlet().getServletContext();
        boolean isFxCharts = Boolean.parseBoolean(context.getInitParameter("isFxCharts"));
        String addedToDBStr = request.getParameter("addedToDashBoard");
        boolean addedToDashboard = false;
        if (addedToDBStr != null && "true".equalsIgnoreCase(addedToDBStr)) {
            addedToDashboard = true;
        }
        HashMap map = null;
        Container container = null;
        OnceViewContainer onecontainer = new OnceViewContainer();
        String customDbrdId = "";
        HashMap divGraphs = null;
        String reportId = request.getParameter("reportId");
        String graphId = request.getParameter("graphId");

        String gaphName = request.getParameter("name");
        String divId = request.getParameter("divId");
        String oneViewIdValue = request.getParameter("oneViewIdValue");
        String repType = request.getParameter("repGraph");
        String busroleId = request.getParameter("busroleId");
        String chartname = request.getParameter("chartname");
        String chartname1 = request.getParameter("chartname1");
        String busrolename = request.getParameter("busrolename");
        int width = Integer.parseInt(request.getParameter("width"));
        int height = Integer.parseInt(request.getParameter("height"));
        int grpNo = Integer.parseInt(request.getParameter("grpNo"));
        boolean oneviewTime = Boolean.parseBoolean(request.getParameter("oneviewTime"));
//        int colSp = Integer.parseInt(request.getParameter("colSp"));
//        int rowSp = Integer.parseInt(request.getParameter("rowSp"));
        String advHtmlFileProps = (String) request.getSession(false).getAttribute("advHtmlFileProps");
        String result = "";
        int viewletId = Integer.parseInt(request.getParameter("divId"));
        if (session != null) {

//           detail.setColSpan(colSp);
//           detail.setRowSpan(rowSp);
//           onecontainer.addDashletDetail(detail);
            boolean desing = false;
            OneViewLetDetails detail = new OneViewLetDetails();
            detail.setHeight(height);
            detail.setWidth(width);

//            result = dashboardTemplateBD.getGraphById(reportId,request, response, session, grpNo, desing);
//            out.print(result);
//             int va = result.indexOf("#");
//            int v = result.indexOf("png");
//            String image= result.substring(va+1,v+3);
//
//            
            map = (HashMap) session.getAttribute("ONEVIEWDETAILS");
            if (map != null) {
                onecontainer = (OnceViewContainer) map.get(oneViewIdValue);

                //OneViewLetDetails detail = new OneViewLetDetails();
                detail = onecontainer.onviewLetdetails.get(Integer.parseInt(request.getParameter("divId")));
                detail.setRepId(reportId);
                detail.setGrapId(graphId);
                detail.setRepName(gaphName);
                detail.setReptype(repType);
                detail.setWidth(width);
                detail.setHeight(height);
                detail.setRoleId(busroleId);
                detail.setRolename(busrolename);
                detail.setGrapNo(grpNo);
                detail.setchartname(chartname);
                detail.setgraphname(chartname1);
                detail.setchartdrills("null");
                detail.setchartrefreshdrills("null");
                detail.setGraphType("JQPlot");
//           detail.graphDetails.put(request.getParameter("divId"),image);

                if (detail.getMeasurDrill() != null) {
                    detail.setMeasurDrill(null);
                }
//                result = dashboardTemplateBD.getGraphById(reportId,request, response, session, grpNo, desing,detail, onecontainer);
            } else {
                //OneViewLetDetails detail = null;

                ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();
//              onecontainer = reportTemplateDAO.getOneViewData(oneViewIdValue);
                //  String fileName = reportTemplateDAO.getOneviewFileName(oneViewIdValue);
                String fileName = session.getAttribute("tempFileName").toString();
                FileInputStream fis2 = new FileInputStream(advHtmlFileProps + "/" + fileName);
                ObjectInputStream ois2 = new ObjectInputStream(fis2);
                onecontainer = (OnceViewContainer) ois2.readObject();
                ois2.close();

                detail = onecontainer.onviewLetdetails.get(Integer.parseInt(request.getParameter("divId")));
                detail.setRepId(reportId);
                detail.setGrapId(graphId);
                detail.setRepName(gaphName);
                detail.setReptype(repType);
                detail.setHeight(height);
                detail.setWidth(width);
                detail.setRoleId(busroleId);
                detail.setRolename(busrolename);
                detail.setGrapNo(grpNo);
                detail.setchartname(chartname);
                detail.setgraphname(chartname1);
                detail.setchartdrills("null");
                detail.setchartrefreshdrills("null");
                detail.setOneviewReportTimeDetails(oneviewTime);
                detail.setGraphType("JQPlot");
                if (detail.getMeasurDrill() != null) {
                    detail.setMeasurDrill(null);
                }
//              detail.graphDetails.put(request.getParameter("divId"),image);
//             reportTemplateDAO.updateOneviewData(onecontainer,oneViewIdValue);
                // DashboardTemplateBD dashboardTemplateBD = new DashboardTemplateBD();
                OneViewBD oneViewBD = new OneViewBD();
                if (detail.getPbReturnObject() == null) {
                    detail.setMeasurOprFlag(true);
                } else {
                    detail.setMeasurOprFlag(false);
                }
                HashMap tempRegHashMap = onecontainer.getTempRegHashMap();
                String tempRegFileName = (String) tempRegHashMap.get(Integer.parseInt(detail.getNoOfViewLets()));
                if (tempRegFileName == null) {
                    tempRegFileName = "InnerRegionDetails" + onecontainer.oneviewId + "_" + viewletId + "_" + session.getId() + "_" + System.currentTimeMillis() + ".txt";
                    tempRegHashMap.put(viewletId, tempRegFileName);
                }



                String result1 = "";
                result1 = dashboardTemplateBD.getGraphDetailsData(request, response, session, detail, onecontainer.timedetails, onecontainer);
                oneViewBD.writeOneviewRegData(onecontainer, result1, detail.getNoOfViewLets(), request);
                FileOutputStream fos = new FileOutputStream(advHtmlFileProps + "/" + fileName);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(onecontainer);
                oos.flush();
                oos.close();
                result = result1;
            }
//            result = dashboardTemplateBD.getGraphById(reportId,request, response, session, grpNo, desing,detail,onecontainer);
            out.print(result);
            return null;
        } else {

            return null;
        }
    }

    public ActionForward getRelatedMeasuresTrend(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String measId = request.getParameter("measId");
        String qry = "select DEPENDENT_MEASURE from PRG_USER_ALL_INFO_DETAILS where ELEMENT_ID = " + measId;
        PbReturnObject retObj = null;
        PbReturnObject retObjname = null;
        ArrayList alist = new ArrayList();
        String result = "";
        StringBuilder kpiheadsbuilder = new StringBuilder();
        StringBuilder kpiSequence = new StringBuilder();
        StringBuilder kpiNumFormat = new StringBuilder();
        StringBuilder kpiRound = new StringBuilder();
        StringBuilder kpiFontColor = new StringBuilder();
        StringBuilder kpiSymbols = new StringBuilder();
        String[] measIds = null;
        PbDb pbdb = new PbDb();
        StringBuilder finalStringVal = new StringBuilder();
        retObj = pbdb.execSelectSQL(qry);
        if (retObj != null && retObj.getRowCount() > 0 && !retObj.getFieldValueString(0, "DEPENDENT_MEASURE").equalsIgnoreCase("")) {
            result = retObj.getFieldValueString(0, "DEPENDENT_MEASURE");
        }
        if (result != "") {
            measIds = result.split(",");
            if (measIds != null && measIds.length > 0) {
                for (int i = 0; i < measIds.length; i++) {
                    String nameqry = "SELECT A.USER_COL_DESC,A.REF_ELEMENT_TYPE,B.NO_FORMAT,B.ROUND, B.FONTCOLOR, B.SYMBOLS from PRG_USER_ALL_INFO_DETAILS A,PRG_USER_SUB_FOLDER_ELEMENTS B where A.ELEMENT_ID = " + measIds[i] + " and A.ELEMENT_ID = B.ELEMENT_ID";
                    retObjname = pbdb.execSelectSQL(nameqry);
                    if (retObjname != null) {
                        kpiheadsbuilder.append("," + retObjname.getFieldValueString(0, "USER_COL_DESC"));
                        kpiSequence.append("," + retObjname.getFieldValueString(0, "REF_ELEMENT_TYPE"));
                        kpiNumFormat.append("," + retObjname.getFieldValueString(0, "NO_FORMAT"));
                        kpiRound.append("," + retObjname.getFieldValueString(0, "ROUND"));
                        kpiFontColor.append("," + retObjname.getFieldValueString(0, "FONTCOLOR"));
                        kpiSymbols.append("," + retObjname.getFieldValueString(0, "SYMBOLS"));
                    }
                }
            }
        }
        if (retObj != null && retObj.getRowCount() > 0 && !retObj.getFieldValueString(0, "DEPENDENT_MEASURE").equalsIgnoreCase("")) {
            alist.add(result);
            alist.add(kpiheadsbuilder.toString().substring(1));

            String arr = (String) alist.get(1);
            String arr1[] = arr.split(",");
            measIds = result.split(",");
            finalStringVal.append("<table align='left'>");
            for (int i = 0; i < 4; i++) {
                finalStringVal.append("<tr><td id='" + measIds + "'>" + arr1[i] + "</td></tr>");
            }
        }
        JSONObject jsonMap = new JSONObject();
        jsonMap.put("MeasureIds", result);
        jsonMap.put("measureName", alist.get(1));
        response.getWriter().print(jsonMap);
        return null;
    }
    // by gopesh

    public ActionForward getMeasuresForRole(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException, ClassNotFoundException {
        HttpSession session = request.getSession(false);
        HashMap map = new HashMap();
        Container container = null;
        String oneViewId = "";
        String foldersIds = "";
        String PbUserId = "";
        String result = "";
        ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();
        if (session != null) {
            oneViewId = request.getParameter("oneViewId");
            foldersIds = request.getParameter("foldersIds");
            PbUserId = String.valueOf(session.getAttribute("USERID"));
            result = reportTemplateDAO.getUserDimDetails(foldersIds, PbUserId);

            request.setAttribute("dims", result);
            request.setAttribute("USERID", PbUserId);
            request.setAttribute("reportId", oneViewId);

            return mapping.findForward("addDims");
//            response.getWriter().print(result);

        } else {
            return mapping.findForward("sessionExpired");
        }
    } // by gopesh

    public ActionForward setDimensionDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException, ClassNotFoundException {

        HttpSession session = request.getSession(false);
        String oneviewId = request.getParameter("oneviewId");
        String dimIds = request.getParameter("dimIds");
        String dimName = request.getParameter("dimName");
        OneViewBD oneBD = new OneViewBD();
        oneBD.setDimensionDetails(oneviewId, dimIds, dimName, session);

        return null;
    }
    // by gopesh

    public ActionForward getMeasureGraphForTrends(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException, ClassNotFoundException, Exception {
        HttpSession session = request.getSession(false);
        OneViewBD oneBd = new OneViewBD();
        String timeval = "";
        int counttb = 0;


        boolean ischange = Boolean.parseBoolean(request.getParameter("ischange"));
        boolean ischangepercent = Boolean.parseBoolean(request.getParameter("ischangepercent"));
        //String selectedid=request.getParameter("selectedid");

        String oneViewId = request.getParameter("oneViewId");
        String counttb1 = request.getParameter("counttb");
        String seltbid1 = request.getParameter("seltbid");
        if (!counttb1.equalsIgnoreCase("")) {
            counttb = Integer.parseInt(counttb1);
        }
        if (oneViewId != null) {
            session.setAttribute("oneViewId", oneViewId);
        }

        String advHtmlFileProps = (String) session.getAttribute("advHtmlFileProps");
//        String fileName = session.getAttribute("tempFileName").toString();
        String oldadvHtmlFileProps = (String) session.getAttribute("oldAdvHtmlFileProps");
        ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();
        String fileName = reportTemplateDAO.getOneviewFileName(oneViewId);


        StringBuilder imgtest = new StringBuilder();
        OnceViewContainer oneContainer = (OnceViewContainer) oneBd.readFileDetails(oldadvHtmlFileProps, fileName);
        ArrayList measIdList = new ArrayList();
        ArrayList measNameList = new ArrayList();
        measIdList = oneContainer.getParamIds();
        measNameList = oneContainer.getParamNames();
        if (measIdList == null || (measIdList != null && measIdList.isEmpty())) {
            PrintWriter out = response.getWriter();
            out.print("Please select parameters for Drill");
            return null;
        }
        int noOfTrends = 0;
        String Nofalg = request.getParameter("getNoOfTrends");
        if (Nofalg != null && measIdList != null) {
            noOfTrends = measIdList.size();
            PrintWriter out = response.getWriter();

            out.print(noOfTrends);
            session.setAttribute("noOfTrends", noOfTrends);
            return null;
        }
//         imgtest.append("<table  align='left' ><tr>");


        //for months last date
        String monthVal = request.getParameter("monthVal");
        PbReportCollection repo = new PbReportCollection();
        String measId = request.getParameter("measId");


        timeval = repo.getMonthDrillForOneview(monthVal, measId);

        int count = 0;
        if (measIdList.size() < 4 || measIdList.size() > 4) {
            count = 4;
        } else {
            count = measIdList.size();
        }
        int indexofTrend = Integer.parseInt(request.getParameter("num"));
//         for (int j = 0; j < count; j++) {
        ArrayList rowview = new ArrayList();
        rowview.add((String) measIdList.get(indexofTrend));
//             if (j == 2) {
//                 imgtest.append("<tr>");
//             }
//             imgtest.append("<td>"+measNameList.get(indexofTrend)+"</td>");
//             String oneViewId = request.getParameter("oneViewId");
        String roleId = request.getParameter("roleId");
        String prevYearComp = request.getParameter("prevYearComp");
        StringBuilder sb = new StringBuilder();
        DashboardViewerDAO dao = new DashboardViewerDAO();
        PbReturnObject pbretObjForTime = new PbReturnObject();
        String imgdata = "";
        ArrayList arl2 = new ArrayList();
        PbReportQuery timequery = new PbReportQuery();
        ArrayList QueryCols = new ArrayList();
        ArrayList QueryAggs = new ArrayList();
        List<String> measureIdVal = new ArrayList<String>();
        measureIdVal.add(measId);
        String userId = String.valueOf(session.getAttribute("USERID"));
        List<KPIElement> kpiElements = dao.getKPIElements(measureIdVal, new HashMap<String, String>());
        if (kpiElements != null) {
            for (KPIElement elem : kpiElements) {
                if (elem.getElementName() != null) {
                    QueryCols.add(elem.getElementId());
                }
                QueryAggs.add(elem.getAggregationType());
            }
        }
        OnceViewContainer onecontainer1 = null;
        PbReturnObject retObj = null;
//             ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();
        retObj = reportTemplateDAO.testForExisting();
        ArrayList<String> va = new ArrayList<String>();
        for (int i = 0; i < retObj.getRowCount(); i++) {
            va.add(retObj.getFieldValueString(i, "ONEVIEWID"));
        }
        if (!va.isEmpty() && va.contains(oneViewId)) {
            retObj = reportTemplateDAO.getOneviewFileNam(oneViewId);
            fileName = request.getSession(false).getAttribute("tempFileName").toString();
            FileInputStream fis = new FileInputStream(advHtmlFileProps + "/" + fileName);
            ObjectInputStream ois = new ObjectInputStream(fis);
            onecontainer1 = (OnceViewContainer) ois.readObject();
            ois.close();
        }
        ArrayList arl = new ArrayList();

        if (onecontainer1 != null && !onecontainer1.timedetails.isEmpty()) {
            if (timeval != null && !timeval.isEmpty()) {
                onecontainer1.timedetails.remove(2);
                onecontainer1.timedetails.add(2, timeval);
            }
            arl = (ArrayList) onecontainer1.timedetails;
            arl2.addAll(arl);
        } else {
            arl.add("Day");
            arl.add("PRG_STD");
            ProgenParam pramnam = new ProgenParam();
            String date = pramnam.getdateforpage();
            arl.add(date);
            arl.add("Month");
            arl.add("Last Period");
            arl2.addAll(arl);
        }
        if (ischange) {
            measureIdVal = new ArrayList<String>();
            measureIdVal.add(String.valueOf(QueryCols.get(2)));
        } else if (ischangepercent) {
            measureIdVal = new ArrayList<String>();
            measureIdVal.add(String.valueOf(QueryCols.get(3)));
        }

        if (arl2 != null && !arl2.isEmpty() && prevYearComp != null && !prevYearComp.isEmpty() && prevYearComp.equalsIgnoreCase("true")) {
            arl2.set(4, "Last Period");
            measureIdVal.add(String.valueOf(QueryCols.get(1)));

        }

        timequery.setRowViewbyCols(rowview);
        timequery.setColViewbyCols(new ArrayList());
        timequery.setColViewbyCols(new ArrayList());
        timequery.setQryColumns(QueryCols);
        timequery.setColAggration(QueryAggs);
        timequery.setTimeDetails(arl2);
//             if(ischange){
//               timequery.setDefaultMeasure(String.valueOf(QueryCols.get(2)));
//             timequery.setDefaultMeasureSumm(String.valueOf(QueryAggs.get(2)));
//             }else if (ischangepercent){
//                timequery.setDefaultMeasure(String.valueOf(QueryCols.get(3)));
//             timequery.setDefaultMeasureSumm(String.valueOf(QueryAggs.get(3)));
//             }else{
//             timequery.setDefaultMeasure(String.valueOf(QueryCols.get(0)));
//             timequery.setDefaultMeasureSumm(String.valueOf(QueryAggs.get(0)));
//             }
        timequery.isTimeSeries = false;
        timequery.setBizRoles(roleId);
        timequery.setUserId(userId);
        if (ischange) {
            pbretObjForTime = timequery.getPbReturnObject(String.valueOf(QueryCols.get(2)));
        } else if (ischangepercent) {
            pbretObjForTime = timequery.getPbReturnObject(String.valueOf(QueryCols.get(3)));
        } else {
            pbretObjForTime = timequery.getPbReturnObject(String.valueOf(QueryCols.get(0)));
        }
        ProgenJQPlotGraph jqGraph = new ProgenJQPlotGraph();
        jqGraph.trendType = "montFormat";
        jqGraph.setChartId(oneViewId);
        jqGraph.setChartId(oneViewId);
        jqGraph.setGraphType("Line");

        if (indexofTrend == 0) {
            int totaltrends = Integer.parseInt(String.valueOf(session.getAttribute("noOfTrends")));
            imgtest.append("<table align='center' style='margin-left:auto;margin-right:auto;'><tr><td><span style='font-size:12px;color:#2191C0;font-weight:bold;text-decoration: none;'>");
            imgtest.append("</span>");//<a href='javascript:void(0)' onclick='showSingleGraph(0,"+totaltrends+")'>Single Graph</a>
            imgtest.append("<INPUT type='image' src='" + request.getContextPath() + "/icons pinvoke/arrow-skip.png' style='cursor: pointer;width:13px; float:right;height:10px;' title='Single View' onclick='showSingleGraph(0," + totaltrends + ")'/>");
        }
        imgtest.append("<table align='center' style='margin-left:auto;margin-right:auto;'><tr><td><span id='measName" + indexofTrend + "' style='font-size:12px;color:#2191C0;font-weight:bold;text-decoration: none;'>" + measNameList.get(indexofTrend) + "</span>");
        session.setAttribute("measName" + indexofTrend, measNameList.get(indexofTrend));
        imgtest.append("</td><input id='inputhidden" + indexofTrend + "' type='hidden' value='' id='h'></tr></table>");
        imgtest.append("<div id='chart" + indexofTrend + "-" + oneViewId + "' style='width:320px; height:260px;top:0px;margin-left:20px;'></div>");

        String datavaluesstr = "";
        String keyvaluesstr = "";
        String barchartcolumntitlesstr = "";
        String viewbycolumnsstr = "";

        imgtest.append("<script>$(\".jqplot-highlighter-tooltip, .jqplot-canvasOverlay-tooltip \").css('font-size', '1em');");
        imgtest.append(jqGraph.getTrendGraphForParams(pbretObjForTime, (ArrayList) measureIdVal, rowview, prevYearComp, indexofTrend, counttb, seltbid1));
        imgtest.append("$(\"#chartA-" + oneViewId + "\").bind(\"jqplotDataClick\", function(ev, seriesIndex, pointIndex, data) {");
        imgtest.append("relatedMeasuresForDrill('" + measId + "','" + oneViewId + "','" + roleId + "');");
        imgtest.append("});");
        imgtest.append("</script>");
//             imgtest.append("</td>");
//             if (j == 3) {
//                 imgtest.append("</tr>");
//             }
//         }
//         imgtest.append("</tr></table>");

        PrintWriter out = response.getWriter();
        out.print(imgtest.toString());
        return null;
    }

    public ActionForward getOverlayTrends(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException, ClassNotFoundException, Exception {
        HttpSession session = request.getSession(false);
        OneViewBD oneBd = new OneViewBD();

        String timeval = "";
        String oneViewId = request.getParameter("oneViewId");
        String advHtmlFileProps = (String) session.getAttribute("advHtmlFileProps");
        String oldadvHtmlFileProps = (String) session.getAttribute("oldAdvHtmlFileProps");
        ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();
        String fileName = reportTemplateDAO.getOneviewFileName(oneViewId);


        StringBuilder imgtest = new StringBuilder();
        OnceViewContainer oneContainer = (OnceViewContainer) oneBd.readFileDetails(oldadvHtmlFileProps, fileName);
        ArrayList measIdList = new ArrayList();
        ArrayList measNameList = new ArrayList();
        ArrayList selectedParam = new ArrayList();
        ArrayList selectedParamVAl = new ArrayList();
        String selParam = request.getParameter("selParam");
        if (selParam == null) {
            measIdList = oneContainer.getParamIds();
        } else {
            measIdList.add(selParam);
        }
        measNameList = oneContainer.getParamNames();
        String Nofalg = request.getParameter("getNoOfTrends");
        HashMap<String, List> map = new HashMap<String, List>();

        String checkcon = request.getParameter("checkcon");
        if (checkcon != null && checkcon.equalsIgnoreCase("checkcon")) {
            PrintWriter out = response.getWriter();
            if (measIdList == null || (measIdList != null && measIdList.isEmpty())) {
                out.print("Please select parameters for Drill");
                return null;
            } else {
                out.print("parameters for Drill");
                return null;
            }
        }
        int nuOfParams = 0;
        String applyChangeNum = request.getParameter("applyChangeNum");
        // int nuOfParams = Integer.parseInt(request.getParameter("nuOfParams"));
        if (applyChangeNum != null && applyChangeNum != "") {
            nuOfParams = Integer.parseInt(request.getParameter("nuOfParams"));
            nuOfParams = nuOfParams;
        } else {
            nuOfParams = 3;
        }

//    if(nuOfParams>measIdList.size()){
//    nuOfParams=measIdList.size();
//    }
        //String selParam = request.getParameter("selParam");
        for (int j = 0; j < 1; j++) {
            String measId = request.getParameter("measId");
            // String measName = request.getParameter("measName");
            String roleId = request.getParameter("roleId");
            String prevYearComp = request.getParameter("prevYearComp");
            StringBuilder sb = new StringBuilder();
            DashboardViewerDAO dao = new DashboardViewerDAO();
            PbReturnObject pbretObjForTime = new PbReturnObject();
            PbReturnObject pbretObjForTime1 = new PbReturnObject();
            String imgdata = "";

            ArrayList arl2 = new ArrayList();
            ArrayList rowview = new ArrayList();
            ArrayList rowview1 = new ArrayList();
            rowview.add(measIdList.get(0));
            rowview1.add("TIME");
            PbReportQuery timequery = new PbReportQuery();
            PbReportQuery timequery1 = new PbReportQuery();
            ArrayList QueryCols = new ArrayList();
            ArrayList QueryAggs = new ArrayList();
            List<String> measureIdVal = new ArrayList<String>();
            measureIdVal.add(measId);

            String monthVal = request.getParameter("monthVal");
            PbReportCollection repo = new PbReportCollection();
            timeval = repo.getMonthDrillForOneview(monthVal, measId);


            String userId = String.valueOf(session.getAttribute("USERID"));
            List<KPIElement> kpiElements = dao.getKPIElements(measureIdVal, new HashMap<String, String>());
            if (kpiElements != null) {
                for (KPIElement elem : kpiElements) {
                    if (elem.getElementName() != null) {
                        QueryCols.add(elem.getElementId());
                    }
                    QueryAggs.add(elem.getAggregationType());
                }
            }
            OnceViewContainer onecontainer1 = null;
            PbReturnObject retObj = null;
            retObj = reportTemplateDAO.testForExisting();
            ArrayList<String> va = new ArrayList<String>();
            for (int i = 0; i < retObj.getRowCount(); i++) {
                va.add(retObj.getFieldValueString(i, "ONEVIEWID"));
            }
            if (!va.isEmpty() && va.contains(oneViewId)) {
                retObj = reportTemplateDAO.getOneviewFileNam(oneViewId);
                fileName = request.getSession(false).getAttribute("tempFileName").toString();
                FileInputStream fis = new FileInputStream(advHtmlFileProps + "/" + fileName);
                ObjectInputStream ois = new ObjectInputStream(fis);
                onecontainer1 = (OnceViewContainer) ois.readObject();
                ois.close();
            }
            ArrayList arl = new ArrayList();

            if (onecontainer1 != null && !onecontainer1.timedetails.isEmpty()) {
                if (timeval != null && !timeval.isEmpty()) {
                    onecontainer1.timedetails.remove(2);
                    onecontainer1.timedetails.add(2, timeval);
                }
                arl = (ArrayList) onecontainer1.timedetails;
                arl2.addAll(arl);
            } else {
                arl.add("Day");
                arl.add("PRG_STD");
                ProgenParam pramnam = new ProgenParam();
                String date = pramnam.getdateforpage();
                arl.add(date);
                arl.add("Month");
                arl.add("Last Period");
                arl2.addAll(arl);
            }
            if (arl2 != null && !arl2.isEmpty() && prevYearComp != null && !prevYearComp.isEmpty() && prevYearComp.equalsIgnoreCase("true")) {
                arl2.set(4, "Last Period");
                measureIdVal.add(String.valueOf(QueryCols.get(1)));
                //
            }

//                timequery.setRowViewbyCols(new ArrayList());
//                repQuery.setParamValue(collect.reportParametersValues);
            timequery.setRowViewbyCols(rowview);
            timequery.setColViewbyCols(new ArrayList());
            //timequery.setColViewbyCols(new ArrayList());
            timequery.setQryColumns(QueryCols);
            timequery.setColAggration(QueryAggs);
            timequery.setTimeDetails(arl2);
            timequery.setDefaultMeasure(String.valueOf(QueryCols.get(0)));
            timequery.setDefaultMeasureSumm(String.valueOf(QueryAggs.get(0)));
            timequery.setBizRoles(roleId);
            timequery.setUserId(userId);

            timequery1.setRowViewbyCols(rowview1);
            timequery1.setColViewbyCols(rowview);
            //timequery1.setColViewbyCols(rowview);
            timequery1.setQryColumns(QueryCols);
            timequery1.setColAggration(QueryAggs);
            timequery1.setTimeDetails(arl2);
            timequery1.setDefaultMeasure(String.valueOf(QueryCols.get(0)));
            timequery1.setDefaultMeasureSumm(String.valueOf(QueryAggs.get(0)));
            timequery1.setBizRoles(roleId);
            timequery1.setUserId(userId);
            char[] sorttype = new char[]{'1'};
            ArrayList<String> measureIdVal1 = new ArrayList<String>();
            measureIdVal1.add("A_" + measId);
            ArrayList dataSeq = new ArrayList();
            pbretObjForTime = timequery.getPbReturnObject(String.valueOf(QueryCols.get(0)));
            dataSeq = pbretObjForTime.findTopBottom(measureIdVal1, sorttype, nuOfParams);
            pbretObjForTime.setViewSequence(dataSeq);
            ArrayList alist1 = new ArrayList();
            alist1 = pbretObjForTime.retrieveDataBasedOnViewSeq("A_" + measId);
            String selParamSt = "";
            String[] selParamStArr = null;
            for (int i = 0; i < alist1.size(); i++) {
                //
                //if(i==0 || i==1){
                selectedParam.add(pbretObjForTime.getFieldValueStringBasedOnViewSeq(i, "A_" + measIdList.get(0)));
                // }
                if (i == 0) {
                    //selectedParam.add(pbretObjForTime.getFieldValueStringBasedOnViewSeq(i,"A_"+measIdList.get(0)));
                    selParamSt = selParamSt + pbretObjForTime.getFieldValueStringBasedOnViewSeq(i, "A_" + measIdList.get(0));
                } else {
                    selParamSt = selParamSt + "," + pbretObjForTime.getFieldValueStringBasedOnViewSeq(i, "A_" + measIdList.get(0));
                }
                selectedParamVAl.add(alist1.get(i).toString());
                //selParamSt = selParamSt.substring(0);
            }
            if (selParamSt != null && !selParamSt.equalsIgnoreCase("")) {
                selParamStArr = selParamSt.split(",");
            }

            HashMap hmap = new HashMap();
            hmap.put(measIdList.get(0), selectedParam);
            timequery1.setInMap(hmap);
            pbretObjForTime1 = timequery1.getPbReturnObject(String.valueOf(QueryCols.get(0)));

            ProgenJQPlotGraph jqGraph = new ProgenJQPlotGraph();
            jqGraph.trendType = "montFormat";
            jqGraph.setChartId(oneViewId);
            jqGraph.setChartId(oneViewId);
            jqGraph.setGraphType("Line");
            //
//             imgtest.append("<td>");
            //  imgtest.append("<table align='center' style='margin-left:auto;margin-right:auto;'><tr><td><span style='font-size:12px;color:#2191C0;font-weight:bold;text-decoration: none;'>"+measNameList.get(j)+"</span></td></tr></table>");
            imgtest.append("<div id='chartA" + nuOfParams + "-" + oneViewId + "' style='width:560px; height:500px;top:0px;margin-left:20px;'></div>");

            String datavaluesstr = "";
            String keyvaluesstr = "";
            String barchartcolumntitlesstr = "";
            String viewbycolumnsstr = "";
            pbretObjForTime1.setCrosstabelements(selParamStArr, measIdList.get(0).toString());
            imgtest.append("<script>$(\".jqplot-highlighter-tooltip, .jqplot-canvasOverlay-tooltip \").css('font-size', '1em');");
            imgtest.append(jqGraph.getoverLayedGraph(pbretObjForTime1, (ArrayList) measureIdVal, rowview1, prevYearComp, measIdList.get(0).toString(), selectedParam, nuOfParams));
            imgtest.append("$(\"#chartA-" + oneViewId + "\").bind(\"jqplotDataClick\", function(ev, seriesIndex, pointIndex, data) {");
            imgtest.append("relatedMeasuresForDrill('" + measId + "','" + oneViewId + "','" + roleId + "');");
            imgtest.append("});");
            imgtest.append("</script>");

            //ArrayList alist1 = new ArrayList();

//        for(int i=0;i<pbretObjForTime.rowCount;i++){
//        alist1.add(pbretObjForTime.getFieldValueString(pbretObjForTime.getViewSequence().get(i), "A_"+(String)measIdList.get(j)));
//        }
//        

        }
        JqplotGraphProperty jqprop = new JqplotGraphProperty();
        String[] colors = (String[]) jqprop.getSeriescolors();
        colors = ProGenJqPlotProperties.seriescolors1;
        ArrayList Seriescolors = new ArrayList();
        for (int i = 0; i < colors.length; i++) {
            Seriescolors.add(colors[i]);
        }

        ArrayList imgtestList = new ArrayList();
        imgtestList.add(imgtest.toString());
        map.put("imgtestList", imgtestList);
        map.put("measIdList", measIdList);
        map.put("measNameList", measNameList);
        map.put("selectedParam", selectedParam);
        map.put("selectedParamVAl", selectedParamVAl);
        map.put("Seriescolors", Seriescolors);




        Gson json = new Gson();
        String jsonString = json.toJson(map);
        PrintWriter out = response.getWriter();
        out.print(jsonString);
        return null;
//    return null;
    }

    public ActionForward refreshOneVIewReg(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(false);
        String oneviewID = request.getParameter("oneViewIdValue");
        PbReturnObject retObj = null;
        OnceViewContainer onecontainer = null;
        OneViewLetDetails detail = new OneViewLetDetails();
        ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();
        OneViewBD oneViewBD = new OneViewBD();
        String regId = request.getParameter("regId");
        String idArr = request.getParameter("idArr");
        String drillviewby = request.getParameter("drillviewby");
        String advHtmlFileProps = (String) request.getSession(false).getAttribute("advHtmlFileProps");
        String oldAdvHtmlFileProps = (String) request.getSession(false).getAttribute("oldAdvHtmlFileProps");
        String fileName = session.getAttribute("tempFileName").toString();
//String idArr="null";
//String viewbydrill="null";
        HashMap<String, String> map = new HashMap<String, String>();

        FileInputStream fis = new FileInputStream(advHtmlFileProps + "/" + fileName);
        ObjectInputStream ois = new ObjectInputStream(fis);
        onecontainer = (OnceViewContainer) ois.readObject();
        ois.close();
        detail = onecontainer.onviewLetdetails.get(Integer.parseInt(regId));
        String result = "";
        HashMap tempRegFileMap = onecontainer.getTempRegHashMap();
        String repid = detail.getRepId();
        detail.setchartdrills(idArr);
        detail.setchartrefreshdrills(idArr);
        detail.setdrillviewby(drillviewby);
        File file1 = new File(advHtmlFileProps + "/" + tempRegFileMap.get(Integer.parseInt(regId)));
        //
        if (file1.exists()) {
            try {
                oneViewBD.form = form;
                oneViewBD.mapping = mapping;
                request.setAttribute("OneviewTiemDetails", onecontainer.timedetails);
                //sandeep
                if (detail.getReptype() != null && detail.getReptype().equalsIgnoreCase("template")) {
                    detail.setRepName("Template");
                }
                //end
                result = oneViewBD.buildRegionData(request, response, onecontainer, detail);
                //sandeep
                if (result != null && (result == null ? "" != null : !result.equals(""))) {
                } else {
                    detail.setReptype("template");
                    detail.setRepName("Template");
                    result = oneViewBD.buildRegionData(request, response, onecontainer, detail);
                }
                //end
                oneViewBD.writeOneviewRegData(onecontainer, result, detail.getNoOfViewLets(), request);
//               oneViewBD.saveOneviewRegData(onecontainer,result,detail.getNoOfViewLets(),request);
                if (detail != null && detail.getRoleId() != null) {
                    oneViewBD.checkRoleAssign(oneviewID, detail.getRoleId());
                }

                String conFileName = reportTemplateDAO.getOneviewFileName(oneviewID);
                FileOutputStream fos = new FileOutputStream(oldAdvHtmlFileProps + "/" + conFileName);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(onecontainer);
                oos.flush();
                oos.close();
//               response.getWriter().print(repid);
                if (detail.getReptype().equalsIgnoreCase("repGraph")) {
                    map.put("result", result);
                    map.put("repid", repid);

                    Gson json = new Gson();
                    String jsonString = json.toJson(map);
                    PrintWriter out = response.getWriter();
                    out.print(jsonString);
                } else {
                    response.getWriter().print(result);
                }
            } catch (Exception e) {
                response.getWriter().print("refresh not completed");
                logger.error("Exception:", e);
            }
        }
        return null;
    }
//by gopesh
    public ActionForward getSingleDrillTrend(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException, ClassNotFoundException, Exception {
        HttpSession session = request.getSession(false);
        String dataset = request.getParameter("dataset");
        int totaltrends = Integer.parseInt(String.valueOf(session.getAttribute("noOfTrends")));
        String datalistObj = request.getParameter("datalistObj");
        String[] datasetObj = datalistObj.split("~");
        String[] datasetstr = dataset.split(",");
        ArrayList datalist = new ArrayList();
        ArrayList vallist = new ArrayList();
        PrintWriter out = response.getWriter();
//        String oneViewId = request.getParameter("oneViewId");
        int num = Integer.parseInt(request.getParameter("num"));
        String names = String.valueOf(session.getAttribute("measName" + num));
        StringBuilder sb = new StringBuilder();
        StringBuilder imgtest = new StringBuilder();
        ProgenJQPlotGraph jqGraph = new ProgenJQPlotGraph();
        if (num == 0) {
            for (int i = 0; i < datasetstr.length; i++) {
                datalist.add("'" + datasetstr[i] + "'");
                vallist.add(datasetstr[i + 1]);
                i++;
            }

            for (int i = 0; i < totaltrends; i++) {
                session.setAttribute("inputhidden" + i, datasetObj[i]);
                imgtest.append("<input id='inputhidden" + i + "' type='hidden' value='" + datasetObj[i].substring(1, datasetObj[i].length() - 1) + "' >");
            }
        } else {
            String[] datasetstr1 = null;
            if (num < totaltrends) {
                String dataliststr = String.valueOf(session.getAttribute("inputhidden" + num));
                dataliststr = dataliststr.substring(1, dataliststr.length());
                datasetstr1 = dataliststr.split(",");
            }
            for (int i = 0; i < datasetstr1.length; i++) {
                datalist.add("'" + datasetstr1[i] + "'");
                vallist.add(datasetstr1[i + 1]);
                i++;

            }
        }
        imgtest.append("<table align='center' style='margin-left:auto;margin-right:auto;'><tr><td><span id='' style='font-size:12px;color:#2191C0;font-weight:bold;text-decoration: none;'>" + names + "</span>");
        imgtest.append("</tr></table>");
        if (num < 0) {
            imgtest.append("<INPUT type='image' src='" + request.getContextPath() + "/icons pinvoke/arrow-skip.png' style='cursor: pointer;width:13px; float:right;height:10px;' title='Next Graph' onclick='showSingleGraph(" + (num - 1) + "," + totaltrends + ")'/>");
        } //            if(num+1!=totaltrends){
        //                out.print("NoData");
        //            //imgtest.append("<INPUT type='image' src='"+request.getContextPath()+"/icons pinvoke/arrow-skip.png' style='cursor: pointer;width:13px; float:right;height:10px;' title='Next Graph' onclick='showSingleGraph("+(num+1)+","+totaltrends+")'/>");
        //            }
        else {
            imgtest.append("<INPUT type='image' src='" + request.getContextPath() + "/icons pinvoke/arrow-skip.png' style='cursor: pointer;width:13px; float:right;height:10px;' title='Next Graph' onclick='showSingleGraph(" + (num + 1) + "," + totaltrends + ")'/>");
        }
        imgtest.append("<div id='chartsingle-" + num + "' style='width:650px; height:560px;top:25px;'></div>");


        imgtest.append("<script>$(\".jqplot-highlighter-tooltip, .jqplot-canvasOverlay-tooltip \").css('font-size', '1em');");
        imgtest.append(jqGraph.getSingleTrendGraph(datalist, vallist, num));
        imgtest.append("</script>");

        //PrintWriter out = response.getWriter();
        out.print(imgtest.toString());
        return null;

    }
//by gopesh
    public ActionForward NoOfRegions(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(false);
        OnceViewContainer onecontainer = null;
        OneViewBD oneviewbd = new OneViewBD();
        String iconVisibility = request.getParameter("icons");
        String advHtmlFileProps = (String) request.getSession(false).getAttribute("advHtmlFileProps");
        String fileName = session.getAttribute("tempFileName").toString();

        FileInputStream fis = new FileInputStream(advHtmlFileProps + "/" + fileName);
        ObjectInputStream ois = new ObjectInputStream(fis);
        onecontainer = (OnceViewContainer) ois.readObject();
        ois.close();
        int size = onecontainer.onviewLetdetails.size();
        if (iconVisibility != null && !iconVisibility.equalsIgnoreCase("al")) {
            onecontainer.setIconVisibility(iconVisibility);
            oneviewbd.writeFileDetails(advHtmlFileProps, fileName, onecontainer);
        } else {
            iconVisibility = onecontainer.getIconVisibility();
        }
        ArrayList alist = new ArrayList();
        alist.add(size);
        alist.add(iconVisibility);
        response.getWriter().print(alist);
        return null;
    }

    public ActionForward enableCustomTimeMeasure(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession(false);
        String advHtmlFileProps = (String) request.getSession(false).getAttribute("advHtmlFileProps");
        OnceViewContainer onecontainer = new OnceViewContainer();
        String divId = request.getParameter("colNo");
        String oneViewIdValue = request.getParameter("oneViewIdValue");
        String isCustomTimeApplied = request.getParameter("isCustomTimeApplied");
        String customMsrDate = request.getParameter("customMsrDate");
        String customMsrDate1 = request.getParameter("customMsrDate1");
        String customMsrDuration = request.getParameter("customMsrDuration");
        String customMsrCompre = request.getParameter("customMsrCompre");
        String customMsrAgr = request.getParameter("customMsrAgr");
        ArrayList customTimeDetails = new ArrayList();
        if (isCustomTimeApplied != null && Boolean.parseBoolean(isCustomTimeApplied)) {
            customTimeDetails.add("Day");
            customTimeDetails.add("PRG_DATE_RANGE");
            String CurrValue = "";
            CurrValue = customMsrDate.substring(3, 5).concat("/").concat(customMsrDate.substring(0, 2)).concat(customMsrDate.substring(5));
            String CurrValue1 = "";
            CurrValue1 = customMsrDate1.substring(3, 5).concat("/").concat(customMsrDate1.substring(0, 2)).concat(customMsrDate1.substring(5));
            customTimeDetails.add(CurrValue);
            customTimeDetails.add(CurrValue1);
            customTimeDetails.add(CurrValue);
            customTimeDetails.add(CurrValue1);
//          customTimeDetails.add(customMsrCompre);
        }
        String result = "";
        if (session != null) {
            OneViewLetDetails detail = null;
            String fileName = request.getSession(false).getAttribute("tempFileName").toString();
            FileInputStream fis2 = new FileInputStream(advHtmlFileProps + "/" + fileName);
            ObjectInputStream ois2 = new ObjectInputStream(fis2);
            onecontainer = (OnceViewContainer) ois2.readObject();
            ois2.close();
            List<OneViewLetDetails> oneviewletDetails = onecontainer.onviewLetdetails;
            for (int i = 0; i < oneviewletDetails.size(); i++) {
                detail = oneviewletDetails.get(i);
                String regionid = detail.getNoOfViewLets();
                if (regionid != null && regionid.equalsIgnoreCase(divId)) {

//                detail = onecontainer.onviewLetdetails.get(Integer.parseInt(divId));
//                DashboardTemplateBD dashboardTemplateBD = new DashboardTemplateBD();

                    if (isCustomTimeApplied != null && Boolean.parseBoolean(isCustomTimeApplied)) { // here we are applying custom time we need to fire query
                        detail.setoneviewcustomtimedetails(true);  //taking custom timedetails given by user
                        detail.setMsrCustomTimeDetails(customTimeDetails);
                        detail.setCustomTimeDetails(customTimeDetails);
//                  onecontainer.timedetails=customTimeDetails;
                    } else {
                        detail.setoneviewcustomtimedetails(false); // taking oneview time details
                    }
                    detail.setMeasurOprFlag(false);
//                  detail.setMsrCustomAggregation(customMsrAgr);
//                  result=dashboardTemplateBD.getMeasureDetailsData(request, response, session,onecontainer, detail);
                    OneViewBD viewBd = new OneViewBD();
//                result=viewBd.buildRegionData(request, response, onecontainer, detail);
                    FileOutputStream fos = new FileOutputStream(advHtmlFileProps + "/" + fileName);
                    ObjectOutputStream oos = new ObjectOutputStream(fos);
                    oos.writeObject(onecontainer);
                    oos.flush();
                    oos.close();
                }
            }
        }
        response.getWriter().print(result);
        return null;
    }

    public ActionForward getCustomTimeMsrDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession(false);
        String advHtmlFileProps = (String) request.getSession(false).getAttribute("advHtmlFileProps");
        HashMap map = null;
        OnceViewContainer onecontainer = new OnceViewContainer();
        String divId = request.getParameter("colNo");
        String oneViewIdValue = request.getParameter("oneViewIdValue");
        JSONObject jsonMap = new JSONObject();
        ArrayList customTimeDetails = new ArrayList();
        if (session != null) {
            OneViewLetDetails detail = null;
            ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();
            String fileName = session.getAttribute("tempFileName").toString();
            FileInputStream fis2 = new FileInputStream(advHtmlFileProps + "/" + fileName);
            ObjectInputStream ois2 = new ObjectInputStream(fis2);
            onecontainer = (OnceViewContainer) ois2.readObject();
            ois2.close();
//                detail = onecontainer.onviewLetdetails.get(Integer.parseInt(divId));
            List<OneViewLetDetails> oneviewletDetails = onecontainer.onviewLetdetails;
            for (int i = 0; i < oneviewletDetails.size(); i++) {
                detail = oneviewletDetails.get(i);
                String regionid = detail.getNoOfViewLets();
                if (regionid != null && regionid.equalsIgnoreCase(divId)) {
                    customTimeDetails.add(detail.isoneviewcustomtimedetails());    //cutomtimeenabled flag
                    if (detail.isoneviewcustomtimedetails()) {
                        ArrayList timeDetails = detail.getCustomTimeDetails();
                        String tempdate = timeDetails.get(2).toString();
                        String date = tempdate.substring(3, 5).concat("/").concat(tempdate.substring(0, 2)).concat(tempdate.substring(5));
                        String tempdate1 = timeDetails.get(3).toString();
                        String date1 = tempdate1.substring(3, 5).concat("/").concat(tempdate1.substring(0, 2)).concat(tempdate1.substring(5));
//                       
                        customTimeDetails.add(date);
                        customTimeDetails.add(date1);
//                       customTimeDetails.add(timeDetails.get(4).toString());
                    }
                    logger.info("****detail.getMsrCustomAggregation()" + detail.getMsrCustomAggregation());

                    if (detail.getMsrCustomAggregation() != null) {
                        jsonMap.put("customMsrAgr", detail.getMsrCustomAggregation());
                    } else {
                        PbDb db = new PbDb();
                        String query = "select ELEMENT_ID , AGGREGATION_TYPE from  PRG_USER_ALL_INFO_DETAILS where ELEMENT_ID in (" + detail.getRepId() + ")";
                        PbReturnObject retObj = null;
                        try {
                            retObj = db.execSelectSQL(query);
                        } catch (Exception e) {
                            logger.error("Exception:", e);
                        }
                        if (retObj != null && retObj.getRowCount() > 0) {
                            jsonMap.put("customMsrAgr", retObj.getFieldValueString(0, 1));
                        }
                    }
                }
            }
            jsonMap.put("customTimeDetails", customTimeDetails);
            response.getWriter().print(jsonMap.toString());
        }
        return null;
    }

    public ActionForward buildOneviewTemplateGraph(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession(false);
        HashMap map = new HashMap();
        HashMap onemap = new HashMap();
        Container container = null;
        String reportId = "";
        String userId = "";
        String action1 = "";
        String oneviewID = "";
        if (session != null) {
            String advHtmlFileProps = (String) request.getSession(false).getAttribute("advHtmlFileProps");
            String fileName = null;
            userId = (String) session.getAttribute("USERID");
            action1 = request.getParameter("action1");
            reportId = request.getParameter("oneViewIdValue");
            map = (HashMap) session.getAttribute("PROGENTABLES");
            container = (Container) map.get(reportId);
            oneviewID = reportId;
            OnceViewContainer onecontainer = null;
            onemap = (HashMap) session.getAttribute("ONEVIEWDETAILS");
            if (onemap != null) {
                onecontainer = (OnceViewContainer) onemap.get(oneviewID);
                onecontainer.setUserId(userId);
            } else {
                fileName = request.getSession(false).getAttribute("tempFileName").toString();
                FileInputStream fis2 = new FileInputStream(advHtmlFileProps + "/" + fileName);
                ObjectInputStream ois2 = new ObjectInputStream(fis2);
                onecontainer = (OnceViewContainer) ois2.readObject();
                ois2.close();
            }
            OneViewBD viewbd = new OneViewBD();
            String result = null;
            PbReportViewerBD viewerBd = new PbReportViewerBD();
            container.setOneviewGraphTimedetails((ArrayList<String>) onecontainer.timedetails);
            container.setgTAverage("false");
            container.setBusTemplateFromOneview(true);
            viewerBd.prepareReport(action1, container, container.getReportCollect().reportId, userId, new HashMap());
            HashMap GraphClassesHashMap = (HashMap) session.getAttribute("GraphClassesHashMap");
            HashMap GraphSizesDtlsHashMap = (HashMap) session.getAttribute("GraphSizesDtlsHashMap");
            HashMap GraphHashMap = container.getGraphHashMap();
            ReportTemplateBD reportBD = new ReportTemplateBD();
            int grphId = 0;
            String grpId = String.valueOf(grphId);
            reportBD.request = request;
            if (action1 != null && action1.equalsIgnoreCase("fromDesigner")) {
                GraphHashMap = reportBD.setDefaults("0", "Bar", container, GraphSizesDtlsHashMap, GraphClassesHashMap, null, null, null);
                container.setGraphHashMap(GraphHashMap);
            }
            reportBD.buildGraph(container, request, response, "0");
            viewbd.generateTemplateMeasureReturnObject(container, userId);
            onecontainer.setContainer(container);
            ReportTemplateBD bd = new ReportTemplateBD();
            DataFacade facade = new DataFacade(container);
            facade.setUserId(userId);
            facade = bd.generateReportQrys(reportId, facade, userId);
            onecontainer.setFacade(facade);
//            result=viewbd.generateContainerforTemplateGraph(request,response,action, container,userId,onecontainer);
            result = viewbd.generateBusinessTemplateRegion(request, response, onecontainer);
            if (onemap != null) {
            } else {
                FileOutputStream fos = new FileOutputStream(advHtmlFileProps + "/" + fileName);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(onecontainer);
                oos.flush();
                oos.close();
            }
            response.getWriter().print(result);
        }
        return null;
    }

    public ActionForward changeBusinessTemplateGraphColumn(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession(false);
//       HashMap onemap = new HashMap();
        OnceViewContainer onecontainer = null;
        String advHtmlFileProps = (String) request.getSession(false).getAttribute("advHtmlFileProps");
        String oneviewID = request.getParameter("REPORTID");
        String graphChange = request.getParameter("graphChange");
        String viewletId = request.getParameter("viewletId");
//       String viewletId=request.getParameter("viewletId");
//       onemap = (HashMap) session.getAttribute("ONEVIEWDETAILS");
//       onecontainer = (OnceViewContainer) onemap.get(oneviewID);
        String fileName = request.getSession(false).getAttribute("tempFileName").toString();
        FileInputStream fis2 = new FileInputStream(advHtmlFileProps + "/" + fileName);
        ObjectInputStream ois2 = new ObjectInputStream(fis2);
        onecontainer = (OnceViewContainer) ois2.readObject();
        ois2.close();
        OneViewLetDetails detail = null;
        if (viewletId != null && viewletId != "") {
            detail = (OneViewLetDetails) onecontainer.onviewLetdetails.get(Integer.parseInt(viewletId));
        }
        Container container = onecontainer.getContainer();
        container.setSessionContext(session, container);
        OneViewBD oneViewBD = new OneViewBD();
        String result = "";
        if (graphChange != null && graphChange.equalsIgnoreCase("GrpType")) {
            result = oneViewBD.changeGraphType(request, response, onecontainer, detail);
        } else {
            if (onecontainer.getOneViewType().equalsIgnoreCase("Business TemplateView")) {
                ReportViewerAction viewerAction = new ReportViewerAction();
                ActionForward graphColumnChanges = viewerAction.graphColumnChanges(mapping, form, request, response);
                result = oneViewBD.generatebusinessTemplateGraph(request, response, onecontainer, detail);
            } else {
                result = oneViewBD.MeasureChangeInMsrBsedTemplate(mapping, form, request, response, oneviewID, onecontainer);
                response.getWriter().print(result);
                return null;
            }
        }
        oneViewBD.writeOneviewRegData(onecontainer, result, detail.getNoOfViewLets(), request);
        FileOutputStream fos = new FileOutputStream(advHtmlFileProps + "/" + fileName);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(onecontainer);
        oos.flush();
        oos.close();
        response.getWriter().print(result);
        return null;
    }

    public ActionForward changeBusinessTemplateRowViewby(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        String onviewId = request.getParameter("oneviewId");
        String rowviewbyId = request.getParameter("rowviewby");
        OneViewBD oneBd = new OneViewBD();
        String result = oneBd.changeViewbyBusinessTemplateData(request, response, onviewId, rowviewbyId);
        response.getWriter().print(result);
        return null;
    }

    public ActionForward getonename(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        String onviewId = request.getParameter("oneViewIdValue");
        String qry = "select ONEVIEW_NAME from prg_ar_oneview_file_data where ONEVIEWID=" + onviewId + "";
        PbDb pbdb = new PbDb();
        String oneviewname = "";
        PbReturnObject pbd = pbdb.execSelectSQL(qry);
//            int rowCount = pbd.getRowCount();
        if (pbd.getRowCount() != 0) {
            oneviewname = pbd.getFieldValueString(0, 0);
        }
        response.getWriter().print(oneviewname);
        return null;
    }

    public ActionForward getOneviewBusinessRole(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession(false);
        OnceViewContainer onecontainer = null;
        String advHtmlFileProps = (String) request.getSession(false).getAttribute("advHtmlFileProps");
        String fileName = request.getSession(false).getAttribute("tempFileName").toString();
        FileInputStream fis2 = new FileInputStream(advHtmlFileProps + "/" + fileName);
        ObjectInputStream ois2 = new ObjectInputStream(fis2);
        onecontainer = (OnceViewContainer) ois2.readObject();
        ois2.close();
        Container container = onecontainer.getContainer();
        String roleId = (String) container.getParametersHashMap().get("UserFolderIds");
        String oneviewId = onecontainer.oneviewId;
        HashMap map = new HashMap();
        map.put("roleId", roleId);
        map.put("oneviewId", oneviewId);
        map.put("oneviewType", onecontainer.getOneViewType());
        Gson gson = new Gson();
        String gsonString = gson.toJson(map);
        response.getWriter().print(gsonString);
        return null;
    }

    public ActionForward buildMesureBasedTemplateData(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {

        HttpSession session = request.getSession(false);
        HashMap map = new HashMap();
        HashMap onemap = new HashMap();
        Container container = null;
        String reportId = "";
        String userId = "";
        String action1 = "";
        String oneviewID = "";
        if (session != null) {
            String advHtmlFileProps = (String) request.getSession(false).getAttribute("advHtmlFileProps");
            String fileName = null;
            userId = (String) session.getAttribute("USERID");
            action1 = request.getParameter("action1");
            reportId = request.getParameter("oneViewIdValue");
            map = (HashMap) session.getAttribute("PROGENTABLES");
            container = (Container) map.get(reportId);
            oneviewID = reportId;
            OnceViewContainer onecontainer = null;
            onemap = (HashMap) session.getAttribute("ONEVIEWDETAILS");
            if (onemap != null) {
                onecontainer = (OnceViewContainer) onemap.get(oneviewID);
                onecontainer.setUserId(userId);
            } else {
                fileName = request.getSession(false).getAttribute("tempFileName").toString();
                FileInputStream fis2 = new FileInputStream(advHtmlFileProps + "/" + fileName);
                ObjectInputStream ois2 = new ObjectInputStream(fis2);
                onecontainer = (OnceViewContainer) ois2.readObject();
                ois2.close();
            }
            OneViewBD viewbd = new OneViewBD();
            String result = null;
            PbReportViewerBD viewerBd = new PbReportViewerBD();
            container.setOneviewGraphTimedetails((ArrayList<String>) onecontainer.timedetails);
            container.setgTAverage("false");
            container.setBusTemplateFromOneview(true);
            viewerBd.prepareReport(action1, container, container.getReportCollect().reportId, userId, new HashMap());
//            result=viewbd.generateBusinessTemplateRegion(request,response,onecontainer);
//            result=viewbd.generateMsrBasedTemplateReturnObject(oneviewID, onecontainer, container);

            HashMap GraphClassesHashMap = (HashMap) session.getAttribute("GraphClassesHashMap");
            HashMap GraphSizesDtlsHashMap = (HashMap) session.getAttribute("GraphSizesDtlsHashMap");
            HashMap GraphHashMap = container.getGraphHashMap();
            ReportTemplateBD reportBD = new ReportTemplateBD();
            int grphId = 0;
            String grpId = String.valueOf(grphId);
            reportBD.request = request;
            if (action1 != null && action1.equalsIgnoreCase("fromDesigner")) {
                GraphHashMap = reportBD.setDefaults("0", "Bar", container, GraphSizesDtlsHashMap, GraphClassesHashMap, null, null, null);
                container.setGraphHashMap(GraphHashMap);
            }
            reportBD.buildGraph(container, request, response, "0");
            if (action1 != null && action1.equalsIgnoreCase("measChange")) {
                onecontainer.setElementIds((String) ((List) container.getTableHashMap().get("Measures")).get(0));
            }
            viewbd.generateMsrTemplateReturnObject(oneviewID, onecontainer, container);
            onecontainer.setContainer(container);
            ReportTemplateBD bd = new ReportTemplateBD();
            DataFacade facade = new DataFacade(container);
            facade.isMsrbasedBusTemplate = true;
            facade.setUserId(userId);
            facade = bd.generateReportQrys(reportId, facade, userId);
            onecontainer.setFacade(facade);
            if (action1 != null && action1.equalsIgnoreCase("fromDesigner")) {
                result = viewbd.generateBusinessTemplateRegion(request, response, onecontainer);
            } else {
                result = viewbd.generateBusinessTemplateView(request, response, onecontainer);
            }
            if (onemap != null) {
            } else {
                FileOutputStream fos = new FileOutputStream(advHtmlFileProps + "/" + fileName);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(onecontainer);
                oos.flush();
                oos.close();
            }
            response.getWriter().print(result);
        }
        return null;
    }

    public ActionForward deleteregion(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        String oneviewid = request.getParameter("oneviewID");
        OnceViewContainer onecontainer1 = null;
        String regid = request.getParameter("regid");
        String action = request.getParameter("action");
        HashMap map = new HashMap();
        HttpSession session = request.getSession(false);
        String userId = String.valueOf(request.getSession(false).getAttribute("USERID"));
        String oldAdvHtmlFileProps = null;
        String advHtmlFileProps = null;
//            ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();
        String fileName = null;
        File file = null;
        if (session.getAttribute("ONEVIEWDETAILS") != null) {
            map = (HashMap) session.getAttribute("ONEVIEWDETAILS");
            if (map != null) {
                onecontainer1 = (OnceViewContainer) map.get(oneviewid);
            }
        } else {
            oldAdvHtmlFileProps = (String) request.getSession(false).getAttribute("oldAdvHtmlFileProps");
            advHtmlFileProps = (String) request.getSession(false).getAttribute("advHtmlFileProps");
            String isseurity = (String) request.getSession(false).getAttribute("isseurity");
            ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();
            PbReturnObject securityfilters = null;
            securityfilters = (PbReturnObject) request.getSession(false).getAttribute("securityfilters");
            fileName = reportTemplateDAO.getOneviewFileName(oneviewid);
            file = new File(oldAdvHtmlFileProps + "/" + fileName);
            if (file.exists()) {
                FileInputStream fis = new FileInputStream(oldAdvHtmlFileProps + "/" + fileName);
                ObjectInputStream ois = new ObjectInputStream(fis);
                onecontainer1 = (OnceViewContainer) ois.readObject();
                ois.close();
            }
        }
        List<OneViewLetDetails> oneviewletDetails = onecontainer1.onviewLetdetails;
        List<OneViewLetDetails> oneviewletDetails1 = new ArrayList<OneViewLetDetails>();
//             
        for (int i = 0; i < oneviewletDetails.size(); i++) {
            OneViewLetDetails detail = oneviewletDetails.get(i);
            String regionid = detail.getNoOfViewLets();
            if (regionid != null && regionid.equalsIgnoreCase(regid)) {
            } else {
                oneviewletDetails1.add(detail);
            }
        }
        onecontainer1.onviewLetdetails.clear();
        onecontainer1.onviewLetdetails = oneviewletDetails1;
        if (session.getAttribute("ONEVIEWDETAILS") != null) {
            map = (HashMap) session.getAttribute("ONEVIEWDETAILS");
            if (map != null) {
                map.put(oneviewid, onecontainer1);
                session.setAttribute("ONEVIEWDETAILS", map);
            }
        } else {

            FileOutputStream fos = new FileOutputStream(oldAdvHtmlFileProps + "/" + fileName);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(onecontainer1);
            oos.flush();
            oos.close();
            fileName = (String) request.getSession(false).getAttribute("tempFileName").toString();
            fos = new FileOutputStream(advHtmlFileProps + "/" + fileName);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(onecontainer1);
            oos.flush();
            oos.close();
        }
        return null;
    }

    public ActionForward saveoneviewjson(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        PbReportViewerDAO dao = new PbReportViewerDAO();
        ReportManagementDAO daomg = new ReportManagementDAO();
        String reportId = request.getParameter("reportId");
        String reportName = request.getParameter("reportName");
        HttpSession session = request.getSession(false);
        String fromoneview = request.getParameter("fromoneview");
        String busrolename = request.getParameter("busrolename");
        String oneviewid = request.getParameter("oneviewID");
        String chartname = request.getParameter("chartname");
        String chartname1 = request.getParameter("graphName");
        boolean oneviewtime = Boolean.parseBoolean(request.getParameter("oneviewtime"));
        String regid = request.getParameter("regid");
        String action = request.getParameter("action");
        request.setAttribute("fromoneview", fromoneview);
        request.setAttribute("busrolename", busrolename);
        Container container = Container.getContainerFromSession(request, reportId);
        String report = "";
        String report1 = "";
        String meta11 = "";

        String filePath = "";

//       if(session != null){
//         filePath = dao.getFilePath(session);
//       }else {
        filePath = "/usr/local/cache";
//       }
        report = dao.getReports(reportId, reportName, container, fromoneview, busrolename, filePath, session, request);

        Map map = new HashMap();
        Gson gson = new Gson();
        Type tarType1 = new TypeToken<Map<String, String>>() {
        }.getType();
        map = gson.fromJson(report, tarType1);
        String data = (String) map.get("data");
        String meta = (String) map.get("meta");
        if (request.getParameter("chartData") != null) {
            XtendReportMeta reportMeta = new XtendReportMeta();
            Type metaType = new TypeToken<XtendReportMeta>() {
            }.getType();
            String meta1 = request.getParameter("chartData");
            Type tarType11 = new TypeToken<Map<String, DashboardChartData>>() {
            }.getType();
            Type tarType111 = new TypeToken<String>() {
            }.getType();
            Map<String, DashboardChartData> chartData = gson.fromJson(meta1, tarType11);

            Set keySet = chartData.keySet();
            Iterator itr = keySet.iterator();
            String key = "";
//         report1 = dao.geroneviewcharts(reportId,reportName,container,oneviewid,regid);
            if (report1 != null && !report1.equalsIgnoreCase("false")) {
                map = gson.fromJson(report1, tarType1);
                if (map != null) {
                    String data1 = (String) map.get("data");
                    meta11 = (String) map.get("meta");
                    if (meta11 != null) {
                        meta = meta11;
                    }
                }
            }
            Map<String, DashboardChartData> chartData1;
            while (itr.hasNext()) {
                key = itr.next().toString();
                if (chartname != null && chartname.equalsIgnoreCase(key)) {
                    DashboardChartData chartvale = chartData.get(key);
                    reportMeta = gson.fromJson(meta, metaType);
                    chartData1 = reportMeta.getChartData();
                    chartData1.put(key, chartvale);
                    reportMeta.setChartData(chartData1);
                    if (request.getParameter("filters1") != null) {
                        boolean flag = false;
                        if ((request.getParameter("isNewReport") != null && request.getParameter("isNewReport").equalsIgnoreCase("true"))) {
                            flag = true;
                        } else {
                            flag = false;
                        }
                        if (!flag) {
                            Type tarType1111 = new TypeToken<Map<String, List<String>>>() {
                            }.getType();
                            Map<String, List<String>> map1 = gson.fromJson(request.getParameter("filters1"), tarType1111);
                            reportMeta.setFilterMap(map1);
                        }
                    } else {
                        Map<String, List<String>> map1 = null;
                        reportMeta.setFilterMap(map1);
                    }
                    meta = gson.toJson(reportMeta);
                }
            }


        }
        XtendAdapter adapter = new XtendAdapter();
        Map<String, String> dataMapgblsave = new HashMap<String, String>();
        if (request.getSession(false).getAttribute("dataMapgblsave") != null) {
            dataMapgblsave = (Map<String, String>) request.getSession(false).getAttribute("dataMapgblsave");
//              }
            String data1 = dataMapgblsave.get(regid);
            FileReadWrite fileReadWrite = new FileReadWrite();
            File datafile = new File("/usr/local/cache/OneviewGO/oneview_" + oneviewid + "/oneview_" + oneviewid + "_" + regid + "/" + reportName.replaceAll("\\W", "").trim() + "_" + reportId + ".json");
            if (datafile.exists() && data1 != null) {
//           fileReadWrite.writeToFile("/usr/local/cache/OneviewGO/oneview_"+oneviewid+"/oneview_"+oneviewid+"_"+regid+"/"+reportName.replaceAll("\\W", "").trim()+"_"+reportId+".json", data1);
                data = data1;
            }
            adapter.saveoneviewChartMeta(reportName, reportId, meta, data, oneviewid, regid, "null", "null", chartname);
//                }
        } else {
//    if(report==null){
            adapter.saveoneviewChartMeta(reportName, reportId, meta, data, oneviewid, regid, "null", "null", chartname);
//        }
        }
        return null;
    }
    // sandeep to get date and datetype for oneview

    public ActionForward getContainerFromSession(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession(false);
//         HashMap map;
        String oneviewId = request.getParameter("oneViewIdValue");
        String oneviewtypedate = (String) session.getAttribute("oneviewdatetype");
        OnceViewContainer onecontainer = null;
        if (session != null) {
            HashMap onemap = new HashMap();
            onemap = (HashMap) session.getAttribute("ONEVIEWDETAILS");
            if (onemap != null) {
                onecontainer = (OnceViewContainer) onemap.get(oneviewId);
//              onecontainer.setUserId(userId);
            } else {
                String advHtmlFileProps = session.getAttribute("advHtmlFileProps").toString();
                String fileName = session.getAttribute("tempFileName").toString();
                if (session.getAttribute("tempFileName") != null) {
                    FileInputStream fis1 = new FileInputStream(advHtmlFileProps + "/" + fileName);
                    ObjectInputStream ois1 = new ObjectInputStream(fis1);
                    onecontainer = (OnceViewContainer) ois1.readObject();
                    ois1.close();
                }
            }

            onecontainer.evaluateReportDateHeaders(onecontainer, oneviewtypedate);
            HashMap<String, List> map = new HashMap<String, List>();
            ArrayList<String> timedetails = new ArrayList<String>();
            ArrayList<String> timedetails1 = new ArrayList<String>();
            ArrayList<String> timedetailscf = new ArrayList<String>();
            ArrayList<String> timedetails1ct = new ArrayList<String>();
            ArrayList<String> datetype = new ArrayList<String>();
            ArrayList<String> durationvale = new ArrayList<String>();
            datetype.add(onecontainer.datetype);
            timedetails.add(onecontainer.day0);
            timedetails.add(onecontainer.dated);
            timedetails.add(onecontainer.fullName0);
            timedetails1.add(onecontainer.day);
            timedetails1.add(onecontainer.date);
            timedetails1.add(onecontainer.fullName);
            if (oneviewtypedate != null && oneviewtypedate.equalsIgnoreCase("false")) {
                timedetailscf.add(onecontainer.cfday);
                timedetailscf.add(onecontainer.cfdate);
                timedetailscf.add(onecontainer.cffullName);
                timedetails1ct.add(onecontainer.ctday);
                timedetails1ct.add(onecontainer.ctdate);
                timedetails1ct.add(onecontainer.ctfullName);
            }

            map.put("datetype", datetype);
            map.put("timedetails", timedetails);
            map.put("timedetails1", timedetails1);
            if (oneviewtypedate != null && oneviewtypedate.equalsIgnoreCase("false")) {
                map.put("timedetailscf", timedetailscf);
                map.put("timedetails1ct", timedetails1ct);
                if (onecontainer.timedetails.get(1).equalsIgnoreCase("PRG_STD")) {
                    String value = onecontainer.timedetails.get(3);
                    String valu = onecontainer.timedetails.get(4);
                    durationvale.add(value);
                    durationvale.add(valu);
                    map.put("durationvale", durationvale);

                }
            }
//           response.getWriter().print(timedetails);
            Gson json = new Gson();
            String jsonString = json.toJson(map);
            PrintWriter out = response.getWriter();
            out.print(jsonString);
        }
        return null;
    }
}
