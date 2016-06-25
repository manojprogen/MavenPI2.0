<%-- 
    Document   : pbKpiGraphTarget
    Created on : Jan 7, 2010, 11:24:46 AM
    Author     : mahesh.sanampudi@progenbusiness.com
--%>


<%@page contentType="text/html" pageEncoding="UTF-8" import="utils.db.ProgenConnection,prg.db.PbReturnObject,prg.db.PbDb,prg.db.Session,prg.db.Container,java.math.*,java.text.*,java.util.*,com.progen.report.pbDashboardCollection,com.progen.reportdesigner.db.DashboardTemplateDAO"%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<% String contextPath=request.getContextPath(); %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>

        <script src="<%=contextPath%>/javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/dashboardDesign.js"></script>
        <link type="text/css" href="<%=contextPath%>/stylesheets/ui.all.css" rel="stylesheet" />
        <link type="text/css" href="<%=contextPath%>/stylesheets/demos.css" rel="stylesheet" />
        <link href="<%=contextPath%>/stylesheets/javascript.css" type="text/css" rel="stylesheet">
        <link href="<%=contextPath%>/stylesheets/html.css" type="text/css" rel="stylesheet">
        <link href="<%=contextPath%>/stylesheets/css.css" type="text/css" rel="stylesheet">
        <link type="text/css" href="<%=contextPath%>/stylesheets/metadataButton.css" rel="stylesheet" />
        
    </head>
    <body>
        <form name="kpiTargetForm" method="post">
            <%
            HashMap map = new HashMap();
            Container container = null;
            long diffDays = 0;
            long perday = 0;
            String timeDim = null;
            String targetType = "";

            String foldersIds = request.getParameter("foldersIds");
            String kpiId = request.getParameter("kpiIds");
            String kpiName = request.getParameter("kpiName");
            String needleValue = request.getParameter("needleValue");
            String dashboardId = request.getParameter("dashboardId");
            if (request.getParameter("targetType") != null) {
                targetType = request.getParameter("targetType");
            }
            //////////////////.println("targettype in jsp is: " + targetType);
                DashboardTemplateDAO dashboardTemplateDAO = new DashboardTemplateDAO();
            String minTimeLevel = dashboardTemplateDAO.getUserFolderMinTimeLevel(foldersIds);

            if (request.getSession(false).getAttribute("PROGENTABLES") != null) {
                map = (HashMap) request.getSession(false).getAttribute("PROGENTABLES");
            }
            if (map.get(dashboardId) != null) {
                container = (prg.db.Container) map.get(dashboardId);
            } else {
                container = new prg.db.Container();
            }
            HashMap ParametersMap = container.getParametersHashMap();
            ArrayList timeArray = (ArrayList) ParametersMap.get("TimeDetailstList");
            HashMap timeDetsMap = (HashMap) ParametersMap.get("TimeDimHashMap");
            if (timeArray.get(1).toString().equalsIgnoreCase("PRG_STD")) {
                ArrayList month = (ArrayList) timeDetsMap.get("AS_OF_DATE");
                String dateSql="";
                if(ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER))
                 dateSql = "select datepart(dd, convert(datetime,'"+ month.get(0) +"',101))";
                else
                  dateSql = "select TO_NUMBER(TO_CHAR((TO_DATE('" + month.get(0) + "','MM/DD/YYYY')),'DD')) FROM DUAL";
                //////////////////////.println("datesql is: "+dateSql);
                PbDb pbdb = new PbDb();
                PbReturnObject retObj = pbdb.execSelectSQL(dateSql);
                if (retObj.getRowCount() > 0) {
                    diffDays = retObj.getFieldValueInt(0, 0);
                    perday = Long.parseLong(needleValue) / diffDays;
                    //////////////////.println("perday value is: " + Math.round(perday));
                    timeDim = timeArray.get(1).toString();
                }
            } else {
                ArrayList fromDate = (ArrayList) timeDetsMap.get("AS_OF_DATE1");
                ArrayList toDate = (ArrayList) timeDetsMap.get("AS_OF_DATE2");
                DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
                Date fromdt = df.parse(String.valueOf(fromDate.get(0)));
                Date todt = df.parse(String.valueOf(toDate.get(0)));
                //////////////////.println("fromdt  is: " + fromdt);
                //////////////////.println("todt  is: " + todt);
                diffDays = (todt.getTime() - fromdt.getTime()) / (24 * 60 * 60 * 1000);
                //////////////////.println("days diff is: " + diffDays);
                //perday=Double.parseDouble(String.valueOf(needleValue))/Double.parseDouble(String.valueOf(deltaDays));
                perday = Long.parseLong(needleValue) / diffDays;
                //////////////////.println("perday value is: " + Math.round(perday));
                timeDim = timeArray.get(1).toString();
            }

            %>

            <%if (targetType.equalsIgnoreCase("PRORATED")) {%>
                <table align="center">
                    <Tr>
                    <Td><font style="font-size:'1px';font-family:'verdana';"><b><%=kpiName%></b> Value is : <%=needleValue%> for <%=diffDays%> Days</font></Td>
                </Tr>
                <Tr>
                    <Td><font style="font-size:'1px';font-family:'verdana';"><b><%=kpiName%></b> Value is : <%=perday%> per Day</font></Td>
                </Tr>

            </table>
            <Table align="center">
                <Tr>
                    <Td><b>Period</b></Td>
                    <Td ALIGN="CENTER"><b>Target Value</b></Td>
                </Tr>
                <Tr>
                    <Td>Day</Td>
                    <Td><Input type="text" class="myTextbox3" name="tgtValDay" id="tgtValDay" style="width:90px;height:15px;background:white"></Td>
                </Tr>
            </Table>
            <%} else if (targetType.equalsIgnoreCase("INDIVIDUAL")) {%>
            <table align="center">
                <Tr>
                    <%if (timeArray.get(0).toString().equalsIgnoreCase("DAY") && timeArray.get(1).toString().equalsIgnoreCase("PRG_DATE_RANGE")) {%>
                    <Td><font style="font-size:'1px';font-family:'verdana';"><b><%=kpiName%></b> Value is : <%=needleValue%> for <%=diffDays%> Days</font></Td>
                </Tr>
                <Tr>
                    <Td><font style="font-size:'1px';font-family:'verdana';"><b><%=kpiName%></b> Value is : <%=perday%> per Day</font></Td>
                    <%} else {%>
                        <Td><font style="font-size:'1px';font-family:'verdana';"><b><%=kpiName%></b> Value is : <%=needleValue%></font></Td>
                    <%}%>
                    </Tr>
                </table>
                <Table align="center">
                    <Tr>
                       <Td><b>Period</b></Td>
                       <Td ALIGN="CENTER"><b>Target Value</b></Td>
                    </Tr>
                <% if (minTimeLevel.equals("5")) {
                if (timeArray.get(0).toString().equalsIgnoreCase("DAY") && timeArray.get(1).toString().equalsIgnoreCase("PRG_DATE_RANGE")) {%>
                    <Tr>
                        <Td>Day</Td>
                        <Td><Input type="text" class="myTextbox3" name="tgtValDay" id="tgtValDay" style="width:90px;height:15px;background:white"></Td>
                    </Tr>
                <%} else {%>
                    <Tr>
                    <Td>Day</Td>
                    <Td><Input type="text" class="myTextbox3" name="tgtValDay" id="tgtValDay" style="width:90px;height:15px;background:white"></Td>
                </Tr>
                <Tr>
                        <Td>Week</Td>
                        <Td><Input type="text" class="myTextbox3" name="tgtValWeek" id="tgtValWeek" style="width:90px;height:15px;background:white"></Td>
                    </Tr>
                    <Tr>
                        <Td>Month</Td>
                        <Td><Input type="text" class="myTextbox3" name="tgtValMonth" id="tgtValMonth" style="width:90px;height:15px;background:white"></Td>
                    </Tr>
                    <Tr>
                        <Td>Quarter</Td>
                        <Td><Input type="text" class="myTextbox3" name="tgtValQtr" id="tgtValQtr" style="width:90px;height:15px;background:white"></Td>
                    </Tr>
                    <Tr>
                        <Td>Year</Td>
                        <Td><Input type="text" class="myTextbox3" name="tgtValYr" id="tgtValYr" style="width:90px;height:15px;background:white"></Td>
                    </Tr>
                <%}
                } else if (minTimeLevel.equals("4")) {%>
                    <Tr>
                        <Td>Week</Td>
                        <Td><Input type="text" class="myTextbox3" name="tgtValWeek" id="tgtValWeek" style="width:90px;height:15px;background:white"></Td>
                    </Tr>
                    <Tr>
                        <Td>Month</Td>
                        <Td><Input type="text" class="myTextbox3" name="tgtValMonth" id="tgtValMonth" style="width:90px;height:15px;background:white"></Td>
                    </Tr>
                    <Tr>
                        <Td>Quarter</Td>
                        <Td><Input type="text" class="myTextbox3" name="tgtValQtr" id="tgtValQtr" style="width:90px;height:15px;background:white"></Td>
                    </Tr>
                    <Tr>
                        <Td>Year</Td>
                        <Td><Input type="text" class="myTextbox3" name="tgtValYr" id="tgtValYr" style="width:90px;height:15px;background:white"></Td>
                    </Tr>
                <%} else if (minTimeLevel.equals("3")) {%>
                    <Tr>
                        <Td>Month</Td>
                        <Td><Input type="text" class="myTextbox3" name="tgtValMonth" id="tgtValMonth" style="width:90px;height:15px;background:white"></Td>
                    </Tr>
                    <Tr>
                        <Td>Quarter</Td>
                        <Td><Input type="text" class="myTextbox3" name="tgtValQtr" id="tgtValQtr" style="width:90px;height:15px;background:white"></Td>
                    </Tr>
                    <Tr>
                        <Td>Year</Td>
                        <Td><Input type="text" class="myTextbox3" name="tgtValYr" id="tgtValYr" style="width:90px;height:15px;background:white"></Td>
                    </Tr>
                <%} else if (minTimeLevel.equals("2")) {%>
                    <Tr>
                        <Td>Quarter</Td>
                        <Td><Input type="text" class="myTextbox3" name="tgtValQtr" id="tgtValQtr" style="width:90px;height:15px;background:white"></Td>
                    </Tr>
                    <Tr>
                        <Td>Year</Td>
                        <Td><Input type="text" class="myTextbox3" name="tgtValYr" id="tgtValYr" style="width:90px;height:15px;background:white"></Td>
                    </Tr>
                <%} else if (minTimeLevel.equals("1")) {%>
                    <Tr>
                        <Td>Year</Td>
                        <Td><Input type="text" class="myTextbox3" name="tgtValYr" id="tgtValYr" style="width:90px;height:15px;background:white"></Td>
                    </Tr>
                <%}
                } else {%>
                <table align="center">
                    <Tr>
                        <%if (timeArray.get(0).toString().equalsIgnoreCase("DAY") && timeArray.get(1).toString().equalsIgnoreCase("PRG_DATE_RANGE")) {%>
                        <Td><font style="font-size:'1px';font-family:'verdana';"><b><%=kpiName%></b> Value is : <%=needleValue%> for <%=diffDays%> Days</font></Td>
                    </Tr>
                    <Tr>
                        <Td><font style="font-size:'1px';font-family:'verdana';"><b><%=kpiName%></b> Value is : <%=perday%> per Day</font></Td>
                        <%} else {%>
                        <Td><font style="font-size:'1px';font-family:'verdana';"><b><%=kpiName%></b> Value is : <%=needleValue%></font></Td>
                    <%}%>
                    </Tr>
                </table>
                <Table align="center">
                    <Tr>
                        <Td><b>Period</b></Td>
                        <Td ALIGN="CENTER"><b>Target Value</b></Td>
                    </Tr>
                    <% if (minTimeLevel.equals("5")) {
                    if (timeArray.get(0).toString().equalsIgnoreCase("DAY") && timeArray.get(1).toString().equalsIgnoreCase("PRG_DATE_RANGE")) {%>
                    <Tr>
                        <Td>Day</Td>
                        <Td><Input type="text" class="myTextbox3" name="tgtValDay" id="tgtValDay" style="width:90px;height:15px;background:white"></Td>
                    </Tr>
                    <%} else {%>
                    <Tr>
                        <Td>Day</Td>
                        <Td><Input type="text" class="myTextbox3" name="tgtValDay" id="tgtValDay" style="width:90px;height:15px;background:white"></Td>
                    </Tr>
                    <Tr>
                        <Td>Week</Td>
                        <Td><Input type="text" class="myTextbox3" name="tgtValWeek" id="tgtValWeek" style="width:90px;height:15px;background:white"></Td>
                    </Tr>
                    <Tr>
                        <Td>Month</Td>
                        <Td><Input type="text" class="myTextbox3" name="tgtValMonth" id="tgtValMonth" style="width:90px;height:15px;background:white"></Td>
                    </Tr>
                    <Tr>
                        <Td>Quarter</Td>
                        <Td><Input type="text" class="myTextbox3" name="tgtValQtr" id="tgtValQtr" style="width:90px;height:15px;background:white"></Td>
                    </Tr>
                    <Tr>
                        <Td>Year</Td>
                        <Td><Input type="text" class="myTextbox3" name="tgtValYr" id="tgtValYr" style="width:90px;height:15px;background:white"></Td>
                    </Tr>
                    <%}
                    } else if (minTimeLevel.equals("4")) {%>
                    <Tr>
                        <Td>Week</Td>
                        <Td><Input type="text" class="myTextbox3" name="tgtValWeek" id="tgtValWeek" style="width:90px;height:15px;background:white"></Td>
                    </Tr>
                    <Tr>
                        <Td>Month</Td>
                        <Td><Input type="text" class="myTextbox3" name="tgtValMonth" id="tgtValMonth" style="width:90px;height:15px;background:white"></Td>
                    </Tr>
                    <Tr>
                        <Td>Quarter</Td>
                        <Td><Input type="text" class="myTextbox3" name="tgtValQtr" id="tgtValQtr" style="width:90px;height:15px;background:white"></Td>
                    </Tr>
                    <Tr>
                        <Td>Year</Td>
                        <Td><Input type="text" class="myTextbox3" name="tgtValYr" id="tgtValYr" style="width:90px;height:15px;background:white"></Td>
                    </Tr>
                    <%} else if (minTimeLevel.equals("3")) {%>
                    <Tr>
                        <Td>Month</Td>
                        <Td><Input type="text" class="myTextbox3" name="tgtValMonth" id="tgtValMonth" style="width:90px;height:15px;background:white"></Td>
                    </Tr>
                    <Tr>
                        <Td>Quarter</Td>
                        <Td><Input type="text" class="myTextbox3" name="tgtValQtr" id="tgtValQtr" style="width:90px;height:15px;background:white"></Td>
                    </Tr>
                    <Tr>
                        <Td>Year</Td>
                        <Td><Input type="text" class="myTextbox3" name="tgtValYr" id="tgtValYr" style="width:90px;height:15px;background:white"></Td>
                    </Tr>
                    <%} else if (minTimeLevel.equals("2")) {%>
                    <Tr>
                        <Td>Quarter</Td>
                        <Td><Input type="text" class="myTextbox3" name="tgtValQtr" id="tgtValQtr" style="width:90px;height:15px;background:white"></Td>
                    </Tr>
                    <Tr>
                        <Td>Year</Td>
                        <Td><Input type="text" class="myTextbox3" name="tgtValYr" id="tgtValYr" style="width:90px;height:15px;background:white"></Td>
                    </Tr>
                    <%} else if (minTimeLevel.equals("1")) {%>
                    <Tr>
                        <Td>Year</Td>
                        <Td><Input type="text" class="myTextbox3" name="tgtValYr" id="tgtValYr" style="width:90px;height:15px;background:white"></Td>
                    </Tr>
                    <%}
                    }%>
                  <%--  <Tr>

                        <Td>
                            <Select name="kpitgtPeriod" class="myTextbox3" id="kpitgtPeriod" >
                                <Option value="Day">Day</Option>
                                <Option value="Week">Week</Option>
                                <Option value="Month" SELECTED>Month</Option>
                                <Option value="Quarter">Quarter</Option>
                                <Option value="Year">Year</Option>
                            </Select>
                        </Td>
                    </Tr> --%>
                </Table>
                <br>
                <Table ALIGN="CENTER">
                    <Tr>
                        <Td><input type="button" class="navtitle-hover" style="width:auto" value="Next" onclick="dispKpiGraphTarget()"></Td>
                        <Td><input type="button" class="navtitle-hover" style="width:auto" value="Cancel" onclick="history.go(-1)"></Td>
                    </Tr>
                </Table>
                <input type="hidden" name="kpiId" id="kpiId" value="<%=kpiId%>">
                    <input type="hidden" name="kpiName" id="kpiName" value="<%=kpiName%>">
                <input type="hidden" name="perDay" id="perDay" value="<%=perday%>">
                <input type="hidden" name="timeDim" id="timeDim" value="<%=timeDim%>">
                <input type="hidden" name="dayDiff" id="dayDiff" value="<%=diffDays%>">
                <input type="hidden" name="targetType" id="targetType" value="<%=targetType%>">
                </form>
    </body>
</html>
