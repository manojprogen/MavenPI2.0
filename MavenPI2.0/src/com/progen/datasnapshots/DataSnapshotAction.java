/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.datasnapshots;

import com.google.gson.Gson;
import com.progen.db.ProgenDataSet;
import com.progen.oneView.bd.OneViewBD;
import com.progen.report.bd.PbReportTableBD;
import com.progen.report.drill.DrillMaps;
import com.progen.reportdesigner.bd.DashboardTemplateBD;
import com.progen.reportdesigner.db.ReportTemplateDAO;
import com.progen.reportview.bd.PbReportViewerBD;
import com.progen.scheduler.SchedulerBD;
import com.progen.servlet.ServletUtilities;
import java.io.*;
import java.sql.Clob;
import java.sql.SQLException;
import java.text.ParseException;
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
import org.jdom.input.SAXBuilder;
import prg.db.*;
import prg.reportscheduler.HeadLinesSchedulerJob;
import utils.db.ProgenConnection;

/**
 *
 * @author arun
 */
public class DataSnapshotAction extends LookupDispatchAction {

    public static Logger logger = Logger.getLogger(DataSnapshotAction.class);

    public ActionForward openDataSnapshot(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ParseException {

        HttpSession session = request.getSession(false);
        DataSnapshot snapShot = new DataSnapshot();
        DataSnapshotBD snapShotBd = new DataSnapshotBD();
        DataSnapshotDAO snapshotDAO = new DataSnapshotDAO();
        XMLReturnObject xmlRetObj = new XMLReturnObject();
        AdvancedHtmlData advancedHtmlData = null;
        SAXBuilder builder = new SAXBuilder();
        ArrayList<String> displayColumns = new ArrayList<String>();
        ArrayList displayLabels = new ArrayList();
        HashMap<String, ArrayList<String>> dimNameList = new HashMap<String, ArrayList<String>>();
        Container container = new Container();
        PbReturnObject retObj = null;
        HashMap reportParameterValues = new HashMap();
        DrillMaps drillMap = new DrillMaps();
        Clob xmlView = null;
        String xmlString = "";
        String advancedHtmlFileName = "";
        String userId = session.getAttribute("USERID").toString();
        int snapShotInt = 0;
        if (session != null) {
            String snapShotId = request.getParameter("snapShotId");

            if (snapShotId != null) {
                snapShotInt = Integer.parseInt(snapShotId);
                advancedHtmlFileName = snapshotDAO.getAdvancedHtmlFileName(snapShotInt);
                snapShot = snapShotBd.openDataSnapshot(snapShotInt, userId);
                // session.setAttribute("DataSnapshotId", Integer.parseInt(snapShotId));


                //xmlView=snapShot.getXmlView();
                session.setAttribute("snapShot", snapShot);
                try {

                    if (!advancedHtmlFileName.equalsIgnoreCase("")) {

                        FileInputStream fis = new FileInputStream(session.getAttribute("advHtmlFileProps") + "/" + advancedHtmlFileName + ".txt");
                        ObjectInputStream ois = new ObjectInputStream(fis);
                        advancedHtmlData = (AdvancedHtmlData) ois.readObject();
                        ois.close();
                        ArrayList reportParamIds = advancedHtmlData.getDimensionIds();
                        for (int i = 0; i < reportParamIds.size(); i++) {
                            reportParameterValues.put(reportParamIds.get(i), "All");
                        }
                        HashMap<String, String> snapShotDrill = drillMap.getDrillForReport(snapShot.getReportId(), userId, reportParamIds.get(0).toString().replace("A_", ""), reportParameterValues);
                        xmlRetObj.setDrillMap(snapShotDrill);
                        for (int i = 0; i < advancedHtmlData.getColumnIds().size(); i++) {
                            displayColumns.add(advancedHtmlData.getColumnIds().get(i));
                            displayLabels.add(advancedHtmlData.getColumnNames().get(i));
                        }
                        container.setDisplayColumns(displayColumns);
                        container.setDisplayLabels(displayLabels);
                        container.setTableMeasure(advancedHtmlData.getMeasureIds());
                        container.setTableMeasureNames(advancedHtmlData.getMeasureNames());
                        container.setReportParameterIds(advancedHtmlData.getDimensionIds());
                        container.setReportParameterNames(advancedHtmlData.getDimensionNames());
                        xmlRetObj.setContainer(container);
                        xmlRetObj.setRetObj((PbReturnObject) advancedHtmlData.getReturnObject());
                        session.setAttribute("xmlRetObj", xmlRetObj);
                        session.setAttribute("advancedHtmlData", advancedHtmlData);
                        return mapping.findForward("advancedHtmlSnapshot");

                    } else {
                        return mapping.findForward("viewSnapshot");
                    }
                } catch (Exception ex) {
                    logger.error("Exception: ", ex);
                }
            }

        } else {
            return mapping.findForward("sessionExpired");
        }
        return null;
    }

    public ActionForward clearFilter(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        String viewbyList = request.getParameter("viewbyList");
        String[] viewBys = viewbyList.split(",");
        XMLReturnObject xmlRetObj = (XMLReturnObject) session.getAttribute("xmlRetObj");
        AdvancedHtmlData advancedHtmlData = (AdvancedHtmlData) session.getAttribute("advancedHtmlData");
        String data = xmlRetObj.clearFilters(viewBys, advancedHtmlData);
        try {
            PrintWriter out = response.getWriter();
            out.print(data);
        } catch (IOException ex) {
            logger.error("Exception: ", ex);
        }
        return null;
    }
//    public ActionForward getDimensionValues(ActionMapping mapping,ActionForm form, HttpServletRequest request, HttpServletResponse response)
//    {
//         HttpSession session = request.getSession(false);
//         XMLReturnObject xmlRetObj=(XMLReturnObject) session.getAttribute("xmlRetObj");
//         StringBuffer outerBuffer = new StringBuffer();
//         String contextPath=request.getContextPath();
//         ArrayList<String> dimValuesList=null;
//         PrintWriter out=null;
//
//
//         for(int i=0;i<xmlRetObj.getDimensionList().size();i++)
//         {
//              String dimName= (String) xmlRetObj.getContainer().getReportParameterNames().get(i);
//              String dimId=(String) xmlRetObj.getContainer().getReportParameterIds().get(i);
//              dimValuesList=xmlRetObj.getDimensionList().get(dimId);
//          outerBuffer.append("<li class='closed' id='" + dimName+  "'>");
//                outerBuffer.append("<img src='" + contextPath + "/icons pinvoke/table.png'></img>");
//                outerBuffer.append("<span style='font-family:verdana;'>" + dimName + "</span>");
//                outerBuffer.append("<ul id='factName-" + dimName + "'>");
//                for(int j=0;j<dimValuesList.size();j++)
//                {
//                    outerBuffer.append("<li class='closed' id='" + dimId+"_"+dimValuesList.get(j) + "'>");
//                    outerBuffer.append("<img src='" + contextPath + "/icons pinvoke/document-attribute-f.png'></img>");
//                    outerBuffer.append("<span id='" +dimId+"_"+ dimValuesList.get(j) + "'    style='font-family:verdana;'>" + dimValuesList.get(j) + "</span>");
//                    outerBuffer.append("</li>");
//                }
//                outerBuffer.append("</ul>");
//                outerBuffer.append("</li>");
//        }
//        try {
//            out = response.getWriter();
//            out.print(outerBuffer);
//        } catch (IOException ex) {
//            logger.error("Exception:",ex);
//        }
//        return null;
//
//    }

    public ActionForward getNewPbReturnObject(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        String viewById = request.getParameter("viewbyList");
//        
        String[] viewBys = viewById.split(",");
//        viewBys[0]=viewById;
        String data = "";


        XMLReturnObject xmlRetObj = (XMLReturnObject) session.getAttribute("xmlRetObj");
        AdvancedHtmlData advancedHtmlData = (AdvancedHtmlData) session.getAttribute("advancedHtmlData");
        // xmlRetObj.getRetObj().resetViewSequence();
//         ProgenLog.log(ProgenLog.FINE, this, "getNewPbReturnObject getting data", "Exit");
        logger.info("Getting data exit");

        if ((xmlRetObj.getFilters()).isEmpty()) {
            data = xmlRetObj.getNewReturnObject(viewBys, advancedHtmlData);
        } else {
            data = xmlRetObj.getNewReturnObject(viewBys, xmlRetObj.getFilters(), advancedHtmlData);
        }
//             ProgenLog.log(ProgenLog.FINE, this, "getNewPbReturnObject getting data", "Exit");
        logger.info("Exit");
        PrintWriter out = null;
        try {
            out = response.getWriter();
        } catch (IOException ex) {
            logger.error("Exception: ", ex);
        }
        out.print(data);
        return null;

    }

    public ActionForward getDrillMapForSnapshot(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);

        String value = request.getParameter("drillValue");
        String elementId = request.getParameter("elementId");
        String reportId = request.getParameter("reportId");
        String userId = session.getAttribute("USERID").toString();
        String viewbyList = request.getParameter("viewbyList");
        XMLReturnObject xmlObject = (XMLReturnObject) session.getAttribute("xmlRetObj");
        AdvancedHtmlData advancedHtmlData = (AdvancedHtmlData) session.getAttribute("advancedHtmlData");
        String filterObject = "";
        HashMap<String, String> snapShotDrill = xmlObject.getDrillMap();
        String drillElementId = snapShotDrill.get(elementId.replace("A_", ""));
        //drillElementId="A_"+drillElementId;
        String[] viewBys = new String[1];
        viewBys[0] = "A_" + drillElementId;
        // HashMap<String, String> paramMap=new HashMap<String, String>();
        //  paramMap.put(elementId, value);

        if (!viewBys[0].equalsIgnoreCase(elementId) && xmlObject.getContainer().getReportParameterIds().contains(viewBys[0])) {

            xmlObject.getFilters().put(elementId, value);
//               ProgenLog.log(ProgenLog.FINE, this, "getDrillMapForSnapshot getting data based on filter", "Exit");
            logger.info("getting data based on filter Exit");
            filterObject = xmlObject.getNewReturnObject(viewBys, xmlObject.getFilters(), advancedHtmlData);
//               ProgenLog.log(ProgenLog.FINE, this, "getDrillMapForSnapshot getting data based on filter", "Exit");
            logger.info("getting data based on filter Exit");
        }


        filterObject = filterObject + "||" + xmlObject.getFilters() + "||" + viewBys[0];
//          
        PrintWriter out = null;
        try {
            out = response.getWriter();
        } catch (IOException ex) {
            logger.error("Exception: ", ex);
        }
        out.print(filterObject);
        return null;
    }

