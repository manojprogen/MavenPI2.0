<%-- 
    Document   : dashboardPage
    Created on : Jan 9, 2015, 6:05:35 PM
    Author     : Bharagavi
--%>


<%@page import="java.net.InetAddress"%>

<%@page import="prg.db.PbReturnObject"%>
<%@page import="prg.db.PbDb"%>
<%@page import="prg.db.Container"%>
<%@page import="com.progen.report.PbReportCollection"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.sql.Connection"%>
<%@page import="utils.db.ProgenParam"%>
<%@page import="com.progen.users.UserLayerDAO"%>
<%@page import="com.progen.action.UserStatusHelper"%>
<%@page import="java.util.HashMap"%>
<%@page import="utils.db.ProgenConnection"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<jsp:useBean id="duration" scope="session" class="utils.db.ProgenParam"/>

<%
    boolean isCompanyValid = false;
    String themeColor = "blue";
    String headerreports="";
    if (session.getAttribute("theme") == null) {
        session.setAttribute("theme", themeColor);
    } else {
        themeColor = String.valueOf(session.getAttribute("theme"));
    }
    //added by manik for pi logo alignment
    String LOGINID=String.valueOf(session.getAttribute("LOGINID"));

    String userid = String.valueOf(session.getAttribute("USERID"));

    UserLayerDAO userdao = new UserLayerDAO();
    int USERID = Integer.parseInt((String) session.getAttribute("USERID"));
    
    String userType = userdao.getUserTypeForFeatures(USERID);

    PbDb pbdb = new PbDb();
    String userId = "";
    userId = String.valueOf(request.getSession(false).getAttribute("USERID"));
    String isKPIDashboard=(String)request.getAttribute("isKPIDashboard");
   if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)){
     headerreports = "SELECT a.report_id,a.report_name FROM prg_ar_report_master a,prg_ar_user_reports b WHERE a.report_id =b.report_id AND b.user_id= "+USERID+" AND a.iskpidash ='true' GROUP BY a.report_id,a.report_name ORDER BY a.report_id DESC limit 5;";
    }else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)){
     headerreports = "SELECT top (5) a.report_id,a.report_name FROM prg_ar_report_master a,prg_ar_user_reports b WHERE a.report_id =b.report_id AND b.user_id="+USERID+" AND a.iskpidash ='true' GROUP BY a.report_id,a.report_name ORDER BY a.report_id desc";
    // headerreports = "select report_id, report_name from prg_ar_report_master where report_id in(SELECT top(5) a.report_id FROM prg_ar_report_master a,prg_ar_user_reports b WHERE a.report_id  =b.report_id AND b.user_id  ="+USERID+" AND a.iskpidash='true' group by a.report_id order by a.report_id desc)";
  }else{
         headerreports = "SELECT a.report_id,a.report_name FROM prg_ar_report_master a,prg_ar_user_reports b WHERE a.report_id =b.report_id AND b.user_id="+USERID+" AND a.iskpidash ='true' and  ROWNUM < 6 GROUP BY a.report_id,a.report_name ORDER BY a.report_id desc";
        }



    PbReturnObject headerreports1 = pbdb.execSelectSQL(headerreports);

    String userstart = "select start_page from prg_ar_users where pu_id=" + userId;
    PbReturnObject userstartpbro = pbdb.execSelectSQL(userstart);
    userstartpbro.writeString();
    String homeVar = "";
    String roleid=(String)request.getAttribute("roleid");
    String strpage = userstartpbro.getFieldValueString(0, 0);
    if (strpage == null || strpage.equalsIgnoreCase("")) {
        homeVar = "home.jsp";
    } else if (strpage != null && strpage.equalsIgnoreCase("newHome.jsp")) { 
        homeVar = "newHome.jsp";
    } else {
        homeVar = "home.jsp";
    }


