<%--
    Document   : MyTable
    Created on : Aug 7, 2009, 7:09:17 PM
    Author     : Administrator
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" import="utils.db.ProgenConnection,java.sql.*,java.util.*"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

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

        <link rel="stylesheet" href="stylesheets/treeviewstyle/jquery.treeview.css" />
        <link rel="stylesheet" href="stylesheets/treeviewstyle/screen.css" />

        <script src="javascript/treeview/jquery.js" type="text/javascript"></script>
        <script src="javascript/treeview/jquery.cookie.js" type="text/javascript"></script>
        <script src="javascript/treeview/jquery.treeview.js" type="text/javascript"></script>

        <script type="text/javascript" src="javascript/treeview/demo.js"></script>

        <script type="text/javascript" src="javascript/draggable/ui.core.js"></script>
        <script type="text/javascript" src="javascript/draggable/ui.draggable.js"></script>

        <script language="JavaScript" src="<%=contextPath%>/querydesigner/JS/jquery.columnfilters.js"></script>


        <link href="StyleSheet.css" rel="stylesheet" type="text/css" />
        <link href="confirm.css" rel="stylesheet" type="text/css" />
        <link href="jquery.contextMenu.css" rel="stylesheet" type="text/css" />

        <script src="jquery.simplemodal-1.1.1.js" type="text/javascript"></script>
        <script src="jquery.contextMenu.js" type="text/javascript"></script>

        <script type="text/javascript">
            $(document).ready(function() {

                $("#addNewCustomerInstructionsImg").click(function(ev) {
                    toggleAddCustomerInstructions();
                });

                $("#addNewCustomerInstructionsLink").click(function(ev) {
                    ev.preventDefault();
                    toggleAddCustomerInstructions();
                });

                $("#addNewCustomerInstructionsClose").click(function(ev) {
                    ev.preventDefault();
                    toggleAddCustomerInstructions();
                });

                $(".customerRow").contextMenu({ menu: 'myMenu' }, function(action, el, pos) { contextMenuWork(action, el, pos); });

                $(".openmenu").contextMenu({ menu: 'connListMenu', leftButton: true }, function(action, el, pos) {

                    contextMenuWork(action, el.parent("tr"), pos); });


                $(".connMenu").contextMenu({ menu: 'tabListMenu', leftButton: true }, function(action, el, pos) {
                    $(el).attr('id')
                    contextMenuWork1(action, el.parent("li"), pos);
                });
/*

                $(".dimMenu").contextMenu({ menu: 'dimListMenu', leftButton: true }, function(action, el, pos) {

                    contextMenuWorkdim(action, el.parent("tr"), pos); });
                $(".memMenu").contextMenu({ menu: 'memberListMenu', leftButton: true }, function(action, el, pos) {

                    contextMenuWorkMember(action, el.parent("li"), pos); });
                //  $(".hierarchyMenu").contextMenu({ menu: 'hierarchyListMenu', leftButton: true }, function(action, el, pos) {

                //      contextMenuWorkhierarchy(action, el.parent("li"), pos); });


                $(".folder").contextMenu({ menu: 'hierarchyListMenu', leftButton: true }, function(action, el, pos) {

                    contextMenuWorkhierarchy(action, el.parent("li"), pos); });

*/

                $("#myList").treeview({
                    animated:"slow",
                    persist: "cookie"
                });


            });
