<%-- 
    Document   : exceptionalHolidays
    Created on : Dec 30, 2009, 7:36:18 PM
    Author     : Administrator
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>ExceptionalHoliday Page</title>
        <script src="<%=request.getContextPath()%>/javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/jQuery/jquery/ui/ui.datepicker.js"></script>
        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/demos.css" rel="stylesheet" />
        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/ui.all.css" rel="stylesheet" />
            <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/metadataButton.css" rel="stylesheet" />

        <script type="text/javascript">
            $(function() {
                $("#date").datepicker({
                    changeMonth: true,
                    changeYear: true,
                    showButtonPanel: true,
                    numberOfMonths: 1,
                    stepMonths: 1
                });
            });
                function cancelexceptionHoliday()
                {
                    parent.document.getElementById('exceptionalholdaysdiv').style.display='none';
                    parent.document.getElementById("exceptionalholdaystab").style.display='none';
                    parent.document.getElementById('fade').style.display='none';
                }
                function saveexceptionHoliday()
                {
                    document.exceholdayform.action="<%=request.getContextPath()%>/calenderSetUpAction.do?timeCalenders=exceptionalHolidays";
            <%--alert(document.wekholdayform.action);--%>
                        document.exceholdayform.submit();
                        cancelexceptionHoliday();
                    }
        </script>
    </head>
    <body>
        <br/><br/><br/>
        <%
            String tableid = request.getParameter("tableid");
            //////////////////////////////////////////////////////////////////.println.println("tableid is" + tableid);
        %>
        <center>
            <form action="" method="post" name="exceholdayform">
                <table>
                    <tr>
                        <td>
                            <input type="text" name="date" id="date" readonly>
                        </td>
                        <td>
                            <select name="status" id="status">
                                <option value="0">-select-</option>
                                <option value="W">WorkingDay</option>
                                <option value="L">Holiday</option>
                            </select>
                        </td>
                        <td>
                            <input type="text" name="reason" id="reason">
                        </td>

                    </tr>
                    <tr>
                        <td></td>
                    </tr>
                    <tr>
                        <td align="right">
                            <input type="submit" value="Save" onclick="saveexceptionHoliday()" class="navtitle-hover">
                        </td>
                        <td align="left">
                            <input type="button" value="Cancel" onclick="cancelexceptionHoliday()" class="navtitle-hover">
                        </td>
                        <td></td>
                    </tr>
                </table>
                <input type="hidden" name="tableid" id="tableid" value="<%=tableid%>">
            </form>
        </center>
    </body>
</html>
