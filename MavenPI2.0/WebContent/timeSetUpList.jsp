<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="prg.db.PbReturnObject,java.sql.*,utils.db.ProgenConnection,java.util.*,prg.db.PbDb"%>
<%@taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<html>
    
    <head>
        <% String ctxPath=request.getContextPath(); %>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Dimensions</title>


        <link type="text/css" href="stylesheets/demos.css" rel="stylesheet" />
        <link type="text/css" href="stylesheets/ui.all.css" rel="stylesheet" />
        <link rel="stylesheet" href="stylesheets/treeviewstyle/jquery.treeview.css" />
        <link href="stylesheets/jquery.contextMenu.css" rel="stylesheet" type="text/css" />

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
        <script type="text/javascript" src="javascript/queryDesign.js"></script>
        <script type="text/javascript" src="javascript/effects.core.js"></script>
        <script type="text/javascript" src="javascript/effects.explode.js"></script>

        <script language="JavaScript" src="<%=ctxPath%>/querydesigner/JS/jquery.columnfilters.js"></script>
        <!-- below five lines only added by bharathi reddy on 26-08-09 -->


        <script src="javascript/jquery.simplemodal-1.1.1.js" type="text/javascript"></script>
        <script src="javascript/jquery.contextMenu.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=ctxPath%>/jQuery/jquery/ui/ui.datepicker.js"></script>
        <link type="text/css" href="<%=ctxPath%>/stylesheets/metadataButton.css" rel="stylesheet" />
        
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
                border:solid black 1px;

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
            .MainTable{
                border-top-width: 1px;
                border-bottom-width: 1px;
                border-right-width: 1px;
                border-left-width: 1px;
                border-color:Black;



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
                top: 10px;
                left: 25%;
                width: 50%;
                height:50%;
                padding: 16px;
                border: 10px solid silver;
                background-color: white;
                z-index:1002;


            }
            .white_content2 {
                display: none;
                position: absolute;
                top: 10px;
                left: 25%;
                width:80%;
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
            a img{border:none}
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
            .myClassTest2{

                height:100%;
                width:30px;
                max-height:100%;
                min-width:700px;
                overflow:auto;

            }
            .savedRelations{
                border:solid black 0.5px;

            }
            #accordion{
                height:95%;

            }
            .label{
                font-family:verdana;
                font-size:12px;
                font-weight:normal;
            }
            .loading_image{
                display: block;
                position: absolute;
                top: 0%;
                left: 0%;
                width: 100%;
                height: 170%;
                background-color: black;
                z-index:1001;
                -moz-opacity: 0.5;
                opacity:.50;
                filter:alpha(opacity=50);
                z-index:1001;
                overflow:hidden;}
            </style>


        </head>
        <body style="overflow:hidden" class="body" onload="Timeremoveimg()">
        <%

                    String connId = String.valueOf(session.getAttribute("connId"));
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
        <table style="width:100%;height:100%" border="solid black 1px"  border="1">
            <form name="myForm3" method="post">
                <tr>
                    <td width="400px" valign="top" class="draggedTable1">
                        <div style="height:33px" class="navtitle-hover draggedDivs ui-corner-all">
                            <font style="font-weight:bold" face="verdana" size="1px"> Time Set-Up
                                - <a href="javascript:void(0)" onclick="javascript:goConnection()" style="color:black;text-decoration: none;"><b><%=connName%></b></a></font>
                            <table align="right" >
                                <tr>
                                    <td >
                                        <a href="javascript:void(0)" onclick="refreshPage()"><img   src="<%=request.getContextPath()%>/icons pinvoke/refreshPage.png"></img></a>
                                    </td>
                                </tr>
                            </table>
                        </div>
                        <div style="height:553px;overflow-y:auto">
                            <ul id="myList4" class="filetree">
                                <li class="closed" style="background-image:url('images/treeViewImages/plus.gif')"><img src="images/treeViewImages/calendar-list.png"><span id="123"><font size="1.5px" face="verdana" class="calenderCreateMenu">Time Calenders</font></span>
                                    <ul>
                                        <logic:notEmpty name="calList">
                                            <logic:iterate id="calList" name="calList" >

                                                <logic:equal name="calList" property="calId" value="1">

                                                    <ul>
                                                        <li id="1" ><img src="images/treeViewImages/calendar.png"><span id="1" class="calenderMenu"><font size="1px" face="verdana"><bean:write name="calList" property="calName"/></font></span>
                                                            <ul>
                                                                <li id="1" class="closed"><img src="images/treeViewImages/calendar.png"><span id="<bean:write name="calList" property="calId"/>-1"  class="standMenu"><font size="1px" face="verdana">Year</font></span></li>
                                                                <li id="2" class="closed"><img src="images/treeViewImages/calendar-select-days-span.png"><span id="<bean:write name="calList" property="calId"/>-2"  class="standMenu"><font size="1px" face="verdana">Quarter</font></span></li>
                                                                <li id="3" class="closed"><img src="images/treeViewImages/calendar-select-month.png"><span id="<bean:write name="calList" property="calId"/>-3"  class="standMenu"><font size="1px" face="verdana">Month</font></span></li>
                                                                <li id="4" class="closed"><img src="images/treeViewImages/calendar-select-week.png"><span id="<bean:write name="calList" property="calId"/>-4"  class="standMenu"><font size="1px" face="verdana">Week</font></span></li>
                                                                <li id="5" class="closed"><img src="images/treeViewImages/calendar-day.png"><span id="<bean:write name="calList" property="calId"/>-5"  class="standMenu"><font size="1px" face="verdana">Day</font></span></li>
                                                            </ul>
                                                        </li>
                                                    </ul>

                                                </logic:equal>

                                                <logic:notEqual name="calList" property="calId" value="1">
                                                    <li id="<bean:write name="calList" property="calId"/>" class="closed" ><img src="images/treeViewImages/calendar.png"><span id="<bean:write name="calList" property="calId"/>" class="calenderMenu"><font size="1px" face="verdana"><bean:write name="calList" property="calName"/></font></span>
                                                        <ul>
                                                            <ul>
                                                                <li id="1" class="closed"><img src="images/treeViewImages/calendar.png"><span id="<bean:write name="calList" property="calId"/>-1"  class="standMenu"><font size="1px" face="verdana">Year</font></span></li>
                                                                <li id="2" class="closed"><img src="images/treeViewImages/calendar-select-days-span.png"><span id="<bean:write name="calList" property="calId"/>-2"  class="standMenu"><font size="1px" face="verdana">Quarter</font></span></li>
                                                                <li id="3" class="closed"><img src="images/treeViewImages/calendar-select-month.png"><span id="<bean:write name="calList" property="calId"/>-3"  class="standMenu"><font size="1px" face="verdana">Month</font></span></li>
                                                                <li id="4" class="closed"><img src="images/treeViewImages/calendar-select-week.png"><span id="<bean:write name="calList" property="calId"/>-4"  class="standMenu"><font size="1px" face="verdana">Week</font></span></li>
                                                                <li id="5" class="closed"><img src="images/treeViewImages/calendar-day.png"><span id="<bean:write name="calList" property="calId"/>-5"  class="standMenu"><font size="1px" face="verdana">Day</font></span></li>
                                                            </ul>
                                                        </ul>
                                                    </li>
                                                </logic:notEqual >
                                            </logic:iterate>
                                        </logic:notEmpty>

                                    </ul>
                                </li>
                            </ul>
                        </div>
                    </td>
                <div id="tddiv" style="display:none" >
                    <td  valign="top" width=400px">
                        <%-- <div style="height:33px" class="navtitle-hover draggedDivs ui-corner-all">
                             <font style="font-weight:bold" face="verdana" size="1px">Create Calender</font>
                         </div>--%>

                        <div  id="createCalenderDiv"  style="display:none" title="Create Calender">
                            <iframe name="calenderframe" id="calenderframe" src="#" frameborder="0" marginheight="0" marginwidth="0" width="100%" height="100%"></iframe>
                            <%-- <script>
                                 $(function() {
                                     $('#datepicker').datepicker({
                                         changeMonth: true,
                                         changeYear: true,
                                         showButtonPanel: true,
                                         numberOfMonths: 3,
                                         stepMonths: 3
                                     });
                                     $('#datepicker1').datepicker({
                                         changeMonth: true,
                                         changeYear: true,
                                         showButtonPanel: true,
                                         numberOfMonths: 3,
                                         stepMonths: 3
                                     });
                                 });
                             </script>
                             <center>

                                    <table align="center" style="width:80%">
                                    <tr><td>&nbsp;</td></tr>
                                    <tr><td>&nbsp;</td></tr>
                                    <tr><td>&nbsp;</td></tr>
                                    <tr>
                                            <td><label class="label" >Calender Type</label></td>
                                            <td>
                                            <select id="ccalType" name="ccalType" style="width:130px">
                                                <option value="Enterprise">Enterprise</option>
                                            </select>
                                        </td>
                                    </tr>
                                        <tr>
                                            <td><label class="label">Calender Name</label></td>
                                            <td><input type="text" name="ccalName" id="ccalName" style="width:130px"> </td>
                                        </tr>

                                        <tr>
                                            <td><label class="label">Customise</label></td>
                                        <td>
                                            <input type="checkbox" id="custom" name="custom"  onchange="customcheck()">
                                        </td>
                                    </tr>
                                </table>
                                <div id="customDiv" style="display:none">
                                        <table style="width:80%" align="center">
                                        <tr>
                                                <td><label class="label">Year Start Month</label></td>
                                                <td><input type="text" name="strYear" id="datepicker" style="width:130px"></td>
                                        </tr><tr>
                                                <td><label class="label">Year End Month</label></td>
                                                <td><input type="text" name="endYear" id="datepicker1" style="width:130px"></td>
                                        </tr>
                                    </table>
                                </div>
                                    <br>
                                <table>
                                    <tr>
                                        <td>
                                            <input type="button" name="Save" class="navtitle-hover" value="Save" onclick="saveCraeatetimesetUp()">
                                            </td>
                                            <td>
                                            <input type="button" name="Cancel" value="Cancel" class="navtitle-hover" onclick="cancelCraeatetimesetUp()">
                                        </td>
                                    </tr>
                                </table>

                                <input type="hidden" name="h" id="h" value="<%=request.getContextPath()%>">
                                <input type="hidden" name="numofdaysyr" id="numofdaysyr" >
                                <input type="hidden" name="connId" id="connId" value="<%=connId%>" >
                            </center>--%>
                            <input type="hidden" name="h" id="h" value="<%=request.getContextPath()%>">
                        </div>
                        <div class="loading_image" id="loading" style="display: none"><img border="0px" style="position: absolute; left: 600px; top: 200px;" src="images/help-loading.gif" id="imgId"></div>
                            <%--
            <div  id="standardCalender" style="display:none" class="white_content1" >
                <center>
                <form name="myForm2" method="post">
                    <table align="center" >
                         <tr><td>&nbsp;</td></tr>
                    <tr><td>&nbsp;</td></tr>
                    <tr><td>&nbsp;</td></tr>
                        <tr>
                            <td>Calender ID</td><td><input type="text" name="calId" id="calId" readOnly > </td>
                        </tr><tr>
                            <td>Calender Type</td><td><input type="text" name="calType" id="calType" readOnly > </td>
                        </tr><tr>
                            <td>Calender Name</td><td><input type="text" name="calName" id="calName" > </td>
                            <tr></tr>
                            <td>Denorm Table</td><td><input type="text" name="denomTab" readOnly id="denomTab"> </td>

                    </tr>
                    <tr><td>Customise</td>
                        <td>
                            <input type="checkbox" id="custom" name="custom" value="N" onchange="customcheck()">
                        </td>
                    </tr>
                   </table>
                    <div id="customDiv" style="display:none">
                      <table>

                    <tr>
                        <td>Year Start&nbsp;&nbsp;&nbsp;&nbsp;</td><td>&nbsp;<input type="text" name="strYear" id="strYear"> </td>
                    </tr><tr>
                        <td>Year End&nbsp;&nbsp;&nbsp;&nbsp;</td><td>&nbsp;<input type="text" name="endYear" id="endYear"> </td>
                    </tr>

                      </table>
                    </div>
                        <table>
                            <tr>
                                <td>
                                    <input type="button" name="Save" value="Save" onclick="savetimesetUp()">
                                    <input type="button" name="Cancel" value="Cancel" onclick="canceltimesetUp()">
                                </td>
                            </tr>

                        </table>


                <input type="hidden" name="h" id="h" value="<%=request.getContextPath()%>">

            </form>
            </center>
        </div>

                            --%>

                    </td>
                </div>
                <div id="timeedittabDiv" style="display:none" title="Edit">

                    <iframe  id="timeedittab" NAME="timeedittab"  style="width:350px;height: 200px" SRC='#' frameborder="0"></iframe>

                </div>
                <div style="display:none"><td width="5%" valign="top">
                        <iframe  id="standardcustom" NAME="standardcustom"  style="width:700px" class="white_content2" SRC='#' frameborder="0"></iframe>
                    </td>
                </div>
                </tr>
            </form>
        </table>

        <ul id="calMenu" class="contextMenu" style="width:150px;text-align:left">

            <li class="insert"><a href="#editCalender">Edit</a></li>
            <li class="insert"><a href="#WeekHolidays">WeekHolidays</a></li>
            <li class="insert"><a href="#YearHolidays">YearHolidays</a></li>
            <li class="insert"><a href="#ExceptionalHolidays">ExceptionalHolidays</a></li>
        </ul>
        <ul id="standCustomMenu" class="contextMenu" style="width:150px;text-align:left">

            <li class="insert"><a href="#standCustom">View</a></li>
        </ul>

        <ul id="calCreateMenu" class="contextMenu" style="width:150px;text-align:left">

            <li class="insert"><a href="#createCalender">Create Calender</a></li>
        </ul>
        <div id="fade" class="black_overlay"></div>
        <%String Query = " SELECT CONNECTION_ID, CONNECTION_NAME FROM PRG_USER_CONNECTIONS";
                    PbReturnObject pbro1 = new PbDb().execSelectSQL(Query);
        %>
        <div >
            <div>
                <iframe  id="timesetuptab" NAME='timesetuptab' style="height:600px;width:100%;display:none" SRC='#' frameborder="0" marginheight="0" marginwidth="0" ></iframe>
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
                <input type="hidden" name="connId" id="connId" value="<%=connId%>" >
                <%--<input type="button"  value="Connect" onclick="saveTables()">--%>
            </form>

        </div>
        <div id="weekholdaysdiv" style="display:none" title="Week Holidays" >
            <iframe id="weekholdaystab" name='weekholdaystab' style="height:150px;width:100%;" SRC='#' frameborder="0"></iframe>
        </div>
        <div id="yearholdaysdiv" style="display:none" title="Year Holidays" >
            <iframe id="yearholdaystab" name='yearholdaystab' style="height:100%;width:100%;" SRC='#' frameborder="0"></iframe>
        </div>
        <div id="exceptionalholdaysdiv"  title="Exceptional Holidays" style="display:none">
            <iframe id="exceptionalholdaystab" name='exceptionalholdaystab' style="width:100%;" SRC='#' frameborder="0"></iframe>
        </div>
