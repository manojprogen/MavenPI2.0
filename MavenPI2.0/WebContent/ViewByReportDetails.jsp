<%-- 
    Document   : ViewByReportDetails
    Created on : Mar 29, 2010, 12:05:32 PM
    Author     : sathish
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="prg.db.PbReturnObject,java.util.ArrayList,prg.db.PbDb" %>



<%
            String userId = request.getParameter("userId");
            //////.println("userid..."+userId);
            PbDb pbdb = new PbDb();

            String query1 = "select FOLDER_NAME,FOLDER_ID from prg_user_folder where folder_id in(select user_folder_id from prg_grp_user_folder_assignment where user_id=" + userId + ")";
            PbReturnObject list = pbdb.execSelectSQL(query1);
            int rowCount = list.getRowCount();
            ////.println("rowCount  " + rowCount);
            PbReturnObject list1;
%>

<html>
    <head>
        <script src="javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <script src="javascript/treeview/jquery.cookie.js" type="text/javascript"></script>
        <script src="javascript/treeview/jquery.treeview.js" type="text/javascript"></script>
        <script type="text/javascript" src="javascript/treeview/demo.js"></script>
        <link rel="stylesheet" href="stylesheets/treeviewstyle/jquery.treeview.css" />
        <link rel="stylesheet" href="stylesheets/treeviewstyle/screen.css" />
        <script type="text/javascript">
             
            $(document).ready(function(){
                $(".treeview").treeview({
                    animated: "normal",
                    unique:true
                });
            });
              
        </script>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <center>
        <font style="color:#369;font-weight:bold;font-size:12px"> Business Roles</font>
        </center>
        <div style="height:450px;overflow:auto">
            <%
                int folderId;
                String folderName;
                for (int i = 0; i < rowCount; i++) {
                    folderId = list.getFieldValueInt(i, "FOLDER_ID");
                    folderName = list.getFieldValueString(i, "FOLDER_NAME");
                    //////.println("folderid..." + folderId);
                   // ////.println("folderName" + folderName);
            %>

            <ul id="roles" class="treeview">
                <li id="<%=folderId%>" class="close">
                    <img src='icons pinvoke/report.png'></img>
                    <span id="<%=folderId%> " style='font-family:verdana;font-size:10pt'><%=folderName%></span>
                    <ul style="display:none">

                        <%
                          String getrepssql = "select  REPORT_ID from  prg_ar_report_details  where folder_id in (" + folderId + ")";
                        PbReturnObject pbr = pbdb.execSelectSQL(getrepssql);
                        if(pbr.getRowCount()>0){
                             String repIds = "";
                for (int n = 0; n < pbr.getRowCount(); n++) {
                    repIds += "," + String.valueOf(pbr.getFieldValueInt(n, 0));
                }
                             if(!repIds.equalsIgnoreCase("")){
                             repIds=repIds.substring(1);
                             }
                    String query2 = "select REPORT_NAME from prg_ar_report_master where report_id " +
                            "in(select REPORT_ID from prg_ar_user_reports where report_id in(" + repIds + ") " +
                            "and user_id in("+userId+"))";
                    ////.println("quertyy"+query2);
                    list1 = pbdb.execSelectSQL(query2);
                    int count = list1.rowCount;
                    for (int j = 0; j < count; j++) {
                        String reportName = list1.getFieldValueString(j, "REPORT_NAME");
                        //////.println("rep name " + reportName);
                        %>
                        <li>
                            <%=reportName%>
                        </li>
                        <%}
                    }%>

                    </ul>
                </li>
            </ul>

            <%}%>
        </div>
        

    </body>
</html>
