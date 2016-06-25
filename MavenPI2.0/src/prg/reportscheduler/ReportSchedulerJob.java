package prg.reportscheduler;

import com.progen.datasnapshots.DataSnapshotBD;
import com.progen.datasnapshots.DataSnapshotGenerator;
import com.progen.oneView.bd.OneViewBD;
import com.progen.portal.PortalPdfGenerator;
import com.progen.report.PbReportCollection;
import com.progen.report.data.DataFacade;
import com.progen.report.display.util.NumberFormatter;
import com.progen.report.kpi.KPIBuilder;
import com.progen.report.pbDashboardCollection;
import com.progen.reportdesigner.bd.ReportTemplateBD;
import com.progen.reportdesigner.db.ReportTemplateDAO;
import com.progen.reportview.bd.PbReportViewerBD;
import com.progen.reportview.bd.ProgenReportViewerBD;
import com.progen.scheduler.ReportSchedule;
import com.progen.scheduler.ReportSchedulePreferences;
import com.progen.scheduler.ScheduleLogger;
import com.progen.scheduler.SchedulerBD;
import com.progen.scheduler.db.SchedulerDAO;
import com.progen.scheduler.tracker.TrackerCondition;
import com.progen.userlayer.db.LogReadWriter;
import java.io.*;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.Mem;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import prg.db.Container;
import prg.db.PbDb;
import prg.db.PbReturnObject;
import prg.util.PbMail;
import prg.util.PbMailParams;

public class ReportSchedulerJob implements Job {

    public static Logger logger = Logger.getLogger(ReportSchedulerJob.class);
    PbMailParams params = null;
    PbMail mailer = null;
    private String KPIHtml = "";
    public static Container containerNew = null;

    public void execute(JobExecutionContext jec) {

        JobDataMap dataMap = jec.getJobDetail().getJobDataMap();
        ReportSchedule schedule = (ReportSchedule) dataMap.get("scheduleReport");
        Calendar currentDate = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("H:m");
        String dateNow = formatter.format(currentDate.getTime());
        //  logger.info("..."+dateNow+"...."+schedule.getScheduledTime());
        if (schedule.isQuickRefreshReport() && schedule.getScheduledTime().equalsIgnoreCase(dateNow)) {
            generateQuickRefreshFile(schedule);
        } else if (!schedule.isFromOneview()) {
            if ((!schedule.isRunFlag() && schedule.getScheduledTime().equalsIgnoreCase(dateNow) && schedule.isExportReportSchedule()) || (!schedule.isRunFlag() && schedule.getScheduledTime().equals("0:0") && schedule.isExportReportSchedule())) {
                try {
                    sendExportSchedulerMail(schedule);
                } catch (ParseException ex) {
                    logger.error("Exception: ", ex);
                }
            } else if ((!schedule.isRunFlag() && schedule.getScheduledTime().equalsIgnoreCase(dateNow)) || (!schedule.isRunFlag() && schedule.getScheduledTime().equals("0:0"))) {
                try {
                    sendSchedulerMail(schedule);
                } catch (ParseException ex) {
                    logger.error("Exception: ", ex);
                }
            }
        } else if (schedule.isFromOneview()) {
            try {
                sendSchedulerMail(schedule);
            } catch (ParseException ex) {
                logger.error("Exception: ", ex);
            }
        } else if (schedule.isFromdsrbKpi()) {
            try {
                sendSchedulerMail(schedule);
            } catch (ParseException ex) {
                logger.error("Exception: ", ex);
            }
        }
    }

