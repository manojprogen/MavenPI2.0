
<%--
    Document   : PbFXGraphXMLBuild
    Created on : Dec 13, 2009, 2:21:32 PM
    Author     : santhosh.kumar@progenbusiness.com
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" import="com.progen.report.charts.PbGraphDisplay,com.progen.charts.PbFxXML,prg.db.Container,prg.db.PbReturnObject,java.util.HashMap,java.util.ArrayList"%>
<%
            String divId = request.getParameter("divId");
            String DashBoardID = request.getParameter("DashBoardID");
            String userId = null;
            HashMap map = null;
            Container container = null;
            HashMap NewDbrdGraphHashMap = null;
            HashMap GraphDetails = null;
            StringBuffer sbuffer = new StringBuffer("");
            HashMap singleRecord = new HashMap();
            PbGraphDisplay GraphDisplay = new PbGraphDisplay();
            String graphType = null;
            PbFxXML FxXML = new PbFxXML();
            PbReturnObject RetObj = null;
            if (session.getAttribute("PROGENTABLES") != null) {
                map = (HashMap) session.getAttribute("PROGENTABLES");
                if (map.get(DashBoardID) != null) {
                    userId = String.valueOf(session.getAttribute("USERID"));
                    container = (Container) map.get(DashBoardID);
                    NewDbrdGraphHashMap = container.getNewDbrdGraph();
                    GraphDetails = (HashMap) NewDbrdGraphHashMap.get(divId);
                    RetObj = (PbReturnObject) GraphDetails.get("RetObj");
                    graphType = String.valueOf(GraphDetails.get("graphTypeName"));
                    Boolean SwapColumn = GraphDetails.get("SwapColumn") == null ? true : Boolean.parseBoolean(String.valueOf(GraphDetails.get("SwapColumn")));
                    GraphDisplay.setCurrentDispRecordsRetObjWithGT(RetObj);
                    GraphDisplay.setCurrentDispRetObjRecords(RetObj);
                    GraphDisplay.setAllDispRecordsRetObj(RetObj);
                    GraphDisplay.getFxGraphHeadersByGraphMap(GraphDetails);
                    if (SwapColumn) {
                        GraphDisplay.getFxDataSet(singleRecord);
                    } else {
                        GraphDisplay.getFxDataSetForMeasureAnalysis(singleRecord);
                    }
                    sbuffer = FxXML.getFxXML(singleRecord, GraphDetails, userId, DashBoardID, divId, null);
                }
            } else {
            }
            String contextPath=request.getContextPath();

%>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script type="text/javascript"   src="<%=contextPath%>/javascript/dtfx.js"></script><!-- including Java FX charts related js file-->
        
       
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
        <script type="text/javascript"  >

            function loadJavaFXScript(){
                var myApp;
                var graphType="<%=graphType%>"
                var appletName="<%="Applet_" + divId + "_" + DashBoardID%>";
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
                function test(){

                }
        </script>
    </body>
</html>
