<%@page import="prg.targetparam.qdparams.PbTargetParamParams"%>
<%@page import="prg.targetparam.qdclient.PbTargetParamManager"%>
<%@page import="prg.db.Session"%>
<%@page import="prg.db.PbReturnObject"%>
<%@ page import="java.util.ArrayList"%>

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
        String path=request.getContextPath();
        PbTargetParamParams targetParams = new PbTargetParamParams();
        PbTargetParamManager targetClient = new PbTargetParamManager();
        Session targetSession = new Session();

        String[] targetIds = request.getParameterValues("chk1");
        ////////////////////////////////////////////////////////////////////////.println("targetId is: "+targetIds);

        String targetId = null;

        PbReturnObject getActiveAlerts = null;
        PbReturnObject getTargetName = null;
        String targetNames = null;
        String temp = null;
        int number = 0;
        PbReturnObject getMeasureCount = null;
        String measureId = null;

        for(int i=0;i<targetIds.length;i++)
        {
            /*////////////////////////////////////////////////////////////////////////.println("Project Parameter Ids are: "+targetIds[i]);
            if(targetId == null)
            {
                targetId = targetIds[i];
            }
            else
            {
                targetId = targetId + "," + targetIds[i];
            }*/
            ////////////////////////////////////////////////////////////////////////.println("targetId is: "+targetIds[i]);
            targetParams.setTargetId(targetIds[i]);
            targetSession.setObject(targetParams);
            getActiveAlerts = targetClient.getActiveAlerts(targetSession);
            int count = getActiveAlerts.getFieldValueInt(0,"NUM_OF_ACTIVE_ALERTS");
            targetId = targetIds[i];
            ////////////////////////////////////////////////////////////////////////.println("targetId inside for loop is: "+targetId);
            ////////////////////////////////////////////////////////////////////////.println("number of active alerts are: "+count);

            getTargetName = targetClient.getTargetMaster(targetSession);
            measureId = String.valueOf(getTargetName.getFieldValueInt(0,"MEASURE_ID"));

            /*
            targetParams.setMeasureId(measureId);
            targetSession.setObject(targetParams);
            getMeasureCount = targetClient.checkMeasureCount(targetSession);
            int num = getMeasureCount.getFieldValueInt(0,"NUMBER_OF_RECORDS");
            */
            if(count==0)
            {
                targetParams.setTargetId(targetId);
                targetSession.setObject(targetParams);
                targetClient.deleteTarget(targetSession);
            }
            /*
            else if(count==0 && num>1)
            {

                targetParams.setTargetId(targetId);
                targetSession.setObject(targetParams);
                targetClient.deleteTargetMaster(targetSession);
            }*/
            else
            {
                getTargetName = targetClient.getTargetMaster(targetSession);
                temp = getTargetName.getFieldValueString(0,"TARGET_NAME");
                if(targetNames==null)
                {
                    targetNames = temp;
                }
                else
                {
                    targetNames = targetNames+","+temp;
                }

                number++;
            }
        }
        ////////////////////////.println(" path-0=- "+path);

        ////////////////////////////////////////////////////////////////////////.println("targetNames that have active alerts are: "+targetNames);
        //////////////////////////////////////////////////////////////////////////.println("Target Ids are: "+targetId);

%>

        <form name="myForm" method="post">
<%
            if(number!=0)
            {
                ////////////////////////////////////////////////////////////////////////.println("in if block");
%>
            <script>
                var targetNames = "<%=targetNames%>"
                document.myForm.action = '<%=path%>'+"/home.jsp#Targets";
                document.myForm.submit();
                alert("'"+targetNames+"' Target(s) can not be deleted as it(they) has(have) active alerts.");
            </script>
<%
            }
            else
            {
                ////////////////////////////////////////////////////////////////////////.println("in else block");
                //response.sendRedirect("pbTargetList.jsp");
%>
                <script>
                    document.myForm.action = '<%=path%>'+"/home.jsp#Targets";
                    document.myForm.submit();
                    alert("Target deleted successfully")
                </script>
<%
            }
%>
        </form>

    </body>
</html>
