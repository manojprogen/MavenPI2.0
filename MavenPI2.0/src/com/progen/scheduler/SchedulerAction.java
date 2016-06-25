/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.scheduler;

import com.google.common.collect.ArrayListMultimap;
import com.google.gson.Gson;
import com.progen.report.PbReportCollection;
import com.progen.report.ReportParameterValue;
import com.progen.report.scorecard.bd.ScorecardDesignBd;
import com.progen.report.scorecard.bd.ScorecardViewerBD;
import com.progen.report.scorecard.tracker.ScorecardTracker;
import com.progen.report.scorecard.tracker.ScorecardTrackerJob;
import com.progen.report.scorecard.tracker.ScorecardTrackerRule;
import com.progen.reportdesigner.db.ReportTemplateDAO;
import com.progen.scheduler.db.SchedulerDAO;
import com.progen.scheduler.tracker.Tracker;
import com.progen.scheduler.tracker.TrackerCondition;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.ParseException;
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
public class SchedulerAction extends LookupDispatchAction {

    public static Logger logger = Logger.getLogger(SchedulerAction.class);

    @Override
    protected Map getKeyMethodMap() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("scheduleReport", "scheduleReport");
        map.put("scheduleTracker", "scheduleTracker");
        map.put("getTrackerMeasures", "getTrackerMeasures");
        map.put("getTrackerDimensions", "getTrackerDimensions");
        map.put("getTrackerDimensionsDetails", "getTrackerDimensionsDetails");
        map.put("getTrackerDetails", "getTrackerDetails");
        map.put("scheduleHistory", "scheduleHistory");
        map.put("runTracker", "runTracker");
        map.put("getReports", "getReports");
        map.put("getReportScheduleDetails", "getReportScheduleDetails");
        map.put("deleteSchedule", "deleteSchedule");
        map.put("runReportScheduler", "runReportScheduler");
        map.put("uploadDimensionsForMail", "uploadDimensionsForMail");
        map.put("getDimDetails", "getDimDetails");
        map.put("getAllReportDetails", "getAllReportDetails");
        map.put("getReportParameters", "getReportParameters");
        map.put("checkUserAvailibility", "checkUserAvailibility");
        map.put("saveTrackerConditions", "saveTrackerConditions");
        map.put("getTrckerCondFrmSession", "getTrckerCondFrmSession");
        map.put("getAllBusRoles", "getAllBusRoles");
        map.put("getScoreCardsForScheduler", "getScoreCardsForScheduler");
        map.put("getScorecardTimeDetails", "getScorecardTimeDetails");
        map.put("saveScorecardTracker", "saveScorecardTracker");
        map.put("editScorecardTracker", "editScorecardTracker");
        map.put("runScorecardTracker", "runScorecardTracker");
        return map;
    }

    public ActionForward scheduleReport(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        try {
            HttpSession session = request.getSession();
            ReportSchedule schedule = new ReportSchedule();
            List<UserDimensionMap> userMap = null;
            String selectedreportId = request.getParameter("allDim");
            boolean isEdit = Boolean.parseBoolean(request.getParameter("isEdit"));
            boolean autoSplited = Boolean.parseBoolean(request.getParameter("autoChckInput"));
            String reportId = request.getParameter("reportId");
            String strtDate = request.getParameter("startdate");
            String endDate = request.getParameter("enddate");
            String subscriber = request.getParameter("toAddress");
            String contentType = request.getParameter("contentType");
            String schedulerName = request.getParameter("schdReportName");
            String paramXml = request.getParameter("paramXml");
//            String viewById=request.getParameter("viewByIdInput");
            String viewBy = request.getParameter("viewBy");
            String[] dimDetail = request.getParameterValues("dimDetail");
            String[] mailDetail = request.getParameterValues("mail");
            String dimId = request.getParameter("dimId");
            String[] rowViewById = request.getParameterValues("viewById");
            String[] rowViewByVal = request.getParameterValues("viewByValue");
            String roleId = request.getParameter("businessRole");
            String checkViewByIds = request.getParameter("checkedViewBy");
            String checkViewByName = request.getParameter("viewBySelected");
            String scheduledTime = "";
            String frequency = request.getParameter("frequency");
            String particularDate = "";
            String schedulerFrequency = "";
            String hrs = request.getParameter("hrs");
            String mins = request.getParameter("mins");
            String fromReport = request.getParameter("fromReport");
            scheduledTime = hrs.concat(":").concat(mins);
            String dimName = "";
            String dataSelection = "";
            ArrayList<String> dataoptionValues = new ArrayList<String>();
            boolean runflag = Boolean.parseBoolean(request.getParameter("runFlag"));

            if (checkViewByIds != null && !"".equals(checkViewByIds)) {
                String[] viewByArr = checkViewByIds.split(",");
                String[] viewByNamesArr = checkViewByName.split(",");
                if (viewByArr.length > 0) {
                    dimId = viewByArr[0];
                    dimName = viewByNamesArr[0];
                }
                checkViewByIds = "";
                checkViewByName = "";
                if (viewByArr.length > 1) {
                    for (int i = 1; i < viewByArr.length; i++) {
                        checkViewByIds = checkViewByIds + "," + viewByArr[i];
                        checkViewByName = checkViewByName + "," + viewByNamesArr[i];
                    }
                    checkViewByIds = checkViewByIds.substring(1);
                    checkViewByName = checkViewByName.substring(1);
                }
            }

            if (frequency.equalsIgnoreCase("1")) {
                schedulerFrequency = "Daily";
                particularDate = "";

                String[] dataSelection1 = request.getParameterValues("selectedvalues");
                dataSelection = request.getParameter("dailyData");
                dataoptionValues.add(dataSelection);
                if (dataSelection1 != null) {
                    dataoptionValues.addAll(Arrays.asList(dataSelection1));
                }

            } else if (frequency.equalsIgnoreCase("2")) {
                schedulerFrequency = "Monthly";
                particularDate = request.getParameter("monthDate");
                if (!"L".equalsIgnoreCase(particularDate) && !"B".equalsIgnoreCase(particularDate)) {
                    dataSelection = request.getParameter("monthlyData");
                }
            } else {
                schedulerFrequency = "Weekly";
                particularDate = request.getParameter("alertDay");
            }

            String userId = String.valueOf(session.getAttribute("USERID"));
            Date sDate;
            Date eDate;
            DateFormat formatter;
            formatter = new SimpleDateFormat("MM/dd/yyyy");
            sDate = formatter.parse(strtDate);
            eDate = formatter.parse(endDate);

            schedule.setReportId(Integer.parseInt(selectedreportId));
            schedule.setContenType(contentType);
            schedule.setEndDate(eDate);
            schedule.setFrequency(schedulerFrequency);
            schedule.setStartDate(sDate);
            schedule.setViewBy(viewBy);
            schedule.setScheduledTime(scheduledTime);
            schedule.setUserId(userId);
            schedule.setSchedulerName(schedulerName);
            schedule.setIsAutoSplited(autoSplited);
            schedule.setViewById(dimId);
            schedule.setViewByName(dimName);
            schedule.setFolderId(roleId);
            schedule.setParticularDay(particularDate);
            schedule.setCheckedViewByIds(checkViewByIds);
            schedule.setCheckViewByNames(checkViewByName);
            schedule.setDataSelection(dataSelection);
            schedule.setDataSelectionTypes(dataoptionValues);
            schedule.setRunFlag(runflag);

            if (autoSplited) {
                userMap = (List<UserDimensionMap>) session.getAttribute("userDimMap");
                schedule.setUsermap(userMap);
                session.removeAttribute("userDimMap");
            }

            if (!(paramXml.equalsIgnoreCase("") && paramXml != null)) {
                schedule.setParameterXml(paramXml);
            }
            List<ReportSchedulePreferences> schdPreferenceList = new ArrayList<ReportSchedulePreferences>();
            List<ReportScheduleSlices> schdSlicesList = new ArrayList<ReportScheduleSlices>();
            ReportSchedulePreferences schedulePreferences = null;
            ReportScheduleSlices scheduleSlices = null;
            for (int i = 0; i < dimDetail.length; i++) {
                schedulePreferences = new ReportSchedulePreferences();
                schedulePreferences.setDimValues(dimDetail[i]);
                schedulePreferences.setMailIds(mailDetail[i]);
                schedulePreferences.setDimId(dimId);
                schdPreferenceList.add(schedulePreferences);

            }
            if (viewBy.equalsIgnoreCase("SelectedViewby")) {
                String[] viewByIdsArr = checkViewByIds.split(",");

                for (String eachViewById : viewByIdsArr) {
                    scheduleSlices = new ReportScheduleSlices();
                    scheduleSlices.setRowViewById(eachViewById);
                    scheduleSlices.setRowViewByVal("All");
                    schdSlicesList.add(scheduleSlices);
                }
            }

            schedule.setReportSchedulePrefrences(schdPreferenceList);
            schedule.setReportScheduleSlices(schdSlicesList);
            SchedulerDAO dao = new SchedulerDAO();
            if (isEdit) {
                //reportId = String.valueOf(schedule.getReportScheduledId());
                dao.deleteReportSchedule(reportId);
                schedule.setReportScheduledId(Integer.parseInt(reportId));
            }
            String schedId = dao.insertReportSchedule(schedule);
            dao.insertReportSchedulePreferences(schedule);
            SchedulerBD bd = new SchedulerBD();
            bd.scheduleReport(schedule, isEdit);

            PrintWriter out = null;
            try {
                out = response.getWriter();
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
            out.print(schedId);
        } catch (ParseException ex) {
            logger.error("Exception:", ex);
        }
        return null;
    }

    public ActionForward getTrackerMeasures(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        try {
            String folderId = request.getParameter("folderId");
            String ctxPath = request.getParameter("ctxPath");
            String userId = request.getParameter("userId");
            ArrayList list = new ArrayList();
            ReportTemplateDAO repDao = new ReportTemplateDAO();
            String measures = repDao.getMeasures(folderId, list, ctxPath);
            response.getWriter().print(measures);
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }
        return null;
    }

    public ActionForward getTrackerDimensions(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String folderId = request.getParameter("folderId");
        String userId = request.getParameter("userId");
        ReportTemplateDAO repDao = new ReportTemplateDAO();
        PbReturnObject dimObj = repDao.getUserDimensions(folderId, userId, "");
        PbReturnObject retObj = null;
        ArrayList<TrackerDimensions> dimList = new ArrayList<TrackerDimensions>();
        TrackerDimensions trackerDimension = null;
        for (int i = 0; i < dimObj.getRowCount(); i++) {
            retObj = repDao.getNonFavoriteParamters(dimObj.getFieldValueString(i, 2), dimObj.getFieldValueString(i, 0));
            for (int j = 0; j < retObj.getRowCount(); j++) {
                trackerDimension = new TrackerDimensions();
                trackerDimension.setDimensionId(retObj.getFieldValueString(j, 0));
                trackerDimension.setDimensionName(retObj.getFieldValueString(j, 1));
                dimList.add(trackerDimension);
            }

        }
        Gson gson = new Gson();
        try {
            PrintWriter out = response.getWriter();
            out.print(gson.toJson(dimList));
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }
        return null;
    }

    public ActionForward getTrackerDimensionsDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        PrintWriter out = null;
        try {
            String dimSelected = request.getParameter("dimSelected");
            SchedulerDAO schedulerDAO = new SchedulerDAO();
            String dimMem = schedulerDAO.getTrackerDimDetails(dimSelected);
            out = response.getWriter();
            out.print(dimMem);
            out.close();

        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }
        return null;
    }

    public ActionForward scheduleTracker(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        try {
            HttpSession session = request.getSession(false);
            String trackerName = request.getParameter("trackerName");
            String frequency = request.getParameter("frequency");
            String startDate = request.getParameter("startdate");
            String endDate = request.getParameter("enddate");
            String folderId = request.getParameter("folderId");
            String measureId = request.getParameter("measId");
            String measureName = request.getParameter("measureSelected");
            String dimId = request.getParameter("dimId");
            String dimName = request.getParameter("dimName");
            String mode = request.getParameter("mode");
            String subscribers = request.getParameter("toAddress");
            boolean isEdit = Boolean.parseBoolean(request.getParameter("isEdit"));
            String userId = (String) session.getAttribute("USERID");
            String paramXml = request.getParameter("paramXml");
            String condValue = request.getParameter("conditionValue");
            String condOperator = request.getParameter("operatorValue");
            String suppMsrIds = request.getParameter("suppMeasId");
            String suppMsrNames = request.getParameter("suppMeasName");
            boolean autoChckInput = Boolean.parseBoolean(request.getParameter("autoChckInput"));
            boolean sendAnywayCheck = false;
            if (request.getParameter("sendAnywayInput") != null && !request.getParameter("sendAnywayInput").equals("")) {
                sendAnywayCheck = Boolean.parseBoolean(request.getParameter("sendAnywayInput"));
            }
            String trackerId = "";
            if (isEdit) {
                trackerId = request.getParameter("trackerId");
            }

            String trackerFrequency = "";
            String[] operators = null;
            String[] sValues = null;
            String[] eValues = null;
            String[] viewByValues = null;
            String[] mailIds = null;
            String[] targetValues = null;   //for targetBasis
            String[] DevsnValues = null;    //for targetBasis
            Date sDate;
            Date eDate;
            DateFormat formatter;
            formatter = new SimpleDateFormat("MM/dd/yyyy");
            sDate = formatter.parse(startDate);
            eDate = formatter.parse(endDate);
            viewByValues = request.getParameterValues("dimDetail");
            mailIds = request.getParameterValues("mail");
            operators = request.getParameterValues("operators");
            sValues = request.getParameterValues("sValues");
            eValues = request.getParameterValues("eValues");
            targetValues = request.getParameterValues("targetValues");  //for targetBasis
            DevsnValues = request.getParameterValues("DevsnValues");    //for targetBasis
            ArrayListMultimap<String, TrackerCondition> trackerCondMap = null;
            ArrayListMultimap<String, TrackerCondition> trackerMap = ArrayListMultimap.create();
            if (session.getAttribute("TrackerCondition") != null) {
                trackerCondMap = (ArrayListMultimap<String, TrackerCondition>) session.getAttribute("TrackerCondition");
                for (int j = 0; j < viewByValues.length; j++) {
                    List<TrackerCondition> trckrConds = trackerCondMap.get(viewByValues[j]);
                    for (TrackerCondition trackerCondition : trckrConds) {
                        trackerMap.put(viewByValues[j], trackerCondition);
                    }
                }
                session.removeAttribute("TrackerCondition");
            }

            String scheduledTime = "";
            String scheduledDay = "";

            if (frequency.equalsIgnoreCase("1")) {
                trackerFrequency = "Daily";
                String hrs = request.getParameter("hrs");
                String mins = request.getParameter("mins");
                scheduledTime = hrs.concat(":").concat(mins);
            } else if (frequency.equalsIgnoreCase("2")) {
                trackerFrequency = "Monthly";
                scheduledDay = request.getParameter("alertDate");
                String hrs = request.getParameter("hrs");
                String mins = request.getParameter("mins");
                scheduledTime = hrs.concat(":").concat(mins);
            } else if (frequency.equalsIgnoreCase("3")) {
                trackerFrequency = "Weekly";
                scheduledDay = request.getParameter("alertDate");
                String hrs = request.getParameter("hrs");
                String mins = request.getParameter("mins");
                scheduledTime = hrs.concat(":").concat(mins);
            }

            Tracker tracker = new Tracker();
            tracker.setTrackerName(trackerName);
            tracker.setFrequency(trackerFrequency);
            tracker.setStartDate(sDate);
            tracker.setEndDate(eDate);
            tracker.setScheduledTime(scheduledTime);
            tracker.setFolderId(folderId);
            tracker.setMeasureId(measureId);
            tracker.setDimensionName(dimName);
            tracker.setMode(mode);
            tracker.setViewById(dimId);
            tracker.setSubscribers(subscribers);
            tracker.setUserId(userId);
            tracker.setTrackerConditions(trackerMap);
            tracker.setMeasureName(measureName);
            tracker.setCondValue(condValue);
            tracker.setCondOperator(condOperator);
            tracker.setSupportingMsr(suppMsrIds);
            tracker.setSupportingMsrNames(suppMsrNames);
            tracker.setisAutoIdentify(autoChckInput);
            tracker.setSendAnyWay(sendAnywayCheck);
            tracker.setParticularDay(scheduledDay);

            if (!(paramXml.equalsIgnoreCase("") && paramXml != null)) {
                tracker.setParameterXml(paramXml);
            }
            SchedulerDAO schedulerDAO = new SchedulerDAO();
            if (isEdit) {
                schedulerDAO.deleteTrackerDetails(trackerId);
            }

            schedulerDAO.insertTrackerSchedule(tracker);
            schedulerDAO.insertTrackerCondition(tracker);
            SchedulerBD schedulerBD = new SchedulerBD();
            schedulerBD.scheduleTracker(tracker, isEdit);

        } catch (ParseException ex) {
            logger.error("Exception:", ex);
        }
        return null;
    }

    public ActionForward getTrackerDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        SchedulerDAO dao = new SchedulerDAO();
        String trackerId = request.getParameter("trackerId");
        boolean fromReport = false;
        Tracker tracker = null;
        PrintWriter out = null;
        Gson gson = new Gson();
        String userId = String.valueOf(session.getAttribute("USERID"));
        ReportTemplateDAO reptTemplateDao = new ReportTemplateDAO();
        HashMap<String, ArrayList<String>> roleMap = reptTemplateDao.getRoleIds(userId);
        String roleJson = gson.toJson(roleMap);
        String trackerCondJson = "";
        String dimValJson = "";
        StringBuilder dimStr = new StringBuilder();
        if (trackerId.equalsIgnoreCase(null) || trackerId.equalsIgnoreCase("null") || trackerId.equalsIgnoreCase("")) {
            tracker = new Tracker();
            if (session.getAttribute("fromReport") != null) {
                fromReport = (Boolean) session.getAttribute("fromReport");
            }

            if (fromReport) {
                StringBuilder paramXML = (StringBuilder) session.getAttribute("paramXML");
                String[] paramDetails = paramXML.toString().split("~");
                StringBuilder paramHtml = (StringBuilder) session.getAttribute("paramHtml");
                String reportName = (String) session.getAttribute("reportName");

                session.removeAttribute("fromReport");
                session.removeAttribute("reportId");
                session.removeAttribute("paramXML");
                session.removeAttribute("paramHtml");
                session.removeAttribute("reportName");
                session.removeAttribute("isEdit");

                tracker.setTrackerName(reportName);
                tracker.setParameterXml(paramDetails[0]);
                tracker.setParameter(paramHtml.toString());
            }
        } else {
            tracker = dao.getTracker(Integer.parseInt(trackerId), false);
            ArrayListMultimap<String, TrackerCondition> trackerCond = tracker.getTrackerConditions();
            session.setAttribute("TrackerCondition", trackerCond);
            HashMap<String, List<TrackerCondition>> trackerMap = new HashMap<String, List<TrackerCondition>>();
            Set condSet = trackerCond.keySet();
            Iterator condItr = condSet.iterator();
            int i = 0;
            StringBuilder dimValStr = new StringBuilder();
            while (condItr.hasNext()) {
                String dimValue = (String) condItr.next();
                dimValStr.append("," + "\"").append(dimValue).append("\"");
                List<TrackerCondition> trackerCondition = trackerCond.get(dimValue);
                trackerMap.put(dimValue, trackerCondition);
                i++;
            }
            trackerCondJson = gson.toJson(trackerMap);
//            

            dimValStr.replace(0, 1, "");
            dimStr.append("{");
            dimStr.append("\"dimValues\":[").append(dimValStr.toString()).append("]}");
        }

        String trackerJson = gson.toJson(tracker);
        String completeJson = trackerJson.concat("~").concat(roleJson).concat("~").concat(trackerCondJson).concat("~").concat(dimStr.toString());
        try {
            out = response.getWriter();
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }
        out.print(completeJson);

        return null;
    }

    public ActionForward scheduleHistory(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        PrintWriter out = null;
        try {
            HttpSession session = request.getSession(false);
            SchedulerBD schedulerBD = new SchedulerBD();
            out = response.getWriter();
//            int trackerId = Integer.parseInt((String) request.getParameter("trackerId"));
            String timeLevel = request.getParameter("timeLevel");
            String memberId = request.getParameter("memberId");
            String userId = (String) session.getAttribute("USERID");
            String measureId = request.getParameter("measureId");
            String folderId = request.getParameter("folderId");
            String dimId = request.getParameter("dimId");
            String dimName = request.getParameter("dimName");
            String msrName = request.getParameter("msrName");
            String fromMsr = request.getParameter("fromMeasure");
            String frequency = request.getParameter("frequency");

            List<String> memIdList = Arrays.asList(measureId.split(","));

            if ("1".equals(frequency)) //Daily
            {
                timeLevel = "Day";
            } else if ("2".equals(frequency)) //Monthly
            {
                timeLevel = "Month";
            } else if ("3".equals(frequency)) //Weekly
            {
                timeLevel = "Week";
            }
            String schedulerHistory = schedulerBD.buildSchedulerHistory(timeLevel, measureId, dimId, memIdList, userId, folderId, dimName, msrName, fromMsr);
            out.print(schedulerHistory);
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        } finally {
            out.close();
        }
        return null;
    }

    public ActionForward runTracker(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        SchedulerBD bd = new SchedulerBD();
        String trackerIdStr = request.getParameter("trackerId");
        boolean fromStudio = Boolean.parseBoolean(request.getParameter("fromStudio"));
        String[] trackerArr = trackerIdStr.split(",");
        for (String trackerId : trackerArr) {
            if (trackerId != null) {
                if ("".equalsIgnoreCase(trackerId)) {
                    trackerId = "0";
                }
                int trckrId = Integer.parseInt(trackerId);
                bd.runTracker(trckrId, fromStudio);
            }

        }
        return null;
    }

    public ActionForward getReports(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        SchedulerDAO schedulerDAO = new SchedulerDAO();
        String reportJson = schedulerDAO.getReports();
        PrintWriter out = null;
        try {
            out = response.getWriter();
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }
        out.print(reportJson);
        return null;
    }

    public ActionForward getReportScheduleDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        SchedulerDAO dao = new SchedulerDAO();
        String schedulerId = request.getParameter("schedulerId");
        String userId = String.valueOf(session.getAttribute("USERID"));
        boolean fromReport = false;
        ReportTemplateDAO reptTemplateDao = new ReportTemplateDAO();

        ArrayList<String> firstRoleId = null;
        ArrayList<String> firstRolename = null;
        List<ReportDetails> allReportDetails = null;
