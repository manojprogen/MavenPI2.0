
<%@page pageEncoding="UTF-8"  contentType="text/html" import="com.progen.reportview.db.PbReportViewerDAO,com.progen.report.entities.Report,com.progen.report.KPIElement,com.progen.report.entities.KPI,com.progen.report.entities.QueryDetail,com.progen.report.DashletDetail,com.progen.action.UserStatusHelper,com.progen.i18n.TranslaterHelper"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="prg.db.Session,prg.db.PbDb,com.progen.reportdesigner.db.ReportTemplateDAO,com.progen.reportdesigner.db.DashboardTemplateDAO,com.progen.users.PrivilegeManager,prg.db.DataTracker,java.awt.Font,utils.db.*,java.awt.*,java.io.*,java.util.*,java.lang.*,prg.db.Container,prg.db.PbReturnObject"%>
<%         long time_start = System.nanoTime();
String contPath=request.getContextPath();%>
<%@page import="com.progen.charts.ProGenChartUtilities,prg.util.screenDimensions,prg.util.screenDimensions,java.text.DateFormat,java.sql.*,com.progen.report.display.DisplayParameters,com.progen.report.pbDashboardCollection"%>
<jsp:useBean id="duration" scope="session" class="utils.db.ProgenParam"/>
<jsp:setProperty name="duration" property="*" />
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<jsp:useBean id="brdcrmb" class="com.progen.action.BreadCurmbBean" scope="session"/>

<%  String reportId = (String) request.getAttribute("REPORTID");
            String day0 = null, dated = null, fullName0 = null, day1 = null, date = null, fullName = null, cfday = null, cfdate = null, cffullName = null, ctday = null, ctdate = null, ctfullName = null;
            ArrayList<String> timeinfo = null;
            String[] vals1 = null;
            String vals = " ";
            Container container = Container.getContainerFromSession(request, reportId);
            pbDashboardCollection collect = (pbDashboardCollection) container.getReportCollect();
            PbReportViewerDAO pbr1 = new PbReportViewerDAO();
            pbr1.setGlobalParameters(container);
            String sytm = container.isYearCal;
            timeinfo = collect.timeDetailsArray;
            vals = timeinfo.toString();
            vals = vals.replace("[", "");
            vals = vals.replace("]", "");
            vals1 = vals.split(",");
            DateFormat formatter;
            String[] dates1 = new String[10];
            String[] dates = new String[10];
            String[] date0 = new String[10];
            String[] dats0 = new String[10];
            String[] cfdates = new String[10];
            String[] cfdates1 = new String[10];
            String[] ctdates = new String[10];
            String[] ctdates1 = new String[10];
            if (vals1[2].contains("/")) {
                date0 = vals1[2].split("/");
            }
            if (vals1[2].contains("-")) {
                dats0 = vals1[2].split(" ");
                String values = dats0[0];
                String[] repdates = values.split("-");
                date0[2] = repdates[0];
                date0[0] = repdates[1];
                date0[1] = repdates[2];
            }
            if (timeinfo.get(1).equalsIgnoreCase("PRG_DATE_RANGE")) {
                if (vals1[3].contains("/")) {
                    dates = vals1[3].split("/");
                }
                if (vals1[4].contains("/")) {
                    cfdates = vals1[4].split("/");
                }
                if (vals1[5].contains("/") && vals1[5] != "") {
                    ctdates = vals1[5].split("/");
                }
                if (vals1[3].contains("-")) {
                    dates1 = vals1[3].split(" ");
                    String values = dates1[1];
                    String[] repdates = values.split("-");
                    dates[2] = repdates[0];
                    dates[0] = repdates[1];
                    dates[1] = repdates[2];
                }
                if (vals1[4].contains("-") && !vals1[4].isEmpty()) {
                    cfdates1 = vals1[4].split(" ");
                    String values = cfdates1[1];
                    String[] repdates = values.split("-");
                    cfdates[2] = repdates[0];
                    cfdates[0] = repdates[1];
                    cfdates[1] = repdates[2];
                }
                if (vals1[5].contains("-")) {
                    ctdates1 = vals1[5].split(" ");
                    String values = ctdates1[1];
                    String[] repdates = values.split("-");
                    ctdates[2] = repdates[0];
                    ctdates[0] = repdates[1];
                    ctdates[1] = repdates[2];
                }
            }
            Calendar ca1 = Calendar.getInstance();
            if (timeinfo.get(1).equalsIgnoreCase("PRG_DATE_RANGE")) {
                ca1.set(Integer.parseInt(date0[2].substring(0, 4)), Integer.parseInt(date0[0].replace(" ", "")) - 1, Integer.parseInt(date0[1]));
                java.util.Date d0 = new java.util.Date(ca1.getTimeInMillis());
                String partialName0 = new SimpleDateFormat("MMM").format(d0);
                day0 = d0.toString().substring(0, 3);
                dated = date0[1];
                fullName0 = partialName0 + "'" + date0[2];
                // to date
                ca1.set(Integer.parseInt(dates[2].substring(0, 4)), Integer.parseInt(dates[0].replace(" ", "")) - 1, Integer.parseInt(dates[1]));
                java.util.Date d = new java.util.Date(ca1.getTimeInMillis());
                String partialName = new SimpleDateFormat("MMM").format(d);
                day1 = d.toString().substring(0, 3);
                date = dates[1];
                fullName = partialName + "'" + dates[2];
                // compare from
                ca1.set(Integer.parseInt(cfdates[2].substring(0, 4)), Integer.parseInt(cfdates[0].replace(" ", "")) - 1, Integer.parseInt(cfdates[1]));
                java.util.Date cfd = new java.util.Date(ca1.getTimeInMillis());
                String cfName = new SimpleDateFormat("MMM").format(cfd);
                cfday = cfd.toString().substring(0, 3);
                cfdate = cfdates[1];
                cffullName = cfName + "'" + cfdates[2];
                // compare to
                ca1.set(Integer.parseInt(ctdates[2].substring(0, 4)), Integer.parseInt(ctdates[0].replace(" ", "")) - 1, Integer.parseInt(ctdates[1]));
                java.util.Date ctd = new java.util.Date(ca1.getTimeInMillis());
                String ctName = new SimpleDateFormat("MMM").format(ctd);
                ctday = d.toString().substring(0, 3);
                ctdate = ctdates[1];
                ctfullName = ctName + "'" + ctdates[2];
            } else if (timeinfo.get(1).equalsIgnoreCase("PRG_STD")) {
                ca1.set(Integer.parseInt(date0[2].substring(0, 4)), Integer.parseInt(date0[0].replace(" ", "")) - 1, Integer.parseInt(date0[1]));
                java.util.Date d0 = new java.util.Date(ca1.getTimeInMillis());
                String partialName0 = new SimpleDateFormat("MMM").format(d0);
                day0 = d0.toString().substring(0, 3);
                dated = date0[1];
                fullName0 = partialName0 + "'" + date0[2];
            } else if (timeinfo.get(1).equalsIgnoreCase("PRG_DAY_ROLLING")) {
                ca1.set(Integer.parseInt(date0[2].substring(0, 4)), Integer.parseInt(date0[0].replace(" ", "")) - 1, Integer.parseInt(date0[1]));
                java.util.Date d0 = new java.util.Date(ca1.getTimeInMillis());
                String partialName0 = new SimpleDateFormat("MMM").format(d0);
                day0 = d0.toString().substring(0, 3);
                dated = date0[1];
                fullName0 = partialName0 + "'" + date0[2];
            }
%>

