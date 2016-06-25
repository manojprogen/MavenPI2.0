<%-- 
    Document   : AdminTab
    Created on : Nov 11, 2009, 4:37:13 PM
    Author     : Administrator
--%>
<%@page contentType="text/html" pageEncoding="UTF-8" import="com.progen.users.PrivilegeManager,utils.db.*,prg.db.Session,prg.db.PbReturnObject,java.sql.*,prg.db.PbReturnObject,prg.db.Session,java.util.*,prg.db.PbDb,prg.util.screenDimensions"%>
<%--<%@page contentType="text/html" pageEncoding="windows-1252"%>--%>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<jsp:useBean id="brdcrmb"  scope="session" class="com.progen.action.BreadCurmbBean"/>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%          //for clearing cache
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader ("Expires", 0);

            String userIdStr = "";
             String themeColor="blue";
            if (session.getAttribute("USERID") != null) {
                userIdStr = (String) session.getAttribute("USERID");
            }
            if(session.getAttribute("theme")==null)
                session.setAttribute("theme",themeColor);
            else
                themeColor=String.valueOf(session.getAttribute("theme"));

            screenDimensions dims =new screenDimensions();
                int pageFont,anchorFont;
                HashMap screenMap  =dims.getFontSize(session,request,response);
                ////.println("screenMap --"+screenMap .size());
                if(!String.valueOf(screenMap .get("pageFont")).equalsIgnoreCase("NULL")){
                pageFont=Integer.parseInt(String.valueOf(screenMap .get("pageFont")));
                anchorFont = Integer.parseInt(String.valueOf(screenMap .get("pageFont")))+1;
                ////.println("pageFont--"+pageFont+"---anchorFont--"+anchorFont);
                }else{
                pageFont = 11;
                anchorFont = 12;
                ////.println("pageFont--"+pageFont+"---anchorFont--"+anchorFont);
                }
            boolean showExtraTabs = true;
            ServletContext context = getServletContext();
            //boolean isAxa = Boolean.parseBoolean(context.getInitParameter("isAxa"));
            if(session.getAttribute("USERID")==null || String.valueOf(screenMap.get("Redirect")).equalsIgnoreCase("Yes")){
response.sendRedirect(request.getContextPath()+"/baseAction.do?param=logoutApplication");
   }else{
                String contextPath=request.getContextPath();
%>
<html>
    <head>
       
        <script src="<%=contextPath%>/javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <script type="text/javascript" src="javascript/draggable/ui.core.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/jquery.tablesorter.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/tablesorter/addons/pager/jquery.tablesorter.pager.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/ui.dialog.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/scripts.js"></script>
        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/themes/<%=themeColor %>/style.css" type="text/css" media="print, projection, screen">
        <link type="text/css" href="<%=contextPath%>/jQuery/jquery/themes/base/ui.all.css" rel="stylesheet" />
<!--        <link type="text/css" href="<%=contextPath%>/jQuery/jquery/themes/base/ui.theme.css" rel="stylesheet" />-->
        <link type="text/css" href="<%=contextPath%>/jQuery/jquery/demos.css" rel="stylesheet" />
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=themeColor %>/metadataButton.css" rel="stylesheet" />
        <link type="text/css" href="<%=contextPath%>/jQuery/jquery/themes/base/BreadCrumb.css" rel="stylesheet" />
<!--        <link type="text/css" href="<%=contextPath%>/jQuery/jquery/themes/base/Base.css" rel="stylesheet" />-->
        <script type="text/javascript" src="<%=contextPath%>/jQuery/jquery/ui/jquery.jBreadCrumb.1.1.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/jQuery/jquery/ui/jquery.easing.1.3.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/ui.tabs.js"></script>

        <style type="text/css">

            a {cursor:pointer;font-size:<%=anchorFont%>px;}
            *{font:<%=pageFont%>px verdana}


        </style>
       
        <style type="text/javascript">
            .black_overlay{
                display: none;
                position: absolute;
                top: 0%;
                left: 0%;
                width: 110%;
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
        </style>
    </head>
    <body>
        <%

            //For Getting Roles
            String Pagename = "User List";
            String url = request.getRequestURL().toString();
            brdcrmb.inserting(Pagename, url);
            String userId = "";

            userId = String.valueOf(request.getSession(false).getAttribute("USERID"));

            String alertUrl = request.getContextPath() + "/alerts/JSPS/pbAlertList.jsp?userId=" + userId;
            String scenarioUrl = request.getContextPath() + "/Scenario/JSPs/pbScenarioList.jsp?userId=" + userId;
            String targetUrl = request.getContextPath() + "/QTarget/JSPs/pbTargetList.jsp?userId=" + userId;

        %>
                        <table style="width:100%;">
            <tr>
             <%--   <td valign="top" style="width:50%;">
                         <span style="font-size:10px;color:#2191C0;font-weight:bold;text-decoration: none;"> Welcome,<strong style="color:#008000;font-size:12px"> <%=session.getAttribute("LOGINID")%> </strong></span>
                </td>
            </tr>
            <tr>--%>
                <td valign="top" style="width:50%;">
                    <jsp:include page="Headerfolder/headerPage.jsp"/>
                </td>
            </tr>
        </table>
        <form name="myFormH" method="post" action=""></form>
        <table width="100%" class="ui-corner-all">
            <tr>
                <td style="height:10px;width:80%" align="left">
                    <%  String pgnam = "";%>
                    <div id=container align="left">
                        <div id='breadCrumb' class='breadCrumb module'>
                            <ul>
                                <li style="display:none"></li>
                                <li style="display:none"></li>
                                <%
            if (brdcrmb.getPgname1() != null) {
                pgnam = brdcrmb.getPgname1().toString();
                if (pgnam.equalsIgnoreCase(Pagename)) {
                                %>
                                <li style="font-family:helvetica;font-size:11px;color: #967117;font-weight:bold;">
                                    <%=brdcrmb.getPgname1()%>
                                </li>
                                <%
                      } else {
                                %>
                                <li>
                                    <a href=<%=brdcrmb.getPgurl1()%>><%=brdcrmb.getPgname1()%></a>
                                </li>
                                <%
                }
            }
            if (brdcrmb.getPgname2() != null) {
                pgnam = brdcrmb.getPgname2().toString();
                if (pgnam.equalsIgnoreCase(Pagename)) {
                                %>
                                <li style="font-family:helvetica;font-size:11px;color: #967117;font-weight:bold;">
                                    <%=brdcrmb.getPgname2()%>
                                </li>
                                <%
                         } else {
                                %>
                                <li>
                                    <a href=<%=brdcrmb.getPgurl2()%>><%=brdcrmb.getPgname2()%></a>
                                </li>
                                <%                    }
            }
            if (brdcrmb.getPgname3() != null) {
                pgnam = brdcrmb.getPgname3().toString();
                if (pgnam.equalsIgnoreCase(Pagename)) {
                                %>
                                <li style="font-family:helvetica;font-size:11px;color: #967117;font-weight:bold;">
                                    <%=brdcrmb.getPgname3()%>
                                </li>
                                <%
                         } else {
                                %>
                                <li>
                                    <a href=<%=brdcrmb.getPgurl3()%>><%=brdcrmb.getPgname3()%></a>
                                </li>
                                <%
                }
            }
            if (brdcrmb.getPgname4() != null) {
                pgnam = brdcrmb.getPgname4().toString();
                if (pgnam.equalsIgnoreCase(Pagename)) {
                                %>
                                <li style="font-family:helvetica;font-size:11px;color: #967117;font-weight:bold;">
                                    <%=brdcrmb.getPgname4()%>
                                </li>
                                <%
                         } else {
                                %>
                                <li>
                                    <a href=<%=brdcrmb.getPgurl4()%>><%=brdcrmb.getPgname4()%></a>
                                </li>
                                <%
                }
            }
            if (brdcrmb.getPgname5() != null) {
                pgnam = brdcrmb.getPgname5().toString();
                if (pgnam.equalsIgnoreCase(Pagename)) {
                                %>
                                <li style="font-family:helvetica;font-size:11px;color: #967117;font-weight:bold;">
                                    <%=brdcrmb.getPgname5()%>
                                </li>
                                <%
                         } else {
                                %>
                                <li>
                                    <a href=<%=brdcrmb.getPgurl5()%>><%=brdcrmb.getPgname5()%></a>
                                </li>
                                <%}
            }%>
                                <li style="display:none"></li>
                                <li style="display:none"></li>
                            </ul>
                        </div>
                    </div>
                    <div class="chevronOverlay main">
                    </div>
                </td>
<!--                <td valign="top" style="height:10px;width:20%" align="right">
                    <a href="javascript:void(0)" onclick="javascript:goNavigate()" style="font-size:10px;color:#2191C0;font-weight:bold;text-decoration: none;font-family:Georgia"> Navigation </a> |
                    <a href="javascript:void(0)" onclick="javascript:gohome()" style="font-size:10px;color:#2191C0;font-weight:bold;text-decoration: none;font-family:Georgia"> Home </a> |
                    <%if (showExtraTabs) {%>
                      <%--<a href="bugDetailsList.jsp" style="font-size:10px;color:#2191C0;;font-weight:bold;text-decoration: none;font-family:Georgia"> Bug </a> |--%>
                    <%}%>
                    <a href="javascript:void(0)" onclick="javascript:logout()" style="font-size:10px;color:#2191C0;font-weight:bold;text-decoration: none;font-family:Georgia"> Logout </a>
                </td>-->
            </tr>
        </table>
        <div id="tabs" style="width:99%;min-height:560px;height:auto" align="center">
            <ul>
                <li ><a href="userList.jsp" title="User Creation" >User List</a></li>
                <%//if(!isAxa){%>
<!--                hided by anitha pallothu starting from here-->
<!--                <li ><a href="useraccList.jsp" title="User Accounts">Company List</a></li>
                <%//}%>
                <li><a href="userTypeList.jsp" title="User Type List">User Type</a></li>
                <%//if (showExtraTabs) {
                   // if(!isAxa){%>
                <li><a href="stickyList.jsp" title="Sticky List">Sticky List</a></li>

                <%--<li ><a href="<%=targetUrl%>" title="Targets" onclick="pagname('Targets')" >Target Definition</a></li>--%>
                
                <li ><a href="<%=alertUrl%>" title="Alerts" onclick="pagname('Alerts')">Alerts Definiton</a></li>
                <li ><a href="<%=request.getContextPath()%>/Target/newJsps/pbTargetList.jsp&pagename=Administrator" title="Deviation" onclick="pagname('Deviation')">Deviation Analysis</a></li>
                <li ><a href="<%=scenarioUrl%>" title="Scenarios">Scenario</a></li>
                <li ><a href="javascript:void(0)" onclick="goCalenderAd(),pagname('Business Roles')" title="Time">Time Creation</a></li>
                <%  if("PROGEN".equalsIgnoreCase(PrivilegeManager.getUserName(Integer.parseInt(userId))))
                    {%>
-->                <li ><a href="<%=request.getContextPath()%>/SuperAdmin/SuperAdminConsole.jsp" onclick="pagname('SuperAdmin')" title="Super Admin Console">Super Admin Console</a></li>
<!--                    ends at here-->
<!--                <li ><a href="purchaseDetails.jsp" title="Purchase Details" >Purchase Details</a></li>-->
                <li ><a href="newpurchaseDetails.jsp" title="Purchase Details" >Purchase Details</a></li>
                <li ><a href="Assignment.jsp" title="Assignment" >Assignment</a></li>
                
                 <%}

                    //}}%>
                <li ><a href="DateFormat.jsp" title="User Datee Format" >Time SetUp</a></li>
                <li><a href="KillSession.jsp" title="Kill Session">Session Management</a></li>
<!--               <li ><a href="createCustomUser.jsp" title="Create User" >Create User</a></li>-->
            </ul>
        </div>        
        <table style="width:100%">
            <tr>
                <td valign="top" style="width:100%;">
                    <jsp:include page="Headerfolder/footerPage.jsp"/>
                </td>
            </tr>
        </table>
        <div id="fade" class="black_overlay"></div>

        <div id="calenderDiv">
            <iframe  id="calenderDisp" NAME='calenderDisp'  STYLE='display:none;height:400px;width:600px'   class="white_content" SRC='' ></iframe>

        </div>
<!--        <div  id="navigateDialog" class="navigateDialog" title="Navigation" style="display:none;">
            <iframe src="startPage.jsp" frameborder="0" height="100%" width="100%"></iframe>
        </div>-->
        <div id="assignPriviDialog" title="Assign Privileges" style="display:none">
            <iframe id="userTypePrivilagesFrame" width="100%" height="100%" frameborder="0" ></iframe>
        </div>

        <!--added by uday-->
        <div id="scenarioMeta" title="Create Scenario" style="display:none">
            <center>
                <br><br>
                <table style="width:80%" border="0">
                    <tr>
                        <td valign="top" class="myHead" style="width:30%">Scenario Name</td>
                        <td valign="top" style="width:80%">
                            <input type="text" maxlength="35" name="scenarioName" style="width:80%" id="scenarioName" onkeyup="tabmsg1()" onfocus="document.getElementById('save').disabled = false;"><br>
                            <span id="duplicate" style="color:red"></span>
                        </td>
                    </tr>
                    <tr>
                        <td  valign="top" class="myHead" style="width:30%">Description</td>
                        <td valign="top" style="width:70%">
                            <textarea name="scenarioDesc" id="scenarioDesc" style="width:80%"></textarea>
                        </td>
                    </tr>
                </table>
                <table>
                    <tr>
                        <td><input type="button" class="navtitle-hover" style="width:auto" value="Next" id="save" onclick="saveScenario()"></td>
                        <td><input type="button" class="navtitle-hover" style="width:auto" value="Cancel" onclick="cancelScenario()"></td>
                    </tr>
                </table>
            </center>
        </div>

        <div id="scenarioDialog" title="Assign Rating" style="display:none">
            <iframe id="assignRatingframe" name="assignRatingframe" frameborder="0" marginheight="0" marginwidth="0" src='#' width="100%" height="100%"></iframe>
        </div>

        <div id="editScenario" title="Edit Scenario" style="display:none">
            <iframe id="editScenarioFrame" name="editScenarioFrame" frameborder="0" src='#' width="100%" height="100%"></iframe>
        </div>
        <div id="stickTextDiv" title="View Text" style="display:none">
            <textarea style="overflow:auto" id="innerStick" cols="40" rows="9" readonly value=""></textarea>
        </div>
        <div id="viewByCompany"   title="Assigned Roles and Reports" STYLE='display:none;' >
            <iframe src='#' scrolling="no" height="100%" width="100%" frameborder="0" id="viewByFrame"></iframe>
        </div>
        <script type="text/javascript">
            function initNavi(){
                if ($.browser.msie == true){
//                    $("#navigateDialog").dialog({
//                        autoOpen: false,
//                        height: 520,
//                        width: 820,
//                        position: 'justify',
//                        modal: true
//                    });
                    $("#assignPriviDialog").dialog({
                        autoOpen: false,
                        height: 620,
                        width: 820,
                        position: 'justify',
                        modal: true
                    });
                    //added by uday
                    $("#scenarioMeta").dialog({
                        autoOpen: false,
                        height: 250,
                        width: 400,
                        position: 'justify',
                        modal: true
                    });
                    $("#scenarioDialog").dialog({
                        autoOpen: false,
                        height: 500,
                        width: 500,
                        position: 'justify',
                        modal: true
                    });
                    $("#editScenario").dialog({
                        autoOpen: false,
                        height: 400,
                        width: 500,
                        position: 'justify',
                        modal: true
                    });
                    $("#stickTextDiv").dialog({
                        autoOpen: false,
                        height: 300,
                        width: 300,
                        position: 'justify',
                        modal: true
                    });
                    $("#viewByCompany").dialog({
                        //bgiframe: true,
                        autoOpen: false,
                        height: 550,
                        width: 500,
                        position: 'justify',
                        modal: true

                    });
                }
                else{
//                    $("#navigateDialog").dialog({
//                        autoOpen: false,
//                        height: 460,
//                        width: 820,
//                        position: 'justify',
//                        modal: true
//                    });
                    $("#assignPriviDialog").dialog({
                        autoOpen: false,
                        height: 590,
                        width: 820,
                        position: 'justify',
                        modal: true
                    });
                    //added by uday
                    $("#scenarioMeta").dialog({
                        autoOpen: false,
                        height: 200,
                        width: 500,
                        position: 'justify',
                        modal: true
                    });
                    $("#scenarioDialog").dialog({
                        autoOpen: false,
                        height: 400,
                        width: 500,
                        position: 'justify',
                        modal: true
                    });
                    $("#editScenario").dialog({
                        autoOpen: false,
                        height: 350,
                        width: 500,
                        position: 'justify',
                        modal: true
                    });
                    $("#stickTextDiv").dialog({
                        autoOpen: false,
                        height: 200,
                        width: 300,
                        position: 'justify',
                        modal: true
                    });
                    $("#viewByCompany").dialog({
                        autoOpen: false,
                        height: 550,
                        width: 500,
                        position: 'justify',
                        modal: true
                    });
                }
            }
        </script>
         <script type="text/javascript">
            $(document).ready(function(){
                $("#breadCrumb").jBreadCrumb();
                $("#tabs").tabs();
            });
        </script>

        <script type="text/javascript">
            function viewByCompanyParent(userId){
                initNavi();
                var frameobj = document.getElementById("viewByFrame");
                frameobj.src= "ViewByCompanyDetails.jsp?userId="+userId;
                $("#viewByCompany").dialog('open');

            }
            function viewTextParent(sText,name){
                initNavi();
                $("#stickTextDiv").data('title.dialog',name)
                $("#stickTextDiv").dialog('open');
                document.getElementById("innerStick").value = sText;
            }
            //added by uday
            function createNewScenarioParent(){
                initNavi();
                $("#scenarioMeta").data('title.dialog','Create Scenario')
                $("#scenarioMeta").dialog('open');
                document.getElementById("scenarioName").value='';
                document.getElementById("scenarioDesc").value='';
            }
            function cancelScenarioParent(){
                $("#scenarioMeta").dialog('close');
            }
            function assignScenatioRatingParent(url){
                initNavi();
                var frameobj = document.getElementById("assignRatingframe");
                frameobj.src= "<%=request.getContextPath()%>/Scenario/JSPs/pbAssignScenarioRating.jsp?"+url;
                $("#scenarioDialog").data('title.dialog','Assign Rating')
                $("#scenarioDialog").dialog('open');
            }
            function editScenarioParent(scenarioId) {
                var FrameObj=document.getElementById("editScenarioFrame");
                FrameObj.src='<%=request.getContextPath()%>'+"/Scenario/JSPs/pbEditScenario.jsp?scenarioId="+scenarioId;
                initNavi();
                $("#editScenario").data('title.dialog','Edit Scenario')
                $("#editScenario").dialog('open');
            }

//            function goNavigate(){
//                initNavi();
//                $("#navigateDialog").dialog('open');
//            }
//            function closeStart(){
//                $("#navigateDialog").dialog('close');
//            }
//            function logout(){
//                document.forms.myFormH.action="baseAction.do?param=logoutApplication";
//                document.forms.myFormH.submit();
//            }
//            function gohome(){
//                document.forms.myFormH.action="baseAction.do?param=goHome";
//                document.forms.myFormH.submit();
//            }
            function gouserAd(){
                document.forms.myFormH.action="userList.jsp";
                document.forms.myFormH.submit();
            }
            function goToAccPage(jspname){
                document.forms.myFormH.action=jspname;
                document.forms.myFormH.submit();
            }
            <%--
                function gouserAd(){
                            document.forms.myFormH.action="userList.jsp";
                            document.forms.myFormH.submit();
                        }
                        function goPortalAd(){

                $.ajax({
                    url: 'portalTemplateAction.do?paramportal=checkUserPortalExist',
                    success: function(data){
                        if(data!=""){
                            var portallist=data.split("~");
                            var portalname=portallist[0];
                            var portalId=portallist[1];

                            /*        $.ajax({
                    url: 'portalViewer.do?portalBy=viewPortal&PORTALID='+portalId+'&PORTALNAME='+portalname,
                    success: function(data){


                    }
                });*/
                            document.forms.myFormH.action='portalViewer.do?portalBy=viewPortal&PORTALID='+portalId+'&PORTALNAME='+portalname;
                            document.forms.myFormH.submit();
                        }
                        else if(data==''){
                            document.getElementById('portal').style.display='block';
                            document.getElementById('fade').style.display='block';
                        }
                    }
                });

            }
            --%>
                function goCalenderAd(){
                    var frameObj=document.getElementById("calenderDisp");
                    var source = "createUserCalender.jsp";
                    frameObj.src=source;
                    // document.getElementById('calenderDiv').style.display='none';
                    frameObj.style.display='block';
                    document.getElementById('fade').style.display='block';

                }

                function CancelCalender(){
                    document.forms.myFormH.action="AdminTab.jsp";
                    document.forms.myFormH.submit();
                    // window.location.reload(true);
                }
                

        </script>

</html>
<%}%>
