<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>

<%@page import="com.progen.users.UserLayerDAO"%>


<%-- Create a Manager Class and pass a params object with userid set to delete the data --%>

<% 
    String[] userNames = request.getParameterValues("chkusers");
//String[] userNames = request.getParameterValues("deleteUsers");

    ////////////////////////////////////////////////.println.println(" userNames-=-=- "+userNames[0]);
    UserLayerDAO uDao=new UserLayerDAO();
    uDao.deleteUser(userNames);
    response.sendRedirect("AdminTab.jsp");
%>
<%--   <jsp:include page="pbUserList.jsp"/>  --%>