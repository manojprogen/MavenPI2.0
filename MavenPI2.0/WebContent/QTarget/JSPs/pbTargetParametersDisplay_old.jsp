<%@page import="prg.targetparam.qdparams.PbTargetParamParams"%>
<%@page import="prg.targetparam.qdclient.PbTargetParamManager"%>
<%@page import="prg.db.Session"%>
<%@page import="prg.db.PbReturnObject"%>
<html>
    <%
    String path = request.getContextPath();
    ////////////////////////////////////////////////////////////////////////.println("path is:: "+path);
    %>
    <body>
<%
        String selectedMeasure = (String)session.getAttribute("selectedMeasure");
        String targetDescription = request.getParameter("targetDescription");
        String targetName = request.getParameter("targetName");
        String busGroup = request.getParameter("country1"); //measure
        String timeLevel = request.getParameter("timeLevel"); //
        String parameterIds = request.getParameter("parameterIds");
        String parameterNames = request.getParameter("parameterNames");
        String primaryTargetParameter = request.getParameter("primaryTargetParameter");
        ////////////////////////////////////////////////////////////////////////.println("primaryTargetParameter is: "+primaryTargetParameter);
        String userId = (String)session.getAttribute("userId");

        
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
        else if(timeLevel.equalsIgnoreCase("Quarter"))
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
        ////////////////////////////////////////////////////////////////////////.println(endDate+" timeLevel "+timeLevel+" startDate "+startDate);
        ////////////////////////////////////////////////////////////////////////.println(primaryTargetParameter+" parameterIds "+parameterIds+" parameterNames "+parameterNames);

        PbTargetParamManager pManager = new PbTargetParamManager();
        PbTargetParamParams pParams = new PbTargetParamParams();
        Session pSession = new Session();
        PbReturnObject parmObj = new PbReturnObject();
        pParams.setTargetName(targetName);
        pParams.setTargetDescription(targetDescription);
        pParams.setPrimaryParameter(primaryTargetParameter);
        pParams.setMeasureId(selectedMeasure);
        pParams.setTimeLevel(timeLevel);
        pParams.setTargetEndDate(endDate);
        pParams.setTargetStartDate(startDate);
        pParams.setParameterIds(parameterIds);
        pParams.setParameterNames(parameterNames);
        pParams.setUserId(userId);
        
        pSession.setObject(pParams);
        parmObj = pManager.addTargetMaster(pSession);
        String targetId = parmObj.getFieldValueString(0,"TARGET_ID");
        ////////////////////////////////////////////////////////////////////////.println(" targetId "+targetId);
        pParams.setTargetId(targetId);
        pSession.setObject(pParams);
        pManager.addParameterDetails(pSession);

        response.sendRedirect(path+"/targetView.do?targetParams=viewTarget&targetId="+targetId+"&minTimeLevel="+timeLevel);
       
%>
<%--
<form name="myForm" method="post">
    <script>
        var targetId = '<%=targetId%>'
        var minTimeLevel = '<%=timeLevel%>'
        var path = '<%=path%>'
        document.myForm.action = path+"/targetView.do?targetParams=viewTarget&targetId="+targetId+"&minTimeLevel="+minTimeLevel
        document.myForm.submit();
    </script>
</form>
--%>
    </body>
</html>