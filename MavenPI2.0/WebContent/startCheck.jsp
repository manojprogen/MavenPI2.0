<%@page pageEncoding="UTF-8" contentType="text/html"  %>

<%
//            System.out.println("***********StartCheck==========================================**************");
            String strpage ="landingPage.jsp";
                strpage=  session.getAttribute("startPage").toString();
            if (strpage == null || strpage.equalsIgnoreCase("")) {
                response.sendRedirect("landingPage.jsp");
            } else {
                response.sendRedirect(strpage);
            }
        %>
 
