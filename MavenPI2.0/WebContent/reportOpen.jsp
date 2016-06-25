<%-- 
    Document   : reportOpen
    Created on : Jan 15, 2010, 2:25:14 PM
    Author     : Saurabh
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <%
        String reportUrl=String.valueOf(request.getAttribute("reportUrl"));
        ////////////////////////////////////////////////.println.println("reportUrl--"+reportUrl);
        response.sendRedirect(reportUrl);
        %>
    </body>
</html>