<% int USERID = 0;
            USERID = Integer.parseInt(String.valueOf(session.getAttribute("USERID")));
            Locale locale = null;
            locale = (Locale) session.getAttribute("userLocale");
            String[] days = {"Beg. of Week", "End of Week"};
            String[] day = {"B", "L"};
            String PbReportId = "";
            PbReportId = (String) request.getAttribute("REPORTID");
            String[] strOprtrs = {"<", ">", "<=", ">=", "=", "<>"};
            String[] kpiSymbols = {"", "$", "Rs", "Euro", "Yen", "%"};
            String[] NbrFormats = {" ", "K", "M", "L", "Cr"};
            String[] NbrFormatsDisp = {"Absolute", "Thousands(K)", "Millions(M)", "Lakhs(L)", "Crores(C)"};
            String[] roundvalue = {"0", "1", "2", "3", "4", "5"};
            String[] roundtext = {"No Decimal", "One Decimal", "Two Decimal", "Three Decimal", "Four Decimal", "Five Decimal"};
            String[] alignmentValue = {"", "left", "right", "center"};
            String[] alignmentText = {"", "Left", "Right", "Center"};
            String[] negativeValue = {"", "Bracket", "Red", "Bracket&Red"};
            String[] negativeText = {"", "In Bracket", "In Red", "In Bracket&Red"};
            String NbrFormat = "";
            //for clearing cache
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
            String themeColor = "blue";
            ServletContext context = getServletContext();
            boolean isPowerAnalyserEnableforUser = false;
            String userType = null;
            HashMap<String, UserStatusHelper> statushelper;
            statushelper = (HashMap) context.getAttribute("helperclass");
            UserStatusHelper helper = new UserStatusHelper();
            if (!statushelper.isEmpty()) {
                helper = statushelper.get(request.getSession(false).getId());
                if (helper != null) {
                    isPowerAnalyserEnableforUser = helper.getPowerAnalyser();
                    userType = helper.getUserType();
                }
            }
            container.isPowerAnalyserEnableforUser = isPowerAnalyserEnableforUser;
            boolean isAxa = Boolean.parseBoolean(context.getInitParameter("isAxa"));
            screenDimensions dims = new screenDimensions();
            int pageFont, anchorFont;
            HashMap screenMap = dims.getFontSize(session, request, response);
            if (!String.valueOf(screenMap.get("pageFont")).equalsIgnoreCase("NULL")) {
                pageFont = Integer.parseInt(String.valueOf(screenMap.get("pageFont")));
                anchorFont = Integer.parseInt(String.valueOf(screenMap.get("pageFont"))) + 1;
            } else {
                pageFont = 11;
                anchorFont = 12;
            }
            if (session.getAttribute("USERID") == null || String.valueOf(screenMap.get("Redirect")).equalsIgnoreCase("Yes")) {
                response.sendRedirect(request.getContextPath() + "/newpbLogin.jsp");
            } else {
                String ParamSectionDisplay = "";
                String buildDashBoard = "";
                String zooming = "";
                String dashboardName = "";
                String dashboardDesc = "";
                HashMap map = new HashMap();
                String uId = null;

                String leftTdDashStatus = "";
                String imageTypeDash = "control-180";
                String DashParamStatus = "block";

                if (session.getAttribute("theme") == null) {
                    session.setAttribute("theme", themeColor);
                } else {
                    themeColor = String.valueOf(session.getAttribute("theme"));
                }

                if (session.getAttribute("imageTypeDash") == null) {
                    session.setAttribute("imageTypeDash", imageTypeDash);
                } else {
                    imageTypeDash = String.valueOf(session.getAttribute("imageTypeDash"));
                }
                if (session.getAttribute("leftTdDashStatus") == null) {
                    session.setAttribute("leftTdDashStatus", leftTdDashStatus);
                } else {
                    leftTdDashStatus = String.valueOf(session.getAttribute("leftTdDashStatus"));
                }
                if (session.getAttribute("DashParamStatus") == null) {
                    session.setAttribute("DashParamStatus", DashParamStatus);
                } else {
                    DashParamStatus = String.valueOf(session.getAttribute("DashParamStatus"));
                }

                uId = String.valueOf(session.getAttribute("USERID"));
                HashMap GraphTypesHashMap = null;
                HashMap GraphClassesHashMap = null;
                String[] grpTypeskeys = new String[0];
                String[] grpClasseskeys = new String[0];

                GraphTypesHashMap = (HashMap) session.getAttribute("GraphTypesHashMap");
                GraphClassesHashMap = (HashMap) session.getAttribute("GraphClassesHashMap");

                grpTypeskeys = (String[]) GraphTypesHashMap.keySet().toArray(new String[0]);
                grpClasseskeys = (String[]) GraphClassesHashMap.keySet().toArray(new String[0]);
                PbReportId = (String) request.getAttribute("REPORTID");
                HashMap grpDataMap = (HashMap) request.getAttribute("kpiGraphData");
                if (request.getAttribute("ParamSectionDisplay") != null) {
                    ParamSectionDisplay = String.valueOf(request.getAttribute("ParamSectionDisplay"));
                    session.setAttribute("ParamSectionDisplay", ParamSectionDisplay);
                }
                if (request.getAttribute("buildDashBoard") != null) {
                    buildDashBoard = String.valueOf(request.getAttribute("buildDashBoard"));
                }
                if (request.getAttribute("zooming") != null) {
                    zooming = String.valueOf(request.getAttribute("zooming"));
                }
                if (request.getSession(false).getAttribute("PROGENTABLES") != null) {
                    map = (HashMap) request.getSession(false).getAttribute("PROGENTABLES");
                }
                if (map.get(PbReportId) != null) {
                    container = (prg.db.Container) map.get(PbReportId);
                } else {
                    container = new prg.db.Container();
                }

                if (container.getDbrdName() != null) {
                    dashboardName = container.getDbrdName();
                    dashboardDesc = container.getDbrdDesc();
                }
                ArrayList alist = container.getReportCollect().timeDetailsArray;
                HashMap parametersMap = container.getParametersHashMap();
                DashboardTemplateDAO dashboardDAO = new DashboardTemplateDAO();
                PbReturnObject DrillretObj = new PbReturnObject();
                String ddformT = null;
                if (session.getAttribute("dateFormat") != null) {
                    ddformT = session.getAttribute("dateFormat").toString();
                }
                String[] scheduleday = {"Mon", "Tue", "Wed", "Thur", "Fri", "Sat", "Sun"};
                String[] sday = {"2", "3", "4", "5", "6", "7", "1"};
                PbDb pbdb = new PbDb();
                String rolesidqry = "select folder_id from prg_ar_report_details where report_id=" + PbReportId;
                PbReturnObject roleobj = pbdb.execSelectSQL(rolesidqry);
                String rolesid = roleobj.getFieldValueString(0, 0);
                String tag_id_name = "Report Category";
                String tagName_qry = "select tag_name from PRG_TAG_MASTER where Tag_Id in(select TAG_ID  from PRG_TAG_REPORT_ASSIGNMENT where TAG_SHORT_DESC='" + container.getDbrdName() + "')";
                PbReturnObject tagNameObj = pbdb.execSelectSQL(tagName_qry);
                if (!tagNameObj.getFieldValueString(0, 0).equalsIgnoreCase("") && tagNameObj.getFieldValueString(0, 0) != null) {
                    tag_id_name = tagNameObj.getFieldValueString(0, 0);
                }
%>

<% String mapMenustatus = "block";
                String dashletsColumnstatus = "block";
                if (session.getAttribute("mapMenustatus") == null) {
                    session.setAttribute("mapMenustatus", mapMenustatus);
                } else {
                    mapMenustatus = String.valueOf(session.getAttribute("mapMenustatus"));
                }
                if (session.getAttribute("dashletsColumnstatus") == null) {
                    session.setAttribute("dashletsColumnstatus", dashletsColumnstatus);
                } else {
                    dashletsColumnstatus = String.valueOf(session.getAttribute("dashletsColumnstatus"));
                }
                String folderdetailsqquery = " select distinct FOLDER_ID from PRG_AR_REPORT_DETAILS  where REPORT_ID =" + PbReportId;
                StringBuffer foldernames = new StringBuffer();
                String folderDetails = "";
                PbDb reportDetail = new PbDb();
                PbReturnObject retObj = new PbReturnObject();
                try {
                    retObj = reportDetail.execSelectSQL(folderdetailsqquery);
                    for (int j = 0; j < retObj.getRowCount(); j++) {
                        foldernames = foldernames.append(retObj.getFieldValueString(j, 0));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                folderDetails = foldernames.toString();%>


<%
//get no of graphs in the dashboard...
                String GraphsCountInDash = null;
                String dashdoardGraphCountQuery = " select max(DISPLAY_SEQUENCE) from PRG_AR_DASHBOARD_DETAILS  where DASHBOARD_ID =" + PbReportId;
                PbDb GraphCountConnection = new PbDb();
                PbReturnObject retObj1 = new PbReturnObject();
                try {
                    retObj1 = GraphCountConnection.execSelectSQL(dashdoardGraphCountQuery);
                    GraphsCountInDash = retObj1.getFieldValueString(0, 0);
                    if (GraphsCountInDash.equalsIgnoreCase("")) {
                        GraphsCountInDash = "0";
                    }
                    GraphsCountInDash = String.valueOf((Integer.parseInt(GraphsCountInDash)) + 1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String NewDashboardId = null;
                String NewDasboardIdQuery = " select max(REPORT_ID) from PRG_AR_REPORT_DETAILS";
                PbDb PbDbobj8 = new PbDb();
                ArrayList arlist8 = new ArrayList();
                PbReturnObject retObj8 = new PbReturnObject();
                try {
                    retObj8 = PbDbobj8.execSelectSQL(NewDasboardIdQuery);
                    for (int j = 0; j < retObj8.getRowCount(); j++) {
                        String str = (retObj8.getFieldValueString(0, 0));
                        NewDashboardId = String.valueOf(((Integer.parseInt(str)) + 1));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                ArrayList params = new ArrayList();
                ArrayList timeparams = new ArrayList();
                ArrayList timedetails = new ArrayList();
                //added for sticky notes
                String currentURL = "";
                HashMap paramMap = new HashMap();
                String parameters = "";
                String allParameters = "";
                String timeLevelStr = "";
                String minTimeLevel = "";
                String userFolders = "";
                DashboardTemplateDAO dbrdDAO = new DashboardTemplateDAO();
                //end
                HashMap hm = container.getTimeParameterHashMap();
                ArrayList timeDetails = (ArrayList) hm.get("TimeDetailstList");
                ArrayList timeArray = (ArrayList) hm.get("TimeDetailstList");
                HashMap hmobj = container.getReportParameterHashMap();
                HashMap reportParametersValues = (HashMap) hmobj.get("reportParametersValues");
                HashMap timeDetsMap = (HashMap) hm.get("TimeDimHashMap");
                String[] BuildedParamsWithTime = null;
                String[] BuildedParams = null;
                String getVal = null;
                ArrayList alistt = new ArrayList();
                if (timeDetsMap != null) {
                    BuildedParamsWithTime = (String[]) timeDetsMap.keySet().toArray(new String[0]); //contains 31150,31149,31156,31153
                    for (int i = 0; i < BuildedParamsWithTime.length; i++) {
                        timeparams.add(BuildedParamsWithTime[i]);
                    }
                }
                if (timeDetsMap != null) {
                    Set<String> repViewByIds = timeDetsMap.keySet();
                    for (String viewById : repViewByIds) {
                        ArrayList timeDetailst = (ArrayList) timeDetsMap.get(viewById);
                        if (getVal != null) {
                            getVal = getVal + viewById + "~" + timeDetailst + "@";
                        } else {
                            getVal = viewById + "~" + timeDetailst + "@";
                        }
                    }
                }
                if (reportParametersValues != null) {
                    BuildedParams = (String[]) reportParametersValues.keySet().toArray(new String[0]);
                    for (int j = 0; j < BuildedParams.length; j++) {
                        params.add(BuildedParams[j]);
                    }
                }
                String viewSelectValue = "";
                String sortValue = "";
                String dimensionValue = "";
                if (String.valueOf(session.getAttribute(PbReportId + "_" + "sortType")) != null) {
                    sortValue = String.valueOf(session.getAttribute(PbReportId + "_" + "sortType"));
                }
                if (String.valueOf(session.getAttribute(PbReportId + "_" + "mapView")) != null) {
                    viewSelectValue = String.valueOf(session.getAttribute(PbReportId + "_" + "mapView"));
                }
                if (String.valueOf(session.getAttribute(PbReportId + "_" + "DimensionSelect")) != null) {
                    dimensionValue = String.valueOf(session.getAttribute(PbReportId + "_" + "DimensionSelect"));
                }
                session.setAttribute("timeDetsMap", timeDetsMap);
                session.setAttribute("params", params);
                session.setAttribute("timeDetsMap", timeDetsMap);
                session.setAttribute("folderDetails", folderDetails);
                session.setAttribute("NewDashboardId", NewDashboardId);
                session.setAttribute("timeparams", timeparams);
                session.setAttribute("timeDetails", timeDetails);
                if (request.getAttribute("currentURL") != null) {
                    currentURL = String.valueOf(request.getAttribute("currentURL"));
                }
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
                    if (hm != null && hm.size() > 0) {
                        //   params = (ArrayList) hm.get("Parameters");
                        params = (ArrayList) session.getAttribute("params");
                        if (!params.isEmpty()) {
                            parameters = params.get(0).toString();

                            for (int i = 0; i < params.size(); i++) {
                                allParameters = allParameters + "," + params.get(i);
                            }
                        }
                        if (!allParameters.equalsIgnoreCase("")) {
                            allParameters = allParameters.substring(1);
                        }
                        timeArray = (ArrayList) hm.get("TimeDetailstList");
                    }
                }
                String userid = String.valueOf(session.getAttribute("USERID"));
                String ctxPath = request.getContextPath();
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
                String sqlString = null;
                sqlString = container.getSqlStr();
                if (sqlString != null && sqlString != "") {
                    sqlString = sqlString.replace("\"", "").replace("'", "~");
                }
                HashMap map1 = collect.reportParameters;
                ArrayList al11 = null;
                ArrayList<String> onlyname = new ArrayList<String>();
                String[] a11 = null;               //  for(int i=0; i <map.size() ; i++){ ;
                if (map1 != null && (!map1.isEmpty())) {
                    a11 = (String[]) (map1.keySet()).toArray(new String[map1.size()]);
                    if (map1.size() > 2) {
                        for (int j = 0; j < map1.size(); j++) {
                            al11 = (ArrayList) map1.get(a11[j]);
                            onlyname.add(al11.get(1).toString().replaceAll(" ", "1q1"));
                        }
                    } else {
                        for (int j = 0; j < map1.size(); j++) {
                            al11 = (ArrayList) map1.get(a11[j]);
                            onlyname.add(al11.get(1).toString().replaceAll(" ", "1q1"));
                        }
                    }
                }%>
<html>
    <head>
        <title><bean:message key="ProGen.Title"/></title>
        <script type="text/javascript" src="<%=contPath%>/javascript/lib/jquery/js/jquery-1.4.2.min.js"></script>
        <script type="text/javascript" src="<%=contPath%>/javascript/lib/jquery/js/jquery-1.7.2.min.js"></script>
        <script type="text/javascript" src="<%= contPath%>/JS/jquery-ui.min.js"></script>
        <script type="text/javascript" src="<%= contPath%>/JS/jquery.multiselect.js"></script><!--
        -->        <script type="text/javascript" src="<%= contPath%>/JS/jquery.multiselect.filter.js"></script>
        <script type="text/javascript" src="<%=contPath%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>
        <link type="text/css" href="<%=contPath%>/stylesheets/themes/<%=themeColor%>/jquery-ui-1.7.3.custom.css" rel="stylesheet" />
        <link type="text/css" href="<%=contPath%>/stylesheets/themes/<%=themeColor%>/metadataButton.css" rel="stylesheet" />
        <link type="text/css" href="<%=contPath%>/stylesheets/themes/<%=themeColor%>/pbReportViewerCSS.css" rel="stylesheet" />
        <link type="text/css" href="<%=contPath%>/jQuery/jquery/themes/base/ui.all.css" rel="stylesheet" />
        <link type="text/css" href="<%=contPath%>/jQuery/jquery/themes/base/ui.theme.css" rel="stylesheet" />
        <link type="text/css" href="<%=contPath%>/css/css.css" rel="stylesheet" />
        <link type="text/css" href="<%=contPath%>/css/style.css" rel="stylesheet" />
        <link type="text/css" href="<%=contPath%>/stylesheets/newDashboard.css" rel="stylesheet" />
        <link rel="stylesheet" type="text/css" href="<%=contPath%>/stylesheets/tablesorterStyle.css" />
        <link type="text/css" href="<%=contPath%>/jQuery/jquery/themes/base/BreadCrumb.css" rel="stylesheet" />
        <link type="text/css" href="<%=contPath%>/jQuery/jquery/themes/base/Base.css" rel="stylesheet" />
        <script type="text/javascript" src="<%=contPath%>/jQuery/jquery/ui/ui.core.js"></script>
        <script type="text/javascript" src="<%=contPath%>/jQuery/jquery/external/bgiframe/jquery.bgiframe.js"></script>
        <script type="text/javascript" src="<%=contPath%>/javascript/pbReportViewerJS.js"></script>

        <script type="text/javascript" src="<%=contPath%>/JS/stickyNote.js"></script>
        <script type="text/javascript" src="<%=contPath%>/javascript/reportviewer/ReportViewer.js"></script>
        <script type="text/javascript" src="<%=contPath%>/javascript/reportviewer/graphViewer.js"></script>
        <script type="text/javascript" src="<%=contPath%>/javascript/jquery.tablesorter.js"></script>
        <script type="text/javascript" src="<%=contPath%>/javascript/dashboardDesign.js"></script>
        <script type="text/javascript" src="<%=contPath%>/jQuery/jquery/ui/ui.sortable.js"></script>
        <script type="text/javascript" src="<%=contPath%>/jQuery/jquery/ui/ui.dialog.js"></script>
        <script type="text/javascript" src="<%=contPath%>/jQuery/jquery/ui/jquery.cookie.js"></script>
        <script type="text/javascript" src="<%=contPath%>/javascript/stickyNote.js"></script>
        <script type="text/javascript" src="<%=contPath%>/jQuery/jquery/ui/jquery.easing.1.3.js"></script>
        <script type="text/javascript" src="<%=contPath%>/jQuery/jquery/ui/jquery.jBreadCrumb.1.1.js"></script>
        <link href="<%=contPath%>/reportDesigner.css" type="text/css" rel="stylesheet">
        <script type="text/javascript" src="<%=contPath%>/javascript/overlib.js"></script>
        <script type="text/javascript" src="<%=contPath%>/javascript/dashboardDesignerViewer.js"></script>
        <script src="<%=contPath%>/javascript/lib/jquery/js/jquery.tablesorter.mod.js" type="text/javascript"></script>
        <script src="<%=contPath%>/javascript/lib/jquery/js/jquery.tablesorter.collapsible.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=contPath%>/javascript/lib/jquery/js/jquery.tablesorter.innergrid.js"></script>
        <script type="text/javascript"  src="<%=contPath%>/tracker/JS/dateSelection.js"></script>
        <link type="text/css" href="<%=contPath%>/stylesheets/themes/<%=themeColor%>/ReportCss.css" rel="stylesheet"/>
        <script type="text/javascript" src="<%= contPath%>//dragAndDropTable.js"></script>
        <link type="text/css" href="<%=contPath%>/datedesign.css" rel="stylesheet"/>

        <script type="text/javascript" src="<%=contPath%>/jQuery/d3/coloring.js"></script>
        <script type="text/javascript" src="<%=contPath%>/jQuery/d3/jscolor.js"></script>
        <link rel="stylesheet" type="text/css" href="css/jquery.multiselect.css" />
        <link rel="stylesheet" type="text/css" href="css/jquery.multiselect.filter.css" />
        <style>
            #fixedtop1 { position: fixed; top: 0px; left:200px; margin: auto; right: 800px; width: 50px; border: none; }
            #center250a { width: auto;height: 65px;  background:none; }
            #center250b { width: auto;height: 65px;   background:none; }
            .viewbyname{ vertical-align:middle;}
            #arrowL{width:20px; height:10px;  float:left;cursor:pointer;}
            #arrowR{width:20px;height:10px;float:right;cursor:pointer;}
            #resetgraph{width:40px;height:10px;float:right;cursor:pointer;}
            #list-container {overflow:hidden;width:65vw;height:30px;float:left;}
            .item{ margin:5px; float:left; position:relative;}
            #gottabId11{ width:20px; height:10px; float:right; cursor:pointer; }
            .list1{min-width:15000px; float:left;}
        </style>
        <style type="text/css" >
            .ui-progressbar-value { background-image: url(images/barchart.gif); }
            .ajaxboxstyle {
                background-color:#FFFFFF;
                border: 0.1em solid #0000FF;
                height:50px;
                margin:0 0.5em;
                overflow-x:hidden;
                overflow-y:auto;
                position:absolute;
                text-align:left;
                border-top: 1px groove #848484;
                border-right: 1px inset #999999;
                border-bottom: 1px inset #999999;
                border-left: 1px groove #848484;
                background-color:#f0f0f0;
                width:450px;
                z-index:99999;
            }
            .black_overlay{
                display: none; position: absolute; top: 0%; left: 0%;width: 110%;  height: 200%; background-color: black;     z-index:1001; -moz-opacity: 0.5; opacity:.50;overflow:auto;

            }

            .white_content {
                display: none;position: absolute; top: 30%;    left: 35%; width: 50%; height:50%;padding: 16px;    border: 10px solid silver; background-color: white; z-index:1002;-moz-border-radius-bottomleft:6px; -moz-border-radius-bottomright:6px; -moz-border-radius-topleft:6px;-moz-border-radius-topright:6px;

            }

            table.grid .collapsible {
                padding: 0 0 3px 0;
            }

            .collapsible a.collapsed {
                display: block;
                width: 15px;
                height: 15px;
                background: url(images/addImg.gif) no-repeat 3px 3px;
                outline: 0;
            }

            .collapsible a.expanded {
                display: block;
                width: 15px;
                height: 15px;
                background: url(images/deleteImg.gif) no-repeat 3px 3px;
                outline: 0;
            }
        </style>
        <style type="text/css">
            .column { width: 500px; float: left; padding-bottom: 50px;padding-left:30px }
            .column1 { width: 100%; float: left; padding-bottom: 5px;padding-left:10px }
            .portlet { margin: 0 1em 1em 0; }
            .portlet-header { margin: 0.3em; padding-bottom: 4px; padding-left: 0.2em;cursor:move }
            .portlet-header .ui-icon { float: right; }
            .portlet-content { padding: 0.4em; }
            .ui-sortable-placeholder { border: 1px dotted black; visibility: visible !important; height: 100px !important; }
            .ui-sortable-placeholder * { visibility: hidden; }
            .ajaxboxstyle {
                position: absolute;
                background-color: #FFFFFF;
                text-align: left;
                border: 1px solid #000000;
                border-top-width:1px;
                height:80px;
                width:230px;
                overflow:auto;
                overflow-x:hidden;
                margin:0em 0.5em;
                z-index:99999;
            }
            a {font-family:Verdana;cursor:pointer;font-size:<%=anchorFont%>px;}
            *{font:<%=pageFont%>px verdana}

            table.grid .collapsible {
                padding: 0 0 3px 0;
            } .collapsible a.collapsed {
                display: block;
                width: 15px;
                height: 15px;
                background: url(images/addImg.gif) no-repeat 3px 3px;
                outline: 0;
            }
            .mycls {
                background-color:#FFFFFF;
                border:0px solid #d7faff;
                height:180px;
                overflow:auto;
                width:180px;
            }
            .collapsible a.expanded {
                display: block;
                width: 15px;
                height: 15px;
                background: url(images/deleteImg.gif) no-repeat 3px 3px;
                outline: 0;
            }
            #ui-datepicker-div
            {
                z-index: 9999999;
            }
            .overlap_div{
                z-index: 9999999;
            }
        </style>
        
        <%--new Script--%>
       

       
        <style type="text/css">

            ul {padding-left: 0px; margin: 0px;}
            #acc { margin-top: -1.5%; margin-left:80%;
                   float:right;color: white; overflow: auto;
                   width: 200px; position: absolute;
            }
            #acc h3 {
                /*fallback for browsers not supporting gradients*/
                background: #003040;
                background: linear-gradient(#076D8F, #3C93B9);
                cursor: pointer;
                font-size: 12px;
                line-height: 22px;
                padding: 0 10px;margin-top: 27px;
            }
            #acc h3:hover { text-shadow: 0 0 1px rgba(255,255,255,.7); }
            #acc h3 span {
                font-size: 16px;
                margin-right: 10px;
            }
            #acc li { list-style-type: none; }
            #acc li ul {background: #3A545E;}
            #acc ul ul li a {
                color: white;
                font-size: 12px;
                line-height: 27px;
                padding: 0 15px;
                text-decoration: none;
                transition: all 0.15s;
            }
            #acc ul ul li a:hover {
                /*  background-color: #003545;*/
                border-left: 5px solid lightgreen;
            }
            #acc li.active ul { display: block; }
            /*@media only screen and (max-width: 1024px){#center250b {margin-left: -225px;} #acc { margin-top: -2%;}}*/
        </style>
    </head>
    <% String pagename = "";
                    String loguserId = String.valueOf(session.getAttribute("USERID"));
                    Connection con = ProgenConnection.getInstance().getConnection();
                    Statement st = con.createStatement();
                    ResultSet rs = st.executeQuery("select * from prg_message_board where USERID=" + loguserId);
                    Statement st1 = con.createStatement();
                    ResultSet rs1 = st1.executeQuery("select * from prg_ar_personalized_reports where prg_user_id=" + loguserId);
                    PbReturnObject pbro = new PbReturnObject(rs1);
                    ArrayList repPrevList = new ArrayList();
                    st.close();
                    st1.close();
                    rs.close();
                    rs1.close();
                    con.close();
                    try {
                        String AvailableFiolers = "select folder_Id,FOLDER_NAME from PRG_USER_FOLDER where folder_id in(";
                        AvailableFiolers += "select user_folder_id from prg_grp_user_folder_assignment where user_id=" + String.valueOf(request.getSession(false).getAttribute("USERID"));
                        AvailableFiolers += " union ";
                        AvailableFiolers += "select folder_id from prg_user_folder where grp_id in(";
                        AvailableFiolers += "select grp_id from prg_grp_user_assignment where user_id=" + String.valueOf(request.getSession(false).getAttribute("USERID"));
                        AvailableFiolers += " and grp_id not in(select grp_id from prg_grp_user_folder_assignment where user_id=" + String.valueOf(request.getSession(false).getAttribute("USERID")) + ")))";
                        PbReturnObject folderpbro = pbdb.execSelectSQL(AvailableFiolers);
                        //String userreports = "SELECT REPORT_ID, REPORT_NAME FROM PRG_AR_REPORT_MASTER where REPORT_ID in(SELECT  REPORT_ID FROM PRG_AR_USER_REPORTS where  user_id=" + String.valueOf(request.getSession(false).getAttribute("USERID")) + ")";
                        String userreports = "SELECT A.REPORT_ID,A.REPORT_NAME FROM PRG_AR_REPORT_MASTER A,(SELECT distinct REPORT_ID FROM PRG_AR_USER_REPORTS WHERE user_id=" + String.valueOf(request.getSession(false).getAttribute("USERID")) + ") B WHERE A.REPORT_ID =b.REPORT_ID";
                        PbReturnObject reportpbro = pbdb.execSelectSQL(userreports);
                        PbReturnObject rolereppbro = null;
                        if (request.getParameter("roleId") != null) {
                            String folderId = request.getParameter("roleId");
                            String rolerepdashs = "SELECT REPORT_ID, REPORT_NAME FROM PRG_AR_REPORT_MASTER where REPORT_ID in(SELECT  REPORT_ID FROM PRG_AR_REPORT_DETAILS where  folder_id=" + folderId + ")";
                            rolereppbro = pbdb.execSelectSQL(rolerepdashs);
                        }
                        String DbrdNameQuery = "select REPORT_NAME,MAP_ENABLED from prg_ar_report_master where report_id=" + PbReportId;
                        PbReturnObject dbrdNameObj = pbdb.execSelectSQL(DbrdNameQuery);
                        String mapEnabled;
                        boolean isMapEnabled = true;
                        mapEnabled = dbrdNameObj.getFieldValueString(0, "MAP_ENABLED");
                        if ((mapEnabled == null) || ("".equalsIgnoreCase(mapEnabled)) || ("Y".equalsIgnoreCase(mapEnabled))) {
                            isMapEnabled = true;
                        } else {
                            isMapEnabled = false;
                        }
                        container.setMapEnabled(isMapEnabled);
                        String repPrevilage = "SELECT PRP_ID,USER_ID,PREVILAGE_NAME FROM PRG_AR_REPORT_PREVILAGES WHERE USER_ID=" + loguserId;
                        PbReturnObject previlageObj = pbdb.execSelectSQL(repPrevilage);
                        for (int rep = 0; rep < previlageObj.getRowCount(); rep++) {
                            repPrevList.add(previlageObj.getFieldValueString(rep, "PREVILAGE_NAME"));
                        }
                        DashboardTemplateDAO dao = new DashboardTemplateDAO();%>

    <body id="mainBody" onload='shwDate()' >
        <iframe name="widgetframe" id="widgetframe" style="display:none" src='about:blank'></iframe>
        <iframe name="widgetframe1" id="widgetframe1" style="display:none" src='about:blank'></iframe>
        <table style="width:100%">
            <tr>
                <td valign="top" style="width:100%;">
                    <jsp:include page="Headerfolder/headerPage.jsp"/>
                </td>
            </tr>
        </table>
        <div id="overWriteDashboard" style="display: none" title="overWriteDashboard">
            <% if (!alist.get(1).toString().equalsIgnoreCase("PRG_DATE_RANGE")) {%>
            <table>
                <tr><td colspan="2"><font size="2" style="font-weight: bold;">Do you want to over write the report ?</font></td></tr>
                <tr><td><input type="radio" name="Date" id="sysDate" value="sysDate">Global Date</td><td>&nbsp;&nbsp;&nbsp;<input type="radio" name="Date" id="reportDate" value="reportDate" checked>Report Date</td><td>&nbsp;&nbsp;&nbsp;<input type="radio" name="Date" id="currdetails" value="currdetails" checked>Current Details</td></tr>
                <tr><td><br></td></tr>
                <tr><td><input type="radio" name="Date" id="yestrday" value="yestrday">YesterDay</td><td>&nbsp;&nbsp;&nbsp;<input type="radio" name="Date" id="tomorow" value="tomorow">Tomorrow</td></tr>
                <tr><td><br></td></tr>
                <tr><td><input type="radio" name="Date" id="newSysDate" value="newSysDate">System Date</td><td><select id="sysSign" name="sign">
                            <option value="+">+</option>
                            <option value="-">-</option>
                        </select></td><td><input id="newSysVal" type="text">Days </td></tr>
                <tr><td><input type="radio" name="Date" id="globalDate" value="globalDate" >Global Date</td><td><select id="globalSign" name="globalSign">
                            <option value="+">+</option>
                            <option value="-">-</option>
                        </select></td><td><input id="newGlobVal" type="text">Days </td></tr>
                <tr><td><br></td></tr>
                <tr><td><br></td></tr>
                <tr><td colspan="2" align="center"><input type="button" value="Ok" class="navtitle-hover" style="width:40px;height:25px;color:black" onclick="overRide()"/>&nbsp;&nbsp;&nbsp;
                        <input type="button" value="Cancel" class="navtitle-hover" style="width:50px;height:25px;color:black" onclick="cancelOverWriteDashboard()"/></td></tr>
            </table>
            <%} else {%>
            <table>
                <tr><td colspan="2"><font size="2" style="font-weight: bold;">Do you want to over write the report ?</font></td></tr>
                <tr><td><input type="radio" name="Date" id="reportDate" onclick="getCustomDateDb()" value="reportDate" checked>Report Date</td><td>&nbsp;&nbsp;&nbsp;<input type="radio" name="Date" id="customDate" onclick="getCustomDateDb()" value="customDate">Custom Date</td><td>&nbsp;&nbsp;&nbsp;<input type="radio" name="Date" id="currdetails" onclick="getCustomDateDb()" value="currdetails">Current Details</td></tr>
                <table id="dateRangeTab" style="display: none;">
                    <tr><td style="font-weight:bold;">For From Date</td></tr>
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
                    <tr><td><input type="radio" name="CmpToDate" id="cmptoyestrday" value="cmptoyestrday">YesterDay</td><td>&nbsp;<input type="radio" name="CmpToDate" id="cmptotomorow" value="cmptotomorow" checked>Tomorrow</td></tr>
                    <tr><td><input type="radio" name="CmpToDate" id="cmptoSysDate" value="cmptoSysDate">System Date</td><td><select id="cmptoSysSign" name="sign">
                                <option value="+">+</option>
                                <option value="-">-</option>
                            </select></td><td><input id="cmptoSysVal" type="text">Days </td></tr>
                    <tr><td><input type="radio" name="CmpToDate" id="cmptoglobalDate" value="fromToglobalDate" />Global Date</td><td><select id="cmptoglobalSign" name="globalSign">
                                <option value="+">+</option>
                                <option value="-">-</option>
                            </select></td><td><input id="cmptoGlobVal" type="text"/>Days </td></tr>
                </table>
                <tr><td><br></td></tr>
                <tr><td><br></td></tr>
                <tr><td colspan="2" align="center"><input type="button" value="Ok" class="navtitle-hover" style="width:40px;height:25px;color:black;" onclick="overRide()"/>&nbsp;&nbsp;&nbsp;
                        <input type="button" value="Cancel" class="navtitle-hover" style="width:50px;height:25px;color:black;" onclick="cancelOverWriteDashboard()"/></td></tr>
            </table>
            <%}%>
        </div><iframe name="widgetframe" id="widgetframe" style="display:none" src='about:blank'></iframe>
        <form name="frmParameter" action=" " method="POST" >
            <div id="light" align="center" class="white_content"><img  alt="Page is Loading" src='images/ajax.gif'></div><table style="height:600px;width:100%;max-height:100%"  cellpadding="0" cellspacing="0">
                <%
                                        //  DashboardTemplateDAO dao = new DashboardTemplateDAO();
                                        ProgenParam connectionparam = new ProgenParam();

                                        String lastupdateedate = "";

                                        // Connection con = null;
                                        ArrayList<String> qryelements = collect.reportQryElementIds;
                                        List<DashletDetail> dashlets = collect.dashletDetails;
                                        String elementId = "";

                                        if (qryelements == null || qryelements.isEmpty()) {
                                            if (dashlets != null || !dashlets.isEmpty()) {
                                                List<QueryDetail> qdList = dashlets.get(0).getReportDetails().getQueryDetails();
                                                if (qdList != null && !qdList.isEmpty()) {
                                                    elementId = qdList.get(0).getElementId();
                                                } else {
                                                    Report reportDetails = dashlets.get(0).getReportDetails();

                                                    if (reportDetails != null) {
                                                        List<KPIElement> kpiElements = reportDetails.getKPIElements();
                                                        if (kpiElements != null && !kpiElements.isEmpty()) {
                                                            for (int i = 0; i < kpiElements.size(); i++) {
                                                                if (!kpiElements.get(i).isIsGroupElement()) {
                                                                    elementId = kpiElements.get(i).getElementId();
                                                                    break;
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        if (elementId != null && !elementId.isEmpty()) {
                                            con = connectionparam.getConnection(elementId);
                                            lastupdateedate = dao.getLastUpdatedDate(con, elementId);
                                        }%>
<!--                <tr style="width:90%;max-height:100%"><td align="right"><table align="left" width="90%" class="ui-corner-all"><tbody><tr><td align="right" styel="width:95%;margin-right:20px;"><font color="black">Last Updated:&nbsp;<%=lastupdateedate%></font></td></tr></tbody></table></td></tr>-->
                <tr style="background:#006699;height:30px;width:100%;max-height:100%">
                    <td> <table width="100%" class="ui-corner-all">
                            <tr>
                                <td valign="top" style="width:1%" >
                                    <a onclick="tabparamListDisp()" href="javascript:void(0)" title="Parameter Region"><img alt="Parameter Region" class="ui-icon ui-icon-contact"/></a></td>
                                <td  style="height:10px;width:auto;" align="left">

                                    <%String oldDashName = null;%>
                                    <%if (container != null) {
                                                                if (container.getReportName() != null) {
                                                                    pagename = container.getDbrdName();
                                                                } else {
                                                                    pagename = request.getParameter("pagename");
                                                                }
                                                            } else {
                                                                pagename = request.getParameter("pagename");
                                                            }
                                                            String pgurl = "";
                                                            int fntsize = anchorFont + 1;
                                                            if (pagename != null) {
                                                                com.progen.reportview.action.showReportName repname = new com.progen.reportview.action.showReportName();
                                                                ArrayList repNameList = repname.buildReportName(pagename);
                                                                //for (int i = 0; i < repNameList.size(); i++) {%>
<!--                                    <span id="dashName"  style="color: #4F4F4F;font-family:verdana;font-size:<%=fntsize%>px;font-weight:bold"  title="<%//=pagename%>"><%//=repNameList.get(i)%></span>-->
                                    <span id="dashName"  style="color: #FFF;font-family:verdana;font-size:<%=fntsize%>px;font-weight:bold"  title="<%=pagename%>"><%=pagename%></span>
                                    <br/>
                                    <%//}
                                                                                                } else {
                                                                                                    if (dbrdNameObj.getRowCount() > 0) {
                                                                                                        pagename = dbrdNameObj.getFieldValueString(0, "REPORT_NAME");//(String) ((HashMap) request.getSession(false).getAttribute("DashboardHashMap")).get("DashboardName");
                                                                                                    }
                                                                                                    com.progen.reportview.action.showReportName repname = new com.progen.reportview.action.showReportName();
                                                                                                    ArrayList repNameList = repname.buildReportName(pagename);
                                                                                                    // for (int i = 0; i < repNameList.size(); i++) { %>
                                    <span id="dashName"  style="color: #4F4F4F;font-family:verdana;font-size:<%=fntsize%>px;font-weight:bold"  title="<%//=pagename%>"><%//=repNameList.get(i)%></span>-->
                                    <span id="dashName"  style="color: #4F4F4F;font-family:verdana;font-size:<%=fntsize%>px;font-weight:bold"  title="<%=pagename%>"><%=pagename%></span>
                                    <br/>
                                    <%//}
                                                            }
                                                            oldDashName = pagename;
                                                            if (request.getAttribute("url") != null) {
                                                                pgurl = pgurl + request.getAttribute("url").toString();
                                                                pgurl = pgurl + ";pagename=" + pagename;
                                                                brdcrmb.inserting(pagename, pgurl);
                                                            }%>
                                </td>
                                <td valign="top" width="50%" align="left">
                                    <div id='homediv' style="display:none;">
                                        <table align="left"  style="display:none;">
                                            <tr align="left" >
                                                <% for (int i = 0; i < repid1.size() && i < 5; i++) {%>
                                                <%String s;%>
                                                <% if (list1.get(i).toString().length() > 30) {%>
                                                <% s = list1.get(i).toString().substring(0, 30).concat("...");%>
                                                <% } else {%>
                                                <%s = list1.get(i).toString();%>
                                                <%}%>
                                                <% if (reporttype1.get(i).toString().equals("R")) {%>
                                                <td  style="white-space: nowrap;width: auto"><a href="reportViewer.do?reportBy=viewReport&REPORTID=<%=repid1.get(i)%>&action=open" style="font-family: helvetica; font-size: 11px; color: rgb(150, 113, 23); font-weight: bold;"  title="<%=list1.get(i).toString()%>">&nbsp;<%=s%></a><img src="<%=request.getContextPath()%>/jQuery/jquery/themes/base/ChevronOverlay.png" border="0"/></td>
                                                    <%} else {%>
                                                <td style="white-space: nowrap;width: auto"><a  href="dashboardViewer.do?reportBy=viewDashboard&REPORTID=<%=repid1.get(i)%>" style="font-family: helvetica; font-size: 11px; color: rgb(150, 113, 23); font-weight: bold;"  title="<%=list1.get(i).toString()%>">&nbsp;<%=s%></a><img src="<%=request.getContextPath()%>/jQuery/jquery/themes/base/ChevronOverlay.png" border="0"/></td>
                                                <% }
                          }%> </tr></table></div><div class="chevronOverlay main"></div></td>
                                <!--                                <td align="left" style="font-size:larger; background-color: #B4D9EE; width: 1%;  padding: 0.6em; border: 1px solid #CCC; "><label>ViewBy&nbsp;&nbsp;&nbsp;&nbsp;</label></td>-->
                                <td align="right" id="autoshow" style="display: none">
                                    <a href="#javascript.void(0)"  class='ui-icon ui-icon-circle-triangle-s' title="showdate" onclick="autoshow()"></a>
                                </td></tr></table></td></tr>
                <tr style="width:100%;height:544px;max-height:100%">
                    <td><table width="100%" class="ui-corner-all" height="100%" border="1px solid black" cellpadding="0" cellspacing="0">
                            <tr class="ui-corner-all">
                                <td id="leftTd" valign="top" style="width:16%;display:none;" align="top" class="ui-corner-all">
                                    <table style="width:99%;" class="ui-corner-all">
                                        <%//if ((repPrevList != null) && repPrevList.contains("Favourite Links")) {%>
                                        <tr>
                                            <td>
                                                <div class="navsection">
                                                    <div class="navtitle1" id="displayfavlink" onclick="displayfavlink()" >&nbsp;<b style="font-family:verdana;font-weight:bold;">Favourite Links</b> </div>
                                                    <table id="favlinkcont" cellpadding="2"   cellspacing="2" style="display:none" class="ui-corner-all" style= "width:100%">
                                                        <tbody>
                                                            <tr style="width:100%">
                                                                <td style="width:100%">
                                                                    <iframe src="<%=request.getContextPath()%>/getFavouriteReports.do" scrolling="no" id="favFrame" frameborder="0" style="width:100%"></iframe>
                                                                </td>
                                                            </tr>
                                                            <tr style="width:100%">
                                                                <td align="center">
                                                                    <input type="button" id="CustomizeFav" class="navtitle-hover"  style="width:122px;height:20px;color:black;" value="Customize">
                                                                </td>
                                                            </tr></tbody> </table></div> </td> </tr>
                                                            <%if (repPrevList.contains("Compose Message") || repPrevList.contains("Scheduler") || repPrevList.contains("Sticky Notes")) {
                                                                    if (!isAxa) {%>
                                        <tr> <td>  </td>
                                        </tr>
                                        <%}
                                                                }
                                                                if ((repPrevList != null) && repPrevList.contains("BizRoles")) {
                                                                }%>
                                        <tr> <td> <div class="navsection">
                                                    <div class="navtitle1" id="OverrideDashboard" onclick="OverrideDashboard()" >&nbsp;<b style="font-family:verdana;font-weight:bold;">OverWrite</b></div>
                                                </div>
                                            </td>
                                        </tr><tr> <td><div class="navsection"><div class="navtitle1" id="createDashboard" onclick="createDashboard()" >&nbsp;<b style="font-family:verdana;font-weight:bold;">Save As New</b></div>
                                                </div> </td> </tr><tr> <td><div class="navsection">
                                                    <div class="navtitle1" id="renameDashboard" onclick="renameDashboard('<%=oldDashName%>','<%=PbReportId%>')" >&nbsp;<b style="font-family:verdana;font-weight:bold;">Rename Dashboard</b></div>
                                                </div> </td></tr><tr> <td> <div class="navsection">
                                                    <div class="navtitle1" id="dashbardQuery" onclick="dashbardQuery('<%=PbReportId%>')" >&nbsp;<b style="font-family:verdana;font-weight:bold;">Sql Query</b></div>
                                                </div></td>
                                        </tr> <tr> <td><div class="navsection">
                                                    <div class="navtitle1" id="displayDbrdTimeInfo" onclick="displayDbrdTimeInfo('<%=PbReportId%>')" >&nbsp;<b style="font-family:verdana;font-weight:bold;">Dashboard Time Info</b> </div>
                                                    <table id="dbrdTimecont" cellpadding="2"   cellspacing="2" style="display:none" class="ui-corner-all" style="width:100%">
                                                        <tbody>
                                                            <tr style="width:100%">
                                                                <td id="DbrdTimeinfo">
                                                                    <iframe src="<%=request.getContextPath()%>/dashboardTemplateViewerAction.do?templateParam2=getDbrdTimeDisplay" scrolling="no" id="DbrdTime" frameborder="0" style="width:100%"></iframe>
                                                                </td> </tr> </tbody> </table> </div>
                                            </td>
                                        </tr> </table></td><div id="divassign"  style="display:none;width:100%;height:100%;position:centre;" title="Assign Report to Users">
                                <iframe name="divassignframe" id="divassignframe" style="display:'';width:100%;height:100%;border:none;" src="about:blank"></iframe>
                            </div>
                            <td width="1%" title="Click Here To Hide/Show Left Pane" style="background-color:#e6e6e6;border:0px;cursor:pointer"  >
<!--                                    <br>  <a href="#"  onclick="saveNewRepGrpTab1('<%=request.getContextPath()%>','<%=USERID%>','<%=foldernames%>','<%=PbReportId%>', '<%=pagename%>')" class="ui-icon ui-icon-person" title="Assign DashBoard to Users"></a>-->
                                <br> <div><img id="tdImage" onclick="hideTdDash()" src="<%=request.getContextPath()%>/icons pinvoke/<%=String.valueOf(session.getAttribute("imageTypeDash"))%>.png"/></div>
                            </td> <td valign="top" style="width:83%" class="ui-corner-all">
                                <input type="hidden" name="h" id="h" value="<%=request.getContextPath()%>">
                                <input type="hidden" name="REPORTID" id="REPORTID" value="<%=PbReportId%>">
                                <input type="hidden" name="pagename" value="<%=pagename%>">
                                <input type="hidden" name="parameters" id="parameters" value="<%=parameters%>">
                                <input type="hidden" name="TimeLevelstr" id="TimeLevelstr" value="<%=timeLevelStr%>">
                                <table style="width:100%" valign="top">
                                    <tr valign="top" style="width:99%">
                                        <td valign="top" style="width:99%">
                                            <!--                                                <center>-->
                                            <div class="navsection" style="width:99%"> <div id="fixedtop1" >
                                                    <% if (timeinfo.get(1).equalsIgnoreCase("PRG_DATE_RANGE")) {%>
                                                    <div id="center250b">
                                                        <%} else {%>
                                                        <div id="center250a">
                                                            <%}%>
                                                            <div class="form clearFix">
                                                                <span class="wr100">
                                                                    <table class="select" id="roundtrip">
                                                                        <tr width="100%">
                                                                            <% if (timeinfo.get(1).equalsIgnoreCase("PRG_DATE_RANGE") && sytm.equalsIgnoreCase("No")) {%>
                                                                            <td  white-space:nowrap ;width: auto"></td>
                                                                            <td  align="right"  id="datetime"  tabindex="6">
                                                                                <span id="depShow">
                                                                                    <span class="top w100 mrtop" id="field1"><%=(fullName0).substring(0, 4) + (fullName0).substring(6)%></span>
                                                                                    <span class="date">
                                                                                        <small Style="font-weight: bold" id="field2"><%=dated%></small></span>
                                                                                    <span class="bottom w100" id="field3"><%=day0%></span></span></td>
                                                                            <td><input type="hidden" class="ui-datepicker" id="fromdate" name="fromdate" onchange="dateclick()" onclick="showdate()"/></td>
                                                                            <td><input  type="hidden" id="datetext" name="datetext" value=""/></td>

                                                                            <td align="left" style="white-space:nowrap ;width: auto;padding-left: 1.5em">To :</td>
                                                                            <td align="right" id="todatetime" tabindex="8"  style="padding-left: 0.5em">
                                                                                <span id="retShow" style="position:relative;">
                                                                                    <span class="top w100 mrtop" id="tdfield1"><%=(fullName).substring(0, 4) + (fullName).substring(6)%></span>
                                                                                    <span class="date"><small class="init" Style="font-weight: bold" id="tdfield2"><%=date%></small></span>
                                                                                    <span class="bottom w100" id="tdfield3"><%=day1%></span></span></td>
                                                                            <td><input  type="hidden" class="ui-datepicker " name="todate" id="todate" onchange="dateclick()" onclick="showdate()"/> </td>
                                                                            <td><input  type="hidden" id="datetext" name="datetext" value=""/></td>

                                                                            <td align="right" style="font-weight:bold ; width: auto;padding-left: 1.5em"> COMPARE</td>
                                                                            <td align="right" style="width: auto;padding-left: 1.5em"></td>
                                                                            <td align="left" id="comparefromtime" tabindex="8"  style="padding-left: 0.5em">
                                                                                <span id="retShow" style="position:relative;">
                                                                                    <span class="top w100 mrtop" id="cffield1"><%=(cffullName).substring(0, 4) + (cffullName).substring(6)%></span>
                                                                                    <span class="date"><small class="init" Style="font-weight: bold" id="cffield2"><%=cfdate%></small></span>
                                                                                    <span class="bottom w100" id="cffield3"><%=cfday%></span></span></td>
                                                                            <td><input type="hidden" class="ui-datepicker " id="comparefrom" name="comparefrom"  onchange="dateclick()" onclick="showdate()"/></td>
                                                                            <td><input  type="hidden" id="datetext" name="datetext" value=""/></td>

                                                                            <td align="right" style="width: auto;padding-left: 1.5em">To:</td>
                                                                            <td align="left" id="comparetoTime" tabindex="8"  style="padding-left: 0.5em"> <span  id="retShow" style="position:relative;">
                                                                                    <span class="top w100 mrtop" id="ctfield1"><%=(ctfullName).substring(0, 4) + (ctfullName).substring(6)%></span>
                                                                                    <span class="date"><small class="init" Style="font-weight: bold" id="ctfield2"><%=ctdate%></small></span>
                                                                                    <span class="bottom w100" id="ctfield3"><%=ctday%></span></span></td>
                                                                            <td><input  type="hidden" class="ui-datepicker1 " id="compareto" name="compareto" onchange="dateclick()" onclick="showdate()"/></td>
                                                                            <td><input  type="hidden" id="datetext" name="datetext" value=""/></td>
                                                                                <%} else if (timeinfo.get(1).equalsIgnoreCase("PRG_STD") && sytm.equalsIgnoreCase("No")) {%>
                                                                            <td  style="padding-left: 20em;" align="right"  tabindex="6">
                                                                                <span id="retShow" style="position:relative;">
                                                                                    <span class="top w100 mrtop" id="pfield1"><%=(fullName0).substring(0, 4) + (fullName0).substring(6)%></span>
                                                                                    <span class="date">
                                                                                        <small Style="font-weight: bold" id="pfield2"><%=dated%></small></span>
                                                                                    <span class="bottom w100" id="pfield3"><%=day0%></span></span></td>
                                                                            <td> <input height="100px" width="100px" type="hidden" class="ui-datepicker " id="perioddate" name="perioddate" onchange="dateclick()" onclick="showdate()"/><td>
                                                                            <td> <input  type="hidden" id="datetext" name="datetext" value=""/></td>
                                                                            <td>
                                                                                <% DisplayParameters dur = new DisplayParameters();
            String duration1 = String.valueOf(dur.displayTime(collect.timeDetailsMap));%>
                                                                                <%=duration1%>
                                                                            </td>
                                                                            <% } else if (timeinfo.get(1).equalsIgnoreCase("PRG_DAY_ROLLING") && sytm.equalsIgnoreCase("No")) {%>
                                                                            <td style="padding-left: 20em" align="right"  tabindex="6">
                                                                                <span class="top w100 mrtop" id="pfield1"><%=(fullName0).substring(0, 4) + (fullName0).substring(6)%></span>
                                                                                <span class="date">
                                                                                    <small Style="font-weight: bold" id="pfield2"><%=dated%></small></span>
                                                                                <span class="bottom w100" id="pfield3"><%=day0%></span></td>
                                                                            <td> <input height="100px" width="100px" type="hidden" class="ui-datepicker " id="perioddate" name="perioddate" onchange="dateclick()" onclick="showdate()"/><td>
                                                                            <td> <input  type="hidden" id="datetext" name="datetext" value=""/></td>
                                                                            <td>
                                                                                <% DisplayParameters dur = new DisplayParameters();
                String duration1 = String.valueOf(dur.displayTime(collect.timeDetailsMap));%>
                                                                                <%=duration1%>
                                                                            </td>
                                                                            <% } else if (timeinfo.get(1).equalsIgnoreCase("PRG_DATE_RANGE") && sytm.equalsIgnoreCase("Yes")) {%>
                                                                            <!--               //added by Mohit for Custom setting-->
                                                                            <td  style="padding-left: 11em; white-space:nowrap ;width: auto">From :</td>
                                                                            <td  align="right"  id="datetime"  tabindex="6">
                                                                                <span class="top w100 mrtop" id="field1"><%=(fullName0).substring(4)%></span>
                                                                            <td>
                                                                                <img src="<%=request.getContextPath()%>/images/calendar_18x16.gif" width="18" height="16" onclick="showYeardashDb('fromdate')"></td>
                                                                            <td><input height="100px" width="100px" type="hidden" id="fromdate" name="fromdate" onchange="" onclick=""/>
                                                                            </td>
                                                                            <td> <input  type="hidden" id="datetext" name="datetext" value=""/></td>

                                                                            <td align="left" style="white-space:nowrap ;width: auto;padding-left: 1.5em">To :</td>
                                                                            <td align="right" id="todatetime" tabindex="8"  style="padding-left: 0.5em">
                                                                                <span class="top w100 mrtop" id="tdfield1"><%=(fullName).substring(4)%></span>
                                                                            <td>
                                                                                <img src="<%=request.getContextPath()%>/images/calendar_18x16.gif" width="18" height="16" onclick="showYeardashDb('todate')"></td>
                                                                            <td><input height="100px" width="100px" type="hidden" id="todate" name="todate" onchange="" onclick=""/>
                                                                            </td>
                                                                            <td> <input  type="hidden" id="datetext" name="datetext" value=""/></td>
                                                                            <td align="right" style="font-weight:bold ; width: auto;padding-left: 1.5em"> COMPARE</td>
                                                                            <td align="right" style="width: auto;padding-left: 1.5em"></td>
                                                                            <td align="left" id="comparefromtime" tabindex="8"  style="padding-left: 0.5em">
                                                                                <span class="top w100 mrtop" id="cffield1"><%=(cffullName).substring(4)%></span>
                                                                            <td>
                                                                                <img src="<%=request.getContextPath()%>/images/calendar_18x16.gif" width="18" height="16" onclick="showYeardashDb('comparefrom')"></td>
                                                                            <td><input height="100px" width="100px" type="hidden" id="comparefrom" name="comparefrom" onchange="" onclick=""/>
                                                                            </td>
                                                                            <td> <input  type="hidden" id="datetext" name="datetext" value=""/></td>
                                                                            <td align="right" style="width: auto;padding-left: 1.5em">To:</td>
                                                                            <td align="left" id="comparetoTime" tabindex="8"  style="padding-left: 0.5em"> <span  id="retShow" style="position:relative;">
                                                                                    <span class="top w100 mrtop" id="ctfield1"><%=(ctfullName).substring(4)%></span>
                                                                                    <td>
                                                                                        <img src="<%=request.getContextPath()%>/images/calendar_18x16.gif" width="18" height="16" onclick="showYeardashDb('compareto')"></td>
                                                                                    <td><input height="100px" width="100px" type="hidden" id="compareto" name="compareto" onchange="" onclick=""/>
                                                                                    </td>
                                                                                    <td> <input  type="hidden" id="datetext" name="datetext" value=""/></td>
                                                                                        <%} else if (timeinfo.get(1).equalsIgnoreCase("PRG_STD") && sytm.equalsIgnoreCase("Yes")) {%>
                                                                                    <td  style="padding-left: 20em;" align="right"  tabindex="6">
                                                                                        <span id="retShow" style="position:relative;">
                                                                                        </span></td>
                                                                                    <td> Year:&nbsp;</td>
                                                                                    <td align="right">
                                                                                        <select id="calyear" name="calyear" style="width:90px" onchange="saveyearDb('perioddate')">
                                                                                            <% String calndrYear = (fullName0).substring(4);
                                 for (int year = 2005; year <= 2020; year++) {
                                     if (calndrYear.equalsIgnoreCase(Integer.toString(year))) {%>
                                                                                            <option selected value="<%=year%>"> <%=year%> </option>
                                                                                            <%} else {%>
                                                                                            <option value="<%=year%>"> <%=year%> </option>
                                                                                            <%}
                                     }%>
                                                                                        </select></td>
                                                                                    <td><input height="100px" width="100px" type="hidden" id="perioddate" name="perioddate" onchange="" onclick=""/>
                                                                                    </td>
                                                                                    <td> <input  type="hidden" id="datetext" name="datetext" value=""/></td>
                                                                                    <td>
                                                                                        <% DisplayParameters dur = new DisplayParameters();
            String duration1 = String.valueOf(dur.displayTime(collect.timeDetailsMap));%>
                                                                                        <%=duration1%>
                                                                                    </td>
                                                                                    <% } else if (timeinfo.get(1).equalsIgnoreCase("PRG_DAY_ROLLING") && sytm.equalsIgnoreCase("Yes")) {%>
                                                                                    <td  style="padding-left: 20em;" align="right"  tabindex="6">
                                                                                        <span class="top w100 mrtop" id="pfield1"><%=(fullName0).substring(4)%></span></td>
                                                                                    <td>
                                                                                        <img src="<%=request.getContextPath()%>/images/calendar_18x16.gif" width="18" height="16" onclick="showYeardashDb('perioddate')"></td>
                                                                                    <td><input height="100px" width="100px" type="hidden" id="perioddate" name="perioddate" onchange="" onclick=""/>
                                                                                    </td>
                                                                                    <td> <input  type="hidden" id="datetext" name="datetext" value=""/></td>
                                                                                    <td>
                                                                                        <% DisplayParameters dur = new DisplayParameters();
                String duration1 = String.valueOf(dur.displayTime(collect.timeDetailsMap));%>
                                                                                        <%=duration1%> <% }%>
                                                                                    </td> <td> <a onclick="resetdash(<%= PbReportId%>,'<%=pagename%>')" href="#" style=""> Reset </a></td><td></td>
                                                                                    <td align="right"><input style="width:30px" type="button" class="navtitle-hover" value=" Go " onclick="submitdashboardbyfilter()"/></td>
                                                                                    <td align="right"><a  href="#javascript.void(0)" class="ui-icon ui-icon-circle-triangle-s" title="Autohide" onclick="autohide()"></a></td>
                                                                        </tr>
                                                                    </table> </span></div>
                                                        </div>
                                                    </div>
                                                    <div id="tabParameters" style="display:none;width:50%;height:100%;background-color:white; direction: ltr; float: right; position:absolute;text-align:left;border: 1px solid LightGrey;left:0px;top:122px;z-index: 10000;">
                                                        <%=ParamSectionDisplay%>
                                                    </div> </div> </td> </tr> <tr valign="top"><td valign="top" width="100%"><div >
                                                <div class="navtitle1" style="width:100%;display:none;"  onclick="dispPortlet()" ><table width="100%">
                                                        <tr><td align="right" width="55%"></td>
                                                            <% if (PrivilegeManager.isModuleEnabledForUser("MAP", USERID) && container.isMapEnabled()) {%>
                                                            <td align="left" valign="top" width="8%">
                                                            </td><%}%>
                                                            <td align="left" valign="top" width="12%">
                                                                <div style="display:none;width:100px;height:35px;background-color:#ffffff;
                                                                     overflow:auto;position:absolute;
                                                                     text-align:left;border:1px solid #000000;border-top-width: 0px;"
                                                                     id="GraphTable1"> <Table width="100%" >
                                                                        <Tr>
                                                                            <Td  id= "addStdKpi"></Td>
                                                                        </Tr>
                                                                        <%if (!isAxa) {%>
                                                                        <Tr>

                                                                            <Td  id= "addKpiwithTarget"></Td>

                                                                        </Tr>
                                                                        <%}%>
                                                                    </Table>
                                                                </div>
                                                            </td><td align="left" valign="top" width="13%">
                                                                <div style="display:none;width:120px;height:53px;background-color:#ffffff;
                                                                     overflow:auto;position:absolute;
                                                                     text-align:left;border:1px solid #000000;border-top-width: 0px;"
                                                                     id="GraphTable2"><Table width="100%">
                                                                        <%if (!isAxa) {%>
                                                                        <Tr>
                                                                            <Td  id= "addGraph1"></Td>
                                                                        </Tr>
                                                                        <%}%>
                                                                        <Tr>
                                                                            <Td  id= "addGraph2"></Td>
                                                                        </Tr>

                                                                        <Tr>
                                                                            <Td  id= "addGrap3"></Td>
                                                                        </Tr>
                                                                    </Table>
                                                                </div>
                                                            </td>
                                                        </tr> </table>
                                                    <b style="font-family:verdana"></b>
                                                </div>
                                                <div id="divPortlet"  style="width:100%;">
                                                    <table id="tablePortlet" style="width:99%"></table></div><div id="acc"><ul><li id="tag_name">
                                                            <h3 style="color:#ffffff;" onclick="noReportTags()"><span class="icon-tags" style="color:#ffffff; height: 600px; overflow: auto;"></span>
                                                                <%=tag_id_name%></h3>
                                                            <ul class="treeC" id="reports_nav1" style="display: none;">
                                                            </ul></li></ul></div> </div> </td></tr>
                                    <tr><td>

                                            <div id="rightDivone" class="" style="width:79vw;">
                                                <div id="arrowL" style="">
                                                    <span id="prev" style=""><img   style="height:25px;" src="<%=request.getContextPath()%>/images/_arrow-left-.png" /></span>
                                        <!--                      <span id="next11" style="" ><img title="Click for next reports" style="height:20px;" src="<%=request.getContextPath()%>/images/_arrow-right-.png" /></span>-->
                                                </div>
                                                <div id="arrowR" style="">
                                                    <span id="next1" style="" ><img  style="height:25px;margin-left: -30px"  src="<%=request.getContextPath()%>/images/_arrow-right-.png" /></span>
                                                </div>
                                                <div id="resetgraph" style="margin-top:0.3%;">
                                                    <img title="Reset" onclick="resetdash(<%= PbReportId%>,'<%=pagename%>')" style=' background:white;height:20px; border-radius:10px ;margin-left: -20px'   src="<%=request.getContextPath()%>/images/refersh_image.png" />
                                                    <!--           <span id="" style=""><a onclick='generateJsonDataReset(parent.$("#graphsId").val())' style="margin-left: -30px"> Reset </a></span>-->
                                                </div>
                                                <div id="gottabId11" style="margin-top:0.3%;">
                                                    <input id='gottabId1' class='navtitle-hover' type='button'  value='Go' onclick='submitdashboardbyfilter()' style='width:25px; background:white; border-radius:10px ;margin-left: -40px'>
                                                </div>

                                                <div id="list-container" style="margin-top: 5px;">
                                                    <div  id="updaterowgraph" class='list1'>

                                                        <% HashMap reportParam = collect.reportParameters;
                                                                                ArrayList al1 = null;
                                                                                List al2 = null;
                                                                                String key1;
                                                                                int i1 = 0;
                                                                                PbDb db = new PbDb();
                                                                                PbReturnObject retOb = null;
                                                                                String qry = "select ELEMENT_ID, PARAM_DISP_NAME, DISP_SEQ_NO from PRG_AR_REPORT_PARAM_DETAILS where REPORT_ID = '" + PbReportId + "' order by 3";
                                                                                retOb = db.execSelectSQL(qry);
                                                                                ArrayList<String> parameterlist = new ArrayList<String>();
                                                                                for (int j1 = 0; j1 < reportParam.size() && j1 < Integer.parseInt(container.Filters); j1++) {
                                                                                    parameterlist.add(retOb.getFieldValueString(j1, "ELEMENT_ID"));
                                                                                }
                                                                                int setgblflag = 0;
                                                                                Set keySet1 = reportParam.keySet();
                                                                                String[] a1 = (String[]) (reportParam.keySet()).toArray(new String[reportParam.size()]);
                                                                                ReportTemplateDAO rtd = new ReportTemplateDAO();
                                                                                HashMap<String, ArrayList> AllFilterValues = rtd.GetAllFilterValues(a1);
                                                                                for (int j1 = 0; j1 < reportParam.size() && j1 < Integer.parseInt(container.Filters); j1++) {
                                                                                    Iterator itr1 = keySet1.iterator();
                                                                                    if (setgblflag == j1) {
                                                                                        String idgbl = "globalfilterrow" + setgblflag;
                                                        %>
                                                        <div  id="<%=idgbl%>" class='item' style="vertical-align: top;margin-top: -2px;margin-left: 10px;">
                                                            <%setgblflag = setgblflag + 4;
                                                                                                                    }
                                                                                                                    while (itr1.hasNext()) {
                                                                                                                        key1 = itr1.next().toString();
                                                                                                                        al1 = (ArrayList) reportParam.get(parameterlist.get(j1));
                                                                                                                        al2 = (List) al1.get(8);
                                                                                                                        if (key1.equalsIgnoreCase(parameterlist.get(j1))) {
                                                            %>
                                                            <select name="<%=parameterlist.get(j1)%>" id=<%=al1.get(1).toString().replaceAll(" ", "1q1")%>  multiple style="">
                                                                <% boolean flag = false;
                                                                                                   if (al2.size() >= 1) {
                                                                                                       for (int l = 0; l < al2.size(); l++) {
                                                                                                           if (!al2.get(l).toString().equalsIgnoreCase("All")) {
                                                                                                               String value = AllFilterValues.get(parameterlist.get(j1)).get(l).toString() + "_" + l + "_" + parameterlist.get(j1);
                                                                                                           value = value + "_selecttrue";%>
                                                                <script type="text/javascript">
                                                                        filterMapNew["<%=parameterlist.get(j1)%>"].push("<%=al2.get(l).toString()%>");
                                                                </script>
                                                                <option  value="<%=value%>"><%=al2.get(l).toString().replace("]", "").replace("[", "")%></option>
                                                                <%} } }
                                    if (flag == false) {%>
                                                                <script> var temp=[];
                                                                        temp.push("All");
                                                                        $("#CBOARP<%=parameterlist.get(j1)%>").val(JSON.stringify(temp))
                                                                </script>
                                                                <%}
                                                                                                                        }
                                                                                                                    }%></select>
                                                                <%i1++;
                                                                                                                        if ((setgblflag - 1) == j1) {%>

                                                        </div>
                                                        <% }
                                        }%>
                                                    </div>
                                                </div>    </div></td></tr></table>
                                <table   style="width:100%;" valign="top">  <%--  added by K   --%>
                                    <tr><TD colspan="2"> <div id="DasboardMainDiv" style="width:99%;height:0px"> <%=buildDashBoard%><div id="zooming" style="width:auto;height:0px"></div></div>
                                        </TD> </tr> <tr valign="top" style="width:99%"><td valign="top" width="99%">
                                            <table id="DasboardTable"  style="height:80px;width:100%"></table>
                                            <table id="tempDasboardTable"  style="height:80px;width:100%"></table>
                                        </td> </tr> </table><iframe id="msgframe" class="frame1" src='about:blank' style="display:none"></iframe>
                                <iframe id="Scheduler" class="frame1" src='about:blank' style="display:none" ></iframe>
                                <iframe id="tracker" class="frame1" src='about:blank' style="display:none"></iframe>
                                <iframe id="cstLinksFrame" height="380px" width="660px" src="getAllReports.do" style="display:none" ></iframe>
                                <iframe id="prtLinksFrame" height="380px" width="660px" src='about:blank'  style="display:none"></iframe>
                                <div id="favLinksDialog" title="Favourite Links" style="display:none">
                                    <iframe src="getAllReports.do" scrolling="no" height="100%" width="100%" frameborder="0" id="favFrame"></iframe>
                                </div>
                                <br>
                                <table style="width:100%">
                                    <tr> <tr> <td> &nbsp;</td>
                                        <%--<input type="button" class="navtitle-hover" value="Cancel" style="width:auto" onclick="javascript:cancelDashboard();"></td>--%>
                                    </tr><tr> <td valign="top" style="width:100%;">
                                            <jsp:include page="Headerfolder/footerPage.jsp"/>
                                        </td></tr> </table> <div id="fade" class="black_overlay"></div>
                                <div id="reportstart" class="navigateDialog" title="Navigation" style="display:none">
                                    <iframe src="startPage.jsp" frameborder="0" height="100%" width="100%" ></iframe>
                                </div>
                                <div id="kpiTarget" class="KPI Target" title="KPI Target" style="display:none">
                                    <iframe src='about:blank' id="kpiTargetFrame" frameborder="0" height="100%" width="100%" ></iframe>
                                </div>
                                <div id="editKpiCustomColor" class="edit KPI CustomColor" title="Custom KPI Ranges" style="display:none">
                                    <iframe src='about:blank' id="editKpiCustomColorFrame" frameborder="0" height="100%" width="100%" ></iframe>
                                </div> <div id="fadestart" class="black_start"></div>
                                <div id="busRoleDiv" title="Business Roles" style="display:none">
                                    <div class="innerDiv" style="height:270px;width:96%;overflow:auto">
                                        <table align="center" width="99%" style="overflow:auto;height:250px;" >
                                            <tr style="overflow:auto;height:auto;" >
                                                <td valign="top">
                                                    <%for (int i = 0; i < folderpbro.getRowCount(); i++) {%>

                                                    <div id="rep-<%=folderpbro.getFieldValueString(i, 0)%>"></div>
                                                    <%}%>
                                                </td> </tr><tr> <td> <br><br>
                                                </td>
                                            </tr>
                                        </table>
                                    </div>
                                </div>
                                <div id="fadeBusRole" class="blackBus"></div>
                                <%} catch (Exception e) {
                                                    e.printStackTrace();
                                                }%>
                                <div id="fadestart" class="black_start"></div>
                                <div id="composeMessageDialog" title="Compose Message" style="display:none">
                                    <iframe id="composeMessageFrame" src='about:blank'  height="100%" width="100%" frameborder="0" scrolling="no"></iframe>
                                </div>

                                <div id="reportSchedulerDialog" title="Report Scheduler" style="display:none">
                                    <iframe id="reportSechFrame" src="ReportScheduler/pbreportScheduler.jsp?ReportType=D&repName=<%=pagename%>&REPORTID=<%=PbReportId%>"  height="100%" width="100%" frameborder="0"  align="center"></iframe>
                                </div>
                                <div id="Createdash" title="SaveAs NewDashboard" style="display:none">
                                    <center>
                                        <br>
                                        <table style="width:100%" border="0">
                                            <tr>
                                                <td valign="top" class="myHead" style="width:40%">Dashboard Name</td>
                                                <td valign="top" style="width:60%">
                                                    <input type="text" maxlength="35" name="dashboardName" style="width:150px;border:1px solid #2191C0" class="myTextbox3" id="dashboardName" onkeyup="dashDesc()" onfocus="document.getElementById('dashboardsave').disabled = false;"><br><span id="duplicateDashboard" style="color:red"></span>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td  valign="top" class="myHead" style="width:30%">Description</td>
                                                <td valign="top" style="width:70%">
                                                    <textarea name="dashboardDesc" id="dashboardDesc" style="width:150px;border:1px solid #2191C0"></textarea>
                                                </td>
                                            </tr>
                                        </table>
                                        <table>
                                            <tr>
                                                <td><input type="button" class="navtitle-hover" style="width:auto" value="Next" id="dashboardsave" onclick="saveDashboard()"></td>
                                            </tr>
                                        </table>
                                    </center>
                                </div>

                                <div id="renameDashboarddiv" title="Rename Dashboard" style="display:none;">
                                    <table align="center">
                                        <tr>
                                            <td>Old Name</td>
                                            <td><input type="text" disabled id="oldName" name="oldName"></td>
                                        </tr>
                                        <tr>
                                            <td>New Name</td>
                                            <td><input type="text" id="newName" name="newName"></td>
                                        </tr> <tr> <td colspan="2"><br/>  </td></tr> <tr><td colspan="2" align="center">
                                                <input class="navtitle-hover" style="width:auto" type="submit" value="Rename Dashboard" onclick="createNewNameForDashboard()" >
                                            </td>
                                        </tr><input type="hidden" id="renamesubmit" name="renamesubmit" value="">
                                    </table> </div>

                                <div id="scorecardDialog" style="display:none" title="Add Scorecard">
                                    <iframe id="scardDispmem" NAME='scardDispmem' width="100%" height="100%" frameborder="0" SRC='about:blank'></iframe>
                                </div>
                                <div id="graphsDialog" style="display:none" title="Add Graph">
                                    <iframe id="dataDispmem" NAME='dataDispmem' width="100%" height="100%" frameborder="0" SRC='about:blank'></iframe>
                                </div>
                                <div id="kpisDialog1" style="display:none">
                                    <table><tr><td>
                                                <input id="tableList" type="checkbox" onclick="getDisplayTables('<%=request.getContextPath()%>','<%=params%>',dashBoardIdFact,dashletIdFact,kpiMasterIdFact,kpitypFact,kpidashidFact,'<%=folderDetails%>')">All</td>
                                            <td id="tabListDiv" ><input type="textbox" id="tabsListVals"><input type="textbox" style="display:none;" id="tabsListIds">
                                                <div id="paramVals" class="ajaxboxstyle" style="display:none;overflow: auto;"></div></td>
                                            <td id="tablistLink" ><a href="javascript:void(0)" class="ui-icon ui-icon-note" onclick="showList('<%=request.getContextPath()%>','<%=params%>','<%=folderDetails%>')" ></a></td>
                                            <td id="goButton" onclick="setValueToContainer('<%=request.getContextPath()%>','<%= PbReportId%>',dashBoardIdFact,dashletIdFact,kpiMasterIdFact,kpitypFact,kpidashidFact,'<%=folderDetails%>')"><input type="button" value="GO" class="navtitle-hover"></td>
                                        </tr></table>
                                    <iframe id="kpidataDispmem1" NAME='kpidataDispmem1' width="100%" height="100%" frameborder="0" SRC='about:blank'></iframe></div>
                                <div id="kpisDialog" style="display:none">
                                    <iframe id="kpidataDispmem" NAME='kpidataDispmem' width="100%" height="100%" frameborder="0" SRC='about:blank'></iframe></div>
                                <div id="ReorderkpisDialog" style="display:none">
                                    <iframe id="kpiReOrder" NAME='kpiReOrder' width="100%" height="100%" frameborder="0" SRC='about:blank'></iframe></div>
                                <div id="kpiTypeDialog" style="display:none" title="Edit KPI Type">
                                    <div id="KpiExport" style="display:none">
                                        <iframe id="exportKPi" NAME='exportKPi' width="100%" height="100%" frameborder="0" SRC="TableDisplay/pbDownload.jsp"></iframe></div>
                                    <table border="1" width="100%">
                                        <tbody id="editKpiType">

                                        </tbody>
                                    </table>
                                    <table align="center" width="100%"><tr><td><input type="button" name="Done" value="Done" onclick="updateKpiType()" class="navtitle-hover"/></td></tr></table>
                                </div>
                                <div id="RenameDialog" style="display:none" Name="Rename" title="KPIs">
                                    <table border="1">
                                        <thead id="editKpiNameHead" align="center"  >
                                        <th>
                                            <strong>Old KPI Name</strong>
                                        </th>
                                        <th>
                                            <strong>New KPI Name</strong>
                                        </th>
                                        </thead>
                                        <tbody id="editKpiNameTbody">

                                        </tbody>
                                    </table>
                                    <table align="center"><tr><td><input type="button" name="Save" value="Save" onclick="updateKpiName()" class="navtitle-hover"/></td></tr></table>
                                </div>
                                <div id="DashletRename" style="display:none" Name="Rename" title="DashletRename">
                                    <table id="Dashlet" align="center"><br><br>
                                        <tr><td>Old Dashlet Name</td><td><input type="text" name="oldDashletName" readonly id="oldDashLetName" value="" > </td></tr>

                                        <tr><td>New Dashlet Name</td><td><input type="text" name="newDashLetName" id="newDashLetName" value="" > </td></tr>

                                    </table><br>
                                    <table align="center">
                                        <tr align ="center" colspan="4">
                                            <td align ="center" colspan="4">
                                                <input align="center" type="button" name="Save" value="Done" class="navtitle-hover" onclick="updateDashletName()"/>
                                            </td>
                                        </tr>
                                    </table>
                                </div>
                                <div id="TableRename" style="display:none" Name="Rename" title="DashletRename">
                                    <table id="Dashlet" align="center"><br><br>
                                        <tr><td>Old Dashlet Name</td><td><input type="text" name="oldDashLetName" readonly id="oldTableName" value="" > </td></tr>

                                        <tr><td>New Dashlet Name</td><td><input type="text" name="newDashLetName" id="newTableName" value="" > </td></tr>
                                        <tr></tr>
                                        <tr></tr>
                                        <tr></tr>
                                        <tr>
                                            <td align ="center" colspan="4">
                                                <input type="button" name="Save" value="Done" class="navtitle-hover" onclick="updateTableName()" >
                                            </td> </tr> </table> </div> <div id="kpisGraphsDialog" style="display:none" title="KPI Graph">
                                    <iframe id="kpigraphdataDispmem" NAME='kpigraphdataDispmem' width="100%" height="100%" frameborder="0" SRC='about:blank'></iframe>
                                </div>
                                <div id="kpiDrillDialog" style="display:none" title="KPI Drill">
                                    <iframe id="kpiDrillDispmem" NAME='kpidrilldataDispmem' width="100%" height="100%" frameborder="0" SRC='about:blank'></iframe>
                                </div>
                                <div id="kpiComment" class="commentDialog" title="Comments" style="display:none">
                                    <iframe src='about:blank' id="kpiCommentFrame" frameborder="0" height="100%" width="100%" ></iframe>
                                </div>
                                <div id="kpigrptargetType" title="KPI Target Type" style="display:none">
                                    <iframe src='about:blank' id="kpitargetTypeFrame" frameborder="0" height="100%" width="100%" ></iframe>
                                </div>
                                <div id="ZoomTarget" style="display:none"></div>
                                <div id="zoomer" class="portlet ui-widget ui-widget-content ui-helper-clearfix ui-corner-all" style="display:none;"> </div>
                                <div id="dashquickTrend" style="overflow: visible;display: none"> <iframe name="dashgraphframe" frameborder="0" id="dashgraphframe" height="500" width="800" src='about:blank'></iframe></div>
                                <div id="AddAttribute" style="display:none"></div> <div id="kpizoom" style="display:none;"  title="Graph">
                                    <%--<iframe id="kpizoomDispmem" NAME='kpigraphdataDispmem' width="100%" height="100%" frameborder="0" SRC=''></iframe>--%>
                                </div> <div id="NewDbrdGraphViewer" title="NewDashboard Graph" style="display:none">
                                    <center>
                                        <br>
                                        <table style="width:100%" border="0">
                                            <tr>
                                                <td valign="top" class="myHead" style="width:40%">DashboardGraphName</td>
                                                <td valign="top" style="width:60%">
                                                    <input id="NewDbrdGraphName"  type="text" maxlength="35" name="NewDbrdGraphName" style="width:150px;height:20px;border:1px solid #2191C0" class="myTextbox3"  ><br><br><br>
                                                </td></tr> </table><table> <tr>
                                                <td><input type="button" class="navtitle-hover" style="width:auto" value="Next" id="NewDbrdGraphsave" onclick="createDbrdGraphViewer(document.getElementById('dbrdgraph1').value,document.getElementById('hidedbrdgraph1').value)"></td> </tr>
                                        </table>
                                    </center>
                                </div>
                                <input type="hidden" name="allGraphIds" value="" id="allGraphIds">
                                <input type="hidden" name="REPIds" value="" id="REPIds">
                                <input type="hidden" name="CEPIds" value="" id="CEPIds">
                                <input type="hidden" name="MsrIds" value="" id="MsrIds">
                                <input type="hidden" name="divId" value="" id="divId">
                                <input type="hidden" name="hideDiv" value="" id="hideDiv">
                                <input type="hidden" name="kpis" value="" id="kpis">
                                <input type="hidden" name="kpiIds" value="" id="kpiIds">
                                <input type="hidden" name="allGrDetails" value="" id="allGrDetails">
                                <input type="hidden" name="dbrdId" value="<%=PbReportId%>" id="dbrdId">
                                <input type="hidden" name="kpiCommenttext" id="kpiCommenttext">
                                <input type="hidden" name="kpiCommentelmntid" id="kpiCommentelmntid">
                                <input type="hidden" name="kpiCommentmasterid" id="kpiCommentmasterid">
                                <input type="hidden" name="timeDimension" id="timeDimension">
                                <input type="hidden" name="kpiType" id="kpiType">
                                <input type="hidden" name="diffinDays" id="diffinDays">
                                <input type="hidden" name="folderId" id="folderId">
                                <input type="hidden" name="reportId" id="reportId">
                                <input type="hidden" name="graphid" value="" id="graphid">
                                <input type="hidden" name="dbrdgraph1" id="dbrdgraph1">
                                <input type="hidden" name="hidedbrdgraph1" id="hidedbrdgraph1">
                                <input type="hidden" id="chartData" name="chartData" />
                                <input type="hidden" id="viewby" name="viewby"/>
                                <input type="hidden" id="fromoneview" name="fromoneview" value="dashboard" />
                                <input type="hidden" id="viewbyIds" name="viewbyIds"/>
                                <input type="hidden" name="measure" id="measure"/>
                                <input type="hidden" name="measureIds" id="measureIds"/>
                                <input type="hidden" name="aggregation" id="aggregation"/>
                                <input type="hidden" id="type" name="type"  />
                                <input type="hidden" name="ctxPath1" id="ctxPath1" value='<%=request.getContextPath()%>'>
                                <input type="hidden" id="roleid" name="" value="<%=rolesid%>">
                                <input type="hidden" id="Designer" name="Designer" value="fromDbrd">
                                <input id="showSqlbox" type="hidden" value="<%=sqlString.replace("'", "")%>">
                                <div id="graphListDialog" style="display:none" title="Select Graphs">
                                    <table>
                                        <% ProGenChartUtilities utilities = new ProGenChartUtilities();
                                                        String str = utilities.buildGraphTypesDiv(request.getContextPath(), grpTypeskeys, GraphTypesHashMap, "getDbrdGraphColumns");
                                        %>
                                        <%=str%>
                                    </table></div>

                                <div id="graphColsDialog" title="Graph Columns" style="display:none">
                                    <iframe  id="graphCols" NAME='bucketDisp'  width="100%" height="100%" frameborder="0" SRC='about:blank'></iframe>
                                </div>
                                <div id="tableColsDialog" title="Table Columns" style="display:none">
                                    <iframe  id="tableCols" NAME='bucketDisp'  width="100%" height="100%" frameborder="0" SRC='about:blank'></iframe>
                                </div>  <div id="scoreCardActionTypesDialog" title="Select Action" style="display:none">
                                    <br>
                                    <table border="0"> <tr><td><input type="radio" id="pastOrNew" name="pastOrNew" value="new"> Create New Action</td>
                                            <td><select class="myTextbox5" align="right" name="ActionTypes" id="ActionTypes" ></select></td></tr>
                                        <tr/>
                                        <tr><td><input type="radio" id="pastOrNew" name="pastOrNew" value="past"> View Past action</td></tr>
                                    </table>
                                    <br> <br>
                                    <center><input type="button" class="navtitle-hover" value="Go" onclick="showActions()" style="width: 15%" ></center>
                                </div> <div id="scoreCardActionsDialog" title="Score Card Action" style="display:none">
                                    <iframe  id="scardActions" NAME='ScoreCard Action'  width="100%" height="100%" frameborder="0" SRC='about:blank'></iframe>
                                </div><div id="pastScoreCardActionsDialog" title="Past Score Card Action" style="display:none">
                                    <iframe  id="scardPastActions" NAME='ScoreCard Action'  width="100%" height="100%" frameborder="0" SRC='about:blank'></iframe>
                                </div>

                                <div id="MapMeasures" style="display: none" title="Map Measures">
                                    <iframe  frameborder="0" id="mapMeasureFrame" width="100%" height="100%" name="mapMeasureFrame" src='about:blank'></iframe>
                                </div>
                                <div id="graphPropertiesDiv" style="display: none" title="Graph Properties">
                                    <iframe  frameborder="0" id="graphPropertiesFrame" width="100%" height="100%" name="graphPropertiesFrame" src='about:blank'></iframe>
                                </div>
                            </td></tr></table>
        </form>

        <div id="regionDialog" style="display:none">
            <table id="regTable">
                <tr>
                    <td class="myhead" >Enter no. of Rows </td>
                    <td><Input type="text"  class="myTextbox5" name="rows" id="noOfRows" maxlength=100></td>
                </tr>
                <tr>
                    <td class="myhead" >Enter no. of Columns </td>
                    <td><Input type="text"  class="myTextbox5" name="rows" id="noOfColumns" maxlength=100></td>

                <tr>
                <input type="button" class="navtitle-hover" value="Ok" onclick="createRegion()">
                </tr>
            </table>
        </div>
        <div id="ScheduleTrackerDialog" style="display:none" title="Kpi Alerts">

        </div>
        <div id="AlertConditionDialog" style="display:none" title=" Apply Alert">
            <form action=""  name="scheduleAlertForm" id="scheduleAlertForm" method="post"></form>
        </div>
        <div id="ScheduleTrackerDialog1" style="display:none" title="Kpi Alerts">
            <table align="center" border="0" width="100%">
                <tr><td><input type="checkbox" id="dayId"  name="" checked align="left">&nbsp;&nbsp;&nbsp;Daily</td></tr>
                <tr><td><input type="checkbox" id="mtdId"  name=""  align="left">&nbsp;&nbsp;&nbsp;MTD</td></tr>
                <tr><td><input type="checkbox" id="wtdId"  name="" align="left">&nbsp;&nbsp;&nbsp;WTD</td></tr>
                <tr><td><input type="checkbox" id="qtdId"  name="" align="left">&nbsp;&nbsp;&nbsp;QTD</td></tr>
                <tr><td><input type="checkbox" id="ytdId"  name="" align="left">&nbsp;&nbsp;&nbsp;YTD</td></tr>
            </table><br>

            <table align="center"><tr><td><input type="button" id="thistracker"  class="navtitle-hover" value="Next" onClick="trackerActionNew()" ></td>
                </tr></table>
        </div>

        <div id="schedulerActionDialog" style="display:none" >
            <form action=""  name="scheduleDbrdForm" id="scheduleDbrdForm" method="post">

                <div id="scheduler" style =" display:none">
                    <table  align="center" style="width:100%;">
                        <Tr>

                            <Td class="myhead">Alert Name </Td>
                            <Td><Input type="text" name="schdReportName" id="schdReportName" maxlength=100  style="width:100%" value="" >
                            </Td>

                        </Tr>
                        <tr>
                            <td class="myhead">Email To</td>
                            <td><textarea id="usertextarea" style="width: 100%; height: 80px;margin-left:0px;" rows="" cols="" name="usertextarea"></textarea>
                            </td>
                        </tr><Tr><Td class="myhead"><%=TranslaterHelper.getTranslatedString("START_DATE", locale)%></Td>
                            <Td ><Input type="text" name="startdate" id="stDatepicker" class="mydate" maxlength=100  style="width:160px" value="" readonly />
                            </Td>
                            <td ></td> </Tr>
                        <Tr>
                            <Td class="myhead"><%=TranslaterHelper.getTranslatedString("END_DATE", locale)%> </Td>
                            <Td><Input type="text" name="enddate" id="edDatepicker" class="mydate" maxlength=100  style="width:160px" value="" readonly />
                            </Td>
                            <td ></td>

                        </Tr>
                        <tr>
                            <td class="myhead" >Time   </td>
                            <td>
                                        <table><tr>
                                                <td> <%=TranslaterHelper.getTranslatedString("HOURS", locale)%></td>
                                                <td>
                                                    <select name="hrs" id="hrs" style="width:50px">
                                                        <%for (int i = 00; i < 24; i++) {%>
                                                        <option  value="<%=i%>"><%=i%></option>
                                                        <%}%>
                                                    </select>
                                                </td>
                                                <td>
                                                    <%=TranslaterHelper.getTranslatedString("MINUTES", locale)%></td>
                                                <td>
                                                    <select name="mins" id="mins" style="width:40px">
                                                        <%for (int i = 00; i < 60; i++) {%>
                                                        <option  value="<%=i%>"><%=i%></option>
                                                        <%}%>
                                                    </select>
                                                </td>
<!--                                            </div>-->
                                            </tr></table>
                                        </td>
                                        <td></td>
                        </tr>
                        <tr>
                            <td class="myhead" > Date   </td>
                            <td>
                                <table id="dataSelection" ><tr>
                                        <td id="dailyData">
                                            <select id="dailyData" name="dailyData">
                                                <option value="globalDate">Global Date</option>
                                                <option value="current">Today Date</option>
                                                <option value="last">Yesterday Date</option>
                                            </select>
                                        </td>

                                    </tr>
                                </table>
                            </td>
                        </tr>
                        <tr>
                            <Td class="myhead" ><%=TranslaterHelper.getTranslatedString("FREQUENCY", locale)%></Td>
                            <td>
                                <table>
                                    <tr>

                                        <Td >
                                            <select name="frequency" id="frequency" class="myTextbox5" onchange="checkFrequency(this.id)" style="width:120px;margin-left:0px;">
                                                <option value="Daily" selected>Daily</option>
                                                <option value="Weekly">Weekly</option>
                                                <option value="Monthly">Monthly</option>
                                                <option value="Hourly">Hourly</option>
                                            </select>
                                        </Td>
                                        <td>
                                            <div id="onlyDateSelect" style="display:none" >
                                                Day
                                                <select name="monthDate" id="monthDate" onchange="addDate(this)">
                                                    <option value='L'>EOM</option>
                                                    <option value='B'>BOM</option>
                                                    <%for (int i = 1; i <= 31; i++) {%>
                                                    <option value='<%=i%>'><%=i%></option>
                                                    <%}%>

                                                </select>
                                            </div>
                                        </td>
                                        <td>
                                            <div id="dayOfWeek" style="display:none;width: auto">
                                                <select name="alertDay" id="alertDay">
                                                    <%for (int i = 1; i <= days.length; i++) {%>
                                                    <option value='<%=day[i - 1]%>'><%=days[i - 1]%></option>
                                                    <%}%>
                                                </select>
                                            </div>
                                        </td></tr>
                                </table>
                            </td>
                        </tr>
                        <tr id="weekday" style="display:none;">
                            <td class="myhead">Week Day</td>
                            <td>
                                <select id="particularDay" name="particularDay" style='width:100px'>
                                    <% for (int i = 0; i < scheduleday.length; i++) {%>
                                    <option value="<%=sday[i]%>"><%=scheduleday[i]%></option>
                                    <%}%>
                                </select>
                            </td>
                        </tr>
                        <tr id="monthday" style="display:none;">
                            <td class="myhead">Month Day</td>
                            <td>
                                <select id="monthParticularDay" name="monthParticularDay" style='width:100px;'>
                                    <% for (int i = 1; i <= 31; i++) {%>
                                    <option value="<%=i%>"><%=i%></option>
                                    <%}%>
                                </select>
                            </td>
                        </tr>
                        <tr id="hourlyId" style="display:none;">
                            <td class="myhead">Alert Hours</td>
                            <td>
                                <select id="particularHour" name="particularHour" style='width:100px'>
                                    <%for (int i = 00; i < 24; i++) {%>
                                    <option  value="<%=i%>"><%=i%></option>
                                    <%}%> </select></td>
                        </tr>
                        <tr> <td colspan="2">
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
                        <tr> <td colspan="2" style="height:20px"></td>
                        </tr>
                        <tr>
                            <td  id="saveScheduleReport" align="center"><input id="saveScheduleReport" class="navtitle-hover" type="button" onclick="sendKpiAlertMeasure()" value="Send Alert">
                            </td>
                        </tr>
                        <tr><td colspan="2">&nbsp; </td></tr>
                        <tr><td colspan="2" align="center"><font size="1" color="red">*</font>Please separate multiple Email Id's by comma(,).</td></tr>
                    </table>
                </div>
                <table id="innerRegionId" name="" style="display: none;">

                </table>
            </form>
        </div>
        <div id='loading' class='loading_image' style="display:none;">
            <img alt=""  id='imgId' src='<%=request.getContextPath()%>/images/help-loading.gif'  border='0px' style='position:absolute; left: 150px; top: 10px;'/>
        </div>

        <div id="trackerConditionsDialog" title="Conditions"  style="display: none">
            <form action=""  name="trackerCondForm" id="trackerDbrdForm" method="post">
                <table width="100%">
                    <tr>
                        <td style="font-weight: bolder">
                            <font style='font-size:12px;font-weight:bold;text-decoration: none;font-family:Georgia;color:#336699'>
                                <%=TranslaterHelper.getTranslatedString("ABSOLUTE_BASIS", locale)%>  :</font>
                            <input type="radio" class="navtitle-hover" name="trackerTest"   value="absolute Basis" style="width:auto" onclick="absoluteCond()" id="absolute" checked>
                        </td>
                        <td style="font-weight: bolder">
                            <div id="targetdiv" style="display:none">
                                <font style='font-size:12px;font-weight:bold;text-decoration: none;font-family:Georgia;color:#336699'>
                                    <%=TranslaterHelper.getTranslatedString("TARGET_BASIS", locale)%>  :</font>
                                <input type="radio" class="navtitle-hover" name="trackerTest" value="target Basis" id="target" style="width:auto" onclick="targetCond()" >
                            </div>
                        </td> </tr> </table> <div id="targetBasisConditions" style="display:none">
                    <table id="targetBasisConditionsTable" >
                        <tr>
                            <td colspan="2" style="height:20px"></td>
                        </tr>
                        <Tr>
                            <Td style="font-weight: bolder">
                                <font style='font-size:12px;font-weight:bold;text-decoration: none;font-family:Georgia;color:#336699'>
                                    TargetValue :</font>
                                <Input type="text"  class="myTextbox3" id="trgetVal" name="targetvalue"  style="width:100px" onkeypress="return isNumberKeyDot(event)" readonly="" >
                            </Td>
                            <Td><table><tr>
                                        <Td id="viewDeviationValue" style="display:block"><a href="javascript:void(0)" style="font-size:12px;font-weight:bold;text-decoration: none;font-family:Georgia;" title="Deviation Value">Deviation%</a></Td>
                                        <td style="width:10%"></td>
                                        <Td id="deviationPer" style="display:block"><input type="text"  id="deviationPercent"   name="deviationPercent" readonly=""></Td>
                                    </tr></table></Td>
                        </Tr>
                        <tr>
                            <Td width="40%"></Td>
                            <td width="20%"></td>
                        </tr>
                    </table>
                </div>
                <div id="absoluteBasis" style="display:block" >
                    <table id="absoluteBasisTable">
                        <tr>
                            <td colspan="2" style="height:20px"></td>
                        </tr>
                        <tr>
                            <Td style="font-weight: bolder"><font style='font-size:12px;font-weight:bold;text-decoration: none;font-family:Georgia;color:#336699'>CurentValue :</font><Input type="text"  class="myTextbox3" name="currentvalue" id="CurrentVal"  style="width:100px" readonly=""></Td>

                        </tr>
                        <tr>
                            <td colspan="2" style="height:20px"></td>
                        </tr> <Tr><td width="100%">
                                <table  width="100%">
                                    <tr>
                                        <Td style="font-weight: bolder"><font style='font-size:12px;font-weight:bold;text-decoration: none;font-family:Georgia;color:#336699'>
                                                <%=TranslaterHelper.getTranslatedString("ADD_CONDITION", locale)%>  :</font></Td>
                                        <Td width="40%"></Td>
                                        <td width="20%"></td>
                                    </tr>
                                </table>
                            </td>
                        </Tr>
                        <Tr>
                            <td width="100%">
                                <table id="condTable"  width="100%">
                                    <tr id="cond0">
                                        <td align="left" > <span id="condTD0"><%=TranslaterHelper.getTranslatedString("WHEN_VALUE", locale)%></span></td>
                                        <td>
                                            <select name="condOp" id="0condOp" onchange='addTextBox(this,"0")'>
                                                <!--                                            <option value="none">-Select-</option>-->
                                                <%for (String Str : strOprtrs) {%>
                                                <option  value="<%=Str%>"><%=Str%></option>
                                                <%}%>
                                            </select>
                                        </td>
                                        <Td>
                                            <Input type="text"  class="myTextbox3" name="sCondVal" id="0sCondVal"  style="width:120px" >
                                        </Td>
                                        <Td>
                                            <Input type="text"  class="myTextbox3" name="eCondVal" id="0eCondVal" style="width:100px;display: none" >
                                        </Td>
                                        <td>
                                            Send Mail to
                                        </td>
                                        <td>
                                            <input type="text"  id="0condMail" class="myTextbox3" name="condMail" style="width: auto" >
                                        </td>

                                        <Td>
                                            <IMG ALIGN="middle" onclick='addCondRow()' SRC="<%=request.getContextPath()%>/icons pinvoke/plus-circle.png" BORDER="0" ALT=""   title="Add Row" />
                                        </Td>
                                        <Td>
                                            <IMG ALIGN="middle" onclick='deleteCondRow("cond0")' SRC="<%=request.getContextPath()%>/icons pinvoke/cross-circle.png" BORDER="0" ALT=""   title="Delete Row" />
                                        </Td>
                                    </tr>
                                </table>
                                <table style="width:100% " align="center">
                                    <tr>
                                        <td colspan="2" style="height:10px"></td>
                                    </tr>
                                    <tr>
                                        <td colspan="2" align="center">
                                            <input type="button" class="navtitle-hover" style="width:auto" value="Done" onclick="saveEachCondition()">
                                        </td> </tr> </table></td></Tr>
                    </table>
                </div>
            </form>
        </div><div id="drillToRep" style="display:none" name="Drill To Report" tilte =" ">
            <form id="DrilltoRepForm" name ="DrilltoRepForm" method="post">
                <table id="DrillTable" name ="DrillTab">

                    <tbody id="getDrillReports"> </tbody>
                    <tfoot><tr></tr> <tr></tr> <tr></tr><tr>
                            <td colspan="4" align="center" rowspan="2">
                                <input type="button" class="navtitle-hover" style="width:auto" name="Done" value="GO" onclick="DrillToReport()" /><br><br>
                            </td>
                        </tr>
                    </tfoot>
                </table>
            </form>  </div><div id="applydbrcolrdiv" title="Apply Color Based Grouping" style="display:none">
            <iframe id="applydbrcolorframe" name="applydbrcolorframe" frameborder="0" marginheight="0" marginwidth="0" src='about:blank' width="100%" height="100%"></iframe>
        </div>
        <div id="AttributesDialog" style="display:none" title="Attributes">
            <form action=""  name="AttributesForm" id="AttributesForm" method="post">
                <table width="100%">
                    <tr>
                        <td width="9%">
                            KpiRegion In NumberFormat <input id="globalnumberformat" name="globalnumberformat" checked="yes" type="checkbox" value='true' onclick="kpiregionnumberformat()"  >
                        </td>
                    </tr>
                    <tr></tr><tr></tr>
                </table>
                <table width="100%" name="atrTable1" id="atrTable1">
                    <thead>
                    <th><input type="checkbox" id="checkformata" name="checkformatselect"  onclick="return selectall()">
                    </th>
                    <th width="13%">ElementName</th>
                    <th>Symbols</th>
                    <th>Alignment</th>
                    <th>Font Color</th>
                    <th width="16%">Row BackGround Color</th>
                    <th width="13%">Negative value</th>
                    <th width="13%">Number Format</th>
                    <th width="12%">Round</th>
                    </thead>
                    <tbody id="attributeTbody">
                    <table>
                        <tr>
                            <td align="right" style="width:2%">
                                <input type="checkbox"  style="visibility:hidden">
                            </td>
                            <td  valign="center" style="width:13%">
                                <input  type="text"  style="visibility:hidden;width:85%">
                            </td>
                            <td align="center" style="width:9%" id="headerkpiSymbol1">
                                <select name="kpiSymbols" id="kpiSymbols1" style="width:85px;">
                                    <%for (String Str : kpiSymbols) {%>
                                    <option  value="<%=Str%>" ><%=Str%></option>
                                    <%}%>
                                </select>
                            </td>

                            <td valign="right" style="width:9%" id="headerkpiAlignId1">
                                <select name="kpiAlignment" id="kpiAlignment1" style="width:95px;">
                                    <option value="left" selected="left">Left</option>
                                    <option value="right">Right</option>
                                    <option value="center">Center</option>
                                </select>
                            </td>
                            <td valign="center" style="width:10%" id="headerKpiFontId1">
                                <select id="kpiFont1" name="kpiFont" style="width:105px;background-color: white">
                                    <% DashboardTemplateDAO fcolors = new DashboardTemplateDAO();
                                                    Map<String, String> fcolorsmap = new HashMap<String, String>();
                                                    fcolorsmap = fcolors.getcolors();
                                                    for (Map.Entry<String, String> entry : fcolorsmap.entrySet()) {
                                                                                          StringBuilder dispKpi = new StringBuilder();
                                                                                          if (entry.getValue() != "#FFFFFF") {%>
                                    <option style="color:<%=entry.getValue()%>" value="<%=entry.getValue()%>"><%=entry.getKey()%></option>
                                    <%} else {%>
                                    <option style="color:#000000" value="<%=entry.getValue()%>"><%=entry.getKey()%></option>
                                    <%}%>
                                    <% } %>
                                </select>
                            </td>
                            <td valign="center" style="width:14%" id="KpiBgId1">
                                <select id="kpiBg1" name="kpiBg" style="width:150px; background-color: white">
                                    <% DashboardTemplateDAO colors1 = new DashboardTemplateDAO();
                                                    Map<String, String> colorsmap1 = new HashMap<String, String>();
                                                    colorsmap1 = colors1.getcolors();
                                                    for (Map.Entry<String, String> entry : colorsmap1.entrySet()) {
                                                                                              StringBuilder dispKpi = new StringBuilder();
                                                                                              if (entry.getValue() != "#FFFFFF") {%>
                                    <option style="color:<%=entry.getValue()%>" value="<%=entry.getValue()%>"><%=entry.getKey()%></option>
                                    <%} else {%>
                                    <option style="color:#000000" value="<%=entry.getValue()%>"><%=entry.getKey()%></option>
                                    <%}%>
                                    <% }%>
                                </select>
                            </td>
                            <td align="center" style="width:12%" id="NegativevalueId1">
                                <select name="Negativevalue" id="Negativevalue1" style="width:125px;">
                                    <option value="" selected=""></option>
                                    <option value="Bracket">In Bracket</option>
                                    <option value="Red">In Red</option>
                                    <option value="Bracket&Red">In Bracket&Red</option>
                                </select>
                            </td>
                            <td align="center" style="width:11%" Id="headerKpiNbrFormatId1">
                                <select name="NbrFormat" id="NbrFormat1" style="width:120px">
                                    <%for (int i = 0; i < NbrFormats.length; i++) {
                                                                                if (NbrFormat.equalsIgnoreCase(NbrFormats[i])) {%>
                                    <option selected value="<%=NbrFormats[i]%>"><%=NbrFormatsDisp[i]%></option>a
                                    <%} else {%>
                                    <option value="<%=NbrFormats[i]%>"><%=NbrFormatsDisp[i]%></option>
                                    <%} }%>
                                </select>
                            </td>
                            <td align="right" style="width:10%">
                                <select name="kpiRound"id="kpiRoundId1" style="width:120px;">
                                    <% for (int i = 0; i < roundvalue.length; i++) {%>
                                    <option value="<%=roundvalue[i]%>"><%=roundtext[i]%></option>
                                    <%} %>
                                </select>
                            </td>
                        </tr>
                    </table>
                    <table name="atrTable" id="atrTable">
                        <tr  id="attributeRowId">
                            <td>
                                <input type="checkbox" id="checkformat" name="checkformatselect">
                            </td>
                            <td valign="center" style="width:30%" id="kpielementIds0">

                            </td>
                            <td valign="center" style="width:30%" id="kpiSymbolsId">
                                <select name="kpiSymbols" id="kpiSymbols" style="width:120px;" class="kpiSymbols">
                                    <%for (String Str : kpiSymbols) {%>
                                    <option  value="<%=Str%>" ><%=Str%></option>
                                    <%}%>
                                </select>
                            </td>  <td valign="center" style="width:30%" id="kpiAlignId">
                                <select name="kpiAlignment" id="kpiAlignment" style="width:120px;"  class="kpiAlignment">
                                    <option value="left" selected="left">Left</option>
                                    <option value="right">Right</option>
                                    <option value="center">Center</option>
                                </select>
                            </td>
                            <td valign="center" style="width:30%" id="KpiFontId">
                                <select id="kpiFont" name="kpiFont" style="width:120px;background-color: white" class="kpiFont">
                                    <%for (Map.Entry<String, String> entry : fcolorsmap.entrySet()) {
                                                                                                   StringBuilder dispKpi = new StringBuilder();
                                                                                                   if (entry.getValue() != "#FFFFFF") {%>
                                    <option style="color:<%=entry.getValue()%>" value="<%=entry.getValue()%>"><%=entry.getKey()%></option>
                                    <%} else {%>
                                    <option style="color:#000000" value="<%=entry.getValue()%>"><%=entry.getKey()%></option>
                                    <%}%>
                                    <% } %></select>
                            </td>
                            <td valign="center" style="width:30%" id="KpiBgId">
                                <select id="kpiBg" name="kpiBg" style="width:120px; background-color: white" class="kpiBg">
                                    <% for (Map.Entry<String, String> entry : colorsmap1.entrySet()) {
                                                                                                       StringBuilder dispKpi = new StringBuilder();
                                                                                                       if (entry.getValue() != "#FFFFFF") {%>
                                    <option style="color:<%=entry.getValue()%>" value="<%=entry.getValue()%>"><%=entry.getKey()%></option>
                                    <%} else {%>
                                    <option style="color:#000000" value="<%=entry.getValue()%>"><%=entry.getKey()%></option>
                                    <%}%> <% }%>
                                </select>
                            </td>
                            <td valign="center" style="width:30%" id="NegativevalueId">
                                <select name="Negativevalue" id="Negativevalue" class="Negativevalue" style="width:120px;">
                                    <option value="" selected=""></option>
                                    <option value="Bracket">In Bracket</option>
                                    <option value="Red">In Red</option>
                                    <option value="Bracket&Red">In Bracket&Red</option>
                                </select>
                            </td>
                            <td valign="center" style="width:30%" Id="KpiNbrFormatId">
                                <select name="NbrFormat" id="NbrFormat" class="NbrFormat" style="width:120px">
                                    <%for (int i = 0; i < NbrFormats.length; i++) {
                                                                                if (NbrFormat.equalsIgnoreCase(NbrFormats[i])) {%>
                                    <option selected value="<%=NbrFormats[i]%>"><%=NbrFormatsDisp[i]%></option>a
                                    <%} else {%>
                                    <option value="<%=NbrFormats[i]%>"><%=NbrFormatsDisp[i]%></option>
                                    <%}}%>
                                </select>
                            </td>
                            <td valign="right" style="width:30%">
                                <select name="kpiRound"id="kpiRoundId" style="width:120px;" class="kpiRoundId">
                                    <% for (int i = 0; i < roundvalue.length; i++) {%>
                                    <option value="<%=roundvalue[i]%>"><%=roundtext[i]%></option>
                                    <%}%>
                                </select> </td></tr>  </table><br />
                    <center>
                        <input type="button"  class="navtitle-hover" style="width:auto" value="Done" onclick="saveKpiSymbol()">
                    </center> </tbody>
                </table>
            </form>
        </div> <div id="showCal1" style="display:none" title="Period Selection" align="center">
            <table>
                <tr><td><br></td></tr>
                <tr><td>Select Year : </td> <td><select id="calyear" name="calyear" style="width:90px">
                            <option value="Select-Year"> Select-Year </option>
                            <% for (int year = 2000; year <= 2020; year++) {%>
                            <option value="<%=year%>"> <%=year%> </option>
                            <%}%>
                        </select></td></tr><tr><td><br></td></tr><tr><td><br></td></tr>
                <tr rowspan="2"><td colspan="2"align="center"><input id="myyear1" type="button" value="Ok" class="navtitle-hover" style="width:40px;height:25px;color:black" onclick="saveyeardash()"/>
                    </td></tr></table></div><div id="ParameterSave" style="display: none"name="SelectingParameters" title="Override Dashboard">
            <table id="OverrideParameters" name="Parameters">
                <tr><td>
                        Do You Want To Save  </td>
                    <td id="SelectedParameter" name="SelectedParams">
                        <select id="param" name ="params">
                            <option value="1">Date and DateRelated</option>
                            <option value="2">Not Date but DateRelated</option>
                            <option value="3">Other than Date and DateRelated</option>
                        </select>
                    </td>
                </tr>
                <tr><td>&nbsp;</td></tr>
                <tr><td>&nbsp;</td></tr>
                <tr> <td align ="center" colspan="4">
                        <input type="button" name="Done" value="Done" class="navtitle-hover" onclick="saveParameter()">
                    </td></tr> </table>
        </div>
        <div id="TopBottomDeshlet" style="display: none"title="Top/Bottom">
            <table align="center" width="100%">
                <tr><td align="left" width="30%">Measure</td>
                    <td align="left" width="70%" id="selectMeasure"> </td>
                </tr>&nbsp;&nbsp;<tr id="RowCount">
                    <td align="left" width="30%">Row Count</td>
                    <td align="left" width="70%"><input type="text" id="topBottomValue" onkeypress="return isNumberKey(event)" name="topBottomValue" value='' size="17"></td>
                </tr><tr>
                    <td colspan="2">
                        <table>
                            <tr>
                                <td align="left"><input type="radio" name="sortRadio" value="top">Top&nbsp;&nbsp</td>
                                <td align="middle"><input type="radio" name="sortRadio" value="buttom">Bottom&nbsp;&nbsp</td>
                                <td align="right"><input type='checkbox'  id="allCheck" name="allCheck" title='Display all rows' value='true' onclick="chengeSortOPE()">All</td>
                            </tr></table></td></tr> <tr><td>&nbsp;&nbsp;</td></tr>
                <tr><td align="center" colspan="2"><input type="button" name="Go" value="Go" class="navtitle-hover" onclick="topBottom()"></td>
                </tr></table> </div>
        <div id="sort" style="display: none" title="Sort">
            <table align="center">
                <tr>
                    <td class="myhead" >Measure Names</td>
                    <td align="right" id="SortMeasure"></td>
                </tr>
                <tr>
                    <td class="myhead" >Sort Order</td>
                    <td id="SortOrder"></td>
                </tr><tr><td>&nbsp;&nbsp;</td></tr>
                <tr>
                    <td align="center" colspan="2">
                        <input type="button" value="GO" id="GoButton" align="center" onclick="goButtonforsort()"/>
                    </td>
                </tr>
            </table>
        </div>
        <div id="ColorDiv" style="display: none">
            <table><tr><td>Measure Names</td>
                    <td id="SelectMeasureforColor"></td></tr>
                <tr><td><input type="button" value="GO" onclick="saveappliedColors()"></td></tr>
            </table>
        </div>
        <div id="GraphRenameDialog" style="display: none"title="Graph Rename">
            <table>
                <tr>
                    <td class="myhead">Old Name</td>
                    <td id="graphName">
                        <input type="text" readonly id="graphNameVal">
                    </td> </tr><tr><td class="myhead">New Name</td>
                    <td><input type="text" id="NewgraphName" maxlength="100">
                    </td></tr><tr><td>&nbsp;&nbsp;</td></tr>
                <tr><td align="center" colspan="2"><input type="button" value="Save" onclick="updateGraphName()"/></td></tr>
            </table>
        </div><div id="RelatedGraphsDialog"><div id="GraphdrillToRep"></div>
            <div id="DbrdRelatedGraphsDialog" title="Define Related Graphs"></div><div id="KpiHeadsRename" style="display: none">
                <table id="KpiheadsRenameTable">
                    <thead><th>Old KPI Heads</th><th>New KPI Heads</th></thead><tbody id="Kpiheadsbody"></tbody>
                    <tfoot align="center"><td><input type="button" value="Save" class="navtitle-hover" onclick="updateKpiheads()">
                    </td> </tfoot></table></div><div id="editKpiGrDialog" style="display: none"title="KPIGraph Rename">
                <table><tr><td>Old Name</td><td id="oldKpiGrname"></td> </tr> <tr> <td>New Name</td>
                        <td id="newKpigrName"><input type="text" id="newKpigrName1" name="newKpigrName1"/></td></tr><tr>&nbsp;&nbsp;</tr><tr><td><input type="button" value="Done" class="navtitle-hover" onclick="updateKpiGrName()"/></td>
                    </tr></table> </div><span id="DashTimeSpan"> </span>    <div id="progressbar"></div>
            <div id="printId" style="display:none;" >
                <IFRAME NAME="dFrame" id="dFrame" STYLE="display:none;width:0px;height:0px"  frameborder="0" src='about:blank'></IFRAME>
            </div>
            <div id="TextkpiDrillDialog" style="display:none" title="Drill">
                <iframe id="TextkpiDrillDispmem" NAME='TextkpidrilldataDispmem' width="100%" height="100%" frameborder="0" SRC='about:blank'></iframe>
            </div>
            <div id="TextkpiComment" class="commentDialog" title="Comments" style="display:none">
                <iframe src='about:blank' id="textkpiCommentFrame" frameborder="0" height="100%" width="100%" ></iframe>
            </div>
            <div id="addTargetkpisDialog" style="display:none">
                <table><tr><td>
                            <input id="tableListtarget" type="checkbox" onclick="getDisplayTablesTargert('<%=request.getContextPath()%>','<%=params%>',targetkpityp,targetreportId,targetkpimasterid,targetdashletId,targetelementId,targettargetelem,targettargetElemname,'<%=folderDetails%>')">All</td>
                        <td id="tabListDivtarget" ><input type="textbox" id="tabsListValstarget"><input type="textbox" style="display:none;" id="tabsListIdstarget">
                            <div id="paramValstarget" class="ajaxboxstyle" style="display:none;overflow: auto;"></div></td>
                        <td id="tablistLinktarget" ><a href="javascript:void(0)" class="ui-icon ui-icon-note" onclick="showListTargert('<%=request.getContextPath()%>','<%=params%>','<%=folderDetails%>')" ></a></td>
                        <td id="goButtontarget" onclick="setValueToContainerTargert('<%=request.getContextPath()%>','<%= PbReportId%>',targetkpityp,targetreportId,targetkpimasterid,targetdashletId,targetelementId,targettargetelem,targettargetElemname,'<%=folderDetails%>')"><input type="button" value="GO" class="navtitle-hover"></td>
                    </tr></table>
                <iframe id="addTargetkpidataDispmem" NAME='addTargetkpidataDispmem' width="100%" height="100%" frameborder="0" SRC='about:blank'></iframe></div>
                <%@include file="pbStickyNote.jsp"%>
            <div title="Drill To Report" id="DrillToReportforGroups"></div>
            <div id="newReorderKpiDialogue" title="Reorder Kpi" style="display:none">
                <ul id="sortableUl" class="sortable">
                </ul><table align="center"><tbody><tr><td id="saveNewKpis"></td></tr></tbody></table>
            </div><div id="DrillToReportDialoge" title="Drill To Report" style="display:none"></div>
            <div id="dashboardKpiAlertId" style="display:none;"></div>
            <div id="OLAPGraphDialog" style="display:none" title="OLAP Graph">
                <iframe id="OLAPGraphFrame" NAME='OLAPGraphFrame' width="100%" height="100%" frameborder="0" SRC='about:blank'></iframe>
            </div> <div id="MyDialogbox" style="display:none" ></div>
            <div id="AddMoreParamsDiv" title="Add More Dimension ">
                <iframe  id="addmoreParamFrame" name='addMoreParamFrame' width="100%" height="100%" frameborder="0"   src='about:blank'></iframe>
            </div><div id="removeMoreParamsDiv" title="Remove More Dimension ">
            </div><div id="showSqlStrDialog" title="SQL Query" style="display:none">
            </div>
            <div id="showFilters" style="overflow-y: auto;display: none;border-radius: 10px;border: 1px solid grey;background-color: white;padding: 20px;width: auto;height: auto;"></div>
            <div id="editViewByDiv" title="Edit ViewBy" style="display:none">
                <iframe  id="editViewByFrame" name='editViewFrame' width="100%" height="100%" frameborder="0"   src='about:blank'></iframe>
            </div>
            <div id="HideColumns" title="HideColumns" style="display:none"><div id="HideColumnsdata"></div></div>
            <script>
            $(function() {
            <% if (timeinfo.get(1).equalsIgnoreCase("PRG_DATE_RANGE")) {%>
                    $( "#fromdate" ).datepicker({
                        showOn: "button",
                        buttonImage: "images/calendar_18x16.gif",
                        buttonImageOnly: true,
                        showButtonPanel: true,
                        changeMonth: true,
                        changeYear: true,
                        showButtonPanel: true,
                        numberOfMonths: 1,
                        stepMonths: 1,
                        dateFormat: "D,d,M,yy",
                        onClose: function showdate(){
                            var a;
                            a=($("#fromdate").val());
                            var dateArr=new Array()
                            dateArr=a.split(",");
                            if(a!=""){
                                $("#field1").html(dateArr[2]+"'"+dateArr[3].substring(2))
                                $("#field2").html(dateArr[1])
                                $("#field3").html(dateArr[0])
                            }
                        }  }).datepicker("setDate", new Date(('<%=vals1[2]%>')) );
                    $( "#todate" ).datepicker({
                        showOn: "button",
                        buttonImage: "images/calendar_18x16.gif",
                        buttonImageOnly: true,
                        showButtonPanel: true,
                        changeMonth: true,
                        changeYear: true,
                        showButtonPanel: true,
                        numberOfMonths: 1,
                        stepMonths: 1,
                        dateFormat: "D,d,M,yy",
                        onClose: function showdate(){
                            var a;
                            a=($("#todate").val());
                            var dateArr=new Array()
                            dateArr=a.split(",");
                            if(a!=""){
                                $("#tdfield1").html(dateArr[2]+"'"+dateArr[3].substring(2))
                                $("#tdfield2").html(dateArr[1])
                                $("#tdfield3").html(dateArr[0])
                            }
                        }

                    }).datepicker("setDate", new Date(('<%=vals1[3]%>')) );
                    $( "#comparefrom" ).datepicker({
                        showOn: "button",
                        buttonImage: "images/calendar_18x16.gif",
                        buttonImageOnly: true,
                        showButtonPanel: true,
                        dateFormat: "D,d,M,yy",
                        changeYear: true,
                        changeMonth: true,
                        showButtonPanel: true,
                        numberOfMonths: 1,
                        stepMonths: 1,
                        onClose: function showdate(){
                            var a;
                            a=($("#comparefrom").val());
                            var dateArr=new Array()
                            dateArr=a.split(",");
                            if(a!=""){
                                $("#cffield1").html(dateArr[2]+"'"+dateArr[3].substring(2))
                                $("#cffield2").html(dateArr[1])
                                $("#cffield3").html(dateArr[0])
                            }
                        }

                    }).datepicker("setDate", new Date(('<%=vals1[4]%>')) );
                    $( "#compareto" ).datepicker({
                        showOn: "button",
                        buttonImage: "images/calendar_18x16.gif",
                        buttonImageOnly: true,
                        showButtonPanel: true,
                        changeMonth: true,
                        changeYear: true,
                        showButtonPanel: true,
                        numberOfMonths: 1,
                        stepMonths: 1,
                        dateFormat: "D,d,M,yy",
                        onClose: function showdate(){
                            var a;
                            a=($("#compareto").val());

                            var dateArr=new Array()
                            dateArr=a.split(",");
                            if(a!=""){
                                $("#ctfield1").html(dateArr[2]+"'"+dateArr[3].substring(2))
                                $("#ctfield2").html(dateArr[1])
                                $("#ctfield3").html(dateArr[0])
                            }
                        }

                    }).datepicker("setDate", new Date(('<%=vals1[5]%>')) );
            <%} else if (timeinfo.get(1).equalsIgnoreCase("PRG_STD") && sytm.equalsIgnoreCase("No")) {%>

                    $( "#perioddate" ).datepicker({
                        showOn: "button",
                        buttonImage: "images/calendar_18x16.gif",
                        buttonImageOnly: true,
                        showButtonPanel: true,
                        changeMonth: true,
                        changeYear: true,
                        showButtonPanel: true,
                        numberOfMonths: 1,
                        stepMonths: 1,
                        dateFormat: "D,d,M,yy",

                        onClose: function showdate(){
                            var a;
                            a=($("#perioddate").val());
                            var dateArr=new Array()
                            dateArr=a.split(",");
                            if(a!=""){
                                $("#pfield1").html(dateArr[2]+"'"+dateArr[3].substring(2))
                                $("#pfield2").html(dateArr[1])
                                $("#pfield3").html(dateArr[0])
                            }
                        }
                    }).datepicker("setDate", new Date(('<%=vals1[2]%>')) );
            <%}%>
                });

                function autohide(){
                    $('#center250a').hide();
                    $('#autoshow').show();
                }
                function autoshow(){
                    $('#center250a').show();
                    $('#autoshow').hide();
                }
                function dateclick()
                {
                    $('#datetext').val('topdate');
                    var  perioddate=$('#perioddate').val();
                    var fromdate=$('#fromdate').val();
                    var todate=$('#todate').val();
                    var comparefrom=$('#comparefrom').val();
                    var compareto=$('#compareto').val();
                    $.ajax({
                        url:"<%=request.getContextPath()%>/reportViewer.do?reportBy=dateParse&perioddate="+perioddate+"&fromdate="+fromdate+"&todate="+todate+"&comparefrom="+comparefrom+"&compareto="+compareto,
                        type: 'GET',
                        async: false,
                        cache: false,
                        timeout: 30000,
                        success:function(data)
                        {
                            var data1=new Array()
                            data1=data.toString().split(",");
                            var perioddate=data1[0];
                            var fromdate=data1[0];
                            var todate=data1[1];
                            var comparefrom=data1[2];
                            var compareto=data1[3];

            <% if (timeinfo.get(1).equalsIgnoreCase("PRG_DATE_RANGE")) {%>
                            $('#datepicker1').val(fromdate);
                            $('#datepicker2').val(todate);
                            $('#datepicker3').val(comparefrom);
                            $('#datepicker4').val(compareto);
            <%} else {%>
                            $('#datepicker').val(perioddate);
            <%}%>
                        }
                    });

                }

                var filterMapNew={};
            <% if (a11 != null) {
                      for (int j = 0; j < a11.length; j++) {%>
                          filterMapNew["<%=a11[j]%>"]=new Array();  <%}
                                 }%>
                                     Array.prototype.remove = function(x){
                                         return this.filter(function(v){
                                             return v !== x;
                                         });
                                     };
                                     function globalFilter1(filName){
                                         var filName1=filName.replace(/1q1/g, " ");//replace all occurance of 1g1
                                         if(filName1==""){
                                             filName1=filName;
                                         }
                                         $("#"+filName).multiselect({
                                             selectedText: "# of # selected",
                                             noneSelectedText: filName1,
                                             selectedList: 2
                                         }).multiselectfilter();}

                                     function setMultiFilter()
                                     {
                                         var keys=Object.keys(filterMapNew)
                                         //                                              alert(keys)
                                         for(var i=0;i<keys.length;i++)
                                         {
                                             if(JSON.stringify(filterMapNew[keys[i]])==null || JSON.stringify(filterMapNew[keys[i]])=="[]" || JSON.stringify(filterMapNew[keys[i]])=="" || JSON.stringify(filterMapNew[keys[i]])=="undefined")
                                             {
                                                 var temp=[];
                                                 temp.push("All");
                                                 $("#CBOARP"+keys[i]).val(JSON.stringify(temp))
                                             }
                                             else{
                                                 //                                                       alert(JSON.stringify(filterMapNew[keys[i]]))
                                                 $("#CBOARP"+keys[i]).val(JSON.stringify(filterMapNew[keys[i]]))
                                             }
                                         }
                                     }

                                     $(document).ready(function(){
            <% for (int i = 0; i < onlyname.size(); i++) {
            %>
                    globalFilter1('<%=onlyname.get(i)%>');
            <%}%>
                    var widthe=$("#list-container").width();

                    var $item = $('div.item'), //Cache your DOM selector
                    visible = 1, //Set the number of items that will be visible
                    index = 0, //Starting index
                    endIndex = ( $item.length / visible ) - 1; //End index
                    $('div#arrowR').click(function(){
                        if(index < endIndex ){
                            index++;
                            $item.animate({'left':'-='+widthe});
                        }
                    });

                    $('div#arrowL').click(function(){
                        if(index > 0){
                            index--;
                            $item.animate({'left':'+='+widthe});
                        }
                    });
                    $("#AttributesDialog").dialog({
                        autoOpen: false,
                        height: 300,
                        width: 1000,
                        position: 'justify',
                        modal: true,
                        resizable:false
                    });
                    $("#ReorderkpisDialog").dialog({
                        autoOpen: false,
                        height: 400,
                        width: 500,
                        position: 'justify',
                        modal: true,
                        resizable:false
                    });
                    $("#kpisDialog").dialog({
                        autoOpen: false,
                        height: 460,
                        width: 600,
                        position: 'justify',
                        modal: true,
                        resizable:false
                    });
                    $("#KpiHeadsRename").dialog({
                        autoOpen: false,
                        height:300,
                        width: 400,
                        position: 'justify',
                        modal: true,
                        resizable:false
                    });
                    $("#kpisDialog1").dialog({
                        autoOpen: false,
                        height: 500,
                        width: 600,
                        position: 'justify',
                        modal: true,
                        resizable:false
                    });

                    $("#RenameDialog").dialog({
                        autoOpen: false,
                        height: 300,
                        width: 300,
                        position: 'justify',
                        modal: true,
                        resizable:false
                    });
                    $("#DashletRename").dialog({
                        autoOpen: false,
                        height: 200,
                        width: 300,
                        position: 'justify',
                        modal: true,
                        resizable:false
                    });
                    $("#kpiDrillDialog").dialog({
                        autoOpen: false,
                        height: 460,
                        width: 600,
                        position: 'justify',
                        modal: true,
                        resizable:false
                    });
                    $("#overWriteDashboard").dialog({
                        autoOpen: false,
                        height: 350,
                        width: 500,
                        position: 'justify',
                        modal: true,
                        resizable:false
                    });
                    $("#Createdash").dialog({
                        autoOpen: false,
                        height: 200,
                        width: 350,
                        position: 'justify',
                        modal: true,
                        resizable:false
                    });
                    $("#renameDashboarddiv").dialog({
                        autoOpen: false,
                        height: 200,
                        width: 350,
                        position: 'justify',
                        modal: true,
                        resizable:false
                    });
                    $("#showSqlStrDialog").dialog({
                        autoOpen: false,
                        height: 200,
                        width: 350,
                        position: 'justify',
                        modal: true,
                        resizable:false
                    });
                    $("#ScheduleTrackerDialog").dialog({
                        autoOpen: false,
                        height: 200,
                        width: 200,
                        position: 'justify',
                        modal: true
                    });
                    $("#AlertConditionDialog").dialog({
                        autoOpen: false,
                        height: 200,
                        width: 500,
                        position: 'justify',
                        modal: true
                    });
                    $("#schedulerActionDialog").dialog({
                        autoOpen: false,
                        height: 500,
                        width: 600,
                        position: 'justify',
                        modal: false,
                        resizable:false
                    });
                    $("#ScheduleTrackerDialog1").dialog({
                        autoOpen: false,
                        height: 200,
                        width: 200,
                        position: 'justify',
                        modal: true
                    });

                    $("#drillToRep").dialog({
                        autoOpen: false,
                        height:200,
                        width: 400,
                        position: 'justify',
                        modal: true,
                        resizable:false
                    });
                    $("#addTargetkpisDialog").dialog({
                        autoOpen: false,
                        height: 500,
                        width: 600,
                        position: 'justify',
                        modal: true,
                        resizable:false
                    });
                });

        </script>
                     <script type="text/javascript">
            var sortValue='';
            var viewSelectValue='';
            var dimensionValue='';
            var kpiGlMasterId="";
            var kpimasterid = "";
            var kpimasteridSym = "";
            var kpidashboardId="";
            var dashletId="";
            var refRepId = "";
            var dashletIdfortype=""
            var reportIdfortype=""
            var kpiIDs=new Array
            var idx;
            var atrIdx;
            var rowId;
            var tableCondition=""
            var elmntIdSch = ""
            var elemtName = ""
            var allHeaderNames="";
            var KpimasterSc = ""
            var dashId = ""
            var repIdSch = ""
            var devPercent;
            var drillElmntId = "";
            var drillElmntName = "";
            var drillReportId = "";
            var kpiMasterIddrill = "";
            var selectedelementId=""
            var eids;
            var enames;
            var TableFlag="";
            var dashletIdforSort = "";
            var flagforsort = "";
            var dashboardIdforcolors = "";
            var graphname = "";
            var kpiname1="";
            var iskpiWithGraph="";
            var selectrepids="";
            var isKPIDashboard1="";
            if('<%=viewSelectValue%>'!='null'){
                sortValue='<%=sortValue%>';
                viewSelectValue = '<%=viewSelectValue%>';
                dimensionValue='<%=dimensionValue%>';
            }
            var Divtitle=""
            var  PbReportId="<%=PbReportId%>"
            var firstViewByName="";
            var secondViewByName="";
            var fordesignerflag = "";
            function copyDate(eleId){
                document.getElementById(eleId+"1").value = document.getElementById(eleId).value;

            }
            var contextPath = '<%= request.getContextPath()%>';
            $(function(){
                if('<%=ddformT%>'!='null'){
                    $('#datepicker').datepicker()
                    $( "#datepicker" ).datepicker( "option", "dateFormat", '<%=ddformT%>');
                }
                $("#dashquickTrend").dialog({
                    autoOpen: false, height: 500, width: 800, position: 'justify', modal: true, resizable:true, title:'Quick Trend'

                });
                $("#breadCrumb").jBreadCrumb();
                $('#datepicker').datepicker({
                    changeMonth: true,
                    changeYear: true,
                    showButtonPanel: true,
                    numberOfMonths: 1,
                    stepMonths: 1
                });
                $('#datepicker1').datepicker({
                    changeMonth: true,
                    changeYear: true,
                    showButtonPanel: true,
                    numberOfMonths: 1,
                    stepMonths: 1
                });
                $('#datepicker2').datepicker({
                    changeMonth: true,
                    changeYear: true,
                    showButtonPanel: true,
                    numberOfMonths: 1,
                    stepMonths: 1
                });
                $('#datepicker3').datepicker({
                    changeMonth: true,
                    changeYear: true,
                    showButtonPanel: true,
                    numberOfMonths: 1,
                    stepMonths: 1
                });
                $('#datepicker4').datepicker({
                    changeMonth: true,
                    changeYear: true,
                    showButtonPanel: true,
                    numberOfMonths: 1,
                    stepMonths: 1
                });
            });
            $(function(){
                if ($.browser.msie == true){
                    $(".navigateDialog").dialog({
                        autoOpen: false,
                        height: 620,
                        width: 820,
                        position: 'justify',
                        modal: true,
                        resizable:false
                    });
                    $("#timeselect").show();

                    $("#graphsDialog").dialog({
                        autoOpen: false,
                        height: 420,
                        width: 850,
                        position: 'justify',
                        modal: true,
                        resizable:false
                    });

                    $("#scorecardDialog").dialog({
                        autoOpen: false,
                        height: 420,
                        width: 850,
                        position: 'justify',
                        modal: true,
                        resizable:false
                    });
                    $("#graphListDialog").dialog({
                        autoOpen: false,
                        height: 620,
                        width: 450,
                        position: 'justify',
                        modal: true,
                        resizable:false
                    });
                    $("#graphColsDialog").dialog({
                        autoOpen: false,
                        height: 480,
                        width: 600,
                        position: 'justify',
                        modal: true,
                        resizable:false
                    });
                    $("#tableColsDialog").dialog({
                        autoOpen: false,
                        height: 480,
                        width: 600,
                        position: 'justify',
                        modal: true,
                        resizable:false
                    });
                    $("#kpisDialog").dialog({
                        autoOpen: false,
                        height: 800,
                        width: 600,
                        position: 'justify',
                        modal: true,
                        resizable:false
                    });
                    $("#kpisDialog1").dialog({
                        autoOpen: false,
                        height: 500,
                        width: 600,
                        position: 'justify',
                        modal: true,
                        resizable:false
                    });
                    $("#AttributesDialog").dialog({
                        autoOpen: false,
                        height: 500,
                        width: 1100,
                        position: 'justify',
                        modal: true,
                        resizable:false
                    });
                    $("#kpiTypeDialog").dialog({
                        autoOpen: false,
                        height: 300,
                        width: 300,
                        position: 'justify',
                        modal: true,
                        resizable:false
                    });
                    $("#RenameDialog").dialog({
                        autoOpen: false,
                        height: 300,
                        width: 300,
                        position: 'justify',
                        modal: true,
                        resizable:false
                    });
                    $("#DashletRename").dialog({
                        autoOpen: false,
                        height: 300,
                        width: 300,
                        position: 'justify',
                        modal: true,
                        resizable:false
                    });
                    $("#TableRename").dialog({
                        autoOpen: false,
                        height: 400,
                        width: 300,
                        position: 'justify',
                        modal: true,
                        resizable:false
                    });
                    $("#ScheduleTrackerDialog").dialog({
                        autoOpen: false,
                        height: 200,
                        width: 200,
                        position: 'justify',
                        modal: true
                    });
                    $("#AlertConditionDialog").dialog({
                        autoOpen: false,
                        height: 200,
                        width: 500,
                        position: 'justify',
                        modal: true
                    });
                    $("#schedulerActionDialog").dialog({
                        autoOpen: false,
                        height: 500,
                        width: 600,
                        position: 'justify',
                        modal: false,
                        resizable:false
                    });
                    $("#ScheduleTrackerDialog1").dialog({
                        autoOpen: false,
                        height: 200,
                        width: 200,
                        position: 'justify',
                        modal: true
                    });


                    $("#kpisGraphsDialog").dialog({
                        autoOpen: false,
                        height: 560,
                        width: 600,
                        position: 'justify',
                        modal: true,
                        resizable:false
                    });

                    $("#Createdash").dialog({
                        autoOpen: false,
                        height: 300,
                        width: 350,
                        position: 'justify',
                        modal: true,
                        resizable:false
                    });
                    $("#renameDashboarddiv").dialog({
                        autoOpen: false,
                        height: 200,
                        width: 350,
                        position: 'justify',
                        modal: true,
                        resizable:false
                    });
                    $("#zoomer").dialog({
                        autoOpen: false,
                        height: 600,
                        width: 900,
                        position: 'justify',
                        modal: true,
                        resizable:false,
                        buttons:{
                            "Drill":function(){
                                getDrillRepsforGr();

                            }
                        }
                    });
                    $("#ZoomTarget").dialog({
                        autoOpen: false,
                        height: 600,
                        width: 900,
                        position: 'justify',
                        modal: true,
                        resizable:false
                    });

                    $("#kpiDrillDialog").dialog({
                        autoOpen: false,
                        height: 460,
                        width: 600,
                        position: 'justify',
                        modal: true,
                        resizable:false
                    });
                    $("#TextkpiDrillDialog").dialog({
                        autoOpen: false,
                        height: 460,
                        width: 600,
                        position: 'justify',
                        modal: true,
                        resizable:false
                    });
                    $("#TextkpiComment").dialog({
                        autoOpen: false,
                        height: 350,
                        width: 400,
                        position: 'justify',
                        modal: true,
                        resizable:false
                    });

                    $("#kpiComment").dialog({
                        autoOpen: false,
                        height: 600,
                        width: 400,
                        position: 'justify',
                        modal: true,
                        resizable:false
                    });
                    $("#ZoomTarget").dialog({
                        autoOpen: false,
                        height: 450,
                        width: 400,
                        position: 'justify',
                        modal: true,
                        resizable:false
                    });

                    $("#kpizoom").dialog({
                        autoOpen: false,
                        height: 500,
                        width: 640,
                        position: 'justify',
                        modal: true,
                        resizable:false
                    });
                    $("#NewDbrdGraphViewer").dialog({
                        autoOpen: false,
                        height: 200,
                        width: 350,
                        position: 'justify',
                        modal: true,
                        resizable:false
                    });

                    $("#scoreCardActionTypesDialog").dialog({
                        autoOpen: false,
                        height: 150,
                        width: 350,
                        position: 'justify',
                        modal: true,
                        resizable:false
                    });

                    $("#scoreCardActionsDialog").dialog({
                        autoOpen: false,
                        height: 450,
                        width: 400,
                        position: 'justify',
                        modal: true,
                        resizable:false
                    });
                    $("#pastScoreCardActionsDialog").dialog({
                        autoOpen: false,
                        height: 500,
                        width: 750,
                        position: 'justify',
                        modal: true,
                        resizable:false
                    });

                    $("#MapMeasures").dialog({
                        autoOpen: false,
                        height: 400,
                        width: 700,
                        modal: true,
                        position: 'justify',
                        resizable:false
                    });

                    $("#graphPropertiesDiv").dialog({
                        bgiframe: true,
                        autoOpen: false,
                        height: 430,
                        width: 800,
                        modal: true
                    });
                    $("#trackerConditionsDialog").dialog({
                        autoOpen: false,
                        height:480,
                        width: 700,
                        position: 'justify',
                        modal: true
                    });
                    $("#applydbrcolrdiv").dialog({
                        autoOpen: false,
                        height:350,
                        width: 750,
                        position: 'justify',
                        modal: true
                    }
                );
                    $("#showCal1").dialog({
                        autoOpen: false,
                        height: 140,
                        width: 210,
                        position: 'top',
                        modal: true

                    });
                    $("#drillToRep").dialog({
                        autoOpen: false,
                        height:200,
                        width: 400,
                        position: 'justify',
                        modal: true,
                        resizable:false
                    });
                    $("#kpidrillToRep").dialog({
                        autoOpen: false,
                        height:400,
                        width: 450,
                        position: 'justify',
                        modal: true,
                        resizable:false
                    });
                    $("#ParameterSave").dialog({
                        autoOpen: false,
                        height:200,
                        width: 400,
                        position: 'justify',
                        modal: true,
                        resizable:false
                    });
                    $("#TopBottomDeshlet").dialog({
                        autoOpen: false,
                        height:400,
                        width: 400,
                        position: 'justify',
                        modal: true,
                        resizable:false
                    });
                    $("#sort").dialog({
                        autoOpen: false,
                        height:300,
                        width: 300,
                        position: 'justify',
                        modal: true
                    });
                    $("#ColorDiv").dialog({
                        autoOpen: false,
                        height:200,
                        width: 300,
                        position: 'justify',
                        modal: true
                    });
                    $("#GraphRenameDialog").dialog({
                        autoOpen: false,
                        height:200,
                        width: 300,
                        position: 'justify',
                        modal: true
                    });
                    $("#KpiHeadsRename").dialog({
                        autoOpen: false,
                        height:500,
                        width: 400,
                        position: 'justify',
                        modal: true,
                        resizable:false
                    });
                    $("#editKpiGrDialog").dialog({
                        autoOpen: false,
                        height: 200,
                        width: 300,
                        position: 'justify',
                        modal: true,
                        resizable:false
                    });
                    $("#reportviewbys").dialog({
                        autoOpen: false,
                        height: 300,
                        width: 200,
                        position: 'justify',
                        modal: true,
                        resizable:false
                    });
                    $("#addTargetkpisDialog").dialog({
                        autoOpen: false,
                        height: 500,
                        width: 600,
                        position: 'justify',
                        modal: true,
                        resizable:false
                    });
                    $("#overWriteDashboard").dialog({
                        autoOpen: false,
                        height: 350,
                        width: 500,
                        position: 'justify',
                        modal: true
                    });
                    $("#DrillToReportforGroups").dialog({
                        autoOpen: false,
                        height: 500,
                        width: 500,
                        position: 'justify',
                        modal: true,
                        resizable:false
                    });
                    $("#ReorderkpisDialog").dialog({
                        autoOpen: false,
                        height: 400,
                        width: 500,
                        position: 'justify',
                        modal: true,
                        resizable:false
                    });
                    $("#newReorderKpiDialogue").dialog({
                        autoOpen: false,
                        height: 400,
                        width: 500,
                        position: 'justify',
                        modal: true,
                        resizable:false
                    });
                    $("#DrillToReportDialoge").dialog({
                        autoOpen: false,
                        height: 400,
                        width: 500,
                        position: 'justify',
                        modal: true,
                        resizable:false
                    });
                    $("#AddMoreParamsDiv").dialog({
                        autoOpen: false,
                        height: 550,
                        width: 550,
                        position: 'justify',
                        modal: true,
                        resizable:true
                    });
                    $("#removeMoreParamsDiv").dialog({
                        autoOpen: false,
                        height: 400,
                        width: 300,
                        position: 'justify',
                        modal: true,
                        resizable:true
                    });
                }
                else{
                    $("#AddMoreParamsDiv").dialog({
                        autoOpen: false,
                        height: 550,
                        width: 550,
                        position: 'justify',
                        modal: true,
                        resizable:true
                    });
                    $("#removeMoreParamsDiv").dialog({
                        autoOpen: false,
                        height: 400,
                        width: 300,
                        position: 'justify',
                        modal: true,
                        resizable:true
                    });
                    $("#trackerConditionsDialog").dialog({
                        autoOpen: false,
                        height:480,
                        width: 700,
                        position: 'justify',
                        modal: true
                    });
                    $(".navigateDialog").dialog({
                        autoOpen: false,
                        height: 460,
                        width: 820,
                        position: 'justify',
                        modal: true,
                        resizable:false
                    });
                    $("#timeselect").show();
                    $("#graphPropertiesDiv").dialog({
                        bgiframe: true,
                        autoOpen: false,
                        height: 280,
                        width: 800,
                        modal: true
                    });
                    $("#graphsDialog").dialog({
                        autoOpen: false,
                        height: 420,
                        width: 850,
                        position: 'justify',
                        modal: true,
                        resizable:false
                    });
                    $("#scorecardDialog").dialog({
                        autoOpen: false,
                        height: 420,
                        width: 850,
                        position: 'justify',
                        modal: true,
                        resizable:false
                    });
                    $("#graphColsDialog").dialog({
                        autoOpen: false,
                        height: 480,
                        width: 600,
                        position: 'justify',
                        modal: true,
                        resizable:false
                    });
                    $("#tableColsDialog").dialog({
                        autoOpen: false,
                        height: 480,
                        width: 600,
                        position: 'justify',
                        modal: true,
                        resizable:false
                    });
                    $("#kpiTypeDialog").dialog({
                        autoOpen: false,
                        height: 300,
                        width: 300,
                        position: 'justify',
                        modal: true,
                        resizable:false
                    });
                    $("#TableRename").dialog({
                        autoOpen: false,
                        height: 200,
                        width: 300,
                        position: 'justify',
                        modal: true,
                        resizable:false
                    });


                    $("#showCal1").dialog({
                        autoOpen: false,
                        height: 140,
                        width: 210,
                        position: 'top',
                        modal: true
                    });
                    $("#applydbrcolrdiv").dialog({
                        autoOpen: false,
                        height: 350,
                        width: 750,
                        position: 'justify',
                        modal: true,
                        resizable:false
                    });

                    $("#kpisGraphsDialog").dialog({
                        autoOpen: false,
                        height: 480,
                        width: 600,
                        position: 'justify',
                        modal: true,
                        resizable:false
                    });

                    $("#graphListDialog").dialog({
                        autoOpen: false,
                        height: 620,
                        width: 450,
                        position: 'justify',
                        modal: true,
                        resizable:false
                    });
                    $("#zoomer").dialog({
                        autoOpen: false,
                        height: 600,
                        width: 900,
                        position: 'justify',
                        modal: true,
                        resizable:false,
                        buttons:{
                            "Drill":function(){
                                getDrillRepsforGr();
                            }
                        }
                    });
                    $("#TextkpiDrillDialog").dialog({
                        autoOpen: false,
                        height: 460,
                        width: 600,
                        position: 'justify',
                        modal: true,
                        resizable:false
                    });
                    $("#TextkpiComment").dialog({
                        autoOpen: false,
                        height: 350,
                        width: 400,
                        position: 'justify',
                        modal: true,
                        resizable:false
                    });
                    $("#kpiComment").dialog({
                        autoOpen: false,
                        height: 350,
                        width: 400,
                        position: 'justify',
                        modal: true,
                        resizable:false
                    });
                    $("#ZoomTarget").dialog({
                        autoOpen: false,
                        height: 350,
                        width: 400,
                        position: 'justify',
                        modal: true,
                        resizable:false
                    });

                    $("#kpizoom").dialog({
                        autoOpen: false,
                        height: 500,
                        width: 640,
                        position: 'justify',
                        modal: true,
                        resizable:false
                    });

                    $("#NewDbrdGraphViewer").dialog({
                        autoOpen: false,
                        height: 200,
                        width: 350,
                        position: 'justify',
                        modal: true,
                        resizable:false
                    });

                    $("#scoreCardActionTypesDialog").dialog({
                        autoOpen: false,
                        height: 150,
                        width: 350,
                        position: 'justify',
                        modal: true,
                        resizable:false
                    });

                    $("#scoreCardActionsDialog").dialog({
                        autoOpen: false,
                        height: 370,
                        width: 400,
                        position: 'justify',
                        modal: true,
                        resizable:false
                    });

                    $("#pastScoreCardActionsDialog").dialog({
                        autoOpen: false,
                        height: 500,
                        width: 750,
                        position: 'justify',
                        modal: true,
                        resizable:false
                    });

                    $("#MapMeasures").dialog({
                        autoOpen: false,
                        height: 400,
                        width: 700,
                        position: 'justify',
                        modal: true,
                        resizable:false
                    });
                    $("#drillToRep").dialog({
                        autoOpen: false,
                        height:200,
                        width: 400,
                        position: 'justify',
                        modal: true,
                        resizable:false
                    });
                    $("#kpidrillToRep").dialog({
                        autoOpen: false,
                        height:400,
                        width: 450,
                        position: 'justify',
                        modal: true,
                        resizable:false
                    });
                    $("#ParameterSave").dialog({
                        autoOpen: false,
                        height:200,
                        width: 400,
                        position: 'justify',
                        modal: true,
                        resizable:false
                    });
                    $("#TopBottomDeshlet").dialog({
                        autoOpen: false,
                        height:200,
                        width: 400,
                        position: 'justify',
                        modal: true,
                        resizable:false
                    });
                    $("#sort").dialog({
                        autoOpen: false,
                        height:200,
                        width: 300,
                        position: 'justify',
                        modal: true
                    });
                    $("#ColorDiv").dialog({
                        autoOpen: false,
                        height:200,
                        width: 300,
                        position: 'justify',
                        modal: true
                    });
                    $("#GraphRenameDialog").dialog({
                        autoOpen: false,
                        height:200,
                        width: 300,
                        position: 'justify',
                        modal: true
                    });

                    $("#editKpiGrDialog").dialog({
                        autoOpen: false,
                        height: 200,
                        width: 300,
                        position: 'justify',
                        modal: true,
                        resizable:false
                    });
                    $("#reportviewbys").dialog({
                        autoOpen: false,
                        height: 300,
                        width: 200,
                        position: 'justify',
                        modal: true,
                        resizable:false
                    });

                    $("#DrillToReportforGroups").dialog({
                        autoOpen: false,
                        height: 500,
                        width: 500,
                        position: 'justify',
                        modal: true,
                        resizable:false
                    });

                    $("#newReorderKpiDialogue").dialog({
                        autoOpen: false,
                        height: 400,
                        width: 500,
                        position: 'justify',
                        modal: true,
                        resizable:false
                    });
                    $("#DrillToReportDialoge").dialog({
                        autoOpen: false,
                        height: 400,
                        width: 500,
                        position: 'justify',
                        modal: true,
                        resizable:false
                    });
                }
                if($("#frequency").val()=="1")
                    $("#timeselect").show();

                if($("#emailType").val()=="normalSub")
                    $("#toAddress").show();

                addDate(null);
            });

            $(function(){
                $("#regionDialog").dialog({
                    autoOpen: false,
                    height:360,
                    width: 590,
                    position: 'justify',
                    modal: true
                });
                $("#stDatepicker").datepicker({

                    changeMonth: true,
                    changeYear: true
                });
                $("#edDatepicker").datepicker({
                    changeMonth: true,
                    changeYear: true
                });
                $("#NbrFormat1").change(function(){
                    $(".NbrFormat").val($(this).val());
                });
                $("#kpiSymbols1").change(function(){
                    $(".kpiSymbols").val($(this).val());
                });
                $("#kpiAlignment1").change(function(){
                    $(".kpiAlignment").val($(this).val());
                });
                $("#kpiFont1").change(function(){
                    $(".kpiFont").val($(this).val());
                });
                $("#kpiBg1").change(function(){
                    $(".kpiBg").val($(this).val());
                });
                $("#kpiRoundId1").change(function(){
                    $(".kpiRoundId").val($(this).val());
                });
                $("#Negativevalue1").change(function(){
                    $(".Negativevalue").val($(this).val());
                });
            });
            var kCounter=0;
            var gCounter=0;
            var varVal=0;
            var rowCount = 0;
            var tdCount = 0;
            var grpCnt=<%=GraphsCountInDash%>;
            var tdNum=0;
            var kt=0;
            var graphIds = '';
            var graphCount=0;
            var DashIds='';
            var NewDashIds='';
            var tdarray = new Array();
            var actionTypesInitialized = false;
            var selectedScoreCard;
            var currentScore;
            var currentvalue = "";
            var targetVal="";
            var devPercent="";
            var kpitype="";

        </script>
             <script type="text/javascript">
            var isDashboardOrReport='dashboard';
            function displayfavlink(){
                $("#favlinkcont").toggle(500);
            }

            function showActionTypesDialog(scoreCardId, score){
                selectedScoreCard = scoreCardId;
                currentScore = score;
                if (actionTypesInitialized == false){
                    $.ajax({ type: 'GET',
                        async: false,
                        cache: false,
                        timeout: 30000,
                        url: 'scoreCardViewer.do?reportBy=getScorecardActionTypes',
                        success: function(data){
                            if(data!=""){
                                var json = eval('('+data+')');
                                var actionTypes = json.ActionType;
                                var dispLbls = json.DisplayLabel;
                                var html = "";
                                for (var i=0;i<actionTypes.length;i++){
                                    html = html + "<option value='"+actionTypes[i]+"'>"+dispLbls[i]+"</option>";
                                }

                                $("#ActionTypes").html(html);
                                actionTypesInitialized = true;
                            }
                        }
                    });
                }

                $("#scoreCardActionTypesDialog").dialog('open');
            }


            function closeActionsDialog(){
                $("#scoreCardActionsDialog").dialog('close');
            }
            function closePastActionsDialog(){
                $("#pastScoreCardActionsDialog").dialog('close');
            }

            function gotoInsight(elementId, kpiMasterId){
                var dashBoardId = '<%= PbReportId%>';
                document.forms.frmParameter.action="dashboardViewer.do?reportBy=getKPIInsightViewerPage&elementId="+elementId+"&kpiMasterId="+kpiMasterId+"&dashBoardId="+dashBoardId;
                document.forms.frmParameter.submit();
            }
            function createDbrdGraphViewer(divId,hideDiv){
                $("#NewDbrdGraphViewer").dialog('close');
                var grpIdsStrArray=graphIds.split(",");

                if(grpIdsStrArray.length<=2){
                    $("#graphListDialog").dialog('open');
                }
                else{
                    //alert("You can select maximum of  two Graphs")
                }
                document.getElementById('divId').value=divId;
                document.getElementById('hideDiv').value=hideDiv;

                var id=divId.replace("dispGrp","");
                document.getElementById("innertd"+id).innerHTML= document.getElementById('NewDbrdGraphName').value;
            }

            function getDbrdGraphColumns(name,typeValue){
                var divId=document.getElementById('divId').value;

                graphCount++;
                $("#graphListDialog").dialog('close');
                $("#graphColsDialog").dialog('open');
                var frameObj=document.getElementById("graphCols");
                var source = "pbDbrdGraphColsViewer.jsp?folderIds="+<%=folderDetails%>+"&grpType="+typeValue+"&gid="+graphCount+"&divId="+divId;
                frameObj.src=source;
            }

            function zoomer(divid,name)
            {

                ////////alert("divid="+divid)
            <%--$("#innerzoomerkpidiv").data('title.dialog',name)--%>
                    graphname = name
                    $("#zoomer").data('title.dialog',name)
                    $("#zoomer").html("#innerzoomerkpidiv")
                    $("#zoomer").dialog('open');

                    document.getElementById("zoomer").innerHTML=document.getElementById(divid).innerHTML;
                    var divObj = document.getElementById("zoomer");
                    var imgObjs = divObj.getElementsByTagName("img")
                    imgObjs[0].style.width = '780';
                    imgObjs[0].style.height = '500';
                    ////////alert("zoom here")Dashlets-2447

                }
                function getDrillRepsforGr(){
                    drillToReports(graphname,<%=folderDetails%>)
                }
                function ZoomTarget(imgdata){
                    //alert("target graph");
                    $("#ZoomTarget").dialog('open');
                    var divObj = document.getElementById("ZoomTarget");
                    var imgObjs = divObj.getElementsByName("imgdata");
                    imgObjs[0].style.width = '780';
                    imgObjs[0].style.height = '500';

                }
                function createDash(){
                    $("#Createdash").dialog('open');
                }

                //for insering data to dashboard desc whe key typed.
                function dashDesc()
                {
                    document.getElementById('dashboardDesc').value = document.getElementById('dashboardName').value;
                }

                function OverrideDashboard(){
                    var userType='<%=userType%>';
                    var isPAEnableforUser='<%=isPowerAnalyserEnableforUser%>'
                    if(userType=="Admin" || isPAEnableforUser=="true"){
                        $("#overWriteDashboard").dialog('open');
                    }else{
                        alert("You do not have the sufficient previlages")
                    }

                }

                function overRide(){
                    OverWriteDashboard('<%=timeDetails%>','<%=getVal%>','<%=PbReportId%>',grpCnt,document.getElementById("dbrdId").value,<%=uId%>,DashIds,NewDashIds,'<%= request.getContextPath()%>');
                }

                function Viewer_KpiDrilldown(elmntId){
                    var frameObj=document.getElementById("pbKPIDrillDownViewer");
                    frameObj.src="pbKPIDrillDownViewer.jsp?kpiId="+elmntId+"&folderIds="+<%=folderDetails%>;
                    $("#kpiDrillDialog").dialog('open');
                }

                function createDashboard()
                {
                    var userType='<%=userType%>';
                    var isPAEnableforUser='<%=isPowerAnalyserEnableforUser%>'
                    if(userType=="Admin" || isPAEnableforUser=="true"){
                        createDash();
                    }else{
                        alert("You do not have the sufficient previlages")
                    }
                }
                function cancelDashboard(){
                    document.forms.frmParameter.action="baseAction.do?param=goHome";
                    document.forms.frmParameter.submit();
                }

                function displaylink(){
                    $("#linkTable").toggle(500);
                }

                function dispPortlet(){
                    $("#tabPortlet").toggle(500);
                }

                function getCustomDateDb(){
                    var Date=""
                    if(document.getElementById("reportDate") != null && document.getElementById("reportDate").checked){
                        var htmlVar = "";
                        $("#dateRangeTab").hide();
                        $("#overWriteDashboard").height(350);
                    }else if(document.getElementById("customDate") != null && document.getElementById("customDate").checked){
                        $("#dateRangeTab").show();
                        $("#overWriteDashboard").height(500);
                    }else if(document.getElementById("currdetails") != null && document.getElementById("currdetails").checked){
                        var htmlVar = "";
                        $("#dateRangeTab").hide();
                        $("#overWriteReport").height(350);
                    }
                }
                //for adding extra kpis in kpi table
                var dashBoardIdFact = "";
                var dashletIdFact = "";
                var kpiMasterIdFact = "";
                var kpitypFact = "";
                var kpidashidFact = "";
                function addMoreKpis(dashBoardId,dashletId,kpiMasterId,folderDetails,kpityp){
                    dashBoardIdFact = dashBoardId;
                    dashletIdFact = dashletId;
                    kpiMasterIdFact = kpiMasterId;
                    kpitypFact = kpityp;
                    document.getElementById('tableList').checked=false;
                    $("#tabListDiv").show();
                    $("#tablistLink").show();
                    $("#goButton").show();
                    var kpidashid= "Dashlets-"+dashletId;
                    kpidashidFact = kpidashid;
                    var tdOBJ=document.getElementById(kpidashid);
                    if(tdOBJ==null)
                        tdOBJ=document.getElementById(dashletId);
                    var imgObjs = tdOBJ.getElementsByTagName("div")
                    document.getElementById("divId").value=imgObjs[1].id//
                    document.getElementById("folderId").value='<%=folderDetails%>'//
                    document.getElementById("reportId").value='<%=PbReportId%>'
                    var frameObj=document.getElementById("kpidataDispmem1");
                    $("#kpisDialog1").dialog('option','title','Edit KPI')
                    $("#kpisDialog1").dialog('open');
                    var source="dashboardTemplateViewerAction.do?templateParam2=getMoreKpis&foldersIds=<%=folderDetails%>&divId="+kpidashid+"&kpiType="+kpityp+"&dashboardId="+dashBoardId+"&kpiMasterId="+kpiMasterId+"&dashletId="+dashletId;
                    frameObj.src=source;
                    ////////alert(frameObj.src)

                }   function addAttributes(dashBoardId,dashletid,kpiMasterId,eid,ename,kpiType){
                    dashletId=dashletid;
                    kpidashboardId=dashBoardId;
                    kpimasteridSym = kpiMasterId;
                    iskpiWithGraph=kpiType;
                    eids=new Array();
                    eids=eid.split(",");
                    enames=new Array();
                    enames=ename.split(",");
                    $("#kpiSymbolsId").val("");
                    $("#kpiAlignId").val("");
                    $("#KpiFontId").val("");
                    $("#KpiNbrFormatId").val("");
                    $("#kpiRoundId").val("");
                    $("#attributeTbody").html("");
                    $('#atrTable tr:first').val("");
                    $("#atrTable").find("tr:gt(0)").remove();
                    if($("#atrTable").find("tr:gt(0)")){
                        for(var i=0;i<enames.length;i++){
                            if(jQuery.trim(enames[i])!=""){
                                var  td="";
                                td+="<input type='text' readonly value='"+enames[i]+"' id='"+eids[i]+"'>";
                                if(i==0){
                                    $("#kpielementIds"+i).html(td);
                                }else{
                                    var firstTr=$("#atrTable tr:first").html()
                                    if(firstTr!=null){
                                        firstTr=firstTr.toString().replace("kpielementIds0", "kpielementIds"+i,"gi")
                                        $('#atrTable tr:last').after('<tr>'+firstTr+'</tr>')
                                        $("#kpielementIds"+i).html(td);
                                    }
                                }
                            }}
                    }
                    var isMemberUseInOtherLevel="false"
                    var grpColArray=new Array
                    $.ajax({
                        url: 'dashboardTemplateViewerAction.do?templateParam2=getKpiAttributes&dashboardId='+kpidashboardId+'&masterId='+kpimasteridSym,
                        type: 'GET',
                        async: false,
                        cache: false,
                        timeout: 30000,
                        success: function(data){

                            if(data!='null'){

            <%--            $("#atrTable").find("tr").remove();--%>
                                var jsonVar=eval('('+data+')')
                                var EIdfrmHelper=jsonVar.EIdfrmHelper;
                                var EIdfrmMap=jsonVar.EIdfrmMap;
                                var ENamefrmMap=jsonVar.ENamefrmMap;
                                var ENamefrmHelper=jsonVar.ENamefrmHelper;
                                var symbols=jsonVar.symbols;
                                var alignment=jsonVar.alignment;
                                var font=jsonVar.font;
                                var nbrFormat=jsonVar.nbrFormat;
                                var round=jsonVar.round;
                                var selectedids=jsonVar.selectedids;
                                var background=jsonVar.background;
                                var negativevalue=jsonVar.negativevalue;

                                for(var i=0;i<ENamefrmMap.length;i++)
                                {
                                    if(i==0){
                                        $("#atrTable").find("tr").remove();
                                    }
                                    var rowCount =  document.getElementById("atrTable").rows.length;
                                    atrIdx = rowCount ;
                                    var rowID="atrId"+atrIdx
                                    var condHtml="";
                                    condHtml+="<tr id='"+rowID+"'><td  align='right'  style='width:2%'>";
                                    condHtml+="<input id='checkformat"+i+"' type='checkbox' name=\"checkformatselect\" >";
                                    condHtml+=" <td id='kpielementIds"+i+"' align='center' style='width:13%'>";
                                    condHtml+="<input type='text' value='"+ENamefrmMap[i]+"' style='width:85%'>";
                                    condHtml+="</td>";
                                    condHtml+="<td align=\"center\" style='width:9%' id='kpiSymbols"+i+"'>"+"<select id=\""+atrIdx+"kpiSymbols\" style=\"width:85px;\" name=\"kpiSymbols\" class=\"kpiSymbols\">";
            <%for (int j = 0; j < kpiSymbols.length; j++) {%>
                                    if(symbols[i]=='<%=kpiSymbols[j]%>')
                                    condHtml+="<option selected value=<%=kpiSymbols[j]%>><%=kpiSymbols[j]%></option>"
                                    else
                                        condHtml+="<option value=<%=kpiSymbols[j]%>><%=kpiSymbols[j]%></option>"
            <%}%>
                                    condHtml+="</select></td>";
                                    condHtml+="<td valign=\"center\" style='width:9%' id='kpialign"+i+"'>"+"<select id=\""+atrIdx+"kpiAlignment\" style=\"width:95px;\" name=\"kpiAlignment\" class=\"kpiAlignment\">";
            <%for (int j = 0; j < alignmentValue.length; j++) {%>
                                    if(alignment[i]=='<%=alignmentValue[j]%>')
                                    condHtml+="<option selected value=<%=alignmentValue[j]%>><%=alignmentText[j]%></option>"
                                    else
                                        condHtml+="<option value=<%=alignmentValue[j]%>><%=alignmentText[j]%></option>"
            <%}%>
                                    condHtml+="</select></td>";
                                    condHtml+="<td valign=\"center\" style='width:10%' id='kpiFont"+i+"'>"+"<select id=\""+atrIdx+"kpiFont\" style=\"width:105px;background-color: white\" name=\"kpiFont\" class=\"kpiFont\">";
            <%
                                          DashboardTemplateDAO colors = new DashboardTemplateDAO();
                                          Map<String, String> colorsmap = new HashMap<String, String>();
                                          colorsmap = colors.getcolors();
                                          for (Map.Entry<String, String> entry1 : colorsmap.entrySet()) {
            %>
                                    if(font[i]=='<%=entry1.getValue()%>') {
                                        if('<%=entry1.getValue()%>'!='#FFFFFF')
                                        condHtml+="<option style=\"color:<%=entry1.getValue()%>\" selected value=<%=entry1.getValue()%>><%=entry1.getKey()%></option>"
                                        else
                                            condHtml+="<option style=\"color:#000000\" selected value=<%=entry1.getValue()%>><%=entry1.getKey()%></option>"
                                    }
                                    else{
                                        if('<%=entry1.getValue()%>'!='#FFFFFF')
                                        condHtml+="<option style=\"color:<%=entry1.getValue()%>\" value=<%=entry1.getValue()%>><%=entry1.getKey()%></option>"
                                        else
                                            condHtml+="<option style=\"color:#000000\" value=<%=entry1.getValue()%>><%=entry1.getKey()%></option>"
                                    }
            <% }
            %>
                                    condHtml+="</select></td>";
                                    condHtml+="<td valign=\"center\" style='width:14%' id='kpiBg"+i+"'>"+"<select id=\""+atrIdx+"kpiBg\" style=\"width:150px;background-color: white\" name=\"kpiBg\" class=\"kpiBg\">";
            <%
                                                            colorsmap = colors.getcolors();
                                                            for (Map.Entry<String, String> entry1 : colorsmap.entrySet()) {
            %>
                                    if(background[i]=='<%=entry1.getValue()%>') {
                                        if('<%=entry1.getValue()%>'!='#FFFFFF')
                                        condHtml+="<option style=\"color:<%=entry1.getValue()%>\" selected value=<%=entry1.getValue()%>><%=entry1.getKey()%></option>"
                                        else
                                            condHtml+="<option style=\"color:#000000\" selected value=<%=entry1.getValue()%>><%=entry1.getKey()%></option>"
                                    }
                                    else{
                                        if('<%=entry1.getValue()%>'!='#FFFFFF')
                                        condHtml+="<option style=\"color:<%=entry1.getValue()%>\" value=<%=entry1.getValue()%>><%=entry1.getKey()%></option>"
                                        else
                                            condHtml+="<option style=\"color:#000000\" value=<%=entry1.getValue()%>><%=entry1.getKey()%></option>"
                                    }
            <% }
            %>
                                    condHtml+="</select></td>";
                                    condHtml+="<td align=\"center\" style='width:12%' id='Negativevalue"+i+"'>"+"<select id=\""+atrIdx+"Negativevalue\" style=\"width:125px;\" name=\"Negativevalue\" class=\"Negativevalue\">";
            <%for (int j = 0; j < negativeValue.length; j++) {%>
                                    if(negativevalue[i]=='<%=negativeValue[j]%>')
                                    condHtml+="<option selected value=<%=negativeValue[j]%>><%=negativeText[j]%></option>"
                                    else
                                        condHtml+="<option value=<%=negativeValue[j]%>><%=negativeText[j]%></option>"
            <%}%>
                                    condHtml+="</select></td>";
                                    condHtml+="<td align=\"center\" style='width:11%' id='kpiNbrFormat"+i+"'>"+"<select id=\""+atrIdx+"NbrFormat\" style=\"width:120px;\" name=\"NbrFormat\" class=\"NbrFormat\">";
            <%for (int j = 0; j < NbrFormats.length; j++) {%>
                                    if(nbrFormat[i]=='<%=NbrFormats[j]%>')
                                    condHtml+="<option selected value=<%=NbrFormats[j]%>><%=NbrFormatsDisp[j]%></option>"
                                    else
                                        condHtml+="<option value=<%=NbrFormats[j]%>><%=NbrFormatsDisp[j]%></option>"
            <%}%>
                                    condHtml+="</select></td>";
                                    condHtml+="<td align=\"right\" style='width:10%' id='kpiRounding"+i+"'>"+"<select id=\""+atrIdx+"kpiRound\" style=\"width:120px;\" name=\"kpiRound\" class=\"kpiRoundId\">";
            <%for (int j = 0; j < roundvalue.length; j++) {%>
                                    if(round[i]=='<%=roundvalue[j]%>')
                                    condHtml+="<option selected value=<%=roundvalue[j]%>><%=roundtext[j]%></option>"
                                    else
                                        condHtml+="<option value=<%=roundvalue[j]%>><%=roundtext[j]%></option>"
            <%}%>
                                    condHtml+="</select></td>";
                                    condHtml+="</tr>";
                                    if(rowCount > 0){
                                        $('#atrTable tr:last').after(condHtml);
                                    }else{
                                        $("#atrTable").html(condHtml);
                                    }
                                    for(var j=0;j<selectedids.length;j++)
                                    {
                                        if(selectedids[j]==EIdfrmHelper[i]){
                                            $("#checkformat"+i).attr('checked',true);
                                        }
                                    }
                                }
                            }
                        }
                    });
                    $("#AttributesDialog").dialog('open');
                }
                var checkViewbyId=new Array();
                function saveKpiSymbol(){
                    $("#AttributesDialog").dialog('close');
                    var RepSelectObj=document.getElementsByName("checkformatselect");
                    var selectrepids = new Array();
                    var deleterepids = new Array();
                    for(var i=0;i<RepSelectObj.length;i++){
                        if(RepSelectObj[i].checked){
                            selectrepids.push(eids[i-1])
                            RepSelectObj[i].checked=true;

                        }
                        else{
                            RepSelectObj[i].checked=false;
                        }

                    } $.post("<%=request.getContextPath()%>/dashboardTemplateViewerAction.do?templateParam2=saveKpiSymbol&kpiMasterId="+ kpimasteridSym+"&kpidashboardId="+kpidashboardId+"&eids="+encodeURIComponent(eids)+"&selectrepids="+selectrepids,$("#AttributesForm").serialize(),
                    function(data){
                        if(iskpiWithGraph=="KpiWithGraph"){
                            var divIdObj=document.getElementById("Dashlets-"+dashletId+"-kpi");
                            divIdObj.innerHTML='<center><img id="imgId" src="images/ajax.gif" align="middle"  width="75px" height="75px"  style="position:absolute" ></center>';
                            $.ajax({
                                type:'GET',
                                async:false,
                                cache:false,
                                timeout:30000,
                                url: 'dashboardViewer.do?reportBy=setKpiProperties&dashletId='+dashletId+'&dashBoardId='+kpidashboardId+'&kpiType='+iskpiWithGraph,
                                success: function(data){
                                    divIdObj.innerHTML =data;
                                }
                            });
                        }else{
                            displayKPI(dashletId, kpidashboardId, "","",kpimasteridSym, "","","","false")
                        }
                    });
                }

                //added by Mohit for Custom setting
                function saveyearDb(calname){
                    //                alert(calname);
                    if(calname=="Select-Year")
                    {alert("Please Chosse Correct Year"); }
                    else{
                        $('#datetext').val('topdate');
                        var calyear;var dateArr=new Array();
                        if(calname=="perioddate")
                        {
                            calyear = $("#calyear").val();
                            //                alert(calyear);
                            calyear="Mon,01,Jan,".concat(calyear)
                            //                alert(calyear);
                            dateArr=calyear.split(",");
                            //               alert(dateArr);
                            $('#perioddate').val(calyear);
                            //                alert($('#perioddate').val(calyear));
                            if(calyear!=""){$("#pfield1").html(dateArr[3])}
                            $("#showCal1").dialog('close');
                        }
                        if(calname=="fromdate")
                        {
                            calyear = $("#calyear").val();
                            calyear="Mon,01,Jan,".concat(calyear)
                            dateArr=calyear.split(",");
                            $('#fromdate').val(calyear);
                            if(calyear!=""){$("#field1").html(dateArr[3])}
                            $("#showCal1").dialog('close');
                        }
                        if(calname=="todate")
                        {
                            calyear = $("#calyear").val();
                            calyear="Mon,31,Dec,".concat(calyear)
                            dateArr=calyear.split(",");
                            $('#todate').val(calyear);
                            if(new Date($('#todate').val()).getTime()<new Date($('#fromdate').val()).getTime()){
                                alert("Wrong Date Selected");
                            }
                            if(calyear!=""){$("#tdfield1").html(dateArr[3])}
                            $("#showCal1").dialog('close');
                        }
                        if(calname=="comparefrom")
                        {
                            calyear = $("#calyear").val();
                            calyear="Mon,01,Jan,".concat(calyear)
                            dateArr=calyear.split(",");
                            $('#comparefrom').val(calyear);
                            if(calyear!=""){$("#cffield1").html(dateArr[3])}
                            $("#showCal1").dialog('close');
                        }
                        if(calname=="compareto")
                        {
                            calyear = $("#calyear").val();
                            calyear="Mon,31,Dec,".concat(calyear)
                            dateArr=calyear.split(",");
                            $('#compareto').val(calyear);
                            if(new Date($('#compareto').val()).getTime()<new Date($('#comparefrom').val()).getTime()){
                                alert("Wrong Date Selected");
                            }
                            if(calyear!=""){$("#ctfield1").html(dateArr[3])}
                            $("#showCal1").dialog('close');
                        }}
                }  function addAtrRow(){
                    $("#atrTable").append('<tr>'+$("#atrTable tr").html()+'</tr>');
                }
                function deleteAtrRow(){
                    var table = document.getElementById("atrTable");
                    var rowCount = table.rows.length;
                    if(rowCount>1)
                        $('#atrTable tr:last').remove();
                    else
                        alert("you can not delete all the Rows");
                }  function KpiType(kpiMasterId,dashletId,reportId){
                    kpiGlMasterId=kpiMasterId
                    dashletIdfortype=dashletId
                    reportIdfortype=reportId
                    $("#kpiTypeDialog").dialog('open');
                    $.post('dashboardTemplateViewerAction.do?templateParam2=getKpiType&kpiMasterId='+kpiMasterId,function(data){
                        if(data != "") {
                            $("#editKpiType").html(data)
                        }
                    });
                }
                function kpiRename(kpiMasterId,kpiDashboardId,kpiDashId){
                    dashId=kpiDashId
                    kpiGlMasterId=kpiMasterId
                    kpidashboardId=kpiDashboardId
                    $("#RenameDialog").dialog('open');
                    $.post('dashboardTemplateViewerAction.do?templateParam2=getKpiNewName&kpiMasterId='+kpiMasterId,function(data){
                        // alert(data)
                        if(data != "") {
                            var jsonVar=eval('('+data+')')
                            $("#editKpiNameTbody").html(jsonVar.htmlVar)
                            kpiIDs=jsonVar.ElementIds
                            // alert("kpiIDs\t"+kpiIDs)
                        }
                    });
                }
                function updateKpiName(){
                    var newNamesArray=new Array;
                    for(var i=0;i<kpiIDs.length;i++){
                        //alert("kpiIDs[i]\t"+kpiIDs[i])
                        var temp="#KpiRename"+kpiIDs[i];
                        var tempval=$(temp).val();
                        if( jQuery.trim(tempval) !="")
                            newNamesArray.push(kpiIDs[i]+"~"+$(temp).val());
                    }
                    $("#RenameDialog").dialog('close');
                    //alert("newNamesArray\t"+newNamesArray)
                    if(newNamesArray.length!=0)
                        $.post('dashboardTemplateViewerAction.do?templateParam2=getUpdatedName&kpiMasterId='+kpiGlMasterId+'&newNamesArray='+encodeURIComponent(newNamesArray)+'&kpidashboardId='+kpidashboardId,function(data){
                            displayKPI(dashId, kpidashboardId, "","",kpiGlMasterId, "","","","false")
                        });
                }
                function DashletRename(kpiMasterId,oldName,dashbrdId,kpidashId,fordesigner){
                    dashId=kpidashId;
                    kpidashboardId=dashbrdId;
                    kpimasterid=kpiMasterId;
                    fordesignerflag = fordesigner;
                    $("#DashletRename").dialog('open');
                    $("#oldDashLetName").val(oldName)
                    $("#newDashLetName").val('')

                }
                function updateDashletName(){
                    var newDashletName =$("#newDashLetName").val()
                    $("#DashletRename").dialog('close');
                    $.post('dashboardTemplateViewerAction.do?templateParam2=saveDashletName&kpiMasterId='+kpimasterid+'&newDashletName='+encodeURIComponent(newDashletName)+'&kpidashboardId='+kpidashboardId+'&fordesigner='+fordesignerflag+'&kpidashId='+dashId,function(data){
                        displayKPI(dashId, kpidashboardId, "","",kpimasterid, "","","","false")
                    } );
                }

                function TableRename(refReportId,oldName,dashltId,dbrdId,flag){
                    dashId=dashltId;
                    kpidashboardId=dbrdId;
                    refRepId=refReportId;
                    TableFlag =flag
                    $("#TableRename").dialog('open');
                    $("#oldTableName").val(oldName)

                }
                function isNumberKey(evt)
                {
                    var charCode = (evt.which) ? evt.which : event.keyCode

                    if (charCode > 31 && (charCode < 48 || charCode > 57))
                        return false;

                    return true;
                }
                function tableTranspose(dashletId,flag)
                {
                    $.ajax({
                        type:'GET',
                        async:false,
                        cache:false,
                        timeout:30000,
                        url: 'dashboardTemplateViewerAction.do?templateParam2=tableTranspose&dashletId='+dashletId+'&dashReportID=<%=PbReportId%>'+'&flag='+flag,
                        beforeSend:function(){
                            document.getElementById("Dashlets-"+dashletId).innerHTML='<center><img id="imgId" src="images/ajax.gif" align="middle"  width="100px" height="100px"  style="position:absolute" ></center>';
                        },
                        success: function(data){
                            document.getElementById("Dashlets-"+dashletId).innerHTML=data
                        }
                    });
                }


                function resetTableData(dashletId,flag){
                    $.ajax({
                        type:'GET',
                        async:false,
                        cache:false,
                        timeout:30000,
                        url: 'dashboardTemplateViewerAction.do?templateParam2=resetTableData&dashletId='+dashletId+'&dashReportID=<%=PbReportId%>'+'&flag='+flag,
                        beforeSend:function(){
                            document.getElementById("Dashlets-"+dashletId).innerHTML='<center><img id="imgId" src="images/ajax.gif" align="middle"  width="100px" height="100px"  style="position:absolute" ></center>';
                        },
                        success: function(data){

                            document.getElementById("Dashlets-"+dashletId).innerHTML=data
                        }
                    });


                }
                //added by Mohit for Custom setting
                function showYeardashDb(calname){
                    $("#showCal1").dialog('open');
                    document.getElementById("myyear1").onclick=function(){
                        saveyear(calname);
                    }
                }
                function applyColor(dashletId){
                    if(document.getElementById("colordivId"+dashletId).style.display=='none'){
                        $("#colordivId"+dashletId).show();
                    }else{
                        $("#colordivId"+dashletId).hide()
                    }

                }
                function getDashletTableOptions(dashletId){
                    var divId='DashletOptions-'+dashletId;
                    if(document.getElementById(divId).style.display=='none'){
                        document.getElementById(divId).style.display='';
                    }else{
                        document.getElementById(divId).style.display='none'
                    }

                }
                function applydbrdcolor(measVal,reportId,dashletId,flag){
                    var mesval=$("select#"+measVal).val();
                    var mesarry= new Array();
                    mesarry=mesval.split(",");
                    var columnName=mesarry[0];
                    var disColumnName=mesarry[1];
                    var labelName=mesarry[2];
                    var frameObj = document.getElementById("applydbrcolorframe");
                    frameObj.src= "TableDisplay/PbApplyColors.jsp?colmnname="+columnName+"&dispcolmname="+disColumnName+"&reportid="+reportId+"&dashletId="+dashletId+"&flag="+flag+"&labelName="+labelName+"&fromModule=dashboard";
                    $("#applydbrcolrdiv").dialog('open');
                }

                function sort_Top_Buttom(dashBoardId,kpiMasterId,dashletID,flag,rowViewByMeasureNames){
                    $("#DashletOptions-"+dashletID).hide();
                    var measures=new Array()
                    var measureNames=new Array()
                    measures=rowViewByMeasureNames.split("&")
                    var td=""
                    td+="<select id='selectmeasurefortable' title='select measure'>"
                    var measureId=document.getElementById("selectmeasurefortable");
                    for(var i=0;i<measures.length;i++){
                        measureNames=measures[i].split(",")
                        if(measureNames[0]!='null'){
                            td=td+"<option value='A_"+measureNames[0]+"'>"+measureNames[1]+"</option>"
                        }
                    }
                    td=td+"</select>"
                    $("#selectMeasure").html(td);
                    $("#TopBottomDeshlet").dialog('open')
                    dashletId=dashletID
                    TableFlag=flag
                }//sort_Top_Buttom
                function topBottom(){
                    var checkval="false"
                    var sortAll=document.getElementsByName('allCheck');
                    for(var i=0;i<sortAll.length;i++){
                        if(sortAll[i].checked){
                            checkval=sortAll[i].value
                        }
                    }
                    var sortType=$('input[name=sortRadio]:checked').val()
                    var countVal=$("#topBottomValue").val()
                    var sortByColumeVal= $("#selectmeasurefortable").val();
                    $("#TopBottomDeshlet").dialog('close')
                    if(jQuery.trim(countVal)!="" || checkval=="true")
                        $.ajax({ type: 'GET',
                            async: false,
                            cache: false,
                            timeout: 30000,
                            url: 'scoreCardViewer.do?reportBy=getDashletTable&dashletId='+dashletId+'&PbReportId=<%=PbReportId%>&sortType='+sortType+'&countVal='+countVal+'&sortByColumeVal='+sortByColumeVal+'&checkval='+checkval+'&flag='+TableFlag,
                            beforeSend:function(){
                                document.getElementById("Dashlets-"+dashletId).innerHTML='<center><img id="imgId" src="images/ajax.gif" align="middle"  width="100px" height="100px"  style="position:absolute" ></center>';
                            },
                        success: function(data){
                            document.getElementById("Dashlets-"+dashletId).innerHTML=data
                        }});
                }
                function chengeSortOPE(){
                    //alert("1")
                    var sortAll=document.getElementsByName('allCheck');
                    for(var i=0;i<sortAll.length;i++){
                        if(sortAll[i].checked){
                            $("#RowCount").hide();
                        }else{
                            $("#RowCount").show();} } }
                function updateTableName(){
                    var newTableName =$("#newTableName").val()
                    $("#TableRename").dialog('close');
                    $.ajax({
                        type:'GET',
                        async:false,
                        cache:false,
                        timeout:30000,
                        url: 'dashboardTemplateViewerAction.do?templateParam2=saveTableName&refReportId='+refRepId+'&newTableName='+newTableName+'&dashletID='+dashId+'&dashboardId='+kpidashboardId+'&flag='+TableFlag,
                        beforeSend:function(){
                            document.getElementById("Dashlets-"+dashId).innerHTML='<center><img id="imgId" src="images/ajax.gif" align="middle"  width="100px" height="100px"  style="position:absolute" ></center>';
                        },
                        success: function(data){
                            document.getElementById("Dashlets-"+dashId).innerHTML=data
                        }});
                }
                function updateKpiType(){
                    var rowCount = $('#editKpiType tr').length;

                    var kpiTupesdetails=new Array(rowCount)
                    for(var i=0;i<rowCount;i++){
                        kpiTupesdetails[i]=$("#selectKPIType"+i).val()
                    }
                    $.post('dashboardTemplateViewerAction.do?templateParam2=saveKpiTypes&kpiMasterId='+kpiGlMasterId+'&kpiTupesdetails='+kpiTupesdetails,function(data){
                        if(data == "true") {
                            $("#kpiTypeDialog").dialog('close');
                            alert("Your changes will shown when you login next time")
                            displayKPI(dashletIdfortype, reportIdfortype, "","",kpiGlMasterId, "","","","false");
                        }
                    });

                }

                function trackerActionNew(){

                    var daily = '';
                    var monthly = '';
                    var quarterly = '';
                    var weekly = '';
                    var yearly = '';
                    if($("#mtdId").is(':checked')){
                        monthly = "Month";
                    }
                    if($("#qtdId").is(':checked')){
                        quarterly = "Qtr";
                    }
                    if($("#wtdId").is(':checked')){
                        weekly = "Week";
                    }
                    if($("#dayId").is(':checked')){
                        daily = "Day";
                    }
                    if($("#ytdId").is(':checked')){
                        yearly = "Year";
                    }
                    var innerhtml = '';
                    innerhtml+="<tr><td><input type='text' id='' name='Day' value='"+daily+"'></td></tr>";
                    innerhtml+="<tr><td><input type='text' id='' name='Month' value='"+monthly+"'></td></tr>";
                    innerhtml+="<tr><td><input type='text' id='' name='Week' value='"+weekly+"'></td></tr>";
                    innerhtml+="<tr><td><input type='text' id='' name='Qtr' value='"+quarterly+"'></td></tr>";
                    innerhtml+="<tr><td><input type='text' id='' name='Year' value='"+yearly+"'></td></tr>";
                    $("#innerRegionId").html(innerhtml);

                    if(daily!='' || monthly!='' || weekly!='' || quarterly!='' || yearly!=''){
                        $("#schedulerActionDialog").dialog('option','title','KPI Alerts')
                        $("#schedulerActionDialog").dialog('open')
                        $("#ScheduleTrackerDialog1").dialog('close')
                        // $("#ScheduleTrackerDialog").dialog('close')
                        $("#scheduler").show();
                    }
                    else{
                        alert("Please Select atleast one type!")
                    }
                }
                function sendKpiAlertMeasure()
                {


                    var subject = "";
                    var sDatepicker = "";
                    var eDatepicker = "";
                    var usertextarea = "";
                    var hrs = "0";
                    subject=$("#schdReportName").val();
                    sDatepicker = $("#stDatepicker").val();
                    eDatepicker = $("#edDatepicker").val();
                    usertextarea = $("#usertextarea").val();
                    var table = document.getElementById("AlertConditionTable");
                    var totalrows=table.rows.length-1;//alert(totalrows);
                    hrs = $("#hrs").val();
                    var validate = true;

                    if(subject==""){
                        alert("Please Select Alert Name!")
                        validate = false;
                    }else if(usertextarea==""){
                        alert("Please Enter EmailId!")
                        validate = false;
                    }
                    else if(sDatepicker==""){
                        alert("Please Select Start Date!")
                        validate = false;
                    }
                    else if(eDatepicker==""){
                        alert("Please Select End Date!")
                        validate = false;
                    }

                    else if(hrs=="0"){
                        alert("Please Select Hours!")
                        validate = false;
                    }
                    var headerLogo=$("input[name='headerLogo']:checked").val();
                    var footerLogo=$("input[name='footerLogo']:checked").val();
                    var optionalHeader=$("input[name='optionalHeader']:checked").val();
                    var optionalFooter=$("input[name='optionalFooter']:checked").val();
                    var htmlSignature=$("input[name='htmlSignature']:checked").val();
                    if(validate){

                        $.post("<%=request.getContextPath()%>/dashboardTemplateViewerAction.do?templateParam2=dashboardKpiAlerts&elmntId="+elmntIdSch+'&KpimasterSc='+KpimasterSc+'&dashId='+dashId+'&repIdSch='+repIdSch+"&folderId="+'<%=folderDetails%>'+"&elemtName="+elemtName+"&totalrows="+totalrows+"&firstViewByName="+firstViewByName+"&secondViewByName="+secondViewByName+'&headerLogo='+headerLogo+'&footerLogo='+footerLogo+'&optionalHeader='+optionalHeader+'&optionalFooter='+optionalFooter+'&htmlSignature='+htmlSignature,$('#scheduleDbrdForm,#scheduleAlertForm').serialize(),
                        function(data){
                            $("#schedulerActionDialog").dialog('close');
                            alert(data)
                        }
                    );
                    }

                }
                function addEmailRow()
                {
                    var table = document.getElementById("TrackerTable");
                    var rowCount = table.rows.length;

                    idx = rowCount ;
                    var row = table.insertRow(rowCount);
                    row.id="row"+idx;
                    var tdhtml="<td width='100%'> <table width='100%' id='MailToTable'> <tr><td width='20%'>";
                    tdhtml+="<%=TranslaterHelper.getTranslatedString("MAIL_TO", locale)%></td>";

                    tdhtml+=" <td width='14%'><input type='text'  id=\""+idx+"email\" class='myTextbox3' name='mail' style='width: auto;' readonly=''></td>";
                    tdhtml+="<td width='1%'><img  align='middle' SRC='<%=request.getContextPath()%>/icons pinvoke/plus-circle.png' BORDER='0' ALT=''  onclick='addEmailRow()' title='Add Row' /></td>";
                    tdhtml+="<td width='1%'><img  align='middle' title='Delete Row' src='<%=request.getContextPath()%>/icons pinvoke/cross-circle.png' BORDER='0' ALT=''  onclick=\" deleteEmailRow('"+row.id+"')\"  /></td>";
                    tdhtml+=" <Td><IMG ALIGN='middle' onclick=\"applyConditions('"+row.id+"')\" SRC='<%=request.getContextPath()%>/icons pinvoke/tables-stacks.png' BORDER='0' ALT=''   title='ApplyConditions' /> </td>";
                    tdhtml+="</tr></table></td>";
                    row.innerHTML=tdhtml;

                }
                var condrowId
                function applyConditions(rowId)
                {condrowId=rowId.substr(3);
                    $("#trgetVal").val("");
                    $("#condTable").find("tr:gt(0)").remove();
                    $("#targetBasisConditions").hide();
                    $('input:radio[name=trackerTest]')[1].checked = false;
                    $('input:radio[name=trackerTest]')[0].checked = true;
                    $("#0sCondVal").val("");
                    $("#0condMail").val("");
                    $("#CurrentVal").val(currentvalue);
                    if(kpitype=="Basic")
                    {
                        $("#targetdiv").hide();
                        $("#targetBasisConditions").hide();
                        $("#trackerConditionsDialog").dialog("open");
                    }
                    else{
                        $("#targetdiv").show();

                        $("#trackerConditionsDialog").dialog("open");
                    }
                }

                function deleteEmailRow(rowId)
                {
                    var rowId=rowId.substr(3);
                    try {
                        var table = document.getElementById("TrackerTable");
                        var rowCount = table.rows.length;
                        if(rowCount > 3) {
                            table.deleteRow(rowId);
                            idx--;
                        }
                        else{
                            alert("You cannot delete all the rows");
                        }
                    }catch(e) {}
                }  function absoluteCond(){
                    $("#targetBasisConditions").hide();
                    $("#absoluteBasis").show();
                    var rowCount=$('#condTable tr').length;
                    for(var i=0; i<rowCount; i++){
                        $("#condTD"+i).html('');
                        $("#condTD"+i).html("When Value");
                    }
                }
                function targetCond(){
                    tableCondition="trgtBasic"
                    if(targetVal!="null")
                    {
                        $("#trgetVal").val(targetVal);
                        $("#deviationPercent").val(devPercent);
                    }
                    else{
                        $("#trgetVal").val("");
                        $("#deviationPercent").val("");
                    }
                    if(kpitype=="BasicTarget")
                        $("#targetBasisConditions").show();
                    else
                    {
                        // alert("trget");
                        $("#targetBasisConditions").hide();
                    }
                    // var tableCondition = document.getElementById("#targetConditions");
                    var rowCount=$('#condTable tr').length;
                    var tempID =""
                    for(var i=0;i<rowCount;i++){
                        tempID="#condTD"+i
                        //  alert("tempID--"+tempID+"-----")
                        $(tempID).html('');
                        $(tempID).html("When Deviation%");
                    }

                    // $("#trackerCondition").hide();
                }

                function deviationValue(){
                    // alert(currentvalue);
                    targetVal=document.getElementById("trgetVal").value;
                    // alert("targetVal\t"+targetVal);
                    devPercent=  ((currentvalue-targetVal)/targetVal)*100;

                    devPercent=Math.round(devPercent*100)/100
                    // devPercent+='%';
                    //alert("devitionvalue\t"+devPercent)
                    document.getElementById("deviationPer").style.display="";
                    $("#deviationPercent").val(devPercent+'%');

                }
                function addCondRow()
                { var rowCount =  document.getElementById("condTable").rows.length;
                    condIdx = rowCount ;
                    var rowID="cond"+condIdx
                    var condHtml="";
                    var temTrgtType=""
                    if(tableCondition=="trgtBasic"){
                        temTrgtType="When Deviation%"
                    }else{
                        temTrgtType="When Value"
                    }
                    condHtml+="<tr id='"+rowID+"'><td align='left'><span id='condTD"+condIdx+"'>"+temTrgtType+"</span></td>";
                    condHtml+="<td>";
                    condHtml+="<select name=\"condOp\" id=\""+condIdx+"condOp\" onchange=\"addTextBox(this,'"+condIdx+"')\">"+
            <%for (String Str : strOprtrs) {%>
                    "<option value='<%=Str%>'><%=Str%></option>"+
            <%}%>
                    "</select></td>";
                    condHtml+="<td><input type='text' style='width: 120px;' id=\""+condIdx+"sCondVal\" name='sCondVal' class='myTextbox3'></td>\n\
            <td><input type='text' style='width: 120px; display: none;' id=\""+condIdx+"eCondVal\" name='eCondVal' class='myTextbox3'></td>\n\
            <td>Send Mail to </td><td><input type='text' id=\""+condIdx+"condMail\" class='myTextbox3' name='condMail' style='width: auto'> </td>\n\
            <td><img border='0' align='middle' title='Add Row' alt='' src='<%=request.getContextPath()%>/icons pinvoke/plus-circle.png' onclick='addCondRow()'></td>\n\
            <td><img border='0' align='middle' title='Delete Row' alt='' src='<%=request.getContextPath()%>/icons pinvoke/cross-circle.png' onclick=\"deleteCondRow('"+rowID+"')\"></td></tr>";

                    if(rowCount > 0){
                        $('#condTable tr:last').after(condHtml);

                    }else{
                        $("#condTable").html(condHtml)
                    }

                }

                var mailArr=new Array();

                function addMail(currOrDevvalue,condop,whenvalue,mailId)
                {
                    switch(condop)
                    {
                        case '<':
                            if(currOrDevvalue<whenvalue)
                                mailArr.push(mailId);
                            break;
                        case '>':
                            if(currOrDevvalue>whenvalue)
                                mailArr.push(mailId);
                            break;

                        case '<=':
                            if(currOrDevvalue<=whenvalue)
                                mailArr.push(mailId);
                            break;

                        case '>=':if(currOrDevvalue>=whenvalue)
                                mailArr.push(mailId);
                            break;
                        case '=':
                            if(currOrDevvalue==whenvalue)
                                mailArr.push(mailId);
                            break;
                        case '<>':
                            mailArr.push(mailId);
                            break;

                    }
                }
                function deleteCondRow(rowId)
                {
                    var rowId=rowId.substr(4);
                    var table = document.getElementById("condTable");
                    var rowCount = table.rows.length;
                    if(rowCount> 1) {
                        table.deleteRow(rowId);
                        condIdx--;
                    }
                    else{
                        alert("You cannot delete all the rows");
                    }
                }

                function isNumberKeyDot(evt)
                {
                    var charCode = (evt.which) ? evt.which : event.keyCode
                    if (charCode != 46 && charCode > 31
                        && (charCode < 48 || charCode > 57))
                        return false;

                    return true;
                }
                function saveEachCondition()
                {

                    var mailIdObj=document.getElementsByName("condMail");
                    var condop=document.getElementsByName("condOp");
                    var whenvalue=document.getElementsByName("sCondVal");

                    var currOrDevvalue;
                    if(document.getElementById("absolute").checked)
                    {
                        for(var i=0;i<mailIdObj.length;i++)
                        {

                            currOrDevvalue=currentvalue;
                            addMail(currOrDevvalue,condop[i].value,whenvalue[i].value,mailIdObj[i].value);
                        }
                    }
                    else if(document.getElementById("target").checked)
                    {
                        for(var i=0;i<mailIdObj.length;i++)
                        {
                            currOrDevvalue=devPercent;
                            addMail(currOrDevvalue,condop[i].value,whenvalue[i].value,mailIdObj[i].value);
                        }
                    }

                    $("#"+condrowId+"email").val(mailArr);
                    $('#trackerConditionsDialog').dialog('close');
                    mailArr=new Array()

                }
                function addTextBox(symbol,rowId)
                {
                    var open = document.getElementById(rowId+"eCondVal");
                    if(symbol.value=="<>")
                    {
                        open.style.display='';
                    }
                    else{
                        open.style.display='none';
                    }
                }


                //for kpi graph tables when clicked on selected choices
                function showKpis(divId,type,hideDiv){
                    document.getElementById("divId").value=divId//
                    document.getElementById('hideDiv').value=hideDiv;
                    document.getElementById("folderId").value=<%=folderDetails%>//
                    document.getElementById("reportId").value=<%=PbReportId%>
                    var frameObj=document.getElementById("kpidataDispmem");
                    var source="dashboardTemplateViewerAction.do?templateParam2=getKpis&foldersIds="+<%=folderDetails%>+'&divId='+divId.id+'&kpiType='+type+'&dashboardId='+<%=PbReportId%>;
                    frameObj.src=source;
                    $("#kpisDialog").dialog('option','title','Edit KPI')
                    $("#kpisDialog").dialog('open');
                }

                //for kpi graph tables whe clicked on inner table.
                function showKpisInner(divId,type){
                    document.getElementById("divId").value=divId.id//
                    document.getElementById("folderId").value=<%=folderDetails%>//
                    document.getElementById("reportId").value=<%=PbReportId%>
                    var frameObj=document.getElementById("kpidataDispmem");
                    var source="dashboardTemplateViewerAction.do?templateParam2=getKpis&foldersIds="+<%=folderDetails%>+'&divId='+divId.id+'&kpiType='+type+'&dashboardId='+<%=PbReportId%>;
                    frameObj.src=source;
                    $("#kpisDialog").dialog('option','title','Edit KPI')
                    $("#kpisDialog").dialog('open');
                }
                //for kpi graphs
                function showKpiGraphs(divId,hideDiv){
                    document.getElementById("divId").value=divId;
                    document.getElementById('hideDiv').value=hideDiv;
                    document.getElementById("folderId").value=<%=folderDetails%>
                    var frameObj=document.getElementById("kpigraphdataDispmem");
                    var source="dashboardTemplateViewerAction.do?templateParam2=getKpisGraphs&foldersIds="+<%=folderDetails%>+'&dashboardId='+<%=PbReportId%>;
                    frameObj.src=source;
                    $("#kpisGraphsDialog").dialog('open');
                }

                function addKpiwithTarget(){
                    var KPIType="Target";
                    kt=0;
                    if(((tdNum)%2)!=0 )
                    {
                        rowCount++;
                    }
grpCnt++;
                    var tableObj = document.getElementById("DasboardTable");
                    var table = document.getElementById("DasboardTable");
                    var rowCount1 = table.rows.length;
                    document.getElementById("GraphTable2").style.display = 'none'
                    document.getElementById("GraphTable1").style.display = 'none'

                    if(tdNum==-1){
                        tdNum=0;
                    }
                    var newRow = tableObj.insertRow(rowCount);
                    newRow.id = "rowPortlet_"+rowCount;
                    var cell1 = newRow.insertCell(0);
                    cell1.id="portletTd_"+tdNum;
                    cell1.colSpan=2;
                    cell1.style.paddingLeft="30px";

                    var inDiv = document.createElement('div');
                    inDiv.id="kpidivid_"+rowCount;
                    inDiv.className = 'portlet';
                    inDiv.style.width='1080px';
                    inDiv.style.minHeight='200px';
                    var txtDiv = document.createElement('div');
                    txtDiv.className = 'portlet-header';
                    txtDiv.innerHTML = '<table width="100%" style="height:10px"><tr><td style="color:#369;font-weight:bold">KPI Region</td><td align="right"  ><a class="ui-icon ui-icon-trash" href="javascript:void(0)"  onclick="closeTargetKpi('+rowCount+','+tdNum+')" ></a> </td></tr></table>'
                    var cntDiv = document.createElement('div');
                    cntDiv.className = 'portlet-content1';
                    cntDiv.id='dispGrp'+grpCnt;
                    cntDiv.innerHTML = '';
                    //    inDiv.appendChild(txtDiv);
                    inDiv.appendChild(cntDiv);
                    cell1.appendChild(inDiv);
                    totalrows=rowCount;
                    var rc=rowCount;

                    tdarray.push("portletTd_"+tdNum);
                    if(((tdNum)%2)!=0 )
                    {
                        tdNum=tdNum+1;
                    }
                    else
                    {
                        tdNum=tdNum+2;
                    }
                    rowCount++;
                    document.getElementById("kpidivid_"+rc).style.display='none'
                    showKpis('dispGrp'+grpCnt,KPIType,"kpidivid_"+rc)

                    $(".column").sortable({
                        connectWith: '.column',
                        disabled:true
                    });
                    $(".portlet").addClass("ui-widget ui-widget-content ui-helper-clearfix ui-corner-all")
                    .find(".portlet-header")
                    .addClass("ui-widget-header ui-corner-all")
                    .end()
                    .find(".portlet-content");
                    $(".portlet-header .ui-icon").click(function() {
                        $(this).toggleClass();
                        $(this).parents(".portlet:first").find(".portlet-content").toggle();
                    });
                    $(".column").disableSelection();
                }
                var mapAdded = false;
                var mapDiv = "";

                function setMapDiv(div){
                    mapDiv = div;
                    mapAdded = true;
                }
                function addMap(){

                    if (mapAdded == false){
                        var tableObj = document.getElementById("DasboardTable");

                        document.getElementById("GraphTable2").style.display = 'none'
                        document.getElementById("GraphTable1").style.display = 'none'

                        if(tdNum==-1){//tdNum is golbal variable increases bases on created no of charts.
                            tdNum=0;
                        }

                        var newRow = tableObj.insertRow(0);
                        newRow.id = "rowPortlet_"+rowCount;

                        var cell1 = newRow.insertCell(0);
                        cell1.id="portletTd_"+tdNum;
                        cell1.colspan= "2";

                        var inDiv = document.createElement('div');
                        inDiv.className = 'portlet ';
                        //inDiv.style.width='520px';
                        inDiv.style.height='625px';
                        inDiv.id='hideDiv'+grpCnt;
                        var txtDiv = document.createElement('div');
                        txtDiv.className = 'portlet-header';
                        txtDiv.innerHTML = '<table id="addstd" width="100%" style="height:10px"><tr><td style="color:#369;font-weight:bold">Map Region</td><td id="delPortlet" width="25px" align="right"><a href="#" class="ui-icon ui-icon-trash" onclick="closePortlet('+tdNum+','+rowCount+')"></a></td></tr></table>'
                        var cntDiv = document.createElement('div');
                        cntDiv.className = 'portlet-content';
                        cntDiv.id='dispGrp'+grpCnt;
                        cntDiv.style.height='590px';
                        cntDiv.style.width='97%';
                        cntDiv.style.overflow='auto';
                        cntDiv.innerHTML = '';
                        inDiv.appendChild(txtDiv);
                        inDiv.appendChild(cntDiv);
                        cell1.appendChild(inDiv);
                        tdarray.push("portletTd_"+tdNum);
                        document.getElementById("hideDiv"+grpCnt).style.display='none'
                        if(tdNum%2 ==0)
                        {
                            tdNum++;
                            tdCount++;
                        }
                        else
                        {
                            tdNum++;
                            rowCount++;
                        }

                        showMap('dispGrp'+grpCnt,"hideDiv"+grpCnt);

                        $(".column").sortable({
                            connectWith: '.column'
                        });
                        $(".portlet").addClass("ui-widget ui-widget-content ui-helper-clearfix ui-corner-all")
                        .find(".portlet-header")
                        .addClass("ui-widget-header ui-corner-all")
                        .end()
                        .find(".portlet-content");
                        $(".portlet-header .ui-icon").click(function() {
                            $(this).toggleClass();
                            $(this).parents(".portlet:first").find(".portlet-content").toggle();
                        });
                        $(".column").disableSelection();
                    }
                    else{
                        alert("Map already added in the dashboard");
                    }
                }

                function showMap(divId, hideDiv){
                    var folderId = '<%=folderDetails%>';
                    var reportId= '<%=PbReportId%>';
                    var ctxPath = '<%= request.getContextPath()%>';
                    document.getElementById("divId").value=divId;
                    document.getElementById('hideDiv').value=hideDiv;
                    document.getElementById("folderId").value=<%=folderDetails%>//
                    document.getElementById("reportId").value=<%=PbReportId%>
                    var frameObj=document.getElementById("mapMeasureFrame");
                    var source=ctxPath+"/TableDisplay/PbChangeMapColumnsRT.jsp?folderIds="+<%=folderDetails%>+'&divId='+divId+'&dashboardId='+<%=PbReportId%>+'&editMap=false';
                    frameObj.src=source;
                    $("#MapMeasures").dialog('open');
                }

                function addSortValueToMap(){
                    var sortType = $("#sortValuesForMap").val();
                    var mapView = $("#ViewSelect").val();
                    var geoView = $("#GeoViewForMap").val();
                    submitFormMapMeasChange(mapDiv,'D',sortType,mapView,geoView);

                }
                //added by Govardhan
                function DrillToReport(){
                    $("#drillToRep").dialog('close');
                    var IDVal="#selectReport"+selectedelementId.replace(/^\s+|\s+$/g,'');
                    drillReportId =  $(IDVal).val();
                    var url= 'reportViewer.do?reportBy=viewReport&REPORTID=' + drillReportId;
                    document.frmParameter.action = url;
                    document.frmParameter.submit();
                }
                //added by mohit
                function DrillWithFilters(url){

                    setMultiFilter();
                    document.frmParameter.action = url;
                    document.frmParameter.target = "_blank";
                    document.frmParameter.submit();
                    //               alert("hello")
                    document.frmParameter.target = "";
                }
                function displayMapOrNot(){
                    if(document.getElementById("mapMenu").style.display=="none"){
                        document.getElementById("mapMenu").style.display = "block";
                        document.getElementById("DashletsColumn1_1").style.display = "block";

                        var frameObj = document.getElementById("widgetframe");
                        var frameObj1 = document.getElementById("widgetframe1");
                        var source = 'divPersistent.jsp?method=forMapMenuInDashBoard&block=yes';
                        var source1 = 'divPersistent.jsp?method=forMapDisplayInDashBoard&block=yes';
                        frameObj.src = source;
                        frameObj1.src = source1;
                        addSortValueToMap();
                    }else{
                        document.getElementById("mapMenu").style.display = "none";
                        document.getElementById("DashletsColumn1_1").style.display = "none";
                        var frameObj = document.getElementById("widgetframe");
                        var frameObj1 = document.getElementById("widgetframe1");
                        var source = 'divPersistent.jsp?method=forMapMenuInDashBoard&block=no';
                        var source1 = 'divPersistent.jsp?method=forMapDisplayInDashBoard&block=no';
                        frameObj.src = source;
                        frameObj1.src = source1;
                    }

                } function addScoreCard(){
                    var tableObj = document.getElementById("DasboardTable");

                    document.getElementById("GraphTable2").style.display = 'none'
                    document.getElementById("GraphTable1").style.display = 'none'

                    if(tdNum==-1){//tdNum is golbal variable increases bases on created no of charts.
                        tdNum=0;
                    }

                    if(parseInt(tdNum%2)==0)
                    {
                        var newRow = tableObj.insertRow(rowCount);
                        newRow.id = "rowPortlet_"+rowCount;
                        var cell1 = newRow.insertCell(0);
                        cell1.id="portletTd_"+tdNum;
                        cell1.colspan= "1";
                        var inDiv = document.createElement('div');
                        inDiv.className = 'portlet ';
                        inDiv.style.width='520px';
                        inDiv.style.height='325px';
                        inDiv.id='hideDiv'+grpCnt;

                        var txtDiv = document.createElement('div');
                        txtDiv.className = 'portlet-header';
                        txtDiv.innerHTML = '<table id="addstd" width="100%" style="height:10px"><tr><td style="color:#369;font-weight:bold">Scorecard Region</td><td id="delPortlet" width="25px" align="right"><a href="#" class="ui-icon ui-icon-trash" onclick="closePortlet('+tdNum+','+rowCount+')"></a></td></tr></table>'
                        var cntDiv = document.createElement('div');
                        cntDiv.className = 'portlet-content';
                        cntDiv.id='dispGrp'+grpCnt;
                        cntDiv.style.height='270px';
                        cntDiv.style.width='97%';
                        cntDiv.style.overflow='auto';
                        cntDiv.innerHTML = '';
                        inDiv.appendChild(txtDiv);
                        inDiv.appendChild(cntDiv);
                        cell1.appendChild(inDiv);
                        tdarray.push("portletTd_"+tdNum);
                        document.getElementById("hideDiv"+grpCnt).style.display='none'
                    }
                    else if(parseInt((tdNum%2))!=0)
                    {
                        var rowId = document.getElementById("rowPortlet_"+rowCount);
                        var cell2 = rowId.insertCell(1);
                        cell2.id="portletTd_"+tdNum;
                        var inDiv = document.createElement('div');
                        inDiv.className = 'portlet';
                        inDiv.style.width='520px';
                        inDiv.style.height='325px';
                        inDiv.id='hideDiv'+grpCnt;

                        var txtDiv = document.createElement('div');
                        txtDiv.className = 'portlet-header ';
                        txtDiv.innerHTML = '<table id="addstd" width="100%" style="height:10px"><tr><td style="color:#369;font-weight:bold">Scorecard Region</td><td id="delPortlet" width="25px" align="right"><a href="#" class="ui-icon ui-icon-trash" onclick="closePortlet('+tdNum+','+rowCount+')"></a></td></tr></table>'
                        var cntDiv = document.createElement('div');
                        cntDiv.className = 'portlet-content';
                        cntDiv.id='dispGrp'+grpCnt;
                        cntDiv.style.height='270px';
                        cntDiv.style.width='97%';
                        cntDiv.style.overflow='auto';

                        cntDiv.innerHTML = '';
                        inDiv.appendChild(txtDiv);
                        inDiv.appendChild(cntDiv);
                        cell2.appendChild(inDiv);
                        tdarray.push("portletTd_"+tdNum);

                        document.getElementById("hideDiv"+grpCnt).style.display='none';
                    }

                    if(tdNum%2 ==0)
                    {
                        tdNum++;
                        tdCount++;
                    }
                    else
                    {
                        tdNum++;
                        rowCount++;
                    }
                    showScorecard('dispGrp'+grpCnt,"hideDiv"+grpCnt);
                    $(".column").sortable({
                        connectWith: '.column'
                    });
                    $(".portlet").addClass("ui-widget ui-widget-content ui-helper-clearfix ui-corner-all")
                    .find(".portlet-header")
                    .addClass("ui-widget-header ui-corner-all")
                    .end()
                    .find(".portlet-content");
                    $(".portlet-header .ui-icon").click(function() {
                        $(this).toggleClass();
                        $(this).parents(".portlet:first").find(".portlet-content").toggle();
                    });
                    $(".column").disableSelection();
                }

                //for kpi graph tables when clicked on selected choices
                function showScorecard(divId,hideDiv){
                    document.getElementById("divId").value=divId//
                    document.getElementById('hideDiv').value=hideDiv;
                    document.getElementById("folderId").value=<%=folderDetails%>//
                    document.getElementById("reportId").value=<%=PbReportId%>
                    var frameObj=document.getElementById("scardDispmem");
                    var source="dashboardTemplateViewerAction.do?templateParam2=getScoreCards"
                    frameObj.src=source;
                    $("#scorecardDialog").dialog('open');
                }

                function addStdKpi(){
                    var KPIType="Standard";
                    kt=1;
                    grpCnt++;
                    var tableObj = document.getElementById("DasboardTable");
                    var table = document.getElementById("DasboardTable");
                    var rowCount1 = table.rows.length;

                    document.getElementById("GraphTable2").style.display = 'none'
                    document.getElementById("GraphTable1").style.display = 'none'

                    if(tdNum==-1){//tdNum is golbal variable increases bases on created no of charts.
                        tdNum=0;
                    }

                    if(parseInt(tdNum%2)==0)
                    {
                        var newRow = tableObj.insertRow(rowCount);
                        newRow.id = "rowPortlet_"+rowCount;
                        var cell1 = newRow.insertCell(0);
                        cell1.id="portletTd_"+tdNum;
                        cell1.colspan= "1";

                        var inDiv = document.createElement('div');
                        inDiv.className = 'portlet ';
                        inDiv.style.width='520px';
                        inDiv.style.height='325px';
                        inDiv.id='hideDiv'+grpCnt;

                        var txtDiv = document.createElement('div');
                        txtDiv.className = 'portlet-header';
                        txtDiv.innerHTML = '<table id="addstd" width="100%" style="height:10px"><tr><td style="color:#369;font-weight:bold">KPI Region</td><td id="delPortlet" width="25px" align="right"><a href="#" class="ui-icon ui-icon-trash" onclick="closePortlet('+tdNum+','+rowCount+')"></a></td></tr></table>'
                        var cntDiv = document.createElement('div');
                        cntDiv.className = 'portlet-content';
                        cntDiv.id='dispGrp'+grpCnt;
                        cntDiv.style.height='270px';
                        cntDiv.style.width='97%';
                        cntDiv.style.overflow='auto';

                        cntDiv.innerHTML = '';
                        //                        inDiv.appendChild(txtDiv);
                        inDiv.appendChild(cntDiv);
                        cell1.appendChild(inDiv);
                        tdarray.push("portletTd_"+tdNum);
                        document.getElementById("hideDiv"+grpCnt).style.display='none'
                        showKpis('dispGrp'+grpCnt,KPIType,"hideDiv"+grpCnt)

                    }
                    else if(parseInt((tdNum%2))!=0)
                    {
                        var rowId = document.getElementById("rowPortlet_"+rowCount);

                        var cell2 = rowId.insertCell(1);
                        cell2.id="portletTd_"+tdNum;
                        var inDiv = document.createElement('div');
                        inDiv.className = 'portlet';
                        inDiv.style.width='520px';
                        inDiv.style.height='325px';
                        inDiv.id='hideDiv'+grpCnt;

                        var txtDiv = document.createElement('div');
                        txtDiv.className = 'portlet-header ';
                        txtDiv.innerHTML = '<table id="addstd" width="100%" style="height:10px"><tr><td style="color:#369;font-weight:bold">KPI Region</td><td id="delPortlet" width="25px" align="right"><a href="#" class="ui-icon ui-icon-trash" onclick="closePortlet('+tdNum+','+rowCount+')"></a></td></tr></table>'
                        var cntDiv = document.createElement('div');
                        cntDiv.className = 'portlet-content';
                        cntDiv.id='dispGrp'+grpCnt;
                        cntDiv.style.height='270px';
                        cntDiv.style.width='97%';
                        cntDiv.style.overflow='auto';

                        cntDiv.innerHTML = '';
                        //                        inDiv.appendChild(txtDiv);
                        inDiv.appendChild(cntDiv);
                        cell2.appendChild(inDiv);
                        tdarray.push("portletTd_"+tdNum);

                        document.getElementById("hideDiv"+grpCnt).style.display='none'
                        showKpis('dispGrp'+grpCnt,KPIType,"hideDiv"+grpCnt)
                    }

                    if(tdNum%2 ==0)
                    {
                        tdNum++;
                        tdCount++;
                    }
                    else
                    {
                        tdNum++;
                        rowCount++;
                    }

                    $(".column").sortable({
                        connectWith: '.column'
                    });
                    $(".portlet").addClass("ui-widget ui-widget-content ui-helper-clearfix ui-corner-all")
                    .find(".portlet-header")
                    .addClass("ui-widget-header ui-corner-all")
                    .end()
                    .find(".portlet-content");
                    $(".portlet-header .ui-icon").click(function() {
                        $(this).toggleClass();
                        $(this).parents(".portlet:first").find(".portlet-content").toggle();
                    });
                    $(".column").disableSelection();
                }
                function GroupKPI(dashBoardId,dashletId,kpiMasterId,folderdetails,kpiType){
                    var kpidashid= "Dashlets-"+dashletId;
                    var kpitype=kpiType;

                    var tdOBJ=document.getElementById(kpidashid);
                    if(tdOBJ==null)
                        tdOBJ=document.getElementById(dashletId);
                    var imgObjs = tdOBJ.getElementsByTagName("div");

                    document.getElementById("divId").value=imgObjs[1].id//


                    document.getElementById("folderId").value='<%=folderDetails%>'//
                    document.getElementById("reportId").value=<%=PbReportId%>
                    var frameObj=document.getElementById("kpidataDispmem");
                    var source="dashboardTemplateViewerAction.do?templateParam2=groupKpis&foldersIds=<%=folderDetails%>&divId="+kpidashid+"&kpiType="+kpitype+"&dashboardId="+dashBoardId+"&kpiMasterId="+kpiMasterId+"&dashletId="+dashletId;
                    frameObj.src=source;
                    $("#kpisDialog").dialog('option','title','KPI Grouping')
                    $("#kpisDialog").dialog('open');

                }

                function addGraph(){
                    kt=1;
                    grpCnt++;
                    var tableObj = document.getElementById("DasboardTable");
                    var table = document.getElementById("DasboardTable");
                    var rowCount1 = table.rows.length;
                    document.getElementById("GraphTable2").style.display = 'none'
                    document.getElementById("GraphTable1").style.display = 'none'

                    if(tdNum==-1){
                        tdNum=0;
                    }
                    if(parseInt(tdNum%2)==0)
                    {
                        var newRow = tableObj.insertRow(rowCount);
                        newRow.id = "rowPortlet_"+rowCount;
                        var cell = newRow.insertCell(0);
                        cell.id="portletTd_"+tdNum;
                        var inDivG = document.createElement('div');
                        inDivG.className = 'portlet';
                        inDivG.style.width='520px';
                        inDivG.style.height='325px';
                        var centr = document.createElement('center');


                        var txtDivG = document.createElement('div');
                        txtDivG.className = 'portlet-header ';
                        //txtDivG.innerHTML = '<table width="100%" style="height:10px"><tr><td style="color:#369;font-weight:bold">Graph Region</td><td align="right"> <a href="javascript:void(0)" onclick="showGraphs(dispGrp'+grpCnt+')">Add / Edit Graph</a>&nbsp;&nbsp;<a href="javascript:void(0)" onclick="showKpiGraphs(dispGrp'+grpCnt+')">Add KPI Graph</a>&nbsp;&nbsp;<a href="javascript:void(0)" onclick="createDbrdGraphs(dispGrp'+grpCnt+')">Create Graph</a></td><td id="delPortlet" width="25px" align="right"><a href="#"  class="ui-icon ui-icon-trash" onclick="closePortlet('+tdNum+','+kt+')"></a></td></tr></table>'
                        txtDivG.innerHTML = '<table width="100%" style="height:10px"><tr><td style="color:#369;font-weight:bold">Graph Region</td><td id="delPortlet" width="25px" align="right"><a href="#"  class="ui-icon ui-icon-trash" onclick="closePortlet('+tdNum+','+rowCount+')"></a></td></tr></table>'
                        var cntDivG = document.createElement('div');
                        cntDivG.className = 'portlet-content';
                        cntDivG.id='dispGrp'+grpCnt;
                        cntDivG.innerHTML = ''

                        inDivG.appendChild(txtDivG);
                        centr.appendChild(cntDivG);
                        inDivG.appendChild(centr);
                        cell.appendChild(inDivG);
                        newRow.appendChild(cell);
                        tdarray.push("portletTd_"+tdNum);

                    }
                    else if(parseInt((tdNum%2))!=0)
                    {

                        var rowId = document.getElementById("rowPortlet_"+rowCount);
                        var cell = rowId.insertCell(1);
                        cell.id="portletTd_"+tdNum;
                        var inDivG = document.createElement('div');
                        inDivG.className = 'portlet';
                        inDivG.style.width='520px';
                        inDivG.style.height='325px';
                        var txtDiv = document.createElement('div');
                        txtDiv.className = 'portlet-header';
                        //txtDiv.innerHTML = '<table width="100%" style="height:10px"><tr><td style="color:#369;font-weight:bold">Graph Region</td><td align="right"> <a href="javascript:void(0)" onclick="showGraphs(dispGrp'+grpCnt+')">Add / Edit Graph</a>&nbsp;&nbsp;<a href="javascript:void(0)" onclick="showKpiGraphs(dispGrp'+grpCnt+')">Add KPI Graph</a>&nbsp;&nbsp;<a href="javascript:void(0)" onclick="createDbrdGraphs(dispGrp'+grpCnt+')">Create Graph</a></td><td id="delPortlet" width="25px" align="right"><a href="#" class="ui-icon ui-icon-trash" onclick="closePortlet('+tdNum+','+kt+')"></a></td></tr></table>'
                        txtDiv.innerHTML = '<table width="100%" style="height:10px"><tr><td style="color:#369;font-weight:bold">Graph Region</td><td id="delPortlet" width="25px" align="right"><a href="#" class="ui-icon ui-icon-trash" onclick="closePortlet('+tdNum+','+rowCount+')"></a></td></tr></table>'
                        var cntDiv = document.createElement('div');
                        cntDiv.className = 'portlet-content';
                        cntDiv.id='dispGrp'+grpCnt;
                        cntDiv.innerHTML = ''
                        inDivG.appendChild(txtDiv);
                        inDivG.appendChild(cntDiv);
                        cell.appendChild(inDivG);
                        tdarray.push("portletTd_"+tdNum);
                    } if(tdNum%2 ==0)
                    {
                        tdNum++;
                        tdCount++;
                    }
                    else
                    {
                        tdNum++;
                        rowCount++;
                    }
                    $(".column").sortable({
                        connectWith: '.column',
                        disabled:true
                    });
                    $(".portlet").addClass("ui-widget ui-widget-content ui-helper-clearfix ui-corner-all")
                    .find(".portlet-header")
                    .addClass("ui-widget-header ui-corner-all")
                    .end()
                    .find(".portlet-content");
                    $(".portlet-header .ui-icon").click(function() {
                        $(this).toggleClass();
                        $(this).parents(".portlet:first").find(".portlet-content").toggle();
                    });
                    $(".column").disableSelection();
                }

                function closePortlet(x,y){
                    var confirmText= confirm("Are you sure you want to delete");
                    if(confirmText==true){

                        var tdOBJ=document.getElementById('portletTd_'+x);
                        var imgObjs = tdOBJ.getElementsByTagName("div")
                        NewDashIds=NewDashIds+imgObjs[2].id+",";
                        var parTrObject=tdOBJ.parentNode;
                        parTrObject.removeChild(tdOBJ);
                        if((tdNum-1)==x)
                        {
                            if(x%2 ==0 )
                            {
                                rowCount=y;
                                tdNum=x;

                            }
                            else
                            {
                                rowCount=y;
                                tdNum=x;

                            }
                        }  //added by k on 28-06-10
                        var temparray= new Array();
                        for (var i = 0; i < tdarray.length; i++){
                            if (tdarray[i] != 'portletTd_'+x )
                                temparray.push(tdarray[i])

                        }

                        tdarray=temparray;
                        alterDashBoardTable();
                    }
                    if(confirmText==false){
                        return null;
                    }
                }  function closeTargetKpi(ids,tdno){  //added by sathish

                    var confirmText= confirm("Are you sure you want to delete");
                    if(confirmText==true){
                        var divobj=document.getElementById('kpidivid_'+ids);

                        var imgObjs = divobj.getElementsByTagName("div")
                        NewDashIds=NewDashIds+imgObjs[1].id+",";
                        var temp1array= new Array();
                        for (var i = 0; i < tdarray.length; i++){
                            if (tdarray[i] != "portletTd_"+tdno )
                                temp1array.push(tdarray[i])
                        }
                        tdarray=temp1array;
                        var parobj=divobj.parentNode;

                        parobj.removeChild(divobj);
                        if((rowCount-1)==ids)
                        {
                            rowCount--;

                        }

                        alterDashBoardTable();
                    }
                }

                function closeOldPortlet(ids, dashletId){
                    var dashboardId = document.getElementById("dbrdId").value;
                    var confirmText= confirm("Are you sure you want to delete");
                    if(confirmText==true){
                        DashIds+=ids+","
                        // if(ids=='DashletsColumn1_2')

                        var divobj=document.getElementById(ids);
                        if(divobj==null)
                            divobj=document.getElementById(dashletId);
                        var parObj=divobj.parentNode;
                        parObj.removeChild(divobj);
                        var parentId = parObj.id;
                        if(document.getElementById(parentId).innerHTML==""){
                            document.getElementById(parentId).style.display='none';
                        }
                        $.ajax({ type: 'GET',
                            async: false,
                            cache: false,
                            timeout: 30000,
                            url: 'dashboardTemplateViewerAction.do?templateParam2=deleteDbrdGraphs&dashboardId='+dashboardId+'&dashletId='+dashletId,
                            success: function(data){
                                if (data != null && data != "")
                                    alert("Report Deleted");
                            }
                        });
                    }
                }

                function addRegion()
                {
                    $("#regionDialog").dialog('open');
                }
                function addKpiGraph(){

                    kt=1;
                    grpCnt++;
                    var tableObj = document.getElementById("DasboardTable");
                    var table = document.getElementById("DasboardTable");
                    document.getElementById("GraphTable2").style.display = 'none'
                    document.getElementById("GraphTable1").style.display = 'none'
                    var rowCount1 = table.rows.length;
                    if(tdNum==-1){
                        tdNum=0;
                    }
                    if(parseInt(tdNum%2)==0)
                    {
                        var newRow = tableObj.insertRow(rowCount);
                        newRow.id = "rowPortlet_"+rowCount;
                        var cell = newRow.insertCell(0);
                        cell.id="portletTd_"+tdNum;
                        var inDivG = document.createElement('div');
                        inDivG.className = 'portlet';
                        inDivG.style.width='520px';
                        inDivG.style.height='325px';
                        inDivG.id='hideDiv'+grpCnt;
                        var txtDivG = document.createElement('div');
                        txtDivG.className = 'portlet-header ';
                        //txtDivG.innerHTML = '<table width="100%" style="height:10px"><tr><td style="color:#369;font-weight:bold">Graph Region</td><td align="right"> <a href="javascript:void(0)" onclick="showGraphs(dispGrp'+grpCnt+')">Add / Edit Graph</a>&nbsp;&nbsp;<a href="javascript:void(0)" onclick="showKpiGraphs(dispGrp'+grpCnt+')">Add KPI Graph</a>&nbsp;&nbsp;<a href="javascript:void(0)" onclick="createDbrdGraphs(dispGrp'+grpCnt+')">Create Graph</a></td><td id="delPortlet" width="25px" align="right"><a href="#"  class="ui-icon ui-icon-trash" onclick="closePortlet('+tdNum+','+kt+')"></a></td></tr></table>'
                        txtDivG.innerHTML = '<table width="100%" style="height:10px"><tr><td style="color:#369;font-weight:bold">Graph Region</td><td id="delPortlet" width="25px" align="right"><a href="#"  class="ui-icon ui-icon-trash" onclick="closePortlet('+tdNum+','+rowCount+')"></a></td></tr></table>'
                        var cntDivG = document.createElement('div');
                        cntDivG.className = 'portlet-content';
                        cntDivG.id='dispGrp'+grpCnt;
                        cntDivG.innerHTML = ''
                        //                        inDivG.appendChild(txtDivG);
                        inDivG.appendChild(cntDivG);
                        cell.appendChild(inDivG);
                        newRow.appendChild(cell);
                        tdarray.push("portletTd_"+tdNum);

                        document.getElementById("hideDiv"+grpCnt).style.display='none'
                        showKpiGraphs("dispGrp"+grpCnt,"hideDiv"+grpCnt)
                    }
                    else if(parseInt((tdNum%2))!=0)
                    {var rowId = document.getElementById("rowPortlet_"+rowCount);
                        var cell = rowId.insertCell(1);
                        cell.id="portletTd_"+tdNum;
                        var inDivG = document.createElement('div');
                        inDivG.className = 'portlet';
                        inDivG.style.width='520px';
                        inDivG.style.height='325px';
                        inDivG.id='hideDiv'+grpCnt;
                        var txtDiv = document.createElement('div');
                        txtDiv.className = 'portlet-header';
                        //txtDiv.innerHTML = '<table width="100%" style="height:10px"><tr><td style="color:#369;font-weight:bold">Graph Region</td><td align="right"> <a href="javascript:void(0)" onclick="showGraphs(dispGrp'+grpCnt+')">Add / Edit Graph</a>&nbsp;&nbsp;<a href="javascript:void(0)" onclick="showKpiGraphs(dispGrp'+grpCnt+')">Add KPI Graph</a>&nbsp;&nbsp;<a href="javascript:void(0)" onclick="createDbrdGraphs(dispGrp'+grpCnt+')">Create Graph</a></td><td id="delPortlet" width="25px" align="right"><a href="#" class="ui-icon ui-icon-trash" onclick="closePortlet('+tdNum+','+kt+')"></a></td></tr></table>'
                        txtDiv.innerHTML = '<table width="100%" style="height:10px"><tr><td style="color:#369;font-weight:bold">Graph Region</td><td id="delPortlet" width="25px" align="right"><a href="#" class="ui-icon ui-icon-trash" onclick="closePortlet('+tdNum+','+rowCount+')"></a></td></tr></table>'
                        var cntDiv = document.createElement('div');
                        cntDiv.className = 'portlet-content';
                        cntDiv.id='dispGrp'+grpCnt;
                        cntDiv.innerHTML = ''
                        //                        inDivG.appendChild(txtDiv);
                        inDivG.appendChild(cntDiv);
                        cell.appendChild(inDivG);
                        tdarray.push("portletTd_"+tdNum);

                        document.getElementById("hideDiv"+grpCnt).style.display='none'
                        showKpiGraphs("dispGrp"+grpCnt,"hideDiv"+grpCnt)}if(tdNum%2 ==0)
                    {
                        tdNum++;
                        tdCount++;
                    }
                    else
                    {
                        tdNum++;
                        rowCount++;
                    }
                    $(".column").sortable({
                        connectWith: '.column'
                    });
                    $(".portlet").addClass("ui-widget ui-widget-content ui-helper-clearfix ui-corner-all")
                    .find(".portlet-header")
                    .addClass("ui-widget-header ui-corner-all")
                    .end()
                    .find(".portlet-content");
                    $(".portlet-header .ui-icon").click(function() {
                        $(this).toggleClass();
                        $(this).parents(".portlet:first").find(".portlet-content").toggle();
                    });
                    $(".column").disableSelection();
                }
                function addDbrdGraph(){
                    kt=1;
                    grpCnt++;
                    // rowCount=0;
                    var tableObj = document.getElementById("DasboardTable");
                    var table = document.getElementById("DasboardTable");
                    document.getElementById("GraphTable2").style.display = 'none'
                    document.getElementById("GraphTable1").style.display = 'none'

                    var rowCount1 = table.rows.length;
                    if(tdNum==-1){
                        tdNum=0;
                    }
                    if(parseInt(tdNum%2)==0)
                    {
                        var newRow = tableObj.insertRow(rowCount);
                        newRow.id = "rowPortlet_"+rowCount;
                        var cell = newRow.insertCell(0);
                        cell.id="portletTd_"+tdNum;
                        var inDivG = document.createElement('div');
                        inDivG.className = 'portlet';
                        inDivG.style.width='520px';
                        inDivG.style.height='325px';
                        inDivG.id='hideDiv'+grpCnt;
                        var txtDivG = document.createElement('div');
                        txtDivG.className = 'portlet-header ';
                        //txtDivG.innerHTML = '<table width="100%" style="height:10px"><tr><td style="color:#369;font-weight:bold">Graph Region</td><td align="right"> <a href="javascript:void(0)" onclick="showGraphs(dispGrp'+grpCnt+')">Add / Edit Graph</a>&nbsp;&nbsp;<a href="javascript:void(0)" onclick="showKpiGraphs(dispGrp'+grpCnt+')">Add KPI Graph</a>&nbsp;&nbsp;<a href="javascript:void(0)" onclick="createDbrdGraphs(dispGrp'+grpCnt+')">Create Graph</a></td><td id="delPortlet" width="25px" align="right"><a href="#"  class="ui-icon ui-icon-trash" onclick="closePortlet('+tdNum+','+kt+')"></a></td></tr></table>'
                        txtDivG.innerHTML = '<table width="100%" style="height:10px"><tr><td id="innertd'+grpCnt+'" style="color:#369;font-weight:bold">Graph Region</td><td id="properties" style="cursor:pointer" onclick=showDesignGraphProperties("dispGrp'+grpCnt+'","hideDiv'+grpCnt+'") align="right">Properties</td><td id="delPortlet" width="25px" align="right"><a href="#"  class="ui-icon ui-icon-trash" onclick="closePortlet('+tdNum+','+rowCount+')"></a></td></tr></table>'
                        var cntDivG = document.createElement('div');
                        cntDivG.className = 'portlet-content';
                        cntDivG.id='dispGrp'+grpCnt;
                        cntDivG.innerHTML = ''
                        inDivG.appendChild(txtDivG);
                        inDivG.appendChild(cntDivG);
                        cell.appendChild(inDivG);
                        newRow.appendChild(cell);
                        tdarray.push("portletTd_"+tdNum);

                        document.getElementById("hideDiv"+grpCnt).style.display='none'
                        createDbrdGraphs("dispGrp"+grpCnt,"hideDiv"+grpCnt)
                    }
                    else if(parseInt((tdNum%2))!=0)
                    {
                        var rowId = document.getElementById("rowPortlet_"+rowCount);
                        var cell = rowId.insertCell(1);
                        cell.id="portletTd_"+tdNum;
                        var inDivG = document.createElement('div');
                        inDivG.className = 'portlet';
                        inDivG.style.width='520px';
                        inDivG.style.height='325px';
                        inDivG.id='hideDiv'+grpCnt;
                        var txtDiv = document.createElement('div');
                        txtDiv.className = 'portlet-header';
                        txtDiv.innerHTML = '<table width="100%" style="height:10px"><tr><td  id="innertd'+grpCnt+'" style="color:#369;font-weight:bold">Graph Region</td><td id="properties" style="cursor:pointer" onclick=showDesignGraphProperties("dispGrp'+grpCnt+'","hideDiv'+grpCnt+'") align="right">Properties</td><td id="delPortlet" width="25px" align="right"><a href="#" class="ui-icon ui-icon-trash" onclick="closePortlet('+tdNum+','+rowCount+')"></a></td></tr></table>'
                        var cntDiv = document.createElement('div');
                        cntDiv.className = 'portlet-content';
                        cntDiv.id='dispGrp'+grpCnt;
                        cntDiv.innerHTML = ''
                        inDivG.appendChild(txtDiv);
                        inDivG.appendChild(cntDiv);
                        cell.appendChild(inDivG);
                        tdarray.push("portletTd_"+tdNum);


                        document.getElementById("hideDiv"+grpCnt).style.display='none'
                        createDbrdGraphs("dispGrp"+grpCnt,"hideDiv"+grpCnt)
                    }
  if(tdNum%2 ==0)
                    {
                        tdNum++;
                        tdCount++;
                    }
                    else
                    {
                        tdNum++;
                        rowCount++;
                    }

                    $(".column").sortable({
                        connectWith: '.column',
                        disabled:true
                    });
                    $(".portlet").addClass("ui-widget ui-widget-content ui-helper-clearfix ui-corner-all")
                    .find(".portlet-header")
                    .addClass("ui-widget-header ui-corner-all")
                    .end()
                    .find(".portlet-content");
                    $(".portlet-header .ui-icon").click(function() {
                        $(this).toggleClass();
                        $(this).parents(".portlet:first").find(".portlet-content").toggle();
                    });
                    $(".column").disableSelection();
                } function addReportGraph(){
                    kt=1;
                    grpCnt++;
                    var tableObj = document.getElementById("DasboardTable");
                    var table = document.getElementById("DasboardTable");
                    var rowCount1 = table.rows.length;
                    document.getElementById("GraphTable2").style.display = 'none'
                    document.getElementById("GraphTable1").style.display = 'none'
                    if(tdNum==-1){
                        tdNum=0;
                    }
                    if(parseInt(tdNum%2)==0)
                    {
                        var newRow = tableObj.insertRow(rowCount);
                        newRow.id = "rowPortlet_"+rowCount;
                        var cell = newRow.insertCell(0);
                        cell.id="portletTd_"+tdNum;
                        var inDivG = document.createElement('div');
                        inDivG.className = 'portlet';
                        inDivG.style.width='520px';
                        inDivG.style.height='325px';
                        inDivG.id='hideDiv'+grpCnt;
                        var txtDivG = document.createElement('div');
                        txtDivG.className = 'portlet-header ';
                        // txtDivG.innerHTML = '<table width="100%" style="height:10px"><tr><td style="color:#369;font-weight:bold">Graph Region</td><td align="right"> <a href="javascript:void(0)" onclick="showGraphs(dispGrp'+grpCnt+')">Add / Edit Graph</a>&nbsp;&nbsp;<a href="javascript:void(0)" onclick="showKpiGraphs(dispGrp'+grpCnt+')">Add KPI Graph</a>&nbsp;&nbsp;<a href="javascript:void(0)" onclick="createDbrdGraphs(dispGrp'+grpCnt+')">Create Graph</a></td><td id="delPortlet" width="25px" align="right"><a href="#"  class="ui-icon ui-icon-trash" onclick="closePortlet('+tdNum+','+kt+')"></a></td></tr></table>'
                        txtDivG.innerHTML = '<table width="100%" style="height:10px"><tr><td style="color:#369;font-weight:bold">Graph Region</td><td id="delPortlet" width="25px" align="right"><a href="#"  class="ui-icon ui-icon-trash" onclick="closePortlet('+tdNum+','+rowCount+')"></a></td></tr></table>'
                        var cntDivG = document.createElement('div');
                        cntDivG.className = 'portlet-content';
                        cntDivG.id='dispGrp'+grpCnt;
                        cntDivG.innerHTML = ''
                        inDivG.appendChild(txtDivG);
                        inDivG.appendChild(cntDivG);
                        cell.appendChild(inDivG);
                        newRow.appendChild(cell);
                        tdarray.push("portletTd_"+tdNum);

                        document.getElementById("hideDiv"+grpCnt).style.display='none'
                        showGraphs("dispGrp"+grpCnt,"hideDiv"+grpCnt)
                    }
                    else if(parseInt((tdNum%2))!=0)
                    { var rowId = document.getElementById("rowPortlet_"+rowCount);
                        var cell = rowId.insertCell(1);
                        cell.id="portletTd_"+tdNum;
                        var inDivG = document.createElement('div');
                        inDivG.className = 'portlet';
                        inDivG.style.width='520px';
                        inDivG.style.height='325px';
                        inDivG.id='hideDiv'+grpCnt;
                        var txtDiv = document.createElement('div');
                        txtDiv.className = 'portlet-header';
                        //txtDiv.innerHTML = '<table width="100%" style="height:10px"><tr><td style="color:#369;font-weight:bold">Graph Region</td><td align="right"> <a href="javascript:void(0)" onclick="showGraphs(dispGrp'+grpCnt+')">Add / Edit Graph</a>&nbsp;&nbsp;<a href="javascript:void(0)" onclick="showKpiGraphs(dispGrp'+grpCnt+')">Add KPI Graph</a>&nbsp;&nbsp;<a href="javascript:void(0)" onclick="createDbrdGraphs(dispGrp'+grpCnt+')">Create Graph</a></td><td id="delPortlet" width="25px" align="right"><a href="#" class="ui-icon ui-icon-trash" onclick="closePortlet('+tdNum+','+kt+')"></a></td></tr></table>'
                        txtDiv.innerHTML = '<table width="100%" style="height:10px"><tr><td style="color:#369;font-weight:bold">Graph Region</td><td id="delPortlet" width="25px" align="right"><a href="#" class="ui-icon ui-icon-trash" onclick="closePortlet('+tdNum+','+rowCount+')"></a></td></tr></table>'
                        var cntDiv = document.createElement('div');
                        cntDiv.className = 'portlet-content';
                        cntDiv.id='dispGrp'+grpCnt;
                        cntDiv.innerHTML = ''
                        inDivG.appendChild(txtDiv);
                        inDivG.appendChild(cntDiv);
                        cell.appendChild(inDivG);
                        tdarray.push("portletTd_"+tdNum);

                        document.getElementById("hideDiv"+grpCnt).style.display='none'
                        showGraphs("dispGrp"+grpCnt,"hideDiv"+grpCnt)
                    }


                    if(tdNum%2 ==0)
                    {
                        tdNum++;
                        tdCount++;
                    }
                    else
                    {
                        tdNum++;
                        rowCount++;
                    }
                    $(".column").sortable({
                        connectWith: '.column'
                    });
                    $(".portlet").addClass("ui-widget ui-widget-content ui-helper-clearfix ui-corner-all")
                    .find(".portlet-header")
                    .addClass("ui-widget-header ui-corner-all")
                    .end()
                    .find(".portlet-content");
                    $(".portlet-header .ui-icon").click(function() {
                        $(this).toggleClass();
                        $(this).parents(".portlet:first").find(".portlet-content").toggle();
                    });
                    $(".column").disableSelection();
                }
                function addDimEmailRow(){
                    var table=document.getElementById("emailDiv")
                    var rowCount = table.rows.length;
                    idx = rowCount ;
                    var row = table.insertRow(rowCount);
                    row.id="row"+idx;
                    var tdhtml="<td   class='myhead' width='13%'>";
                    tdhtml+="<%=TranslaterHelper.getTranslatedString("MAIL_TO", locale)%> ";
                    tdhtml+="</td><td width='10p%'><input type='text' id=\""+idx+"mail\" name='mail' style='width: 225px'> </td> <td width='1%'>";
                    tdhtml+="<img  align='middle' SRC='<%=request.getContextPath()%>/icons pinvoke/plus-circle.png' BORDER='0' ALT=''  onclick='addDimEmailRow()' title='Add Row' /></td>";
                    tdhtml+="<td width='2px'><img  align='middle' title='Delete Row' src='<%=request.getContextPath()%>/icons pinvoke/cross-circle.png' BORDER='0' ALT=''  onclick=\" deleteDimEmailRow('"+row.id+"')\"  /></td>";
                    row.innerHTML=tdhtml;
                }
                function deleteDimEmailRow(rowId)
                {
                    var rowId=rowId.substr(3);
                    try {
                        var table = document.getElementById("emailDiv");
                        var rowCount = table.rows.length;
                        if(rowCount > 3) {
                            table.deleteRow(rowId);
                            idx--;
                        }
                        else{
                            alert("You cannot delete all the rows");
                        }
                    }catch(e) {
                        //                alert(e);
                    }
                } function alterDashBoardTable()
                {var table = document.getElementById("DasboardTable");
                    var newtable = document.getElementById("tempDasboardTable");
                    var rowCount1 = table.rows.length;
                    var count=0;
                    var newRow="";
                    var cell="";
                    var rcount=-1;
            <%--alert("size->"+tdarray.length)--%>
                    for(var i=0;i< tdarray.length;i++)
                    {
                        var a= document.getElementById(tdarray[i]).innerHTML;
                        var imgObjs = document.getElementById(tdarray[i]).getElementsByTagName("div")
                        var width1= imgObjs[0].style.width;
            <%--alert("width1="+width1)--%>
                        if(width1=="520px")
                        {

                            if(count%2==0)
                            {
            <%--alert("newrow4 500"+count)--%>
                                rcount++;
                                newRow = newtable.insertRow(rcount);
                                newRow.id = "rowPortlet_"+rcount;
                                cell = newRow.insertCell(0);
                                cell.id=tdarray[i];
                                cell.innerHTML= document.getElementById(tdarray[i]).innerHTML;
                                cell.style.width='auto';
                                cell.style.paddingLeft='30px';
                                cell.setAttribute("colspan", "1");
                                newRow.appendChild(cell);
                                count++;
            <%--alert("count="+count)--%>
                            }
                            else
                            {
            <%--alert("append to  500"+count)--%>
                                cell = newRow.insertCell(1);
                                cell.id=tdarray[i];
                                cell.innerHTML= document.getElementById(tdarray[i]).innerHTML;
                                cell.style.width='auto';
                                cell.style.paddingLeft='30px';
                                cell.setAttribute("colspan","1");
                                newRow.appendChild(cell);
                                count++;} }
                        else
                        { rcount++;
                            newRow = newtable.insertRow(rcount);
                            newRow.id = "rowPortlet_"+rcount;
                            cell = newRow.insertCell(0);
                            cell.id=tdarray[i];
                            cell.setAttribute("colspan", "2");
                            cell.style.paddingLeft='30px';
                            cell.innerHTML= document.getElementById(tdarray[i]).innerHTML;
                            newRow.appendChild(cell);
                            if(count%2==0)
                                count=count+2;
                            else
                                count=count+3; } } document.getElementById("DasboardTable").innerHTML=document.getElementById("tempDasboardTable").innerHTML;
                        document.getElementById("tempDasboardTable").innerHTML='';
                        if((count)%2==0)
                        { if(tdNum%2 !=0)
                            {
                                tdNum=tdNum+1;}
                        }
                        else
                        {if(tdNum%2 ==0){
                                tdNum=tdNum+1;
                            }
                        }if(count%2==0)
                            rcount++;
                        rowCount=rcount; }
                    function gotoDBCON1(ctxPath,repid){
                        document.forms.frmParameter.action=ctxPath+"/reportViewer.do?reportBy=viewReport&REPORTID="+repid+"&action=open";
                        document.forms.frmParameter.submit(); }
                    function gotoDBCON2(ctxPath,repid){
                        document.forms.frmParameter.action=ctxPath+"/dashboardViewer.do?reportBy=viewDashboard&REPORTID="+repid;
                        document.forms.frmParameter.submit();}
                    function addAllKpis(dashBoardId,dashletId,kpiMasterId,idsStr,namesStr,kpityp){
                        var kpidashid= "Dashlets-"+dashletId;
                        var tdOBJ=document.getElementById(kpidashid);
                        if(tdOBJ==null)
                            tdOBJ=document.getElementById(dashletId);
                        var imgObjs = tdOBJ.getElementsByTagName("div")
                        document.getElementById("divId").value=imgObjs[1].id
                        document.getElementById("folderId").value='<%=folderDetails%>'//
                        document.getElementById("reportId").value='<%=PbReportId%>'
                        var frameObj=document.getElementById("kpiReOrder");
                        var source="dashboardTemplateViewerAction.do?templateParam2=getAllKpis&foldersIds=<%=folderDetails%>&divId="+kpidashid+"&kpiType="+kpityp+"&dashboardId="+dashBoardId+"&kpiMasterId="+kpiMasterId+"&dashletId="+dashletId+"&idsStr="+encodeURIComponent(idsStr)+"&namesStr="+encodeURIComponent(namesStr);
                        frameObj.src=source;

                        $("#ReorderkpisDialog").dialog('option','title','ReOrder Kpis Sequence')
                        $("#ReorderkpisDialog").dialog('open');

                    }
                    function exportToExcel(dashBoardId,dashletId,kpiMasterId,idsStr,namesStr)
                    {
                        var htmltype="kpidownload";
                        var REPORTID=document.getElementById(dashletId);
                        var ctxPath = '<%= request.getContextPath()%>';

                        $.ajax({
                            type:'GET',
                            async:false,
                            cache:false,
                            timeout:30000,
                            url: ctxPath+'/dashboardViewer.do?reportBy=excelDownload&REPORTID='+dashletId +"&dashboardid="+dashBoardId,
                            success: function(data){
                                var source = '<%=request.getContextPath()%>/TableDisplay/pbDownload.jsp?dType='+htmltype;
                                var frameObj=document.getElementById("exportKPi");
                                frameObj.src=source;
                            }
                        });

                    }

                    function selectall()
                    {
                        var SelectObj=document.getElementsByName("checkformatselect");
                        for(var i=0;i<SelectObj.length;i++){
                            if(SelectObj[0].checked){
                                SelectObj[i].checked=true;

                            }
                            else{
                                SelectObj[i].checked=false;
                            }

                        }
                    }

                    $(function() {
            <% if (timeinfo.get(1).equalsIgnoreCase("PRG_DATE_RANGE") && sytm.equalsIgnoreCase("No")) {%>
                        $( "#fromdate" ).datepicker({
                            showOn: "button",
                            buttonImage: "images/calendar_18x16.gif",
                            buttonImageOnly: true,
                            showButtonPanel: true,
                            changeMonth: true,
                            changeYear: true,
                            showButtonPanel: true,
                            numberOfMonths: 1,
                            stepMonths: 1,
                            dateFormat: "D,d,M,yy",
                            onClose: function showdate(){
                                var a;
                                a=($("#fromdate").val());
                                var dateArr=new Array()
                                dateArr=a.split(",");
                                if(a!=""){
                                    $("#field1").html(dateArr[2]+"'"+dateArr[3].substring(2))
                                    $("#field2").html(dateArr[1])
                                    $("#field3").html(dateArr[0])
                                }
                            }

                        }).datepicker("setDate", new Date(('<%=vals1[2]%>')) );
                        $( "#todate" ).datepicker({
                            showOn: "button",
                            buttonImage: "images/calendar_18x16.gif",
                            buttonImageOnly: true,
                            showButtonPanel: true,
                            changeMonth: true,
                            changeYear: true,
                            showButtonPanel: true,
                            numberOfMonths: 1,
                            stepMonths: 1,
                            dateFormat: "D,d,M,yy",
                            onClose: function showdate(){
                                var a;
                                a=($("#todate").val());
                                var dateArr=new Array()
                                dateArr=a.split(",");
                                if(a!=""){
                                    $("#tdfield1").html(dateArr[2]+"'"+dateArr[3].substring(2))
                                    $("#tdfield2").html(dateArr[1])
                                    $("#tdfield3").html(dateArr[0])
                                }
                            }

                        }).datepicker("setDate", new Date(('<%=vals1[3]%>')) );
                        $( "#comparefrom" ).datepicker({
                            showOn: "button",
                            buttonImage: "images/calendar_18x16.gif",
                            buttonImageOnly: true,
                            showButtonPanel: true,
                            dateFormat: "D,d,M,yy",
                            changeYear: true,
                            changeMonth: true,
                            showButtonPanel: true,
                            numberOfMonths: 1,
                            stepMonths: 1,
                            onClose: function showdate(){
                                var a;
                                a=($("#comparefrom").val());
                                var dateArr=new Array()
                                dateArr=a.split(",");
                                if(a!=""){
                                    $("#cffield1").html(dateArr[2]+"'"+dateArr[3].substring(2))
                                    $("#cffield2").html(dateArr[1])
                                    $("#cffield3").html(dateArr[0])
                                }
                            }

                        }).datepicker("setDate", new Date(('<%=vals1[4]%>')) );
                        $( "#compareto" ).datepicker({
                            showOn: "button",
                            buttonImage: "images/calendar_18x16.gif",
                            buttonImageOnly: true,
                            showButtonPanel: true,
                            changeMonth: true,
                            changeYear: true,
                            showButtonPanel: true,
                            numberOfMonths: 1,
                            stepMonths: 1,
                            dateFormat: "D,d,M,yy",
                            onClose: function showdate(){
                                var a;
                                a=($("#compareto").val());

                                var dateArr=new Array()
                                dateArr=a.split(",");
                                if(a!=""){
                                    $("#ctfield1").html(dateArr[2]+"'"+dateArr[3].substring(2))
                                    $("#ctfield2").html(dateArr[1])
                                    $("#ctfield3").html(dateArr[0])
                                }
                            }

                        }).datepicker("setDate", new Date(('<%=vals1[5]%>')) );
            <%} else if (timeinfo.get(1).equalsIgnoreCase("PRG_STD") && sytm.equalsIgnoreCase("No")) {%>

                        $( "#perioddate" ).datepicker({
                            showOn: "button",
                            buttonImage: "images/calendar_18x16.gif",
                            buttonImageOnly: true,
                            showButtonPanel: true,
                            changeMonth: true,
                            changeYear: true,
                            showButtonPanel: true,
                            numberOfMonths: 1,
                            stepMonths: 1,
                            dateFormat: "D,d,M,yy",

                            onClose: function showdate(){
                                var a;
                                a=($("#perioddate").val());
                                var dateArr=new Array()
                                dateArr=a.split(",");
                                if(a!=""){
                                    $("#pfield1").html(dateArr[2]+"'"+dateArr[3].substring(2))
                                    $("#pfield2").html(dateArr[1])
                                    $("#pfield3").html(dateArr[0])
                                }
                            }


                        }).datepicker("setDate", new Date(('<%=vals1[2]%>')) );
            <%}%>
                    });

                    function autohide(){
                        $('#center250a').hide();
                        $('#center250b').hide();
                        $('#autoshow').show();
                    }
                    function autoshow(){
                        $('#center250a').show();
                        $('#center250b').show();
                        $('#autoshow').hide();
                    }
                    function dateclick()
                    {
                        $('#datetext').val('topdate');
                        var  perioddate=$('#perioddate').val();
                        var fromdate=$('#fromdate').val();
                        var todate=$('#todate').val();
                        var comparefrom=$('#comparefrom').val();
                        var compareto=$('#compareto').val();
                        $.ajax({ type: 'GET',
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

            <% if (timeinfo.get(1).equalsIgnoreCase("PRG_DATE_RANGE")) {%>
                                $('#datepicker1').val(fromdate);
                                $('#datepicker2').val(todate);
                                $('#datepicker3').val(comparefrom);
                                $('#datepicker4').val(compareto);
            <%} else {%>
                                $('#datepicker').val(perioddate);
            <%}%>
                            }
                        });

                    }
        </script>
        <script type="text/javascript">
                $(function() {
                    $(".column").sortable({
                        connectWith: '.column',
                        disabled:true

                    });

                    $(".portlet").addClass("ui-widget ui-widget-content ui-helper-clearfix ui-corner-all")
                    .find(".portlet-header")
                    .addClass("ui-widget-header ui-corner-all")
                    .end()
                    .find(".portlet-content");

                    $(".portlet-header .ui-icon").click(function() {
                        $(this).toggleClass("ui-icon-minusthick");
                        $(this).parents(".portlet:first").find(".portlet-content").toggle();
                    });

                    $(".column").disableSelection();
                });
                function saveReportAssignment(elemNameList,groupId,dbrdId,dashletId)
                {
                    $.post("GroupMeassureCreation.do?templateParam=saveReportAssignment&groupId="+groupId+"&dbrdId="+dbrdId+"&dashletId="+dashletId+"&elemNameList="+elemNameList, $("#drilltoReportForm").serialize() ,
                    function(data){
                        $("#DrillToReportforGroups").dialog('close');
                        alert(data);


                    });

                }
                function saveAssignmentForKpi(dbrdId,dashletId)
                {
                    var divIdObj=document.getElementById("Dashlets-"+dashletId+"-kpi");
                    divIdObj.innerHTML='<center><img id="imgId" src="images/ajax.gif" align="middle"  width="75px" height="75px"  style="position:absolute" ></center>';
                    $.post("dashboardViewer.do?reportBy=saveReportForKpiWithGraph&dashboardId="+dbrdId+"&dashletId="+dashletId, $("#drilltoReportForm").serialize() ,
                    function(data){
                        $("#DrillToReportDialoge").dialog('close');
                        divIdObj.innerHTML=data;
                    });

                }
        </script>

        <script type="text/javascript">
                var count=0;
                function shwDate(){

                    $('#datepicker').datepicker({
                        changeMonth: true,
                        changeYear: true,
                        showButtonPanel: true,
                        numberOfMonths: 1,
                        stepMonths: 1
                    });
                    displayDbrdTimeInfo('<%=PbReportId%>');
                    document.getElementById("leftTd").style.display = "none";
                    var frameObj = document.getElementById("widgetframe");
                    var source = 'divPersistent.jsp?method=forleftTdDash&block=yes';
                    frameObj.src = source;
                    document.getElementById("tdImage").src = "<%=request.getContextPath()%>/icons pinvoke/control.png";

                    document.getElementById("tabParameters").style.display = "none";
                    var frameObj = document.getElementById("widgetframe");
                    var source = 'divPersistent.jsp?method=forDashParameters&block=no';
                    frameObj.src = source;
  }

        </script>
        <script type="text/javascript">
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
                } function showDesignGraphProperties(divId,hideDivId)
                {
                    document.getElementById('dbrdgraph1').value=divId;
                    document.getElementById('hidedbrdgraph1').value=hideDivId;
                    var source="<%=request.getContextPath()%>/PbGraphDetails.jsp?REPORTID=<%=PbReportId%>&graphId="+divId+"&from=dashboard";
                    var frameObj=document.getElementById("graphPropertiesFrame")
                    frameObj.src=source;
                    $("#graphPropertiesDiv").dialog("open")
                }
                function kpiDrillToReport(elementId,elementname)
                {
                    //                alert(elementId)
                    drillElmntId = elementId;
                    drillElmntName = elementname;
                    $("#KpiElementname").val(drillElmntName);
                    $.post("<%=request.getContextPath()%>/dashboardTemplateViewerAction.do?templateParam2=GetReportNames&elmntId="+drillElmntId+"&foldersIds="+<%=folderDetails%>+"&elementname="+elementname,
                    function(data){
                        if(data != ""){
                            var htmlVal=data.split("~")
                            $("#getDrillReports").html(htmlVal[0]);
                            selectedelementId=htmlVal[1];
                            //                                alert("drillReportId\t"+drillReportId)
                        }

                    });
                    $("#drillToRep").dialog('option','title','Drill To Report')
                    $("#drillToRep").dialog('open');


                }
function sortByNumber(dashletId,rowViewByMeasures,flag)
                {
                    $("#DashletOptions-"+dashletId).hide();
                    var measures=new Array()
                    var measureNames=new Array()
                    measures=rowViewByMeasures.split("&")
                    var td=""
                    td+="<select id='SortMeasure"+dashletId+"' title='select measure'>"
                    var measureId=document.getElementById("SortMeasure"+dashletId);
                    for(var i=0;i<measures.length;i++){
                        measureNames=measures[i].split(",")
                        if(measureNames[0]!='null'){
                            td=td+"<option value='A_"+measureNames[0]+"'>"+measureNames[1]+"</option>"
                        }
                    }
                    td=td+"</select>"
                    var td1 = ""
                    td1+="<select id='SortingOrder"+dashletId+"' title='sorting Order'>"
                    td1+= "<option value='0'>Ascending</option><option value='1'>Descending</option>"
                    td1=td1+"</select>"
                    dashletIdforSort = dashletId
                    flagforsort = flag

                    $("#SortMeasure").html(td);
                    $("#SortOrder").html(td1);
                    $("#sort").dialog('open')
                }

                function goButtonforsort()
                {
                    $("#sort").dialog('close')
                    goButtonForSort(dashletIdforSort,flagforsort)
                }
                function applyColor(dashletId,rowViewByMeasures,flag,dashboadrId)
                {
                    dashletIdforSort = dashletId
                    flagforsort = flag
                    dashboardIdforcolors = dashboadrId

                    var measures=new Array()
                    var measureNames=new Array()
                    measures=rowViewByMeasures.split("&")
                    var td=""
                    td+="<select id='measurefortable"+dashletId+"' title='select measure'>"
                    for(var i=0;i<measures.length;i++){
                        measureNames=measures[i].split(",")
                        if(measureNames[0]!='null'){
                            td=td+"<option value='A_"+measureNames[0]+"'>"+measureNames[1]+"</option>"
                        }
                    }
                    td=td+"</select>"
                    $("#SelectMeasureforColor").html(td)
                    $("#ColorDiv").dialog('open')
                }
                function saveappliedColors()
                {
                    $("#ColorDiv").dialog('close')
                    applydbrdcolor('measurefortable'+dashletIdforSort,dashboardIdforcolors ,dashletIdforSort,flagforsort)

                }
                function RenameKpiHeading(dashletId,dashboardId,kpiMasterId,kpiheads)
                {
                    kpiGlMasterId = kpiMasterId
                    dashId = dashletId
                    repIdSch=dashboardId
                    var kpiheadsArray = new Array()
                    kpiheadsArray = kpiheads.split(",")
                    idx = kpiheadsArray.length;
                    var td='';
                    for(var i=0;i<kpiheadsArray.length;i++){
                        td+="<tr><td>"
                        td+="<input type='text'readonly value='"+kpiheadsArray[i]+"'>"
                        td+="</td><td>"
                        td+="<input type='text' id='kpiheads"+i+"' value='"+kpiheadsArray[i]+"'></td>"
                        td+="</tr>"
                    }
                    $("#Kpiheadsbody").html(td)
                    $("#KpiHeadsRename").dialog('open')
                }
                var targetkpityp = '';
                var targetreportId = '';
                var targetkpimasterid = '';
                var targetdashletId = '';
                var targetelementId = '';
                var targettargetelem = '';
                var targettargetElemname = '';
                function getTargetMapElements(elementId,roleid,reportId,dashletId,kpimasterid,targetelem,targetElemname)
                {
                    var kpityp=document.getElementById("kpiType");
                    targetkpityp = kpityp;
                    targetreportId = reportId;
                    targetkpimasterid = kpimasterid;
                    targetdashletId = dashletId;
                    targetelementId = elementId;
                    targettargetelem = targetelem;
                    targettargetElemname = targetElemname;
                    document.getElementById('tableListtarget').checked=false;
                    $("#tabListDivtarget").show();
                    $("#tablistLinktarget").show();
                    $("#goButtontarget").show();
                    var frameObj=document.getElementById("addTargetkpidataDispmem");
                    var source="dashboardTemplateViewerAction.do?templateParam2=getMoreKpisForTarget&foldersIds=<%=folderDetails%>&kpiType="+kpityp+"&dashboardId="+reportId+"&kpiMasterId="+kpimasterid+"&dashletId="+dashletId+"&elementId="+elementId+"&targetelem="+targetelem+"&targetElemName="+targetElemname;
                    frameObj.src=source;
                    $("#addTargetkpisDialog").dialog('option','title','Target Mapping For KPI')
                    $("#addTargetkpisDialog").dialog('open');
                }
                function cancelRepTargetKpi()
                {
                    $("#addTargetkpisDialog").dialog('close');
                }
                function dashBoardPdf(dashboardId)
                {
                    var source ="<%=request.getContextPath()%>/pbDashBoardDownLoad.jsp?dashboardId="+dashboardId;
                    var dSrc = document.getElementById("dFrame");
                    dSrc.src = source;
                }
                function initCollapser(divId){
                    if (divId == ""){
                        $(".tablesorter")
                        .collapsible("td.collapsible", {
                            collapse: true
                        })
                    }
                    else{
                        $("#"+divId+" > .tablesorter")
                        .collapsible("td.collapsible", {
                            collapse: true
                        });
                    }
                }
                function KpiGraphToTable(dashletId,dashBoardId,refReportId,graphId,kpiMasterId,dispSequence,dispType,dashletName,graphOrTable,editDbrd,flag,fromDesigner){
                    var divIdObj=document.getElementById("Dashlets-"+dashletId+"-graph");
                    divIdObj.innerHTML='<center><img id="imgId" src="images/ajax.gif" align="middle"  width="75px" height="75px"  style="position:absolute" ></center>';
                    var dispUrl = 'dashboardViewer.do?reportBy=displayGraph&dashletId='+dashletId+'&dashBoardId='+dashBoardId+'&refReportId='+refReportId+'&graphId='+graphId+'&kpiMasterId='+kpiMasterId+'&dispSequence='+dispSequence+'&dispType='+dispType+'&dashletName='+dashletName+'&editDbrd='+editDbrd+'&fromDesigner='+fromDesigner+'&flag='+flag;

                    if (graphOrTable){
                        if (graphOrTable != "" && graphOrTable != "undefined")
                            dispUrl = dispUrl + '&graphOrTable=' + graphOrTable;
                    }
                    $.ajax({
                        type:'GET',
                        async:false,
                        cache:false,
                        timeout:30000,
                        url: dispUrl,
                        success: function(data){
                            divIdObj.innerHTML = data;
                        }
                    });
                }
                function checkFrequency(id){
                    if($("#"+id).val()=="Weekly"){
                        $("#weekday").show();
                        $("#monthday").hide();
                        $("#hourlyId").hide();
                    }else if($("#"+id).val()=="Monthly"){
                        $("#weekday").hide();
                        $("#hourlyId").hide();
                        $("#monthday").show();
                    }else if($("#"+id).val()=="Hourly"){
                        $("#weekday").hide();
                        $("#hourlyId").show();
                        $("#monthday").hide();
                    }
                    else{
                        $("#monthday").hide();
                        $("#weekday").hide();
                        $("#hourlyId").hide();
                    }
                }
                function tabparamListDisp(){
                    var viewbys=$("#numbuerOfViewbys").val();
                    if (!$('#tabParameters').is(':visible')) {
                        var divHeight=(4/5)*($(window).height());
                        $("#tabParameters").height(divHeight);
                        $("#tabParameters").width(divHeight);
                        //                        $(".dynamicClass").css('height', (((9/10)*(divHeight))-(viewbys*12)));
                        $(".dynamicClass").css('height', divHeight);
                        $(".dynamicClass").css('width', divHeight);

                    }
                    $( "#tabParameters" ).toggle("slow" );
                    // $( "#paramRegionTop" ).toggle(500 );
                    $( "#allParametersTab" ).hide();
                }
                function closeparamsTab()
                {
                    $( "#tabParameters" ).hide();
                    //$( "#allParametersTab" ).hide();
                }

                function   generateQuickTrendChart11(div, data, columns, measureArray, divWid, divHgt){
                    buildLine(div, data, columns, measureArray, divWid, divHgt)
                }
                function renameDashboard(dbName,repId){

                    var isPAEnableforUser='<%=isPowerAnalyserEnableforUser%>'
                    if(isPAEnableforUser=="true"){
                        $("#renameDashboarddiv").dialog('open');
                        $("#oldName").val(dbName);
                        $("#renamesubmit").val(repId);
                    }else{
                        alert("You do not have the sufficient previlages")
                    }


                }
                function createNewNameForDashboard(){
                    var dashBoardId = document.getElementById("renamesubmit").value;
                    var newName = document.getElementById("newName").value;
                    var oldName = document.getElementById("oldName").value;
                    if(newName==''){
                        alert("Please Enter Dashboard New Name");
                    }
                    else{
                        $.ajax({
                            url: "<%=request.getContextPath()%>/dashboardViewer.do?reportBy=renameDashBoard&dashBoardId="+dashBoardId+"&oldName="+oldName+"&newName="+newName,
                            success: function(data){
                                $("#renameDashboarddiv").dialog('close');
                                window.location.href=window.location.href;
                            }
                        });
                    }
                }

        </script>

    </body><script type="text/javascript">
            $(window).load(function(){
                $.post('<%=request.getContextPath()%>/reportViewer.do?reportBy=getDataCall&userId=<%=userid%>',
                function(data) {var data1 = JSON.parse(data);var tags = "";var len = data1.length;
                    for (var i = 0; i < data1.length; i++) {
                        tags += "<li class='parentC' style='background: #0078A7;'><a href='#' style='text-align: left;' onclick='slide_ul_C(tagReportC"+ i +")'>" + data1[i]["Region"] + "</a><ul id='tagReportC" + i + "' style='display: none;height: auto; overflow: auto'></ul></li>";}
                    $("#reports_nav1").append(tags);
                    for (var j = 0; j < len; j++) {displayReportsCategory(data1[j]["Id"], len, j);}});
                function displayReportsCategory(tagId, len, k) {
                    $.ajax({
                        type: 'GET',
                        async: false,
                        cache: false,
                        url: '<%=request.getContextPath()%>/reportViewer.do?reportBy=getTagsBlocks&userId=' +<%=userid%> + '&tagId=' + tagId,
                        success: function(data) {
                            var data2 = JSON.parse(data);var len = data2["reportId"].length;var div = '';
                            for (var i = 0; i < len; i++) {
                                //div += "<li><a href='#' onclick='reportsPath(" + data2["reportId"][i] + ")'>" + data2["tagShortDesc"][i] + "</a></li>";
                                if(data2["tagType"][i]=="R"){
                                    div += "<li><a href='#' onclick='reportsPath(" + data2["reportId"][i] + ")'>" + data2["tagShortDesc"][i] + "</a></li>";
                                }else if(data2["tagType"][i]=='D'){
                                    div += "<li><a href='#'onclick='openDashboard(\""+data2["tagShortDesc"][i]+"\","+data2["reportId"][i]+")'>" + data2["tagShortDesc"][i] + "</a></li>";

                                }
                                else if(data2["tagType"][i]=='T'){
                                    div += "<li><a href='#'onclick='openTimeDashboard(\""+data2["tagShortDesc"][i]+"\","+data2["reportId"][i]+")'>" + data2["tagShortDesc"][i] + "</a></li>";

                                }else if(data2["tagType"][i]=='O'){
                                    div += "<li><a href='#' onclick='openOneView("+data2["reportId"][i]+")'>" + data2['tagShortDesc'][i] + "</a></li>";
                                }
                            }$("#tagReportC" + k).append(div);}});}});
            $("#acc h3").click(function() {$("#acc ul ul").slideUp();if(!$(this).next().is(":visible")) {
                    $(this).next().slideDown();}})
            $( '.treeC li' ).each( function() {
                if( $( this ).children( 'ul' ).length > 0 ) {
                    $( this ).addClass( 'parentC' );}});
            $( '.treeC li.parentC > a' ).click( function( ) { $( this ).parent().children( 'ul' ).slideToggle( 'fast' );
            });  function slide_ul_C(tag_ul_C){$(tag_ul_C).slideToggle( 'fast' );}
    </script></html><%}
long time_end = System.nanoTime();
double seconds = (time_end - time_start) * Math.pow(10, -9);
DataTracker datatrack = new DataTracker();
                      datatrack.setclickdata(request, PbReportId, seconds);%>
<script type="text/javascript">

        $('.mydate').datepicker({
            changeMonth: true,
            changeYear: true
        });
</script>