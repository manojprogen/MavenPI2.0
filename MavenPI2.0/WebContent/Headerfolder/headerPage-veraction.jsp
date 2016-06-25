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

            String LOGINID = String.valueOf(session.getAttribute("LOGINID"));
            boolean isCompanyValid = false;
            String themeColor = "blue";
            if (session.getAttribute("theme") == null) {
                session.setAttribute("theme", themeColor);
            } else {
                themeColor = String.valueOf(session.getAttribute("theme"));
            }

            String userid = String.valueOf(session.getAttribute("USERID"));
            UserLayerDAO userdao = new UserLayerDAO();
            int USERID = Integer.parseInt((String) session.getAttribute("USERID"));
            String userType = userdao.getUserTypeForFeatures(USERID);
//            userType = "ANALZER"; // for udise
            PbDb pbdb = new PbDb();
            String userId = "";
            userId = String.valueOf(request.getSession(false).getAttribute("USERID"));
            String userstart = "select start_page from prg_ar_users where pu_id=" + userId;
            PbReturnObject userstartpbro = pbdb.execSelectSQL(userstart);
            userstartpbro.writeString();
            String homeVar = "";
            String strpage = userstartpbro.getFieldValueString(0, 0);
            if (strpage == null || strpage.equalsIgnoreCase("")) {
                homeVar = "home.jsp";
            } else if (strpage != null && strpage.equalsIgnoreCase("newHome.jsp")) {
                homeVar = "newHome.jsp";
            } else {
                homeVar = "home.jsp";
            }

%>
<html>
    <head>
        <script type="text/javascript" src="<%= request.getContextPath()%>/JS/global.js"></script>
	<link rel="stylesheet" href="css/font-awesome.min.css" type="text/css"/>



        <!--        <link rel="stylesheet" type="text/css" href="css/slide.css"/>-->


        <!--Added By Ashutosh for Quick Nav-->
        <style type="text/css">

            ul {padding-left: 0px; margin: 0px;}
            #accordian {
                background-color: #004050;
                box-shadow: 0 5px 15px 1px rgba(0,0,0,.6), 0 0 200px 1px rgba(255,255,255,.5);
                color: white;
                width: 300px;
            }

            /*heading styles*/


            #accordian h3 {
                /*fallback for browsers not supporting gradients
                background: #003040;
/               background: linear-gradient(#003040, #002535);*/
                cursor: pointer;
                font-size: 12px;
                line-height: 34px;
                padding: 0 10px;

            }

            /*heading hover effect*/


            #accordian h3:hover { text-shadow: 0 0 1px rgba(255,255,255,.7); }

            /*iconfont styles*/


            #accordian h3 span {
                font-size: 16px;
                margin-right: 10px;
            }

            /*list items*/


            #accordian li { list-style-type: none; }