%>

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
    ProgenParam connectionparam = new ProgenParam();
   
     String reportId1 = (String) request.getAttribute("REPORTID");
     
    Container container = Container.getContainerFromSession(request, reportId1);
    PbReportCollection collect = new PbReportCollection();
    if (container != null) {
        collect = container.getReportCollect();
    }
   // PbReportViewerDAO dao = new PbReportViewerDAO();
    String lastupdateedate = "";
    Connection con = null;
    String Dashboardname="";
    Dashboardname=container.getReportName();
    ArrayList<String> qryelements = collect.reportQryElementIds;
    String elementId = "";
    if (qryelements != null && !qryelements.isEmpty()) {
        elementId = qryelements.get(0);
        con = connectionparam.getConnection(elementId);
     //   lastupdateedate = dao.getLastUpdatedDate(con, elementId);
    }
    String reportName = "";
    reportName = container.getReportName();
    String companyId = "";
    String compLogo = "";
    String bussLogo = "";
    String compTitle = "";
    String bussTitle = "";
    String rightWebSiteUrl = "";
    String leftWebSiteUrl = "";
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
            }
        }
    }
    HashMap map = (HashMap) request.getSession(false).getAttribute("PROGENTABLES");
    HashMap TableHashMap = null;
    StringBuffer sbufRoles = new StringBuffer("");
    if (map != null) {
        container = (Container) map.get(reportId1);
        HashMap ReportHashMap = container.getReportHashMap();
        TableHashMap = container.getTableHashMap();
        String[] repBisRoles = (String[]) ReportHashMap.get("ReportFolders");

        for (int k = 0; k < repBisRoles.length; k++) {
            sbufRoles.append("," + repBisRoles[k]);
        }
    }
    String reportDrill = container.getMsrDrillReportSelection();
     String tabId = "";
                String currentURL = "";
 if (request.getSession(false) != null && request.getSession(false).getAttribute("PROGENTABLES") != null) {


                try {
//                    tabId = request.getParameter("ReportId");
//                    currentURL = request.getParameter("currentURL");
                    currentURL = (String)request.getAttribute("currentURL");
//                    currentURL = (String)session.getAttribute("currentURL");
                    if (currentURL == null) {
                        currentURL = "";
                    }
    } catch (Exception exp) {
                    exp.printStackTrace();
                }
            } else {
                response.sendRedirect(request.getContextPath() + "/baseAction.do?param=logoutApplication");
            }%>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <meta name="description" content="Tab Styles Inspiration: A small collection of styles for tabs" />
        <meta name="keywords" content="tabs, inspiration, web design, css, modern, effects, svg" />
        <meta name="author" content="Codrops" />
        <link rel="shortcut icon" href="../favicon.ico">
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>
        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/themes/<%=themeColor%>/jquery-ui-1.7.3.custom.css" rel="stylesheet" />
        <link rel="stylesheet" type="text/css" href="css/normalize.css" />
        <link rel="stylesheet" type="text/css" href="css/demo.css" />
        <link rel="stylesheet" type="text/css" href="css/tabs.css" />
        <link rel="stylesheet" type="text/css" href="css/tabstyles.css" />
        <link rel="stylesheet" type="text/css" href="css/menuTab.css" />
        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/themes/<%=themeColor%>/TableCss.css" rel="stylesheet" />
        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/themes/<%=themeColor%>/pbReportViewerCSS.css" rel="stylesheet" />

        <link rel="stylesheet" type="text/css" href="css/animatedMenu.css" />
        <!--<link rel="stylesheet" href="css/menuStyle.css" />-->
        <script src="<%=request.getContextPath()%>/javascript/newUI/modernizr.custom.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/pbReportViewerJS.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/reportviewer/ReportViewer.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/jQuery/farbtastic12/farbtastic/farbtastic.js"></script>

        <script type="text/javascript">

            $(document).ready(function(){
            <%
                    long current_time = System.currentTimeMillis();
                    long st_time = Long.parseLong(session.getAttribute("rep_st_time").toString());
                    double total_time = (current_time - st_time) * Math.pow(10, -3);
                    double finalValue = Math.round(total_time * 100.0) / 100.0;
            %>

           $("li").filter(function() {
               return $(this).text() == 'Data Analysis';
           }).click(function() {
               var html1 = "";
               var st=[];
               st.push('<%=request.getContextPath()%>');
               st.push('<%=sbufRoles.toString().substring(1)%>');
               st.push('<%=reportId1%>');
               st.push('<%=reportDrill%>');
               $("#saveWithGraph").remove();
               html1 += '<ul class="drpcontent" id="themeselect">';
               html1 += "<li style='text-align;'>";
               html1 += "<a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='dispChangeTableColumns(\""+st[0]+"\","+st[1]+","+st[2]+")' >Edit Report Data</a>";
               html1 +="</li>";
               html1 += "<li style='text-align;'>";
               html1 += "<a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='editViewBy()' >Modify Report Layout</a>";
               html1 +="</li>";
               html1 += "<li style='text-align;'>";
               html1 += "<a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='reportDrillAssignment(\""+st[0]+"\","+st[2]+")' >Define Report Drill</a>";
               html1 +="</li>";
               html1 += "<li style='text-align;'>";
               html1 += "<a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='addCustomMeasure1(\""+st[0]+"\","+st[2]+")' >Define Custom Measure</a>";
               html1 +="</li>";
               <%if(container.isReportCrosstab() && !container.IsTimedasboard() ) { %>
               html1 += "<li style='text-align;'>";
               html1 += "<a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='difineCustomSeq("+st[1]+","+st[2]+")' >Custom Sequence</a>";
               html1 +="</li>";
                          <%}%>
               html1 += "<li style='text-align;'>";
               html1 += "<a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='openNewReportDtls(\""+st[0]+"\")' >Save As New Report</a>";
               html1 +="</li>";
//               $('#testCase').html("");

               $('#testCase').append(html1);
           });

           $("li").filter(function() {
               return $(this).text() == 'Graph Analysis';
           }).click(function() {
               $('#themeselect').empty();
               var html1 = "";
//               var graphFlag = "Graph";
               html1 += '<ul class="drpcontent" id="themeselect">';
               html1 += "<li style='text-align;'>";
               html1 += "<a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='editSingleCharts()' >Add Graph Object</a>";
               html1 +="</li>";
               html1 += "<li style='text-align;'>";
               html1 += "<a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='buildCharts()' >Add Graph</a>";
               html1 +="</li>";
//               html1 += "<li style='text-align;'>";
//               html1 += "<a data-color='color1' style='padding:10px 0px 0px 24px;' name='within' onclick='parent.selectDrill(this.name)' >Drill Down</a>";
//               html1 +="</li>";
//               html1 += "<li style='text-align;'>";
//               html1 += "<a data-color='color1' style='padding:10px 0px 0px 24px;' name='single' onclick='parent.selectDrill(this.name)' >Drill Across</a>";
//               html1 +="</li>";
//               html1 += "<li style='text-align;'>";
//               html1 += "<a data-color='color1' style='padding:10px 0px 0px 24px;' name='multi' onclick='parent.selectDrill(this.name)' >Multi-Select Drill</a>";
//               html1 +="</li>";
//               html1 += "<li style='text-align;'>";
//               html1 += "<a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='editMeasure1()' >Add Graph Below</a>";
//               html1 +="</li>";
               html1 += "<li id='saveWithGraph' style='text-align;'>";
               html1 += "<a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='saveXtCharts()' >Save All Graphs</a>";
               html1 +="</li>";
               html1 += "<li id='saveWithGraph' style='text-align;'>";
               html1 += "<a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='generateJsonData("+parent.$("#graphsId").val()+")' >Reset</a>";
               html1 +="</li>";
//               $('#themeselect').html("");
               $('#testCase').append(html1);
           });
           $("li").filter(function() {
               return $(this).text() == 'Trend Analysis';
           }).click(function() {
               $('#themeselect').empty();
               var html1 = "";
//               var trendflag = "Trend";
               html1 += '<ul class="drpcontent" id="themeselect">';
               html1 += "<li style='text-align;'>";
               html1 += "<a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='editSingleCharts()' >Add Graph Object</a>";
               html1 +="</li>";
               html1 += "<li style='text-align;'>";
               html1 += "<a data-color='color1' style='padding:10px 0px 0px 24px;' id ='addGraphDataTrendAnalysis' onclick='buildChartsInTrends("+parent.$("#graphsId").val()+")' >Add Trend Data</a>";
               html1 +="</li>";
//               html1 += "<li style='text-align;'>";
//               html1 += "<a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='editMeasure()' >Graph Drill: Std</a>";
//               html1 +="</li>";
//               html1 += "<li style='text-align;'>";
//               html1 += "<a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='editMeasure()' >Graph Drill: Within</a>";
//               html1 +="</li>";
//               html1 += "<li style='text-align;'>";
//               html1 += "<a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='editMeasure()' >Graph Drill: Multi</a>";
//               html1 +="</li>";
//               html1 += "<li style='text-align;'>";
//               html1 += "<a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='editMeasure()' >Add Graph Below</a>";
//               html1 +="</li>";
               html1 += "<li id='saveWithGraph' style='text-align;'>";
               html1 += "<a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='saveXtTrend()' >Save As New Trend</a>";
               html1 +="</li>";
               html1 += "<li id='reset' style='text-align;'>";
               html1 += "<a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='trendAnalysisActionJs("+parent.$("#graphsId").val()+")' >Reset</a>";
               html1 +="</li>";
//               $('#themeselect').html("");
               $('#testCase').append(html1);
           });

           $('.menu-item a').click(function(){
               $('.menu-item a').removeClass('active');
               $(this).addClass('active');
           });

//           var screenh=screen.height;
//           screenh=screenh*.56;
//           $("#tabel1").css('height', screenh);
           var iframe = document.getElementById("iframe1");
           var iframeContent = (iframe.contentWindow || iframe.contentDocument);
           //                    iframeContent.loadingMenu(text);

       });


       function editMeasure1(){
           alert("This Option Will Be Coming Soon.")
       }

