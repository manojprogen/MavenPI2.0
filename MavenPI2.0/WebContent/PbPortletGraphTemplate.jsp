<%--
    Document   : PbPortletGraphTemplate
    Created on : Nov 4, 2009, 1:35:12 PM
    Author     : santhosh.kumar@progenbusiness.com
--%>
<%@page contentType="text/html" pageEncoding="UTF-8" import="prg.db.Container,java.util.*,com.progen.charts.ProGenChartUtilities"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<%@taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>


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

            if (request.getAttribute("portalTabId") != null) {
                portalTabId = String.valueOf(request.getAttribute("portalTabId"));
            }
            /*
            else if (request.getParameter("portalTabId") != null) {
            portalTabId = request.getParameter("portalTabId");
            }
             */
            HashMap GraphTypesHashMap = null;
            String[] grpTypeskeys = new String[0];
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
                    GraphTypesHashMap = (HashMap) session.getAttribute("GraphTypesHashMap");
                    grpTypeskeys = (String[]) GraphTypesHashMap.keySet().toArray(new String[0]);
                    String cyttsPath=request.getContextPath();
%>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
       <title>pi EE</title>
        <script src="<%=cyttsPath%>/javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=cyttsPath%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>
        <script src="<%=cyttsPath%>/javascript/treeview/jquery.cookie.js" type="text/javascript"></script>
        <script src="<%=cyttsPath%>/javascript/treeview/jquery.treeview.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=cyttsPath%>/javascript/treeview/demo.js"></script>
