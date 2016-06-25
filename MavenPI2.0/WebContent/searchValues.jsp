<%--
    Document   : searchValues
    Created on : Aug 31, 2010, 4:27:53 PM
    Author     : Administrator
--%>
<%@page import="java.util.Locale,prg.util.screenDimensions,com.progen.action.UserStatusHelper,java.util.HashMap,java.util.ArrayList,prg.db.PbReturnObject,prg.db.PbDb,com.progen.i18n.TranslaterHelper"%>

<%
        Locale cle = null;
        cle = (Locale) session.getAttribute("UserLocaleFormat");

        String themeColor = "blue";
        if (session.getAttribute("theme") == null) {
            session.setAttribute("theme", themeColor);
        } else {
            themeColor = String.valueOf(session.getAttribute("theme"));
        }
          String ResultStatus = "block";
          String KPIStatus = "block";
          String topBtmStatus = "block";
          String TrendStatus = "block";
          String TrendChartStatus = "block";
          String RelatedReportStatus = "block";
             if (session.getAttribute("ResultStatus") == null) {
                session.setAttribute("ResultStatus", ResultStatus);
            } else {
                ResultStatus = String.valueOf(session.getAttribute("ResultStatus"));
            }
            if (session.getAttribute("KPIStatus") == null) {
                session.setAttribute("KPIStatus", KPIStatus);
            } else {
                KPIStatus = String.valueOf(session.getAttribute("KPIStatus"));
            }
             if (session.getAttribute("topBtmStatus") == null) {
                session.setAttribute("topBtmStatus", topBtmStatus);
            } else {
                topBtmStatus = String.valueOf(session.getAttribute("topBtmStatus"));
            }
            if (session.getAttribute("TrendStatus") == null) {
                session.setAttribute("TrendStatus", TrendStatus);
            } else {
                TrendStatus = String.valueOf(session.getAttribute("TrendStatus"));
            }
            if (session.getAttribute("TrendChartStatus") == null) {
                session.setAttribute("TrendChartStatus", TrendChartStatus);
            } else {
                TrendChartStatus = String.valueOf(session.getAttribute("TrendChartStatus"));
            }
            if (session.getAttribute("RelatedReportStatus") == null) {
                session.setAttribute("RelatedReportStatus", RelatedReportStatus);
            } else {
                RelatedReportStatus = String.valueOf(session.getAttribute("RelatedReportStatus"));
            }



%>
<%
        boolean showExtraTabs=true;
         boolean headLines=false;
         boolean portalViewer=false;
         boolean reportAnalyzer=false;
         boolean whatif=false;
         String userType = null;
         boolean isActive = false;

            //for clearing cache
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader ("Expires", 0);
            ServletContext context = getServletContext();
           // boolean isAxa = Boolean.parseBoolean(context.getInitParameter("isAxa"));
            screenDimensions dims =new screenDimensions();
                int pageFont,anchorFont;
                HashMap map =dims.getFontSize(session,request,response);
                ////.println("map--"+map.size());
                if(!String.valueOf(map.get("pageFont")).equalsIgnoreCase("NULL")){
                pageFont=Integer.parseInt(String.valueOf(map.get("pageFont")));
                anchorFont = Integer.parseInt(String.valueOf(map.get("pageFont")))+1;
                ////.println("pageFont--"+pageFont+"---anchorFont--"+anchorFont);
                }else{
                pageFont = 11;
                anchorFont = 12;
                ////.println("pageFont--"+pageFont+"---anchorFont--"+anchorFont);
                }



    boolean isQDEnableforUser=false;
    boolean isPowerAnalyserEnableforUser=false;
  //  boolean isOneViewEnableforUser=false;
   // boolean isScoreCardsEnableforUser=false;

    // ServletContext context=this.getServletConfig().getServletContext();
     HashMap<String,UserStatusHelper> statushelper;
     //statushelper=(HashMap)context.getAttribute("helperclass");
     UserStatusHelper helper=new UserStatusHelper();
     if(context.getAttribute("helperclass")!=null){
     statushelper=(HashMap)context.getAttribute("helperclass");
     if(!statushelper.isEmpty()){
        helper=statushelper.get(request.getSession(false).getId());
        if(helper!=null){
       // isPortalEnableforUser=helper.getPortalViewer();
        isQDEnableforUser=helper.getQueryStudio();
        isPowerAnalyserEnableforUser=helper.getPowerAnalyser();
       // isOneViewEnableforUser=helper.getOneView();
       // isScoreCardsEnableforUser=helper.getScoreCards();
        userType=helper.getUserType();
        }
     }
     }
    // 
     /*
UserStatusHelper ushelpers=new UserStatusHelper();
if (session.getAttribute("userstatushelper") != null) {
          ushelpers = (UserStatusHelper)session.getAttribute("userstatushelper");
         isActive = ushelpers.getIsActive();
          headLines =ushelpers.getHeadlines();
          portalViewer = ushelpers.getPortalViewer();
          reportAnalyzer = ushelpers.getReportAnalyzer();
          whatif = ushelpers.getWhatif();
          userType =  ushelpers.getUserType();
          
    }*/

