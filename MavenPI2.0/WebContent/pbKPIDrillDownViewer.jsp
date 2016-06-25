

<%@page import="utils.db.*,java.util.ArrayList,java.util.HashMap,java.util.Vector,com.progen.reportdesigner.action.DashboardTemplateAction,prg.db.PbReturnObject,com.progen.reportdesigner.db.DashboardTemplateDAO"%>



<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<% String contextPath=request.getContextPath();%>
<html>
    <head>

        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="css/myStyles.css" rel="stylesheet" type="text/css">
        <link rel="stylesheet" type="text/css" href="<%=contextPath%>/stylesheets/tablesorterStyle.css" />
        <script type="text/javascript" src="<%=contextPath%>/javascript/lib/jquery/js/jquery-1.4.2.min.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/jquery.tablesorter.js"></script>
        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/style.css" type="text/css" media="print, projection, screen">
        <link type="text/css" href="<%=contextPath%>/stylesheets/metadataButton.css" rel="stylesheet" />
        <script>

            function kpiDrillSave()
            {
                document.forms.myForm.action="pbSaveKpiDrill.jsp";
                document.forms.myForm.submit();

                closeKpiDrill();
            }
            function closeKpiDrill()
            {
                parent.$("#kpiDrillDialog").dialog('close');
            <%--var frameObj=parent.document.getElementById("kpiDrillDispmem");
            var divObj=parent.document.getElementById("kpiDrill");
            frameObj.style.display='none';
            divObj.style.display='none';
            parent.document.getElementById('fade').style.display='none';--%>
                }

        </script>
        <style type="text/css">
            *{font:11px verdana}
        </style>

    </head>
    <body>
        <%
            String kpiStr = "";
            String[] elements = null;
            String ReportId = "";
            HashMap kpiDrillRepMap = new HashMap();
            kpiStr = request.getParameter("kpiId");
            if (kpiStr != null) {
                elements = kpiStr.split(",");
            }
            ////////////////.println.println("kpiId values are  " + kpiStr);
            String foldersIds = request.getParameter("folderIds");
            ////.println("foldersIds values are " + foldersIds);
            DashboardTemplateDAO dashboardDAO = new DashboardTemplateDAO();
            DashboardTemplateAction dashbrdAction = new DashboardTemplateAction();
            PbReturnObject retObj = new PbReturnObject();
            PbReturnObject retObj1 = new PbReturnObject();
            PbReturnObject retObj2 = new PbReturnObject();
            PbReturnObject retObj3 = new PbReturnObject();
            ArrayList kpiIds = new ArrayList();
            if (session.getAttribute("kpiDrillRepMap") != null) {
                kpiDrillRepMap = (HashMap) session.getAttribute("kpiDrillRepMap");
            }
            ////.println("session.getAttribute(kpiDrillRepMap) is: " + kpiDrillRepMap);
            if (elements != null) {
        %>
        <center>
            <br>
            <form name="myForm" method="post">
                <table border="0" cellpadding="1" cellspacing="1" id="tablesorter" class="tablesorter" width="15%">
                    <thead><tr>
                            <th>KPI Name</th>
                            <th>Drill Down Structure</th>
                        </tr></thead><tfoot></tfoot><tbody>
                        <%
                for (int i = 0; i < elements.length; i++) {
                    retObj1 = dashboardDAO.getKpiDrillNames(elements[i]);
                    for (int j = 0; j < retObj1.getRowCount(); j++) {
                        kpiIds.add(elements[i]);
                        retObj2 = dashboardDAO.getKpiDrillReports(elements[i], foldersIds);

                        %>
                        <tr>
                            <td width="25%" id="column1" class="myTextbox5"><%=retObj1.getFieldValueString(j, "COL_DISP_NAME")%></td>
                            <td width="25%">
                                <select class="myTextbox5" name="repName<%=i%>" id="repName<%=i%>">
                                    <%
                        ////////////////////////////////////////////////////////////////////////////////////////.println.println("---retObj1.getFieldValueInt(j,DRILL_REPORT)------"+j+"---------"+retObj1.getFieldValueInt(j,"DRILL_REPORT"));
                        for (int k = 0; k < retObj2.getRowCount(); k++) {
                            if (kpiDrillRepMap.size() != 0) {
                                if (Integer.parseInt(kpiDrillRepMap.get(elements[i]).toString()) == retObj2.getFieldValueInt(k, "REPORT_ID")) {
                                    %>
                                    <option selected value="<%=retObj2.getFieldValueInt(k, "REPORT_ID")%>"><%=retObj2.getFieldValueString(k, "REPORT_NAME")%></option>
                                    <%} else {%>
                                    <option value="<%=retObj2.getFieldValueInt(k, "REPORT_ID")%>"><%=retObj2.getFieldValueString(k, "REPORT_NAME")%></option>
                                    <%}
                            } else {%>
                                    <option value="<%=retObj2.getFieldValueInt(k, "REPORT_ID")%>"><%=retObj2.getFieldValueString(k, "REPORT_NAME")%></option>
                                    <%}
                        }
                                    %>
                                </select>
                            </td>
                        </tr>
                        <%
                    }
                }
                session.setAttribute("kpiIds", kpiIds);
                        %>
                    </tbody></table>
            </form>
            <br>
            <table>
                <tr>
                    <td><input class="navtitle-hover" type="button" value="Save" onclick="javascript:kpiDrillSave();"></td>
                        <%--    <td><input class="btn" type="button" value="Save" onclick="javascript:kpiDrillSave();"></td>
                        <td><input class="navtitle-hover" type="button" value="Close Window" onclick="closeKpiDrill();"></td> --%>
                </tr>
            </table>
        </center>
        <%
            }
        %>
    </body>
</html>
