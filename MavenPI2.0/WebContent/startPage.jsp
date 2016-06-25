<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.progen.users.PrivilegeManager"%>
<%--
    Document   : startPage
    Created on : Oct 22, 2009, 10:23:16 PM
    Author     : Chiranjeevi
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="prg.db.PbReturnObject,prg.db.Session,java.util.*,prg.db.PbDb,com.progen.reportdesigner.db.ReportTemplateDAO,java.sql.*" %>

<%
            String userIdStr = "";
            String themeColor="blue";
            if (session.getAttribute("USERID") != null) {
                userIdStr = (String) session.getAttribute("USERID");
            }
            if(session.getAttribute("theme")==null)
                session.setAttribute("theme",themeColor);
            else
                themeColor=String.valueOf(session.getAttribute("theme"));
            String contextPath=request.getContextPath();
%>
<html>
    <head>
        <title>Start Page</title>

         <script src="javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/quicksearch.js"></script>
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=themeColor %>/ReportCss.css" rel="stylesheet"/>
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=themeColor %>/jquery-ui-1.7.3.custom.css" rel="stylesheet" />
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=themeColor %>/metadataButton.css" rel="stylesheet" />
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=themeColor %>/pbReportViewerCSS.css" rel="stylesheet" />
        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/treeviewstyle/jquery.treeview.css" />
        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/treeviewstyle/screen.css" />
        <script src="<%=contextPath%>/javascript/treeview/jquery.treeview.js" type="text/javascript"></script>
        <style type="text/css">
             *{
               -x-system-font: none;
               font-family: verdana;
               font-size: 13px;
               font-size-adjust: none;
               font-stretch: normal;
               font-style: normal;
               font-variant: normal;
               font-weight: normal;
               line-height: normal;
            }
            .white_content {
                display:none;
                position: absolute;
                top: 15%;
                left: 24%;
                width:800px;
                height:450px;
                padding: 5px;
                border: 10px solid silver;
                background-color: white;
                z-index:1002;
                -moz-border-radius-bottomleft:6px;
                -moz-border-radius-bottomright:6px;
                -moz-border-radius-topleft:6px;
                -moz-border-radius-topright:6px;
            }
            .black_overlay{
                display:none;
                position: absolute;
                top: 0%;
                left: 0%;
                width: 100%;
                height: 100%;
                background-color: black;
                z-index:1001;
                -moz-opacity: 0.5;
                opacity:.50;
                filter:alpha(opacity=50);
                overflow:auto;
            }
            .upperTab{
                position: absolute;
                top: 1%;
                left: 0%;
            }
            a {font-family:Verdana;cursor:pointer;text-decoration:none;font-size:12px;color:black}

            a:hover{text-decoration:inherit;font-weight:bold}
            font {
                font-family:verdana;font-size:12px;color:#336699
            }
        </style>
      
    </head>
    <body>
        <%
                    PbDb pbdb = new PbDb();
                    String userId = "";


                    userId = String.valueOf(request.getSession(false).getAttribute("USERID"));
                    String userprivis = "SELECT USER_ID, PRIVELEGE_ID FROM PRG_AR_USER_PRIVELEGES where USER_ID in(" + userId + ") ";

                    PbReturnObject userprivispbro = pbdb.execSelectSQL(userprivis);
                    ////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("-------------------");
                    userprivispbro.writeString();
                    Vector privis = new Vector();
                    for (int i = 0; i < userprivispbro.getRowCount(); i++) {
                        privis.add(userprivispbro.getFieldValueString(i, 1));
                    }
                    ////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("session.getAttribute(repList)  " + session.getAttribute("repList"));

