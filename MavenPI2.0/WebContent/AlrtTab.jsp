<%@page contentType="text/html" pageEncoding="windows-1252" import="java.sql.Connection,prg.db.PbReturnObject,prg.db.PbDb,utils.db.ProgenConnection "%>
<%--
    Document   : AlrtTab
    Created on : Nov 11, 2009, 5:35:41 PM
    Author     : Administrator
--%>

<%
        PbReturnObject alertReturn2 = null;
        PbReturnObject alerts1 = null;
        PbReturnObject alerts2 = null;
        PbReturnObject alerts3 = null;
        String userId = "";
        PbDb pbdb = new PbDb();
        String alertQ = "";
        Connection con = null;
        try {
            con = ProgenConnection.getInstance().getCustomerConn();
            userId = String.valueOf(request.getSession(false).getAttribute("USERID"));
            alertQ = "select alert_id,alert_name, risk_status from alert_master where user_id=" + userId;
            alertReturn2 = pbdb.execSelectSQL(alertQ, con);

%>

<html>
    <head>

    </head>
    <body style="cursor: auto;">
        <script type="text/javascript">
            $(document).ready(function(){
                $("#hr").treeview({
                    animated: "normal",
                    unique:true
                });

                $("#mr").treeview({
                    animated: "normal",
                    unique:true
                });

                $("#lr").treeview({
                    animated: "normal",
                    unique:true
                });
            });
            function openMessagewindow(value)
            {
                window.open('pbGetAlertMessage.jsp?alertId='+value,'welcome','width=750,height=300',menubar=1)
            }
            function openMessagewindowchild(value,name)
            {
                window.open('pbGetAlertMessageChild.jsp?alertId='+value+"&paramVal="+name,'welcome','width=750,height=300',menubar=1)
            }
        </script>
        <center>
            <div id="AlrtTab"  style="width:99%;min-height:125px;max-height:100%;height:auto">
                <table>
                    <tr><td>
                            <div style="height:400px;overflow:auto" id="highRiskAlerts">
                                <table border="0" width="400px">
                                    <tr>
                                        <td class="prgtableheader2" style="background-color:red" align="center">
                                            High Risk Alerts
                                        </td>
                                    </tr>
                                    <tr><td>
                                            <ul id="hr" class="filetree">
                                                <li class="closed"><span style="color:red;font-weight:bold;font-size:12px">High Risk Alerts</span>
                                                    <ul  class="filetree">
                                                        <%  for (int m = 0; m < alertReturn2.getRowCount(); m++) {
        String aName = alertReturn2.getFieldValueString(m, "ALERT_NAME");
        String alertRisk = alertReturn2.getFieldValueString(m, "RISK_STATUS");

        if (alertRisk.equalsIgnoreCase("HIGH")) {
            con = ProgenConnection.getInstance().getCustomerConn();
            String localAelrtId = String.valueOf(alertReturn2.getFieldValueInt(m, "ALERT_ID"));
            String alertDet = "select * from alert_details where alert_id=" + localAelrtId;
            alerts1 = pbdb.execSelectSQL(alertDet, con);
                                                        %>
                                                        <li class="closed"><a  style="font-size:12px;color:red" onclick="openMessagewindow('<%=alertReturn2.getFieldValueInt(m, "ALERT_ID")%>')"><%=aName%></a>

                                                            <ul>
                                                                <%for (int i = 0; i < alerts1.getRowCount(); i++) {
                                                                if (alerts1.getFieldValueString(i, "RISK_STATUS").equals("HIGH")) {
                                                                %>
                                                                <li class="closed"><a style="font-size:10px;color:red" onclick="openMessagewindowchild('<%=alerts1.getFieldValueString(i, "ALERT_ID")%>','<%=alerts1.getFieldValueString(i, "PARAMETER_VALUE")%>')"><%=alertReturn2.getFieldValueString(m, "ALERT_NAME")%><%=" '"%><%=alerts1.getFieldValueString(i, "PARAMETER_VALUE")%><%="'"%></a>

                                                                <%
                                                                } else if (alerts1.getFieldValueString(i, "RISK_STATUS").equals("MEDIUM")) {
                                                                %>
                                                                <li class="closed"><a style="font-size:10px;color:orange" onclick="openMessagewindowchild('<%=alerts1.getFieldValueString(i, "ALERT_ID")%>','<%=alerts1.getFieldValueString(i, "PARAMETER_VALUE")%>')"><%=alertReturn2.getFieldValueString(m, "ALERT_NAME")%><%=" '"%><%=alerts1.getFieldValueString(i, "PARAMETER_VALUE")%><%="'"%></a>

                                                                <%
                                                                } else if (alerts1.getFieldValueString(i, "RISK_STATUS").equals("LOW")) {
                                                                %>
                                                                <li class="closed"><a style="font-size:10px;color:green" onclick="openMessagewindowchild('<%=alerts1.getFieldValueString(i, "ALERT_ID")%>','<%=alerts1.getFieldValueString(i, "PARAMETER_VALUE")%>')"><%=alertReturn2.getFieldValueString(m, "ALERT_NAME")%><%=" '"%><%=alerts1.getFieldValueString(i, "PARAMETER_VALUE")%><%="'"%></a>

                                                                <%
                                                                }
                                                                %>
                                                                <%}%>

                                                            </ul>
                                                        </li>
                                                        <%
      }%>

                                                        <%}%>
                                                    </ul>
                                                </li>
                                            </ul>
                                        </td>
                                    </tr>
                                </table>
                            </div>
                        </td>
                        <td>
                            <div style="height:400px;overflow:auto" id="MediumRiskAlerts">
                                <table border="0" width="400px">
                                    <tr>
                                        <td class="prgtableheader2" style="background-color:orange" align="center">
                                            Medium Risk Alerts
                                        </td>
                                    </tr>
                                    <tr><td>

                                            <ul id="mr" class="filetree">
                                                <li class="closed"><span style="color:orange;font-weight:bold;font-size:12px">Medium Risk Alerts</span>
                                                    <ul  class="filetree">

                                                        <%  for (int m = 0; m < alertReturn2.getRowCount(); m++) {
        String aName = alertReturn2.getFieldValueString(m, "ALERT_NAME");
        String alertRisk = alertReturn2.getFieldValueString(m, "RISK_STATUS");

        if (alertRisk.equalsIgnoreCase("MEDIUM")) {
            con = ProgenConnection.getInstance().getCustomerConn();
            String localAelrtId = String.valueOf(alertReturn2.getFieldValueInt(m, "ALERT_ID"));
            String alertDet = "select * from alert_details where alert_id=" + localAelrtId;
            alerts2 = pbdb.execSelectSQL(alertDet, con);
                                                        %>
                                                        <li class="closed"><a  style="font-size:12px;color:orange" onclick="openMessagewindow('<%=alertReturn2.getFieldValueString(m, "ALERT_ID")%>')"><%=aName%></a>
                                                            <ul>
                                                                <%for (int i = 0; i < alerts2.getRowCount(); i++) {
                                                                if (alerts2.getFieldValueString(i, "RISK_STATUS").equals("HIGH")) {
                                                                %>
                                                                <li class="closed"><a style="font-size:10px;color:red" onclick="openMessagewindowchild('<%=alerts2.getFieldValueString(i, "ALERT_ID")%>','<%=alerts2.getFieldValueString(i, "PARAMETER_VALUE")%>')"><%=alertReturn2.getFieldValueString(m, "ALERT_NAME")%><%=" '"%><%=alerts2.getFieldValueString(i, "PARAMETER_VALUE")%><%="'"%></a>

                                                                <%
                                                                } else if (alerts2.getFieldValueString(i, "RISK_STATUS").equals("MEDIUM")) {
                                                                %>
                                                                <li class="closed"><a style="font-size:10px;color:orange" onclick="openMessagewindowchild('<%=alerts2.getFieldValueString(i, "ALERT_ID")%>','<%=alerts2.getFieldValueString(i, "PARAMETER_VALUE")%>')"><%=alertReturn2.getFieldValueString(m, "ALERT_NAME")%><%=" '"%><%=alerts2.getFieldValueString(i, "PARAMETER_VALUE")%><%="'"%></a>

                                                                <%
                                                                } else if (alerts2.getFieldValueString(i, "RISK_STATUS").equals("LOW")) {
                                                                %>
                                                                <li class="closed"><a style="font-size:10px;color:green" onclick="openMessagewindowchild('<%=alerts2.getFieldValueString(i, "ALERT_ID")%>','<%=alerts2.getFieldValueString(i, "PARAMETER_VALUE")%>')"><%=alertReturn2.getFieldValueString(m, "ALERT_NAME")%><%=" '"%><%=alerts2.getFieldValueString(i, "PARAMETER_VALUE")%><%="'"%></a>

                                                                <%
                                                                }
                                                                %>
                                                                <%}%>

                                                            </ul>
                                                        </li>
                                                        <%
        }

    }%>
                                                    </ul>
                                                </li>
                                            </ul>
                                        </td>
                                    </tr>
                                </table>
                            </div>
                        </td>
                        <td>
                            <div style="height:400px;overflow:auto" id="LowRiskAlerts">
                                <table border="0" width="400px">
                                    <tr>
                                        <td class="prgtableheader2" style="background-color:green" align="center">
                                            Low Risk Alerts
                                        </td>
                                    </tr>
                                    <tr><td>

                                            <ul id="lr" class="filetree">
                                                <li class="closed"><span style="color:green;font-weight:bold;font-size:12px">Low Risk Alerts</span>
                                                    <ul class="filetree">

                                                        <%  for (int m = 0; m < alertReturn2.getRowCount(); m++) {
        String aName = alertReturn2.getFieldValueString(m, "ALERT_NAME");
        String alertRisk = alertReturn2.getFieldValueString(m, "RISK_STATUS");
        if (alertRisk.equalsIgnoreCase("LOW")) {
            con = ProgenConnection.getInstance().getCustomerConn();
            String localAelrtId = String.valueOf(alertReturn2.getFieldValueInt(m, "ALERT_ID"));
            String alertDet = "select * from alert_details where alert_id=" + localAelrtId;
            alerts3 = pbdb.execSelectSQL(alertDet, con);
                                                        %>
                                                        <li class="closed"><a  style="font-size:12px;color:orange" onclick="openMessagewindow('<%=alertReturn2.getFieldValueString(m, "ALERT_ID")%>')"><%=aName%></a>
                                                            <ul>
                                                                <%for (int i = 0; i < alerts3.getRowCount(); i++) {
                                                                ////////////////////////////////////////////////////////////////////////////////////////.println.println(alerts.getFieldValueString(i, "RISK_STATUS") + " --///-- " + alerts.getFieldValueString(i, "PARAMETER_VALUE"));
                                                                if (alerts3.getFieldValueString(i, "RISK_STATUS").equals("HIGH")) {
                                                                %>
                                                                <li class="closed"><a style="font-size:10px;color:red" onclick="openMessagewindowchild('<%=alerts3.getFieldValueString(i, "ALERT_ID")%>','<%=alerts3.getFieldValueString(i, "PARAMETER_VALUE")%>')"><%=alertReturn2.getFieldValueString(m, "ALERT_NAME")%><%=" '"%><%=alerts3.getFieldValueString(i, "PARAMETER_VALUE")%><%="'"%></a>

                                                                <%
                                                                } else if (alerts3.getFieldValueString(i, "RISK_STATUS").equals("MEDIUM")) {
                                                                %>
                                                                <li class="closed"><a style="font-size:10px;color:orange" onclick="openMessagewindowchild('<%=alerts3.getFieldValueString(i, "ALERT_ID")%>','<%=alerts3.getFieldValueString(i, "PARAMETER_VALUE")%>')"><%=alertReturn2.getFieldValueString(m, "ALERT_NAME")%><%=" '"%><%=alerts3.getFieldValueString(i, "PARAMETER_VALUE")%><%="'"%></a>

                                                                <%
                                                                } else if (alerts3.getFieldValueString(i, "RISK_STATUS").equals("LOW")) {
                                                                %>
                                                                <li class="closed"><a style="font-size:10px;color:green" onclick="openMessagewindowchild('<%=alerts3.getFieldValueString(i, "ALERT_ID")%>','<%=alerts3.getFieldValueString(i, "PARAMETER_VALUE")%>')"><%=alertReturn2.getFieldValueString(m, "ALERT_NAME")%><%=" '"%><%=alerts3.getFieldValueString(i, "PARAMETER_VALUE")%><%="'"%></a>

                                                                <%
                                                                }
                                                                %>
                                                                <%}%>

                                                            </ul>
                                                        </li>
                                                        <%}
    }%>
                                                    </ul>
                                                </li>
                                            </ul>
                                        </td>
                                    </tr>
                                </table>
                            </div>
                        </td>
                    </tr>
                </table>
            </div>
        </center>
    </body>
</html>
<%
        } catch (Exception e) {
            if (con != null) {
                con.close();
            }
            e.printStackTrace();
        }
%>
