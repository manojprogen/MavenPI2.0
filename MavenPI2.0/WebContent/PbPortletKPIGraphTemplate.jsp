<%--
    Document   : PbPortletKPIGraphTemplate
    Created on : Nov 17, 2009, 12:52:06 PM
    Author     : santhosh.kumar@progenbusiness.com
--%>
<%@page contentType="text/html" pageEncoding="UTF-8" import="java.text.SimpleDateFormat,java.text.DateFormat,prg.db.PbReturnObject,java.util.*,java.util.*,utils.db.ProgenConnection,prg.db.PbDb" %>


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
                String UserFldsData = "";
                if (request.getAttribute("UserFlds") != null) {
                    UserFldsData = String.valueOf(request.getAttribute("UserFlds"));
                }
           
String ctxpath1=request.getContextPath();
         
%>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>pi EE</title>

        <script src="<%=ctxpath1%>/javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=ctxpath1%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>
        <script type="text/javascript" src="<%=ctxpath1%>/javascript/jquery.tablesorter.js"></script>
        <script type="text/javascript" src="<%=ctxpath1%>/tablesorter/addons/pager/jquery.tablesorter.pager.js"></script>
<!--        <script type="text/JavaScript" src="<%=request.getContextPath()%>/javascript/jquery.columnfilters.js"></script>-->

        <script src="<%=ctxpath1%>/javascript/treeview/jquery.cookie.js" type="text/javascript"></script>
        <script src="<%=ctxpath1%>/javascript/treeview/jquery.treeview.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=ctxpath1%>/javascript/treeview/demo.js"></script>
<!--        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/draggable/ui.core.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/draggable/ui.draggable.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/draggable/ui.droppable.js"></script>

        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/ui.resizable.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/ui.accordion.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/ui.dialog.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/ui.sortable.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/effects.core.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/effects.explode.js"></script>-->

        <script type="text/javascript" src="<%=ctxpath1%>/javascript/PbPortletKPIGraphTemplateJS.js"></script>
<!--        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/chili-1.8b.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/docs.js"></script>-->
        <!--addeb by bharathi reddy fro search option-->
        <script type="text/javascript" src="<%=ctxpath1%>/javascript/quicksearch.js"></script>


        <link type="text/css" href="<%=ctxpath1%>/stylesheets/demos.css" rel="stylesheet" />
        <link type="text/css" href="<%=ctxpath1%>/stylesheets/ui.all.css" rel="stylesheet" />
        <link rel="stylesheet" href="<%=ctxpath1%>/stylesheets/treeviewstyle/jquery.treeview.css" />
        <link rel="stylesheet" href="<%=ctxpath1%>/stylesheets/treeviewstyle/screen.css" />
        <link rel="stylesheet" href="<%=ctxpath1%>/stylesheets/jq.css" type="text/css" media="print, projection, screen">
        <link rel="stylesheet" href="<%=ctxpath1%>/stylesheets/style.css" type="text/css" media="print, projection, screen">
        <link href="<%=ctxpath1%>/stylesheets/javascript.css" type="text/css" rel="stylesheet">
        <link href="<%=ctxpath1%>/stylesheets/html.css" type="text/css" rel="stylesheet">
        <link href="<%=ctxpath1%>/stylesheets/css.css" type="text/css" rel="stylesheet">

        <link href="<%=ctxpath1%>/reportDesigner.css" type="text/css" rel="stylesheet">
        <link type="text/css" href="<%=ctxpath1%>/stylesheets/metadataButton.css" rel="stylesheet" />

      
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
            .myTextbox3 {
                background-color:white;
                border-color:#848484 #999999 #999999 #848484;
                border-style:groove inset inset groove;
                border-width:1px;
                color:#000000;
                font-family:Verdana,Arial,Helvetica,sans-serif;
                font-size:8pt;
                font-weight:normal;
                margin-left:5px;
                padding:0;
                width:86px;
            }
        </style>
    </head>
    <!-- <body class="ReportTemplate" >-->
    <body class="body">


        <form action=""  name="myForm2" method="post">
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
                <script type="text/javascript">
                        getUserDims();
                        getBuckets();
                    </script>
                    <td  valign="top" width="87%" style="height:200px">
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
                            <tr id="kpiregion">
                                <td align="left" width="100%" height="180px" valign="top">
                                    <table style="height:100%" class="draggedTable" border="0" >
                                        <tr id="editTable">
                                            <td style="height:250px" valign="top">
                                                <h3  style="height:20px;width:100%" align="left"  tabindex="0" aria-expanded="true" role="tab" class="ui-accordion-header ui-helper-reset ui-state-default ui-corner-all">
                                                    &nbsp;&nbsp;<font size="2" style="font-weight:bold">KPI Graph </font>&nbsp;&nbsp;
                                                </h3>
                                                <table style="width:100%;height:220px" >
                                                    <tr>
                                                        <td width="200px" valign="top" class="draggedTable1">
                                                            <div style="height:20px" class="ui-state-default draggedDivs ui-corner-all"><font size="2" style="font-weight:bold">Drag KPI from below</font></div>
                                                            <div class="masterDiv" style="height:200px;overflow:auto">
                                                                <ul id="kpiTree" class="filetree treeview-famfamfam">