<script type="text/javascript">
            var numofdays='';
            $(document).ready(function() {
                if ($.browser.msie == true){
                    $("#createCalenderDiv").dialog({
                        bgiframe: true,
                        autoOpen: false,
                        resizable: false,
                        height:450,
                        width:850,
                        position:'top',
                        modal: true,
                        overlay: {
                            backgroundColor: '#000',
                            opacity: 0.5
                        }
                    });
                    $("#timeedittabDiv").dialog({
                        bgiframe: true,
                        autoOpen: false,
                        resizable: false,
                        height:300,
                        width:300,
                        position:'top',
                        modal: true,
                        overlay: {
                            backgroundColor: '#000',
                            opacity: 0.5
                        }
                    });
                    $("#weekholdaysdiv").dialog({
                        bgiframe: true,
                        autoOpen: false,
                        resizable: false,
                        height:200,
                        width:300,
                        position:'top',
                        modal: true,
                        overlay: {
                            backgroundColor: '#000',
                            opacity: 0.5
                        }
                    });
                    $("#yearholdaysdiv").dialog({
                        bgiframe: true,
                        autoOpen: false,
                        resizable: false,
                        height:450,
                        width:850,
                        position:'top',
                        modal: true,
                        overlay: {
                            backgroundColor: '#000',
                            opacity: 0.5
                        }
                    });
                    $("#exceptionalholdaysdiv").dialog({
                        bgiframe: true,
                        autoOpen: false,
                        resizable: false,
                        height:250,
                        width:850,
                        position:'top',
                        modal: true,
                        overlay: {
                            backgroundColor: '#000',
                            opacity: 0.5
                        }
                    });



                }else{
                    $("#createCalenderDiv").dialog({
                        bgiframe: true,
                        autoOpen: false,
                        resizable: false,
                        height:450,
                        width:850,
                        position:'top',
                        modal: true,
                        overlay: {
                            backgroundColor: '#000',
                            opacity: 0.5
                        }
                    });
                    $("#timeedittabDiv").dialog({
                        bgiframe: true,
                        autoOpen: false,
                        resizable: false,
                        height:300,
                        width:400,
                        position:'top',
                        modal: true,
                        overlay: {
                            backgroundColor: '#000',
                            opacity: 0.5
                        }
                    });
                    $("#weekholdaysdiv").dialog({
                        bgiframe: true,
                        autoOpen: false,
                        resizable: false,
                        height:200,
                        width:300,
                        position:'top',
                        modal: true,
                        overlay: {
                            backgroundColor: '#000',
                            opacity: 0.5
                        }
                    });
                    $("#yearholdaysdiv").dialog({
                        bgiframe: true,
                        autoOpen: false,
                        resizable: false,
                        height:450,
                        width:850,
                        position:'top',
                        modal: true,
                        overlay: {
                            backgroundColor: '#000',
                            opacity: 0.5
                        }
                    });
                    $("#exceptionalholdaysdiv").dialog({
                        bgiframe: true,
                        autoOpen: false,
                        resizable: false,
                        height:250,
                        width:850,
                        position:'top',
                        modal: true,
                        overlay: {
                            backgroundColor: '#000',
                            opacity: 0.5
                        }
                    });


                }

                function closeCalenderParent(){
                    $("#createCalenderDiv").dialog('close');
                }


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

                $(".calenderMenu").contextMenu({ menu: 'calMenu' }, function(action, el, pos) { contextMenuCalender(action, el, pos); });

                $(".calenderCreateMenu").contextMenu({ menu: 'calCreateMenu' }, function(action, el, pos) { contextMenucalCreateMenu(action, el, pos); });
                $(".standMenu").contextMenu({ menu: 'standCustomMenu' }, function(action, el, pos) { contextMenustandCustomMenu(action, el, pos); });

                $("#myList4").treeview({
                    animated:"slow",
                    persist: "cookie"
                });
                $("#selectConnection").dialog({
                    bgiframe: true,
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
                            saveTablestimecon();
                            $(this).dialog('close');
                        }
                    },
                    close: function() {

                    }
                });
            });
            function contextMenuCalender(action, el, pos) {

                switch (action) {
                    case "deleteDim":
                        {
                            var msg = "Delete " + $(el).find("#contactname").text() + "?";
                            $("#HiddenFieldRowId").val($(el).find("#customerid").text());
                            confirm(msg);
                            break;
                        }
                    case "editCalender":
                        {   // alert($(el).attr('id'));
                            var calid=$(el).attr('id');
                            showcalender(calid);
                            break;
                        }
                    case "WeekHolidays":
                        {
                            var tableid=$(el).attr('id');
            <%--alert(tableid);--%>
                                addweekHoliday(tableid);
                                break;
                            }
                        case "YearHolidays":
                            {
                                var calid=$(el).attr('id');
                                //alert(calid);
                                addyearHolidays(calid);
                                break;
                            }
                        case "ExceptionalHolidays":
                            {
                                var calid=$(el).attr('id');
                                //alert(calid);
                                addExceptionalHolidays(calid);
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

                    function addweekHoliday(tableid)
                    {
                        $("#weekholdaysdiv").dialog('open');
                        var frameObj=document.getElementById("weekholdaystab");
                        var source="Timesetuppages/weekHolidays.jsp?tableid="+tableid;
                        frameObj.src=source;
                        //alert(frameObj.src);
            <%-- document.getElementById('weekholdaysdiv').style.display='block';
             document.getElementById("weekholdaystab").style.display='block';
             document.getElementById('fade').style.display='block';--%>
                     }

                     function addyearHolidays(tableid) {
                         $("#yearholdaysdiv").dialog('open')
                         var frameObj=document.getElementById("yearholdaystab");
                         var source="Timesetuppages/yearHolidays.jsp?tableid="+tableid;
                         frameObj.src=source;
                         //alert(frameObj.src);
            <%-- document.getElementById('yearholdaysdiv').style.display='block';
             document.getElementById("yearholdaystab").style.display='block';
             document.getElementById('fade').style.display='block';--%>
                     }

                     function addExceptionalHolidays(tableid)
                     {
                         $("#exceptionalholdaysdiv").dialog('open')

                         var frameObj=document.getElementById("exceptionalholdaystab");
                         var source="Timesetuppages/exceptionalHolidays.jsp?tableid="+tableid;
                         frameObj.src=source;
            <%--  //alert(frameObj.src);
              document.getElementById('exceptionalholdaysdiv').style.display='block';
              document.getElementById("exceptionalholdaystab").style.display='block';
              document.getElementById('fade').style.display='block';--%>
                      }


                      function contextMenustandCustomMenu(action, el, pos) {

                          switch (action) {
                              case "deleteDim":
                                  {
                                      var msg = "Delete " + $(el).find("#contactname").text() + "?";
                                      $("#HiddenFieldRowId").val($(el).find("#customerid").text());
                                      confirm(msg);
                                      break;
                                  }
                              case "standCustom":
                                  {
                                      // alert($(el).attr('id'));
                                      standCustomize($(el).attr('id'));
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
                      function contextMenucalCreateMenu(action, el, pos) {

                          switch (action) {
                              case "deleteDim":
                                  {
                                      var msg = "Delete " + $(el).find("#contactname").text() + "?";
                                      $("#HiddenFieldRowId").val($(el).find("#customerid").text());
                                      confirm(msg);
                                      break;
                                  }
                              case "createCalender":
                                  {
                                      createCalender();
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
                      var xmlHttp2;

                      function showcalender(str)
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

                          var url=ctxPath+"/CalenderEditSetUp";
                          url=url+"?calId="+str;
                          // alert(url);
                          // var payload = "q="+str+"&id="+id;
                          //alert('target url is---'+url);
                          xmlHttp2.onreadystatechange=stateChangedcalender;
                          xmlHttp2.open("GET",url,true);
                          xmlHttp2.send(null);
                      }


                      function stateChangedcalender()
                      {
                          //  alert('hi in target')

                          if (xmlHttp2.readyState==4)
                          {
                              var output=xmlHttp2.responseText;
                              //alert("output is "+output);
                              var calDetails=output.split("\n");
                              //alert(calDetails);
                              var calvalues=calDetails[0].split(",");
                              var connid = document.getElementById("connId1").value;
                              // alert(connid);
                              // document.getElementById('calId').value=calvalues[0];
                              // document.getElementById('calType').value=calvalues[1];
                              // document.getElementById('calName').value=calvalues[2];
                              // document.getElementById('denomTab').value=calvalues[3];
                              $("#timeedittabDiv").dialog('open');
                              var frameObj=document.getElementById("timeedittab");
                              var source="editTimeSetup.jsp?calId="+calvalues[0]+"&calType="+calvalues[1]+"&calName="+calvalues[2]+"&denomTab="+calvalues[3]+"&connid="+connid;
                              frameObj.src=source;
                            

                              // alert(frameObj.src);
            <%--document.getElementById("timeedittab").style.display='block';
            document.getElementById('fade').style.display='block';--%>

                            // standardCalender();

                        }

                    }

                    function  standCustomize(minlevel){
                        var dets1=minlevel.split("-");
                        var frameObj=document.getElementById("standardcustom");
                        var source="standardCalenderCustomization.jsp?minlevel="+dets1[1]+"&CalenderId="+dets1[0];
                        frameObj.src=source;
                        // alert(frameObj.src);
                        document.getElementById("standardcustom").style.display='block';
                        document.getElementById('fade').style.display='block';
                    }

                    function standardCalender(){

                        document.getElementById('standardCalender').style.display='block';
                        document.getElementById('fade').style.display='block';
                    }
                    function testing(){
                        $("#createCalenderDiv").dialog('close');
                        window.location.href = window.location.href;
                    }
                    function createCalender(){
            <%--document.getElementById('tddiv').style.display='';--%>
                        $("#createCalenderDiv").dialog('open');
                        //    document.getElementById('createCalenderDiv').style.display='';
                        var frameObj = document.getElementById('calenderframe');
                        var source = '<%=request.getContextPath()%>/createUserCalender.jsp';
                        frameObj.src = source;
                        // alert(frameObj);

                        //  document.getElementById('fade').style.display='block';
                    }
                    function savetimesetUp(){
                        document.myForm2.action="saveTimeSetUp.do";
                        document.myForm2.submit();
                        refreshList1();
                    }
                    function refreshList(){
                        // alert('1')
                        window.location.reload(true);
                        // alert('2')

                    }
                    function refreshList1(){
                        document.myForm.action="allTimeSetUps.do";
                        document.myForm.submit();
                        //  window.location.reload(true);
                    }
                    function refreshList3(){
                        document.myForm3.action="allTimeSetUps.do";
                        document.myForm3.submit();
                        //  window.location.reload(true);
                    }

                    function canceltimesetUp(){
                        $("#timeedittabDiv").dialog('close');
            <%--  document.getElementById('timeedittab').style.display='none';
              document.getElementById('fade').style.display='';--%>
                      }
                      function canceltimesetUpCustom(){
                          document.getElementById('standardcustom').style.display='none';
                          document.getElementById('fade').style.display='';
                      }
                      function cancelCraeatetimesetUp(){
                          document.getElementById('fade').style.display='';
                          document.getElementById('createCalenderDiv').style.display='none';
                          document.getElementById("ccalType").value="";
                          document.getElementById("ccalName").value="";
                          document.getElementById("datepicker").value="";
                          document.getElementById("datepicker1").value="";
                          document.getElementById('custom').checked=false;
                          document.getElementById('customDiv').style.display='none';
                      }
                      function saveCraeatetimesetUp(){
                          var calType=document.getElementById("ccalType").value;
                          var calName=document.getElementById("ccalName").value;
                          var stryear=document.getElementById("datepicker").value;
                          var endyear=document.getElementById("datepicker1").value;
                          var str=stryear.split('/');
                          var strday=str[1];
                          var strmonth=str[0];
                          var stryr=str[2];
                          var end=endyear.split('/');
                          var endday=str[1];
                          var endmonth=str[0];
                          var endyr=str[2];
                          /* if(calType==""){
                    alert('Please Enter Calender Type');
                }else */
                          if(calName==""){
                              alert('Please enter Calender Name');
                          }
                          else if(document.getElementById("custom").checked==true){

                              if(stryear=="" || endyear==""){
                                  alert('Please Enter Dates ');
                              }
                              else{

                                  shownumofdays(stryear,endyear);
                                  /* alert('s'+numofdays)
                   if(document.getElementById("numofdaysyr").value>366 ||document.getElementById("numofdaysyr").value<365){
                       alert('Please Select Complete Year');
                   }else{
                   var day;
                    if(strday!=01){
                         day=confirm('Your year does not start from month start date \n Are you want to Configure ')
                    }
                    if(day==true){
                    document.myForm3.action="saveCreateTimeSetUp.do";
                    document.myForm3.submit();
                   // refreshList();
                   }}
                                   */
                              }
                          }else{
                              document.myForm3.action="saveCreateTimeSetUp.do";
                              // alert('===='+document.getElementById("custom").value)
                              document.getElementById("custom").value='N';
                              //  alert('===='+document.getElementById("custom").value)
                              document.myForm3.submit();
                              //refreshList();
                          }
                      }
                      function customcheck(){
                          if(document.getElementById('custom').checked==true)
                          {
                              document.getElementById('customDiv').style.display='';
                          }else{
                              document.getElementById('customDiv').style.display='none';
                          }
                      }



                      function shownumofdays(str,end)
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

                          var url=ctxPath+"/CalenderNumofDaysCheck";
                          url=url+"?strdate="+str+"&enddate="+end;
                          // alert(url)
                          // var payload = "q="+str+"&id="+id;
                          //alert('target url is---'+url);
                          xmlHttp2.onreadystatechange=stateChangedshownumofdays;
                          xmlHttp2.open("GET",url,true);
                          xmlHttp2.send(null);
                      }


                      function stateChangedshownumofdays()
                      {
                          //  alert('hi in target')

                          if (xmlHttp2.readyState==4)
                          {
                              var output=xmlHttp2.responseText;
                              // alert("output is "+output);
                              numofdays=output.split("\n")[0].split(",")[0];
                              var checksysdate=output.split("\n")[0].split(",")[1];
                              document.getElementById("numofdaysyr").value=output.split("\n")[0].split(",")[0];
                              //alert(document.getElementById("numofdaysyr").value)
                              //alert('s'+numofdays)
                   
                              // alert(checksysdate);
                              if(checksysdate<0){

                                  alert('Please select start date before current date')
                              }
                              else if(document.getElementById("numofdaysyr").value<=0){
                                  alert('Please Select End date greaterthan Start date')
                              }else{
                                  if(document.getElementById("numofdaysyr").value>366 ||document.getElementById("numofdaysyr").value<365){
                                      alert('Please Select Complete Year');
                                  }else{
                                      var day;
                                      if(document.getElementById("datepicker").value.split('/')[1]!=01){
                                          day=confirm('Your year does not start from month start date \n Are you want to Configure ')
                                          if(day!=true){
                                              document.myForm3.action="saveCreateTimeSetUp.do";
                                              //  alert(document.getElementById("custom").value)
                                              document.getElementById("custom").value='Y';
                                              // alert('=='+document.getElementById("custom").value)
                                              document.myForm3.submit();
                                              // refreshList();
                                          }
                                      }
                                      else{
                                          document.myForm3.action="saveCreateTimeSetUp.do";
                                          //  alert(document.getElementById("custom").value)
                                          document.getElementById("custom").value='Y';
                                          //  alert('==='+document.getElementById("custom").value)

                                          document.myForm3.submit();
                                      }
                                  }
                              }
                          }
                          //return numofdays;
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
                      function saveTablestimecon()
                      {
                          $('#selectConnection').dialog('close');
                          var connId1=document.getElementById("connId1").value;
                          document.getElementById("connId").value =connId1;
                          //var frameObj = document.getElementById('timesetuptab');
                          document.myFormcon.action = "allTimeSetUps.do?connId="+connId1;
                          //alert(connId1+"---------connId1")
                          document.myFormcon.submit();
                          //var source="allTimeSetUps.do?connId="+connId1;
                          //  alert(source)
                          //frameObj.src=source;
                          document.getElementById('timesetuptab').style.display='';
                          // var frameObj = document.getElementById('businessgrptab');
                          //window.location.reload(true);
                      }
                      function goConnection(){
                        
                          $('#selectConnection').dialog('open');
                      }

                      function Timeremoveimg(){
                          // alert("Hai");
                          parent.document.getElementById('loading').style.display='none';
                      }
                      function pageexits(){
                          parent.document.getElementById('loading').style.display='block';
                      }
                      window.onunload = pageexits;
                      function refreshPage(){
                          window.location.reload(true);
                          parent.document.getElementById('loading').style.display='block';
                      }
        </script>
    </body>
</html>