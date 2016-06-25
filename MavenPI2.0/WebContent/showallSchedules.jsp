<%-- 
    Document   : showallSchedules
    Created on : Apr 5, 2012, 5:58:44 PM
    Author     : progen
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page  import="com.progen.reportview.bd.PbReportViewerBD,utils.db.ProgenConnection,java.io.*,java.util.*,prg.db.PbDb,prg.db.PbReturnObject,java.awt.*" %>




<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%//for clearing cache
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
            String themeColor="blue";
             if(session.getAttribute("theme")==null)
                session.setAttribute("theme",themeColor);
            else
                themeColor=String.valueOf(session.getAttribute("theme"));
            String loguserId = String.valueOf(session.getAttribute("USERID"));
            PbReportViewerBD viewer=new PbReportViewerBD();
             HashMap map=new HashMap();
             map=viewer.viewScheduleReportDetails(loguserId);
             LinkedList scheduleNames=null,reportName=null,url=null;
             if(map!=null){
             scheduleNames=(LinkedList)map.get("schedulerNames");
             reportName=(LinkedList)map.get("reportName");
             url=(LinkedList)map.get("url");
             }
             String contextpath=request.getContextPath();
           
%>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <script type="text/javascript" src="<%=contextpath%>/javascript/lib/jquery/js/jquery-1.4.2.min.js"></script>
        <script type="text/javascript" src="<%=contextpath%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>
         <script type="text/javascript" src="<%=contextpath%>/javascript/pbReportViewerJS.js"></script>
        <link type="text/css" href="<%=contextpath%>/stylesheets/themes/<%=themeColor %>/metadataButton.css" rel="stylesheet" />
        <link type="text/css" href="<%=contextpath%>/stylesheets/themes/<%=themeColor %>/ReportCss.css" rel="stylesheet"/>
        <script type="text/javascript"></script>
        <style type="text/css">
                     .ui-state-default {
                         background:#E6E6E6 none repeat-x scroll 50% 50%;
                         border:1px solid #E6E6E6;
                         color:#000000;
                         font-weight:normal;
                         height:20px;
                         outline-style:none;
                         outline-width:medium;
                     }

                     .ui-corner-all {
                         -moz-border-radius-bottomleft:6px;
                         -moz-border-radius-bottomright:6px;
                         -moz-border-radius-topleft:6px;
                         -moz-border-radius-topright:6px;
                     }
          .white_content1 {
                position: absolute;
                top: 50px;
                left: 25%;
                width: 700px;
                height:400px;
                padding: 16px;
                border: 16px solid silver;
                background-color: white;
                z-index:1002;
                -moz-border-radius-bottomleft:5px;
                -moz-border-radius-bottomright:5px;
                -moz-border-radius-topleft:5px;
                -moz-border-radius-topright:5px;
            }
            a {text-decoration:none;cursor:pointer;color:#369}
            a:hover{text-decoration:underline}
            *{font:11px verdana}
        </style>
    </head>
    <body>       
<table border="1px solid #bdbdbd"  width="80%" align="center">
            <tr>
                <td>
                    <div style="width:100%;height:220px;overflow-y:auto">
        <table id="messages" cellpadding="2"   cellspacing="0"  align="center" class="ui-corner-all" width="100%" border="0">
                        <tr>
                            <th class="bgcolor" align="left">ScheduleName</th>
                            <th class="bgcolor" align="left">ReportName</th>
                        </tr>
                        <% for(int i=0;i<scheduleNames.size();i++){%>
                            <tr>
                                  <td style="border-right:black" align="left"><%=scheduleNames.get(i)%></td>
                                  <td style="border-right:black" align="left"><a href="<%=url.get(i)%>" target="_blank" style="font-weight:bold"><%=reportName.get(i)%></a></td>
                              
                            </tr>
              <% } %>

        </table>
        </div>
        </td>
        </tr>
        </table>
        </body>
</html>