<!--                                                                    <li class="open" style="background-image:url('images/treeViewImages/plus.gif')">
                                                                        <ul id="kpisUL"  class="filetree treeview-famfamfam">-->
                                                                            <ul id="kpis">

                                                                            </ul>
<!--                                                                        </ul>
                                                                    </li>-->
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
                                            </td>
                                        </tr>
                                        <tr id="previewTable" style="display:none;">
                                            <td  style="height:250px" valign="top" >
                                                <h3  style="height:20px;width:100%" align="left"  tabindex="0" aria-expanded="true" role="tab" class="ui-accordion-header ui-helper-reset ui-state-default ui-corner-all">
                                                    <font size="2" style="font-weight:bold">KPI Table</font>&nbsp;&nbsp;<a id="prevTab"  href="javascript:EditTable()" title="Click to Edit Table" >Edit</a>
                                                </h3>
                                                <div id="previewDispTable" style="min-width:800px;min-height:230px;">
                                                </div>
                                            </td>
                                        </tr>
                                    </table>
                                </td>
                            </tr>
                            <tr id="kpigraphregion" style="display:none">
                                <td style="display:block" id="kpigraphregionupper">
                                    <h3  style="height:20px;width:100%" align="left"  tabindex="0" aria-expanded="true" role="tab" class="ui-accordion-header ui-helper-reset ui-state-default ui-corner-all">
                                        &nbsp;&nbsp;<font size="2" style="font-weight:bold">KPI Graph </font>&nbsp;&nbsp;<a id="prevTab"  href="javascript:void(0)" onclick="showChartImage()" title="Preview" >Preview</a>
                                    </h3>
                                    <div id="kpigraphEdit">
                                    </div>
                                </td>
                                <td style="display:none" id="kpigraphregionlower">
                                    <h3  style="height:20px;width:100%" align="left"  tabindex="0" aria-expanded="true" role="tab" class="ui-accordion-header ui-helper-reset ui-state-default ui-corner-all">
                                        &nbsp;&nbsp;<font size="2" style="font-weight:bold">KPI Graph </font>&nbsp;&nbsp;<a id="prevTab"  href="javascript:void(0)" onclick="enableGraphEdit()" title="Edit" >Edit</a>
                                    </h3>
                                    <div id="kpigraphPreview">
                                    </div>
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

        <table width="100%">
            <tr>
                <td height="10px">&nbsp;</td>
                <td height="10px">&nbsp;</td>
            </tr>
            <tr id="td1" style="display:block" align="center">
                <td height="10px" align="center">&nbsp;</td>
                <td align="center"><input type="button" name="next" id="next"  class="navtitle-hover" style="width:auto"  value="Next" onclick="showKPIGraph('<%=ReportId%>','<%=portalTabId%>')">&nbsp;&nbsp;<input type="button"  name="cancel" id="cancel1"  class="navtitle-hover" style="width:auto"  value="Cancel" onclick="javascript:cancelReport();"></td>
            </tr>
            <%--<tr id="td2" style="display:none">
                <td height="10px">&nbsp;</td>
                <td align="center"><input type="button" name="Next" id="Next"   class="navtitle-hover"  value="Next" onclick="showChartImageCheck()">&nbsp;&nbsp;<input type="button"  name="cancel" id="cancel2"  class="navtitle-hover"  value="Cancel" onclick="showKPIGraph('<%=ReportId%>','<%=portalTabId%>')"></td>
            </tr>--%>
            <tr id="td2" style="display:none">
                <td height="10px">&nbsp;</td>
                <td align="center"><input type="button" name="save" id="save"   class="navtitle-hover" style="width:auto"  value="Save" onclick="savePortletDetails('<%=ReportId%>','<%=portalTabId%>')">&nbsp;&nbsp;<input type="button"  name="cancel" id="cancel2"  class="navtitle-hover" style="width:auto"  value="Cancel" onclick="javascript:cancelReport();"></td>
            </tr>
        </table>
        <div id="fadestart" class="black_start"></div>


        <div id="measureTypeDiv" style="display:none">
                <table>
                    <tr>
                        <td class="tdstyle">Measure Type</td>
                        <td>
                            <select id="MeasId">
                                <option value="Standard">Standard  </option>
                                <option value="Non-Standard"> Non-Standard</option>
                            </select>
                        </td>
                    </tr>
                    <tr></tr>
                    <tr>
                        <td class="tdstyle">   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Basis</td>
                        <td>
                            <select id="selectBasis">
                                <option value="absolute">Absolute  </option>
                                <option value="deviation">Deviation%</option>
                            </select>
                        </td>
                    </tr>
                </table>
                <br><br>
                <table align="center">
                    <tr>
                        <td align="center"><input type="button" value="Next" class="navtitle-hover" onclick="showTargetDiv()"/></td>
                    </tr>
                </table>
            </div>

        <div id="targetDiv" style="display:none">
      
        </div>
          <script type="text/javascript" >
            var repotId='<%=ReportId%>'
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
////                  $("#kpisUL").treeview({
////                    animated:"slow",
////                    persist: "cookie"
////                });


            $("#measureTypeDiv").dialog({
            bgiframe: true,
            autoOpen: false,
            height: 400,
            width: 440,
            modal: true,
            position:'justify',
            title:'Choose measure type'
        });

             $("#targetDiv").dialog({
            bgiframe: true,
            autoOpen: false,
            height: 400,
            width: 440,
            modal: true,
            position:'justify',
            title:'Enter target'
        });
            });
            function showKPIGraph(portletId,portalTabId){

                document.getElementById("td1").style.display='none';
                document.getElementById("td2").style.display='';
                //document.getElementById("td3").style.display='none';
                //document.getElementById("kpiregion").style.display='none';
                //document.getElementById("kpigraphregion").style.display='';
                document.getElementById("kpigraphEdit").innerHTML='<center><img id="imgId" src="images/ajax.gif" align="middle"  width="75px" height="75px"  style="position:absolute" ></center>';
                var REP="";
                var CEP="";
                $.ajax({
                    url: "portalViewer.do?portalBy=displayKPIGraphRegion&portletId="+portletId+"&displayType=Graph",
                    success: function(data){
                        document.getElementById("kpigraphEdit").innerHTML=data;
                        //window.opener.getPortletDetails(portletId,REP,CEP,'','',portalTabId);
                        //window.close();
                         $("#measureTypeDiv").dialog('open');
                    }
                });
               

            }
            function cancelReport(portletId){
                $.ajax({
                    url: "portalViewer.do?portalBy=cancelPortlet&portletId="+portletId,
                    success: function(data){
                        window.close();
                    }
                });
            }
            //code related tokpi graph

            function populateValue(){
                var hr = document.getElementById("box1h").value;
                var mr = document.getElementById("mediumRisk").value;
                if(mr=="between"){
                    document.getElementById("mediumbox1").value = hr;
                }
                else{
                    document.getElementById("mediumbox1m").value = hr;
                }
            }
            function populateValuedh(){
                var hr = document.getElementById("box2").value;
                var mr = document.getElementById("mediumRisk").value;
                if(mr=="between"){
                    document.getElementById("mediumbox1").value = hr;
                }
                else{
                    document.getElementById("mediumbox1m").value = hr;
                }
            }
            function populateValuem(){
                var mr = document.getElementById("mediumbox1m").value;
                var lr = document.getElementById("lowRisk").value;
                if(lr=="between"){
                    document.getElementById("lowbox1").value = mr;
                }
                else{
                    document.getElementById("lowbox1l").value = mr;
                }
            }
            function populateValuedm(){
                var mr = document.getElementById("mediumbox2").value;
                var lr = document.getElementById("lowRisk").value;
                if(lr=="between"){
                    document.getElementById("lowbox1").value = mr;
                }
                else{
                    document.getElementById("lowbox1l").value = mr;
                }
            }
            function addHighRisk(risk){
                if(risk.value=="between"){
                    document.getElementById('doubleRisk').style.display='';
                    document.getElementById('singleRisk').style.display='none';
                }
                else{
                    document.getElementById('doubleRisk').style.display='none';
                    document.getElementById('singleRisk').style.display='';
                }
            }
            function addMediumRisk(risk){
                if(risk.value=="between"){
                    document.getElementById('mediumdoubleRisk').style.display='';
                    document.getElementById('mediumsingleRisk').style.display='none';
                }
                else{
                    document.getElementById('mediumdoubleRisk').style.display='none';
                    document.getElementById('mediumsingleRisk').style.display='';
                }
            }
            function addLowRisk(risk)
            {
                if(risk.value=="between")
                {
                    document.getElementById('lowdoubleRisk').style.display='';
                    document.getElementById('lowsingleRisk').style.display='none';
                }
                else{

                    document.getElementById('lowdoubleRisk').style.display='none';
                    document.getElementById('lowsingleRisk').style.display='';
                }

            }
            function showChartImageCheck(){

                var hrVal = document.getElementById("highRisk").value;
                var hr1;
                var hr2;
                var mr1;
                var mr2;
                var lr1;
                var lr2;
                var counth=0;
                var countm=0;
                var countl=0;
                if(hrVal=="between")
                {
                    hr1 = document.getElementById("box1").value;
                    hr2 = document.getElementById("box2").value;
                    if(hr1=="" || hr2==""){
                        counth=0;
                    }else{
                        counth=1;
                    }
                }
                else{
                    hr1 = document.getElementById("box1h").value;
                    hr2 = 0;
                    if(hr1==""){
                        counth=0;
                    }else{
                        counth=1;
                    }
                }
                var mrVal = document.getElementById("mediumRisk").value;
                if(mrVal=="between")
                {
                    mr1 = document.getElementById("mediumbox1").value;
                    mr2 = document.getElementById("mediumbox2").value;
                    if(mr1=="" || mr2==""){
                        countm=0;
                    }else{
                        countm=1;
                    }
                }
                else
                {
                    mr1 = document.getElementById("mediumbox1m").value;
                    mr2 = 0;
                    if(mr1==""){
                        countm=0;
                    }else{
                        countm=1;
                    }
                }

                var lrVal = document.getElementById("lowRisk").value;
                if(lrVal=="between")
                {
                    lr1 = document.getElementById("lowbox1").value;
                    lr2 = document.getElementById("lowbox2").value;
                    if(lr1=="" || lr2==""){
                        countl=0;
                    }else{
                        countl=1;
                    }
                }
                else
                {
                    lr1 = document.getElementById("lowbox1l").value;
                    lr2 = 0;
                    if(lr1==""){
                        countl=0;
                    }else{
                        countl=1;
                    }
                }
                if(countl==1&&countm==1&&counth==1){
                    return "True";
                }else{
                    alert('Please enter all range values');
                    return "False";
                }
            }
            function showChartImage(){

                var res=showChartImageCheck();
                var kpigraphEditObj=document.getElementById("kpigraphEdit");
                var kpigraphPreviewObj=document.getElementById("kpigraphPreview");

                var kpigraphregionupperObj=document.getElementById("kpigraphregionupper");
                var kpigraphregionlowerObj=document.getElementById("kpigraphregionlower");

                var hrVal = document.getElementById("highRisk").value;
                var hr1;
                var hr2;
                var mr1;
                var mr2;
                var lr1;
                var lr2;

                var grpType="Meter";
                var grpTypeOBj=document.getElementById("kpigrpTypes");
                var measType=$("select#MeasId").val();


                if(res=="True"){
                    kpigraphregionupperObj.style.display='none';
                    kpigraphregionlowerObj.style.display='';
                    kpigraphPreviewObj.innerHTML="<center><img id=\"imgId\" src=\"images/ajax.gif\" align=\"middle\"  width=\"50px\" height=\"50px\"></center>";


                    if(hrVal=="between"){
                        hr1 = document.getElementById("box1").value;
                        hr2 = document.getElementById("box2").value;
                    }
                    else{
                        hr1 = document.getElementById("box1h").value;
                        hr2 = 0;
                    }

                    var mrVal = document.getElementById("mediumRisk").value;
                    if(mrVal=="between"){
                        mr1 = document.getElementById("mediumbox1").value;
                        mr2 = document.getElementById("mediumbox2").value;
                    }
                    else{
                        mr1 = document.getElementById("mediumbox1m").value;
                        mr2 = 0;
                    }
                    var lrVal = document.getElementById("lowRisk").value;
                    if(lrVal=="between"){
                        lr1 = document.getElementById("lowbox1").value;
                        lr2 = document.getElementById("lowbox2").value;
                    }
                    else{
                        lr1 = document.getElementById("lowbox1l").value;
                        lr2 = 0;
                    }
                    if(grpTypeOBj!=null){
                        for(var i=0;i<grpTypeOBj.length;i++){
                            if(grpTypeOBj[i].selected){
                                grpType=grpTypeOBj[i].value;
                            }
                        }
                    }
                    $.ajax({
                        url: 'portalViewer.do?portalBy=displayKPIGraph&hr1='+hr1+'&hr2='+hr2+'&mr1='+mr1+'&mr2='+mr2+'&lr1='+lr1+'&lr2='+lr2+'&kpigrpType='+grpType+'&needleStr='+document.getElementById("needleStr").value+'&portletId='+document.getElementById("REPORTID").value+'&measType='+measType,
                        success: function(data) {
                            if(data != ""){
                                kpigraphPreviewObj.innerHTML=data;
                            }
                        }
                    });
                }
                else{

                }
            }
            function enableGraphEdit(){
                var kpigraphEditObj=document.getElementById("kpigraphEdit");
                var kpigraphPreviewObj=document.getElementById("kpigraphPreview");
                var kpigraphregionupperObj=document.getElementById("kpigraphregionupper");
                var kpigraphregionlowerObj=document.getElementById("kpigraphregionlower");

                kpigraphregionupperObj.style.display='';
                kpigraphregionlowerObj.style.display='none';
            }

            function savePortletDetails(portletId,portalTabId){
                var REP="";
                var CEP="";
                var hrVal = document.getElementById("highRisk").value;
                var hr1;
                var hr2;
                var mr1;
                var mr2;
                var lr1;
                var lr2;
                var daytargetVal=$("#tgtValue").val();
                var measureName=$("#measName").val();
                var grpType="Meter";
                var grpTypeOBj=document.getElementById("kpigrpTypes");
                var measType=$("select#MeasId").val();

                if(hrVal=="between")
                {
                    hr1 = document.getElementById("box1").value;
                    hr2 = document.getElementById("box2").value;
                    if(hr1=="" || hr2==""){
                        counth=0;
                    }else{
                        counth=1;
                    }
                }
                else{
                    hr1 = document.getElementById("box1h").value;
                    hr2 = 0;
                    if(hr1==""){
                        counth=0;
                    }else{
                        counth=1;
                    }
                }
                var mrVal = document.getElementById("mediumRisk").value;
                if(mrVal=="between")
                {
                    mr1 = document.getElementById("mediumbox1").value;
                    mr2 = document.getElementById("mediumbox2").value;
                    if(mr1=="" || mr2==""){
                        countm=0;
                    }else{
                        countm=1;
                    }
                }
                else
                {
                    mr1 = document.getElementById("mediumbox1m").value;
                    mr2 = 0;
                    if(mr1==""){
                        countm=0;
                    }else{
                        countm=1;
                    }
                }

                var lrVal = document.getElementById("lowRisk").value;
                if(lrVal=="between")
                {
                    lr1 = document.getElementById("lowbox1").value;
                    lr2 = document.getElementById("lowbox2").value;
                    if(lr1=="" || lr2==""){
                        countl=0;
                    }else{
                        countl=1;
                    }
                }
                else
                {
                    lr1 = document.getElementById("lowbox1l").value;
                    lr2 = 0;
                    if(lr1==""){
                        countl=0;
                    }else{
                        countl=1;
                    }
                }
                if(grpTypeOBj!=null){
                    for(var i=0;i<grpTypeOBj.length;i++){
                        if(grpTypeOBj[i].selected){
                            grpType=grpTypeOBj[i].value;
                        }
                    }
                }
                if(countl==1&&countm==1&&counth==1){
                    $.ajax({
                        url: "portalViewer.do?portalBy=savePortlet&portletId="+portletId+"&displayType=KPI Graph&hr1="+hr1+'&hr2='+hr2+'&mr1='+mr1+'&mr2='+mr2+'&lr1='+lr1+'&lr2='+lr2+'&kpigrpType='+grpType+'&needleStr='+document.getElementById("needleStr").value+'&measType='+measType+'&daytargetVal='+daytargetVal+'&measureName='+measureName,
                        success: function(data){
                            //window.opener.getPortletDetails(portletId,REP,CEP,'','',portalTabId);
                            window.close();
                            window.opener.refreshPortletDesigner('<%=ctxPath%>');
                        }
                    });
                }else{
                    alert('Please enter all range values');
                }
            }


             function showTargetDiv()
            {
                var measId=$("select#MeasId").val();
                var basis=$("select#selectBasis").val();
                var html="<br><br><table align='center'><tr><td><font style='font-weight:bold'>";
                html=html+$("#measName").val()+"</font>  value is: "+$("#needleStr").val()+" for "+$("#noOfDays").val()+"days</td></tr>";
                html=html+"<tr><td><font style='font-weight:bold'>"+$("#measName").val()+"</font>  value is: "+$("#perDay").val()+" per day</td></tr>";
                if(basis=='deviation')
                    {
                        html=html+"<br><tr><td><font style='font-weight:bold'>Period     </font><font style='font-weight:bold'>Target value</font></td></tr>";
                        html=html+"<tr><td>Day        <input type='text' id='tgtValue' name='tgtValue' value=''/></td></tr>";
                    }
                html=html+"<br><tr><td><input type='button' class='navtitle-hover' value='Next' onclick='showKpiTargetValue()'></td></tr>"
                html=html+ " </table>";
                $("#targetDiv").html(html);
                $("#targetDiv").dialog('open');
            }

            function showKpiTargetValue()
            {
                 var targetVal=$("#tgtValue").val()*$("#noOfDays").val();
                 var basis=$("select#selectBasis").val();
                 var measType=$("select#MeasId").val();
                 if(measType=='Non-Standard')
                     {
                         $("#r1Text").html("Low Risk");
                         $("#r2Text").html("High Risk");
                     }
                     else
                         {
                          $("#r1Text").html("High Risk");
                          $("#r2Text").html("Low Risk");
                         }
                  $.ajax({
                        url: "portalViewer.do?portalBy=getKpiTargetDeviation&targetVal="+targetVal+"&actualValue="+$("#needleStr").val(),
                        success: function(data){
                            var html=$("#measName").val();
                            if(basis=='deviation')
                                {
                                html=html+" Deviation value(in %) is "+data;
                                $("#needleStr").val(data)
                                }
                            else
                                {
                                html=html+" Absolute value is "+$("#needleStr").val();
                                $("#devId").html(" Value");
                                }
                            
                            $("#needleValue").html(html);
                        }
                    });
                 $("#measureTypeDiv").dialog('close');
                 $("#targetDiv").dialog('close');
                document.getElementById("kpiregion").style.display='none';
                document.getElementById("kpigraphregion").style.display='';
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


