<%@page import="prg.targetparam.qdparams.PbTargetParamParams"%>
<%@page import="prg.targetparam.qdclient.PbTargetParamManager"%>
<%@page import="prg.db.Session"%>
<%@page import="prg.db.PbReturnObject"%>

<%@page contentType="text/html" pageEncoding="windows-1252"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
        <Script language="javascript"  src="../JS/myScripts.js"></Script>
        <link href="../css/myStyles.css" rel="stylesheet" type="text/css">
    </head>
    <%
    String path = request.getContextPath();
    //////////////////////////////////////////////////////////////////////////////.println("path is:: "+path);
    %>
    <body>
<%
        session.removeAttribute("targetId");
        PbTargetParamParams targetParams = new PbTargetParamParams();
        PbTargetParamManager targetClient = new PbTargetParamManager();
        Session targetSession = new Session();

        //String targetId = request.getParameter("targetId");
        ////////////////////////////////////////////////////////////////////////////////.println("targetId in update Target.jsp page is: "+targetId);
        String targetId = (String)session.getAttribute("TID");
        //////////////////////////////////////////////////////////////////////////////.println("targetId in update Target.jsp page is: "+targetId);
        String minTimeLevel = (String)session.getAttribute("MTLevel");
        //////////////////////////////////////////////////////////////////////////////.println("MTLevel is:: "+minTimeLevel);
        String targetName = request.getParameter("targetName");

        String targetStartDate = null;
        String targetEndDate = null;

        if(minTimeLevel.equalsIgnoreCase("Day"))
        {
            targetStartDate = request.getParameter("targetStartDate");
            //////////////////////////////////////////////////////////////////////////////.println("targetStartDate is: "+targetStartDate);
            targetEndDate = request.getParameter("targetEndDate");
            //////////////////////////////////////////////////////////////////////////////.println("targetEndDate is: "+targetEndDate);
        }
        else if(minTimeLevel.equalsIgnoreCase("Month"))
        {
            targetStartDate = request.getParameter("targetStartMonth");
            //////////////////////////////////////////////////////////////////////////////.println("targetStartDate is: "+targetStartDate);
            targetEndDate = request.getParameter("targetEndMonth");
            //////////////////////////////////////////////////////////////////////////////.println("targetEndDate is: "+targetEndDate);
        }
        else if(minTimeLevel.equalsIgnoreCase("Quarter"))
        {
            targetStartDate = request.getParameter("targetStartQtr");
            //////////////////////////////////////////////////////////////////////////////.println("targetStartDate is: "+targetStartDate);
            targetEndDate = request.getParameter("targetEndQtr");
            //////////////////////////////////////////////////////////////////////////////.println("targetEndDate is: "+targetEndDate);
        }
        else if(minTimeLevel.equalsIgnoreCase("Year"))
        {
            targetStartDate = request.getParameter("targetStartYear");
            //////////////////////////////////////////////////////////////////////////////.println("targetStartDate is: "+targetStartDate);
            targetEndDate = request.getParameter("targetEndYear");
            //////////////////////////////////////////////////////////////////////////////.println("targetEndDate is: "+targetEndDate);
        }
        ////////////////////////.println(" in targetId update jsp "+targetId);

        targetParams.setTargetId(targetId);
        targetParams.setTargetName(targetName);
        targetParams.setTargetStartDate(targetStartDate);
        targetParams.setTargetEndDate(targetEndDate);
        targetSession.setObject(targetParams);

        targetClient.updateTarget(targetSession);

        //response.sendRedirect("pbTargetList.jsp");


%>
    <script>  
        parent.updateCloseEdit();
    </script>

    </body>
</html>
