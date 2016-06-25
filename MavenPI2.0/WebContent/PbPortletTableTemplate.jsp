<%-- 
    Document   : PbPortletTableTemplate
    Created on : Nov 4, 2009, 1:34:58 PM
    Author     : santhosh.kumar@progenbusiness.com
--%>
<%@page contentType="text/html" pageEncoding="UTF-8" import="java.util.*,prg.db.Container"%>

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
                    String contextPath=request.getContextPath();
%>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
         <title>pi EE</title>

        <script src="<%=contextPath%>/javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/jquery.tablesorter.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/tablesorter/addons/pager/jquery.tablesorter.pager.js"></script>
        <script language="JavaScript" src="<%=contextPath%>/javascript/jquery.columnfilters.js"></script>

        <script src="<%=contextPath%>/javascript/treeview/jquery.cookie.js" type="text/javascript"></script>
        <script src="<%=contextPath%>/javascript/treeview/jquery.treeview.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/treeview/demo.js"></script>


        <script type="text/javascript" src="<%=contextPath%>/javascript/PbPortletTableTemplateJS.js"></script>

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
        <form name="myForm2" method="post">
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
                                <li class="closed" ><img src="icons pinvoke/folder-horizontal.png"><span>Business Roles</span>
                                    <ul id="userFlds">
                                        <%=UserFldsData%>
                                    </ul>
                                </li>

                                <li class="closed"><img src="icons pinvoke/folder-horizontal.png"><span>Dimensions</span>
                                    <ul id="userDims">
                                    </ul>
                                </li>
                                <li class="closed"><img src="icons pinvoke/folder-horizontal.png"><span>Buckets</span>
                                    <ul id="userBuckets">
                                    </ul>
                                </li>
                            </ul>
                        </div>
                    </td>
                <script>
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
                                <!--  <a id="Refresh" href="javascript:showParams()" title="Click to Set Parameters" >Preview</a>-->
                            </td>
                        </tr>

                        <tr>
                            <td align="left" width="100%" height="180px" valign="top">
                                <table style="height:100%" class="draggedTable" border="0" >
                                    <tr id="editTable">
                                        <td style="height:250px" valign="top">
                                            <h3  style="height:20px;width:100%" align="left"  tabindex="0" aria-expanded="true" role="tab" class="ui-accordion-header ui-helper-reset ui-state-default ui-corner-all">
                                                <font size="2" style="font-weight:bold">Table </font>&nbsp;&nbsp;<a id="prevTab"  href="javascript:PreviewTable()" title="Preview Table" >Preview</a>
                                            </h3>
                                            <div id="tableDiv" style="width:100%;min-height:230px;">
                                                <Table>
                                                    <Tr>
                                                        <Td>
                                                            <a id="rep" href="javascript:showRowParams()" title="Click to Select Row Parameter" >Row Edge</a>&nbsp;
                                                            <div style="display:none;width:auto;height:auto;background-color:#ffffff;overflow:auto;position: absolute;text-align: left;border: 1px solid #000000;border-top-width: 0px;color:black" id="repDiv">
                                                            </div>
                                                        </Td>
                                                        <Td>
                                                            <a id="cep" href="javascript:showColParams()" title="Click to Select Column Parameter"  >Column Edge</a>&nbsp;
                                                            <div style="display:none;width:auto;height:auto;background-color:#ffffff;overflow:auto;position: absolute;text-align: left;border: 1px solid #000000;border-top-width: 0px;color:black" id="cepDiv">
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
                                    <tr id="previewTable" style="display:none;">
                                        <td  style="height:250px" valign="top" >
                                            <h3  style="height:20px;width:100%" align="left"  tabindex="0" aria-expanded="true" role="tab" class="ui-accordion-header ui-helper-reset ui-state-default ui-corner-all">
                                                <font size="2" style="font-weight:bold">Table</font>&nbsp;&nbsp;<a id="prevTab"  href="javascript:EditTable()" title="Click to Edit Table" >Edit</a>
                                            </h3>

                                            <div id="previewDispTable" style="min-width:800px;min-height:230px;overflow-x:auto">

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

        <%--<div id="TableMeasuresDIV" style="display:none" title="Table Measures" >
            <iframe id="dataDispmem" NAME='dataDispmem' width="100%" height="100%" frameborder="0" SRC=''></iframe>
        </div>--%>
        <div id="TableMeasuresDIV" title="Table Measures" >
            <iframe id="dataDispmem" NAME='dataDispmem' STYLE='display:none;'   class="white_content1" width="100%" height="100%" frameborder="0" SRC=''></iframe>
        </div>
        <%--<div>
            <iframe  id="graphCols" NAME='bucketDisp'  STYLE='display:none;'   class="white_content1" SRC=''></iframe>
        </div>
        <div>
            <iframe  id="graphDtls" NAME='graphDtls'  class="white_content1" STYLE='display:none;' SRC=''></iframe>
        </div>
        --%>

        <table width="100%" >
            <tr>
                <td height="10px">&nbsp;</td>
                <td height="10px">&nbsp;</td>
            </tr>
            <tr>
                <td height="10px">&nbsp;</td>
                <td align="center">
                    <input type="button" class="navtitle-hover" style="width:auto" value="Next" onclick="createReport('<%=ReportId%>','<%=portalTabId%>')">&nbsp;&nbsp;
                    <input type="button" class="navtitle-hover" style="width:auto" value="Cancel" onclick="cancelReport();">
                </td>
            </tr>
            <tr>
                <td height="10px">&nbsp;</td>
                <td height="10px">&nbsp;</td>
            </tr>
        </table>
                      <script>
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
                /*
                 if ($.browser.msie == true){
                    $("TableMeasuresDIV").dialog({
                        autoOpen: false,
                        height: 400,
                        width: 600,
                        position: 'justify',
                        modal: true
                    });
                }
                else{
                    $("TableMeasuresDIV").dialog({
                        autoOpen: false,
                        height: 400,
                        width: 600,
                        position: 'justify',
                        modal: true
                    });
                }
                 */
                
            });

            
            function createReport(portletId,portalTabId){
                var REP="";
                var CEP="";
                  
                $.ajax({
                    url: "portalViewer.do?portalBy=savePortlet&portletId="+portletId+"&displayType=Table",
                    success: function(data){
                        //alert("portletId is "+portletId+" &  portalTabIdis "+portalTabId)
                        //window.opener.getPortletDetails(portletId,REP,CEP,'','',portalTabId);
                        window.close();
                        window.opener.refreshPortletDesigner('<%=ctxPath%>');
                        //parent.cancelPortletTableTemplate(portletId,REP,CEP);
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

