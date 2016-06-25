<%-- 
    Document   : setAggregation
    Created on : May 22, 2010, 4:09:04 PM
    Author     : Administrator
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/metadataButton.css" rel="stylesheet" />
        <title>JSP Page</title>
        <script type="text/javascript">
            function saveAggregation()
            {
                alert("in saveAggregation");
            }
        </script>
    </head>
    <body>
        <%
        String reportid = request.getParameter("reportid");
        ////.println("reportid---"+reportid);
String selecteddata = request.getParameter("selecteddata");
////.println("selecteddata-----"+selecteddata);
%>
        <form name="myFormAggregation" method="post" action="">
            <table>
                <tr>
                    <td>
                       User Defined Aggregation:
                    </td>
                    <td>
                        <select name="customAggregation" id="customAggregation">
                            <option value="m1">Day
                            <option value="m2">Week
                            <option value="m3">Month
                            <option value="m4">Qtr
                            <option value="m5">Year

                        </select>
                    </td>
                </tr>
                <tr>
                    <td>
                        <input type ="button" name="save" value="save" class="navtitle-hover" onclick="saveAggregation()">
                    </td>
                </tr>
            </table>
        </form>
    </body>
</html>
