<%@page import="com.progen.report.query.PbReportQuery"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="com.progen.reportview.bd.PbReportViewerBD"%>
<%@page import="com.progen.users.UserLayerDAO"%>
<%@page import="com.progen.reportview.db.PbReportViewerDAO"%>
<%@page import="com.progen.charts.JqplotGraphProperty"%>
<%@page import="com.progen.action.UserStatusHelper"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.progen.graph.info.ProgenGraphInfo"%>
<%@page import="com.progen.report.segmentation.Segment"%>
<%@page import="com.progen.report.whatIf.WhatIfScenario"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@page contentType="text/html"%>
<%@page import="java.awt.Font" %>
<%@page import="utils.db.*" %>
<%@ page  import="java.awt.*" %>
<%@ page  import="java.io.*" %>
<%@ page import="java.util.*" %>
<%@ page import="com.progen.db.*" %>
<%@page import="prg.db.Container"%>
<%@page import="prg.db.PbDb" %>
<%@page import="prg.db.Session"%>
<%@page import="prg.db.PbReturnObject"%>
<%@ page import="com.progen.report.PbReportCollection" %>
<%@page import="com.progen.reportdesigner.db.DashboardTemplateDAO"%>
<%@page import="com.progen.reportdesigner.db.ReportTemplateDAO"%>
<%@page import="java.sql.*"%>
<%@page import="prg.util.screenDimensions"%>
<%@page import="prg.db.DataTracker"%>
<%@page import="com.progen.users.ProgenUser" %>
<%@page import="com.progen.users.PrivilegeManager" %>
<%@page import="java.text.DateFormat" %>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.progen.report.display.DisplayParameters"%>
<%@page import="com.progen.reportview.bd.PbReportViewerBD"%>
<jsp:useBean id="duration" scope="session" class="utils.db.ProgenParam"/>
<jsp:setProperty name="duration" property="*" />
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<jsp:useBean id="brdcrmb" class="com.progen.action.BreadCurmbBean" scope="session"/>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%  String reportId1 = (String) request.getAttribute("REPORTID");
   String ReportLayout = (String) request.getAttribute("ReportLayout"); //added by mohit for kpi and none
    String[] scheduleday = {"Mon", "Tue", "Wed", "Thur", "Fri", "Sat", "Sun"};
    String[] sday = {"2", "3", "4", "5", "6", "7", "1"};
    String[] frequency = {"Daily", "Weekly", "Monthly", "Hourly"};
    Container container = Container.getContainerFromSession(request, reportId1);
    if (container.prevStateCnt == 55) {
        container.prevStateCnt = 0;
    } else {
        container.prevStateCnt = container.prevStateCnt + 1;
    }
    boolean isReportAccessible = container.isReportAccessible();
    String isGraphThere = null;
    boolean isQDEnableforUser = false;
    PbReportCollection collect = new PbReportCollection();
    collect = container.getReportCollect();
    ArrayList<String> timeinfo = collect.timeDetailsArray;
    String[] vals1 = null;
    String autoDate = "";
    String vals = " ";
    vals = timeinfo.toString();
    vals = vals.replace("[", "");
    vals = vals.replace("]", "");
    vals1 = vals.split(",");
    container.evaluateReportDateHeaders();
%>
<%
    response.setHeader("Cache-Control", "no-store");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);
    ServletContext context = getServletContext();
    boolean isPowerAnalyserEnableforUser = false;
    String userType = null;
    HashMap<String, UserStatusHelper> statushelper;
    if (context.getAttribute("helperclass") != null) {
        statushelper = (HashMap) context.getAttribute("helperclass");
        UserStatusHelper helper = new UserStatusHelper();
        if (!statushelper.isEmpty()) {
            helper = statushelper.get(request.getSession(false).getId());
            if (helper != null) {
                isQDEnableforUser = helper.getQueryStudio();
                isPowerAnalyserEnableforUser = helper.getPowerAnalyser();
                userType = helper.getUserType();
            }
        }
    }
    screenDimensions dims = new screenDimensions();
    int pageFont, anchorFont;
    HashMap screenMap = dims.getFontSize(session, request, response);

    if (session.getAttribute("USERID") == null || String.valueOf(screenMap.get("Redirect")).equalsIgnoreCase("Yes")) {
        response.sendRedirect(request.getContextPath() + "/  newpbLogin.jsp");
    }
    if (!String.valueOf(screenMap.get("pageFont")).equalsIgnoreCase("NULL")) {
        pageFont = Integer.parseInt(String.valueOf(screenMap.get("pageFont")));
        anchorFont = Integer.parseInt(String.valueOf(screenMap.get("pageFont"))) + 1;
    } else {
        pageFont = 11;
        anchorFont = 11;
    }
    String PbReportId = (String) request.getAttribute("REPORTID");
    PbDb pbdb = new PbDb();
    String rolesidqry = "select folder_id from prg_ar_report_details where report_id=" + PbReportId;
    PbReturnObject roleobj = pbdb.execSelectSQL(rolesidqry);
    String rolesid = roleobj.getFieldValueString(0, 0);
    String folderName = collect.getReportBizRoleName(rolesid);
    String DefaultArrregations[] = {"", "sum", "avg", "min", "max", "count", "COUNTDISTINCT"};
    String userid = String.valueOf(session.getAttribute("USERID"));
    String COMPARISON_DATE= String.valueOf(session.getAttribute("COMPARISON_DATE"));
	//out.println("COMPARISON_DATE"+COMPARISON_DATE);
    UserLayerDAO userdao = new UserLayerDAO();
    HashMap paramhashmapPA = new HashMap();
    userType = userdao.getUserTypeForFeatures(Integer.parseInt(userid));
    paramhashmapPA = userdao.getFeatureListAnaLyzer(userType, Integer.parseInt(userid));
    ReportTemplateDAO rdao = new ReportTemplateDAO();
    if (rdao.getAutometicDate(PbReportId)) {
        autoDate = "checked";
    }
%>
<%
    String query1 = "select * from PRG_HOME_TABS where user_id=" + userid;
    PbReturnObject retob1 = new PbReturnObject();
    PbDb pbdb1 = new PbDb();
    ArrayList repid1 = new ArrayList();
    ArrayList list1 = new ArrayList();
    ArrayList reporttype1 = new ArrayList();
    retob1 = pbdb1.execSelectSQL(query1);
    for (int i = 0; i < retob1.rowCount; i++) {
        repid1.add(retob1.getFieldValueString(i, 1));
        list1.add(retob1.getFieldValueString(i, 2));
        reporttype1.add(retob1.getFieldValueString(i, 3));
    }
    String resetpath = collect.resetPath;
    PbReportViewerDAO dao = new PbReportViewerDAO();
    dao.setOrganizationNames(container, userid);
    dao.setGlobalParameters(container);
    String reportTitleSize = container.reportTitleSize;
    String reportTitleAlign = container.reportTitleAlign;
    String comp = container.Comparison;

    String sytm = container.isYearCal;
    String grfqry = "select * from prg_ar_graph_master where report_id='" + PbReportId + "'";
    PbReturnObject grfobj = new PbReturnObject();
    grfobj = pbdb.execSelectSQL(grfqry);
    String graphCheck = String.valueOf(session.getAttribute("isGraphThere"));
    if (grfobj.getRowCount() != 0) {
        isGraphThere = "Yes";
    } else if (graphCheck != "" && graphCheck != null && graphCheck.equalsIgnoreCase("isGraphThere")) {
        isGraphThere = "Yes";
    } else {
        isGraphThere = "No_Graphs";
    }
    isGraphThere = "No_Graphs"; //added by mayank to hide jqplot graphs.
    String lastupdateedate = "";
    Connection con = null;
    ArrayList<String> qryelements = collect.reportQryElementIds;
    ProgenParam connectionparam = new ProgenParam();
    String elementId = "";
    if (qryelements != null && !qryelements.isEmpty()) {
        elementId = qryelements.get(0);
        con = connectionparam.getConnection(elementId);
        lastupdateedate = dao.getLastUpdatedDate(con, elementId);
    }
    container.setRepInNewtab("true");//bhargavi
%>
<%
    String piVersion = "piVersion2014";
%>
<script type ="text/javascript">
    var reportIdVar = <%=reportId1%>;
</script>
<style type="text/css">
    .loading_image{
        display: block;
        position: absolute;
        top: 0%;
        left: 0%;
        width: 100%;
        height: 170%;
        background-color: black;
        z-index:1001;
        -moz-opacity: 0.5;
        opacity:.50;
        filter:alpha(opacity=50);
        z-index:1001;
        overflow:hidden;
    }
    #ui-datepicker-div
    {
        z-index: 9999999;
        display: none
    }

    #selectable {
        background:none repeat scroll 0 0 white;
        list-style:none outside none;
        width:60%;
    }
    #selectable li {
        font-family:verdana;
        font-size:11px;
        margin-bottom:1px;
        margin-top:1px;
        padding-bottom:0.4em;
        padding-left:0.4em;
        padding-right:0.4em;
        padding-top:0.4em;
        width:120px;
        background: #b4d9ee;
        cursor:pointer;
        color:black;
        width:auto;
    }
    #selectable li:hover{
        background:#ffffff;
    }
