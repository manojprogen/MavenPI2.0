<%
        String connIds = null;
        connIds = String.valueOf(session.getAttribute("connId"));
      if(!(connIds==null || connIds.equalsIgnoreCase("NULL")) ){
              // response.sendRedirect("pbUserFolderContab.jsp");
          response.sendRedirect("pbUserFolderTab.jsp");
        } else {
             response.sendRedirect("pbUserFolderTab.jsp");

        }

%>