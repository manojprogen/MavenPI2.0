<%@page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" import="com.progen.i18n.TranslaterHelper,com.progen.users.PrivilegeManager,utils.db.*,prg.db.Session,prg.db.PbReturnObject,java.sql.*" %>
<%@page import="prg.db.PbReturnObject,prg.db.Session,java.util.*,prg.db.PbDb,prg.util.screenDimensions" %>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<jsp:useBean id="brdcrmb"  scope="session" class="com.progen.action.BreadCurmbBean"/>
<%--
    Document   : UserManagement
    Created on : Mar 13, 2013, 6:30:05 PM
    Author     : gopesh.sharma@progenbusiness.com
--%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%          //for clearing cache
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader ("Expires", 0);
            Locale cle = null;
             cle = (Locale) session.getAttribute("UserLocaleFormat");
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
     String contextPath= request.getContextPath();          
%>
<html>
    <head>

      
         <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
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
        
    </head>
    <body>
        <script  type="text/javascript">
            var xmlhttp;
            function adminAssign(){
            $("#userListDiv").hide();
                }
                 function userList(){
            $("#userListDiv").show();
            $("#logicalGroup").hide();
             $("#selectTest").hide();
                }
               function pbBiManager(){
                var path = "";
                    path = "srchQueryAction.do?srchParam=pbBiManager";

                parent.closeStart();
                document.forms.myFormH.action=path;
                document.forms.myFormH.submit();
            }

                </script>

         <script type="text/javascript">
            $(document).ready(function(){
                $("#breadCrumb").jBreadCrumb();
                $("#tabs").tabs();
            });
        </script>

                  <table style="width:100%;">
            <tr>
                <td valign="top" style="width:50%;">
                    <jsp:include page="Headerfolder/headerPage.jsp"/>
                </td>
            </tr>
        </table>

         <div id="tabs" style="width:99%;min-height:560px;height:auto" align="center">

            <ul>
<!--            <li ><a href=javascript:void(0)" title="Bi Manager" onClick="pbBiManager()">Home</a></li>--> <!--  For Home tab-->
                <li ><a href="userList.jsp" title="User Creation" onClick="userList()"><%=TranslaterHelper.getTranslatedInLocale("User_List", cle)%></a></li>
<!--                <li ><a href="javascript:void(0)" id="QueryStudio Assignment" title="Analyzer Assignment" onClick="">Admin Assignment</a></li>
                <li ><a href="javascript:void(0)" id="PowerAnalyzer Assignment"  title="PowerAnalyzer Assignment" onClick="">Power Analyzer Assignment</a></li>-->
                <li ><a href="javascript:void(0)" title="Logic Group Creation" onClick="createGroupUsers()"><%=TranslaterHelper.getTranslatedInLocale("Create_Logic_Group", cle)%></a></li>
                <li ><a href="javascript:void(0)" title="Edit Group" onClick="editGroup()"><%=TranslaterHelper.getTranslatedInLocale("Edit_Group", cle)%></a></li>
                </ul>

        </div>

    </body>
</html>
<% } %>