//         Gson roleGson=new Gson();
        Gson gson = new Gson();
        PrintWriter out = null;
        String reportDetailsJson = "";

        HashMap<String, ArrayList<String>> roleMap = reptTemplateDao.getRoleIds(userId);
        String roleJson = gson.toJson(roleMap);
        ReportSchedule schedule = null;
        if (schedulerId.equalsIgnoreCase(null) || schedulerId.equalsIgnoreCase("null") || schedulerId.equalsIgnoreCase("")) {
            schedule = new ReportSchedule();
            if (session.getAttribute("fromReport") != null) {
                fromReport = (Boolean) session.getAttribute("fromReport");
            }

            if (fromReport) {
                StringBuilder paramXML = (StringBuilder) session.getAttribute("paramXML");
                String[] paramDetails = paramXML.toString().split("~");
                StringBuilder paramHtml = (StringBuilder) session.getAttribute("paramHtml");
                String reportId = (String) session.getAttribute("reportId");
                String reportName = (String) session.getAttribute("reportName");

                session.removeAttribute("fromReport");
                session.removeAttribute("reportId");
                session.removeAttribute("paramXML");
                session.removeAttribute("paramHtml");
                session.removeAttribute("reportName");
                session.removeAttribute("isEdit");

                schedule.setReportId(Integer.parseInt(reportId));
                schedule.setSchedulerName(reportName);
                schedule.setParameterXml(paramDetails[0]);
                schedule.setParameter(paramHtml.toString());
                schedule.setFolderId(paramDetails[1]);
                allReportDetails = reptTemplateDao.getReportDetails(userId, paramDetails[1]);
                reportDetailsJson = dao.getAllReportDetails(reportId);
            } else {
                firstRoleId = roleMap.get("roleId");
                for (int i = 0; i < firstRoleId.size(); i++) {
                    allReportDetails = reptTemplateDao.getReportDetails(userId, firstRoleId.get(i));
                    ReportDetails reportDetails = allReportDetails.get(0);
                    reportDetailsJson = dao.getAllReportDetails(reportDetails.getReportId());
                }
            }
        } else {
            schedule = dao.getReportScheduleDetails(schedulerId, false);
            allReportDetails = reptTemplateDao.getReportDetails(userId, schedule.getFolderId());
            reportDetailsJson = dao.getAllReportDetails(Integer.toString(schedule.getReportId()));
        }

        String schedulerJson = gson.toJson(schedule);
        String reportJson = gson.toJson(allReportDetails);
        String completeJson = schedulerJson.concat("~").concat(roleJson).concat("~").concat(reportJson).concat("~").concat(reportDetailsJson);

        try {
            out = response.getWriter();
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }
        out.println(completeJson);

        return null;
    }

    public ActionForward deleteSchedule(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        SchedulerDAO dao = new SchedulerDAO();
        String reportId = request.getParameter("deleteSchedulerids");
        String[] idWithName = reportId.split(",");
        String[] scheduleId = new String[idWithName.length];
        String[] scheduleName = new String[idWithName.length];
        for (int i = 0; i < idWithName.length; i++) {
            String[] temp = idWithName[i].split("~");
            scheduleId[i] = temp[0];
            scheduleName[i] = temp[1];
        }
        dao.deleteSchedule(scheduleId, scheduleName);

        PrintWriter out = null;
        try {
            out = response.getWriter();
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }
        out.print("success");
        return null;
    }

    public ActionForward runReportScheduler(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        SchedulerBD bd = new SchedulerBD();
        String schedulerId = request.getParameter("schedulerId");
        boolean fromStudio = Boolean.parseBoolean(request.getParameter("fromStudio"));
//        
        String[] schedulerArr = schedulerId.split(",");
        for (String scheduleId : schedulerArr) {
            if (scheduleId != null) {
                bd.runScheduler(scheduleId, fromStudio);
            }
        }
        return null;
    }

    public ActionForward getDimDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        PrintWriter out = null;
        try {
            SchedulerDAO dao = new SchedulerDAO();
            String reportId = request.getParameter("reportId");
            String dimSelected = dao.getDimDetailsForReportId(reportId);
            out = response.getWriter();
            out.print(dimSelected);

        } catch (IOException ex) {
            logger.error("Exception:", ex);
        } finally {
            out.close();
        }
        return null;
    }

    public ActionForward uploadDimensionsForMail(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        PrintWriter out = null;
        String dimMembers = null;
        String[] dtime = {"Day", "Week", "Month", "Quarter", "Year"};
        TreeSet timeDimension = new TreeSet();

        try {
            for (String dtime1 : dtime) {
                timeDimension.add(dtime1);
            }
            SchedulerDAO dao = new SchedulerDAO();
            String dimId = request.getParameter("dimensionId");
            if (!timeDimension.contains(dimId)) {
                dimMembers = dao.getTrackerDimDetails(dimId);
            }
            out = response.getWriter();
            out.print(dimMembers);
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        } finally {
            out.close();
        }
        return null;
    }

    public ActionForward getAllReportDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String reportId = request.getParameter("reportId");
        SchedulerDAO dao = new SchedulerDAO();
        PrintWriter out;
        String result = dao.getAllReportDetails(reportId);
        try {
            out = response.getWriter();
            out.print(result);
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }
        return null;
    }

    public ActionForward getReportParameters(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        String reportId = request.getParameter("reportId");
        String reportName = request.getParameter("reportName");
        String from = request.getParameter("from");
        Container container = null;
        container = Container.getContainerFromSession(request, reportId);
        PbReportCollection collect = container.getReportCollect();
        String[] rolesArr = collect.reportBizRoles;
        Set<ReportParameterValue> reportParameterVal = container.getReportParameter().getReportParameterValues();
        ArrayList<String> paramNames = new ArrayList<String>();
        StringBuilder paramHtml = new StringBuilder();
        paramNames = (ArrayList) container.getParametersHashMap().get("ParametersNames");
        Iterator<ReportParameterValue> paramsItr = reportParameterVal.iterator();
        int i = 0;
        while (paramsItr.hasNext()) {
            paramHtml.append("<font style=\"font-weight:bold;font-size:11px\" class='wordStyle' id='paramNames' name='paramName'>").append(paramNames.get(i)).append(" : </font>");
            paramHtml.append("<font class='wordStyle' id='paramValues' name='paramValue'>").append(paramsItr.next().getParameterValues()).append("</font>").append("&nbsp&nbsp&nbsp&nbsp");
            i++;
        }
        StringBuilder repParamXml = container.getReportParameter().toXml();
        repParamXml.append("~").append(rolesArr[0]);
        session.setAttribute("paramXML", repParamXml);
        session.setAttribute("paramHtml", paramHtml);
        session.setAttribute("isEdit", false);
        session.setAttribute("reportId", reportId);
        session.setAttribute("reportName", reportName);
        session.setAttribute("fromReport", true);

        if (from.equalsIgnoreCase("scheduler")) {
            return mapping.findForward("reportScheduler");
        } else {
            return mapping.findForward("reportTracker");
        }
    }

    public ActionForward checkUserAvailibility(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String viewById = request.getParameter("reportViewBy");
        PrintWriter out = null;
        HttpSession session = request.getSession(false);
        SchedulerDAO dao = new SchedulerDAO();
        String[] viewByIds = viewById.split(",");
        List<UserDimensionMap> userMapList = dao.getUserDetails(viewByIds[0]);
        session.setAttribute("userDimMap", userMapList);
        try {
            out = response.getWriter();
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }
        if (!(userMapList.isEmpty())) {
            out.print("success");
        } else {
            out.print("NotExists");
        }
        return null;
    }

    public ActionForward getAllBusRoles(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);

        String userId = (String) session.getAttribute("USERID");
        ScorecardDesignBd scoreBd = new ScorecardDesignBd();
        String busRoles = scoreBd.getUserFoldersByUserId(userId);

        try {
            response.getWriter().print(busRoles);
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }
        return null;
    }

    public ActionForward saveTrackerConditions(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        String dimValue = request.getParameter("dimValue");
        String[] condOperator = request.getParameterValues("condOp");
        String[] condStrtVal = request.getParameterValues("sCondVal");
        String[] condEndVal = request.getParameterValues("eCondVal");
        String[] condMail = request.getParameterValues("condMail");
        String fromFlag = request.getParameter("fromFlag");     //for targetBasis
        String condTargetVal = "";
        String condDevPerVal = "";
        boolean fromFlagTr = Boolean.parseBoolean(fromFlag);
        if (request.getParameter("targetVal") != null) {
            condTargetVal = request.getParameter("targetVal");
        }
        if (request.getParameter("deviationVal") != null) {
            condDevPerVal = request.getParameter("deviationVal");     //for targetBasis
        }
        String sendAnywayChck = request.getParameter("sendChckArr");
        String[] sendArr = sendAnywayChck.split(",");

        ArrayListMultimap<String, TrackerCondition> trackerCondMap = null;
        TrackerCondition trackerCondition = null;
        if (session.getAttribute("TrackerCondition") != null) {
            trackerCondMap = (ArrayListMultimap<String, TrackerCondition>) session.getAttribute("TrackerCondition");
            if (trackerCondMap.containsKey(dimValue)) {
                trackerCondMap.removeAll(dimValue);
            }
        } else {
            trackerCondMap = ArrayListMultimap.create();
        }

        for (int i = 0; i < condMail.length; i++) {
            trackerCondition = new TrackerCondition();
            trackerCondition.setViewByValue(dimValue);
            if (condTargetVal != null && !condTargetVal.equals("")) //for targetBasis
            {
                trackerCondition.setTargetValue(Double.parseDouble(condTargetVal));
            }
            if (condDevPerVal != null && !condDevPerVal.equals("")) {
                trackerCondition.setDeviationPerVal(Double.parseDouble(condDevPerVal));      //for targetBasis
            }
            trackerCondition.setOperator(condOperator[i]);
            if (condStrtVal[i] != null && !condStrtVal[i].equals("")) {
                trackerCondition.setMeasureStartValue(Double.parseDouble(condStrtVal[i]));
            }
            if (condOperator[i].equalsIgnoreCase("<>")) {
                trackerCondition.setMeasureEndValue(Double.parseDouble(condEndVal[i]));
            }
            trackerCondition.setMailIds(condMail[i]);
            trackerCondition.setFromFlagTarget(fromFlagTr);
//                if(sendArr[i]!=null && !sendArr[i].equalsIgnoreCase(""))
//                trackerCondition.setSendAnywayCheck(Boolean.parseBoolean(sendArr[i]));
            trackerCondMap.put(dimValue, trackerCondition);
        }
        session.setAttribute("TrackerCondition", trackerCondMap);
        return null;
    }

    public ActionForward getTrckerCondFrmSession(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        ArrayListMultimap<String, TrackerCondition> trackerCond = (ArrayListMultimap<String, TrackerCondition>) session.getAttribute("TrackerCondition");
        HashMap<String, List<TrackerCondition>> trackerMap = new HashMap<String, List<TrackerCondition>>();
        String trackerCondJson = "";
        String completeJson = "";
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }
        StringBuilder dimStr = new StringBuilder();
        if (trackerCond != null) {
            Set condSet = trackerCond.keySet();
            Iterator condItr = condSet.iterator();
            Gson gson = new Gson();
            int i = 0;
            StringBuilder dimValStr = new StringBuilder();

            while (condItr.hasNext()) {
                String dimValue = (String) condItr.next();
                dimValStr.append("," + "\"").append(dimValue).append("\"");
                List<TrackerCondition> trackerCondition = trackerCond.get(dimValue);
                trackerMap.put(dimValue, trackerCondition);
                i++;
            }
            trackerCondJson = gson.toJson(trackerMap);
            dimValStr.replace(0, 1, "");
            dimStr.append("{");
            dimStr.append("\"dimValues\":[").append(dimValStr.toString()).append("]}");
        }
        completeJson = trackerCondJson.concat("~").concat(dimStr.toString());
        writer.print(completeJson);
        return null;
    }

    public ActionForward getScoreCardsForScheduler(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        String folderId = request.getParameter("folderId");
        String userId = (String) session.getAttribute("USERID");
        SchedulerDAO dao = new SchedulerDAO();
        String scorecards = dao.getScorecards(userId, folderId);
        try {
            response.getWriter().print(scorecards);
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }
        return null;
    }

    public ActionForward getScorecardTimeDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        String scardId = request.getParameter("scoreId");
        PrintWriter out = response.getWriter();
        ScorecardViewerBD viewerBD = new ScorecardViewerBD();
        String userId = (String) session.getAttribute("USERID");
