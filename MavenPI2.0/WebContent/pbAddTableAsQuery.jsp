<%-- 
    Document   : pbAddTableAsQuery
    Created on : Nov 16, 2009, 7:10:45 PM
    Author     : Administrator
--%>

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
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

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



        <title>Add Table As Query</title>
        <style>
            .label{
                font-family:verdana;
                font-size:12px;
                font-weight:normal;
                color:black;
            }

        </style>
        
    </head>
    <%
            String connectionId = request.getParameter("connectionId");
            ////////////////////////////////////////////////////////////////////////////////.println.println(" connectionId --. " + connectionId);
            //request.setAttribute("connectionId", String.valueOf(request.getAttribute("connectionId")));
            //request.setAttribute("tableName", String.valueOf(request.getAttribute("tableName")));
            //request.setAttribute("query", String.valueOf(request.getAttribute("query")));
%>
    <body onload='document.getElementById("tableName").focus()'><center>
            <h3>Add Table As Query</h3>
            <form name="dispForm1" method="post">
                <div>
                    <table>
                        <tr>
                            <td>
                                Table Name
                            </td>
                            <td>
                                <Input type="text" id="tableName" MAXLENGTH="32" name="tableName" >
                            </td>
                        
                        </tr>
                        <tr>
                            <td>
                                Query
                            </td>
                            <td>
                                <input type="text" id="query" name="query">
                            </td>
                        </tr>
                    </table>
                    <center><br><br>
                        <Table>
                            <Tr>
                                <Td><input type="button" value="Save" onclick="return addSave()"></Td>
                                <Td><input type="button" value="Cancel" onclick="cancelAddTable()" ></Td>
                            </Tr>

                        </Table>
                    </center>
                </div>
                  <Input type="HIDDEN" name="connectionId" id="connectionId" value="<%=connectionId%>">

            </form></center>
<script language="javascript">

            function cancelAddTable(){
                parent.refreshAddQTable();
            }

            function addSave(){
                var connectionId = document.getElementById("connectionId");
                var query = document.getElementById("query");
                var tableName = document.getElementById("tableName");
                var tname =tableName.value;
                var y=' ';
                for(var i=0;i<=tname.length;i++)
                {
                    if(y == tname[i] )
                    {
                        alert("It's contains spaces");
                        return false;
                    }document.getElementById("tableName").focus();
                }
                if(tableName.value=="" )
                {
                    alert("Enter Table Name");
                    document.getElementById("tableName").focus();
                    return false;
                }
                else {
                    document.forms.dispForm1.action='editconn.do?parameter=addTableAsQuery&connectionId='+connectionId.value+'&query='+query.value+'&tableName='+tableName.value
                    document.forms.dispForm1.submit();


                   
                    /*  $.ajax({
                        url: 'editconn.do?parameter=addTableAsQuery&connectionId='+connectionId.value+'&query='+query.value+'&tableName='+tableName.value,
                        success: function(data){
                            if(data==1)
                                alert("Table already Exists.")
                            else if(data==2){
                                alert("Wrong Query");
                            }
                          else
                            {
                                alert("Data Saved");

                                //parent.refreshAddQTableCols(data);
                                parent.document.getElementById("tableQuery").innerHTML=data;
                                //document.getElementById("tableQuery").innerHTML=data;
                                // var FrameObj=document.getElementById("tabDiv");
                                // FrameObj.src="pbAddTableAsQueryViewTable.jsp?data="+data;

                            }
                        }
                    }); */

                    // parent.refreshAddQTable();
                    return true;
                }

            }


        </script>
    </body>
</html>