    public ActionForward createDataSnapshot(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

        String advHtmlFileProps = (String) request.getSession(false).getAttribute("advHtmlFileProps");
        if (request.getSession(false) != null) {
            PrintWriter out = null;
            String snapName = request.getParameter("snapName");
            String reportIdsParam = request.getParameter("reportIds");
            String[] reportIds = reportIdsParam.split(",");
            String selectedInterval = request.getParameter("selectedInterval");
            String timeValue = request.getParameter("timeValue");
            String timeDuration = request.getParameter("timeDuration");
            String userId = request.getSession(false).getAttribute("USERID").toString();
            String fromHtml = request.getParameter("varhtml");
            DataSnapshotBD snapShotBd = new DataSnapshotBD();
            int refreshInterval = -1;
            if (selectedInterval != null) {
                if (selectedInterval.equalsIgnoreCase("TimeInt")) {


                    if (timeDuration.equalsIgnoreCase("Days")) {
                        refreshInterval = Integer.parseInt(timeValue);
                        refreshInterval = refreshInterval * 24 * 60;
                    } else if (timeDuration.equalsIgnoreCase("Hrs")) {
                        refreshInterval = Integer.parseInt(timeValue);
                        refreshInterval = refreshInterval * 60;

                    } else {
                        refreshInterval = Integer.parseInt(timeValue);
                    }
                }
            }





            ArrayList<DataSnapshot> snapShotList = new ArrayList();
            for (String reportId : reportIds) {
                DataSnapshot snapShot = new DataSnapshot();
                snapShot.setReportId(reportId);
                snapShot.setRefreshInterval(refreshInterval);
                snapShotList.add(snapShot);
                Iterable<String> fileNames = snapShotBd.createDataSnapshot(snapShot, userId, fromHtml, advHtmlFileProps, snapName);
                ServletUtilities.markFilesForDeletion(fileNames, request.getSession(false));
            }
            try {
                out = response.getWriter();
            } catch (IOException ex) {
                logger.error("Exception: ", ex);
            }
            out.print("true");
            return null;
        } else {
            return mapping.findForward("sessionExpired");
        }
    }