</style>
<%
    String showloadingimage = String.valueOf(request.getAttribute("loadingimage"));
    if (showloadingimage != null) {
        out.println(request.getAttribute("loadingimage"));
    }
    long time_start = System.nanoTime();
    String grphFrmHeight = "500", tabFrmHeight = "370";
    String widgetStatus = "none";
    String favlinkStatus = "none";
    String TableStatus = "block";
    String Mapstatus = "none";
    String SnapshotStatus = "none";
    String TopBotStatus = "none";
    String Msgstatus = "none";
    String BusRolesStatus = "none";
    String ParamStatus = "block";
    String TextStatus = "none";
    String GraphStatus = "block";
    String leftTdStatus = "";
    String imageType = "control-180";
    String manageStickyStatus = "none";
    String themeColor = "blue";
    String ViewFrom = "Viewer";
    String dashboardurl = "";
    if (session.getAttribute("imageType") == null) {
        session.setAttribute("imageType", imageType);
    } else {
        imageType = String.valueOf(session.getAttribute("imageType"));
    }
    if (session.getAttribute("leftTdStatus") == null) {
        session.setAttribute("leftTdStatus", leftTdStatus);
    } else {
        leftTdStatus = String.valueOf(session.getAttribute("leftTdStatus"));
    }

    if (session.getAttribute("tabFrmHeight") == null) {
        session.setAttribute("tabFrmHeight", tabFrmHeight);
    } else {
        tabFrmHeight = String.valueOf(session.getAttribute("tabFrmHeight"));
    }
    if (session.getAttribute("GraphStatus") == null) {
        session.setAttribute("GraphStatus", GraphStatus);
    } else {
        GraphStatus = String.valueOf(session.getAttribute("GraphStatus"));
    }
    if (session.getAttribute("TextStatus") == null) {
        session.setAttribute("TextStatus", TextStatus);
    } else {
        TextStatus = String.valueOf(session.getAttribute("TextStatus"));
    }
    if (session.getAttribute("ParamStatus") == null) {
        session.setAttribute("ParamStatus", ParamStatus);
    } else {
        ParamStatus = String.valueOf(session.getAttribute("ParamStatus"));
    }
    if (session.getAttribute("BusRolesStatus") == null) {
        session.setAttribute("BusRolesStatus", BusRolesStatus);
    } else {
        BusRolesStatus = String.valueOf(session.getAttribute("BusRolesStatus"));
    }
    if (session.getAttribute("Msgstatus") == null) {
        session.setAttribute("MsgStatus", Msgstatus);
    } else {
        Msgstatus = String.valueOf(session.getAttribute("Msgstatus"));
    }
    if (session.getAttribute("TopBotStatus") == null) {
        session.setAttribute("TopBotStatus", TopBotStatus);
    } else {
        TopBotStatus = String.valueOf(session.getAttribute("TopBotStatus"));
    }
    if (session.getAttribute("widgetStatus") == null) {
        session.setAttribute("widgetStatus", widgetStatus);
    } else {
        widgetStatus = String.valueOf(session.getAttribute("widgetStatus"));
    }
    if (session.getAttribute("favlinkStatus") == null) {
        session.setAttribute("favlinkStatus", favlinkStatus);
    } else {
        favlinkStatus = String.valueOf(session.getAttribute("favlinkStatus"));
    }
    if (session.getAttribute("Mapstatus") == null) {
        session.setAttribute("Mapstatus", Mapstatus);
    } else {
        Mapstatus = String.valueOf(session.getAttribute("Mapstatus"));
    }
    if (session.getAttribute("TableStatus") == null) {
        session.setAttribute("TableStatus", TableStatus);
    } else {
        TableStatus = String.valueOf(session.getAttribute("TableStatus"));
    }
    if (session.getAttribute("SnapshotStatus") == null) {
        session.setAttribute("SnapshotStatus", SnapshotStatus);
    } else {
        TableStatus = String.valueOf(session.getAttribute("SnapshotStatus"));
    }
    if (session.getAttribute("manageStickyStatus") == null) {
        session.setAttribute("manageStickyStatus", manageStickyStatus);
    } else {
        manageStickyStatus = String.valueOf(session.getAttribute("manageStickyStatus"));
    }
    if (session.getAttribute("theme") == null) {
        session.setAttribute("theme", themeColor);
    } else {
        themeColor = String.valueOf(session.getAttribute("theme"));
    }
    if (request.getAttribute("dashboardurl") != null) {
        dashboardurl = request.getAttribute("dashboardurl").toString();
    }
    session.setAttribute("ViewFrom", ViewFrom);
    String ParamSectionDisplay = "";
    String displayFiltersGlobal = "";
    String currentURL = "";
    HashMap map = new HashMap();
    String reportName = "";
    String reportDesc = "";
    String bizRole = "";
    String startFlag = request.getParameter("startFlag");
    String templId = request.getParameter("divId");
    HashMap paramMap = new HashMap();
    HashMap paramTime = new HashMap();
    HashMap GraphHashMap = new HashMap();
    String parameters = "";
    String allParameters = "";
    String parametersTime = "";
    String timeLevelStr = "";
    String minTimeLevel = "";
    String userFolders = "";
    ArrayList timeArray = new ArrayList();
    ArrayList params = new ArrayList();
    boolean showExtraTabs = false;
    String mapStatus = Mapstatus;
    String viewSelectValue = "";
    String sortValue = "";
    String dimensionValue = "";
    String ctxPath = request.getContextPath();
    if (request.getSession(false) != null && request.getSession(false).getAttribute("PROGENTABLES") != null) {
        if (request.getAttribute("currentURL") != null) {
            currentURL = String.valueOf(request.getAttribute("currentURL"));
        }
        if (request.getSession(false).getAttribute("PROGENTABLES") != null) {
            map = (HashMap) request.getSession(false).getAttribute("PROGENTABLES");
        }
        if (map.get(PbReportId) != null) {
            container = (prg.db.Container) map.get(PbReportId);
        } else {
            container = new prg.db.Container();
        }
        ArrayList<Segment> segmentList = container.getDimensionSegment();
        boolean segmentflag = false;
        if (segmentList.isEmpty()) {
            segmentflag = true;
        } else {
            segmentflag = false;
        }
        GraphHashMap = (HashMap) container.getGraphHashMap();
        ArrayList alist = container.getReportCollect().timeDetailsArray;
        if (String.valueOf(session.getAttribute(PbReportId + "_" + "sortType")) != null) {
            sortValue = String.valueOf(session.getAttribute(PbReportId + "_" + "sortType"));
        }
        if (String.valueOf(session.getAttribute(PbReportId + "_" + "mapView")) != null) {
            viewSelectValue = String.valueOf(session.getAttribute(PbReportId + "_" + "mapView"));
        }
        if (String.valueOf(session.getAttribute(PbReportId + "_" + "DimensionSelect")) != null) {
            dimensionValue = String.valueOf(session.getAttribute(PbReportId + "_" + "DimensionSelect"));
        }
        if (container.getReportName() != null) {
            reportName = container.getReportName();
        }
        if (container.getReportDesc() != null) {
            reportDesc = container.getReportDesc();
        }
        if (container.getReportCollect().reportBizRoles != null && container.getReportCollect().reportBizRoles.length > 0) {
            bizRole = container.getReportCollect().reportBizRoles[0];
        }
        if (container.getParamSectionDisplay() != null) {
            ParamSectionDisplay = String.valueOf(container.getParamSectionDisplay());
        }
        PbReportViewerBD pb = new PbReportViewerBD();
        displayFiltersGlobal = pb.displayFiltersGlobal(container.getReportCollect(), PbReportId);
        ArrayList<String> geographyDimensions = (ArrayList) container.getGeographyDimensionIds();
        ArrayList<String> geographyDimensionLabels = new ArrayList<String>();
        for (int i = 0; i < geographyDimensions.size(); i++) {
            geographyDimensionLabels.add(container.getReportCollect().getParameterDispName(geographyDimensions.get(i)));
        }
        DashboardTemplateDAO dbrdDAO = new DashboardTemplateDAO();
        PbReportCollection repCollect = new PbReportCollection();
        ReportTemplateDAO repDAO = new ReportTemplateDAO();
        userFolders = repDAO.getReportUserFolders(PbReportId);
        if (!(userFolders.equalsIgnoreCase("")) && !(userFolders.equalsIgnoreCase("null")) && userFolders != null) {
            minTimeLevel = dbrdDAO.getUserFolderMinTimeLevel(userFolders);
            if (minTimeLevel.equalsIgnoreCase("5")) {
                timeLevelStr = "Day";
            } else if (minTimeLevel.equalsIgnoreCase("4")) {
                timeLevelStr = "Week";
            } else if (minTimeLevel.equalsIgnoreCase("3")) {
                timeLevelStr = "Month";
            } else if (minTimeLevel.equalsIgnoreCase("2")) {
                timeLevelStr = "Quarter";
            } else if (minTimeLevel.equalsIgnoreCase("1")) {
                timeLevelStr = "Year";
            }
            request.setAttribute("timelevel", timeLevelStr);
            paramMap = container.getParametersHashMap();
            if (paramMap != null && paramMap.size() > 0) {
                if (paramMap.get("Parameters") != null) {
                    params = (ArrayList) paramMap.get("Parameters");
                }
                if (params == null) {
                    params = new ArrayList();
                }
                if ((!params.isEmpty()) && params.get(0) != null) {
                    parameters = params.get(0).toString();
                }
                for (int i = 0; i < params.size(); i++) {
                    allParameters = allParameters + "," + params.get(i);
                }
                if (allParameters.length() > 0) {
                    allParameters = allParameters.substring(1);
                }
                paramTime = container.getParametersHashMap();
                timeArray = (ArrayList) paramTime.get("TimeDetailstList");
                HashMap timeDetsMap = (HashMap) paramTime.get("TimeDimHashMap");
            }
        }
        PbBaseDAO message = new PbBaseDAO();
        int USERID = 0;
        String ddformT = null;
        if (session.getAttribute("dateFormat") != null) {
            ddformT = session.getAttribute("dateFormat").toString();
        }

        if (session.getAttribute("USERID") == null || String.valueOf(screenMap.get("Redirect")).equalsIgnoreCase("Yes")) {
            response.sendRedirect(request.getContextPath() + "/newpbLogin.jsp");
        } else {
            USERID = Integer.parseInt(String.valueOf(session.getAttribute("USERID")));


			String datetoggl=container.datetoggl;
	//out.println("datetoggl........"+datetoggl);
%>
<html>
    <head>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/jQuery/jquery.mb.containerPlus.2.5.1/css/mbContainer.css" title="style"  media="screen"/>
        <link rel="stylesheet" href="<%=request.getContextPath()%>/stylesheets/treeviewstyle/jquery.treeview.css" />
        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/themes/<%=themeColor%>/jquery-ui-1.7.3.custom.css" rel="stylesheet" />
        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/themes/<%=themeColor%>/pbReportViewerCSS.css" rel="stylesheet" />
        <!--<link type="text/css" href="<%=request.getContextPath()%>/styleshee //ts/themes/<%=themeColor%>/css.css" rel="stylesheet" />-->
        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/themes/<%=themeColor%>/ReportCss.css" rel="stylesheet"/>
        <link type="text/css" href="<%=request.getContextPath()%>/jQuery/jquery/themes/base/BreadCrumb.css" rel="stylesheet" />
        <link href="<%=request.getContextPath()%>/stylesheets/jquery.contextMenu.css" rel="stylesheet" type="text/css" />
        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/themes/<%=themeColor%>/TableCss.css" rel="stylesheet" />
        
        
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/lib/jquery/js/jquery-1.7.2.min.js"></script>

<!--    <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>-->

        <script type="text/javascript" src="<%= request.getContextPath()%>/JS/jquery-ui.min.js"></script>
        
    <script type="text/javascript" src="<%= request.getContextPath()%>/JS/reportgbl/jquery.ui.dialog.js"></script>

        
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/pbReportViewerJS.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/TableDisplay/JS/whatIfScenario.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/JS/stickyNote.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/reportviewer/ReportViewer.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/JS/defineDialog.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/jQuery/d3/customtooltip.js"></script>
        <link type="text/css" href="<%=request.getContextPath()%>/css/d3/tooltip.css" rel="stylesheet"/>
        <script type="text/javascript" src="<%=request.getContextPath()%>/jQuery/d3/d3.v3.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/jQuery/d3/d3.layout.cloud.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/jQuery/d3/chartTypes.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/jQuery/d3/chartTypesGroup.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/jQuery/d3/chartTypeOthers.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/jQuery/d3/chartTypeBars.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/jQuery/d3/chartTypeCircular.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/jQuery/d3/chartTypeLine.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/jQuery/d3/chartTypeDashboard.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/jQuery/jquery/ui/jquery.jBreadCrumb.1.1.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/jQuery/jquery.mb.containerPlus.2.5.1/inc/jquery.metadata.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/jQuery/jquery.mb.containerPlus.2.5.1/inc/mbContainer.js"></script>
<!--        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/ui.slider.js"></script>-->
        <script type="text/javascript" src="<%= request.getContextPath()%>//dragAndDropTable.js"></script>

        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/quicksearch.js"></script>
        <script src="<%=request.getContextPath()%>/javascript/treeview/jquery.treeview.js" type="text/javascript"></script>
        <script src="<%=request.getContextPath()%>/javascript/treeview/jquery.cookie.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/JS/shrink.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/css/d3/bootstrap.min.js"></script>
        <% if (PrivilegeManager.isModuleEnabledForUser("MAP", USERID) && container.isMapEnabled()) {%>
        <%}%>
        <link type="text/css" href="<%=request.getContextPath()%>/datedesign.css" rel="stylesheet"/>
        <link type="text/css" href="<%=request.getContextPath()%>/css/d3/jquery.dataTables.css" rel="stylesheet"/>
        <script type="text/javascript">
            var bussTabId="";
            var grp_id="";
            var buss_col_id="";
            var buss_col_name="";
            var buss_table_name="";
            var fromGO="false";
            var folder_id="";
            var user_col_id="";
            var repPrevStateCnt=0;
            var tempBussColName=null;
            var isKPIDashboard1="";
            var fromReport="true";
            var filterMapNew={};
            var filterMapNotin={};
            var filterMapNewtb={};
            var filterMapgraphs={};
            var isfilteraplied=false;
            var reportId = '<%=PbReportId%>'
            var bucketDetails="";
            var folderId="";
            var dimId = new Array();
            var dimLabel = new Array();
            var value=new Array();
            $(function(){
                if('<%=viewSelectValue%>'!='null'){
                    $('#sortValuesForMap').val('<%=sortValue%>');
                    $('#ViewSelect').val('<%=viewSelectValue%>');
                    $('#GeoViewForMap').val('<%=dimensionValue%>');
                }
                if('<%=ddformT%>'!='null'){
                    $('#datepicker').datepicker()
                    $( "#datepicker" ).datepicker( "option", "dateFormat", '<%=ddformT%>');
                }
                var MapStatus = '<%=Mapstatus%>';
                if(MapStatus=="block"){
                    showMap();
                }
                $("#sequnceParamsDiv").dialog({
                    autoOpen: false,
                    height: 400,
                    width: 300,
                    position: 'justify',
                    modal: true,
                    resizable:true
                });
                $("#advanceFormulaDiv").dialog({

                    autoOpen: false,
                    height: 480,
                    width: 545,
                    modal: true
                });
                $("#advanceFormulaTypeDiv").dialog({

                    autoOpen: false,
                    position: 'justify',
                    height: 200,
                    width: 300,
                    modal: true
                });
                $("#QuickTimeBasedFormulaDiv").dialog({

                    autoOpen: false,
                    height: 550,
                    width: 750,
                    position: 'top',
                    modal: true

                });
                $("#createBucketdiv").dialog({

                    autoOpen: false,
                    height: 500,
                    width: 610,
                    position: 'top',
                    modal: true
                });
                $("#ScheduleReport").dialog({
                    autoOpen: false,
                    height: 460,
                    width: 500,
                    position: 'justify',
                    modal: true
                });
                $("#viewFactformulaDiv").dialog({
                    autoOpen: false,
                    height: 350,
                    width: 800,
                    position: 'justify',
                    modal: true
                });
                //                $("#sequnceParamsDiv").dialog({
                //                     autoOpen: false,
                //                     height: 400,
                //                     width: 300,
                //                     position: 'justify',
                //                     modal: true,
                //                     resizable:true
                //                });
                $("#removeMoreParamsDiv").dialog({
                    autoOpen: false,
                    height: 400,
                    width: 300,
                    position: 'justify',
                    modal: true,
                    resizable:true
                });
                $("#AddInnerViewbysDiv").dialog({
                    autoOpen: false,
                    height: 250,
                    width: 300,
                    position: 'justify',
                    modal: true
                });
                $("#modifyMeasureAttrDiv").dialog({
                    autoOpen: false,
                    height: 310,
                    width: 380,
                    position: 'justify',
                    modal: true,
                    resizable:true
                });
                $("#AddMoreParamsDiv").dialog({
                    autoOpen: false,
                    height: 550,
                    width: 550,
                    position: 'justify',
                    modal: true,
                    resizable:true
                });
                $("#reportDrillDiv").dialog({
                    autoOpen: false,
                    height: 250,
                    width: 350,
                    position: 'justify',
                    modal: true,
                    resizable:true
                });
                $("#moreCompanyDiv").dialog({
                        autoOpen: false,
                        height: 400,
                        width: 300,
                        position: 'justify',
                        modal: true
                    });

                $(document).ready(function() {
            <%
       long current_time = System.currentTimeMillis();
       long st_time = Long.parseLong(session.getAttribute("rep_st_time").toString());
       double total_time = (current_time - st_time) * Math.pow(10, -3);
       double finalValue = Math.round(total_time * 100.0) / 100.0;
            %>
               if($.browser.msie ){  $("#advanceFormulaDiv").css({"display":"none"}); }
               $("#leftParamSection").hide();//2Dec
               $("body").on("click", function(){
                   $("datalist").hide();
               });
               document.getElementById("lastUpdate").innerHTML += '<font color="black">&nbsp;Load Time: <%= finalValue%>s</font>';
               document.getElementById("lastUpdate").style.visibility = "visible";
               if($.browser.msie )   {
                   $('.x').css('width',$(window).width());
               }
               if($.browser.safari)
               {
                   $('.rtable').css('width',$(window).width());
               }
           });      jQuery("#breadCrumb").jBreadCrumb();


           var ctxPath='<%=request.getContextPath()%>';
           if ( $('#warnUser').val() == 'true' )
           {
               $('#warnUser').val('false');
               $('#warnUser').remove();
               alert("A user with the same Username has already logged in");
               var login = confirm("If you want to work on this session, the other user session will be logged off. Press Ok to continue");
               if ( login )
               {
                   $.ajax({
                       type: 'GET',
                       async: false,
                       cache: false,
                       timeout: 30000,
                       url:ctxPath+'/baseAction.do?param=killDuplicateAndLogin',
                       success: function(data){
                       }
                   });
               }
               else
               {
                   $.ajax({
                       type: 'GET',
                       async: false,
                       cache: false,
                       timeout: 30000,
                       url:ctxPath+'/baseAction.do?param=removeDuplicateUser',
                       success: function(data){
                       }
                   });
                   parent.logout();
               }
           }
           $(".containerPlus").buildContainers({
               containment:"document",
               elementsPath:"<%=request.getContextPath()%>/jQuery/jquery.mb.containerPlus.2.5.1/elements/",
               onClose:function(o){},
               onIconize:function(o){},
               effectDuration:200
           });

           if (!$("#demoContainer").mb_getState("closed")){
               $('#close').show()
               $('#open').hide()
           }
           openStickyNotes(ctxPath,'<%=currentURL%>');
       });
       function refreshFavLinks()
       {
           document.getElementById('favFrame').contentWindow.location.reload();
       }

       function goThumbnail(userId,templId){
           parent.document.getElementById(templId).innerHTML='<center><img id="imgId" src="images/ajax.gif" align="middle"  width="75px" height="75px"  style="position:absolute" /></center>';
           parent.document.getElementById("userframe").style.display='none';
           parent.document.getElementById("startPagePriv").style.display='none';
           parent.document.getElementById("fadestart").style.display='none';
           $.ajax({
               type: 'GET',
               async: false,
               cache: false,
               timeout: 30000,
               url:'dashboardTemplateAction.do?templateParam2=getTemplateDivs&userId='+userId+'&templId='+templId,
               success: function(img){
                   parent.document.getElementById(templId).innerHTML=img;
               }
           });
       }
       function goCancel(){
           window.close();
       }
       function reportParamsDrill(repId,userId){
           var path='&'+document.getElementById('reppath').value;
           path=path.replace('&',';');
           window.open('pbParameterDrill.jsp?userId='+userId+'&reportId='+repId+'&path='+path,"Parameter Drill", "scrollbars=1,width=550,height=350,address=no");
       }
       function submiturls1($ch){
           var url = $ch;
           var view=(url).substring($ch.lastIndexOf("&")+1);
           if(view.indexOf("CBOARP") != -1){
               value=($ch).substring($ch.lastIndexOf("=")+1);
               $.post(
               'reportViewer.do?reportBy=drillView&value='+value+'&repId='+<%=PbReportId%>,
               function(data){
               });
           }
           document.frmParameter.action = $ch;
           document.frmParameter.submit();
       }
       function submiturlsinNewTab1($ch){
           var url = $ch;
           var view=(url).substring($ch.lastIndexOf("&")+1);
           if(view.indexOf("CBOARP") != -1){
               value=($ch).substring($ch.lastIndexOf("=")+1);

               $.post(
               'reportViewer.do?reportBy=drillView&value='+value+'&repId='+<%=PbReportId%>+'&isnewtabfirstTime=true',
               function(data){
               });
           }
           document.frmParameter.action = $ch;
           document.frmParameter.target = "_blank";
           document.frmParameter.submit();
           document.frmParameter.target = "";
       }
       function modifyParams(columnId,checkBoxObj,tableObj){
           var trObj=tableObj.getElementsByTagName('tr');
           for(i=0;i<trObj.length;i++){
               var tdObj=trObj[i].getElementsByTagName('td');
               for(j=0;j<tdObj.length;j++){

                   if(tdObj[j].id==columnId){
                       if(checkBoxObj.checked)
                           tdObj[j].style.display='';
                       else
                           tdObj[j].style.display='none';
                   }                   }                }            }
       function paramDisp(modifyColumnsObj){
           if(modifyColumnsObj.style.display=='none'){
               modifyColumnsObj.style.display='';
           }
           else{
               modifyColumnsObj.style.display='none';
           }            }
       function addParamList(addParamsListObj,addParamsObj,reportId,path,tableId){
           if(addParamsListObj.style.display=='none'){
               addParamsListObj.style.display='';                }
           else{
               addParamsListObj.style.display='none';
               addParams(addParamsObj,reportId,path,tableId);                }            }
       function addParams(addParamsObj,reportId,path){
           var paramIds='';
           for(var i=0;i<addParamsObj.length;i++){
               if( addParamsObj[i].checked){
                   paramIds=paramIds+','+addParamsObj[i].value;                    }                }
           if(paramIds!=''){
               document.frmParameter.action='pbAddParams.jsp?reportId='+reportId+'&paramIds='+paramIds+'&path='+path;
               document.frmParameter.submit();                }            }
       function getMessage(id,from,sub,message){
           var  src = "pbTakeMailAddress.jsp?from="+from+"&subject="+sub+"&message="+message+"&sample=sample";
           src=src.replace(" ","~","gi");
           var frameObj = document.getElementById("replyMessageFrame");
           frameObj.src = src;
           $('#replyMessageDialog').data('title.dialog','Reply Messages');
           $('#replyMessageDialog').dialog('open');            }
       function cancelMessage(){
           $('#replyMessageDialog').dialog('close');            }
       function cancelSnap(){
           $('#replyMessageDialog').dialog('close');            }
       function cancelMessage1(){
           $('#composeMessageDialog').dialog('close');                submitform();            }
       function savecancelGrp(){
           $("#dispgrpAnalysis").dialog('close');            }
       function toggleYearVisibility() {
           var e = document.getElementById('toggleYear1');
           var e1 = document.getElementById('toggleYear');
           if(e.style.display == 'block'){
               e.style.display = 'none';
               e1.style.display = 'block'}
           else{
               e.style.display = 'block';
               e1.style.display = 'none';
           }}
       function saveYearDetails(calname){
           if(calname=="Select-Year")
           {alert("Please Choose Correct Year"); }
           else{
               $('#datetext').val('topdate');
               var calyear;var dateArr=new Array();var frmDate;var toDate;var dateArr1=new Array();
               if(calname=="perioddate")
               {
                   calyear = $('#calPeriodYear option:selected').val()

                   calyear="Mon,31,Dec,".concat(calyear)
                   dateArr=calyear.split(",");
                   $('#perioddate').val(calyear);
                   if(calyear!=""){$("#pfield1").html(dateArr[3])}

               }
               if(calname=="fromdate")
               {
                   calyear = $('#calFromYear option:selected').val()
                   calyear="Mon,01,Jan,".concat(calyear)
                   dateArr=calyear.split(",");
                   $('#fromdate').val(calyear);
                   if(calyear!=""){$("#field1").html(dateArr[3])}               }
               if(calname=="todate")                {
                   calyear = $('#calToYear option:selected').val()
                   calyear="Mon,31,Dec,".concat(calyear)
                   dateArr=calyear.split(",");
                   $('#todate').val(calyear);
                   if(new Date($('#todate').val()).getTime()<new Date($('#fromdate').val()).getTime()){
                       alert("Wrong Date Selected");                }
                   if(calyear!=""){$("#tdfield1").html(dateArr[3])}               }
               if(calname=="comparefrom")                {
                   calyear = $('#calCmpFromYear option:selected').val()
                   calyear="Mon,01,Jan,".concat(calyear)
                   dateArr=calyear.split(",");
                   $('#comparefrom').val(calyear);
                   if(calyear!=""){$("#cffield1").html(dateArr[3])}               }
               if(calname=="compareto")
               {
                   calyear = $('#calCmpToYear option:selected').val()
                   calyear="Mon,31,Dec,".concat(calyear)
                   dateArr=calyear.split(",");
                   $('#compareto').val(calyear);
                   if(new Date($('#compareto').val()).getTime()<new Date($('#comparefrom').val()).getTime()){
                       alert("Wrong Date Selected");                }
                   if(calyear!=""){$("#ctfield1").html(dateArr[3])}               }
               if(calname=="fromdatetodate"){
                   calyear = $('#calFromToyear option:selected').val()
                   dateArr1 = calyear.split(" - ");
                   frmDate = dateArr1[0];
                   toDate = dateArr1[1];
                   calyear="Mon,01,Jan,".concat(frmDate)
                   dateArr=calyear.split(",");
                   $('#fromdate').val(calyear);
                   if(calyear!=""){$("#field1").html(dateArr[3])}
                   calyear="Mon,31,Dec,".concat(toDate)
                   dateArr=calyear.split(",");
                   $('#todate').val(calyear);
                   if(new Date($('#todate').val()).getTime()<new Date($('#fromdate').val()).getTime()){
                       alert("Wrong Date Selected");                }
                   if(calyear!=""){$("#tdfield1").html(dateArr[3])}               }
               if(calname=="comparefromcompareto")                {
                   calyear = $('#calCmpFromToYear option:selected').val()
                   dateArr1 = calyear.split(" - ");
                   frmDate = dateArr1[0];
                   toDate = dateArr1[1];
                   calyear="Mon,01,Jan,".concat(frmDate)
                   dateArr=calyear.split(",");
                   $('#comparefrom').val(calyear);
                   if(calyear!=""){$("#cffield1").html(dateArr[3])}

                   calyear="Mon,31,Dec,".concat(toDate)
                   dateArr=calyear.split(",");
                   $('#compareto').val(calyear);
                   if(new Date($('#compareto').val()).getTime()<new Date($('#comparefrom').val()).getTime()){
                       alert("Wrong Date Selected");
                   }

               }
           }
       }
       function cancelFavLinks(){
           $('#favLinksDialog').dialog('close');
           setTimeout('reloadTimerfavlinks()',1000);
       }
       function reloadTimerfavlinks(){
           document.getElementById('favFrame').src = document.getElementById('favFrame').src;
       }
       function cancelPerLinks(){
           $('#snapShotDialog').dialog('close');
           alert("snapshot saved successfully")
           setTimeout('reloadTimer()',1000);
       }
       function reloadTimer(){
           document.getElementById('sanpFrame').src = document.getElementById('sanpFrame').src;
       }
       function showPersonalisedReports(){
           var frameObj = document.getElementById("replyMessageFrame");
           frameObj.src = "showallSanps.jsp";
           $('#replyMessageDialog').data('title.dialog','Snapshots');
           $('#replyMessageDialog').dialog('open');
       }
       function showAllMessages(){
           var frameObj = document.getElementById("replyMessageFrame");
           frameObj.src = "showAllMessages.jsp";
           $('#replyMessageDialog').data('title.dialog','Messages');
           $('#replyMessageDialog').dialog('open');
       }
       function showAllSchedulers(){
           var frameObj = document.getElementById("replyMessageFrame");
           frameObj.src = "showAllSchedulers.jsp";
           $('#replyMessageDialog').data('title.dialog','Schedulers');
           $('#replyMessageDialog').dialog('open');
       }
       function displayWidgets(){
           if(document.getElementById("Widgets").style.display=="none"){
               document.getElementById("Widgets").style.display = "block";
               var frameObj = document.getElementById("widgetframe");
               var source = 'divPersistent.jsp?method=forwidgets&block=yes';
               frameObj.src = source;
           }else{
               document.getElementById("Widgets").style.display = "none";
               var frameObj = document.getElementById("widgetframe");
               var source = 'divPersistent.jsp?method=forwidgets&block=no';
               frameObj.src = source;
           }
       }
       function dispParameters(){
           if(document.getElementById("tabParameters").style.display=="none"){
               document.getElementById("tabParameters").style.display = "block";
               var frameObj = document.getElementById("widgetframe");
               var source = 'divPersistent.jsp?method=forParameters&block=yes';
               frameObj.src = source;
               $.post(
               'reportViewer.do?reportBy=hideLeftTd&repId='+<%=PbReportId%>+'&block=yes'+'&tdType=paramtd',
               function(data){

               });
           }else{
               document.getElementById("tabParameters").style.display = "none";
               var frameObj = document.getElementById("widgetframe");
               var source = 'divPersistent.jsp?method=forParameters&block=no';
               frameObj.src = source;
               $.post(
               'reportViewer.do?reportBy=hideLeftTd&repId='+<%=PbReportId%>+'&block=no'+'&tdType=paramtd',
               function(data){

               });
           }
       }
       function dispGraphsReport(){
           if(document.getElementById("tabGraphs") != null && document.getElementById("tabGraphs").style.display=="none"){
               document.getElementById("tabGraphs").style.display = "block";
               var source='<%=request.getContextPath() + "/TableDisplay/pbGraphDisplayRegion.jsp"%>'+"?tabId="+REPORTID.value;
               var gSrc = document.getElementById("iframe4");
               gSrc.src = source;
               var frameObj = document.getElementById("widgetframe");
               var source = 'divPersistent.jsp?method=forGraph&block=no';
               frameObj.src = source;
               var frm1 = document.getElementById("iframe1");
               frm1.style.height = '370';
               frm1.contentWindow.document.getElementById("myTableBody").className = "myTableBodyClass";
           }else if(document.getElementById("tabGraphs") != null){
               document.getElementById("tabGraphs").style.display = "none";
               var frameObj = document.getElementById("widgetframe");
               var source = 'divPersistent.jsp?method=forGraph&block=yes';
               frameObj.src = source;
               var frm1 = document.getElementById("iframe1");
               var trobj =  frm1.contentWindow.document.getElementById("myTableBody").getElementsByTagName("tr")
               if(!(trobj.length<=parseInt(10))){
                   frm1.contentWindow.document.getElementById("myTableBody").className = "myTableBodyClassOnlyTable";
                   frm1.style.height = '725';
               }
           }
           if(document.getElementById("tabGraphs1").style.display=="none"){
               document.getElementById("tabGraphs1").style.display = "block";
               var source='<%=request.getContextPath() + "/TableDisplay/pbGraphDisplayRegion.jsp"%>'+"?tabId="+REPORTID.value;
               var gSrc = document.getElementById("iframe4");
               gSrc.src = source;
               var frameObj = document.getElementById("widgetframe");
               var source = 'divPersistent.jsp?method=forGraph&block=no';
               frameObj.src = source;
               var frm1 = document.getElementById("iframe1");
               frm1.style.height = '370';
               frm1.contentWindow.document.getElementById("myTableBody").className = "myTableBodyClass";
           }else{
               var frameObj = document.getElementById("widgetframe");
               var source = 'divPersistent.jsp?method=forGraph&block=yes';
               frameObj.src = source;
               var frm1 = document.getElementById("iframe5");
               var trobj =  frm1.contentWindow.document.getElementById("myTableBody").getElementsByTagName("tr")
               if(!(trobj.length<=parseInt(10))){
                   frm1.contentWindow.document.getElementById("myTableBody").className = "myTableBodyClassOnlyTable";
                   frm1.style.height = '725';
               }
           }
       }

       function displayMap(){
           var sortType = $("#sortValuesForMap").val();
           var mapView = $("#ViewSelect").val();
           var geoView = $("#GeoViewForMap").val();
           if(document.getElementById("tabmap").style.display=="none")
           {
               var REPORTID = document.getElementById("REPORTID").value;

               var ctxPath=document.getElementById("h").value;

               $.ajax({
                   type: 'GET',
                   async: false,
                   cache: false,
                   timeout: 30000,
                   url:ctxPath+'/mapAction.do?reportBy=isMapEnabled&REPORTID='+REPORTID+'&reportType=R&sortType='+sortType+'&mapView='+mapView+'&geoView='+geoView,
                   success : function(data){
                       if (data != null && data != ""){
                           if(data=="dimensionerror"){

                               document.getElementById("tabmap").style.display = "none";
                               var frameObj = document.getElementById("widgetframe");
                               var source = 'divPersistent.jsp?method=forMap&block=no';
                               frameObj.src = source;
                               alert("Map is not applicable for this view");

                           }
                           else if(data=="error"){
                               document.getElementById("tabmap").style.display = "none";
                               var frameObj = document.getElementById("widgetframe");
                               var source = 'divPersistent.jsp?method=forMap&block=no';
                               frameObj.src = source;
                               alert("You have not configured Geography Dimension in Setup. Please configure the same for viewing Maps");
                           }
                           else{
                               document.getElementById("tabmap").style.display = "block";
                               var frameObj = document.getElementById("widgetframe");
                               var source = 'divPersistent.jsp?method=forMap&block=yes';
                               frameObj.src = source;
                               var frm1 = document.getElementById("map_canvas");
                               frm1.style.height = '370';
                               dispMap(data,"map_canvas","R");
                           }

                       }
                   }

               });
           }else{
               document.getElementById("tabmap").style.display = "none";
               var frameObj = document.getElementById("widgetframe");
               var source = 'divPersistent.jsp?method=forMap&block=no';
               frameObj.src = source;
           }
       }

       function addSortValueToMap(){
           showMap();
       }

       function showMap(){
           var sortType = $("#sortValuesForMap").val();
           var mapView = $("#ViewSelect").val();
           var geoView = $("#GeoViewForMap").val();
           var REPORTID = document.getElementById("REPORTID").value;
           var ctxPath=document.getElementById("h").value;

           $.ajax({
               type: 'GET',
               async: false,
               cache: false,
               timeout: 30000,
               url:ctxPath+'/mapAction.do?reportBy=isMapEnabled&REPORTID='+REPORTID+'&reportType=R&sortType='+sortType+'&mapView='+mapView+'&geoView='+geoView,
               success : function(data){
                   if (data != null && data != ""){
                       if(data=="dimensionerror"){
                           document.getElementById("tabmap").style.display = "none";
                           var frameObj = document.getElementById("widgetframe");
                           var source = 'divPersistent.jsp?method=forMap&block=no';
                           frameObj.src = source;
                           alert("Map is not applicable for this view");
                       }
                       else if(data=="error"){
                           document.getElementById("tabmap").style.display = "none";
                           var frameObj = document.getElementById("widgetframe");
                           var source = 'divPersistent.jsp?method=forMap&block=no';
                           frameObj.src = source;
                           alert("You have not configured Geography Dimension in Setup. Please configure the same for viewing Maps");
                       }
                       else{
                           var frameObj = document.getElementById("widgetframe");
                           var source = 'divPersistent.jsp?method=forMap&block=yes';
                           frameObj.src = source;
                           var frm1 = document.getElementById("map_canvas");
                           frm1.style.height = '370';
                           dispMap(data,"map_canvas","R");
                       }
                   }
               }

           });
       }

       function hideTd(){
           if(document.getElementById("leftTd").style.display=="none"){
               document.getElementById("leftTd").style.display = "";
               var frameObj = document.getElementById("widgetframe");
               var source = 'divPersistent.jsp?method=forleftTd&block=no';
               frameObj.src = source;
               ifrmesizedynamic();
               document.getElementById("tdImage").src = "<%=request.getContextPath()%>/icons pinvoke/control-180.png";
               $.post(
               'reportViewer.do?reportBy=hideLeftTd&repId='+<%=PbReportId%>+'&block=no'+'&tdType=lefttd',
               function(data){

               });
           }else{
               document.getElementById("leftTd").style.display = "none";
               var frameObj = document.getElementById("widgetframe");
               var source = 'divPersistent.jsp?method=forleftTd&block=yes';
               frameObj.src = source;
               ifrmesizedynamicResize();
               document.getElementById("tdImage").src = "<%=request.getContextPath()%>/icons pinvoke/control.png";
               $.post(
               'reportViewer.do?reportBy=hideLeftTd&repId='+<%=PbReportId%>+'&block=yes'+'&tdType=lefttd',
               function(data){

               });
           }
       }

       function displayfavlink(){

           if(document.getElementById("favlinkcont").style.display=="none"){
               document.getElementById("favlinkcont").style.display = "block";
               var frameObj = document.getElementById("widgetframe");
               var source = 'divPersistent.jsp?method=forfavlinkcont&block=yes';

               frameObj.src = source;
           }else{
               document.getElementById("favlinkcont").style.display = "none";
               var frameObj = document.getElementById("widgetframe");
               var source = 'divPersistent.jsp?method=forfavlinkcont&block=no';
               frameObj.src = source;
           }

       }
       function displayeditink(){
           if(document.getElementById("editActionsTab").style.display=="none"){
               document.getElementById("editActionsTab").style.display=""
           }else{
               document.getElementById("editActionsTab").style.display="none"
           }
       }

       function displayactionlink(){
           if(document.getElementById("ActionTab").style.display=="none"){
               document.getElementById("ActionTab").style.display=""
           }else{
               document.getElementById("ActionTab").style.display="none"
           }
       }

       function displaynewactionlink(){

           if(document.getElementById("actionidnew").style.display=="none"){
               document.getElementById("actionidnew").style.display=""
           }else{
               document.getElementById("actionidnew").style.display="none";
           }
       }

       function displayStickList(){
           $("#stickListDiv").toggle(500);
       }
       function dispTables(){
           if(document.getElementById("tabTable").style.display=="none"){
               document.getElementById("tabTable").style.display = "block";
               var frameObj = document.getElementById("widgetframe");
               var source = 'divPersistent.jsp?method=fortabTable&block=yes';
               frameObj.src = source;
           }else{
               document.getElementById("tabTable").style.display = "none";
               var frameObj = document.getElementById("widgetframe");
               var source = 'divPersistent.jsp?method=fortabTable&block=no';
               frameObj.src = source;
           }
       }
       function dispText(){
           if(document.getElementById("tabDiv").style.display=="none"){
               document.getElementById("tabDiv").style.display = "block";
               var frameObj = document.getElementById("widgetframe");
               var source = 'divPersistent.jsp?method=forText&block=yes';
               frameObj.src = source;
           }else{
               document.getElementById("tabDiv").style.display = "none";
               var frameObj = document.getElementById("widgetframe");
               var source = 'divPersistent.jsp?method=forText&block=no';
               frameObj.src = source;
           }
       }
       function dispMessages(){
           if(document.getElementById("messages").style.display=="none"){
               document.getElementById("messages").style.display = "block";
               var frameObj = document.getElementById("widgetframe");
               var source = 'divPersistent.jsp?method=formessages&block=yes';
               frameObj.src = source;
           }else{
               document.getElementById("messages").style.display = "none";
               var frameObj = document.getElementById("widgetframe");
               var source = 'divPersistent.jsp?method=formessages&block=no';
               frameObj.src = source;
           }
       }
       function dispSnapShots(){

           if(document.getElementById("snapshots").style.display=="none"){
               document.getElementById("snapshots").style.display = "block";
               var frameObj = document.getElementById("widgetframe");
               var source = 'divPersistent.jsp?method=forsnapshots&block=yes';
               frameObj.src = source;
           }else{
               document.getElementById("snapshots").style.display = "none";
               var frameObj = document.getElementById("widgetframe");
               var source = 'divPersistent.jsp?method=forsnapshots&block=no';
               frameObj.src = source;
           }

       }

       function dispComparedReports(){
           if(document.getElementById("compareReports").style.display=="block")
               document.getElementById("compareReports").style.display = "none";
           else
               document.getElementById("compareReports").style.display = "block";

       }
       function displayTopbottom(){

           if(document.getElementById("topBot").style.display=="none"){
               document.getElementById("topBot").style.display = "block";
               var frameObj = document.getElementById("widgetframe");
               var source = 'divPersistent.jsp?method=fortopBot&block=yes';
               frameObj.src = source;
           }else{
               document.getElementById("topBot").style.display = "none";
               var frameObj = document.getElementById("widgetframe");
               var source = 'divPersistent.jsp?method=fortopBot&block=no';
               frameObj.src = source;
           }

       }
       function displaySchedulers(){
           if(document.getElementById("scheds").style.display=="none"){
               document.getElementById("scheds").style.display = "block";
           }else{
               document.getElementById("scheds").style.display = "none";
           }
       }
       function displayManageSticky(){

           if(document.getElementById("manageSticky").style.display=="none"){
               document.getElementById("manageSticky").style.display = "block";
               var frameObj = document.getElementById("widgetframe");
               var source = 'divPersistent.jsp?method=forManageSticky&block=yes';
               frameObj.src = source;
           }else{
               document.getElementById("manageSticky").style.display = "none";
               var frameObj = document.getElementById("widgetframe");
               var source = 'divPersistent.jsp?method=forManageSticky&block=no';
               frameObj.src = source;
           }

       }
       function gotoScheduler(){
           var reportId=document.getElementById("REPORTID").value;
           var bizRole="<%= bizRole%>";
           document.forms.frmParameter.action="tracker/JSPS/scheduleReport.jsp?schedulerId=&targetReportId="+reportId+"&targetBizRole="+bizRole;
           document.forms.frmParameter.submit();
       }
       function gotoTracker(){
           alert("redirect to Tracker page");
       }
       function displayBusRoles(){

           if(document.getElementById("BusRolesTab").style.display=="none"){
               document.getElementById("BusRolesTab").style.display = "block";
               var frameObj = document.getElementById("widgetframe");
               var source = 'divPersistent.jsp?method=forBusRoles&block=yes';
               frameObj.src = source;
           }else{
               document.getElementById("BusRolesTab").style.display = "none";
               var frameObj = document.getElementById("widgetframe");
               var source = 'divPersistent.jsp?method=forBusRoles&block=no';
               frameObj.src = source;
           }

       }
       function viewReport(path){
           document.forms.frmParameter.action=path;
           document.forms.frmParameter.submit();
       }
       function createCustomizeReport(){
           document.getElementById('custreportdiv').style.display='block';
           document.getElementById('fade').style.display='block';
           document.getElementById('mainBody').style.overflow='hidden';
           document.getElementById('reportDesc').value = "";
           document.getElementById('reportName').value="";
       }
       function cancelCustomizeReport(){
           document.getElementById('duplicate').innerHTML = '';
           document.getElementById('save').disabled = false;
           document.getElementById('custreportdiv').style.display='none';
           document.getElementById('fade').style.display='none';
           document.getElementById('mainBody').style.overflow='auto';
       }
       function tabmsg(){
           document.getElementById('reportDesc').value = document.getElementById('reportName').value;
       }
       function tabmsg1(){
           document.getElementById('reportDesc1').value = document.getElementById('reportName1').value;
       }
       function saveCustomizeReport(repId){
           var reportName = document.getElementById('reportName').value;
           var reportDesc = document.getElementById('reportDesc').value;
           var roleid=document.getElementById('roleid').value;
           if(reportName==''){
               alert("Please enter Report Name");
           }
           else  if(reportDesc==''){
               alert("Please enter Report Description")
           }
           else{
               $.ajax({
                   type: 'GET',
                   async: false,
                   cache: false,
                   timeout: 30000,
                   url: 'reportTemplateAction.do?templateParam=checkReportName&reportName='+reportName+"&roleid="+roleid,
                   success: function(data){
                       if(data!=""){
                           document.getElementById('duplicate').innerHTML = data;
                           document.getElementById('save').disabled = true;
                       }
                       else if(data=='')
                       {
                           document.forms.frmParameter.action = "reportViewer.do?reportBy=customizeReport&custRepName="+reportName+"&custRepDesc="+reportDesc+"&REPORTID="+repId;
                           document.forms.frmParameter.method="POST";
                           document.forms.frmParameter.submit();
                       }
                   }
               });
           }
       }

       function defineScheduler(repId,repName)
       {
           document.forms.frmParameter.action =  "<%=request.getContextPath()%>/scheduler.do?reportBy=getReportParameters&reportId="+repId+"&reportName="+repName+"&from="+"scheduler";
           document.forms.frmParameter.method="POST";
           document.forms.frmParameter.submit();
       }


       function defineTracker(repId,repName)
       {
           document.forms.frmParameter.action =  "<%=request.getContextPath()%>/scheduler.do?reportBy=getReportParameters&reportId="+repId+"&reportName="+repName+"&from="+"tracker";
           document.forms.frmParameter.method="POST";
           document.forms.frmParameter.submit();
       }

       function cancelTracker()
       {   $('#TrackerSchedulerDialog').dialog('close');
           document.getElementById("tracker").style.display='none';
           document.getElementById('fade').style.display='none';
           document.getElementById('mainBody').style.overflow='auto';
       }

       function updateMsgText(){
           var reportId=document.forms.frmParameter.REPORTID.value;
           var msgText=document.forms.frmParameter.rprtText.value;
           $.ajax({
               type: 'GET',
               async: false,
               cache: false,
               timeout: 30000,
               url: 'baseAction.do?param=reportMessages&reportId='+reportId+'&msgText='+msgText,
               success: function(data){
               }
           });
       }
       var rlsdiv = new Array();
       function roleCancel(){
           var rlid = rlsdiv[0];
           document.getElementById('rep-'+rlid).innerHTML= "";
           document.getElementById('rep-'+rlid).style.display="none";
           rlsdiv.shift();
       }
       function popDiv(){
           $("#busRoleDiv").dialog('open');
       }
       function displayreportCustom()
       {

           var frameObj = document.getElementById("customReportDrillFrame");
           var reportId=document.forms.frmParameter.REPORTID.value;
           var folderId='<%=rolesid%>'
           frameObj.src= "<%=request.getContextPath()%>/PbEditReportDrill.jsp?reportId="+reportId+"&folderId="+folderId;
           $("#customReportDrill").dialog('open');

       }
       function rolesajax(path,roleid){
           if(rlsdiv[0]!=null){
               roleCancel();
           }
           if(rlsdiv[0]==null){
               rlsdiv[0] = roleid;
           }
           $.ajax({
               type: 'GET',
               async: false,
               cache: false,
               timeout: 30000,
               url: path,
               success: function(path){
                   if(path!=""){
                       document.getElementById('rep-'+roleid).innerHTML = path;
                       document.getElementById('rep-'+roleid).style.display="block";
                   }
                   else{
                       document.getElementById('rep-'+roleid).innerHTML = "none";
                   }
               }
           });
       }
       function viewDashboardG(path){
           document.forms.frmParameter.action=path;
           document.forms.frmParameter.submit();
       }
       function viewReportG(path){
           document.forms.frmParameter.action=path;
           document.forms.frmParameter.submit();
       }
       function viewReportDrill(path){
           document.forms.frmParameter.action=path;
           document.forms.frmParameter.submit();
       }
       function goPaths(path){
           parent.closeStart();
           document.forms.frmParameter.action=path;
           document.forms.frmParameter.submit();
       }
       function gotoDBCON(ctxPath){
           if(!<%=isQDEnableforUser%>){
               alert("You do not have the sufficient previlages")
           }else{
               document.forms.frmParameter.action=ctxPath+"/pbBase.jsp?pagename=Query Studio&curntpage=Database Connection#Database_Connection";
               document.forms.frmParameter.submit();
           }
       }
       function formatStr(EL,maxchars){
           strbuff=EL.innerHTML;
           newstr='';
           startI = 0;
           max=maxchars;
           str='';
           subarr=new Array(parseInt(strbuff.length/max+1));
           for (i=0;i<subarr.length;i++)
           {
               subarr[i]=strbuff.substr(startI,max);
               startI+=max;
           }
           for (i=0;i<subarr.length-1;i++)
           {
               newstr+=subarr[i]+'<br/>';
           }
           newstr+=subarr[subarr.length-1];
           if(subarr.length==1){
               EL.innerHTML=EL.innerHTML;
           }else{
               EL.innerHTML=newstr;
           }
       }
       function grpByAnalysis(reportId,divmenuId){
           if(divmenuId.style.display=='none'){
               divmenuId.style.display='' ;
           }else{
               divmenuId.style.display='none' ;
           }
       }
       function oldapplycolor(columnName,disColumnName){
           frameObj = document.getElementById("applycolorframe");
           var reportId=document.forms.frmParameter.REPORTID.value;
           frameObj.src= "<%=request.getContextPath()%>/TableDisplay/PbApplyColors.jsp?colmnname="+columnName+"&dispcolmname="+disColumnName+"&reportid="+reportId;
           $("#applycolrdiv").dialog('open');
       }
       function openSlidreRange(){
           $("#openSlidreRangeDiv").dialog('open')
       }
       function closeapplycolr(){
           $("#applycolrdiv").dialog('close');
       }
       function oldcolumnProperties(columnName,disColumnName,obj){
           if(obj!='null'){
           }
           frameObj = document.getElementById("columnPropertiesframe");
           var reportId=document.forms.frmParameter.REPORTID.value;
           frameObj.src= "<%=request.getContextPath()%>/TableDisplay/PbColumnProperties.jsp?colmnname="+columnName+"&dispcolmname="+disColumnName+"&reportId="+reportId;
           $("#columnPropertiesdiv").dialog('open');
       }


       function closeColumnProperties(){
           $("#columnPropertiesdiv").dialog('close');
       }
       function savecancelBucket(){

           $("#dispbucketAnalysis").dialog('close');
           submitform();
       }
       function addCustomMeasure(reportId)
       {

           var ctxPath='<%=request.getContextPath()%>'
           $.ajax({
               type: 'GET',
               async: false,
               cache: false,
               timeout: 30000,
               url:ctxPath+'/reportViewer.do?reportBy=loadDialogs&loadDialogs=true&reportId='+reportId+'&from=viewer',
               success:function(data) {
                              $("#custmemDispDia").dialog().dialog('open');
               }
           });
//           if($.browser.msie == true)
//           {
               document.getElementById("custmemDisp").src="createCustMemberinviewer.jsp";
//           }
                          

           }


       function openImgDiv(resetPath){

           parent.document.getElementById('loading').style.display='';
       }
       function cancelCustMember(){
           $("#custmemDispDia").dialog('close');

       }
       function cancelCustMembersave(columnname){
           $("#custmemDispDia").dialog('close');
           $('#custmemMeasureDispDia').dialog('close');
       }
       function dbconremoveimage(){
           if(<%=isReportAccessible%> == false){
               $('.rtable').hide();
               alert("You Don't Have Access To This Report Please Contact Your System Admin")
               gohome();
           }
           $('#demoContainer').mb_close();

           document.getElementById("leftTd").style.display = "none";
           var frameObj = document.getElementById("widgetframe");
           var source = 'divPersistent.jsp?method=forleftTd&block=yes';
           frameObj.src = source;
           ifrmesizedynamicResize();
           document.getElementById("tdImage").src = "<%=request.getContextPath()%>/icons pinvoke/control.png";

           document.getElementById("tabParameters").style.display = "none";
           var frameObj = document.getElementById("widgetframe");
           var source = 'divPersistent.jsp?method=forParameters&block=no';
           frameObj.src = source;


           parent.document.getElementById('loading').style.display='none';
           displayRepTimeInfo('<%=PbReportId%>')
       }


       function shwStickyNote(stkid,reportId){

           var divObj=document.getElementsByName('name'+stkid);
           if(divObj.length>0){
               $.ajax({
                   type: 'GET',
                   async: false,
                   cache: false,
                   timeout: 30000,
                   url: 'stickyNoteAction.do?stickyNoteParam=forshowStickSession&disp=none&stickListId='+stkid,
                   success: function(data){
                       document.getElementById(stkid).style.display = "";
                   }
               });
           }else{
               $.ajax({
                   type: 'GET',
                   async: false,
                   cache: false,
                   timeout: 30000,
                   url: "stickyNoteAction.do?stickyNoteParam=buildStickyNote&stickNodeId="+stkid+"&reportId="+reportId,
                   success: function(data){
                       $("#showStickyNote").html(data);
                       $("#showStickyNote").show();
                       document.getElementById("showStickyNote").style.display = "";
                   }
               });
           }

       }
       function deleteSticky(stkId){
           var cofText=confirm("Sure,Do You Want to Delete")
           if(cofText==true){
               $.ajax({
                   type: 'GET',
                   async: false,
                   cache: false,
                   timeout: 30000,
                   url: 'stickyNoteAction.do?stickyNoteParam=deleteStickyNote&stickNodeId='+stkId,
                   success: function(data){
                       document.getElementById(stkId).style.display='none';
                       $('div[id='+stkId+']').remove();
                       var $currentIFrame = $('#manageStickyFrame');
                       $currentIFrame.contents().find("#"+stkId).hide();
                       $currentIFrame.contents().find("#"+stkId).remove();
                       document.getElementById('manageStickyFrame').contentWindow.location.reload()

                   }
               });
           }
       }
       function hideStickTextNow(id){
           var hideConf=confirm("Do You Want to Hide Note Temporarily");
           if(hideConf==true){
               document.getElementById(id).style.display = "none";
           }
       }
       function applyColorSticky(divid,color){
           var divObj=document.getElementById(divid);
           var trObj=divObj.getElementsByTagName("tr")
           var txtObj=divObj.getElementsByTagName("textarea")
           var tabObj=divObj.getElementsByTagName("table")
           var tdObj=trObj[1].getElementsByTagName("td");
           tdObj[0].style.backgroundColor = color;
           trObj[1].style.backgroundColor = color;
           txtObj[0].style.backgroundColor = color;
           tabObj[0].style.backgroundColor = color;

       }

       function dispGmap()
       {

           var pp=document.getElementById("gemap");
           pp.src='GeoMap.jsp';


       }


       function openAlarm(stickId){

           frameObj = document.getElementById("applyAlarmFrame");
           frameObj.src= "applyAlarm.jsp?stickId="+stickId;
           $("#applyAlarmdiv").dialog('open');
       }

       function closeAlarmDiv(){
           $("#applyAlarmdiv").dialog('close');
       }



       function openReport(stickyId,reportId){

           $.ajax({
               type: 'GET',
               async: false,
               cache: false,
               timeout: 30000,
               url: "stickyNoteAction.do?stickyNoteParam=viewReportForStickyNote&REPORTID="+reportId+"&stickyId="+stickyId,
               success:function(data){
                   document.forms.frmParameter.action=data;
                   document.forms.frmParameter.submit();
               }

           });
       }
       function openSnapShotNameDiv(){

           var flag = confirm("do you want to save the snapshot with new name");
           if (flag == true){

               $("#snapShotNameDiv").dialog('open');
           }else{
               SaveAsAdvancedHtml();
           }

       }


       function getSnapShotName(){
           var snapName = $("#snapShotName").val();
           $("#snapShotNameDiv").dialog('close');
           SaveAsAdvancedHtml(snapName);

       }
       function SaveAsAdvancedHtml(snapName)
       {

           if(snapName==undefined)
               snapName='';
           $("#loading").show();
           {

               $.ajax({
                   type: 'GET',
                   async: false,
                   cache: false,
                   timeout: 30000,
                   url:'<%=request.getContextPath()%>/dataSnapshot.do?doAction=createDataSnapshot&varhtml=fromAdvancedHtml&reportIds='+'<%=PbReportId%>'+'&snapName='+snapName,
                   success:function(data)
                   {
                       $("#loading").hide();
                       alert("Saved Advanced HTML successfully");
                   }
               });


           }
       }

       function SendReportEmail(){
           var fileType=document.getElementById("fileType1").value;
           var selectusers=document.getElementById("selectusers").value;
           var share_subject=document.getElementById("share_subject").value;
           var userstextarea=document.getElementById("userstextarea").value;
           var REPORTID=document.getElementById("REPORTID").value;
           var ctxPath=document.getElementById("h").value;
           var delimiter=$('#sDelimiter').val();
           var txtId=$('#txtIdentifier').val();
var comments=$("#comments").val();//added by Dinanath for header logo
var headerLogo=$("input[name='headerLogo']:checked").val();
var footerLogo=$("input[name='footerLogo']:checked").val();
var optionalHeader=$("input[name='optionalHeader']:checked").val();
var optionalFooter=$("input[name='optionalFooter']:checked").val();
var htmlSignature=$("input[name='htmlSignature']:checked").val();
//alert("headerLogo"+headerLogo)
           share_subject = share_subject.replace("&","--");
           if(selectusers=="")
           {
               alert("select Selective Users")
           }
           else
           {
               if(selectusers=="All")
               {parent.$("#shareReport").dialog('close');
                   $("#loading").show();
                   $.ajax({
                       type: 'GET',
                       async: false,
                       cache: false,
                       timeout: 30000,
url:'<%=request.getContextPath()%>/reportViewer.do?reportBy=sendSharereportMail&varhtml=fromAdvancedHtml&repID='+<%=PbReportId%>+'&fileType='+fileType+'&selectusers='+selectusers+'&share_subject='+encodeURIComponent(share_subject)+'&userstextarea='+encodeURIComponent(userstextarea)+'&comments='+encodeURIComponent(comments)+'&headerLogo='+headerLogo+'&footerLogo='+footerLogo+'&optionalHeader='+optionalHeader+'&optionalFooter='+optionalFooter+'&htmlSignature='+htmlSignature,
                       success:function(data)
                       {
                           $("#loading").hide();
                           alert("Report shared sucessfully with the Users");
                       }
                   });
               }
               else
               {
                   if(userstextarea=="")
                   {
                       alert("enter atleast one user id")
                   }
                   else
                   {
                       var userstextarea=document.getElementById("userstextarea").value;
                       var atpos=userstextarea.indexOf("@");
                       var dotpos=userstextarea.lastIndexOf(".");
                       if (atpos<1 || dotpos<atpos+2 || dotpos+2>=userstextarea.length)
                       {
                           alert("Not a valid e-mail address");
                       }
                       else
                       {
                           parent.$("#shareReport").dialog('close');
                           $("#loading").show();
                           $.ajax({
                               type: 'GET',
                               async: false,
                               cache: false,
                               timeout: 30000,
                               url:'<%=request.getContextPath()%>/reportViewer.do?reportBy=sendSharereportMail&varhtml=fromAdvancedHtml&repID='+<%=PbReportId%>+'&fileType='+fileType+'&selectusers='+selectusers+'&share_subject='+share_subject+'&userstextarea='+encodeURIComponent(userstextarea)+'&comments='+encodeURIComponent(comments)+'&headerLogo='+headerLogo+'&footerLogo='+footerLogo+'&optionalHeader='+optionalHeader+'&optionalFooter='+optionalFooter+'&htmlSignature='+htmlSignature,
                               success:function(data)
                               {
                                   $("#loading").hide();
                                   alert("Report shared sucessfully with the Users");
                               }
                           });
                       }
                   }
               }}}

       function openShareReportDiv()
       {
           var reportName="<%=container.getReportName()%>";
           $("#shareReport").dialog('open');
           $("#share_subject").val(reportName);
       }


       function displaytextarea()
       {
           var a=$("#selectusers").val();
           if (a=="selected" )
           {
               $("#tomailtd").show();
               $("#share_subject_id").show();
               $("#share_subject").show();
               $("#comments_id").show();
               $("#comments").show();
               $("#userstextarea").show();
               $("#userstextarea").elastic();
           }else
           {
               $("#userstextarea").val("");
               $("#share_subject_id").hide();
               $("#share_subject").hide();
               $("#comments_id").hide();
               $("#comments").hide();
               $("#tomailtd").hide();
               $("#userstextarea").hide();

           }
       }

       function storesnapshot(){
           $('#snapShotDialog').dialog('open');

       }
       function storeheadline()
       {

           $('#snapshotHeadlineDiv').dialog('open');

       }
       function storedynamicheadline(){

           $('#DynamicHeadlineDiv').dialog('open');
       }

       function showSqlQuery(sqlStr){

           $("#showSqlStrDialog").dialog('open');
           $("#showSqlStrDialog").html(document.getElementById("showSqlbox").value);
       }
       var schedulecnt;
       function openScheduleReport()
       {
           $("#scheduleName").val("");
           $("#usertextarea").val("");
           $("#emailId").show();
           $("#formatId").show();
           $("#sDatepicker").val("");
           $("#eDatepicker").val("");
           $("#hrs").val("");
           $("#mins").val("");
           $("#deleteScheduleReport").hide();
           $("#updateScheduleReport").hide();
           $("#weekday").hide();
           $("#monthday").hide();
           $("#saveScheduleReport").show();
           $.post("<%=request.getContextPath()%>/reportViewer.do?reportBy=getscheduleCount",
           function(data){
               if(data!='null'){
                   var jsonVar=eval('('+data+')')
                   schedulecnt=jsonVar.scheduleCount;
               }
           });
           var userType='<%=userType%>';
           var isPAEnableforUser='<%=isPowerAnalyserEnableforUser%>'
           $("#ScheduleReport").dialog("open");

       }
       function SendScheduleReport(){
//           alert("headerappend")
var headerLogo=$("input[name='headerLogo2']:checked").val();
var footerLogo=$("input[name='footerLogo2']:checked").val();
var optionalHeader=$("input[name='optionalHeader2']:checked").val();
var optionalFooter=$("input[name='optionalFooter2']:checked").val();
var htmlSignature=$("input[name='htmlSignature2']:checked").val();
           $("#loading").show();
           $.post("<%=request.getContextPath()%>/reportViewer.do?reportBy=dailyScheduleReport&reportId="+<%=PbReportId%>+'&fromGO='+fromGO+'&headerLogo2='+headerLogo+'&footerLogo2='+footerLogo+'&optionalHeader2='+optionalHeader+'&optionalFooter2='+optionalFooter+'&htmlSignature2='+htmlSignature,$("#dailyReportScheduleForm").serialize(),
           function(data){

               if(data==''){
                   $("#ScheduleReport").dialog("close");
                   alert("Report is Scheduled Sucessfully")
                   $("#loading").hide();
               }
               else{
                   alert('Scheduler with this name already exists! Please enter another name.')
                   $("#loading").hide();

               }
               fromGO = "false";
           });
       }
       function updateScheduleReport(){
           $("#ScheduleReport").dialog("close");
           var sId=$("#schedulerId").val();
           $.post("<%=request.getContextPath()%>/reportViewer.do?reportBy=updateScheduleDetails&reportId="+<%=PbReportId%>+'&schedulerId='+sId,$("#dailyReportScheduleForm").serialize(),
           function(data){
               alert("Report is Scheduled Sucessfully");
               dispScheduleReports();
           });

       }

       function dispScheduleReports(){
           if(document.getElementById("schedule").style.display=="none"){
               document.getElementById("schedule").style.display = "block";
               $.ajax({
                   type: 'GET',
                   async: false,
                   cache: false,
                   timeout: 30000,
                   url:'<%=request.getContextPath()%>/reportViewer.do?reportBy=getSchedulerNames&reportId='+<%=PbReportId%>,
                   success:function(data)
                   {
                       if(data!='null'){
                           var jsonVar=eval('('+data+')')
                           var schedulerIds=jsonVar.schedulerIds;
                           var schedulerNames=jsonVar.schedulerNames;
                           var temphtml=""
                           for(var i=0;i<schedulerIds.length;i++){
                               temphtml+='<tr><td style="border-right:black" align="left" colspan="2"><a href="javascript:dispScheduler('+schedulerIds[i]+','+"'"+schedulerNames[i]+"'"+')"><u>'+schedulerNames[i]+'</u></a></td></tr>';
                           }
                           $("#schedule").html(temphtml);
                       }
                       else{
                           alert("No Scheduled Reports are there")
                       }
                   }
               });

           }else{
               document.getElementById("schedule").style.display = "none";
           }
       }
       function dispScheduler(sId,sName){
           $("#scheduleId").val(sId)
           $("#editReportSchedule").dialog("open");
       }

       function deleteScheduleReport(){
           $("#ScheduleReport").dialog("close");

           var sId=$("#schedulerId").val();
           var confirmDel=confirm("Do you want to Delete Schedule");
           if(confirmDel==true){
               $.ajax({
                   type: 'GET',
                   async: false,
                   cache: false,
                   timeout: 30000,
                   url:'<%=request.getContextPath()%>/reportViewer.do?reportBy=deleteSchedulerDetails&schedulerId='+sId,
                   success:function(data)
                   {
                       dispScheduleReports();
                   }
               });
           }
       }
       function viewScheduleReport(){
           var userType='<%=userType%>';
           var isPAEnableforUser='<%=isPowerAnalyserEnableforUser%>'
           var frameObj = document.getElementById("viewScheduleFrame");
           frameObj.src = "showallSchedules.jsp";
           $("#viewReportSchedule").dialog('open');

       }
       function displayRepTimeInfo(repId){
           $("#RepTimecont").toggle(500);
           $.post(
           'reportViewer.do?reportBy=getRepTimeSpanDisplay&repId='+repId+'&from=fromtab',
           function(data){
               $("#RepTimeinfo").html("");
               $("#RepTimeinfo").html(data);
           });
       }
       function backToDash(url){
           document.frmParameter.action = url;
           document.frmParameter.submit();
       }
       function addQuickTimeBasedFormula(columnName,disColumnName,REPORTID,path)
       {

           $.ajax({
               type: 'GET',
               async: false,
               cache: false,
               timeout: 30000,
               url:path+'/reportViewer.do?reportBy=getBussTabId&columnName='+columnName,
               success:function(data){
                   if(data!='null'){
                       var jsonVar=eval('('+data+')')

                       bussTabId=jsonVar.buss_table_id;
                       grp_id=jsonVar.grp_id;
                       buss_col_id=jsonVar.buss_col_id;
                       buss_col_name=jsonVar.buss_col_name;
                       buss_table_name=jsonVar.buss_table_name;
                       folder_id=jsonVar.folder_id
                       user_col_id=jsonVar.user_col_id;
                   }

                   if(user_col_id=='SUMMARIZED'||user_col_id=='summarized'||user_col_id=='summarised'){
                       alert('Quick Formula can not be created for this measure!')
                   }
                   else {
                       var frameObj = document.getElementById("QuickTimeBasedFormulaFrame");
                       frameObj.src = "quickTimeBasedFormula.jsp?bussTableId="+bussTabId+"&grpId="+grp_id+"&bussColId="+buss_col_id+"&colName="+buss_col_name+"&tabName="+buss_table_name+"&eleName="+columnName+"&folder_id="+folder_id;
                       $('#QuickTimeBasedFormulaDiv').dialog('open');
                   }

               }

           });
       }
       function getVarcharOp(){
           var formulaVal = document.forms.myForm1.formulaType.value;
           var htmlVar = "";
           if(formulaVal=='VARCHAR'){
               htmlVar+="<td><label class=\"label\"><b>Operation :</b>&nbsp;&nbsp;&nbsp;&nbsp;</label></td>";
               htmlVar+="<td><select id=\"basicoperators\" name=\"basicoperators\">";
               htmlVar+="<option value=\"NONE\">---SELECT---</option>";
               htmlVar+="<option value=\"CONCAT\">Concatination</option>";
               htmlVar+="<option value=\"INTER\">Interpretation</option>";
               htmlVar+="</td>";
           }
           $("#myTabBodyVar").html(htmlVar)
       }
       function advanceFormula(columnName,disColumnName,REPORTID,path){
           var ctxPath='<%=request.getContextPath()%>'
           var htmlVar = "";
           htmlVar = "<td colspan=\"2\">\n\
                 <center><input type=\"button\" class=\"navtitle-hover\" style=\"width:auto\" value=\"Next\" onclick=\"getAdvanceFormulaDiv('"+columnName+"','"+disColumnName+"',"+REPORTID+",'"+path+"')\">\n\
              </td>";
           $('#formulaType option[value=NONE]').attr('selected','selected');
           $('#advanceFormulaTypeDiv').dialog('open');
           $("#myTabBodyVar").html("")
           $("#myTabBody").html(htmlVar)
       }

       function getAdvanceFormulaDiv(columnName,disColumnName,REPORTID,path){
           var formulaVal = document.forms.myForm1.formulaType.value;
           var basicOp = "";
           if(formulaVal=='VARCHAR'){
               basicOp = document.forms.myForm1.basicoperators.value;
           }
           if(formulaVal=='NONE' || basicOp=='NONE'){
               alert('Please select all details')
           }
           else {
               var ctxPath='<%=request.getContextPath()%>'
               $.ajax({
                   type: 'GET',
                   async: false,
                   cache: false,
                   timeout: 30000,
                   url:ctxPath+'/reportViewer.do?reportBy=loadDialogs&loadDialogs=true&reportId='+REPORTID+'&from=viewer',
                   success:function(data) {

                       var frameObj = document.getElementById("advanceFormulaFrame");
                       frameObj.src = "CreateAdvanceFormula.jsp?columnName="+columnName+"&formulaVal="+formulaVal+"&basicOp="+basicOp;
                       $('#advanceFormulaTypeDiv').dialog('close');
                       if(formulaVal=='NUMBER' || formulaVal=='VARCHAR'){
                           if(basicOp=='CONCAT')
                               $("#advanceFormulaDiv").dialog('option', 'height', 480);
                           else
                               $("#advanceFormulaDiv").dialog('option', 'height', 590);
                       }
                       else {
                           $("#advanceFormulaDiv").dialog('option', 'height', 480);
                       }
                       $('#advanceFormulaDiv').dialog('open');

                   }
               });}
       }
       function createSegmentation(columnName,disColumnName,REPORTID,path){
           tempBussColName = columnName;
           $.post("<%= request.getContextPath()%>/userLayerAction.do?userParam=getElementColType&columnName="+columnName,function(data){
               var jsonVar=eval('('+data+')')
               var colType=jsonVar.userColType[0];
               folderId=jsonVar.folderId[0];
               var isSummValid=jsonVar.isSummValid[0];
               if(colType=="NUMBER" || colType=="number" || colType=="calculated" || colType=="CALCULATED" ||  colType=="SUMMARIZED" && isSummValid=="true"){
                   $.post("<%= request.getContextPath()%>/userLayerAction.do?userParam=getElementIdDetails&columnName="+columnName,function(data){
                       var jsonVar=eval('('+data+')')
                       var eleDetails=jsonVar.eleDetails[0]
                       var n=eleDetails.split("~");
                       bucketDetails=eleDetails;
                       var tabName=n[1];
                       var colName=n[3];
                       var colType=n[4];
                       var bussTableId=n[2];
                       var m=n[0].split(",");
                       var bussColId=m[0];
                       var colId=m[1];
                       var tabId=m[2];
                       var grpId=m[3];
                       var elementId=n[5];
                       showBucketDbExistance(bussTableId);

                   });
               }
               else {
                   alert("This function can be applied only for number and calculated measures ! ")
               }
           });
       }
       function showBucketDbExistance(str){
           if (str.length==0)
           {
               document.getElementById("txtHint").innerHTML="";
               return;
           }
           xmlHttp2=GetXmlHttpObject();
           if (xmlHttp2==null)
           {
               alert ("Your browser does not support AJAX!");
               return;
           }
           var ctxPath='<%=request.getContextPath()%>'
           var url=ctxPath+"/BucketDBExistance";
           url=url+"?bussTableId="+str;
           xmlHttp2.onreadystatechange=stateChangedBucketDbExistance;
           xmlHttp2.open("GET",url,true);
           xmlHttp2.send(null);
       }
       function stateChangedBucketDbExistance()
       {
           if (xmlHttp2.readyState==4)
           {
               var output=xmlHttp2.responseText;
               if(output==2){
                   var n=bucketDetails.split("~");
                   var tabName=n[1];
                   var colName=n[3];
                   var colType=n[4];
                   var bussTableId=n[2];
                   var m=n[0].split(",");
                   var bussColId=m[0];
                   var colId=m[1];
                   var tabId=m[2];
                   var grpId=m[3];
                   var elementId=n[5];
                   createBucket(colName,colId,tabId,colType,tabName,grpId,bussColId,bussTableId,elementId);
               }
               else{
                   alert('Please create Bucket Tables in your Database');
               }
           }
       }
       function createBucket(colName,colId,tabId,colType,tabName,grpId,bussColId,bussTableId,elementId)
       {
           var tempBuck = 'yes';
           var frameObj=document.getElementById("bucketDisp1");
           var source = "createBucket.jsp?colName="+colName+"&colId="+colId+"&tabId="+tabId+"&colType="+colType+"&tabName="+tabName+"&grpId="+grpId+"&bussColId="+bussColId+"&bussTableId="+bussTableId+"&tempBuck="+tempBuck+"&folderId="+folderId+"&elementId="+elementId+"&tempBussColName="+tempBussColName+"&reportId="+<%=PbReportId%>;

           if(colType==undefined){
               alert("Please Select Measure Column")
           }else{
               frameObj.src=source;
               $("#createBucketdiv").dialog('open')
           }

       }
       function cancelBuckets()
       {
           $("#QuickTimeBasedFormulaDiv").dialog('close')
       }
       function factFormulaDiv()
       {
           var userType='<%=userType%>';
           var isPAEnableforUser='<%=isPowerAnalyserEnableforUser%>'
           $.post("<%=request.getContextPath()%>/reportViewer.do?reportBy=viewFactFormula&reportId="+<%=PbReportId%>,
           function(data){
               if(data!='null'){
                   var jsonVar=eval('('+data+')')
                   var filterId=jsonVar.filterId;
                   var filterName=jsonVar.filterName;
                   var filterFormula=jsonVar.filterFormula;
                   var html="";
                   html+="<table width='100%'><thead class='navtitle-hover'><th>&nbsp</th><th>Filter Name</th><th>Filter Formula</th></thead><tbody>"
                   for(var i=0;i<filterId.length;i++){
                       html+="<tr><td><input type=\'checkbox\' id=\'filterId"+filterId[i]+"\' name=\'filetrId\'  value="+filterId[i]+"></td>";
                       html+="<td align='center'>"+filterName[i]+"</td>";
                       html+="<td align='center'>"+filterFormula[i]+"</td></tr>";
                   }
                   html+="<tr><td width=\'20px;\'>&nbsp;</td></tr><tr><td align='center'><input class='navtitle-hover' type=\'button\' value='Delete' onclick=deleteFormula('<%=request.getContextPath()%>','<%=PbReportId%>')></td></tr>";
               }
               $("#viewFactformulaDiv").html(html);
           });

           $("#viewFactformulaDiv").dialog('open');

       }
       function cancelDim(){
           $("#AddMoreParamsDiv").dialog('close');
       }
       function gotoDBCON1(ctxPath,repid){
           document.forms.frmParameter.action=ctxPath+"/reportViewer.do?reportBy=viewReport&REPORTID="+repid+"&action=open";
           document.forms.frmParameter.submit();

       }
       function gotoDBCON2(ctxPath,repid){
           document.forms.frmParameter.action=ctxPath+"/dashboardViewer.do?reportBy=viewDashboard&REPORTID="+repid;
           document.forms.frmParameter.submit();

       }
       function getCustomDate(){
           var Date=""
           if(document.getElementById("reportDate") != null && document.getElementById("reportDate").checked){

               var htmlVar = "";
               $("#dateRangeTab").hide();
               $("#overWriteReport").height(350);
           }else if(document.getElementById("customDate") != null && document.getElementById("customDate").checked){
               $("#dateRangeTab").show();
               $("#overWriteReport").height(500);
           }else if(document.getElementById("currdetails") != null && document.getElementById("currdetails").checked){
               var htmlVar = "";
               $("#dateRangeTab").hide();
               $("#overWriteReport").height(350);
           }
       }
       function getPrevReports()
       {
           $("#reportNames").toggle(1000);
       }
       function scheduleOption(){
           var scheduleId=$("#scheduleId").val();

           var userType='<%=userType%>';
           $.post("<%=request.getContextPath()%>/reportViewer.do?reportBy=checkSchedulerPermission&scheduleId="+scheduleId+"&userType="+userType,
           function(data){
               var jsonVar=eval('('+data+')')
               var dataVal = jsonVar.dataVal;
               if(dataVal=="true"){
                   if($("#deleteSchedule").is(':checked')){
                       var confirmDel=false;
                       confirmDel=confirm("Do you want to Delete Schedule");
                       if(confirmDel==true){
                           $.ajax({
                               type: 'GET',
                               async: false,
                               cache: false,
                               timeout: 30000,
                               url:'<%=request.getContextPath()%>/reportViewer.do?reportBy=deleteSchedulerDetails&schedulerId='+scheduleId,
                               success:function(data)
                               {
                                   dispScheduleReports();
                               }
                           });
                       }

                   }
                   if($("#editSchedule").is(':checked')){
                       var confirmDel=false;
                       confirmDel=confirm("Do you want to Edit Scheduler Details");
                       if(confirmDel==true){
                           $("#editReportSchedule").dialog("close");
                           $.ajax({
                               type: 'GET',
                               async: false,
                               cache: false,
                               timeout: 30000,
                               url:'<%=request.getContextPath()%>/reportViewer.do?reportBy=editSchedulerDetails&reportId='+<%=PbReportId%>+'&schedulerId='+scheduleId,
                               success:function(data)
                               {
                                   if(data!='null'){
                                       var jsonVar=eval('('+data+')')
                                       $("#scheduleName").val(jsonVar.schedulerName);
                                       $("#usertextarea").val(jsonVar.ReportmailIds);
                                       $("select#fileType").attr("value",jsonVar.contentType);
                                       var sd = new Date(jsonVar.startDate)
                                       var ed = new Date(jsonVar.endDate)
                                       var strtDate=sd.getDate()+"/"+(1+(sd.getMonth()))+"/"+sd.getFullYear();
                                       var endDate=ed.getDate()+"/"+(1+(ed.getMonth()))+"/"+ed.getFullYear();
                                       var particularDay=jsonVar.particularDay;
                                       $("select#frequency").attr("value",jsonVar.frequency);
                                       if(jsonVar.frequency=="Weekly"){
                                           $("#weekday").show();
                                           $("select#particularDay").attr("value",particularDay);
                                           $("#monthday").hide();
                                       }else if(jsonVar.frequency=="Monthly"){
                                           $("#weekday").hide();
                                           $("#monthday").show();
                                           $("select#monthParticularDay").attr("value",particularDay);
                                       }else{
                                           $("#weekday").hide();
                                           $("#monthday").hide();
                                       }

                                       $("#sDatepicker").val(strtDate);
                                       $("#eDatepicker").val(endDate);
                                       $("select#Data").attr("value",jsonVar.dataSelection);
                                       var time=jsonVar.scheduledTime.split(":");
                                       $("select#hrs").attr("value",time[0]);
                                       $("select#mins").attr("value",time[1]);
                                       $("#updateScheduleReport").show();
                                       $("#saveScheduleReport").hide();
                                       $("#schedulerId").val(scheduleId);
                                       $("#ScheduleReport").dialog("open");
                                   }
                               }
                           });
                       }

                   }
                   if($("#editScheduleReport").is(':checked')){
                       var confirmDel=false;
                       confirmDel=confirm("Report Parameter Filters for the selected schedule would be over-written by current report view with this option. Would you like to proceed with the current report view for the scheduler");


                       if(confirmDel==true){
                           $.ajax({
                               type: 'GET',
                               async: false,
                               cache: false,
                               timeout: 30000,

                               url:'<%=request.getContextPath()%>/reportViewer.do?reportBy=updateScheduleParamMetadataDetails&reportId='+<%=PbReportId%>+'&schedulerId='+scheduleId,
                               success:function(data)
                               {
                                   dispScheduleReports();
                               }
                           });
                       }
                   }
               }
               else {
                   alert('You do not have suffiecient privileges')
               }
               $("#editReportSchedule").dialog("close");

           });
       }
       function getDynamicDetails(){
           var timeDet = "";
           if(document.getElementById("static") != null && document.getElementById("static").checked){
               $("#selectTimeDet").hide();
           }else if(document.getElementById("dynamic") != null && document.getElementById("dynamic").checked){
               $("#selectTimeDet").show();
           }
       }


       function paramListDisp(repId)
       {
               
           $.post(
           'reportViewer.do?reportBy=getRepTimeSpanDisplay&repId='+repId+'&from=fromtab',
           function(data){
               $("#RepTimeinfo1").html("");
               $("#RepTimeinfo1").html(data);
           });
           $( "#allParametersTab" ).toggle("slow");
           $( "#remainingRegions" ).show();
           $( "#paramRegionTop" ).hide();
           $( "#tabParameters" ).hide();
           $( "#allActions" ).hide();
           $( "#snapShotsDiv" ).hide();
           $( "#manageStickyDispDiv" ).hide();
           $( "#topBottomDispDiv" ).hide();
           $( "#scheduledReps" ).hide();
           $( "#busRoleDispDiv" ).hide();


       }
       function hideParamList()
       {
           $( "#tabParameters" ).hide();
       }
       function tabparamListDisp(){
           var viewbys=$("#numbuerOfViewbys").val();
           if (!$('#tabParameters').is(':visible')) {
               var divHeight=(4/5)*($(window).height());
               $("#tabParameters").height(divHeight);
               $(".dynamicClass").css('height', (((9/10)*(divHeight))-(viewbys*12)));

           }
           $( "#tabParameters" ).toggle("slow" );

           $( "#allParametersTab" ).hide();
       }
       function closeAllparamsTab()
       {
           $( "#tabParameters" ).hide();
           $( "#allParametersTab" ).hide();
       }
       function closeparamsTab()
       {
           $( "#tabParameters" ).hide();

       }
       function parametersInRegion()
       {
           $( "#paramRegionTop" ).hide();
           $( "#allActions" ).hide();
           $( "#snapShotsDiv" ).hide();
           $( "#manageStickyDispDiv" ).hide();
           $( "#topBottomDispDiv" ).hide();
           $( "#scheduledReps" ).hide();
           $( "#busRoleDispDiv" ).hide();

           $( "#remainingRegions" ).hide();

       }
       function allActions(){
           $( "#paramRegionTop" ).hide();
           $( "#scheduledReps" ).hide();
           $( "#snapShotsDiv" ).hide();
           $( "#manageStickyDispDiv" ).hide();
           $( "#topBottomDispDiv" ).hide();
           $( "#allActions" ).toggle(500 );
           $( "#busRoleDispDiv" ).hide();

           $( "#remainingRegions" ).hide();
       }
       function filtersDisplayDiv(){
           $( "#filtersDisplayDiv" ).toggle(500 );
       }
       function scheduledReps(){
           $( "#paramRegionTop" ).hide();
           $( "#snapShotsDiv" ).hide();
           $( "#manageStickyDispDiv" ).hide();
           $( "#topBottomDispDiv" ).hide();
           $( "#busRoleDispDiv" ).hide();

           $( "#remainingRegions" ).hide();
           $( "#allActions" ).hide();
           $( "#scheduledReps" ).toggle(500 );
           document.getElementById("schedule1").style.display = "block";
           $.ajax({
               type: 'GET',
               async: false,
               cache: false,
               timeout: 30000,
               url:'<%=request.getContextPath()%>/reportViewer.do?reportBy=getSchedulerNames&reportId='+<%=PbReportId%>,
               success:function(data)
               {
                   if(data!='null'){
                       var jsonVar=eval('('+data+')')
                       var schedulerIds=jsonVar.schedulerIds;
                       var schedulerNames=jsonVar.schedulerNames;
                       var temphtml=""
                       for(var i=0;i<schedulerIds.length;i++){
                           temphtml+='<tr><td style="border-right:black" align="left" colspan="2"><a href="javascript:dispScheduler('+schedulerIds[i]+','+"'"+schedulerNames[i]+"'"+')"><u>'+schedulerNames[i]+'</u></a></td></tr>';
                       }
                       $("#schedule1").html(temphtml);
                   }
                   else{
                       alert("No Scheduled Reports are there")
                   }
               }
           });

       }
       function getsnapShots(){
           $( "#paramRegionTop" ).hide();
           $( "#allActions" ).hide();
           $( "#scheduledReps" ).hide();
           $( "#topBottomDispDiv" ).hide();
           $( "#manageStickyDispDiv" ).hide();
           $( "#snapShotsDiv" ).show();
           $( "#busRoleDispDiv" ).hide();

           $( "#remainingRegions" ).hide();
           document.getElementById("snapshots").style.display = "block";
           var frameObj = document.getElementById("widgetframe");
           var source = 'divPersistent.jsp?method=forsnapshots&block=yes';
           frameObj.src = source;
       }
       function getdisplayStickyNotes(){
           $("#manageStickyDispDiv").show();

           $( "#paramRegionTop" ).hide();
           $( "#allActions" ).hide();
           $( "#scheduledReps" ).hide();
           $( "#topBottomDispDiv" ).hide();
           $( "#snapShotsDiv" ).hide();
           $( "#busRoleDispDiv" ).hide();

           $( "#remainingRegions" ).hide();
           document.getElementById("manageSticky").style.display = "block";
           var frameObj = document.getElementById("widgetframe");
           var source = 'divPersistent.jsp?method=forManageSticky&block=yes';
           frameObj.src = source;
       }
       function getdisplayTopBottom(){
           $("#manageStickyDispDiv").hide();
           $("#topBottomDispDiv").show();

           $( "#paramRegionTop" ).hide();
           $( "#allActions" ).hide();
           $( "#scheduledReps" ).hide();
           $( "#snapShotsDiv" ).hide();
           $( "#busRoleDispDiv" ).hide();

           $( "#remainingRegions" ).hide();
           document.getElementById("topBot").style.display = "block";
           var frameObj = document.getElementById("widgetframe");
           var source = 'divPersistent.jsp?method=fortopBot&block=yes';
           frameObj.src = source;
       }
       function getdisplayBusRoles(){
           $("#manageStickyDispDiv").hide();
           $("#topBottomDispDiv").hide();

           $( "#paramRegionTop" ).hide();
           $( "#allActions" ).hide();
           $( "#scheduledReps" ).hide();
           $( "#snapShotsDiv" ).hide();
           $( "#remainingRegions" ).hide();
           $( "#busRoleDispDiv" ).show();
       }
       function remainingRegions(repId){
           $( "#paramRegionTop" ).hide();
           $( "#allActions" ).hide();
           $( "#snapShotsDiv" ).hide();
           $( "#manageStickyDispDiv" ).hide();
           $( "#topBottomDispDiv" ).hide();
           $( "#scheduledReps" ).hide();
           $( "#busRoleDispDiv" ).hide();

           $( "#remainingRegions" ).show();
       }
       function getBackToPrevReport(){
           var urlList = [];
           $("#reportNames #getBackToReprotUl").each(function(){
               var phrase = '';
               $(this).find('li').each(function(){
                   var current = $(this);
                   phrase = $('a', this).attr('href');
                   urlList.push(phrase);
               });
           });
           var urlLength=urlList.length;
           window.location.href=urlList[urlLength-2];
       }

       function callFilterDiv() {
           $("#topDisplayParams").toggle(500);
       }



        </script>
        <style>

            #fixedtop1 { position: absolute; top: 0px; left:200px; margin: auto; right: 800px; width: 50px; border: none; z-index: 50; }
            #center250a { width: auto;height: 65px;  background:none; }
            #center250b { width: auto;height: 65px;   background:none; }
           
            #datalist>table tr{
                
                border-bottom: 1px solid #000;
            }
                
            #datalist>table td{
                padding: 8px;
            }
            #datalist>table td:hover{
                background-color: #d1d1d1;
            }                                   
        </style>
        <script>

            var fdate,cfdate;
            var fdate1,cfdate1;
            var tdate,ctdate;
            var tdate1,ctdate1;
            var datetype;
		
		 var incriment=0;
			
