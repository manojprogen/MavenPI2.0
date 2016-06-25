<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.*" %>
<%@page import="prg.db.Container"%>
<jsp:useBean id="brdcrmb"  scope="session" class="com.progen.action.BreadCurmbBean"/>
<%

            String loguserId = String.valueOf(session.getAttribute("USERID"));
            HashMap ParametersHashMap = null;
            HashMap TableHashMap = null;
            HashMap GraphHashMap = null;
            HashMap ReportHashMap = null;


            String scenarioId = "";
            Container container = null;
            String scenarioName = "";
            String scenarioDesc = "";
            HashMap map = new HashMap();
            String ScenarioFolders = "";
            String ScenarioDimensions = "";
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



            try {
                if (request.getSession(false) != null && request.getSession(false).getAttribute("SCENARIOTAB") != null) {
                    scenarioName = String.valueOf(request.getAttribute("scenarioName"));
                    map = (HashMap) request.getSession(false).getAttribute("SCENARIOTAB");
                    if (map.get(scenarioId) != null) {
                        container = (prg.db.Container) map.get(scenarioId);
                    } else {
                        container = new prg.db.Container();
                    }
                }

                scenarioName = (String) request.getAttribute("scenarioName");
                ////////////////////////////////////////.println.println("scenarioName in pbScenarioTemplate.jsp is:: " + scenarioName);
                scenarioDesc = (String) request.getAttribute("scenarioDesc");

%>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>pi 1.0</title>
        <script src="<%=request.getContextPath()%>/javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.tablesorter.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/tablesorter/addons/pager/jquery.tablesorter.pager.js"></script>
        <script language="JavaScript" src="<%=request.getContextPath()%>/javascript/jquery.columnfilters.js"></script>
        <script src="<%=request.getContextPath()%>/javascript/treeview/jquery.cookie.js" type="text/javascript"></script>
        <script src="<%=request.getContextPath()%>/javascript/treeview/jquery.treeview.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/treeview/demo.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/draggable/ui.core.js"></script>        
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/draggable/ui.droppable.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/draggable/ui.draggable.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/ui.accordion.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/ui.dialog.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/ui.sortable.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/effects.core.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/effects.explode.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/Scenario/JS/myScripts.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/chili-1.8b.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/docs.js"></script>

        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/demos.css" rel="stylesheet" />
        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/ui.all.css" rel="stylesheet" />
        <link rel="stylesheet" href="<%=request.getContextPath()%>/stylesheets/treeviewstyle/jquery.treeview.css" />
        <link rel="stylesheet" href="<%=request.getContextPath()%>/stylesheets/treeviewstyle/screen.css" />
        <link rel="stylesheet" href="<%=request.getContextPath()%>/stylesheets/jq.css" type="text/css" media="print, projection, screen">
        <link rel="stylesheet" href="<%=request.getContextPath()%>/stylesheets/style.css" type="text/css" media="print, projection, screen">
        <link href="<%=request.getContextPath()%>/stylesheets/javascript.css" type="text/css" rel="stylesheet">
        <link href="<%=request.getContextPath()%>/stylesheets/html.css" type="text/css" rel="stylesheet">
        <link href="<%=request.getContextPath()%>/stylesheets/css.css" type="text/css" rel="stylesheet">
        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/metadataButton.css" rel="stylesheet" />
        <script type="text/javascript" src="<%=request.getContextPath()%>/jQuery/jquery/ui/jquery.easing.1.3.js"></script>
        <link type="text/css" href="<%=request.getContextPath()%>/jQuery/jquery/themes/base/Base.css" rel="stylesheet" />
        <link href="<%=request.getContextPath()%>/reportDesigner.css" type="text/css" rel="stylesheet">
        <link rel="stylesheet" href="<%=request.getContextPath()%>/stylesheets/style.css" type="text/css" media="print, projection, screen">

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
                    $("#timerangediv").dialog({
                        autoOpen: false,
                        draggable: false,
                        height: 400,
                        width: 600,
                        position: 'justify',
                        modal: true
                    });
                    $("#seededmodeldiv").dialog({
                        autoOpen: false,
                        draggable: false,
                        height: 400,
                        width: 600,
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
                    $("#timerangediv").dialog({
                        autoOpen: false,
                        draggable: false,
                        height: 200,
                        width: 450,
                        position: 'justify',
                        modal: true
                    });
                    $("#seededmodeldiv").dialog({
                        autoOpen: false,
                        draggable: false,
                        height: 250,
                        width: 450,
                        position: 'justify',
                        modal: true
                    });
                }
            });
        </script>
        <script>
           
            $(document).ready(function() {
                $("#repTemTree").treeview({
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

            $(function() {

            });

            function createScenario()
            {               
                var path=document.getElementById('path').value;
                var scenarioName=document.getElementById('scenarioName').value;
                var scenarioDesc=document.getElementById('scenarioDesc').value;
                document.myForm2.action=path+'/ScenarioTemplateAction.do?scnTemplateParam=saveScenario&flag=analyzeScenario&scenarioName='+scenarioName+'&scenarioDesc='+scenarioDesc;
                document.myForm2.submit();
            }
            function gohome(){
                document.forms.myForm2.action="baseAction.do?param=goHome";
                document.forms.myForm2.submit();
            }
            function logout(){
                document.forms.myForm2.action="baseAction.do?param=logoutApplication";
                document.forms.myForm2.submit();
            }
            function cancelScenario(){
                //document.forms.myForm2.action="baseAction.do?param=goHome";
                document.forms.myForm2.action='<%=request.getContextPath()%>'+"/AdminTab.jsp#Scenarios";
                document.forms.myForm2.submit();
            }
          

            function goGlobe(){
                $(".navigateDialog").dialog('open');
            }
            function closeStart(){
                $(".navigateDialog").dialog('close');
            }
            function goPaths(path){
                parent.closeStart();
                document.forms.myForm2.action=path;
                document.forms.myForm2.submit();
            }
            function showTimeRange()
            {
                var historicalStartMonth = document.getElementById("historicalStartMonth").value;
                
                var historicalEndMonth = document.getElementById("historicalEndMonth").value;
                var scenarioStartMonth = document.getElementById("scenarioStartMonth").value;
                var scenarioEndMonth = document.getElementById("scenarioEndMonth").value;
                var frameObj=document.getElementById("TimeDispmem");
                var divObj=document.getElementById("timerangediv");
                var path=document.getElementById('path').value;
                var scenarioName = document.getElementById('scenarioName').value;
                var scenarioDesc = document.getElementById('scenarioDesc').value;                
                var source=path+'/ScenarioTemplateAction.do?scnTemplateParam=getScenarioTimeRange&scenarioName='+scenarioName+"&scenarioDesc="+scenarioDesc+"&historicalStartMonth="+historicalStartMonth+"&historicalEndMonth="+historicalEndMonth+"&scenarioStartMonth="+scenarioStartMonth+"&scenarioEndMonth="+scenarioEndMonth;
                
                frameObj.src='<center><img id="imgId" src="images/ajax.gif" align="middle"  width="75px" height="75px"  style="position:absolute" ></center>';
                frameObj.src=source;
                $("#timerangediv").dialog('open');
            }
            function cancelTimeRange()
            {
                $("#timerangediv").dialog('close');
            }
            function selectSeededModel()
            {
                var selectedSeededModels = document.getElementById("selectedSeededModels").value;
                var frameObj=document.getElementById("seededmodelmem");
                var divObj=document.getElementById("seededmodeldiv");
                var path=document.getElementById('path').value;
                var scenarioName = document.getElementById('scenarioName').value;
                var source=path+'/ScenarioTemplateAction.do?scnTemplateParam=getScenarioSeededModels&scenarioName='+scenarioName+"&selectedSeededModels="+selectedSeededModels;
                frameObj.src='<center><img id="imgId" src="images/ajax.gif" align="middle"  width="75px" height="75px"  style="position:absolute" ></center>';
                frameObj.src=source;
                $("#seededmodeldiv").dialog('open');
            }
            function cancelSeededModelsPar()
            {
                $("#seededmodeldiv").dialog('close');
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
            .paramRegion{
                background-color:#e6e6e6;
            }
            a {font-family:Verdana;cursor:pointer;}
            a:link {color:#369}
            *{
                font:           11px verdana;
            }
            .white_content1 {
                display: none;
                position: absolute;                
                padding: 16px;
                border: 10px solid silver;
                background-color: white;
                z-index:1002;
                -moz-border-radius-bottomleft:4px;
                -moz-border-radius-bottomright:4px;
                -moz-border-radius-topleft:4px;
                -moz-border-radius-topright:4px;
            }

            .black_overlay{
                display: none;
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
            .myTextbox10
            {
                font-family: Verdana, Arial, Helvetica, sans-serif;
                font-weight: normal;
                font-size: 8pt;
                color:#000000;
                padding: 0px;
                width:170px;
                margin-left: 5px;
                border-top: 1px outset #848484;
                border-right: 1px outset #999999;
                border-bottom: 1px outset #999999;
                border-left: 1px outset #848484;
                background-color:#FFFFFF;
            }
        </style>
    </head>

    <body class="body" id="mainBody">
        <%
                String Pagename = scenarioName;
                String path = request.getContextPath();
                String url = request.getAttribute("scenariodesignerurl").toString();
                String UserFldsData = "";
                if (request.getAttribute("UserFlds") != null) {
                    UserFldsData = String.valueOf(request.getAttribute("UserFlds"));
                }
        %>
        <form name="myForm2" method="post">
            <input type="hidden" name="PrevParams" id="PrevParams" value="<%=prevParamArray%>">
            <input type="hidden" name="PrevTimeParams" id="PrevTimeParams" value="<%=prevTimeParams%>"
                   <input type="hidden" name="path" id="path" value="<%=path%>"
                   <input type="hidden" name="scenarioName" id="scenarioName" value="<%=scenarioName%>"
                   <input type="hidden" name="scenarioDesc" id="scenarioDesc" value="<%=scenarioDesc%>"
                   <table width="100%" >
                <tr>
                    <td>
                        <table style="width:100%">
                            <tr valign="top">
                                <td valign="top" style="height:30px;width:10%;">
                                    <img width="40px" height="30px"  title="pi " src="<%=request.getContextPath()%>/images/pi_logo.png"/>
                                </td>
                                <td valign="top" style="height:30px;width:80%" >

                                </td>
                                <td valign="top" style="height:30px;width:10%;" align="right">
                                    <img width="150px" height="30px"  title="Progen Business Solutions" src="<%=request.getContextPath()%>/images/prgLogo.gif"/>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
                <tr style="height:15px;width:100%;max-height:100%">
                    <td>
                        <table width="100%" class="ui-corner-all">
                            <tr>
                                <td style="height:10px;width:10%" >
                                    <font  style="color:#369;font-family:verdana;font-size:15px;font-weight:bold"  title="<%=scenarioName%>"><%=scenarioName%></font>
                                </td>                                
                                <td style="height:10px;width:20%" align="right">
                                    <a href="javascript:void(0)" onclick="javascript:goGlobe()" style="font-size:10px;color:#2191C0;font-weight:bold;text-decoration: none;font-family:Georgia"> Navigation </a> |
                                    <a href="javascript:void(0)" onclick="javascript:gohome('<%=loguserId%>')" style="font-size:10px;color:#2191C0;font-weight:bold;text-decoration: none;font-family:Georgia"> Home </a> |
                                    <a href="#" style="font-size:10px;color:#2191C0;font-weight:bold;text-decoration: none;font-family:Georgia"> Help </a> |
                                    <a href="javascript:void(0)" onclick="javascript:logout()" style="font-size:10px;color:#2191C0;font-weight:bold;text-decoration: none;font-family:Georgia"> Logout </a>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>
            <table style="width:100%" border="solid black 1px">
                <tr>
                    <td width="13%" valign="top" class="draggedTable1" >
                        <div style="height:20px" class="ui-state-default draggedDivs ui-corner-all">
                            &nbsp;<font style="font-weight:bold" face="verdana" size="1px">Scenario Designer</font>
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
                            </ul>
                        </div>
                    </td>                   
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
                                                <div align="left" id="paramDisp" style="min-width:800px;min-height:100px;">

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
                                                    &nbsp;&nbsp;<font size="2" style="font-weight:bold">Table </font>&nbsp;&nbsp;<a id="prevTab"  href="javascript:PreviewTable()" title="Preview Table" >Preview</a>
                                                </h3>
                                                <%--onmouseover="showRowParams()" onmouseout="hideRowParams()"--%>
                                                <%--onmouseover="showColParams()" onmouseout="hideColParams()"--%>
                                                <div id="tableDiv" style="width:100%;min-height:230px;">
                                                    <Table>
                                                        <Tr>
                                                            <Td>
                                                                <a id="rep"  href="javascript:showRowParams()"   title="Click to Select Row Parameter" >Select Row Edge</a>&nbsp;
                                                                <div style="display:none;width:auto;height:auto;background-color:#ffffff;overflow:auto;position: absolute;text-align: left;border: 1px solid #000000;border-top-width: 0px;" id="repDiv">
                                                                </div>
                                                            </Td>
                                                            <Td><img src="<%=request.getContextPath()%>/images/separator.gif" style='border:0'  alt='Separator' /></Td>
                                                            <Td>
                                                                <a id="Measure" href="javascript:showMeasures()" title="Click to Select Measures">Select Measures</a>&nbsp;
                                                            </Td>
                                                            <Td><img src="<%=request.getContextPath()%>/images/separator.gif" style='border:0'  alt='Separator' /></Td>
                                                            <Td>
                                                                <a id="TimeRange" href="javascript:showTimeRange()" title="Click to Select Time Range">Select Historical Time Range</a>&nbsp;
                                                            </Td>
                                                            <Td><img src="<%=request.getContextPath()%>/images/separator.gif" style='border:0'  alt='Separator' /></Td>
                                                            <Td>
                                                                <a id="seededmodel" href="javascript:selectSeededModel()" title="Click to Select Seeded Models">Select Seeded Models</a>&nbsp;
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


            <div id="measures" class="white_content1" align="justify">
                <iframe id="dataDispmem" NAME='dataDispmem' style="height:100%;width:100%;overflow:auto" frameborder="0"  SRC='#'></iframe>
            </div>
            <div id="timerangediv" title="Select Time Range">
                <iframe id="TimeDispmem" NAME='TimeDispmem' frameborder="0" src='#' scrolling="no" width="100%" height="100%"></iframe>
            </div>
            <div id="seededmodeldiv" title="Select Seeded Models">
                <iframe id="seededmodelmem" NAME='seededmodelmem' frameborder="0" src='#' scrolling="no" width="100%" height="100%"></iframe>
            </div>


            <input type="hidden" name="REPIds" value="" id="REPIds">
            <input type="hidden" name="CEPIds" value="" id="CEPIds">
            <input type="hidden" name="MsrIds" value="" id="MsrIds">
            <input type="hidden" name="Measures" value="" id="Measures">
            <input type="hidden" name="historicalStartMonth" value="" id="historicalStartMonth">
            <input type="hidden" name="historicalEndMonth" value="" id="historicalEndMonth">
            <input type="hidden" name="scenarioStartMonth" value="" id="scenarioStartMonth">
            <input type="hidden" name="scenarioEndMonth" value="" id="scenarioEndMonth">
            <input type="hidden" name="selectedSeededModels" value="" id="selectedSeededModels">

        </form>


        <div id="fade" class="black_overlay"></div>


        <!--style="cursor:hand;text-decoration:none" -->
        <input type="hidden"  id="h" value="<%=request.getContextPath()%>">

        <div>
            <iframe  id="graphCols" NAME='bucketDisp'  STYLE='display:none;'   class="white_content1" SRC=''></iframe>
        </div>
        <div>
            <iframe  id="graphDtls" NAME='graphDtls'  class="white_content1" STYLE='display:none;' SRC=''></iframe>
        </div>


        <table width="100%">
            <tr>
                <td height="10px">&nbsp;</td>
                <td height="10px">&nbsp;</td>
            </tr>
            <tr>
                <td height="10px">&nbsp;</td>
                <td align="center"><input type="button" class="navtitle-hover" value="Next" onclick="createScenario()" style="width:auto">&nbsp;&nbsp;<input type="button" class="navtitle-hover" style="width:auto" value="Cancel" onclick="javascript:cancelScenario();"></td>
            </tr>
            <tr>
                <td height="10px">&nbsp;</td>
                <td height="10px">&nbsp;</td> 
            </tr>
        </table>
        <table width="100%" class="fontsty" style="background-color:#bdbdbd">
            <tr class="fontsty" style="height:10px;width:100%;max-height:100%;background-color:#bdbdbd">
                <td style="height:10px;width:100%;background-color:#bdbdbd" >
                    <center style="background-color:#bdbdbd"><font  style="color:#fff;font-family:verdana;font-size:10px;font-weight:normal" align="center">Copyright &copy 2009-12 <a href="http://www.progenbusiness.com" style="color:red;font-weight:bold;font-size:10px;font-family:verdana">Progen Business Solutions.</a> All Rights Reserved</font></center>
                </td>
            </tr>
        </table>

        <div id="dispTabProp" style="display:none">
            <iframe id="dispTabPropFrame" NAME='dispTabPropFrame' style="width:700px" class="white_content1" SRC='#'></iframe>
        </div>

        <div id="fadestart" class="black_start"></div>

    </body>
</html>
<%
            } catch (Exception exp) {
                exp.printStackTrace();
            }
%>

