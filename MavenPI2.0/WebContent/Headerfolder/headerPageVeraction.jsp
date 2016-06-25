<%@page import="com.progen.reportview.action.ReportViewerAction"%>
<%@page import="java.util.Locale"%>
<%@page import="com.progen.i18n.TranslaterHelper"%>
<%@page import="java.net.InetAddress"%>
<%@page import="prg.db.PbReturnObject"%>
<%@page import="prg.db.PbDb"%>
<%@page import="prg.db.Container"%>
<%@page import="com.progen.reportview.db.PbReportViewerDAO"%>
<%@page import="com.progen.report.PbReportCollection"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.sql.Connection"%>
<%@page import="utils.db.ProgenParam"%>
<%@page import="com.progen.users.UserLayerDAO"%>
<%@page import="com.progen.action.UserStatusHelper"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.progen.db.PbBaseDAO"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<jsp:useBean id="duration" scope="session" class="utils.db.ProgenParam"/>

<%
//added by manik for pi logo alignment
//added by Dinanath for default locale
                    Locale currentLocale=null;
                   currentLocale=(Locale)session.getAttribute("UserLocaleFormat");
  //ended By Dinanath
            String LOGINID = String.valueOf(session.getAttribute("LOGINID"));
            String status = String.valueOf(session.getAttribute("status"));
            String insightsUserRole = String.valueOf(session.getAttribute("insightsUserRole"));
            String statusVeraction = "Ok";
            String companyTitle =(String)request.getSession(false).getAttribute("compFavtitle");
            if(companyTitle==null ){
               companyTitle = "pi";
            }
           String companyIcon = (String)request.getSession(false).getAttribute("compFavicon");
           if(companyIcon==null){
               companyIcon = "pi_favicon.png";
           }
            boolean isCompanyValid = false;
            String ssoValue = String.valueOf(session.getAttribute("ssoToken"));
            String themeColor = "blue";
            if (session.getAttribute("theme") == null) {
                session.setAttribute("theme", themeColor);
            } else {
                themeColor = String.valueOf(session.getAttribute("theme"));
            }

            String userid = String.valueOf(session.getAttribute("USERID"));
//            UserLayerDAO userdao = new UserLayerDAO();
            int USERID = Integer.parseInt((String) session.getAttribute("USERID"));
    String userType = "";
//            ServletContext context = getServletContext();
//    HashMap<String, UserStatusHelper> statushelper;
//    if (context.getAttribute("helperclass") != null) {
//        statushelper = (HashMap) context.getAttribute("helperclass");
//        UserStatusHelper helper = new UserStatusHelper();
//        if (!statushelper.isEmpty()) {
//            helper = statushelper.get(request.getSession(false).getId());
//            if (helper != null) {
////                isQDEnableforUser = helper.getQueryStudio();
////                isPowerAnalyserEnableforUser = helper.getPowerAnalyser();
////                isXtendUser = helper.getXtendUser();
//                userType = helper.getUserType();
//            }
////
//        }
//    }
//            userType = "ANALZER"; // for udise
            PbDb pbdb = new PbDb();
            String userId = "";
            userId = String.valueOf(request.getSession(false).getAttribute("USERID"));
            String userstart = "select start_page from prg_ar_users where pu_id=" + userId;
            PbReturnObject userstartpbro = pbdb.execSelectSQL(userstart);
            String homeVar = "";
            userstartpbro.writeString();
            String strpage ="landingPage.jsp";
              if (status.equalsIgnoreCase("OK")) {
            strpage ="landingPageVeraction.jsp";
            }else{
            strpage ="landingPage.jsp";
            }
            if(userstartpbro!=null && userstartpbro.rowCount>0){
            strpage = userstartpbro.getFieldValueString(0, 0);
            if (strpage == null || strpage.equalsIgnoreCase("")) {
                homeVar = "home.jsp";
            } else if (strpage != null && strpage.equalsIgnoreCase("newHome.jsp")) {
                homeVar = "newHome.jsp";
            } else {
                homeVar = "home.jsp";
            }
                       }
            // Added by Prabal for open report tab
             String openReportTab=(String) session.getAttribute("ReportTabMap");
             if(openReportTab==null)
                 openReportTab="_blank";
%>
<html>
    <head>

        <link rel="icon" type="image/png" href="<%=request.getContextPath()%>/images/<%=companyIcon%>">
        <title><%=companyTitle%></title>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/latofonts.css"/>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/global.css"/>
        <script type="text/javascript" src="<%=request.getContextPath()%>/JS/global.js"></script>
	<link rel="stylesheet" href="css/font-awesome.min.css" type="text/css"/>
        <!--Added By Ashutosh for Quick Nav-->
        <style type="text/css">
.hamburger {
    position: relative;
    display: inline-block;
    width: 1.25em;
    height: 17px;
    margin-right: 0.3em;
    border-top: 1px solid rgba(138, 191, 66, 0.95);
    border-bottom: 1px solid rgba(138, 191, 66, 0.95);
    font-size: 24px;
    margin-left: 96%;
margin-top: -40px;
                cursor: pointer;
float: left;
            }

