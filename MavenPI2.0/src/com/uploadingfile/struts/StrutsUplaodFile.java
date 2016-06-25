package com.uploadingfile.struts;

import java.util.Date;
import org.apache.struts.action.ActionForm;
import org.apache.struts.upload.FormFile;

public class StrutsUplaodFile extends ActionForm {

    private FormFile filename;
    private FormFile leftLogo;
    private FormFile rightLogo;
    private Date dateForReport;
    private String sDateFormat;
    private String session;
    private String Language;
    private String connid;
    private String footertext;
    private String records;
    private String querycache;
    private String query;
    private String dataset;
    private String geoenable;
    private String debug;
    private String report;
    private String duplicateSegmentation;
    private String module;
    private String tablename;
    private String elementId;
    private String bussRoleId;
    private String periodType;
    private String startValue;
    private String endValue;
    private String fromExcelUpload;
    private String elemtName;
    private String dayandWeekLevel;
    private String monthName;
    private String regName;

    /**
     * @return the filename
     */
    public FormFile getFilename() {
        return filename;
    }

    /**
     * @param filename the filename to set
     */
    public void setFilename(FormFile filename) {
        this.filename = filename;
    }

    /**
     * @return the sDateFormat
     */
    public String getsDateFormat() {
        return sDateFormat;
    }

    /**
     * @param sDateFormat the sDateFormat to set
     */
    public void setsDateFormat(String sDateFormat) {
        this.sDateFormat = sDateFormat;
    }

    /**
     * @return the session
     */
    public String getSession() {
        return session;
    }

    /**
     * @param session the session to set
     */
    public void setSession(String session) {
        this.session = session;
    }

    /**
     * @return the Language
     */
    public String getLanguage() {
        return Language;
    }

    /**
     * @param Language the Language to set
     */
    public void setLanguage(String Language) {
        this.Language = Language;
    }

    /**
     * @return the footertext
     */
    public String getFootertext() {
        return footertext;
    }

    /**
     * @param footertext the footertext to set
     */
    public void setFootertext(String footertext) {
        this.footertext = footertext;
    }

    /**
     * @return the records
     */
    public String getRecords() {
        return records;
    }

    /**
     * @param records the records to set
     */
    public void setRecords(String records) {
        this.records = records;
    }

    /**
     * @return the querycache
     */
    public String getQuerycache() {
        return querycache;
    }

    /**
     * e leftLogo; private File rightLogo;
     *
     * @param querycache the querycache to set
     */
    public void setQuerycache(String querycache) {
        this.querycache = querycache;
    }

    /**
     * @return the query
     */
    public String getQuery() {
        return query;
    }

    /**
     * @param query the query to set
     */
    public void setQuery(String query) {
        this.query = query;
    }

    /**
     * @return the dataset
     */
    public String getDataset() {
        return dataset;
    }

    /**
     * @param dataset the dataset to set
     */
    public void setDataset(String dataset) {
        this.dataset = dataset;
    }

    /**
     * @return the geoenable
     */
    public String getGeoenable() {
        return geoenable;
    }

    /**
     * @param geoenable the geoenable to set
     */
    public void setGeoenable(String geoenable) {
        this.geoenable = geoenable;
    }

    /**
     * @return the debug
     */
    public String getDebug() {
        return debug;
    }

    /**
     * @param debug the debug to set
     */
    public void setDebug(String debug) {
        this.debug = debug;
    }

    /**
     * @return the report
     */
    public String getReport() {
        return report;
    }

    /**
     * @param report the report to set
     */
    public void setReport(String report) {
        this.report = report;
    }

    /**
     * @return the leftLogo
     */
    public FormFile getLeftLogo() {
        return leftLogo;
    }

    /**
     * @param leftLogo the leftLogo to set
     */
    public void setLeftLogo(FormFile leftLogo) {
        this.leftLogo = leftLogo;
    }

    /**
     * @return the rightLogo
     */
    public FormFile getRightLogo() {
        return rightLogo;
    }

