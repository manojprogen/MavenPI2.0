<%--
    Document   : editSchedulers
    Created on : 7 Sep, 2012, 12:08:29 PM
    Author     : progen
--%>

<%@page import="com.progen.scheduler.KPIAlertSchedule"%>
<%@page import="java.util.Locale"%>
<%@page import="com.progen.i18n.TranslaterHelper"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="com.progen.scheduler.ReportSchedule"%>
<%@page import="com.google.gson.reflect.TypeToken"%>
<%@page import="com.google.gson.Gson"%>
<%@page import="java.util.List"%>
<%@page import="java.util.LinkedList"%>
<%@page import="prg.db.PbReturnObject"%>
<%@page import="com.progen.reportview.db.PbReportViewerDAO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%      //for clearing cache
    response.setHeader("Cache-Control", "no-store");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);
    //added by Dinanath dec 2015
    Locale cle = null;
    cle = (Locale) session.getAttribute("UserLocaleFormat");
    String themeColor = "blue";
    if (session.getAttribute("theme") == null) {
        session.setAttribute("theme", themeColor);
    } else {
        themeColor = String.valueOf(session.getAttribute("theme"));
    }
    PbReportViewerDAO dao = new PbReportViewerDAO();
    PbReturnObject retobj = dao.getAllSchedules();
    List<String> schedulerId = new LinkedList<String>();
    List<String> userName = new LinkedList<String>();
    List<String> reportName = new LinkedList<String>();
    List<ReportSchedule> schedule = new LinkedList<ReportSchedule>();
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    String[] scheduleday = {"Mon", "Tue", "Wed", "Thur", "Fri", "Sat", "Sun"};
    String[] sday = {"2", "3", "4", "5", "6", "7", "1"};
    for (int i = 0; i < retobj.getRowCount(); i++) {
        schedulerId.add(retobj.getFieldValueString(i, "PRG_PERSONALIZED_ID"));
        userName.add(retobj.getFieldValueString(i, "USERNAME"));
        reportName.add(retobj.getFieldValueString(i, "REPORTNAME"));
        Gson json = new Gson();
        java.lang.reflect.Type type = new TypeToken<List<ReportSchedule>>() {
        }.getType();
        List<ReportSchedule> scheduleList = json.fromJson(retobj.getFieldValueString(i, "SCHEDULER_DETAILS"), type);
        Date sdate = retobj.getFieldValueDate(i, "SCHEDULE_START_DATE");
        Date edate = retobj.getFieldValueDate(i, "SCHEDULE_END_DATE");
        //Added By Amar to add last sent date
        Date ldate = retobj.getFieldValueDate(i, "LAST_SENT_DATE");
        String modType = retobj.getFieldValueString(i, "MODULE_TYPE");
        //end of code
        if (scheduleList != null && !scheduleList.isEmpty()) {
            for (ReportSchedule schedule1 : scheduleList) {
                schedule1.setStartDate(sdate);
                schedule1.setEndDate(edate);
                schedule1.setLastSentDate(ldate);
                schedule1.setModuleType(modType);
                schedule.add(schedule1);
            }
        }
    }
    //Added By Ram 31Oct2015 for Dashboard schedules
    PbReturnObject retobjDB = dao.getAllDashboardSchedules();
    List<String> schedulerIdDB = new LinkedList<String>();
    List<String> userNameDB = new LinkedList<String>();
    List<String> reportNameDB = new LinkedList<String>();
    List<ReportSchedule> scheduleDB = new LinkedList<ReportSchedule>();
    SimpleDateFormat dateFormatDB = new SimpleDateFormat("dd-MM-yyyy");
    String[] scheduledayDB = {"Mon", "Tue", "Wed", "Thur", "Fri", "Sat", "Sun"};
    String[] sdayDB = {"2", "3", "4", "5", "6", "7", "1"};
    for (int i = 0; i < retobjDB.getRowCount(); i++) {
        schedulerIdDB.add(retobjDB.getFieldValueString(i, "MEASURE_ID"));
        userNameDB.add(retobjDB.getFieldValueString(i, "USERNAME"));
        reportNameDB.add(retobjDB.getFieldValueString(i, "DASHBOARD_NAME"));
        Gson json = new Gson();
        java.lang.reflect.Type type = new TypeToken<List<ReportSchedule>>() {
        }.getType();
        List<ReportSchedule> scheduleListDB = json.fromJson(retobjDB.getFieldValueString(i, "ALERTS_DETAILS"), type);
        Date sdate = retobjDB.getFieldValueDate(i, "ALERT_START_DATE");
        Date edate = retobjDB.getFieldValueDate(i, "ALERT_END_DATE");
        if (scheduleListDB != null && !scheduleListDB.isEmpty()) {
            for (ReportSchedule schedule1 : scheduleListDB) {
                schedule1.setStartDate(sdate);
                schedule1.setEndDate(edate);
                scheduleDB.add(schedule1);
            }
        }
    }
    //added by Dinanath for kpi chart schedulers
    PbReturnObject retobj3 = dao.getAllKPIChartScheduler();
    List<String> schedulerId3 = new LinkedList<String>();
    List<String> userName3 = new LinkedList<String>();
    List<String> reportName3 = new LinkedList<String>();
    List<KPIAlertSchedule> schedule3 = new LinkedList<KPIAlertSchedule>();
    //SimpleDateFormat dateFormat3 = new SimpleDateFormat("dd-MM-yyyy");
    //String[] scheduleday = {"Mon", "Tue", "Wed", "Thur", "Fri", "Sat", "Sun"};
    //String[] sday = {"2", "3", "4", "5", "6", "7", "1"};
    for (int i = 0; i < retobj3.getRowCount(); i++) {
        schedulerId3.add(retobj3.getFieldValueString(i, "PRG_PERSONALIZED_ID"));
        userName3.add(retobj3.getFieldValueString(i, "USERNAME"));
        reportName3.add(retobj3.getFieldValueString(i, "REPORTNAME"));
        Gson json = new Gson();
        java.lang.reflect.Type type = new TypeToken<List<KPIAlertSchedule>>() {
        }.getType();
        List<KPIAlertSchedule> scheduleList = json.fromJson(retobj3.getFieldValueString(i, "SCHEDULER_DETAILS"), type);
        Date sdate3 = retobj3.getFieldValueDate(i, "SCHEDULE_START_DATE");
        Date edate3 = retobj3.getFieldValueDate(i, "SCHEDULE_END_DATE");
        Date ldate = retobj3.getFieldValueDate(i, "LAST_SENT_DATE");
        String modType = retobj3.getFieldValueString(i, "MODULE_TYPE");
        if (scheduleList != null && !scheduleList.isEmpty()) {
            for (KPIAlertSchedule schedule1 : scheduleList) {
                schedule1.setStartDate(sdate3);
                schedule1.setEndDate(edate3);
                schedule1.setLastSentDate(ldate);
                schedule1.setModuleType(modType);
                schedule3.add(schedule1);
            }
        }
    }

    Date today = new Date();
    String contextPath = request.getContextPath();
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script type="text/javascript" src="<%=contextPath%>/javascript/lib/jquery/js/jquery-1.4.2.min.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=themeColor%>/jquery-ui-1.7.3.custom.css" rel="stylesheet" />
        <script type="text/javascript" src="<%=contextPath%>/javascript/jquery.tablesorter.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/tablesorter/jquery.tablesorter.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/tablesorter/addons/pager/jquery.tablesorter.pager.js"></script>
        <link type="text/css" href="<%=contextPath%>/jQuery/jquery/demos.css" rel="stylesheet" />
        <script type="text/javascript" src="<%=contextPath%>/jQuery/jquery/ui/jquery.easing.1.3.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/ui.tabs.js"></script>
        <link rel="stylesheet" href="<%=contextPath%>/tablesorter/themes/blue/style.css" type="text/css" media="print, projection, screen" />
        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/themes/<%=themeColor%>/style.css" type="text/css" media="print, projection, screen">
        <link type="text/css" href="<%=contextPath%>/stylesheets/base/ui.all.css" rel="stylesheet" />
        <link type="text/css" href="<%=contextPath%>/jQuery/jquery/demos.css" rel="stylesheet" />
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=themeColor%>/metadataButton.css" rel="stylesheet" />
        <script type="text/javascript" src="<%= contextPath%>/JS/global.js"></script>
        <script type="text/javascript">
            $(window).resize(function () {
                resizePage("editSchedulers");
            });
            $(document).ready(function () {
                resizePage("editSchedulers");
                var isReport = true;
                var isUpdReport = true;
                $("#editTableSchedule,#editTableScheduleDashboard").tablesorter();
                $("#editTableScheduleDashboard").hide();
//                .tablesorterPager({container: $('#pagerUserList')})
                //   .tablesorterPager({container: $('#pagerUserList')})
                //added by sruhti to display cpudetails and memorydetails
//                $.ajax({
//                    url:'<%=request.getContextPath()%>/reportViewer.do?reportBy=getSystemDetails',
//                    success:function(data)
//                    {
//                        if(data=='')
//                        {
//                            alert("No Data To Display")
//                        }
//                        $("#SystemDetails").html(data)
//                    }
//                });//ended by sruthi
            });
        </script>
 <script type="text/javascript">
            $(window).resize(function () {
                resizePage("editSchedulers");
            });

            if ($.browser.msie == true) {
                $("#ScheduleReport").dialog({
                    autoOpen: false,
                    height: 490,
                    width: 500,
                    position: 'justify',
                    modal: false

                });
                $('#sDatepicker').datepicker({
                    changeMonth: true,
                    changeYear: true
                });
                $('#eDatepicker').datepicker({
                    changeMonth: true,
                    changeYear: true
                });
                $("#SendScheduleNow").dialog({
                    autoOpen: false,
                    height: 400,
                    width: 500,
                    position: 'justify',
                    modal: false

                });
                $("#SendKPIChartScheduleNow").dialog({
                    autoOpen: false,
                    height: 400,
                    width: 500,
                    position: 'justify',
                    modal: false

                });
            } else {
                $("#ScheduleReport").dialog({
                    autoOpen: false,
                    height: 490,
                    width: 500,
                    position: 'justify',
                    modal: false
                });
                $('#sDatepicker').datepicker({
                    changeMonth: true,
                    changeYear: true
                });
                $('#eDatepicker').datepicker({
                    changeMonth: true,
                    changeYear: true
                });
                $("#SendScheduleNow").dialog({
                    autoOpen: false,
                    height: 400,
                    width: 500,
                    position: 'justify',
                    modal: false

                });
                $("#SendKPIChartScheduleNow").dialog({
                    autoOpen: false,
                    height: 400,
                    width: 500,
                    position: 'justify',
                    modal: false

                });
            }
            var pbReportId = "";
            var schedulerId = "";
            function deleteSchedule(sId) {
                var confirmDel = false;
                var confirmDel = confirm("Do you want to Delete Schedule");
                if (confirmDel == true) {
                    $.ajax({
                        url: '<%=request.getContextPath()%>/reportViewer.do?reportBy=deleteSchedulerDetails&schedulerId=' + sId,
                        success: function (data)
                        {
                            window.location.href = window.location.href;
                        }
                    });
                }
            }
            function deleteDashboardSchedule(sId) {
                var confirmDel = false;
                var confirmDel = confirm("Do you want to Delete Schedule");
                if (confirmDel == true) {
                    $.ajax({
                        url: '<%=request.getContextPath()%>/reportViewer.do?reportBy=deleteDashboardSchedulerDetails&schedulerId=' + sId,
                        success: function (data)
                        {
                            window.location.href = window.location.href;
                        }
                    });
                }
            }
            function editSchedule(reportId, scheduleId) {
                isUpdReport = true;
                pbReportId = reportId;
                schedulerId = scheduleId;
                var confirmDel = false;
                confirmDel = confirm("Do you want to Edit Scheduler Details");
                if (confirmDel == true) {
                    $.ajax({
                        url: '<%=request.getContextPath()%>/reportViewer.do?reportBy=editSchedulerDetails&reportId=' + reportId + '&schedulerId=' + scheduleId,
                        success: function (data)
                        {
                            if (data != 'null') {
                                var jsonVar = eval('(' + data + ')')
                                $("#scheduleName").val(jsonVar.schedulerName);
                                $("#usertextarea").val(jsonVar.ReportmailIds);
                                $("select#fileType").attr("value", jsonVar.contentType);
                                var sd = new Date(jsonVar.startDate);
                                var ed = new Date(jsonVar.endDate);
                                // alert(ld);
//                     var strtDate=1+(sd.getMonth())+"/"+sd.getDate()+"/"+sd.getFullYear();
//                     var endDate=1+(ed.getMonth())+"/"+ed.getDate()+"/"+ed.getFullYear();
                                var strtDate = sd.getDate() + "/" + (1 + (sd.getMonth())) + "/" + sd.getFullYear();
                                var endDate = ed.getDate() + "/" + (1 + (ed.getMonth())) + "/" + ed.getFullYear();
                                var particularDay = jsonVar.particularDay;
                                $("select#frequency").attr("value", jsonVar.frequency);
                                if (jsonVar.frequency == "Weekly") {
                                    $("#weekday").show();
                                    $("select#particularDay").attr("value", particularDay);
                                    $("#monthday").hide();
                                } else if (jsonVar.frequency == "Monthly") {
                                    $("#weekday").hide();
                                    $("#monthday").show();
                                    $("select#monthParticularDay").attr("value", particularDay);
                                } else {
                                    $("#weekday").hide();
                                    $("#monthday").hide();
                                }

                                $("#sDatepicker").val(strtDate);
                                $("#eDatepicker").val(endDate);
                                $("select#Data").attr("value", jsonVar.dataSelection);
                                var time = jsonVar.scheduledTime.split(":");
                                var isRepSchedule = jsonVar.isReportSchedule;
                                var isGOSchedule = jsonVar.isGO;
                                $("select#hrs").attr("value", time[0]);
                                $("select#mins").attr("value", time[1]);
                                if (isRepSchedule === true) {
                                    if (isGOSchedule == true) {
                                        $("#emailId").hide();
                                        $("#formatId").hide();
                                        $("#dailyReportScheduleForm").append('<input type="hidden" name="fromGO" id="fromGOId" value="" />');
                                        $("#fromGOId").val(jsonVar.isGO);
                                        $("#updateScheduleReport").show();
                                        $("#updateExportScheduleReport").hide();
                                    } else {
                                        $("#emailId").show();
                                        $("#formatId").show();
                                        $("#updateScheduleReport").show();
                                        $("#updateExportScheduleReport").hide();
                                    }
                                } else {
                                    //added By Amar for Multiple Excel Export
                                    if (jsonVar.isExportReportSchedule) {
                                        $("#dailyReportScheduleForm").append('<input type="hidden" name="filePath" id="filePathId" value="" />');
                                        $("#dailyReportScheduleForm").append('<input type="hidden" name="fileName" id="fileNameId" value="" />');
                                        $("#dailyReportScheduleForm").append('<input type="hidden" name="sheetNumber" id="sheetNumberId" value="" />');
                                        $("#dailyReportScheduleForm").append('<input type="hidden" name="lineNumber" id="lineNumberId" value="" />');
                                        $("#dailyReportScheduleForm").append('<input type="hidden" name="repIds" id="repIdsID" value="" />');
                                        $("#dailyReportScheduleForm").append('<input type="hidden" name="snapUrls" id="snapUrl" value="" />');
                                        $("#dailyReportScheduleForm").append('<input type="hidden" name="colNumber" id="colNumberId" value="" />');
                                        $("#dailyReportScheduleForm").append('<input type="hidden" name="header" id="headerIds" value="" />');
                                        $("#dailyReportScheduleForm").append('<input type="hidden" name="gtNames" id="gtIds" value="" />');
                                        $("#filePathId").val(jsonVar.uploadedFilePath);
                                        $("#fileNameId").val(jsonVar.uploadedFileName);
                                        $("#sheetNumberId").val(jsonVar.sheetNumbers);
                                        $("#lineNumberId").val(jsonVar.lineNumbers);
                                        $("#repIdsID").val(jsonVar.reportIds);
                                        $("#snapUrl").val(jsonVar.snapshotUrls);
                                        $("#colNumberId").val(jsonVar.colNumbers);
                                        $("#headerIds").val(jsonVar.headerValues);
                                        $("#gtIds").val(jsonVar.gTotals);
                                        $("#updateExportScheduleReport").show();
                                        $("#updateScheduleReport").hide();
                                    } else
                                        $("#updateScheduleReport").show();
                                }
                                //added by Dinanath for header logo as on 17/09/2015
                                var isHeaderLogoOn = jsonVar.isHeaderLogoOn;
                                var isFooterLogoOn = jsonVar.isFooterLogoOn;
                                var isOptionalHeaderTextOn = jsonVar.isOptionalHeaderTextOn;
                                var isOptionalFooterTextOn = jsonVar.isOptionalFooterTextOn;
                                var isHtmlSignatureOn = jsonVar.isHtmlSignatureOn;
//                                var isHideHeaderTable=jsonVar.isHideHeaderTable;
                                if (isHeaderLogoOn == "on") {
                                    $("#headerLogo").attr("checked", true);
                                } else {
                                    $("#headerLogo").attr("checked", false);
                                }
                                if (isFooterLogoOn == "on") {
                                    $("#footerLogo").attr("checked", true);
                                } else {
                                    $("#footerLogo").attr("checked", false);
                                }
                                if (isOptionalHeaderTextOn == "on") {
                                    $("#optionalHeader").attr('checked', true);
                                } else {
                                    $("#optionalHeader").attr('checked', false);
                                }
                                if (isOptionalFooterTextOn == "on") {
                                    $("#optionalFooter").attr('checked', true);
                                } else {
                                    $("#optionalFooter").attr('checked', false);
                                }
                                if (isHtmlSignatureOn == "on") {
                                    $("#htmlSignature").attr('checked', true);
                                } else {
                                    $("#htmlSignature").attr('checked', false);
                                }
//                       if(isHideHeaderTable=="on"){
//                                    $( "#hideHeaderTable").attr('checked', true);
//                                }else{
//                                    $( "#hideHeaderTable").attr('checked', false);
//                                }
                                //end of code by Dinanath
                                $("#saveScheduleReport").hide();
                                $("#schedulerId").val(scheduleId);
                                $("#ScheduleReport").dialog("open");
                            }
                        }
                    });
                }
            }
