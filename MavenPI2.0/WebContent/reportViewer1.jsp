<%--
    Document   : reportViewer
    Created on : Sep 12, 2009, 1:32:44 PM
    Author     : santhosh.kumar@progenbusiness.com
--%>

<%@page pageEncoding="UTF-8"%>
<%@page contentType="text/html"%>
<%@page import="java.awt.Font" %>
<%@page import="utils.db.*" %>
<%@ page  import="java.awt.*" %>
<%@ page  import="java.io.*" %>
<%@ page import="java.util.*" %>
<%@page import="prg.db.Container"%>
<%@include file="pbStickyNote.jsp" %>

<%
        String ParamSectionDisplay = "";
        String currentURL = "";
        String PbReportId = "";
        HashMap map = new HashMap();
        Container container = null;


        if (request.getSession(false) != null && request.getSession(false).getAttribute("PROGENTABLES") != null) {

            if (request.getAttribute("ParamSectionDisplay") != null) {
                ParamSectionDisplay = String.valueOf(request.getAttribute("ParamSectionDisplay"));
            // //////////////////////////////////////////////////////////////////////////////////////////////.println.println("ParamSectionDisplay in jsp is:: :" + ParamSectionDisplay);
            }
            PbReportId = (String) request.getAttribute("REPORTID");

            if (request.getAttribute("currentURL") != null) {
                currentURL = String.valueOf(request.getAttribute("currentURL"));
            }
            if (request.getSession(false).getAttribute("PROGENTABLES") != null) {
                map = (HashMap) request.getSession(false).getAttribute("PROGENTABLES");
            }
            if (map.get(PbReportId) != null) {
                container = (prg.db.Container) map.get(PbReportId);
            } else {
                container = new prg.db.Container();
            }

%>
<html>
    <head>
        <link type="text/css" href="<%=request.getContextPath()%>/jQuery/jquery/themes/base/ui.all.css" rel="stylesheet" />
        <link type="text/css" href="<%=request.getContextPath()%>/jQuery/jquery/themes/base/ui.theme.css" rel="stylesheet" />
        <link type="text/css" href="<%=request.getContextPath()%>/jQuery/jquery/themes/base/ui.datepicker.css" rel="stylesheet" />
        <link type="text/css" href="<%=request.getContextPath()%>/jQuery/jquery/demos.css" rel="stylesheet" />
        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/pbReportViewerCSS.css" rel="stylesheet" />
        <link rel="stylesheet" href="<%=request.getContextPath()%>/stylesheets/jq.css" type="text/css" media="print, projection, screen">
        <link type="text/css" href="<%=request.getContextPath()%>/css/css.css" rel="stylesheet" />
        <link type="text/css" href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet" />
        <link rel="stylesheet" href="<%=request.getContextPath()%>/stylesheets/treeviewstyle/jquery.treeview.css" />
        <link rel="stylesheet" href="<%=request.getContextPath()%>/stylesheets/treeviewstyle/screen.css" />
        <link href="<%=request.getContextPath()%>/reportDesigner.css" type="text/css" rel="stylesheet">
        <link href="<%=request.getContextPath()%>/stylesheets/html.css" type="text/css" rel="stylesheet">
        <link href="<%=request.getContextPath()%>/stylesheets/css.css" type="text/css" rel="stylesheet">

        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/lib/jquery/js/jquery-1.4.2.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/jQuery/jquery/ui/ui.core.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/jQuery/jquery/ui/ui.datepicker.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/jQuery/jquery/external/bgiframe/jquery.bgiframe.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/jQuery/jquery/ui/ui.changedialog.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/pbReportViewerJS.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/JS/stickyNote.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/JS/myScript.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/ui.dialog.js"></script>

        <script type="text/javascript">
            $(function(){
                $('#datepicker').datepicker({
                    changeMonth: true,
                    changeYear: true,
                    showButtonPanel: true,
                    numberOfMonths: 2,
                    stepMonths: 2
                });

            });
        </script>


        <script>
            stickyNoteCount = "<%=stickyNoteCount%>";
            stickyNoteCount++;

            function logout(){
                document.forms.myForm.action="baseAction.do?param=logoutApplication";
               // alert(document.forms.myForm.action)
                document.forms.myForm.submit();
            }
            function gohome(){
                document.forms.myForm.action="baseAction.do?param=goHome";
               // alert(document.forms.myForm.action)
                document.forms.myForm.submit();
            }
            function reportParamsDrill(repId,userId){
                var path='&'+document.getElementById('reppath').value;
                path=path.replace('&',';');
                window.open('pbParameterDrill.jsp?userId='+userId+'&reportId='+repId+'&path='+path,"Parameter Drill", "scrollbars=1,width=550,height=350,address=no");
            }
            function submiturls1($ch){
                document.frmParameter.action = $ch;
                document.frmParameter.submit();
            }
            function modifyParams(columnId,checkBoxObj,tableObj){
                var trObj=tableObj.getElementsByTagName('tr');
                for(i=0;i<trObj.length;i++){
                    var tdObj=trObj[i].getElementsByTagName('td');
                    for(j=0;j<tdObj.length;j++){

                        if(tdObj[j].id==columnId){
                            if(checkBoxObj.checked)
                                tdObj[j].style.display='';
                            else
                                tdObj[j].style.display='none';
                        }
                    }
                }
            }

            function paramDisp(modifyColumnsObj){
                if(modifyColumnsObj.style.display=='none'){
                    modifyColumnsObj.style.display='';
                }
                else{
                    modifyColumnsObj.style.display='none';
                }
            }
            function addParamList(addParamsListObj,addParamsObj,reportId,path,tableId){
                if(addParamsListObj.style.display=='none'){
                    addParamsListObj.style.display='';
                }
                else{
                    addParamsListObj.style.display='none';
                    addParams(addParamsObj,reportId,path,tableId);
                }
            }
            function addParams(addParamsObj,reportId,path){
                var paramIds='';
                for(var i=0;i<addParamsObj.length;i++){
                    if( addParamsObj[i].checked){
                        paramIds=paramIds+','+addParamsObj[i].value;
                    }
                }
                if(paramIds!=''){
                    document.frmParameter.action='pbAddParams.jsp?reportId='+reportId+'&paramIds='+paramIds+'&path='+path;
                    document.frmParameter.submit();
                }
            }
            function getMessage(id,from,sub,message){

                src = "pbTakeMailAddress.jsp?from="+from+"&subject="+sub+"&message="+message+"&sample=sample";
                alert(src)
                document.getElementById('replyMessageDialog').innerHTML = "<iframe src="+src+" class=frame1></iframe>";
                document.getElementById('fade').style.display='block';
                document.getElementById('replyMessageDialog').style.display='block';
            }
            function cancelMessage(){
                document.getElementById('fade').style.display='none';
                document.getElementById('replyMessageDialog').style.display='none';
            }

            function cancelFavLinks(){
                $('#favLinksDialog').dialog('close');
                document.getElementById('favFrame').src = document.getElementById('favFrame').src;
            }

            function cancelPerLinks(){
                $('#snapShotDialog').dialog('close');
            }

            function showPersonalisedReports(){
                document.getElementById('replyMessageDialog').innerHTML = "<iframe src=pbPersonalisedReportList.jsp width=700 height=330></iframe>";
                document.getElementById('fade').style.display='block';
                document.getElementById('replyMessageDialog').style.display='block';
            }
            function showAllMessages(){
                document.getElementById('replyMessageDialog').innerHTML = "<iframe src=showAllMessages.jsp width=700 height=330></iframe>";
                document.getElementById('fade').style.display='block';
                document.getElementById('replyMessageDialog').style.display='block';
            }
            function displayfavlink(){
                $("#favlinkcont").toggle(500);
            }
            function displayWidgets(){
                $("#Widgets").toggle(500);
            }
            function dispParameters(){
                $("#tabParameters").toggle(500);
            }
            function dispGraphs(){
                $("#tabGraphs").toggle(500);
            }
            function dispTables(){
                $("#tabTable").toggle(500);
            }
            function dispMessages(){
                $("#messages").toggle(500);
            }
            function dispSnapShots(){
                $("#snapshots").toggle(500);
            }
            function viewReport(path){
                document.forms.frmParameter.action=path;
                document.forms.frmParameter.submit();
            }

            function createCustomizeReport(){
                document.getElementById('custreportdiv').style.display='block';
                document.getElementById('fade').style.display='block';

                document.getElementById('reportDesc').value = "";
                document.getElementById('reportName').value="";
            }

            function cancelCustomizeReport(){
                document.getElementById('duplicate').innerHTML = '';
                document.getElementById('save').disabled = false;
                document.getElementById('custreportdiv').style.display='none';
                document.getElementById('fade').style.display='none';

            }
            function tabmsg1(){
                document.getElementById('reportDesc').value = document.getElementById('reportName').value;
            }
            function saveCustomizeReport(repId){
                var reportName = document.getElementById('reportName').value;
                var reportDesc = document.getElementById('reportDesc').value;

                if(reportName==''){
                    alert("Please enter Report Name");
                }
                else  if(reportDesc==''){
                    alert("Please enter Report Description")
                }
                else{
                    $.ajax({
                        url: 'reportTemplateAction.do?templateParam=checkReportName&reportName='+reportName,
                        success: function(data){
                            if(data!=""){
                                document.getElementById('duplicate').innerHTML = data;
                                document.getElementById('save').disabled = true;
                            }
                            else if(data=='')
                            {
                                //alert("reportViewer.do?reportBy=customizeReport&custRepName="+reportName+"&custRepDesc="+reportDesc+"&REPORTID="+repId)
                                document.forms.frmParameter.action = "reportViewer.do?reportBy=customizeReport&custRepName="+reportName+"&custRepDesc="+reportDesc+"&REPORTID="+repId;
                                document.forms.frmParameter.method="POST";
                                document.forms.frmParameter.submit();

                            }
                        }
                    });
                }
            }





            //end of sai code
        </script>
        <style type="text/css">
            .suggestLink { position: relative;
                background-color: #FFFFFF;
                border: 0px solid #000000;
                border-top-width: 0px;
                padding: 2px 6px 2px 6px;
                left:3px;
                min-width: 20px;
                max-width: 150px;

            }
            .suggestLinkOver { background-color: #0099CC;
                padding: 2px 6px 2px 6px; }
            #cboRegionsuggestList { position: absolute;
                background-color: #FFFFFF;
                text-align: left;
                border: 1px solid #000000;
                border-top-width: 0px;
                width: 160px; }

            .imageStyle{
                position: absolute;
                width:12px;
                height:16px;
                display:inline;
            }
            .ajaxboxstyle {
                position: absolute;
                background-color: #FFFFFF;
                text-align: left;
                border: 1px solid #000000;
                border-top-width:1px;
                min-height:40px;
                min-width:112px;
                max-width:300px;
                overflow:auto;
                overflow-x:hidden;
                max-height:100px;
                margin:0em 0.5em;

            }
            .fontsty{
                -moz-border-radius-bottomleft:4px;
                -moz-border-radius-bottomright:4px;
                -moz-border-radius-topleft:4px;
                -moz-border-radius-topright:4px;
                background-color:#bdbdbd;
            }
            .myAjaxTable {

                table-layout:fixed;
                background-color: #FFFFFF;
                text-align:left;
                border: 0px solid #000000;

                font-size:10px;
                left:4px;

                width:inherit;
                border-collapse:separate;
                border-spacing:5px;
            }

            .myAjaxTable td {
                min-width:30px;
                max-width:100px;
                text-align:left;


            }
            #wrapper { display: inline;}
            #cbostate { width: 160px; }
            #cboRegion { width: 160px; }
            .black_overlay{
                display: none;
                position: absolute;
                top: 0%;
                left: 0%;
                width: 100%;
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
                top: 25%;
                left: 25%;
                width: 25%;
                padding: 16px;
                border: 16px solid #308dbb;
                background-color: white;
                z-index:1002;
                overflow: auto;
                -moz-border-radius-bottomleft:5px;
                -moz-border-radius-bottomright:5px;
                -moz-border-radius-topleft:5px;
                -moz-border-radius-topright:5px;
            }
            .white_content1 {
                position: absolute;
                top: 50px;
                left: 25%;
                width: 700px;
                height:400px;
                padding: 16px;
                border: 16px solid silver;
                background-color: white;
                z-index:1002;
                -moz-border-radius-bottomleft:5px;
                -moz-border-radius-bottomright:5px;
                -moz-border-radius-topleft:5px;
                -moz-border-radius-topright:5px;
            }
            .white_content2 {
                position: absolute;
                top: 30%;
                left: 38%;
                width: 500px;
                height:300px;
                padding: 16px;
                border: 16px solid silver;
                background-color: white;
                z-index:1002;
            }
            .inputbox
            {
                font-size: 10px;
                background-color:#fff;

                border-right:#000000 1px outset;
                border-right:#000000 1px inset;
                border-top: white 1px inset;
                border-left: white 1px inset;
                border-bottom: #000000 1px inset;
                font-family: verdana, arial;
                -moz-border-radius-bottomleft:2px;
                -moz-border-radius-bottomright:2px;
                -moz-border-radius-topleft:2px;
                -moz-border-radius-topright:2px;
            }
            .test
            {
                width : 150px;
                height : 150px;
                border : 1px solid #ffff99;
                background-color: #ffff99;
            }
            .paramTable
            {
                width : 99%;
                height : auto;


            }
            .colorButton
            {
                width:20px;
                height:20px;
                background-color:green;
            }

            a {font-family:Verdana;cursor:pointer}
            
            .leftcol {
                clear:left;
                float:left;
                width:100%;
                background-color:#e6e6e6;
            }
            .label{
                background-color:#e6e6e6;
                background-position:8px center;
                background-repeat:no-repeat;
                border:0 solid #252525;
                clear:both;
                height:auto;
                width:auto;
                cursor:pointer;
                display:block;
                margin-bottom:0;
                margin-right:0;
                padding:0 0.3em 0.3em 22px;
                font-family:verdana;
                font-size:100.01%;
            }
        </style>
        <title>pi 1.0</title>
    </head>
    <%
    String loguserId = String.valueOf(session.getAttribute("USERID"));
    //////////////////////////////////////////////////////////////////////////////////////////////.println.println("userId--->" + loguserId);
    Connection con =  ProgenConnection.getInstance().getConnection();
    Statement st = con.createStatement();
    ResultSet rs = st.executeQuery("select * from prg_message_board where USERID=" + loguserId);


    Statement st1 = con.createStatement();
    ResultSet rs1 = st1.executeQuery("select * from prg_ar_personalized_reports where prg_user_id=" + loguserId);

    %>
    <!--onLoad="showImageOnLoad(4),getAllImageNames()"-->
    <body>
       
            <div id="light" align="center" class="white_content"><img  alt="Page is Loading" src='images/ajax.gif'></div>
            <table style="height:600px;width:100%;max-height:100%"  cellpadding="0" cellspacing="0">
                <tr style="height:40px;width:100%;max-height:100%" >
                    <td>
                        <table>
                            <tr>
                                <td valign="top" style="height:30px;width:8%;">
                                    <img width="100%" height="100%"  title="pi " src="<%=request.getContextPath()%>/images/pi_logo.gif"/>
                                </td>
                                <td valign="top" style="height:30px;" align="right">

                                </td>
                                <td valign="top" style="height:30px;width:8%;">
                                    <img width="100%" height="100%"  title="Progen Business Solutions" src="<%=request.getContextPath()%>/images/ProGen_Logo.jpg"/>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
                <tr style="height:15px;width:100%;max-height:100%">
                    <td>
                        <form name="myForm">
                        <table width="100%" class="ui-corner-all">
                            <tr>
                                <td valign="top" style="width:90%" >
                                    <font  style="color:#369;font-family:verdana;font-size:15px;font-weight:normal"  title="<%=container.getReportDesc()%>"><%=container.getReportName()%></font>
                                </td>
                                <td valign="top" style="height:10px;width:10%" align="right">
                                    <a href="javascript:void(0)" onclick="javascript:gohome()" style="font-size:10px;color:#369;font-weight:bold;text-decoration: none;font-family:Georgia"> Home </a> |
                                    <a href="#" style="font-size:10px;color:#369;font-weight:bold;text-decoration: none;font-family:Georgia"> Help </a> |
                                    <a href="javascript:void(0)" onclick="javascript:logout()" style="font-size:10px;color:#369;font-weight:bold;text-decoration: none;font-family:Georgia"> Logout </a>
                                </td>
                            </tr>
                        </table>
                        </form>
                    </td>
                </tr>
                <tr style="width:100%;height:544px;max-height:100%">
                    <td>
                        <table width="100%" class="ui-corner-all" height="100%" border="1px solid black" cellpadding="0" cellspacing="0">
                            <tr class="ui-corner-all">
                                <!-- Begin of links-->
                                <td valign="top" style="width:20%;" align="top" class="ui-corner-all">
                                    <table style="width:99%;" class="ui-corner-all">
                                        <tr>
                                            <td>
                                                <div class="navsection">
                                                    <div class="navtitle" id="displayfavlink" onclick="displayfavlink()" >&nbsp;<b>Favourite Links</b> </div>
                                                    <table id="favlinkcont" cellpadding="2"   cellspacing="2" style="display:none" class="ui-corner-all" style="width:100%">
                                                        <tbody>

                                                            <tr style="width:100%">
                                                                <td style="width:100%">
                                                                    <iframe src="<%=request.getContextPath()%>/getFavouriteReports.do" scrolling="no" id="favFrame" frameborder="0" style="width:100%"></iframe>
                                                                </td>

                                                            </tr>

                                                            <tr style="width:100%">
                                                                <!--<td style="width:50%">
                                                                <button id="Customize" class="ui-state-default ui-corner-all" style="width:122px;height:20px;color:black;">Customize</button>
                                                            </td>
                                                            <td style="width:50%">
                                                                <button id="Prioritize" class="ui-state-default ui-corner-all" style="width:122px;height:20px;color:black;">Prioritize</button>
                                                            </td>-->
                                                                <td align="center">
                                                                <button id="Customize" class="ui-state-default ui-corner-all" style="width:122px;height:20px;color:black;" >Customize</button>
                                                                <!--<button id="Prioritize" class="ui-state-default ui-corner-all" style="width:122px;height:20px;color:black;">Prioritize</button></td>-->
                                                            </tr>
                                                        </tbody>
                                                    </table>
                                                </div>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <div class="navsection">
                                                    <div class="navtitle" id="displayWidgets" onclick="displayWidgets()" >&nbsp;<b>Widgets</b></div>
                                                    <table id="Widgets" cellpadding="2"   cellspacing="2" style="display:none" align="center" class="ui-corner-all" width="99%">
                                                        <tbody align="center" style="width:99%">
                                                            <tr align="center" style="width:100%">
                                                                <td align="center" > <input type="button" value="Compose Message" class="ui-state-default ui-corner-all" style="width:100%;height:20px;color:black" id="composeMessageDiv" ></td>
                                                            </tr>
                                                            <tr align="center" style="width:100%">
                                                                <td align="center"> <input type="button" value="Customize Report" class="ui-state-default ui-corner-all" style="width:100%;height:20px;color:black" id="customizeReportDiv" onclick="createCustomizeReport()" ></td>
                                                            </tr>
                                                            <tr style="width:100%">
                                                                <td align="center"> <input type="button" value="Report Scheduler" class="ui-state-default ui-corner-all" style="width:100%;height:20px;color:black;" id="defineSchedulerDiv" onclick="defineScheduler()">
                                                            </tr>
                                                            <tr style="width:100%">
                                                                <td align="center"> <input type="button" value="Tracker" class="ui-state-default ui-corner-all" style="width:245px;height:20px;color:black" id="defineTrackerDiv" >
                                                            </tr>
                                                            <tr style="width:100%">
                                                                <td align="center"> <input type="button" value="Sticky Notes" onclick="addNew()"class="ui-state-default ui-corner-all" style="width:100%;height:20px;color:black" id="stickyId" >
                                                            </tr>
                                                        </tbody>
                                                    </table>
                                                </div>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <div class="navsection">
                                                    <div class="navtitle"  onclick="displayTopBottomRes()" >&nbsp;<b>Top 5/Bottom 5 Performance</b>
                                                    </div>
                                                </div>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <div class="navsection">
                                                    <div class="navtitle" id="snaplink" onclick="dispSnapShots()" >&nbsp;<b>Snapshot</b></div>
                                                    <table id="snapshots" cellpadding="2"   cellspacing="0" style="display:none;border-width:thin" align="center" class="ui-corner-all" width="100%" border="0">
                                                        <tr><th >Name</th></tr>
                                                        <%while (rs1.next()) {
        //String messsage = (rs.getString(4).length()<20)?rs.getString(4):rs.getString(4).substring(0,20)+"......";
%>


                                                        <tr>
                                                            
                                                            <td style="border-right:black" align="center"><a  href="javascript:void(0)" onclick='javascript:viewReport("reportViewer.do?reportBy=viewReport&REPORTID=<%=rs1.getString(3)%>")' style="text-decoration:none"><%=rs1.getString(4)%></a></td>

                                                        </tr>
                                                        <%}%>
                                                        <tr><td></td><td align="right"><a href="javascript:showPersonalisedReports()">more...</a></td></tr>

                                                        <tr><td align="center"><input type="button" value="Store Snap Shot" class="ui-state-default ui-corner-all" style="width:100%;height:20px;color:black" id="sanpShotDiv" ></td></tr>
                                                    </table>
                                                </div>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <div class="navsection">
                                                    <div class="navtitle" id="messageslink" onclick="dispMessages()" >&nbsp;<b>Messages</b></div>
                                                    <table id="messages" cellpadding="2"   cellspacing="0" style="display:none;border-width:thin" align="center" class="ui-corner-all" width="100%" border="0">
                                                        <tr><th>Message</th></tr>
                                                        <%while (rs.next()) {
                                                       String messsage = (rs.getString(4).length() < 20) ? rs.getString(4) : rs.getString(4).substring(0, 20) + "......";
                                                        %>


                                                        <tr>
                                                            <td style="border-right:black"><a href="javascript:void(0)" onclick="getMessage('<%=rs.getString(5)%>','<%=rs.getString(1)%>','<%=rs.getString(3)%>','<%=rs.getString(4)%>')" style="text-decoration:none"><%=messsage%></a></td>
                                                            
                                                        </tr>
                                                        <%}%>
                                                        <tr><td></td><td align="right"><a href="javascript:showAllMessages()">more...</a></td></tr>
                                                    </table>
                                                </div>
                                            </td>
                                        </tr>
                                    </table>

                                </td><!--Endof links-->
                           <form name="frmParameter" action="reportViewer.jsp" method="POST" >
                                <input type="hidden" name="h" id="h" value="<%=request.getContextPath()%>">
                                <input type="hidden" name="REPORTID" id="REPORTID" value="<%=PbReportId%>">

                                <!-- Begin of Parameters/Graph Region-->
                                <td valign="top" style="width:80%" class="ui-corner-all">
                                    <table style="width:99%" valign="top">
                                        <tr valign="top" >
                                            <td valign="top" width="100%">
                                                <div class="navsection"  style="height:auto;width:100%">
                                                    <div class="navtitle1" style="height:auto;width:100%"  onclick="dispParameters()" >&nbsp;<b>Parameters Region</b></div>
                                                    <table  class="paramTable" id="tabParameters"  >
                                                        <tr>
                                                            <td valign="top" align="left" width="100%">
                                                                <%=ParamSectionDisplay%>
                                                            </td>
                                                        </tr>
                                                    </table>
                                                </div>
                                            </td>
                                        </tr>
                                        <tr valign="top">
                                            <td valign="top" width="100%">
                                                <div class="navsection" >
                                                    <div class="navtitle1" style="height:auto;width:100%"  onclick="dispGraphs()" >&nbsp;<b>Graphs Region</b></div>
                                                    <table id="tabGraphs" width="100%" style="height:auto;border:0px solid #369;">
                                                        <tr style="width:100%">
                                                            <td valign="top" width="100%">
                                                                <%--<%=request.getContextPath()%>/TableDisplay/pbGraphDisplayRegion.jsp--%>
                                                                <IFRAME NAME='iframe4' ID='iframe4'  SRC='' STYLE='width:1000px;overflow:hidden;height:<%=container.getFrameHgt1()%>px'></IFRAME>
                                                            </td>
                                                        </tr>
                                                    </table>
                                                </div>
                                            </td>
                                        </tr>
                                        <tr valign="top">
                                            <td valign="top" width="100%">
                                                <div class="navsection" >
                                                    <div class="navtitle1" style="height:auto;width:100%"  onclick="dispTables()" >&nbsp;<b>Table Region</b></div>
                                                    <table id="tabTable"   style="height:auto" width="100%">
                                                        <tr>
                                                            <td valign="top" width="100%">
                                                                <jsp:include page="TableDisplay/pbTableMap.jsp">
                                                                    <jsp:param name="ReportId" value="<%=PbReportId%>"/>
                                                                    <jsp:param name="currentURL" value="<%=currentURL%>"/>
                                                                </jsp:include>
                                                            </td>
                                                        </tr>
                                                    </table>
                                                </div>
                                            </td>
                                        </tr>

                                    </table>
                                </td><!-- Endof Parameters/Graph Region-->
                                </form>
                            </tr>
                        </table>
                    </td>
                </tr>
                <tr>

                </tr>
            </table>
            <iframe id="msgframe" height="380px" width="660px"src="pbTakeMailAddress1.jsp" ></iframe>
            <iframe id="Scheduler" height="380px" width="660px" src="ReportScheduler/pbreportScheduler.jsp"   ></iframe>
            <iframe id="tracker" height="380px" width="660px" src="#"></iframe>
            <iframe id="cstLinksFrame" height="380px" width="660px" src="getAllReports.do"></iframe>
            <iframe id="prtLinksFrame" height="380px" width="660px" src="#" ></iframe>


            <div id="custreportdiv" class="white_content2"  align="justify" style="height:120px;width:350px;display:none">
                <center class="leftcol" style="height:auto">
                    <br><br>
                    <div id="leftcol" class='leftcol' width="100%" align="center" >
                        <table style="width:100%" border="0" bgcolor="#e6e6e6">
                            <tr style="background-color:#e6e6e6;width:100%">
                                <td  style="width:30%;background-color:#e6e6e6"><label class="label">Report Name</label></td>
                                <td  style="width:70%;background-color:#e6e6e6">&nbsp;<input type="text" class="inputbox"  maxlength="35" name="reportName" style="width:90%" id="reportName" onkeyup="tabmsg1()" onfocus="document.getElementById('save').disabled = false;"><br><span id="duplicate" style="color:red"></span></td>
                            </tr>
                            <tr>
                                <td  style="width:30%;background-color:#e6e6e6"><label class="label">Description</label></td>
                                <td  style="width:70%;background-color:#e6e6e6"><textarea class="inputbox"  name="reportDesc" id="reportDesc" style="width:90%"></textarea></td>
                            </tr>
                        </table>
                        <table>
                            <tr>
                                <td><input type="button" style="background:#bdbdbd; border-color:#454545" class="ui-state-default ui-corner-all" value="Next" id="save" onclick="saveCustomizeReport('<%=PbReportId%>');"></td>
                                <td></td>
                                <td><input type="button" style="background:#bdbdbd; border-color:#454545"  class="ui-state-default ui-corner-all" value="Cancel" onclick="cancelCustomizeReport();"></td>
                            </tr>
                        </table>
                    </div>
                </center>
            </div>

            <div id="favLinksDialog" title="Favourite Links" >
                <iframe src="getAllReports.do" scrolling="no" STYLE='display:block' height="450" width="560px" frameborder="0" id="favFrame"></iframe>
            </div>

            <div id="composeMessageDialog" title="Compose Message">
                <iframe src="pbTakeMailAddress1.jsp"  STYLE='display:block' height="380px" width="680px" frameborder="0" scrolling="no"></iframe>
            </div>

            <div id="reportSchedulerDialog" title="Report Scheduler">
                <iframe src="ReportScheduler/pbreportScheduler.jsp" height="380px" width="660px" STYLE='display:block'></iframe>
            </div>

            <div id="replyMessageDialog" title="replymessages" class="white_content1">

            </div>

            <div id="snapShotDialog" title="Snap Shots">
                <iframe src="PersonalisedReports/JSPS/pbPersonalisedReportRegister.jsp" STYLE='display:block' height="380px" width="660px" ></iframe>
            </div>
            <br/>
            <table width="100%" class="fontsty">
                <tr class="fontsty" style="height:10px;width:100%;max-height:100%;background-color:#bdbdbd">
                    <td style="height:10px;width:100%;background-color:#bdbdbd" >
                        <center style="background-color:#bdbdbd"><font  style="color:#fff;font-family:verdana;font-size:10px;font-weight:normal" align="center">Copyright © 2009-12 <a href="http://www.progenbusiness.com" style="color:red;font-weight:bold;font-size:10px;font-family:verdana">Progen Business Solutions.</a> All Rights Reserved</font></center>
                    </td>
                </tr>
            </table>

            <div id="fade" class="black_overlay"></div>
        
    </body>
</html>
<%   } else {
            response.sendRedirect("baseAction.do?param=logoutApplication");
        }
%>



