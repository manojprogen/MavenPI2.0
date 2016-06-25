<%--
    Document   : pbDashboardTemplate
    Created on : Sep 9, 2009, 1:57:19 PM
    Author     : mahesh.sanampudi@progenbusiness.com
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" import="java.util.*" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<%@taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<%      //for clearing cache
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
        String dashboardName = "";
        if (request.getSession(false) != null && request.getSession(false).getAttribute("DashboardHashMap") != null) {
            dashboardName = (String) ((HashMap) request.getSession(false).getAttribute("DashboardHashMap")).get("DashboardName");
            //////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("dashboardName-----"+dashboardName);
             }
        String loguserId = String.valueOf(session.getAttribute("USERID"));
        if(dashboardName.equalsIgnoreCase("")){
           dashboardName=String.valueOf(session.getAttribute("dashboardName"));
         }
        if(dashboardName.equalsIgnoreCase("")){
           dashboardName=request.getParameter("pagename");
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
      <!--  <script type="text/javascript" src="<%=contextPath%>/javascript/draggable/ui.core.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/draggable/ui.draggable.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/draggable/ui.droppable.js"></script>

        <script type="text/javascript" src="<%=contextPath%>/javascript/ui.resizable.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/ui.accordion.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/ui.dialog.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/ui.sortable.js"></script>-->
        <script type="text/javascript" src="<%=contextPath%>/javascript/effects.core.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/effects.explode.js"></script>

        <script type="text/javascript" src="<%=contextPath%>/javascript/dashboardDesign.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/chili-1.8b.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/docs.js"></script>


        <link type="text/css" href="<%=contextPath%>/stylesheets/demos.css" rel="stylesheet" />
        <link type="text/css" href="<%=contextPath%>/stylesheets/ui.all.css" rel="stylesheet" />
        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/treeviewstyle/jquery.treeview.css" />
        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/treeviewstyle/screen.css" />
        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/jq.css" type="text/css" media="print, projection, screen">
        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/style.css" type="text/css" media="print, projection, screen">
        <link href="<%=contextPath%>/stylesheets/javascript.css" type="text/css" rel="stylesheet">
        <link href="<%=contextPath%>/stylesheets/html.css" type="text/css" rel="stylesheet">
        <link href="<%=contextPath%>/stylesheets/css.css" type="text/css" rel="stylesheet">
        <link rel="stylesheet" type="text/css" href="<%=contextPath%>/stylesheets/tablesorterStyle.css" />

        <link href="<%=contextPath%>/reportDesigner.css" type="text/css" rel="stylesheet">
            <link type="text/css" href="<%=contextPath%>/stylesheets/metadataButton.css" rel="stylesheet" />
 <script>
            $(document).ready(function() {
                $("#dashbrdTree").treeview({
                    animated: "normal",
                    unique:true
                });

                $(".column").sortable({
                    connectWith: '.column'
                });

                $(".portlet").addClass("ui-widget ui-widget-content ui-helper-clearfix ui-corner-all")
                .find(".portlet-header")
                .addClass("ui-widget-header ui-corner-all")
                .prepend('<span class="ui-icon ui-icon-plusthick"></span>')
                .end()
                .find(".portlet-content");

                $(".portlet-header .ui-icon").click(function() {
                    $(this).toggleClass("ui-icon-minusthick");
                    $(this).parents(".portlet:first").find(".portlet-content").toggle();
                });

                $(".column").disableSelection();

            });
             </script>
       
        <style type="text/css">
            .column { width: 170px; float: left; padding-bottom: 100px; }
            .portlet { margin: 0 1em 1em 0; }
            .portlet-header { margin: 0.3em; padding-bottom: 4px; padding-left: 0.2em; }
            .portlet-header .ui-icon { float: right; }
            .portlet-content { padding: 0.4em; }
            .ui-sortable-placeholder { border: 1px dotted black; visibility: visible !important; height: 50px !important; }
            .ui-sortable-placeholder * { visibility: hidden; }
        </style>
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
    </head>
    <!-- <body class="ReportTemplate" >-->
    <body onload="formatStr(document.getElementById('dashName'),30);">

        <%
        String UserFldsData = "";
        if (request.getAttribute("UserFlds") != null) {
            UserFldsData = String.valueOf(request.getAttribute("UserFlds"));
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("UserFldsData is " + UserFldsData);
        }


        %>



                         <table style="width:100%;">
                            <tr>
                                <td valign="top" style="width:100%;">
                                    <jsp:include page="Headerfolder/headerPage.jsp"/>
                                </td>
                            </tr>
                        </table>


        <form name="myForm2" method="post">
            <table width="100%" >
     
                <tr style="height:15px;width:100%;max-height:100%">
                    <td>
                        <table width="100%" class="ui-corner-all">
                            <tr>
                                <td valign="top" style="width:90%" >
                                    <span id="dashName" style="color: #4F4F4F;font-family:verdana;font-size:15px;font-weight:bold"  title="<%=dashboardName%>"><%=dashboardName%></span>
                                </td>
                                <td valign="top" style="height:10px;width:10%" align="right">
<!--                                    <a href="javascript:void(0)" onclick="javascript:gohome('<%=loguserId%>')" style="font-size:10px;color:#369;font-weight:bold;text-decoration: none;font-family:Georgia"> Home </a> |-->
<!--                                    <a href="#" style="font-size:10px;color:#369;font-weight:bold;text-decoration: none;font-family:Georgia"> Help </a> |-->
<!--                                    <a href="javascript:void(0)" onclick="javascript:logout()" style="font-size:10px;color:#369;font-weight:bold;text-decoration: none;font-family:Georgia"> Logout </a>-->
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>
            <table style="width:100%;height:100%;overflow:scroll" border="solid black 1px" >
                <tr>
                    <td width="13%" valign="top" class="draggedTable1" >
                        <div style="height:20px" class="ui-state-default draggedDivs ui-corner-all">
                            <font style="font-weight:bold" face="verdana" size="1px">Dashboard Designer</font>
                        </div>
                        <div class="masterDiv">
                            <ul id="dashbrdTree" class="filetree treeview-famfamfam">
                                <li class="closed" ><img src="icons pinvoke/folder.png"><span>Business Roles</span>
                                    <ul id="userFlds">
                                        <%=UserFldsData%>
                                    </ul>
                                </li>
                                <li class="closed"><img src="images/treeViewImages/cabin_with_smog_pipe.png"><span>Dimensions</span>
                                    <ul id="userDims">
                                    </ul>
                                </li>
                            </ul>
                        </div>
                    </td>
                    <script>
                        getUserDims();
                    </script>
                    <td  valign="top" width="87%" colspan="2" >
                        <table style="height:auto;max-height:100%;width:100%" border="0">
                            <tr>
                                <td valign="top" width="100%" height="100px" colspan="2">
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
                                                <div id="paramDisp" style="width:800px;height:80px;">

                                                </div>
                                            </td>
                                        </tr>
                                    </table>
                                </td>
                            </tr>
                            <tr>
                                <td align="left" width="43%" height="180px" valign="top">
                                    <table style="height:100%;width:100%" border="0">
                                        <tr id="editKpi">
                                            <td valign="top" style="height:100%;width:610px">

                                                <h3 style="height:20px" align="left" tabindex="0" aria-expanded="true" role="tab" class="ui-accordion-header ui-helper-reset ui-state-default ui-corner-all">
                                                    <font size="2" style="font-weight:bold">KPI Region </font>&nbsp;&nbsp;<a href="javascript:void(0)" onclick="showKpis()">Add KPI</a>
                                                </h3>
                                                <div id="editDispKpi" style="width:99%;height:275px;border:solid;border-color:silver">
                                                </div>
                                            </td>
                                        </tr>
                                        <tr id="previewKpiTable" style="display:none">
                                            <td  style="height:100%;width:610px" valign="top" >

                                                <h3 style="height:20px" align="left" tabindex="0" aria-expanded="true" role="tab" class="ui-accordion-header ui-helper-reset ui-state-default ui-corner-all">
                                                    <font size="2" style="font-weight:bold">KPI Region</font>&nbsp;&nbsp;<a href="javascript:void(0)" id="editKpi" title="Edit Kpi" onclick="editKpis()">Edit</a>
                                                </h3>

                                                <div id="previewDispKpi" style="width:99%;height:275px;border:solid;border-color:silver">

                                                </div>

                                            </td>
                                        </tr>
                                    </table>
                                </td>

                                <td align="left" width="43%" height="180px" valign="top" >
                                    <table style="height:100%;width:100%;" border="0" >
                                        <tr id="editGrp1">
                                            <td align="center" valign="top" style="height:100%">

                                                <h3 style="height:20px" align="left" tabindex="0" aria-expanded="true" role="tab" class="ui-accordion-header ui-helper-reset ui-state-default ui-corner-all">
                                                    <font size="2" style="font-weight:bold">Graph Region </font>&nbsp;&nbsp;<a href="javascript:void(0)" onclick="showGraphs('dispGrp1')">Add / Edit Graph</a>
                                                    &nbsp;&nbsp;<a href="javascript:void(0)" onclick="showKpiGraphs('dispGrp1')">Add KPI Graph</a>
                                                    &nbsp;&nbsp;<a href="javascript:void(0)" onclick="createDbrdGraphs('dispGrp1')">Create Graph</a>
                                                </h3>
                                                <div id="dispGrp1" style="width:99%;height:275px;border:solid;border-color:silver">
                                                </div>
                                            </td>
                                        </tr>
                                    </table>
                                </td>
                            </tr>

                            <tr>
                                <td  align="left" width="43%" height="180px" valign="top">
                                    <table style="height:100%;width:100%;" class="draggedTable" border="0" >
                                        <tr id="editGrp2">
                                            <td  align="center" valign="top" style="height:100%">

                                                <h3 style="height:20px" align="left" tabindex="0" aria-expanded="true" role="tab" class="ui-accordion-header ui-helper-reset ui-state-default ui-corner-all">
                                                    <font size="2" style="font-weight:bold">Graph Region </font>&nbsp;&nbsp;<a href="javascript:void(0)" onclick="showGraphs('dispGrp2')">Add / Edit Graph</a>
                                                &nbsp;&nbsp;<a href="javascript:void(0)" onclick="showKpiGraphs('dispGrp2')">Add KPI Graph</a>
                                                 &nbsp;&nbsp;<a href="javascript:void(0)" onclick="createDbrdGraphs('dispGrp2')">Create Graph</a>
                                                </h3>
                                                <div id="dispGrp2" style="width:99%;height:275px;border:solid;border-color:silver">
                                                </div>
                                            </td>
                                        </tr>
                                    </table>
                                </td>

                                <td align="left" width="43%" height="180px" valign="top">
                                    <table style="height:100%;width:100%;" class="draggedTable" border="0" >
                                        <tr id="editGrp3">
                                            <td align="center" valign="top" style="height:100%">

                                                <h3 style="height:20px" align="left" tabindex="0" aria-expanded="true" role="tab" class="ui-accordion-header ui-helper-reset ui-state-default ui-corner-all">
                                                    <font size="2" style="font-weight:bold">Graph Region </font>&nbsp;&nbsp;<a href="javascript:void(0)" onclick="showGraphs('dispGrp3')">Add / Edit Graph</a>
                                                  &nbsp;&nbsp;<a href="javascript:void(0)" onclick="showKpiGraphs('dispGrp3')">Add KPI Graph</a>
                                                 &nbsp;&nbsp;<a href="javascript:void(0)" onclick="createDbrdGraphs('dispGrp3')">Create Graph</a>
                                                </h3>
                                                <div id="dispGrp3" style="width:99%;height:275px;border:solid;border-color:silver">
                                                </div>
                                            </td>
                                        </tr>
                                    </table>
                                </td>
                            </tr>

                            <tr>
                                <td align="left" width="43%" height="180px" valign="top">
                                    <table style="height:100%;width:100%" class="draggedTable" border="0" >
                                        <tr id="editGrp4">
                                            <td align="center" valign="top" style="height:100%">

                                                <h3 style="height:20px" align="left" tabindex="0" aria-expanded="true" role="tab" class="ui-accordion-header ui-helper-reset ui-state-default ui-corner-all">
                                                    <font size="2" style="font-weight:bold">Graph Region </font>&nbsp;&nbsp;<a href="javascript:void(0)" onclick="showGraphs('dispGrp4')">Add / Edit Graph</a>
                                                 &nbsp;&nbsp;<a href="javascript:void(0)" onclick="showKpiGraphs('dispGrp4')">Add KPI Graph</a>
                                                 &nbsp;&nbsp;<a href="javascript:void(0)" onclick="createDbrdGraphs('dispGrp4')">Create Graph</a>
                                                </h3>
                                                <div id="dispGrp4" style="width:99%;height:275px;border:solid;border-color:silver">
                                                </div>
                                            </td>
                                        </tr>
                                    </table>
                                </td>

                                <td align="left" width="43%" height="180px" valign="top">
                                    <table style="height:100%;width:100%" class="draggedTable" border="0" >
                                        <tr id="editGrp5">
                                            <td align="center" valign="top" style="height:100%">
                                                <h3 style="height:20px" align="left" tabindex="0" aria-expanded="true" role="tab" class="ui-accordion-header ui-helper-reset ui-state-default ui-corner-all">
                                                    <font size="2" style="font-weight:bold">Graph Region </font>&nbsp;&nbsp;<a href="javascript:void(0)" onclick="showGraphs('dispGrp5')">Add / Edit Graph</a>
                                                  &nbsp;&nbsp;<a href="javascript:void(0)" onclick="showKpiGraphs('dispGrp5')">Add KPI Graph</a>
                                                 &nbsp;&nbsp;<a href="javascript:void(0)" onclick="createDbrdGraphs('dispGrp5')">Create Graph</a>
                                                </h3>
                                                <div id="dispGrp5" style="width:99%;height:275px;border:solid;border-color:silver">

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
            <div id="graphs" style="display:none">
                <iframe id="dataDispmem" NAME='dataDispmem' style="width:700px" class="white_content1" SRC=''></iframe>
            </div>
            <div id="kpis" style="display:none">
                <iframe id="kpidataDispmem" NAME='dataDispmem' style="width:700px" class="white_content1" SRC=''></iframe>
            </div>
             <div id="kpisGraphs" style="display:none">
                <iframe id="kpigraphdataDispmem" NAME='kpigraphdataDispmem' style="width:700px" class="white_content1" SRC=''></iframe>
            </div>
            <div id="kpiDrill" style="display:none">
                <iframe id="kpiDrillDispmem" NAME='dataDispmem' style="width:700px" class="white_content1" SRC=''></iframe>
            </div>
            <input type="hidden" name="allGraphIds" value="" id="allGraphIds">
            <input type="hidden" name="REPIds" value="" id="REPIds">
            <input type="hidden" name="CEPIds" value="" id="CEPIds">
            <input type="hidden" name="MsrIds" value="" id="MsrIds">
            <input type="hidden" name="divId" value="" id="divId">
            <input type="hidden" name="kpis" value="" id="kpis">
            <input type="hidden" name="kpiIds" value="" id="kpiIds">
            <input type="hidden" name="allGrDetails" value="" id="allGrDetails">
        </form>

        <div id="fade" class="black_overlay"></div>

        <div id="graphList" style="display:none" class="white_content1">
            <table>
                <tr>
                    <td><img src="graphImages/Bar.gif" alt="BAR" height="100" width="100px" onclick="getDbrdGraphColumns(this,'Bar')">&nbsp;</td>
                    <td><img src="graphImages/Bar3D.gif" alt="BAR3D" height="100" width="100px" onclick="getDbrdGraphColumns(this,'Bar3D')">&nbsp;</td>
                    <td><img src="graphImages/Dual Axis.gif" alt="Dual Axis" height="100" width="100px" onclick="getDbrdGraphColumns(this,'Dual Axis')">&nbsp;</td>
                    <td><img src="graphImages/Line.gif" alt="Line" height="100" width="100px"  onclick="getDbrdGraphColumns(this,'Line')">&nbsp;</td>
                </tr>
                <tr>
                    <td align="center">BAR</td>
                    <td align="center">BAR3D</td>
                    <td align="center">Dual Axis</td>
                    <td align="center">Line</td>
                </tr>
                <tr>
                    <td><img src="graphImages/Pie.gif" alt="Pie" height="100" width="100" onclick="getDbrdGraphColumns(this,'Pie')">&nbsp;</td>
                    <td><img src="graphImages/Pie3D.gif" alt="Pie3D" height="100" width="100" onclick="getDbrdGraphColumns(this,'Pie3D')">&nbsp;</td>
                    <td><img src="graphImages/Ring.gif" alt="Ring" height="100" width="100" onclick="getDbrdGraphColumns(this,'Ring')"></td>
                </tr>
                <tr>
                    <td align="center">Pie</td>
                    <td align="center">Pie3D</td>
                    <td align="center">Ring</td>
                </tr>
            </table>

            <center><input type="button" class="navtitle-hover" style="width:auto" value="Cancel" onclick="document.getElementById('graphList').style.display='none';document.getElementById('fade').style.display='none'"></center>

        </div>
        <!--style="cursor:hand;text-decoration:none" -->
        <div style="display:none">
            <div id="tab" style="display:block" onmouseover="getColDivs(1)">
                <table id="" width="100%">
                    <tr>
                        <td>
                            <a href="javascript:void(0)" onclick="getGraphTypes('1')" >Graph Types</a><div id="getGraphTypes"></div>
                            <a href="javascript:void(0)" onclick="getGraphTypes('2')"  >Graph Sizes</a><div id="getGraphSizes"></div>
                            <a href="javascript:void(0)" onclick="getColumns()"  >Graph Columns</a>
                        </td>
                    </tr>
                </table>
            </div>
            <table id="tab11"  style="display:block" width="100%" onmouseover="getColDivs(2)">
                <tr>
                    <td>
                        <a href="javascript:void(0)" onclick="getGraphTypes('1')" >Graph Types</a><div id="getGraphTypes11"></div>
                        <a href="javascript:void(0)" onclick="getGraphTypes('2')" >Graph Sizes</a><div id="getGraphSizes11"></div>
                        <a href="javascript:void(0)" onclick="getColumns()" >Graph Columns</a>
                    </td>
                </tr>
            </table>

        </div>
        <input type="hidden"  id="h" value="<%=request.getContextPath()%>">

        <div>
            <iframe  id="graphCols" NAME='bucketDisp'  STYLE='display:none;'   class="white_content1" SRC=''></iframe>
        </div>

        <table width="100%" >
            <tr>
                <td height="10px">&nbsp;</td>
                <td height="10px">&nbsp;</td>
            </tr>
            <tr>
                <td height="10px">&nbsp;</td>
                <td align="center"><input type="button" class="navtitle-hover" style="width:auto" value="Next" onclick="createDashboard()">&nbsp;&nbsp;<input type="button" value="Cancel" class="navtitle-hover" style="width:auto" onclick="javascript:cancelDashboard();"></td>
            </tr>
            <tr>
                <td height="10px">&nbsp;</td>
                <td height="10px">&nbsp;</td>
            </tr>
        </table>
        <table width="100%" class="fontsty" style="background-color:#bdbdbd">
            <tr class="fontsty" style="height:10px;width:100%;max-height:100%;background-color:#bdbdbd">
                <td style="height:10px;width:100%;background-color:#bdbdbd" >
                    <center style="background-color:#bdbdbd"><font  style="color:#fff;font-family:verdana;font-size:10px;font-weight:normal" align="center">Copyright © 2009-12 <a href="http://www.progenbusiness.com" style="color:red;font-weight:bold;font-size:10px;font-family:verdana">Progen Business Solutions.</a> All Rights Reserved</font></center>
                </td>
            </tr>
        </table>
 <script>
            


            function createDashboard()
            {
                document.myForm2.action="dashboardTemplateAction.do?templateParam2=saveDashboard";
                document.myForm2.submit();
            }
            var graphCount=0;
            var graphName='';
            var typesDisplayStatus = '0';
            var sizeDisplayStatus = '0';
            var currentGraphId = '0';
            var divStatus = '0';

            //added by susheela
            var allGrDetails;

            function hideDivs()
            {
                //document.getElementById("getGraphTypes").style.display='none';
                //document.getElementById("getGraphSizes").style.display='none';
            }

            function buildFldIds(){
                var fldObj=document.getElementsByName("userfldsList");
                var foldersIds="";

                if(fldObj.length!=0){
                    for(var i=0;i<fldObj.length;i++){
                        if(fldObj[i].checked){
                            foldersIds=foldersIds+","+fldObj[i].id;
                        }
                    }
                }
                if (foldersIds!=""){
                    foldersIds=foldersIds.substr(1,foldersIds.length);
                }
                return foldersIds;
            }

            function getUserDims(){
                var foldersIds=buildFldIds();
                var branches="";
                var str;

                if(foldersIds!=""){
                    $.ajax({
                        url: 'dashboardTemplateAction.do?templateParam2=getUserDims&foldersIds='+foldersIds,
                        success: function(data) {
                            $("#userDims").html("");
                            str=data;
                            branches = $(str).appendTo("#userDims");
                            $("#userDims").treeview({
                                add: branches
                            });

                            var dragUserDim=$('#userDims > li >span');
                            var draggableDims=$('#draggableDims');

                            $(dragUserDim).draggable({
                                helper:"clone",
                                effect:["", "fade"]
                            });

                            $(draggableDims).droppable(
                            {
                                activeClass:"blueBorder",
                                accept:'#userDims > li >span',
                                drop: function(ev, ui) {
                                    var dim=ui.draggable.html();
                                    // add for time dimension
                                    if(dim=='Time-Period Basis'){
                                        var timeDim='AS_OF_DATE,PRG_PERIOD_TYPE,PRG_COMPARE';
                                        var timeDimension=timeDim.split(",");
                                        createParams(timeDimension,ui.draggable.html());
                                    } else if(dim=='Time-Range Basis'){
                                        var timeDim='AS_OF_DATE1,AS_OF_DATE2,CMP_AS_OF_DATE1,CMP_AS_OF_DATE2';
                                        var timeDimension=timeDim.split(",");
                                        createParams(timeDimension,ui.draggable.html());
                                    }else if(dim=='Time-Week Basis'){
                                        var timeDim='AS_OF_WEEK,AS_OF_WEEK1';
                                        var timeDimension=timeDim.split(",");
                                        createParams(timeDimension,ui.draggable.html());
                                    } else if(dim=='Time-Month Basis'){
                                        var timeDim='AS_OF_MONTH,PRG_PERIOD_TYPE,PRG_COMPARE';
                                        var timeDimension=timeDim.split(",");
                                        createParams(timeDimension,ui.draggable.html());
                                    }else if(dim=='Time-Compare Month Basis'){
                                        var timeDim='AS_OF_MONTH1,AS_OF_MONTH2';
                                        var timeDimension=timeDim.split(",");
                                        createParams(timeDimension,ui.draggable.html());
                                    } else if(dim=='Time-Quarter Basis'){
                                        var timeDim='AS_OF_QUARTER,AS_OF_QUARTER1';
                                        var timeDimension=timeDim.split(",");
                                        createParams(timeDimension,ui.draggable.html());
                                    } else if(dim=='Time-Year Basis'){
                                        var timeDim='AS_OF_YEAR,AS_OF_YEAR1';
                                        var timeDimension=timeDim.split(",");
                                        createParams(timeDimension,ui.draggable.html());
                                    }else {
                                        // end of adding
                                        getDimDetails(ui.draggable.html());
                                    }
                                }
                            });
                        }
                    });
                }
            }

//            function gohome(){
//                document.forms.myForm2.action="baseAction.do?param=goHome";
//                document.forms.myForm2.submit();
//            }
//            function logout(){
//                document.forms.myForm2.action="baseAction.do?param=logoutApplication";
//                document.forms.myForm2.submit();
//            }
            function cancelDashboard(){
                document.forms.myForm2.action="baseAction.do?param=goHome";
                document.forms.myForm2.submit();
            }

    function KpiDrilldown(elmntId){
                var frameObj=document.getElementById("kpiDrillDispmem");
                var divObj=document.getElementById("kpiDrill");
                //var source="dashboardTemplateAction.do?templateParam2=kpiDrill";
                frameObj.src="pbKPIDrillDown.jsp?kpiId="+elmntId+"&folderIds="+buildFldIds();
                frameObj.style.display='block';
                divObj.style.display='block';
                document.getElementById('fade').style.display='block';

            }

        </script>
    </body>
</html>

