<%-- 
    Document   : reportTablePrivileges
    Created on : Jan 15, 2010, 1:22:41 PM
    Author     : mahesh.sanampudi@progenbusiness.com
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.ArrayList;" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<%//for clearing cache
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
%>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title> Table Previlages </title>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/lib/jquery/js/jquery-1.4.2.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/jQuery/jquery/ui/ui.core.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/ui.dialog.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/draggable/ui.draggable.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/draggable/ui.droppable.js"></script>
        <link type="text/css" href="<%=request.getContextPath()%>/jQuery/jquery/themes/base/ui.all.css" rel="stylesheet" />
        <link type="text/css" href="<%=request.getContextPath()%>/jQuery/jquery/themes/base/ui.theme.css" rel="stylesheet" />
        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/metadataButton.css" rel="stylesheet" />
        <script>
            function saveRepTabPriv(){
                var userId;
                var chkusersobj = parent.document.forms.myForm.chkusers;
                for(var i=0;i<chkusersobj.length;i++){
                    if(chkusersobj[i].checked){
                        userId=chkusersobj[i].value;
                    }
                }

                var expCheck = document.getElementById("ExpCheck").checked;
                var hideShowColsCheck = document.getElementById("hideShowColsCheck").checked;
                var TablePropertiesCheck = document.getElementById("TablePropertiesCheck").checked;
                var hideTableCheck = document.getElementById("hideTableCheck").checked;
                var transTableCheck = document.getElementById("transTableCheck").checked;

                if(expCheck == true)
                {
                    var expCheck=document.getElementById("ExpCheck").value //= document.getElementById("composeCheck").value;
                }
                if(hideShowColsCheck == true)
                {
                    var hideShowColsCheck=document.getElementById("hideShowColsCheck").value //= document.getElementById("topCheck").value;
                }
                if(TablePropertiesCheck == true)
                {
                    var TablePropertiesCheck=document.getElementById("TablePropertiesCheck").value //= document.getElementById("customCheck").value;
                }
                if(hideTableCheck == true)
                {
                    var hideTableCheck=document.getElementById("hideTableCheck").value //= document.getElementById("snapCheck").value;
                }
                if(transTableCheck == true)
                {
                    var transTableCheck=document.getElementById("transTableCheck").value //= document.getElementById("schedCheck").value;
                }
                if(expCheck == false && hideShowColsCheck == false && TablePropertiesCheck == false && hideTableCheck == false && transTableCheck == false)
                {
                    alert('Please Select Atleast One Previlage')
                }else{
                    document.forms.RepGrpPriForm.action="savePrivilages.do?method=saveRepTablePrevilages&userId="+userId+"&expCheck="+expCheck+"&hideShowColsCheck="+hideShowColsCheck+"&TablePropertiesCheck="+TablePropertiesCheck+"&hideTableCheck="+hideTableCheck+"&transTableCheck="+transTableCheck;
                    document.forms.RepGrpPriForm.submit();
                    parent.cancelUserTablePreveliges();
                }
            }
        </script>
        <style>
            *{
                font:11px verdana
            }
        </style>
    </head>
    <body>
        <form name="RepGrpPriForm" action="" method="post">
            <%
           
            ArrayList UserTablePrevileges = (ArrayList) session.getAttribute("UserTablePrevileges");
           
            %>
            <table width="100%" border="0px">
                <tr style="width:100%">
                    <td width="50%">
                        <%
            if (UserTablePrevileges.contains("Exports")) {%>
                        <input type="checkbox" id="ExpCheck" value="Exports" checked >&nbsp<b style="font-family:Verdana;font-size:12px;color:#369">Exports</b>
                        <%} else {%>
                        <input type="checkbox" id="ExpCheck" value="Exports">&nbsp<b style="font-family:Verdana;font-size:12px;color:#369">Exports</b>
                        <%}%>
                    </td>
                <input type="hidden" name="ExpCheck" id="ExpCheck" >
                <td width="50%">
                    <%
            if (UserTablePrevileges.contains("Hide/Show Columns")) {%>
                    <input type="checkbox" id="hideShowColsCheck" value="Hide/Show Columns" checked>&nbsp<b style="font-family:Verdana;font-size:12px;color:#369">Hide/Show Columns</b>
                    <% } else {%>
                    <input type="checkbox" id="hideShowColsCheck" value="Hide/Show Columns">&nbsp<b style="font-family:Verdana;font-size:12px;color:#369">Hide/Show Columns</b>
                    <%   }%>
                </td>
                <input type="hidden" name="hideShowColsCheck" id="hideShowColsCheck" >
                </tr>
                <tr style="width:100%">
                    <td width="50%">
                        <%
            if (UserTablePrevileges.contains("Table Properties")) {%>
                        <input type="checkbox" id="TablePropertiesCheck" value="Table Properties" checked>&nbsp<b style="font-family:Verdana;font-size:12px;color:#369">Table Properties</b>
                        <% } else {%>
                        <input type="checkbox" id="TablePropertiesCheck" value="Table Properties">&nbsp<b style="font-family:Verdana;font-size:12px;color:#369">Table Properties</b>
                        <% }%>
                    </td>
                <input type="hidden" name="TablePropertiesCheck" id="TablePropertiesCheck" >
                <td width="50%">
                    <%
            if (UserTablePrevileges.contains("Hide Table")) {%>
                    <input type="checkbox" id="hideTableCheck" value="Hide Table" checked>&nbsp<b style="font-family:Verdana;font-size:12px;color:#369">Hide Table</b>
                    <%} else {%>
                    <input type="checkbox" id="hideTableCheck" value="Hide Table">&nbsp<b style="font-family:Verdana;font-size:12px;color:#369">Hide Table</b>
                    <%}%>
                </td>
                <input type="hidden" name="hideTableCheck" id="hideTableCheck" >
                </tr>
                <tr style="width:100%">
                    <td width="50%">
                        <%
            if (UserTablePrevileges.contains("Transpose Table")) {%>
                        <input type="checkbox" id="transTableCheck" value="Transpose Table" checked>&nbsp<b style="font-family:Verdana;font-size:12px;color:#369">Transpose Table</b>
                        <% } else {%>
                        <input type="checkbox" id="transTableCheck" value="Transpose Table">&nbsp<b style="font-family:Verdana;font-size:12px;color:#369">Transpose Table</b>
                        <% }%>
                    </td>
                <input type="hidden" name="transTableCheck" id="transTableCheck" >
                <input type="hidden" name="userId" id="userId" >
                </tr>
                <tr style="width:100%" align="center" >
                <table align="center"  width="100%"><tr >
                        <td width="50%" align="center">
                            <center>
                                <input type="button" class="navtitle-hover" value="Save" onclick="saveRepTabPriv()">

                            </center>
                        </td>
                    </tr></table>
                </tr>
            </table>
        </form>
    </body>
</html>