//       function logout(){
//           document.forms.searchForm.action="<%=request.getContextPath()%>/baseAction.do?param=logoutApplication";
//           document.forms.searchForm.submit();
//       }
function selectDrill(drillType){
           $("#drilltype").val(drillType);
           if(drillType=="multi"){
               alert("Multi Drill Enabled");
           }
           else if(drillType=="single"){
               alert("Drill Across Enabled");
           }
           else if(drillType=="within"){
           alert("Drill Down Enabled");
       }
       }

        </script>

        <style class="cp-pen-styles">
            .container{
                /*margin-left: 2%;*/
                width: 100%;

            }
            .no-csstransforms .cn-button {
                display: none;
            }

        </style>
<!--        added by manik for search css-->
 <style type="text/css">

            #searchbox3
            {
/*                background-color: #eaf8fc;
                background-image: linear-gradient(#fff, #d4e8ec);
                    border-radius: 35px;
                border-width: 1px;
                border-style: solid;
                border-color: #c4d9df #a4c3ca #83afb7;
                width: 430px;
                height: 20px;
                padding: 1px;
                                margin: auto;
                overflow: hidden;  Clear floats */
/*                    margin-left: 25%;*/
            }
            #search3,
            #submit3 {
                float: left;
            }

            #search3 {
                padding-left: 7px;
                height: 18px;
                width: 330px;
                border: 1px solid #a4c3ca;
                font: normal 14px 'trebuchet MS', arial, helvetica;
                background: #f1f1f1;
                border-radius: 50px 3px 3px 50px;
                box-shadow: 0 1px 3px rgba(0, 0, 0, 0.25) inset, 0 1px 0 rgba(255, 255, 255, 1);
                margin-left: 5px;
            }

            /* ----------------------- */

            #submit3
            {
                /*    background-color: #6cbb6b;*/
                background-color: #33CCCC;
                /*    background-image: linear-gradient(#95d788, #6cbb6b);*/
                border-radius: 3px 50px 50px 3px;
                border-width: 1px;
                border-style: solid;
                border-color: #7eba7c #578e57 #447d43;
                box-shadow: 0 0 1px rgba(0, 0, 0, 0.3),
                    0 1px 0 rgba(255, 255, 255, 0.3) inset;
                height: 19px;
                margin: 0 0 0 10px;
                padding: 0;
                width: 70px;
                cursor: pointer;
                font: bold 12px Arial, Helvetica;
                /*    color: #23441e;*/
                color: white;
                text-shadow: 0 1px 0 rgba(255,255,255,0.5);
            }

            #submit3:hover {
                /*    background-color: #95d788;*/
                background-color: #3399CC;
                /*    background-image: linear-gradient(#6cbb6b, #95d788);*/
            }

            #submit3:active {
                /*    background: #95d788;*/
                background: #3399CC;
                outline: none;
                box-shadow: 0 1px 4px rgba(0, 0, 0, 0.5) inset;
            }

            /*#submit::-moz-focus-inner {
                   border: 0;   Small centering fix for Firefox
            }
            #search::-webkit-input-placeholder {
               color: #9c9c9c;
               font-style: italic;
            }*/


            #search3:-moz-placeholder {
                color: #9c9c9c;
                font-style: italic;
            }

            #search3:-ms-placeholder {
                color: #9c9c9c;
                font-style: italic;
            }
            #search3.placeholder {
                color: #9c9c9c !important;
                font-style: italic;
            }
        </style>
    <body>
        <table id="tabel1" class="container" >
