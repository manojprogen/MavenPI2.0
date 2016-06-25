
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.*" %>
<%@page import="prg.db.Container"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%
            String path = request.getContextPath();
            String loguserId = String.valueOf(session.getAttribute("USERID"));
            String scenarioName = "";
            String scenarioId = "";
            String scenarioParamSectionDisplay = "";
            String scenarioTableScetionDisplay = "";            
            String scenarioCombineTableFlag = "";
             String GraphDisplay="";
            HashMap map = new HashMap();
            Container container = null;
            String scenarioCombineTableScetionDisplay="";
            ////.println("in jsp--"+scenarioCombineTableScetionDisplay);

            try {
                 if (request.getAttribute("scenarioCombineTableFlag") != null) {
                    scenarioCombineTableFlag = String.valueOf(request.getAttribute("scenarioCombineTableFlag"));
                    scenarioCombineTableScetionDisplay = scenarioCombineTableScetionDisplay;
                }

                if (request.getAttribute("scenarioId") != null) {
                    scenarioId = String.valueOf(request.getAttribute("scenarioId"));
                }
                if (request.getAttribute("scenarioName") != null) {
                    scenarioName = String.valueOf(request.getAttribute("scenarioName"));
                }
                if (request.getAttribute("scenarioParamSectionDisplay") != null) {
                    scenarioParamSectionDisplay = String.valueOf(request.getAttribute("scenarioParamSectionDisplay"));
                }
                if (request.getAttribute("scenarioTableScetionDisplay") != null) {
                    scenarioTableScetionDisplay = String.valueOf(request.getAttribute("scenarioTableScetionDisplay"));
                }
                if (request.getAttribute("scenarioCombineTableScetionDisplay") != null) {
                    scenarioCombineTableScetionDisplay = String.valueOf(request.getAttribute("scenarioCombineTableScetionDisplay"));
                }

                 if (session.getAttribute("graphdisplay") != null) {
                    GraphDisplay = String.valueOf(session.getAttribute("graphdisplay"));
                }
                 
                ////.println("scenarioCombineTableScetionDisplay in displayScenario is:: " + scenarioCombineTableScetionDisplay);
                ////////////////////////////////////////.println.println("scenarioName in displayScenario is:: " + scenarioName);
                ////////////////////////////////////////.println.println("scenarioParamSectionDisplay in displayScenario is:: " + scenarioParamSectionDisplay);
                ////////////////////////////////////////.println.println("scenarioTableScetionDisplay in displayScenario is:: " + scenarioTableScetionDisplay);

                ////////////////
                ArrayList histMonths = new ArrayList();
                if (request.getAttribute("histMonths") != null) {
                    histMonths = (ArrayList) request.getAttribute("histMonths");
                }
                String modelId = "";
                if (request.getAttribute("modelId") != null) {
                    modelId = (String) request.getAttribute("modelId");
                }
                String viewByName = "";
                if (request.getAttribute("viewByName") != null) {
                    viewByName = (String) request.getAttribute("viewByName");
                }
                String dimensionId = "";
                if (request.getAttribute("dimensionId") != null) {
                    dimensionId = (String) request.getAttribute("dimensionId");
                }
                String NONALLCOMBO = "";
                if (request.getAttribute("NONALLCOMBO") != null) {
                    NONALLCOMBO = (String) request.getAttribute("NONALLCOMBO");
                }
                String NewUrl = "";
                String completeURL = "";
                if (request.getAttribute("completeURL") != null) {
                    completeURL = (String) request.getAttribute("completeURL");
                }
                session.setAttribute("viewByName", viewByName);
                session.setAttribute("modelId", modelId);
                session.setAttribute("modelName", modelId);
                session.setAttribute("dimensionId", dimensionId);
                session.setAttribute("NONALLCOMBO", NONALLCOMBO);
                session.setAttribute("scenarioId", scenarioId);
                session.setAttribute("NewUrl", NewUrl);
                session.setAttribute("completeURL", completeURL);

                if (request.getSession(false).getAttribute("SCENARIOTAB") != null) {
                    map = (HashMap) request.getSession(false).getAttribute("SCENARIOTAB");
                }
                if (map.get(scenarioName) != null) {
                    container = (prg.db.Container) map.get(scenarioName);
                } else {
                    container = new prg.db.Container();
                }

                ////.println("parameterhashmap is : " + container.getParametersHashMap());
%>
<html>
    <head> 
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>pi 1.0</title>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/lib/jquery/js/jquery-1.4.2.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/jQuery/jquery/ui/ui.core.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/jQuery/jquery/ui/ui.datepicker.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/jQuery/jquery/external/bgiframe/jquery.bgiframe.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/jQuery/jquery/ui/ui.changedialog.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/Scenario/JS/myScripts.js"></script>        
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/ui.dialog.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.tablesorter.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/tablesorter/addons/pager/jquery.tablesorter.pager.js"></script>

        <link type="text/css" href="<%=request.getContextPath()%>/jQuery/jquery/themes/base/ui.all.css" rel="stylesheet" />
        <link type="text/css" href="<%=request.getContextPath()%>/jQuery/jquery/themes/base/ui.theme.css" rel="stylesheet" />
        <link type="text/css" href="<%=request.getContextPath()%>/jQuery/jquery/themes/base/ui.datepicker.css" rel="stylesheet" />
        <link type="text/css" href="<%=request.getContextPath()%>/jQuery/jquery/demos.css" rel="stylesheet" />        
        <link rel="stylesheet" href="<%=request.getContextPath()%>/stylesheets/jq.css" type="text/css" media="print, projection, screen">
        <link type="text/css" href="<%=request.getContextPath()%>/css/css.css" rel="stylesheet" />
        <link type="text/css" href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet" />        
        <link href="<%=request.getContextPath()%>/stylesheets/html.css" type="text/css" rel="stylesheet">
        <link href="<%=request.getContextPath()%>/stylesheets/css.css" type="text/css" rel="stylesheet">
        <link rel="stylesheet" href="<%=request.getContextPath()%>/stylesheets/metadataButton.css" type="text/css">
        <link rel="stylesheet" href="<%=request.getContextPath()%>/stylesheets/style.css" type="text/css" media="print, projection, screen">
        <script>
            $(document).ready(function(){
                if ($.browser.msie == true){
                    $("#viewerMeasures").dialog({
                        autoOpen: false,
                        draggable: false,
                        height: 400,
                        width: 600,
                        position: 'justify',
                        modal: true
                    });
                }
                else{         
                    $("#viewerMeasures").dialog({
                        autoOpen: false,
                        height: 400,
                        width: 720,
                        position: 'justify',
                        modal: true
                    });
                }
            });

            function gohome(){
                document.forms.myForm2.action="baseAction.do?param=goHome";
                document.forms.myForm2.submit();
            }
            function logout(){
                document.forms.myForm2.action="baseAction.do?param=logoutApplication";
                document.forms.myForm2.submit();
            }
            function goToScenarioHome(){
                var path='<%=request.getContextPath()%>';                
                document.forms.myForm2.action = path+"/AdminTab.jsp#Scenarios";
                document.forms.myForm2.submit();
            }
            function submitDrill(drillurl)
            {
                var scenarioName=document.getElementById("scenarioName").value;
                var scenarioId=document.getElementById("scenarioId").value;
                document.forms.frmParameter.action = "ScenarioViewerAction.do?scenarioParam=viewScenario&flag=viewScenario&scenarioId="+scenarioId+"&scenarioName="+scenarioName+drillurl;
                document.forms.frmParameter.submit();
            }
            <%--function reload(){

                 //var retest='<%=Container.getTabledisplay()%>'
                 //alert("retest"+retest)
                   document.getElementById("reTest").innerHTML='<%=Container.getTabledisplay()%>'
            }--%>
            
        </script>
        <style type="text/css">
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
            .suggestLink { position: relative;
                           background-color: #FFFFFF;
                           border: 0px solid #000000;
                           border-top-width: 0px;
                           padding: 2px 6px 2px 6px;
                           left:3px;
                           min-width: 20px;
                           max-width: 150px;
            }
            .innerDiv{
                overflow:auto;
                height:95%;
                width:95%;
            }

            .suggestLinkOver {
                background-color: #0099CC;
                padding: 2px 6px 2px 6px;
            }
            #cboRegionsuggestList {
                position: absolute;
                background-color: #FFFFFF;
                text-align: left;
                border: 1px solid #000000;
                border-top-width: 0px;
                width: 160px;
            }

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
            white_contentcolor{
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
                -moz-border-radius-bottomleft:4px;
                -moz-border-radius-bottomright:4px;
                -moz-border-radius-topleft:4px;
                -moz-border-radius-topright:4px;
            }
            .ParamRegion{
                background-color:#e6e6e6;
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

            .colorButton
            {
                width:20px;
                height:20px;
                background-color:green;
            }

            a {font-family:Verdana;cursor:pointer;}
            a:link {color:#369}
            .leftcol {
                clear:left;
                float:left;
                width:100%;
            }
            *{
                font: 11px verdana;
            }
            .label{
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
    </head>
    <body id="mainBody" onload="">

        <div id="light" align="center" class="white_content"><img  alt="Page is Loading" src='images/ajax.gif'></div>
        <form name="myForm2" method="post"></form>
        <table width="100%">
            <tr>
                <td>
                    <table style="width:100%">
                        <tr valign="top">
                            <td valign="top" style="height:30px;width:10%;">
                                <img alt="pi" width="40px" height="30px"  title="pi" src="<%=request.getContextPath()%>/images/pi_logo.png"/>
                            </td>
                            <td valign="top" style="height:30px;width:80%" >

                            </td>
                            <td valign="top" style="height:30px;width:10%;" align="right">
                                <img alt="ProGen Business Solutions" width="150px" height="30px"  title="Progen Business Solutions" src="<%=request.getContextPath()%>/images/prgLogo.gif"/>
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
            <tr style="width:100%;height:544px;max-height:100%">
                <td>
                    <table width="100%" class="ui-corner-all" style="height:100%" border="1px solid black" cellpadding="0" cellspacing="0">
                        <tr class="ui-corner-all">
                            <td valign="top" style="width:99%" class="ui-corner-all">
                                <form name="frmParameter" action="reportViewer.jsp" method="post" >
                                    <input type="hidden" name="h" id="h" value="<%=request.getContextPath()%>">
                                    <input type="hidden" name="scenarioId" id="scenarioId" value="<%=scenarioId%>">
                                    <input type="hidden" name="scenarioName" id="scenarioName" value="<%=scenarioName%>">
                                    <input type="hidden" name="viewByName" id="viewByName" value="<%=viewByName%>">
                                    <input type="hidden" name="path" id="path" value="<%=path%>">
                                    <!-- Begin of Parameters Region-->

                                    <table style="width:99%" valign="top">
                                        <tr valign="top" class="ParamRegion">
                                            <td valign="top" width="100%">
                                                <div class="navsection"  style="height:auto;width:100%">
                                                    <div class="navtitle1" style="width:100%">&nbsp;<b style="font-family:verdana">Parameters Region</b></div>
                                                    <div id="tabParameters">
                                                        <table  class="paramTable" width="70%"  >
                                                            <tr>
                                                                <td valign="top" align="left" width="100%">
                                                                    <%=scenarioParamSectionDisplay%>
                                                                </td>
                                                            </tr>
                                                        </table>
                                                    </div>
                                                </div>
                                            </td>
                                        </tr>

                                        <tr valign="top">
                                            <td valign="top" width="100%">
                                                <div class="navsection" >
                                                    <div class="navtitle1" style="width:100%">&nbsp;<b style="font-family:verdana">Table Region</b>
                                                        <%--&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a id="Measure" href="javascript:showViewerMeasures()" title="Click to Select Measures">Select Measures</a>&nbsp;--%>
                                                    </div>
                                                    <div id="tabTable" >
                                                        <table   style="height:auto" width:="100%">

                                                            <tr>

                                                                <td valign="top" width="100%">
                                                                    <%=scenarioTableScetionDisplay%>
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <td valign="top" width="100%">
                                                                    <%=GraphDisplay%>
                                                                </td>
                                                            </tr>
                                                        </table>
                                                    </div>
                                                </div>
                                            </td>
                                        </tr>
                                        <%--
                                        <tr valign="top">
                                            <td valign="top" width="100%">
                                                <div class="navsection" >
                                                    <div class="navtitle1" style="width:100%">&nbsp;<b style="font-family:verdana">Graph Region</b></div>
                                                    <div id="tabGraph" >
                                                        <table   style="height:auto" width:="100%">
                                                            <tr>
                                                                <td valign="top" width="100%">

                                                                </td>
                                                            </tr>
                                                        </table>
                                                    </div>
                                                </div>
                                            </td>
                                        </tr>
                                        --%>
                                    </table>
                                </form>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
            <tr>
                <td>
                    <Table align="center">
                        <Tr>
                            <Td><Input type="button" class="navtitle-hover" style="width:auto"  value="Scenario Home" onclick="goToScenarioHome();"></Td>                           
                        </Tr>
                    </Table>
                </td>
            </tr>
        </table>
        <br/>
        <table width="100%" class="fontsty">
            <tr class="fontsty" style="height:10px;width:100%;max-height:100%;background-color:#bdbdbd">
                <td style="height:10px;width:100%;background-color:#bdbdbd">
                    <center style="background-color:#bdbdbd"><font  style="color:#fff;font-family:verdana;font-size:10px;font-weight:normal" align="center">Copyright © 2009-12 <a href="http://www.progenbusiness.com" style="color:red;font-weight:bold;font-size:10px;font-family:verdana">Progen Business Solutions.</a> All Rights Reserved</font></center>
                </td>
            </tr>
        </table>
        <div id="viewerMeasures" style="display:none">
            <iframe id="viewerMsrs" NAME='viewerMsrs' style="height:100%;width:100%;overflow:auto" frameborder="0"  SRC='#'></iframe>
        </div>
        <div id="fade" class="black_overlay"></div>
    </body>    
</html>

<%
            } catch (Exception exp) {
                exp.printStackTrace();
            }

%>