$(document).ready(function() {
                $('table#filterTable2').columnFilters({alternateRowClassNames:['rowa','rowb'], excludeColumns:[0]});

            });
            $(document).ready(function() {
                $('table#filterview2').columnFilters({alternateRowClassNames:['rowa','rowb'], excludeColumns:[0]});

            });
            
        </script>


       
        <style type="text/css">

                                    table tr th {
                                        background-color: #d3DADE;
                                        padding: 3px;
                                    }
                                    table tr.rowb { background-color:#EAf2FD; }

                                    table tr.filterColumns td { padding:2px; }

                                    body { padding-bottom:150px; }




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
                                        top: 15%;
                                        left: 25%;
                                        width: 50%;
                                        height:50%;
                                        padding: 16px;
                                        border: 16px solid #308dbb;
                                        background-color: white;
                                        z-index:1002;


                                    }
                                    .relationTable{
                                        border-top-width: 0px;
                                        border-bottom-width: 0px;
                                        border-right-width: 0px;
                                        border-left-width: 0px;


                                    }
        </style>
    </head>
    <body>
        <%try {%>
        <div id="connection" style="width:700px" class="white_content">
            <form name="myForm1" method="post">
                <input type="hidden" id="dbcode" name="dbcode">
                <br><br><br><br><br>
                <center>
                    <table><tr><td align="center">
                                <table style="width:37%">
                                    <tr>
                                        <td class="myHead" style="width:50%">
                                            Connection Name
                                        </td>
                                        <td  style="width:50%">
                                            <input type="text" name="connectionname" id="connectionname">
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="myHead" style="width:50%">
                                            Database
                                        </td>
                                        <td style="width:50%">
                                            <select id="dbname" onchange="getdatabase()" style="width:146px">
                                                <option value="none">----select----</option>
                                                <option value="oracle">Oracle</option>
                                                <option value="excel">Excel</option>
                                            </select>
                                    </td></tr>
                                </table>
                        </td></tr><tr><td align="center">
                                <div id="oraclediv" style="display:none;width:700px;border-width:thick">
                                    <table style="width:37%">
                                        <tr>
                                            <td class="myHead" style="width:50%">
                                                Username
                                            </td>
                                            <td style="width:50%">
                                                <input type="text" name="username" id="username">
                                            </td>
                                        </tr>
                                        <tr>
                                            <td class="myHead" style="width:50%">
                                                Password
                                            </td>
                                            <td style="width:50%">
                                                <input type="password" name="password" id="password">
                                            </td>
                                        </tr>
                                        <tr>
                                            <td class="myHead" style="width:50%">
                                                Server
                                            </td>
                                            <td style="width:50%">
                                                <input type="text" name="server" value="localhost" id="server">
                                            </td>
                                        </tr>
                                        <tr>
                                            <td class="myHead" style="width:50%">
                                                SId
                                            </td>
                                            <td style="width:50%">
                                                <input type="text" name="Serviceid" id="Serviceid">
                                            </td>
                                        </tr>
                                        <tr>
                                            <td class="myHead" style="width:50%">
                                                Port
                                            </td>
                                            <td style="width:50%">
                                                <input type="text" name="Port" value="1521" id="Port">
                                            </td>
                                        </tr>
                                    </table>
                                </div>
                                <div id="exceldiv" style="display:none;width:700px">
                                    <table style="width:37%">
                                        <!-- <tr>
                                <td class="myHead" style="width:50%">
                                    UserName
                                </td>
                                <td style="width:50%">
                                    <input type="text" name="excelUserName">
                                </td>
                            </tr>
                            <tr>
                                <td class="myHead" style="width:50%">
                                    Password
                                </td>
                                <td style="width:50%">
                                    <input type="text" name="excelPassword">
                                </td>
                        </tr>-->
                                        <tr>
                                            <td class="myHead" style="width:50%">
                                                DataSourceName
                                            </td>
                                            <td style="width:50%">
                                                <input type="text" name="exceldsn" id="exceldsn">
                                            </td>
                                    </tr></table>
                                    <!--  <table style="width:37%"><tr>
                                <td style="width:88%" width="88">
                                     File
                                </td>
                                <td style="width:88%">
                                    <input type="file" name="excelfile">
                                </td>
                        </tr>                        </table>-->
                                </div>
                        </td></tr><tr><td align="center">
                                <table>
                                    <tr>
                                        <td><input type="button" class="btn" value="Test Connection" onclick="testconnection()"></td>
                                        <td><input type="button" class="btn" value="Save Connection" onclick="getconnection()"></td>

                                        <td><input type="button" class="btn" value="Cancel" onclick="cancelFade()"></td>
                                    </tr>
                                </table>

                    </td></tr></table>
                </center>
            </form>
        </div>


        <!-- <a href="javascript:void(0)" onclick="createConnection()" >create connection</a>-->

        <%

    /*         ArrayList a = (ArrayList)request.getAttribute("connectionsList");
    for(int i=0;i<a.size();i++)
    {
    DatabaseConnection db = (DatabaseConnection)a.get(i);
   
    }
     */

    String dbConStatus = "";
    String conNameStatus = "";
    String tablesStatus = "";


        %>

        <form name="myForm2">


            <a href="getAllTables.do">table</a>
            <table style="width:100%;height:80%" border="0">
                <tr>
                    <td width="40%" valign="top">
                        <ul id="browser" class="filetree">
                            <li class="closed" style="background-image:url('images/treeViewImages/plus.gif')"><img src="images/treeViewImages/home.png"><span id="123" class="openmenu">Database Connections</span>
                                <ul>
                                    <logic:notEmpty name="connectionsList">
                                        <logic:iterate id="connectionsList" name="connectionsList" >

                                            <li id="<bean:write name="connectionsList" property="connectionId"/>" class="closed" ><img src="images/treeViewImages/key.png" ><span id="<bean:write name="connectionsList" property="connectionId"/>" class="connMenu"><bean:write name="connectionsList" property="connectionName"/></span>
                                                <ul>
                                                    <li class="closed"><span class="folder">Tables</span>
                                                        <ul>
                                                            <logic:notEmpty name="connectionsList" property="tableList">
                                                                <logic:iterate id="tablelist" name="connectionsList" property="tableList" >
                                                                    <logic:notEqual name="tablelist" property="tableName" value="">
                                                                        <li  class="closed"><img src="images/treeViewImages/database_table.png"><span id="table" onclick="colDelete('<bean:write name="tablelist" property="tableId"/>')">
                                                                            <bean:write name="tablelist" property="tableName"/></span>
                                                                            <ul>
                                                                            </logic:notEqual>
                                                                            <logic:notEqual name="tablelist" property="columnName" value="">
                                                                                <%--  <li><input type="checkbox" name="chk2" checked value="<bean:write name="list" property="columnId"/>"><span><bean:write name="list" property="columnName"/></span></li> --%>

                                                                                <li>
                                                                                    <logic:equal name="tablelist" property="isAvailable" value="Y">
                                                                                        <input type="checkbox" name="chk2" checked value="<bean:write name="tablelist" property="columnId"/>">
                                                                                        <span><bean:write name="tablelist" property="columnName"/></span>
                                                                                    </logic:equal>
                                                                                    <logic:equal name="tablelist" property="isAvailable" value="N">
                                                                                        <input type="checkbox" name="chk2" value="<bean:write name="tablelist" property="columnId"/>">
                                                                                        <span ><bean:write name="tablelist" property="columnName"/></span>
                                                                                    </logic:equal>
                                                                                    <%--    <input type="checkbox" name="chk2" checked value="<bean:write name="list" property="columnId"/>">

                                                                             --%>


                                                                                    <%--     <bean:write name="list" property="isAvailable"/>  --%>


                                                                                    <logic:equal name="tablelist" property="isPk" value="Y">
                                                                                        <img src="images/treeViewImages/key.png" >
                                                                                    </logic:equal>
                                                                                </li>
                                                                            </logic:notEqual>
                                                                            <logic:equal name="tablelist" property="endTable" value="true">
                                                                            </ul>
                                                                        </li>
                                                                    </logic:equal>
                                                                </logic:iterate>
                                                            </logic:notEmpty>
                                                        </ul>
                                                    </li>

                                                    <li class="closed"><span class="folder">Views</span>
                                                        <ul>
                                                            <logic:notEmpty name="connectionsList" property="viewList">
                                                                <logic:iterate id="viewList" name="connectionsList" property="viewList" >
                                                                    <logic:notEqual name="viewList" property="tableName" value="">
                                                                        <li class="closed"><img src="images/treeViewImages/database_table.png"><span id="table" onclick="colDelete('<bean:write name="viewList" property="tableId"/>')">
                                                                            <bean:write name="viewList" property="tableName"/></span>
                                                                            <ul >
                                                                            </logic:notEqual>


                                                                            <logic:notEqual name="viewList" property="columnName" value="">
                                                                                <li>  <input type="checkbox" name="chk2" checked value="<bean:write name="viewList" property="columnId"/>"><span><bean:write name="viewList" property="columnName"/></span></li>
                                                                            </logic:notEqual>


                                                                            <logic:equal name="viewList" property="endTable" value="true">
                                                                            </ul>
                                                                        </li>
                                                                    </logic:equal>
                                                                </logic:iterate>
                                                            </logic:notEmpty>
                                                        </ul>
                                                    </li>
<%--
                                                    <li><img src="images/treeViewImages/home.png"><span id="123" class="dimMenu"><font size="2px">Dimensions</font></span>
                                                        <ul>


                                                            <logic:notEmpty name="connectionsList" property="dimensionList">
                                                                <logic:iterate id="dimensionList" name="connectionsList" property="dimensionList">

                                                                    <li  class="closed" id="<bean:write name="dimensionList" property="dimensionId"/>"><img src="images/treeViewImages/dim.png" ><span><font size="2px"><bean:write name="dimensionList" property="dimensionName"/></font></span>
                                                                        <ul>

                                                                            <li class="closed"><span class="folder"><font size="2px">Tables</font></span>
                                                                                <ul>
                                                                                    <logic:notEmpty name="dimensionList" property="tableList">
                                                                                        <logic:iterate id="list" name="dimensionList" property="tableList" >
                                                                                            <logic:notEqual name="list" property="tableName" value="">
                                                                                                <li  class="closed" id="<bean:write name="list" property="tableId"/>,<bean:write name="list" property="tableName"/>"><img src="images/treeViewImages/database_table.png"><span id="table"   class="memMenu" onclick="dimColDelete('<bean:write name="list" property="tableId"/>')">
                                                                                                    <font size="2px"> <bean:write name="list" property="tableName"/></font></span>
                                                                                                    <ul >
                                                                                                    </logic:notEqual>
                                                                                                    <logic:notEqual name="list" property="columnName" value="">

                                                                                                        <li id="<bean:write name="list" property="columnId"/>,<bean:write name="list" property="columnName"/>,<bean:write name="list" property="isPk"/>">
                                                                                                            <logic:equal name="list" property="isAvailable" value="Y">
                                                                                                                <input type="checkbox" name="chk3" checked value="<bean:write name="list" property="columnId"/>">
                                                                                                                <span><font size="2px"><bean:write name="list" property="columnName"/></font></span>
                                                                                                            </logic:equal>
                                                                                                            <logic:equal name="list" property="isAvailable" value="N">
                                                                                                                <input type="checkbox" name="chk3" value="<bean:write name="list" property="columnId"/>">
                                                                                                                <span ><font size="2px"><bean:write name="list" property="columnName"/></font></span>
                                                                                                            </logic:equal>
                                                                                                            <%--    <input type="checkbox" name="chk2" checked value="<bean:write name="list" property="columnId"/>">

                                                                             --%>


                                                                                    <%--     <bean:write name="list" property="isAvailable"/>  --%>


  <%--                                                                                                          <logic:equal name="list" property="isPk" value="Y">
                                                                                                                <img src="images/treeViewImages/key.png" >
                                                                                                            </logic:equal>
                                                                                                        </li>
                                                                                                    </logic:notEqual>
                                                                                                    <logic:equal name="list" property="endTable" value="true">
                                                                                                    </ul>
                                                                                                </li>
                                                                                            </logic:equal>
                                                                                        </logic:iterate>
                                                                                    </logic:notEmpty>
                                                                                </ul>
                                                                            </li>
                                                                            <li class="closed"><span class="folder"><font size="2px">Members</font></span>
                                                                                <ul>
                                                                                    <logic:notEmpty name="dimensionList" property="membersList">
                                                                                        <logic:iterate id="membersList" name="dimensionList" property="membersList" >
                                                                                            <logic:notEqual name="membersList" property="memberName" value="">
                                                                                                <li  class="closed"><img src="images/treeViewImages/database_table.png"><span id="member">
                                                                                                    <font size="2px"><bean:write name="membersList" property="memberName"/></font></span>
                                                                                                </li>
                                                                                            </logic:notEqual>
                                                                                        </logic:iterate>
                                                                                    </logic:notEmpty>
                                                                                </ul>
                                                                            </li>


                                                                            <li class="closed"><span class="folder" ><font size="2px">Hierarchies</font></span>
                                                                                <ul>
                                                                                    <logic:notEmpty name="dimensionList" property="hierarchyList">
                                                                                        <logic:iterate id="hierarchyList" name="dimensionList" property="hierarchyList" >
                                                                                            <logic:notEqual name="hierarchyList" property="relationName" value="">
                                                                                                <li  class="closed"><img src="images/treeViewImages/database_table.png"><span id="heirarchy">
                                                                                                    <font size="2px"><bean:write name="hierarchyList" property="relationName"/></font></span>
                                                                                                    <ul >
                                                                                                    </logic:notEqual>

                                                                                                    <logic:notEqual name="hierarchyList" property="relColumnName" value="">
                                                                                                        <li><span><font size="2px">
                                                                                                        <bean:write name="hierarchyList" property="relColumnName"/></font></span></li>

                                                                                                    </logic:notEqual>


                                                                                                    <logic:equal name="hierarchyList" property="endTable" value="true">

                                                                                                    </ul>
                                                                                                </li>
                                                                                            </logic:equal>

                                                                                        </logic:iterate>
                                                                                    </logic:notEmpty>
                                                                                </ul>
                                                                            </li>
                                                                        </ul>
                                                                    </li>
                                                                </logic:iterate>
                                                            </logic:notEmpty>
                                                    </ul>   </li>

--%>




                                                </ul>
                                            </li>
                                        </logic:iterate>
                                    </logic:notEmpty>
                                </ul>
                            </li>
                        </ul>
                    </td>
                    <div style="display:none">    <td width="40%" valign="top">
                            <iframe  id="dataDisptab" NAME='dataDisptab'  class="white_content" style="height:1000px" SRC='about:blank'></iframe>
                    </td></div>
            <%--        <div style="display:none"><td width="40%" valign="top">
                            <iframe  id="dataDispdim" NAME='dataDispdim' style="width:700px" class="white_content" SRC='#'></iframe>
                    </td></div>
                    <div style="display:none"><td width="40%" valign="top">
                            <iframe  id="hiedataDisp" NAME='hiedataDisp'  style="width:700px" class="white_content" SRC='#'></iframe>
                    </td></div>

--%>
                </tr>
            </table>
            <input type="hidden" name="tabledeleteId" id="tabledeleteId" >
            <input type="hidden" name="dimtableId" id="dimtableId" >
        </form>

        <input type="hidden" name="h" id="h" value="<%=request.getContextPath()%>">
        <div id="fade" class="black_overlay"></div>
 <%--       <ul id="dimListMenu" class="contextMenu" style="width:150px;text-align:left">

            <li class="insert"><a href="#createDim">Create Dimension</a></li>
            <li class="insert"><a href="#deleteDim">Delete Dimension</a></li>

        </ul>
        <ul id="memberListMenu" class="contextMenu" style="width:150px;text-align:left">

            <li class="insert"><a href="#addMember">Add Member Details</a></li>
            <li class="insert"><a href="#combinedKey">Define Combined key</a></li>

        </ul>
        <ul id="hierarchyListMenu" class="contextMenu" style="width:150px;text-align:left">

            <li class="insert"><a href="#addHierarchy">Create Hierarchy</a></li>

        </ul>
 --%>

        <ul id="connListMenu" class="contextMenu">

            <li class="insert"><a href="#insert">Create New Connection</a></li>

        </ul>

        <ul id="tabListMenu" class="contextMenu">

            <li class="insert"><a href="#addTables">Add Tables</a></li>

        </ul>
        <%} catch (Exception e) {
            e.printStackTrace();
        }%>
         <script type="text/javascript">
            $(function() {
                $("#abcd").draggable();
            });
        </script>
        <script>
            
            function contextMenuWork(action, el, pos) {

                switch (action) {
                    case "delete":
                        {
                            var msg = "Delete " + $(el).find("#contactname").text() + "?";
                            $("#HiddenFieldRowId").val($(el).find("#customerid").text());
                            confirm(msg);
                            break;
                        }
                    case "insert":
                        {
                            /*  $("#TextBoxContactName").val("");
                        $("#TextBoxContactTitle").val("");
                        $("#TextBoxCountry").val("");
                        $("#TextBoxPhone").val("");

                        $("#addNewCustomer").modal({
                            close: true,
                            onOpen: modalOpenAddCustomer,
                            onClose: modalOnClose,
                            persist: true,
                            containerCss: ({ width: "500px", height: "275px", marginLeft: "-250px" })
                        });*/

                            createConnection();
                            break;
                        }

                    case "edit":
                        {
                            alert(
                            'Action: ' + action + '\n\n' +
                                'Element ID: ' + $(el).attr('id') + '\n\n' +
                                'X: ' + pos.x + '  Y: ' + pos.y + ' (relative to element)\n\n' +
                                'X: ' + pos.docX + '  Y: ' + pos.docY + ' (relative to document)'
                        );
                        }
                }
            }

            function contextMenuWork1(action, el, pos) {

                switch (action) {
                    case "delete":
                        {
                            var msg = "Delete " + $(el).find("#contactname").text() + "?";
                            $("#HiddenFieldRowId").val($(el).find("#customerid").text());
                            confirm(msg);
                            break;
                        }
                    case "addTables":
                        {

                            //   alert($(el).attr('id'));

                            /*  $("#TextBoxContactName").val("");
                        $("#TextBoxContactTitle").val("");
                        $("#TextBoxCountry").val("");
                        $("#TextBoxPhone").val("");

                        $("#addNewCustomer").modal({
                            close: true,
                            onOpen: modalOpenAddCustomer,
                            onClose: modalOnClose,
                            persist: true,
                            containerCss: ({ width: "500px", height: "275px", marginLeft: "-250px" })
                        });*/

                            tableList($(el).attr('id'));
                            break;
                        }

                    case "edit":
                        {
                            alert(
                            'Action: ' + action + '\n\n' +
                                'Element ID: ' + $(el).attr('id') + '\n\n' +
                                'X: ' + pos.x + '  Y: ' + pos.y + ' (relative to element)\n\n' +
                                'X: ' + pos.docX + '  Y: ' + pos.docY + ' (relative to document)'
                        );
                        }
                }
            }

         /*   function contextMenuWorkdim(action, el, pos) {

                switch (action) {
                    case "deleteDim":
                        {
                            var msg = "Delete " + $(el).find("#contactname").text() + "?";
                            $("#HiddenFieldRowId").val($(el).find("#customerid").text());
                            confirm(msg);
                            break;
                        }
                    case "createDim":
                        {
                            createDimension();
                            break;
                        }

                    case "edit":
                        {
                            alert(
                            'Action: ' + action + '\n\n' +
                                'Element ID: ' + $(el).attr('id') + '\n\n' +
                                'X: ' + pos.x + '  Y: ' + pos.y + ' (relative to element)\n\n' +
                                'X: ' + pos.docX + '  Y: ' + pos.docY + ' (relative to document)'
                        );
                        }
                }
            }


            function contextMenuWorkhierarchy(action, el, pos) {

                switch (action) {
                    case "deleteMember":
                        {
                            var msg = "Delete " + $(el).find("#contactname").text() + "?";
                            $("#HiddenFieldRowId").val($(el).find("#customerid").text());
                            confirm(msg);
                            break;
                        }
                    case "addHierarchy":
                        {

                            var dimId=$(el).parent().parent().attr('id');
                            alert(dimId);
                            createHierarchy(dimId);
                            break;
                        }

                    case "edit":
                        {
                            alert(
                            'Action: ' + action + '\n\n' +
                                'Element ID: ' + $(el).attr('id') + '\n\n' +
                                'X: ' + pos.x + '  Y: ' + pos.y + ' (relative to element)\n\n' +
                                'X: ' + pos.docX + '  Y: ' + pos.docY + ' (relative to document)'
                        );
                        }
                }
            }
            function contextMenuWorkMember(action, el, pos) {

                switch (action) {
                    case "deleteMember":
                        {
                            var msg = "Delete " + $(el).find("#contactname").text() + "?";
                            $("#HiddenFieldRowId").val($(el).find("#customerid").text());
                            confirm(msg);
                            break;
                        }
                    case "addMember":
                        {

                            var colId=$(el).attr('id');
                            alert(colId);
                            createMember(colId);
                            break;
                        }

                    case "edit":
                        {
                            alert(
                            'Action: ' + action + '\n\n' +
                                'Element ID: ' + $(el).attr('id') + '\n\n' +
                                'X: ' + pos.x + '  Y: ' + pos.y + ' (relative to element)\n\n' +
                                'X: ' + pos.docX + '  Y: ' + pos.docY + ' (relative to document)'
                        );
                        }
                }
            }


*/

            function confirm(message) {
                $("#confirm").modal({
                    close: true,
                    overlayId: 'confirmModalOverlay',
                    containerId: 'confirmModalContainer',
                    onClose: modalOnClose,
                    onShow: function modalShow(dialog) {
                        dialog.overlay.fadeIn('slow', function() {
                            dialog.container.fadeIn('fast', function() {
                                dialog.data.hide().slideDown('slow');
                            });
                        });

                        dialog.data.find(".confirmmessage").append(message);

                        // Yes button clicked
                        dialog.data.find("#ButtonYes").click(function(ev) {
                            ev.preventDefault();
                            $.modal.close();
                            alert('The customer with id ' + $("#HiddenFieldRowId").val() + ' would of been deleted.');
                            //$("#ButtonDeleteCustomer").click();
                        });
                    }
                })
            }

            function modalOpenAddCustomer(dialog) {
                dialog.overlay.fadeIn('fast', function() {
                    dialog.container.fadeIn('fast', function() {
                        dialog.data.hide().slideDown('slow');
                    });
                });

                dialog.data.find(".modalheader span").html("Add New Customer");

                // if the user clicks "yes"
                dialog.data.find("#ButtonAddCustomer").click(function(ev) {
                    ev.preventDefault();

                    //Perfom validation
                    if (Page_ClientValidate("addCustomer")) {
                        $.modal.close();
                        $("#ButtonHiddenAddCustomer").click();
                    }

                });
            }

            function toggleAddCustomerInstructions() {
                $("#addNewCustomerFields").toggle();
                $("#addNewCustomerInstructions").toggle()
            }

            function modalOnClose(dialog) {
                dialog.data.fadeOut('slow', function() {
                    dialog.container.slideUp('slow', function() {
                        dialog.overlay.fadeOut('slow', function() {
                            $.modal.close(); // must call this to have SimpleModal
                            // re-insert the data correctly and
                            // clean up the dialog elements
                        });
                    });
                });
            }

           function tableList(span){
                //document.getElementById("activeConnection").value=(span.innerHTML);
                // document.getElementById('type').style.display='block';
                // document.getElementById('fade').style.display='block';
                //
                 var frameObj = document.getElementById('dataDisptab');
                //alert(document.getElementById("conList").innerHTML);
                //window.open("TableList.jsp?connection="+span, "window.optablelist", "status=1,width=350,height=375");
                 var source="TableList.jsp?connection="+span;
                frameObj.src=source;
                document.getElementById('dataDisptab').style.display='block';
                //
                //
                //alert('list');
                //document.getElementById("type").style.display='';
            }

              function getData(tableIds){
                var frameObj=document.getElementById("dataDisp");
                var source="pbViewTable.jsp?tableIds="+tableIds;
                frameObj.src=source;
            }
            function TableList1(span){
                document.getElementById("activeConnection").value=(span.innerHTML);
            }


            function createConnection(){

                document.getElementById('connection').style.display='block';
                document.getElementById('fade').style.display='block';
                //alert('list');
                //document.getElementById("type").style.display='';
            }

            function getTableSet()
            {
                // alert(document.getElementById('tvtype').value);
                if(document.getElementById('tvtype').value=="Tables")
                {
                    document.getElementById('tableList').style.display='';
                    document.getElementById('viewList').style.display='none';
                }

                if(document.getElementById('tvtype').value=="Views")
                {
                    document.getElementById('tableList').style.display='none';
                    document.getElementById('viewList').style.display='';
                }
                if(document.getElementById('tvtype').value=="none")
                {
                    document.getElementById('tableList').style.display='none';
                    document.getElementById('viewList').style.display='none';
                }

            }
             function saveTables(type)
            { // alert('save tables'+type);
                document.myForm.action="pbSaveTables.jsp";
                //alert( document.myForm.action);
                document.myForm.submit();
            }
            function temp(){
                // alert('Hello');
            }

        </script>

        <script>
            function getconnection()
            {
                document.myForm1.action="querydesigner/JSPS/pbCheckConnection.jsp";
                document.myForm1.submit();
            }

            function getdatabase()
            {
                // alert(document.getElementById('dbname').value);
                if(document.getElementById('dbname').value=="oracle")
                {
                    document.getElementById('oraclediv').style.display='';
                    document.getElementById('exceldiv').style.display='none';
                    document.getElementById('dbcode').value = '1';
                    //  alert("dbcode-->"+document.getElementById('dbcode').value);
                }

                if(document.getElementById('dbname').value=="excel")
                {
                    document.getElementById('oraclediv').style.display='none';
                    document.getElementById('exceldiv').style.display='';
                    document.getElementById('dbcode').value = '2';
                    //  alert("dbcode-->"+document.getElementById('dbcode').value);
                }
                if(document.getElementById('dbname').value=="none")
                {
                    document.getElementById('oraclediv').style.display='none';
                    document.getElementById('exceldiv').style.display='none';
                }

            }

            function cancelFade()
            {
                document.getElementById('fade').style.display='none';
                document.getElementById('type').style.display='none';
                document.getElementById('connection').style.display='none';
            }


            function testconnection()
            {
                var un = document.getElementById("username").value;
                var pwd = document.getElementById("password").value;
                var s = document.getElementById("server").value;
                var sid = document.getElementById("Serviceid").value;
                var p = document.getElementById("Port").value;
                var dbname = document.getElementById("dbname").value;
                var dsn = document.getElementById("exceldsn").value;

                xmlHttp=GetXmlHttpObject();
                if (xmlHttp==null)
                {
                    alert ("Your browser does not support AJAX!");
                    return;
                }
                ctxPath=document.getElementById("h").value;
                if(dbname=='oracle'){
                    var url=ctxPath+"/TestConnection";
                    url=url+"?un="+un+"&pwd="+pwd+"&s="+s+"&sid="+sid+"&p="+p;
                }
                else if(dbname=='excel')
                {
                    var url=ctxPath+"/TestExcelConnection";
                    url=url+"?dsn="+dsn;

                }
                // alert(url)
                // var payload = "q="+str+"&id="+id;
                //alert(url);
                xmlHttp.onreadystatechange=stateChanged;
                xmlHttp.open("GET",url,true);
                xmlHttp.send(null);


            }

            function GetXmlHttpObject()
            {
                var xmlHttp=null;
                try
                {
                    // Firefox, Opera 8.0+, Safari
                    xmlHttp=new XMLHttpRequest();
                }
                catch (e)
                {
                    // Internet Explorer
                    try
                    {
                        xmlHttp=new ActiveXObject("Msxml2.XMLHTTP");
                    }
                    catch (e)
                    {
                        xmlHttp=new ActiveXObject("Microsoft.XMLHTTP");
                    }
                }
                return xmlHttp;
            }

            function stateChanged()
            {
                if (xmlHttp.readyState==4)
                {

                    var output1=xmlHttp.responseText
                    alert(output1);


                }
            }

            function saveNext()
            {
                document.getElementById('connection').style.display='none';
                document.getElementById('fade').style.display='none';
                var connectionname = document.getElementById('connectionname').value;
                var dbc = document.getElementById('dbcode').value;
                var un = document.getElementById('username').value;
                var pwd = document.getElementById('password').value;
                var ser = document.getElementById('server').value;
                var sid = document.getElementById('Serviceid').value;
                var port = document.getElementById('Port').value;
                window.open("pbSaveNextCheck.jsp?dbcode="+dbc+"&username="+un+"&password="+pwd+"&server="+ser+"&Serviceid="+sid+"&Port="+port+"&connectionname="+connectionname, "window.optablelist", "status=1,width=350,height=375");
                //document.myForm1.action="<%=request.getContextPath()%>/pbSaveNextCheck.jsp";
                //document.myForm1.submit();
            }
            //added by bharu on 19/08/09
            function colDelete(tableId){
                alert(tableId);
                document.getElementById("tabledeleteId").value=tableId;
                var i=0;
                var obj=document.myForm2.chk2;
                // alert(obj.length)
                for(var j=0;j<obj.length;j++)
                {
                    if(document.myForm2.chk2[j].checked==true)
                    {

                        i++;
                    }
                }

                //alert(i)
                if(i>0)
                {

                    //alert('Unselected Columns Are Deleted');
                    document.myForm2.action="pbDeleteTableColumns.jsp";
                    // alert("url==="+document.myForm2.action);
                    document.myForm2.submit();
                }
                else
                {

                    alert('Unselected Columns Are Deleted please select atleast one column');
                    //document.myForm2.action="pbDeleteTableColumns.jsp?tableId="+tableId;
                    // alert("url==="+document.myForm2.action);
                    // document.myForm2.submit();

                }
            }
         /*   function createHierarchy(dimId){
                // window.open("createHierarchy.jsp?dimId="+dimId, "window.createHierarchy", "status=1,width=350,height=375");
                //showhierarchy(dimId);

                var f=document.getElementById('hiedataDisp');
                var s="createHierarchy.jsp?dimId="+dimId;
                f.src=s;
                document.getElementById('hiedataDisp').style.display='block';
                //document.getElementById('fade').style.display='block';
            }
            function createMember(colId){
                //window.open("createMember.jsp?colId="+colId, "window.createMember", "status=1,width=350,height=375");

                //var f.src=source;
                var f=document.getElementById('dataDispdim');
                var s="createMember.jsp?colId="+colId;
                f.src=s;
                document.getElementById('dataDispdim').style.display='block';
                // document.getElementById('fade').style.display='block';


            }





            function dimColDelete(dimtableId){
                //alert(tableId);
                document.getElementById("dimtableId").value=dimtableId;
                var i=0;
                var obj=document.myForm2.chk3;
                // alert(obj.length)
                var reqchk='';
                for(var j=0;j<obj.length;j++)
                {
                    if(document.myForm2.chk3[j].checked==true)
                    {
                        var sp=document.myForm2.chk3[j].value.split(',');
                        reqchk+="&"+"chk3="+sp[0]+"%2C"+sp[1];
                        i++;
                    }
                }
                reqchk=reqchk.substring(1);
                // alert( reqchk);
                if(i>0)
                {

                    //alert('Unselected Columns Are Deleted'+document.getElementById("chk2"));
                    // document.myForm2.action="deleteDimensionColumns.jsp?tableId="+tableId+reqchk;
                    document.myForm2.action="deleteDimensionColumns.jsp";
                    // alert("url==="+document.myForm2.action);
                    document.myForm2.submit();
                }
                else
                {

                    alert('Unselected Columns Are Deleted please select atleast one column');
                    //document.myForm2.action="pbDeleteTableColumns.jsp?tableId="+tableId;
                    // alert("url==="+document.myForm2.action);
                    // document.myForm2.submit();

                }
            }

            var xmlHttp2;

            function showhierarchy(str)
            {
                alert(str);

                if (str.length==0)
                {
                    document.getElementById("txtHint").innerHTML="";

                    return;
                }
                xmlHttp2=GetXmlHttpObject();
                if (xmlHttp2==null)
                {
                    alert ("Your browser does not support AJAX!");
                    return;
                }
                ctxPath=document.getElementById("h").value;

                var url=ctxPath+"/CreateHierarchy";
                url=url+"?dimId="+str;
                alert(url)
                // var payload = "q="+str+"&id="+id;
                //alert('target url is---'+url);
                xmlHttp2.onreadystatechange=stateChangedhierarchy;
                xmlHttp2.open("GET",url,true);
                xmlHttp2.send(null);
            }


            function stateChangedhierarchy()
            {
                //  alert('hi in target')

                if (xmlHttp2.readyState==4)
                {
                    var output=xmlHttp2.responseText;
                    alert("output is "+output);
                    var members=output.split("\n");
                    var rslength=members.length-1;
                    alert(rslength);

                    var str;
                    str=str+"<tr>"
                    str=str+"<td>"
                    str=str+"state";
                    str=str+"</td>";
                    str=str+"<td>";
                    str=str+"level1";
                    str=str+"</td>";
                    str=str+"</tr>"
                    //  alert(document.getElementById("hierarchytable").a)
                    document.getElementById("hierarchytable").innerHTML=document.getElementById("hierarchytable").innerHTML+str;
                    alert(document.getElementById("hierarchytable").innerHTML);
                    document.getElementById("hierarchydiv").style.display='';
                }

            }

            function GetXmlHttpObject()
            {
                var xmlHttp=null;
                try
                {
                    // Firefox, Opera 8.0+, Safari
                    xmlHttp=new XMLHttpRequest();
                }
                catch (e)
                {
                    // Internet Explorer
                    try
                    {
                        xmlHttp=new ActiveXObject("Msxml2.XMLHTTP");
                    }
                    catch (e)
                    {
                        xmlHttp=new ActiveXObject("Microsoft.XMLHTTP");
                    }
                }
                return xmlHttp;
            }
*/
            function refreshparent()
            {
                document.myForm2.action="getAllTables.do";
                //document.myForm2.submit();
            }
        </script>
    </body>
</html>
