<%--
    Document   : pbGraphChanges
    Created on : Jul 23, 2009, 11:40:07 AM
    Author     : santhosh.kumar@progenbusiness.com
--%>

<%@page contentType="text/html" pageEncoding="windows-1252"%>
<%@ page import="prg.db.PbReturnObject" %>
<%--<%@page  import="prg.graphs.client.*" %>--%>
<%@ page import="prg.db.Container" %>
<%@ page import="java.util.HashMap,java.util.ArrayList" %>
<%      //for clearing cache
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
            HashMap map = null;
            HashMap swapGraphAnalysis = null;
            Container container = null;
            HashMap GraphHashMap = null;
            HashMap singleGraphDetails = null;
            String grpType = null;
            String grpSize = null;
            String grpId = null;
            String tabId = null;
            String TableId = null;
            String graphChange = null;
            String presCollist = null;
            String columnKeys = null;
            String columnAxis = null;
            String swapBy = null;


            HashMap GraphTypesHashMap = null;
            HashMap GraphSizesHashMap = null;
            HashMap GraphClassesHashMap = null;
            HashMap GraphSizesDtlsHashMap = null;
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
    </head>
    <%

            if (session.getAttribute("PROGENTABLES") != null) {
                map = (HashMap) session.getAttribute("PROGENTABLES");
                grpType = request.getParameter("grptypid");
                grpSize = request.getParameter("grpsizeid");
                grpId = request.getParameter("gid");
                tabId = request.getParameter("tabId");
                TableId = request.getParameter("TableId");
                graphChange = request.getParameter("graphChange");
                presCollist = request.getParameter("presCollist");

                columnKeys = request.getParameter("columnKeys");
                columnAxis = request.getParameter("columnAxis");
                swapBy = request.getParameter("swapBy");


                container = (Container) map.get(tabId);

                GraphHashMap = container.getGraphHashMap();

                GraphClassesHashMap = (HashMap) session.getAttribute("GraphClassesHashMap");
                GraphSizesDtlsHashMap = (HashMap) session.getAttribute("GraphSizesDtlsHashMap");

                try {
                    //PbGraphsManager client = new PbGraphsManager();

                    if (GraphHashMap != null) {
                        if (graphChange != null) {
                            if (graphChange.equalsIgnoreCase("GrpType")) {
                                if (grpId != null && grpType != null) {
                                    if (!("".equalsIgnoreCase(grpId)) && !("".equalsIgnoreCase(grpType))) {
                                        singleGraphDetails = (HashMap) GraphHashMap.get(grpId);
                                        if (singleGraphDetails != null) {
                                            singleGraphDetails.put("graphTypeName", grpType);
                                            singleGraphDetails.put("graphClassName", String.valueOf(GraphClassesHashMap.get(grpType)));
                                        }
                                        //code to change graph types
                                    }
                                }
                            } else if (graphChange.equalsIgnoreCase("GrpSize")) {
                                if (grpId != null && grpSize != null) {

                                    if (!("".equalsIgnoreCase(grpId)) && !("".equalsIgnoreCase(grpSize))) {
                                        singleGraphDetails = (HashMap) GraphHashMap.get(grpId);
                                        if (singleGraphDetails != null) {
                                            singleGraphDetails.put("graphSize", grpSize);
                                            ArrayList alist = (ArrayList) GraphSizesDtlsHashMap.get(grpSize);
                                            singleGraphDetails.put("graphWidth", String.valueOf(alist.get(0)));
                                            singleGraphDetails.put("graphHeight", String.valueOf(alist.get(1)));
                                        }
                                    }
                                }
                            } else if (graphChange.equalsIgnoreCase("GrpCols")) {
                                if (grpId != null && TableId != null && presCollist != null) {
                                    if (!("".equalsIgnoreCase(grpId) && "".equalsIgnoreCase(TableId) && "".equalsIgnoreCase(presCollist))) {
                                    }
                                }
                            } else if (graphChange.equalsIgnoreCase("AxisSettings")) {
                                if (grpId != null && TableId != null && columnKeys != null && columnAxis != null) {
                                }
                            } //SwapGraph
                            else if (graphChange.equalsIgnoreCase("SwapGraph")) {
                                if (grpId != null && swapBy != null) {

                                    swapGraphAnalysis = container.getSwapGraphAnalysis();
                                    swapGraphAnalysis.put(grpId, swapBy);
                                    container.setSwapGraphAnalysis(swapGraphAnalysis);
                                }
                            }
                        }
                    } else {
                    }


                    response.sendRedirect("pbGraphDisplayRegion.jsp?tabId=" + tabId);
                } catch (Exception exp) {
                    exp.printStackTrace();
                    //parent.document.forms.forms[0].action="";
                    //parent.document.forms.forms[0].submit();
                }
            } else {
            }

    %>
    <body>
    </body>
</html>