if(session.getAttribute("USERID")==null || String.valueOf(map.get("Redirect")).equalsIgnoreCase("Yes")){
response.sendRedirect(request.getContextPath()+"/newpbLogin.jsp");
   }else{%>
<%
   String userid=String.valueOf(session.getAttribute("USERID"));
   
  String query1="select * from PRG_HOME_TABS where user_id="+userid;
       PbReturnObject retob1=new PbReturnObject();
       PbDb pbdb1 = new PbDb();
       ArrayList repid1=new ArrayList();
         ArrayList list1=new ArrayList();
         ArrayList reporttype1=new ArrayList();
      retob1=pbdb1.execSelectSQL(query1);
       for(int i=0;i<retob1.rowCount;i++)
         {
             repid1.add(retob1.getFieldValueString(i, 1));
             list1.add(retob1.getFieldValueString(i, 2));
             reporttype1.add(retob1.getFieldValueString(i, 3));

         }
String contextPath=request.getContextPath();
%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <script src="<%=contextPath%>/javascript/lib/jquery/js/jquery-1.7.2.min.js" type="text/javascript"></script>
         <script type="text/javascript" src="<%=contextPath%>/TableDisplay/JS/pbTableMapJS.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/lib/jquery/autocomplete/jquery.autocomplete-min.js"></script>
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=themeColor %>/metadataButton.css" rel="stylesheet" />
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=themeColor %>/jquery-ui-1.7.3.custom.css" rel="stylesheet" />
        <link type="text/css" href="<%=contextPath%>/javascript/lib/jquery/autocomplete/styles.css" rel="stylesheet" />
        <link rel="stylesheet" href="<%=contextPath%>/tablesorter/themes/<%=themeColor %>/style.css" type="text/css" media="print, projection, screen" />
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=themeColor%>/pbReportViewerCSS.css" rel="stylesheet" />
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=themeColor%>/ReportCss.css" rel="stylesheet" />
<!--        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/themes/<%=themeColor%>/TableCss.css" rel="stylesheet" />-->
        <script type="text/javascript" src="<%=contextPath%>/javascript/pbReportViewerJS.js"></script>
           <style type="text/css">

               a {font-family:Verdana;cursor:pointer;text-decoration:none;font-size:12px;color:black}

               a:hover{text-decoration:inherit;font-weight:bold}

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
                   overflow:auto;
            }
           </style>

        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Search Page</title>
      
    </head>
    <body>
        <form name="searchForm" action="searchValues.jsp" id="reportForm" method="POST" >
         <div id="favSearch" title="Favourite Search" style="display:none">
             <table style="width:100%;" >
                 <tbody>
                     <tr>
                         <td>
                             <div style="width: 100%; height:300px; overflow-y: auto;">
                                 <table id="favSearchValues" width="100%">
                                       <tr>
                                           <th class="subTotalCell" style="text-align: center"><font style="font-size:small;"><%=TranslaterHelper.getTranslatedInLocale("Name", cle)%></font></th>
                                           <th class="subTotalCell" style="text-align: center"><font style="font-size:small;" ><%=TranslaterHelper.getTranslatedInLocale("Search_Text", cle)%></font></th>
                                             </tr>
                                              <tbody id="favSearchId">

                                         </tbody>
                                 </table>
                             </div>
                         </td>
                    </tr>
                     <tbody>
             </table>

        </div>
        <div id="dispTabProp" title="Table Properties" style="display:none">
            <iframe id="dispTabPropFrame" NAME='dispTabPropFrame' width="100%" height="100%"  frameborder="0" SRC='#'></iframe>
        </div>
        <div id="showExports" title="Exports" style="display:none">
            <iframe id="showExportsFrame" NAME='showExportsFrame' width="100%" height="100%"  frameborder="0" SRC='#'></iframe>
        </div>

        <iframe name="widgetframe" id="widgetframe" style="display:none" src=""></iframe>

            <table style="width:100%">
                <tr>

                    <td valign="top" style="width:50%;">
                        <jsp:include page="Headerfolder/headerPage.jsp"/>
                    </td>
                </tr>
            </table>