//                    PbReturnObject Dlist = new ReportTemplateDAO().getAllDashsNavi(userId);

  //                  PbReturnObject Rlist = new ReportTemplateDAO().getAllrepsNavi(userId);
                    //PbReturnObject Dlist = null;
                    // PbReturnObject Rlist = null;
                    // if (session.getAttribute("dashList") != null) {
                    //     Dlist = (PbReturnObject) session.getAttribute("dashList");
                    // }
                    //  if (session.getAttribute("repList") != null) {
                    //     Rlist = (PbReturnObject) session.getAttribute("repList");
                    //  }
        %>


    <td valign="top">
        <div id="reportstart" >
            <form action=""  name="myFormH" method="post">
                <table width="100%" >
                    <tr style="width:100%">
                        <td style="height:10%;width:64%"><img alt=""  src="<%=request.getContextPath()%>/icons pinvoke/homeB1.png"/>
                            <a href="javascript:void(0)" onclick="javascript:gohome()">&nbsp;Home</a>
                        </td>
              
                        <td style="height:10%;width:20%" align="right" id="assign"><img alt=""  src="<%=request.getContextPath()%>/icons pinvoke/plus.png"/>
                         </td>
                   
                        <td style="height:10%;width:20%" align="right">
                            <a id="assignStartPage" href="javascript:void(0)" onclick="javascript:openstartpage()"> Assign Start Page </a>
                        </td>

                        <td style="height:10%;width:20%" align="right" id="themeImg"><img alt=""  src="<%=request.getContextPath()%>/icons pinvoke/overlay/color--plus.png"/>
                         </td>
                        <td>
                            <a href="javascript:void(0)" style="cursor:hand;text-decoration:none;font-family:verdana;" title="Click For Themes" onclick="showThemes()">&nbsp;&nbsp;Themes</a>
                        </td>

                    </tr>
                </table>
                <table border="1px solid " class="tabsty" align="center" width="100%" style="height:320px">
                    <tr style="width:100%;">
                        <td  class="bgcolor" width="22%">
                            <center>
                                <div><font><b>Tabs</b></font></div>
                            </center>
                        </td>
                        <td  class="bgcolor" width="39%">
                            <center>
                                <div><font><b>Reports</b></font></div>
                            </center>
                        </td>
                        <td  class="bgcolor" width="39%">
                            <center>
                                <div><font><b>Dashboards</b></font></div>
                            </center>
                        </td>
                    </tr>
                    <tr>
                        <td valign="top">
                            <div  id="Tabs" style="overflow:auto">
                                <table >

                                    <%if (userprivispbro.getRowCount() > 0) {%>

                                    <%  if (PrivilegeManager.isModuleEnabledForUser("REPVIEWER", Integer.parseInt(userId))||
                                           PrivilegeManager.isModuleEnabledForUser("VIEWERPLUS", Integer.parseInt(userId))||
                                           PrivilegeManager.isModuleEnabledForUser("ANALYSER", Integer.parseInt(userId))) {%>
                                           <tr align="top"> <td><img alt=""  src="<%=request.getContextPath()%>/icons pinvoke/arrow.png" /><a href="javascript:void(0)" onclick="goPaths('home.jsp#RolesTab')">&nbsp;Business Roles</a></td></tr>

                                    <%}
                                         if (PrivilegeManager.isModuleEnabledForUser("REPVIEWER", Integer.parseInt(userId))||
                                             PrivilegeManager.isModuleEnabledForUser("VIEWERPLUS", Integer.parseInt(userId))||
                                             PrivilegeManager.isModuleEnabledForUser("ANALYSER", Integer.parseInt(userId))) {%>
                                             <tr><td><img alt=""  src="<%=request.getContextPath()%>/icons pinvoke/arrow.png" /><a href="javascript:void(0)" onclick="goPaths('home.jsp#All_Reports')">&nbsp;All Reports</a></td></tr>
                                            <%}
                                               if(PrivilegeManager.isModuleEnabledForUser("ADMIN", Integer.parseInt(userId)))  {%>
                                             <tr><td><img alt=""  src="<%=request.getContextPath()%>/icons pinvoke/arrow.png" /><a href="javascript:void(0)" onclick="goPaths('AdminTab.jsp')">&nbsp;Administrator</a></td></tr>
                                            <%}
                                                if (privis.contains("Dashboard Studio")) {%>
                                             <tr><td><img alt=""  src="<%=request.getContextPath()%>/icons pinvoke/arrow.png" /><a href="javascript:void(0)" onclick="goPaths('home.jsp#Dashboard_Studio')">&nbsp;Dashboard Studio</a></td></tr>
                                            <%}
                                                if (PrivilegeManager.isModuleEnabledForUser("REPVIEWER", Integer.parseInt(userId))||
                                             PrivilegeManager.isModuleEnabledForUser("VIEWERPLUS", Integer.parseInt(userId))||
                                             PrivilegeManager.isModuleEnabledForUser("ANALYSER", Integer.parseInt(userId)))  {%>
                                             <tr><td><img alt=""  src="<%=request.getContextPath()%>/icons pinvoke/arrow.png" /><a href="javascript:void(0)" onclick="goPaths('home.jsp#Report_Studio')">&nbsp;Report Studio</a></td></tr>
                                            <%}
                                                if (PrivilegeManager.isModuleEnabledForUser("QRYSTUDIO", Integer.parseInt(userId))) {%>
                                             <tr><td><img alt=""  src="<%=request.getContextPath()%>/icons pinvoke/arrow.png" /><a href="javascript:void(0)" onclick="goPaths('pbBase.jsp')">&nbsp;Query Studio</a></td></tr>
                                            <%-- <tr><td><img src="<%=request.getContextPath()%>/icons pinvoke/arrow.png"><a href="javascript:void(0)" onclick="goPaths('pbBase.jsp#Database_Connections')" id="dataCon">&nbsp;Database Connec..</a></td></tr>
                                             <tr><td><img src="<%=request.getContextPath()%>/icons pinvoke/arrow.png"><a href="javascript:void(0)" onclick="goPaths('pbBase.jsp#Dimensions')">&nbsp;Dimensions</a></td></tr>
                                             <tr><td><img src="<%=request.getContextPath()%>/icons pinvoke/arrow.png"><a href="javascript:void(0)" onclick="goPaths('pbBase.jsp#Time_SetUp')">&nbsp;Time SetUp</a></td></tr>
                                             <tr><td><img src="<%=request.getContextPath()%>/icons pinvoke/arrow.png"><a href="javascript:void(0)" onclick="goPaths('pbBase.jsp#Business_Groups')">&nbsp;Business Groups</a></td></tr>
                                             <tr><td><img src="<%=request.getContextPath()%>/icons pinvoke/arrow.png"><a href="javascript:void(0)" onclick="goPaths('pbBase.jsp#Business_Roles')">&nbsp;Business Roles</a></td></tr>--%>
                                            <%}
                                               if (PrivilegeManager.isModuleEnabledForUser("MESSAGES", Integer.parseInt(userId))) {%>
                                             <tr><td><img alt=""  src="<%=request.getContextPath()%>/icons pinvoke/arrow.png" /><a href="javascript:void(0)" onclick="goPaths('home.jsp#Messages')">&nbsp;Messages</a></td></tr>
                                            <%}
                                                if (PrivilegeManager.isModuleEnabledForUser("ALERTS", Integer.parseInt(userId))) {%>
                                             <tr><td><img alt=""  src="<%=request.getContextPath()%>/icons pinvoke/arrow.png" /><a href="javascript:void(0)" onclick="goPaths('home.jsp#Alerts')">&nbsp;Alerts</a></td></tr>
                                            <%}
                                                 if (PrivilegeManager.isModuleEnabledForUser("PORTAL", Integer.parseInt(userId))) {%>
                                             <tr><td><img alt=""  src="<%=request.getContextPath()%>/icons pinvoke/arrow.png" /><a href="javascript:void(0)" onclick="goPortalPage()">&nbsp;Portals</a></td></tr>
                                            <%}%>
                                            <%  if (privis.contains("DataCorrection")) {%>
                                             <tr><td><img alt=""  src="<%=request.getContextPath()%>/icons pinvoke/arrow.png" /><a href="javascript:void(0)" onclick="goPaths('home.jsp#Data_Correction')">&nbsp;Data Correction</a></td></tr>
                                            <%}%>
                                            <%  if(PrivilegeManager.isModuleEnabledForUser("ADMIN", Integer.parseInt(userId)))  {%>
                                    <tr><td><img alt=""  src="<%=request.getContextPath()%>/icons pinvoke/arrow.png" /><a href="javascript:void(0)" onclick="goPaths('home.jsp#Admin')">&nbsp;Admin</a></td></tr>
                                            <%}%>
                                            <%}
