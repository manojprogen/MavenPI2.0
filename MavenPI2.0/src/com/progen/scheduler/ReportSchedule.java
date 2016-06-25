/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.scheduler;

import com.progen.scheduler.db.KPIScheduleHelper;
import com.progen.scheduler.tracker.TrackerCondition;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import prg.db.Container;

/**
 *
 * @author progen
 */
public class ReportSchedule {

    private int reportScheduledId;
    private int reportId;
    private String[] reportIds;
    private String[] sheetNumbers;
    private String[] lineNumbers;
    private String[] colNumbers;
    private String[] headerValues;
    private String[] gTotals;
    private Date startDate;
    private Date endDate;
    private String frequency;
    private String dataSelection;
    private String scheduledTime = "00:00";
    private String viewBy;
    private String subscribers;
    private String contentType;
    private String userId;
    private List<ReportSchedulePreferences> reportSchedulePrefrences;
    private List<ReportScheduleSlices> reportScheduleSlices;
    private String schedulerName;
    private String parameterXml;
    private String parameter;
    private boolean isAutoSplited;
    private List<UserDimensionMap> usermap;
    private String viewById;
    private String viewByName;
    private String folderId;
    private String particularDay;
    private String checkedViewByIds;
    private String checkViewByNames;
    private KPIScheduleHelper kPIScheduleHelper;
    private String kpiMasterId;
    private String dashLetId;
    private String elementID;
    private List<String> dataSelectionTypes = new ArrayList<String>();
    private boolean runFlag;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private String PortalFileName;
    private String ReportmailIds;
    private boolean isReportSchedule;
    private boolean isExportReportSchedule;
    private int schedulerLoadId;
    private String schedulerLoadName;
    private String schedulerLoadType;
    private String schedulerLoadTruncType;
    private String schedulerLoadTime;
    private String schedulerFrequency;
    private Date schedulerStartDate;
    private Date schedulerEndDate;
    private String schedulerEmailId;
    private String oneviewFileName;
    private String alertDateType;
    private String alertDate;
    private String measureValueCurrVal;
    private String targetVal;
    private String deviationVal;
    private boolean fromOneview;
    private String oneviewsecurity;
    private boolean fromdsrbKpi;
    private boolean forConditonalTest;
    private boolean quickRefreshReport;
    private String reportAdvHtmlFileProps;
    private String contextPath;
    private String uploadedFilePath;
    private String uploadedFileName;
    private String advHtmlFileProps;
    private String oldAdvHtmlFileProps;
    private String oneviewletid;
    private String filename;
//    private ArrayList<String> evalue= new ArrayList<String>();
//    private ArrayList<String> svalue= new ArrayList<String>();
//    private ArrayList<String> symbolOperator= new ArrayList<String>();
//    private ArrayList<String> alertType= new ArrayList<String>();
//    private ArrayList<String> selectedHeaderNames= new ArrayList<String>();
    private String evalue[];
    private String svalue[];
    private String symbolOperator[];
    private String alertType[];
    private String selectedHeaderNames[];
    private int oneviewid;
    private List<String> oneviewMeasureOptions = new ArrayList<String>();
    private HashMap<Integer, TrackerCondition> measureAlertsConditions;
    private String[] snapshotUrls;
    private String filepath1;// added by sruthi for setpath after scheduling 22/12/2014
    public String localdata;//added by sruthi for schedular
    public String requestUrl;//added by sruthi for 
    private Date lSentDate;
    private String usrMesg = "";
    private String moduleType = "";
    private boolean isGO = false;
    //added by Dinanath for header logo as on 16/09/2015
    private String isHeaderLogoOn = "";
    private String isFooterLogoOn = "";
    private String isOptionalHeaderTextOn = "";
    private String isOptionalFooterTextOn = "";
    private String isHtmlSignatureOn = "";

    public void setIsHeaderLogoOn(String isHeaderLogoOn) {
        this.isHeaderLogoOn = isHeaderLogoOn;
    }

    public void setIsFooterLogoOn(String isFooterLogoOn) {
        this.isFooterLogoOn = isFooterLogoOn;
    }

    public void setIsOptionalHeaderTextOn(String isOptionalHeaderTextOn) {
        this.isOptionalHeaderTextOn = isOptionalHeaderTextOn;
    }

