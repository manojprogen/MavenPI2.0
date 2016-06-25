<%@page import="prg.scenario.param.PbScenarioParamVals"%>
<%@page import="prg.scenario.client.PbScenarioManager"%>
<%@page import="com.progen.scenariodesigner.db.PbScenarioViewerBD"%>

<%@page import="prg.db.Session"%>
<%@page import="prg.db.PbReturnObject"%>
<%@ page import="utils.db.*"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="prg.util.TableUtils"%>

<%@page contentType="text/html" pageEncoding="windows-1252"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<% session.removeAttribute("modelName");%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
        <script src="<%=request.getContextPath()%>/javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <script src="<%=request.getContextPath()%>/javascript/treeview/jquery.cookie.js" type="text/javascript"></script>
        <script src="<%=request.getContextPath()%>/javascript/treeview/jquery.treeview.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/treeview/demo.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/draggable/ui.core.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/ui.tabs.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.tablesorter.js"></script>
        <Script language="javascript"  src="<%=request.getContextPath()%>/Scenario/JS/myScripts.js"></Script>

        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/ui.all.css" rel="stylesheet" />
        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/demos.css" rel="stylesheet" />
        <link href="<%=request.getContextPath()%>/stylesheets/StyleSheet.css" rel="stylesheet" type="text/css"/>
        <link rel="stylesheet" href="<%=request.getContextPath()%>/stylesheets/jquery.treeview.css" />
        <link rel="stylesheet" href="<%=request.getContextPath()%>/stylesheets/style.css"/> 
        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/metadataButton.css" rel="stylesheet" />
        <script type="text/javascript" src="<%=request.getContextPath()%>/jQuery/jquery/ui/ui.dialog.js"></script>

        <script>
            function closeScDialog()
            {                      
                $("#scenarioDialog").dialog('close');
                window.location.reload(true);
            }

            function assignScenatioRating(url)
            {
                parent.assignScenatioRatingParent(url);                
            }

            
            function refreshScenarioList(scnRating)
            {
                var scenarioRating = scnRating
                document.forms.ec.action = "pbScenarioList.jsp";
                document.forms.ec.submit();
                if(scenarioRating != "No")
                {
                    alert("Rating assigned successfully");
                }
                else
                {
                    alert("Rating un-assigned successfully");
                }
            }
            
            function saveScenario(){
                // alert('in savescenario')
                var scenarioName = document.getElementById('scenarioName').value;
                var scenarioDesc = document.getElementById('scenarioDesc').value;
                var path=document.getElementById('path').value;
                
                if(scenarioName==''){
                    alert("Please enter Scenario Name");
                }
                else  if(scenarioDesc==''){
                    alert("Please enter Scenario Description")
                }
                else{                   
                    $.ajax({
                        url: path+'/ScenarioTemplateAction.do?scnTemplateParam=checkScenarioName&scenarioName='+scenarioName,
                        success: function(data){
                            if(data!=""){
                                document.getElementById('duplicate').innerHTML = "Scenario Name already exists";
                                document.getElementById('save').disabled = true;
                            }
                            else if(data==''){
                                document.forms.ec.action = path+'/ScenarioTemplateAction.do?scnTemplateParam=goToScenarioDesigner&scenarioName='+scenarioName+'&scenarioDesc='+scenarioDesc;
                                document.forms.ec.method="POST";
                                document.forms.ec.submit();
                            }
                        }
                    });
                }
            }
            function createNewScenario(){
                parent.createNewScenarioParent();
            }

            function cancelScenario(){
                parent.cancelScenarioParent();
            }
            function editScenario()
            {
                var tdObj;
                var trObj;
                var status;
                var flag = false;
                var i=0;
                var scenarioId;
                var obj = document.forms.ec.chk1;
                var path=document.getElementById('path').value;
                if(isNaN(obj.length))
                {
                    if(document.forms.ec.chk1.checked)
                    {
                        tdObj = document.forms.ec.chk1.parentNode;
                        trObj = tdObj.parentNode;
                        status = trObj.getElementsByTagName("td")[8].innerHTML;
                        scenarioId = document.forms.ec.chk1.value;
                        if(status=="Saved")
                        {
                            alert("Scenario is not editable");
                        }
                        else
                        {
                            parent.editScenarioParent(scenarioId);                            
                        }
                    }
                    else
                    {
                        alert('Please select Scenario');
                    }
                }
                else
                {
                    for(var j=0;j<obj.length;j++)
                    {
                        if(document.forms.ec.chk1[j].checked==true)
                        {
                            tdObj = document.forms.ec.chk1[j].parentNode;
                            trObj = tdObj.parentNode;
                            status = trObj.getElementsByTagName("td")[8].innerHTML;
                            scenarioId = document.forms.ec.chk1[j].value;
                            if(status=="Saved")
                            {
                                flag = true;
                            }
                            i++;
                        }
                    }
                    if(i>1)
                    {
                        alert('Please select only one Scenario');
                    }
                    else if(i==0)
                    {
                        alert('Please select Scenario');
                    }
                    else
                    {
                        if(flag==true)
                        {
                            alert("Scenario is not editable");
                        }
                        else
                        {
                            parent.editScenarioParent(scenarioId);                            
                        }
                    }
                }
            }
            function deleteScenario()
            {
                var dimNames = "";
                var modelNames = "";
                var tdObj;
                var trObj;
                var scenarioNames = "";
                var scenarioIds = "";                
                var i=0;
                var obj = document.forms.ec.chk1;
                var path=document.getElementById('path').value;
                if(isNaN(obj.length))
                {
                    if(document.forms.ec.chk1.checked)
                    {
                        tdObj = document.forms.ec.chk1.parentNode;
                        trObj = tdObj.parentNode;
                        scenarioIds = document.forms.ec.chk1.value;
                        scenarioNames = trObj.getElementsByTagName("td")[1].getElementsByTagName("a")[0].innerHTML;
                        dimNames = trObj.getElementsByTagName("td")[3].innerHTML;
                        modelNames = trObj.getElementsByTagName("td")[4].innerHTML;
                        //var x=confirm("Are you sure you want to Delete '"+scenarioNames+"'");
                        var x=confirm("Are you sure you want to Delete");
                        if(x==true)
                        {                            
                            $.ajax({
                                url: path+"/ScenarioViewerAction.do?scenarioParam=deleteScenario&scenarioIds="+scenarioIds+"&dimNames="+dimNames+"&modelNames="+modelNames,
                                success: function(data){
                                    document.forms.ec.action = path+"/AdminTab.jsp#Scenarios";
                                    document.forms.ec.submit();
                                }
                            });
                        }
                    }
                    else
                    {
                        alert('Please select Scenario(s)')
                    }
                }
                else
                {                    
                    for(var j=0;j<obj.length;j++)
                    {
                        if(document.forms.ec.chk1[j].checked==true)
                        {
                            tdObj = document.forms.ec.chk1[j].parentNode;
                            trObj = tdObj.parentNode;
                            if(scenarioNames=="" && scenarioIds=="" && dimNames=="" && modelNames=="")
                            {
                                scenarioNames = trObj.getElementsByTagName("td")[1].getElementsByTagName("a")[0].innerHTML;
                                scenarioIds = document.forms.ec.chk1[j].value;
                                dimNames = trObj.getElementsByTagName("td")[3].innerHTML;
                                modelNames = trObj.getElementsByTagName("td")[4].innerHTML;
                            }
                            else
                            {
                                scenarioNames = scenarioNames+","+trObj.getElementsByTagName("td")[1].getElementsByTagName("a")[0].innerHTML;
                                scenarioIds = scenarioIds+","+document.forms.ec.chk1[j].value;
                                dimNames = dimNames+","+trObj.getElementsByTagName("td")[3].innerHTML;
                                modelNames = modelNames+","+trObj.getElementsByTagName("td")[4].innerHTML;
                            }
                            i++;                           
                        }
                    }
                    if(i==0)
                    {
                        alert('Please select Scenario(s)')
                    }
                    else
                    {
                        var y=confirm("Are you sure you want to Delete '"+scenarioNames+"'");
                        if(y==true)
                        {
                            $.ajax({
                                url: path+"/ScenarioViewerAction.do?scenarioParam=deleteScenario&scenarioIds="+scenarioIds+"&dimNames="+dimNames+"&modelNames="+modelNames,
                                success: function(data){
                                    document.forms.ec.action = path+"/AdminTab.jsp#Scenarios";
                                    document.forms.ec.submit();
                                }
                            });
                        }
                    }
                }
            }
            function goToListPage()
            {
                $("#editScenario").dialog('close');
                var path=document.getElementById('path').value;               
                document.forms.ec.action = path+"/AdminTab.jsp#Scenarios";
                document.forms.ec.submit();
            }
            

            function goToScenarioListPage()
            {
                var path=document.getElementById('path').value;
                document.forms.ec.action = path+"/AdminTab.jsp#Scenarios";
                document.forms.ec.submit();
            }
            
            
            function tabmsg1(){
                document.getElementById('scenarioDesc').value = document.getElementById('scenarioName').value;
            }
            function viewScenario(path) {
                document.forms.ec.action = path;
                document.forms.ec.submit();
            }
            function compareScenario()
            {
                var i=0;
                var tdObj;
                var trObj;
                var dimName;
                var scnName;
                var scnId;
                var modelName;
                var timeLevel;
                var scnMonths = "";

                var flag = true;
                var obj = document.forms.ec.chk1;
                if(isNaN(obj.length))
                {
                    alert('Please select atleast two scenarios');
                    return false;
                }
                else
                {
                    var dimensionArray = new Array();
                    var scnNamesArray = new Array();
                    var scnIdsArray = new Array();
                    var modelNamesArray = new Array();

                    for(var j=0;j<obj.length;j++)
                    {
                        if(document.forms.ec.chk1[j].checked==true)
                        {
                            tdObj = document.forms.ec.chk1[j].parentNode;
                            trObj = tdObj.parentNode;

                            scnId = document.forms.ec.chk1[j].value;
                            scnName = trObj.getElementsByTagName("td")[1].getElementsByTagName("a")[0].innerHTML;
                            dimName = trObj.getElementsByTagName("td")[3].innerHTML;
                            modelName = trObj.getElementsByTagName("td")[4].innerHTML;
                            timeLevel = trObj.getElementsByTagName("td")[5].innerHTML;
                            if(scnMonths=="") {
                                scnMonths = trObj.getElementsByTagName("td")[6].innerHTML;
                            }
                            if(scnMonths != "") {
                                if(scnMonths != trObj.getElementsByTagName("td")[7].innerHTML) {
                                    scnMonths = scnMonths + "," + trObj.getElementsByTagName("td")[7].innerHTML
                                }
                            }
                            
                            
                            scnIdsArray[i] = scnId;
                            scnNamesArray[i] = scnName;
                            dimensionArray[i] = dimName;
                            modelNamesArray[i] = modelName;
                           
                            if(dimName=="&nbsp;" || modelName=="&nbsp;" || dimName=="" || modelName=="")
                            {
                                flag = false;
                            }
                            i++;
                        }
                    }
                    if(i<2)
                    {
                        alert('Please select atleast two scenarios');
                        return false;
                    }
                    if(i==0)
                    {
                        alert('Please select atleast two scenarios');
                        return false;
                    }
                    if(flag == false)
                    {
                        alert("Scenario belonging to same dimension can only be compared");
                        return false;
                    }
                    if(scnNamesArray.length >=2)
                    {
                        for(var r=0;r<scnNamesArray.length-1;r++)
                        {                            
                            if(scnNamesArray[r] != scnNamesArray[r+1])
                            {
                                alert("Scenario belonging to same dimension can only be compared");
                                return false;
                            }
                        }
                    }
                    if(dimensionArray.length >=2)
                    {
                        for(var q=0;q<dimensionArray.length-1;q++)
                        {                            
                            if((dimensionArray[q] != dimensionArray[q+1]) && (scnNamesArray[q] == scnNamesArray[q+1]))
                            {
                                alert("Scenario belonging to same dimension can only be compared");
                                return false;
                            }
                        }
                    }

                    {   
                        document.forms.ec.action='<%=request.getContextPath()%>'+"/ScenarioViewerAction.do?scenarioParam=compareScenario&flag=compareScenario&scnIds="+scnIdsArray+"&scnNames="+scnNamesArray+"&modelNames="+modelNamesArray+"&dimNames="+dimensionArray+"&timeLevel="+timeLevel+"&scnMonths="+scnMonths;
                        document.forms.ec.submit();
                        return true;
                    }
                }
            }
        </script>
        <title>pi 1.0</title>
        <style type="text/css">
            .navsection {
                text-decoration: none;
                margin: 0 0 0 0;
                border: 1px solid #CDCDCD;
/*                background: url(../images/navtitlebg.gif) no-repeat top left;*/
                -moz-border-radius: 4px 4px 4px 4px;
                COLOR: #000;
                WIDTH: 100%;
            }
            .navtitle
            {
                -moz-border-radius-bottomleft:4px;
                -moz-border-radius-bottomright:4px;
                -moz-border-radius-topleft:4px;
                -moz-border-radius-topright:4px;
                FONT-SIZE: 11px;
                COLOR: #000;
                FONT-FAMILY: Verdana;
                VERTICAL-ALIGN: middle;
                HEIGHT: 20px;
                WIDTH: 100%;
                background-color:#BDBDBD;
                cursor:pointer;
            }
            .navtitle1
            {
                -moz-border-radius-bottomleft:4px;
                -moz-border-radius-bottomright:4px;
                -moz-border-radius-topleft:4px;
                -moz-border-radius-topright:4px;
                FONT-SIZE: 11px;
                COLOR: #000;
                FONT-FAMILY: Verdana;
                VERTICAL-ALIGN: middle;
                HEIGHT: 20px;
                WIDTH: 100%;
                background-color:#BDBDBD;
                cursor:pointer;
            }
            .ui-corner-all {
                -moz-border-radius-bottomleft:6px;
                -moz-border-radius-bottomright:6px;
                -moz-border-radius-topleft:6px;
                -moz-border-radius-topright:6px;
            }
            *{font:11px verdana}
            .frame1{
                border: 0px;
                width: 700px;
                height: 500px;
            }

            .fontsty{
                -moz-border-radius-bottomleft:4px;
                -moz-border-radius-bottomright:4px;
                -moz-border-radius-topleft:4px;
                -moz-border-radius-topright:4px;
                background-color:#bdbdbd;
            }
            .white_content1 {
                display: none;
                position: absolute;
                top: 10px;
                left: 25%;
                width: 50%;
                height:50%;
                padding: 16px;
                border: 10px solid silver;
                background-color: white;
                z-index:1002;


            }
            .white_content2 {
                display: none;
                position: absolute;
                top: 20%;
                left: 35%;
                width: 50%;
                height:50%;
                padding: 16px;
                border: 10px solid silver;
                background-color: white;
                z-index:1002;
            }
        </style>


        <style>
            .black_overlay{
                display: none;
                position: absolute;
                top: 0%;
                left: 0%;
                width: 200%;
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
            }
            a {font-family:verdana, "Times New Roman", Times, serif;font-size:12px;cursor:pointer}
            a:link {color:#3300CC;}
            a:visited {color: #660066;}
            a:hover {text-decoration: underline; color: #ff9900; font-weight:normal;}
            a:active {color: #ff0000;text-decoration: none}
        </style>

    </head>
    <body id="mainBody">
        <%  int rowCount = 0;
                    String isBudget = "";
                    PbScenarioParamVals scenarioParams = new PbScenarioParamVals();
                    PbScenarioManager scenarioClient = new PbScenarioManager();
                    Session scenarioSession = new Session();
                    String userId = request.getParameter("userId");
                    if (userId == null) {
                        userId = (String) session.getAttribute("userId");
                    }

                    if (userId != null) {
                        scenarioParams.setUserId(userId); //get it from User Session
                    }
                    //////////////////////////////////////
                    session.setAttribute("userId", userId);
                    scenarioSession.setObject(scenarioParams);
                    PbReturnObject scenarioList = scenarioClient.getScenarioList(scenarioSession);
                    rowCount = scenarioList.getRowCount();
                    String path = request.getContextPath();


        %>
        <form name="ec" method="post">
            <INPUT type="hidden" name="path" id="path" value="<%=path%>">            
            <center>
                <table id="tablesorter" style="width:95%" class="tablesorter">
                    <thead>
                        <tr>
                            <th></th>
                            <th style="font-weight:bold">Scenario Name</th>
                            <th style="font-weight:bold">Scenario Description</th>
                            <th style="font-weight:bold">Dimension</th>
                            <th style="font-weight:bold">Model</th>
                            <th style="font-weight:bold">Time Level</th>
                            <th style="font-weight:bold">Scenario Start Date</th>
                            <th style="font-weight:bold">Scenario End Date</th>
                            <th style="font-weight:bold">Scenario Status</th>
                            <th style="font-weight:bold">Rating</th>
                        </tr>
                    </thead>
                    <tbody>
                        <%
                                    String str = "";
                                    String scenarioId = "";
                                    String scenarioName = "";
                                    String scenarioDesc = "";
                                    String dimensionId = "";
                                    String scenarioRating = "";
                                    String scenarioModelName = "";
                                    String dimensionName = "";
                                    String scenarioViewVal = "";
                                    String scenarioTimeLevel = "";
                                    String scenarioStartMonth = "";
                                    String scenarioEndMonth = "";
                                    String scenarioStatus = "";
                                    String viewUrl = "";
                                    String nonALlCombo = "";

                                    for (int p = 0; p < scenarioList.getRowCount(); p++) {
                                        if ((p % 2) == 0) {
                                            str = "even";
                                        } else {
                                            str = "odd";
                                        }
                                        String ratingUrl = "";
                                        ratingUrl = "pbAssignScenarioRating.jsp?";
                                        if (scenarioList.getFieldValueString(p, "SCENARIO_ID") != null) {
                                            scenarioId = scenarioList.getFieldValueString(p, "SCENARIO_ID");
                                        }
                                        if (scenarioList.getFieldValueString(p, "SCENARIO_NAME") != null) {
                                            scenarioName = scenarioList.getFieldValueString(p, "SCENARIO_NAME");
                                        }
                                        if (scenarioList.getFieldValueString(p, "SCENARIO_DESC") != null) {
                                            scenarioDesc = scenarioList.getFieldValueString(p, "SCENARIO_DESC");
                                        }
                                        if (scenarioList.getFieldValueString(p, "DIMENSION_ID") != null) {
                                            dimensionId = scenarioList.getFieldValueString(p, "DIMENSION_ID");
                                        }
                                        if (scenarioList.getFieldValueString(p, "SCENARIO_RATING") != null) {
                                            scenarioRating = scenarioList.getFieldValueString(p, "SCENARIO_RATING");
                                        }
                                        if (scenarioList.getFieldValueString(p, "SCN_MODEL_NAME") != null) {
                                            scenarioModelName = scenarioList.getFieldValueString(p, "SCN_MODEL_NAME");
                                        }
                                        /*
                                        if(scenarioList.getFieldValueString(p,"DIMENSION_NAME") != null) {
                                        dimensionName = scenarioList.getFieldValueString(p,"DIMENSION_NAME");
                                        }
                                         */
                                        /*
                                        if (scenarioList.getFieldValueString(p, "VIEWVAL") != null) {
                                        scenarioViewVal = scenarioList.getFieldValueString(p, "VIEWVAL");
                                        }
                                         */
                                        if (scenarioList.getFieldValueString(p, "SCENARIO_TIME_LEVEL") != null) {
                                            scenarioTimeLevel = scenarioList.getFieldValueString(p, "SCENARIO_TIME_LEVEL");
                                        }
                                        if (scenarioList.getFieldValueString(p, "SCENARIO_START_MONTH") != null) {
                                            scenarioStartMonth = scenarioList.getFieldValueString(p, "SCENARIO_START_MONTH");
                                        }
                                        if (scenarioList.getFieldValueString(p, "SCENARIO_END_MONTH") != null) {
                                            scenarioEndMonth = scenarioList.getFieldValueString(p, "SCENARIO_END_MONTH");
                                        }
                                        if (scenarioList.getFieldValueString(p, "SCENARIO_STATUS") != null) {
                                            scenarioStatus = scenarioList.getFieldValueString(p, "SCENARIO_STATUS");
                                        }
                                        /*
                                        if (scenarioList.getFieldValueString(p, "CONTEXT_PATH") != null ||(!(scenarioList.getFieldValueString(p, "CONTEXT_PATH").equalsIgnoreCase("")))) {
                                        viewUrl = scenarioList.getFieldValueString(p, "CONTEXT_PATH");
                                        } else {
                                        viewUrl = "/ScenarioViewerAction.do?scenarioParam=viewScenario&scenarioId="+scenarioId+"&scenarioName="+scenarioName;
                                        }
                                        if(viewUrl.equalsIgnoreCase("")) {
                                        viewUrl = "/ScenarioViewerAction.do?scenarioParam=viewScenario&flag=viewScenario&scenarioId="+scenarioId+"&scenarioName="+scenarioName;
                                        }
                                         */
                                        viewUrl = "/ScenarioViewerAction.do?scenarioParam=viewScenario&flag=viewScenario&scenarioId=" + scenarioId + "&scenarioName=" + scenarioName + "&newScnrFlag=Y";

                                        if (scenarioList.getFieldValueString(p, "DIM_NAME") != null) {
                                            dimensionName = scenarioList.getFieldValueString(p, "DIM_NAME");
                                        }
                                        if (scenarioList.getFieldValueString(p, "NON_ALL_COMBO") != null) {
                                            nonALlCombo = scenarioList.getFieldValueString(p, "NON_ALL_COMBO");
                                        }


                                        ratingUrl = ratingUrl + "scenarioId=" + scenarioId + "&modelName=" + scenarioModelName.replace(" ", "=") + "&scnRating=" + scenarioRating + "&dimId=" + dimensionId;
                                        viewUrl = request.getContextPath() + viewUrl;
                                        nonALlCombo = nonALlCombo.replaceAll(";", "&");
                                        viewUrl = viewUrl + nonALlCombo;
                                        // ratingUrl = "Scenario/JSPs/pbAssignScenarioRating.jsp?";
                                        ratingUrl = "scenarioId=" + scenarioList.getFieldValueString(p, "SCENARIO_ID") + "&modelName=" + scenarioList.getFieldValueString(p, "SCN_MODEL_NAME").replace(" ", "~") + "&scnRating=" + scenarioList.getFieldValueString(p, "SCENARIO_RATING") + "&dimId=" + scenarioList.getFieldValueString(p, "DIMENSION_ID");

                        %>
                        <tr CLASS="<%=str%>">
                            <td><Input type="checkbox" name="chk1" VALUE="<%=scenarioId%>"></td>
                            <td>                                
                                <a style="color:#3D3D3D;font-size:11px;font-family:arial;" href="javascript:void(0)" onclick='javascript:viewScenario("<%=viewUrl%>")'><%=scenarioName%></a>
                            </td>
                            <td><%=scenarioDesc%></td>
                            <td><%=dimensionName%></td>
                            <td><%=scenarioModelName%></td>
                            <td><%=scenarioTimeLevel%></td>
                            <td><%=scenarioStartMonth%></td>
                            <td><%=scenarioEndMonth%></td>
                            <td><%=scenarioStatus%></td>
                            <td>
                                <%
                                                if (scenarioRating.equalsIgnoreCase("Assign Rating")) {
                                %>
                                <a href='javascript:assignScenatioRating("<%=ratingUrl%>")' style="color:black;font-size:11px;font-family:arial;color:#3D3D3D;"><%=scenarioRating%></a>
                                <%
                                                                } else {
                                %>
                                <a href='javascript:assignScenatioRating("<%=ratingUrl%>")' style="color:black;font-size:15px;font-family:arial;color:#3D3D3D;"><%=scenarioRating%></a>
                                <%
                                                }
                                %>
                            </td>
                        </tr>
                        <%}%>
                    </tbody>
                </table>
                <Br>
                <%
                            if (rowCount == 0) {
                %>
                <table>
                    <tr>
                        <td>
                        <Td><Input class="navtitle-hover" type="button" value="Create" onclick="javascript:createNewScenario();"></Td>
                        </td>
                    </tr>
                </table>
                <%                    } else {
                %>
                <table>
                    <tr>
                        <%--<Td><Input class="btn" type="button" value="Home" onclick="javascript:goToUserHome();"></Td>--%>
                        <td><Input class="navtitle-hover" type="button" value="Create" onclick="javascript:createNewScenario();"></td>
                        <td><Input class="navtitle-hover" type="button" value="Edit" onclick="javascript:editScenario();"></td>
                        <td><Input class="navtitle-hover" type="button" value="Delete" onclick="javascript:deleteScenario();"></td>
                        <td><Input class="navtitle-hover" type="button" value="Compare" onclick="return compareScenario();"></td>
                        <td><Input class="navtitle-hover" type="button" value="Analyze" onclick="javascript:analyzeScenario();"></td>
                    </tr>
                    <input type="hidden" name="targetNames" id="targetNames">
                </table>
                <%            }
                %>
            </center>

        </form>
    </body>
</html>