<!--                     <form name="searchForm" method="post" style="padding:0pt">
            <table align="right">
                <tr align="right">
                    <td  style="height:10px;width:30%" align="right">
                        <a href="javascript:void(0)" onclick="javascript:goGlobe()" style="font-size:10px;color:#2191C0;;font-weight:bold;text-decoration: none;font-family:Georgia"> Navigation </a> |
                        <a href="javascript:void(0)" onclick="javascript:gohome()" style="font-size:10px;color:#2191C0;;font-weight:bold;text-decoration: none;font-family:Georgia"> Home </a> |
                        <a href="javascript:void(0)" onclick="javascript:logout()" style="font-size:10px;color:#2191C0;;font-weight:bold;text-decoration: none;font-family:Georgia"> Logout </a>
                    </td>

                </tr>
            </table>
                      </form>-->
                         <br/><br/>
                         <div id="progressbar"></div>
            <div id="Searchdiv" class="tabs" style=" max-width: 100%; cursor: auto;">
                <div class="navtitle-hover" style=" max-width: 100%; cursor: auto; height: 20px;"><span> <font size="2"> <%=TranslaterHelper.getTranslatedInLocale("search", cle)%> </font><b> <sup style=""><i><%=TranslaterHelper.getTranslatedInLocale("beta", cle)%></i></sup></b></span> </div>
                <table style="width:100%">
                <tr valign="top">
                    <td valign="top">
                        <table style="width:100%;height:50px;">
                            <tr valign="top">
                                    <td><span><%=TranslaterHelper.getTranslatedInLocale("enter_keywords", cle)%>
                                            </span></td>
                            </tr>
                            <tr valign="top">
                                <td valign="top" width="60%">
                                    <table>
                                    <tr valign="top">
                                    <td colspan="2">
                                        <input type="text" class="myTextbox3" id="srchText" name="srchText" align="middle" style="width:380px;height:20px;">
                                        <%--<div id="srchdiv" class="ajaxboxstyle" style="height:200px;overflow-y:auto;display:none"></div>--%>
                                    </td>
                                    <td valign="TOP"><input class="navtitle-hover" style="height: 25px;width: 50px" type="button" name="Search"  value=" <%=TranslaterHelper.getTranslatedInLocale("search", cle)%> " onClick="javascript:searchVals()">
                                        &nbsp;<input class="navtitle-hover" style="height: 25px;width: 105px" type="button" name="Favourite Search"  value="<%=TranslaterHelper.getTranslatedInLocale("Favourite_Search", cle)%> " onclick="javascript:openfavSearch()">
                                    </td>
                                    </tr>
                                    </table>
                                </td>
                                <td id="SearchActions" style="display:none" valign="top" width="40%" align="right">
                                    <table cellpadding="0" cellspacing="0" >
                                    <tr valign="top">
                                        <td><a href="#" onClick="opensaveSearchDialog()"><%=TranslaterHelper.getTranslatedInLocale("Save_Search", cle)%></a></td>
                                        <td width="20px"></td>
                                        <td><a href="#" onclick="createReport()"><%=TranslaterHelper.getTranslatedInLocale("Create_Report", cle)%></a></td>
                                    </tr>
                                    </table>
                                </td>
                            </tr>

                        </table>
                    </td>
                </tr>
                <tr>
                    <td>
                        <%--height:800px;overflow-y:auto--%>
                        <div id="srchResults"  style="display:none;">
                            <table>
                                <tr>
                                    <td width="20%" valign="TOP" id="leftPaneHolder">
                                            <div id="leftPaneContents">
                                                <table width="100%">
                                                <tr>
                                                <td width="100%">
                                                    <div id="topBtmPane"  class="navsection">
                                                        <div style="width:100%" onclick="dispTopBottom()" title="Top/Bottom">
                                                            <table width="100%" class="navtitle1">
                                                            <tr>
                                                                <td><b style="font-family:verdana;font-weight:bold"><%=TranslaterHelper.getTranslatedInLocale("Top_Bottom", cle)%></b></td>
                                                                <td><span id="TextImg" class='ui-icon ui-icon-circle-triangle-s'   style='float:right;'></span></td>
                                                            </tr>
                                                            </table>
                                                            </div>
                                                        <div id="topBtmId" style="display:<%=String.valueOf(session.getAttribute("topBtmStatus"))%>">
                                                            <iframe src="#" scrolling="no" id="topBtmFrame" frameborder="0" style="width:100%;height:200px"></iframe>
                                                            </div>
                                                    </div>
                                                </td>
                                                </tr>
                                                <%--<tr>
                                                <td width="100%">
                                                    <div id="timeSeriesChartsPane"  class="navsection">
                                                        <div style="width:100%" onclick="dispTrendCharts()" title="Trends">
                                                            <table width="100%" class="navtitle1">
                                                            <tr>
                                                            <td><b style="font-family:verdana;font-weight:bold">Trends</b></td>
                                                            <td><span id="TextImg" class='ui-icon ui-icon-circle-triangle-s'   style='float:right;'></span></td>
                                                            </tr>
                                                            </table>
                                                        </div>
                                                        <div id="timeSeriesChartsPB"></div>
                                                        <div id="timeSeriesCharts" style="display:<%=String.valueOf(session.getAttribute("TrendChartStatus"))%>"></div>
                                                    </div>
                                                </td>
                                                </tr>--%>
                                                <tr>
                                                <td width="100%">
                                                    <div id="relatedMeasuresPane"  class="navsection">
                                                        <div style="width:100%" onclick="dispTrends()" title="Trend">
                                                            <table width="100%" class="navtitle1">
                                                            <tr>
                                                            <td><b style="font-family:verdana;font-weight:bold"><%=TranslaterHelper.getTranslatedInLocale("Trend", cle)%></b></td>
                                                            <td><span id="TextImg" class='ui-icon ui-icon-circle-triangle-s'   style='float:right;'></span></td>
                                                            </tr>
                                                            </table>
                                                        </div>
                                                        <div id="relatedMeasuresPB"></div>
                                                        <div id="relatedMeasures" style="display:<%=String.valueOf(session.getAttribute("TrendStatus"))%>"></div>
                                                    </div>
                                                </td>
                                                </tr>
                                                  <tr>
                                                        <td width="100%">
                                                            <div id="kpiPane" class="navsection" style="display: none">
                                                            <div style="width:100%" onclick="dispKPI()" title="Measure KPIs">
                                                                 <table width="100%" class="navtitle1">
                                                                    <tr>
                                                                        <td><b style="font-family:verdana;font-weight:bold"><%=TranslaterHelper.getTranslatedInLocale("Measure_KPIs", cle)%></b></td>
                                                                        <td><span id="TextImg" class='ui-icon ui-icon-circle-triangle-s'   style='float:right;'></span></td>
                                                                    </tr>
                                                                </table>
                                                            </div>
                                                                <div id="MeasureKPItable" style="display:<%=String.valueOf(session.getAttribute("KPIStatus"))%>">
                                                            <table>
                                                                <tr>
                                                                    <td width="50%">
                                                                        <div id="monthlyKpisPB"></div>
                                                                        <div id="monthlyKpis"></div>
                                                                    </td>
                                                                    <td width="50%">
                                                                        <div id="quarterlyKpisPB"></div>
                                                                        <div id="quarterlyKpis"></div>
                                                                    </td>
                                                                </tr>
                                                            </table>
                                                            </div>
                                                            </div>
                                                        </td>
                                                    </tr>
                                                </table>
                                            </div>
                                    </td>
                                    <td width="70%" valign="TOP" id="centerPaneHolder">
                                        <div id="progenTablePB"></div>
                                            <div id="centerPaneContents" style="height:100%;">
                                                <table width="100%">
                                                    <tr>
                                                        <td width="100%">
                                                        <div id="progentablePane" class="navsection">
                                                            <div style="width:100%" onclick="dispTable()" title="Results">
                                                                 <table width="100%" class="navtitle1">
                                                                    <tr>
                                                                        <td><b style="font-family:verdana;font-weight:bold"><%=TranslaterHelper.getTranslatedInLocale("Results", cle)%></b></td>
                                                                        <td><span id="TextImg" class='ui-icon ui-icon-circle-triangle-s'   style='float:right;'></span></td>
                                                                    </tr>
                                                                </table>
                                                            </div>
                                                            <div id="tabTable" style="display:<%=String.valueOf(session.getAttribute("ResultStatus"))%>">

                                                        <IFRAME NAME='iframe1' SCROLLING="no" style="width:100%;height:700px;" ID='iframe1' FRAMEBORDER="0" SRC='#'></IFRAME>
                                                        </div>
                                                        </div>
                                                        </td>
                                                    </tr>
                                                 </table>
                                            </div>
                                    </td>
                                    <td width="10%" valign="TOP" id="rightPaneHolder">
                                        <div id="rightPane" class="navsection">
                                            <div style="width:100%" onclick="dispRelatedReport()" title="Related Reports">
                                                 <table width="100%" class="navtitle1">
                                                    <tr>
                                                        <td><b style="font-family:verdana;font-weight:bold"><%=TranslaterHelper.getTranslatedInLocale("Related_Reports", cle)%></b></td>
                                                        <td><span id="TextImg" class='ui-icon ui-icon-circle-triangle-s'   style='float:right;'></span></td>
                                                    </tr>
                                                </table>
                                            </div>
                                            <div id="rightPaneContents" style="display:<%=String.valueOf(session.getAttribute("RelatedReportStatus"))%>">
                                            </div>

                                        </div>
                                    </td>
                                </tr>
                            </table>
                        </div>
                    </td>
                </tr>
                </table></div>