    public void sendSchedulerMail(ReportSchedule schedule) throws ParseException {

        //Code added Amar for CPU Load
        //Code aded by amar to check load scheduler
        int cpuLoadFinal = 0;
        try {
            Calendar currentDate = Calendar.getInstance();
            SimpleDateFormat formatter = new SimpleDateFormat("H:m");
            String dateNow = formatter.format(currentDate.getTime());
            String reportId = String.valueOf(schedule.getReportId());
            logger.info("REPORTID : " + reportId + " ,Scheduler name:" + schedule.getSchedulerName() + " calling at " + dateNow + "\n\n");
            logger.info("Before Lib");
            logger.info("java:lib:path = " + System.getProperty("java.library.path"));
            logger.info("after Lib");

            logger.info("**************************************");
            logger.info("*** Informations about the Memory: ***");
            logger.info("**************************************\n");
            //added by Dinanath
//        String filePath=File.separator+"usr"+File.separator+"local"+File.separator+"cache";
//        File executionPath = new File(filePath);
//       // System.load(executionPath.getAbsolutePath() + "/sigar-x86-winnt.dll");
//        logger.info("executionPath.getAbsolutePath():"+executionPath.getAbsolutePath());
//        logger.info("System.getProperty(os.name):"+System.getProperty("os.name"));
//        logger.info("System.getProperty(os.arch):"+System.getProperty("os.arch"));
//        if(System.getProperty("os.name").toLowerCase().contains("linux1")){
//                    if(System.getProperty("os.arch").toLowerCase().compareTo("i386") == 0){
//                         System.load(executionPath.getAbsolutePath() + "/libsigar-x86-linux.so");
//                    }
//                    else if(System.getProperty("os.arch").toLowerCase().compareTo("amd64") == 0){
//                         System.load(executionPath.getAbsolutePath() + "/libsigar-x86-linux.so");
//                    }
//            }
//         if(System.getProperty("os.name").toLowerCase().contains("windows 77")){
//                    if(System.getProperty("os.arch").toLowerCase().compareTo("x86") == 0){
//                         System.load(executionPath.getAbsolutePath() + "/sigar-x86-winnt.dll");
////                         System.load(executionPath.getAbsolutePath() + "/libsigar-x86-linux.so");
//                    }
//                    else if(System.getProperty("os.arch").toLowerCase().compareTo("x64") == 0){
//                         System.load(executionPath.getAbsolutePath() + "/sigar-amd64-winnt.dll");
//                    }
//            }

            Sigar sigar = new Sigar();
            Mem mem = null;
            CpuPerc cpuPerc = null;
//                try {
            mem = sigar.getMem();
            cpuPerc = sigar.getCpuPerc();
//        } catch (SigarException se) {
//            logger.error("Exception: ",se);
//                }

            logger.info("Actual total free system memory: "
                    + mem.getActualFree() / 1024 / 1024 + " MB");
            logger.info("Actual total used system memory: "
                    + mem.getActualUsed() / 1024 / 1024 + " MB");
            logger.info("Total free system memory ......: " + mem.getFree()
                    / 1024 / 1024 + " MB");
            logger.info("System Random Access Memory....: " + mem.getRam()
                    + " MB");
            logger.info("Total system memory............: " + mem.getTotal()
                    / 1024 / 1024 + " MB");
            logger.info("Total used system memory.......: " + mem.getUsed()
                    / 1024 / 1024 + " MB");
            //puPerc cpuPerc = cpu.getCpuPerc();
            logger.info("Cpu percentage usage combined: " + cpuPerc.getCombined() * 100);
            logger.info("Cpu percentage usage system: " + cpuPerc.getSys() * 100);
            logger.info("Cpu percentage usage user: " + cpuPerc.getUser() * 100);
            logger.info("Free CPU percentage : " + cpuPerc.getIdle() * 100);

            logger.info("\n**************************************\n");
            cpuLoadFinal = (int) (cpuPerc.getCombined() * 100);
        } catch (java.lang.UnsatisfiedLinkError usfdle) {
//            logger.error("Exception: ",usfdle);
        } catch (SigarException se) {
//            logger.error("Exception: ",se);
        } catch (Exception ex) {
//            logger.error("Exception: ",ex);
        }
        if (cpuLoadFinal > 80) {
            logger.info("Now its....continue.................Normal Scheduler in if block ");
            //String mailIds = schedule.getReportmailIds();
            String toAddress = schedule.getReportmailIds();
            String[] allAddress = toAddress.split(",");

            params = new PbMailParams();
            params.setBodyText("");
            // params.setToAddr(mailIds);
            params.setSubject(schedule.getSchedulerName() + " : System CPU Load is very high, scheduler will be delayed 5 minutes");
            params.setHasAttach(false);
            mailer = new PbMail(params);
            for (int j = 0; j < allAddress.length; j++) {
                try {
                    params.setToAddr(allAddress[j]);
                    mailer.sendMail();
                    ScheduleLogger.addLogEntry(schedule.getReportScheduledId(), "Scheduler", "Success");
                } catch (Exception e) {
                    logger.error("Exception: ", e);
                    ScheduleLogger.addLogEntry(schedule.getReportScheduledId(), "Scheduler", "Failed");
                }
            }

            //Code to reschedule the current scheduler
            String scheduleTime = schedule.getScheduledTime();
            String[] splitScheduleTime = scheduleTime.split(":");
            //sysMin = sysMin + aftrEtlTime
            int tmpHr = 0;
            int tmpMin = Integer.parseInt(splitScheduleTime[1]) + 5;
            if (tmpMin >= 60) {
                tmpHr = Integer.parseInt(splitScheduleTime[0]) + tmpMin / 60;
                tmpMin = tmpMin % 60;
            } else {
                tmpHr = Integer.parseInt(splitScheduleTime[0]);
            }
            scheduleTime = String.valueOf(tmpHr).concat(":").concat(String.valueOf(tmpMin));
            schedule.setScheduledTime(scheduleTime);
            SchedulerBD schedBD = new SchedulerBD();
            schedBD.scheduleReport(schedule, true);

            //end of code
        } else {
            logger.info("Now its....continue.................Normal Scheduler in else block");
            String reportId = String.valueOf(schedule.getReportId());
            String userId = schedule.getUserId();
            String[] reportIds = new String[1];
            reportIds[0] = reportId;
            String result = "";
            PbReportViewerBD bd = new PbReportViewerBD();
            ProgenReportViewerBD progenBD = new ProgenReportViewerBD();
            DataSnapshotBD snapshotBD = new DataSnapshotBD();
            List<ReportSchedulePreferences> schdPreferenceList = schedule.getReportSchedulePrefrences();
            ArrayList<String> fileNames = new ArrayList<String>();
            StringBuilder tempSb = new StringBuilder("");
            String contentType = schedule.getContentType();
            String schedulerId = String.valueOf(schedule.getReportScheduledId());
            //Start of code by sandeep on 16/10/14 for schedule// update local files in oneview
            if (schedule.isFromOneview()) {
                fileNames.add(bd.generateoneviewforschedule(reportId, schedule, schedulerId, userId, contentType, schedule.getSchedulerName()));
//End of code by sandeep on 16/10/14 for schedule// update local files in oneview
            } else if (schedule.isGoSchedule()) {
                progenBD.updateReportForGraphSchedule(reportId, schedulerId, userId, contentType, schedule.getSchedulerName());
            } else if (schedule.isFromdsrbKpi()) {
                //added by Dinanath
                DataSnapshotGenerator dsg = new DataSnapshotGenerator();
                dsg.alertsMailForKPIDashboard(schedule, reportId, userId);
                //end of code by Dinanath   
            } else {
                if (schedule.isReportSchedule()) {
//            PbReportViewerBD bd=new PbReportViewerBD();
                    fileNames.add(bd.generateReportForReportSchedule(reportId, schedulerId, userId, contentType, schedule.getSchedulerName()));
                }

                if ("PortalPDF".equalsIgnoreCase(contentType) && schedule.getPortalFileName() == null) {
                    HttpServletRequest request = schedule.getRequest();
                    HttpServletResponse response = schedule.getResponse();
                    String headerTitle = "Progen Business Solutions";
                    PortalPdfGenerator pdf = new PortalPdfGenerator();
                    pdf.setHeaderTitle(headerTitle);
                    pdf.setReportName("Pdf Report");
                    pdf.setFileName("downloadPDF.pdf");
//                 request.setAttribute("PortalTabId", reportId);
                    pdf.setRequest(request);
                    pdf.setResponse(response);
                    pdf.setContentType(contentType);
                    pdf.portalPDF();
                    String pdfFilename = pdf.getFileName();
                    fileNames.add(pdfFilename);

                } else if ("PortalPDF".equalsIgnoreCase(contentType)) {
                    fileNames.add(schedule.getPortalFileName());
                }

                if ("html".equalsIgnoreCase(contentType) || "pdf".equalsIgnoreCase(contentType) || "excel".equalsIgnoreCase(contentType) || "CSV".equalsIgnoreCase(contentType)) {
                    fileNames = snapshotBD.downloadDataSnapShotHtmlOrPdfOrExcel(reportIds, userId, schedule);
                } else if ("kpihtml".equalsIgnoreCase(contentType)) {
                    if (KPIHtml.equalsIgnoreCase("")) {
                        Container container = null;
                        pbDashboardCollection collect = null;
                        HashMap DBKPIHashMap = null;
                        try {
                            container = new DataSnapshotBD().generateContainer(reportId, userId);
                            KPIBuilder kpibuilder = new KPIBuilder();

                            DBKPIHashMap = container.getDBKPIHashMap();

                            collect = (pbDashboardCollection) container.getReportCollect();
                            collect.setDBKPIHashMap(DBKPIHashMap);
                            collect.reportId = reportId;//here reportId is DashBoard Id

                            collect.reportIncomingParameters = container.getRepReqParamsHashMap();

                            kpibuilder.setElemntIdForMail(schedule.getElementID());
                            kpibuilder.setSchedulerFlag(true);

                            try {
                                result = kpibuilder.processSingleKpi(container, schedule.getkPIScheduleHelper().getKpiMasterId(), collect.kpiQuery, null, schedule.getkPIScheduleHelper().getDashLetId(), reportId, false, collect, userId, "false");
//                        
                            } catch (Exception ex) {
                                logger.error("Exception: ", ex);
                            }
                            tempSb.append("<html><head></head><body>" + result + "</body></html>");

                        } catch (SQLException ex) {
                            logger.error("Exception: ", ex);
                        }

                        fileNames = snapshotBD.downloadDataKPIHtml(reportIds, userId, tempSb.toString());
                    } else {
                        fileNames = snapshotBD.downloadDataKPIHtml(reportIds, userId, KPIHtml);
                    }
                } else if (schedule.isFromOneview() || schedule.isFromdsrbKpi()) {
                    List<String> measuTimeData = new ArrayList<String>();
                    List<String> attacheDetails = new ArrayList<String>();
                    List<String> dataSelectionTypes = new ArrayList<String>();
                    DataSnapshotGenerator snapgenter = new DataSnapshotGenerator();
                    String eleId = schedule.getElementID();
                    String userId1 = schedule.getUserId();
                    String measName = schedule.getViewByName();
                    String roleId = schedule.getFolderId();
                    String alertDate = schedule.getAlertDate();
                    attacheDetails = schedule.getDataSelectionTypes();
                    dataSelectionTypes = schedule.getOneviewMeasureOptions();
                    OneViewBD oneviewBd = new OneViewBD();

                    String numberFrmt = dataSelectionTypes.get(2);
                    String targetVal = schedule.getTargetVal();
                    String devPer = schedule.getDeviationVal();
                    String tarval = "";
                    String devval = "";

                    measuTimeData = oneviewBd.getOneviewMeasureTimeData(eleId, roleId, userId, attacheDetails, alertDate, dataSelectionTypes);

                    if ((targetVal != null && devPer != null) && (!targetVal.equalsIgnoreCase("") && !devPer.equalsIgnoreCase(""))) {
                        tarval = NumberFormatter.getModifiedNumber(new BigDecimal(targetVal), numberFrmt);
                        devval = NumberFormatter.getModifiedNumber(new BigDecimal(devPer), numberFrmt);
                        measuTimeData.add(devval);
                        measuTimeData.add(tarval);
                        attacheDetails.add("deviation");
                        attacheDetails.add("target");
                    }

                    String oneviewFileName = snapgenter.generateOneviewMeasureTimeData(eleId, userId1, measName, measuTimeData, attacheDetails, schedule.isFromdsrbKpi());
                    fileNames.add(oneviewFileName);
                }
                String dir = System.getProperty("java.io.tmpdir") + "/";
//        else{
//            fileNames = snapshotBD.downloadDataSnapShotPDF(reportIds, userId, allViewBys, schedule);
//        }
                if (!schedule.isReportSchedule() && !schedule.isFromOneview() && !schedule.isFromdsrbKpi()) {
                    for (int i = 0; i < schdPreferenceList.size(); i++) {
                        //for (int k=0;k<fileNames.size();k++) {
                        //ReportSchedulePreferences schedulePreferences=schdPreferenceList.get(0);
                        ReportSchedulePreferences schedulePreferences = schdPreferenceList.get(i);
                        String mailIds = schedulePreferences.getMailIds();
                        String htmlFile = fileNames.get(0);//chenged from fileNames.get(i)for getting fileNames array listvalues fileNames.get(i)
                        if (htmlFile != null && !htmlFile.isEmpty()) {
                            ArrayList<String> attch = new ArrayList<String>();
                            for (int j = 0; j < fileNames.size(); j++) {
//                                String str = new String(dir + fileNames.get(j));
                                String str = dir + fileNames.get(j);
                                attch.add(str);
                            }

                            String toAddress = mailIds;

                            params = new PbMailParams();
                            params.setBodyText("");
                            params.setToAddr(toAddress);
                            params.setSubject(schedule.getSchedulerName());
                            params.setHasAttach(true);

                            //params.setAttachFile(dir+htmlFile);
                            params.setAttachFile(attch);

                            try {
                                mailer = new PbMail(params);
                                mailer.sendMail();
                                ScheduleLogger.addLogEntry(schedule.getReportScheduledId(), "Scheduler", "Success");
                            } catch (Exception e) {
                                logger.error("Exception: ", e);
                                ScheduleLogger.addLogEntry(schedule.getReportScheduledId(), "Scheduler", "Failed");
                            }
                        }
                    }
                } else if (schedule.isReportSchedule() && !contentType.equals("M")) {
                    String mailIds = schedule.getReportmailIds();
                    String usrMesg = schedule.getUserMessage();
                    //added by Dinanath for header logo and footer logo
                    String isHeaderLogoOn = schedule.getIsHeaderLogoOn();
                    String isFooterLogoOn = schedule.getIsFooterLogoOn();
                    String isOptionalHeaderTextOn = schedule.getIsOptionalHeaderTextOn();
                    String isOptionalFooterTextOn = schedule.getIsOptionalFooterTextOn();
                    String isHtmlSignatureOn = schedule.getIsHtmlSignatureOn();
                    String filePath2 = schedule.getadvHtmlFileProps();
                    if (filePath2 == null) {
                        filePath2 = File.separator + "usr" + File.separator + "local" + File.separator + "cache";
                    }
                    //end of code by Dinanath
                    String FileName = fileNames.get(0);
//            String localhost=schedule.getlocalhost();
                    //added by sruthi for schedular
                    String requesturl = schedule.getrequestUrl();
                    String filepath = schedule.getfilepath();
                    String folderPath = filepath + "/SharedReports";
                    String reportFileName = "/sharedReport_" + reportId + "_" + FileName;
                    String pathname = folderPath + reportFileName;//ended by sruhti
                    StringBuilder completeContent = new StringBuilder();
                    String[] mailids = mailIds.split(",");
                    String url = null;
                    //completeContent.append("<html><body>");
                    //added by sruthi for schedular
                    try {
                        StringBuilder requestURL = new StringBuilder(requesturl);
                        url = requestURL.append("?reportBy=viewReport").append("&REPORTID=" + reportId).append("&action=open").toString();
                    } catch (Exception e) {
                        logger.error("Exception: ", e);
                    }
                    //ended by sruhti
                    if (usrMesg != null && !usrMesg.isEmpty()) {
                        completeContent.append("<h3>").append(usrMesg).append("</h3><br>");
                    }
                    //added by Dinanath 
                    try {

                        File tempFile = new File(filePath2 + File.separator + "HeaderImageOFHtmlEmail.jpg");
                        if (tempFile.exists()) {
                            if (isHeaderLogoOn != null && isHeaderLogoOn.equalsIgnoreCase("on")) {
//                            completeContent.append("<div style='width:100%;height:30%;'><img src=\"cid:image\" height=\"200\" width=\"1100\"></div>");//earlier
                                completeContent.append("<div style=''><img src=\"cid:image\" ></div>");

                            }
                        }
                        if (isOptionalHeaderTextOn != null && isOptionalHeaderTextOn.equalsIgnoreCase("on")) {
//                            String filePath2 = File.separator+"usr"+File.separator+"local"+File.separator+"cache";
                            String htmlSignatureFile = "HtmlOptionalHeaderForScheduler.html";
                            BufferedReader br2 = null;
                            try {
                                br2 = new BufferedReader(new FileReader(filePath2 + File.separator + htmlSignatureFile));
                                String line = br2.readLine();
                                while (line != null) {
                                    completeContent.append(line);
                                    line = br2.readLine();
                                }
                            } catch (Exception e) {
                                logger.error("Exception: ", e);
                            } finally {
                                if (br2 != null) {
                                    br2.close();
                                }
                            }
                        }
                    } catch (Exception e) {
                    }
                    //code endded by Dinanath
                    try {
                        BufferedReader br = new BufferedReader(new FileReader(dir + FileName));
                        try {

                            String line = br.readLine();
                            while (line != null) {
                                completeContent.append(line);
                                line = br.readLine();
                            }
                        } finally {
                            br.close();
                            //ServletUtilities.markFileForDeletion(FileName, request.getSession(false));
                        }
//               completeContent.append( "</body></html>");
                        completeContent.append("</table><br><br>");
                        //code written by Dinanath for footer logo on
                        if (isOptionalFooterTextOn != null && isOptionalFooterTextOn.equalsIgnoreCase("on")) {
                            String htmlSignatureFile = "HtmlOptionalFooterForScheduler.html";
                            BufferedReader br2 = null;
                            try {
                                br2 = new BufferedReader(new FileReader(filePath2 + File.separator + htmlSignatureFile));

                                String line = br2.readLine();
                                while (line != null) {
                                    completeContent.append(line);
                                    line = br2.readLine();
                                }
                            } catch (Exception e) {
                                logger.error("Exception: ", e);
                            } finally {
                                if (br2 != null) {
                                    br2.close();
                                }
                            }
                        }
                        if (isHtmlSignatureOn != null && isHtmlSignatureOn.equalsIgnoreCase("on")) {
                            String htmlSignatureFile = "HtmlSignatureForScheduler.html";
                            BufferedReader br2 = null;
                            try {
                                br2 = new BufferedReader(new FileReader(filePath2 + File.separator + htmlSignatureFile));
                                String line = br2.readLine();
                                while (line != null) {
                                    completeContent.append(line);
                                    line = br2.readLine();
                                }
                            } catch (Exception e) {
                                logger.error("Exception: ", e);
                            } finally {
                                if (br2 != null) {
                                    br2.close();
                                }
                            }
                        }
                        File tempFile = new File(filePath2 + File.separator + "leftLogoImageOfScheduler.jpg");
                        File tempFile2 = new File(filePath2 + File.separator + "rightLogoImageOfScheduler.jpg");
                        if (tempFile.exists() && tempFile2.exists()) {
                            if (isFooterLogoOn != null && isFooterLogoOn.equalsIgnoreCase("on")) {
                                completeContent.append("<div style='width:100%;height:50px;'><table><tr><td><img src=\"cid:pidina_image\" height=\"100\" width=\"200\"/></td><td style=\"width: 750px;font-size:30px\"><center> Powered By ProGen Business Solutions</center></td><td><img src=\"cid:progendina_image\" height=\"100\" width=\"200\"/></td></tr></table></div>");

                            }
                        }
//code endded by Dinanath for header footer logo

                        completeContent.append("</body></html>");
//              completeContent.append("<table width='100%' align='left'><tr><td>End of The Report</td></tr><tr><td><h5>Powered By PI</h5></td></tr></table>"
//                      + "<table><tr><td></td></tr></table>");//commented by Dinanath
                        for (int i = 0; i < mailids.length; i++) {
                            params = new PbMailParams();
                            //  params.setBodyText(completeContent.toString());
                            params.setBodyText("");
                            params.setToAddr(mailids[i]);
                            params.setSubject(schedule.getSchedulerName());
                            if (contentType.equals("H")) {
                                params.setBodyText(completeContent.toString().replace("<Font color=>", "<Font color=#000000>"));
                                params.setHasAttach(false);
                            } else {
                                params.setHasAttach(true);
                            }
                            params.setIsFooterLogoOn(isFooterLogoOn);
                            params.setIsHeaderLogoOn(isHeaderLogoOn);
                            params.setFilePath(filePath2);

//                params.setAttachFile(dir + FileName);
                            ArrayList<String> attach = new ArrayList<String>();
//                            String str = new String(dir + FileName);
                            String str = dir + FileName;
                            attach.add(str);
                            params.setAttachFile(attach);
                            params.setFileUrl(url);//added by sruthi for schedular
                            mailer = new PbMail(params);
                            boolean status = mailer.sendMail();
                            if (status) {
                                ReportTemplateDAO dao = new ReportTemplateDAO();
                                dao.updateSchedulerStatusAndDate(schedulerId, reportId, userId);
                            }
                        }
                    } catch (Exception e) {
                        //Added By Amar
                        if (containerNew != null) {
                            if (containerNew.getReportCollect().getLogReadWriterObject() != null) {
                                StringWriter str = new StringWriter();
                                PrintWriter writer = new PrintWriter(str);
//                                e.printStackTrace(writer);
                                try {
                                    containerNew.getReportCollect().getLogReadWriterObject().fileWriter(str.getBuffer().toString());
                                } catch (IOException eo) {
                                }
                            }//end of code
                        }
                        String mailIdsn = schedule.getReportmailIds();
                        String mailIdn = mailIdsn.concat(",amar.pal@progenbusiness.com");
                        String LogFileName = LogReadWriter.file_name;
//            StringBuilder completeContent=new StringBuilder();
                        String[] mailidsn = mailIdn.split(",");
                        //completeContent.append("<html><body>");

                        try {
                            BufferedReader br = new BufferedReader(new FileReader(dir + LogFileName + ".txt"));
                            try {

                                String line = br.readLine();
                                while (line != null) {
                                    completeContent.append(line);
                                    line = br.readLine();
                                }
                            } finally {
                                br.close();
                                //ServletUtilities.markFileForDeletion(FileName, request.getSession(false));
                            }
//               completeContent.append( "</body></html>");
                            completeContent.append("</table><br><br>");
                            completeContent.append("<table width='100%' align='left'><tr><td>End of The Report</td></tr><tr><td><h5>Powered By PI</h5></td></tr></table>"
                                    + "<table><tr><td></td></tr></table>");
                            //completeContent.append("</body></html>");

                            // logger.info("completeContent"+completeContent.toString());
                            for (int i = 0; i < mailidsn.length; i++) {
                                params = new PbMailParams();
                                //  params.setBodyText(completeContent.toString());
                                params.setBodyText("");
                                params.setToAddr(mailidsn[i]);
                                params.setSubject(schedule.getSchedulerName());
                                params.setHasAttach(true);

//                params.setAttachFile(dir + FileName);
                                ArrayList<String> attach = new ArrayList<String>();
//                                String str = new String(dir + LogFileName + ".txt");
                                String str = dir + LogFileName + ".txt";
                                attach.add(str);
                                params.setAttachFile(attach);
                                mailer = new PbMail(params);
                                boolean status = mailer.sendLogFileMail();
                                if (status) {
                                    ReportTemplateDAO dao = new ReportTemplateDAO();
                                    dao.updateSchedulerStatusAndDate(schedulerId, reportId, userId);
                                }
                            }
                        } catch (Exception e1) {
                            logger.error("Exception: ", e1);
                        }
                        //end of code by Amar
                        logger.error("Exception: ", e);
                    }
                } else if (schedule.isFromOneview()) {
//               String mailIds =schedule.getReportmailIds();
                    String FileName = fileNames.get(0);
                    StringBuilder completeContent = null;
//               String[] mailids = mailIds.split(",");
                    if (schedule.isForConditonalTest()) {
                        completeContent = new StringBuilder();
                        try {
                            BufferedReader br = new BufferedReader(new FileReader(dir + FileName));
                            try {

                                String line = br.readLine();
                                while (line != null) {
                                    completeContent.append(line);
                                    line = br.readLine();
                                }
                            } finally {
                                br.close();
                            }
                            completeContent.append("<br><br><br>");
                            completeContent.append("<table width='100%' align='left'><tr><td></td></tr><tr><td><h5></h5></td></tr></table>"
                                    + "<table><tr><td></td></tr></table>").append("</body></html>");

                            params = new PbMailParams();
                            params.setBodyText(completeContent.toString());
                            params.setToAddr(schedule.getReportmailIds());
                            params.setSubject(schedule.getSchedulerName());
                            if (contentType.equals("H")) {
                                params.setBodyText(completeContent.toString().replace("<Font color=>", "<Font color=#000000>"));
                                params.setHasAttach(false);
                            } else {
                                params.setHasAttach(true);
                            }
                            ArrayList<String> attach = new ArrayList<String>();
//                            String str = new String(dir + FileName);
                            String str = dir + FileName;
                            attach.add(str);
                            params.setAttachFile(attach);
                            mailer = new PbMail(params);
                            boolean status;
                            status = mailer.sendMail();
                        } catch (Exception e) {
                            logger.error("Exception: ", e);
                        }
                    } else {
                        double measVal = Double.parseDouble(schedule.getMeasureValueCurrVal());
                        HashMap<Integer, TrackerCondition> trackCondMap = schedule.getMeasureAlertsConditions();
                        Set trackeCondSet = trackCondMap.keySet();
                        Iterator trackItr = trackeCondSet.iterator();
                        while (trackItr.hasNext()) {
                            int trackViewByVal = (Integer) trackItr.next();
                            TrackerCondition trackerCondition = trackCondMap.get(trackViewByVal);
                            String trackTest = trackerCondition.getViewByValue();
                            boolean isSuccessed = false;
                            if (trackTest.equalsIgnoreCase("percentBasis")) {
                                isSuccessed = this.isConditionSatisfied(trackerCondition);
                            } else {
                                isSuccessed = this.isConditionSatisfy(trackerCondition, measVal);
                            }
                            if (isSuccessed) {
                                completeContent = new StringBuilder();
                                try {
                                    BufferedReader br = new BufferedReader(new FileReader(dir + FileName));
                                    try {

                                        String line = br.readLine();
                                        while (line != null) {
                                            completeContent.append(line);
                                            line = br.readLine();
                                        }
                                    } finally {
                                        br.close();
                                    }
                                    completeContent.append("<br><br><br>");
                                    completeContent.append("<table width='100%' align='left'><tr><td></td></tr><tr><td><h5></h5></td></tr></table>"
                                            + "<table><tr><td></td></tr></table>").append("</body></html>");

                                    params = new PbMailParams();
                                    params.setBodyText(completeContent.toString());
                                    params.setToAddr(trackerCondition.getMailIds());
                                    params.setSubject(trackerCondition.getTagType());
                                    params.setHasAttach(true);
                                    mailer = new PbMail(params);
                                    boolean status;
                                    status = mailer.sendMail();
                                } catch (Exception e) {
                                    logger.error("Exception: ", e);
                                }
                            }
                        }
                    }
                    completeContent = null;

                } else {
                    String FileName = fileNames.get(0);
                    StringBuilder completeContent = null;
                    completeContent = new StringBuilder();
                    try {
                        BufferedReader br = new BufferedReader(new FileReader(dir + FileName));
                        try {

                            String line = br.readLine();
                            while (line != null) {
                                completeContent.append(line);
                                line = br.readLine();
                            }
                        } finally {
                            br.close();
                        }
                        completeContent.append("<br><br><br>");
                        completeContent.append("<table width='100%' align='left'><tr><td></td></tr><tr><td><h5></h5></td></tr></table>"
                                + "<table><tr><td></td></tr></table>").append("</body></html>");

                        params = new PbMailParams();
                        params.setBodyText(completeContent.toString());
                        params.setToAddr(schedule.getReportmailIds());
                        params.setSubject(schedule.getSchedulerName());
                        if (contentType.equals("H")) {
                            params.setBodyText(completeContent.toString().replace("<Font color=>", "<Font color=#000000>"));
                            params.setHasAttach(false);
                        } else {
                            params.setHasAttach(true);
                        }
                        ArrayList<String> attach = new ArrayList<String>();
//                String str=new String(dir + FileName);Prabal
                        String str = dir + FileName;
                        attach.add(str);
                        params.setAttachFile(attach);
                        mailer = new PbMail(params);
                        boolean status;
                        status = mailer.sendMail();
                    } catch (Exception e) {
                        logger.error("Exception: ", e);
                    }
                }

                this.deleteFiles(fileNames);
            }
        }
    }

//        public void sendSchedulerMail(ReportSchedule schedule)
//        {
//            String subscribers = schedule.getSubscribers();
//            String reportId = String.valueOf(schedule.getReportId());
//            String contentType = schedule.getContenType();
//            String userId = schedule.getUserId();
//            String[] reportIds = new String[1];
//            reportIds[0] = reportId;
//            DataSnapshotBD snapshotBD = new DataSnapshotBD();
//            ArrayList<String> fileNames = snapshotBD.downloadDataSnapShotHtml(reportIds, userId, true);
//            String dir = System.getProperty("java.io.tmpdir") + "/";
//
//            if (fileNames != null && !fileNames.isEmpty()){
//
//                String subject="Report Scheduler ";
//                String toAddress=subscribers;
//
//                params=new PbMailParams();
//                params.setBodyText("");
//                params.setToAddr(toAddress);
//                params.setSubject(schedule.getSchedulerName());
//                params.setHasAttach(true);
//
//                if (fileNames.size() == 1){
//                    params.setAttachFile(dir+fileNames.get(0));
//                }
//                else{
//                    try {
//                        String zipFileName = ServletUtilities.makeAndStoreZip(fileNames);
//                        zipFileName = dir + zipFileName;
//                        params.setAttachFile(zipFileName);
//                    } catch (IOException ex) {
//                        logger.error("Exception: ",ex);
//                    }
//                }
//
//                try{
//                    mailer=new PbMail(params);
//                    mailer.sendMail();
//                }
//                catch(Exception e){
//                    logger.error("Exception: ",e);
//                }
//            }
//            this.deleteFiles(fileNames);
//        }
    private void deleteFiles(ArrayList<String> fileNames) {
        Iterator iter = fileNames.listIterator();
        while (iter.hasNext()) {
            String filename = (String) iter.next();
            File file = new File(
                    System.getProperty("java.io.tmpdir"), filename);
            if (file.exists()) {
                file.delete();
            }
        }
        return;
    }

