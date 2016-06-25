/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.oneView.bd;

import com.google.common.collect.ArrayListMultimap;
import com.google.gson.Gson;
import com.itextpdf.text.BaseColor;
import com.progen.bd.ProgenJqplotGraphBD;
import com.progen.charts.JqplotGraphProperty;
import com.progen.charts.ProgenJQPlotGraph;
import com.progen.dashboardView.action.PbDashboardViewerAction;
import com.progen.dashboardView.db.DashboardViewerDAO;
import com.progen.db.ProgenDataSet;
import com.progen.jqplot.ProGenJqPlotChartTypes;
import com.progen.report.charts.PbGraphDisplay;
import com.progen.report.data.DataFacade;
import com.progen.report.display.util.NumberFormatter;
import com.progen.report.entities.KPIGraph;
import com.progen.report.kpi.KPIBuilder;
import com.progen.report.*;
import com.progen.report.query.PbReportQuery;
import com.progen.report.query.PbTimeRanges;
import com.progen.report.query.QueryExecutor;
import com.progen.reportdesigner.bd.DashboardTemplateBD;
import com.progen.reportdesigner.bd.ReportTemplateBD;
import com.progen.reportdesigner.db.*;
import com.progen.reportview.action.ReportViewerAction;
import com.progen.reportview.bd.PbReportViewerBD;
import com.progen.reportview.db.PbReportViewerDAO;
import com.progen.scheduler.ReportSchedule;
import java.io.*;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import prg.db.*;
import prg.util.PbOneViewPdfDriver;
import utils.db.ProgenConnection;

/**
 * added by srikanth.p@progenbusiness.com
 *
 * @author progen
 */
public class OneViewBD {

    public static Logger logger = Logger.getLogger(OneViewBD.class);
    ResourceBundle resourceBundle;
    private String fileName;
    private String localPath;
    private String globalPath;
    public String userId;
    public HttpServletRequest request;
    public HttpServletResponse response;
    public ActionMapping mapping;
    public ActionForm form;

