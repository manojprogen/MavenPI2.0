<%@page import="prg.targetparam.qdparams.PbTargetParamParams"%>
<%@page import="prg.targetparam.qdclient.PbTargetParamManager"%>
<%@page import="prg.db.Session"%>
<%@page import="prg.db.PbReturnObject"%>
<%@page import="java.util.Vector"%>
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
        PbTargetParamParams targetParams = new PbTargetParamParams();
        PbTargetParamManager targetClient = new PbTargetParamManager();        
        Session targetSession = new Session();

        String[] paramIds = request.getParameterValues("chk1");

        /*
        String selectedMeasure = (String)session.getAttribute("selectedMeasure");
        String targetDescription = request.getParameter("targetDescription");
        String targetName = request.getParameter("targetName");
        String busGroup = request.getParameter("country1"); //measure
        String timeLevel = request.getParameter("timeLevel");
        String startDate = "";
        String endDate = "";
        if(timeLevel.equalsIgnoreCase("Day")){
          startDate = request.getParameter("startDate");
          endDate = request.getParameter("endDate");
            }

        ////////////////////////////////////////////////////////////////////////.println(" selectedMeasure "+selectedMeasure+" targetName "+targetName+" busGroup "+busGroup+" .. "+targetDescription);
        ////////////////////////////////////////////////////////////////////////.println(endDate+" timeLevel "+timeLevel+" startDate "+startDate);


        */

        String parameterIds = null;
        String parameterNames = null;
        for(int i=0;i<paramIds.length;i++)
        {
            ////////////////////////////////////////////////////////////////////////.println("Parameter Ids are: "+paramIds[i]);
            if(parameterIds == null)
            {
                parameterIds = paramIds[i];
            }
            else
            {
                parameterIds = parameterIds + "," + paramIds[i];
            }
        }

         session.setAttribute("selectedParams",parameterIds);
        session.setAttribute("selectedParamNames",parameterNames);
        targetParams.setParameterId(parameterIds);
        targetParams.setParameterNames(parameterNames);
       // targetParams.setMeasureId(selectedMeasure);
        targetSession.setObject(targetParams);
        PbReturnObject getSelectedParameterNames = targetClient.getParameterNames(targetSession);

        int count = getSelectedParameterNames.getRowCount();
        ////////////////////////////////////////////////////////////////////////.println("paramIds.length is: "+paramIds.length);
        ////////////////////////////////////////////////////////////////////////.println("count is: "+count);
        String temp = null;
        for(int i=0;i<count;i++)
        {
            temp = getSelectedParameterNames.getFieldValueString(i,"MEMBER_NAME");
            if(parameterNames == null)
            {
                parameterNames = temp;
            }
            else
            {
                parameterNames = parameterNames + "," + temp;
            }
        }
        ////////////////////////////////////////////////////////////////////////.println("selected paramNames are: "+parameterNames);

       

        /*
        targetParams.setParameterId(parameterIds);
        targetSession.setObject(targetParams);
        PbReturnObject nonDefHierarchyParams = (PbReturnObject)targetClient.getNonDefHierarchyParams(targetSession);
        ////////////////////////////////////////////////////////////////////////.println("nonDefHierarchyParams is: "+nonDefHierarchyParams);

        int rowCount = nonDefHierarchyParams.getRowCount();

        ArrayList nonDHParams = new ArrayList();
        String paramId = null;
        for(int i=0;i<rowCount;i++)
        {
            paramId = String.valueOf(nonDefHierarchyParams.getFieldValueInt(i,"PPM_PARAM_ID"));
            ////////////////////////////////////////////////////////////////////////.println("paramId is: "+paramId);
            nonDHParams.add(paramId);
        }
        ////////////////////////////////////////////////////////////////////////.println("ArrayList is: "+nonDHParams);

        int xy = 0;
        ArrayList orphans = new ArrayList();

        for(int j=0;j<paramIds.length;j++)
        {
            if(nonDHParams.contains(paramIds[j]))
            {
                ////////////////////////////////////////////////////////////////////////.println("inside if block");
                orphans.add(paramIds[j]);
                xy = 1;
                ////////////////////////////////////////////////////////////////////////.println("xy==1");
            }
            else
            {
                ////////////////////////////////////////////////////////////////////////.println("inside else block");
                xy = 2;
                ////////////////////////////////////////////////////////////////////////.println("xy==2");
            }
        }

        session.setAttribute("orphans",orphans);

        if(xy==1)
        {
            response.sendRedirect("pbSelectTargetParamHierarchy.jsp");
        }
        else
        {
            ////////////////////////////////////////////////////////////////////////.println("in else block when xy==2");
     */
%>
            <script>
                
                var paramIds = "<%=parameterIds%>";
                var paramNames = "<%=parameterNames%>";
                window.opener.document.myForm.parameterIds.value = paramIds;
                window.opener.document.myForm.parameterNames.value = paramNames;
                window.close();
            </script>
<%
        //}
%>
    </body>
</html>
