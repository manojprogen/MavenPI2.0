<%-- 
    Document   : pbTargetTypeChange
    Created on : Feb 25, 2010, 5:47:40 PM
    Author     : Saurabh
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"  import="com.progen.reportdesigner.db.DashboardTemplateDAO"%>
<% String contextPath=request.getContextPath();%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>

        <script src="<%=contextPath%>/javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/dashboardDesign.js"></script>
        <link type="text/css" href="<%=contextPath%>/stylesheets/ui.all.css" rel="stylesheet" />
        <link type="text/css" href="<%=contextPath%>/stylesheets/demos.css" rel="stylesheet" />
        <link href="<%=contextPath%>/stylesheets/javascript.css" type="text/css" rel="stylesheet">
        <link href="<%=contextPath%>/stylesheets/html.css" type="text/css" rel="stylesheet">
        <link href="<%=contextPath%>/stylesheets/css.css" type="text/css" rel="stylesheet">
        <link type="text/css" href="<%=contextPath%>/stylesheets/metadataButton.css" rel="stylesheet" />

    </head>
    <body>
        <form name="TargetTypeForm" method="post">
            <%
            String elementId = request.getParameter("elementId");
            String masterKpiId = request.getParameter("masterKpiId");
            String timeLevel = request.getParameter("timeLevel");
            String targetValue = request.getParameter("targetValue");
            String reportId = request.getParameter("reportId");
            String timeDimVal = request.getParameter("timeDimVal");
            String currVal=request.getParameter("currVal");
            String kpiName=request.getParameter("kpiName");
            %>
            <table align="center">
                <Tr>
                    <Td><font style="font-family:verdana;font-size:14px;font-weight:bold;color:#369;">Select Target Type :</font></Td>
                </Tr>
                <Tr>
                    <Td><input type="radio" name="targetType" id="targetType" value="proRated" onclick="getTargetType('proRated','<%=elementId%>','<%=masterKpiId%>','<%=timeLevel%>','<%=targetValue%>','<%=reportId%>','<%=timeDimVal%>','<%=currVal%>','<%=kpiName%>')">Pro Rated Period</Td>
                    <Td><input type="radio" name="targetType" id="targetType" value="individual" onclick="getTargetType('individual','<%=elementId%>','<%=masterKpiId%>','<%=timeLevel%>','<%=targetValue%>','<%=reportId%>','<%=timeDimVal%>','<%=currVal%>','<%=kpiName%>')">Individual Period</Td>
                </Tr>
            </table>
        </form>
    </body>
</html>