.hamburger:before {
    content: "";
    position: absolute;
    top: 0.3em;
    left: 0px;
    width: 100%;
    border-top: 1px solid rgba(138, 191, 66, 0.95);
            }

             </style>

        <script type="text/javascript">
            var flag_tags =0;
            var flag_fav =0;
            function slide_ul(tag_ul){
                //                alert($( '#tag_ul' ).is(":visible"))
                //                $('.parent').slideToggle();
                $(tag_ul).slideToggle( 'fast' );
            }
            function reportsPath(repId) {
                // alert("hhhhhhhhh")

                //   openReportWithDefaultFilters()
                window.open('<%=request.getContextPath()%>/reportViewer.do?reportBy=viewReport&REPORTID=' + repId + '&action=open', '_blank');

            }

            function oneViewPath(oneViewId ,oneViewName){
                // alert("hii")
                //alert("oneView"+oneViewId+" .."+oneViewName)
                window.open('srchQueryAction.do?srchParam=oneViewBy&homeFlag=true&oneViewId='+oneViewId+'&oneviewname='+oneViewName,'_blank')
            }
            function dashBoardPath(repId,value){
//                alert("hello")
                //window.open('<%=request.getContextPath()%>/reportViewer.do?reportBy=viewReport&REPORTID=' + repId + '&action=open&isKPIDashboard=true')
//                        alert(repId+"::"+value)
                if(value.split("::")[1]=="D")
                     window.open('<%=request.getContextPath()%>/dashboardViewer.do?reportBy=viewDashboard&REPORTID=' + repId + '&action=open');
                 else
                      window.open('<%=request.getContextPath()%>/reportViewer.do?reportBy=viewReport&REPORTID=' + repId + '&action=open&isKPIDashboard=true');
//                  reportViewer.do?reportBy=viewReport&REPORTID=283&action=open&isKPIDashboard=true
            };

            //          function dashBoardPath(repId){
            //              window.open('<%=request.getContextPath()%>/reportViewer.do?reportBy=viewReport&REPORTID=' + repId + '&action=open&isKPIDashboard=true')
            //          }
            function openDashboard(dashName,dashID){

              window.open('<%=request.getContextPath()%>/dashboardViewer.do?reportBy=viewDashboard&REPORTID=' + dashID + '&action=open');

            }
            function openTimeDashboard(dashName,dashID){

                window.open('<%=request.getContextPath()%>/reportViewer.do?reportBy=viewReport&REPORTID=' + dashID + '&action=open&isKPIDashboard=true');

            }
            function openOneView(oneViewIdValue){
                var oneviewname='';
                $.ajax({
                    url: 'oneViewAction.do?templateParam2=getonename&oneViewIdValue='+oneViewIdValue,
                    success: function(data){
                        //alert("onviewNAm"+data)
                        oneviewname=data;
                        window.open('srchQueryAction.do?srchParam=oneViewBy&homeFlag=true&oneViewId='+oneViewIdValue+'&oneviewname='+oneviewname,'_blank')
                    }
                });
            }

        </script>
        <!--ended By Ashutosh for Quick Nav-->

        <script type="text/javascript">
            var useridh = '<%=userid%>';
            function initNavi() {
                if ($.browser.msie == true) {

                    $("#navigateDialog").dialog({
                        autoOpen: false,
                        height: 620,
                        width: 820,
                        position: 'justify',
                        modal: true
                    });
                    $("#modules").dialog({
                        autoOpen: false,
                        height: 800,
                        width: 200,
                        position: 'justify',
                        modal: true
                    });
                }
                else {
                    $("#navigateDialog").dialog({
                        autoOpen: false,
                        height: 460,
                        width: 820,
                        position: 'justify',
                        modal: true
                    });
                    $("#modules").dialog({
                        autoOpen: false,
                        height: 800,
                        width: 200,
                        position: 'justify',
                        modal: true
                    });
                }


            }
            function openNavigation() {
                $("#opnNavgtn").show();

            }
            function goGlobe() {
                initNavi();
                $("#navigateDialog").dialog('open');
                document.getElementById("reportstartIframe").src = "<%=request.getContextPath()%>/startPage.jsp";
            }
            function gohome() {
                document.forms.searchForm.action = "<%=request.getContextPath()%>/baseAction.do?param=goHome";
                document.forms.searchForm.submit();
            }
            function logout() {
                document.forms.searchForm.action = "<%=request.getContextPath()%>/baseAction.do?param=logoutApplication";
                document.forms.searchForm.submit();
            }
            function veractionLogout(){
               document.forms.searchForm.action = "<%=request.getContextPath()%>/baseAction.do?param=veractionLogout";
                document.forms.searchForm.submit();
            }
            function closeStart() {
                $("#navigateDialog").dialog('close');
            }
            function goPortal() {
                $.ajax({
                    url: 'portalTemplateAction.do?paramportal=checkUserPortalExist',
                    success: function(data) {
                        //                            document.forms.myFormH.action='portalViewer.do?portalBy=viewPortal&PORTALID='+portalId+'&PORTALNAME='+portalname;
                        document.forms.searchForm.action = 'portalViewer.do?portalBy=viewPortals';
                        document.forms.searchForm.submit();


                    }
                });
            }
            function editWall() {
                document.forms.myFormH.action = 'srchQueryAction.do?srchParam=editWall';
                document.forms.myFormH.submit();
            }
            function oneViewBy() {
                document.forms.searchForm.action = 'srchQueryAction.do?srchParam=oneViewBy';
                document.forms.searchForm.submit();
            }

            function IcalPage() {
                document.forms.searchForm.action = 'srchQueryAction.do?srchParam=icalPage';
                document.forms.searchForm.submit();
            }
            function CreateReportNew() {
                $("#createReportNew").dialog('open');
            }


            function CreateReportNew1(){
                //  alert("createReportNew1")
                $("#createReportNew1").dialog('open');
            }

            function CreateReportNew2(){
                //  alert("createReportNew1")
                $("#createReportNew2").dialog('open');
                               $.ajax({
        async:false,
       url: 'reportTemplateAction.do?templateParam=removeSessionAttributes',
        success: function(data){
            }
    });
            }

            function tabmsg1(){
                document.getElementById('reportDesc1').value = document.getElementById('reportName1').value;
            }
            //added by krishan
            function creationOfReport(repId) {
                var fldObj = document.getElementsByName("userfldsList");
                var foldersIds = "";

                if (fldObj.length != 0) {
                    for (var i = 0; i < fldObj.length; i++) {
                        if (fldObj[i].checked) {
                            foldersIds = foldersIds + "," + fldObj[i].id;
                        }
                    }
                }
                if (foldersIds != "") {
                    foldersIds = foldersIds.substr(1, foldersIds.length);
                }
                document.forms.searchForm.action = "reportTemplateAction.do?templateParam=selectRoleGoToDesin&repId=" + repId + "&roleId=" + foldersIds;
                document.forms.searchForm.method = "POST";
                document.forms.searchForm.submit();
            }

            function creationOfReport1(repId){
                var fldObj=document.getElementsByName("userfldsList");
                var foldersIds="";

                if(fldObj.length!=0){
                    for(var i=0;i<fldObj.length;i++){
                        if(fldObj[i].checked){
                            foldersIds=foldersIds+","+fldObj[i].id;
                        }
                    }
                }
                if (foldersIds!=""){
                    foldersIds=foldersIds.substr(1,foldersIds.length);
                }
                document.forms.searchForm.action = "reportTemplateAction.do?templateParam=selectRoleGoToDesin1&repId="+repId+"&roleId="+foldersIds;
                document.forms.searchForm.method="POST";
                document.forms.searchForm.submit();
            }

            function creationOfReport2(repId){
                var fldObj=document.getElementsByName("userfldsList");
                var foldersIds="";

                if(fldObj.length!=0){
                    for(var i=0;i<fldObj.length;i++){
                        if(fldObj[i].checked){
                            foldersIds=foldersIds+","+fldObj[i].id;
                        }
                    }
                }
                if (foldersIds!=""){
                    foldersIds=foldersIds.substr(1,foldersIds.length);
                }
                document.forms.searchForm.action = "reportTemplateAction.do?templateParam=selectRoleGoToDesin2&repId="+repId+"&roleId="+foldersIds;
                document.forms.searchForm.method="POST";
                document.forms.searchForm.submit();
            }

            function groupMeassure() {
                document.forms.searchForm.action = 'srchQueryAction.do?srchParam=groupMeassure';
                document.forms.searchForm.submit();
            }
            function goSearchpage() {
                document.forms.searchForm.action = 'srchQueryAction.do?srchParam=getsearchPage';
                document.forms.searchForm.submit();
            }
            function pbBiManager() {
                document.forms.searchForm.action = 'srchQueryAction.do?srchParam=pbBiManager';
                document.forms.searchForm.submit();
            }
            function modlistDisp() {
                $("#modules").toggle("slow");
            }
            function modlistDisp1() {
                $("#modules").show();
            }
            function modlistclose() {
                $("#modules").hide();
            }
            $(window).load(function(){
                        <%if(status.equalsIgnoreCase("OK")){%>
//  alert("true");
$('head').append(' <link rel="stylesheet" type="text/css" href="css/vaccordian.css"/>');
$('head').append(' <link rel="stylesheet" type="text/css" href="css/vslide.css"/>');
                    $('#navMenu').css({"width":(($(window).width())/4)+70});
                    $('#vnavMenu').css({"width":(($(window).width())/4)+70});
                    $('#veraction_nav').css({"width":(($(window).width())/4)+70});
                    //$("#cbp-spmenu-s1").height($(window).height());
                    $("#naviInner").height($(window).height()-($("#showRightOCl").height()+25));
    <%}else{%>
//        alert("false")
    $('head').append(' <link rel="stylesheet" type="text/css" href="css/accordian.css"/>');
    $('head').append(' <link rel="stylesheet" type="text/css" href="css/slide.css"/>');
 //   $("#accordian").show();
    $('#navMenu').css({"height":$(window).height()});
    <%}%>
            	if($.browser.msie )   {
                    $("#accordian").remove();
                    $("#cbp-spmenu-s1").remove();
                    $('#logout').css({"display":"block"});

                } else{
                    var menuRightO = document.getElementById('cbp-spmenu-s1')
                    var menuRightR = document.getElementById('cbp-spmenu-s2')
//                    $('head').append(' <link rel="stylesheet" type="text/css" href="css/slide.css"/>');
                    $('#showRightO').css({"display":"block"});

                    $('head').append('<script type="text/javascript" src="<%= request.getContextPath()%>/JS/classie.js"/>');
                    $('head').append(' <script type="text/javascript" src="<%= request.getContextPath()%>/JS/alertify.min.js"/>');
                    $('head').append('<link rel="stylesheet" type="text/css" href="css/alertify.default.css"/>');
                    $('head').append(' <link rel="stylesheet" type="text/css" href="css/alertify.core.css"/>');

                var isChrome = navigator.userAgent.toLowerCase().indexOf('chrome') > -1;
                if(isChrome){
                    $('head').append('<link rel="icon" type="image/png" href="<%=request.getContextPath()%>/images/<%=companyIcon%>">');
                    $('head').append('<title><%=companyTitle%></title>');
                }
                    showRightO.onclick = function() {
                        classie.toggle(this, 'active');
                        classie.toggle(menuRightO, 'cbp-spmenu-open');
                        $("#shadow").fadeTo(300,0.15);
                        //				disableOther( 'showRight' );
                        //				disableOther( 'showLeftPush' );
                    };
                    showRightOCl.onclick = function() {
                        //classie.toggle(this, 'active');
                        //classie.toggle(menuRightO, 'cbp-spmenu-open');
                        $('#cbp-spmenu-s1').css({"width":0});
                        $("#shadow").fadeOut(300);
                        //				disableOther( 'showRight' );
                        //				disableOther( 'showLeftPush' );
//                        $(body).css("overflow","auto");
                    };

                    $("#shadow").click( function(){
                        //classie.toggle(this, 'active');
                        //classie.toggle(menuRightO, 'cbp-spmenu-open');
                        $('#cbp-spmenu-s1').css({"width":0});
                        $("#shadow").fadeOut(300);
//                        $(body).css("overflow","auto");
                    });

                }
            });
            $(document).ready(function() {
				$("#cbp-spmenu-s1").height("100%");
                init();
//                highLightRep();
                $(".navigateDialog").dialog({
                    autoOpen: false,
                    height: 620,
                    width: 820,
                    position: 'justify',
                    modal: true
                });
                $("#createReportNew").dialog({
                    autoOpen: false,
                    height: 200,
                    width: 300,
                    position: 'justify',
                    modal: true
                });
                $("#createReportNew1").dialog({
                    autoOpen: false,
                    height: 200,
                    width: 300,
                    position: 'justify',
                    modal: true
                });

                $("#createReportNew2").dialog({
                    autoOpen: false,
                    height: 200,
                    width: 300,
                    position: 'justify',
                    modal: true
                });
                $("#selectBussRole").dialog({
                    autoOpen: false,
                    height: 400,
                    width: 300,
                    position: 'justify',
                    modal: true
                });
                $("#displayLocaleLanguage2").dialog({
                        autoOpen: false,
                        height: 200,
                        width: 300,
                        position: 'justify',
                        modal: true
            });
            });
            <%if(status.equalsIgnoreCase("OK")){%>
            function rtMenuFn(){
                //classie.toggle(this, 'active');
				$('#cbp-spmenu-s1').height($(window).height());
                var menuRightO = document.getElementById('cbp-spmenu-s1');
                 $('#cbp-spmenu-s1').css({
					 right:0,
					 width:350
					 });
                        //classie.toggle(menuRightO, 'cbp-spmenu-open');
                        $("#shadow").fadeTo(300,0.15);

                $(body).css("overflow","hidden");
            }
            <%}else{%>
     function rtMenuFn(){
                //classie.toggle(this, 'active');
                var menuRightO = document.getElementById('cbp-spmenu-s1');
                $('#cbp-spmenu-s1').css({
					 height:$(window).height(),
					 right:0,
					 width:240
					 });
               classie.toggle(menuRightO, 'cbp-spmenu-open');
                $("#shadow").fadeTo(300,0.15);
            }
    <%}%>
            function initNavi() {
                if ($.browser.msie == true) {

                    $("#navigateDialog").dialog({
                        autoOpen: false,
                        height: 620,
                        width: 820,
                        position: 'justify',
                        modal: true
                    });
                }
                else {
                    $("#navigateDialog").dialog({
                        autoOpen: false,
                        height: 460,
                        width: 820,
                        position: 'justify',
                        modal: true
                    });
                }


            }
            function goGlobe() {
                initNavi();
                $("#navigateDialog").dialog('open');
                document.getElementById("reportstartIframe").src = "<%=request.getContextPath()%>/startPage.jsp";
            }
            function openstartpage() {
                //              edited by manik
                modlistclose();
                var frameObj = document.getElementById("startPageFrame");
                frameObj.src = "loginStart.jsp?checkUser=" + useridh + "&fromPage=startpage";
                $('#shadow').trigger('click');
                open1();
            }
            function open1() {
//                alert(2);
                $("#startPagePriv1").dialog('open');
                var frameObj = document.getElementById("startPageFrame");
                frameObj.src = "loginStart.jsp?checkUser=" + useridh + "&fromPage=startpage";
                classie.toggle("#shadow", 'active');
                        classie.toggle(menuRightO, 'cbp-spmenu-open');
                        $('#cbp-spmenu-s1').css({"width":0});
                        $("#shadow").fadeOut(100);
                        $(body).css("overflow","auto");
                         alert(2);
            }
            function init() {
                $("#startPagePriv1").dialog({
                    autoOpen: false,
                    height: 550,
                    width: 900,
                    position: 'justify',
                    modal: true
                });
            }
            function workBenchPage() {
                document.forms.searchForm.action = 'reportTemplateAction.do?templateParam=workBenchPage';
                document.forms.searchForm.submit();
            }
            function goHeadlinePage() {
                document.forms.searchForm.action = 'srchQueryAction.do?srchParam=headlinePage';
                document.forms.searchForm.submit();
            }

            function getXtend() {
                //                          var str='<%=request.getLocalAddr()%>';
                var str = '<%=InetAddress.getLocalHost().getHostAddress()%>';
                var port = '<%=request.getServerPort()%>';
                $("#xtendConnect").attr("href", "http://" + str + ":" + port + "/xtend/user");
            }
            function changeDefaultLangauge(){
                  $.ajax({
                    async:false,
                            data:{'userId':<%=userid%>},
                            url:'baseAction.do?param=getChangeDefaultLocale',
                            success: function(data){
//                            alert(data);

                            $("#displayLocaleLanguage2").html('');
                            if(data=="Already Availble"){

                            }else{
                            $("#displayLocaleLanguage2").html(data);
                            $("#displayLocaleLanguage2").dialog('open');
                            }
                             $("#shadow").trigger('click');
                            }
                        });
            }
            function setSelectedDefaultLocaleLanguage2(){
            var language = $("#myselect").val();
            var idLangCountryCode=$('#myselect option:selected').attr('id');
//            alert(language);
//            alert(idLangCountryCode);
            $("#displayLocaleLanguage2").dialog('close');

                            $.ajax({
                            async:false,
                            data:{'language_country_code': idLangCountryCode,'language_name': language,'userId':<%=userid%>},
                            url:'baseAction.do?param=setLanguageToCurrentUser',
                            success: function(data){

                            window.location.href=window.location.href;

                            }
                        });
            }
        </script>
    </head>
    <body onclick='hideEle();'>
        <div id="shadow" style="position: fixed; top: 0px; width: 100%; height: 100%; background-color: rgb(0, 0, 0); opacity: 0.6; z-index: 996; display: none;"></div>
        <%      boolean isQDEnableforUser = false;
                    boolean isXtendUser = false;
                    boolean isPowerAnalyserEnableforUser = false;
                    ServletContext context = getServletContext();
                    HashMap<String, UserStatusHelper> statushelper;
                    if (context.getAttribute("helperclass") != null) {
                        statushelper = (HashMap) context.getAttribute("helperclass");
                        UserStatusHelper helper = new UserStatusHelper();
                        if (!statushelper.isEmpty()) {
                            helper = statushelper.get(request.getSession(false).getId());
                            if (helper != null) {
                                isQDEnableforUser = helper.getQueryStudio();
                                isPowerAnalyserEnableforUser = helper.getPowerAnalyser();
                                isXtendUser = helper.getXtendUser();
                                userType = helper.getUserType();

                            }
                        }
                    }
                    String reportName = "";
                    ProgenParam connectionparam = new ProgenParam();
                    String reportId1 = (String) request.getAttribute("REPORTID");
                    Container container = Container.getContainerFromSession(request, reportId1);
                    PbReportCollection collect = new PbReportCollection();
                    if (container != null) {
                        collect = container.getReportCollect();
                        reportName = container.getReportName();
                    }
                    PbReportViewerDAO dao = new PbReportViewerDAO();
                    String lastupdateedate = "";
                    Connection con = null;
                    ArrayList<String> qryelements = collect.reportQryElementIds;
                    String elementId = "";
                    if (qryelements != null && !qryelements.isEmpty()) {
                        elementId = qryelements.get(0);
                        con = connectionparam.getConnection(elementId);
                        lastupdateedate = dao.getLastUpdatedDate(con, elementId);
                    }

                    //added by Nazneen for logo based on company

                    String companyId = "";
                    String compLogo = "";
                    String bussLogo = "";
                    String compTitle = "";
                    String bussTitle = "";
                    String rightWebSiteUrl = "";
                    String leftWebSiteUrl = "";
                     //Added by Ram 30Oct15 for Dynamic height/width of logo
                        String leftSideLogoHeight = "";
                        String leftSideLogoWidth = "";
                        String rightSideLogoHeight = "";
                        String rightSideLogoWidth = "";
                    if (session.getAttribute("isCompLogo") != null) {
                        String isCompLogo = session.getAttribute("isCompLogo").toString();
                        if (isCompLogo.equalsIgnoreCase("YES")) {
                            companyId = session.getAttribute("companyId").toString();
                            if (companyId != null && !companyId.equalsIgnoreCase("null") && !companyId.equalsIgnoreCase("")) {
                                compLogo = session.getAttribute("compLogo").toString();
                                bussLogo = session.getAttribute("bussLogo").toString();
                                compTitle = session.getAttribute("compTitle").toString();
                                bussTitle = session.getAttribute("bussTitle").toString();
                                rightWebSiteUrl = session.getAttribute("rightWebSiteUrl").toString();
                                leftWebSiteUrl = session.getAttribute("leftWebSiteUrl").toString();
                                leftSideLogoHeight=session.getAttribute("leftSideLogoHeight" ).toString();
                                leftSideLogoWidth=session.getAttribute("leftSideLogoWidth" ).toString();
                                rightSideLogoHeight=session.getAttribute("rightSideLogoHeight" ).toString();
                                rightSideLogoWidth=session.getAttribute("rightSideLogoWidth").toString();
                            }
                        }
                    }