    public static void main(String args[]) {
        SchedulerDAO dao = new SchedulerDAO();
        ReportSchedule schedule = dao.getReportScheduleDetails(String.valueOf(445), false);
        ReportSchedulerJob job = new ReportSchedulerJob();
        try {
            job.sendSchedulerMail(schedule);
        } catch (ParseException ex) {
            logger.error("Exception: ", ex);
        }
    }

    public void setKPIHtml(String KPIHtml) {
        this.KPIHtml = KPIHtml;
    }

    private boolean isConditionSatisfy(TrackerCondition eachCond, double measVal) {
        boolean success = false;
        String operator = eachCond.getOperator();
        double trackerVal = eachCond.getMeasureStartValue();
        double trackEndVal = eachCond.getMeasureEndValue();

        if (">".equals(operator)) {
            if (measVal > trackerVal) {
                success = true;
            }
        } else if (">=".equalsIgnoreCase(operator)) {
            if (measVal >= trackerVal) {
                success = true;
            }
        } else if ("<".equalsIgnoreCase(operator)) {
            if (measVal < trackerVal) {
                success = true;
            }
        } else if ("<=".equalsIgnoreCase(operator)) {
            if (measVal <= trackerVal) {
                success = true;
            }
        } else if ("==".equalsIgnoreCase(operator)) {
            if (measVal == trackerVal) {
                success = true;
            }
        } else if ("!=".equalsIgnoreCase(operator)) {
            if (measVal != trackerVal) {
                success = true;
            }
        } else if ("<>".equalsIgnoreCase(operator)) {
            if (trackerVal < measVal && measVal < trackEndVal) {
                success = true;
            }
        }
        return success;
    }

