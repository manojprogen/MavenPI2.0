<%-- 
    Document   : manageStickyNote
    Created on : Aug 24, 2010, 2:51:53 PM
    Author     : Administrator
--%>


<%@page contentType="text/html" pageEncoding="UTF-8" import="prg.db.PbDb,prg.db.PbReturnObject"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <%
                int REPORTID = 0;
                if (request.getParameter("REPORTID") != null && request.getParameter("REPORTID") != "") {
                    REPORTID = Integer.parseInt(request.getParameter("REPORTID"));
                }

                PbReturnObject stickyListReturnObj = new PbReturnObject();
                PbDb pbdb = new PbDb();
                String qry = "select * from PRG_USER_STICKYNOTE where REPORT_ID=" + REPORTID;
                stickyListReturnObj = pbdb.execSelectSQL(qry);
                String contextPath=request.getContextPath();
    %>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Manage Sticky Notes</title>
        <link type="text/css" href="<%=contextPath%>/jQuery/jquery/themes/base/ui.theme.css" rel="stylesheet" />
        <script type="text/javascript" src="<%=contextPath%>/JS/stickyNote.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/lib/jquery/js/jquery-1.4.2.min.js"></script>

        <script>
            function shwStickyNote(obj,repId){
                parent.shwStickyNote(obj,repId);
            }
            function deleteSticky(stkId){
                parent.deleteSticky(stkId);
//                window.location.reload(true);
            }
        </script>
    </head>

    <body>
        <table  width="100%">
            <%
                        if (stickyListReturnObj.getRowCount() > 0) {
                            for (int i = 0; i < stickyListReturnObj.getRowCount(); i++) {
            %>
            <tr>
                <td  align="left" width="50%">
                    <a href="javascript:void(0)" onclick="shwStickyNote('<%=stickyListReturnObj.getFieldValueString(i, "STICKY_NOTES_ID")%>',<%=REPORTID%>)" style="text-decoration: none;font-size:10px;font-family:Verdana;color:#336699"><%=stickyListReturnObj.getFieldValueString(i, "STICKY_LABEL")%></a>
                </td>
                <td title="Delete Note" align="right" width="50%">
                    <a class="ui-icon ui-icon-trash" onclick="deleteSticky('<%=stickyListReturnObj.getFieldValueString(i, "STICKY_NOTES_ID")%>')" href="javascript:void(0)"></a>
                </td>
            </tr>
            <%}
                        }%>
        </table>
    </body>
    
</html>