//                        String headerTagStyle="";
//                        if(session.getAttribute("HEADER_FONT") != null && !session.getAttribute("HEADER_FONT").toString().equalsIgnoreCase("null") && !session.getAttribute("HEADER_FONT").toString().equalsIgnoreCase("") ){
//                            headerTagStyle="font-size:"+session.getAttribute("HEADER_FONT")+"px";
//                        }

                        //End!!!
                    //end of code by Nazneen for logo based on company
                        //
                        //
if (userType.equalsIgnoreCase("ANALZER")) {

        isPowerAnalyserEnableforUser = false;
        isQDEnableforUser = false;
        isXtendUser = false;
    }
        %>
        <!--Added By Ashutosh for Quick Nav-->
         <%if(status.equalsIgnoreCase("OK")){%>
           <div class="cbp-spmenu cbp-spmenu-vertical cbp-spmenu-right" id="cbp-spmenu-s1" style="width:0px;position:fixed;overflow: hidden; z-index: 999999999;">
            <h3  id="showRightOCl" style="cursor: pointer; color: #ffffff; background: rgba(138, 191, 66, 0.95);" > INSIGHTS </h3>
            <div id="naviInner" style="background-color: rgba(138, 191, 66, 0.95);overflow-x:hidden;overflow-y:auto;">
            <div id="accordian" style="display:none;">
                <ul id="navMenu" style="overflow-y: auto;">
		    <li style="background-color: rgba(138, 191, 66, 0.95);">
                        <h3 style="color:#ffffff;" onclick="goPaths('<%=homeVar%>')"><span style="color:#ffffff;"><i class=""></i></span>Favorite Reports</h3>
                    </li>
<!--                     <li style="border-bottom: none;">
                        <h3 style="color:#ffffff;background: rgba(138, 191, 66, 0.95);" onclick="noReportTags()"><span  style="color:#ffffff;"></span><%=TranslaterHelper.getTranslatedInLocale("Report_Tags", currentLocale)%></h3>
                        <ul class="tree" id="reports_nav" style="display: none;">
                        </ul>
                    </li> -->
            <%if(!insightsUserRole.equalsIgnoreCase("A")){%>
            <li style="background-color: rgba(138, 191, 66, 0.95);">
                  <h3 style="color:#ffffff;margin-left: 6px;background: rgba(138, 191, 66, 0.95);" onclick="goPaths('home.jsp#Report_Studio')"><span style="color:#ffffff;"><i class=""></i></span><%=TranslaterHelper.getTranslatedInLocale("report_studio", currentLocale)%></h3>
            </li>
            <%}%>

                </ul>
            </div>
            <div id="vaccordian">
                <ul id="vnavMenu" style="overflow-y: hidden;">
                    <li>
                        <!--                        <h3 style="color:#ffffff;" ><span style="color:#ffffff;display: none;"><i class="fa fa-compass"></i></span>Veraction Menu</h3>-->
                        <ul id="veraction_nav" style="display: block;">
                        </ul>
                    </li>
<!--                   for testing -->
<%if(insightsUserRole.equalsIgnoreCase("VPA")){%>
                <li>
                    <h3 style="color:#ffffff;margin-left: 6px;background: rgba(138, 191, 66, 0.95);"onclick="openPage('srchQueryAction.do?srchParam=pbBiManager')"><span  style="color:#ffffff;"><i class=""></i></span><%=TranslaterHelper.getTranslatedInLocale("bi_manager", currentLocale)%></h3>
                </li>
               <!-- <li>
                        <h3 style="color:#ffffff;margin-left: 6px;background: rgba(138, 191, 66, 0.95);" onmouseover="this.style.backgroundColor='rgba(138, 191, 66, 0.95)'" href="javascript:void(0)" title="Login Start Page" onclick="javascript:openstartpage()"><span style="color:#ffffff;"> <i class=""></i></span>Login Start Page</h3>
                    </li> -->
<%}%>
                    <li>
                        <h3 style="color:#ffffff;margin-left: 6px;background: rgba(138, 191, 66, 0.95);" onmouseover="this.style.backgroundColor='rgba(138, 191, 66, 0.95)'"onclick="javascript:veractionLogout();display:none;"><span style="color:#ffffff;"> <i class=""></i> </span><%=TranslaterHelper.getTranslatedInLocale("Logout", currentLocale)%></h3>
                    </li>
                </ul>
            </div>
            </div>
        </div>
          <%}else{%>

        <div class="cbp-spmenu cbp-spmenu-vertical cbp-spmenu-right" id="cbp-spmenu-s1" style="overflow: hidden; z-index: 999999999;">
            <h3  id="showRightOCl" onclick="" style="cursor: pointer; color: #ffffff; " > Quick Navigator </h3>
          <div id="accordian" style="display:none;">
                <ul id="navMenu" style="overflow-y: auto;">
                     <!------------------------------------------------------ ---------------
                      @Author : Prabal Pratap Singh
                      @Date : 08-DEC 2015
                      @Work : for adding some new menu here and chenge in color according to the theme
                     --------------------------------------------------------------------- -->
                    <!--  code Start-->
                    <li>
                        <h3 style="color:#ffffff;" onclick="goPaths('<%=homeVar%>')"><span style="color:#ffffff;"><i class="fa fa-home"></i></span><%=TranslaterHelper.getTranslatedInLocale("home", currentLocale)%></h3>
                    </li>
                    <li>
                        <h3 style="color:#ffffff;" onclick="goPaths('home.jsp#Report_Studio')"><span style="color:#ffffff;"><i class="fa fa-file-video-o fa-m point"></i></span><%=TranslaterHelper.getTranslatedInLocale("report_studio", currentLocale)%></h3>
                    </li>
                    <li>
                        <h3 style="color:#ffffff;" onclick="noReportTags()"><span  style="color:#ffffff;"><i class="fa fa-tags"></i></span><%=TranslaterHelper.getTranslatedInLocale("Report_Tags", currentLocale)%></h3>
                        <ul class="tree" id="reports_nav" style="display: none;">
                        </ul>
                    </li>
                    <!-- This is the list item that is open by default -->
                    <li>
                        <h3 style="color:#ffffff;" onclick="noFavoriteReports()"><span  style="color:#ffffff;"><i class="fa fa-heart"></i></span><%=TranslaterHelper.getTranslatedInLocale("favourite_report", currentLocale)%></h3>
                        <ul id="fav" style="display: none;">
                        </ul>
                    </li>
<!--                    <li>
                        <h3 style="color:#ffffff;" onclick="noOneView()"><span  style="color:#ffffff;"><i class="fa fa-newspaper-o"></i></span><%=TranslaterHelper.getTranslatedInLocale("oneview", currentLocale)%></h3>
                        <ul id="oneview" style="display: none;">
                        </ul>
                    </li>-->
                    <li>
                        <h3 style="color:#ffffff;" onclick="noDashBoards()"><span  style="color:#ffffff;"><i class="fa fa-tachometer"></i> </span><%=TranslaterHelper.getTranslatedInLocale("kpi_dashboard", currentLocale)%></h3>
                        <ul id="dash" style="display: none;">
                        </ul>
                    </li>
                    <li>
                        <h3 style="color:#ffffff;" onclick="openPage('home.jsp#Dashboard_Studio')"> <span  style="color:#ffffff;" > <i class="fa fa-industry"></i></i></span><%=TranslaterHelper.getTranslatedInLocale("dashboard_studio", currentLocale)%></h3>
                    </li>
<!--                     <li>
                         <h3 style="color:#ffffff;"  onclick="openPage('srchQueryAction.do?srchParam=getsearchPage')"> <span  style="color:#ffffff;" ><i class="fa fa-search"></i></span><%=TranslaterHelper.getTranslatedInLocale("search", currentLocale)%></h3>
                    </li> -->
                     <li>
                         <h3 style="color:#ffffff;"  onclick="openPage('pbBase.jsp?pagename=Query Studio&curntpage=Database Connection#Database_Connection')"> <span  style="color:#ffffff;" ><i class="fa fa-simplybuilt"></i></span><%=TranslaterHelper.getTranslatedInLocale("query_studio", currentLocale)%></h3>
                    </li>
<!--                    <li>
                        <h3 style="color:#ffffff;"  onclick="openPage('srchQueryAction.do?srchParam=headlinePage')"> <span  style="color:#ffffff;" ><i class="fa fa-header"></i></span><%=TranslaterHelper.getTranslatedInLocale("Heading_Page", currentLocale)%></h3>
                    </li> -->
                     <li>
                         <h3 style="color:#ffffff;"onclick="openPage('srchQueryAction.do?srchParam=pbBiManager')"><span  style="color:#ffffff;"><i class="fa fa-sellsy"></i></span><%=TranslaterHelper.getTranslatedInLocale("bi_manager", currentLocale)%></h3>
                    </li>
                  <!--  <li>
                        <h3 style="color:#ffffff;" onclick="openPage('srchQueryAction.do?srchParam=icalPage')"> <span  style="color:#ffffff;" ><i class="fa fa-calendar"></i></span><%=TranslaterHelper.getTranslatedInLocale("ical", currentLocale)%><sup><%=TranslaterHelper.getTranslatedInLocale("beta", currentLocale)%></sup></h3>
                    </li> -->
<!--                    <li>
                        <h3 style="color:#ffffff;"onclick="openPage('reportTemplateAction.do?templateParam=workBenchPage')"><span  style="color:#ffffff;"><i class="fa fa-tasks"></i>  </span><%=TranslaterHelper.getTranslatedInLocale("work_bench", currentLocale)%></h3>

                    </li>                -->
                    <!--                    Added by Dinanath-->
                    <li>
                        <h3 style="color:#ffffff;" onclick="javascript:changeDefaultLangauge();"><span style="color:#ffffff;"> <i class="fa fa-eye-slash"></i> </span><%=TranslaterHelper.getTranslatedInLocale("cdl", currentLocale)%></h3>
                    </li>
                     <li>
                        <h3 style="color:#ffffff;" href="javascript:void(0)" title="Login Start Page" onclick="javascript:openstartpage()"><span style="color:#ffffff;"> <i class="fa fa-check-square-o"></i></span><%=TranslaterHelper.getTranslatedInLocale("Login_Start_Page", currentLocale)%></h3>
                    </li>
                    <li>
                        <h3 style="color:#ffffff;" onclick="javascript:logout();display:none;"><span style="color:#ffffff;"> <i class="fa fa-sign-out"></i> </span><%=TranslaterHelper.getTranslatedInLocale("Logout", currentLocale)%></h3>
                    </li>


                     <!--  code End of Prabal Pratap singh-->
                </ul>
            </div>
        </div>

         <%}%>
        <!----------------------------------
        Author:-Faiz Ansari
        Details:- Below html codes are defining the header/banner section for every page.

        ------------------------------------>
        <!--- Code Start --->
                <div id='hdFixedDiv'></div>
      		<div id='hdFixedDiv2'>
                    <div id='hdContent'>
                        <%if(compLogo.equals("veractionLogo.png")){%>
                            <div id='logoDiv' class='fltL'><img onclick='ToglFiltrn(18953)' style='cursor:pointer;margin:10px;height:<%=leftSideLogoHeight%>;width:<%=leftSideLogoWidth%>' src='images/<%=compLogo%>'></div>
                        <%}else{%>
                            <div id='logoDiv' class='fltL'><img onclick='ToglFiltrn(18953)' style='cursor:pointer;margin-left:10px;height:100%;'src='images/pi_logo.png'></div>
                        <%}%>

                        <div id='headerData' class='fltL'>
                            <div id='hdFavRep' class='fltL'>
                                <div id='hdFavRep1'><span id='19054' style='font-size:12px' onclick='ToglFiltrn(19054)' title='Key Metrics'> Key Metrics </span></div>
                                <div id='hdFavRep2'><span id='18994' style='font-size:12px' onclick='ToglFiltrn(18994)' title='Geo Metrics'> Geo Metrics </span></div>
                                <div id='hdFavRep3'><span id='19153' style='font-size:12px' onclick='ToglFiltrn(19153)' title='Modes'> Modes </span></div>
                                <div id='hdFavRep4' onclick="goPaths('landingPage.jsp')" ><span style='font-size:12px'> Reports </span></div>
                    </div>
                            <div id='hdTimeCtrlDiv'></div>
                            <div id='rtMnuDiv'><i id='rtMenuBtn' onclick='rtMenuFn()' class='fa fa-ellipsis-v' style="display:none;"></i></div>
                            </div>
