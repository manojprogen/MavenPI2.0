
<%--
    Document   : PbFXGraphXMLBuild
    Created on : Dec 13, 2009, 2:21:32 PM
    Author     : santhosh.kumar@progenbusiness.com
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.HashMap,java.util.ArrayList,prg.db.Container,prg.db.PbReturnObject" %>
<%@page import="com.progen.report.charts.PbGraphDisplay,com.progen.charts.PbFxXML,com.progen.reportview.bd.PbReportViewerBD,com.progen.charts.ProgenChartDisplay" %>
<%
            PbFxXML FxXML = new PbFxXML();
            PbReportViewerBD repViewBD = new PbReportViewerBD();
            String reportId = request.getParameter("reportId");
            String graphId = request.getParameter("graphId");
            String dbrdId = request.getParameter("dbrdId");
            HashMap GraphDetails = null;
            StringBuffer sbuffer = new StringBuffer("");
            HashMap singleRecord = new HashMap();
            PbGraphDisplay pbDisplay = new PbGraphDisplay();
            HashMap ParametersMap = null;
            HashMap map = null;
            Container container = null;
            ArrayList Parameters = new ArrayList();
            ArrayList REP = new ArrayList();
            ArrayList ParameterNames = new ArrayList();
            String[] viewByElementIds = new String[0];
            String[] viewByDispNames = new String[0];
            ArrayList grpDetails = new ArrayList();
            map = (HashMap) session.getAttribute("PROGENTABLES");
            container = (Container) map.get(dbrdId);
            ParametersMap = container.getParametersHashMap();
            Parameters = (ArrayList) ParametersMap.get("Parameters");
            ParameterNames = (ArrayList) ParametersMap.get("Parameters");
            if (Parameters != null && Parameters.size() != 0) {
                viewByElementIds = new String[1];
                viewByDispNames = new String[1];
                if (((String[]) Parameters.toArray(new String[0]))[0].equalsIgnoreCase("TIME")) {
                    viewByElementIds[0] = ((String[]) Parameters.toArray(new String[0]))[0];
                    viewByDispNames[0] = ((String[]) ParameterNames.toArray(new String[0]))[0];
                } else {
                    viewByElementIds[0] = "A_" + ((String[]) Parameters.toArray(new String[0]))[0];
                    viewByDispNames[0] = "A_" + ((String[]) ParameterNames.toArray(new String[0]))[0];
                }
                REP.add(viewByElementIds[0].replace("A_", ""));
            }
            
            PbReturnObject graphDataSet = repViewBD.getGraphsDataForDashBoard(reportId, graphId, request, response, session,REP);
            

            pbDisplay.setReportId(reportId);
            pbDisplay.setCurrentDispRetObjRecords(graphDataSet);
            pbDisplay.setCurrentDispRecordsRetObjWithGT(graphDataSet);
            pbDisplay.setAllDispRecordsRetObj(graphDataSet);
            pbDisplay.setOut(response.getWriter());
            pbDisplay.setResponse(response);
            pbDisplay.setViewByElementIds(viewByElementIds);
            pbDisplay.setViewByColNames(viewByDispNames);
            pbDisplay.setJscal(null);
            // pbDisplay.setJscal(request.getContextPath()+"/reportViewer.do?reportBy=viewReport&REPORTID="+reportId);//need to build
            pbDisplay.setCtxPath(request.getContextPath());
            pbDisplay.setShowGT("N");
            grpDetails = pbDisplay.getGraphByGraphIdFX(reportId, graphId, ParametersMap);
            GraphDetails = ((HashMap[]) grpDetails.get(5))[0];
            if (pbDisplay.SwapColumn) {
                pbDisplay.getFxDataSet(singleRecord);
            } else {
                pbDisplay.getFxDataSetForMeasureAnalysis(singleRecord);
            }
            sbuffer = FxXML.getFxXML(singleRecord, GraphDetails, String.valueOf(session.getAttribute("USERID")), dbrdId, graphId, null);

%>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script type="text/javascript"   src="<%=request.getContextPath()%>/javascript/dtfx.js"></script><!-- including Java FX charts related js file-->
        <script type="text/javascript"  >
            function loadJavaFXScript(){
                var myApp;
                var graphType="<%=pbDisplay.graphTypeName%>"
                var appletName="<%="Applet_" + graphId + "_" + reportId + "_" + dbrdId%>";
                javafx({
                    archive: "<%=request.getContextPath()%>/FxFiles/JavaFXApplication3.jar",
                    draggable: true,
                    height:<%=pbDisplay.graphHeight%>,
                    width:<%=pbDisplay.graphWidth%>,
                    code: "misc.MyChart",
                    name: appletName,
                    id: appletName
                }
            );
                myApp = document.getElementById(appletName);
                myApp.script.xmlStr = "<%=sbuffer.toString()%>";
            <%=FxXML.getFxChartsFunNames()%>
                }                
        </script>
        <style type="text/css">
            .flagDiv{
                width:auto;
                height:auto;
                background-color:#ffffff;
                overflow:auto;
                position:absolute;
                text-align:left;
                border:1px solid #000000;
                border-top-width: 0px;
                z-index:1001
            }
        </style>
    </head>
    <body>
        <div style="width:415px;height:300px;z-index:1000" align="left">
            <script>
                loadJavaFXScript();
            </script>
        </div>
    </body>
</html>