<!--            <tr>
            <div id="header " class="container1" style="width: 100%;height: 50px; ">

                <a target="_blank" href="http://<%=leftWebSiteUrl%>"> <img alt="" border="0px"  width="354px" height="47px"  title=<%=compTitle%> src="<%=request.getContextPath()%>/images/<%=compLogo%>"/></a>
           added by manik for pi logo alignment 
                <%  if(LOGINID.equalsIgnoreCase("progen")){ %>
                    <a target="_blank" href="http://<%=leftWebSiteUrl%>"> <img alt="" border="0px"  width="40px" height="30px"  title=<%=compTitle%> src="<%=request.getContextPath()%>/images/<%=compLogo%>"/></a>

                    <%}else{    %>
                <a target="_blank" href="http://<%=leftWebSiteUrl%>"> <img alt="" border="0px"  width="354px" height="47px"  title=<%=compTitle%> src="<%=request.getContextPath()%>/images/<%=compLogo%>"/></a>
                   <%}%>


                <a target="_blank" href="http://<%=rightWebSiteUrl%>"> <img alt="" border="0px" align="right"  width="120px" height="47px"  title=<%=compTitle%> src="<%=request.getContextPath()%>/images/<%=bussLogo%>"/> </a>
                <a href="javascript:void(0)" class="extlinkicon" onclick="javascript:logout()" title="Logout" style="text-decoration:none; float: right; color: white; margin-top: 1%; margin-right: 8px"><img alt="LogOut" style="color: white" src="images/extlink.gif" width="25px" height="24px" border="o"/></a>
                <a href="landingPage.jsp" class="extlinkicon" onclick="" title="Landing Page" style="text-decoration:none; float: right; color: white; margin-top: 1%; margin-right: 8px"><img alt="Landing" style="color: white" src="images/home_landing.png" width="25px" height="24px" border="o"/></a>
            </div></tr>-->
    <tr>
    <div  class="container">
        <!--			<section>-->
        <!--<div id="Tabdiv" class="tabs tabs-style-linebox"  style="height:42px; background-image: url(images/blackbackground.gif)" >-->
        <div id="Tabdiv" class=""  style="height:auto" >
            <!--                                    style="height:40px"-->
            <!--					<nav>
                                                            <ul class="" style="height:40px"  >
                                                                <li style="background: linear-gradient(#009FE3, #361414) repeat scroll 0% 0% transparent;" ><a style="width:100%" href="#section-underline-1" onclick="loadingMenu('Table')" ><span><font size="3" face="verdana" style="font-size: 15px; color: white">Data Analysis</font></span></a></li>
                                                                <li style="background: linear-gradient(#009FE3, #361414) repeat scroll 0% 0% transparent;"><a href="#section-underline-2" onclick="loadingMenu('Graph')" style="width:100%" ><span><font size="3" face="verdana" style="font-size: 15px; color: white">Graph Analysis</font></span></a></li>
                                                                    <li style="background: linear-gradient(#009FE3, #361414) repeat scroll 0% 0% transparent;"><a href="#section-underline-3" onclick="loadingMenu('I')" style="width:100%"><span><font size="3" face="verdana" style="font-size: 15px; color: white">Report Indicator</font></span></a></li>
                                                                    <li style="background: linear-gradient(#009FE3, #361414) repeat scroll 0% 0% transparent;"><a href="#section-underline-4" onclick="loadingMenu()" style="width:100%"><span><font size="3" face="verdana" style="font-size: 15px; color: white">Performance Analysis</font></span></a></li>
                                                                    <li style="background: linear-gradient(#009FE3, #361414) repeat scroll 0% 0% transparent;"><a href="#section-underline-5" onclick="loadingMenu()" style="width:100%" ><span><font size="3" face="verdana" style="font-size: 15px; color: white">Top/Bottom Analysis</font></span></a></li>
                                                                    <li id="testCase" style="background: linear-gradient(#009FE3, #361414) repeat scroll 0% 0% transparent;"><a href="#section-underline-6" onclick="" style="width:100%"><span><font size="3" face="verdana" style="font-size: 15px; color: white">Options</font>
                                                                          </span></a>

                                                                   <ul style="top:0px;left:-8%;z-index: 1000000">
                                                                        <li onclick="check()" style="text-align: left;">Table Properties</li>
                                                                    <li style="text-align: left;">Graph Properties</li>
                                                                    <li style="text-align: left;">Filters</li>
                                                                    <li style="text-align: left;">Other</li>
                                                                    </ul>
                                                                    </li>
                                                                    <li>

                                                                        <img src="images/pdf.png" title="Download PDF" onclick="downloadPDF('<%=reportId1%>')" style="margin-top: 4px;cursor: pointer ">
                                                                        <img src="images/excel.png" title="Export Excel" onclick="downloadExcel('<%=reportId1%>')" style="margin-top: 4px; padding-left: 5px;cursor: pointer">
                                                                        <img src="images/email.png" title="Send Mail" onclick="openShareReportDiv()" style="margin-top: 4px; padding-left: 5px; cursor: pointer">
                                                                        <img src="images/sceduler.png" title="Scheduler" onclick="openScheduleReport()" style="margin-top: 4px; padding-left: 5px; cursor: pointer">
                                                                        <img src="images/filter.png" title="View/Apply Filter" style="margin-top: 4px; padding-left: 5px; cursor: pointer" onclick="callFilterDiv()">
                                                                    </li>

                                                                    <li><a href="#section-underline-7" style="width:140px"><span>Tab 7</span></a></li>
                                                            </ul>
                                                    </nav>-->
            <ul class="navbar color2" style="width:100%" >
               <li class="menu-item " style="width:12%;background: skyblue"><a href='reportViewer.do?reportBy=viewReport&REPORTID=<%=reportId1%>&action=open&isKPIDashboard=<%=isKPIDashboard%>' style="padding: 1px;"><span><font size="3" face="verdana" style="padding:0px 5px 0px 10px;font-size: 14px; color: white"><%=Dashboardname%></font></span></a></li>
              <%for(int i=headerreports1.rowCount-1; i>=0; i--){
                  if(!headerreports1.getFieldValueString(i, 0).equalsIgnoreCase(""))%>
                <li class="menu-item" style="width: 12%;z-index:00000;"><a style="padding: 1px;width:100%" href='reportViewer.do?reportBy=viewReport&REPORTID=<%=headerreports1.getFieldValueString(i, 0)%>&action=open&isKPIDashboard=<%=isKPIDashboard%>'><span><font size="3" face="verdana" style="padding:0px 5px 0px 10px;font-size: 14px; color: white"><%=headerreports1.getFieldValueString(i, 1)%></font></span></a></li>
                <%}%>
