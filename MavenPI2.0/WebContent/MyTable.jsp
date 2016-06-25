<%--
    Document   : MyTable
    Created on : Aug 7, 2009, 7:09:17 PM
    Author     : Administrator
--%>

<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"  import="com.progen.i18n.TranslaterHelper,com.progen.action.BreadCurmbBean,java.util.Locale"%>

<%--<%@page contentType="text/html" pageEncoding="UTF-8"%>--%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<jsp:useBean id="brdcrmb" class="com.progen.action.BreadCurmbBean" scope="session"/>

<%//for clearing cache
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
             Locale cle = null;
             cle = (Locale) session.getAttribute("UserLocaleFormat");
             String contextPath=request.getContextPath();
%>

<html>
    <head>
<!--        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">-->
        <title>DBConnections Page</title>
        <link rel="stylesheet" href="css/bootstrap.min.css" type="text/css"/>
        <link rel="stylesheet" href="css/bootstrap-theme.min.css" type="text/css"/>
	<link rel="stylesheet" href="css/font-awesome.min.css" type="text/css"/>
        <script src="javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>
        <link rel="stylesheet" href="stylesheets/treeviewstyle/jquery.treeview.css" />
        <link rel="stylesheet" href="stylesheets/treeviewstyle/screen.css" />
        <script src="javascript/treeview/jquery.cookie.js" type="text/javascript"></script>
        <script src="javascript/treeview/jquery.treeview.js" type="text/javascript"></script>

        <script type="text/javascript" src="javascript/treeview/demo.js"></script>

        <!--        <script type="text/javascript" src="javascript/draggable/ui.core.js"></script>
                <script type="text/javascript" src="javascript/draggable/ui.draggable.js"></script>
                <script type="text/javascript" src="javascript/draggable/ui.droppable.js"></script>-->
        <link type="text/css" href="stylesheets/ui.all.css" rel="stylesheet" />
        <!--        <script type="text/javascript" src="javascript/ui.resizable.js"></script>
                <script type="text/javascript" src="javascript/ui.accordion.js"></script>
                <script type="text/javascript" src="javascript/ui.dialog.js"></script>
                <script type="text/javascript" src="javascript/ui.sortable.js"></script>-->
        <script type="text/javascript" src="javascript/queryDesign.js"></script>
        <!--        <script type="text/javascript" src="javascript/effects.core.js"></script>
                <script type="text/javascript" src="javascript/effects.explode.js"></script>-->
        <link type="text/css" href="stylesheets/demos.css" rel="stylesheet" />
        <script language="JavaScript" src="<%=contextPath%>/querydesigner/JS/jquery.columnfilters.js"></script>
        <!-- below five lines only added by bharathi reddy on 26-08-09 -->
        <link href="stylesheets/StyleSheet.css" rel="stylesheet" type="text/css" />
        <link href="stylesheets/confirm.css" rel="stylesheet" type="text/css" />
        <link href="stylesheets/jquery.contextMenu.css" rel="stylesheet" type="text/css" />
        <script src="javascript/jquery.simplemodal-1.1.1.js" type="text/javascript"></script>
        <script src="javascript/jquery.contextMenu.js" type="text/javascript"></script>
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=session.getAttribute("theme")%>/metadataButton.css" rel="stylesheet" />
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=session.getAttribute("theme")%>/ReportCss.css" rel="stylesheet" />
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=session.getAttribute("theme")%>/ui.theme.css" rel="stylesheet" />
        <!--        <script type="text/javascript" src="javascript/ui.tabs.js"></script>-->
