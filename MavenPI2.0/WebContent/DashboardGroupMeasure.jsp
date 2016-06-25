<%-- 
    Document   : DashboardGroupMeasure   (It is Used To Display All The GroupMeasures in The Drag And Drop and can
                                          Save the selected measures in the related dashlets.)
    Created on : 6 Jun, 2012, 7:26:32 PM
    Author     : veenadhari.g@progenbusiness.com
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" import="com.progen.report.KPIElement,com.progen.report.entities.KPI,com.progen.report.DashletDetail,java.util.*,java.text.*,java.math.*,prg.db.Container"%>
<!DOCTYPE html>
<%@page import="prg.db.PbDb,utils.db.ProgenConnection,prg.db.PbReturnObject,com.progen.report.pbDashboardCollection,com.progen.report.kpi.KPISingleGroupHelper"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%//for clearing cache
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
            String themeColor = "blue";
            if (session.getAttribute("theme") == null) {
                session.setAttribute("theme", themeColor);
            } else {
                themeColor = String.valueOf(session.getAttribute("theme"));
            }
            String contextPath=request.getContextPath();
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title><bean:message key="ProGen.Title"/></title>
        <script src="<%=contextPath%>/javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/blue/jquery-ui-1.7.3.custom.css" rel="stylesheet" />
        <script src="<%=contextPath%>/javascript/treeview/jquery.cookie.js" type="text/javascript"></script>
        <script src="<%=contextPath%>/javascript/treeview/jquery.treeview.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/treeview/demo.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/quicksearch.js"></script>
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=themeColor%>/metadataButton.css" rel="stylesheet" />
        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/treeviewstyle/jquery.treeview.css" />
        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/treeviewstyle/screen.css" />
        <link rel="stylesheet" href="stylesheets/treeviewstyle/screen.css" />
        <script type="text/javascript" src="<%= contextPath%>//dragAndDropTable.js"></script>
        <style type="text/css">

            * {
                font-family: verdana;
                font-size: 11px;
                font-size-adjust: none;
                font-stretch: normal;
                font-style: normal;
                font-variant: normal;
                font-weight: normal;
                line-height: normal;
            }
        </style>
        <%
        String divId = "";
        String dbrdId = "";
        String KpiData = "";
        HashMap map = new HashMap();
        Container container = null;
        long diffDays = 0;
        
        if(request.getAttribute("groupmeasures")!=null){
            KpiData = String.valueOf(request.getAttribute("groupmeasures"));
        }
        if (request.getAttribute("dbrdId") != null) {
            dbrdId = String.valueOf(request.getAttribute("dbrdId"));
        }
        if (request.getAttribute("divId") != null) {
            divId = String.valueOf(request.getAttribute("divId"));
        }
        
        if (request.getSession(false).getAttribute("PROGENTABLES") != null) {
            map = (HashMap) request.getSession(false).getAttribute("PROGENTABLES");
        }
        
        if (map.get(dbrdId) != null) {
                        container = (prg.db.Container) map.get(dbrdId);
        } else {
                        container = new prg.db.Container();
               }
        
        HashMap ParametersMap = container.getParametersHashMap();
        
        %>
        <script>
            
        </script>
    </head>
    <body>
        
    </body>
</html>
