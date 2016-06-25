
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%//for clearing cache
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
%>

<html>
    <head>

        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/metadataButton.css" rel="stylesheet" />
        <script>
            function saveMember()
            {
                document.myForm.action="otherDimension.do";
                document.myForm.submit();
                parent.cancelotherDimension1();
            }
            function cancelDim()
            {
                parent.cancelotherDimension();
            }
        </script>
        <style>
            .label{
                font-family:verdana;
                font-size:12px;
                font-weight:normal;
            }

        </style>
        <title>JSP Page</title>
    </head>
    <body>
<%
        String colName = request.getParameter("colName");
        String colId = request.getParameter("colId");
        String tabId = request.getParameter("tabId");
        String colType = request.getParameter("colType");
        String bussTableId = request.getParameter("bussTableId");
        String bussColId = request.getParameter("bussColId");
        String tabName = request.getParameter("tabName");
        String grpId = request.getParameter("grpId");
        //out.println(bussColId+" "+bussTableId+" "+colType+" "+tabId+" "+colId+" "+colName);

        session.setAttribute("colName",colName);
        session.setAttribute("colId",colId);
        //request.setAttribute("tabId",tabId);
        session.setAttribute("colType",colType);
        session.setAttribute("bussTableId",bussTableId);
        //request.setAttribute("bussColId",bussColId);
        //request.setAttribute("tabName",tabName);
        session.setAttribute("grpId",grpId);
%>
<form name="myForm">
<center>
    <table>
        <tr>
                        <td><label class="label" >Dimension Name</label></td>
            <td><input type="text" name="dName"></td>
        </tr>
        <tr>
                        <td><label class="label" >Member <%=colName%> as </label></td>
            <td><input type="text" name="mName" value="<%=colName%>"></td>
        </tr>
    </table>

</center><br>
<center>
                <input type="button" value="save" class="navtitle-hover" style="width:auto" onclick="saveMember()">&nbsp;<input type="button" class="navtitle-hover" style="width:auto" value="Cancel" onclick="cancelDim()">
</center></form>
    </body>
</html>
