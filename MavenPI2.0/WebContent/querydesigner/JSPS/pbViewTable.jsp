<%--
    Document   : pbViewTable
    Created on : Aug 17, 2009, 5:22:59 PM
    Author     : santhosh.kumar@progenbusiness.com
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="prg.db.PbReturnObject"%>
<%@page import="com.progen.datadisplay.client.PbDataDisplayManager"%>
<%
        String tableIds = request.getParameter("tableIds");
        PbDataDisplayManager manager = new PbDataDisplayManager();
        if (tableIds == null || "".equalsIgnoreCase(tableIds)) {
            tableIds = "2";
        }
         PbReturnObject retObj=new PbReturnObject();
          String[] tableColumnNames=null;
        if(!manager.viewTable(tableIds).isEmpty())
         retObj =(PbReturnObject) manager.viewTable(tableIds).get(0);
         if(retObj.getRowCount()>0)
         {
          tableColumnNames = retObj.getColumnNames();
           }


%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <link rel="stylesheet" href="<%=request.getContextPath()%>/tablesorter/docs/css/jq.css" type="text/css" media="print, projection, screen" />
        <link rel="stylesheet" href="<%=request.getContextPath()%>/tablesorter/themes/blue/style.css" type="text/css" media="print, projection, screen" />
<!--        <script type="text/javascript" src="<%=request.getContextPath()%>/tablesorter/jquery-latest.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/tablesorter/jquery.tablesorter.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/tablesorter/addons/pager/jquery.tablesorter.pager.js"></script>
        <script language="JavaScript" src="<%=request.getContextPath()%>/tablesorter/jquery.columnfilters.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/tablesorter/docs/js/chili/chili-1.8b.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/tablesorter/docs/js/docs.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/tablesorter/ui.draggable.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/tablesorter/dragTable.js"></script>-->


        <script type="text/javascript">

            $(function() {
                $("table")
                .tablesorter({widthFixed: true, widgets: ['zebra']})
                .tablesorterPager({container: $("#pager")});
            });


        </script>
    </head>
    <body>
        <img id="imgId" src="images/ApplicationSLA_icon.jpg" width="100px" height="100px" style="left:25%;top:25%">
        <div class="drag" id="main" style="width:100px">
            <script>
                var divObj=document.getElementById("main");
                divObj.style.visibility="hidden"
            </script>
            <div id="pager" class="pager" align="left" >
                <form>
                    <img src="<%=request.getContextPath()%>/tablesorter/addons/pager/icons/first.png" class="first"/>
                    <img src="<%=request.getContextPath()%>/tablesorter/addons/pager/icons/prev.png" class="prev"/>
                    <input type="text" class="pagedisplay"/>
                    <img src="<%=request.getContextPath()%>/tablesorter/addons/pager/icons/next.png" class="next"/>
                    <img src="<%=request.getContextPath()%>/tablesorter/addons/pager/icons/last.png" class="last"/>
                    <select class="pagesize">
                        <option selected value="5">5</option>
                        <option value="10">10</option>
                    </select>
                </form>
            </div>
            <table cellspacing="1" class="tablesorter" id="tablesorter" width="100px">
                <thead>
                    <tr valign="top">
                        <%for (int i = 0; i < tableColumnNames.length; i++) {%>
                        <th><%=tableColumnNames[i]%></th>
                        <%}%>
                    </tr>
                </thead>
                <tfoot>
                </tfoot>
                <tbody>
                    <%for (int rowId = 0; rowId < retObj.getRowCount(); rowId++) {%>
                    <tr valign="top">
                        <%for (int colId = 0; colId < tableColumnNames.length; colId++) {%>
                        <td><%=retObj.getFieldValue(rowId, tableColumnNames[colId])%></td>
                        <%}%>
                    </tr>
                    <%}%>
                </tbody>
            </table>
            <script>
                $(document).ready(function() {
                    $('table#tablesorter').columnFilters();
                });
                divObj.style.visibility="visible";
                document.getElementById("imgId").style.visibility="hidden";
                document.getElementById("imgId").style.display="none";
            </script>
        </div>

    </body>
</html>
