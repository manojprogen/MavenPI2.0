<%@ page import="utils.db.*"%>
<%@page import="com.progen.metadata.*"%>
<%@page import="com.progen.report.params.PrgReportParams" %>
<%@page import="prg.db.Session"%>
<%@page import="prg.db.PbReturnObject" %>
<%@page import="prg.tracker.client.PbTrackerManager"%>
<%@page import="prg.tracker.bean.PbTrackerBean"%>
<%@page import="prg.tracker.db.*"%>
 <%                  FactzQry factq=new FactzQry();
                     ProgenParam pparam=new ProgenParam();
                     int oldId=Integer.parseInt(request.getParameter("REPORTID"));
                     //////////////////////////////////////////////////////////////////////.println.println("ReportId is "+oldId);
                     ReportData rd = new ReportData(oldId+"");
                     rd.getReportMetaData();
                     PrgReportParams prp = new PrgReportParams(rd);
                     prp.storeParams(request);
                     TableData td= new TableData(rd);
                     factq.ReportData1=rd;
                     String tableId =td.getTableIds(String.valueOf(oldId));
                     //////////////////////////////////////////////////////////////////////.println.println("Got report type"+oldId+"type"+ factq.ReportData1.ReportType);
                     if((factq.ReportData1.ReportType.equalsIgnoreCase("Progen 2D Report"))||(factq.ReportData1.ReportType.equalsIgnoreCase("Progen 3D Report"))||(factq.ReportData1.ReportType.equalsIgnoreCase("Progen 2D Trend"))||(factq.ReportData1.ReportType.equalsIgnoreCase("Progen 3D Trend")))
                     {   //////////////////////////////////////////////////////////////////////.println.println("redirect to 2d ");
                         response.sendRedirect("pbGet2DTracker.jsp?REPORTID=oldId");
                     }
                     else{
                         //////////////////////////////////////////////////////////////////////.println.println("redirect to normal");
                         response.sendRedirect("pbGetTracker.jsp?REPORTID=oldId");
                     }

%>