<%--
    Document   : scheduleReport
    Created on : 25 Jan, 2011, 7:54:45 PM
    Author     : progen
--%>

<%@page import="com.progen.i18n.TranslaterHelper"%>
<%@page import="java.util.Locale"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="utils.db.*"%>
<%@page import="prg.db.*"%>
<%@page import="java.sql.*"%>
<%@page import="com.progen.report.*"%>
<%
            String themeColor="blue";
             Locale locale = null;
             String url="";
             int columnSize = 1;
             locale = (Locale) session.getAttribute("userLocale");
              if(session.getAttribute("theme")==null)
                session.setAttribute("theme",themeColor);
            else
                themeColor=String.valueOf(session.getAttribute("theme"));
             
                String schedulerId=request.getParameter("schedulerId");
                String isEdit=request.getParameter("isEdit");
                if (isEdit != null && "false".equalsIgnoreCase(isEdit))
                    session.removeAttribute("scheduler");
                //String[] days = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
                //String[] day={"SUN","MON","TUE","WED","THU","FRI","SAT"};

                String[] days = {"Beg of Week", "End of Week"};
                String[] day={"B","L"};

                String targetReportId = request.getParameter("targetReportId");
                String targetBizRole = request.getParameter("targetBizRole");
                String paramHtml = "";
                String paramXML = "";
                if (targetReportId == null){
                    targetReportId = "";
                    targetBizRole = "";
                }
                else{
                    Container container = Container.getContainerFromSession(request, targetReportId);
                    ReportParameter repParam = container.getReportParameter();
                    paramHtml = container.getParamHtml();
                    paramXML = repParam.toXml().toString();
                    url=request.getContextPath()+"/reportViewer.do?reportBy=viewReport&REPORTID="+targetReportId+"&action=open";
                }
        try {
%>

<html>
    <head>
        <title>piEE</title>
        <script src="<%=request.getContextPath()%>/javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <script type="text/javascript"  src="<%=request.getContextPath()%>/tracker/JS/dateSelection.js"></script>
<!--        <script type="text/javascript"  src="<%=request.getContextPath()%>/javascript/scheduler.js"></script>-->
        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/themes/<%=themeColor %>/ReportCss.css" rel="stylesheet" />
        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/themes/<%=themeColor %>/metadataButton.css" rel="stylesheet" />
        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/themes/<%=themeColor%>/TableCss.css" rel="stylesheet" />
        <link href="<%=request.getContextPath()%>/stylesheets/themes/<%=themeColor%>/style.css" rel="stylesheet" type="text/css">
         <style type="text/css">
            *{
               -x-system-font: none;
               font-family: verdana;
               font-size: 11px;
               font-size-adjust: none;
               font-stretch: normal;
               font-style: normal;
               font-variant: normal;
               font-weight: normal;
               line-height: normal;
            }
             .ajaxboxstyle {
                position: absolute;
                background-color: #FFFFFF;
                text-align: left;
                border: 1px solid #000000;
                border-top-width:1px;
                height:80px;
                width:180px;
                overflow:auto;
                overflow:hidden;
                margin:0em 0.5em;
            }
            .myAjaxTable {
                table-layout:fixed;
                background-color: #FFFFFF;
                text-align:left;
                border: 0px solid #000000;
                font-size:10px;
                left:4px;
                height:auto;
                border-collapse:separate;
                border-spacing:5px;
            }
             .suggestLink {
                position: relative;
                background-color: #FFFFFF;
                border: 0px solid #000000;
                border-top-width: 0px;
                padding: 2px 6px 2px 6px;
                left:3px;
                min-width: 20px;
                max-width: 150px;
            }
            .prgtable1
            {
                border: #silver solid 1px;
                font-size: 1.4em;
                border-collapse:collapse;
            }
    </style>
        <script type="text/javascript">

         var dimSelectedArr=new Array();
         var idx;
         var dimId;
         var dimName;
         var selectedView;
        var viewByName;
        var viewById;
        var colViewByName;
        var measureList;
        var paramList;
        var paramId;
        var dimSelectedViewArr=new Array();
        var viewIdx;
        var grpColArray=new Array();
        var fromReport = false;

        $(document).ready(function(){
            var tgtRepId = '<%=targetReportId%>';
            var tgtBizRole = '<%=targetBizRole%>';
            var paramXML = '<%=paramXML%>';
            var paramHTML = '<%=paramHtml%>';
            fromReport = false;
            
            if($.browser.msie==true)
            {
                $("#reportDetails").dialog({
                    autoOpen: false,
                    height:400,
                    width: 470,
                    position: 'justify',
                    modal: true
                });
                $("#checkViewDialog").dialog({
                    autoOpen: false,
                    height:420,
                    width: 410,
                    position: 'justify',
                    modal: true
                });
            }
            else
            {
                 $("#reportDetails").dialog({
                    autoOpen: false,
                    height:300,
                    width: 490,
                    position: 'justify',
                    modal: true
                 });
                 $("#checkViewDialog").dialog({
                    autoOpen: false,
                    height:420,
                    width: 410,
                    position: 'justify',
                    modal: true
                 });
            }

             $("#loading").show();
               if($("#frequency").val()=="1")
               $("#timeselect").show();

            if($("#emailType").val()=="normalSub")
               $("#toAddress").show();

                 $.ajax({
                    url: "<%=request.getContextPath()%>/scheduler.do?reportBy=getReportScheduleDetails&schedulerId=<%=schedulerId%>",
                     success: function(data){
                             var allJson=data.split("~");
                             var schedulerJson=eval('('+allJson[0]+')');
                            populateRoles(allJson[1]);
                            populateReports(allJson[2]);
                            populateReportDetails(allJson[3],schedulerJson.viewById);
                            populateParamDetails(allJson[3]);
                             var paramXml=schedulerJson.parameterXml;
                             if (paramXml && paramXml!="")
                                 fromReport = true;
                                $("#paramXml").val(paramXml);
                                $("#paramsDiv").html(schedulerJson.parameter);
                                $("select#selectSchdBusRoles").attr("value",schedulerJson.folderId);
                                $("select#reportList").attr("value",schedulerJson.reportId);
                                $("select#defaultViewByList").attr("value",schedulerJson.viewById);
                                 $("#schdReportName").val(schedulerJson.schedulerName);
                                 $("#checkedViewBy").val(schedulerJson.checkedViewByIds);

                            if(schedulerJson.parameter!="" &&  schedulerJson.parameter!=undefined )
                                  {
                                    var paramJson=eval('('+schedulerJson.parameter+')');
                                    var paramHtml="";
                                    for(var i=0;i<paramJson.paramId.length;i++)
                                    {
                                         if(paramId.indexOf(paramJson.paramId[i])!=-1)
                                         {
                                            paramHtml+="<font style=\"font-weight:bold;font-size:11px\" class='wordStyle' id='paramNames' name='paramName'>";
                                            paramHtml+=paramList[paramId.indexOf(paramJson.paramId[i])];
                                            paramHtml+=": </font><font class='wordStyle' id='paramValues' name='paramValue'>";
                                            paramHtml+=paramJson.paramName[i]+"</font> &nbsp&nbsp&nbsp&nbsp";
                                         }
                                    }
                                    $("#paramsDiv").html(paramHtml);
                                  }
                                if(schedulerJson.startDate!=null)
                                    {
                                        var sd = new Date(schedulerJson.startDate)
                                         var ed = new Date(schedulerJson.endDate)
                                         var strtDate=1+(sd.getMonth())+"/"+sd.getDate()+"/"+sd.getFullYear();
                                        var endDate=1+(ed.getMonth())+"/"+ed.getDate()+"/"+ed.getFullYear();
                                        var particularDay=schedulerJson.particularDay;
                                         $("#stDatepicker").val(strtDate);
                                        $("#edDatepicker").val(endDate);
                                         var time=schedulerJson.scheduledTime.split(":");
                                        $("select#hrs").attr("value",time[0]);
                                        $("select#mins").attr("value",time[1]);
                                    }

                                    $("select#selectSchdBusRoles").attr("value",schedulerJson.folderId);
                                    $("select#reportList").attr("value",schedulerJson.reportId);
                                     $("#schdReportName").val(schedulerJson.schedulerName);
                                     $("#dailyDataTD").hide();
                                     $("#monthlyDataTD").hide();
                                 if(schedulerJson.frequency=='Weekly')
                                 {
                                     $("select#frequency").attr("value","3");
                                      document.getElementById('dayOfWeek').style.display='';
                                      $("select#alertDay").attr("value",particularDay);
                                      document.getElementById("dataSelection").style.display='none';
                                 }
                                 else if(schedulerJson.frequency=='Monthly')
                                 {
                                     $("select#frequency").attr("value","2");
                                     document.getElementById('onlyDateSelect').style.display='';
                                     $("select#monthDate").attr("value",particularDay);
                                     $("#monthlyDataTD").show();
                                     $("select#monthlyData").attr("value",schedulerJson.dataSelection);
                                     if (particularDay != "B" && particularDay != "L")
                                         document.getElementById("dataSelection").style.display='';
                                     else
                                         document.getElementById("dataSelection").style.display='none';
                                 }
                                 else
                                 {
                                     $("select#frequency").attr("value","1");
                                     $("#dailyDataTD").show();
                                     $("select#dailyData").attr("value",schedulerJson.dataSelection);
                                 }


                                    $("#contentType").val(schedulerJson.contentType);

                                    if(schedulerJson.viewBy=='DefaultViewBy')
                                        {
                                            $('#DefaultViewBy').attr('checked', true);
                                            $("#viewBy").val("DefaultViewBy");
                                        }
                                    else if(schedulerJson.viewBy=='AllViewBy')
                                    {
                                        $('#AllViewBy').attr('checked', true);
                                        $("#viewBy").val("AllViewBy");
                                    }

                                    else if(schedulerJson.viewBy=='SelectedViewby')
                                        {
                                            $('#SelectedViewby').attr('checked', true);
                                            $("#sliceTable").show();
                                             $("#viewBySelected").val(schedulerJson.checkViewByNames);
                                             $("#viewBy").val("SelectedViewby");
                                        }
                                        else
                                             $('#DefaultViewBy').attr('checked', true);
                                    if(schedulerJson.reportSchedulePrefrences!=null)
                                    {
                                        var scheduleDetail=schedulerJson.reportSchedulePrefrences;
                                        for(var k=0;k<scheduleDetail.length;k++)
                                        {
                                            if(k>0)
                                                addDimEmailRow();
                                            $("#"+(k+2)+"dimDetail").val(scheduleDetail[k].dimValues);
                                            $("#"+(k+2)+"mail").val(scheduleDetail[k].mailIds);
                                        }
                                    }

                              if(schedulerJson.isAutoSplited==true)
                                    {
                                        $('#autoCheck').attr('checked','checked');
                                        $("#autoChckInput").val(true);
                                    }

                                    if('<%=isEdit%>'=='readOnly')
                                    {
                                        $('#schdReportName').attr("readonly", true);
                                        $('#selectSchdBusRoles').attr("disabled", true);
                                        $('#reportList').attr("disabled", true);
                                        $('#defaultViewByList').attr("disabled", true);
                                        $('#frequency').attr("disabled", true);
                                        $('#monthDate').attr("disabled", true);
                                        $('#alertDay').attr("disabled", true);
                                        $('#dayOfWeek').attr("disabled", true);
                                        $('#hrs').attr("disabled", true);
                                        $('#mins').attr("disabled", true);
                                        $('#contentType').attr("disabled", true);
                                        $('#autoCheck').attr("disabled", 'disabled');
                                         if(schedulerJson.viewBy=='DefaultViewBy')
                                        {
                                            document.getElementById('allViewByTd').style.display="none";
                                           document.getElementById('advOptionsTd').style.display="none";
                                        }
                                    else if(schedulerJson.viewBy=='AllViewBy')
                                    {
                                       document.getElementById('defaultViewByTd').style.display="none";
                                           document.getElementById('advOptionsTd').style.display="none";
                                    }

                                    else if(schedulerJson.viewBy=='SelectedViewby')
                                        {
                                           document.getElementById('allViewByTd').style.display="none";
                                           document.getElementById('defaultViewByTd').style.display="none";
                                        }
                                        for(var k=0;k<scheduleDetail.length;k++)
                                        {
                                            $("#"+(k+2)+"dimDetail").attr("readonly",true);
                                            $("#"+(k+2)+"mail").attr("readonly", true);
                                        }
                                        $("#saveButton").hide();
                                        $("#runButton").attr("align","center");
                                }

                                checKViewBys(true);

                                if (tgtRepId != "" && tgtBizRole != ""){

                                    var currVal = $("#selectSchdBusRoles").val();
                                    if (currVal == tgtBizRole){

                                    }
                                    else{
                                        $("#selectSchdBusRoles").val(tgtBizRole);
                                        //uploadReports(tgtRepId);
                                        //$("#reportList").val(tgtRepId);
                                    }

                                    var selBizRole = $('#selectSchdBusRoles option:selected').text();
                                    $("#schdBusRolesTxt").html(selBizRole);

                                    $("#selectSchdBusRoles").hide();
                                    $("#reportList").hide();

                                    //$('#selectSchdBusRoles').attr("disabled", true);
                                    //$('#reportList').attr("disabled", true);
                                    $("#fromReport").val("true");
                                    $("#paramXml").val(paramXML);
                                    $("#paramsDiv").html(paramHTML);
                                    $("#schedHomeBn").hide();
                                    $("#gotoReportBn").show();
                                }
                                else if (fromReport == true){   // coming from a report for editing.. 
                                    var selBizRole = $('#selectSchdBusRoles option:selected').text();
                                    $("#schdBusRolesTxt").html(selBizRole);
                                    var selRepId = $('#reportList option:selected').text();
                                    $("#schdrepListTxt").html(selRepId);

                                    $("#schedHomeBn").hide();
                                    $("#gotoReportBn").hide();
                                    $("#schdBusRolesTxt").show();
                                    $("#schdrepListTxt").show();
                                    $("#selectSchdBusRoles").hide();
                                    $("#reportList").hide();
                                }
                                else{
                                    $("#schedHomeBn").show();
                                    $("#gotoReportBn").hide();

                                    $("#schdBusRolesTxt").hide();
                                    $("#schdrepListTxt").hide();

                                }
                            }
                         });



                if('<%=isEdit%>'!='readOnly')
                {
                    $('#stDatepicker').datepicker({
                    changeMonth: true,
                    changeYear: true
                    });
                    $('#edDatepicker').datepicker({
                        changeMonth: true,
                        changeYear: true
                    });
                }

        addDate(null);

        });

         function goPaths(path){
                parent.closeStart();
                document.forms.scheduleReportForm.action=path;
                document.forms.scheduleReportForm.submit();
            }
            function viewReportG(path){
                document.forms.scheduleReportForm.action=path;
                document.forms.scheduleReportForm.submit();
            }
            function viewDashboardG(path){
                document.forms.scheduleReportForm.action=path;
                document.forms.scheduleReportForm.submit();
            }
    function saveReportSchedule(runFlag)
      {
          var viewBySelected=$("#viewBy").val();
            $("#rowCount").val(idx);
            $("#dimId").val(dimId);
          var strdate=document.getElementById("stDatepicker").value;
          var enddate=document.getElementById("edDatepicker").value;
          var scheduleName=document.getElementById("schdReportName").value;
          var mailid=document.getElementById("2mail").value;
          var schdleName=document.getElementById("schdReportName").value;
          var roleId=document.getElementById("selectSchdBusRoles").value;
          var reportId=document.getElementById("reportList").value;
          var mailVal=document.getElementsByName("mail");
           var autoCheck=document.getElementById("autoCheck");
           var emailFlag="";

           if(viewBySelected=='DefaultViewBy' ||viewBySelected=='AllViewBy')
               {
                   $("#checkedViewBy").val("");
               }
          if(scheduleName=="")
              alert('Please enter Scheduler Name');
          else if(roleId=="")
              alert('Please Select Business Role');
          else if(reportId=="")
              alert('Please Select Report');
          else if(strdate=="")
              alert('Please enter Start Date');
          else if(enddate=="")
              alert('Please enter End Date');
          else if(mailid=="")
              alert('Please enter Email Id');
          else if(schdleName=="")
              alert("Please enter Scheduler Name");


//            if(autoCheck.checked==false && (selectedView=="DefaultViewBy" || selectedView=="AllViewBy" ) )
//            {
//                var toAddress=document.getElementById("toAddress").value;
//                if(toAddress=="" )
//                  alert('Please enter To Whom You want to send mail');
//              else
//                emailFlag=validateEmail(toAddress);
//
//            }

//            else if(selectedView=="SelectedViewby" && autoCheck.checked==false)
//                {
//                    for(var j=0;j<mailVal.length;j++)
//                    {
//                         emailFlag=validateEmail(mailVal[j].value);
//                         if(emailFlag==false)
//                             break;
//                    }
//
//                }
//              if(emailFlag==true || autoCheck.checked)
                else
              {
                  if('<%=isEdit%>'=='readOnly')
                  {
                      $("#loading").show();
                        $.ajax({
                            url:'<%=request.getContextPath()%>/scheduler.do?reportBy=runReportScheduler&schedulerId='+'<%=schedulerId%>'+"&fromStudio="+true,
                            success:function(data){
                                $("#loading").hide();
                                alert("Report Scheduled");
                                document.forms.scheduleReportForm.action = "<%=request.getContextPath()%>/home.jsp#Scheduler";
                                document.forms.scheduleReportForm.submit();
                            }
                        });
                  }
                  else
                  {
                      $.ajax({
                        url:'<%=request.getContextPath()%>/calenderSetUpAction.do?timeCalenders=valiDateCalnder&startYear='+strdate+'&endYear='+enddate+'&scheduler='+"scheduler",
                        success:function(data){
                            if(data=='true')
                            {
                                $.post("<%=request.getContextPath()%>/scheduler.do?reportBy=scheduleReport&runFlag="+runFlag, $("#scheduleReportForm").serialize() ,
                                function(data){
                                    $("#reportId").val(data);
                                    if(runFlag=="true")
                                        runSchedule();
                                    else
                                    {
                                        if ("<%=targetReportId%>" == "" && fromReport == false){
                                            document.forms.scheduleReportForm.action = "<%=request.getContextPath()%>/home.jsp#Scheduler";
                                            document.forms.scheduleReportForm.submit();
                                        }
                                        else{
                                            alert("Scheduler saved.");
                                        }
                                    }

                                });
                            }else{
                                alert("Please select End Year correctly.")
                            }
                        }
                    });
                  }


              }

            }

            function goSchedulerHome()
            {
                document.forms.scheduleReportForm.action = "<%=request.getContextPath()%>/home.jsp#Scheduler";
                document.forms.scheduleReportForm.submit();
            }
             function validateEmail(email)
            {
                var ids=email.split(",");
                for(var i=0;i<ids.length;i++)
                {
                    var eachId=ids[i];
                    var c=0;
                     invalidChars = "' /:;";
                        for (var j = 0; j< invalidChars.length; j++)
                        {
                            badChar = invalidChars.charAt(j)
                            if (eachId.indexOf(badChar,0) != -1)
                            {
                                c=1;
                                alert("You can't use following characters " + invalidChars +" in your Email address.");
                             document.forms.scheduleReportForm.eachId.focus();
                             return false;
                            }
                        }
                        atPos = eachId.indexOf("@",1);
                        if (atPos == -1)
                        {
                            c=1;
                            alert("You need to provide your Email UserId. i.e  your email should be in this format info@ezcommerceinc.com");
                         document.forms.scheduleReportForm.eachId.focus();
                         return false;
                        }
                        if (eachId.indexOf("@",atPos+1) != -1)
                        {
                            c=1;
                            alert("The Email address you have provided does not have '@' symbol. Please enter valid Email address.");
                         document.forms.scheduleReportForm.eachId.focus();
                         return false;
                        }
                        periodPos =eachId.lastIndexOf(".")
                        if (periodPos == -1)
                        {
                            c=1;
                            alert("The Email address you have provided does not have .com or .net etc. Please provide a valid Email address.");
                         document.forms.scheduleReportForm.eachId.focus();
                        return false;
                        }
                        if (! ( (periodPos+3 == eachId.length) || (periodPos+4  == eachId.length) ))
                        {
                            c=1;
                            alert("The Email address you have provided does not have .com or .net etc. Please provide a valid Email address.");
                          document.forms.scheduleReportForm.eachId.focus();
                         return false;
                        }

                 }
                 return true;
            }

        function runSchedule()
            {
                var reportId = '<%=schedulerId%>';
                $("#loading").show();
                $.ajax({
                    url:'<%=request.getContextPath()%>/scheduler.do?reportBy=runReportScheduler&schedulerId='+reportId+"&fromStudio="+false,
                    success:function(data){
                        $("#loading").hide();
                        alert("Report Scheduled");
                        if ("<%=targetReportId%>" == "" && fromReport == false){
                            document.forms.scheduleReportForm.action = "<%=request.getContextPath()%>/home.jsp#Scheduler";
                            document.forms.scheduleReportForm.submit();
                        }
                    }
                });

            }

            function getDimMembers(){
                 $.ajax({
                    url:'<%=request.getContextPath()%>/scheduler.do?reportBy=uploadDimensionsForMail&dimensionId='+dimId,
                    success:function(data){
                        var dimArr="";
                        if(data!="null")
                            dimArr=data.split("~");
                            dimSelectedArr=dimArr;

                    }
                });

            }
             function getSuggesions(id)
            {
                var val=$("#"+id+"dimDetail").val();
                var suggArry=new Array();
                var dimHtml="";
                var multipleVal=val.split(",");
                var multiValLen=multipleVal.length;
                var lastVal=multipleVal[multiValLen-1];
                for(var k=0;k<dimSelectedArr.length;k++)
                 {
                     if(dimSelectedArr[k].substr(0,lastVal.length).toUpperCase()==lastVal.toUpperCase())
                         {
                             suggArry.push(dimSelectedArr[k]);
                         }
                 }
                dimHtml+="<table cellspacing='1' cellpadding='1'  id='dimSelectedTable' class='myAjaxTable'> <tr><td width='20%'>";
                dimHtml+="<tr><td onclick=\"getSelectedDim('all',"+id+")\"><div id='all' class='suggestLink'>All</div></td></tr>";
                 for(var k=0;k<suggArry.length;k++)
                 {

                     dimHtml+="<tr><td  onclick=\"getSelectedDim('"+id+"resultlist"+k+"',"+id+")\"><div id=\""+id+"resultlist"+k+"\" class='suggestLink'>";
                     dimHtml+=suggArry[k];
                     dimHtml+="</div></td>";
                     if((k+1) < suggArry.length)
                     {
                         dimHtml+="<td onclick=\"getSelectedDim('"+id+"resultlist"+(k+1)+"',"+id+")\" ><div id=\""+id+"resultlist"+(k+1)+"\" class='suggestLink' >";
                         dimHtml+=suggArry[k+1];
                         dimHtml+="</div></td>";
                     }
                     dimHtml+="</tr>";
                     dimHtml+="<tr></tr>";
                     k++;
                 }
                 dimHtml+="</table>";
                  $("#"+id+"selectDimHtml").html("")
                 $("#"+id+"selectDimHtml").html(dimHtml);
                 if(dimSelectedArr.length>0)
                  $('#'+id+'selectDimHtml').show();

            }



            function buildHtml(count)
            {
                var dimHtml="";
                dimHtml+="<table cellspacing='1' cellpadding='1'  id='dimSelectedTable' class='myAjaxTable'> <tr><td width='20%'>";
                dimHtml+="<tr><td onclick=\"getSelectedDim('all',"+count+")\"><div id='all' class='suggestLink'><%=TranslaterHelper.getTranslatedString("ALL", locale)%></div></td></tr>";
                 for(var k=0;k<dimSelectedArr.length;k++)
                 {

                     dimHtml+="<tr><td  onclick=\"getSelectedDim('"+count+"resultlist"+k+"',"+count+")\"><div id=\""+count+"resultlist"+k+"\" class='suggestLink'>";
                     dimHtml+=dimSelectedArr[k];
                     dimHtml+="</div></td>";
                     if((k+1) < dimSelectedArr.length)
                     {
                         dimHtml+="<td onclick=\"getSelectedDim('"+count+"resultlist"+(k+1)+"',"+count+")\" ><div id=\""+count+"resultlist"+(k+1)+"\" class='suggestLink' >";
                         dimHtml+=dimSelectedArr[k+1];
                         dimHtml+="</div></td>";
                     }
                     dimHtml+="</tr>";
                     dimHtml+="<tr></tr>";
                     k++;
                 }
                 dimHtml+="</table>";
                  $("#"+count+"selectDimHtml").html("")
                 $("#"+count+"selectDimHtml").html(dimHtml);

            }

       function addDimEmailRow()
        {
            var table = document.getElementById("dimEmail");
            var rowCount = table.rows.length;
             idx = rowCount ;
            var row = table.insertRow(rowCount);
            row.id="row"+idx;
            var tdhtml="<td width='13%'>";
            tdhtml+="<%=TranslaterHelper.getTranslatedString("DIMENSION_DETAILS", locale)%> <td width='20%'><input type='text' id=\""+idx+"dimDetail\" class='dimDetail' value='All' name='dimDetail' autocomplete='off' onkeyup=\"getSuggesions('"+idx+"')\">";
            tdhtml+="<img alt='' style='vertical-align:inline;display:inline;width:12px;height:15px;cursor:pointer' onclick=\"showDiv('"+idx+"')\"  src='../../images/include.png'/>";
            tdhtml+="<div id=\""+idx+"selectDimHtml\"  class='ajaxboxstyle' style='display: none;overflow: auto;'></div>";
            tdhtml+="</td><td width='10p%'><input type='text' id=\""+idx+"mail\" name='mail' style='width: 225px'> </td> <td width='1%'>";
            tdhtml+="<img  align='middle' SRC='<%=request.getContextPath()%>/icons pinvoke/plus-circle.png' BORDER='0' ALT=''  onclick='addDimEmailRow()' title='Add Row' /></td>";
            tdhtml+="<td width='2px'><img  align='middle' title='Delete Row' src='<%=request.getContextPath()%>/icons pinvoke/cross-circle.png' BORDER='0' ALT=''  onclick=\" deleteDimEmailRow('"+row.id+"')\"  /></td>";
            row.innerHTML=tdhtml;
        }

        function getSelectedDim(id,count)
        {
            var dimPrevVal=$("#"+count+"dimDetail").val().split(",");
            var prevLength=dimPrevVal.length;
            var completeVal=new Array();
            for(var k=0;k<prevLength-1;k++)
                completeVal.push(dimPrevVal[k]);

            var dimValue=$("#"+id).html();
            completeVal.push(dimValue);
             $("#"+count+"dimDetail").val(completeVal);
             closeDiv();
        }
        function closeDiv(){
                $('.ajaxboxstyle').hide();
            }

     function deleteDimEmailRow(rowId)
        {
            var rowId=rowId.substr(3);
            try {
                var table = document.getElementById("dimEmail");
                var rowCount = table.rows.length;
                if(rowCount > 3) {
                    table.deleteRow(rowId);
                    idx--;
                }
                else{
                    alert("You cannot delete all the rows");
                }
            }catch(e) {
//                alert(e);
            }
        }

        function viewReportDetails()
        {
            var repName=$("#reportList option:selected").text();
            var measureHtml="";
            var paramHtml="";
            var colViewByHtml="";

            $("#reportNameId").html(repName);
            var rowHtml="";
            $("#colViewBy").html("");
            if(colViewByName.length!=0)
            {
                document.getElementById('colViewBy').style.display="";
                colViewByHtml+="<td valign='top' style='color: #3A457C;border: 1px solid #FFF;font-size: 10pt;padding: 4px;font-weight: bolder' width='3%' ><b>Column View By:</b></td><td>";
            for(var k=0;k<colViewByName.length;k++)
                colViewByHtml+=colViewByName[k]+"<br>";
                colViewByHtml+="</td>";
                $("#colViewBy").html(colViewByHtml);
            }

            rowHtml+="<td valign='top' style='color: #3A457C;border: 1px solid #FFF;font-size: 10pt;padding: 4px;font-weight: bolder' width='3%' ><b>Default View By:</b></td><td>";
             for(var k=0;k<viewByName.length;k++)
                rowHtml+=viewByName[k]+"<br>";
            rowHtml+="</td>";
            $("#rowViewBy").html(rowHtml);

            measureHtml="<td valign='top' style='color: #3A457C;border: 1px solid #FFF;font-size: 10pt;padding: 4px;font-weight: bolder' width='3%' ><b>Measures:</b></td><td>";
             for(var l=0;l<measureList.length;l++)
                 measureHtml+=measureList[l]+"<br>";
             measureHtml+="</td>";
            $("#msrTable").html(measureHtml);


            paramHtml="<td valign='top' style='color: #3A457C;border: 1px solid #FFF;font-size: 10pt;padding: 4px;font-weight: bolder' width='3%'><b>Parameters:</b></td><td>";
             for(var m=0;m<paramList.length;m++)
                 paramHtml+=paramList[m]+"<br>";
             paramHtml+="</td>";
            $("#paramTable").html(paramHtml);

              $("#reportDetails").dialog('open');
        }
        function selectViewBy(ViewBy)
        {
            selectedView=ViewBy;
            $("#viewBy").val(selectedView);

            if(ViewBy=='SelectedViewby')
            {
                 $("#sliceTable").show();
//                    var reportId=$("#reportList").val();
//                 $.ajax({
//                    url:'<%=request.getContextPath()%>/scheduler.do?reportBy=getDimDetails&reportId='+reportId,
//                    success:function(data){
//                       var dimDetail=data.split(",");
//                       dimId=dimDetail[1];
//                       dimName=dimDetail[0];
//                       $("#dimensionName").html(dimName);
//                       getDimMembers();
//                    }
//                });

            }

            else if(ViewBy=='DefaultViewBy' || ViewBy=="AllViewBy")
            {
                 $("#sliceTable").hide();

            }

        }

function uploadReportDetails()
{
    var reportId=$("#reportList").val();                                  //Commented While the new scheduler is Creating null will goes that why Commentd
     var reportName=$('#reportList :selected').text();
//     $("#schdReportName").val(reportName);
     $.ajax({
            url:'<%=request.getContextPath()%>/scheduler.do?reportBy=getAllReportDetails&reportId='+reportId,
            success:function(data){
                var reportsJson=eval("("+data+")");
                var html="";
                 paramId=reportsJson.ParamId;
                 viewById=reportsJson.ViewById;
                viewByName=reportsJson.ViewByName;
                colViewByName=reportsJson.colViewByName;
                measureList=reportsJson.MeasureName;
                paramList=reportsJson.ParamName;
                addTimeParameters();
                $("#viewByIdInput").val(viewById);
                 for(var m=0;m<paramList.length;m++)
                 {
                     if(viewByName.indexOf( paramList[m])==-1 && colViewByName.indexOf( paramList[m])==-1)
                         html+="<option value=\""+paramId[m]+"\">"+paramList[m]+"</option>";
                 }
                     var slicesHtml="<option id='select'>select</option>";
                 $(".slicesClass").html(slicesHtml+html);
                 $("#dimensionName").html(viewByName[0]);
                 dimId=reportsJson.ViewById;
                 getDimMembers();
                changeParamDetails();

            }
        });
}

function addTimeParameters(){
    var timeViewBys = new Array();
    timeViewBys.push("Day");
    timeViewBys.push("Week");
    timeViewBys.push("Month");
    timeViewBys.push("Quarter");
    timeViewBys.push("Year");

    for (var i=0;i<timeViewBys.length;i++){
        paramId.push(timeViewBys[i]);
        paramList.push(timeViewBys[i]);
    }
}
function uploadDimensions(measureId)
{
     $.ajax({
        url:"<%=request.getContextPath()%>/scheduler.do?reportBy=getTrackerDimensionsDetails&dimSelected="+measureId,
        success: function(data){
            var dimArr=data.split("~");
            dimSelectedViewArr=dimArr;
        }
    });
}

function getViewSuggesions(id)
    {
        var val=$("#"+id+"otherView").val();
        var measureVal=$("#"+id+"addSlices").val();
        uploadDimensions(measureVal);
        var suggArry=new Array();
        var dimHtml="";
        var multipleVal=val.split(",");
        var multiValLen=multipleVal.length;
        var lastVal=multipleVal[multiValLen-1];
       for(var k=0;k<dimSelectedViewArr.length;k++)
         {
             if(dimSelectedViewArr[k].substr(0,lastVal.length)==lastVal)
                     suggArry.push(dimSelectedViewArr[k]);
         }

        dimHtml+="<table cellspacing='1' cellpadding='1'  id='dimViewSelectedTable' class='myAjaxTable'> <tr><td width='20%'>";
        dimHtml+="<tr><td onclick=\"getSelectedViewDim('all',"+id+")\"><div id='all' class='suggestLink'>All</div></td></tr>";
         for(var k=0;k<suggArry.length;k++)
         {

             dimHtml+="<tr><td  onclick=\"getSelectedViewDim('"+id+"resultViewlist"+k+"',"+id+")\"><div id=\""+id+"resultViewlist"+k+"\" class='suggestLink'>";
             dimHtml+=suggArry[k];
             dimHtml+="</div></td>";
             if((k+1) < suggArry.length)
             {
                 dimHtml+="<td onclick=\"getSelectedViewDim('"+id+"resultViewlist"+(k+1)+"',"+id+")\" ><div id=\""+id+"resultViewlist"+(k+1)+"\" class='suggestLink' >";
                 dimHtml+=suggArry[k+1];
                 dimHtml+="</div></td>";
             }
             dimHtml+="</tr>";
             dimHtml+="<tr></tr>";
             k++;
         }
         dimHtml+="</table>";
          $("#"+id+"selectOtherViewHtml").html("")
         $("#"+id+"selectOtherViewHtml").html(dimHtml);
          $('#'+id+'selectOtherViewHtml').show();

    }
    function getSelectedViewDim(id,count)
        {
             var dimValue=$("#"+id).html();
             $("#"+count+"otherView").val(dimValue);
             closeDiv();
        }
        function addViewByRow()
        {
            var firstRowHtml=$("#1addSlices").html();
            var table = document.getElementById("additionalSlices");
            var rowCount = table.rows.length;
             viewIdx = rowCount ;
            var row = table.insertRow(rowCount);
            row.id="row"+viewIdx;
            var tdhtml="<td width='2%'>";
            tdhtml+="<select id=\""+viewIdx+"addSlices\" name='viewById' class='slicesClass' onchange='uploadDimensions(this.value)'>";
            tdhtml+=firstRowHtml;
            tdhtml+="</select> </td><td width='23%'>";
            tdhtml+="<input type='text' id=\""+viewIdx+"otherView\" class='otherView' value='All' onkeyup='getViewSuggesions("+viewIdx+")' autocomplete='off' name='viewByValue'>";
            tdhtml+="<img alt='' style='vertical-align:inline;display:inline;width:12px;height:15px;cursor:pointer' onclick=\"showViewDiv("+viewIdx+")\"  src='../../images/include.png'/>";
            tdhtml+="<div id=\""+viewIdx+"selectOtherViewHtml\"  class='ajaxboxstyle' style='display: none;overflow: auto;'></div>";
            tdhtml+="</td><td width='2px'>";
            tdhtml+="<img  align='middle' SRC='<%=request.getContextPath()%>/icons pinvoke/plus-circle.png' BORDER='0' ALT=''  onclick='addViewByRow()' title='Add Row' /></td>";
            tdhtml+="<td><img  align='middle' title='Delete Row' src='<%=request.getContextPath()%>/icons pinvoke/cross-circle.png' BORDER='0' ALT=''  onclick=\" deleteViewByRow('"+row.id+"')\"  /></td>";
            row.innerHTML=tdhtml;
        }
        function deleteViewByRow(rowId)
        {
            var rowId=rowId.substr(3);
            try {
                var table = document.getElementById("additionalSlices");
                var rowCount = table.rows.length;
                if(rowCount > 2) {
                    table.deleteRow(rowId);
                    viewIdx--;
                }
                else{
                    alert("You cannot delete all the rows");
                }
            }catch(e) {
//                alert(e);
            }
        }

        function showDiv(rowId)
            {
                 if('<%=isEdit%>'=='readOnly')
                    return;
               else
               {
                   if(dimSelectedArr.length>0)
                    {
                        buildHtml(rowId);
                        $('#'+rowId+'selectDimHtml').show();
                    }
               }

            }

            function showViewDiv(rowId)
            {
                buildViewHtml(rowId);
                 $('#'+rowId+'selectOtherViewHtml').show();
            }

           function buildViewHtml(count)
            {
                var dimHtml="";
                dimHtml+="<table cellspacing='1' cellpadding='1'  id='dimSelectedTable' class='myAjaxTable'> <tr><td width='20%'>";
                dimHtml+="<tr><td onclick=\"getSelectedViewDim('all',"+count+")\"><div id='all' class='suggestLink'><%=TranslaterHelper.getTranslatedString("ALL", locale)%></div></td></tr>";
                 for(var k=0;k<dimSelectedViewArr.length;k++)
                 {

                     dimHtml+="<tr><td  onclick=\"getSelectedViewDim('"+count+"resultViewlist"+k+"',"+count+")\"><div id=\""+count+"resultViewlist"+k+"\" class='suggestLink'>";
                     dimHtml+=dimSelectedViewArr[k];
                     dimHtml+="</div></td>";
                     if((k+1) < dimSelectedViewArr.length)
                     {
                         dimHtml+="<td onclick=\"getSelectedViewDim('"+count+"resultViewlist"+(k+1)+"',"+count+")\" ><div id=\""+count+"resultViewlist"+(k+1)+"\" class='suggestLink' >";
                         dimHtml+=dimSelectedViewArr[k+1];
                         dimHtml+="</div></td>";
                     }
                     dimHtml+="</tr>";
                     dimHtml+="<tr></tr>";
                     k++;
                 }
                 dimHtml+="</table>";
                  $("#"+count+"selectOtherViewHtml").html("")
                 $("#"+count+"selectOtherViewHtml").html(dimHtml);

            }

            function getSelectedViewDim(id,count)
        {
            var dimPrevVal=$("#"+count+"otherView").val().split(",");
            var prevLength=dimPrevVal.length;
            var completeVal=new Array();
            for(var k=0;k<prevLength-1;k++)
                completeVal.push(dimPrevVal[k]);

            var dimValue=$("#"+id).html();
            completeVal.push(dimValue);
             $("#"+count+"otherView").val(completeVal);
             closeDiv();
        }

        function isSliceReq()
        {
            var isSlice=document.getElementById("sliceCheck");
            if(isSlice.checked)
               document.getElementById('additionalSlices').style.display="";
            else
                document.getElementById('additionalSlices').style.display="none";
        }

        function checkForUsers()
        {
            var autoCheck=document.getElementById("autoCheck");
            if(autoCheck.checked)
            {
                $.ajax({
                    url:"<%=request.getContextPath()%>/scheduler.do?reportBy=checkUserAvailibility&reportViewBy="+viewById,
                    success: function(data){
                        if(data=='NotExists')
                        {
                            alert("This View by is not applicable");
//                           document.getElementById('addressTab').style.display="";
//                          document.getElementById('allViewByTd').style.display="";
//                          document.getElementById('advOptionsTd').style.display="";
                          $('#autoCheck').removeAttr("checked");
                           $("#autoChckInput").val(false);
                        }
                        else
                        {
//                            document.getElementById('addressTab').style.display="none";
//                            document.getElementById('allViewByTd').style.display="none";
//                            document.getElementById('advOptionsTd').style.display="none";
                             $("#autoChckInput").val(true);
                        }
                    }
                });
            }
           else
           {
//                 document.getElementById('addressTab').style.display="";
//                 document.getElementById('allViewByTd').style.display="";
//                 document.getElementById('advOptionsTd').style.display="";
                 $("#autoChckInput").val(false);

           }
        }

        function uploadReports(tgtRepId)
        {
            var roleId=$("#selectSchdBusRoles").val();
            $.ajax({
                url: '<%=request.getContextPath()%>/reportTemplateAction.do?templateParam=getReportBasedOnRole&roleId='+roleId,
                success: function(data)
                {
                    var json=eval('('+data+')');
                     var optionsHtml="";
                      var select="";
                    for(var i=0;i<json.length;i++)
                    {
                             if(json[i].reportType=='R')
                             {
                                 optionsHtml=optionsHtml+"<option value=\""+json[i].reportId+"\">"+json[i].reportName+"</option>";
                             }
                    }
                    select=select+"<option value=\"\">Select Report</option>"

                    if(optionsHtml!=""){             //This Comment For while Selecting BusinessRole bydefault Report Selecting
                        $("#reportList").html("");
                        var html=$("#reportList").html();
                        $("#reportList").html(select+html+optionsHtml);
                     }

                     if (tgtRepId){
                        $("#reportList").val(tgtRepId);
                        var selRepId = $('#reportList option:selected').text();
                        $("#schdrepListTxt").html(selRepId);
                     }
                    // uploadReportDetails();
                }
         });
        }





        function populateRoles(data)
        {
             var json=eval('('+data+')');
             var roleId=json.roleId;
             var roleHtml="";
              var busRole=busRole+"<option value=\"\">Select BusinesRole</option>";
             for(var i=0;i<roleId.length;i++)
                 {
                         roleHtml=roleHtml+"<option value=\""+roleId[i]+"\">"+json.roleName[i]+"</option>";
                 }
                  var html=$("#selectSchdBusRoles").html();
                    $("#sortable").html("")
                    $("#selectSchdBusRoles").html(html+busRole+roleHtml);
                     $("#loading").hide();

        }

        function populateReports(data)
        {
             var allReptJson=eval('('+data+')');
             var reportHtml="";
             var select=""
            
            for(var j=0;j<allReptJson.length;j++)
            {
                 if(allReptJson[j].reportType=='R')
                     reportHtml=reportHtml+"<option value=\""+allReptJson[j].reportId+"\">"+allReptJson[j].reportName+"</option>";
            }
             select=select+"<option value=\"\">Select Report</option>"
               $("#reportList").html(select+reportHtml);

        }

        function populateReportDetails(data,selectedViewById)
        {

             var reportsJson=eval("("+data+")");
             var html="";
             paramId=reportsJson.ParamId;
             viewById=reportsJson.ViewById;
            viewByName=reportsJson.ViewByName;
            colViewByName=reportsJson.colViewByName;
            measureList=reportsJson.MeasureName;
            paramList=reportsJson.ParamName;
            addTimeParameters();
            $("#viewByIdInput").val(viewById);
             for(var m=0;m<paramList.length;m++)
             {
                 if(viewByName.indexOf( paramList[m])==-1 && colViewByName.indexOf( paramList[m])==-1)
                     html+="<option value=\""+paramId[m]+"\">"+paramList[m]+"</option>";
             }
             if(selectedViewById!=undefined)
             {
                var paramIndex=paramId.indexOf(selectedViewById);
                $("#dimensionName").html(paramList[paramIndex]);
                dimId=selectedViewById;
             }
             else{
                  $("#dimensionName").html(viewByName[0]);
                  dimId=reportsJson.ViewById;
             }

             var slicesHtml="<option id='select'>select</option>";
             $(".slicesClass").html(slicesHtml+html);
             $("#dimId").html(viewById[0]);
             getDimMembers();
        }

                                function deleteColumn(index){
                                    //var x=index.replace("GrpCol","GrpColRight");
                                    var x = index;
                                    var LiObj=document.getElementById(x);
                                    var parentUL=document.getElementById(LiObj.parentNode.id);
                                    parentUL.removeChild(LiObj);;
                                    var i=0;
                                    for(i=0;i<grpColArray.length;i++){
                                        if(grpColArray[i]==x){
                                            grpColArray.splice(i,1);
                                    }
                                    }
                                }

                                    //if((liobj.length<'<%//=columnSize%>'&&tarLoc=="sortable1")||tarLoc=="sortable2"){
                                    //if((tarLoc=="sortable1")||tarLoc=="sortable2"){

                                function createColumn(elmntId,elementName,tarLoc){
                                    var ulobj=document.getElementById("sortable1");
                                    var liobj=ulobj.getElementsByTagName("li");
                                    if((liobj.length<'<%=columnSize%>'&&tarLoc=="sortable1")||tarLoc=="sortable2"){
                                        var parentUL=document.getElementById(tarLoc);
                                        var x=grpColArray.toString();

                                        if(x.match(elmntId)==null){
                                            var childLI=document.createElement("li");
                                            childLI.id='GrpColRight'+elmntId;

                                            childLI.style.width='auto';
                                            childLI.style.height='auto';
                                            childLI.style.color='white';
                                            childLI.className='navtitle-hover';
                                            var table=document.createElement("table");
                                            table.id="GrpTab"+elmntId;
                                            var row=table.insertRow(0);
                                            var cell1=row.insertCell(0);

                                            var a=document.createElement("a");
                                            var deleteElement = 'GrpColRight'+elmntId;
                                            a.href="javascript:deleteColumn('"+deleteElement+"')";
                                            a.innerHTML="a";
                                            a.className="ui-icon ui-icon-close";
                                            cell1.appendChild(a);
                                            var cell2=row.insertCell(1);

                                            cell2.style.color='black';
                                            cell2.innerHTML=elementName;
                                            childLI.appendChild(table);
                                            parentUL.appendChild(childLI);
                                            grpColArray.push(deleteElement);
                                        }
                                    }
                                    else{
                                       alert("You cannot add more than one column in main measure");
                                    }
                                }
                                
        function checKViewBys(fromReady)
        {
            var timeViewBys = new Array();
            timeViewBys.push("Day");
            timeViewBys.push("Week");
            timeViewBys.push("Month");
            timeViewBys.push("Quarter");
            timeViewBys.push("Year");

            var checkHtml="<tr><td colspan='2' style='height:15px'></td></tr>";
            var checkedIds=$("#checkedViewBy").val();
            var checkedIdsArr=checkedIds.split(",");
            grpColArray=new Array();
            var newHtml = "";
             for(var k=0;k<paramList.length;k++)
                 {
                    newHtml+="<li id=\""+paramId[k]+","+paramList[k]+"\" style=\"width: auto; height: auto; color: white;\" class=\"navtitle-hover\">"+paramList[k]+"</li>";
                 }

            var timeHtml = "";
  /*          for (var z=0;z<timeViewBys.length;z++){
                timeHtml+="<li id=\""+timeViewBys[z]+","+timeViewBys[z]+"\" style=\"width: auto; height: auto; color: white;\" class=\"navtitle-hover\">"+timeViewBys[z]+"</li>";
            }
*/
            var totalHtml = newHtml+timeHtml;

                 $("#measures").html(totalHtml);
                 checkHtml+="<tr><td colspan='2' style='height:15px'></td></tr><tr>";
                 checkHtml+="<td colspan='' align='right'><input type='button' class='navtitle-hover' style='width:auto' value='Done' onclick='getCheckedParameters()'>";
                 //$("#checkViewTable").html(checkHtml);

                 if (checkedIdsArr.length > 0){
                    var primaryViewByHtml = "";
                    var additionalViewByHtml = "";
                    var primaryViewBy = checkedIdsArr[0];
                    var index = paramId.indexOf(primaryViewBy);
                    if (index != -1){
                        primaryViewByHtml = "<li id=\"GrpColRight"+paramId[index]+","+paramList[index]+"\" style=\"width: auto; height: auto; color: white;\" class=\"navtitle-hover\"><table id=\"GrpTab"+paramId[index]+"\"><tbody><tr><td><a href=\"javascript:deleteColumn('GrpColRight"+paramId[index]+","+paramList[index]+"')\" class=\"ui-icon ui-icon-close\">a</a></td><td style=\"color: black;\">"+paramList[index]+"</td></tr></tbody></table></li>";
                        $("#sortable1").html(primaryViewByHtml);
                        grpColArray.push("GrpColRight"+paramId[index]+","+paramList[index]);
                    }

                    for(var l=1;l<checkedIdsArr.length;l++)
                     {
                         if (checkedIdsArr[l] != ""){
                             index = paramId.indexOf(checkedIdsArr[l]);
                             if(index!=-1)
                             {
                                additionalViewByHtml+= "<li id=\"GrpColRight"+paramId[index]+","+paramList[index]+"\" style=\"width: auto; height: auto; color: white;\" class=\"navtitle-hover\"><table id=\"GrpTab"+paramId[index]+"\"><tbody><tr><td><a href=\"javascript:deleteColumn('GrpColRight"+paramId[index]+","+paramList[index]+"')\" class=\"ui-icon ui-icon-close\">a</a></td><td style=\"color: black;\">"+paramList[index]+"</td></tr></tbody></table></li>";
                                grpColArray.push("GrpColRight"+paramId[index]+","+paramList[index]);
                             }
                             else{  //Time Dimension Parameter
                                additionalViewByHtml+= "<li id=\"GrpColRight"+timeViewBys[index]+","+timeViewBys[index]+"\" style=\"width: auto; height: auto; color: white;\" class=\"navtitle-hover\"><table id=\"GrpTab"+timeViewBys[index]+"\"><tbody><tr><td><a href=\"javascript:deleteColumn('GrpColRight"+timeViewBys[index]+","+timeViewBys[index]+"')\" class=\"ui-icon ui-icon-close\">a</a></td><td style=\"color: black;\">"+timeViewBys[index]+"</td></tr></tbody></table></li>";
                                grpColArray.push("GrpColRight"+timeViewBys[index]+","+timeViewBys[index]);
                             }
                         }
                     }
                     $("#sortable2").html(additionalViewByHtml);
                 }

                 if('<%=isEdit%>'=='readOnly')
                  {
                      $("#dimSaveBn").attr("disabled","disabled");
                  }

                  if (fromReady == false)
                       $("#checkViewDialog").dialog('open');


                 if('<%=isEdit%>'!='readOnly')
                  {
                    var dragMeasure=$('#measures > li > ul > li > ul > li > span,#measures > li > ul > li >  span,#measures > li');
                    $(dragMeasure).draggable({
                        helper:"clone",
                        effect:["", "fade"]
                    });
                    $("#sortable1").droppable(
                    {
                        activeClass:"blueBorder",
                        accept:'#measures > li > ul > li > ul > li > span,#measures > li > ul > li >  span,#measures > li',
                        drop: function(ev, ui) {
                            createColumn(ui.draggable.attr('id'),ui.draggable.html(),"sortable1");
                        }
                    });
                    $("#sortable2").droppable({
                        activeClass:"blueBorder",
                        accept:'#measures > li > ul > li > ul > li > span,#measures > li > ul > li >  span,#measures > li',
                        drop: function(ev, ui) {
                            createColumn(ui.draggable.attr('id'),ui.draggable.html(),"sortable2");
                        }
                    });
                  }

        }

        function getCheckedParameters()
        {
            var dimNameString="";
            var dimIdString="";

            var primDimString = "";
            var primDimNameString = "";

            var leftcolsUl=document.getElementById("sortable1");
            var rightcolsUl=document.getElementById("sortable2");

            var leftcolIds=leftcolsUl.getElementsByTagName("li");
            var rightcolIds=rightcolsUl.getElementsByTagName("li");

            if(leftcolIds!=null && leftcolIds.length!=0){
                for(var i=0;i<leftcolIds.length;i++){
                    //leftgrpColNames=leftgrpColNames+","+(leftcolIds[i].id).replace("GrpCol","");
                    var mainMeasure = leftcolIds[i].id.replace("GrpColRight","");
                    var measvalues = mainMeasure.split(",");
                    dimIdString = dimIdString+","+measvalues[0];
                    primDimString = measvalues[0];
                    dimNameString = dimNameString+","+measvalues[1];
                    primDimNameString = measvalues[1];
                }
                for(var i=0;i<rightcolIds.length;i++){
                    //rightgrpColNames=rightgrpColNames+","+(rightcolIds[i].id).replace("GrpCol","");
                    //suppMeasures = suppMeasures + "," + rightcolIds[i].id.replace("GrpCol","");
                    var suppMeasures = rightcolIds[i].id.replace("GrpColRight","");
                    var measvalues = suppMeasures.split(",");
                    dimIdString = dimIdString+","+measvalues[0];
                    dimNameString = dimNameString+","+measvalues[1];
                }

                if(dimIdString!=""){
                    dimIdString = dimIdString.substring(1);
                    primDimString = primDimString.substring(1);
                }
                if(dimNameString!=""){
                    dimNameString = dimNameString.substring(1);
                    primDimNameString = primDimNameString.substring(1);
                }

            }
            else{
                if((leftcolIds==null || leftcolIds.length==0)){
                    alert("Please Select Main Measure")
                }
            }

             $("#checkViewDialog").dialog('close');
   /*         var paramObj=document.getElementsByName('parameterCheck');
            var paramArr=new Array();
            var paramNameArr=new Array();
            for(var j=0;j< paramObj.length;j++)
            {
                if(paramObj[j].checked)
                {
                    var paramIdArr=paramObj[j].id.split("_");
                    var paramNames=$("#paramName_"+paramIdArr[1]).html();
                    paramNameArr.push(paramNames);
                    paramArr.push(paramIdArr[1]);
                }
            }*/
           $("#viewBySelected").val(dimNameString);
           $("#checkedViewBy").val(dimIdString);
           changeDefaultViewBy();
        }

        function populateParamDetails(data)
        {
            var paramsJson=eval("("+data+")");
             var html="";
             var paramIds=paramsJson.ParamId;
             var paramNames=paramsJson.ParamName;
             for(var m=0;m<paramIds.length;m++)
             {
                 if(viewById.indexOf( paramIds[m])!=-1)
                    html+="<option  value=\""+paramIds[m]+"\" selected> "+paramNames[m]+"</option>";
                else
                     html+="<option value=\""+paramIds[m]+"\">"+paramNames[m]+"</optiopn>";
             }
             $("#defaultViewByList").html(html);
        }


        function changeDefaultViewBy()
        {
                 
            var dimNames = $("#viewBySelected").val();
            var dimIds = $("#checkedViewBy").val();

            var dimIdsArr = dimIds.split(",");
            var dimNamesArr = dimNames.split(",");

            //var defaultViewById=$("#defaultViewByList").val();
            dimId=dimIdsArr[0];
            $("#viewByIdInput").val(dimId);
            //dimName=$("#defaultViewByList option:selected").text();
            dimName=dimNamesArr[0];
            $("#dimensionName").html(dimName);
            getDimMembers();

            var dDetail=document.getElementsByName("dimDetail");
            for(var i=2; i<dDetail.length+2; i++)
                {
                    $("#"+i+"dimDetail").val("All");
                }

        }

        function  changeParamDetails()
        {
            var paramHtml="";
            for(var m=0;m<paramId.length;m++)
             {
                 if(viewById.indexOf( paramId[m])!=-1)
                    paramHtml+="<option  value=\""+paramId[m]+"\" selected> "+paramList[m]+"</option>";
                else
                     paramHtml+="<option value=\""+paramId[m]+"\">"+paramList[m]+"</optiopn>";
             }
             $("#defaultViewByList").html(paramHtml);
        }

        function gotoReport(){
            var callBackURL = "<%=url%>";
            callBackURL = callBackURL.replace(/;/gi, "&");
            document.forms.scheduleReportForm.action = callBackURL;
            document.forms.scheduleReportForm.submit();
        }

     function additionalAggregation(){
       var optionElemts = document.getElementById('dailyData').options;
       var selecedEle=document.getElementById('dailyData').value;
       var html=""
       //var html1=""
       for(var i=0;i<optionElemts.length;i++){
       if(optionElemts[i].value!=selecedEle)
       html=html+"<td align='right'><input type='checkbox' name='selectedvalues'  id='additionAgri"+i+"' value='"+optionElemts[i].value+"'/>"+optionElemts[i].value+"<br></td>";
       }
       //html1="<td width='100%' class='myhead'></td>"
       $("#tableId").html(html);
       if(document.getElementById("buttonsId").style.display=='none'){
                        $("#buttonsId").show();
                    }else{
                        $("#buttonsId").hide();
           }

    }

    </script>
    </head>
     <table style="width:100%">
                <tr>
                    <td valign="top" style="width:50%;">
                        <jsp:include page="../../Headerfolder/headerPage.jsp"/>
                    </td>
                </tr>
            </table>


            <form action=""  name="scheduleReportForm" id="scheduleReportForm"method="post">
                  <div class="navtitle1" style=" max-width: 100%; cursor: auto; height: 20px;"align="left">
                    <span> <font size="2" style="font-weight: bolder"><%=TranslaterHelper.getTranslatedString("SCHEDULER", locale)%> </font><b> </b></span>
                    </div>
                <br>
                <table align="right"><tr><td>
            <input type="button" id="schedHomeBn" value="<%=TranslaterHelper.getTranslatedString("SCHEDULER_HOME", locale)%>" onclick="javascript:goSchedulerHome()" class="navtitle-hover" style="width:auto"  style="font-size:10px;color:#2191C0;;font-weight:bold;text-decoration: none;font-family:Georgia">
            <input type="button" id="gotoReportBn" value="Back to Report" onclick="javascript:gotoReport()" class="navtitle-hover" style="width:auto"  style="font-size:10px;color:#2191C0;;font-weight:bold;text-decoration: none;font-family:Georgia">
                    </td></tr></table>
             <table style='width:100%' align='center'>
                    <tr>
                        <td colspan='2' style='height:15px'></td>
                    </tr>
                </table>
             <center>
                   <div align="center" style="border-width: 4px; border-style: groove; border-color: skyblue; margin-top: 5px; margin-left: 10px; margin-right: 10px; margin-bottom:  5px;  padding-top: 2px;padding-bottom: 30px;"> &nbsp;
                                &nbsp;
                    <div style="width: 75%" align="center">

<!--                <table style='width:100%' align='center'>
                    <tr>
                        <td colspan='2' style='height:15px'></td>
                    </tr>
                </table>-->
                <div id="paramsDiv"></div>
                 <table style='width:100%' align='center'>
                    <tr>
                        <td colspan='2' style='height:15px'></td>
                    </tr>
                </table>
                <table>
                    <Tr>
                    <table width="66%" align="center">
                        <tr>
                            <Td class="myhead"><span style="color:red">*</span>Scheduler Name </Td>
                            <Td><Input type="text" name="schdReportName" id="schdReportName" maxlength=100  style="width:auto" value="" >
                            </Td>
                        </tr>
                    </table>
                     </Tr>

                     <Tr>
                    <table width="66%" align="center">
                        <tr>
                            <Td class="myhead"><span style="color:red">*</span><%=TranslaterHelper.getTranslatedString("BUSINESS_ROLES", locale)%> </Td>
                            <Td><select id="selectSchdBusRoles" name="businessRole" onchange="uploadReports()" ></select>
                            </Td>
                            <td id="schdBusRolesTxt"></td>
                        </tr>
                    </table>
                     </Tr>
                    <Tr>
                    <table width="66%" align="center">
                        <tr>
                            <Td class="myhead"><span style="color:red">*</span><%=TranslaterHelper.getTranslatedString("SELECT_REPORT", locale)%> </Td>
                            <Td><select id="reportList" name="allDim" onchange="uploadReportDetails()" style="width:230px"></select>
                            </Td>
                            <td id="schdrepListTxt"></td>
                            <td><a id="reptDetails" onclick="javascript:viewReportDetails()" style="cursor: pointer;"><u>View Report Details</u></a></td>
                        </tr>
                    </table>
                     </Tr>
<%--                     <Tr>
                    <table width="66%" align="center">
                        <tr>
                            <Td class="myhead"><span style="color:red">*</span>Select Default View By </Td>
                            <Td><select id="defaultViewByList" name="deafalutViewByList" onchange="changeDefaultViewBy()" ></select>
                            </Td>
                        </tr>
                    </table>
                     </Tr>
--%>

                    <Tr>
                    <table width="66%" align="center">
                        <tr>
                        <Td class="myhead"><span style="color:red">*</span><%=TranslaterHelper.getTranslatedString("START_DATE", locale)%></Td>
                        <Td width="30%"><Input type="text" readonly  name="startdate" id="stDatepicker" maxlength=100  style="width:120px" value="" >
                        </Td>
                        <td width="20%"></td>
                    </tr>
                    </table>
                    </Tr>
                    <Tr>
                    <table width="66%" align="center">
                        <tr>
                        <Td class="myhead"><span style="color:red">*</span><%=TranslaterHelper.getTranslatedString("END_DATE", locale)%> </Td>
                        <Td><Input type="text" readonly  name="enddate" id="edDatepicker" maxlength=100  style="width:120px" value="" >
                        </Td>
                        <td width="20%"></td>
                    </tr>
                    </table>
                        </Tr>

                    <tr>
                    <table width="66%" align="center">
                        <tr>
                            <Td class="myhead" width="40%"><%=TranslaterHelper.getTranslatedString("FREQUENCY", locale)%></Td>
                            <Td width="10%">
                                <Select name="frequency" id="frequency" class="myTextbox5" onchange="addDate(this)"  style="width:100px">
                                    <Option value="1"><%=TranslaterHelper.getTranslatedString("DAILY", locale)%></Option>
                                    <Option value="3">Weekly</Option>
                                    <Option value="2"><%=TranslaterHelper.getTranslatedString("MONTHLY", locale)%></Option>

                                </Select>
                            </Td>
                            <td>
                                <div id="onlyDateSelect" style="display:none">
                                Day
                                <select name="monthDate" id="monthDate" onchange="addDate(this)">
                                        <option value='L'>EOM</option>
                                        <option value='B'>BOM</option>
                                    <%for (int i=1;i<=31;i++) {%>
                                        <option value='<%=i%>'><%=i%></option>
                                     <%}%>
                                        
                                    </select>
                                </div>
                            </td>
                            <td>
                                <div id="dayOfWeek" style="display:none;width: auto">
                                    <select name="alertDay" id="alertDay">
                                    <%for (int i=1;i<=days.length;i++) {%>
                                        <option value='<%=day[i-1]%>'><%=days[i-1]%></option>
                                     <%}%>
                                    </select>
                                </div>
                            </td>
                             <Td>
                                <div id="timeselect" style="display: none">
                                    <%=TranslaterHelper.getTranslatedString("HOURS", locale)%>
                                    <select name="hrs" id="hrs" >
                                        <%for (int i = 00; i < 24; i++) {%>
                                        <option  value="<%=i%>"><%=i%></option>
                                        <%}%>
                                    </select>
                                   <%=TranslaterHelper.getTranslatedString("MINUTES", locale)%>
                                   <select name="mins" id="mins">
                                        <%for (int i = 00; i < 60; i++) {%>
                                        <option  value="<%=i%>"><%=i%></option>
                                        <%}%>
                                    </select>
                                </div>
                            </Td>
<!--                            </td>-->
                        </tr>
                    </table>
                </tr>
                <tr>
                <table id="dataSelection" width="66%" align="center"><tr>
                    <td class="myhead">Data</td>
                    <td id="dailyDataTD">
                        <select id="dailyData" name="dailyData" >
                            <option value="Current Day">Current Day</option>
                            <option value="Last Day">Last Day</option>
                            <option value="WTD">WTD</option>
                            <option value="MTD">MTD</option>
                            <option value="QTD">QTD</option>
                            <option value="YTD">YTD</option>
                        </select>
                    </td>
                    <td id="monthlyDataTD">
                        <select id="monthlyData" name="monthlyData">
                            <option value="last">Last Month</option>
                            <option value="mtd">MTD</option>
                            <option value="both">Last Month + MTD</option>
                        </select>
                    </td>
                    <td><a id="aggregationId" onclick="javascript:additionalAggregation()" style="cursor: pointer;"><u>Additional Aggregation</u></a></td>
                   
                    </tr>
<!--                    <tr >
                          <td id="buttonsId" style="display:none" align="right">
                          <table id="tableId">

                          </table>
                    </td>
                </tr>-->
                    </table>
                </tr>
                
                <tr>
                <table width="66%" align="center">
                    <tr>
                    <td class="myhead"><%=TranslaterHelper.getTranslatedString("CONTENT_TYPE", locale)%></td>
                    <td>
                        <Select name="contentType" id="contentType" class="myTextbox3">
                            <Option value="html"><%=TranslaterHelper.getTranslatedString("HTML", locale)%></Option>
                            <Option value="pdf"><%=TranslaterHelper.getTranslatedString("PDF", locale)%></Option>
                            <Option value="excel"><%=TranslaterHelper.getTranslatedString("EXCEL", locale)%></Option>
<!--                            <Option value="xml"><%=TranslaterHelper.getTranslatedString("XML", locale)%></Option>-->
                        </Select>
                    </td>
                    <td id="buttonsId" style="display:none" align="right">
                          <table id="tableId">

                          </table>
                    </td>
                </tr>
                </table>
                </tr>
                <tr>
                <table  width="66%" align="center" id="autoCheckTable" >
                    <tr>
                        <Td style="font-weight: bolder" class="myhead"><%=TranslaterHelper.getTranslatedString("SUBSCRIBERS", locale)%>  </Td>
                        <td><input type="checkbox" name="autoCheck" onclick="checkForUsers()" id="autoCheck"><br></td>
                        <td><font> Auto Identifier</font></td>
                    </tr>
                </table>
                </tr>
                <tr>
                <table  width="66%" align="center" id="SubscriberTable" >
                    <tr>
                        <Td style="font-weight: bolder" class="myhead">Scheduler Views </Td>
                        <td id="defaultViewByTd"><input type="radio" name="viewBys" onclick="selectViewBy(this.value)" value="DefaultViewBy" id="DefaultViewBy">Default View<br></td>
                        <td id="allViewByTd">  <input type="radio" name="viewBys" onclick="selectViewBy(this.value)" value="AllViewBy" id="AllViewBy">All Views<br></td>
                        <td id="advOptionsTd">    <input type="radio" name="viewBys" onclick="selectViewBy(this.value)" value="SelectedViewby" id="SelectedViewby">Selective Views<br></td>

                    </tr>
                </table>
                </tr>
<!--                <tr>
                    <table id="addressTab" style="display: none" width="66%">
                      <tr>
                          <td style="font-weight: bolder" class="myhead">Email-Id</td>
                          <td><input type="text" class="myTextbox3" name="toAddress" style="width:200px;" id="toAddress" ><br/></td>
                      </tr>
                      </table>

                </tr>-->
                 <tr>
                        <table  width="66%" align="center" id="sliceTable" style="display: none" >
                    <tr>
                        <Td style="font-weight: bolder" class="myhead">Select  Viewbys  </Td>
                        <td><Input type="text" readonly  class="myTextbox5" name="viewBySelected" id="viewBySelected" maxlength=100  style="width:auto"></td>
                        <td><a href="javascript:void(0)" onclick="javascript:checKViewBys(false)" style="font-size:12px;font-weight:bold;text-decoration: none;font-family:Georgia"> Click here to select view By</a></td>
                    </tr>
                </table>
            </tr>
<!--                 <tr>
                        <table  width="66%" align="center" id="sliceTable" style="display: none" >
                    <tr>
                        <Td style="font-weight: bolder" class="myhead">Enable Report Slices  </Td>
                        <td><input type="checkbox" name="sliceCheck" onclick="isSliceReq()" id="sliceCheck"><br></td>
                    </tr>
                </table>
            </tr>-->
                            <div  id="advancedMail" title="Dimension Based Mail">
                <table style="width:70%;" id="dimEmail" >
                      <tr> <td colspan='2' style='height:20px'></td></tr>
                    <tr>
                        <th class="subTotalCell" style="text-align: center" id="dimensionName"  width="23%" colspan="2"><font style="font-size:small;"> </font></th>
                        <th class="subTotalCell" style="text-align: center"><font style="font-size:small;" >Email Id</font></th>
                    </tr>
                    <tbody id="dimMail">
                        <tr id="row2">
                            <td width="13%">
                                <%=TranslaterHelper.getTranslatedString("DIMENSION_DETAILS", locale)%></td>
                            <td width="25%">    <input type="text" id="2dimDetail" class="dimDetail" value="All" onkeyup='getSuggesions("2")' name="dimDetail" autocomplete="off">
                               <img alt="" style='vertical-align:inline;display:inline;width:12px;height:15px;cursor:pointer' onclick='showDiv("2")'  src='../../images/include.png'/>
                                <div id="2selectDimHtml" class="ajaxboxstyle" style="display: none;overflow: auto;"></div>

                            </td>
                            <td width="10%">
                                <input type="text" id="2mail" name="mail" style="width: 225px">
                            </td>
                            <Td width="2px">
                                <IMG ALIGN="middle" onclick='addDimEmailRow()' SRC="<%=request.getContextPath()%>/icons pinvoke/plus-circle.png" BORDER="0" ALT=""   title="Add Row" />
                            </Td>
                            <Td>
                                <IMG ALIGN="middle" onclick='deleteDimEmailRow("row2")' SRC="<%=request.getContextPath()%>/icons pinvoke/cross-circle.png" BORDER="0" ALT=""   title="Delete Row" />
                            </Td>
                        </tr>
                    </tbody>
                </table>
          </div>
             <div>
                    <table style="width:70%;display: none" id="additionalSlices">
                         <tr>
                                <td colspan='2' style='height:15px'></td>
                            </tr>
                    <tr>
                        <th class="subTotalCell" style="text-align: center;" width="25%" id="addDimName"><font style="font-size:small;">Additional ViewBys </font></th>
                        <th class="subTotalCell" style="text-align: center"><font style="font-size:small;" >View By Details</font></th>
                    </tr>
                    <tbody id="dimMail">
                        <tr id="viewBy1">
                             <td width="2%">
                                 <select id="1addSlices" name="viewById" class="slicesClass" onchange="uploadDimensions(this.value)">
                                 </select>
                            </td>
                            <td width="25%">
                                <input type="text" id="1otherView" class="otherView" value="All" onkeyup='getViewSuggesions("1")' name="viewByValue" autocomplete="off">
                                <img alt="" style='vertical-align:inline;display:inline;width:12px;height:15px;cursor:pointer' onclick='showDiv("1")'  src='../../images/include.png'/>
                                <div id="1selectOtherViewHtml" class="ajaxboxstyle" style="display: none;overflow: auto;"></div>

                            </td>

                            <Td width="2px">
                                <IMG ALIGN="middle" onclick='addViewByRow()' SRC="<%=request.getContextPath()%>/icons pinvoke/plus-circle.png" BORDER="0" ALT=""   title="Add Row" />
                            </Td>
                            <Td>
                                <IMG ALIGN="middle" onclick='deleteViewByRow("viewBy1")' SRC="<%=request.getContextPath()%>/icons pinvoke/cross-circle.png" BORDER="0" ALT=""   title="Delete Row" />
                            </Td>
                        </tr>
                    </tbody>
                </table>
                </div>

 </table>
                    </div>

                    </div>
                             </center>




              <div id='loading' class='loading_image' style="display:none;">
                  <img alt=""  id='imgId' src='<%=request.getContextPath()%>/images/help-loading.gif'  border='0px' style='position:absolute; left: 150px; top: 10px;'/>
            </div>

            <div id="reportDetails" title="View Report Details">
                <table border="3" bordercolor="#BDBDBD" bgcolor="#FFFFFF" width="100%" cellspacing="5" cellpadding="3" height="100%">
                    <tr><td align="center" colspan="2" id="reportNameId" class="myhead"> Report Details</td></tr>
                    <tr id="paramTable"></tr>
                    <tr id="rowViewBy"></tr>
                    <tr id="colViewBy" style="display: none"></tr>
                    <tr id="msrTable"></tr>

                </table>
           </div>
            <div id="checkViewDialog" title="Select View Bys">
                <table style="width:100%;height:270px" border="solid black 1px">
                    <tr>
                        <td width="50%" valign="top" class="draggedTable1">
                            <div style="height:20px" class="ui-state-default draggedDivs ui-corner-all"><font size="2" style="font-weight:bold">Select Dimensions from below</font></div>
                            <div style="height:250px;overflow:scroll">
                                <ul id="myList3" class="filetree treeview-famfamfam">
                                    <ul id="measures">
                                    </ul>
                                </ul>
                            </div>
                        </td>

                        <td width="50%" valign="top" >
                            <div style="height:20px" class="ui-state-default draggedDivs ui-corner-all"><font size="2" style="font-weight:bold">Drag Dimensions here</font></div>
                            <div style="height:250px;overflow:auto">
                                <table width="100%">
                                    <tr style="height:50%">
                                        <td  id="selectedMeasures" >
                                            <div style="height:20px" class="ui-state-default draggedDivs ui-corner-all"><font size="2" style="font-weight:bold">Primary Viewby</font></div>
                                            <ul id="sortable1" class="sortable" style="height:50px;color:white" >
                                            </ul>
                                        </td>
                                    </tr>
                                    <tr style="height:50%">
                                        <td  id="selectedMeasures1" >
                                            <div style="height:20px" class="ui-state-default draggedDivs ui-corner-all"><font size="2" style="font-weight:bold">Additional Viewbys</font></div>
                                            <ul id="sortable2" class="sortable" style="height:100px;color:white" >
                                            </ul>
                                        </td>
                                    </tr>
                                </table>
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <td align="center" colspan="2">
                            <input type='button' class='navtitle-hover' value='Save' onclick='getCheckedParameters()' id="dimSaveBn">
                        </td>
                    </tr>
                </table>

            </div>
            <table style='width:100%' align='center'>
                <tr>
                    <td colspan='2' style='height:20px'></td>
                </tr>
            </table>


<!--                        <br><br><br>-->
                        

                        <table style='width:100%' align='left'>
                            <tr>
                                <td colspan='2' style='height:15px'></td>
                            </tr>
                            <tr>

                                <td id="saveButton" colspan='' align='right' width="40%"><input type='button' class='navtitle-hover' style='width:10%' value='<%=TranslaterHelper.getTranslatedString("SAVE", locale)%>' onclick='saveReportSchedule("false")'>
                                <td id="runButton" width="55%"><input type='button' class='navtitle-hover'  style='width:12%' value='Run/Execute' onclick='saveReportSchedule("true")'>

<!--                                <td colspan='' align='center'><input type='button' class='navtitle-hover' style='width:auto' value='<%=TranslaterHelper.getTranslatedString("SAVE", locale)%>' onclick='saveReportSchedule("false")'>-->

                                </td>
                            </tr>
                        </table>
                <br>
<!--                        <input type="hidden" id="h" value="<%=request.getContextPath()%>">-->
                        <input type="hidden" id="reportId" name="reportId" value="<%=schedulerId%>">
                        <input type="hidden" id="isEdit" name="isEdit" value="<%=isEdit%>">
                        <input type="hidden" id="viewBy" name="viewBy" value="">
                        <input type="hidden" id="autoChckInput" name="autoChckInput" value="">
                        <input type="hidden" id="dimId" name="dimId" value="">
                        <input type="hidden" id="paramXml" name="paramXml" value="">
                        <input type="hidden" id="viewByIdInput" name="viewByIdInput" value="">
                        <input type="hidden" id="checkedViewBy" name="checkedViewBy" value="">
                        <input type="hidden" id="primaryDim" name="primaryDim" value="">
                        <input type="hidden" id="additionalDim" name="additionalDim" value="">
                        <input type="hidden" id="fromReport" name="fromReport" value="false">


</form>


    </body>
</html>
   <%} catch (Exception e) {
            e.printStackTrace();
        }
        %>

