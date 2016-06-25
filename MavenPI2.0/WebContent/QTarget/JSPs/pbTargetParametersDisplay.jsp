<%@page import="prg.targetparam.qdparams.PbTargetParamParams"%>
<%@page import="prg.targetparam.qdclient.PbTargetParamManager"%>
<%@page import="prg.db.Session"%>
<%@page import="prg.db.PbReturnObject"%>
<html>
    <%
    String path = request.getContextPath();
    //////////////////////////////////////////.println("path is:: .--"+path);
    %>
    <body>
<%
        String timeLevels=request.getParameter("timeLevels");
        //////////////////////////////////////////.println(" in save jsp ");
        String selectedMeasure = (String)session.getAttribute("selectedMeasure");
        String targetDescription = request.getParameter("targetDescription");
        String targetName = request.getParameter("targetName");
        String busGroup = request.getParameter("country1"); //measure
        String timeLevel = "";//request.getParameter("timeLevel"); //
        String parameterIds = request.getParameter("parameterIds");
        String parameterNames = request.getParameter("parameterNames");
        //String primaryTargetParameter = request.getParameter("primaryTargetParameter");
        String userId = request.getParameter("userId");//String)session.getAttribute("userId");
        String levels[]= timeLevels.split(",");
        String topLevel=levels[0];
        ////////////////////////.println(" parameterIds "+parameterIds);
        if(topLevel.equalsIgnoreCase("1"))
            timeLevel="Year";
        else if(topLevel.equalsIgnoreCase("2"))
            timeLevel="Qtr";
        else if(topLevel.equalsIgnoreCase("3"))
            timeLevel="Month";
        else if(topLevel.equalsIgnoreCase("4"))
            timeLevel="Day";

        
        String startDate = "";
        String endDate = "";
        if(timeLevel.equalsIgnoreCase("Day")){
          startDate = request.getParameter("targetStartDate");
          endDate = request.getParameter("targetEndDate");
            }
        else if(timeLevel.equalsIgnoreCase("Month"))
        {
            startDate = request.getParameter("targetStartMonth");
            endDate = request.getParameter("targetEndMonth");
        }
        else if(timeLevel.equalsIgnoreCase("Quarter")||timeLevel.equalsIgnoreCase("Qtr"))
        {
            startDate = request.getParameter("targetStartQtr");
            endDate = request.getParameter("targetEndQtr");
        }
        else if(timeLevel.equalsIgnoreCase("Year"))
        {
            startDate = request.getParameter("targetStartYear");
            endDate = request.getParameter("targetEndYear");
        }

        ////////////////////////////////////////////////////////////////////////.println(" selectedMeasure "+selectedMeasure+" targetName "+targetName+" busGroup "+busGroup+" .. "+targetDescription);
        //////////////////////////////////////////.println(endDate+" timeLevel "+timeLevel+" startDate "+startDate);
        ////////////////////////////////////////////////////////////////////////.println(primaryTargetParameter+" parameterIds "+parameterIds+" parameterNames "+parameterNames);

        PbTargetParamManager pManager = new PbTargetParamManager();
        PbTargetParamParams pParams = new PbTargetParamParams();
        Session pSession = new Session();
        PbReturnObject parmObj = new PbReturnObject();
        pParams.setTargetName(targetName);
        pParams.setTargetDescription(targetDescription);
       // pParams.setPrimaryParameter(primaryTargetParameter);
        pParams.setMeasureId(selectedMeasure);
        pParams.setTimeLevel(timeLevel);
        if(timeLevel.equalsIgnoreCase("Day"))
        {
        pParams.setTargetEndDate(endDate);
        pParams.setTargetStartDate(startDate);
        }
         else if(timeLevel.equalsIgnoreCase("Month"))
        {
         pParams.setTargetStartMonth(startDate);
         pParams.setTargetEndMonth(endDate);
        }
         else if(timeLevel.equalsIgnoreCase("Quarter")||timeLevel.equalsIgnoreCase("Qtr"))
        {
         pParams.setTargetStartQtr(startDate);
         pParams.setTargetEndQtr(endDate);
        }
         else if(timeLevel.equalsIgnoreCase("Year"))
        {
         pParams.setTargetStartYear(startDate);
         pParams.setTargetEndYear(endDate);
        }
        pParams.setParameterIds(parameterIds);
        pParams.setParameterNames(parameterNames);
        pParams.setUserId(userId);
        
        pSession.setObject(pParams);
        parmObj = pManager.addTargetMaster(pSession);
        String targetId = parmObj.getFieldValueString(0,"TARGET_ID");
        ////////////////////////////////////////////////////////////////////////.println(" targetId "+targetId);
        pParams.setTargetId(targetId);
        pSession.setObject(pParams);
        PbReturnObject pbroDet = pManager.addParameterDetails(pSession);
        //////////////////////////////////////////.println(" timeLevels ");
        pParams.setTargetTimeLevels(timeLevels);
        pParams.setTargetId(targetId);
        pSession.setObject(pParams);
        pManager.addTargetTimeLevels(pSession);
        //////////////////////////////////////////.println("s------------------------- "+path+"/targetView.do?targetParams=viewTarget&targetId="+targetId+"&minTimeLevel="+timeLevel);
        //response.sendRedirect(path+"/targetView.do?targetParams=viewTarget&targetId="+targetId+"&minTimeLevel="+timeLevel);

       
%>

    <script>
        var targetId = '<%=targetId%>'
        var timeLevel = '<%=timeLevel%>'
        var path = '<%=path%>'
        parent.document.ec.action = path+"/targetView.do?targetParams=viewTarget&targetId="+targetId+"&minTimeLevel="+timeLevel
        parent.document.ec.submit();
       // parent.parent.document.baseForm.action = path+"/targetView.do?targetParams=viewTarget&targetId="+targetId+"&minTimeLevel="+timeLevel
       // parent.parent.document.baseForm.submit();
    </script>

    </body>
</html>