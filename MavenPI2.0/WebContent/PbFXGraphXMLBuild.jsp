<%--
    Document   : PbFXGraphXMLBuild
    Created on : Dec 13, 2009, 2:21:32 PM
    Author     : santhosh.kumar@progenbusiness.com
--%>
<%@page contentType="text/html" pageEncoding="UTF-8" import="com.progen.report.charts.PbGraphDisplay,com.progen.charts.PbFxXML,java.util.HashMap,java.util.ArrayList,prg.db.Container,prg.db.PbReturnObject" %>
<%
            String graphId = request.getParameter("graphId");
            String reportId = request.getParameter("REPORTID");
            String userId = null;
            HashMap map = null;
            Container container = null;
            HashMap CrossTabGraphHashMap = null;
            HashMap GraphHashMap = null;
            HashMap TableHashMap = null;
            HashMap GraphDetails = null;
            StringBuffer sbuffer = new StringBuffer("");
            HashMap singleRecord = new HashMap();
            PbGraphDisplay GraphDisplay = new PbGraphDisplay();
            String graphType = null;
            ArrayList CEP = null;
            PbFxXML FxXML = new PbFxXML();

         if (session.getAttribute("PROGENTABLES") != null) {
                map = (HashMap) session.getAttribute("PROGENTABLES");
                if (map.get(reportId) != null) {
                    userId = String.valueOf(session.getAttribute("USERID"));
                    container = (Container) map.get(reportId);
                    GraphHashMap = container.getGraphHashMap();
                    TableHashMap = container.getTableHashMap();
                    CrossTabGraphHashMap = container.getCrossTabGraphHashMap();
                        //.println("count====="+Integer.parseInt(container.getColumnViewByCount()));

                    if (TableHashMap.get("CEP") != null) {
                        CEP = (ArrayList) TableHashMap.get("CEP");
                        if (CEP == null || CEP.size() == 0) {
                            CEP = new ArrayList();
                        }
                    }
                    if (CEP != null && CEP.size() != 0) {
                        GraphDetails = (HashMap) CrossTabGraphHashMap.get(graphId);
                    } else {
                        GraphDetails = (HashMap) GraphHashMap.get(graphId);
                    }



                    graphType = String.valueOf(GraphDetails.get("graphTypeName"));
                    Boolean SwapColumn = GraphDetails.get("SwapColumn") == null ? true : Boolean.parseBoolean(String.valueOf(GraphDetails.get("SwapColumn")));

                    GraphDisplay.setViewByColNames((String[]) GraphDetails.get("viewByElementIds"));
                    GraphDisplay.setViewByElementIds((String[]) GraphDetails.get("viewByElementIds"));
                    GraphDisplay.setCurrentDispRecordsRetObjWithGT(container.getRetObj());
                    GraphDisplay.setCurrentDispRetObjRecords(container.getRetObj());
                    GraphDisplay.setAllDispRecordsRetObj(container.getRetObj());

                    GraphDisplay.getFxGraphHeadersByGraphMap(GraphDetails);
                    if (SwapColumn) {
                        if(CEP!=null){
                         singleRecord.put("columnviewby",String.valueOf(CEP.size()));
                        }
                        GraphDisplay.getFxDataSet(singleRecord);
                    } else {
                         if(CEP!=null){
                         singleRecord.put("columnviewby",String.valueOf(CEP.size()));
                        }
                         GraphDisplay.getFxDataSetForMeasureAnalysis(singleRecord);
                    }
                    sbuffer = FxXML.getFxXML(singleRecord, GraphDetails, userId, reportId, graphId, null);
                }
            } else {
            }

%>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script type="text/javascript"   src="<%=request.getContextPath()%>/javascript/dtfx.js"></script><!-- including Java FX charts related js file-->
       
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
         <script type="text/javascript" >
            function loadJavaFXScript(){
                var myApp;
                var graphType="<%=graphType%>"
                var appletName="<%="Applet_" + graphId + "_" + reportId%>";
                javafx({
                    archive: "<%=request.getContextPath()%>/FxFiles/JavaFXApplication3.jar",
                    draggable: true,
                    height:<%=GraphDisplay.graphHeight%>,
                    width:<%=GraphDisplay.graphWidth%>,
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
    </body>
</html>
