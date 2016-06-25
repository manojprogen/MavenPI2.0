
<%@page contentType="text/html" pageEncoding="UTF-8" import="utils.db.*,java.util.ArrayList,java.util.HashMap,java.util.Vector,com.progen.reportdesigner.action.DashboardTemplateAction,com.progen.reportdesigner.db.DashboardTemplateDAO,prg.db.PbReturnObject,java.util.Map,com.progen.report.kpi.KPIHelper"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<%
            String kpiStr = "";
            String[] elements = null;
            String grpStatusStr = "";
            String kpinamesStr = "";
            String[] grpElemntStatus = null;
            String[] kpinames = null;
            String ReportId = "";
            Map kpiDrillRepMap = null;
            kpiStr = request.getParameter("kpiId");
            kpiStr = kpiStr.substring(1);
            if (kpiStr != null) {
                elements = kpiStr.split(",");
            }
            String foldersIds = request.getParameter("folderIds");
            String reportId = request.getParameter("reportId");
            String dashletId = request.getParameter("dashletId");
            String kpiMasterId = request.getParameter("kpiMasterId");
            String fromDesigner = request.getParameter("fromDesigner");
            grpStatusStr = request.getParameter("grpStatus");
            kpinamesStr = request.getParameter("kpiNames");
            kpinames = kpinamesStr.split(",");
            grpElemntStatus = grpStatusStr.split(",");
            DashboardTemplateDAO dashboardDAO = new DashboardTemplateDAO();
            DashboardTemplateAction dashbrdAction = new DashboardTemplateAction();
            PbReturnObject retObj1 = new PbReturnObject();
            PbReturnObject retObj2 = new PbReturnObject();
            ArrayList kpiIds = new ArrayList();
            ArrayList groupNames = new ArrayList();
            KPIHelper helper = new KPIHelper();
            kpiDrillRepMap = helper.getKPIDrillMap(request, reportId, dashletId);
            String contextPath=request.getContextPath();