    private boolean isConditionSatisfied(TrackerCondition eachCond) {
        boolean successed = false;
        String operator = eachCond.getOperator();
        double trackerVal = eachCond.getMeasureStartValue();
        double trackEndVal = eachCond.getMeasureEndValue();
        double devPerVal = eachCond.getDeviationPerVal();
        if (">".equals(operator)) {
            if (devPerVal > trackerVal) {
                successed = true;
            }
        } else if (">=".equalsIgnoreCase(operator)) {
            if (devPerVal >= trackerVal) {
                successed = true;
            }
        } else if ("<".equalsIgnoreCase(operator)) {
            if (devPerVal < trackerVal) {
                successed = true;
            }
        } else if ("<=".equalsIgnoreCase(operator)) {
            if (devPerVal <= trackerVal) {
                successed = true;
            }
        } else if ("==".equalsIgnoreCase(operator)) {
            if (devPerVal == trackerVal) {
                successed = true;
            }
        } else if ("!=".equalsIgnoreCase(operator)) {
            if (devPerVal != trackerVal) {
                successed = true;
            }
        } else if ("<>".equalsIgnoreCase(operator)) {
            if (trackerVal < devPerVal && devPerVal < trackEndVal) {
                successed = true;
            }
        }
        return successed;
    }

    public void generateQuickRefreshFile(ReportSchedule schedule) {
        String reportId = String.valueOf(schedule.getReportId());
        String userId = schedule.getUserId();
        PbReportViewerBD viewerBd = new PbReportViewerBD();
        Container container = new Container();
        if (container.getReportCollect() == null) {
            PbReportCollection collect = new PbReportCollection();
            container.setReportCollect(collect);
        }
        viewerBd.prepareReport("open", container, reportId, userId, new HashMap());
        String folderPath = schedule.getReportAdvHtmlFileProps() + "/Reports";
        //
        File folderDir = new File(folderPath);
        if (!folderDir.exists()) {
            folderDir.mkdir();
        }
        DateFormat dateFormat = new SimpleDateFormat("ddMMyyyy");
        Calendar cal = Calendar.getInstance();
        String date = dateFormat.format(cal.getTime());
        //
        String filePath = folderPath + "/" + reportId + "_" + date + "_time stamp.txt";
        container.setQuickRefreshEnabled(true);
        container.setQuickautoRefresh(true);
        PbDb db = new PbDb();
        PbReturnObject retobj = null;
        String qry = "select FILE_PATH from PRG_AR_REPORT_MASTER where REPORT_ID='" + reportId + "'";
        try {
            retobj = db.execSelectSQL(qry);
            if (retobj != null && retobj.getRowCount() > 0) {
                String path = retobj.getFieldValueString(0, 0);
                File file = new File(path);
                if (file.exists()) {
                    file.delete();
                }
            }
        } catch (SQLException ex) {
            logger.error("Exception: ", ex);
        }
        String finalqry = "UPDATE PRG_AR_REPORT_MASTER SET FILE_PATH='" + filePath + "' where REPORT_ID='" + reportId + "'";

        try {
            db.execUpdateSQL(finalqry);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }
        //dao.insertQuickRefreshOption(reportId,quickRefreshEnable,filePath,schedule,autorefresh);
        ReportTemplateBD bd = new ReportTemplateBD();
        container.setFacadePath(filePath);
        DataFacade facade = new DataFacade(container);
        facade.setUserId(userId);
        try {
            // 
            if (container.getViewByCount() < 2 && container.getReportCollect().reportColViewbys.isEmpty()) {
                facade = bd.generateReportQrys(reportId, facade, userId);
            }
            viewerBd.writeBackUpFile(facade, filePath);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }
    }
    // This function is added by Amar

