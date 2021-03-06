<%--
    Document   : PbPortletKPITableTemplate
    Created on : Nov 17, 2009, 12:52:16 PM
    Author     : santhosh.kumar@progenbusiness.com
--%>
<%@page contentType="text/html" pageEncoding="UTF-8" import="java.util.*,prg.db.Container" %>


<%
        String ctxPath = request.getContextPath();
        String loguserId = String.valueOf(session.getAttribute("USERID"));
        HashMap ParametersHashMap = null;
        HashMap TableHashMap = null;
        HashMap GraphHashMap = null;
        HashMap ReportHashMap = null;


        String ReportId = "";
        Container container = null;
        String reportName = "";
        String reportDesc = "";
        HashMap map = new HashMap();
        String ReportFolders = "";
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
        String portalTabId = "";
        String rptType = "";
        if (request.getAttribute("portalTabId") != null) {
            portalTabId = String.valueOf(request.getAttribute("portalTabId"));
        }
        if (request.getAttribute("rptType") != null) {
            rptType = String.valueOf(request.getAttribute("rptType"));
        }
        /*
        else if (request.getParameter("portalTabId") != null) {
            portalTabId = request.getParameter("portalTabId");
        }
        */


        if (request.getSession(false) != null && request.getSession(false).getAttribute("PROGENTABLES") != null) {

            try {
                ReportId = String.valueOf(request.getAttribute("ReportId"));
                map = (HashMap) request.getSession(false).getAttribute("PROGENTABLES");

                if (map.get(ReportId) != null) {
                    container = (prg.db.Container) map.get(ReportId);
                } else {
                    container = new prg.db.Container();
                }
                reportName = container.getReportName();
                reportDesc = container.getReportDesc();

                ParametersHashMap = container.getParametersHashMap();
                TableHashMap = container.getTableHashMap();
                GraphHashMap = container.getGraphHashMap();
                ReportHashMap = container.getReportHashMap();

                if (request.getAttribute("ReportFolders") != null) {
                    ReportFolders = String.valueOf(request.getAttribute("ReportFolders"));
                }
                if (request.getAttribute("ReportDimensions") != null) {
                    ReportDimensions = String.valueOf(request.getAttribute("ReportDimensions"));
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

                if (TableHashMap != null) {
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
                if (ParametersHashMap.get("Parameters") != null) {
                    Parameters = (ArrayList) container.getParametersHashMap().get("Parameters");
                    ParametersNames = (ArrayList) container.getParametersHashMap().get("ParametersNames");
                }

                if (Parameters.size() != 0) {
                    for (int paramIndex = 0; paramIndex < Parameters.size(); paramIndex++) {
                        prevParamArray = prevParamArray + "," + String.valueOf(ParametersNames.get(paramIndex)) + "-" + String.valueOf(Parameters.get(paramIndex));
                    }
                    if (!(prevParamArray.equalsIgnoreCase(""))) {
                        prevParamArray = prevParamArray.substring(1);
                    }
                }
                if (ParametersHashMap.get("TimeParameters") != null) {
                    TimeParameters = (ArrayList) ParametersHashMap.get("TimeParameters");
                    TimeParametersNames = (ArrayList) ParametersHashMap.get("TimeParametersNames");
                }
                if (Parameters.size() != 0) {
                    for (int paramIndex = 0; paramIndex < Parameters.size(); paramIndex++) {
                        prevTimeParams = prevTimeParams + "," + String.valueOf(ParametersNames.get(paramIndex)) + "-" + String.valueOf(Parameters.get(paramIndex));
                    }
                    if (!(prevTimeParams.equalsIgnoreCase(""))) {
                        prevTimeParams = prevTimeParams.substring(1);
                    }
                }
                String UserFldsData = "";
                if (request.getAttribute("UserFlds") != null) {
                    UserFldsData = String.valueOf(request.getAttribute("UserFlds"));
                }
                String ctesPath=request.getContextPath();
%>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>pi EE</title>

        <script src="<%=ctesPath%>/javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=ctesPath%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>
        <script type="text/javascript" src="<%=ctesPath%>/javascript/jquery.tablesorter.js"></script>
        <script type="text/javascript" src="<%=ctesPath%>/tablesorter/addons/pager/jquery.tablesorter.pager.js"></script>
<!--        <script language="JavaScript" src="<%=request.getContextPath()%>/javascript/jquery.columnfilters.js"></script>-->

        <script src="<%=ctesPath%>/javascript/treeview/jquery.cookie.js" type="text/javascript"></script>
        <script src="<%=ctesPath%>/javascript/treeview/jquery.treeview.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=ctesPath%>/javascript/treeview/demo.js"></script>
<!--        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/draggable/ui.core.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/draggable/ui.draggable.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/draggable/ui.droppable.js"></script>

        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/ui.resizable.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/ui.accordion.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/ui.dialog.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/ui.sortable.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/effects.core.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/effects.explode.js"></script>-->

        <script type="text/javascript" src="<%=ctesPath%>/javascript/PbPortletKPITableTemplateJS.js"></script>
<!--        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/chili-1.8b.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/docs.js"></script>-->

   <!--addeb by bharathi reddy fro search option-->
        <script type="text/javascript" src="<%=ctesPath%>/javascript/quicksearch.js"></script>


        <link type="text/css" href="<%=ctesPath%>/stylesheets/demos.css" rel="stylesheet" />
        <link type="text/css" href="<%=ctesPath%>/stylesheets/ui.all.css" rel="stylesheet" />
        <link rel="stylesheet" href="<%=ctesPath%>/stylesheets/treeviewstyle/jquery.treeview.css" />
        <link rel="stylesheet" href="<%=ctesPath%>/stylesheets/treeviewstyle/screen.css" />
        <link rel="stylesheet" href="<%=ctesPath%>/stylesheets/jq.css" type="text/css" media="print, projection, screen">
        <link rel="stylesheet" href="<%=ctesPath%>/stylesheets/style.css" type="text/css" media="print, projection, screen">
        <link href="<%=ctesPath%>/stylesheets/javascript.css" type="text/css" rel="stylesheet">
        <link href="<%=ctesPath%>/stylesheets/html.css" type="text/css" rel="stylesheet">
        <link href="<%=ctesPath%>/stylesheets/css.css" type="text/css" rel="stylesheet">

        <link href="<%=ctesPath%>/reportDesigner.css" type="text/css" rel="stylesheet">
        <link type="text/css" href="<%=ctesPath%>/stylesheets/metadataButton.css" rel="stylesheet" />

       
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
            .paramRegion{
                background-color:#e6e6e6;
            }
        </style>
    </head>
    <!-- <body class="ReportTemplate" >-->
    <body class="body">


        <form name="myForm2" method="post">
            <input type="hidden" name="PrevParams" id="PrevParams" value="<%=prevParamArray%>">
            <input type="hidden" name="PrevTimeParams" id="PrevTimeParams" value="<%=prevTimeParams%>"
                   <input type="hidden" name="REPORTID" id="REPORTID" value="<%=ReportId%>">
            <table style="width:100%;height:380px" border="solid black 1px" >
                <tr>
                    <td width="13%" valign="top" class="draggedTable1" >
                        <div style="height:20px" class="ui-state-default draggedDivs ui-corner-all">
                            <font style="font-weight:bold" face="verdana" size="1px">Portlet details</font>
                        </div>
                        <div class="masterDiv">
                            <ul id="repTemTree" class="filetree treeview-famfamfam">
                                <li class="closed" ><img src="icons pinvoke/folder-horizontal.png">&nbsp;<span>Business Roles</span>
                                    <ul id="userFlds" class="background">
                                        <%=UserFldsData%>
                                    </ul>
                                </li>

                                <li class="closed"><img src="icons pinvoke/folder-horizontal.png">&nbsp;<span>Dimensions</span>
                                    <ul id="userDims" class="background">
                                    </ul>
                                </li>
                                <li class="closed"><img src="icons pinvoke/folder-horizontal.png">&nbsp;<span>Buckets</span>
                                    <ul id="userBuckets" class="background">
                                    </ul>
                                </li>
                            </ul>
                        </div>
                    </td>
                    <script>
                        getUserDims();
                        getBuckets();
                    </script>
                    <td  valign="top" width="87%" >
                        <table style="height:auto;max-height:100%;width:100%"  border="0">
                            <tr>
                                <td valign="top" width="100%" height="100px" class="paramRegion">
                                    <table style="height:100%" class="draggedTable"  id="newDragTables">
                                        <tr id="dragDims">
                                            <td style="height:100%" id="draggableDims" valign="top">
                                                <h3 style="height:20px" align="left" tabindex="0" aria-expanded="true" role="tab" class="ui-accordion-header ui-helper-reset ui-state-default ui-corner-all">
                                                    &nbsp;&nbsp;<font size="2" style="font-weight:bold">Drag Parameters To Here </font> &nbsp;&nbsp;<a id="showParams" href="javascript:showParams()" title="Click to Set Parameters" >Preview</a>

                                                </h3>
                                                <div id="dragDiv" style="min-width:800px;min-height:100px">
                                                    <ul id="sortable" style="width:800px">
                                                    </ul>
                                                </div>
                                            </td>
                                        </tr>
                                        <tr id="lovParams" style="height:100%;display:none">
                                            <td valign="top" style="height:100%">
                                                <h3 style="height:20px" align="left" tabindex="0" aria-expanded="true" role="tab" class="ui-accordion-header ui-helper-reset ui-state-default ui-corner-all">
                                                    &nbsp;&nbsp;<font size="2" style="font-weight:bold">Parameters</font>&nbsp;&nbsp;<a id="editParams" href="javascript:showMbrs()" title="Click to Edit Parameters" >Edit</a>
                                                </h3>
                                                <div id="paramDisp" style="width:800px">

                                                </div>
                                            </td>
                                        </tr>
                                    </table>
                                </td>
                            </tr>
                            <tr>
                                <td align="left" width="100%" height="180px" valign="top">
                                    <table style="height:100%" class="draggedTable" border="0" >
                                        <tr id="editTable">
                                            <td style="height:250px" valign="top">
                                                <h3  style="height:20px;width:100%" align="left"  tabindex="0" aria-expanded="true" role="tab" class="ui-accordion-header ui-helper-reset ui-state-default ui-corner-all">
                                                    &nbsp;&nbsp;<font size="2" style="font-weight:bold">kpi</font>&nbsp;&nbsp;<a id="prevTab"  href="javascript:PreviewTable()" title="Preview Table" >Preview</a>
                                                </h3>
                                                <table style="width:100%;height:220px" >
                                                        <tr>
                                                            <td width="200px" valign="top" class="draggedTable1">
                                                                <div style="height:20px" class="ui-state-default draggedDivs ui-corner-all"><font size="2" style="font-weight:bold">Drag KPI from below</font></div>
                                                                <div class="masterDiv" style="height:200px;overflow:auto">
                                                                    <ul id="kpiTree" class="filetree treeview-famfamfam">
<!--                                                                        <li class="open" style="background-image:url('images/treeViewImages/plus.gif')">-->
                                                                            <ul id="kpis">

                                                                            </ul>
<!--                                                                        </li>-->
                                                                    </ul>
                                                                </div>
                                                            </td>
                                                            <td width="200px" valign="top"  id="draggableKpis">
                                                                <div style="height:20px" class="ui-state-default draggedDivs ui-corner-all"><font size="2" style="font-weight:bold">Drop KPI Here </font></div>
                                                                <div id="dragDiv" style="height:200px;overflow:auto">
                                                                    <ul id="kpiSortable">
                                                                    </ul>
                                                                </div>
                                                            </td>
                                                        </tr>
                                                    </table>
                                                <%--<div id="tableDiv" style="width:100%;min-height:230px;">

                                                </div>--%>
                                            </td>
                                        </tr>
                                        <tr id="previewTable" style="display:none;">
                                            <td  style="height:250px" valign="top" >
                                                <h3  style="height:20px;width:100%" align="left"  tabindex="0" aria-expanded="true" role="tab" class="ui-accordion-header ui-helper-reset ui-state-default ui-corner-all">
                                                    <font size="2" style="font-weight:bold">kpi</font>&nbsp;&nbsp;<a id="prevTab"  href="javascript:EditTable()" title="Click to Edit Table" >Edit</a>
                                                </h3>
                                                <div id="previewDispTable" style="min-width:800px;min-height:230px;">
                                                </div>
                                            </td>
                                        </tr>
                                    </table>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>


            <div id="measures" style="display:none">
                <iframe id="dataDispmem" NAME='dataDispmem' style="width:700px" class="white_content1" SRC='#'></iframe>
            </div>
            <input type="hidden" name="allGraphIds" value="" id="allGraphIds">
            <input type="hidden" name="REPIds" value="" id="REPIds">
            <input type="hidden" name="CEPIds" value="" id="CEPIds">
            <input type="hidden" name="MsrIds" value="" id="MsrIds">
            <input type="hidden" name="Measures" value="" id="Measures">

            <input type="hidden" name="graphColumns" value="" id="graphColumns">

            <input type="hidden" name="currGrpColId" value="" id="currGrpColId">
        </form>
        <div id="fade" class="black_overlay"></div>
        <input type="hidden"  id="h" value="<%=request.getContextPath()%>">

        <table width="100%" >
            <tr>
                <td height="10px">&nbsp;</td>
                <td height="10px">&nbsp;</td>
            </tr>
            <tr>
                <td  height="10px">&nbsp;</td>
                <td align="center"><input type="button" class="navtitle-hover" style="width:auto" value="Save" onclick="savePortletDetails('<%=ReportId%>','<%=portalTabId%>','<%=rptType%>')">&nbsp;&nbsp;<input type="button" class="navtitle-hover" style="width:auto" value="Cancel" onclick="javascript:cancelReport();"></td>
            </tr>
            <tr>
                <td height="10px">&nbsp;</td>
                <td height="10px">&nbsp;</td>
            </tr>
        </table>
        <div id="fadestart" class="black_start"></div>
         <script type="text/javascript">
            var reportID='<%=ReportId%>'
            $(document).ready(function() {
                $("#repTemTree").treeview({
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
//                $("#kpiTree").treeview({
//                    animated: "normal",
//                    unique:true
//                });


            });
            function savePortletDetails(portletId,portalTabId,rptType){
                var REP="";
                var CEP="";
                //alert("portalViewer.do?portalBy=savePortlet&portletId="+portletId+"&displayType=KPI Table&portalTabId="+portalTabId)

                 $.ajax({
                    url: "portalViewer.do?portalBy=savePortlet&portletId="+portletId+"&displayType="+rptType,
                    success: function(data){
                        //window.opener.getPortletDetails(portletId,REP,CEP,'','',portalTabId);
                        window.close();
                        window.opener.refreshPortletDesigner('<%=ctxPath%>');
                    }
                });

            }
            function gohome(){
                document.forms.myForm2.action="baseAction.do?param=goHome";
                document.forms.myForm2.submit();
            }
            function logout(){
                document.forms.myForm2.action="baseAction.do?param=logoutApplication";
                document.forms.myForm2.submit();
            }
            function cancelReport(portletId){
                $.ajax({
                    url: "portalViewer.do?portalBy=cancelPortlet&portletId="+portletId,
                    success: function(data){
                        window.close();
                    }
                });
            }
        </script>
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

