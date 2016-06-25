<%-- 
    Document   : pbGraphMap
    Created on : Jul 17, 2009, 11:03:48 AM
    Author     : santhosh.kumar@progenbusiness.com
--%>

<%@page contentType="text/html" pageEncoding="windows-1252"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1252">       
    </head>
    <body>
       <body>
        <!-- use height=265px or 40% for 1440*900 resolution-->
        <IFRAME NAME="iframe4" ID="iframe4" STYLE="width:100%;height:320px;border:1" SRC="TableDisplay/pbGraphDisplayRegion.jsp?TableId=<%=request.getParameter("TableId")%>"></IFRAME>
    </body>
    </body>
</html>
