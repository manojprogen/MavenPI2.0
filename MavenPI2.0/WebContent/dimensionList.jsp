<%@page contentType="text/html" pageEncoding="UTF-8" import="java.util.*,java.sql.*,prg.db.PbReturnObject,prg.db.PbDb,utils.db.ProgenConnection"%>
<%@taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%//for clearing cache
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
            String connId = String.valueOf(session.getAttribute("connId"));
            String contxPath=request.getContextPath();
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Dimensions</title>
        <link rel="stylesheet" href="css/bootstrap.min.css" type="text/css"/>
        <link rel="stylesheet" href="css/bootstrap-theme.min.css" type="text/css"/>
	<link rel="stylesheet" href="css/font-awesome.min.css" type="text/css"/>
        <script src="javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=contxPath%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>

        <link rel="stylesheet" href="stylesheets/treeviewstyle/jquery.treeview.css" />
        <link rel="stylesheet" href="stylesheets/treeviewstyle/screen.css" />

        <script src="javascript/treeview/jquery.cookie.js" type="text/javascript"></script>
        <script src="javascript/treeview/jquery.treeview.js" type="text/javascript"></script>

<!--        <script type="text/javascript" src="javascript/treeview/demo.js"></script>-->

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
<!--        <script language="JavaScript" src="<%=request.getContextPath()%>/querydesigner/JS/jquery.columnfilters.js"></script>-->
        <!-- below five lines only added by bharathi reddy on 26-08-09 -->

        <link href="stylesheets/StyleSheet.css" rel="stylesheet" type="text/css" />
        <link href="stylesheets/confirm.css" rel="stylesheet" type="text/css" />
        <link href="stylesheets/jquery.contextMenu.css" rel="stylesheet" type="text/css" />
        <script src="javascript/jquery.simplemodal-1.1.1.js" type="text/javascript"></script>
        <script src="javascript/jquery.contextMenu.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%= contxPath%>//dragAndDropTable.js"></script>
        <link type="text/css" href="<%=contxPath%>/stylesheets/themes/<%=session.getAttribute("theme")%>/metadataButton.css" rel="stylesheet" />
        <link type="text/css" href="<%=contxPath%>/stylesheets/themes/<%=session.getAttribute("theme")%>/ReportCss.css" rel="stylesheet" />
        <script type="text/javascript" src="<%=contxPath%>/javascript/quicksearch.js"></script>


        <script type="text/javascript">
            $(document).ready(function(){
                $("#connId1").val('<%=connId%>')
                if ($.browser.msie == true){
                    $("#dimension").dialog({
                        autoOpen: false,
                        height: 800,
                        width: 800,
                        position: 'justify',
                        modal: true
                    });
                    $("#dimWizardDiv").dialog({
                        autoOpen: false,
                        height: 400,
                        width: 400,
                        position: 'justify',
                        modal: true
                    });

                    $("#renameDimensionDiv").dialog({
                        autoOpen: false,
                        height: 400,
                        width: 400,
                        position: 'justify',
                        modal: true
                    });
                    $("#dataDispmemDiv").dialog({
                        autoOpen: false,
                        height: 560,
                        width: 700,
                        position: 'justify',
                        modal: true
                    });
                    $("#tabmemberDiv").dialog({
                        autoOpen: false,
                        height: 560,
                        width: 700,
                        position: 'justify',
                        modal: true
                    });
                     $("#addmemberDiv").dialog({
                        autoOpen: false,
                        height: 200,
                        width: 200,
                        position: 'absolute',
                        modal: true
                    });
                    $("#dataDispreNameMemDiv").dialog({
                        autoOpen: false,
                        height: 500,
                        width: 700,
                        position: 'justify',
                        modal: true
                    });
                    $("#dataDispparentgrpDiv").dialog({
                        autoOpen: false,
                        height: 600,
                        width: 700,
                        position: 'justify',
                        modal: true
                    });
                    $("#hiedataDispDiv").dialog({
                        autoOpen: false,
                        height: 500,
                        width: 700,
                        position: 'justify',
                        modal: true
                    });
                    $("#editHierarachydiv").dialog({
                        autoOpen: false,
                        height: 200,
                        width: 200,
                        position: 'justify',
                        modal: true


                    });
                    $("#timeDimensionMembers").dialog({
                        autoOpen: false,
                        height: 500,
                        width: 665,
                        minHeight: 109,
                        position: 'justify',
                        modal: true
                    });
                    $("#createCalenderDiv").dialog({
                        bgiframe: true,
                        autoOpen: false,
                        resizable: false,
                        height:310,
                        width:570,
                        position:'top',
                        modal: true,
                        overlay: {
                            backgroundColor: '#000',
                            opacity: 0.5
                        }
                    });
                    $("#dimensionMembers").dialog({
                        autoOpen: false,
                        height: 550,
                        width: 665,
                        minHeight: 109,
                        position: 'justify',
                        modal: true
                    });
                }
                else{
                    $("#dimension").dialog({
                        autoOpen: false,
                        height: 250,
                        width: 400,
                        position: 'justify',
                        modal: true
                    });
                    $("#dimWizardDiv").dialog({
                        autoOpen: false,
                        height: 800,
                        width: 800,
                        position: 'justify',
                        modal: true
                    });
                    $("#renameDimensionDiv").dialog({
                        autoOpen: false,
                        height: 250,
                        width: 400,
                        position: 'justify',
                        modal: true
                    });
                    $("#dataDispmemDiv").dialog({
                        autoOpen: false,
                        height: 400,
                        width: 700,
                        position: 'justify',
                        modal: true
                    });
                    $("#tabmemberDiv").dialog({
                        autoOpen: false,
                        height: 560,
                        width: 700,
                        position: 'justify',
                        modal: true
                    });
                    $("#addmemberDiv").dialog({
                        autoOpen: false,
                        height: 200,
                        width: 200,
                        position: 'absolute',
                        modal: true
                    });
                    $("#dataDispreNameMemDiv").dialog({
                        autoOpen: false,
                        height: 400,
                        width: 700,
                        position: 'justify',
                        modal: true
                    });
                    $("#dataDispparentgrpDiv").dialog({
                        autoOpen: false,
                        height: 500,
                        width: 700,
                        position: 'justify',
                        modal: true
                    });
                    $("#hiedataDispDiv").dialog({
                        autoOpen: false,
                        height: 400,
                        width: 700,
                        position: 'justify',
                        modal: true
                    });
                    $("#editHierarachydiv").dialog({
                        autoOpen: false,
                        height: 200,
                        width: 200,
                        position: 'justify',
                        modal: true


                    });
                    $("#timeDimensionMembers").dialog({
                        autoOpen: false,
                        height: 510,
                        width: 670,
                        minHeight: 109,
                        position: 'justify',
                        modal: true
                    });
                    $("#createCalenderDiv").dialog({
                        bgiframe: true,
                        autoOpen: false,
                        resizable: false,
                        height:320,
                        width:560,
                        position:'top',
                        modal: true,
                        overlay: {
                            backgroundColor: '#000',
                            opacity: 0.5
                        }
                    });
                    $("#dimensionMembers").dialog({
                        autoOpen: false,
                        height: 500,
                        width: 665,
                        minHeight: 109,
                        position: 'justify',
                        modal: true
                    });

                }
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

            *{
                margin:         0px;
                padding:        0px;
                font:           11px verdana;
            }

            ul.project{
                margin-left:    0px;
            }

            h1{
                font:           24px verdana;
            }

            h2
            {
                margin-top:     30px;
                font:           bold 16px verdana;
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
                /*               background:     white;*/
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
            .white_parent{
                display: none;
                position: absolute;
                top: 15%;
                left: 25%;
                width: 730px;
                height:400px;
                padding: 16px;
                border: 10px solid silver;
                background-color: white;
                z-index:1002;
                overflow:auto;
                -moz-border-radius-bottomleft:6px;
                -moz-border-radius-bottomright:6px;
                -moz-border-radius-topleft:6px;
                -moz-border-radius-topright:6px;
            }
            .white_hiedata{
                display: none;
                position: absolute;
                top: 15%;
                left: 25%;
                width: 500px;
                height:350px;
                padding: 16px;
                border: 10px solid silver;
                background-color: white;
                z-index:1002;
                -moz-border-radius-bottomleft:6px;
                -moz-border-radius-bottomright:6px;
                -moz-border-radius-topleft:6px;
                -moz-border-radius-topright:6px;
            }

            .white_content1 {
                display: none;
                position: absolute;
                top: 15%;
                left: 25%;
                width: 50%;
                height:60%;
                padding: 16px;
                border: 10px solid silver;
                background-color: white;
                z-index:1002;
                -moz-border-radius-bottomleft:6px;
                -moz-border-radius-bottomright:6px;
                -moz-border-radius-topleft:6px;
                -moz-border-radius-topright:6px;
            }
            .smallWhiteDiv {
                display: none;
                position: absolute;
                top: 30%;
                left: 35%;
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
            .label{
                font-family:verdana;
                font-size:12px;
                font-weight:normal;
            }
            .navigation {
                width: 950px;
                height: 60px;
                margin: 0;
                padding: 0;
                text-align: center;
            }
            .navigation ul {
                list-style: none;
                margin: 0;
                padding: 0;
                text-align: center;
            }
            .navigation ul li {
                float: left;
            }
            .navigation ul li a {
                float: left;
                margin: 0 0 0 20px;
                padding: 7px;
                border: 3px solid black;
                border-top: none;
            }
        </style>
    </head>
    <body style="overflow:hidden" onload="dimsremoveimage()">
        <%
                    if (request.getAttribute("DimensionInsertStatus") != null) {
                        String status = (String) request.getAttribute("DimensionInsertStatus");

        %>
        <script type="text/javascript">
alert('<%=status%>');
window.location.href="<%=request.getContextPath()%>/getAllDimensions.do";
        </script>

        <%}%>
        <% 
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
        <form name="myForm2" method="post">
            <font size="1px" face="verdana" valign="top">
                <table style="width:100%;height:100%" border="solid black 1px" align="left">
                    <tr>
                        <td width="28%" valign="top">
                            <div style="height:36px" class="themeColor  draggedDivs ui-corner-all">
                                <font style="font-weight:bold" face="verdana" size="1px">&nbsp;Dimensions-
                                <a  href="javascript:void(0)" onclick="javascript:goConnection()" style="color:black;text-decoration:none;"><b><%=connName%></b></a></font>
                                <div style="padding: 5px;float: right;">
                                    <a class="fa fa-refresh themeColor" href="javascript:void(0)" onclick="refreshPage()" style="color:white;"></a>
                            </div>
                            </div>
                            <%if (request.getAttribute("dimensionslist") != null) {
                                            ////////////////////.println("String is \n" + request.getAttribute("dimensionslist"));
                                            out.println(request.getAttribute("dimensionslist"));
                                        }
                            %>
                            <%--This is the code for generating dimensions list--%>
                            <%-- <div style="height:553px;overflow-y:auto;">
                            <ul id="myList1" class="filetree">
                                <li class="closed" style="background-image:url('images/treeViewImages/plus.gif')"><img src="images/treeViewImages/Dim.gif"><span id="123" class="dimMenu "><font size="1.5px" face="verdana">&nbsp;Dimensions</font></span>
                                    <ul>
                                        <logic:notEmpty name="DimensionList">
                                            <logic:iterate id="DimensionList" name="DimensionList" >
                                                <li  class="closed" id="<bean:write name="DimensionList" property="dimensionId"/>"><img src="images/treeViewImages/Dim.gif" ><span class="individualDim" id="<bean:write name="DimensionList" property="dimensionId"/>"><font size="1px" face="verdana">&nbsp;&nbsp;<bean:write name="DimensionList" property="dimensionName"/></font></span>
                                                    <ul>

                                                        <li class="closed" ><span class="folder" ><font size="1px" face="verdana" ><span  class="tabMenu" id="<bean:write name="DimensionList" property="dimensionId"/>">&nbsp;Tables</span></font></span>
                                                            <ul>
                                                                <logic:notEmpty name="DimensionList" property="tableList">
                                                                    <logic:iterate id="list" name="DimensionList" property="tableList" >
                                                                        <logic:notEqual name="list" property="tableName" value="">
                                                                            <li  class="closed" id="<bean:write name="list" property="tableId"/>,<bean:write name="list" property="tableName"/>"><img src="images/treeViewImages/database_table.png"><span id="table"   class="memMenu" onclick="dimColDelete('<bean:write name="list" property="tableId"/>')">
                                                                                <font size="1px" face="verdana">&nbsp;<bean:write name="list" property="tableName"/></font></span>
                                                                                <ul >
                                                                                </logic:notEqual>
                                                                                <logic:notEqual name="list" property="columnName" value="">

                                                                                    <li id="<bean:write name="list" property="columnId"/>,<bean:write name="list" property="columnName"/>,<bean:write name="list" property="isPk"/>" class="closed">
                                                                                        <logic:equal name="list" property="isAvailable" value="Y">
                                                                                            <input type="checkbox" name="chk2" checked value="<bean:write name="list" property="columnId"/>">
                                                                                            <span><font size="1px" face="verdana">&nbsp;<bean:write name="list" property="columnName"/></font></span>
                                                                                        </logic:equal>
                                                                                        <logic:equal name="list" property="isAvailable" value="N">
                                                                                            <input type="checkbox" name="chk2" value="<bean:write name="list" property="columnId"/>">
                                                                                            <span ><font size="1px" face="verdana">&nbsp;<bean:write name="list" property="columnName"/></font></span>
                                                                                        </logic:equal>
                                                                                        <logic:equal name="list" property="isPk" value="Y">
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
                                                        <li class="closed"><span class="folder"><font size="1px" face="verdana">&nbsp;Members</font></span>
                                                            <ul>
                                                                <logic:notEmpty name="DimensionList" property="membersList">
                                                                    <logic:iterate id="membersList" name="DimensionList" property="membersList" >
                                                                        <logic:notEqual name="membersList" property="memberName" value="">

                                                                                <li id="<bean:write name="membersList" property="memberName"/>~<bean:write name="membersList" property="memberId"/>"><img src="images/treeViewImages/database_table.png"><span id="member" class="memRenameMenu">
                                                                                <font size="1px" face="verdana">&nbsp;<bean:write name="membersList" property="memberName"/></font></span>
                                                                            </li>
                                                                        </logic:notEqual>
                                                                    </logic:iterate>
                                                                </logic:notEmpty>
                                                            </ul>
                                                        </li>


                                                        <li class="closed"><span class="folder" ><font size="1px" face="verdana"><span class="hieMenu" id="<bean:write name="DimensionList" property="dimensionId"/>">&nbsp;Hierarchies</span></font></span>
                                                            <ul>
                                                                 <% int i=0;%>
                                                                <logic:notEmpty name="DimensionList" property="hierarchyList">
                                                                    <logic:iterate id="hierarchyList" name="DimensionList" property="hierarchyList" >
                                                                        <logic:notEqual name="hierarchyList" property="relationName" value="">
                                                                            <li  class="closed"><img src="images/dim.png">
                                                                              <logic:equal name="hierarchyList" property="relType" value="Y">
                                                                              <span id="heirarchy"> <font size="1px" face="verdana">
                                                                                    &nbsp;<bean:write name="hierarchyList" property="relationName"/> &nbsp;
                                                                                     <font size="1px" face="verdana" color="green"><b>D</b>
                                                                              </font></font></span>
                                                                                  </logic:equal>
                                                                              <logic:notEqual name="hierarchyList" property="relType" value="Y">
                                                                              <span id="heirarchy"> <font size="1px" face="verdana">
                                                                                    &nbsp;<bean:write name="hierarchyList" property="relationName"/> &nbsp;
                                                                                      </font></span>
                                                                                  </logic:notEqual>

                                                                                 <ul ><li >
                                                                                    <%i=0;%>
                                                                                </logic:notEqual>
                                                                                <logic:notEqual name="hierarchyList" property="relColumnName" value="">
                                                                                    <span class="parentGrpMenu" id="<bean:write name="hierarchyList" property="relColumnId"/>"><font size="1px" face="verdana ">
                                                                                   &nbsp; <bean:write name="hierarchyList" property="relColumnName"/></font></span>
                                                                                 <logic:equal name="hierarchyList" property="endColumn" value="true">
                                                                                         <%i=i+1;%>
                                                                                     <ul ><li>
                                                                                          </logic:equal>
                                                                                </logic:notEqual>


                                                                                <logic:equal name="hierarchyList" property="endTable" value="true">
                                                                                                  <%for(int j=0;j<i;j++){%>

                                                                                                       </li>
                                                                                                        </ul>
                                                                                              <%}%>
                                                                                                </li>      
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
                                    </ul>
                                </li>
                            </ul>
</div>--%>
                            <%--Upto here is the code for displaying the dimensions list--%>
                        </td>
                        <td width="72%" valign="top">
                        </td>
                    </tr>
                </table>
            </font>

            <input type="hidden" name="dimtableId" id="dimtableId" >

        </form>
        <div style="display:none" title="Add Tables" id="dataDispmemDiv">
            <iframe  id="dataDispmem" NAME='dataDispmem' frameborder="0" width="100%" height="100%"  src='about:blank'></iframe>
        </div>
         <div style="display:none" title="Add Columns" id="tabmemberDiv">
            <iframe  id="tabDispmem" NAME='tabDispmem' frameborder="0" width="100%" height="100%"  src='about:blank'></iframe>
        </div>
        <div style="display:none" title="Group Detail" id="addmemberDiv">
            <p> <b>     Check the groups in which you want migrate these changes</b> </p>
            <table id="addmem">

            </table>
            <table>
                            <tr>
                                <td align="center"><input type="button" class="navtitle-hover" style="width:auto" value="Migrate" onclick="migrateCol()"></td>
                            </tr>
           </table>
        </div>
                        <div id="timeDimensionMembers" name ="timeDimensionMembers" title="Add Members">
                            <table width="100%" id="timeDimensionTable">
                                <tr>
                                    <td></td>
                                </tr>
                            </table>

                            <table style="width: 100%;">
                                <tbody>
                                    <tr>
                                        <td style="height: 15px;">

                                        </td>
                                    </tr>
                                    <tr>
                                        <td align="center" colspan="2">
                                            <input type="button" onclick="saveTimeDimenMembers()" value="Save" style="width: auto;" class="navtitle-hover">
                                        </td>
                                    </tr>
                                </tbody>
                            </table>
                            </div>
        <div style="display:none" id="hiedataDispDiv" title="Create Hierarchy">
            <iframe  id="hiedataDisp" NAME='hiedataDisp' frameborder="0" width="100%" height="100%"  src='about:blank'></iframe>
        </div>
        <div style="display:none" id="dataDispparentgrpDiv" title="Add Group">
            <iframe   id="dataDispparentgrp" frameborder="0" NAME='dataDispparentgrp' width="100%" height="100%"  src='about:blank'></iframe>
        </div>
        <%--for Rename Member--%>
        <div style="display:none" id="dataDispreNameMemDiv" title="Rename">
            <iframe  id="dataDispreNameMem" NAME='dataDispreNameMem' style="width:100%" height="100%" frameborder="0" src='about:blank'></iframe>
        </div>
        <span id="hierar">

            <div id="hierarchydiv" style="width:700px" class="white_content1" >
                <form name="myFormHierarchy" method="post">
                    <center>
                        <table id="hierarchytable"style="width:37%">
                            <%--  <tr>
                            <td class="myHead" style="width:58%">
                                Members
                            </td>
                            <td  style="width:58%">
                                Levels
                            </td>
                            </tr> --%>
                        </table>

                        <table>
                            <tr>
                                <td><input type="button" class="navtitle-hover" style="width:auto" value="Save" onclick=""></td>
                            </tr>
                        </table>


                    </center>
                </form>
            </div>
        </span>

        <div id="helptextmenu" style="width:450px;display:none;height:50%" class="white_Help" align="center">
            <iframe id="helptextFrame" frameborder="0" width="100%" height="100%" name="helptextFrame" src='about:blank'></iframe>
        </div>

        <div id="dimension" title="Create Dimension" style="display:none">
            <center><br><br>
                <form name="myForm" method="post" action="#">
                    <table style="width:70%">
                        <tr>
                            <td class="myHead" style="width:70%">
                                <label class="label">Dimension Name</label>
                            </td>
                            <td style="width:58%">
                                <input type="text" name="dimensionName" id="dimensionName" onkeyup="tabmsg1()">
                            </td>
                        </tr>
                        <tr>
                            <td class="myHead" style="width:70%">
                                <label class="label">Description</label>
                            </td>
                            <td style="width:58%">
                                <input type="text" id="desc" name="dimensionDescription">
                            </td>
                        </tr>
                    </table>
                    <br>
                    <table>
                        <tr>
                            <td><input type="button" class="navtitle-hover" style="width:auto" value="Save" onclick="saveDimension()"></td>
                            <td><input type="button" class="navtitle-hover" style="width:auto" value="Cancel" onclick="cancelFade()"></td>
                        </tr>
                    </table>
                    <input type="hidden" name="connId" id="connId" value="<%=connId%>">
                </form>
            </center>
        </div>


        <%String Query = " SELECT CONNECTION_ID, CONNECTION_NAME FROM PRG_USER_CONNECTIONS";
                    PbReturnObject pbro1 = new PbDb().execSelectSQL(Query);
        %>
        <div>
            <div>
                <iframe  id="dimtab" NAME='dimtab' style="height:600px;width:100%;display:none;" src='about:blank' frameborder="0" marginheight="0" marginwidth="0"></iframe>
            </div>
        </div>
        <div id="selectConnection" title="Select Connection">
            <form name="myFormcon">
                <table cellpadding="5">
                    <tr><td>
                            <label for="name">Connection Name</label></td>
                        <td> <select id="connId1" name="connId1" style="width:146px">
                                <%for (int i1 = 0; i1 < pbro1.getRowCount(); i1++) {%>
                                <option value="<%=pbro1.getFieldValueInt(i1, 0)%>"><%=pbro1.getFieldValueString(i1, 1)%></option>
                                <%}%>
                            </select></td></tr>

                </table>
                <%--<input type="button"  value="Connect" onclick="saveTables()">--%>
            </form>

        </div>
        <div id="fade" class="black_overlay"></div>

        <%--added by susheela start --%>
        <div id="renameDimensionDiv" title="Rename Dimension" STYLE='display:none;'>
            <iframe  id="renameDimensionFrame" NAME='renameDimenionFrame' frameborder="0" height="100%" width="100%" src='about:blank'></iframe>
        </div>
        <%-- <div id="renameDimenion" title="View Custom Drill" class="white_content1">

          </div> --%>
        <div id="fade" class="black_overlay"></div>
        <div id="deleteDimensionDialog" title="Delete Dimensions">
            <p><span class="ui-icon ui-icon-alert" style="float:left; margin:0 7px 20px 0;"></span>The dimension will be deleted. Are you sure?</p>
        </div>
        <%--added by susheela over --%>


        <ul id="dimListMenu" class="contextMenu" style="width:200px;text-align:left">

            <li class="insert"><a href="#createDim">Create Dimension</a></li>
            <li class="insert"><a href="#deleteDim">Delete Dimension</a></li>
            <li class="insert"><a href="#UserTimeDim">Enable PRG DAY INFO</a></li>


        </ul>

        <ul id="dimensionTableMenuList" class="contextMenu" style="text-align:left;height:20px;width:auto ">
            <li class="insert"><a href="#restrictAccess">Restrict Access</a></li>

            <%-- 07-10-09 over --%>
        </ul>

        <%--added by susheela start --%>
        <ul id="indDimListMenu" class="contextMenu" style="text-align:left;height:100px;width:auto ">
            <li class="insert"><a href="#deleteIndDim">Delete Dimension</a></li>
            <li class="insert"><a href="#renameIndDim">Rename Dimension</a></li>
            <%-- 07-10-09 start --%>
            <li class="insert"><a href="#deleteDimTable">Remove Table For Dimension</a></li>

            <%-- 07-10-09 over --%>
        </ul>
<!--         <ul id="indUserDimListMenu" class="contextMenu" style="text-align:left;height:100px;width:auto ">
            <li class="insert"><a href="#deleteIndDim">Delete Dimension</a></li>
            <li class="insert"><a href="#renameIndDim">Rename Dimension</a></li>
            <%-- 07-10-09 start --%>
            <li class="insert"><a href="#deleteDimTable">Remove Table For Dimension</a></li>

            <%-- 07-10-09 over --%>
        </ul>-->
        <ul id="indDimListMenuForTime" style="height: 0px;width: 0px" >
<!--            <li class="insert"><a href="#deleteIndDim">Delete Dimension</a></li>
            <li class="insert"><a href="#renameIndDim">Rename Dimension</a></li>
            <%-- 07-10-09 start --%>
            <li class="insert"><a href="#deleteDimTable">Remove Table For Dimension</a></li>
            <li class="insert"><a href="#StandardTimeDim">Enable Standard Dimension</a></li>
            <li class="insert"><a href="#UserTimeDim">Enable PRG_DAY_INFO</a></li>-->

            <%-- 07-10-09 over --%>
        </ul>
        <ul id="desMenu" class="contextMenu" style="width:auto;text-align:left">
            <li class="insert"><a href="#addDesc">Add Description/Help Text</a></li>
        </ul>

        <ul id="memberListMenu" class="contextMenu" style="width:200px;text-align:left">

                <li class="insert"><a href="#addMember">Add Member Details</a></li>
                <li class="insert"><a href="#addMemberWizard">Add Member Wizard</a></li>
                <li class="insert"><a href="#addColumnWizard">Add Additional Columns</a></li>
                <li class="insert"><a href="#migrateColumnWizard">Migrate Additional Columns</a></li>
            <%--   <li class="insert"><a href="#combinedKey">Define Combined key</a></li> --%>

        </ul>
            <ul id="standTimeMemListMenu" class="contextMenu" style="width:200px;text-align:left">
                <li class="insert"><a href="#configure">Configure</a></li>
                <li class="insert"><a href="#addMember">Add Member Details</a></li>
                <li class="insert"><a href="#addMemberWizard">Add Member Wizard</a></li>
            </ul>
        <ul id="hierarchyListMenu" class="contextMenu" style="width:150px;text-align:left">

            <li class="insert"><a href="#addHierarchy">Create Hierarchy</a></li>

        </ul>

        <ul id="dimTableListMenu" class="contextMenu" style="width:150px;text-align:left">

            <li class="insert"><a href="#createDimTable">Add Tables</a></li>
            <li class="insert"><a href="#deleteDimTable">Delete Tables</a></li>

        </ul>

        <ul id="parentGrpListMenu" class="contextMenu" style="width:150px;text-align:left">

            <li class="insert"><a href="#addParentGrp">Add Group</a></li>

        </ul>
        <%--for Renaming Member --%>
        <ul id="memRenameListMenu" class="contextMenu" style="width:150px;text-align:left">

            <li class="insert"><a href="#renameMem">Rename</a></li>

        </ul>
        <ul id="editHierarchylist" class="contextMenu" style="width:150px;text-align:left">
            <li class="insert"><a href="#editHierarachy" id="editlink">Edit Hierarchy</a></li>
        </ul>
        <div id="fade1" class="black_overlay" >
            <img id="imgId" src="images/ajax.gif"  width="100px" height="100px"  style="position:absolute;left:600px;top:200px" >
        </div>
        <div id="editHierarachydiv" style="display: none" title="Edit Hierarachy" class="navigation" >

            <ul id='HierarachyUL' class='sortable'>

            </ul><br><br>  <br><br>  <br><br> <br><br>
            <div id="divButt">

            </div>
            <div  id="createCalenderDiv"  style="display:none" title="Configure Calender">
                <iframe name="calenderframe" id="calenderframe" src='about:blank' frameborder="0" marginheight="0" marginwidth="0" width="100%" height="100%"></iframe>
            </div>
        </div>
                                <div id="dimensionMembers" name ="timeDimensionMembers" title="Add Members">
                            <table width="100%" id="dimensionMembersTable">
                                <tr>
                                    <td></td>
                                </tr>
                            </table>

                            <table style="width: 100%;">
                                <tbody>
                                    <tr>
                                        <td style="height: 15px;">

                                        </td>
                                    </tr>
                                    <tr>
                                        <td align="center" colspan="2">
                                            <input type="button" onclick="saveDimensionMembers()" value="Save" style="width: auto;" class="navtitle-hover">
                                        </td>
                                    </tr>
                                </tbody>
                            </table>
                            </div>
        <input type="hidden" name="h" id="h" value="<%=request.getContextPath()%>">
         <script type="text/javascript">
            var temparray=new Array();
            var columnId;
            function checktab(id)
               {
                  var temp = "temp"+id;
                   if(document.getElementById(temp).checked==true)
                             {
                                temparray.push(id);

                             }
               }
            function migrateCol()
            {
               // alert(temparray)
                 $("#addmemberDiv").dialog('close');
                 $.ajax({

            url:'dimensioncheck.do?dimensionParam=migrateDimensionGrp&colId='+columnId+'&grpnames='+temparray,
            success: function(data){

               alert("migrate successfully");
            }
        });

            }
            function unloadPage()
            {
                //alert("unload event detected!");
                parent.document.getElementById("loading").style.display = 'block';
            }
            window.onunload = unloadPage;
            //added by susheela start
            var delDimId="";
            var delConnId="";

            function deleteDimension(){
                // alert('delConnId '+delConnId+" delDimId "+delDimId);
                $.ajax({
                    url: 'dimensioncheck.do?dimensionParam=checkDimensionForDelete&dimensionId='+delDimId+'&connectionId='+delConnId,
                    success: function(data) {
                        if(data==1)
                        {
                            alert("Dimension Deleted");
                            refreshDim();
                        }
                        else if(data==2)
                        {
                            alert("Dimesion is being used in business group.");
                        }

                    }
                });
            }
            //added by susheela over

            $(document).ready(function() {
                //added by susheela start
                $("#deleteDimensionDialog").dialog({
                    bgiframe: true,
                    autoOpen: false,
                    resizable: false,
                    height:200,
                    position:'justify',
                    modal: true,
                    overlay: {
                        backgroundColor: '#000',
                        opacity: 0.5
                    },

                    buttons: {
                        Cancel: function() {
                            $(this).dialog('close');
                        },
                        'Delete Selected Dimension': function() {
                            $(this).dialog('close');
                            deleteDimension();

                        }

                    }
                });
                //added by susheela over

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

                $(".dimMenu").contextMenu({ menu: 'dimListMenu', leftButton: true }, function(action, el, pos) {

                    contextMenuWork(action, el.parent("tr"), pos); });

                $(".desHlepMenu").contextMenu({ menu: 'desMenu', leftButton: true }, function(action, el, pos) {
                    qryDimIdToUpdate=el.attr('id');
                    contextMenuHelp(action, el.parent("tr"), pos);

                });
            
            <%--added by susheela start --%>
                    $(".individualDim").contextMenu({ menu: 'indDimListMenu', leftButton: true }, function(action, el, pos) {

                        contextMenuWork(action, el.parent("li"), pos); });
//                    $(".individualUserDefDim").contextMenu({ menu: 'indUserDimListMenu', leftButton: true }, function(action, el, pos) {
//
//                        contextMenuWork(action, el.parent("li"), pos); });

                    $(".individualTimeDim").contextMenu({ menu: 'indDimListMenuForTime', leftButton: true }, function(action, el, pos) {

                        contextMenuWork(action, el.parent("li"), pos); });


                     $(".dimensionTableMenu").contextMenu({ menu: 'dimensionTableMenuList', leftButton: true }, function(action, el, pos) {

                        contextMenuWork(action, el.parent("li"), pos); });
            <%--added by susheela over --%>

                    $(".memMenu").contextMenu({ menu: 'memberListMenu', leftButton: true }, function(action, el, pos) {

                        contextMenuWorkMember(action, el.parent("li"), pos); });
                    //  $(".hierarchyMenu").contextMenu({ menu: 'hierarchyListMenu', leftButton: true }, function(action, el, pos) {

                    //      contextMenuWorkhierarchy(action, el.parent("li"), pos); });
                    $(".standTimeDimMemMenu").contextMenu({ menu: 'standTimeMemListMenu', leftButton: true }, function(action, el, pos){
                        contextMenuWorkMember(action, el.parent("li"), pos); });


                    $(".hieMenu").contextMenu({ menu: 'hierarchyListMenu', leftButton: true }, function(action, el, pos) {

                        contextMenuWorkhierarchy(action, el, pos); });

                    $(".tabMenu").contextMenu({ menu: 'dimTableListMenu', leftButton: true }, function(action, el, pos) {
                        //alert($(el).parent().parent().attr('id'))


                        contextMenuWork1(action, el, pos); });

                    $(".parentGrpMenu").contextMenu({ menu: 'parentGrpListMenu', leftButton: true }, function(action, el, pos) {
                        //alert($(el).parent().parent().attr('id'))


                        contextMenuWorkParentGrp(action, el, pos); });

                    $(".memRenameMenu").contextMenu({ menu: 'memRenameListMenu', leftButton: true }, function(action, el, pos) {
                        contextMenuWorkRenameMem(action, el, pos); });

                    $(".editHierarchyMenu").contextMenu({ menu: 'editHierarchylist', leftButton: true }, function(action, el, pos) {
                        contextMenuWorkEditHierMenu(action, el, pos); });


                    $("#myList1").treeview({
                        animated:"slow",
                        persist: "cookie"
                    });

                    $("#selectConnection").dialog({
                        //bgiframe: true,
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
                                // alert('---'+IsConnection);
                                // if(IsConnection!="YES"){
                                //  alert('hi')
                                saveTables();
                                //  }

                                $(this).dialog('close');
                            }
                        },
                        close: function() {

                        }
                    });

                });

                var dimension_Id = '';
                var dim_tab_id = '';
                var col_id = '';
                var fromUserDefTimeDim = "false";
                var fromAddMember = "false";
                var dimensionName = '';




                function contextMenuWork(action, el, pos) {

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
                        //added bu susheela start
                    case "deleteIndDim":
                        {
                            delDimId = $(el).attr('id');
                            //alert('in delete dim '+dimId);

                            delConnId=document.getElementById("connId1").value;
                            var count=9;
                            if(count>0){
                                $('#deleteDimensionDialog').dialog('open');
                                count=0;
                            }
                            break;
                        }
                    case "renameIndDim":
                        {
                            var dimId = $(el).attr('id');
                            var connId=document.getElementById("connId1").value;
                            // alert('in rename dim '+dimId);
                            $("#renameDimensionDiv").dialog('open');
                            var frameObj2=document.getElementById("renameDimensionFrame");
                            // frameObj2.style.display='block';
                            //  document.getElementById('fade').style.display='block';
                            //alert('in view subFolId '+subFolId);
                            var source2 = "pbRenameQryDimension.jsp?dimensionId="+dimId+'&connectionId='+connId;
                            frameObj2.src=source2;
                            break;
                        }
                    //07-12-09
                    case "deleteDimTable":
                        {
                        var localDimId = $(el).attr('id');
                        var connId=document.getElementById("connId1").value;
                       //alert('localDimId '+localDimId+' connId '+connId);
                        checkQueryDimTableDelete(localDimId,connId);
                        break;
                       }
                    case "UserTimeDim":
                        {
                            fromUserDefTimeDim = "true";
                            var connId = document.getElementById("connId1").value;
                            //var msg = confirm("This will add New Time Dimension");
                           createTimeMember();
                       }
                    case "restrictAccess":
                       {
                          var connId=document.getElementById("connId1").value;
                          var idval = $(el).attr('id');
                          var details = idval.split(",");
                          col_id = details[0];
                          dim_tab_id = details[1];
                          var liObj=document.getElementById(idval);
                          var spanObj=liObj.getElementsByTagName('span');
                          var fontObj=spanObj[0].getElementsByTagName('font');
                          var dimensionName = (fontObj[0].innerHTML).split(";");
                          
                          spanObj=liObj.parentNode.parentNode.getElementsByTagName('span');
                          fontObj = spanObj[0].getElementsByTagName('font');
                          var tableName = (fontObj[0].innerHTML).split(";");
                          $.ajax({
                            url: 'dimensioncheck.do?dimensionParam=getDimensionMembers&dimName='+dimensionName[1]+'&tableName='+tableName[1]+'&connId='+connId+'&dim_tab_id='+dim_tab_id+'&col_id='+col_id,
                            success: function(data){
                          var jsonVar=eval('('+data+')')
                          $("#dimensionMembers").dialog('open');
                          var tableObj  = $('#dimensionMembersTable');
                          tableObj.html("");
                          tableObj.html(jsonVar.htmlStr);
                          grpColArray=jsonVar.memberValues;
                            $("#myList3").treeview({
                                 animated:"slow",
                                 persist: "cookie"
                            });

                            $('ul#myList3 li').quicksearch({
                                position: 'before',
                                attached: 'ul#myList3',
                                loaderText: '',
                                delay: 100
                            });
                            $(".myDragTabs").draggable({
                                helper:"clone",
                                effect:["", "fade"]
                            });
                            $("#dropTabs").droppable({
                                activeClass:"blueBorder",
                                accept:'.myDragTabs',
                                drop: function(ev, ui) {
                                    createColumn(ui.draggable.attr('id'),ui.draggable.html(),"sortable");
                                }
                            });
                            }

                            });
                       }
                //added bu susheela over
        }
    }
    //07-12-09 start
    function checkQueryDimTableDelete(localDimId,connId)
    {
        $.ajax({
            url: 'dimensioncheck.do?dimensionParam=checkQueryDimTableDelete&dimId='+localDimId+'&connectionId='+connId,
            success: function(data){
                // alert('date '+data);
                if(data==2){
                    alert('The dimension table cant be deleted as it is being used in a business group.');
                }
                else if(data==1){
                    alert('The dimension table is deleted successfully.');
                    window.location.reload(true);
                }
            }
        });
    }
    //07-12-09 over
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

                    var dimId=$(el).attr('id');
                    // alert(dimId);
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
    //added by susheela start
    function  refreshDim()
    {
        window.location.reload(true);
    }
    //added bu susheela over


    function contextMenuWork1(action, el, pos) {

        switch (action) {
            case "deleteDim":
                {
                    var msg = "Delete " + $(el).find("#contactname").text() + "?";
                    $("#HiddenFieldRowId").val($(el).find("#customerid").text());
                    confirm(msg);
                    break;
                }
            case "createDimTable":
                {
                    //  alert("dimid"+$(el).attr('id'));
                    // alert($(el).parent().attr('id')+'--'+$(el).parent().parent().attr('id'))
                    dimId=$(el).attr('id');
                    //alert(dimId)
                    getData(dimId);
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
                    var colid=$(el).attr('id');
                    var dimensionDetails = colid.split(",");                    
                    dimensionName = dimensionDetails[3];
                    var Id =  new Array();
                    Id = colid.split(",");
                    dimension_Id = Id[2];
//                    if(dimensionName=='PR_DAY_DENOM'){
//                        fromUserDefTimeDim = "false";
//                        fromAddMember = "true";
//                        createTimeMember();
//                    }else if(dimensionName=='PR_DAY_INFO'){
//                       fromUserDefTimeDim = "true";
//                       fromAddMember = "true"
//                       createTimeMember();
//                    }else{

                       createMember(colid);
//                    }
                    break;
                }
                case "addMemberWizard":
                    {
                        var colid=$(el).attr('id');
                        createMember1(colid);
                      <%-- var f=document.getElementById('tabDispmem');
                var s="dimensionTabMember.jsp?colId="+colid;
                f.src=s;
                $("#tabmemberDiv").dialog('open');--%>

                    break;
                    }
                    case "addColumnWizard":
                    {
                        var colid=$(el).attr('id');
                        $.ajax({
            url:'dimensioncheck.do?dimensionParam=saveDimensionMembers&colId='+colid,
            success: function(data){
                var f=document.getElementById('tabDispmem');
                var s="dimensionTabMember.jsp?colId="+colid+"&flag=true";
                f.src=s;
            }
        });
                        
                $("#tabmemberDiv").dialog('open');
                    break;
                    }
           case "migrateColumnWizard":
                    {
                        columnId=$(el).attr('id');
                        var colid=$(el).attr('id');
                        $.ajax({

            url:'dimensioncheck.do?dimensionParam=getDimensionGrp&colId='+colid,
            success: function(data){
               $("#addmem").html(data);
            }
        });
                            $("#addmemberDiv").dialog('open');
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

                case "configure":
                    {
                        var connId = document.getElementById("connId1").value;
                         $("#createCalenderDiv").dialog('open');
                        //    document.getElementById('createCalenderDiv').style.display='';
                        var frameObj = document.getElementById('calenderframe');
                        var source = '<%=request.getContextPath()%>/createUserCalender.jsp?connId='+connId;
                        frameObj.src = source;
                    }
        }
    }
    var isMemberUseInOtherLevel="false";
                        function testing(){
                        $("#createCalenderDiv").dialog('close');
                        window.location.href = window.location.href;
                    }

    function createTimeMember(){
        var connId =  document.getElementById("connId1").value;
        $.ajax({
            url: 'dimensioncheck.do?dimensionParam=getTimeDimensionMembers&fromUserDefinedTimeDim='+fromUserDefTimeDim+"&connId="+connId+'&fromAddMember='+fromAddMember,
            success:function(data){
                if(data!="Dimension Enabled"){
                var jsonVar=eval('('+data+')')
                $("#timeDimensionMembers").dialog('open');
                var tableObj  = $('#timeDimensionTable');
                tableObj.html("");
                tableObj.html(jsonVar.htmlStr);

                isMemberUseInOtherLevel=jsonVar.isMemberUseInOtherLevel
                $("#myList3").treeview({
                    animated:"slow",
                    persist: "cookie"
                });

                $('ul#myList3 li').quicksearch({
                    position: 'before',
                    attached: 'ul#myList3',
                    loaderText: '',
                    delay: 100
                });
                $(".myDragTabs").draggable({
                    helper:"clone",
                    effect:["", "fade"]
                });
                $("#dropTabs").droppable({
                    activeClass:"blueBorder",
                    accept:'.myDragTabs',
                    drop: function(ev, ui) {
                        createColumn(ui.draggable.attr('id'),ui.draggable.html(),"sortable");
                    }
                });
            }
            else{
            alert("User Time Dimension Already Enabled");
            }
            }
        });
       

    }
    function saveTimeDimenMembers(){
        var elementIds = new Array();
        var i = 0;
        var connId  = document.getElementById("connId1").value;

        $('#sortable li').each(function(){
            var element = $(this).attr('id');
            var elemts = new Array;
            elemts = element.split("_");
            elementIds[i] = elemts[0];
            i++;
        })
        $.ajax({
            url:'dimensioncheck.do?dimensionParam=saveTimeDimensionMembers&memberIds='+elementIds+'&dimension_Id='+dimension_Id+'&fromUserDefTimeDim='+fromUserDefTimeDim+'&connId='+connId,
            success: function(data){
                $("#timeDimensionMembers").dialog('close');
                     refreshPage();
            }
        });

//        refreshPage();
    }
    function addDescClose(){
        document.getElementById("helptextmenu").style.display = 'none';
        document.getElementById('fade').style.display='none';
    }
    function contextMenuHelp(action, el, pos) {

        switch (action) {
            case "addDesc":
                {

                    //alert('qryDimIdToUpdate - '+qryDimIdToUpdate);
                    var DivObj=document.getElementById("helptextmenu");
                    var FrameObj=document.getElementById("helptextFrame");
                    FrameObj.src="AddDescription.jsp?qryDimId="+qryDimIdToUpdate;
                    document.getElementById('fade').style.display='block';
                    DivObj.style.display="block";

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

    //ReNaming Member
    function contextMenuWorkRenameMem(action, el, pos) {

        switch (action) {
            case "renameMem":
                {
                    var mem=$(el).parent().attr('id').split("~");
                    var memName=mem[0];
                    var memId=mem[1];
                    renameMemberCheck(memName,memId);
                    break;

                }
        }
    }

    function contextMenuWorkEditHierMenu(action, el, pos){
        switch (action) {
            case "editHierarachy":
                {
                    //alert("dim id \t"+$(el).attr('id'));
                    var idval=$(el).attr('id').split("_")
                    var dimId=idval[1]

                    $.ajax({
                        url: 'dimensioncheck.do?dimensionParam=editHierarachy&dimid='+dimId+'&relID='+idval[2],
                        success:function(data){
                            var dataJson=data.split("splitStr")
                            $("#editHierarachydiv").dialog('open')
                            $("#HierarachyUL").html(dataJson[0])
                            $("#divButt").html(dataJson[1])
                        }
                    });
                  
                    $(".sortable").sortable();

                    break;

                }
        }

    }
    function deleteHierarach(memId,relId,dimID){
        $.ajax({
            url: 'dimensioncheck.do?dimensionParam=deleteHierarachyLevel&memId='+memId+'&relId='+relId+'&dimID='+dimID,
            success:function(data){
                var dataJson=data.split("splitStr")
                $("#HierarachyUL").html("")
                $("#divButt").html("")
                $("#HierarachyUL").html(dataJson[0])
                $("#divButt").html(dataJson[1])
            }
        });

    }

    function saveHirrarchy(dimID,relId){
        var liIds = $('#HierarachyUL li').map(function(i,n) {
            return $(n).attr('id');
        }).get().join(',');
        var memIDs=liIds.split(",")
      //  alert("lides\t"+memIDs.length)
        if(memIDs.length!=0){
      
            $.ajax({
                url: 'dimensioncheck.do?dimensionParam=saveHierarachyLevel&memId='+liIds.toString()+'&relId='+relId+'&dimID='+dimID,
                success:function(data){
                    $("#editHierarachydiv").dialog('close')
                    refreshPage()
                }
            });
        }


    }
 
    function renameMemberCheck(memName,memId){
        document.getElementById('fade1').style.display='block';
        $.ajax({
            url: 'dimensioncheck.do?dimensionParam=checkReNamememExist&memId='+memId+'&memName='+memName,
            success: function(data){
                document.getElementById('fade1').style.display='';
                if(data!=""){
                    alert('Sorry,This member is in use');
                }else{
                    var f=document.getElementById('dataDispreNameMem');
                    var connectionId=document.getElementById("connId").value;
                    var s="createReNameMember.jsp?memId="+memId+"&memName="+memName+"&connectionId="+connectionId;
                    f.src=s;
                    $("#dataDispreNameMemDiv").dialog('open');

            <%-- document.getElementById('dataDispreNameMem').style.display='block';
             document.getElementById('fade').style.display='block';--%>

                 }

             }
         });
     }

     function cancelRenameMember(){
         $("#dataDispreNameMemDiv").dialog('close');
            <%-- document.getElementById('dataDispreNameMem').style.display='none';
             document.getElementById('fade').style.display='';--%>

     }
     function saveReNameMember(){
         $("#dataDispreNameMemDiv").dialog('close');
            <%-- document.getElementById('dataDispreNameMem').style.display='none';
             document.getElementById('fade').style.display='';--%>
         alert('This member is Renamed successfully');
         window.location.reload(true);
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
     function createDimension(){
         $("#dimension").dialog('open');
            <%--document.getElementById('dimension').style.display='block';
            document.getElementById('fade').style.display='block';--%>
    }
    function createHierarchy(dimId){
        // window.open("createHierarchy.jsp?dimId="+dimId, "window.createHierarchy", "status=1,width=350,height=375");
        //showhierarchy(dimId);

        var f=document.getElementById('hiedataDisp');
        var s="createHierarchy.jsp?dimId="+dimId;
        f.src=s;
        $("#hiedataDispDiv").dialog('open');
            <%--document.getElementById('hiedataDispDiv').style.display='block';
            document.getElementById('fade').style.display='block';--%>
        //document.getElementById('fade').style.display='block';
    }
    function createHierarchy1(dimId){
        // window.open("createHierarchy.jsp?dimId="+dimId, "window.createHierarchy", "status=1,width=350,height=375");
        //showhierarchy(dimId);
        var flag="wizard";

        var f=document.getElementById('hiedataDisp');
        var s="createHierarchy.jsp?dimId="+dimId+"&flag="+flag;
        f.src=s;
        $("#hiedataDispDiv").dialog('open');
            <%--document.getElementById('hiedataDispDiv').style.display='block';
            document.getElementById('fade').style.display='block';--%>
        //document.getElementById('fade').style.display='block';
    }
    function createHierarchy2(dimId){
        var check="wizard";
         var f=document.getElementById('hiedataDisp');
        var s="createHierarchy.jsp?dimId="+dimId+"&check="+check;
        f.src=s;
        $("#hiedataDispDiv").dialog('open');
      }
    function createMember(colid){
        //window.open("createMember.jsp?colId="+colId, "window.createMember", "status=1,width=350,height=375");

        //var f.src=source;
        var listarr= colid.split(",");

        var dimtabId = listarr[0];
        var tableId = listarr[1];
        var dimId = listarr[2];
        var tabName = listarr[3];
        colId=colid;
        showMemberExistance(dimId,dimtabId);

    }
    function createMember1(colid){
        //window.open("createMember.jsp?colId="+colId, "window.createMember", "status=1,width=350,height=375");

        //var f.src=source;
        var listarr= colid.split(",");

        var dimtabId = listarr[0];
        var tableId = listarr[1];
        var dimId = listarr[2];
        var tabName = listarr[3];
        colId=colid;
        showMemberExistance1(dimId,dimtabId);

    }


    function cancelFade(){
        $("#dimension").dialog('close');
            <%--document.getElementById('fade').style.display='none';
            document.getElementById('dimension').style.display='none';--%>
    }

    function tabmsg1(){

        // alert(document.myForm.hieName.value)
        var name=document.myForm.dimensionName.value;
        // alert(name);
        document.myForm.dimensionDescription.value=name;
    }
    function saveDimension()
    {
        document.myForm.action="saveDimension.do";
        document.myForm.submit();
        //window.location.href=window.location.href;
    }
    function getData(dimId){

        var frameObj=document.getElementById("dataDispmem");
        //  var source="dimTablesList.do?dimId="+dimId;
        var source="CheckTableList.jsp?dimId="+dimId;
        frameObj.src=source;
        $("#dataDispmemDiv").dialog('open');
            <%--document.getElementById("dataDispmemDiv").style.display='block';
            document.getElementById('fade').style.display='block';--%>
        //document.getElementById("fade").style.display='block';
    }

    function dimColDelete(dimtableId){
        //alert(tableId);
        document.getElementById("dimtableId").value=dimtableId;
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
            showDeleteColumns(reqchk,dimtableId);
        }
        else
        {

            alert('Unselected Columns Are Deleted please select atleast one column');

        }
    }

    var xmlHttp2;

    function showhierarchy(str)
    {
        //alert(str);

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
        // alert(url)
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
            //alert("output is "+output);
            var members=output.split("\n");
            var rslength=members.length-1;
            //alert(rslength);

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
            // alert(document.getElementById("hierarchytable").innerHTML);
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

    function refreshparentmem()
    {
        //  document.myForm2.action="getAllDimensions.do";
        //  document.myForm2.submit();
        $("#dataDispmemDiv").dialog('close');
            <%--document.getElementById("dataDispmemDiv").style.display='none';
            document.getElementById('fade').style.display='';--%>
        // window.location.reload(true);

    }
    function refreshparentmem1()
    {

        window.location.reload(true);

    }
    function refreshparenthie()
    {
        $("#hiedataDispDiv").dialog('close');
            <%-- document.getElementById("hiedataDispDiv").style.display='none';
             document.getElementById('fade').style.display='';--%>
         // window.location.reload(true);
               
     }
     function refreshparenthie1()
     {

         window.location.reload(true);

     }
     function refreshparent()
     {
          document.myForm2.action="getAllDimensions.do";
          document.myForm2.submit();
         //window.location.reload(true);

     }
     function cancelTablesp()
     {
         //alert("hgj");
         // window.location.href=window.location.href;
         // window.location.href="<%=request.getContextPath()%>/getAllDimensions.do";
         // document.getElementById("dataDispmem").style.display='none';
         // document.getElementById('fade').style.display='';
         window.location.reload(true);
     }

     function cancelTablesp1()
     {
         //alert("hgj");
         // window.location.href=window.location.href;
         // window.location.href="<%=request.getContextPath()%>/getAllDimensions.do";
         $("#dataDispmemDiv").dialog('close');
            <%--document.getElementById("dataDispmemDiv").style.display='none';
            document.getElementById('fade').style.display='';--%>
        // window.location.reload(true);
    }


    function showMemberExistance(str,str1)
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

        var url=ctxPath+"/MemberExistance";
        url=url+"?dimId="+str+"&dimtabId="+str1;
        // alert(url)
        // var payload = "q="+str+"&id="+id;
        //alert('target url is---'+url);
        xmlHttp2.onreadystatechange=stateChangedMemberExistance;
        xmlHttp2.open("GET",url,true);
        xmlHttp2.send(null);
    }
function showMemberExistance1(str,str1)
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

        var url=ctxPath+"/MemberExistance";
        url=url+"?dimId="+str+"&dimtabId="+str1;
        // alert(url)
        // var payload = "q="+str+"&id="+id;
        //alert('target url is---'+url);
        xmlHttp2.onreadystatechange=stateChangedMemberExistance1;
        xmlHttp2.open("GET",url,true);
        xmlHttp2.send(null);
    }

 function stateChangedMemberExistance1()
    {
        // alert('hi in target')

        if (xmlHttp2.readyState==4)
        {
            var output=xmlHttp2.responseText;
            // alert("output is "+output);


            if(output>0){
                alert('Already Member Exist for this table');
            }
            else{
                //  alert('in if of member');
                if(dimensionName=='PR_DAY_DENOM' || dimensionName=='PR_DAY_INFO'){
                    createTimeMember();
                }else{
                var f=document.getElementById('tabDispmem');
                var s="dimensionTabMember.jsp?colId="+colId;
                f.src=s;
                $("#tabmemberDiv").dialog('open');
                }
            <%--document.getElementById('dataDispmemDiv').style.display='block';
            document.getElementById('fade').style.display='block';--%>

            }
        }
    }

    function stateChangedMemberExistance()
    {
        // alert('hi in target')

        if (xmlHttp2.readyState==4)
        {
            var output=xmlHttp2.responseText;
            // alert("output is "+output);


            if(output>0){
                alert('Already Member Exist for this table');
            }
            else{
                //  alert('in if of member');
                if(dimensionName=='PR_DAY_DENOM' || dimensionName=='PR_DAY_INFO'){
                    createTimeMember();
                }else{
                var f=document.getElementById('dataDispmem');

                var s="createMember.jsp?colId="+colId;
                f.src=s;
                $("#dataDispmemDiv").dialog('open');
                }
            <%--document.getElementById('dataDispmemDiv').style.display='block';
            document.getElementById('fade').style.display='block';--%>

            }
        }
    }
    function showDeleteColumns(str,str1)
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

        var url=ctxPath+"/DimensionDeleteColumns";
        url=url+"?"+str+"&dimtableId="+str1;
        // alert(url)
        // var payload = "q="+str+"&id="+id;
        //alert('target url is---'+url);
        xmlHttp2.onreadystatechange=stateChangedDeleteColumns;
        xmlHttp2.open("GET",url,true);
        xmlHttp2.send(null);
    }


    function stateChangedDeleteColumns()
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
    function saveTables()
    {
        $('#selectConnection').dialog('close');
        var connId1=document.getElementById("connId1").value;
        document.myForm.action="getAllDimensions.do?connId="+connId1;
        document.myForm.submit();
        /* var frameObj = document.getElementById('dimtab');
        var source="getAllDimensions.do?method= &connId="+connId1;
        frameObj.src=source;*/

        /* $.ajax({
                    url: 'getAllDimensions.do?connId='+connId1,
                    success: function(data){
                        alert(data)
                    }
                });

         */
        document.getElementById('dimtab').style.display='';
        // document.myForm.submit();
        //window.location.reload(true);
    }


    function goConnection(){
        // alert('in go con')
        //  IsConnection="NO";
        //  alert(IsConnection)
        $('#selectConnection').dialog('open');
    }


    function contextMenuWorkParentGrp(action, el, pos) {
        switch (action) {

            case "addParentGrp":
                {

                    var colid=$(el).attr('id');
                    //alert(colid);
                    var memId=colid.split(",")[0];
                    var hieId=colid.split(",")[1];
                    var dimId=colid.split(",")[2];
                    createParent(memId,hieId,dimId);
                    break;
                }

        }
    }
    function createParent(memId,hieId,dimId){
        var f=document.getElementById('dataDispparentgrp');
        var s="createParent.jsp?memId="+memId+"&hieId="+hieId+"&dimId="+dimId;
        f.src=s;
        $("#dataDispparentgrpDiv").dialog('open');
            <%-- document.getElementById('dataDispparentgrpDiv').style.display='block';
             document.getElementById('fade').style.display='block';--%>

     }
     function cancelGrp()
     {   $("#dataDispparentgrpDiv").dialog('close');
            <%--document.getElementById("dataDispparentgrpDiv").style.display='none';
            document.getElementById('fade').style.display='';--%>

    }
    function savecancelGrp()
    {  $("#dataDispparentgrpDiv").dialog('close');
            <%--document.getElementById("dataDispparentgrpDiv").style.display='none';
            document.getElementById('fade').style.display='';--%>
        document.getElementById('fade1').style.display='block';
        setTimeout("savecancelGrpwait()",3000);

    }
    function savecancelGrpwait(){
        window.location.reload(true);
        document.getElementById('fade1').style.display='';
    }


    function dimsremoveimage(){
        parent.document.getElementById('loading').style.display='none';
    }
    function refreshPage(){

        window.location.reload(true);
    }
    function saveDimensionMembers(){
        var elementIds = new Array();
        var i = 0;
        var connId  = document.getElementById("connId1").value;

        $('#sortable li').each(function(){
            var element = $(this).attr('id');
            var elemts = new Array;
            elemts = element.split("_");
            elementIds[i] = elemts[0];
            i++;
        })
        if(elementIds.length>0){
        $.ajax({
            url:'dimensioncheck.do?dimensionParam=saveAccessEnabledDimMembers&dim_tab_id='+dim_tab_id+'&col_id='+col_id+'&dimMembers='+elementIds,
            success: function(data){
                $("#timeDimensionMembers").dialog('close');
                     refreshPage();
            }
        });
        }
    }
        </script>
    </body>
</html>