<!--        <div id="reportstart" class="navigateDialog" title="Navigation">
            <iframe src="#" id="reportstartIframe" frameborder="0" height="50%" width="800px" ></iframe>
        </div>-->

        <%--<div id="report" class="white_content"  align="justify" style="height:150px;width:400px;display:none">--%>
            <div id="reportMeta" title="Create Report" style="display:none">
                <center>
                    <br><br>

                    <table style="width:80%" border="0">
                        <tr>
                            <td valign="top" class="myHead" style="width:40%"><%=TranslaterHelper.getTranslatedInLocale("Report_Name", cle)%></td>
                            <td valign="top" style="width:60%">
                                <input type="text" maxlength="35" name="reportName" style="width:80%" id="reportName" onkeyup="tabmsg1()" onfocus="document.getElementById('save').disabled = false;"><br><span id="duplicate" style="color:red"></span>
                            </td>
                        </tr>
                        <tr>
                            <td  valign="top" class="myHead" style="width:30%"><%=TranslaterHelper.getTranslatedInLocale("Description", cle)%></td>
                            <td valign="top" style="width:70%">
                                <textarea name="reportDesc" id="reportDesc" style="width:80%"></textarea>
                            </td>
                        </tr>
                    </table>
                    <table>
                        <tr>
                            <td><input type="button" class="navtitle-hover" value="<%=TranslaterHelper.getTranslatedInLocale("next", cle)%>" id="save" onclick="saveReport()"></td>
                            <td><input type="button" class="navtitle-hover" value="<%=TranslaterHelper.getTranslatedInLocale("cancel", cle)%>" onclick="cancelReport()"></td>
                        </tr>
                    </table>

                </center>
            </div>
            <div id="saveSearchData" title="Save Search" style="display:none">
                <center>
                    <br><br>
                     <table style="width:90%" border="0">
                         <tr>
                             <td valign="top" class="myHead" style="width:35%"><%=TranslaterHelper.getTranslatedInLocale("Search_Name", cle)%></td>
                              <td valign="top" style="">
                                  <input type="text" maxlength="35" name="searchName" style="width:80%" id="searchName">
                             </tr>
                         </table>
                    <table>
                        <br>
                        <br>
                        <td><input type="button" class="navtitle-hover" value="save" id="saveSearch" onclick="saveSearch()"></td>
                        </table>
                    </center>
                </div>
        <div id="fade" class="black_overlay"></div>
        <input type="hidden" id="roleid" name="">
        <input type="hidden" id="cpath" value="<%=request.getContextPath()%>">
        </form>
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
                    $("#reportMeta").dialog({
                        autoOpen: false,
                        height: 300,
                        width: 300,
                        position: 'justify',
                        modal: true
                    });
                    $("#saveSearchData").dialog({
                        autoOpen: false,
                        height: 300,
                        width: 300,
                        position: 'justify',
                        modal: true
                    });
                    $("#favSearch").dialog({
                        autoOpen: false,
                        height: 400,
                        width: 600,
                        position: 'justify',
                        modal: true
                    });

                }else{
                    $(".navigateDialog").dialog({
                        autoOpen: false,
                        height: 460,
                        width: 820,
                        position: 'justify',
                        modal: true
                    });
                    $("#reportMeta").dialog({
                        autoOpen: false,
                        height: 200,
                        width: 300,
                        position: 'justify',
                        modal: true
                    });
                    $("#saveSearchData").dialog({
                        autoOpen: false,
                        height: 200,
                        width: 300,
                        position: 'justify',
                        modal: true
                    });
                    $("#favSearch").dialog({
                        autoOpen: false,
                        height: 450,
                        width: 600,
                        position: 'justify',
                        modal: true
                    });
                }
                var options, a;
                options = {
                    serviceUrl:'srchQueryAction.do?srchParam=autoSuggest',
                    delimiter: /(,)\s*/, // regex or character
                    minChars:2,
                    deferRequestBy: 500, //miliseconds
                    maxHeight:400,
                    width:450,
                    zIndex: 9999,
                    noCache: false
                };
                a = $("#srchText").autocomplete(options);
                $("#srchText").keyup(function(event){
                    if(event.keyCode == 13){
                        searchVals();
                    }
                });


            });

