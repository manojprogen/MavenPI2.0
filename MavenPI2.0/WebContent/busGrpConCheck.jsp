<style>
    *{
        font-size: 12px;
    }
</style>

<%
        String connIds = null;
        connIds = String.valueOf(session.getAttribute("connId"));
        if (!(connIds == null || connIds.equalsIgnoreCase("NULL"))) {
           response.sendRedirect("pbBusGrpConTab.jsp");
        } else {
            response.sendRedirect("pbBusGrpTab.jsp");
        }

%>