//            $(function() {
//                //                <% if (timeinfo.get(1).equalsIgnoreCase("PRG_DATE_RANGE") && sytm.equalsIgnoreCase("No")) {%>
//                $( "#fromdate" ).datepicker({
//                    showOn: "button",
//                    buttonImage: "images/calendar_18x16.gif",
//                    buttonImageOnly: true,
//                    showButtonPanel: true,
//                    changeMonth: true,
//                    changeYear: true,
//                    showButtonPanel: true,
//                    numberOfMonths: 1,
//                    stepMonths: 1,
//                    dateFormat: "D,d,M,yy",
//                      yearRange: "1900:2100",
//                    onClose: function showdate(){
//                        var a;
//                        a=($("#fromdate").val());
//
//                        var dateArr=new Array()
//                        dateArr=a.split(",");
//                        if(a!=""){
//                            $("#field1").html(dateArr[2]+"'"+dateArr[3].substring(2))
//                            $("#field2").html(dateArr[1])
//                    $("#field3").html(dateArr[0])
//                        }
//                    }
//
//
//                }).datepicker("setDate", new Date(('<%=vals1[2]%>')) );
//                $( "#todate" ).datepicker({
//                    showOn: "button",
//                    buttonImage: "images/calendar_18x16.gif",
//                    buttonImageOnly: true,
//                    showButtonPanel: true,
//                    changeMonth: true,
//                    changeYear: true,
//                    showButtonPanel: true,
//                    numberOfMonths: 1,
//                    stepMonths: 1,
//                    dateFormat: "D,d,M,yy",
//                      yearRange: "1900:2100",
//                    onClose: function showdate(){
//                        var b;
//                        b=($("#todate").val());
//                        var dateArr=new Array()
//                        dateArr=b.split(",");
//                        if(b!=""){
//                            $("#tdfield1").html(dateArr[2]+"'"+dateArr[3].substring(2))
//                            $("#tdfield2").html(dateArr[1])
//                            $("#tdfield3").html(dateArr[0])
//                        }
//
//                        tdate=b;
//                        tdate1 = new Date(tdate).getTime();
//                        var d =($("#fromdate").val());
//                        fdate=d;
//                        fdate1 = new Date(fdate).getTime();
//                        if(tdate1<fdate1){
//                            alert("Wrong Date Selected");
//                        }
//
//
//                    }
//
//                }).datepicker("setDate", new Date(('<%=vals1[3]%>')) );
//                $( "#comparefrom" ).datepicker({
//                    showOn: "button",
//                    buttonImage: "images/calendar_18x16.gif",
//                    buttonImageOnly: true,
//                    showButtonPanel: true,
//                    dateFormat: "D,d,M,yy",
//                    changeYear: true,
//                    changeMonth: true,
//                    showButtonPanel: true,
//                    numberOfMonths: 1,
//                    stepMonths: 1,
//                      yearRange: "1900:2100",
//                    onClose: function showdate(){
//                        var a;
//                        a=($("#comparefrom").val());
//                        var dateArr=new Array()
//                        dateArr=a.split(",");
//                        if(a!=""){
//                            $("#cffield1").html(dateArr[2]+"'"+dateArr[3].substring(2))
//                            $("#cffield2").html(dateArr[1])
//                            $("#cffield3").html(dateArr[0])
//                        }
//                    }
//
//                }).datepicker("setDate", new Date(('<%=vals1[4]%>')) );
//                $( "#compareto" ).datepicker({
//                    showOn: "button",
//                    buttonImage: "images/calendar_18x16.gif",
//                    buttonImageOnly: true,
//                    showButtonPanel: true,
//                    changeMonth: true,
//                    changeYear: true,
//                    showButtonPanel: true,
//                    numberOfMonths: 1,
//                    stepMonths: 1,
//                    dateFormat: "D,d,M,yy",
//                     yearRange: "1900:2100",
//                    onClose: function showdate(){
//                        var b;
//                        b=($("#compareto").val());
//
//                        var dateArr=new Array()
//                        dateArr=b.split(",");
//                        if(b!=""){
//                            $("#ctfield1").html(dateArr[2]+"'"+dateArr[3].substring(2))
//                            $("#ctfield2").html(dateArr[1])
//                            $("#ctfield3").html(dateArr[0])
//                        }
//
//                        ctdate=b;
//
//                        ctdate1 = new Date(ctdate).getTime();
//                        var e =($("#comparefrom").val());
//                        cfdate=e;
//
//                        cfdate1 = new Date(cfdate).getTime();
//                        if(ctdate1<cfdate1){
//                            alert("Wrong Date Selected");
//                        }
//
//                    }
//
//                }).datepicker("setDate", new Date(('<%=vals1[5]%>')) );
//            <%} else if (timeinfo.get(1).equalsIgnoreCase("PRG_STD") && sytm.equalsIgnoreCase("No")) {%>
//
//                            $( "#perioddate" ).datepicker({
//                                showOn: "button",
//                                buttonImage: "images/calendar_18x16.gif",
//                                buttonImageOnly: true,
//                                showButtonPanel: true,
//                                changeMonth: true,
//                                changeYear: true,
//                                showButtonPanel: true,
//                                numberOfMonths: 1,
//                                stepMonths: 1,
//                                dateFormat: "D,d,M,yy",
//                                yearRange: "1900:2100",
//                                onClose: function showdate(){
//                                    var a;
//                                    a=($("#perioddate").val());
//                                    var dateArr=new Array()
//                                    dateArr=a.split(",");
//                                    if(a!=""){
//                                        $("#pfield1").html(dateArr[2]+"'"+dateArr[3].substring(2))
//                                        $("#pfield2").html(dateArr[1])
//                                $("#pfield3").html(dateArr[0])
//                                    }
//                                }
//
//
//                            }).datepicker("setDate", new Date(('<%=vals1[2]%>')) );
//            <%}%>
//                        });

                        function autohide(){
                            $('#center250a').toggle();
                            $('#center250b').toggle();
                        }
                        function autoshow(){
                            $('#center250a').show();
                            $('#center250b').show();
                            $('#autoshow').hide();
                            if(document.getElementById("tabParameters345").style.display == 'none'){
                                $('#tabParameters345').show();
                            }else{
                                $('#tabParameters345').hide();
                            }
                        }
                        function dateclick(id)
                        {
                            $('#datetext').val('topdate');
                            var  perioddate=$('#perioddate').val();
                            var fromdate=$('#fromdate').val();
                            var todate=$('#todate').val();
                            var comparefrom=$('#comparefrom').val();
                            var compareto=$('#compareto').val();
                            $.ajax({
                                type: 'GET',
                                async: false,
                                cache: false,
                                timeout: 30000,
                                url:"<%=request.getContextPath()%>/reportViewer.do?reportBy=dateParse&perioddate="+perioddate+"&fromdate="+fromdate+"&todate="+todate+"&comparefrom="+comparefrom+"&compareto="+compareto,
                                success:function(data)
                                {
                                    var data1=new Array()
                                    data1=data.toString().split(",");
                                    var perioddate=data1[0];
                                    var fromdate=data1[0];
                                    var todate=data1[1];
                                    var comparefrom=data1[2];
                                    var compareto=data1[3];

                              <% if(timeinfo.get(1).equalsIgnoreCase("PRG_DATE_RANGE")) { %>
                                           var getdate;
                                        getdate=($("#fromdate").val());

                                      var dateArr=new Array()
                                     dateArr=getdate.split(",");
                      // alert("dateArr"+dateArr)



            <% ReportTemplateDAO dao1 = new ReportTemplateDAO();
                                                   String isopen = request.getParameter("action");
                                           ArrayList dateList = new ArrayList();
                                                   if (dao1.getAutometicDate(PbReportId)) {%>
                                                                   if(id=='fromdate'){

            <%dateList = dao1.getDefaultDate(PbReportId, isopen);
                    if (!dateList.isEmpty()) {%>


                                var theDate = new Date(dateArr);
                                        theDate.setDate(theDate.getDate()<%=dateList.get(0)%>);
                                        var fieldarr = ((theDate).toString()).split(" ");

                                var theDate1 = new Date(dateArr);
                                        theDate1.setDate(theDate1.getDate()<%=dateList.get(1)%>);
                                        var tdfieldarr = ((theDate1).toString()).split(" ");

                                var theDate2 = new Date(dateArr);
                                        theDate2.setDate(theDate2.getDate()<%=dateList.get(2)%>);
                                        var cffieldarr = ((theDate2).toString()).split(" ");

                                var theDate3 = new Date(dateArr);
                                        theDate3.setDate(theDate3.getDate()<%=dateList.get(3)%>);
                                        var ctfieldarr = ((theDate3).toString()).split(" ");

                                        $("#field1").html(dateArr[2]+"'"+dateArr[3].substring(2))
                                        $("#field2").html(dateArr[1])
                                        $("#field3").html(dateArr[0])
                                        $("#tdfield1").html(tdfieldarr[1]+"'"+tdfieldarr[3].substring(2))
                                        $("#tdfield2").html(tdfieldarr[2])
                                        $("#tdfield3").html(tdfieldarr[0])
                                        $("#cffield1").html(cffieldarr[1]+"'"+cffieldarr[3].substring(2))
                                        $("#cffield2").html(cffieldarr[2])
                                        $("#cffield3").html(cffieldarr[0])
                                        $("#ctfield1").html(ctfieldarr[1]+"'"+ctfieldarr[3].substring(2))
                                        $("#ctfield2").html(ctfieldarr[2])
                                        $("#ctfield3").html(ctfieldarr[0])
                                        $( "#todate" ).datepicker("setDate",theDate1);
                                        $( "#comparefrom" ).datepicker("setDate",theDate2);
                                        $( "#compareto" ).datepicker("setDate",theDate3);
            <% } else {%>
                                                          $("#field1").html(dateArr[2]+"'"+dateArr[3].substring(2))
                                                          $("#field2").html(dateArr[1])
                                                          $("#field3").html(dateArr[0])
            <% }
            %>
                                                        }
            <%} else {%>
                                            $("#field1").html(dateArr[2]+"'"+dateArr[3].substring(2))
                                            $("#field2").html(dateArr[1])
                                            $("#field3").html(dateArr[0])
            <% }%>
            <%} else {%>
                                                      $('#datepicker').val(perioddate);
            <%}%>
                                                      }
                                                  });

                                              }
                                              function viewReset(repId){
                                                  var displaySeqNo="";
                                                  var paramDisplyName="";
                                                  var parametertype="";
                                                  var elementId="";
                                                  var paramdisId="";


                                                  $.post(
                                                  'reportViewer.do?reportBy=viewParameterReset&reportId='+repId,

                                                  function(data){

                                                      var jsonVar=eval('('+data+')')
                                                      displaySeqNo=jsonVar.displaySeqNoList
                                                      paramDisplyName=jsonVar.paramDisplyNameList
                                                      parametertype=jsonVar.parametertypeList
                                                      elementId=jsonVar.elementIdList
                                                      paramdisId=jsonVar.paramdispIdList

                                                      var html="";
                                                      var foothtml="";
                                                      for(var i=0;i<paramDisplyName.length;i++)
                                                      {
                                                          html+="<tr><td><input type='text'  id=\'displaySeqNo"+displaySeqNo[i]+"\' value="+displaySeqNo[i]+"></td>";
                                                          html+="<td><input type='text' id=\'paramDisplyName"+i+"\' value=\'"+paramDisplyName[i]+"\'></td>";
                                                          html+="<td><input type='text' id=\'parametertype"+i+"\'   value="+parametertype[i]+"></td>";
                                                          html+="<td><select id =\'paramid-"+paramdisId[i]+"\' name=\'paramid-"+paramdisId[i]+"\'>";
                                                          if(parametertype[i]=="INCLUDED"){
                                                              html+="<option selected value=\"INCLUDED\">INCLUDED</option>";
                                                          } else{
                                                              html+="<option value=\"INCLUDED\">INCLUDED</option>";
                                                          }if(parametertype[i]=="EXCLUDED"){
                                                              html+="<option selected value=\"EXCLUDED\">EXCLUDED</option>";
                                                          } else{
                                                              html+="<option value=\"EXCLUDED\">EXCLUDED</option>";
                                                          } if(parametertype[i]=="NOT_SELECTED") {
                                                              html+="<option selected value=\"NOT_SELECTED\">NOT_SELECTED</option>";
                                                          } else{
                                                              html+="<option value=\"NOT_SELECTED\">NOT_SELECTED</option>";
                                                          }if(parametertype[i]=="LIKE") {
                                                              html+="<option selected value=\"LIKE\">LIKE</option>";
                                                          }else{
                                                              html+="<option value=\"LIKE\">LIKE</option>";
                                                          }if(parametertype[i]=="NOT LIKE"){
                                                              html+="<option selected value=\"NOT LIKE\">NOT LIKE</option>";
                                                          }else{
                                                              html+="<option value=\"NOT LIKE\">NOT LIKE</option>";
                                                          }if(parametertype[i]=="IN"){
                                                              html+="<option selected value=\"IN\">IN</option>";
                                                          }else{
                                                              html+="<option value=\"IN\">IN</option>";
                                                          }if(parametertype[i]=="NOT IN"){
                                                              html+="<option selected value=\"NOT IN\">NOT IN</option>";
                                                          }else{
                                                              html+="<option value=\"NOT IN\">NOT IN</option>";
                                                          }
                                                  html+="</select></        td>";
                                                          html+="</tr>";

                                                      }

                                                      foothtml+="<tr><td align='center' colspan='4' align='center' rowspan='2'><center><input type='button' value='Done' class='navtitle-hover' style='width:auto;height:20px;color:black' onclick=updateRepParams(<%=PbReportId%>,'"+paramdisId+"') ></center></td></tr>";
                                                      $("#viewparamId").html(html);
                                                      $("#viewParamfoot").html(foothtml);
                                                      $("#viewResetParameterDiv").dialog('open');
                                                  });


                                              }
                                              function updateRepParams(pbrepId,paramdispIds)
                                              {
                                                  $("#viewResetParameterDiv").dialog('close');
                                                  $.post(
                                                  'reportViewer.do?reportBy=updateParamswithChanges&reportId='+pbrepId+'&paramdispIds='+paramdispIds,$("#ResetParamsForm").serialize(),
                                                  function(data){
                                                      alert("Changes Will Be Reflected Once You Reset The Report")
                                                  });
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
                                              function checkRefreshFrequency(id){
                                                  if($("#"+id).val()=="Weekly"){
                                                      $("#refreshweekday").show();
                                                      $("#refreshmonthday").hide();
                                                      $("#refreshhourly").hide();
                                                  }else if($("#"+id).val()=="Monthly"){
                                                      $("#refreshweekday").hide();
                                                      $("#refreshmonthday").show();
                                                      $("#refreshhourly").hide();
                                                  }else if($("#"+id).val()=="Hourly"){
                                                      $("#refreshweekday").hide();
                                                      $("#refreshmonthday").hide();
                                                      $("#refreshhourly").show();
                                                  }else{
                                                      $("#refreshweekday").hide();
                                                      $("#refreshmonthday").hide();
                                                      $("#refreshhourly").hide();
                                                  }
                                              }
                                              function listDisp2()
                                              {
                                                  $("#reportNames").toggle(500);

                                              }
                                              function  difineCustomSeq(bizRoles,reportId)
                                              {
                                                  $.post('reportViewer.do?reportBy=defineCustomSequence&folderIds='+bizRoles+'&REPORTID='+reportId, $("#myGrpForm").serialize() ,
                                                  function(data){
                                                      $("#DefinecustSeqtabid").html(data)
                                                      $(".sortable").sortable();
                                                      $("#DefinecustSeqDialog").dialog('open')
                                                  });
                                              }
                                              function reOrderCustSeq(colviewby)
                                              {
                                                  var ReportId=$("#custseqsubid").val();
                                                  var tempArray=new Array();
                                                  var action="yes"
                                                  var dimUl=document.getElementById("sortable");
                                                  var dimIds=dimUl.getElementsByTagName("li");
                                                  for(var i=0;i<dimIds.length;i++){
                                                      var  colIdsvar=dimIds[i].id.replace("li-","");
                                                      tempArray.push(colIdsvar)
                                                  }
                                                  $.post(
                                                  'reportViewer.do?reportBy=reOrderCustomSeq&totalliurl='+tempArray+'&iscrosstabrep='+action+'&ReportId='+ReportId+'&colviewby='+colviewby,
                                                  function(data)
                                                  {

                                                      parent.$("#DefinecustSeqDialog").dialog('close')
                                                      var url = 'reportViewer.do?reportBy=viewReport&REPORTID='+ReportId+'&action=paramChange'
                                                      submiturls1(url);

                                                  });
                                              }
//added by Dinanath
function getFullCompNames(fullCompNames,totalComp,companyIds){
                                                  var htmlVar = "<table>";
                                                  var  compNameArray = new Array();
    var  companyIdsDup = new Array();
                                                  compNameArray = fullCompNames.toString().split(",");
    companyIdsDup = companyIds.toString().split(",");
                                                  for(var i=0;i<totalComp;i++){
                                                      var k= i+1;
        htmlVar += "&nbsp;<tr><td><input type='radio' name='selectedCompId'  id="+companyIdsDup[i]+" />"+k+". &nbsp;"+compNameArray[i]+"</td></tr>";
                                                  }
                                                  htmlVar += "</table>";
                                                  $("#moreCompTab").html(htmlVar);
                                                  $("#moreCompanyDiv").dialog('open');
                                              }
//added by Dinanath
function setSelectedCompanyId(){
    var companyId=$('input[type=radio][name=selectedCompId]:checked').attr('id');
    $.post('reportViewer.do?reportBy=setSelectedDefaultCompanyId&companyId='+companyId+"&reportId="+<%=reportId1%>,
    function(data){
        $("#moreCompanyDiv").dialog('close');
        alert(data);
        window.location.href=window.location.href;
    });
}

                                      function showRepPrevState(pbreportid){
                                                  var url1= "";
                                                  url1 = 'reportViewer.do?reportBy=viewReport&REPORTID='+pbreportid+'&action=paramChange'+'&repPrevStateCnt='+<%=container.prevStateCnt%>+'&ShowPrevState=true';
                                                  submiturls(url1);

                                              }
                                              function displayFunction(data1,displ){
                                                  $("#grandTotalDiv").html("");
                                                  document.getElementById("grandTotalDiv").style.display=displ;
                                                  $("#grandTotalDiv").html(data1);
                                              }

function settodayDate(date){
    settodayDate1(date)
}
                                              // added by Manik
                                              function toggleGraphandTable(){
                                                  var strDiv1Cont = $("#tabGraphs").html();
                                                  var strDiv2Cont = $("#tabTable").html();

                                                  $("#tabGraphs").html(strDiv2Cont);
                                                  $("#tabTable").html(strDiv1Cont);

                                              }
                                              function tabparamListDispTop(){
                                                  var paramValq=$("#tabParametersTop").attr('title');
                                                  var widt=($(window).width())-(($(window).width())/10);
                                                  var rght=(($(window).width())/20);


                                                  if(paramValq=='show_param'){
                                                      var viewbys=$("#numbuerOfViewbys").val();
                                                      var divHeight=(3/5)*($(window).height());
                                                      $("#tabParametersTop").css('height', divHeight);
                                                      var h2=$("#headerTableData").height();
                                                      var heit=(divHeight-h2);

                                                      $(".dynamicClass").css('height', heit);
                                                      $(".dynamicClass").css('width', widt);
                                                      $(".dynamicClass").css('overflow', 'auto');
                                                      $("#tabParametersTop").animate({
                                                          right:rght,

                                                          height:divHeight,
                                                          width:widt
                                                      });
                                                      $("#headerDiv").css('display', 'block');
                                                      $("#paramRegion").css('display', 'block');
                                                      $('#tabParametersTop').attr('title', 'hide_param');
                                                      $('#tabParametersTop').css('display', 'block');
                                                  }else{
                                                      $("#tabParametersTop").animate({

                                                          height:'0px',
                                                          width:'0px'
                                                      });
                                                      $("#headerDiv").css('display', 'none');
                                                      $("#paramRegion").css('display', 'none');

                                                      $('#tabParametersTop').attr('title', 'show_param');

                                                  }
                                              }

var istoggledate="";

                                               <%if((container.datetoggl).equalsIgnoreCase("")){%>
                       istoggledate="No";
			<%}else{%>
			  istoggledate=<%=container.datetoggl%>;
			<%}%>
// added by krishan pratap
                                             function toggledate(){

						<% if (timeinfo.get(1).equalsIgnoreCase("PRG_DATE_RANGE") && sytm.equalsIgnoreCase("No")) {%>

							 if(  ($('#dateid').css('display')=='none') ) {
										   incriment++;
												  istoggledate='No';
									 $("#dateid").css('display', '');			 // alert("kk"+incriment+"istoggledate"+istoggledate)
								         $("#dateid1").css('display', '');
								         $("#comparefromtime").css('display', '');
									 $("#dateid22").css('display', '');

													    $("#dateid5").css('display', '');
									 $("#comparetoTime").css('display', '');
									 $("#dateid23").css('display', '');
									 //$("#dateit23").css('display', '');
                                                                    }
								else {


									 incriment++;
								         istoggledate='Yes';
												// alert("else"+incriment+"istoggledate"+istoggledate)
                                                                          $("#dateid").css('display', 'none');
									  $("#dateid1").css('display', 'none');
									  $("#comparefromtime").css('display', 'none');
									 $("#dateid22").css('display', 'none');
                                                  $("#dateid5").css('display', 'none');
										$("#comparetoTime").css('display', 'none');
										$("#dateid23").css('display', 'none');
										//$("#dateit23").css('display', 'none');




											  }

															 <%} else if (timeinfo.get(1).equalsIgnoreCase("PRG_STD") && sytm.equalsIgnoreCase("No")) {%>
															 if($('#column2').css('display')=='none'){

															    incriment++;
																istoggledate='No';
															//  alert("else if"+incriment+"istoggledate"+istoggledate)

															  $("#column2").css('display', '');
															
												
															 }else{
															 
												  incriment++;
												   istoggledate='Yes';
												// alert("kk else if else"+incriment+"istoggledate"+istoggledate)

												  $("#column2").css('display', 'none');
															 }

															 <%}%>

                                                 // istoggledate='Yes';
                                                 // $('#column2').toggle();




												  
												  

                                                                                              }

        </script>
        <style type="text/css">

            fieldset { border:0; margin: 6em; height: 12em;}
            label {font-weight: normal; float: left; margin-right: .5em; font-size: 1.1em;}
            select {margin-right: 1em; float: left;}
            .ui-slider {clear: both;}
            .background{
                fill:white;
            }
        </style>
        <style type="text/css">
            .mycls {
                background-color:#FFFFFF;
                border:0px solid #d7faff;
                height:180px;
                overflow:auto;
                width:180px;
            }

            .suggestLink {
                position: relative;
                background-color: #FFFFFF;
                border: 0px solid #000000;
                border-top-width: 0px;
                padding: 2px 6px 2px 6px;
                left:3px;
                min-width: 20px;
                max-width: 150px;
            }
            .innerDiv{
                overflow:auto;
                height:95%;
                width:95%;
            }

            .suggestLinkOver {
                background-color: #0099CC;
                padding: 2px 6px 2px 6px;
            }
            #cboRegionsuggestList {
                position: absolute;
                background-color: #FFFFFF;
                text-align: left;
                border: 1px solid #000000;
                border-top-width: 0px;
                width: 160px;
            }
            .imageStyle{
                position: absolute;
                width:12px;
                height:16px;
                display:inline;
            }
            .ajaxboxstyle {
                position: relative;
                background-color: #FFFFFF;
                text-align: left;
                border: 1px solid #000000;
                border-top-width:1px;
                height:150px;
                width:230px;
                overflow:auto;
                overflow:hidden;
                margin:0em 0.5em;
                z-index: 9999999;
            }
            .myAjaxTable {
                table-layout:fixed;
                background-color: #FFFFFF;
                text-align:left;
                border: 0px solid #000000;
                font-size:10px;
                left:4px;
                height:auto;
                border-collapse:separate;
                border-spacing:5px;
            }
            .More:hover{color:red;text-decoration:none}
            .myAjaxTable td {
                min-width:30px;
                max-width:100px;
                text-align:left;
            }
            #wrapper { display: inline;}
            #cbostate { width: 160px; }
            #cboRegion { width: 160px; }
            .black_overlay{
                display: none;
                position: absolute;
                top: 0%;
                left: 0%;
                width: 100%;
                height: 200%;
                background-color: black;
                z-index:1001;
                -moz-opacity: 0.5;
                opacity:.50;
                filter:alpha(opacity=50);
                overflow:auto;
            }
            .black_start{
                display: none;
                position: absolute;
                top: 0%;
                left: 0%;
                width: 100%;
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
                top: 25%;
                left: 25%;
                width: 25%;
                padding: 16px;
                border: 16px solid #308dbb;
                background-color: white;
                z-index:1002;
                overflow: auto;
                -moz-border-radius-bottomleft:5px;
                -moz-border-radius-bottomright:5px;
                -moz-border-radius-topleft:5px;
                -moz-border-radius-topright:5px;
            }
            white_contentcolor{
                display: none;
                position: absolute;
                top: 25%;
                left: 25%;
                width: 25%;
                padding: 16px;
                border: 16px solid #308dbb;
                background-color: white;
                z-index:1002;
                overflow: auto;
                -moz-border-radius-bottomleft:5px;
                -moz-border-radius-bottomright:5px;
                -moz-border-radius-topleft:5px;
                -moz-border-radius-topright:5px;
            }
            .white_content1 {
                position: absolute;
                top: 50px;
                left: 25%;
                width: 700px;
                height:400px;
                padding: 16px;
                border: 16px solid silver;
                background-color: white;
                z-index:1002;
                -moz-border-radius-bottomleft:5px;
                -moz-border-radius-bottomright:5px;
                -moz-border-radius-topleft:5px;
                -moz-border-radius-topright:5px;
            }
            .white_content2 {
                position: absolute;
                top: 30%;
                left: 38%;
                width: 500px;
                height:300px;
                padding: 16px;
                border: 16px solid silver;
                background-color: white;
                z-index:1002;
                -moz-border-radius-bottomleft:4px;
                -moz-border-radius-bottomright:4px;
                -moz-border-radius-topleft:4px;
                -moz-border-radius-topright:4px;
            }
            .ParamRegion{
                background-color:#e6e6e6;
            }
            .inputbox
            {
                font-size: 10px;
                background-color:#fff;
                border-right:#000000 1px outset;
                border-right:#000000 1px inset;
                border-top: white 1px inset;
                border-left: white 1px inset;
                border-bottom: #000000 1px inset;
                font-family: verdana, arial;
                -moz-border-radius-bottomleft:2px;
                -moz-border-radius-bottomright:2px;
                -moz-border-radius-topleft:2px;
                -moz-border-radius-topright:2px;
            }
            .test
            {
                width : 150px;
                height : 150px;
                border : 1px solid #ffff99;
                background-color: #ffff99;
            }
            .paramTable
            {
                width : 99%;
                height : auto;
            }
            .colorButton
            {
                width:20px;
                height:20px;
                background-color:green;
            }
            a {font-family:Verdana;cursor:pointer;font-size:<%=anchorFont%>px;}
            *{font: <%=pageFont%>px verdana;}
            .leftcol {
                clear:left;
                float:left;
                width:100%;
            }

            .label{
                background-position:8px center;
                background-repeat:no-repeat;
                border:0 solid #252525;
                clear:both;
                height:auto;
                width:auto;
                cursor:pointer;
                display:block;
                margin-bottom:0;
                margin-right:0;
                padding:0 0.3em 0.3em 22px;
                font-family:verdana;
                font-size:100.01%;
            }
            .MeasuresULClass{
                -moz-border-radius-bottomleft:4px;
                -moz-border-radius-bottomright:4px;
                -moz-border-radius-topleft:4px;
                -moz-border-radius-topright:4px;
                background:#79C9EC url(../jQuery/images/ui-bg_glass_75_79c9ec_1x400.png) repeat-x scroll 50% 50%;
                border:1px solid #448DAE;
                color:#000;
                FONT-SIZE: 11px;
                FONT-FAMILY: Verdana;
                VERTICAL-ALIGN: middle;
                HEIGHT: 20px;
                WIDTH: auto;
                cursor:pointer;
            }
            .selectedMeasuresULClass{
                -moz-border-radius-bottomleft:4px;
                -moz-border-radius-bottomright:4px;
                -moz-border-radius-topleft:4px;
                -moz-border-radius-topright:4px;
                background:#79C9EC url(../jQuery/images/ui-bg_glass_75_79c9ec_1x400.png) repeat-x scroll 50% 50%;
                border:1px solid #448DAE;
                color:#000;
                FONT-SIZE: 11px;
                FONT-FAMILY: Verdana;
                VERTICAL-ALIGN: middle;
                HEIGHT: 20px;
                WIDTH: auto;
                cursor:pointer;
            }

            .TopButtons0{

                -moz-border-radius:3px 3px 3px 3px;
                background-color: darkseagreen;
                color:#FFFFFF;
                float:left;
                font-family:helvetica,arial,sans-serif;
                font-size:13px;
                font-weight:700;
                margin-left: 10px;
                padding:10px 25px;
                text-align:center;
                text-decoration:none;
                text-shadow:none;
                -moz-border-radius:3px;
                -webkit-border-radius:3px;
                -webkit-transition:all 0.1s ease-in-out;
                width:70px;

            }

            .TopButtons0:hover {
                text-shadow:none;
                text-align:center;
                font-family:helvetica, arial, sans-serif;
                color:#FFFFFF;
                font-size:13px;
                margin-left: 10px;
                background:#006699;
                text-decoration:none;
                font-weight:700;
                -moz-border-radius:3px;
                -webkit-border-radius:3px;
                -webkit-transition:all 0.1s ease-in-out;
            }
            .TopButtons1{

                -moz-border-radius:3px 3px 3px 3px;
                color:#FFFFFF;
                float:left;
                font-family:helvetica,arial,sans-serif;
                font-size:13px;
                font-weight:700;	padding:10px 25px;
                text-align:center;
                margin-left: 10px;
                text-decoration:none;
                text-shadow:none;
                -moz-border-radius:3px;
                -webkit-border-radius:3px;
                -webkit-transition:all 0.1s ease-in-out;
                width:70px;
                background-color: #006699;
            }
            .TopButtons1:hover {
                text-shadow:none;
                text-align:center;
                font-family:helvetica, arial, sans-serif;
                color:#FFFFFF;
                font-size:13px;
                margin-left: 10px;
                background:darkseagreen;
                text-decoration:none;
                font-weight:700;
                -moz-border-radius:3px;
                -webkit-border-radius:3px;
                -webkit-transition:all 0.1s ease-in-out;
            }
            .TopButtonsNames {

                -moz-border-radius:3px 3px 3px 3px;
                color:black;
                float:left;
                font-family:helvetica,arial,sans-serif;
                font-size:13px;
                font-weight:700;	padding:0px 15px;
                text-align:center;
                text-decoration:none;
                text-shadow:none;
                -moz-border-radius:3px;
                -webkit-border-radius:3px;
                -webkit-transition:all 0.1s ease-in-out;
                width:100px;
                background-color: #ffffff;
            }
        </style>
        <title><bean:message key="ProGen.Title"/></title>
    </head>
    <%
        String pgurl = "";
        String repid = "";
        PbReturnObject folderpbro = new PbReturnObject();

        ProgenUser user = (ProgenUser) request.getSession(false).getAttribute("ProgenUser");
        if (user.isDuplicateSession(Integer.parseInt((String) session.getAttribute("USERID")))) {
            out.print("<input type=\"hidden\" id=\"warnUser\" value=\"true\">");
        }

        if (request.getAttribute("url") != null) {
            pgurl = request.getAttribute("url").toString();
        }
        String loguserId = String.valueOf(session.getAttribute("USERID"));
        String tempSql;
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
            tempSql = "select PMB_FROM, PMB_TO, PMB_SUBJECT, isnull(PMB_MESSAGE,PMB_SUBJECT) PMB_MESSAGE, PMB_ID, USERID from prg_message_board where USERID=" + loguserId;
        } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
            tempSql = "select PMB_FROM, PMB_TO, PMB_SUBJECT, ifnull(PMB_MESSAGE,PMB_SUBJECT) PMB_MESSAGE, PMB_ID, USERID from prg_message_board where USERID=" + loguserId;
        } else {
            tempSql = "select PMB_FROM, PMB_TO, PMB_SUBJECT, nvl(PMB_MESSAGE,PMB_SUBJECT), PMB_ID, USERID from prg_message_board where USERID=" + loguserId;
        }
        PbReturnObject pbrors = pbdb.execSelectSQL(tempSql);

        try {
            String userId = "";
            userId = String.valueOf(request.getSession(false).getAttribute("USERID"));
            String AvailableFiolers = "select folder_Id,FOLDER_NAME from PRG_USER_FOLDER where folder_id in(";
            AvailableFiolers += "select user_folder_id from prg_grp_user_folder_assignment where user_id=" + userId;
            AvailableFiolers += " union ";
            AvailableFiolers += "select folder_id from prg_user_folder where grp_id in(";
            AvailableFiolers += "select grp_id from prg_grp_user_assignment where user_id=" + userId;
            AvailableFiolers += " and grp_id not in(select grp_id from prg_grp_user_folder_assignment where user_id=" + userId + ")))";

            folderpbro = pbdb.execSelectSQL(AvailableFiolers);
            // String userreports = "SELECT REPORT_ID, REPORT_NAME FROM PRG_AR_REPORT_MASTER where REPORT_ID in(SELECT  REPORT_ID FROM PRG_AR_USER_REPORTS where  user_id=" + userId + ")";
            String userreports = "SELECT A.REPORT_ID,A.REPORT_NAME FROM PRG_AR_REPORT_MASTER A,(SELECT distinct REPORT_ID FROM PRG_AR_USER_REPORTS WHERE user_id=" + userId + ") B WHERE A.REPORT_ID =b.REPORT_ID";
            PbReturnObject reportpbro = pbdb.execSelectSQL(userreports);
            PbReturnObject rolereppbro = null;
            if (request.getParameter("roleId") != null) {
                String folderId = request.getParameter("roleId");
                String rolerepdashs = "SELECT REPORT_ID, REPORT_NAME FROM PRG_AR_REPORT_MASTER where REPORT_ID in(SELECT  REPORT_ID FROM PRG_AR_REPORT_DETAILS where  folder_id=" + folderId + ")";
                rolereppbro = pbdb.execSelectSQL(rolerepdashs);
            }

            for (int grp = 0; grp < grfobj.getRowCount(); grp++) {
                if (grfobj.getFieldValueInt(grp, "GRAPH_SIZE") == 3) {
                    grphFrmHeight = "440";
                } else {
                    if (container.getFrameHgt1() != null) {
                        grphFrmHeight = "500";
                    }
                }
            }
            if (container.getWhatIfScenario() != null) {
                WhatIfScenario whatIfScenario = container.getWhatIfScenario();
            }
            String sqlString = null;
            sqlString = container.getSqlStr();
            sqlString = sqlString.replace("\"", "").replace("'", "~");
            String reportquery = null;
            reportquery = container.getReportQuery();
            reportquery = reportquery.replace("\"", "").replace("'", "~");
    %>


    <body id="mainBody" onload='dbconremoveimage()' style="background-color:#fff">
        <iframe name="widgetframe" id="widgetframe" style="display:none" src="about:blank"></iframe>

        <%
            brdcrmb.inserting(reportName, pgurl);
        %>
        <div id="light" align="center"  class="white_content"><img  alt="Page is Loading" src='images/ajax.gif'></div>

        <div id="loadImg" align="center"  style="display:none;"><img align="middle"  width="75px" height="75px"  style="position:absolute" alt="Page is Loading" src='images/ajax.gif'></div>
        <table style="height:600px;max-height:100%;width:100%;"  cellpadding="0" cellspacing="0" >
            <tr style="height:40px;width:100%;max-height:100%; display: '' " >
                <td>
                    <table style="width:100%;">
                        <tr>
                            <td valign="top" style="width:50%;">
                                <%if (piVersion == "piVersion2014") {%>
                                <jsp:include page="Headerfolder/headerPage.jsp"/>
                                <jsp:include page="reportPage.jsp"/>
                                <% } else {%>
                                <jsp:include page="Headerfolder/footerPage.jsp"/>
                                <% }%>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
            <tr style="width:90%;max-height:100%"><td align="right">
<!--                    <table id="companySection" align="left" width="90%" class="ui-corner-all" style="text-align: left">
                        <tbody><tr>

                                <%if (container.srtUpCharVal.toUpperCase().equalsIgnoreCase("YES")) {%>
                                <td align="left" styel="width:95%;margin-right:20px;">
                                    <font color="black">Company:&nbsp;&nbsp;<%=container.companyName%>&nbsp;&nbsp;<a onclick="getFullCompNames('<%=container.orgCompNames%>','<%=container.totalComp%>','<%=container.companyId%>')">More..</a></font>
                                </td>
                                <% }%>

                            </tr></tbody>
                    </table>-->
                </td></tr>
            <tr style="height:15px;width:100%;max-height:100%; display: none">
                <td>
                    <table width="100%" class="ui-corner-all">
                        <tr>

                            <% int fntsize = anchorFont + 1;
                                String width = "";
                            %>

                            <%if (reportTitleAlign.equalsIgnoreCase("center")) {%>
                            <td width="30%"></td>
                            <td style="white-space:nowrap ;" valign="top" align=<%=reportTitleAlign%> width="40%">
                                <%} else {%>
                            <td style="white-space:nowrap ;" valign="top" align=<%=reportTitleAlign%> width="70%">
                                <%}%>



<!--                                 <span id="reportName" style="color: #4F4F4F;font-family:verdana;font-size:<%=reportTitleSize%>px;font-weight:bold"  title="<%=reportDesc%>"><%=reportName%></span>-->
                                <br/>

                            </td>
                            <%
                                com.progen.reportview.action.showReportName repname = new com.progen.reportview.action.showReportName();
                                ArrayList repNameList = repname.buildReportName(reportName);
                                fntsize = anchorFont + 1;
                                for (int i = 0; i < repNameList.size(); i++) {
                            %>

                            <%}%>

                            <td valign="top" style="height:10px;width:1%" align="right">
                                <%if (startFlag != null) {%>
                                <a href="javascript:void(0)" onclick="javascript:goThumbnail('<%=loguserId%>','<%=templId%>')" style="font-size:10px;font-weight:bold;text-decoration: none;font-family:Georgia"> Done </a> |
                                <a href="javascript:void(0)" onclick="javascript:goCancel()" style="font-size:10px;font-weight:bold;text-decoration: none;font-family:Georgia"> Cancel </a> |
                                <%}%>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
            <tr id="menuBarUITR" style="width:100%;height:544px;max-height:100%">
                <td>
                    <table width="100%" id="menuBarUI" class="ui-corner-all" style="height:100%; width: 100%;border-top: 1px solid rgb(244, 244, 244); "  cellpadding="0" cellspacing="0">
                        <tr class="ui-corner-all">

                            <td id="leftTd" valign="top" style="width:10%;display:none;border:0px" align="top" class="ui-corner-all">
                                <table style="width:210px;" class="ui-corner-all" >
                                    <tr>
                                        <td>
                                            <div class="navsection">
                                                <% if (userdao.getFeatureEnable("Favorite Links") || userType.equalsIgnoreCase("SUPERADMIN")) {%>
                                                <div class="navtitle" id="displayfavlink" onclick="displayfavlink()" title="<bean:message bundle="Tooltips" key="reportViewer.Favlink"/>">&nbsp;<b style="font-family:verdana;font-weight:bold">Favourite Links</b>
                                                    <span id="favlinkImg" class='ui-icon ui-icon-circle-triangle-s'   style='float:right;'></span>
                                                </div><% }%>

                                            </div>
                                        </td>
                                    </tr>

                                    <tr>
                                        <td  style="width:100%">

                                            <div class="navsection" style="width:100%">
                                            </div>

                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <% if (userdao.getFeatureEnable("Scheduled Reports") || userType.equalsIgnoreCase("SUPERADMIN")) {%>
                                            <div class="navsection">
                                                <div class="navtitle" id="messageslink" onclick="dispScheduleReports()" title="Click here to View the schedule Reports" >&nbsp;<b style="font-family:verdana;font-weight:bold">Scheduled Reports</b>
                                                    <span id="SchedulerImg" class='ui-icon ui-icon-circle-triangle-s'   style='float:right;'></span>
                                                </div>
                                                <table id="schedule"  cellpadding="2"   cellspacing="0"  class="ui-corner-all" style='display:none;width:100%;overflow: auto;' height="180px">

                                                </table>
                                            </div><% }%>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <% if (userdao.getFeatureEnable("Snapshots") || userType.equalsIgnoreCase("SUPERADMIN")) {%>
                                            <div class="navsection">
                                                <div class="navtitle" id="snaplink" onclick="dispSnapShots()" title="<bean:message bundle="Tooltips" key="reportViewer.Snapshot"/>">&nbsp;<b style="font-family:verdana;font-weight:bold">Snapshot</b>
                                                    <span id="SnapShotsImg" class='ui-icon ui-icon-circle-triangle-s'    style='float:right;'></span>
                                                </div>
                                            </div>
                                            <% }%>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <% if (userdao.getFeatureEnable("Top/Bottom") || userType.equalsIgnoreCase("SUPERADMIN")) {%>
                                            <div class="navsection">
                                                <div class="navtitle" id="displayManageSticky" onclick="displayManageSticky()" title="<bean:message bundle="Tooltips" key="reportViewer.ManageStickyNotes"/>" >&nbsp;<b style="font-family:verdana;font-weight:bold">Manage Sticky Notes</b>
                                                    <span id="TopbottomImg" class='ui-icon ui-icon-circle-triangle-s'   style='float:right;'></span>
                                                </div>

                                            </div>
                                        </td>

                                    </tr>
                                    <%                                        }

                                    %>
                                    <tr>
                                        <td>
                                            <div class="navsection">
                                                <div class="navtitle" id="displayTopbottom" onclick="displayTopbottom()" title="<bean:message bundle="Tooltips" key="reportViewer.TopBottom"/>" >&nbsp;<b style="font-family:verdana;font-weight:bold">Top 5/ Bottom 5</b>
                                                    <span id="TopbottomImg" class='ui-icon ui-icon-circle-triangle-s'   style='float:right;'></span>
                                                </div>
                                            </div>
                                        </td>
                                    </tr>

                                    <tr>
                                        <td>
                                            <div class="navsection">
                                                <div class="navtitle" id="displayBusRoles" onclick="displayBusRoles()">&nbsp;<b style="font-family:verdana;font-weight:bold">Business Roles</b>
                                                    <span id="BusRolesImg" class='ui-icon ui-icon-circle-triangle-s'   style='float:right;'></span>
                                                </div>

                                            </div>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <div class="navsection" style="background-color:#e6e6e6">
                                                <div class="navtitle" id="drillDownAssist" title="<bean:message bundle="Tooltips" key="reportViewer.DrillAssis"/>">&nbsp;
                                                    <b style="font-family:verdana;font-weight:bold">Drill Down Viewer</b></div>
                                                    <%
                                                        if (container.getParameterDrillDisplay() != null) {%>
                                                    <%=container.getParameterDrillDisplay()%>
                                                    <%}
                                                    %>
                                            </div>
                                        </td>
                                    </tr>
                                    <%--added by swathi--%>
                                    <tr>
                                        <td>
                                            <div class="navsection" style="background-color:#e6e6e6">
                                                <div class="navtitle" id="advanceParamater" title="Advanced Parameter" onclick="advanceParamter('<%=PbReportId%>','<%=request.getContextPath()%>')">&nbsp;
                                                    <b style="font-family:verdana;font-weight:bold;">Advanced Parameter</b>
                                                </div>
                                            </div>
                                            </div>
                                        </td>
                                    </tr>
                                    <%//}
%>
                                    <tr>
                                        <td>
                                            <div class="navsection" style="background-color:#e6e6e6">
                                                <div class="navtitle" id="paramDrillDownAssist" title="<bean:message bundle="Tooltips" key="reportViewer.ParamDrill"/>">&nbsp;
                                                    <b style="font-family:verdana;font-weight:bold">Parameter Assistance</b></div>
                                                <div style="width:200px;overflow:auto">
                                                    <%
                                                        if (container.getReportParamDrillAssis() != null) {%>
                                                    <%=container.getReportParamDrillAssis()%>
                                                    <%}
                                                    %>
                                                </div>
                                            </div>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <div class="navsection">
                                                <div class="navtitle1" id="displayRepTimeInfo" onclick="displayRepTimeInfo('<%=PbReportId%>')" >&nbsp;<b style="font-family:verdana;font-weight:bold;">Report Time Info</b> </div>
                                                <table id="RepTimecont" cellpadding="2"   cellspacing="2" style="display:none" class="ui-corner-all" style="width:100%">
                                                    <tbody>
                                                        <tr style="width:100%">
                                                            <td id="RepTimeinfo">
<!--                                                           <iframe src="<%=request.getContextPath()%>/reportViewer.do?reportBy=getRepTimeSpanDisplay" scrolling="no" id="DbrdTime" frameborder="0" style="width:100%"></iframe>-->
                                                            </td>
                                                        </tr>
                                                    </tbody>
                                                </table>
                                            </div>
                                        </td>
                                    </tr>
                                    <%//}%>

                                </table>
                            </td>
                            <td id="leftParamSection" width="1%"  style="background-color:#e6e6e6;border:0px;cursor:pointer;  z-index:1001; display: none" class="ui-corner-all" valign="top" >

                                <a onclick="paramListDisp('<%=PbReportId%>')" href="javascript:void(0)" title="Actions"><img alt=""class="ui-icon ui-icon-newwin"/></a>
                                <br>

                                <a href="#javascript.void(0)" class="ui-icon ui-icon-circle-triangle-s" title="Hide/show date" onclick="autohide()"></a>
                                <br>
                                <a class="ui-icon ui-icon-arrowthick-2-ne-sw" title="Display Grand Total" onclick="displayGrandTotal('<%=request.getContextPath()%>','<%=PbReportId%>')"></a>
                                <br>
                                <% if (isGraphThere != null && isGraphThere.equalsIgnoreCase("Yes")) {%>
                                <a href="#javascript.void(0)" class='ui-icon  ui-icon-circle-plus' title="Hide/Show GraphRegion" onclick="parent.dispGraphs()"></a>
                                <%}%><br>
                                <div><img src="<%=request.getContextPath()%>/icons pinvoke/control-180.png" id="tdImage" title="Click Here For Previous state" onclick="showRepPrevState('<%=PbReportId%>')" /></div><br>

                                <% if (userdao.getFeatureEnable("Report Save") || userType.equalsIgnoreCase("SUPERADMIN")) {%> <a style="text-decoration: none;font-family: helvetica;font-size: 9pt;" class="ui-icon ui-icon-disk" title="Save As New" href="javascript:openNewReportDtls('<%=request.getContextPath()%>')"></a>
                                <% }%> <br>
                                <%if (userdao.getFeatureEnable("Assign Report to Users") || userType.equalsIgnoreCase("SUPERADMIN")) {%>
                                <a href="#javascript.void(0)"  onclick="saveNewRepGrpTab1('<%=request.getContextPath()%>','<%=USERID%>','<%=rolesid%>','<%=PbReportId%>','<%=reportName%>')" class="ui-icon ui-icon-person" title="Assign Report to Users"></a>
                                <% }%><br>

                                <br>
                                <div id="reportNames">
                                    <%=new PbReportViewerBD().prepareBrdCrmb(brdcrmb, reportName)%>
                                </div>

                                <br>
                                <a onclick="generateJsonData('<%=PbReportId%>')" href="javascript:void(0)" title="Actions">Chart</a>

                            </td>
                            <td id="reportTD1" valign="top" style="width:89%;" class="ui-corner-all">
                                <form name="frmParameter" action="reportViewer.jsp" id="reportForm" method="POST" >
                                    <input type="hidden" name="h" id="h" value="<%=request.getContextPath()%>">
                                    <input type="hidden" name="stickUrl" id="stickUrl">
                                    <input type="hidden" name="REPORTID" id="REPORTID" value="<%=PbReportId%>">
                                    <input type="hidden" name="REPORTNAME" id="REPORTNAME" value="">
                                    <input type="hidden" name="newReportName" id="newReportName" value="">
                                    <input type="hidden" name="REPORTDESC" id="REPORTDESC" value="">
                                    <input type="hidden" name="parameters" id="parameters" value="<%=parameters%>">
                                    <input type="hidden" name="TimeLevelstr" id="TimeLevelstr" value="<%=timeLevelStr%>">
                                    <input type="hidden" name="loadDialog" id="loadDialog" value="false">
                                    <input id="showSqlbox" type="hidden" value="<%=sqlString.replace("'", "").replace("~", "'")%>">
                                    <!--  added by sruthi to display full query                   -->
                                    <input id="showSqlboxquery" type="hidden" value="<%=sqlString.replace("'", "").replace("~", "'")%>">
                                    <input id="showSqlboxfull"type="hidden" value="<%=reportquery.replace("\"", "").replace("'", "~")%>">
                                    <!--  ended by sruthi                                  -->
                                    <input type="hidden" name="Name" id="Name" value="<%=reportName%>">
                                    <input type="hidden" name="Desc" id="Desc" value="<%=reportDesc%>">
                                    <input type="hidden" name="ReportLayout" id="ReportLayout" value="<%=ReportLayout%>">
