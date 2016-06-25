<%@page contentType="text/html" pageEncoding="UTF-8" import="com.progen.i18n.TranslaterHelper,java.util.Locale"%>
<%--
    Document   : configEtl
    Created on : Dec 17, 2012, 4:52:01 PM
    Author     : Nazneen Khan
--%>

<!DOCTYPE html>

         <% String themeColor = "blue";
            if (session.getAttribute("theme") == null) {
                session.setAttribute("theme", themeColor);
            } else {
                themeColor = String.valueOf(session.getAttribute("theme"));
            }

         //added by Dinanath
            Locale cle = null;
            cle = (Locale) session.getAttribute("UserLocaleFormat");
            String contextPath=request.getContextPath();

         %>
         <html>
    <head>
       <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script src="<%=contextPath%>/javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=themeColor %>/jquery-ui-1.7.3.custom.css" rel="stylesheet" />
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=themeColor %>/TableCss.css" rel="stylesheet" />
        <link href="http://code.google.com/apis/maps/documentation/javascript/examples/standard.css" rel="stylesheet" type="text/css" />

     <style type="text/css">
         .migrate
            {
              
                font-size: 10pt;
                font-weight: bold;
                color: white;
                padding-left:12px;
                border:0px;
            }
      </style>

       
    </head>
    <body>
         <script type="text/javascript">
