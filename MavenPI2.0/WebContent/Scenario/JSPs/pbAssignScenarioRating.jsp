<%@page import="prg.scenario.param.PbScenarioParamVals"%>
<%@page import="prg.scenario.client.PbScenarioManager"%>
<%@page import="prg.db.Session"%>
<%@page import="prg.db.PbReturnObject"%>
<%@ page import="utils.db.*"%>
<%@ page import="java.util.ArrayList"%>

<%@page contentType="text/html" pageEncoding="windows-1252"%>

<html>
    <head>

        <meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
        <title>pi 1.0</title>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/lib/jquery/js/jquery-1.4.2.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/jQuery/jquery/ui/ui.core.js"></script>
        <Script type="javascript"  src="<%=request.getContextPath()%>/Scenario/JS/myScripts.js"></Script>
        <link href="../css/myStyles.css" rel="stylesheet" type="text/css">
        <link type="text/css" href="<%=request.getContextPath()%>/jQuery/jquery/themes/base/ui.all.css" rel="stylesheet" />
        <link type="text/css" href="<%=request.getContextPath()%>/jQuery/jquery/themes/base/ui.theme.css" rel="stylesheet" />
        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/metadataButton.css" rel="stylesheet" />

        <style type="text/css">
            *{font:11px verdana;}
            
        </style>
        <script type="text/javascript">
            function saveRating(scenarioId,modelName,dimensionId)
            {
                var selectedScnRating;
                var j=0;
                var scnRatingObj = document.getElementsByName("scenarioRating");
                for(var i=0;i<scnRatingObj.length;i++)
                {
                    if(scnRatingObj[i].checked)
                    {           
                        selectedScnRating = scnRatingObj[i].value;            
                        j++;
                    }
                }

                if(j==0)
                {
                    alert("Please select Scenario Rating");
                    document.getElementsByName("scenarioRating")[0].focus();
                    return false;
                }
                else
                {
                    $.ajax({
                        url: '<%=request.getContextPath()%>'+"/ScenarioViewerAction.do?scenarioParam=updateScenarioRating&scenarioRating="+selectedScnRating+"&scenarioId="+scenarioId+"&modelName="+modelName+"&dimensionId="+dimensionId,
                        success: function(data){
                            parent.goToScenarioListPage();
                        }
                    });
                    return true;
                }
            }
        </script>

    </head>
    <body>

        <%
            String scenarioId = request.getParameter("scenarioId");
            String modelName = request.getParameter("modelName");            
            modelName = modelName.replace("~", " ");            
            String scenarioRating = request.getParameter("scnRating");            
            String dimensionId = request.getParameter("dimId");
            
            
            session.setAttribute("ratingSID", scenarioId);
            session.setAttribute("ratingModelName", modelName);
            session.setAttribute("ratingDimId", dimensionId);

            PbScenarioParamVals scenarioParams = new PbScenarioParamVals();
            PbScenarioManager scenarioClient = new PbScenarioManager();
            Session scenarioSession = new Session();

            scenarioParams.setScenarioId(scenarioId);
            scenarioSession.setObject(scenarioParams);
            PbReturnObject getScenarioDetails = scenarioClient.getScenarioMaster(scenarioSession);

            String scenarioName = getScenarioDetails.getFieldValueString(0, "SCENARIO_NAME");


        %>
        <br><br>
        <center>
            <font size="1px" color="black"> Fields marked <span style="color:red">*</span> are MANDATORY </font>
            <br><br>
            <form name="myForm" method="post">
                <table>
                    <tr>
                        <td class="myhead">Scenario Name</td>
                        <td>
                            <input type="text" disabled class="myTextbox3" name="scenarioName" id="scenarioName" value="<%=scenarioName%>" style="width:200px">
                        </td>
                    </tr>
                    <tr>
                        <td class="myhead">Model Name</td>
                        <td>
                            <input type="text" disabled class="myTextbox3" name="modelName" id="modelName" value="<%=modelName%>" style="width:200px">
                        </td>
                    </tr>
                    <tr>
                        <td rowspan="6" class="myhead"><span style="color:red">*</span>Scenario Rating</td>
                        <td>
                            <%
            if (scenarioRating.equalsIgnoreCase("*")) {
                            %>
                            <input checked type="radio" name="scenarioRating" id="scenarioRating" value="One"> <span style="color:black">One - Star Rating</span>
                            <%                    } else {
                            %>
                            <input type="radio" name="scenarioRating" id="scenarioRating" value="One"> <span style="color:black">One - Star Rating</span>
                            <%            }
                            %>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <%
            if (scenarioRating.equalsIgnoreCase("* *")) {
                            %>
                            <input checked type="radio" name="scenarioRating" id="scenarioRating" value="Two"> <span style="color:black">Two - Stars Rating</span>
                            <%                    } else {
                            %>
                            <input type="radio" name="scenarioRating" id="scenarioRating" value="Two"> <span style="color:black">Two - Stars Rating</span>
                            <%            }
                            %>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <%
            if (scenarioRating.equalsIgnoreCase("* * *")) {
                            %>
                            <input checked type="radio" name="scenarioRating" id="scenarioRating" value="Three"> <span style="color:black">Three - Stars Rating</span>
                            <%                    } else {
                            %>
                            <input type="radio" name="scenarioRating" id="scenarioRating" value="Three"> <span style="color:black">Three - Stars Rating</span>
                            <%            }
                            %>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <%
            if (scenarioRating.equalsIgnoreCase("* * * *")) {
                            %>
                            <input checked type="radio" name="scenarioRating" id="scenarioRating" value="Four"> <span style="color:black">Four - Stars Rating</span>
                            <%                    } else {
                            %>
                            <input type="radio" name="scenarioRating" id="scenarioRating" value="Four"> <span style="color:black">Four - Stars Rating</span>
                            <%            }
                            %>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <%
            if (scenarioRating.equalsIgnoreCase("* * * * *")) {
                            %>
                            <input checked type="radio" name="scenarioRating" id="scenarioRating" value="Five"> <span style="color:black">Five - Stars Rating</span>
                            <%                    } else {
                            %>
                            <input type="radio" name="scenarioRating" id="scenarioRating" value="Five"> <span style="color:black">Five - Stars Rating</span>
                            <%            }
                            %>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <%
            if (scenarioRating.equalsIgnoreCase("Assign Rating")) {
                            %>
                            <input checked type="radio" name="scenarioRating" id="scenarioRating" value="No"> <span style="color:black">No Rating</span>
                            <%                    } else {
                            %>
                            <input type="radio" name="scenarioRating" id="scenarioRating" value="No"> <span style="color:black">No Rating</span>
                            <%            }
                            %>
                        </td>
                    </tr>
                </table>
                <br>
                <table>
                    <tr>
                        <td>
                            <input class="navtitle-hover" type="button" value="Save" onclick="return saveRating('<%=scenarioId%>','<%=modelName%>','<%=dimensionId%>')">
                        </td>
                    </tr>
                </table>
            </form>
        </center>

    </body>
</html>