<input height="100px" width="100px" type="hidden" id="perioddate" name="perioddate" onchange="" value="" onclick=""/>
<input height="100px" width="100px" type="hidden" id="fromdate" name="fromdate" onchange="" value="" onclick=""/>
<input height="100px" width="100px" type="hidden" id="todate" name="todate" onchange="" value="" onclick=""/>
<input height="100px" width="100px" type="hidden" id="comparefrom" name="comparefrom" onchange="" value="" onclick=""/>
<input height="100px" width="100px" type="hidden" id="compareto" name="compareto" onchange="" value="" onclick=""/>

                                    <table style="width:99%">

                                        <tr id="topDisplayParams" valign="top" class="DisplayParams" style="display: ''">
                                            <td valign="top" width="100%">
                                                <!--edited by manik-->
                                                <!--                                            <div class="navsection"  style="height:auto;width:100%">-->
                                                <div id="tableOption" class="navsectionDiv"  style="height: auto; width: 11%; position: absolute; margin-top: 5px;margin-left: 88%">



                                                    <table width="100%"><tr>
                                                            <td align="left" style="display: none;">  <%if (displayFiltersGlobal != null && !displayFiltersGlobal.equalsIgnoreCase("")) {%>
                                                                <%=displayFiltersGlobal%>
                                                                <%}%></td>

                                                            <!-- changed by sruthi for what if,group measure and view fact                                       -->
                                                            <td>
                                                                <!--added by shivam-->

                                                                <img class="iconImgs" src="<%=request.getContextPath()%>/images/icons/options.png" alt="More" onclick="moreListDispTop()"  title="Click Here For More Options"  style="margin-top: 4px; padding-left: 5px; cursor: pointer; float: right;"/>
                                                                <img class="iconImgs" src="<%=request.getContextPath()%>/images/icons/filter_icon.png" alt="Filters" onclick="tabparamListDispTop()"  title="Click Here For Filters" style="margin-top: 4px; padding-left: 5px; cursor: pointer; float: right;"/>
                                                                <img class="iconImgs" src="<%=request.getContextPath()%>/images/icons/downloads.png" alt="Downloads" title="Downloads Report" onclick="parent.showExports('<%= PbReportId%>','<%=request.getContextPath()%>')" style="margin-top: 4px; padding-left: 5px; cursor: pointer; float: right;">
                                                                <img class="iconImgs" src="<%=request.getContextPath()%>/images/icons/Smail.png" alt="Mail" title="Send Mail" onclick="openShareReportDiv()" style="margin-top: 4px; padding-left: 5px; cursor: pointer; float: right;">
                                                                <img class="iconImgs" src="<%=request.getContextPath()%>/images/icons/scheduler.png" alt="Scheduler" title="Scheduler" onclick="openScheduleReport()" style="margin-top: 4px; padding-left: 5px; cursor: pointer; float: right;">