/*                    else {%>

                                    <tr align="top"> <td><img alt=""  src="<%=request.getContextPath()%>/icons pinvoke/arrow.png"><a href="javascript:void(0)" onclick="goPaths('home.jsp#RolesTab')">&nbsp;Business Roles</a></td></tr>
                                    <tr><td><img alt=""  src="<%=request.getContextPath()%>/icons pinvoke/arrow.png" /><a href="javascript:void(0)" onclick="goPaths('home.jsp#All_Reports')">&nbsp;All Reports</a></td></tr>
                                    <tr><td><img alt=""  src="<%=request.getContextPath()%>/icons pinvoke/arrow.png" /><a href="javascript:void(0)" onclick="goPaths('AdminTab.jsp')">&nbsp;Administrator</a></td></tr>
                                    <tr><td><img alt=""  src="<%=request.getContextPath()%>/icons pinvoke/arrow.png" /><a href="javascript:void(0)" onclick="goPaths('home.jsp#Report_Studio')">&nbsp;Report Studio</a></td></tr>
                                    <tr><td><img alt=""  src="<%=request.getContextPath()%>/icons pinvoke/arrow.png" /><a href="javascript:void(0)" onclick="goPaths('home.jsp#Dashboard_Studio')">&nbsp;Dashboard Studio</a></td></tr>
                                    <tr><td><img alt=""  src="<%=request.getContextPath()%>/icons pinvoke/arrow.png" /><a href="javascript:void(0)" onclick="goPaths('pbBase.jsp')">&nbsp;Query Studio</a></td></tr>
                                    <tr><td><img alt=""  src="<%=request.getContextPath()%>/icons pinvoke/arrow.png" /><a href="javascript:void(0)" onclick="goPaths('home.jsp#Messages')">&nbsp;Messages</a></td></tr>
                                    <tr><td><img alt=""  src="<%=request.getContextPath()%>/icons pinvoke/arrow.png" /><a href="javascript:void(0)" onclick="goPaths('home.jsp#Alerts')">&nbsp;Alerts</a></td></tr>
                                    <tr><td><img alt=""  src="<%=request.getContextPath()%>/icons pinvoke/arrow.png" /><a href="javascript:void(0)" onclick="goPortalPage()">&nbsp;Portals</a></td></tr>
                                    <tr><td><img alt=""  src="<%=request.getContextPath()%>/icons pinvoke/arrow.png" /><a href="javascript:void(0)" onclick="goPaths('home.jsp#Data_Correction')">&nbsp;Data Correction</a></td></tr>
                                    <tr><td><img alt=""  src="<%=request.getContextPath()%>/icons pinvoke/arrow.png" /><a href="javascript:void(0)" onclick="goPaths('home.jsp#Admin')">&nbsp;Admin</a></td></tr>
                                            <%-- <tr><td><img src="<%=request.getContextPath()%>/icons pinvoke/arrow.png"><a href="javascript:void(0)" onclick="goPaths('pbBase.jsp#Database_Connections')" id="dataCon">&nbsp;Database Connec..</a></td></tr>
                                             <tr><td><img src="<%=request.getContextPath()%>/icons pinvoke/arrow.png"><a href="javascript:void(0)" onclick="goPaths('pbBase.jsp#Dimensions')">&nbsp;Dimensions</a></td></tr>
                                             <tr><td><img src="<%=request.getContextPath()%>/icons pinvoke/arrow.png"><a href="javascript:void(0)" onclick="goPaths('pbBase.jsp#Time_SetUp')">&nbsp;Time SetUp</a></td></tr>
                                             <tr><td><img src="<%=request.getContextPath()%>/icons pinvoke/arrow.png"><a href="javascript:void(0)" onclick="goPaths('pbBase.jsp#Business_Groups')">&nbsp;Business Groups</a></td></tr>
                                             <tr><td><img src="<%=request.getContextPath()%>/icons pinvoke/arrow.png"><a href="javascript:void(0)" onclick="goPaths('pbBase.jsp#Business_Roles')">&nbsp;Business Roles</a></td></tr>--%>
                                            <%}*/%>

                                </table>
                            </div>
                        </td>
                      
                        <td valign="top">
                             
                            <div id="Reports"   style="overflow:auto;height:320px;">
                                  <%
                                         if (PrivilegeManager.isModuleEnabledForUser("REPVIEWER", Integer.parseInt(userId))||
                                             PrivilegeManager.isModuleEnabledForUser("VIEWERPLUS", Integer.parseInt(userId))||
                                             PrivilegeManager.isModuleEnabledForUser("ANALYSER", Integer.parseInt(userId))) {%>
                                <table>
                                    <ul id="reptList" class="filetree treeview-famfamfam">
                                        <ul id="reportsUL">
                                        </ul>
                                    </ul>
                                </table><%}%>
                            </div>

                        </td>
                        <td valign="top">
                           
                            <div id="Dashs"   style="overflow:auto;height:320px">
                                  <%
                                         if (PrivilegeManager.isModuleComponentEnabledForUser("REPDESIGNER", "DASHBOARDDESIGNER",Integer.parseInt(userId))) {%>
                                <table style="overflow:auto">
                                   <ul id="dashbdList" class="filetree treeview-famfamfam">
                                        <ul id="dashbdUL">
                                        </ul>
                                    </ul>
                                </table><%}%>
                            </div>

                        </td>
                    </tr>
                </table>
            </form>
        </div>



