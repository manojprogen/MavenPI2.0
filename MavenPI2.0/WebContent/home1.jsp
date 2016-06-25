
<%@page contentType="text/html" pageEncoding="windows-1252"%>
<%@ page import="utils.db.*"%>
<%@page import="prg.db.Session"%>
<%@page import="prg.db.PbReturnObject"%>
<%@page contentType="text/html"%>
<%@page import="java.sql.*"%>
<%@page import="prg.db.PbReturnObject" %>
<%@page import="prg.db.Session" %>
<%@page import="java.util.*"%>
<%@page import="prg.db.PbDb" %>




<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>


        <title>pi 1.0</title>
        <link rel="stylesheet" href="stylesheets/treeviewstyle/jquery.treeview.css" />
        <link rel="stylesheet" href="stylesheets/treeviewstyle/screen.css" />

        <script src="javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <script src="javascript/treeview/jquery.cookie.js" type="text/javascript"></script>
        <script src="javascript/treeview/jquery.treeview.js" type="text/javascript"></script>

        <script type="text/javascript" src="javascript/treeview/demo.js"></script>

        <script type="text/javascript" src="javascript/draggable/ui.core.js"></script>
        <script type="text/javascript" src="javascript/draggable/ui.draggable.js"></script>
        <script type="text/javascript" src="javascript/draggable/ui.droppable.js"></script>
        <link type="text/css" href="stylesheets/ui.all.css" rel="stylesheet" />
        <script type="text/javascript" src="javascript/ui.resizable.js"></script>
        <script type="text/javascript" src="javascript/ui.accordion.js"></script>
        <script type="text/javascript" src="javascript/ui.dialog.js"></script>
        <script type="text/javascript" src="javascript/ui.sortable.js"></script>
        <script type="text/javascript" src="javascript/queryDesign.js"></script>
        <script type="text/javascript" src="javascript/effects.core.js"></script>
        <script type="text/javascript" src="javascript/effects.explode.js"></script>
        <link type="text/css" href="stylesheets/demos.css" rel="stylesheet" />
        <script language="JavaScript" src="<%=request.getContextPath()%>/querydesigner/JS/jquery.columnfilters.js"></script>
        <!-- below five lines only added by bharathi reddy on 26-08-09 -->
        <link href="stylesheets/StyleSheet.css" rel="stylesheet" type="text/css" />
        <link href="stylesheets/confirm.css" rel="stylesheet" type="text/css" />
        <link href="stylesheets/jquery.contextMenu.css" rel="stylesheet" type="text/css" />
        <script src="javascript/jquery.simplemodal-1.1.1.js" type="text/javascript"></script>
        <script src="javascript/jquery.contextMenu.js" type="text/javascript"></script>
        <script type="text/javascript" src="javascript/ui.tabs.js"></script>


        <script type="text/javascript">
            $(function() {
                $("#tabs").tabs().find(".ui-tabs-nav").sortable({axis:'x'});
            });
        </script>





        <script src="javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>

        <script type="text/javascript" src="../JS//ui.core.js"></script>
        <script type="text/javascript" src="../JS/ui.tabs.js"></script>
        <script type="text/javascript" src="../JS/ui.sortable.js"></script>
        <link type="text/css" href="../css/jquery/demos.css" rel="stylesheet" />
        <link type="text/css" href="../css/jquery/ui.all.css" rel="stylesheet" />
        <script type="text/javascript">
            $(function() {
                $("#tabs").tabs().find(".ui-tabs-nav").sortable({axis:'x'});;
            });
        </script>
    </head>
    <body>

        <%

        //For Getting Roles
        try {
            PbDb pbdb = new PbDb();
          

            String userId = request.getParameter("userId");
            //String userId="82";
          
            String userFoldersql = "SELECT FOLDER_ID, FOLDER_NAME FROM PRG_USER_FOLDER where folder_id in(SELECT  USER_FOLDER_ID FROM PRG_GRP_USER_FOLDER_ASSIGNMENT where user_id=" + userId + ")";
            PbReturnObject folderpbro = pbdb.execSelectSQL(userFoldersql);
            folderpbro.writeString();
            //String userreports = "SELECT REPORT_ID, REPORT_NAME FROM PRG_AR_REPORT_MASTER where REPORT_ID in(SELECT  REPORT_ID FROM PRG_AR_USER_REPORTS where  user_id=" + userId + ") and report_type='R'";
            String userreports = "SELECT A.REPORT_ID,A.REPORT_NAME FROM PRG_AR_REPORT_MASTER A,(SELECT distinct REPORT_ID FROM PRG_AR_USER_REPORTS WHERE user_id=" + userId + ") B WHERE A.REPORT_ID =b.REPORT_ID and A.report_type='R'";

            PbReturnObject reportpbro = pbdb.execSelectSQL(userreports);
            reportpbro.writeString();
           // String userdashs = "SELECT REPORT_ID, REPORT_NAME FROM PRG_AR_REPORT_MASTER where REPORT_ID in(SELECT  REPORT_ID FROM PRG_AR_USER_REPORTS where  user_id=" + userId + ") and report_type='D'";
            String userdashs = "SELECT A.REPORT_ID,A.REPORT_NAME FROM PRG_AR_REPORT_MASTER A,(SELECT distinct REPORT_ID FROM PRG_AR_USER_REPORTS WHERE user_id=" + userId + ") B WHERE A.REPORT_ID =b.REPORT_ID and A.report_type='D'";

            PbReturnObject dashpbro = pbdb.execSelectSQL(userreports);
            dashpbro.writeString();
            String folderId="";
            String rolerepdashs = "SELECT REPORT_ID, REPORT_NAME FROM PRG_AR_REPORT_MASTER where REPORT_ID in(SELECT  REPORT_ID FROM PRG_AR_USER_REPORTS where  folder_id=" + folderId + ")";

            PbReturnObject rolereppbro = pbdb.execSelectSQL(userreports);
            rolereppbro.writeString();
        %>

        <div id="tabs" style="width:100%">
            <ul>
                <li><a href="#RolesTab">USER FOLDERS</a></li>
                <li><a href="#assignedReports">ALL ASSIGNED REPORTS</a></li>
            </ul>
            <div id="RolesTab"  style="width:99%">
                <table width="100%">
                    <tr>
                        <td width="50%">
                            <div  class="prgtableheader2" style="width:100%;background-color:silver;color:black"><b>User Folders</b></div>
                            <br>
                            <div style="height:250px" id="Roles">
                                <table border="0" width="570px"  >

                                    <%
                                 for (int i = 0; i < folderpbro.getRowCount(); i++) {
                                    %>
                                    <tr>
                                        <td style="font-size:12px">
                                            <b><%=folderpbro.getFieldValueString(i, 1)%></b>
                                        </td>
                                    </tr>


                                    <% }
                                    %>
                                    <tr><td>&nbsp;</td></tr>
                                    <tr><td><a href="baseAction.do?param=loginApplication&userId=<%=userId%>"> QueryDesigner</a></td></tr>
                                </table>
                            </div>

                        </td>



                    </tr>
                </table>
            </div>
            <div id="assignedReports"  style="width:100%">
                <table width="99%">
                    <tr>
                        <td width="50%">
                            <div  class="prgtableheader2" style="width:100%;background-color:silver;color:black"><b> Reports</b></div>
                            <br>
                            <div style="height:250px" id="Reports">

                                <table border="0" width="570px"  >

                                    <%
                    for (int i = 0; i < reportpbro.getRowCount(); i++) {
                                    %>
                                    <tr>
                                        <td style="font-size:12px">
                                            <b><%=reportpbro.getFieldValueString(i, 1)%></b>
                                        </td>
                                    </tr>


                                    <% }
                                    %>

                                    <tr><td>&nbsp;</td></tr>
                                    <tr><td><a href="baseAction.do?param=loginApplication&userId=<%=userId%>"> QueryDesigner</a></td></tr>
                                </table>
                            </div>
                        </td>



                    </tr>
                </table>
            </div>
        </div>

        <%} catch (Exception e) {
            e.printStackTrace();
        }%>
        <%  %>
    </body>
</html>
