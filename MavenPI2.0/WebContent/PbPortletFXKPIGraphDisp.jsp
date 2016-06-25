<%-- 
    Document   : PbPortletFXKPIGraphDisp
    Created on : Dec 18, 2009, 3:24:42 PM
    Author     : santhosh.kumar@progenbusiness.com
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" import="com.progen.charts.PbFxXML,com.progen.report.charts.*,java.io.Reader,com.progen.report.query.PbReportQuery,com.progen.portal.portlet.PortletProcessor,java.util.*,java.sql.*,prg.db.PbReturnObject,prg.db.PbDb,prg.db.Container"%>

<%
            PbFxXML FxXML = new PbFxXML();
            String PortletId = null;
            String graphType = null;
            String startRange = "";
            String firstBreak = "";
            String secondBreak = "";
            String needleValue = "";
            String endRange = "";
            StringBuffer sbuffer = null;

            try {
                PortletId = request.getParameter("PortletId");
                graphType = request.getParameter("graphType");
                startRange = request.getParameter("startRange");
                firstBreak = request.getParameter("firstBreak");
                secondBreak = request.getParameter("secondBreak");
                needleValue = request.getParameter("needleValue");
                endRange = request.getParameter("endRange");
                sbuffer = new StringBuffer("");

                sbuffer.append("<Graphs>");
                sbuffer.append("<Graph>");
                sbuffer.append("<graph_type>" + graphType + "</graph_type>");
                sbuffer.append("<graph_width>350</graph_width>");
                sbuffer.append("<graph_height>250</graph_height>");
                sbuffer.append("<allow_legend>true</allow_legend>");
                sbuffer.append("<allow_tooltip>true</allow_tooltip>");
                sbuffer.append("<legend_location>Bottom</legend_location>");
                sbuffer.append("<font_color>Black</font_color>");
                sbuffer.append("<back_ground_color>White</back_ground_color>");
                sbuffer.append("<start_range>" + startRange + "</start_range>");
                sbuffer.append("<first_break>" + firstBreak + "</first_break>");
                sbuffer.append("<second_break>" + secondBreak + "</second_break>");
                sbuffer.append("<end_range>" + endRange + "</end_range>");
                sbuffer.append("<needle_value>" + needleValue + "</needle_value>");
                sbuffer.append("</Graph>");
                sbuffer.append("</Graphs>");%>
<html>
    <head>
        <title></title>
        <script type="text/javascript"  src="<%=request.getContextPath()%>/javascript/dtfx.js"></script><!-- including Java FX charts related js file-->
      
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
    <body style="background-color:white">
        <div style="width:100%;height:100%;z-index:1000" align="left">
            <script>
                loadJavaFXScript();
            </script>
        </div> 
          <script type="text/javascript">
            function loadJavaFXScript(){
                var graphType="<%=graphType%>";
                var xmlStr="<%=sbuffer%>";
                var appletName="portletApplet_<%=PortletId%>"
                var myApp;
                javafx({
                    archive: "<%=request.getContextPath()%>/FxFiles/JavaFXApplication3.jar",
                    draggable: true,
                    height:280,
                    width:390,
                    code: "misc.MyChart",
                    name: appletName,
                    id: appletName
                }
            );
                myApp = document.getElementById(appletName);
                myApp.script.xmlStr = xmlStr;

            <%=FxXML.getFxChartsFunNames()%>
                    /*
                 if(graphType=="Thermometer"){
                    myApp.script.thermometer();
                }else{
                    myApp.script.meter();
                }
                     */
                }
        </script>
    </body>
</html>

<%} catch (Exception exp) {
                exp.printStackTrace();
            }%>

