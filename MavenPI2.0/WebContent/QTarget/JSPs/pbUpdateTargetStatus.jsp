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
    String targetId = request.getParameter("chk1");
    ////////////////////////.println("targetId in pbUpdateTargetStatus page is:: "+targetId);

    PbTargetParamParams targetParams = new PbTargetParamParams();
    PbTargetParamManager targetClient = new PbTargetParamManager();
    Session targetSession = new Session();

    targetParams.setTargetId(targetId);
    targetParams.setTargetStatus("Published");
    targetSession.setObject(targetParams);
    targetClient.updateTargetStatus(targetSession);   

%>
<form name="myForm" method="post">
    <script>
        document.myForm.action = '<%=request.getContextPath()%>'+"/home.jsp#Targets";
        document.myForm.submit();
        //alert("Target published updated successfuly");
    </script>
</form>
    </body>
</html>
