<%--
    Document   : dimensionTableList1
    Created on : Aug 21, 2009, 5:45:00 PM
    Author     : Saurabh
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" import="java.util.*,prg.db.PbDb,prg.db.PbReturnObject"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%//for clearing cache
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
           String contextPath=request.getContextPath();
%>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <script src="javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>
        <script src="javascript/treeview/jquery.cookie.js" type="text/javascript"></script>
        <script src="javascript/treeview/jquery.treeview.js" type="text/javascript"></script>

<!--        <script type="text/javascript" src="javascript/treeview/demo.js"></script>

        <script type="text/javascript" src="javascript/draggable/ui.core.js"></script>
        <script type="text/javascript" src="javascript/draggable/ui.draggable.js"></script>
        <script type="text/javascript" src="javascript/draggable/ui.droppable.js"></script>
        <script type="text/javascript" src="javascript/ui.resizable.js"></script>
        <script type="text/javascript" src="javascript/ui.accordion.js"></script>
        <script type="text/javascript" src="javascript/ui.dialog.js"></script>
        <script type="text/javascript" src="javascript/ui.sortable.js"></script>-->
        <script type="text/javascript" src="javascript/queryDesign.js"></script>
<!--        <script type="text/javascript" src="javascript/effects.core.js"></script>
        <script type="text/javascript" src="javascript/effects.explode.js"></script>-->
        <link type="text/css" href="stylesheets/demos.css" rel="stylesheet" />
        <link type="text/css" href="<%=contextPath%>/stylesheets/metadataButton.css" rel="stylesheet" />


        <style>

            .prgtableheader
            {   font-family: Verdana, Arial, Helvetica, sans-serif;
                font-size: 11px;
                font-weight: bold;
                color: #3A457C;
                padding-left:12px;
                background-color:#EAF2F7;
                height:100%;
            }
            .btn {
                font-family: Verdana, Arial, tahoma, sans-serif;
                font-size:11px;
                background-color: #B5B5B5;
                color:#454545;
                font-weight:600;
                height:22px;
                text-decoration:none;
                cursor: pointer;
                border-bottom: 1px solid #999999;
                border-right: 1px solid #999999;
                border-left: 1px solid #F5F5F5;
                border-top:1px solid #F5F5F5;
                margin:2px;
            }
        </style>
    </head>
    <body>
          <script>
            var y="";
            var xmlHttp;
            var ctxPath;

            function saveTables()
            {   var connId=document.getElementById("connId").value;
                var dimId=document.getElementById("dimId").value;
                document.myForm.action="dimTablesList.do?connId="+connId+"&dimId="+dimId;
                // alert( document.myForm.action);
                document.myForm.submit();

            }



            function cancelTables()
            {
                //alert("kkk");
                parent.cancelTablesp1();

            }

        </script>
        <%  String dimId = request.getParameter("dimId");
                ////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("dim in jsop==" + dimId);
                String connId = String.valueOf(session.getAttribute("connId"));
                response.sendRedirect("dimTablesList.do?connId=" + connId + "&dimId=" + dimId);
        %>

        <%--     String Query = " SELECT CONNECTION_ID, CONNECTION_NAME FROM PRG_USER_CONNECTIONS";
          PbReturnObject pbro = new PbDb().execSelectSQL(Query);


    <br><br>
    <center>
     <form name="myForm">
        <input type="hidden" name="dimId" id="dimId" value="<%=dimId%>">
        <table style="width:50%">

            <tr>
                <td>Connection</td>
                <td >
                    <select id="connId" name="connId" style="width:146px">
                        <%for (int i = 0; i < pbro.getRowCount(); i++) {%>
                        <option value="<%=pbro.getFieldValueInt(i, 0)%>"><%=pbro.getFieldValueString(i, 1)%></option>
                        <%}%>
                    </select>
            </td></tr>
        </table>
        <br>
        <input type="button"  value="Connect" onclick="saveTables()">
        <input type="button"  value="Cancel" onclick="cancelTables()">
    </center>
    </form>
</body>
</html>--%>
                 