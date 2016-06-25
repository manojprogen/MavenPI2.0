<%-- 
    Document   : textKpiTableComments
    Created on : 20 Jun, 2012, 12:59:48 PM
    Author     : Ramesh janakuttu
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"  import="com.progen.dashboardView.db.DashboardViewerDAO,java.util.HashMap" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">


<%
String paramid=request.getParameter("paramid");
String dashletId=request.getParameter("dashletId");
String paramvalue=request.getParameter("paramvalue");
String userId = "";
userId = (String) session.getAttribute("USERID");
DashboardViewerDAO dao=new DashboardViewerDAO();
String comment=dao.getTextKpiComment(paramvalue,dashletId);
String date=dao.getTextKpiAllCommmentDetails(paramvalue);
String user=dao.getUserName(userId);
String contxtPath=request.getContextPath();

%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
                <script type="text/javascript" src="<%=contxtPath%>/javascript/lib/jquery/js/jquery-1.4.2.min.js"></script>
        <script type="text/javascript" src="<%=contxtPath%>/javascript/dashboardDesign.js"></script>
        <link type="text/css" href="<%=contxtPath%>/stylesheets/metadataButton.css" rel="stylesheet" />
    </head>
    <body>
        <form name="myForm" method="post" >
            <table width="100%"  >
                <%if (comment!=null) {%>

                <%--<tr align="right" style="width:100%">
                    <td width="100%" align="right"><a href="javascript:void(0)" style="text-decoration:none;cursor:pointer" onclick="clearComments()"><h7>Clear all Comments</h5></a></td>
                </tr>--%>
                <%--<tr style="width:100%">
                    <td width="100%" align="center">
                        <textarea id="existCommentArea" name="existCommentArea" cols="40" rows="9"  style="overflow:auto">
                            <%=comment%>
                        </textarea>
                    </td>
                </tr>
                <tr style="width:100%">
                    <td width="100%" align="center">
                        <textarea id="commentArea" name="commentArea" cols="40" rows="9" style="overflow:auto">
                          <%=comment%>
                        </textarea>
                    </td>
                </tr>--%>
               
                <tr style="width:100%">
                    <td width="100%" align="center"><textarea id="commentArea" name="commentArea" cols="40" rows="9" style="overflow:auto">Date:<%=date%>
UserName:<%=user%>
Comment:<%=comment%>
                       
                      </textarea></td>
                </tr>

                <%}
 else {%>
                <tr style="width:100%">
                    <td width="100%" align="center"><textarea id="commentArea" name="commentArea" cols="40" rows="9" style="overflow:auto"></textarea></td>
                </tr>
                <%}%>
                <tr style="width:100%" align="center">
                    <td>
                    <table align="center"  width="100%">
                        <tr>
                            <td width="50%" align="center">
                                <center>
                                    <input type="button" class="navtitle-hover" value="Save" onclick="saveTextKpiComment('<%=paramid%>','<%=dashletId%>','<%=userId%>')">
                                </center>
                            </td>
                        </tr>
                    </table>
                   </td>
                </tr>
            </table>
        </form>
    </body>
</html>