//Added By Ram 02Nov2015 for Dashboard Schedule
            function editScheduleDashboard(reportId, scheduleId) {
                isUpdReport = false;
                pbReportId = reportId;
                schedulerId = scheduleId;
                var confirmDel = false;
                confirmDel = confirm("Do you want to Edit Scheduler Details");
                if (confirmDel == true) {
                    $.ajax({
                        url: '<%=request.getContextPath()%>/reportViewer.do?reportBy=editSchedulerDashboardDetails&reportId' + reportId + '&schedulerId=' + scheduleId,
                        success: function (data)
                        {
                            if (data != 'null') {
//                        alert(data+"\n\n")
                                var jsonVar = eval('(' + data + ')')

                                $("#scheduleName").val(jsonVar.schedulerName);
                                $("#usertextarea").val(jsonVar.ReportmailIds);
                                $("select#fileType").attr("value", jsonVar.contentType);
                                var sd = new Date(jsonVar.startDate)
                                var ed = new Date(jsonVar.endDate)
                                var strtDate = sd.getDate() + "/" + (1 + (sd.getMonth())) + "/" + sd.getFullYear();
                                var endDate = ed.getDate() + "/" + (1 + (ed.getMonth())) + "/" + ed.getFullYear();
                                var particularDay = jsonVar.particularDay;
                                $("select#frequency").attr("value", jsonVar.frequency);
                                if (jsonVar.frequency == "Weekly") {
                                    $("#weekday").show();
                                    $("select#particularDay").attr("value", particularDay);
                                    $("#monthday").hide();
                                } else if (jsonVar.frequency == "Monthly") {
                                    $("#weekday").hide();
                                    $("#monthday").show();
                                    $("select#monthParticularDay").attr("value", particularDay);
                                } else {
                                    $("#weekday").hide();
                                    $("#monthday").hide();
                                }

                                $("#sDatepicker").val(strtDate);
                                $("#eDatepicker").val(endDate);
                                $("select#Data").attr("value", jsonVar.dataSelection);
                                var time = jsonVar.scheduledTime.split(":");
                                var isRepSchedule = jsonVar.isReportSchedule;
                                var isGOSchedule = jsonVar.isGO;
                                $("select#hrs").attr("value", time[0]);
                                $("select#mins").attr("value", time[1]);

                                $("#emailId").show();
                                $("#formatId").show();
                                $("#updateScheduleReport").show();
                                $("#updateExportScheduleReport").hide();

                                //added by Dinanath for header logo as on 17/09/2015
                                var isHeaderLogoOn = jsonVar.isHeaderLogoOn;
                                var isFooterLogoOn = jsonVar.isFooterLogoOn;
                                var isOptionalHeaderTextOn = jsonVar.isOptionalHeaderTextOn;
                                var isOptionalFooterTextOn = jsonVar.isOptionalFooterTextOn;
                                var isHtmlSignatureOn = jsonVar.isHtmlSignatureOn;
//                                var isHideHeaderTable=jsonVar.isHideHeaderTable;
                                if (isHeaderLogoOn == "on") {
                                    $("#headerLogo").attr("checked", true);
                                } else {
                                    $("#headerLogo").attr("checked", false);
                                }
                                if (isFooterLogoOn == "on") {
                                    $("#footerLogo").attr("checked", true);
                                } else {
                                    $("#footerLogo").attr("checked", false);
                                }
                                if (isOptionalHeaderTextOn == "on") {
                                    $("#optionalHeader").attr('checked', true);
                                } else {
                                    $("#optionalHeader").attr('checked', false);
                                }
                                if (isOptionalFooterTextOn == "on") {
                                    $("#optionalFooter").attr('checked', true);
                                } else {
                                    $("#optionalFooter").attr('checked', false);
                                }
                                if (isHtmlSignatureOn == "on") {
                                    $("#htmlSignature").attr('checked', true);
                                } else {
                                    $("#htmlSignature").attr('checked', false);
                                }
                                //end of code by Dinanath
                                $("#saveScheduleReport").hide();
                                $("#schedulerId").val(scheduleId);
                                $("#ScheduleReport").dialog("open");
                            }
                        }
                    });
                }
            }
            function updateScheduleReport() {
                $("#ScheduleReport").dialog("close");
//          $("#loading").show();
                var sId = $("#schedulerId").val();
                $.post("<%=request.getContextPath()%>/reportViewer.do?reportBy=updateScheduleDetails&reportId=" + pbReportId + '&schedulerId=' + sId + '&isUpdReport=' + isUpdReport, $("#dailyReportScheduleForm").serialize(),
                        function (data) {
                            alert("Report is Scheduled Sucessfully");

                        });

            }
