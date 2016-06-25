<%@page contentType="text/html" pageEncoding="UTF-8" import="prg.db.PbReturnObject"%>


<html>
    <head>
        <title>pi EE</title>
        <link rel="stylesheet" href="<%=request.getContextPath()%>/stylesheets/style.css" type="text/css" media="print, projection, screen">
        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/metadataButton.css" rel="stylesheet" />


       
         <style type="text/css">
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
                filter:alpha(opacity=50);
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
            *{font:11px verdana}
        </style>
        

    </head>
    <%
            PbReturnObject list = null;
            if (request.getAttribute("allWhatIfList") != null) {
                list = (PbReturnObject) request.getAttribute("allWhatIfList");
            }

    %>
    <body id="mainBody" onload='document.getElementById("reportName").focus();'>

        <form name="reportForm"  method="post" style="width:98%">
            <table  border="0px solid" width="100%">
                <tr valign="top">
                    <td align="right">
                        <input type="button" value="Create What-If Scenario" class="navtitle-hover" style="width:auto"  onclick="javascript:createNewWhatIfScenario()">
                        <input type="button" value="Delete What-If Scenario" class="navtitle-hover" style="width:auto"  onclick="javascript:deleteWhatIfScenario()">
                    </td>
                </tr>
                <tr valign="top">
                    <td valign="top" colspan="2">
                        <table align="center" id="tablesorter" class="tablesorter" width="100%">
                            <thead>
                                <tr>
                                    <th nowrap><input type="checkbox" name="checkCtr" id="checkCtr" onclick="checkAll()">Select All</th>
                                    <th nowrap>What-If Scenario Name</th>
                                    <th nowrap>What-If Scenario Description</th>
                                </tr>
                            </thead>
                            <tbody>
                                <%int i = 0;
            if (list != null) {
                for (i = 0; i < list.getRowCount(); i++) {%>
                                <tr>
                                    <td style="width:30px"><input type="checkbox" name="RepSelect" id="RepSelect<%=(i + 1)%>" value="<%=list.getFieldValueString(i, 0)%>" onclick="unCheckTop(document.reportForm.checkCtr)"></td>
                                    <td>
                                        <a href="javascript:void(0)" onclick='javascript:viewWhatIfReport("reportViewer.do?reportBy=viewWhatIfScenario&REPORTID=<%=list.getFieldValueString(i, 0)%>")'> <%=list.getFieldValueString(i, 1)%></a>
                                    </td>

                                    <td>
                                        <%=list.getFieldValueString(i, 2)%>
                                    </td>
                                </tr>
                                <%}
            }%>
                            </tbody>
                        </table>
                        <input type="hidden" name="limitrep" id="limitrep" value="<%=i%>">
                    </td>
                </tr>
            </table>
            <br><br>

        </form>
        <div id="fade" class="black_overlay"></div>

        <div id="userFolderAssignmentDisplay">
            <iframe id="userFolderAssignmentDisp" NAME='userFolderAssignmentDisp'  STYLE='display:none;height:400px'   class="white_content" SRC='' ></iframe>
        </div>


 <script type="text/javascript">
            function createNewWhatIfScenario(){
                parent.createWhatIfScenario();
            }

            function cancelWhatIfReport(){
                document.getElementById('duplicate').innerHTML = '';
                document.getElementById('save').disabled = false;
                document.getElementById('whatIfReportMeta').style.display='none';
                document.getElementById('fade').style.display='none';
            }
            function tabmsg2(){
                document.getElementById('whatIfDesc').value = document.getElementById('whatIfName').value;
            }

            function saveWhatIfScenario(){
                var whatIfName = document.getElementById('whatIfName').value;
                var whatIfDesc = document.getElementById('whatIfDesc').value;

                if(whatIfName==''){
                    alert("Please Enter What-If Scenario Name");
                    document.getElementById('whatIfName').focus();
                }
                else  if(whatIfDesc==''){
                    alert("Please Enter Description")
                    document.getElementById('whatIfDesc').focus();
                }
                else{
                    $.ajax({
                        url: 'reportTemplateAction.do?templateParam=checkWhatIfScenarioName&whatIfScenarioName='+whatIfName,
                        success: function(data){
                            if(data!=""){
                                document.getElementById('whatIfDuplicate').innerHTML = "What-If Scenario Name already exists";
                                document.getElementById('save').disabled = true;
                                document.getElementById("whatIfName").focus();
                            }
                            else if(data==''){
                                document.forms.reportForm.action = "reportTemplateAction.do?templateParam=goToWhatIfScenarioDesigner&whatIfScenarioName="+whatIfName+"&whatIfScenarioDesc="+whatIfDesc;
                                document.forms.reportForm.method="POST";
                                document.forms.reportForm.submit();
                            }
                        }
                    });
                }
            }
            
            function viewWhatIfReport(path){
                document.forms.reportForm.action=path;
                document.forms.reportForm.submit();
            }
            function userFolderAssignment(){
                var frameObj=document.getElementById("userFolderAssignmentDisp");
                var source = "userFolderAssignment.jsp";
                frameObj.src=source;
                document.getElementById('userFolderassign').style.display='none';
                frameObj.style.display='block';
                document.getElementById('fade').style.display='block';

            }
            function cancelUsersFolders(){
                document.getElementById("userFolderAssignmentDisp").style.display='none';
                document.getElementById('fade').style.display='';
                document.getElementById('userFolderassign').style.display='';
                cancelUsersFolders();
            }
        </script>
        <script type="text/javascript">
            function selectAllWhatIfReports()
            {
                var RepSelectObj=document.getElementsByName("RepSelect");

                for(var i=0;i<RepSelectObj.length;i++){
                    if(RepSelectObj[0].checked){
                        RepSelectObj[i].checked=true;
                    }
                    else{
                        RepSelectObj[i].checked=false;
                    }
                }
            }
            function deleteWhatIfScenario()
            {
                var RepSelectObj=document.getElementsByName("RepSelect");
                var deleterepids = new Array();
                for(var i=0;i<RepSelectObj.length;i++){
                    if(RepSelectObj[i].checked){
                        deleterepids.push(RepSelectObj[i].value);
                    }
                    else{
                        RepSelectObj[i].checked=false;
                    }
                }
                if(deleterepids.length!=0){                               
                    $.ajax({
                        url: '<%=request.getContextPath()%>'+"/reportTemplateAction.do?templateParam=deleteUserWhatIfScenarios&deleterepids="+deleterepids.toString(),
                        success: function(data){                            
                            if(data=="true") {
                                document.forms.reportForm.action='<%=request.getContextPath()%>'+"/home.jsp#What-If_Analysis";
                                document.forms.reportForm.submit();                                
                            }
                        }
                    });
                }else{
                    alert("Please select What-If Scenario(s)")
                }
            }

            //added by uday on 18-feb-2010
            function checkAll()
            {
                var chkObj;
                if(document.reportForm.RepSelect!=undefined) {
                    chkObj = document.reportForm.RepSelect;                
                    if(document.reportForm.checkCtr.checked==true)
                    {
                        if(isNaN(chkObj.length))
                        {
                            chkObj.checked=true;
                        }
                        else
                        {
                            for (var i = 0; i < chkObj.length; i++) {
                                chkObj[i].checked = true ;
                            }
                        }
                    }
                    else
                    {
                        if(isNaN(chkObj.length))
                        {
                            chkObj.checked=false;
                        }
                        else
                        {
                            for (var i = 0; i < chkObj.length; i++) {
                                chkObj[i].checked = false ;
                            }
                        }
                    }
                }
            }
            function unCheckTop(ctr)
            {
                var all = document.reportForm.RepSelect.length;
                var m=0;
                var n=0;
                for(var j=0;j<all;j++)
                {
                    if(document.reportForm.RepSelect[j].checked==false)
                    {
                        m++;
                    }
                    if(document.reportForm.RepSelect[j].checked==true)
                    {
                        n++;
                    }
                }
                if(m>=1)
                {
                    ctr.checked=false;
                }
                if(n==all)
                {
                    ctr.checked=true;
                }else {
                    ctr.checked=false;
                }

            }
        </script>
    </body>
</html>