function goPaths(path){
                var modulecode=path.replace("home.jsp#","");
                var userType='<%=userType%>'
                if(modulecode=='Dashboard_Studio' || modulecode=='Report_Studio'){
                    if(!<%=isPowerAnalyserEnableforUser%>)
                        alert("You do not have the sufficient previlages")
                }else if(modulecode=='pbBIManager.jsp'){
                    if(!<%=isQDEnableforUser%>){
                         path="home.jsp";
                    alert("You do not have the sufficient previlages")
                }
                }
                parent.closeStart();
                document.forms.searchForm.action=path;
                document.forms.searchForm.submit();
            }
            function gotoDBCON(ctxPath){
                 if(!<%=isQDEnableforUser%>){
                        alert("You do not have the sufficient previlages")
                    }else{
                document.forms.searchForm.action=ctxPath+"/pbBase.jsp?pagename=Query Studio&curntpage=Database Connection#Database_Connection";
                document.forms.searchForm.submit();
                }
            }

function dispTable(){
    if(document.getElementById("tabTable").style.display=="none"){
        document.getElementById("tabTable").style.display = "block";
        var frameObj = document.getElementById("widgetframe");
        var source = 'divPersistent.jsp?method=forResultStatus&block=yes';
        frameObj.src = source;
    }else{
        document.getElementById("tabTable").style.display = "none";
        var frameObj = document.getElementById("widgetframe");
        var source = 'divPersistent.jsp?method=forResultStatus&block=no';
        frameObj.src = source;
    }
}

