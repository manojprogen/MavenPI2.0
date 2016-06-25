<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="prg.db.*" %>
<%
    String themeColor="blue";
    if(session.getAttribute("theme")==null)
        session.setAttribute("theme",themeColor);
    else
        themeColor=String.valueOf(session.getAttribute("theme"));


%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/themes/<%=themeColor %>/ReportCss.css" rel="stylesheet"/>
        <title>JSP Page</title>

        
        <style>
            a {font-family:Verdana;font-size:10px;cursor:pointer;}
/*            a:link {color:#369}*/
            /* .body{
                 background-color:#e6e6e6;
             }*/
        </style>
    </head>
    <body class="body">
        <form name="myForm" action="" method="post">
            <table>
                <%
                PbDb pbdb = new PbDb();
                String loguserId = String.valueOf(session.getAttribute("USERID"));
                PbReturnObject pbro = pbdb.execSelectSQL("select REPORT_SCHEDULE_ID,SCHEDULER_NAME from prg_report_scheduler where from_report='Y' and CREATED_BY="+loguserId);
                int len = 0;
                if (pbro.getRowCount() > 10) {
                    len = 10;
                } else {
                    len = pbro.getRowCount();
                }

                String schedId = "";
                String schedName = "";
                String schedUrl = "";

                for (int i = 0; i < len; i++) {
                    schedId = pbro.getFieldValueString(i, 0);
                    schedName = pbro.getFieldValueString(i, 1);
                    schedUrl = "tracker/JSPS/scheduleReport.jsp?schedulerId="+schedId+"&isEdit="+"true";
                %>
                <tr>
                    <td>
                        <a  href="<%=schedUrl%>"  title="<%=schedName%>" target="_blank" style="text-decoration:none"><%=schedName%></a>
                    </td>
                </tr>
                    <%}%>
            </table>
        </form>
            <script>
            function getFavLinks(){
                document.myForm.action = "reportViewer.do?reportBy=favReports";
                document.myForm.submit();
            }
            function viewReport(path){
                //parent.document.forms[0].action=path;
                //parent.document.forms[0].submit();
                parent.submiturls1(path);
            }

        </script>
    </body>
</html>
