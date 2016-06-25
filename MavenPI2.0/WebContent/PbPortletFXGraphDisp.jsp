<%--
    Document   : PbPortletFXGraphDisp
    Created on : Dec 10, 2009, 12:26:27 PM
    Author     : santhosh.kumar@progenbusiness.com
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" import="com.progen.report.charts.*,com.progen.charts.PbFxXML,java.io.Reader,com.progen.report.query.PbReportQuery,com.progen.portal.portlet.PortletProcessor,prg.db.PbDb,prg.db.Container,prg.db.PbReturnObject,java.sql.*,java.util.*"%>


<%
            Connection con = null;
            PortletProcessor PProcessor = new PortletProcessor();
            PbReportQuery pb = new PbReportQuery();
            PbReturnObject sortPbretObj = null;
            Container container = new Container();
            PbGraphDisplay GraphDisplay = new PbGraphDisplay();
            PbFxXML FxXML = new PbFxXML();
            try {
                String REP = null;
                String perBy = null;
                String graphType = null;
                String graphClass = null;
                String PortletName = null;
                String PortletId = null;
                String userId = String.valueOf(session.getAttribute("USERID"));

                ArrayList REPList = null;
                ArrayList grpElementIds = null;
                ArrayList REPNames = null;
                ArrayList grpElementNames = null;

                HashMap GraphDetails = null;
                HashMap singleRecord = null;
                HashMap GraphClassesHashMap = (HashMap) session.getAttribute("GraphClassesHashMap");



                con =  utils.db.ProgenConnection.getInstance().getConnection();

                String portletType = "";
                Reader characterStream = null;

                PortletId = request.getParameter("PORTLETID");
                REP = request.getParameter("REP");
                perBy = request.getParameter("perBy");
                graphType = request.getParameter("graphType");

                String getXmlQuery = "select a.xml_path, nvl(b.portlet_name,a.portlet_name) portlet_name,nvl(b.portlet_desc,a.portlet_desc) portlet_desc, b.portlet_tab_id,a.portlet_type from PRG_PORTLETS_MASTER a , prg_portal_tab_details b where a.portlet_id= b.portlet_id and a.portlet_id='" + PortletId + "'";
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(getXmlQuery);
                while (rs.next()) {
                    if (rs.getClob(1) != null) {
                        characterStream = rs.getClob(1).getCharacterStream();
                    } else {
                        characterStream = null;
                    }
                    portletType = rs.getString(5);
                    PortletName = rs.getString(2);
                    if (characterStream != null) {
                        if (portletType.equalsIgnoreCase("I")) {
                            PProcessor.setCharacterStream(characterStream);
                            PProcessor.processDocument();
                            if (REP.equalsIgnoreCase("")) {
                                grpElementIds = new ArrayList();
                                grpElementNames = new ArrayList();
                                REPList = PProcessor.reportRowViewbyValues;

                                REPNames = new ArrayList();
                                for (int k = 0; k < REPList.size(); k++) {
                                    REPNames.add(PProcessor.getParameterNamesHashMap().get(String.valueOf(REPList.get(k))));
                                }
                                for (int j = 0; j < PProcessor.reportRowViewbyValues.size(); j++) {
                                    if (String.valueOf(REPList.get(j)).equalsIgnoreCase("Time")) {
                                        grpElementIds.add(String.valueOf(REPList.get(j)));
                                        grpElementNames.add("Time");
                                    } else {
                                        grpElementIds.add("A_" + String.valueOf(REPList.get(j)));
                                        grpElementNames.add(String.valueOf(REPNames.get(j)));
                                    }
                                }
                            } else {
                                grpElementIds = new ArrayList();
                                grpElementNames = new ArrayList();
                                REPNames = new ArrayList();
                                REPList = new ArrayList();
                                String[] REPStr = REP.split(",");
                                for (int k = 0; k < REPStr.length; k++) {
                                    REPList.add(REPStr[k]);
                                    REPNames.add(PProcessor.getParameterNamesHashMap().get(REPStr[k]));
                                }
                                for (int j = 0; j < REPList.size(); j++) {
                                    if (String.valueOf(REPList.get(j)).equalsIgnoreCase("Time")) {
                                        grpElementIds.add(String.valueOf(REPList.get(j)));
                                        grpElementNames.add("Time");

                                    } else {
                                        grpElementIds.add("A_" + String.valueOf(REPList.get(j)));
                                        grpElementNames.add(String.valueOf(REPNames.get(j)));
                                    }
                                }
                            }
                            for (int k = 0; k < PProcessor.reportQryElementIds.size(); k++) {
                                grpElementIds.add("A_" + PProcessor.reportQryElementIds.get(k));
                                grpElementNames.add(String.valueOf(PProcessor.reportQryColNames.get(k)));
                            }

                            pb.setQryColumns(PProcessor.reportQryElementIds);
                            pb.setColAggration(PProcessor.reportQryAggregations);
                            pb.setDefaultMeasure(String.valueOf(PProcessor.reportQryElementIds.get(0)));
                            pb.setDefaultMeasureSumm(String.valueOf(PProcessor.reportQryAggregations.get(0)));
                            pb.setRowViewbyCols(REPList);
                            pb.setColViewbyCols(PProcessor.reportColViewbyValues);
                            pb.setParamValue(PProcessor.reportParametersValues);
                            pb.setTimeDetails(PProcessor.timeDetailsArray);
                            PbReturnObject pbretObj = pb.getPbReturnObject(String.valueOf(PProcessor.reportQryElementIds.get(0)));

                            if (pbretObj != null && pbretObj.getRowCount() != 0) {
                                container.setOriginalColumns(grpElementIds);
                                int count = pbretObj.getRowCount();
                                String viewPerBy = "Top";

                                if (perBy != null && (!"".equalsIgnoreCase(perBy))) {
                                    viewPerBy = perBy.split("-")[0];
                                    count = Integer.parseInt(perBy.split("-")[1]);

                                    if (pbretObj.getRowCount() < count) {
                                        count = pbretObj.getRowCount();
                                    }
                                    if (viewPerBy.contains("Top")) {
                                        sortPbretObj = pbretObj.sort(1, "A_" + PProcessor.reportQryElementIds.get(0), "N");
                                    } else {
                                        sortPbretObj = pbretObj.sort(0, "A_" + PProcessor.reportQryElementIds.get(0), "N");
                                    }
                                    for (int j = sortPbretObj.getRowCount() - 1; j >= count; j--) {
                                        sortPbretObj.deleteRow(j);
                                    }
                                } else {
                                    perBy = "Top-10";
                                    if (count > 10) {
                                        count = 10;
                                    }
                                    sortPbretObj = pbretObj.sort(1, "A_" + PProcessor.reportQryElementIds.get(0), "N");
                                    for (int j = sortPbretObj.getRowCount() - 1; j >= count; j--) {
                                        sortPbretObj.deleteRow(j);
                                    }
                                }
                                container.setDisplayedSet(sortPbretObj);
                                if (graphType == null || ("".equalsIgnoreCase(graphType))) {
                                    graphType = String.valueOf(PProcessor.getGraphDeails().get("graphType"));
                                }
                                graphClass = String.valueOf(GraphClassesHashMap.get(graphType));

                                String[] viewByElementIds = ((String[]) REPList.toArray(new String[0]));
                                for (int i = 0; i < viewByElementIds.length; i++) {
                                    if (viewByElementIds[i].equalsIgnoreCase("Time")) {
                                    } else {
                                        viewByElementIds[i] = "A_" + viewByElementIds[i];
                                    }
                                }
                                GraphDetails = new HashMap();
                                singleRecord = new HashMap();
                                GraphDetails.put("graphId", PortletId);
                                GraphDetails.put("graphName", PortletName);
                                GraphDetails.put("graphWidth", "100");
                                GraphDetails.put("graphHeight", "100");
                                GraphDetails.put("graphClassName", graphClass);
                                GraphDetails.put("graphTypeName", graphType);
                                GraphDetails.put("barChartColumnTitles", ((String[]) grpElementNames.toArray(new String[0])));
                                GraphDetails.put("barChartColumnNames", ((String[]) grpElementIds.toArray(new String[0])));
                                GraphDetails.put("pieChartColumns", ((String[]) grpElementIds.toArray(new String[0])));
                                GraphDetails.put("viewByElementIds", viewByElementIds);

                                GraphDisplay.setCurrentDispRecordsRetObjWithGT(sortPbretObj);
                                GraphDisplay.setCurrentDispRetObjRecords(sortPbretObj);
                                GraphDisplay.setAllDispRecordsRetObj(sortPbretObj);
                                GraphDisplay.setViewByElementIds((String[]) GraphDetails.get("viewByElementIds"));
                                GraphDisplay.getFxGraphHeadersByGraphMap(GraphDetails);

                                GraphDisplay.getFxDataSet(singleRecord);

                                StringBuffer sbuffer = FxXML.getFxXML(singleRecord, GraphDetails, userId, PortletId, PortletId, null);

%>
<html>
    <head>        
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
    <body style="background-color:white">
        <div style="width:100%;height:100%;z-index:1000" align="left">
            <script>
                loadJavaFXScript();
            </script>

            <%} else {%>
            <center>No data available to display
            </center>
            <%}
                        }
                    }
                              }%>
    </body>
    <script>

            function loadJavaFXScript()
            {
                var graphType="<%=graphType%>";
                var xmlStr="<%=sbuffer%>";
                var appletName="portletApplet_<%=PortletId%>"
                var myApp;
                javafx(
                {
                    archive: "<%=request.getContextPath()%>/FxFiles/JavaFXApplication3.jar",
                    draggable: true,
                    height:285,
                    width:390,
                    code: "misc.MyChart",
                    name: appletName,
                    id: appletName

                }
            );
                myApp = document.getElementById(appletName);               
                myApp.script.xmlStr = xmlStr;
            <%=FxXML.getFxChartsFunNames()%>
                }
        </script>
</html>

<%} catch (Exception exp) {
                exp.printStackTrace();
            }%>