//         var compName = "";

          $(document).ready(function(){
               if ($.browser.msie == true){
                    $("#addNewConnDiv").dialog({
                        autoOpen: false,
                        height: 480,
                        width: 700,
                        position: 'justify',
                        modal: true
                    });
                    $("#editConnDiv").dialog({
                        autoOpen: false,
                        height: 480,
                        width: 700,
                        position: 'justify',
                        modal: true
                    });
                    $("#addNewCompanyDiv").dialog({
                        autoOpen: false,
                        height: 230,
                        width: 437,
                        position: 'justify',
                        modal: true
                    });
                    $("#editCompanyDetailsDiv").dialog({
                        autoOpen: false,
                        height: 230,
                        width: 437,
                        position: 'justify',
                        modal: true
                    });
                    $("#userAssignmentDiv").dialog({
                        autoOpen: false,
                        height: 450,
                        width: 700,
                        position: 'top',
                        modal: true
                    });
               }
               else {
                   $("#addNewConnDiv").dialog({
                        autoOpen: false,
                        height: 480,
                        width: 700,
                        position: 'justify',
                        modal: true
                    });
                    $("#editConnDiv").dialog({
                        autoOpen: false,
                        height: 480,
                        width: 700,
                        position: 'justify',
                        modal: true
                    });
                     $("#addNewCompanyDiv").dialog({
                        autoOpen: false,
                        height: 230,
                        width: 437,
                        position: 'justify',
                        modal: true
                    });
                    $("#editCompanyDetailsDiv").dialog({
                        autoOpen: false,
                        height: 230,
                        width: 437,
                        position: 'justify',
                        modal: true
                    });
                    $("#userAssignmentDiv").dialog({
                        autoOpen: false,
                        height: 450,
                        width: 700,
                        position: 'justify',
                        modal: true
                    });
               }

           });
           function createCompany() {
              var htmlVar = "";
               htmlVar+="<table id=\"measureDetTab\" class=\"tablesorter\"  border=\"1\" cellpadding=\"1\" cellspacing=\"1\">\n\
                                      <tr><td align=\"right\" class=\"migrate\" colspan=\"3\"><img width=\"22px\" height=\"22px\" border=\"0\"  src=\"icons pinvoke/plus-circle.png\" alt=\"\" title=\"Add New Company\" onclick=\"addNewCompany()\"></a></img></td></tr>\n\
                                      <tr><td>\n\
                                        <table id=\"compConnDetTable\" class=\"tablesorter\"  border=\"1\" cellpadding=\"1\" cellspacing=\"1\">\n\
                                         <thead><tr>\n\
                                                    <th class=\"migrate\"><label>Company Name </label></th>\n\
                                                    <th class=\"migrate\" ><label>Company Description </label></th>\n\
                                                    <th class=\"migrate\" ><label>Action </label></th>\n\
                                                </tr></thead>\n\
                                        <tbody id=\"companyDetailsTabBody\"></tbody>\n\
                                        </table>\n\
                                    </table>";

                        $("#measureDetTabBody").html(htmlVar)
               htmlVar = "";
               $.post("<%= request.getContextPath()%>/userLayerAction.do?userParam=getCompanyDetails",function(data){
               var jsonVar=eval('('+data+')')
               var compId=jsonVar.compId
               var compName=jsonVar.compName
               var compDesc=jsonVar.compDesc

                for(var i=0;i<compId.length;i++)
                   {
                       htmlVar+= "<tr><td style=\"background-color: lightgoldenrodyellow ; text-align: center\">"+compName[i]+"</td>\n\
                                        <td style=\"background-color: lightgoldenrodyellow ;text-align: center\">"+compDesc[i]+"</td>\n\
                                        <td><input type=\"hidden\" name=\"compId"+[i]+"\" id=\""+compId[i]+"\" value=\""+compId[i]+"\">\n\
                                            <img width=\"22px\" height=\"22px\" border=\"0\" src=\"icons pinvoke/pencil-small.png\" alt=\"\" title=\"Edit Company Details\" onclick=\"editCompany("+compId[i]+")\"></a></img>\n\
                                    </td></tr>";
                         $("#companyDetailsTabBody").html(htmlVar)
                   }
                 });
           }
           function addNewCompany(){
               var htmlVar="" ;
               htmlVar+="<table id=\"addEtlTab\" border=\"2\" cellpadding=\"1\" cellspacing=\"1\">\n\
                                    <tr><td align=\"left\" class=\"migrate\" colspan=\"2\"><label style=\"color:Red\">*</label>All fields are compulsory</td></tr>\n\
                                    <tr><td align=\"left\" class=\"migrate\"><label>Select Type : &nbsp;&nbsp;&nbsp;&nbsp;</label></td>\n\
                                        <td align=\"left\" style=\"background-color:lightgoldenrodyellow; color:black;\"><input type=\"radio\" name=\"checkData\" value=\"createData\" id=\"checkval1\" checked > Create From Data\n\
                                                           <input type=\"radio\" name=\"checkData\" value=\"migrate\" id=\"checkval2\" disabled > Migrate From Source</td></tr>\n\
                                      <tr><td align=\"left\" class=\"migrate\"><label>Company Name : &nbsp;&nbsp;&nbsp;&nbsp;</label></td>\n\
                                        <td align=\"left\"><input type=\"text\" class=\"\" value=\"\" id=\"companyName\" name=\"companyName\" onkeyup=\"copyDetails()\" size=\"45px\" style=\"background-color:lightgoldenrodyellow; color:black;\"/></td></tr>\n\
                                    <tr><td align=\"left\" class=\"migrate\"><label>Company Description : &nbsp;&nbsp;&nbsp;&nbsp;</label></td>\n\
                                        <td align=\"left\"><input type=\"text\" class=\"\" value=\"\" id=\"companyDesc\" name=\"companyDesc\" size=\"45px\" style=\"background-color:lightgoldenrodyellow; color:black;\"/></td></tr>\n\
                            <tr><td class=\"migrate\" colspan=\"2\">&nbsp;</td></tr>\n\
                            </table>";
                         $("#addNewCompanyTab").html(htmlVar)
                        $("#addNewCompanyDiv").dialog('open')
           }

            function copyDetails(){
                var name=document.addNewCompanyForm.companyName.value;
                document.addNewCompanyForm.companyDesc.value=name;
            }
            function copyDetails1(){
                var name=document.editCompanyDetailsForm.companyName.value;
                document.editCompanyDetailsForm.companyDesc.value=name;
            }


            function saveCompany(){
                var companyName=document.addNewCompanyForm.companyName.value;
                var companyDesc=document.addNewCompanyForm.companyDesc.value;

                var companyName_len = companyName.length;
                var companyDesc_len = companyDesc.length;
                if(companyName_len==0 || companyDesc_len==0){
                    alert('Please Enter Company Name!')
                }
                else {
                    $.post('userLayerAction.do?userParam=saveCompanyDetails&companyName='+companyName+'&companyDesc='+companyDesc,function(data)  {
                    if(data==1)
                            alert('Details Saved Successfully')
                    else
                            alert('Error ! Details Not Saved')
                       $("#addNewCompanyDiv").dialog('close')
                        createCompany();
                    });
                }
            }

           function editCompany(compId){
                var htmlVar = "";
                $.post("<%= request.getContextPath()%>/userLayerAction.do?userParam=editCompanyDetails&compId="+compId,function(data){
                    var jsonVar=eval('('+data+')')
                    var compName=jsonVar.compName
                    var compDesc=jsonVar.compDesc

                 htmlVar+="<table id=\"editCompanyDet\" border=\"2\" cellpadding=\"1\" cellspacing=\"1\">\n\
                                    <tr><td align=\"left\" class=\"migrate\" colspan=\"2\"><label style=\"color:Red\">*</label>All fields are compulsory</td></tr>\n\
                                    <tr><td align=\"left\" class=\"migrate\"><label>Select Type : &nbsp;&nbsp;&nbsp;&nbsp;</label></td>\n\
                                        <td align=\"left\" style=\"background-color:lightgoldenrodyellow; color:black;\"><input type=\"radio\" name=\"checkData\" value=\"createData\" id=\"checkval1\" checked > Create From Data\n\
                                                           <input type=\"radio\" name=\"checkData\" value=\"migrate\" id=\"checkval2\" disabled > Migrate From Source</td></tr>\n\
                                      <tr><td align=\"left\" class=\"migrate\"><label>Company Name : &nbsp;&nbsp;&nbsp;&nbsp;</label></td>\n\
                                        <td align=\"left\"><input type=\"text\" class=\"\" value=\""+compName[0]+"\" id=\"companyName\" name=\"companyName\" onkeyup=\"copyDetails1()\" size=\"45px\" style=\"background-color:lightgoldenrodyellow; color:black;\"/></td></tr>\n\
                                    <tr><td align=\"left\" class=\"migrate\"><label>Company Description : &nbsp;&nbsp;&nbsp;&nbsp;</label></td>\n\
                                        <td align=\"left\"><input type=\"text\" class=\"\" value=\""+compDesc[0]+"\" id=\"companyDesc\" name=\"companyDesc\" size=\"45px\" style=\"background-color:lightgoldenrodyellow; color:black;\"/></td></tr>\n\
                            <tr><td class=\"migrate\"><input type=\"hidden\" name=\"compId\" id=\"compId\" value=\""+compId+"\"></td>\n\
                                <td class=\"migrate\">&nbsp;</td></tr>\n\
                            </table>";
                        $("#editCompanyDetailsTab").html(htmlVar)
                        $("#editCompanyDetailsDiv").dialog('open')
                });
           }
           function updateCompany(){
              var companyName=document.forms.editCompanyDetailsForm.companyName.value;
              var companyDesc=document.forms.editCompanyDetailsForm.companyDesc.value;
              var compId=document.forms.editCompanyDetailsForm.compId.value;

               var companyName_len = companyName.length;
               var companyDesc_len = companyDesc.length;
                if(companyName_len==0 || companyDesc_len==0){
                    alert('Please Enter Complete Details!')
                }
                else {
                    $.post('userLayerAction.do?userParam=updateCompanyDetails&companyName='+companyName+'&companyDesc='+companyDesc+'&compId='+compId,function(data)  {
                    if(data==1)
                            alert('Details Updated Successfully')
                    else
                            alert('Error ! Details Not Updated')
                       $("#editCompanyDetailsDiv").dialog('close')
                        createCompany();
                    });
                }
          }
           function addEtlConnDetails() {
               var htmlVar = "";
               htmlVar+="<table id=\"measureDetTab\" class=\"tablesorter\"  border=\"1\" cellpadding=\"1\" cellspacing=\"1\">\n\
                                    <tr><td colspan=\"9\" class=\"migrate\" align=\"right\"><label>Select Company :</label> <select name=\"companySelect\" id=\"companySelect\" onchange=\"getcompConnDet()\"></select></td></tr>\n\
                                    <tr><td>\n\
                                        <table id=\"compConnDetTable\" class=\"tablesorter\"  border=\"1\" cellpadding=\"1\" cellspacing=\"1\">\n\
                                         <thead><tr>\n\
                                                <th class=\"migrate\"><label>Company Name </label></th>\n\
                                                <th class=\"migrate\" ><label>Load Type </label></th>\n\
                                                <th class=\"migrate\" ><label>User Name </label></th>\n\
                                                <th class=\"migrate\"><label>Server </label></th>\n\
                                                <th class=\"migrate\"><label>Port Number </label></th>\n\
                                                <th class=\"migrate\"><label>Source <br>Time Zone </label></th>\n\
                                                <th class=\"migrate\"><label>Target <br>Time Zone </label></th>\n\
                                                <th class=\"migrate\"><label>DB Connection </label></th>\n\
                                                <th class=\"migrate\"><label>Action </label></th>\n\
                                        </tr></thead>\n\
                                        <tbody id=\"companyConnDetTabBody\"></tbody>\n\
                                        </table>\n\
                                    </td></tr></table>";
                        $("#measureDetTabBody").html(htmlVar)
                        $.get("<%= request.getContextPath()%>/userLayerAction.do?userParam=getAllCompanyDetails",function(data){
                            $("#companySelect").html(data)
                        });
           }
           function showProcessList() {
               $("#measureDetTabBody").html("")
              $.post("<%= request.getContextPath()%>/userLayerAction.do?userParam=getProcessListDetails",function(data){
                  if(data==""){
                      alert('Connection Error! This will work only for Mysql Databases...')
                  }
                  else {
                    var jsonVar=eval('('+data+')')
                    var processId=jsonVar.processId
                    var user=jsonVar.user
                    var host=jsonVar.host
                    var database=jsonVar.database
                    var command=jsonVar.command
                    var time=jsonVar.time
                    var state=jsonVar.state
                    var info=jsonVar.info

               var htmlVar = "";
               htmlVar+="<br><table id=\"measureDetTab\" class=\"tablesorter\"  border=\"1\" cellpadding=\"1\" cellspacing=\"1\">\n\
                                    <tr><td><table id=\"showProcessTable\" class=\"tablesorter\"  border=\"1\" cellpadding=\"1\" cellspacing=\"1\">\n\
                                         <thead><tr>\n\
                                                <th class=\"migrate\"><label>Process Id </label></th>\n\
                                                <th class=\"migrate\"><label>User </label></th>\n\
                                                <th class=\"migrate\"><label>Host </label></th>\n\
                                                <th class=\"migrate\"><label>Database </label></th>\n\
                                                <th class=\"migrate\"><label>Command </label></th>\n\
                                                <th class=\"migrate\"><label>Time <br>Time Zone </label></th>\n\
                                                <th class=\"migrate\"><label>State </label></th>\n\
                                                <th class=\"migrate\"><label>Information </label></th>\n\
                                                <th class=\"migrate\"><label>Action </label></th>\n\
                                    </tr></thead>";
            $("#measureDetTabBody").html(htmlVar)
              for(var i=0;i<processId.length;i++)
                   {
                       htmlVar+= "<tr><td style=\"background-color: lightgoldenrodyellow ; text-align: center\">"+processId[i]+"</td>\n\
                                        <td style=\"background-color: lightgoldenrodyellow ;text-align: center\">"+user[i]+"</td>\n\
                                        <td style=\"background-color: lightgoldenrodyellow ;text-align: center\">"+host[i]+"</td>\n\
                                        <td style=\"background-color: lightgoldenrodyellow ;text-align: center\">"+database[i]+"</td>\n\
                                        <td style=\"background-color: lightgoldenrodyellow ;text-align: center\">"+command[i]+"</td>\n\
                                        <td style=\"background-color: lightgoldenrodyellow ;text-align: center\">"+time[i]+"</td>\n\
                                        <td style=\"background-color: lightgoldenrodyellow ;text-align: center\">"+state[i]+"</td>\n\
                                        <td style=\"background-color: lightgoldenrodyellow ;text-align: center\">"+info[i]+"</td>\n\
                                        <td style=\"text-align: center\"><img width=\"22px\" height=\"22px\" border=\"0\" src=\"icons pinvoke/cross-small.png\" alt=\"\" title=\"Kill Process\" onclick=\"killProcess("+processId[i]+")\"></img></td>\n\
                                  </tr>";
                         $("#measureDetTabBody").html(htmlVar)
                   }
              }
              });
           }
            function killProcess(processId){
               var retVal = confirm("Are you sure you want to continue to kill this process !");
               if( retVal == true ){
                   $.post('userLayerAction.do?userParam=killProcess&processId='+processId,function(data)  {
//                                if(data==1){
                                        alert('Process Killed Successfully')
                                        showProcessList();
//                                }
//                                else{
//                                    alert('Error ! Process not Killed')
//                                }

                   });
               }
            }

            function getcompConnDet(){
            var companyId = $("#companySelect").val();
            $("#companyConnDetTabBody").html("")
            var htmlVar = "";
               htmlVar +="";
               $.post("<%= request.getContextPath()%>/userLayerAction.do?userParam=getCompanyConnDetails&companyId="+companyId,function(data){
               var jsonVar=eval('('+data+')')
               var id=jsonVar.id
               var compId=jsonVar.compId
               var compName=jsonVar.compName
               var loadType=jsonVar.loadType
               var userName=jsonVar.userName
               var pswd=jsonVar.pswd
               var server=jsonVar.server
               var serviceId=jsonVar.serviceId
               var serviceName=jsonVar.serviceName
               var portNumber=jsonVar.portNumber
               var sourceTz=jsonVar.sourceTz
               var targetTz=jsonVar.targetTz
               var dbConn=jsonVar.dbConn
                for(var i=0;i<compId.length;i++)
                   {
                       htmlVar+= "<tr><td style=\"background-color: lightgoldenrodyellow ; text-align: center\">"+compName[i]+"</td>\n\
                                        <td style=\"background-color: lightgoldenrodyellow ;text-align: center\">"+loadType[i]+"</td>\n\
                                        <td style=\"background-color: lightgoldenrodyellow ;text-align: center\">"+userName[i]+"</td>\n\
                                        <td style=\"background-color: lightgoldenrodyellow ;text-align: center\">"+server[i]+"</td>\n\
                                        <td style=\"background-color: lightgoldenrodyellow ;text-align: center\">"+portNumber[i]+"</td>\n\
                                        <td style=\"background-color: lightgoldenrodyellow ;text-align: center\">"+sourceTz[i]+"</td>\n\
                                        <td style=\"background-color: lightgoldenrodyellow ;text-align: center\">"+targetTz[i]+"</td>\n\
                                        <td style=\"background-color: lightgoldenrodyellow ;text-align: center\">"+dbConn[i]+"</td>\n\
                                        <td><input type=\"hidden\" name=\"compId"+[i]+"\" id=\""+compId[i]+"\" value=\""+compId[i]+"\">\n\
                                            <img width=\"22px\" height=\"22px\" border=\"0\" src=\"icons pinvoke/pencil-small.png\" alt=\"\" title=\"Edit Connection Details\" onclick=\"editConnDetails("+compId[i]+","+id[i]+")\"></a></img>\n\
                                            <img width=\"22px\" height=\"22px\" border=\"0\" src=\"icons pinvoke/cross-small.png\" alt=\"\" title=\"Delete Connection Details\" onclick=\"deleteConnDetails("+compId[i]+","+id[i]+")\"></img>\n\
                                     </td></tr>";
                         $("#companyConnDetTabBody").html(htmlVar)
                   }
                   htmlVar+="<tr><td colspan=\"9\" class=\"migrate\" align=\"right\"><img width=\"22px\" height=\"22px\" border=\"0\" src=\"icons pinvoke/plus-circle.png\" title=\"Add New Connection Details\" onclick=\"addNewConnDetails()\"></td></tr>";
                   $("#companyConnDetTabBody").html(htmlVar)
                 });
            }

            function editConnDetails(compId,tabId){

                 var htmlVar = "";
               $.post("<%= request.getContextPath()%>/userLayerAction.do?userParam=editCompanyConnDetails&tabId="+tabId+"&compId="+compId,function(data){
                    var jsonVar=eval('('+data+')')

                    var compName=jsonVar.compName
                    var loadTypes=jsonVar.loadType
                    var userName=jsonVar.userName
                    var pswd=jsonVar.pswd
                    var server=jsonVar.server
                    var serviceId=jsonVar.serviceId
                    var serviceName=jsonVar.serviceName
                    var portNumber=jsonVar.portNumber
                    var dsnName=jsonVar.dsnName
                    var dbConnType=jsonVar.dbConnType
                    var dbName=jsonVar.dbName
                    var sourceTz=jsonVar.sourceTz
                    var targetTz=jsonVar.targetTz
                    var runIndep=jsonVar.runIndep

                 var tzArray = ['Australia/Adelaide','GMT','Australia/Sydney'];
                  htmlVar+="<table id=\"addNewConn1\" class=\"tablesorter\"  border=\"1px solid\" cellpadding=\"0\" cellspacing=\"0\" align=\"center\">\n\
                            <tr><td align=\"left\" class=\"migrate\">"+compName[0]+"</td>\n\
                                <td align=\"right\" class=\"migrate\"><label style=\"color: #00486A; font-family:  monospace;font-size: 12px;font-style:  normal\">Fields marked </label><label style=\"color:Red\">*</label> <label style=\"color: #00486A; font-family:  monospace;font-size: 12px;font-style:  normal\"> are compulsory &nbsp;</label></td></tr>\n\
                            <tr><td class=\"migrate\" colspan=\"2\">&nbsp;</td></tr>\n\
                             <tr class=\"migrate\"><td align=\"left\" class=\"migrate\" ><label style=\"color:Red\">*</label><label>Load Type : &nbsp;&nbsp;&nbsp;&nbsp;</label></td>\n\
                                <td><input type=\"text\" class=\"\" value=\""+loadTypes[0]+"\" id=\"loadTypes\" name=\"loadTypes\" size=\"45px\" style=\"background-color:lightgoldenrodyellow; color:black;\" readonly/>(can not change this field)</td></tr>\n\
                            <tr class=\"migrate\"><td align=\"left\" class=\"migrate\"><label style=\"color:Red\">*</label><label>User Name : &nbsp;&nbsp;&nbsp;&nbsp;</label></td>\n\
                                <td align=\"left\"><input type=\"text\" class=\"\" value=\""+userName[0]+"\" id=\"userName\" name=\"userName\" size=\"45px\" style=\"background-color:lightgoldenrodyellow; color:black;\"/>(Example: report)</td></tr>\n\
                            <tr class=\"migrate\"><td align=\"left\" class=\"migrate\"><label style=\"color:Red\">*</label><label>Password : &nbsp;&nbsp;&nbsp;&nbsp;</label></td>\n\
                                <td align=\"left\"><input type=\"password\" class=\"\" value=\""+pswd[0]+"\" id=\"password\" name=\"password\" size=\"45px\" style=\"background-color:lightgoldenrodyellow; color:black;\"/></td></tr>\n\
                            <tr class=\"migrate\"><td align=\"left\" class=\"migrate\"><label style=\"color:Red\">*</label><label>Server : &nbsp;&nbsp;&nbsp;&nbsp;</label></td>\n\
                                <td align=\"left\"><input type=\"text\" class=\"\" value=\""+server[0]+"\" id=\"server\" name=\"server\" size=\"45px\" style=\"background-color:lightgoldenrodyellow; color:black;\"/>(Example: localhost or 191.166.0.111)</td></tr>\n\
                            <tr class=\"migrate\"><td align=\"left\" class=\"migrate\"><label>Service Id : &nbsp;&nbsp;&nbsp;&nbsp;</label></td>\n\
                                <td align=\"left\"><input type=\"text\" class=\"\" value=\""+serviceId[0]+"\" id=\"serviceId\" name=\"serviceId\" size=\"45px\" style=\"background-color:lightgoldenrodyellow; color:black;\"/></td></tr>\n\
                            <tr class=\"migrate\"><td align=\"left\" class=\"migrate\"><label>Service Name : &nbsp;&nbsp;&nbsp;&nbsp;</label></td>\n\
                                <td align=\"left\"><input type=\"text\" class=\"\" value=\""+serviceName[0]+"\" id=\"serviceName\" name=\"serviceName\" size=\"45px\" style=\"background-color:lightgoldenrodyellow; color:black;\"/></td></tr>\n\
                            <tr class=\"migrate\"><td align=\"left\" class=\"migrate\"><label style=\"color:Red\">*</label><label>Port Number : &nbsp;&nbsp;&nbsp;&nbsp;</label></td>\n\
                                <td align=\"left\"><input type=\"text\" class=\"\" value=\""+portNumber[0]+"\" id=\"port\" name=\"port\" size=\"45px\" style=\"background-color:lightgoldenrodyellow; color:black;\"/>(Example: 1521)</td></tr>\n\
                            <tr class=\"migrate\"><td align=\"left\" class=\"migrate\"><label style=\"color:Red\">*</label><label>Source Time Zone : &nbsp;&nbsp;&nbsp;&nbsp;</label></td>\n\
                                <td><select name=\"SourceTimeZone\" id=\"SourceTimeZone\">";
                            for(var i=0;i<tzArray.length;i++){
                                var falg=0;
                                if(sourceTz[0]==tzArray[i])
                                    htmlVar+="<option selected value = '"+tzArray[i]+"'>"+tzArray[i]+"</option>";
                                else
                                    htmlVar+="<option value = '"+tzArray[i]+"'>"+tzArray[i]+"</option>";
                            }
                            htmlVar+=" </select></td></tr>\n\
                            <tr class=\"migrate\"><td align=\"left\" class=\"migrate\" ><label style=\"color:Red\">*</label><label>Target Time Zone : &nbsp;&nbsp;&nbsp;&nbsp;</label></td>\n\
                                <td><select name=\"TargetTimeZone\" id=\"TargetTimeZone\">";
                              for(var i=0;i<tzArray.length;i++){
                                var falg=0;
                                if(targetTz[0]==tzArray[i])
                                    htmlVar+="<option selected value = '"+tzArray[i]+"'>"+tzArray[i]+"</option>";
                                else
                                  htmlVar+="<option value = '"+tzArray[i]+"'>"+tzArray[i]+"</option>";
                              }
                            htmlVar+="</select></td></tr>\n\
                            <tr class=\"migrate\"><td align=\"left\" class=\"migrate\"><label>DSN Name : &nbsp;&nbsp;&nbsp;&nbsp;</label></td>\n\
                                <td align=\"left\"><input type=\"text\" class=\"\" value=\""+dsnName[0]+"\" id=\"dsnName\" name=\"dsnName\" size=\"45px\" style=\"background-color:lightgoldenrodyellow; color:black;\"/></td></tr>\n\
                            <tr class=\"migrate\"><td align=\"left\" class=\"migrate\"><label>DB Connection Type : &nbsp;&nbsp;&nbsp;&nbsp;</label></td>\n\
                                <td align=\"left\"><input type=\"text\" class=\"\" value=\""+dbConnType[0]+"\" id=\"dbConnType\" name=\"dbConnType\" size=\"45px\" style=\"background-color:lightgoldenrodyellow; color:black;\"/>(Example: sqlserver or oracle)</td></tr>\n\
                            <tr class=\"migrate\"><td align=\"left\" class=\"migrate\"><label style=\"color:Red\">*</label><label>DB Name : &nbsp;&nbsp;&nbsp;&nbsp;</label></td>\n\
                                <td align=\"left\"><input type=\"text\" class=\"\" value=\""+dbName[0]+"\" id=\"dbName\" name=\"dbName\" size=\"45px\" style=\"background-color:lightgoldenrodyellow; color:black;\"/>(Example: ctserver or kisea)</td></tr>\n\
                            <tr class=\"migrate\"><td align=\"left\" class=\"migrate\"><label>Run Independently : &nbsp;&nbsp;&nbsp;&nbsp;</label></td>\n\
                                <td align=\"left\"><select name=\"runIndep\" id=\"runIndep\">";
                                 if(runIndep=='Y')
                                    htmlVar+="<option selected value = 'Y'>YES</option>";
                                 else
                                    htmlVar+="<option value = 'Y'>YES</option>";
                                if(runIndep=='N')
                                htmlVar+="<option selected value = 'N'>NO</option>";
                                 else
                                htmlVar+="<option value = 'N'>NO</option>";

                             htmlVar+="</select></td></tr>\n\
                        <tr><td class=\"migrate\">&nbsp;</td>\n\
                                <td class=\"migrate\"><input type=\"hidden\" name=\"tabId\" id=tabId\" value=\""+tabId+"\"></td></tr>\n\                     \n\
                            </table>";
                        $("#editConnTab").html(htmlVar)
                        $("#editConnDiv").dialog('open');
                });
            }
            function deleteConnDetails(compId,tabId){
               var retVal = confirm("Are you sure you want to continue to delete this connection details !");
               if( retVal == true ){
                   $.post('userLayerAction.do?userParam=deleteConnectionsDetails&tabId='+tabId,function(data)  {
                                if(data==1){
                                        alert('Details Deleted Successfully')
                                        addEtlConnDetails();
                                }
                                else{
                                    alert('Error ! Details not Deleted')
                                }

                   });
               }
            }
            function addNewConnDetails(){
                var companyId = $("#companySelect").val();
                var flag = 0;
                $.get("<%= request.getContextPath()%>/userLayerAction.do?userParam=getloadtypeDetails&companyId="+companyId,function(data){
                    if(data==''){
                       flag = 0;
                    }
                    else
                        flag = 1;

                if(flag==1){
                $.get("<%= request.getContextPath()%>/userLayerAction.do?userParam=getCompanyName&companyId="+companyId,function(data){
                            var jsonVar=eval('('+data+')')
                            var companyName=jsonVar.compName

                var htmlVar = "";
                htmlVar+="<table id=\"addNewConn1\" class=\"tablesorter\"  border=\"1px solid\" cellpadding=\"0\" cellspacing=\"0\" align=\"center\">\n\
                            <tr><td align=\"left\" class=\"migrate\">"+companyName[0]+"</td>\n\
                                <td align=\"right\" class=\"migrate\"><label style=\"color: #00486A; font-family:  monospace;font-size: 12px;font-style:  normal\">Fields marked </label><label style=\"color:Red\">*</label> <label style=\"color: #00486A; font-family:  monospace;font-size: 12px;font-style:  normal\"> are compulsory &nbsp;</label></td></tr>\n\
                            <tr><td class=\"migrate\" colspan=\"2\">&nbsp;</td></tr>\n\
                             <tr class=\"migrate\"><td align=\"left\" class=\"migrate\" ><label style=\"color:Red\">*</label><label>Load Type : &nbsp;&nbsp;&nbsp;&nbsp;</label></td>\n\
                                <td><select name=\"loadType\" id=\"loadType\"></select></td></tr>\n\
                            <tr class=\"migrate\"><td align=\"left\" class=\"migrate\"><label style=\"color:Red\">*</label><label>User Name : &nbsp;&nbsp;&nbsp;&nbsp;</label></td>\n\
                                <td align=\"left\"><input type=\"text\" class=\"\" value=\"\" id=\"userName\" name=\"userName\" size=\"45px\" style=\"background-color:lightgoldenrodyellow; color:black;\"/>(Example: report)</td></tr>\n\
                            <tr class=\"migrate\"><td align=\"left\" class=\"migrate\"><label style=\"color:Red\">*</label><label>Password : &nbsp;&nbsp;&nbsp;&nbsp;</label></td>\n\
                                <td align=\"left\"><input type=\"password\" class=\"\" value=\"\" id=\"password\" name=\"password\" size=\"45px\" style=\"background-color:lightgoldenrodyellow; color:black;\"/></td></tr>\n\
                            <tr class=\"migrate\"><td align=\"left\" class=\"migrate\"><label style=\"color:Red\">*</label><label>Server : &nbsp;&nbsp;&nbsp;&nbsp;</label></td>\n\
                                <td align=\"left\"><input type=\"text\" class=\"\" value=\"\" id=\"server\" name=\"server\" size=\"45px\" style=\"background-color:lightgoldenrodyellow; color:black;\"/>(Example: localhost or 191.166.0.111)</td></tr>\n\
                            <tr class=\"migrate\"><td align=\"left\" class=\"migrate\"><label>Service Id : &nbsp;&nbsp;&nbsp;&nbsp;</label></td>\n\
                                <td align=\"left\"><input type=\"text\" class=\"\" value=\"\" id=\"serviceId\" name=\"serviceId\" size=\"45px\" style=\"background-color:lightgoldenrodyellow; color:black;\"/></td></tr>\n\
                            <tr class=\"migrate\"><td align=\"left\" class=\"migrate\"><label>Service Name : &nbsp;&nbsp;&nbsp;&nbsp;</label></td>\n\
                                <td align=\"left\"><input type=\"text\" class=\"\" value=\"\" id=\"serviceName\" name=\"serviceName\" size=\"45px\" style=\"background-color:lightgoldenrodyellow; color:black;\"/></td></tr>\n\
                            <tr class=\"migrate\"><td align=\"left\" class=\"migrate\"><label style=\"color:Red\">*</label><label>Port Number : &nbsp;&nbsp;&nbsp;&nbsp;</label></td>\n\
                                <td align=\"left\"><input type=\"text\" class=\"\" value=\"\" id=\"port\" name=\"port\" size=\"45px\" style=\"background-color:lightgoldenrodyellow; color:black;\"/>(Example: 1521)</td></tr>\n\
                            <tr class=\"migrate\"><td align=\"left\" class=\"migrate\"><label style=\"color:Red\">*</label><label>Source Time Zone : &nbsp;&nbsp;&nbsp;&nbsp;</label></td>\n\
                                <td><select name=\"SourceTimeZone\" id=\"SourceTimeZone\">\n\
                                        <option value = 'GMT'>GMT</option>\n\
                                        <option value = 'ADE'>Australia/Adelaide</option>\n\
                                        <option value = 'SYD'>Australia/Sydney</option>\n\
                                    </select></td></tr>\n\
                            <tr class=\"migrate\"><td align=\"left\" class=\"migrate\" ><label style=\"color:Red\">*</label><label>Target Time Zone : &nbsp;&nbsp;&nbsp;&nbsp;</label></td>\n\
                                <td><select name=\"TargetTimeZone\" id=\"TargetTimeZone\">\n\
                                        <option value = 'Australia/Adelaide'>Australia/Adelaide</option>\n\
                                        <option value = 'GMT'>GMT</option>\n\
                                        <option value = 'Australia/Sydney'>Australia/Sydney</option>\n\
                                    </select></td></tr>\n\
                            <tr class=\"migrate\"><td align=\"left\" class=\"migrate\"><label>DSN Name : &nbsp;&nbsp;&nbsp;&nbsp;</label></td>\n\
                                <td align=\"left\"><input type=\"text\" class=\"\" value=\"\" id=\"dsnName\" name=\"dsnName\" size=\"45px\" style=\"background-color:lightgoldenrodyellow; color:black;\"/></td></tr>\n\
                            <tr class=\"migrate\"><td align=\"left\" class=\"migrate\"><label>DB Connection Type : &nbsp;&nbsp;&nbsp;&nbsp;</label></td>\n\
                                <td align=\"left\"><input type=\"text\" class=\"\" value=\"\" id=\"dbConnType\" name=\"dbConnType\" size=\"45px\" style=\"background-color:lightgoldenrodyellow; color:black;\"/>(Example: sqlserver or oracle)</td></tr>\n\
                            <tr class=\"migrate\"><td align=\"left\" class=\"migrate\"><label style=\"color:Red\">*</label><label>DB Name : &nbsp;&nbsp;&nbsp;&nbsp;</label></td>\n\
                                <td align=\"left\"><input type=\"text\" class=\"\" value=\"\" id=\"dbName\" name=\"dbName\" size=\"45px\" style=\"background-color:lightgoldenrodyellow; color:black;\"/>(Example: ctserver or kisea)</td></tr>\n\
                            <tr class=\"migrate\"><td align=\"left\" class=\"migrate\"><label>Run Independently : &nbsp;&nbsp;&nbsp;&nbsp;</label></td>\n\
                                <td align=\"left\"><select name=\"runIndep\" id=\"runIndep\">\n\
                                        <option value = 'Y'>YES</option>\n\
                                        <option value = 'N'>NO</option>\n\
                                    </select></td></tr>\n\
                            <tr><td class=\"migrate\">&nbsp;</td>\n\
                                <td class=\"migrate\"><input type=\"hidden\" name=\"cId\" id=cId\" value=\""+companyId+"\"></td></tr>\n\
                            </table>";
                        $("#addNewConnTab").html(htmlVar)
                        $("#addNewConnDiv").dialog('open');

                        $.get("<%= request.getContextPath()%>/userLayerAction.do?userParam=getloadtypeDetails&companyId="+companyId,function(data){
                              $("#loadType").html(data)
                        });
                 });
               }
               else {
                     alert('No more Connections can be added for this company ')
               }
               });
            }

            function saveDetails(){
              var cId=document.forms.addNewConnForm.cId.value;
              var loadType=document.forms.addNewConnForm.loadType.value;
              var userName=document.forms.addNewConnForm.userName.value;
              var password=document.forms.addNewConnForm.password.value;
              var server=document.forms.addNewConnForm.server.value;
              var serviceId=document.forms.addNewConnForm.serviceId.value;
              var serviceName=document.forms.addNewConnForm.serviceName.value;
              var port=document.forms.addNewConnForm.port.value;
              var SourceTimeZone=document.forms.addNewConnForm.SourceTimeZone.value;
              var TargetTimeZone=document.forms.addNewConnForm.TargetTimeZone.value;
              var dsnName=document.forms.addNewConnForm.dsnName.value;
              var dbConnType=document.forms.addNewConnForm.dbConnType.value;
              var dbName=document.forms.addNewConnForm.dbName.value;
              var runIndep=document.forms.addNewConnForm.runIndep.value;

           if(checkUserName(userName)){
                if(checkPassword(password)){
                    if(checkServer(server)){
                        if(checkPort(port)){
                            if(checkDbName(dbName)){
                                $.post('userLayerAction.do?userParam=saveConnectionsDetails&companyId='+cId+'&loadType='+loadType+'&userName='+userName+'&password='+password+'&server='+server+'&serviceId='+serviceId+'&serviceName='+serviceName+'&port='+port+'&SourceTimeZone='+SourceTimeZone+'&TargetTimeZone='+TargetTimeZone+'&dsnName='+dsnName+'&dbConnType='+dbConnType+'&dbName='+dbName+'&runIndep='+runIndep,function(data)  {
                                if(data==1)
                                        alert('Details Saved Successfully')
                                else
                                        alert('Error ! Details Not Saved')
                                        $("#addNewConnDiv").dialog('close');
                                        addEtlConnDetails();
                                });
                            }
                        }
                    }
                }
            }
          }
          function updateDetails(){
              var tabId=document.forms.editConnForm.tabId.value;
              var loadType=document.forms.editConnForm.loadTypes.value;
              var userName=document.forms.editConnForm.userName.value;
              var password=document.forms.editConnForm.password.value;
              var server=document.forms.editConnForm.server.value;
              var serviceId=document.forms.editConnForm.serviceId.value;
              var serviceName=document.forms.editConnForm.serviceName.value;
              var port=document.forms.editConnForm.port.value;
              var dsnName=document.forms.editConnForm.dsnName.value;
              var dbConnType=document.forms.editConnForm.dbConnType.value;
              var dbName=document.forms.editConnForm.dbName.value;
              var SourceTimeZone=document.forms.editConnForm.SourceTimeZone.value;
              var TargetTimeZone=document.forms.editConnForm.TargetTimeZone.value;
              var runIndep=document.forms.editConnForm.runIndep.value;

           if(checkUserName(userName)){
                if(checkPassword(password)){
                    if(checkServer(server)){
                        if(checkPort(port)){
                            if(checkDbName(dbName)){
                                $.post('userLayerAction.do?userParam=updateConnectionsDetails&tabId='+tabId+'&loadType='+loadType+'&userName='+userName+'&password='+password+'&server='+server+'&serviceId='+serviceId+'&serviceName='+serviceName+'&port='+port+'&SourceTimeZone='+SourceTimeZone+'&TargetTimeZone='+TargetTimeZone+'&dsnName='+dsnName+'&dbConnType='+dbConnType+'&dbName='+dbName+'&runIndep='+runIndep,function(data)  {
                                if(data==1)
                                        alert('Details Updated Successfully')
                                else
                                        alert('Error ! Details Not Updated')
                                        $("#editConnDiv").dialog('close');
                                        addEtlConnDetails();
                                });
                            }
                        }
                    }
                }
            }
          }


            function checkUserName(userName){
            var name_len = userName.length;
               if (name_len == 0) {
                    alert("User Name should not be empty !");
                    return false;
               }
               else
                   return true;
            }
            function checkPassword(password){
            var password_len = password.length;
               if (password_len == 0) {
                    alert("Password should not be empty !");
                    return false;
               }
               else
                   return true;
            }
            function checkServer(server){
            var server_len = server.length;
               if (server_len == 0) {
                    alert("Server should not be empty !");
                    return false;
               }
               else
                   return true;
            }

            function checkPort(port){
                var numbers = /^[0-9]+$/;
                if(port!="" && port.match(numbers))
                {
                    return true;
                }
                else
                {
                    alert('Port number should not be empty / Port number must have numeric characters only !');
                    return false;
                }
            }
            function checkDbName(dbName){
            var dbName_len = dbName.length;
               if (dbName_len == 0) {
                    alert("DB Name should not be empty !");
                    return false;
               }
               else
                   return true;
            }
            function cancelSaveDetails(){
                $("#addNewConnDiv").dialog('close');
           }
           function cancelEditDetails(){
                $("#editConnDiv").dialog('close');
           }
           function cancelAddCompany(){
                $("#addNewCompanyDiv").dialog('close');
           }
           function cancelEditCompany(){
                $("#editCompanyDetailsDiv").dialog('close');
           }