<link rel="stylesheet" type="text/css" href="<%=contextPath%>/css/global.css"/>
        <script>
            $(document).ready(function(){
                if ($.browser.msie == true){
                    $("#datatablist").dialog({
                        autoOpen: false,
                        height: 500,
                        width: 650,
                        position: 'top',
                        modal: true
                    });
                    $("#connectDia").dialog({
                        //bgiframe: true,
                        autoOpen: false,
                        height: 400,
                        width: 700,
                        position: 'top',
                        modal: true
                    });
                    $("#exceldiv").dialog({
                        //bgiframe: true,
                        autoOpen: false,
                        height: 350,
                        width: 500,
                        position: 'absolute',
                        modal: true
                    });
                    $("#editNetConn").dialog({
                        autoOpen: false,
                        height: 500,
                        width: 700,
                        position: 'top',
                        modal: true
                    });
                    $("#tableQuery").dialog({
                        autoOpen: false,
                        height: 300,
                        width: 400,
                        position: 'top',
                        modal: true
                    });
                    $("#dataDispDiv").dialog({
                        autoOpen: false,
                        height: 500,
                        width: 800,
                        position: 'top',
                        modal: true
                    });
                    $("#dbcontabproperties").dialog({
                        autoOpen: false,
                        height: 500,
                        width: 700,
                        position: 'top',
                        modal: true
                    });
                    $("#editDbTableRelations1").dialog({
                        autoOpen: false,
                        height: 450,
                        width: 700,
                        position: 'top',
                        modal: true
                    });
                    $("#editDbTableRelations").dialog({
                        autoOpen: false,
                        height: 450,
                        width: 700,
                        position: 'top',
                        modal: true
                    });
                    $("#addColumnDivid").dialog({
                        autoOpen: false,
                        height: 500,
                        width: 700,
                        position: 'top',
                        modal: true

//                        close: function(ev, ui) { //$(this).remove();
//                            $("#addColumnDivid").html("");
                           
                       // }


                    });

                    $("#FolderDetails").dialog({
                        autoOpen: false,
                        height: 400,
                        width: 400,
                        position: 'top',
                        modal: true
                        
                    });


                }else{
                    $("#connectDia").dialog({
                        autoOpen: false,
                        height: 400,
                        width: 700,
                        position: 'top',
                        modal: true
                    });
                    $("#exceldiv").dialog({
                        autoOpen: false,
                        height: 250,
                        width: 500,
                        position: 'absolute',
                        modal: true
                    });
                    $("#datatablist").dialog({
                        autoOpen: false,
                        height: 500,
                        width: 650,
                        position: 'top',
                        modal: true
                    });
                    $("#editNetConn").dialog({
                        autoOpen: false,
                        height: 500,
                        width: 700,
                        position: 'top',
                        modal: true
                    });
                    $("#tableQuery").dialog({
                        autoOpen: false,
                        height: 300,
                        width: 400,
                        position: 'top',
                        modal: true
                    });
                    $("#dataDispDiv").dialog({
                        autoOpen: false,
                        height: 500,
                        width: 700,
                        position: 'top',
                        modal: true
                    });
                    $("#dbcontabproperties").dialog({
                        autoOpen: false,
                        height: 500,
                        width: 700,
                        position: 'top',
                        modal: true
                    });
                    $("#editDbTableRelations1").dialog({
                        autoOpen: false,
                        height: 450,
                        width: 700,
                        position: 'top',
                        modal: true
                    });
                    $("#editDbTableRelations").dialog({
                        autoOpen: false,
                        height: 400,
                        width: 700,
                        position: 'top',
                        modal: true
                    });
                    
                    $("#addColumnDivid").dialog({
                        autoOpen: false,
                        height:500,
                        width: 700,
                        position: 'top',
                        modal: true

//                        close: function(ev, ui) {// $(this).remove();
//
//                            $("#addColumnDivid").html("");
//                            //                            alert( $("#addColumnDivid").html())
//                        }

                    });
                }
                $("#FolderDetails").dialog({
                    autoOpen: false,
                    height: 400,
                    width: 400,
                    position: 'top',
                    modal: true
                });

                $("#dataDispDialog").dialog({
                    //bgiframe: true,
                    autoOpen: false,
                    height:400,
                    width: 600,
                    position: 70,
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

                $(".openmenu1").contextMenu({ menu: 'connListMenu', leftButton: true }, function(action, el, pos) {

                    contextMenuWork2(action, el.parent("tr"), pos); });


                $(".connMenu").contextMenu({ menu: 'tabListMenu', leftButton: true }, function(action, el, pos) {
                    $(el).attr('id')
                    contextMenuWork1(action, el.parent("li"), pos);
                });

                $(".openmenu").contextMenu(
                { menu: 'myMenu', leftButton: true
                }, function(action, el, pos) {
                    //alert( $(el).parent().attr('id'));
                    tableId=$(el).parent().attr('id');
                    //connId=$(el).parent().parent().parent().parent().attr('id');
                    //alert($(el).parent().attr('id'));
                    //alert($(el).parent().parent().attr('id'));
                    //alert($(el).parent().parent().parent().attr('id'));
                    //alert($(el).parent().parent().parent().parent().attr('id'));
                    document.getElementById("tableId").value=tableId;
                    //  document.getElementById("connId").value=connId;
                    contextMenuWork(action, el.parent("li"), pos);

                });

                $("#myList").treeview({
                    animated:"slow",
                    persist: "cookie"
                });

                var opts =
                    {
                    row_border:"0px solid #CCC",
                    col_border:"0px solid #000"
                };

            });
             $(document).ready(function() {
                        $('table#filterTable2').columnFilters({alternateRowClassNames:['rowa','rowb'], excludeColumns:[0]});
                    });
                    $(document).ready(function() {
                        $('table#filterview2').columnFilters({alternateRowClassNames:['rowa','rowb'], excludeColumns:[0]});

                    });

           
        </script>

       

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
            .draggedTable{
                height:100%;
                width:250px;
                border:solid black 0px;

            }
            .draggedTable1{
                height:100%;
                width:400px;
                border:solid black 0px;

            }
            .draggedColumns{
                height:200px;
                min-width:900px;
                border:solid black 0.5px;

            }
            .myClassTest{
                height:50%;
                max-height:50%;
                min-width:500px;
                border:solid black 0.5px;

            }
            .myClassTest1{
                position:absolute;
                height:43%;
                max-height:43%;
                min-width:900px;
                overflow:auto;

            }
            .myClassTestnew{
                position:absolute;
                height:43%;
                max-height:43%;
                width:650px;
                overflow:auto;

            }
            .savedRelations{
                border:solid black 0.5px;

            }
            #accordion{
                height:95%;

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
                font:           11px verdana;
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
                width: 100%;
                height: 120%;
                background-color: black;
                z-index:1001;
                -moz-opacity: 0.5;
                opacity:.50;
                filter:alpha(opacity=50);
                overflow:auto;
            }
            .backgrnd_loading{
                display: none;
                position: absolute;
                top: 0%;
                left: 0%;
                width: 100%;
                height: 120%;
                background-color: black;
                background-image:
                    z-index:1001;
                /* -moz-opacity: 0.5;
                 opacity:.50;
                 filter:alpha(opacity=50);*/
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
            .MainTable{
                border-top-width: 1px;
                border-bottom-width: 1px;
                border-right-width: 1px;
                border-left-width: 1px;
                border-color:Black;



            }
            #relationTable{
                border-top-width: 0px;
                border-bottom-width: 0px;
                border-right-width: 0px;
                border-left-width: 0px;
                overflow:auto;
                width:550px;
            }

            .white_content1 {
                display: none;
                position: absolute;
                top: 10px;
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
                width:280px;
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
            .myClassTest2{

                height:100%;
                max-height:100%;
                width:550px;
                overflow:auto;

            }
            .savedRelations{
                border:solid black 0.5px;

            }
            #accordion{
                height:95%;

            }
            .DBConnection{
                overflow:hidden;
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
                text-align:left;
                color:#000;
            }
            #dataDisptab{
                height:400px;
                width:600px;
                overflow-y:hidden;
                position:fixed;
                padding-left: 0px;
                padding-right: 0px;
                padding-top:0px;
                padding-bottom:0px;
            }
            #mainBody{
                overflow:hidden;
                position: relative; /* fix to 'IE7 10000px padding-bottom spill over footer' problem */
                padding-left: 0px;  /*LC fullwidth */
                padding-right: 0px;  /* RC fullwidth + CC padding */
                padding-top:0px;
                padding-bottom:0px;
            }
            #connectDia input[type=text],
            #connectDia select{
                height: 25px;
                margin: 5px 0px 0px;
            }
            #connectDia input[type=button]{
                height: 25px;
                margin: 5px;
            }
        </style>

    </head>
    <body class="DBConnection" onload="dbconremoveimage()" id="mainBody">
      <table style="width:100%;height:100%" border="solid black 1px" >
            <form name="myForm2" method="post">
                <input type="hidden" name="tableId" id="tableId" value="">
                <input type="hidden" name="connId" id="connId" value="">

                <tr>
                    <td width=30%" valign="top" class="draggedTable1" >
                        <div style="height:33px;" class="prgBtn themeColor draggedDivs ui-corner-all">
                            <font style="font-weight:bold" face="verdana" size="1px">&nbsp; <%=TranslaterHelper.getTranslatedInLocale("database_connections", cle)%></font>
                            
                                <a class="fa fa-refresh themeColor" href="javascript:void(0)" onclick="refreshPage()" style="color:white;float: right;margin: 5px;"></a>
                              
                        </div>

                        <%if (request.getAttribute("buildedinjava") != null) {%>
                        <%=request.getAttribute("buildedinjava")%>
                        <%
                                    }%>
                    </td>

                    <td  valign="top" width="70%" style="height:100%">
                        <table style="height:100%; width:100%;">
                            <tr>
                                <td width="30%" style="height:100%;">
                                    <table style="height:100%;border:1px solid" class="draggedTable"  id="newDragTables">
                                        <tr>
                                            <td style="height:100%" id="draggableTables" valign="top">
                                                <h3 style="height:36px" align="left" tabindex="0" aria-expanded="true" role="tab" class="ui-accordion-header themeColor ui-helper-reset prgBtn ui-corner-all">
                                                    <font size="2" style="font-weight:bold"><%=TranslaterHelper.getTranslatedInLocale("dt_tab_relation", cle)%></font>
                                                    <div style="padding: 6px;">
                                                                <a class="fa fa-cog themeColor" title="Suggest Relations" href="javascript:autoSuggestRelations('ALL')" style='color:#ffffff; '></a>
                                                            
                                                                <a class="fa fa-star themeColor" title="Suggest Star Relations" href="javascript:autoSuggestRelations('STAR')" style='color:#ffffff; '></a>

                                                           
                                                                <a class="fa fa-times-circle-o themeColor" title="Clear All" href="javascript:clearAllTables()" style='color:#ffffff; '></a></a>
                                                        </div>
                                                </h3>
                                                <div id="accordion" style="height:553px;overflow-y:auto;color: black;">

                                                </div>
                                            </td>
                                        </tr>
                                    </table>
                                </td>
                                <td align="left" width="70%" valign="top" style="height:100%">
                                    <table style="height:100%;width:100%">
                                        <tr style="width:100%">
                                            <td style="height:50%" valign="top">
                                                <div class="myClassTest2" id="draggedCols" style="overflow:auto;width:600px">
                                                    <h3 style="height:36px;" align="left" tabindex="0" aria-expanded="true" role="tab" class="themeColor ui-accordion-header ui-helper-reset prgBtn ui-corner-all">
                                                        <font size="2" style="font-weight:bold"><%=TranslaterHelper.getTranslatedInLocale("dt_col_relation", cle)%></font>
                                                        <div style="padding: 6px;">
                                                                    <a class="fa fa-check-square themeColor" title="Add Relations" href="javascript:addAllRelations();" style='color: white; '></a>
                                                                    <a class="fa fa-times-circle-o themeColor" title="Clear All" href="javascript:clearcolRels();" style='color: white;  '></a>
                                                        </div>
                                                    </h3>
                                                    <div class="myClassTestnew" style="overflow: auto;width:580px">
                                                        <table  id="relationTable" align="left">

                                                        </table>
                                                    </div>
                                                </div>
                                            </td>
                                        </tr>
                                        <tr style="width:100%">
                                            <td style="height:50%" valign="top">
                                                <div class="myClassTest2" id="commitRel" style="width:auto">
                                                    <h3  style="height:36px;" align="left" tabindex="0" aria-expanded="true" role="tab" class="ui-accordion-header themeColor ui-helper-reset prgBtn ui-corner-all">
                                                        <font size="2" style="font-weight:bold"><%=TranslaterHelper.getTranslatedInLocale("commit_rel", cle)%> </font>
                                                        <div style="padding: 6px;">
                                                                   <a class="fa fa-check-square themeColor" title="Commit All" href="javascript:saveRelations();" style='color:white; '></a>
                                                                   <a class="fa fa-times-circle-o themeColor"  title="Clear All" href="javascript:clearRelText();" style='color:white '></a>
                                                        </div>
                                                                   
                                                    </h3>
                                                    <span style="width:50px;font-size:10px" id="relSpan"></span>

                                                </div>
                                            </td></tr>
                                    </table>
                                </td>
                            </tr>

                        </table>
                    </td>
                </tr>
                <input type="hidden" name="tabledeleteId" id="tabledeleteId" >
                <input type="hidden" name="dimtableId" id="dimtableId" >
                <input type="hidden" name="h" id="h" value="<%=request.getContextPath()%>">
            </form>
        </table>
        <div id="datatablist" title="Add Tables" style="display:none">
            <iframe  id="dataDisptab" NAME='dataDisptab'    frameborder="0" height="100%" width="100%" SRC='about:blank'></iframe>

        </div>
        <%-- <div id="dataDispDialog" title="Table Data Pad">--%>
        <div id="dataDispDiv" title="View Table" style="display:none">
            <iframe  id="dataDisp" NAME='dataDisp'  width="100%" height="100%" frameborder="0" SRC=''></iframe>

        </div>
        <div id="fade" class="black_overlay"></div>

        <ul id="connListMenu" class="contextMenu">

            <li class="insert"><a href="#insert"><%=TranslaterHelper.getTranslatedInLocale("create_new_con", cle)%></a></li>

        </ul>

        <ul id="tabListMenu" class="contextMenu">
            <li class="insert"><a href="#addTables"><%=TranslaterHelper.getTranslatedInLocale("add_tables", cle)%></a></li>
            <li class="addtablesasquery"><a href="#addtablesasquery"><%=TranslaterHelper.getTranslatedInLocale("add_table_as_query", cle)%> </a></li>
            <li class="edietnetworkconnection"><a href="#edietnetworkconnection"><%=TranslaterHelper.getTranslatedInLocale("edit_network_connection", cle)%></a></li>
            <li class="deletenetworkconnection"><a href="#deletenetworkconnection"><%=TranslaterHelper.getTranslatedInLocale("delete_network_connection", cle)%></a></li>
        </ul>
        <ul id="myMenu" class="contextMenu">
            <li class="viewTable"><a href="#viewTable"><font class="text_classstyle1"><%=TranslaterHelper.getTranslatedInLocale("view_table", cle)%></font></a></li>
            <li class="viewRelateddbTables"><a href="#viewRelateddbTables"><font class="text_classstyle1"><%=TranslaterHelper.getTranslatedInLocale("view_related_DB_tables", cle)%></font></a></li>
            <li class="deleteTable"><a href="#deleteTable"><font class="text_classstyle1"><%=TranslaterHelper.getTranslatedInLocale("delete_table", cle)%></font></a></li>
            <%--<li class="addColumns"><a href="#addColumns"><font class="text_classstyle1">Add Columns</font></a></li>
           <li class="deleteColumns"><a href="#deleteColumns"><font class="text_classstyle1">DeleteColumns</font></a></li>--%>
            <li class="relateddbTables"><a href="#relateddbTables"><font class="text_classstyle1"><%=TranslaterHelper.getTranslatedInLocale("Migrate_Db_Table_Relation", cle)%></font></a></li>
            <li class="addColumntoTable"><a href="#addColumntoTableTillGroup"><font class="text_classstyle1"><%=TranslaterHelper.getTranslatedInLocale("Add_Column_To_Table_Till_Group", cle)%></font></a></li>
            
            <li class="addColumntoTable"><a href="#addColumntoTable"><font class="text_classstyle1"><%=TranslaterHelper.getTranslatedInLocale("Add_Column_To_Table", cle)%></font></a></li>
            <li class="tableproperties"><a href="#tableproperties"><font class="text_classstyle1"><%=TranslaterHelper.getTranslatedInLocale("Table_Properties", cle)%></font></a></li>

        </ul>
        <div id="dbcontabproperties" title="Table Properties"></div>
        <div id="tabColDiv" style="width:700px;display:none" class="white_content1" >
            <iframe id="tabDiv" frameborder="0" width="100%" height="100%" name="tabDiv" src=''></iframe>
        </div>

        <script>
                var tableActive;
                var $dragTables=$('#tableList > li >span')
                var $draggableTables=$('#draggableTables');

                $($dragTables).draggable({
                    helper:"clone",
                    effect:["", "fade"]
                });


                $($draggableTables).droppable(
                {
                    activeClass:"blueBorder",
                    accept:'#tableList > li >span',
                    drop: function(ev, ui) {
                        //for(a in ui.draggable){
                        //alert(ui.draggable.html());
                        /*  alert(a)
                }*/
                        //$draggableTables.html(ui.draggable.html());

                        var tab=ui.draggable.html();
                        //alert(tableNamesDrag.match(tab)+tab)
                        if(tableNamesDrag.match(tab)!=tab ||tableNamesDrag.match(tab)==null){

                            // tabIdArr.length=count;
                            var idTab=ui.draggable.attr("id").split("-");
                            if(tabDetailsArray[tab]==undefined){


                                tabDetailsArray[tab]=true;
                            }
                            else{
                                tabDetailsArray[tab]=false;
                            }
                            tabIdArr.push(idTab[1]);
                            tabNameArr.push(tab)
                            tableNamesDrag=tabNameArr.toString()
                            count++;
                            var liObject=document.getElementById(idTab[1])
                            var ulObject=liObject.getElementsByTagName("ul");
                            //                        //                    alert("id\t"+ulObject[0].id)
                            //                        getTableDetails(ulObject[0].id);
                            getTableDetails(ui.draggable.html(),ulObject[0].id);
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
            <p><span class="ui-icon ui-icon-alert" style="float:left; margin:0 7px 20px 0;"></span><%=TranslaterHelper.getTranslatedInLocale("All_the_items", cle)%></p>
        </div>
        <div id="successMsg" title="Operation Success">
            <p>
                <span  class="ui-icon ui-icon-circle-check" style="float:left; margin:0 7px 50px 0;"></span>
                <%=TranslaterHelper.getTranslatedInLocale("Relations_Saved_Successfully", cle)%>
            </p>

        </div>
        <div id="failureMsg" title="Operation Failure">
            <p>
                <span  class="ui-icon ui-icon-alert" style="float:left; margin:0 7px 50px 0;"></span>
                <%=TranslaterHelper.getTranslatedInLocale("RelationsCantBesaved", cle)%>
            </p>

        </div>
        <div id="warning" title="Message">
            <p>
                <span  class="ui-icon ui-icon-alert" style="float:left; margin:0 7px 50px 0;"></span>
            <div id="alert"></div>
        </p>

    </div>
    <%--added by susheela for delete connection start --%>
    <div id="warningDelete" title="Message">
        <p>
            <span  class="ui-icon ui-icon-alert" style="float:left; margin:0 7px 50px 0;"></span>
        <div id="alertDelete"></div>
    </p>
</div>
<div id="editDbTableRelations1" title="Migrate DB Tables" style="display:none">
    <iframe  id="editDbTableRelationsFrame1" NAME='editDbTableRelationsFrame1' frameborder="0" height="100%" width="100%" SRC='#'></iframe>
</div>
<div id="editDbTableRelations" title="View Related DB Tables" style="display:none">
    <iframe  id="editDbTableRelationsFrame" NAME='editDbTableRelationsFrame' frameborder="0" height="100%" width="100%" SRC='#'></iframe>
</div>
<div id="fade" class="black_overlay"></div>
<%--added by susheela for delete connection over --%>
<div id="removeTable" title="Remove The Table?">
    <p><span class="ui-icon ui-icon-alert" style="float:left; margin:0 7px 20px 0;"></span><%=TranslaterHelper.getTranslatedInLocale("The_table_will", cle)%></p>
</div>
<div id="editNetConn" style="display:none" title="Edit Network Connection" >
    <iframe id="editNetConnFrame" frameborder="0" width="100%" height="100%" name="editNetConnFrame" src="#"></iframe>
</div>

<div id="tableQuery" style="display:none" title="Add Table As Query">
    <iframe id="tableQueryFrame" frameborder="0" width="100%" height="100%" name="tableQueryFrame" src=''></iframe>
</div>

<div id="connectDia" title="Create Connection" class="white" >
    <form name="myForm1" method="post" action="">
        <input type="hidden" id="dbcode" name="dbcode">
        <br><br><br><br>
        <center>
            <table style="width:100%;height:auto;border:0px solid">
                <tr>
                    <td align="center">
                        <table style="width:50%">
                            <tr>
                                <td class="myHead" style="width:50%">
                                    <label class="label" ><%=TranslaterHelper.getTranslatedInLocale("Connection_Name", cle)%></label>
                                </td>
                                <td  style="width:50%">
                                    <input type="text" name="connectionname" id="connectionname" style="width:150px">
                                </td>
                            </tr>
                            <tr>
                                <td class="myHead" style="width:50%">
                                    <label class="label" ><%=TranslaterHelper.getTranslatedInLocale("Database", cle)%></label>
                                </td>
                                <td style="width:50%">
                                    <select id="dbname" name="dbname" onchange="getdatabase()" style="width:153px">
                                        <option value="none">----select----</option>
                                        <option value="oracle"><%=TranslaterHelper.getTranslatedInLocale("Oracle", cle)%></option>
                                        <option value="excel"><%=TranslaterHelper.getTranslatedInLocale("Excel", cle)%></option>
                                        <option value="Mysql"><%=TranslaterHelper.getTranslatedInLocale("Mysql", cle)%></option>
                                        <option value="SqlServer"><%=TranslaterHelper.getTranslatedInLocale("Sql_Server", cle)%></option>
                                        <option value="DB2"><%=TranslaterHelper.getTranslatedInLocale("DB2", cle)%></option>
                                        <option value="postgreSQL"><%=TranslaterHelper.getTranslatedInLocale("PostgreSQL", cle)%></option>
                                    </select>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
                <tr>
                    <td align="center">
                        <div id="oraclediv" style="display:none;width:650px;border-width:thick">
                            <table style="width:50%">
                                <tr>
                                    <td class="myHead" style="width:50%">
                                        <label class="label" ><%=TranslaterHelper.getTranslatedInLocale("Username", cle)%></label>
                                    </td>
                                    <td style="width:50%">
                                        <input type="text" name="username" id="username" style="width:150px">
                                    </td>
                                </tr>
                                <tr>
                                    <td class="myHead" style="width:50%">
                                        <label class="label" ><%=TranslaterHelper.getTranslatedInLocale("Password", cle)%></label>
                                    </td>
                                    <td style="width:50%">
                                        <input type="password" name="password" id="password" style="width:150px">
                                    </td>
                                </tr>
                                <tr>
                                    <td class="myHead" style="width:50%">
                                        <label class="label" ><%=TranslaterHelper.getTranslatedInLocale("Server", cle)%></label>
                                    </td>
                                    <td style="width:50%">
                                        <input type="text" name="server" value="localhost" id="server" style="width:150px">
                                    </td>
                                </tr>
                                <tr>
                                    <td class="myHead" style="width:50%">
                                        <label class="label" ><%=TranslaterHelper.getTranslatedInLocale("SId", cle)%></label>
                                    </td>
                                    <td style="width:50%">
                                        <input type="text" name="Serviceid" id="Serviceid" style="width:150px">
                                    </td>
                                </tr>
                                <tr>
                                    <td class="myHead" style="width:50%">
                                        <label class="label"><%=TranslaterHelper.getTranslatedInLocale("Port", cle)%></label>
                                    </td>
                                    <td style="width:50%">
                                        <input type="text" name="Port" value="1521" id="Port" style="width:150px">
                                    </td>
                                </tr>
                            </table>
                        </div>
                        <div id="exceldiv" style="display:none; " align="center">
                            <iframe name="uploadexcelframe" id="uploadexcelframe" src="#" scrolling="no" style="width:100%;height:100%" frameborder="0"></iframe>
                            <table style="width:50%">

                                <%--<tr>
                                            <td class="myHead" style="width:50%">
                                                <label class="label" >DataSourceName</label>
                                            </td>
                                    <td style="width:50%">
                                                <input type="text" name="exceldsn" id="exceldsn" style="width:150px">
                                            </td>
                                        </tr>


                                        <tr>
                                            <td class="myHead" style="width:50%">
                                                <label class="label" >Select File</label>
                                            </td>
                                    <td style="width:50%">
                                                <input type="file" name="path1" id="path1" style="width:150px">
                                            </td>

                                </tr>--%>
                            </table>

                        </div>
                        <div id="sqlserverdiv" style="display:none;width:650px">
                            <table style="width:50%">

                                <tr>
                                    <td class="myHead" style="width:50%">
                                        <label class="label" ><%=TranslaterHelper.getTranslatedInLocale("database_name", cle)%></label>
                                    </td>
                                    <td style="width:50%">
                                        <input type="text" name="sqlserverdbname" id="sqlserverdbname" style="width:150px">
                                    </td>
                                </tr>
                                <tr>
                                    <td class="myHead" style="width:50%">
                                        <label class="label" ><%=TranslaterHelper.getTranslatedInLocale("Username", cle)%></label>
                                    </td>
                                    <td style="width:50%">
                                        <input type="text" name="sqlserverusername" id="sqlserverusername" style="width:150px">
                                    </td>
                                </tr>
                                <tr>
                                    <td class="myHead" style="width:50%">
                                        <label class="label" ><%=TranslaterHelper.getTranslatedInLocale("Password", cle)%></label>
                                    </td>
                                    <td style="width:50%">
                                        <input type="password" name="sqlserverpswrd" id="sqlserverpswrd" style="width:150px">
                                    </td>
                                </tr>
                                <tr>
                                    <td class="myHead" style="width:50%">
                                        <label class="label" ><%=TranslaterHelper.getTranslatedInLocale("HostName", cle)%></label>
                                    </td>
                                    <td style="width:50%">
                                        <input type="text" name="sqlserverhostname" id="sqlserverhostname" style="width:150px">
                                    </td>
                                </tr>
                                <tr>
                                    <td class="myHead" style="width:50%">
                                        <label class="label" ><%=TranslaterHelper.getTranslatedInLocale("PortNumber", cle)%></label>
                                    </td>
                                    <td style="width:50%">
                                        <input type="text" name="sqlserverportnum" id="sqlserverportnum" style="width:150px">
                                    </td>
                                </tr>
                            </table>
                        </div>
                        <div id="postgrediv" style="display:none;width:650px;border-width:thick">
                            <table style="width:50%">
                                <tr>
                                    <td class="myHead" style="width:50%">
                                        <label class="label" ><%=TranslaterHelper.getTranslatedInLocale("Username", cle)%></label>
                                    </td>
                                    <td style="width:50%">
                                        <input type="text" name="pstgreusername" id="pstgreusername" style="width:150px">
                                    </td>
                                </tr>
                                <tr>
                                    <td class="myHead" style="width:50%">
                                        <label class="label" ><%=TranslaterHelper.getTranslatedInLocale("Password", cle)%></label>
                                    </td>
                                    <td style="width:50%">
                                        <input type="password" name="pstgrepassword" id="pstgrepassword" style="width:150px">
                                    </td>
                                </tr>
                                <tr>
                                    <td class="myHead" style="width:50%">
                                        <label class="label" ><%=TranslaterHelper.getTranslatedInLocale("Server", cle)%></label>
                                    </td>
                                    <td style="width:50%">
                                        <input type="text" name="postgresserver" value="" id="postgresserver" style="width:150px">
                                    </td>
                                </tr>
                                <tr>
                                    <td class="myHead" style="width:50%">
                                        <label class="label"><%=TranslaterHelper.getTranslatedInLocale("Port", cle)%></label>
                                    </td>
                                    <td style="width:50%">
                                        <input type="text" name="pstgrePort" value="5432" id="pstgrePort" style="width:150px">
                                    </td>
                                </tr>
                                <tr>
                                    <td class="myHead" style="width:50%">
                                        <label class="label" ><%=TranslaterHelper.getTranslatedInLocale("database_name", cle)%></label>
                                    </td>
                                    <td style="width:50%">
                                        <input type="text" name="pstgredbname" id="pstgredbname" style="width:150px">
                                    </td>
                                </tr>
                            </table>
                        </div>
                        <div id="mysqldiv" style="display:none;width:650px;border-width:thick">
                            <table  style="width:50%">
                                <tr>
                                    <td class="myHead" style="width:50%">
                                        <label class="label" ><%=TranslaterHelper.getTranslatedInLocale("DB_Name", cle)%></label>
                                    </td>
                                    <td style="width:50%">
                                        <input type="text" name="mysqldbname" id="mysqldbname" style="width:150px">
                                    </td>
                                </tr>
                                <tr>
                                    <td class="myHead" style="width:50%">
                                        <label class="label" ><%=TranslaterHelper.getTranslatedInLocale("Server", cle)%></label>
                                    </td>
                                    <td style="width:50%">
                                        <input type="text" name="mysqlserver" id="mysqlserver" value="localhost" style="width:150px">
                                    </td>
                                </tr>
                                <tr>
                                    <td class="myHead" style="width:50%">
                                        <label class="label" ><%=TranslaterHelper.getTranslatedInLocale("Port", cle)%></label>
                                    </td>
                                    <td style="width:50%">
                                        <input type="text" name="mysqlport" id="mysqlport" value="3306" style="width:150px">
                                    </td>
                                </tr>
                                <tr>
                                    <td class="myHead" style="width:50%">
                                        <label class="label" ><%=TranslaterHelper.getTranslatedInLocale("Username", cle)%></label>
                                    </td>
                                    <td style="width:50%">
                                        <input type="text" name="mysqlusername" id="mysqlusername" style="width:150px">
                                    </td>
                                </tr>
                                <tr>
                                    <td class="myHead" style="width:50%">
                                        <label class="label" ><%=TranslaterHelper.getTranslatedInLocale("Password", cle)%></label>
                                    </td>
                                    <td style="width:50%">
                                        <input type="password" name="mysqlpassword" id="mysqlpassword" style="width:150px">
                                    </td>
                                </tr>

                            </table>
                        </div>
                        <div id="addColumnDivid"style="height: 515px; min-height: 109px; width: 670px;display: none" title="Add column to table">

                            <iframe name="addColumnframe" id="addColumnframe" src="#" scrolling="yes" style="width:100%;height:100%" frameborder="0"></iframe>

                        </div>
                        <div id="FolderDetails" style="display: none" title="Folder details">

                        </div>
                    </td></tr><tr><td align="center">
                        <table>
                            <tr>
                                <td><input type="button" class="prgBtn" value="Test Connection" onclick="testconnection()"></td>
                                <td><input type="button" class="prgBtn" value="Save Connection" onclick="getconnection()"></td>

                                <td><input type="button" class="prgBtn" value="Cancel" onclick="cancelFade()"></td>
                            </tr>
                        </table>

                    </td></tr></table>
        </center>
    </form>
</div>
 <script>
             function closeexclediv(){
                $("#exceldiv").dialog('close');
                window.location.href = window.location.href;
            }
            function unloadPage()
            {
                //alert("unload event detected!");
                parent.document.getElementById("fade1").style.display = 'block';
            }
            window.onunload = unloadPage;
            var tableId="";
            //var connId="";

            function refreshEditConnection()
            {   $("#editNetConn").dialog('close');
            <%--document.getElementById("editNetConn").style.display='none';
            document.getElementById('fade').style.display='none';--%>
                    //window.location.reload(true);
                }
                function refreshAddQTableCols(data)
                {
                    document.getElementById("editNetConn").innerHTML="";

                    // document.getElementById('fade').style.display='none';//tabColDiv
                    var DivObj=document.getElementById("tableQuery");
                    var FrameObj=document.getElementById("tableQueryFrame");
                    FrameObj.src="pbAddTableAsQueryViewTable.jsp?data="+data;
                    // document.getElementById('fade').style.display='block';
                    // DivObj.style.display="block";
                    $("#tableQuery").dialog('open');
                    $("#editNetConn").dialog('open');
                }
                function refreshAddQTable()
                {   $("#tableQuery").dialog('close');
            <%--document.getElementById("tableQuery").style.display='none';
            document.getElementById('fade').style.display='none';--%>
                }

                //                $(document).ready(function() {
                //
                //
                //                });

                //                function confirm(message) {
                //                    $("#confirm").modal({
                //                        close: true,
                //                        overlayId: 'confirmModalOverlay',
                //                        containerId: 'confirmModalContainer',
                //                        onClose: modalOnClose,
                //                        onShow: function modalShow(dialog) {
                //                            dialog.overlay.fadeIn('slow', function() {
                //                                dialog.container.fadeIn('fast', function() {
                //                                    dialog.data.hide().slideDown('slow');
                //                                });
                //                            });
                //
                //                            dialog.data.find(".confirmmessage").append(message);
                //
                //                            // Yes button clicked
                //                            dialog.data.find("#ButtonYes").click(function(ev) {
                //                                ev.preventDefault();
                //                                $.modal.close();
                //                                alert('The customer with id ' + $("#HiddenFieldRowId").val() + ' would of been deleted.');
                //                                //$("#ButtonDeleteCustomer").click();
                //                            });
                //                        }
                //                    })
                //                }

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

                //added by susheela start

                function deleteConnection(conId)
                {  //alert('here');

                    $.ajax({
                        url: 'editconn.do?parameter=deleteUserConnection&connectionId='+conId,
                        success: function(data) {
                            //alert(data);
                            if(data==1)
                                alert("Unable to Delete Connection.Some Business Group exists.")
                            else{

                                alert("Connection deleted successfully.")
                                window.location.reload(true);

                            }

                        }
                    });

                }
                //added by susheela over

                function contextMenuWork(action, el, pos) {
                    //alert( $(el).parent().attr('id'));

                    switch (action) {
                        case "viewTable":
                            {
                                //alert(this.id);
                                getData(tableId);
                                break;
                            }
                        case "deleteTable":
                            {
                                // alert("deleteTable\t"+tableId)
                                var confirmVal=confirm("Are you sure you want to Delete ?")
                                if(confirmVal==true){
                                    $.ajax({
                                        url:'<%= request.getContextPath()%>/editconn.do?parameter=deleteTable&tableId='+tableId,
                                        success:function(data){
                                            if(data=='true'){
                                                refreshPage();
                                            }else{
                                                alert("Unable to delete ,table is used in Business Group")
                                            }
                                        }
                                    });
                                }
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
                        case "tableproperties":
                            {
                                //  alert("tableproperties");
                                // alert(tableId);
                                showtabproperties();
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
                        //added by sunita`
                    case "relateddbTables":
                        {
                            // alert('in related Db Tables');
                            var tabId= $(el).attr('id');//$(el).parent().attr('id');
                            var con=$(el).parent().attr('id');
                            // alert(con+' con tabId '+tabId);
                            var frameObj2=document.getElementById("editDbTableRelationsFrame1");
                            //frameObj2.style.display='block';
                            //  document.getElementById("editDbTableRelations").style.display='block';
                            //alert('in view drill.');
                            //   document.getElementById('fade').style.display='block';
                            //alert('in view subFolId '+subFolId);
                            var source2 = "pbEditDbTableRealation.jsp?dbTableId="+tabId;
                            frameObj2.src=source2;
                            $("#editDbTableRelations1").dialog('open')
                            break;
                        }
                    case "viewRelateddbTables":
                        {
                            var tabId= $(el).attr('id');//$(el).parent().attr('id');
                            var con=$(el).parent().attr('id');
                            //alert(con+' con tabId '+tabId);
                            var frameObj2=document.getElementById("editDbTableRelationsFrame");
                            //  frameObj2.style.display='block';
                            //    document.getElementById("editDbTableRelations").style.display='block';
                            //alert('in view drill.');
                            //    document.getElementById('fade').style.display='block';
                            //alert('in view subFolId '+subFolId);
                            var source2 = "pbViewDbRelatedTabels.jsp?dbTableId="+tabId;
                            frameObj2.src=source2;
                            $("#editDbTableRelations").dialog('open')
                            break;
                        }
                    case "addColumntoTable":
                        {
                            var tabId= $(el).attr('id');
                            var con=$(el).parent().attr('id');
                            // alert(con+' con tabId '+tabId);
                            var tableName=$("#tableId-"+tabId).html();
                            var frameObj5=document.getElementById("addColumnframe");
                            var source5="addColumnToTable.jsp?tableId="+tabId+"&tableName="+tableName;
                            frameObj5.src=source5;
                            $("#addColumnDivid").dialog('open')
                            //$("#addColumnDivid").html("");
                           // $('<iframe id="addColumnframe" name="addColumnframe"  style="width:100%;height:100%" frameborder="0" src="<%= request.getContextPath()%>/addColumnToTable.jsp?tableId='+tabId+'&tableName='+tableName+'>').appendTo($("#addColumnDivid"));
                            //                       $("#addColumnToTable").src="/addColumnToTable.jsp";

                            break;
                        }
                        case "addColumntoTableTillGroup":
                        {
                            var tabId= $(el).attr('id');
                            var con=$(el).parent().attr('id');
                            // alert(con+' con tabId '+tabId);
                            var tableName=$("#tableId-"+tabId).html();
                            var frameObj5=document.getElementById("addColumnframe");
                            var source5="addColumnToTable.jsp?tableId="+tabId+"&tableName="+tableName+"&flag=true";
                            frameObj5.src=source5;
                            $("#addColumnDivid").dialog('open')
                            //$("#addColumnDivid").html("");
                           // $('<iframe id="addColumnframe" name="addColumnframe"  style="width:100%;height:100%" frameborder="0" src="<%= request.getContextPath()%>/addColumnToTable.jsp?tableId='+tabId+'&tableName='+tableName+'>').appendTo($("#addColumnDivid"));
                            //                       $("#addColumnToTable").src="/addColumnToTable.jsp";

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

                function showtabproperties()
                {
                    //alert("table id is"+tableId);
                    $.ajax({
                        url: '<%=request.getContextPath()%>/Dbcontablepropertiesaction.do?method=tableproperties&tableid='+tableId,
                        success: function(data){
            <%-- document.getElementById("dbcontabproperties").style.display='block';
             document.getElementById('fade').style.display='block';--%>
                             $("#dbcontabproperties").dialog('open');
                             document.getElementById("dbcontabproperties").innerHTML = data;
                             $("#tabs").tabs().find(".ui-tabs-nav").sortable({axis:'x'});
                         }
                     });
                 }
                 function closetabproperties()
                 {   $("#dbcontabproperties").dialog('close');
            <%--document.getElementById("dbcontabproperties").style.display='none';
            document.getElementById('fade').style.display='none';--%>
                }
                function contextMenuWork2(action, el, pos) {

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

                                //  alert($(el).attr('id'));

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
                        case "edietnetworkconnection":
                            {
                                var DivObj=document.getElementById("editNetConn");
                                var FrameObj=document.getElementById("editNetConnFrame");
                                FrameObj.src="pbEditConnection.jsp?connectionId="+$(el).attr('id');
                                // document.getElementById('fade').style.display='block';
                                //   DivObj.style.display="block";
                                $("#editNetConn").dialog('open');
                                break;
                            }
                        case"deletenetworkconnection":
                            {
                                //                          alert("connid\t"+$(el).attr('id'))
                                var connID=$(el).attr('id');
                                $.ajax({
                                    url:'<%= request.getContextPath()%>/editconn.do?parameter=deleteNetWorkConnection&connID='+connID,
                                    success:function(data){

                                        if(data=="true"){
                                            alert(" Network Connection Deleted Successfully.")
                                            refreshPage()
                                        }else{
                                            alert("Please delete the related BussnessAreas of this Connection ")
                                        }
                                    }
                                })

                                break;
                            }
                        case "addtablesasquery":
                            {
                                //alert("addtablesasquery");
                                var DivObj=document.getElementById("tableQuery");
                                var conId=$(el).attr('id');
                                // alert('conId '+conId);
                                var FrameObj=document.getElementById("tableQueryFrame");
                                FrameObj.src="pbAddTableAsQuery.jsp?connectionId="+conId;
                                // document.getElementById('fade').style.display='block';
                                // DivObj.style.display="block";
                                $("#tableQuery").dialog('open');

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

                    $(function() {
                        $("#abcd").draggable();
                    });

                   
                    function tableList(span){
                        //document.getElementById("activeConnection").value=(span.innerHTML);
                        // document.getElementById('type').style.display='block';
                        // document.getElementById('fade').style.display='block';

                        //                    alert(span)
                        var frameObj = document.getElementById('dataDisptab');
                        //alert(document.getElementById("conList").innerHTML);
                        //window.open("TableList.jsp?connection="+span, "window.optablelist", "status=1,width=350,height=375");
                        var source="TableList.jsp?connection="+span;
                        frameObj.src=source;
                        // document.getElementById('datatablist').style.display='';
                        //document.getElementById('dataDisptab').style.display='block';
                        // document.getElementById('fade').style.display='block';
                        $("#datatablist").dialog('open')
                        //datatablist
                        //
                        //alert('list');
                        //document.getElementById("type").style.display='';
                    }

                    function getData(tableIds){
                        var frameObj=document.getElementById("dataDisp");
                        // alert('tableIds==='+tableIds);
                        // alert('connId==='+connId);
                        var source="pbViewTable.jsp?tableIds="+tableIds;
                        frameObj.src=source;
                        // frameObj.style.display="block";
                        //  document.getElementById('fade').style.display='block';
                        // document.getElementById("dataDispDialog").innerHTML = "<iframe class=frame1 src=pbViewTable.jsp?tableIds="+tableIds+"></iframe>";
                        $('#dataDispDiv').dialog('open');

                    }

                    function getTableId(obj){
                        //var e=event || window.event;
                        //alert(obj.id)
                        //alert(connectId)
                        //  connId=connectId;

                        tableId=obj.id;
                    }
                    function TableList1(span){
                        document.getElementById("activeConnection").value=(span.innerHTML);
                    }


                    function createConnection(){
                        // document.getElementById('connection').style.display='block';
                        // document.getElementById('fade').style.display='block';
                        //alert('list');
                        //document.getElementById("type").style.display='';
                        $("#connectDia").dialog('open');
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


                    function getconnection()
                    {
                        var databaseName= document.getElementById("dbname").value
//                        if(document.getElementById("dbname").value=='SqlServer'){
//                            // alert("Database is an Sql Server database");
                            document.myForm1.action="querydesigner/JSPS/pbCheckConnection.jsp";
                            document.myForm1.submit();
            
//                        }
                       
                    }


                    function getdatabase()
                    {
                        // alert(document.getElementById('dbname').value);
                        if(document.getElementById('dbname').value=="oracle"){
                            document.getElementById('oraclediv').style.display='';
                            document.getElementById('sqlserverdiv').style.display='none';
                            document.getElementById('exceldiv').style.display='none';
                            document.getElementById('postgrediv').style.display='none';
                            document.getElementById('mysqldiv').style.display='none';
                            document.getElementById('dbcode').value = '1';
                            //  alert("dbcode-->"+document.getElementById('dbcode').value);
                        }else if(document.getElementById('dbname').value=="excel"){
                            document.getElementById('oraclediv').style.display='none';
                            document.getElementById('sqlserverdiv').style.display='none';
                            document.getElementById('postgrediv').style.display='none';
                            document.getElementById('mysqldiv').style.display='none';
                            var frameobj = document.getElementById("uploadexcelframe");
                            frameobj.src = "<%=request.getContextPath()%>/UploadExcelTable.jsp";
                            $("#connectDia").dialog('close');
                            $("#exceldiv").dialog('open');
                            //document.getElementById('exceldiv').style.display='';
                            document.getElementById('dbcode').value = '2';
                            //  alert("dbcode-->"+document.getElementById('dbcode').value);
                        } else if(document.getElementById('dbname').value=="postgreSQL"){
                            document.getElementById('oraclediv').style.display='none';
                            document.getElementById('exceldiv').style.display='none';
                            document.getElementById('postgrediv').style.display='block';
                            document.getElementById('sqlserverdiv').style.display='none';
                            document.getElementById('mysqldiv').style.display='none';
                            document.getElementById('dbcode').value = '3';
                        }else if(document.getElementById('dbname').value=="SqlServer"){
                            document.getElementById('oraclediv').style.display='none';
                            document.getElementById('exceldiv').style.display='none';
                            document.getElementById('postgrediv').style.display='none';
                            document.getElementById('sqlserverdiv').style.display='block';
                            document.getElementById('mysqldiv').style.display='none';
                            document.getElementById('dbcode').value = '4';
                        }else if(document.getElementById('dbname').value=="Mysql"){
                            document.getElementById('oraclediv').style.display='none';
                            document.getElementById('exceldiv').style.display='none';
                            document.getElementById('postgrediv').style.display='none';
                            document.getElementById('sqlserverdiv').style.display='none';
                            document.getElementById('mysqldiv').style.display='block';
                            document.getElementById('dbcode').value = '5';
                        }else if(document.getElementById('dbname').value=="none"){
                            document.getElementById('oraclediv').style.display='none';
                            document.getElementById('exceldiv').style.display='none';
                            document.getElementById('postgrediv').style.display='none';
                            document.getElementById('sqlserverdiv').style.display='none';
                        }else{
                            document.getElementById('oraclediv').style.display='none';
                            document.getElementById('exceldiv').style.display='none';
                            document.getElementById('postgrediv').style.display='none';
                            document.getElementById('sqlserverdiv').style.display='none';
                        }

                    }

                    function cancelFade()
                    {
                        //document.getElementById('fade').style.display='none';
                        //document.getElementById('type').style.display='none';
                        //document.getElementById('connection').style.display='none';
                        $("#connectDia").dialog('close');
                    }


                    function testconnection(){
                        var un = document.getElementById("username").value;
                        var pwd = document.getElementById("password").value;
                        var s = document.getElementById("server").value;
                        var sid = document.getElementById("Serviceid").value;
                        var p = document.getElementById("Port").value;
                        var dbname = document.getElementById("dbname").value;
           
                        //alert("dbname"+dbname)
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
                        else if(dbname=='excel'){
                            var dsn = document.getElementById("exceldsn").value;
                            var url=ctxPath+"/TestExcelConnection";
                            url=url+"?dsn="+dsn;

                        }
                        else if(dbname=='postgreSQL'){
                            var username = document.getElementById("pstgreusername").value;
                            var password = document.getElementById("pstgrepassword").value;
                            var port = document.getElementById("pstgrePort").value;
                            var server = document.getElementById("postgresserver").value;
                            alert("server"+server)
                            var pstgredbname = document.getElementById("pstgredbname").value;
                            var url="<%=request.getContextPath()%>"+"/validateConnectionAction.do?typeofdatabase=testPostgreConnection";
                            url=url+"&usrename="+username+"&password="+password+"&port="+port+"&server="+server+"&pstgredbname="+pstgredbname;
                            //alert(url);
                        }else if(dbname=='Mysql'){
                            var username = document.getElementById("mysqlusername").value;
                            var password = document.getElementById("mysqlpassword").value;
                            var port = document.getElementById("mysqlport").value;
                            var server = document.getElementById("mysqlserver").value;
                            var mysqldbname = document.getElementById("mysqldbname").value;
                            var url="<%=request.getContextPath()%>"+"/validateConnectionAction.do?typeofdatabase=testMySqlConnection";
                            url=url+"&usrename="+username+"&password="+password+"&port="+port+"&server="+server+"&mysqldbname="+mysqldbname;
                            //alert(url);
                        }else if(dbname=="SqlServer"){
//                            alert("in SqlServer")
                            var sqlserverdbname = document.getElementById("sqlserverdbname").value;
                            var username = document.getElementById("sqlserverusername").value;
                            var password = document.getElementById("sqlserverpswrd").value;
                            var hostName = document.getElementById("sqlserverhostname").value;
                            var port = document.getElementById("sqlserverportnum").value;
//                            var url=ctxPath+"/TestConnectionSqlserver";
//                            url=url+"?usrename="+username+"&password="+password+"&port="+port+"&hostName="+hostName+"&seqlServer="+dbname+"&sqlserverdbname="+sqlserverdbname;
                            $.ajax({
                            url:"<%= request.getContextPath()%>/editconn.do?parameter=tesetConnection&usrename="+username+"&password="+password+"&port="+port+"&hostName="+hostName+"&seqlServer="+dbname+"&sqlserverdbname="+sqlserverdbname,
                            success:function(data){

                           alert(data)
                            }});
                        }
                        xmlHttp.onreadystatechange=stateChanged;
                        xmlHttp.open("GET",url,true);
                        xmlHttp.send(null);


                    }

                    function showDeleteTableColumns(str,str1)
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

                        var url=ctxPath+"/DBTableDeleteColumns";
                        url=url+"?"+str+"&tabledeleteId="+str1;

                        xmlHttp2.onreadystatechange=stateChangedDeleteTableColumns;
                        xmlHttp2.open("GET",url,true);
                        xmlHttp2.send(null);
                    }


                    function stateChangedDeleteTableColumns()
                    {
                        // alert('hi in target')

                        if (xmlHttp2.readyState==4)
                        {
                            var output=xmlHttp2.responseText;

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
                        //document.getElementById('connection').style.display='none';
                        //document.getElementById('fade').style.display='none';
                        $("#connectDia").dialog('close');
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
                        // alert(tableId);
                        document.getElementById("tabledeleteId").value=tableId;
                        var i=0;
                        var obj=document.myForm2.chk2;
                        // alert(obj.length)
                        var reqchk='';
                        for(var j=0;j<obj.length;j++)
                        {
                            if(document.myForm2.chk2[j].checked==true)
                            {

                                reqchk+="&"+"chk2="+document.myForm2.chk2[j].value;
                                i++;

                            }
                        }

                        reqchk=reqchk.substring(1);
                        // alert( reqchk);
                        if(i>0)
                        {

                            showDeleteTableColumns(reqchk,tableId);
                        }
                        else
                        {

                            alert('Unselected Columns Are Deleted please select atleast one column');

                        }
                    }
                    function refreshIframe(source){
                        // alert("source is "+source)
                        var frameObj=document.getElementById("dataDisp");
                        frameObj.src=source;
                    }
                    function refreshparent()
                    {
                        document.myForm2.action="getAllTables.do";
                        document.myForm2.submit();
                    }
                    function cancelTableList()
                    {
                        //document.myForm2.action="getAllTables.do";
                        // document.myForm2.submit();

                        //   document.getElementById("dataDisp").style.display='';
                        //   document.getElementById('fade').style.display='';
                        // document.getElementById("tableQuery").style.display='';
                        $('#dataDispDiv').dialog('close');
                        $("#tableQuery").dialog('close');

                    }
                    function saveTableList(){
                        document.forms.myForm.action='editconn.do?parameter=saveDetails'
                        document.forms.myForm.submit();
                    }
                    function cancelTableList1()
                    {
                        //document.myForm2.action="getAllTables.do";
                        // document.myForm2.submit();
                        $("#datatablist").dialog('close');
            <%--document.getElementById("dataDisptab").style.display='none';
            document.getElementById('fade').style.display='';--%>
                    }
                    function cancelTableList2()
                    {
                        document.myForm2.action="getAllTables.do";
                        document.myForm2.submit();
                        //document.getElementById("dataDisptab").style.display='none';
                        // document.getElementById('fade').style.display='';
                    }
                    function refreshparenttest()
                    {
                        //document.myForm2.action="getAllTables.do";
                        //document.myForm2.submit();
                    }
                    function cancelTableListcon(){
                        $("#datatablist").dialog('close');
                        // document.getElementById("dataDisptab").style.display='none';
                        document.myForm2.action="getAllTables.do";
                        document.myForm2.submit();

                        // document.getElementById('fade').style.display='';
                    }

                    function dbconremoveimage()
                    {
                        //alert("Hai");
                        //parent.document.getElementById('fade1').style.display='none';
                        parent.document.getElementById('loading').style.display='none';
                    }
        

                    function shwTable(){
                        alert("ib table")
                        alert("id---"+document.getElementById("tables"))
                        document.getElementById("tables").style.display='block';
                        document.getElementById("columns").style.display='none';
                    }
                    function shwCol(){
                        document.getElementById("tables").style.display='none';
                        document.getElementById("columns").style.display='block';
                    }
                    function refreshPage(){

                        window.location.reload(true);
                        parent.document.getElementById('loading').style.display='block';
                    }
                    function pageexits()
                    {
                        parent.document.getElementById('loading').style.display='block';
                    }
                    window.onunload = pageexits;
                    //window.onunload = parent.document.getElementById('loading').style.display='block';

                    function cancelFunction(){
                        parent.parent.document.getElementById('loading').style.display='none';
                        $("#FolderDetails").dialog('close')
                        $("#addColumnDivid").dialog('close')
                        refreshPage();
                    }
                    function saveinBussRole(){
                        var checkObj=document.getElementsByName("bussGrpCheck")
                        //                    alert("checkObj\t"+checkObj.length)
                        var rolenames=new Array;
                        for(var i=0;i<checkObj.length;i++){
                            if(checkObj[i].checked){
                                rolenames.push(checkObj[i].value )
                            }
                        }
                        //alert("rolenames\t"+rolenames)
                        $("#FolderDetails").dialog('close')
                        $.ajax({
                            url:'<%= request.getContextPath()%>/editconn.do?parameter=checkAndSaveInBussRole&rolenames='+rolenames.toString(),
                            success:function(data){
                                // alert("data"+data)
                                if(data=="false"){
                                    alert('Error in inserting.');
                                }else{
                                    var folderNames=new Array;
                                    var isPublished =new Array;

                                    var dataJson=eval("("+data+")")
                                    folderNames=dataJson.folderName;
                                    isPublished==dataJson.isPublish;
                                    var table="";
                                    var busstableName=dataJson.busstableName[0];

                                    table+="<p>"+busstableName+" is useing in below roles </p><br>"
                                    table+="<table width='100%' border='1'><thead><tr><th class='prgBtn'>Floder name </th><th class='prgBtn'> Publish Status</th>\n\
                                       <th class='prgBtn'> Check to Migrate</th></tr></thead><tbody>"
                                    for(var i=0;i<folderNames.length;i++){
                                        var textValue = folderNames[i];
                                        if (jQuery.inArray(textValue, rolenames)==false){
                                            table+="<tr><td>"+folderNames[i]+"</td><td>";
                                            // alert("=="+dataJson.isPublish[i]+"===")
                                            if(dataJson.isPublish[i]=="True"){
                                                table+="<img src='<%=request.getContextPath()%>/icons pinvoke/tick-small.png'><td><input type='checkbox' name='bussGrpCheck' id='bussGrpCheck' checked value='"+dataJson.folderName[i]+"' ></td></td></tr>";
                                            }else{
                                                table+="<img src='<%=request.getContextPath()%>/icons pinvoke/cross-small.png'><td><input type='checkbox' name='bussGrpCheck' id='bussGrpCheck' checked value='"+dataJson.folderName[i]+"' ></td></td></tr>";
                                            }
                                        }
                                    }
                                    table+="</tbody></table>";
                                    table+="<p>      If you want migrate these changes to published BussinessRole click on Next </p>"
                                    table+="<br><table align='right' ><tr><td> <input type='button' onclick='cancelFunction()' name=cancel value='Cancel'  class='prgBtn' style='width:100px;' ></td><td><input type='button' onclick=saveAndPublishRole() name='next' value='Next'  class='prgBtn' style='width:100px;' ></td><td></td></tr></table>"

                                    parent.document.getElementById('loading').style.display='none';
                                    if(dataJson.folderName.length>0){
                                        $("#FolderDetails").dialog('open')
                                        $("#FolderDetails").html("");
                                        $("#FolderDetails").html(table);

                                    }else{
                                        alert("No Role to migrate")
                                        cancelFunction()
                                    }
                                }

                            }
                        });

                    }

                    function saveinBussGrpSrc(){
                        var checkObj=document.getElementsByName("bussGrpCheck")
                        //                    alert("checkObj\t"+checkObj.length)
                        var grpNames=new Array;
                        for(var i=0;i<checkObj.length;i++){
                            if(checkObj[i].checked){
                                grpNames.push(checkObj[i].value )
                            }
                        }
                        if(grpNames.length>0){
                            $.ajax({
                                url:'<%= request.getContextPath()%>/editconn.do?parameter=checkAndSaveInBussSrc',
                                success:function(data){
                                    //  alert("data"+data)
                                    if(data=="false"){

                                        alert("No BussinessGroup to migrate ")

                                    }else{
                                        var dataJson=eval("("+data+")")
                                        var table="";
                                        var busstableName=dataJson.busstableName[0];

                                        table+="<p>"+busstableName+" is useing in below roles </p><br>"
                                        table+="<table width='100%' border='1'><thead><tr><th class='prgBtn'>Folder name </th><th class='prgBtn'> Check to Migrate </th></tr></thead><tbody>"
                                        for(var i=0;i<dataJson.folderName.length;i++){
                                            table+="<tr><td>"+dataJson.folderName[i]+"</td><td>";

                                            table+="<input type='checkbox' name='bussGrpCheck' id='bussGrpCheck' checked value='"+dataJson.folderName[i]+"' ></td></tr>";

                                        }
                                        table+="</tbody></table>";
                                        table+="<p>   Please check  Which Role you want migrate these changes and click on Next </p>"
                                        table+="<br><table align='right' ><tr><td> <input type='button' onclick=cancelFunction()  name='cancel' value='Cancel'  class='prgBtn' style='width:100px;' ></td><td><input type='button' onclick=saveinBussRole() name='next' value='Next'  class='prgBtn' style='width:100px;' ></td><td></td></tr></table>"
                                        parent.document.getElementById('loading').style.display='none';
                                        if(dataJson.folderName.length>0){
                                            $("#FolderDetails").dialog('open')
                                            $("#FolderDetails").html("");
                                            $("#FolderDetails").html(table);

                                        }else{
                                            alert("No Role to migrate")
                                            cancelFunction()
                                        }

                                    }
                                }

                            });
                        }else{

                            cancelFunction()
                        }

                    }
                    function saveinBussGrpSrc1(){
                        var checkObj=document.getElementsByName("bussGrpCheck")
                        //                    alert("checkObj\t"+checkObj.length)
                        var grpNames=new Array;
                        for(var i=0;i<checkObj.length;i++){
                            if(checkObj[i].checked){
                                grpNames.push(checkObj[i].value )
                            }
                        }
                        if(grpNames.length>0){
                            $.ajax({
                                url:'<%= request.getContextPath()%>/editconn.do?parameter=checkAndSaveInBussSrc',
                                success:function(data){
                                    //  alert("data"+data)
                                    if(data=="false"){

                                        alert("No BussinessGroup to migrate ")

                                    }
                                    else{
                                        alert("migration complete")
                                        refreshPage();
                                }

                                }

                            });
                        }else{

                            cancelFunction()
                        }


                    }
                    function saveAndPublishRole(){
                        var checkObj=document.getElementsByName("bussGrpCheck")
                        if(checkObj.length>0){
                            var roleNames=new Array;
                            for(var i=0;i<checkObj.length;i++){
                                if(checkObj[i].checked){
                                    roleNames.push(checkObj[i].value )
                                }
                            }
                            $.ajax({

                                url:'<%= request.getContextPath()%>/editconn.do?parameter=saveAndPublishRole&roleNames='+roleNames,
                                success:function(data){
                                    //                                alert("data\t"+data);
                                    if(data="true"){
                                        alert('Migrateing is complete.');
                                        cancelFunction()
                                    }else{
                                        alert('Error in migrateing.');
                                    }
                                }

                            });
                        }else{
                            cancelFunction()
                        }
                   
                    }

        </script>
</body>

</html>
