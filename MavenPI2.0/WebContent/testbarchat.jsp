<%-- 
    Document   : testbarchat
    Created on : Jan 18, 2010, 1:26:14 PM
    Author     : Saurabh
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.progen.report.pbDashboardCollection" %>
<%@page import="java.util.ArrayList;"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <%
            ArrayList progenChart = new ArrayList();
            pbDashboardCollection pbdasbord = new pbDashboardCollection();
            String[] rowViewArray = {"AndhraPradesh", "Tamilnadu", "Karnataka", "Kerala"};
            String MedSize = "Medium";
            int size = 0;

            if (MedSize.equalsIgnoreCase("Medium")) {
                size = 400;
                double[] colArray = {35144, 25400, 18242, 12248, 7846};
                progenChart = pbdasbord.progenChart(colArray, rowViewArray, size);
            } else {
                size = 800;
                double[] colArray = {35144, 25400, 18242, 12248, 7846, 5436, 3241, 2230, 1111, 786};
                progenChart = pbdasbord.progenChart(colArray, rowViewArray, size);
            }
        %>
        <style type="text/css">
            dl dt{float:left;width:100px;clear:both;margin:0 8px 0 0;font-weight:normal;text-align:right;}
            dl dd{float:none;margin:0 8px 1px 4px;}
            dl{font-family:verdana;font-size:10px;line-height:5px}
        </style>

    </head>
    <body>
        <table>            
            <%=progenChart.get(0)%>
        </table>
        <br/>            
        <%=progenChart.get(1)%>
    </body>

</html>
