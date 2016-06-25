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
    <body>
<%
        PbTargetParamParams targetParams = new PbTargetParamParams();
        PbTargetParamManager targetClient = new PbTargetParamManager();
        Session targetSession = new Session();
        String path=request.getContextPath();

        String targetId = request.getParameter("chk1");
        ////////////////////////////////////////////////////////////////////////.println("targetId in checkActiveAlerts page is: "+targetId);

        targetParams.setTargetId(targetId);
        targetSession.setObject(targetParams);
        PbReturnObject getTargetName = targetClient.getTargetMaster(targetSession);
        String targetName = getTargetName.getFieldValueString(0,"TARGET_NAME");

        targetParams.setTargetId(targetId);
        targetSession.setObject(targetParams);
        PbReturnObject getActiveAlerts = targetClient.getActiveAlerts(targetSession);

        int count = getActiveAlerts.getFieldValueInt(0,"NUM_OF_ACTIVE_ALERTS");
        ////////////////////////////////////////////////////////////////////////.println("num_of_active_alerts are: "+count);

        if(count==0)
        {
            ////////////////////////.println("in if block");
            session.setAttribute("targetId",targetId);
            %>
             <form name="myForm" method="post">
            <script>
                //alert('in jkjk  ')
                parent.initialogTypeTarget();
                parent.$("#editTarget").dialog('open');
                var frameObj = parent.document.getElementById("editTargetDisp");
                frameObj.src='<%=path%>'+"/QTarget/JSPs/pbEditTarget.jsp";
                //document.myForm.action = '<%=path%>'+"/QTarget/JSPs/pbEditTarget.jsp";
                //document.myForm.submit();
            </script>
         </form>
         <%
           // response.sendRedirect(request.getContextPath()+"/QTarget/JSPs/pbEditTarget.jsp");
        }
        else
        {
            ////////////////////////.println("in else block");
%>
         <form name="myForm" method="post">
            <script>
                var targetName = "<%=targetName%>";
                document.myForm.action = '<%=path%>'+"/home.jsp#Targets";
                document.myForm.submit();
                alert("'"+targetName+"' target is not editable as it has active alerts");
            </script>
         </form>
<%
            //response.sendRedirect("pbTargetList.jsp");
        }
%>
    </body>
</html>