    public void setIsOptionalFooterTextOn(String isOptionalFooterTextOn) {
        this.isOptionalFooterTextOn = isOptionalFooterTextOn;
    }

    public void setIsHtmlSignatureOn(String isHtmlSignatureOn) {
        this.isHtmlSignatureOn = isHtmlSignatureOn;
    }

    public String getIsHeaderLogoOn() {
        return this.isHeaderLogoOn;
    }

    public String getIsFooterLogoOn() {
        return this.isFooterLogoOn;
    }

    public String getIsOptionalHeaderTextOn() {
        return this.isOptionalHeaderTextOn;
    }

    public String getIsOptionalFooterTextOn() {
        return this.isOptionalFooterTextOn;
    }

    public String getIsHtmlSignatureOn() {
        return this.isHtmlSignatureOn;
    }
//ended by Dinanath
    private HashMap GraphSizesDtlsHashMap = null;

    public String getReportmailIds() {
        return ReportmailIds;
    }

    public void setReportmailIds(String ReportmailIds) {
        this.ReportmailIds = ReportmailIds;
    }

    public String getPortalFileName() {
        return PortalFileName;
    }

    public void setPortalFileName(String PortalFileName) {
        this.PortalFileName = PortalFileName;
    }

    public int getReportScheduledId() {
        return reportScheduledId;
    }

    public void setReportScheduledId(int reportScheduledId) {
        this.reportScheduledId = reportScheduledId;
    }

    public int getReportId() {
        return reportId;
    }

    public void setReportId(int reportId) {
        this.reportId = reportId;
    }

    public int getoneviewid() {
        return oneviewid;
    }