<!--                                                                <img class="iconImgs" src="<%=request.getContextPath()%>/images/icons/toggle.png" alt="Toggle" title="Toggle Date" onclick="toggledate()" style="margin-top: 4px; padding-left: 5px; cursor: pointer; float: right;">-->
<!--                                                                <img class="iconImgs" src="<%=request.getContextPath()%>/images/chart1.png" width="16px" alt="Graphs" title="Add Graphs" onclick="addGraphInDesigner('<%=request.getContextPath()%>','<%=PbReportId%>')" style="margin-top: 4px; padding-left: 5px; cursor: pointer; float: right;">-->
                                                                <img class="iconImgs" src="<%=request.getContextPath()%>/images/drillupgrey.png" alt="kk" title="Drill Up" onclick="showRepPrevState1('<%=PbReportId%>')" style="margin-top: 2px; padding-left: 2px; cursor: pointer; float: right;">
<!--                                                            <td id="saveTabId" ><a style="text-decoration: none;font-family: helvetica;font-size: 9pt;" class="ui-icon ui-icon-disk" title="Global Save" href="javascript:toggledate()"></a></td>-->

                                                                <!--             <span onclick="//moreListDispTop()" title="Click Here For More Options" class="glyphicon glyphicon-list" aria-hidden="true" style="float: right; font-size: 13px; cursor: pointer;"></span>                         								
                                                                                             <span onclick="//tabparamListDispTop()" title="Click Here For Filters" class="glyphicon glyphicon-filter" aria-hidden="true" style="float: right; font-size: 14px; margin-right: 10px; cursor: pointer;"></span>                         
                                                                                             <span onclick="//openShareReportDiv()" title="Send Mail" class="glyphicon glyphicon-envelope" aria-hidden="true" style="float: right; font-size: 13px; margin-right: 10px; cursor: pointer;"></span>                         
                                                                                             <span onclick="//openScheduleReport()" title="Scheduler" class="glyphicon glyphicon-time" aria-hidden="true" style="float: right; font-size: 13px; margin-right: 10px; cursor: pointer;"></span>                         
                                                                                             <span onclick="parent.showExports('<%= PbReportId%>','<%=request.getContextPath()%>')" title="Downloads Report" class="glyphicon glyphicon-download-alt" aria-hidden="true" style="float: right; font-size: 13px; margin-right: 10px; cursor: pointer;"></span>                         -->

                                                                <% if (isGraphThere != null && !isGraphThere.equalsIgnoreCase("Yes")) {%>
                   <!--                                <a href="javascript:addGraphInDesigner('<%=request.getContextPath()%>','<%=PbReportId%>')"  title="Click Here For Quick Graphs"> <img alt="Graphs" width="16" height="16" src="<%=request.getContextPath()%>/images/chart1.png" style="float: right"/></a>
                                                   <a onclick="showTableGraphs()"  title="Show/Hide Graphs"> <b style="float:right;padding-right: .5%;">Show/Hide Graph</b></a>-->
                                                                <%}%>

                                                      

                                                        <div id="datalist" onmouseleave='hideme(id)' style="display:none;position:absolute;width:9%;height:5%;margin-top: 15%;float: right;">
                                                            <table style="border: thin solid black; background-color:#fff;">
                                                                <tr>
                                                                    <!-- //  added by shivam(24/8/15)-->

                                                                    <td>  <a id="whatifSc" href="javascript:openWhatIfDiloge('<%=request.getContextPath()%>','<%=container.getReportId()%>')"  title="Perform What If" ><font class="gFontFamily gFontSize12" color="gray">What If Analysis</font></a></td>
                                                                </tr>
                                                                <tr>
                                                                    <td> <a href="javascript:openGroupMeasureList('<%=request.getContextPath()%>','<%=container.getReportId()%>')"  title="Group Measures"><font class="gFontFamily gFontSize12" color="gray">Group Measures</font></a></td>
                                                                </tr>
                                                                <tr>
                                                                    <td><a href="javascript:factFormulaDiv()"><font class="gFontFamily gFontSize12" color="gray" >View Fact Formula</font></a></td>
                                                                </tr>
                                                                <tr>
                                                                    <td><a onclick="paramListDisp('<%=PbReportId%>')" href="javascript:void(0)"><font class="gFontFamily gFontSize12" style="white-space: nowrap" color="gray" >View Favourite Link</font></a></td>
                                                                </tr>
                                                                <tr>
                                                                    <td><a onclick="callReportLevelGraph('<%=PbReportId%>')" href="javascript:void(0)"><font class="gFontFamily gFontSize12" style="white-space: nowrap" color="gray" >Graph on Report</font></a></td>
                                                                </tr>
                                                                  <tr>
                                                                       <td><a onclick="importExcelFile1('<%=request.getContextPath()%>','<%=container.getReportId()%>')" href="javascript:void(0)"><font class="gFontFamily gFontSize12" style="white-space: nowrap" color="gray" >Upload Template Excel</font></a></td>
                                                                 
                                                                 </tr>
<tr>
                                                                       <td><a onclick="javascript:showSqlQuery('<%=sqlString%>')" href="javascript:void(0)"><font class="gFontFamily gFontSize12" style="white-space: nowrap" size="2" >Display SQL Query</font></a></td>

                                                                 </tr>
<tr>
<!--                                                                       <td><a onclick="hideMeasures('<%=request.getContextPath()%>','<%=container.getReportId()%>')" href="javascript:void(0)"><font style="white-space: nowrap" size="2" >Show/Hide Measure</font></a></td>-->

                                                                 </tr>
<tr>
                                                                       <td><a onclick="saveReportTagName()" href="javascript:void(0)"><font class="gFontFamily gFontSize12" style="white-space: nowrap" color="gray" >Tag Report</font></a></td>

                                                                 </tr>
                                                                 </tr>
