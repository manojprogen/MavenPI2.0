<%@page import="prg.targetparam.qdparams.PbTargetParamParams"%>
<%@page import="prg.targetparam.qdparams.PbTargetValuesParam"%>
<%@page import="prg.targetparam.qdclient.PbTargetParamManager"%>
<%@page import="prg.db.Session"%>
<%@page import="prg.db.PbReturnObject"%>
<%@page import="java.util.ArrayList"%>


<%@page contentType="text/html" pageEncoding="windows-1252"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>

        <meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
        <Script language="javascript"  src="../JS/myScripts.js"></Script>
        <link href="../css/myStyles.css" rel="stylesheet" type="text/css">
    </head>
    <body>
<%
        String minTimeLevel = (String)session.getAttribute("minLevel");
        String periodType = (String)session.getAttribute("pType");
        String targetId = (String)session.getAttribute("tId");
        //String measureId = (String)session.getAttribute("mId");
        //String reUrl = (String)session.getAttribute("copyUrl");
        //if(reUrl == null)
        //{
        //    reUrl = (String)session.getAttribute("copyUrlSec");
        //}
        
        String flag = request.getParameter("flag");

        ////////////////////////////////////////////////////////////////////////.println("minTimeLevel is::::::: "+minTimeLevel);
        ////////////////////////////////////////////////////////////////////////.println("periodType is::: "+periodType);
        ////////////////////////////////////////////////////////////////////////.println("targetId is::: "+targetId);
        //////////////////////////////////////////////////////////////////////////.println("measureId is::: "+measureId);
        ////////////////////////////////////////////////////////////////////////.println("flag is::: "+flag);
        
        String srcFrom = null;
        String srcTo = null;
        String desFrom = null;
        String desTo = null;

        if(minTimeLevel.equalsIgnoreCase("Day"))
        {
            srcFrom = request.getParameter("SrcFromDate");
            srcTo = request.getParameter("SrcToDate");
            desFrom = request.getParameter("DesFromDate");
            desTo = request.getParameter("DesToDate");
        }
        else if(minTimeLevel.equalsIgnoreCase("Month"))
        {
            srcFrom = request.getParameter("SrcFromMonth");
            srcTo = request.getParameter("SrcToMonth");
            desFrom = request.getParameter("DesFromMonth");
            desTo = request.getParameter("DesToMonth");
        }
        else if(minTimeLevel.equalsIgnoreCase("Quarter"))
        {
            srcFrom = request.getParameter("SrcFromQtr");
            srcTo = request.getParameter("SrcToQtr");
            desFrom = request.getParameter("DesFromQtr");
            desTo = request.getParameter("DesToQtr");
        }
        else
        {
            srcFrom = request.getParameter("SrcFromYear");
            srcTo = request.getParameter("SrcToYear");
            desFrom = request.getParameter("DesFromYear");
            desTo = request.getParameter("DesToYear");
        }
        
        ////////////////////////////////////////////////////////////////////////.println("srcFrom is:: "+srcFrom);
        ////////////////////////////////////////////////////////////////////////.println("srcTo is:: "+srcTo);        
        ////////////////////////////////////////////////////////////////////////.println("desFromDate is:: "+desFrom);
        ////////////////////////////////////////////////////////////////////////.println("desToDate is:: "+desTo);

        PbTargetParamParams targetParams = new PbTargetParamParams();
        PbTargetValuesParam factParams = new PbTargetValuesParam();
        PbTargetParamManager targetClient = new PbTargetParamManager();
        Session targetSession = new Session();

        targetParams.setMinTimeLevel(minTimeLevel);
        targetParams.setSrcFrom(srcFrom);
        targetParams.setSrcTo(srcTo);
        targetParams.setDesFrom(desFrom);
        targetParams.setDesTo(desTo);
        targetSession.setObject(targetParams);
        PbReturnObject getRange = targetClient.getRangeValues(targetSession);
        PbReturnObject getSrcRange = (PbReturnObject)getRange.getObject("SourceRange");
        PbReturnObject getDesRange = (PbReturnObject)getRange.getObject("DestinationRange");

        int srcRange = getSrcRange.getRowCount();
        int decRange = getDesRange.getRowCount();
        ////////////////////////////////////////////////////////////////////////.println("srcRange is:: "+srcRange);
        ////////////////////////////////////////////////////////////////////////.println("decRange is::: "+decRange);

        ArrayList srcArrayList = new ArrayList();
        ArrayList desArrayList = new ArrayList();
        String temp = null;
        for(int i=0;i<srcRange;i++)
        {
            temp = getSrcRange.getFieldValueString(i,"VIEW_BY");
            srcArrayList.add(temp);
        }

        for(int i=0;i<decRange;i++)
        {
            temp = getDesRange.getFieldValueString(i,"VIEW_BY");
            desArrayList.add(temp);
        }

        ////////////////////////////////////////////////////////////////////////.println("srcArrayList is:: "+srcArrayList);
        ////////////////////////////////////////////////////////////////////////.println("desArrayList is:: "+desArrayList);

        
        factParams.setTargetId(targetId);
        //factParams.setMeasureId(Integer.parseInt(measureId));
        factParams.setPeriodType(periodType);
        factParams.setMinTimeLevel(minTimeLevel);
        factParams.setSourceDates(srcArrayList);
        factParams.setDestDates(desArrayList);
        targetSession.setObject(factParams);
        if(flag.equalsIgnoreCase("true"))
        {
            targetClient.copyRangeData(targetSession);
            //////////////////////////////////////////////////////////////////////////.println("in 1--- "+reUrl);
        }
        else
        {
            targetClient.copyRangeDataDelete(targetSession);
            //////////////////////////////////////////////////////////////////////////.println("in 2--- "+reUrl);
        }
       //////////////////////////////////////////////////////////////////////////.println("reUrl --- "+reUrl);
       
        
%>

    <script>
            
            window.opener.copyUrlParent();
            window.close();
    </script>

        
    </body>
</html>