function dispKPI(){
    if(document.getElementById("MeasureKPItable").style.display=="none"){
        document.getElementById("MeasureKPItable").style.display = "block";
        var frameObj = document.getElementById("widgetframe");
        var source = 'divPersistent.jsp?method=forKpiStatus&block=yes';
        frameObj.src = source;
    }else{
        document.getElementById("MeasureKPItable").style.display = "none";
        var frameObj = document.getElementById("widgetframe");
        var source = 'divPersistent.jsp?method=forKpiStatus&block=no';
        frameObj.src = source;
    }
}

function dispTopBottom(){
    if(document.getElementById("topBtmId").style.display=="none"){
        document.getElementById("topBtmId").style.display = "block";
        var frameObj = document.getElementById("widgetframe");
        var source = 'divPersistent.jsp?method=forTopBtmStatus&block=yes';
        frameObj.src = source;
    }else{
        document.getElementById("topBtmId").style.display = "none";
        var frameObj = document.getElementById("widgetframe");
        var source = 'divPersistent.jsp?method=forTopBtmStatus&block=no';
        frameObj.src =source;
    }
}

function dispTrends(){
    if(document.getElementById("relatedMeasures").style.display=="none"){
        document.getElementById("relatedMeasures").style.display = "block";
        var frameObj = document.getElementById("widgetframe");
        var source = 'divPersistent.jsp?method=forTrends&block=yes';
        frameObj.src = source;
    }else{
        document.getElementById("relatedMeasures").style.display = "none";
        var frameObj = document.getElementById("widgetframe");
        var source = 'divPersistent.jsp?method=forTrends&block=no';
        frameObj.src = source;
    }
}