//        String result = viewerBD.loadParams(scardId, userId);
        HashMap map = viewerBD.loadParams(scardId, userId);
        String result = viewerBD.dispayTimeParamsForScorecardTracker(map);
        out.print(result);
        return null;
    }

    public ActionForward saveScorecardTracker(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        String mode = request.getParameter("mode");
        String isEdit = request.getParameter("isEdit");
        if (!"disabled".equalsIgnoreCase(isEdit)) {
            ScorecardTracker tracker = null;
            if ("Edit".equalsIgnoreCase(mode)) {
                tracker = (ScorecardTracker) session.getAttribute("ScorecardTracker");
            } else {
                tracker = new ScorecardTracker();
            }
            Date sDate = null;
            Date eDate = null;
            Date asOfDate = null;
            String startDate = request.getParameter("startDate");
            String endDate = request.getParameter("endDate");
            String asOfDateStr = request.getParameter("CBO_AS_OF_DATE");
            String compareWith = request.getParameter("CBO_PRG_COMPARE");
            String period = request.getParameter("CBO_PRG_PERIOD_TYPE");
            DateFormat formatter;
            formatter = new SimpleDateFormat("MM/dd/yyyy");
            try {
                sDate = formatter.parse(startDate);
                eDate = formatter.parse(endDate);
                asOfDate = formatter.parse(asOfDateStr);
            } catch (ParseException ex) {
                logger.error("Exception:", ex);
            }
            String frequency = request.getParameter("frequency");
            String trackerFrequency = "";
            if (frequency.equalsIgnoreCase("1")) {
                trackerFrequency = "Daily";
            } else if (frequency.equalsIgnoreCase("2")) {
                trackerFrequency = "Monthly";
            } else if (frequency.equalsIgnoreCase("3")) {
                trackerFrequency = "Quarterly";
            } else {
                trackerFrequency = "Yearly";
            }
            String scheduledTime = "";
            if (Integer.parseInt(request.getParameter("frequency")) == 4) {
                scheduledTime = request.getParameter("alertMonth");
            } else if (Integer.parseInt(request.getParameter("frequency")) == 2) {
                scheduledTime = request.getParameter("alertDate");
            } else if (Integer.parseInt(request.getParameter("frequency")) == 1) {
                String hrs = request.getParameter("hrs");
                String mins = request.getParameter("mins");
                scheduledTime = hrs.concat(":").concat(mins);
            } else {
                scheduledTime = "";
            }
            tracker.setsCardId(Integer.parseInt(request.getParameter("scoreCard")));
            tracker.setTrackerName(request.getParameter("sTrackerName"));
            tracker.setFolderId(request.getParameter("buzzRole"));
            tracker.setUpdatedBy(Integer.parseInt(session.getAttribute("USERID").toString()));
            tracker.setCreatedBy(Integer.parseInt(session.getAttribute("USERID").toString()));
            tracker.setParticularDay(null);
            tracker.setStartDate(sDate);
            tracker.setEndDate(eDate);
            tracker.setScheduleTime(scheduledTime);
            tracker.setFrequency(frequency);
            tracker.setAsOfDate(asOfDate);
            tracker.setCompareWith(compareWith);
            tracker.setPeriod(period);
            String[] stValues = request.getParameterValues("stScore");
            String[] endValues = request.getParameterValues("endScore");
            String[] operators = request.getParameterValues("operator");
            String[] emails = request.getParameterValues("email");
            ScorecardTrackerRule trackerRule = null;
            List<ScorecardTrackerRule> ruleLst = new ArrayList<ScorecardTrackerRule>();
            for (int i = 0; i < stValues.length; i++) {
                trackerRule = new ScorecardTrackerRule();
                trackerRule.setStartValue(Integer.parseInt(stValues[i]));
                if (endValues[i] != null && !endValues[i].equalsIgnoreCase("")) {
                    trackerRule.setEndValue(Integer.parseInt(endValues[i]));
                }
                trackerRule.setOperator(operators[i]);
                trackerRule.setEmail(emails[i]);
                ruleLst.add(trackerRule);
            }
            tracker.setRuleLst(ruleLst);
            SchedulerDAO dao = new SchedulerDAO();
            if ("Edit".equalsIgnoreCase(mode)) {
                dao.deleteScorecardTracker(tracker);
            }
            dao.saveScorecardTracker(tracker, mode);
            SchedulerBD bd = new SchedulerBD();
            bd.scheduleScorecardTracker(tracker);
            session.setAttribute("ScorecardTracker", tracker);
        }
        return null;
    }

    public ActionForward editScorecardTracker(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        int sheduleId = Integer.parseInt(request.getParameter("schedulerId"));
        String isEdit = request.getParameter("isEdit");
        HttpSession session = request.getSession(false);
        SchedulerDAO dao = new SchedulerDAO();
        ScorecardTracker tracker = dao.editScorecardTracker(sheduleId);
        session.setAttribute("ScorecardTracker", tracker);
        Gson gson = new Gson();
        String json = gson.toJson(tracker);
        request.setAttribute("isEdit", isEdit);
        request.setAttribute("mode", "Edit");
        request.setAttribute("json", json);
//        
//        ScorecardTrackerJob job=new ScorecardTrackerJob();
//        job.sendScorecardDetails(tracker);
//        try {
//            response.getWriter().print(json);
//        } catch (IOException ex) {
//
//        }
        return mapping.findForward("ScorecardTracker");
    }

    public ActionForward runScorecardTracker(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        String from = request.getParameter("from");
        ScorecardTrackerJob job = new ScorecardTrackerJob();
        if ("ScorePage".equalsIgnoreCase(from)) {
            ScorecardTracker tracker = (ScorecardTracker) session.getAttribute("ScorecardTracker");
            job.sendScorecardDetails(tracker);
        } else if ("MetaPage".equalsIgnoreCase(from)) {
            SchedulerDAO dao = new SchedulerDAO();
            String scoreSchedIds = request.getParameter("trackerIds");
            String scheduleIds[] = scoreSchedIds.split(",");
            for (String schedId : scheduleIds) {
                ScorecardTracker tracker = dao.editScorecardTracker(Integer.parseInt(schedId));
                job.sendScorecardDetails(tracker);
            }
        }
        return null;
    }
}
