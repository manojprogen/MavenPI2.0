<%-- 
    Document   : KillSession
    Created on : 5 Jul, 2012, 12:41:44 PM
    Author     : progen
--%>
<%@page contentType="text/html" pageEncoding="UTF-8" import="com.progen.db.PbBaseDAO,prg.db.PbReturnObject,java.util.Date,java.text.ParseException,java.text.DateFormat,java.text.SimpleDateFormat,java.util.Locale"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%
   PbBaseDAO dao=new PbBaseDAO();
   PbReturnObject retObj=dao.getAllActiveSessions();
   String currentlyloggedinUserId=(String) request.getSession(false).getAttribute("USERID");
   String contextPath=request.getContextPath();
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <script src="<%=contextPath%>/javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
         <script type="text/javascript" src="<%=contextPath%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>
         <script type="text/javascript" src="<%=contextPath%>/javascript/scripts.js"></script>
         
    </head>
    <body>
        <table align="center" id="tablesorterUserList" class="tablesorter"  style="width:50%" cellpadding="0" cellspacing="1">
            <thead>
                <tr align="center">
                    <th>User Id</th>
                    <th>Session</th>
                    <th>Login Time</th>
                    <th>Duration</th>
                </tr>
            </thead>
            <tbody>
             <% if(retObj!=null && retObj.getRowCount()>0){
                for(int i=0;i<retObj.getRowCount();i++){
                    String userId=retObj.getFieldValueString(i,0);
                    
                if(!userId.equalsIgnoreCase(currentlyloggedinUserId)) { %>
            <tr>
                <td align="center"><%=retObj.getFieldValueString(i,"USER_NAME")%></td>
                <td align="center"><a onclick="javascript:killSpecificSession('<%=retObj.getFieldValueString(i,"USER_ID")%>','<%=retObj.getFieldValueString(i,"SESSION_ID")%>')" title="kill Session">Kill Session</a></td>
                <td align="center"><%=retObj.getFieldValueString(i,"LOGIN_TIME")%></td>
                <%
               long diff = dao.getTimeDuration(retObj.getFieldValueString(i,"LOGIN_TIME"));
                %>
                <td align="center"><%= diff %> mins</td>

            </tr>
            <%
                }
                }
                }%>
            </tbody>
        </table>
            <script type="text/javascript">
            function killSpecificSession(userId,sessionId){
                    $.ajax({
                        url: "<%=request.getContextPath()%>/baseAction.do?param=killSpecficSession&userId="+userId+"&sessionId="+sessionId,
                        success: function(data){
                           if(data=='success'){
                               window.location.href = window.location.href;
                           }
                        }
                    });
            }
         </script>
    </body>
</html>