function dispTrendCharts(){
    if(document.getElementById("timeSeriesCharts").style.display=="none"){
        document.getElementById("timeSeriesCharts").style.display = "block";
        var frameObj = document.getElementById("widgetframe");
        var source = 'divPersistent.jsp?method=forTrendChartsSearch&block=yes';
        frameObj.src = source;
    }else{
        document.getElementById("timeSeriesCharts").style.display = "none";
        var frameObj = document.getElementById("widgetframe");
        var source = 'divPersistent.jsp?method=forTrendChartsSearch&block=no';
        frameObj.src = source;
    }
}

function dispRelatedReport(){
    if(document.getElementById("rightPaneContents").style.display=="none"){
        document.getElementById("rightPaneContents").style.display = "block";
        var frameObj = document.getElementById("widgetframe");
        var source = 'divPersistent.jsp?method=forRelatedReport&block=yes';
        frameObj.src = source;
    }else{
        document.getElementById("rightPaneContents").style.display = "none";
        var frameObj = document.getElementById("widgetframe");
        var source = 'divPersistent.jsp?method=forRelatedReport&block=no';
        frameObj.src =source;
    }
}


function processKpis(timeLevel)
{
    $("#monthlyKpisPB").progressbar({value: 37});
    $("#quarterlyKpisPB").progressbar({value: 37});
    $.ajax({
     url: 'srchQueryAction.do?srchParam=getKpis&timeLevel='+timeLevel,
    success: function(data){
        if ( timeLevel == 'MONTH')
        {
            $( "#monthlyKpisPB").progressbar("value",100);
            $( "#monthlyKpisPB").hide();
            $("#monthlyKpis").html(data);
        }
        else if ( timeLevel == 'QUARTER')
        {
           $( "#quarterlyKpisPB").progressbar("value",100);
           $( "#quarterlyKpisPB").hide();
           $("#quarterlyKpis").html(data);
        }
    }
    });

}

function getRelatedMeasures()
{
    $( "#relatedMeasuresPB").progressbar({value: 37});

    $.ajax({
         url: 'srchQueryAction.do?srchParam=getRelatedTrendMeasures',
        success: function(data){
            $( "#relatedMeasuresPB").progressbar("value",100);
            $( "#relatedMeasuresPB").hide();
            $("#relatedMeasures").html(data);
        }
    });
}

function getTimeSeriesCharts()
{
<%--    $( "#timeSeriesChartsPB").progressbar({value: 37});
    $.ajax({
         url: 'srchQueryAction.do?srchParam=generateTimeSeriesCharts',
        success: function(data){
            $( "#timeSeriesChartsPB").progressbar("value",100);
            $( "#timeSeriesChartsPB").hide();
            $("#timeSeriesCharts").html(data);
        }
    });--%>
}

function getRelatedReports()
{
    $.ajax({
         url: 'srchQueryAction.do?srchParam=getRelatedReports',
        success: function(data){
            $("#rightPaneContents").html(data);
        }
    });
}


function getFavSearch(searchtext){
$('#srchText').val(searchtext);
 $("#favSearch").dialog('close');
 searchVals();



}

