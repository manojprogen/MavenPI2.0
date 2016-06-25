
<%@page contentType="text/html" pageEncoding="windows-1252" import="prg.db.PbReturnObject,java.sql.*,utils.db.ProgenConnection"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<% String contextPath=request.getContextPath();%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
        <link href="../css/myStyles.css" rel="stylesheet" type="text/css">
        <link type="text/css" href="<%=contextPath%>/stylesheets/metadataButton.css" rel="stylesheet" />
        <style type="text/css">
           *{
               font:11px verdana;
           }
        </style>
    </head>
    <body>
<%
ResultSet rs = null;

String alertId = request.getParameter("alertId");
String paramVal = request.getParameter("paramVal");
//Class.forName("oracle.jdbc.driver.OracleDriver");
//////////////////////////////////////////////////////////////////////////////////////////.println.println(alertId+" paramVal "+paramVal);
Connection con = ProgenConnection.getCustomerConn();
//Connection con = ProgenConnection.getConnection();

Statement st = con.createStatement();
String Query = "select am.alert_name as view_by, ad.parameter_id,ad.actual_value,am.measure, ad.target_value, ad.deviation_per, ad.risk_status from alert_details ad,alert_master am where am.alert_id = ad.alert_id and ad.alert_id="+alertId+" and parameter_value='"+paramVal+"'";//"select * from alert_details where alert_id="+alertId+" and parameter_value='"+paramVal+"'";
rs = st.executeQuery(Query);
PbReturnObject pbro = new PbReturnObject(rs);

String alertName = pbro.getFieldValueString(0,"VIEW_BY");
String parameterName = pbro.getFieldValueString(0,"PARAMETER_ID");
String measureName = pbro.getFieldValueString(0,"MEASURE");
String targetValue = pbro.getFieldValueString(0,"TARGET_VALUE");
String actualValue = pbro.getFieldValueString(0,"ACTUAL_VALUE");
String deviationPer = pbro.getFieldValueString(0,"DEVIATION_PER");
String riskStatus = pbro.getFieldValueString(0,"RISK_STATUS");

//out.println(rs.getString(1));
rs.close();
st.close();
con.close();

%>


<br>
        <center>
            <br><br>
            <table>
                <tr>
                    <td><span style="font-weight:bold;color:black;font-size:14px">Alert Name</span></td>
                    <td>::</td>
                    <td><span style="font-size:14px;color:black;"><%=alertName%></span></td>
                </tr>
                <tr>
                    <td><span style="font-weight:bold;color:black;font-size:14px">Parameter Name</span></td>
                    <td>::</td>
                    <td><span style="font-size:14px;color:black"><%=parameterName%></span></td>
                </tr>
                <tr>
                    <td><span style="font-weight:bold;color:black;font-size:14px">Measure Name</span></td>
                    <td>::</td>
                    <td><span style="font-size:14px;color:black"><%=measureName%></span></td>
                </tr>
            </table>
            <br><br>
            <table>
                
                    <th style="background-color:#b4d9ee"><span style="font-weight:bold;color:black">Parameter Value</span></th>
                    <th style="background-color:#b4d9ee"><span style="font-weight:bold;color:black">Target Value</span></th>
                    <th style="background-color:#b4d9ee"><span style="font-weight:bold;color:black">Actual Value</span></th>
                    <th style="background-color:#b4d9ee"><span style="font-weight:bold;color:black">Deviation (%)</span></th>
                
                <tr>
<%
                    if(riskStatus.equalsIgnoreCase("HIGH"))
                    {
%>
                    <td align="center"><span style="color:red;font-weight:bold;font-size:11px"><%=paramVal%></span></td>
                    <td align="center"><span style="color:red;font-weight:bold;font-size:11px"><%=targetValue%></span></td>
                    <td align="center"><span style="color:red;font-weight:bold;font-size:11px"><%=actualValue%></span></td>
                    <td align="center"><span style="color:red;font-weight:bold;font-size:11px"><%=deviationPer%></span></td>
<%
                    }
                    else if(riskStatus.equalsIgnoreCase("MEDIUM"))
                    {
%>
                    <td align="center"><span style="color:orange;font-weight:bold;font-size:11px"><%=paramVal%></span></td>
                    <td align="center"><span style="color:orange;font-weight:bold;font-size:11px"><%=targetValue%></span></td>
                    <td align="center"><span style="color:orange;font-weight:bold;font-size:11px"><%=actualValue%></span></td>
                    <td align="center"><span style="color:orange;font-weight:bold;font-size:11px"><%=deviationPer%></span></td>
<%
                    }
                    else
                    {
%>
                    <td align="center"><span style="color:green;font-weight:bold;font-size:11px"><%=paramVal%></span></td>
                    <td align="center"><span style="color:green;font-weight:bold;font-size:11px"><%=targetValue%></span></td>
                    <td align="center"><span style="color:green;font-weight:bold;font-size:11px"><%=actualValue%></span></td>
                    <td align="center"><span style="color:green;font-weight:bold;font-size:11px"><%=deviationPer%></span></td>
<%
                    }
%>
                </tr>
            </table>
            <br>
            <input type="button" class="navtitle-hover" value="close" onclick="window.close()">
        </center>

    </body>
</html>