//            function getLoadType(){
//                var companyId= $("#companySelect").val()
//                if (companyId=='0000'){
//                   $("#measureLoadTypeBody").html("")
//                }
//                else {
//                $.ajax({
//                url: 'userLayerAction.do?userParam=getloadtypeDetails&companyId='+companyId,
//                success: function(data) {
//                    var jsonVar=eval('('+data+')');
//                    var loadTypes=jsonVar.loadTypeValues
//                    var htmlVar="" ;
//
//                    var loadTypesArray = ['QuickInit','QuickIncr','CtInit','AccInit','ComInit','ComIncr'];
//                    var loadTypeDescArray = ['Initial Load For Quick Travel','Incremental Load For Quick Travel','Initial Load For CT Server','Initial Load For Accpac','Combined Initial Load for All','Combined Incremental Load For All'];
//                    htmlVar +="<table id=\"LoadValues\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">\n\
//                                <tr><td align=\"left\" class=\"migrate\" style=\"font-size: 12px;\">Choose ETL Load Type :</td></tr>//";
//                    for(var i=0;i<loadTypesArray.length;i++){
//                        var falg=0;
//                        for(var j=0;j<loadTypes.length;j++){
//                            if(loadTypesArray[i]==loadTypes[j]){
//                               falg=1;
//                            }
//                        }
//                        if(falg==1)
//                        htmlVar+="<tr><td><input type=\"radio\" name=\"checkLoad\" value=\"chk\" id=\""+loadTypesArray[i]+"\" disabled >&nbsp;"+loadTypeDescArray[i]+"&nbsp;&nbsp;( "+loadTypesArray[i]+" )</td></tr>";
//                        else
//                          htmlVar+="<tr><td><input type=\"radio\" name=\"checkLoad\" value=\"chk\" id=\""+loadTypesArray[i]+"\">&nbsp;"+loadTypeDescArray[i]+"&nbsp;&nbsp;( "+loadTypesArray[i]+" )</td></tr>";
//
//                    }
//                    htmlVar += "</table>";
//                    $("#measureLoadTypeBody").html(htmlVar)
//                }
//            });
//            }
//          }
          function assignUsers(){
                            var frameObj2=document.getElementById("userAssignmentFrame");
//                            $("#sortable").html("")
//                            $.get("<%= request.getContextPath()%>/userLayerAction.do?userParam=getAllCompanyDetails",function(data){
//                            companyId="0000";
                            var source2 = "UserSecurity.jsp?";
                            frameObj2.src=source2;
                            $("#userAssignmentDiv").dialog('open')
//                        });
          }
       </script>
       <form id="configEtlForm" name="configEtlForm" method="post" action="">
            <table align="center">
                <tr valign="top">
                    <td><input type="button" class="migrate" style="width:auto;color:black" value="<%=TranslaterHelper.getTranslatedInLocale("Create_Company", cle)%>" onclick="createCompany()"></td>
                    <td>&nbsp;&nbsp;</td>
                    <td><input type="button" class="migrate" style="width:auto;color:black" value="<%=TranslaterHelper.getTranslatedInLocale("ETL_Connection_Details", cle)%>" onclick="addEtlConnDetails()"></td>
                    <td>&nbsp;&nbsp;</td>
                    <td><input type="button" class="migrate" style="width:auto;color:black" value="<%=TranslaterHelper.getTranslatedInLocale("Company_User_Assignment", cle)%>" onclick="assignUsers()"></td>
                    <td>&nbsp;&nbsp;</td>
                    <td><input type="button" class="migrate" style="width:auto;color:black" value="<%=TranslaterHelper.getTranslatedInLocale("Show_Process_List", cle)%>" onclick="showProcessList()"></td>
                </tr>
            </table><br/>

            <div align="center" style=" width:100% ">
                <form id="configForm" name="configForm" method="post" action="">
                    <table align ="center" id="measureDetTab" class="tablesorter"  border="0" cellpadding="0" cellspacing="0">
                        <tr><td id ="measureDetTabBody">

                            </td></tr>
                    </table>
                </form>
            </div>
        </form>
        <div id="addNewCompanyDiv" title="Add New Company" style="display:none" style="height: 398px; width: 676px;">
           <form id="addNewCompanyForm" name="addNewCompanyForm" action="" method="post">
             <table id="addNewCompany" class="tablesorter"  border="1px solid" cellpadding="0" cellspacing="0" align="center">
                 <tr>
                     <td id="addNewCompanyTab" name="addNewCompanyTab">

                     </td>
                 </tr>
             </table>
                <br> <br>
               <center><input type="button" class="migrate" style="width:auto;color:black" value="Save" id="btnn" onclick="saveCompany()"/>&nbsp;&nbsp;&nbsp;&nbsp;<input type="button"  class="migrate" style="width:auto" value="Cancel" onclick="cancelAddCompany()"/></center>
           </form>
       </div>
        <div id="editCompanyDetailsDiv" title="Edit Company Details" style="display:none" style="height: 398px; width: 676px;">
           <form id="editCompanyDetailsForm" name="editCompanyDetailsForm" action="" method="post">
             <table id="editCompanyDetails" class="tablesorter"  border="1px solid" cellpadding="0" cellspacing="0" align="center">
                 <tr>
                     <td id="editCompanyDetailsTab" name="editCompanyDetailsTab">

                     </td>
                 </tr>
             </table>
                <br> <br>
               <center><input type="button" class="migrate" style="width:auto;color:black" value="Update Details" id="btnn" onclick="updateCompany()"/>&nbsp;&nbsp;&nbsp;&nbsp;<input type="button"  class="migrate" style="width:auto" value="Cancel" onclick="cancelEditCompany()"/></center>
           </form>
       </div>
       <div id="addNewConnDiv" title="Add New Connection Details" style="display:none" style="height: 398px; width: 676px;">
           <form id="addNewConnForm" name="addNewConnForm" action="" method="post">
             <table id="addNewConn" class="tablesorter"  border="1px solid" cellpadding="0" cellspacing="0" align="center">
                 <tr>
                     <td id="addNewConnTab" name="addNewConnTab">

                     </td>
                 </tr>
             </table>
                <br> <br>
               <center><input type="button" class="migrate" style="width:auto;color:black" value="Save" id="btnn" onclick="saveDetails()"/>&nbsp;&nbsp;&nbsp;&nbsp;<input type="button"  class="migrate" style="width:auto" value="Cancel" onclick="cancelSaveDetails()"/></center>
           </form>
       </div>
        <div id="editConnDiv" title="Update Connection Details" style="display:none" style="height: 398px; width: 676px;">
           <form id="editConnForm" name="editConnForm" action="" method="">
             <table id="editConnTab" class="tablesorter"  border="1px solid" cellpadding="0" cellspacing="0" align="center">
                 <tr>
                     <td id="editConnTab" name="editConnTab">

                     </td>
                 </tr>
               </table>
                 <br> <br>
               <center><input type="button" class="migrate" style="width:auto;color:black" value="Update Details" id="btnn" onclick="updateDetails()"/>&nbsp;&nbsp;&nbsp;&nbsp;<input type="button"  class="migrate" style="width:auto" value="Cancel" onclick="cancelEditDetails()"/></center>

           </form>
       </div>
<div id="userAssignmentDiv" title="User Assignment" style="display:none; height: 398px; width: 676px;">
    <iframe  id="userAssignmentFrame" NAME='userAssignmentFrame' frameborder="0" height="100%" width="100%" SRC='#'></iframe>
</div>

    </body>

</html>