<!--                    <div style='float: left;margin-top: 48px;'><i onclick='rtMenuFn()' id='rtMenuBtn' style='cursor:pointer;margin-top:-15px;font-size: 20px' class='fa fa-ellipsis-v'></i>
                   </div>-->
                    <span class="hamburger" onclick='rtMenuFn()'></span>
                                    </div>
                    <div id='repCtrlDiv'>Report</div>
                                </div>
                <script>

                function ToglFiltrn(repId){//edited by Prabal
                    var url ="";
                          url = "<%=request.getContextPath()%>/reportViewer.do?reportBy=viewReport&REPORTID="+repId+"&action=open&reportDrill=Y","<%=openReportTab%>";
    		if(typeof parent.document.frmParameter!="undefined"){
    parent.document.frmParameter.action = url;
         
           document.frmParameter.submit();
           document.frmParameter.target = "";
			} else{
		window.open('<%=request.getContextPath()%>/reportViewer.do?reportBy=viewReport&REPORTID='+repId+'&action=open','<%=openReportTab%>');

               }
		
         
                   //window.open('<%=request.getContextPath()%>/reportViewer.do?reportBy=viewReport&REPORTID='+repId+'&action=open&reportDrill=Y','<%=openReportTab%>');
               }
               function highLightRep(){
                   if($("#hdFavRep1").children().attr("id") == <%=reportId1%>){
                       $("#hdFavRep1").addClass("udLine");
                   }
                   if($("#hdFavRep2").children().attr("id") == <%=reportId1%>){
                       $("#hdFavRep2").addClass("udLine");
                   }
                   if($("#hdFavRep3").children().attr("id") == <%=reportId1%>){
                       $("#hdFavRep3").addClass("udLine");
                   }
                   //alert($("#hdFavRep1").children().attr("id"))
               }

               function hideme(id){
                   //alert("h"+id);
                   if(id=="mmoreFilters"){
                        $("#tableOption").fadeIn();
                   }
                   $("#"+id).slideUp();
               }

                </script>
      <!--- Code End --->
        <!--ended By Ashutosh for Quick Nav-->
        <table id="headerTable" style="display:none;width:100%;">
            <tr valign="top">

                <td valign="top" style="height:25px;width:50%;" align="left">
               <!--       <%if (companyId != null && !companyId.equalsIgnoreCase("null") && !companyId.equalsIgnoreCase("")) {
                                    if (compLogo != null && !compLogo.equalsIgnoreCase("null") && !compLogo.equalsIgnoreCase("")) {

//added by manik for pi logo alignment
                                        if (LOGINID.equalsIgnoreCase("progen")) {%>
                    <a target="_blank" href="http://<%=leftWebSiteUrl%>"> <img alt="" width="50px" height="20px" border="0px"   title=<%=compTitle%> src="<%=request.getContextPath()%>/images/<%=compLogo%>"/></a>

<%} else {%>
               <a target="_blank" href="http://<%=leftWebSiteUrl%>"> <img alt=""  width="50px" height="20px" border="0px"  title=<%=compTitle%> src="<%=request.getContextPath()%>/images/<%=compLogo%>"/></a>
                    <%}} else {%>
<% if(!leftSideLogoWidth.equalsIgnoreCase("") && !leftSideLogoWidth.equalsIgnoreCase("0")&& leftSideLogoWidth!=null  && !leftSideLogoHeight.equalsIgnoreCase("") && !leftSideLogoHeight.equalsIgnoreCase("0") && leftSideLogoHeight!=null){%>
               <a target="_blank" href="http://<%=leftWebSiteUrl%>"> <img alt="" border="0px"  width=<%=leftSideLogoWidth%> height=<%=leftSideLogoHeight%>  title=<%=compTitle%> src="<%=request.getContextPath()%>/images/<%=compLogo%>"/></a>
               <%} else {%>

                    <a target="_blank" href="http://<%=leftWebSiteUrl%>"> <img alt=""  width="50px" height="20px" border="0px"  title=<%=compTitle%> src="<%=request.getContextPath()%>/images/<%=compLogo%>"/></a>
                        <%}}
                            }
                   else if (!isCompanyValid) {%>
     <!--                        <a target="_blank" href="<bean:message key="leftwebsite.url"/>"> <img alt="" border="0px"  width="40px" height="30px"  title="<bean:message key="piLogo.Title"/>" src="<%=request.getContextPath()%>/images/pi_logo.png"/></a>-->

                    <%}%>-->
                    <%if(container != null){%>
<!--                    <span style="font-size:10px;color:#2191C0;font-weight:bold;text-decoration: none;"> Welcome,<strong style="color:#008000;font-size:12px"> <%=session.getAttribute("LOGINID")%> </strong></span>-->
<!--                      <span style="font-size:10px;color:#2191C0;font-weight:bold;text-decoration: none;margin-left:9px"><strong style="color:black;font-size:16px"> <%=reportName%> </strong></span>-->
                      <%}%>
                </td>
                <!--                   <td valign="top" style="height:30px;width:40%" >

                                    </td>-->
                <!--Added by Ashutosh-->
                <td ><img src="images/nav.jpg" title="Click here for Quick Navigator" type="button" id="showRightO" data-toggle=".container" align="right" style="margin-top:1%; margin-right: 1%;cursor: pointer; display: none;"/></td>

                <td  style="width:2%;padding-top: 4px;" align="right">
                    <a class="noteicon" title="" style="text-decoration: none;" onmouseout="modlistclose()" onmouseover="modlistDisp1()" onclick="modlistDisp()" href="javascript:void(0)"><img alt="" height="22px" width="22px"  border="o" src="images/home_landing.png"></a>
                     <div id="modules" onmouseover="modlistDisp1()"  style="display:none;width:140px;height:auto;background-color:#FCFCFC;overflow: visible;position:absolute;text-align:left;z-index: 9999999;margin-left: 0px;margin-top: 0px;border-left: 1px solid black">
                        <table border='0' align='left' >
                            <tr><td>
                                    <table><tr><td><a href="javascript:void(0)" onclick="goPaths('<%=homeVar%>')" title="Home"  style="font-size: 11px;"> <%=TranslaterHelper.getTranslatedInLocale("home", currentLocale)%> </a></td></tr></table>
                                    <table><tr><td><a href="landingPageVeraction.jsp" onclick="" title="Landing Page"  style="font-size: 11px;color: #369"> <%=TranslaterHelper.getTranslatedInLocale("landingPage", currentLocale)%> </a></td></tr></table>
                                    <table><tr><td><a href="home.jsp" onclick="" title="Home"  style="font-size: 11px;color: #369"> <%=TranslaterHelper.getTranslatedInLocale("favourite_report", currentLocale)%></a></td></tr></table>
                                    <%if (homeVar != null && homeVar.equalsIgnoreCase("newHome.jsp")) {%>
                                    <table><tr><td><a href="javascript:void(0)" onclick="goPaths('home.jsp#RolesTab')" title="Business Roles"  style="font-size: 11px;"> <%=TranslaterHelper.getTranslatedInLocale("business_role", currentLocale)%> </a></td></tr></table>
                                    <%}%>
                                    <table><tr><td><a href="javascript:void(0)" onclick="goPaths('home.jsp#All_Reports')" style="font-size: 11px;"><%=TranslaterHelper.getTranslatedInLocale("my_reports", currentLocale)%></a></td></tr></table>
                                    <%--<table><tr><td><a href='javascript:void(0)' style="text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;" onclick="enableComp()" >Enable Comparision</a></td></tr></table> --%>
                                    <% if (isPowerAnalyserEnableforUser || userType.equalsIgnoreCase("ADMIN")) {%>
                                    <table><tr><td><a href="javascript:void(0)" onclick="goPaths('home.jsp#Dashboard_Studio')" style="font-size: 11px;"><%=TranslaterHelper.getTranslatedInLocale("dashboard_studio", currentLocale)%></a></td></tr></table>
                                    <table><tr><td><a href="javascript:void(0)" onclick="goPaths('home.jsp#Report_Studio')" style="font-size: 11px;"><%=TranslaterHelper.getTranslatedInLocale("report_studio", currentLocale)%></a></td></tr></table>
                                    <table><tr><td><a href="javascript:void(0)" onclick="goPaths('home.jsp#Report_Builder')" style="font-size: 11px;"><%=TranslaterHelper.getTranslatedInLocale("cust_rep", currentLocale)%></a></td></tr></table>
                                    <table><tr><td><a href="javascript:void(0)" onclick="goSearchpage()" style="font-size: 11px;"><%=TranslaterHelper.getTranslatedInLocale("search", currentLocale)%></a></td></tr></table>
                                    <% }
                                                if (isQDEnableforUser) {%>
                                    <table><tr><td><a href="javascript:void(0)" onclick="gotoDBCON('<%=request.getContextPath()%>')" title="Query Studio" style="font-size: 11px;"><%=TranslaterHelper.getTranslatedInLocale("query_studio", currentLocale)%></a></td></tr></table>
                                    <% }%>
                                    <!--                                                        <table><tr><td><a href="javascript:void(0)" onclick="goPortal()">Portals</a></td></tr></table>-->
                                    <!--                                                        <table><tr><td><a href="javascript:void(0)" onclick="goPaths('home.jsp#Scorecard')">Scorecard</a></td></tr></table>-->
                                    <!--                                                         <table><tr><td><a href="javascript:void(0)" onclick="goSearchpage()" style="font-size: 11px;">Search</a></td></tr></table>-->
                                    <% if (isPowerAnalyserEnableforUser || userType.equalsIgnoreCase("ADMIN")) {%>
                                    <table><tr><td><a href="javascript:void(0)" onclick="goPaths('home.jsp#Html_Reports')" style="font-size: 11px;"><%=TranslaterHelper.getTranslatedInLocale("html_reports", currentLocale)%></a></td></tr></table>
                                    <table><tr><td><a href="javascript:void(0)" onclick="goHeadlinePage()" style="font-size: 11px;"><%=TranslaterHelper.getTranslatedInLocale("headlines", currentLocale)%></a></td></tr></table>
                                    <table><tr><td><a href="javascript:void(0)" onclick="oneViewBy()" style="font-size: 11px;"><%=TranslaterHelper.getTranslatedInLocale("oneview", currentLocale)%></a></td></tr></table>
                                    <table><tr><td><a href="javascript:void(0)" onclick="IcalPage()" style="font-size: 11px;"><%=TranslaterHelper.getTranslatedInLocale("ical", currentLocale)%><sup><font color="#008000"><%=TranslaterHelper.getTranslatedInLocale("beta", currentLocale)%></font></sup></a></td></tr></table>
                                    <table><tr><td><a href="javascript:void(0)" onclick="workBenchPage()" style="font-size: 11px;"><%=TranslaterHelper.getTranslatedInLocale("work_bench", currentLocale)%></a></td></tr></table>
                                       <% } %>
                                    <!--                                                         <table><tr><td><a href="javascript:void(0)" onclick="goPaths('home.jsp#Headlines')" style="font-size: 11px;">Headlines (S)</a></td></tr></table>
                                                                                             <table><tr><td><a href="javascript:void(0)" onclick="goPaths('home.jsp#Dynamic_Headlines')" style="font-size: 11px;">Headlines (D)</a></td></tr></table>-->
                                    <!--<table><tr><td><a href="javascript:void(0)" onclick="editWall()" style="font-size: 11px;">Edit Wall </a></td></tr></table>-->
                                    <!--<table><tr><td><a  href="javascript:void(0)" onclick="goPaths('pbBIManager.jsp')" style="font-size: 11px;">BI Manager</a></td></tr></table>-->
                                    <!--<table><tr><td><a  href="javascript:void(0)" onclick="goPaths('pbBIManager.jsp')" >BI Manager</a></td></tr></table>-->
                                    <%  if (isQDEnableforUser || userType.equalsIgnoreCase("ADMIN")) {%>
                                    <table><tr><td><a  href="javascript:void(0)" style="font-size: 11px;" onclick="pbBiManager()" ><%=TranslaterHelper.getTranslatedInLocale("bi_manager", currentLocale)%></a></td></tr></table>
                                    <table><tr><td><a href="javascript:void(0)" title="Login Start Page" onclick="javascript:openstartpage()" style="font-size: 11px;"><%=TranslaterHelper.getTranslatedInLocale("login_startpg", currentLocale)%></a></td></tr></table>
                                    <% }%>
                                    <% if (isXtendUser) {%>
                                    <table><tr><td><a href="javascript:void(0)" onclick="workBenchPage()" style="font-size: 11px;"><%=TranslaterHelper.getTranslatedInLocale("work_bench", currentLocale)%></a></td></tr></table>
                                    <table><tr><td><a id="xtendConnect" href="javascript:void(0)"  onclick='getXtend()' style="font-size: 11px;" target="_blank"> <%=TranslaterHelper.getTranslatedInLocale("xtend", currentLocale)%> </a></td></tr></table>
                                    <% }%>
