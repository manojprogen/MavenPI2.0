<%@page contentType="text/html" pageEncoding="UTF-8" import="prg.db.*"%>

<%
    String themeColor="blue";
    if(session.getAttribute("theme")==null)
        session.setAttribute("theme",themeColor);
    else
        themeColor=String.valueOf(session.getAttribute("theme"));
String contextPath=request.getContextPath();

%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=themeColor %>/ReportCss.css" rel="stylesheet"/>
         <link type="text/css" href="<%=contextPath%>/stylesheets/themes/blue/jquery-ui-1.7.3.custom.css" rel="stylesheet" />
         <link href="<%=contextPath%>/stylesheets/jquery.contextMenu.css" rel="stylesheet" type="text/css" />
        <script type="text/javascript" src="<%=contextPath%>/javascript/lib/jquery/js/jquery-1.4.2.min.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>
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
        <form name="myForm" method="post" action="">

            <%
        ////////////////////////////////////////////////////////////////////////////.println.println("In favLinks.jsp--->" + session.getAttribute("favlinks"));
        if (session.getAttribute("favlinks") != null) {
            PbReturnObject links = (PbReturnObject) session.getAttribute("favlinks");
            if(links.getRowCount()<=10){
          %>
                     <div style="height:auto;">
                         <script>
                             parent.document.getElementById("favFrame").style.height='100%';

                          </script>
            <%}else if(links.getRowCount()<=15){  %>
              <script>
                             parent.document.getElementById("favFrame").style.height='100%';

                          </script>
             <div style="height:auto;">
            <%}else if(links.getRowCount()<=20){  %>
             <script>

                parent.document.getElementById("favFrame").style.height='100%';
                          </script>
             <div style="height:100%;">
          <%}%>
          <table width="100%" align="left">
            <%for (int i = 0; i < links.getRowCount(); i++) {
                String message1="";
                if(links.getFieldValueString(i, 3)!=null && links.getFieldValueString(i, 3).equalsIgnoreCase("R")){
                     message1 = (links.getFieldValueString(i, 0).length() <= 25) ? links.getFieldValueString(i, 0) : links.getFieldValueString(i, 0).substring(0, 25) + "..";
                    %>
                    <tr>
                    <td align="left"><a href="javascript:void(0)" title="<%=links.getFieldValueString(i, 0)%>" onclick='javascript:viewReport("reportViewer.do?reportBy=viewReport&REPORTID=<%=links.getFieldValueInt(i, 1)%>&action=open")' style="text-decoration:none"><span style="font-size:12px;font-family: Calibri, Calibri, Calibri, sans-serif;"><%=message1%></span></a></td>
                    <td align="right"><a href="javascript:parent.submiturls1('<%=request.getContextPath()%>/reportViewer.do?reportBy=viewReport&REPORTID=<%=links.getFieldValueInt(i, 1)%>&reportDrill=Y')" class="ui-icon ui-icon-gear" title="Report Drill" ></a></td>
                    </tr>
            <%}else{
                     message1 = (links.getFieldValueString(i, 0).length() <= 25) ? links.getFieldValueString(i, 0) : links.getFieldValueString(i, 0).substring(0, 25) + "..";
                    %>
                    <tr>
              <td align="left"><a href="javascript:void(0)"  title="<%=links.getFieldValueString(i, 0)%>" onclick='javascript:viewReport("dashboardViewer.do?reportBy=viewDashboard&REPORTID=<%=links.getFieldValueInt(i, 1)%>&pagename=<%=links.getFieldValueString(i, 0)%>&reportDrill=Y")' style="text-decoration:none"><span style="font-size:12px;font-family: Calibri, Calibri, Calibri, sans-serif;"><%=message1%></span></a></td>
              <td align="right"><a href="javascript:parent.submiturls1('<%=request.getContextPath()%>/dashboardViewer.do?reportBy=viewDashboard&REPORTID=<%=links.getFieldValueInt(i, 1)%>')" class="ui-icon ui-icon-gear" title="Report Drill" ></a></td>
              <%
            }
            }
        }

            %>
            </table>
            </div>
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
