<%--
    Document   : headLines
    Created on : 19 Jul, 2011, 3:58:03 PM
    Author     : progen
--%>


<%@page contentType="text/html" pageEncoding="UTF-8" import="java.sql.Clob,java.io.Reader,java.util.Locale,com.progen.i18n.TranslaterHelper"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<%
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader ("Expires", 0);

                 Locale locale = null;
                  locale = (Locale) session.getAttribute("userLocale");
String themeColor = "blue";
            if (session.getAttribute("theme") == null) {
        session.setAttribute("theme", themeColor);
    }
    else {
        themeColor = String.valueOf(session.getAttribute("theme"));
    }
String contextpath=request.getContextPath();

%>
<html>
     <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script src="<%=contextpath%>/javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=contextpath%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>
        <link type="text/css" href="<%=contextpath%>/stylesheets/themes/<%=themeColor%>/jquery-ui-1.7.3.custom.css" rel="stylesheet" />
        <link type="text/css" href="<%=contextpath%>/stylesheets/themes/<%=themeColor%>/metadataButton.css" rel="stylesheet" />

        <script type="text/javascript" src="javascript/ui.dialog.js"></script>
       <link rel="stylesheet" href="<%=contextpath%>/tablesorter/docs/css/jq.css" type="text/css" media="print, projection, screen" />
        <link rel="stylesheet" href="<%=contextpath%>/tablesorter/themes/<%=themeColor%>/style.css" type="text/css" media="print, projection, screen" />
       <script type="text/javascript" src="<%=contextpath%>/tablesorter/jquery.tablesorter.js"></script>
        <script type="text/javascript" src="<%=contextpath%>/tablesorter/addons/pager/jquery.tablesorter.pager.js"></script>
        <script type="text/javascript"  language="JavaScript" src="<%=contextpath%>/tablesorter/jquery.columnfilters.js"></script>
        <script type="text/javascript" src="<%=contextpath%>/tablesorter/docs/js/chili/chili-1.8b.js"></script>
<!--        <script type="text/javascript" src="<%=request.getContextPath()%>/tablesorter/docs/js/docs.js"></script>-->
      <script type="text/javascript" src="<%=contextpath%>/TableDisplay/overlib.js"></script>
      <link type="text/css" href="<%=contextpath%>/stylesheets/themes/<%=themeColor %>/ReportCss.css" rel="stylesheet"/>
        <link rel="stylesheet" href="<%=contextpath%>/stylesheets/themes/<%=themeColor%>/style.css" type="text/css" media="print, projection, screen">
        <link type="text/css" href="<%=contextpath%>/stylesheets/themes/<%=themeColor%>/metadataButton.css" rel="stylesheet" />
         <script src="<%=contextpath%>/javascript/lib/jquery/js/jquery.tablesorter.mod.js" type="text/javascript"></script>
        <script src="<%=contextpath%>/javascript/lib/jquery/js/jquery.tablesorter.collapsible.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=contextpath%>/javascript/lib/jquery/js/jquery.tablesorter.innergrid.js"></script>
    <head>
        <style type="text/css">

            .headlinestyle {
                font-family: verdana;
                font-size: 12px;
                font-size-adjust: none;
                font-stretch: normal;
                font-style: normal;
                font-variant: normal;
                font-weight: bold;
                line-height: normal;


            }
        </style>