<!--    <td valign="right">
        <a id="assignStartPage" href="javascript:void(0)" onclick="javascript:openstartpage()"> Assign Start Page </a>
    </td>-->
<!--      <div id="reportstart" class="navigateDialog" title="Navigation">
            <iframe src="startPage.jsp" id="reportstartIframe" frameborder="0" height="100%" width="800px" ></iframe>-->

<!--        <div id="startPagePriv"  title="Login Start Page" STYLE='display:none'>
            <iframe src="#" height="100%" width="100%" frameborder="0" id="startPageFrame"></iframe>
        </div>-->

        <div id="portal" class="white_content"  align="justify" style="height:150px;width:400px;display:none">
            <center>
                <br><br>
                <table style="width:80%" border="0">
                    <tr>
                        <td valign="top" class="myHead" style="width:30%">Portal Name</td>
                        <td valign="top" style="width:80%">
                            <input type="text" maxlength="35" name="portalName" style="width:80%" id="portalName" onkeyup="tabmsgPortal()" onfocus="document.getElementById('portalsave').disabled = false;"><br><span id="duplicatePortal" style="color:red"></span>
                        </td>
                    </tr>
                    <tr>
                        <td  valign="top" class="myHead" style="width:30%">Portal Description</td>
                        <td valign="top" style="width:70%">
                            <textarea cols="" rows=""  name="portalDesc" id="portalDesc" style="width:80%"></textarea>
                        </td>
                    </tr>
                </table>
                <table>
                    <tr>
                        <td><input type="button" class="navtitle-hover" value="Next" id="portalsave" onclick="savePortal()"></td>
                            <%--<td><input type="button" class="btn" value="Cancel" onclick="cancelPortal()"></td>--%>
                    </tr>
                </table>

            </center>
        </div>


          <div id="showThemes" title="Themes"  style="display:none">
            <table >
                <tbody>
                    <tr>
                        <td>Themes :</td>
                        <td>
                            <select name="theme" id="theme" style="width:130px">
                                <option  value="<%=session.getAttribute("theme")%>" selected> <%=session.getAttribute("theme")%></option>
                                <%if(!session.getAttribute("theme").toString().equalsIgnoreCase("Blue")){%>
                                <option value="blue">Blue</option>
                                <%} if(!session.getAttribute("theme").toString().equalsIgnoreCase("Green")){%>
                                <option value="green">Green</option>
                                <%} if(!session.getAttribute("theme").toString().equalsIgnoreCase("Orange")){%>
                                <option value="orange">Orange</option>
                                <%} if(!session.getAttribute("theme").toString().equalsIgnoreCase("Violet")){%>
                                <option value="violet">Violet</option>
                                <%} if(!session.getAttribute("theme").toString().equalsIgnoreCase("Purple")){%>
                                <option value="purple">Purple</option>
                                <%}%>
                            </select>
                        </td>
                    </tr>
                    <tr></tr>
                    <tr></tr>
                    <tr>
                        <td align="center" colspan="2"><input class="navtitle-hover" type="button" value="Ok" onclick="ChangeTheme()"></td>
                    </tr>
                </tbody>
            </table>
        </div>
  <script type="text/javascript">
            $(document).ready(function()
            {
               if ( parent.$("#startPagePriv").length == 0 ){
                $("#assignStartPage").remove();
                $("#assign").remove();
               }
               if($.browser.msie== true)
               {
                     $("#showThemes").dialog({
                       bgiframe: true,
                       autoOpen: false,
                       height: 250,
                       width: 300,
                       modal: true
            });
               }

            else{
                 $("#showThemes").dialog({
                bgiframe: true,
                autoOpen: false,
                height: 150,
                width: 300,
                modal: true
            });
            }

//            $("#quicksearch").remove();
           $.ajax({
                url: '<%=request.getContextPath()%>/reportTemplateAction.do?templateParam=getRoles',
                success: function(data) {
                    var json=eval('('+data+')');
                    var roleId=json.roleId;
                    var roleName=json.roleName;
                    var ulHtml="";
                     for(var i=0;i<roleId.length;i++)
                     {
                         ulHtml+="<li class='closed' id='" + roleId[i]+ "' onclick=\"getReportName('"+roleId[i]+"')\" >";
                         ulHtml+="<img src='<%=request.getContextPath()%>/icons pinvoke/table.png'/>";
                         ulHtml+="<span style='font-family:verdana;' title='" + roleName[i] + "'>" + roleName[i] + "</span>";
                         ulHtml+="<ul id='dRoleRept_"+roleId[i]+"'>";
                         ulHtml+="</ul></li>";
                     }
                      $("#dashbdUL").html(ulHtml);
                      ulHtml="";
                      
                    $("#dashbdUL").treeview({
                    animated:"slow"
                    //persist: "cookie"
                });

//                    $('ul#dashbdUL li').quicksearch({
//                        position: 'before',
//                        attached: 'ul#dashbdUL',
//                        loaderText: '',
//                        delay: 100
//                    })

                    for(var i=0;i<roleId.length;i++)
                     {
                         ulHtml+="<li class='closed' id='" + roleId[i]+ "' onclick=\"getReportName('"+roleId[i]+"')\">";
                         ulHtml+="<img src='<%=request.getContextPath()%>/icons pinvoke/table.png'/>";
                         ulHtml+="<span style='font-family:verdana;' title='" + roleName[i] + "'>" + roleName[i] + "</span>";
                         ulHtml+="<ul id='rRoleRept_"+roleId[i]+"'>";
                         ulHtml+="</ul></li>";
                     }
                     $("#reportsUL").html(ulHtml);
                  
                 $("#reportsUL").treeview({
                    animated:"slow"
                    //persist: "cookie"
                });
//                $('ul#reportsUL > li > ul >li ').quicksearch({
//                position: 'before',
//                attached: 'ul#reportsUL',
//                loaderText: '',
//                delay: 100
//                })
                }
                 });
        });

    function getReportName(roleId)
        {
             var rliHtml="";
             var dliHtml="";
             $.ajax({
                url: '<%=request.getContextPath()%>/reportTemplateAction.do?templateParam=getReportBasedOnRole&roleId='+roleId,
                success: function(data)
                {
                    var json=eval('('+data+')');
                    for(var i=0;i<json.length;i++)
                    {
                              if(json[i].reportType=='R')
                         {
                             rliHtml+="<li class='closed'>";
                             rliHtml+="<img src='<%=request.getContextPath()%>/icons pinvoke/report.png'/>";
                             rliHtml+="<a onclick=\"viewReportG('<%=request.getContextPath()%>/reportViewer.do?reportBy=viewReport&REPORTID="+json[i].reportId+"&action=open')\">";
                             rliHtml+="<span class='' id='" +json[i].reportId + "'  title='" + json[i].reportName + "' style='font-family:verdana;'>" +json[i].reportName + "</span></a>";
                             rliHtml+="</li>";
                         }
                         else
                          {
                             dliHtml+="<li class='closed'>";
                             dliHtml+="<img src='<%=request.getContextPath()%>/icons pinvoke/report.png'/>";
                             dliHtml+="<a onclick=\"viewDashboardG('<%=request.getContextPath()%>/dashboardViewer.do?reportBy=viewDashboard&REPORTID="+json[i].reportId +"&pagename=" + json[i].reportName + "')\">";
                             dliHtml+="<span class='' id='" +json[i].reportId + "'  title='" + json[i].reportName + "' style='font-family:verdana;'>" + json[i].reportName + "</span></a>";
                             dliHtml+="</li>";

                          }
                    }

                     $("#rRoleRept_"+roleId).html(rliHtml);
                     $("#dRoleRept_"+roleId).html(dliHtml);
                }
                 });
        }
