/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.scheduler.db;

import com.google.common.base.Joiner;
import com.google.common.collect.ArrayListMultimap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.progen.report.colorgroup.ColorCodeBuilder;
import com.progen.report.scorecard.tracker.ScorecardTracker;
import com.progen.report.scorecard.tracker.ScorecardTrackerRule;
import com.progen.scheduler.ReportSchedule;
import com.progen.scheduler.ReportSchedulePreferences;
import com.progen.scheduler.ReportScheduleSlices;
import com.progen.scheduler.UserDimensionMap;
import com.progen.scheduler.tracker.Tracker;
import com.progen.scheduler.tracker.TrackerCondition;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import org.apache.log4j.Logger;
import prg.db.OneViewLetDetails;
import prg.db.PbDb;
import prg.db.PbReturnObject;
import utils.db.ProgenConnection;

/**
 *
 * @author progen
 */
public class SchedulerDAO extends PbDb {

    ResourceBundle resourceBundle;
    private String KPIHtml;
    public static Logger logger = Logger.getLogger(SchedulerDAO.class);

    private ResourceBundle getResourceBundle() {
        if (this.resourceBundle == null) {
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                resourceBundle = new SchedulerResBundleSqlServer();
            } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                resourceBundle = new SchedulerResBundleMYSql();
            } else {
                resourceBundle = new SchedulerResBundle();
            }
        }

        return resourceBundle;
    }

    public String insertReportSchedule(ReportSchedule schedule) {
        String query = getResourceBundle().getString("addReportSchedule");
        Object[] objArr = new Object[21];
        objArr[0] = schedule.getFrequency();
        objArr[1] = schedule.getScheduledTime();
        objArr[2] = schedule.getSubscribers();
        objArr[3] = schedule.getViewBy();
        objArr[4] = schedule.getContenType();
        objArr[5] = schedule.getUserId();
        objArr[6] = schedule.getUserId();
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(schedule.getStartDate());
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
            objArr[7] = startCalendar.get(Calendar.YEAR) + "-" + (startCalendar.get(Calendar.MONTH) + 1) + "-" + startCalendar.get(Calendar.DATE);
        } else {
            objArr[7] = startCalendar.get(Calendar.DATE) + "-" + (startCalendar.get(Calendar.MONTH) + 1) + "-" + startCalendar.get(Calendar.YEAR);
        }

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(schedule.getEndDate());
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
            objArr[8] = startCalendar.get(Calendar.YEAR) + "-" + (startCalendar.get(Calendar.MONTH) + 1) + "-" + startCalendar.get(Calendar.DATE);
        } else {
            objArr[8] = endCalendar.get(Calendar.DATE) + "-" + (endCalendar.get(Calendar.MONTH) + 1) + "-" + endCalendar.get(Calendar.YEAR);
        }

        objArr[9] = schedule.getReportId();
        objArr[10] = schedule.getSchedulerName();
        if (schedule.getContenType().equalsIgnoreCase("kpihtml")) {
            Gson gson = new Gson();
            KPIScheduleHelper scheduleHelper = new KPIScheduleHelper();
            scheduleHelper.setDashBoardID(Integer.toString(schedule.getReportId()));
            scheduleHelper.setDashLetId(schedule.getDashLetId());
            scheduleHelper.setKpiMasterId(schedule.getKpiMasterId());
            scheduleHelper.setElementId(schedule.getElementID());
            objArr[11] = gson.toJson(scheduleHelper);
        } else {
            objArr[11] = schedule.getParameterXml();
        }
        objArr[12] = schedule.isIsAutoSplited();
        objArr[13] = schedule.getViewById();
        objArr[14] = schedule.getFolderId();
        objArr[15] = schedule.getParticularDay();
        objArr[16] = schedule.getViewById() + "," + schedule.getCheckedViewByIds();
        objArr[17] = schedule.getViewByName() + "," + schedule.getCheckViewByNames();
        if (schedule.getParameterXml() != null && !"".equals(schedule.getParameterXml())) {
            objArr[18] = 'Y';
        } else {
            objArr[18] = 'N';
        }

        objArr[19] = schedule.getDataSelection();
        Gson gson = new Gson();
        if (schedule.getDataSelectionTypes() != null) {
            String dataSelection = gson.toJson(schedule.getDataSelectionTypes());
            objArr[20] = dataSelection;
        }
        String finalQuery = buildQuery(query, objArr);
//        
        try {
            execUpdateSQL(finalQuery);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }

        String sql = "select PRG_REPORT_SCHEDULER_SEQ.currval from dual";
        PbReturnObject retObj = null;
        try {
            retObj = execSelectSQL(sql);
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        String retVal = "";
        if (retObj != null) {
            retVal = retObj.getFieldValueString(0, 0);
        }
        return retVal;
    }

    public String getTrackerDimDetails(String dimId) {
        PbReturnObject retObj = null;
        String dimMem = "";
        try {
            Connection con = null;
            PbDb pbdb = new PbDb();
            String parentGrpValQuery = "";
            PbReturnObject vewdetspbro = null;
            String vewdetssql = getResourceBundle().getString("getDimMembers");
            Object[] objArr = new Object[1];
            objArr[0] = dimId;
            String finalQuery = buildQuery(vewdetssql, objArr);
            vewdetspbro = pbdb.execSelectSQL(finalQuery);
            String connectionId = String.valueOf(vewdetspbro.getFieldValueInt(0, "CONNECTION_ID"));
            String busstabName = String.valueOf(vewdetspbro.getFieldValueString(0, "BUSS_TABLE_NAME"));
            String memName = String.valueOf(vewdetspbro.getFieldValueString(0, "BUSS_COL_NAME"));
            parentGrpValQuery = "select distinct " + memName + "  from " + busstabName + " order by " + memName;
            con = ProgenConnection.getInstance().getConnectionByConId(connectionId);
            retObj = new PbReturnObject();
            try {
                retObj = super.execSelectSQL(parentGrpValQuery, con);

                String[] dimMember = new String[retObj.getRowCount()];

                if (retObj.getRowCount() > 0) {
                    for (int i = 0; i < retObj.getRowCount(); i++) {
                        dimMember[i] = retObj.getFieldValueString(i, 0);
                    }
                }
                dimMem = Joiner.on("~").join(dimMember);
            } catch (Exception ex) {
                logger.error("Exception:", ex);
            }
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        return dimMem;
    }

    public void insertTrackerSchedule(Tracker tracker) {
        String trackerquery = getResourceBundle().getString("addTrackerSchedule");
        Object[] objArr;
        objArr = new Object[19];
        objArr[0] = tracker.getTrackerName();
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(tracker.getStartDate());
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
            objArr[1] = startCalendar.get(Calendar.YEAR) + "-" + (startCalendar.get(Calendar.MONTH) + 1) + "-" + startCalendar.get(Calendar.DATE);
        } else {
            objArr[1] = startCalendar.get(Calendar.DATE) + "-" + (startCalendar.get(Calendar.MONTH) + 1) + "-" + startCalendar.get(Calendar.YEAR);
        }

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(tracker.getEndDate());
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
            objArr[2] = startCalendar.get(Calendar.YEAR) + "-" + (startCalendar.get(Calendar.MONTH) + 1) + "-" + startCalendar.get(Calendar.DATE);
        } else {
            objArr[2] = endCalendar.get(Calendar.DATE) + "-" + (endCalendar.get(Calendar.MONTH) + 1) + "-" + endCalendar.get(Calendar.YEAR);
        }

        objArr[3] = tracker.getFrequency();
        objArr[4] = tracker.getScheduledTime();
        objArr[5] = tracker.getMeasureId();
        objArr[6] = tracker.getViewById();
        objArr[7] = tracker.getMode();
        objArr[8] = tracker.getSubscribers();
        objArr[9] = tracker.getFolderId();
        objArr[10] = tracker.getUserId();
        objArr[11] = tracker.getUserId();
        objArr[12] = tracker.getCondOperator();
