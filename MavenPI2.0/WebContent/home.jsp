<%@page contentType="text/html" pageEncoding="UTF-8" import="com.progen.i18n.TranslaterHelper,com.progen.action.UserStatusHelper,com.progen.users.PrivilegeManager,com.progen.action.UserStatusHelper,utils.db.*,prg.db.Session"%>
<%@page import="prg.db.PbReturnObject,java.sql.*,prg.db.PbReturnObject,prg.db.Session,java.util.*,prg.db.PbDb,prg.util.screenDimensions,com.progen.reportdesigner.db.ReportTemplateDAO,com.progen.users.ProgenUser"%>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
        boolean showExtraTabs=true;
         String themeColor = "blue";
         boolean headLines=false;
         boolean portalViewer=false;
         boolean reportAnalyzer=false;
         boolean whatif=false;
         String userType = null;
         boolean isActive = false;
          String[] scheduleday = {"Mon", "Tue", "Wed", "Thur", "Fri", "Sat", "Sun"};
    String[] sday = {"2", "3", "4", "5", "6", "7", "1"};
    String[] frequency = {"Daily", "Weekly", "Monthly", "Hourly"};
         HttpSession sessionhttp = request.getSession(false);
            //added by Dinanath for default locale
                   
                    Locale cL=null;
                   cL=(Locale)session.getAttribute("UserLocaleFormat");
            //for clearing cache
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader ("Expires", 0);
            ServletContext context = getServletContext();
           // boolean isAxa = Boolean.parseBoolean(context.getInitParameter("isAxa"));
            screenDimensions dims =new screenDimensions();
                int pageFont,anchorFont;
                HashMap map =dims.getFontSize(session,request,response);
                if(!String.valueOf(map.get("pageFont")).equalsIgnoreCase("NULL")){
                pageFont=Integer.parseInt(String.valueOf(map.get("pageFont")));
                anchorFont = Integer.parseInt(String.valueOf(map.get("pageFont")))+1;
                }else{
                pageFont = 11;
                anchorFont = 12;
                }

String status = String.valueOf(session.getAttribute("status"));
   if (session.getAttribute("theme") == null) {
        session.setAttribute("theme", themeColor);
    }
    else {
        themeColor = String.valueOf(session.getAttribute("theme"));
    }
   // boolean isPortalEnableforUser=false;
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
          System.out.println("userType"+userType);
    }*/

