<%-- 
    Document   : yearHolidays
    Created on : Dec 29, 2009, 7:42:02 PM
    Author     : Administrator
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script src="<%=request.getContextPath()%>/javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/jQuery/jquery/ui/ui.datepicker.js"></script>
          <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/demos.css" rel="stylesheet" />
          <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/ui.all.css" rel="stylesheet" />
           <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/metadataButton.css" rel="stylesheet" />
        <title>YearHoliday Page</title>
        <script type="text/javascript">
            $(function() {
            for(var i=0;i<10;i++)
            {
                    $("#holiday"+i).datepicker({
                        changeMonth: true,
                        changeYear: true,
                        showButtonPanel: true,
                        numberOfMonths: 1,
                        stepMonths: 1
                    });
            }
            });
              function cancelyearholidays()
            {
            <%--alert("cancel");--%>
                        parent.document.getElementById('yearholdaysdiv').style.display='none';
                        parent.document.getElementById("yearholdaystab").style.display='none';
                        parent.document.getElementById('fade').style.display='none';
                    }
                    function saveYearHolidays()
                    {
                        document.wekholdayform.action="<%=request.getContextPath()%>/calenderSetUpAction.do?timeCalenders=yearHolidays";
                        alert(document.wekholdayform.action);
                        document.wekholdayform.submit();
                        cancelyearholidays();
                    }
        </script>
    </head>
    <body>
        <form action="" name="wekholdayform" method="post">
        <center>
            <br/><br/>
            <strong><font size="+1">Select Year Holidays</font></strong>
            <br/>
            <table>
                <%
                String tableid = request.getParameter("tableid");
                //////////////////////////////////////////////////////////////////.println.println("tableid"+tableid);
                int i=0;
            for (i = 0; i <10; i++) {
                %>
                <tr>
                    <td>
                        <label>Holiday<%=i+1%>:</label>
                    </td>
                    <td>
                        <input type="text" name="holiday<%=i%>" id="holiday<%=i%>" readonly>
                    </td>
                    <td>
                        <input type="text" name="reason<%=i%>" id="reason1<%=i%>">
                    </td>
                </tr>
                <%}%>
               
                     <table align="center"><tr>
                    <td align="center"><input type="submit" value="Save" onclick="saveYearHolidays()" class="navtitle-hover"></td>
                    <td align="center"><input type="button" value="Cancel" onclick="cancelyearholidays()" class="navtitle-hover"></td>
                      </tr></table>
            </table>
                <input type="hidden" name="limit" id="limit" value="<%=i%>">
                <input type="hidden" name="tableid" id="tableid" value="<%=tableid%>">
        </center>
        </form>
    </body>
</html>