    public void setoneviewid(int oneviewid) {
        this.oneviewid = oneviewid;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDataSelection() {
        return dataSelection;
    }

    public void setDataSelection(String dataSelection) {
        this.dataSelection = dataSelection;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getScheduledTime() {
        return scheduledTime;
    }

    public void setScheduledTime(String scheduledTime) {
        this.scheduledTime = scheduledTime;
    }

    public String getViewBy() {
        return viewBy;
    }

    public void setViewBy(String viewBy) {
        this.viewBy = viewBy;
    }

    public String getSubscribers() {
        return subscribers;
    }

    public void setSubscribers(String subscribers) {
        this.subscribers = subscribers;
    }

    public String getContenType() {
        return contentType;
    }

    public void setContenType(String contenType) {
        this.contentType = contenType;
    }

    public List<ReportSchedulePreferences> getReportSchedulePrefrences() {
        return reportSchedulePrefrences;
    }

    public void setReportSchedulePrefrences(List<ReportSchedulePreferences> reportSchedulePrefrences) {
        this.reportSchedulePrefrences = reportSchedulePrefrences;
    }

    public List<ReportScheduleSlices> getReportScheduleSlices() {
        return reportScheduleSlices;
    }

    public void setReportScheduleSlices(List<ReportScheduleSlices> reportScheduleSlices) {
        this.reportScheduleSlices = reportScheduleSlices;
    }

    public String getSchedulerName() {
        return schedulerName;
    }

    public void setSchedulerName(String schedulerName) {
        this.schedulerName = schedulerName;
    }

    public String getParameterXml() {
        return parameterXml;
    }

    public void setParameterXml(String paramXml) {
        this.parameterXml = paramXml;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public boolean isIsAutoSplited() {
        return isAutoSplited;
    }

    public void setIsAutoSplited(boolean isAutoSplited) {
        this.isAutoSplited = isAutoSplited;
    }

    public List<UserDimensionMap> getUsermap() {
        return usermap;
    }

    public void setUsermap(List<UserDimensionMap> usermap) {
        this.usermap = usermap;
    }

    public String getViewById() {
        return viewById;
    }

    public void setViewById(String viewById) {
        this.viewById = viewById;
    }

    public String getFolderId() {
        return folderId;
    }

    public void setFolderId(String folderId) {
        this.folderId = folderId;
    }

    public String getParticularDay() {
        return particularDay;
    }

    public void setParticularDay(String particularDay) {
        this.particularDay = particularDay;
    }

    public String getCheckedViewByIds() {
        return checkedViewByIds;
    }

    public void setCheckedViewByIds(String checkedViewByIds) {
        this.checkedViewByIds = checkedViewByIds;
    }

    public String getCheckViewByNames() {
        return checkViewByNames;
    }

    public void setCheckViewByNames(String checkedViewByNames) {
        this.checkViewByNames = checkedViewByNames;
    }

    public String getViewByName() {
        return viewByName;
    }

    public void setViewByName(String viewByName) {
        this.viewByName = viewByName;
    }

    public String getKpiMasterId() {
        return kpiMasterId;
    }

    public void setKpiMasterId(String kpiMasterId) {
        this.kpiMasterId = kpiMasterId;
    }

    public String getDashLetId() {
        return dashLetId;
    }

    public void setDashLetId(String dashLetId) {
        this.dashLetId = dashLetId;
    }

    public KPIScheduleHelper getkPIScheduleHelper() {
        return kPIScheduleHelper;
    }

    public void setkPIScheduleHelper(KPIScheduleHelper kPIScheduleHelper) {
        this.kPIScheduleHelper = kPIScheduleHelper;
    }

    public String getElementID() {
        return elementID;
    }

    public void setElementID(String elementID) {
        this.elementID = elementID;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }

    public List<String> getDataSelectionTypes() {
        return dataSelectionTypes;
    }

    public void setDataSelectionTypes(List<String> dataSelectionTypes) {
        this.dataSelectionTypes = dataSelectionTypes;
    }

    public boolean isRunFlag() {
        return runFlag;
    }

    public void setRunFlag(boolean runFlag) {
        this.runFlag = runFlag;
    }

    public boolean isReportSchedule() {
        return isReportSchedule;
    }

    public void setIsReportSchedule(boolean isReportSchedule) {
        this.isReportSchedule = isReportSchedule;
    }

    //started by Nazneen
    public int getSchedulerLoadId() {
        return schedulerLoadId;
    }

    public void setSchedulerLoadId(int schedulerLoadId) {
        this.schedulerLoadId = schedulerLoadId;
    }

    public String getSchedulerLoadName() {
        return schedulerLoadName;
    }

    public void setSchedulerLoadName(String SchedulerloadName) {
        this.schedulerLoadName = SchedulerloadName;
    }

    public String getSchedulerLoadType() {
        return schedulerLoadType;
    }

    public void setSchedulerLoadType(String SchedulerLoadType) {
        this.schedulerLoadType = SchedulerLoadType;
    }

    public String getSchedulerLoadTime() {
        return schedulerLoadTime;
    }

    public void setSchedulerLoadTruncType(String schedulerLoadTruncType) {
        this.schedulerLoadTruncType = schedulerLoadTruncType;
    }

    public String getSchedulerLoadTruncType() {
        return schedulerLoadTruncType;
    }

    public void setSchedulerLoadTime(String SchedulerLoadTime) {
        this.schedulerLoadTime = SchedulerLoadTime;
    }

    public String getSchedulerLoadFrequency() {
        return schedulerFrequency;
    }

    public void setSchedulerLoadFrequency(String SchedulerFrequency) {
        this.schedulerFrequency = SchedulerFrequency;
    }

    public Date getSchedulerLoadStartDate() {
        return schedulerStartDate;
    }

    public void setSchedulerLoadStartDate(Date schedulerStartDate) {
        this.schedulerStartDate = schedulerStartDate;
    }

    public Date getSchedulerLoadEndDate() {
        return schedulerEndDate;
    }

    public void setSchedulerLoadEndDate(Date schedulerEndDate) {
        this.schedulerEndDate = schedulerEndDate;
    }

    public String getSchedulerLoadEmailId() {
        return schedulerEmailId;
    }

    public void setSchedulerLoadEmailId(String SchedulerEmailId) {
        this.schedulerEmailId = SchedulerEmailId;
    }
    //end of Nazneen Code

    public boolean isFromOneview() {
        return fromOneview;
    }

    public void setFromOneview(boolean fromOneview) {
        this.fromOneview = fromOneview;
    }

    public String getOneviewFileName() {
        return oneviewFileName;
    }

    public void setFromsecureOneview(String oneviewsecurity) {
        this.oneviewsecurity = oneviewsecurity;
    }

    public String getFromsecureOneview() {
        return oneviewsecurity;
    }

    public void setOneviewFileName(String oneviewFileName) {
        this.oneviewFileName = oneviewFileName;
    }

    public String getAlertDateType() {
        return alertDateType;
    }

    public void setAlertDateType(String alertDateType) {
        this.alertDateType = alertDateType;
    }

    public String getAlertDate() {
        return alertDate;
    }

    public void setAlertDate(String alertDate) {
        this.alertDate = alertDate;
    }

    public List<String> getOneviewMeasureOptions() {
        return oneviewMeasureOptions;
    }

    public void setOneviewMeasureOptions(List<String> oneviewMeasureOptions) {
        this.oneviewMeasureOptions = oneviewMeasureOptions;
    }

    public HashMap<Integer, TrackerCondition> getMeasureAlertsConditions() {
        return measureAlertsConditions;
    }

    public void setMeasureAlertsConditions(HashMap<Integer, TrackerCondition> measureAlertsConditions) {
        this.measureAlertsConditions = measureAlertsConditions;
    }

    public String getMeasureValueCurrVal() {
        return measureValueCurrVal;
    }

    public void setMeasureValueCurrVal(String measureValueCurrVal) {
        this.measureValueCurrVal = measureValueCurrVal;
    }

    public boolean isFromdsrbKpi() {
        return fromdsrbKpi;
    }

    public void setFromdsrbKpi(boolean fromdsrbKpi) {
        this.fromdsrbKpi = fromdsrbKpi;
    }

    public boolean isForConditonalTest() {
        return forConditonalTest;
    }

    public void setForConditonalTest(boolean forConditonalTest) {
        this.forConditonalTest = forConditonalTest;
    }

    public String getTargetVal() {
        return targetVal;
    }

    public void setTargetVal(String targetVal) {
        this.targetVal = targetVal;
    }

    public String getDeviationVal() {
        return deviationVal;
    }

    public void setDeviationVal(String deviationVal) {
        this.deviationVal = deviationVal;
    }

    /**
     * @return the quickRefreshReport
     */
    public boolean isQuickRefreshReport() {
        return quickRefreshReport;
    }

    /**
     * @param quickRefreshReport the quickRefreshReport to set
     */
    public void setQuickRefreshReport(boolean quickRefreshReport) {
        this.quickRefreshReport = quickRefreshReport;
    }

    /**
     * @return the reportAdvHtmlFileProps
     */
    public String getReportAdvHtmlFileProps() {
        return reportAdvHtmlFileProps;
    }

    /**
     * @param reportAdvHtmlFileProps the reportAdvHtmlFileProps to set
     */
    public void setReportAdvHtmlFileProps(String reportAdvHtmlFileProps) {
        this.reportAdvHtmlFileProps = reportAdvHtmlFileProps;
    }

    /**
     * @return the contextPath
     */
    public String getContextPath() {
        return contextPath;
    }

    /**
     * @param contextPath the contextPath to set
     */
    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }

    public void setUploadedFilePath(String filePath) {
        this.uploadedFilePath = filePath;
    }

    public void setUploadedFileName(String fileName) {
        this.uploadedFileName = fileName;
    }

    public void setadvHtmlFileProps(String advHtmlFileProps) {
        this.advHtmlFileProps = advHtmlFileProps;
    }

    public void setoldAdvHtmlFileProps(String oldAdvHtmlFileProps) {
        this.oldAdvHtmlFileProps = oldAdvHtmlFileProps;
    }

    public void setoneletid(String oneviewletid) {
        this.oneviewletid = oneviewletid;
    }

    public void setfilename(String filename) {
        this.filename = filename;
    }

    public String getUploadedFilePath() {
        return uploadedFilePath;
    }

    public String getUploadedFileName() {
        return uploadedFileName;
    }

    public String getadvHtmlFileProps() {
        return advHtmlFileProps;
    }

    public String getoldAdvHtmlFileProps() {
        return oldAdvHtmlFileProps;
    }

    public String getoneletid() {
        return oneviewletid;
    }

    public String getfilename() {
        return filename;
    }
    //Added by Amar

    public void setReportIds(String[] reportId) {
        this.reportIds = reportId;
    }

    public void setSheetNumbers(String[] sheets) {
        this.sheetNumbers = sheets;
    }

    public void setLineNumbers(String[] lines) {
        this.lineNumbers = lines;
    }

    public String[] getReportIds() {
        return reportIds;
    }

    public String[] getSheetNumbers() {
        return sheetNumbers;
    }

    public String[] getLineNumbers() {
        return lineNumbers;
    }

    public boolean isExportReportSchedule() {
        return isExportReportSchedule;
    }

    public void setIsExportReportSchedule(boolean isReportSchedule) {
        this.isExportReportSchedule = isReportSchedule;
    }

    public void setSnashotUrls(String[] urls) {
        this.snapshotUrls = urls;

    }

    public String[] getSnapshotUrls() {
        return snapshotUrls;
    }

    public void setColNumbers(String[] colCounts) {
        this.colNumbers = colCounts;
    }

    public void setHeaderValues(String[] headValues) {
        this.headerValues = headValues;
    }

    public void setGTValues(String[] gtValues) {
        this.gTotals = gtValues;
    }

    public String[] getColNumbers() {
        return colNumbers;
    }

    public String[] getHeaderValues() {
        return headerValues;
    }

    public String[] getGTValues() {
        return gTotals;
    }

    public HashMap getGraphSizesDtlsHashMap() {
        return GraphSizesDtlsHashMap;
    }

    public void setGraphSizesDtlsHashMap(HashMap GraphSizesDtlsHashMap) {
        this.GraphSizesDtlsHashMap = GraphSizesDtlsHashMap;
    }

    public void setIsGoSchedule(boolean Go) {
        isGO = Go;
    }

    public boolean isGoSchedule() {
        return isGO;
    }
    //added by sruthi for setpath after scheduling 22/12/2014

    public String getfilepath() {
        return filepath1;
    }

    public void setfilepath(String filepath) {
        this.filepath1 = filepath;
    }

    //ended by sruthi
//added by sruthi for scheduling
    public String getlocalhost() {
        return localdata;
    }

    public void setlocalhost(String localhost) {
        this.localdata = localhost;
    }

    public String getrequestUrl() {
        return requestUrl;
    }

    public void setrequestUrl(String requestUrl1) {
        this.requestUrl = requestUrl1;
    }

    //ended bys ruthi
    //Added by Amar
    public void setLastSentDate(Date lsentDate) {
        this.lSentDate = lsentDate;
    }

    public Date getLastSentDate() {
        return this.lSentDate;
    }

    public void setUserMessage(String usrMesg) {
        this.usrMesg = usrMesg;
    }

    public String getUserMessage() {
        return this.usrMesg;
    }

    public void setModuleType(String modType) {
        this.moduleType = modType;
    }

    public String getModuleType() {
        return this.moduleType;
    }
    //end of code 

    public void setStartValue(String[] sCmpValue) {
        svalue = sCmpValue;
    }

    public String[] getStartValue() {
        return svalue;
    }

    public void setEndValue(String[] eCmpValue) {
        evalue = eCmpValue;
    }

    public String[] getEndValue() {
        return evalue;
    }

    public void setOperatorSymbol(String[] symbol) {
        symbolOperator = symbol;
    }

    public String[] getOperatorSymbol() {
        return this.symbolOperator;
    }

    public void setAlertType(String[] type) {
        alertType = type;
    }

    public String[] getAlertType() {
        return this.alertType;
    }

    public void setColumnHeaderNames(String[] selectedHeaderNames) {
        this.selectedHeaderNames = selectedHeaderNames;
    }

    public String[] getColumnHeaderNames() {
        return this.selectedHeaderNames;
    }
    //added by Dinanth
    private String firstViewByName = null;
    private String secondViewByName = null;

    public void setFirstViewByName(String fistViewByName) {
        this.firstViewByName = fistViewByName;
    }

    public String getFirstViewByName() {
        return this.firstViewByName;
    }

    public void setSecondViewByName(String secondViewByName) {
        this.secondViewByName = secondViewByName;
    }

    public String getSecondViewByName() {
        return this.secondViewByName;
    }
    //added by Dinanth
    private Container container = null;

    public void setSchedulerContainer(Container container) {
        this.container = container;
    }

    public Container getSchedulerContainer() {
        return container;
    }
}
