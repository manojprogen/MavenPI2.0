<%--
    Document   : pbDashboardViewer
    Created on : Oct 10, 2009, 6:50:35 PM
    Author     : mahesh.sanampudi@progenbusiness.com
--%>

<%@page pageEncoding="UTF-8"%>
<%@page contentType="text/html"%>
<%@page import="java.awt.Font" %>
<%@page import="utils.db.*" %>
<%@ page  import="java.awt.*" %>
<%@ page  import="java.io.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.lang.*" %>
<%@page import="prg.db.Container"%>
<%@page import="prg.db.PbReturnObject"%>
<%@page import="com.progen.reportdesigner.db.DashboardTemplateDAO"%>
<%@include file="pbStickyNote.jsp" %>
<%@page import="prg.db.PbDb" %>
<%@page import="prg.db.Session"%>
<%@page import="java.sql.*"%>
<jsp:useBean id="brdcrmb" class="com.progen.action.BreadCurmbBean" scope="session"/>


<%      //for clearing cache
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
            String ParamSectionDisplay = "";
            ArrayList GraphSectionDisplay = new ArrayList();
            ArrayList GraphTitles = new ArrayList();
            String kpiDisplay = "";
            String currentURL = "";
            String PbReportId = "";
            String PbUserId = "";
            String dashboardName = "";
            HashMap map = new HashMap();
            Container container = null;
            int findiv = 1;
            int tottd = 0;
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("request.getSession(false) in report viewer  is " + request.getSession(false));
            PbReportId = (String) request.getAttribute("REPORTID");
            PbUserId = (String) request.getAttribute("USERID");

            HashMap grpDataMap = (HashMap) request.getAttribute("kpiGraphData");
            HashMap kpiDataMap = (HashMap) request.getAttribute("kpiMasterData");
            HashMap grpTitleDataMap = (HashMap) request.getAttribute("kpiGraphTitleData");
////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("kpiGraphData in jsp is:: "+grpDataMap);
////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("kpiMasterData in jsp is:: "+kpiDataMap);
////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("grpTitleDataMap in jsp is:: "+grpTitleDataMap);
            DashboardTemplateDAO dbrdDAO = new DashboardTemplateDAO();
            PbReturnObject kpiObj = dbrdDAO.getDbrdViewOrder(PbReportId);

            if (request.getAttribute("ParamSectionDisplay") != null) {
                ParamSectionDisplay = String.valueOf(request.getAttribute("ParamSectionDisplay"));
                session.setAttribute("ParamSectionDisplay", ParamSectionDisplay);
////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("in if-->" + ParamSectionDisplay);
            }
            if (request.getAttribute("kpiDisplay") != null) {
                kpiDisplay = (String) request.getAttribute("kpiDisplay");
                ////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("kpiDisplay" + kpiDisplay);
            }
            if (request.getAttribute("grpImagesList") != null) {
                GraphSectionDisplay = (ArrayList) request.getAttribute("grpImagesList");
                GraphTitles = (ArrayList) request.getAttribute("grpTitles");
            }
////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("GraphSectionDisplay is:: " + GraphSectionDisplay);
////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("GraphTitles is:: " + GraphTitles);
////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("GraphSectionDisplaylength is:: " + GraphSectionDisplay.size());
//int totalDivs=GraphSectionDisplay.size();//+kpiDisplay.size();
////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("totaldivs are:: "+totalDivs);

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

            if (container.getDbrdName() != null) {
                dashboardName = container.getDbrdName();
            }

%>
<html>
    <head>
        <link type="text/css" href="<%=request.getContextPath()%>/jQuery/jquery/themes/base/ui.all.css" rel="stylesheet" />
        <link type="text/css" href="<%=request.getContextPath()%>/jQuery/jquery/themes/base/ui.theme.css" rel="stylesheet" />
        <link type="text/css" href="<%=request.getContextPath()%>/jQuery/jquery/demos.css" rel="stylesheet" />
        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/pbReportViewerCSS.css" rel="stylesheet" />
        <link type="text/css" href="<%=request.getContextPath()%>/css/css.css" rel="stylesheet" />
        <link type="text/css" href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet" />
        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/newDashboard.css" rel="stylesheet" />
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/stylesheets/tablesorterStyle.css" />
        <link type="text/css" href="<%=request.getContextPath()%>/jQuery/jquery/themes/base/BreadCrumb.css" rel="stylesheet" />
        <link type="text/css" href="<%=request.getContextPath()%>/jQuery/jquery/themes/base/Base.css" rel="stylesheet" />

        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/lib/jquery/js/jquery-1.4.2.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/jQuery/jquery/ui/ui.core.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/jQuery/jquery/ui/ui.datepicker.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/jQuery/jquery/external/bgiframe/jquery.bgiframe.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/jQuery/jquery/ui/ui.changedialog.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/jQuery/jquery/ui/ui.resizable1.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/jQuery/jquery/ui/ui.draggable1.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/pbReportViewerJS.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/JS/stickyNote.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/JS/myScript.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.tablesorter.js"></script>

        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/dashboardDesign.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/jQuery/jquery/ui/ui.sortable.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/jQuery/jquery/ui/ui.dialog.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/jQuery/jquery/ui/jquery.cookie.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/stickyNote.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/jQuery/jquery/ui/jquery.easing.1.3.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/jQuery/jquery/ui/jquery.jBreadCrumb.1.1.js"></script>
        <link href="<%=request.getContextPath()%>/reportDesigner.css" type="text/css" rel="stylesheet">

        <script>
            stickyNoteCount = "<%=stickyNoteCount%>";
            stickyNoteCount++;
