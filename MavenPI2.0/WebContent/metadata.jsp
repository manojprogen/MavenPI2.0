<%@page contentType="text/html" pageEncoding="UTF-8" import="prg.db.PbReturnObject,prg.db.PbDb"%>


<html>
    <head>
        <title>Metadata</title>
        <script src="<%=request.getContextPath()%>/javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.tablesorter.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/tablesorter/addons/pager/jquery.tablesorter.pager.js"></script>
        <script language="JavaScript" src="<%=request.getContextPath()%>/javascript/jquery.columnfilters.js"></script>

        <script type="text/javascript" src="<%=request.getContextPath()%>/tablesorter/docs/js/chili/chili-1.8b.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/tablesorter/docs/js/docs.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/tablesorter/ui.draggable.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/tablesorter/dragTable.js"></script>



        <link rel="stylesheet" href="<%=request.getContextPath()%>/tablesorter/docs/css/jq.css" type="text/css" media="print, projection, screen" />
        <link rel="stylesheet" href="<%=request.getContextPath()%>/stylesheets/style.css" type="text/css" media="print, projection, screen">


        <script>
            function createReport(){
                document.getElementById('report').style.display='block';
                document.getElementById('fade').style.display='block';
            }

            function cancelReport(){
                document.getElementById('duplicate').innerHTML = '';
                document.getElementById('save').disabled = false;
                document.getElementById('report').style.display='none';
                document.getElementById('fade').style.display='none';

            }
            function tabmsg1(){
                document.getElementById('reportDesc').value = document.getElementById('reportName').value;
            }

            function saveReport(){

                var roleid=document.getElementById('roleid').value;
                var reportName = document.getElementById('reportName').value;
                var reportDesc = document.getElementById('reportDesc').value;

                if(reportName==''){
                    alert("Please enter Report Name");
                }
                else  if(reportDesc==''){
                    alert("Please enter Report Description")
                }
                else{
                    $.ajax({
                    url: 'reportTemplateAction.do?templateParam=checkReportName&reportName='+reportName+"&roleid="+roleid,
                    success: function(data){
                        if(data!=""){
                            document.getElementById('duplicate').innerHTML = "Report Name already exists";
                            document.getElementById('save').disabled = true;
                        }
                        else if(data=='')
                        {                            
                            document.forms.reportForm.action = "reportTemplateAction.do?templateParam=goToReportDesigner&reportName="+reportName+"&reportDesc="+reportDesc;
                            document.forms.reportForm.method="POST";
                            document.forms.reportForm.submit();
                        }
                    }
                });
                }
                
            }
            $(function() {
                $("#tablesorter")
                .tablesorter({widthFixed: true, widgets: ['zebra']})
                .tablesorterPager({container: $('#pager')})
                .columnFilters();

            });
            function viewReport(path){
                document.forms.reportForm.action=path;
                document.forms.reportForm.submit();
            }
            function userFolderAssignment(){

                var frameObj=document.getElementById("userFolderAssignmentDisp");
                var source = "userFolderAssignment.jsp";
                frameObj.src=source;
                document.getElementById('userFolderassign').style.display='none';
                frameObj.style.display='block';
                document.getElementById('fade').style.display='block';
            }
            function cancelUsersFolders(){
                document.getElementById("userFolderAssignmentDisp").style.display='none';
                document.getElementById('fade').style.display='';
                document.getElementById('userFolderassign').style.display='';
                cancelUsersFolders();
            }


        </script>

        <style>
            .black_overlay{
                display: none;
                position: absolute;
                top: 0%;
                left: 0%;
                width: 200%;
                height: 200%;
                background-color: black;
                z-index:1001;
                -moz-opacity: 0.5;
                opacity:.50;
                filter:alpha(opacity=50);
                overflow:auto;
            }

            .white_content {
                display: none;
                position: absolute;
                top: 30%;
                left: 35%;
                width: 50%;
                height:50%;
                padding: 16px;
                border: 10px solid silver;
                background-color: white;
                z-index:1002;
            }
        </style>

    </head>
    <%   String reportId = request.getParameter("ReportId");
        PbReturnObject list = null;
        if (request.getAttribute("reportList") != null) {
            list = (PbReturnObject) request.getAttribute("reportList");
        }
        PbDb pbdb=new PbDb();
        String folderId = "select folder_id from prg_ar_report_details where report_id=" + reportId;
            PbReturnObject roleobj = pbdb.execSelectSQL(folderId);
            int roleid = roleobj.getFieldValueInt(0, 0);
    %>
    <body>
        <form name="reportForm"  method="post">
            <table align="center" id="tablesorter" class="tablesorter">
                <thead>
                    <tr>
                        <th>Report Name</th>
                        <th>Report Description</th>
                        <th>Type</th>
                        
                    </tr>
                </thead>
                <tbody>
                    <%for (int i = 0; i < list.getRowCount(); i++) {%>
                    <tr>
                        <td>
                            <a href="javascript:void(0)" onclick='javascript:viewReport("reportViewer.do?reportBy=viewReport&REPORTID=<%=list.getFieldValueString(i, 0)%>")'> <%=list.getFieldValueString(i, 1)%></a>
                        </td>
                        <td>
                            <%=list.getFieldValueString(i, 2)%>
                        </td>
                        <td>
                            <%
    if (list.getFieldValueString(i, 3) != null && list.getFieldValueString(i, 3).equalsIgnoreCase("R")) {
                            %>
                            Report
                            <%     } else {
                            %>
                            Dashboard
                            <%     }
                            %>                           
                        </td>
                    </tr>
                    <%}%>
                </tbody>
            </table>
            <br><br>
            <center>
            <input type="button" value="Create Report" onclick="javascript:createReport()"></center>

            <div id="report" class="white_content"  align="justify" style="height:150px;width:400px">
                <center>
                    <br><br>

                    <table style="width:80%" border="0">
                        <tr>
                            <td valign="top" class="myHead" style="width:30%">Report Name</td>
                            <td valign="top" style="width:80%">
                                <input type="text" maxlength="35" name="reportName" style="width:80%" id="reportName" onkeyup="tabmsg1()" onfocus="document.getElementById('save').disabled = false;"><br><span id="duplicate" style="color:red"></span>
                            </td>
                        </tr>
                        <tr>
                            <td  valign="top" class="myHead" style="width:30%">Description</td>
                            <td valign="top" style="width:70%">
                                <textarea name="reportDesc" id="reportDesc" style="width:80%"></textarea>
                            </td>
                        </tr>
                    </table>
                    <table>
                        <tr>
                            <td><input type="button" class="btn" value="Next" id="save" onclick="saveReport()"></td>
                            <td><input type="button" class="btn" value="Cancel" onclick="cancelReport()"></td>
                        </tr>
                    </table>

                </center>
            </div>
      
        <div id="fade" class="black_overlay"></div>
        <div id="userFolderassign" >
            <a href="javascript:userFolderAssignment()">Business Role assignment</a>
        </div>
        <div id="userFolderAssignmentDisplay">
            <iframe  id="userFolderAssignmentDisp" NAME='userFolderAssignmentDisp'  STYLE='display:none;height:400px'   class="white_content" SRC='' ></iframe>

        </div>
        <input type="hidden" id="roleid" name="" value="<%=roleid%>" />
  </form>

    </body>
</html>