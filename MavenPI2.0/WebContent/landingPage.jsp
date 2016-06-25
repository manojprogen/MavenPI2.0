<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<!--landingPage.jsp-->
<%-- 
    Document   : LandingPage
    Created on : Dec 4, 2015, 2:53:27 PM
    Author     : Prabal Pratap Singh
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"  import="com.progen.i18n.TranslaterHelper,java.util.Locale,com.progen.action.UserStatusHelper,utils.db.*,prg.db.Session,prg.db.PbReturnObject,java.sql.*,prg.db.PbDb,java.util.*,com.progen.reportdesigner.db.ReportTemplateDAO" %>
<%
    response.setHeader("Cache-Control", "no-store");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);
    if (session.getAttribute("USERID") == null || request.getSession(false) == null) {
        response.sendRedirect(request.getContextPath() + "/baseAction.do?param=logoutApplication");
    } else {
        Locale cL = null;
        cL = (Locale) session.getAttribute("UserLocaleFormat");
        String themeColor = "blue";
        if (session.getAttribute("theme") == null) {
            session.setAttribute("theme", themeColor);
        } else {
            themeColor = String.valueOf(session.getAttribute("theme"));
        }
        String compTitle = (String) session.getAttribute("compTitle");
        if (compTitle == null) {
            compTitle = "Progen Business Solution";
        }
        String userid = String.valueOf(session.getAttribute("USERID"));
        ServletContext context = getServletContext();
        String userType = null;
        boolean isQDEnableforUser = false;
        boolean isPowerAnalyserEnableforUser = false;
        HashMap<String, UserStatusHelper> statushelper;
        UserStatusHelper helper = new UserStatusHelper();
        if (context.getAttribute("helperclass") != null) {
            statushelper = (HashMap) context.getAttribute("helperclass");
            if (!statushelper.isEmpty()) {
                helper = statushelper.get(request.getSession(false).getId());
                if (helper != null) {
                    isQDEnableforUser = helper.getQueryStudio();
                    isPowerAnalyserEnableforUser = helper.getPowerAnalyser();
                    userType = helper.getUserType();
                }
            }
        }
        String layoutVar = null;
        layoutVar = (String) session.getAttribute("LayoutVarMap");
        if (layoutVar == null) {
            layoutVar = "Description";
        }
        String openReportTab = (String) session.getAttribute("ReportTabMap");
        if (openReportTab == null) {
            openReportTab = "_blank";
        }
        String favourit_Report_Tag = "";

        favourit_Report_Tag = session.getAttribute("isFavRepAsTag").toString().trim();
        String recentRepAsTag = "";
        recentRepAsTag = session.getAttribute("isRecentRepAsTag").toString().trim();
        String customReportFlag = "";
        customReportFlag = session.getAttribute("isRecentRepAsTag").toString().trim();
        String selectedLandingTag = "";
        selectedLandingTag = (String) session.getAttribute("currentSelectedTag");
        String contextPath = request.getContextPath();
        String custaomTagSequenceLanding = "";
        custaomTagSequenceLanding = (String) session.getAttribute("custaomTagSequenceLanding");
%>
<html>
    <head> 
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <jsp:include page="attachments.jsp"></jsp:include>

<!--        <script src="<%=contextPath%>/JS/waves.min.js" type="text/javascript"></script>-->

<!--        <script type="text/javascript" src="<%=contextPath%>/javascript/pbReportViewerJS.js" ></script>-->
<!--        <script type="text/javascript" src="<%=contextPath%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>-->
<!--        <script src="<%=contextPath%>/javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>-->
<!--        <script src="<%=contextPath%>/javascript/treeview/jquery.cookie.js" type="text/javascript"></script>-->
<!--        <script src="<%=contextPath%>/javascript/treeview/jquery.treeview.js" type="text/javascript"></script>-->
<!--        <script type="text/javascript" src="<%=contextPath%>/javascript/draggable/ui.core.js"></script>-->
<!--        <script type="text/javascript" src="<%=contextPath%>/javascript/draggable/ui.draggable.js"></script>-->
<!--        <script type="text/javascript" src="<%=contextPath%>/javascript/draggable/ui.droppable.js"></script>-->
<!--        <script type="text/javascript" src="<%=contextPath%>/javascript/ui.resizable.js"></script>-->
<!--        <script type="text/javascript" src="<%=contextPath%>/javascript/ui.accordion.js"></script>-->
<!--        <script type="text/javascript" src="<%=contextPath%>/javascript/ui.dialog.js"></script>-->
<!--        <script type="text/javascript" src="<%=contextPath%>/javascript/ui.sortable.js"></script>-->

<!--        <link rel="stylesheet" href="<%=contextPath%>/css/waves.min.css" type="text/css"/>-->
<!--        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=session.getAttribute("theme")%>/jquery-ui-1.7.3.custom.css" rel="stylesheet" />-->
<!--        <link type="text/css" href="<%=contextPath%>/stylesheets/ui.all.css" rel="stylesheet" />-->
<!--        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/treeviewstyle/jquery.treeview.css" />-->
        <!--        <link href="https://fonts.googleapis.com/css?family=Open+Sans:400,800,300" rel="stylesheet" type="text/css">-->

        <link type="text/css" href="<%=contextPath%>/css/landingPage.css" rel="stylesheet" >
    </head>
    <body>
        <form name="searchForm" action="landingPage.jsp" id="reportForm" method="POST"  >
            <button value="false" style="display: none" id="hidbtn">click</button>
            <!-- This is for header include     -->
            <div style="width:100%;height: 20%;">
                <jsp:include page="Headerfolder/headerPage.jsp"/>
            </div>
            <!-- This is for Report label content holder     -->
            <div class="my-container-fluid">     
                <div class="col-xs-12 col-sm-12 rmpd rmmg" style="height: 12px;z-index: 100;">
                    <div id="sequenceSettingDiv">
                        <i class="fa fa-cog " onclick="showConfigList();" title="<%=TranslaterHelper.getTranslatedInLocale("Change_Sequence_Setting", cL)%>"></i>
                    </div>
                    <ul id="sequenceConfigList" class="dropdown-menu" onmouseleave="javascript:($(this).hide())">
                        <li onclick="changeSeq();"><a href="javascript:void(0);" ><%=TranslaterHelper.getTranslatedInLocale("Change_Tag_Sequence", cL)%></a></li>
                        <li onclick="changeSeqOfRAssignment();"><a href="javascript:void(0);" ><%=TranslaterHelper.getTranslatedInLocale("Change_Report_Sequence", cL)%></a></li>
                        <li onclick="javascript:$('#changeFonts').dialog('open');"><a class="dropdown-toggle" data-toggle="dropdown" href="javascript:void(0);"><%=TranslaterHelper.getTranslatedInLocale("Change_Tag_Font", cL)%> </a></li>
                    </ul>
                </div>
                <!-- This is for Report Tags div     -->
                <div id="leftDiv" class="col-xs-3 col-sm-3 rmpd rmmg">
                    <div id="svgContain" class="myshadow  lpTagDiv">
                    </div>
                </div>
                <!-- This is for  Tags releted Report and slider    -->
                <div id="rightDiv" class="col-xs-9 col-sm-9  rmpd rmmg wrapper "  role="main">
                    <div id="gallery" class="col-xs-12 col-sm-12 rmpd ">
                        <div id="slider" class="col-xs-12 col-sm-12 rmpd ">
                            <div id="d1" class="col-xs-12 col-sm-12 rmpd"></div>
                            <div id="d2"  class="col-xs-12 col-sm-12 rmpd "></div>
                            <div id="d3"  class="col-xs-12 col-sm-12 rmpd"></div>
                        </div>
                    </div>
                </div>
                <div  class="col-xs-12 col-sm-12 rmpd rmmg" id="next_prev_div" style="text-align: center;"> 
                    <button id="prev" class="fa fa-angle-double-left fa-2x" onclick="javascript:void(0);" title="Go Previous" style="display: none;cursor: pointer;font-weight: bold;" title="Click for previous reports">
                    </button>
                    <button id="next"   onclick="javascript:void(0);"  title="Go Next"  class="fa fa-angle-double-right fa-2x" style="display: none;cursor: pointer;font-weight: bold;" >
                    </button>
                </div>  
                <div class="col-xs-12 col-sm-12 rmpd rmmg" style="text-align:right;">
                    <span style="color:#A3A5A8;margin-right: 4%;font-size:11px;">Powered By <i class="fa fa-copyright"></i> 2015 <%=compTitle%>. All Rights Reserved</span>
                </div>
            </div>
            <!--End of my-container-fliud-->
            <script type="text/javascript">
                var firstId = '';
                var firstType = '';
                var title = '';
                var tagIdS = [];
                var tagNameS = [];
                var fontSize = '12px';
                var selectedSlice = '';
                var filtagSname = '';
                var filtagLname = '';
                var filRepId = '';
                var filtagType = '';
                var hasTouch = '';
                var tagAssignIdForSeq = '';
                var tagShortNameForSeq = '';
                var nextCount = 1;
                var createdDate;
                var updatedDate;
                var layoutText = '<%=layoutVar%>';
                var dateString = "";
                var dateString1 = "";
                var currentSelectedTagId = "";

                function showConfigList() {
                    $("#sequenceConfigList").fadeToggle();
                    }
                function displayReports(tagId) {
                    currentSelectedTagId = tagId;
                    $.post('<%=request.getContextPath()%>/reportViewerAction.do?reportBy=currentSelectedTag&tagId=' + currentSelectedTagId,
                    function(data) {
                        if (data == "") {
                        }
                    });
                    C == 0;
                    disablePrev();
                    enableNext();
                    $(".svgContain").removeClass("myActive");
                    document.getElementById(tagId).className = "myActive svgContain";

                    //Commented by Faiz Ansari
                    //                var tileFontSize =($(window).width()*.02);
                    //                if(tileFontSize>=18){
                    //                    tileFontSize=18;
                    //                }
                    tileFontSize = 22;
                    //                    End!!!

                    if (title == 'Maps' || title == 'maps' || title == 'MAPS' || title == 'Interactive thematic maps and charts') {
                        var ctxPath = '<%=request.getContextPath()%>';
                        //                     var tileFontSize =($(window).width()*.02);
                        //                if(tileFontSize>=18){
                        //                    tileFontSize=18;
                        //                }
                        var subtileFontSize = ($(window).width() * .014);
                        if (subtileFontSize >= 11) {
                            subtileFontSize = 11;
                        }
                        $.ajax({
                            type: 'GET',
                            async: false,
                            cache: false,
                            timeout: 30000,
                            url: ctxPath + '/reportViewer.do?reportBy=getTagsBlocks&userId=' +<%=userid%> + '&tagId=0000',
                            success: function(json) {
                                var data = JSON.parse(json);
                                $("#d1").html("");
                                $("#d2").html("");
                                $("#d3").html("");
                                var keys = Object.keys(data);
                                if (keys.length > 9) {
                                    $("#prev").css('display', '');
                                    $("#next").css('display', '');
                                } else {
                                    $("#prev").css('display', 'none');
                                    $("#next").css('display', 'none');
                                }
                                for (var i = 0; i < keys.length; i++) {
                                    var div = "<div id='tile_M_" + (i + 1) + "'  onclick='getXtendUI(\"" + encodeURIComponent(keys[i]) + "\",\"" + encodeURIComponent(data[keys[i]]) + "\")' class='myshadow myTile'>";
                                    div += "<div style='height:60%;' ><span  style='color:#336699'><h1 align='center' style='padding-top:10%;color:black;font:" + tileFontSize + "px inherit' title='" + keys[i] + "'> " + keys[i] + " </h1></span>";
                                    div += "</div><div style='height:30%;' title='" + keys[i] + "'> <h4 align='center' style='color:black;font:" + subtileFontSize + "px Helvetica, sans-serif'> " + keys[i] + "</h4>";
                                    div += "</div></div></div> ";

                                    if (i > 9 && i <= 17) {
                                        var div1 = "<div id='tile_M_" + (i - 12) + "'   onclick='getXtendUI(\"" + encodeURIComponent(keys[i]) + "\",\"" + encodeURIComponent(data[keys[i]]) + "\")' class='myshadow myTile'>\n\
                                                      <div style='height:60%;' >\n\
                                                       <span  style='color:#336699'><h1 align='center' style='padding-top:10%;color:black;font:" + tileFontSize + "px  Helvetica, sans-serif' onclick='' title='" + keys[i] + "'> " + keys[i] + " </h1></span>\n\
                                                          </div><div style='height:50px;' title='" + keys[i] + "'>\n\
                                                           <h4 align='center' style='color:black;;font:" + subtileFontSize + "px Helvetica, sans-serif'> " + keys[i] + "</h4></div></div></div> ";
                                        $("#d2").append(div1);

                                    } else if (i > 17)
                                    {
                                        var div2 = "<div id='tile_M_" + (i - 24) + "'  onclick='getXtendUI(\"" + encodeURIComponent(keys[i]) + "\",\"" + encodeURIComponent(data[keys[i]]) + "\")' class='myshadow myTile'><div style='height:30%;' >\n\
                                                    <span href='#' style='color:#336699' ><h1 align='center' style='padding-top:10%;color:black;font:" + tileFontSize + "px  Helvetica, sans-serif' onclick='' title='" + keys[i] + "'> " + keys[i] + " </h1></span>\n\
                                                   </div><div style='height:30%;' title='" + keys[i] + "'>\n\
                                                   <h4 align='center' style='color:black;;font:" + subtileFontSize + "px  Helvetica, sans-serif'> " + keys[i] + "</h4></div></div> </div> ";
                                        if (div2 == 'null' || div2 == "") {

                                            $("#d1").append("<div class='col-xs-10 col-sm-10 rmpd' style='top: 40%; left: 10%;  box-shadow: 2px 2px 5px 4px rgb(209, 209, 209);'> <h3 style='margin-left: 20px;'>There is no  Reports are assigned to you.</h3></div>");
                                        } else {//alert("lse"+div2);

                                        }

                                    }
                                    else {
                                        $("#d1").append(div);
                                    }
                                }
                            }
                        });// end of ajax
                    }// end of else
                    else {//alert("else"+'+<%=userid%>+'+"     tagid="+tagId);
                        $("#d1").html("");
                        $("#d2").html("");
                        $("#d3").html("");
                        var ctxPath = '<%=request.getContextPath()%>';
                        $.ajax({
                            type: 'GET',
                            async: false,
                            cache: false,
                            timeout: 30000,
                            url: ctxPath + '/reportViewer.do?reportBy=getTagsBlocks&userId=' +<%=userid%> + '&tagId=' + tagId,
                            success: function(data) {
                                filtagSname = '';
                                filtagLname = '';
                                filRepId = '';
                                filtagType = '';

                                var jsonVar = eval('(' + data + ')')
                                var tagShortDesc = jsonVar.tagShortDesc;
                                var tagLongDesc = jsonVar.tagLongDesc;
                                var reportId = jsonVar.reportId;
                                var tagType = jsonVar.tagType;
                                var tagAssignId = jsonVar.tagAssignId;
                                filtagSname = tagShortDesc;
                                filtagLname = tagLongDesc;
                                filRepId = reportId;
                                filtagType = tagType;
                                tagAssignIdForSeq = tagAssignId;
                                tagShortNameForSeq = tagShortDesc;
                                $.ajax({
                                    type: 'GET',
                                    async: false,
                                    cache: false,
                                    timeout: 30000,
                                    url: ctxPath + '/reportViewerAction.do?reportBy=getReportDefinitionDate&userId=' +<%=userid%> + '&reportId=' + reportId,
                                    success: function(data) { //alert(data.replace("{","").replace("}",""))
                                        dateString = JSON.parse(data);
                                    }
                                });
                                if (reportId.length == 0) {
                                    $("#prev").css('display', 'none');
                                    $("#next").css('display', 'none');
                                    $("#d1").append("<div class='col-xs-10 col-sm-10 rmpd' style='top: 40%; left: 10%;  box-shadow: 2px 2px 5px 4px rgb(209, 209, 209);'> <h3 style='margin-left: 20px;'>There is no  Reports are assigned to you.</h3></div>");
                                } else {


                                    for (var i = 0; i < reportId.length; i++) {
                                        var div = ""; // alert("repId ="+reportId[i]);
                                        if (reportId.length > 9) {
                                            $("#prev").css('display', '');
                                            $("#next").css('display', '');
                                        } else {
                                            $("#prev").css('display', 'none');
                                            $("#next").css('display', 'none');
                                        }
                                        if (layoutText == 'Description')
                                        {
                                            var tileFontSize = ($(window).width() * .02);
                                            if (tileFontSize >= 18) {
                                                tileFontSize = 18;
                                            }
                                            var subtileFontSize = ($(window).width() * .014);
                                            if (subtileFontSize >= 11) {
                                                subtileFontSize = 11;
                                            }
                                            if (tagType[i] == "R") {
                                                div += "<div id='tile_" + tagType[i] + "_" + i + "'  onmouseover='myhover(this);' onmouseout='myout(this);' class='myTile myshadow  lpRepDiv' onclick='ToglFiltr(" + reportId[i] + ",\"" + tagShortDesc[i] + "\")' ><div style='height:60%' >";

                                            } else if (tagType[i] == 'O') {
                                                div += "<div id='tile_" + tagType[i] + "_" + i + "'  onmouseover='myhover(this);' onmouseout='myout(this);' class='myTile myshadow  lpRepDiv' onclick='openOneView(" + reportId[i] + ")'><div style='height:60%' >";
                                            } else if (tagType[i] == 'D') {
                                                div += "<div id='tile_" + tagType[i] + "_" + i + "'  onmouseover='myhover(this);' onmouseout='myout(this);' class='myTile myshadow  lpRepDiv' onclick='openDashboard(\"" + tagShortDesc[i] + "\"," + reportId[i] + ")'><div style='height:60%' >";
                                            } else {
                                                div += "<div id='tile_" + tagType[i] + "_" + i + "' onmouseover='myhover(this);' onmouseout='myout(this);' class='myTile myshadow  lpRepDiv'  ><div style='height:60%;' >";
                                            }
                                            div += "<h1 align='center' style='color:black;margin-top:10%;font:" + tileFontSize + "px  Helvetica, sans-serif;' title='" + tagShortDesc[i] + "'> " + tagShortDesc[i].truncate(40) + " </h1>";
                                            div += "</div><div style='height:20%;' title='" + tagLongDesc[i] + "'>\n\
                                                  <h4 align='center' style='color:#545454;font-size:" + subtileFontSize + "px ;'> " + tagLongDesc[i] + "</h4> </div></div></div> ";

                                            if (i > 8 && i <= 17) {
                                                var div1 = "";
                                                if (tagType[i] == "R") {
                                                    div1 += "<div id='tile_" + tagType[i] + "_" + i + "'  onmouseover='myhover(this);' onmouseout='myout(this);' class='myshadow myTile ' onclick='ToglFiltr(" + reportId[i] + ",\"" + tagShortDesc[i] + "\")' >   <div style='height:60%;' >";

                                                } else if (tagType[i] == 'O') {
                                                    div1 += "<div id='tile_" + tagType[i] + "_" + i + "'  onmouseover='myhover(this);' onmouseout='myout(this);' class='myshadow myTile' onclick='openOneView(" + reportId[i] + ")' ><div style='height:60%;' >";
                                                } else if (tagType[i] == 'D') {
                                                    div += "<div id='tile_" + tagType[i] + "_" + i + "' onmouseover='myhover(this);' onmouseout='myout(this);' class='myshadow myTile' onclick='openDashboard(\"" + tagShortDesc[i] + "\"," + reportId[i] + ")' ><div style='height:60%;' >";
                                                } else {
                                                    div1 += "<div id='tile_" + tagType[i] + "_" + i + "' onmouseover='myhover(this);' onmouseout='myout(this);' class='myshadow myTile'  ><div style='height:60%;' >";
                                                }
                                                div1 += "<h1 align='center' style='color:black;padding-top:10%;font:" + tileFontSize + "px Helvetica, sans-serif' onclick='' title='" + tagShortDesc[i] + "'> " + tagShortDesc[i] + " </h1>";

                                                div1 += "</div><div style='height:20%;' title='" + tagLongDesc[i] + "'>\n\
                                                           <h4 align='center' style='color:#545454;font:" + subtileFontSize + "px  Helvetica, sans-serif;color:black;' > " + tagLongDesc[i].truncate(40) + "</h4></div></div> </div> ";
                                                $("#d2").append(div1);
                                            } else if (i > 17) {
                                                var div2 = "";
                                                if (tagType[i] == "R") {
                                                    div2 += "<div id='tile_" + tagType[i] + "_" + i + "'  onmouseover='myhover(this);' onmouseout='myout(this);' class='myshadow myTile ' onclick='ToglFiltr(" + reportId[i] + ",\"" + tagShortDesc[i] + "\")'><div style='height:60%;' >";

                                                } else if (tagType[i] == 'O') {
                                                    div2 += "<div id='tile_" + tagType[i] + "_" + i + "' onmouseover='myhover(this);' onmouseout='myout(this);' class='myshadow myTile' onclick='openOneView(" + reportId[i] + ")' ><div style='height:60%;' >";
                                                } else if (tagType[i] == 'D') {
                                                    div += "<div id='tile_" + tagType[i] + "_" + i + "' onmouseover='myhover(this);' onmouseout='myout(this);' class='myshadow myTile' onclick='openDashboard(\"" + tagShortDesc[i] + "\"," + reportId[i] + ")' ><div style='height:60%;' >";
                                                } else {
                                                    div2 += "<div id='tile_" + tagType[i] + "_" + i + "' onmouseover='myhover(this);' onmouseout='myout(this);' class='myshadow myTile'  ><div style='height:60%;' >";
                                                }
                                                div2 += "<h1 align='center' style='color:black;padding-top:10%;font:" + tileFontSize + "px inherit;'  title='" + tagShortDesc[i] + "'> " + tagShortDesc[i].truncate(40) + " </h1>";

                                                div2 += "</div><div style='height:20%;' title='" + tagLongDesc[i] + "'>\n\
                                                              <h4 align='center' style='color:#545454;font:" + subtileFontSize + "px  inherit;'> " + tagLongDesc[i].truncate(40) + "</h4> </div></div> </div> ";
                                                $("#d3").append(div2);

                                            }
                                            // }else if(i>17)
                                            else {
                                                $("#d1").append(div);
                                            }
                                        } else {

                                            var tileFontSize = ($(window).width() * .02);
                                            if (tileFontSize >= 18) {
                                                tileFontSize = 18;
                                            }
                                            var subtileFontSize = ($(window).width() * .014);
                                            if (subtileFontSize >= 20) {
                                                subtileFontSize = 20;
                                            }
                                            var addSize = 3;
                                            var padding = 30;
                                            createdDate = "";
                                            updatedDate = "";
                                            var dateundefined = dateString[reportId[i]];
                                            //                                     alert(demo)
                                            if (dateundefined == 'undefined' || dateundefined === undefined)
                                            {
                                                createdDate = "";
                                                updatedDate = "";
                                            } else {
                                                var fields = dateundefined.split(/~/);
                                                createdDate = fields[0];
                                                updatedDate = fields[1];
                                                if (createdDate == 'null' || createdDate === undefined)
                                                    createdDate = "";
                                                if (updatedDate === null || updatedDate === undefined)
                                                    updatedDate = "";
                                            }
                                            if ((scHeight) * (0.225) <= 130 && (scHeight) * (0.225) > 115) {
                                                addSize = 0;
                                                padding = 25;
                                            }
                                            if ((scHeight) * (0.225) <= 115) {
                                                addSize = 4;
                                                padding = 5;
                                            }

                                            if (tagType[i] == "R") {
                                                div += "<div id='tile_" + tagType[i] + "_" + i + "' onmouseover='myhover(this);' onmouseout='myout(this);' class='myTile myshadow  lpRepDiv ' onclick='ToglFiltr(" + reportId[i] + ",\"" + tagShortDesc[i] + "\")' ><div style='height:30%' >";
                                            } else if (tagType[i] == 'O') {
                                                div += "<div id='tile_" + tagType[i] + "_" + i + "'  onmouseover='myhover(this);' onmouseout='myout(this);' class='myTile myshadow lpRepDiv ' onclick='openOneView(" + reportId[i] + ")'><div style='height:30%' >";
                                            } else if (tagType[i] == 'D') {
                                                div += "<div id='tile_" + tagType[i] + "_" + i + "'  onmouseover='myhover(this);' onmouseout='myout(this);' class='myTile myshadow lpRepDiv ' onclick='openDashboard(\"" + tagShortDesc[i] + "\"," + reportId[i] + ")'><div style='height:30%' >";
                                            } else {
                                                div += "<div id='tile_" + tagType[i] + "_" + i + "' onmouseover='myhover(this);' onmouseout='myout(this);' class='myTile myshadow lpRepDiv '  ><div style='height:30%;' >";
                                            }
                                            div += "<h1 class='var-h1' style='font-size:" + (subtileFontSize + addSize) + "px ;' title='" + tagShortDesc[i] + "'> " + tagShortDesc[i].truncate(40) + " </h1></div>";
                                            div += "<div  id='updatedDate" + i + "' title='" + tagLongDesc[i] + "' style='padding-top: " + padding + "px; ' class='var-date-div'>Last Updated :&nbsp&nbsp&nbsp&nbsp<span style=' font-weight: 300;font-size: inherit;font-family: inherit;line-height: 1;'>" + updatedDate + "</span> </div>";
                                            div += "<div id='createdDate" + i + "' title='" + tagLongDesc[i] + "' style='padding-top: 10px; ' class='var-date-div'>Created :&nbsp&nbsp&nbsp&nbsp<span style=' font-weight: 300;font-size: inherit;font-family: inherit;line-height: 1;'>" + createdDate + "</span> </div> </div></div> ";

                                            if (i > 8 && i <= 17) {
                                                var div1 = "";
                                                if (tagType[i] == "R") {
                                                    div1 += "<div id='tile_" + tagType[i] + "_" + i + "'  onmouseover='myhover(this);' onmouseout='myout(this);' class='myshadow myTile lpRepDiv  ' onclick='ToglFiltr(" + reportId[i] + ",\"" + tagShortDesc[i] + "\")' >   <div style='height:30%;' >";

                                                } else if (tagType[i] == 'O') {
                                                    div1 += "<div id='tile_" + tagType[i] + "_" + i + "'  onmouseover='myhover(this);' onmouseout='myout(this);' class='myshadow myTile lpRepDiv ' onclick='openOneView(" + reportId[i] + ")' ><div style='height:30%;' >";
                                                } else if (tagType[i] == 'D') {
                                                    div += "<div id='tile_" + tagType[i] + "_" + i + "' onmouseover='myhover(this);' onmouseout='myout(this);' class='myshadow myTile lpRepDiv ' onclick='openDashboard(\"" + tagShortDesc[i] + "\"," + reportId[i] + ")' ><div style='height:30%;' >";
                                                } else {
                                                    div1 += "<div id='tile_" + tagType[i] + "_" + i + "' onmouseover='myhover(this);' onmouseout='myout(this);' class='myshadow myTile lpRepDiv '  ><div style='height:30%;' >";
                                                }
                                                div1 += "<h1 class='var-h1' style='font-size:" + (subtileFontSize + 3) + "px ;' title='" + tagShortDesc[i] + "'> " + tagShortDesc[i].truncate(40) + " </h1></div>";
                                                div1 += "<div  id='updatedDate" + i + "' title='" + tagLongDesc[i] + "' style='padding-top: " + padding + "px;' class='var-date-div' >Last Updated :&nbsp&nbsp&nbsp&nbsp<span style=' font-weight: 300;font-size: inherit;font-family: inherit;line-height: 1;'>" + updatedDate + "</span> </div>";
                                                div1 += "<div id='createdDate" + i + "' title='" + tagLongDesc[i] + "' style='padding-top: 10px; '  class='var-date-div'>Created :&nbsp&nbsp&nbsp&nbsp<span style=' font-weight: 300;font-size: inherit;font-family: inherit;line-height: 1;'>" + createdDate + "</span> </div> </div></div> ";
                                                $("#d2").append(div1);
                                            } else if (i > 17)
                                            {
                                                var div2 = "";
                                                if (tagType[i] == "R") {
                                                    div2 += "<div id='tile_" + tagType[i] + "_" + i + "'  onmouseover='myhover(this);' onmouseout='myout(this);' class='myshadow myTile  lpRepDiv ' onclick='ToglFiltr(" + reportId[i] + ",\"" + tagShortDesc[i] + "\")'><div style='height:30%;' >";
                                                } else if (tagType[i] == 'O') {
                                                    div2 += "<div id='tile_" + tagType[i] + "_" + i + "' onmouseover='myhover(this);' onmouseout='myout(this);' class='myshadow myTile lpRepDiv ' onclick='openOneView(" + reportId[i] + ")' ><div style='height:30%;' >";
                                                } else if (tagType[i] == 'D') {
                                                    div += "<div id='tile_" + tagType[i] + "_" + i + "' onmouseover='myhover(this);' onmouseout='myout(this);' class='myshadow myTile lpRepDiv ' onclick='openDashboard(\"" + tagShortDesc[i] + "\"," + reportId[i] + ")' ><div style='height:30%;' >";
                                                } else {
                                                    div2 += "<div id='tile_" + tagType[i] + "_" + i + "' onmouseover='myhover(this);' onmouseout='myout(this);' class='myshadow myTile lpRepDiv '  ><div style='height:30%;' >";
                                                }
                                                div2 += "<h1  class='var-h1' style='font-size:" + (subtileFontSize + 3) + "px ;' title='" + tagShortDesc[i] + "'> " + tagShortDesc[i].truncate(40) + " </h1></div>";
                                                div2 += "<div  id='updatedDate" + i + "' title='" + tagLongDesc[i] + "' style='padding-top: " + padding + "px;'  class='var-date-div' >Last Updated :&nbsp&nbsp&nbsp&nbsp<span style=' font-weight: 300;font-size: inherit;font-family: inherit;line-height: 1;'>" + updatedDate + "</span> </div>";
                                                div2 += "<div id='createdDate" + i + "' title='" + tagLongDesc[i] + "' style='padding-top: 10px; '  class='var-date-div'>Created :&nbsp&nbsp&nbsp&nbsp<span style=' font-weight: 300;font-size: inherit;font-family: inherit;line-height: 1;'>" + createdDate + "</span> </div> </div></div> ";
                                                $("#d3").append(div2);

                                            }
                                            // }else if(i>17)
                                            else {
                                                $("#d1").append(div);
                                            }
                                        }//end of else for date  
                                        $(".myTile").css("height", (scHeight) * (0.234));// for handling the myTile height
                                    }
                                    $("#slider").css("left", "0px");
                                    C = 0;
                                }
                            }
                        });
                    }
                }

                String.prototype.truncate = function() {
                    var re = this.match(/^.{0,40}[\S]*/);
                    var l = re[0].length;
                    var re = re[0].replace(/\s$/, '');
                    if (l < this.length)
                        re = re + "...";
                    return re;
                }
                function createCustomTagHtml() {
                    var leftMenuDivHtml1 = "";
                <%if (favourit_Report_Tag.equalsIgnoreCase("true")) {%>
                        leftMenuDivHtml1 += "<div class='svgContain' id='favorite' onclick='displayFavReports(this.id)'  style='font-size:" + fontSize + ";'  title='Favorite Reports'  >Favorite Reports</div>";
                <%}%>
                <%if (recentRepAsTag.equalsIgnoreCase("true")) {%>
                        leftMenuDivHtml1 += "<div class='svgContain' id='recentalyReports' onclick='displayFavReports(this.id)'  style='font-size:" + fontSize + ";'  title='Most Recently Accessed'  >Most Recently Accessed</div>";
                <%}%>
                <%if (customReportFlag.equalsIgnoreCase("true")) {%>
                        leftMenuDivHtml1 += "<div class='svgContain' id='customReports' onclick='displayFavReports(this.id)'  style='font-size:" + fontSize + ";'  title='Custom Reports'  >Custom Reports</div>";
                <%}%>
                        return leftMenuDivHtml1;
                    }

                    function checkPie(fontSize1) {
                        fontSize = fontSize1;//alert(fontSize);
                        $("#svgContain").html("");
                        $.post('<%=request.getContextPath()%>/reportViewer.do?reportBy=getDataCall&userId=<%=userid%>',
                        function(data) {
                            var leftMenuDivHtml = "<div id='buzzRolDiv' style='margin: 10px 0px; overflow-y: auto; height: 98%;'>";
                            var data1 = JSON.parse(data);
                            var tagHtml = "";
                            var selectedVar = "";
                            selectedVar = '<%=selectedLandingTag%>';
                <%if (selectedLandingTag != null && !selectedLandingTag.isEmpty()) {%>
                            $("#" + selectedVar).addClass("myActive");
                <% }%>
                            for (var i = 0; i < data1.length; i++)
                            {
                                firstId = data1[0]["Id"];
                                firstType = data1[0]["Type"];
                                title = data1[0]["titleValue"];
                                tagIdS.push(data1[i]["Id"]);
                                tagNameS.push(data1[i]["Region"]);
                                if (data1[i]["Id"] == selectedVar) {
                                    tagHtml += " <div class='svgContain myActive' onclick='displayReports(this.id)' style='font-size:" + fontSize + ";'   title='" + data1[i]["Region"] + "' id='" + data1[i]["Id"] + "' >" + data1[i]["Region"] + "</div>";
                                } else {
                                    tagHtml += " <div class='svgContain' onclick='displayReports(this.id)' style='font-size:" + fontSize + ";'   title='" + data1[i]["Region"] + "' id='" + data1[i]["Id"] + "' >" + data1[i]["Region"] + "</div>";
                                }
                            }
                <%if (custaomTagSequenceLanding != null && custaomTagSequenceLanding.equalsIgnoreCase("top")) {%>
                            $("#svgContain").append(leftMenuDivHtml + createCustomTagHtml() + tagHtml + "</div>");

                <%} else {%>
                            $("#svgContain").append(leftMenuDivHtml + tagHtml + createCustomTagHtml() + "</div>");
                <%}%>
                            $("#svgContain").append(leftMenuDivHtml + "</div>");
                            scrollBar("#buzzRolDiv");
                <%if (selectedLandingTag != null && !selectedLandingTag.isEmpty()) {%>
                            if (selectedVar == 'favorite') {
                                displayFavReports(selectedVar);
                            } else if (selectedVar == 'recentalyReports') {
                                displayFavReports(selectedVar);
                            } else if (selectedVar == 'customReports') {
                                displayFavReports(selectedVar);
                            } else {
                                displayReports(selectedVar);
                            }
                <%} else if (favourit_Report_Tag.equalsIgnoreCase("true") && custaomTagSequenceLanding.equalsIgnoreCase("top")) {%>
                            displayFavReports('favorite');
                <%} else {%>
                            displayReports(firstId);
                <%}%>
                        });
                    }// end of checkPie()



                    function displayFavReports(tagId) {
                        setTimeout(function() {
                            currentSelectedTagId = tagId;
                            $("#prev").css('display', 'none');
                            $("#next").css('display', 'none');
                            $.post('<%=request.getContextPath()%>/reportViewerAction.do?reportBy=currentSelectedTag&tagId=' + currentSelectedTagId,
                            function(data) {
                                if (data == "") {
                                }
                            });
                            C == 0;
                            $("#slider").css("left", "0px");
                            C = 0;
                            disablePrev();
                            enableNext();
                            $(".svgContain").removeClass("myActive");
                            document.getElementById(tagId).className = "myActive svgContain";
                            $("#d1").html("");
                            $("#d2").html("");
                            $("#d3").html("");
                            if (tagId == 'favorite') {
                                $.ajax({
                                    type: "POST",
                                    async: false,
                                    //                            dataType: "json",
                                    //                            contentType: "application/json; charset=utf-8",
                                    data: {"userId": "<%=userid%>", "favouriteVar": "favourite"},
                                    url: '<%=request.getContextPath()%>/reportTemplateAction.do?templateParam=GetAllfavReports',
                                    success: function(data) {
                                        var data1 = JSON.parse(data);

                                        if (data1.length == 0) {
                                            $("#d1").append("<div class='col-xs-10 col-sm-10 rmpd' style='top: 40%; left: 10%; box-shadow: 2px 2px 5px 4px #d3d3d3;'> <h3 style='margin-left: 20px;'>No reports saved in My Favorites</h3></div>");
                                        } else {
                                            var repIds = [];

                                            for (var i = 0; i < data1.length; i++) {
                                                repIds.push(data1[i]["ID"]);
                                            }
                                            $.ajax({
                                                type: "POST",
                                                async: false,
                                                //                                      dataType: "json",
                                                //                                      contentType: "application/json; charset=utf-8",
                                                data: {"userId": "<%=userid%>", "reportId": repIds.toString()},
                                                url: '<%=request.getContextPath()%>/reportViewerAction.do?reportBy=getReportDefinitionDate',
                                                success: function(data) {//alert(data)
                                                    dateString1 = "";
                                                    dateString1 = JSON.parse(data);
                                                    for (var i = 0; i < data1.length; i++) {
                                                        favReps(data1, i);
                                                    }
                                                },
                                                error: function() {//alert("error")
                                                }
                                            });// end of ajax for date
                                            $("#slider").css("left", "0px");
                                            C = 0;
                                        }
                                    }
                                }); // end of ajax
                            } else if (tagId == 'recentalyReports') {
                                $.ajax({
                                    type: "POST",
                                    async: false,
                                    //                            dataType: "json",
                                    //                            contentType: "application/json; charset=utf-8",
                                    data: {"userId": "<%=userid%>"},
                                    url: '<%=request.getContextPath()%>/reportTemplateAction.do?templateParam=getRecentalyUsedUserReports',
                                    success: function(data) {//alert("recent       "+data)
                                        var data1 = JSON.parse(data);
                                        if (data.length <= 2) {
                                            $("#d1").append("<div class='col-xs-10 col-sm-10 rmpd' style='top: 40%; left: 10%;  box-shadow: 2px 2px 5px 4px rgb(209, 209, 209);'> <h3 style='margin-left: 20px;'>No reports saved in My Favorites</h3></div>");
                                        } else {
                                            var reportDesc = data1["REPORT_DESC"];
                                            var reportName = data1["REPORT_NAME"];
                                            var reportId = data1["REPORT_ID"];
                                            var createdDate = data1["CREATED_DATE"];
                                            var updatedDate = data1["UPDATED_DATE"];
                                            DisplayRecentlyUsedReport(reportDesc, reportName, reportId, createdDate, updatedDate);
                                        }
                                    }
                                });// end of ajax for recently used
                            }
                            else if (tagId == 'customReports') {

                                $.ajax({
                                    type: "POST",
                                    async: false,
                                    //                            dataType: "json",
                                    //                            contentType: "application/json; charset=utf-8",
                                    data: {"userId": "<%=userid%>", "customReports": "true"},
                                    url: '<%=request.getContextPath()%>/reportTemplateAction.do?templateParam=getRecentalyUsedUserReports',
                                    success: function(data) {//alert("custom       "+data)
                                        var data1 = JSON.parse(data);
                                        if (data.length <= 2) {
                                            $("#d1").append("<div class='col-xs-10 col-sm-10 rmpd' style='top: 40%; left: 10%;  box-shadow: 2px 2px 5px 4px #d3d3d3;'> <h3 style='margin-left: 20px;'>No reports saved in Custom Reports</h3></div>");
                                        } else {
                                            var reportDesc = data1["REPORT_DESC"];
                                            var reportName = data1["REPORT_NAME"];
                                            var reportId = data1["REPORT_ID"];
                                            var createdDate = data1["CREATED_DATE"];
                                            var updatedDate = data1["UPDATED_DATE"];
                                            DisplayRecentlyUsedReport(reportDesc, reportName, reportId, createdDate, updatedDate);
                                        }
                                    }
                                });// end of ajax for custom reports
                            }
                            else {
                                alert("Please Select a valid Tag..");
                            }
                        }, 1000);
                    }//end of display favourite report method


                    function DisplayRecentlyUsedReport(reportDes, reportNam, repotId, createdDat, updatedDat) {
                        $("#slider").css("left", "0px");
                        C = 0;
                        var reportDesc = [];
                        reportDesc = reportDes.toString().split(",");
                        var reportName = [];
                        reportName = reportNam.toString().split(",");
                        var reportId = [];
                        reportId = repotId.toString().split(",");
                        var createdDate1 = [];
                        createdDate1 = createdDat.toString().split(",");
                        var updatedDate = [];
                        updatedDate = updatedDat.toString().split(",");
                        var tileFontSize = ($(window).width() * .02);
                        if (tileFontSize >= 18) {
                            tileFontSize = 18;
                        }
                        var subtileFontSize = ($(window).width() * .014);
                        if (subtileFontSize >= 20) {
                            subtileFontSize = 20;
                        }
                        var addSize = 3;
                        var padding = 30;
                        if ((scHeight) * (0.225) <= 130 && (scHeight) * (0.225) > 115) {
                            addSize = 0;
                            padding = 25;
                        }
                        if ((scHeight) * (0.225) <= 115) {
                            addSize = 4;
                            padding = 5;
                        }
                        if (repotId.length > 9) {
                            $("#prev").css('display', '');
                            $("#next").css('display', '');
                        } else {
                            $("#prev").css('display', 'none');
                            $("#next").css('display', 'none');
                        }

                        for (var i = 0; i < reportId.length; i++) {
                            var div = "";
                            var created = "";
                            var updated = "";
                            created = createdDate1[i].toString();
                            updated = updatedDate[i].toString();
                            if (created === undefined)
                                created = "";
                            if (updated === null || updated === undefined)
                                updated = "";
                            div += "<div id='tile_" + i + "' onmouseover='myhover(this);' onmouseout='myout(this);' class='myTile myshadow  lpRepDiv ' onclick='ToglFiltr(" + reportId[i] + ",\"" + reportName[i] + "\")' ><div style='height:30%' >";
                            div += "<h1 class='var-h1' style='font-size:" + (subtileFontSize + addSize) + "px ;' title='" + reportName[i] + "'> " + reportName[i].toString().truncate(40) + " </h1></div>";
                            div += "<div  id='updatedDate" + i + "' title='" + reportName[i] + "' style='padding-top: " + padding + "px; ' class='var-date-div'>Last Updated :&nbsp&nbsp&nbsp&nbsp<span style=' font-weight: 300;font-size: inherit;font-family: inherit;line-height: 1;'>" + updated + "</span> </div>";
                            div += "<div id='createdDate" + i + "' title='" + reportName[i] + "' style='padding-top: 10px; ' class='var-date-div'>Created :&nbsp&nbsp&nbsp&nbsp<span style=' font-weight: 300;font-size: inherit;font-family: inherit;line-height: 1;'>" + created + "</span> </div> </div></div> ";
                            if (i > 8 && i <= 17) {
                                var div1 = "";
                                div1 += "<div id='tile_" + i + "' onmouseover='myhover(this);' onmouseout='myout(this);' class='myTile myshadow  lpRepDiv ' onclick='ToglFiltr(" + reportId[i] + ",\"" + reportName[i] + "\")' ><div style='height:30%' >";
                                div1 += "<h1 class='var-h1' style='font-size:" + (subtileFontSize + addSize) + "px ;' title='" + reportName[i] + "'> " + reportName[i].toString().truncate(40) + " </h1></div>";
                                div1 += "<div  id='updatedDate" + i + "' title='" + reportName[i] + "' style='padding-top: " + padding + "px; ' class='var-date-div'>Last Updated :&nbsp&nbsp&nbsp&nbsp<span style=' font-weight: 300;font-size: inherit;font-family: inherit;line-height: 1;'>" + updated + "</span> </div>";
                                div1 += "<div id='createdDate" + i + "' title='" + reportName[i] + "' style='padding-top: 10px; ' class='var-date-div'>Created :&nbsp&nbsp&nbsp&nbsp<span style=' font-weight: 300;font-size: inherit;font-family: inherit;line-height: 1;'>" + created + "</span> </div> </div></div> ";
                                $("#d2").append(div1);
                            } else if (i > 17)
                            {
                                var div2 = "";
                                div2 += "<div id='tile_" + i + "' onmouseover='myhover(this);' onmouseout='myout(this);' class='myTile myshadow  lpRepDiv ' onclick='ToglFiltr(" + reportId[i] + ",\"" + reportName[i] + "\")' ><div style='height:30%' >";
                                div2 += "<h1 class='var-h1' style='font-size:" + (subtileFontSize + addSize) + "px ;' title='" + reportName[i] + "'> " + reportName[i].toString().truncate(40) + " </h1></div>";
                                div2 += "<div  id='updatedDate" + i + "' title='" + reportName[i] + "' style='padding-top: " + padding + "px; ' class='var-date-div'>Last Updated :&nbsp&nbsp&nbsp&nbsp<span style=' font-weight: 300;font-size: inherit;font-family: inherit;line-height: 1;'>" + updated + "</span> </div>";
                                div2 += "<div id='createdDate" + i + "' title='" + reportName[i] + "' style='padding-top: 10px; ' class='var-date-div'>Created :&nbsp&nbsp&nbsp&nbsp<span style=' font-weight: 300;font-size: inherit;font-family: inherit;line-height: 1;'>" + created + "</span> </div> </div></div> ";
                                $("#d3").append(div2);
                            }
                            else {
                                $("#d1").append(div);
                            }
                            $(".myTile").css("height", (scHeight) * (0.234));// for handling the myTile height
                        }
                        $("#slider").css("left", "0px");
                        C = 0;
                    }


                    function favReps(data1, i) {
                        $("#slider").css("left", "0px");
                        C = 0;
                        var reportId = data1[i]["ID"];
                        var reportName = data1[i]["NAME"];
                        var div = "";
                        if (data1.length > 9) {
                            $("#prev").css('display', '');
                            $("#next").css('display', '');
                        } else {
                            $("#prev").css('display', 'none');
                            $("#next").css('display', 'none');
                        }
                        createdDate = "";
                        updatedDate = "";
                        if ((dateString1[reportId]).split(/~/) == undefined)
                            createdDate = dateString1[reportId].replace("~", "");
                        else {
                            var fields = (dateString1[reportId]).split(/~/);
                            createdDate = fields[0];
                            updatedDate = fields[1];
                        }
                        if (createdDate == 'null' || createdDate === undefined)
                            createdDate = "";
                        if (updatedDate === null || updatedDate === undefined)
                            updatedDate = "";
                        tileFontSize = 22;
                        if (tileFontSize >= 18) {
                            tileFontSize = 18;
                        }
                        var subtileFontSize = ($(window).width() * .014);
                        if (subtileFontSize >= 20) {
                            subtileFontSize = 20;
                        }
                        var addSize = 3;
                        var padding = 30;
                        if ((scHeight) * (0.225) <= 130 && (scHeight) * (0.225) > 115) {
                            addSize = 0;
                            padding = 25;
                        }
                        if ((scHeight) * (0.225) <= 115) {
                            addSize = 4;
                            padding = 5;
                        }
                        div += "<div id='tile_" + i + "' onmouseover='myhover(this);' onmouseout='myout(this);' class='myTile myshadow  lpRepDiv ' onclick='ToglFiltr(" + reportId + ",\"" + reportName + "\")' ><div style='height:30%' >";
                        div += "<h1 class='var-h1' style='font-size:" + (subtileFontSize + addSize) + "px ;' title='" + reportName + "'> " + reportName.truncate(40) + " </h1></div>";
                        div += "<div  id='updatedDate" + i + "' title='" + reportName + "' style='padding-top: " + padding + "px; ' class='var-date-div'>Last Updated :&nbsp&nbsp&nbsp&nbsp<span style=' font-weight: 300;font-size: inherit;font-family: inherit;line-height: 1;'>" + updatedDate + "</span> </div>";
                        div += "<div id='createdDate" + i + "' title='" + reportName + "' style='padding-top: 10px; ' class='var-date-div'>Created :&nbsp&nbsp&nbsp&nbsp<span style=' font-weight: 300;font-size: inherit;font-family: inherit;line-height: 1;'>" + createdDate + "</span> </div> </div></div> ";

                        if (i > 8 && i <= 17) {
                            var div1 = "";
                            div1 += "<div id='tile_" + i + "' onmouseover='myhover(this);' onmouseout='myout(this);' class='myTile myshadow  lpRepDiv ' onclick='ToglFiltr(" + reportId + ",\"" + reportName + "\")' ><div style='height:30%' >";
                            div1 += "<h1 class='var-h1' style='font-size:" + (subtileFontSize + addSize) + "px ;' title='" + reportName + "'> " + reportName.truncate(40) + " </h1></div>";
                            div1 += "<div  id='updatedDate" + i + "' title='" + reportName + "' style='padding-top: " + padding + "px; ' class='var-date-div'>Last Updated :&nbsp&nbsp&nbsp&nbsp<span style=' font-weight: 300;font-size: inherit;font-family: inherit;line-height: 1;'>" + updatedDate + "</span> </div>";
                            div1 += "<div id='createdDate" + i + "' title='" + reportName + "' style='padding-top: 10px; ' class='var-date-div'>Created :&nbsp&nbsp&nbsp&nbsp<span style=' font-weight: 300;font-size: inherit;font-family: inherit;line-height: 1;'>" + createdDate + "</span> </div> </div></div> ";
                            $("#d2").append(div1);
                        } else if (i > 17)
                        {
                            var div2 = "";
                            div2 += "<div id='tile_" + i + "' onmouseover='myhover(this);' onmouseout='myout(this);' class='myTile myshadow  lpRepDiv ' onclick='ToglFiltr(" + reportId + ",\"" + reportName + "\")' ><div style='height:30%' >";
                            div2 += "<h1 class='var-h1' style='font-size:" + (subtileFontSize + addSize) + "px ;' title='" + reportName + "'> " + reportName.truncate(40) + " </h1></div>";
                            div2 += "<div  id='updatedDate" + i + "' title='" + reportName + "' style='padding-top: " + padding + "px; ' class='var-date-div'>Last Updated :&nbsp&nbsp&nbsp&nbsp<span style=' font-weight: 300;font-size: inherit;font-family: inherit;line-height: 1;'>" + updatedDate + "</span> </div>";
                            div2 += "<div id='createdDate" + i + "' title='" + reportName + "' style='padding-top: 10px; ' class='var-date-div'>Created :&nbsp&nbsp&nbsp&nbsp<span style=' font-weight: 300;font-size: inherit;font-family: inherit;line-height: 1;'>" + createdDate + "</span> </div> </div></div> ";
                            $("#d3").append(div2);

                        }
                        else {
                            $("#d1").append(div);
                        }
                        $(".myTile").css("height", (scHeight) * (0.234));// for handling the myTile height
                    }


                    function myhover(obj) {
                        obj.style.backgroundColor = "#8BC34A";
                        obj.style.color = "white";
                    }

                    function myout(obj) {
                        obj.style.backgroundColor = "white";
                        obj.style.color = "inherit";
                    }

                    jQuery(document).ready(function($) {
                        hasTouch = /android|iphone|ipad/i.test(navigator.userAgent.toLowerCase());

                        var wid = ($("#gallery").width());
                        $("#d1").css('width', wid);
                        $("#d2").css('width', wid);
                        $("#d3").css('width', wid);
                        $('.smallDiv ').hover(function() {
                            $(this).stop().animate({
                                opacity: 1
                            }, 200);
                        }, function() {
                            $(this).stop().animate({
                                opacity: 0.3
                            }, 200);
                        });

                        $("#AddMoreFiltersDiv").dialog({
                            autoOpen: false,
                            height: 550,
                            width: 750,
                            position: 'justify',
                            modal: true,
                            resizable: true
                        });
                    });

                    var $gal = $('#gallery'),
                    $sli = $('#slider'),
                    $box = $('div', $sli),
                    W = $gal.width(), // 500

                    N = $box.length, // 3
                    C = 0;
                    $sli.width(W * N);
                    var wid1 = $("#rightDiv").width();
                    disablePrev();

                    $('#prev').click(function() {
                        if (C == 0) {
                            var d2html = $("#d2").html();
                            if (d2html != "") {
                                C = (this.id == 'prev' ? --C : --C) < 0 ? N - 1 : C % N;
                                $sli.stop().animate({left: -C * wid2}, 800);
                                enablePrev();
                            } else {
                                disableNext();
                                $sli.stop().animate({left: -C * wid2}, 800);
                            }

                        } else if (C == 1) {
                            var d3html = $("#d1").html();
                            if (d3html != "") {
                                C = (this.id == 'prev' ? --C : --C) < 0 ? N - 1 : C % N;
                                $sli.stop().animate({left: -C * wid2}, 800);
                            } else {
                                enableNext();
                                $sli.stop().animate({left: -C * wid2}, 800);
                            }
                            disablePrev();
                            enableNext();
                        } else if (C == 2) {
                            C = (this.id == 'prev' ? --C : --C) < 0 ? N - 1 : C % N;
                            $sli.stop().animate({left: -C * wid2}, 800);
                            enableNext();
                        }//alert(C)
                    });
                    $('#next').click(function() {
                        if (C == 0) {
                            var d2html = $("#d2").html();
                            if (d2html != "") {
                                //                                      C = (this.id=='next' ? ++C : --C) <0 ? N-1 : C%N;
                                C++;
                                $sli.stop().animate({left: -((C * wid2) + 15)}, 800);
                            } else {
                                disableNext();
                            }
                            if ($("#d3").html() == "") {
                                disableNext();
                            }
                            enablePrev();
                        } else if (C == 1) {
                            var d3html = $("#d3").html();
                            if (d3html != "") {
                                //                                   C = (this.id=='next' ? ++C : --C) <0 ? N-1 : C%N;
                                C++;
                                $sli.stop().animate({left: -((C * wid2) + 15)}, 800);
                            } else {
                                disableNext();
                            }
                            disableNext();
                        } else if (C == 2) {
                            //                                  C = (this.id=='next' ? ++C : --C) <0 ? N-1 : C%N;
                            C++;
                            $sli.stop().animate({left: -((C * wid2) + 15)}, 800);
                            enableNext();
                            disablePrev();
                        }
                    });


                    function disableNext() {
                        $("#next").attr("disabled", true);
                        $("#next").css("color", "gray");
                    }
                    function enableNext() {
                        //            setTimeout(function(){
                        $("#next").attr("disabled", false);
                        $("#next").css("color", "black");
                        //            },1000); 
                    }
                    function disablePrev() {
                        $("#prev").attr("disabled", true);
                        $("#prev").css("color", "gray");
                    }
                    function enablePrev() {
                        //             setTimeout(function(){
                        $("#prev").attr("disabled", false);
                        $("#prev").css("color", "black");
                        //          },1000); 
                    }
                    function ToglFiltr(repId, repName) {
                        if ($("#hidbtn").val() == "false") {
                            if (hasTouch) {
                                window.location.href = ('<%=request.getContextPath()%>/reportViewer.do?reportBy=viewReport&REPORTID=' + repId + '&action=open');
                            } else {
                                window.open('<%=request.getContextPath()%>/reportViewer.do?reportBy=viewReport&REPORTID=' + repId + '&action=open', '<%=openReportTab%>');//old value is _blank for opening in new tab
                            }
                        } else { // alert(repId+"          "+repName);
                            applyNewFilters(repId, repName);
                        }
                    }

                    function openDashboard(dashName, dashID) {
                        if (hasTouch) {
                            window.location.href = ('<%=request.getContextPath()%>/dashboardViewer.do?reportBy=viewDashboard&REPORTID=' + dashID + '&pagename=' + dashName + '&editDbrd=false');
                        } else {
                            window.open('<%=request.getContextPath()%>/dashboardViewer.do?reportBy=viewDashboard&REPORTID=' + dashID + '&pagename=' + dashName + '&editDbrd=false', '_blank');
                        }
                    }

                    function changeFont(fontVal) {
                        if (fontVal == 'Small') {
                            fontSize = '12px ';
                            fchange = 1;
                        } else if (fontVal == 'Medium') {
                            fontSize = '15px ';
                            fchange = 1;
                        } else {
                            fontSize = '18px ';
                            fchange = 1;
                        }

                        var fSize = fontSize.toString().replace("px Helvetica ", "");
                        $.post('<%=request.getContextPath()%>/reportViewer.do?reportBy=updateFontSizeChangeOfTag&fontSizeTag=' + fSize,
                        function(data) {
                            if (data == "") {
                                alert("Font has been changed successfuly");
                            }
                            else {
                                alert("Invalid entries");
                            }
                            $('#changeFonts').dialog('close');
                        });

                        $("#svgContain").html('');
                        tagIdS = [];
                        tagNameS = [];
                        checkPie(fontSize)
                    }
            </script>
            <div id="AddMoreFiltersDiv" Style="display:none;overflow-y: hidden" title="Add More Filters ">
                <iframe  id="addmoreFiltersFrame" name='addMoreFiltersFrame' width="100%" height="100%" frameborder="0"   src='about:blank'></iframe>
            </div>
            <!-- End of code by Amar on Dec 2014 -->
        </form>
        <script type="text/javascript">
            var tId;
            var tName;
            var seqId;

            function changeSeq() {
                var htmlVar2 = "";
                tId = new Array();
                tName = new Array();
                seqId = new Array();
                for (var i = 0; i < tagIdS.length; i++) {
                    htmlVar2 += "<li id='" + tagIdS[i] + "' class='btn-custom2' style='margin-bottom: 1px; height: 20px; font-size: 12px; padding: 3px;'>" + tagNameS[i] + "</li>";
                }
                $("#addDynamic").html(htmlVar2);
                //                $("#changeSequence1").css("background-color","#d1d1d1");
                $("#changeSequence1").dialog('open');
                $("#sequenceConfigList").css("display", "none");
            }
            function changeSeqOfRAssignment() {
                var htmlVar2 = "";
                $("#sequenceConfigList").css("display", "none");
                for (var i = 0; i < tagAssignIdForSeq.length; i++) {
                    htmlVar2 += "<li id='" + tagAssignIdForSeq[i] + "' class='btn-custom2' style=' margin-bottom: 1px; height: 20px; font-size: 12px; padding: 3px;'>" + tagShortNameForSeq[i] + "</li>";
                }
                $("#addDynamicSecond").html(htmlVar2);
                //                $("#changeSequence1OfReportAssignment").css("background-color","#fff");
                $("#changeSequence1OfReportAssignment").dialog('open');
            }

            function initDialog1() {


                if (checkBrowser() == "ie") {
                    $("#changeSequence1").dialog({
                        autoOpen: false,
                        height: 400,
                        width: 300,
                        position: 'justify'
                        //modal: true
                    });
                    $("#changeSequence1OfReportAssignment").dialog({
                        autoOpen: false,
                        height: 430,
                        width: 330,
                        position: 'justify'
                    });
                }
                else {
                    //                    $("#changeSequence1,#changeSequence1OfReportAssignment,#changeFonts").css("background-color","#ececec");
                    $("#changeSequence1,#changeSequence1OfReportAssignment,#changeFonts").dialog({
                        autoOpen: false,
                        modal: true,
                        resizable: false,
                        minHeight: 0,
                        show: {
                            effect: "explode",
                            duration: 500
                        },
                        hide: {
                            effect: "explode",
                            duration: 500
                        }
                    });
                }
                $('ul.sortable-list').sortable();
            }
            //            $("#changeSequence1OfReportAssignment").css("background-color","#fff");
            function changeSequence1Tab() {
                var itemStr = getItems('#wrapperSequence');
                var s = itemStr.toString().split(",");
                var tpid = [];
                var tpseqid = [];
                for (var i = 0; i < s.length; i++) {
                    tpid.push(s[i]);
                    tpseqid.push(i);
                }
                $.post('<%=request.getContextPath()%>/reportViewer.do?reportBy=updateAccordingSequence&tagIdforUpd=' + tpid + '&tagSequenceIdForUpd=' + tpseqid, $("#sequenceForTag").serialize(),
                function(data) {
                    alert("Sequences have been changed successfuly");
                    if (data == "") {
                        $("#changeSequence1").dialog('close');
                        window.location.href = window.location.href;
                    }
                    else {
                        alert("Invalid entries");
                    }
                });
            }
            function getItems(container)
            {
                var columns = [];
                $(container + ' ul.column').each(function() {
                    columns.push($(this).sortable('toArray').join(','));
                });
                return columns.join('|');
            }
            function changeSequence1ReportAssignment() {
                var itemStr = getItemsSecond('#wrapperSequenceSecond');
                var s = itemStr.toString().split(",");
                var tagAssignIdS = [];
                var tagAssignSeqId = [];
                for (var i = 0; i < s.length; i++) {
                    tagAssignIdS.push(s[i]);
                    tagAssignSeqId.push(i);
                }
                $.post('<%=request.getContextPath()%>/reportViewer.do?reportBy=updateTagReportAssignmentSequence&currentSelectedTagId=' + currentSelectedTagId + '&tagAssignIdforUpd=' + tagAssignIdS + '&tagAssginSequenceIdForUpd=' + tagAssignSeqId, $("#sequenceForTagSecond").serialize(),
                function(data) {
                    alert("Sequences have been updated successfuly");
                    if (data == "") {
                        $("#changeSequence1OfReportAssignment").dialog('close');
                        window.location.href = window.location.href;
                    }
                    else {
                        alert("Invalid entries");
                    }
                });
            }
            function getItemsSecond(container)
            {
                var columns = [];
                $(container + ' ul.columnSecond').each(function() {
                    columns.push($(this).sortable('toArray').join(','));
                });
                return columns.join('|');
            }



            window.onload = function() {
                var wwidth = $(window).width();

                if (wwidth < 768)
                    checkPie('12px');
                else
                    checkPie('18px');

                initDialog1();
            };

            function openDashboard(dashName, dashID) {
                if (hasTouch) {
                    window.location.href = ('<%=request.getContextPath()%>/dashboardViewer.do?reportBy=viewDashboard&REPORTID=' + dashID + '&pagename=' + dashName + '&editDbrd=false');
                } else {
                    window.open('<%=request.getContextPath()%>/dashboardViewer.do?reportBy=viewDashboard&REPORTID=' + dashID + '&pagename=' + dashName + '&editDbrd=false', '_blank');
                }
            }

            function mediateFilterEnable() {
                if ($("#hidbtn").val() == "false") {
                    $('#Tbtn').removeClass('tick').addClass('tick1');

                    $("#hidbtn").attr('value', 'true');
                } else {
                    $('#Tbtn').removeClass('tick1').addClass('tick');

                    $("#hidbtn").attr('value', 'false');
                }
            }
            function goPaths(path) {//alert(path);
                var modulecode = path.replace("home.jsp#", "");
                //modified by anitha for business roles tab
                var ctxpath = '<%=request.getContextPath()%>';
                $.ajax({
                    url: ctxpath + "/portalTemplateAction.do?paramportal=setModuleNameInSession&modulecode=" + modulecode,
                    success: function(data) {
                        parent.closeStart();
                        document.forms.searchForm.action = path;
                        document.forms.searchForm.submit();
                    }
                });
            }
            function valid(form) {
                var input = 0;
                input = document.myform.data.value;
                window.open('<%=request.getContextPath()%>/Search.jsp');
            }
            function openOneView(oneViewIdValue) {
                var oneviewname = '';
                $.ajax({
                    url: 'oneViewAction.do?templateParam2=getonename&oneViewIdValue=' + oneViewIdValue,
                    success: function(data) {
                        oneviewname = data;
                        if (hasTouch) {
                            window.location.href = ('srchQueryAction.do?srchParam=oneViewBy&homeFlag=true&oneViewId=' + oneViewIdValue + '&oneviewname=' + oneviewname);
                        }
                        else {
                            window.open('srchQueryAction.do?srchParam=oneViewBy&homeFlag=true&oneViewId=' + oneViewIdValue + '&oneviewname=' + oneviewname, '_blank');
                        }
                    }
                });
            }


        </script>

        <div id="changeSequence1" style="display:none;" title="<%=TranslaterHelper.getTranslatedInLocale("Change_Sequence", cL)%>">
            <form action="javascript:void(0)" name="sequenceForTag" id="sequenceForTag" method="post" onsubmit="return changeSequence1Tab()">
                <div id="wrapperSequence" class="setHeightScrollable"  >
                    <ul class="sortable-list column" id="addDynamic" ></ul>
                </div >
                <div style="width:20%;margin-left: auto;margin-right: auto;margin-top:5%;">
                    <input class="prgBtn" style="width:auto" type="submit" value="<%=TranslaterHelper.getTranslatedInLocale("save", cL)%>" >
                </div>
            </form>
        </div>

        <div id="changeSequence1OfReportAssignment" style="display:none;background-color: white;" title="<%=TranslaterHelper.getTranslatedInLocale("Change_Report_Block_Sequence", cL)%>">
            <form action="javascript:void(0)" name="sequenceForTagSecond" id="sequenceForTagSecond" method="post" onsubmit="return changeSequence1ReportAssignment()">
                <div id="wrapperSequenceSecond" class="setHeightScrollable">
                    <ul class="sortable-list columnSecond" id="addDynamicSecond">

                    </ul>
                </div>
                <div class="secgh" style="width:20%;margin-left: auto;margin-right: auto;margin-top:5%;" >
                    <input class="prgBtn" type="submit" value="<%=TranslaterHelper.getTranslatedInLocale("save", cL)%>" >
                </div>
            </form>
        </div> 
        <div id="changeFonts" title="Tag Font Size">
            <button class="prgBtn" onclick="changeFont('Small')"><%=TranslaterHelper.getTranslatedInLocale("Small", cL)%></button>
            <button class="prgBtn" onclick="changeFont('Medium')"><%=TranslaterHelper.getTranslatedInLocale("Medium", cL)%></button>
            <button class="prgBtn" onclick="changeFont('Large')"><%=TranslaterHelper.getTranslatedInLocale("Large", cL)%></button>
        </div>
        <script type="text/javascript">
            var wid2 = '';
            var C = 0;
            var scHeigh;
            // edit by shivam          
            $(document).ready(function() {
                //Added By Faiz Ansari
                scrollBar("body");
                $("#hdFavRep4").addClass("udLine");
                scHeight = $(window).height();
                $("body").height((scHeight) * .85);
                $("body").width($(window).width());
                var screenWid = $(window).width();
                var leftW = ((screenWid) * .3);
                var rightW = ((screenWid) * (.7));
                wid2 = $("#rightDiv").width();
                var cur = 1;
                var max = $(".box-wrapper div").length;

            });

        </script>
        <!--added by dinanath -->
        <script type="text/javascript">

            var searchtextname = [];
            var searchtextid = [];
            var searchdesc = [];
            var tagReportName = new Array();
            var tagReportId = new Array();

            function gotoDBCON(contextPath) {
                if (!<%=isQDEnableforUser%>) {
                    alert("You do not have the sufficient previlages")
                } else {
                    window.location.href = ctxPath + "/pbBase.jsp?pagename=Query Studio&curntpage=Database Connection#Database_Connection";
                }
            }
        </script>
    </body>
</html>
<%}%>
