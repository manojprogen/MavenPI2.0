
<%--
    Document   : reportGraphPrivileges
    Created on : Jan 15, 2010, 1:22:24 PM
    Author     : mahesh.sanampudi@progenbusiness.com
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.ArrayList;" %>

<%//for clearing cache
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title> Graph Previlages </title>
       <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/lib/jquery/js/jquery-1.4.2.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/jQuery/jquery/ui/ui.core.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/ui.dialog.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/draggable/ui.draggable.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/draggable/ui.droppable.js"></script>
        <link type="text/css" href="<%=request.getContextPath()%>/jQuery/jquery/themes/base/ui.all.css" rel="stylesheet" />
        <link type="text/css" href="<%=request.getContextPath()%>/jQuery/jquery/themes/base/ui.theme.css" rel="stylesheet" />
        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/metadataButton.css" rel="stylesheet" />
        <script>
            function saveRepGrpPriv(){
                var userId;
                var chkusersobj = parent.document.forms.myForm.chkusers;
                for(var i=0;i<chkusersobj.length;i++){
                    if(chkusersobj[i].checked){
                        userId=chkusersobj[i].value;
                    }
                }

                var grpTypes = document.getElementById("grptypCheck").checked;
                var grpCols = document.getElementById("grpcolsCheck").checked;
                var rowsCheck = document.getElementById("rowsCheck").checked;
                var grpProperties = document.getElementById("grpprpCheck").checked;
                var addGraphs = document.getElementById("addGraphsCheck").checked;
                var editGrpName = document.getElementById("editGrpNameCheck").checked;
                var deleteGraph = document.getElementById("deleteGraphCheck").checked;


                if(grpTypes == true)
                {
                    var grpTypes=document.getElementById("grptypCheck").value //= document.getElementById("linkCheck").value;
                }
                if(grpCols == true)
                {
                    var grpCols=document.getElementById("grpcolsCheck").value //= document.getElementById("composeCheck").value;
                }
                if(rowsCheck == true)
                {
                    var rowsCheck=document.getElementById("rowsCheck").value //= document.getElementById("topCheck").value;
                }
                if(grpProperties == true)
                {
                    var grpProperties=document.getElementById("grpprpCheck").value //= document.getElementById("snapCheck").value;
                }
                if(addGraphs == true)
                {
                    var addGraphs=document.getElementById("addGraphsCheck").value //= document.getElementById("snapCheck").value;
                }
                if(editGrpName == true)
                {
                    var editGrpName=document.getElementById("editGrpNameCheck").value //= document.getElementById("snapCheck").value;
                }
                if(deleteGraph == true)
                {
                    var deleteGraph=document.getElementById("deleteGraphCheck").value //= document.getElementById("snapCheck").value;
                }
                if(grpTypes == false && grpCols == false && rowsCheck == false && grpProperties == false && addGraphs == false && editGrpName == false && deleteGraph == false)
                {
                    alert('Please Select Atleast One Previlage')
                }else{
                    document.forms.RepGrpPriForm.action="savePrivilages.do?method=saveRepGraphPrevilages&userId="+userId+"&grpTypes="+grpTypes+"&grpCols="+grpCols+"&rowsCheck="+rowsCheck+"&grpProperties="+grpProperties+"&addGraphs="+addGraphs+"&editGrpName="+editGrpName+"&deleteGraph="+deleteGraph;
                    document.forms.RepGrpPriForm.submit();
                    parent.cancelUserGraphPreveliges();
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
            //.out.println("session.getAttribute(UserGraphPrevileges) is "+session.getAttribute("UserGraphPrevileges"));
            ArrayList UserGraphPrevileges = (ArrayList) session.getAttribute("UserGraphPrevileges");
            ////////////////////////////////////////////////.println.println("UserGraphPrevileges is " + UserGraphPrevileges);
            %>
            <table width="100%" border="0px">
                <tr style="width:100%">
                    <td width="50%">
                        <%
            if (UserGraphPrevileges.contains("Graph Types")) {%>
                        <input type="checkbox" id="grptypCheck" value="Graph Types" checked >&nbsp<b style="font-family:Verdana;font-size:12px;color:#369">Graph Types</b>
                        <%} else {%>
                        <input type="checkbox" id="grptypCheck" value="Graph Types">&nbsp<b style="font-family:Verdana;font-size:12px;color:#369">Graph Types</b>
                        <%}%>
                    </td>
                <input type="hidden" name="grptypCheck" id="grptypCheck" >
                <td width="50%">
                    <%
            if (UserGraphPrevileges.contains("Graph Columns")) {%>
                    <input type="checkbox" id="grpcolsCheck" value="Graph Columns" checked >&nbsp<b style="font-family:Verdana;font-size:12px;color:#369">Graph Columns</b>
                    <%} else {%>
                    <input type="checkbox" id="grpcolsCheck" value="Graph Columns">&nbsp<b style="font-family:Verdana;font-size:12px;color:#369">Graph Columns</b>
                    <%  }%>
                </td>
                <input type="hidden" name="grpcolsCheck" id="grpcolsCheck" >
                </tr>
                <tr style="width:100%">
                    <td width="50%">
                        <%
            if (UserGraphPrevileges.contains("Rows")) {%>
                        <input type="checkbox" id="rowsCheck" value="Rows" checked>&nbsp<b style="font-family:Verdana;font-size:12px;color:#369">Rows</b>
                        <%} else {%>
                        <input type="checkbox" id="rowsCheck" value="Rows">&nbsp<b style="font-family:Verdana;font-size:12px;color:#369">Rows</b>
                        <%}%>
                    </td>
                <input type="hidden" name="rowsCheck" id="rowsCheck" >
                <td width="50%">
                    <%
            if (UserGraphPrevileges.contains("Graph Properties")) {%>
                    <input type="checkbox" id="grpprpCheck" value="Graph Properties" checked>&nbsp<b style="font-family:Verdana;font-size:12px;color:#369">Graph Properties</b>
                    <%} else {%>
                    <input type="checkbox" id="grpprpCheck" value="Graph Properties">&nbsp<b style="font-family:Verdana;font-size:12px;color:#369">Graph Properties</b>
                    <%}%>
                </td>
                <input type="hidden" name="grpprpCheck" id="grpprpCheck" >
                </tr>
                <tr style="width:100%">
                    <td width="50%">
                        <%
            if (UserGraphPrevileges.contains("Add Graphs")) {%>
                        <input type="checkbox" id="addGraphsCheck" value="Add Graphs" checked>&nbsp<b style="font-family:Verdana;font-size:12px;color:#369">Add Graphs</b>
                        <%} else {%>
                        <input type="checkbox" id="addGraphsCheck" value="Add Graphs">&nbsp<b style="font-family:Verdana;font-size:12px;color:#369">Add Graphs</b>
                        <%}%>
                    </td>
                <input type="hidden" name="addGraphsCheck" id="addGraphsCheck" >
                <td width="50%">
                    <%
            if (UserGraphPrevileges.contains("Edit Graph Title")) {%>
                    <input type="checkbox" id="editGrpNameCheck" value="Edit Graph Title" checked>&nbsp<b style="font-family:Verdana;font-size:12px;color:#369">Edit Graph Name</b>
                    <%} else {%>
                    <input type="checkbox" id="editGrpNameCheck" value="Edit Graph Title">&nbsp<b style="font-family:Verdana;font-size:12px;color:#369">Edit Graph Name</b>
                    <%}%>
                </td>
                <input type="hidden" name="editGrpNameCheck" id="editGrpNameCheck" >
                </tr>
                <tr style="width:100%">
                    <td width="50%">
                        <%
            if (UserGraphPrevileges.contains("Delete Graph")) {%>
                        <input type="checkbox" id="deleteGraphCheck" value="Delete Graph" checked>&nbsp<b style="font-family:Verdana;font-size:12px;color:#369">Delete Graph</b>
                        <%} else {%>
                        <input type="checkbox" id="deleteGraphCheck" value="Delete Graph">&nbsp<b style="font-family:Verdana;font-size:12px;color:#369">Delete Graph</b>
                        <%}%>
                    </td>
                <input type="hidden" name="deleteGraphCheck" id="deleteGraphCheck" >
                <input type="hidden" name="userId" id="userId" >
                </tr>

                <tr style="width:100%" align="center" >
                <table align="center"  width="100%"><tr >
                        <td width="50%" align="center">
                            <center>
                                <input type="button" class="navtitle-hover"value="Save" onclick="saveRepGrpPriv()">
                                <%--  <input type="button" class="ui-state-default ui-corner-all" value="Cancel" onclick="cancelRepGrpPriv()">--%>
                            </center>
                        </td>
                    </tr></table>
                </tr>
            </table>
        </form>
    </body>
</html>