/*            #accordian li ul {background: #3f3f3f;}*/
            #accordian li ul {background: #939393;}
            #accordian ul ul li a {
                color: white;
                /*  display: block;*/
                font-size: 12px;
                line-height: 27px;
                padding: 0 15px;
                text-decoration: none;
                transition: all 0.15s;
            }

            #accordian ul ul li a:hover {
                background-color: #696969;
                border-left: 5px solid #8BC34A;
            }

            #accordian li.active ul { display: block; }
            /*Faiz Ansari*/
            .udLine{border-bottom:3px solid #8bc34a;cursor:pointer}
            #hdFavRep{width:70%;margin-top:48px;float:left;}
            #hdFavRep div{border-color:#fff;float:left;width:20%;margin:0% 1% 0% 1% ;text-align: center; white-space: nowrap; overflow: hidden; font-size:13px; }
            /*End!!*/
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
            	if($.browser.msie )   {
                    $("#accordian").remove();
                    $("#cbp-spmenu-s1").remove();
                    $('#logout').css({"display":"block"});

                } else{
                    var menuRightO = document.getElementById('cbp-spmenu-s1')
                    var menuRightR = document.getElementById('cbp-spmenu-s2')
                    $('head').append(' <link rel="stylesheet" type="text/css" href="css/slide.css"/>');
                    $('#showRightO').css({"display":"block"});

                    $('head').append('<script type="text/javascript" src="<%= request.getContextPath()%>/JS/classie.js"/>');
                    $('head').append(' <script type="text/javascript" src="<%= request.getContextPath()%>/JS/alertify.min.js"/>');
                    $('head').append('<link rel="stylesheet" type="text/css" href="css/alertify.default.css"/>');
                    $('head').append(' <link rel="stylesheet" type="text/css" href="css/alertify.core.css"/>');
                    showRightO.onclick = function() {
                        classie.toggle(this, 'active');
                        classie.toggle(menuRightO, 'cbp-spmenu-open');
                        $("#shadow").fadeTo(300,0.15);
                        //				disableOther( 'showRight' );
                        //				disableOther( 'showLeftPush' );
                    };
                    showRightOCl.onclick = function() {
                        classie.toggle(this, 'active');
                        classie.toggle(menuRightO, 'cbp-spmenu-open');
                        $("#shadow").fadeOut(300);
                        //				disableOther( 'showRight' );
                        //				disableOther( 'showLeftPush' );
                    };

                    $("#shadow").click( function(){
                        classie.toggle(this, 'active');
                        classie.toggle(menuRightO, 'cbp-spmenu-open');
                        $("#shadow").fadeOut(300);
                    });

                }
            });
            $(document).ready(function() {
                var headerReportDesc = []
                var headerReportId = []
                  $.post('<%=request.getContextPath()%>/reportViewer.do?reportBy=getDataCall&userId=<%=userid%>',
                function(data){
                    var data1 = JSON.parse(data);
                    var tagID = data1[0]["Id"]

                 var ctxPath='<%=request.getContextPath()%>';
                    $.ajax({
                        type: 'GET',
                        async: false,
                        cache: false,
                        timeout: 30000,
                        url:ctxPath+'/reportViewer.do?reportBy=getTagsBlocks&userId='+<%=userid%>+'&tagId='+tagID,
                        success:function(json) {
                            var data = JSON.parse(json);
//                            alert(JSON.stringify(data))
                           //for report names
                           for(var i=0;i< 3;i++){
                               //having three report name to show in header for the very first tag
                               headerReportDesc.push(data["tagShortDesc"][i])
                               headerReportId.push(data["reportId"][i])
                               $("#hdFavRep"+(i+1)).html("<span id='"+data["reportId"][i]+"' style='font-size:12px;' onclick='ToglFiltrn(\""+data["reportId"][i]+"\")'>"+data["tagShortDesc"][i]+"</span>");
                           }
                        },
                        complete:function(){
                            highLightRep();
                        }
                    });

                });

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
            function rtMenuFn(){
                //classie.toggle(this, 'active');
                var menuRightO = document.getElementById('cbp-spmenu-s1');
                        classie.toggle(menuRightO, 'cbp-spmenu-open');
                        $("#shadow").fadeTo(300,0.15);
            }
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
                open1();
            }
            function open1() {
                init();
                $("#startPagePriv1").dialog('open');
                var frameObj = document.getElementById("startPageFrame");
                frameObj.src = "loginStart.jsp?checkUser=" + useridh + "&fromPage=startpage";
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
                            alert(data);
                            window.location.href=window.location.href;

                            }
                        });
            }
        </script>



        <title>pi</title>
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
                    //end of code by Nazneen for logo based on company
if (userType.equalsIgnoreCase("ANALZER")) {

        isPowerAnalyserEnableforUser = false;
        isQDEnableforUser = false;
        isXtendUser = false;
    }
        %>
        <!--Added By Ashutosh for Quick Nav-->
        <div class="cbp-spmenu cbp-spmenu-vertical cbp-spmenu-right" id="cbp-spmenu-s1" style="overflow: hidden; z-index: 999999999;">
            <h3  id="showRightOCl" onclick="" style="cursor: pointer; color: #ffffff; " > Quick Navigator </h3>
          <div id="accordian" style="display:none;">
                <ul>
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
                        <h3 style="color:#ffffff;" onclick="javascript:logout();display:none;"><span style="color:#ffffff;"> <i class="fa fa-sign-out"></i> </span><%=TranslaterHelper.getTranslatedInLocale("Logout", currentLocale)%></h3>
                    </li>


                     <!--  code End of Prabal Pratap singh-->
                </ul>
            </div>
        </div>

        <!----------------------------------
        Author:-Faiz Ansari
        Details:- Below html codes are defining the header/banner section for every page.
                    But only contain logo and menu. To get more options in header append your code
                    to "headerData" data id.
        ------------------------------------>
        <!--- Code Start --->
                <div id='hdFixedDiv' style='width:100%;height:75px;'></div>
      		<div style='position:fixed;top:0px;width:100%;height:100px;z-index:101;'>
