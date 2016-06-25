<%--
    Document   : NewReportLook
    Created on : Oct 1, 2009, 3:39:50 PM
    Author     : Administrator
--%>

<%@page contentType="text/html" pageEncoding="windows-1252"%>
<%@page import="java.sql.*" %>
<%@page import="utils.db.*" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<%--<%@include file="pbStickyNote.jsp" %>--%>
<% String contextPath=request.getContextPath();%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1252">

        <link type="text/css" href="<%=contextPath%>/jQuery/jquery/themes/base/ui.theme.css" rel="stylesheet" />


        <link type="text/css" href="<%=contextPath%>/stylesheets/demos.css" rel="stylesheet" />
        <link type="text/css" href="<%=contextPath%>/stylesheets/ui.all.css" rel="stylesheet" />
        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/treeviewstyle/jquery.treeview.css" />
        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/treeviewstyle/screen.css" />
        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/jq.css" type="text/css" media="print, projection, screen">
        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/style.css" type="text/css" media="print, projection, screen">
        <link href="<%=contextPath%>/stylesheets/javascript.css" type="text/css" rel="stylesheet">
        <link href="<%=contextPath%>/stylesheets/html.css" type="text/css" rel="stylesheet">
        <link href="<%=contextPath%>/stylesheets/css.css" type="text/css" rel="stylesheet">

        <link href="<%=contextPath%>/reportDesigner.css" type="text/css" rel="stylesheet">



        <script type="text/javascript" src="<%=contextPath%>/javascript/lib/jquery/js/jquery-1.4.2.min.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/jQuery/jquery/ui/ui.core.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/jQuery/jquery/external/bgiframe/jquery.bgiframe.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/jQuery/jquery/ui/ui.changedialog.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/jQuery/jquery/ui/ui.resizable1.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/jQuery/jquery/ui/ui.draggable1.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/ui.dialog.js"></script>
        <script type="text/javascript" src="stickyNote.js"></script>
        <script>
           <%-- stickyNoteCount = "<%=stickyNoteCount%>";
            stickyNoteCount++;--%>
        </script>
        <title>Report's NewLook </title>

        <style type="text/css">
            .navsection {
                text-decoration: none;
                margin: 0 0 0 0;
                border: 1px solid #CDCDCD;
/*                background: url(../images/navtitlebg.gif) no-repeat top left;*/
                -moz-border-radius: 4px 4px 4px 4px;
                COLOR: #000;
                WIDTH: 100%;
            }
            .navtitle
            {
                -moz-border-radius-bottomleft:4px;
                -moz-border-radius-bottomright:4px;
                -moz-border-radius-topleft:4px;
                -moz-border-radius-topright:4px;
                FONT-SIZE: 11px;
                COLOR: #000;
                FONT-FAMILY: Verdana;
                VERTICAL-ALIGN: middle;
                HEIGHT: 20px;
                WIDTH: 100%;
                background-color:#BDBDBD;
                cursor:pointer;
            }
            .navtitle1
            {
                -moz-border-radius-bottomleft:4px;
                -moz-border-radius-bottomright:4px;
                -moz-border-radius-topleft:4px;
                -moz-border-radius-topright:4px;
                FONT-SIZE: 11px;
                COLOR: #000;
                FONT-FAMILY: Verdana;
                VERTICAL-ALIGN: middle;
                HEIGHT: 20px;
                WIDTH: 100%;
                background-color:#BDBDBD;
                cursor:pointer;
            }
            .ui-corner-all {
                -moz-border-radius-bottomleft:6px;
                -moz-border-radius-bottomright:6px;
                -moz-border-radius-topleft:6px;
                -moz-border-radius-topright:6px;
            }
            .ui-state-default, .ui-widget-content .ui-state-default {
                -moz-background-clip:border;
                -moz-background-inline-policy:continuous;
                -moz-background-origin:padding;
                background:#E6E6E6 repeat-x scroll 50% 50%;
                border:1px solid #E6E6E6;
                height:20px;
                color:#000;
                font-weight:normal;

                outline-style:none;
                outline-width:medium;
            }
            .frame1{
                border: 0px;
                width: 700px;
                height: 500px;
            }
            .test
            {
                width : 140px;
                height : 120px;
                border : 1px solid #ffff99;
                background-color: #ffff99;
            }
            .mycls
            {
                width : 150px;
                height : 150px;
                border : 1px solid #ffff99;
                background-color: #ffff99;
            }
            .colorButton
            {
                width:20px;
                height:20px;
                background-color:green;
            }
            .inputlabel
            {
                font-size: 10px;

                border-right:#000000 1px outset;
                border-right:#000000 1px inset;
                border-top: white 1px inset;
                border-left: white 1px inset;
                border-bottom: #000000 1px inset;
                font-family: verdana, arial;
                background-color: transparent;
            }
        </style>
        <script type="text/javascript">
            $(document).ready(function(){
                $("#msgframe").dialog({
                    bgiframe: true,
                    autoOpen: false,
                    height: 300,
                    modal: true,
                    Cancel: function() {
                        $(this).dialog('close');
                    }
                });
                $("#cstLinksFrame").dialog({
                    bgiframe: true,
                    autoOpen: false,
                    height: 300,
                    modal: true,
                    Cancel: function() {
                        $(this).dialog('close');
                    }
                });
                $("#prtLinksFrame").dialog({
                    bgiframe: true,
                    autoOpen: false,
                    height: 300,
                    modal: true,
                    Cancel: function() {
                        $(this).dialog('close');
                    }
                });
                $("#Scheduler").dialog({
                    bgiframe: true,
                    autoOpen: false,
                    height: 300,
                    modal: true,
                    Cancel: function() {
                        $(this).dialog('close');
                    }
                });
                $("#tracker").dialog({
                    bgiframe: true,
                    autoOpen: false,
                    height: 300,
                    modal: true,
                    Cancel: function() {
                        $(this).dialog('close');
                    }
                });
                $('#defineSchedulerDiv').click(function() {
                    $('#reportSchedulerDialog').dialog('open');
                });
                $('#defineTrackerDiv').click(function() {
                    $('#tracker').dialog('open');
                });
                $('#composeMessageDiv').click(function() {
                    $('#composeMessageDialog').dialog('open');
                });

                $('#Customize').click(function() {
                    $('#favLinksDialog').dialog('open');
                });

                $('#Prioritize').click(function() {
                    $('#prtLinksFrame').dialog('open');
                });
                $('#sanpShotDiv').click(function() {
                    $('#snapShotDialog').dialog('open');
                });


                $test=$(".navtitle");

                $test.hover(
                function(){
                    this.style.background="#308DBB";
                    this.style.color="#fff";
                },
                function(){
                    this.style.background="#BDBDBD";
                    this.style.color="#000"
                }
            );
                $test=$(".ui-state-default ");

                $test.hover(
                function(){
                    this.style.background="#308DBB";
                    this.style.color="#000";
                },
                function(){
                    this.style.background="#E6E6E6";
                    this.style.color="#000"
                }

            );

                $("#favLinksDialog").dialog({
                    //bgiframe: true,
                    autoOpen: false,
                    height: 520,
                    width: 660,
                    position: 'top',
                    modal: true
                });

                 $("#composeMessageDialog").dialog({
                    //bgiframe: true,
                    autoOpen: false,
                    height: 420,
                    width: 720,
                    position: 'top',
                    modal: true
                });

                $("#snapShotDialog").dialog({
                    //bgiframe: true,
                    autoOpen: false,
                    height: 300,
                    width: 600,
                    position: 'top',
                    modal: true
                });

                 $("#reportSchedulerDialog").dialog({
                    //bgiframe: true,
                    autoOpen: false,
                    height: 380,
                    width: 680,
                    position: 'top',
                    modal: true
                });

            });

     
        </script>
        
        <style>
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
            a {font-family:Georgia, "Times New Roman", Times, serif;font-size:12px;cursor:pointer}
            a:link {color:#3300CC;}
            a:visited {color: #660066;}
            a:hover {text-decoration: none; color: #ff9900; font-weight:bold;}
            a:active {color: #ff0000;text-decoration: none}
        </style>
    </head>
    <body>

    <%

        String loguserId = String.valueOf(session.getAttribute("USERID"));
    //////////////////////////////////////////////////////////////////////////////////////////////.println.println("userId--->" + loguserId);
    Connection con =  ProgenConnection.getInstance().getConnection();
    Statement st = con.createStatement();
    ResultSet rs = st.executeQuery("select * from prg_message_board where USERID=" + loguserId);


    Statement st1 = con.createStatement();
    ResultSet rs1 = st1.executeQuery("select * from prg_ar_personalized_reports where prg_user_id=" + loguserId);

    %>

        <table style="height:600px;width:100%;max-height:100%"  cellpadding="0" cellspacing="0">
            <tr style="height:40px;width:100%;max-height:100%" >
                <td>
                    <table>
                        <tr>
                            <td valign="top" style="height:30px;width:8%;">
                                <img width="100%" height="100%"  title="pi " src="/reportRunner/images/pi_logo.gif"/>
                            </td>
                            <td valign="top" style="height:30px;" align="right">
                                Global Links (Home,logout)
                            </td>
                            <td valign="top" style="height:38px;width:8%;">
                                <img width="100%" height="100%"  title="Progen Business Solutions" src="/reportRunner/images/ProGen_Logo.gif"/>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
            <tr style="height:15px;width:100%;max-height:100%">
                <td>
                    <table width="100%" class="ui-corner-all">
                        <tr>
                            <td valign="top" style="height:10px;width:90%" >
                                <font face="verdana" size="3px">Report Name</font>
                            </td>
                            <td valign="top" style="height:10px;width:10%" align="right">
                                <a href="#" style="color:#369;font-weight:bold"> Help </a>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
            <tr style="width:100%;height:544px;max-height:100%">
                <td>
                    <table width="100%" class="ui-corner-all" height="100%" border="1px solid black" cellpadding="0" cellspacing="0">
                        <tr class="ui-corner-all">
                            <!-- Begin of links-->
                            <td valign="top" style="width:20%;" align="top" class="ui-corner-all">
                                <table style="width:99%;" class="ui-corner-all">
                                    <tr>
                                        <td>
                                            <div class="navsection">
                                                <div class="navtitle" id="displayfavlink" onclick="displayfavlink()" >&nbsp;<b>Favourite Links</b> </div>
                                                <table id="favlinkcont" cellpadding="2"   cellspacing="0" style="display:none" class="ui-corner-all">
                                                    <tbody>

                                                        <tr style="width:100%">
                                                            <td>
                                                                <iframe src="getFavouriteReports.do" scrolling="no" id="favFrame" frameborder="0"></iframe>
                                                            </td>

                                                        </tr>

                                                        <tr style="width:100%">
                                                            <!--<td style="width:50%">
                                                                <button id="Customize" class="ui-state-default ui-corner-all" style="width:122px;height:20px;color:black;">Customize</button>
                                                            </td>
                                                            <td style="width:50%">
                                                                <button id="Prioritize" class="ui-state-default ui-corner-all" style="width:122px;height:20px;color:black;">Prioritize</button>
                                                            </td>-->
                                                            <td align="center">
                                                                <button id="Customize" class="ui-state-default ui-corner-all" style="width:122px;height:20px;color:black;">Customize</button>
                                                            <!--<button id="Prioritize" class="ui-state-default ui-corner-all" style="width:122px;height:20px;color:black;">Prioritize</button></td>-->
                                                        </tr>
                                                    </tbody>
                                                </table>
                                            </div>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <div class="navsection">
                                                <div class="navtitle" id="displayWidgets" onclick="displayWidgets()" >&nbsp;<b>Widgets</b></div>
                                                <table id="Widgets" cellpadding="2"   cellspacing="0" style="display:none" align="center" class="ui-corner-all" width="100%">
                                                    <tbody align="center" style="width:100%">
                                                        <tr align="center" style="width:100%">
                                                            <td align="left" style="width:100%"> <input type="button" value="Compose Message" class="ui-state-default ui-corner-all" style="width:100%;height:20px;color:black" id="composeMessageDiv" ></td>
                                                        </tr>
                                                        <tr align="center">
                                                            <td align="right"> <input type="button" value="Customize Report" class="ui-state-default ui-corner-all" style="width:250px;height:20px;color:black" id="composeMessageDiv" ></td>
                                                        </tr>
                                                        <tr>
                                                            <td align="center"> <input type="button" value="Report Scheduler" class="ui-state-default ui-corner-all" style="width:100%;height:20px;color:black;" id="defineSchedulerDiv" onclick="defineScheduler()">
                                                        </tr>
                                                        <tr>
                                                            <td align="center"> <input type="button" value="Tracker" class="ui-state-default ui-corner-all" style="width:100%;height:20px;color:black" id="defineTrackerDiv" >
                                                        </tr>
                                                        <tr>
                                                            <td align="center"> <input type="button" value="Sticky Notes" onclick="addNew()"class="ui-state-default ui-corner-all" style="width:100%;height:20px;color:black" id="stickyId" >
                                                        </tr>
                                                    </tbody>
                                                </table>
                                            </div>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <div class="navsection">
                                                <div class="navtitle"  onclick="displayTopBottomRes()" >&nbsp;<b>Top 5/Bottom 5 Performance</b>
                                                </div>
                                            </div>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <div class="navsection">
                                                <div class="navtitle" id="snaplink" onclick="dispSnapShots()" >&nbsp;<b>Snapshot</b></div>
                                                <table id="snapshots" cellpadding="2"   cellspacing="0" style="display:none;border-width:thin" align="center" class="ui-corner-all" width="100%" border="0">
                                                    <tr><th width="50%">Name</th><th width="50%">View</th></tr>
                                                <%while(rs1.next())
                                                {
                                                    //String messsage = (rs.getString(4).length()<20)?rs.getString(4):rs.getString(4).substring(0,20)+"......";
                                                    %>


                                                    <tr>
                                                        <td><%=rs1.getString(4)%></td>
                                                        <td style="border-right:black" align="center"><a href="javascript:getReport('<%=rs1.getString(1)%>','<%=rs1.getString(5)%>')" style="text-decoration:none">View</a></td>

                                                    </tr>
                                                    <%}%>
                                                    <tr><td></td><td align="right"><a href="javascript:showPersonalisedReports()">more...</a></td></tr>
                                                    
                                                    <tr><td align="right"><input type="button" value="Store Snap Shot" onclick="addNew()"class="ui-state-default ui-corner-all" style="width:100%;height:20px;color:black" id="sanpShotDiv" ></td></tr>
                                                </table>
                                                
                                               
                                                
                                            </div>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <div class="navsection"> 
                                                <div class="navtitle" id="messageslink" onclick="dispMessages()" >&nbsp;<b>Messages</b></div>
                                                <table id="messages" cellpadding="2"   cellspacing="0" style="display:none;border-width:thin" align="center" class="ui-corner-all" width="100%" border="0">
                                                    <tr><th>Message</th><th>From</th></tr>
                                                <%while(rs.next())
                                                {
                                                    String messsage = (rs.getString(4).length()<20)?rs.getString(4):rs.getString(4).substring(0,20)+"......";
                                                    %>

                                                   
                                                    <tr>
                                                        <td style="border-right:black"><a href="javascript:void(0)" onclick="getMessage('<%=rs.getString(5)%>','<%=rs.getString(1)%>','<%=rs.getString(3)%>','<%=rs.getString(4)%>')" style="text-decoration:none"><%=messsage%></a></td>
                                                        <td><%=rs.getString(1)%></td>
                                                    </tr>
                                                    <%}%>
                                                    <tr><td></td><td align="right">more...</td></tr>
                                                </table>
                                            </div>
                                        </td>
                                    </tr>
                                </table>

                            </td><!--Endof links-->
                            <!-- Begin of Parameters/Graph Region-->
                            <td valign="top" style="width:80%" class="ui-corner-all">
                                <table style="width:99%" valign="top">
                                    <tr valign="top">
                                        <td valign="top">
                                            <div class="navsection" >
                                                <div class="navtitle1"  onclick="dispParameters()" >&nbsp;<b>Parameters Region</b></div>
                                                <table id="tabParameters"   style="display:none;height:80px">
                                                    <tr>
                                                        <td valign="top">
                                                            <p>This is Parameters Region</p>
                                                        </td>
                                                    </tr>
                                                </table>
                                            </div>
                                        </td>
                                    </tr>
                                    <tr valign="top">
                                        <td valign="top">
                                            <div class="navsection" >
                                                <div class="navtitle1"  onclick="dispGraphs()" >&nbsp;<b>Graphs Region</b></div>
                                                <table id="tabGraphs" style="display:none;height:250px">
                                                    <tr>
                                                        <td valign="top">
                                                            <p>This is Graphs Region</p>
                                                        </td>
                                                    </tr>
                                                </table>
                                            </div>
                                        </td>
                                    </tr>
                                    <tr valign="top">
                                        <td valign="top">
                                            <div class="navsection" >
                                                <div class="navtitle1"  onclick="dispTables()" >&nbsp;<b>Table Region</b></div>
                                                <table id="tabTable"   style="display:none;height:150px">
                                                    <tr>
                                                        <td valign="top">
                                                            <p>This is Table Region</p>
                                                        </td>
                                                    </tr>
                                                </table>
                                            </div>
                                        </td>
                                    </tr>

                                </table>
                            </td><!-- Endof Parameters/Graph Region-->
                        </tr>
                    </table>
                </td>
            </tr>
            <tr style="height:1px;width:100%;max-height:100%">
                <td style="height:1px;width:100%" bgcolor="#369">
                    <p style="color:#fff;font-size:10px;font-family:verdana;" align="center">Copyright © 2009-12 <a href="http://www.progenbusiness.com" style="color:red;font-weight:bold">Progen Business Solutions.</a> All Rights Reserved</p>
                </td>
            </tr>
        </table>
        <div id="fade" class="black_overlay"></div>
        <iframe id="msgframe" class="frame1" src="#" ></iframe>
        <iframe id="Scheduler" class="frame1" src="#"></iframe>
        <iframe id="tracker" class="frame1" src="#"></iframe>
        <iframe id="cstLinksFrame" class="frame1" src="#" ></iframe>
        <iframe id="prtLinksFrame" class="frame1" src="#" ></iframe>


        <div id="favLinksDialog" title="Favourite Links">
            <iframe src="getAllReports.do" scrolling="no" height="400px" width="560px" frameborder="0" id="favFrame"></iframe>
        </div>

        <div id="composeMessageDialog" title="Compose Message">
            <iframe src="pbTakeMailAddress1.jsp" height="380px" width="680px" frameborder="0" scrolling="no"></iframe>
        </div>

        <div id="reportSchedulerDialog" title="Report Scheduler">
           <iframe src="ReportScheduler/pbreportScheduler.jsp" height="380px" width="660px"></iframe>
        </div>

        <div id="replyMessageDialog" style="display:none" class="white_content">

        </div>

        <div id="snapShotDialog" title="Compose Message">
            <iframe src="PersonalisedReports/JSPS/pbPersonalisedReportRegister.jsp" height="380px" width="660px"></iframe>
        </div>
<script type="text/javascript">
            function displayfavlink(){ 
                $("#favlinkcont").toggle(500);
            }
            function displayWidgets(){
                $("#Widgets").toggle(500);
            }
            function dispParameters(){
                $("#tabParameters").toggle(500);
            }
            function dispGraphs(){
                $("#tabGraphs").toggle(500);
            }
            function dispTables(){
                $("#tabTable").toggle(500);
            }
            function dispMessages(){
                $("#messages").toggle(500);
            }
            function dispSnapShots(){
                $("#snapshots").toggle(500);
            }


 function getMessage(id,from,sub,message)
           {
               
               src = "pbTakeMailAddress.jsp?from="+from+"&subject="+sub+"&message="+message+"&sample=sample";
               alert(src)
               document.getElementById('replyMessageDialog').innerHTML = "<iframe src="+src+" class=frame1></iframe>";
               document.getElementById('fade').style.display='block';
               document.getElementById('replyMessageDialog').style.display='block';
           }

           function cancelMessage()
           {
               document.getElementById('fade').style.display='none';
               document.getElementById('replyMessageDialog').style.display='none';
           }



           function cancelFavLinks()
            {

                $('#favLinksDialog').dialog('close');
                document.getElementById('favFrame').src = document.getElementById('favFrame').src;
            }

            function cancelPerLinks()
            {
                $('#snapShotDialog').dialog('close');
            }

            function showPersonalisedReports()
            {
                document.getElementById('replyMessageDialog').innerHTML = "<iframe src=pbPersonalisedReportList.jsp width=700 height=330></iframe>";
                document.getElementById('fade').style.display='block';
                document.getElementById('replyMessageDialog').style.display='block';
            }

        </script>
    </body>
</html>