<!--        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/draggable/ui.core.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/draggable/ui.draggable.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/draggable/ui.droppable.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/ui.dialog.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/ui.sortable.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/docs.js"></script>-->
        <script src="javascript/jquery.contextMenu.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=cyttsPath%>/javascript/PbPortletGraphTemplateJS.js"></script>


        <link type="text/css" href="<%=cyttsPath%>/stylesheets/demos.css" rel="stylesheet" />
        <link type="text/css" href="<%=cyttsPath%>/stylesheets/ui.all.css" rel="stylesheet" />
        <link rel="stylesheet" href="<%=cyttsPath%>/stylesheets/treeviewstyle/jquery.treeview.css" />
        <link rel="stylesheet" href="<%=cyttsPath%>/stylesheets/treeviewstyle/screen.css" />
        <link rel="stylesheet" href="<%=cyttsPath%>/stylesheets/jq.css" type="text/css" media="print, projection, screen">
        <link rel="stylesheet" href="<%=cyttsPath%>/stylesheets/style.css" type="text/css" media="print, projection, screen">
        <link href="<%=cyttsPath%>/stylesheets/javascript.css" type="text/css" rel="stylesheet">
        <link href="<%=cyttsPath%>/stylesheets/html.css" type="text/css" rel="stylesheet">
        <link href="<%=cyttsPath%>/stylesheets/css.css" type="text/css" rel="stylesheet">
        <link type="text/css" href="<%=cyttsPath%>/stylesheets/metadataButton.css" rel="stylesheet" />
        <link href="stylesheets/jquery.contextMenu.css" rel="stylesheet" type="text/css" />
        <link href="<%=cyttsPath%>/reportDesigner.css" type="text/css" rel="stylesheet">
        <script type="text/javascript" src="<%=cyttsPath%>/jQuery/jquery/ui/jquery.jBreadCrumb.1.1.js"></script>
        <script type="text/javascript" src="<%=cyttsPath%>/jQuery/jquery/ui/jquery.easing.1.3.js"></script>
        <link type="text/css" href="<%=cyttsPath%>/jQuery/jquery/themes/base/BreadCrumb.css" rel="stylesheet" />
        <link type="text/css" href="<%=cyttsPath%>/jQuery/jquery/themes/base/Base.css" rel="stylesheet" />
        <link href="stylesheets/jquery.contextMenu.css" rel="stylesheet" type="text/css" />
        <link type="text/css" href="<%=cyttsPath%>/jQuery/jquery/themes/base/ui.theme.css" rel="stylesheet" />
        <link href="<%=cyttsPath%>/reportDesigner.css" type="text/css" rel="stylesheet">
       
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
            .paramRegion{
                background-color:#e6e6e6;
            }
            .appletDiv{
                z-index:1000;
                -moz-border-radius-bottomleft:4px;
                -moz-border-radius-bottomright:4px;
                -moz-border-radius-topleft:4px;
                -moz-border-radius-topright:4px;
                border:1px solid #448DAE;
                height:300px
            }
            .flagDiv{
                width:150px;
                height:200px;
                background-color:#ffffff;
                overflow:auto;
                position:absolute;
                text-align:left;
                border:1px solid #000000;
                border-top-width: 0px;
                z-index:1002;
            }
        </style>
    </head>
    <!-- <body class="ReportTemplate" >-->
    <body >

        <%
                    String UserFldsData = "";
                    if (request.getAttribute("UserFlds") != null) {
                        UserFldsData = String.valueOf(request.getAttribute("UserFlds"));
                    }
        %>
        <form name="myForm2" method="post" action="">
            <input type="hidden" name="PrevParams" id="PrevParams" value="<%=prevParamArray%>">
            <input type="hidden" name="PrevTimeParams" id="PrevTimeParams" value="<%=prevTimeParams%>">
                   <input type="hidden" name="REPORTID" id="REPORTID" value="<%=ReportId%>">

            <table style="width:100%;height:380px" border="solid black 1px" >
                <tr>
                    <td width="13%" valign="top" class="draggedTable1" >
                        <div style="height:20px" class="ui-state-default draggedDivs ui-corner-all">
                            <font style="font-weight:bold" face="verdana" size="1px">Portlet Details</font>
                        </div>
                        <div class="masterDiv">
                            <ul id="repTemTree" class="filetree treeview-famfamfam">
                                <li class="closed" ><img alt=""  src="icons pinvoke/folder-horizontal.png"><span>Business Roles</span>
                                    <ul id="userFlds">
                                        <%=UserFldsData%>
                                    </ul>
                                </li>

                                <li class="closed"><img alt=""  src="icons pinvoke/folder-horizontal.png"><span>Dimensions</span>
                                    <ul id="userDims">
                                    </ul>
                                </li>
                                <li class="closed"><img alt=""  src="icons pinvoke/folder-horizontal.png"><span>Buckets</span>
                                    <ul id="userBuckets">
                                    </ul>
                                </li>
                            </ul>
                        </div>
                    </td>
                <script type=" " >
                    getUserDims();
                </script>
                <td  valign="top" width="87%" >
                    <table style="height:auto;max-height:100%;width:100%"  border="0">
                        <tr>
                            <td valign="top" width="100%" height="100px" class="paramRegion">
                                <table style="height:100%" class="draggedTable"  id="newDragTables">
                                    <tr id="dragDims">
                                        <td style="height:100%" id="draggableDims" valign="top">
                                            <h3 style="height:20px" align="left" tabindex="0" aria-expanded="true" role="tab" class="ui-accordion-header ui-helper-reset ui-state-default ui-corner-all">
                                                <font size="2" style="font-weight:bold">Drag Parameters To Here </font> &nbsp;&nbsp;<a id="showParams" href="javascript:showParams()" title="Click to Set Parameters" >Preview</a>

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
                                                <font size="2" style="font-weight:bold">Parameters</font>&nbsp;&nbsp;<a id="editParams" href="javascript:showMbrs()" title="Click to Edit Parameters" >Edit</a>
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
                                    <tr id="previewGraph">
                                        <td  style="height:100%" valign="top" >

                                            <h3 style="height:20px;width:100%" align="left" tabindex="0" aria-expanded="true" role="tab" class="ui-accordion-header ui-helper-reset ui-state-default ui-corner-all">
                                                <table><tr><td><font size="2" style="font-weight:bold">Graphs </font> &nbsp;&nbsp;</td><td>
                                                <a id="rep" href="javascript:showRowParams()" title="View By" >ViewBy</a>&nbsp;&nbsp;
                                                <div style="display:none;width:auto;height:auto;background-color:#ffffff;overflow:auto;position: absolute;text-align: left;border: 1px solid #000000;border-top-width: 0px;color:black" id="repDiv">
                                                </div></td><td>
                                                <a href="javascript:void(0)" onclick="sortOption()" title="sort">Sort</a>&nbsp;&nbsp;
                                                   <div style="display:none;width:auto;height:auto;background-color:#ffffff;overflow:auto;position:absolute;text-align:left; border: 1px solid #000000;border-top-width: 0px;color:blue" id="sortDiv">
                                                      <table>
                                                          <tr><td><a href="javascript:void(0)" onclick="setSortOrder('Top-10')"><font style="color:#000000">Top-10</font></a></td></tr>
                                                          <tr><td><a href="javascript:void(0)" onclick="setSortOrder('Bottom-10')"><font style="color:#000000">Bottom-10</font></a></td></tr>
                                                          <tr><td><a href="javascript:void(0)" onclick="setSortOrder('Top-5')"><font style="color:#000000">Top-5</font></a></td></tr>
                                                          <tr><td><a href="javascript:void(0)" onclick="setSortOrder('Bottom-5')"><font style="color:#000000">Bottom-5</font></a></td></tr>
                                                      </table>
                                                  </div></td><td>
                                                <a href="javascript:void(0)" onclick="showGraphs()" title="Add Graph">Add Graph</a>&nbsp;&nbsp;
                                                <a href="javascript:void(0)" id="editGraphs" title="Preview" onclick="previewGraphs()">Preview</a>
                                                  </td></tr></table>  </h3>
                                            <div   id="previewDispGraph" style="width:100%;min-height:250px;height:auto;max-height:100%">
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
                <iframe id="dataDispmem" NAME='dataDispmem' style="width:700px" class="white_content1" src='about:blank'></iframe>
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
        <%--<div id="graphList"  style="display:none" title="Select Graph" >--%>
        <div id="graphList" align="" title="Select Graph"  style="display:none;overflow:scroll" class="white_content1">
            <table>
                <tbody>
                <tbody>
                    <%
                    ProGenChartUtilities utilities = new ProGenChartUtilities();
                    String str = utilities.buildGraphTypesDiv(request.getContextPath(), grpTypeskeys, GraphTypesHashMap, "getGraphName");
                    %>
                    <%=str%>
                </tbody>
            </table>
            <%--<center><input type="button" class="navtitle-hover" style="width:auto" value="Cancel" onclick="document.getElementById('graphList').style.display='none';document.getElementById('fade').style.display='none'"></center>--%>

        </div>
        <input type="hidden"  id="h" value="<%=request.getContextPath()%>">
        <div id="graphColsDiv" title="Add Graph Columns" >
            <iframe  id="graphCols" NAME='bucketDisp' STYLE='display:none;'  scrolling="no" width="250%"    class="white_content1" frameborder="0"  src='about:blank' onload='javascript:resizeIframe(this);' ></iframe>
        </div>
        <div id="graphDtlsDiv"    title="Graph Details" >
            <iframe  id="graphDtls" NAME='graphDtls'  STYLE='display:none;'   class="white_content1" width="100%" height="100%" frameborder="0"    src='about:blank'></iframe>
        </div>

        <%--<div id="graphColsDiv" style="display:none" title="Add Graph Columns" >
            <iframe  id="graphCols" NAME='bucketDisp' width="100%" height="100%" frameborder="0" SRC=''></iframe>
        </div>
        <div id="graphDtlsDiv"   style="display:none" title="Graph Details" >
            <iframe  id="graphDtls" NAME='graphDtls' width="100%" height="100%" frameborder="0"   SRC=''></iframe>
        </div>--%>
        <table width="100%" >
            <tr>
                <td >&nbsp;</td>
                <td>&nbsp;</td>
            </tr>
            <tr>
                <td>&nbsp;</td>
                <td align="center"><input type="button" class="navtitle-hover" style="width:auto" value="Save" onclick="createReport('<%=ReportId%>','<%=portalTabId%>')">&nbsp;&nbsp;<input type="button" class="navtitle-hover" style="width:auto" value="Cancel" onclick="javascript:cancelReport();"></td>
            </tr>
            <tr>
                <td >&nbsp;</td>
                <td >&nbsp;</td>
            </tr>
        </table>
            <div style="display: none" id="applyFilterDiv" title="Apply Filter">
                <iframe id="filterIframe" name="filterIframe" src="about:blank" frameborder="0"></iframe>
            </div>
             <script  type="text/javascript" >
            var reportID='<%=ReportId %>'
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
                if ($.browser.msie == true){
                    $("graphList").dialog({
                        autoOpen: false,
                        height: 300,
                        width: 350,
                        position: 'justify',
                        modal: true
                    });
                    $("graphColsDiv").dialog({
                        autoOpen: false,
                        height: 300,
                        width: 350,
                        position: 'justify',
                        modal: true
                    });
                    $("graphDtlsDiv").dialog({
                        autoOpen: false,
                        height: 300,
                        width: 350,
                        position: 'justify',
                        modal: true
                    });
                    $("#applyFilterDiv").dialog({
                        autoOpen: false,
                        height: 600,
                        width: 400,
                        position: 'justify',
                        modal: true
                    })
                }
                else{
                    $("graphList").dialog({
                        autoOpen: false,
                        height: 300,
                        width: 350,
                        position: 'justify',
                        modal: true
                    });
                    $("graphColsDiv").dialog({
                        autoOpen: false,
                        height: 300,
                        width: 350,
                        position: 'justify',
                        modal: true
                    });
                    $("graphDtlsDiv").dialog({
                        autoOpen: false,
                        height: 300,
                        width: 350,
                        position: 'justify',
                        modal: true
                    });
                    $("#applyFilterDiv").dialog({
                         autoOpen: false,
                        height: 600,
                        width: 400,
                        position: 'justify',
                        modal: true
                    })
                }
            });

            function createReport(portletId,portalTabId){
                // alert("portletId is "+portletId+" &  portalTabIdis "+portalTabId)
                var REP="";
                var CEP="";
                $.ajax({
                    url: "portalViewer.do?portalBy=savePortlet&portletId="+portletId+"&displayType=Graph",
                    success: function(data){
                        //window.opener.getPortletDetails(portletId,REP,CEP,'','',portalTabId);
                        window.close();
                        window.opener.refreshPortletDesigner('<%=ctxPath%>');
                        //parent.cancelPortletGraphTemplate(portletId,REP,CEP);
                    }
                });
            }
            function goh(){
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
            function resizeIframe(obj)
	 {
	   obj.style.height = obj.contentWindow.document.body.scrollHeight + 'px';
//	   obj.style.width = obj.contentWindow.document.body.scrollWidth + 'px';
	 }

        </script>
    </body>
</html>
<%} catch (Exception exp) {
                    exp.printStackTrace();
                }
            } else {
                response.sendRedirect(request.getContextPath() + "/pbSessionExpired.jsp");
            }%>