    public void sendExportSchedulerMail(ReportSchedule schedule) throws ParseException {

        //Code aded by amar to check load scheduler
        int cpuLoadFinal = 0;
        try {
            Calendar currentDate = Calendar.getInstance();
            SimpleDateFormat formatter = new SimpleDateFormat("H:m");
            String dateNow = formatter.format(currentDate.getTime());
            String reportId = String.valueOf(schedule.getReportId());

            String reportId1[] = schedule.getReportIds();
            if (reportId.equalsIgnoreCase("0")) {
                if (reportId1 != null && reportId1.length >= 0) {
                    for (int i = 0; i < reportId1.length; i++) {
                        logger.info("REPORTID : " + reportId1[i] + " ,Scheduler name:" + schedule.getSchedulerName() + "  calling at " + dateNow + "\n\n");
                    }
                }
            } else {
                logger.info("REPORTID : " + reportId + " ,Scheduler name:" + schedule.getSchedulerName() + " calling at " + dateNow + "\n\n");
            }

            logger.info("**************************************");
            logger.info("*** Informations about the Memory: ***");
            logger.info("**************************************\n");
            Sigar sigar = new Sigar();
            Mem mem = null;
            CpuPerc cpuPerc = null;
            mem = sigar.getMem();
            cpuPerc = sigar.getCpuPerc();

            logger.info("Actual total free system memory: "
                    + mem.getActualFree() / 1024 / 1024 + " MB");
            logger.info("Actual total used system memory: "
                    + mem.getActualUsed() / 1024 / 1024 + " MB");
            logger.info("Total free system memory ......: " + mem.getFree()
                    / 1024 / 1024 + " MB");
            logger.info("System Random Access Memory....: " + mem.getRam()
                    + " MB");
            logger.info("Total system memory............: " + mem.getTotal()
                    / 1024 / 1024 + " MB");
            logger.info("Total used system memory.......: " + mem.getUsed()
                    / 1024 / 1024 + " MB");
            //puPerc cpuPerc = cpu.getCpuPerc();
            logger.info("Cpu percentage usage combined: " + cpuPerc.getCombined() * 100);
            logger.info("Cpu percentage usage system: " + cpuPerc.getSys() * 100);
            logger.info("Cpu percentage usage user: " + cpuPerc.getUser() * 100);
            logger.info("Free CPU percentage : " + cpuPerc.getIdle() * 100);

            logger.info("\n**************************************\n");
            cpuLoadFinal = (int) (cpuPerc.getCombined() * 100);
        } catch (java.lang.UnsatisfiedLinkError usfdle) {
//            logger.error("Exception: ",usfdle);
        } catch (org.hyperic.sigar.SigarException se) {
//            logger.error("Exception: ",se);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }
        if (cpuLoadFinal > 80) {
            logger.info("Now its....continue.................Export Scheduler in if block");
            //String mailIds = schedule.getReportmailIds();
            String toAddress = schedule.getReportmailIds();
            String[] allAddress = toAddress.split(",");

            params = new PbMailParams();

            params.setBodyText("");
            // params.setToAddr(mailIds);
            params.setSubject(schedule.getSchedulerName() + " : System CPU Load is very high, scheduler will be delayed 5 minutes");
            params.setHasAttach(false);
            mailer = new PbMail(params);
            for (int j = 0; j < allAddress.length; j++) {
                try {
                    params.setToAddr(allAddress[j]);
                    mailer.sendMail();
                    ScheduleLogger.addLogEntry(schedule.getReportScheduledId(), "Scheduler", "Success");
                } catch (Exception e) {
                    logger.error("Exception: ", e);
                    ScheduleLogger.addLogEntry(schedule.getReportScheduledId(), "Scheduler", "Failed");
                }
            }

            //Code to reschedule the current scheduler
            String scheduleTime = schedule.getScheduledTime();
            String[] splitScheduleTime = scheduleTime.split(":");
            //sysMin = sysMin + aftrEtlTime
            int tmpHr = 0;
            int tmpMin = Integer.parseInt(splitScheduleTime[1]) + 5;
            if (tmpMin >= 60) {
                tmpHr = Integer.parseInt(splitScheduleTime[0]) + tmpMin / 60;
                tmpMin = tmpMin % 60;
            } else {
                tmpHr = Integer.parseInt(splitScheduleTime[0]);
            }
            scheduleTime = String.valueOf(tmpHr).concat(":").concat(String.valueOf(tmpMin));
            schedule.setScheduledTime(scheduleTime);
            SchedulerBD schedBD = new SchedulerBD();
            schedBD.scheduleReport(schedule, true);

            //end of code
        } else {
            logger.info("Now its....continue.................in ExportScheduler else block");
            String userMessage = schedule.getUserMessage();
            String reportId = String.valueOf(schedule.getReportId());
            String userId = schedule.getUserId();
            String[] reportIds;
            String[] eReportIds;
            eReportIds = schedule.getReportIds();
            //reportIds[0] = reportId;
            String result = "";
            DataSnapshotBD snapshotBD = new DataSnapshotBD();
            List<ReportSchedulePreferences> schdPreferenceList = schedule.getReportSchedulePrefrences();
            ArrayList<String> fileNames = new ArrayList<String>();
            StringBuilder tempSb = new StringBuilder("");
            String contentType = schedule.getContentType();
            String schedulerId = String.valueOf(schedule.getReportScheduledId());
            if (schedule.isExportReportSchedule()) {
                PbReportViewerBD bd = new PbReportViewerBD();
                String reportStatus = bd.generateReportForExportReportSchedule(schedule, eReportIds, schedulerId, userId, contentType, schedule.getSchedulerName());
            }

            try {
                params = new PbMailParams();
                //params.setBodyText(completeContent.toString());
                if (userMessage != null && !userMessage.isEmpty()) {
                    params.setBodyText("<h3>" + userMessage + "</h3>");
                } else {
                    params.setBodyText(" ");
                }
                params.setToAddr(schedule.getReportmailIds());
                params.setSubject(schedule.getSchedulerName());
//                if(contentType.equals("H")){
//                params.setBodyText(completeContent.toString().replace("<Font color=>", "<Font color=#000000>"));
//                params.setHasAttach(false);
//                }else{
                params.setHasAttach(true);
                //}
                ArrayList<String> attach = new ArrayList<String>();
//                String str = new String(schedule.getUploadedFilePath());
                String str = schedule.getUploadedFilePath();
                //Added by amar to send temporary tempalete as attached file
//                String fileName = new String(schedule.getUploadedFileName());
                String fileName = schedule.getUploadedFileName();
                String[] fileWtExt = fileName.split(".xls");
                String[] filepath = str.replaceAll("[()]", "").split(fileName.replace("[()]", ""));
                String zipFilePath = "";
                String tempFileUrl = filepath[0] + "Temp" + fileName;
                //Code added by Amar to find the file size and zip it if more than 10 mb
                File fileT = new File(tempFileUrl);
                long fileSz = fileT.length();
                fileSz = fileSz / 1024;
                fileSz = fileSz / 1024;
                if (fileSz > 10) {
                    try {
                        zipFilePath = filepath[0] + "Temp" + fileWtExt[0] + ".zip";
                        //create ZipOutputStream to write to the zip file
                        FileOutputStream fos = new FileOutputStream(zipFilePath);
                        ZipOutputStream zos = new ZipOutputStream(fos);
                        //add a new Zip Entry to the ZipOutputStream
                        ZipEntry ze = new ZipEntry(fileT.getName());
                        zos.putNextEntry(ze);
                        //read the file and write to ZipOutputStream
                        FileInputStream fis = new FileInputStream(fileT);
                        byte[] buffer = new byte[1024];
                        int len;
                        while ((len = fis.read(buffer)) > 0) {
                            zos.write(buffer, 0, len);
                        }

                        //Close the zip entry to write to zip file
                        zos.closeEntry();
                        //Close resources
                        zos.close();
                        fis.close();
                        fos.close();
                        params.createdZipFile(true);
                        attach.add(zipFilePath);
                        logger.info(fileT.getCanonicalPath() + " is zipped to " + zipFilePath);

                    } catch (IOException e) {
                        logger.error("Exception: ", e);
                    }

                } else {
                    attach.add(tempFileUrl);
                }
                //End of code
                // attach.add(tempFileUrl);
                //end of code
                //attach.add(str);
                params.setAttachFile(attach);
                mailer = new PbMail(params);
                boolean status;
                status = mailer.sendExportReportMail();
                if (status) {
                    ReportTemplateDAO dao = new ReportTemplateDAO();
                    dao.updateSchedulerStatusAndDate(schedulerId, eReportIds[0], userId);
                }
                //Added by amar to delete temp file
                File tempFileToDelete = new File(tempFileUrl);
                if (tempFileToDelete.exists()) {
                    tempFileToDelete.delete();
                }
                if (params.getZipFile()) {
                    File tempZipFile = new File(zipFilePath);
                    if (tempZipFile.exists()) {
                        tempZipFile.delete();
                    }
                }
                //end of code
            } catch (Exception e) {
                logger.error("Exception: ", e);
            }
        }//this.deleteFiles(fileNames);
    }
    //Code added by Amar for file download from network drive

    public void downloadFromNetworkDrive() {
        FileInputStream reader = null;
        FileOutputStream writer = null;
        try {
            File file = new File("\\\\192.168.0.27\\usr\\local\\cache\\readme.txt");
            reader = new FileInputStream(file);
            String filePath = "usr" + File.separator + "local" + File.separator + "cache" + File.separator + "filename.txt";
            writer = new FileOutputStream(new File(filePath));
            byte[] buff = new byte[1024];
            int byteRead;
            while ((byteRead = reader.read(buff)) > 0) {
                writer.write(buff, 0, byteRead);
            }
            logger.info(file.getAbsolutePath());
            reader.close();
            writer.close();
        } catch (Exception ef) {
            logger.error("Exception: ", ef);
        } finally {
        }
        //followed by printing the contents of file

    }
    //end of code
}