<!--                    <div style='z-index:101;box-shadow:0px 0px 3px 3px #777;top:0px;width:100%;min-width:1000px;height:70px;background-color:#000;color:#fff;'>
                        <div style='width:10%;height:100%;float:left;'><img style='height:100%;margin-top: 2%;margin-left:20px;' src='images/piLogo_White.png'></div>
                        <div id='headerData' style='width:90%;float:left;height:100%'>
                            <div id='hdFavRep' >
                                <div id='hdFavRep1' class='myFont udLine' style='margin-left:10%' ><span> Report 1 </span></div>
                                <div id='hdFavRep2' class='myFont udLine' ><span>  Report 2 </span></div>
                                <div id='hdFavRep3' class='myFont udLine' ><span>  Report 3 </span></div>
                                <div id='hdFavRep4' class='myFont udLine' style='width:12%' onclick="goPaths('landingPage.jsp')" ><span style='font-size:12px'> Reports </span></div>
                            </div>
                            <div id='hdTimeCtrlDiv' style='width:28%;margin-top:48px;float:left'>
                                    </div>
                            <div style='float: left;margin-top: 48px;'><i onclick='rtMenuFn()' id='rtMenuBtn' style='cursor:pointer;margin-top:-15px;font-size: 20px' class='fa fa-ellipsis-v'></i></div>
                                </div>

                    </div>-->
<!--For veraction -->
                    <div style='z-index:101;box-shadow:0px 0px 3px 3px #777;top:0px;width:100%;min-width:1000px;height:70px;background-color:#000;color:#fff;'>
                        <div style='width:25%;height:80%;float:left;'><img style='height:100%;width:88%;margin-top: 2%;margin-left:20px;' src='images/logo.png'></div>
                        <div id='headerData' style='width:75%;float:left;height:100%'>
                            <div id='hdFavRep' style="width:65%" >
                                <div id='hdFavRep1' class='myFont udLine' style='margin-left:20px;width:25%;' ><span> Report 1 </span></div>
                                <div id='hdFavRep2' class='myFont udLine' style="width:25%;"><span>  Report 2 </span></div>
                                <div id='hdFavRep3' class='myFont udLine' style="width:25%;"><span>  Report 3 </span></div>
                                <div id='hdFavRep4' class='myFont udLine' style='width:12%' onclick="goPaths('landingPage.jsp')" ><span style='font-size:12px'> Reports </span></div>
                    </div>
                            <div id='hdTimeCtrlDiv' style='width:33%;margin-top:48px;float:left'>
                                    </div>
                            <div style='float: left;margin-top: 48px;'><i onclick='rtMenuFn()' id='rtMenuBtn' style='cursor:pointer;margin-top:-15px;font-size: 20px' class='fa fa-ellipsis-v'></i></div>
                                </div>

                    </div>
