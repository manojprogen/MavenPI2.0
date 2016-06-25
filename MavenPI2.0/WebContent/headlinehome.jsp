<%-- 
    Document   : workBench
    Created on : Jun 16, 2013, 4:10:03 PM
    Author     : progen
--%>



<%@page contentType="text/html" pageEncoding="UTF-8" import="java.util.HashMap,java.util.Locale,com.progen.action.UserStatusHelper,com.progen.i18n.TranslaterHelper"%>
<!DOCTYPE html>
<%
    
    response.setHeader("Cache-Control", "no-store");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);
    String userType = null;
    boolean isPortalEnableforUser=false;
    boolean isQDEnableforUser=false;
    boolean isPowerAnalyserEnableforUser=false;
    boolean isOneViewEnableforUser=false;
    boolean isScoreCardsEnableforUser=false;
    ServletContext context = getServletContext();
     HashMap<String,UserStatusHelper> statushelper;
     statushelper=(HashMap)context.getAttribute("helperclass");
     UserStatusHelper helper=new UserStatusHelper();
     if(!statushelper.isEmpty()){
        helper=statushelper.get(request.getSession(false).getId());
        if(helper!=null){
        isPortalEnableforUser=helper.getPortalViewer();
        isQDEnableforUser=helper.getQueryStudio();
        isPowerAnalyserEnableforUser=helper.getPowerAnalyser();
        isOneViewEnableforUser=helper.getOneView();
        isScoreCardsEnableforUser=helper.getScoreCards();
        userType=helper.getUserType();

        }}
            Locale cle = null;
             cle = (Locale) session.getAttribute("UserLocaleFormat");
             String contextPath=request.getContextPath();
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>pi</title>
        <link rel="stylesheet" href="stylesheets/treeviewstyle/jquery.treeview.css" />
        <link rel="stylesheet" href="stylesheets/treeviewstyle/screen.css" />
          <link type="text/css" href="<%=contextPath%>/jQuery/jquery/themes/base/BreadCrumb.css" rel="stylesheet" />
<!--        <link type="text/css" href="<%=request.getContextPath()%>/jQuery/jquery/themes/base/Base.css" rel="stylesheet" />-->
<!--        <link type="text/css" href="stylesheets/ui.all.css" rel="stylesheet" />
        <link type="text/css" href="stylesheets/demos.css" rel="stylesheet" />-->
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/blue/jquery-ui-1.7.3.custom.css" rel="stylesheet" />
        <link href="stylesheets/StyleSheet.css" rel="stylesheet" type="text/css" />
<!--        <link href="stylesheets/confirm.css" rel="stylesheet" type="text/css" />-->
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
<!--        <script type="text/javascript" src="javascript/queryDesign.js"></script>
        <script type="text/javascript" src="javascript/effects.core.js"></script>
        <script type="text/javascript" src="javascript/effects.explode.js"></script>
        <script language="JavaScript" src="<%=request.getContextPath()%>/querydesigner/JS/jquery.columnfilters.js"></script>
         below five lines only added by bharathi reddy on 26-08-09 -->
        <script src="javascript/jquery.simplemodal-1.1.1.js" type="text/javascript"></script>
        <script src="javascript/jquery.contextMenu.js" type="text/javascript"></script>
        <script type="text/javascript" src="javascript/ui.tabs.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/jQuery/jquery/ui/jquery.jBreadCrumb.1.1.js"></script>
         <script type="text/javascript" src="<%=contextPath%>/javascript/ui.dialog.js"></script>
        
         <style type="text/css">

            .headlinestyle {
                font-family: verdana;
                font-size: 12px;
                font-size-adjust: none;
                font-stretch: normal;
                font-style: normal;
                font-variant: normal;
                font-weight: bold;
                line-height: normal;


            }
        </style>
         <script type="text/javascript">
            jQuery(document).ready(function(){
                $("#breadCrumb").jBreadCrumb();
            });
            $(function() {
                $("#tabs").tabs().find(".ui-tabs-nav").sortable({axis:'x'});

            });
            $(document).ready(function() {
             
            })
            function gotoDBCON(ctxPath){
                 if(!<%=isQDEnableforUser%>){
                        alert("You do not have the sufficient previlages")
                    }else{
                document.forms.frmParameter.action=ctxPath+"/pbBase.jsp?pagename=Query Studio&curntpage=Database Connection#Database_Connection";
                document.forms.frmParameter.submit();
                }
            }
             function goPaths(path){
//                alert(path)
                var modulecode=path.replace("home.jsp#","");
                var userType='<%=userType%>'
                if(modulecode=='Dashboard_Studio' || modulecode=='Report_Studio'){
                    if(!<%=isPowerAnalyserEnableforUser%>)
                        alert("You do not have the sufficient previlages")
                }
                parent.closeStart();
                document.forms.baseForm.action=path;
                document.forms.baseForm.submit();
            }
            
           </script>
    </head>
    <body>
        <table style="width:100%;">
            <tr>
                <td valign="top" style="width:100%;">
                    <jsp:include page="Headerfolder/headerPage.jsp"/>
                </td>
            </tr>
        </table>
        <form  action="" id="baseForm" name="baseForm" method="post" style="padding:0pt">
             </form>
            <table width="99%" style="height:100%;"border="0" cellpadding="10" >
                <tbody>
                <tr>
                <td  valign="top" width="50%" align="center" >                   
                 <div id="tabs" style="width: 99%; min-height: 100%; max-height: 100%;" align="center">
                 <ul>
<!--                   <li><a href="dbQueries.jsp" title="ETL" onclick="pagname('ETL')">ETL</a></li>   -->
                <li><a href="headLines.jsp" title="Static Headlines" ><%=TranslaterHelper.getTranslatedInLocale("Static_Headlines", cle)%> </a></li>
                <li><a href="dynamicHeadLines.jsp" title="Dynamic Headlines" ><%=TranslaterHelper.getTranslatedInLocale("Dynamic_Headlines", cle)%> </a></li>
                 </ul></div>
                </td>
                </tr>
                </tbody>
            </table>
       
        <table width="100%" class="fontsty" bgcolor="#bdbdbd">
            <tr style="height:10px;width:100%;max-height:100%;background-color:#bdbdbd">
                <td style="height:10px;width:100%" bgcolor="#bdbdbd">
            <center><font  style="color:#fff;font-size:10px;font-family:verdana;" align="center">Copyright © 2009-12 <a href="http://www.progenbusiness.com" style="color:red;font-weight:bold">Progen Business Solutions.</a> All Rights Reserved</font></center>
        </td>
    </tr>
</table>
</body>
</html>