//added By Amar for Multiple Excel Export
            function updateExportScheduleReport() {
                $("#ScheduleReport").dialog("close");
//          $("#loading").show();
                var sId = $("#schedulerId").val();
                $.post("<%=request.getContextPath()%>/reportViewer.do?reportBy=updateExportReportScheduleDetails&reportId=" + pbReportId + '&schedulerId=' + sId + '&isUpdReport=' + isUpdReport, $("#dailyReportScheduleForm").serialize(),
                        function (data) {
                            alert("Report is Scheduled Sucessfully");
                        });
            }
            function checkFrequency(id) {
                if ($("#" + id).val() == "Weekly") {
                    $("#weekday").show();
                    $("#monthday").hide();
                } else if ($("#" + id).val() == "Monthly") {
                    $("#weekday").hide();
                    $("#monthday").show();
                } else {
                    $("#monthday").hide();
                    $("#weekday").hide();
                }
            }
            function sendSchedule(reportId, scheduleId, checkReport) {
                if (checkReport === "report") {
                    isReport = true;
                } else
                    isReport = false;
                var confirmDel = false;
                $("#userEmails").val("");
                confirmDel = confirm("Are you sure you want to send scheduler now !!");
                if (confirmDel == true) {
                    $("#SendScheduleNow").dialog("open");
                    $("#schedulerId").val(scheduleId);
                    $("#reportId").val(reportId);
                    $("#userEmails").val(scheduleMailIds);
                    $("#userMesg").val('');
                }
            }
            //added by Dinanath
            function sendKPIChartSchedule(reportId, scheduleId, checkReport) {
                if (checkReport === "report") {
                    isReport = true;
                } else
                    isReport = false;
                var confirmDel = false;
                $("#userEmails").val("");
                confirmDel = confirm("Are you sure you want to send scheduler noww !!");
                if (confirmDel == true) {
                    $("#SendKPIChartScheduleNow").dialog("open");
                    $("#schedulerId").val(scheduleId);
                    $("#reportId").val(reportId);
                    $("#userEmailsKPI").val('');
                    $("#userMesgKPI").val('');
                }
            }
            function SendScheduleMailNow() {
                var schedulerId = $("#schedulerId").val();
                var reportId = $("#reportId").val();
                var emailIds = document.forms.SendScheduleMailForm.userEmails.value;
                var usrMesg = document.forms.SendScheduleMailForm.userMesg.value;
                $.post("<%=request.getContextPath()%>/reportViewer.do?reportBy=sendSchedulerNow&reportId=" + reportId + '&schedulerId=' + schedulerId + "&emailIds=" + encodeURIComponent(emailIds) + "&isReport=" + isReport + "&usrMesg=" + encodeURIComponent(usrMesg),
                        function (data) {
                            alert("Report is Scheduled Sucessfully");
                            $("#SendScheduleNow").dialog("close");
                        });

            }
            //added by Dinaanth
            function SendKPIChartMailNow() {
                var schedulerId = $("#schedulerId").val();
                var reportId = $("#reportId").val();
                var emailIds = document.forms.SendKPIChartScheduleNowForm.userEmailsKPI.value;
                var usrMesg = document.forms.SendKPIChartScheduleNowForm.userMesgKPI.value;
                $.post("<%=request.getContextPath()%>/reportViewerAction.do?reportBy=sendKPIChartSchedulerNow&reportId=" + reportId + '&schedulerId=' + schedulerId + "&emailIds=" + encodeURIComponent(emailIds) + "&isReport=" + isReport + "&usrMesg=" + encodeURIComponent(usrMesg),
                        function (data) {
                            alert("KPI Chart is Scheduled Sucessfully");
                            $("#SendKPIChartScheduleNow").dialog("close");
                        });
            }
            function RescheduleTimeRange() {

                $("#ReScheduleNow").dialog('open');
            }
