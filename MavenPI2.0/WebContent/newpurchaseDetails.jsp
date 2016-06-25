<%-- 
    Document   : newpurchaseDetails
    Created on : 29 Aug, 2012, 4:12:40 PM
    Author     : swathi
--%>
<%@page import="com.progen.action.UserStatusHelper"%>
<%@page import="prg.db.Session"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.*"%>
<%@page import="java.io.InputStream"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%
          //for clearing cache
           response.setHeader("Cache-Control", "no-store");
           response.setHeader("Pragma", "no-cache");
           response.setDateHeader("Expires", 0);
           int qdCnt=0;
           int paCnt=0;
        UserStatusHelper helper=UserStatusHelper.getUserStatusHelper();
       if (helper == null) {
           Properties helperProps = new Properties();
           InputStream servletStream =request.getSession().getServletContext().getResourceAsStream("/WEB-INF/GenerateAssignModule.xml");
             if (servletStream != null) {
        try {
                   helperProps.loadFromXML(servletStream);
                   helper.createUserStatusHelper(helperProps);
                   helper=UserStatusHelper.getUserStatusHelper();
               } catch (Exception e) {
                   e.printStackTrace();
            }
           }
       }
       qdCnt=helper.getQueryStudionTotalCnt();
       paCnt=helper.getPowerAnalyzerTotalCnt();
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <script src="<%=request.getContextPath()%>/javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.tablesorter.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/tablesorter/addons/pager/jquery.tablesorter.pager.js"></script>
         <link type="text/css" href="<%=request.getContextPath()%>/jQuery/jquery/demos.css" rel="stylesheet" />
        <script type="text/javascript" src="<%=request.getContextPath()%>/jQuery/jquery/ui/jquery.easing.1.3.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/ui.tabs.js"></script>
    </head>
    <body>
        <table align="center" id="tablesorterUserList" class="tablesorter"  style="width:50%" cellpadding="0" cellspacing="1">
            <thead>
            <th>Module</th>
            <th>License</th>
            </thead>
            <tr>
                <td width="50px">Admin</td>
                <td width="50px"><%=qdCnt%></td>
            </tr>
            <tr>
                <td width="50px">Power Analyzer</td>
                <td width="50px"><%=paCnt%></td>
            </tr>
        </table>
    </body>
</html>