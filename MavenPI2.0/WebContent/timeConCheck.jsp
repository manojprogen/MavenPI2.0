
<!doctype html>
<%@page import="prg.db.PbDb" %>
<%@page import="prg.db.PbReturnObject" %>

<%
         String connIds=null;
         connIds=String.valueOf(session.getAttribute("connId"));
         //////////////////////////////////////////////////////////////////////////////////////////.println.println("connId in bus grp-->"+connIds);

           if(!(connIds==null || connIds.equalsIgnoreCase("NULL")) ){
            //////////////////////////////////////////////////////////////////////////////////////////.println.println("in redirect to with time  con");
            response.sendRedirect("timeSetUpCon.jsp");
            }else{
             //////////////////////////////////////////////////////////////////////////////////////////.println.println("in redirect to with in time null con");
            response.sendRedirect("timeSetUp.jsp");
           }

%>