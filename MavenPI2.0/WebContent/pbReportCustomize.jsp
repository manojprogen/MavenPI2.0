<%--
    Document   : pbReportCustomize
    Created on : Oct 13, 2009, 4:29:23 PM
    Author     : santhosh.kumar@progenbusiness.com
--%>
<%@taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@page  contentType="text/html" pageEncoding="UTF-8" import="java.util.*,prg.db.Container,prg.db.PbReturnObject,prg.db.PbDb,com.progen.charts.ProGenChartUtilities"%>

<%          //for clearing cache
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
            String loguserId = String.valueOf(session.getAttribute("USERID"));
            String AllGraphIds = "";
            HashMap ParametersHashMap = null;
            HashMap TableHashMap = null;
            HashMap GraphHashMap = null;
            HashMap ReportHashMap = null;


            String customReportId = "";
            String PrevReportId = "";
            Container container = null;
            String reportName = "";
            String reportDesc = "";
            HashMap map = new HashMap();
            String ReportFolders = "";
            String customMembers = "";
            String ReportDimensions = "";
            String ParamRegion = "";
            String ParamDispRegion = "";
            String TableRegion = "";
            String GraphRegion = "";
            String prevParamArray = "";
            String prevTimeParams = "";

            String prevREP = "";
            String prevCEP = "";
            String prevMeasures = "";
            String prevMeasureNames = "";
            String prevMeasureNamesList = "";

            ArrayList Parameters = new ArrayList();
            ArrayList ParametersNames = new ArrayList();

            ArrayList TimeParameters = new ArrayList();
            ArrayList TimeParametersNames = new ArrayList();


            ArrayList REP = new ArrayList();
            ArrayList CEP = new ArrayList();
            ArrayList Measures = new ArrayList();
            ArrayList MeasuresNames = new ArrayList();

            HashMap GraphTypesHashMap = null;
            //HashMap GraphClassesHashMap = null;

            String[] grpTypeskeys = new String[0];
            //String[] grpClasseskeys = new String[0];


            if (request.getSession(false) != null && request.getSession(false).getAttribute("PROGENTABLES") != null) {
                try {
                    GraphTypesHashMap = (HashMap) session.getAttribute("GraphTypesHashMap");
                    //GraphClassesHashMap = (HashMap) session.getAttribute("GraphClassesHashMap");
                    grpTypeskeys = (String[]) GraphTypesHashMap.keySet().toArray(new String[0]);
                    //grpClasseskeys = (String[]) GraphClassesHashMap.keySet().toArray(new String[0]);

                    customReportId = String.valueOf(request.getAttribute("CustomReportId"));
                    PrevReportId = String.valueOf(request.getAttribute("PrevReportId"));
                    map = (HashMap) request.getSession(false).getAttribute("PROGENTABLES");

                    if (map.get(customReportId) != null) {
                        container = (prg.db.Container) map.get(customReportId);
                    } else {
                        container = new prg.db.Container();
                    }
                    reportName = container.getReportName();
                    reportDesc = container.getReportDesc();

                    ParametersHashMap = container.getParametersHashMap();
                    TableHashMap = container.getTableHashMap();
                    GraphHashMap = container.getGraphHashMap();
                    ReportHashMap = container.getReportHashMap();

                    if (GraphHashMap.get("graphIds") != null) {
                        AllGraphIds = String.valueOf(GraphHashMap.get("graphIds"));
                    }
                    if (request.getAttribute("ReportFolders") != null) {
                        ReportFolders = String.valueOf(request.getAttribute("ReportFolders"));
                    }
                    if (request.getAttribute("ReportDimensions") != null) {
                        ReportDimensions = String.valueOf(request.getAttribute("ReportDimensions"));
                    }
                    if (request.getAttribute("customMembers") != null) {
                        customMembers = String.valueOf(request.getAttribute("customMembers"));
                    }
                    if (request.getAttribute("ParamRegion") != null) {
                        ParamRegion = String.valueOf(request.getAttribute("ParamRegion"));
                    }
                    if (request.getAttribute("ParamDispRegion") != null) {
                        ParamDispRegion = String.valueOf(request.getAttribute("ParamDispRegion"));
                    }
                    if (request.getAttribute("TableRegion") != null) {
                        TableRegion = String.valueOf(request.getAttribute("TableRegion"));
                    }
                    if (request.getAttribute("GraphRegion") != null) {
                        GraphRegion = String.valueOf(request.getAttribute("GraphRegion"));
                    }
                    if (TableHashMap != null && TableHashMap.size() != 0) {
                        REP = (ArrayList) TableHashMap.get("REP");
                        CEP = (ArrayList) TableHashMap.get("CEP");
                        Measures = (ArrayList) TableHashMap.get("Measures");
                        MeasuresNames = (ArrayList) TableHashMap.get("MeasuresNames");

                        if (REP != null) {
                            for (int j = 0; j < REP.size(); j++) {
                                prevREP = prevREP + "," + REP.get(j);
                            }
                            if (!(prevREP.equalsIgnoreCase(""))) {
                                prevREP = prevREP.substring(1);
                            }
                        }
                        if (CEP != null) {
                            for (int j = 0; j < CEP.size(); j++) {
                                prevCEP = prevCEP + "," + CEP.get(j);
                            }
                            if (!(prevCEP.equalsIgnoreCase(""))) {
                                prevCEP = prevCEP.substring(1);
                            }
                        }
                        if (Measures != null && MeasuresNames != null) {
                            for (int j = 0; j < Measures.size(); j++) {
                                prevMeasures = prevMeasures + "," + Measures.get(j);
                                prevMeasureNames = prevMeasureNames + "," + MeasuresNames.get(j);
                                prevMeasureNamesList = prevMeasureNamesList + "," + MeasuresNames.get(j) + "-" + Measures.get(j);
                            }
                            if (!(prevMeasures.equalsIgnoreCase(""))) {
                                prevMeasures = prevMeasures.substring(1);
                                prevMeasureNames = prevMeasureNames.substring(1);
                                prevMeasureNamesList = prevMeasureNamesList.substring(1);
                            }
                        }
                    }
                    if (ParametersHashMap.get("Parameters") != null && ParametersHashMap.size() != 0) {
                        Parameters = (ArrayList) container.getParametersHashMap().get("Parameters");
                        ParametersNames = (ArrayList) container.getParametersHashMap().get("ParametersNames");
                    }

                    if (Parameters.size() != 0) {
                        for (int paramIndex = 0; paramIndex < Parameters.size(); paramIndex++) {
                            prevParamArray = prevParamArray + "," + String.valueOf(ParametersNames.get(paramIndex));
                        }
                        if (!(prevParamArray.equalsIgnoreCase(""))) {
                            prevParamArray = prevParamArray.substring(1);
                        }
                    }
                    if (ParametersHashMap.get("TimeParameters") != null) {
                        TimeParameters = (ArrayList) ParametersHashMap.get("TimeParameters");
                        TimeParametersNames = (ArrayList) ParametersHashMap.get("TimeParametersNames");
                    }
                    if (TimeParameters.size() != 0) {
                        for (int paramIndex = 0; paramIndex < TimeParameters.size(); paramIndex++) {
                            prevTimeParams = prevTimeParams + "," + String.valueOf(TimeParameters.get(paramIndex));
                        }
                        if (!(prevTimeParams.equalsIgnoreCase(""))) {
                            prevTimeParams = prevTimeParams.substring(1);
                        }
                    }
                    String contextPath=request.getContextPath();
                    %>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title><bean:message key="ProGen.Title"/></title>

        <script src="<%=contextPath%>/javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/jquery.tablesorter.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/tablesorter/addons/pager/jquery.tablesorter.pager.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/jquery.columnfilters.js"></script>

        <script src="<%=contextPath%>/javascript/treeview/jquery.cookie.js" type="text/javascript"></script>
        <script src="<%=contextPath%>/javascript/treeview/jquery.treeview.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/treeview/demo.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/draggable/ui.core.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/draggable/ui.draggable.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/draggable/ui.droppable.js"></script>

        <script type="text/javascript" src="<%=contextPath%>/javascript/ui.resizable.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/ui.accordion.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/ui.dialog.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/ui.sortable.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/effects.core.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/effects.explode.js"></script>

        <script type="text/javascript" src="<%=contextPath%>/javascript/reportCustomize.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/chili-1.8b.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/docs.js"></script>
        <!-- below two lines only added by bharathi reddy on 26-08-09 -->

        <script src="javascript/jquery.contextMenu.js" type="text/javascript"></script>



        <link type="text/css" href="<%=contextPath%>/stylesheets/demos.css" rel="stylesheet" />
        <link type="text/css" href="<%=contextPath%>/stylesheets/ui.all.css" rel="stylesheet" />
        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/treeviewstyle/jquery.treeview.css" />
        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/treeviewstyle/screen.css" />
        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/jq.css" type="text/css" media="print, projection, screen">
        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/style.css" type="text/css" media="print, projection, screen">
        <link href="<%=contextPath%>/stylesheets/javascript.css" type="text/css" rel="stylesheet">
        <link href="<%=contextPath%>/stylesheets/html.css" type="text/css" rel="stylesheet">
        <link href="<%=contextPath%>/stylesheets/css.css" type="text/css" rel="stylesheet">

        <link href="<%=contextPath%>/reportDesigner.css" type="text/css" rel="stylesheet">
        <link type="text/css" href="<%=contextPath%>/stylesheets/metadataButton.css" rel="stylesheet" />
        <!-- below three lines only added by bharathi reddy on 26-08-09 -->


        <link href="stylesheets/jquery.contextMenu.css" rel="stylesheet" type="text/css" />

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
        <script type="text/javascript">

            $(document).ready(function() {
                //Tie a menu to the right click of each row of the table with a class of customerRow
                $(".customerRow").contextMenu({ menu: 'myMenu' },
                function(action, el, pos) { contextMenuWork(action, el, pos); });

                $(".custMeasureMenu").contextMenu({ menu: 'custMeasureListMenu' },
                function(action, el, pos) { contextMenuWorkCustMeasure(action, el, pos); });
                //for adding custom measure by bharathi reddy
                // $(".custMeasureMenu").contextMenu({ menu: 'custMeasureListMenu', leftButton: true }, function(action, el, pos) {


                // contextMenuWorkCustMeasure(action, el, pos); });
                //ends
                $("#repTemTree").treeview({
                    animated: "normal",
                    unique:true
                });
                $("#repTemTree2").treeview({
                    animated: "normal",
                    unique:true
                });
                $("#repTemTree3").treeview({
                    animated: "normal",
                    unique:true
                });
                $("#graphTree").treeview({
                    animated: "normal",
                    unique:true
                });
                $("#tableTree").treeview({
                    animated: "normal",
                    unique:true
                });
                $(".column").sortable({
                    connectWith: '.column'
                });
            });

            //for adding custom measure by bharathi reddy
            function contextMenuWorkCustMeasure(action, el, pos) {
                switch (action) {
                    case "addCustMeasure":{
                            addCustomMeasure();
                            break;
                        }
                }
            }

            function createCustMeasure(Obj){
                $("#"+Obj.id).contextMenu({
                    menu: 'myMenu'
                },function(action, el, pos) {
                    contextMenuWorkCustMeasure(action, el, pos);
                });
            }

            function addCustomMeasure(){
                var f=document.getElementById('custmemDisp');
                // alert(buildFldIds())
                var s="createCustMember.jsp?folderIds="+buildFldIds();
                f.src=s;
                document.getElementById('custmemDisp').style.display='block';
                document.getElementById('fade').style.display='block';
            }
            function cancelCustMember(){
                document.getElementById('custmemDisp').style.display='none';
                document.getElementById('fade').style.display='none';

            }
            function cancelCustMembersave(columnname){
                document.getElementById('custmemDisp').style.display='none';
                document.getElementById('fade').style.display='none';

                var branches = $("<li><img src='icons pinvoke/table.png' ><span >&nbsp;"+columnname+"</span></li>").appendTo("#customMeasure");
                $("#customMeasure").treeview({
                    add: branches
                });
            }
            //ends
            function createReport(repId){
                document.myForm2.action="reportViewer.do?reportBy=saveCustomReport&REPORTID="+repId;
                document.myForm2.submit();
            }
            function cancelReport(repId){
                document.forms.myForm2.action="reportViewer.do?reportBy=viewReport&REPORTID="+repId;
                document.forms.myForm2.submit();
            }
//            function gohome(){
//                document.forms.myForm2.action="baseAction.do?param=goHome";
//                document.forms.myForm2.submit();
//            }
//            function logout(){
//                document.forms.myForm2.action="baseAction.do?param=logoutApplication";
//                document.forms.myForm2.submit();
//            }
            function viewDashboardG(path){
                document.forms.myForm2.action=path;
                document.forms.myForm2.submit();
            }
            function viewReportG(path){
                document.forms.myForm2.action=path;
                document.forms.myForm2.submit();
            }
//
//            function goGlobe(){
//                $(".navigateDialog").dialog('open');
//            }
//            function closeStart(){
//                $(".navigateDialog").dialog('close');
//            }
            function goPaths(path){
                parent.closeStart();
                document.forms.myForm2.action=path;
                document.forms.myForm2.submit();
            }
        </script>
        <style type="text/css">
            .column { width:auto; float: left; padding-bottom: 5px; }
            .portlet { margin: 0 1em 1em 0;width:auto }
            .portlet-header { margin: 0.3em; padding-bottom: 4px; padding-left: 0.2em;width:auto }
            .portlet-header .ui-icon { float: right;width:auto }
            .portlet-content { padding: 0.4em;width:auto; }
            .ui-sortable-placeholder { border: 1px dotted black; visibility: visible !important; height: 50px !important; }
            .ui-sortable-placeholder * { visibility: hidden; }
            .fontsty{
                -moz-border-radius-bottomleft:4px;
                -moz-border-radius-bottomright:4px;
                -moz-border-radius-topleft:4px;
                -moz-border-radius-topright:4px;
                background-color:#bdbdbd;
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
            a {font-family:Verdana;cursor:pointer;}
            a:link {color:#369}
            *{
                font:           11px verdana;
            }
            .parameterReg{
                background-color:#e6e6e6
            }

            .white_content {
                display: none;
                position: absolute;
                top: 15%;
                left: 25%;
                width: 550px;
                height:650px;
                padding: 16px;
                border: 10px solid silver;
                background-color: white;
                z-index:1002;
                -moz-border-radius-bottomleft:6px;
                -moz-border-radius-bottomright:6px;
                -moz-border-radius-topleft:6px;
                -moz-border-radius-topright:6px;
            }


        </style>
    </head>
    <!-- <body class="ReportTemplate" >-->
    <body id="mainBody">
                        <table style="width:100%;">
                            <tr>
                                <td valign="top" style="width:100%;">
                                    <jsp:include page="Headerfolder/headerPage.jsp"/>
                                </td>
                            </tr>
                        </table>


        <form name="myForm2" method="post">
            <input type="hidden" name="PrevParams" id="PrevParams" value="<%=prevParamArray%>">
            <input type="hidden" name="PrevTimeParams" id="PrevTimeParams" value="<%=prevTimeParams%>"
            <input type="hidden" name="REPORTID" id="REPORTID" value="<%=customReportId%>">
            <table width="100%" >
 

                <tr style="height:15px;width:100%;max-height:100%">
                    <td>
                        <table width="100%" class="ui-corner-all">
                            <tr>
                                <td valign="top" style="width:80%" >
                                    <%
                    com.progen.reportview.action.showReportName repname = new com.progen.reportview.action.showReportName();
                    ArrayList repNameList = repname.buildReportName(reportName);
                    for (int i = 0; i < repNameList.size(); i++) {%>
                                    <font  style="color:#369;font-family:verdana;font-size:15px;font-weight:bold"  title="<%=reportDesc%>"><%=repNameList.get(i)%></font>
                                    <br/>
                                    <%}%>
                                </td>
                                <td valign="top" style="height:10px;width:20%" align="right">
<!--                                    <a href="javascript:void(0)" onclick="javascript:goGlobe()" style="font-size:10px;color:#369;font-weight:bold;text-decoration: none;font-family:Georgia"> Navigation </a> |
                                    <a href="javascript:void(0)" onclick="javascript:gohome('<%=loguserId%>')" style="font-size:10px;color:#369;font-weight:bold;text-decoration: none;font-family:Georgia;"> Home </a> |-->
<!--                                    <a href="#" style="font-size:10px;color:#369;font-weight:bold;text-decoration: none;font-family:Georgia"> Help </a> |-->
<!--                                    <a href="javascript:void(0)" onclick="javascript:logout()" style="font-size:10px;color:#369;font-weight:bold;text-decoration: none;font-family:Georgia"> Logout </a>-->
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>

            <table style="width:100%" border="solid black 1px" >
                <tr>
                    <td width="13%" valign="top" class="draggedTable1" >
                        <div style="height:20px" class="ui-state-default draggedDivs ui-corner-all">
                            <font style="font-weight:bold" face="verdana" size="1px">&nbsp;Report Customization</font>
                        </div>
                        <div class="masterDiv">
                            <ul id="repTemTree" class="filetree treeview-famfamfam">
                                <li class="closed" ><img src="icons pinvoke/folder-horizontal.png">&nbsp;<span>User Folders</span>
                                    <ul id="userFlds">
                                        <%=ReportFolders%>
                                    </ul>
                                </li>
                            </ul>
                            <ul>
                                <ul id="repTemTree2" class="filetree treeview-famfamfam">
                                    <!--images/treeViewImages/cabin_with_smog_pipe.png-->
                                    <li class="closed"><img src="icons pinvoke/folder-horizontal.png">&nbsp;<span>Dimensions</span>
                                        <ul id="userDims">
                                            <%-- <%=ReportDimensions%>--%>
                                        </ul>
                                    </li>
                                </ul>
                            </ul>

                            <%--newly added by bharathi reddy for adding custom measure <ul>"--%>

                            <ul id="repTemTree3" class="filetree treeview-famfamfam">
                                <!--images/treeViewImages/cabin_with_smog_pipe.png-->
                                <li class="closed"><img src="icons pinvoke/folder-horizontal.png"><span id="123" class="cntxtMenu" onmouseup="createCustMeasure(this)">Custom Measure</span>
                                    <ul id="customMeasure">
                                        <%=customMembers%>
                                    </ul>
                                </li>
                            </ul>
                            <%--</ul>--%>
                            <ul id="myMenu" class="contextMenu" >
                                <li><a href="#addCustMeasure">Add Measure</a></li>
                            </ul>
                            <%--newly added by bharathi reddy for adding custom measure end--%>
                        </div>
                    </td>
                <script>
                    getUserDims();
                    displayPrevParams();
                </script>
                <td  valign="top" width="87%" >
                    <table style="height:auto;max-height:100%;width:100%"  border="0">
                        <tr>
                            <td valign="top" width="100%" height="100px" >
                                <table style="height:100%" class="draggedTable"  id="newDragTables">
                                    <tr id="dragDims" style="height:100%;display:none" class="parameterReg">
                                        <td style="height:100%" id="draggableDims" valign="top">
                                            <h3 style="height:20px" align="left" tabindex="0" aria-expanded="true" role="tab" class="ui-accordion-header ui-helper-reset ui-state-default ui-corner-all">
                                                &nbsp;&nbsp; <font size="2" style="font-weight:bold">Drag Parameters To Here </font> &nbsp;&nbsp;<a id="showParams" href="javascript:showParams()" title="Click to Set Parameters" >Preview</a>

                                            </h3>
                                            <div id="dragDiv" style="min-width:800px;min-height:100px">
                                                <ul id="sortable" style="width:800px">
                                                    <%=ParamRegion%>
                                                </ul>
                                            </div>
                                        </td>
                                    </tr>
                                    <tr id="lovParams" style="height:100%;" class="parameterReg">
                                        <td valign="top" style="height:100%">
                                            <h3 style="height:20px" align="left" tabindex="0" aria-expanded="true" role="tab" class="ui-accordion-header ui-helper-reset ui-state-default ui-corner-all">
                                                &nbsp;&nbsp;<font size="2" style="font-weight:bold">Parameters</font>&nbsp;&nbsp;<a id="editParams" href="javascript:showMbrs()" title="Click to Edit Parameters" >Edit</a>
                                            </h3>
                                            <div id="paramDisp" style="min-width:800px;min-height:100px">
                                                <%=ParamDispRegion%>
                                            </div>
                                        </td>
                                    </tr>
                                </table>
                                <!--  <a id="Refresh" href="javascript:showParams()" title="Click to Set Parameters" >Preview</a>-->
                            </td>
                        </tr>
                        <tr>
                            <td align="left" width="100%" height="180px" valign="top">
                                <table style="height:100%" class="draggedTable" border="0" >
                                    <tr id="previewGraph">
                                        <td  style="height:100%" valign="top" >

                                            <h3 style="height:20px;width:100%" align="left" tabindex="0" aria-expanded="true" role="tab" class="ui-accordion-header ui-helper-reset ui-state-default ui-corner-all">
                                                &nbsp;&nbsp;<font size="2" style="font-weight:bold">Graphs </font>&nbsp;&nbsp;
                                                <%--<a href="javascript:void(0)" onclick="showGraphs()">AddGraphs</a>&nbsp;&nbsp;--%>
                                                <a href="javascript:void(0)" id="editGraphs" title="Refresh Graphs" onclick="previewGraphs()">Refresh Graphs</a>
                                            </h3>
                                            <div   id="previewDispGraph" style="width:100%;min-height:250px;height:auto;max-height:100%">
                                                <%=GraphRegion%>
                                            </div>

                                        </td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                        <tr>
                            <td align="left" width="100%" height="180px" valign="top">
                                <table  border="0" width="100%">
                                    <tr id="editTable" style="display:none;">
                                        <td style="height:250px" valign="top">
                                            <h3  style="height:20px;width:100%" align="left"  tabindex="0" aria-expanded="true" role="tab" class="ui-accordion-header ui-helper-reset ui-state-default ui-corner-all">
                                                &nbsp;&nbsp;<font size="2" style="font-weight:bold">Table </font>&nbsp;&nbsp;<a id="prevTab"  href="javascript:PreviewTable()" title="Preview Table" >Preview</a>
                                            </h3>
                                            <div id="tableDiv" style="width:100%;min-height:230px;">
                                                <Table>
                                                    <Tr>
                                                        <Td>
                                                            <a id="rep" href="javascript:showRowParams()" title="Click to Select Row Parameter" >Row Edge</a>&nbsp;
                                                            <div style="display:none;width:auto;height:auto;background-color:#ffffff;overflow:auto;position: absolute;text-align: left;border: 1px solid #000000;border-top-width: 0px;" id="repDiv">
                                                            </div>
                                                        </Td>
                                                        <Td>
                                                            <a id="cep" href="javascript:showColParams()" title="Click to Select Column Parameter"  >Column Edge</a>&nbsp;
                                                            <div style="display:none;width:auto;height:auto;background-color:#ffffff;overflow:auto;position: absolute;text-align: left;border: 1px solid #000000;border-top-width: 0px;" id="cepDiv">
                                                            </div>
                                                        </Td>
                                                        <Td>
                                                            <a id="Measure" href="javascript:showMeasures()" title="Click to Select Measures" >Measures</a>&nbsp;
                                                        </Td>
                                                    </Tr>
                                                </Table>
                                            </div>

                                        </td>
                                    </tr>

                                    <tr id="previewTable">
                                        <td  style="height:250px" valign="top" >
                                            <h3  style="height:20px;width:100%" align="left"  tabindex="0" aria-expanded="true" role="tab" class="ui-accordion-header ui-helper-reset ui-state-default ui-corner-all">
                                                <font size="2" style="font-weight:bold">Table </font>&nbsp;&nbsp;<a id="prevTab"  href="javascript:EditTable()" title="Click to Edit Table" >Edit</a>
                                            </h3>

                                            <div id="previewDispTable" style="min-width:800px;min-height:230px;">
                                                <%=TableRegion%>
                                            </div>
                                        </td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                    </table>
                </td>
                <div style="display:none"><td width="40%" valign="top">
                        <iframe  id="custmemDisp" NAME='custmemDisp'  class="white_content" src='about:blank'></iframe>
                    </td></div>
                </tr>
            </table>

            <table width="100%" >
                <tr>
                    <td height="10px">&nbsp;</td>
                    <td height="10px">&nbsp;</td>
                </tr>
                <tr>
                    <td height="10px">&nbsp;</td>
                    <td align="center"><input type="button" value="Next" class="navtitle-hover" onclick="createReport('<%=customReportId%>')" style="width:auto">&nbsp;&nbsp;<input type="button" class="navtitle-hover" value="Cancel" style="width:auto"  onclick="javascript:cancelReport('<%=PrevReportId%>')"></td>
                </tr>
                <tr>
                    <td height="10px">&nbsp;</td>
                    <td height="10px">&nbsp;</td>
                </tr>
            </table>


            <!--<div id="footer">
                <table width="100%" class="fontsty" style="background-color:#bdbdbd">
                    <tr class="fontsty" style="height:10px;width:100%;max-height:100%;background-color:#bdbdbd">
                        <td style="height:10px;width:100%;background-color:#bdbdbd" >
                            <center style="background-color:#bdbdbd"><font  style="color:#fff;font-family:verdana;font-size:10px;font-weight:normal" align="center">Copyright © 2009-12 <a href="http://www.progenbusiness.com" style="color:red;font-weight:bold;font-size:10px;font-family:verdana">Progen Business Solutions.</a> All Rights Reserved</font></center>
                        </td>
                    </tr>
                </table>
            </div>-->


            <div id="measures" style="display:none">
                <iframe id="dataDispmem" NAME='dataDispmem' style="width:700px" class="white_content1" src='about:blank'></iframe>
            </div>

            <input type="hidden" name="allGraphIds" value="<%=AllGraphIds%>" id="allGraphIds">
            <input type="hidden" name="REPIds" value="<%=prevREP%>" id="REPIds">
            <input type="hidden" name="CEPIds" value="<%=prevCEP%>" id="CEPIds">
            <input type="hidden" name="MsrIds" value="<%=prevMeasures%>" id="MsrIds">
            <input type="hidden" name="Measures" value="<%=prevMeasureNamesList%>" id="Measures">

            <input type="hidden" name="graphColumns" value="" id="graphColumns">
            <input type="hidden" name="currGrpColId" value="" id="currGrpColId">
        </form>
        <div id="fade" class="black_overlay"></div>

        <div id="graphList" style="display:none;overflow:scroll" class="white_content1">

            <table width="100%">
                <tbody>
                    <%
                    ProGenChartUtilities utilities = new ProGenChartUtilities();
                    String str = utilities.buildGraphTypesDiv(request.getContextPath(), grpTypeskeys, GraphTypesHashMap, "getGraphName");
                    %>
                    <%=str%>
                </tbody>
            </table>

            <center><input type="button" value="Cancel" onclick="document.getElementById('graphList').style.display='none';document.getElementById('fade').style.display='none'"></center>

        </div>
        <input type="hidden"  id="h" value="<%=request.getContextPath()%>">
        <div>
            <iframe  id="graphCols" NAME='bucketDisp'  STYLE='display:none;'   class="white_content1" src='about:blank'></iframe>
        </div>
        <div>
            <iframe  id="graphDtls" NAME='graphDtls'  class="white_content1" STYLE='display:none;' src='about:blank'></iframe>
        </div>

<!--        <div id="reportstart" class="navigateDialog" title="Navigation">
            <iframe src="startPage.jsp" frameborder="0" height="100%" width="800px" ></iframe>
        </div>-->
        <div id="fadestart" class="black_start"></div>

        <%--newly added by bharathi reddy for adding custom measure --%>
        <ul id=custMeasureListMenu" class="contextMenu" style="width:150px;text-align:left">
            <li class="addCustMeasure"><a href="#addCustMeasure">Add Measure</a></li>
        </ul>
        <%--newly added by bharathi reddy for adding custom measure end--%>


    </body>
</html>
<%
                } catch (Exception exp) {
                    exp.printStackTrace();
                }

            } else {
                response.sendRedirect(request.getContextPath() + "/pbSessionExpired.jsp");
            }
%>


