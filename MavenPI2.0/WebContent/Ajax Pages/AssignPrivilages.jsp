<%-- 
    Document   : AssignPrivilages
    Created on : Nov 19, 2009, 11:18:21 AM
    Author     : Administrator
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.ArrayList;" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%          //for clearing cache
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
            boolean showExtraTabs = true;
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/lib/jquery/js/jquery-1.4.2.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/jQuery/jquery/ui/ui.core.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/ui.dialog.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/draggable/ui.draggable.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/draggable/ui.droppable.js"></script>
        <link type="text/css" href="<%=request.getContextPath()%>/jQuery/jquery/themes/base/ui.all.css" rel="stylesheet" />
        <link type="text/css" href="<%=request.getContextPath()%>/jQuery/jquery/themes/base/ui.theme.css" rel="stylesheet" />
        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/metadataButton.css" rel="stylesheet" />

        <script>
            function saveUserPriv(showExtraTabs){
                var userId;
                var chkusersobj = parent.document.forms.myForm.chkusers;
                for(var i=0;i<chkusersobj.length;i++){
                    if(chkusersobj[i].checked){
                        userId=chkusersobj[i].value;
                    }
                }

                var busVar = document.getElementById("BusCheck").checked;
                var allVar = document.getElementById("AllCheck").checked;
                var adminVar = document.getElementById("AdminCheck").checked;
                var dsVar="";
                if(showExtraTabs){
                 dsVar = document.getElementById("DsCheck").checked;
                }
                var rsVar = document.getElementById("RsCheck").checked;
                var qsVar = document.getElementById("QsCheck").checked;
                 var msgVar = "";
                var alrtVar =  "";
                var prtlVar = "";
                var dataCorrectVar = "";
                var admincopyVar =  "";

                var scenarioVar = "";
                var targetVar = "";

                 if(showExtraTabs){
                msgVar = document.getElementById("MsgCheck").checked;
                 alrtVar = document.getElementById("AlrtCheck").checked;
                prtlVar = document.getElementById("PrtlCheck").checked;
                 dataCorrectVar = document.getElementById("DataCorrectionCheck").checked;
                 admincopyVar = document.getElementById("AdminCheckCopy").checked;

                 scenarioVar = document.getElementById("ScenarioCheck").checked;
                 targetVar = document.getElementById("TargetCheck").checked;
                 }
                 var buscheck="";
                 var allcheck = "";
                 var admincheck = "";
                   var dscheck="";
                   var rscheck ="";
                   var qscheck="";
                   var msgcheck="";
                   var alrtcheck ="";
                   var prtlcheck="";
                   var dataCorrectCheck="";
                   var adminCheckcopy="";
                    var scenarioCheck="";
                     var targetCheck="";
                if(busVar == true)
                {
                  buscheck = document.getElementById("BusCheck").value; //= document.getElementById("BusCheck").value;
                }
                if(allVar == true)
                {
                     allcheck =   document.getElementById("AllCheck").value; //= document.getElementById("AllCheck").value;
                }
                if(adminVar == true)
                {
                     admincheck = document.getElementById("AdminCheck").value; //= document.getElementById("AdminCheck").value;
                }
                 if(showExtraTabs){
                if(dsVar == true)
                {
                    dscheck = document.getElementById("DsCheck").value; //= document.getElementById("DsCheck").value;
                }
                 }
                if(rsVar == true)
                {
                    rscheck = document.getElementById("RsCheck").value; //= document.getElementById("RsCheck").value;
                }
                if(qsVar == true)
                {
                     qscheck = document.getElementById("QsCheck").value; //= document.getElementById("QsCheck").value;
                }
                 if(showExtraTabs){
                if(msgVar == true)
                {
                     msgcheck =  document.getElementById("MsgCheck").value; //= document.getElementById("MsgCheck").value;
                }
                if(alrtVar == true)
                {
                     alrtcheck =  document.getElementById("AlrtCheck").value; //= document.getElementById("AlrtCheck").value;
                }
                if(prtlVar == true)
                {
                     prtlcheck = document.getElementById("PrtlCheck").value; //= document.getElementById("PrtlCheck").value;
                }
                if(dataCorrectVar == true)
                {
                    dataCorrectCheck = document.getElementById("DataCorrectionCheck").value; //= document.getElementById("DataCorrectionCheck").value;
                }
                if(admincopyVar == true)
                {
                    adminCheckcopy = document.getElementById("AdminCheckCopy").value; //= document.getElementById("AdminCheckCopy").value;
                }
                if(scenarioVar == true)
                {
                    scenarioCheck = document.getElementById("ScenarioCheck").value; //= document.getElementById("DataCorrectionCheck").value;
                }
                if(targetVar == true)
                {
                     targetCheck = document.getElementById("TargetCheck").value; //= document.getElementById("DataCorrectionCheck").value;
                }
                 }
                  if(showExtraTabs){
                if(busVar == false && allVar == false && adminVar == false && dsVar == false && rsVar == false && qsVar == false && msgVar ==false && alrtVar == false && prtlVar==false && admincopyVar==false && dataCorrectVar==false && targetVar==false &&scenarioVar==false)
                {
                    alert('Please Select At least one')
                }else{
                    document.forms.userPriForm.action="savePrivilages.do?method=save&userId="+userId+"&buscheck="+buscheck+"&allcheck="+allcheck+"&admincheck="+admincheck+"&dscheck="+dscheck+"&rscheck="+rscheck+"&qscheck="+qscheck+"&msgcheck="+msgcheck+"&alrtcheck="+alrtcheck+"&prtlcheck="+prtlcheck+"&adminCheckcopy="+adminCheckcopy+"&dataCorrectCheck="+dataCorrectCheck+"&scenarioCheck="+scenarioCheck+"&targetCheck="+targetCheck;
                    document.forms.userPriForm.submit();
                    parent.cancelPriv();
                }
                  }else{
                     if(busVar == false && allVar == false && adminVar == false  && rsVar == false && qsVar == false )
                {
                    alert('Please Select At least one')
                }else{
                    document.forms.userPriForm.action="savePrivilages.do?method=save&userId="+userId+"&buscheck="+buscheck+"&allcheck="+allcheck+"&admincheck="+admincheck+"&rscheck="+rscheck+"&qscheck="+qscheck;
                   // alert(document.forms.userPriForm.action)
                    document.forms.userPriForm.submit();
                    parent.cancelPriv();
            }

                  }
            }
        </script>
        <style>
            *{
                font:11px verdana
            }
        </style>
    </head>
    <body>
        <form name="userPriForm" action="" method="post">
            <%  ArrayList UserPrevileges = new ArrayList();
            UserPrevileges = (ArrayList) session.getAttribute("UserPrevileges");
            //////////////////////////////.println.println("UserPrevileges is " + UserPrevileges);
            %>
            <table width="100%" border="0px" >
                <tr style="width:100%">
                    <td width="50%">
                        <%
            if ((UserPrevileges != null) && UserPrevileges.contains("Business Roles")) {%>
                        <input type="checkbox" id="BusCheck" checked value="Business Roles">&nbsp<b style="font-family:Verdana;font-size:12px;color:#369">Business Roles</b>
                        <%} else {%>       <input type="checkbox" id="BusCheck" value="Business Roles">&nbsp<b style="font-family:Verdana;font-size:12px;color:#369">Business Roles</b>
                        <%}%>
                    </td>
                <input type="hidden" name="buscheck" id="buscheck">
                <td width="50%">
                    <%
            if ((UserPrevileges != null) && UserPrevileges.contains("All Reports")) {%>
                    <input type="checkbox" id="AllCheck" value="All Reports" checked>&nbsp<b style="font-family:Verdana;font-size:12px;color:#369">All Reports</b>
                    <%} else {%>
                    <input type="checkbox" id="AllCheck" value="All Reports">&nbsp<b style="font-family:Verdana;font-size:12px;color:#369">All Reports</b>
                    <%  }%>
                </td>
                <input type="hidden" name="allcheck" id="allcheck">
                </tr>
                <tr style="width:100%">
                    <td width="50%">
                        <%
            if ((UserPrevileges != null) && UserPrevileges.contains("Administrator")) {%>
                        <input type="checkbox" id="AdminCheck" value="Administrator" checked>&nbsp<b style="font-family:Verdana;font-size:12px;color:#369">Administrator</b>
                        <% } else {%>
                        <input type="checkbox" id="AdminCheck" value="Administrator">&nbsp<b style="font-family:Verdana;font-size:12px;color:#369">Administrator</b>
                        <%   }%>
                    </td>
                <input type="hidden" name="admincheck" id="admincheck">
                <%if(showExtraTabs){%>
                <td width="50%">
                    <%
            if ((UserPrevileges != null) && UserPrevileges.contains("Dashboard Studio")) {%>
                    <input type="checkbox" id="DsCheck" value="Dashboard Studio" checked>&nbsp<b style="font-family:Verdana;font-size:12px;color:#369">Dashboard Studio</b>
                    <% } else {%>
                    <input type="checkbox" id="DsCheck" value="Dashboard Studio">&nbsp<b style="font-family:Verdana;font-size:12px;color:#369">Dashboard Studio</b>
                    <% }%>
                </td>
                <input type="hidden" name="dscheck" id="dscheck">
                <%}%>
                </tr>
                <tr style="width:100%">
                    <td width="50%">
                        <%
            if ((UserPrevileges != null) && UserPrevileges.contains("Report Studio")) {%>
                        <input type="checkbox" id="RsCheck" value="Report Studio" checked>&nbsp<b style="font-family:Verdana;font-size:12px;color:#369">Report Studio</b>
                        <%} else {%>
                        <input type="checkbox" id="RsCheck" value="Report Studio">&nbsp<b style="font-family:Verdana;font-size:12px;color:#369">Report Studio</b>
                        <%}%>
                    </td>
                <input type="hidden" name="rscheck" id="rscheck">
                <td width="50%">
                    <%if ((UserPrevileges != null) && UserPrevileges.contains("Query Studio")) {%>
                    <input type="checkbox" id="QsCheck" value="Query Studio" checked>&nbsp<b style="font-family:Verdana;font-size:12px;color:#369">Query Studio</b>
                    <% } else {%>
                    <input type="checkbox" id="QsCheck" value="Query Studio">&nbsp<b style="font-family:Verdana;font-size:12px;color:#369">Query Studio</b>
                    <%}%>
                    <input type="hidden" name="qscheck" id="qscheck">
                    </tr>
                    <%if(showExtraTabs){%>
                <tr style="width:100%">
                    <td width="50%">
                        <%if ((UserPrevileges != null) && UserPrevileges.contains("Messages")) {%>
                        <input type="checkbox" id="MsgCheck" value="Messages" checked>&nbsp<b style="font-family:Verdana;font-size:12px;color:#369">Messages</b>
                        <%} else {%>
                        <input type="checkbox" id="MsgCheck" value="Messages">&nbsp<b style="font-family:Verdana;font-size:12px;color:#369">Messages</b>
                        <% }%>
                    </td>
                <input type="hidden" name="msgcheck" id="msgcheck">
                <td width="50%">
                    <%
            if ((UserPrevileges != null) && UserPrevileges.contains("Alerts")) {%>
                    <input type="checkbox" id="AlrtCheck" value="Alerts" checked>&nbsp<b style="font-family:Verdana;font-size:12px;color:#369">Alerts</b>
                    <%} else {%>
                    <input type="checkbox" id="AlrtCheck" value="Alerts">&nbsp<b style="font-family:Verdana;font-size:12px;color:#369">Alerts</b>
                    <%}%>
                </td>
                <input type="hidden" name="alrtcheck" id="alrtcheck">
                <input type="hidden" name="userId" id="userId">
                </tr>
                <tr style="width:100%">
                    <td width="50%">
                        <% if ((UserPrevileges != null) && UserPrevileges.contains("Portals")) {%>
                        <input type="checkbox" id="PrtlCheck" name="PrtlCheck" value="Portals" checked>&nbsp<b style="font-family:Verdana;font-size:12px;color:#369">Portals</b>
                        <%} else {%>
                        <input type="checkbox" id="PrtlCheck" name="PrtlCheck" value="Portals">&nbsp<b style="font-family:Verdana;font-size:12px;color:#369">Portals</b>
                        <%}%>
                    </td>
                <input type="hidden" name="prtlcheck" id="prtlcheck">
                <td width="50%">
                    <% if ((UserPrevileges != null) && UserPrevileges.contains("Admin")) {%>
                    <input type="checkbox" id="AdminCheckCopy" name="AdminCheckCopy" value="Admin" checked>&nbsp<b style="font-family:Verdana;font-size:12px;color:#369">Admin</b>
                    <%} else {%>
                    <input type="checkbox" id="AdminCheckCopy" name="AdminCheckCopy" value="Admin">&nbsp<b style="font-family:Verdana;font-size:12px;color:#369">Admin</b>
                    <%}%>
                </td>
                <input type="hidden" name="adminCheckcopy" id="adminCheckcopy">
                </tr>
                <tr style="width:100%">
                    <td width="50%">
                        <% if ((UserPrevileges != null) && UserPrevileges.contains("DataCorrection")) {%>
                        <input type="checkbox" id="DataCorrectionCheck" name="DataCorrectionCheck" value="DataCorrection" checked>&nbsp<b style="font-family:Verdana;font-size:12px;color:#369">Data Correction</b>
                        <%} else {%>
                        <input type="checkbox" id="DataCorrectionCheck" name="DataCorrectionCheck" value="DataCorrection">&nbsp<b style="font-family:Verdana;font-size:12px;color:#369">Data Correction</b>
                        <%}%>
                    </td>
                <input type="hidden" name="datacorrection" id="datacorrection">
                 <td width="50%">
                        <% if ((UserPrevileges != null) && UserPrevileges.contains("Target")) {%>
                        <input type="checkbox" id="TargetCheck" name="TargetCheck" value="Target" checked>&nbsp<b style="font-family:Verdana;font-size:12px;color:#369">Targets</b>
                        <%} else {%>
                        <input type="checkbox" id="TargetCheck" name="TargetCheck" value="Target">&nbsp<b style="font-family:Verdana;font-size:12px;color:#369">Targets</b>
                        <%}%>
                    </td>
                <input type="hidden" name="Target" id="Target">

                </tr>
                 <tr style="width:100%">
                    <td width="50%">
                        <% if ((UserPrevileges != null) && UserPrevileges.contains("Scenario")) {%>
                        <input type="checkbox" id="ScenarioCheck" name="ScenarioCheck" value="Scenario" checked>&nbsp<b style="font-family:Verdana;font-size:12px;color:#369">Scenario</b>
                        <%} else {%>
                        <input type="checkbox" id="ScenarioCheck" name="ScenarioCheck" value="Scenario">&nbsp<b style="font-family:Verdana;font-size:12px;color:#369">Scenario</b>
                        <%}%>
                    </td>

                <input type="hidden" name="Scenario" id="Scenario">
                </tr>
                
                <!-- added by uday on 22-mar-2010 -->
                <tr style="width:100%">
                    <td width="50%">
                        <% if ((UserPrevileges != null) && UserPrevileges.contains("WhatIf")) {%>
                        <input type="checkbox" id="WhatIfCheck" name="WhatIfCheck" value="WhatIf" checked>&nbsp<b style="font-family:Verdana;font-size:12px;color:#369">What-If Analysis</b>
                        <%} else {%>
                        <input type="checkbox" id="WhatIfCheck" name="WhatIfCheck" value="WhatIf">&nbsp<b style="font-family:Verdana;font-size:12px;color:#369">What-If Analysis</b>
                        <%}%>
                    </td>

                <input type="hidden" name="WhatIf" id="WhatIf">
                </tr>
                <%}%>
                <tr style="width:100%" align="center" >
                <table align="center"  width="100%">
                    <tr>
                        <td width="50%" align="center">
                            <center>
                                <input type="button" class="navtitle-hover" value="Save" onclick="saveUserPriv()">
                            </center>
                        </td>
                    </tr>
                </table>
                </tr>
            </table>
        </form>
    </body>
</html>