//            Double condVal = Double.parseDouble(tracker.getCondValue());
//            condVal = condVal/30;
//            objArr[13] = condVal;
        if (tracker.getCondValue().equalsIgnoreCase("")) {
            objArr[13] = 0;
        } else {
            objArr[13] = tracker.getCondValue();
        }
        objArr[14] = tracker.getSupportingMsr();
        objArr[15] = tracker.isAutoIdentify();
        objArr[16] = tracker.getSupportingMsrNames();
        objArr[17] = tracker.isSendAnyWay();

        if (tracker.getParameterXml() != null && !"".equals(tracker.getParameterXml())) {
            objArr[18] = "Y";
        } else {
            objArr[18] = "N";
        }
        String finalTrackerQuery = buildQuery(trackerquery, objArr);

        try {
            execUpdateSQL(finalTrackerQuery);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
    }

    public void insertTrackerCondition(Tracker tracker) {
        ArrayList queries = new ArrayList();
        String trckConditionQry = getResourceBundle().getString("addTrackerCondition");
        Object[] objArr2 = new Object[9];
        ArrayListMultimap<String, TrackerCondition> trackerCondMap = tracker.getTrackerConditions();
        Set condSet = trackerCondMap.keySet();
        Iterator condItr = condSet.iterator();
        while (condItr.hasNext()) {
            String dimValue = (String) condItr.next();
            List<TrackerCondition> trackerCondition = trackerCondMap.get(dimValue);
            for (TrackerCondition trackerCond : trackerCondition) {
                objArr2[0] = trackerCond.getViewByValue();
                objArr2[1] = trackerCond.getOperator();
                objArr2[2] = trackerCond.getMeasureStartValue();
                if (trackerCond.getOperator().equalsIgnoreCase("<>")) {
                    objArr2[3] = trackerCond.getMeasureEndValue();
                } else {
                    objArr2[3] = "null";
                }
                objArr2[4] = trackerCond.getMailIds();
                objArr2[5] = trackerCond.isSendAnywayCheck();
                objArr2[6] = trackerCond.getTargetValue();                //for targetBasis
                objArr2[7] = trackerCond.getDeviationPerVal();
                objArr2[8] = trackerCond.isFromFlagTarget();                    //for targetBasis
                String finalTrckCondQry = buildQuery(trckConditionQry, objArr2);
                queries.add(finalTrckCondQry);
            }
        }
        try {
            executeMultiple(queries);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
    }

    public Tracker getTracker(int trackerId, boolean isRun) {
        PbReturnObject retObj = null;
        PbReturnObject dimRetObj = null;
        Tracker tracker = null;
        String trackerIdQry = "";
        PbReturnObject currValObj = null;
        int currId = 0;
        PbReturnObject condRetObj = null;
        try {
            String query = getResourceBundle().getString("getTrackerDetails");
            String dimQry = getResourceBundle().getString("getDimensionName");
            String dimId = "";
            Object[] obj = new Object[1];
            if (isRun) {
                trackerIdQry = getResourceBundle().getString("getTrackerId");
                currValObj = new PbReturnObject();
                try {
                    currValObj = execSelectSQL(trackerIdQry);
                    obj[0] = currValObj.getFieldValueInt(0, 0);
                    currId = currValObj.getFieldValueInt(0, 0);
                } catch (SQLException ex) {
                    logger.error("Exception:", ex);
                }
            } else {
                obj[0] = trackerId;
            }

            String finalQuery = buildQuery(query, obj);
            retObj = execSelectSQL(finalQuery);
            tracker = new Tracker();

            if (isRun) {
                tracker.setTrackerId(currId);
            } else {
                tracker.setTrackerId(trackerId);
            }

            if (retObj.getRowCount() > 0) {
                dimId = retObj.getFieldValueString(0, "VIEWBY_ID");
                Object[] dimObj = new Object[1];
                dimObj[0] = dimId;
                String finalDimQry = buildQuery(dimQry, dimObj);
//                
                dimRetObj = execSelectSQL(finalDimQry);
                tracker.setTrackerName(retObj.getFieldValueString(0, "TRACKER_NAME"));
                tracker.setFrequency(retObj.getFieldValueString(0, "FREQUENCY"));
                tracker.setScheduledTime(retObj.getFieldValueString(0, "SCHEDULED_TIME"));
                tracker.setStartDate(retObj.getFieldValueDate(0, "START_DATE"));
                tracker.setEndDate(retObj.getFieldValueDate(0, "END_DATE"));
                tracker.setFolderId(retObj.getFieldValueString(0, "FOLDER_ID"));
                tracker.setMode(retObj.getFieldValueString(0, "TRACKER_MODE"));
                tracker.setSubscribers(retObj.getFieldValueString(0, "SUBSCRIBERS"));
                tracker.setMeasureId(retObj.getFieldValueString(0, "MEASURE_ID"));
                tracker.setMeasureName(retObj.getFieldValueString(0, "USER_COL_DESC"));
                tracker.setViewById(retObj.getFieldValueString(0, "VIEWBY_ID"));
                tracker.setUserId(retObj.getFieldValueString(0, "CREATED_BY"));
                tracker.setDimensionName(dimRetObj.getFieldValueString(0, "USER_COL_DESC"));
                tracker.setCondOperator(retObj.getFieldValueString(0, "OPERATOR"));
                tracker.setCondValue(retObj.getFieldValueString(0, "FILTER_VALUE"));
                tracker.setSupportingMsr(retObj.getFieldValueString(0, "SUPP_MEASURES"));
                tracker.setisAutoIdentify(Boolean.parseBoolean(retObj.getFieldValueString(0, "IS_AUTO_IDENTIFY")));
                tracker.setSupportingMsrNames(retObj.getFieldValueString(0, "SUPP_MEASURES_NAMES"));
                tracker.setSendAnyWay(Boolean.parseBoolean(retObj.getFieldValueString(0, "IS_SENDANYWAY")));
                TrackerCondition trackerCondition = null;
                if (isRun) {
                    condRetObj = this.getTrackerConditionDetails(currId);
                } else {
                    condRetObj = this.getTrackerConditionDetails(trackerId);
                }

                ArrayListMultimap<String, TrackerCondition> trackerCondMap = ArrayListMultimap.create();
                for (int i = 0; i < condRetObj.getRowCount(); i++) {
                    trackerCondition = new TrackerCondition();
                    trackerCondition.setViewByValue(condRetObj.getFieldValueString(i, "VIEWBY_VALUE"));
                    trackerCondition.setOperator(condRetObj.getFieldValueString(i, "CONDITION"));
                    trackerCondition.setMeasureStartValue(condRetObj.getFieldValueInt(i, "MEASURE_START_VALUE"));
                    trackerCondition.setMeasureEndValue(condRetObj.getFieldValueInt(i, "MEASURE_END_VALUE"));
                    trackerCondition.setMailIds(condRetObj.getFieldValueString(i, "MAIL_IDS"));
                    trackerCondition.setSendAnywayCheck(Boolean.parseBoolean(condRetObj.getFieldValueString(i, "IS_SENDANYNWAY")));
                    trackerCondition.setTargetValue(condRetObj.getFieldValueInt(i, "TARGET_VALUE"));                            //for targetBasis
                    trackerCondition.setDeviationPerVal(condRetObj.getFieldValueInt(i, "DEVIATION_PER_VALUE"));
                    trackerCondition.setFromFlagTarget(Boolean.parseBoolean(condRetObj.getFieldValueString(i, "TRACKER_FLAG")));   //for targetBasis

                    trackerCondMap.put(condRetObj.getFieldValueString(i, "VIEWBY_VALUE"), trackerCondition);
                }
                tracker.setTrackerConditions(trackerCondMap);
            }
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        return tracker;

    }

    public PbReturnObject getTrackerConditionDetails(int trackerId) {
        PbReturnObject retObj = null;
        try {
            String query = getResourceBundle().getString("getTrckConditionDetails");
            Object[] obj = new Object[1];
            obj[0] = trackerId;
            String finalQuery = buildQuery(query, obj);
            retObj = execSelectSQL(finalQuery);

        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        return retObj;
    }

    public void deleteTrackerDetails(String trackerId) {
        boolean retObj = false;

        String query = getResourceBundle().getString("deleteTrackerDetails");
        String trckrCondQry = getResourceBundle().getString("deleteTrackerConditions");
        Object[] obj = new Object[1];
        obj[0] = trackerId;
        ArrayList queries = new ArrayList();
        String finalQuery = buildQuery(query, obj);
        queries.add(finalQuery);
        String finalCondQuery = buildQuery(trckrCondQry, obj);
        queries.add(finalCondQuery);
        retObj = executeMultiple(queries);
    }

    public String getReports() {
        String query = getResourceBundle().getString("getReportName");
        PbReturnObject retObj = null;
        StringBuilder reportBuilder = new StringBuilder();
        StringBuilder reportId = new StringBuilder();
        StringBuilder reportName = new StringBuilder();
        try {
            retObj = execSelectSQL(query);
            for (int i = 0; i < retObj.getRowCount(); i++) {
                reportId.append("," + "\"").append(retObj.getFieldValueInt(i, 0)).append("\"");
                reportName.append("," + "\"").append(retObj.getFieldValueString(i, 1)).append("\"");
            }
            reportId.replace(0, 1, "");
            reportName.replace(0, 1, "");
            reportBuilder.append("{");
            reportBuilder.append("\"ReportId\":[").append(reportId.toString()).append("]");
            reportBuilder.append(",");
            reportBuilder.append("\"ReportName\":[").append(reportName.toString()).append("]");
            reportBuilder.append("}");

        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        return reportBuilder.toString();
    }

    public ReportSchedule getReportScheduleDetails(String scheduledReportId, boolean isRun) {

        String schedulerIdQry = "";
        String query = getResourceBundle().getString("getReportScheduleDetails");
        PbReturnObject retObj = null;
        Object[] obj = new Object[1];
        PbReturnObject currValObj = null;
        PbReturnObject schdPreferenceObj = null;
        Gson gson1 = new Gson();
        //Gson gson = new GsonBuilder().serializeNulls().create();
        PbReturnObject schdSliceObj = null;
        int currId = 0;
        StringBuilder paramStr = new StringBuilder();
        LinkedHashMap paramMap = null;
        String paramXml = null;
        if (isRun) {
            schedulerIdQry = getResourceBundle().getString("getSchedulerId");
            currValObj = new PbReturnObject();
            try {
                currValObj = execSelectSQL(schedulerIdQry);
                obj[0] = currValObj.getFieldValueInt(0, 0);
                currId = currValObj.getFieldValueInt(0, 0);
            } catch (SQLException ex) {
                logger.error("Exception:", ex);
            }
        } else {
            obj[0] = scheduledReportId;
        }
        String finalQuery = buildQuery(query, obj);
        ReportSchedule schedule = new ReportSchedule();
        try {
            retObj = execSelectSQL(finalQuery);
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }

        for (int i = 0; i < retObj.getRowCount(); i++) {
            if (isRun) {
                schedule.setReportScheduledId(currId);
            } else {
                schedule.setReportScheduledId(Integer.parseInt(scheduledReportId));
            }
            schedule.setReportId(Integer.parseInt(retObj.getFieldValueString(i, "REPORT_ID")));
            schedule.setStartDate(retObj.getFieldValueDate(i, "START_DATE"));
            schedule.setEndDate(retObj.getFieldValueDate(0, "END_DATE"));
            schedule.setFrequency(retObj.getFieldValueString(i, "FREQUENCY"));
            schedule.setScheduledTime(retObj.getFieldValueString(i, "SCHEDULER_TIME"));
            schedule.setSubscribers(retObj.getFieldValueString(i, "MAIL_ID"));
            schedule.setContenType(retObj.getFieldValueString(i, "CONTENT_TYPE"));
            schedule.setUserId(retObj.getFieldValueString(i, "CREATED_BY"));
            schedule.setIsAutoSplited(Boolean.parseBoolean(retObj.getFieldValueString(i, "IS_AUTOSPLIT")));
            schedule.setViewById(retObj.getFieldValueString(i, "VIEWBY_ID"));
            schedule.setFolderId(retObj.getFieldValueString(i, "FOLDER_ID"));
            schedule.setParticularDay(retObj.getFieldValueString(i, "PARTICULAR_DAY"));
            schedule.setCheckedViewByIds(retObj.getFieldValueString(i, "CHECKED_VIEWBY"));
            schedule.setCheckViewByNames(retObj.getFieldValueString(i, "CHECKED_VIEWBY_NAME"));
            schedule.setViewBy(retObj.getFieldValueString(i, "VIEW_BY"));
            schedule.setDataSelection(retObj.getFieldValueString(i, "DATA_SELECTION"));
            List<String> dataSelectionTypes = null;
            dataSelectionTypes = gson1.fromJson(retObj.getFieldValueString(i, "DATA_MULTIVALUES"), new TypeToken<ArrayList<String>>() {
            }.getType());
            schedule.setDataSelectionTypes(dataSelectionTypes);

            schedule.setSchedulerName(retObj.getFieldValueString(i, "SCHEDULER_NAME"));
            if (schedule.isIsAutoSplited()) {
                String[] viewByIds = schedule.getViewById().split(",");
                List<UserDimensionMap> userDimMap = this.getUserDetails(viewByIds[0]);
                schedule.setUsermap(userDimMap);
            }
            if (retObj.getFieldValueString(i, "CONTENT_TYPE").equalsIgnoreCase("kpihtml")) {

                Gson gson = new GsonBuilder().serializeNulls().create();
                KPIScheduleHelper kPIScheduleHelper = gson.fromJson(retObj.getFieldValueClobString(i, "PARAM_DETAIL"), KPIScheduleHelper.class);
                schedule.setkPIScheduleHelper(kPIScheduleHelper);
            } else {
                paramXml = retObj.getFieldValueClobString(i, "PARAM_DETAIL");
            }

            ColorCodeBuilder colorCodeBuilder = new ColorCodeBuilder();
            if (paramXml != null && !("null".equalsIgnoreCase(paramXml))) {
                schedule.setParameterXml(paramXml);
                paramMap = colorCodeBuilder.parseReportParamXML(paramXml);
                //if(paramMap!=null && !(paramMap.isEmpty()))

                StringBuilder idStr = new StringBuilder();
                StringBuilder valStr = new StringBuilder();
                Set paramSet = paramMap.keySet();
                Iterator paramItr = paramSet.iterator();
                while (paramItr.hasNext()) {
                    String id = (String) paramItr.next();
                    String[] value = paramMap.get(id).toString().split(",");
                    idStr.append("," + "\"").append(id).append("\"");
                    for (int j = 0; j < value.length; j++) {
                        valStr.append("," + "\"").append(value[j]).append("\"");
                    }

                    //  List<String> valList=Arrays.asList(value);
                }
                idStr.replace(0, 1, "");
                valStr.replace(0, 1, "");
                paramStr.append("{");
                paramStr.append("\"paramId\":[").append(idStr.toString()).append("]");
                paramStr.append(",");
                paramStr.append("\"paramName\":[").append(valStr.toString()).append("]");
                paramStr.append("}");
            }
            schedule.setParameter(paramStr.toString());

        }
        if (isRun) {
            schdPreferenceObj = this.getSchedulerPreferenceDetails(currId);
        } else {
            schdPreferenceObj = this.getSchedulerPreferenceDetails(Integer.parseInt(scheduledReportId));
        }

        List<ReportSchedulePreferences> schdPreferenceList = new ArrayList<ReportSchedulePreferences>();
        List<ReportScheduleSlices> schdSliceList = new ArrayList<ReportScheduleSlices>();
        ReportSchedulePreferences schedulePreferences;
        ReportScheduleSlices scheduleSlices;
        for (int i = 0; i < schdPreferenceObj.getRowCount(); i++) {
            schedulePreferences = new ReportSchedulePreferences();
            schedulePreferences.setDimValues(schdPreferenceObj.getFieldValueString(i, "DIM_VALS"));
            schedulePreferences.setMailIds(schdPreferenceObj.getFieldValueString(i, "MAIL_IDS"));
            schedulePreferences.setDimId(schdPreferenceObj.getFieldValueString(i, "DIM_ID"));
            schdPreferenceList.add(schedulePreferences);
        }
        if (schedule.getCheckedViewByIds() != null && !schedule.getCheckedViewByIds().equals("")) {
            String[] viewByIdsArr = schedule.getCheckedViewByIds().split(",");
            for (String viewById : viewByIdsArr) {
                if (!"".equals(viewById) && !viewById.equalsIgnoreCase(schedule.getViewById())) {
                    scheduleSlices = new ReportScheduleSlices();
                    scheduleSlices.setRowViewById(viewById);
                    scheduleSlices.setRowViewByVal("All");
                    schdSliceList.add(scheduleSlices);
                }
            }
        }

        schedule.setReportSchedulePrefrences(schdPreferenceList);
        schedule.setReportScheduleSlices(schdSliceList);

        return schedule;
    }

    public void deleteReportSchedule(String reportId) {

        String query = getResourceBundle().getString("deleteReportDetails");
        String schdPreferenceQry = getResourceBundle().getString("deleteSchdPrfrDetails");
        String schdSliceQry = getResourceBundle().getString("deleteSchdSliceDetails");
        ArrayList queries = new ArrayList();
        Object[] obj = new Object[1];
        obj[0] = reportId;
        String finalQuery = buildQuery(query, obj);
        queries.add(finalQuery);
        String finalPrfrQry = buildQuery(schdPreferenceQry, obj);
        queries.add(finalPrfrQry);
        String finalSliceQry = buildQuery(schdSliceQry, obj);
        queries.add(finalSliceQry);
        try {
            boolean flag = executeMultiple(queries);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }

    }

    public void deleteSchedule(String[] scheduleId, String[] scheduleName) {
        StringBuilder reportId = new StringBuilder();
        StringBuilder trackerId = new StringBuilder();
        StringBuilder scorecardTrackerId = new StringBuilder();
        for (int i = 0; i < scheduleName.length; i++) {
            if (scheduleName[i].equalsIgnoreCase("Scheduler")) {
                reportId.append(",").append(scheduleId[i]);
            }
            if (scheduleName[i].equalsIgnoreCase("Scorecard")) {
                scorecardTrackerId.append(",").append(scheduleId[i]);
            } else {
                trackerId.append(",").append(scheduleId[i]);
            }
        }
        reportId.replace(0, 1, "");
        trackerId.replace(0, 1, "");
        if (!scorecardTrackerId.toString().equalsIgnoreCase("")) {
            scorecardTrackerId.replace(0, 1, "");
        }

        if (!(reportId.toString().equalsIgnoreCase(""))) {
            deleteReportSchedule(reportId.toString());
        }
        if (!(trackerId.toString().equalsIgnoreCase(""))) {
            deleteTrackerDetails(trackerId.toString());
        }
        if (!scorecardTrackerId.toString().equalsIgnoreCase("")) {
            deleleScorecardTracker(scorecardTrackerId);
        }
    }

    public String getDimDetailsForReportId(String reportId) {
        String query = getResourceBundle().getString("getDimIdForReport");
        Object[] obj = new Object[1];
        obj[0] = reportId;
        String finalQuery = buildQuery(query, obj);
        PbReturnObject retObj = null;
        StringBuilder dimMembers = new StringBuilder();
        try {
            retObj = execSelectSQL(finalQuery);
            dimMembers.append(retObj.getFieldValueString(0, 0)).append(",").append(retObj.getFieldValueString(0, 1));
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }

        return dimMembers.toString();
    }

    public void insertReportSchedulePreferences(ReportSchedule schedule) {
        ArrayList queries = new ArrayList();
        String reptSchdPreferencesQry = getResourceBundle().getString("addReptSchdPreferences");
        Object[] objArr = new Object[4];
        List<ReportSchedulePreferences> schdPreferenceList = schedule.getReportSchedulePrefrences();

        for (ReportSchedulePreferences schedulePreferences : schdPreferenceList) {
            objArr[0] = schedulePreferences.getDimValues();
            objArr[1] = schedulePreferences.getMailIds();
            objArr[2] = schedulePreferences.getDimId();
            String finalPreferenceQry = buildQuery(reptSchdPreferencesQry, objArr);
            queries.add(finalPreferenceQry);
        }
        try {
            executeMultiple(queries);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
    }

    public PbReturnObject getSchedulerPreferenceDetails(int scheduleId) {
        PbReturnObject retObj = null;
        try {
            String query = getResourceBundle().getString("getSchdPreferenceDetails");
            Object[] obj = new Object[1];
            obj[0] = scheduleId;
            String finalQuery = buildQuery(query, obj);
            retObj = execSelectSQL(finalQuery);

        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        return retObj;
    }

    public String getAllReportDetails(String reportId) {
        String paramQuery = getResourceBundle().getString("getReportParams");
        String measureQuery = getResourceBundle().getString("getReportMeasures");
        String ViewByQuery = getResourceBundle().getString("getDimIdForReport");
        String ViewByQuery1 = getResourceBundle().getString("getDimIdForReport1");
        PbReturnObject paramRetObj = new PbReturnObject();
        PbReturnObject measureRetObj = new PbReturnObject();
        PbReturnObject ViewByRetObj = new PbReturnObject();
        PbReturnObject ViewByRetObj1 = new PbReturnObject();
        Object[] obj = new Object[1];
        obj[0] = reportId;
        StringBuilder reportBuilder = new StringBuilder();
        String finalParamQuery = buildQuery(paramQuery, obj);
        String finalMeasureQuery = buildQuery(measureQuery, obj);
        String finalViewByQuery = buildQuery(ViewByQuery, obj);
        String finalViewByQuery1 = buildQuery(ViewByQuery1, obj);
        try {
            paramRetObj = execSelectSQL(finalParamQuery);
            measureRetObj = execSelectSQL(finalMeasureQuery);
            ViewByRetObj = execSelectSQL(finalViewByQuery);
            ViewByRetObj1 = execSelectSQL(finalViewByQuery1);
            StringBuilder paramStr = new StringBuilder();
            StringBuilder paramId = new StringBuilder();
            StringBuilder measuresStr = new StringBuilder();
            StringBuilder viewByStr = new StringBuilder();
            StringBuilder viewById = new StringBuilder();
            StringBuilder coLViewByStr = new StringBuilder();
            for (int i = 0; i < paramRetObj.getRowCount(); i++) {
                paramStr.append("," + "\"").append(paramRetObj.getFieldValueString(i, "PARAM_DISP_NAME")).append("\"");
                paramId.append("," + "\"").append(paramRetObj.getFieldValueString(i, "ELEMENT_ID")).append("\"");

            }
            for (int j = 0; j < measureRetObj.getRowCount(); j++) {
                measuresStr.append("," + "\"").append(measureRetObj.getFieldValueString(j, "COL_DISP_NAME")).append("\"");
            }

            if (ViewByRetObj1 != null) {
                for (int k = 0; k < ViewByRetObj1.getRowCount(); k++) {
                    if (ViewByRetObj1.getFieldValueString(k, "DEFAULT_VALUE").equalsIgnoreCase("TIME")) {
                        viewByStr.append("," + "\"").append(ViewByRetObj1.getFieldValueString(k, "DEFAULT_VALUE")).append("\"");
                    }
                    viewById.append("," + "\"").append(ViewByRetObj1.getFieldValueString(k, "DEFAULT_VALUE")).append("\"");


                }
            }

            if (ViewByRetObj != null) {
                for (int k = 0; k < ViewByRetObj.getRowCount(); k++) {
                    if (ViewByRetObj.getFieldValueString(k, "IS_ROW_EDGE").equalsIgnoreCase("N")) {
                        coLViewByStr.append("," + "\"").append(ViewByRetObj.getFieldValueString(k, "DISP_NAME")).append("\"");
                    } else {
                        viewByStr.append("," + "\"").append(ViewByRetObj.getFieldValueString(k, "DISP_NAME")).append("\"");
                        viewById.append("," + "\"").append(ViewByRetObj.getFieldValueString(k, "ELEMENT_ID")).append("\"");
                    }

                }
            }
            paramStr.replace(0, 1, "");
            paramId.replace(0, 1, "");
            measuresStr.replace(0, 1, "");
            viewByStr.replace(0, 1, "");
            viewById.replace(0, 1, "");
            coLViewByStr.replace(0, 1, "");

            reportBuilder.append("{");
            reportBuilder.append("\"ParamName\":[").append(paramStr.toString()).append("]" + ",");
            reportBuilder.append("\"ParamId\":[").append(paramId.toString()).append("]" + ",");
            reportBuilder.append("\"MeasureName\":[").append(measuresStr.toString()).append("]" + ",");
            reportBuilder.append("\"ViewByName\":[").append(viewByStr.toString()).append("]" + ",");
            reportBuilder.append("\"ViewById\":[").append(viewById.toString()).append("]" + ",");
            reportBuilder.append("\"colViewByName\":[").append(coLViewByStr.toString()).append("]");
            reportBuilder.append("}");

        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        return reportBuilder.toString();

    }

    public void insertReportScheduleSlices(ReportSchedule schedule) {
        ArrayList queries = new ArrayList();
        String reptSchdSlicesQry = getResourceBundle().getString("addReptSchdSlices");
        Object[] objArr = new Object[2];
        List<ReportScheduleSlices> schdSlicesList = schedule.getReportScheduleSlices();

        for (ReportScheduleSlices scheduleSlices : schdSlicesList) {
            objArr[0] = scheduleSlices.getRowViewById();
            objArr[1] = scheduleSlices.getRowViewByVal();
            String finalSliceQry = buildQuery(reptSchdSlicesQry, objArr);
            queries.add(finalSliceQry);
        }
        try {
            executeMultiple(queries);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
    }

    public PbReturnObject getSchedulerSliceDetails(int scheduleId) {
        PbReturnObject retObj = null;
        try {
            String query = getResourceBundle().getString("getSchdSliceDetails");
            Object[] obj = new Object[1];
            obj[0] = scheduleId;
            String finalQuery = buildQuery(query, obj);
            retObj = execSelectSQL(finalQuery);

        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        return retObj;
    }

    public List<UserDimensionMap> getUserDetails(String viewById) {
        String query = getResourceBundle().getString("getUserDetails");
        PbReturnObject retObj = null;
        List<UserDimensionMap> userMapList = new ArrayList<UserDimensionMap>();
        UserDimensionMap userMap = null;
        Object[] obj = new Object[1];
        obj[0] = viewById;
        String finalQry = buildQuery(query, obj);
        try {
            retObj = execSelectSQL(finalQry);
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        for (int i = 0; i < retObj.getRowCount(); i++) {
            userMap = new UserDimensionMap();
            userMap.setDimensionId(viewById);
            userMap.setDimensionValue(retObj.getFieldValueString(i, "DIM_VALUE"));
            userMap.setMailId(retObj.getFieldValueString(i, "MAIL_ID"));
            userMap.setUserId(Integer.parseInt(retObj.getFieldValueString(i, "USER_ID")));
            userMap.setUserName(retObj.getFieldValueString(i, "USER_NAME"));
            if (retObj.getFieldValueString(i, "MOBILE_NO") != null && !retObj.getFieldValueString(i, "MOBILE_NO").equals("")) {
                userMap.setMobileNo(Integer.parseInt(retObj.getFieldValueString(i, "MOBILE_NO")));
            }
            userMapList.add(userMap);
        }
        return userMapList;
    }

    public String getScorecards(String userId, String folderId) {
        String query = getResourceBundle().getString("getScoreCards");
        Object bindObj[] = new Object[2];
        bindObj[0] = userId;
        bindObj[1] = folderId;
        StringBuilder builder = new StringBuilder();
        try {
            PbReturnObject pbro = execSelectSQL(buildQuery(query, bindObj));
            if (pbro != null) {
                for (int i = 0; i < pbro.getRowCount(); i++) {
                    builder.append("<option value=").append(pbro.getFieldValueInt(i, 0)).append(">").append(pbro.getFieldValueString(i, 1)).append("</option>");
                }
            }
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        return builder.toString();
    }

    public void saveScorecardTracker(ScorecardTracker tracker, String mode) {
        String insertQry = getResourceBundle().getString("insertScorecardTracker");
        String seqQry = getResourceBundle().getString("PRG_AR_SCORECARD_TRACKER_SEQ");

        Object bindObj[] = null;
        String finalQry = "";
        int trackerId = 0;
        Calendar startCalendar = Calendar.getInstance();
        Calendar asOfDateCalendar = Calendar.getInstance();
        startCalendar.setTime(tracker.getStartDate());
        String stDate = startCalendar.get(Calendar.DATE) + "-" + startCalendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()) + "-" + startCalendar.get(Calendar.YEAR);
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(tracker.getEndDate());
        asOfDateCalendar.setTime(tracker.getAsOfDate());
        String endDate = endCalendar.get(Calendar.DATE) + "-" + startCalendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()) + "-" + endCalendar.get(Calendar.YEAR);
        String asOfDate = asOfDateCalendar.get(Calendar.DATE) + "-" + asOfDateCalendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()) + "-" + asOfDateCalendar.get(Calendar.YEAR);

        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
            bindObj = new Object[15];
            bindObj[0] = tracker.getsCardId();
            bindObj[1] = tracker.getTrackerName();
            bindObj[2] = "sysdate";
            bindObj[3] = tracker.getCreatedBy();
            bindObj[4] = "sysdate";
            bindObj[5] = tracker.getCreatedBy();
            bindObj[6] = tracker.getFrequency();
            bindObj[7] = tracker.getScheduleTime();
            bindObj[8] = stDate;
            bindObj[9] = endDate;
            bindObj[10] = tracker.getFolderId();
            bindObj[11] = "";
            bindObj[12] = asOfDate;
            bindObj[13] = tracker.getCompareWith();
            bindObj[14] = tracker.getPeriod();
            try {
                trackerId = insertAndGetSequenceInSQLSERVER(finalQry, "PRG_AR_SCORECARD_TRACKER");
            } catch (Exception ex) {
                logger.error("Exception:", ex);
            }
        } else {
            try {
                bindObj = new Object[16];
                PbReturnObject pbroSeq = execSelectSQL(seqQry);
                trackerId = pbroSeq.getFieldValueInt(0, 0);
                bindObj[0] = trackerId;
                bindObj[1] = tracker.getsCardId();
                bindObj[2] = tracker.getTrackerName();
                if ("Edit".equalsIgnoreCase(mode)) {
                    Calendar createdDate = Calendar.getInstance();
                    createdDate.setTime(tracker.getStartDate());
                    String date = createdDate.get(Calendar.DATE) + "-" + createdDate.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()) + "-" + createdDate.get(Calendar.YEAR);

                    bindObj[3] = "'" + date + "'";
                    bindObj[4] = tracker.getCreatedBy();
                } else {
                    bindObj[3] = "sysdate";
                    bindObj[4] = tracker.getCreatedBy();
                }

                bindObj[5] = "sysdate";
                bindObj[6] = tracker.getUpdatedBy();
                bindObj[7] = tracker.getFrequency();
                bindObj[8] = tracker.getScheduleTime();
                bindObj[9] = stDate;
                bindObj[10] = endDate;
                bindObj[11] = tracker.getFolderId();
                bindObj[12] = "";
                bindObj[13] = asOfDate;
                bindObj[14] = tracker.getCompareWith();
                bindObj[15] = tracker.getPeriod();
                finalQry = buildQuery(insertQry, bindObj);

                execUpdateSQL(finalQry);
            } catch (SQLException ex) {
                logger.error("Exception:", ex);
            } catch (Exception e) {
                logger.error("Exception:", e);
            }
        }
        tracker.setsCardShedId(trackerId);
        this.saveScorecardTrackerRules(tracker.getRuleLst(), trackerId);
    }

    void saveScorecardTrackerRules(List<ScorecardTrackerRule> ruleList, int trackerId) {
        String insertRulesQry = getResourceBundle().getString("insertScardRulesQry");

        ArrayList<String> queryList = new ArrayList<String>();
        Object[] bindObj = new Object[8];
        String finalQry = "";
        for (ScorecardTrackerRule rule : ruleList) {
            bindObj[0] = trackerId;
            bindObj[1] = rule.getOperator();
            bindObj[2] = rule.getStartValue();
            bindObj[3] = rule.getEndValue();
            bindObj[4] = rule.getEmail();
            bindObj[5] = 0;
            bindObj[6] = "";
            finalQry = buildQuery(insertRulesQry, bindObj);
            queryList.add(finalQry);

        }
        executeMultiple(queryList);
    }

    public ScorecardTracker editScorecardTracker(int scheduleId) {
        ScorecardTracker tracker = new ScorecardTracker();
        String masterQry = getResourceBundle().getString("editScorecardTracker");
        String rulesQry = getResourceBundle().getString("editScorecardTrackerRules");
        Object bindObj[] = new Object[1];
        bindObj[0] = scheduleId;
        try {
            PbReturnObject pbroTracker = execSelectSQL(buildQuery(masterQry, bindObj));
            if (pbroTracker.getRowCount() > 0) {
                tracker.setsCardShedId(scheduleId);
                tracker.setsCardId(pbroTracker.getFieldValueInt(0, "SCORECARD_ID"));
                tracker.setTrackerName(pbroTracker.getFieldValueString(0, "SCHEDULER_NAME"));
                tracker.setCreatedDate(pbroTracker.getFieldValueDate(0, "CREATED_DATE"));
                tracker.setCreatedBy(pbroTracker.getFieldValueInt(0, "CREATED_BY"));
                tracker.setUpDatedDate(pbroTracker.getFieldValueDate(0, "UPDATED_DATE"));
                tracker.setUpdatedBy(pbroTracker.getFieldValueInt(0, "UPDATED_BY"));
                tracker.setFrequency(pbroTracker.getFieldValueString(0, "FREQUENCY"));
                tracker.setScheduleTime(pbroTracker.getFieldValueString(0, "SCHEDULER_TIME"));
                tracker.setStartDate(pbroTracker.getFieldValueDate(0, "START_DATE"));
                tracker.setEndDate(pbroTracker.getFieldValueDate(0, "END_DATE"));
                tracker.setFolderId(pbroTracker.getFieldValueString(0, "FOLDER_ID"));
                tracker.setParticularDay(pbroTracker.getFieldValueString(0, "PARTICULAR_DAY"));
                tracker.setAsOfDate(pbroTracker.getFieldValueDate(0, "AS_OF_DATE"));
                tracker.setCompareWith(pbroTracker.getFieldValueString(0, "COMPARE_WITH"));
                tracker.setPeriod(pbroTracker.getFieldValueString(0, "PERIOD"));

                PbReturnObject pbroRules = execSelectSQL(buildQuery(rulesQry, bindObj));
                ScorecardTrackerRule rule;
                List<ScorecardTrackerRule> ruleLst = new ArrayList<ScorecardTrackerRule>();
                for (int i = 0; i < pbroRules.getRowCount(); i++) {
                    rule = new ScorecardTrackerRule();
                    rule.setOperator(pbroRules.getFieldValueString(i, "OPERATOR"));
                    rule.setStartValue(Double.parseDouble(pbroRules.getFieldValueString(i, "START_VALUE")));
                    rule.setEndValue(Double.parseDouble(pbroRules.getFieldValueString(i, "END_VALUE")));
                    rule.setEmail(pbroRules.getFieldValueString(i, "EMAIL_ID"));
                    rule.setPhoneNo(pbroRules.getFieldValueInt(i, "PHONE_NO"));
                    rule.setContentType(pbroRules.getFieldValueString(i, "CONTENT_TYPE"));
                    rule.setSendType(pbroRules.getFieldValueString(i, "SEND_TYPE"));
                    ruleLst.add(rule);
                }
                tracker.setRuleLst(ruleLst);
            }

        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        return tracker;
    }

    public void deleteScorecardTracker(ScorecardTracker tracker) {
        String masterQry = getResourceBundle().getString("deleteScorecardTracker");
        String rulesQry = getResourceBundle().getString("deleteScorecardTrackerRules");
        String qry = "select CREATED_DATE,CREATED_BY from PRG_AR_SCORECARD_TRACKER where SCORECARD_SCHEDULE_ID=" + tracker.getsCardShedId();
        try {
            PbReturnObject pbro = execSelectSQL(qry);
            if (pbro.getRowCount() > 0) {
                tracker.setCreatedBy(pbro.getFieldValueInt(0, "CREATED_BY"));
                tracker.setCreatedDate(pbro.getFieldValueDate(0, "CREATED_DATE"));
            }
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        Object bindObj[] = new Object[1];
        bindObj[0] = tracker.getsCardShedId();
        ArrayList<String> queries = new ArrayList<String>();
        queries.add(buildQuery(masterQry, bindObj));
        queries.add(buildQuery(rulesQry, bindObj));
        executeMultiple(queries);
    }

    private void deleleScorecardTracker(StringBuilder scorecardTrackerIds) {
        String masterQry = getResourceBundle().getString("deleteScorecardTracker");
        String rulesQry = getResourceBundle().getString("deleteScorecardTrackerRules");
        Object bindObj[] = new Object[1];
        bindObj[0] = scorecardTrackerIds;
        ArrayList<String> queries = new ArrayList<String>();
        queries.add(buildQuery(masterQry, bindObj));
        queries.add(buildQuery(rulesQry, bindObj));
        executeMultiple(queries);
    }

    public void setKPIHtml(String kpiHtml) {
        this.KPIHtml = kpiHtml;
    }

    //Surender
    public StringBuilder getOneviewMeasureHeader(OneViewLetDetails oneviewlet, String valu) {
        StringBuilder finalStringVal = new StringBuilder();

        finalStringVal.append("<table style='margin-left: 10px;width:100%;'>");
        finalStringVal.append("<tr >");
//            finalStringVal.append("<td width='2%'></td>");
        String reportName = oneviewlet.getRepName();
        String title = reportName;
        int strlength = (oneviewlet.getWidth() / 11);
        if (reportName.length() >= strlength) {
            reportName = reportName.substring(0, strlength - 3);
            reportName += "..";
        }
        if (valu != null) {
            finalStringVal.append("<td id=\"Dashlets" + oneviewlet.getNoOfViewLets() + "\" style='font-size:12pt;color:#000000;white-space:nowrap;'>");
            finalStringVal.append("<a style=\"\" href=\"javascript:submiturls12('" + valu + "')\"");
            finalStringVal.append("<strong id='forDillDown" + oneviewlet.getNoOfViewLets() + "' style=\"font-size: 12ptwhite-space:nowrap;;\" title=\"" + title + "\">" + reportName + "</strong></a>"); //oneviewlet.getRepName()
            finalStringVal.append("</td>");
        } else {
            finalStringVal.append("<td id=\"Dashlets" + oneviewlet.getNoOfViewLets() + "\" style='font-size:12pt;color:#000000;white-space:nowrap;' title=\"" + title + "\" >" + reportName + "</td>"); //oneviewlet.getRepName()
        }
        finalStringVal.append("<td id=\"refreshTabId" + oneviewlet.getNoOfViewLets() + "\" style='width:1%;align:right;'><a style=\"text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;font-size: 9pt;\" class=\"ui-icon ui-icon-arrowrefresh-1-w\" title=\"Refresh Region\" onclick=\"refreshOneVIewReg(" + oneviewlet.getNoOfViewLets() + ")\" href=\"#\"></a></td>");
        if (oneviewlet.getUserStatus()) {
            finalStringVal.append("<td id=\"saveTabId" + oneviewlet.getNoOfViewLets() + "\" style='width:1%;align:right;'><a style=\"text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;font-size: 9pt;\" class=\"ui-icon ui-icon-disk\" title=\"Save\" onclick=\"saveEachOneVIewReg(" + oneviewlet.getNoOfViewLets() + ")\" href=\"#\"></a></td>");
        }
        if (oneviewlet.getUserStatus()) {
            finalStringVal.append("<td id=\"optionId" + oneviewlet.getNoOfViewLets() + "\" style='width:1%;align:right;'>");
            finalStringVal.append("<a href='javascript:void(0)' class=\"ui-icon ui-icon-triangle-2-n-s\" onclick=\"selectforReadd(" + oneviewlet.getNoOfViewLets() + ")\"  style='text-decoration:none'  title=\"Region Options\"></a>");

            finalStringVal.append("<div id=\"reigonOptionsDivId" + oneviewlet.getNoOfViewLets() + "\" style='display:none;width:120px;height:auto;background-color:white;overflow:auto;position:absolute;text-align:left;border:1px solid #000000;'>");
            finalStringVal.append("<table border='0' align='left' >");
            finalStringVal.append("<tr><td>");

            finalStringVal.append("<table><tr><td><a href='javascript:void(0)' style='text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;' onclick=\"renameRegion('Dashlets-" + oneviewlet.getNoOfViewLets() + "'," + oneviewlet.getNoOfViewLets() + ",'Dashlets" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "')\" >Rename</a></td></tr></table>");
            finalStringVal.append("<table><tr><td><a href='javascript:void(0)' style='text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;' onclick=\"drillToReport('" + oneviewlet.getRepName() + "','" + oneviewlet.getRoleId() + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getReptype() + "')\"  >Drill</a></td></tr></table>");
            finalStringVal.append("<table><tr><td><a href='javascript:void(0)' style='text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;' onclick=\"customTimeAggregation('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "')\" title='customTimeAggregation' href='javascript:void(0)'>CustomTimeAggregation</a></td></tr></table>");

            //            finalStringVal.append("<table><tr><td><a href='javascript:void(0)' style='text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;' onclick=\"drillToReport('dashboard','" + oneviewlet.getRepName() + "','" + oneviewlet.getRoleId() + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "')\" >Drill To Dashboard</a></td></tr></table>");
//            finalStringVal.append("<table><tr><td><a href='javascript:void(0)' style='text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;' onclick=\"saveEachOneVIewReg("+oneviewlet.getNoOfViewLets()+")\"  >Save</a></td></tr></table>");
            finalStringVal.append("</td></tr>");
            finalStringVal.append("</table>");
            finalStringVal.append("</div>");
            finalStringVal.append("</td>");
        }


//            finalStringVal.append("<td id=\"regionId" + oneviewlet.getNoOfViewLets() + "\" style='height:10px;>");
        if (oneviewlet.getUserStatus()) {
            finalStringVal.append("<td id=\"optionId1" + oneviewlet.getNoOfViewLets() + "\" style='width:0px;align:right;'>");
            finalStringVal.append("<a href='javascript:void(0)' class=\"ui-icon ui-icon-plusthick\" onclick=\"selectReadd(" + oneviewlet.getNoOfViewLets() + ")\"  style='text-decoration:none'  title=\"Re Add Onevielet\"></a>");

            finalStringVal.append("<div id=\"readdDivId" + oneviewlet.getNoOfViewLets() + "\" style='display:none; width:90px; height:100px; background-color:#ffffff; overflow:auto;  position:absolute; text-align:left; border:1px solid #000000; border-top-width: 0px; z-index:1002;'>");
            finalStringVal.append("<table border='0' align='left' >");
            finalStringVal.append("<tr><td>");

            finalStringVal.append("<table><tr><td><a href='javascript:void(0)' style='text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;' onclick=\"selectedType('report','Dashlets-" + oneviewlet.getNoOfViewLets() + "'," + oneviewlet.getNoOfViewLets() + "," + oneviewlet.getWidth() + "," + oneviewlet.getHeight() + ",'Dashlets" + oneviewlet.getNoOfViewLets() + "','GrpTyp" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "')\" >Reports</a></td></tr></table>");
            finalStringVal.append("<table><tr><td><a href='javascript:void(0)' style='text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;' onclick=\"selectedType('measures','Dashlets-" + oneviewlet.getNoOfViewLets() + "'," + oneviewlet.getNoOfViewLets() + "," + oneviewlet.getWidth() + "," + oneviewlet.getHeight() + ",'Dashlets" + oneviewlet.getNoOfViewLets() + "','GrpTyp" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "')\" >Measures</a></td></tr></table>");

            finalStringVal.append("<table><tr><td><a href='javascript:void(0)' style='text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;' onclick=\"selectedType('dashboard','Dashlets-" + oneviewlet.getNoOfViewLets() + "'," + oneviewlet.getNoOfViewLets() + "," + oneviewlet.getWidth() + "," + oneviewlet.getHeight() + ",'Dashlets" + oneviewlet.getNoOfViewLets() + "','GrpTyp" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "')\" >KPIs</a></td></tr></table>");
            finalStringVal.append("<table><tr><td><a href='javascript:void(0)' style='text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;' onclick=\"selectedType('headline','Dashlets-" + oneviewlet.getNoOfViewLets() + "'," + oneviewlet.getNoOfViewLets() + "," + oneviewlet.getWidth() + "," + oneviewlet.getHeight() + ",'Dashlets" + oneviewlet.getNoOfViewLets() + "','GrpTyp" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "')\" >Headlines</a></td></tr></table>");
            finalStringVal.append("<table><tr><td><a href='javascript:void(0)' style='text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;white-space:nowrap;' onclick=\"selectedType('complexkpi','Dashlets-" + oneviewlet.getNoOfViewLets() + "'," + oneviewlet.getNoOfViewLets() + "," + oneviewlet.getWidth() + "," + oneviewlet.getHeight() + ",'Dashlets" + oneviewlet.getNoOfViewLets() + "','GrpTyp" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "')\" >Custom KPI</a></td></tr></table>");
            finalStringVal.append("<table><tr><td><a href='javascript:void(0)' style='text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;white-space:nowrap;' onclick=\"selectedType('Date','Dashlets-" + oneviewlet.getNoOfViewLets() + "'," + oneviewlet.getNoOfViewLets() + "," + oneviewlet.getWidth() + "," + oneviewlet.getHeight() + ",'Dashlets" + oneviewlet.getNoOfViewLets() + "','GrpTyp" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "')\" >Date</a></td></tr></table>");
            finalStringVal.append("<table><tr><td><a href='javascript:void(0)' style='text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;white-space:nowrap;' onclick=\"selectedType('notes','Dashlets-" + oneviewlet.getNoOfViewLets() + "'," + oneviewlet.getNoOfViewLets() + "," + oneviewlet.getWidth() + "," + oneviewlet.getHeight() + ",'Dashlets" + oneviewlet.getNoOfViewLets() + "','GrpTyp" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "')\" >Notes</a></td></tr></table>");
            finalStringVal.append("</td></tr>");
            finalStringVal.append("</table>");
            finalStringVal.append("</div>");
            finalStringVal.append("</td>");
        }
        finalStringVal.append("</tr></table>");
        return finalStringVal;
    }

    //Surender
    public StringBuilder getOneviewHeadLinesHeader(OneViewLetDetails oneviewlet, String valu) {
        StringBuilder finalStringVal = new StringBuilder();

        finalStringVal.append("<table style='margin-left: 10px;width:100%;'>");//kruthika
        finalStringVal.append("<tr >");
//            finalStringVal.append("<td width='2%'></td>");
        if (valu != null) {
            finalStringVal.append("<td id=\"Dashlets" + oneviewlet.getNoOfViewLets() + "\" style='font-size:12pt;color:#000000;white-space:nowrap;'>");
            finalStringVal.append("<a style=\"\" href=\"javascript:submiturls12('" + valu + "')\"");
            finalStringVal.append("<strong id='forDillDown" + oneviewlet.getNoOfViewLets() + "' style=\"font-size: 12pt;white-space:nowrap;\">" + oneviewlet.getRepName() + "</strong></a>");
            finalStringVal.append("</td>");
        } else if (!oneviewlet.getRepName().equalsIgnoreCase("HeadLines")) {
            finalStringVal.append("<td id=\"Dashlets" + oneviewlet.getNoOfViewLets() + "\" style='font-size:12pt;color:#000000;white-space:nowrap;'  >" + oneviewlet.getRepName() + "</td>");
        } else {
            finalStringVal.append("<td id=\"Dashlets" + oneviewlet.getNoOfViewLets() + "\" style='font-size:12pt;color:#000000;white-space:nowrap;'  >HeadLines</td>");
        }
        finalStringVal.append("<td id=\"refreshTabId" + oneviewlet.getNoOfViewLets() + "\" style='width:1%;align:right;'><a style=\"text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;font-size: 9pt;\" class=\"ui-icon ui-icon-arrowrefresh-1-w\" title=\"Refresh Region\" onclick=\"refreshOneVIewReg(" + oneviewlet.getNoOfViewLets() + ")\" href=\"#\"></a></td>");
        if (oneviewlet.getUserStatus()) {
            finalStringVal.append("<td id=\"saveTabId" + oneviewlet.getNoOfViewLets() + "\" style='width:1%;align:right;'><a style=\"text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;font-size: 9pt;\" class=\"ui-icon ui-icon-disk\" title=\"Save\" onclick=\"saveEachOneVIewReg(" + oneviewlet.getNoOfViewLets() + ")\" href=\"#\"></a></td>");
        }
        if (oneviewlet.getUserStatus()) {
            finalStringVal.append("<td id=\"optionId" + oneviewlet.getNoOfViewLets() + "\" style='width:1%;align:right;'>");
            finalStringVal.append("<a href='javascript:void(0)' class=\"ui-icon ui-icon-triangle-2-n-s\" onclick=\"selectforReadd(" + oneviewlet.getNoOfViewLets() + ")\"  style='text-decoration:none'  title=\"Region Options\"></a>");

            finalStringVal.append("<div id=\"reigonOptionsDivId" + oneviewlet.getNoOfViewLets() + "\" style='display:none;width:120px;height:auto;background-color:white;overflow:auto;position:absolute;text-align:left;border:1px solid #000000;'>");
            finalStringVal.append("<table border='0' align='left' >");
            finalStringVal.append("<tr><td>");

            finalStringVal.append("<table><tr><td><a href='javascript:void(0)' style='text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;' onclick=\"renameRegion('Dashlets-" + oneviewlet.getNoOfViewLets() + "'," + oneviewlet.getNoOfViewLets() + ",'Dashlets" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "')\" >Rename</a></td></tr></table>");
            finalStringVal.append("<table><tr><td><a href='javascript:void(0)' style='text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;' onclick=\"drillToReport('" + oneviewlet.getRepName() + "','" + oneviewlet.getRoleId() + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getReptype() + "')\"  >Drill</a></td></tr></table>");
//            finalStringVal.append("<table><tr><td><a href='javascript:void(0)' style='text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;' onclick=\"drillToReport('dashboard','" + oneviewlet.getRepName() + "','" + oneviewlet.getRoleId() + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "')\" >Drill To Dashboard</a></td></tr></table>");
//            finalStringVal.append("<table><tr><td><a href='javascript:void(0)' style='text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;' onclick=\"saveEachOneVIewReg("+oneviewlet.getNoOfViewLets()+")\"  >Save</a></td></tr></table>");
            finalStringVal.append("</td></tr>");
            finalStringVal.append("</table>");
            finalStringVal.append("</div>");
            finalStringVal.append("</td>");
        }


//            finalStringVal.append("<td id=\"regionId" + oneviewlet.getNoOfViewLets() + "\" style='height:10px;>");
        if (oneviewlet.getUserStatus()) {
            finalStringVal.append("<td id=\"optionId1" + oneviewlet.getNoOfViewLets() + "\" style='width:0px;align:right;'>");
            finalStringVal.append("<a href='javascript:void(0)' class=\"ui-icon ui-icon-plusthick\" onclick=\"selectReadd(" + oneviewlet.getNoOfViewLets() + ")\"  style='text-decoration:none'  title=\"Re Add Onevielet\"></a>");

            finalStringVal.append("<div id=\"readdDivId" + oneviewlet.getNoOfViewLets() + "\" style='display:none; width:90px; height:100px; background-color:#ffffff; overflow:auto;  position:absolute; text-align:left; border:1px solid #000000; border-top-width: 0px; z-index:1002;'>");
            finalStringVal.append("<table border='0' align='left' >");
            finalStringVal.append("<tr><td>");


            finalStringVal.append("<table><tr><td><a href='javascript:void(0)' style='text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;' onclick=\"selectedType('report','Dashlets-" + oneviewlet.getNoOfViewLets() + "'," + oneviewlet.getNoOfViewLets() + "," + oneviewlet.getWidth() + "," + oneviewlet.getHeight() + ",'Dashlets" + oneviewlet.getNoOfViewLets() + "','GrpTyp" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "')\" >Reports</a></td></tr></table>");
            finalStringVal.append("<table><tr><td><a href='javascript:void(0)' style='text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;' onclick=\"selectedType('measures','Dashlets-" + oneviewlet.getNoOfViewLets() + "'," + oneviewlet.getNoOfViewLets() + "," + oneviewlet.getWidth() + "," + oneviewlet.getHeight() + ",'Dashlets" + oneviewlet.getNoOfViewLets() + "','GrpTyp" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "')\" >Measures</a></td></tr></table>");

            finalStringVal.append("<table><tr><td><a href='javascript:void(0)' style='text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;' onclick=\"selectedType('dashboard','Dashlets-" + oneviewlet.getNoOfViewLets() + "'," + oneviewlet.getNoOfViewLets() + "," + oneviewlet.getWidth() + "," + oneviewlet.getHeight() + ",'Dashlets" + oneviewlet.getNoOfViewLets() + "','GrpTyp" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "')\" >KPIs</a></td></tr></table>");
            finalStringVal.append("<table><tr><td><a href='javascript:void(0)' style='text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;' onclick=\"selectedType('headline','Dashlets-" + oneviewlet.getNoOfViewLets() + "'," + oneviewlet.getNoOfViewLets() + "," + oneviewlet.getWidth() + "," + oneviewlet.getHeight() + ",'Dashlets" + oneviewlet.getNoOfViewLets() + "','GrpTyp" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "')\" >Headlines</a></td></tr></table>");
            finalStringVal.append("<table><tr><td><a href='javascript:void(0)' style='text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;white-space:nowrap;' onclick=\"selectedType('complexkpi','Dashlets-" + oneviewlet.getNoOfViewLets() + "'," + oneviewlet.getNoOfViewLets() + "," + oneviewlet.getWidth() + "," + oneviewlet.getHeight() + ",'Dashlets" + oneviewlet.getNoOfViewLets() + "','GrpTyp" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "')\" >Custom KPI</a></td></tr></table>");
            // finalStringVal.append("<table><tr><td><a href='javascript:void(0)' style='text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;white-space:nowrap;' onclick=\"selectedType('Date','Dashlets-" + oneviewlet.getNoOfViewLets() + "'," + oneviewlet.getNoOfViewLets() + "," + oneviewlet.getWidth() + "," + oneviewlet.getHeight() + ",'Dashlets" + oneviewlet.getNoOfViewLets() + "','GrpTyp" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "')\" >Date</a></td></tr></table>");
            finalStringVal.append("<table><tr><td><a href='javascript:void(0)' style='text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;white-space:nowrap;' onclick=\"selectedType('notes','Dashlets-" + oneviewlet.getNoOfViewLets() + "'," + oneviewlet.getNoOfViewLets() + "," + oneviewlet.getWidth() + "," + oneviewlet.getHeight() + ",'Dashlets" + oneviewlet.getNoOfViewLets() + "','GrpTyp" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "')\" >Notes</a></td></tr></table>");
//                        finalStringVal.append("</td></tr></table>");

            finalStringVal.append("</td></tr>");
            finalStringVal.append("</table>");
            finalStringVal.append("</div>");
            finalStringVal.append("</td>");
        }
        finalStringVal.append("</tr></table>");
        return finalStringVal;
    }

    public StringBuilder getOneviewComplexKpiHeader(OneViewLetDetails oneviewlet, String valu, String curval) {
//        StringBuilder finalStringVal = new StringBuilder();
//
//          finalStringVal.append("<table style='margin-left: 10px;width:"+oneviewlet.getWidth()+"px;'>");
//            finalStringVal.append("<tr >");
////            finalStringVal.append("<td width='2%'></td>");
//               finalStringVal.append("<td id=\"Dashlets" + oneviewlet.getNoOfViewLets() + "\" style='font-size:12pt;color:#000000;white-space:nowrap;'  >"+oneviewlet.getRepName()+"</td>");
//
//
//
////            finalStringVal.append("<td id=\"regionId" + oneviewlet.getNoOfViewLets() + "\" style='height:10px;>");
//            finalStringVal.append("<td id=\"optionId"+oneviewlet.getNoOfViewLets()+"\" style='width:0px;align:right;'>");
//            finalStringVal.append("<a href='javascript:void(0)' class=\"ui-icon ui-icon-plusthick\" onclick=\"selectReadd("+oneviewlet.getNoOfViewLets()+")\"  style='text-decoration:none'  title=\"Re Add Oneviewlet\"></a>");
//
//            finalStringVal.append("<div id=\"readdDivId" + oneviewlet.getNoOfViewLets() + "\" style='display:none;width:70px;height:auto;background-color:white;overflow:auto;position:absolute;text-align:left;border:1px solid #000000;'>");
//            finalStringVal.append("<table border='0' align='left' >");
//            finalStringVal.append("<tr><td>");
//
//            finalStringVal.append("<table><tr><td><a href='javascript:void(0)' style='text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;' onclick=\"selectedType('report','Dashlets-" + oneviewlet.getNoOfViewLets() + "'," + oneviewlet.getNoOfViewLets() + "," + oneviewlet.getWidth() + "," + oneviewlet.getHeight() + ",'Dashlets" + oneviewlet.getNoOfViewLets() + "','GrpTyp" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "')\" >Reports</a></td></tr></table>");
//            finalStringVal.append("<table><tr><td><a href='javascript:void(0)' style='text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;' onclick=\"selectedType('measures','Dashlets-" + oneviewlet.getNoOfViewLets() + "'," + oneviewlet.getNoOfViewLets() + "," + oneviewlet.getWidth() + "," + oneviewlet.getHeight() + ",'Dashlets" + oneviewlet.getNoOfViewLets() + "','GrpTyp" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "')\" >Measures</a></td></tr></table>");
//
//            finalStringVal.append("<table><tr><td><a href='javascript:void(0)' style='text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;' onclick=\"selectedType('dashboard','Dashlets-" + oneviewlet.getNoOfViewLets() + "'," + oneviewlet.getNoOfViewLets() + "," + oneviewlet.getWidth() + "," + oneviewlet.getHeight() + ",'Dashlets" + oneviewlet.getNoOfViewLets() + "','GrpTyp" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "')\" >KPIs</a></td></tr></table>");
//            finalStringVal.append("<table><tr><td><a href='javascript:void(0)' style='text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;' onclick=\"selectedType('headline','Dashlets-" + oneviewlet.getNoOfViewLets() + "'," + oneviewlet.getNoOfViewLets() + "," + oneviewlet.getWidth() + "," + oneviewlet.getHeight() + ",'Dashlets" + oneviewlet.getNoOfViewLets() + "','GrpTyp" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "')\" >Headlines</a></td></tr></table>");
//            finalStringVal.append("<table><tr><td><a href='javascript:void(0)' style='text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;' onclick=\"selectedType('complexkpi','Dashlets-" + oneviewlet.getNoOfViewLets() + "'," + oneviewlet.getNoOfViewLets() + "," + oneviewlet.getWidth() + "," + oneviewlet.getHeight() + ",'Dashlets" + oneviewlet.getNoOfViewLets() + "','GrpTyp" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "')\" >Custom kpi</a></td></tr></table>");
//           // finalStringVal.append("<table><tr><td><a href='javascript:void(0)' style='text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;' onclick=\"selectedType('Date','Dashlets-" + oneviewlet.getNoOfViewLets() + "'," + oneviewlet.getNoOfViewLets() + "," + oneviewlet.getWidth() + "," + oneviewlet.getHeight() + ",'Dashlets" + oneviewlet.getNoOfViewLets() + "','GrpTyp" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "')\" >Date</a></td></tr></table>");
//            finalStringVal.append("<table><tr><td><a href='javascript:void(0)' style='text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;' onclick=\"selectedType('notes','Dashlets-" + oneviewlet.getNoOfViewLets() + "'," + oneviewlet.getNoOfViewLets() + "," + oneviewlet.getWidth() + "," + oneviewlet.getHeight() + ",'Dashlets" + oneviewlet.getNoOfViewLets() + "','GrpTyp" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "')\" >Notes</a></td></tr></table>");
//            finalStringVal.append("</td></tr>");
//            finalStringVal.append("</table>");
//            finalStringVal.append("</div>");
//            finalStringVal.append("</td></tr></table>");
//        return finalStringVal;

        StringBuilder finalStringVal = new StringBuilder();

//         if(oneviewlet.getFontColor()!=null && !oneviewlet.getFontColor().isEmpty() ){
//             fontcolor=oneviewlet.getFontColor();
//         }
//         else
        String fontcolor = "#000000";
        finalStringVal.append("<table style='margin-left: 10px;width:100%;'>");
        finalStringVal.append("<tr >");
//            finalStringVal.append("<td width='2%'></td>");
        String reportName = oneviewlet.getRepName();
        String title = reportName;
        int strlength = (oneviewlet.getWidth() / 11);
        if (reportName.length() >= strlength) {
            reportName = reportName.substring(0, strlength - 3);
            reportName += "..";
        }
        if (valu != null) {
            finalStringVal.append("<td id=\"Dashlets" + oneviewlet.getNoOfViewLets() + "\" style='font-size:12pt;color:000000;white-space:nowrap'>");
            finalStringVal.append("<a style=\"\" href=\"javascript:submiturls12('" + valu + "')\">");
            finalStringVal.append("<strong id='forDillDown" + oneviewlet.getNoOfViewLets() + "' style=\"font-size: 12pt;white-space:nowrap;font-weight: normal\" title=\"" + title + "\">" + reportName + "</strong></a>"); //oneviewlet.getRepName()
            finalStringVal.append("</td>");
        } else {
            finalStringVal.append("<td id=\"Dashlets" + oneviewlet.getNoOfViewLets() + "\" style='font-size:12pt;color:#000000;white-space:nowrap;' title=\"" + title + "\" >" + reportName + "</td>");//oneviewlet.getRepName()
        }
        finalStringVal.append("<td id=\"refreshTabId" + oneviewlet.getNoOfViewLets() + "\" style='width:1%;align:right;'><a style=\"text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;font-size: 9pt;\" class=\"ui-icon ui-icon-arrowrefresh-1-w\" title=\"Refresh Region\" onclick=\"refreshOneVIewReg(" + oneviewlet.getNoOfViewLets() + ")\" href=\"#\"></a></td>");
        if (oneviewlet.getUserStatus()) {
            finalStringVal.append("<td id=\"saveTabId" + oneviewlet.getNoOfViewLets() + "\" style='width:1%;align:right;'><a style=\"text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;font-size: 9pt;\" class=\"ui-icon ui-icon-disk\" title=\"Save\" onclick=\"saveEachOneVIewReg(" + oneviewlet.getNoOfViewLets() + ")\" href=\"#\"></a></td>");
        }
        if (oneviewlet.getUserStatus()) {
            finalStringVal.append("<td id=\"optionId" + oneviewlet.getNoOfViewLets() + "\" style='width:1%;align:right;'>");
            finalStringVal.append("<a href='javascript:void(0)' class=\"ui-icon ui-icon-triangle-2-n-s\" onclick=\"selectforReadd(" + oneviewlet.getNoOfViewLets() + ")\"  style='text-decoration:none'  title=\"Region Options\"></a>");

            finalStringVal.append("<div id=\"reigonOptionsDivId" + oneviewlet.getNoOfViewLets() + "\" style='display:none;width:120px;height:auto;background-color:white;overflow:auto;position:absolute;text-align:left;border:1px solid #000000;' class=\"overlapDiv\">");
            finalStringVal.append("<table border='0' align='left' >");
            finalStringVal.append("<tr><td>");

            finalStringVal.append("<table><tr><td><a href='javascript:void(0)' style='text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;' onclick=\"renameRegion('Dashlets-" + oneviewlet.getNoOfViewLets() + "'," + oneviewlet.getNoOfViewLets() + ",'Dashlets" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "')\" >Rename</a></td></tr></table>");
            finalStringVal.append("<table><tr><td><a href='javascript:void(0)' style='text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;' onclick=\"drillToReport('" + oneviewlet.getRepName() + "','" + oneviewlet.getRoleId() + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getReptype() + "')\"  >Drill</a></td></tr></table>");
//            finalStringVal.append("<table><tr><td><a href='javascript:void(0)' style='text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;' onclick=\"drillToReport('dashboard','" + oneviewlet.getRepName() + "','" + oneviewlet.getRoleId() + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "')\" >Drill To Dashboard</a></td></tr></table>");
            finalStringVal.append("<table><tr><td><a href='javascript:void(0)' style='text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;' onclick=\"measureOptionsForComplexKPI('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + curval + "','','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','')\" title='MeasuresOptions' href='javascript:void(0)'>Measure Option</a></td></tr></table>");
//            finalStringVal.append("<table><tr><td><a href='javascript:void(0)' style='text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;' onclick=\"saveEachOneVIewReg("+oneviewlet.getNoOfViewLets()+")\"  >Save</a></td></tr></table>");
            //finalStringVal.append("<table><tr><td><a href='javascript:void(0)' style='text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;' onclick=\"taggleNewMeasures('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + curval+ "','','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "')\" title='NewMeasureType' href='javascript:void(0)'>Toggle Display</a></td></tr></table>");
            //finalStringVal.append("<table><tr><td><a href='javascript:void(0)' style='text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;' onclick=\"measureTrendGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getRoleId()+ "','"+oneviewlet.getHeight()+"','" + oneviewlet.getWidth() + "','" + oneviewlet.getNoOfViewLets() + "','"+oneviewlet.getOneviewId()+"')\" title='MeasureTrend' href='javascript:void(0)'>Trend</a></td></tr></table>");
//            finalStringVal.append("<table><tr><td><a  style='text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;'  onclick=\"measureComments('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "')\" href='javascript:void(0)' title='Add/View Comments'>Comments</a></td></tr></table>");
//            if(currVal!=priorVal){
//            if(oneviewlet.isOneviewMeasureCompare()){
//            finalStringVal.append("<table><tr><td><input type='radio' onclick=\"measureNoCompare('nocomp"+oneviewlet.getNoOfViewLets()+"','" + oneviewlet.getRepName() + "','" + oneviewlet.getRoleId() + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "')\" valign='top' href='javascript:void(0)'    id='mesNoCompare"+oneviewlet.getNoOfViewLets()+"'></input>No-Comparision</td></tr></table>");
//            finalStringVal.append("<table><tr><td><input type='radio' onclick=\"measureCompare('comp"+oneviewlet.getNoOfViewLets()+"','" + oneviewlet.getRepName() + "','" + oneviewlet.getRoleId() + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "')\" valign='top' href='javascript:void(0)' checked=''  id='mesCompare"+oneviewlet.getNoOfViewLets()+"' ></input>Comparision</td></tr></table>");
//            }
//            else{
//            finalStringVal.append("<table><tr><td><input type='radio' onclick=\"measureNoCompare('nocomp"+oneviewlet.getNoOfViewLets()+"','" + oneviewlet.getRepName() + "','" + oneviewlet.getRoleId() + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "')\" valign='top' href='javascript:void(0)' checked=''  id='mesNoCompare"+oneviewlet.getNoOfViewLets()+"'></input>No-Comparision</td></tr></table>");
//            finalStringVal.append("<table><tr><td><input type='radio' onclick=\"measureCompare('comp"+oneviewlet.getNoOfViewLets()+"','" + oneviewlet.getRepName() + "','" + oneviewlet.getRoleId() + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "')\" valign='top' href='javascript:void(0)'  id='mesCompare"+oneviewlet.getNoOfViewLets()+"'  ></input>Comparision</td></tr></table>");
//            }
//            }
            finalStringVal.append("</td></tr>");
            finalStringVal.append("</table>");
            finalStringVal.append("</div>");
            finalStringVal.append("</td>");
        }

//            finalStringVal.append("<td id=\"regionId" + oneviewlet.getNoOfViewLets() + "\" style='height:10px;>");
        if (oneviewlet.getUserStatus()) {
            finalStringVal.append("<td id=\"optionId1" + oneviewlet.getNoOfViewLets() + "\" style='width:0px;align:right;'>");
            finalStringVal.append("<a href='javascript:void(0)' class=\"ui-icon ui-icon-plusthick\" onclick=\"selectReadd(" + oneviewlet.getNoOfViewLets() + ")\"  style='text-decoration:none'  title=\"Re Add Onevielet\"></a>");

            finalStringVal.append("<div id=\"readdDivId" + oneviewlet.getNoOfViewLets() + "\" style='display:none; width:90px; height:100px; background-color:#ffffff; overflow:auto;  position:absolute; text-align:left; border:1px solid #000000; border-top-width: 0px; z-index:1002;'>");
            finalStringVal.append("<table border='0' align='left' >");
            finalStringVal.append("<tr><td>");


            finalStringVal.append("<table><tr><td><a href='javascript:void(0)' style='text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;' onclick=\"selectedType('report','Dashlets-" + oneviewlet.getNoOfViewLets() + "'," + oneviewlet.getNoOfViewLets() + "," + oneviewlet.getWidth() + "," + oneviewlet.getHeight() + ",'Dashlets" + oneviewlet.getNoOfViewLets() + "','GrpTyp" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "')\" >Reports</a></td></tr></table>");
            finalStringVal.append("<table><tr><td><a href='javascript:void(0)' style='text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;' onclick=\"selectedType('measures','Dashlets-" + oneviewlet.getNoOfViewLets() + "'," + oneviewlet.getNoOfViewLets() + "," + oneviewlet.getWidth() + "," + oneviewlet.getHeight() + ",'Dashlets" + oneviewlet.getNoOfViewLets() + "','GrpTyp" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "')\" >Measures</a></td></tr></table>");

            finalStringVal.append("<table><tr><td><a href='javascript:void(0)' style='text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;' onclick=\"selectedType('dashboard','Dashlets-" + oneviewlet.getNoOfViewLets() + "'," + oneviewlet.getNoOfViewLets() + "," + oneviewlet.getWidth() + "," + oneviewlet.getHeight() + ",'Dashlets" + oneviewlet.getNoOfViewLets() + "','GrpTyp" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "')\" >KPIs</a></td></tr></table>");
            finalStringVal.append("<table><tr><td><a href='javascript:void(0)' style='text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;' onclick=\"selectedType('headline','Dashlets-" + oneviewlet.getNoOfViewLets() + "'," + oneviewlet.getNoOfViewLets() + "," + oneviewlet.getWidth() + "," + oneviewlet.getHeight() + ",'Dashlets" + oneviewlet.getNoOfViewLets() + "','GrpTyp" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "')\" >Headlines</a></td></tr></table>");
            finalStringVal.append("<table><tr><td><a href='javascript:void(0)' style='text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;white-space:nowrap;' onclick=\"selectedType('complexkpi','Dashlets-" + oneviewlet.getNoOfViewLets() + "'," + oneviewlet.getNoOfViewLets() + "," + oneviewlet.getWidth() + "," + oneviewlet.getHeight() + ",'Dashlets" + oneviewlet.getNoOfViewLets() + "','GrpTyp" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "')\" >Custom KPI</a></td></tr></table>");
            finalStringVal.append("<table><tr><td><a href='javascript:void(0)' style='text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;white-space:nowrap;' onclick=\"selectedType('Date','Dashlets-" + oneviewlet.getNoOfViewLets() + "'," + oneviewlet.getNoOfViewLets() + "," + oneviewlet.getWidth() + "," + oneviewlet.getHeight() + ",'Dashlets" + oneviewlet.getNoOfViewLets() + "','GrpTyp" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "')\" >Date</a></td></tr></table>");
            finalStringVal.append("<table><tr><td><a href='javascript:void(0)' style='text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;white-space:nowrap;' onclick=\"selectedType('notes','Dashlets-" + oneviewlet.getNoOfViewLets() + "'," + oneviewlet.getNoOfViewLets() + "," + oneviewlet.getWidth() + "," + oneviewlet.getHeight() + ",'Dashlets" + oneviewlet.getNoOfViewLets() + "','GrpTyp" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "')\" >Notes</a></td></tr></table>");
            finalStringVal.append("</td></tr>");
            finalStringVal.append("</table>");
            finalStringVal.append("</div>");
            finalStringVal.append("</td>");
        }
        finalStringVal.append("</tr></table>");
        return finalStringVal;
    }
}