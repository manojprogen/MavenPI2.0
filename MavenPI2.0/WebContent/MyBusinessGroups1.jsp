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
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>

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
        <script type="text/javascript" src="javascript/BusinessGroup.js"></script>

        <script type="text/javascript">
            var bustableId="";

    
            var grpId="";
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

                $(".openmenubusgrp").contextMenu(
                { menu: 'myMenubusgrp', leftButton: true
                }, function(action, el, pos) {
                    bustableId=$(el).parent().attr('id');
                    contextMenuWork(action, el.parent("li"), pos);

                });

                $(".bucketMenu").contextMenu({ menu: 'grpbucket', leftButton: true }, function(action, el, pos) {
                    contextMenubucket(action,el.parent("li"), pos);
                });


                $(".createFolderMenu").contextMenu({ menu: 'userFolder', leftButton: true }, function(action, el, pos) {
                    contextMenuFolder(action,el.parent("li"), pos);
                }); 
                $("#myList2").treeview({
                    animated:"slow",
                    persist: "cookie"
                });

                $("#createUserFolder").dialog({
                    bgiframe: true,
                    autoOpen: false,
                    height:250,
                    width:300,
                    modal: true,
                    buttons: {
                        'Create User Folder': function() {
                            createUserFolder();

                        },
                        Cancel: function() {
                            $(this).dialog('close');
                        }
                    },
                    close: function() {

                    }
                });

                $("#assignToUser").dialog({
                    bgiframe: true,
                    autoOpen: false,
                    height:250,
                    width:300,
                    modal: true,
                    close: function() {

                    }
                });
            });


            function contextMenubucket(action, el, pos) {

                switch (action) {
                    case "createBuckets":
                        {  //alert(el.attr('id'));
                            var n=el.attr('id').split("~");
                            var colName=n[1];
                            var m=n[0].split(",");
                            var colId=m[0];
                            var tabId=m[1];
                            createBucket(colName,colId,tabId);
                            break;
                        }
                    case "editGroup":
                        {
                            // dimId=$(el).parent().attr('id');
                            //  alert((el.attr('id'))+"-->"+dimId)
                            // getData(el.attr('id'));
                            // createDimensionTable();
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

            function contextMenuWork(action, el, pos) {
                //alert( $(el).parent().attr('id'));

                switch (action) {
                    case "viewTable":
                        {
                            // alert(bustableId);
                            var a=bustableId.split(",");
                            bustableId=a[0];
                            grpId=a[2];
                            document.getElementById("bustableId").value=bustableId;
                            document.getElementById("grpId").value=grpId;
                            //alert(bustableId+','+grpId);
                            getbusData(bustableId,grpId);
                            break;
                        }
                    case "deleteTable":
                        {
                            alert("deleteTable")
                            break;
                        }
                    case "addColumns":
                        {
                            alert("addColumns")
                            break;
                        }
                    case "deleteColumns":
                        {
                            alert("deleteColumns")
                            break;
                        }

                    case "delete":
                        {
                            var msg = "Delete " + $(el).find("#contactname").text() + "?";
                            $("#HiddenFieldRowId").val($(el).find("#customerid").text());
                            confirm(msg);
                            break;
                        }
                    case "insert":
                        {
                            $("#TextBoxContactName").val("");
                            $("#TextBoxContactTitle").val("");
                            $("#TextBoxCountry").val("");
                            $("#TextBoxPhone").val("");

                            $("#addNewCustomer").modal({
                                close: true,
                                onOpen: modalOpenAddCustomer,
                                onClose: modalOnClose,
                                persist: true,
                                containerCss: ({ width: "500px", height: "275px", marginLeft: "-250px" })
                            });
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

            function contextMenuFolder(action, el, pos) {

                switch (action) {
                    case "createUserFolder":
                        {
                            //alert(el.attr("id"));
                            grpId=el.attr("id");
                            addUserFolder();
                            break;
                        }
                        case "assignToUser":
                        {
                            grpId=el.attr("id");                            
                            //assignBussGrpToUser();
                             $('#assignToUser').dialog('open');
                            break;
                        }

                }
            }
             function addUserFolder(){
                var $foldName=$("#folderName");
                var $foldDesc=$("#folderDesc");
                $foldName.val("");
                $foldDesc.val("");
                $('#createUserFolder').dialog('open');
            }
            function assignBussGrpToUser(){
                
            }
            function copyfolderDesc(){
                var $foldName=$("#folderName");
                var $foldDesc=$("#folderDesc");
                $foldDesc.val($foldName.val());
            }
            function createUserFolder(){
                $('#createUserFolder').dialog('close');
                var folderName=$("#folderName").val();
                var folderDesc=$("#folderDesc").val();
                $.ajax({
                    url: 'userLayerAction.do?userParam=addUserFolder&fldName='+folderName+'&fldDesc='+folderDesc+'&grpId='+grpId,
                    success: function(data) {
                        if(data=="false")
                            alert("Unable to Create User Folder")
                        else{
                            parent.checkUserFolder();
                            alert("User Folder Created successfully")
                        }
                            
                    }
                }); 
            }
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

            function cancelFade()
            {
                document.getElementById('fade').style.display='none';
                //document.getElementById('type').style.display='none';
                document.getElementById('dimension').style.display='none';
            }

            function getbusData(tableIds,grpId){
                var frameObj=document.getElementById("busgrpdataDisp");
                // alert('tableIds==='+tableIds);
                // alert('connId==='+connId);
                var source="businessGroupViewTable.jsp?tableIds="+tableIds+"&grpId="+grpId;
                frameObj.src=source;
                frameObj.style.display='block';
                document.getElementById('fade').style.display='block';
            }

            function refreshIframe(source){
                // alert("source is "+source)
                var frameObj=document.getElementById("busgrpdataDisp");
                frameObj.src=source;

            }
            function getbusTableId(obj){
                //var e=event || window.event;
                //alert(obj.id)
                //alert(connectId)
                //  connId=connectId;
                bustableId=obj.id.split(",")[0];


                grpId=obj.id.split(",")[2];
            }
            function cancelTableList()
            {  //alert('hi');
                document.getElementById("busgrpdataDisp").style.display='none';
                document.getElementById('fade').style.display='';
                // window.location.href="<%=request.getContextPath()%>/getAllBusinessGroups.do";
            }
            function refreshparent()
            {
                // document.myForm2.action="getAllDimensions.do";
                // document.myForm2.submit();
                // frameObj.style.display='';
                window.location.reload(true);

            }
            function cancelTablesp()
            {
                //alert("hgj");
                // window.location.href=window.location.href;
                window.location.href="<%=request.getContextPath()%>/getAllDimensions.do";
                //document.getElementById("busgrpdataDisp").style.display='none';
            }
            function createBucket(colName,colId,tabId)
            {

                var frameObj=document.getElementById("bucketDisp");
                var source = "createBucket.jsp?colName="+colName+"&colId="+colId+"&tabId="+tabId;
                // alert(source);
                frameObj.src=source;
                frameObj.style.display='block';
                document.getElementById('fade').style.display='block';

            }
            function cancelBuckets()
            {
                document.getElementById("bucketDisp").style.display='none';
                document.getElementById('fade').style.display='';

            }

        </script>
        <style type="text/css">

    table tr th {
        background-color: #d3DADE;
        padding: 3px;
    }
    table tr.rowb { background-color:#EAf2FD; }

    table tr.filterColumns td { padding:2px; }

    body { padding-bottom:150px; }

    *
    {
        margin:         0px;
        padding:        0px;
        font:           11px sans-serif;
    }

    ul.project
    {
        margin-left:    0px;
    }

    h1
    {
        font:           24px sans-serif;
    }

    h2
    {
        margin-top:     30px;
        font:           bold 16px sans-serif;
    }

    hr
    {
        margin-bottom:  4px;
    }



    p
    {
        margin:         10px 0px;
    }
    td
    {
        padding:        0px;
        background:     white;
    }

    th
    {
        padding:        0px;
        background:     #888;
        color:          white;
    }

    #container
    {
        padding:        20px;
        background:     #FFF;
        width:          600px;
        margin:         0px auto;
    }


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

    .white_content1 {
        display: none;
        position: absolute;
        top: 15%;
        left: 25%;
        width: 50%;
        height:50%;
        padding: 16px;
        border: 10px solid silver;
        background-color: white;
        z-index:1002;


    }

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
                $("#dimensionTree").treeview({
                    animated: "normal",
                    unique:true
                });
            });
        </script>
    </head>
    <body style="overflow:hidden">
        <%--  <a href="getAllBusinessGroups.do">check</a> --%>
        <table style="width:100%;height:100%" border="solid black 1px">
            <input type="hidden" name="bustableId" id="bustableId" value="">
            <input type="hidden" name="grpId" id="grpId" value="">
            <tr>
                <td   width="28%" valign="top">
                    <div style="height:33px" class="ui-state-default draggedDivs ui-corner-all">
                        <font style="font-weight:bold" face="verdana" size="1px">BUSINESS GROUPS</font>
                       <table>
                            <tr>
                                <td valign="top">
                                    <%--<a class="ui-icon ui-icon-plus" title="Add Relations" href="javascript:addBussinessGroup()"></a>--%>
                                </td>
                            </tr>
                        </table>
                    </div>
                    <div style="height:499px;overflow-y:scroll">
                        <ul id="businessTree">
                            <li class="" style="background-image:url('images/treeViewImages/plus.gif')"><img src="images/treeViewImages/cabin_with_smog_pipe.png"><span class="bussGroupMenu">Business Group</span>
                                <ul id="bussGrps">
                                    <logic:present name="BusinessGroupList">
                                        <logic:notEmpty name="BusinessGroupList">
                                            <logic:iterate id="BusinessGroupList" name="BusinessGroupList" >
                                                <li class="closed" id="<bean:write name="BusinessGroupList" property="businessGrpId"/>" ><img src="images/treeViewImages/home.png"><span class="createFolderMenu"><bean:write name="BusinessGroupList" property="businessGrpName"/></span>
                                                    <ul>
                                                        <li class="closed" id='dimGrp-<bean:write name="BusinessGroupList" property="businessGrpId"/>'><img src="images/treeViewImages/dim.png"><span id="123" class="addGrpDims" ><font size="1px" face="verdana">Dimensions</font></span>
                                                            <ul>
                                                                <logic:notEmpty name="BusinessGroupList" property="dimensionList">
                                                                    <logic:iterate id="dimensionList" name="BusinessGroupList" property="dimensionList">

                                                                        <li  class="closed"><img src="images/treeViewImages/dim.png" ><span><font size="1px" face="verdana"><bean:write name="dimensionList" property="dimensionName"/></font></span>
                                                                            <ul>

                                                                                <li class="closed"><span class="folder"><font size="1px" face="verdana">Tables</font></span>
                                                                                    <ul>
                                                                                        <logic:notEmpty name="dimensionList" property="dimTableList">
                                                                                            <logic:iterate id="dimTableList" name="dimensionList" property="dimTableList" >
                                                                                                <logic:notEqual name="dimTableList" property="dimTableName" value="">
                                                                                                    <li  class="closed"><img src="images/treeViewImages/database_table.png"><span >
                                                                                                        <font size="1px" face="verdana"> <bean:write name="dimTableList" property="dimTableName"/></font></span>
                                                                                                        <ul >
                                                                                                        </logic:notEqual>
                                                                                                        <logic:notEqual name="dimTableList" property="dimColumnName" value="">

                                                                                                            <li id="<bean:write name="dimTableList" property="dimColumnId"/>~<bean:write name="dimTableList" property="dimColumnName"/>">
                                                                                                                <logic:equal name="dimTableList" property="isAvailable" value="Y">
                                                                                                                    <input type="checkbox" name="chkdimtabs" checked value="<bean:write name="dimTableList" property="dimColumnId"/>">
                                                                                                                    <logic:equal name="dimTableList" property="colType" value="NUMBER">
                                                                                                                        <span class="bucketMenu"><font size="1px" face="verdana"><bean:write name="dimTableList" property="dimColumnName"/></font></span>
                                                                                                                    </logic:equal>
                                                                                                                    <logic:notEqual name="dimTableList" property="colType" value="NUMBER">
                                                                                                                        <span><font size="1px" face="verdana"><bean:write name="dimTableList" property="dimColumnName"/></font></span>
                                                                                                                    </logic:notEqual>
                                                                                                                </logic:equal>
                                                                                                                <logic:equal name="dimTableList" property="isAvailable" value="N">
                                                                                                                    <input type="checkbox" name="chkdimtabs" value="<bean:write name="dimTableList" property="dimColumnId"/>">
                                                                                                                    <logic:equal name="dimTableList" property="colType" value="NUMBER">
                                                                                                                        <span class="bucketMenu"><font size="1px" face="verdana"><bean:write name="dimTableList" property="dimColumnName"/>,<bean:write name="dimTableList" property="colType"/></font></span>
                                                                                                                    </logic:equal>
                                                                                                                    <logic:notEqual name="dimTableList" property="colType" value="NUMBER">
                                                                                                                        <span ><font size="1px" face="verdana"><bean:write name="dimTableList" property="dimColumnName"/>,<bean:write name="dimTableList" property="colType"/></font></span>
                                                                                                                    </logic:notEqual>
                                                                                                                </logic:equal>
                                                                                                                <logic:equal name="dimTableList" property="isPk" value="Y">
                                                                                                                    <img src="images/treeViewImages/key.png" >
                                                                                                                </logic:equal>
                                                                                                            </li>
                                                                                                        </logic:notEqual>
                                                                                                        <logic:equal name="dimTableList" property="endTable" value="true">
                                                                                                        </ul>
                                                                                                    </li>
                                                                                                </logic:equal>
                                                                                            </logic:iterate>
                                                                                        </logic:notEmpty>
                                                                                    </ul>
                                                                                </li>
                                                                                <li class="closed"><span class="folder"><font size="1px" face="verdana">Members</font></span>
                                                                                    <ul>
                                                                                        <logic:notEmpty name="dimensionList" property="dimMembersList">
                                                                                            <logic:iterate id="dimMembersList" name="dimensionList" property="dimMembersList" >
                                                                                                <logic:notEqual name="dimMembersList" property="dimMemberName" value="">
                                                                                                    <li  ><img src="images/treeViewImages/database_table.png"><span >
                                                                                                        <font size="1px" face="verdana"><bean:write name="dimMembersList" property="dimMemberName"/></font></span>
                                                                                                    </li>
                                                                                                </logic:notEqual>
                                                                                            </logic:iterate>
                                                                                        </logic:notEmpty>
                                                                                    </ul>
                                                                                </li>

                                                                                <li class="closed"><span class="folder" ><font size="1px" face="verdana">Hierarchies</font></span>
                                                                                    <ul>
                                                                                        <logic:notEmpty name="dimensionList" property="dimHierarchyList">
                                                                                            <logic:iterate id="dimHierarchyList" name="dimensionList" property="dimHierarchyList" >
                                                                                                <logic:notEqual name="dimHierarchyList" property="dimRelationName" value="">
                                                                                                    <li  class="closed"><img src="images/databases-relation.png"><span id="heirarchy">
                                                                                                        <font size="1px" face="verdana"><bean:write name="dimHierarchyList" property="dimRelationName"/></font></span>
                                                                                                        <ul >
                                                                                                        </logic:notEqual>

                                                                                                        <logic:notEqual name="dimHierarchyList" property="dimRelColumnName" value="">
                                                                                                            <li ><span><font size="1px" face="verdana">
                                                                                                            <bean:write name="dimHierarchyList" property="dimRelColumnName"/></font></span></li>

                                                                                                        </logic:notEqual>
                                                                                                        <logic:equal name="dimHierarchyList" property="endTable" value="true">

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
                                                        <li class="closed"><img src="images/beaker-empty.png"><span >Buckets</span>
                                                            <ul>
                                                                <logic:notEmpty name="BusinessGroupList" property="bucketsList">
                                                                    <logic:iterate id="bucketsList" name="BusinessGroupList" property="bucketsList" >
                                                                        <logic:notEqual name="bucketsList" property="bucketTableName" value="">
                                                                            <li class="closed"><img src="images/beaker-empty.png"><span><bean:write name="bucketsList" property="bucketTableName"/></span>
                                                                                <ul>
                                                                                </logic:notEqual>
                                                                                <logic:notEqual name="bucketsList" property="bucketColName" value="">

                                                                                    <li>

                                                                                        <%-- <input type="checkbox" name="chkbuckets" checked value="<bean:write name="bucketsList" property="bucketColId"/>">--%>
                                                                                        <span><bean:write name="bucketsList" property="bucketColName"/></span>

                                                                                    </li>
                                                                                </logic:notEqual>

                                                                                <logic:equal name="bucketsList" property="endTable" value="true">
                                                                                </ul>
                                                                            </li>
                                                                        </logic:equal>
                                                                    </logic:iterate>
                                                                </logic:notEmpty>
                                                            </ul>
                                                        </li>

                                                        <li class="closed"><span class="folder">Facts</span>
                                                            <ul>
                                                                <logic:notEmpty name="BusinessGroupList" property="factsList">
                                                                    <logic:iterate id="factsList" name="BusinessGroupList" property="factsList" >
                                                                        <logic:notEqual name="factsList" property="factTableName" value="">
                                                                            <li class="closed"><img src="images/treeViewImages/database_table.png"><span><bean:write name="factsList" property="factTableName"/></span>
                                                                                <ul>
                                                                                </logic:notEqual>
                                                                                <logic:notEqual name="factsList" property="factTableColName" value="">

                                                                                    <li id="<bean:write name="factsList" property="factTableColId"/>~<bean:write name="dimTableList" property="factTableColName"/>">

                                                                                        <input type="checkbox" name="chkfacts" checked value="<bean:write name="factsList" property="factTableColId"/>">
                                                                                        <logic:equal name="factsList" property="factColType" value="NUMBER">
                                                                                            <span class="bucketMenu"><bean:write name="factsList" property="factTableColName"/></span>
                                                                                        </logic:equal>
                                                                                        <logic:notEqual name="factsList" property="factColType" value="NUMBER">
                                                                                            <span class="bucketMenu"><bean:write name="factsList" property="factTableColName"/></span>
                                                                                        </logic:notEqual>
                                                                                    </li>
                                                                                </logic:notEqual>
                                                                                <logic:equal name="factsList" property="endTable" value="true">
                                                                                </ul>
                                                                            </li>
                                                                        </logic:equal>
                                                                    </logic:iterate>
                                                                </logic:notEmpty>
                                                            </ul>
                                                        </li>

                                                        <li class="closed" id='AllTab-<bean:write name="BusinessGroupList" property="businessGrpId"/>' ><span class="folder addGrpTables">AllTables</span>
                                                            <ul id='ul-<bean:write name="BusinessGroupList" property="businessGrpId"/>'>
                                                                <logic:notEmpty name="BusinessGroupList" property="allTablesList">
                                                                    <logic:iterate id="allTablesList" name="BusinessGroupList" property="allTablesList" >
                                                                        <logic:notEqual name="allTablesList" property="allTableName" value="">
                                                                            <li onclick="getbusTableId(this)"   id="<bean:write name="allTablesList" property="allTableId"/>"class="closed"><img src="images/treeViewImages/database_table.png"><span  class="openmenubusgrp"><bean:write name="allTablesList" property="allTableName"/></span>
                                                                                <ul>
                                                                                </logic:notEqual>
                                                                                <logic:notEqual name="allTablesList" property="allTableColName" value="">

                                                                                    <li >

                                                                                        <input type="checkbox" name="chkalltabs" checked value="<bean:write name="allTablesList" property="allTableColId"/>">
                                                                                        <span><bean:write name="allTablesList" property="allTableColName"/></span>

                                                                                    </li>
                                                                                </logic:notEqual>
                                                                                <logic:equal name="allTablesList" property="endTable" value="true">
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
                                    </logic:present>
                                </ul>
                            </li>
                        </ul>
                    </div>
                </td>
                <td>
                    <table style="width:100%;height:100%">

                        <tr>
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
                            <td class="hideTableCell" id="dbDimensions" valign="top" >
                                <div style="height:33px" class="ui-state-default draggedDivs ui-corner-all">
                                    <b>Dimensions</b>

                                </div>
                                <ul id="dimensionTree" class="filetree">
                                    <li style="background-image:url('images/treeViewImages/plus.gif')"><img src="images/treeViewImages/database.png"><span  id="123">Dimension</span>
                                        <ul id="dimensionTree">

                                            <logic:notEmpty name="dimensionList" scope="request" >
                                                <logic:iterate id="dimensionList" name="dimensionList" scope="request">

                                                    <li class="closed"><img src="images/treeViewImages/database_table.png"><span class="dimensions" id='dimId-<bean:write name="dimensionList" property="dimensionId"/>'><bean:write name="dimensionList" property="dimensionName"/></span>
                                                        <ul id='ul-dimId-<bean:write name="dimensionList" property="dimensionId"/>'>


                                                            <logic:iterate id="tableList" name="dimensionList" property="tableList">
                                                                <li id='dimTable-<bean:write name="tableList" property="tableId"/>'><img src="images/treeViewImages/bullet_star.png"><span><bean:write name="tableList" property="tableName"/></span></li>
                                                            </logic:iterate>

                                                        </ul>
                                                    </li>

                                                </logic:iterate>

                                            </logic:notEmpty>

                                        </ul>
                                    </li>
                                </ul>

                            </td>
                            <td class="hideTableCell" id="draggableTables"   width="25%" valign="top">

                                <div style="height:33px" class="ui-state-default draggedDivs ui-corner-all">
                                    <b>Drag Tables to Add As Facts</b>
                                    <table>
                                        <tr><td valign="top">
                                            <a class="ui-icon ui-icon-check" title="Add Relations" href="javascript:addBussTables()"></a></td>
                                            <td valign="top">
                                                <a class="ui-icon ui-icon-closethick" title="Clear All" href="javascript:delTables()"></a>
                                    </td></tr></table>
                                </div><div id="divDraggableTables"></div>

                            </td>
                            <td  class="hideTableCell" id="addDbDims" width="25%" valign="top">
                                <table  style="width:100%;height:100%">
                                    <tr style="height:50%">
                                        <td id="dimensionDrag" valign="top"><div style="height:33px" class="ui-state-default draggedDivs ui-corner-all">
                                                <b>Drag Dimensions To Add</b><table>
                                                    <tr><td valign="top">
                                                        <a class="ui-icon ui-icon-check" title="Add Dimensions" href="javascript:addDimensions()"></a></td>
                                                        <td valign="top">
                                                            <a class="ui-icon ui-icon-closethick" title="Clear All" href="javascript:removeAllDims()"></a>
                                                </td></tr></table>
                                            </div><div id="draggedDims"></div>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td id="factsDrag" valign="top">
                                            <div style="height:33px" class="ui-state-default draggedDivs ui-corner-all">
                                                <b>Drag Related Tables To Add As Facts</b><table>
                                                    <tr><td valign="top">
                                                        <a class="ui-icon ui-icon-check" title="Add Relations" href="#"></a></td>
                                                        <td valign="top">
                                                            <a class="ui-icon ui-icon-closethick" title="Clear All" href="javascript:delTables()"></a>
                                                </td></tr></table>
                                            </div><div id="draggedFacts"></div>
                                        </td>
                                </tr></table>
                            </td>
                            <td class="hideTableCell" id="relDBTables" valign="top">
                                <div style="height:33px" class="ui-state-default draggedDivs ui-corner-all">
                                    <b>Related Tables</b>

                                </div><ul id="bussRelatedTableTree" class="filetree">
                                    <li style="background-image:url('images/treeViewImages/plus.gif')"><img src="images/treeViewImages/database.png"><span  id="123">Related Tables</span>
                                        <ul id="bussRelatedTable">

                                        </ul>
                                    </li>
                                </ul>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
        </table>
        <div id="fade" class="black_overlay"></div>
        <div>

            <iframe  id="busgrpdataDisp" NAME='busgrpdataDisp'  STYLE='display:none;'   class="white_content1" SRC=''></iframe>

        </div>
        <div >
            <iframe  id="bucketDisp" NAME='bucketDisp'  STYLE='display:none;'   class="white_content1" SRC=''></iframe>
        </div>
        <ul id="myMenubusgrp" class="contextMenu">
            <li class="viewTable"><a href="#viewTable"><font class="text_classstyle1">View Table</font></a></li>
        </ul>

        <ul id="grpbucket" class="contextMenu" style="width:150px;text-align:left">

            <li class="insert"><a href="#createBuckets">Add Buckets</a></li>
            <li class="insert"><a href="#deleteBuckets">Delete Buckets</a></li>

        </ul>



        <!--Added By Santhosh-->
        <script>
            var tableActive;
            var $dragTables=$('#bussTable > li >span')
            var $dimensions=$(".dimensions");
            var $draggableTables=$('#draggableTables');
            var $dimensionDrag=$("#dimensionDrag");
            var $factsDrag=$('#factsDrag');
            $($dragTables).draggable({
                helper:"clone",
                effect:["", "fade"]
            });
            $($dimensions).draggable({
                helper:"clone",
                effect:["", "fade"]
            });

            $($dimensionDrag).droppable(
            {
                activeClass:"blueBorder",
                accept:'.dimensions',
                drop: function(ev, ui) {
                    addDimension(ui);

                }
            });

            $($factsDrag).droppable(
            {
                activeClass:"blueBorder",
                accept:'#bussRelatedTable > li > ul > li >span',
                drop: function(ev, ui) {
                    var idTab=ui.draggable.attr("id").split("-");
                    getBussTableDetails(ui.draggable.html(),idTab[1],"draggedFacts");
                }
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

                        getBussTableDetails(ui.draggable.html(),idTab[1],"divDraggableTables");
                    }
                    else
                        $('#warning').dialog('open');
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
                    <td><input type="text" onkeyup='groupDesc()'name="grpName" id="grpName" class="text ui-widget-content ui-corner-all" /></td></tr>

                    <tr><td>
                        <label for="email">Group Description</label></td><td>
                    <textarea name="grpDesc" id="grpDesc" class="textarea ui-widget-content ui-corner-all" ></textarea></td></tr>
                </table>
            </form>
        </div>

        <div id="createUserFolder" title="Create New User Folder">
            <form>
                <table cellpadding="5">
                    <tr><td>
                        <label for="name">Folder Name</label></td>
                    <td><input type="text" onkeyup='javascript:copyfolderDesc()'name="folderName" id="folderName" class="text ui-widget-content ui-corner-all" /></td></tr>

                    <tr><td>
                        <label for="email">Folder Description</label></td><td>
                    <textarea name="folderDesc" id="folderDesc" class="textarea ui-widget-content ui-corner-all" ></textarea></td></tr>
                </table>
            </form>
        </div>
        <div id="assignToUser" title="Assign To Users">
            <span>Assign Business Group To Users</span>
        </div>

        <ul id="groupListMenu" class="contextMenu" style="width:150px;text-align:left">
            <li class="insert"><a href="#createGroup">Create Business Group</a></li>
            <li class="insert"><a href="#deleteGroup">Delete Business Group</a></li>
        </ul>
        <ul id="groupTableMenu" class="contextMenu" style="width:150px;text-align:left">
            <li class="insert"><a href="#addTables">Add Tables</a></li>
        </ul>
        <ul id="groupDimMenu" class="contextMenu" style="width:150px;text-align:left">
            <li class="insert"><a href="#addDimensions">Add Dimensions</a></li>
        </ul>
        <ul id="userFolder" class="contextMenu" style="width:150px;text-align:left">
            <li class="insert"><a href="#createUserFolder">Create User Folder</a></li>
            <li class="insert"><a href="#assignToUser">Assign To Users</a></li>
        </ul>
        <!-- End -->

    </body>

</html>