<!--                <li class="menu-item" style="width:15%"><a href="#section-underline-3!" onclick="loadingMenu('Indicator')" style="padding: 1px;width:100%"><span><font size="3" face="verdana" style="padding:0px 5px 0px 10px;font-size: 14px; color: white">Trend Analysis</font></span></a></li>-->
<!--                <li class="menu-item"><a href="#section-underline-3!" onclick="loadingMenu('Indicator')" style="padding:10px;width:100%"><span><font size="3" face="verdana" style="padding:0px 5px 0px 10px;font-size: 15px; color: white">Report Indicator</font></span></a></li>-->
<!--                <li class="menu-item"><a href="#section-underline-4!" onclick="loadingMenu('Performance')" style="padding:10px;width:100%"><span><font size="3" face="verdana" style="padding:0px 5px 0px 10px;font-size: 15px; color: white">Performance Analysis</font></span></a></li>-->
<!--                <li class="menu-item"><a href="#section-underline-5!" onclick="loadingMenu('Top/Bottom')" style="padding:10px;width:100%"><span><font size="3" face="verdana" style="padding:0px 5px 0px 10px;font-size: 15px; color: white">Top/Bottom Analysis</font></span></a></li>-->
                <li id="testCase" class="drpdown menu-item" style="width: 13%"><a href="#" style="padding: 1px;width:100%"><span><font size="3" face="verdana" style="font-size: 14px; color: white">Options</font></span></a>
                    <ul class="drpcontent" id="themeselect">
                        <li style='text-align'>
                            <a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='dispChangeTableColumnskpi("<%=request.getContextPath()%>","<%=sbufRoles.toString().substring(1)%>","<%=reportId1%>")' >Edit Report Data</a>
                        </li>
                        <li style='text-align'>
                            <a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='kpieditViewBy()' >Modify Report Layout</a>
                        </li>
                        <%if(container.IsTimedasboard()){%>
                        <li style='text-align'>
                            <a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='reportDrillAssignmentkpi1("<%=request.getContextPath()%>","<%=reportId1%>")' >Define Report Drill</a>
                        </li>
                        <%}else{%>
                        <li style='text-align'>
                            <a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='reportDrillAssignmentkpi("<%=request.getContextPath()%>","<%=reportId1%>")' >Define Report Drill</a>
                        </li>
                        <%}%>
                        <li style='text-align'>
                            <a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='editDashBoardTable("<%=request.getContextPath()%>","<%=roleid%>","<%=reportId1%>")'>Dashboard Properties</a>
                        </li>
                         <%if(!container.IsTimedasboard()){%>
                        <li style='text-align'>
                            <a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='showkpiTableProperties("<%=request.getContextPath()%>","<%=roleid%>","<%=reportId1%>")' >Custom Sequence</a>
                        </li>
                         <%}%>
                        <li style='text-align'>
                            <a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='openNewReportDtlskpi("<%=request.getContextPath()%>")' >Save As New Report</a>
                        </li>
                    </ul>       </li>
                
                <ul style="float:right;margin-left: 90%;margin-top: -1.3%;position: relative">
                    <li style="background-color: white; float: right"><span>
                            <img src="images/pdf.png" title="Download PDF" onclick="downloadPDF('<%=reportId1%>')" style="margin-top: 4px;cursor: pointer ">
                            <img src="images/excel.png" title="Export Excel" onclick="downloadExcel('<%=reportId1%>')" style="margin-top: 4px; padding-left: 5px;cursor: pointer">
                            <img src="images/email.png" title="Send Mail" onclick="openShareReportDiv()" style="margin-top: 4px; padding-left: 5px; cursor: pointer">
                            <img src="images/sceduler.png" title="Scheduler" onclick="openScheduleReport()" style="margin-top: 4px; padding-left: 5px; cursor: pointer">
                            <!--<img src="images/filter.png" title="View/Apply Filter" style="margin-top: 4px; padding-left: 5px; cursor: pointer" onclick="callFilterDiv()">-->
                        </span>
                    </li>
                </ul>

            </ul>



        </div>
    </div></tr>


</table>


</body>
</html>

