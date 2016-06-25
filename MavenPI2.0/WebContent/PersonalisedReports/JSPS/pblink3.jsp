<%-- 
    Document   : pblink3
    Created on : Jun 27, 2009, 2:31:45 PM
    Author     : Saurabh
--%>

<%@page contentType="text/html" pageEncoding="windows-1252"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
        <title>JSP Page</title>
    </head>
    <body>
        <%
        String cpath=request.getParameter("cpath");
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("path==="+cpath);
        response.sendRedirect(cpath);
        %>

    </body>
</html>
