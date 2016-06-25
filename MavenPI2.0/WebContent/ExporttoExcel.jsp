<%-- 
    Document   : ExportTest
    Created on : Dec 18, 2010, 11:00:01 AM
    Author     : progen
--%>

<%@page import="java.io.IOException,com.progen.report.bd.ExcelBD,prg.db.Container"%>
<%@page contentType="application/vnd.ms-excel" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <%
        String reportId=request.getParameter("reportId");
        
        Container container = Container.getContainerFromSession(request, reportId);
        ExcelBD repTableBd = new ExcelBD();
        String reportName = container.getReportName();

        try {
            ServletOutputStream outStream = response.getOutputStream();
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename="+reportName+".xls");
            repTableBd.exportRTExcelColumns(container, outStream);
            outStream.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        %>
    </body>
</html>
