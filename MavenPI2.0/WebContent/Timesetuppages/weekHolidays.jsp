<%-- 
    Document   : weekHolidays
    Created on : Dec 29, 2009, 5:59:12 PM
    Author     : Administrator
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/metadataButton.css" rel="stylesheet" />
        <script type="text/javascript">
            function cancelwekholidays()
            {
            parent.$("#weekholdaysdiv").dialog('close');
            <%--parent.document.getElementById('weekholdaysdiv').style.display='none';
            parent.document.getElementById("weekholdaystab").style.display='none';
            parent.document.getElementById('fade').style.display='none';--%>
                }
                function saveweekholidays()
                {
                    document.wekholdayform.action="<%=request.getContextPath()%>/calenderSetUpAction.do?timeCalenders=weekHolidays";
                    document.wekholdayform.submit();
                    cancelwekholidays();
                }
        </script>
        <title>WeekHolidays Page</title>
    </head>
    <body>
        <form name="wekholdayform" action="" method="POST">
            <center>
                <%
                            String tableid = request.getParameter("tableid");
                            ////////////////////////////////////////////////////////////////////.println.println("tableid" + tableid);
                            String[] Nameofdays = {"SUNDAY", "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY"};
                            String[] Nameofdaysvalue = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};

                %>
                <select name="wkday" id="wkday">
                    <option value="select">-select-</option>
                    <%
                                for (int i = 0; i < Nameofdays.length; i++) {
                    %>
                    <option value=<%=Nameofdaysvalue[i]%>><%=Nameofdays[i]%></option>
                    <%
                                }
                    %>
                </select>
                <br/><br/>
                <input type="submit" value="SaveWeekHoiday" onclick="saveweekholidays()"  class="navtitle-hover">
                &nbsp;&nbsp;&nbsp;
                <input type="button" value="Cancel" onclick="cancelwekholidays()"  class="navtitle-hover">
                <%--
                Hidden values for getting into an action class
                --%>
                <input type="hidden" name="tableid" id="tableid" value=<%=tableid%>>
            </center>
        </form>
    </body>
</html>
