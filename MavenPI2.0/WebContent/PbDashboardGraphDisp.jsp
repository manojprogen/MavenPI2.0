<%--
    Document   : PbDashboardGraphDisp
    Created on : Dec 19, 2009, 5:40:10 PM
    Author     : santhosh.kumar@progenbusiness.com
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" import="prg.db.Container,prg.db.PbReturnObject,java.util.HashMap,java.util.ArrayList,com.progen.report.charts.PbGraphDisplay,com.progen.dashboardView.bd.PbDashboardViewerBD,com.progen.charts.PbFxXML"%>

<%          //for clearing cache
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
            // graphBuilder = new StringBuffer("");
            PbFxXML FxXML = new PbFxXML();
            PbDashboardViewerBD dashViewerBD = new PbDashboardViewerBD();
            ArrayList xmlDetails = null;
            String height = null;
            String width = null;
            String graphType = null;
            String xmlStr = null;

            String userId = null;

            String dashletId = request.getParameter("dashletId");
            String dashBoardId = request.getParameter("dashBoardId");
            String refReportId = request.getParameter("refReportId");
            String kpiMasterId = request.getParameter("kpiMasterId");
            String dispSequence = request.getParameter("dispSequence");
            String dispType = request.getParameter("dispType");
            String dashletName = request.getParameter("dashletName");
            String graphId = request.getParameter("graphId");
            String myGraphType = request.getParameter("myGraphType");
            //.println("in jsp myGraphType="+myGraphType);


            if (session.getAttribute("USERID") != null && session.getAttribute("PROGENTABLES") != null) {
                HashMap map = null;
                Container container = null;


                map = (HashMap) session.getAttribute("PROGENTABLES");
                if (map.get(dashBoardId) != null) {
                    userId = String.valueOf(session.getAttribute("USERID"));
                    //.println("userId="+userId);
                    container = (Container) map.get(dashBoardId);
                    container.getDashboardCollection().setMyGraphType(myGraphType);
                    dashViewerBD.setContainer(container);
                    if (dispType != null) {
                        dashViewerBD.setDashBoardId(dashBoardId);
                        dashViewerBD.setDispType(dispType);
                        dashViewerBD.setGraphId(graphId);
                        dashViewerBD.setRefReportId(refReportId);
                        dashViewerBD.setDispSequence(dispSequence);
                        dashViewerBD.setDashletId(dashletId);
                        dashViewerBD.setServletRequest(request);
                        dashViewerBD.setServletResponse(response);
                        dashViewerBD.setUserId(userId);
                        xmlDetails = dashViewerBD.buildFXGraphs();
                        //////////.println("xmlDetails is " + xmlDetails);
                        height = xmlDetails.get(0).toString();
                        width = xmlDetails.get(1).toString();
                        graphType = xmlDetails.get(2).toString();
                        xmlStr = xmlDetails.get(3).toString();
                        //////////.println("xmlStr is "+xmlStr);
                        ////////.println("graphType is "+graphType);
                    }
                    //graphBuilder.append("</div");

%>
<html>
    <head>
        <title></title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script src="<%=request.getContextPath()%>/javascript/dtfx.js"></script><!-- including Java FX charts related js file-->
       
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
                var appletName="DashApplet_<%=dashBoardId%>_<%=dashletId%>";
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
<%}
            } else {%>
<!--code to submit session expired page through parent document-->
<%}%>


