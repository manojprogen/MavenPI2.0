<%-- 
    Document   : pbStartPage
    Created on : Oct 28, 2009, 10:31:55 AM
    Author     : santhosh.kumar@progenbusiness.com
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
   <%
   String path=request.getContextPath()+"/baseAction.do?param=loginPage";
   response.sendRedirect(path); 
   %>
</html>