//
//            function logout(){
//                document.forms.frmParameter.action="baseAction.do?param=logoutApplication";
//                //alert(document.forms.frmParameter.action)
//                document.forms.frmParameter.submit();
//            }
//            function gohome(){
//                document.forms.frmParameter.action="baseAction.do?param=goHome";
//                document.forms.frmParameter.submit();
//            }
            $("#breadCrumb").jBreadCrumb();

        </script>

        <script type="text/javascript">
            $(function(){
                $('#datepicker').datepicker({
                    changeMonth: true,
                    changeYear: true,
                    showButtonPanel: true,
                    numberOfMonths: 1,
                    stepMonths: 1
                });

            });
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
            a {font-family:Verdana;cursor:pointer;}
            a:link {color:#369}
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
            *{
                font:11px verdana;
            }
            .startpage {
                display:none;
                position: absolute;
                top: 15%;
                left: 24%;
                width:800px;
                height:400px;
                padding: 5px;
                border: 10px solid silver;
                background-color: white;
                z-index:1002;
                -moz-border-radius-bottomleft:6px;
                -moz-border-radius-bottomright:6px;
                -moz-border-radius-topleft:6px;
                -moz-border-radius-topright:6px;
            }
            .black_start{
                display:none;
                position: absolute;
                top: 0%;
                left: 0%;
                width: 100%;
                height: 100%;
                background-color: black;
                z-index:1001;
                -moz-opacity: 0.5;
                opacity:.50;
                filter:alpha(opacity=50);
                overflow:auto;
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
            }
            .test
            {
                width : 150px;
                height : 150px;
                border : 1px solid #ffff99;
                background-color: #ffff99;
            }
            .colorButton
            {
                width:20px;
                height:20px;
                background-color:green;
            }
            .ui-widget-content {
                -moz-background-clip:border;
                -moz-background-inline-policy:continuous;
                -moz-background-origin:padding;
                background:#FFFFFF ;
                border:1px solid #A6C9E2;
                color:#222222;
            }
            .whiteBus {
                display:none;
                position: absolute;
                top: 30%;
                left: 37%;
                width: 200px;
                height:200px;
                padding: 16px;
                border: 16px solid silver;
                background-color: white;
                z-index:1002;
                -moz-border-radius-bottomleft:5px;
                -moz-border-radius-bottomright:5px;
                -moz-border-radius-topleft:5px;
                -moz-border-radius-topright:5px;
            }
            .blackBus{
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
            .fontsty{
                -moz-border-radius-bottomleft:4px;
                -moz-border-radius-bottomright:4px;
                -moz-border-radius-topleft:4px;
                -moz-border-radius-topright:4px;
                background-color:#bdbdbd;
            }
            .innerDiv{
                overflow:auto;
                height:96%;
                width:100%;
            }
            .paramRegion{
                background-color:#e6e6e6}
            }
            /* .body{background-color:#e6e6e6}*/
        </style>
        <script type="text/javascript">
            $(function() {
                $(".column").sortable({
                    connectWith: '.column'
                });

                $(".portlet").addClass("ui-widget ui-widget-content ui-helper-clearfix ui-corner-all")
                .find(".portlet-header")
                .addClass("ui-widget-header ui-corner-all")
                .end()
                .find(".portlet-content");

                $(".column").disableSelection();
            });

            $(document).ready(function() {
                $("#tablesorter")
                .tablesorter({widthFixed: true, widgets: ['zebra']})
                jQuery("#breadCrumb").jBreadCrumb();
            });

        </script>
        <script type="text/javascript">
            $(document).ready(function(){
                if ($.browser.msie == true){
                    $(".navigateDialog").dialog({
                        autoOpen: false,
                        height: 620,
                        width: 820,
                        position: 'justify',
                        modal: true
                    });
                }
                else{
                    $(".navigateDialog").dialog({
                        autoOpen: false,
                        height: 460,
                        width: 820,
                        position: 'justify',
                        modal: true
                    });
                }

            });
        </script>
        <SCRIPT LANGUAGE="JavaScript">
            function reportParamsDrill(repId,userId){
                var path='&'+document.getElementById('reppath').value;
                path=path.replace('&',';');
                window.open('pbParameterDrill.jsp?userId='+userId+'&reportId='+repId+'&path='+path,"Parameter Drill", "scrollbars=1,width=550,height=350,address=no");
            }
            function submiturls1($ch)
            {
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
            function customizeReport(reportId)
            {
                document.frmParameter.action="reportViewer.do?reportBy=editReport";
                document.frmParameter.target="_blank";
                document.frmParameter.submit();
            }
            function closeComposeDiv(){
                $('#msgframe').dialog('close');
            }
            function closesnapshot(){
                $('#snapshot').dialog('close');
            }
            function closeTracker(){
                $('#frametrack').dialog('close');
            }
            function closeScheduler(){
                $('#scheduler').dialog('close');
            }
            function displayfavlink(){
                $("#favlinkcont").toggle(500);
            }
            function displayDbrdTimeInfo(){
                $("#dbrdTimecont").toggle(500);
            }
            function cancelFavLinks(){
                $('#favLinksDialog').dialog('close');
                document.getElementById('favFrame').src = document.getElementById('favFrame').src;
            }
            function cancelPerLinks(){
                $('#snapShotDialog').dialog('close');
                //document.getElementById('favFrame').src = document.getElementById('favFrame').src;
                window.location.reload();
            }
            //added for navigate
            function viewDashboardG(path){
                document.forms.frmParameter.action=path;
                document.forms.frmParameter.submit();
            }
            function viewReportG(path){
                document.forms.frmParameter.action=path;
                document.forms.frmParameter.submit();
            }

//            function goGlobe(){
//                $(".navigateDialog").dialog('open');
//            }
//            function closeStart(){
//                $(".navigateDialog").dialog('close');
//            }
            function goPaths(path){
                parent.closeStart();
                document.forms.frmParameter.action=path;
                document.forms.frmParameter.submit();
            }
            //bus roles
            function roleCancel(){

                document.getElementById('busRoleDiv').style.display='none';
                document.getElementById('fadeBusRole').style.display='none';
                document.getElementById('rep-'+rlsdiv).innerHTML= "";
                document.getElementById('mainBody').style.overflow='auto';
            }
            function displayBusRoles(){
                $('#BusRolesTab').toggle(500);
            }
            function popDiv(){
                document.getElementById('busRoleDiv').style.display='block';
                document.getElementById('fadeBusRole').style.display='block';
                document.getElementById('mainBody').style.overflow='hidden';
            }

            function rolesajax(path,roleid)
            {
                rlsdiv = roleid;
                // alert(path);
                //   alert(roleid);
                $.ajax({
                    url: path,
                    success: function(path){
                        if(path!=""){
                            document.getElementById('rep-'+roleid).innerHTML = path;
                        }
                        else
                        {
                            document.getElementById('rep-'+roleid).innerHTML = "none";
                        }
                    }
                });
            }
            function cancelMessage1(){
                $('#composeMessageDialog').dialog('close');

            }
            function cancelSheduler()
            {   $('#reportSchedulerDialog').dialog('close');
                document.getElementById("Scheduler").style.display='none';
                document.getElementById('fade').style.display='none';
                document.getElementById('mainBody').style.overflow='auto';

            }
        </SCRIPT>
        <script type="text/javascript">
            function formatStr(EL,maxchars){
                strbuff=EL.innerHTML;
                newstr='';
                startI = 0;
                max=maxchars;
                str='';
                subarr=new Array(parseInt(strbuff.length/max+1));
                for (i=0;i<subarr.length;i++)
                {
                    subarr[i]=strbuff.substr(startI,max);
                    startI+=max;
                }
                for (i=0;i<subarr.length-1;i++)
                {
                    newstr+=subarr[i]+'<br/>';
                }
                newstr+=subarr[subarr.length-1];
                if(subarr.length==1){
                    EL.innerHTML=EL.innerHTML;
                }else{
                    EL.innerHTML=newstr;
                }
            }
        </script>
        <title>pi EE</title>
    </head>
    <% String pagename = "";
                String loguserId = String.valueOf(session.getAttribute("USERID"));
                ////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("userId--->" + loguserId);
                Connection con = ProgenConnection.getInstance().getConnection();
                Statement st = null;
                ResultSet rs = null;
                Statement st1 = null;
                ResultSet rs1 = null;
                PbReturnObject pbro = null;

                try {

                    rs = st.executeQuery("select * from prg_message_board where USERID=" + loguserId);
                    st1 = con.createStatement();
                    rs1 = st1.executeQuery("select * from prg_ar_personalized_reports where prg_user_id=" + loguserId);
                    pbro = new PbReturnObject(rs1);

                    rs.close();
                    st1.close();
                    rs1.close();
                    st.close();
                    con.close();

                    PbDb pbdb = new PbDb();
                    userId = "";
                    userId = String.valueOf(request.getSession(false).getAttribute("USERID"));
                    String AvailableFiolers = "select folder_Id,FOLDER_NAME from PRG_USER_FOLDER where folder_id in(";
                    AvailableFiolers += "select user_folder_id from prg_grp_user_folder_assignment where user_id=" + userId;
                    AvailableFiolers += " union ";
                    AvailableFiolers += "select folder_id from prg_user_folder where grp_id in(";
                    AvailableFiolers += "select grp_id from prg_grp_user_assignment where user_id=" + userId;
                    AvailableFiolers += "and grp_id not in(select grp_id from prg_grp_user_folder_assignment where user_id=" + userId + ")))";
                    // PbReturnObject folderpbro = pbdb.execSelectSQL(userFoldersql);
                    PbReturnObject folderpbro = pbdb.execSelectSQL(AvailableFiolers);
                    //String userreports = "SELECT REPORT_ID, REPORT_NAME FROM PRG_AR_REPORT_MASTER where REPORT_ID in(SELECT  REPORT_ID FROM PRG_AR_USER_REPORTS where  user_id=" + userId + ")";
                    String userreports = "SELECT A.REPORT_ID,A.REPORT_NAME FROM PRG_AR_REPORT_MASTER A,(SELECT distinct REPORT_ID FROM PRG_AR_USER_REPORTS WHERE user_id=" + userId + ") B WHERE A.REPORT_ID =b.REPORT_ID";
                    PbReturnObject reportpbro = pbdb.execSelectSQL(userreports);

                    PbReturnObject rolereppbro = null;
                    if (request.getParameter("roleId") != null) {
                        String folderId = request.getParameter("roleId");
                        ////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("folderId----" + folderId);
                        String rolerepdashs = "SELECT REPORT_ID, REPORT_NAME FROM PRG_AR_REPORT_MASTER where REPORT_ID in(SELECT  REPORT_ID FROM PRG_AR_REPORT_DETAILS where  folder_id=" + folderId + ")";
                        rolereppbro = pbdb.execSelectSQL(rolerepdashs);
                        ////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("Here it will show the row count");
                        ////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println(rolereppbro.getRowCount());
                    }

    %>

    <!--onLoad="showImageOnLoad(4),getAllImageNames()"-->
    <body id="mainBody" onload="formatStr(document.getElementById('dashName'),30);">
        <div id="light" align="center" class="white_content"><img  alt="Page is Loading" src='images/ajax.gif'></div>

        <table style="height:600px;width:100%;max-height:100%"  cellpadding="0" cellspacing="0">
            <tr style="height:40px;width:100%;max-height:100%">
                <td valign="top">
                    <table style="width:100%;">
                        <tr>
                            <td valign="top" style="width:100%;">
                                <jsp:include page="Headerfolder/headerPage.jsp"/>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
            <tr style="height:15px;width:100%;max-height:100%">
                <td>
                    <table width="100%" class="ui-corner-all">
                        <tr>
                            <td  style="height:10px;width:25%" align="left">
                                <%pagename = dashboardName;//request.getParameter("pagename");
            String pgurl = "";
            ////////////////////////////////////////////////////////////////////////////.println.println("pageurl---" + pgurl);
            if (pagename != null) {

                                %>
                                <span id="dashName" style="color: #4F4F4F;font-family:verdana;font-size:15px;font-weight:bold"  title="<%=pagename%>"><%=pagename%></span>
                                <%} else {
                //String pagename = "";
                //if (request.getSession(false) != null && request.getSession(false).getAttribute("DashboardHashMap") != null) {
                pagename = dashboardName;//(String) ((HashMap) request.getSession(false).getAttribute("DashboardHashMap")).get("DashboardName");
                //////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("dashboardName-----" + dashboardName);
                //}

                                %>
                                <span id="dashName" style="color: #4F4F4F;font-family:verdana;font-size:15px;font-weight:bold"  title="<%=pagename%>"><%=pagename%></span>
                                <%}
                if (request.getAttribute("url") != null) {
                    pgurl = pgurl + request.getAttribute("url").toString();
                    pgurl = pgurl + ";pagename=" + pagename;
                    ////////////////////////////////////////////////////////////////////////////.println.println("pageurl---" + pgurl);
                    brdcrmb.inserting(pagename, pgurl);

            }%>
                                <%-- <input type="hidden" name="pgname" id="pgname" value='<%=pagename%>'>
                                 <input type="hidden" name="pgurl" id="pgurl" value='"<%=pgurl%>"'> --%>
                            </td>
                            <td valign="top" width="50%">
                                <% String pgnam = "";%>

                                <div id='breadCrumb' class='breadCrumb module' style="width:500px">
                                    <ul>
                                        <li style="display:none;"></li>
                                        <li style="display:none;"></li>
                                        <%
                          if (brdcrmb.getPgname1() != null) {
                              pgnam = brdcrmb.getPgname1().toString();
                              //out.println("six");
                              ////////////////////////////////////////////////////////////////////////////.println.println("six");
                              if (pgnam.equalsIgnoreCase(pagename)) {
                                        %>

                                        <li style="font-family:helvetica;font-size:11px;color: #967117;font-weight:bold;">
                                            <%=brdcrmb.getPgname1()%>

                                        </li>
                                        <%
                                    } else {
                                        %>

                                        <li>
                                            <a href='<%=brdcrmb.getPgurl1()%>'><%=brdcrmb.getPgname1()%></a>
                                        </li>
                                        <%
                                 }
                             }
                             if (brdcrmb.getPgname2() != null) {
                                 pgnam = brdcrmb.getPgname2().toString();
                                 //  out.println("seven");
                                 ////////////////////////////////////////////////////////////////////////////.println.println("seven");
                                 if (pgnam.equalsIgnoreCase(pagename)) {
                                        %>
                                        <li style="font-family:helvetica;font-size:11px;color: #967117;font-weight:bold;">
                                            <%=brdcrmb.getPgname2()%>
                                        </li>
                                        <%
                                    } else {
                                        %>
                                        <li>
                                            <a href='<%=brdcrmb.getPgurl2()%>'><%=brdcrmb.getPgname2()%></a>
                                        </li>
                                        <%                    }
                             }
                             if (brdcrmb.getPgname3() != null) {
                                 //  out.println("eight");
                                 ////////////////////////////////////////////////////////////////////////////.println.println("eight");
                                 pgnam = brdcrmb.getPgname3().toString();
                                 if (pgnam.equalsIgnoreCase(pagename)) {
                                        %>
                                        <li style="font-family:helvetica;font-size:11px;color: #967117;font-weight:bold;">
                                            <%=brdcrmb.getPgname3()%>
                                        </li>
                                        <%
                                    } else {
                                        %>
                                        <li>
                                            <a href='<%=brdcrmb.getPgurl3()%>'><%=brdcrmb.getPgname3()%></a>
                                        </li>
                                        <%
                                 }
                             }
                             if (brdcrmb.getPgname4() != null) {
                                 pgnam = brdcrmb.getPgname4().toString();
                                 //   out.println("nine");
                                 ////////////////////////////////////////////////////////////////////////////.println.println("nine");
                                 if (pgnam.equalsIgnoreCase(pagename)) {
                                        %>
                                        <li style="font-family:helvetica;font-size:11px;color: #967117;font-weight:bold;">
                                            <%=brdcrmb.getPgname4()%>
                                        </li>
                                        <%
                                    } else {
                                        %>
                                        <li>
                                            <a href='<%=brdcrmb.getPgurl4()%>'><%=brdcrmb.getPgname4()%></a>
                                        </li>
                                        <%
                                 }
                             }
                             if (brdcrmb.getPgname5() != null) {
                                 pgnam = brdcrmb.getPgname5().toString();
                                 //  out.println("ten");
                                 ////////////////////////////////////////////////////////////////////////////.println.println("ten");
                                 if (pgnam.equalsIgnoreCase(pagename)) {
                                        %>
                                        <li style="font-family:helvetica;font-size:11px;color: #967117;font-weight:bold;">
                                            <%=brdcrmb.getPgname5()%>
                                        </li>
                                        <%
                                    } else {
                                        %>
                                        <li>
                                            <a href='<%=brdcrmb.getPgurl5()%>'<%=brdcrmb.getPgname5()%></a>
                                        </li>
                                        <%
                                 }
                             }
                                        %>
                                        <li style="display:none;"></li>
                                        <li style="display:none;"></li>

                                    </ul>
                                </div>

                                <div class="chevronOverlay main">
                                </div>
                            </td>
                            <td valign="top" style="height:10px;width:20%" align="right">
<!--                                <a href="javascript:void(0)" onclick="javascript:goGlobe()" style="font-size:10px;color:#2191C0;font-weight:bold;text-decoration: none;font-family:Georgia"> Navigation </a> |
                                <a href="javascript:void(0)" onclick="javascript:gohome()" style="font-size:10px;color:#2191C0;font-weight:bold;text-decoration: none;font-family:Georgia"> Home </a> |-->
<!--                                <a href="#" style="font-size:10px;color:#2191C0;font-weight:bold;text-decoration: none;font-family:Georgia"> Help </a> |-->
<!--                                <a href="javascript:void(0)" onclick="javascript:logout()" style="font-size:10px;color:#2191C0;font-weight:bold;text-decoration: none;font-family:Georgia"> Logout </a>-->
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
            <tr style="width:100%;height:544px;max-height:100%">
                <td>
                    <table width="100%" class="ui-corner-all" height="100%" border="1px solid black" cellpadding="0" cellspacing="0">
                        <tr class="ui-corner-all">
                            <!-- Begin of links-->
                            <td valign="top" style="width:16%;" align="top" class="ui-corner-all">
                                <table style="width:99%;" class="ui-corner-all">
                                    <tr>
                                        <td>
                                            <div class="navsection">
                                                <div class="navtitle" id="displayfavlink" onclick="displayfavlink()" >&nbsp;<b style="font-family:verdana">Favourite Links</b> </div>
                                                <table id="favlinkcont" cellpadding="2"   cellspacing="2" style="display:none" class="ui-corner-all" style="width:100%">
                                                    <tbody>
                                                        <tr style="width:100%">
                                                            <td style="width:100%">
                                                                <iframe src="<%=request.getContextPath()%>/getFavouriteReports.do" scrolling="no" id="favFrame" frameborder="0" style="width:100%"></iframe>
                                                            </td>
                                                        </tr>
                                                        <tr style="width:100%">
                                                            <td align="center">
                                                                <button id="Customize" class="navtitle-hover" style="width:122px;height:20px;color:black;" >Customize</button>
                                                        </tr>
                                                    </tbody>
                                                </table>
                                            </div>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <div class="navsection">
                                                <div class="navtitle" id="displayWidgets" onclick="displayWidgets()" >&nbsp;<b style="font-family:verdana">Widgets</b></div>
                                                <table id="Widgets" cellpadding="2"   cellspacing="2" style="display:none" align="center" class="ui-corner-all" width="99%">
                                                    <tbody align="center" style="width:99%">
                                                        <tr align="center" >
                                                            <td align="center" > <input type="button" value="Compose Message" class="navtitle-hover" style="width:100%;height:20px;color:black" id="composeMessageDiv" ></td>
                                                        </tr>
                                                        <tr>
                                                            <td align="center"> <input type="button" value="Report Scheduler" class="navtitle-hover" style="width:185px;height:20px;color:black;" id="defineSchedulerDiv" onclick="defineScheduler('<%=request.getParameter("REPORTID")%>','<%=container.getReportName()%>')">
                                                        </tr>
                                                        <tr>
                                                            <td align="center"> <input type="button" value="Sticky Notes" onclick="addNew()"class="navtitle-hover" style="width:100%;height:20px;color:black" id="stickyId" >
                                                        </tr>
                                                    </tbody>
                                                </table>
                                            </div>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <div class="navsection">
                                                <div class="navtitle" id="displayBusRoles" onclick="displayBusRoles()" >&nbsp;<b style="font-family:verdana">Business Roles</b></div>
                                                <div id="BusRolesTab" style="display:none;width:99%">
                                                    <!-- <table  >
                                                        <tr>
                                                            <td>
                                                                <ul id="measureTree" class="filetree treeview-famfamfam" style="color:#fff">
                                                                    <li class="open" >-->
                                                    <ul id="measures" style="color:#fff">
                                                        <%
for (int i = 0; i < folderpbro.getRowCount(); i++) {
                                                        %>
                                                        <li><img src="<%=request.getContextPath()%>/icons pinvoke/folder-horizontal.png"><a href="javascript:void(0)" onclick="rolesajax('GetrolesAction.do?param=roles&roleId=<%=folderpbro.getFieldValueString(i, 0)%>','<%=folderpbro.getFieldValueString(i, 0)%>',popDiv())"> <%=folderpbro.getFieldValueString(i, 1)%> </a>
                                                        </li>
                                                        <% }
                                                        %>
                                                    </ul>
                                                    <!--  </li>
                                                                </ul>
                                                            </td>
                                                        </tr>
                                                    </table>-->
                                                </div>
                                            </div>
                                        </td>
                                    </tr>

                                </table>
                            </td><!--Endof links-->
                        <form name="frmParameter" action="pbDashboardViewer.jsp" method="POST" >
                            <input type="hidden" name="h" id="h" value="<%=request.getContextPath()%>">
                            <input type="hidden" name="REPORTID" id="REPORTID" value="<%=PbReportId%>">
                            <input type="hidden" name="pagename" value="<%=pagename%>">
                            <td valign="top" style="width:84%" class="ui-corner-all">
                                <table style="width:100%" valign="top">
                                    <tr valign="top" style="width:99%">
                                        <td valign="top" width="99%">
                                            <center>
                                                <div class="navsection" style="width:99%">
                                                    <div class="navtitle1" onclick="dispParameters()" align="left">&nbsp;<b>Parameters Region</b></div>
                                                    <div  id="tabParameters" class="paramRegion">
                                                        <table   style="height:80px;width:100%">
                                                            <tr>
                                                                <td valign="top" style="width:100%">
                                                                    <%=ParamSectionDisplay%>
                                                                </td>
                                                            </tr>
                                                        </table>
                                                    </div>
                                                </div>
                                            </center>
                                        </td>
                                    </tr>

                                    <tr valign="top" style="width:100%">
                                    <table width="100%" cellspacing="8" cellpadding="8" align="center">
                                        <tr style="width:100%" >
                                            <%  for (int kpi = 0; kpi < kpiObj.getRowCount(); kpi++) {
                if (kpi % 2 == 0) {
                                            %>
                                        </tr><tr style="width:100%" >
                                            <% }
  if (kpiObj.getFieldValueString(kpi, "DISPLAY_TYPE").equalsIgnoreCase("KPI")) {
      //////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("display type in if is:: "+kpiObj.getFieldValueString(kpi, "DISPLAY_TYPE"));
      //////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("kpiObj.getFieldValueInt(kpi, KPI_MASTER_ID) is:: "+kpiObj.getFieldValueInt(kpi, "KPI_MASTER_ID"));
      //////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("kpiDataMap.get("+kpiObj.getFieldValueInt(kpi, "KPI_MASTER_ID")+") is:: "+kpiDataMap.get(kpiObj.getFieldValueInt(kpi, "KPI_MASTER_ID")));
%>
                                            <td width="49%" valign="top">
                                                <div  class="portlet"  style="height:300px;width:96%;overflow:auto" id="KPI">
                                                    <div style="height:3px"></div>
                                                    <center> <div class="portlet-header" align="center" style="height:20px;width:98%;background-color:#b4d9ee"> KPI</div></center>
                                                    <table width="100%" align="left" ><tr><td width="100%" valign="top" align="left">
                                                                <%=kpiDataMap.get(String.valueOf(kpiObj.getFieldValueInt(kpi, "KPI_MASTER_ID")))%>
                                                            </td>
                                                        </tr>
                                                    </table>
                                                </div>
                                            </td>
                                            <%} else {%>
                                            <td width="49%" valign="top">
                                                <div  class="portlet"  style="height:300px;width:96%;overflow:auto" id="Graph"<%=kpi%>>
                                                    <div style="height:3px"></div>
                                                    <center> <div class="portlet-header" align="center" style="height:20px;width:98%;background-color:#b4d9ee"> <%=grpTitleDataMap.get(String.valueOf(kpiObj.getFieldValueInt(kpi, "DISPLAY_SEQUENCE")))%></div></center>
                                                    <table width="100%" align="left" ><tr><td width="100%" valign="top" align="center">
                                                                <%=grpDataMap.get(String.valueOf(kpiObj.getFieldValueInt(kpi, "DISPLAY_SEQUENCE")))%>
                                                            </td>
                                                        </tr>
                                                    </table>
                                                </div>
                                            </td>
                                            <%}
            }%>



                                            <%--       <td width="50%" valign="top">
                                                   <div  class="portlet"  style="height:350px;width:100%;overflow:auto" id="Graph0">
                                                       <div class="portlet-header" align="center"><%=GraphTitles.get(0)%></div>
                                                       <table>
                                                           <tr>
                                                               <td><%=GraphSectionDisplay.get(0)%></td>
                                                           </tr>
                                                       </table>
                                                   </div>
                                                   </td>

                                                   <%
                                                   if (GraphSectionDisplay.size() % 2 == 0) {
                                                       tottd = GraphSectionDisplay.size() / 2;
                                                       //////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("tottd in 0 is:: "+tottd);
                                                   } else {
                                                       tottd = (GraphSectionDisplay.size() + 1) / 2;
                                                       //////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("tottd in else is:: "+tottd);
                                                   }
                                                   for (int divs = 0; divs < GraphSectionDisplay.size()-1; divs++) {
                                                      if(divs%2==0){

                                                %>
                                                </tr><tr style="width:100%" >
                                                <% }%>
                                                <td width="50%" valign="top">
                                                <div  class="portlet"  style="height:350px;width:100%;overflow:auto" id="Graph"<%=divs+1%>>
                                                    <div class="portlet-header" align="center"><%=GraphTitles.get(divs+1)%></div>
                                                    <table>
                                                        <tr>
                                                            <td><%=GraphSectionDisplay.get(divs+1)%></td>
                                                        </tr>
                                                    </table>
                                                </div>
                                                 </td>
                                                <% }%>

                                            <%--         <%if (GraphSectionDisplay.size() >= 4) {%>
                                            <div  class="portlet"  style="height:350px;width:100%;overflow:auto" id="Graph3">
                                                <div class="portlet-header" align="center"> <%=GraphTitles.get(3)%></div>
                                                <table>
                                                    <tr>
                                                        <td><%=GraphSectionDisplay.get(3)%></td>
                                                    </tr>
                                                </table>
                                            </div>
                                            <% }%> --%>

                                            <%--        <td width="50%" valign="Top">
                                                <%if (GraphSectionDisplay.size() >= 1) {%>
                                                <div  class="portlet"  style="height:350px;width:100%;overflow:auto" id="Graph0">
                                                    <div class="portlet-header" align="center"> <%=GraphTitles.get(0)%></div>
                                                    <table >
                                                        <tr>
                                                            <td>
                                                                <%=GraphSectionDisplay.get(0)%>
                                                            </td>
                                                        </tr>
                                                    </table>
                                                </div>
                                                <% }%>
                                                <%if (GraphSectionDisplay.size() >= 3) {%>
                                                <div  class="portlet"  style="height:350px;width:100%;overflow:auto" id="Graph2">
                                                    <div class="portlet-header" align="center"> <%=GraphTitles.get(2)%></div>
                                                    <table>
                                                        <tr>
                                                            <td><%=GraphSectionDisplay.get(2)%></td>
                                                        </tr>
                                                    </table>
                                                </div>
                                                <% }%>
                                                <%if (GraphSectionDisplay.size() >= 5) {%>
                                                <div  class="portlet"  style="height:350px;width:100%;overflow:auto" id="Graph5">
                                                    <div class="portlet-header" align="center"> <%=GraphTitles.get(4)%></div>
                                                    <table>
                                                        <tr>
                                                            <td><%=GraphSectionDisplay.get(4)%></td>
                                                        </tr>
                                                    </table>
                                                </div>
                                                <% }%>
                                            </td>   --%>
                                        </tr>
                                    </table>
                                    </tr>
                                </table>
                            </td>
                            </tr>
                    </table>
                </td>
            </tr>
        </table>
        <iframe id="msgframe" class="frame1" src="#" style="display:none"></iframe>
        <iframe id="Scheduler" class="frame1" src="#" style="display:none" ></iframe>
        <iframe id="tracker" class="frame1" src="#" style="display:none"></iframe>
        <iframe id="cstLinksFrame" height="380px" width="660px" src="getAllReports.do" style="display:none"></iframe>
        <iframe id="prtLinksFrame" height="380px" width="660px" src="#"  style="display:none"></iframe>
        <div id="favLinksDialog" title="Favourite Links" >
            <iframe src="getAllReports.do" scrolling="no" STYLE='display:block' height="450" width="560px" frameborder="0" id="favFrame"></iframe>
        </div>
        <br>
        <table width="100%" class="fontsty">
            <tr class="fontsty" style="height:10px;width:100%;max-height:100%;background-color:#bdbdbd">
                <td style="height:10px;width:100%;background-color:#bdbdbd" >
                    <center style="background-color:#bdbdbd"><font  style="color:#fff;font-family:verdana;font-size:10px;font-weight:normal" align="center">Copyright © 2009-12 <a href="http://www.progenbusiness.com" style="color:red;font-weight:bold;font-size:10px;font-family:verdana">Progen Business Solutions.</a> All Rights Reserved</font></center>
                </td>
            </tr>
        </table>
        <div id="fade" class="black_overlay"></div>
<!--        <div id="reportstart" class="navigateDialog" title="Navigation">
            <iframe src="startPage.jsp" frameborder="0" height="100%" width="800px" ></iframe>
        </div>-->

        <div id="fadestart" class="black_start"></div>
        <div id="busRoleDiv" class="whiteBus">

            <table width="100%">
                <tr>
                <center><td align="right" valign="top"><img src="<%=request.getContextPath()%>/icons pinvoke/cross-circle.png" ><a href="javascript:void(0)" onclick="roleCancel()" style="color:black;font-weight:bold">Close</a></td></center>
                </tr>
            </table>
            <div class="innerDiv">
                <table align="center" width="100%" style="overflow:scroll;height:auto" >
                    <tr style="overflow:scroll;height:auto;" >
                        <%for (int i = 0; i < folderpbro.getRowCount(); i++) {%>

                    <div id="rep-<%=folderpbro.getFieldValueString(i, 0)%>"></div>
                    <%}%>
                    </tr>
                    <tr>
                    <br><br>
                    </tr>
                </table>
            </div>
        </div>
        <div id="fadeBusRole" class="blackBus"></div>
        <%} catch (Exception e) {
                e.printStackTrace();
            }%>
        <div id="fadestart" class="black_start"></div>
        <div id="composeMessageDialog" title="Compose Message">
            <iframe src="pbTakeMailAddress1.jsp"  STYLE='display:block' height="380px" width="680px" frameborder="0" scrolling="no"></iframe>
        </div>

        <div id="reportSchedulerDialog" title="Report Scheduler">
            <center>
                <iframe src="ReportScheduler/pbreportScheduler.jsp?ReportType=D&repName=<%=pagename%>&REPORTID=<%=PbReportId%>"  height="250px" width="550px" frameborder="0" STYLE='display:block' align="center"></iframe>
            </center>
        </div>


    </form>
</body>
</html>

