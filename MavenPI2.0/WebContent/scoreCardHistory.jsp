<%-- 
    Document   : scoreCardHistory
    Created on : Dec 22, 2010, 12:06:10 PM
    Author     : progen
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%
    String timeLevel = request.getParameter("timeLevel");
    String scardId = request.getParameter("scorecardId");
    String scardMemId = request.getParameter("scorecardMemberId");
    String themeColor = "blue";
    String contextPath=request.getContextPath();
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <script src="<%=contextPath%>/javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=themeColor %>/jquery-ui-1.7.3.custom.css" rel="stylesheet" />
        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/style.css" type="text/css" media="print, projection, screen">
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=themeColor%>/TableCss.css" rel="stylesheet" />
        <link href="<%=contextPath%>/stylesheets/themes/<%=themeColor%>/css.css" rel="stylesheet" type="text/css">
        
        
    </head>
    <body>

        <script type="text/javascript">
            loadHistory();
        </script>
        <table width="100%" id="displayStyleTable" style="display: none">
            <tr align="right">
                <td id="dispTable" align="right">
                    <a onclick="changeDispStyle('table');" style="cursor:pointer">Display as Table</a>
                </td>
                <td id="dispGraph" style="display:none" align="right">
                    <a onclick="changeDispStyle('graph');" style="cursor:pointer">Display as Graph</a>
                </td>
            </tr>
        </table>
        <div id="scardHistory">
            <center><img id='imgId' src='images/ajax.gif' align='middle'  width='75px' height='75px'  style='position:absolute' /></center>
        </div>
        <script type="text/javascript">
            function loadHistory(){
                var timeLevel = '<%=timeLevel%>';
                var scardId = '<%=scardId%>';
                var scardMemId = '<%=scardMemId%>';

                $.ajax({
                    url: 'scoreCardViewer.do?reportBy=loadScorecardHistory&timeLevel='+timeLevel+'&scorecardId='+scardId+'&scorecardMemberId='+scardMemId,
                    success: function(data){
                        $("#displayStyleTable").show();
                        $("#scardHistory").html(data);
                    }
                });
            }

            function changeDispStyle(type){
                if (type == 'graph'){
                    $("#dispTable").show();
                    $("#dispGraph").hide();

                    $("#historyChart").show();
                    $("#historyTable").hide();
                }
                else if (type == 'table'){
                    $("#dispTable").hide();
                    $("#dispGraph").show();

                    $("#historyChart").hide();
                    $("#historyTable").show();
                }
            }
        </script>
    </body>
</html>
