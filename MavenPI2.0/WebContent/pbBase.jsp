<%@page contentType="text/html" pageEncoding="UTF-8" import="com.progen.i18n.TranslaterHelper,java.util.Locale,com.progen.users.PrivilegeManager,prg.db.Container,java.util.ArrayList"%>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<jsp:useBean id="brdcrmb"  scope="session" class="com.progen.action.BreadCurmbBean"/>
<!--pbBase.jsp-->
<%
   //for clearing cache
    response.setHeader("Cache-Control", "no-store");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);
    String contextPath=request.getContextPath();
Locale cle=null;
                   cle=(Locale)session.getAttribute("UserLocaleFormat");
 if (session.getAttribute("USERID") == null || request.getSession(false) == null) {
        response.sendRedirect(request.getContextPath() + "/baseAction.do?param=logoutApplication");
    } else {%>
<html lang="en">
    <head>

        <link rel="stylesheet" href="stylesheets/treeviewstyle/jquery.treeview.css" />
<!--        <link rel="stylesheet" href="stylesheets/treeviewstyle/screen.css" />-->
<!--          <link type="text/css" href="<%=contextPath%>/jQuery/jquery/themes/base/BreadCrumb.css" rel="stylesheet" />
        <link type="text/css" href="<%=contextPath%>/jQuery/jquery/themes/base/Base.css" rel="stylesheet" />-->
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=session.getAttribute("theme")%>/ui.theme.css"/>
        <link type="text/css" href="stylesheets/ui.all.css" rel="stylesheet" />
<!--        <link type="text/css" href="stylesheets/demos.css" rel="stylesheet" />-->
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=session.getAttribute("theme")%>/jquery-ui-1.7.3.custom.css" rel="stylesheet" />        
        <link href="stylesheets/StyleSheet.css" rel="stylesheet" type="text/css" />
        <link href="stylesheets/confirm.css" rel="stylesheet" type="text/css" />
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
        <script language="JavaScript" src="<%=contextPath%>/querydesigner/JS/jquery.columnfilters.js"></script>
        <!-- below five lines only added by bharathi reddy on 26-08-09 -->
        <script src="javascript/jquery.simplemodal-1.1.1.js" type="text/javascript"></script>
        <script src="javascript/jquery.contextMenu.js" type="text/javascript"></script>
        <script type="text/javascript" src="javascript/ui.tabs.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/jQuery/jquery/ui/jquery.jBreadCrumb.1.1.js"></script>
         <script type="text/javascript" src="<%=contextPath%>/javascript/ui.dialog.js"></script>
 <style>
            .fontsty{
                -moz-border-radius-bottomleft:4px;
                -moz-border-radius-bottomright:4px;
                -moz-border-radius-topleft:4px;
                -moz-border-radius-topright:4px;
                background-color:#bdbdbd;
            }
            a {font-family:Verdana;cursor:pointer;}
            .startpage {
                display:none;
                position: absolute;
                top: 15%;
                left: 24%;
                width:800px;
                height:400px;
                padding: 5px;
                border: 10px solid silver;
                background-color: white;
                z-index:1002;
                -moz-border-radius-bottomleft:6px;
                -moz-border-radius-bottomright:6px;
                -moz-border-radius-topleft:6px;
                -moz-border-radius-topright:6px;
            }
            .black_start{
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
           /* .body{
                background-color:#e6e6e6;
            }*/
        </style>

        <style type="text/css">
            .black_overlay{
                display: block;
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
            .loading_image{
                display: block;
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
                z-index:1001;
                overflow:auto;
            }
            .white_content1 {
                display: block;
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
        </style>
    </head>
    <%
            String Pagename = TranslaterHelper.getTranslatedInLocale("query_studio", cle);
            //////////////////////////////////////////////////////////.println.println("URL iS" + request.getRequestURL());
            String url = request.getRequestURL().toString();
    %>
    <body class="body" id="bodyId" style="margin: 0px;">
        <% String userId = String.valueOf(session.getAttribute("USERID"));
        Container container = null;
        %>
        <script>
//            function(){
//               home('<%=userId%>')
//            }
//                </script>
        <div id="loading" class="loading_image">
            <img id="imgId" src="images/help-loading.gif"  border="0px" style="position:absolute;left:600px;top:200px;">
</div>
            <table style="width:100%;">
                    <tr>
                            <td valign="top" style="width:100%;">
                                <jsp:include page="Headerfolder/headerPage.jsp"/>
                        </td>
                    </tr>
                </table>
            <form  action="" name="baseForm" method="post" style="padding:0pt">
        <table width="99%" border="0" >
            <tr style="display: none">
                <td valign="top">
                        <table width="100%">
                        <tr>
                                <td style="height:10px;width:80%" align="left" valign="top">
                                        <%
           brdcrmb.inserting(Pagename, url);
                            
              String pgnam = "";
              %>
                                    <div id='breadCrumb' class='breadCrumb module' style="width:500px">
                      <ul>
                                            <li style="display:none"></li>
                                            <li style="display:none"></li>
                          <%
               if (brdcrmb.getPgname1() != null) {
                   pgnam = brdcrmb.getPgname1().toString();
                   //out.println("six");
                   //////////////////////////////////////////////////////////.println.println("six");
                   if (pgnam.equalsIgnoreCase(Pagename)) {
                       %>

                       <li style="font-family:helvetica;font-size:11px;color: #967117;font-weight:bold;">
                       <%=brdcrmb.getPgname1()%>

                       </li>
                       <%
                   } else {
                       %>
                       <li>
                           <a href='<%=brdcrmb.getPgurl1()%>'><%=brdcrmb.getPgname1()%></a>
                       </li>
                       <%
                   }
               }
               if (brdcrmb.getPgname2() != null) {
                   pgnam = brdcrmb.getPgname2().toString();
                   //  out.println("seven");
                   //////////////////////////////////////////////////////////.println.println("seven");
                   if (pgnam.equalsIgnoreCase(Pagename)) {
                       %>
                       <li style="font-family:helvetica;font-size:11px;color: #967117;font-weight:bold;">
                       <%=brdcrmb.getPgname2()%>
                       </li>
                       <%
                   } else {
                       %>
                       <li>
                       <a href='<%=brdcrmb.getPgurl2()%>'><%=brdcrmb.getPgname2()%></a>
                       </li>
                       <%                    }
               }
               if (brdcrmb.getPgname3() != null) {
                   //  out.println("eight");
                   //////////////////////////////////////////////////////////.println.println("eight");
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
                       <a href='<%=brdcrmb.getPgurl3()%>'><%=brdcrmb.getPgname3()%></a>
                       </li>
                       <%
                   }
               }
               if (brdcrmb.getPgname4() != null) {
                   pgnam = brdcrmb.getPgname4().toString();
                   //   out.println("nine");
                   //////////////////////////////////////////////////////////.println.println("nine");
                   if (pgnam.equalsIgnoreCase(Pagename)) {
                    %>
                       <li style="font-family:helvetica;font-size:11px;color: #967117;font-weight:bold;">
                       <%=brdcrmb.getPgname4()%>
                       </li>
                       <%
                   } else {
                       %>
                       <li>
                       <a href='<%=brdcrmb.getPgurl4()%>'><%=brdcrmb.getPgname4()%></a>
                       </li>
                       <%
                   }
               }
               if (brdcrmb.getPgname5() != null) {
                   pgnam = brdcrmb.getPgname5().toString();
                   //  out.println("ten");
                   //////////////////////////////////////////////////////////.println.println("ten");
                   if (pgnam.equalsIgnoreCase(Pagename)) {
                       %>
                       <li style="font-family:helvetica;font-size:11px;color: #967117;font-weight:bold;">
                       <%=brdcrmb.getPgname5()%>
                       </li>
                       <%
                   } else {
                       %>
                       <li>
                       <a href='<%=brdcrmb.getPgurl5()%>'><%=brdcrmb.getPgname5()%></a>
                       </li>
                       <%
                   }
               }
                     %>
                                            <li style="display:none"></li>
                                            <li style="display:none"></li>

               </ul>
                  </div>
                                     <div class="chevronOverlay main" style="padding: 0px;"></div>

                                </td>
<!--                                <td  style="height:10px;width:20%" align="right">
                                <a href="javascript:void(0)" onclick="javascript:goGlobe()" style="font-size:10px;color:#2191C0;;font-weight:bold;text-decoration: none;font-family:Georgia"> Navigation </a> |
                                <a href="javascript:void(0)" onclick="javascript:home('<%=userId%>')" style="font-size:10px;color:#2191C0;;font-weight:bold;text-decoration: none;font-family:Georgia"> Home </a> |
                                 <a href="bugDetailsList.jsp" style="font-size:10px;color:#2191C0;;font-weight:bold;text-decoration: none;font-family:Georgia"> Bug </a> |
                                <a  href="javascript:void(0)" onclick="javascript:logout()" style="font-size:10px;color:#2191C0;;font-weight:bold;text-decoration: none;font-family:Georgia"> Logout </a>
                            </td>-->
                        </tr>
                    </table>                
                </td>
            </tr>
            <tr>
                <td valign="top" width="50%" align="center" >
                    <center >
                    <div id="tabs" style="width:99%;" align="center">                      
                            <ul>
                            <li ><a href="pbConnectionTab.jsp" title="Database Connections" onclick="pagname('DatabaseConnections')"><%=TranslaterHelper.getTranslatedInLocale("database_connections", cle)%></a></li>
                            <li ><a href="dimConCheck.jsp" title="Dimensions" onclick="pagname('Dimensions')" ><%=TranslaterHelper.getTranslatedInLocale("dimensions", cle)%></a></li>
<!--                            <li ><a href="timeConCheck.jsp" title="Time SetUp" onclick="pagname('Time SetUp')">Time SetUp</a></li>-->
                            <li ><a href="busGrpConCheck.jsp" title="Business Groups" onclick="pagname('Business Groups')"><%=TranslaterHelper.getTranslatedInLocale("business_groups", cle)%></a></li>
                            <li ><a href="busRoleConCheck.jsp" title="Business Roles" onclick="pagname('Business Roles')"><%=TranslaterHelper.getTranslatedInLocale("business_role", cle)%></a></li>
                           
<!--                            <li ><a href="Etl/etlUpload.jsp" title="ETL" onclick="pagname('ETL')">ETL</a></li>
                            <li ><a href="modifyMeasures.jsp" title="ModifyMeasures" onclick="pagname('ModifyMeasures')">Modify Measures</a></li>-->
                            <li ><a href="addsegmentation.jsp" title="Segmentation" onclick="pagname('metadata')"><%=TranslaterHelper.getTranslatedInLocale("segmentation", cle)%></a></li>
                            <li ><a href="flexiSegmentation.jsp" title="Flexi Segmentation" onclick="pagname('metadata')"><%=TranslaterHelper.getTranslatedInLocale("flexi_segmentation", cle)%></a></li>
                            <li ><a href="RoleOperations.jsp" title="Role Operations" onclick="pagname('metadata')"><%=TranslaterHelper.getTranslatedInLocale("role_operations", cle)%></a></li>
 <!--                            <li ><a href="Uploadfile.jsp" title="DataUpload" >Data Upload</a></li>
                            <li ><a href="dataDownload.jsp" title="DataDownload" >Data Download</a></li>
                            <li ><a href="modifyMembers.jsp" title="modifyMembers" >Modify Members</a></li>-->

                             <%
                           
                            if(PrivilegeManager.isModuleComponentEnabledForUser("QRYSTUDIO", "ETL", Integer.parseInt(userId))){
                            }%>


                        </ul>
                    </div><!-- End demo -->
                    </center>
                </td>
            </tr>
        </table>
          </form>
        <table width="100%" class="fontsty" >
            <tr style="width:100%;max-height:100%;">
                <td style="width:100%" >
                    <%@include file="Headerfolder/footerPage.jsp" %>

               </td>
            </tr>
        </table>
<!--         <div id="reportstart" class="navigateDialog" title="Navigation">
            <iframe src="startPage.jsp" frameborder="0" height="100%" width="800px" ></iframe>
            </div>-->

            <div id="fadestart" class="black_start"></div>

<!--
            <div id="reportstart" class="navigateDialog" title="Navigation">
            <iframe src="#" id="reportstartIframe" frameborder="0" height="100%" width="800px" ></iframe>

        </div>-->
         <div id="startPagePriv"  title="Login Start Page" STYLE='display:none'>
            <iframe src="#" height="100%" width="100%" frameborder="0" id="startPageFrame"></iframe>
        </div>
<script type="text/javascript">
            jQuery(document).ready(function(){
                $("#breadCrumb").jBreadCrumb();
            });
            $(function() {
                $("#tabs").tabs().find(".ui-tabs-nav").sortable({axis:'x'});

            });
//            function logout(){
//                document.forms.baseForm.action="newpbLogin.jsp";
//                document.forms.baseForm.submit();
//            }
//            function home(userId){
//                document.forms.baseForm.action="home.jsp?userId="+userId;
//                document.forms.baseForm.submit();
//            }
        </script>
        <script type="text/javascript">
    function pagname(pagename){
        var changedpage = pagename;      
        document.getElementById("crntpage").style.display = "none";
        document.getElementById("chngepage").style.display = "block";
        document.getElementById("getname").value = pagename;
    }
    
function viewDashboardG(path){
                document.forms.baseForm.action=path;
                document.forms.baseForm.submit();
            }
            function viewReportG(path){
                document.forms.baseForm.action=path;
                document.forms.baseForm.submit();
            }
//
//            function goGlobe(){
//               $(".navigateDialog").dialog('open');
//              document.getElementById("reportstartIframe").src="startPage.jsp"
//            }
//            function closeStart(){
//                $(".navigateDialog").dialog('close');
//            }
             function goPaths(path){
                parent.closeStart();
                document.forms.baseForm.action=path;
                document.forms.baseForm.submit();
            }

</script>
<script type="text/javascript">
     $(document).ready(function(){
        if ($.browser.msie == true){
         $(".navigateDialog").dialog({
        autoOpen: false,
        height: 620,
        width: 820,
        position: 'justify',
        modal: true
    });


    $("#startPagePriv").dialog({
                    autoOpen: false,
                    height: 550,
                    width: 820,
                    position: 'justify',
                    modal: true
                });

    }
    else{
         $(".navigateDialog").dialog({
        autoOpen: false,
        height: 460,
        width: 820,
        position: 'justify',
        modal: true
    });


    $("#startPagePriv").dialog({
                    autoOpen: false,
                    height: 550,
                    width: 820,
                    position: 'justify',
                    modal: true
                });
    }
    });
</script>
    </body>
</html>
<%}%>