//            $(document).ready(function(){
//                if ($.browser.msie == true){
//                    $("#startPagePriv").dialog({
//                        autoOpen: false,
//                        height: 650,
//                        width: 820,
//                        position: 'justify',
//                        modal: true
//                    });
//                    $(".navigateDialog").dialog({
//                        autoOpen: false,
//                        height: 620,
//                        width: 820,
//                        position: 'justify',
//                        modal: true
//                    });
//                }
//
//                else{
//                    $("#startPagePriv").dialog({
//                        autoOpen: false,
//                        height: 550,
//                        width: 820,
//                        position: 'justify',
//                        modal: true
//                    });
//                    $(".navigateDialog").dialog({
//                        autoOpen: false,
//                        height: 460,
//                        width: 820,
//                        position: 'justify',
//                        modal: true
//                    });
//                }
//            });


            function initDialog(){

            //                $("#startPagePriv").dialog({
            //                    autoOpen: false,
            //                    height: 550,
            //                    width: 820,
            //                    position: 'justify',
            //                    modal: true
            //                });


           }

            function viewDashboardG(path){
                //document.forms.myFormH.action=path;
                //document.forms.myFormH.submit();
                parent.viewDashboardG(path);
            }
            function viewReportG(path){
                //document.forms.myFormH.action=path;
                // document.forms.myFormH.submit();
                parent.viewReportG(path);
            }
            function gohome(){
                // document.forms.myFormH.action="baseAction.do?param=goHome";
                // document.forms.myFormH.submit();
                parent.gohome();
            }
            function closeStart(){
                //   document.getElementById('reportstart').style.display='none';
                // document.getElementById('fadestart').style.display='none';
                parent.closeStart();
            }
            function goPaths(path){
                parent.goPaths(path);
            }
            function goPortalPage(){
                $.ajax({
                    url: '<%=request.getContextPath()%>/portalTemplateAction.do?paramportal=checkUserPortalExist',
                    success: function(data){
                        if(data!=""){
                            var portallist=data.split("~");
                            var portalname=portallist[0];
                            var portalId=portallist[1];
                            parent.document.forms[0].method="post";
                            parent.document.forms[0].action='<%=request.getContextPath()%>/portalViewer.do?portalBy=viewPortals';
                            parent.document.forms[0].submit();
                        }else{
                            document.getElementById('portal').style.display='block';
                            document.getElementById('fade').style.display='block';
                        }
                    }
                });

                // parent.window.location.reload(true);
            }
            function tabmsgPortal(){
                document.getElementById('portalDesc').value = document.getElementById('portalName').value;
            }
            function cancelPortal(){
                document.getElementById('duplicatePortal').innerHTML = '';
                document.getElementById('portalsave').disabled = false;
                document.getElementById('portal').style.display='none';
                document.getElementById('fade').style.display='none';
            }
            function savePortal(){
                var portalName = document.getElementById('portalName').value;
                var portalDesc = document.getElementById('portalDesc').value;
                $.ajax({
                    url: '<%=request.getContextPath()%>/portalTemplateAction.do?paramportal=checkPortalName&portalName='+portalName+'&portalDesc='+portalDesc,
                    success: function(data){
                        if(data!=""){
                            document.getElementById('duplicatePortal').innerHTML = data;
                            document.getElementById('portalsave').disabled = true;
                        }
                        else if(data==''){
                            $.ajax({
                                url: '<%=request.getContextPath()%>/portalTemplateAction.do?paramportal=insertPortalMaster&portalName='+portalName+'&portalDesc='+portalDesc,
                                success: function(data){

                                    var portallist=data.split("~");
                                    var portalname=portallist[0];
                                    var portalId=portallist[1];
                                    parent.document.forms[0].method="post";
                                    parent.document.forms[0].action='<%=request.getContextPath()%>/portalViewer.do?portalBy=viewPortal&PORTALID='+portalId+'&PORTALNAME='+portalname;
                                    parent.document.forms[0].submit();

                                    /*        $.ajax({
                    url: 'portalViewer.do?portalBy=viewPortal&PORTALID='+portalId+'&PORTALNAME='+portalname,
                    success: function(data){


                    }
                });*/
                                }
                            });

                        }
                    }
                });
            }

           
//            function openstartpage(){
//                initDialog();                
//                parent.$("#reportstart").dialog('close');
//                parent.$("#startPagePriv").dialog('open');
//                var frameObj =parent.document.getElementById("startPageFrame");
//                frameObj.src =  "loginStart.jsp?checkUser="+<%=userIdStr%>+"&fromPage=startpage";
//  
//
//
//            }
            function showThemes(){
               $("#showThemes").dialog('open');
            }
            
         function ChangeTheme()
        {
             $("#showThemes").dialog('close');
            var theme=document.getElementById('theme').value
            $.ajax({
            url:'<%=request.getContextPath()%>/reportViewer.do?reportBy=changeTheme&changeTheme=true&theme='+theme,
            success:function(data) {
             parent.document.location.href=parent.document.location.href;
             }
         });
        }

        </script>


        <!-- <div id="fade" class="black_overlay"></div>
        <div id="assignStart" style="display:Name" >
            <iframe id="assignStartiframe" NAME="assignStart" src="#">
             </iframe>
         </div>-->
    </body>
</html>