<!--                                    Added by Dinanath-->
                                    <table><tr><td><a  href="javascript:void(0)" style="font-size: 11px;" onclick="changeDefaultLangauge()" ><%=TranslaterHelper.getTranslatedInLocale("cdl", currentLocale)%></a></td></tr></table>
                                </td></tr>
                        </table>
                    </div>
                </td>
                <%-- <td align="right" width="1%" ><a href="javascript:void(0)" class="ui-icon1 ui-icon-home" onclick="javascript:gohome()" title="Home" style="text-decoration:none" ><img src="images/homeicon.png" height="20px" width="20px" "></a></td> --%>
                <td id="logout"align="right" width="2%" style="padding-top: 15px; display: none;"><a href="javascript:void(0)" class="extlinkicon" onclick="javascript:logout()" title="Logout" style="text-decoration:none"><img alt="" src="images/extlink.gif" width="25px" height="24px" border="o"/></a></td>

                <td valign="top" style="height:30px;width:5%;" align="right">

                       <!-- <img alt=""  width="120px" height="50px"  title="<bean:message key="progenLogo.Title"/>" src="<%=request.getContextPath()%>/images/<bean:message key="ProGen.businessLogo"/>"/> -->
                    <%if (companyId != null && !companyId.equalsIgnoreCase("null") && !companyId.equalsIgnoreCase("")) {
                                    if (bussLogo != null && !bussLogo.equalsIgnoreCase("null") && !bussLogo.equalsIgnoreCase("")) {%>
<!--        Added by Ram 30Oct15 for Dynamic height/width of Logo of Header.                         -->
 <% if(!rightSideLogoWidth.equalsIgnoreCase("") && !rightSideLogoWidth.equalsIgnoreCase("0")&& rightSideLogoWidth!=null && !rightSideLogoHeight.equalsIgnoreCase("") && !rightSideLogoHeight.equalsIgnoreCase("0") && rightSideLogoHeight!=null){%>
                    <a target="_blank" href="http://<%=rightWebSiteUrl%>"> <img alt="" border="0px"  width='80px' height='30x'  title=<%=compTitle%> src="<%=request.getContextPath()%>/images/<%=compLogo%>"/> </a>
                    <%} else {%>
                   <a target="_blank" href="http://<%=rightWebSiteUrl%>"> <img alt="" border="0px"  width='80px' height='30x'  title=<%=compTitle%> src="<%=request.getContextPath()%>/images/<%=compLogo%>"/> </a>
                    <%}}
                        } else {%>
<% if(!rightSideLogoWidth.equalsIgnoreCase("") && !rightSideLogoWidth.equalsIgnoreCase("0")&& rightSideLogoWidth!=null && !rightSideLogoHeight.equalsIgnoreCase("") && !rightSideLogoHeight.equalsIgnoreCase("0") && rightSideLogoHeight!=null){%>
                    <a target="_blank" href="<bean:message key="rightwebsite.url"/>"> <img alt="" border="0px"  width=<%=rightSideLogoWidth%> height=<%=rightSideLogoHeight%>  title="<bean:message key="progenLogo.Title"/>" src="<%=request.getContextPath()%>/images/<bean:message key="ProGen.businessLogo"/>"/> </a>
                    <%} else {%>
                    <a target="_blank" href="http://<%=rightWebSiteUrl%>"> <img alt="" border="0px"  width='80px' height='30x'  title=<%=compTitle%> src="<%=request.getContextPath()%>/images/<%=compLogo%>"/> </a>
                    <%}}%>
                </td>

            </tr>
        </table>
        <form name="searchForm" method="post" style="padding:0pt" action="">
            <!--                <table align="right" width="26%">

                                <tr align="right">
                                    <td align="left" width="12%" style="visibility: hidden">
                                                <font color="black">Last Updated:&nbsp;&nbsp;<%=lastupdateedate%></font>
                                            </td>
                                    <td align="right" width="7%"></td>

                                    <td  style="width:1%" align="right">
                                        <a href="javascript:void(0)" onclick="javascript:goGlobe()" style="font-size:10px;font-weight:bold;text-decoration: none;font-family:Georgia"> </a>
                                    <div align="left">
                                    <ul class="dropDownMenu1" style="width:100px;list-style-type: none">
                                        <li style="width:50%">
                                           <a href="javascript:openNavigation()"> <span style="float:right;font-size:10px;color:#336699;font-weight:bold;text-decoration: none;">Navigation</span></a>
                                    <a href='javascript:void(0)' title="modules" ></a>

                                    <ul style="position: absolute; list-style-type: none;width:100px">
                                        <li style="width:100%">
                                            <a href="javascript:void(0)" onclick="goPaths('home.jsp#RolesTab')">Business Roles</a>
                                               <a href="javascript:void(0)" onclick="javascript:gohome()" title="Home" > Home </a>
                                        </li>
                                        <li style="width:100%">
                                            <a href="javascript:void(0)" onclick="goPaths('home.jsp#All_Reports')">My Reports</a>
                                        </li>
                                        <li style="width:100%">
                                            <a href="javascript:void(0)" onclick="goPaths('AdminTab.jsp')">Administrator</a>
                                        </li>
                                        <li style="width:100%">
                                            <a href="javascript:void(0)" onclick="goPaths('home.jsp#Dashboard_Studio')">Dashboard Studio</a>
                                        </li>
                                        <li style="width:100%">
                                            <a href="javascript:void(0)" onclick="goPaths('home.jsp#Report_Studio')">Report Studio</a>
                                        </li>
                                        <li style="width:100%">
                                            <a href="javascript:void(0)" onclick="goPaths('pbBase.jsp')">Query Studio</a>
                                            <a href="javascript:void(0)" onclick="gotoDBCON('')" title="Query Studio">Query Studio</a>
                                        </li>
                                        <li style="width:100%">
                                         <a href="javascript:void(0)" onclick="goPortal()">Portals</a>
                                        </li>
                                        <li style="width:100%">
                                        <a   href="javascript:void(0)" onclick="goSearchpage()"   title="Search">Search</a>
                                        </li>
                                          <li style="width:100%">
                                              <a href="javascript:void(0)" onclick="goPaths('home.jsp#Scorecard')">ScoreCard</a>
                                          </li>
                                         <li style="width:100%">
                                             <a href="javascript:void(0)" onclick="goPaths('home.jsp#Html_Reports')">Html Reports</a>
                                         </li>
                                          <li style="width:100%">
                                              <a href="javascript:void(0)" onclick="goPaths('home.jsp#Headlines')">Headlines</a>
                                          </li>
                                        <li style="width:100%">
                                     <a  href="javascript:void(0)" onclick="goPaths('home.jsp#Global_Parameterss')">Global Parameters</a></li>
                                        <li style="width:100%">
                                              <a  href="javascript:void(0)" onclick="oneViewBy()" >One View<sup><font color="#008000">Beta</font></sup></a></li>
                                            <li style="width:100%"><a  href="metadataScheduler.jsp" title="Scheduler" >Scheduler</a></li>
                                         <li style="width:100%">
                                              <a  href="javascript:void(0)" onclick="IcalPage()">i-Cal<sup><font color="#008000">Beta</font></sup></a></li>
                                          <li style="width:100%">
                                              <a  href="javascript:void(0)" onclick="goPaths('pbBIManager.jsp')" >BI Manager</a></li>
                                    </ul></li></ul></div>
                                       |<a href="javascript:void(0)" onclick="javascript:gohome()" title="Home" style="font-size:10px;font-weight:bold;text-decoration: none;font-family:Georgia"> Home </a> |
                                        <a href="javascript:void(0)" onclick="javascript:logout()" title="Logout" style="font-size:10px;font-weight:bold;text-decoration: none;font-family:Georgia"> Logout </a>
                                <a class="noteicon" title="Modules" style="text-decoration: none;" onmouseout="modlistclose()" onmouseover="modlistDisp1()" onclick="modlistDisp()" href="javascript:void(0)"><img alt="" height="22px" width="22px"  border="o" src="images/iconnote.gif"></a>
                                        <div id="modules" onmouseover="modlistDisp1()"  style="display:none;width:125px;height:auto;background-color:white;overflow: visible;position:absolute;text-align:left;z-index: 9999999;margin-left: 0px;margin-top: 0px;">
                                                         <table border='0' align='left' >
                                                             <tr><td>
            <%--<table><tr><td><a href='javascript:void(0)' style="text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;" onclick="addIcal()" >Add New I-cal</a></td></tr></table>--%>
            <table><tr><td><a href="javascript:void(0)" onclick="goPaths('<%=homeVar%>')" title="Home"  style="font-size: 11px;"> Home </a></td></tr></table>
            <table><tr><td><a href="javascript:void(0)" onclick="goPaths('home.jsp#All_Reports')" style="font-size: 11px;">My Reports</a></td></tr></table>
            <%--<table><tr><td><a href='javascript:void(0)' style="text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;" onclick="enableComp()" >Enable Comparision</a></td></tr></table> --%>
            <% if (isPowerAnalyserEnableforUser || userType.equalsIgnoreCase("ADMIN")) {%>
            <table><tr><td><a href="javascript:void(0)" onclick="goPaths('home.jsp#Dashboard_Studio')" style="font-size: 11px;">Dashboard Studio</a></td></tr></table>
            <table><tr><td><a href="javascript:void(0)" onclick="goPaths('home.jsp#Report_Studio')" style="font-size: 11px;">Report Studio</a></td></tr></table>
            <% }
                        if (isQDEnableforUser) {%>
            <table><tr><td><a href="javascript:void(0)" onclick="gotoDBCON('<%=request.getContextPath()%>')" title="Query Studio" style="font-size: 11px;">Query Studio</a></td></tr></table>
            <% }%>
          <table><tr><td><a href="javascript:void(0)" onclick="goPortal()">Portals</a></td></tr></table>
          <table><tr><td><a href="javascript:void(0)" onclick="goPaths('home.jsp#Scorecard')">Scorecard</a></td></tr></table>
           <table><tr><td><a href="javascript:void(0)" onclick="goSearchpage()" style="font-size: 11px;">Search</a></td></tr></table>
           <table><tr><td><a href="javascript:void(0)" onclick="goPaths('home.jsp#Html_Reports')" style="font-size: 11px;">HTML Reports</a></td></tr></table>
           <table><tr><td><a href="javascript:void(0)" onclick="goPaths('home.jsp#Headlines')" style="font-size: 11px;">Headlines (S)</a></td></tr></table>
           <table><tr><td><a href="javascript:void(0)" onclick="goPaths('home.jsp#Dynamic_Headlines')" style="font-size: 11px;">Headlines (D)</a></td></tr></table>
           <table><tr><td><a href="javascript:void(0)" onclick="oneViewBy()" style="font-size: 11px;">One View</a></td></tr></table>
           <table><tr><td><a href="javascript:void(0)" onclick="IcalPage()" style="font-size: 11px;">i-Cal<sup><font color="#008000">Beta</font></sup></a></li></td></tr></table>
           <table><tr><td><a  href="javascript:void(0)" onclick="goPaths('pbBIManager.jsp')" style="font-size: 11px;">BI Manager</a></td></tr></table>
           <table><tr><td><a  href="javascript:void(0)" onclick="goPaths('pbBIManager.jsp')" >BI Manager</a></td></tr></table>
            <%  if (isQDEnableforUser || userType.equalsIgnoreCase("ADMIN")) {%>
            <table><tr><td><a  href="javascript:void(0)" style="font-size: 11px;" onclick="pbBiManager()" >BI Manager</a></td></tr></table>
            <% }%>
            <table><tr><td><a href="javascript:void(0)" title="Login Start Page" onclick="javascript:openstartpage()" style="font-size: 11px;">Login Start Page</a></td></tr></table>
        </td></tr>
