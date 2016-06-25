<%-- Create a Manager object and call adduser method to add a user taking a params object--%>

<%--<jsp:useBean id="params" class="pb.businessarea.params.PbBusinessAreaParams" scope="request"/>
    <jsp:setProperty name="params" property="*"/>
 
<jsp:useBean id="Client" class="pb.businessarea.client.PbBusinessAreaManager" scope="request"/>
    <jsp:setProperty name="Client" property="*"/>    --%>
   
 
 <%@page import="utils.db.ProgenConnection"%>
 <%@page import="prg.db.PbReturnObject" %>
 <%@page import="java.sql.*"%>
<%@page import="java.util.*" %>
<%@page import="prg.db.Container"%>
<%@page import="com.progen.reportview.action.SnapshotDesigner"%>

<%
        String userId = String.valueOf(session.getAttribute("USERID"));
////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("userId-----------"+userId);
    String reportId = String.valueOf(session.getAttribute("REPORTID"));
        ////////////////////////////////////////////////////////////////.println.println("reportId-----------"+reportId);
        String repcustname= request.getParameter("repcustname");
        ////////////////////////////////////////////////////////////////.println.println("repcustname---"+repcustname);
    String emails = request.getParameter("toAddress");//UrlVal
        ////////////////////////////////////////////////////////////////.println.println("repcustname---"+emails);
        String snapDate=request.getParameter("snapDate");
        ////////////////////////////////////////////////////////////////.println.println("repcustname---"+snapDate);
    String[] email = emails.split(",");
        String emailList = "";
        for (int i = 0; i < email.length; i++) {
            emailList = emailList + ",'" + email[i] + "'";
        }

        if (email.length > 0) {
    emailList = emailList.substring(1);
         }
        ArrayList displayColumns = null;
        ArrayList displayLabels = null;
        String completeurl = request.getParameter("UrlVal");
        ////////////////////////////////////////////////////////////////.println.println("completeurl--"+completeurl);
    

        ////////////////////////////////////////////////////////////////.println.println("-------------------------------------------------------------------------------");
        SnapshotDesigner cls1 = new SnapshotDesigner();
        cls1.createDocument(reportId,completeurl,request,snapDate,emailList,userId,repcustname);
        ////////////////////////////////////////////////////////////////.println.println("---------------------------------66----------------------------------------------");
%>
