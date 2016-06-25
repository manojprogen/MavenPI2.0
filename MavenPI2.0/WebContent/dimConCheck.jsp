
<!doctype html>
<%@page import="prg.db.PbDb,prg.db.PbReturnObject" %>

<%
        String connIds = null;
        connIds = String.valueOf(session.getAttribute("connId"));
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("connId in dim-->" + connIds);
      if(!(connIds==null || connIds.equalsIgnoreCase("NULL")) ){
           ////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("in redirect to with  con");
            response.sendRedirect("pbDimensionTab.jsp");
                   
        } else {
           ////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("in redirect to with null con");
            response.sendRedirect("pbDimensionTabCon.jsp");

        }

%>