if(session.getAttribute("USERID")==null || String.valueOf(map.get("Redirect")).equalsIgnoreCase("Yes")){
response.sendRedirect(request.getContextPath()+"/baseAction.do?param=logoutApplication");
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
      String contxtPath=request.getContextPath();

%>


<jsp:useBean id="brdcrmb"  scope="session" class="com.progen.action.BreadCurmbBean"/>

<html>
    <head>
       
        <link type="text/css" href="<%=contxtPath%>/stylesheets/themes/<%=themeColor%>/jquery-ui-1.7.3.custom.css" rel="stylesheet" />
        <link type="text/css" href="<%=contxtPath%>/stylesheets/themes/<%=themeColor %>/ReportCss.css" rel="stylesheet"/>
        <link rel="stylesheet" href="stylesheets/treeviewstyle/jquery.treeview.css" />
        <link rel="stylesheet" href="stylesheets/treeviewstyle/screen.css" />
        <link type="text/css" href="<%=contxtPath%>/jQuery/jquery/themes/base/BreadCrumb.css" rel="stylesheet" />
        <link type="text/css" href="<%=contxtPath%>/jQuery/jquery/themes/base/Base.css" rel="stylesheet" />
        <link type="text/css" href="stylesheets/ui.all.css" rel="stylesheet" />
        <link type="text/css" href="stylesheets/demos.css" rel="stylesheet" />
<!--        <link rel="stylesheet" href="<%=contxtPath%>/stylesheets/style.css" type="text/css" media="print, projection, screen">-->
        <link href="stylesheets/StyleSheet.css" rel="stylesheet" type="text/css" />
        <link href="stylesheets/confirm.css" rel="stylesheet" type="text/css" />
        <link href="stylesheets/jquery.contextMenu.css" rel="stylesheet" type="text/css" />
        <script src="javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <script type="text/javascript" src="javascript/draggable/ui.core.js"></script>
        <script src="javascript/treeview/jquery.cookie.js" type="text/javascript"></script>
        <script src="javascript/treeview/jquery.treeview.js" type="text/javascript"></script>
        <script type="text/javascript" src="javascript/treeview/demo.js"></script>
        <script type="text/javascript" src="javascript/draggable/ui.draggable.js"></script>
        <script type="text/javascript" src="javascript/draggable/ui.droppable.js"></script>
        <script type="text/javascript" src="javascript/ui.resizable.js"></script>
        <script type="text/javascript" src="javascript/ui.accordion.js"></script>
        <script type="text/javascript" src="javascript/ui.dialog.js"></script>
        <script type="text/javascript" src="javascript/ui.sortable.js"></script>
        <script type="text/javascript" src="javascript/effects.core.js"></script>
        <script type="text/javascript" src="<%=contxtPath%>/jQuery/jquery/ui/jquery.jBreadCrumb.1.1.js"></script>
        <script type="text/javascript"  language="JavaScript" src="<%=contxtPath%>/querydesigner/JS/jquery.columnfilters.js"></script>
        <script src="javascript/jquery.simplemodal-1.1.1.js" type="text/javascript"></script>
        <script src="javascript/jquery.contextMenu.js" type="text/javascript"></script>
        <script type="text/javascript" src="javascript/ui.tabs.js"></script>
        <script type="text/javascript" src="<%=contxtPath%>/javascript/ui.datepicker.js"></script>
        <!--below five lines only added by bharathi reddy on 26-08-09 -->
        <%--For tablesorter--%>
<!--        <link rel="stylesheet" href="<%=contxtPath%>/tablesorter/docs/css/jq.css" type="text/css" media="print, projection, screen" />-->
        <link rel="stylesheet" href="<%=contxtPath%>/tablesorter/themes/<%=themeColor%>/style.css" type="text/css" media="print, projection, screen" />
        <%--<script type="text/javascript" src="<%=request.getContextPath()%>/tablesorter/jquery-latest.js"></script>--%>
        <script type="text/javascript" src="<%=contxtPath%>/tablesorter/jquery.tablesorter.js"></script>
        <script type="text/javascript" src="<%=contxtPath%>/tablesorter/addons/pager/jquery.tablesorter.pager.js"></script>
        <script type="text/javascript"  language="JavaScript" src="<%=contxtPath%>/tablesorter/jquery.columnfilters.js"></script>
        <script type="text/javascript" src="<%=contxtPath%>/tablesorter/docs/js/chili/chili-1.8b.js"></script>
        <script type="text/javascript" src="<%=contxtPath%>/tablesorter/docs/js/docs.js"></script>
        <script type="text/javascript" src="<%=contxtPath%>/javascript/pi.js"></script>
        <script type="text/javascript" src="<%=contxtPath%>/template/js/managementTemplate.js"></script>
	<link rel="stylesheet" href="css/font-awesome.min.css" type="text/css"/>
     
        <style type="text/css">
            .tabsty{
                -moz-border-radius-bottomleft:4px;
                -moz-border-radius-bottomright:4px;
                -moz-border-radius-topleft:4px;
                -moz-border-radius-topright:4px;
            }

            .startpage {
                display:none;
                position: absolute;
                top: 15%;
                left: 18%;
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
            .black_overlay{
                display: none;
                position: absolute;
                top: 0%;
                left: 0%;
                width: 200%;
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
            }
            a {font-family:inherit;cursor:pointer;font-size:<%=anchorFont%>px;}
            *{font-size:<%=pageFont%>px}
             #ui-datepicker-div
            {
                z-index: 9999999;
                width: auto;
                display: none;
            }
        </style>
    </head>
    <body>
        <%

           ProgenUser user = (ProgenUser) request.getSession(false).getAttribute("ProgenUser");
            if ( user.isDuplicateSession(Integer.parseInt((String)session.getAttribute("USERID")) ) )
            {
                out.print("<input type=\"hidden\" id=\"warnUser\" value=\"true\">");
            }
    String Pagename = "Home";
    String url = request.getRequestURL().toString();
    brdcrmb.inserting(Pagename, url);
    String ctxPath = request.getContextPath();
    try {
        PbDb pbdb = new PbDb();
        String userId = "";
        userId = String.valueOf(request.getSession(false).getAttribute("USERID"));

        ArrayList path = new ArrayList();
        path.add("baseAction.do?param=goHome");
        session.setAttribute("path", path);

       // String userprivis = "SELECT USER_ID, PRIVELEGE_ID FROM PRG_AR_USER_PRIVELEGES where USER_ID in(" + userId + ")";
      //  PbReturnObject userprivispbro = pbdb.execSelectSQL(userprivis);
     //   Vector privis = new Vector();
     //   for (int i = 0; i < userprivispbro.getRowCount(); i++) {
     //       privis.add(userprivispbro.getFieldValueString(i, 1));
     //   }
              String targetUrl=request.getContextPath() + "/QTarget/JSPs/pbTargetList.jsp?userId=" + userId;
        %>
                    <table style="width:100%">
                <tr>

                    <td valign="top" style="width:50%;">
                        <%  if (status.equalsIgnoreCase("OK")) {%>
                                <jsp:include page="Headerfolder/headerPageVeraction.jsp"/>
                                <%} else {%>
                        <jsp:include page="Headerfolder/headerPage.jsp"/>
                                <%}%>
                    </td>
                </tr>
            </table>
                    <form action=""  name="myFormH" method="post" style="padding:0pt">
            <table width="99%" >

                <tr>
                    <td style="height:10px;width:70%" align="left">
                        <div id=container>
<!--                            <div id='breadCrumb' class='breadCrumb module'>
                                <ul>
                                    <li style="display:none"></li>
                                    <li style="display:none"></li>
                <%--                    <% String pgnam = "";
                      if (brdcrmb.getPgname1() != null) {
                          pgnam = brdcrmb.getPgname1().toString();
                          //out.println("six");
                   ////////////////////////////////////////////////////////////////////.println.println("six");
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
                   ////////////////////////////////////////////////////////////////////.println.println("seven");
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
                   ////////////////////////////////////////////////////////////////////.println.println("eight");
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
                   ////////////////////////////////////////////////////////////////////.println.println("nine");
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
                   ////////////////////////////////////////////////////////////////////.println.println("ten");
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
                           //  }
                         //}
                                    %>   --%>
                                    <li style="display:none"></li>
                                    <li style="display:none"></li>


                                            <td style="height:10%;width:20%" align="right">
                                                <a href='javascript:void(0)' title="modules" class="ui-icon ui-icon-arrowthickstop-1-e" onclick="opensmodules()"></a>
                                            </td>
                                </ul>
                            </div>-->
                            </div>
<!--                        </div>-->
                        <div class="chevronOverlay main">
                        </div>
                    </td>
<!--                    <td align="right"><span title="Click here for see sales Report" class=" ui-icon ui-icon-plusthick" onclick="salesReportFormulaDiv1()"></span></td>-->
<!--                    <td  style="height:10px;width:30%" align="right">
                        <a href="javascript:void(0)" onclick="javascript:goGlobe()" style="font-size:10px;color:#2191C0;;font-weight:bold;text-decoration: none;font-family:Georgia"> Navigation </a> |
                        <a href="javascript:void(0)" onclick="javascript:gohome()" style="font-size:10px;color:#2191C0;;font-weight:bold;text-decoration: none;font-family:Georgia"> Home </a> |
                       <%  if(showExtraTabs){%>
                        <a href="bugDetailsList.jsp" style="font-size:10px;color:#2191C0;;font-weight:bold;text-decoration: none;font-family:Georgia"> Bug </a> |
                       <%}%>
                        <a href="javascript:void(0)" onclick="javascript:logout()" style="font-size:10px;color:#2191C0;;font-weight:bold;text-decoration: none;font-family:Georgia"> Logout </a>
                    </td>-->

                </tr>
            </table>
            <div id="tabs" style="width:99%;min-height:100%;max-height:100%;">
<%--                 <ul>

                    <%
         //if (privis.size() != 0) {
              // System.out.println("vals"+UserStatusHelper);
              if (PrivilegeManager.isModuleEnabledForUser("REPVIEWER", Integer.parseInt(userId))||
                     PrivilegeManager.isModuleEnabledForUser("VIEWERPLUS", Integer.parseInt(userId))||
                     PrivilegeManager.isModuleEnabledForUser("ANALYSER", Integer.parseInt(userId))) {
                  if (request.getParameter("roleId") != null) {
                    %>
                    <li><a href="RolesTab.jsp?roleId=<%=request.getParameter("roleId")%>" title="RolesTab" onclick="showhiden()">Business Roles</a></li>
                    <% } else {%>
                    <li><a href="RolesTab.jsp" title="RolesTab" onclick="showhiden()">Business Roles</a></li>
                    <% }
                }
                 if (PrivilegeManager.isModuleEnabledForUser("REPVIEWER", Integer.parseInt(userId))||
                     PrivilegeManager.isModuleEnabledForUser("VIEWERPLUS", Integer.parseInt(userId))||
                     PrivilegeManager.isModuleEnabledForUser("ANALYSER", Integer.parseInt(userId))){%>
                    <li><a href="studioAction.do?studioParam=getAllItems&fromTab=MyReports&pagename=All Reports" title="All Reports">My Reports</a></li>

                    <%}
                if (PrivilegeManager.isModuleEnabledForUser("ADMIN", Integer.parseInt(userId))) {%>
                    <li><a href="javascript:void(0)" title="Administrator" onclick="hideremain('<%=ctxPath%>')">Administrator</a></li>
                    <%}%>
                      <%//if(showExtraTabs){
                //if (PrivilegeManager.isModuleEnabledForUser("REPVIEWER", Integer.parseInt(userId))||
                   //  PrivilegeManager.isModuleEnabledForUser("VIEWERPLUS", Integer.parseInt(userId))||
                   //  PrivilegeManager.isModuleEnabledForUser("ANALYSER", Integer.parseInt(userId)))
                 if(PrivilegeManager.isModuleComponentEnabledForUser("REPDESIGNER", "DASHBOARDDESIGNER", Integer.parseInt(userId))){
    %>
                    <li><a href="studioAction.do?studioParam=getAllItems&fromTab=DashboardStudio" title="Dashboard Studio">Dashboard Studio</a></li>
                    <%}

                if (PrivilegeManager.isModuleEnabledForUser("REPDESIGNER", Integer.parseInt(userId))) {%>
                    <li><a href="studioAction.do?studioParam=getAllItems&fromTab=ReportStudio" title="Report Studio">Report Studio</a></li>
                    <%} %>
            <%
                if (PrivilegeManager.isModuleEnabledForUser("QRYSTUDIO", Integer.parseInt(userId))) {%>
                    <li><a href="javascript:void(0)" onclick="gotoDBCON('<%=ctxPath%>')" title="Query Studio">Query Studio</a></li>
                    <%}%>
                    <%
                if (PrivilegeManager.isModuleEnabledForUser("PURGEREP", Integer.parseInt(userId))) {%>
                    <li><a  href="reportTemplateAction.do?templateParam=getPurgeReps"  title="Purge Report">Purge Report</a></li>
                    <%}
                     // if(showExtraTabs){
                         // if(!isAxa){
                if (PrivilegeManager.isModuleEnabledForUser("MESSAGES", Integer.parseInt(userId))) {
                %>
                    <li style="display:none"><a href="MsgTab.jsp" title="Messages">Messages</a></li>
                    <%}

                if (PrivilegeManager.isModuleEnabledForUser("ALERTS", Integer.parseInt(userId))) {%>
                    <li><a href="AlrtTab.jsp" title="Alerts">Alerts</a></li>
                    <%}


                if (PrivilegeManager.isModuleEnabledForUser("PORTAL", Integer.parseInt(userId))) {%>
                    <li><a  href="javascript:void(0)" onclick="goPortal()" title="Portals">Portals</a></li>
                    <%}
                    if (PrivilegeManager.isModuleEnabledForUser("TARGETS", Integer.parseInt(userId)))
                     {

                    %>

                    <li><a  href="<%=targetUrl%>" title="Targets">Targets</a></li>
                    <%


                }

        //}

    //}%>


           <%
          // if (PrivilegeManager.isModuleEnabledForUser("ETL", Integer.parseInt(userId))) {%>

               <li><a  href="Etl/etlUpload.jsp" title="ETL">ETL</a></li>
           <%if (PrivilegeManager.isModuleEnabledForUser("SEARCH", Integer.parseInt(userId))){
              %>
               <li><a   href="javascript:void(0)" onclick="goSearchpage()"   title="Search">Search</a></li>

                            <li><a  href="searchValues.jsp" title="Search">Search</a></li>
            <%}//}
                          //          else {
                        if (PrivilegeManager.isModuleEnabledForUser("DATACORR", Integer.parseInt(userId))){%>
<!--                          <li><a  href="dataCorrection.jsp" title="Data Correction">Data Correction</a></li>-->
                    <%}
               // if (request.getParameter("roleId") != null) {
                    %>
<!--                    <li><a href="RolesTab.jsp?roleId=<%=request.getParameter("roleId")%>" title="RolesTab" onclick="showhiden()">Business Roles</a></li>
                    <% //} else {%>
                    <li><a href="RolesTab.jsp" title="RolesTab" onclick="showhiden()">Business Roles</a></li>
                    <% //}%>
                    <li><a href="reportTemplateAction.do?templateParam=getAllReportshome&pagename=All Reports" title="All Reports">My Reports</a></li>
                    <li><a href="javascript:void(0)" title="Administrator" onclick="hideremain('<%=ctxPath%>')">Administrator</a></li>
                      <%if(showExtraTabs){%>
                    <li><a href="reportTemplateAction.do?templateParam=getAllDashs&pagename=Dashboard Studio" title="Dashboard Studio">Dashboard Studio</a></li>
                    <%}%>
                    <li><a href="reportTemplateAction.do?templateParam=getAllreps&pagename=Report Studio" title="Report Studio">Report Studio</a></li>

                    <li><a  href="javascript:void(0)" onclick="gotoDBCON('<%=ctxPath%>')" title="Query Studio">Query Studio</a></li>
                    <!--<li><a  href="productTab.jsp"  title="Product">Product</a></li>-->
                    <li><a  href="reportTemplateAction.do?templateParam=getPurgeReps"  title="Purge Report">Purge Report</a></li>
                    <%//if(!isAxa){
                    if (showExtraTabs) {%>
                    <li style="display:none"><a href="MsgTab.jsp" title="Messages">Messages</a></li>
                    <li><a href="AlrtTab.jsp" title="Alerts">Alerts</a></li>

                    <li><a  href="javascript:void(0)" onclick="goPortal()" title="Portals">Portals</a></li>
                    <li><a  href="dataCorrection.jsp" title="Data Correction">Data Correction</a></li>

                    <li><a  href="pbCopyUserList.jsp" title="Admin">Admin</a></li>
                    <li><a href="reportTemplateAction.do?templateParam=getAllWhatIfs" title="What-If Analysis">What-If Analysis</a></li>
                     <li><a  href="<%=targetUrl%>" title="Targets">Targets</a></li>

                    <%}%>
                    <% if (PrivilegeManager.isModuleEnabledForUser("SCORECARD", Integer.parseInt(userId))){
                    %>

                                     <li><a  href="studioAction.do?studioParam=getAllItems&fromTab=Scorecard" title="Scorecard">Scorecard</a></li>
                                     <%
                             }if (PrivilegeManager.isModuleEnabledForUser("HTMLREPORTS", Integer.parseInt(userId))){
                                      %>
                                     <li><a  href="studioAction.do?studioParam=getAllItems&fromTab=HtmlReports" title="Html Reports" >Html Reports</a></li>
                             <%
                             }if (PrivilegeManager.isModuleEnabledForUser("SCEDULER", Integer.parseInt(userId))){
                              %>
                                     <li><a  href="metadataScheduler.jsp" title="Scheduler" >Scheduler</a></li>
                             <% }%>

                                   <li><a  href="sentimentAnalysis.jsp" title="Sentiment Analysis" >Sentiment  Analysis</a></li>
                          <%if(headLines){%>
                                   <li><a  href="headLines.jsp" title="Headlines" >Headlines</a></li>
                                   <%}%>
                                     <li ><a href="Uploadfile.jsp" title="DataUpload" >Data Upload</a></li>
                                     <li ><a href="dataDownload.jsp" title="DataDownload" >Data Download</a></li>
                                     <li ><a href="createCustomUser.jsp" title="Create User" >Create User</a></li>
                </ul>--%>
        <!--   code changed by veena on mar 30 2012 for modified user creation     -->
        <ul>
            <% if(userType.equalsIgnoreCase("Admin") ) {%>

            <% if (request.getParameter("roleId") != null) {
                    %>
                    <li><a href="RolesTab.jsp?roleId=<%=request.getParameter("roleId")%>" title="RolesTab" onclick="showhiden()"><%=TranslaterHelper.getTranslatedInLocale("My_Favorite_Reports", cL)%></a></li>
                    <% } else {%>
                    <%
                         //modified by anitha for business roles tab
                         String moduleCde = (String) sessionhttp.getAttribute("setModuleNameInSession");
                         if (moduleCde != null && moduleCde.equalsIgnoreCase("Report_Studio")) {%>
                    <li><a href="RolesTab.jsp" title="RolesTab" onclick="showhiden()"><%=TranslaterHelper.getTranslatedInLocale("business_role", cL)%></a></li>
                    <%} else {
                    %>
                    <li><a href="RolesTab.jsp" title="RolesTab" onclick="showhiden()"><%=TranslaterHelper.getTranslatedInLocale("My_Favorite_Reports", cL)%></a></li>
                    <%}
                         }%>
<!--                     <li><a  href="javascript:void(0)" onclick="oneViewBy()" >One View Beta</a></li>-->
                    <% for(int i=0;i<repid1.size()&& i<5;i++) {%>
                    <%String s;%>
                     <% if(list1.get(i).toString().length()>30 ){%>
                    <% s=list1.get(i).toString().substring(0, 30).concat("...");%>
                        <% }else{ %>
                        <%s=list1.get(i).toString();%>
                        <%}%> 
                        <% if(reporttype1.get(i).toString().equals("R")){ %>
                        <li><a href="javascript:void(0)" style="font-size: 11px" onclick="gotoDBCON1('<%=ctxPath%>',<%=repid1.get(i)%>)" title="<%=list1.get(i).toString()%>"><%=s%></a></li>
<!--                     <li><a href="reportViewer.do?reportBy=viewReport&REPORTID=<%=repid1.get(i)%>&action=open&RepotType=hometab" onclick="closeTable()" title="<%=list1.get(i).toString()%>"><%=s%></a></li>-->

                     <%}else{%>
                          <li><a href="javascript:void(0)" style="font-size: 11px" onclick="gotoDBCON2('<%=ctxPath%>',<%=repid1.get(i)%>)" title="<%=list1.get(i).toString()%>"><%=s%></a></li>
<!--                     <li><a  href="dashboardViewer.do?reportBy=viewDashboard&REPORTID=<%=repid1.get(i)%>&ReportType=hometab" onclick="closeTable()"title="<%=list1.get(i).toString()%>"><%=s%> </a> </li>-->

                     <% } %>

                    <% } %>


<!--                    <li><a href="studioAction.do?studioParam=getAllItems&fromTab=MyReports&pagename=All Reports" title="All Reports">My Reports</a></li>

                    <li><a href="javascript:void(0)" title="Administrator" onclick="hideremain('<%=ctxPath%>')">Administrator</a></li>

                    <li><a href="studioAction.do?studioParam=getAllItems&fromTab=DashboardStudio" title="Dashboard Studio">Dashboard Studio</a></li>

                    <li><a href="studioAction.do?studioParam=getAllItems&fromTab=ReportStudio" title="Report Studio">Report Studio</a></li>
                           <li><a href="javascript:void(0)" onclick="gotoDBCON('<%=ctxPath%>')" title="Query Studio">Query Studio</a></li>

                    <li style="display:none"><a href="MsgTab.jsp" title="Messages">Messages</a></li>


                     <li><a  href="javascript:void(0)" onclick="goPortal()" title="Portals">Portals</a></li>

                     <li><a   href="javascript:void(0)" onclick="goSearchpage()"   title="Search">Search</a></li>

                     <li><a  href="studioAction.do?studioParam=getAllItems&fromTab=Scorecard" title="Scorecard">Scorecard</a></li>

                     <li><a  href="studioAction.do?studioParam=getAllItems&fromTab=HtmlReports" title="Html Reports" >Html Reports</a></li>

                     <li><a  href="metadataScheduler.jsp" title="Scheduler" >Scheduler</a></li>

                     <li><a  href="headLines.jsp" title="Headlines" >Headlines</a></li>
                     <li><a  href="GlobalParams.jsp" title="Global Parameterss" >Global Parameters</a></li>-->

            <%}%><%else{
           if (request.getParameter("roleId") != null) {
                    %>
                    <li><a href="RolesTab.jsp?roleId=<%=request.getParameter("roleId")%>" title="RolesTab" onclick="showhiden()"><%=TranslaterHelper.getTranslatedInLocale("My_Favorite_Reports", cL)%></a></li>
                    <% } else {%>
                    <%
                         //modified by anitha for business roles tab
                         String moduleCde = (String) sessionhttp.getAttribute("setModuleNameInSession");
                         if (moduleCde != null && moduleCde.equalsIgnoreCase("Report_Studio")) {%>
                    <li><a href="RolesTab.jsp" title="RolesTab" onclick="showhiden()"><%=TranslaterHelper.getTranslatedInLocale("business_role", cL)%></a></li>
                    <%} else {
                    %>
                    <li><a href="RolesTab.jsp" title="RolesTab" onclick="showhiden()"><%=TranslaterHelper.getTranslatedInLocale("My_Favorite_Reports", cL)%></a></li>
                    <% }}%>

              <%--    <li><a href="studioAction.do?studioParam=getAllItems&fromTab=MyReports&pagename=All Reports" title="All Reports">My Reports</a></li>

                    <%if(reportAnalyzer){%>
                 <!--    <li><a href="studioAction.do?studioParam=getAllItems&fromTab=ReportStudio" title="Report Studio">Report Studio</a></li>-->
                    <%}%>
                    <%if(portalViewer){%>
                     <li><a  href="javascript:void(0)" onclick="goPortal()" title="Portals">Portals</a></li>
                     <%}%>

                     <li><a  href="studioAction.do?studioParam=getAllItems&fromTab=HtmlReports" title="Html Reports" >Html Reports</a></li>

                     <%if(headLines){%>
                     <li><a  href="headLines.jsp" title="Headlines" >Headlines</a></li>
                     <%}%>
              --%>

                <%}%>
        </ul>
            </div>
        <div id="tabs1" style="display: none">

        <ul>
            <% if(userType.equalsIgnoreCase("Admin") ) {%>

            <% if (request.getParameter("roleId") != null) {
                    %>
                    <li><a href="RolesTab.jsp?roleId=<%=request.getParameter("roleId")%>" title="RolesTab" onclick="showhiden()"><%=TranslaterHelper.getTranslatedInLocale("business_role", cL)%></a></li>
                    <% } else {%>
                    <li><a href="RolesTab.jsp" title="RolesTab" onclick="showhiden()"><%=TranslaterHelper.getTranslatedInLocale("business_role", cL)%></a></li>
                    <% }%>

                    <li><a href="studioAction.do?studioParam=getAllItems&fromTab=MyReports&pagename=All Reports" title="All Reports">My Reports</a></li>

                    <li><a href="javascript:void(0)" title="Administrator" onclick="hideremain('<%=ctxPath%>')">Administrator</a></li>

                    <li><a href="studioAction.do?studioParam=getAllItems&fromTab=DashboardStudio" title="Dashboard Studio">Dashboard Studio</a></li>

                    <li><a href="studioAction.do?studioParam=getAllItems&fromTab=ReportStudio" title="Report Studio">Report Studio</a></li>

                    <li><a href="studioAction.do?studioParam=getAllItems&fromTab=AOBuilder" title="AO Builder">AO Builder</a></li>
                    <li><a href="studioAction.do?studioParam=getAllItems&fromTab=MOBuilder" title="MO Builder">Management Studio</a></li>
<!--                     <li><a href="studioAction.do?studioParam=getAllItems&fromTab=CustomReportStudio" title="Report Builder">Custom Report Studio</a></li>-->

                    <li><a href="javascript:void(0)" onclick="gotoDBCON('<%=ctxPath%>')" title="Query Studio">Query Studio</a></li>

                    <li style="display:none"><a href="MsgTab.jsp" title="Messages">Messages</a></li>


                     <li><a  href="javascript:void(0)" onclick="goPortal()" title="Portals">Portals</a></li>

                     <li><a   href="javascript:void(0)" onclick="goSearchpage()"   title="Search">Search</a></li>

                     <li><a  href="studioAction.do?studioParam=getAllItems&fromTab=Scorecard" title="Scorecard">Scorecard</a></li>

                     <li><a  href="studioAction.do?studioParam=getAllItems&fromTab=HtmlReports" title="Html Reports" >Html Reports</a></li>

                     <li><a  href="metadataScheduler.jsp" title="Scheduler" >Scheduler</a></li>

                     <li><a  href="headLines.jsp" title="Headlines" >Headlines</a></li>
                     <li><a  href="dynamicHeadLines.jsp" title="Dynamic Headlines" >Dynamic Headlines</a></li>
                     <li><a  href="GlobalParams.jsp" title="Global Parameterss" >Global Parameters</a></li>
                     <li><a  href="javascript:void(0)" onclick="oneViewBy()" >One View <sup>Beta</sup></a></li>
                     <li><a  href="javascript:void(0)" onclick="IcalPage()" title="View Calendar" >i-Cal <sup>Beta</sup></a></li>
<!--                     <li><a  href="oneview.jsp" title="Headlines" >OneView</a></li>-->

            <%}%><%else{
           if (request.getParameter("roleId") != null) {
                    %>
                    <li><a href="RolesTab.jsp?roleId=<%=request.getParameter("roleId")%>" title="RolesTab" onclick="showhiden()"><%=TranslaterHelper.getTranslatedInLocale("business_role", cL)%></a></li>
                    <% } else {%>
                    <li><a href="RolesTab.jsp" title="RolesTab" onclick="showhiden()"><%=TranslaterHelper.getTranslatedInLocale("business_role", cL)%></a></li>
                    <% }%>

                    <li><a href="studioAction.do?studioParam=getAllItems&fromTab=MyReports&pagename=All Reports" title="All Reports">My Reports</a></li>
                    <%if(isPowerAnalyserEnableforUser){%>
                    <li><a href="studioAction.do?studioParam=getAllItems&fromTab=DashboardStudio" title="Dashboard Studio">Dashboard Studio</a></li>
                    <li><a href="studioAction.do?studioParam=getAllItems&fromTab=ReportStudio" title="Report Studio">Report Studio</a></li>
                     <li><a href="studioAction.do?studioParam=getAllItems&fromTab=ReportBuilder" title="Report Builder">Report Builder</a></li>
                     <li><a href="studioAction.do?studioParam=getAllItems&fromTab=AOBuilder" title="AO Builder">AO Builder</a></li>
                      <li><a href="studioAction.do?studioParam=getAllItems&fromTab=MOBuilder" title="MO Builder">Management Studio</a></li>

                    <%}%>
                    <li><a href="javascript:void(0)" title="Administrator" onclick="hideremain('<%=ctxPath%>')">Administrator</a></li>
                    <%if(isQDEnableforUser){%>
                    <li><a href="javascript:void(0)" onclick="gotoDBCON('<%=ctxPath%>')" title="Query Studio">Query Studio</a></li>
                    <%}%>
                     <li><a  href="javascript:void(0)" onclick="goPortal()" title="Portals">Portals</a></li>
                     
                     <li><a  href="studioAction.do?studioParam=getAllItems&fromTab=Scorecard" title="Scorecard">Scorecard</a></li>
                     
                     <li><a  href="studioAction.do?studioParam=getAllItems&fromTab=HtmlReports" title="Html Reports" >Html Reports</a></li>

                    
                     <li><a  href="headLines.jsp" title="Headlines" >Headlines</a></li>
                      <li><a  href="dynamicHeadLines.jsp" title="Dynamic Headlines" >Dynamic Headlines</a></li>
                 
                     <li><a  href="javascript:void(0)" onclick="oneViewBy()" >One View Beta</a></li>
                  
                     <%}%>
        </ul>
            </div>
            <br>
<!--            <div id="portal" class="white_content"  align="justify" style="height:150px;width:400px;display:none">
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
                                <textarea name="portalDesc" id="portalDesc" style="width:80%"></textarea>
                            </td>
                        </tr>
                    </table>
                    <table>
                        <tr>
                            <td><input type="button" class="btn" value="Next" id="portalsave" onclick="savePortal()"></td>
                            <td><input type="button" class="btn" value="Cancel" onclick="cancelPortal()"></td>
                        </tr>
                    </table>

                </center>
            </div>-->
            <input type="hidden" name="reportFlag" ID="reportFlag" value="">
            <input type="hidden" name="PORTALID" ID="PORTALID" value="">
            <input type="hidden" name="PORTALNAME" ID="PORTALNAME" value="">
            <div id="fade" class="black_overlay"></div>
            <table style="width:100%">
                <tr>
                    <td valign="top" style="width:100%;">
                        <jsp:include page="Headerfolder/footerPage.jsp"/>
                    </td>
                </tr>
            </table>

            <%} catch (Exception e) {
        e.printStackTrace();
    }%>
        </form>
<!--        <div id="reportstart" class="navigateDialog" title="Navigation">
            <iframe src="#" id="reportstartIframe" frameborder="0" height="100%" width="800px" ></iframe>

        </div>-->
         <div id="startPagePriv"  title="Login Start Page" STYLE='display:none'>
            <iframe src="about:blank" height="100%" width="100%" frameborder="0" id="startPageFrame"></iframe>
        </div>
        <div id="fadestart" class="black_start"></div>
        <div id="reportMetaData" style="display:none">
            <table align="center"><tr><td>
                <input type="button"  class="prgBtn" value="ok" onclick="closeTableDetails()"/>
                    </td></tr></table>
            <div id='loadingmetadata' class='loading_image' style="display:none;">
                <img alt=""  id='imgId' src='images/help-loading.gif'  border='0px' style='position:absolute; left: 150px; top: 10px;'/>
        </div>
        </div>
        <div id="reportMeta" style="display:none">
            <center>
                <br>

                <table style="width:100%" >
                    <tr>
                        <td valign="top" class="myHead" style="width:40%">Report Name</td>
                        <td valign="top" style="width:60%">
                                <input type="text" name="reportName" style="width:150px" id="reportName" onkeyup="tabmsg1()" onfocus="document.getElementById('save').disabled = false;"><br><span id="duplicate" style="color:red"></span>
                        </td>
                    </tr>
                    <tr>
                        <td  valign="top" class="myHead" style="width:30%">Description</td>
                        <td valign="top" style="width:70%">
                            <textarea cols=""   rows=""  name="reportDesc" id="reportDesc" style="width:150px"></textarea>
                        </td>
                    </tr>
                </table>
                <table>
                    <tr>
                        <td id="btnCol"></td>
                            <%--<td><input type="button" class="prgBtn" style="width:auto" value="Cancel" onclick="cancelReport()"></td>--%>
                    </tr>
                </table>
            </center>
        </div>
        <div id="Createdash" title="Create Dashboard" style="display:none">
            <center>
                <br>
                <table style="width:100%" border="0">
                    <tr>
                        <td valign="top" class="myHead" style="width:40%">Dashboard Name</td>
                        <td valign="top" style="width:60%">
                                <input type="text"  name="dashboardName" style="width:150px" id="dashboardName" onkeyup="tabmsg2()" onfocus="document.getElementById('dashboardsave').disabled = false;"><br><span id="duplicateDashboard" style="color:red"></span>
                        </td>
                    </tr>
                    <tr>
                        <td  valign="top" class="myHead" style="width:30%">Description</td>
                        <td valign="top" style="width:70%">
                            <textarea cols="" rows=""  name="dashboardDesc" id="dashboardDesc" style="width:150px"></textarea>
                        </td>
                    </tr>
                </table>
                <table>
                    <tr>
                        <td><input type="button" class="prgBtn" style="width:auto" value="Next" id="dashboardsave" onclick="saveDashboard()"></td>
                            <%--<td><input type="button" class="prgBtn" style="width:auto" value="Cancel" onclick="cancelDashboard()"></td>--%>
                    </tr>
                </table>
            </center>
        </div>



                    <div id="timeBasedDash" title="Create TimeBased Dashboard" style="display:none">
            <center>
                <br>
                <table style="width:100%" border="0">
                    <tr>
                        <td valign="top" class="myHead" style="width:40%">Dashboard Name</td>
                        <td valign="top" style="width:60%">
                                <input type="text"  name="timeBaseddashboardName" style="width:150px" id="timeBaseddashboardName" onkeyup="tabmsg4()" onfocus="document.getElementById('saveTimeBasedDash').disabled = false;"><br><span id="duplicateTimeBasedDashboard" style="color:red"></span>
                        </td>
                    </tr>
                    <tr>
                        <td  valign="top" class="myHead" style="width:30%">Description</td>
                        <td valign="top" style="width:70%">
                            <textarea cols="" rows=""  name="timeBaseddashboardDesc" id="timeBaseddashboardDesc" style="width:150px"></textarea>
                        </td>
                    </tr>
                </table>
                <table>
                    <tr>
                        <td><input type="button" class="prgBtn" style="width:auto" value="Next" id="saveTimeBasedDash" onclick="saveTimeBasedDash()"></td>
                            <%--<td><input type="button" class="prgBtn" style="width:auto" value="Cancel" onclick="cancelDashboard()"></td>--%>
                    </tr>
                </table>
            </center>
        </div>

        <div id="CreateKPIdash" title="Create KPI Dashboard" style="display:none">
            <center>
                <br>
                <table style="width:100%" border="0">
                    <tr>
                        <td valign="top" class="myHead" style="width:40%">Dashboard Name</td>
                        <td valign="top" style="width:60%">
                                <input type="text"  name="kpidashboardName" style="width:150px" id="kpidashboardName" onkeyup="tabmsg3()" onfocus="document.getElementById('kpidashboardsave').disabled = false;"><br><span id="duplicatekpiDashboard" style="color:red"></span>
                        </td>
                    </tr>
                    <tr>
                        <td  valign="top" class="myHead" style="width:30%">Description</td>
                        <td valign="top" style="width:70%">
                            <textarea cols="" rows=""  name="kpidashboardDesc" id="kpidashboardDesc" style="width:150px"></textarea>
                        </td>
                    </tr>
                                </table>
                <table>
                    <tr>
                        <td><input type="button" class="prgBtn" style="width:auto" value="Next" id="kpidashboardsave" onclick="saveKPIDashboard()"></td>
                            <%--<td><input type="button" class="prgBtn" style="width:auto" value="Cancel" onclick="cancelDashboard()"></td>--%>
                    </tr>
                </table>
            </center>
                            </div>
                    

		<div id="editDashbdDialog" title="Edit Dashboard Name" style="display:none">
            <iframe  id="editdashbdNameFrame" name='editRepName'  width="100%" height="100%" frameborder="0"   src='about:blank' ></iframe>
        </div>
        <div id="editReportDialog" title="Edit Report Name" style="display:none">
            <iframe  id="editRepNameFrame" name='editRepName'  width="100%" height="100%" frameborder="0"  src='about:blank'  ></iframe>
        </div>
        <!-- added by uday -->
        <div id="copyUser" title="Copy User"  style="display:none;">
            <iframe src='about:blank' height="100%" width="100%" frameborder="0" id="copyUserFrame"></iframe>
        </div>

        <!-- added by uday 11-feb-2010-->
        <div id="whatIfScenarioMeta" title="Create What-If Scenario" style="display:none">
            <center>
                <br>
                <table style="width:80%" border="0">
                    <tr>
                        <td valign="top" nowrap style="width:30%">What-If Scenario Name</td>
                        <td valign="top" style="width:80%">
                            <input type="text" maxlength="35" name="whatIfName" style="width:80%" id="whatIfName" onkeyup="tabmsg2()" onfocus="document.getElementById('save').disabled = false;"><br><span id="whatIfDuplicate" style="color:red"></span>
                        </td>
                    </tr>
                    <tr>
                        <td  valign="top" style="width:30%">Description</td>
                        <td valign="top" style="width:70%">
                            <textarea cols="" rows="4" name="whatIfDesc" id="whatIfDesc" style="width:80%"></textarea>
                        </td>
                    </tr>
                </table>
                <table>
                    <tr>
                        <td><input type="button" class="prgBtn" style="width:auto" value="Next" id="whatIfSave" onclick="saveWhatIfScenario()"></td>
                    </tr>
                </table>
            </center>
        </div>

        <div id="CreateDataSnapshotDiv" title="Save As HTML" style="display:none">
            <center>
                <br>
                <table style="width:100%" >
                    <tr>
                        <td valign="top" style="width: 40%;" class="myHead">Refresh</td>
                        <td><select id="selectedInterval"  onchange="showRefreshOptions()">

                                <option value="NoRefresh">No Refresh</option>
                                <option value="TimeInt">Time Interval</option>
                            </select>
                        </td>
                    </tr>

                    <tr id="TimeIntervalOptions" style="display:none">
                        <td valign="top" style="width: 40%;" class="myHead">Time Interval</td>
                        <td><input type="text" name="TimeInMin" value="" id="TimeInMin" style="width:100px">
                        </td>
                        <td><select id="TimeDuration">
                                <option value="Days">Days</option>
                                <option value="Hrs">Hours</option>
                                <option value="Mins">Minutes</option>
                            </select>
                        </td>
                    </tr>
                </table>
                <table>
                    <tr>
                        <td><input type="button" class="prgBtn" style="width:auto" value="save" id="save" onclick="saveAsSnapshot('<%=request.getContextPath()%>')"></td>
                    </tr>
                </table>
            </center>
        </div>
                    <div id="assingHtmlReps" title="Assing Html Reports" style="display:none">
                          <iframe  id="userAssignDisp" NAME='userAssignDisp' height="100%" width="100%"  frameborder="0" src='about:blank'></iframe>
                    </div>
        <div id="downloadDialog" title="Download" style="display:none">
            <center>
                <br>
                <table style="width:100%" >
                    <tr>
                        <td valign="top" style="width: 150px;" style="color:#000;font-weight: bolder;font-size: large">Download For All ViewBy</td>
                        <td width="8px"></td>
                        <td><input type="checkbox" id="downloadViewBy" name="downloadViewBy" /></td>
                    </tr>
                    <Tr>
                        <td  align="center" colspan="4" height="10px" ></td>
                    </Tr>
                </table>

                <table>
                    <tr>
                        <td><input type="button" class="prgBtn" style="width:auto" value="Download" id="downloadAllViewBy" onclick="downloadAllViewBySnapshot('<%=request.getContextPath()%>')"></td>
                    </tr>
                </table>
            </center>
        </div>

        <div id='loading' class='loading_image' style="display:none;">
            <img alt=""  id='imgId' src='images/help-loading.gif'  border='0px' style='position:absolute; left: 150px; top: 10px;'/>
        </div>
                     <div id="snapShotNameDiv" style="display:none" >
            <table>
                <tr>
                        <td valign="top" class="myHead" style="width:40%">Snapshot Name</td>
                            <td valign="top" style="width:60%">
                                <input type="text" maxlength="35" name="snapShotName" style="width:80%" id="snapShotName" onkeyup="tabmsg1()" onfocus="document.getElementById('save').disabled = false;"><br><span id="duplicate" style="color:red"></span>
                            </td>
                        </tr>

            </table>
              <br/><br/>
            <table align="center">
                <tr>
                    <td>
                        <input type="button" align="center" value="Next" onclick="getSnapShotName()">
                    </td>
                </tr>
            </table>
        </div>
                    <input type="hidden" name="htmlType" id="htmlType" value=""/>

                    <div id="ReportDetails" title="ReportDetails" style="display:none">

                    </div>
                    <div id="hometabs" title="Home Tabs" style="display:none"></div>
                            <div id="createReportNew" title="Create Report" style="display:none">
                <center>
                    <br><br>

                    <table style="width:80%" border="0">
                        <tr>
                            <td valign="top" class="myHead" style="width:40%">Report Name</td>
                            <td valign="top" style="width:60%">
                                <input type="text" maxlength="35" name="newreportName" style="width:80%" id="newreportName" onkeyup="tabmsg1()" onfocus="document.getElementById('save').disabled = false;"><br><span id="duplicate" style="color:red"></span>
                            </td>
                        </tr>
                        <tr>
                            <td  valign="top" class="myHead" style="width:30%">Description</td>
                            <td valign="top" style="width:70%">
                                 <textarea cols=""   rows=""  name="newreportDesc" id="newreportDesc" style="width:80%"></textarea>
                            </td>
                        </tr>
                    </table>
                    <table>
                        <tr>
                            <td><input type="button" class="prgBtn" value="Next" id="save" onclick="saveReport1()"></td>
                            <td><input type="button" class="prgBtn" value="Cancel" onclick="cancelReport()"></td>
                        </tr>
                    </table>

                </center>
            </div>
<!--Start of code By Bhargavi Parsi on 30th nov 2015-->
                     <div id="createReportwithAO" title="Create Report With AO" style="display:none">
                <center>
                    <br><br>

                    <table style="width:80%" border="0">
                        <tr>
                            <td valign="top" class="myHead" style="width:40%">Report Name</td>
                            <td valign="top" style="width:60%">
                                <input type="text" maxlength="35" name="newreportNameAO" style="width:80%" id="newreportNameAO" onkeyup="tabmsgAO()" onfocus="document.getElementById('save').disabled = false;"><br><span id="duplicate" style="color:red"></span>
                            </td>
                        </tr>
                        <tr>
                            <td  valign="top" class="myHead" style="width:30%">Description</td>
                            <td valign="top" style="width:70%">
                                 <textarea cols=""   rows=""  name="newreportDescAO" id="newreportDescAO" style="width:80%"></textarea>
                            </td>
                        </tr>
                    </table>
                    <table>
                        <tr>
                            <td><input type="button" class="prgBtn" value="Next" id="save" onclick="saveReportNameAO()"></td>
                            <td><input type="button" class="prgBtn" value="Cancel" onclick="cancelReport()"></td>
                            <td><input type="hidden" name="AO Flag" id="aoFlag" value="true"></td>
                        </tr>
                    </table>

                </center>
            </div>
<!--end of code by Bhargavi-->
           <div id="createReportNew1" title="Create Custom Report" style="display:none">
                <center>
                    <br><br>

                    <table style="width:80%" border="0">
                        <tr>
                            <td valign="top" class="myHead" style="width:40%">Report Name</td>
                            <td valign="top" style="width:60%">
                                <input type="text" maxlength="35" name="newreportName1" style="width:80%" id="newreportName1" onkeyup="tabmsg22()" onfocus="document.getElementById('save').disabled = false;"><br><span id="duplicate" style="color:red"></span>
                            </td>
                        </tr>
                        <tr>
                            <td  valign="top" class="myHead" style="width:30%">Description</td>
                            <td valign="top" style="width:70%">
                                 <textarea cols=""   rows=""  name="newreportDesc1" id="newreportDesc1" style="width:80%"></textarea>
                            </td>
                        </tr>
                    </table>
                    <table>
                        <tr>
                            <td><input type="button" class="prgBtn" value="Next" id="save" onclick="saveReport2()"></td>
                            <td><input type="button" class="prgBtn" value="Cancel" onclick="cancelReport()"></td>
                        </tr>
                    </table>

                </center>
            </div>
           <div id="createMgmtDashboard" title="Create Management Dashboard" style="display:none">
                <center>
                    <br><br>

                    <table style="width:80%" border="0">
                        <tr>
                            <td valign="top" class="myHead" style="width:40%">Name</td>
                            <td valign="top" style="width:60%">
                                <input type="text" maxlength="35" name="newMgmtDashboardName" style="width:80%" id="newMgmtDashboardName" onkeyup="tabMgmtDB()" onfocus="document.getElementById('save').disabled = false;"><br><span id="duplicate" style="color:red"></span>
                            </td>
                        </tr>
                        <tr>
                            <td  valign="top" class="myHead" style="width:30%">Description</td>
                            <td valign="top" style="width:70%">
                                 <textarea cols=""   rows=""  name="newMgmtDashboardDesc" id="newMgmtDashboardDesc" style="width:80%"></textarea>
                            </td>
                        </tr>
                    </table>
                    <table>
                        <tr>
                            <td><input type="button" class="prgBtn" value="Next" id="save" onclick="createMgmtDB('<%=contxtPath%>')"></td>
                            <td><input type="button" class="prgBtn" value="Cancel" onclick="cancelMgmtDB()"></td>
                        </tr>
                    </table>

                </center>
            </div>
            <div id="createMgmtDBBox" title="Select Measures" style="display:none">
                <iframe  id="createMgmtDBFrame" name='createMgmtDBFrame' width="100%" height="100%" frameborder="0"  ></iframe>
            </div>
            <div id="selectPagesBox" title="Select Pages" style="display:none">
                <iframe  id="selectPagesFrame" name='selectPagesFrame' width="100%" height="100%" frameborder="0"  ></iframe>
            </div>
<!--            added by Dinanath-->
<div id="displayLocaleLanguage" title="Choose Default Language">

                    <div id="createAONew" title="Create Custom AO" style="display:none">
                <center>
                    <br><br>

                    <table style="width:80%" border="0">
                        <tr>
                            <td valign="top" class="myHead" style="width:40%">AO Name</td>
                            <td valign="top" style="width:60%">
                                <input type="text" maxlength="35" name="newAOName" style="width:80%" id="newAOName" onkeyup="tabmsgAONew()" onfocus="document.getElementById('save').disabled = false;"><br><span id="duplicate" style="color:red"></span>
                            </td>
                        </tr>
                        <tr>
                            <td  valign="top" class="myHead" style="width:30%">Description</td>
                            <td valign="top" style="width:70%">
                                 <textarea cols=""   rows=""  name="newAODesc" id="newAODesc" style="width:80%"></textarea>
                            </td>
                        </tr>
                    </table>
                    <table>
                        <tr>
                            <td><input type="button" class="prgBtn" value="Next" id="save" onclick="saveReportAO1()"></td>
                            <td><input type="button" class="prgBtn" value="Cancel" onclick="cancelReport()"></td>
                        </tr>
                    </table>

                </center>
</div>
    <div id="ScheduleAO" style="display:none" title="<%=TranslaterHelper.getTranslatedInLocale("Schedule_AO", cL)%>" style="height:360px;">
            <form action="" name="dailyAoScheduleForm" id="dailyAoScheduleForm" method="post">
                <table>
                    <tr>
                        <td class="myalertheader"><%=TranslaterHelper.getTranslatedInLocale("Schedule_Name", cL)%></td>
                        <td><input id="AOname" name="AOname" type="text"  readonly></td>
                    </tr>
                    <tr>
                        <td class="myalertheader"><%=TranslaterHelper.getTranslatedInLocale("Load_Type", cL)%></td>
                          <td  class="pdg-left-2">
                            <select id="loadType" name="loadType">
                                <option value="Truncate and Load">Truncate and Load</option>
                                <option value="Delete and Load">Delete and Load</option>
                                <option value="Insert">Insert</option>
                            </select>
                        </td>
                    </tr>
                    <tr id="emailId">
                        <td class="myalertheader"><%=TranslaterHelper.getTranslatedInLocale("Email_To", cL)%></td>
                        <td class="pdg-left-2"><textarea id="usermailtextarea" style="width: 250px; height: 80px;" rows="" cols="" name="usermailtextarea"></textarea></td>
                    </tr>
                    <tr><td><input type="radio" id="Normal_Date" name="dateSelect" onclick="showDateSel();">Normal Date Selection</td></tr>

                    <tr id="singleDate" style="display: none"><td class="myalertheader"><%=TranslaterHelper.getTranslatedInLocale("Select_Date", cL)%></td><td><input id="SelectedDate"  type="text" value="" style="width: 120px;" maxlength="100" name="SelectedDate" readonly="" onclick="selectScheduleDate();"></td></tr>
                    
                     <tr><td><input type="radio" id="Custom_Date" name="dateSelect" onclick="showDateSel();">Custom Date Selection</td></tr>
                    
                   
                    <tr id="dualDate_st" style="display: none"><td class="myalertheader"><%=TranslaterHelper.getTranslatedInLocale("Load_Start_Date", cL)%></td><td><input id="sDate"  type="text" value="" style="width: 120px;" maxlength="100" name="sDate" readonly="" onclick="selectScheduleStDate();"></td></tr>
                    <tr id="dualDate_ed" style="display: none"><td class="myalertheader"><%=TranslaterHelper.getTranslatedInLocale("Load_End_Date", cL)%></td><td><input id="eDate"  type="text" value="" style="width: 120px;" maxlength="100" name="eDate" readonly="" onclick="selectScheduleEDate();"></td></tr>
                    
                   <tr><td class="myalertheader"><%=TranslaterHelper.getTranslatedInLocale("Start_Date", cL)%></td><td class="pdg-left-2"><input id="sDatepicker"  class='mydate' type="text" value="" style="width: 120px;" maxlength="100" name="startdate" readonly=""></td></tr>
                    <tr><td class="myalertheader"><%=TranslaterHelper.getTranslatedInLocale("End_Date", cL)%></td><td class="pdg-left-2"><input id="eDatepicker"  class='mydate' type="text" value="" style="width: 120px;" maxlength="100" name="enddate" readonly=""></td></tr>
                    
                           <tr><td class="myalertheader"><%=TranslaterHelper.getTranslatedInLocale("Time", cL)%></td>
                               <td class="pdg-left-2"><table><tr>
                                           <td>
                                             hrs<select name="hrs" id="hrs" >
                                            <%for (int i = 00; i < 24; i++) {%>
                                            <option  value="<%=i%>"><%=i%></option>
                                            <%}%>
                                        </select>  
                                           </td>
                                           <td>
                                              mins<select name="mins" id="mins">
                                            <%for (int i = 00; i < 60; i++) {%>
                                            <option  value="<%=i%>"><%=i%></option>
                                            <%}%>
                                        </select> 
                                           </td>
                                       </tr></table>
                        </td>
                    </tr>
                   
                    <tr>
                        <td class="myalertheader"><%=TranslaterHelper.getTranslatedInLocale("Frequency", cL)%></td>
                        <td  class="pdg-left-2">
                            <select id="frequency" name="frequency" onchange="checkFrequency(this.id)">
                                <option value="Daily">Daily</option>
                                <option value="Weekly">Weekly</option>
                                <option value="Monthly">Monthly</option>
                            </select>
                        </td>
                    </tr>
                    <tr id="weekday" style="disaply:none;">
                        <td class="myalertheader"><%=TranslaterHelper.getTranslatedInLocale("Week_Day", cL)%></td>
                        <td class="pdg-left-2">
                            <select id="particularDay" name="particularDay">
                                <% for (int i = 0; i < scheduleday.length; i++) {%>
                                <option value="<%=sday[i]%>"><%=scheduleday[i]%></option>
                                <%}%>
                            </select>
                        </td>
                    </tr>
                    <tr id="monthday" style="display:none;">
                        <td class="myalertheader"><%=TranslaterHelper.getTranslatedInLocale("Month_Day", cL)%></td>
                        <td class="pdg-left-2">
                            <select id="monthParticularDay" name="monthParticularDay">
                                <% for (int i = 1; i <= 31; i++) {%>
                                <option value="<%=i%>"><%=i%></option>
                                <%}%>
                            </select>
                    </td>
                </tr>
                        <tr><td>
                         <input type="hidden" name="dateType" id="dateType">
                    </td></tr>      
                    <tr><td colspan="2">&nbsp; </td></tr>
                    <tr>
                        <td  id="saveScheduleAO" align="center"><input id="saveScheduleAO" class="alert-button-hover" type="button" onclick="SendScheduleAO('<%=ctxPath%>')" value="<%=TranslaterHelper.getTranslatedInLocale("Save_Schedule_AO", cL)%>">
                        
                    </tr>
                    <tr><td colspan="2">&nbsp; </td></tr>
                    <tr><td colspan="2" align="center"><font size="1" color="red">*</font><%=TranslaterHelper.getTranslatedInLocale("Please_separate_multiple_Email_Id_s_by_comma", cL)%>(,).</td></tr>
                </table>
                <input type="hidden" id="schedulerId" name="schedulerId" value="">
            </form>
            
        </div>
<div id="scheduleStDateAO" style="display:none" title="<%=TranslaterHelper.getTranslatedInLocale("scheduleStDateAO", cL)%>">
            <table>
                <tr><td><input type="radio" name="Date" id="st_yestrday" value="st_yestrday"><%=TranslaterHelper.getTranslatedInLocale("Yesterday", cL)%></td><td>&nbsp;&nbsp;&nbsp;<input type="radio" name="Date" id="st_tomorow" value="st_tomorow"><%=TranslaterHelper.getTranslatedInLocale("Tomorrow", cL)%></td>
                    </tr>
                <tr><td><br></td></tr>
                <tr><td><input type="radio" name="Date" id="st_fixeddate" value="st_fixeddate" ><%=TranslaterHelper.getTranslatedInLocale("Fixed_Date", cL)%></td>
                <td class="pdg-left-2"><input id="st_fdatepicker"  type="text" value="" style="width: 120px;" maxlength="100" name="st_fdate" readonly=""></td>
                </tr>
                <tr><td><input type="radio" name="Date" id="st_newSysDate" value="st_newSysDate"><%=TranslaterHelper.getTranslatedInLocale("System_Date", cL)%></td><td><select id="st_sysSign" name="sign">
                            <option value="+">+</option>
                            <option value="-">-</option>
                        </select></td><td><input id="st_newSysVal" type="text"><%=TranslaterHelper.getTranslatedInLocale("Days", cL)%> </td></tr>
                <tr><td><input type="radio" name="Date" id="st_globalDate" value="st_globalDate" ><%=TranslaterHelper.getTranslatedInLocale("Global_Date", cL)%></td><td><select id="st_globalSign" name="st_globalSign">
                            <option value="+">+</option>
                            <option value="-">-</option>
                        </select></td><td><input id="st_newGlobVal" type="text"><%=TranslaterHelper.getTranslatedInLocale("Days", cL)%> </td></tr>
                        
                <tr><td><br></td></tr>
                <tr><td><br></td></tr>
                <tr><td colspan="2" align="center"><input type="button" onclick="scheduleStartDate()" value="<%=TranslaterHelper.getTranslatedInLocale("Ok", cL)%>" class="navtitle-hover" style="width:40px;height:25px;color:black"/>&nbsp;&nbsp;&nbsp;
                        </tr>
            </table>
        </div>   
<div id="scheduleDateAO" style="display:none" title="scheduleDateAO">
            <table>
                <tr><td><input type="radio" name="Date" id="yestrday" value="yestrday"><%=TranslaterHelper.getTranslatedInLocale("Yesterday", cL)%></td><td>&nbsp;&nbsp;&nbsp;<input type="radio" name="Date" id="tomorow" value="tomorow"><%=TranslaterHelper.getTranslatedInLocale("Tomorrow", cL)%></td>
                    </tr>
                <tr><td><br></td></tr>
                <tr><td><input type="radio" name="Date" id="fixeddate" value="fixeddate" ><%=TranslaterHelper.getTranslatedInLocale("Fixed_Date", cL)%></td>
                <td class="pdg-left-2"><input id="fdatepicker"  type="text" value="" style="width: 120px;" maxlength="100" name="fdate" readonly=""></td>
                </tr>
                <tr><td><input type="radio" name="Date" id="newSysDate" value="newSysDate"><%=TranslaterHelper.getTranslatedInLocale("System_Date", cL)%></td><td><select id="sysSign" name="sign">
                            <option value="+">+</option>
                            <option value="-">-</option>
                        </select></td><td><input id="newSysVal" type="text"><%=TranslaterHelper.getTranslatedInLocale("Days", cL)%> </td></tr>
                <tr><td><input type="radio" name="Date" id="globalDate" value="globalDate" ><%=TranslaterHelper.getTranslatedInLocale("Global_Date", cL)%></td><td><select id="globalSign" name="globalSign">
                            <option value="+">+</option>
                            <option value="-">-</option>
                        </select></td><td><input id="newGlobVal" type="text"><%=TranslaterHelper.getTranslatedInLocale("Days", cL)%> </td></tr>
                        
                <tr><td><br></td></tr>
                <tr><td><br></td></tr>
                <tr><td colspan="2" align="center"><input type="button" onclick="scheduleDate()" value="<%=TranslaterHelper.getTranslatedInLocale("Ok", cL)%>" class="navtitle-hover" style="width:40px;height:25px;color:black"/>&nbsp;&nbsp;&nbsp;
                        </tr>
            </table>
        </div>   
                        
       <div id="scheduleEDateAO" style="display:none" title="<%=TranslaterHelper.getTranslatedInLocale("scheduleEDateAO", cL)%>">
            <table>
                <tr><td><input type="radio" name="Date" id="ed_yestrday" value="ed_yestrday"><%=TranslaterHelper.getTranslatedInLocale("Yesterday", cL)%></td><td>&nbsp;&nbsp;&nbsp;<input type="radio" name="Date" id="ed_tomorow" value="tomorow"><%=TranslaterHelper.getTranslatedInLocale("Tomorrow", cL)%></td>
                    </tr>
                <tr><td><br></td></tr>
                <tr><td><input type="radio" name="Date" id="ed_fixeddate" value="ed_fixeddate" ><%=TranslaterHelper.getTranslatedInLocale("Fixed_Date", cL)%></td>
                    <td class="pdg-left-2"><input id="ed_fdatepicker"  class='mydate' type="text" value="" style="width: 120px;" maxlength="100" name="ed_fdate" readonly=""></td>
                </tr>
                <tr><td><input type="radio" name="Date" id="ed_newSysDate" value="ed_newSysDate"><%=TranslaterHelper.getTranslatedInLocale("System_Date", cL)%></td><td><select id="ed_sysSign" name="sign">
                            <option value="+">+</option>
                            <option value="-">-</option>
                        </select></td><td><input id="ed_newSysVal" type="text"><%=TranslaterHelper.getTranslatedInLocale("Days", cL)%> </td></tr>
                <tr><td><input type="radio" name="Date" id="ed_globalDate" value="ed_globalDate" ><%=TranslaterHelper.getTranslatedInLocale("Global_Date", cL)%></td><td><select id="ed_globalSign" name="ed_globalSign">
                            <option value="+">+</option>
                            <option value="-">-</option>
                        </select></td><td><input id="ed_newGlobVal" type="text"><%=TranslaterHelper.getTranslatedInLocale("Days", cL)%> </td></tr>
                        
                <tr><td><br></td></tr>
                <tr><td><br></td></tr>
                <tr><td colspan="2" align="center"><input type="button" onclick="scheduleEndDate()" value="<%=TranslaterHelper.getTranslatedInLocale("Ok", cL)%>" class="navtitle-hover" style="width:40px;height:25px;color:black"/>&nbsp;&nbsp;&nbsp;
                        </tr>
            </table>
        </div>
  <script type="text/javascript">
            $(document).ready(function(){
                
                $("#st_fdatepicker").datepicker({
                    changeMonth: true,
                    changeYear: true
                });
                $("#fdatepicker").datepicker({
                    changeMonth: true,
                    changeYear: true
                });
                $("#ed_fdatepicker").datepicker({
                    changeMonth: true,
                    changeYear: true
                });
                $("#sdatepicker").datepicker({
                    changeMonth: true,
                    changeYear: true
                });
                $("#edatepicker").datepicker({
                    changeMonth: true,
                    changeYear: true
                });
                if ($.browser.msie == true){
                    $("#editTarget").dialog({
                        autoOpen: false,
                        height: 400,
                        width: 450,
                        position: 'justify',
                        modal: true
                    });
                     $("#ScheduleAO").dialog({
                    autoOpen: false,
                    height: 460,
                    width: 500,
                    position: 'justify',
                    modal: false
                });
                 $("#scheduleStDateAO").dialog({
                    autoOpen: false,
                    height: 300,
                    width: 500,
                    position: 'justify',
                    modal: false
                });
                 $("#scheduleDateAO").dialog({
                    autoOpen: false,
                    height: 300,
                    width: 500,
                    position: 'justify',
                    modal: false
                });
               
                 $("#scheduleEDateAO").dialog({
                    autoOpen: false,
                    height: 300,
                    width: 500,
                    position: 'justify',
                    modal: false
                });
                    $(".navigateDialog").dialog({
                        autoOpen: false,
                        height: 620,
                        width: 820,
                        position: 'justify',
                        modal: true
                    });
                    $("#reportMeta").dialog({
                        autoOpen: false,
                        height: 200,
                        width: 300,
                        position: 'justify',
                        modal: true,
                        title:'Create Report'
                    });
                      $("#reportMetaData").dialog({
                        autoOpen: false,
                        height: 700,
                        width: 700,
                        position: 'justify',
                        modal: true

                    });
                    $("#Createdash").dialog({
                        autoOpen: false,
                        height: 300,
                        width: 350,
                        position: 'justify',
                        modal: true
                    });
                    $("#timeBasedDash").dialog({
                        autoOpen: false,
                        height: 300,
                        width: 350,
                        position: 'justify',
                        modal: true
                    });
                    $("#editReportDialog").dialog({
                        autoOpen: false,
                        height: 400,
                        width: 400,
                        position: 'justify',
                        modal: true
                    });
                    $("#copyUser").dialog({
                        autoOpen: false,
                        height: 400,
                        width: 400,
                        position: 'justify',
                        modal: true
                    });
                    //added by uday on 20-mar-2010
                    $("#whatIfScenarioMeta").dialog({
                        autoOpen: false,
                        height: 300,
                        width: 300,
                        position: 'justify',
                        modal: true
                    });
                    $("#editDashbdDialog").dialog({
                        autoOpen: false,
                        height: 400,
                        width: 400,
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
                    $("#downloadDialog").dialog({
                    autoOpen: false,
                    height: 200,
                    width: 300,
                    position: 'justify',
                    modal: true
                });$("#snapShotNameDiv").dialog({
                    autoOpen: false,
                    height: 150,
                    width: 250,
                    position: 'justify',
                    modal: true
                });
                $("#CreateDataSnapshotDiv").dialog({
                    autoOpen: false,
                    height: 200,
                    width: 300,
                    position: 'justify',
                    modal: true
                });
                $("#ReportDetails").dialog({
                    autoOpen: false,
                    height: 250,
                    width: 300,
                    position: 'justify',
                    modal: true
                });
                $("#hometabs").dialog({
                    autoOpen: false,
                    height: 560,
                    width: 750,
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
                                    $("#createReportwithAO").dialog({
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
                           $("#createMgmtDashboard").dialog({
                        autoOpen: false,
                        height: 200,
                        width: 300,
                        position: 'justify',
                        modal: true
            });
            $("#createAONew").dialog({
                        autoOpen: false,
                        height: 200,
                        width: 300,
                        position: 'justify',
                        modal: true
            });

//                           $("#createReportNew2").dialog({
//                        autoOpen: false,
//                        height: 200,
//                        width: 300,
//                        position: 'justify',
//                        modal: true
//            });
//added by Dinanath
 $("#displayLocaleLanguage").dialog({
                        autoOpen: false,
                        height: 200,
                        width: 300,
                        position: 'justify',
                        modal: true
            });
                }
                else{
                     $("#ScheduleAO").dialog({
                    autoOpen: false,
                    height: 460,
                    width: 500,
                    position: 'justify',
                    modal: false
                });
                $("#scheduleStDateAO").dialog({
                    autoOpen: false,
                    height: 300,
                    width: 500,
                    position: 'justify',
                    modal: false
                });
                $("#scheduleDateAO").dialog({
                    autoOpen: false,
                    height: 300,
                    width: 500,
                    position: 'justify',
                    modal: false
                });
               
                $("#scheduleEDateAO").dialog({
                    autoOpen: false,
                    height: 300,
                    width: 500,
                    position: 'justify',
                    modal: false
                });
                    $("#ReportDetails").dialog({
                    autoOpen: false,
                    height: 250,
                    width: 300,
                    position: 'justify',
                    modal: true
                    })
                     $("#reportMetaData").dialog({
                        autoOpen: false,
                        height: 600,
                        width: 700,
                        position: 'justify',
                        modal: true

                    });
                    $("#editTarget").dialog({
                        autoOpen: false,
                        height: 300,
                        width: 400,
                        position: 'justify',
                        modal: true
                    });
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
                        modal: true,
                        title:'Create Report'
                    });
                    $("#Createdash").dialog({
                        autoOpen: false,
                        height: 200,
                        width: 350,
                        position: 'justify',
                        modal: true
                    });
                    $("#timeBasedDash").dialog({
                        autoOpen: false,
                        height: 200,
                        width: 350,
                        position: 'justify',
                        modal: true
                    });
                    $("#editReportDialog").dialog({
                        autoOpen: false,
                        height: 400,
                        width: 400,
                        position: 'justify',
                        modal: true
                    });
                    $("#copyUser").dialog({
                        autoOpen: false,
                        height: 250,
                        width: 400,
                        position: 'justify',
                        modal: true
                    });
                    //added by uday on 20-mar-2010
                    $("#whatIfScenarioMeta").dialog({
                        autoOpen: false,
                        height: 200,
                        width: 500,
                        position: 'justify',
                        modal: true
                    });
                    $("#editDashbdDialog").dialog({
                        autoOpen: false,
                        height: 400,
                        width: 400,
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

                $("#CreateDataSnapshotDiv").dialog({
                    autoOpen: false,
                    height: 200,
                    width: 300,
                    position: 'justify',
                    modal: true
                });
                 $("#downloadDialog").dialog({
                    autoOpen: false,
                    height: 100,
                    width: 300,
                    position: 'justify',
                    modal: true
                });
                $("#snapShotNameDiv").dialog({
                    autoOpen: false,
                    height: 150,
                    width: 250,
                    position: 'justify',
                    modal: true
                });
                $("#assingHtmlReps").dialog({
                     autoOpen: false,
                     height: 550,
                     width: 700,
                     position: 'justify',
                     modal: true
                });
                 $("#hometabs").dialog({
                    autoOpen: false,
                    height: 560,
                    width: 750,
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
            $("#createReportwithAO").dialog({
                        autoOpen: false,
                        height: 200,
                        width: 300,
                        position: 'justify',
                        modal: true
                    });

            $("#displayLocaleLanguage").dialog({
                        autoOpen: false,
                        height: 200,
                        width: 300,
                        position: 'justify',
                        modal: true
            });
            $("#createAONew").dialog({
                        autoOpen: false,
                        height: 200,
                        width: 300,
                        position: 'justify',
                        modal: true
            });
                }
//                added by Dinanath
                $.ajax({
                    async:false,
                            data:{'userId':<%=userid%>},
                            url:'baseAction.do?param=getDisplayAllLocaleAvailable',
                            success: function(data){
//                            alert(data);

                            $("#displayLocaleLanguage").html('');
                            if(data=="Already Availble"){

                            }else{
                            $("#displayLocaleLanguage").html(data);
                            $("#displayLocaleLanguage").dialog('open');
                            }

                            }
                        });
//                jQuery(window).bind("beforeunload", function(){
//                    var confirmVar= confirm("Do you really want to close?")
//                    if(confirmVar){
//                         $.ajax({
//                            url:'baseAction.do?param=killBrowserSession',
//                            success: function(data){
//                                if(data=='success')
//                                alert("U R Session has been Expired")
//                            }
//                        });
//                    }
//
//                            })
                $("#breadCrumb").jBreadCrumb();
                $("#tabs").tabs().find(".ui-tabs-nav").sortable({axis:'x'});
                 $("#tabs1").tabs().find(".ui-tabs-nav").sortable({axis:'x'}).hide();
//
                if ( $('#warnUser').val() == 'true' )
                {
                    $('#warnUser').val('false');
                    $('#warnUser').remove();
                    alert("A user with the same Username has already logged in");
                    var login = confirm("If you want to work on this session, the other user session will be logged off. Press Ok to continue");
                    if ( login )
                    {
                        $.ajax({
                            url:'baseAction.do?param=killDuplicateAndLogin',
                            success: function(data){
                            }
                        });
                    }
                    else
                    {
                        $.ajax({
                            url:'baseAction.do?param=removeDuplicateUser',
                            success: function(data){
                            }
                        });
                        logout();
                    }
                }
            });
            function setSelectedDefaultLocaleLanguage(){
//            var language = $('input:radio[name=langValue]:checked').val();
//            var idLangCountryCode=$('input[type=radio][name=langValue]:checked').attr('id');
            var language = $("#myselect").val();
            var idLangCountryCode=$('#myselect option:selected').attr('id');
//            alert(language);
//            alert(idLangCountryCode);
            $("#displayLocaleLanguage").dialog('close');

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
                             <script type="text/javascript">
              function viewScorecard(scardId){
                var ctxPath = '<%=request.getContextPath()%>';
                //                $.post('scorecardDesign.do?scorecardParam=viewScorecard&scorecardId=',$("#scorecardForm").serialize())


                document.scorecardForm.action='<%=request.getContextPath()%>/scorecardDesign.do?scorecardParam=viewScorecard&scorecardId='+scardId;
                document.scorecardForm.submit();
            }

            function viewReport(path){
                document.forms.myForm.action=path;
                document.forms.myForm.submit();
            }
//            function logout(){
//                document.forms.myFormH.action="baseAction.do?param=logoutApplication";
//                document.forms.myFormH.submit();
//            }
//             function goGlobe(){
//                $(".navigateDialog").dialog('open');
//              document.getElementById("reportstartIframe").src="startPage.jsp"
//                //$("#reportstartIframe").src="startPage.jsp";
//            }
//            function gohome(){
//                alert("hi i am in home.jsp");
//                document.forms.myFormH.action="baseAction.do?param=goHome";
//                document.forms.myFormH.submit();
//            }
            function gouser(){
                document.forms.myFormH.action="userList.jsp";
                document.forms.myFormH.submit();
            }
            function goTest(){
            }
            function hideremain(ctxPath){                
               // document.getElementById("RolesTab").style.display = "none";
                document.forms.myFormH.action=ctxPath+"/AdminTab.jsp";
                document.forms.myFormH.submit();
                }
            function showhiden(){
                document.getElementById("RolesTab").style.display = "block";
            }
            function tabmsgPortal(){
                document.getElementById('portalDesc').value = document.getElementById('portalName').value;
            }
            function goPortal(){
               
                $.ajax({
                    url: 'portalTemplateAction.do?paramportal=checkUserPortalExist',
                    success: function(data){
//                        if(data!=""){
//                            var portallist=data.split("~");
//                            var portalname=portallist[0];
//                            var portalId=portallist[1];

//                            document.forms.myFormH.action='portalViewer.do?portalBy=viewPortal&PORTALID='+portalId+'&PORTALNAME='+portalname;
                            document.forms.myFormH.action='portalViewer.do?portalBy=viewPortals';
                            document.forms.myFormH.submit();
//                        }
//                        else if(data==''){
//                            document.getElementById('portal').style.display='block';
//                            document.getElementById('fade').style.display='block';
//                        }
                    }
                });
            }
            function goSearchpage(){
                document.forms.myFormH.action='srchQueryAction.do?srchParam=getsearchPage';
                document.forms.myFormH.submit();
            }
            function oneViewBy(){
                document.forms.myFormH.action='srchQueryAction.do?srchParam=oneViewBy';
                document.forms.myFormH.submit();
            }
            function IcalPage(){
                document.forms.myFormH.action='srchQueryAction.do?srchParam=icalPage';
                document.forms.myFormH.submit();
            }
            function groupMeassure(){
                document.forms.myFormH.action='srchQueryAction.do?srchParam=groupMeassure';
                document.forms.myFormH.submit();
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
                    url: 'portalTemplateAction.do?paramportal=checkPortalName&portalName='+portalName+'&portalDesc='+portalDesc,
                    success: function(data){
                        if(data!=""){
                            document.getElementById('duplicatePortal').innerHTML = data;
                            document.getElementById('portalsave').disabled = true;
                        }
                        else if(data==''){
                            $.ajax({
                                url: 'portalTemplateAction.do?paramportal=insertPortalMaster&portalName='+portalName+'&portalDesc='+portalDesc,
                                success: function(data){

                                    var portallist=data.split("~");
                                    var portalname=portallist[0];
                                    var portalId=portallist[1];
                                    document.forms.myFormH.action='portalViewer.do?portalBy=viewPortals';
                                    document.forms.myFormH.submit();
                                }
                            });

                        }
                    }
                });
            }
            function viewDashboardG(path){
                document.forms.myFormH.action=path;
                document.forms.myFormH.submit();
            }
            function viewReportG(path){
                document.forms.myFormH.action=path;
                document.forms.myFormH.submit();
            }
            function goPaths(path){
                var modulecode=path.replace("home.jsp#","");
                //modified by anitha for business roles tab
                var ctxpath = '<%=request.getContextPath()%>';
                $.ajax({
                                url: ctxpath+"/portalTemplateAction.do?paramportal=setModuleNameInSession&modulecode="+modulecode,
                                success: function(data){
                if(modulecode=='Dashboard_Studio' || modulecode=='Report_Studio'|| modulecode=='Report_Builder' || modulecode=='AO_Builder' || modulecode=='MO_Builder'){
                    if(!<%=isPowerAnalyserEnableforUser%>){
                        alert("You do not have the sufficient previlages")
                }
              
                }else if(modulecode=='pbBIManager.jsp'){
                    if(!<%=isQDEnableforUser%>){
                         path="home.jsp";
                    alert("You do not have the sufficient previlages")
                }
                }
                parent.closeStart();
                document.forms.myFormH.action=path;
                document.forms.myFormH.submit();
            }
                });
            }

            function pbBiManager(){
                var path = "";
                if(!<%=isQDEnableforUser%>){
                         path="home.jsp";
                    alert("You do not have the sufficient previlages")
                }
                else {
                    path = "srchQueryAction.do?srchParam=pbBiManager";
                }
                parent.closeStart();
                document.forms.myFormH.action=path;
                document.forms.myFormH.submit();
            }

            function gotoDBCON(ctxPath){
                 if(!<%=isQDEnableforUser%>){
                        alert("You do not have the sufficient previlages")
                    }else{
                document.forms.myFormH.action=ctxPath+"/pbBase.jsp?pagename=Query Studio&curntpage=Database Connection#Database_Connection";
                document.forms.myFormH.submit();
                }
            }
             function gotoDBCON1(ctxPath,repid){
                document.forms.myFormH.action=ctxPath+"/reportViewer.do?reportBy=viewReport&REPORTID="+repid+"&action=open";
                document.forms.myFormH.submit();

            }
             function gotoDBCON2(ctxPath,repid){

                document.forms.myFormH.action=ctxPath+"/dashboardViewer.do?reportBy=viewDashboard&REPORTID="+repid;
                document.forms.myFormH.submit();

            }
//            function createReport(){
//                $("#reportMeta").dialog('open');
//            }
            function createReport(copyReportFlag){
                var title = $("#reportMeta").dialog( "option", "title" );
                var parentTd=document.getElementById("btnCol");
                parentTd.innerHTML="";
                var btnVal=document.createElement("input");
                btnVal.id="save";
                btnVal.type="button";
                btnVal.className="prgBtn";
                btnVal.style.width="auto";
                if(copyReportFlag){
                    $( "#reportMeta" ).dialog( "option", "title", 'Copy Report' );
                    btnVal.value="Copy";
                    btnVal.onclick=function(){
                        saveCopyReport('<%=request.getContextPath()%>');
                    };
                }
                else{
                    $( "#reportMeta" ).dialog( "option", "title", 'Create Report' );
                    btnVal.value="Next";
                    btnVal.onclick=function(){
                        saveReport();
                    };
                }
                parentTd.appendChild(btnVal);
                $("#reportMeta").dialog('open');

            }
            function closeReport(){
              //  $("#reportMeta").dialog('close');
               //added by Mayank...
                $("#createReportNew").dialog('close');
            }

            function createDash(){
                $("#Createdash").dialog('open');
            }
            function closeDash(){
                $("#Createdash").dialog('close');
            }
            function timeBasedDash(){
                $("#timeBasedDash").dialog('open');
            }
            function closetimeBasedDash(){
                $("#timeBasedDash").dialog('close');
            }

            function editRepName(source){
                var frameObj = document.getElementById("editRepNameFrame");
                frameObj.src = source;
                $("#editReportDialog").dialog('open');
            }
            function closeEditRep(){
                $("#editReportDialog").dialog('close');
            }

            function editdashbdName(sourcedb){
                var dbframe = document.getElementById("editdashbdNameFrame");
                dbframe.src=sourcedb;
                $("#editDashbdDialog").dialog('open');
            }
            function closeDashbdEdit(){
                $("#editDashbdDialog").dialog('close');
            }

            //added by uday on 20-mar-2010
            function createWhatIfScenario(){
                document.getElementById("whatIfName").value = "";
                document.getElementById("whatIfDesc").value = "";
                $("#whatIfScenarioMeta").dialog('open');
            }

           function showRefreshOptions()
             {
                var selectedInterval=document.getElementById("selectedInterval").value;
                if(selectedInterval=="TimeInt"){
                    document.getElementById("TimeIntervalOptions").style.display='';
                }
                else{
                     document.getElementById("TimeIntervalOptions").style.display='none';
                }
            }
             function saveAsSnapshot(varhtml)
            {
              $("#CreateDataSnapshotDiv").dialog('close');
                if(snapName==undefined)
                    snapName="";
                var snapName = $("#snapShotName").val();
                if(varhtml==undefined)
                    varhtml='fromHtml';

                var RepSelectObj=document.getElementsByName("RepSelect");
                if(RepSelectObj==null||RepSelectObj==undefined||RepSelectObj.length==0)
                    RepSelectObj=document.getElementsByName("MyRepSelect");
                var reportIds = new Array();
                for(var i=1;i<RepSelectObj.length;i++)
                {
                    if(RepSelectObj[i].checked )
                    {
                        reportIds.push(RepSelectObj[i].value);
                    }
                }
                var selectedInterval=document.getElementById("selectedInterval").value;
                if(selectedInterval=="TimeInt")
                {
                     var timeSelected=document.getElementById("TimeInMin").value;
                     var timeDuration=document.getElementById("TimeDuration").value;
                     var urlStr='<%=request.getContextPath()%>/dataSnapshot.do?doAction=createDataSnapshot&reportIds='+reportIds.toString()+'&selectedInterval='+selectedInterval+'&varhtml='+varhtml+'&timeDuration='+timeDuration+'&timeValue='+timeSelected+'&snapName='+encodeURIComponent(snapName);
                }
                else
                urlStr='<%=request.getContextPath()%>/dataSnapshot.do?doAction=createDataSnapshot&reportIds='+reportIds.toString()+'&selectedInterval='+selectedInterval+'&varhtml='+varhtml+'&timeDuration=&timeValue='+'&snapName='+encodeURIComponent(snapName),

                $("#loading").show();
                $.ajax({
                    url: urlStr,
                    success: function(data)
                    {
                        $("#loading").hide();
                       alert("Saved HTML successfully")

                    var confimMsg=confirm("Do you want to assign html reports to other Users ?")
                    if(confimMsg){
                     $("#assingHtmlReps").dialog('open');
                     $("#userAssignDisp").attr("src", "getUserAssignList.jsp?grpId=0&roleId=<%=request.getParameter("roleId")%>&flage=fromHtmlReps&reportIds="+reportIds)
                       }

                    }
                });


            }
             function closeTableDetails(){
              $("#reportMetaData").dialog('close');
             }
            function downloadAllViewBySnapshot(ctxPath)
            {
                 var RepSelectObj=document.getElementsByName("RepSelect");
                 if(RepSelectObj==null||RepSelectObj==undefined||RepSelectObj.length==0)
                     RepSelectObj=document.getElementsByName("MyRepSelect");
                var reportIds = new Array();
                var allViewBy=document.getElementById("downloadViewBy");
                var viewDownload="";
                if(allViewBy.checked)
                    viewDownload=true;
                else
                    viewDownload=false;

                for(var i=1;i<RepSelectObj.length;i++){
                    if(RepSelectObj[i].checked){
                        reportIds.push(RepSelectObj[i].value);
                    }
                    else{
                        RepSelectObj[i].checked=false;
                    }
                }
                 $("#downloadDialog").dialog('close');
                $("#loading").show();
                $.ajax({
                    url:ctxPath+"/dataSnapshot.do?doAction=downloadDataSnapshot&reportIds="+reportIds.toString()+"&allViewBy="+viewDownload,
                    success: function(data){
                        $("#loading").hide();
                        document.forms.myFormH.action = ctxPath+"/PbDownloadServlet?dType=zip&fileName="+data;
                        document.forms.myFormH.submit();
                    }
                });
            }
//            function closeTable()
//            {
//                        parent.$("#Report_Studio").hide();
//                        parent.$("#Dashboard_Studio").hide();
//                        parent.$("#All_Reports").hide();
//                        parent.$("#Scorecard").hide();
//                        parent.$("#Html_Reports").hide();
//                        parent.$("#Headlines").hide();
//                        parent.$("#Global_Parameterss").hide();
//
//
//                        }
//                        function refreshGraphs(source,tabId)
//{
//    alert('helllo in refresh in home.jsp')
//    source = source + "?tabId="+tabId;
//   if($.browser.safari==true){
//
//        frames['iframe4'].location = source;
//    }else{
//        var gSrc = document.getElementById("iframe4");
//        gSrc.src = source;
//
//    }
// document.getElementById('loading').style.display='none';
//
//}
function salesReportFormulaDiv1()
            {

               document.getElementById("hometabs").innerHTML = "<iframe width='100%' height='100%'  frameborder='0' class=frame1 src=edithomepagetabs.jsp?></iframe>";
                $("#hometabs").dialog('open');
            }

//            if ($.browser.msie == true){
//        document.getElementById('loading').style.display='none';
//    }
function refreshpage(){
 window.location.reload(true);
}
        
function createKPIDash1(){
    $("#CreateKPIdash").dialog({
                        autoOpen: false,
                        height: 200,
                        width: 350,
                        position: 'justify',
                        modal: true
                    });
  $("#CreateKPIdash").dialog('open');

 }
function tabmsg22(){
                document.getElementById('newreportDesc1').value = document.getElementById('newreportName1').value;
 }
 function tabMgmtDB(){
                document.getElementById('newMgmtDashboardDesc').value = document.getElementById('newMgmtDashboardName').value;
 }
function tabmsgAONew(){
                document.getElementById('newAODesc').value = document.getElementById('newAOName').value;
 }
function tabmsgAO(){
                document.getElementById('newreportDescAO').value = document.getElementById('newreportNameAO').value;
            }


function deleteKPIDash(){
  $("#CreateKPIdash").dialog('close');
  }
  
  function checkFrequency(id){
                                                  if($("#"+id).val()=="Weekly"){
                                                      $("#weekday").show();
                                                      $("#monthday").hide();
                                                  }else if($("#"+id).val()=="Monthly"){
                                                      $("#weekday").hide();
                                                      $("#monthday").show();
                                                  }else{
                                                      $("#monthday").hide();
                                                      $("#weekday").hide();
                                                  }
                                              }
  
   function selectScheduleStDate() {
    $("#scheduleStDateAO").dialog('open');
}
   function selectScheduleDate() {
    $("#scheduleDateAO").dialog('open');
}
function selectScheduleEDate() {
    $("#scheduleEDateAO").dialog('open');
}
function scheduleDate(){
      var Date="";
      if(document.getElementById("fixeddate") != null && document.getElementById("fixeddate").checked){
                   Date=$("#fdatepicker").val();
        }else if(document.getElementById("yestrday") != null && document.getElementById("yestrday").checked){
            Date = "yestrday";
        }else if(document.getElementById("tomorow") != null && document.getElementById("tomorow").checked){
            Date = "tomorow";
        }else if(document.getElementById("newSysDate") != null && document.getElementById("newSysDate").checked){
            var sysSign = $("#sysSign").val();
            var newSysVal = $("#newSysVal").val();
            if(newSysVal==""){
                newSysVal="0";
            }
            if(sysSign=="-"){
            Date = "newSysDate,".concat(sysSign,newSysVal);
        }else{
            Date = "newSysDate,".concat(newSysVal);
        }
        Date = Date.toString().replace(" ", "+", "gi");           
        }else if(document.getElementById("globalDate") != null && document.getElementById("globalDate").checked){
            var globalSign = $("#globalSign").val();
            var newGlobVal = $("#newGlobVal").val();
            if(newGlobVal==""){
                newGlobVal="0";
            }
            if(globalSign=="-"){
            Date = "globalDate,".concat(globalSign,newGlobVal);
        }else{
             Date = "globalDate,".concat(newGlobVal);
        }
            Date = Date.toString().replace(" ", "+", "gi");
        }
        
      $("#SelectedDate").val(Date); 
      $("#scheduleDateAO").dialog('close');
  }
  
  function scheduleStartDate(){
      var Date="";
      if(document.getElementById("st_fixeddate") != null && document.getElementById("st_fixeddate").checked){
                   Date=$("#st_fdatepicker").val();
        }else if(document.getElementById("st_yestrday") != null && document.getElementById("st_yestrday").checked){
            Date = "yestrday";
        }else if(document.getElementById("st_tomorow") != null && document.getElementById("st_tomorow").checked){
            Date = "tomorow";
        }else if(document.getElementById("st_newSysDate") != null && document.getElementById("st_newSysDate").checked){
            var sysSign = $("#st_sysSign").val();
            var newSysVal = $("#st_newSysVal").val();
            if(newSysVal==""){
                newSysVal="0";
            }
            Date = "newSysDate,".concat(sysSign,",", newSysVal);
            Date = Date.toString().replace(" ", "+", "gi");          
        }else if(document.getElementById("st_globalDate") != null && document.getElementById("st_globalDate").checked){
            var globalSign = $("#st_globalSign").val();
            var newGlobVal = $("#st_newGlobVal").val();
            if(newGlobVal==""){
                newGlobVal="0";
            }
            Date = "globalDate,".concat(globalSign,",", newGlobVal);
            Date = Date.toString().replace(" ", "+", "gi");
        }
        
      $("#sDate").val(Date); 
      $("#scheduleStDateAO").dialog('close');
  }
  
  function scheduleEndDate(){
      var Date="";
      if(document.getElementById("ed_fixeddate") != null && document.getElementById("ed_fixeddate").checked){
                   Date=$("#ed_fdatepicker").val();
        }else if(document.getElementById("ed_yestrday") != null && document.getElementById("ed_yestrday").checked){
            Date = "yestrday";
        }else if(document.getElementById("ed_tomorow") != null && document.getElementById("ed_tomorow").checked){
            Date = "tomorow";
        }else if(document.getElementById("ed_newSysDate") != null && document.getElementById("ed_newSysDate").checked){
            var sysSign = $("#ed_sysSign").val();
            var newSysVal = $("#ed_newSysVal").val();
            if(newSysVal==""){
                newSysVal="0";
            }
            Date = "newSysDate,".concat(sysSign,",", newSysVal);
            Date = Date.toString().replace(" ", "+", "gi");           
        }else if(document.getElementById("ed_globalDate") != null && document.getElementById("ed_globalDate").checked){
            var globalSign = $("#ed_globalSign").val();
            var newGlobVal = $("#ed_newGlobVal").val();
            if(newGlobVal==""){
                newGlobVal="0";
            }
            Date = "globalDate,".concat(globalSign,",", newGlobVal);
            Date = Date.toString().replace(" ", "+", "gi");
        }
      $("#eDate").val(Date); 
      $("#scheduleEDateAO").dialog('close');
  }
  function showDateSel(){
       if (document.getElementById('Normal_Date').checked) {
           $("#dateType").val("Normal_Date");
        document.getElementById('singleDate').style.display = 'block';
        document.getElementById('dualDate_ed').style.display = 'none';
        document.getElementById('dualDate_st').style.display = 'none';
    }
    else{ 
         $("#dateType").val("Custom_Date");
        document.getElementById('singleDate').style.display = 'none';
        document.getElementById('dualDate_ed').style.display = 'block';
        document.getElementById('dualDate_st').style.display = 'block';
      }
  }
  //added by Mohit
  function getUserType(){
       var usertype= "<%= userType%>";
       return usertype;
  }
 
//  
        </script>
                       <script type="text/javascript">
    
     $('.mydate').datepicker({
       changeMonth: true,
       changeYear: true
   });
</script>
                    </body>
</html>
<%}%>