<tr>
                                                                       <td><a onclick="reportBugMail23('<%=request.getContextPath()%>','<%=container.getReportId()%>','<%=container.getReportId()%>')" href="javascript:void(0)"><font class="gFontFamily gFontSize12" style="white-space: nowrap" color="gray">Report Bug</font></a></td>

                                                                 </tr>
                                                        <tr>
                                                                       <td><a onclick="reportmulticalendar('<%=request.getContextPath()%>','<%=container.getReportId()%>')" href="javascript:void(0)"><font style="white-space: nowrap" size="2" >Report Multi Calendar </font></a></td>

                                                                 </tr>
                                                            </table>
                                                        </div>

                                                        </td>

                                                        </tr>
                                                    </table>
                                                </div>

                                                        </td>

                                        </tr>
                                                    </table>
                                                </div>
                                        </tr>


                                        <tr valign="top" class="ParamRegion">
                                            <td valign="top" width="100%">

                                                <div class="navsection"  style="height:auto;width:100%;">
                                                    <div id="fixedtop1" style=";">
                                                        <% if (timeinfo.get(1).equalsIgnoreCase("PRG_DATE_RANGE")) {%>
                                                        <div id="center250b">
                                                            <%} else {%>
                                                            <div id="center250a">
                                                                <%}%>
                                                                <div class="form clearFix">
                                                                    <span class="wr100">
                                                                        <table align="center" id="roundtrip">
                                                                            <tr width="100%"> 
                                                                                <% if (timeinfo.get(1).equalsIgnoreCase("PRG_DATE_RANGE") && sytm.equalsIgnoreCase("Yes") && collect.timeDetailsMap != null && !collect.timeDetailsMap.isEmpty()) {%>
                                                                                <td><table><tr style="display: block" id="toggleYear1" title="Toggle Year">
                                                                                            <td  align="right"  id="datetime"  tabindex="6">
                                                                                            <td style=""> From Year:&nbsp;<select id="calFromYear" name="calFromYear" style="width:90px" onchange="saveYearDetails('fromdate')">
                                                                                                    <%
                                                                                                        String fromYear = (container.fullName0).substring(4);
                                                                                                        for (int year = 2005; year <= 2020; year++) {
                                                                                                            //added by mayank
                                                                                                            int y = year + 1;
                                                                                                            String years = Integer.toString(y);
                                                                                                            String yearSec = years.substring(years.length() / 2);

                                 if (fromYear.equalsIgnoreCase(Integer.toString(year))) {%>
                                                                                                    <option selected value="<%=year%>"> <%=year%>  - <%=yearSec%>  </option>
                                                                                                    <%} else {%>
                                                                                                    <option value="<%=year%>"> <%=year%> - <%=yearSec%> </option>
                                                                                                    <%}
                             }%>
                                                                                                </select></td>
                                                                                            <td style="">To Year:&nbsp;<select id="calToYear" name="calToYear" style="width:90px" onchange="saveYearDetails('todate')">
                                                                                                    <%
                                                                                                        String toYear = (container.fullName).substring(4);
                                                                                                        for (int year = 2005; year <= 2020; year++) {
                                                                                                            //added by mayank
                                                                                                            int y = year + 1;
                                                                                                            String years = Integer.toString(y);
                                                                                                            String yearSec = years.substring(years.length() / 2);
                                 if (toYear.equalsIgnoreCase(Integer.toString(year))) {%>
                                                                                                    <option selected value="<%=year%>"> <%=year%> - <%=yearSec%> </option>
                                                                                                    <%} else {%>
                                                                                                    <option value="<%=year%>"> <%=year%> - <%=yearSec%> </option>
                                                                                                    <%}
                             }%>
                                                                                                </select></td>
                                                                                                <%if (container.cfdate != null && container.ctdate != null) {%>

                                                                                            <td align="right" style="font-weight:bold ; width: auto;padding-left: 1.5em;"> COMPARE</td>
                                                                                            <td align="right" style="width: auto;padding-left: 1.5em"></td>
                                                                                            <td style=""> From Year:&nbsp;<select id="calCmpFromYear" name="calCmpFromYear" style="width:90px" onchange="saveYearDetails('comparefrom')">
                                                                                                    <%
                                                                                                        String fromCmpYear = (container.cffullName).substring(4);
                                                                                                        for (int year = 2005; year <= 2020; year++) {
                                                                                                            int y = year + 1;
                                                                                                            String years = Integer.toString(y);
                                                                                                            String yearSec = years.substring(2);
                                             if (fromCmpYear.equalsIgnoreCase(Integer.toString(year))) {%>
                                                                                                    <!--<option selected value="<%=year%>"> <%=year%> - <%=year + 1%> </option>-->
                                                                                                    <option selected value="<%=year%>"> <%=year%> - <%=yearSec%> </option>
                                                                                                    <%} else {%>
                                                                                                    <option value="<%=year%>"> <%=year%> - <%=yearSec%> </option>
                                                                                                    <%}
                                         }%>
                                                                                                </select></td>
                                                                                            <td style="">To Year:&nbsp;<select id="calCmpToYear" name="calCmpYear" style="width:90px" onchange="saveYearDetails('compareto')">
                                                                                                    <%
                                                                                                        String toCmpYear = (container.ctfullName).substring(4);
                                                                                                        
                                                                                                        for (int year = 2005; year <= 2020; year++) {
                                                                                                            int y = year + 1;
                                                                                                            String years = Integer.toString(y);
                                                                                                            String yearSec = years.substring(2);
                                             if (toCmpYear.equalsIgnoreCase(Integer.toString(year))) {%>

                                                                                                    <option selected value="<%=year%>"> <%=year%> - <%=yearSec%> </option>
                                                                                                    <%} else {%>
                                                                                                    <option value="<%=year%>"> <%=year%>  - <%=yearSec%> </option>
                                                                                                    <%}
                                         }%>
                                                                                                </select></td>
                                                                                                <%}%>
                                                                                        </tr></table></td>
                                                                                <td><input height="100px" width="100px" type="hidden" id="fromdate" name="fromdate" onchange="" onclick=""/></td>
                                                                                <td> <input  type="hidden" id="datetext" name="datetext" value=""/></td>
                                                                                <td><input height="100px" width="100px" type="hidden" id="todate" name="todate" onchange="" onclick=""/></td>
                                                                                <td> <input  type="hidden" id="datetext" name="datetext" value=""/></td>

                                                                                <td><input height="100px" width="100px" type="hidden" id="comparefrom" name="comparefrom" onchange="" onclick=""/></td>
                                                                                <td> <input  type="hidden" id="datetext" name="datetext" value=""/></td>
                                                                                <td><input height="100px" width="100px" type="hidden" id="compareto" name="compareto" onchange="" onclick=""/></td>
                                                                                <td> <input  type="hidden" id="datetext" name="datetext" value=""/></td>

                                                                                <%} else if (timeinfo.get(1).equalsIgnoreCase("PRG_STD") && sytm.equalsIgnoreCase("Yes") && collect.timeDetailsMap != null && !collect.timeDetailsMap.isEmpty()) {%>
                                                                                <td style="">Year:&nbsp;</td>
                                                                                <td align="right">
                                                                                    <select id="calPeriodYear" name="calPeriodYear" style="width:90px" onchange="saveYearDetails('perioddate')">
                                                                                        <% String calndrYear1 = (container.fullName0).substring(4);
                                                                                            for (int year = 2005; year <= 2020; year++) {
                                                                                                int y = year + 1;
                                                                                                String years = Integer.toString(y);
                                                                                                String yearSec = years.substring(2);
                      if (calndrYear1.equalsIgnoreCase(Integer.toString(year))) {%>
                                                                                        <option selected value="<%=year%>"> <%=year%> - <%=yearSec%> </option>
                                                                                        <%} else {%>
                                                                                        <option value="<%=year%>"> <%=year%> - <%=yearSec%>  </option>
                                                                                        <%}
                  }%>
                                                                                    </select>
                                                                                </td>
                                                                                <td><input height="100px" width="100px" type="hidden" id="perioddate" name="perioddate" onchange="" onclick=""/></td>
                                                                                <td> <input  type="hidden" id="datetext" name="datetext" value=""/></td>
                                                                                <td>
                                                                                    <%

                                                                                        DisplayParameters dur = new DisplayParameters();
                                                                                       // String duration1 = String.valueOf(dur.displayTime(collect.timeDetailsMap)); added by krishan
                                                                                         String duration1 = String.valueOf(dur.displayTpggleTime(collect.timeDetailsMap,comp,COMPARISON_DATE));

                                                                                    %>
                                                                                    <%=duration1%>
                                                                                </td>
                                                                                <% } else if (timeinfo.get(1).equalsIgnoreCase("PRG_DAY_ROLLING") && sytm.equalsIgnoreCase("Yes") && collect.timeDetailsMap != null && !collect.timeDetailsMap.isEmpty()) {%>
                                                                                <td align="right"  tabindex="6">
                                                                                    <span class="top w100 mrtop" id="pfield1"><%=(container.fullName0).substring(4)%></span>
                                                                                <td onmouseover="setdatetype('perioddate')"> <input height="100px" width="100px" type="hidden" class="ui-datepicker " id="perioddate" name="perioddate" onchange="dateclick()" onclick="showdate()"/><td>
                                                                                <td> <input  type="hidden" id="datetext" name="datetext" value=""/></td>
                                                                                <td>
                                                                                    <%

                                                                                        DisplayParameters dur = new DisplayParameters();
                                                                                      //  String duration1 = String.valueOf(dur.displayTime1(collect.timeDetailsMap)); // added by krishan
                                                                                         String duration1 = String.valueOf(dur.displayTpggleTime(collect.timeDetailsMap,comp,COMPARISON_DATE));

                                                                                    %>
                                                                                    <%=duration1%>
                                                                                </td>
                                                                                <% } else if (timeinfo.get(1).equalsIgnoreCase("PRG_DATE_RANGE") && sytm.equalsIgnoreCase("No") && collect.timeDetailsMap != null && !collect.timeDetailsMap.isEmpty()) {%>
                                                                                <td  white-space:nowrap ;width: auto"></td>
                                                                                <td  align="right"  id="datetime"  tabindex="6">
                                                                                    <span id="depShow">
                                                                                        <span class="top w100 mrtop" id="field1"><%=(container.fullName0).substring(0, 4) + (container.fullName0).substring(6)%></span>
                                                                                        <span class="date">
                                                                                            <small Style="font-weight: bold" id="field2"><%=container.dated%></small></span>
                                                                                        <span class="bottom w100" id="field3"><%=container.day0%></span></span></td>
                                                                                <td onmouseover="setdatetype('fromdate')"><input type="hidden" class="ui-datepicker" id="fromdate" name="fromdate" onchange="dateclick(this.id)" onclick="showdate()"/></td>
                                                                                <td><input  type="hidden" id="datetext" name="datetext" value=""/></td>

                                                                                <td align="left" style="white-space:nowrap ;width: auto;padding-left: 1.5em">To :</td>
                                                                                <td align="right" id="todatetime" tabindex="8"  style="padding-left: 0.5em">
                                                                                    <span id="retShow" style="position:relative;">
                                                                                        <span class="top w100 mrtop" id="tdfield1"><%=(container.fullName).substring(0, 4) + (container.fullName).substring(6)%></span>
                                                                                        <span class="date"><small class="init" Style="font-weight: bold" id="tdfield2"><%=container.date%></small></span>
                                                                                        <span class="bottom w100" id="tdfield3"><%=container.day%></span></span></td>
                                                                                <td onmouseover="setdatetype('todate')"><input  type="hidden" class="ui-datepicker " name="todate" id="todate" onchange="dateclick(this.id)" onclick="showdate()"/> </td>
                                                                                <td><input  type="hidden" id="datetext" name="datetext" value=""/></td>
                                                                                <% if(datetoggl.equalsIgnoreCase("Yes") ||COMPARISON_DATE.equalsIgnoreCase("Yes")|| comp.equalsIgnoreCase("Yes")){  // added by krishan Pratap
                                                                                    //out.println("@@datetogg"+datetoggl);
                                                                                    if (container.cfdate != null) {%>

                                                                                <td  id="dateid" align="right" style="font-weight:bold ; width: auto;padding-left: 1.5em;display:none;"> COMPARE </td>
                                                                                <td  id="dateid1"  align="right" style="width: auto;padding-left: 1.5em;display:none;"></td>
                                                                                <td  align="left" id="comparefromtime" tabindex="8"  style="padding-left: 0.5em;display:none;">
                                                                                    <span id="retShow" style="position:relative;">
                                                                                        <span class="top w100 mrtop" id="cffield1"><%=(container.cffullName).substring(0, 4) + (container.cffullName).substring(6)%></span>
                                                                                        <span class="date"><small class="init" Style="font-weight: bold" id="cffield2"><%=container.cfdate%></small></span>
                                                                                        <span class="bottom w100" id="cffield3"><%=container.cfday%></span></span></td>
                                                                                <td id="dateid22" style="display:none;" onmouseover="setdatetype('comparefrom')"><input type="hidden" class="ui-datepicker " id="comparefrom"  name="comparefrom"  onchange="dateclick(this.id)" onclick="showdate()"/></td>
                                                                                <td  ><input  type="hidden" id="datetext" name="datetext"  value=""/></td>
                                                                                    <% }%>
                                                                                    <% if (container.ctdate != null) {%>
                                                                                <td id="dateid5"  align="right" style="width: auto;padding-left: 1.5em;display:none;">To:</td>
                                                                                <td   align="left" id="comparetoTime" tabindex="8"  style="padding-left: 0.5em;display:none;"> <span  id="retShow" style="position:relative;">
                                                                                        <span class="top w100 mrtop" id="ctfield1"><%=(container.ctfullName).substring(0, 4) + (container.ctfullName).substring(6)%></span>
                                                                                        <span class="date"><small class="init" Style="font-weight: bold" id="ctfield2"><%=container.ctdate%></small></span>
                                                                                        <span class="bottom w100" id="ctfield3"><%=container.ctday%></span></span></td>
                                                                                <td id="dateid23" style="display:none;" onmouseover="setdatetype('compareto')"><input  type="hidden" class="ui-datepicker1 " id="compareto" name="compareto" onchange="dateclick(this.id)" onclick="showdate()"/></td>
                                                                                <td  ><input  type="hidden" id="datetext" name="datetext" style="display:none;" value=""/></td>
																				</div >
                                                                                    <%}
                                                                                    } else{//out.println("datetoggl12"+datetoggl);
                                                                                    %>

                                                                                     <% if (container.cfdate != null) {%>
                                                                                     
                                                                                <td id="dateid" align="right" style="font-weight:bold ; width: auto;padding-left: 1.5em;display:'';"> COMPARE </td>
                                                                                <td id="dateid1" align="right" style="width: auto;padding-left: 1.5em;display:'';"></td>
                                                                                <td   align="left" id="comparefromtime" tabindex="8"  style="padding-left: 0.5em;display:'';">
                                                                                    <span id="retShow" style="position:relative;">
                                                                                        <span class="top w100 mrtop" id="cffield1"><%=(container.cffullName).substring(0, 4) + (container.cffullName).substring(6)%></span>
                                                                                        <span class="date"><small class="init" Style="font-weight: bold" id="cffield2"><%=container.cfdate%></small></span>
                                                                                        <span class="bottom w100" id="cffield3"><%=container.cfday%></span></span></td>
                                                                                <td id="dateid22" onmouseover="setdatetype('comparefrom')"><input type="hidden" class="ui-datepicker " id="comparefrom" name="comparefrom" style="display:'';" onchange="dateclick(this.id)" onclick="showdate()"/></td>
                                                                                <td ><input  type="hidden" id="datetext" name="datetext" style="display:'';" value=""/></td>

                                                                                    <% }%>
                                                                                    <% if (container.ctdate != null) {%>

																				<td  id="dateid5"align="right" style="width: auto;padding-left: 1.5em;display:'';">To:</td>
                                                                                <td  align="left" id="comparetoTime" tabindex="8"  style="padding-left: 0.5em;"> <span  id="retShow" style="position:relative;">
                                                                                        <span class="top w100 mrtop" id="ctfield1"><%=(container.ctfullName).substring(0, 4) + (container.ctfullName).substring(6)%></span>
                                                                                        <span class="date"><small class="init" Style="font-weight: bold" id="ctfield2"><%=container.ctdate%></small></span>
                                                                                        <span class="bottom w100" id="ctfield3"><%=container.ctday%></span></span></td>
                                                                                <td id="dateid23" onmouseover="setdatetype('compareto')"><input  type="hidden" class="ui-datepicker1 " id="compareto" name="compareto" style="display:'';" onchange="dateclick(this.id)" onclick="showdate()"/></td>
                                                                                <td ><input  type="hidden" id="datetext" name="datetext" style="display:'';" value=""/></td>
                                                                                </div >

                                                                                <%}}%>

                                                                                    <%} else if (timeinfo.get(1).equalsIgnoreCase("PRG_STD") && sytm.equalsIgnoreCase("No") && collect.timeDetailsMap != null && !collect.timeDetailsMap.isEmpty()) {%>
                                                                                <td align="right">
                                                                                    <span class="top w100 mrtop" id="pfield1"><%=(container.fullName0).substring(0, 4) + (container.fullName0).substring(6)%></span>
                                                                                    <span class="date">
                                                                                        <small Style="font-weight: bold" id="pfield2"><%=container.dated%></small></span>
                                                                                    <span class="bottom w100" id="pfield3"><%=container.day0%></span></td>
                                                                                <td onmouseover="setdatetype('perioddate')"> <input height="100px" width="100px" type="hidden" class="ui-datepicker " id="perioddate"  name="perioddate" onchange="dateclick(this.id)" onclick="showdate()"/><td>
                                                                                <td> <input  type="hidden" id="datetext" name="datetext" value=""/></td>
                                                                                <td>
                                                                                    <%

                                                                                        DisplayParameters dur = new DisplayParameters();
                                                                                        String duration1 = String.valueOf(dur.displayTpggleTime(collect.timeDetailsMap,comp,COMPARISON_DATE));
                                                                                    %>
                                                                                    <%=duration1%>
                                                                                </td>
                                                                                <% } else if (timeinfo.get(1).equalsIgnoreCase("PRG_DAY_ROLLING") && sytm.equalsIgnoreCase("No") && collect.timeDetailsMap != null && !collect.timeDetailsMap.isEmpty()) {%>
                                                                                <td align="right"  tabindex="6">
                                                                                    <span class="top w100 mrtop" id="pfield1"><%=(container.fullName0).substring(0, 4) + (container.fullName0).substring(6)%></span>
                                                                                    <span class="date">
                                                                                        <small Style="font-weight: bold" id="pfield2"><%=container.dated%></small></span>
                                                                                    <span class="bottom w100" id="pfield3"><%=container.day0%></span></td>
                                                                                <td onmouseover="setdatetype('perioddate')"> <input height="100px" width="100px" type="hidden" class="ui-datepicker " id="perioddate" name="perioddate" onchange="dateclick()" onclick="showdate()"/><td>
                                                                                <td> <input  type="hidden" id="datetext" name="datetext" value=""/></td>
                                                                                <td>
                                                                                    <%

                                                                                        DisplayParameters dur = new DisplayParameters();
                                                                                        String duration1 = String.valueOf(dur.displayTime(collect.timeDetailsMap));

                                                                                    %>
                                                                                    <%=duration1%>
                                                                                </td>
                                                                                <% }%>

                                                                                <% if (userdao.getFeatureEnable("Report Save") || userType.equalsIgnoreCase("SUPERADMIN")) {%>
                                                                                <td id="saveTabId" ><a style="text-decoration: none;font-family: helvetica;font-size: 9pt;" class="ui-icon ui-icon-disk" title="Global Save" href="javascript:overWriteReport1('<%=request.getContextPath()%>','overWrite')"></a></td>
                                                                                <% }
               if (userdao.getFeatureEnable("Reset") || userType.equalsIgnoreCase("SUPERADMIN")) {%><td> <a onclick="openImgDiv()" href="<%=resetpath%>" style=""> Reset </a></td><td></td>
                                                                                <% }
                if (userdao.getFeatureEnable("Toggle") || userType.equalsIgnoreCase("SUPERADMIN")) {%><td id="dateToggle"><a href="javascript:void(0)" class="ui-icon ui-icon-transferthick-e-w" title="Toggle" onclick="datetoggl1('<%=userType%>','<%=isPowerAnalyserEnableforUser%>',istoggledate)"></a></td>

                                                                                <% }
                if (userdao.getFeatureEnable("Go Button") || userType.equalsIgnoreCase("SUPERADMIN")) {%><td id="visualGO" width="35"><input   type="button" class="navtitle-hover" value=" Go "  style="height: 20px; width: 25px" onclick="submitform()"/></td>
                                                                                    <% }
               if (userdao.getFeatureEnable("Hide Date Section") || userType.equalsIgnoreCase("SUPERADMIN")) {%>
                                                                                    <% }%>
                                                                            </tr>
                                                                        </table>
                                                                    </span>
                                                                </div>

                                                            </div>
                                                        </div>

                                                    </div></div>


                                                <div id="grandTotalDiv" style="display:none;width:100%;height:80px;background-color:white; text-align:center;border:1px solid #000000;z-index: 10000;border-color: LightGrey;" title="">
                                                </div>

                                                <div id="tabParametersTop" title="show_param" style="display:none;background-color:white;width:0px;height:0px; position:absolute;text-align:left;border: 1px solid LightGrey;top:122px;z-index: 1000;">

                                                    <%=ParamSectionDisplay%>
                                                </div>
                                                <% if (isGraphThere != null && !isGraphThere.equalsIgnoreCase("Yes")) {%>
                                        <tr>
                                            <td>

                                                <div id="tabGraphs1" style="background-color:#fff;display:none;">
                                                    <table  width="100%" style="height:auto;border:0px solid #369;">
                                                        <tr style="width:100%">
                                                            <td valign="top" width="100%">
                                                                <IFRAME NAME='iframe5' ID='iframe5'  SRC='about:blank' STYLE='width:100%;height:500px' frameborder="0"></IFRAME>
                                                            </td>
                                                        </tr>
                                                    </table>
                                                </div>
                                                <div id="tabGraphs" style="background-color:#fff;display:none;">
                                                    <table  width="100%" style="height:auto;border:0px solid #369;">
                                                        <tr style="width:100%">
                                                            <td valign="top" width="100%">
                                                                <IFRAME NAME='iframe5' ID='iframe4'  SRC='about:blank' STYLE='width:100%;height:400px' frameborder="0"></IFRAME>
                                                            </td>
                                                        </tr>
                                                    </table>
                                                </div>
                                            </td>
                                        </tr>
                                        <%}%>

                                        <%if (isGraphThere.equalsIgnoreCase("Yes")) {
                                        %>
                                        <tr valign="top">
                                            <td valign="top" width="100%">
                                                <div class="navsection" >

                                                    <div id="tabGraphs" style="background-color:#fff;display:<%=String.valueOf(session.getAttribute("GraphStatus"))%>;">
                                                        <table  width="100%" style="height:auto;border:0px solid #369;">
                                                            <tr style="width:100%">
                                                                <td valign="top" width="100%">
                                                                    <IFRAME NAME='iframe4' ID='iframe4'  SRC='about:blank' STYLE='width:100%;overflow:auto;height:467px' frameborder="0"></IFRAME>
                                                                    <%-- <IFRAME NAME='iframe4' ID='iframe4'  SRC='' STYLE='width:100%;overflow:hidden;height:450px'></IFRAME>--%>
                                                                </td>
                                                            </tr>
                                                        </table>
                                                    </div>
                                                </div>
                                            </td>
                                        </tr>
                                        <%

                                                if (session.getAttribute("GraphStatus").toString().equalsIgnoreCase("block")) {
                                                    session.setAttribute("tabFrmHeight", "750");
                                                }
                                            } else {
                                                session.setAttribute("tabFrmHeight", "750");
                                            }
                                            if (PrivilegeManager.isModuleEnabledForUser("MAP", USERID) && container.isMapEnabled()) {
                                        %>

                                        <tr valign="top">
                                            <td valign="top" width="100%">
                                                <div class="navsection" >
                                                    <div style="width:100%" onclick="displayMap()" title="<bean:message bundle="Tooltips" key="reportViewer.DispMap"/>">
                                                        <table width="100%" class="navtitle1">
                                                            <tr>
                                                                <td><b style="font-family:verdana;font-weight:bold">Map Region</b></td>
                                                                <td><span id="TextImg" class='ui-icon ui-icon-circle-triangle-s'   style='float:right;'></span></td>
                                                            </tr>
                                                        </table>
                                                    </div>
                                                    <%
                                                        String displaymode = "";
                                                        String geoViewDisplayMode = "";
                                                        String allowColorGroup = "none";


                                                        if (String.valueOf(session.getAttribute("TopBtmEnable")).equalsIgnoreCase("none")) {
                                                            displaymode = "none";
                                                            allowColorGroup = "";
                                                        }
                                                        if (String.valueOf(session.getAttribute("GeoViewSelection")).equalsIgnoreCase("none")) {
                                                            geoViewDisplayMode = "none";
                                                        }

                                                    %>
                                                    <div id="tabmap" style="display:<%=String.valueOf(session.getAttribute("Mapstatus"))%>">

                                                        <table width="100%"><tr>
                                                                <td align="left" width="10%">
                                                                    <a href="javascript:void(0);" style="text-decoration: none;" onclick="addMapMeasures('<%= request.getContextPath()%>','<%=PbReportId%>')">Add Measures</a>
                                                                </td>
                                                                <td width="10%"  align ="left" style="display: <%=displaymode%>">
                                                                    <select style="width: 130px;" class="myTextbox5" align="left" name="ViewSelect" id="ViewSelect" >
                                                                        <option value="overall">Over All</option>
                                                                        <option value="locationWise">Location</option>
                                                                    </select>
                                                                </td>
                                                                <td style="display:<%=displaymode%>" align="left" width="10%">
                                                                    <select style="width: 130px;" class="myTextbox5" align="left" name="sortValuesForMap" id="sortValuesForMap" >

                                                                        <option value="0">Top 3</option>
                                                                        <option value="1">Top 5</option>
                                                                        <option value="2">Bottom 3</option>
                                                                        <option value="3">Bottom 5</option>

                                                                    </select>
                                                                </td>
                                                                <td  style="display:<%=geoViewDisplayMode%>" align="left" width="10%">
                                                                    <select style="width: 130px;" class="myTextbox5" align="left" name="GeoViewForMap" id="GeoViewForMap" >
                                                                        <%for (int i = 0; i < geographyDimensions.size(); i++) {
                                                                        %>
                                                                        <option value="<%=geographyDimensions.get(i)%>"><%=geographyDimensionLabels.get(i)%></option>
                                                                        <%}%>

                                                                    </select>
                                                                </td>
                                                                <td style="display:<%=displaymode%>" align="left" ><input type="button" align="left" class="navtitle-hover" style="width:auto" value="Go" onclick="addSortValueToMap()"id="MapView" ></td>
                                                                <td style="display:<%=allowColorGroup%>" align ="left">
                                                                    <a href="javascript:void(0);" style="text-decoration: none;" name="setMapColorGrouping" id="setMapColorGrouping" onclick="setColourGroupForMap('<%=request.getContextPath()%>','<%=PbReportId%>')">Color Grouping</a>
                                                                </td>
                                                            </tr> </table>

                                                        <table  width="100%" style="height:auto;border:0px solid #369;">
                                                            <tr><td>  </td> </tr>
                                                            <tr>
                                                                <td  valign="top" >
                                                                    <div id="map_canvas" style="height:370px"></div>
                                                                </td>    </tr>      </table>  </div>  </div></td>
                                        </tr>
                                        <%}%>
                                        <tr valign="top">
                                            <td valign="top" width="100%">
                                                <div class="navsection" style="height:800px" >

                                                    <div id="tabTable" style="display:<%=String.valueOf(session.getAttribute("TableStatus"))%>">
                                                        <table   style="height:auto" width="100%">
                                                            <tr>
                                                                <td valign="top" width="100%">
                                                                    <jsp:include page="TableDisplay/pbTableMap.jsp">
                                                                        <jsp:param name="ReportId" value="<%=PbReportId%>"/>
                                                                        <jsp:param name="currentURL" value="<%=currentURL%>"/>
                                                                    </jsp:include>
                                                                </td>
                                                            </tr>
                                                        </table>
                                                    </div>
                                                </div>
                                            </td>
                                        </tr>
                                    </table>
                                </form>

                            </td>

                        </tr>
                    </table>
                    <div id="addUnderConsideration" style="display: none" title="">
                        <img style="margin-left:27%" src="images/under_construction.png">
                    </div>
                </td>
            </tr>
            <tr>
                <td>

                </td>
            </tr>
        </table>
        <iframe id="msgframe" height="380px" width="660px"src="pbTakeMailAddress1.jsp" style="display:none"></iframe>
        <iframe id="Scheduler" height="380px" width="660px" src="about:blank"   style="display:none"></iframe>
        <iframe id="cstLinksFrame" height="380px" width="660px" src="getAllReports.do" style="display:none"></iframe>
        <iframe id="prtLinksFrame" height="380px" width="660px" src="about:blank" style="display:none"></iframe>
        <div id="custreportdiv" class="white_content2"  align="justify" style="height:120px;width:350px;display:none">
            <center class="leftcol" style="height:auto">
                <br><br>
                <div id="leftcol" class='leftcol' style="width:100%" align="center" >
                    <table style="width:100%" border="0" >
                        <tr style="width:100%">
                            <td  style="width:40%;"><label class="label">Report Name</label></td>
                            <td  style="width:60%;"><input type="text" class="inputbox"  maxlength="35" name="reportName" style="width:80%" id="reportName" onkeyup="tabmsg()" onfocus="document.getElementById('save').disabled = false;"><br><span id="duplicate" style="color:red"></span></td>
                        </tr>
                        <tr>
                            <td  style="width:40%;"><label class="label">Description</label></td>
                            <td  style="width:60%;"><input type="text" class="inputbox"  name="reportDesc" maxlength="35" id="reportDesc" style="width:80%;overflow:auto;height:50px"></td>
                        </tr>
                    </table>
                    <table>
                        <tr>
                            <td><input type="button"  class="navtitle-hover" style="width:auto" value="Next" id="save" onclick="saveCustomizeReport('<%=PbReportId%>');"></td>
                            <td></td>
                            <td><input type="button"   class="navtitle-hover" style="width:auto" value="Cancel" onclick="cancelCustomizeReport();"></td>
                        </tr>
                    </table>
                </div>
            </center>
        </div>
        <div class="tooltip" id="my_tooltip" style="display: none"></div>
        <div id="favLinksDialog" title="Favourite Links" align="center" style="display:none">
            <iframe src="getAllReports.do" scrolling="no"  height="100%" width="100%" frameborder="0" id="favFrame"></iframe>
        </div>
        <div id="composeMessageDialog" title="Compose Message" style="display:none">
            <iframe src="pbTakeMailAddress1.jsp"   height="100%" width="100%" frameborder="0" scrolling="no"></iframe>
        </div>
        <div id="replyMessageDialog" title="" style="display:none">
            <iframe id="replyMessageFrame" width="100%" height="100%" frameborder="0" src="about:blank"></iframe>
        </div>
        <div id="snapShotDialog" title="Snap Shots" style="display:none">
            <iframe src="PersonalisedReports/JSPS/pbPersonalisedReportRegister.jsp?ReportId=<%=PbReportId%>"  frameborder="0" width="100%" height="100%"></iframe>
        </div>
        <div id="snapshotHeadlineDiv" title="Static Headlines" style="display:none">
            <Table>

                <Tr><Td >Headline Name

                        <textarea cols="" rows=""   name="headlinename" id="headlinename" style="width:350px;height:80px "></textarea>
                    </Td></Tr>

            </Table>
            <Table align="center">
                <Tr>
                    <Td><Input class="navtitle-hover" type="button" value="Save" onclick="saveHeadlines('<%=request.getContextPath()%>','<%=PbReportId%>','<%=USERID%>',<%=rolesid%>)"></Td>
                    <Td><Input class="navtitle-hover" type="reset" value="Clear" style="display:none">
                        <input type="hidden" name="UrlVal">
                    </Td>
                </Tr>
            </Table>
        </div>
        <br/>
        <div id="DynamicHeadlineDiv" title="Dynamic Headlines" style="display:none">
            <Table>
                <Tr><Td>Headline Name
                        <textarea cols="" rows=""   name="dynamicheadlinename" id="dynamicheadlinename" style="width:350px;height:80px "></textarea>
                    </Td></Tr><br/>
                <Tr> <Td align="center"><Input class="navtitle-hover" type="button" value="Save" onclick="saveDynamicHeadlines('<%=request.getContextPath()%>','<%=PbReportId%>','<%=USERID%>',<%=rolesid%>)"></Td></Tr>
            </Table>
        </div>
        <table style="width:100%">
            <tr>
                <td valign="top" style="width:100%;">
                    <jsp:include page="Headerfolder/footerPage.jsp"/>
                </td>
            </tr>
        </table>
        <div id="fade" class="black_overlay"></div>
        <div id="busRoleDiv"  title="Business Roles" style="display:none">
            <div class="innerDiv" style="height:270px;width:96%;overflow:auto">
                <table align="center" width="99%" style="overflow:auto;height:250px;" >
                    <tr style="overflow:auto;height:auto;" >
                        <td valign="top">
                            <%for (int i = 0; i < folderpbro.getRowCount(); i++) {%>
                            <div id="rep-<%=folderpbro.getFieldValueString(i, 0)%>" style="display:none"></div>
                            <%}%>
                        </td>
                    </tr>
                </table>
            </div>
        </div>
        <div id="fadeBusRole" class="blackBus"></div>
        <div id="saveNewReport" style="display:none" title="Save Report">
            <table border="0" style="width:100%;">
                <tr valign="top">
                    <td valign="top"   style="width:40%;">
                        <label class="gFontFamily gFontSize12 label">Report Name</label>
                    </td>
                    <td  valign="top"  style="width:60%;">
                        <input type="text" class="inputbox"  maxlength="200" name="reportName1" style="width:80%" id="reportName1" onblur="tabmsg1()" onkeyup="tabmsg1()" onfocus="document.getElementById('save1').disabled = false;">
                        <br>
                        <span id="duplicate1" style="color:red"></span>
                    </td>
                </tr>
                <tr valign="top"><td colspan="">&nbsp;</td></tr>
                <tr valign="top">
                    <td valign="top"  style="width:40%;"><label class="gFontFamily gFontSize12 label">Description</label> </td>
                    <td   valign="top" style="width:60%;">
                        <input type="text" class="inputbox"  name="reportDesc1" maxlength="200" id="reportDesc1" style="width:80%;overflow:auto;height:50px">
                    </td>
                </tr>
                <tr valign="top">
                    <td valign="top"  style="width:40%;"> <label class="gFontFamily gFontSize12 label">With Graph</label> </td>
                    <td   valign="top" style="width:60%;"><input type="checkbox"   name="withGraph"  id="withGraph"  ></td>
                </tr>
                <tr valign="top"><td valign="top"  colspan="">&nbsp;</td>    </tr>
                <tr><td  valign="top" colspan="2" align="center">
                        <%if (isGraphThere.equalsIgnoreCase("Yes")) {%>
                        <input type="button"  class="gFontFamily gFontSize12 navtitle-hover" style="width:auto" value="Next" id="save1" onclick="saveNewRepGrpTab('<%=PbReportId%>','<%=request.getContextPath()%>');">
                        <%} else {%>
                        <input type="button"  class="gFontFamily gFontSize12 navtitle-hover" style="width:auto" value="Next" id="save1" onclick="saveAsNewReport('<%=PbReportId%>','<%=request.getContextPath()%>','<%=reportName%>');">
                        <%}%>
                    </td>
                </tr>
            </table>
        </div>
        <%} catch (Exception e) {
                e.printStackTrace();
            }%>
        <div id="fadestart" class="black_start"></div>
        <div id="graphColsDialog" title="Add Graph Columns" style="display:none">
            <table><tr><td>
                        <input id="graphtableList" type="checkbox" onclick="getgraphDisplayTables('<%=request.getContextPath()%>','<%=params%>')">All</td>
                    <td id="graphtabListDiv" ><input type="textbox" id="graphtabsListVals"><input type="textbox" style="display:none;" id="graphtabsListIds">
                        <div id="graphparamVals" class="ajaxboxstyle" style="display:none;overflow: auto;"></div></td>
                    <td id="graphtablistLink" ><a href="javascript:void(0)" class="ui-icon ui-icon-note" onclick="graphshowList('<%=request.getContextPath()%>','<%=params%>')" ></a></td>
                    <td id="graphgoButton" onclick="graphsetValueToContainer('<%=request.getContextPath()%>','<%= PbReportId%>','<%=rolesid%>')"><input type="button" value="GO" class="navtitle-hover"></td>
                </tr></table>
            <iframe  id="graphColsFrame" name='graphColsFrame' width="100%" height="100%" frameborder="0"   SRC='TableDisplay/PbChangeGraphColumnsRT.jsp'></iframe>
        </div>
        <div id="DefinecustSeqDialog" title="Define Custom Sequence" style="display:none">
            <form id="custseqForm">
                <table  align="center" >
                    <tr>
                        <td id="DefinecustSeqtabid"></td>
                    </tr>
                </table>
            </form>
        </div>
        <div id="editViewByDiv" title="Edit ViewBy" style="display:none">
            <iframe  id="editViewByFrame" name='editViewFrame' width="100%" height="100%" frameborder="0"   src='about:blank'></iframe>
        </div>
        <!--added by shobhit for multi dashboard on 22/09/15-->        
        <div id="defineParentChildDB" title="Define Multi Dashboard" style="display:none">
            <iframe  id="defineParentChildDBFrame" name='editViewFrame' width="100%" height="100%" frameborder="0"   src='about:blank'></iframe>
        </div>
        <div id="filterViewByDiv" title="Quickfilters" style="display:none">
            <iframe  id="filterViewByFrame" name='filterViewFrame' width="100%" height="100%" frameborder="0"   src='about:blank'></iframe>
        </div>
        <div id="editViewByDivgbl" title="Resequnce params" style="display:none">
            <iframe  id="editViewByFramegbl" name='editViewFramegbl' width="100%" height="100%" frameborder="0"   src='about:blank'></iframe>
        </div>
        <div id="saveRTD" title="Tag Reports" style="display:none">
            <iframe  id="saveReportByFrame" name='saveViewFrame' width="100%" height="100%" frameborder="0"   src='about:blank'></iframe>
        </div>
        <div id="tableColsDialog" title="Add Measures" style="display:none" >
            <table><tr><td>
                        <input id="tableList" type="checkbox" onclick="getDisplayTables('<%=request.getContextPath()%>','<%=params%>')">All</td>
                    <td id="tabListDiv" ><input type="textbox" id="tabsListVals"><input type="textbox" style="display:none;" id="tabsListIds">
                        <div id="paramVals" class="ajaxboxstyle" style="display:none;overflow: auto;"></div></td>
                    <td id="tablistLink" ><a href="javascript:void(0)" class="ui-icon ui-icon-note" onclick="showList('<%=request.getContextPath()%>','<%=params%>')" ></a></td>
                    <td id="goButton" onclick="setValueToContainer('<%=request.getContextPath()%>','<%= PbReportId%>','<%=rolesid%>')"><input type="button" value="GO" class="navtitle-hover"></td>
                </tr></table>
            <iframe  id="tableColsFrame" name='tableColsFrame' width="100%" height="100%" frameborder="0"   src='TableDisplay/PbChangeTableColumnsRT.jsp'></iframe>
        </div>
        <div id="customReportDrill" title="Edit Report Customize Drill Down" style="display:none">
            <iframe  id="customReportDrillFrame" NAME='customReportDrillFrame' width="100%" height="100%" frameborder="0"   SRC='about:blank'></iframe>
        </div>
        <div id="fadestart" class="black_start"></div>
        <div id="applycolrdiv" title="Apply Color Based Grouping" style="display:none">
            <iframe id="applycolorframe" name="applycolorframe" frameborder="0" marginheight="0" marginwidth="0" src='about:blank'width="100%" height="100%"></iframe>
        </div>
        <div id="dispGrpProp" title="Graph Properties" style="display:none">
            <iframe id="dispGrpPropFrame" NAME='dispGrpPropFrame' width="100%" height="100%"  frameborder="0" SRC='about:blank'></iframe>
        </div>
        <div id="zoomgraph" title="Zoom Graph" style="display:none">
            <iframe id="zoomgraphFrame" NAME='zoomgraphFrame' width="100%" height="100%"  frameborder="0" SRC='about:blank'></iframe>
        </div>
        <div id="dispTabProp" title="Table Properties" style="display:none">
            <iframe id="dispTabPropFrame" NAME='dispTabPropFrame' width="100%" height="100%"  frameborder="0" SRC='about:blank'></iframe>
        </div>
        <div id="iTextDiv"  style="display:none" title="">
            <textarea  id="itextarea" style="width:315px" cols="50" rows="8" readonly="readonly"></textarea>
        </div>
        <div id="dispgrpAnalysis" title="Dimension Segmentation" style="display:none">
            <iframe id="dispgrpAnalysisFrame" NAME='dispgrpAnalysisFrame' width="100%" height="100%"  frameborder="0" SRC='about:blank'></iframe>
        </div><div id="dispbucketAnalysis" title="Bucket Analysis" style="display:none">
            <iframe id="dispbucketAnalysisFrame" NAME='dispbucketAnalysisFrame' width="100%" height="100%"  frameborder="0" SRC='about:blank'></iframe>
        </div>

        <div style="display:none" id="custmemDispDia" title="Custom Measures">
            <iframe  id="custmemDisp" NAME='custmemDisp' height="100%" width="100%" frameborder="0" SRC='createCustMemberinviewer.jsp'></iframe>
        </div>
        <div id="advanceFormulaDiv" title="Advance Formula" style="width:515px;height:200px;overflow-y:auto;overflow-x:auto;display: none">
            <iframe id="advanceFormulaFrame" NAME='advanceFormulaFrame' width="100%" height="100%" frameborder="0" SRC='about:blank'></iframe>
        </div>
        <div id="advanceFormulaTypeDiv" title="Advance Formula Type" style="display:none" >
            <form action=""  id="myForm1" name="myForm1">
                <table id="advFormType" class="tablesorter"  cellpadding="0" cellspacing="0" align="center">
                    <br>
                    <tr>
                        <td colspan="2"><label class="label"><b>Please select the type of Formula</b></label></td>
                    </tr>
                    <tr><td>&nbsp;</td></tr>
                    <tr>
                        <td><label class="label"><b>Type :</b>&nbsp;&nbsp;&nbsp;&nbsp;</label> </td>
                        <td>   <select id="formulaType" name="formulaType" onchange="getVarcharOp()">
                                <option value="NONE">--Select--</option>
                                <option value="DATE">Date</option>
                                <option value="NUMBER">Number</option>
                                <option value="VARCHAR">Varchar</option>
                            </select>
                        </td>
                    </tr>
                    <tr><td>&nbsp;</td></tr>
                    <tr id="myTabBodyVar">

                    </tr>
                    <tr><td>&nbsp;</td></tr>
                    <tr id="myTabBody">

                    </tr>

                </table>
            </form>
        </div>
        <div style="display:none" id="custmemMeasureDispDia" title="Custom Measures">
            <iframe  id="custmemMeasDisp"  height="100%" width="100%" frameborder="0" src='about:blank'></iframe>
        </div>
        <div id="TimeBasedDiaolgDisplay" name="TimeBasedDiaolgDisplay" title="" style="z-index: 1050">
            <iframe id="TimeBasedDisplay" name="TimeBasedDisplay" height="100%" width="100%" frameborder="0" src="about:blank"> </iframe>
        </div>
        <div id="showSqlStrDialog" title="SQL Query" style="display:none">
            <iframe  id="sqlQueryStr" NAME='sqlQueryStr' width="100%" height="100%" frameborder="0"   SRC='about:blank'></iframe>
        </div>
        <!-- added by sruthi to display fullquery       -->
        <div id="showSqlStrDialogfull" title="SQL Query" style="display:none">
            <iframe  id="sqlQueryStrfull" NAME='sqlQueryStr1' width="100%" height="100%" frameborder="0"   SRC='about:blank'></iframe>
        </div>
        <!--ended by sruthi-->
        <div id="performWhatIfDiv" style="display: none" title="Perform What If">
            <iframe  frameborder="0" id="performWhatIfFrame" name="performWhatIfFrame" style="width:750px; height:490px; "  src='about:blank'></iframe>

        </div>
        <div id="correlationId" style="display: none; font-size: 14px" title="Statistic Value">


        </div>
        <div id="showExports" title="Exports" style="display:none">
            <iframe id="showExportsFrame" NAME='showExportsFrame' width="100%" height="100%"  frameborder="0" SRC='about:blank'></iframe>
        </div>
        <div id="DefineTarget" title="Define Target" style="display: none">
            <br/>
            <table width="100%" align="center">
                <tbody align="center">
                    <tr>
                        <td>
                            Target measure name
                            <input type="text" id="trgtMesNameDisp" name="tegtMesNameDisp" style="height:21px; " >
                        </td>
                    </tr>
                </tbody>
            </table>
            <table  align="center" border="1" style="width: 100% ">
                <tr  align="center" valign="top">
                    <td align="left" valign="top" width="50%">
                        <div style="height:200px; overflow:auto" id="MeasuresDIV"  align="left">
                            <table  align="center" border="0" width="100%">
                                <tr  align="left" valign="top" style="color: black;" class="">
                                    <td style="font-weight:bold" align="center">
                                        <div class="navtitle-hover" style="height: 20px;"><font size="2" style="font-weight: bold;"> Target measures  </font>
                                        </div></td>
                                </tr></table>
                            <ul id="trgtMeasuresUL" class="sortable" >
                            </ul>
                        </div>
                    </td>
                    <td  align="left" valign="top" width="50%">
                        <div style="height:100px; overflow:auto"  align="left" >
                            <textarea cols="" rows=""  id="whatIfTrgtMesArea" name="whatIfTrgtMesArea" style="width:95%;height:80px " readonly ></textarea>
                            <input type="hidden" id="whatIfTrgtmesHidden" name="whatIfTrgtmesHidden" value="">
                            <input type="hidden" id="sensitivityFactorid" name="sensitivityFactorid" value="">
                            <a href="javascript:clearTextArea()"><font color="blue">Clear </font> </a>
                        </div>
                        <div >
                            <table>
                                <tr>
                                    <td>
                                        <input type="text" name="numberInput" id="numberInput" value="" onkeypress="return isNumberevent(event)" onblur="WhatifOperators(this.value)" title="Enter Number"style="height: 21px"/>
                                    </td>
                                </tr>
                            </table>
                            <table cellspacing="4" border="0" align="center" width="50%">
                                <tbody><tr align="center" style="width: 100%;">
                                        <td>
                                            <table>
                                                <tbody><tr><td><input type="button" onclick="WhatifOperators('+')" id="+" value="+" style="width:55px;" class="navtitle-hover"></td></tr>
                                                    <tr><td><input type="button" onclick="WhatifOperators('-')" id="-" value="-" style="width:55px;" class="navtitle-hover"></td></tr>
                                                    <tr><td><input type="button" onclick="WhatifOperators('*')" id="*" style="width:55px;" class="navtitle-hover" value="*"></td></tr>
                                                </tbody></table>
                                        </td>
                                        <td>
                                            <table>
                                                <tbody><tr><td><input type="button" onclick="WhatifOperators('/')" id="/" style="width:55px;" class="navtitle-hover" value="/"></td></tr>
                                                    <tr><td><input type="button" onclick="WhatifOperators('(')" id="(" value="(" style="width:55px;" class="navtitle-hover"></td></tr>
                                                    <tr><td><input type="button" onclick="WhatifOperators(')')" id=")" value=")" style="width:55px;" class="navtitle-hover"></td></tr>

                                                </tbody></table>
                                        </td>

                                    </tr>

                                </tbody></table>

                        </div>
                    </td>
                </tr>
            </table><br/><br/>
            <table width="100%" align="center">
                <tbody align="center">
                    <tr>
                        <td width="50%" align="right">
                            <input type="button" onclick="saveWhatIfTargetMes('<%= PbReportId%>','<%= request.getContextPath()%>')" name="save" value="Save"  class="navtitle-hover" style="width: 30%">
                        </td>
                        <td width="50%" align="left">
                            <input type="button"  onclick="saveEditTargetMes('<%= PbReportId%>','<%= request.getContextPath()%>')" id="saveEditTarget" name="saveEditTarget" value="Done"  class="navtitle-hover" style="width: 30%;display: none" >
                        </td>
                    </tr></tbody>
            </table>

        </div>
        <div  id="columnPropertiesdiv" title="Column Properties" style="display:none">
            <iframe id="columnPropertiesframe" name="columnPropertiesframe" frameborder="0" marginheight="0" marginwidth="0" src='about:blank' width="100%" height="100%"></iframe>
        </div>
        <div id="shwparamAssis" title="Parameter Assistance Filter" style="display:none">
            <div id="shwparamAssisDisplay" style="height:180px;width:auto;overflow:auto">
            </div>
        </div>

        <input type="hidden" title="<%=folderName%>" id="roleid" name="" value="<%=rolesid%>">
        <input type="hidden" name ="aggVal" id="aggVal" value="none">
        <input type ="hidden" name="fromTimeBased" id="fromTimeBased" value="false">


        <div id="paramFilterDiv" title="Parameter Filter" style="display:none">
            <iframe  id="paramFilter" NAME='paramFilter' width="100%" height="100%" frameborder="0"   SRC='about:blank'></iframe>
        </div>
        <div id="paramFilterMemberDiv" title="Parameter Members" style="display:none">
            <iframe  id="paramFilterMember" NAME='paramFilterMember' width="100%" height="100%" frameborder="0"   SRC='about:blank'></iframe>
        </div>
        <div id="paramFilterMeasureValues" title="paramFilterMeasureValues" style="display:none">
            <form id="paramFilterMeasureValFrm">

            </form>
        </div>
        <div id="QuickTimeBasedFormulaDiv" title="Quick TimeBased Formula" style="width:515px;height:300px;overflow-y:auto;overflow-x:auto;display: none">
            <iframe id="QuickTimeBasedFormulaFrame" NAME='QuickTimeBasedFormulaFrame' width="100%" height="100%" frameborder="0" SRC='about:blank'></iframe>

        </div>

        <div id="conversionFormula" title="Conversion Formula" style="display:none">
            <table align="center">
                <tr><td>New Measure Name</td><td><input type="text" name="newMsr" id="newMsr"></td></tr>
                <tr><td>Old Measure</td><td><input type="text" name="oldMsr" id="oldMsr" readonly></td></tr>
                <tr><td>Divide by</td><td><input type="text" name="divideby" id="divideby" onkeypress="javascript:return isNumberKey(event)" onblur="isZeroKey(this.id)"></td></tr>
                <tr><td><input type="checkbox" name="dividebyProgenDateDiff" id="dividebyProgenDateDiff" onclick="javascript:return isDividebyProgenDateDiff()"></td><td>Standard Progen Date Difference</td></tr>
                <tr><td>&nbsp;</td></tr>
                <tr align="center"><td colspan="2" align="center"><input type="submit" name="saveConversion" value="save" class="navtitle-hover" onclick="saveConversion('<%=request.getContextPath()%>')"></td></tr>
            </table>
        </div>

        <div id="SignConversion" title="Sign Conversion" style="display:none">
            <table align="center">
                <tr><td>New Measure Name</td><td><input type="text" name="newMsr" id="newMsr1"></td></tr>
                <tr><td>Old Measure</td><td><input type="text" name="oldMsr" id="oldMsr1" readonly></td></tr>
                <tr><td>&nbsp;</td></tr>
                <tr align="center"><td colspan="2" align="center"><input type="submit" name="signConversion" value="save" class="navtitle-hover" onclick="SaveSignConversion('<%=request.getContextPath()%>')"></td></tr>
            </table>
        </div>

        <div id="mapColorGrouping" title="Map Color Groping" style="display: none">
            <br>
            <table border="0" width="100%">
                <tr>
                    <td width="55%"><b>Select Main Measure For Color Grouping :</b> </td>
                    <td>
                        <select style="width: 130px;" class="myTextbox5"  name="mainMeasures" id="mainMeasures" >
                        </select>
                    </td>
                </tr>
            </table>
            <br> <br>
            <table width="100%">
                <tr align="center">
                    <td><input type="button"  class="navtitle-hover" style="width:auto" value="Next"  onclick="applyColorGroupToMap('<%=PbReportId%>');"></td>
                </tr>
            </table>
        </div>

        <div id="showStickyNote" style="display:none;z-index:2000;position:absolute;width:150px;left:20px;top:300px">
        </div>
        <SPAN id="StickNoteSpan">

        </SPAN>
        <span id="reptimeDetails">

        </span>
        <div id="MapMeasures" style="display: none" title="Map Measures">

        </div>

        <div id="createSegmentDiv" style="display: none" title="Segment" align="center">

            <br>
            <table border="0">
                <tr>
                    <th>Measure Name   &nbsp;:  </th>
                    <td id="measurename"></td>

                </tr>

                <tr>
                    <th>Maximum Value  :  </th>
                    <td id="maximumvalue"> </td>

                </tr>
                <tr>
                    <th>Minimum Value  :  </th>
                    <td id="minimumvalue"></td>

                </tr>
                <tr>
                    <th align="left">Average Value &nbsp; :  </th>
                    <td id="averagevalue"> </td>

                </tr>
            </table>
            <br>

            <br>

            <table id="segmentTable" align="center">
                <thead>
                    <tr >
                        <th style="font-size: small;" class="bgcolor">
                            Segment Name
                        </th >
                        <th style="font-size: small;" class="bgcolor">
                            Lower Limit
                        </th>
                        <th style="font-size: small;" class="bgcolor">
                            Upper Limit
                        </th>
                    </tr>
                </thead>

                <tbody  id="segmentvalues">
                    <tr>
                        <td>
                            <input type="text" name="segmentInput0" value="" id="segmentInput0">
                        </td>
                        <td>
                            <input type="text" name="minInput0"  id="minInput0" style="text-align:right;" onkeypress="javascript:return isNumberKey(event)">
                        </td>
                        <td>
                            <input type="text" name="maxInput0"  id="maxInput0" style="text-align:right;" onkeypress="javascript:return isNumberKey(event)" onblur="javascript:displayMinLimit(this)">
                        </td>
                    </tr>
                </tbody>

            </table>
            <br>
            <table align="center">
                <tr align="center" >
                    <td colspan="1">
                        <input type="button" class="navtitle-hover" style="width:auto" value="Add Row" onclick="addsegSingleRow()"/>

                    </td >
                    <td colspan="1">
                        <input type="button"   class="navtitle-hover" style="width:auto" value="Delete Row" onclick="deleteSegSingleRow()" />
                    </td>
                    <td colspan="1">
                        <input type="button"   class="navtitle-hover" style="width:auto" value="Go" onclick="saveSegmentValues('<%=request.getContextPath()%>')">
                    </td>
                </tr>
            </table>
            <input type="hidden" id="segmentReportId">
            <input type="hidden" id="segmentMeasureId">
        </div>
        <div id="createSegmentDialogDiv" style="display: none" title="CreateSegment" align="center">
            <br>
            <table border="0">
                <center>
                    <tr>
                        <td>
                            <b> Segment Name:</b>
                        </td>
                        <td>
                            <input type="text" name="" value="" id="segmentId" size="18">
                        </td>
                    </tr>
                    <br>
                    <tr>
                        <td><b>Select Measures :</b> </td>
                        <td>
                            <select style="width: 130px;" class="myTextbox5"  name="SegmentMeasures" id="SegmentMeasures" >
                            </select>
                        </td>
                    </tr>
                </center>
            </table>
            <br> <br>
            <input type="button" class="navtitle-hover" value="Save" onclick="openCreateSegmentDialog('<%=PbReportId%>','<%=request.getContextPath()%>')" >
        </div>
        <div id="sensitivityDiv" style="display: none">
            <iframe id="sensitivityiFrame" name="sensitivityiFrame"width="100%" height="100%" frameborder="0"   SRC='about:blank'></iframe>
        </div>

        <div>
            <div id="demoContainer" class="containerPlus draggable {buttons:'m', icon:'chart.png', skin:'stiky', width:'800',  closed:'true',rememberMe:true,title:' What-If Analysis'}" style="position:absolute;top:250px;left:400px; height:60%">
                <div id="innerContainer" ></div>
            </div>

            <div  id="applyAlarmdiv"  title="Set Reminder For StickyNote" style="display:none;">
                <iframe id="applyAlarmFrame" name="applyAlarmFrame" frameborder="0" scrolling="" marginheight="0" marginwidth="0" src='about:blank' width="100%" height="100%"></iframe>
            </div>
        </div>
        <div id="groupMeasureDiv" title="Group Measure" style="display:none;">
            <table align="center">
                <tr>
                    <td>
                        Group Name:
                    </td>
                    <td>
                        <input type="text" name="mesGrpName" value="" id="mesGrpName">
                    </td>
                </tr>
            </table>
            <div id="grpContentDiv" >

            </div>
            <br/><br/><br/>
            <div align="center">
                <input type="button" name="done" value="Done" onclick="saveMeasureGroup('<%= PbReportId%>')" class="navtitle-hover" >
                <input type="button" name="edit" value="Edit Measure" onclick="editGrpMeasure()" class="navtitle-hover" >
            </div>
        </div>
        <div id="openEditGrpMesNames" title="Edit Group Measure Names" style="display:none;" >
            <br/><br/>
            <table width="100%" border="1">
                <thead>
                    <tr>
                        <th class="navtitle-hover" width="50%">Measure Name</th>
                        <th class="navtitle-hover"  width="50%">Edit Measure Name</th>
                    </tr>
                </thead>
                <tbody id="editMeasureNameTbody" style="overflow:auto"></tbody>
            </table>
            <br/><br/>
            <table align="center">
                <tr>
                    <td>
                        <input type="button" name="save" value="Save" onclick="saveEditMesNames()"  class="navtitle-hover" >
                    </td>
                </tr>
            </table>

        </div>
        <div id="editSensitivityDiv" title="Edit Sensitivity" style="display: none">
            <table>
                <tr>
                    <td>
                        Sensitivity Factor:
                    </td>
                    <td>
                        <select id="editSensitivityFactor" name="editSensitivityFactor" >
                            <option value="">--select--</option>
                        </select>
                    </td>
                </tr>
            </table><br/><br/>
            <table align="center">
                <tr>
                    <td>
                        <input type="button" class="navtitle-hover" value="Ok" onclick="changeEditSensitivity('<%=request.getContextPath()%>','<%=PbReportId%>')" style="width:100px" >
                    </td>
                    <td>
                        <input type="button" class="navtitle-hover" value="Cancel" onclick="cancelEditSensitivit()" style="width:100px">
                    </td>
                </tr>
            </table>
        </div>

        <div id="comparableReportsDiv" style="display:none">

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
        <div id="shareReport" title="Share Report" style="display: none">
            <table>
                <tr>
                    <td width="20%"><b>Format</b> </td>
                    <td>
                        <select name="fileType" id="fileType1" style="width:130px">
                            <option value="H">HTML</option>
                            <option value="E">Excel</option>
                            <option value="P">PDF</option>
                            <option value="CSV">CSV</option>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td width="20%"><b>Users </b></td>
                    <td>
                        <select name="selectusers" id="selectusers" style="width:130px" onchange="displaytextarea()">
                            <option value="">Select</option>
                            <option value="selected">Selective Users</option>
                            <option value="All" >All Users</option>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td width="20%" id="share_subject_id" style="display:none"><b>Subject</b> </td>
                    <td colspan="1" >

                        <input type="text" id="share_subject" name="share_subject" style="display:none">
                    </td>
                </tr>
                <tr>
                    <td width="20%" id="comments_id" style="display: none" >Comments</td>
                    <td colspan="1"><textarea  id="comments" name="comments" cols="" rows="" style="width:250px;height:70px;display:none"></textarea></td>
                </tr>
                <tr>
                    <td width="20%" id="tomailtd" style="display:none"><b>Email To</b> </td>
                    <td colspan="1" >

                        <textarea  id="userstextarea" name="userstextarea" cols="" rows=""  style="width:250px;height:80px;display:none"></textarea>
                    </td>
                </tr>
                                        <tr>
                                            <td colspan="2">
                                                <table border="0" style="width:100%">
                                                    <tr style='width:100%'>
                                                        <td style='width:40%'>Header Logo</td>
                                                        <td style='width:10%'><input type='checkbox' id="headerLogo" name="headerLogo"></td>
                                                        <td style='width:40%'>Footer Logo</td>
                                                        <td style='width:10%'><input type='checkbox' id="footerLogo" name="footerLogo"></td>
                                                    </tr>
                                                </table>
                                            </td>
                                        </tr>
                                         <tr>
                                            <td colspan="2">
                                                <table border="0" style="width:100%">
                                                    <tr style='width:100%'>
                                                        <td style='width:40%'>Optional Header</td>
                                                        <td style='width:10%'><input type='checkbox' id="optionalHeader" name="optionalHeader"></td>
                                                        <td style='width:40%'>Optional Footer</td>
                                                        <td style='width:10%'><input type='checkbox' id="optionalFooter" name="optionalFooter"></td>
                                                    </tr>
                                                </table>
                                            </td>
                                        </tr>
                                         <tr>
                                            <td colspan="2">
                                                <table border="0" style="width:100%">
                                                    <tr style='width:100%'>
                                                        <td style='width:40%'>Signature</td>
                                                        <td style='width:10%'><input type='checkbox' id="htmlSignature" name="htmlSignature"></td>
                                                        <td style='width:40%'></td>
                                                        <td style='width:10%'></td>
                                                    </tr>
                                                </table>
                                            </td>
                                        </tr>
                <tr> <td colspan="2">&nbsp; </td>  </tr>
                <tr>
                    <td colspan="2" align="center">
                        <input class="alert-button-hover" type="button" id="share_report_button" value="Share Report" onclick="SendReportEmail()">
                    </td>
                </tr>
                <tr> <td colspan="2">&nbsp; </td>  </tr>
                <tr> <td colspan="2"><span style="color:red;font-size:12px;"><sup>*</sup></span> Please separate multiple Email Id's by comma(,). </td>  </tr>
            </table>
        </div>
        <div id="sequenceDiv" style="display: none"></div>
        <div id="rowWiseClrId" style="display: none"></div>

        <div id="customKpidiv" title="custom KPI" style="display:none">
            <table>
                <tr>
                    <td>Measure Name: </td>
                    <td><input type="text" value="" id="measElementId" name="measElementId" readonly/></td>
                </tr>
                <tr>
                    <td>KPI Name: </td>
                    <td><input type="text" value="" id="measKPIName" name="measKPIName"/></td>
                </tr>
                <tr>
                    <td><input type="radio" name="totalvalue" id="subtotalDet" value="subtotal" checked>Sub Total</td>
                    <td><input type="radio" name="totalvalue" id="grandtotalDet" value="grandtotal" >Grand Total</td>
                <tr>
                    <td>Aggregation Type: </td>
                    <td>
                        <select id="aggType" name="aggType">
                            <option value="sum">Sum</option>
                            <option value="avg">Average</option>
                            <option value="min">Minimum</option>
                            <option value="max">Maximum</option>
                            <option value="count">Count</option>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td><input type="radio" name="timeDetails" id="static" onclick="getDynamicDetails()" value="static">Static</td>
                    <td><input type="radio" name="timeDetails" id="dynamic" onclick="getDynamicDetails()" value="dynamic" checked>Dynamic</td>
                </tr>
                <tr><td><table id="selectTimeDet" >
                            <tr><td><input type="radio" name="timeMapDetails" id="repTimeDet" value="reportTime">Report Time Details</td></tr>
                            <tr><td><input type="radio" name="timeMapDetails" id="otherTimeDet" value="otherTime" checked>Dashboard/Oneview Time Details</td></tr>
                        </table></td></tr>
                <tr>
                    <td colspan="2"><input type="button" class="navtitle-hover" value="Done" onclick="saveReportKPI('<%=request.getContextPath()%>','<%=PbReportId%>')"/></td>
                </tr>
            </table>
            <input type="hidden" id="reportKpiMeasId" value="" name="reportKpiMeasId"/>
        </div>


        <div id="goalseakId" style="display:none"  title="Applying GoalSeek">

            <form  id="goalbaicId" action="" method="post" name="goalSeek">
                <center>
                    <table align="center">
                        <tr><td><font size="2" style="font-weight: bold;">Grand Total </font><input type="text" id="grandTotalId" align="center" value='' class="" readonly> </td></tr>
                        <tr><td><font size="2" style="font-weight: bold;">Enter Goal: </font><input type="text" id="goailId" align="center" value='' class="" > </td></tr>
                    </table>


                    <table align="center">
                        <tr>
                            <td align="center">
                                <input type="button" align="center" value="Done" class="navtitle-hover"  onclick="addRuntimeColumn('GoalSeekColumn','_gl','(GoalSeek)',dimId,dimLabel,'<%=PbReportId%>','<%=request.getContextPath()%>')">
                            </td>
                        </tr>

                    </table>

                    <input type="hidden" id="colNameId" name="colNameId" value="" />
                    <input type="hidden" id="discolId" name="discolId" value="" />
                </center>
            </form>
        </div>
        <IFRAME NAME="dFrame" ID="downFrame" STYLE="display:none;width:0px;height:0px" SRC="TableDisplay/pbDownload.jsp" frameborder="0"></IFRAME>
        <div id="goalAdhocId" style="display:none"  title="Applying GoalSeek">
            <form id='percentColmnId' name='percentColmnId' action='' method='post'></form>
        </div>
        <div id="goalTimeIndviId" style="display:none"  title="Applying GoalSeek Time Based">
            <form id='goalTimeChangeId' name='percentColmnId' action='<%=request.getContextPath()%>/TableDisplay/pbDownload.jsp' method='post'></form>
        </div>
        <div id="goalTimePercentBaseId" style="display:none"  title="Applying GoalSeek Time Based">
            <form id='goalPercentBaseId' name='percentColmnId' action='<%=request.getContextPath()%>/TableDisplay/pbDownload.jsp' method='post'></form>
        </div>
        <div id="newProductintroId" style="display: none"></div>

        <div id="goalTimeBaseId" style="display: none" title="Goalseek On TimeBase" >

        </div>
        <div id="drillMeasuresId" style="display: none" title="Enable Measure Drill">
            <iframe  id="drillmeasFrame" NAME='drillmeasFrame'  width="100%" height="100%" frameborder="0" SRC='about:blank'></iframe>
        </div>
        <div id="relatedMeasuresId" style="display: none" title="Related Measure ">

        </div>

        <div id="designerGraphList" style="display:none;"></div>

        <div id="overWriteReport" style="display:none" title="OverWriteReport">
            <table>
                <tr><td colspan="2"><font size="2" style="font-weight: bold;">Do you want to over write the report ?</font></td></tr>
                <% if (!alist.get(1).toString().equalsIgnoreCase("PRG_DATE_RANGE")) {%>
                <tr><td><input type="radio" name="Date" id="sysDate" value="sysDate">Global Date</td>
                    <td>&nbsp;&nbsp;&nbsp;<input type="radio" name="Date" id="reportDate" value="reportDate" >Report Date</td>
                    <td>&nbsp;&nbsp;&nbsp;<input type="radio" name="Date" id="currdetails" value="currdetails" checked>Current Details</td>
                    <td>&nbsp;&nbsp;&nbsp;<input type="checkbox" name="autometicDate" id="autometicDate" <%=autoDate%> onclick="" value="autometicDate">Automatic Date</td></tr>
                <tr><td><br></td></tr>
                <tr><td><input type="radio" name="Date" id="yestrday" value="yestrday">YesterDay</td><td>&nbsp;&nbsp;&nbsp;<input type="radio" name="Date" id="tomorow" value="tomorow">Tomorrow</td>
                    <td>&nbsp;&nbsp;&nbsp;<input type="checkbox" name="Cache" id="cacheAO"  onclick="" value="cacheAO">Cache Analytical Object</td></tr>
                <tr><td><br></td></tr>
                <tr><td><input type="radio" name="Date" id="fixeddate" value="fixeddate" >Fixed Date</td><td><input id="fdatepicker" type="text" value="" style="width: 120px;" maxlength="100" name="fdate" readonly=""></td>
                </tr>
                <tr><td><input type="radio" name="Date" id="newSysDate" value="newSysDate">System Date</td><td><select id="sysSign" name="sign">
                            <option value="+">+</option>
                            <option value="-">-</option>
                        </select></td><td><input id="newSysVal" type="text">Days </td></tr>
                <tr><td><input type="radio" name="Date" id="globalDate" value="globalDate" >Global Date</td><td><select id="globalSign" name="globalSign">
                            <option value="+">+</option>
                            <option value="-">-</option>
                        </select></td><td><input id="newGlobVal" type="text">Days </td></tr>
                        <%} else {%>
                <tr><td><input type="radio" name="Date" id="reportDate" onclick="getCustomDate()" value="reportDate" checked>Report Date</td><td>&nbsp;&nbsp;&nbsp;<input type="radio" name="Date" id="customDate" onclick="getCustomDate()" value="customDate">Custom Date</td>
                    <td>&nbsp;&nbsp;&nbsp;<input type="radio" name="Date" id="currdetails" onclick="getCustomDate()" value="currdetails">Current Details</td>
                    <td>&nbsp;&nbsp;&nbsp;<input type="checkbox" name="autometicDate" id="autometicDate" <%=autoDate%> onclick="" value="autometicDate">Autometic Date</td>
                    <td>&nbsp;&nbsp;&nbsp;<input type="checkbox" name="Cache" id="cacheAO"  onclick="" value="cacheAO">Cache Analytical Object</td></tr>
                <table id="dateRangeTab" style="display: none;">
                    <tr><td style="font-weight:bold;">For From Date</td></tr>
                    <tr><td><input type="radio" name="FromDate" id="fromfixeddate" value="fromfixeddate">Fixed Date</td><td><input id="ffdatepicker" type="text" value="" style="width: 120px;" maxlength="100" name="ffdate" readonly=""></td>
                    </tr>
                    <tr><td><input type="radio" name="FromDate" id="fromyestrday" value="fromyestrday" checked>YesterDay</td><td>&nbsp;<input type="radio" name="FromDate" id="fromtomorow" value="fromtomorow">Tomorrow</td></tr>
                    <tr><td><input type="radio" name="FromDate" id="fromSysDate" value="fromSysDate">System Date</td><td><select id="fromSysSign" name="sign">
                                <option value="+">+</option>
                                <option value="-">-</option>
                            </select></td><td><input id="fromSysVal" type="text">Days </td></tr>
                    <tr><td><input type="radio" name="FromDate" id="fromglobalDate" value="fromToglobalDate" >Global Date</td><td><select id="fromglobalSign" name="globalSign">
                                <option value="+">+</option>
                                <option value="-">-</option>
                            </select></td><td><input id="fromGlobVal" type="text">Days </td></tr>
                    <tr><td style="font-weight:bold;">For To Date</td></tr>
                    <tr><td><input type="radio" name="ToDate" id="tofixeddate" value="tofixeddate">Fixed Date</td><td><input id="ftdatepicker" type="text" value="" style="width: 120px;" maxlength="100" name="ftdate" readonly=""></td>
                    </tr>
                    <tr><td><input type="radio" name="ToDate" id="toyestrday" value="toyestrday">YesterDay</td><td>&nbsp;<input type="radio" name="ToDate" id="totomorow" value="totomorow" checked>Tomorrow</td></tr>
                    <tr><td><input type="radio" name="ToDate" id="toSystDate" value="toSystDate">System Date</td><td><select id="toSysSign" name="sign">
                                <option value="+">+</option>
                                <option value="-">-</option>
                            </select></td><td><input id="toSysVal" type="text">Days </td></tr>
                    <tr><td><input type="radio" name="ToDate" id="toglobalDdate" value="toglobalDdate" >Global Date</td><td><select id="toglobalSign" name="globalSign">
                                <option value="+">+</option>
                                <option value="-">-</option>
                            </select></td><td><input id="toGlobVal" type="text">Days </td></tr>
                    <tr><td style="font-weight:bold;">For Compare From Date</td></tr>
                    <tr><td><input type="radio" name="CmpFrmDate" id="CmpFrmfixeddate" value="CmpFrmfixeddate">Fixed Date</td><td><input id="fcfdatepicker" type="text" value="" style="width: 120px;" maxlength="100" name=fcfdate" readonly=""></td>
                    </tr>
                    <tr><td><input type="radio" name="CmpFrmDate" id="CmpFrmyestrday" value="CmpFrmyestrday" checked>YesterDay</td><td>&nbsp;<input type="radio" name="CmpFrmDate" id="CmpFrmtomorow" value="CmpFrmtomorow">Tomorrow</td></tr>
                    <tr><td><input type="radio" name="CmpFrmDate" id="CmpFrmSysDate" value="CmpFrmSysDate">System Date</td><td><select id="CmpFrmSysSign" name="sign">
                                <option value="+">+</option>
                                <option value="-">-</option>
                            </select></td><td><input id="CmpFrmSysVal" type="text">Days </td></tr>
                    <tr><td><input type="radio" name="CmpFrmDate" id="CmpFrmglobalDate" value="fromToglobalDate" >Global Date</td><td><select id="CmpFrmglobalSign" name="globalSign">
                                <option value="+">+</option>
                                <option value="-">-</option>
                            </select></td><td><input id="CmpFrmGlobVal" type="text">Days </td></tr>
                    <tr><td style="font-weight:bold;">For Compare To Date</td></tr>
                    <tr><td><input type="radio" name="CmpToDate" id="Cmptofixeddate" value="Cmptofixeddate">Fixed Date</td><td><input id="fctdatepicker" type="text" value="" style="width: 120px;" maxlength="100" name="fctdate" readonly=""></td>
                    </tr>
                    <tr><td><input type="radio" name="CmpToDate" id="cmptoyestrday" value="cmptoyestrday">YesterDay</td><td>&nbsp;<input type="radio" name="CmpToDate" id="cmptotomorow" value="cmptotomorow" checked>Tomorrow</td></tr>
                    <tr><td><input type="radio" name="CmpToDate" id="cmptoSysDate" value="cmptoSysDate">System Date</td><td><select id="cmptoSysSign" name="sign">
                                <option value="+">+</option>
                                <option value="-">-</option>
                            </select></td><td><input id="cmptoSysVal" type="text">Days </td></tr>
                    <tr><td><input type="radio" name="CmpToDate" id="cmptoglobalDate" value="fromToglobalDate" >Global Date</td><td><select id="cmptoglobalSign" name="globalSign">
                                <option value="+">+</option>
                                <option value="-">-</option>
                            </select></td><td><input id="cmptoGlobVal" type="text">Days </td></tr>
                </table>
                <%}%>
                <tr><td><br></td></tr>
                <tr><td><br></td></tr>
                <tr><td colspan="2" align="center"><input type="button" value="Ok" class="navtitle-hover" style="width:40px;height:25px;color:black" onclick="OverWriteReport('<%=request.getContextPath()%>','<%=PbReportId%>',istoggledate,incriment)"/>&nbsp;&nbsp;&nbsp;
                        <input type="button" value="Cancel" class="navtitle-hover" style="width:50px;height:25px;color:black" onclick="cancelOverWriteReport()"/></td></tr>
            </table>
        </div>

        <div id="allParametersTab" style="display:none;width:22%;height:140%;background-color:white; direction: ltr; float: right; position:absolute;text-align:left;border:1px solid #000000;left:0px;top:122px;z-index: 10000;overflow: auto;border-color: LightGrey;">
            <table>
                <tr><td  style="padding-top:6px"></td></tr>
                <% if (userdao.getFeatureEnable("Favorite Links") || userType.equalsIgnoreCase("SUPERADMIN")) {%>
                <tr><td><td style="padding-right:10px"><a href="javascript:void(0)"class='ui-icon ui-icon-star' title="Favourite Links" onclick="remainingRegions('<%=PbReportId%>')"></a></td>
                    <% }%> 
                    <!--<td><td style="padding-right:10px"><a href="javascript:void(0)"class='ui-icon ui-icon-wrench' title="Actions" onclick="allActions()"></a></td>-->
                    <% if (userdao.getFeatureEnable("Scheduled Reports") || userType.equalsIgnoreCase("SUPERADMIN")) {%>
                    <!--<td><td style="padding-right:10px"><a href="javascript:void(0)"class='ui-icon ui-icon-clock' title="Scheduled Reports" onclick="scheduledReps()"></a></td>-->
                    <% }
                        if (userdao.getFeatureEnable("Snapshots") || userType.equalsIgnoreCase("SUPERADMIN")) {%>
                    <!--<td><td style="padding-right:10px"><a href="javascript:void(0)"class='ui-icon ui-icon-video' title="SnapShots" onclick="getsnapShots()"></a></td>-->
                    <% }
                         if (userdao.getFeatureEnable("Top/Bottom") || userType.equalsIgnoreCase("SUPERADMIN")) {%>
                    <td><td style="padding-right:10px"><a href="javascript:void(0)"class='ui-icon ui-icon-mail-open' title="Top 5/Bottom 5" onclick="getdisplayTopBottom()"></a></td>
                    <% }
                          if (userdao.getFeatureEnable("Report Info") || userType.equalsIgnoreCase("SUPERADMIN")) {%>
                    <td><td style="padding-right:10px"><a href="javascript:void(0)"class='ui-icon ui-icon-lightbulb' title="Information about Report" onclick="getdisplayBusRoles()"></a></td>
                    <% }%>
                    <!--<td><td style="padding-right:10px"><a href="javascript:void(0)"class='ui-icon ui-icon-newwin' title="Enable For Xtend" onclick="createCSVOfreport('<%=request.getContextPath()%>','<%=PbReportId%>','<%=USERID%>')"></a></td>-->
                    <td><td align="right" style="padding-left:50px"><a href="javascript:void(0)"class='ui-icon ui-icon-triangle-1-w' onclick="closeAllparamsTab()"></a></td>
                </tr>
            </table><hr style="color:LightGrey;"/>
            <table width="80%"><tr><td  style="padding-top:6px"></td></tr>
                <tr><td>
                        <div id="allActions" style="width:100%;display:none;">
                            <table>
                                <tr style="height:20%"><td style="width:100%;height:25px;"><u><span style="font-size:13px;font-family: helvetica;font-weight:bold;">Actions</span></u></td></tr>
                                <% if (userdao.getFeatureEnable("E-Mail Report") || userType.equalsIgnoreCase("SUPERADMIN")) {%>
                                <tr style="height:20%"><td style="width:100%;height:25px;" id="keyevent"><a  href="javascript:openShareReportDiv()"><b style="font-size:12px">Share Report</b></a></td></tr>
                                <% }
                                     if (userdao.getFeatureEnable("Report Comparison") || userType.equalsIgnoreCase("SUPERADMIN")) {%>
                                <tr style="height:20%">
                                    <%if (!container.isReportCrosstab() && !container.isSegementedReport()) {%>
                                    <td style="width:100%;height:25px;"><a href="javascript:getComparableReports('<%=request.getContextPath()%>','<%=PbReportId%>')"> <b style="font-family: helvetica;font-size:12px">Compare Reports</b></a></td>
                                    <%}%>
                                </tr><%  }
                                     if (userdao.getFeatureEnable("Overwrite Report") || userType.equalsIgnoreCase("SUPERADMIN")) {%>
                                <tr style="height:20%"><td style="width:100%;height:25px;"><a href="javascript:overWriteReport1('<%=request.getContextPath()%>','overWrite')"><b style="font-family: helvetica;font-size:12px">Over Write Report</b></a></td></tr>
                                <% }
                                      if (userdao.getFeatureEnable("Save as New Report") || userType.equalsIgnoreCase("SUPERADMIN")) {%>
                                <tr style="height:20%"><td style="width:100%;height:25px"> <a href="javascript:openNewReportDtls('<%=request.getContextPath()%>')"><b style="font-family: helvetica;font-size:12px">Save as New Report</b></a></td></tr>
                                <% }%>
                                <% if (isGraphThere != null && !isGraphThere.equalsIgnoreCase("Yes")) {%>
                                <tr style="height:20%"><td style="width:100%;height:25px;"><a href="javascript:addGraphInDesigner('<%=request.getContextPath()%>','<%=PbReportId%>')"> <b style="font-family: helvetica;font-size:12px">Add Graph</b></a></td></tr>
                                <%}%>
                                <%if (userdao.getFeatureEnable("Assign Report to Users") || userType.equalsIgnoreCase("SUPERADMIN")) {%>
                                <tr style="height:20%"><td style="width:100%;height:25px"><a href="javascript:saveNewRepGrpTab1('<%=request.getContextPath()%>','<%=USERID%>','<%=rolesid%>','<%=PbReportId%>','<%=reportName%>')"><b style="font-family: helvetica;font-size:12px">Assign Report to Users</b></a></td></tr>
                                <% }%>
                                <tr style="height:20%"><td style="width:100%;height:25px"><a href="javascript:openSnapShotNameDiv()"><b style="font-family: helvetica;font-size:12px">Save As Advanced Html</b></a></td></tr>
                                <%  if (userdao.getFeatureEnable("Store Snapshot") || userType.equalsIgnoreCase("SUPERADMIN")) {%>
                                <tr style="height:20%"><td style="width:100%;height:25px"><a href="javascript:storesnapshot()" id="sanpShotDiv"><b style="font-family: helvetica;font-size:12px">Store Snapshot</b></a></td></tr>
                                <% }
                                     if (userdao.getFeatureEnable("Store Static Headline") || userType.equalsIgnoreCase("SUPERADMIN")) {%>
                                <tr style="height:20%"><td style="width:100%;height:25px"><a href="javascript:storeheadline()" id="snapShotHeadline"><b style="font-family: helvetica;font-size:12px">Store Static Headline</b></a></td></tr>
                                <% }
                                     if (userdao.getFeatureEnable("Store Dynamic Headline") || userType.equalsIgnoreCase("SUPERADMIN")) {%>
                                <tr style="height:20%"><td style="width:100%;height:25px"><a href="javascript:storedynamicheadline()" id="dynamicHeadline"><b style="font-family: helvetica;font-size:12px">Store Dynamic Headline</b></a></td></tr>
                                <% }
                                     if (userdao.getFeatureEnable("Scheduler") || userType.equalsIgnoreCase("SUPERADMIN")) {%>
                                <tr style="height:20%"><td style="width:100%;height:25px"><a href="javascript:openScheduleReport()"><b style="font-family: helvetica;font-size:12px">Daily Report Scheduler</b></a></td></tr>
                                <% }
                                      if (userdao.getFeatureEnable("View Scheduled Reports") || userType.equalsIgnoreCase("SUPERADMIN")) {%>
                                <%  }
                                      if (userdao.getFeatureEnable("Dimension Drill Down") || userType.equalsIgnoreCase("SUPERADMIN")) {%>

                                <tr style="height:20%"><td style="width:100%;height:25px;"><a href="javascript:displayreportCustom()"> <b style="font-family: helvetica;font-size:12px">Customize Drill Down</b></a></td></tr>
                                <%  }
                                     if (userdao.getFeatureEnable("Display Report Query") || userType.equalsIgnoreCase("SUPERADMIN")) {%> <li id="showsql" style="width:100%">
                                <tr style="height:20%"><td id="showsql" style="width:100%;height:25px">
                                        <a id="showSql" href="javascript:showSqlQuery('show')" title="Click to See SQL Query" ><b style="font-family: helvetica;font-size:12px">Display SQL Query </b></a>
                                    </td></tr>
                                    <% }
                                          if (userdao.getFeatureEnable("View Fact Formula") || userType.equalsIgnoreCase("SUPERADMIN")) {%>
                                <tr style="height:20%"><td style="width:100%;height:25px"><a href="javascript:factFormulaDiv()"><b style="font-family: helvetica;font-size:12px">View Fact Formula</b></a></td></tr>
                                <% }
                                    if (userdao.getFeatureEnable("Parameter Reset") || userType.equalsIgnoreCase("SUPERADMIN")) {%>
                                <tr style="height:20%"><td style="width:100%;height:25px"><a href="javascript:viewReset(<%=PbReportId%>)"><b style="font-family: helvetica;font-size:12px">Reset Parameter</b></a></td></tr>

                                <% }
                                      if (userdao.getFeatureEnable("Measure Drill Down") || userType.equalsIgnoreCase("SUPERADMIN")) {%>
                                <tr style="height:20%"><td style="width:100%;height:25px;"><a href="javascript:reportDrillAssignment('<%=request.getContextPath()%>','<%=PbReportId%>','<%=container.getMsrDrillReportSelection()%>')"> <b style="font-family: helvetica;font-size:12px">Assign Measure - Report Drill</b></a></td></tr>

                                <tr style="height:20%"><td style="width:100%;height:25px;"><a href="javascript:advanceParamter('<%=PbReportId%>','<%=request.getContextPath()%>')"><b style="font-family: helvetica;font-size:12px">Advanced Parameters</b></a></td></tr>
                                <% }
                                     if (userdao.getFeatureEnable("Parameter Edit (View/Filter)") || userType.equalsIgnoreCase("SUPERADMIN")) {%>
                                <tr style="height:20%"><td style="width:100%;height:25px;"><a href="javascript:paramViewFilterEdit('<%=request.getContextPath()%>','<%=PbReportId%>')"> <b style="font-family: helvetica;font-size:12px">Parameter View/Filter Edit</b></a></td></tr>
                                <% }
                                     if (userdao.getFeatureEnable("Initialize Report") || userType.equalsIgnoreCase("SUPERADMIN")) {%>
                                <tr style="height:20%"><td style="width:100%;height:25px;"><a href="javascript:initilizeParameters('<%=request.getContextPath()%>','<%=PbReportId%>')"> <b style="font-family: helvetica;font-size:12px">Initialize Report</b></a></td></tr>
                                <% }%>
                                <tr style="height:20%"><td style="width:100%;height:25px;"><a href="javascript:editBuckets('<%=request.getContextPath()%>','<%=rolesid%>')"> <b style="font-family: helvetica;font-size:12px">Edit Buckets</b></a></td></tr>
                                <%if (container.isQuickRefreshEnabled()) {%>
                                <tr style="height:20%"><td style="width:100%;height:25px;"><a href="javascript:quickRefresh('<%=request.getContextPath()%>','<%=PbReportId%>')"><b style="font-family: helvetica;font-size:12px;color:red;">Create Quick Refresh</b></a></td></tr>
                                <%} else {%>
                                <tr style="height:20%"><td style="width:100%;height:25px;"><a href="javascript:quickRefresh('<%=request.getContextPath()%>','<%=PbReportId%>')"><b style="font-family: helvetica;font-size:12px">Create Quick Refresh</b></a></td></tr>
                                <% }%>
                            </table>
                        </div>
                        <% if (userdao.getFeatureEnable("Scheduled Reports") || userType.equalsIgnoreCase("SUPERADMIN")) {%>
                        <div id="scheduledReps" style="width:100%;display:none;">
                            <table><tr style="height:20%"><td style="width:100%;height:25px;"><u><span style="font-size:13px;font-family: helvetica;font-weight:bold;">Scheduled Reports</span></u></td></tr></table>
                            <table id="schedule1"  cellpadding="2"   cellspacing="0"  class="ui-corner-all" style='display:none;width:100%;overflow: auto;' height="180px">
                            </table>
                        </div>
                        <% }%>
                        <% if (userdao.getFeatureEnable("Snapshots") || userType.equalsIgnoreCase("SUPERADMIN")) {%>
                        <div id="snapShotsDiv" style="width:100%;display:none;">
                            <table><tr style="height:20%"><td style="width:100%;height:25px;"><u><span style="font-size:13px;font-family: helvetica;font-weight:bold;">SnapShots</span></u></td></tr></table>
                            <table id="snapshots" cellpadding="2"   cellspacing="0" style="display:<%=String.valueOf(session.getAttribute("SnapshotStatus"))%>;border-width:thin" align="center" class="ui-corner-all" width="99%" border="0">
                                <tr>
                                    <td style="border-right:black" align="left" colspan="2">
                                        <iframe src="<%=request.getContextPath()%>/snapShotdisplay.jsp?REPORTID=<%=PbReportId%>" id="sanpFrame" frameborder="0" style="width:100%;overflow:auto"></iframe>
                                    </td>
                                </tr>
                                <tr>
                                    <td></td>
                                    <td align="right" style="width:100%"><a href="javascript:showPersonalisedReports()" class="ui-icon ui-icon-trash" title="Delete" style="font-weight:bold"></a></td></tr>

                            </table>
                        </div>
                        <% }%>

                        <% if (userdao.getFeatureEnable("Top/Bottom") || userType.equalsIgnoreCase("SUPERADMIN")) {%>
                        <div id="topBottomDispDiv" style="width:100%;display:none;">
                            <table><tr style="height:20%"><td style="width:100%;height:35px;"><u><span style="font-size:13px;font-family: helvetica;font-weight:bold;">Top 5/ Bottom 5</span></u></td></tr></table>
                            <table id="topBot" style="display:<%=String.valueOf(session.getAttribute("TopBotStatus"))%>;" cellpadding="2"   cellspacing="2"  class="ui-corner-all" style="width:100%;height:100%">
                                <tbody>
                                    <tr align="center">
                                        <td><iframe src="<%=request.getContextPath()%>/topBottom.jsp?REPORTID=<%=PbReportId%>" scrolling="no" id="favFrame" frameborder="0" style="width:100%;height:200px"></iframe>
                                        </td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>
                        <% }%>
                        <div id="busRoleDispDiv" style="width:100%;display:none;">

                            <table>
                                <% if (userdao.getFeatureEnable("Report Info") || userType.equalsIgnoreCase("SUPERADMIN")) {%>
                                <tr><td><u><span style="font-size:13px;font-family: helvetica;font-weight:bold;">Report Time Info.</span></u></td></tr>
                                <tr style="width:100%">
                                    <td id="RepTimeinfo1">
                                    </td>
                                </tr>
                                <% }%>
                                <tr><td  style="padding-top:10px"></td></tr>
                                <tr><td><u><span style="font-size:13px;font-family: helvetica;font-weight:bold;">Drill Down Viewer</span></u></td></tr>
                                <tr><td>
                                        <%
                                             if (container.getParameterDrillDisplay() != null) {%>
                                        <%=container.getParameterDrillDisplay()%>
                                        <%}
                                        %>
                                    </td></tr>
                                <tr><td  style="padding-top:10px"></td></tr>
                                <tr style="height:20%"></tr>
                                <tr><td><u><span style="font-size:13px;font-family: helvetica;font-weight:bold;">Parameter Assistance</span></u></td></tr>
                                <tr><td>
                                        <%
                                             if (container.getReportParamDrillAssis() != null) {%>
                                        <%=container.getReportParamDrillAssis()%>
                                        <%}
                                        %>
                                    </td></tr>
                            </table>
                        </div>
                        <div id="remainingRegions" style="width:100%;display:none;">
                            <table width="100%">
                                <tr><td ><u><span style="font-size:12px;font-family: helvetica;font-weight:bold;">Favourite Links</span></u></td></tr>
                                <tr style="width:99%;height:100%;">
                                    <td style="width:99%;height:100%;">
                                        <% ReportTemplateDAO rDao = new ReportTemplateDAO();
                                        %>
                                        <%=rDao.getFavLiksRegion(session, request.getContextPath())%>
                                    </td>
                                </tr>

                            </table>
                        </div>
                    </td></tr>
            </table>
        </div>
        <div id="goalSeekIndividual" style="display: none">
            <table>
                <thead>
                <th><font size="2" style="font-weight: bold;">Prior Value&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</font></th>
                <th><font size="2" style="font-weight: bold;">Current Value&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</font></th>
                <th><font size="2" style="font-weight: bold;">Change% Value&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</font></th>
                <th><font size="2" style="font-weight: bold;">GS(ch%) Value&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</font></th>
                <th><font size="2" style="font-weight: bold;">GoalSeek Value&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</font></th>
                </thead>
                <tbody id="goalSeekBody">

                </tbody>
            </table>
        </div>
        <div id="ScheduleReport" style="display:none" title="Schedule Report" style="height:360px;">
            <form action="" name="dailyReportScheduleForm" id="dailyReportScheduleForm" method="post">
                <table>
                    <tr>
                        <td class="myalertheader">Schedule Name</td>
                        <td class="pdg-left-2"><input id="scheduleName" type="text" value="" style="width: auto;" maxlength="100" name="scheduleName"></td>
                    </tr>
                    <tr id="emailId">
                        <td class="myalertheader">Email To</td>
                        <td class="pdg-left-2"><textarea id="usertextarea" style="width: 250px; height: 80px;" rows="" cols="" name="usertextarea"></textarea></td>
                    </tr>
                    <tr id="formatId">
                        <td class="myalertheader">Format</td>
                        <td class="pdg-left-2"><select style="width: 130px;" id="fileType" name="fileType">
                                <option value="H">HTML</option>
                                <option value="E">Excel</option>
                                <option value="P">PDF</option>
                            </select>
                        </td>
                    </tr>