    /**
     * @param rightLogo the rightLogo to set
     */
    public void setRightLogo(FormFile rightLogo) {
        this.rightLogo = rightLogo;
    }

    /**
     * @return the duplicateSegmentation
     */
    public String getDuplicateSegmentation() {
        return duplicateSegmentation;
    }

    /**
     * @param duplicateSegmentation the duplicateSegmentation to set
     */
    public void setDuplicateSegmentation(String duplicateSegmentation) {
        this.duplicateSegmentation = duplicateSegmentation;
    }

    /**
     * @return the module
     */
    public String getModule() {
        return module;
    }

    /**
     * @param module the module to set
     */
    public void setModule(String module) {
        this.module = module;
    }

    /**
     * @return the tablename
     */
    public String getTablename() {
        return tablename;
    }

    /**
     * @param tablename the tablename to set
     */
    public void setTablename(String tablename) {
        this.tablename = tablename;
    }

    /**
     * @return the connid
     */
    public String getConnid() {
        return connid;
    }

    /**
     * @param connid the connid to set
     */
    public void setConnid(String connid) {
        this.connid = connid;
    }

    /**
     * @return the elementId
     */
    public String getElementId() {
        return elementId;
    }

    /**
     * @param elementId the elementId to set
     */
    public void setElementId(String elementId) {
        this.elementId = elementId;
    }

    /**
     * @return the bussRoleId
     */
    public String getBussRoleId() {
        return bussRoleId;
    }

    /**
     * @param bussRoleId the bussRoleId to set
     */
    public void setBussRoleId(String bussRoleId) {
        this.bussRoleId = bussRoleId;
    }

    /**
     * @return the periodType
     */
    public String getPeriodType() {
        return periodType;
    }

    /**
     * @param periodType the periodType to set
     */
    public void setPeriodType(String periodType) {
        this.periodType = periodType;
    }

    /**
     * @return the startValue
     */
    public String getStartValue() {
        return startValue;
    }

    /**
     * @param startValue the startValue to set
     */
    public void setStartValue(String startValue) {
        this.startValue = startValue;
    }

    /**
     * @return the endValue
     */
    public String getEndValue() {
        return endValue;
    }

    /**
     * @param endValue the endValue to set
     */
    public void setEndValue(String endValue) {
        this.endValue = endValue;
    }

    /**
     * @return the fromExcelUpload
     */
    public String getFromExcelUpload() {
        return fromExcelUpload;
    }

    /**
     * @param fromExcelUpload the fromExcelUpload to set
     */
    public void setFromExcelUpload(String fromExcelUpload) {
        this.fromExcelUpload = fromExcelUpload;
    }

    /**
     * @return the elemtName
     */
    public String getElemtName() {
        return elemtName;
    }

    /**
     * @param elemtName the elemtName to set
     */
    public void setElemtName(String elemtName) {
        this.elemtName = elemtName;
    }

    /**
     * @return the dayandWeekLevel
     */
    public String getDayandWeekLevel() {
        return dayandWeekLevel;
    }

    /**
     * @param dayandWeekLevel the dayandWeekLevel to set
     */
    public void setDayandWeekLevel(String dayandWeekLevel) {
        this.dayandWeekLevel = dayandWeekLevel;
    }

    /**
     * @return the monthName
     */
    public String getMonthName() {
        return monthName;
    }

    /**
     * @param monthName the monthName to set
     */
    public void setMonthName(String monthName) {
        this.monthName = monthName;
    }

    /**
     * @return the regName
     */
    public String getRegName() {
        return regName;
    }

    /**
     * @param regName the regName to set
     */
    public void setRegName(String regName) {
        this.regName = regName;
    }
    /**
     * @return the dateForReport
     */
//    public Date getDateForReport() {
//        return dateForReport;
//    }
//
//    /**
//     * @param dateForReport the dateForReport to set
//     */
//    public void setDateForReport(Date dateForReport) {
//        this.dateForReport = dateForReport;
//    }
    /**
     * @return the leftLogo
     */
}