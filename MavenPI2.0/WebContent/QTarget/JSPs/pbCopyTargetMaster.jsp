<%@page import="prg.targetparam.qdparams.PbTargetParamParams"%>
<%@page import="prg.targetparam.qdclient.PbTargetParamManager"%>
<%@ page import="prg.targetparam.qdparams.PbTargetValuesParam"%>
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
    ////////////////////////////////////////////////////////////////////////.println("path is:: "+path);
    %>
    <body>
<%
        PbTargetParamParams targetParams = new PbTargetParamParams();
        PbTargetParamManager targetClient = new PbTargetParamManager();
        Session targetSession = new Session();

        String minTimeLevel = (String)session.getAttribute("MTL");
        ////////////////////////////////////////////////////////////////////////.println("MTL is:: "+minTimeLevel);

        String targetId = request.getParameter("targetId");
        String targetName = request.getParameter("targetName");
        String measureId = request.getParameter("measureId");
        String targetDescription = request.getParameter("targetDescription");
        //String targetType = request.getParameter("targetType");
        //String multiplierVal = (String)session.getAttribute("multiplierVal");
        //int multiplier = Integer.parseInt(multiplierVal); //1;
        String multiplierVal = request.getParameter("val");
        int multiplier = 1;
        if(multiplierVal != null)
        {
            multiplier = Integer.parseInt(multiplierVal);
        }

        ////////////////////////////////////////////////////////////////////////.println("targetDescription is:: "+targetDescription);

        String targetStartDate = null;
        String targetEndDate = null;
        if(minTimeLevel.equalsIgnoreCase("Day"))
        {
            targetStartDate = request.getParameter("targetStartDate");
            targetEndDate = request.getParameter("targetEndDate");
        }
        else if(minTimeLevel.equalsIgnoreCase("Month"))
        {
            targetStartDate = request.getParameter("targetStartMonth");
            targetEndDate = request.getParameter("targetEndMonth");
        }
        else if(minTimeLevel.equalsIgnoreCase("Quarter"))
        {
            targetStartDate = request.getParameter("targetStartQtr");
            targetEndDate = request.getParameter("targetEndQtr");
        }
        else if(minTimeLevel.equalsIgnoreCase("Year"))
        {
            targetStartDate = request.getParameter("targetStartYear");
            targetEndDate = request.getParameter("targetEndYear");
        }

        targetParams.setTargetId(targetId);
        targetParams.setTargetName(targetName);
        targetParams.setTargetDescription(targetDescription);
        targetParams.setTargetStartDate(targetStartDate);
        targetParams.setTargetEndDate(targetEndDate);

        targetSession.setObject(targetParams);
        PbReturnObject newTarget = targetClient.copyTarget(targetSession);

        
        int newTargetId = Integer.parseInt(newTarget.getFieldValueString(0,"NEW_TARGET_ID"));
        String newSDate = newTarget.getFieldValueString(0,"S_DATE");
        String newEDate = newTarget.getFieldValueString(0,"E_DATE");
        int oldTargetId = Integer.parseInt(targetId);
        int mId = Integer.parseInt(measureId);
        ////////////////////////////////////////////////////////////////////////.println( newSDate+" --- "+newEDate +" /// "+oldTargetId+" ==========================================...in jsp /newTargetId -"+newTargetId);

        PbTargetValuesParam tParams = new PbTargetValuesParam();
        tParams.setMeasureId(mId);
        tParams.setTargetId(String.valueOf(oldTargetId));
        tParams.setPeriodType(minTimeLevel);
        tParams.setDestFromDate(newSDate);
        tParams.setDestToDate(newEDate);
        tParams.setMultiplier(multiplier);
        //tParams.//
        tParams.setCopyTargetId(newTargetId);
        //tParams.setParamName(paramName);


        PbTargetParamManager tManager =  new PbTargetParamManager();
        Session pSesion = new Session();
        pSesion.setObject(tParams);

        session.setAttribute("measureId",Integer.valueOf(mId));
        session.setAttribute("targetId",Integer.valueOf(newTargetId));

        /*if(targetType.equalsIgnoreCase("Y")){
        ////////////////////////////////////////////////////////////////////////.println("in if... ");
            tManager.copyTargetValues(pSesion);
            response.sendRedirect("tragetdisplay.jsp");
        }
        else{
             ////////////////////////////////////////////////////////////////////////.println("in ielse... ");
             tManager.copyTargetCrossTabNew(pSesion);
             response.sendRedirect("crossTab.jsp");
        }*/
    try{
         tManager.copyTargetDetails(pSesion);
         tManager.copyTargetValues(pSesion);
         //response.sendRedirect("newTArgetDisplay.jsp");
       }
   catch(Exception ec){
       ec.printStackTrace();
       ////////////////////////////////////////////////////////////////////////.println("error -- "+ec.getMessage());
       }
%>
        <script>
            var targetId = '<%=newTargetId%>'
            var timeLevel = '<%=minTimeLevel%>'
            var path = '<%=path%>'
            parent.document.ec.action = path+"/targetView.do?targetParams=viewTarget&targetId="+targetId+"&minTimeLevel="+timeLevel
            parent.document.ec.submit();
        </script>
    </body>
</html>
