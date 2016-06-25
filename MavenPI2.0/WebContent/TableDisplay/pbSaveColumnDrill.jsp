<%-- 
    Document   : pbSaveColumnDrill
    Created on : Jul 29, 2009, 6:13:53 PM
    Author     : santhosh.kumar@progenbusiness.com
--%>

<%@page contentType="text/html" pageEncoding="windows-1252"%>
<%@page  import="prg.graphs.client.*" %>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
        <title>JSP Page</title>
        <script>
            function refreshParent(){
                window.opener.submitform();
                windowClose();
            }
            function windowClose(){
                 window.close();
            }
        </script>
    </head>
    <%
       
        String colNames = request.getParameter("COLIDS");
        String reportId = request.getParameter("REPORTID");
        String tableId = request.getParameter("TABLEID");

        String[] columnIds=colNames.split(",");
        String[] reportIds=new String[columnIds.length];
        for(int i=0;i<reportIds.length;i++){
            reportIds[i]=request.getParameter(columnIds[i]);
         
            }

        PbGraphsManager client = new PbGraphsManager();
        client.updateColumnDrillDown(columnIds, reportIds, tableId);
       
    %>
    <body onload="refreshParent();">
    </body>
</html>