<!--End veraction -->
                    <div id='repCtrlDiv' style='display:none;height:30px;background-color: #f2f2f2;border-bottom:1px solid #d1d1d1'>Report</div>
                </div>
                <script>

               function ToglFiltrn(repId){
                   window.open('<%=request.getContextPath()%>/reportViewer.do?reportBy=viewReport&REPORTID='+repId+'&action=open','_blank');
               }
               function highLightRep(){
                   if($("#hdFavRep1").children().attr("id") == <%=reportId1%>){
                       $("#hdFavRep1").css("border-color","#8BC34A");
                   }
                   if($("#hdFavRep2").children().attr("id") == <%=reportId1%>){
                       $("#hdFavRep2").css("border-color","#8BC34A");
                   }
                   if($("#hdFavRep3").children().attr("id") == <%=reportId1%>){
                       $("#hdFavRep3").css("border-color","#8BC34A");
                   }
                   //alert($("#hdFavRep1").children().attr("id"))
               }

               function hideme(id){
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

                <td  style="width:2%;padding-top: 4px; align="right">
                    <a class="noteicon" title="" style="text-decoration: none;" onmouseout="modlistclose()" onmouseover="modlistDisp1()" onclick="modlistDisp()" href="javascript:void(0)"><img alt="" height="22px" width="22px"  border="o" src="images/home_landing.png"></a>
                     <div id="modules" onmouseover="modlistDisp1()"  style="display:none;width:140px;height:auto;background-color:#FCFCFC;overflow: visible;position:absolute;text-align:left;z-index: 9999999;margin-left: 0px;margin-top: 0px;border-left: 1px solid black">
                        <table border='0' align='left' >
                            <tr><td>
                                    <table><tr><td><a href="javascript:void(0)" onclick="goPaths('<%=homeVar%>')" title="Home"  style="font-size: 11px;"> <%=TranslaterHelper.getTranslatedInLocale("home", currentLocale)%> </a></td></tr></table>
                                    <table><tr><td><a href="landingPage.jsp" onclick="" title="Landing Page"  style="font-size: 11px;color: #369"> <%=TranslaterHelper.getTranslatedInLocale("landingPage", currentLocale)%> </a></td></tr></table>
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
            <iframe src="about:blank" id="reportstartIframe" frameborder="0" height="100%" width="800px" ></iframe>
        </div>

        <div id="selectBussRole" title="Select Business Role" style="display:none">

        </div>
        <div id="startPagePriv1"  title="Login Start Page" STYLE='display:none'>
            <!--         <div id="startPagePriv1"  title="Login Start Page" STYLE='display:block'>-->
            <iframe src="about:blank" height="100%" width="100%" frameborder="0" id="startPageFrame"></iframe>
        </div>
        <!--Added By Ashutosh for Quick Nav-->
        <script type="text/javascript">
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
                    alert("trashhhh")
                    classie.toggle(this, 'active');
                    classie.toggle(menuTop, 'cbp-spmenu-open');
                }
                function  closeRightdiv() {
                    //                            classie.toggle( this, 'active' );
                    classie.toggle(menuRight, 'cbp-spmenu-open');
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
                    for (var i = 0; i < data1.length; i++) {
                        tags += "<li class='parent'><a href='#' style='text-align: left;background:#0078A7;' onclick='slide_ul(tagReport"+ i +")'>" + data1[i]["Region"] + "</a><ul id='tagReport" + i + "' style='display: none;'></ul></li>";
                        //                 tags +="<a href='#'>"+data1[i]["Region"]+"</a>";
                    }

                    $("#reports_nav").append(tags);
                    //alert($("#reports_nav").width());
                    for (var j = 0; j < len; j++) {
                        displayReportsTags(data1[j]["Id"], len, j);
                    }
                });
                function displayReportsTags(tagId, len, k) {
                    $.ajax({
                        type: 'GET',
                        async: false,
                        cache: false,
                        // timeout: 30000,
                        url: '<%=request.getContextPath()%>/reportViewer.do?reportBy=getTagsBlocks&userId=' +<%=userid%> + '&tagId=' + tagId,
                        success: function(data) {
                            var data2 = JSON.parse(data);
                            //alert("..."+JSON.stringify(data2["reportId"].length));
                            var len = data2["reportId"].length;

                            var div = '';

                            for (var i = 0; i < len; i++) {
                                //                       alert(data2["reportId"][i]);
                                if(data2["tagType"][i]=="R"){
                                    div += "<li><a href='#' onclick='reportsPath(" + data2["reportId"][i] + ")'>" + data2["tagShortDesc"][i] + "</a></li>";
                                }else if(data2["tagType"][i]=='D'){
                                    div += "<li><a href='#'onclick='openDashboard(\""+data2["tagShortDesc"][i]+"\","+data2["reportId"][i]+")'>" + data2["tagShortDesc"][i] + "</a></li>";

                                }
                                else if(data2["tagType"][i]=='T'){
                                    div += "<li><a href='#'onclick='openTimeDashboard(\""+data2["tagShortDesc"][i]+"\","+data2["reportId"][i]+")'>" + data2["tagShortDesc"][i] + "</a></li>";

                                }else if(data2["tagType"][i]=='O'){
                                    //div += "<li><a href='#' onclick='oneViewPath('" + data2["reportId"][i] +"','"+ data2["tagShortDesc"][i] + "')'>" + data2["tagShortDesc"][i] + "</a></li>";
                                    div += "<li><a href='#' onclick='openOneView("+data2["reportId"][i]+")'>" + data2['tagShortDesc'][i] + "</a></li>";
                                }

                            }
                            //                   alert(div);
                            //                   alert($("#tagReport"+k).attr("id"));
                            $("#tagReport" + k).append(div);


                        }
                    });
                }

                $.ajax({
                    type: 'GET',
                    async: false,
                    cache: false,
                    timeout: 30000,
                    //                            url: "<%=request.getContextPath()%>/reportTemplateAction.do?templateParam=GetAllfavReports",
                    url: '<%=request.getContextPath()%>/reportTemplateAction.do?templateParam=getFavReports',
                    success: function(data){
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
                        ////alert(favReports);
                        // addFavoriteReports(favReports);
                        $("#fav").append(favReports);

                    }
                });

                $.ajax({
                    type: 'GET',
                    async: false,
                    cache: false,
                    timeout: 30000,
                    url:"<%=request.getContextPath()%>/reportTemplateAction.do?templateParam=GetAllDB",
                    success:function(data)
                    {
                        //  alert(data);
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

                $.ajax({
                    type: 'GET',
                    async: false,
                    cache: false,
                    timeout: 30000,
                    url:"<%=request.getContextPath()%>/reportTemplateAction.do?templateParam=GetAllOneView",
                    success:function(data)
                    {    //alert(data);
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

        </script>

        <!--end of code by Ashutosh -->
        <div id="displayLocaleLanguage2" title="Choose Default Language">

        </div>
    </body>
</html>
