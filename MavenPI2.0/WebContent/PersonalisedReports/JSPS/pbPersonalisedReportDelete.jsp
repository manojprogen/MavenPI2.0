<%@ page import="prg.personalisedreps.client.PbPersonalisedrepManager"%>
 <%@page import="prg.personalisedreps.params.PbPersonalisedrepParams" %>
<%@ page import="prg.db.Session"%>
<%@ page import="prg.db.Session"%>
<%-- Create a Manager Class and pass a params object with userid set to delete the data --%>

<%
    Session prgSession = new Session();
    PbPersonalisedrepParams params = new PbPersonalisedrepParams();
    String[] reps = request.getParameterValues("chk2");
    for(int i=0;i<reps.length;i++)
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println(reps[i]);
    params.setPersonalisedreps(reps);
    PbPersonalisedrepManager Client = new PbPersonalisedrepManager();
    prgSession.setObject(params);
    Client.deletePersonalisedreport(prgSession);
    response.sendRedirect("pbPersonalisedReportsList.jsp");
%>
