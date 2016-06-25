<%--
    Document   : MyTable
    Created on : Aug 7, 2009, 7:09:17 PM
    Author     : Administrator
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" import="prg.db.PbDb,prg.db.PbReturnObject,java.util.ArrayList,java.util.HashMap,prg.business.group.BusinessGroupEditAction"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%          //for clearing cache
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);

            String statusOfBucket = "";
            if (request.getAttribute("statusOfBucket") != null) {
                statusOfBucket = (String) request.getAttribute("statusOfBucket");
            }
            String DefaultArrregations[] = {"", "sum", "avg", "min", "max", "count", "COUNTDISTINCT"};
String contextPath=request.getContextPath();

%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>

         <link rel="stylesheet" href="css/bootstrap.min.css" type="text/css"/>
        <link rel="stylesheet" href="css/bootstrap-theme.min.css" type="text/css"/>
	<link rel="stylesheet" href="css/font-awesome.min.css" type="text/css"/>
        <script src="javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>

        <link rel="stylesheet" href="stylesheets/treeviewstyle/jquery.treeview.css" />
        <link rel="stylesheet" href="stylesheets/treeviewstyle/screen.css" />

        <script src="javascript/treeview/jquery.cookie.js" type="text/javascript"></script>
        <script src="javascript/treeview/jquery.treeview.js" type="text/javascript"></script>

        <!--        <script type="text/javascript" src="javascript/treeview/demo.js"></script>

                <script type="text/javascript" src="javascript/draggable/ui.core.js"></script>
                <script type="text/javascript" src="javascript/draggable/ui.draggable.js"></script>
                <script type="text/javascript" src="javascript/draggable/ui.droppable.js"></script>-->

        <!--        <script type="text/javascript" src="javascript/ui.resizable.js"></script>
                <script type="text/javascript" src="javascript/ui.accordion.js"></script>
                <script type="text/javascript" src="javascript/ui.dialog.js"></script>
                <script type="text/javascript" src="javascript/ui.sortable.js"></script>
                <script type="text/javascript" src="javascript/effects.core.js"></script>
                <script type="text/javascript" src="javascript/effects.explode.js"></script>-->
        <link type="text/css" href="stylesheets/ui.all.css" rel="stylesheet" />
