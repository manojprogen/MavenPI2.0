<%--
    Document   : MyTable
    Created on : Aug 7, 2009, 7:09:17 PM
    Author     : Administrator
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%//for clearing cache
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>

        <link rel="stylesheet" href="stylesheets/treeviewstyle/jquery.treeview.css" />
        <link rel="stylesheet" href="stylesheets/treeviewstyle/screen.css" />
        <link type="text/css" href="stylesheets/ui.all.css" rel="stylesheet" />
        <link type="text/css" href="stylesheets/demos.css" rel="stylesheet" />
        <link type="text/css" href="stylesheets/jquery.contextMenu.css" rel="stylesheet" />

        <script src="javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <script src="javascript/treeview/jquery.cookie.js" type="text/javascript"></script>
        <script src="javascript/treeview/jquery.treeview.js" type="text/javascript"></script>

        <script type="text/javascript" src="javascript/treeview/demo.js"></script>

        <script type="text/javascript" src="javascript/draggable/ui.core.js"></script>
        <script type="text/javascript" src="javascript/draggable/ui.draggable.js"></script>
        <script type="text/javascript" src="javascript/draggable/ui.droppable.js"></script>
        <script type="text/javascript" src="javascript/ui.resizable.js"></script>
        <script type="text/javascript" src="javascript/ui.accordion.js"></script>
        <script type="text/javascript" src="javascript/ui.dialog.js"></script>
        <script type="text/javascript" src="javascript/ui.sortable.js"></script>
        <script type="text/javascript" src="javascript/BusinessGroup.js"></script>
        <script type="text/javascript" src="javascript/effects.core.js"></script>
        <script type="text/javascript" src="javascript/effects.explode.js"></script>
        <script type="text/javascript" src="javascript/jquery.bgiframe.js"></script>
        <script type="text/javascript" src="javascript/effects.highlight.js"></script>
        <script type="text/javascript" src="javascript/jquery.contextMenu.js"></script>

        <style>
            .myTableClass{
                visibility:hidden;
                display:none;
            }
            .myDraggedDiv{
                visibility:hidden;
                display:none;
            }
            .draggedDivs{



            }
            .mainTableCell{
                border:solid black 0.5px;
            }

            .hideTableCell{
                border:solid black 0.5px;
                visibility:hidden;
                display:none;
            }
            .myTextCell{
                border:solid black 0.5px;
                width:150px;
            }
            .draggedTable{
                height:100%;
                width:250px;
                border:solid black 0.5px;

            }
            .draggedColumns{
                height:200px;
                min-width:900px;
                border:solid black 0.5px;

            }
            .myClassTest{
                height:50%;
                max-height:50%;
                min-width:900px;
                border:solid black 0.5px;

            }
            .myClassTest1{
                position:absolute;
                height:43%;
                max-height:43%;
                min-width:900px;
                overflow:auto;

            }
            .savedRelations{
                border:solid black 0.5px;

            }
            #accordion{
                height:95%;

            }

        </style>
        <script>
            $(function() {
                $("#businessTree").treeview({
                    animated: "normal",
                    unique:true
                });
                $("#bussTableTree").treeview({
                    animated: "normal",
                    unique:true
                });
                $("#bussRelatedTableTree").treeview({
                    animated: "normal",
                    unique:true
                });
            });
        </script>
    </head>
    <body>
        <a href="javascript:check()">check</a>
        <table style="width:100%;height:100%">

            <tr>
                <td class="mainTableCell" width="25%" valign="top">
                    <div style="height:33px" class="ui-state-default draggedDivs ui-corner-all">
                        <b>BUSINESS GROUP</b><table>
                            <tr><td valign="top">
                                    <a class="ui-icon ui-icon-plus" title="Add Relations" href="javascript:addBussinessGroup()"></a></td>
                            </tr></table>
                    </div>
                    <ul id="businessTree">
                        <li class="" style="background-image:url('images/treeViewImages/plus.gif')"><img src="images/treeViewImages/cabin_with_smog_pipe.png"><span class="groupMenu"  id="123">Business Group</span>
                            <ul id="bussGrps">
                            </ul>

                        </li>
                    </ul>
                </td>

                <td id="myDbTableList" class="hideTableCell" width="25%" valign="top">
                    <ul id="bussTableTree" class="filetree">
                        <li style="background-image:url('images/treeViewImages/plus.gif')"><img src="images/treeViewImages/database.png"><span  id="123">TABLES</span>
                            <ul id="bussTable">

                                <logic:notEmpty name="list" scope="request" >
                                    <logic:iterate id="list" name="list" scope="request">
                                        <logic:notEqual name="list" property="tableName" value="">
                                            <li class="closed"><img src="images/treeViewImages/database_table.png"><span id='tableId-<bean:write name="list" property="tableId"/>'><bean:write name="list" property="tableName"/></span>
                                                <ul id='<bean:write name="list" property="tableName"/>'>


                                                </logic:notEqual>
                                                <logic:notEqual name="list" property="columnName" value="">
                                                    <li id="columns"><img id='colId-<bean:write name="list" property="columnId"/>' src="images/treeViewImages/bullet_star.png"><span><bean:write name="list" property="columnName"/></span></li>
                                                    </logic:notEqual>
                                                    <logic:equal  name="list" property="endTable" value="true">
                                                </ul>
                                            </li>
                                        </logic:equal>
                                    </logic:iterate>

                                </logic:notEmpty>

                            </ul>
                        </li>
                    </ul>

                </td>
                <td>

                </td>
                <td class="hideTableCell" id="draggableTables"   width="25%" valign="top">

                    <div style="height:33px" class="ui-state-default draggedDivs ui-corner-all">
                        <b>Drag Tables to Add Facts</b><table>
                            <tr><td valign="top">
                                    <a class="ui-icon ui-icon-check" title="Add Relations" href="javascript:addBussTables()"></a></td>
                                <td valign="top">
                                    <a class="ui-icon ui-icon-closethick" title="Clear All" href="javascript:delTables()"></a>
                                </td></tr></table>
                    </div><div id="divDraggableTables"></div>

                </td>
                <td class="hideTableCell" id="relDBTables" valign="top">

                    <div style="height:30px" class="ui-state-default draggedDivs ui-corner-all">
                        <b>Related Tables</b> </div>

                    <ul id="bussRelatedTableTree" class="filetree">
                        <li style="background-image:url('images/treeViewImages/plus.gif')"><img src="images/treeViewImages/database.png"><span  id="123">Related Tables</span>
                            <ul id="bussRelatedTable">

                            </ul>
                        </li>
                    </ul>
                </td>
            </tr>
        </table>
        <script>
            var tableActive;
            var $dragTables=$('#bussTable > li >span')
            var $draggableTables=$('#draggableTables');

            $($dragTables).draggable({
                helper:"clone",
                effect:["", "fade"]
            });


            $($draggableTables).droppable(
            {
                activeClass:"blueBorder",
                accept:'#bussTable > li >span,#bussRelatedTable > li > ul > li >span',
                drop: function(ev, ui) {
                    /*   for(a in ui.draggable){
                    alert(ui.draggable.html());
                    alert(a)
                }*/
                    //$draggableTables.html(ui.draggable.html());


                    var tab=ui.draggable.html();
                    if(bussTableNamesDrag.match(tab)==null){
                        count++;

                        // bussTabIdArray.length=count;
                        var idTab=ui.draggable.attr("id").split("-");
                        if(bussTabDetailsArray[tab]==undefined){


                            bussTabDetailsArray[tab]=true;
                        }
                        else{
                            bussTabDetailsArray[tab]=false;
                        }
                        bussTabIdArray.push(idTab[1]);
                        bussTabNameArray.push(tab)
                        bussTableNamesDrag=bussTabNameArray.toString()

                        getBussTableDetails(ui.draggable.html(),idTab[1]);
                    }
                    else
                        $('#warning').dialog('open');
                    alert("!!!1111")
                    $("#alert").html("Table is already Added")
                }

            }

        );

            $col=$("#draggedCols");
            $($col).droppable(
            {
                activeClass:"blueBorder",
                accept:".tableColumns",
                drop: function(ev, ui) {
                    var x=document.getElementById("draggedCols");
                    // x.innerHTML=x.innerHTML+ui.draggable.attr("id");
                    //x.innerHTML=x.innerHTML+ui.draggable.html();
                    buildRelationTable(ui);
                }

            }

        );

        </script>


        <div id="dialog" title="Empty Drag Tables?">
            <p><span class="ui-icon ui-icon-alert" style="float:left; margin:0 7px 20px 0;"></span>All the items will be cleared. Are you sure?</p>
        </div>
        <!-- <div id="successMsg" title="Operation Success">
            <p>
                <span  class="ui-icon ui-icon-circle-check" style="float:left; margin:0 7px 50px 0;"></span>
                Relations Saved Successfully.
            </p>

        </div>
        <div id="failureMsg" title="Operation Failure">
            <p>
                <span  class="ui-icon ui-icon-alert" style="float:left; margin:0 7px 50px 0;"></span>
                Relations Cant Be saved.
            </p>

        </div>-->
        <div id="warning" title="Message">
            <p>
                <span  class="ui-icon ui-icon-alert" style="float:left; margin:0 7px 50px 0;"></span>
            <div id="alert"></div>
        </p>

    </div>

    <div id="createGroup" title="Create new user">

        <form>
            <table cellpadding="5">
                <tr><td>
                        <label for="name">Group Name</label></td>
                    <td><input type="text" name="grpName" id="grpName" class="text ui-widget-content ui-corner-all" /></td></tr>

                <tr><td>
                        <label for="email">Group Description</label></td><td>
                        <textarea name="grpDesc" id="grpDesc" class="textarea ui-widget-content ui-corner-all" ></textarea></td></tr>
            </table>

        </form>

    </div>
    <!--
    <div id="removeTable" title="Remove The Table?">
        <p><span class="ui-icon ui-icon-alert" style="float:left; margin:0 7px 20px 0;"></span>The table will be removed from the list. Are you sure?</p>
    </div>
    -->

    <!--Right Click Context Menu-->
    <ul id="groupListMenu" class="contextMenu" style="width:150px;text-align:left">

        <li class="insert"><a href="#createGroup">Create Business Group</a></li>
        <li class="insert"><a href="#deleteGroup">Delete Business Group</a></li>

    </ul>
    <ul id="groupTableMenu" class="contextMenu" style="width:150px;text-align:left">

        <li class="insert"><a href="#addTables">Add Tables</a></li>
    </ul>

</body>

</html>