<style type="text/css" >
            .ui-progressbar-value { background-image: url(images/barchart.gif); }
            .myHead
            {
                font-family: Verdana;
                font-size: 8pt;
                font-weight: bold;
                color: #000;
                padding-left:12px;
                width:20%;
                background-color:#b4d9ee;
                border:0px;
                /*apply this class to a Headings of servicestable only*/
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
            .ajaxboxstyle {
                background-color:#FFFFFF;
                border: 0.1em solid #0000FF;
                height:50px;
                margin:0 0.5em;
                overflow-x:hidden;
                overflow-y:auto;
                position:absolute;
                text-align:left;
                border-top: 1px groove #848484;
                border-right: 1px inset #999999;
                border-bottom: 1px inset #999999;
                border-left: 1px groove #848484;
                background-color:#f0f0f0;
                width:450px;
            }
            .black_overlay{
                display: none;
                position: absolute;
                top: 0%;
                left: 0%;
                width: 110%;
                height: 200%;
                background-color: black;
                z-index:1001;
                -moz-opacity: 0.5;
                opacity:.50;
                overflow:auto;
            }

            .white_content {
                display: none;
                position: absolute;
                top: 30%;
                left: 35%;
                width: 50%;
                height:50%;
                padding: 16px;
                border: 10px solid silver;
                background-color: white;
                z-index:1002;
                -moz-border-radius-bottomleft:6px;
                -moz-border-radius-bottomright:6px;
                -moz-border-radius-topleft:6px;
                -moz-border-radius-topright:6px;
            }

            table.grid .collapsible {
                padding: 0 0 3px 0;
            }

            .collapsible a.collapsed {
                display: block;
                width: 15px;
                height: 15px;
                background: url(images/addImg.gif) no-repeat 3px 3px;
                outline: 0;
            }

            .collapsible a.expanded {
                display: block;
                width: 15px;
                height: 15px;
                background: url(images/deleteImg.gif) no-repeat 3px 3px;
                outline: 0;
            }
        </style>
        <script type="text/javascript">
           $(document).ready(function(){
             $("#selectHeadlineDiv").dialog({
            //bgiframe: true,
            autoOpen: false,
            height: 400,
            width: 500,
            position: 'justify',
            modal: true,
            title:'Select Headlines'
        });
          $("#emailDiv").dialog({
            //bgiframe: true,
            autoOpen: false,
            height: 400,
            width: 500,
            position: 'justify',
            modal: true,
            title:'Send Emails'
        });

        $("#headlinesDialog").dialog({
                autoOpen: false,
                height: 450,
                width: 800,
                position: 'justify',
                modal: true,
                resizable:false
            });
             $("#headlinesMailList").dialog({
                autoOpen: false,
                height: 450,
                width: 800,
                position: 'justify',
                modal: true,
                resizable:false
            });
            $("#shareHeadline").dialog({
                autoOpen: false,
                height: 300,
                width: 400,
                position: 'justify',
                modal: true,
                resizable:false
            });

                $.post('<%=request.getContextPath()%>/dataSnapshot.do?doAction=getReportHeadlines&edit=true',$("#headLinesForm").serialize(),
            function(data){
                $("#headlinetableDiv").html(data);
                 initCollapser("");
            });

            });
            </script>
       
    </head>
    <body>
        <form name="headLinesForm" id="headLinesForm" action="">
<!--            <table style="width:100px" align="right"><tr>
                    <td><input type='button' value='Share' class="navtitle-hover" style="display:none" onclick=''/></td>
                     <td style="width:40px"><a class="ui-icon ui-icon-trash" style='text-decoration:none'onclick="editHeadlines()"></a> </td>
                    <td><a onclick='openMailDialog(this.id)'><img src="icons pinvoke/mail.png"/></a></td>
                </tr></table>-->
            <br>
            <div id="headlinetableDiv"></div>
            <div id="selectHeadlineDiv" style="display:none"></div>
            <br>
            <div id="headlinetableDiv"></div>
            <div id="headlinesDialog" title="Headlines Mailer"style="display: none">&nbsp;</div>
        <div id="headlinesMailList" title="Headlines Mailer" style="display: none">
                <table border="0" width="100%">
                    <tr>
                        <Td style="font-family: Verdana, Arial, Helvetica, sans-serif;font-size: 8pt;font-weight: bold;color: #3A457C;padding-left:12px;background-color:#B4D9EE;"width="40%">To </Td>
                        <Td> <input type="text" id="toAddress" class="myTextbox5" maxlength=100 style="width:150px" ></Td>
                    </tr>

                    <tr>
                    <table width="66%" align="center">
                        <tr>

                             <Td>
                                <div id="timeselect" ><div id="headlinetableDiv"></div>
                                    <%=TranslaterHelper.getTranslatedString("HOURS", locale)%>
                                    <select name="hrs" id="hrs" ><%for (int i = 00; i < 24; i++) {%><option  value="<%=i%>"><%=i%></option><%}%></select>
                                   <%=TranslaterHelper.getTranslatedString("MINUTES", locale)%>
                                   <select name="mins" id="mins"><%for (int i = 00; i < 60; i++) {%> <option  value="<%=i%>"><%=i%></option> <%}%> </select>
                                </div>
                            </Td>
<!--                            </td>-->
                        </tr>
                    </table>
                </tr>
                    <tr/>
                    <tr align="center">
                        <Td colspan="2">
                            <input  type="button" class="navtitle-hover" onclick="sendInsightMail()" value="Schedule Mailer" style="width: auto">
                        </Td>
                    </tr>

                </table>
                <input type="text" id="id" name="id">

            </div>

        </form>
        <div id="shareHeadline" title="ShareHeadline" style="display: none">
                                        <table>
                                        <tr>
                                            <td width="20%"><b>Format</b> </td>
                                            <td>
                                                <select name="fileType" id="fileType" style="width:130px">
                                                        <option value="H">HTML</option>
<!--                                                        <option value="E">Excel</option>
                                                        <option value="P">PDF</option>
                                                        <option value="CSV">CSV</option>-->
                                                </select>
                                             </td>
                                        </tr>
                                        <tr>
                                            <td width="20%"><b>Users </b></td>
                                            <td>
                                                <select name="selectusers" id="selectusers" style="width:130px" onchange="displaytextarea()">
                                                     <option value="">Select</option>
                                                        <option value="selected">Selective Users</option>
                                                        <option value="All" >All Users</option>
                                                </select>
                                             </td>
                                        </tr>
                                        <tr>
                                                 <td width="20%" id="share_subject_id" style="display:none"><b>Subject</b> </td>
                                                <td colspan="1" >

                                                    <input type="text" id="share_subject" name="share_subject" style="display:none">
                                                </td>
                                        </tr>
                                        <tr>
                                                 <td width="20%" id="tomailtd" style="display:none"><b>Email To</b> </td>
                                                <td colspan="1" >

                                                    <textarea  id="userstextarea" name="userstextarea" cols="" rows=""  style="width:250px;height:80px;display:none"></textarea>
                                                </td>
                                        </tr>
                                        <tr> <td colspan="2">&nbsp; </td>  </tr>
                                         <tr>
                                                <td colspan="2" align="center">
                                                    <input class="navtitle-hover" type="button" id="share_report_button" value="Share Report" onclick="SendHeadlineEmail()">
                                                  </td>
                                        </tr>
                                        <tr> <td colspan="2">&nbsp; </td>  </tr>
                                        <tr> <td colspan="2"> Please separate multiple Email Id's by comma(,). </td>  </tr>
                                        </table>
              </div>
                                 <script type="text/javascript">
            var reportHeadlineId;
            var flag=false;
         

            function getHeadlineData(id)
            {

            var ids=id.split(",");
            var headlineId=ids[0];

             var currRow = $("#"+ids[0]);
           $( "#"+headlineId+"prgBar").progressbar({value: 37});
            $.post('<%=request.getContextPath()%>/dataSnapshot.do?doAction=getReportHeadlineData&headlineId='+headlineId,$("#headLinesForm").serialize(),
            function(data){
//                data=data.replace('progenTable','tablesorter');
                  $( "#"+headlineId+"prgBar").remove();
                        $("#"+headlineId+"div").html(data);
                                    currRow.attr("initialized","true");
            });



            }
             function initCollapser(divId){
                if (divId == ""){
                    $(".tablesorter")
                    .collapsible("td.collapsible", {
                        collapse: true
                    });
                }
                else{
                    $("#"+divId+" > .tablesorter")
                    .collapsible("td.collapsible", {
                        collapse: true
                    });
                }
            }

            function editHeadlines()
            {
                 $.post('<%=request.getContextPath()%>/dataSnapshot.do?doAction=getReportHeadlines&edit=true',$("#headLinesForm").serialize(),
            function(data){
                $("#headlinetableDiv").html(data);
                 initCollapser("");
            });
            }

            function deleteHeadline(id)
            {
                var flag=confirm("Do you want to delete Headline?");
                if(flag)
                   $.post('<%=request.getContextPath()%>/dataSnapshot.do?doAction=deleteHeadline&headlineId='+id,$("#headLinesForm").serialize(),
            function(data){
               editHeadlines();
            });
            }
            function mailHeadline(id,name){
                //alert(name);
                reportHeadlineId=id;
            $("#share_subject").val(name);
            $("#selectusers").val("");
            $("#userstextarea").val("");
            $("#share_subject_id").hide();
            $("#share_subject").hide();
            $("#tomailtd").hide();
            $("#userstextarea").hide();
            $("#shareHeadline").dialog('open');
            }
            function selectHeadllines()
            {
                 $.post('<%=request.getContextPath()%>/dataSnapshot.do?doAction=getReportHeadlinesforMail',$("#headLinesForm").serialize(),
            function(data){
                $("#selectHeadlineDiv").html(data);
                 $("#selectHeadlineDiv").dialog('open');
            });
            }

            function sendMails()
            {
                 $("#selectHeadlineDiv").dialog('close');
                 var headlineList = [];
                $(':checkbox:checked').each(function(i){
                headlineList[i] = $(this).attr('id');
                });
                 $.post('<%=request.getContextPath()%>/dataSnapshot.do?doAction=sendEmails&headlineList='+headlineList,$("#headLinesForm").serialize(),
            function(data){
                $("#emailDiv").html(data+$("#timeselect").html()+$("#send").html());
                $("#emailDiv").dialog('open');
            });
            }

            function sendEmailstoUsers()
            {
                var hrs=$("select#hrs").val();
                var mins=$("select#mins").val();
                var headlineId=$("#headlinemailid").val();
                  $.post('<%=request.getContextPath()%>/dataSnapshot.do?doAction=sendEmailstoUsers&headlineId='+headlineId+'&hrs='+hrs+'&mins='+mins,$("#headLinesForm").serialize(),
            function(data){

            });

            }

             function openMailDialog(id)
             {
                 $("#toAddress").val("");
                 $("#subject").val("");
                 $("#mailContent").val("");
                $("#headlinesDialog").dialog('open');
                $("#id").val(id);

                $.post('<%=request.getContextPath()%>/dataSnapshot.do?doAction=getReportHeadlinesforMail',$("#headLinesForm").serialize(),
            function(data){
                $("#headlinesDialog").html(data);
//                 initCollapser("");
            });


            }
            function nextStepOfMail()
             {
                var checkboxChecked1=new Array();
                var checkedHeadLines1=new Array();

                $(":checkbox:checked").each(
                 function() {
                   var a=$(this).val();
                   checkboxChecked1.push(a)
                   var trobj=$("#"+a+" td")
                   var c=trobj[1].innerHTML;
                   checkedHeadLines1.push(c)
                  //var temp ="#"+b[0]+" td[1]"
                  //alert($(temp).html());
               }
               );
                  if(checkboxChecked1.length!=0){

                  $("#headlinesDialog").dialog('close');
                  $("#headlinesMailList").dialog('clear');
                  $("#headlinesMailList").dialog('open');
                  var hrsoptions="";
                  for (var k = 00; k < 24; k++)
                  {
                    hrsoptions=hrsoptions+"<option  value='"+k+"'>"+k+"</option>";
                  }

                  var minsoptions="";
                  for (var j = 00; j < 60; j++)
                  {
                    minsoptions=minsoptions+"<option  value='"+j+"'>"+j+"</option>";
                  }

                  var htmlfile="<table>";
                  htmlfile=htmlfile+"<table><tr><td class='myHead'>Description</td><td><textarea style='width: 450px; height: 40px;' rows='400' cols='10' name='description' id='description'></textarea></td></tr></table>";
                  for(var i=0; i<checkboxChecked1.length; i++)
                      {
                         htmlfile=htmlfile+"<table><tr><td class='myHead'>Subject</td><td>"+checkedHeadLines1[i]+"</td></tr><tr id='emails'><td class='myHead'>Mail To</td><td><input type='text' id='toAddress' name='toAddress' style='width:450px;height:20px'></td></tr></table>";
                          //htmlfile=htmlfile+"<table><tr><td>Send only once&nbsp;&nbsp;<input type='radio' id='single' name='time'/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td><td>Send at selected time frequency<input type='radio' id='frequent' name='time'/></td></tr></table>";
                         //htmlfile=htmlfile+"<table><tr><td>Send me anyway<input type='radio' id='default' name='datastatus' value='Send me anyway'/>&nbsp;&nbsp;&nbsp;&nbsp;</td><td>&nbsp;Send headline if data exists&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type='radio' id='particular' name='datastatus' value='Send headline if data exists'/></td></tr></table><br>";
                         //$("#headlinesMailList tr:first").after(htmlfile);
                      }
                      htmlfile=htmlfile+"</table>"
                  htmlfile=htmlfile+"<table></table>"
                  htmlfile=htmlfile+"<table><tr><td><font class='headlinestyle' color='#336699'>Mail Frequency</font></td></tr><tr><td>Schedule&nbsp;&nbsp;<input type='radio' id='single' name='mailfrequency' onclick='schedule()'></td><td>&nbsp;&nbsp;&nbsp;</td><td>Sendme now&nbsp;&nbsp;<input type='radio' id='frequent' name='mailfrequency' onclick='sendme()'></td></tr></table>";
                  htmlfile=htmlfile+"<div id='schedulediv' style='display:none'><table><tr><td><select name='schedule' id='schedule' onchange='changeschedule(this)'><option value='onlyonce'>Onlyonce</option><option value='daily'>Daily</option><option value='hourly'>Hourly</option></select></td></tr><tr><td id='Time'>TIME</td><td>HH<select name='hrs' id='hrs'>"+hrsoptions+"</select>&nbsp;&nbsp;&nbsp;MM<select name='mins' id='mins'>"+minsoptions+"</select></td></tr><tr id='hourly' display:none><td>Frequency</td><td>HH<select name='mailhrs' id='mailhrs' >"+hrsoptions+"</select>&nbsp;&nbsp;&nbsp;MM<select name='mailmins' id='mailmins'>"+minsoptions+"</select></td></tr></table></div>";
                  htmlfile=htmlfile+"<table><tr><td><font class='headlinestyle' color='#336699'>Data Qualifier</font></td></tr><tr><td>Send Anyway<input type='radio' id='default' name='datastatus' value='Send me anyway'>&nbsp;&nbsp;</td><td>&nbsp;</td><td>&nbsp;Send headline if data exists&nbsp;<input type='radio' id='particular' name='datastatus' value='Send headline if data exists'></td></tr></table><br>";
                  htmlfile=htmlfile+"<table align='center'><tr><td colspan='2' align='center'><input class='navtitle-hover' type='button' onclick='sendHeadLinesMail()' value='Schedule Mailer' style='width: auto'></td></tr><input type='hidden' id='headlinesIds' value=''><br><br><tr><td colspan='2'> Please separate multiple Email Id's by comma(,). </td> </tr></table> <input type='hidden' id='headlinesName' value=''>";
                  $("#headlinesMailList").html(htmlfile);
                  $("#headlinesIds").val(checkboxChecked1.toString());
                  $("#headlinesName").val(checkedHeadLines1.join("&&"));
                  }
                  else
                      {
                           alert("Please select a headline")
                      }


             }
             function schedule(){
              $("#hourly").hide();
              $("#schedulediv").show();
                }
                function sendme(){
                    $("#schedulediv").hide();
                }
             function changeschedule(option){
             var open = document.getElementById("hourly");
               if(option.value=="hourly")
               {   $("#Time").val("Strat Time")
                      open.style.display='';
                 }
                  else{
                       open.style.display='none';
                  }
             }
            function sendHeadLinesMail()
            {
                var headlineId=$("#headlinesIds").val();
                var headlinesName=$("#headlinesName").val();
                //var mailContent=encodeURIComponent( $("#mailContent").val());
                //var subject=encodeURIComponent( $("#subject").val());
                var toAddress=encodeURIComponent( $("#toAddress").val());
                var hrs= $("#hrs").val()+":"+$("#mins").val();
                var sendMailType="unchecked";
                if($("#default").attr('checked')==true)
                    sendMailType='unchecked';
                else
                    sendMailType='checked';
                //var mins=encodeURIComponent( $("#mins").val());
                var timeVal="onlyonce";
                var mailhr="",mailmin="";
                if($("#single").attr('checked')==true)
                    {
                  //   alert($("#schedule").val())
                     timeVal=$("#schedule").val();
                     if($("#schedule").val()=='hourly')
                      {   mailhr=$("#mailhrs").val()
                         mailmin=$("#mailmins").val()}
                    }
                    else
                        timeVal='frequent';
//                var timeVal="single";
//                if($("#single").attr('checked')==true)
//                    timeVal='single';
//                else
//                    timeVal='frequent';
                var description=""
                description=$("#description").val();
                var toemails=new Array();
                var hrss=new Array();
                var minss=new Array();
                var data = $('input:text[name=toAddress]');
                $.each(data, function(key, object) {
                var a=object.value;
                a.replace(/,$/," This ");
                toemails.push(a);
                });

                var data1 = $('select[name=hrs]');
                $.each(data1, function(key1, object1) {
                hrss.push(object1.value);
                });

                var data2 = $('select[name=mins]');
                $.each(data2, function(key2, object2) {
                minss.push(object2.value);
                });

                if(toAddress=="")
                    alert("Please specify recipient email address");
                else if($("#default").attr('checked')==false&&$("#particular").attr('checked')==false)
                    alert("Select sending options using radio buttons");
                else
                {
                    $("#headlinesMailList").dialog('close');
                    $("#loading").show();
                    $.ajax({
                        url:"dataSnapshot.do?doAction=getReportHeadlineDataForMail&headlineId="+headlineId+"&toAddress="+encodeURIComponent(toemails.join("&&"))+"&hrs="+encodeURIComponent(hrss.toString())+"&mins="+encodeURIComponent(minss.toString())+"&headlinesName="+encodeURIComponent(headlinesName)+'&sendMailType='+sendMailType+'&timeVal='+timeVal+'&mailhr='+mailhr+'&mailmin='+mailmin+'&description='+description,
                        success:function(data){
                             $("#loading").hide();
                             if(data=='success')
                            alert("HeadLines mailer scheduled sucessfully")
                            else
                                alert("There is no data for headline");
                        }
                    });

                }
            }
            function displaytextarea()
            {
            var a=$("#selectusers").val();
            if (a=="selected" )
            {
               $("#tomailtd").show();
               $("#share_subject_id").show();
               $("#share_subject").show();
               $("#userstextarea").show();
               $("#userstextarea").elastic();
            }else
            {
              $("#userstextarea").val("");
              $("#share_subject_id").hide();
              $("#share_subject").hide();
              $("#tomailtd").hide();
              $("#userstextarea").hide();
            }
            }
         function SendHeadlineEmail(){
        var fileType=document.getElementById("fileType").value;
        var selectusers=document.getElementById("selectusers").value;
        var share_subject=document.getElementById("share_subject").value;
        var userstextarea=document.getElementById("userstextarea").value;
        if(selectusers=="")
        {
            alert("select Selective Users")
        }
        else
        {
          $("#shareHeadline").dialog('close');
            $("#loading").show();
            $.ajax({
                url:'dataSnapshot.do?doAction=sendHeadlineMail&headlineid='+reportHeadlineId+'&fileType='+fileType+'&selectusers='+selectusers+'&share_subject='+share_subject+'&userstextarea='+encodeURIComponent(userstextarea),
                success:function(data)
                {
                    $("#loading").hide();
                    alert("headline shared sucessfully with the Users");
                }
            });
        }
    }
//            function selectHeadllines()
//            {
//                 $.post('<%=request.getContextPath()%>/dataSnapshot.do?doAction=getReportHeadlinesforMail',$("#headLinesForm").serialize(),
//            function(data){
//                $("#selectHeadlineDiv").html(data);
//                 $("#selectHeadlineDiv").dialog('open');
//            });
//            }
//
//            function sendMails()
//            {
//                 $("#selectHeadlineDiv").dialog('close');
//                 var headlineList = [];
//                $(':checkbox:checked').each(function(i){
//                headlineList[i] = $(this).attr('id');
//                });
//                 $.post('<%=request.getContextPath()%>/dataSnapshot.do?doAction=sendEmails&headlineList='+headlineList,$("#headLinesForm").serialize(),
//            function(data){
//                $("#emailDiv").html(data+$("#timeselect").html()+$("#send").html());
//                $("#emailDiv").dialog('open');
//            });
//            }
//
//            function sendEmailstoUsers()
//            {
//                var hrs=$("select#hrs").val();
//                var mins=$("select#mins").val();
//                var headlineId=$("#headlinemailid").val();
//                  $.post('<%=request.getContextPath()%>/dataSnapshot.do?doAction=sendEmailstoUsers&headlineId='+headlineId+'&hrs='+hrs+'&mins='+mins,$("#headLinesForm").serialize(),
//            function(data){
//
//            });
//
//            }

        </script>
    </body>
</html>
