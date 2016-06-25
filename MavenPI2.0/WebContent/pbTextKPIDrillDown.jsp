
<%@page contentType="text/html" pageEncoding="UTF-8" import="utils.db.*,java.util.ArrayList,java.util.HashMap,java.util.Vector,com.progen.reportdesigner.action.DashboardTemplateAction,com.progen.reportdesigner.db.DashboardTemplateDAO,com.progen.report.pbDashboardCollection,prg.db.PbReturnObject,com.progen.report.kpi.KPIHelper,java.util.Map,com.progen.report.DashletDetail,prg.db.Container"%>

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
            String kpiType = null;
            kpiStr = request.getParameter("kpiId");
            kpiStr = kpiStr.substring(1);
            if (kpiStr != null) {
                elements = kpiStr.split(",");
            }
            String foldersIds = request.getParameter("folderIds");
            String reportId = request.getParameter("reportId");
            HashMap map = (HashMap) session.getAttribute("PROGENTABLES");
                Container container = (Container) map.get(reportId);
           
            pbDashboardCollection collect = new pbDashboardCollection();
             
            String dashletId = request.getParameter("dashletId");
            String kpiMasterId = request.getParameter("kpiMasterId");
            String fromDesigner = request.getParameter("fromDesigner");
            grpStatusStr = request.getParameter("grpStatus");
            kpinamesStr = request.getParameter("kpiNames");
            kpiType = request.getParameter("kpiType");
            collect = (pbDashboardCollection) container.getReportCollect();
             DashletDetail detail = collect.getDashletDetail(dashletId);
            kpinamesStr = kpinamesStr.substring(1);
            kpinames = kpinamesStr.split(",");
            grpElemntStatus = grpStatusStr.split(",");
            DashboardTemplateDAO dashboardDAO = new DashboardTemplateDAO();
            DashboardTemplateAction dashbrdAction = new DashboardTemplateAction();
            PbReturnObject retObj1 = new PbReturnObject();
            PbReturnObject retObj2 = new PbReturnObject();
            ArrayList kpiIds = new ArrayList();
            ArrayList groupNames = new ArrayList();
            //KPIHelper helper = new KPIHelper();
            kpiDrillRepMap = detail.TextkpiDrill;
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

            function TextkpiDrillSave()
            {
                var dashBoardId = '<%=reportId%>';
                var dashletId = '<%=dashletId%>';
                var kpiMasterId = '<%=kpiMasterId%>';
                var fromDesigner = '<%=fromDesigner%>';
                closeTextKpiDrill();
                parent.document.getElementById("Dashlets-"+dashletId).innerHTML='<center><img id="imgId" src="images/ajax.gif" align="middle"  width="75px" height="75px"  style="position:absolute" ></center>';
                $.post("dashboardTemplateViewerAction.do?templateParam2=saveTextKPIDrill", $("#myTextForm").serialize() , 
                         
                function(data){
                    parent.document.getElementById("Dashlets-"+dashletId).innerHTML=data
                     
                });
            }
            function closeTextKpiDrill()
            {
                parent.$("#TextkpiDrillDialog").dialog('close');
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
            <form name="myTextForm" method="post" id="myTextForm" action="">
                <table border="0" cellpadding="1" cellspacing="1" id="tablesorter" class="tablesorter" width="15%">
                    <thead><tr>
                            <th>KPI Name</th>
                            <th>Drill Down Structure</th>
                        </tr></thead><tfoot></tfoot><tbody>
                        <%
                for (int i = 0; i < elements.length; i++) {
                         String grpElemntName = elements[i];
                         retObj2 = dashboardDAO.getGroupKpiDrillReports(grpElemntName, foldersIds);
                         String selectedDrill = null;
                         if(kpiDrillRepMap!=null){
                             if(kpiDrillRepMap.get(grpElemntName)!=null)
                         selectedDrill = kpiDrillRepMap.get(grpElemntName).toString();
                           }
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
                                    %>
                                    <%  if(selectedDrill!=null && selectedDrill.equalsIgnoreCase(retObj2.getFieldValueString(k, "REPORT_ID"))){ %>
                                    <option selected value="<%=retObj2.getFieldValueInt(k, "REPORT_ID")%>"><%=retObj2.getFieldValueString(k, "REPORT_NAME")%></option>
                                    <% }else{%>
                                    <option value="<%=retObj2.getFieldValueInt(k, "REPORT_ID")%>"><%=retObj2.getFieldValueString(k, "REPORT_NAME")%></option>
                                     <%}%>
                                    <%} %>  
                                </select>
                            </td>
                          </tr>
                          <%}%>
                          <%
                session.setAttribute("kpiIds", kpiIds);
                session.setAttribute("groupNames", groupNames);
                        %>
                    </tbody></table>
                    <input type="hidden" id="REPORT_ID" name="REPORT_ID" value="<%=reportId%>"/>
                    <input type="hidden" id="DASHLET_ID" name="DASHLET_ID" value="<%=dashletId%>"/>
                    <input type="hidden" id="KPI_MASTER_ID" name="KPI_MASTER_ID" value="<%=kpiMasterId%>"/>
                    <input type="hidden" id="KPI_TYPE" name="KPI_TYPE" value="<%=kpiType%>">
            </form>
            <br>
            <table>
                <tr>
                    <td><input class="navtitle-hover" type="button" value="Save" onclick="javascript:TextkpiDrillSave();"></td>
                </tr>
            </table>
        </center>
        <%
            
                               }
        %>
    </body>
</html>
