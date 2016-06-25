<%@page import="prg.scenario.param.PbScenarioParamVals"%>
<%@page import="prg.scenario.client.PbScenarioManager"%>
<%@page import="prg.db.Session"%>
<%@page import="prg.db.PbReturnObject"%>

<%@page contentType="text/html" pageEncoding="windows-1252"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
        <title>pi 1.0</title>
        <Script language="javascript"  src="../JS/scenarioScript.js"></Script>
        <link href="../css/myStyles.css" rel="stylesheet" type="text/css">
    </head>
    <body>
<%
        PbScenarioParamVals scenarioParams = new PbScenarioParamVals();
        PbScenarioManager scenarioClient = new PbScenarioManager();
        Session scenarioSession = new Session();

        String scenarioId = (String)session.getAttribute("ratingSID");
        ////////////////////////////////////////.println.println("scenarioId is:: "+scenarioId);
        String modelName = (String)session.getAttribute("ratingModelName");
        ////////////////////////////////////////.println.println("modelName is:: "+modelName);
        String scenarioRating = request.getParameter("scenarioRating");
        ////////////////////////////////////////.println.println("scenarioRating is:: "+scenarioRating);        
        String dimensionId = (String)session.getAttribute("ratingDimId");
        ////////////////////////////////////////.println.println("dimensionId is:: "+dimensionId);

        scenarioParams.setScenarioId(scenarioId);
        scenarioParams.setModelName(modelName);
        scenarioParams.setDimensionId(dimensionId);
        if(scenarioRating.equalsIgnoreCase("One"))
        {
            scenarioParams.setScenarioRating("*");
        }
        else if(scenarioRating.equalsIgnoreCase("Two"))
        {
            scenarioParams.setScenarioRating("* *");
        }
        else if(scenarioRating.equalsIgnoreCase("Three"))
        {
            scenarioParams.setScenarioRating("* * *");
        }
        else if(scenarioRating.equalsIgnoreCase("Four"))
        {
            scenarioParams.setScenarioRating("* * * *");
        }
        else if(scenarioRating.equalsIgnoreCase("Five"))
        {
            scenarioParams.setScenarioRating("* * * * *");
        }
        else
        {
            scenarioParams.setScenarioRating("Assign Rating");
        }

        scenarioSession.setObject(scenarioParams);
        scenarioClient.updateScenarioRating(scenarioSession);

        

%>
        <form name="myForm" method="post">
            <script type="text/javascript">
                parent.parent.closeScDialog();
            </script>
        </form>
        
    </body>
</html>