<!--        <link type="text/css" href="stylesheets/demos.css" rel="stylesheet" />-->
        <script  type="text/javascript"  language="JavaScript" src="<%=contextPath%>/querydesigner/JS/jquery.columnfilters.js"></script>
        <!-- below five lines only added by bharathi reddy on 26-08-09 -->
        <link href="stylesheets/StyleSheet.css" rel="stylesheet" type="text/css" />
        <link href="stylesheets/confirm.css" rel="stylesheet" type="text/css" />
        <link href="stylesheets/jquery.contextMenu.css" rel="stylesheet" type="text/css" />
        <script src="javascript/jquery.simplemodal-1.1.1.js" type="text/javascript"></script>
        <script src="javascript/jquery.contextMenu.js" type="text/javascript"></script>
        <script type="text/javascript" src="javascript/BusinessGroup.js"></script>
         <link rel="stylesheet" href="<%=contextPath%>/tablesorter/themes/<%=session.getAttribute("theme")%>/style.css" type="text/css" media="print, projection, screen" />
        <link type="text/css" href="<%=contextPath%>/stylesheets/metadataButton.css" rel="stylesheet" />
         <script type="text/javascript" src="<%=contextPath%>/tablesorter/jquery.tablesorter.js"></script>
         <script type="text/javascript" src="<%=contextPath%>/tablesorter/addons/pager/jquery.tablesorter.pager.js"></script>

         <script type="text/javascript"  language="JavaScript" src="<%=contextPath%>/tablesorter/jquery.columnfilters.js"></script>
         <script type="text/javascript" src="<%=contextPath%>/tablesorter/docs/js/chili/chili-1.8b.js"></script>

        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=session.getAttribute("theme")%>/ReportCss.css" rel="stylesheet" />
         <link rel="stylesheet" type="text/css" href="<%=contextPath%>/css/global.css"/>
        <style type="text/css">
            *{
                font-size: 12px;
            }
           #ui-datepicker-div
           {
               z-index: 9999999;
               width: 40em; padding: .2em .2em 0; font-size: 62.5%;
           }

             .migrate
           {
               font-family: inherit;
               font-size: 12px;
               font-weight: bold;
               color: #3A457C;
               padding-left:12px;
               background-color:#3A457Curgajobs;
               border:0px;
               /*apply this class to a Headings of servicestable only*/
           }

       </style>
        <script type="text/javascript">
            $(document).ready(function(){
                <%--$("#targetDetails").tablesorter({headers : {0:{sorter:false}}});--%>

            <%--$("#targetDetails").tablesorterPager({container: $('#pagination')});--%>
                $("#datefortarget").datepicker({
                   changeMonth: true,
                   changeYear: true
                  <%-- numberOfMonths: 1,
                   stepMonths: 1--%>
               });

                if ($.browser.msie == true){
                    $("#dimension").dialog({
                        autoOpen: false,
                        height: 400,
                        width: 400,
                        position: 'justify',
                        modal: true
                    });
                    $("#userAssigned").dialog({
                        autoOpen: false,
                        height: 550,
                        width: 700,
                        position: 'justify',
                        modal: true
                    });
                    $("#migrateChangesDiv").dialog({
                        autoOpen: false,
                        height: 550,
                        width: 700,
                        position: 'justify',
                        modal: true
                    });
                    $("#editBgDiv").dialog({
                        autoOpen: false,
                        height: 550,
                        width: 700,
                        position: 'justify',
                        modal: true
                    });
                    $("#targetDialog").dialog({
                        //bgiframe: true,
                        autoOpen: false,
                        height: 500,
                        width: 500,
                        position: 'top',
                        modal: true
                    });
                    $("#tableDisplay").dialog({
                        //bgiframe: true,
                        autoOpen: false,
                        height: 680,
                        width: 800,
                        position: 'top',
                        modal: true
                    });
                    $("#targetParametersDiv").dialog({
                        //bgiframe: true,
                        autoOpen: false,
                        height: 500,
                        width: 500,
                        position: 'top',
                        modal: true
                    });
                    $("#editBucketDiv").dialog({
                        //bgiframe: true,
                        autoOpen: false,
                        height: 500,
                        width: 610,
                        position: 'top',
                        modal: true
                    });
                    $("#createBucketdiv").dialog({
                        //bgiframe: true,
                        autoOpen: false,
                        height: 500,
                        width: 610,
                        position: 'top',
                        modal: true
                    });
                    $("#quickFormulaDialog").dialog({
                        //bgiframe: true,
                        autoOpen: false,
                        height: 520,
                        width: 750,
                        position: 'top',
                        modal: true

                    });
                    // start by ramesh
                    $("#dateDivId").dialog({
                        //bgiframe: true,
                        autoOpen: false,
                        height:400,
                        width: 400,
                        position: 'top',
                        modal: true
                    });
                      $("#GroupEditdiv").dialog({
                        autoOpen: false,
                        height: 400,
                        width: 400,
                        position: 'justify',
                        modal: true
                    });
                    // end by ramesh
                }
                else{
                    $("#dimension").dialog({
                        autoOpen: false,
                        height: 250,
                        width: 400,
                        position: 'justify',
                        modal: true
                    });
                    $("#userAssigned").dialog({
                        autoOpen: false,
                        height: 450,
                        width: 700,
                        position: 'justify',
                        modal: true
                    });
                    $("#migrateChangesDiv").dialog({
                        autoOpen: false,
                        height: 450,
                        width: 700,
                        position: 'justify',
                        modal: true
                    });
                    $("#editBgDiv").dialog({
                        autoOpen: false,
                        height: 450,
                        width: 700,
                        position: 'justify',
                        modal: true
                    });
                    $("#childDiv").dialog({
                        autoOpen: false,
                        height: 450,
                        width: 700,
                        position: 'justify',
                        modal: true
                    });
                    $("#targetDialog").dialog({
                        //bgiframe: true,
                        autoOpen: false,
                        height: 400,
                        width: 500,
                        position: 'top',
                        modal: true
                    });
                    $("#tableDisplay").dialog({
                        //bgiframe: true,
                        autoOpen: false,
                        height: 580,
                        width: 800,
                        position: 'top',
                        modal: true
                    });
                    $("#targetParametersDiv").dialog({
                        //bgiframe: true,
                        autoOpen: false,
                        height: 400,
                        width: 500,
                        position: 'top',
                        modal: true
                    });
                    $("#editBucketDiv").dialog({
                        //bgiframe: true,
                        autoOpen: false,
                        height: 500,
                        width: 610,
                        position: 'top',
                        modal: true
                    });
                    $("#createBucketdiv").dialog({
                        //bgiframe: true,
                        autoOpen: false,
                        height: 500,
                        width: 610,
                        position: 'top',
                        modal: true
                    });
                    $("#quickFormulaDialog").dialog({
                        //bgiframe: true,
                        autoOpen: false,
                        height: 551,
                        width: 750,
                        position: 'top',
                        modal: true

                    });
                     // start by ramesh
                   $("#dateDivId").dialog({
                        //bgiframe: true,
                        autoOpen: false,
                        height:400,
                        width: 400,
                        position: 'top',
                        modal: true
                    });
                      $("#GroupEditdiv").dialog({
                        autoOpen: false,
                        height: 400,
                        width: 400,
                        position: 'justify',
                        modal: true
                    });
                    // end by ramesh

                }
            });
        </script>
        

        <style type="text/css">
            .myhead
            {
                font-family: Verdana, Arial, Helvetica, sans-serif;
                font-size: 8pt;
                font-weight: bold;
                color: #3A457C;
                padding-left:12px;

                background-color:#b4d9ee;
                border:0px;
                /*apply this class to a Headings of servicestable only*/
            }

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
                font:           11px Verdana;
                color:          black;
            }
            .insert{
                width:100%
            }
            ul.project
            {
                margin-left:    0px;
            }

            h1
            {
                font:           24px Verdana;
            }

            h2
            {
                margin-top:     30px;
                font:           bold 16px Verdana;
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
                width: 100%;
                height: 120%;
                background-color: black;
                z-index:1001;
                -moz-opacity: 0.5;
                opacity:.50;
                filter:alpha(opacity=50);
                overflow:auto;
            }
            .black_overlay1{
                display: none;
                position: absolute;
                top: 0%;
                left:0%;
                width: 100%;
                height: 120%;
                background-color: black;
                z-index:1001;
                -moz-opacity: 0.5;
                opacity:0.50;
                filter:alpha(opacity=50);
                overflow:auto;
            }
            .white_content {
                display: none;
                position: absolute;
                top: 1%;
                left: 25%;
                width: 50%;
                height:50%;
                padding: 16px;
                border: 16px solid #308dbb;
                background-color: white;
                z-index:1002;
                -moz-border-radius-bottomleft:6px;
                -moz-border-radius-bottomright:6px;
                -moz-border-radius-topleft:6px;
                -moz-border-radius-topright:6px;
            }
            .relationTable{
                border-top-width: 0px;
                border-bottom-width: 0px;
                border-right-width: 0px;
                border-left-width: 0px;
            }
            .myClassTestnew {
                height:43%;
                overflow:auto;
                position:absolute;
                width:350px;
            }
            .white_content1 {
                display: none;
                position: absolute;
                top: 50px;
                left: 25%;
                width: 50%;
                height:50%;
                padding: 16px;
                border: 10px solid silver;
                background-color: white;
                z-index:1002;
                -moz-border-radius-bottomleft:6px;
                -moz-border-radius-bottomright:6px;
                -moz-border-radius-topleft:6px;
                -moz-border-radius-topright:6px;
            }
            .white_bucket{
                display: none;
                position: absolute;
                top: 50px;
                left: 25%;
                width: 50%;
                height:50%;
                padding: 16px;
                border: 10px solid silver;
                background-color: white;
                z-index:1002;
                -moz-border-radius-bottomleft:6px;
                -moz-border-radius-bottomright:6px;
                -moz-border-radius-topleft:6px;
                -moz-border-radius-topright:6px;
            }
            .myClassTestnew {
                height:43%;
                overflow:auto;
                position:absolute;
                width:350px;
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
            .frame1{
                border: 0px;
                width: 500px;
                height: 500px;
            }
            .label{
                font-family:verdana;
                font-size:12px;
                font-weight:normal;
                color: black;
                padding:2px;
            }
            .MybusiBody{
                overflow:hidden;
                position:relative;
                padding-bottom:0px;
                padding-left:0px;
                padding-top:0px;
                padding-right:0px;
            }
            .color{
                color:transparent;
            }
            #tablePropDisp a{
                color: #000;
            }
        </style>
        
    </head>
    <body class="MybusiBody" onload="busremoveimage()">


        <%

                    String connId = String.valueOf(session.getAttribute("connId"));
                    String userid = String.valueOf(session.getAttribute("USERID"));


                    ////////////////////////////////////////////////////////////////.println.println("userid is"+userid);
                    String connName = "";
                    PbReturnObject pbro = null;
                    if (connId == null || connId.equalsIgnoreCase("NULL")) {
                        connName = "";
                    } else {
                        String Query = " SELECT CONNECTION_NAME FROM PRG_USER_CONNECTIONS where CONNECTION_ID in(" + connId + ")";
                        pbro = new PbDb().execSelectSQL(Query);
                        connName = pbro.getFieldValueString(0, 0).toUpperCase();
                    }

        %>
        <%--  <a href="getAllBusinessGroups.do">check</a> --%>
        <table style="width:100%;height:100%" border="solid black 1px">
            <form name="myFormnew" method="post">
                <input type="hidden" name="loginuser" id="loginuser" value="<%=userid%>">
                <input type="hidden" name="connId" id="connId" value="<%=connId%>">
                <input type="hidden" name="bustableId" id="bustableId" value="">
                <input type="hidden" name="grpId" id="grpId" value="">
                <tr>

                    <td width="28%" valign="top">

                        <div style="height:33px" class="themeColor draggedDivs ui-corner-all">
                            <font style="font-weight:bold" face="verdana" size="1px"> Business Groups
                                - <a href="javascript:void(0)" onclick="javascript:goConnection()" style="text-decoration:none;color:black"><b><%=connName%></b></a></font>
                                <div style="padding: 5px;float: right;">
                                    <a class="fa fa-refresh themeColor" href="javascript:void(0)" onclick="refreshPage()" style="color:white;"></a>
                        </div>
                        </div>
                        <%if (request.getAttribute("bussgrpstr") != null) {
                                        // //////////////////.println("session.getAttribute(bussgrpstr)\t" + request.getAttribute("bussgrpstr"));
                                        out.println(request.getAttribute("bussgrpstr"));
                                    }
                        %>

                    </td>
                    <td>
                        <table style="width:100%;height:100%">
                            <tr>
                                <td id="myDbTableList" class="hideTableCell" width="25%" valign="top">
                                    <div style="width:99%;height:580px;overflow:auto">
                                        <ul id="bussTableTree" class="filetree" style="width:100%">
                                            <li style="background-image:url('images/treeViewImages/plus.gif')"><img src="images/treeViewImages/database.png"><span  id="123">&nbsp;TABLES CHECK</span>
                                                <ul id="bussTable" style="height:550px;overflow-y:auto">
                                                    <%if (request.getAttribute("list") != null) {
                                                                    out.println(request.getAttribute("list"));
                                                                }
                                                    %>

                                                </ul>
                                            </li>
                                        </ul>
                                    </div>


                                                </td>
                                            <td class="hideTableCell" id="dbDimensions" valign="top" >
                                                <div style="height:33px" class="themeColor draggedDivs ui-corner-all">
                                                    
                                                    <b>Dimensions</b>
                                                </div>
                                                <ul id="dimensionTree" class="filetree" style="height:550px;overflow-y:auto">
                                                    <li style="background-image:url('images/treeViewImages/plus.gif')"><img src="images/treeViewImages/database.png"><span  id="123">Dimension</span>
                                                        <ul id="dimensionTree">
                                                            <%
                                                                        if (request.getAttribute("dimensionList") != null && (!request.getAttribute("dimensionList").toString().equals(""))) {
                                                                            out.println(request.getAttribute("dimensionList"));
                                                                            ////////////////////.println(request.getAttribute("dimensionList").toString());
                                                                        }
                                                            %>

                                                            <%--<logic:notEmpty name="dimensionList" scope="request" >
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
                                                            --%>
                                                        </ul>
                                                    </li>
                                                </ul>

                                            </td>


                                            <%--Start by uday --%>

                                            <td class="hideTableCell" id="relatedTablesPanel" valign="top" width="35%">

                                                <div style="height:33px" class="themeColor draggedDivs ui-corner-all">
                                                    <font style="font-weight:normal" face="verdana" size="1px">Related Tables</font>
                                                </div>
                                                <div style="height:550px;overflow-y:auto">
                                                    <ul id="relatedTablesTree" name="relatedTablesTree" class="filetree">
                                                        <li style="background-image:url('images/treeViewImages/plus.gif')"><img src="images/treeViewImages/database.png"><span id="123">Related Tables</span>
                                                            <ul id="relatedTablesTreeIn" onclick="openRelated()">

                                                            </ul>
                                                        </li>
                                                    </ul>
                                                </div>
                                            </td>
                                            <td class="hideTableCell" id="editRelationPanel" valign="top" width="65%">
                                                <table style="height:100%;width:100%" border="0">
                                                    <tr style="height:50%">
                                                        <td valign="top">
                                                            <div style="height:33px" class="themeColor draggedDivs ui-corner-all">
                                                                <font style="font-weight:normal" face="verdana" size="1px">Drag Columns To Add Relations</font>
                                                                <table>
                                                                    <tr><td valign="top">
                                                                            <a class="ui-icon ui-icon-check" title="Update Relations" href="javascript:updateRelatedTablesRelations()" style="background-color:rgb(139, 195, 74)"></a></td>
                                                                        <td valign="top">
                                                                            <a class="ui-icon ui-icon-closethick" title="Clear All" onclick="javascript:removeAllRelatedTablesRelations()" style="background-color:rgb(139, 195, 74);"></a>
                                                                        </td>
                                                                    </tr>
                                                                </table>
                                                            </div>
                                                            <div style="height:100%;overflow-y:auto">
                                                                <table id="showRelatedTableColumnsRelation">

                                                                </table>
                                                            </div>
                                                        </td>
                                                    </tr>
                                                    <tr style="height:50%">
                                                        <td valign="top">
                                                            <div style="height:33px" class="themeColor draggedDivs ui-corner-all">
                                                                <b>Commit Relations</b>
                                                                <table>
                                                                    <tr>
                                                                        <td valign="top">
                                                                            <a class="ui-icon ui-icon-check" title="Commit Relations" href="javascript:checkCommitRelatedTableRelations()"></a>
                                                                        </td>
                                                                        <td valign="top">
                                                                            <a class="ui-icon ui-icon-closethick" title="Clear All" href="javascript:removeAllRelations()"></a>
                                                                        </td>
                                                                    </tr>
                                                                </table>
                                                            </div>
                                                            <div id="commitRelationTxt">
                                                            </div>
                                                        </td>
                                                    </tr>
                                                </table>
                                            </td>

                                            <%--  End  by uday--%>


                                            <td class="hideTableCell" id="draggableTables"   width="25%" valign="top">

                                                <div style="height:33px" class="navtitle-hover draggedDivs ui-corner-all">
                                                    <b>Drag Tables to Add As Facts</b>
                                                    <table>
                                                        <tr><td valign="top">
                                                                <a class="ui-icon ui-icon-check" title="Add Relations" href="javascript:addBussTables()" style="background-color:rgb(139, 195, 74);"></a></td>
                                                            <td valign="top">
                                                                <a class="ui-icon ui-icon-closethick" title="Clear All" href="javascript:delTables()" style="background-color:rgb(139, 195, 74)"></a>
                                                            </td></tr></table>
                                                </div><div id="divDraggableTables" class="ui-accordion" style="height:550px;overflow:auto"></div>

                                            </td>
                                            <td  class="hideTableCell" id="addDbDims" width="25%" valign="top">
                                                <table  style="width:100%;height:100%">
                                                    <tr style="height:50%">
                                                        <td id="dimensionDrag" valign="top" class="themeColor draggedDivs ui-corner-all">
                                                            <div style="height:33px" class="themeColor draggedDivs ui-corner-all"><b>Drag Dimensions To Add</b>
                                                                <table>
                                                                    <tr><td valign="top">
                                                                            <a class="ui-icon ui-icon-check" title="Add Dimensions" href="javascript:addDimensions()" style="background-color:rgb(139, 195, 74);"></a></td>
                                                                        <td valign="top">
                                                                            <a class="ui-icon ui-icon-closethick" title="Clear All" href="javascript:removeAllDims()" style="background-color:rgb(139, 195, 74);"></a>
                                                                        </td></tr>
                                                                </table>
                                                            </div>
                                                            <!-- Modified By Bhargavi on 4th may 2016-->
                                                            <ul id="draggedDims" style="height:250px;overflow:auto;background-color:white;">


                                                            </ul>
                                                        </td>

                                                    </tr>
                                                    <tr>
                                                        <td id="factsDrag" valign="top">
                                                            <div style="height:auto" class="themeColor draggedDivs ui-corner-all">
                                                                <b>Drag Related Tables To Add As Facts</b><table>
                                                                    <tr>
                                                                        <td valign="top">
                                                                            <a class="ui-icon ui-icon-closethick" title="Clear All" href="javascript:delTables()" style="background-color:rgb(139, 195, 74);"></a>
                                                                        </td></tr></table>
                                                            </div>
                                                            <ul id="draggedFacts" style="height:250px;overflow-y:auto" class="ui-accordion ui-widget ui-helper-reset ui-sortable">

                                                            </ul>
                                                        </td>
                                                    </tr></table>
                                            </td>
                                            <td class="hideTableCell" id="relDBTables" valign="top">
                                                <div style="height:33px" class="themeColor draggedDivs ui-corner-all">
                                                    <b>Related Tables</b>

                                                </div><ul id="bussRelatedTableTree" class="filetree" style="height:550px;overflow-y:auto">
                                                    <li style="background-image:url('images/treeViewImages/plus.gif')"><img src="images/treeViewImages/database.png"><span  id="123">Related Tables</span>
                                                        <ul id="bussRelatedTable">

                                                        </ul>
                                                    </li>
                                                </ul>
                                            </td>

                                            <!-- Susheela code started here-->
                                            <td class="hideTableCell" id="TargetMeasuresDisp" style="display:none;visibility:hidden" valign="top">
                                                <div style="height:33px" class="navtitle-hover draggedDivs ui-corner-all" style="display:none;">
                                                    <b>Available Facts</b>
                                                </div>
                                                <div style="width:auto;height:535px;overflow:auto">
                                                    <ul id="TargetMeasuresDispTree" class="filetree">
                                                        <li style="background-image:url('images/treeViewImages/plus.gif')"><img src="images/treeViewImages/database.png"><span  id="123"></span>
                                                            <ul id="TargetMeasuresDispList">

                                                            </ul>
                                                        </li>
                                                    </ul>
                                                </div>
                                            </td>
                                            <td class="hideTableCell" id="dargTargetMeasuresPanel" style="display:none;visibility:hidden" valign="top" width="65%">
                                                <div style="height:33px" class="navtitle-hover draggedDivs ui-corner-all">
                                                    <font style="font-weight:bold" face="verdana" size="1px">Drag Target Measures To Save</font>
                                                    <table>
                                                        <tr><td valign="top">
                                                                <a class="ui-icon ui-icon-check" title="Save Measure" href="javascript:saveTargetMeasure()" style="background-color:rgb(139, 195, 74);"></a></td>
                                                            <td valign="top">
                                                                <a class="ui-icon ui-icon-closethick" title="Clear All" onclick="javascript:removeAllMeasures()" style="background-color:rgb(139, 195, 74);"></a>
                                                            </td>
                                                        </tr>
                                                    </table>
                                                </div>

                                                <div style="height:535px;overflow:auto" id="showDraggedTargetMeasuresDiv">
                                                    <table>
                                                        <tr>
                                                        <br><br>
                                                        <td>
                                                            <Table><Tr><Td>
                                                                        <div id="innerDraggedTargetDiv">
                                                                            <ul id="innerDraggedTargetUl">

                                                                            </ul>
                                                                        </div></Td></Tr>
                                                            </Table>
                                                        </td>
                                                        </tr>
                                                    </table>
                                                </div>

                                            </td>
                                            <!-- Susheela code ended here-->

                                            <td class="hideTableCell" id="addTabsSrc-1" width="20%" valign="top">
                                                <div style="height:33px" class="navtitle-hover draggedDivs ui-corner-all" >
                                                    <b>Tables</b>
                                                    <table>
                                                        <tr><td valign="top">
                                                                <a class="ui-icon ui-icon-check" title="Add Relations" href="javascript:addBussTables()" style="background-color:rgb(139, 195, 74);"></a></td>
                                                            <td valign="top">
                                                                <a class="ui-icon ui-icon-closethick" title="Clear All" href="javascript:delTables()" style="background-color:rgb(139, 195, 74);"></a>
                                                            </td></tr></table>
                                                </div>
                                                <div style="height:550px;overflow-y:auto">
                                                    <ul id="addTabsToSrcTree" class="ui-accordion ui-widget ui-helper-reset ui-sortable">

                                                    </ul>
                                                </div>

                                            </td>
                                            <td class="hideTableCell" id="addTabsSrc-2" width="30%" valign="top">
                                                <div style="height:33px" class="navtitle-hover draggedDivs ui-corner-all">
                                                    <b>Drag Tables To Add Relations</b>
                                                    <table>
                                                        <tr><td style="background-color:#e6e6e6" valign="top">
                                                                <a class="ui-icon ui-icon-gear" title="Suggest Relations" href="javascript:autoSuggSrcRls('ALL')" style="background-color:rgb(139, 195, 74);"></a></td>
                                                            <td style="background-color:#e6e6e6" valign="top">
                                                                <a class="ui-icon ui-icon-closethick" title="Clear All" href="javascript:saveSrcTblRelations()" style="background-color:rgb(139, 195, 74);"></a>
                                                            </td></tr></table>
                                                </div>
                                                <div style="width:98%;height:90%;overflow:auto">
                                                    <ul id="draggedSrcTabs" style="height:95%;" class="ui-accordion ui-widget ui-helper-reset ui-sortable">
                                                    </ul>
                                                </div>

                                            </td>
                                            <td class="hideTableCell" id="addTabsSrc-3" width="50%" valign="top">
                                                <table style="height:100%;width:100%">
                                                    <tr id="dropSrcDragCols" style="height:50%">
                                                        <td valign="top">
                                                            <div style="height:33px" class="navtitle-hover draggedDivs ui-corner-all">
                                                                <b>Drag Columns To Add Relations</b>
                                                                <table>
                                                                    <tr><td valign="top">
                                                                            <a class="ui-icon ui-icon-check" title="Add Relations" href="javascript:addSrcRelations()" style="background-color:rgb(139, 195, 74);"></a></td>
                                                                        <td valign="top">
                                                                            <a class="ui-icon ui-icon-closethick" title="Clear All" href="javascript:removeAllSrcTables()" style="background-color:rgb(139, 195, 74);"></a>
                                                                        </td>
                                                                    </tr>
                                                                </table>
                                                            </div>
                                                            <div class="myClassTestnew">
                                                                <table id="srcDragColRelTab">
                                                                </table>
                                                            </div>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td valign="top">
                                                            <div style="height:33px" class="navtitle-hover draggedDivs ui-corner-all">
                                                                <b>Commit Relations</b>
                                                                <table>
                                                                    <tr><td valign="top">
                                                                            <a class="ui-icon ui-icon-check" title="Add Relations" href="javascript:checkCartesianTbl()" style="background-color:rgb(139, 195, 74);"></a></td>
                                                                        <td valign="top">
                                                                            <a class="ui-icon ui-icon-closethick" title="Clear All" href="javascript:removeAllRelations()" style="background-color:rgb(139, 195, 74);"></a>
                                                                        </td></tr></table>
                                                            </div>
                                                            <div class="myClassTestnew">
                                                                <div id="relationTxt" style="height:95%">
                                                                </div>
                                                            </div>

                                                        </td>  </tr></table>

                                            </td>

                                            <%--26-sept-09 by uday --%>
                                            <%-- <td class="hideTableCell" id="showEditRelationUI" width="65%" valign="top">
                                                 <table border="1" style="height:100%;width:100%">
                                                     <tr style="height:50%">
                                                         <td width="40%" valign="top">
                                                             <div style="height:33px" class="navtitle-hover draggedDivs ui-corner-all">
                                                                 <font style="font-weight:bold" face="verdana" size="1px">Related Tables</font>
                                                             </div>
                                                             <ul id="relationTablesTree" class="filetree">
                                                                 <li style="background-image:url('images/treeViewImages/plus.gif')"><img src="images/treeViewImages/database.png"><span  id="123">Related Tables</span>
                                                                     <ul id="relationTablesTree">

                                                            <logic:notEmpty name="relatedTablesList" scope="request" >
                                                                <logic:iterate id="relatedTablesList" name="relatedTablesList" scope="request">

                                                                    <li id='relTable-<bean:write name="relatedTablesList" property="relatedTableId"/>'>
                                                                        <img src="images/treeViewImages/bullet_star.png"><span><bean:write name="relatedTablesList" property="relatedTableName"/></span>
                                                                    </li>

                                                                </logic:iterate>
                                                            </logic:notEmpty>

                                                        </ul>
                                                    </li>
                                                </ul>
                                            </td>
                                            <td width="60%" valign="top">
                                                <div style="height:33px" class="navtitle-hover draggedDivs ui-corner-all">
                                                    <font style="font-weight:bold" face="verdana" size="1px">Edit Relation</font>
                                                </div>
                                            </td>
                                        </tr>
                                    </table>
                                </td> --%>

                                            </tr>
                                            </table>


                                            </td>

                                            </tr>
                                            </form>
                                            <div style="display:none" title="Add Time Dimension " id="addMoreTimeDimDiv">

                                            </div>
                                            </table>
                                            <div id="fade" class="black_overlay"></div>
                                            <div id="fade1" class="black_overlay" >

                                                <img id="imgId" src="images/ajax.gif"  width="100px" height="100px"  style="position:absolute;left:600px;top:200px" >

                                            </div>
                                            <%-- <div id="busgrpdataDispDialog" title="Table Data Pad">--%>
                                            <div>
                                                <iframe  id="busgrpdataDisp" NAME='busgrpdataDisp'  STYLE='display:none;'   frameborder="0" class="white_content1" SRC=''></iframe>

                                            </div>
                                            <div >
                                                <iframe  id="bucketDisp" NAME='bucketDisp'  STYLE='display:none;'frameborder="0" height="60%"  class="white_bucket" SRC=''></iframe>
                                            </div>
                                            <div id="createBucketdiv" style="display: none" title="Add Bucket">
                                                <iframe  id="bucketDisp1" NAME='bucketDisp1'  frameborder="0"   height="100%" width="100%"  SRC=''></iframe>
                                            </div>

                                            <%-- added by susheela start--%>
                                            <div id="targetParametersDiv" title="Select Parameters" STYLE='display:none;'>
                                                <iframe  id="targetParameters" NAME='targetParameters'    frameborder="0" height="100%" width="100%" SRC='#'></iframe>
                                            </div>
                                            <div id="editBucketDiv" title="Edit Bucket Details " STYLE='display:none;'>
                                                <iframe  id="editBucketframe" NAME='editBucketframe'    frameborder="0" height="100%" width="100%" SRC='#'></iframe>
                                            </div>
                                            <div id="fade" class="black_overlay"></div>

                                            <%-- added by susheela over--%>

                                            <div >
                                                <iframe  id="timeDimDisp" NAME='timeDimDisp'  STYLE='display:none;'  frameborder="0" class="white_content1" SRC=''></iframe>
                                            </div>
                                            <ul id="myMenubusgrp" class="contextMenu">
                                                <li class="viewTable"><a href="#viewTable"><font class="text_classstyle1">View Table</font></a></li>
                                                <%--      <li class="addFormula"><a href="#addFormula"><font class="text_classstyle1">Add Formula</font></a></li>--%>
                                                <li class="addFilter"><a href="#addFilter"><font class="text_classstyle1">Add Filter</font></a></li>
                                                <li class="editRelation"><a href="#editRelation"><font class="text_classstyle1">Edit Relation</font></a></li>
                                            </ul>
                                            <ul id="grpbucket" class="contextMenu" style="width:auto;text-align:left">

                                                <li class="insert"><a href="#createBuckets">Add Buckets</a></li>
                                                <%--     <li class="insert"><a href="#deleteBuckets">Delete Buckets</a></li> --%>
                                                <li class="insert"><a href="#createDimensions">Measure To Dimension</a></li>
                                                <li class="insert"><a href="#addTimeDim">Map Time Dimension</a></li>
                                                <li class="insert"><a href="#addmoreTimeDim">Add Time Dimension</a></li>
                                                <li class="insert"><a href="#addMemberDesc">Add Member Details</a></li>
                                                <li class="insert"><a href="#addtimebaseformula"><font class="text_classstyle1">Add TimeBased Formula</font></a></li>
                                                <li class="insert"><a href="#viewFormula">View Formula</a></li>
                                                <li class="insert"><a href="#AddEditTargets">Add/EditTargets</a></li>
                                                <li class="insert"><a href="#AddDateFormula">AddDateFormula</a></li>


                                            </ul>

                                            <div id="tableDisplay" title="Table Properties" style="display:none">
                                                <iframe  id="tablePropDisp" NAME='tablePropDisp'  <%--STYLE='display:none;height:480px'--%>  height="100%" width="100%" frameborder="0" SRC='#'></iframe>
                                            </div>
                                            <div>
                                                <iframe  id="showTimeDimDisp" NAME='showTimeDimDisp'  STYLE='display:none' frameborder="0" class="white_content1" SRC=''></iframe>
                                            </div>

                                            <div id="userAssigned" title="Assign To Users">
                                                <iframe  id="userAssignDisp" NAME='userAssignDisp' height="100%" width="100%"  frameborder="0" SRC='#'></iframe>
                                            </div>

                                            <!--Added By Santhosh-->
                                            <script>
    //start by uday
    function changeColor()
    {
        $colObj=$(".draggableColumn");
        //alert($colObj.length);
        $colObj.hover(
        function(){
            this.style.color="red";
        },
        function(){
            this.style.color="black";
        }
    );
    }
    function checkDrag()
    {
        var $dragColumn = $(".draggableColumn");
        var $droppableArea = $("#editRelationPanel");
        $dragColumn.draggable({
            helper:"clone",
            effect:["", "fade"]
        });
        $droppableArea.droppable({
            activeClass:"blueBorder",
            accept:'.draggableColumn',
            drop: function(ev, ui){
                var idContent=ui.draggable.attr("id").split(":");
                var tableName = idContent[2];
                var tableId = idContent[0];
                var colName = ui.draggable.html();
                var colId = idContent[1];
                //alert(tableName+"."+colName);
                var tableColName = tableName+"."+colName;
                var tableColId = tableId+"."+colId;
                //alert("names:  "+tableColName+"  Ids: "+tableColId);
                //var relationId = ui.draggable.parent("span")
                addColumnRelation(tableColName,tableColId);
            }
        });
    }
    //end by uday



    var tableActive;
    var $dragTables=$('#bussTable > li >span')
    var $dimensions=$(".dimensions");
    var $draggableTables=$('#draggableTables');
    var $dimensionDrag=$("#dimensionDrag");
    var $factsDrag=$('#factsDrag');
    var $srcTabDrop=$('#addTabsSrc-2');
    var $dropSrcDragCols=$('#dropSrcDragCols')
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
    $($srcTabDrop).droppable(
    {
        activeClass:"blueBorder",
        accept:'.dragTableName',
        drop: function(ev, ui) {
            var flag=false;
            flag=checkDuplicateTabEntry(ui.draggable.attr("id"));
            if(flag)
                addDragTableToScr(ui.draggable,"canDel")

        }
    });

    $($dropSrcDragCols).droppable(
    {
        activeClass:"blueBorder",
        accept:'.srcTableColumns',
        drop: function(ev, ui) {
            var tempSpanId=(ui.draggable.attr("id")).split("-")
            var p=(ui.draggable.parent("p")).attr("id").split("-");
            var relCode=p[1]+"."+tempSpanId[0];
            var relVal=tempSpanId[1]+"."+ui.draggable.html();
            relVal=(relVal.split("<br>")).join("");
            createSrcRelation(relVal,relCode);
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
            var idTab=ui.draggable.attr("id").split("-");
            var tab=ui.draggable.html();
            $.ajax({
                url: 'businessgroupeditaction.do?groupdetails=checkRelationTables&tableID='+idTab[1],
                success: function(data){
                    if(data=="false"){
                        alert("No relations for table "+tab)

                    }else{


                        if(bussTableNamesDrag.match(tab)==null){
                            count++;

                            // bussTabIdArray.length=count;

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

            });

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

    function checkbusinessgrp(){
        //  alert('sk code')
        window.location.reload(true);
    }





                                            </script>

                                            <div id="dialog" title="Empty Drag Tables?">
                                                <p><span class="ui-icon ui-icon-alert" style="float:left; margin:0 7px 20px 0;"></span>All the items will be cleared. Are you sure?</p>
                                            </div>
                                            <div id="cartesianDialog" title="Empty Drag Tables?">
                                                <p><span class="ui-icon ui-icon-alert" style="float:left; margin:0 7px 20px 0;"></span><div id="cartesianTabs"></div><p/>
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
                                                <p/>

                                            </div>

                                            <div id="createGroup" title="Create New Business Group">

                                                <form>
                                                    <table cellpadding="5">
                                                        <tr><td style="padding:2px;">
                                                                <label for="name" class="label">Group Name</label></td>
                                                            <td><input type="text" onkeyup='groupDesc()'name="grpName" id="grpName" class="text ui-widget-content ui-corner-all"  style="width:149px" /></td></tr>

                                                        <tr><td style="padding:2px;">
                                                                <label for="email" class="label"> Description</label></td><td>
                                                                <textarea name="grpDesc" id="grpDesc" class="textarea ui-widget-content ui-corner-all" style="width:149px;overflow:auto"></textarea></td></tr>
                                                    </table>
                                                    <input type="hidden" name="connId" id="connId" value="<%=connId%> ">
                                                </form>

                                            </div>
                                            <div id="copyGroup" title="New Business Group">

                                                <form>
                                                    <table cellpadding="5">
                                                        <tr><td>
                                                                <label for="name" class="label">Group Name</label></td>
                                                            <td><input type="text" onkeyup='groupCopyDesc()'name="grpNameCopy" id="grpNameCopy" class="text ui-widget-content ui-corner-all"  style="width:149px" /></td></tr>

                                                        <tr><td>
                                                                <label for="email" class="label"> Description</label></td><td>
                                                                <textarea name="grpCopyDesc" id="grpCopyDesc" class="textarea ui-widget-content ui-corner-all" style="width:149px"></textarea></td></tr>
                                                    </table>
                                                    <input type="hidden" name="connId" id="connId" value="<%=connId%> ">
                                                </form>

                                            </div>
                                            <div id="deleteConfirm" title="Delete Business Group">
                                                <b style="font-family:verdana;font-size:12px">This business role have some reports would you like to delete</b>
                                            </div>


                                            <div id="createUserFolder" title="Create New Business Role">
                                                <form>
                                                    <table cellpadding="5">
                                                        <tr><td>
                                                                <label for="name" class="label">Business Role Name</label></td>
                                                            <td><input type="text" onkeyup='javascript:copyfolderDesc()'name="folderName" id="folderName" class="text ui-widget-content ui-corner-all" style="width:149px"/></td></tr>

                                                        <tr><td>
                                                                <label for="email" class="label">Description</label></td><td>
                                                                <textarea name="folderDesc" id="folderDesc" class="textarea ui-widget-content ui-corner-all" style="overflow:auto;width:149px"></textarea></td></tr>
                                                    </table>

                                                </form>
                                            </div>
                                                <!--added by Nazneen for creating Empty role-->
                                                <div id="createUserFolderEmpty" title="Create New Empty Business Role">
                                                <form>
                                                    <table cellpadding="5">
                                                        <tr><td>
                                                                <label for="name" class="label">Business Role Name</label></td>
                                                            <td><input type="text" onkeyup='javascript:copyEmptyfolderDesc()'name="EmptyFolderName" id="EmptyFolderName" class="text ui-widget-content ui-corner-all" style="width:149px"/></td></tr>

                                                        <tr><td>
                                                                <label for="email" class="label">Description</label></td><td>
                                                                <textarea name="EmptyFolderDesc" id="EmptyFolderDesc" class="textarea ui-widget-content ui-corner-all" style="overflow:auto;width:149px"></textarea></td></tr>
                                                    </table>

                                                </form>
                                            </div>
                                            <div id="assignToUser" title="Assign To Users">
                                                <span>Assign Business Group To Users</span>
                                            </div>
                                            <!--
                                            <div id="removeTable" title="Remove The Table?">
                                                <p><span class="ui-icon ui-icon-alert" style="float:left; margin:0 7px 20px 0;"></span>The table will be removed from the list. Are you sure?</p>
                                            </div>
                                            -->

                                            <!-- Formula Pad and Filter div -->
                                            <!--                                            added by ramesh-->
                                            <div id="quickFormulaDialog" title="Quick Formula" width="650px">
                                                <form name="columnForm" id="columnForm" method="post" action="">
                                                    <%--<input type="hidden" name="groupId" id="groupId" value="<%=group_id%>">--%>
                                                    <%--<input type="hidden" name="connId" id="connId" value="<%=connId%>">--%>

                                                 <table id="QuickFormulaId" class="tablesorter"  border="1px solid" cellpadding="0" cellspacing="0" align="right">
                                                        <thead>
                                                            <tr>
                                                        <th class="navtitle-hover" >Sequence No</th>
                                                        <th class="navtitle-hover" >DisplayName</th>
                                                        <th class="navtitle-hover">Default Aggregation</th>
                                                        <th class="navtitle-hover">sum</th>
                                                        <th class="navtitle-hover" >Avg</th>
                                                        <th class="navtitle-hover" >Min</th>
                                                        <th class="navtitle-hover" >Max</th>
                                                        <th class="navtitle-hover" >count</th>
                                                        <th class="navtitle-hover">CountDistinct</th>
                                                        <th class="navtitle-hover" >RoleFlag</th>
                                                            </tr>
                                                        </thead>

                                                        <tbody  id="addQuicktab" name="addQuicktab">
                                                        

                                                        </tbody>

                                                    </table>

                                                    <center><input type="submit" class="navtitle-hover" style="width:auto;color:black" value="Save" id="btnn" onclick="saveFormula()"/>&nbsp;&nbsp;<input type="button"  class="navtitle-hover" style="width:auto" value="Cancel" onclick="cancelBuckets()"/></center>
                                                </form>
                                            </div>

                                            <div id="formulaDialog" title="Formula Pad">

                                            </div>

                                            <div id="addtimebaseformula" title="Add TimeBased Formula" style="width:515px;height:300px;overflow-y:auto;overflow-x:auto">

                                            </div>

                                            <div id="filterDialog" title="Filter">

                                            </div>
                                             <div id="groupHierarchyDialog" title="GroupHierarchy">

                                             </div>

                                            <%--08-12-09 start susheela--%>
                                            <div id="grpDimDeleteDialog" title="Dimension Delete">
                                                <label><b>Some Roles Use The Selected Dimension. Are you sure you want to delete from the related role.</b></label>

                                            </div>
                                            <div id="grpDimDeleteReportDialog" title="Dimension Delete From Report">
                                                <label><b>Some Reprts Use The Selected Dimension. Are you sure you want to delete such reports.</b></label>

                                            </div>
                                            <%--08-12-09 over susheela--%>

                                            <div id="editTimeDimDialog" title="Time Dimension Pad">
                                                <form>
                                                    <label ><b>Already TimeDimension Exists for This Business Group</b></label>
                                                    <table cellpadding="5">

                                                        <tr><td>
                                                                <label >You want to Map more.... </label></td></tr>
                                                        <tr>
                                                            <td align="center"><input type="button"  class="navtitle-hover" style="width:auto"  value="   Map   " onclick="ShowTimeDim();"></td></tr>

                                                        <%-- <tr><td>
                                                            <label >Create New Instead Of This</label></td><td>
                                                        <input type="button" class="btn" value="Create New  " onclick="createNewTimeDim()"></td></tr>
                                                        --%>
                                                    </table>

                                                </form>
                                            </div>
                                            <%String Query = " SELECT CONNECTION_ID, CONNECTION_NAME FROM PRG_USER_CONNECTIONS";
                                                        PbReturnObject pbro1 = new PbDb().execSelectSQL(Query);
                                            %>
                                            <div>
                                                <div>
                                                    <iframe  id="businessgrptab" NAME='businessgrptab' style="height:600px;width:100%;display:none" SRC='#' frameborder="0" marginheight="0" marginwidth="0"></iframe>
                                                </div>
                                            </div>
                                            <div id="addGroupsDialog" title="Add Groups">
                                                <form name="addGroupForm" id="addGroupForm" method="post">
                                                    <table cellpadding="5">
                                                      <tr><td>Group Name :&nbsp;&nbsp; </td>
                                                        <td><input type="text" name="GroupName" id="GroupName"></td>
                                                    </tr>
                                                    <tr><td>&nbsp;&nbsp; </td>
                                                          <td>&nbsp;&nbsp;</td>
                                                      </tr>
                                                      <tr><td>&nbsp;&nbsp; </td>
                                                          <td>&nbsp;&nbsp;</td>
                                                      </tr>
                                                      <tr><td>&nbsp;&nbsp; </td>
                                                          <td align="center"><input type="button" value="Save" onclick="saveGroups()"></td>
                                                    </tr>
                                                </table>
                                                </form>
                                             </div>
                                            <div id="selectConnection" title="Select Connection">
                                                <form name="myFormcon" id="myFormcon" method="post">
                                                    <table cellpadding="5">
                                                        <tr><td>
                                                                <label for="name" class="label">Connection Name</label></td>
                                                            <td> <select id="connId1" name="connId1" style="width:146px">
                                                                    <%for (int i1 = 0; i1 < pbro1.getRowCount(); i1++) {%>
                                                                    <option value="<%=pbro1.getFieldValueInt(i1, 0)%>"><%=pbro1.getFieldValueString(i1, 1)%></option>
                                                                    <%}%>
                                                                </select></td></tr>

                                                    </table>
                                                    <%--<input type="button"  value="Connect" onclick="saveTables()">--%>
                                                </form>

                                            </div>
                                            <!--Right Click Context Menu-->
                                            <ul id="groupListMenu" class="contextMenu" style="width:auto;text-align:left">

                                                <li class="insert"><a href="#createGroup">Create Business Group</a></li>
                                                <li class="insert"><a href="#deleteGroup">Delete Business Group</a></li>

                                            </ul>

                                            <ul id="userFolder" class="contextMenu" style="width:auto;text-align:left">
                                                <li class="insert"><a href="#createUserFolder">Create Business Role</a></li>
                                                <li class="insert"><a href="#deletebusinessrole">Delete Business Group</a></li>
                                                <!--                                                <li class="insert"><a href="#assignToUser">Assign To Users</a></li>-->
                                                <li class="insert"><a href="#copyBussGroup">Copy Business Group</a></li>
                                                <li class="insert"><a href="#migrateChangesToBussGroup">Migrate Changes To Business Roles</a></li>

                                                <%-- 16-12-09 start--%>
                                                <li class="insert"><a href="#editBussGroup">Edit Business Group</a></li>
                                                <li class="insert"><a href="#createUserFolderEmpty">Create Empty Business Role</a></li>
                                                <%-- 16-12-09 start--%>
                                            </ul>
                                            <ul id="groupTableMenu" class="contextMenu" style="width:auto;text-align:left">

                                                <li class="insert"><a href="#addTables">Add Tables</a></li>
                                            </ul>

                                            <ul id="groupDimMenu" class="contextMenu" style="width:auto;text-align:left">

                                                <li class="insert"><a href="#addDimensions">Add Dimensions</a></li>
                                                <li class="insert"><a href="#addTimeDimensions">Add Time Dimensions</a></li>
                                            </ul>
                                            <ul id="addTabsToSrc" class="contextMenu" style="width:auto;text-align:left">

                                                <li class="insert"><a href="#addTabsToSrc">Add Tables</a></li>
                                            </ul>

                                              <ul id="grophierarchy" class="contextMenu" style="width:auto;text-align:left">
                                                <li class="insert"><a href="#addMeasures">Add Measures</a></li>
                                            </ul>

                                            <ul id="groupchildmenu" class="contextMenu" style="width:auto;text-align:left">
                                                <li class="insert"><a href="#MagrateGroupHierarchy">Migrate Group Hierarchy To Business Roles</a></li>
                                            </ul>
                                            <ul id="groupsChildMenu" class="contextMenu" style="width:auto;text-align:left">
                                                <li class="insert"><a href="#addChilds">Add Childs</a></li>
                                            </ul>

                                            <ul id="cTableProp" class="contextMenu" style="width:auto;text-align:left">

                                                <li class="insert"><a href="#addTabsToSrcprop">Table Properties</a></li>
                                                <li class="addFormulafact"><a href="#addFormulafact"><font class="text_classstyle1">Add Parameterised Formula</font></a></li>
                                                <li class="addFormulafact1"><a href="#addQuickFormula"><font class="text_classstyle1">Add Quick Formula</font></a></li>
                                                <li class="addFormulafact"><a href="#addFormulafactForSingal"><font class="text_classstyle1">Add Formula</font></a></li>
                                                <li class="addGroup"><a href="#addGroups"><font class="text_classstyle1">Add Groups</font></a></li>
                                                <%--<li class="groupHierarchy"><a href="#groupHierarchy"><font class="text_classstyle1">Group Hierarchy</font></a></li>--%>
                                            </ul>

                                            <ul id="FormulaMenuList" class="contextMenu">
                                                <li class="addFormulafact"><a href="#addFormulafact"><font class="text_classstyle1">Add Formula</font></a></li>
                                            </ul>
                                            <ul id="memberDescMenuList" class="contextMenu" style="width:auto;text-align:left">

                                                <li class="insert"><a href="#addMemberDesc">Add Member Details</a></li>
                                            </ul>
                                            <div id="memberDescDialog" title="Member Details" style="height:100%;width:100%">

                                            </div>
                                            <%--added by susheela start --%>
                                            <ul id="targetMenu" class="contextMenu" style="width:auto;text-align:left">
                                                <li class="insert"><a href="#showTargetMeasures">Add Target Measure</a></li>
                                            </ul>

                                            <%--added by susheela start 28-12-09 --%>
                                            <div id="targetDialog" title="Target" style="height:100%;width:100%">

                                            </div>
                                            <ul id="createTargetMenu" class="contextMenu" style="width:auto;text-align:left">
                                                <%-- <li class="insert"><a href="#showcreatetarget">Define Target</a></li> --%>

                                            </ul>

                                            <ul id="targetEditMenu" class="contextMenu" style="width:auto;text-align:left">
                                                <li class="insert"><a href="#showviewtarget">View Target</a></li>
                                                <li class="insert"><a href="#showedittarget">Edit Target</a></li>
                                            </ul>

                                            <div id="migrateChangesDiv" title="Migrate Changes To Business Roles" STYLE='display:none;'>
                                                <iframe  id="migrateChangesData" NAME='migrateChangesData' frameborder="0" height="100%" width="100%"  frameborder="0" SRC='#'></iframe>
                                            </div>
                                            <div id="fade" class="black_overlay"></div>

                                            <ul id="BgDimensionMenu" class="contextMenu">

                                                <li class="insert"><a href="#deleteBgDimension">Delete Dimension</a></li>
                                            </ul>

                                            <ul id="bucketChildMenu" class="contextMenu" style="width:auto;text-align:left">

                                                <li class="insert"><a href="#editBucket">Edit Bucket</a></li>
                                            </ul>
                                            <div id="copyBussGroupDiv">
                                                <iframe  id="copyBussGroupFrame" NAME='copyBussGroupFrame'  STYLE='display:none;'   class="white_content1" SRC='#'></iframe>
                                            </div>
                                            <div id="fade" class="black_overlay"></div>
                                            <div id="editBgDiv" title="Edit Business Group" STYLE='display:none;'>
                                                <iframe  id="editBgData" NAME='editBgData' frameborder="0" height="100%"  frameborder="0"  width="100%" SRC='#'></iframe>
                                            </div>
                                            <div id="fade" class="black_overlay"></div>

                                            <ul id="formulaViewListMenu" class="contextMenu" style="width:100px">
                                                <li class="view"><a href="#view">View</a></li>
                                            </ul>
                                            <div id="formulaViewDiv" title="View Custom Measure" style="display:none">
                                                <table >
                                                    <tr>
                                                        <td id="value"></td>
                                                    </tr>
                                                </table>
                                            </div>

                                            <div id="targetDiv" title="Add/Edit Targets" style="display:none">
                                                <form id="tragetform" name="tragetform" action="" method="post">
                                                <table width="100%" id="tid">
                                                    <tr>
                                                        <td width="30%" align="left"><b>Measures:</b>
                                                            <input type="text" name="measure" id="idvale" readonly/>
                                                            <input type="hidden"name="busscolid" id="bussid"/>
                                                             </td>
                                                             <td width="10%" align="right" id="targetid1"><b><font size="6">Dimensions:</font></b>
                                                                 <select id="dimentionList" name="dimentionList" onchange="selectdimensions()">

                                                            </select> </td>

                                                    </tr>
                                                </table>
                                                <table  width="100%">
                                                    <tr>
                                                        <td><b>Duration:</b>
                                                            <select id="durationSelect" name="durationSelect" onchange="selecting()">
                                                                <option value="" >--SELECT--</option>
                                                                <option value="Month" >Month</option>
                                                                <option value="Quater">Quater</option>
                                                                <option value="year">year</option>
                                                                <option value="Week">Week</option>

                                                            </select></td>
                                                            <td align="right"><input class="navtitle-hover" type="button" name="done" value="Done" onclick="saveTargetDetails()"></td>
                                                 </tr>
                                                 <%--<img alt=""  src="<%=request.getContextPath()%>/tablesorter/addons/pager/icons/first.png"class="first"/>
                             <img alt=""  src="<%=request.getContextPath()%>/tablesorter/addons/pager/icons/prev.png" class="prev"/>
                            <input type="text" readonly class="pagedisplay" style="width:60px"/>
                            <img alt=""  src="<%=request.getContextPath()%>/tablesorter/addons/pager/icons/next.png" class="next"/>
                            <img alt=""  src="<%=request.getContextPath()%>/tablesorter/addons/pager/icons/last.png" class="last"/>
                            <select class="pagesize" id="selPagerRep">
                                <option selected value="10">10</option>
                                <option value="15">15</option>
                                <option id="allReps" value="">All</option>

                            </select>--%>
                                                 </table>

                                                <table style="display: none" id="showdateId">
                                                    <tr>
                                                        <td><b>Month:</b>
                                                            <select id="datefortarget" >
                                                               <option value="" >--SELECT--</option>
                                                               <option value="January" >January</option>
                                                               <option value="February">February</option>
                                                               <option value="March">March</option>
                                                               <option value="April">April</option>
                                                               <option value="May">May</option>
                                                               <option value="June">June</option>
                                                               <option value="July">July</option>
                                                               <option value="August">August</option>
                                                               <option value="September">September</option>
                                                               <option value="October">October</option>
                                                               <option value="November">November</option>
                                                               <option value="December">December</option>

                                                           </select>

                                                        </td><td>
                                                            <select name="yearpicker" id="yearpicker" style="display: none"></select>
                                                        </td>

                                                    </tr>

                                                </table>

                                               <%--// </div>--%>
                                               <br><br>


                                               <div id="targetDiv1" style="display:none">
                            <div id="pagermodifyMeasures" class="pager" align="left" >
                            <img src="<%=request.getContextPath()%>/tablesorter/addons/pager/icons/first.png" class="first"/>
                            <img src="<%=request.getContextPath()%>/tablesorter/addons/pager/icons/prev.png" class="prev"/>
                            <input type="text" readonly class="pagedisplay" style="width:60px"/>
                            <img alt=""  src="<%=request.getContextPath()%>/tablesorter/addons/pager/icons/next.png" class="next"/>
                            <img alt=""  src="<%=request.getContextPath()%>/tablesorter/addons/pager/icons/last.png" class="last"/>
                            <select class="pagesize" id="selPagerRep">
                                <option selected value="10">10</option>
                                <option value="15">15</option>
                                <option id="allReps" value="">All</option>

                            </select></div>
                                              <table border="1" width="400%" height="400%" id="targetDetails" class="tablesorter" >
                                                  <thead>
                                                        <tr>
                                                            <th class="migrate" width="30%" id="durationId"> Duration Details</th>
                                                             <th class="migrate"  width="30%" id="memberId">Members</th>
                                                             <th  class="migrate" id="targetId">Targets</th>

                                                        </tr>
                                                  </thead>
                                                  <tbody>

                                                  </tbody>

                                                    </table>

                                               </div>

                   </form>
                                            </div>

                                            <!-- End -->
               <input type="hidden" name="h" id="h" value="<%=request.getContextPath()%>">
                <%--start by ramesh--%>
                     <div id="dateDivId" title="DateFormula">
                         <form name="DateForm" id="DateForm">
                         <table>
                        <tr>
                            <td>
                                <label class="label">Aggregation&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;:</label>
                            </td>
                            <td>
                                <select name="aggregationtype">
                                    <option>sum</option>
                                    <option>Avg</option>
                                    <option>Min</option>
                                    <option>Max</option>
                                    <option>Count</option>
                                    <option>COUNTDISTINCT</option>

                                </select>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <label class="label">Name&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;:</label>
                            </td>
                            <td>
                                <input type="text" name="datetext" id="datetextid" size="28"/>
                            </td>
                            <td>
                                <input type="hidden" name="busstableId" id="busstableId" size="28" value=""/>
                            </td>
                            <td>
                                <input type="hidden" name="connid" id="connid" size="28" value=""/>
                            </td>
                            <td>
                                <input type="hidden" name="busscolumnname" id="busscolumnname" size="28" value=""/>
                            </td>
                            <td>
                                <input type="hidden" name="tablename" id="tablename" size="28" value=""/>
                            </td>
                            <td>
                                <input type="hidden" name="BussColId" id="BussColId" size="28" value=""/>
                            </td>
                        </tr>
                        <tr>
                            <td style="width:80px" style="display:none;">
                                <label class="label" >Date&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;:</label>
                            </td>
                            <td>
                                <select id="dateformulaid" name="datecolumn">

                                </select>
                            </td>

                        </tr>
                         </table>
                             <%-- <input type="submit" value="Save" onclick="saveDateFormula()" class="migrate"/>--%>
                             <center><input type="button" class="navtitle-hover" style="width:auto;color:black" value="Save" onclick="saveDateFormula()"/>&nbsp;&nbsp;<input type="button"  class="navtitle-hover" style="width:auto" value="Cancel" onclick="cancelBuckets()"/></center>
                         </form>
                            </div>
                            <div>
                                 <iframe  id="grouphiid" NAME='grouphiid'  STYLE='display:none;'   frameborder="0" class="white_content1" SRC=''></iframe>

                                            </div>
                     <%--end by ramesh--%>

                     <div id="childDiv">

                     </div>

                       <div id='loadingmetadata' class='loading_image' style="display:none;">
                    <img alt=""  id='imgId' src='images/help-loading.gif'  border='0px' style='position:absolute; left: 450px; top: 150px;'/>
                    </div>
<script type="text/javascript">
            //added by susheela on 11jan-10
            var targetId="";
            var tarMinTimeLevel="";
            var grpID;
            var connId;

            //added by susheela 28-12-09 satrt
            var measureId="";
            var quickFormulaTabId="";
            var grupId="";
            var groupId="";
            var tableId="";

            function cancelTarget()
            {
                $('#targetDialog').dialog('close');
            }

            function checkGroupDimDelete()
            {
                //alert('businessgroupeditaction.do?groupdetails=getGroupDimDeleteStatus&delGrpId='+delGrpId+'&delDimId='+delDimId);
                $.ajax({
                    url: 'businessgroupeditaction.do?groupdetails=getGroupDimDeleteStatus&delGrpId='+delGrpId+'&delDimId='+delDimId,
                    success: function(data) {
                        //alert('data '+data);
                        if(data==3)
                        {
                            //alert('The Dimension is being used in some role so it cant be deleted.');
                            var count=9;
                            if(count>0)
                            {
                                $('#grpDimDeleteDialog').dialog('open');
                                count=0;
                            }
                        }
                        else if(data==2)
                        {
                            alert('The dimension is deleted successfully.');
                            window.location.reload(true);
                        }
                        else if(data==1)
                        {
                            // alert('The Dimension is being used in some reports so it cant be deleted.');
                            var count=9;
                            if(count>0)
                            {
                                $('#grpDimDeleteReportDialog').dialog('open');
                                count=0;
                            }
                        }

                    }
                });
            }

            function changeGrpMemberSt(memId)
            {
                // alert('memId '+memId);
                $.ajax({
                    url: 'businessgroupeditaction.do?groupdetails=updateGrpDimMemberStatus&memberId='+memId,
                    success: function(data) {
                        if(data==1)
                        {

                        }
                    }
                });
            }
            function changeGrpDimSt(dimId)
            {
                // alert('dimId '+dimId);
                $.ajax({
                    url: 'businessgroupeditaction.do?groupdetails=updateGrpDimStatus&dimensionId='+dimId,
                    success: function(data) {
                        if(data==1)
                        {

                        }
                    }
                });
            }
            function changeGrpFactSt(bussTabId)
            {
                //alert('bussTabId '+bussTabId);
                $.ajax({
                    url: 'businessgroupeditaction.do?groupdetails=updateGrpFactStatus&bussTabId='+bussTabId,
                    success: function(data) {
                        if(data==1)
                        {

                        }
                    }
                });
            }
            function checkFactAndDimOnRoleCreation(grpId)
            {

                // alert('grpId '+grpId);
                $.ajax({
                    url: 'businessgroupeditaction.do?groupdetails=checkFactAndDimOnRoleCreation&grpId='+grpId,
                    success: function(data) {
                        if(data==1)
                        {
                            // alert('data '+data);
                            $('#createUserFolder').dialog('open');
                        }
                        else
                        {
                            alert(data);
                        }
                    }
                });
            }
            function checkTimeDimForBusGroup()
            {
                $.ajax({
                    url: 'businessgroupeditaction.do?groupdetails=checkTimeDimForBusGroup&grpId='+grpId,
                    success: function(data) {
                        //alert('data '+data);
                        if(data==1)
                        {
                            checkFactAndDimOnRoleCreation(grpId);
                        }
                        else
                        {
                            alert('The time dimension is not mapped for the business group.');
                        }
                    }
                });
            }

            function checkReportDelete()
            {
                alert(delGrpId+' in rep Delete '+delDimId);//deleteGroupDimension
                $.ajax({
                    url: 'businessgroupeditaction.do?groupdetails=deleteGroupDimension&delGrpId='+delGrpId+'&delDimId='+delDimId,
                    success: function(data) {
                        if(data==1){
                            alert('Dimension Deleted Successfully.');
                            window.location.reload(true);
                        }
                    }
                });
            }

            function refreshSaveEdit()
            {
                window.location.reload(true);
                $("#editBgDiv").dialog('close');
            <%--document.getElementById("editBgData").style.display='none';
            document.getElementById('fade').style.display='none';--%>
                }

                function cancelGrpEditParent()
                {   $("#editBgDiv").dialog('close');
            <%-- document.getElementById("editBgData").style.display='none';
             document.getElementById('fade').style.display='none';--%>
                 }


                 //start susheela start 05-12-09

                 var delDimId="";
                 var delGrpId="";
                 //over susheela

                 //start susheela
                 function cancelMigrationToRole()
                 {
                     $("#migrateChangesDiv").dialog('close');
            <%--document.getElementById("migrateChangesData").style.display='none';
            document.getElementById('fade').style.display='none';--%>
                }
                function cancelDiv(){
                    $("#targetParametersDiv").dialog('close');
            <%--document.getElementById("targetParameters").style.display='none';
            document.getElementById('fade').style.display='none';--%>

                }
            <%--  function cancelMeasureMemDiv()
              {
                  document.getElementById("measureMembers").style.display='none';
                  document.getElementById('fade').style.display='none';
              } --%>
                  function updateTheBussGroupCol(tabcolId)
                  {
                      //alert('tabcolId '+tabcolId);
                      var allV=tabcolId.split(",");
                      var first="";
                      var last="";
                      for(var l=0;l<allV.length;l++)
                      {
                          if(l==0)
                              first=allV[l];
                          if(l==allV.length-1)
                              last=allV[l];
                      }
                      // alert(first+";first;; "+last);
                      var lastV = last.split("~");
                      var tab="";
                      var col="";
                      for(var l=0;l<lastV.length;l++)
                      {
                          if(l==lastV.length-1)
                              tab=lastV[l];
                      }

                      col=first.substring(0,first.length);
                      //alert(tab+'tab ecol '+col)
                      $.ajax({
                          url: 'targetmeasuresaction.do?targetMeasures=updateFactColumnFlag&tableId='+tab+'&columnId='+col,
                          success: function(data) {
                              if(data=="false")
                                  alert("Unable to Save The role Flag")
                              else{

                              }

                          }
                      });
                      // alert(tab+" tab "+col);
                  }
                  function removeAllMeasures()
                  {
                      document.getElementById('innerDraggedTargetUl').innerHTML = "";
                      AllSelectedTabCols="";
                  }

                  function refreshPage()
                  {
                      setTimeout('reloadTimer()',7000)
                      //document.myFormnew.action="getAllBusinessGroups.do";
                      //document.myFormnew.submit();
                  }
                  function reloadTimer()
                  {
                      window.location.reload(true);
                  }


                  //end susheela

                  var bustableId="";
                  var grpId="";
                  var bucketDetails="";
                  var timeDimDetails="";

                  //01-Oct-09 added by susheela start
                  var newTab;
                  var AllSelectedTabCols="";

                  var measureName="";
                  var gId = "";

                  function saveTargetMeasure(){
                      var frameObj=document.getElementById("targetParameters");
                      //  frameObj.style.display='block';
                      // document.getElementById('fade').style.display='block';
                      var source = "pbShowMeasureParameters.jsp?AllSelectedTabCols="+AllSelectedTabCols+"&busGroup="+grpId;
                      frameObj.src=source;
                      $("#targetParametersDiv").dialog('open');
                  }

                  function saveTM(){
                      $.ajax({
                          url: 'targetmeasuresaction.do?targetMeasures=saveTargetFacts&AllSelectedTabCols='+AllSelectedTabCols+'&busGroup='+grpId,
                          success: function(data) {
                              if(data=="false")
                                  alert("Unable to Save Target Fact")
                              else{

                                  var ob=document.getElementById("TargetMeasuresDisp");
                                  ob.style.visibility="hidden";
                                  ob.style.display="hideTableCell"; //dargTargetMeasuresPanel
                                  var ob2=document.getElementById("dargTargetMeasuresPanel");
                                  //document.getElementById("dargTargetMeasuresPanel")="";
                                  ob2.style.visibility="hidden";
                                  ob2.style.display="hideTableCell";
                                  alert("Target Fact Created successfully")
                              }
                          }
                      });
                  }

                  $(document).ready(function() {
                      $("#grpDimDeleteDialog").dialog({
                          bgiframe: true,
                          autoOpen: false,
                          resizable: false,
                          height:140,
                          position:'top',
                          modal: true,
                          overlay: {
                              backgroundColor: '#000',
                              opacity: 0.5
                          },

                          buttons: {
                              Cancel: function() {
                                  $(this).dialog('close');
                              },
                              'Ok': function() {
                                  $(this).dialog('close');
                                  checkReportDelete();

                              }
                          }
                      });

                      $("#grpDimDeleteReportDialog").dialog({
                          bgiframe: true,
                          autoOpen: false,
                          resizable: false,
                          height:140,
                          position:'top',
                          modal: true,
                          overlay: {
                              backgroundColor: '#000',
                              opacity: 0.5
                          },

                          buttons: {
                              Cancel: function() {
                                  $(this).dialog('close');
                              },
                              'Ok': function() {
                                  $(this).dialog('close');
                                  grpReportDimDelete();

                              }
                          }
                      });

                      $(".BgDimensionDiv").contextMenu(
                      { menu: 'BgDimensionMenu', leftButton: true
                      }, function(action,el,pos) {
                          // targetMeasure
                          var val=el.attr("id");
                          var allV=val.split("~");
                          delGrpId=allV[0];
                          delDimId =allV[1];

                          //$(el).parent().attr('id');
                          //alert(delGrpId+' delGrpId delDimId./// ...==..... '+delDimId);
                          contextMenuWork(action,el.parent("li"), pos);

                      });






            <%--  $(".bucketEditMenu").contextMenu(
               { menu: 'bucketChildMenu', leftButton: true
               }, function(action,el,pos) {
                   // targetMeasure
                   var val=el.attr("id");
                    alert("in bucketEditMenu"+ val)
                   var allV=val.split("~");
                   delGrpId=allV[0];
                   delDimId =allV[1];
                   //alert('val '+val);
                   //$(el).parent().attr('id');
                   //alert(delGrpId+' delGrpId delDimId./// ...==..... '+delDimId);
                   contextMenuWork(action,el.parent("li"), pos);

                      });--%>

                              $("#copyGroup").dialog({
                                  bgiframe: true,
                                  autoOpen: false,
                                  height:250,
                                  width:300,
                                  modal: true,
                                  buttons: {
                                      Cancel: function() {
                                          $(this).dialog('close');
                                      },
                                      'Ok': function() {
                                          copyBussinessGroupFrame();

                                      }
                                  },
                                  close: function() {

                                  }
                              });
                              // added by susheela start

                              $(".targetDiv").contextMenu(
                              { menu: 'targetMenu', leftButton: true
                              }, function(action,el,pos) {
                                  // targetMeasure
                                  grpId=$(el).parent().attr('id');
                                  contextMenuWork(action,el.parent("li"), pos);

                              });

                              //added on 11jan2010
                              $(".targetIdDiv").contextMenu(
                              { menu: 'targetEditMenu', leftButton: true
                              }, function(action,el,pos) {
                                  // targetMeasure
                                  var all=el.attr('id');
                                  var allV=all.split(":");
                                  targetId=allV[0];
                                  tarMinTimeLevel=allV[1];
                                  contextMenuWork(action,el.parent("li"), pos);

                              });

                              $(".createTargetDiv").contextMenu(
                              { menu: 'createTargetMenu', leftButton: true
                              }, function(action,el,pos) {
                                  // targetMeasure
                                  measureId=el.attr('id');
                                  contextMenuWork(action,el.parent("li"), pos);

                              });





                              $("#formulaViewDiv").dialog({
                                  //bgiframe: true,
                                  autoOpen: false,
                                  height: 150,
                                  width: 250,
                                  position: 'absolute',
                                  modal: true
                              });

                              $("#targetDiv").dialog({
                                  //bgiframe: true,
                                  autoOpen: false,
                                  height: 500,
                                  width: 700,
                                  position: 'absolute',
                                  modal: true
                              });
                              <%--$("#targetDiv1").dialog({
                                  //bgiframe: true,
                                  autoOpen: false,
                                  height: 500,
                                  width: 700,
                                  position: 'absolute',
                                  modal: true
                              });--%>
                              $("#addMoreTimeDimDiv").dialog({
                                  //bgiframe: true,
                                  autoOpen: false,
                                  height: 550,
                                  width: 650,
                                  position: 'absolute',
                                  modal: true
                              });


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
                                  //alert( $(el).parent().attr('id'));
                                  bustableId=$(el).parent().attr('id');
                                  //connId=$(el).parent().parent().parent().parent().attr('id');
                                  //alert($(el).parent().attr('id'));
                                  //alert($(el).parent().parent().attr('id'));
                                  //alert($(el).parent().parent().parent().attr('id'));
                                  //alert($(el).parent().parent().parent().parent().attr('id'));

                                  contextMenuWork(action, el.parent("li"), pos);

                              });

                              $(".bucketMenu").contextMenu({ menu: 'grpbucket', leftButton: true }, function(action, el, pos) {
                                  //alert($(el).parent().parent().attr('id'))


                                  contextMenubucket(action,el.parent("li"), pos);


                              });

                              $(".tableProperty").contextMenu({ menu: 'cTableProp', leftButton: true }, function(action, el, pos) {
                                  // alert(el.html()+"***"+el.attr("id"))
                                  contextMenuWork3(action, el.parent("li"), pos);
                                  //alert(el.html()+"***"+el.parent.attr("id"))
                              });

                              $(".assignUserMenu").contextMenu({ menu: 'userAssignListMenu', leftButton: true }, function(action, el, pos) {
                                  // alert(el.html()+"***"+el.attr("id"))
                                  contextMenuWorkAssignUser(action, el.parent("li"), pos);
                                  //alert(el.html()+"***"+el.parent.attr("id"))
                              });

                              $(".createFolderMenu").contextMenu({ menu: 'userFolder', leftButton: true }, function(action, el, pos) {
                                  contextMenuFolder(action,el.parent("li"), pos);
                              });

                              $(".formulaMenu").contextMenu({ menu: 'FormulaMenuList', leftButton: true }, function(action, el, pos) {
                                  contextMenuFormula(action,el, pos);
                              });

                              $(".memberDescMenu").contextMenu({ menu: 'memberDescMenuList', leftButton: true }, function(action, el, pos) {
                                  contextMenuWork(action,el, pos);
                              });

                              $(".bucketEditMenu").contextMenu({ menu: 'bucketChildMenu', leftButton: true }, function(action, el, pos) {
                                  contextEditBucketMenu(action,el, pos);
                              });
                              $(".grouphierarchyid").contextMenu({ menu: 'grophierarchy', leftButton: true }, function(action, el, pos) {
                                  contextEditGroupHierarchyMenu(action,el, pos);
                              });
                              $(".groupHierarechyclick").contextMenu({ menu: 'groupchildmenu', leftButton: true }, function(action, el, pos) {
                                  contextMigrateGroupHierarchyMenu(action,el, pos);
                              });
//                              $(".grouphierarchyid1").contextMenu({menu: 'groupsChildMenu',leftButton: true }, function(action, el, pos) {
//                                    contextEditChildHierarchyMenu(action,el, pos);
//                                });

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
                                      Cancel: function() {
                                          $(this).dialog('close');
                                      },
                                      'Create Business Role': function() {
                                          createUserFolder();

                                      }
                                  },
                                  close: function() {

                                  }
                              });
                              //added by Nazneen for creating empty business role
                              $("#createUserFolderEmpty").dialog({
                                  bgiframe: true,
                                  autoOpen: false,
                                  height:250,
                                  width:300,
                                  modal: true,
                                  buttons: {
                                      Cancel: function() {
                                          $(this).dialog('close');
                                      },
                                      'Create Empty Business Role': function() {
                                          createUserFolderEmpty();

                                      }
                                  },
                                  close: function() {

                                  }
                              });

                              $("#targetDialog").dialog({
                                  //bgiframe: true,
                                  autoOpen: false,
                                  height: 400,
                                  width: 500,
                                  position: 'top',
                                  modal: true
                              });

                              $("#deleteConfirm").dialog({
                                  bgiframe: true,
                                  autoOpen: false,
                                  height:200,
                                  width:250,
                                  modal: true,
                                  buttons: {
                                      Cancel: function() {
                                          $(this).dialog('close');
                                      },
                                      'OK': function() {
                                          //el.attr("id");
                                          //-- Here my ajax function should come
                                          Deleteselctedbusroles()
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
                          var usrgrpid="";
                          function groupCopyDesc(){
                              var $grpName=$("#grpNameCopy");
                              var $grpDesc=$("#grpCopyDesc");
                              $grpDesc.val($grpName.val());
                          }
                          function copyBussinessGroupFrame()
                          {

                              var connId=document.getElementById("connId1").value;
                              $('#copyGroup').dialog('close');
                              var groupName=$("#grpNameCopy").val();
                              var grpDesc=$("#grpCopyDesc").val();
                              groupName=groupName.replace("&","'|| chr(38) ||'","gi");
                              grpDesc=grpDesc.replace("&","'|| chr(38) ||'","gi");
                              //alert(grpId+'oooo '+connId);

                              // alert(groupName+" groupName "+grpDesc);
                              $.ajax({
                                  url: 'businessgroupeditaction.do?groupdetails=checkGroupNameForConn&groupName='+groupName+'&connId='+connId,
                                  success: function(data) {
                                      //alert(' data '+data);
                                      if(data==1){
                                          alert('The groupName already exists for this connection.');

                                      }
                                      else if(data==2){
                                          //alert('in save .');migrateChangesData
                                          var frameObj=document.getElementById("copyBussGroupFrame");
                                          frameObj.style.display='block';
                                          document.getElementById('fade').style.display='block';
                                          var source = "pbCopyBussGroup.jsp?connId="+connId+"&groupName="+groupName+'&grpDesc='+grpDesc+'&grpId='+grpId;
                                          frameObj.src=source;
                                      }

                                  }

                              });
                          }

                          function cancelCopyRefresh()
                          {
                              document.getElementById("copyBussGroupFrame").style.display='none';
                              document.getElementById('fade').style.display='none';
                              window.location.reload(true);
                          }

                          function cancelGrpCopyParent()
                          {
                              document.getElementById("copyBussGroupFrame").style.display='none';
                              document.getElementById('fade').style.display='none';
                              // window.location.reload(true);
                          }
                          function copyBussinessGroup(connId,grp){
                              grpId=grp;
                              //alert('in copy '+connId+' --- '+grp);
                              var $grpName=$("#grpNameCopy");
                              var $grpDesc=$("#grpCopyDesc");
                              $grpName.val("");
                              $grpDesc.val("");
                              $('#copyGroup').dialog('open');
                          }

                          function contextMenuFormula(action, el, pos) {

                              switch (action) {
                                  case "addFormulafact":
                                      {
                                          var connId=document.getElementById("connId").value;
                                          //  alert('in alert '+el.attr('id')+'connId-'+connId);
                                          var grpId=el.attr('id');
//                                          alert(grpId)
                                          document.getElementById("grpId").value=grpId;
                                          document.getElementById("formulaDialog").innerHTML = "<iframe width='100%' height='100%'  frameborder='0' src=columnFormula.jsp?connId="+connId+"&grpId="+grpId+" ></iframe>";
                                          $('#formulaDialog').dialog('open');

                                          break;
                                      }
                              }
                          }


                          var usrgrpid="";
                          function contextMenuFolder(action, el, pos) {

                              switch (action) {
                                  case "createUserFolder":
                                      {
                                          //alert(el.attr("id"));
                                          grpId=el.attr("id");
                                          addUserFolder();
                                          break;
                                      }
                                      //added by Nazneen for creating empty business role
                                      case "createUserFolderEmpty":
                                      {
                                          //alert(el.attr("id"));
                                          grpId=el.attr("id");
                                          addUserFolderEmpty();
                                          break;
                                      }
                                  //                                  case "assignToUser":
                              //                                      {
                          //                                          // alert("assignToUser")
                      //                                          contextMenuWorkAssignUser(action, el, pos);
                  //                                          // $('#assignToUser').dialog('open');
              //                                          break;
          //                                      }
      case "deletebusinessrole":
          {
              usrgrpid = el.attr("id");
              //   alert('------'+$(el).parent().attr('id'));
              deletebusinessrole(usrgrpid)
              // alert(el.attr("id"));
              // alert('------'+$(el).parent().attr('id'));
              break;
          }
      // susheela 09-12-09 start
  case "copyBussGroup":
      {
          var grp= el.attr("id");
          var connId=document.getElementById("connId1").value;
          copyBussinessGroup(connId,grp);
          break;
      }
  case "editBussGroup":
      {
          var grp= el.attr("id");
          var connId=document.getElementById("connId1").value;
          //editBussinessGroup(connId,grp);
          var frameObj=document.getElementById("editBgData");
          // frameObj.style.display='block';
          //  document.getElementById('fade').style.display='block';
          var source = "pbEditBusGroup.jsp?grp="+grp;
          $("#editBgDiv").dialog('open');
          frameObj.src=source;
          break;
      }
  case "migrateChangesToBussGroup":
      {
          var grp= el.attr("id");
          //alert('in buss group mig '+grp);

          var frameObj=document.getElementById("migrateChangesData");
          //  frameObj.style.display='block';
          // document.getElementById('fade').style.display='block';
          var source = "PbMigrateChangesToRolesNew.jsp?grp="+grp;
          frameObj.src=source;
          $("#migrateChangesDiv").dialog('open');
          break;
      }
  // susheela 09-12-09 over
}
}
function Deleteselctedbusroles()
{

// var busid = $(el).parent().attr('id');
// var busid=el.attr("id");
var busid = usrgrpid;
//alert(busid);
$.ajax({
url: 'BusinessGroups.do?parameter=Deleteallreports&busid='+busid,
success: function(data) {
  if(data==1)
  {
      window.location.reload();
      alert("Reports Deleted Successfully");
  }
}
});
}



function deletebusinessrole(usrgrpid){
// alert("delete Business roles");
var userid = document.getElementById("loginuser").value;
// alert(usrgrpid);
// alert(userid);
$.ajax({
url: 'BusinessGroups.do?parameter=deletebusinessrole&userid='+userid+"&deletingid="+usrgrpid,
success: function(data) {
  //  alert(data);
  if(data==1)
  {
      alert("You dont have privilage to delete.");
  }
  if(data==2)
  {
      $("#deleteConfirm").dialog('open');
      /*var commit = confirm("Do You Like To Delete Confirmly.");
                               if(commit == true){
                               alert("Business Role was Not Deleted.");
                                   }
                                   else if(commit == true){
                                           alert("Business Role Deleted Successfully.");-
                                       }*/
  }
}
});
}

//added by susheela start
function dragTarget(Obj){
//alert('in drag target//..')
var $draglist=$(".dragTargetMeasures");
var $droptargetLeft=$("#showDraggedTargetMeasuresDiv");
$draglist.draggable({
helper:"clone",
effect:["", "fade"]
});
$droptargetLeft.droppable({activeClass:"blueBorder",
drop: function(ev,ui) {

  var all = ui.draggable.attr('id');
  var allVals = all.split("~");
  for(var l=0;l<allVals.length;l++){
      if(l==2)
          newTab = allVals[l];
  }
  AllSelectedTabCols = AllSelectedTabCols+","+ui.draggable.attr('id');

  addTarget(ui.draggable);
}
});

}
function addTarget(target){

var strUl = document.getElementById("innerDraggedTargetUl");
var strLi = "<li id=\""+newTab+"."+target.html()+"\" class=\"navtitle-hover\" style='color:white'>&nbsp;<font color='black'>"+newTab+"."+target.html()+"</font></li>";
strUl.innerHTML += strLi;
}
//added by susheela over



function addUserFolder(){
var $foldName=$("#folderName");
var $foldDesc=$("#folderDesc");
$foldName.val("");
$foldDesc.val("");
$('#createUserFolder').dialog('open');
}
//added by Nazneen for creating empty business role
function addUserFolderEmpty(){
var $foldName=$("#folderName");
var $foldDesc=$("#folderDesc");
$foldName.val("");
$foldDesc.val("");
$('#createUserFolderEmpty').dialog('open');
}
function assignBussGrpToUser(){

}
function copyfolderDesc(){
var $foldName=$("#folderName");
var $foldDesc=$("#folderDesc");
$foldDesc.val($foldName.val());
}
//added by Nazneen
function copyEmptyfolderDesc(){
var $foldName=$("#EmptyFolderName");
var $foldDesc=$("#EmptyFolderDesc");
$foldDesc.val($foldName.val());
}
function createUserFolder(){
$('#createUserFolder').dialog('close');
var folderName=$("#folderName").val();
var folderDesc=$("#folderDesc").val();
folderName=folderName.replace("&","'|| chr(38) ||'","gi");
folderDesc=folderDesc.replace("&","'|| chr(38) ||'","gi");
$.ajax({
url: 'userLayerAction.do?userParam=addUserFolder&fldName='+folderName+'&fldDesc='+folderDesc+'&grpId='+grpId,
success: function(data) {
  if(data=="false")
      alert("Unable to Create Business Role")
  else{ alert("Business Role Created successfully")
      window.location.reload();
      parent.checkUserFolder();
      // alert("Business Role Created successfully")
  }

}
});
}
//added by Nazneen for creating empty business role
function createUserFolderEmpty(){
$('#createUserFolderEmpty').dialog('close');
var folderName=$("#EmptyFolderName").val();
var folderDesc=$("#EmptyFolderDesc").val();
folderName=folderName.replace("&","'|| chr(38) ||'","gi");
folderDesc=folderDesc.replace("&","'|| chr(38) ||'","gi");
$.ajax({
    url: 'userLayerAction.do?userParam=addUserFolderEmpty&fldName='+folderName+'&fldDesc='+folderDesc+'&grpId='+grpId,
success: function(data) {
  if(data=="false")
      alert("Unable to Create Empty Business Role")
  else{ alert("Empty Business Role Created successfully")
      window.location.reload();
      parent.checkUserFolder();
      // alert("Business Role Created successfully")
  }
}
});
}
function contextMenubucket(action, el, pos)
{

switch (action) {
case "createBuckets":
  {   //alert(el.attr('id'));
      var n=el.attr('id').split("~");
      bucketDetails=el.attr('id');
      var tabName=n[1];
      var colName=n[3];
      var colType=n[4];
      var bussTableId=n[2];
      var m=n[0].split(",");
      var bussColId=m[0];
      var colId=m[1];
      var tabId=m[2];
      var grpId=m[3];
      showBucketDbExistance(bussTableId);
      // createBucket(colName,colId,tabId,colType,tabName,grpId,bussColId,bussTableId);
      break;
  }
case "addTimeDim":
  {
      var n=el.attr('id').split("~");
      timeDimDetails=el.attr('id');
      var tabName=n[1];
      var colName=n[3];
      var colType=n[4];
      var bussTableId=n[2];
      var m=n[0].split(",");
      var bussColId=m[0];
      var colId=m[1];
      var tabId=m[2];
      var grpId=m[3];

      showTimeDimExistance(grpId);
      // createTimeDimension(grpId,bussColId,bussTableId,tabName,colName,colType);
      break;
  }
case "addmoreTimeDim":
  {
      var n=el.attr('id').split("~");
      timeDimDetails=el.attr('id');
      var tabName=n[1];
      var colName=n[3];
      var colType=n[4];
      var bussTableId=n[2];
      var m=n[0].split(",");
      var bussColId=m[0];
      var colId=m[1];
      var tabId=m[2];
      var grpId=m[3];
      grpID=grpId;
      addMoreTimeDim(bussTableId)
      break;
  }
case "addMemberDesc":
  {

      var n=el.attr('id').split("~");
      timeDimDetails=el.attr('id');
      var tabName=n[1];
      var colName=n[3];
      var colType=n[4];
      var bussTableId=n[2];
      var m=n[0].split(",");
      var bussColId=m[0];
      var colId=m[1];
      var tabId=m[2];
      var grpId=m[3];
      document.getElementById("memberDescDialog").innerHTML = "<iframe class=frame1 src=addMemberDetails.jsp?memberId="+bussColId+"&grpId="+grpId+"&memberName="+colName+" style='width:500px'></iframe>";
      $('#memberDescDialog').dialog('open');

      break;
  }
case "addtimebaseformula":
  {
      var connId = document.getElementById("connId").value;

      var n=el.attr('id').split("~");


      timeDimDetails=el.attr('id');

      var tabName=n[1];

      var colName=n[3];

      var colType=n[4];

      var bussTableId=n[2];

      var m=n[0].split(",");

      var bussColId=m[0];

      var colId=m[1];

      var tabId=m[2];

      var grpId=m[3];


      document.getElementById("addtimebaseformula").innerHTML="<iframe frameborder='0' width='100%' height='100%' scrolling=no src=timebasedFormula.jsp?connId="+connId+"&bussTableId="+bussTableId+"&grpId="+grpId+"&bussColId="+bussColId+"&colName="+colName+"&tabName="+tabName+"></iframe>";
      $('#addtimebaseformula').dialog('open');
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
case "createDimensions"  :
  {
      var n=el.attr('id').split("~");
      bucketDetails=el.attr('id');
      var tabName=n[1];
      var colName=n[3];
      var colType=n[4];
      var bussTableId=n[2];
      var m=n[0].split(",");
      var bussColId=m[0];
      var colId=m[1];
      var tabId=m[2];
      var grpId=m[3];
      //alert("colName "+colName+"colId "+colId+"tabId "+tabId+"grpId "+grpId);
      //alert("tabName "+tabName+"colType "+colType+" bussTableId "+bussTableId+" bussColId "+bussColId);
      var frameObj=document.getElementById("bucketDisp");
      //var source = "otherDimension.do?colName="+colName+"&colId="+colId+"&tabId="+tabId+"&colType="+colType+"&bussTableId="+bussTableId+"&bussColId="+bussColId+"&tabName="+tabName+"&grpId="+grpId;
      var source = "addOtherDimension.jsp?colName="+colName+"&colId="+colId+"&tabId="+tabId+"&colType="+colType+"&bussTableId="+bussTableId+"&bussColId="+bussColId+"&tabName="+tabName+"&grpId="+grpId;
      // alert(source);
      frameObj.src=source;
      frameObj.style.display='block';
      document.getElementById('fade').style.display='block';
      break;
  }
case "addMemberDesc":
  {
      //alert("addmemdesc");
      var n=el.attr('id').split("~");
      timeDimDetails=el.attr('id');
      var tabName=n[1];
      var colName=n[3];
      var colType=n[4];
      var bussTableId=n[2];
      var m=n[0].split(",");
      var bussColId=m[0];
      var colId=m[1];
      var tabId=m[2];
      var grpId=m[3];
      document.getElementById("memberDescDialog").innerHTML = "<iframe class=frame1 src=addMemberDetails.jsp?memberId="+bussColId+"&grpId="+grpId+"&memberName="+colName+" style='width:500px'></iframe>";
      $('#memberDescDialog').dialog('open');
      break;
  }
case "viewFormula":
  {
      document.getElementById("value").innerHTML=$(el).attr('title');
      $("#formulaViewDiv").dialog('open');


      break;
  }
case "AddEditTargets":
  {
    var connId = document.getElementById("connId").value;
      var n=el.attr('id').split("~");
      timeDimDetails=el.attr('id');
      var tabName=n[1];
      var colName=n[3];
      $("#idvale").val(colName)
      var colType=n[4];
      var bussTableId=n[2];
      var m=n[0].split(",");
      var bussColId=m[0];

      var colId=m[1];
      var tabId=m[2];
       grupId=m[3];
      $("#bussid").val(bussColId)
      $.ajax({
        url: 'targetmeasuresaction.do?targetMeasures=dimensions&groupId='+grupId+'&bussTableId='+bussTableId,
        success: function(data) {
            var jsonVar=eval('('+data+')')

             var keys = [];
                                 for (var key in jsonVar) {
                                     if (jsonVar.hasOwnProperty(key)) {
                                         keys.push(key);
                                     }
                                 }
                                 var htmlVar=""
                                 var jsonvaleuse=""
                                 htmlVar+="<option  value=''>--SELECT--</option>"
                                 for(var i=0;i<keys.length;i++)
                                 {
                                      jsonvaleuse=jsonVar[keys[i]]
                                htmlVar+="<option  value='"+keys[i]+"'>"+jsonvaleuse+"</option>"
                                 }
                                 $("#dimentionList").html(htmlVar);
        }

    });
    $("#targetDiv").dialog('open');
      break;
  }
  case "addFormula":
                              {
                                  //alert('in alert '+bustableId)
                                  var a=bustableId.split(",");
                                  bustableId=a[0];
                                  grpId=a[2];
                                  document.getElementById("bustableId").value=bustableId;
                                  document.getElementById("grpId").value=grpId;
                                  document.getElementById("formulaDialog").innerHTML = "<iframe width='100%' height='100%'  frameborder='0' src=columnFormula.jsp?bussTableId="+bustableId+"&grpId="+grpId+" ></iframe>";
                                  $('#formulaDialog').dialog('open');

                                  break;
                              }
 // start by  ramesh
  case "AddDateFormula":
      {
      var connId = document.getElementById("connId").value;
      var n=el.attr('id').split("~");
      timeDimDetails=el.attr('id');
      var tabName=n[1];
      var colName=n[3];
      var colType=n[4];
      var bussTableId=n[2];
      var m=n[0].split(",");
      var bussColId=m[0];
      var colId=m[1];
      var tabId=m[2];
       grupId=m[3];
       $("#busstableId").val(bussTableId)
       $("#connid").val(connId)
       $("#busscolumnname").val(colName)
        $("#tablename").val(tabName)
        $("#BussColId").val(bussColId)
        $.ajax({
        url: 'targetmeasuresaction.do?targetMeasures=addDateFormula&busstableid='+bussTableId,
        success: function(data) {
           var jsonVar=eval('('+data+')')
           var datecolumns=jsonVar.datelist
           var htmlVar=""
           var jsonvaleuse=""
           htmlVar+="<option  value=''>--SELECT--</option>"
           for(var i=0;i<datecolumns.length;i++)
               {
                    jsonvaleuse=datecolumns[i]
                        htmlVar+="<option  value='"+datecolumns[i]+"'>"+jsonvaleuse+"</option>"
               }
                $("#dateformulaid").html(htmlVar)
        }
        });

        $("#dateDivId").dialog('open');
         break;
      }
// end by ramesh

}
}
function sleep(milliSeconds){
var startTime = new Date().getTime(); // get the current time
while (new Date().getTime() < startTime + milliSeconds); // hog cpu
}


function contextEditBucketMenu(action, el, pos){
//alert('in contextEditBucketMenu')
switch (action) {
case "editBucket":
  {
      // alert( $(el).attr('id'));
      //getBuckets
      var ulid=$(el).attr('id').replace("_span", "")
      var bucketObj=document.getElementById($(el).attr('id')).parentNode;
      var ulObj=bucketObj.getElementsByTagName("ul");
      var busGroup=ulid.toString().split("-")
      var busGroupID=busGroup[1]
      //    alert("busGroupID--"+busGroupID)
      // getBuckets(busGroupID)
      var liobj=ulObj[0].getElementsByTagName("li");


      var source = "editBucketDetails.jsp?busGroupID="+busGroupID;
      var frameObj=document.getElementById("editBucketframe");
      frameObj.src=source;
      $("#editBucketDiv").dialog('open');


      break;
  }
}

}
function contextMigrateGroupHierarchyMenu(action, el, pos){
switch (action) {
case "MagrateGroupHierarchy":
  {
      var temp=el.parent("li")
      var groupid=temp.attr("id")
      $.ajax({
url: 'businessgroupeditaction.do?groupdetails=getFolderidDetails&groupid='+groupid,
success: function(data) {
    if(data=='true')
        alert('Changes Migrated To Role Successfully.');
    else
        alert('Error! Changes not Migrated To Roles.')
}
});
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
      //alert("deleteTable")
      break;
  }
case "addColumns":
  {
      //alert("addColumns")
      break;
  }
case "deleteColumns":
  {
      // alert("deleteColumns")
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

case "addFormula":
  {
      //alert('in alert '+bustableId)
      var a=bustableId.split(",");
      bustableId=a[0];
      grpId=a[2];
      document.getElementById("bustableId").value=bustableId;
      document.getElementById("grpId").value=grpId;
      document.getElementById("formulaDialog").innerHTML = "<iframe class=frame1 src=columnFormula.jsp?bussTableId="+bustableId+"&grpId="+grpId+" style='width:500px'></iframe>";
      $('#formulaDialog').dialog('open');

      break;
  }

case "addMemberDesc":
  {

      var a=el.attr('id');
      var dets=a.split('~');
      var memberId=dets[1];
      var grpId=dets[0].split("-")[1];
      var memberName=dets[2];
      alert(memberId+"=="+grpId+"=="+memberName)
      document.getElementById("memberDescDialog").innerHTML = "<iframe class=frame1 src=addMemberDetails.jsp?memberId="+memberId+"&grpId="+grpId+"&memberName="+memberName+" style='width:500px'></iframe>";
      $('#memberDescDialog').dialog('open');

      break;
  }

case "addFilter":
  {
      //alert('in alert '+bustableId)
      var a=bustableId.split(",");
      bustableId=a[0];
      grpId=a[2];
      document.getElementById("bustableId").value=bustableId;
      document.getElementById("grpId").value=grpId;
      document.getElementById("filterDialog").innerHTML = "<center><iframe class=frame1 src=filter.jsp?bussTableId="+bustableId+"></iframe></center>";
      $('#filterDialog').dialog('open');

      break;
  }

//start by uday
case "editRelation":
{
  //alert('in alert editRelation '+bustableId)
  var a=bustableId.split(",");
  var tableId=a[0];
  grpId=a[2];
  document.getElementById("bustableId").value=bustableId;
  // document.getElementById("grpId").value=grpId;
  hideBussTabsArea();
  hideAddSrcTblLayer();
  hideDimensionLayer();
  // alert("before");
  var ob5=document.getElementById("TargetMeasuresDisp");
  ob5.style.visibility='none';
  ob5.style.display="hideTableCell"; //dargTargetMeasuresPanel
  var ob6=document.getElementById("dargTargetMeasuresPanel");
  ob6.style.visibility='hidden'; //hidden
  ob6.style.display="hideTableCell";// hideTableCell
  hideRelated();
  hideEdit();
  // hideTarget();
  // hideDragTarget();
  //alert("after")



  // document.getElementById("showEditRelationUI").style.visibility= 'visible';
  //document.getElementById("showEditRelationUI").style.display="table-cell";
  //                              alert("in editRelation")
  getRelatedTables(tableId);

  getEditRelationSectionData(tableId,grpId);
  break;
}
//end by uday

// added by susheela start
case "showTargetMeasures":
{
var a=bustableId.split(",");
bustableId=a[0];
// document.getElementById("grpId").value=grpId;
grpId=el.attr('id');
//alert('  grpId -- '+grpId)

getTargetMeasures(grpId);
break;
}


case "deleteBgDimension":
{

//alert(delGrpId+' in del delGrpId '+delDimId);
checkGroupDimDelete();
break;
}


//added by susheela start 28-12-09
case "showcreatetarget":
{
// alert('in showcreatetarget '+measureId);
var  userId=document.getElementById("loginuser").value;
// alert('userId  '+userId)
document.getElementById("targetDialog").innerHTML = "<center><iframe class=frame1 src=QTarget/JSPs/pbTargetMaster.jsp?userId="+userId+"&measureId="+measureId+"></iframe></center>";
$('#targetDialog').dialog('open');
break;
}
case "showviewtarget":
{
var  userId=document.getElementById("loginuser").value;
//alert(userId+' in  show view target '+targetId);
var path = "<%=request.getContextPath()%>";
//alert(path+"/targetView.do?targetParams=viewTarget&targetId="+targetId+"&minTimeLevel="+tarMinTimeLevel)
parent.document.baseForm.action = path+"/targetView.do?targetParams=viewTargetForView&targetId="+targetId+"&minTimeLevel="+tarMinTimeLevel;
parent.document.baseForm.submit();

break;
}
case "showedittarget":
{
var  userId=document.getElementById("loginuser").value;
// alert(userId+' in  show edit target '+targetId+" tarMinTimeLevel "+tarMinTimeLevel);
/*   var targetId = '
                        var timeLevel = '< */
var path = "<%=request.getContextPath()%>";
//alert(path+"/targetView.do?targetParams=viewTarget&targetId="+targetId+"&minTimeLevel="+tarMinTimeLevel)
parent.document.baseForm.action = path+"/targetView.do?targetParams=viewTarget&targetId="+targetId+"&minTimeLevel="+tarMinTimeLevel;
parent.document.baseForm.submit();
break;
}
//added by susheela over 28-12-09

            <%--added by susheela over --%>
}
}

//start by uday
function getRelatedTables(tableId){
//alert("table Id is:: "+tableId);
//document.getElementById("showEditRelationUI").innerHTML = "<iframe class=frame2 src=relatedTables.do?tableId="+bustableId+" border=1></iframe>";
$.ajax({
url: 'relatedTablesList.do?relTables=getRelatedTables&tableId='+tableId,
success: function(data) {
if(data != "")
{
//alert(data);
buildTree(data);
//document.getElementsByName("relatedTablesTree")[1].innerHTML = data;
}

}
});

}
function getEditRelationSectionData(tableId,grpId)
{
$.ajax({
url: 'relatedTablesList.do?relTables=getEditRelationSection&tableId='+tableId+'&grpId='+grpId,
success: function(data) {
if(data != "")
{
//alert(data);
showRelationships(data);
//document.getElementsByName("relatedTablesTree")[1].innerHTML = data;
}

}
});
}
function buildTree(data)
{
var branches;
//alert(data);
document.getElementById("relatedTablesTreeIn").innerHTML = data;
// branches = $(data).appendTo("#relatedTablesTreeIn");
//alert(branches);
// $("#relatedTablesTreeIn").treeview({
//     add: branches
// });
}
function openRelated(){
//alert("openRelated")
// alert(' in o ')
$("#relatedTablesTreeIn").treeview({
animated: "normal",
unique:true
});
}

function showRelationships(data)
{
/* document.getElementById("showRelatedTableColumnsRelation").innerHTML = "";
                document.getElementById("showRelatedTableColumnsRelation").innerHTML = data;

                document.getElementById("relatedTablesPanel").style.visibility = 'visible';
                document.getElementById("relatedTablesPanel").style.display="table-cell";
                document.getElementById("editRelationPanel").style.visibility = 'visible';
                document.getElementById("editRelationPanel").style.display="table-cell";
                document.getElementById("commitRelationTxt").innerHTML = ""; */
var ob=document.getElementById("TargetMeasuresDisp");
ob.style.visibility='hidden';
ob.style.display='none';
var ob2=document.getElementById("dargTargetMeasuresPanel");
ob2.style.visibility='hidden';
ob2.style.display='none';
//added by susheela 04-12-09

document.getElementById("showRelatedTableColumnsRelation").innerHTML = "";
document.getElementById("showRelatedTableColumnsRelation").innerHTML = data;

document.getElementById("relatedTablesPanel").style.visibility = 'visible';
document.getElementById("relatedTablesPanel").style.display="table-cell";
document.getElementById("editRelationPanel").style.visibility = 'visible';
document.getElementById("editRelationPanel").style.display="table-cell";
document.getElementById("commitRelationTxt").innerHTML = "";

}
//end by uday
//start by Nazneen
function contextEditGroupHierarchyMenu(action, el, pos)
{
switch (action) {
    case "addMeasures":
        {
        var props = el.attr('id');
        var connId=document.getElementById("connId").value;
        var tabId = props.split(',')[0];

            document.getElementById("groupHierarchyDialog").innerHTML = "<iframe width='100%' height='100%'  frameborder='0' src=groupHierarchy.jsp?connId="+connId+"&grpId="+props+"&tabId="+tabId+"></iframe>";

        $('#groupHierarchyDialog').dialog('open');
        break;
        }
  }
}
function contextEditChildHierarchyMenu(action, el, pos)
{
switch (action) {
    case "addChilds":
        {
        var props = el.attr('id');
        var parentId=props.split(':')[0];
        var groupId=props.split(':')[1];
        var tabId = props.split(':')[2];
        var connId=document.getElementById("connId").value;
       document.getElementById("groupHierarchyDialog").innerHTML = "<iframe width='100%' height='100%'  frameborder='0' src=groupChildMenu.jsp?connId="+connId+"&groupId="+groupId+"&parentId="+parentId+"&tabId="+tabId+"></iframe>";
        $('#groupHierarchyDialog').dialog('open');
        break;
        }
  }
}
function contextEditGroupHierarchyChildMenu(action, el, pos)
{
switch (action) {
    case "addChild":
        {
//        var props = el.attr('id');
//        var connId=document.getElementById("connId").value;
//        var parentId = props.split(',')[0];
//        var grpId=props.split(',')[2];

        document.getElementById("childDiv").innerHTML = "<iframe width='100%' height='100%'  frameborder='0' src=groupChildMenu.jsp?connId="+connId+"&grpId="+grpId+"&tabId="+tabId+"></iframe>";

        $('#childDiv').dialog('open');
        break;
        }
  }
}
//end by Nazneen

function contextMenuWork3(action, el, pos)
{
switch (action) {
case "addTabsToSrcprop":
{
var props = el.attr('id');
// alert(props)
var tabId = props.split(',')[0];



var frameObj=document.getElementById("tablePropDisp");
// alert(tabId)
var source="getBusinessTables.do?tableId="+tabId;
frameObj.src=source;
$("#tableDisplay").dialog('open');
// frameObj.style.display='block';
//document.getElementById('fade').style.display='block';
break;
}
case "addFormulafact":
{


//alert('in alert '+el.attr('id'));
var a=el.attr('id').split(",");
var bustableId=a[0];
var grpId=a[2];

document.getElementById("bustableId").value=bustableId;
document.getElementById("grpId").value=grpId;
var connId=document.getElementById("connId").value;
//alert("connId="+connId+"&bussTableId="+bustableId+"&grpId="+grpId);
document.getElementById("formulaDialog").innerHTML = "<iframe class=frame1 src=columnParamFormula.jsp?connId="+connId+"&bussTableId="+bustableId+"&grpId="+grpId+" style='width:500px;height:100%'></iframe>";
$('#formulaDialog').dialog('open');

break;
}
case "addFormulafactForSingal":
      {
          var connId=document.getElementById("connId").value;
          //  alert('in alert '+el.attr('id')+'connId-'+connId);
          var grpId=el.attr('id');
//          alert(grpId)
          document.getElementById("grpId").value=grpId;
          document.getElementById("formulaDialog").innerHTML = "<iframe width='100%' height='100%'  frameborder='0' src=columnFormula.jsp?connId="+connId+"&grpId="+grpId+" ></iframe>";
          $('#formulaDialog').dialog('open');
          break;
      }
<%--case "groupHierarchy":
{
var props = el.attr('id');
var connId=document.getElementById("connId").value;
var tabId = props.split(',')[0];
grpId=props.split(',')[2];
document.getElementById("groupHierarchyDialog").innerHTML = "<iframe width='100%' height='100%'  frameborder='0' src=groupHierarchy.jsp?connId="+connId+"&grpId="+grpId+"&tabid="+tabId+"></iframe>";

 $('#groupHierarchyDialog').dialog('open');
    break;
}--%>
//added by Nazneen
case "addGroups":
{
        var props = el.attr('id');
        var connId=document.getElementById("connId").value;
        <%--var tabId = props.split(',')[0];--%>
        groupId=props.split(',')[2];

        tableId=props.split(',')[0];
        $('#addGroupsDialog').dialog('open');
        break;
}
//added by Nazneen

case "addQuickFormula":
{
var props = el.attr('id');
var connId=document.getElementById("connId").value;
quickFormulaTabId = props.split(',')[0];
grpId=props.split(',')[2];


                         $.ajax({
                             url:'addQuickFormula.do?&colid='+quickFormulaTabId,
                             success:function(data){
                                 var jsonVar=eval('('+data+')')

                                 var keys = [];
                                 for (var key in jsonVar) {
                                     if (jsonVar.hasOwnProperty(key)) {
                                         keys.push(key);
                                     }
                                 }
                                 var htmlVar=""
                                 for(var i=0;i<keys.length;i++)
                                 {
                                     var jsonvaleuse=jsonVar[keys[i]]
                                          if(i%2=="0")
                                              {
                                                htmlVar+="<tr class='navtitle-hover'>"
                                              }

htmlVar+="<td>"+i+"</td><td ><input type=\"text\" id=\"displayname"+i+"\" value=\""+jsonvaleuse[0]+"\" readonly=\"\" name=\"displayname"+i+"\" size=\"15\"><input type=\"hidden\" value=\""+keys[i]+"\" id=\"colsids"+i+"\" name=\"colsids"+i+"\"></td>\n\
                                   <td valign=\"center\" >"+"<select disabled id=\"defaultAgr"+i+"\" style=\"width:128px;\" name=\"defaultAgr\" onchange='checkVals(this,"+i+")'>"
            <%for (int j = 0; j < DefaultArrregations.length; j++) {%>

if(jsonvaleuse[8]=='<%=DefaultArrregations[j]%>')
htmlVar+="<option selected value=<%=DefaultArrregations[j]%>><%=DefaultArrregations[j]%></option>"
else
htmlVar+="<option value=<%=DefaultArrregations[j]%>><%=DefaultArrregations[j]%></option>"

            <%}%>

htmlVar+="</select></td>\n"
if(jsonvaleuse[1]=='sum')
htmlVar+="<td  align='center'><input type='checkbox' name='sum' id='sum"+i+"' value='sum"+i+"' disabled checked></td>\n"
else if(jsonvaleuse[8]=='sum')
htmlVar+="<td  align='center'><input type='checkbox' name='sum' id='sum"+i+"' value='sum"+i+"' disabled></td>\n"
else
htmlVar+="<td  align='center'><input type='checkbox' name='sum' id='sum"+i+"' value='sum"+i+"' ></td>\n"
if(jsonvaleuse[2]=="avg")
htmlVar+="<td  align='center'><input type=\"checkbox\" disabled=\"\" value=\"avg"+i+"\" id=\"avg"+i+"\" name=\"avg\" checked></td>"
else if(jsonvaleuse[8]=="avg")
htmlVar+="<td  align='center'><input type=\"checkbox\" disabled=\"\" value=\"avg"+i+"\" id=\"avg"+i+"\" name=\"avg\"></td>"
else
htmlVar+="<td  align='center'><input type=\"checkbox\"  value=\"avg"+i+"\" id=\"avg"+i+"\" name=\"avg\"></td>"
if(jsonvaleuse[3]=="min")
htmlVar+="<td  align='center'><input type=\"checkbox\" disabled=\"\" value=\"min"+i+"\" id=\"min"+i+"\" name=\"min\" checked></td>"
else if(jsonvaleuse[8]=="min")
htmlVar+="<td  align='center'><input type=\"checkbox\" disabled=\"\" value=\"min"+i+"\" id=\"min"+i+"\" name=\"min\"></td>"
else
htmlVar+="<td  align='center'><input type=\"checkbox\"  value=\"min"+i+"\" id=\"min"+i+"\" name=\"min\"></td>"
if(jsonvaleuse[4]=="max")
htmlVar+="<td  align='center'><input type=\"checkbox\" disabled=\"\" value=\"max"+i+"\" id=\"max"+i+"\" name=\"max\" checked></td>"
else if(jsonvaleuse[8]=="max")
htmlVar+="<td  align='center'><input type=\"checkbox\" disabled=\"\" value=\"max"+i+"\" id=\"max"+i+"\" name=\"max\"></td>"
else
htmlVar+="<td  align='center'><input type=\"checkbox\"  value=\"max"+i+"\" id=\"max"+i+"\" name=\"max\"></td>"
if(jsonvaleuse[5]=="count")
htmlVar+="<td  align='center'><input type='checkbox' name='count' id='count"+i+"' value='count"+i+"' disabled checked></td>"
else if(jsonvaleuse[8]=="count")
htmlVar+="<td  align='center'><input type=\"checkbox\" disabled=\"\" value=\"count"+i+"\" id=\"count"+i+"\" name=\"count\"></td>"
else
htmlVar+="<td  align='center'><input type=\"checkbox\"  value=\"count"+i+"\" id=\"count"+i+"\" name=\"count\"></td>"

if(jsonvaleuse[6]=="COUNTDISTINCT")
htmlVar+="<td  align='center'><input type=\"checkbox\" disabled=\"\" value=\"COUNTDISTINCT"+i+"\" id=\"COUNTDISTINCT"+i+"\" name=\"COUNTDISTINCT\" checked></td>"
else if(jsonvaleuse[8]=="COUNTDISTINCT")
htmlVar+="<td  align='center'><input type=\"checkbox\" disabled=\"\" value=\"COUNTDISTINCT"+i+"\" id=\"COUNTDISTINCT"+i+"\" name=\"COUNTDISTINCT\"></td>"
else
htmlVar+="<td  align='center'><input type=\"checkbox\"  value=\"COUNTDISTINCT"+i+"\" id=\"COUNTDISTINCT"+i+"\" name=\"COUNTDISTINCT\"></td>"

if(jsonvaleuse[7]=="roleflag")
htmlVar+="<td  align='center'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type='checkbox' name='roleflag' id='roleflag"+i+"' value='roleflag"+i+"' checked></td>"
else
htmlVar+="<td  align='center'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type='checkbox' name='roleflag' id='roleflag"+i+"' value='roleflag"+i+"'></td>"
htmlVar+="</tr>";
}

$("#addQuicktab").html(htmlVar);
}


});
$('#quickFormulaDialog').dialog('open');

}

}
}

//function contextMenuWorkAssignUser(action, el, pos)
//{
////alert("hello")
//var props = el.attr('id');
//// alert(props)
//var grpId = props.split(',')[0];
//
//
//$("#userAssigned").dialog('open')
//var frameObj=document.getElementById("userAssignDisp");
//// alert(tabId)
//var source="getUserAssignList.jsp?grpId="+grpId;
//frameObj.src=source;
//// frameObj.style.display='block';
//// document.getElementById('fade').style.display='block';
//
//}



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
// document.getElementById("busgrpdataDispDialog").innerHTML = "<iframe class=frame1 src=businessGroupViewTable.jsp?tableIds="+tableIds+"&grpId="+grpId+"></iframe>";
//  $('#busgrpdataDispDialog').dialog('open');

}

// added by susheela 01-Oct-09 targetMeasure
function getTargetMeasures(grpId){
var ob=document.getElementById("dbDimensions");
ob.style.visibility="hidden";
ob.style.display="hideTableCell";
var ob1=document.getElementById("addDbDims");
ob1.style.visibility="hidden";
ob1.style.display="hideTableCell";
var ob2=document.getElementById("relDBTables");
ob2.style.visibility="hidden";
ob2.style.display="hideTableCell";
hideBussTabsArea();
hideAddSrcTblLayer();
hideDimensionLayer();

//susheela's divs
var ob4=document.getElementById("TargetMeasuresDisp");
ob4.style.visibility="visible";
ob4.style.display="table-cell"; //dargTargetMeasuresPanel
var ob3=document.getElementById("dargTargetMeasuresPanel");
ob3.style.visibility="visible"; //hidden
ob3.style.display="table-cell";// hideTableCell


$.ajax({
url: 'targetmeasuresaction.do?targetMeasures=getTargetFacts&grpId='+grpId,
success: function(data) {
if(data != ""){
buildTree10(data)
}

}
});
}

function buildTree10(data)
{
var branches;
//alert(data);
document.getElementById("TargetMeasuresDispList").innerHTML = "";
branches = $(data).appendTo("#TargetMeasuresDispList");
//alert(branches);
$("#TargetMeasuresDispList").treeview({
add: branches
});
}
function buildTree11()
{
var branches;
$.ajax({
url: 'targetmeasuresaction.do?targetMeasures=getSavedTargetFacts',
success: function(data) {
// alert('data............. '+data);
if(data!= ""){
// alert('data..11 '+data);
//document.getElementById("TargetMeasuresDispList").innerHTML = data;
buildTree12(data)

}

}
});

}

            <%--added by susheela over --%>

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
// added by susheela
function targetMeasure(val){
//grpId=val;//obj.id.split(",")[0];
//alert('grpId-- '+grpId);
}
//added by susheela over


function cancelTableList()
{  //alert('hi');
document.getElementById("busgrpdataDisp").style.display='none';
document.getElementById('fade').style.display='';
// window.location.href="<%=request.getContextPath()%>/getAllBusinessGroups.do";
}
<%--function cancelgrouplist()
{  //alert('hi');
document.getElementById("grouphiid").style.display='none';
document.getElementById('fade').style.display='';
// window.location.href="<%=request.getContextPath()%>/getAllBusinessGroups.do";
}--%>

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


function createBucket(colName,colId,tabId,colType,tabName,grpId,bussColId,bussTableId)
{

var frameObj=document.getElementById("bucketDisp1");
var source = "createBucket.jsp?colName="+colName+"&colId="+colId+"&tabId="+tabId+"&colType="+colType+"&tabName="+tabName+"&grpId="+grpId+"&bussColId="+bussColId+"&bussTableId="+bussTableId;

if(colType==undefined){
alert("Please Select Measure Column")
}else{
frameObj.src=source;
$("#createBucketdiv").dialog('open')
            <%--frameObj.style.display='block';
            document.getElementById('fade').style.display='block';--%>
}


}


function cancelBuckets()
{

$("#quickFormulaDialog").dialog('close')

}
function saveFormula()
{
    //changed by Nazneen Khan

//document.columnForm.action='addQuickFormula.do?testVar=save';
//document.columnForm.submit();
 
      alert("Formula Saved Successfully")
       async:false,
           $.post( "<%= request.getContextPath()%>/addQuickFormula.do?",$("#columnForm").serialize(),function(data)
    {
       // window.location.href=window.location.href;
       callfunction(); 
    });
//           alert("Formula Saved Successfully")             
//           var form =  $('#columnForm');             
//                        $.ajax( {
//      type: "POST",
//      url: form.attr( 'action' ),
//      data: form.serialize(),
//      success: function( response ) {   
//          window.refresh;
//             // callfunction(); 
//      }
//    } );

<%--$.post( "addQuickFormula.do",$("#columnForm").serialize(),function(data)
{});--%>
}
//added by Nazneen
function callfunction(){

    $.post( "<%= request.getContextPath()%>/targetmeasuresaction.do?targetMeasures=refreshjs",function(data)
    {       
       window.location.href=window.location.href;
    });
    
}
function cancelBuckets1()
{
document.getElementById("bucketDisp").style.display='none';
document.getElementById('fade').style.display='';
alert('bucket Created Successfully');
window.location.reload(true);
}
function  parentCancelTimeDim(){
document.getElementById("timeDimDisp").style.display='none';
document.getElementById('fade').style.display='';

}
function  parentCancelTimeDim1(minTimeLevel,dimscount){
// alert('hi in jsp parent'+minTimeLevel+"------"+dimscount)
document.getElementById("timeDimDisp").style.display='none';
document.getElementById('fade').style.display='';
document.getElementById('fade1').style.display='block';
if(minTimeLevel==5){

setTimeout("refreshPage()",7000);

}else if(minTimeLevel==4){

setTimeout("refreshPage()", 6000);

}else if(minTimeLevel==3){

setTimeout("refreshPage()", 5000);

}else if(minTimeLevel==2){

setTimeout("refreshPage()", 4000);

}else if(minTimeLevel==1){

setTimeout("refreshPage()",3000);

}
// alert('hi in jsp parent')
}function parentCancelTimeDim2(dimscount){
// alert('hi inparent'+dimscount)
var connId1=document.getElementById("connId1").value;
document.myFormnew.action="getAllBusinessGroups.do?method=dynamicdims&connid="+connId1;
document.myFormnew.submit();
document.getElementById('fade1').style.display='';
// if(dimscount==0){
//  window.location.reload(true);
// }
}

function  parentCancelTableProperties(){
$("#tableDisplay").dialog('close');
            <%--document.getElementById("tablePropDisp").style.display='none';
            document.getElementById('fade').style.display='';--%>

}
function  parentCancelTableProperties1(){
$("#tableDisplay").dialog('close');
            <%--document.getElementById("tablePropDisp").style.display='none';
            document.getElementById('fade').style.display='';--%>
// window.location.reload(true);
}
function cancelUsers(){
$("#userAssigned").dialog('close');
            <%--document.getElementById("userAssignDisp").style.display='none';
            document.getElementById('fade').style.display='';--%>

}



function  createTimeDimension(grpId,bussColId,bussTableId,tabName,colName,colType){
alert("bussTableId\t"+bussTableId)

var frameObj=document.getElementById("timeDimDisp");
var source = "createTimeDimension.jsp?grpId="+grpId+"&bussColId="+bussColId+"&bussTableId="+bussTableId+"&tabName="+tabName+"&colName="+colName+"&colType="+colType;
// alert(source)
frameObj.src=source;
frameObj.style.display='block';
document.getElementById('fade').style.display='block';


}

function createNewTimeDim(){
// alert('in new Time dim')
$('#editTimeDimDialog').dialog('close');
var n=timeDimDetails.split("~");
var tabName=n[1];
var colName=n[3];
var colType=n[4];
var bussTableId=n[2];
var m=n[0].split(",");
var bussColId=m[0];
var colId=m[1];
var tabId=m[2];
var grpId=m[3];
//check global varibles r there or not while integrating
createTimeDimension(grpId,bussColId,bussTableId,tabName,colName,colType);
}
function ShowTimeDim(){

//  $('#editTimeDimDialog').dialog('close');
//  alert('show time dim')
var n=timeDimDetails.split("~");
var tabName=n[1];
var colName=n[3];
var colType=n[4];
var bussTableId=n[2];
var m=n[0].split(",");
var bussColId=m[0];
var colId=m[1];
var tabId=m[2];
var grpId=m[3];
$('#editTimeDimDialog').dialog('close');

var frameObj=document.getElementById("showTimeDimDisp");
var source = "showTimeDimension.jsp?grpId="+grpId+"&bussColId="+bussColId+"&bussTableId="+bussTableId+"&tabName="+tabName+"&colName="+colName+"&colType="+colType;
// alert(source)
frameObj.src=source;

frameObj.style.display='block';
document.getElementById('fade').style.display='block';
}

function parentCancelShowTimeDim(){
document.getElementById("showTimeDimDisp").style.display='none';
document.getElementById('fade').style.display='';
}
function cancelFormula()
{

$('#formulaDialog').dialog('close');
window.location.reload(true);

}
function cancelFilter()
{

$('#filterDialog').dialog('close');

}

function createOtherDimensions()
{
var frameObj=document.getElementById("bucketDisp");
var source = "createBucket.jsp?colName="+colName+"&colId="+colId+"&tabId="+tabId;
// alert(source);
frameObj.src=source;
frameObj.style.display='block';
document.getElementById('fade').style.display='block';
}
function  cancelotherDimension(){
document.getElementById("bucketDisp").style.display='none';
document.getElementById('fade').style.display='';
}
function  cancelotherDimension1(){
document.getElementById("bucketDisp").style.display='none';
document.getElementById('fade').style.display='';
window.location.reload(true);
}


var xmlHttp2;


function showBucketDbExistance(str)
{

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

var url=ctxPath+"/BucketDBExistance";
url=url+"?bussTableId="+str;
// alert(url)
// var payload = "q="+str+"&id="+id;
//alert('target url is---'+url);
xmlHttp2.onreadystatechange=stateChangedBucketDbExistance;
xmlHttp2.open("GET",url,true);
xmlHttp2.send(null);
}


function stateChangedBucketDbExistance()
{

if (xmlHttp2.readyState==4)
{
var output=xmlHttp2.responseText;
//alert("output is "+output);


if(output==2){
var n=bucketDetails.split("~");
var tabName=n[1];
var colName=n[3];
var colType=n[4];
var bussTableId=n[2];
var m=n[0].split(",");
var bussColId=m[0];
var colId=m[1];
var tabId=m[2];
var grpId=m[3];
createBucket(colName,colId,tabId,colType,tabName,grpId,bussColId,bussTableId);
}
else{
alert('Please create Bucket Tables in your Database');

}
}
}


function showTimeDimExistance(str)
{


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

var url=ctxPath+"/TimeDimExistance";
url=url+"?grpId="+str;
//alert(url)
xmlHttp2.onreadystatechange=stateChangedTimeDimExistance;
xmlHttp2.open("GET",url,true);
xmlHttp2.send(null);
}


function addMoreTimeDim(bussTableID){
$("#addMoreTimeDimDiv").dialog('open')
$("#addMoreTimeDimDiv").html("");
$('<iframe id="addColumnframe" name="addColumnframe"  style="width:100%;height:100%" frameborder="0" src="<%= request.getContextPath()%>/addColumnToTable.jsp?tableId='+bussTableID+'&tableName= >').appendTo($("#addMoreTimeDimDiv"));

}
function closeMoreTimeDim(){
$("#addMoreTimeDimDiv").dialog('close')
showTimeDimExistance(grpID);

}

function stateChangedTimeDimExistance()
{
// alert('hi in target')

if (xmlHttp2.readyState==4)
{
var output=xmlHttp2.responseText;
// alert("output is "+output);


if(output>0){
// alert('hi')
// var x=confirm('Already TimeDimension Exist for this Business Group\nYou want to Map more...');
// if(x==true){
//   ShowTimeDim();
//  }
//         alert('bye')
$('#editTimeDimDialog').dialog('open');
}
else{
alert("timeDimDetails\t"+timeDimDetails)
var n=timeDimDetails.split("~");
var tabName=n[1];
var colName=n[3];
var colType=n[4];
var bussTableId=n[2];
var m=n[0].split(",");
var bussColId=m[0];
var colId=m[1];
var tabId=m[2];
var grpId=m[3];
//check global varibles r there or not while integrating
createTimeDimension(grpId,bussColId,bussTableId,tabName,colName,colType);
}
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
function saveTablesgrp()
{
$('#selectConnection').dialog('close');
var connId1=document.getElementById("connId1").value;
/*$.ajax({
        url:'getAllBusinessGroups.do?method=changedatabase&connid='+connId1,
        success:function(data){
            alert("success");
            // window.location.href = window.location.href;

        }
});*/
document.forms.myFormcon.action = "getAllBusinessGroups.do?method=dynamicdims&connid="+connId1;
document.forms.myFormcon.submit();
/*
 var frameObj = document.getElementById('businessgrptab');
var source="getAllBusinessGroups.do?method=changedatabase&connid="+connId1;
frameObj.src=source;
alert(source+"again changed")
document.getElementById('businessgrptab').style.display='';
window.location.reload(true);
*/
}
function goConnection(){
// alert('in go con')
$('#selectConnection').dialog('open');
}
function isNumberKey(evt)
           {
   var charCode = (evt.which) ? evt.which : evt.keyCode
    if(charCode==46 || charCode==44)
        return true;
    if (charCode > 31 && (charCode < 48 || charCode > 57))
        return false;

    return true;
       }
  function selecting(){
      $("#showdateId").show();
//      alert($("#durationSelect").val())
      if($("#durationSelect").val()=="Month"){
          $("#yearpicker").show()
          for (i = new Date().getFullYear(); i > 1900; i--)
            {
                $('#yearpicker').append($('<option />').val(i).html(i));
            }

      }else if($("#durationSelect").val()=="Year"){

      }
  }
  function selectdimensions(){
         var  dimID= $("#dimentionList").val();

         var grpID=grupId;
      <%--  $('.tableTdDate').each(function() {
                       //                  alert( this.checked)
                       if (!this.checked){
                           status=false
                           return false
                       }

                   });--%>
 $.ajax({
     url: 'targetmeasuresaction.do?targetMeasures=getCustomerTableDetails&dimID='+dimID+'&grpID='+grpID,
                    success: function(data)
                    {
                        $("#targetDetails > tbody").html('')
                        var jsonVar=eval('('+data+')')
                      var length=data.length
                     $("#allReps").val(length);
                       var htmlVar=""
                        var dimentionvalues=jsonVar.DimValues

                        for(var i=0;i<dimentionvalues.length;i++){
                           var tempVal= $("#datefortarget").val()+"-"+$("#yearpicker").val()
                            htmlVar="<tr><td class='tableTdDate'><input readonly type='text' name='durationDetails' value='"+tempVal+"'></td><td><input readonly type='text' name='dimentionvalue' value='"+dimentionvalues[i]+"'></td><td><input type='text' name='targetvalue' id='targetvalue' value=''onkeypress='return isNumberKey(event)'></td></tr>"
                           if( $('#myTable >tbody >tr').length!=0)
                              $('#targetDetails tr:last').after(htmlVar);
                              else
                              $('#targetDetails').append(htmlVar);
                    }
                                    $('#targetDetails:has(tbody tr)').tablesorter({headers : {0:{sorter:false}}}).tablesorterPager({container: $("#pagermodifyMeasures") });

                }
                    }
                     );

      $("#targetDiv1").show();

  }
  function saveDateFormula()
  {
      var  busstableid=$("#busstableId").val()
      var connid=$("#connid").val()
      var busscolumnname=$("#busscolumnname").val()
      var tablename=$("#tablename").val()
      var busscolumnid1=$("#bussColId").val()
      $.post('targetmeasuresaction.do?targetMeasures=saveDateFormula&busstableid='+busstableid+'&connid='+connid+'&busscolumnname='+busscolumnname+'&tablename='+tablename,$("#DateForm").serialize(),
      function(data)
      {
      });
     $("#dateDivId").dialog('close')

  }
  //added by Nazneen
  function saveGroups(){
      var groupName = $("#GroupName").val()
      var tabId = tableId;
      var groupsId= groupId;
//      alert(groupsId)
//      alert('tabId-->'+tabId)
      $.post('businessgroupeditaction.do?groupdetails=saveHierarchyGroups&tabId='+tabId+'&GroupName='+groupName+'&groupsId='+groupsId,function(data)  {
      if(data==1){
          alert('Group created successfully')
      }
      else
          alert('Group is not created')
        });

     $("#addGroupsDialog").dialog('close')

  }
  //end by Nazneen

        </script>
 <script>
$(function() {

/*   $("#businessTree").treeview({
                    animated: "normal",
                    persist:"cookie",
                    unique:true
                });
*/
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

//start by uday
$("#relatedTablesTree").treeview({
animated: "normal",
unique:true
});
//end by uday

$("#addTabsToSrcTree").treeview({
animated: "normal",

unique:true
});

//added by susheela start
$("#TargetMeasuresDispTree").treeview({
animated: "normal",
unique:true
});
/* $("#TargetMeasuresFactTree").treeview({
                animated: "normal",
                unique:true
            }); */
$("#TargetMeasuresFactTree2").treeview({
animated: "normal",
unique:true
});
//added by susheela over



$("#formulaDialog").dialog({
//bgiframe: true,
autoOpen: false,
height: 580,
width: 589,
position: 'top',
modal: true
});
$("#groupHierarchyDialog").dialog({
//bgiframe: true,
autoOpen: false,
height: 580,
width: 700,
position: 'top',
modal: true
});
$("#addGroupsDialog").dialog({
//bgiframe: true,
autoOpen: false,
height: 300,
width: 400,
position: 'top',
modal: true
});

$("#memberDescDialog").dialog({
//bgiframe: true,
autoOpen: false,
height: 580,
width: 520,
position: 'top',
modal: true
});
$("#addtimebaseformula").dialog({
autoOpen:false,
height:580,
width: 800,
position: 'center',
modal: true
});
$("#filterDialog").dialog({
//bgiframe: true,
autoOpen: false,
height: 580,
width: 560,
position: 'top',
modal: true
});


$("#editTimeDimDialog").dialog({
//bgiframe: true,
autoOpen: false,
height: 100,
width: 330,
position: 70,
modal: true
});

$("#selectConnection").dialog({
// bgiframe: true,
autoOpen: false,
height:250,
width:350,
modal: true,
buttons: {
Cancel: function() {

// var x=confirm('To See Dimensions Please Select Connection Are You Sure To Cancel')
// if(x==true){
$(this).dialog('close');
// }
},
'Select': function() {
saveTablesgrp();
$(this).dialog('close');
}
},
close: function() {

}
});
$("#busgrpdataDispDialog").dialog({
//bgiframe: true,
autoOpen: false,
height:400,
width: 600,
position: 70,
modal: true
});
});

function busremoveimage()
{
parent.document.getElementById('loading').style.display='none';
var checkStatus='<%=statusOfBucket%>'
if(checkStatus=="true"){
alert("bucket Created Successfully");
window.location.reload(true);
}

}
function bsgroupexits()
{
parent.document.getElementById('loading').style.display='block';
}
window.onunload = bsgroupexits;
function refreshPage(){
window.location.reload(true);
parent.document.getElementById('loading').style.display='block';
}


function checkVals(obj,id) {

//  $("#"+obj.value+id).attr('disabled','true');
//  // $("#otherDevText").attr("disabled","disabled");
            <%for (int i = 0; i < DefaultArrregations.length; i++) {%>

if(obj.value=='<%=DefaultArrregations[i]%>'){
$("#"+obj.value+id).attr('disabled',true);
}
else{
$("#"+'<%=DefaultArrregations[i]%>'+id).attr('disabled',false);
}

            <%}%>


}
            function saveTargetDetails(){
//                alert("1")
                $.post('targetmeasuresaction.do?targetMeasures=saveTragetDetails&grupId='+grupId, $("#tragetform").serialize(), function(data){
                    $("#targetDiv").dialog('close');
                    refreshPage();
                });
            }

        </script>
                                            </body>

                                            </html>