%>

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
                var dashBoardId = '<%=reportId%>';
                var dashletId = '<%=dashletId%>';
                var kpiMasterId = '<%=kpiMasterId%>';
                var fromDesigner = '<%=fromDesigner%>';

                $.post("dashboardTemplateViewerAction.do?templateParam2=saveKPIDrill", $("#myForm").serialize() ,
                function(data){
                    closeKpiDrill();
                    parent.displayKPI(dashletId, dashBoardId, "", "", kpiMasterId, "", "", "", fromDesigner);
                });
            }
            function closeKpiDrill()
            {
                parent.$("#kpiDrillDialog").dialog('close');
            }

        </script>
        <style type="text/css">
            *{font:11px verdana}
        </style>

    </head>
    <body>
        <%
            if (elements != null) {
        %>
        <center>
            <br>
            <form name="myForm" method="post" id="myForm" action="">
                <table border="0" cellpadding="1" cellspacing="1" id="tablesorter" class="tablesorter" width="15%">
                    <thead><tr>
                            <th>KPI Name</th>
                            <th>Drill Down Structure</th>
                        </tr></thead><tfoot></tfoot><tbody>
                        <%
                for (int i = 0; i < elements.length; i++) {
                    boolean isGroupElmnt = Boolean.parseBoolean(grpElemntStatus[i]);
                    if(!isGroupElmnt){
                //    retObj1 = dashboardDAO.getKpiDrillNames(elements[i],kpiMasterId);
                  //  for (int j = 0; j < retObj1.getRowCount(); j++) {
                        kpiIds.add(elements[i]);
                        retObj2 = dashboardDAO.getGroupKpiDrillReports(kpinames[i], foldersIds);
                        String selectId = elements[i] + "_report";
                        %>
                        <tr>
                            <td width="25%" id="column1" class="myTextbox5"><%=kpinames[i]%></td>
                            <td width="25%">
                                <select class="myTextbox5" name="<%=selectId%>" id="<%=selectId%>">
                                    <option value="0">None</option>
                                    <%
                        for (int k = 0; k < retObj2.getRowCount(); k++) {
                            String generaterepId = "";
                            generaterepId = retObj2.getFieldValueString(k, "REPORT_ID")+"~"+retObj2.getFieldValueString(k, "REPORT_TYPE");
                            if (kpiDrillRepMap != null && kpiDrillRepMap.size() != 0) {
                                    generaterepId = retObj2.getFieldValueString(k, "REPORT_ID")+"~"+retObj2.getFieldValueString(k, "REPORT_TYPE");
                                    if((kpiDrillRepMap.get(elements[i]).toString()).equalsIgnoreCase("none")){ %>
                                     <option value="none">none</option>
                                   <% }
                               else if (kpiDrillRepMap.get(elements[i]).toString().equalsIgnoreCase(retObj2.getFieldValueString(k, "REPORT_ID"))) {
                                    %>
                                    <option selected value="<%=generaterepId%>"><%=retObj2.getFieldValueString(k, "REPORT_NAME")%></option>
                                    <%} else {%>
                                    
                                    <option value="<%=generaterepId%>"><%=retObj2.getFieldValueString(k, "REPORT_NAME")%></option>
                                    <%}
                            } else {%>
                                    <option value="<%=generaterepId%>"><%=retObj2.getFieldValueString(k, "REPORT_NAME")%></option>
                                    <%}
                        }
                                    %>
                                </select>
                            </td>
                        </tr>
                        <%
              //      }
                    }//end of if
                    else{
                         String grpElemntName = elements[i];
                         retObj2 = dashboardDAO.getGroupKpiDrillReports(grpElemntName, foldersIds);
                         String selectId = grpElemntName + "_report";
                         groupNames.add(grpElemntName);
                          %>
                          <tr>
                           <td width="25%" id="column1" class="myTextbox5"><%=kpinames[i]%></td>
                                                       <td width="25%">
                                <select class="myTextbox5" name="<%=selectId%>" id="<%=selectId%>">
                                    <option value="0">None</option>
                                    <%
                        for (int k = 0; k < retObj2.getRowCount(); k++) {
                             String generaterepId = "";
                             generaterepId = retObj2.getFieldValueString(k, "REPORT_ID")+"~"+retObj2.getFieldValueString(k, "REPORT_TYPE");
                            if (kpiDrillRepMap != null && kpiDrillRepMap.size() != 0) {
                               generaterepId = retObj2.getFieldValueString(k, "REPORT_ID")+"~"+retObj2.getFieldValueString(k, "REPORT_TYPE");
                               if((kpiDrillRepMap.get(elements[i]).toString()).equalsIgnoreCase("none")){ %>
                                     <option value="none">none</option>
                                   <% }
                               else if (kpiDrillRepMap.get(elements[i]).toString().equalsIgnoreCase(retObj2.getFieldValueString(k, "REPORT_ID"))) {
                                    %>
                                    <option selected value="<%=generaterepId%>"><%=retObj2.getFieldValueString(k, "REPORT_NAME")%></option>
                                    <%} else {%>
                                    <option value="<%=generaterepId%>"><%=retObj2.getFieldValueString(k, "REPORT_NAME")%></option>
                                    <%}
                            } else {%>
                                    <option value="<%=generaterepId%>"><%=retObj2.getFieldValueString(k, "REPORT_NAME")%></option>
                                    <%}
                        }
                                    %>
                                </select>
                            </td>
                          </tr>
                       <% }
                    //write code for else
                }
                session.setAttribute("kpiIds", kpiIds);
                session.setAttribute("groupNames", groupNames);
                        %>
                    </tbody></table>
                    <input type="hidden" id="REPORT_ID" name="REPORT_ID" value="<%=reportId%>"/>
                    <input type="hidden" id="DASHLET_ID" name="DASHLET_ID" value="<%=dashletId%>"/>
                    <input type="hidden" id="KPI_MASTER_ID" name="KPI_MASTER_ID" value="<%=kpiMasterId%>"/>
            </form>
            <br>
            <table>
                <tr>
                    <td><input class="navtitle-hover" type="button" value="Save" onclick="javascript:kpiDrillSave();"></td>
                </tr>
            </table>
        </center>
        <%
            }
        %>
    </body>
</html>