    public ActionForward downloadDataSnapshot(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ParseException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            String reportIdsParam = request.getParameter("reportIds");
            String[] reportIds = reportIdsParam.split(",");
            Boolean allViewBy = Boolean.parseBoolean(request.getParameter("allViewBy"));
            String userId = session.getAttribute("USERID").toString();
            DataSnapshotBD snapShotBd = new DataSnapshotBD();
            ArrayList<String> files = snapShotBd.downloadDataSnapShotHtml(reportIds, userId, allViewBy);
            try {
                String fileName = ServletUtilities.makeAndStoreZip(files);
//                ServletOutputStream sos = response.getOutputStream();
//                response.setContentType("application/zip");
//                response.setHeader("Content-Disposition", "attachment; filename=\"PRG_OFFLINE_REPORTS.ZIP\"");
//                sos.write(zip);
//                sos.flush();
//                sos.close();

                ServletUtilities.markFilesForDeletion(files, session);
                ServletUtilities.markFileForDeletion(fileName, session);
                response.getWriter().print(fileName);

            } catch (IOException ex) {
                logger.error("Exception: ", ex);
            }

            return null;
        } else {
            return mapping.findForward("sessionExpired");
        }
    }

    public ActionForward deleteSnapShots(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            DataSnapshotDAO snapshotdao = new DataSnapshotDAO();
            String snapShotId = "";
            String deletesnapshotids = request.getParameter("deletesnapshotids");

            if (session.getAttribute("USERID") != null) {
                snapShotId = session.getAttribute("USERID").toString();

            }
            snapshotdao.delSnapShots(deletesnapshotids);
//         return mapping.findForward("repList");
            // return mapping.findForward("ReportStudio");
            return null;
        } else {
            return mapping.findForward("sessionExpired");
        }

    }

    public ActionForward saveHeadline(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException {

        HttpSession session = request.getSession(false);
        HashMap map = null;
        Container container = null;
        map = (HashMap) session.getAttribute("PROGENTABLES");
        String reportId = request.getParameter("ReportId");
        String headline = request.getParameter("headline");
        String fromOption = request.getParameter("fromOption");
        DataSnapshotBD snapshotBD = new DataSnapshotBD();
        if (map != null) {
            container = (Container) map.get(reportId);
        }
        //Container container=Container.getContainerFromSession(request, reportId);

        int headlineId = snapshotBD.saveHeadline(container, session.getAttribute("USERID").toString(), headline, reportId, container.getRetObj().getViewSequence().size());
        response.getWriter().print(String.valueOf(headlineId));


        return null;
    }

    public ActionForward getReportHeadlines(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

        String isEdit = request.getParameter("edit");
        DataSnapshotDAO snapshotDAO = new DataSnapshotDAO();
        String headlinesTable = "";
        headlinesTable = snapshotDAO.getReportHeadlines(isEdit, request);
        try {
            response.getWriter().print(headlinesTable);
        } catch (IOException ex) {
            logger.error("Exception: ", ex);
        }

        return null;
    }

    public ActionForward getReportHeadlinesforMail(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String userId = String.valueOf(request.getSession(false).getAttribute("USERID"));
        DataSnapshotDAO snapshotDAO = new DataSnapshotDAO();
        String headlinesTable = snapshotDAO.getReportHeadlinesforMail(userId);
        try {
            response.getWriter().print(headlinesTable);
        } catch (IOException ex) {
            logger.error("Exception: ", ex);
        }

        return null;
    }

    public ActionForward getReportHeadlineData(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

        String headlineId = request.getParameter("headlineId");
        DataSnapshotDAO dataSnapshotDAO = new DataSnapshotDAO();
        Gson gson = new Gson();
        PrintWriter out = null;

        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
            String clobString = dataSnapshotDAO.getHeadlineDataString(headlineId);
            StringReader newreader;
            try {
                out = response.getWriter();
                newreader = new StringReader(clobString);
                char[] cbuf;
                int toRead = 0;
                do {
                    cbuf = new char[5196];
                    toRead = newreader.read(cbuf, 0, 5196);
                    if (toRead == -1) {
                        break;
                    }
                    out.print(cbuf);
                } while (true);
                newreader.close();
            } catch (Exception ex) {
                logger.error("Exception: ", ex);
            }
        } else {
            Clob clob = dataSnapshotDAO.getHeadlineData(headlineId);

            //String headlineTable=dataSnapshotDAO.getheadlineTable(headlineHelper);
            Reader clobReader;
            try {
                out = response.getWriter();
                clobReader = clob.getCharacterStream();

                char[] cbuf;
                int toRead = 0;
                do {
                    cbuf = new char[5196];
                    toRead = clobReader.read(cbuf, 0, 5196);
                    if (toRead == -1) {
                        break;
                    }
                    out.print(cbuf);
                } while (true);
                clobReader.close();
            } catch (Exception ex) {
                logger.error("Exception: ", ex);
            }

        }
        return null;
    }

    public ActionForward getReportHeadlineDataForMail(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

        String headlineId = request.getParameter("headlineId");
        String headlinesName = request.getParameter("headlinesName");
        String toAddress = request.getParameter("toAddress");
        String hrs = request.getParameter("hrs");
        String mins = request.getParameter("mins");
        String sendMailType = request.getParameter("sendMailType");
        String timeVal = request.getParameter("timeVal");
        String mailhrs = request.getParameter("mailhr");
        String mailmins = request.getParameter("mailmin");
        String description = request.getParameter("description");
        String dataStatus = "";
        int totalMins = 0;
        int totalmailMins = 0;
        DataSnapshotDAO snapshotDAO = new DataSnapshotDAO();
//        String dataStatusHeadlineId = null;
        StringBuilder dataStatusHeadlineId=new StringBuilder();
        PrintWriter out = null;

        try {
//            if(sendMailType.equalsIgnoreCase("checked"))
//            dataStatus=snapshotDAO.getDataStatus(headlineId);
            String[] headLineIds = headlineId.split(",");
            String[] headlinesNames = headlinesName.split("&&");
            String[] toAddresss = toAddress.split("&&");
            String[] hrss = hrs.split(",");
            String[] minss = mins.split(",");
            String[] scheduleTime = new String[hrss.length];
            totalMins = Integer.parseInt(hrs) * 60 + Integer.parseInt(mins);
            //  totalmailMins=Integer.parseInt(mailhrs)*60+Integer.parseInt(mailmins);
            String hourlyTime = "";
            hourlyTime = mailmins + " " + mailhrs;
            for (int i = 0; i < hrss.length; i++) {
                scheduleTime[i] = minss[i] + " " + hrss[i];
            }
            out = response.getWriter();
            if (sendMailType.equalsIgnoreCase("checked")) {
                for (int i = 0; i < headLineIds.length; i++) {
                    dataStatus = snapshotDAO.getDataStatus(headLineIds[i]);
                    if (dataStatus.equals("Non-Empty")) {
                        dataStatusHeadlineId.append(dataStatusHeadlineId).append(",").append(headLineIds[i]);
                    }
                }
                if (dataStatusHeadlineId != null) {
//                    
                    //dataStatusHeadlineId=dataStatusHeadlineId.substring();
                    headLineIds = dataStatusHeadlineId.substring(5, dataStatusHeadlineId.length()).split(",");
                    SchedulerBD bd = new SchedulerBD();
                    bd.secheduleHeadLines(headLineIds, headlinesNames, toAddresss, scheduleTime, timeVal, totalMins, hourlyTime, description);
                    out.print("success");
                } else {
                    out.print("failure");
                }
            } else {
                SchedulerBD bd = new SchedulerBD();
                bd.secheduleHeadLines(headLineIds, headlinesNames, toAddresss, scheduleTime, timeVal, totalMins, hourlyTime, description);
                out.print("success");
            }
//            if(!dataStatus.equalsIgnoreCase("Empty"))
//            {
//            SchedulerBD bd = new SchedulerBD();
//            bd.secheduleHeadLines(headLineIds,headlinesNames, toAddresss, scheduleTime,timeVal,totalMins,hourlyTime,description);
//            out.print("success");
//            }
//            else
//                out.print("failure");

        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }


        return null;
    }

    public ActionForward deleteHeadline(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String headlineId = request.getParameter("headlineId");
        DataSnapshotDAO dataSnapshotDAO = new DataSnapshotDAO();
        dataSnapshotDAO.deleteHeadline(headlineId);
        return null;
    }

    public ActionForward sendEmails(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String headlines = request.getParameter("headlineList");
        DataSnapshotDAO dataSnapshotDAO = new DataSnapshotDAO();
        String emailHeadlines = dataSnapshotDAO.getHeadlinesforEmail(headlines);
        try {
            response.getWriter().print(emailHeadlines);
        } catch (IOException ex) {
            logger.error("Exception: ", ex);
        }


        return null;
    }

    public ActionForward sendEmailstoUsers(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String headlineId = request.getParameter("headlineId");
        String hrs = request.getParameter("hrs");
        String mins = request.getParameter("mins");
        String[] t = new String[2];
        t[0] = hrs;
        t[1] = mins;
        return null;
    }

    public ActionForward sendHeadlineMail(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        String headlineId = request.getParameter("headlineid");
        // 
        String dlType = request.getParameter("fileType");
        String userId = String.valueOf(session.getAttribute("USERID"));
        String userstextarea = request.getParameter("userstextarea");
        String subject = request.getParameter("share_subject");
        HeadLinesSchedulerJob schedulejob = new HeadLinesSchedulerJob();
        try {
            schedulejob.sendSchedulerMail(headlineId, subject, userstextarea, null, null);
        } catch (IOException ex) {
            logger.error("Exception: ", ex);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }

        return null;
    }

    public ActionForward shareHeadlinetoUser(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {

        String reportId = request.getParameter("reportId");
        String headlineId = request.getParameter("headlineId");
        String refUserId = request.getSession(false).getAttribute("USERID").toString();
        String[] users = null;
        if (request.getParameter("users") != null) {
            users = request.getParameter("users").split(",");
        }
        //   
        DataSnapshotDAO dataSnapshotDAO = new DataSnapshotDAO();
        dataSnapshotDAO.shareHealinestoUsers(headlineId, users);
        response.getWriter().print("success");

        return null;
    }

    public ActionForward displaycheckedHeadlines(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException, ClassNotFoundException, Exception {
        String checkedHeadlines = null;
        checkedHeadlines = request.getParameter("checkedHeadlines");
        String advHtmlFileProps = (String) request.getSession(false).getAttribute("advHtmlFileProps");

        String height = request.getParameter("height");
        String width = request.getParameter("width");
        String oneViewIdValue = request.getParameter("oneViewIdValue");
        String repType = request.getParameter("repHeadline");
        String reportName = request.getParameter("name");
        String busroleId = request.getParameter("busroleId");
        int widt = Integer.parseInt(width);
        int heigh = Integer.parseInt(height);
//        int colSp = Integer.parseInt(request.getParameter("colSp"));
//        int rowSp = Integer.parseInt(request.getParameter("rowSp"));
        HttpSession session = request.getSession(false);
        HashMap oneviewHashMap = null;
        OnceViewContainer onecontainer = new OnceViewContainer();
        oneviewHashMap = (HashMap) session.getAttribute("ONEVIEWDETAILS");
        if (oneviewHashMap != null) {
            onecontainer = (OnceViewContainer) oneviewHashMap.get(oneViewIdValue);
            OneViewLetDetails detail = new OneViewLetDetails();
            detail = onecontainer.onviewLetdetails.get(Integer.parseInt(request.getParameter("divId")));
//           detail.setRepId(reportId);
            detail.setHealinesIds(checkedHeadlines);
            detail.setRepName(reportName);
            detail.setReptype(repType);
            detail.setWidth(widt);
            detail.setHeight(heigh);
            detail.setRoleId(busroleId);
            if (detail.getMeasurDrill() != null) {
                detail.setMeasurDrill(null);
            }
        } else {
            OneViewLetDetails detail = new OneViewLetDetails();
            ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();
//             onecontainer = reportTemplateDAO.getOneViewData(oneViewIdValue);

            // String fileName = reportTemplateDAO.getOneviewFileName(oneViewIdValue);
            String fileName = request.getSession(false).getAttribute("tempFileName").toString();
            FileInputStream fis2 = new FileInputStream(advHtmlFileProps + "/" + fileName);
            ObjectInputStream ois2 = new ObjectInputStream(fis2);
            onecontainer = (OnceViewContainer) ois2.readObject();
            ois2.close();

            detail = onecontainer.onviewLetdetails.get(Integer.parseInt(request.getParameter("divId")));
//           detail.setRepId(reportId);
            detail.setHealinesIds(checkedHeadlines);
            detail.setRepName(reportName);
            detail.setWidth(widt);
            detail.setHeight(heigh);
            detail.setReptype(repType);
            if (detail.getMeasurDrill() != null) {
                detail.setMeasurDrill(null);
            }
//           reportTemplateDAO.updateOneviewData(onecontainer,oneViewIdValue);

            HashMap tempRegHashMap = onecontainer.getTempRegHashMap();
            String tempRegFileName = (String) tempRegHashMap.get(Integer.parseInt(detail.getNoOfViewLets()));
            if (tempRegFileName == null) {
                tempRegFileName = "InnerRegionDetails" + onecontainer.oneviewId + "_" + detail.getNoOfViewLets() + "_" + session.getId() + "_" + System.currentTimeMillis() + ".txt";
                tempRegHashMap.put(Integer.parseInt(detail.getNoOfViewLets()), tempRegFileName);
            }
            DashboardTemplateBD dashboardTemplateBD = new DashboardTemplateBD();
            OneViewBD oneViewBD = new OneViewBD();
            String result = "";
            result = dashboardTemplateBD.getHeadLinehDetailsData(request, response, session, detail);
            oneViewBD.writeOneviewRegData(onecontainer, result, detail.getNoOfViewLets(), request);
            FileOutputStream fos = new FileOutputStream(advHtmlFileProps + "/" + fileName);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(onecontainer);
            oos.flush();
            oos.close();
        }
//           String divid = "Dashlets-".concat(request.getParameter("divId"));
//            request.setAttribute("divid", divid);


        request.setAttribute("checkedHeadlines", checkedHeadlines);
        DataSnapshotDAO snapshotDAO = new DataSnapshotDAO();
        String headlinedetails = snapshotDAO.getReportHeadlines("false", request);
        response.getWriter().print(headlinedetails);
        return null;
    }

    public ActionForward getDynamicHeadlines(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String headlinesTable = "";
        String userId = (String) request.getSession(false).getAttribute("USERID");
        DataSnapshotDAO snapshotDAO = new DataSnapshotDAO();
        headlinesTable = snapshotDAO.getDynamicHeadlines(userId);
        try {
            response.getWriter().print(headlinesTable);
        } catch (IOException ex) {
            logger.error("Exception: ", ex);
        }
        return null;
    }

    public ActionForward getDynamicHeadlineData(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ParseException {
        String headlineId = request.getParameter("headlineId");
        String reportId = request.getParameter("reportId");
        String headlineName = request.getParameter("headlineName");
        String userId = (String) request.getSession(false).getAttribute("USERID");
        PbReportViewerBD viewerbd = new PbReportViewerBD();
        Container container = viewerbd.generateContainer(reportId, headlineId, userId, headlineName);
        DataSnapshotGenerator snapshotgenerator = new DataSnapshotGenerator();
//     if ( container.getReportCollect().tablePropertiesXML != null )
//         ReportTablePropertyBuilder.TABLE_PROPERTY_BUILDER.updateTablePropertiesInContainer(container, container.getReportCollect().tablePropertiesXML);
        PbReportTableBD reportTableBD = new PbReportTableBD();
        reportTableBD.searchDataSet(container);
        ArrayList<String> sortCols = null;
        char[] sortTypes = null;
        char[] sortDataTypes = null;
        int topbottomCount;
        ArrayList<Integer> rowSequence;
        int topBottomRowCount;
        ProgenDataSet retObj = container.getRetObj();
        int rowCount = retObj.getViewSequence().size();
        String sort = null;
        if (container.isTopBottomSet()) {
            sortCols = container.getSortColumns();
            sortTypes = container.getSortTypes();

            topbottomCount = container.getTopBottomCount();

            if (container.getTopBottomType().equals(ContainerConstants.TOP_BOTTOM_TYPE_TOP_ROWS)) {
                //sort = "1";
                if (container.getTopBottomMode().equals(ContainerConstants.TOP_BOTTOM_MODE_PERCENTWISE)) {
                    rowSequence = retObj.findTopBottomPercentWise(sortCols, sortTypes, topbottomCount);
                } else {
                    rowSequence = retObj.findTopBottom(sortCols, sortTypes, topbottomCount);
                }
                topBottomRowCount = rowSequence.size();
                rowCount = rowSequence.size();
                retObj.setViewSequence(rowSequence);
                container.setGrret(retObj);

            } else if (container.getTopBottomType().equals(ContainerConstants.TOP_BOTTOM_TYPE_BOTTOM_ROWS)) {

                // sort = "0";
                if (container.getTopBottomMode().equals(ContainerConstants.TOP_BOTTOM_MODE_PERCENTWISE)) {
                    rowSequence = retObj.findTopBottomPercentWise(sortCols, sortTypes, topbottomCount);
                } else {
                    rowSequence = retObj.findTopBottom(sortCols, sortTypes, topbottomCount);
                }
                topBottomRowCount = rowSequence.size();
                //rowCount = retObj.getRowCount();
                rowCount = rowSequence.size();
                retObj.setViewSequence(rowSequence);
                container.setGrret(retObj);
            }

        } else if (sort == null) //user hasn't done any sort
        {
            if (sortCols != null) //check if any previous sort is present retain them
            {
                sortCols = container.getSortColumns();
                if (!sortCols.isEmpty()) {

                    sortTypes = container.getSortTypes();
                    sortDataTypes = container.getSortDataTypes();
                    rowSequence = retObj.sortDataSet(sortCols, sortTypes, sortDataTypes);//dataTypes, container.getOriginalColumns());
                    retObj.setViewSequence(rowSequence);
                    rowCount = rowSequence.size();
                    container.setGrret(retObj);
                }
            }
        }
        String tabledata = snapshotgenerator.generateAndStoreHtmlSnapshot(container, userId, "fromdynamicheadline");
        try {
            response.getWriter().print(tabledata);
        } catch (IOException ex) {
            logger.error("Exception: ", ex);
        }
        return null;
    }

    public ActionForward checkReportAccess(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String reportId = request.getParameter("reportId");
        String userId = (String) request.getSession(false).getAttribute("USERID");
        DataSnapshotDAO dao = new DataSnapshotDAO();
        String accessReport = dao.checkReportAccess(reportId, userId);
        try {
            if (accessReport.equalsIgnoreCase("invalid")) {
                return mapping.findForward("invalidReportAccess");
            } else {
                response.getWriter().print(accessReport);
            }
        } catch (IOException ex) {
            logger.error("Exception: ", ex);
        }
        return null;
    }

    @Override
    protected Map getKeyMethodMap() {
        Map map = new HashMap<String, String>();
        map.put("createDataSnapshot", "createDataSnapshot");
        map.put("openDataSnapshot", "openDataSnapshot");
        map.put("downloadDataSnapshot", "downloadDataSnapshot");
        map.put("deleteSnapShots", "deleteSnapShots");
        map.put("getNewPbReturnObject", "getNewPbReturnObject");
        map.put("getDrillMapForSnapshot", "getDrillMapForSnapshot");
        map.put("clearFilter", "clearFilter");
        map.put("saveHeadline", "saveHeadline");
        map.put("getReportHeadlines", "getReportHeadlines");
        map.put("getReportHeadlineData", "getReportHeadlineData");
        map.put("deleteHeadline", "deleteHeadline");
        map.put("getReportHeadlinesforMail", "getReportHeadlinesforMail");
        map.put("sendEmails", "sendEmails");
        map.put("sendEmailstoUsers", "sendEmailstoUsers");
        map.put("getReportHeadlineDataForMail", "getReportHeadlineDataForMail");
        map.put("sendHeadlineMail", "sendHeadlineMail");
        map.put("shareHeadlinetoUser", "shareHeadlinetoUser");
        map.put("displaycheckedHeadlines", "displaycheckedHeadlines");
        map.put("getDynamicHeadlines", "getDynamicHeadlines");
        map.put("getDynamicHeadlineData", "getDynamicHeadlineData");
        map.put("checkReportAccess", "checkReportAccess");
//      map.put("getDimensionValues", "getDimensionValues");

        return map;
    }
}
