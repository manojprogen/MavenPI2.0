<%@ page import="utils.db.*"%>
<%@ page import="prg.db.PbReturnObject"%>
<%@page import="prg.scenario.param.PbScenarioParamVals"%>
<%@ page import="prg.db.Session"%>
<%@ page import="prg.scenario.client.PbScenarioManager"%>
<%@page import="com.progen.scenariodesigner.db.ScenarioTemplateDAO"%>
<%@page contentType="text/html" pageEncoding="windows-1252"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1252">        
        <script src="<%=request.getContextPath()%>/javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/web/javascript/ui.dialog.js"></script>
        <script src="<%=request.getContextPath()%>/scripts/jquery.cookie.js" type="text/javascript"></script>
        <script src="<%=request.getContextPath()%>/scripts/jquery.treeview.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/demo.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/web/javascript/draggable/ui.core.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/ui.tabs.js"></script>
        <Script type="javascript"  src="<%=request.getContextPath()%>/Scenario/JS/myScripts.js"></Script>
        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/metadataButton.css" rel="stylesheet" />

        <%
            String path = request.getContextPath();
        %>
        <script type="text/javascript">
            function updateScenario()
            {                
                var path = '<%=path%>'
                var scename =document.getElementById("scenarioName").value;
                var sceDec =document.getElementById("scenarioDescription").value;
                var hStMonth =document.getElementById("histStartMonth").value;
                var hEndMonth =document.getElementById("histEndMonth").value;
                var scStartMonth =document.getElementById("scenarioStartMonth").value;
                var scEndMonth =document.getElementById("scenarioEndMonth").value;
                var scnID = document.getElementById("scenarioID").value;              
                $.ajax({
                    url: path+'/ScenarioViewerAction.do?scenarioParam=upDateScenario&scenarioName='+scename+'&scenarioDescription='+sceDec+'&histStartMonth='+hStMonth+'&histEndMonth='+hEndMonth+'&scenarioStartMonth='+scStartMonth+'&scenarioEndMonth='+scEndMonth+'&scenarioID='+scnID,
                    success: function(data) {
                        parent.goToListPage();
                    }
                });
            }
            
            
            function copyToScenarioDesc() {
                document.getElementById("scenarioDescription").value = document.getElementById("scenarioName").value;
            }
            function focusFirstField() {
                document.getElementById("scenarioName").focus();
            }
        </script>
        <style type="text/css">
            .navsection {
                text-decoration: none;
                margin: 0 0 0 0;
                border: 1px solid #CDCDCD;
/*                background: url(../images/navtitlebg.gif) no-repeat top left;*/
                -moz-border-radius: 4px 4px 4px 4px;
                COLOR: #000;
                WIDTH: 100%;
            }
            .navtitle
            {
                -moz-border-radius-bottomleft:4px;
                -moz-border-radius-bottomright:4px;
                -moz-border-radius-topleft:4px;
                -moz-border-radius-topright:4px;
                FONT-SIZE: 11px;
                COLOR: #000;
                FONT-FAMILY: Verdana;
                VERTICAL-ALIGN: middle;
                HEIGHT: 20px;
                WIDTH: 100%;
                background-color:#BDBDBD;
                cursor:pointer;
            }
            .navtitle1
            {
                -moz-border-radius-bottomleft:4px;
                -moz-border-radius-bottomright:4px;
                -moz-border-radius-topleft:4px;
                -moz-border-radius-topright:4px;
                FONT-SIZE: 11px;
                COLOR: #000;
                FONT-FAMILY: Verdana;
                VERTICAL-ALIGN: middle;
                HEIGHT: 20px;
                WIDTH: 100%;
                background-color:#BDBDBD;
                cursor:pointer;
            }
        </style>
        <script type="text/javascript">
            $(document).ready(function(){
                $test=$(".navtitle");

                $test.hover(
                function(){
                    this.style.background="#308DBB";
                    this.style.color="#fff";
                },
                function(){
                    this.style.background="#BDBDBD";
                    this.style.color="#000"
                }
            );
                $test=$(".ui-state-default ");

                $test.hover(
                function(){
                    this.style.background="#308DBB";
                    this.style.color="#000";
                },
                function(){
                    this.style.background="#E6E6E6";
                    this.style.color="#000"
                });
            });
        </script>

    </head>
    <body onload="focusFirstField()">

        <%
            String sId = request.getParameter("scenarioId");
            // ////////////////////////////////////////.println.println("sid---------------" + sId);
            ScenarioTemplateDAO scnTemplatedao = new ScenarioTemplateDAO();
            PbReturnObject getScenarioDetails = scnTemplatedao.getEditScenario(sId);

            String scenarioName = getScenarioDetails.getFieldValueString(0, "SCENARIO_NAME");
            String scenarioDescription = getScenarioDetails.getFieldValueString(0, "SCENARIO_DESC");
            String histStartMonth = getScenarioDetails.getFieldValueString(0, "HISTORICAL_ST_MONTH");
            String histEndMonth = getScenarioDetails.getFieldValueString(0, "HISTORICAL_END_MONTH");
            String scnStartMonth = getScenarioDetails.getFieldValueString(0, "SCENARIO_START_MONTH");
            String scnEndMonth = getScenarioDetails.getFieldValueString(0, "SCENARIO_END_MONTH");
            PbReturnObject getAllMonths = scnTemplatedao.getAllMonths();
            PbReturnObject scenariostrMonths = scnTemplatedao.getScstrtMonths(sId);
            int count = getAllMonths.getRowCount();
            int sMcount = scenariostrMonths.getRowCount();
        %>

        <br>
        <center>
            <font size="1px"> Fields marked <span style="color:red">*</span> are MANDATORY </font>
            <br><br>
            <form name="myForm" method="post">
                <table>
                    <tr>
                        <td class="myhead"><span style="color:red">*</span>Scenario Name</td>
                        <td>
                            <input type="text" id="scenarioName" name="scenarioName" class="myTextbox3" style="width:150px" onkeyup="copyToScenarioDesc()" value="<%=scenarioName%>">
                        </td>
                    </tr>
                    <tr>
                        <td class="myhead">&nbsp;&nbsp;Scenario Description</td>
                        <td>
                            <input type="text" id="scenarioDescription" name="scenarioDescription" style="width:150px" class="myTextbox3" value="<%=scenarioDescription%>">
                        </td>
                    </tr>
                    <tr>
                        <td class="myhead"><span style="color:red">*</span>Historical Start Month</td>
                        <td>
                            <select id="histStartMonth" name="histStartMonth" class="myTextbox3" style="width:152px">
                                <option value="">--Select--</option>
                                <%
            for (int p = 0; p < getAllMonths.getRowCount(); p++) {
                String monName = getAllMonths.getFieldValueString(p, "MON_NAME");
                if (histStartMonth.equalsIgnoreCase(monName)) {
                                %>
                                <option selected value="<%=monName%>"><%=monName%></option>
                                <%
            } else {
                                %>
                                <option value="<%=monName%>"><%=monName%></option>
                                <%
                }
            }
                                %>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td class="myhead"><span style="color:red">*</span>Historical End Month</td>
                        <td>
                            <select id="histEndMonth" name="histEndMonth" class="myTextbox3" style="width:152px" onchange="getScenarioMonths()">
                                <option value="">--Select--</option>
                                <%
            for (int p = 0; p < getAllMonths.getRowCount(); p++) {
                String monName = getAllMonths.getFieldValueString(p, "MON_NAME");
                if (histEndMonth.equalsIgnoreCase(monName)) {
                                %>
                                <option selected value="<%=monName%>"><%=monName%></option>
                                <%
            } else {
                                %>
                                <option value="<%=monName%>"><%=monName%></option>
                                <%
                }
            }
                                %>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td class="myhead"><span style="color:red">*</span>Scenario Start Month</td>
                        <td>
                            <select id="scenarioStartMonth" name="scenarioStartMonth" class="myTextbox3" style="width:152px">
                                <option value="">--Select--</option>
                                <%
            for (int j = 0; j < sMcount; j++) {
                String monName = scenariostrMonths.getFieldValueString(j, "MON_NAME");
                if (scnStartMonth.equalsIgnoreCase(monName)) {
                                %>
                                <option selected value="<%=monName%>"><%=monName%></option>
                                <%
            } else {
                                %>
                                <option value="<%=monName%>"><%=monName%></option>
                                <%
                }
            }
                                %>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td class="myhead"><span style="color:red">*</span>Scenario End Month</td>
                        <td>
                            <select id="scenarioEndMonth" name="scenarioEndMonth" class="myTextbox3" style="width:152px">
                                <option value="">--Select--</option>
                                <%
            for (int j = 0; j < sMcount; j++) {
                String monName = scenariostrMonths.getFieldValueString(j, "MON_NAME");
                if (scnEndMonth.equalsIgnoreCase(monName)) {
                                %>
                                <option selected value="<%=monName%>"><%=monName%></option>
                                <%
            } else {
                                %>
                                <option value="<%=monName%>"><%=monName%></option>
                                <%
                }
            }
                                %>
                            </select>
                        </td>
                    </tr> 
                </table>
                <input type="hidden" id="scenarioID" name="scenarioID" value="<%=sId%>">
                <input type="hidden" id="path" name="path" value="<%=path%>">
                <br>
                <table>
                    <tr>
                        <td>                            
                            <input type="button" class="navtitle-hover" value="Save" onclick="updateScenario()">
                        </td>
                    </tr>
                </table>
            </form>
        </center>

    </body>
</html>