//Added By Ram
            function disp() {
                var val = $("input[name=ram]:checked").map(
                        function () {
                            return this.value;
                        }).get().join(",");
                if (val == 'report') {
                    $("#editTableSchedule").show();
                    $("#editTableScheduleDashboard").hide();
                    $("#editKPIChartTableSchedule").hide();
                } else if(val=='dashboard') {
                    $("#editTableSchedule").hide();
                    $("#editTableScheduleDashboard").show();
                    $("#editKPIChartTableSchedule").hide();
                } else if(val=='kpichart') {
                    $("#editTableSchedule").hide();
                    $("#editTableScheduleDashboard").hide();
                    $("#editKPIChartTableSchedule").show();
                }
            }
            function delayAllSchedulerDaily() {


                $("#delayAllScheduler").dialog('open');
            }
            function ReScheduleReportsNow() {

                var stHrs = document.forms.RescheduleReportFormName.shrs.value;
                var stMins = document.forms.RescheduleReportFormName.smins.value;
                var endHrs = document.forms.RescheduleReportFormName.ehrs.value;
                var endMins = document.forms.RescheduleReportFormName.emins.value;
                var stTime = stHrs.concat(":").concat(stMins);
                var endTime = endHrs.concat(":").concat(endMins);

                alert("Report is Scheduled Sucessfully");
                $("#ReScheduleNow").dialog("close");
                $.post("<%=request.getContextPath()%>/reportViewer.do?reportBy=TimeBasedReschedulingReports&stTime=" + stTime + '&endTime=' + endTime,
                        function (data) {

                        });
            }
            function delayAllScheduler() {
                var stHrs = document.forms.delayAllSchedulerByTime.hrsOfDelay.value;
                var stMins = document.forms.delayAllSchedulerByTime.minsOfDelay.value;
                var incrOrDecrOpt = document.forms.delayAllSchedulerByTime.incrOrDecr.value;
//                alert(incrOrDecrOpt);
                var allschedId = [];
                var checkboxes = document.getElementsByName('SchedSelect[]');
                for (var i in checkboxes) {
                    if (checkboxes[i].checked) {
                        allschedId.push(checkboxes[i].value);
                    }
                }
                var stTime = stHrs.concat(":").concat(stMins);
                $("#delayAllScheduler").dialog("close");
                $.post("<%=request.getContextPath()%>/reportViewer.do?reportBy=delayAllSchedulerByGivenTime&stTime=" + stTime + '&preOrPostMode=' + incrOrDecrOpt + '&schedulerIdsArr=' + allschedId.toString(),
                        function (data) {
                            alert(data);
                        });
            }
            $("#ReScheduleNow").dialog({
                autoOpen: false,
                height: 250,
                width: 400,
                position: 'justify',
                modal: false
            });

            $("#delayAllScheduler").dialog({
                autoOpen: false,
                height: 250,
                width: 400,
                position: 'justify',
                modal: false
            });
            var kk = 1;
            function selectAllSched(source) {
                var checkboxes = document.getElementsByName('SchedSelect[]');
//   var checkAll=document.getElementsByID('RepSelect');
//   alert(checkboxes);
                kk++;

                for (var i in checkboxes) {
//        alert(checkboxes[i]);
//        alert(checkboxes[i].checked);
//        if(checkboxes[i].checked)
//        checkboxes[i].checked=false;    
//        else
                    if (kk % 2 == 0) {
                        checkboxes[i].checked = source.checked;
                    } else
                        checkboxes[i].checked = false;
                }

            }
        </script>
        <style type="text/css">
            #ui-datepicker-div{z-index:9999999}
            .ui-datepicker td {background-color: #A7A4A4;}
            .ui-datepicker td a {border: 1px solid #000;}
            table.tablesorter tbody td{background-color:#FFF;border-color:#f5f5f5;padding:4px;vertical-align:top}
            .black_overlay{display:none;position:absolute;top:0;left:0;width:110%;height:200%;background-color:#000;z-index:1001;-moz-opacity:.5;opacity:.5;filter:alpha(opacity=50);overflow:auto}
            .white_content{display:none;position:absolute;top:30%;left:35%;width:50%;height:50%;padding:16px;border:10px solid silver;background-color:#fff;z-index:1002;-moz-border-radius-bottomleft:6px;-moz-border-radius-bottomright:6px;-moz-border-radius-topleft:6px;-moz-border-radius-topright:6px}
            .myHead{font-family:Verdana;font-size:8pt;font-weight:700;color:#000;padding-left:12px;width:50%;background-color:#b4d9ee;border:0}
        </style>
    </head>
    <body>
        <div style="height: 10px;width: 100%;">
            <div style="float:left;margin-left: 5px;"><input onclick='disp()' type="radio"  style="margin:0px;" name="ram" value="report" checked> <span color="blue"> <%=TranslaterHelper.getTranslatedInLocale("Show_Report_Scheduler", cle)%></span></div>
            <div style="float:left;margin-left: 5px;"><input onclick='disp()' type="radio"  style="margin:0px;" name="ram" value="dashboard"><span color="blue"> <%=TranslaterHelper.getTranslatedInLocale("Show_Dashboard_Scheduler", cle)%></span></div>
            <div style="float:left;margin-left: 5px;"><input onclick='disp()' type="radio"  style="margin:0px;" name="ram" value="kpichart"><span color="blue"> <%=TranslaterHelper.getTranslatedInLocale("kpi_chart_scheduler", cle)%></span></div>
            <div style="float:right;margin-left: 5px;"><span> Time Based Rescheduling</span><a class="fa fa-pencil" style="margin-left: 5px;text-decoration:none;" title="Edit Schedule" onclick="RescheduleTimeRange()" href="javascript:void(0);"></a></div>
            <div style="float:right;margin-left: 5px;"><span> Delay Schedule</span><a class="fa fa-pencil" style="margin-left: 5px;text-decoration:none;" title="Delay All Schedule" onclick="delayAllSchedulerDaily(this, 'SchedSelect')" href="javascript:void(0);"></a></div>
        </div>       
            <br>
        <div id='editTableScheduleDiv' style='overflow: auto;border:1px solid #888'>
            <table id="editTableSchedule" class="tablesorter" align="center" cellspacing="0" cellpadding="0" border="1px" style="width:100%;margin:0px;" >
                <thead>
                <th><input name="SchedSelect" id="RepSelect" onclick="return selectAllSched(this, 'SchedSelect')" type="checkbox"></th>    
                <th>S.No</th>     
                <th><%=TranslaterHelper.getTranslatedInLocale("Schedule_Name", cle)%></th>
                <th><%=TranslaterHelper.getTranslatedInLocale("Report_Name", cle)%></th>
                <th><%=TranslaterHelper.getTranslatedInLocale("Owner", cle)%></th>
                <th><%=TranslaterHelper.getTranslatedInLocale("Start_Date", cle)%></th>
                <th><%=TranslaterHelper.getTranslatedInLocale("End_Date", cle)%></th>
                <th>Last Sent Date</th>
                <th><%=TranslaterHelper.getTranslatedInLocale("Time", cle)%></th>
                <th><%=TranslaterHelper.getTranslatedInLocale("Status", cle)%></th>
                <th>Scheduler Type</th>
                <th>Frequency</th>
                <th><%=TranslaterHelper.getTranslatedInLocale("Edit", cle)%></th>
                </thead>
                <tbody>
                    <% int rowcount = 1;
                        for (int i = 0; i < schedule.size(); i++) {
                            ReportSchedule rschedule = new ReportSchedule();
                            rschedule = schedule.get(i);%>
                    <tr>
                        <td width="3%"><input name="SchedSelect[]" id="RepSelect<%=rowcount%>"  value="<%=rschedule.getReportScheduledId()%>" type="checkbox"></td>
                        <td width="3%"><%=rowcount%></td>
                        <td width="15%"><%=rschedule.getSchedulerName()%></td>
                        <td width="15%"><%=reportName.get(i)%></td>
                        <td width="5%"><%=userName.get(i)%></td>
                        <td width="5%"><%=dateFormat.format(rschedule.getStartDate())%></td>
                        <td width="5%"><%=dateFormat.format(rschedule.getEndDate())%></td>
                        <%if (rschedule.getLastSentDate() != null) {%>
                        <td width="5%"><%=dateFormat.format(rschedule.getLastSentDate())%></td>
                        <%} else {%>
                        <td width="5%"></td>
                        <%}%>
                        <td width="4%"><%=rschedule.getScheduledTime()%></td>
                        <%if (today.before(rschedule.getEndDate())) {%>
                        <td width="5%"><%=TranslaterHelper.getTranslatedInLocale("Active", cle)%></td>
                        <%} else {%>
                        <td width="5%"><%=TranslaterHelper.getTranslatedInLocale("In_Active", cle)%></td>
                        <%}%>
                        <td width="5%"><%=rschedule.getModuleType()%></td>
                        <td width="5%"><%=rschedule.getFrequency()%></td>
                        <td width="10%">
                            <table>
                                <tr>
                                    <td><a class="ui-icon ui-icon-pencil" style="text-decoration:none;" title="Edit Schedule" onclick="editSchedule('<%=rschedule.getReportId()%>', '<%=rschedule.getReportScheduledId()%>')" href="javascript:void(0);"></a></td>
                                    <td><a class="ui-icon ui-icon-trash" style="text-decoration:none;" title="Delete Schedule" onclick="deleteSchedule('<%=rschedule.getReportScheduledId()%>')" href="javascript:void(0);"></a></td>
                                    <td><a class="ui-icon ui-icon-mail-closed" style="text-decoration:none;" title="Send Schedule Now" onclick="sendSchedule('<%=rschedule.getReportId()%>', '<%=rschedule.getReportScheduledId()%>', 'report')" href="javascript:void(0);"></a></td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                    <% rowcount++; }%>
                </tbody>

            </table>
            <!--        Added by Ram for Dashboard schedule    -->
            <table id="editTableScheduleDashboard" class="tablesorter" align="center" cellspacing="0" cellpadding="0" border="1px" style="display:none;width:100%;margin:0px;" >
                <thead>
                <th><%=TranslaterHelper.getTranslatedInLocale("Schedule_Name", cle)%></th>
                <th><%=TranslaterHelper.getTranslatedInLocale("Report_Name", cle)%></th>
                <th><%=TranslaterHelper.getTranslatedInLocale("Owner", cle)%></th>
                <th><%=TranslaterHelper.getTranslatedInLocale("Start_Date", cle)%></th>
                <th><%=TranslaterHelper.getTranslatedInLocale("End_Date", cle)%></th>
                <th><%=TranslaterHelper.getTranslatedInLocale("Time", cle)%></th>
                <th><%=TranslaterHelper.getTranslatedInLocale("Status", cle)%></th>
                <th><%=TranslaterHelper.getTranslatedInLocale("Edit", cle)%></th>
                </thead>
                <tbody>
                    <% for (int i = 0; i < scheduleDB.size(); i++) {
                            ReportSchedule rschedule = new ReportSchedule();
                            rschedule = scheduleDB.get(i);%>
                    <tr>
                        <td width="15%"><%=rschedule.getSchedulerName()%></td>
                        <td width="15%"><%=reportNameDB.get(i)%></td>
                        <td width="10%"><%=userNameDB.get(i)%></td>
                        <td width="10%"><%=dateFormat.format(rschedule.getStartDate())%></td>
                        <td width="10%"><%=dateFormat.format(rschedule.getEndDate())%></td>
                        <td width="5%"><%=rschedule.getScheduledTime()%></td>
                        <%if (today.before(rschedule.getEndDate())) {%>
                        <td width="5%"><%=TranslaterHelper.getTranslatedInLocale("Active", cle)%></td>
                        <%} else {%>
                        <td width="5%"><%=TranslaterHelper.getTranslatedInLocale("In_Active", cle)%></td>
                        <%}%>
                        <td width="20%">
                            <table>
                                <tr>
                                    <td><a class="ui-icon ui-icon-pencil" style="text-decoration:none;" title="Edit Schedule" onclick="editScheduleDashboard('<%=rschedule.getReportId()%>', '<%=rschedule.getReportScheduledId()%>')" href="javascript:void(0);"></a></td>
                                    <td><a class="ui-icon ui-icon-trash" style="text-decoration:none;" title="Delete Schedule" onclick="deleteDashboardSchedule('<%=rschedule.getReportScheduledId()%>')" href="javascript:void(0);"></a></td>
                                    <td><a class="ui-icon ui-icon-mail-closed" style="text-decoration:none;" title="Send Schedule Now" onclick="sendSchedule('<%=rschedule.getReportId()%>', '<%=rschedule.getReportScheduledId()%>', 'dashboard')" href="javascript:void(0);"></a></td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                    <% }%>
                </tbody>

            </table>
            <table id="editKPIChartTableSchedule" class="tablesorter" align="center" cellspacing="0" cellpadding="0" border="1px" style="width:100%;margin:0px;" >
                <thead>
                <th><input name="SchedSelect" id="RepSelect" onclick="return selectAllSched(this, 'SchedSelect')" type="checkbox"></th>    
                <th>S.No</th>     
                <th><%=TranslaterHelper.getTranslatedInLocale("Schedule_Name", cle)%></th>
                <th><%=TranslaterHelper.getTranslatedInLocale("Report_Name", cle)%></th>
                <th><%=TranslaterHelper.getTranslatedInLocale("Owner", cle)%></th>
                <th><%=TranslaterHelper.getTranslatedInLocale("Start_Date", cle)%></th>
                <th><%=TranslaterHelper.getTranslatedInLocale("End_Date", cle)%></th>
                <th>Last Sent Date</th>
                <th><%=TranslaterHelper.getTranslatedInLocale("Time", cle)%></th>
                <th><%=TranslaterHelper.getTranslatedInLocale("Status", cle)%></th>
                <th>Scheduler Type</th>
                <th>Frequency</th>
                <th><%=TranslaterHelper.getTranslatedInLocale("Edit", cle)%></th>
                </thead>
                <tbody>
                    <% int rowcount2 = 1;
                        for (int i = 0; i < schedule3.size(); i++) {
                            KPIAlertSchedule rschedule = new KPIAlertSchedule();
                            rschedule = schedule3.get(i);%>
                    <tr>
                        <td width="3%"><input name="SchedSelect[]" id="RepSelect<%=rowcount2%>"  value="<%=rschedule.getKpiAlertSchedId()%>" type="checkbox"></td>
                        <td width="3%"><%=rowcount2%></td>
                        <td width="15%"><%=rschedule.getKpiAlertSchedName()%></td>
                        <td width="15%"><%=reportName3.get(i)%></td>
                        <td width="5%"><%=userName3.get(i)%></td>
                        <td width="5%"><%=dateFormat.format(rschedule.getStartDate())%></td>
                        <td width="5%"><%=dateFormat.format(rschedule.getEndDate())%></td>
                        <%if (rschedule.getLastSentDate() != null) {%>
                        <td width="5%"><%=dateFormat.format(rschedule.getLastSentDate())%></td>
                        <%} else {%>
                        <td width="5%"></td>
                        <%}%>
                        <td width="4%"><%=rschedule.getScheduledTime()%></td>
                        <%if (today.before(rschedule.getEndDate())) {%>
                        <td width="5%"><%=TranslaterHelper.getTranslatedInLocale("Active", cle)%></td>
                        <%} else {%>
                        <td width="5%"><%=TranslaterHelper.getTranslatedInLocale("In_Active", cle)%></td>
                        <%}%>
                        <td width="5%"><%=rschedule.getModuleType()%></td>
                        <td width="5%"><%=rschedule.getFrequency()%></td>
                        <td width="10%">
                            <table>
                                <tr>
                                    <td><a class="ui-icon ui-icon-pencil" style="text-decoration:none;" title="Edit Schedule" onclick="editKPIChartSchedule('<%=rschedule.getReportId()%>', '<%=rschedule.getKpiAlertSchedId()%>')" href="javascript:void(0);"></a></td>
                                    <td><a class="ui-icon ui-icon-trash" style="text-decoration:none;" title="Delete Schedule" onclick="deleteKPIChartSchedule('<%=rschedule.getKpiAlertSchedId()%>')" href="javascript:void(0);"></a></td>
                                    <td><a class="ui-icon ui-icon-mail-closed" style="text-decoration:none;" title="Send Schedule Now" onclick="sendKPIChartSchedule('<%=rschedule.getReportId()%>', '<%=rschedule.getKpiAlertSchedId()%>', 'kpichart')" href="javascript:void(0);"></a></td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                    <% rowcount2++; }%>
                </tbody>

            </table>
        </div>
        <div id="SendScheduleNow" style="display:none" title="Send Schedule Report Now">
            <form action="" name="SendScheduleMailForm" id="SendScheduleMailForm" method="post">
                <table>
                    <tr><td>&nbsp; </td></tr>
                    <tr><td>&nbsp;</td></tr>
                    <tr><td class="myhead">Email To : &nbsp;</td><td><textarea id="userEmails" style="width: 280px; height: 80px;" rows="" cols="" name="userEmails"></textarea></td></tr>
                    <tr><td>&nbsp; </td></tr>
                    <tr><td class="myhead">Message : &nbsp;</td><td><textarea id="userMesg" style="width: 280px; height: 80px;" rows="" cols="" name="userMesg"></textarea></td></tr>
                    <tr><td>&nbsp; </td></tr>
                    <tr><td align="center" colspan="2"><font size="1" color="red">*</font>Please separate multiple Email Id's by comma(,).</td></tr>
                    <tr><td>&nbsp; </td></tr>
                    <tr><td id="SendScheduleMailNow" align="center" colspan="2"><input class="navtitle-hover" type="button" onclick="SendScheduleMailNow()" value="Send Now"></td></tr>
                </table>
            </form>
        </div>
                <!--added by Dinanath-->
        <div id="SendKPIChartScheduleNow" style="display:none" title="Send Schedule KPI chart Now">
            <form action="" name="SendKPIChartScheduleNowForm" id="SendKPIChartScheduleNowForm" method="post">
                <table>
                    <tr><td>&nbsp; </td></tr>
                    <tr><td>&nbsp;</td></tr>
                    <tr><td class="myhead">Email To : &nbsp;</td><td><textarea id="userEmailsKPI" style="width: 280px; height: 80px;" rows="" cols="" name="userEmailsKPI"></textarea></td></tr>
                    <tr><td>&nbsp; </td></tr>
                    <tr><td class="myhead">Message : &nbsp;</td><td><textarea id="userMesgKPI" style="width: 280px; height: 80px;" rows="" cols="" name="userMesgKPI"></textarea></td></tr>
                    <tr><td>&nbsp; </td></tr>
                    <tr><td align="center" colspan="2"><font size="1" color="red">*</font>Please separate multiple Email Id's by comma(,).</td></tr>
                    <tr><td>&nbsp; </td></tr>
                    <tr><td id="SendKPIChartScheduleNow1" align="center" colspan="2"><input class="navtitle-hover" type="button" onclick="SendKPIChartMailNow()" value="Send Now"></td></tr>
                </table>
            </form>
        </div>
        <div id="ScheduleReport" style="display:none" title="Schedule Report">
            <form action="" name="dailyReportScheduleForm" id="dailyReportScheduleForm" method="post">
                <table>
                    <tr><td class="myhead">Schedule Name</td><td><input id="scheduleName" type="text" value="" style="width: auto;" maxlength="100" name="scheduleName" readonly></td></tr>
                    <tr><td class="myhead">Email To</td><td><textarea id="usertextarea" style="width: 250px; height: 80px;" rows="" cols="" name="usertextarea"></textarea></td></tr>
                    <tr>
                        <td class="myhead">Format</td>
                        <td><select style="width: 130px;" id="fileType" name="fileType">
                                <option value="H">HTML</option>
                                <option value="E">Excel</option>
                            <option value="Ex">ExcelX</option>
                                <option value="P">PDF</option>
                            <option value="C">CSV</option>
                            <option value="M">Status</option>
                            </select>
                        </td>
                    </tr>
                    <tr><td class="myhead">StartDate</td><td><input id="sDatepicker"  type="text" value="" style="width: 120px;" maxlength="100" name="startdate" readonly=""></td></tr>
                    <tr><td class="myhead">End Date</td><td><input id="eDatepicker"  type="text" value="" style="width: 120px;" maxlength="100" name="enddate" readonly=""></td></tr>
                    <tr><td class="myhead">Time</td>
                        <td><table><tr><td>
                                        hrs<select name="hrs" id="hrs" >
                                            <%for (int i = 00; i < 24; i++) {%>
                                            <option  value="<%=i%>"><%=i%></option>
                                            <%}%>
                                        </select></td>
                                    <td>mins
                                        <select name="mins" id="mins">
                                            <%for (int i = 00; i < 60; i++) {%>
                                            <option  value="<%=i%>"><%=i%></option>
                                            <%}%>
                                        </select></td></tr></table>
                        </td>
                    </tr>
                    <tr>
                        <td class="myhead">Period</td>
                        <td>
                            <select id="Data" name="Data">
                                <option value="Current Day">Current Day</option>
                                <option value="Previous Day">Previous Day</option>
                                <option value="Report Date">Report Date</option>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td class="myhead">Frequency</td>
                        <td>
                            <select id="frequency" name="frequency" onchange="checkFrequency(this.id)">
                                <option value="Daily">Daily</option>
                                <option value="Weekly">Weekly</option>
                                <option value="Monthly">Monthly</option>
                            </select>
                        </td>
                    </tr>
                    <tr id="weekday" style="disaply:none;">
                        <td class="myhead">Week Day</td>
                        <td>
                            <select id="particularDay" name="particularDay">
                                <% for (int i = 0; i < scheduleday.length; i++) {%>
                                <option value="<%=sday[i]%>"><%=scheduleday[i]%></option>
                                <%}%>
                            </select>
                        </td>
                    </tr>
                    <tr id="monthday" style="display:none;">
                        <td class="myhead">Month Day</td>
                        <td>
                            <select id="monthParticularDay" name="monthParticularDay">
                                <% for (int i = 1; i <= 31; i++) {%>
                                <option value="<%=i%>"><%=i%></option>
                                <%}%>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2">
                            <table border="0" style="width:100%">
                                <tr style='width:100%'>
                                    <td style='width:40%'>Header Logo</td>
                                    <td style='width:10%'><input type='checkbox' id="headerLogo" name="headerLogo"></td>
                                    <td style='width:40%'>Footer Logo</td>
                                    <td style='width:10%'><input type='checkbox' id="footerLogo" name="footerLogo"></td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2">
                            <table border="0" style="width:100%">
                                <tr style='width:100%'>
                                    <td style='width:40%'>Optional Header Text</td>
                                    <td style='width:10%'><input type='checkbox' id="optionalHeader" name="optionalHeader"></td>
                                    <td style='width:40%'>Optional Footer Text</td>
                                    <td style='width:10%'><input type='checkbox' id="optionalFooter" name="optionalFooter"></td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2">
                            <table border="0" style="width:100%">
                                <tr style='width:100%'>
                                    <td style='width:40%'>Signature</td>
                                    <td style='width:10%'><input type='checkbox' id="htmlSignature" name="htmlSignature"></td>
                                    <td style='width:40%'></td>
                                    <td style='width:10%'></td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                    <tr><td colspan="2">&nbsp; </td></tr>

                    <tr>
                        <!--                     <td  id="saveScheduleReport" align="center"><input id="saveScheduleReport" class="navtitle-hover" type="button" onclick="SendScheduleReport()" value="Save Schedule">-->
                        <td id="updateScheduleReport" align="center" colspan="2"><input class="navtitle-hover" type="button" onclick="updateScheduleReport()" value="Update Schedule">
                        <td id="updateExportScheduleReport" align="center" colspan="2"><input class="navtitle-hover" type="button" onclick="updateExportScheduleReport()" value="Update Export Schedule">
                            <!--                     <td id="deleteScheduleReport" align="left"><input  class="navtitle-hover" type="button" onclick="deleteScheduleReport()" value="Delete Schedule">-->
                    </tr>
                    <tr><td colspan="2">&nbsp; </td></tr>
                    <tr><td colspan="2" align="center"><font size="1" color="red">*</font>Please separate multiple Email Id's by comma(,).</td></tr>
                </table>
            </form>
            <input type="hidden" id="schedulerId" name="schedulerId" value="">
            <input type="hidden" id="reportId" name="reportId" value="">
        </div>
       
    </body>
</html>
