<%-- 
    Document   : pbAddColumns.jsp
    Created on : Jul 20, 2009, 6:55:13 PM
    Author     : santhosh.kumar@progenbusiness.com
--%>

<%@page contentType="text/html" pageEncoding="windows-1252" import="java.sql.*,utils.db.*,prg.db.*,prg.table.params.PbTableParams,prg.table.client.PbTableManager"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<%//for clearing cache
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
%>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
    </head>
    
        <%
        try{
        String columnIds=request.getParameter("columnIds");
        String reportId=request.getParameter("reportId");
        String tableId=request.getParameter("tableId");
        String lastColumnSequence=request.getParameter("lastSeq");

        
        String[] queryColumnIds =columnIds.split(";");

        Session prgSession=new Session();
        PbTableParams params=new PbTableParams();
        params.setQueryColumns(queryColumnIds);
        params.setReportId(reportId);
        params.setTableId(tableId);
        params.setLastColumnSequence(lastColumnSequence);

        prgSession.setObject(params);
        PbTableManager manager=new PbTableManager();
        manager.addMoreColumns(prgSession);
        //response.sendRedirect(request.getContextPath()+"/reportRunner.jsp");
        }
        catch(Exception exp){
            exp.printStackTrace();
            }
        %>
    <%--<body onload="javascript:parent.frmParameter.submit();">

    </body>--%>
</html>