</table>
</div>
</td>
            <%-- <td align="right" width="1%" ><a href="javascript:void(0)" class="ui-icon1 ui-icon-home" onclick="javascript:gohome()" title="Home" style="text-decoration:none" ><img src="images/homeicon.png" height="20px" width="20px" "></a></td> --%>
            <td align="right" width="1%"><a href="javascript:void(0)" class="extlinkicon" onclick="javascript:logout()" title="Logout" style="text-decoration:none"><img alt="" src="images/extlink.gif" width="25px" height="24px" border="o"/></a></td>

         </tr>
     </table>-->
        </form>
        <div id="navigateDialog" class="navigateDialog" title="Navigation" style="height:1px">
            <iframe id="reportstartIframe" frameborder="0" height="100%" width="800px" ></iframe>
        </div>

        <div id="selectBussRole" title="Select Business Role" style="display:none">

        </div>
        <div id="startPagePriv1"  title="Login Start Page" STYLE='display:none'>
            <!--         <div id="startPagePriv1"  title="Login Start Page" STYLE='display:block'>-->
            <iframe height="100%" width="100%" frameborder="0" id="startPageFrame"></iframe>
        </div>
        <!--Added By Ashutosh for Quick Nav-->
        <script type="text/javascript">
         var tags = "";
            $( window ).load(function() {

                // var menuTop = document.getElementById('cbp-spmenu-s3');
                var menuRightO = document.getElementById('cbp-spmenu-s1')
                var menuRightR = document.getElementById('cbp-spmenu-s2')
                showRightO = document.getElementById('showRightO')
                showRight = document.getElementById('showRight')
                showRightOCl = document.getElementById('showRightOCl')
                body = document.body;




                //showTop.onclick = function() {
                //
                //				classie.toggle( this, 'active' );
                //				classie.toggle( menuTop, 'cbp-spmenu-open' );
                //				//disableOther( 'showTop' );
                //			};
                //            showRightO.onclick = function() {
                //                classie.toggle(this, 'active');
                //                classie.toggle(menuRightO, 'cbp-spmenu-open');
                //                $("#shadow").fadeTo(300,0.15);
                ////				disableOther( 'showRight' );
                ////				disableOther( 'showLeftPush' );
                //            };
                //            showRightOCl.onclick = function() {
                //                classie.toggle(this, 'active');
                //                classie.toggle(menuRightO, 'cbp-spmenu-open');
                //                $("#shadow").fadeOut(300);
                ////				disableOther( 'showRight' );
                ////				disableOther( 'showLeftPush' );
                //            };
                //
                //            $("#shadow").click( function(){
                //                 classie.toggle(this, 'active');
                //                classie.toggle(menuRightO, 'cbp-spmenu-open');
                //                $("#shadow").fadeOut(300);
                //            });
                function trashOneview() {
                    //                          var menuTop = document.getElementById( 'cbp-spmenu-s3' );
//                    alert("trashhhh")
                    classie.toggle(this, 'active');
                    classie.toggle(menuTop, 'cbp-spmenu-open');
                }
                function  closeRightdiv() {
                    //                            classie.toggle( this, 'active' );
                    classie.toggle(menuRight, 'cbp-spmenu-open');
                    $('#cbp-spmenu-s1').css({"width":0});
                    //				disableOther( 'showRight' );
                }

                $( '.tree li' ).each( function() {
                    if( $( this ).children( 'ul' ).length > 0 ) {
                        $( this ).addClass( 'parent' );
                    }
                });
                // alert($( '.tree li' ).is(":visible"))
                $( '.tree li.parent > a' ).click( function( ) {
                    //alert("hello");
                    //$( this ).parent().toggleClass( 'active' );
                    // $( this ).parent().children( 'ul' ).slideToggle( 'fast' );
                    $( this ).parent().children( 'ul' ).slideToggle( 'fast' );
                });
                $.post('<%=request.getContextPath()%>/reportViewer.do?reportBy=getDataCall&userId=<%=userid%>',
                function(data) {

                    var data1 = JSON.parse(data);
                    var tags = "";
                    //alert(JSON.stringify(data));
                    var len = data1.length;
                    //alert(data1.length);
                    <%if(status.equalsIgnoreCase("OK")){%>
                    for (var i = 0; i < data1.length; i++) {
                        tags += "<li class='parent'style='background: rgba(100, 164, 14, 0.9);border-bottom: none;'><a href='#' style='text-align: left;font-size: 15px;margin-left: 10px;' onclick='slide_ul(tagReport"+ i +")'>" + data1[i]["Region"] + "</a><ul id='tagReport" + i + "' style='display: none;'></ul></li>";
                        //                 tags +="<a href='#'>"+data1[i]["Region"]+"</a>";
                    }
                    <%}else{%>
                         for (var i = 0; i < data1.length; i++) {
                        tags += "<li class='parent'><a href='#' style='text-align: left;' onclick='slide_ul(tagReport"+ i +")'>" + data1[i]["Region"] + "</a><ul id='tagReport" + i + "' style='display: none;'></ul></li>";
                        //                 tags +="<a href='#'>"+data1[i]["Region"]+"</a>";
                    }
                    <%}%>
                    $("#reports_nav").append(tags);

                    //alert($("#reports_nav").width());
                    //for veraction
                   if(1==2){
                   for (var j = 0; j < len; j++) {
                        displayReportsTags(data1[j]["Id"], len, j);
                    }
                   }
                });
                function displayReportsTags(tagId, len, k) {
                    $.ajax({
                        type: 'POST',
                        async: false,
                        cache: false,
                        // timeout: 30000,
                        url: '<%=request.getContextPath()%>/reportViewer.do?reportBy=getTagsBlocks&userId=' +<%=userid%> + '&tagId=' + tagId,
                        success: function(data) {
                            alert("intags")
                            var data2 = JSON.parse(data);
                            //alert("..."+JSON.stringify(data2["reportId"].length));
                            var len = data2["reportId"].length;

                            var div = '';
                             <%if(status.equalsIgnoreCase("OK")){%>
                                     for (var i = 0; i < len; i++) {
                                //                       alert(data2["reportId"][i]);
                                if(data2["tagType"][i]=="R"){
                                    div += "<li style='background: rgba(138, 191, 66, 0.95) none repeat scroll 0% 0%;' onmouseover=\"this.style.backgroundColor='rgba(138, 191, 66, 0.95)'\"><a href='#' style='font-size: 14px;margin-left: 20px;' onclick='reportsPath(" + data2["reportId"][i] + ")'>" + data2["tagShortDesc"][i] + "</a></li>";
                                }else if(data2["tagType"][i]=='D'){
                                    div += "<li style='background: rgba(138, 191, 66, 0.95) none repeat scroll 0% 0%;'><a href='#' style='font-size: 14px;margin-left: 20px;' onclick='openDashboard(\""+data2["tagShortDesc"][i]+"\","+data2["reportId"][i]+")'>" + data2["tagShortDesc"][i] + "</a></li>";

                                }
                                else if(data2["tagType"][i]=='T'){
                                    div += "<li style='background: rgba(138, 191, 66, 0.95) none repeat scroll 0% 0%;'><a href='#' style='font-size: 14px;margin-left: 20px;' onclick='openTimeDashboard(\""+data2["tagShortDesc"][i]+"\","+data2["reportId"][i]+")'>" + data2["tagShortDesc"][i] + "</a></li>";

                                }else if(data2["tagType"][i]=='O'){
                                    //div += "<li><a href='#' onclick='oneViewPath('" + data2["reportId"][i] +"','"+ data2["tagShortDesc"][i] + "')'>" + data2["tagShortDesc"][i] + "</a></li>";
                                    div += "<li style='background: rgba(138, 191, 66, 0.95) none repeat scroll 0% 0%;'><a href='#' style='font-size: 14px;margin-left: 20px;' onclick='openOneView("+data2["reportId"][i]+")'>" + data2['tagShortDesc'][i] + "</a></li>";
                                }

                            }
                                     <%}else{%>
                            for (var i = 0; i < len; i++) {
                                //                       alert(data2["reportId"][i]);
                                if(data2["tagType"][i]=="R"){
                                    div += "<li ><a href='#' style='background: gray none repeat scroll 0% 0%;' onclick='reportsPath(" + data2["reportId"][i] + ")'>" + data2["tagShortDesc"][i] + "</a></li>";
                                }else if(data2["tagType"][i]=='D'){
                                    div += "<li><a href='#' style='background: gray none repeat scroll 0% 0%;' onclick='openDashboard(\""+data2["tagShortDesc"][i]+"\","+data2["reportId"][i]+")'>" + data2["tagShortDesc"][i] + "</a></li>";
                                }
                                else if(data2["tagType"][i]=='T'){
                                    div += "<li><a href='#' style='background: gray none repeat scroll 0% 0%;' onclick='openTimeDashboard(\""+data2["tagShortDesc"][i]+"\","+data2["reportId"][i]+")'>" + data2["tagShortDesc"][i] + "</a></li>";
                                }else if(data2["tagType"][i]=='O'){
                                    //div += "<li><a href='#' onclick='oneViewPath('" + data2["reportId"][i] +"','"+ data2["tagShortDesc"][i] + "')'>" + data2["tagShortDesc"][i] + "</a></li>";
                                    div += "<li><a href='#' onclick='openOneView("+data2["reportId"][i]+")'>" + data2['tagShortDesc'][i] + "</a></li>";
                                }

                            }
                                          <%}%>

                            //                   alert(div);
                            //                   alert($("#tagReport"+k).attr("id"));
                            $("#tagReport" + k).append(div);


                        }
                    });
                }
   if(1==2){
                $.ajax({
                    type: 'GET',
                    async: false,
                    cache: false,
                    timeout: 30000,
                    //                            url: "<%=request.getContextPath()%>/reportTemplateAction.do?templateParam=GetAllfavReports",
                    url: '<%=request.getContextPath()%>/reportTemplateAction.do?templateParam=getFavReports',
                    success: function(data){
                        alert("in fav")
                        var json=eval('('+data+')');
                        //                                alert(json[0].reportId);
                        //                                var data1 = JSON.parse(data);
                        //                                var keys = Object.keys(data1);
                        var favReports = "";
                        for (var i = 0; i < json.length; i++)
                        {
                            //alert(keys[i])
                            // alert("..."+JSON.stringify(data1[keys[i]]));
                            favReports += "<li><a href='#' onclick='reportsPath(" + json[i].reportId + ")' style='display: block;'>" + json[i].reportName + "</a></li>";
                        }
                        //alert(favReports);
                        // addFavoriteReports(favReports);
                        $("#fav").append(favReports);

                    }
                });
                }
if(1==2){
                $.ajax({
                    type: 'GET',
                    async: false,
                    cache: false,
                    timeout: 30000,
                    url:"<%=request.getContextPath()%>/reportTemplateAction.do?templateParam=GetAllDB",
                    success:function(data)
                    {
                          alert("in DB");
                        //alert(($('#nav').width()))
                        var data1 = JSON.parse(data);
                        var keys = Object.keys(data1);
                        var dashBoards = "";
                        for (var i = 0; i < keys.length; i++)
                        {
                            //alert(keys[i])
                            //alert("..."+JSON.stringify(data1[keys[i]]));
//                            dashBoards += "<li onclick=dashBoardPath('" + keys[i] + "','"+ data1[keys[i]].split("::")[1] + "')><a href='#' >" + data1[keys[i]].split("::")[0] + "</a></li>";
                            dashBoards += "<li onclick='dashBoardPath(\"" + keys[i] + "\",\""+ data1[keys[i]] + "\")'><a href='#' >" + data1[keys[i]].split("::")[0] + "</a></li>";
                        }""

                        $("#dash").append(dashBoards);
                        // alert($("#dash").width());
                    }
                });
                }
// for veraction performance we comment the code fires the query every time
if(1==2){
                $.ajax({
                    type: 'GET',
                    async: false,
                    cache: false,
                    timeout: 30000,
                    url:"<%=request.getContextPath()%>/reportTemplateAction.do?templateParam=GetAllOneView",
                    success:function(data)
                    {    //alert(data);
                        alert("in one view")
                        var data1 = JSON.parse(data);
                        var keys = Object.keys(data1);
                        var one_view = "";
                        for (var i = 0; i < keys.length; i++)
                        {
                            //alert(keys[i])
                            //alert("..."+JSON.stringify(data1[keys[i]]));
                            one_view += "<li><a href='#' onclick=oneViewPath('" + keys[i] +"','"+ data1[keys[i]] + "')>" + data1[keys[i]] + "</a></li>";
                        }
                        $("#oneview").append(one_view);
                    }
                });
}
                $("#accordian h3").click(function() {
                    //Slide up all the link lists
                    // alert("hello");
                    $("#accordian ul ul").slideUp();
                    //Slide down the link list below the h3 clicked - only if it's closed
                    if(!$(this).next().is(":visible")) {
                        $(this).next().slideDown();
                    }
                })
                $('#accordian').css({"display":"block"});
            });
            
                                 /*KeepAlive SSO session till user using the application*/
                    <%if (status.equalsIgnoreCase("OK")) {
                            PbBaseDAO baseDAO = new PbBaseDAO();
                            HashMap checkUserSession = baseDAO.checkUserSession(ssoValue, application.getInitParameter("sso_keepAlive").toString());
                            if (checkUserSession != null) {
                                if (checkUserSession.get("status").toString().equalsIgnoreCase("ERROR")) {
                                    session.invalidate();
                    %>
                    veractionLogout();
                    <%
                     }
                 }
             }%>
           //Added by Ashutosh for Veraction Navigation menu
           <%if(status.equalsIgnoreCase("OK")){%>
            $.ajax({
                type:'POST',
                timeout: 30000,
                url:'<%=request.getContextPath()%>/userLayerAction.do?userParam=getSSOUserLinks',
                success:function(data)
                {   //alert(JSON.stringify(data));
                    var data1 = JSON.parse(data);
                    var keys = Object.keys(data1);
                    var vNav = "";
                    var div = "";
                    //                       alert((data1[keys[0]][0])["name"]);
                    //                       alert(data1[keys[0]].length);
                    //                        alert(data1[keys[0]].length)
                    //                       alert((((data1[keys[0]][0])["items"])[0])["name"]);
//                    alert(data1[keys[0]].length)
                    var len = data1[keys[0]].length;
//                    alert((data1[keys[0]][len-1])["url"]);
                    var logoffurl =((data1[keys[0]][len-1])["url"]);

                    $.ajax({
                type:'POST',
              timeout: 30000,
                url:"<%=request.getContextPath()%>/baseAction.do?param=setLogOffURL&logoffurl="+logoffurl+"&rd=rd",
                success:function(data){}
                    });

                    for (var i = 0; i < (data1[keys[0]].length-1); i++) {
                        if(typeof data1!="undefined" && data1!="" && typeof data1[keys[0]]!="undefined" && data1[keys[0]]!="" && typeof (data1[keys[0]][i])["label"]!="undefined" && (data1[keys[0]][i])["label"]!=""){
                        vNav += "<h3 style='background: rgba(138, 191, 66, 0.95);'><li><a href='#' onmouseover=\"this.style.backgroundColor='rgba(138, 191, 66, 0.95)'\" style='outline: -moz-use-text-color;text-align: left;background:rgba(138, 191, 66, 0.95);font-size: 17px;' onclick='slide_ul(nav_menu"+ i +")'>" + ((data1[keys[0]][i])["label"]) + "</a><ul id='nav_menu" + i + "' style='display: none;'></ul></li></h3>";
                    }
                    }

                    $("#veraction_nav").append(vNav);
                    for (var j = 0; j < data1[keys[0]].length; j++) {
			if(typeof data1[keys[0]][j]!="undefined" && data1[keys[0]][j]!=""){
                        for(var k = 0; k < (data1[keys[0]][j])["items"].length; k++){
                            div += "<li><a style='background:rgba(138, 191, 66, 0.95);font-size: 15px;margin-left: 15px;'href='"+(((data1[keys[0]][j])["items"])[k])["url"]+"' >" + (((data1[keys[0]][j])["items"])[k])["label"] + "</a></li>";
                        }
			}
                        $("#nav_menu"+j).append(div);
                        div = "";
                    }
                }
            });

    <%}%>
        </script>
        <script type="text/javascript">
            function noReportTags(){
                if(($("#reports_nav").width()) == 0){
                    //alert("No Report Tags Define!!");
                    alertify.error("No Report Tags Define!!");

                }

            }
            function noFavoriteReports(){
                if(($("#fav").width()) == 0){
                    // alert("No Favorite Reports!!");
                    alertify.error("No Favorite Reports!!");
                }
            }
            function noOneView(){
                if(($("#oneview").width()) == 0){
                    // alert("No One View Created!!");
                    alertify.error("No One View Created!!");
                }
            }
            function noDashBoards(){
                if(($("#dash").width()) == 0){
                    //alert("No KPI Dashboard Created!!");
                    alertify.error("No KPI Dashboard Created!!");
                }
            }
    $(document).mouseup(function (e)
    {
        var container = $("#modules");

        if (!container.is(e.target) // if the target of the click isn't the container...
            && container.has(e.target).length === 0) // ... nor a descendant of the container
        {
            container.hide(300);
        }
    });
      $("#headerTable").mouseover(function(){
     var container = $(".ui-multiselect-menu");
     container.hide(500);
});
      $("#headerTable").mouseover(function(){
     var container = $("#themeselect2");
     container.hide(500);
});
  function openPage(obj){
		window.open(obj,"_self");
	}
function getStartPage(){
     var ctxPath='<%=request.getContextPath()%>';
     var userId = '<%=USERID%>';
         $.ajax({
                    url: ctxPath+'/reportViewerAction.do?reportBy=getStartPage&user_Id='+userId,
                    success: function(data){
                     window.location.href=ctxPath+"/"+data;
                    }
                });
}

        </script>

        <!--end of code by Ashutosh -->
        <div id="displayLocaleLanguage2" title="Choose Default Language">

        </div>
    </body>
</html>