function searchVals()
{
    $("#srchResults").show();
    //$("#srchResults").html(data);
    $("#progenTablePB").progressbar({value: 37});
    if(document.getElementById("srchText").value==""){
        $("#srchResults").hide();
        $("#progenTablePB").hide();
        alert('Please enter Text for Search');
    } else {
        var srchTxt = encodeURIComponent(document.getElementById("srchText").value);
    $.ajax({
        url: 'srchQueryAction.do?srchParam=isSearchValid&srchText='+srchTxt,
        success: function(data){
        if(data=="ReportOnly"){
        $("#srchResults").show();
        //$("#srchResults").html(data);
        //$("#progenTablePB").progressbar({value: 37});
        var source = "TableDisplay/pbDisplay.jsp?source=L&tabId=-999";
        var dSrc = document.getElementById("iframe1");
        dSrc.src = source;
        source = "topBottom.jsp?REPORTID=-999";
        dSrc = document.getElementById("topBtmFrame");
        dSrc.src = source;
        getRelatedReports();
        getRelatedMeasures();
        processKpis('MONTH');
        processKpis('QUARTER');
        $("#SearchActions").show();
        $("#leftPaneContents").show();
     //   $("#kpiPane").show();
        $("#leftPaneHolder").attr('width', '20%');
        $("#centerPaneHolder").attr('width', '70%');
        $("#rightPaneHolder").attr('width', '10%');

    }
    else if ( data=="DimensionOnly" )
    {

        $("#srchResults").show();
        //$("#progenTablePB").progressbar("value",69);
        var source = "TableDisplay/pbDisplay.jsp?source=L&tabId=-999";
        var dSrc = document.getElementById("iframe1");
        dSrc.src = source;
        getRelatedReports();
        $("#leftPaneContents").hide();
     //   $("#kpiPane").hide();
        $("#leftPaneHolder").attr('width', '1%');
        $("#centerPaneHolder").attr('width', '79%');
        $("#rightPaneHolder").attr('width', '20%');
    }
    else
    {
        alert('No data found for the Search')
        $("#SearchActions").hide();
        $("#srchResults").hide();

        $("#progenTablePB").hide();
    }
    }
    });
}
}





            function createReport(){
            <%--document.getElementById('report').style.display='block';--%>
            <%--document.getElementById('fade').style.display='block';--%>
                        $("#reportMeta").dialog('open');
                    }
                    function opensaveSearchDialog(){
                        $("#saveSearchData").dialog('open');
                    }
                    function openfavSearch(){
                        $("#favSearch").dialog('open');
                        var searchName = document.getElementById('searchName').value;
                        var searchValue = document.getElementById('srchText').value;

                        $.ajax({
                            url: 'srchQueryAction.do?srchParam=getFavSearch&searchName='+searchName+'&searchValue='+searchValue,
                            success: function(data){

                                $("#favSearchId").html(data);
                            }
                        });

                    }

                    function cancelReport(){
                        $("#reportMeta").dialog('close');
            <%--document.getElementById('duplicate').innerHTML = '';--%>
            <%--document.getElementById('report').style.display='none';--%>
            <%--document.getElementById('fade').style.display='none';--%>
                    }
                    function tabmsg1(){
                        document.getElementById('reportDesc').value = document.getElementById('reportName').value;
                    }

                    function saveReport(){
                        var reportName = document.getElementById('reportName').value;
                        var reportDesc = document.getElementById('reportDesc').value;

                        if(reportName==''){
                            alert("Please enter Report Name");
                        }
                        else  if(reportDesc==''){
                            alert("Please enter Report Description")
                        }
                        else{
                            $.ajax({
                                url: 'srchQueryAction.do?srchParam=checkReportName&reportName='+reportName,
                                success: function(data){
                                    if(data!=""){
                                        document.getElementById('duplicate').innerHTML = "Report Name already exists";
                                        document.getElementById('save').disabled = true;
                                    }
                                    else if(data=='')
                                    {
                                        $.ajax({
                                            url: 'srchQueryAction.do?srchParam=createReport&reportName='+reportName+'&reportDesc='+reportDesc,
                                            success: function(data){
                                                if (data=="ERROR")
                                                    alert("Creation of Report Failed");
                                                else
                                                    alert("Report Created Successfully with Id "+data);
                                            }
                                        });
                                        cancelReport();
                                    }
                                }
                            });
                        }

                    }

            function hideProgress()
            {
                $( "#progenTablePB").progressbar("value",100);
                $( "#progenTablePB").hide();
            }

            function saveSearch()
            {
                var searchName = document.getElementById('searchName').value;
                var searchValue = document.getElementById('srchText').value;

                if(searchName=='')
                    alert("Enter Search Name");
                else{
                    $.ajax({
                        url: 'srchQueryAction.do?srchParam=saveFavSearch&searchName='+searchName+'&searchValue='+searchValue,
                        success: function(data){
                            var json=eval("("+data+")");
                            if ( json.errorCode == 'SRCH_FAV_ALREADY_EXISTS' )
                                alert(json.mesgTxt);
                            else
                                {
                                    alert(json.mesgTxt);
                                    $("#saveSearchData").dialog('close');
                                }
                        }
                    });
                }
            }
            function deleteFavSearch(searchName,i){
                if(confirm('Are You Sure You Want To Delete?'))
                    deleteForSure(searchName,i);
            }
            function deleteForSure(searchName,i){

                document.getElementById('favSearchId').deleteRow(i);

                $.ajax({
                    url: 'srchQueryAction.do?srchParam=deleteFavSearch&searchName='+searchName,
                    success: function(data){

                    }
                });

                    }
                    function viewSrchReport(path){
                        var contextPath=document.getElementById("cpath").value;
                        document.forms.searchForm.action=contextPath+'/'+path;
                        document.forms.searchForm.submit();
                    }



        </script>
    </body>
</html>
<%}%>
