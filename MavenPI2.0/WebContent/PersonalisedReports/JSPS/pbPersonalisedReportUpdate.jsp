 <%@ page import="prg.personalisedreps.client.PbPersonalisedrepManager"%>
 <%@page import="prg.personalisedreps.params.PbPersonalisedrepParams" %>
 <%@ page import="prg.db.Session"%> 
 <%@page import="prg.db.PbDb"%> 
 <%@page import="utils.db.ProgenConnection"%>
 <%@page import="prg.db.PbReturnObject" %>
 <%@page import="java.sql.*"%>
 <%@ page import="utils.db.*"%>
<% 
    int rowCount=0;
    PbPersonalisedrepParams params = new PbPersonalisedrepParams();
    PbPersonalisedrepManager client =new PbPersonalisedrepManager();
    Session prgSession = new Session();
   // PbReturnObject retObj = client.getPersonalisedReportList(prgSession);         
    //rowCount =retObj.getRowCount();
    try
    {
     params.setPersonalisedId(request.getParameter("personalisedid"));
     params.setRepCustname(request.getParameter("repcustname"));
     params.setRepSeqno(request.getParameter("repseqno"));
     prgSession.setObject(params);
     client.updatePersonalisedReport(prgSession);
     ////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("successfully populated");
    }
    catch(Exception ex)
    {
     ////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println(ex.getMessage());
    }
  response.sendRedirect("pbPersonalisedReportsList.jsp");
%>
     