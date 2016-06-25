<%-- 
    Document   : PbFxDashBoardKPIGraph
    Created on : Feb 24, 2010, 7:15:03 PM
    Author     : Administrator
--%>
<%@page contentType="text/html" pageEncoding="UTF-8" import="com.progen.dashboardView.bd.PbDashboardViewerBD,com.progen.charts.PbFxXML,com.progen.report.charts.PbGraphDisplay,java.util.HashMap,java.util.ArrayList,prg.db.Container,prg.db.PbReturnObject" %>
<%

            PbFxXML FxXML = new PbFxXML();
            PbDashboardViewerBD dashViewerBD = new PbDashboardViewerBD();
            HashMap singleRecord = new HashMap();
            PbGraphDisplay GraphDisplay = new PbGraphDisplay();
            String userId = "";
            String xmlStr = "";

            String startrange = request.getParameter("startrange");
            String endrange = request.getParameter("endrange");
            String firstbreak = request.getParameter("firstbreak");
            String secondbreak = request.getParameter("secondbreak");
            String needlevalue = request.getParameter("needlevalue");
            String height = request.getParameter("graphHeight");
            String width = request.getParameter("graphWidth");
            String dashBoardId = request.getParameter("dashBoardId");
            String graphType = request.getParameter("kpigrpType");


            singleRecord.put("startrange", startrange);
            singleRecord.put("endrange", endrange);
            singleRecord.put("firstbreak", firstbreak);
            singleRecord.put("secondbreak", secondbreak);
            singleRecord.put("needlevalue", needlevalue);

            GraphDisplay.graphTypeName = graphType;
            GraphDisplay.graphWidth = width;
            GraphDisplay.graphHeight = height;
            GraphDisplay.graphId = dashBoardId;

            

            if (session.getAttribute("USERID") != null && session.getAttribute("PROGENTABLES") != null) {
                userId = String.valueOf(session.getAttribute("USERID"));
                dashViewerBD.setUserId(userId);
                xmlStr = dashViewerBD.buildKPIXML(singleRecord, dashBoardId, GraphDisplay).toString();
                String path=request.getContextPath();

%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script src="<%=path%>/javascript/dtfx.js"></script><!-- including Java FX charts related js file-->
        
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
        <script>
            loadJavaFXScript();
        </script>
        <script type="text/javascript">
            function loadJavaFXScript(){
                var myApp;
                var appletName="DashApplet_DBDesigner_<%=dashBoardId%>";
                var graphType="<%=graphType%>";
                javafx({
                    archive: "<%=request.getContextPath()%>/FxFiles/JavaFXApplication3.jar",
                    draggable: true,
                    height:"<%=height%>",
                    width:"<%=width%>",
                    code: "misc.MyChart",
                    name: appletName,
                    id: appletName
                }
            );
                myApp = document.getElementById(appletName);
                myApp.script.xmlStr = "<%=xmlStr%>";              

            <%=FxXML.getFxChartsFunNames()%>
                }

        </script>
    </body>
</html>
<%
            } else {
                out.print("Exception occurred...Plese logout ang login");
            }%>