<!--edited by shivam-->
                    <tr><td class="myalertheader">StartDate</td><td class="pdg-left-2"><input id="sDatepicker"  class='mydate' type="text" value="" style="width: 120px;" maxlength="100" name="startdate" readonly=""></td></tr>
                    <tr><td class="myalertheader">End Date</td><td class="pdg-left-2"><input id="eDatepicker"  class='mydate' type="text" value="" style="width: 120px;" maxlength="100" name="enddate" readonly=""></td></tr>
                    <tr><td class="myalertheader">Time</td>
                        <td class="pdg-left-2"><table><tr><td>
                                        hrs<select name="hrs" id="hrs" >
                                            <%for (int i = 00; i < 24; i++) {%>
                                            <option  value="<%=i%>"><%=i%></option>
                                            <%}%>
                                        </select></td>
                                    <td>mins
                                        <select name="mins" id="mins">
                                            <%for (int i = 00; i < 60; i++) {%>
                                            <option  value="<%=i%>"><%=i%></option>
                                            <%}%>
                                        </select></td></tr></table>
                        </td>
                    </tr>
                    <% if (!alist.get(1).toString().equalsIgnoreCase("PRG_DATE_RANGE")) {%>
                    <tr>
                        <td class="myalertheader">Period</td>
                        <td class="pdg-left-2">
                            <select id="Data" name="Data" >
                                <option value="Current Day">Current Day</option>
                                <option value="Previous Day">Previous Day</option>
                                <option value="Report Date">Report Date</option>
                            </select>
                        </td>
                    </tr>
                    <%}%>
                    <tr>
                        <td class="myalertheader">Frequency</td>
                        <td  class="pdg-left-2">
                            <select id="frequency" name="frequency" onchange="checkFrequency(this.id)">
                                <option value="Daily">Daily</option>
                                <option value="Weekly">Weekly</option>
                                <option value="Monthly">Monthly</option>
                            </select>
                        </td>
                    </tr>
                    <tr id="weekday" style="disaply:none;">
                        <td class="myalertheader">Week Day</td>
                        <td class="pdg-left-2">
                            <select id="particularDay" name="particularDay">
                                <% for (int i = 0; i < scheduleday.length; i++) {%>
                                <option value="<%=sday[i]%>"><%=scheduleday[i]%></option>
                                <%}%>
                            </select>
                        </td>
                    </tr>
                    <tr id="monthday" style="display:none;">
                        <td class="myalertheader">Month Day</td>
                        <td class="pdg-left-2">
                            <select id="monthParticularDay" name="monthParticularDay">
                                <% for (int i = 1; i <= 31; i++) {%>
                                <option value="<%=i%>"><%=i%></option>
                                <%}%>
                            </select>
                    </td>
                </tr>
                 <tr>
                                            <td colspan="2">
                                                <table border="0" style="width:100%">
                                                    <tr style='width:100%'>
                                                        <td style='width:40%'>Header Logo</td>
                                                        <td style='width:10%'><input type='checkbox' id="headerLogo2" name="headerLogo2"></td>
                                                        <td style='width:40%'>Footer Logo</td>
                                                        <td style='width:10%'><input type='checkbox' id="footerLogo2" name="footerLogo2"></td>
                                                    </tr>
                                                </table>
                        </td>
                    </tr>
                                         <tr>
                                            <td colspan="2">
                                                <table border="0" style="width:100%">
                                                    <tr style='width:100%'>
                                                        <td style='width:40%'>Optional Header</td>
                                                        <td style='width:10%'><input type='checkbox' id="optionalHeader2" name="optionalHeader2"></td>
                                                        <td style='width:40%'>Optional Footer</td>
                                                        <td style='width:10%'><input type='checkbox' id="optionalFooter2" name="optionalFooter2"></td>
                                                    </tr>
                                                </table>
                                            </td>
                                        </tr>
                                         <tr>
                                            <td colspan="2">
                                                <table border="0" style="width:100%">
                                                    <tr style='width:100%'>
                                                        <td style='width:40%'>Signature</td>
                                                        <td style='width:10%'><input type='checkbox' id="htmlSignature2" name="htmlSignature2"></td>
                                                        <td style='width:40%'></td>
                                                        <td style='width:10%'></td>
                                                    </tr>
                                                </table>
                                            </td>
                                        </tr>
                    <tr><td colspan="2">&nbsp; </td></tr>
                    <tr>
                        <td  id="saveScheduleReport" align="center"><input id="saveScheduleReport" class="alert-button-hover" type="button" onclick="SendScheduleReport()" value="Save Schedule">
                        <td id="updateScheduleReport" align="center"><input class="navtitle-hover" type="button" onclick="updateScheduleReport()" value="Update Schedule">
                    </tr>
                    <tr><td colspan="2">&nbsp; </td></tr>
                    <tr><td colspan="2" align="center"><font size="1" color="red">*</font>Please separate multiple Email Id's by comma(,).</td></tr>
                </table>
            </form>
            <input type="hidden" id="schedulerId" name="schedulerId" value="">
        </div>
        <div id="viewReportSchedule" style="display:none" title="viewSchedules">
            <iframe id="viewScheduleFrame" width="100%" height="100%" frameborder="0" src="about:blank"></iframe>
        </div>
        <div id="editReportSchedule" style="display:none" title="Edit Schedule">
            <table align="center" width="100%">
                <tr><td><input type="radio" name="scheduleOption" id="deleteSchedule" value="deleteSchedule">Delete Scheduler</td></tr>
                <tr><td><input type="radio" name="scheduleOption" id="editSchedule" value="editSchedule">Edit Scheduler Details</td></tr>
                <tr><td><input type="radio" name="scheduleOption" id="editScheduleReport" value="editScheduleReport">Edit Scheduled Reports</td></tr>
                <tr><td><input type="hidden" name="scheduleId" id="scheduleId" value=""></td></tr><br>
                <tr><td>&nbsp; </td></tr>
                <tr><td align="center"><input  class="navtitle-hover" type="button" onclick="scheduleOption()" value="Done"></tr>

            </table>
        </div>

        <div id="assignheadlineReps" title="Assing Headline Reports" style="display:none">
            <iframe  id="userAssignHeadline" NAME='userAssignHeadline' height="100%" width="100%"  frameborder="0" src='about:blank'></iframe>
        </div>
        <div id="dimTypeDiv" style="display:none">
            <table id="dimTable">
            </table>
        </div>
        <div id="advanceParamDiv" name="advanceParamDiv" title="Advance Paramters">
            <form id="advanceParamFrm" name="advanceParamFrm" action="" method="post"></form>
        </div>
        <div id="viewFactformulaDiv" title="View Fact Formula" style="display: none"></div>
        <div id="viewFilterEdit" name="viewFilterEdit" title="Parameter View/Filter Edit" style="display: none">
            <iframe id="viewFilterEditFrame" NAME='viewFilterEditFrame' width="100%" height="100%" frameborder="0" SRC='about:blank'></iframe>
        </div>
        <div id="AddMoreParamsDiv" title="Add More Dimension ">
            <iframe  id="addmoreParamFrame" name='addMoreParamFrame' width="100%" height="100%" frameborder="0"   src='about:blank'></iframe>
        </div>
        <div id="dashboardProp1" title="Dashboard Properties"  style="display: none"></div>
        <div id="hideLabel" title="Hide Labels"> </div>

        <div id="removeMoreParamsDiv" title="Remove More Dimension ">

        </div>
        <div id="sequnceParamsDiv" title="Sequence Dimension ">

        </div>




        <div id="AddInnerViewbysDiv" title="Add Inner Viewbys" style="display: none">

        </div>
        <div id="viewResetParameterDiv" title="View Reset Parameter" style="display: none">
            <form action=""  name="ResetParamsForm" id="ResetParamsForm" method="post">
                <table id="viewResetParamId" align="right">

                    <thead>
                        <tr>
                            <th class="navtitle-hover" > SequenceNo</th>
                            <th class="navtitle-hover" >ParameterName</th>
                            <th class="navtitle-hover">Type</th>


                        </tr>
                    </thead>
                    <tbody id="viewparamId">

                    </tbody>
                    <tfoot id="viewParamfoot">

                    </tfoot>

                </table>

            </form></div>
        <div id="reportDrillDiv" name="reportDrillDiv" title="Assigning Report To Report Drill">
            <form id="reportDrillFrm" name="reportDrillFrm" action="" method="post"></form>
        </div>
        <div id="DimenssionDrillDiv" name="DimenssionDrillDiv" title="DimenssionDrill">
            <form id="DimenssionDrillFrm" name="DimenssionDrillFrm" action="" method="post"></form>
        </div>

        <div id="createBucketdiv" style="display: none" title="Add Segment">
            <iframe  id="bucketDisp1" NAME='bucketDisp1'  frameborder="0"   height="100%" width="100%"  SRC=''></iframe>
        </div>

        <%
            HashMap parametersMap = null;
            parametersMap = container.getParametersHashMap();
            if (session.getAttribute("USERID") == null || String.valueOf(screenMap.get("Redirect")).equalsIgnoreCase("Yes")) {
                response.sendRedirect(request.getContextPath() + "/newpbLogin.jsp");
            } else {
                String userId = String.valueOf(request.getSession(false).getAttribute("USERID"));
                String userFolderId = String.valueOf(parametersMap.get("UserFolderIds"));
                String reportId = request.getParameter("REPORTID");
                String reportType = "R";
                String sourcePage = "Viewer";
                reportName = container.getReportName();
                String oldReportId = reportId;
                if (sourcePage.equalsIgnoreCase("viewer")) {
                    sourcePage = "Viewer";
                } else {
                    sourcePage = "Designer";
                }
                String isWhatIfReport = "";
                String FavQuery = null;
                if (request.getAttribute("isWhatIfReport") != null) {
                    isWhatIfReport = (String) request.getAttribute("isWhatIfReport");
                }
                if (request.getAttribute("FavQuery") != null) {
                    FavQuery = (String) request.getAttribute("FavQuery");
                }

        %>


        <script type="text/javascript">
          
            var isDashboardOrReport='report';
            var y='';
            var msrArray=new Array();
            var msrArray1=new Array();
            var xmlHttp;
            $(document).ready(function() {
                $("#myList3").treeview({
                    animated:"slow",
                    persist: "cookie"
                });
            });

            $(function() {

                $(".myDragTabs").draggable({
                    helper:"clone",
                    effect:["", "fade"]
                });

                $("#dropTabs").droppable({
                    activeClass:"blueBorder",
                    accept:'.myDragTabs',
                    drop: function(ev, ui) {
                        createColumn(ui.draggable.attr('id'),ui.draggable.html());
                    }
                });

                $(".myDragTabs1").draggable({
                    helper:"clone",
                    effect:["", "fade"]
                });

                $("#dropTabs1").droppable({
                    activeClass:"blueBorder",
                    accept:'.myDragTabs1',
                    drop: function(ev, ui) {
                        createColumn1(ui.draggable.attr('id'),ui.draggable.html());
                    }
                });
            });
            function savereportDetails(reportId,reportType,userId,userFolderId,grpIds){
                dispReports(reportId,reportType,userId,userFolderId,grpIds);
            }


            function getDisplayTables(ctxpath,paramslist){
                var check = $("#tableList").is(":checked")
                if($("#tableList").is(":checked")){
                    $("#tabListDiv").hide();
                    $("#tablistLink").hide();
                    $("#goButton").hide();
                    $("#tabsListIds").val("");
                    $("#tabsListVals").val("");
                    $.post(ctxpath+"/reportViewer.do?reportBy=setTableListToContainer&repId="+'<%= PbReportId%>',
                    function(data){
                        document.getElementById("tableColsFrame").src="TableDisplay/PbChangeTableColumnsRT.jsp?loadDialogs=true&currentReportId="+'<%= PbReportId%>'+"&currentBizRoles="+'<%=rolesid%>'+'&tableList=true'+"";
                    });

                }else{
                    $("#tabListDiv").show();
                    $("#tablistLink").show();
                    $("#goButton").show();
                    var htmlVar = "<table>";
                    $.post(ctxpath+"/reportViewer.do?reportBy=getTableList&paramslist="+paramslist+'&currentBizRoles='+'<%=rolesid%>',
                    function(data){
                        var jsonVar=eval('('+data+')')
                        var json1 = jsonVar.idsList.split(",");
                        var jsonname = jsonVar.namesList.split(",");
                        for(var i=0;i<json1.length;i++){
                            if(json1[i].replace(" ", "", "gi") != "0")
                                htmlVar += "<tr><td id='"+json1[i]+"' onclick=\"selectTables('"+json1[i]+"','"+jsonname[i]+"')\">"+jsonname[i]+"</td></tr>";
                        }
                        htmlVar += "</table>";
                        $("#paramVals").html(htmlVar);
                        document.getElementById("tableColsFrame").src="TableDisplay/PbChangeTableColumnsRT.jsp?loadDialogs=true&currentReportId="+'<%= PbReportId%>'+"&currentBizRoles="+'<%=rolesid%>'+"";
                    });

                }
            }
            function showList(ctxpath,paramslist){
                if(document.getElementById("paramVals").style.display=='none'){
                    $("#paramVals").show();
                    $("#tabListDiv").show();
                    $("#tablistLink").show();
                    $("#goButton").show();
                    var htmlVar = "<table>";
                    $.post(ctxpath+"/reportViewer.do?reportBy=getTableList&paramslist="+paramslist+'&currentBizRoles='+'<%=rolesid%>',
                    function(data){
                        var jsonVar=eval('('+data+')')
                        var json1 = jsonVar.idsList.split(",");
                        var jsonname = jsonVar.namesList.split(",");
                        for(var i=0;i<json1.length;i++){
                            if(json1[i].replace(" ", "", "gi") != "0")
                                htmlVar += "<tr><td id='"+json1[i]+"' onclick=\"selectTables('"+json1[i]+"','"+jsonname[i]+"')\">"+jsonname[i]+"</td></tr>";
                        }
                        htmlVar += "</table>";
                        $("#paramVals").html(htmlVar);

                    });
                }else{
                    $("#paramVals").hide();
                }
            }
            function selectTables(tdId,tname){
                document.getElementById(tdId).style.display='none';
                if($("#tabsListVals").val() == ""){
                    $("#tabsListVals").val(tname)
                    $("#tabsListIds").val(tdId)
                }else{
                    var Ids = $("#tabsListIds").val()+","+tdId
                    var value = $("#tabsListVals").val()+","+tname
                    $("#tabsListIds").val(Ids)
                    $("#tabsListVals").val(value)
                }
            }
            function setValueToContainer(ctxpath,repId,bizRoles){
                $("#paramVals").hide();
                var tabLst = $("#tabsListIds").val();
                $("#tabsListVals").val('')
                $("#tabsListIds").val('')
                $.post(ctxpath+"/reportViewer.do?reportBy=setTableListToContainer&repId="+repId+'&tabLst='+tabLst,
                function(data){
                    document.getElementById("tableColsFrame").src="TableDisplay/PbChangeTableColumnsRT.jsp?loadDialogs=true&currentReportId="+repId+"&currentBizRoles="+bizRoles+"";
                });
            }
            function getgraphDisplayTables(ctxpath,paramslist){
                var check = $("#graphtableList").is(":checked")
                if($("#graphtableList").is(":checked")){
                    $("#graphtabListDiv").hide();
                    $("#graphtablistLink").hide();
                    $("#graphgoButton").hide();
                    $("#graphtabsListIds").val("");
                    $("#graphtabsListVals").val("");
                    $.post(ctxpath+"/reportViewer.do?reportBy=setTableListToContainer&repId="+'<%= PbReportId%>',
                    function(data){
                        document.getElementById("graphColsFrame").src="TableDisplay/PbChangeGraphColumnsRT.jsp?loadDialogs=true&tableList=true&REPORTID="+'<%= PbReportId%>';
                    });

                }else{
                    $("#graphtabListDiv").show();
                    $("#graphtablistLink").show();
                    $("#graphgoButton").show();
                    var htmlVar = "<table>";
                    $.post(ctxpath+"/reportViewer.do?reportBy=getTableList&paramslist="+paramslist+'&currentBizRoles='+'<%=rolesid%>',
                    function(data){
                        var jsonVar=eval('('+data+')')
                        var json1 = jsonVar.idsList.split(",");
                        var jsonname = jsonVar.namesList.split(",");
                        for(var i=0;i<json1.length;i++){
                            if(json1[i].replace(" ", "", "gi") != "0")
                                htmlVar += "<tr><td id='"+json1[i]+"' onclick=\"graphselectTables('"+json1[i]+"','"+jsonname[i]+"')\">"+jsonname[i]+"</td></tr>";
                        }
                        htmlVar += "</table>";
                        $("#graphparamVals").html(htmlVar);
                        document.getElementById("graphColsFrame").src="TableDisplay/PbChangeGraphColumnsRT.jsp?loadDialogs=true&REPORTID="+'<%= PbReportId%>';
                    });

                }
            }
            function graphshowList(ctxpath,paramslist){
                if(document.getElementById("graphparamVals").style.display=='none'){
                    $("#graphparamVals").show();
                    $("#graphtabListDiv").show();
                    $("#graphtablistLink").show();
                    $("#graphgoButton").show();
                    var htmlVar = "<table>";
                    $.post(ctxpath+"/reportViewer.do?reportBy=getTableList&paramslist="+paramslist+'&currentBizRoles='+'<%=rolesid%>',
                    function(data){
                        var jsonVar=eval('('+data+')')
                        var json1 = jsonVar.idsList.split(",");
                        var jsonname = jsonVar.namesList.split(",");
                        for(var i=0;i<json1.length;i++){
                            if(json1[i].replace(" ", "", "gi") != "0")
                                htmlVar += "<tr><td id='"+json1[i]+"' onclick=\"graphselectTables('"+json1[i]+"','"+jsonname[i]+"')\">"+jsonname[i]+"</td></tr>";
                        }
                        htmlVar += "</table>";
                        $("#graphparamVals").html(htmlVar);

                    });
                }else{
                    $("#graphparamVals").hide();
                }

            }
            function graphselectTables(tdId,tname){
                document.getElementById(tdId).style.display='none';
                if($("#graphtabsListVals").val() == ""){
                    $("#graphtabsListVals").val(tname)
                    $("#graphtabsListIds").val(tdId)
                }else{
                    var Ids = $("#graphtabsListIds").val()+","+tdId
                    var value = $("#graphtabsListVals").val()+","+tname
                    $("#graphtabsListIds").val(Ids)
                    $("#graphtabsListVals").val(value)
                }
            }
            function graphsetValueToContainer(ctxpath,repId,bizRoles){
                $("#graphparamVals").hide();
                var tabLst = $("#graphtabsListIds").val();
                $("#graphtabsListVals").val('')
                $("#graphtabsListIds").val('')
                $.post(ctxpath+"/reportViewer.do?reportBy=setTableListToContainer&repId="+repId+'&tabLst='+tabLst,
                function(data){
                    document.getElementById("graphColsFrame").src="TableDisplay/PbChangeGraphColumnsRT.jsp?loadDialogs=true&REPORTID="+repId;
                });
            }

            function createColumn1(elmntId,elementName){

                var parentUL=document.getElementById("sortable1");

                var x=msrArray1.toString();
                if(msrArray1.indexOf(elmntId)<0){
                    msrArray1.push(elmntId)

                    var childLI=document.createElement("li");
                    var uid=elmntId.split("~");
                    childLI.id=uid[1];
                    childLI.style.width='180px';
                    childLI.style.color='white';
                    childLI.className='navtitle-hover';
                    var table=document.createElement("table");
                    table.id=elmntId;
                    var row=table.insertRow(0);
                    var cell1=row.insertCell(0);


                    var a=document.createElement("a");
                    a.href="javascript:deleteColumn1('"+uid[1]+"')";
                    a.innerHTML="a";
                    a.className="ui-icon ui-icon-close";
                    cell1.appendChild(a);
                    var cell2=row.insertCell(1);

                    cell2.style.color='black';
                    cell2.innerHTML=elementName;
                    childLI.appendChild(table);
                    parentUL.appendChild(childLI);
                }else
                {
                    alert("Already Assigned")
                }
                $("#sortable1").sortable();
                $("#sortable1").disableSelection();
            }


            function deleteColumn1(index){
                var LiObj=document.getElementById(index);
                var parentUL=document.getElementById("sortable1");
                parentUL.removeChild(LiObj);
                index='u~'+index;
                var l=msrArray1.indexOf(index);
                msrArray1.splice(l,1);
            }

            function cancelreportDetails(sourcepage){

                if(sourcepage=="Designer"){
                    document.forms.myFormDetails.action="reportViewer.do?reportBy=backToReportDesigner&REPORTID=<%=reportId%>";

                    document.forms.myFormDetails.submit();
                }else{
                    document.forms.myFormDetails.action="reportViewer.do?reportBy=viewReport&REPORTID=<%=oldReportId%>";

                    document.forms.myFormDetails.submit();
                }
            }
            function checkExist(chk){
                var chkarr=chk.split("*");
                for(var i=0;i<chkarr.length;i++){
                    msrArray1.push(chkarr[i]);
                }
            }
            // added by krishan pratap
                function showRepPrevState1(pbreportid){

                     var url= "";
//                     if(repPrevStateCnt == 0)
//                         repPrevStateCnt = repPrevStateCnt
//                     else
//                     repPrevStateCnt = repPrevStateCnt + 1 ;
                     url = 'reportViewer.do?reportBy=viewReport&REPORTID='+pbreportid+'&action=paramChange'+'&repPrevStateCnt='+<%=container.prevStateCnt%>+'&ShowPrevState=true';
                     submiturls(url);

                 }
                 function displayFunction(data1,displ){
                 $("#grandTotalDiv").html("");
                     document.getElementById("grandTotalDiv").style.display=displ;
                 $("#grandTotalDiv").html(data1);
                 }
        </script>
        <div id="moreCompanyDiv" title="Companies" style="display:none;">
            <table id="moreCompTable" class="tablesorter" cellpadding="0" cellspacing="0" align="center">
                <tr>
                    <td id="moreCompTab" name="moreCompTab">

                    </td>
                </tr>
            </table>
            <button type="button" onclick="setSelectedCompanyId()">DONE</button>
        </div>

        <div id="multipleReportMsrDrill" style="display:none">
            <form id="multipleReportMsrDrillFrm"></form>
        </div>
        <div id="initializeRepDiv" name="initializeRepDiv" title="Initialize Report">
            <form id="initializeRepForm" name="initializeRepForm" action="" method="post"></form>
        </div>
        <div id="modifyMeasureDiv" name="modifyMeasureDiv" title="Modify Measure">
            <form id="modifyMeasureForm" name="modifyMeasureForm" action="" method="post"></form>
        </div>
        <div id="modifyMeasureAttrDiv" name="modifyMeasureAttrDiv" title="Modify Measure Attribute">
            <form id="modifyMeasureAttrForm" name="modifyMeasureAttrForm" action="" method="post"></form>
        </div>
        <div id="editBucketDiv" title="Edit Bucket Details " STYLE='display:none;'>
            <iframe  id="editBucketframe" NAME='editBucketframe'    frameborder="0" height="100%" width="100%" SRC="about:blank"></iframe>
        </div>
        <div id="ReportDrillPopUpDiv" style="display: none">

        </div>
        <div id="DimFilterBy" style="display:none;"></div>

        <div id='loadingmetadata' class='loading_image' style="display:none;">
            <img alt=""  id='imgId' src='images/help-loading.gif'  border='0px' style='position:absolute; left: 450px; top: 150px;'/>
        </div>

        <div id="quickRefreshDiv" style="display:none;" title="Auto Quick Refresh">
            <form action="" name="refreshForm" id="refreshForm" method="post">
                <table width="100%">
                    <tr><td class="myalertheader">Auto Refresh</td>
                        <td><select id="refreshId" name="refreshId"  style="width:50px" onchange="checkRefreshSelection(this.id)">
                                <option value="true">Yes</option>
                                <option value="false">No</option>
                            </select></td></tr>
                    <tr id="autoRefreshTr" ><td colspan="2"><table width="100%">
                                <tr><td class="myalertheader">StartDate</td><td><input id="stDatepicker"  type="text" value="" style="width: 120px;" maxlength="100" name="startdate" readonly=""></td></tr>
                                <tr><td class="myalertheader">End Date</td><td><input id="etDatepicker"  type="text" value="" style="width: 120px;" maxlength="100" name="enddate" readonly=""></td></tr>
                                <tr><td class="myalertheader">Time</td>
                                    <td><table><tr><td>
                                                    hrs<select name="hours" id="hours" >
                                                        <%for (int i = 00; i < 24; i++) {%>
                                                        <option  value="<%=i%>"><%=i%></option>
                                                        <%}%>
                                                    </select></td>
                                                <td>mins
                                                    <select name="mints" id="mints">
                                                        <%for (int i = 00; i < 60; i++) {%>
                                                        <option  value="<%=i%>"><%=i%></option>
                                                        <%}%>
                                                    </select></td></tr></table>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="myalertheader">Frequency</td>
                                    <td><select id="refreshFrequency"  style="width:120px" onchange="checkRefreshFrequency(this.id)" name="refreshFrequency">
                                            <%for (int i = 0; i < frequency.length; i++) {%>
                                            <option value="<%=frequency[i]%>"><%=frequency[i]%></option>
                                            <%}%>
                                        </select></td>
                                </tr>
                                <tr id="refreshweekday" style="disaply:none;">
                                    <td class="myalertheader">Week Day</td>
                                    <td>
                                        <select id="refreshparticularDay" name="refreshparticularDay">
                                            <% for (int i = 0; i < scheduleday.length; i++) {%>
                                            <option value="<%=sday[i]%>"><%=scheduleday[i]%></option>
                                            <%}%>
                                        </select>
                                    </td>
                                </tr>
                                <tr id="refreshmonthday" style="display:none;">
                                    <td class="myalertheader">Month Day</td>
                                    <td>
                                        <select id="refreshmonthParticularDay" name="refreshmonthParticularDay">
                                            <% for (int i = 1; i <= 31; i++) {%>
                                            <option value="<%=i%>"><%=i%></option>
                                            <%}%>
                                        </select>
                                    </td>
                                </tr>
                                <tr id="refreshhourly" style="display:none;">
                                    <td class="myalertheader">Hourly</td>
                                    <td>
                                        <select id="hourlyParticularDay" name="hourlyParticularDay">
                                            <% for (int i = 0; i <= 23; i++) {%>
                                            <option value="<%=i%>"><%=i%></option>
                                            <%}%>
                                        </select>
                                    </td>
                                </tr>
                            </table>
                        </td></tr>
                    <tr><td colspan="2">&nbsp; </td></tr>
                    <tr><td align="right" colspan="2"><a href="javascript:disableQuickRefresh('<%=request.getContextPath()%>','<%=PbReportId%>')"><u>Disable Quick Refresh For This Report</u></a></td></tr>
                    <tr><td colspan="2">&nbsp; </td></tr>
                    <tr><td align="center" colspan="2"><input class="navtitle-hover" type="button" onclick="saveQuickRefresh('<%=request.getContextPath()%>','<%=PbReportId%>')" value="Done"></td></tr>
                </table>
                <input type="hidden" id="reportId" name="reportId" value="<%=PbReportId%>">
            </form>
        </div>
        <div id="hideMeasureDiv" style="display:none" title="Hiding Measures">
            <form id="hideMsrFrm"></form>
        </div>
        <div id="selfDrill" style="display:none"></div>
        <div id="hideViewByDiv" style="display:none" title="Hiding ViewBys">
            <iframe  id="hideViewbyFrame" NAME='hideViewbyFrame' width="100%" height="100%" frameborder="0" SRC="about:blank"></iframe>
        </div>
<!--        //added by krishan pratap-->
              <div id="importExcelFileDiv123" style="display:none" title="Upload Template Sheet">

         </div>
<div id="ReportMultiCalendar" style="display:none" title="Report Multi Calendar">
               <div id="ReportMultiCalendardata"></div>
         </div>
        <div id="importExcelFileDiv" style="display:none" title="Upload Excel Sheet Data">
        </div>
        <div id="mappingExcelFileDiv" style="display:none" title="Mapping Excel Sheet Data with Report">
        </div>
        <div id="addNewCharts" style="display:none" title="Drag Parameters">
        </div><div id="CustomerReportBugMail" style="display:none" title="Customer Report Bug Mail">
        </div>
        <div id="legendsDiv" style="display:none" title="Drag Parameters">
        </div>
        <form action="" method="POST" id="graphForm">
            <input type="hidden" id="viewby" name="viewby"/>
            <input type="hidden" id="viewbyIds" name="viewbyIds"/>
            <input type="hidden" id="groupbys" name="groupbys"/>
            <input type="hidden" name="measure" id="measure"/>
            <input type="hidden" name="measureIds" id="measureIds"/>
            <input type="hidden" name="aggregation" id="aggregation"/>
            <input type="hidden" id="graphName" name="graphName" value="<%=reportName%>"/>
            <input type="hidden" id="graphsId" name="graphsId" value="<%=PbReportId%>"/>
            <input type="hidden" id="usersId" name="usersId" value="<%=USERID%>"/>
            <input type="hidden" id="numOfCharts" name="numOfCharts"/>
            <input type="hidden" id="lines" name="lines"/>
            <input type="hidden" id="currLine" name="currLine" />
            <input type="hidden" id="currLineCharts" name="currLineCharts" />
            <input type="hidden" id="chartData" name="chartData" />
            <input type="hidden" id="drills" name="drills" />
            <input type="hidden" id="filters1" name="filters1" />
            <input type="hidden" id="filtersmapgraph" name="filtersmapgraph" />
            <input type="hidden" id="driver" name="driver" />
            <input type="hidden" id="driverList" name="driverList" />
            <input type="hidden" id="ctxpath" name="ctxpath" value="<%=request.getContextPath()%>" />
            <input type="hidden" id="drilltype" name="drilltype"  />
            <input type="hidden" id="drillFormat" name="drillFormat"  />
            <input type="hidden" id="type" name="type"  />
            <input type="hidden" id="chartType" name="chartType"  />
            <input type="hidden" id="visualChartType" name="visualChartType"  />
            <input type="hidden" id="currType" name="currType"  />
            <input type="hidden" id="changeVisualType" name="changeVisualType"  />
            <input type="hidden" id="tabDiv" name="tabDiv"  />
            <input type="hidden" id="legends" name="legends"  />
            <input type="hidden" name="conditionalMeasure" id="conditionalMeasure" />
            <input type="hidden" name="shadeType" id="shadeType" />
            <input type="hidden" name="isShaded" id="isShaded" value="false"/>
            <input type="hidden" name="conditionalMap" id="conditionalMap" />
            <input type="hidden" name="measureColor" id="measureColor"/>
            <input type="hidden" name="timeMap" id="timeMap"/>
            <input type="hidden" name="timeDetailsArray" id="timeDetailsArray"/>
            <input type="hidden" name="draggableViewBys" id="draggableViewBys"/>
            <input type="hidden" name="multigblrefresh" id="multigblrefresh"/>
            <input type="hidden" name="searchtype" id="searchtype"/>
            <!--added by shobhit for multi dashboard on 22/09/15--> 
            <input type="hidden" id="parentViewBy" name="parentViewBy"  />
            <input type="hidden" id="childViewBys" name="childViewBys"  />
            <input type="hidden" id="childMeasBys" name="childMeasBys"  />
            <input type="hidden" id="selectedViewBys" name="selectedViewBys"  />
            <input type="hidden" id="selectedMeasBys" name="selectedMeasBys"  />
        </form>
    </body>
</html>
<% }%>
<%}
    } else {
        response.sendRedirect("baseAction.do?param=logoutApplication");
    }
    long time_end = System.nanoTime();
    double seconds = (time_end - time_start) * Math.pow(10, -9);
    DataTracker datatrack = new DataTracker();
    datatrack.setclickdata(request, PbReportId, seconds);

%>
<!--Added by shivam-->
<script type="text/javascript">
    document.getElementById('loading').style.display='none';
    
     $('.mydate').datepicker({
       changeMonth: true,
       changeYear: true
   });
</script>
 <script type="text/javascript">
            
//            document.getElementsByClassName("iconImgs").style.width="10px";
            if(screen.width<1000){
//                {alert("idddd");
                  $(".iconImgs").css("width","8px");  
                }
                else
                    {
                        $(".iconImgs").css("width","13px");
//                         alert("else");
                    }
 </script>