    private ResourceBundle getResourceBundle() {
        if (this.resourceBundle == null) {
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                resourceBundle = new ReportTemplateResBundleSqlServer();
            } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                resourceBundle = new ReportTemplateResourceBundleMySql();
            } else {
                resourceBundle = new ReportTemplateResourceBundle();
            }
        }

        return resourceBundle;
    }

    public String getOneViewNotesData(OnceViewContainer oneContainer, String oneViewId, OneViewLetDetails oneviewlet, boolean isDesigner) {
        StringBuilder notesData = new StringBuilder();
//         OneViewLetDetails oneviewlet=oneContainer.onviewLetdetails.get(viewletId);


        if (isDesigner) {
            //notesData.append("<td id='" + oneviewlet.getNoOfViewLets() + "' width='" + oneviewlet.getWidth() + "px'  style='height:" + oneviewlet.getHeight() + "px;' rowspan='" + oneviewlet.getRowSpan() + "' colspan='" + oneviewlet.getColSpan() + "'>");


            //belowe code builds the header for oneView Notes
            notesData.append("<table style='margin-left: 10px;width:100%'>"); //kruthika
            notesData.append("<tr >");
            notesData.append("<td id=\"Dashlets" + oneviewlet.getNoOfViewLets() + "\" style='font-size:12pt;color:#000000;white-space:nowrap;' title=\"" + oneviewlet.getRepName() + "\" >" + oneviewlet.getRepName() + "</td>");

//         notesData.append("<td style='width:0px;align:right;'><a href='javascript:void(0)' class=\"ui-icon ui-icon-pencil\" onclick=\"renameNote('"+oneViewId+"',"+oneviewlet.getNoOfViewLets()+"')\"  style='text-decoration:none'  title=\"Rename Note\"></a></td>");
//         notesData.append("<td style='width:0px;align:right;'><a href='javascript:void(0)' class=\"ui-icon ui-icon-trash\" onclick=\"deleteNote('"+oneViewId+"','"+oneviewlet.getNoOfViewLets()+"')\"  style='text-decoration:none'  title=\"Delete Note\"></a></td>");
            notesData.append("<td id=\"refreshTabId" + oneviewlet.getNoOfViewLets() + "\" style='width:1%;align:right;'><a style=\"text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;font-size: 9pt;\" class=\"ui-icon ui-icon-arrowrefresh-1-w\" title=\"Refresh Region\" onclick=\"refreshOneVIewReg(" + oneviewlet.getNoOfViewLets() + ")\" href=\"#\"></a></td>");
            notesData.append("<td id=\"saveTabId" + oneviewlet.getNoOfViewLets() + "\" style='width:1%;align:right;'><a style=\"text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;font-size: 9pt;\" class=\"ui-icon ui-icon-disk\" title=\"Save\" onclick=\"saveEachOneVIewReg(" + oneviewlet.getNoOfViewLets() + ")\" href=\"#\"></a></td>");
            if (oneviewlet.getUserStatus()) {
                notesData.append("<td style='width:0px;align:right;'><a href='javascript:void(0)' class=\"ui-icon ui-icon-comment\" onclick=\"writeNote('" + oneViewId + "','" + oneviewlet.getNoOfViewLets() + "')\"  style='text-decoration:none'  title=\"Write Note\"></a></td>");
            }
            if (oneviewlet.getUserStatus()) {
                notesData.append("<td id=\"optionId" + oneviewlet.getNoOfViewLets() + "\" style='width:1%;align:right;'>");
                notesData.append("<a href='javascript:void(0)' class=\"ui-icon ui-icon-plusthick\" onclick=\"selectReadd(" + oneviewlet.getNoOfViewLets() + ")\"  style='text-decoration:none'  title=\"Re Add Onevielet\"></a>");

                notesData.append("<div id=\"readdDivId" + oneviewlet.getNoOfViewLets() + "\" style='display:none; width:90px; height:100px; background-color:#ffffff; overflow:auto;  position:absolute; text-align:left; border:1px solid #000000; border-top-width: 0px; z-index:1002;'>");
                notesData.append("<table border='0' align='left' >");
                notesData.append("<tr><td>");

                notesData.append("<table><tr><td><a href='javascript:void(0)' style='text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;' onclick=\"selectedType('report','Dashlets-" + oneviewlet.getNoOfViewLets() + "'," + oneviewlet.getNoOfViewLets() + "," + oneviewlet.getWidth() + "," + oneviewlet.getHeight() + ",'Dashlets" + oneviewlet.getNoOfViewLets() + "','GrpTyp" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "')\" >Reports</a></td></tr></table>");
                notesData.append("<table><tr><td><a href='javascript:void(0)' style='text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;' onclick=\"selectedType('measures','Dashlets-" + oneviewlet.getNoOfViewLets() + "'," + oneviewlet.getNoOfViewLets() + "," + oneviewlet.getWidth() + "," + oneviewlet.getHeight() + ",'Dashlets" + oneviewlet.getNoOfViewLets() + "','GrpTyp" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "')\" >Measures</a></td></tr></table>");

                notesData.append("<table><tr><td><a href='javascript:void(0)' style='text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;' onclick=\"selectedType('dashboard','Dashlets-" + oneviewlet.getNoOfViewLets() + "'," + oneviewlet.getNoOfViewLets() + "," + oneviewlet.getWidth() + "," + oneviewlet.getHeight() + ",'Dashlets" + oneviewlet.getNoOfViewLets() + "','GrpTyp" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "')\" >KPIs</a></td></tr></table>");
                notesData.append("<table><tr><td><a href='javascript:void(0)' style='text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;' onclick=\"selectedType('headline','Dashlets-" + oneviewlet.getNoOfViewLets() + "'," + oneviewlet.getNoOfViewLets() + "," + oneviewlet.getWidth() + "," + oneviewlet.getHeight() + ",'Dashlets" + oneviewlet.getNoOfViewLets() + "','GrpTyp" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "')\" >Headlines</a></td></tr></table>");
                notesData.append("<table><tr><td><a href='javascript:void(0)' style='text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;white-space:nowrap;' onclick=\"selectedType('complexkpi','Dashlets-" + oneviewlet.getNoOfViewLets() + "'," + oneviewlet.getNoOfViewLets() + "," + oneviewlet.getWidth() + "," + oneviewlet.getHeight() + ",'Dashlets" + oneviewlet.getNoOfViewLets() + "','GrpTyp" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "')\" >Custom KPI</a></td></tr></table>");
                notesData.append("</td></tr>");
                notesData.append("</table>");
                notesData.append("</div>");
                notesData.append("</td>");
            }
            notesData.append("</tr>");
            notesData.append("</table>");
            notesData.append("<div id='Dashlets-" + oneviewlet.getNoOfViewLets() + "' class='ui-tabs ui-widget ui-widget-content ui-corner-all' style='border-bottom: medium hidden LightGrey ; border-left: medium hidden LightGrey ; border-right: medium hidden LightGrey ; border-color: LightGrey ; width: 100%; height: 100%; margin-left: 10px; margin-right: 10px;'>");
            //header Region built
            //belowe code to build region of note Viewlet
            notesData.append("<div id='note-" + oneviewlet.getNoOfViewLets() + "' style='width:" + oneviewlet.getWidth() + "px; height:" + oneviewlet.getHeight() + "px;'>");
        }



        HashMap<String, ArrayList> notesMap = oneviewlet.noteMap;
        if (notesMap != null) {
            if (notesMap.isEmpty()) {
                notesData.append("<div id='note-" + oneviewlet.getNoOfViewLets() + "' style='color:#d0d0d0;font-size:25pt;position :absolute;font-family:verdana;width:" + oneviewlet.getWidth() + "px; height:" + oneviewlet.getHeight() + "px;'><p align='center'>Notes</p></div>");
            } else {
                Set noteKeySet = notesMap.keySet();
                Iterator iter = noteKeySet.iterator();
                String noteWriter = "";
                ArrayList note = new ArrayList();
                notesData.append("<div class='notesStyle' style='width:" + oneviewlet.getWidth() + "px;height:" + oneviewlet.getHeight() + "px;overflow-y:auto;overflow-x:hide'>");
//                notesData.append("<table border='0' style='width:"+oneviewlet.getWidth()+"px; height:"+oneviewlet.getHeight()+"px;'>");
//                notesData.append("<tr>");
//                    notesData.append("<td>");
                while (iter.hasNext()) {
                    noteWriter = iter.next().toString();
                    note = (ArrayList) notesMap.get(noteWriter);

//                    notesData.append("<ul>");
//                    notesData.append("<li clas=''>"+noteWriter+"&nbsp&nbsp:<a href='javascript:void(0)' class=\"ui-icon ui-icon-trash\" onclick=\"deleteNote('"+oneViewId+"','"+oneviewlet.getNoOfViewLets()+"','"+noteWriter+"','true','null')\"  style='float:right;'  title=\"Delete\"></a>");
//                    notesData.append("<ul class=''>");
                    for (int i = (note.size() - 1); i >= 0; i--) {
                        notesData.append("<li class=''>" + note.get(i).toString() + "<span style='font-family: verdana; font-size: 10px; font-style: italic;'>&nbsp&nbsp(" + noteWriter + ")</span> <a href='javascript:void(0)' class=\"ui-icon ui-icon-trash\" onclick=\"deleteNote('" + oneViewId + "','" + oneviewlet.getNoOfViewLets() + "','" + noteWriter + "','false','" + i + "')\"  style='float:right;'  title=\"Delete\"></a></li>");  //<a href='javascript:void(0)' class=\"ui-icon ui-icon-trash\" onclick=\"deleteNote('"+oneViewId+"','"+oneviewlet.getNoOfViewLets()+"','"+noteWriter+"','false','"+i+"')\"  style='float:right;'  title=\"Delete\"></a>
                    }
//                    notesData.append("</ul>");
//                    notesData.append("</li>");
//                    notesData.append("</ul>");

                }
//                notesData.append("</td>");
//                notesData.append("</tr>");
//                notesData.append("</table>");
                notesData.append("</div>");
                notesData.append("</div>");
                notesData.append("</div>");
            }
        } else {
            oneviewlet.noteMap = new HashMap<String, ArrayList>();
            notesData.append("<div id='note-" + oneviewlet.getNoOfViewLets() + "' style='color:#d0d0d0;font-size:25pt;position :absolute;font-family:verdana;width:" + oneviewlet.getWidth() + "px; height:" + oneviewlet.getHeight() + "px;'><p align='center'>Notes</p></div>");
        }
        return notesData.toString();
    }

    public String getOneViewTemplateDesign(OnceViewContainer oneContainer, String oneViewId, OneViewLetDetails oneviewlet) {
        StringBuffer resultString = new StringBuffer();
        //resultString.append("<td id='" + oneviewlet.getNoOfViewLets() + "' width='" + oneviewlet.getWidth() + "px'  style='height:" + oneviewlet.getHeight() + "px;' rowspan='" + oneviewlet.getRowSpan() + "' colspan='" + oneviewlet.getColSpan() + "'>");
        resultString.append("<table style='margin-left: 10px;width:100%'>"); //kruthika
        resultString.append("<tr >");
        resultString.append("<td id=\"Dashlets" + oneviewlet.getNoOfViewLets() + "\" style='font-size:12pt;color:#000000;white-space:nowrap;' title=\"\" ></td>");//"+oneviewlet.getRepName()+"
        resultString.append("<td id=\"refreshTabId" + oneviewlet.getNoOfViewLets() + "\" style='width:1%;align:right;'><a style=\"text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;font-size: 9pt;\" class=\"ui-icon ui-icon-arrowrefresh-1-w\" title=\"Refresh Region\" onclick=\"refreshOneVIewReg(" + oneviewlet.getNoOfViewLets() + ")\" href=\"#\"></a></td>");
        if (oneviewlet.getUserStatus()) {
            resultString.append("<td id=\"saveTabId" + oneviewlet.getNoOfViewLets() + "\" style='width:1%;align:right;'><a style=\"text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;font-size: 9pt;\" class=\"ui-icon ui-icon-disk\" title=\"Save\" onclick=\"saveEachOneVIewReg(" + oneviewlet.getNoOfViewLets() + ")\" href=\"#\"></a></td>");
        }
        if (oneviewlet.getUserStatus()) {
            resultString.append("<td id=\"optionIds" + oneviewlet.getNoOfViewLets() + "\" style='width:1%;align:right;'>");
            resultString.append("<a href='javascript:void(0)' class=\"ui-icon ui-icon-plusthick\" onclick=\"selectReadd(" + oneviewlet.getNoOfViewLets() + ")\"  style='text-decoration:none'  title=\"Re Add Onevielet\"></a>");

            resultString.append("<div id=\"readdDivId" + oneviewlet.getNoOfViewLets() + "\" style='display:none; width:90px; height:100px; background-color:#ffffff; overflow:auto;  position:absolute; text-align:left; border:1px solid #000000; border-top-width: 0px; z-index:1002;'>");
            resultString.append("<table border='0' align='left' >");
            resultString.append("<tr><td>");

            resultString.append("<table><tr><td><a href='javascript:void(0)' style='text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;' onclick=\"selectedType('report','Dashlets-" + oneviewlet.getNoOfViewLets() + "'," + oneviewlet.getNoOfViewLets() + "," + oneviewlet.getWidth() + "," + oneviewlet.getHeight() + ",'Dashlets" + oneviewlet.getNoOfViewLets() + "','GrpTyp" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "')\" >Reports</a></td></tr></table>");
            resultString.append("<table><tr><td><a href='javascript:void(0)' style='text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;' onclick=\"selectedType('measures','Dashlets-" + oneviewlet.getNoOfViewLets() + "'," + oneviewlet.getNoOfViewLets() + "," + oneviewlet.getWidth() + "," + oneviewlet.getHeight() + ",'Dashlets" + oneviewlet.getNoOfViewLets() + "','GrpTyp" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "')\" >Measures</a></td></tr></table>");

            resultString.append("<table><tr><td><a href='javascript:void(0)' style='text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;' onclick=\"selectedType('dashboard','Dashlets-" + oneviewlet.getNoOfViewLets() + "'," + oneviewlet.getNoOfViewLets() + "," + oneviewlet.getWidth() + "," + oneviewlet.getHeight() + ",'Dashlets" + oneviewlet.getNoOfViewLets() + "','GrpTyp" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "')\" >KPIs</a></td></tr></table>");
            resultString.append("<table><tr><td><a href='javascript:void(0)' style='text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;' onclick=\"selectedType('headline','Dashlets-" + oneviewlet.getNoOfViewLets() + "'," + oneviewlet.getNoOfViewLets() + "," + oneviewlet.getWidth() + "," + oneviewlet.getHeight() + ",'Dashlets" + oneviewlet.getNoOfViewLets() + "','GrpTyp" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "')\" >Headlines</a></td></tr></table>");
            resultString.append("<table><tr><td><a href='javascript:void(0)' style='text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;white-space:nowrap;' onclick=\"selectedType('complexkpi','Dashlets-" + oneviewlet.getNoOfViewLets() + "'," + oneviewlet.getNoOfViewLets() + "," + oneviewlet.getWidth() + "," + oneviewlet.getHeight() + ",'Dashlets" + oneviewlet.getNoOfViewLets() + "','GrpTyp" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "')\" >Custom KPI</a></td></tr></table>");
            resultString.append("<table><tr><td><a href='javascript:void(0)' style='text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;white-space:nowrap;' onclick=\"selectedType('Date','Dashlets-" + oneviewlet.getNoOfViewLets() + "'," + oneviewlet.getNoOfViewLets() + "," + oneviewlet.getWidth() + "," + oneviewlet.getHeight() + ",'Dashlets" + oneviewlet.getNoOfViewLets() + "','GrpTyp" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "')\" >Date</a></td></tr></table>");
            resultString.append("<table><tr><td><a href='javascript:void(0)' style='text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;white-space:nowrap;' onclick=\"selectedType('notes','Dashlets-" + oneviewlet.getNoOfViewLets() + "'," + oneviewlet.getNoOfViewLets() + "," + oneviewlet.getWidth() + "," + oneviewlet.getHeight() + ",'Dashlets" + oneviewlet.getNoOfViewLets() + "','GrpTyp" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "')\" >Notes</a></td></tr></table>");

            resultString.append("</td></tr>");
            resultString.append("</table>");
            resultString.append("</div>");
            resultString.append("</td>");
        }
        resultString.append("</tr>");
        resultString.append("</table>");
        resultString.append("<div id='Dashlets-" + oneviewlet.getNoOfViewLets() + "' class='ui-tabs ui-widget ui-widget-content ui-corner-all' style='border-bottom: medium hidden LightGrey ; border-left: medium hidden LightGrey ; border-right: medium hidden LightGrey ; border-color: LightGrey ; width: 100%; height: 100%; margin-left: 10px; margin-right: 10px;'>");
        resultString.append("<div id='template-" + oneviewlet.getNoOfViewLets() + "' style='color:#d0d0d0;font-size:25pt;position :absolute;font-family:verdana;width:" + oneviewlet.getWidth() + "px; height:" + oneviewlet.getHeight() + "px;'><p align='center'>" + oneviewlet.getRepName() + "</p></div>");
        resultString.append("</div>");
        resultString.append("</div>");
        return resultString.toString();
    }

    /**
     * Insert the region file names in Database and Setting the list Of files to
     * OneViewContainer Added by Anil
     */
    public String insertOneviewRegData(OnceViewContainer oneContainer, String oneViewId, HttpServletRequest request) throws FileNotFoundException, IOException {
        ArrayList al = new ArrayList();
        HashMap regMap = new HashMap();
        int innerViewLetId = 0;
        List<OneViewLetDetails> dashletDetails = oneContainer.onviewLetdetails;
        String innerRegFileName = "";
        String finalqry = getResourceBundle().getString("insertOneviewRegiondata");
        PbDb pbDb = new PbDb();
        Object[] obj = new Object[3];
        String finalQuery = null;
        for (int i = 0; i < oneContainer.onviewLetdetails.size(); i++) {
            innerRegFileName = "InnerRegionDetails" + oneViewId + "_" + innerViewLetId + "_" + System.currentTimeMillis() + ".txt";
            obj[0] = oneViewId;
            obj[1] = innerViewLetId;
            obj[2] = innerRegFileName;
            regMap.put(innerViewLetId, innerRegFileName);
            oneContainer.SetRegHashMap(regMap);
            innerViewLetId++;
            finalQuery = pbDb.buildQuery(finalqry, obj);
            al.add(finalQuery);
        }
        try {
            pbDb.executeMultiple(al);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        String advHtmlFileProps = (String) request.getSession(false).getAttribute("oldAdvHtmlFileProps");
        ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();
        String fileName = reportTemplateDAO.getOneviewFileName(oneViewId);
        FileOutputStream fos1 = new FileOutputStream(advHtmlFileProps + "/" + fileName);
        ObjectOutputStream oos1 = new ObjectOutputStream(fos1);
        oos1.writeObject(oneContainer);
        oos1.flush();
        oos1.close();
        return null;
    }

    /**
     * Writing the region Data into Temporary Region Files Added by Anil
     */
    public void writeOneviewRegData(OnceViewContainer oneContainer, String result, String regId, HttpServletRequest request) throws FileNotFoundException, IOException {
        String advHtmlFileProps = (String) request.getSession(false).getAttribute("advHtmlFileProps");
        HashMap tempRegHashMap = oneContainer.getTempRegHashMap();
        String tempRegFileName = (String) tempRegHashMap.get(Integer.parseInt(regId));
        if (tempRegFileName != null) {
            FileOutputStream fos1 = new FileOutputStream(advHtmlFileProps + "/" + tempRegFileName);
            ObjectOutputStream oos1 = new ObjectOutputStream(fos1);
            oos1.writeObject(result);
            oos1.flush();
            oos1.close();
        }
    }

    /**
     * Writing and Saving the region Data into Region Files permanently Added by
     * Anil
     */
    public String saveOneviewRegData(OnceViewContainer oneContainer, String result, String regId, HttpServletRequest request) throws FileNotFoundException, IOException {
        String oldAdvHtmlFileProps = (String) request.getSession(false).getAttribute("oldAdvHtmlFileProps");
        HashMap regHashMap = oneContainer.getRegHashMap();
        String regFileName = (String) regHashMap.get(Integer.parseInt(regId));
        if (regFileName == null && result != null) {
            regFileName = insertSingleOneViewRegData(regHashMap, oneContainer.oneviewId, regId);
            result = buildRegionData(request, response, oneContainer, oneContainer.onviewLetdetails.get(Integer.parseInt(regId)));
            writeOneviewRegData(oneContainer, result, regId, request); /*
             * local saving
             */
        }
        if (regFileName != null) {
            FileOutputStream fos1 = new FileOutputStream(oldAdvHtmlFileProps + "/" + regFileName);
            ObjectOutputStream oos1 = new ObjectOutputStream(fos1);
            oos1.writeObject(result);
            oos1.flush();
            oos1.close();
        }
        return result;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    public String getGlobalPath() {
        return globalPath;
    }

    public void setGlobalPath(String globalPath) {
        this.globalPath = globalPath;
    }

    public String buildOneViewPdf(String oneViewId) {
        PbOneViewPdfDriver pdfDriver = new PbOneViewPdfDriver();
        OnceViewContainer onecontainer = null;
        OneViewLetDetails oneViewLet = null;
        String tempPdfFile = "";
        String pageType = request.getParameter("pageType");
        String fitTo = request.getParameter("fitTo");
        try {
            FileInputStream fis2 = new FileInputStream(globalPath + "/" + fileName);
            ObjectInputStream ois2 = new ObjectInputStream(fis2);
            onecontainer = (OnceViewContainer) ois2.readObject();
            ois2.close();
            List<OneViewLetDetails> viewLetDetails = onecontainer.onviewLetdetails;
            pdfDriver.setRequest(request);
            pdfDriver.setResponse(response);
            pdfDriver.setFileName("OneView");
            ArrayListMultimap rowMap = ArrayListMultimap.create();

            for (OneViewLetDetails viewlet : viewLetDetails) {
                rowMap.put(viewlet.getRow(), viewlet.getCol());
            }
            Set rowset = rowMap.keySet();
            SortedSet maxRowSet = new TreeSet();
            Iterator rowIter = rowset.iterator();
            HashMap<Object, HashMap> matchedMap = new HashMap<Object, HashMap>();

            List rowList = null;
            int temp = 0;
            int detailCount = 0;
            ArrayList<Integer> selectedList = new ArrayList<Integer>();

            //belowe loop sinks the selected regions with dashletIds
            while (rowIter.hasNext()) {
                temp = Integer.parseInt(rowIter.next().toString());
                selectedList.add(temp);
                maxRowSet.add(temp);
                rowList = rowMap.get(temp);
                HashMap intMatchMap = new HashMap();
                if (rowList != null) {
                    for (int i = 0; i < rowList.size(); i++) {
                        intMatchMap.put(rowList.get(i), detailCount);
                        detailCount++;
                    }
                    matchedMap.put(temp, intMatchMap);
                }
            }
            int assignedRows = (Integer) maxRowSet.last();
            LinkedList initialEachRowCols = new LinkedList(); //this list contains number of columns in each row
//            Set tablecolsSet=new LinkedHashSet();
            LinkedList tableList = new LinkedList();

            /*
             * belowe loop is to detect the total number of tables to be build
             * and their rows,cols respectevely
             */
            int estemRows = 0;
            int carriedCols = 0; /*
             * it contains the number of columns forwarded to next row
             */
            int totalHeight = 0;
            LinkedList nextColumnList = new LinkedList();
            for (int i = 0; i <= assignedRows; i++) {
                int colsInRow = 0;
                rowList = rowMap.get(i);
                if (rowList != null) {
                    for (int j = 0; j < rowList.size(); j++) {
                        int index = (Integer) rowList.get(j);
                        oneViewLet = viewLetDetails.get((Integer) matchedMap.get(i).get(rowList.get(j)));
                        if (oneViewLet != null) {
                            if (j == 0 && index == 0) { /*
                                 * This condition is to get the total Height of oneView
                                 */
                                totalHeight += oneViewLet.getHeight();
                            }
                            if (oneViewLet.getRowSpan() > 1) {    /*
                                 * If there is rowspan it increases the next
                                 * rows column count
                                 */
                                estemRows += oneViewLet.getRowSpan();
                                int spans = oneViewLet.getRowSpan() - 1;
                                int colSpans = oneViewLet.getColSpan() - 1;
                                for (int k = 0; k < spans; k++) {
                                    if (!nextColumnList.isEmpty() && k < nextColumnList.size()) {
                                        Integer prev = (Integer) nextColumnList.get(k);
                                        nextColumnList.remove(k);
                                        nextColumnList.add(k, (prev + 1) + (colSpans));
                                    } else {
                                        nextColumnList.add(1 + (colSpans));  /*
                                         * it contains the next row columns
                                         * carried i.e [4,3] means next row
                                         * included with 4 and the next->next
                                         * with 3
                                         */
                                    }
                                }
                            } else {
                                estemRows++;
                            }
//                            }
                            if (oneViewLet.getColSpan() > 1) {
                                colsInRow += oneViewLet.getColSpan();

                            } else {
                                colsInRow++;
                            }
                        }
                    }
                    if (rowList.size() == 0) {
                        if (carriedCols != 0) {
                            initialEachRowCols.add(carriedCols);
                            if (!tableList.isEmpty()) {
                                int last = (Integer) tableList.getLast();
                                if (colsInRow != last && colsInRow != 0) {
                                    tableList.add(colsInRow);
                                }
                            }
                        }
                        if (nextColumnList.size() > 0) {
                            carriedCols = (Integer) nextColumnList.get(0);
                            nextColumnList.remove(0);
                        } else {
                            carriedCols = 0;
                        }

                    } else {
                        if (!nextColumnList.isEmpty()) {
                            colsInRow = colsInRow + carriedCols;
                            carriedCols = (Integer) nextColumnList.get(0);
                            nextColumnList.remove(0);
                        } else {
                            colsInRow = colsInRow + carriedCols;
                            carriedCols = 0;
                        }

                        initialEachRowCols.add(colsInRow); /*
                         * initialEachRowCols List Total number columns in each row
                         */
                        if (colsInRow != 0) {
//                        tablecolsSet.add(colsInRow);
                            if (!tableList.isEmpty()) {
                                int last = (Integer) tableList.getLast();
                                if (colsInRow != last) {
                                    tableList.add(colsInRow);
                                }
                            } else {
                                tableList.add(colsInRow); /*
                                 * tableList contains the each element is table
                                 * with respective number of columns in the
                                 * table List i.e consequitive same numbers
                                 * grouped into one table
                                 */
                            }

                        }

                    }
                }

            }
            /*
             * This loop is to maintain the backup the table columns of
             * tableList
             */
            LinkedList tempTableList = new LinkedList();
            for (int i = 0; i < tableList.size(); i++) {
                tempTableList.add(tableList.get(i));
            }




            /*
             * belowe code is to detect which dashlet belongs to which table
             */

            HashMap detail2Table = new HashMap(); /*
             * it gives the table of the viewlet to be place
             */
            int cols = 0;
            rowIter = rowset.iterator();
            int key = 0;
            int count = 0;
            ArrayList rowListFromRowSet = new ArrayList();
            while (rowIter.hasNext()) {
                rowListFromRowSet.add(rowIter.next());
            }
            for (int i = 0; i < rowListFromRowSet.size(); i++) {
                key = (Integer) rowListFromRowSet.get(i);
                rowList = rowMap.get(key);   /*
                 * order of each key in rowMap maps to the number of columns
                 * mapped by key in initialEachRowCols List elements
                 */
                if (rowList != null) {
                    cols = (Integer) initialEachRowCols.get(key);

                    for (int j = 0; j < tableList.size(); j++) {
                        if (cols == (Integer) tableList.get(j) && cols != 0) {
                            for (int k = 0; k < rowList.size(); k++) {
                                detail2Table.put(matchedMap.get(key).get(rowList.get(k)), j); /*
                                 * map contains the detail and its respective
                                 * table to place
                                 */
                            }
                            break;
                        }
                    }
                    if ((key + 1) < initialEachRowCols.size() && (Integer) initialEachRowCols.get(key) != (Integer) initialEachRowCols.get((Integer) rowListFromRowSet.get(i + 1))) { /*
                         * cindiotion is to detect malitiple add regions with
                         * diff col sizes like adding 4,3,4 cols for each region addition
                         */
                        tableList.add(tableList.indexOf(cols), 0);
                        tableList.remove(tableList.indexOf(cols));
                    }
                    // count++;

                }

            }




            /*
             * Below code is to set from and to date to pdf
             */
            List timeDetails = onecontainer.timedetails;
            DashboardTemplateDAO dao = new DashboardTemplateDAO();
            String folderId = "";
            String elemtId = null;
            String connectionId = null;
            for (OneViewLetDetails viewlet : viewLetDetails) {
                if (viewlet.getReptype().equalsIgnoreCase("measures")) {
                    elemtId = viewlet.getRepId();
                    break;
                } else if (viewlet.getRoleId() != null && viewlet.getRoleId().length() > 1) {
                    folderId = viewlet.getRoleId();
                    break;
                }
            }
            if (folderId != null && !folderId.isEmpty()) {
//            
                String connQuery = "SELECT DISTINCT CONNECTION_ID FROM PRG_USER_ALL_INFO_DETAILS WHERE FOLDER_ID=" + folderId;
                PbReturnObject returnObject = dao.execSelectSQL(connQuery);

                if (returnObject != null && returnObject.rowCount > 0) {
                    connectionId = returnObject.getFieldValueString(0, 0);
                }
            }

            PbTimeRanges timeRanges = new PbTimeRanges();

            String priodType = timeDetails.get(3).toString();
            String compareWith = timeDetails.get(4).toString();
            String date = timeDetails.get(2).toString();
            if (elemtId != null) {
                timeRanges.elementID = elemtId;
            } else {
                timeRanges.connId = connectionId;
            }

            timeRanges.setRange(priodType, compareWith, date);
            pdfDriver.fromDate = timeRanges.st_d.split(" ")[0];
            pdfDriver.toDate = timeRanges.ed_d.split(" ")[0];
            pdfDriver.oneViewWidth = (float) onecontainer.width;

            pdfDriver.oneViewHeight = totalHeight;
            pdfDriver.setFitTo(fitTo);
            pdfDriver.setPageType(pageType);
            float pageHeight = pdfDriver.getPaperHeight();
            String title = onecontainer.oneviewName;
            pdfDriver.setTitle(title);
            pdfDriver.setTimeDetailsArray(onecontainer.timedetails);

            String headerTitle = null;
            PbDb pbdb = new PbDb();
            String headerQry = "select SETUP_CHAR_VALUE from PRG_GBL_SETUP_VALUES where SETUP_KEY='COMPANY_NAME'";
            PbReturnObject headerObj = pbdb.execSelectSQL(headerQry);
            int rowCount = headerObj.getRowCount();
            if (rowCount != 0) {
                headerTitle = headerObj.getFieldValueString(0, 0);
            } else {
                headerTitle = "ProGen Business Solutions";
            }
            /*
             * Belowe code is to get the logo
             */
            String logoPath = "/images/prgLogo.gif";

            pdfDriver.logoPath = logoPath;
            pdfDriver.logoWidth = 50;
            pdfDriver.logoHeight = 40;
            pdfDriver.setHeaderTitle(headerTitle);
            pdfDriver.openOneViewPdf();
            pdfDriver.createDocuementHeader1();

            //(float)onecontainer.heigth;


//            Iterator tableIter=tablecolsSet.iterator();
            for (int i = 0; i < tempTableList.size(); i++) {
                pdfDriver.setPaperColumns((Integer) tempTableList.get(i));
            }

            for (OneViewLetDetails viewlet : viewLetDetails) {
                pdfDriver.viewletName = viewlet.getRepName();
                pdfDriver.viewletHeight = viewlet.getHeight();
                pdfDriver.viewletWidth = viewlet.getWidth();
                pdfDriver.colSpan = viewlet.getColSpan();
                pdfDriver.rowSpan = viewlet.getRowSpan();
                pdfDriver.viewletNum = viewlet.getNoOfViewLets();

                int i = (Integer) detail2Table.get(Integer.parseInt(viewlet.getNoOfViewLets()));
                if (viewlet.getReptype().equalsIgnoreCase("repGraph")) {
                    pdfDriver.createOneVIewGraph1(i);
                }
                if (viewlet.getReptype().equalsIgnoreCase("template")) {
                    pdfDriver.viewletName = "";
                    pdfDriver.createTemplate(i);
                }
                if (viewlet.getReptype().equalsIgnoreCase("measures")) {
                    if (viewlet.isTrendGraph()) {
                        pdfDriver.setTrendDays(viewlet.getTrendDays());
                        pdfDriver.createOneVIewTrendGraph1(i);
                    } else {
                        createOneViewMeasure(pdfDriver, viewlet, i, onecontainer);
                    }
                }
                if (viewlet.getReptype().equalsIgnoreCase("repTable")) {
                    setReportTableDataForPdf(viewlet, onecontainer);
                }
                if (viewlet.getReptype().equalsIgnoreCase("complexkpi")) {
                    setcomplexKpiDataForPdf(pdfDriver, viewlet, onecontainer);
                    pdfDriver.createMeasureType1(i);
                }
                if (viewlet.getReptype().equalsIgnoreCase("notes")) {
                    pdfDriver.setNoteMap(viewlet.noteMap);
                    pdfDriver.createNotes(i);
                }

            }
            tempPdfFile = pdfDriver.getCreatedOneVIewPdf();
        } catch (Exception e) {
            logger.error("Exception:", e);
        }
        return tempPdfFile;
    }

    public void createOneViewMeasure(PbOneViewPdfDriver pdfDriver, OneViewLetDetails viewlet, int pdftableToAdd, OnceViewContainer oneContainer) {
        String displayType = viewlet.getDisplayType();
        String measureType = viewlet.getMeasType();

        String clrapp = viewlet.getMeasureColor();
        DashboardTemplateDAO templateDao = new DashboardTemplateDAO();
        HashMap clrCode = new HashMap();
        if (clrapp != null && clrapp.length() > 1) {
            clrCode.put("getColor", clrapp);
            clrCode = templateDao.getColorCodesWithRGB(clrCode);
            Set clrSet = clrCode.keySet();
            Iterator iter = clrSet.iterator();
            String rgb = "";
            if (iter.hasNext()) {
                rgb = iter.next().toString();
                String temp = rgb.substring(rgb.indexOf('(') + 1, rgb.indexOf(')'));
                String[] clrs = temp.split(",");
                pdfDriver.setMeasFontColor(new BaseColor(Integer.parseInt(clrs[0].trim()), Integer.parseInt(clrs[1].trim()), Integer.parseInt(clrs[2].trim())));
            }

        } else {
            pdfDriver.setMeasFontColor(new BaseColor(0, 149, 182)); //new BaseColor(100, 178, 255)
        }


        if (displayType == null) {
            displayType = "";
        }

        if (displayType != null && (displayType.equalsIgnoreCase("") || displayType.equalsIgnoreCase("fifth"))) {
            buildResultSetForMeasure(viewlet, oneContainer, pdfDriver);
            pdfDriver.createMeasureType1(pdftableToAdd);
        }
        if (displayType != null && displayType.equalsIgnoreCase("first")) {
            buildResultSetForMeasure(viewlet, oneContainer, pdfDriver);
            pdfDriver.createMeasureType2(pdftableToAdd);

        }
        if (displayType != null && displayType.equalsIgnoreCase("second")) {
            buildResultSetForMeasure(viewlet, oneContainer, pdfDriver);
            pdfDriver.createMeasureType3(pdftableToAdd);

        }
        if (displayType != null && displayType.equalsIgnoreCase("third")) {
            pdfDriver.setRadius1(10);
            pdfDriver.setRadius2(8);
            buildResultSetForMeasure(viewlet, oneContainer, pdfDriver);
            pdfDriver.createMeasureType4(pdftableToAdd);
        }
        if (displayType != null && displayType.equalsIgnoreCase("fourth")) {
            buildResultSetForMeasure(viewlet, oneContainer, pdfDriver);
            pdfDriver.createMeasureType5(pdftableToAdd);
        }

    }

    public void buildResultSetForMeasure(OneViewLetDetails viewlet, OnceViewContainer oneContainer, PbOneViewPdfDriver pdfDriver) {
        ArrayList measureList = new ArrayList();
        measureList.add(viewlet.getRepId());
        ArrayList QueryCols = new ArrayList();
        ArrayList QueryAggs = new ArrayList();
        double currVal = 0.0;
        double priorVal = 0.0;
        double changePer = 0.0;
        List<String> timeDetails = oneContainer.timedetails;
        String measureType = viewlet.getMeasType();
        DashboardViewerDAO dao = new DashboardViewerDAO();
        PbReturnObject pbretObjTime = new PbReturnObject();
        List<KPIElement> kpiElements = dao.getKPIElements(measureList, new HashMap<String, String>());
        if (kpiElements != null) {
            for (KPIElement elem : kpiElements) {
                if (elem.getElementName() != null) {
                    QueryCols.add(elem.getElementId());
                }
                QueryAggs.add(elem.getAggregationType());
            }
        }
        if (viewlet.getPbReturnObject() != null) {
            pbretObjTime = viewlet.getPbReturnObject();
        } else {
            if (String.valueOf(QueryAggs.get(0)) != null) {
                request.setAttribute("OneviewTiemDetails", oneContainer.timedetails);
                pbretObjTime = dao.getReturnObjectForOneView(QueryCols, QueryAggs, userId, viewlet.getRoleId(), request, oneContainer, viewlet);
            }
        }


        if (!pbretObjTime.getFieldValueString(0, ("A_" + QueryCols.get(0)).toString()).equalsIgnoreCase("")) {
            currVal = Double.parseDouble((pbretObjTime.getFieldValueString(0, ("A_" + QueryCols.get(0)).toString())));
        }
        if (QueryCols.size() == 4) {
            if (pbretObjTime.getFieldValueString(0, ("A_" + QueryCols.get(1)).toString()) != null && !pbretObjTime.getFieldValueString(0, ("A_" + QueryCols.get(1)).toString()).equalsIgnoreCase("")) {
                priorVal = Double.parseDouble((pbretObjTime.getFieldValueString(0, ("A_" + QueryCols.get(1)).toString())));
            }
            if (pbretObjTime.getFieldValueString(0, ("A_" + QueryCols.get(3)).toString()) != null && !pbretObjTime.getFieldValueString(0, ("A_" + QueryCols.get(3)).toString()).equalsIgnoreCase("")) {
                changePer = Double.parseDouble((pbretObjTime.getFieldValueString(0, ("A_" + QueryCols.get(3)).toString())));
            }
        }

        int decimalPlaces = 1;
        BigDecimal curval = new BigDecimal(currVal);
        BigDecimal prior = new BigDecimal(priorVal);
        BigDecimal chper = new BigDecimal(changePer);
        curval = curval.setScale(decimalPlaces, BigDecimal.ROUND_HALF_UP);
        prior = prior.setScale(decimalPlaces, BigDecimal.ROUND_HALF_UP);
        chper = chper.setScale(decimalPlaces, BigDecimal.ROUND_HALF_UP);
        char[] formatType;
        if (viewlet.getFormatVal() != null && !viewlet.getFormatVal().equalsIgnoreCase("")) {
            formatType = viewlet.getFormatVal().toCharArray();
        } else {
            formatType = new char[1];
            formatType[0] = ' ';
        }

        String formatVal = "";
        switch (formatType[0]) {
            case 'K':
                formatVal = "K";
                break;
            case 'L':
                formatVal = "Lkh";
                break;
            case 'M':
                formatVal = "Mn";
                break;
            case ' ':
                formatVal = " ";
                break;
            default:
                formatVal = "Crs";
        }
        String suffixValue = "";
        if (viewlet.getSuffixValue() != null) {
            suffixValue = viewlet.getSuffixValue();
        }
        if (viewlet.getSuffixValue() != null) {
            if (viewlet.getSuffixValue().equalsIgnoreCase("K")) {
                suffixValue = "K";
            } else if (viewlet.getSuffixValue().equalsIgnoreCase("L")) {
                suffixValue = "Lkh";
            } else if (viewlet.getSuffixValue().equalsIgnoreCase("M")) {
                suffixValue = "Mn";
            } else if (viewlet.getSuffixValue().equalsIgnoreCase("Cr")) {
                suffixValue = "Crs";
            }
        }
        pdfDriver.formatVal = suffixValue;

        if (viewlet.getPrefixValue() != null) {
            pdfDriver.prefixVal = viewlet.getPrefixValue();
        }

        String currValStr = viewlet.getCurrValue();
        String priorValStr = viewlet.getPriorValue();
        String changPer = "";
        if (formatType != null && formatVal != null && viewlet.getFormatVal() != null && viewlet.getRoundVal() != null && !viewlet.getRoundVal().equalsIgnoreCase("")) {
            currValStr = NumberFormatter.getModifiedNumber(curval, viewlet.getFormatVal(), Integer.parseInt(viewlet.getRoundVal()));
            if (QueryCols.size() == 4) {
                if (pbretObjTime.getFieldValueString(0, ("A_" + QueryCols.get(1)).toString()) != null && !pbretObjTime.getFieldValueString(0, ("A_" + QueryCols.get(1)).toString()).equalsIgnoreCase("")) {
                    priorValStr = pbretObjTime.getFieldValueString(0, ("A_" + QueryCols.get(1)).toString());
                    if (!priorValStr.equalsIgnoreCase("") && !priorValStr.equalsIgnoreCase("undefined") && priorValStr != null) {
                        BigDecimal priorVal1 = new BigDecimal(Double.parseDouble(priorValStr.replace(",", "")));
                        priorValStr = NumberFormatter.getModifiedNumber(priorVal1, viewlet.getFormatVal(), Integer.parseInt(viewlet.getRoundVal()));

                    }
                }
            }
        }
        pdfDriver.measCurrVal = currValStr;
        pdfDriver.measPriorVal = priorValStr;

        pdfDriver.measureFmtType = viewlet.getFormatVal();
        pdfDriver.formatVal = formatVal;








        pdfDriver.setCalCurrVal(curval);
        pdfDriver.setCalPriorVal(prior);
        pdfDriver.setCalChPer(chper);
        String currWidth = "";
        String priorwidth = "";
        int maxWidth = 40;
        String value = Integer.toString((int) currVal);
        String displayType = viewlet.getDisplayType();

        if (displayType != null && displayType.equalsIgnoreCase("first")) {
            if (currVal > priorVal) {
                currWidth = Integer.toString(maxWidth);
                if (!value.substring(0, 1).equalsIgnoreCase("-")) {
                    priorwidth = Integer.toString((int) (priorVal / currVal * maxWidth));
                } else {
                    priorwidth = Integer.toString((int) (currVal / priorVal * maxWidth)).replace("-", "");
                }
            } else if (currVal == 0.0 && priorVal == 0.0) {
                priorwidth = "0";
                currWidth = "0";
            } else if (currVal == priorVal) {
                priorwidth = Integer.toString(maxWidth);
                currWidth = Integer.toString(maxWidth);
            } else if (currVal < 0.0 && priorVal == 0.0) {
                priorwidth = Integer.toString(maxWidth);
                if (!value.substring(0, 1).equalsIgnoreCase("-")) {
                    currWidth = Integer.toString((int) (currVal / priorVal * maxWidth));
                } else {
                    currWidth = Integer.toString((int) (priorVal / currVal * maxWidth)).replace("-", "");
                }
            } else {
                priorwidth = Integer.toString(maxWidth);
                if (!value.substring(0, 1).equalsIgnoreCase("-")) {
                    currWidth = Integer.toString((int) (currVal / priorVal * maxWidth));
                } else {
                    currWidth = Integer.toString((int) (priorVal / currVal * maxWidth)).replace("-", "");
                }
            }
            pdfDriver.setCurrWidth(Float.parseFloat(currWidth));
            pdfDriver.setPriorWidth(Float.parseFloat(priorwidth));



            if (timeDetails != null && !timeDetails.isEmpty()) {
                if (timeDetails.get(3).equalsIgnoreCase("Day")) {
                    measureType = "DTD";
                } else if (timeDetails.get(3).equalsIgnoreCase("Week")) {
                    measureType = "WTD";
                } else if (timeDetails.get(3).equalsIgnoreCase("Month")) {
                    measureType = "MTD";
                } else if (timeDetails.get(3).equalsIgnoreCase("Quarter")) {
                    measureType = "QTD";
                } else if (timeDetails.get(3).equalsIgnoreCase("Year")) {
                    measureType = "YTD";
                }
            } else {
                measureType = "MTD";
            }
            pdfDriver.setMeasureType(measureType);
        }

        String filePath = "";
        if (displayType != null && displayType.equalsIgnoreCase("second")) {
            if (currVal > priorVal) {
                if (measureType != null && measureType.equalsIgnoreCase("Standard")) {
                    filePath = "/images/Green Arrow.jpg";
                    pdfDriver.setMeasFontColor(new BaseColor(0, 128, 0)); //green
                    if (value.substring(0, 1).equalsIgnoreCase("-")) {
                        filePath = "/images/Red Arrow.jpeg";
                        pdfDriver.setMeasFontColor(BaseColor.RED);
                    }
                } else {
                    filePath = "/images/Red Up Arrow.jpeg";
                    pdfDriver.setMeasFontColor(BaseColor.RED);
                    if (value.substring(0, 1).equalsIgnoreCase("-")) {
                        filePath = "/images/Green Arrow.jpg";
                        pdfDriver.setMeasFontColor(new BaseColor(0, 128, 0)); //green
                    }
                }
            } else {
                if (measureType != null && measureType.equalsIgnoreCase("Standard")) {
                    filePath = "/images/Red Arrow.jpeg";
                    pdfDriver.setMeasFontColor(BaseColor.RED);
                    if (value.substring(0, 1).equalsIgnoreCase("-")) {
                        filePath = "/images/Green Arrow.jpg";
                        pdfDriver.setMeasFontColor(new BaseColor(0, 128, 0)); //green
                    }
                } else {
                    filePath = "/images/Green Down Arrow.jpg";
                    pdfDriver.setMeasFontColor(new BaseColor(0, 128, 0)); //green
                    if (value.substring(0, 1).equalsIgnoreCase("-")) {
                        filePath = "/images/Red Arrow.jpeg";
                        pdfDriver.setMeasFontColor(BaseColor.RED);
                    }
                }
            }
            pdfDriver.setFilePath(filePath);
            pdfDriver.arrowHeight = 18;
            pdfDriver.arrowWidth = 20;
        }
    }

    public void setReportTableDataForPdf(OneViewLetDetails viewlet, OnceViewContainer oneContainer) throws Exception {
        String reportId = viewlet.getRepId();
        HashMap map = null;
        Container container = null;
        List timedetails = oneContainer.timedetails;
        HttpSession session = request.getSession();
        if (request.getAttribute("OneviewTableTimeDetails") != null) {
            request.removeAttribute("OneviewTableTimeDetails");
        }

        if (timedetails != null && !viewlet.isOneviewReportTimeDetails() && !timedetails.isEmpty()) {
            request.setAttribute("OneviewTableTimeDetails", timedetails);
        }
        if (request.getAttribute("OneviewGraphTimeDetails") != null) {
            request.removeAttribute("OneviewGraphTimeDetails");
        }


        if (request.getAttribute("OneviewtableDate") != null) {
            request.removeAttribute("OneviewtableDate");
        }

        if (timedetails != null && !timedetails.isEmpty() && viewlet.isOneviewReportTimeDetails()) {
            request.setAttribute("OneviewtableDate", timedetails.get(2));
        }
        if (request.getAttribute("OneviewgraphDate") != null) {
            request.removeAttribute("OneviewgraphDate");
        }
        String userId = String.valueOf(session.getAttribute("USERID"));
        PbReportViewerBD reportViewerBD = new PbReportViewerBD();
        String strURL = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
        strURL = strURL + "reportViewer.do?reportBy=viewReport&REPORTID=" + reportId + "&action=open";
        request.setAttribute("url", strURL);
        request.setAttribute("REPORTID", reportId);
        reportViewerBD.prepareReport(reportId, userId, request, response,false);
        map = (HashMap) session.getAttribute("PROGENTABLES");

        container = (Container) map.get(reportId);
        ArrayList cols = container.getDisplayColumns();
        ArrayList disCols = container.getDisplayLabels();
        ArrayList dTypes = container.getDataTypes();
        int fromRow = container.getFromRow();
        int toRow = container.getRetObj().getViewSequence().size();

        ProgenDataSet retObj = container.getRetObj();



    }

    public void setKpiDataForPdf(OneViewLetDetails viewlet, OnceViewContainer oneContainer, PbOneViewPdfDriver pdfDriver) {
        DashboardTemplateBD dashboardTemplateBD = new DashboardTemplateBD();
        PbDashboardViewerAction genDashForOneview = new PbDashboardViewerAction();
        HttpSession session = request.getSession(false);
        request.setAttribute("REPORTID", viewlet.getRepId());
        request.setAttribute("OneviewTest", true);
        request.setAttribute("dashletId", viewlet.getKpiDashLetNo());
        try {
            genDashForOneview.viewDashboard(mapping, form, request, response);
            HashMap map = null;
            Container container = null;
            String dashBoardId = viewlet.getRepId();
            String kpiMasterId = null;
            pbDashboardCollection collect = null;
            KPIBuilder kpibuilder = new KPIBuilder();
            String kpiType;
            if (session != null) {
                map = (HashMap) session.getAttribute("PROGENTABLES");
                if (map != null) {
                    container = (Container) map.get(dashBoardId);
                    collect = (pbDashboardCollection) container.getReportCollect();
                    DashletDetail detail = collect.getDashletDetail(viewlet.getNoOfViewLets());
                    kpiType = viewlet.getKpiType();
                    ArrayList<String> headsAray = new ArrayList<String>();
                    if (detail.getKpiheads().isEmpty() || detail.getKpiheads().get(0).equalsIgnoreCase("")) {
                        if (kpiType != null && kpiType.equalsIgnoreCase("Basic")) {
                            headsAray.add("KPI");
                            headsAray.add("Value");
                        } else if (kpiType != null && kpiType.equalsIgnoreCase("Standard")) {
                            headsAray.add("KPI");
                            headsAray.add("Current/Prior");
                            headsAray.add("Change%");
                        } else if (kpiType != null && kpiType.equalsIgnoreCase("MultiPeriod")) {
                            headsAray.add("KPI");
                            headsAray.add("Current Day");
                            headsAray.add("MTD");
                            headsAray.add("QTD");
                            headsAray.add("YTD");

                        } else if (kpiType != null && kpiType.equalsIgnoreCase("MultiPeriodCurrentPrior")) {
                            headsAray.add("KPI");
                            headsAray.add("MTD Current");
                            headsAray.add("Target");
                            headsAray.add("MTD Prior");
                            headsAray.add("Deviation");
                            headsAray.add("Deviation%");
                            headsAray.add("MTD Change");
                            headsAray.add("MTD Change%");
                            headsAray.add("QTD Current");
                            headsAray.add("Target");
                            headsAray.add("QTD Prior");
                            headsAray.add("Deviation");
                            headsAray.add("Deviation%");
                            headsAray.add("QTD Change");
                            headsAray.add("QTD Change%");
                            headsAray.add("YTD Current");
                            headsAray.add("Target");
                            headsAray.add("YTD Prior");
                            headsAray.add("Deviation");
                            headsAray.add("Deviation%");
                            headsAray.add("YTD Change");
                            headsAray.add("YTD Change%");

                        } else if (kpiType != null && kpiType.equalsIgnoreCase("Target")) {
                            headsAray.add("KPI");
                            headsAray.add("Current/Prior");
                            headsAray.add("Change%");
                            headsAray.add("Target Value");
                            headsAray.add("Deviation%");

                        } else {
                            headsAray.add("KPI");
                            headsAray.add("Value");
                            headsAray.add("Target");
                            headsAray.add("Deviation");
                            headsAray.add("Deviation%");
                        }
                    } else {
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Exception:", e);
        }

    }

    public String getOneViewVersion(String oneViewId) {
        String version = "1.0";
        String query = "SELECT ONE_VERSION FROM PRG_AR_ONEVIEW_FILE_DATA where ONEVIEWID='" + oneViewId + "'";
        DashboardViewerDAO dao = new DashboardViewerDAO();
        try {
            PbReturnObject retObj = dao.execSelectSQL(query);
            if (retObj != null && retObj.rowCount > 0) {
                version = retObj.getFieldValueString(0, 0);
            }
        } catch (Exception e) {
            logger.error("Exception:", e);
        }

        return version;
    }

    public String insertSingleOneViewRegData(HashMap regMap, String oneViewId, String viewltId) throws FileNotFoundException, IOException {
        String innerRegFileName = null;
        String finalqry = getResourceBundle().getString("insertOneviewRegiondata");
        PbDb pbDb = new PbDb();
        Object[] obj = new Object[3];
        String finalQuery = null;
        if (regMap != null) {
            innerRegFileName = "InnerRegionDetails" + oneViewId + "_" + viewltId + "_" + System.currentTimeMillis() + ".txt";
            obj[0] = oneViewId;
            obj[1] = viewltId;
            obj[2] = innerRegFileName;
            regMap.put(Integer.parseInt(viewltId), innerRegFileName);
            finalQuery = pbDb.buildQuery(finalqry, obj);
            try {
                pbDb.execUpdateSQL(finalQuery);
            } catch (Exception ex) {
                logger.error("Exception:", ex);
            }
        }
        return innerRegFileName;
    }

    public String buildRegionData(HttpServletRequest request, HttpServletResponse response, OnceViewContainer onecontainer, OneViewLetDetails detail) {
        String regionData = "";
        HttpSession session = request.getSession(false);
        String assignFlag = null;
        //boolean powerAnalyzer = (Boolean)session.getAttribute("isPowerAnalyserEnableforUser");
        try {
            //if(powerAnalyzer) added by Kruthika
            assignFlag = (String) request.getAttribute("assignFlag");
            String ifpoweranalyer = session.getAttribute("isPowerAnalyserEnableforUser").toString();
            if (ifpoweranalyer != null && ifpoweranalyer.equalsIgnoreCase("true")) {
                detail.setUserStatus(true);
            } else {
                detail.setUserStatus(false);
            }
            DashboardTemplateBD dashboardTemplateBD = new DashboardTemplateBD();
            if (detail.getReptype().toString().equalsIgnoreCase("measures")) {
                regionData = dashboardTemplateBD.getMeasureDetailsData(request, response, session, onecontainer, detail);
            }
            if (detail.getReptype().toString().equalsIgnoreCase("repKpis")) {
                PbDashboardViewerAction genDashForOneview = new PbDashboardViewerAction();
                request.setAttribute("REPORTID", detail.getRepId());
                request.setAttribute("OneviewTest", true);
                request.setAttribute("dashletId", detail.getKpiDashLetNo());
                if (detail.isOneviewReportTimeDetails()) {
                    request.setAttribute("dbrdkpiTimeDetails", (ArrayList) detail.getMsrCustomTimeDetails());
                } else {
                    request.setAttribute("dbrdkpiTimeDetails", (ArrayList) onecontainer.timedetails);
                }
                if (onecontainer.getFilterBusinessRole() != null && !onecontainer.getFilterBusinessRole().equalsIgnoreCase("") && detail != null && detail.getRoleId() != null && !detail.getRoleId().equalsIgnoreCase("") && onecontainer.getFilterBusinessRole().equalsIgnoreCase(detail.getRoleId())) {
                    if (onecontainer.getReportParameterValues() != null && !onecontainer.getReportParameterValues().isEmpty()) {
                        request.setAttribute("reportParameterVals", (LinkedHashMap) onecontainer.getReportParameterValues());
                    }
                }
                genDashForOneview.viewDashboard(mapping, form, request, response);
                regionData = dashboardTemplateBD.getOneviewKpisData(request, response, session, detail, onecontainer);
            }
            if (detail.getReptype().toString().equalsIgnoreCase("repTable")) {
                regionData = dashboardTemplateBD.getTableDetailsData(request, response, session, detail, onecontainer.timedetails, onecontainer);
            }
            if (detail.getReptype().toString().equalsIgnoreCase("repGraph")) {
                regionData = dashboardTemplateBD.getGraphDetailsData(request, response, session, detail, onecontainer.timedetails, onecontainer);
            }
            if (detail.getReptype().toString().equalsIgnoreCase("headLine")) {
                regionData = dashboardTemplateBD.getHeadLinehDetailsData(request, response, session, detail);
            }
            if (detail.getReptype().toString().equalsIgnoreCase("notes")) {
                regionData = getOneViewNotesData(onecontainer, onecontainer.oneviewId, detail, true);
            }
            if (detail.getReptype().toString().equalsIgnoreCase("complexkpi")) {
                regionData = dashboardTemplateBD.getComplexKpisData(request, response, session, detail, onecontainer.oneviewId, Integer.parseInt(detail.getNoOfViewLets()));
            }
            if (detail.getReptype().toString().equalsIgnoreCase("template")) {
                regionData = getOneViewTemplateDesign(onecontainer, onecontainer.oneviewId, detail);
            }
            if (detail.getReptype().toString().equalsIgnoreCase("portlet")) {
                regionData = dashboardTemplateBD.getPortalDetailsData(request, response, session, detail);
            }
            if (detail.getReptype().toString().equalsIgnoreCase("TemplateGraph") || detail.getReptype().toString().equalsIgnoreCase("MsrTemplateGraph")) {
                regionData = generatebusinessTemplateGraph(request, response, onecontainer, detail);
            }
            if (detail.getReptype().toString().equalsIgnoreCase("TemplateMeasure")) {
                regionData = generateTemplateMeasure(onecontainer, detail);
            }
            if (detail.getReptype().toString().equalsIgnoreCase("TemplateTable")) {
                regionData = generateMesureTableData(onecontainer.oneviewId, onecontainer, detail);
            }


        } catch (Exception e) {
            logger.error("Exception:", e);
        }
        return regionData;
    }
//Start of code by sandeep on 17/10/14 for schedule// update local files in oneview

    public String updateoneviewschedule(Container container, String reportId, String schedulerId, String userId, String schedulerName, OnceViewContainer onecontainer, OneViewLetDetails detail, ReportSchedule schedule) throws Exception {
        String regionData = "";
        DashboardTemplateBD dashboardTemplateBD = new DashboardTemplateBD();
        String reptype = detail.getReptype();
        if (reptype != null && reptype.equalsIgnoreCase("repGraph")) {
            regionData = dashboardTemplateBD.getGraphDetailsData1(container, detail, onecontainer.timedetails, onecontainer, schedule);
        } else if (reptype != null && reptype.equalsIgnoreCase("repTable")) {
            regionData = dashboardTemplateBD.getTableDetailsData1(container, detail, onecontainer.timedetails, onecontainer, schedule);
        } else {
            detail.setisschedule(true);
            regionData = dashboardTemplateBD.getMeasureDetailsData(null, null, null, onecontainer, detail);
            detail.setisschedule(false);
        }

        return regionData;
    }
    //End of code by sandeep on 16/10/14 for schedule// update local files in oneview

    public String getDimensions(String userId) {
        String query = "SELECT UA.USER_FOLDER_ID,UF.FOLDER_NAME  from PRG_GRP_USER_FOLDER_ASSIGNMENT UA,PRG_USER_FOLDER UF where UA.USER_FOLDER_ID=UF.FOLDER_ID and UA.USER_ID=" + userId;
        PbDb pbdb = new PbDb();
        HashMap dimMap = new HashMap();
        String jsonString = null;
        try {
            PbReturnObject roleIdResult = pbdb.execSelectSQL(query);
            if (roleIdResult != null) {
                for (int i = 0; i < roleIdResult.rowCount; i++) {
                    dimMap.put(roleIdResult.getFieldValueString(i, 0), roleIdResult.getFieldValueString(i, 1));
                }
                Gson gson = new Gson();
                jsonString = gson.toJson(dimMap);
            }

        } catch (Exception e) {
            logger.error("Exception:", e);
        }

        return jsonString;
    }

    public void setcomplexKpiDataForPdf(PbOneViewPdfDriver pdfDriver, OneViewLetDetails viewlet, OnceViewContainer oneContainer) {
        char[] formatType;
        if (viewlet.getFormatVal() != null && !viewlet.getFormatVal().equalsIgnoreCase("")) {
            formatType = viewlet.getFormatVal().toCharArray();
        } else {
            formatType = new char[1];
            formatType[0] = ' ';
        }

        String formatVal = "";
        switch (formatType[0]) {
            case 'K':
                formatVal = "K";
                break;
            case 'L':
                formatVal = "Lkh";
                break;
            case 'M':
                formatVal = "Mn";
                break;
            case ' ':
                formatVal = " ";
                break;
            default:
                formatVal = "Crs";
        }
        pdfDriver.formatVal = formatVal;

        String suffixValue = "";
        if (viewlet.getSuffixValue() != null) {
            suffixValue = viewlet.getSuffixValue();
        }
        if (viewlet.getSuffixValue() != null) {
            if (viewlet.getSuffixValue().equalsIgnoreCase("K")) {
                suffixValue = "K";
            } else if (viewlet.getSuffixValue().equalsIgnoreCase("L")) {
                suffixValue = "Lkh";
            } else if (viewlet.getSuffixValue().equalsIgnoreCase("M")) {
                suffixValue = "Mn";
            } else if (viewlet.getSuffixValue().equalsIgnoreCase("Cr")) {
                suffixValue = "Crs";
            }
        }
        pdfDriver.formatVal = suffixValue;
        if (viewlet.getPrefixValue() != null) {
            pdfDriver.prefixVal = viewlet.getPrefixValue();
        }
        String clrapp = viewlet.getMeasureColor();
        DashboardTemplateDAO templateDao = new DashboardTemplateDAO();
        HashMap clrCode = new HashMap();
        if (clrapp != null && clrapp.length() > 1) {
            clrCode.put("getColor", clrapp);
            clrCode = templateDao.getColorCodesWithRGB(clrCode);
            Set clrSet = clrCode.keySet();
            Iterator iter = clrSet.iterator();
            String rgb = "";
            if (iter.hasNext()) {
                rgb = iter.next().toString();
                String temp = rgb.substring(rgb.indexOf('(') + 1, rgb.indexOf(')'));
                String[] clrs = temp.split(",");
                pdfDriver.setMeasFontColor(new BaseColor(Integer.parseInt(clrs[0].trim()), Integer.parseInt(clrs[1].trim()), Integer.parseInt(clrs[2].trim())));
            }

        } else {
            pdfDriver.setMeasFontColor(new BaseColor(100, 178, 255));
        }
        DashboardTemplateBD dashboardTemplateBD = new DashboardTemplateBD();
        try {
            if (viewlet.getCustomKpiVal() != null) {

                pdfDriver.measCurrVal = viewlet.getCustomKpiVal();
            } else {
                viewlet.ispdfEnabled = true;
                String kpiVal = dashboardTemplateBD.getComplexKpisData(request, response, request.getSession(false), viewlet, oneContainer.oneviewId, Integer.parseInt(viewlet.getNoOfViewLets()));
                pdfDriver.measCurrVal = viewlet.getCustomKpiVal();
            }

        } catch (Exception e) {
            logger.error("Exception:", e);
        }



    }

    public boolean deleteFileRDir(File delFile) {
        boolean flag = false;
        try {
            if (delFile.isDirectory()) {
                String[] inFiles = delFile.list();
                if (inFiles.length == 0) {
                    flag = delFile.delete();
                } else {
//                String[] inFiles=delFile.list();
                    for (String temp : inFiles) {
                        File fileDelete = new File(delFile, temp);
                        deleteFileRDir(fileDelete);
                    }
                    if (delFile.list().length == 0) {
                        flag = delFile.delete();
                    }
                }
            } else {
                flag = delFile.delete();
            }
        } catch (Exception e) {
            logger.error("Exception:", e);
        }
        return flag;
    }

    public boolean writeFileDetails(String filePath, String fileName, Object obj) throws Exception {
        boolean flag = false;
        File file = null;
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
//        try{
        fos = new FileOutputStream(filePath + "/" + fileName);
        oos = new ObjectOutputStream(fos);
        oos.writeObject(obj);
        oos.close();
        fos.close();
        flag = true;
//        }catch(Exception e){
//            logger.error("Exception:",e);
//        }
        return flag;
    }

    public Object readFileDetails(String filePath, String fileName) throws Exception {
        Object redObj = null;
        FileInputStream fis = null;
        ObjectInputStream ois = null;
//        try{
        fis = new FileInputStream(filePath + "/" + fileName);
        ois = new ObjectInputStream(fis);
        redObj = ois.readObject();
        ois.close();
        fis.close();
//        }catch(Exception e){
//            logger.error("Exception:",e);
//        }
        return redObj;
    }

    public String getRangeBasedColor(OneViewLetDetails viewlet, List timeDetails, BigDecimal currVal, HashMap timeMemMap) {
        String colorCode = "";
        HashMap rangeColorMap = viewlet.getRangeColorMap();
        Set keySet = rangeColorMap.keySet();
        Iterator iter = keySet.iterator();
        int numDays = 0;
        try {
            if (timeMemMap != null && !timeMemMap.isEmpty()) {
                ArrayList timeMemList = (ArrayList) timeMemMap.get("PR_DAY_DENOM");
                if (timeMemList != null && !timeMemList.isEmpty()) {
                    String fromDateStr = timeMemList.get(0).toString();
                    String toDateStr = timeMemList.get(1).toString();
                    if (fromDateStr.length() >= 10 && toDateStr.length() >= 10) {
                        SimpleDateFormat sdf = new SimpleDateFormat("mm/dd/yyyy");
                        fromDateStr = fromDateStr.substring(0, 9);
                        toDateStr = toDateStr.substring(0, 9);
                        Date frmDate = sdf.parse(fromDateStr);
                        Date toDate = sdf.parse(toDateStr);
                        numDays = (int) ((toDate.getTime() - frmDate.getTime()) / (1000 * 60 * 60 * 24));
                    }
                }
            } else {
                if (timeDetails.get(3).toString().equalsIgnoreCase("month")) {
                    numDays = 30;
                }
                if (timeDetails.get(3).toString().equalsIgnoreCase("Qtr")) {
                    numDays = 90;
                }
                if (timeDetails.get(3).toString().equalsIgnoreCase("Year")) {
                    numDays = 365;
                }
                if (timeDetails.get(3).toString().equalsIgnoreCase("Day")) {
                    numDays = 1;
                }
                if (timeDetails.get(3).toString().equalsIgnoreCase("week")) {
                    numDays = 7;
                }
            }

            while (iter.hasNext()) {
                String key = iter.next().toString();
                ArrayList details = (ArrayList) rangeColorMap.get(key);
                if (details.size() > 3) {
                    String operator = details.get(0).toString();
                    Double range1 = Double.parseDouble(details.get(1).toString());

                    Double expVal = range1 * numDays;
                    Double currentVal = currVal.doubleValue();
                    if (operator.equalsIgnoreCase(">")) {
                        if (currentVal > expVal) {
                            colorCode = details.get(3).toString();
                        }
                    }
                    if (operator.equalsIgnoreCase("<")) {
                        if (currentVal < expVal) {
                            colorCode = details.get(3).toString();
                        }
                    }
                    if (operator.equalsIgnoreCase("==")) {
                        if (currentVal == expVal) {
                            colorCode = details.get(3).toString();
                        }
                    }
                    if (operator.equalsIgnoreCase(">=")) {
                        if (currentVal >= expVal) {
                            colorCode = details.get(3).toString();
                        }
                    }
                    if (operator.equalsIgnoreCase("<=")) {
                        if (currentVal <= expVal) {
                            colorCode = details.get(3).toString();
                        }
                    }
                    if (operator.equalsIgnoreCase("!=")) {
                        if (currentVal != expVal) {
                            colorCode = details.get(3).toString();
                        }
                    }
                    if (operator.equalsIgnoreCase("<>")) {
                        Double range2 = Double.parseDouble(details.get(2).toString());
                        Double expVal2 = range2 * numDays;
                        if (currentVal >= expVal && currentVal <= expVal2) {
                            colorCode = details.get(3).toString();
                        }
                    }

                }

            }
        } catch (Exception e) {
            logger.error("Exception:", e);
        }

        return colorCode;
    }

    public ArrayList getOneviewMeasureTimeData(String eleId, String roleId, String userId, List<String> multiperiods, String alertDate, List<String> dataSelectionTypes) {

        ArrayList<String> timedetailsData = new ArrayList<String>();
        ArrayList timedetails = new ArrayList();

        timedetails.add("Day");
        timedetails.add("PRG_STD");
        timedetails.add(alertDate);
        timedetails.add("Month");
        timedetails.add("Last Period");

        ArrayList QueryCols = new ArrayList();
        ArrayList QueryAggs = new ArrayList();
        List<String> measureIdVal = new ArrayList<String>();
        measureIdVal.add(eleId);
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
        PbReportQuery repQuery = new PbReportQuery();
        repQuery.setRowViewbyCols(new ArrayList());
        repQuery.setColViewbyCols(new ArrayList());
        repQuery.setQryColumns(QueryCols);
        repQuery.setColAggration(QueryAggs);
        repQuery.setTimeDetails(timedetails);
        repQuery.setDefaultMeasure(String.valueOf(QueryCols.get(0)));
        repQuery.setDefaultMeasureSumm(String.valueOf(QueryAggs.get(0)));
        repQuery.isKpi = true;
        repQuery.setBizRoles(roleId);
        repQuery.setUserId(userId);

        PbReturnObject kpiMulitiObject = new PbReturnObject();
        ArrayList tempTimeDetailsArrayList = (ArrayList) timedetails.clone();
        for (String str : multiperiods) {
            tempTimeDetailsArrayList.set(3, str);
            repQuery.setTimeDetails(tempTimeDetailsArrayList);
            kpiMulitiObject = repQuery.getPbReturnObjectWithFlag(String.valueOf(QueryCols.get(0)));
            String value = "";
            if (kpiMulitiObject.rowCount > 0) {
                if (!dataSelectionTypes.isEmpty()) {
                    String prefix = dataSelectionTypes.get(0);
                    String sufix = dataSelectionTypes.get(1);
                    String numberFrmt = dataSelectionTypes.get(2);
                    String roundVal = dataSelectionTypes.get(3);

                    if ((prefix == null || prefix.equalsIgnoreCase("")) && (sufix == null || sufix.equalsIgnoreCase(""))) {
                        if (!kpiMulitiObject.getFieldValueString(0, 1).equalsIgnoreCase("")) {
                            value = NumberFormatter.getModifiedNumber(new BigDecimal(kpiMulitiObject.getFieldValueString(0, 1)), numberFrmt, Integer.parseInt(roundVal)).replace(numberFrmt, "");
                        } else {
                            value = "0";
                        }
                    } else if ((prefix != null || !prefix.equalsIgnoreCase("")) && (sufix == null || sufix.equalsIgnoreCase(""))) {
                        if (!kpiMulitiObject.getFieldValueString(0, 1).equalsIgnoreCase("")) {
                            value = prefix.concat(" ") + (NumberFormatter.getModifiedNumber(new BigDecimal(kpiMulitiObject.getFieldValueString(0, 1)), numberFrmt, Integer.parseInt(roundVal)).replace(numberFrmt, ""));
                        } else {
                            value = "0";
                        }
                    } else if ((prefix == null || prefix.equalsIgnoreCase("")) && (sufix != null || !sufix.equalsIgnoreCase(""))) {
                        if (!kpiMulitiObject.getFieldValueString(0, 1).equalsIgnoreCase("")) {
                            value = (NumberFormatter.getModifiedNumber(new BigDecimal(kpiMulitiObject.getFieldValueString(0, 1)), numberFrmt, Integer.parseInt(roundVal)).replace(numberFrmt, "")) + " " + sufix;
                        } else {
                            value = "0";
                        }
                    } else {
                        if (!kpiMulitiObject.getFieldValueString(0, 1).equalsIgnoreCase("")) {
                            value = prefix.concat(" ") + (NumberFormatter.getModifiedNumber(new BigDecimal(kpiMulitiObject.getFieldValueString(0, 1)), numberFrmt, Integer.parseInt(roundVal)).replace(numberFrmt, "")) + " " + sufix;
                        } else {
                            value = "0";
                        }
                    }
                } else {
                    value = NumberFormatter.getModifidNumber(new BigDecimal(kpiMulitiObject.getFieldValueString(0, 1)));
                    int decimalPlaces = 1;
                    BigDecimal bd = new BigDecimal(value);
                    bd = bd.setScale(decimalPlaces, BigDecimal.ROUND_HALF_UP);
                    NumberFormat formatter = NumberFormat.getInstance(new Locale("en_US"));
                    value = formatter.format(bd);
                }
            } else {
                value = "0";
            }
            if (str.equalsIgnoreCase("Year")) {
                timedetailsData.add(value);
            } else if (str.equalsIgnoreCase("Qtr")) {
                timedetailsData.add(value);
            } else if (str.equalsIgnoreCase("Month")) {
                timedetailsData.add(value);
            } else if (str.equalsIgnoreCase("Week")) {
                timedetailsData.add(value);
            } else if (str.equalsIgnoreCase("Day")) {
                timedetailsData.add(value);
            }
        }
        return timedetailsData;
    }

    public PbReturnObject getOneviewMeasureOptions(String oneviewId) {
        String finalVal = "SELECT ONEVIEW_FILE ,FILEPATH FROM  PRG_AR_ONEVIEW_FILE_DATA WHERE ONEVIEWID='" + oneviewId + "' ";
        PbReturnObject retObj = null;

        PbDb pbDb = new PbDb();
        try {
            retObj = pbDb.execSelectSQL(finalVal);
        } catch (Exception exception) {
            logger.error("Exception:", exception);
        }
        return retObj;
    }
    //added by Dinanath for dashboard scheduler

    public void insertOneviewMesureAlerts(String oneviewId, String userId, ReportSchedule schedule, String regionId) throws Exception {

        String elemId = schedule.getElementID();
        String qry = getResourceBundle().getString("insertOneviewMeasureAlerts");
//        String finalstring = getResourceBundle().getString("updateOneviewMeasureAlerts");
        PbReturnObject retObj = null;
//        retObj = testForOneviewMeasureAlerts(oneviewId, elemId,regionId);
        String query3 = "select  max(schedulerId) as schedulerId from prg_ar_oneview_measure_alerts;";
        Object[] obj3 = null;
        try {
//
            PbDb pbDb = new PbDb();
            String finalqry = query3;
            retObj = pbDb.execSelectSQL(finalqry);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        boolean test = false;
        String schedulerId = "";
        int schedulerIdMax = 0;
        String finalqry = "";
        try {
            if (retObj.rowCount > 0) {
//            test = true;
                schedulerId = Integer.toString(retObj.getFieldValueInt(0, "schedulerId"));
                schedulerIdMax = Integer.parseInt(schedulerId);
                schedulerIdMax = schedulerIdMax + 1;
            } else {
                schedulerIdMax = 1;
            }
        } catch (Exception e) {
            schedulerIdMax = 1;
        }
        PbDb pbDb = new PbDb();
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(schedule.getStartDate());
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(schedule.getEndDate());
        Date sdate = schedule.getStartDate();
        Date edate = schedule.getEndDate();
        schedule.setStartDate(null);
        schedule.setEndDate(null);
        schedule.setReportScheduledId(schedulerIdMax);
        List<ReportSchedule> scheduleList = new ArrayList<ReportSchedule>();
        scheduleList.add(schedule);
        Gson gson = new Gson();
        String gsonString = gson.toJson(scheduleList);
        String startDate = "";
        String endDate = "";

        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
            startDate = startCalendar.get(Calendar.YEAR) + "-" + (startCalendar.get(Calendar.MONTH) + 1) + "-" + startCalendar.get(Calendar.DATE);
            endDate = endCalendar.get(Calendar.YEAR) + "-" + (endCalendar.get(Calendar.MONTH) + 1) + "-" + endCalendar.get(Calendar.DATE);
        } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
            startDate = startCalendar.get(Calendar.YEAR) + "-" + (startCalendar.get(Calendar.MONTH) + 1) + "-" + startCalendar.get(Calendar.DATE);
            endDate = endCalendar.get(Calendar.YEAR) + "-" + (endCalendar.get(Calendar.MONTH) + 1) + "-" + endCalendar.get(Calendar.DATE);
        } else {
            startDate = startCalendar.get(Calendar.DATE) + "-" + (startCalendar.get(Calendar.MONTH) + 1) + "-" + startCalendar.get(Calendar.YEAR);
            endDate = endCalendar.get(Calendar.DATE) + "-" + (endCalendar.get(Calendar.MONTH) + 1) + "-" + endCalendar.get(Calendar.YEAR);
        }

        Object[] obj = null;
//        if (!test) {
        obj = new Object[9];
        obj[0] = userId;
        obj[1] = schedule.getSchedulerName();
        obj[2] = oneviewId;
        obj[3] = schedule.getElementID();;
        obj[4] = gsonString;
        obj[5] = startDate;
        obj[6] = endDate;
        obj[7] = regionId;
        obj[8] = schedulerIdMax;
        finalqry = pbDb.buildQuery(qry, obj);
//        } else {
//            obj = new Object[5];
//            obj[0] = schedule.getSchedulerName();
//            obj[1] = gsonString;
//            obj[2] = startDate;
//            obj[3] = endDate;
//            obj[4]=oneviewAlertId;
//            finalqry = pbDb.buildQuery(finalstring, obj);
//        }
        //        try {
        int execUpdateSQL = pbDb.execUpdateSQL(finalqry);
//        } catch (Exception ex) {
//            logger.error("Exception:",ex);
//        }
        schedule.setStartDate(sdate);
        schedule.setEndDate(edate);

        retObj = getOneviewMeasureOptions(oneviewId);
        ArrayList<String> oneviewMsrOptions = new ArrayList<String>();
        if (retObj.rowCount > 0) {
            oneviewMsrOptions.add(retObj.getFieldValueString(0, 0));
            oneviewMsrOptions.add(retObj.getFieldValueString(0, 1));
        }
        schedule.setOneviewMeasureOptions(oneviewMsrOptions);
    }

    public PbReturnObject testForOneviewMeasureAlerts(String oneviewId, String elemId, String regionId) {

        String qry = getResourceBundle().getString("getOneviewMeasureAlertsTest");
        PbReturnObject retObj = null;
        Object[] obj = null;
        try {
            obj = new Object[3];
            obj[0] = oneviewId;
            obj[1] = elemId;
            obj[2] = regionId;
            PbDb pbDb = new PbDb();
            String finalqry = pbDb.buildQuery(qry, obj);
            retObj = pbDb.execSelectSQL(finalqry);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        return retObj;
    }

    public HashMap getFormatedMesureValues(HashMap formalValMap, int minRadius, String formatType, int roundVal) {
//        
        HashMap newValMap = new HashMap();
        Double currVal = (Double) formalValMap.get("currVal");
        Double priorVal = (Double) formalValMap.get("priorVal");
        String currValStr = "";
        String priorValStr = "";
        int length = ((6 * minRadius) / 40);
        String boundTo = formatType;
        /*
         * if(formatType != null && !formatType.equalsIgnoreCase(" ")){
         * currVal=getNumberFromFormat(currVal, formatType);
         * priorVal=getNumberFromFormat(priorVal, formatType);
        }
         */
        if (formatType.trim().length() > 0) {
            currVal = getNumberFromFormat(currVal, formatType);
            priorVal = getNumberFromFormat(priorVal, formatType);
        }
        if (currVal > priorVal) {
            ArrayList priorValList = formatVal(priorVal, length, formatType, roundVal, "temp");
            priorValStr = priorValList.get(0).toString();
            boundTo = priorValList.get(1).toString();
            ArrayList currValList = formatVal(currVal, length, boundTo, roundVal, boundTo);
            currValStr = currValList.get(0).toString();
        } else {
            ArrayList currValList = formatVal(currVal, length, formatType, roundVal, "temp");
            currValStr = currValList.get(0).toString();
            boundTo = currValList.get(1).toString();
            ArrayList priorValList = formatVal(priorVal, length, boundTo, roundVal, boundTo);
            priorValStr = priorValList.get(0).toString();
        }


        newValMap.put("currVal", currValStr);
        newValMap.put("priorVal", priorValStr);
        newValMap.put("format", boundTo);
//        
        return newValMap;
    }
    /*
     * returns foramtted value and formatted type
     */

    public static ArrayList formatVal(Double val, int maxChar, String format, int roundVal, String boundToFormat) {
        ArrayList formatValList = new ArrayList();
        String formatVal = "";
        ArrayList formatTypes = new ArrayList() {

            {
                add("");
                add("K");
                add("L");
                add("M");
                add("Cr");
            }
        };
        String valStr = NumberFormatter.getModifiedNumber(val, format, roundVal);
        if (format.equalsIgnoreCase("Cr") || (format.equalsIgnoreCase(boundToFormat))) {
            formatVal = valStr;
            formatValList.add(formatVal);
            formatValList.add(format);
            return formatValList;
        } else {
            if (valStr.length() > maxChar) {
                if (formatTypes.contains(format) && formatTypes.indexOf(format) < formatTypes.size()) {
                    format = formatTypes.get(formatTypes.indexOf(format) + 1).toString();
                    formatValList = formatVal(val, maxChar, format, roundVal, boundToFormat);
                }
                return formatValList;
            } else {
                formatVal = valStr;
                formatValList.add(formatVal);
                formatValList.add(format);
                return formatValList;
            }
        }

    }

    public static double getNumberFromFormat(double calValue, String format) {
//        double calValue=Double.parseDouble(val);
        if (format != null && !format.equalsIgnoreCase(" ")) {
            if (format.equalsIgnoreCase("K")) {
                calValue = calValue * NumberFormatter.getPowerOfTen(3);
            } else if (format.equalsIgnoreCase("L")) {
                calValue = calValue * NumberFormatter.getPowerOfTen(5);
            } else if (format.equalsIgnoreCase("M") || format.equalsIgnoreCase("Mn")) {
                calValue = calValue * NumberFormatter.getPowerOfTen(6);
            } else if (format.equalsIgnoreCase("Cr")) {
                calValue = calValue * NumberFormatter.getPowerOfTen(7);
            }
        }
        return calValue;
    }

    public String getMeasureMeterGraph(double changePer, int height, int width, LinkedHashMap rangeMap, String divId, OneViewLetDetails viewlet, boolean isZoom, int days, Double currVal) {
        //
        ProgenJQPlotGraph jqgraph = new ProgenJQPlotGraph();
        StringBuilder meterScript = new StringBuilder();
        double iPoint = 0;
        double fPoint = 0;
        double sPoint = 0;
        double ePoint = 0;
        KPIGraph kpiGrph = new KPIGraph();
        String function = "";
        int divHeight = height;
        double targetVal = targetVal = viewlet.getTargetValPerDay();
        String round = viewlet.getFormatVal() == null ? "" : viewlet.getFormatVal();
        String formattedTarget = NumberFormatter.getModifiedNumber((targetVal * days), round, Integer.parseInt(viewlet.getRoundVal()));
        String formateedCurr = NumberFormatter.getModifiedNumber(currVal, round, Integer.parseInt(viewlet.getRoundVal()));
        String dialMeasureBase = (viewlet.getDialMeasureBase() == null ? "" : viewlet.getDialMeasureBase());
        int zindex = 0;
        int top = 0;
//        if(dialMeasureBase.equalsIgnoreCase("changePer")){
//            targetVal=changePer;
//        }else
        if (dialMeasureBase.equalsIgnoreCase("deviationPer")) {
            targetVal = getDeviation(currVal, (targetVal * days));
        }
//            else{
//            Long temp=Math.round(targetVal);
//            targetVal=Double.parseDouble(temp.toString());
//        }

        if (isZoom) {
            divHeight = height - ((height * 100) / 20);
            function = "javascript:void(0)";
        } else {
            divHeight = 140;
            width = 240;
            zindex = -1;
            top = -2;
            function = "zoomMeasureDialChart(\"" + targetVal + "\",\"" + viewlet.getNoOfViewLets() + "\",\"" + viewlet.getRepName() + "\",\"" + currVal + "\")";
        }
        // 
//        changePer=Double.parseDouble(NumberFormatter.getModifiedNumber(targetVal, "", 2));
        // 


        if (rangeMap == null || rangeMap.isEmpty()) {
            iPoint = targetVal - 20;
            ePoint = targetVal + 20;
            /*
             * if(changePer < 0){ iPoint=changePer+20;
             * ePoint=Math.abs(changePer-20); }else{ iPoint=changePer-20;
             * ePoint=changePer+20;
            }
             */
            fPoint = targetVal;
            sPoint = targetVal;
        } else {
            /*
             * ArrayList rangeDetail = null; String[] keys = {"High", "Medium",
             * "Low"}; for (int i = 0; i < rangeMap.size(); i++) { rangeDetail =
             * (ArrayList) rangeMap.get(keys[i]); if (!rangeDetail.isEmpty() &&
             * rangeDetail.size() == 3) { KPIColorRange kpicolorObj = new
             * KPIColorRange(); kpicolorObj.setColor(keys[i]);
             * kpicolorObj.setOperator(rangeDetail.get(0).toString());
             * kpicolorObj.setRangeStartValue(Double.parseDouble(rangeDetail.get(1).toString()));
             * kpicolorObj.setRangeEndValue(Double.parseDouble(rangeDetail.get(2).toString()));
             * kpiGrph.addkpiGrphColorRange(keys[i], kpicolorObj); } }
             * kpiGrph.initializeRanges(); iPoint = kpiGrph.getStartRange();
             * ePoint = kpiGrph.getEndRange(); fPoint = kpiGrph.getFsplit();
             * sPoint = kpiGrph.getSsplit();
             */

            HashMap resultMap = evaluateMeterPoints(targetVal, rangeMap);
            iPoint = (Double) resultMap.get("intialPoint");
            fPoint = (Double) resultMap.get("firstPoint");
            sPoint = (Double) resultMap.get("secondPoint");
            ePoint = (Double) resultMap.get("endPoint");
        }
        String suffix = "";
        if (viewlet.getSuffixValue() != null) {
            suffix = viewlet.getSuffixValue();
        }

        if (height <= 80) {
            jqgraph.setShowTickLabels(false);
        } else {
            jqgraph.setShowTickLabels(true);
        }
        String measurecolor = "";
        if (viewlet.getMeasureColor() != null && !viewlet.getMeasureColor().isEmpty()) {
            measurecolor = viewlet.getMeasureColor();
        }
        if (measurecolor.equalsIgnoreCase("")) {
            if (viewlet.getCurrValue().contains("-")) {
                measurecolor = "red";
            }
        }
        meterScript.append("<style>#Dashlets-" + divId + " {background:none; }</style>");
        meterScript.append("<div style='float:left; width:10px; padding:-2em; margin:auto;z-index:1000;top:" + top + "em ; font-size:12pt; color:" + measurecolor + ";' >" + formateedCurr + "</div>");

        meterScript.append("<div id='meterZoomContent' style='width:" + width + "px;height:" + height + "px;' onclick='" + function + "'>");
//          meterScript.append("<div style='margin:auto;z-index:"+zindex+";top:"+top+"em' >");
//        meterScript.append("<table border='0' width='100%'>");
//        meterScript.append("<tr>");
//        meterScript.append("<td width='100%' height='2%' style='font-size:10pt; color:"+measurecolor+"' >"+formateedCurr+"</td></tr></table></div>");
        meterScript.append("<table border='0' width='100%'>");
        if (isZoom) {
            meterScript.append("<tr>");
            meterScript.append("<td width='100%' height='20%' >");
            meterScript.append("<table border='1' width='100%'>");
            meterScript.append("<thead>");
            if (dialMeasureBase.equalsIgnoreCase("deviationPer")) {
                meterScript.append("<th>Target</th><th>Current</th><th>Deviation Percent</th>");
            } else if (dialMeasureBase.equalsIgnoreCase("absolute")) {
                meterScript.append("<th>Current</th>");
            } else {
                meterScript.append("<th>Current</th><th>Prior</th><th>Change Percent</th>");
            }
            meterScript.append("</thead>");
            meterScript.append("<tr>");
            if (dialMeasureBase.equalsIgnoreCase("deviationPer")) {
                meterScript.append("<td>" + formattedTarget + suffix + "</td>");
                meterScript.append("<td>" + formateedCurr + suffix + "</td>");
                meterScript.append("<td>" + targetVal + "</td>");
            } else if (dialMeasureBase.equalsIgnoreCase("absolute")) {
                meterScript.append("<td>" + formateedCurr + suffix + "</td>");
            } else {
                meterScript.append("<td>" + formateedCurr + suffix + "</td>");
                meterScript.append("<td>" + viewlet.getPriorValue() + "</td>");
                meterScript.append("<td>" + changePer + "</td>");
            }
            meterScript.append("</tr>");
            meterScript.append("</table>");
            meterScript.append("</td>");
            meterScript.append("</tr>");
        }

        jqgraph.setMeasureType(viewlet.getMeasType());

        meterScript.append("<tr>");
        meterScript.append("<td width='100%' height='40%' >");
        meterScript.append("<div id='chart-" + divId + "' style='height:" + divHeight + "px;width:" + width + "px;margin:auto;z-index:" + zindex + ";top:" + top + "em' ></div>");
//        if(dialMeasureBase.equalsIgnoreCase("absolute")){
//            String result=jqgraph.prepareMeterGraph(iPoint, fPoint, sPoint, ePoint, targetVal, divId);
//            
//            meterScript.append(result);
////            meterScript.append(jqgraph.prepareMeterGraph((int)iPoint, (int)fPoint, (int)sPoint, (int)ePoint, (int)targetVal, divId));
//        }else{
        meterScript.append(jqgraph.prepareMeterGraph(iPoint, fPoint, sPoint, ePoint, targetVal, divId));
//        }

        meterScript.append("</td>");
        meterScript.append("</tr>");
        meterScript.append("</table>");
        return meterScript.toString();
    }

    public HashMap evaluateMeterPoints(double changePer, LinkedHashMap rangeMap) {
        Set keySet = rangeMap.keySet();
        HashMap resultMap = new HashMap();
        Double initialPoint = null;
        Double firstPoint = null;
        Double secoundPoint = null;
        Double endPoint = null;
        Iterator keyIter = keySet.iterator();
        String key = "";
        String operator = "";
        double startRange = 0;
        double endRannge = 0;
        ArrayList details = new ArrayList();
        while (keyIter.hasNext()) {
            key = keyIter.next().toString();
            details = (ArrayList) rangeMap.get(key);
            operator = details.get(0).toString();
            startRange = Double.parseDouble(details.get(1).toString());
            if (operator.equalsIgnoreCase(">") || operator.equalsIgnoreCase(">=")) {
                if (initialPoint == null) {       /*
                     * for first iteration
                     */
                    initialPoint = (startRange - 20);
                    firstPoint = startRange;
                } else if (firstPoint == null) {     /*
                     * for second iteration
                     */
                    firstPoint = startRange;
                } else {                          /*
                     * for third iteration
                     */
                    if (secoundPoint == null) {
                        secoundPoint = startRange;
                        if (changePer > startRange) {
                            endPoint = changePer + 20;
                        }
                    } else {
                        endPoint = startRange;
                        if (changePer > endPoint) {
                            endPoint = changePer + 20;
                        }
                    }
                }

            } else if (operator.equalsIgnoreCase("<>") || operator.equalsIgnoreCase("between")) {
                endRannge = Double.parseDouble(details.get(2).toString());
                if (initialPoint == null) {       /*
                     * for first iteration
                     */
                    initialPoint = startRange;
                    firstPoint = endRannge;
                } else {                          /*
                     * for second and third iteration
                     */
                    if (secoundPoint == null) {
                        firstPoint = startRange;
                        secoundPoint = endRannge;
                    } else {
                        secoundPoint = startRange;
                        endPoint = endRannge;
                    }
//                    else{
//                        endPoint=endRannge;
//                    }

                }
            } else if (operator.equalsIgnoreCase("<") || operator.equalsIgnoreCase("<=")) {
                if (initialPoint == null) {           /*
                     * for first iteration
                     */
                    if (changePer <= startRange) {
                        initialPoint = (changePer - 20);
                        firstPoint = startRange;
                    } else {
                        initialPoint = (startRange - 20);
                        firstPoint = startRange;
                    }
                } else if (firstPoint == null) {   /*
                     * for second iteration
                     */
                    firstPoint = startRange;
                } else {
                    if (secoundPoint == null) {
                        secoundPoint = startRange;
                    } else {
                        endPoint = startRange;
                    }

                }
            }
        }
        resultMap.put("intialPoint", initialPoint);
        resultMap.put("firstPoint", firstPoint);
        resultMap.put("secondPoint", secoundPoint);
        resultMap.put("endPoint", endPoint);
        //
        // 
        //
        // 
        return resultMap;
    }

    public double getDeviation(Double actulaValue, Double targetValue) {

        Double deviation = (((double) ((actulaValue - targetValue)) / targetValue) * 100);

        double tempDev = deviation;

        deviation = Math.abs(deviation);

        double d = tempDev;//deviation[i];
        double p = (float) Math.pow(10, 2);
        double Rval2 = 0;
        Rval2 = d * p;
        float tmp = Math.round(Rval2);
        double finalDev = tmp / p;

        return finalDev;
    }

    public int getOneViewDays(OnceViewContainer onecontainer) {
        ArrayList timeMemList = (onecontainer.timeHashMap != null) ? (ArrayList) onecontainer.timeHashMap.get("PR_DAY_DENOM") : null;
        int days = 0;
        try {
            if (timeMemList != null && !timeMemList.isEmpty()) {
                String fromDateStr = timeMemList.get(0).toString();
                String toDateStr = timeMemList.get(1).toString();
                if (fromDateStr.length() >= 10 && toDateStr.length() >= 10) {
                    SimpleDateFormat sdf;
                    if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                        sdf = new SimpleDateFormat("yyyy-mm-dd");
                        fromDateStr = fromDateStr.substring(0, 10);
                        toDateStr = toDateStr.substring(0, 10);
                    } else {
                        sdf = new SimpleDateFormat("mm/dd/yyyy");
                        fromDateStr = fromDateStr.substring(0, 9);
                        toDateStr = toDateStr.substring(0, 9);
                    }
                    Date frmDate = sdf.parse(fromDateStr);
                    Date toDate = sdf.parse(toDateStr);

                    //                        Calendar frmCal=new GregorianCalendar();
                    //                        Calendar toCal=new GregorianCalendar();
                    //                        frmCal.setTime(frmDate);
                    //                        toCal.setTime(toDate);
                    days = (int) ((toDate.getTime() - frmDate.getTime()) / (1000 * 60 * 60 * 24)) + 1;
                }
            }

        } catch (Exception e) {
            logger.error("Exception:", e);
        }
        return days;
    }

    public void checkRoleAssign(String oneviewId, String roleId) {
        String qry = "select ROLES_IN_ONE_VIEW from PRG_AR_ONEVIEW_FILE_DATA where ONEVIEWID = '" + oneviewId + "'";
        PbDb pbdb = new PbDb();
        PbReturnObject retObj = new PbReturnObject();
        try {
            retObj = pbdb.executeSelectSQL(qry);
            if (retObj != null && retObj.getRowCount() > 0) {
                if (retObj.getFieldValueString(0, 0) == null || retObj.getFieldValueString(0, 0).equalsIgnoreCase("")) {
                    String insertqry = "update PRG_AR_ONEVIEW_FILE_DATA set ROLES_IN_ONE_VIEW = " + roleId + " where ONEVIEWID = '" + oneviewId + "'";
                    try {
                        pbdb.execUpdateSQL(insertqry);
                    } catch (Exception ex) {
                        logger.error("Exception:", ex);
                    }
                }
            }
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
    }

    public void setDimensionDetails(String oneviewId, String dimIds, String dimName, HttpSession session) {

        FileInputStream fis2;
        try {

            String advHtmlFileProps = (String) session.getAttribute("oldAdvHtmlFileProps");
            ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();
            String fileName = reportTemplateDAO.getOneviewFileName(oneviewId);
            //String fileName = session.getAttribute("tempFileName").toString();
            OnceViewContainer oneContainer = (OnceViewContainer) this.readFileDetails(advHtmlFileProps, fileName);
            ArrayList paramIds = new ArrayList();
            ArrayList paramNames = new ArrayList();
            if (dimIds != null && !dimIds.isEmpty()) {
                String[] dimidstr = dimIds.split(",");
                String[] dimNamestr = dimName.split(",");
                for (int i = 0; i < dimidstr.length; i++) {
                    paramIds.add(dimidstr[i]);
                }
                for (int i = 0; i < dimNamestr.length; i++) {
                    paramNames.add(dimNamestr[i]);
                }
            }
            oneContainer.setParamIds(paramIds);
            oneContainer.setParamNames(paramNames);
            this.writeFileDetails(advHtmlFileProps, fileName, oneContainer);

        } catch (Exception e) {
            logger.error("Exception:", e);
        }

    }

    public String generateBusinessTemplateRegion(HttpServletRequest request, HttpServletResponse response, OnceViewContainer onecontainer) {
        HttpSession session = request.getSession(false);
        String finalStringVal = "";
        String oneviewId = onecontainer.oneviewId;
        int innerWidth = onecontainer.width;
        int screenwidth = innerWidth - 140;
        int templateWidth[] = {((screenwidth / 5) + (screenwidth / 5)), screenwidth / 5, screenwidth / 5, screenwidth / 5, screenwidth / 5, screenwidth / 5, screenwidth / 5, screenwidth / 5, screenwidth / 5, screenwidth / 5};
        int templateHeight[] = {450, 150, 150, 150, 150, 150, 150, 150, 150, 150};
        String repType[] = {"TemplateGraph", "TemplateMeasure", "TemplateMeasure", "TemplateMeasure", "TemplateMeasure", "TemplateMeasure", "TemplateMeasure", "TemplateMeasure", "TemplateMeasure", "TemplateMeasure"};
        int noofViewelets;
        int MsrtempWidth[] = {screenwidth, screenwidth / 2, screenwidth / 2, screenwidth / 2, screenwidth / 2, screenwidth / 2, screenwidth / 2};
        int MsrtempHeight[] = {150, 450, 450, 450, 450, 450, 450};
        String MsrrepType[] = {"TemplateTable", "MsrTemplateGraph", "MsrTemplateGraph", "MsrTemplateGraph", "MsrTemplateGraph", "MsrTemplateGraph", "MsrTemplateGraph"};
        if (onecontainer.getOneViewType() != null && onecontainer.getOneViewType().equalsIgnoreCase("Business TemplateView")) {
            noofViewelets = 10;
        } else {
            noofViewelets = 7;
        }
        OneViewLetDetails detail = null;
        HashMap tempRegHashMap = new HashMap();
        Container container = onecontainer.getContainer();
        HashMap ParametersHashMap = container.getParametersHashMap();
        ArrayList Parameters = (ArrayList) ParametersHashMap.get("Parameters");
        ArrayList ParametersNames = (ArrayList) ParametersHashMap.get("ParametersNames");
//        String[] param = new String[Parameters.size()+1];
//        String[] paramName = new String[Parameters.size()+1];
//        
        String[] param = (String[]) Parameters.toArray(new String[Parameters.size() + 1]);
//        
        param[Parameters.size()] = "TIME";
//        paramName =(String[])ParametersNames.toArray();
        String[] paramName = (String[]) ParametersNames.toArray(new String[Parameters.size() + 1]);
        paramName[Parameters.size()] = "TIME";
//        Container container=onecontainer.getContainer();
//        ArrayList measures=(ArrayList)container.getDisplayColumns();
//        DashboardTemplateDAO daofrmsrattr = new DashboardTemplateDAO();
        for (int i = 0; i < noofViewelets; i++) {
            detail = new OneViewLetDetails();
            detail.setNoOfViewLets(String.valueOf(i));
            detail.setOneviewId(oneviewId);
            if (onecontainer.getOneViewType() != null && onecontainer.getOneViewType().equalsIgnoreCase("Business TemplateView")) {
                detail.setWidth(templateWidth[i]);
                detail.setHeight(templateHeight[i]);
                detail.setReptype(repType[i]);
            } else {
                if (i != 0 && param.length >= i) {
                    detail.setParamId(param[i - 1]);
                    detail.setParamName(paramName[i - 1]);
                } else {
                    detail.setParamId("");
                    detail.setParamName("");
                }
                detail.setWidth(MsrtempWidth[i]);
                detail.setHeight(MsrtempHeight[i]);
                detail.setReptype(MsrrepType[i]);
            }
            onecontainer.addDashletDetail(detail);
            String regFileName = "InnerRegionDetails" + oneviewId + "_" + i + "_" + session.getId() + "_" + System.currentTimeMillis() + ".txt";
            tempRegHashMap.put(i, regFileName);
        }
        if (onecontainer.getOneViewType() != null && onecontainer.getOneViewType().equalsIgnoreCase("Measure Based Business Template")) {
            onecontainer.setElementIds((String) ((List) container.getTableHashMap().get("Measures")).get(0));
        }
        onecontainer.SetTempRegHashMap(tempRegHashMap);
//          if(onecontainer.getOneViewType()!=null && onecontainer.getOneViewType().equalsIgnoreCase("Business TemplateView"))
        finalStringVal = generateBusinessTemplateView(request, response, onecontainer);
        return finalStringVal;
    }

    public String buildBusinessTemplateReturnObject(HttpServletRequest request, HttpServletResponse response, OnceViewContainer onecontainer) {
        StringBuilder finalStringVal = new StringBuilder();
        HttpSession session = request.getSession(false);
        String reportId = onecontainer.oneviewId;
        String userId = onecontainer.getUserId();
        Container container = onecontainer.getContainer();
        PbReportViewerBD viewerBd = new PbReportViewerBD();
        try {
            container.setOneviewGraphTimedetails((ArrayList<String>) onecontainer.timedetails);
            viewerBd.prepareReport("", container, container.getReportCollect().reportId, userId, new HashMap());
            if (onecontainer.getOneViewType().equalsIgnoreCase("Business TemplateView")) {
                this.generateTemplateMeasureReturnObject(container, userId);
            } else {
                this.generateMsrTemplateReturnObject(onecontainer.oneviewId, onecontainer, container);
            }
            container.setSessionContext(session, container);
            ReportTemplateBD bd = new ReportTemplateBD();
            DataFacade facade = new DataFacade(container);
            facade.setUserId(userId);
            if (onecontainer.getOneViewType().equalsIgnoreCase("Measure Based Business Template")) {
                facade.isMsrbasedBusTemplate = true;
            }
            facade = bd.generateReportQrys(reportId, facade, userId);
            onecontainer.setFacade(facade);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        finalStringVal.append(generateBusinessTemplateView(request, response, onecontainer));
        return finalStringVal.toString();
    }

    public String generateBusinessTemplateView(HttpServletRequest request, HttpServletResponse response, OnceViewContainer onecontainer) {
        StringBuilder finalStringVal = new StringBuilder();
        String oneviewId = onecontainer.oneviewId;
        List<OneViewLetDetails> onviewLetdetails = onecontainer.onviewLetdetails;
        Container container = onecontainer.getContainer();
        if (onecontainer.getOneViewType() != null && onecontainer.getOneViewType().equalsIgnoreCase("Business TemplateView")) {
            HashMap ParametersHashMap = container.getParametersHashMap();
            ArrayList Parameters = (ArrayList) ParametersHashMap.get("Parameters");
            ArrayList ParametersNames = (ArrayList) ParametersHashMap.get("ParametersNames");
            ArrayList<String> rowviewby = (ArrayList<String>) container.getReportCollect().reportRowViewbyValues;
            String rowViewbyId = (String) rowviewby.get(0);
            finalStringVal.append("<div id='dimensionssion' style='width:100%;height:20%'>");
            finalStringVal.append("<table style='width:auto;'><tr>");
            for (int k = 0; k < Parameters.size(); k++) {
                String parmId = (String) Parameters.get(k);
                String paramName = (String) ParametersNames.get(k);
                if (parmId.equalsIgnoreCase(rowViewbyId)) {
                    finalStringVal.append("<td><input style='width:auto;' class='navtitle' type='button' onclick=\"changeRowViewby('" + oneviewId + "','" + parmId + "')\" value='" + paramName + "'></td>");
                } else {
                    finalStringVal.append("<td><input style='width:auto;' class='navtitle-hover' type='button' onclick=\"changeRowViewby('" + oneviewId + "','" + parmId + "')\" value='" + paramName + "'></td>");
                }
            }
            finalStringVal.append("</tr></table>");
            finalStringVal.append("</div>");

            finalStringVal.append("<div style='display:table;width:100%;'>");
            finalStringVal.append("<div style='width:40%;height:490px;border-spacing: 15px;float:left;'>");
            finalStringVal.append("<div>").append(this.generateInnerTemplateData(request, response, onecontainer, (OneViewLetDetails) onviewLetdetails.get(0))).append("</div>");
            finalStringVal.append("</div>");
            finalStringVal.append("<div style='width:60%;height:490px;border-spacing: 15px;float:left;'>");
            finalStringVal.append("<div>").append(this.generateInnerTemplateData(request, response, onecontainer, (OneViewLetDetails) onviewLetdetails.get(1)));
            finalStringVal.append(this.generateInnerTemplateData(request, response, onecontainer, (OneViewLetDetails) onviewLetdetails.get(2)));
            finalStringVal.append(this.generateInnerTemplateData(request, response, onecontainer, (OneViewLetDetails) onviewLetdetails.get(3))).append("</div>");
            finalStringVal.append("<div>").append(this.generateInnerTemplateData(request, response, onecontainer, (OneViewLetDetails) onviewLetdetails.get(4)));
            finalStringVal.append(this.generateInnerTemplateData(request, response, onecontainer, (OneViewLetDetails) onviewLetdetails.get(5)));
            finalStringVal.append(this.generateInnerTemplateData(request, response, onecontainer, (OneViewLetDetails) onviewLetdetails.get(6))).append("</div>");
            finalStringVal.append("<div>").append(this.generateInnerTemplateData(request, response, onecontainer, (OneViewLetDetails) onviewLetdetails.get(7)));
            finalStringVal.append(this.generateInnerTemplateData(request, response, onecontainer, (OneViewLetDetails) onviewLetdetails.get(8)));
            finalStringVal.append(this.generateInnerTemplateData(request, response, onecontainer, (OneViewLetDetails) onviewLetdetails.get(9))).append("</div>");
            finalStringVal.append("</div>");
            finalStringVal.append("</div>");
        } else {
            HashMap tableHM = container.getTableHashMap();
            List measures = (List) tableHM.get("Measures");
            List measNames = (List) tableHM.get("MeasuresNames");
            finalStringVal.append("<div style='display:table;width:100%;'>");
            finalStringVal.append("<table style='width:auto;margin-left: 10px;'><tr><td>Select Measure</td><td><select id='templateMsrId' name='templateMsrId' onchange=\"changeTemplateMsr('" + oneviewId + "')\">");
            for (int i = 0; i < measures.size(); i++) {
                if (onecontainer.getElementIds() != null && onecontainer.getElementIds().equalsIgnoreCase((String) measures.get(i))) {
                    finalStringVal.append("<option selected value='" + ((String) measures.get(i)).replace("A_", "") + "'>").append((String) measNames.get(i)).append("</option>");
                } else {
                    finalStringVal.append("<option value='" + ((String) measures.get(i)).replace("A_", "") + "'>").append((String) measNames.get(i)).append("</option>");
                }
            }
            finalStringVal.append("</select></td></tr></table>");
            for (int i = 0; i < onviewLetdetails.size(); i++) {
                finalStringVal.append(this.generateInnerTemplateData(request, response, onecontainer, (OneViewLetDetails) onviewLetdetails.get(i)));
//         finalStringVal.append(this.generateMsrTemplateData(onecontainer, (OneViewLetDetails) onviewLetdetails.get(i)));
            }
//     finalStringVal.append("<div>").append(this.generateMsrTemplateData(onecontainer, (OneViewLetDetails) onviewLetdetails.get(0))).append("</div>");
//     finalStringVal.append("<div>").append(this.generateMsrTemplateData(onecontainer, (OneViewLetDetails) onviewLetdetails.get(1)));
//     finalStringVal.append(this.generateMsrTemplateData(onecontainer, (OneViewLetDetails) onviewLetdetails.get(2))).append("</div>");
//     finalStringVal.append("<div>").append(this.generateMsrTemplateData(onecontainer, (OneViewLetDetails) onviewLetdetails.get(3)));
//     finalStringVal.append(this.generateMsrTemplateData(onecontainer, (OneViewLetDetails) onviewLetdetails.get(4))).append("</div>");
//     finalStringVal.append("<div>").append(this.generateMsrTemplateData(onecontainer, (OneViewLetDetails) onviewLetdetails.get(5)));
//     finalStringVal.append(this.generateMsrTemplateData(onecontainer, (OneViewLetDetails) onviewLetdetails.get(6))).append("</div>");
            finalStringVal.append("</div>");
        }
        return finalStringVal.toString();
    }

    public String generateInnerTemplateData(HttpServletRequest request, HttpServletResponse response, OnceViewContainer onecontainer, OneViewLetDetails detail) {
        StringBuilder regionData = new StringBuilder();
        HttpSession session = request.getSession(false);
        HashMap regHashMap = onecontainer.getRegHashMap();
        HashMap tempRegFileMap = onecontainer.getTempRegHashMap();
        String advHtmlFileProps = session.getAttribute("advHtmlFileProps").toString();
        String oldAdvHtmlFileProps = (String) request.getSession(false).getAttribute("oldAdvHtmlFileProps");
        try {
            int i = Integer.parseInt(detail.getNoOfViewLets());
            String action = request.getParameter("action");
            String result = null;
            regionData.append("<div id='" + i + "' style='width:" + detail.getWidth() + "px; height:" + detail.getHeight() + "px;float:left; padding:5px;'>");
            if (action.equalsIgnoreCase("GO")) {
                result = this.buildRegionData(request, response, onecontainer, detail);
                writeOneviewRegData(onecontainer, result, detail.getNoOfViewLets(), request);
            } else if (action.equalsIgnoreCase("open")) {
                File file = new File(oldAdvHtmlFileProps + "/" + regHashMap.get(i));
                if (file.exists()) {
                    FileInputStream fis3 = new FileInputStream(oldAdvHtmlFileProps + "/" + regHashMap.get(i));
                    ObjectInputStream ois3 = new ObjectInputStream(fis3);
                    result = (String) ois3.readObject();
                }
            } else if (action.equalsIgnoreCase("initialSave")) {
                result = this.buildRegionData(request, response, onecontainer, detail);
                writeOneviewRegData(onecontainer, result, detail.getNoOfViewLets(), request);
            } else {
                if (tempRegFileMap.containsKey(i) && tempRegFileMap.get(i) != null) {
                    File file = new File(advHtmlFileProps + "/" + tempRegFileMap.get(i));
                    if (file.exists()) {
                        FileInputStream fis3 = new FileInputStream(advHtmlFileProps + "/" + tempRegFileMap.get(i));
                        ObjectInputStream ois3 = new ObjectInputStream(fis3);
                        result = (String) ois3.readObject();
                    } else {
                        result = this.buildRegionData(request, response, onecontainer, detail);
                    }
                } else {
                    result = this.buildRegionData(request, response, onecontainer, detail);
                }
                this.response = response;
                result = this.saveOneviewRegData(onecontainer, result, detail.getNoOfViewLets(), request);
            }
            regionData.append(result);
            regionData.append("</div>");

        } catch (FileNotFoundException ex) {
            logger.error("Exception:", ex);
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        return regionData.toString();
    }

    public String generatebusinessTemplateGraph(HttpServletRequest request, HttpServletResponse response, OnceViewContainer onecontainer, OneViewLetDetails detail) {
        StringBuilder htmlVar = new StringBuilder();
        HttpSession session = request.getSession(false);
//        String[] jqgrapharray={"Area","Area-Line","Bar-Horizontal","Bar-Vertical","Block","Bubble","Bubble(log)","Dot-Graph","DualAxis(Bar-Line)","DualAxis(Area-Line)","Donut-Single","Donut-Double","Funnel","Funnel(INV)","Line","Line(Dashed)","Line(Simple)","Line(Simple-R)","Line(Smooth)","Line(Std)","Mekko","Overlaid(Bar-Line)","Overlaid(Bar-Dot)","Pie","Pie-Empty","Scatter","Scatter(Partial)","Scatter(Regression)","StackedArea","StackedBar(V)","StackedBar(H)","StackedBar(Percent)","StackedH(Percent)","Waterfall","Waterfall(GT)"};
//        String[] jqgraphIds={"5500","5501","5502","5503","5504","5505","5506","5508","5509","5510","5511","5512","5513","5514","5515","5516","5517","5518","5519","5520","5521","5522","5523","5524","5525","5526","5527","5528","5529","5530","5531","5532","5533","5534","5535"};
//
//        String advHtmlFileProps=session.getAttribute("advHtmlFileProps").toString();
        String i = detail.getNoOfViewLets();
        int viewlets = Integer.parseInt(i);
        Container container = onecontainer.getContainer();
        if (detail.getReptype().toString().equalsIgnoreCase("MsrTemplateGraph")) {
//         ArrayList parameterIds = (ArrayList) container.getParametersHashMap().get("Parameters");
//         if(parameterIds.size()>=viewlets){
            DataFacade facade = onecontainer.getFacade();
//          String viewbyId=(String)parameterIds.get(viewlets-1);
            String viewbyId = (String) detail.getParamId();
            if (facade.refreshconatinerMap != null && !facade.refreshconatinerMap.isEmpty() && viewbyId != "" && facade.refreshconatinerMap.containsKey(viewbyId)) {
                container = (Container) facade.refreshconatinerMap.get(viewbyId);
                container.setSessionContext(session, container);
            } //         }
            else {
                htmlVar.append(generateMsrTemplateData(onecontainer, detail));
                return htmlVar.toString();
            }
        }
//        Container container=onecontainer.getContainer();
        int grphId = 0;
        String grpId = String.valueOf(grphId);
        String reportId = container.getReportId();
        ReportTemplateBD reportBD = new ReportTemplateBD();
        String viewbyId = (String) container.getReportCollect().reportRowViewbyValues.get(0);
        htmlVar.append(reportBD.generateTemplateGraphHeader(grpId, reportId, detail, viewbyId));
//        onecontainer.setContainer(container);
//        htmlVar.append("<table style='margin-left: 10px;width:"+detail.getWidth()+"px;'><tr><td style='font-size:12pt;color:#000000;white-space:nowrap;'>").append(onecontainer.oneviewName).append("</td></tr></table>");
//        htmlVar.append("<table style='margin-left: 10px;width:"+detail.getWidth()+"px;'><tr><td style='font-size:12pt;color:#000000;white-space:nowrap;'>").append("&nbsp;").append("</td></tr></table>");
//        htmlVar.append("<table style='margin-left: 10px;width:480px;'><tr><td style='width:80%;'></td><td style='font-size:12pt;width:20%;'>").append("<a class='ui-icon ui-icon-image' style='text-decoration:none' onclick=\"changeGraphTypes('grphTypes');\" title='graphTypes' href='javascript:void(0)'></a>");
//        htmlVar.append("<div id='grphTypes' class='overlapDiv' style='width:120px;height:140px;display:none;overflow:auto;position: absolute; text-align: left; border: 1px solid rgb(0, 0, 0);'><table>");
//        for (int gqtype = 0; gqtype < jqgrapharray.length; gqtype++) {
//        htmlVar.append("<tr><td><a href='javascript:void(0)' onclick=\"buildJqGraph('"+jqgrapharray[gqtype]+"','"+grpId+"','"+jqgraphIds[gqtype]+"','"+reportId+"')\">"+jqgrapharray[gqtype]+"</a></td></tr>");
//                }
//        htmlVar.append("</table></div></td></tr></table>");

        htmlVar.append("<div id='Dashlets-" + i + "' class='ui-tabs ui-widget ui-widget-content ui-corner-all' style='border-bottom: medium hidden LightGrey ; border-left: medium hidden LightGrey ; border-right: medium hidden LightGrey ; border-color: LightGrey ; width: 100%; height: 90%; margin-left: 10px; margin-right: 10px;'>");

//          HashMap GraphClassesHashMap = (HashMap) session.getAttribute("GraphClassesHashMap");
        HashMap GraphSizesDtlsHashMap = (HashMap) session.getAttribute("GraphSizesDtlsHashMap");
//          try{
//         String result = reportBD.buildGraph(container, request, response, grpId);
//          }catch(Exception e){
//           logger.error("Exception:",e);
//          }
        ArrayList grpDetails = new ArrayList();
        HashMap GraphTypesHashMap = null;
        String[] ViewBys = null;
        PbGraphDisplay GraphDisplay = new PbGraphDisplay();
        GraphDisplay.setGraphSizesDtlsHashMap(GraphSizesDtlsHashMap);
        ProgenDataSet recordsRetObj = container.getRetObj();
        ArrayList measures = (ArrayList) container.getDisplayColumns();
        HashMap GraphHashMap = container.getGraphHashMap();
        HashMap singleGraphDetails = (HashMap) GraphHashMap.get(grpId);
        LinkedHashMap grphMeasMap = (LinkedHashMap) singleGraphDetails.get("graphMeasures");
        String grpMsrId = (String) measures.get(1);
//            
        if (grphMeasMap != null && !grphMeasMap.isEmpty()) {
            Set s1 = grphMeasMap.keySet();
            grpMsrId = (String) s1.toArray()[0];
        }
        int rowct = 0;
        ArrayList<String> sortCols = new ArrayList<String>();
        ArrayList<Integer> rowSequence;// = null;

        if (recordsRetObj.rowCount > 10) {
            rowct = 10;
        } else {
            rowct = recordsRetObj.rowCount;
        }
        char[] sortTypes = null;
        sortTypes = new char[]{'1'};
        sortCols.add(grpMsrId);
        rowSequence = recordsRetObj.findTopBottom(sortCols, sortTypes, rowct);
        recordsRetObj.setViewSequence(rowSequence);
        GraphDisplay.setCurrentDispRetObjRecords(recordsRetObj);
        GraphDisplay.setCurrentDispRecordsRetObjWithGT((PbReturnObject) recordsRetObj);
        GraphDisplay.setAllDispRecordsRetObj(recordsRetObj);
        GraphDisplay.setNoOfDays(container.getNoOfDays());
        GraphDisplay.setColumnAverages(recordsRetObj.getColumnAverages());
        GraphDisplay.setColumnGrandTotals(recordsRetObj.getColumnGrandTotals());
        GraphDisplay.setColumnOverAllMinimums(recordsRetObj.getColumnOverAllMinimums());
        GraphDisplay.setColumnOverAllMaximums(recordsRetObj.getColumnOverAllMaximums());
        ArrayList viewByColNames = container.getViewByColNames();
        ArrayList viewByElementId = container.getViewByElementIds();
        ViewBys = (String[]) viewByElementId.toArray(new String[0]);
        for (int viewIndex = 0; viewIndex < ViewBys.length; viewIndex++) {
            if (ViewBys[viewIndex].equalsIgnoreCase("Time")) {
                ViewBys[viewIndex] = ViewBys[viewIndex];
            } else {
                if (ViewBys[viewIndex].contains("A_")) {
                    ViewBys[viewIndex] = ViewBys[viewIndex];
                } else {
                    ViewBys[viewIndex] = "A_" + ViewBys[viewIndex];
                }
            }
        }
        GraphDisplay.setViewByColNames((String[]) viewByColNames.toArray(new String[0]));
        GraphDisplay.setViewByElementIds(ViewBys);
        GraphDisplay.setCtxPath(request.getContextPath());
        GraphDisplay.setSession(request.getSession(false));
        GraphDisplay.setResponse(response);
        try {
            GraphDisplay.setOut(response.getWriter());
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }
        GraphDisplay.setReportId(reportId);
        HashMap[] graphMapDetails = container.getGraphMapDetails();

        GraphDisplay.setGraphHashMap(container.getGraphHashMap());
        GraphDisplay.setGraphMapDetails(graphMapDetails);
        GraphDisplay.setSortColumns(container.getSortColumns());
        grpDetails = GraphDisplay.getGraphHeaders(container.getNoOfDays(), container);

        container.setGraphHashMap(GraphDisplay.getGraphHashMap());
        JqplotGraphProperty graphproperty = new JqplotGraphProperty();
        HashMap singleGraphDetails1 = (HashMap) container.getGraphHashMap().get(grpId);
        String jqgraphId = "24";
        String jqGraphTypeName = "Bar-Vertical";
        String selectgraph = "jq";
        if (container.getGraphHashMap().containsKey("jqdetailmap" + grpId) && container.getGraphHashMap().get("jqdetailmap" + grpId) != null) {
            HashMap jqgraphdetails = new HashMap();
            HashMap jqdetailmap = new HashMap();

            jqdetailmap = (HashMap) container.getGraphHashMap().get("jqdetailmap" + grpId);
            jqgraphdetails = (HashMap) jqdetailmap.get(grpId);
            if (jqgraphdetails != null && !jqgraphdetails.isEmpty() && jqgraphdetails.containsKey("graphTypeid")) {
                jqgraphId = (String) jqgraphdetails.get("graphTypeid");
                jqGraphTypeName = (String) jqgraphdetails.get("graphTypename");
                if (onecontainer.getOneViewType().equalsIgnoreCase("Measure Based Business Template")) {
                    if (detail.getGraphType() != null && detail.getGraphType() != "") {
                        jqGraphTypeName = detail.getGraphType();
                    } else {
                        jqGraphTypeName = "Bar-Vertical";
                    }
                }
//                
            }
        }
        ProGenJqPlotChartTypes jqplotcontainer = new ProGenJqPlotChartTypes();
        ProgenJqplotGraphBD jqplotgraphbd = new ProgenJqplotGraphBD();
        jqplotgraphbd.JqplotGraphProperty = graphproperty;
        String chartId = "chart-" + i; //+grpNo+"_"+reportId+"_"
        jqplotgraphbd.chartId = chartId;
        jqplotgraphbd.tickId = i;
        htmlVar.append("<div id='" + chartId + "' style=\"width:" + (detail.getWidth() - 20) + "px;height:" + (detail.getHeight() - 20) + "px;\" align=\"center\" ></div><br>"); //olapGraph('"+name+"','"+reportId+"','"+graphId+"','"+grpNo+"')
        htmlVar.append("<script>");
        jqplotgraphbd.tablecols = null;
        jqplotgraphbd.setIsFromOneView(true);
        htmlVar.append(jqplotgraphbd.prepareJqplotGraph(reportId, jqgraphId, jqGraphTypeName, jqplotcontainer, request, null, selectgraph, grpId));
        htmlVar.append("</script>");
        jqplotgraphbd.setIsFromOneView(false);
        htmlVar.append("</div>");
        return htmlVar.toString();
    }

    public String generateTemplateMeasure(OnceViewContainer onecontainer, OneViewLetDetails detail) {
        StringBuilder finalStringVal = new StringBuilder();
        int i = Integer.parseInt(detail.getNoOfViewLets());
        Container container = onecontainer.getContainer();
        PbReturnObject retObj = container.getMsrRetObj();
        HashMap tableHM = container.getTableHashMap();
        ArrayList<String> rowviewby = (ArrayList<String>) container.getReportCollect().reportRowViewbyValues;
//     List measures = (List) tableHM.get("Measures");
//     List measNames = (List) tableHM.get("MeasuresNames");
        ArrayList measures = (ArrayList) container.getDisplayColumns();
        ArrayList measureNames = (ArrayList) container.getDisplayLabels();
        int size = measures.size();
        if (i < size) {
            String msrId = (String) measures.get(i);
            DashboardTemplateDAO daofrmsrattr = new DashboardTemplateDAO();
            HashMap<String, String> modifyMeasureAttr = daofrmsrattr.getModifyMeasureattr(msrId.replace("A_", ""));
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
            } else {
                detail.setRoundVal("2");
//            detail.setMeasType("Standard");
                detail.setFormatVal("");
            }
        }
        String suffixValue = "";
        String prefixvalue = "";
        if (i < size) {
            finalStringVal.append("<table style='margin-left: 10px;width:" + detail.getWidth() + "px;'><tr><td style='font-size:12pt;color:#000000;white-space:nowrap;'>").append(measureNames.get(i)).append("</td></tr></table>");
        }
        finalStringVal.append("<div id='Dashlets-" + i + "' class='ui-tabs ui-widget ui-widget-content ui-corner-all' style='border-bottom: medium hidden LightGrey ; border-left: medium hidden LightGrey ; border-right: medium hidden LightGrey ; border-color: LightGrey ; width: 100%; height: 90%; margin-left: 10px; margin-right: 10px;'>");
        if (i < size) {
            String msid = (String) measures.get(i);
            finalStringVal.append("<table><tr><td><a href='javascript:void(0)' onclick=\"changeGraphColumn('" + msid.replace("A_", "") + "','" + detail.getOneviewId() + "','" + (String) rowviewby.get(0) + "')\"><span style='color:;font-size:18pt;'>");
            if (retObj.getFieldValueString(0, msid) != "" && retObj.getFieldValueString(0, msid) != null && !retObj.getFieldValueString(0, msid).equalsIgnoreCase("null")) {
                String formattedData = "";
                try {
                    BigDecimal currVal = new BigDecimal(retObj.getFieldValueString(0, msid));
                    suffixValue = detail.getFormatVal();
                    formattedData = NumberFormatter.getModifiedNumberFormat(currVal, detail.getFormatVal(), Integer.parseInt(detail.getRoundVal()));
                    if (detail.getFormatVal() != null && !detail.getFormatVal().equalsIgnoreCase("")) {
                        BigDecimal bd = new BigDecimal("1");
                        BigDecimal data;
                        if (formattedData.contains(",")) {
                            data = new BigDecimal(String.valueOf(formattedData).replaceAll(",", ""));
                        } else {
                            data = new BigDecimal(String.valueOf(formattedData));
                        }
                        // 
                        if (data.compareTo(bd) == 0 || data.compareTo(bd) == 1) {
                            suffixValue = detail.getFormatVal();
                            formattedData = NumberFormatter.getModifiedNumberFormat(currVal, detail.getFormatVal(), Integer.parseInt(detail.getRoundVal()));
                        } else {
                            suffixValue = "";
                            formattedData = NumberFormatter.getModifiedNumberFormat(currVal, null, Integer.parseInt(detail.getRoundVal()));
                        }
                    }
                } catch (Exception e) {
                    formattedData = retObj.getFieldValueString(0, msid);
                }

                finalStringVal.append(detail.getPrefixValue() + formattedData);
//        String value;
//        if(detail.getFormatVal()!=null)
//            value = NumberFormatter.getModifiedNumberFormat(currVal, detail.getFormatVal(), Integer.parseInt(detail.getRoundVal()));
//         else
//            value=retObj.getFieldValueString(0, msid);
//        finalStringVal.append(detail.getPrefixValue()+value);
//        finalStringVal.append(retObj.getModifiedNumber(currVal));
            }
            finalStringVal.append("</span>");
            if (suffixValue != null && !suffixValue.equalsIgnoreCase("")) {
                finalStringVal.append("<span style='font-size:9pt;'>" + suffixValue + "</span>");
            }
            finalStringVal.append("</a></td></tr></table>");
        } else {
            finalStringVal.append("<div id='template-" + i + "' style='color:#d0d0d0;font-size:25pt;position :absolute;font-family:verdana;width:" + detail.getWidth() + "px; height:" + (detail.getHeight() - 10) + "px;'>");
            finalStringVal.append("<p align='center'>Template</p></div>");
        }
        finalStringVal.append("</div>");
        return finalStringVal.toString();

    }

    public void generateTemplateMeasureReturnObject(Container container, String userId) {
        PbReportQuery repQuery = new PbReportQuery();
        PbReturnObject retObj = null;
        QueryExecutor qryExec = new QueryExecutor();
        PbReportCollection collect = container.getReportCollect();
//        
//        
        repQuery.setRowViewbyCols(collect.reportRowViewbyValues);
        repQuery.setColViewbyCols(collect.reportColViewbyValues);
        repQuery.setParamValue(container.getReportCollect().reportParametersValues);
        repQuery.setQryColumns(collect.reportQryElementIds);
        repQuery.setColAggration(collect.reportQryAggregations);

        repQuery.setTimeDetails(collect.timeDetailsArray);
        repQuery.isKpi = true;
        repQuery.setReportId(collect.reportId);
        repQuery.setBizRoles(collect.reportBizRoles[0]);
        repQuery.setUserId(userId);
        try {
            String query = repQuery.generateViewByQry();
            //
            retObj = (PbReturnObject) qryExec.executeQuery(collect, query, false);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
//        return retObj;
        container.setMsrRetObj(retObj);
    }

    public String changeViewbyBusinessTemplateData(HttpServletRequest request, HttpServletResponse response, String oneviewId, String rowviewbyId) {
        HttpSession session = request.getSession(false);
        String advHtmlFileProps = (String) request.getSession(false).getAttribute("advHtmlFileProps");
        String fileName = request.getSession(false).getAttribute("tempFileName").toString();
        OnceViewContainer onecontainer = null;
        StringBuilder finalStringVal = new StringBuilder();
        try {
            FileInputStream fis2 = new FileInputStream(advHtmlFileProps + "/" + fileName);
            ObjectInputStream ois2 = new ObjectInputStream(fis2);
            onecontainer = (OnceViewContainer) ois2.readObject();
            ois2.close();
            int grphId = 0;
            String grpId = String.valueOf(grphId);
            Container prevContainer = onecontainer.getContainer();
            HashMap GraphHashMap = prevContainer.getGraphHashMap();
            HashMap singleGraphDetails = (HashMap) GraphHashMap.get(grpId);
            LinkedHashMap grphMeasMap = (LinkedHashMap) singleGraphDetails.get("graphMeasures");
            HashMap jqgraphdetails = new HashMap();
            HashMap jqdetailmap = new HashMap();
            if (prevContainer.getGraphHashMap().containsKey("jqdetailmap" + grpId) && prevContainer.getGraphHashMap().get("jqdetailmap" + grpId) != null) {
                jqdetailmap = (HashMap) prevContainer.getGraphHashMap().get("jqdetailmap" + grpId);
                jqgraphdetails = (HashMap) jqdetailmap.get(grpId);
//              
            }
            DataFacade facade = onecontainer.getFacade();
            if (facade.refreshconatinerMap != null && !facade.refreshconatinerMap.isEmpty() && facade.refreshconatinerMap.containsKey(rowviewbyId)) {
                Container container = (Container) facade.refreshconatinerMap.get(rowviewbyId);
                HashMap GraphHashMap1 = container.getGraphHashMap();
                HashMap singleGraphDetails1 = (HashMap) GraphHashMap1.get(grpId);
                singleGraphDetails1.put("graphMeasures", grphMeasMap);
                GraphHashMap1.put(grpId, singleGraphDetails1);
                if (jqdetailmap != null && !jqdetailmap.isEmpty()) {
                    GraphHashMap1.put("jqdetailmap" + grpId, jqdetailmap);
                }
                container.setGraphHashMap(GraphHashMap1);
                container.setSessionContext(session, container);
                onecontainer.setContainer(container);
                finalStringVal.append(generateBusinessTemplateView(request, response, onecontainer));
                FileOutputStream fos = new FileOutputStream(advHtmlFileProps + "/" + fileName);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(onecontainer);
                oos.flush();
                oos.close();
            } else {
                finalStringVal.append(generateBusinessTemplateView(request, response, onecontainer));
            }
        } catch (Exception e) {
            logger.error("Exception:", e);
        }


        return finalStringVal.toString();
    }

    public String changeGraphType(HttpServletRequest request, HttpServletResponse response, OnceViewContainer onecontainer, OneViewLetDetails detail) {
        StringBuilder htmlVar = new StringBuilder();
        HttpSession session = request.getSession(false);
        String grpidfrmrep = request.getParameter("gid");
        String jqgraphId = request.getParameter("jqgraphId");
        String jqGraphTypeName = request.getParameter("jqGraphTypeName");
        String reportId = request.getParameter("REPORTID");
        String viewbyId = request.getParameter("viewbyId");
        Container container = onecontainer.getContainer();
        if (onecontainer.getOneViewType().equalsIgnoreCase("Measure Based Business Template")) {
            DataFacade facade = onecontainer.getFacade();
            if (facade.refreshconatinerMap != null && !facade.refreshconatinerMap.isEmpty() && facade.refreshconatinerMap.containsKey(viewbyId)) {
                container = (Container) facade.refreshconatinerMap.get(viewbyId);
                container.setSessionContext(session, container);
            }
        }
        HashMap GraphHashMap = container.getGraphHashMap();
        HashMap jqgraphdetails = new HashMap();
        HashMap jqdetailmap = new HashMap();
        jqgraphdetails.put("grpidfrmrep", grpidfrmrep);
        jqgraphdetails.put("graphTypeid", jqgraphId);
        jqgraphdetails.put("graphTypename", jqGraphTypeName);
        jqgraphdetails.put("selectedgraphtype", "jq");
        jqdetailmap.put(grpidfrmrep, jqgraphdetails);
        container.getGraphHashMap().put("jqdetailmap" + grpidfrmrep, jqdetailmap);
        ReportTemplateBD reportBD = new ReportTemplateBD();
        detail.setGraphType(jqGraphTypeName);
//     String viewbyId=(String)container.getReportCollect().reportRowViewbyValues.get(0);
        htmlVar.append(reportBD.generateTemplateGraphHeader(grpidfrmrep, reportId, detail, viewbyId));
        String i = detail.getNoOfViewLets();
        htmlVar.append("<div id='Dashlets-" + i + "' class='ui-tabs ui-widget ui-widget-content ui-corner-all' style='border-bottom: medium hidden LightGrey ; border-left: medium hidden LightGrey ; border-right: medium hidden LightGrey ; border-color: LightGrey ; width: 100%; height: 90%; margin-left: 10px; margin-right: 10px;'>");
        JqplotGraphProperty graphproperty = new JqplotGraphProperty();
        ProGenJqPlotChartTypes jqplotcontainer = new ProGenJqPlotChartTypes();
        ProgenJqplotGraphBD jqplotgraphbd = new ProgenJqplotGraphBD();
        jqplotgraphbd.JqplotGraphProperty = graphproperty;
        String chartId = "chart-" + i; //+grpNo+"_"+reportId+"_"
        jqplotgraphbd.chartId = chartId;
        jqplotgraphbd.tickId = i;
//            String chartId = "chart-" + grpidfrmrep;
//            jqplotgraphbd.chartId = chartId;
//            jqplotgraphbd.tickId = grpidfrmrep;
        htmlVar.append("<div id='" + chartId + "' style=\"width:" + (detail.getWidth() - 20) + "px;height:" + (detail.getHeight() - 20) + "px;\" align=\"center\" ></div><br>"); //olapGraph('"+name+"','"+reportId+"','"+graphId+"','"+grpNo+"')
        htmlVar.append("<script>");
        jqplotgraphbd.tablecols = null;
        jqplotgraphbd.setIsFromOneView(true);
        htmlVar.append(jqplotgraphbd.prepareJqplotGraph(reportId, jqgraphId, jqGraphTypeName, jqplotcontainer, request, null, "jq", grpidfrmrep));
        htmlVar.append("</script>");
        jqplotgraphbd.setIsFromOneView(false);
        htmlVar.append("</div>");

        return htmlVar.toString();
    }

    public void generateMsrTemplateReturnObject(String oneviewId, OnceViewContainer onecContainer, Container container) {
        String result = null;
        HashMap tableHM = container.getTableHashMap();
        ArrayList<String> queryCols = new ArrayList<String>();
        ArrayList<String> queryAggs = new ArrayList<String>();
        List measures = (List) tableHM.get("Measures");
        List measNames = (List) tableHM.get("MeasuresNames");
        List<String> elementIds = new ArrayList<String>();
        for (int i = 0; i < measures.size(); i++) {
            elementIds.add(((String) measures.get(i)).replace("A_", ""));
        }
        PbReturnObject pbretObj = null;
        PbReportCollection collect = container.getReportCollect();
        DashboardViewerDAO dao = new DashboardViewerDAO();
        List<KPIElement> kpiElements = dao.getKPIElements(elementIds, new HashMap<String, String>());
        onecContainer.setKpiElements(kpiElements);

        if (kpiElements != null) {
            for (KPIElement elem : kpiElements) {
                if (elem != null) {
                    queryCols.add(elem.getElementId());
                    queryAggs.add(elem.getAggregationType());
                }
            }
        }
        try {
            PbReportQuery repQuery = new PbReportQuery();
            repQuery.setRowViewbyCols(new ArrayList());
            repQuery.setParamValue(collect.reportParametersValues);//Added by k
            HashMap<String, List> inMap = (HashMap<String, List>) collect.operatorFilters.get("IN");
            if (inMap != null) {
                repQuery.setInMap(inMap);
            }

            repQuery.setColViewbyCols(new ArrayList());
            repQuery.setQryColumns(queryCols);
            repQuery.setColAggration(queryAggs);
            repQuery.setTimeDetails(collect.timeDetailsArray);
            repQuery.setDefaultMeasure(String.valueOf(queryCols.get(0)));
            repQuery.setDefaultMeasureSumm(String.valueOf(queryAggs.get(0)));
            repQuery.isKpi = true;
            repQuery.setReportId(collect.reportId);
            repQuery.setBizRoles(collect.reportBizRoles[0]);
            repQuery.setUserId(userId);

            MultiPeriodKPI multiPeriodKPI = new MultiPeriodKPI();
            ArrayList<String> multiperiods = new ArrayList<String>();
            multiperiods.add("Year");
            multiperiods.add("Qtr");
            multiperiods.add("Month");
            PbReturnObject kpiMulitiObject = new PbReturnObject();
            ArrayList tempTimeDetailsArrayList = (ArrayList) collect.timeDetailsArray.clone();
            for (String str : multiperiods) {
                tempTimeDetailsArrayList.set(3, str);
                repQuery.setTimeDetails(tempTimeDetailsArrayList);
                kpiMulitiObject = repQuery.getPbReturnObjectWithFlag(String.valueOf(queryCols.get(0)));
                HashMap vals111 = new HashMap();
                vals111 = repQuery.getTimememdetails();
                collect.setTimememdetails(vals111);
                container.setTimememdetails(vals111);
                if (str.equalsIgnoreCase("Year")) {
                    multiPeriodKPI.setYearObject(kpiMulitiObject);
                } else if (str.equalsIgnoreCase("Qtr")) {
                    multiPeriodKPI.setQuarterObject(kpiMulitiObject);
                } else if (str.equalsIgnoreCase("Month")) {
                    multiPeriodKPI.setMonthObject(kpiMulitiObject);
                }
            }

            container.setMultiPeriodKPI(multiPeriodKPI);
            repQuery.setTimeDetails(collect.timeDetailsArray);
            pbretObj = repQuery.getPbReturnObject(String.valueOf(queryCols.get(0)));
            container.setKpiRetObj(pbretObj);
//                    container.setSqlStr(repQuery.getFinalNormalQuery());
//                     result= generateMesureTableData(onviewId, container,onecContainer);
        } catch (Exception e) {
            logger.error("Exception:", e);
        }
//         return  result;
    }

    public String generateMesureTableData(String oneviewId, OnceViewContainer oneContainer, OneViewLetDetails detail) {
        StringBuilder finalhtmlVal = new StringBuilder();
        Container container = oneContainer.getContainer();
        String[] headers = {"Current", "Prior", "Change", "Change%", "MTD", "QTD", "YTD"};
        List measures = (List) container.getTableHashMap().get("Measures");
        List MeasuresNames = (List) container.getTableHashMap().get("MeasuresNames");
        String eId = ((String) measures.get(0)).replace("A_", "");
        if (oneContainer.getElementIds() != null && oneContainer.getElementIds() != "") {
            eId = (String) oneContainer.getElementIds().replace("A_", "");
        }
        List<String> elementIds = new ArrayList<String>();
        elementIds.add(eId);

//       for(int i=0;i<measures.size();i++)
//          elementIds.add(((String)measures.get(i)).replace("A_",""));
        ArrayList<String> QueryCols = new ArrayList<String>();
        DashboardViewerDAO dao = new DashboardViewerDAO();
        List<KPIElement> kpiElements = dao.getKPIElements(elementIds, new HashMap<String, String>());
        PbReturnObject retObj = container.getKpiRetObj();
        if (kpiElements != null) {
            for (KPIElement elem : kpiElements) {
                if (elem.getElementName() != null) {
                    QueryCols.add(elem.getElementId());
                }

            }
        }
        int i = Integer.parseInt(detail.getNoOfViewLets());
//    finalhtmlVal.append("<table style='margin-left: 10px;'><tr><td style='font-size:12pt;color:#000000;white-space:nowrap;'>").append((String)MeasuresNames.get(0)).append("</td><tr></table>");
        finalhtmlVal.append("<table style='margin-left: 10px;' id='tablesorter' class='tablesorter' width='99%;height:auto' cellspacing='1' cellpadding='1'>");
        finalhtmlVal.append("<thead><tr>");
        for (int j = 0; j < headers.length; j++) {
            finalhtmlVal.append("<th>").append(headers[j]).append("</th>");
        }
        finalhtmlVal.append("</tr></thead><tbody>");
        String currVal = "", priorVal = "", changeVal = "", changePerCentVal = "", mtdVal = "", qtdVal = "", ytdVal = "";
        if (QueryCols.size() == 4) {
            if (!retObj.getFieldValueString(0, ("A_" + QueryCols.get(0)).toString()).equalsIgnoreCase("")) {
                currVal = retObj.getFieldValueString(0, ("A_" + QueryCols.get(0)));
            }
            if (retObj.getFieldValueString(0, ("A_" + QueryCols.get(1)).toString()) != null && !retObj.getFieldValueString(0, ("A_" + QueryCols.get(1)).toString()).equalsIgnoreCase("")) {
                priorVal = retObj.getFieldValueString(0, ("A_" + QueryCols.get(1)).toString());
            }
            if (retObj.getFieldValueString(0, ("A_" + QueryCols.get(2)).toString()) != null && !retObj.getFieldValueString(0, ("A_" + QueryCols.get(2)).toString()).equalsIgnoreCase("")) {
                changeVal = retObj.getFieldValueString(0, ("A_" + QueryCols.get(1)).toString());
            }
            if (retObj.getFieldValueString(0, ("A_" + QueryCols.get(3)).toString()) != null && !retObj.getFieldValueString(0, ("A_" + QueryCols.get(3)).toString()).equalsIgnoreCase("")) {
                changePerCentVal = retObj.getFieldValueString(0, ("A_" + QueryCols.get(3)).toString());
            }
        } else {
            if (!retObj.getFieldValueString(0, ("A_" + QueryCols.get(0)).toString()).equalsIgnoreCase("")) {
                currVal = retObj.getFieldValueString(0, ("A_" + QueryCols.get(0)));
            }
        }
        if ((container.getMultiPeriodKPI().getMonthObject().getFieldValueString(0, ("A_" + QueryCols.get(0)).toString())) != null && !("".equalsIgnoreCase(container.getMultiPeriodKPI().getMonthObject().getFieldValueString(0, ("A_" + QueryCols.get(0)).toString())))) {
            mtdVal = container.getMultiPeriodKPI().getMonthObject().getFieldValueString(0, ("A_" + QueryCols.get(0)));
        }
        if ((container.getMultiPeriodKPI().getQuarterObject().getFieldValueString(0, ("A_" + QueryCols.get(0)).toString())) != null && !("".equalsIgnoreCase(container.getMultiPeriodKPI().getQuarterObject().getFieldValueString(0, ("A_" + QueryCols.get(0)).toString())))) {
            qtdVal = (container.getMultiPeriodKPI().getQuarterObject().getFieldValueString(0, ("A_" + QueryCols.get(0)).toString()));
        }
        if ((container.getMultiPeriodKPI().getYearObject().getFieldValueString(0, ("A_" + QueryCols.get(0)).toString())) != null && !("".equalsIgnoreCase(container.getMultiPeriodKPI().getYearObject().getFieldValueString(0, ("A_" + QueryCols.get(0)).toString())))) {
            ytdVal = (container.getMultiPeriodKPI().getYearObject().getFieldValueString(0, ("A_" + QueryCols.get(0)).toString()));
        }
        if (currVal != null && currVal != "") {
            finalhtmlVal.append("<td>").append(retObj.getModifiedNumber(new BigDecimal(currVal))).append("</td>");
        } else {
            finalhtmlVal.append("<td>").append(currVal).append("</td>");
        }
        if (priorVal != null && priorVal != "") {
            finalhtmlVal.append("<td>").append(retObj.getModifiedNumber(new BigDecimal(priorVal))).append("</td>");
        } else {
            finalhtmlVal.append("<td>").append(priorVal).append("</td>");
        }
        if (changeVal != null && changeVal != "") {
            finalhtmlVal.append("<td>").append(retObj.getModifiedNumber(new BigDecimal(changeVal))).append("</td>");
        } else {
            finalhtmlVal.append("<td>").append(changeVal).append("</td>");
        }
        if (changePerCentVal != null && changePerCentVal != "") {
            finalhtmlVal.append("<td>").append(retObj.getModifiedNumber(new BigDecimal(changePerCentVal))).append("</td>");
        } else {
            finalhtmlVal.append("<td>").append(changePerCentVal).append("</td>");
        }
        if (mtdVal != null && mtdVal != "") {
            finalhtmlVal.append("<td>").append(retObj.getModifiedNumber(new BigDecimal(mtdVal))).append("</td>");
        } else {
            finalhtmlVal.append("<td>").append(mtdVal).append("</td>");
        }
        if (qtdVal != null && qtdVal != "") {
            finalhtmlVal.append("<td>").append(retObj.getModifiedNumber(new BigDecimal(qtdVal))).append("</td>");
        } else {
            finalhtmlVal.append("<td>").append(qtdVal).append("</td>");
        }
        if (ytdVal != null && ytdVal != "") {
            finalhtmlVal.append("<td>").append(retObj.getModifiedNumber(new BigDecimal(ytdVal))).append("</td>");
        } else {
            finalhtmlVal.append("<td>").append(ytdVal).append("</td>");
        }
        finalhtmlVal.append("</tbody></table>");


        return finalhtmlVal.toString();
    }

    public String generateMsrTemplateData(OnceViewContainer onecontainer, OneViewLetDetails detail) {
        StringBuilder finalStringVal = new StringBuilder();
        int i = Integer.parseInt(detail.getNoOfViewLets());
//      finalStringVal.append("<div id='" + i + "' style='width:" + detail.getWidth() + "px; height:" + detail.getHeight() + "px;float:left; padding:5px;'>");
        finalStringVal.append("<div id='Dashlets-" + i + "' class='ui-tabs ui-widget ui-widget-content ui-corner-all' style='border-bottom: medium hidden LightGrey ; border-left: medium hidden LightGrey ; border-right: medium hidden LightGrey ; border-color: LightGrey ; width: 100%; height: 90%; margin-left: 10px; margin-right: 10px;'>");
        finalStringVal.append("<div id='template-" + i + "' style='color:#d0d0d0;font-size:25pt;position :absolute;font-family:verdana;width:" + detail.getWidth() + "px; height:" + (detail.getHeight() - 10) + "px;'>");
        finalStringVal.append("<p align='center'>Template</p></div>");
        finalStringVal.append("</div>");
//      finalStringVal.append("</div>");
        return finalStringVal.toString();
    }

    public String MeasureChangeInMsrBsedTemplate(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, String onviewId, OnceViewContainer onecontainer) {
        String result = "";
        String fileName = request.getSession(false).getAttribute("tempFileName").toString();
        try {
            HttpSession session = request.getSession(false);
            String msrId = request.getParameter("grpColumns");
            onecontainer.setElementIds("A_" + msrId);
            ReportViewerAction viewerAction = new ReportViewerAction();
//               viewerAction.graphColumnChanges(mapping, form, request, response);
            Container container = onecontainer.getContainer();
            DataFacade facade = onecontainer.getFacade();
            if (facade.refreshconatinerMap != null) {
                Iterator<String> itr = facade.refreshconatinerMap.keySet().iterator();
                while (itr.hasNext()) {
                    String key = itr.next();
                    container = (Container) facade.refreshconatinerMap.get(key);
                    container.setSessionContext(session, container);
                    viewerAction.graphColumnChanges(mapping, form, request, response);
                }
            }
            onecontainer.setContainer(container);
            result = this.generateBusinessTemplateView(request, response, onecontainer);
            String advHtmlFileProps = (String) request.getSession(false).getAttribute("advHtmlFileProps");
            FileOutputStream fos = new FileOutputStream(advHtmlFileProps + "/" + fileName);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(onecontainer);
            oos.flush();
            oos.close();
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        return result;
    }
//sandeep

    public void oneviewdatetoggle(HttpServletRequest request, HttpServletResponse response, OnceViewContainer onecontainer1, OnceViewContainer onecontainer, OneViewLetDetails detail, String oneviewID) throws FileNotFoundException, IOException, SQLException, Exception {
        Container container = null;
        String reptype = detail.getReptype();
        ArrayList<String> timeInfo = null;
        HttpSession session = request.getSession(false);
        String userId = String.valueOf(session.getAttribute("USERID"));
        if (reptype != null && reptype.equalsIgnoreCase("measures")) {
            String isfirsttime = "toggledatemeasure";
            session.setAttribute("isfirsttime", isfirsttime);
            timeInfo = (ArrayList<String>) onecontainer.timedetails;
            String priodType = timeInfo.get(3).toString();
            String compareWith = timeInfo.get(4).toString();
            PbTimeRanges timeRanges = new PbTimeRanges();
            String date = timeInfo.get(2).toString();
            timeRanges.elementID = detail.getRepId();
            if (timeInfo != null && timeInfo.get(1).toString().equalsIgnoreCase("PRG_STD")) {
//      String isfirsttime ="toggledate";
//      session.setAttribute("isfirsttime", isfirsttime);
                String isfirsttimecheck = (String) session.getAttribute("isfirstdatereport");
                if (isfirsttimecheck != null && isfirsttimecheck.equalsIgnoreCase("togglereport")) {
                    timeRanges.setRange((ArrayList) onecontainer.timedetails);
                    onecontainer.timedetails.set(1, "PRG_STD");
                    onecontainer.timedetails.set(2, timeInfo.get(2).toString());
                    onecontainer.timedetails.set(3, "Month");
                    onecontainer.timedetails.set(4, "Last Period");
//        onecontainer.timedetails.remove(5);
                } else {
                    timeRanges.setRange(priodType, compareWith, date);
                    onecontainer.timedetails.set(1, "PRG_DATE_RANGE");
                    onecontainer.timedetails.set(2, timeRanges.st_d.length() >= 10 ? timeRanges.st_d.substring(0, 10) : timeRanges.st_d);
                    onecontainer.timedetails.set(3, timeRanges.ed_d.length() >= 10 ? timeRanges.ed_d.substring(0, 10) : timeRanges.ed_d);
                    onecontainer.timedetails.set(4, timeRanges.p_st_d.length() >= 10 ? timeRanges.p_st_d.substring(0, 10) : timeRanges.p_st_d);
                    onecontainer.timedetails.add(5, timeRanges.p_ed_d.length() >= 10 ? timeRanges.p_ed_d.substring(0, 10) : timeRanges.p_ed_d);
                }
            } else {
                String isfirsttimecheck = (String) session.getAttribute("isfirstdatereport");
                if (isfirsttimecheck != null && isfirsttimecheck.equalsIgnoreCase("togglereport")) {
                    timeRanges.setRange(priodType, compareWith, date);
                    onecontainer.timedetails.set(1, "PRG_DATE_RANGE");
                    onecontainer.timedetails.set(2, timeInfo.get(2).toString());
                    onecontainer.timedetails.set(3, timeInfo.get(3).toString());
                    onecontainer.timedetails.set(4, timeInfo.get(4).toString());
                    onecontainer.timedetails.add(5, timeInfo.get(5).toString());
                } else {
                    timeRanges.setRange((ArrayList) onecontainer.timedetails);
                    onecontainer.timedetails.set(1, "PRG_STD");
                    onecontainer.timedetails.set(2, timeInfo.get(3).toString());
                    onecontainer.timedetails.set(3, "Month");
                    onecontainer.timedetails.set(4, "Last Period");
                    onecontainer.timedetails.remove(5);
                }
            }
            String isfirstdatereport = "togglereport";
            timeInfo = (ArrayList<String>) onecontainer1.timedetails;
            session.setAttribute("isfirstdatereport", isfirstdatereport);
        } else {
//       String isfirsttime ="toggledate";
//      session.setAttribute("isfirsttime", isfirsttime);
//       String isfirstdatereport1 =(String) session.getAttribute("isfirstdatereport");
//       if(isfirstdatereport1!=null && isfirstdatereport1.equalsIgnoreCase("togglereport")){
//
//       }else{
            container = detail.getContainer();
            String reportId = detail.getRepId();
//         ArrayList<String> timeInfo=null;
            PbReportCollection collect = new PbReportCollection();
            if (container != null) {
                collect = container.getReportCollect();
            } else {

                HashMap map = null;
                PbReportViewerBD reportViewerBD = new PbReportViewerBD();
                request.setAttribute("REPORTID", reportId);
                reportViewerBD.prepareReport(reportId, userId, request, response,false);
                map = (HashMap) session.getAttribute("PROGENTABLES");

                container = (Container) map.get(reportId);
                collect = container.getReportCollect();
            }
            timeInfo = (ArrayList<String>) onecontainer.timedetails;
//   if(isfirstdatereport1!=null && isfirstdatereport1.equalsIgnoreCase("togglereport")){
//onecontainer.timedetails=timeInfo;
//onecontainer1.timedetails=timeInfo;
//       }else{
            String date1 = "";
            String date2 = "";
            String date3 = "";
            String date4 = "";
            PbReportViewerDAO dateformat = new PbReportViewerDAO();
            String vals = "";

            if (container.getTimememdetails().get("PR_DAY_DENOM") != null) {
                vals = container.getTimememdetails().get("PR_DAY_DENOM").toString();
            } else {
                vals = container.getTimememdetails().get("pr_day_denom").toString();
            }
            vals = vals.replace("[", "");
            vals = vals.replace("]", "");
            String[] vals1 = vals.split(",");
            if (vals1[0].toString().contains("-") && vals1[1].toString().contains("-") && vals1[2].toString().contains("-") && vals1[3].toString().contains("-")) {
                date1 = vals1[0].trim().substring(5, 7) + "/" + vals1[0].trim().substring(8, 10) + "/" + vals1[0].trim().substring(0, 4);
                date2 = vals1[1].trim().substring(5, 7) + "/" + vals1[1].trim().substring(8, 10) + "/" + vals1[1].trim().substring(0, 4);
                date3 = vals1[2].trim().substring(5, 7) + "/" + vals1[2].trim().substring(8, 10) + "/" + vals1[2].trim().substring(0, 4);
                date4 = vals1[3].trim().substring(5, 7) + "/" + vals1[3].trim().substring(8, 10) + "/" + vals1[3].trim().substring(0, 4);
            } else {
                date1 = vals1[0].trim().substring(0, 10);
                date2 = vals1[1].trim().substring(0, 10);
                date3 = vals1[2].trim().substring(0, 10);
                date4 = vals1[3].trim().substring(0, 10);

            }
            if (timeInfo != null && timeInfo.get(1).toString().equalsIgnoreCase("PRG_STD")) {
                String isfirsttimecheck = (String) session.getAttribute("isfirstdatereport");
                if (isfirsttimecheck != null && isfirsttimecheck.equalsIgnoreCase("togglereport")) {
                    String[] timemapkey = {"PRG_PERIOD_TYPE", "AS_OF_DATE", "PRG_COMPARE"};
                    String[] timeType = {"Month", timeInfo.get(2).toString(), "Last Period"};
                    collect.timeDetailsMap.clear();
                    for (int i = 0; i < timemapkey.length; i++) {
                        ArrayList timedetails = new ArrayList();
                        timedetails.add(timeType[i]);
                        timedetails.add("CBO_" + timemapkey[i]);
                        timedetails.add("DATE");
                        timedetails.add("1");
                        timedetails.add("1");
                        timedetails.add(timeType[i]);
                        timedetails.add(timemapkey[i]);
                        collect.timeDetailsMap.put(timemapkey[i], timedetails);
                    }
                } else {
                    String[] timemapkey = {"AS_OF_DATE1", "AS_OF_DATE2", "CMP_AS_OF_DATE1", "CMP_AS_OF_DATE2"};
                    String[] timeType = {"From DATE", "To DATE", "Comp From DATE", "Comp To DATE"};
                    String[] date = {date1, date2, date3, date4};
                    collect.timeDetailsMap.clear();
                    for (int i = 0; i < timemapkey.length; i++) {
                        ArrayList timedetails = new ArrayList();
                        timedetails.add(date[i].trim());
                        timedetails.add("CBO_" + timemapkey[i]);
                        timedetails.add(timeType[i]);
//                        timedetails.add("" + (i + 1) + "");
                        timedetails.add(String.valueOf((i + 1)));
                       timedetails.add(String.valueOf((i + 1)));
                        timedetails.add(date[i].trim());
                        timedetails.add(timemapkey[i]);
                        collect.timeDetailsMap.put(timemapkey[i], timedetails);
                    }
                }
                if (isfirsttimecheck != null && isfirsttimecheck.equalsIgnoreCase("togglereport")) {
                    collect.timeDetailsArray = (ArrayList) onecontainer.timedetails;
                } else {
                    collect.timeDetailsArray.set(1, "PRG_DATE_RANGE");
                    collect.timeDetailsArray.set(2, date1);
                    collect.timeDetailsArray.set(3, date2);
                    collect.timeDetailsArray.set(4, date3);
                    collect.timeDetailsArray.add(date4);
                }

            } else if (timeInfo != null && timeInfo.get(1).toString().equalsIgnoreCase("PRG_DATE_RANGE")) {
                String isfirsttimecheck = (String) session.getAttribute("isfirstdatereport");
                if (isfirsttimecheck != null && isfirsttimecheck.equalsIgnoreCase("togglereport")) {
                    String[] timemapkey = {"AS_OF_DATE1", "AS_OF_DATE2", "CMP_AS_OF_DATE1", "CMP_AS_OF_DATE2"};
                    String[] timeType = {"From DATE", "To DATE", "Comp From DATE", "Comp To DATE"};
                    String[] date = {timeInfo.get(2).toString(), timeInfo.get(3).toString(), timeInfo.get(4).toString(), timeInfo.get(5).toString()};
                    collect.timeDetailsMap.clear();
                    for (int i = 0; i < timemapkey.length; i++) {
                        ArrayList timedetails = new ArrayList();
                        timedetails.add(date[i].trim());
                        timedetails.add("CBO_" + timemapkey[i]);
                        timedetails.add(timeType[i]);
                       //                        timedetails.add("" + (i + 1) + "");
                        timedetails.add(String.valueOf((i + 1)));
                       timedetails.add(String.valueOf((i + 1)));
                        timedetails.add(date[i].trim());
                        timedetails.add(timemapkey[i]);
                        collect.timeDetailsMap.put(timemapkey[i], timedetails);
                    }
                } else {
                    String[] timemapkey = {"PRG_PERIOD_TYPE", "AS_OF_DATE", "PRG_COMPARE"};
                    String[] timeType = {"Month", date2.trim().substring(0, 10), "Last Period"};
                    collect.timeDetailsMap.clear();
                    for (int i = 0; i < timemapkey.length; i++) {
                        ArrayList timedetails = new ArrayList();
                        timedetails.add(timeType[i]);
                        timedetails.add("CBO_" + timemapkey[i]);
                        timedetails.add("DATE");
                        timedetails.add("1");
                        timedetails.add("1");
                        timedetails.add(timeType[i]);
                        timedetails.add(timemapkey[i]);
                        collect.timeDetailsMap.put(timemapkey[i], timedetails);
                    }
                }

                if (isfirsttimecheck != null && isfirsttimecheck.equalsIgnoreCase("togglereport")) {
                    collect.timeDetailsArray = (ArrayList) onecontainer.timedetails;
                } else {
                    collect.timeDetailsArray.set(1, "PRG_STD");
                    collect.timeDetailsArray.set(2, date2.trim().substring(0, 10));
                    collect.timeDetailsArray.set(3, "Month");
                    collect.timeDetailsArray.set(4, "Last Period");
//        collect.timeDetailsArray.remove(5);
                }

            }
            container.setReportCollect(collect);
            onecontainer.timedetails = collect.timeDetailsArray;
            onecontainer1.timedetails = collect.timeDetailsArray;
            String isfirsttime = "toggledatemeasure";
            session.setAttribute("isfirsttime", isfirsttime);
            String isfirstdatereport = "togglereport";
            timeInfo = (ArrayList<String>) onecontainer1.timedetails;
            session.setAttribute("isfirstdatereport", isfirstdatereport);
            HashMap map = new HashMap();
            map.put("timeInfo", timeInfo);
//                map=map1;
//                                session.setAttribute("PROGENTABLES", map);
            request.setAttribute("OneviewTiemDetails", onecontainer.timedetails);
            session.setAttribute("temearry", map);

//       }
        }
    }

    public void datefromtotoggle(HttpServletRequest request, HttpServletResponse response, OnceViewContainer onecontainer1, OnceViewContainer onecontainer, OneViewLetDetails detail, String oneviewID, String action, String duration, String CurrVal, String localfileName, String oneVersion, String cfvalue, String ctvalue) throws FileNotFoundException, IOException, SQLException, Exception {
//String oneVersion= onecontainer1.getOneViewVersion();
        String advHtmlFileProps = "";
        HttpSession session = request.getSession(false);
        String userId = String.valueOf(session.getAttribute("USERID"));
        String oneviewtypedate = (String) session.getAttribute("oneviewdatetype");
        if (action != null && action.equalsIgnoreCase("datetoggle")) {
            if (!detail.getReptype().toString().equalsIgnoreCase("template")) {
                oneviewdatetoggle(request, response, onecontainer1, onecontainer, detail, oneviewID);
            }

        } else {
            if (onecontainer1.timedetails.get(1).equalsIgnoreCase("PRG_STD")) {
            } else {
                if (!detail.getReptype().toString().equalsIgnoreCase("template")) {
                    if (duration == null ? "" != null : !duration.equals("")) {
                        String reptype = detail.getReptype();
                        if (reptype != null && reptype.equalsIgnoreCase("measures")) {
//                                              ArrayList<String> timeInfo=(ArrayList<String>) onecontainer1.timedetails;
//                                              if (timeInfo!=null && timeInfo.get(1).toString().equalsIgnoreCase("PRG_DATE_RANGE")) {
//String priodType="Month";
//  String compareWith="Last Period";
//PbTimeRanges timeRanges=new PbTimeRanges();
// String date=timeInfo.get(2).toString();
// timeRanges.elementID = detail.getRepId();
// timeRanges.setRange(priodType,compareWith,date);
// onecontainer.timedetails.set(1,"PRG_DATE_RANGE");
// onecontainer.timedetails.set(2, timeRanges.st_d.length() >= 10 ? timeRanges.st_d.substring(0,10):timeRanges.st_d);
//                        onecontainer.timedetails.set(3, timeRanges.ed_d.length() >= 10 ? timeRanges.ed_d.substring(0,10):timeRanges.ed_d);
//                        onecontainer.timedetails.set(4,timeRanges.p_st_d.length() >= 10 ? timeRanges.p_st_d.substring(0,10):timeRanges.p_st_d );
//                        onecontainer.timedetails.add(5,timeRanges.p_ed_d.length() >= 10 ? timeRanges.p_ed_d.substring(0,10):timeRanges.p_ed_d  );
//
//                                              }
                        } else {
                            ArrayList<String> timeInfo = null;
//  String reportId=request.getParameter("REPORTID");
//  HttpSession session = request.getSession(false);
                            HashMap map = new HashMap();
                            Container container = null;
                            HashMap<String, ArrayList<String>> timeDetailsMap = new LinkedHashMap<String, ArrayList<String>>();
//  map = (HashMap) session.getAttribute("PROGENTABLES");
//  container = (Container) map.get(reportId);
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
                                map = (HashMap) session.getAttribute("PROGENTABLES");

                                container = (Container) map.get(reportId);
                                collect = container.getReportCollect();
                            }
                            timeInfo = collect.timeDetailsArray;
                            String date1 = "";
                            String date2 = "";
                            String date3 = "";
                            String date4 = "";
                            PbReportViewerDAO dateformat = new PbReportViewerDAO();
                            String vals = "";

                            if (container.getTimememdetails().get("PR_DAY_DENOM") != null) {
                                vals = container.getTimememdetails().get("PR_DAY_DENOM").toString();
                            } else {
                                vals = container.getTimememdetails().get("pr_day_denom").toString();
                            }
                            vals = vals.replace("[", "");
                            vals = vals.replace("]", "");
                            String[] vals1 = vals.split(",");
                            if (vals1[0].toString().contains("-") && vals1[1].toString().contains("-") && vals1[2].toString().contains("-") && vals1[3].toString().contains("-")) {
                                date1 = vals1[0].trim().substring(5, 7) + "/" + vals1[0].trim().substring(8, 10) + "/" + vals1[0].trim().substring(0, 4);
                                date2 = vals1[1].trim().substring(5, 7) + "/" + vals1[1].trim().substring(8, 10) + "/" + vals1[1].trim().substring(0, 4);
                                if (oneviewtypedate != null && oneviewtypedate.equalsIgnoreCase("false")) {
                                    date3 = cfvalue;
                                    date3 = ctvalue;
                                } else {
                                    date3 = vals1[2].trim().substring(5, 7) + "/" + vals1[2].trim().substring(8, 10) + "/" + vals1[2].trim().substring(0, 4);
                                    date4 = vals1[3].trim().substring(5, 7) + "/" + vals1[3].trim().substring(8, 10) + "/" + vals1[3].trim().substring(0, 4);
                                }
                            } else {
                                date1 = vals1[0].trim().substring(0, 10);
                                date2 = vals1[1].trim().substring(0, 10);
                                if (oneviewtypedate != null && oneviewtypedate.equalsIgnoreCase("false")) {
                                    date3 = cfvalue;
                                    date3 = ctvalue;
                                } else {
                                    date3 = vals1[2].trim().substring(0, 10);
                                    date4 = vals1[3].trim().substring(0, 10);
                                }

                            }
                            String[] timemapkey = {"AS_OF_DATE1", "AS_OF_DATE2", "CMP_AS_OF_DATE1", "CMP_AS_OF_DATE2"};
                            String[] timeType = {"From DATE", "To DATE", "Comp From DATE", "Comp To DATE"};
                            String[] dateo = {CurrVal, duration, date3, date4};
                            collect.timeDetailsMap.clear();
                            for (int j = 0; j < timemapkey.length; j++) {
                                ArrayList timedetails = new ArrayList();
                                timedetails.add(dateo[j].trim());
                                timedetails.add("CBO_" + timemapkey[j]);
                                timedetails.add(timeType[j]);
//                                timedetails.add("" + (j + 1) + "");
                                timedetails.add(String.valueOf((j + 1)));
                                timedetails.add(String.valueOf((j + 1)));
                                timedetails.add(dateo[j].trim());
                                timedetails.add(timemapkey[j]);
                                collect.timeDetailsMap.put(timemapkey[j], timedetails);
                            }
                            collect.timeDetailsArray.set(1, "PRG_DATE_RANGE");
                            collect.timeDetailsArray.set(2, CurrVal);
                            collect.timeDetailsArray.set(3, duration);
                            collect.timeDetailsArray.set(4, date3);
                            int valof4th = collect.timeDetailsArray.size();
//  if(valof4th==5){
                            collect.timeDetailsArray.add(5, date4);
//                                        }
// else{
//    collect.timeDetailsArray.set(5,date4);
// }
                            container.setReportCollect(collect);
                            onecontainer1.timedetails = collect.timeDetailsArray;
                            onecontainer.timedetails = collect.timeDetailsArray;

                        }
                    }
                }
                request.setAttribute("OneviewTiemDetails", onecontainer.timedetails);
//  if(oneVersion.equalsIgnoreCase("1.1")){
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
//            FileOutputStream fos1 = new FileOutputStream(advHtmlFileProps+"/"+localfileName);
//            ObjectOutputStream oos1 = new ObjectOutputStream(fos1);
//            oos1.writeObject(onecontainer);
//            oos1.flush();
//            oos1.close();
            }
        }




    }
}
