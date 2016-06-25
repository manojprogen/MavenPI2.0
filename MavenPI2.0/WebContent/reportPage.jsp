<%@page import="com.progen.reportview.db.ProgenReportViewerDAO"%>
<%@page contentType="text/html" pageEncoding="UTF-8" import="com.progen.report.query.PbTimeRanges,com.progen.i18n.TranslaterHelper,java.util.Locale,com.progen.report.display.DisplayParameters,com.google.gson.Gson,com.progen.report.XtendAdapter,java.util.Iterator"%>
<%@page import="java.util.Set,java.util.Map,java.util.List,java.net.InetAddress,prg.db.PbReturnObject,prg.db.PbDb,prg.db.Container,com.progen.reportview.db.PbReportViewerDAO,com.progen.report.PbReportCollection,java.text.DateFormatSymbols"%>
<%@page import="java.util.ArrayList,java.sql.Connection,utils.db.ProgenParam,com.progen.users.UserLayerDAO,com.progen.action.UserStatusHelper,java.util.HashMap,com.progen.reportdesigner.db.ReportTemplateDAO,java.util.StringTokenizer,com.progen.reportview.db.ProgenReportViewerDAO"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<jsp:useBean id="duration" scope="session" class="utils.db.ProgenParam"/>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%
String isMultiCompany =String.valueOf(session.getAttribute("isMultiCompany"));
XtendAdapter.userID = session.getAttribute("USERID").toString();
String insightsUserRole="";
if(request.getSession(false)!=null){
    insightsUserRole = session.getAttribute("insightsUserRole").toString();
}

    if (session.getAttribute("USERID") == null || request.getSession(false) == null) {
        response.sendRedirect(request.getContextPath() + "/baseAction.do?param=logoutApplication");
    } else {

        //added by Mohit Gupta for default locale
                    Locale cL=null;
                   cL=(Locale)session.getAttribute("UserLocaleFormat");
        //ended By Mohit Gupta
        boolean isCompanyValid = false;
        String themeColor = "blue";
        if (session.getAttribute("theme") == null) {
            session.setAttribute("theme", themeColor);
        } else {
            themeColor = String.valueOf(session.getAttribute("theme"));
        }
        //added by manik for pi logo alignment
        String LOGINID = String.valueOf(session.getAttribute("LOGINID"));
   PbReportViewerDAO dao = new PbReportViewerDAO();
        String userid = String.valueOf(session.getAttribute("USERID"));
        if(userid !=null && userid.equalsIgnoreCase("41")){
            isMultiCompany = "false";
        }
//        UserLayerDAO userdao = new UserLayerDAO();
        int USERID = Integer.parseInt((String) session.getAttribute("USERID"));
//            ServletContext context = getServletContext();
//    boolean isPowerAnalyserEnableforUser = false;
    String userType = null;
//    HashMap<String, UserStatusHelper> statushelper;
//    if (context.getAttribute("helperclass") != null) {
//        statushelper = (HashMap) context.getAttribute("helperclass");
//        UserStatusHelper helper = new UserStatusHelper();
//        if (!statushelper.isEmpty()) {
//            helper = statushelper.get(request.getSession(false).getId());
//            if (helper != null) {
////                isQDEnableforUser = helper.getQueryStudio();
//                isPowerAnalyserEnableforUser = helper.getPowerAnalyser();
//                userType = helper.getUserType();
//            }
//        }
//    }
//        userType = "ANALYZER";
//        PbDb pbdb = new PbDb();
        String userId = "";
        String morefiltercolor = "false";
        userId = String.valueOf(request.getSession(false).getAttribute("USERID"));
//        String userstart = "select start_page from prg_ar_users where pu_id=" + userId;
        //PbReturnObject userstartpbro = pbdb.execSelectSQL(userstart);
      //  userstartpbro.writeString();
        String homeVar = "";
        String brcheckd = "ie";
//        String strpage = userstartpbro.getFieldValueString(0, 0);
//        if (strpage == null || strpage.equalsIgnoreCase("")) {
//            homeVar = "home.jsp";
//        } else if (strpage != null && strpage.equalsIgnoreCase("newHome.jsp")) {
//            homeVar = "newHome.jsp";
//
//        } else if (strpage != null && strpage.equalsIgnoreCase("landingPage.jsp")) {
//            homeVar = "landingPage.jsp";
//        } else {
//            homeVar = "home.jsp";
//        }
        String reportId1 = (String) request.getAttribute("REPORTID");
           String fileLocation ="";
            if(session != null){
            fileLocation = dao.getFilePath(session);
        }else {
               fileLocation = "/usr/local/cache";
            }
        XtendAdapter adapter = new XtendAdapter();
        request.setAttribute("typegbl", "false");
        String reportData = adapter.getFilters(reportId1, fileLocation, request);
        request.removeAttribute("typegbl");
        Map<String, List<String>> allFiltersnames = new HashMap<String, List<String>>();

        Map<String, List<String>> FilterMap = new HashMap<String, List<String>>();Map<String, String> notInMore = new HashMap<String, String>();
        Map<String, List<String>> filterMapgraphs = new HashMap<String, List<String>>();
        Map<String, List<String>> filterMaptrend = new HashMap<String, List<String>>();
        Map<String, List<String>> filterMapadvance = new HashMap<String, List<String>>();
        Map<String, List<String>> advFilterMap = new HashMap<String, List<String>>();
        Map<String, List<String>> TrendFilterMap = new HashMap<String, List<String>>();
        Map<String, List<String>> allFilters = new HashMap<String, List<String>>();
        List<String> viewbynamess = new ArrayList<String>();
        List<String> parameterlistt = new ArrayList<String>();
        List<String> viewbynames = new ArrayList<String>();
        List<String> viewbynamesold = new ArrayList<String>();
        List<String> filtervalues = new ArrayList<String>();
        List<String> parameterlist = new ArrayList<String>();
        List<String> parameterlistold = new ArrayList<String>();
        allFiltersnames = (Map<String, List<String>>) request.getAttribute("allFiltersnames");
        allFilters = (Map<String, List<String>>) request.getAttribute("allFilters");
        parameterlist = (List<String>) request.getAttribute("parameterlist");session.setAttribute("parameterlist",parameterlist);session.setAttribute("allFiltersnames",allFiltersnames);
        viewbynames = (List<String>) request.getAttribute("viewbynames");session.setAttribute("viewbynames",viewbynames);session.setAttribute("allFilters",allFilters);
        viewbynamesold=viewbynames;parameterlistold=parameterlist;session.setAttribute("viewbynamesold",viewbynames);session.setAttribute("parameterlistold",parameterlist);
        FilterMap = (Map<String, List<String>>) session.getAttribute("FilterMap");
        advFilterMap = (Map<String, List<String>>) session.getAttribute("advFilterMap");
        TrendFilterMap = (Map<String, List<String>>) session.getAttribute("TrendFilterMap");
        Gson gson = new Gson();
         Map<String,String> reloadFlag = new HashMap<String,String>();
        ProgenReportViewerDAO pbdao = new ProgenReportViewerDAO();
        String mgmtDBFlag=pbdao.getManagementDashboardFlag(reportId1);
%>

<%
//added by krishan
    // String defaulttab=String.valueOf(session.getAttribute("opentab"));
    String defaulttab = String.valueOf(session.getAttribute("DEFAULT_TAB"));
    String filterShow = "true";
    String gblsequnce = "";
    String isResetGO = String.valueOf(session.getAttribute("isGraphGO"));
    String isGraphRefresh = String.valueOf(session.getAttribute("isGraphRefresh"));
    String requestValue = request.getParameter("action");
    //     added by Manik
    String fromOneview = (String) request.getParameter("fromOneview");
    String adhocviewby = (String) request.getParameter("adhocviewby");
    if (fromOneview != null && !fromOneview.equalsIgnoreCase("")) {
        if (fromOneview.equalsIgnoreCase("true")) {
            if (defaulttab == null || defaulttab.equalsIgnoreCase("")) {
                defaulttab = (String) request.getParameter("DEFAULT_TAB");
                gblsequnce = (String) request.getParameter("gblsequnce");
            }
        }
    }
    String fromVisual = (String) request.getParameter("fromVisual");
    if (fromVisual != null && !fromVisual.equalsIgnoreCase("")) {
        if (fromVisual.equalsIgnoreCase("true")) {
//            if (defaulttab == null || defaulttab.equalsIgnoreCase("")) {
            defaulttab = (String) request.getParameter("DEFAULT_TAB");
//            }
        }
    }
//
    //String defaulttab2=(String) request.getAttribute("defaulttab");

    boolean isQDEnableforUser = false;

    boolean isXtendUser = false;
    boolean isPowerAnalyserEnableforUser = false;
    boolean isRestrictedPowerAnalyserEnable = false;
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
            if(isPowerAnalyserEnableforUser)
                //Added by Ashutosh for Restricted PowerAnalyzer 11-12-2015
                isRestrictedPowerAnalyserEnable=helper.getRestrictedPowerAnalyser();
                session.setAttribute("isRestrictedPowerAnalyserEnable",isRestrictedPowerAnalyserEnable);
                //Ended by Ashutosh
        }
    }
//    ProgenParam connectionparam = new ProgenParam();

    // added by manik
    //  String reportTag = (String) request.getAttribute("reportTag");
    //   String reportTag = "map";
//    String reportTag = (String) request.getAttribute("reportTag");
    Container container = Container.getContainerFromSession(request, reportId1);
    PbReportCollection collect = new PbReportCollection();
    String rolesid = "";
    if (container != null) {
        collect = container.getReportCollect();
            XtendAdapter.roleId = collect.reportBizRoles[0];
            if(collect.reportBizRoles[0] == null){
            XtendAdapter.roleId = "2006";
            }
            rolesid = collect.reportBizRoles[0];
//        String[] roleId = collect.reportBizRoles;
//        HashMap<String,String> roleMap = new HashMap<String,String>();
//        roleMap.put(reportId1,roleId[0]);
//        request.getSession(false).setAttribute("roleMap", roleMap);
       /// dao.setGlobalIcon(container);          //added by krishan pratap
    }

//    String lastupdateedate = "";
//    Connection con = null;
//    ArrayList<String> qryelements = collect.reportQryElementIds;

    //added by Ram  for getting viewByElements for language lookup
     ReportTemplateDAO rto=new ReportTemplateDAO();
      HashMap mapParamId = collect.reportParameters;
      ArrayList<String> viewByElementIds = new ArrayList();
       for (Object key : mapParamId.keySet()) {
            viewByElementIds.add(key.toString());
    }
    String listString = "";
    String selecteddata=container.getshowfilters();
    for (String s : viewByElementIds)
    {
        if(!s.equalsIgnoreCase("TIME")&&!s.equalsIgnoreCase("none")&&!s.equalsIgnoreCase("KPI")){
        listString += s + ",";
    }
    }
    String viewByEleIds="";
    if(listString != null && !listString.isEmpty()){
     viewByEleIds=listString.substring(0, listString.length()-1);
       }
    container.setLookupViewBys(viewByEleIds);

    PbReturnObject retObject = null;
        retObject=rto.getLookupData(viewByEleIds);
        HashMap lookup=new HashMap();
         HashMap lookupdata=new HashMap();
         if(retObject != null){
        for(int i=0;i<retObject.getRowCount();i++)
        {
        lookup.put(retObject.getFieldValueString(i, 0), retObject.getFieldValueString(i, 1));
        lookupdata.put(retObject.getFieldValueString(i, 1), retObject.getFieldValueString(i, 0));
    }
               }
container.setFilterLookupData(lookupdata);
container.setFilterLookupOriginalToNew(lookup);
    //end Ram code

                            // added by krishan pratap
        String showall="";
        boolean showicons=false;
        String showiconreport="";
        String showicongraph="";
        String showicontrends="";
        String showiconadvancevisual="";
        showall=container.showall;
        showicons=container.showicons;
        if(showall == "none"){
             showiconreport=container.showiconreport;
             showicongraph=container.showicongraph;
             showicontrends=container.showicontrends;
             showiconadvancevisual=container.showiconadvancevisual;
       }else{
             showiconreport="block";
             showicongraph="block";
             showicontrends="block";
             showiconadvancevisual="block";
        }

//ended by krishan pratap

    String elementId = "";
    //code added by Dinanath
    String resetpath=null;
    if(collect.resetPath!=null){
    resetpath = (collect.resetPath).toString().split("/")[2];
    resetpath = request.getContextPath()+"/"+resetpath;
    }else{
        resetpath=request.getContextPath()+"/reportViewer.do?reportBy=viewReport&REPORTID="+reportId1+"&action=reset";
    }//end of code by dinanath
//     ArrayList<String> timeinfo = collect.timeDetailsArray;
//     String sytm = container.isYearCal;
//    if (qryelements != null && !qryelements.isEmpty()) {
//        elementId = qryelements.get(0);
//        con = connectionparam.getConnection(elementId);
//        lastupdateedate = dao.getLastUpdatedDate(con, elementId);
//    }

    //Added by Faiz Ansari
                    String[] timeDetails=new String[3];
                    int f=0;
                    String datetype="";
                    ArrayList<String> timeinfo = collect.timeDetailsArray;
                  //
                 //
                     StringTokenizer st = new StringTokenizer(timeinfo.get(2),"/");
                     while (st.hasMoreTokens()) {
                         timeDetails[f]=(st.nextToken());
                         f++;
                     }
                     String date=timeDetails[1];String datedisplay="";  HashMap vals111 = new HashMap();
                     String month = new DateFormatSymbols().getMonths()[(Integer.parseInt(timeDetails[0]))-1];
                     String year = timeDetails[2];
                     //added by anitha
                     String cmpdatedisplay="";
                      if (timeinfo.get(1).equalsIgnoreCase("PRG_STD")){
                         // start of sandeep code for date displaying, track id- TA_R_02
                          vals111 = collect.getTimememdetails();
                            ArrayList timeotherval = new ArrayList();
                            //modified by anitha
                                        ArrayList months = new ArrayList();
                                                months.add(0," ");
                                                months.add(1,"Jan");
                                                months.add(2,"Feb");
                                                months.add(3,"Mar");
                                                months.add(4,"Apr");
                                                months.add(5,"May");
                                                months.add(6,"Jun");
                                                months.add(7,"Jul");
                                                months.add(8,"Aug");
                                                months.add(9,"Sep");
                                                months.add(10,"Oct");
                                                months.add(11,"Nov");
                                                months.add(12,"Dec");

                            if(vals111!=null){
                               timeotherval= (ArrayList)vals111.get("PR_DAY_DENOM");
                            if(timeotherval!=null){
                                if(timeotherval.get(0).toString().contains("-")){
                                    String monthValue1 = months.get(Integer.parseInt((String)timeotherval.get(0).toString().split(" ")[0].split("-")[1])).toString();
                                    String monthValue2 = months.get(Integer.parseInt((String)timeotherval.get(1).toString().split(" ")[0].split("-")[1])).toString();
                                    String monthValue3 = months.get(Integer.parseInt((String)timeotherval.get(2).toString().split(" ")[0].split("-")[1])).toString();
                                    String monthValue4 = months.get(Integer.parseInt((String)timeotherval.get(3).toString().split(" ")[0].split("-")[1])).toString();
                            String stdate=timeotherval.get(0).toString().split(" ")[0].split("-")[2]+"-"+monthValue1+"-"+timeotherval.get(0).toString().split(" ")[0].split("-")[0];
                            String enddate=timeotherval.get(1).toString().split(" ")[0].split("-")[2]+"-"+monthValue2+"-"+timeotherval.get(1).toString().split(" ")[0].split("-")[0];
                            String stdatecmp=timeotherval.get(2).toString().split(" ")[0].split("-")[2]+"-"+monthValue3+"-"+timeotherval.get(2).toString().split(" ")[0].split("-")[0];
                            String enddatecmp=timeotherval.get(3).toString().split(" ")[0].split("-")[2]+"-"+monthValue4+"-"+timeotherval.get(3).toString().split(" ")[0].split("-")[0];
                  datedisplay="("+stdate+" To "+enddate+")";
                  cmpdatedisplay=stdatecmp+" To "+enddatecmp;
                                   }else{
                                    String monthValue1 = months.get(Integer.parseInt((String)timeotherval.get(0).toString().split(" ")[0].split("/")[0])).toString();
                                    String monthValue2 = months.get(Integer.parseInt((String)timeotherval.get(1).toString().split(" ")[0].split("/")[0])).toString();
                                    String monthValue3 = months.get(Integer.parseInt((String)timeotherval.get(2).toString().split(" ")[0].split("/")[0])).toString();
                                    String monthValue4 = months.get(Integer.parseInt((String)timeotherval.get(3).toString().split(" ")[0].split("/")[0])).toString();
                                     String stdate=timeotherval.get(0).toString().split(" ")[0].split("/")[1]+"-"+monthValue1+"-"+timeotherval.get(0).toString().split(" ")[0].split("/")[2];
                                     String enddate=timeotherval.get(1).toString().split(" ")[0].split("/")[1]+"-"+monthValue2+"-"+timeotherval.get(1).toString().split(" ")[0].split("/")[2];
                                     String stdatecmp=timeotherval.get(2).toString().split(" ")[0].split("/")[1]+"-"+monthValue3+"-"+timeotherval.get(2).toString().split(" ")[0].split("/")[2];
                                     String enddatecmp=timeotherval.get(3).toString().split(" ")[0].split("/")[1]+"-"+monthValue4+"-"+timeotherval.get(3).toString().split(" ")[0].split("/")[2];
                  datedisplay="("+stdate+" To "+enddate+")";
                  cmpdatedisplay=stdatecmp+" To "+enddatecmp;
                            }
                            }
                                   }
                              // end of sandeep code
                     if (timeinfo.get(3).equalsIgnoreCase("Year") ){
                        if(container.getNewUIyr()!=null && container.getNewUIyr()!=""){
                          year=container.getNewUIyr();    datetype="Yearly";
                         }
                     }else if(timeinfo.get(3).equalsIgnoreCase("Qtr")){
                         if(container.getNewUiqr()!=null && container.getNewUiqr()!=""){
                          year=container.getNewUiqr();     datetype="Quarterly";
                     }
                     }else if(timeinfo.get(3).equalsIgnoreCase("Month")){
                         datetype="Monthly";
                     }else if(timeinfo.get(3).equalsIgnoreCase("Day")){//added by sruthi for daily
                         datetype="Daily";
                     }//ended by sruthi
                      else{
                                datetype="Weekly";
                     }
                                         }
                     String mon1=timeDetails[0];
                     String yr1=timeDetails[2];
                     String date2="";
                     String date3="";
                     String date4="";
                     String month2 ="";
                     String year2 ="";
                     String mon2="";
                     String common1="";
                     String common2="";
                     String comyr1="";
                     String comyr2="";
                    String yr2="";
                    String compare="";
                    if (timeinfo.get(1).equalsIgnoreCase("PRG_DATE_RANGE")){
                        f=0;
                        st = new StringTokenizer(timeinfo.get(3),"/");
                        while (st.hasMoreTokens()) {
                         timeDetails[f]=(st.nextToken());
                         f++;
                         }
                        date2=timeDetails[1];
                        month2 = new DateFormatSymbols().getMonths()[(Integer.parseInt(timeDetails[0]))-1];
                        year2 = timeDetails[2];
                        mon2=timeDetails[0];
                        yr2=timeDetails[2];

                        f=0;
                        if(timeinfo.get(4)==null){
                            timeinfo.set(4, timeinfo.get(2));
                            }
                        if(timeinfo.get(5)==null){
                            timeinfo.set(5, timeinfo.get(3));
                            }
                        st = new StringTokenizer(timeinfo.get(4),"/");
                        while (st.hasMoreTokens()) {
                         timeDetails[f]=(st.nextToken());
                         f++;
                         }
                        date3=timeDetails[1];
                        common1=timeDetails[0];
                        comyr1=timeDetails[2];
                        f=0;
                        st = new StringTokenizer(timeinfo.get(5),"/");
                        while (st.hasMoreTokens()) {
                         timeDetails[f]=(st.nextToken());
                         f++;
                         }
                        date4=timeDetails[1];
                        common2=timeDetails[0];
                        comyr2=timeDetails[2];

                    }else{
                        if (timeinfo.get(4).equalsIgnoreCase("Last Month")){
                            compare="MOM";
                        }else  if (timeinfo.get(4).equalsIgnoreCase("Same Month Last Year")){
                            compare="MOY";
                        }else  if (timeinfo.get(4).equalsIgnoreCase("Last Qtr")){
                             compare="QOQ";
                        }else if (timeinfo.get(4).equalsIgnoreCase("Complete Last Month")){
                           compare="CMOM";
                        }else  if (timeinfo.get(4).equalsIgnoreCase("Same Qtr Last Year")){
                            compare="QOY";
                    }else  if (timeinfo.get(4).equalsIgnoreCase("Complete Same Month Last Year")){
                            compare="CMOY";
                    }else  if (timeinfo.get(4).equalsIgnoreCase("Complete Last Qtr")){
                            compare="CQOQ";
                    }else  if (timeinfo.get(4).equalsIgnoreCase("Complete Same Qtr Last Year")){
                            compare="CQOY";
                    
                    }else  if (timeinfo.get(4).equalsIgnoreCase("Last Year")){
                            compare="YOY";
                    
                    }else  if (timeinfo.get(4).equalsIgnoreCase("Complete Same Qtr Last Year")){
                            compare="CYOY";
                    }else if(timeinfo.get(4).equalsIgnoreCase("Previous Day")){//added by sruthi for daily
                        compare="DOD";
                    }else if(timeinfo.get(4).equalsIgnoreCase("Same Day Last Week ")){
                        compare="DOW";
                    }else if(timeinfo.get(4).equalsIgnoreCase("Same Day Last Month")){
                        compare="DOM";
                    }else if(timeinfo.get(4).equalsIgnoreCase("Same Day Last Year")){
                        compare="DOY";
                    }//ended by sruthi
                    }
       ////End!!!
                    String qtr=collect.Qtrtype;if(qtr!=null && qtr!=""){}else{qtr="Q1";}

    String reportName = "";

    reportName = container.getReportName();
    //added by sruthi
    String PbReportId = container.getReportId();
//    String rolesidqry = "select folder_id from prg_ar_report_details where report_id=" + PbReportId;
//    PbReturnObject roleobj = pbdb.execSelectSQL(rolesidqry);
    //ended by sruthi
     String veractionStatus = String.valueOf(session.getAttribute("status"));
    String companyId = "";
    String compLogo = "";
    String bussLogo = ""; String userflag = "";
    if(mgmtDBFlag.equalsIgnoreCase("true")){
        veractionStatus="OK";
    }
    if(veractionStatus.equalsIgnoreCase("OK")){ userflag = "veraction";}
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
            currentURL = (String) request.getAttribute("currentURL");
//                    currentURL = (String)session.getAttribute("currentURL");
            if (currentURL == null) {
                currentURL = "";
            }
        } catch (Exception exp) {
            exp.printStackTrace();
        }
    } else {
        response.sendRedirect(request.getContextPath() + "/baseAction.do?param=logoutApplication");
    }
    String reportTabs[] = {"Report", "Graph"};
    String ctxpath=request.getContextPath();
     if(veractionStatus.equalsIgnoreCase("OK")){
         datedisplay="";cmpdatedisplay="";
     }

%>

<html>
    <head>
        <script type="text/javascript" src="<%= ctxpath%>/JS/global.js"></script>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <script type="text/javascript" src="<%=ctxpath%>/JS/Grid/jquery.gridster.js"></script>
        <script type="text/javascript" src="<%=ctxpath%>/jQuery/d3/customtooltip.js"></script>
        <link type="text/css" href="<%=ctxpath%>/css/d3/tooltip.css" rel="stylesheet"/>
        <link type="text/css" href="<%=ctxpath%>/stylesheets/themes/<%=themeColor%>/TableCss.css" rel="stylesheet" />
        <link type="text/css" href="<%=ctxpath%>/stylesheets/themes/<%=themeColor%>/pbGraph.css" rel="stylesheet" />
        <link type="text/css" href="<%=ctxpath%>/stylesheets/themes/<%=themeColor%>/pbReportViewerCSS.css" rel="stylesheet" />
        <link type="text/css" href="<%=ctxpath%>/stylesheets/themes/<%=themeColor%>/toggle-button.css" rel="stylesheet" />
        <link type="text/css" href="<%=ctxpath%>/css/reportPage.css" rel="stylesheet" />
        <script type="text/javascript" src="<%=ctxpath%>/javascript/pbReportViewerJS.js"></script>
        
          <script type="text/javascript" src="<%=ctxpath%>/TableDisplay/JS/pbTableMapJS.js"></script>
        <script type="text/javascript" src="<%=ctxpath%>/javascript/reportviewer/ReportViewer.js"></script>
        <script type="text/javascript" src="<%=ctxpath%>/TableDisplay/JS/pbTableMapJSForPopUp.js"></script>
        <script src="<%=ctxpath%>/TableDisplay/JS/pbDisplayJS.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=ctxpath%>/jQuery/d3/d3.v3.min.js"></script>
        <script type="text/javascript" src="<%=ctxpath%>/jQuery/d3/jquery.dataTables.js"></script>
        <script type="text/javascript" src="<%=ctxpath%>/jQuery/d3/jquery.dataTables.min.js"></script>
        <script type="text/javascript" src="<%=ctxpath%>/jQuery/d3/colpick.js"></script>
        <script type="text/javascript" src="<%=ctxpath%>/jQuery/d3/d3.layout.cloud.js"></script>
        <script type="text/javascript" src="<%=ctxpath%>/jQuery/d3/chartTypes.js"></script>
        <script type="text/javascript" src="<%=ctxpath%>/jQuery/d3/coloring.js"></script>
        <script type="text/javascript" src="<%=ctxpath%>/jQuery/d3/jscolor.js"></script>
       <script type="text/javascript" src="<%=ctxpath%>/jQuery/d3/topojson.v1.min.js"></script>
        <link href="css/select2.css"  rel="stylesheet" type="text/css"/>
        <script type="text/javascript" src="<%=ctxpath%>/JS/d3.geo.min.js"></script>
<!--        <script type="text/javascript" src="<%=ctxpath%>/JS/d3.geo.tile.js.js"></script>-->
        <script src="JS/select2.min.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%= ctxpath%>/JS/reportgbl/jquery.multiselect.js"></script>
        <script type="text/javascript" src="<%= ctxpath%>/JS/jquery.multiselect.filter.js"></script>
        <script type="text/javascript" src="<%=ctxpath%>/javascript/reportviewer/progenGraphViewer.js"></script>
        <script type="text/javascript" src="<%=ctxpath%>/javascript/reportviewer/graphViewer.js"></script>
        <script type="text/javascript" src="<%=ctxpath%>/javascript/saveSvgAsPng.js"></script>
        <script type="text/javascript" src="<%=ctxpath%>/javascript/from_html.js"></script>
        <script type="text/javascript" src="<%=ctxpath%>/javascript/jspdf.js"></script>
        <script type="text/javascript" src="<%=ctxpath%>/jQuery/farbtastic12/farbtastic/farbtastic.js"></script>
        <link rel="stylesheet" href="<%=ctxpath%>/jQuery/farbtastic12/farbtastic/farbtastic.css" type="text/css" />
        <link rel="stylesheet" type="text/css" href="css/normalize.css" />
        <link rel="stylesheet" type="text/css" href="css/tabs.css" />
        <link rel="stylesheet" type="text/css" href="css/tabstyles.css" />
        <link rel="stylesheet" type="text/css" href="css/menuTab.css" />
        <link rel="stylesheet" type="text/css" href="css/d3/colpick.css" />
        <link rel="stylesheet" type="text/css" href="css/animatedMenu.css" />
        <link rel="stylesheet" type="text/css" href="css/Grid/jquery.gridster.css" /> <!-- added by manik-->
        <link rel="stylesheet" type="text/css" href="css/jquery.multiselect.css" />
<!--        <link type="text/css" href="<%=ctxpath%>/css/global.css" rel="stylesheet" />-->
        <!--<link rel="stylesheet" href="//code.jquery.com/ui/1.11.4/themes/smoothness/jquery-ui.css">-->
        <style type="text/css">
            .imgShadow{
                border-radius: 2px;
                background: white none repeat scroll 0% 0%;
                box-shadow: 8px 3px 5px white;}

#gridsterDiv::-webkit-scrollbar-track
{
	-webkit-box-shadow: inset 0 0 6px rgba(0,0,0,0.3);
	border-radius: 1px;
	background-color: #F5F5F5;
}

#gridsterDiv::-webkit-scrollbar
{
	width: 3px;
        height: 3px;
	background-color: #F5F5F5;
}

#gridsterDiv::-webkit-scrollbar-thumb
{
	border-radius: 1px;
	-webkit-box-shadow: inset 0 0 6px rgba(0,0,0,.3);
	background-color: #555;
}

        </style>

        <style class="cp-pen-styles">
            .container{
                /*margin-left: 2%;*/
                width: 100%;

            }
            .no-csstransforms .cn-button {
                display: none;
            }

            .btn-custom2 { background-color: hsl(190, 80%, 43%) !important; background-repeat: repeat-x;
                           filter: progid:DXImageTransform.Microsoft.gradient(startColorstr="#27c7e7", endColorstr="#15a8c5");
                           background-image: -khtml-gradient(linear, left top, left bottom, from(#27c7e7), to(#15a8c5));
                           background-image: -moz-linear-gradient(top, #27c7e7, #15a8c5);
                           background-image: -ms-linear-gradient(top, #27c7e7, #15a8c5);
                           background-image: -webkit-gradient(linear, left top, left bottom, color-stop(0%, #27c7e7), color-stop(100%, #15a8c5));
                           background-image: -webkit-linear-gradient(top, #27c7e7, #15a8c5); background-image: -o-linear-gradient(top, #27c7e7, #15a8c5);
                           background-image: linear-gradient(#27c7e7, #15a8c5); border-color: #15a8c5 #15a8c5 hsl(190, 80%, 40.5%);
                           color: #fff !important; text-shadow: 0 -1px 0 rgba(0, 0, 0, 0.16); -webkit-font-smoothing: antialiased;
            }
            #rightDiv1{
                /*width:1300px;*/
                /*    height:50px;*/
            }
            #list-container {
                overflow:hidden;
                width: 88%;
                height:30px;
                float:left;
                margin-left: 0px;
            }

            #list-containertable {overflow:hidden;width:90%;height:30px;float:left;}
            #list-containertr {overflow:hidden;width: 85vw;height:30px;float:left;}
            #list-containerad {overflow:hidden;width: 90vw;height:30px;float:left;}
            .list{border:0px;width:90%; float:left; }
            .listviewby{border:0px;width:100%;float:left;  }
.listtable{
                /*    background:grey;*/
                border:0px;
               width:100%;
                float:left;
            }


            #arrowR{
                /*background:yellow;*/
                width:20px;
                height:10px;
                float:right;
                cursor:pointer;
            }
            #arrowRtr{width:20px;height:10px;float:right;cursor:pointer;}
            #arrowRad{width:20px;height:10px;float:right;cursor:pointer;}
            #arrowRtable{width:20px;height:10px;display:none;float:right;cursor:pointer;}
            #resetgraph{width:40px;height:10px;float:right;cursor:pointer;}
            #quickfilter{width:40px;height:10px;float:right;cursor:pointer;}
            #gottabId11{
                width:20px;
                height:10px;
                float:right;
                cursor:pointer;
            }
            #morefilters{
                width:20px;
                height:10px;
                z-index: 101;
                float:right;
                cursor:pointer;
            }

            #arrowL{
                /*background:yellow;*/
                width:20px;
                height:10px;
                float:left;
                cursor:pointer;
            }
            #arrowLtr{width:20px; height:10px;  float:left;cursor:pointer;}
            #arrowLad{width:20px; height:10px;  float:left;cursor:pointer;}
            #arrowLtable{width:20px; height:10px; display:none; float:left;cursor:pointer;}


            .item{
                margin:5px;
                float:left;
                position:relative;
            }
            .itemad{ margin:5px; float:left; position:relative;}
            .itemtr{ margin:5px; float:left; position:relative;}
            .itemtable{ margin:5px; float:left; position:relative;}


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

            /*******************************
            Author:-Faiz Ansari
            Details:- These css codes are used to style the header/banner when report/graph/advance visuals are opned.
            *******************************/
            /*** Code Start ***/
            .select2-results__option,.select2-selection__rendered{font-family:Helvetica, Arial,  sans-serif;font-weight:500;font-size: 12px; color:#444;text-align:center}
            .customRadio1,.customRadio2,.customRadio3{
                width: 8px;
                height: 8px;
                border-radius: 50%;
                background-color: #ffffff;
                border:2px solid #fff;

            }
            .customSelect {
                float: left;width: 40%;background-color: #f2f2f2;color: #545759;height:16px;
                font-size: 14px;margin-left: 4%;padding: 5px;border-radius: 2px;cursor:pointer;
            }
            .customSelect span{font-family:Helvetica, Arial,  sans-serif;font-weight:500;font-size: 14px;}
            .customSelect i{float:right;padding-right:5px;font-size:18px;}
            .customSelect ul{display:none;position: absolute;width: 98px;margin-top: 6px;background-color: #f2f2f2;box-shadow: 0px 1px 2px 0px #808080}
            .customSelect ul li{padding:5px;font-family:Helvetica, Arial,  sans-serif;font-weight:500;font-size: 14px;}
            .customSelect ul li:hover{background-color: #8BC34A}
            .timeRadio{display: none}

            select option{
                font-family:Helvetica, Arial,  sans-serif;
                font-weight:500;
                font-size: 14px;
            }
            .pdd1{padding:2px 0px 2px 14px;}
            .mrg-l{margin-left:10px;}
            .hoverEft:hover {background-color:#8bc34a;border-radius:1px;cursor:pointer;padding:5px;}

            #goBtn:hover {background-color:#8bc34a}
            .ftddropmnu{margin:0px;padding:0px;display:none;background-color:#F8F8F8}
            .ftddropmnu li{color:rgb(80, 80, 80);list-style-type:none;cursor:pointer;padding:5px 10px 5px 10px;
                           text-decoration:none; font-family:Helvetica, Arial,  sans-serif;font-weight:500;font-size: 12px;}
            .ftddropmnu li input{margin-right:10px}
            .ftddropmnum{display:block;background-color:#FDFDFD;color:#000;font-family:Helvetica, Arial,  sans-serif;
                         font-weight:500;font-size: 12px;padding:5px;border-top:1px solid #D0D0D0;border-bottom:1px solid #D0D0D0;cursor:pointer}
            #reportMenu{position:absolute;background-color: #000;margin-top:5px;display:none}
            #reportMenu li{display:block;list-style-type: none;padding:5px;cursor:pointer}
            #reportMenu li a{color:#fff;text-decoration:  none;font-family:Helvetica, Arial,  sans-serif;font-weight:500;font-size: 14px;}
            #reportMenu li:hover{background-color:#8bc34a}
            #optionMenuDiv {overflow: hidden;overflow-y: auto;display:none;width:180px;background-color:#fff;margin-top: 30px;box-shadow:0px 2px 2px 1px #888;position:absolute;right:2px;border: 1px solid #aaa;}
            #optionMenuDiv li{list-style-type: none;cursor:pointer;border-bottom: 1px solid #ccc;color:#555;padding:4px;}
            #optionMenuDiv li:hover{background-color:#8bc34a}
            #optionMenuDiv li:hover a{color:#fff}
            #optionMenuDiv li a{display:block;padding:5px;text-decoration: none;color:#262626;font-size: 12px;}
            /*#stdDtDiv1 {width:60%};*/
            /*#hdTimeCtrlDiv {margin-top: 48px};*/
            /*** Code End ***/

        </style>
    <body onclick='hideEle();'>
        <script type="text/javascript">
            window.mgmtDBFlag="<%=mgmtDBFlag%>";
//alert("is it "+<%=isRestrictedPowerAnalyserEnable%>);
//alert("is it isMultiCompany "+<%=isMultiCompany %>);
var isMultiCompany="<%=isMultiCompany %>";
var userflag="<%=userflag%>";
             function hideme(id){
                   if(id=="mmoreFilters"){
                        $("#tableOption").fadeIn();
                   }
                   $("#"+id).fadeOut();
               }
                
//                  $("#stdDtDiv1").width("60%");
//                           $("#hdTimeCtrlDiv").css('margin-top','48px');
            //var defaulttab = '';
            //added by ram
            var lookupdataMap = {};var viewbynamesold=[];viewbynamesold='<%=viewbynamesold%>';viewbynamesold=viewbynamesold.split(",");
             var lookupMap = {};var parameterlistold=[];parameterlistold='<%=parameterlistold%>';parameterlistold=parameterlistold.split(",");
            parent.lookupdataMap = <%= gson.toJson(lookupdata)%>;
            parent.lookupMap = <%= gson.toJson(lookup)%>;

            //end Ram code
            var gridster1;
            var counter = 0;
            //               var filterMapgraph = {};
            //    var filterMaptrend = {};
            var filterMapglobal = {};
            var isPowerAnalyserEnableforUser = '<%= isPowerAnalyserEnableforUser%>';
             
 var draggable = '<%=container.chart_isDraggable%>';
 window.userId = '<%=userId%>';
        <%if(!insightsUserRole.equalsIgnoreCase("")){%>
               window.userType = '<%=insightsUserRole%>';
        <%}else{%>
            window.userType = '<%= userType%>';
        <%}%>
            var isResetGO = '<%= isResetGO%>';
            var isGraphRefresh = '<%= isGraphRefresh%>';
            var isPowerAnalyserEnableforUser = '<%= isPowerAnalyserEnableforUser%>';

            //sandeep
            function displayFilt1(filName) {
                var selectid = filName;
                filName = filName.split("__")[1];
                var filName11 = filName.split("1q1");
                var filName1;
                if (filName11.length > 1) {
                    for (var j = 0; j < filName11.length; j++) {
                        if (j == 0) {
                            filName1 = filName11[j]
                        } else {
                            filName1 = filName1 + " " + filName11[j]
                        }
                    }
                } else {
                    filName1 = filName.replace("1q1", " ");
                }
                if (filName1 == "") {
                    filName1 = filName;
                }
                //                if(filName1.length>20){
                //                    filName1=filName1.substring(0, 18);
                //                }
                //filName1=filName1.replace("1q1", " ");
                $("#" + selectid).multiselect({
                    selectedText: "# of # selected",
                    noneSelectedText: filName1,
                    selectedList: 2
                }).multiselectfilter();
            }
            function displayFiltadv(filName) {
                 var selectid = filName;
                filName = filName.split("__")[1];
                var filName11 = filName.split("1q1");
                var filName1;
                if (filName11.length > 1) {
                    for (var j = 0; j < filName11.length; j++) {
                        if (j == 0) {
                            filName1 = filName11[j]
                        } else {
                            filName1 = filName1 + " " + filName11[j]
                        }
                    }
                } else {
                    filName1 = filName.replace("1q1", " ");
                }
                filName1 = filName1.replace("ad", "");
                //                if(filName1.length>20){
                //                    filName1=filName1.substring(0, 18);
                //                }
                if (filName1 == "") {
                    filName1 = filName;
                }
                //     filName1=filName1.replace("1q1", " ");
                $("#" + selectid).multiselect1({
                    selectedText: "# of # selected",
                    noneSelectedText: filName1,
                    selectedList: 2
                }).multiselectfilter1();
            }
            function displayFiltTrend(filName) {
                 var selectid = filName;
                filName = filName.split("__")[1];
                var filName11 = filName.split("1q1");
                var filName1;
                if (filName11.length > 1) {
                    for (var j = 0; j < filName11.length; j++) {
                        if (j == 0) {
                            filName1 = filName11[j]
                        } else {
                            filName1 = filName1 + " " + filName11[j]
                        }
                    }
                } else {
                    filName1 = filName.replace("1q1", " ");
                }

                filName1 = filName1.replace("Tr", "");
                //                if(filName1.length>20){
                //                    filName1=filName1.substring(0, 18);
                //                }
                if (filName1 == "") {
                    filName1 = filName;
                }
                //     filName1=filName1.replace("1q1", " ");
                $("#" + selectid).multiselectTrend({
                    selectedText: "# of # selected",
                    noneSelectedText: filName1,
                    selectedList: 2
                }).multiselectfilterTrend();
            }
            function displayFilttable(filName) {
                //                var filName1=filName.replace("1q1", " ");
                var selectid = filName;
                filName = filName.split("__")[1];
                var filName11 = filName.split("1q1");
                var filName1;
                if (filName11.length > 1) {
                    for (var j = 0; j < filName11.length; j++) {
                        if (j == 0) {
                            filName1 = filName11[j]
                        } else {
                            filName1 = filName1 + " " + filName11[j]
                        }
                    }
                } else {
                    filName1 = filName.replace("1q1", " ");
                }

                //  filName1=filName.replace("1q1", " ");
                filName1 = filName1.replace("table", "").replace("11q", "/");
                ;
                //                if(filName1.length>20){
                //                    filName1=filName1.substring(0, 20)+"..";
                //                }
                if (filName1 == "") {
                    filName1 = filName
                }
                $("#" + selectid).multiselecttable({
                    selectedText: "# of # selected",
                    noneSelectedText: filName1,
                    selectedList: 2
                }).multiselectfiltertable();
            }
            //end of sandeep code for global filter
            var wid2 = '';
            /*******************************
             Author:-Faiz Ansari
             Module:- This function is used to create and display more filters.
             *******************************/
            /*** Function Start ***/

            function Datedisplayregion(){
                if($("#Datetype").val()!=undefined){
                var selectedmonthval = $("#Datetype").val();
                if(selectedmonthval=='Monthly'){
                     appendDateSelect1(3);
                }else if(selectedmonthval=='Quarterly'){
                     appendDateSelect2(3);
                }else if(selectedmonthval=='Yearly'){
                     appendDateSelect3(3);
                }else if(selectedmonthval=='Weekly'){
                     appendDateSelect4(3);
                } else if(selectedmonthval=='Daily'){//dded by sruthi for daily
                     appendDateSelect5(3);
                }//ended by sruthi
                if(parent.$("#AOId").val()!=null && parent.$("#AOId").val()!=""  &&'<%=timeinfo.get(1)%>' == "PRG_STD"){
                comparitiveMethod();
                }
                if(selectedmonthval=='Yearly'){
                    if(parent.$("#AOId").val()!=null && parent.$("#AOId").val()!=""  &&'<%=timeinfo.get(1)%>' == "PRG_STD"){
                     $("#normalcompspan").css({'top':'15px'});
                     $("#DatedropGO").css({'margin-left':'4%'});
                     }
                }
            }
                }
                 //dded by sruthi for daily
                function appendDateSelect5(id){
                     parent.$("#optMonthly3").css("checked", "false");
                parent.$("#optQuarterly3").css("checked", "false");
                parent.$("#optYearly3").css("checked", "false");
                   parent.$("#optDaily3").css("checked", "true");
                     $(".customRadio" + id).css("background-color", "#fff");
                $("#optMonthly" + id).prev().css("background-color", "#8BC34A");
                $("#apTimeSelect" + id).html("");
                 var htmlVardate = "";
                 htmlVardate += "<span id='comparefromDTToDateD' style='display:block;width:21%;float:left;'>";
                    htmlVardate += "<select id='dateslectDaily' style='width:90%'>";
                   var size=31;
                    for(var i=1;i<=size;i++){
                         htmlVardate += "<option id='dateslectDaily"+i+"' value='"+i+"'>"+i+"</option>";
                    }
                      htmlVardate += "</select></span>";

                  $("#apTimeSelect" + id).html(htmlVardate);
                   $("#apTimeSelect" + id).append("" +
                        "<span id='spanmonth' style='float:left;width:39%'>" +
                        "<select id='selectMonth" + id + "' class='gFontFamily ' style='width:90%;height:30px;padding:5px 0px 5px 14px;margin-left:12px;' onchange='selectDateTo(\"" + 'dateslectmotnh' + "\")'>" +
            <% if(month!=null && month!=""){%>
                // "<option id='selectMonth31' class='gFontFamily  pdd1'><%=month%></option>"+<%}%>
                "<option id='selectMonth31' class='gFontFamily  pdd1'>January</option>" +
                        "<option id='selectMonth32' class='gFontFamily  pdd1'>February</option>" +
                        "<option id='selectMonth33' class='gFontFamily  pdd1'>March</option>" +
                        "<option id='selectMonth34' class='gFontFamily  pdd1'>April</option>" +
                        "<option id='selectMonth35' class='gFontFamily  pdd1'>May</option>" +
                        "<option id='selectMonth36' class='gFontFamily  pdd1'>June</option>" +
                        "<option id='selectMonth37' class='gFontFamily  pdd1'>July</option>" +
                        "<option id='selectMonth38' class='gFontFamily  pdd1'>August</option>" +
                        "<option id='selectMonth39' class='gFontFamily  pdd1'>September</option>" +
                        "<option id='selectMonth310' class='gFontFamily  pdd1'>October</option>" +
                        "<option id='selectMonth311' class='gFontFamily  pdd1'>November</option>" +
                        "<option id='selectMonth312' class='gFontFamily  pdd1'>December</option>" +
                        "</select></span>" +
                        "<span id='spanyear' style='float:left;width:40%'>" +
                        "<select id='selectYear" + id + "' class='gFontFamily  pdd1' style='width:90%;height:30px;padding:5px 0px 5px 25px;margin-right:12px;float:right'>" +
                        //"<option id='selectYear32010' class='gFontFamily  pdd1' selected value=<%=year%> ><%=year%></option>"+
                        "<option id='selectYear32010' class='gFontFamily  pdd1'>2010</option>" +
                        "<option id='selectYear32011' class='gFontFamily  pdd1'>2011</option>" +
                        "<option id='selectYear32012' class='gFontFamily  pdd1'>2012</option>" +
                        "<option id='selectYear32013' class='gFontFamily  pdd1'>2013</option>" +
                        "<option id='selectYear32014' class='gFontFamily  pdd1'>2014</option>" +
                        "<option id='selectYear32015' class='gFontFamily  pdd1'>2015</option>" +
                        "<option id='selectYear32016' class='gFontFamily  pdd1'>2016</option>" +
                        "<option id='selectYear32017' class='gFontFamily  pdd1'>2017</option>" +
                         "<option id='selectYear32018' class='gFontFamily  pdd1'>2018</option>" +
                          "<option id='selectYear32019' class='gFontFamily  pdd1'>2019</option>" +
                           "<option id='selectYear32020' class='gFontFamily  pdd1'>2020</option>" +
                        "</select></span>"
                );
                     $("#selectMonth3" +<%=mon1%>).prop("selected", "true");
                $("#selectYear3" +<%=year%>).prop("selected", "true");
                $("#dateslectDaily" +<%=date%>).prop("selected", "true");
                     var htmlVar = "";
                      <% if(compare!=null && compare.equalsIgnoreCase("DOD")){%>
                           parent.$('#CBO_PRG_COMPARE').val("Previous Day");parent.$('#gCompq').val("Previous Day");
                        htmlVar += "<option class='gFontFamily ' selected>DOD</option>";
                             htmlVar += "<option class='gFontFamily ' >DOW</option>";
                             htmlVar += "<option class='gFontFamily ' >DOM</option>";
                             htmlVar += "<option class='gFontFamily ' >DOY</option>";
                      <%}else  if(compare!=null && compare.equalsIgnoreCase("DOW")){%>
                           parent.$('#CBO_PRG_COMPARE').val("Same Day Last Week");parent.$('#gCompq').val("Same Day Last Week");
                            htmlVar += "<option class='gFontFamily ' selected>DOW</option>";
                             htmlVar += "<option class='gFontFamily ' >DOD</option>";
                             htmlVar += "<option class='gFontFamily ' >DOM</option>";
                             htmlVar += "<option class='gFontFamily ' >DOY</option>";
                      
                      <%}else if(compare!=null && compare.equalsIgnoreCase("DOM")){%>
                           parent.$('#CBO_PRG_COMPARE').val("Same Day Last Month");parent.$('#gCompq').val("Same Day Last Month");
                           htmlVar += "<option class='gFontFamily ' selected>DOM</option>";
                             htmlVar += "<option class='gFontFamily ' >DOD</option>";
                             htmlVar += "<option class='gFontFamily ' >DOW</option>";
                             htmlVar += "<option class='gFontFamily ' >DOY</option>";
                          <%} else{%>
                              parent.$('#CBO_PRG_COMPARE').val("Same Day Last Year");parent.$('#gCompq').val("Same Day Last Year");
                            htmlVar += "<option class='gFontFamily ' selected>DOY</option>";
                             htmlVar += "<option class='gFontFamily ' >DOD</option>";
                             htmlVar += "<option class='gFontFamily ' >DOW</option>";
                             htmlVar += "<option class='gFontFamily ' >DOM</option>";
                             <%}%>
                                $("#compSelect3").html("");
                                $("#compSelect3").html(htmlVar);
                                   $('#selectMonth' + id + ',#selectYear' + id + ',#compSelect' + id).select2({
                                      minimumResultsForSearch: Infinity
                                       });
                                 $("#DatedropGO").css("margin-left", "25%");
                                        $("#dateslectDaily").select2({minimumResultsForSearch: Infinity});
                }//ended by sruthi
            function appendDateSelect4(id) {
                parent.$("#optMonthly3").css("checked", "true");
                parent.$("#optQuarterly3").css("checked", "false");
                parent.$("#optYearly3").css("checked", "false");
                  parent.$("#optDaily3").css("checked", "false");
                $(".customRadio" + id).css("background-color", "#fff");
                $("#optMonthly" + id).prev().css("background-color", "#8BC34A");
//                $("#timeMonth"+id).show();
                $("#apTimeSelect" + id).html("");

                var htmlVardate = "";
                 htmlVardate += "<span id='comparefromDTToDateW' style='display:block;width:21%;float:left;'>";
                    htmlVardate += "<select id='dateslectWeek' style='width:90%'>";
                   var size=31;
                    for(var i=1;i<=size;i++){
                         htmlVardate += "<option id='dateslectWeek"+i+"' value='"+i+"'>"+i+"</option>";
                    }
                      htmlVardate += "</select></span>";

                  $("#apTimeSelect" + id).html(htmlVardate);


                $("#apTimeSelect" + id).append("" +
                        "<span id='spanmonth' style='float:left;width:39%'>" +
                        "<select id='selectMonth" + id + "' class='gFontFamily ' style='width:90%;height:30px;padding:5px 0px 5px 14px;margin-left:12px;'>" +
            <% if(month!=null && month!=""){%>
                // "<option id='selectMonth31' class='gFontFamily  pdd1'><%=month%></option>"+<%}%>
                "<option id='selectMonth31' class='gFontFamily  pdd1'>January</option>" +
                        "<option id='selectMonth32' class='gFontFamily  pdd1'>February</option>" +
                        "<option id='selectMonth33' class='gFontFamily  pdd1'>March</option>" +
                        "<option id='selectMonth34' class='gFontFamily  pdd1'>April</option>" +
                        "<option id='selectMonth35' class='gFontFamily  pdd1'>May</option>" +
                        "<option id='selectMonth36' class='gFontFamily  pdd1'>June</option>" +
                        "<option id='selectMonth37' class='gFontFamily  pdd1'>July</option>" +
                        "<option id='selectMonth38' class='gFontFamily  pdd1'>August</option>" +
                        "<option id='selectMonth39' class='gFontFamily  pdd1'>September</option>" +
                        "<option id='selectMonth310' class='gFontFamily  pdd1'>October</option>" +
                        "<option id='selectMonth311' class='gFontFamily  pdd1'>November</option>" +
                        "<option id='selectMonth312' class='gFontFamily  pdd1'>December</option>" +
                        "</select></span>" +
                        "<span id='spanyear' style='float:left;width:40%'>" +
                        "<select id='selectYear" + id + "' class='gFontFamily  pdd1' style='width:90%;height:30px;padding:5px 0px 5px 25px;margin-right:12px;float:right'>" +
                        //"<option id='selectYear32010' class='gFontFamily  pdd1' selected value=<%=year%> ><%=year%></option>"+
                        "<option id='selectYear32010' class='gFontFamily  pdd1'>2010</option>" +
                        "<option id='selectYear32011' class='gFontFamily  pdd1'>2011</option>" +
                        "<option id='selectYear32012' class='gFontFamily  pdd1'>2012</option>" +
                        "<option id='selectYear32013' class='gFontFamily  pdd1'>2013</option>" +
                        "<option id='selectYear32014' class='gFontFamily  pdd1'>2014</option>" +
                        "<option id='selectYear32015' class='gFontFamily  pdd1'>2015</option>" +
                        "<option id='selectYear32016' class='gFontFamily  pdd1'>2016</option>" +
                        "<option id='selectYear32017' class='gFontFamily  pdd1'>2017</option>" +
                        "<option id='selectYear32018' class='gFontFamily  pdd1'>2018</option>" +
                        "<option id='selectYear32019' class='gFontFamily  pdd1'>2019</option>" +
                        "<option id='selectYear32020' class='gFontFamily  pdd1'>2020</option>" +
                        "</select></span>"
                );
                        $("#selectMonth3" +<%=mon1%>).prop("selected", "true");
                $("#selectYear3" +<%=year%>).prop("selected", "true");
                $("#dateslectWeek" +<%=date%>).prop("selected", "true");

                var htmlVar = "";
            <% if(compare!=null && compare.equalsIgnoreCase("WOW")){%>
    parent.$('#CBO_PRG_COMPARE').val("Last Week");parent.$('#gCompq').val("Last Week");
                        htmlVar += "<option class='gFontFamily ' selected>WOW</option>";
                htmlVar += "<option class='gFontFamily ' >WOY</option>";
                 
            <%}else if(compare!=null && compare.equalsIgnoreCase("WOY")){%>
                parent.$('#CBO_PRG_COMPARE').val("Same Week Last Year");parent.$('#gCompq').val("Same Week Last Year");
                htmlVar += "<option class='gFontFamily ' selected >WOY</option>";
                htmlVar += "<option class='gFontFamily '>WOW</option>";
              
                   
        <%}else {%>
    parent.$('#CBO_PRG_COMPARE').val("Last Month");parent.$('#gCompq').val("Last Month");
    htmlVar += "<option class='gFontFamily ' selected>WOW</option>";
                htmlVar += "<option class='gFontFamily ' >WOY</option>";
            <%}%>parent.$('#CBO_PRG_COMPARE').val("Same Month Last Year");parent.$('#gCompq').val("Same Month Last Year");

                $("#compSelect3").html("");
                $("#compSelect3").html(htmlVar);
                $('#selectMonth' + id + ',#selectYear' + id + ',#compSelect' + id).select2({
                    minimumResultsForSearch: Infinity
                });
                 $("#DatedropGO").css("margin-left", "25%");
                $("#dateslectWeek").select2({minimumResultsForSearch: Infinity});
            }
            function appendDateSelect1(id) {
 parent.$("#optMonthly3").css("checked", "true");
                parent.$("#optQuarterly3").css("checked", "false");
                parent.$("#optYearly3").css("checked", "false");
                 parent.$("#optDaily3").css("checked", "false");
                $(".customRadio" + id).css("background-color", "#fff");
                $("#optMonthly" + id).prev().css("background-color", "#8BC34A");
//                $("#timeMonth"+id).show();
                $("#apTimeSelect" + id).html("");
                var htmlVardate = "";
                 htmlVardate += "<span id='comparefromDTToDate' style='display:block;width:21%;float:left;'>";
                    htmlVardate += "<select id='dateslectmotnh' style='width:90%'>";
                   var size=31;
                    for(var i=1;i<=size;i++){
                         htmlVardate += "<option id='dateslectmotnh"+i+"' value='"+i+"'>"+i+"</option>";
                    }
                      htmlVardate += "</select></span>";
if(userflag!="" && userflag=="veraction"){}else{
                  $("#apTimeSelect" + id).html(htmlVardate);}

                $("#apTimeSelect" + id).append("" +
                        "<span id='spanmonth' style='float:left;width:39%'>" +
                        "<select id='selectMonth" + id + "' class='gFontFamily ' style='width:90%;height:30px;padding:5px 0px 5px 14px;margin-left:12px;' onchange='selectDateTo(\"" + 'dateslectmotnh' + "\")'>" +
            <% if(month!=null && month!=""){%>
                // "<option id='selectMonth31' class='gFontFamily  pdd1'><%=month%></option>"+<%}%>
                "<option id='selectMonth31' class='gFontFamily  pdd1'>January</option>" +
                        "<option id='selectMonth32' class='gFontFamily  pdd1'>February</option>" +
                        "<option id='selectMonth33' class='gFontFamily  pdd1'>March</option>" +
                        "<option id='selectMonth34' class='gFontFamily  pdd1'>April</option>" +
                        "<option id='selectMonth35' class='gFontFamily  pdd1'>May</option>" +
                        "<option id='selectMonth36' class='gFontFamily  pdd1'>June</option>" +
                        "<option id='selectMonth37' class='gFontFamily  pdd1'>July</option>" +
                        "<option id='selectMonth38' class='gFontFamily  pdd1'>August</option>" +
                        "<option id='selectMonth39' class='gFontFamily  pdd1'>September</option>" +
                        "<option id='selectMonth310' class='gFontFamily  pdd1'>October</option>" +
                        "<option id='selectMonth311' class='gFontFamily  pdd1'>November</option>" +
                        "<option id='selectMonth312' class='gFontFamily  pdd1'>December</option>" +
                        "</select></span>" +
                        "<span id='spanyear' style='float:left;width:40%'>" +
                        "<select id='selectYear" + id + "' class='gFontFamily  pdd1' style='width:90%;height:30px;padding:5px 0px 5px 25px;margin-right:12px;float:right'>" +
                        //"<option id='selectYear32010' class='gFontFamily  pdd1' selected value=<%=year%> ><%=year%></option>"+
                        "<option id='selectYear32010' class='gFontFamily  pdd1'>2010</option>" +
                        "<option id='selectYear32011' class='gFontFamily  pdd1'>2011</option>" +
                        "<option id='selectYear32012' class='gFontFamily  pdd1'>2012</option>" +
                        "<option id='selectYear32013' class='gFontFamily  pdd1'>2013</option>" +
                        "<option id='selectYear32014' class='gFontFamily  pdd1'>2014</option>" +
                        "<option id='selectYear32015' class='gFontFamily  pdd1'>2015</option>" +
                        "<option id='selectYear32016' class='gFontFamily  pdd1'>2016</option>" +
                        "<option id='selectYear32017' class='gFontFamily  pdd1'>2017</option>" +
                         "<option id='selectYear32018' class='gFontFamily  pdd1'>2018</option>" +
                          "<option id='selectYear32019' class='gFontFamily  pdd1'>2019</option>" +
                           "<option id='selectYear32020' class='gFontFamily  pdd1'>2020</option>" +
                        "</select></span>"
                );
                        $("#selectMonth3" +<%=mon1%>).prop("selected", "true");
                $("#selectYear3" +<%=year%>).prop("selected", "true");
                $("#dateslectmotnh" +<%=date%>).prop("selected", "true");

                var htmlVar = "";
            <% if(compare!=null && compare.equalsIgnoreCase("MOM")){%>
    parent.$('#CBO_PRG_COMPARE').val("Last Month");parent.$('#gCompq').val("Last Month");
                        htmlVar += "<option class='gFontFamily ' selected>MOM</option>";
                        htmlVar += "<option class='gFontFamily ' >MOY</option>";
                       htmlVar += "<option class='gFontFamily '>CMOM</option>";
                      htmlVar += "<option class='gFontFamily '>CMOY</option>";
<%}else if(compare!=null && compare.equalsIgnoreCase("MOY")){%>
            parent.$('#CBO_PRG_COMPARE').val("Same Month Last Year");parent.$('#gCompq').val("Same Month Last Year");

                htmlVar += "<option class='gFontFamily ' selected >MOY</option>";
                htmlVar += "<option class='gFontFamily '>MOM</option>";
                    htmlVar += "<option class='gFontFamily '>CMOM</option>";
                      htmlVar += "<option class='gFontFamily '>CMOY</option>";
      <%}else if(compare!=null && compare.equalsIgnoreCase("CMOM")){%>
             parent.$('#CBO_PRG_COMPARE').val("Complete Last Month");parent.$('#gCompq').val("Complete Last Month");
                htmlVar += "<option class='gFontFamily ' selected >CMOM</option>";
                htmlVar += "<option class='gFontFamily '>MOM</option>";
                htmlVar += "<option class='gFontFamily '>MOY</option>";
                htmlVar += "<option class='gFontFamily '>CMOY</option>";
         <%}else if(compare!=null && compare.equalsIgnoreCase("CMOY")){%>
                parent.$('#CBO_PRG_COMPARE').val("Complete Same Month Last Year");parent.$('#gCompq').val("Complete Same Month Last Year");
                htmlVar += "<option class='gFontFamily ' selected >CMOY</option>";
                htmlVar += "<option class='gFontFamily '>MOM</option>";
                htmlVar += "<option class='gFontFamily '>MOY</option>";
                htmlVar += "<option class='gFontFamily '>CMOM</option>";
        <%}else {%>
    parent.$('#CBO_PRG_COMPARE').val("Last Month");parent.$('#gCompq').val("Last Month"); htmlVar += "<option class='gFontFamily ' selected>MOM</option>";
                htmlVar += "<option class='gFontFamily ' >MOY</option>";
                 if(userflag!="" && userflag=="veraction"){}else{
                 htmlVar += "<option class='gFontFamily '>CMOM</option>";
                htmlVar += "<option class='gFontFamily '>CMOY</option>";}
            <%}%>parent.$('#CBO_PRG_COMPARE').val("Same Month Last Year");parent.$('#gCompq').val("Same Month Last Year");

                $("#compSelect3").html("");
                $("#compSelect3").html(htmlVar);
                $('#selectMonth' + id + ',#selectYear' + id + ',#compSelect' + id).select2({
                    minimumResultsForSearch: Infinity
                });
                if(userflag!="" && userflag=="veraction"){
                            $("#spanmonth").css("width", "50%");$("#comparitiveSpan").css("margin-top", "8px");
                            $("#spanyear").css("width", "50%");$("#DatedropGO").css("margin-left", "0px");
                }else{
                 $("#DatedropGO").css("margin-left", "25%");}
                   $("#dateslectmotnh").select2({minimumResultsForSearch: Infinity});
            }
            function appendDateSelect2(id) {
                parent.$("#optQuarterly3").css("checked", "true");
                parent.$("#optMonthly3").css("checked", "false");
                parent.$("#optYearly3").css("checked", "false");
                 parent.$("#optDaily3").css("checked", "false");
                $(".customRadio" + id).css("background-color", "#fff");
                $("#optQuarterly" + id).prev().css("background-color", "#8BC34A");
                $("#apTimeSelect" + id).html("");
                $("#apTimeSelect" + id).append("<span style='float:left;width:50%'>" +
                        "<select id='selectQuater" + id + "' class='gFontFamily ' style='width:90%;height:30px;padding:5px 0px 5px 14px;margin-left:12px;'>" +
            <% if(qtr!=null && qtr!=""){%>
                // "<option id='selectQuater31' class='gFontFamily  pdd1' selected value=<%=qtr%> ><%=qtr%></option>"+<%}%>
                "<option id='selectQuater3Q1' class='gFontFamily  pdd1'>Q1</option>" +
                        "<option id='selectQuater3Q2' class='gFontFamily  pdd1'>Q2</option>" +
                        "<option id='selectQuater3Q3' class='gFontFamily  pdd1'>Q3</option>" +
                        "<option id='selectQuater3Q4' class='gFontFamily  pdd1'>Q4</option>" +
                        "</select></span>" +
                        "<span style='float:left;width:50%'>" +
                        "<select id='selectYear" + id + "' class='gFontFamily  pdd1' style='width:90%;height:30px;padding:5px 0px 5px 25px;margin-right:12px;float:right'>" +
                        // "<option id='selectYear32010' class='gFontFamily  pdd1' selected value=<%=year%> ><%=year%></option>"+
                        "<option id='selectYear32010' class='gFontFamily  pdd1'>2010</option>" +
                        "<option id='selectYear32011' class='gFontFamily  pdd1'>2011</option>" +
                        "<option id='selectYear32012' class='gFontFamily  pdd1'>2012</option>" +
                        "<option id='selectYear32013' class='gFontFamily  pdd1'>2013</option>" +
                        "<option id='selectYear32014' class='gFontFamily  pdd1'>2014</option>" +
                        "<option id='selectYear32015' class='gFontFamily  pdd1'>2015</option>" +
                        "<option id='selectYear32016' class='gFontFamily  pdd1'>2016</option>" +
                        "<option id='selectYear32017' class='gFontFamily  pdd1'>2017</option>" +
                        "<option id='selectYear32018' class='gFontFamily  pdd1'>2018</option>" +
                        "<option id='selectYear32019' class='gFontFamily  pdd1'>2019</option>" +
                        "<option id='selectYear32020' class='gFontFamily  pdd1'>2020</option>" +
                        
                        "</select></span>"
                );
                        $("#selectQuater3" + '<%=qtr%>').prop("selected", "true");
                $("#selectYear3" + '<%=year%>').prop("selected", "true");
                var htmlVar = "";
            <% if(compare!=null && compare.equalsIgnoreCase("QOQ")){%>parent.$('#CBO_PRG_COMPARE').val("Last Qtr");parent.$('#gCompq').val("Last Qtr");
                    htmlVar += "<option class='gFontFamily 'selected >QOQ</option>";
                    htmlVar += "<option class='gFontFamily ' >QOY</option>";
                    htmlVar += "<option class='gFontFamily ' >CQOQ</option>";
                    htmlVar += "<option class='gFontFamily ' >CQOY</option>";
    <%}else if(compare!=null && compare.equalsIgnoreCase("QOY")){%>
    parent.$('#CBO_PRG_COMPARE').val("Same Qtr Last Year");parent.$('#gCompq').val("Same Qtr Last Year");
                            htmlVar += "<option class='gFontFamily ' selected>QOY</option>";
                    htmlVar += "<option class='gFontFamily ' >QOQ</option>";
                     htmlVar += "<option class='gFontFamily ' >CQOQ</option>";
                    htmlVar += "<option class='gFontFamily ' >CQOY</option>";
                    <%}else if(compare!=null && compare.equalsIgnoreCase("CQOQ")){%>
    parent.$('#CBO_PRG_COMPARE').val("Complete Last Qtr");parent.$('#gCompq').val("Complete Last Qtr");
                            htmlVar += "<option class='gFontFamily ' selected>CQOQ</option>";
                    htmlVar += "<option class='gFontFamily ' >QOY</option>";
                     htmlVar += "<option class='gFontFamily ' >QOQ</option>";
                    htmlVar += "<option class='gFontFamily ' >CQOY</option>";
            <%}else if(compare!=null && compare.equalsIgnoreCase("CQOY")){%>
    parent.$('#CBO_PRG_COMPARE').val("Complete Same Qtr Last Year");parent.$('#gCompq').val("Complete Same Qtr Last Year");
                            htmlVar += "<option class='gFontFamily ' selected>CQOY</option>";
                    htmlVar += "<option class='gFontFamily ' >QOY</option>";
                     htmlVar += "<option class='gFontFamily ' >QOQ</option>";
                    htmlVar += "<option class='gFontFamily ' >CQOQ</option>";

            <%}else{%>
    parent.$('#CBO_PRG_COMPARE').val("Last Qtr");parent.$('#gCompq').val("Last Qtr");
    htmlVar += "<option class='gFontFamily 'selected >QOQ</option>";
                    htmlVar += "<option class='gFontFamily ' >QOY</option>";
                     if(userflag!="" && userflag=="veraction"){}else{
                     htmlVar += "<option class='gFontFamily ' >CQOQ</option>";
                    htmlVar += "<option class='gFontFamily ' >CQOY</option>";}
    <%}%>

                    $("#compSelect3").html("");
                    $("#compSelect3").html(htmlVar);
                    $('#selectQuater' + id + ',#selectYear' + id + ',#compSelect' + id).select2({
                        minimumResultsForSearch: Infinity
                    });
                    if(userflag!="" && userflag=="veraction"){
                        $("#DatedropGO").css("margin-left", "0px");$("#comparitiveSpan").css("margin-top", "5px");
                    }else{
                     $("#DatedropGO").css("margin-left", "25%");}
                }
                function appendDateSelect3(id) {
                    parent.$("#optQuarterly3").css("checked", "false");
                    parent.$("#optMonthly3").css("checked", "false");
                    parent.$("#optYearly3").css("checked", "true");
                     parent.$("#optDaily3").css("checked", "false");
                    $(".customRadio" + id).css("background-color", "#fff");
                    $("#optYearly" + id).prev().css("background-color", "#8BC34A");
//                $("#timeMonth"+id).hide();
                    $("#apTimeSelect" + id).html("");
                    $("#apTimeSelect" + id).append("<span style='float:left;width:50%;margin-left:0%;margin-top:5px;'>" +
                            "<select id='selectYear" + id + "' class='gFontFamily  pdd1' style='width:90%;height:30px;padding:5px 0px 5px 25px;margin-right:12px;float:right'>" +
                            // "<option id='selectYear32010' class='gFontFamily  pdd1' selected value=<%=year%> ><%=year%></option>"+
                            "<option id='selectYear32010' class='gFontFamily  pdd1'>2010</option>" +
                            "<option id='selectYear32011' class='gFontFamily  pdd1'>2011</option>" +
                            "<option id='selectYear32012' class='gFontFamily  pdd1'>2012</option>" +
                            "<option id='selectYear32013' class='gFontFamily  pdd1'>2013</option>" +
                            "<option id='selectYear32014' class='gFontFamily  pdd1'>2014</option>" +
                            "<option id='selectYear32015' class='gFontFamily  pdd1'>2015</option>" +
                            "<option id='selectYear32016' class='gFontFamily  pdd1'>2016</option>" +
                            "<option id='selectYear32017' class='gFontFamily  pdd1'>2017</option>" +
                              "<option id='selectYear32018' class='gFontFamily  pdd1'>2018</option>" +
                                "<option id='selectYear32019' class='gFontFamily  pdd1'>2019</option>" +
                                  "<option id='selectYear32020' class='gFontFamily  pdd1'>2020</option>" +
                            "</select></span>");
                    $("#selectYear3" + '<%=year%>').prop("selected", "true");
                    var htmlVar = "";
                    htmlVar += "<option class='gFontFamily ' selected value='YOY'>YOY</option>";
                     if(userflag!="" && userflag=="veraction"){}else{
                     htmlVar += "<option class='gFontFamily ' selected value='YOY'>CYOY</option>";}
                    $("#compSelect3").html("");
                    $("#compSelect3").html(htmlVar);
                    $('#selectYear' + id + ',#compSelect' + id).select2({
                        minimumResultsForSearch: Infinity
                    });
                       if(userflag!="" && userflag=="veraction"){
                         $("#DatedropGO").css("margin-left", "55px");  $("#comparitiveSpan").css("margin-top", "5px");
                       }else{
                       
                     $("#DatedropGO").css("margin-left", "5px");}
                    var gComp = $("#select2-compSelect" + id + "-container").text();
//                                                if(gComp=='YOY'){
                    parent.$('#CBO_PRG_COMPARE').val("Last Year");parent.$('#gCompq').val("Last Year");
//              }

                }

                 function enableCompar(eid)

                {
                    var flage="";
                    if($("#"+eid).val()!="show"){

                         $("#forcompare").show();
                           $("#comparefromDTToDate11").show();  $("#hidetime14").show();  $("#selFrmComMon111").show();  $("#selFrmComYr11").show();
                        $("#comparetoDTToDate11").show();
                         $("#hidedata15").show();  $("#selToComMon111").show();  $("#selToComYr11").show();  $("#goiddate").show();
                        $("#selfrom121").show();
                          $("#seltocom111").show();
                            $("#hidedata15").show();
                             $("#rangeDtclear1").css({
                                            height:"110px"
                                            });
                                            $('#enabledetacomradio').attr('checked', true)

                                             $("#"+eid).val("show");

                                              parent.$("#compDate").val("false");
                                              flage=true;
                    }else{
$('#rangeDtclear').attr('id','rangeDtclear1');
                         $("#forcompare").hide();
 $("#comparefromDTToDate11").hide();  $("#hidetime14").hide();  $("#selFrmComMon111").hide();  $("#selFrmComYr11").hide();
                        $("#comparetoDTToDate11").hide();
                         $("#hidedata15").hide();  $("#selToComMon111").hide();  $("#selToComYr11").hide();  $("#goiddate").hide();
                        $("#selfrom121").hide();
                          $("#seltocom111").hide();
                            $("#hidedata15").hide();
                             $("#rangeDtclear1").css({
                                            height:"62px"
                                            });
                                         $("#"+eid).val("show1");
                                            $("#enablecomdata").html("Enable Comparison")
                                             $('#enabledetacomradio').attr('checked', false)
                                             parent.$("#compDate").val("true");
                                              flage=false;

                    }
                    $.ajax({
        type:'POST',
        async: false,
  url:"<%=request.getContextPath()%>/reportTemplateAction.do?templateParam=changeCompDate&reportId="+'<%=reportId1%>'+"&flageValue="+flage,
        success: function(data){

        }
    })
                }
                 function selectDateTo(id){
                   var selectedmonth1 = $("#select2-selectMonth3-container").text(); 
                 var selectedyear1 = $("#select2-selectYear3-container").text();
                    var x = $("#select2-selFrmDatMon-container").title;
                   var size=31;
                   if(selectedmonth1=='December'||selectedmonth1=='October'||selectedmonth1=='August'||selectedmonth1=='July'||selectedmonth1=='May'||selectedmonth1=='March'||selectedmonth1=='January')
                       {
                             $("#select2-dateslectmotnh-container").text("31");
                           size=31;
                       }
                     else if(selectedmonth1=='April'||selectedmonth1=='June'||selectedmonth1=='September'||selectedmonth1=='November')
                           {
                                $("#select2-dateslectmotnh-container").text("30");
                              size=30;
                           }
                   else if(selectedmonth1=='February')   
                          {
                          var isLeap = new Date(selectedyear1, 1, 29).getMonth() == 1
                        
                            if(isLeap)
                          {
                                 $("#select2-dateslectmotnh-container").text("29");
                               size=29;
                          }
                          else
                              {
                                   $("#select2-dateslectmotnh-container").text("28");
                                    size=28;
                              } }
                        else
                            {}
                         var htmlVardatef = "";
                  for(var i=1;i<=size;i++){
                         htmlVardatef += "<option id='dateslectmotnh"+i+"' value='"+i+"'>"+i+"</option>";
                    }
                    
                  $("#"+id).html("");
                  $("#"+id).append(htmlVardatef);$("#dateslectmotnh" +size).prop("selected", "true");
                 }  
                function enableDate()
                {
enabledate=true
                     $("body").on("click", "#htime1", function() {
                       document.getElementById('enabledatashow').checked = false;
                        $("#htime1").next().css("margin-left", ($(this).parent().width() - 490) / 2);


                    });
                    $('#dateenable').val("true")
                    $("#hidedate11").show();
                    $("#hidedate12").show();$("#hidetime14").show();
                    $("#hidedata15").show();

                     $("#enabledatashowrad").hide();
                    $("#enabledataradio").show();
                     $("#enabledatashow").hide();
                      $("#enabledata").show();
                    $("#selFrmDatToDate11").show();
                    $("#comparefromDTToDate11").show();

                    $("#selToDatToDate11").show();
                   $("#comparetoDTToDate11").show();
                     $("#rangeDtclear").css({

                                                width:"490px",
                                                display:"none",
                                               position:"absolute",
                                               height:"110px",
                                               "margin-left":"-68px"

                                            });

                                            $("#selFrmDatMon111").css({ width:"20%"  });
                     $("#selToDatMon111").css({ width:"20%"  });
                      $("#selFrmComMon111").css({ width:"20%"  });
                      $("#selToComMon111").css({ width:"20%"  });

$("#selFrmDatYr11").css({ width:"14%"  });
$("#selToDatYr11").css({ width:"14%"  });
$("#selFrmComYr11").css({ width:"14%"  });
$("#selToComYr11").css({ width:"14%"  });

$("#selFrmDatToDate11").css({ width:"10%"  });
$("#comparefromDTToDate11").css({ width:"10%"  });
$("#selToDatToDate11").css({ width:"10%"  });
$("#comparefromDTToDate11").css({ width:"10%"  });
                }

                function disableDate()
                {
                  enabledate=false
                  document.getElementById('enabledataradio').checked = false;
                    $("body").on("click", "#htime1", function() {

                        $("#htime1").next().css("margin-left", ($(this).parent().width() - 260) / 2);


                    });
$('#dateenable').val("false")
                    $("#enabledatashow").show();
                    $("#enabledata").hide();

                    $("#enabledata").hide();

                     $("#enabledatashowrad").show();
                    $("#enabledataradio").hide();
                    $("#hidedate11").hide();
                 $("#hidedate12").hide();
                 $("#hidetime14").hide();
                 $("#hidedata15").hide();
                 $("#selFrmDatToDate11").hide();
                 $("#selToDatToDate11").hide();
                 $("#comparefromDTToDate11").hide();
                  $("#comparetoDTToDate11").hide();
               $("#enabledatashowrad").css({ margin:"-24px 82%"})
                $("#enabledatashow").css({ margin:"-27px 71%",
                                                width:"25%"})

                      $("#rangeDtclear").css({

                                                width:"375px",
                                                display:"none",
                                               position:"absolute",
                                               height:"110px",
                                               "margin-left":"-68px"

                                            });
                      $("#rangeDtclear").css({"margin-left":"-68px"})

                   $("#selFrmDatMon111").css({ width:"27%"  });
                     $("#selToDatMon111").css({ width:"27%"  });
                      $("#selFrmComMon111").css({ width:"27%"  });
                      $("#selToComMon111").css({ width:"27%"  });

$("#selFrmDatYr11").css({ width:"19%"  });
$("#selToDatYr11").css({ width:"19%"  });
$("#selFrmComYr11").css({ width:"19%"  });
$("#selToComYr11").css({ width:"19%"  });

                }
                function comparitiveMethod(id){
                  if(document.getElementById("comparivitiveButton").checked==true){
                        $("#comparitiveSpan").css({'display': 'block'});
                        $("#DatedropGO").css({'margin-left':'25%'});
                        $("#DatedropGO").css({'margin-top':'8px'});
                        $("#dateCompareSpan").css({'padding-left':'45px'});
                        $("#normalcompspan").css({'top':'13px'});
                        $("#dateDiv").css({'height':'117px'});
                    }else if(document.getElementById("comparivitiveButton").checked==false){
                        $("#comparitiveSpan").css({'display': 'none'});
                        $("#DatedropGO").css({'margin-left':'0%'});
                        $("#DatedropGO").css({'margin-top':'5px'});
                        $("#dateCompareSpan").css({'padding-left':'95px'});
                        $("#normalcompspan").css({'top':'-25px'});
                        $("#dateDiv").css({'height':'92px'});
                    var selectedmonthval = $("#Datetype").val();
                    if(selectedmonthval=='Yearly'){
                    if(parent.$("#AOId").val()!=null && parent.$("#AOId").val()!=""  &&'<%=timeinfo.get(1)%>' == "PRG_STD"){
                     $("#normalcompspan").css({'top':'5px'}); 
                     $("#DatedropGO").css({'margin-left':'3%'});
                    }
                }
                    }
                }
                function setTime(id) {
            <%if (timeinfo.get(1).equalsIgnoreCase("PRG_DATE_RANGE")){%>
                    $("#timeMonth1").text($("#select2-selFrmDatMon-container").text() + ",");
                    $("#timeYear1").text($("#select2-selFrmDatYr-container").text());
                    $("#timeMonth2").text($("#select2-selToDatMon-container").text() + ",");
                    $("#timeYear2").text($("#select2-selToDatYr-container").text());
                    $("#htime1").next().slideUp("fast");
                    dateConverstionRange('null');
            <%}else{%>
                    if ($("#select2-selectMonth3-container").is(":visible")) {
                        $("#timeMonth3").text($("#select2-selectMonth3-container").text() + ",");
                        $("#timeYear3").text($("#select2-selectYear3-container").text());
                    }
                    else if ($("#select2-selectQuater3-container").is(":visible")) {
                        $("#timeMonth3").text($("#select2-selectQuater3-container").text() + ",");
                        $("#timeYear3").text($("#select2-selectYear3-container").text());
                    }
                    else {
                        $("#timeMonth3").text("");
                        $("#timeYear3").text($("#select2-selectYear3-container").text());
                    }
                    dateConverstion(id, 'null');
            <%}%>
                }
                function appendRepCtrlDiv(tabName) {
                    var istoggledate1 = "";
            <%if((container.datetoggl).equalsIgnoreCase("")){%>
//                       istoggledate1="No";
            <%}else{%>
                    istoggledate1 =<%=container.datetoggl%>;
            <%}%>
                    var st = [];
                    st.push('<%=request.getContextPath()%>');
                    st.push('<%=sbufRoles.toString().substring(1)%>');
                    st.push('<%=reportId1%>');
                    st.push('<%=reportDrill%>');
                    st.push('<%=USERID%>');
                    st.push('<%=rolesid%>');
                    st.push('<%=reportName%>');
                    var htmlVar = "";
                    var drillType = "multi";
                    if (tabName == "Trend") {
                        htmlVar += "<li ><a data-color='color1' class='gFontFamily ' id ='addGraphDataTrendAnalysis' onclick='buildChartsInTrends(parent.$('#graphsId').val())' ><%=TranslaterHelper.getTranslatedInLocale("Add_Trend_Charts", cL)%></a></li>";
           <% if(((insightsUserRole.equalsIgnoreCase(""))&&(userType.equalsIgnoreCase("ADMIN"))) || ((!insightsUserRole.equalsIgnoreCase(""))&&(insightsUserRole.equalsIgnoreCase("VPA")))) {%>
                        htmlVar += "<li ><a data-color='color1' class='gFontFamily ' onclick='saveXtCharts()' ><%=TranslaterHelper.getTranslatedInLocale("Save_Trend_Charts", cL)%></a></li>";
            <% }%>
                        htmlVar += "<li><a data-color='color1' class='gFontFamily '  onclick='OpenDefTabs()'><%=TranslaterHelper.getTranslatedInLocale("Default_Tab", cL)%></a></li>";
                        htmlVar += "<li ><a data-color='color1' class='gFontFamily ' onclick='trendAnalysisActionJsNew(parent.$('#graphsId').val())' ><%=TranslaterHelper.getTranslatedInLocale("Reset", cL)%></a></li>";
                    } else if (tabName == "Dashboard") {
                        htmlVar +="<li title='Change To Management Tempalate' ><a data-color='color1' class='gFontFamily ' onclick='addMgmtTemp()' >Management Template </span></a></li>";
                        htmlVar +="<li title='Edit Management Template' ><a data-color='color1' class='gFontFamily ' onclick='editMgmtTemplate()' >Edit Management Template</span></a></li>";
              <% if(((insightsUserRole.equalsIgnoreCase(""))&&(userType.equalsIgnoreCase("ADMIN"))) || ((!insightsUserRole.equalsIgnoreCase(""))&&(insightsUserRole.equalsIgnoreCase("VPA")))) {%>
                        htmlVar += "<li title='Add new graph Objects by customizing ViewBys' ><a data-color='color1' class='gFontFamily ' onclick='editSingleCharts(\"add1\")' ><%=TranslaterHelper.getTranslatedInLocale("Add_Graph_Object", cL)%></a></li>";
                        htmlVar += "<li title='Edit the existing graph Objects by deleting or adding new ViewBys or Measures'><a data-color='color1' class='gFontFamily ' onclick='editSingleCharts(\"edit\")' ><%=TranslaterHelper.getTranslatedInLocale("Edit_Graph_Object", cL)%></a></li>";
                        htmlVar += "<li title='Add AO Object'><a data-color='color1' class='gFontFamily ' onclick='addAOObject(\"<%=collect.reportBizRoles[0]%>\")' ><%=TranslaterHelper.getTranslatedInLocale("Add_AO_Object", cL)%></a></li>";
                        htmlVar += "<li title='Update AO Object'><a data-color='color1' class='gFontFamily ' onclick='updateAOObject()' ><%=TranslaterHelper.getTranslatedInLocale("UP_AO_Object", cL)%></a></li>";

            <% }%>
                        htmlVar += "<li title='Rename Attributes'><a data-color='color1' class='gFontFamily ' onclick='renameViewBys()' ><%=TranslaterHelper.getTranslatedInLocale("RENAME_ATTRIBUTES", cL)%></a></li>";
                        htmlVar += "<li title='Add Page'><a data-color='color1' class='gFontFamily ' onclick='addPage()' >Add Page</a></li>";
                        htmlVar += "<li ><a data-color='color1' class='gFontFamily ' onclick='removePage(\"local\")' ><%=TranslaterHelper.getTranslatedInLocale("Remove_Page_Local", cL)%></a></li>";
                        htmlVar += "<li ><a data-color='color1' class='gFontFamily ' onclick='removePage(\"global\")' ><%=TranslaterHelper.getTranslatedInLocale("Remove_Page_Global", cL)%></a></li>";
                        htmlVar += "<li title='Rename Page'><a data-color='color1' class='gFontFamily ' onclick='renamePage()' ><%=TranslaterHelper.getTranslatedInLocale("Rename_Page", cL)%></a></li>";
                        htmlVar += "<li title='Add Graph by customizing existing/new ViewBys or Measures'><a data-color='color1' class='gFontFamily ' onclick='buildCharts(\"add\")' ><%=TranslaterHelper.getTranslatedInLocale("Add_Graph", cL)%></a></li>";
                        htmlVar += "<li  title='Edit the existing graphs by deleting or adding new ViewBys or Measures'><a data-color='color1' class='gFontFamily ' onclick='buildCharts(\"isEdit\")' ><%=TranslaterHelper.getTranslatedInLocale("Edit_Graph", cL)%></a></li>";
             <% if(((insightsUserRole.equalsIgnoreCase(""))&&(userType.equalsIgnoreCase("ADMIN"))) || ((!insightsUserRole.equalsIgnoreCase(""))&&(insightsUserRole.equalsIgnoreCase("VPA")))) {%>
//                        htmlVar += "<li ><a data-color='color1' class='gFontFamily ' onclick='saveXtCharts()' ><%//=TranslaterHelper.getTranslatedInLocale("Save_All_Graphs", cL)%></a></li>";
						htmlVar += "<li title='Create a copy of existing Dashboards' id='saveGraphLi' style='text-align;'>";
                        htmlVar += "<a data-color='color1' onclick='openNewReportDtls(\"" + st[0] + "\")' >Save As New Dashboard</a>";
                        htmlVar += "</li>";
                        htmlVar+="<li title='Save a copy of report in Local and Global Repository'><a data-color='color1' class='gFontFamily ' onclick='publishGraphs(\"" + st[0] + "\",<%=isRestrictedPowerAnalyserEnable%>,isMultiCompany,<%=isPowerAnalyserEnableforUser%>)' >Publish Graphs</a></li>";
                        htmlVar += "<li title='Enable more than one drill in a page' style='text-align;'>";
                        htmlVar += "<a data-color='color1' class='gFontFamily ' onclick='selectMultiDrill(\"" + drillType + "\")' ><%=TranslaterHelper.getTranslatedInLocale("Enable_Multi_Drill", cL)%></a>";
                        htmlVar += "</li>";
//                         htmlVar += "<li><a data-color='color1' class='gFontFamily ' onclick='resetGraphs()'><%=TranslaterHelper.getTranslatedInLocale("Reset_Graphs", cL)%></a></li>";
                        htmlVar += "<li title='Choose default tab (Report/Graphs/AdvanceVisuals/Trends),Show Types(Name/Icons) and Show Icon(All/ Data Analysis/ Dashboard/Visual)'><a data-color='color1' class='gFontFamily ' onclick='OpenDefTabs()'><%=TranslaterHelper.getTranslatedInLocale("Default_Tab", cL)%></a></li>";
            <% }%>
                        htmlVar += "<li title='Resequence the filters'><a data-color='color1' class='gFontFamily '  onclick='resequenceGraphs1()'><%=TranslaterHelper.getTranslatedInLocale("Resequence_Param", cL)%></a></li>";
               <% if(((insightsUserRole.equalsIgnoreCase(""))&&(userType.equalsIgnoreCase("ADMIN"))) || ((!insightsUserRole.equalsIgnoreCase(""))&&(insightsUserRole.equalsIgnoreCase("VPA")))) {%>            
                        htmlVar += "<li title='Regenerate Filters'><a data-color='color1' class='gFontFamily '  onclick='regenerateFilters(" + parent.$("#graphsId").val() + ")' ><%=TranslaterHelper.getTranslatedInLocale("Regenerate_Filters", cL)%></a></li>";
                           <% }%> 
    htmlVar += "<li title='Add formula based new customized measureset'><a data-color='color1' class='gFontFamily '  onclick='addCustomMeasure(" + parent.$("#graphsId").val() + ")' ><%=TranslaterHelper.getTranslatedInLocale("Add_Custom_Measure", cL)%></a></li>";
                        htmlVar += "<li title='Reset the page/report'><a data-color='color1' class='gFontFamily '  onclick='generateJsonDataReset(" + parent.$("#graphsId").val() + ")' ><%=TranslaterHelper.getTranslatedInLocale("Reset", cL)%></a></li>";
                        htmlVar += "<li title='Toggle Date'><a id='toggleDateBtn' data-color='color1' class='gFontFamily '  onclick='datetoggl1(\"SUPERADMIN\",\"" +<%=isPowerAnalyserEnableforUser%> + "\",\"" + istoggledate1 + "\")' ><%=TranslaterHelper.getTranslatedInLocale("Toggle_Date", cL)%></a></li>";
                        //Added by Ashutosh for graphs save points
                        htmlVar+="<li title='Save graphs for the logged In User only'><a data-color='color1' class='gFontFamily ' onclick='savePointXtCharts()' >Save Local Graphs</a></li>";
                        htmlVar+="<li title='Load graphs from Global repository'><a data-color='color1' class='gFontFamily ' onclick='loadGlobalGraphs()' >Load GlobalGraphs</a></li>";

                    } else if (tabName == "Visual") {
                        htmlVar += "<li ><a data-color='color1' class='gFontFamily ' id ='addGraphDataTrendAnalysis' onclick='createAdvanceVisual(" + parent.$("#graphsId").val() + ")' ><%=TranslaterHelper.getTranslatedInLocale("Add_Visual", cL)%></a></li>";
                        htmlVar += "<li ><a data-color='color1' class='gFontFamily ' id ='addGraphAnalysis' onclick='editAdvanceVisual(" + parent.$("#graphsId").val() + ")' ><%=TranslaterHelper.getTranslatedInLocale("Edit_Visual", cL)%></a></li>";
           <% if(((insightsUserRole.equalsIgnoreCase(""))&&(!userType.equalsIgnoreCase("ANALYZER"))) || ((!insightsUserRole.equalsIgnoreCase(""))&&(!insightsUserRole.equalsIgnoreCase("A")))) {%>
                        htmlVar += "<li ><a data-color='color1' class='gFontFamily ' onclick='saveXtCharts()' ><%=TranslaterHelper.getTranslatedInLocale("Save_Visual", cL)%></a></li>";
                        htmlVar += "<li><a data-color='color1'  class='gFontFamily ' onclick='OpenDefTabs()'><%=TranslaterHelper.getTranslatedInLocale("Default_Tab", cL)%></a></li>";
            <% }%>
                        htmlVar += "<li ><a data-color='color1' class='gFontFamily ' onclick='getAdvanceVisuals(" + parent.$("#graphsId").val() + ")' ><%=TranslaterHelper.getTranslatedInLocale("Reset", cL)%></a></li>";
                        htmlVar += "<li ><a data-color='color1' class='gFontFamily ' onclick='setProperties(" + parent.$("#graphsId").val() + ")' ><%=TranslaterHelper.getTranslatedInLocale("Properties", cL)%></a></li>";
            <% if(((insightsUserRole.equalsIgnoreCase(""))&&(!userType.equalsIgnoreCase("ANALYZER"))) || ((!insightsUserRole.equalsIgnoreCase(""))&&(!insightsUserRole.equalsIgnoreCase("A")))) {%>
                        htmlVar += "<li ><a data-color='color1' class='gFontFamily ' onclick='resequenceGraphs()' ><%=TranslaterHelper.getTranslatedInLocale("Delete_Resequence", cL)%></a></li>";
            <% }%>
            <% if (true) {%>
                        htmlVar += "<li ><a data-color='color1' class='gFontFamily ' onclick='defineParentChildDB()' ><%=TranslaterHelper.getTranslatedInLocale("Define_Multi_Dashboard", cL)%></a></li>";
            <%}%>
                        htmlVar += "<li ><a id='toggleDateBtn' data-color='color1' class='gFontFamily '  onclick='datetoggl1(\"SUPERADMIN\",\"" +<%=isPowerAnalyserEnableforUser%> + "\",\"" + istoggledate1 + "\")' ><%=TranslaterHelper.getTranslatedInLocale("Toggle_Date", cL)%></a></li>";
                    } else {
                        htmlVar += "<li  title='Edit Table Data' ><a data-color='color1' class='gFontFamily ' onclick='dispChangeTableColumns(\"" + st[0] + "\"," + st[1] + "," + st[2] + ")' ><%=TranslaterHelper.getTranslatedInLocale("Edit_Table_Data", cL)%></a></li>";
                        htmlVar += "<li title='Edit Table Layout'><a data-color='color1' class='gFontFamily ' onclick='editViewBy()' ><%=TranslaterHelper.getTranslatedInLocale("Edit_Table_Layout", cL)%></a></li>";
                        htmlVar += "<li title='Measure As ViewBys'><a data-color='color1' class='gFontFamily ' onclick='editMeasureAsViewBy(\"" + st[0] + "\"," + st[1] + "," + st[2] + ")' ><%=TranslaterHelper.getTranslatedInLocale("Measures_As_ViewBys", cL)%></a></li>";
                        htmlVar += "<li title='Define Report Drill'><a data-color='color1' class='gFontFamily ' onclick='reportDrillAssignment(\"" + st[0] + "\"," + st[2] + ")' ><%=TranslaterHelper.getTranslatedInLocale("Define_Report_Drill", cL)%></a></li>";
                        htmlVar += "<li title='Define Custom Measures'><a data-color='color1' class='gFontFamily ' onclick='addCustomMeasure1(\"" + st[0] + "\"," + st[2] + ")' ><%=TranslaterHelper.getTranslatedInLocale("Define_Custom_Measure", cL)%></a></li>";
                         htmlVar += "<li title='Inititalize Report'><a data-color='color1' class='gFontFamily ' onclick='initilizeParameters(\"" + st[0] + "\"," + st[2] + ")' ><%=TranslaterHelper.getTranslatedInLocale("Initialize_Report", cL)%></a></li>";

            <%if (container.isReportCrosstab()) {%>
                        htmlVar += "<li title='CustomSequence'><a data-color='color1'  class='gFontFamily '  onclick='difineCustomSeq(" + st[1] + "," + st[2] + ")' ><%=TranslaterHelper.getTranslatedInLocale("Custom_Sequence", cL)%></a></li>";
            <%}%>
           <% if(((insightsUserRole.equalsIgnoreCase(""))&&(!userType.equalsIgnoreCase("ANALYZER"))) || ((!insightsUserRole.equalsIgnoreCase(""))&&(!insightsUserRole.equalsIgnoreCase("A")))) {%>
                        htmlVar += "<li title='Save As New Report'><a data-color='color1' class='gFontFamily ' onclick='openNewReportDtls(\"" + st[0] + "\")' ><%=TranslaterHelper.getTranslatedInLocale("Save_As_New_Report", cL)%></a></li>";
                        htmlVar += "<li title='Save/Overwrite Report'><a data-color='color1' class='gFontFamily ' onclick=overWriteReport1(\"" + st[0] + "\",'overWrite',<%=isRestrictedPowerAnalyserEnable%>,<%=isMultiCompany %>,<%=isPowerAnalyserEnableforUser%>) ><%=TranslaterHelper.getTranslatedInLocale("Save_Overwrite_Report", cL)%></a></li>";
                        htmlVar += "<li title='Save Table Data'><a data-color='color1' class='gFontFamily ' onclick='saveTableRegion(\"" + st[0] + "\"," + st[2] + ")' ><%=TranslaterHelper.getTranslatedInLocale("Save_Table_Data", cL)%></a></li>";
                        htmlVar += "<li title='Choose default tab (Report/Graphs/AdvanceVisuals/Trends),Show Types(Name/Icons) and Show Icon(All/ Data Analysis/ Dashboard/Visual)'><a data-color='color1' class='gFontFamily ' onclick='OpenDefTabs()'><%=TranslaterHelper.getTranslatedInLocale("Default_Tab", cL)%></a></li>";
            <% }%>
                          htmlVar += "<li title='Change Initialize Advance Parameter'><a data-color='color1' class='gFontFamily gFontSize12' onclick='parent.advanceParamter(\""+'<%=PbReportId%>'+"\",\"" + '<%=request.getContextPath()%>' + "\")'><%=TranslaterHelper.getTranslatedInLocale("Change_internalize_advanceParamter", cL)%></a></li>";
                        htmlVar += "<li title='Assign Report'><a data-color='color1' class='gFontFamily ' onclick='saveNewRepGrpTab1(\"" + st[0] + "\"," + st[4] + "," + st[5] + "," + st[2] + ",\"" + '<%=reportName%>' + "\")'><%=TranslaterHelper.getTranslatedInLocale("Assign_Report", cL)%></a></li>";
                        htmlVar += "<li title='Toggle Date'><a id='toggleDateBtn' data-color='color1' class='gFontFamily '  onclick='datetoggl1(\"SUPERADMIN\",\"" +<%=isPowerAnalyserEnableforUser%> + "\",\"" + istoggledate1 + "\")' ><%=TranslaterHelper.getTranslatedInLocale("Toggle_Date", cL)%></a></li>";
                        htmlVar += "<li title='Tag Report'><a  data-color='color1' class='gFontFamily gFontSize12'  onclick='saveReportTagName(\"" + st[0] + "\"," + st[2] + ")' ><%=TranslaterHelper.getTranslatedInLocale("Tag_Report", cL)%></a></li>";
                         htmlVar += "<li title='Create Local Save Point'><a data-color='color1' class='gFontFamily gFontSize12' onclick=\"getCollectandContainerObjectInfo('show')\"><%=TranslaterHelper.getTranslatedInLocale("Create_Local_Savepoint", cL)%></a></li>";
//                        htmlVar += "<li><a data-color='color1' class='gFontFamily gFontSize12' onclick=\"getCollectandContainerObjectInfo('change')\"><%=TranslaterHelper.getTranslatedInLocale("Change_Default_Savepoint", cL)%></a></li>"
						$("#pageList").css("width","40%"); 
					}

                    $("#optionMenuDiv").html("");
                    $("#optionMenuDiv").append(htmlVar);
                }
                var weekday = new Array(7);
                weekday[0] = "Sun";
                weekday[1] = "Mon";
                weekday[2] = "Tue";
                weekday[3] = "Wed";
                weekday[4] = "Thu";
                weekday[5] = "Fri";
                weekday[6] = "Sat";
                //sandeep
                function dateConverstionRange(time) {
                    var converteddate;
                    var fromdate = $('#fromdate').val();
                    var todate = $('#todate').val();
                    var comparefrom = $('#comparefrom').val();
                    var compareto = $('#compareto').val();
                     var date=$( "#selFrmDatToDate option:selected" ).val();
 if(userflag!="" && userflag=="veraction"){date="01";}
                    // alert(date)
                    var selectedmonth = $("#select2-selFrmDatMon-container").text();
                    var selectedmonthval = selectedmonth.substring(0, 3);
                    var selectedyear = $("#select2-selFrmDatYr-container").text();
                    var dateformat = selectedmonth + " " +date + "," + selectedyear;
                    var dateformat1 = selectedmonth + "/" + date + "/" + selectedyear;
                    var d = new Date(dateformat);
                    var d1 = new Date(dateformat1);
                    var day = weekday[d.getDay()];
                    fromdate = day + "," +date+"," + selectedmonthval + "," + selectedyear;
                    parent.$('#fromdate').val(fromdate);
                    var selectedmonth = $("#select2-selToDatMon-container").text();
                    var selectedmonthval = selectedmonth.substring(0, 3);
                    var selectedyear = $("#select2-selToDatYr-container").text();


                            var dateselected=$( "#selToDatToDate option:selected" ).val();
 if(userflag!="" && userflag=="veraction"){dateselected="01";}
                    var dateformat = selectedmonth + " " + dateselected + "," + selectedyear;
                    var dateformat1 = selectedmonth + "/" + dateselected + "/" + selectedyear;
                    var d = new Date(dateformat);
                    var d1 = new Date(dateformat1);
                    var day = weekday[d.getDay()];
                    var lastDay = new Date(d1.getFullYear(), d1.getMonth() + 1, 0);
                    var lastday = lastDay.toString().split(" ")[2];
                     if(userflag!="" && userflag=="veraction"){dateselected=lastday;}
                    todate = day + "," + dateselected + "," + selectedmonthval + "," + selectedyear;
                    parent.$('#todate').val(todate);
                    var selectedmonth = $("#select2-selFrmComMon-container").text();
                    var selectedmonthval = selectedmonth.substring(0, 3);
                    var comdata=$( "#comparefromDateToDate option:selected" ).val();
    if(userflag!="" && userflag=="veraction"){comdata="01";}
                    var selectedyear = $("#select2-selFrmComYr-container").text();
                    var dateformat = selectedmonth + " " + comdata + "," + selectedyear;
                    var dateformat1 = selectedmonth + "/" + comdata+ "/" + selectedyear;
                    var d = new Date(dateformat);
                    var d1 = new Date(dateformat1);
                    var day = weekday[d.getDay()];
                    comparefrom = day + "," +comdata+ "," + selectedmonthval + "," + selectedyear;

                    parent.$('#comparefrom').val(comparefrom);
                    var selectedmonth = $("#select2-selToComMon-container").text();
                    var selectedmonthval = selectedmonth.substring(0, 3);
                    var selectedyear = $("#select2-selToComYr-container").text();


                             var dateval=$( "#comparetoDateToDate option:selected" ).val();
 if(userflag!="" && userflag=="veraction"){dateval="01";}
                    var dateformat = selectedmonth + " " +dateval + "," + selectedyear;
                    var dateformat1 = selectedmonth + "/" + dateval + "/" + selectedyear;
                    var d1 = new Date(dateformat1);
                    var d = new Date(dateformat);
                    var day = weekday[d.getDay()];
                    var lastDay = new Date(d1.getFullYear(), d1.getMonth() + 1, 0);
                    var lastday = lastDay.toString().split(" ")[2];
                    if(userflag!="" && userflag=="veraction"){dateval=lastday;}
                    compareto = day + "," + dateval + "," + selectedmonthval + "," + selectedyear;
                    parent.$('#compareto').val(compareto);

                    fromdate = $('#fromdate').val();
                    todate = $('#todate').val();
                    comparefrom = $('#comparefrom').val();
                    compareto = $('#compareto').val();
                    parent.$('#datetext').val('topdate');
//                    alert("fromdate.."+fromdate+"..todate.."+todate+"..comparefrom.."+comparefrom+"..compareto.."+compareto);
                    $.ajax({
                        type: 'GET',
                        async: false,
                        cache: false,
                        timeout: 30000,
                        url: "<%=request.getContextPath()%>/reportViewer.do?reportBy=dateParse&fromdate=" + fromdate + "&todate=" + todate + "&comparefrom=" + comparefrom + "&compareto=" + compareto,
                        success: function(data)
                        {
                            var data1 = new Array()
                            data1 = data.toString().split(",");
                            var perioddate = data1[0];
                            parent.$('#datepicker').val(perioddate);
                        }
                    });
                    if (time == 'load') {
                    } else {
                        submitform();
                    }


                }
 var dateComp="";
                /*** Function End ***/
                $(document).ready(function() {
                    if($('#selecctall').is(":checked")){
                        $('.checkbox1').each(function() {
                                this.checked = true;
                                this.disabled = true;
                            });
                    }
                    $('#selecctall').click(function(event) {
                        if (this.checked) {
                            $('.checkbox1').each(function() {
                                this.checked = true;
                                this.disabled = true;
                            });
                        } else {
                            $('.checkbox1').each(function() {
                                this.checked = false;
                                this.disabled = false;
                            });
                        }
                    });
                    //added by Dinanath
                    $("#createMultiSavePointForReport").dialog({
                        autoOpen: false,
                        height: 350,
                        width: 800,
                        position: 'justify',
                        modal: true
                    });

                 <% boolean dateDisplay=container.getdateenable(); %>
                <% boolean dateComp=container.getenableComp();
               String dateComp1=Boolean.toString(dateComp);
%>
       var dateComp1='<%=dateComp1%>'
         if(dateComp1=="true")
                 {
                   $("#rangeDtclear").show();
                 }
                 else
                     { $("#rangeDtclear1").show();
                     }

                    /*******************************
                     Author:-Faiz Ansari
                     Module:- These codes are used to append changes in header/banner when report/graph/advance visuals is opened.
                     *******************************/
                    /*** Function Start ***/
                    var htmlVar = "";
            <%if (timeinfo.get(1).equalsIgnoreCase("PRG_DATE_RANGE")){%>
                    htmlVar += "<div style='width:95%;'>";
                    htmlVar += "<div id='rangeDtDiv1' class='udLine' ><span id='htime1' class=' gFontFamily ' style='cursor:pointer'><span id='timeMonth1' class=' gFontFamily '><%=date%> </span><span id='timeMonth1' class=' gFontFamily '><%=month%>,</span> <span id='timeYear1' class=' gFontFamily '><%=year%></span></span>";
                    
            htmlVar += "<div id='rangeDtclear' style='display:none;position:absolute;width:490px;height:110px;border-radius:2px;background-color:#000;z-index:101;margin-top:6px;box-shadow:4px 3px 3px -1px #777;padding:5px;'>";
//                                            htmlVar+="<span class='gFontFamily ' style='float:left;padding:5px'><label class='customRadio1'for='optMonthly1'></label><input id='optMonthly1' onclick='appendDateSelect1(1)' class='timeRadio' type='radio' name='htime1' value='monthly' checked='true'/> Monthly </span>";
//                                            htmlVar+="<span class='gFontFamily ' style='float:left;padding:5px'><label class='customRadio1'for='optQuarterly1'></label><input id='optQuarterly1' onclick='appendDateSelect2(1)' class='timeRadio' type='radio' name='htime1' value='quarterly'/> Quarterly </span>";
//                                            htmlVar+="<span class='gFontFamily ' style='float:left;padding:5px'><label class='customRadio1'for='optYearly1'></label><input id='optYearly1' onclick='appendDateSelect3(1)' class='timeRadio' type='radio' name='htime1' value='yearly'/> Yearly</span>";
//                                            //htmlVar+="<span class='gFontFamily ' style='float:left;padding:5px'><label onclick='appendDateSelect1(1)' class='timeRadio customRadio'  value='monthly' ></label> Monthly </span><span class='gFontFamily ' style='float:left;padding:5px'><input onclick='appendDateSelect2(1)' class='timeRadio' type='radio' name='htime' value='quarterly'/> Quarterly </span><span id='apTimeSelect' class='gFontFamily ' style='float:left;padding:5px'><input onclick='appendDateSelect3(1)' class='timeRadio' type='radio' name='htime' value='yearly'/> Yearly</span>";
//                                            htmlVar+="<div id='apTimeSelect1'></div>";
//                                            htmlVar+="<span style='margin-top:5px;width:50%;float:left;'>";
//                                                htmlVar+="<select id='compSelect1' style='width:90%;height:30px;padding:5px 0px 5px 14px;margin-left:12px;'>";
//                                                htmlVar+="<option class='gFontFamily '>MOM</option>";
//                                                htmlVar+="<option class='gFontFamily '>QOQ</option>";
//                                                htmlVar+="<option class='gFontFamily '> YOY</option>";
//                                            htmlVar+="</select></span>";
//                                            htmlVar+="<span style='width: 40%;float: left;background-color: #8BC34A;margin-left: 5%;display: block;height: 22px;margin-top: 5px;font-size: 14px;padding-top: 5px;border-radius: 2px;cursor: pointer;' onclick='setTime(1)'>GO</span>";
//                                        htmlVar+="<span style='display:block;width:100%;float:left;font-size: 12px;padding: 2px 0px;'>From Date</span>";




                    htmlVar += "<span id='selFrmDatToDate11' style='display:block;width:10%;float:left;'>";

                    htmlVar += "</span>";




                     htmlVar += "<span id='hidedate11' style='display:block;width:2%;float:left;padding: 6px 0px;'>";
                    htmlVar += "-</span>";
                     htmlVar += "<span id='selFrmDatMon111' class='classSelFrmDatMon' style='display:block;width:20%;float:left;'  >";
                    htmlVar += "<select id='selFrmDatMon' style='width:90%' onchange='selectDate(\"" + 'selFrmDatToDate' + "\")'>";
                    htmlVar += "<option id='selFrmDatMon1' class='gFontFamily  pdd1' value='Jan'>January</option>";
                    htmlVar += "<option id='selFrmDatMon2' class='gFontFamily  pdd1' value='feb'>February</option>";
                    htmlVar += "<option id='selFrmDatMon3' class='gFontFamily  pdd1' value='mar'>March</option>";
                    htmlVar += "<option id='selFrmDatMon4' class='gFontFamily  pdd1' value='apr'>April</option>";
                    htmlVar += "<option id='selFrmDatMon5' class='gFontFamily  pdd1' value='may'>May</option>";
                    htmlVar += "<option id='selFrmDatMon6' class='gFontFamily  pdd1' value='jun'>June</option>";
                    htmlVar += "<option id='selFrmDatMon7' class='gFontFamily  pdd1' value='jul'>July</option>";
                    htmlVar += "<option id='selFrmDatMon8' class='gFontFamily  pdd1' value='aug'>August</option>";
                    htmlVar += "<option id='selFrmDatMon9' class='gFontFamily  pdd1' value='sep'>September</option>";
                    htmlVar += "<option id='selFrmDatMon10' class='gFontFamily  pdd1' value='oct'>October</option>";
                    htmlVar += "<option id='selFrmDatMon11' class='gFontFamily  pdd1' value='nov'>November</option>";
                    htmlVar += "<option id='selFrmDatMon12' class='gFontFamily  pdd1' value='dec'>December</option>";
                    htmlVar += "</select>";
                    htmlVar += "</span>";
                    htmlVar += "</span>";
                     htmlVar += "<span style='display:block;width:2%;float:left;padding: 6px 0px;'>";
                    htmlVar += "-</span>";
                    htmlVar += "<span id='selFrmDatYr11' style='display:block;width:14%;float:left;'>";
                    htmlVar += "<select id='selFrmDatYr' style='width:90%'>";
                    htmlVar += "<option id='selFrmDatYr2010' value='2010'>2010</option>";
                    htmlVar += "<option id='selFrmDatYr2011' value='2011'>2011</option>";
                    htmlVar += "<option id='selFrmDatYr2012' value='2012'>2012</option>";
                    htmlVar += "<option id='selFrmDatYr2013' value='2013'>2013</option>";
                    htmlVar += "<option id='selFrmDatYr2014' value='2014'>2014</option>";
                    htmlVar += "<option id='selFrmDatYr2015' value='2015'>2015</option>";
                     htmlVar += "<option id='selFrmDatYr2016' value='2016'>2016</option>";
                      htmlVar += "<option id='selFrmDatYr2017' value='2017'>2017</option>";
                       htmlVar += "<option id='selFrmDatYr2018' value='2018'>2018</option>";
                       htmlVar += "<option id='selFrmDatYr2019' value='2019'>2019</option>";
                        htmlVar += "<option id='selFrmDatYr2020' value='2020'>2020</option>";
                          
                    htmlVar += "</select>";
                    htmlVar += "</span>";
                     htmlVar += "<span style='display:block;width:4%;float:left;padding: 6px 0px;'>";
                    htmlVar += "To</span>";

                     htmlVar += "<span id='selToDatToDate11' style='display:block;width:10%;float:left;'>";
                    htmlVar += "</span>";
                    htmlVar += "<span id='hidedate12' style='display:block;width:2%;float:left;padding: 6px 0px;'>";
                    htmlVar += "-</span>";

//                                        htmlVar+="<span style='display:block;width:100%;float:left;font-size: 12px;padding: 2px 0px;'>To Date</span>";
                    htmlVar += "<span id='selToDatMon111' style='display:block;width:20%;float:left;'>";
                    htmlVar += "<select id='selToDatMon' style='width:90%' onchange='selectDateToDatMon(\"" + 'selToDatToDate' + "\")'>";
                    htmlVar += "<option id='selToDatMon1' class='gFontFamily  pdd1' value='jan'>January</option>";
                    htmlVar += "<option id='selToDatMon2' class='gFontFamily  pdd1' value='feb'>February</option>";
                    htmlVar += "<option id='selToDatMon3' class='gFontFamily  pdd1' value='mar'>March</option>";
                    htmlVar += "<option id='selToDatMon4' class='gFontFamily  pdd1' value='apr'>April</option>";
                    htmlVar += "<option id='selToDatMon5' class='gFontFamily  pdd1' value='may'>May</option>";
                    htmlVar += "<option id='selToDatMon6' class='gFontFamily  pdd1' value='jun'>June</option>";
                    htmlVar += "<option id='selToDatMon7' class='gFontFamily  pdd1' value='jul'>July</option>";
                    htmlVar += "<option id='selToDatMon8' class='gFontFamily  pdd1' value='aug'>August</option>";
                    htmlVar += "<option id='selToDatMon9' class='gFontFamily  pdd1' value='sep'>September</option>";
                    htmlVar += "<option id='selToDatMon10' class='gFontFamily  pdd1' value='oct'>October</option>";
                    htmlVar += "<option id='selToDatMon11' class='gFontFamily  pdd1' value='nov'>November</option>";
                    htmlVar += "<option id='selToDatMon12' class='gFontFamily  pdd1' value='dec'>December</option>";
                    htmlVar += "</select>";
                    htmlVar += "</span>";
                    htmlVar += "<span style='display:block;width:2%;float:left;padding: 6px 0px;'>";
                    htmlVar += "-</span>";
                    htmlVar += "<span id='selToDatYr11' style='display:block;width:14%;float:left;'>";
                    htmlVar += "<select id='selToDatYr' style='width:90%'>";
                    htmlVar += "<option id='selToDatYr2010' value='2010'>2010</option>";
                    htmlVar += "<option id='selToDatYr2011' value='2011'>2011</option>";
                    htmlVar += "<option id='selToDatYr2012' value='2012'>2012</option>";
                    htmlVar += "<option id='selToDatYr2013' value='2013'>2013</option>";
                    htmlVar += "<option id='selToDatYr2014' value='2014'>2014</option>";
                    htmlVar += "<option id='selToDatYr2015' value='2015'>2015</option>";
                     htmlVar += "<option id='selToDatYr2016' value='2016'>2016</option>";
                      htmlVar += "<option id='selToDatYr2017' value='2017'>2017</option>";
                       htmlVar += "<option id='selToDatYr2018' value='2018'>2018</option>";
                        htmlVar += "<option id='selToDatYr2019' value='2019'>2019</option>";
                         htmlVar += "<option id='selToDatYr2020' value='2020'>2020</option>";
                     
                    htmlVar += "</select>";
                    htmlVar += "</span>";
                    if(parent.istoggledate!='null' && parent.istoggledate=='No'){
                    htmlVar += "<span id='forcompare' style='display:block;width:100%;float:left;font-size: 12px;padding: 2px 0px;'>Compare With</span>";


                  htmlVar += "<span id='comparefromDTToDate11' style='display:block;width:10%;float:left;'>";
                    htmlVar += "</span>";
                      htmlVar += "<span id='hidetime14' style='display:block;width:2%;float:left;padding: 6px 0px;'>";
                    htmlVar += "-</span>";


                    htmlVar += "<span id='selFrmComMon111' style='display:block;width:20%;float:left;'>";
                    htmlVar += "<select id='selFrmComMon' style='width:90%' onchange='selectDateFrmComMon(\"" + 'comparefromDateToDate' + "\")'>";
                    htmlVar += "<option id='selFrmComMon1' class='gFontFamily  pdd1' value='jan'>January</option>";
                    htmlVar += "<option id='selFrmComMon2' class='gFontFamily  pdd1' value='feb'>February</option>";
                    htmlVar += "<option id='selFrmComMon3' class='gFontFamily  pdd1' value='mar'>March</option>";
                    htmlVar += "<option id='selFrmComMon4' class='gFontFamily  pdd1' value='apr'>April</option>";
                    htmlVar += "<option id='selFrmComMon5' class='gFontFamily  pdd1' value='may'>May</option>";
                    htmlVar += "<option id='selFrmComMon6' class='gFontFamily  pdd1' value='jun'>June</option>";
                    htmlVar += "<option id='selFrmComMon7' class='gFontFamily  pdd1' value='jul'>July</option>";
                    htmlVar += "<option id='selFrmComMon8' class='gFontFamily  pdd1' value='aug'>August</option>";
                    htmlVar += "<option id='selFrmComMon9' class='gFontFamily  pdd1' value='sep'>September</option>";
                    htmlVar += "<option id='selFrmComMon10' class='gFontFamily  pdd1' value='oct'>October</option>";
                    htmlVar += "<option id='selFrmComMon11' class='gFontFamily  pdd1' value='nov'>November</option>";
                    htmlVar += "<option id='selFrmComMon12' class='gFontFamily  pdd1' value='dec'>December</option>";
                    htmlVar += "</select>";
                    htmlVar += "</span>";
                      htmlVar += "<span id='selfrom121' style='display:block;width:2%;float:left;padding: 6px 0px;'>";
                    htmlVar += "-</span>";
                    htmlVar += "<span id='selFrmComYr11' style='display:block;width:14%;float:left;'>";
                    htmlVar += "<select id='selFrmComYr' style='width:90%'>";
                    htmlVar += "<option id='selFrmComYr2010' value='2010'>2010</option>";
                    htmlVar += "<option id='selFrmComYr2011' value='2011'>2011</option>";
                    htmlVar += "<option id='selFrmComYr2012' value='2012'>2012</option>";
                    htmlVar += "<option id='selFrComYr2013' value='2013'>2013</option>";
                    htmlVar += "<option id='selFrmComYr2014' value='2014'>2014</option>";
                    htmlVar += "<option id='selFrmComYr2015' value='2015'>2015</option>";
                    htmlVar += "<option id='selFrmComYr2016' value='2016'>2016</option>";
                    htmlVar += "<option id='selFrmComYr2017' value='2017'>2017</option>";
                    htmlVar += "<option id='selFrmComYr2018' value='2018'>2018</option>";
                    htmlVar += "<option id='selFrmComYr2019' value='2019'>2019</option>";
                     htmlVar += "<option id='selFrmComYr2020' value='2020'>2020</option>";
                    htmlVar += "</select>";
                    htmlVar += "</span>";

                     htmlVar += "<span  id='goiddate'style='display:block;width:4%;float:left;padding: 6px 0px;'>";
                    htmlVar += "To</span>";



                      htmlVar += "<span id='comparetoDTToDate11' style='display:block;width:10%;float:left;'>";
                    htmlVar += "</span>";
                       htmlVar += "<span id ='hidedata15' style='display:block;width:2%;float:left;padding: 6px 0px;'>";
                    htmlVar += "-</span>";


//                                        htmlVar+="<span style='display:block;width:100%;float:left;font-size: 12px;padding: 2px 0px;'>Compare To</span>";
                    htmlVar += "<span id='selToComMon111' style='display:block;width:20%;float:left;'>";
                    htmlVar += "<select id='selToComMon' style='width:90%' onchange='selectDateToComMon(\"" + 'comparetoDateToDate' + "\")'>";
                    htmlVar += "<option id='selToComMon1' class='gFontFamily  pdd1' value='jan'>January</option>";
                    htmlVar += "<option id='selToComMon2' class='gFontFamily  pdd1' value='feb'>February</option>";
                    htmlVar += "<option id='selToComMon3' class='gFontFamily  pdd1' value='mar'>March</option>";
                    htmlVar += "<option id='selToComMon4' class='gFontFamily  pdd1' value='apr'>April</option>";
                    htmlVar += "<option id='selToComMon5' class='gFontFamily  pdd1' value='may'>May</option>";
                    htmlVar += "<option id='selToComMon6' class='gFontFamily  pdd1' value='jun'>June</option>";
                    htmlVar += "<option id='selToComMon7' class='gFontFamily  pdd1' value='jul'>July</option>";
                    htmlVar += "<option id='selToComMon8' class='gFontFamily  pdd1' value='aug'>August</option>";
                    htmlVar += "<option id='selToComMon9' class='gFontFamily  pdd1' value='sep'>September</option>";
                    htmlVar += "<option id='selToComMon10' class='gFontFamily  pdd1' value='oct'>October</option>";
                    htmlVar += "<option id='selToComMon11' class='gFontFamily  pdd1' value='nov'>November</option>";
                    htmlVar += "<option id='selToComMon12' class='gFontFamily  pdd1' value='dec'>December</option>";
                    htmlVar += "</select>";
                    htmlVar += "</span>";
                    htmlVar += "<span id='seltocom111' style='display:block;width:2%;float:left;padding: 6px 0px;'>";
                    htmlVar += "-</span>";
                    htmlVar += "<span id='selToComYr11' style='display:block;width:14%;float:left;'>";
                    htmlVar += "<select id='selToComYr' style='width:90%'>";
                    htmlVar += "<option id='selToComYr2010' value='2010'>2010</option>";
                    htmlVar += "<option id='selToComYr2011' value='2011'>2011</option>";
                    htmlVar += "<option id='selToComYr2012' value='2012'>2012</option>";
                    htmlVar += "<option id='selToComYr2013' value='2013'>2013</option>";
                    htmlVar += "<option id='selToComYr2014' value='2014'>2014</option>";
                    htmlVar += "<option id='selToComYr2015' value='2015'>2015</option>";
                      htmlVar += "<option id='selToComYr2016' value='2016'>2016</option>";
                      htmlVar += "<option id='selToComYr2017' value='2017'>2017</option>";
                      htmlVar += "<option id='selToComYr2018' value='2018'>2018</option>";
                      htmlVar += "<option id='selToComYr2019' value='2019'>2019</option>";
                      htmlVar += "<option id='selToComYr2020' value='2020'>2020</option>";
                    htmlVar += "</select>";
                    htmlVar += "</span>";

                    }
//                                        htmlVar+="<span style='display:block;width:50%;float:left;'><select id='selToDatMon' style='width:80%'><option class='gFontFamily  pdd1'> January</option><option class='gFontFamily  pdd1'> February</option><option class='gFontFamily  pdd1'>March</option><option class='gFontFamily  pdd1'>April</option><option class='gFontFamily  pdd1'>May</option><option class='gFontFamily  pdd1'>June</option><option class='gFontFamily  pdd1'>July</option><option class='gFontFamily  pdd1'>August</option><option class='gFontFamily  pdd1'>September</option><option class='gFontFamily  pdd1'>October</option><option class='gFontFamily  pdd1'>November</option><option class='gFontFamily  pdd1'>December</option></select></span>";
//                                        htmlVar+="<span style='display:block;width:50%;float:left;'><select id='selToDatYr' style='width:80%'><option>2013</option><option>2014</option><option>2015</option></select></span>";
//
//                                        htmlVar+="<span style='display:block;width:50%;float:left;'><select id='selFrmComMon' style='width:80%'><option class='gFontFamily  pdd1'> January</option><option class='gFontFamily  pdd1'> February</option><option class='gFontFamily  pdd1'>March</option><option class='gFontFamily  pdd1'>April</option><option class='gFontFamily  pdd1'>May</option><option class='gFontFamily  pdd1'>June</option><option class='gFontFamily  pdd1'>July</option><option class='gFontFamily  pdd1'>August</option><option class='gFontFamily  pdd1'>September</option><option class='gFontFamily  pdd1'>October</option><option class='gFontFamily  pdd1'>November</option><option class='gFontFamily  pdd1'>December</option></select></span>";
//                                        htmlVar+="<span style='display:block;width:50%;float:left;'><select id='selFrmComYr' style='width:80%'><option>2013</option><option>2014</option><option>2015</option></select></span>";
//
//                                        htmlVar+="<span style='display:block;width:50%;float:left;'><select id='selToComMon' style='width:80%'><option class='gFontFamily  pdd1'> January</option><option class='gFontFamily  pdd1'> February</option><option class='gFontFamily  pdd1'>March</option><option class='gFontFamily  pdd1'>April</option><option class='gFontFamily  pdd1'>May</option><option class='gFontFamily  pdd1'>June</option><option class='gFontFamily  pdd1'>July</option><option class='gFontFamily  pdd1'>August</option><option class='gFontFamily  pdd1'>September</option><option class='gFontFamily  pdd1'>October</option><option class='gFontFamily  pdd1'>November</option><option class='gFontFamily  pdd1'>December</option></select></span>";
//                                        htmlVar+="<span style='display:block;width:50%;float:left;'><select id='selToComYr' style='width:80%'><option>2013</option><option>2014</option><option>2015</option></select></span>";
                    htmlVar += "<span style='display: block;width: 10%;float: left;background-color: rgb(139, 195, 74);margin: 8px 45%;border-radius: 2px;font-size: 18px;' onclick='setTime(1)'>GO</span>";
                       if(userflag!="" && userflag=="veraction"){
                        
                           
                       }else{
                       
                     htmlVar += "<span id='enablecomdata' style='display: block;width: 26%;float: left;background-color: #000000;margin: -28px 67%;border-radius: 2px;font-size: 15px;' >Enable Comparison</span>";
                      htmlVar += "<td><input id='enabledetacomradio' checked value='show' type='radio'  style='display: block;width: 31%;float: left;background-color: rgb(139, 195, 74);margin: -23px 82%;border-radius: 2px;font-size: 18px;' onclick='enableCompar(this.id)'></td>";}
                    // htmlVar += "<span id='enabledatashow' style='display: block;width: 17%;float: left;background-color: #000000;margin: -28px 70%;border-radius: 2px;font-size: 15px;' >enable date</span>";
                     // htmlVar += "<td><input id='enabledatashowrad' checked='unchecked'  type='radio' id='enabledata' style='display: block;width: 31%;float: left;background-color: rgb(139, 195, 74);margin: -23px 71%;border-radius: 2px;font-size: 18px;' onclick='enableDate()'></td>";

                    // htmlVar += "<span id='enabledata' style='display: block;width: 18%;float: left;background-color:#000000;margin: -28px 70%;border-radius: 2px;font-size: 15px;' >disable date</span>";
                     // htmlVar += "<td><input type='radio' id='enabledataradio'  checked='unchecked'  style='display: block;width: 22%;float: left;background-color: rgb(139, 195, 74);margin: -24px 80%;border-radius: 2px;font-size: 18px;' onclick='disableDate()'></td>";
//                    htmlVar += "<input type='radio' style='display: block;width: 10%;float: left;background-color: rgb(139, 195, 74);margin: 8px 45%;border-radius: 2px;font-size: 18px;' onclick='timeEnable()'>enable time";
                    htmlVar += "</div>";
                    htmlVar += "</div>";
                    htmlVar += "<div style='float:left;width:5%;margin-left:5%;margin:2% 2% 0% 5%;'><i style='color:#fff' class='fa fa-minus'></i></div>";
                    htmlVar += "<div id='rangeDtDiv2' class='udLine' ><span id='htime2' class=' gFontFamily ' style='cursor:pointer'><span id='timeMonth1' class=' gFontFamily '><%=date2%> </span><span id='timeMonth2' class=' gFontFamily '><%=month2%>,</span> <span id='timeYear2' class=' gFontFamily '><%=year2%></span></span>";
                    htmlVar += "</div>";
                    htmlVar += "</div>";
            <%}else{%>
                    htmlVar += "<div style='width:80%;'>";
            <%if (timeinfo.get(3).equalsIgnoreCase("Year")){%>
                    htmlVar += "<div id='stdDtDiv1' class='udLine' ><span id='htime3' class=' gFontFamily ' style='cursor:pointer;'><span id='timeYear3' class=' gFontFamily '><%=year%></span><br><span id='dateDisplay' style='font-size: 11px;'><%=datedisplay%></span></span>";
            <% }else if (timeinfo.get(3).equalsIgnoreCase("Qtr")){%>
                    htmlVar += "<div id='stdDtDiv1' class='udLine' ><span id='htime3' class=' gFontFamily ' style='cursor:pointer;'><span id='timeMonth3' class=' gFontFamily '><%=qtr%>-</span> <span id='timeYear3' class=' gFontFamily '><%=year%></span><br><span id='dateDisplay' style='font-size: 11px;'><%=datedisplay%></span></span>";
            <%}else{%>
                    htmlVar += "<div id='stdDtDiv1' class='udLine' ><span id='htime3' class=' gFontFamily ' style='cursor:pointer;'><span id='timeMonth3' class=' gFontFamily '><%=month%>,</span> <span id='timeYear3' class=' gFontFamily '><%=year%></span><br><span id='dateDisplay'  style='font-size: 11px;'><%=datedisplay%></span></span>";
            <%}%>
            if(userflag!="" && userflag=="veraction"){
                    htmlVar += "<div  class='hideAllDiv' id='dateDiv'  style='display:none;position:absolute;width:230px;height:100px;border-radius:2px;background-color:#000;z-index:101;margin-top:6px;box-shadow:4px 3px 3px -1px #777;'>";
<%if ((timeinfo.get(3).equalsIgnoreCase("Month")) || (timeinfo.get(3).equalsIgnoreCase("Monthly"))){%>
                    htmlVar += "<span class='gFontFamily ' style='float:left;padding:5px'><label class='customRadio3'for='optMonthly3'></label><input id='optMonthly3' onclick='appendDateSelect1(3)' class='timeRadio' type='radio' name='htime1' value='monthly' checked='true'/> Monthly </span>";
            <%}else{%>
                    htmlVar += "<span class='gFontFamily ' style='float:left;padding:5px'><label class='customRadio3'for='optMonthly3'></label><input id='optMonthly3' onclick='appendDateSelect1(3)' class='timeRadio' type='radio' name='htime1' value='monthly' /> Monthly </span>";
            <%}%>
            <%if (timeinfo.get(3).equalsIgnoreCase("Qtr")){%>
                    htmlVar += "<span class='gFontFamily ' style='float:left;padding:5px'><label class='customRadio3'for='optQuarterly3'></label><input id='optQuarterly3' onclick='appendDateSelect2(3)' class='timeRadio' type='radio' name='htime1' value='quarterly'  checked='true'/> Quarterly </span>";
            <%}else{%>
                    htmlVar += "<span class='gFontFamily ' style='float:left;padding:5px'><label class='customRadio3'for='optQuarterly3'></label><input id='optQuarterly3' onclick='appendDateSelect2(3)' class='timeRadio' type='radio' name='htime1' value='quarterly' /> Quarterly </span>";
            <%}%>
            <%if (timeinfo.get(3).equalsIgnoreCase("Year")){%>
                    htmlVar += "<span class='gFontFamily ' style='float:left;padding:5px'><label class='customRadio3'for='optYearly3'></label><input id='optYearly3' onclick='appendDateSelect3(3)' class='timeRadio' type='radio' name='htime1' value='yearly' checked='true'/> Yearly</span>";
            <%}else{%>

                    htmlVar += "<span class='gFontFamily ' style='float:left;padding:5px'><label class='customRadio3'for='optYearly3'></label><input id='optYearly3' onclick='appendDateSelect3(3)' class='timeRadio' type='radio' name='htime1' value='yearly'/> Yearly</span>";
            <%}%>
                 <%if (timeinfo.get(3).equalsIgnoreCase("Day")){%>//added by sruthi for daily
                    htmlVar += "<span class='gFontFamily ' style='float:left;padding:5px'><label class='customRadio3'for='optDaily3'></label><input id='optDaily3' onclick='appendDateSelect5(3)' class='timeRadio' type='radio' name='htime1' value='Daily' checked='true'/> Daily</span>";
            <%}else{%>

                    htmlVar += "<span class='gFontFamily ' style='float:left;padding:5px'><label class='customRadio3'for='optDaily3'></label><input id='optDaily3' onclick='appendDateSelect5(3)' class='timeRadio' type='radio' name='htime1' value='Daily'/> Daily</span>";
            <%}%>
                }else{
                    htmlVar += "<div  class='hideAllDiv' id='dateDiv'  style='display:none;position:absolute;width:260px;height:117px;border-radius:2px;background-color:#000;z-index:101;margin-top:6px;box-shadow:4px 3px 3px -1px #777;'>";
                    }
                    //htmlVar+="<span class='gFontFamily ' style='float:left;padding:5px'><label onclick='appendDateSelect1(1)' class='timeRadio customRadio'  value='monthly' ></label> Monthly </span><span class='gFontFamily ' style='float:left;padding:5px'><input onclick='appendDateSelect2(1)' class='timeRadio' type='radio' name='htime' value='quarterly'/> Quarterly </span><span id='apTimeSelect' class='gFontFamily ' style='float:left;padding:5px'><input onclick='appendDateSelect3(1)' class='timeRadio' type='radio' name='htime' value='yearly'/> Yearly</span>";

                    htmlVar += "<div id='apTimeSelect3'>";

                    htmlVar += "</div>";
                     htmlVar += "<div id='apTimeSelectnew'>";
                    if((parent.$("#AOId").val()==null ||parent.$("#AOId").val()=="")){
                      htmlVar += "<span id='comparitiveSpan' style='margin-top:5px;width:50%;float:left;'>";
                    }else{
                    htmlVar += "<span id='comparitiveSpan' style='display:none;margin-top:8px;width:50%;float:left;'>";
                    }
                    htmlVar += "<select id='compSelect3' style='width:90%;height:30px;padding:5px 0px 5px 14px;margin-left:12px;'>";
                    htmlVar += "<option class='gFontFamily ' selected value='MOM'>MOM</option>";
                    htmlVar += "<option class='gFontFamily '>MOY</option>";

                    htmlVar += "</select></span>";
                    if(userflag!="" && userflag=="veraction"){}else{
                     htmlVar += "<span style='margin-top:5px;width:50%;float:left;'>";
                    htmlVar += "<select id='Datetype' onchange='Datedisplayregion()' style='width:90%;height:30px;padding:5px 0px 5px 14px;margin-left:12px;'>";
                    htmlVar += "<option id='DatetypeDaily' class='gFontFamily ' value='Daily'>Daily</option>";
                    htmlVar += "<option id='DatetypeWeekly' class='gFontFamily ' value='Weekly'>Weekly</option>";
                    htmlVar += "<option id='DatetypeMonthly' class='gFontFamily ' value='Monthly'>Monthly</option>";
                    htmlVar += "<option id='DatetypeQuarterly' class='gFontFamily ' value='Quarterly'>Quarterly</option>";
                    htmlVar += "<option id='DatetypeYearly' class='gFontFamily ' value='Yearly'>Yearly</option>";
                    htmlVar += "</select></span>";
                    }
                    if(userflag!="" && userflag=="veraction"){
                     htmlVar += "<span><span id='DatedropGO'  style='width: 45%;float: left;background-color: #8BC34A;display: block;height: 22px;margin-top: 8px;font-size: 14px;padding-top: 5px;border-radius: 2px;cursor: pointer;' onclick='setTime(3)'>GO</span>";
}else{
                    htmlVar += "<span><span id='DatedropGO'  style='width: 45%;float: left;background-color: #8BC34A;margin-left: 30%;display: block;height: 22px;margin-top: 8px;font-size: 14px;padding-top: 5px;border-radius: 2px;cursor: pointer;' onclick='setTime(3)'>GO</span>";
                 }
                    //added by anitha for comparision with
                   if(userflag !=="veraction"){
                    if(parent.$("#AOId").val()!=null && parent.$("#AOId").val()!=""  &&'<%=timeinfo.get(1)%>' == "PRG_STD"){
                    htmlVar += "<span id='dateCompareSpan' style='width: 5%;float: left;display: block;padding-left: 45px;padding-top: 15px;' ><input type='checkbox' onclick='comparitiveMethod(this)' id='comparivitiveButton' name='comparivitiveButton' value='comparivitiveButton'><br><span id='normalcompspan' style='margin-top:10px;margin-left:-200px;top:14px;position:relative;width:50%;float:center;font-size: 11px;white-space: nowrap;'>(Compared To:  <span id='DateCompare'><%=cmpdatedisplay%>)</span></span></span>"
                    }else{
                    htmlVar +="<br><span id='normalcompspan' style='margin-top:10px;top:5px;position:relative;width:50%;float:center;font-size: 11px;white-space: nowrap;'>(Compared To:  <span id='DateCompare'><%=cmpdatedisplay%>)</span></span>";
                     }
                     }
                    htmlVar += "</span>";
                    htmlVar += "</div>";
                    htmlVar += "</div>";
                    htmlVar += "</div>";
            <%}%>

                    $("#hdTimeCtrlDiv").append(htmlVar);
                    if('<%=timeinfo.get(1)%>' == "PRG_STD"){
                    if(userflag =="veraction"){
                    $("#hdTimeCtrlDiv").css("margin-top","30px");
                    $("#hdTimeCtrlDiv").css("width","24%");
                    $("#hdTimeCtrlDiv").css("float","right");
                    }else{
                    
                    $("#hdTimeCtrlDiv").css("margin-top","17px");
                        }
                    }
                    $("#hdFixedDiv").height("85px");
                    if ($(window).width() <= 1250) {
                        $("#stdDtDiv1").width("60%");
                    }

            <%if (timeinfo.get(1).equalsIgnoreCase("PRG_DATE_RANGE")){%>
             var htmlVardatef = "";
                
                    htmlVardatef += "<select id='selFrmDatToDate' style='width:90%'>";
                   var size=31;
                    for(var i=1;i<=size;i++){
                         htmlVardatef += "<option id='selFrmDatToDate"+i+"' value='"+i+"'>"+i+"</option>";
                    }
                      htmlVardatef += "</select>";
                       $("#selFrmDatToDate11").append(htmlVardatef);

var htmlVardatef = "";
                
                    htmlVardatef += "<select id='selToDatToDate' style='width:90%'>";
                   var size=31;
                    for(var i=1;i<=size;i++){
                         htmlVardatef += "<option id='selToDatToDate"+i+"' value='"+i+"'>"+i+"</option>";
                    }
                      htmlVardatef += "</select>";

                  $("#selToDatToDate11").append(htmlVardatef);
                  
                  
                  var htmlVardatef = "";
                
                    htmlVardatef += "<select id='comparefromDateToDate' style='width:90%'>";
                   var size=31;
                    for(var i=1;i<=size;i++){
                         htmlVardatef += "<option id='comparefromDateToDate"+i+"' value='"+i+"'>"+i+"</option>";
                    }
                      htmlVardatef += "</select>";

                  $("#comparefromDTToDate11").append(htmlVardatef);
                  
                   
                   var htmlVardatef = "";
                
                    htmlVardatef += "<select id='comparetoDateToDate' style='width:90%'>";
                   var size=31;
                    for(var i=1;i<=size;i++){
                         htmlVardatef += "<option id='comparetoDateToDate"+i+"' value='"+i+"'>"+i+"</option>";
                    }
                      htmlVardatef += "</select>";

                  $("#comparetoDTToDate11").append(htmlVardatef);
                 
                  
                    $("#selFrmDatMon" +<%=mon1%>).prop("selected", "true");
                    $("#selFrmDatYr" +<%=yr1%>).prop("selected", "true");
                    $("#selToDatMon" +<%=mon2%>).prop("selected", "true");
                    $("#selToDatYr" +<%=yr2%>).prop("selected", "true");
                    $("#selFrmComMon" +<%=common1%>).prop("selected", "true");
                    $("#selFrmComYr" +<%=comyr1%>).prop("selected", "true");
                    $("#selToComMon" +<%=common2%>).prop("selected", "true");
                    $("#selToComYr" +<%=comyr2%>).prop("selected", "true");
                    $("#selFrmDatToDate" +<%=date%>).prop("selected", "true");
                    $("#selToDatToDate" +<%=date2%>).prop("selected", "true");
                    $("#comparefromDateToDate" +<%=date3%>).prop("selected", "true");
                    $("#comparetoDateToDate" +<%=date4%>).prop("selected", "true");
                    if ($(window).width() <= 1250) {
                        $("#hdFavRep").width("62%");
                        $("#hdTimeCtrlDiv").width("35%");
                        $("#rangeDtDiv1,#rangeDtDiv2").width("42%");
                    }
                     if(userflag!="" && userflag=="veraction"){
                          $("#hidedate11").hide();
                 $("#hidedate12").hide();
                 $("#hidetime14").hide();
                 $("#hidedata15").hide();
                                         $("#selFrmDatMon111").css({ width:"25%"  });
                     $("#selToDatMon111").css({ width:"25%"  });
   $("#selFrmComMon111").css({ width:"25%"  });
                      $("#selToComMon111").css({ width:"25%"  });$("#dateDiv").css({'width':'230px'});
$("#selFrmDatYr11").css({ width:"18%"  });$("#normalcompspan").hide();$("#dateDiv").css({'height':'100px'});
$("#selToDatYr11").css({ width:"18%"  });$("#selFrmComYr11").css({ width:"18%"  });
$("#selToComYr11").css({ width:"18%"  });
                          $("#selFrmDatToDate11").hide(); $("#selToDatToDate11").hide(); $("#comparefromDTToDate11").hide(); $("#comparetoDTToDate11").hide();
                     }else{
                         
                     }
            <%}%>
                   $('#selFrmDatToDate,#selFrmDatMon,#selFrmDatYr,#selToDatToDate,#selToDatMon,#selToDatYr,#comparefromDateToDate,#selFrmComMon,#selFrmComYr,#comparetoDateToDate,#selToComMon,#selToComYr').select2({
                        minimumResultsForSearch: Infinity
                    });if($("#Datetype").val()!=undefined)
                       $("#Datetype"+'<%=datetype%>').prop("selected", "true");
            <%if ((timeinfo.get(3).equalsIgnoreCase("Month")) || (timeinfo.get(3).equalsIgnoreCase("Monthly"))){%>
                    appendDateSelect1(3);
            <%}else if (timeinfo.get(3).equalsIgnoreCase("Qtr")){%>
                    appendDateSelect2(3);
            <%}else if (timeinfo.get(3).equalsIgnoreCase("Year")){%>
                    appendDateSelect3(3)
            <%} else if(timeinfo.get(3).equalsIgnoreCase("Day")){%>//added by sruthi for daily
                appendDateSelect5(3)//ended by sruthi
            <%}else {%>
                appendDateSelect4(3)
            <%}%>
                    htmlVar = "";
//    added  by krishan Pratap
 <%  if (veractionStatus.equalsIgnoreCase("OK")) {%>
     if ('<%=defaulttab%>' == 'Report'||'<%=defaulttab%>' == 'Table') {
         htmlVar = "<div style='width:30%;height:100%;float:left'><a  id='landingPageredirect' href='landingPage.jsp'><i style='color:#369;font-size:20px;padding:5px 0px 5px 15px;cursor:pointer;float: left;padding-left: 10px;'class='fa fa-long-arrow-left'></i></a><a onclick='openImgDiv()' href='<%=resetpath%>' ><span id='repName'class=' gFontFamily ' title='<%=reportName%>' style='white-space: nowrap;overflow: hidden;text-overflow: ellipsis;padding:5px 5px 5px 40px;display:block;font-size:16px;font-weight:bold;color:#369;'><%=reportName%></span></a></div>";
     }else{
                                htmlVar = "<div style='width:30%;height:100%;float:left'><a onclick='openImgDiv()' href='<%=resetpath%>' ><span id='repName'class=' gFontFamily ' title='<%=reportName%>' style='white-space: nowrap;overflow: hidden;text-overflow: ellipsis;padding:5px 5px 5px 40px;display:block;font-size:16px;font-weight:bold;color:#369;'><%=reportName%></span></a></div>";
     }
                               
                                <%} else {%>
                    htmlVar = "<div style='width:30%;height:100%;float:left'><a href='landingPage.jsp'><i style='color:#369;font-size:20px;padding:5px 0px 5px 15px;cursor:pointer;float: left;padding-left: 10px;'class='fa fa-long-arrow-left'></i></a><a onclick='openImgDiv()' href='<%=resetpath%>' ><span id='repName'class=' gFontFamily ' title='<%=reportName%>' style='white-space: nowrap;overflow: hidden;text-overflow: ellipsis;padding:5px 5px 5px 40px;display:block;font-size:16px;font-weight:bold;color:#369;'><%=reportName%></span></a></div>";
                                <%}%>
                    
            <%if(!true){%>
                    htmlVar += "<div id='tblName' value='Table' class=' gFontFamily ' style='width: 30%;float: left;text-align: center;padding: 10px 0px 2px;cursor:pointer;display:<%=showiconreport%>;'><%=TranslaterHelper.getTranslatedInLocale("Data_Analysis", cL)%></div>";
                    htmlVar += "<div id='dashName' value='Dashboard' class=' gFontFamily ' style='width: 30%;float: left;text-align: center;padding: 10px 0px 2px;cursor:pointer;display:<%=showicongraph%>;'><%=TranslaterHelper.getTranslatedInLocale("dashboard", cL)%></div>";
                    htmlVar += "<div id='visualName' value='Visual' class=' gFontFamily ' style='width: 30%;float: left;text-align: center;padding: 10px 0px 2px;cursor:pointer;display:<%=showiconadvancevisual%>;'><%=TranslaterHelper.getTranslatedInLocale("Visual", cL)%></div></div>";
//                    htmlVar += "<div style='width:16%;height:100%;float:left'>";
                    htmlVar += "</div>";
            <%}else{%>
                    htmlVar += "<div id='pageList' style='font-size:14px;width:65%;height:100%;float:left'>";
                    htmlVar += "</div>";
                    htmlVar += "<div style='width: 10%;float: left;height:100%;'><div id='morePages' onclick='showMorePages(event)' class=' gFontFamily ' style='font-size:14px;width: 10%;float: left;text-align: center;padding: 10px 0px 2px;cursor:pointer;'></div></div>";
                    htmlVar += "<div style='width:16%;height:100%;float:left'>";
            <%if(showiconadvancevisual.equalsIgnoreCase("none")){%>
//                    htmlVar += "<i id='visualIco' value='Visual' class='fa fa-area-chart fa-2x' style='float:right;margin-left:10px;padding:5px 5px 0px 5px;cursor:pointer;color:grey;display:none'></i>";
            <%}else{%>
//                    htmlVar += "<i id='visualIco' value='Visual' class='fa fa-area-chart fa-2x' style='float:right;margin-left:10px;padding:5px 5px 0px 5px;cursor:pointer;color:grey;display:block''></i>";
            <% }
                                    if(showicongraph.equalsIgnoreCase("none")){%>
                    htmlVar += "<i id='dashIco' value='Dashboard' class='fa fa-line-chart fa-2x' title='Clicked To view DashBoard' style='float:right;margin:0px 10px;padding:5px 5px 0px 5px;cursor:pointer;color:grey;display:none'></i>";
            <% }else{%>
                    htmlVar += "<i id='dashIco' value='Dashboard' class='fa fa-line-chart fa-2x' title='Clicked To view DashBoard' style='float:right;margin:0px 10px;padding:5px 5px 0px 5px;cursor:pointer;color:grey;display:block''></i>";
            <% }
                                        if(showiconreport.equalsIgnoreCase("none")){%>
                    htmlVar += "<i id='tblIco' value='Table' class='fa fa-table fa-2x' title='Clicked To view Data Table' style='float:right;margin-left:10px;padding:5px 5px 0px 5px;cursor:pointer;color:grey;display:none'></i>";
            <% }else{%>
                    htmlVar += "<i id='tblIco' value='Table' class='fa fa-table fa-2x' title='Clicked To view Data Table' style='float:right;margin-left:10px;padding:5px 5px 0px 5px;cursor:pointer;color:grey;display:block'></i>";
            <%}%>
//htmlVar += "</div>";
            <%}%>
                    htmlVar += "</div>";
                    htmlVar += "<div style='width:5%;margin-top:-30px;height:100%;float:right'>";
                    htmlVar += "<i id='optionMenuBtn' class='fa fa-list-ul fa-2x' title='Clicked To view Option' style='float:right;margin-right:20px;padding:5px;cursor:pointer;color:grey'></i>";
                    htmlVar += "<div id='optionMenuDiv' class='hideAllDiv' onmouseleave='hideme(\"" + 'optionMenuDiv' + "\")'>";
                    htmlVar += "</div></div>";
                    $("#repCtrlDiv").html("");
                    $("#repCtrlDiv").append(htmlVar);
                    $("#tblName,#tblIco").attr("onclick", "loadingMenu('Table')");
                    $("#dashName,#dashIco").attr("onclick", "loadingMenu('Graph')");
                    $("#visualName,#visualIco").attr("onclick", "loadingMenu('advance')");
            <%if(container.report_Headrvalue!=null && container.report_Headrvalue.equalsIgnoreCase("hideheader")){%>
                    $("#repCtrlDiv").hide();
                    $("#hdFixedDiv").height("60px");
            <%}else{%>
                    $("#repCtrlDiv").show();
            <%}%>
                    appendRepCtrlDiv('<%=defaulttab%>');
                    var htmlVar = "";
                    $("body").on("click", "#optionMenuBtn", function() {
                            $("#optionMenuDiv").css({
                               "height":"auto",
                               "max-height":$(window).height()-150
                            });
                        if ($("#optionMenuDiv").is(":visible")) {
                            $(".hideAllDiv").hide();
//                            $("#optionMenuDiv").slideUp("fast");
                        }
                        else {
                            $(".hideAllDiv").hide();
                            $("#optionMenuDiv").fadeIn("fast");
                        }
                    });
                    $("body").on("click", "#optionMenuDiv li", function() {
                        $(".hideAllDiv").hide();
                        $("#optionMenuDiv").fadeOut("fast");
                    });
                    $("body").on("click", "#selMonth", function() {
                        $(this).next().slideToggle();
                    });
                    $("body").on("click", "#htime1,#htime2", function() {
                        $(".hideAllDiv").hide();
                        $("#htime1").next().css("margin-left", ($(this).parent().width() - 490) / 2);
                        $("#htime3").next().slideUp("fast");
                        $("#htime1").next().slideToggle("fast");
                    });
                    $("body").on("click", "#htime3", function() {
                        if(parent.$("#AOId").val()!=null && parent.$("#AOId").val()!=""  &&'<%=timeinfo.get(1)%>' == "PRG_STD"){
                        comparitiveMethod();
                        }
                        if ($("#dateDiv").is(":visible")) {
                            $(".hideAllDiv").hide();
                        } else {
                            $(".hideAllDiv").hide();
                            $("#htime3").next().css("margin-left", ($(this).parent().width() - 240) / 2);
                            $("#htime1,#htime2").next().slideUp("fast");
                            $(this).next().slideToggle("fast");
                        }
                    });
//                    $("body").on("click","#select2-selectMonth-container",function(){
//                        $("#timeMonth").text($("#select2-selectMonth-container").text()+",");
//                    });
//                    $("body").on("click","#select2-selectQuater-container",function(){
//                        $("#timeMonth").text($("#select2-selectQuater-container").text()+",");
//                    });
//                    $("body").on("click","#select2-selectYear-container",function(){
//                        $("#timeYear").text($("#select2-selectYear-container").text());
//                    });
//                    if('<%=defaulttab%>' == 'Report'){$("#tblName").addClass("udLine");appendRepCtrlDiv('Table');}
//                    else if('<%=defaulttab%>' == 'Graph'){$("#dashName").addClass("udLine");appendRepCtrlDiv('Dashboard');}
//                    else if('<%=defaulttab%>' == 'AdvanceVisuals') {$("#visualName").addClass("udLine");appendRepCtrlDiv('Visual');}
//                    else{$("#tblName").addClass("udLine");appendRepCtrlDiv('Table');}
//                    $("#tblName,#dashName,#visualName").click(function(){
//                        $("#tblName,#dashName,#visualName").removeClass("udLine");
//                        $(this).addClass("udLine");
//                        appendRepCtrlDiv($(this).text());
//                    });
                    if ('<%=defaulttab%>' == 'Report') {
                        $("#tblName,#tblIco").addClass("udLine");
                        appendRepCtrlDiv('Table');
                    }
                    else if ('<%=defaulttab%>' == 'Graph') {
                        $("#dashName,#dashIco").addClass("udLine");
                        appendRepCtrlDiv('Dashboard');
                    }
                    else if ('<%=defaulttab%>' == 'AdvanceVisuals') {
                        $("#visualName,#visualIco").addClass("udLine");
                        appendRepCtrlDiv('Visual');
                    }
                    else {
                        $("#tblName,#tblIco").addClass("udLine");
                        appendRepCtrlDiv('Table');
                    }

                    $("#tblName,#dashName,#visualName,#tblIco,#dashIco,#visualIco").click(function() {
                        $("#tblName,#dashName,#visualName,#tblIco,#dashIco,#visualIco").removeClass("udLine");
                        $(this).addClass("udLine");
                        if ($(this).attr("id") == "tblName" || $(this).attr("id") == "tblIco") {
                            $("#tblName,#tblIco").addClass("udLine");
                            appendRepCtrlDiv($(this).attr("value"));
                        }
                        else if ($(this).attr("id") == "dashName" || $(this).attr("id") == "dashIco") {
                            $("#dashName,#dashIco").addClass("udLine");
                            appendRepCtrlDiv($(this).attr("value"));
                        }
                        else {
                            $("#visualName,#visualIco").addClass("udLine");
                            appendRepCtrlDiv($(this).attr("value"));
                        }
                    });
                    var ckOpenFlg = "0";
                    $("body").on("click", ".ftddropmnum", function() {
                        $(".ftddropmnum").next().slideUp("fast");
                        $(".ftddropmnum").children().children().removeClass("fa-chevron-up");
                        $(".ftddropmnum").children().children().addClass("fa-chevron-down");
                        if (ckOpenFlg != $(this).attr("id")) {
                            $(this).children().children().removeClass("fa-chevron-down");
                            $(this).children().children().addClass("fa-chevron-up");
                            $(this).next().slideDown("slow");
                            ckOpenFlg = $(this).attr("id");
                        }
                        else {
                            ckOpenFlg = "0";
                        }

                    });
                    //                $("body").on("click",".customSelect",function(){
                    //                    $(this).children("ul").toggle();
                    //                });
                    //                $("body").on("click",".customSelect li",function(){
                    //                    $(this).parent().parent().children("span").text($(this).text());
                    //                    $(".customSelect li").css("background-color","#f2f2f2");
                    //                    $(this).css("background-color","#8BC34A");
                    //                });
                    //                $("body").on("hover",".customSelect li",function(){
                    //                    $(".customSelect li").css("background-color","#f2f2f2");
                    //                    $(this).css("background-color","#8BC34A");
                    //                });

                    /*** Function End ***/
                    $("#xtendChartssTD").height($(window).height());
                    $("#conditionalShading").hide();
                    //                $("#paramHeaderId").css({'display':'block'});
                    wid2 = $("#rightDiv1").width();
                    //        added by Manik Srivastava
                    // var widt=parseInt(((($(window).width()*.84))-14)/8);
                    //      loadingMenu('Table')
                    //$("#trendMeas").multiselect({
                    //   selectedText: "# of # selected",
                    //   selectedList: 3
                    //});
                    //sandeep

            <%
                if (allFiltersnames != null) {
                    StringBuilder stringbuilder5 = new StringBuilder();
                    Set keySet15 = allFiltersnames.keySet();
                    List<String> filterids5 = new ArrayList<String>();
                    String key15;
                    int i15 = 0;
                    Set keySet5 = allFilters.keySet();
                    Iterator itr5 = keySet5.iterator();
                    String key5;
                    for (int j15 = 0; j15 < viewbynames.size(); j15++) {
                        List<String> parameterlistNames = new ArrayList<String>();
                        Iterator itr15 = keySet15.iterator();
                        while (itr15.hasNext()) {

                            key15 = itr15.next().toString();
                            if (key15.equalsIgnoreCase(viewbynames.get(j15))) {
                                key15 = key15.replace(" ", "1q1");String selectidg=parameterlist.get(j15)+"__"+key15;
                                String keyadv = "ad" + key15;String selectidad=parameterlist.get(j15)+"__"+keyadv;
                                String keytrend = "Tr" + key15;String selectidtr=parameterlist.get(j15)+"__"+keytrend;

            %>
                    displayFilt1('<%=selectidg%>');
                   // displayFiltadv('<%=selectidad%>');
                   // displayFiltTrend('<%=selectidtr%>');
            <%}
            }
        }
    }%>
            <%
                String[] listids = null;
                List al12 = null;
                List Notal12 = null;
                HashMap reportParam1 = collect.reportParameters;
                if (reportParam1 != null && !reportParam1.isEmpty()) {
                    ArrayList al1 = null;
                    List al2 = null;
                    //  for(int i=0; i <map.size() ; i++){ ;
                    if (reportParam1 != null && (!reportParam1.isEmpty())) {
                        listids = (String[]) (reportParam1.keySet()).toArray(new String[reportParam1.size()]);

                    }
                    List<String> viewbynames1 = new ArrayList<String>();
                    List<String> viewbyidstd = new ArrayList<String>();
                    viewbynames1 = new ArrayList<String>();

                    HashMap inMap1;
                    inMap1 = (HashMap) collect.operatorFilters.get("IN");
                    String[] a1 = (String[]) (reportParam1.keySet()).toArray(new String[reportParam1.size()]);
                    for (int j = 0; j < reportParam1.size(); j++) {
                        al1 = (ArrayList) reportParam1.get(a1[j]);
                        al2 = (List) al1.get(8);
                        viewbynames1.add(al1.get(1).toString());
                        viewbyidstd.add(al1.get(0).toString());

                        al12 = (List<String>) inMap1.get(a1[j]);

                    }

                    StringBuilder stringbuilder5 = new StringBuilder();

                    String key15;
int size = 6;
                            if (viewbynames1.size() > 6) {
                                size = viewbynames1.size();
                            } else {
                                size = viewbynames1.size();
                            }
                    for (int j15 = 0; j15 < size; j15++) {
                        key15 = viewbynames1.get(j15).toString();
                        if (key15.equalsIgnoreCase(viewbynames1.get(j15))) {
                            key15 = key15.replace(" ", "1q1");
                            String keytable = "table" + key15.replace("/", "11q");
String selectid=viewbyidstd.get(j15)+"__"+keytable;
            %>
                    displayFilttable('<%=selectid%>');
            <%}
        }
    }%>

            <%
        if (parameterlist != null) {
            for (int j = 0; j < parameterlist.size(); j++) {%>
                    parent.filterMapgraphs["<%=parameterlist.get(j)%>"] = new Array();  parent.filterMapNotinDB["<%=parameterlist.get(j)%>"] = new Array();
                    <%}
                     }%>
            <% if (reportParam1 != null && !reportParam1.isEmpty()) {
          if (listids != null) {
              for (int j = 0; j < listids.length; j++) {%>
                    parent.filterMapNewtb["<%=listids[j]%>"] = new Array();
                    parent.filterMapNotin["<%=listids[j]%>"] = new Array();
                    parent.filterMapNew["<%=listids[j]%>"] = new Array();
                    filterMapglobal["<%=listids[j]%>"] = new Array();
                    <%}
                          }
                          HashMap inMap;
                          inMap = (HashMap) collect.operatorFilters.get("IN");
                          HashMap NotinMap = (HashMap) collect.operatorFilters.get("NOTIN");
                          ArrayList alids = null;
                          String[] a1 = (String[]) (reportParam1.keySet()).toArray(new String[reportParam1.size()]);
                          for (int j = 0; j < reportParam1.size(); j++) {
                              alids = (ArrayList) reportParam1.get(a1[j]);
                              al12 = (List<String>) inMap.get(a1[j]);
                              Notal12 = (List<String>) NotinMap.get(a1[j]);
                              if (Notal12 != null && Notal12.size() > 0) {
                                  for (int l = 0; l < Notal12.size(); l++) {
                                      if (!Notal12.get(l).toString().equalsIgnoreCase("All")) {
                                          filterShow = "true";
            %>
                    parent.filterMapNotin["<%=a1[j]%>"].push("<%=Notal12.get(l).toString()%>");
            <%  }
                    }
                }
                if (al12.size() >= 1) {
                    for (int l = 0; l < al12.size(); l++) {
                        if (!al12.get(l).toString().equalsIgnoreCase("All")) {
                            filterShow = "true";
//Added by Ram
                            if (lookup.containsKey(al12.get(l))) {
                                al12.set(l, lookup.get(al12.get(l)));
                            }

                            // endded by ram
            %>
                    parent.filterMapNew["<%=a1[j]%>"].push("<%=al12.get(l).toString()%>");
                    parent.filterMapNewtb = parent.filterMapNew



            <%  }
                            }
                        }

                    }
                }

            %>
                    //end of sandeep code for global filter
            <% if ((isResetGO.equalsIgnoreCase("true") || isGraphRefresh.equalsIgnoreCase("true")) && requestValue != null && requestValue.equalsIgnoreCase("paramChange")) {%>

                    getGraphObjectReset('<%=reportId1%>', '<%=reportName%>');
            <%}%>
                    //
                    //             $.ajax({
                    //            url:'reportTemplateAction.do?templateParam=GetDefaultTab&reportId='+"<%=reportId1%>",
                    //            success: function(data){
                    //           defaulttab=data;
                    ////           alert(defaulttab+"mohit")
                    //             opencustomtab(defaulttab);
                    ////loadingMenu(defaulttab)
                    //            }
                    //           opencustomtab("<%=defaulttab%>");
                    //        });

                    var resizeFlag;
                    if(userType == "ANALYZER"){
                           resizeFlag=false;
                    }
                    else{
                        resizeFlag=true;
                    }

                    var widt = parseInt((((($(window).width() * .56)) - 35) / 4) / 8);
                    var x = "1%";
                    if(draggable==="nondraggable"&& window.userType!=="ANALYZER"){
                    gridster1 = $(".gridster > ul").gridster({
                        widget_margins: [11, 11],
                        //        autogenerate_stylesheet: false,
                        widget_base_dimensions: [widt, widt],
                        min_cols: 29,
                        resize: {
                            enabled: resizeFlag,
                            axes: ['both'],
                            handle_class: 'gs-resize-handle',
                            start: function(e, ui, $widget) {
                                //                    gridResize(e, ui,$widget);
                                //alert("id  "+($($widget).attr('id')).replace("div","" ))
                                //$('#renameTitle'+($($widget).attr('id')).replace("div","" )).quickfit({ max: 24, min: 14, truncate: true });


                            },
                            stop: function(e, ui, $widget) {
                                var newHeight = this.resize_coords.data.height;
                                var newWidth = this.resize_coords.data.width;
                                //                 gridResize(newHeight, newWidth);
                                //      alert("id  "+($($widget).attr('id')).replace("div","" ))
                                var chartId = $($widget).attr('id').replace("div", "");
                                localRefresh(chartId);
                            }
                        },
                        
                        draggable: {
                            ignore_dragging: true
                        }
                        
                        // edit by shivam
                    }).data('gridster').disable();
                }else{
                gridster1 = $(".gridster > ul").gridster({
                        widget_margins: [11, 11],
                        //        autogenerate_stylesheet: false,
                        widget_base_dimensions: [widt, widt],
                        min_cols: 29,
                        resize: {
                            enabled: resizeFlag,
                            axes: ['both'],
                            handle_class: 'gs-resize-handle',
                            start: function(e, ui, $widget) {
                                //                    gridResize(e, ui,$widget);
                                //alert("id  "+($($widget).attr('id')).replace("div","" ))
                                //$('#renameTitle'+($($widget).attr('id')).replace("div","" )).quickfit({ max: 24, min: 14, truncate: true });


                            },
                            stop: function(e, ui, $widget) {
                                var newHeight = this.resize_coords.data.height;
                                var newWidth = this.resize_coords.data.width;
                                //                 gridResize(newHeight, newWidth);
                                //      alert("id  "+($($widget).attr('id')).replace("div","" ))
                                var chartId = $($widget).attr('id').replace("div", "");
                                localRefresh(chartId);
                            }
                        }
                       // edit by shivam
                    }).data('gridster').disable();
                }
                    
                    //  gridster1.generate_stylesheet({rows: 78, cols: 29});
                    //  gridster1.generate_stylesheet({rows: 9, cols: 8});

                    var x = "1%";
                    gridster2 = $(".gridster > ul").gridster({
                        widget_margins: [9, 9],
                        autogenerate_stylesheet: false,
                        widget_base_dimensions: [widt, widt],
                        min_cols: 8,
                        resize: {
                            enabled: resizeFlag,
                            axes: ['both'],
                            handle_class: 'gs-resize-handle',
                            start: function(e, ui, $widget) {
                                //                    gridResize(e, ui,$widget);
                                //alert("id  "+($($widget).attr('id')).replace("div","" ))
                                //$('#renameTitle'+($($widget).attr('id')).replace("div","" )).quickfit({ max: 24, min: 14, truncate: true });


                            },
                            stop: function(e, ui, $widget) {
                                var newHeight = this.resize_coords.data.height;
                                var newWidth = this.resize_coords.data.width;
                                //                 gridResize(newHeight, newWidth);
                                //      alert("id  "+($($widget).attr('id')).replace("div","" ))
                                var chartId = $($widget).attr('id').replace("div", "");
                                localRefresh(chartId);
                            }

                        }
                        // edit by shivam

                    }).data('gridster').disable();
                    gridster2.generate_stylesheet({rows: 30, cols: 8});
            <%
                long current_time = System.currentTimeMillis();
                long st_time = Long.parseLong(session.getAttribute("rep_st_time").toString());
                double total_time = (current_time - st_time) * Math.pow(10, -3);
                double finalValue = Math.round(total_time * 100.0) / 100.0;
            %>

                    //                    $("li").filter(function() {
                    //                        return $(this).text() == 'Data Analysis';
                    //                    }).click(function() {
                    $("#lmTable").click(function() {
                        var html1 = "";
                        var st = [];
                        st.push('<%=request.getContextPath()%>');
                        st.push('<%=sbufRoles.toString().substring(1)%>');
                        st.push('<%=reportId1%>');
                        st.push('<%=reportDrill%>');
                        st.push('<%=USERID%>')
                        st.push('<%=rolesid%>')
                        st.push('<%=reportName%>')
                        $("#saveWithGraph").remove();
                        // Added By Ram for removing overlap options
                        //                        html1+='<li id="testCase" class="drpdown menu-item" style="width: 15%"></li><a href="#" style="padding: 1px;width:100%"><span><font size="3" face="verdana" style="font-size: 14px; color: white">Options</font></span></a>';
                        //                        html1 += '<ul class="drpcontent" id="themeselect">';
                        html1 += "<li style='text-align;'>";
                        html1 += "<a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='dispChangeTableColumns(\"" + st[0] + "\"," + st[1] + "," + st[2] + ")' >Edit Table Data</a>";
                        html1 += "</li>";
                        html1 += "<li style='text-align;'>";
                        html1 += "<a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='editViewBy()' >Edit Table Layout</a>";
                        html1 += "</li>";
                        html1 += "<li style='text-align;'>";
                        html1 += "<a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='reportDrillAssignment(\"" + st[0] + "\"," + st[2] + ")' >Define Report Drill</a>";
                        html1 += "</li>";
                        html1 += "<li style='text-align;'>";
                        html1 += "<a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='addCustomMeasure1(\"" + st[0] + "\"," + st[2] + ")' >Define Custom Measure</a>";
                        html1 += "</li>";
            <%if (container.isReportCrosstab()) {%>
                        html1 += "<li style='text-align;'>";
                        html1 += "<a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='difineCustomSeq(" + st[1] + "," + st[2] + ")' >Custom Sequence</a>";
                        html1 += "</li>";
            <%}%>
            <% if (!userType.equalsIgnoreCase("ANALYZER")) {%>
                        html1 += "<li id='saveGraphLi' style='text-align;'>";
                        html1 += "<a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='openNewReportDtls(\"" + st[0] + "\")' >Save As New Report</a>";
                        html1 += "</li>";
                        html1 += "<li id='saveGraphLi1' style='text-align;'>";
                        html1 += "<a data-color='color1' style='padding:10px 0px 0px 24px;' onclick=overWriteReport1(\"" + st[0] + "\",'overWrite',<%=isRestrictedPowerAnalyserEnable%>,<%=isMultiCompany %>,<%=isPowerAnalyserEnableforUser%>) >Save/Overwrite Report</a>";
                        html1 += "</li>";
                        html1 += "<li id='saveTableGraphLi' style='text-align;'>";
                        html1 += "<a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='saveTableRegion(\"" + st[0] + "\"," + st[2] + ")' >Save Table Data</a>";
                        html1 += "</li>";
            <% }%>
            <% if (!userType.equalsIgnoreCase("ANALYZER")) {%>
//                        html1 += "<li id='saveTableGraphLi' style='text-align;'>";
//                        html1 += "<a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='resetGraphs()' >Reset Graphs</a>";
//                        html1 += "</li>";
            <% }%>
                        html1 += "<li>";
                        html1 += "<a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='OpenDefTabs()'>Default Tab</a>";
                        html1 += "</li>";
                        html1 += "<li id='assginReport' style='text-align;'>";
                        html1 += "<a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='saveNewRepGrpTab1(\"" + st[0] + "\"," + st[4] + "," + st[5] + "," + st[2] + ",\"" + '<%=reportName%>' + "\")'>Assign Report</a>";
                        html1 += "</li>";
                        //                           html1 +="</li></ul>";
                        //               $('#testCase').html("");

                        //                           $('#testCase').html(html1);
                        $('#themeselect2').html(html1);
                    });
                    //                       $("li").filter(function() {
                    //                           return $(this).text() == 'Graphical Analysis';
                    //                       }).click(function() {
                    $("#lmGraph").click(function() {
                        $('#themeselect').empty();
                        $('#saveGraphLi').remove();
                        var add = "add";
                        var edit = "isEdit";
                        var drillType = "multi";
                        var html1 = "";
                        //               var graphFlag = "Graph";
                        //                           html1+='<li id="testCase" class="drpdown menu-item" style="width: 15%"></li><a href="#" style="padding: 1px;width:100%"><span><font size="3" face="verdana" style="font-size: 14px; color: white">Options</font></span></a>';
                        //                           html1 += '<ul class="drpcontent" id="themeselect">';
            <% if (!userType.equalsIgnoreCase("ANALYZER")) {%>
                        html1 += "<li style='text-align;'>";
                        html1 += "<a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='editSingleCharts(\"add1\")' >Add Graph Object</a>";
                        html1 += "</li>";
                        html1 += "<li style='text-align;'>";
                        html1 += "<a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='editSingleCharts(\"edit1\")' >Edit Graph Object</a>";
                        html1 += "</li>";
            <% }%>
                        html1 += "<li style='text-align;'>";
                        html1 += "<a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='addPage()' >Add Page</a>";
                        html1 += "</li>";
                        html1 += "<li style='text-align;'>";
                        html1 += "<a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='buildCharts(\"" + add + "\")' >Add Graph</a>";
                        html1 += "</li>";
                        html1 += "<li style='text-align;'>";
                        html1 += "<a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='buildCharts(\"" + edit + "\")' >Edit Graph</a>";
                        html1 += "</li>";
                        html1 += "<li style='text-align;'>";
                        html1 += "<a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='selectMultiDrill(\"" + drillType + "\")' >Enable Multi Drill</a>";
                        html1 += "</li>";
                        //                           html1 += "<li style='text-align;'>";
                        //                           html1 += "<a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='selectReportDrill()' >Enable/Disable Report Drill</a>";
                        //                           html1 +="</li>";
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
            <% if (!userType.equalsIgnoreCase("ANALYZER")) {%>
                        html1 += "<li id='saveWithGraph' style='text-align;'>";
//                        html1 += "<a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='saveXtCharts()' >Save All Graphs</a>";
                        html1 += "</li>";
            <% }%>
                        html1 += "<li id='regenerateFilters' style='text-align;'>";
                        html1 += "<a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='regenerateFilters(" + parent.$("#graphsId").val() + ")' >Regenerate Filters</a>";
                        html1 += "</li>";
                        html1 += "<li id='exportToPDF' style='text-align;'>";
                        html1 += "<a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='downloadAsPDF()' >Save as PDF</a>";
                        html1 += "</li>";
                        html1 += "</li>";
                        html1 += "<li>";
                        html1 += "<a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='OpenDefTabs()'>Default Tab</a>";
                        html1 += "</li>";
                        html1 += "<li>";
                        html1 += "<a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='resequenceGraphs1()'>Resequence Param</a>";
                        html1 += "</li>";
                        html1 += "<li id='resetId' style='text-align;'>";
                        //               html1 += "<a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='generateJsonData("+parent.$("#graphsId").val()+",Graph,"+gridster1+")' >Reset</a>";
                        html1 += "<a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='generateJsonDataReset(" + parent.$("#graphsId").val() + ")' >Reset</a>";
                        //                           html1 +="</li></ul>";
                        html1 += "</li>";
                        //                           $('#testCase').html(html1);
                        $('#themeselect2').html(html1);
                    });
                    //                       $("li").filter(function() {
                    //                           return $(this).text() == 'Trend Analysis';
                    //                       }).click(function() {
                    $("#lmTrend").click(function() {
                        $('#themeselect').empty();
                        $('#themeselect').remove();
                        $('#saveGraphLi').remove();
                        $('#regenerateFilters').remove();
                        $('#resetId').remove();
                        $('#saveWithGraph').remove();
                        var html1 = "";
                        //               var trendflag = "Trend";
                        //                           html1+='<li id="testCase" class="drpdown menu-item" style="width: 15%"></li><a href="#" style="padding: 1px;width:100%"><span><font size="3" face="verdana" style="font-size: 14px; color: white">Options</font></span></a>';
                        //                           html1 += '<ul class="drpcontent" id="themeselect">';
                        //               html1 += "<li style='text-align;'>";
                        //               html1 += "<a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='editSingleCharts()' >Add Graph Object</a>";
                        //               html1 +="</li>";
                        html1 += "<li style='text-align;'>";
                        html1 += "<a data-color='color1' style='padding:10px 0px 0px 24px;' id ='addGraphDataTrendAnalysis' onclick='buildChartsInTrends(" + parent.$("#graphsId").val() + ")' >Add Trend Charts</a>";
                        html1 += "</li>";
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
                        html1 += "<a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='saveXtCharts()' >Save Trend Charts</a>";
                        html1 += "</li>";
                        html1 += "<li>";
                        html1 += "<a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='OpenDefTabs()'>Default Tab</a>";
                        html1 += "</li>";
                        html1 += "<li id='reset' style='text-align;'>";
                        html1 += "<a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='trendAnalysisActionJsNew(" + parent.$("#graphsId").val() + ")' >Reset</a>";
                        //                           html1 +="</li></ul>";
                        html1 += "</li>";
                        //               $('#themeselect').html("");
                        //$('#testCase').remove(html1);
                        $('#themeselect2').html(html1);
                        //                           $('#testCase').html(html1);
                    });
                    //                       $("li").filter(function() {
                    //                           return $(this).text() == 'Thematic Map/Charts';
                    //                       }).click(function() {
                    $("#lmAdvance").click(function() {

                        $('#themeselect').empty();
                        $('#saveGraphLi').remove();
                        $('#regenerateFilters').remove();
                        $('#resetId').remove();
                        var html1 = "";
                        //                           html1+='<li id="testCase" class="drpdown menu-item" style="width: 15%"></li><a href="#" style="padding: 1px;width:100%"><span><font size="3" face="verdana" style="font-size: 14px; color: white">Options</font></span></a>';
                        //                           html1 += '<ul class="drpcontent" id="themeselect">';
                        html1 += "<li style='text-align;'>";
                        html1 += "<a data-color='color1' style='padding:10px 0px 0px 24px;' id ='addGraphDataTrendAnalysis' onclick='createAdvanceVisual(" + parent.$("#graphsId").val() + ")' >Add Visual</a>";
                        html1 += "</li>";
                        html1 += "<li style='text-align;'>";
                        html1 += "<a data-color='color1' style='padding:10px 0px 0px 24px;' id ='addGraphAnalysis' onclick='editAdvanceVisual(" + parent.$("#graphsId").val() + ")' >Edit Visual</a>";
                        html1 += "</li>";
            <% if (!userType.equalsIgnoreCase("ANALYZER")) {%>
                        html1 += "<li id='advanceVisual' style='text-align;'>";
                        html1 += "<a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='saveXtCharts()' >Save Visual</a>";
                        html1 += "</li>";
            <% }%>
            <% if (!userType.equalsIgnoreCase("ANALYZER")) {%>
                        html1 += "<li id='enableAdvanceDrill' style='text-align;'>";
                        html1 += "<a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='enableDisableDrill()' >Enable/Disable Drill</a>";
                        html1 += "</li>";
            <% }%>
                        html1 += "<li id='reset' style='text-align;'>";
                        html1 += "<a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='getAdvanceVisuals(" + parent.$("#graphsId").val() + ")' >Reset</a>";
                        html1 += "</li>";
                        html1 += "<li id='prop' style='text-align;'>";
                        html1 += "<a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='setProperties(" + parent.$("#graphsId").val() + ")' >Properties</a>";
                        html1 += "</li>";
                        html1 += "<li>";
                        html1 += "<a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='OpenDefTabs()'>Default Tab</a>";
                        html1 += "</li>";
            <% if (!userType.equalsIgnoreCase("ANALYZER")) {%>
                        html1 += "<li id='reset' style='text-align;'>";
                        html1 += "<a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='resequenceGraphs()' >Delete / Resequence</a>";
                        //                             html1 +="</li></ul>";
                        html1 += "</li>";
            <% }%>
                        html1 += "<li id='reset' style='text-align;'>";
                        html1 += "<a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='defineParentChildDB()' >Define Multi Dashboard</a>";
                        //                             html1 +="</li></ul>";
                        html1 += "</li>";
                        //                          html1 +="</ul>";
                        $('#themeselect2').html(html1);
                        //                          $('#testCase').html(html1);
                        //End Ram code
                    });
                    $('.menu-item a').click(function() {
                        $('.menu-item a').removeClass('active');
                        $(this).addClass('active');
                    });
                    //           var screenh=screen.height;
                    //           screenh=screenh*.56;
                    //           $("#tabel1").css('height', screenh);
                    //                    var iframe = document.getElementById("iframe1");
                    //                    var iframeContent = (iframe.contentWindow || iframe.contentDocument);
                    //                    iframeContent.loadingMenu(text);
                    $("#lmTable img").addClass("imgShadow");
                    // var gr='graph'; added by krishan
                    //opencustomtab('<%//=defaulttab%>');
            <% if (defaulttab != null && !defaulttab.equalsIgnoreCase("")) {%>
                    opencustomtab("<%=defaulttab%>");
            <% }%>
                    var widthe = $("#list-container").width();
                    var widthetr = $("#list-containertr").width();
                    var widthead = $("#list-containerad").width();
                    var $item = $('div.item'), //Cache your DOM selector
                            visible = 1, //Set the number of items that will be visible
                            index = 0, //Starting index
                            endIndex = ($item.length / visible) - 1; //End index
                    $('div#arrowR').click(function() {
                        if (index < endIndex) {
                            index++;
                            $item.animate({'left': '-=' + widthe});
                        }
                    });
                    $('div#arrowL').click(function() {
                        if (index > 0) {
                            index--;
                            $item.animate({'left': '+=' + widthe});
                        }
                    });
                    var $itemtr = $('div.itemtr'), //Cache your DOM selector
                            visibletr = 1, //Set the number of items that will be visible
                            indextr = 0, //Starting index
                            endIndextr = ($itemtr.length / visibletr) - 1; //End index
                    $('div#arrowRtr').click(function() {
                        if (indextr < endIndextr) {
                            indextr++;
                            $itemtr.animate({'left': '-=' + widthetr});
                        }
                    });
                    $('div#arrowLtr').click(function() {
                        if (indextr > 0) {
                            indextr--;
                            $itemtr.animate({'left': '+=' + widthetr});
                        }
                    });
                    var $itemad = $('div.itemad'), //Cache your DOM selector
                            visiblead = 1, //Set the number of items that will be visible
                            indexad = 0, //Starting index
                            endIndexad = ($itemad.length / visiblead) - 1; //End index
                    $('div#arrowRad').click(function() {
                        if (indexad < endIndexad) {
                            indexad++;
                            $itemad.animate({'left': '-=' + widthead});
                        }
                    });
                    $('div#arrowLad').click(function() {
                        if (indexad > 0) {
                            indexad--;
                            $itemad.animate({'left': '+=' + widthead});
                        }
                    });
                    var $itemtable = $('div.itemtable'), //Cache your DOM selector
                            visibletable = 1, //Set the number of items that will be visible
                            indextable = 0, //Starting index
                            endIndextable = ($itemtable.length / visibletable) - 1; //End index
                    $('div#arrowRtable').click(function() {
                        if (indextable < endIndextable) {
                            indextable++;
                            $itemtable.animate({'left': '-=' + widthe});
                        }
                    });
                    $('div#arrowLtable').click(function() {
                        if (indextable > 0) {
                            indextable--;
                            $itemtable.animate({'left': '+=' + widthe});
                        }
                    });
            <%if (gblsequnce != "" || gblsequnce.equalsIgnoreCase("gblsequnce")) {%>
                    $('#rightDiv1table').hide();
                    if (checkBrowser() == "ie") {

                        $('#rightDiv1').hide();
                        $("#xtendChartssTD").css({'margin-top': '0px'});
                    } else {
                        $('#rightDiv1').show();
                    }

            <%} else {
           if (filterShow != "" && filterShow.equalsIgnoreCase("true")) {%>

                    if (checkBrowser() == "ie") {

                        $('#rightDiv1table').hide();
                        $("#xtendChartssTD").css({'margin-top': '0px'});
                    } else {
                        $('#rightDiv1table').show();
                    }
            <%} else {%>
                    $('#rightDiv1table').hide();
            <%}%>
            <%}%>
                    //     $('#rightDiv1table').show();
                    $("#shareReport").dialog({
                        autoOpen: false,
                        height: 350,
                        width: 400,
                        position: 'justify',
                        modal: true,
                        resizable: false
                    });
                    $("#ScheduleReport").dialog({
                        autoOpen: false,
                        height: 350,
                        width: 500,
                        position: 'justify',
                        modal: true
                    });

                    $("#initializeRepDiv").dialog({
                     autoOpen: false,
                     height: 500,
                     width: 450,
                     position: 'justify',
                     modal: true,
                     resizable:true
                });
                    $("#setGORefresh").dialog({
                        autoOpen: false,
                        modal: true,
                        width: 350,
                        height: 150,
                        draggable: true,
                        resizable: true,
                        position: [480, 150]
                    });
                    if($("#managementTempl").is(":visible")){$("#gridsterDiv").height($(window).height()-300);}
                    else{$("#gridsterDiv").height($(window).height() - 220 + "px");}
                    
                });
                function editMeasure1() {
                    alert("This Option Will Be Coming Soon.")
                }

                function logout() {
                    document.forms.searchForm.action = "<%=request.getContextPath()%>/baseAction.do?param=logoutApplication";
                    document.forms.searchForm.submit();
                }
                function selectDrill(drillType, id) {
                    //                      alert(drillType)
                    var chartData = JSON.parse($("#chartData").val());
                    chartData[id]["drillType"] = drillType;
                    $("#drilltype").val(drillType);
                    if (drillType == "reportDrill") {
                        graphToReportDrill("<%=request.getContextPath()%>", "<%=reportId1%>", id);
                    }
                    else if (drillType == "single") {
                        alert("Drill Across Enabled");
                    }
                    else if (drillType == "within") {
                        alert("Drill Down Enabled");
                    }
                    else if (drillType == "pageDrill") {
                        pageDrill(id);
                    }else if (drillType == "action") {
                        $("#chartData").val(JSON.stringify(chartData));
                        setActionDrillCharts(id);
//                        alert("Action Drill Enabled");
                    }
                    $("#chartData").val(JSON.stringify(chartData));
                }

                function selectMultiDrill(drillType) {
                    var chartData = JSON.parse($("#chartData").val());
                    $("#drilltype").val(drillType);
                    var chartIds = Object.keys(chartData);
                    for (var i = 1; i <= chartIds.length; i++) {

                        var chartId = "chart" + i;
                        chartData[chartId]["drillType"] = drillType;
                    }
                    alert("Multi Drill Enabled");
                    $("#chartData").val(JSON.stringify(chartData));
                }
                function selectReportDrill() {
                    reportDrillAssignment("<%=request.getContextPath()%>", "<%=reportId1%>");
                    var drillType = "reportDrill";
                    var chartData = JSON.parse($("#chartData").val());
                    //                      $("#drilltype").val(drillType);
                    var chartIds = Object.keys(chartData);
                    for (var i = 1; i <= chartIds.length; i++) {

                        var chartId = "chart" + i;
                        chartData[chartId]["reportDrill"] = drillType;
                    }
                    alert("Multi Drill Enabled");
                    $("#chartData").val(JSON.stringify(chartData));
                }

                function enableDisableDrill() {
                    var chartData = JSON.parse($("#chartData").val());
                    var chartIds = Object.keys(chartData);
                    var chartId;
                    var drillType = "enableGraphDrill";
                    for (var i = 1; i <= chartIds.length; i++) {
                        chartId = "chart" + i;
                        if (typeof chartData[chartId]["enableGraphDrill"] !== "undefined") {
                            chartData[chartId]["enableGraphDrill"] = "";
                            if (i == chartIds.length)
                                alert("Graph Drill Disabled");
                        } else {
                            chartData[chartId]["enableGraphDrill"] = drillType;
                            if (i == chartIds.length)
                                alert("Graph Drill Enabled");
                        }
                    }
                    $("#chartData").val(JSON.stringify(chartData));
                }

                function graphToReportDrill(ctxPath, reportId, chartId) {
                    var chartData = JSON.parse(parent.$("#chartData").val());
                    var graphDrillMap=chartData[chartId]["graphDrillMap"];
                    $.post(ctxPath + '/reportViewer.do?reportBy=reportDrillAssignment&reportId=' + reportId,
                            function(data) {
                                if (data != 'null') {
                                    var jsonVar = eval('(' + data + ')')
                                    console.log("jsonVar"+JSON.stringify(jsonVar))
                                    var roleName = jsonVar.roleName;
                                    var MsrIds = jsonVar.MsrIds;
                                    var MsrNames = jsonVar.MsrNames;
                                    var reportIds = jsonVar.reportIds;
                                    var reportNames = jsonVar.reportNames;
                                    var assignRepIds = jsonVar.assignRepIds;
                                    var htmlVar = "";
                                    htmlVar += "<table width='100%'><tr style='display:none'><td width='40%'><input type='radio' name='reportselection' id='singlereport' onclick=\"selectSingleReport('" + MsrIds + "')\" value='single report' checked>Single Report</td>";
                                    htmlVar += "<td colspan='2' width='60%'><input type='radio' name='reportselection' id='multireport' onclick=\"selectMultieReport('" + MsrIds + "')\" value='multi report'>Multi Report</td></tr></br>";
                                    htmlVar += "<tr><td style='color:#fff'>.</td><td align='center' class='gFontFamily  fontWeightBold' colspan='3'>" + roleName[0] + " Role</td></tr>";
                                    htmlVar += "<tr><td style='color:#fff'>.</td><td width='90%' align='center' class='gFontFamily ' style='background-color:#b4d9ee;'>ReportName</td></tr>";
                                   if(typeof chartData !=="undefined" && chartData[chartId]["chartType"]==="Combo-Analysis"){
                                       for (var i = 0; i < 5; i++) {
                                        //                    htmlVar+="<tr><td><input type='text' value='"+MsrNames[i]+"' style='background-color:white' readonly=''id='"+MsrIds[i]+"' name='msrName'></td><td><input type='hidden' value='"+MsrIds[i]+"' style='background-color:white' id='"+MsrIds[i]+"' name='msrId'></td>";
                                       if(i==3){
                                       htmlVar += "<tr><td class='gFontFamily  fontWeightBold'>Header 1</td><td id='singleReportTd" + MsrIds[i] + "'><select id='reportToDrill" + (i+1)+"_" + chartId + "' style='width:100%;' name='reportToDrill'>";

                                       }else if(i==4){
                                          htmlVar += "<tr><td class='gFontFamily  fontWeightBold'>Header 2</td><td id='singleReportTd" + MsrIds[i] + "'><select id='reportToDrill" + (i+1)+"_" + chartId + "' style='width:100%;' name='reportToDrill'>";
                                       }
                                       else{
                                           htmlVar += "<tr><td class='gFontFamily  fontWeightBold'>chart"+parseInt(i+1)+"</td><td id='singleReportTd" + MsrIds[i] + "'><select id='reportToDrill" + (i+1)+"_" + chartId + "' style='width:100%;' name='reportToDrill'>";
                                       }
                                        if (assignRepIds[i] == '0')
                                            htmlVar += "<option selected class='gFontFamily ' value='0'>NOT_SELECTED</option>";
                                        else
                                            htmlVar += "<option class='gFontFamily ' value='0'>NOT_SELECTED</option>";
                                        for (var j = 0; j < reportIds.length; j++) {
                                            if (assignRepIds[i] == reportIds[j])
                                                htmlVar += "<option selected value='" + reportIds[j] + "'>" + reportNames[j] + "</option>";
                                            else
                                                htmlVar += "<option value='" + reportIds[j] + "'>" + reportNames[j] + "</option>";
                                        }
                                        htmlVar += "</select></td>";
                                        //                       htmlVar+="<td style id='multiReportTd"+MsrIds[i]+"' style='display:none;'><input type='text' value='' onclick=\"getmultiReportIds('"+ctxPath+"','"+reportId+"',this.id)\" style='background-color:white;width:150px;' id='multireport"+MsrIds[i]+"' name='multireportNames'></td>";
                                        htmlVar += "</tr>";
                                        //                       htmlVar+="<tr><td colspan='3'><input type='hidden' value='0' style='background-color:white' id='multireportId"+MsrIds[i]+"' name='multireportIds'></td></tr>";
                                    }
                                   }else {
                                         for (var i = 0; i < 2; i++) {
                                        //                    htmlVar+="<tr><td><input type='text' value='"+MsrNames[i]+"' style='background-color:white' readonly=''id='"+MsrIds[i]+"' name='msrName'></td><td><input type='hidden' value='"+MsrIds[i]+"' style='background-color:white' id='"+MsrIds[i]+"' name='msrId'></td>";
                                       if(i==0){
                                       htmlVar += "<tr><td class='gFontFamily  fontWeightBold'>Header</td><td id='singleReportTd" + MsrIds[i] + "'><select id='reportToDrill" + (i+1)+"_" + chartId + "' style='width:100%;' name='reportToDrill'>";
                                       }else{

                                       htmlVar += "<tr><td class='gFontFamily  fontWeightBold'>"+chartId+"</td><td id='singleReportTd" + MsrIds[i] + "'><select id='reportToDrill" + (i+1)+"_" + chartId + "' style='width:100%;' name='reportToDrill'>";
                                       }
                                       var index='';
                                       if(i==0){
                                           index='';
                                       }
                                       else{
                                           index=i;
                                       }
                                        if (typeof graphDrillMap!=='undefined' && typeof graphDrillMap["newReportId"+index]!=='undefined')
                                            htmlVar += "<option selected class='gFontFamily ' value='0'>NOT_SELECTED</option>";
                                        else
                                            htmlVar += "<option class='gFontFamily ' value='0'>NOT_SELECTED</option>";
                                        for (var j = 0; j < reportIds.length; j++) {
                                            if (typeof graphDrillMap!=='undefined' && typeof graphDrillMap["newReportId"+index]!=='undefined' && graphDrillMap["newReportId"+index] == reportIds[j]){
                                                htmlVar += "<option selected value='" + reportIds[j] + "'>" + reportNames[j] + "</option>";
                                            }
                                            else{
                                                htmlVar += "<option value='" + reportIds[j] + "'>" + reportNames[j] + "</option>";
                                        }
                                        }
                                        htmlVar += "</select></td>";
                                        //                       htmlVar+="<td style id='multiReportTd"+MsrIds[i]+"' style='display:none;'><input type='text' value='' onclick=\"getmultiReportIds('"+ctxPath+"','"+reportId+"',this.id)\" style='background-color:white;width:150px;' id='multireport"+MsrIds[i]+"' name='multireportNames'></td>";
                                        htmlVar += "</tr>";
                                        //                       htmlVar+="<tr><td colspan='3'><input type='hidden' value='0' style='background-color:white' id='multireportId"+MsrIds[i]+"' name='multireportIds'></td></tr>";
                                    }
                                   }

                                    htmlVar += "<tr><td>&nbsp;</td></tr><tr><td colspan='3'  align='center'><input type='button' value='DONE' class='navtitle-hover gFontFamily '  style='width:auto' onclick=\"saveDrillAssignGraphToReport('" + reportId + "','" + chartId + "')\"></td></tr>";
                                    htmlVar += "</table>";
                                    parent.$("#reportDrillFrm").html(htmlVar);
                                    parent.$("#reportDrillDiv").dialog().dialog('open');
                                }

                            });
                }
                function saveDrillAssignGraphToReport(reportId, chartId) {
                    var chartData = JSON.parse($("#chartData").val());
                    var newReportId = $("#reportToDrill" + chartId).val();
                    var graphDrillMap = {};
                    if(typeof chartData !=="undefined" && chartData[chartId]["chartType"]==="Combo-Analysis"){
                        var newReportId = $("#reportToDrill1_" + chartId).val();
                        var newReportId1 = $("#reportToDrill2_" + chartId).val();
                        var newReportId2 = $("#reportToDrill3_" + chartId).val();
                        var newReportId3 = $("#reportToDrill4_" + chartId).val();
                        var newReportId4 = $("#reportToDrill5_" + chartId).val();
                       graphDrillMap["reportId"] = reportId;
                    graphDrillMap["newReportId"] = newReportId;
                    graphDrillMap["newReportId1"] = newReportId1;
                    graphDrillMap["newReportId2"] = newReportId2;
                    graphDrillMap["newReportId3"] = newReportId3;
                    graphDrillMap["newReportId4"] = newReportId4;
                    }else{
                         var newReportId = $("#reportToDrill1_" + chartId).val();
                          var newReportId1 = $("#reportToDrill2_" + chartId).val();
                    graphDrillMap["reportId"] = reportId;
                    graphDrillMap["newReportId"] = newReportId;
                    graphDrillMap["newReportId1"] = newReportId1;
                    }

                    chartData[chartId]["graphDrillMap"] = graphDrillMap;
                    $("#chartData").val(JSON.stringify(chartData));
                    var advanceType = parent.$("#advanceChartType").val();
//                    alert(advanceType)
                    if(typeof advanceType !=="undefined" && (advanceType=="advanceGraph" || advanceType=="")){
//                          window.complexId = chartId;
     var advanceChartData;
    try{
        
    advanceChartData = JSON.parse(parent.$("#advanceChartData").val());
    }catch(e){
        
    }       
    if(typeof advanceChartData !=="undefined"){
       var advanceDetails = advanceChartData[window.complexId]["chart5"]; 
       var chartDetails = chartData["chart5"];
       advanceDetails["graphDrillMap"]=chartDetails["graphDrillMap"];
//       alert(JSON.stringify(advanceChartData))
       parent.$("#advanceChartData").val(JSON.stringify(advanceChartData));
       parent.$("#chartData").val(JSON.stringify(chartData));
    }
                        
                        }
                    parent.$("#reportDrillDiv").dialog('close');
                    alert("Reports Assigned Succesfully")
                }
                //added by krishan
                var counttt = 0;
                function opencustomtab(text) {
                    window.current_Tab_Flag =text;
                    //           }
                    if (text == 'Report') {
                        $("#lmGraph img").removeClass("imgShadow");
                        $("#lmTrend img").removeClass("imgShadow");
                        $("#lmAdvance img").removeClass("imgShadow");
                        $("#lmTable img").addClass("imgShadow");
//                        var iframe = document.getElementById("iframe1");
//                        var iframeContent = (iframe.contentWindow || iframe.contentDocument);
//
//                        setTimeout(function(){ iframeContent.hideGraphSec(text); }, 2000);
                        $("#tableOption").css({'display': ''});
                        //                        $("#menuBarUITR").css({'display':''});
                        $('#SwHiFil').show();
                        $('#menuBarUI').show();
                        $("#reportTD1").show();
                        $("#xtendChartTD").hide();
                        $("#xtendChartssTD").hide();
                        $('#addUnderConsideration').hide();
                        $("#footerTable").css({'display': 'block'});
                        $("#reportTD1").css({'border': ''});
                        $("#companySection").css({'display': ''});
                    }
                    else if (text == 'Graph') {

                        $("#visualGO").html('');
                        var url = "reportViewer.do?reportBy=viewReport&action=paramChange&DEFAULT_TAB=Graph&current_defaulttab=" + current_defaulttab + "&current_reportId=" + current_reportId;
                        var html1 = '<input  type="button" class="navtitle-hover" value=" Go "  style="height: 20px; width: 25px" onclick="submitformGraph(\'' + url + '\')"/>'
                        $("#visualGO").html(html1);
                        $("#dateToggle").html('');
                        var url1 = "&DEFAULT_TAB=Graph";
                        var html1 = '<a href="javascript:void(0)" class="ui-icon ui-icon-transferthick-e-w" title="Toggle" onclick="datetoggleForGraph(\'' + url1 + '\')"></a>';
                        $("#dateToggle").html(html1);
                        $("#lmTrend img").removeClass("imgShadow");
                        $("#lmAdvance img").removeClass("imgShadow");
                        $("#lmTable img").removeClass("imgShadow");
                        $("#lmGraph img").addClass("imgShadow");
                        $("#loading").show();
//                        var iframe = document.getElementById("iframe1");
//                        var iframeContent = (iframe.contentWindow || iframe.contentDocument);
//                        //                    alert(iframeContent)
//                        setTimeout(function(){ iframeContent.hideGraphSec(text); }, 2000);
//                        //                    iframeContent.hideGraphSec(text);
                        $("#tableOption").css({'display': 'none'});
                        $("#reportTD1").css({'border': 'hidden'});
                        $("#companySection").css({'display': 'none'});
                        counttt++;
//                        $("#footerTable").hide();
                        $("#SwHiFil").hide();
                        var graphFlag = "Graph";
                        $('#addUnderConsideration').hide();
                        $('#xtendChartTD').hide();
                        $('#menuBarUI').hide();
                        //            $("#reportTD1").show();
                        $("#noneDataDiv").css({'display': 'none'});
                                    $("#menuBarUITR").css({'display':'none'});
                        //             document.getElementById("paramHeaderId").style.display='none';
                        $("#footerTable").css({'display': 'block'});
                        $("#gridsterDiv").css({'display': 'block'});
                        if($("#managementTempl").is(":visible")){$("#gridsterDiv").height($(window).height()-300);}
                        else{$("#gridsterDiv").height($(window).height() - 220 + "px");}
                        if (checkBrowser() == "ie") {

                            $('#rightDiv1').hide();
                            $("#xtendChartssTD").css({'margin-top': '0px'});
                        } else {
                            $('#rightDiv1').show();
                        }


                        //     $('#globalfilterrow').show();
                        $('#rightDiv1ad').hide();
                        $('#rightDiv1table').hide();
                        $('#rightDiv1trend').hide();
                        //goRefresh('<%=request.getParameter("REPORTID")%>');

                        if (counttt == 1) {
                            generateJsonData1st('<%=request.getParameter("REPORTID")%>', graphFlag, gridster1);
                        } else {
                            $("#gridsterDiv").css({'display': 'block'});
                            generateJsonData('<%=request.getParameter("REPORTID")%>', graphFlag, gridster1);
//                            generateJsonGlobal('<%=request.getParameter("REPORTID")%>', graphFlag, gridster1);
                        }
                        $("#noneDataDiv").css({'display': 'none'});
                    }
                    else if (text == 'AdvanceVisuals') {
                        $('#menuBarUI').hide();
                        $("#visualGO").html('');
                        var url = "reportViewer.do?reportBy=viewReport&action=paramChange&DEFAULT_TAB=AdvanceVisuals&current_defaulttab=" + current_defaulttab + "&current_reportId=" + current_reportId;
                        var html = '<input  type="button" class="navtitle-hover" value=" Go "  style="height: 20px; width: 25px" onclick="submitformGraph(\'' + url + '\')"/>'
                        $("#visualGO").html(html);
                        $("#dateToggle").html('');
                        var url1 = "&DEFAULT_TAB=AdvanceVisuals";
                        var html1 = '<a href="javascript:void(0)" class="ui-icon ui-icon-transferthick-e-w" title="Toggle" onclick="datetoggleForGraph(\'' + url1 + '\')"></a>';
                        $("#dateToggle").html(html1);
                        $("#lmTable img").removeClass("imgShadow");
                        $("#lmGraph img").removeClass("imgShadow");
                        $("#lmTrend img").removeClass("imgShadow");
                        $("#lmAdvance img").addClass("imgShadow");
                        //    $("#reportTD1").hide();
                        //                        $("#menuBarUITR").css({'display':'none'});
//                        var iframe = document.getElementById("iframe1");
//                        var iframeContent = (iframe.contentWindow || iframe.contentDocument);
//
//                        setTimeout(function(){ iframeContent.hideGraphSec(text); }, 10000);
                        $("#tableOption").css({'display': 'none'});
                        $("#footerTable").css({'display': 'block'});
                        $("#gridsterDiv").css({'display': 'none'});
                        $("#xtendChartssTD").css({'display': 'none'});
                        $("#reportTD1").css({'border': 'hidden'});
                        $("#companySection").css({'display': 'none'});
                        $("#SwHiFil").hide();
                        $("#content_1").hide();
                        var graphFlag = "Graph";
                        $("#loading").show();
                        getAdvanceVisuals('<%=request.getParameter("REPORTID")%>', graphFlag);
                    }
                    else if (text == 'Trends') {
                        $("#visualGO").html('');
                        var url = "reportViewer.do?reportBy=viewReport&action=paramChange&DEFAULT_TAB=Trends";
                        var html = '<input  type="button" class="navtitle-hover" value=" Go "  style="height: 20px; width: 25px" onclick="submitformGraph(\'' + url + '\')"/>'
                        $("#visualGO").html(html);
                        $("#dateToggle").html('');
                        var url1 = "&DEFAULT_TAB=Trends";
                        var html1 = '<a href="javascript:void(0)" class="ui-icon ui-icon-transferthick-e-w" title="Toggle" onclick="datetoggleForGraph(\'' + url1 + '\')"></a>';
                        $("#dateToggle").html(html1);
                        $("#lmAdvance img").removeClass("imgShadow");
                        $("#lmTable img").removeClass("imgShadow");
                        $("#lmGraph img").removeClass("imgShadow");
                        $("#lmTrend img").addClass("imgShadow");
                        $("#footerTable").css({'display': 'block'});
                        var trendFlag = "Trend";
                        var flag = "trendAnalysisAction";
                        var Measures = "trendAnalysisActionMeasures";
                        $("#gridsterDiv").css({'display': 'block'});
                        $("#xtendChartssTD").css({'display': 'none'});
                        $("#companySection").css({'display': 'none'});
                        //                        $("#menuBarUITR").css({'display':'none'});
//                        var iframe = document.getElementById("iframe1");
//                        var iframeContent = (iframe.contentWindow || iframe.contentDocument);
//                        setTimeout(function() {
//                            iframeContent.hideGraphSec(text);
//                        }, 10000);
                        $("#tableOption").css({'display': 'none'});
                        $("#footerTable").css({'display': 'block'});
                        $("#reportTD1").css({'border': 'hidden'});
                        $('#SwHiFil').hide();
                        $('#xtendChartTD').hide();
                        $('#menuBarUI').hide();
                        $("#content_1").show();
                        $("#loading").show();
                        trendAnalysisActionJs('<%=request.getParameter("REPORTID")%>', trendFlag, gridster1);
                    }

                }

                function modlistDisp() {
                    $("#modules").toggle();
                }
                function modlistDisp1() {
                    $("#modules").show();
                }
                function modlistclose() {
                    $("#modules").hide();
                }
                function closeStart() {
                    $("#navigateDialog").dialog('close');
                }
                var userID = '<%=userid%>';
                function openstartpage() {
                    modlistclose();
                    var frameObj = document.getElementById("startPageFrame");
                    frameObj.src = "loginStart.jsp?checkUser=" + userID + "&fromPage=startpage";
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
                function showHideFil() {
                    if (checkBrowser() == "ie") {

                        $('#rightDiv1table').hide();
                        $("#xtendChartssTD").css({'margin-top': '0px'});
                    } else {
                        $('#rightDiv1table').toggle();
                    }

                }
                //added by Dinanath for save Locale collect object ddddd
                function getCollectandContainerObjectInfo(message)
                {
                    var htmlfile = "";
                    var reportId = '<%=reportId1%>';
                    var userId = '<%=userId%>';
                    $.ajax({
                        type: 'POST',
                        async: false,
                        cache: false,
//                        timeout: 30000,
                        url: "<%=request.getContextPath()%>/reportViewerAction.do?reportBy=getAllCollectObjectCreatedFile&userId=" + userId + "&reportId=" + reportId + "&action=" + message,
                        success: function(data)
                        {
                            htmlfile = data;
                        }
                    });
                    $("#createMultiSavepointRep").html('');
                    $("#createMultiSavepointRep").html(htmlfile);
                    $("#createMultiSavePointForReport").dialog('open');
                }

                function saveCollectObjectInLocalFile2() {
                    var fileNameLocalObj = $('input[type=radio][name=savepointfilename]:checked').attr('id');
                    var overriteOrNew = $("input[type=radio][name=savepointfilename]:checked").val();
                    var usrSavepointName = $("input[type=text][name=yourSavepointName]").val();
                    var reportId = '<%=reportId1%>';
                    var userId = '<%=userId%>';
                    var rowCount = $('#noOfCollectObject tr').length;
                    rowCount = Number(rowCount) - 4;
                    document.getElementById('noOfCollectObjectMessage').innerHTML = '';
                    if (rowCount > 3 && overriteOrNew.toString().contains('New'))
                        document.getElementById('noOfCollectObjectMessage').innerHTML = 'You can not create savepoint more than ' + (rowCount - 1);
                    else{
                         var checkedornot=$("input[type=radio][name=savepointfilename]").is(":checked");
                        if(checkedornot==true){
                        $.ajax({
                            type: 'POST',
                            async: false,
                            cache: false,
//                            timeout: 30000,
                            url: "<%=request.getContextPath()%>/reportViewerAction.do?reportBy=saveCollectObjectInLocalFile&userId=" + userId + "&reportId=" + reportId + "&fileName=" + fileNameLocalObj + "&status=" + overriteOrNew +"&usrSavepointName="+usrSavepointName,
                            success: function(data)
                            {
                                $("#createMultiSavePointForReport").dialog('close');
//                                alert(data)
                                window.location.href = "<%=request.getContextPath()%>/reportViewer.do?reportBy=viewReport&REPORTID=" + reportId + "&action=open";
                            }
                        });
                        }else{
                        alert("Please select option!");
                        return false;
                    }
                }
                }
                function setDefaultCollectSavepoint() {
                    var savepoint_name = $("input[type=radio][name=setDefaultCollect]:checked").val();
                    var reportId = '<%=reportId1%>';
                    var userId = '<%=userId%>';
                    var checkedorNot = $("input[type=radio][name=setDefaultCollect]").is(":checked");
                    if(checkedorNot==true){
                    $.ajax({
                        type: 'POST',
                        async: false,
                        cache: false,
//                        timeout: 30000,
                        url: "<%=request.getContextPath()%>/reportViewerAction.do?reportBy=setDefaultCollectSavepoint&userId=" + userId + "&reportId=" + reportId + "&savepointName=" + savepoint_name,
                        success: function(data)
                        {
                            $("#createMultiSavePointForReport").dialog('close');
//                            alert(data)
                            window.location.href = "<%=request.getContextPath()%>/reportViewer.do?reportBy=viewReport&REPORTID=" + reportId + "&action=open";
                        }
                    });
                    }else{
                    alert("Please select option");
                    return false;
                }
                }
                function changelist()
                {
                    $("#list-container").css({'width': '98vw'});
                }
        </script>

             <style>
        .myButton {
	-moz-box-shadow: 0px 10px 14px -7px #3dc21b;
	-webkit-box-shadow: 0px 10px 14px -7px #3dc21b;
	box-shadow: 0px 10px 14px -7px #3dc21b;
	background:-webkit-gradient(linear, left top, left bottom, color-stop(0.05, #44c767), color-stop(1, #5cbf2a));
	background:-moz-linear-gradient(top, #44c767 5%, #5cbf2a 100%);
	background:-webkit-linear-gradient(top, #44c767 5%, #5cbf2a 100%);
	background:-o-linear-gradient(top, #44c767 5%, #5cbf2a 100%);
	background:-ms-linear-gradient(top, #44c767 5%, #5cbf2a 100%);
	background:linear-gradient(to bottom, #44c767 5%, #5cbf2a 100%);
	filter:progid:DXImageTransform.Microsoft.gradient(startColorstr='#44c767', endColorstr='#5cbf2a',GradientType=0);
	background-color:#44c767;
	-moz-border-radius:3px;
	-webkit-border-radius:3px;
	border-radius:3px;
	border:1px solid #18ab29;
	display:inline-block;
	cursor:pointer;
	color:#ffffff;
	font-family:Arial;
	font-size:13px;
	font-weight:bold;
	padding:8px 22px;
        /*margin-top: 2%;*/
        /*margin-left: 10%;*/
	text-decoration:none;
	text-shadow:0px 1px 0px #45e6b0;
}
.myButton:hover {
	background:-webkit-gradient(linear, left top, left bottom, color-stop(0.05, #5cbf2a), color-stop(1, #44c767));
	background:-moz-linear-gradient(top, #5cbf2a 5%, #44c767 100%);
	background:-webkit-linear-gradient(top, #5cbf2a 5%, #44c767 100%);
	background:-o-linear-gradient(top, #5cbf2a 5%, #44c767 100%);
	background:-ms-linear-gradient(top, #5cbf2a 5%, #44c767 100%);
	background:linear-gradient(to bottom, #5cbf2a 5%, #44c767 100%);
	filter:progid:DXImageTransform.Microsoft.gradient(startColorstr='#5cbf2a', endColorstr='#44c767',GradientType=0);
	background-color:#5cbf2a;
}
.myButton:active {
	position:relative;
	top:1px;
}
   </style>                    

        <!--           <table style="width:100%;margin-left:">
                  <tr valign="top">

                            <td valign="top" style="height:60px;width:70%;" align="left">
        <%if (companyId != null && !companyId.equalsIgnoreCase("null") && !companyId.equalsIgnoreCase("")) {
                if (compLogo != null && !compLogo.equalsIgnoreCase("null") && !compLogo.equalsIgnoreCase("")) {

//added by manik for pi logo alignment
                    if (LOGINID.equalsIgnoreCase("progen")) {%>
<a target="_blank" href="http://<%=leftWebSiteUrl%>"> <img alt="" border="0px"  width="40px" height="30px"  title=<%=compTitle%> src="<%=request.getContextPath()%>/images/<%=compLogo%>"/></a>

        <%} else {%>
                            <a target="_blank" href="http://<%=leftWebSiteUrl%>"> <img alt="" border="0px"  width="40px" height="30px"  title=<%=compTitle%> src="<%=request.getContextPath()%>/images/<%=compLogo%>"/></a>
        <%}
            }
        } else if (!isCompanyValid) {%>
             <a target="_blank" href="<bean:message key="leftwebsite.url"/>"> <img alt="" border="0px"  width="40px" height="30px"  title="<bean:message key="piLogo.Title"/>" src="<%=request.getContextPath()%>/images/pi_logo.png"/></a>

        <%}%>
        <span style="font-size:10px;color:#2191C0;font-weight:bold;text-decoration: none;"> Welcome,<strong style="color:#008000;font-size:12px"> <%=session.getAttribute("LOGINID")%> </strong></span>
     </td>
    <td valign="top" style="height:30px;width:40%" >

     </td>
                         <td  style="width:2%;padding-top: 15px" align="right">
           <a class="noteicon" title="Modules" style="text-decoration: none;" onmouseout="modlistclose()" onmouseover="modlistDisp1()" onclick="modlistDisp()" href="javascript:void(0)"><img alt="" height="22px" width="22px"  border="o" src="images/iconnote.gif"></a>

<a class="noteicon" title="" style="text-decoration: none;" onmouseout="modlistclose()" onmouseover="modlistDisp1()" onclick="modlistDisp()" href="javascript:void(0)"><img alt="" height="22px" width="22px"  border="o" src="images/home_landing.png"></a>

             <div id="modules" onmouseover="modlistDisp1()"  style="display:none;width:125px;height:auto;background-color:#FCFCFC;overflow: visible;position:absolute;text-align:left;z-index: 9999999;margin-left: 0px;margin-top: 0px;border-left: 1px solid black">
                              <table border='0' align='left' style="margin-left: 6px" >
                                  <tr><td>
        <%--<table><tr><td><a href='javascript:void(0)' style="text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;" onclick="addIcal()" >Add New I-cal</a></td></tr></table>--%>
        <table><tr><td><a href="javascript:void(0)" onclick="goPaths('<%=homeVar%>')" title="Home"  style="font-size: 11px;color:black"> Home </a></td></tr></table>
        <%if (homeVar != null && homeVar.equalsIgnoreCase("landingPage.jsp")) {%>
       <table><tr><td><a href="landingPage.jsp" onclick="" title="Landing Page"  style="font-size: 11px;color:black"> Landing Page </a></td></tr></table>
        <%}%>
      <table><tr><td><a href="home.jsp" onclick="" title="Home"  style="font-size: 11px;"> Favourite Reports </a></td></tr></table>
        <%if (homeVar != null && homeVar.equalsIgnoreCase("newHome.jsp")) {%>
       <table><tr><td><a href="javascript:void(0)" onclick="goPaths('home.jsp#RolesTab')" title="Business Roles"  style="font-size: 11px;color:black"> Business Roles </a></td></tr></table>
        <%}%>
        <table><tr><td><a href="javascript:void(0)" onclick="goPaths('home.jsp#All_Reports')" style="font-size: 11px;color:black">My Reports</a></td></tr></table>
        <%--<table><tr><td><a href='javascript:void(0)' style="text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;" onclick="enableComp()" >Enable Comparision</a></td></tr></table> --%>
        <% if (isPowerAnalyserEnableforUser || userType.equalsIgnoreCase("ADMIN")) {%>
        <table><tr><td><a href="javascript:void(0)" onclick="goPaths('home.jsp#Dashboard_Studio')" style="font-size: 11px;color:black">Dashboard Studio</a></td></tr></table>
        <table><tr><td><a href="javascript:void(0)" onclick="goPaths('home.jsp#Report_Studio')" style="font-size: 11px;color:black">Report Studio</a></td></tr></table>
        <table><tr><td><a href="javascript:void(0)" onclick="goSearchpage()" style="font-size: 11px;color:black">Search</a></td></tr></table>
        <% }
            if (isQDEnableforUser) {%>
        <table><tr><td><a href="javascript:void(0)" onclick="gotoDBCON('<%=request.getContextPath()%>')" title="Query Studio" style="font-size: 11px;color:black">Query Studio</a></td></tr></table>
        <% }%>
      <table><tr><td><a href="javascript:void(0)" onclick="goPortal()">Portals</a></td></tr></table>
      <table><tr><td><a href="javascript:void(0)" onclick="goPaths('home.jsp#Scorecard')">Scorecard</a></td></tr></table>
       <table><tr><td><a href="javascript:void(0)" onclick="goSearchpage()" style="font-size: 11px;">Search</a></td></tr></table>
       <table><tr><td><a href="javascript:void(0)" onclick="goPaths('home.jsp#Html_Reports')" style="font-size: 11px;color:black">HTML Reports</a></td></tr></table>
       <table><tr><td><a href="javascript:void(0)" onclick="goHeadlinePage()" style="font-size: 11px;color:black">Headlines</a></td></tr></table>
       <table><tr><td><a href="javascript:void(0)" onclick="goPaths('home.jsp#Headlines')" style="font-size: 11px;">Headlines (S)</a></td></tr></table>
       <table><tr><td><a href="javascript:void(0)" onclick="goPaths('home.jsp#Dynamic_Headlines')" style="font-size: 11px;">Headlines (D)</a></td></tr></table>
       <table><tr><td><a href="javascript:void(0)" onclick="oneViewBy()" style="font-size: 11px;color:black">One View</a></td></tr></table>
         <table><tr><td><a href="javascript:void(0)" onclick="editWall()" style="font-size: 11px;color:black">Edit Wall </a></td></tr></table>
       <table><tr><td><a href="javascript:void(0)" onclick="IcalPage()" style="font-size: 11px;color:black">i-Cal<sup><font color="#008000">Beta</font></sup></a></li></td></tr></table>
       <table><tr><td><a href="javascript:void(0)" onclick="workBenchPage()" style="font-size: 11px;color:black">Work Bench</a></td></tr></table>
       <table><tr><td><a  href="javascript:void(0)" onclick="goPaths('pbBIManager.jsp')" style="font-size: 11px;">BI Manager</a></td></tr></table>
       <table><tr><td><a  href="javascript:void(0)" onclick="goPaths('pbBIManager.jsp')" >BI Manager</a></td></tr></table>
        <%  if (isQDEnableforUser || userType.equalsIgnoreCase("ADMIN")) {%>
        <table><tr><td><a  href="javascript:void(0)" style="font-size: 11px;color:black" onclick="pbBiManager()" >BI Manager</a></td></tr></table>
        <% }%>
        <table><tr><td><a href="javascript:void(0)" title="Login Start Page" onclick="javascript:openstartpage()" style="font-size: 11px;color:black">Login Start Page</a></td></tr></table>
        <% if (isXtendUser) {%>
           <table><tr><td><a href="javascript:void(0)" onclick="workBenchPage()" style="font-size: 11px;color:black">Work Bench</a></td></tr></table>
            <table><tr><td><a id="xtendConnect" href="javascript:void(0)"  onclick='getXtend()' style="font-size: 11px;color:black" target="_blank"> Xtend </a></td></tr></table>
        <% }%>
        </td></tr>
</table>
</div>
</td>
        <%-- <td align="right" width="1%" ><a href="javascript:void(0)" class="ui-icon1 ui-icon-home" onclick="javascript:gohome()" title="Home" style="text-decoration:none" ><img src="images/homeicon.png" height="20px" width="20px" "></a></td> --%>
        <td align="right" width="2%" style="padding-top: 15px"><a href="javascript:void(0)" class="extlinkicon" onclick="javascript:logout()" title="Logout" style="text-decoration:none"><img alt="" src="images/extlink.gif" width="25px" height="24px" border="o"/></a></td>

     <td valign="top" style="height:30px;width:10%;" align="right">

         <img alt=""  width="120px" height="50px"  title="<bean:message key="progenLogo.Title"/>" src="<%=request.getContextPath()%>/images/<bean:message key="ProGen.businessLogo"/>"/>
        <%if (companyId != null && !companyId.equalsIgnoreCase("null") && !companyId.equalsIgnoreCase("")) {
                if (bussLogo != null && !bussLogo.equalsIgnoreCase("null") && !bussLogo.equalsIgnoreCase("")) {
        %>
            <a target="_blank" href="http://<%=rightWebSiteUrl%>"> <img alt="" border="0px"  width="120px" height="50px"  title=<%=compTitle%> src="<%=request.getContextPath()%>/images/<%=bussLogo%>"/> </a>
        <%}
        } else {%>
        <a target="_blank" href="<bean:message key="rightwebsite.url"/>"> <img alt="" border="0px"  width="120px" height="50px"  title="<bean:message key="progenLogo.Title"/>" src="<%=request.getContextPath()%>/images/<bean:message key="ProGen.businessLogo"/>"/> </a>
        <%}%>
        </td>

    </tr>
</table>-->

        <table id="tabel1" class="container" >
            <tr>
            </tr>
            <tr>
                <!--            <div id="header21 " class="container" style="height: 30px;border: 1px solid #000000;background-color: rgb(86, 86, 86)">-->
                <!--            <div id="header21 " class="container" style="height: 30px;border: 1px solid #000000;background-color: #006699">-->
<!--            <div id="header21" class="container" style="display:none;height:30px;background-color: #006699">

                <div style="width:70%;float: left;"><font size="4" face="verdana" color="black" ><strong style="font-size: larger; color:#fff; margin-left: 2%;float: left; padding-top: 8px;"><%=reportName%>  </strong></font></div>
                <div style="width: 20%;float:left">
                    <a id="lmTable" class="currentloadingMenu" name="Table" style="margin-left: 10px;float: left;" onclick="loadingMenu('Table')"> <img alt="" border="0px"  width="25px" height="25px"  title="Data Analysis" src="<%=request.getContextPath()%>/images/icons/dataAna1.png"/> </a>
                    <a  id="lmGraph" class="currentloadingMenu" name="Graph"  style="margin-left: 10px;float: left;" onclick="loadingMenu('Graph')"> <img alt="" border="0px"  width="25px" height="25px"  title="Graph Analysis" src="<%=request.getContextPath()%>/images/icons/graph1.png"/> </a>
                    <a  id="lmTrend" class="currentloadingMenu" name="Indicator"  style="margin-left: 10px;float: left;" onclick="loadingMenu('Indicator')"> <img alt="" border="0px"  width="25px" height="25px" style="background-color: white; border-radius: 15px;" title="Trend Analysis" src="<%=request.getContextPath()%>/images/icons/trend1.png"/> </a>
                    <a  id="lmAdvance" class="currentloadingMenu" name="advance" style="margin-left: 10px;float: left;" onclick="loadingMenu('advance')" > <img alt="" border="0px"  width="25px" height="25px"  title="Advance Visual" src="<%=request.getContextPath()%>/images/icons/graph4.png"/> </a>
                    <div class="dropdown" style="cursor: pointer; z-index: 11; width: 25px; height: 25px; float: left; margin-left: 10px;"><span data-toggle="dropdown" class="dropdown-toggle" style="text-decoration: none">
                            <img style="width: 25px; height: 25px;" src="images/icons/opt.png" alt="Options" onclick="optionSelect()">
                        </span>
                             <ul id="graphOptionchart1" class="dropdown-menu">
                              <ul class="dropdown-menu" id="themeselect2" style="display: none; top: unset; left: auto; position: absolute; margin-left: 25px;">
                        <ul class="dropdown-menu" id="themeselect2" >
                                                 //added by krishan  
                            <% if (defaulttab.equals("Trends")) {%>

                            <li >
                                <a data-color='color1' style='padding:10px 0px 0px 24px;'id ='addGraphDataTrendAnalysis' onclick='buildChartsInTrends(parent.$("#graphsId").val())' >Add Trend Charts</a>
                            </li>
                            <% if (!userType.equalsIgnoreCase("ANALYZER")) {%>
                            <li >
                                <a data-color='color1' style='padding:10px 0px 0px 24px;'onclick='saveXtCharts()' >Save Trend Charts</a>
                            </li>
                            <% }%>
                            <li>
                                <a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='OpenDefTabs()'>Default Tab</a>
                            </li>
                            <li >
                                <a data-color='color1' style='padding:10px 0px 0px 24px;'onclick='trendAnalysisActionJsNew(parent.$("#graphsId").val())' >Reset</a></li>
                                <%} else if (defaulttab.equals("Graph")) {%>
                                <% if (!userType.equalsIgnoreCase("ANALYZER")) {%>
                            <li >
                                <a data-color='color1' style='padding:10px 0px 0px 24px;'onclick='editSingleCharts("add1")' >Add Graph Object</a>
                            </li>
                            <li >
                                <a data-color='color1' style='padding:10px 0px 0px 24px;'onclick='editSingleCharts("edit")' >Edit Graph Object</a>
                            </li>
                            <% }%>
                            <li >
                                <a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='addPage()' >Add Page</a>

                            </li>
                            <li >
                                <a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='buildCharts("add")' >Add Graph</a>

                            </li>
                            <li >
                                <a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='buildCharts("isEdit")' >Edit Graph</a>
                            </li>

                            <% if (!userType.equalsIgnoreCase("ANALYZER")) {%>
                            <li >
                                <a data-color='color1' style='padding:10px 0px 0px 24px;'  onclick='saveXtCharts()' >Save All Graphs</a>
                            </li>

                            <% }%>
                            <li >
                                <a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='regenerateFilters(parent.$("#graphsId").val())' >Regenerate Filters</a>
                            </li>
                            <li >
                                <a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='addCustomMeasure(parent.$("#graphsId").val())' >Add Custom Measure</a>
                            </li>
                            <li>
                                <a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='OpenDefTabs()'>Default Tab</a>
                            </li>
                            <li >
                                <a data-color='color1' style='padding:10px 0px 0px 24px;'  onclick='generateJsonDataReset(parent.$("#graphsId").val())' >Reset</a>
                            </li>

                            <%} else if (defaulttab.equals("AdvanceVisuals")) {%>
                            <li >
                                <a data-color='color1' style='padding:10px 0px 0px 24px;' id ='addGraphDataTrendAnalysis' onclick='createAdvanceVisual(parent.$("#graphsId").val())' >Add Visual</a>
                            </li>
                            <li >
                                <a data-color='color1' style='padding:10px 0px 0px 24px;' id ='addGraphAnalysis' onclick='editAdvanceVisual(parent.$("#graphsId").val())' >Edit Visual</a>
                            </li>
                            <% if (!userType.equalsIgnoreCase("ANALYZER")) {%>
                            <li >
                                <a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='saveXtCharts()' >Save Visual</a>
                            </li>
                            <% }%>
                            <li >
                                <a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='getAdvanceVisuals(parent.$("#graphsId").val())' >Reset</a>
                            </li>

                            <li >
                                <a data-color='color1' style='padding:10px 0px 0px 24px;'  onclick='setProperties(parent.$("#graphsId").val())' >Properties</a>
                            </li>
                            <li>
                                <a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='OpenDefTabs()'>Default Tab</a>
                            </li>
                            <% if (!userType.equalsIgnoreCase("ANALYZER")) {%>
                            <li >
                                <a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='resequenceGraphs()' >Delete / Resequence</a>
                            </li>
                            <% }%>
                            <% if (true) {%>
                            <li >
                                <a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='defineParentChildDB()' >Define Multi Dashboard </a>
                            </li>
                            <% }%>
                            <%} else {%>

                            <li >
                                <a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='dispChangeTableColumns("<%=request.getContextPath()%>", "<%=sbufRoles.toString().substring(1)%>", "<%=reportId1%>")' >Edit Table Data</a>
                            </li>
                            <li >
                                <a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='editViewBy()' >Edit Table Layout</a>
                            </li>
                            <li >
                                <a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='reportDrillAssignment("<%=request.getContextPath()%>", "<%=reportId1%>")' >Define Report Drill</a>
                            </li>
                            <li >
                                <a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='addCustomMeasure1("<%=request.getContextPath()%>", "<%=reportId1%>")' >Define Custom Measure</a>
                            </li>
                            <%if (container.isReportCrosstab()) {%>
                            <li >
                                <a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='difineCustomSeq("<%=sbufRoles.toString().substring(1)%>", "<%=reportId1%>")' >Custom Sequence</a>
                            </li>
                            <%}%>
                            <% if (!userType.equalsIgnoreCase("ANALYZER")) {%>
                            <li >
                                <a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='openNewReportDtls("<%=request.getContextPath()%>")' >Save As New Report</a>
                            </li>
                            <li >
                                <a data-color='color1' style='padding:10px 0px 0px 24px;' onclick=overWriteReport1("<%=request.getContextPath()%>", 'overWrite',<%=isRestrictedPowerAnalyserEnable%>,<%=isMultiCompany %>,<%=isPowerAnalyserEnableforUser%>) >Save/Overwrite Report</a>
                            </li>
                            <li >
                                <a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='saveTableRegion("<%=request.getContextPath()%>", "<%=container.getReportId()%>")' >Save Table Data </a>
                            </li>
                            <% }%>
                            <li>
                                <a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='resetGraphs()'>Reset Graphs</a>
                            </li>
                            <li>
                                <a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='OpenDefTabs()'>Default Tab</a>
                            </li>
                            <li >
                                <a data-color='color1' style='padding:10px 0px 0px 24px;' href="javascript:saveNewRepGrpTab1('<%=request.getContextPath()%>','<%=USERID%>','<%=rolesid%>','<%=PbReportId%>','<%=reportName%>')">Assign Report</a>
                            </li>


                            <%}%>
                            ended by krishan 

                        </ul>
                    </div>-->

<!-- <a  id="SwHiFil" style="margin-left: 10px;" onclick="showHideFil()" ><input type="hidden" id="SwHiFilI" value="show"/> <img alt=""  border="0px"    title="Show/Hide Filters" src="<%=request.getContextPath()%>/images/icons/UpDown.png"/> </a>-->
<!-- <img src="images/down_arrow.png" title="Downloads Report" onclick="parent.showExports('<%= PbReportId%>','<%=request.getContextPath()%>')" style="margin-top: 4px; padding-left: 5px; cursor: pointer">
                                                              <img src="images/email.png" title="Send Mail" onclick="openShareReportDiv()" style="margin-top: 4px; padding-left: 5px; cursor: pointer">
                                <img src="images/sceduler.png" title="Scheduler" onclick="openScheduleReport()" style="margin-top: 4px; padding-left: 5px; cursor: pointer">-->



<!--                <font color="black;" style="color: #fff; margin-top: -17px; margin-right: 10px;">&nbsp;Load Time: <%= finalValue%>s</font>-->

                    <!--added by manik for Search    -->
                    <!--                <form id="searchbox3" action="Search.jsp" target="_blank" onsubmit=" return checkInputValue()" name="myform" style="float: left">
                                        <input id="submit3" style="float: right" type="submit" value="Search"  >
                                        <input id="search3" style="float: right" type="text" name="data" value="" placeholder="Please enter the Search criteria  eg: Enrollment" autocomplete="off" >
                                    </form>-->
<!--                </div>-->
    <!--<div style="width:10%;float:left;"><font size="2" face="verdana" color="black" ><strong style="font-size: smaller; color:#fff; float: left; padding-top: 8px;">&nbsp;Query Time: <%= finalValue%>s  </strong></font></div>-->
                </tr>
                <tr>
                <div  class="container">
                    <div id="Tabdiv" class=""  style="height:auto" >
                        <ul class="navbar color2" style="width:100%;display: none;" >
                            <!--                    <li class="menu-item " style="width:15%"><a  onclick="loadingMenu('Table')" style="padding: 1px;"><span><font size="3" face="verdana" style="padding:0px 5px 0px 10px;font-size: 14px; color: white">Data Analysis</font></span></a></li>
                                                <li class="menu-item" style="width: 15%"><a id="graphAnchor" style="padding: 1px;width:100%" onclick="loadingMenu('Graph')"><span><font size="3" face="verdana" style="padding:0px 5px 0px 10px;font-size: 14px; color: white">Graphical Analysis</font></span></a></li>
                                                <li class="menu-item" style="width:15%"><a href="#section-underline-3!" onclick="loadingMenu('Indicator')" style="padding: 1px;width:100%"><span><font size="3" face="verdana" style="padding:0px 5px 0px 10px;font-size: 14px; color: white">Trend Analysis</font></span></a></li>
                                                <li class="menu-item" style="width:15%"><a id="advanceAnchor" href="#section-underline-3!" onclick="loadingMenu('advance')" style="padding: 1px;width:100%"><span><font size="3" face="verdana" style="padding:0px 5px 0px 10px;font-size: 14px; color: white">Thematic Map/Charts</font></span></a></li>
                                                <li id="testCase" class="drpdown menu-item" style="width: 15%"><a  style="padding: 1px;width:100%"><span><font size="3" face="verdana" style="font-size: 14px; color: white">Options</font></span></a>
                                                    <ul class="drpcontent" id="themeselect">
                            <!--                     //added by krishan  -->
                            <% if (defaulttab.equals("Trends")) {%>

                            <li >
                                <a data-color='color1' style='padding:10px 0px 0px 24px;'id ='addGraphDataTrendAnalysis' onclick='buildChartsInTrends(parent.$("#graphsId").val())' >Add Trend Charts</a>
                            </li>
                            <% if (!userType.equalsIgnoreCase("ANALYZER")) {%>
                            <li >
                                <a data-color='color1' style='padding:10px 0px 0px 24px;'onclick='saveXtCharts()' >Save Trend Charts</a>
                            </li>
                            <% }%>
                            <li>
                                <a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='OpenDefTabs()'>Default Tab</a>
                            </li>
                            <li >
                                <a data-color='color1' style='padding:10px 0px 0px 24px;'onclick='trendAnalysisActionJsNew(parent.$("#graphsId").val())' >Reset</a></li>
                                <%} else if (defaulttab.equals("Graph")) {%>
                                <% if (!userType.equalsIgnoreCase("ANALYZER")) {%>
                            <li >
                                <a data-color='color1' style='padding:10px 0px 0px 24px;'onclick='editSingleCharts("add1")' >Add Graph Object</a>
                            </li>
                            <li >
                                <a data-color='color1' style='padding:10px 0px 0px 24px;'onclick='editSingleCharts("edit")' >Edit Graph Object</a>
                            </li>
                            <% }%>
                            <li >
                                <a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='addPage()' >Add Page</a>

                            </li>
                            <li >
                                <a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='buildCharts("add")' >Add Graph</a>

                            </li>
                            <li >
                                <a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='buildCharts("isEdit")' >Edit Graph</a>
                            </li>

                            <% if (!userType.equalsIgnoreCase("ANALYZER")) {%>
<!--                            <li >
                                <a data-color='color1' style='padding:10px 0px 0px 24px;'  onclick='saveXtCharts()' >Save All Graphs</a>
                            </li>-->
                            <% }%>
                            <li >
                                <a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='regenerateFilters(parent.$("#graphsId").val())' >Regenerate Filters</a>
                            </li>
                            <li >
                                <a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='addCustomMeasure(parent.$("#graphsId").val())' >Add Custom Measure</a>
                            </li>
                            <li>
                                <a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='OpenDefTabs()'>Default Tab</a>
                            </li>
                            <li >
                                <a data-color='color1' style='padding:10px 0px 0px 24px;'  onclick='generateJsonDataReset(parent.$("#graphsId").val())' >Reset</a>
                            </li>

                            <%} else if (defaulttab.equals("AdvanceVisuals")) {%>
                            <li >
                                <a data-color='color1' style='padding:10px 0px 0px 24px;' id ='addGraphDataTrendAnalysis' onclick='createAdvanceVisual(parent.$("#graphsId").val())' >Add Visual</a>
                            </li>
                            <li >
                                <a data-color='color1' style='padding:10px 0px 0px 24px;' id ='addGraphAnalysis' onclick='editAdvanceVisual(parent.$("#graphsId").val())' >Edit Visual</a>
                            </li>
                            <% if (!userType.equalsIgnoreCase("ANALYZER")) {%>
                            <li >
                                <a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='saveXtCharts()' >Save Visual</a>
                            </li>
                            <% }%>
                            <li >
                                <a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='getAdvanceVisuals(parent.$("#graphsId").val())' >Reset</a>
                            </li>

                            <li >
                                <a data-color='color1' style='padding:10px 0px 0px 24px;'  onclick='setProperties(parent.$("#graphsId").val())' >Properties</a>
                            </li>
                            <li>
                                <a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='OpenDefTabs()'>Default Tab</a>
                            </li>
                            <% if (!userType.equalsIgnoreCase("ANALYZER")) {%>
                            <li >
                                <a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='resequenceGraphs()' >Delete / Resequence</a>
                            </li>
                            <% }%>
                            <% if (true) {%>
                            <li >
                                <a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='defineParentChildDB()' >Define Multi Dashboard </a>
                            </li>
                            <% }%>
                            <%} else {%>

                            <li >
                                <a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='dispChangeTableColumns("<%=request.getContextPath()%>", "<%=sbufRoles.toString().substring(1)%>", "<%=reportId1%>")' >Edit Report Data</a>
                            </li>
                            <li >
                                <a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='editViewBy()' >Modify Report Layout</a>
                            </li>
                            <li >
                                <a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='reportDrillAssignment("<%=request.getContextPath()%>", "<%=reportId1%>")' >Define Report Drill</a>
                            </li>
                            <li >
                                <a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='addCustomMeasure1("<%=request.getContextPath()%>", "<%=reportId1%>")' >Define Custom Measure</a>
                            </li>
                            <%if (container.isReportCrosstab()) {%>
                            <li >
                                <a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='difineCustomSeq("<%=sbufRoles.toString().substring(1)%>", "<%=reportId1%>")' >Custom Sequence</a>
                            </li>
                            <%}%>
                            <% if (!userType.equalsIgnoreCase("ANALYZER")) {%>
                            <li >
                                <a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='openNewReportDtls("<%=request.getContextPath()%>")' >Save As New Report</a>
                            </li>
                            <li >
                                <a data-color='color1' style='padding:10px 0px 0px 24px;' onclick=overWriteReport1("<%=request.getContextPath()%>", 'overWrite',<%=isRestrictedPowerAnalyserEnable%>,<%=isMultiCompany %>,<%=isPowerAnalyserEnableforUser%>) >Save/Overwrite Report</a>
                            </li>
                            <li >
                                <a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='saveTableRegion("<%=request.getContextPath()%>", "<%=container.getReportId()%>")' >Save Table Data </a>
                            </li>
                            <% }%>
<!--                            <li>
                                <a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='resetGraphs()'>Reset Graphs</a>
                            </li>-->
                            <li>
                                <a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='OpenDefTabs()'>Default Tab</a>
                            </li>
                            <li >
                                <a data-color='color1' style='padding:10px 0px 0px 24px;' href="javascript:saveNewRepGrpTab1('<%=request.getContextPath()%>','<%=USERID%>','<%=rolesid%>','<%=PbReportId%>','<%=reportName%>')">Assign Report</a>
                            </li>


                            <%}%>
                            <!--ended by krishan -->

                        </ul>
                        <ul style="float:right;margin-left: 88%;margin-top: -1.3%;position: relative;display: none;">
                            <li style="background-color: white; float: right"><span>
                                    <!--                            added by krishan-->
                                    <img src="images/down_arrow.png" title="Downloads Report" onclick="parent.showExports('<%= PbReportId%>', '<%=request.getContextPath()%>')" style="margin-top: 4px; padding-left: 5px; cursor: pointer">
                                    <!--  <img src="images/sceduler.png" title="Schedule Graph" onclick="scheduleGO()" style="margin-top: 4px;cursor: pointer ">-->
                                      <!--<img src="images/pdf.png" title="Download PDF" onclick="downloadPDF('<%=reportId1%>')" style="margin-top: 4px;cursor: pointer ">-->
                                      <!--<img src="images/excel.png" title="Export Excel" onclick="downloadExcel('<%=reportId1%>')" style="margin-top: 4px; padding-left: 5px;cursor: pointer">-->
                                    <img src="images/email.png" title="Send Mail" onclick="openShareReportDiv()" style="margin-top: 4px; padding-left: 5px; cursor: pointer">
                                    <img src="images/sceduler.png" title="Scheduler" onclick="openScheduleReport()" style="margin-top: 4px; padding-left: 5px; cursor: pointer">
                                    <!--<img src="images/filter.png" title="View/Apply Filter" style="margin-top: 4px; padding-left: 5px; cursor: pointer" onclick="callFilterDiv()">-->
                                </span>
                            </li>
                        </ul>


                    </div>
                </div></tr>
                <!--added by sandeep-->

                <tr>
                <div id='managementTempl'>               
                    <div class="rootContainer">
                        <div id='firstKPI' class="leftSection">

                        </div>
                        <div id='secondKPI' class="rightSection">

                        </div>
                    </div>
                    <div class="outerBox">
                        <div id='pagesDash' class="rootContainer2">
                            
                        </div>
                        <div onclick="openMenu()" class="menuBar">
                            <i class="fa fa-bars fa-2x" aria-hidden="true"></i>
                            <div id='optionMenu'></div>
                        </div>
<!--                        <div  class="section3" onclick="openLink(this)">
                            Gross Sales
                        </div>-->
                    </div>
                </div>      
                
                <div id="rightDiv1" class="container" style="display:none;position:fixed;z-index:50;box-shadow: 0px 2px 2px 0px #888;">
<!--                    <div id="arrowL" style="display:none;">
                        <span id="prev" style=""><img   style="height:30px;" src="<%=request.getContextPath()%>/images/_arrow-left-.png" /></span>
                                  <span id="next11" style="" ><img title="Click for next reports" style="height:20px;" src="<%=request.getContextPath()%>/images/_arrow-right-.png" /></span>
                    </div>
                    <div id="arrowR" style="display:none;">
                        <span id="next1" style="" ><img  style="height:30px;margin-left: -30px"  src="<%=request.getContextPath()%>/images/_arrow-right-.png" /></span>
                    </div>
                    <div id="resetgraph" style="display:none;">
                        <img title="Reset"  style=' background:white;height:20px; border-radius:10px ;margin-left: -20px'  onclick='generateJsonDataReset(parent.$("#graphsId").val())' src="<%=request.getContextPath()%>/images/refersh_image.png" />
                                   <span id="" style=""><a onclick='generateJsonDataReset(parent.$("#graphsId").val())' style="margin-left: -30px"> Reset </a></span>
                    </div>-->

                        <div id="list-container" style="">
                            <div  id="updaterowgraph" class='list'>
                                <!--<div  id="globalfilterrow" style="display:none;vertical-align: top;margin-top: 30px">-->
                                <%
                                String graphselecteddata=(String)session.getAttribute("graphselectdata");
                                    if (allFiltersnames != null && !allFiltersnames.isEmpty()) {
                                        StringBuilder stringbuilder = new StringBuilder();
                                        List<String> selectfilters = new ArrayList<String>();
                                        Set keySet1 = allFiltersnames.keySet();
                                        List<String> filterids = new ArrayList<String>();

    int size = 6;
                          if(graphselecteddata!=null && !graphselecteddata.isEmpty()){
                              size=Integer.parseInt(graphselecteddata);
                          }else{
                                 if (viewbynames.size() >= 6) {%>
                                   <script type="text/javascript">
                                  $("#updaterowgraph").removeClass("list");
				  $("#updaterowgraph").addClass("listviewby");
                                   </script>
				<%  size = 6;
                                } else {
                                    size = viewbynames.size();
                                    %>
                                     <script type="text/javascript">
                                     changelist();
                                                                      </script>
                                     <%
                                }}
                                        String key1;
                                        int i1 = 0;
                                        int setgblflag = 0;String selectidgraph;
                                        Set keySet = allFilters.keySet();
                                        Iterator itr = keySet.iterator();
                                        String key;
                                        if(size>0){
                                        for (int j1 = 0; j1 < size; j1++) {
                                            List<String> parameterlistNames = new ArrayList<String>();
                                            Iterator itr1 = keySet1.iterator();
                                            if (setgblflag == j1) {
                                                String idgbl = "globalfilterrow" + setgblflag;
                                %>
                                  <script type="text/javascript">parent.sizeoffiltersg='<%=size%>'</script>
                                <div  id="<%=idgbl%>" class='item' style="vertical-align: top;margin: 0px;padding: 0px 5px;">
                                    <%setgblflag = setgblflag + 6;
                                        }
                                        while (itr1.hasNext()) {
                                            key1 = itr1.next().toString();

                                            if (key1.equalsIgnoreCase(viewbynames.get(j1))) {
                                                key1 = key1.toString().replace(" ", "1q1");
selectidgraph=parameterlist.get(i1)+"__"+key1;
                                    %>
                                    <select name="<%=parameterlist.get(i1)%>" id=<%=selectidgraph%> multiple style="">

                                        <%
                                                }
                                            }%>
                                    </select>
<!--                                    &nbsp;-->
                                    <%
                                        filterMapgraphs.put(parameterlist.get(i1), selectfilters);
                                        i1++;
                            if ((setgblflag - 1) == j1) {%>

                                </div>
                                <% }
                                        }}

                                    }

                                %>
                                <!--                </td><td>-->

                            </div>
                        </div>

<!--                    TA_JQ_1005 :: Filters alignment UI Issue -->
                   <div style='float:left;width:10%;'>
                    <div id="gottabId11" style="float:left;width: 35px;height: 26px;">
                        <input id='gottabId1' class='prgBtn gFontFamily ' type='button'  value='<%=TranslaterHelper.getTranslatedInLocale("go", cL)%>' onclick='ApplyfilterGO()' style='height:100%;width:100%;text-transform: uppercase;font-weight: bold;'>
                        </div>
                   <div id="morefilters" style="width:auto;float:right">
                        <span  id="dragDropParam" style='font-size:20px;color:#888;float:left;' onclick="hidedragdrop()"><i class="fa fa-exchange fa-rotate-90"  title="<%=TranslaterHelper.getTranslatedInLocale("Click_To_Show_Parameter", cL)%>"  ></i></span>
                        <span  id="newChartIcon" style='width:20px;height:20px;float:left;'><img class=""  style="width:20px;height:20px;" title="<%=TranslaterHelper.getTranslatedInLocale("Clicked To Add New Chart", cL)%>" src="images/icons/newChart.png" alt="Options" onclick="draganddrop(JSON.parse($('#viewby').val())[0],'gridsterDiv','gridsterDiv')"></img></span>                 
                        <span  id="more" style='font-size:22px;float:left;margin:-2px 5px 0px 5px' onclick="showmorefiltersg('moreFiltersg')" ><input type="hidden"  value="show"/> <i class="fa fa-filter"  title="<%=TranslaterHelper.getTranslatedInLocale("Clicked To Show Filter", cL)%>" > </i></span>
<!--                        <span  id="greenfilter" style="display:none" onclick="showmorefiltersg('moreFiltersg')" ><input type="hidden" id="more" value="show"/> <i class="fa fa-filter" title="<%=TranslaterHelper.getTranslatedInLocale("More", cL)%>"></i> </span>-->
                        <span style='font-size:22px;color:#888;float:left;margin-left:5px;' onclick="moreListDispTopgraph()"><i class="fa fa-ellipsis-v" title="<%=TranslaterHelper.getTranslatedInLocale("More", cL)%>"></i></span>
                        <div id='mmoreFiltersg'  onmouseleave='hideme(this.id)' style='position:absolute;display:none;margin-left:-190px;z-index:1;width:225px;height:315px;background-color:#fdfdfd;border:1px solid #D1D1D1;border-radius:5px;box-shadow:0px 3px 3px 2px #bfbfbf'>
                            <div id='moreFiltersg'   style='width:100%;height:255px;overflow-y:auto'></div>
                            <div class='gFontFamily ' style='width: 100%;height: 30px;text-align: center;background-color: #8BC34A;'><span onclick="ApplyfilterGO()" style='display: block;padding: 5px;font-size: 14px;color: rgb(0, 0, 0);'><%=TranslaterHelper.getTranslatedInLocale("Apply", cL)%></span></div>
                        </div>

                         <div id="datalist1"  class="hideAllDiv1"  onmouseleave='hideme(id)' style='position:absolute;display:none;margin-left:-80px;z-index:1;width:140px;height:155px;background-color:#fdfdfd;border:1px solid #D1D1D1;border-radius:5px;box-shadow:0px 3px 3px 2px #bfbfbf'>
                            <%if(container.report_Headrvalue != null && (container.report_Headrvalue).equalsIgnoreCase("hideheader")){%>
                         <span style="background-color:white;" class="ftddropmnum"><a style="color:gray;margin:centre" onclick="saveShowHeader('showheader','<%=PbReportId%>')" >Show Report Header</a></span>
                                 <%}else{%>
                                  <span style="background-color:white;" class="ftddropmnum"><a style="color:gray;margin:centre" onclick="saveShowHeader('hideheader','<%=PbReportId%>')" >Hide Report Header</a></span>
                                <%}%>
                                <%if(container.chart_isDraggable != null && (container.chart_isDraggable).equalsIgnoreCase("nondraggable")){%>
                         <span style="background-color:white;" class="ftddropmnum"><a style="color:gray;margin:centre" onclick="isDraggable('draggable','<%=PbReportId%>')" >Enable Draggable Chart</a></span>
                                 <%}else{%>
                                  <span style="background-color:white;" class="ftddropmnum"><a style="color:gray;margin:centre" onclick="isDraggable('nondraggable','<%=PbReportId%>')" >Disable Draggable Chart</a></span>
                                <%}%>
                               
                   </div>
                   </div>
                   </div>
<!--                End!!!-->

                    </div></tr><tr>
                    <div id="rightDiv1trend" class="container" style="display:none;position:fixed;z-index:50;">
                        <div id="arrowLtr">
                            <span id="prev" style=""><img title="Click for previous reports"  style="height:30px;" src="<%=request.getContextPath()%>/images/_arrow-left-.png" /></span>
                    <!--                      <span id="next11" style="" ><img title="Click for next reports" style="height:20px;" src="<%=request.getContextPath()%>/images/_arrow-right-.png" /></span>-->
                        </div>
                        <div id="arrowRtr">
                            <span id="next1" style="" ><img title="Click for next reports" style="height:30px;margin-left: -30px"  src="<%=request.getContextPath()%>/images/_arrow-right-.png" /></span>
                        </div>
                        <div id="resetgraph">
                            <img title="Reset"  style=' background:white;height:20px; border-radius:10px ;margin-left: -20px'  onclick='trendAnalysisActionJsNew(parent.$("#graphsId").val())' src="<%=request.getContextPath()%>/images/refersh_image.png" />
                            <!-- <span id="" style=""><a onclick='trendAnalysisActionJsNew(parent.$("#graphsId").val())' style="margin-left: -30px"> Reset </a></span>        </div>-->
                        </div><div id="gottabId11">
                            <input id='gottabId1' class='navtitle-hover gFontFamily ' type='button'  value='Go' onclick='ApplyfilterGO()' style='width:25px; background:white; border-radius:10px ;margin-left: -40px'>
                        </div>

                        <div id="list-containertr" style="">
                            <div class='list'>

                                <!--<div  id="globalfilterrowTrend" style="display:none;vertical-align: top;margin-top: 30px">-->
                                <%
                                    if (allFiltersnames != null && !allFiltersnames.isEmpty()) {
                                        StringBuilder stringbuilder = new StringBuilder();

                                        Set keySet1 = allFiltersnames.keySet();
                                        List<String> filterids = new ArrayList<String>();
                                        int size1 = viewbynames.size();
                                        int setgblflag = 0;
                                        String key1;
                                        int i1 = 0;
String selectidtr;
                                        Set keySet = allFilters.keySet();
                                        Iterator itr = keySet.iterator();
                                        String key;
                                        for (int j1 = 0; j1 < size1; j1++) {
                                            List<String> parameterlistNames = new ArrayList<String>();
                                            Iterator itr1 = keySet1.iterator();

                                            if (setgblflag == j1) {
                                                String idgbl = "globalfilterrow" + setgblflag;
                                %>
                                <div  id="<%=idgbl%>" class='itemtr' style="vertical-align: top;margin-top: 0px;margin-left: 10px;">
                                    <%setgblflag = setgblflag + 6;
                                        }
                                        while (itr1.hasNext()) {
                                            key1 = itr1.next().toString();

                                            if (key1.equalsIgnoreCase(viewbynames.get(j1))) {
                                                key1 = key1.toString().replace(" ", "1q1");
                                                key1 = "Tr" + key1;
selectidtr=parameterlist.get(i1)+"__"+key1;
                                    %>
                                    <select name="<%=parameterlist.get(i1)%>" id=<%=selectidtr%> multiple style="">

                                        <%
                                                }
                                            }%>
                                    </select>


                                    <%
                    if ((setgblflag - 1) == j1) {%>

                                </div>
                                <% }
                                            i1++;
                                        }

                                    }

                                %>
                            </div></div></div></tr><tr>

                        <!--                </td><td>-->
                    <div id="rightDiv1ad" class="container" style="display:none;position:fixed;z-index:50;">
                        <div id="arrowLad"  style="display:none">
                            <span id="prev" style=""><img title="Click for previous reports"  style="height:30px;" src="<%=request.getContextPath()%>/images/_arrow-left-.png" /></span>
                    <!--                      <span id="next11" style="" ><img title="Click for next reports" style="height:20px;" src="<%=request.getContextPath()%>/images/_arrow-right-.png" /></span>-->
                        </div>
                        <div id="arrowRad" style="display:none">
                            <span id="next1" style="" ><img title="Click for next reports" style="height:30px;margin-left: -30px"  src="<%=request.getContextPath()%>/images/_arrow-right-.png" /></span>
                        </div>
                        <div id="resetgraph"  style="display:none">
                            <img title="Reset"  style=' background:white;height:20px; border-radius:10px ;margin-left: -20px'  onclick='getAdvanceVisuals(parent.$("#graphsId").val())' src="<%=request.getContextPath()%>/images/refersh_image.png" />
                            <!-- <span id="" style=""><a onclick='getAdvanceVisuals(parent.$("#graphsId").val())' style="margin-left: -30px"> Reset </a></span>        </div>-->
                        </div>
                        <div id="gottabId11">
                            <input id='gottabId1' class='navtitle-hover gFontFamily ' type='button'  value='<%=TranslaterHelper.getTranslatedInLocale("go", cL)%>' onclick='ApplyfilterGO()' style='width:45px; background:lightgray; border-radius:1px ;margin-left: -70px'>
                        </div>
                        <div id="morefilters" style="margin-top:3px;">
                            <a  id="moread" style="margin-left: 10px;" onclick="showmorefiltersg('moreFiltersad')" ><input type="hidden"  value="show"/> <img alt=""  border="0px"    title="More" src="<%=request.getContextPath()%>/images/icons/filter.png"/> </a>
                            <a  id="greenfilterad" style="margin-left: 10px;display:none;" onclick="showmorefiltersg('moreFiltersad')" ><input type="hidden" id="more" value="show"/> <img alt=""  border="0px"    title="More" src="<%=request.getContextPath()%>/images/icons/orangefilter.png"/> </a>
                            <div id='mmoreFiltersad'  style='position:absolute;display:none;margin-left:-190px;z-index:1;width:225px;height:315px;background-color:#fdfdfd;border:1px solid #D1D1D1;border-radius:5px;box-shadow:0px 3px 3px 2px #bfbfbf'>
                                <div id='moreFiltersad'  style='width:100%;height:255px;overflow-y:auto'></div>
                                <div class='gFontFamily ' style='width: 100%;height: 30px;text-align: center;background-color: #8BC34A;'><span onclick="ApplyfilterGO()" style='display: block;padding: 5px;font-size: 14px;color: rgb(0, 0, 0);'><%=TranslaterHelper.getTranslatedInLocale("Apply", cL)%></span></div>
                                <!--<div style='width: 100%;height: 30px;text-align: center;'><i class="fa fa-plus-circle" onclick="AddMoreDims('<%=request.getContextPath()%>')" title="Add More Paramter" style='font-size: 22px;margin: 5px 10px;color: #d6d6d6;'></i><i class="fa fa-exchange fa-rotate-90" title="Sequnce Parameter" onclick="parent.sequenceParams('<%=request.getContextPath()%>')"  style='font-size: 18px;margin: 5px 10px;color: #d6d6d6;'></i><i class="fa fa-minus-circle" onclick="parent.RemoveMoreDims('<%=request.getContextPath()%>')"   title="Remove Parameter" style='font-size: 22px;margin: 5px 10px;color: #d6d6d6;'></i><i class="fa fa-floppy-o" onclick="parent.saveParamSection('<%=request.getContextPath()%>')"    title="Save Parameter" style='font-size: 22px;margin: 5px 10px;color: #d6d6d6;'></i></div></div>-->
                            </div>
                        </div>
                        <div id="list-containerad" style="">
                            <div class='list'>
                                <!--<div  id="globalfilterrowadv" style="display:none;vertical-align: top;margin-top: 30px">-->
                                <%
                                    if (allFiltersnames != null && !allFiltersnames.isEmpty()) {
                                        StringBuilder stringbuilder = new StringBuilder();

                                        Set keySet1 = allFiltersnames.keySet();
                                        List<String> filterids = new ArrayList<String>();
                                        int size1 = viewbynames.size(); if (viewbynames.size() > 6) {
                                    size1 = 6;
                                } else {
                                    size1 = viewbynames.size();
                                }
                                        int setgblflag = 0;
                                        String key1;
                                        int i1 = 0;
String selectidad;
                                        Set keySet = allFilters.keySet();
                                        Iterator itr = keySet.iterator();
                                        String key;
                                        for (int j1 = 0; j1 < size1; j1++) {
                                            List<String> parameterlistNames = new ArrayList<String>();
                                            Iterator itr1 = keySet1.iterator();
                                            if (setgblflag == j1) {
                                                String idgbl = "globalfilterrow" + setgblflag;
                                %>
                                <div  id="<%=idgbl%>" class='itemad' style="vertical-align: top;margin-top: 0px;margin-left: 10px;">
                                    <%setgblflag = setgblflag + 6;
                                        }
                                        while (itr1.hasNext()) {
                                            key1 = itr1.next().toString();

                                            if (key1.equalsIgnoreCase(viewbynames.get(j1))) {
                                                key1 = key1.toString().replace(" ", "1q1");
                                                key1 = "ad" + key1;
selectidad=parameterlist.get(i1)+"__"+key1;
                                    %>
                                    <select name="<%=parameterlist.get(i1)%>" id=<%=selectidad%> multiple style="">

                                        <%
                                                }
                                            }%>
                                    </select>

                                    &nbsp;&nbsp;&nbsp;&nbsp;
                                    <%
                    if ((setgblflag - 1) == j1) {%>

                                </div>
                                <% }
                                            i1++;
                                        }

                                    }

                                %>
                            </div></div></div>
                    <!--                </td><td>-->


                </tr>
                 <% if(defaulttab!=null && (defaulttab.equalsIgnoreCase("Report") || defaulttab.equalsIgnoreCase("TABLE") || defaulttab.equalsIgnoreCase(""))) {    %>
                <tr>
            <div id="rightDiv1table" class="container" style="display:none;position:fixed;padding:0px;">
                <div id="arrowLtable">
                    <span id="prev" style=""><img   style="height:30px;" src="<%=request.getContextPath()%>/images/_arrow-left-.png" /></span>
            <!--                      <span id="next11" style="" ><img title="Click for next reports" style="height:20px;" src="<%=request.getContextPath()%>/images/_arrow-right-.png" /></span>-->
                </div>
                <div id="arrowRtable">
                    <span id="next1" style="" ><img  style="height:30px;margin-left: -30px"  src="<%=request.getContextPath()%>/images/_arrow-right-.png" /></span>
                </div>
                <div id="resetgraph" style="display:none;">
            <!--                 <img title="Reset"  style=' background:white;height:20px; border-radius:10px ;margin-left: -20px'  onclick="parent.openImgDiv()" href="<%=resetpath%>" src="<%=request.getContextPath()%>/images/refersh_image.png" />-->
                    <span id="" style=""><a onclick="openImgDiv()" href="<%=resetpath%>" style="margin-left: -20px"> <img title="Reset"  style=' background:white;height:20px; border-radius:10px ;'   src="<%=request.getContextPath()%>/images/refersh_image.png" /> </a></span>
                </div>
                <div id="gottabId11">
                    <input id='gottabId1' class='navtitle-hover gFontFamily ' type='button'  value='<%=TranslaterHelper.getTranslatedInLocale("go", cL)%>' onclick='submitform()' style='width:45px; background:lightgray;color:black; border-radius:1px ;margin-left: -70px'>
                </div>
                <div id="morefilters" style="margin-top:3px;">
                    <a  id="moretb" style="margin-left: 10px;" onclick="showmorefilters('moreFilters')" ><input type="hidden" id="more" value="show"/> <img alt=""  border="0px"    title="<%=TranslaterHelper.getTranslatedInLocale("more", cL)%>" src="<%=request.getContextPath()%>/images/icons/filter.png"/> </a>
                                  <a  id="greenfiltertb" style="margin-left: 10px;display:none;" onclick="showmorefilters('moreFilters')" ><input type="hidden" id="more" value="show"/> <img alt=""  border="0px"    title="More" src="<%=request.getContextPath()%>/images/icons/orangefilter.png"/> </a>
                    <div id='mmoreFilters'    style='position:absolute;display:none;margin-left:-190px;z-index:101;width:225px;height:315px;background-color:#fdfdfd;border:1px solid #D1D1D1;border-radius:5px;box-shadow:0px 3px 3px 2px #bfbfbf'>
                        <div id='moreFilters'  style='width:100%;height:255px;overflow-y:auto'></div>
                        <div class='gFontFamily ' style='width: 100%;height: 30px;text-align: center;background-color: #8BC34A;'><span onclick="submitform()" style='display: block;padding: 5px;font-size: 14px;color: rgb(0, 0, 0);'><%=TranslaterHelper.getTranslatedInLocale("Apply", cL)%></span></div>
                        <div style='width: 100%;height: 30px;text-align: center;'><i class="fa fa-plus-circle" onclick="AddMoreDims('<%=request.getContextPath()%>')" title="<%=TranslaterHelper.getTranslatedInLocale("Add_More_Paramter", cL)%>" style='font-size: 22px;margin: 5px 10px;color: #d6d6d6;'></i><i class="fa fa-exchange fa-rotate-90" title="<%=TranslaterHelper.getTranslatedInLocale("Sequnce_Parameter", cL)%>" onclick="parent.sequenceParams('<%=request.getContextPath()%>')"  style='font-size: 18px;margin: 5px 10px;color: #d6d6d6;'></i><i class="fa fa-minus-circle" onclick="parent.RemoveMoreDims('<%=request.getContextPath()%>')"   title="<%=TranslaterHelper.getTranslatedInLocale("Remove_Parameter", cL)%>" style='font-size: 22px;margin: 5px 10px;color: #d6d6d6;'></i><i class="fa fa-floppy-o" onclick="parent.saveParamSection('<%=request.getContextPath()%>')"    title="<%=TranslaterHelper.getTranslatedInLocale("Save_Parameter", cL)%>" style='font-size: 22px;margin: 5px 10px;color: #d6d6d6;'></i></div></div>
                </div>
                <div id="quickfilter" style="display:none;">
                    <img title="qickfilters"  style=' background:white;height:30px; border-radius:10px ;margin-left: -30px'  onclick="editFilterBy()"  src="<%=request.getContextPath()%>/images/changeView.png" />
                    <!--<span id="" style=""><a onclick='editFilterBy()' style="margin-left: -50px"> qfilters </a></span>-->
                </div>

                <div id="list-containertable" style="">
                    <div class='listtable'>
                        <!--<div  id="globalfilterrowadv" style="display:none;vertical-align: top;margin-top: 30px">-->
                        <%
                            HashMap reportParam = collect.reportParameters;
                            PbDb db = new PbDb();
                            PbReturnObject retObj = null;
                            String qry = "select DISTINCT ELEMENT_ID, PARAM_DISP_NAME, DISP_SEQ_NO from PRG_AR_REPORT_PARAM_DETAILS where REPORT_ID = '" + reportId1 + "' order by 3";
                            retObj = db.execSelectSQL(qry);
                            HashMap inMap;
                            HashMap notinMap;
                            inMap = (HashMap) collect.operatorFilters.get("IN");
                            notinMap = (HashMap) collect.operatorFilters.get("NOTIN");
    int limit=reportParam.size();
                            if (reportParam != null && !reportParam.isEmpty()) {
                                StringBuilder stringbuilder = new StringBuilder();
                                ArrayList al1 = null;
                                viewbynames = new ArrayList<String>();
                                parameterlist = new ArrayList<String>();
                                List al2 = null;
                                List notal2 = null;

                                List<String> qfilters = new ArrayList<String>();
                                qfilters = container.getqfilters();
                                String[] a1 = (String[]) (reportParam.keySet()).toArray(new String[reportParam.size()]);
                                for (int j = 0; j < reportParam.size(); j++) {
                                    al1 = (ArrayList) reportParam.get(a1[j]);
                                    al2 = (List) al1.get(8);

                                    if (qfilters != null && !qfilters.isEmpty()) {
                                        if (qfilters.contains(retObj.getFieldValueString(j, "ELEMENT_ID"))) {
                                            viewbynames.add(retObj.getFieldValueString(j, "PARAM_DISP_NAME"));
                                            parameterlist.add(retObj.getFieldValueString(j, "ELEMENT_ID"));
                                        }
                                    } else {
                                        viewbynames.add(retObj.getFieldValueString(j, "PARAM_DISP_NAME"));
                                        parameterlist.add(retObj.getFieldValueString(j, "ELEMENT_ID"));
                                    }
                                    al12 = (List<String>) inMap.get(a1[j]);
                                     notal2 = (List<String>) notinMap.get(a1[j]);
                                  if(adhocviewby!=null && adhocviewby.toString().equalsIgnoreCase("true")){
                                      limit=reportParam.size();
                                            }else{
                                        limit=reportParam.size();
                                            }
                                if(j<limit){
                                    if (al12.size() >= 1 || notal2.size()>=1) {
                                        if (qfilters != null && !qfilters.isEmpty()) {
                                            if (qfilters.contains(retObj.getFieldValueString(j, "ELEMENT_ID"))) {
                                                if (!al12.get(0).toString().equalsIgnoreCase("All") ) {
                                                    viewbynamess.add(al1.get(1).toString());
                                                    parameterlistt.add(a1[j]);
                                                }else{
                                                    if(notal2!=null && notal2.size()>=1 && !notal2.get(0).toString().equalsIgnoreCase("All")){
                                                          viewbynamess.add(al1.get(1).toString());
                                                    parameterlistt.add(a1[j]);
                                                }
                                            }
                                            }
                                        } else {
                                            if (!al12.get(0).toString().equalsIgnoreCase("All")) {
                                                viewbynamess.add(al1.get(1).toString());
                                                parameterlistt.add(a1[j]);
                                            }
                                            else{
                                                    if(notal2!=null && notal2.size()>=1 && !notal2.get(0).toString().equalsIgnoreCase("All")){
                                                          viewbynamess.add(al1.get(1).toString());
                                                    parameterlistt.add(a1[j]);
                                        }
                                    }
                                                           }
                                                       }
                                                           }
                                                       }
                                if (viewbynamess.isEmpty()) {
                                    viewbynamess = viewbynames;
                                    parameterlistt = parameterlist;
                                } else {
                                    if (viewbynamess.size() >= 1) {
                                        for (int j = 0; j < viewbynames.size(); j++) {
                                            if (viewbynamess.contains(viewbynames.get(j))) {
                                            } else {
                                                viewbynamess.add(viewbynames.get(j));
                                                parameterlistt.add(parameterlist.get(j));
                                            }
                                        }
                                    }
                                }
                                List<String> filterids = new ArrayList<String>();
                                int size1 = viewbynamess.size();
                                int size = 6;
//                                if(selecteddata!=null && selecteddata!=""){ //changed by sruthi for filters
                                    //size=Integer.parseInt(selecteddata);
//                                }else{
                                //int size = 6;
                                if (viewbynamess.size() > 6) {
                                    size =viewbynamess.size();
                                } else {
                                    size = viewbynamess.size();
                                }
//                            }
                                int setgblflag = 0;
                                String key1;String selectid;
                                int i1 = 0;


                                String key;
                                if(size>0){  //changed by sruthi for showfilters
                                for (int j1 = 0; j1 < size; j1++) {
                                    List<String> parameterlistNames = new ArrayList<String>();

                                    if (setgblflag == j1) {
                                        String idgbl = "globalfilterrow" + setgblflag;
                        %>
                           <script type="text/javascript">parent.sizeoffilters='<%=size%>'</script>
                        <div  id="<%=idgbl%>" class='itemtable' style="vertical-align: top;margin-top: 0px;margin-left: 15px;">
                            <%setgblflag = setgblflag + 6;
                                }
                                key1 = viewbynamess.get(j1).toString();


                                key1 = key1.toString().replace(" ", "1q1").replace("/", "11q");
                                key1 = "table" + key1;
    selectid=parameterlistt.get(i1)+"__"+key1;
                                if (reportParam1 != null && !reportParam1.isEmpty()) {

                                    ArrayList alids = null;

                                    alids = (ArrayList) reportParam1.get(parameterlistt.get(i1));
                                    al12 = (List<String>) inMap.get(parameterlistt.get(i1));
                                    notal2 = (List<String>) notinMap.get(parameterlistt.get(i1));
                                    String notin = "false";
                                    if (al12!=null&&al12.size() >= 1 && !al12.get(0).toString().equalsIgnoreCase("All")) {
                                    } else {
                                        if (notal2 != null && notal2.size() > 0) {
                                            notin = "true";
                                        }
                                    }
                            %>
                            <select title=<%=notin%> name="<%=parameterlistt.get(i1)%>"  id=<%=selectid%>  multiple style="">
                                <%
                                    // al12 = (List) alids.get(8);
                                    if (al12!=null&&al12.size() >= 1) {
                                        if (al12!=null&&al12.get(0).toString().equalsIgnoreCase("All")) {

                                            if (notal2 != null && notal2.size() > 0) {
                                                for (int l = 0; l < notal2.size(); l++) {
                                                    //Added by Ram
                                                try{
                                                    if (lookup.containsKey(al12.get(l))) {
                                                        al12.set(l, lookup.get(al12.get(l)));
                                                    }
													}catch(Exception e){
														e.printStackTrace();
													}
                                                    // endded by ram
                                                    String value = notal2.get(l) + "_" + l + "_" + parameterlistt.get(i1);
                                                    if (!notal2.get(l).toString().equalsIgnoreCase("All")) {
                                                        value = value + "_selecttrue";

                                %>

                                <option value="<%=value%>" ><%=notal2.get(l).toString().replace("]", "").replace("[", "")%></option>

                                <%  }
                                        }
                                    }
                                } else {
                                    for (int l = 0; l < al12.size(); l++) {
                                        //Added by Ram
                                        if (lookup.containsKey(al12.get(l))) {
                                            al12.set(l, lookup.get(al12.get(l)));
                                        }

                                        // endded by ram
                                        String value = al12.get(l) + "_" + l + "_" + parameterlistt.get(i1);
                                    if (!al12.get(l).toString().equalsIgnoreCase("All")) {if(j1>=6){morefiltercolor="true";}
                                            value = value + "_selecttrue";

                                %>

                                <option value="<%=value%>" ><%=al12.get(l).toString().replace("]", "").replace("[", "")%></option>

                                <%  }
                                                }
                                            }

                                        }
                                    }

                                %>
                                <%

                                %>
                            </select>
<!--                            &nbsp;&nbsp;&nbsp;&nbsp;-->

                            <%
                    if ((setgblflag - 1) == j1) {%>

                        </div>
                        <% }
                                    i1++;
                                }

                            }
                            }

                        %>
                    </div></div></div>
            </tr>
            <% } %>
            <!--end of sandeep code-->
            <!--added by manik-->
            <tr>
                <!--        <div id="xtendChartTD" style="display: none;vertical-align: top">-->

                <!--Added by Shivam for ie9 Issue-->
            <script type="text/javascript">
                                $(document).ready(function(){
                                    if($("#Datetype").val()!=undefined)
                                    $("#Datetype").select2({minimumResultsForSearch: Infinity});


                        var ie9 = (navigator.userAgent.match(/msie/i) && navigator.userAgent.match(/9/));
                                if (ie9){
                        // $("#themeselect2").css({"top":"116px", "width":"100px" ,"height":"220px", "vertical-align":"top", "position":"absolute", "cursor":"pointer", "background-color":"rgb(238, 238, 238)", "right":"50px", "left":"auto"});
                        $("#themeselect2").css({"width":"100px", "vertical-align":"top", "position":"absolute", "cursor":"pointer", "background-color":"rgb(238, 238, 238)", "right":"10%", "left":"auto"});
                        }
                        else{
                        }
                        //                                $("#iframe1").css("margin-top","5px")

          <%if(morefiltercolor.equalsIgnoreCase("true")){%>
              $("#moretb").hide();
              $("#greenfiltertb").show();
          <%}%>
                        });
                                function resizeHichart()
                                {
                                $("#Hichart1").height(($(window).height()) - 164 + "px");
                                        $("#Hichart1").width($(window).width());
                                }
            </script>
            <div id="xtendChartssTD" style="display: none;vertical-align: top;margin-top: 30px">
                <div id="gridsterDiv" class="gridster" style="position:fixed;float: left;padding-bottom: 100px;margin-bottom: 138px;background-color: #ECEFF1; width:100% ;  overflow: auto;" ondrop="drop(event, this.id)" class="dragClass" ondragover="allowDrop(event)">
                    <ul id="gridUL" type="none" style="width: 100%">

                    </ul>
                </div>
            </div>
            <div id="xtendChartTD" style="display: none;vertical-align: top;margin: 40px 0px 0px 0px;">


                <div id="xtendProp" style="display:block;width:100%;height:auto;margin-top:0%" >
                </div>
                <div id="xtendChart" style="float:left;width:100%" ></div>
                <!--           <div id="addUnderConsideration" style="display: none" title="">
                                                     <img style="margin-left:27%" src="images/under_construction.png">
                              </div>-->
            </div>
            </tr>

            </table>
            <div id="divassign"  style="display:none;width:100%;height:100%;position:centre;" title="<%=TranslaterHelper.getTranslatedInLocale("Assign_Report_to_Users", cL)%>">
                <iframe name="divassignframe" id="divassignframe" style="width:100%;height:100%;border:none;" ></iframe>
            </div>
            <div id="tempDashletDiv" style="display: none"></div>
            <div id="addActionChartDiv" style="display: none"></div>
            <div id="pngcon" style='display:none'></div>
            <div id="reportSequence" style="display: none">
            </div>
            <div id="chartOPtions" title="Chart Selection" style="display:none">
            </div>
            <div id="measureFilters" title="Measure Filters" style="display:none">
            </div>
            <div id="advanceProperties" title="Properties" style="display:none">
            </div>
            <div id="quickTrend" style="overflow: visible;display: none"></div>
            <div id="openGraphSection" style="overflow: visible;display: none"></div>
            <div id="pdfExportPageSetup" style="overflow: visible;display: none"></div>
            <div id="advanceChartList" style="overflow: visible;display: none"></div>
            <div id="progenlogo" style="overflow: visible;display: none"></div>
            <div id="legendchart" style="overflow: visible;display: none"></div>
            <div id="openTableTile" style="overflow: visible;display: none"></div>
            <div id="imageTagDiv" style="overflow: visible;display: none"></div>

 <% if(defaulttab!=null && (defaulttab.equalsIgnoreCase("Report") || defaulttab.equalsIgnoreCase("TABLE") || defaulttab.equalsIgnoreCase(""))) {    %>
            <!--// added by krishan-->
<div id="showExports1" title="<%=TranslaterHelper.getTranslatedInLocale("Exports_Report", cL)%>" style="display:none">
                <iframe id="showExportsFrame1" NAME='showExportsFrame1' width="100%" height="100%"  frameborder="0" ></iframe>
            </div>

            <form name="searchForm" method="post" style="padding:0pt" action=""></form>
            <!--<form action="" method="POST" id="graphForm">
                             <input type="hidden" id="viewby" name="viewby"/>
            </form>-->
            <IFRAME NAME="downFrame1" ID="downFrame1" STYLE="display:none;width:0px;height:0px" SRC="TableDisplay/pbDownload.jsp" frameborder="0"></IFRAME>
            <div id="startPagePriv1"  title="Login Start Page" STYLE='display:none'>
                <!--         <div id="startPagePriv1"  title="Login Start Page" STYLE='display:block'>-->
                <iframe  height="100%" width="100%" frameborder="0" id="startPageFrame"></iframe>
            </div>

           <% } %>
            <div  id="custom_color_picker1" style="display:none"></div>
            <div  id="div_background"  style="display: none"> </div>
            <div  id="resetGraphs" style="display: none"> </div>
            <script type="text/javascript">
                                var dragId = "";
                                var dragIdArray = [];
                                function createAdvanceVisualNew(){
                                createAdvanceVisual(parent.$("#graphsId").val());
                                }
                        function getAdvanceVisualsNew(){
                        getAdvanceVisuals(parent.$("#graphsId").val());
                        }
                        function regenerateFiltersNew(){
                        regenerateFilters(parent.$("#graphsId").val());
                        }
                        function generateJsonDataNew(){
                        generateJsonData(parent.$("#graphsId").val());
                        }
                        function buildChartsInTrendsNew(){
                        buildChartsInTrends(parent.$("#graphsId").val());
                        }
                        function trendAnalysisActionJsNew(repId){
                        //    trendAnalysisActionJs(parent.$("#graphsId").val());
                        var trendFlag = "Trend";
                                resetglobalfilters(repId)
                                parent.$("#multigblrefresh").val("reset")
                                trendAnalysisActionJs(repId, trendFlag, 11);
                        }
                        function generateJsonDataReset(repId,pageChange){
                        newProp="";
                        var graphFlag = "Graph";
                        if(typeof pageChange==='undefined'){
                           if(typeof parent.currentPage==='undefined'){
                            if(typeof currentPage==='undefined'){
                            currentPage='default';
                        }
                        }
                           pageChange = parent.currentPage;
                        }
//                                resetglobalfilters(repId)
//                                parent.$("#multigblrefresh").val("reset")
                                generateJsonData(repId, graphFlag, gridster1,pageChange);
                        }
                        function drag(ev, id)
                        {

                        ev.dataTransfer.setData("Text", id);
                                dragId = id

                        }

                        function allowDrop(ev)
                        {
                        ev.preventDefault();
                        }

                        function drop(ev, id)
                        {
                        dragIdArray.push(id);
                                ev.preventDefault();
                                if (id != "gridsterDiv"){
                        draganddrop1(dragId, id)
                        } else{
                        draganddrop(dragId, dragIdArray, id)
                        }

                        }
                        function optionSelect(){
                        //        $("#themeselect2").toggle();
                        //$("#lmOption").onmouseover()
                        $("#themeselect2").show();
                        }
                        function dateSelect(){
                        $("#dateselect2").toggle(200);
                        }
function savecurrent_defaulttab(current_defaulttab,current_reportId){
//            alert("current_defaulttab="+current_defaulttab+"      current_reportId= "+current_reportId);

                                var urlxx = $("#contextpath").attr("value") + '/reportTemplateAction.do?templateParam=saveCurrentTabAndReportId&current_defaulttab=' + current_defaulttab + '&current_reportId=' + current_reportId;
                                $.ajax({
                        url:urlxx,
                                success:function(data){
                        },
                                error:function(){
                      //  alert("Sorry your current deault tab is not saved.");
                     }
                        });

}

                        function loadingMenu(text)
                        {
			if(text=='Graph'){
			$("#landingPageredirect").css("display", "none");
				
			}else{
			$("#landingPageredirect").css("display", "block");

				}

                            var   current_reportId = document.getElementById("REPORTID").value;
                            savecurrent_defaulttab(text,current_reportId);
                            window.current_Tab_Flag =text;
                                <%if (defaulttab != null && !defaulttab.equalsIgnoreCase("null") && !defaulttab.equalsIgnoreCase("") &&  !defaulttab.equalsIgnoreCase("table") && !defaulttab.equalsIgnoreCase("report")) {

                        if (session.getAttribute("reloadFlag") != null) {

                             reloadFlag = (Map<String,String>) session.getAttribute("reloadFlag");
                             String status = (String)reloadFlag.get(PbReportId);
                             String status2=(String)request.getAttribute("reloadFlag");

                             if(status == null ){
                             reloadFlag.put(PbReportId, "false");
                            session.setAttribute("reloadFlag", reloadFlag);
                            %>
                                    parent.document.getElementById('loading').style.display = '';
                                    document.forms.frmParameter.action = "reportViewer.do?reportBy=viewReport&action=open&REPORTID=" + document.forms.frmParameter.REPORTID.value +"";
                                    document.forms.frmParameter.submit();
    <%
                             }
                            else if(status2 == null){
                                request.setAttribute("reloadFlag", "false");
                                         %>
                                             parent.document.getElementById('loading').style.display = '';
                                    document.forms.frmParameter.action = "reportViewer.do?reportBy=viewReport&action=open&REPORTID=" + document.forms.frmParameter.REPORTID.value +"";
                                    document.forms.frmParameter.submit();
                                        <%

                            }

                        } else {
                            %>if(text == "Table"){
                                    <%
                            reloadFlag.put(PbReportId, "false");
                            request.setAttribute("reloadFlag", "false");
                            session.setAttribute("reloadFlag", reloadFlag);%>
                                     parent.document.getElementById('loading').style.display = '';
                                    document.forms.frmParameter.action = "reportViewer.do?reportBy=viewReport&action=open&REPORTID=" + document.forms.frmParameter.REPORTID.value +"";
                                    document.forms.frmParameter.submit();
                            }


    <%}}%>



                        $(window).resize();
                     
                                //   alert("hello2"+text)
                     if (text == 'Table'){
                                $("#pageList").empty();        
                                $("#morePages").empty();        
                        var html1 = '<input  type="button" class="navtitle-hover" value=" Go "  style="height: 20px; width: 25px" onclick="submitform()"/>'
                                $("#visualGO").html(html1);
                                $("#dateToggle").html('');
                                var html1 = '<a href="javascript:void(0)" class="ui-icon ui-icon-transferthick-e-w" title="Toggle" onclick="datetoggl1("","","No")"></a>';
                                $("#dateToggle").html(html1);
                                if (parent.isfilteraplied){
                        var keys = Object.keys(parent.filterMapNew)
                                for (var i in keys){
                        var elemntid = keys[i];
                                var selectedFilters1 = [];
                                selectedFilters1 = parent.filterMapNew[elemntid]
                                parent.filterMapNewtb[elemntid] = selectedFilters1
                        }
                        setnotinfilters();
                        parent.document.getElementById('loading').style.display = '';
                                document.forms.frmParameter.action = "reportViewer.do?reportBy=viewReport&action=paramChange&REPORTID=" + document.forms.frmParameter.REPORTID.value;
                                document.forms.frmParameter.submit();
                        } else{
                        var keys = Object.keys(parent.filterMapNewtb)
                                for (var i in keys){
                        var elemntid = keys[i];
                                var selectedFilters1 = [];
                                selectedFilters1 = parent.filterMapNewtb[elemntid]
                                parent.filterMapNew[elemntid] = selectedFilters1
                        }
                        if (checkBrowser() == "ie") {

                        $('#rightDiv1table').hide();
                                $("#xtendChartssTD").css({'margin-top':'0px'});
                        } else{
                        $('#rightDiv1table').show();
                        }
                        $('#SwHiFil').show();
                                $("#lmGraph img").removeClass("imgShadow");
                                $("#lmTrend img").removeClass("imgShadow");
                                $("#lmAdvance img").removeClass("imgShadow");
                                $("#lmTable img").addClass("imgShadow");
//                                var iframe = document.getElementById("iframe1");
//                                var iframeContent = (iframe.contentWindow || iframe.contentDocument);
//                                iframeContent.hideGraphSec(text);
                                if($("#repCtrlDiv").is(":visible")){
                                    $("#iframe1").height(($(window).height())-120+"px");
                                }
                                else{
                                    $("#iframe1").height(($(window).height())-95+"px");
                                }
                                $("#iframe1").width($(window).width()-5);
                                //                $("#tableOption").css({'margin-left': '1285px'});
                                $("#tableOption").css({'display':''});
                                $("#menuBarUITR").css({'display':''});
                                $("#reportTD1").show();
                                $("#xtendChartTD").hide();
                                $("#xtendChartssTD").hide();
                                $('#menuBarUI').show();
                                $("#reportTD1").show();
                                $('#addUnderConsideration').hide();
                                $('#rightDiv1').hide();
                                $('#rightDiv1ad').hide();
                                $('#rightDiv1trend').hide();
                                $("#footerTable").css({'display':'block'});
                                $("#reportTD1").css({'border':''});
                                $("#companySection").css({'display':'block'});
                                //goRefresh('<%=request.getParameter("REPORTID")%>');

                        }
                        }
                        else if (text == 'Graph')
                        {
                        var AOId=parent.document.getElementById("AOId").value;
                         var aoAsGoId = parent.document.getElementById("aoAsGoId").value;
//                         alert("aoAsGoId::"+aoAsGoId)
                        var REPORTID = parent.document.getElementById("REPORTID").value;
    if(typeof REPORTID ==="undefined" || REPORTID===""){
        REPORTID = $("#graphsId").val();
    }
//                        alert(AOId)

                         if((aoAsGoId==null || typeof aoAsGoId ==="undefined" || aoAsGoId=="") )
                       {
                      if(!(AOId==null || typeof AOId ==="undefined" || AOId=="" ))
                      {

                         useAOasGO(AOId,REPORTID);
                      }
                      }

                        $("#visualGO").html('');
                                var url = "reportViewer.do?reportBy=viewReport&action=paramChange&DEFAULT_TAB=Graph";
                                var html1 = '<input  type="button" class="navtitle-hover" value=" Go "  style="height: 20px; width: 25px" onclick="submitformGraph(\'' + url + '\')"/>'
                                $("#visualGO").html(html1);
                                $("#dateToggle").html('');
                                var url1 = "&DEFAULT_TAB=Graph";
                                var html1 = '<a href="javascript:void(0)" class="ui-icon ui-icon-transferthick-e-w" title="Toggle" onclick="datetoggleForGraph(\'' + url1 + '\')"></a>';
                                $("#dateToggle").html(html1);
                                $("#lmTrend img").removeClass("imgShadow");
                                $("#lmAdvance img").removeClass("imgShadow");
                                $("#lmTable img").removeClass("imgShadow");
                                $("#lmGraph img").addClass("imgShadow");
                                $("#loading").show();
//                                var iframe = document.getElementById("iframe1");
//                                var iframeContent = (iframe.contentWindow || iframe.contentDocument);
//                                iframeContent.hideGraphSec(text);
                                $("#tableOption").css({'display':'none'});
                                counttt++;
                                //            $("#footerTable").hide();
                                $("#SwHiFil").hide();
                                var graphFlag = "Graph";
                                $('#addUnderConsideration').hide();
                                $('#xtendChartTD').hide();
                                //            $("#reportTD1").show();
                                $("#noneDataDiv").css({'display':'none'});
                                //            $("#menuBarUITR").css({'display':'none'});
                                //             document.getElementById("paramHeaderId").style.display='none';
                                //            $("#footerTable").css({'display':'none'});
                                if($("#managementTempl").is(":visible")){$("#gridsterDiv").height($(window).height()-300);}
                                else{$("#gridsterDiv").height(($(window).height()) - 220 + "px");}
                                $("#gridsterDiv").width($(window).width() + "px");
                                $("#gridsterDiv").css({'display':'block'});
                                $("#reportTD1").css({'border':'hidden'});
                                $("#companySection").css({'display':'none'});
                                if (checkBrowser() == "ie") {

                        $('#rightDiv1').hide();
                                $("#xtendChartssTD").css({'margin-top':'0px'});
                        } else{
                        $('#rightDiv1').show();
                        }


                        //     $('#globalfilterrow').show();
                        $('#rightDiv1ad').hide();
                                $('#rightDiv1table').hide();
                                $('#rightDiv1trend').hide();
                                //goRefresh('<%=request.getParameter("REPORTID")%>');
                                //   $("#gridUL").width();
                                //   alert("gridWidth   "+$("#gridUL").width())

                                //        document.getElementById('addUnderConsideration').style.display = "none";
                                if (counttt == 1){
                        generateJsonData1st('<%=request.getParameter("REPORTID")%>', graphFlag, gridster1);
                        } else{
                        $("#gridsterDiv").css({'display':'block'});
                                //$("#reportTD1").hide();

                                generateJsonData('<%=request.getParameter("REPORTID")%>', graphFlag, gridster1);
                        }
                        $("#noneDataDiv").css({'display':'none'});
                                //generateJsonData('<%=request.getParameter("REPORTID")%>',graphFlag,gridster1);
                                //alert("gridHeight   "+$("#gridUL").height())
                        }
                        else if (text == 'advance') {

                        $("#visualGO").html('');
                                var url = "reportViewer.do?reportBy=viewReport&action=paramChange&DEFAULT_TAB=AdvanceVisuals";
                                var html = '<input  type="button" class="navtitle-hover" value=" Go "  style="height: 20px; width: 25px" onclick="submitformGraph(\'' + url + '\')"/>'
                                $("#visualGO").html(html);
                                $("#dateToggle").html('');
                                var url1 = "&DEFAULT_TAB=AdvanceVisuals";
                                var html1 = '<a href="javascript:void(0)" class="ui-icon ui-icon-transferthick-e-w" title="Toggle" onclick="datetoggleForGraph(\'' + url1 + '\')"></a>';
                                $("#dateToggle").html(html1);
                                $("#lmTable img").removeClass("imgShadow");
                                $("#lmGraph img").removeClass("imgShadow");
                                $("#lmTrend img").removeClass("imgShadow");
                                $("#lmAdvance img").addClass("imgShadow");
                                $("#loading").show();
//                                var iframe = document.getElementById("iframe1");
//                                var iframeContent = (iframe.contentWindow || iframe.contentDocument);
//                                iframeContent.hideGraphSec(text);
                                $("#menuBarUITR").css({'display':'none'});
                                $("#tableOption").css({'display':'none'});
                                //                 $("#reportTD1").show();
                                //    $("#reportTD1").hide();
                                // $("#footerTable").css({'display':'block'});
                                $("#gridsterDiv").css({'display':'none'});
                                $("#xtendChartssTD").css({'display':'none'});
                                $("#reportTD1").css({'border':'hidden'});
                                $("#companySection").css({'display':'none'});
                                //               $("#menuBarUITR").css({'display':'none'});
                                // document.getElementById("paramHeaderId").style.display='none';
                                $("#SwHiFil").hide();
                                $("#content_1").hide();
                                $('#rightDiv1').hide();
                                $('#rightDiv1table').hide();
                                if (checkBrowser() == "ie") {

                        $('#rightDiv1ad').hide();
                                $("#xtendChartssTD").css({'margin-top':'0px'});
                        } else{
                        $('#rightDiv1ad').show();
                        }

                        $('#rightDiv1trend').hide();
                                var graphFlag = "Graph";
                                getAdvanceVisuals('<%=request.getParameter("REPORTID")%>', graphFlag);
                                // runtimeglobalfilters(filterMapad,'null',"advance");

                        }
                        else {

                        $("#visualGO").html('');
                                var url = "reportViewer.do?reportBy=viewReport&action=paramChange&DEFAULT_TAB=Trends";
                                var html = '<input  type="button" class="navtitle-hover" value=" Go "  style="height: 20px; width: 25px" onclick="submitformGraph(\'' + url + '\')"/>'
                                $("#visualGO").html(html);
                                $("#dateToggle").html('');
                                var url1 = "&DEFAULT_TAB=Trends";
                                var html1 = '<a href="javascript:void(0)" class="ui-icon ui-icon-transferthick-e-w" title="Toggle" onclick="datetoggleForGraph(\'' + url1 + '\')"></a>';
                                $("#dateToggle").html(html1);
                                $("#lmAdvance img").removeClass("imgShadow");
                                $("#lmTable img").removeClass("imgShadow");
                                $("#lmGraph img").removeClass("imgShadow");
                                $("#lmTrend img").addClass("imgShadow");
                                //         $("#footerTable").css({'display':'block'});
                                var trendFlag = "Trend";
                                //               $("#reportTD1").show();
                                var flag = "trendAnalysisAction";
                                var Measures = "trendAnalysisActionMeasures";
                                $("#gridsterDiv").css({'display':'block'});
                                $("#xtendChartssTD").css({'display':'none'});
                                $("#companySection").css({'display':'none'});
                                //               $("#menuBarUITR").css({'display':'none'});
//                                var iframe = document.getElementById("iframe1");
//                                var iframeContent = (iframe.contentWindow || iframe.contentDocument);
//                                iframeContent.hideGraphSec(text);
                                $("#tableOption").css({'display':'none'});
                                //                document.getElementById("paramHeaderId").style.display='none';
                                $("#footerTable").css({'display':'block'});
                                $("#reportTD1").css({'border':'hidden'});
                                $('#xtendChartTD').hide();
                                $('#rightDiv1').hide();
                                $('#rightDiv1table').hide();
                                $('#rightDiv1ad').hide();
                                if (checkBrowser() == "ie") {

                        $('#rightDiv1trend').hide();
                                $("#xtendChartssTD").css({'margin-top':'0px'});
                        } else{
                        $('#rightDiv1trend').show();
                        }

                        //$("#content_1").show();
                        $("#loading").show();
                                trendAnalysisActionJs('<%=request.getParameter("REPORTID")%>', trendFlag, gridster1);
                                runtimeglobalfilters(filterMaptrend, 'null', "trend");
                                //        callReportLevelGraph('<%=request.getParameter("REPORTID")%>',trendFlag,gridster1);
                        }
                        }
                        function resetglobalfilters(repId){
                        parent.isfilteraplied = true;
                                var keys = Object.keys(filterMapglobal)
                                for (var i in keys){
                        var elemntid = keys[i];
                                var selectedFilters1 = [];
                                selectedFilters1 = filterMapglobal[elemntid]
                                parent.filterMapNewtb[elemntid] = selectedFilters1
                        }
                        var keys = Object.keys(parent.filterMapNew)
                                for (var i in keys){
                        var elemntid = keys[i];
                                $("#CBOARP" + elemntid).val("");
                        }
                        }
            </script>
            <!--        //added by krishan pratap-->
                    <div id="initializeRepDiv" name="initializeRepDiv" title="Initialize Report">
       <form id="initializeRepForm" name="initializeRepForm" action="" method="post"></form>
     </div>
            <div id="AllSheets" style="height:195px;top:100px;display:none;" Title="<%=TranslaterHelper.getTranslatedInLocale("Default_Tab", cL)%>">
                <table><tr>
                        <td align="left" colspan="1"  style="font-size:small; background-color: #B4D9EE; width: 50%;  padding: 0.6em; border: 1px solid #CCC; "><label ><center class="gFontFamily "><%=TranslaterHelper.getTranslatedInLocale("Default_Tab", cL)%></center></label></td>
                        <td  style="padding: 0.6em;">
                            <select name="TabId" id="TabId" style="width:150px;"><option class="gFontFamily "> --SELECT-- </option>
                                <%for (int i = 0; i < reportTabs.length; i++) {
                            if (reportTabs[i].equalsIgnoreCase(defaulttab)) {%>
                                <option class="gFontFamily " selected value="<%=reportTabs[i]%>" ><%=reportTabs[i]%></option>
                                <%} else {%>
                                <option class="gFontFamily " value="<%=reportTabs[i]%>"><%=reportTabs[i]%></option>
                                <%}
                        }%>
                            </select>
                        </td>
                    </tr>
                    <tr><td align="left" colspan="1"  style="font-size:small; background-color: #B4D9EE; width: 50%;  padding: 0.6em; border: 1px solid #CCC; "><label><center><%=TranslaterHelper.getTranslatedInLocale("Show_Type", cL)%></center></label></td>
                                    <%if(!showicons){ %>
                        <td style="padding: 0.6em;"> <input type="radio" id="defaultName" name="type" value="name" checked > <%=TranslaterHelper.getTranslatedInLocale("Name", cL)%>
                            <%}else{%>
                        <td style="padding: 0.6em;"> <input type="radio" id="defaultName" name="type" value="name" > <%=TranslaterHelper.getTranslatedInLocale("Name", cL)%>
                            <%}%>
                            <br>
                            <%if(showicons){ %>
                            <input type="radio" id="defaultIcons" name="type" value="icons" checked > <%=TranslaterHelper.getTranslatedInLocale("Icons", cL)%></td>
                            <%}else{%>
                    <input type="radio" id="defaultIcons" name="type" value="icons"> <%=TranslaterHelper.getTranslatedInLocale("Icons", cL)%></td>
                    <%}%>
                    </tr>
                    <tr><td align="left" colspan="1"  style="font-size:small; background-color: #B4D9EE; width: 50%;  padding: 0.6em; border: 1px solid #CCC; "><label><center><%=TranslaterHelper.getTranslatedInLocale("Show_Icon", cL)%></center></label></td>
                        <td style="padding: 0.6em;">  <ul class="chk-container">
                                <!--                      //added by krishan pratap-->
                                <%if(showall=="none"){ %>

                                <li><input class='defChk' type="checkbox" id="selecctall" value="all" ><%=TranslaterHelper.getTranslatedInLocale("All", cL)%></li>
                                    <%if(showiconreport=="block"||defaulttab.equalsIgnoreCase("Report")){%>
                                <li><input class="defChk checkbox1" type="checkbox" id="report" name="check[]" value="report" checked> <%=TranslaterHelper.getTranslatedInLocale("Data_Analysis", cL)%></li>
                                    <%}else{%>
                                <li><input class="defChk checkbox1" type="checkbox" id="report" name="check[]" value="report" > <%=TranslaterHelper.getTranslatedInLocale("Data_Analysis", cL)%></li>
                                    <%}if(showicongraph=="block" || defaulttab.equalsIgnoreCase("Graph")){%>
                                <li><input class="defChk checkbox1" type="checkbox" id="graph" name="check[]" value="graph" checked> <%=TranslaterHelper.getTranslatedInLocale("dashboard", cL)%></li>
                                    <%}else{%>
                                <li><input class="defChk checkbox1" type="checkbox" id="graph" name="check[]" value="graph" ><%=TranslaterHelper.getTranslatedInLocale("dashboard", cL)%></li>

                                <%}if(showiconadvancevisual=="block" || defaulttab.equalsIgnoreCase("AdvanceVisuals")){%>
<!--                                <li><input class="checkbox1" type="checkbox"  id="visual"name="check[]" value="visual" checked>  <%=TranslaterHelper.getTranslatedInLocale("Visual", cL)%></li>-->
                                    <%}else{%>
<!--                                <li><input class="checkbox1" type="checkbox"  id="visual"name="check[]" value="visual" >  <%=TranslaterHelper.getTranslatedInLocale("Visual", cL)%></li>-->

                                <%}}else{%>
                                <li><input class='defChk' type="checkbox" id="selecctall" value="all" checked>  <%=TranslaterHelper.getTranslatedInLocale("All", cL)%></li>
                                <li><input class="defChk checkbox1" type="checkbox" id="report" name="check[]" value="report" checked> <%=TranslaterHelper.getTranslatedInLocale("Data_Analysis", cL)%></li>
                                <li><input class="defChk checkbox1" type="checkbox" id="graph" name="check[]" value="graph"  checked> <%=TranslaterHelper.getTranslatedInLocale("dashboard", cL)%></li>
                                <!--                     <li><input class="checkbox1" type="checkbox" id="trends" name="check[]" value="trends" checked> Trends</li>-->
<!--                                <li><input class="checkbox1" type="checkbox"  id="visual"name="check[]" value="visual" checked>  <%=TranslaterHelper.getTranslatedInLocale("Visual", cL)%></li>-->
                                    <%}%>
                                <!--                     ended by krishan pratap-->
                            </ul></td></tr>
                    <br>
                    <tr><td colspan="2" title='Click Here to Save Your Default Tab for this Report' style='align:center; width: 5%;  padding: 0.6em;'>
                            <center> <input id='Ar' class='gFontFamily  prgBtn'  value='<%=TranslaterHelper.getTranslatedInLocale("save", cL)%>' type='button' onclick="SaveDefTab('<%=reportId1%>')"></center></td></tr>
                </table>
            </div>
<!--            <div id="setGORefresh" style="display:none" title="<%=TranslaterHelper.getTranslatedInLocale("Reset_Refresh_Graphs", cL)%>">
                <table border="0" style="width:100%;">


                    <tr valign="top">
                        <td valign="top"  style="width:40%;"> <label class="gFontFamily  label"><%=TranslaterHelper.getTranslatedInLocale("Reset_GO", cL)%></label> </td>
                        <% //if (isResetGO.equalsIgnoreCase("true")) {%>
                        <td   valign="top" style="width:60%;"><input type="checkbox"  checked name="resetGO"  id="resetGO"  ></td>
                            <% //} else {%>
                        <td   valign="top" style="width:60%;"><input type="checkbox"   name="resetGO"  id="resetGO"  ></td>
                            <% //}%>
                    </tr>

                    <tr valign="top">
                        <td valign="top"  style="width:40%;"> <label class="gFontFamily  label"><%=TranslaterHelper.getTranslatedInLocale("Refresh_Graph", cL)%></label> </td>
                        <% //if (isGraphRefresh.equalsIgnoreCase("true")) {%>
                        <td   valign="top" style="width:60%;"><input type="checkbox" checked  name="refreshGraph"  id="refreshGraph"  ></td>
                            <% //} else {%>
                        <td   valign="top" style="width:60%;"><input type="checkbox"   name="refreshGraph"  id="refreshGraph"  ></td>
                            <% //}%>
                    </tr>
                    <tr><td colspan="2" title='Click Here to Save ' style='align:center; width: 5%;  padding: 0.6em;'>
                            <center> <input id='Ar' class='gFontFamily  navtitle-hover'  value='<%=TranslaterHelper.getTranslatedInLocale("save", cL)%>' type='button' onclick="saveResetGraph('<%=reportId1%>')"></center></td></tr>
                </table>
            </div>-->
            <div id="colorsDiv1" style="display: none" title="Select color">
                <center>
                    <input type="text" id="customColor1" class="gFontFamily " style="" value="#12345" >
                    <div id="colorpicker1" style=""></div>
                    <input type="button" align="center" value="Done" class="navtitle-hover gFontFamily " onclick="saveSelectedColor1()">
                    <input type="button" align="center" value="Cancel" class="navtitle-hover gFontFamily " onclick="cancelColor()">

                </center>
            </div>
            <div id="initializeCharts" style="display: none"></div>
            <div id="appliedFilters" style="overflow-y: auto;display: none;border-radius: 10px;border: 1px solid grey;background-color: white;padding: 10px;width: auto;height: auto;"></div>
            <div id="viewByList" class='dropdowncss' style="overflow-y: auto;z-index: 11;border-radius: 5px;display: none;border: 1px solid grey;background-color: white;padding: 10px;width: auto;height: auto;"></div>
            <div id="showFilters" class='dropdowncss' style="overflow-y: auto;display: none;border-radius: 10px;border: 1px solid grey;background-color: white;padding: 20px;width: auto;height: auto;"></div>
            <div id="pagesList" class='dropdowncss' style="overflow-y: auto;z-index: 11;border-radius: 5px;display: none;border: 1px solid grey;background-color: white;padding: 10px;width: auto;height: auto;z-index:99999"></div>
            <div id="createMultiSavePointForReport" title="<%=TranslaterHelper.getTranslatedInLocale("Create_New_or_Overwrite_SavePoint", cL)%>" style="display:none">
            <table ><tr><td><input type='radio' name='createorchange' id='createorchange' value='' checked="checked" onclick="getCollectandContainerObjectInfo('show')"><%=TranslaterHelper.getTranslatedInLocale("Create_Overwrite", cL)%></td><td>&nbsp;&nbsp;&nbsp;&nbsp;</td><td><input type='radio' name='createorchange' id='createorchange' value='' onclick="getCollectandContainerObjectInfo('change')"><%=TranslaterHelper.getTranslatedInLocale("Change_Default_Savepoint", cL)%></td></tr></table><br>
            <div id="createMultiSavepointRep">
            </div>
            <script type="text/javascript">
            
                                $(window).resize(function(){
                                    if($("#repCtrlDiv").is(":visible")){
                    $("#iframe1").height(($(window).height())-120+"px");
                    }
                    else{
                        $("#iframe1").height(($(window).height())-95+"px");
                    }
                    $("#iframe1").width($(window).width()-5);
                                });
                                $(window).load(function(){
                        if (checkBrowser() == "ie")   {
                        $("#rightDiv1").remove();
                                $("#rightDiv1table").remove();
                                $("#rightDiv1ad").remove();
                                $("#rightDiv1trend").remove();
                        }

                <%if (timeinfo.get(1).equalsIgnoreCase("PRG_DATE_RANGE")){%>
                        dateConverstionRange('load');
                <%}else{%>
                    if(userflag =="veraction"){
                    $("#hdTimeCtrlDiv").css("margin-top","30px");
                    }else{
                    
                    $("#hdTimeCtrlDiv").css("margin-top","17px");
                        }
                        dateConverstion('3', 'load');
                <%}%>
                        });
                                $(document).mouseout(function (e)
                        {
                        var container = $("#appliedFilters");
                                if (!container.is(e.target) // if the target of the click isn't the container...
                                && container.has(e.target).length === 0) // ... nor a descendant of the container
                        {
                        container.hide(500);
                        }
                        });
                                $("#xtendChartssTD").mouseover(function(){
                        var container = $("#themeselect2");
                                container.hide(500);
//                                var iframe = document.getElementById("iframe1");
//                                var iframeContent = (iframe.contentWindow || iframe.contentDocument);
//                                iframeContent.hideGraphSec('<%=defaulttab%>');
                        });
                                $("#headerTable").mouseover(function(){
                        var container = $("#themeselect2");
                                container.hide(500);
                        });
                                $("#xtendChartTD").mouseover(function(){
                        var container = $("#themeselect2");
                                container.hide(500);
//                                var iframe = document.getElementById("iframe1");
//                                var iframeContent = (iframe.contentWindow || iframe.contentDocument);
//                                iframeContent.hideGraphSec('<%=defaulttab%>');
                        });
                                /*******************************
                                 Author:-Sandeep Nagapuri
                                 Module:- This function is used to create and display more filters.
                                 *******************************/
                                        /*** Function Start ***/
                                                function showmorefiltersg(id){var html = "";
                                                        if ($("#mmoreFiltersg").is(":visible"))
                                                        $(".hideAllDiv").hide();
                                                        else
                                                        $(".hideAllDiv").hide();
                                                       var fsize=6;
                                                     //  viewbynamesold=viewbynamesold.split(",")
                                                      // parameterlistold=parameterlistold.split(",")
                <%
                            
                                       
                                      if (viewbynamesold != null && !viewbynamesold.isEmpty()) {
                                          if (viewbynames!=null && viewbynames.size() > 6) {%>
                                              for (var j1 = fsize; j1 < viewbynamesold.length; j1++) {
                                                  var paramid = parameterlistold[j1].replace("[","").replace("]","");
                                                var fid = id+paramid.toString().trim();
                                                        var elemname = viewbynamesold[j1].toString().replace("[","").replace("]","");
                                                        elemname = elemname.replace(" ", "1q1").replace(" ", "1q1").replace(" ", "1q1").replace(" ", "1q1");
                                                        var selectedlist; var selnotinlist; var elementid = paramid
                                                        selectedlist = parent.filterMapgraphs[elementid];
                                                           selnotinlist = parent.filterMapNotinDB[elementid];
                             if (selnotinlist !== "" && selnotinlist !== undefined && selnotinlist.length >= 1 && selnotinlist[0] != "All"){
                       html += "<span id='spang" + fid + "' style='background-color:#76E1BD;' class='ftddropmnum' onclick=lovfiltersreptable(\"" + elemname + "\",\"" + paramid + "\",\"" + id + "\",'graph','null') >"+viewbynamesold[j1]+"<span style='float:right;padding:5px'><i class='fa fa-chevron-down'></i> </span> </span><ul id=" + fid + " style='height:180px;width:100%;' class='ftddropmnu'></ul>"
   }

                                                        else if (selectedlist !== "" && selectedlist !== undefined && selectedlist.length >= 1 && selectedlist[0] != "All"){
                                                if (id == 'moreFiltersg'){
                                                html += "<span id='spang" + fid + "' style='background-color:lightgray;' class='ftddropmnum' onclick=lovfiltersreptable(\"" + elemname + "\",\"" + paramid + "\",\"" + id + "\",'graph','null') >"+viewbynamesold[j1].toString().replace("[","").replace("]","")+"<span style='float:right;padding:5px'><i class='fa fa-chevron-down'></i> </span> </span><ul id=" + fid + " style='height:180px;width:100%;' class='ftddropmnu'></ul>"
                                                } else{
                                                if (id == 'moreFiltersad'){
                                                html += "<span id='spanad" + fid + "' style='background-color:lightgray;' class='ftddropmnum' onclick=lovfiltersreptable(\"" + elemname + "\",\"" + paramid + "\",\"" + id + "\",'advance','null') >"+viewbynamesold[j1].toString().replace("[","").replace("]","")+"<span style='float:right;padding:5px'><i class='fa fa-chevron-down'></i> </span> </span><ul id=" + fid + " style='height:180px;width:100%;' class='ftddropmnu'></ul>"

                                                }
                                                }
                                                } else{
                                                if (id == 'moreFiltersg'){
                                                html += "<span id='spang"+fid +"' class='ftddropmnum' onclick=lovfiltersreptable(\"" + elemname + "\",\"" + paramid.toString().trim() + "\",\"" + id + "\",'graph','null') >"+viewbynamesold[j1].toString().replace("[","").replace("]","")+"<span style='float:right;padding:5px'><i class='fa fa-chevron-down'></i> </span> </span><ul id=" + fid + " style='height:180px;width:100%;' class='ftddropmnu'></ul>"
                                                } else{
                                                if (id == 'moreFiltersad'){
                                                html += "<span id='spanad" + fid + "' class='ftddropmnum' onclick=lovfiltersreptable(\"" + elemname + "\",\"" + paramid + "\",\"" + id + "\",'advance','null') >"+viewbynamesold[j1].toString().replace("[","").replace("]","")+"<span style='float:right;padding:5px'><i class='fa fa-chevron-down'></i> </span> </span><ul id=" + fid + " style='height:180px;width:100%;' class='ftddropmnu'></ul>"

                                                }
                                                }
                                                }
                                                    }
                                                   <%  }
                                                }%>
                //  html+="<input type='button' style='margin-left:8em;height:20px;margin-top:1em;background-color:#f2f2f2;' onclick=tableMenuApply(this) value='Apply'>";
                                                $("#m" + id).toggle(200)
                                                        $("#" + id).html(html)
                                                }
                                        function showmorefilters(id){
                                        $("#tableOption").fadeToggle("fast");
                                                var html = "";
                                                if ($("#mmoreFilters").is(":visible"))
                                                $(".hideAllDiv").hide();
                                                else
                                                $(".hideAllDiv").hide();
                <%
                                      if (viewbynamess != null && !viewbynamess.isEmpty()) {
                                          for (int j1 = 6; j1 < viewbynamess.size(); j1++) {
                                              String paramid = parameterlistt.get(j1);%>
                                        var fid = id + '<%=paramid%>'
                                                var elemname = '<%=viewbynamess.get(j1)%>';
                                                elemname = elemname.replace(" ", "1q1").replace(" ", "1q1").replace(" ", "1q1").replace(" ", "1q1");
                                                var selectedlist; var selnotinlist; var elementid = '<%=paramid%>'
                                                selectedlist = parent.filterMapNew[elementid];
                                                selnotinlist = parent.filterMapNotin[elementid];
                                                var notinflag='<%=notInMore.get(parameterlistt.get(j1))%>';
                                                if (selnotinlist !== "" && selnotinlist !== undefined && selnotinlist.length >= 1){
                                           html += "<span id='span" + fid + "' style='background-color:#76E1BD;' class='ftddropmnum' onclick=lovfiltersreptable(\"" + elemname + "\",'<%=paramid%>',\"" + id + "\",'table') ><%=viewbynamess.get(j1)%><span style='float:right;padding:5px'><i class='fa fa-chevron-down'></i> </span> </span><ul id=" + fid + " style='height:200px;width:100%;'  class='ftddropmnu'></ul>"

                                                }
                                                else if (selectedlist !== "" && selectedlist !== undefined && selectedlist.length >= 1 && selectedlist[0] != "All"){
                                        html += "<span id='span" + fid + "' style='background-color:lightgray;' class='ftddropmnum' onclick=lovfiltersreptable(\"" + elemname + "\",'<%=paramid%>',\"" + id + "\",'table') ><%=viewbynamess.get(j1)%><span style='float:right;padding:5px'><i class='fa fa-chevron-down'></i> </span> </span><ul id=" + fid + " style='height:200px;width:100%;'  class='ftddropmnu'></ul>"

                                        } else{
                                        html += "<span id='span" + fid + "' class='ftddropmnum' onclick=lovfiltersreptable(\"" + elemname + "\",'<%=paramid%>',\"" + id + "\",'table') ><%=viewbynamess.get(j1)%><span style='float:right;padding:5px'><i class='fa fa-chevron-down'></i> </span> </span><ul id=" + fid + " style='height:200px;width:100%;'  class='ftddropmnu'></ul>"
                                        }
                                        //                                          html += "<span id='span"+fid+"' class='ftddropmnum' onclick=lovfiltersreptable(\""+elemname+"\",'<%=paramid%>',\""+id+"\",'table') ><%=viewbynamess.get(j1)%><span style='float:right;padding:5px'><i class='fa fa-chevron-down'></i> </span> </span><ul id="+fid+" style='height:150px;overflow:auto;' onmouseover=scrollin(\""+id+"\") onmouseout=scrollout(\""+id+"\") class='ftddropmnu'></ul>"
                <% }
                                                }
                %>//  html+="<input type='button' style='margin-left:8em;height:20px;margin-top:1em;background-color:#f2f2f2;' onclick=tableMenuApply(this) value='Apply'>";
                                        //     $("#tableOption").fadeToggle("fast");
                                        $("#m" + id).toggle(200)
                                                $("#" + id).html(html)
                                        }
                                        function dateConverstion(id, time){

                                        var months = new Array(12);
                                                months[1] = "Jan";
                                                months[2] = "Feb";
                                                months[3] = "Mar";
                                                months[4] = "Apr"
                                                months[5] = "May"
                                                months[6] = "Jun"
                                                months[7] = "Jul"
                                                months[8] = "Aug"
                                                months[9] = "Sep"
                                                months[10] = "Oct"
                                                months[11] = "Nov"
                                                months[12] = "Dec"
                                                var datetype;
                                           if(userflag!="" && userflag=="veraction"){   
                                         if(parent.$('#optMonthly3').is(':checked')){
                                             datetype='Monthly'
                                         }
                                             if (parent.$('#optQuarterly3').is(':checked')){ 
                                                 datetype='Quarterly'
                                             }
                                       if (parent.$('#optYearly3').is(':checked')) {
                                       datetype='Yearly'
                                       }
                                        if (parent.$('#optDaily3').is(':checked')) {//added by sruthi for daily
                                       datetype='Daily'
                                       }//ended by sruthi
                                  }else{     
                                           
datetype = $("#Datetype").val();
                                           }
                                                var converteddate;
                                                if (datetype=='Monthly'){

                                          
                                        parent.$('#CBO_PRG_PERIOD_TYPE').val("Month")
                                                var selectedmonth = $("#select2-selectMonth" + id + "-container").text();
                                                var selectedmonthval = $("#selectMonth" + id).val();
                                                var gComp = $("#select2-compSelect" + id + "-container").text();var lastday=$("#dateslectmotnh").val();
                                                 if(userflag!="" && userflag=="veraction"){ lastday="01";}
                                                var selectedyear = $("#select2-selectYear" + id + "-container").text();
                                                var dateformat = selectedmonth + " " + lastday + "," + selectedyear;
                                                var dateformat1 = selectedmonth + " " + lastday + "," + selectedyear;
                                                var d = new Date(dateformat);
                                                var d1 = new Date(dateformat1);
                                                var lastDay = new Date(d1.getFullYear(), d1.getMonth() + 1, 0);
                                               if(userflag!="" && userflag=="veraction"){ 
                                              lastday = lastDay.toString().split(" ")[2];}
                                                var day = weekday[d.getDay()];
                                                parent.$('#CBO_PRG_COMPARE').val("Last Month")

                                                if (gComp == 'MOM'){
                                        parent.$('#CBO_PRG_COMPARE').val("Last Month");parent.$('#gCompq').val("Last Month");
                                        } else if (gComp == 'MOY'){
                                        parent.$('#CBO_PRG_COMPARE').val("Same Month Last Year");parent.$('#gCompq').val("Same Month Last Year");
                                        }else if(gComp == 'CMOM'){
                                            parent.$('#CBO_PRG_COMPARE').val("Complete Last Month");parent.$('#gCompq').val("Complete Last Month");
                                        }else if(gComp == 'CMOY'){parent.$('#CBO_PRG_COMPARE').val("Complete Same Month Last Year");parent.$('#gCompq').val("Complete Same Month Last Year");}
                                        converteddate = day + "," + lastday + "," + selectedmonthval + "," + selectedyear;
                                        }
                                          //added by sruthi for daily
                                        if (datetype=='Daily'){
                                            parent.$('#CBO_PRG_PERIOD_TYPE').val("Daily") 
                                             var selectedmonth = $("#select2-selectMonth" + id + "-container").text();
                                              alert("selectedmonth..."+selectedmonth)
                                               var selectedmonthval = $("#selectMonth" + id).val();
                                               alert("selectedmonthval..."+selectedmonthval)
                                               var lastday=$("#dateslectDaily").val();
                                              alert("lastday..."+lastday)
                                           var gComp = $("#select2-compSelect" + id + "-container").text();
                                            alert("gComp..."+gComp)
                                                var selectedyear = $("#select2-selectYear" + id + "-container").text();
                                                 alert("selectedyear..."+selectedyear)
                                                var dateformat = selectedmonth + " " + lastday + "," + selectedyear;
                                                var dateformat1 = selectedmonth + " " + lastday + "," + selectedyear;
                                                  var d = new Date(dateformat);
                                                var d1 = new Date(dateformat1);
                                               // var lastDay = new Date(d1.getFullYear(), d1.getMonth() + 1, 0);
                                                var day = weekday[d.getDay()];
                                                parent.$('#CBO_PRG_COMPARE').val("Previous Day")
                                                if (gComp == 'DOD'){
                                        parent.$('#CBO_PRG_COMPARE').val("Previous Day");
                                        parent.$('#gCompq').val("Previous Day");
                                        } else if (gComp == 'DOW'){
                                        parent.$('#CBO_PRG_COMPARE').val("Same Day Last Week");
                                        parent.$('#gCompq').val("Same Day Last Week");
                                        }else if (gComp == 'DOM'){
                                        parent.$('#CBO_PRG_COMPARE').val("Same Day Last Month");
                                        parent.$('#gCompq').val("Same Day Last Month");
                                        }else if (gComp == 'DOY'){
                                        parent.$('#CBO_PRG_COMPARE').val("Same Day Last Year");
                                        parent.$('#gCompq').val("Same Day Last Year");
                                        }
                                        converteddate = day + "," + lastday + "," + selectedmonthval + "," + selectedyear;
                                        alert("converteddate...."+converteddate)
                                        }//ended by sruthi
                                          if(userflag!="" && userflag=="veraction"){ }else{
                                         if (datetype!=undefined &&datetype=='Weekly'){
                                                parent.$('#CBO_PRG_PERIOD_TYPE').val("Week")
                                                var selectedmonth = $("#select2-selectMonth" + id + "-container").text();
                                                var selectedmonthval = $("#selectMonth" + id).val();var lastday=$("#dateslectWeek").val();
                                                var gComp = $("#select2-compSelect" + id + "-container").text();
                                                var selectedyear = $("#select2-selectYear" + id + "-container").text();
                                                var dateformat = selectedmonth + " " + lastday + "," + selectedyear;
                                                var dateformat1 = selectedmonth + " " + lastday + "," + selectedyear;
                                                var d = new Date(dateformat);
                                                var d1 = new Date(dateformat1);
                                                var lastDay = new Date(d1.getFullYear(), d1.getMonth() + 1, 0);

                                                var day = weekday[d.getDay()];
                                                parent.$('#CBO_PRG_COMPARE').val("Last Week")
                                                if (gComp == 'WOW'){
                                        parent.$('#CBO_PRG_COMPARE').val("Last Week");parent.$('#gCompq').val("Last Week");
                                        } else if (gComp == 'WOY'){
                                        parent.$('#CBO_PRG_COMPARE').val("Same Week Last Year");parent.$('#gCompq').val("Same Week Last Year");
                                        }else if (gComp == 'CWOW'){
                                        parent.$('#CBO_PRG_COMPARE').val("Complete Last Week");parent.$('#gCompq').val("Complete Last Week");
                                        }else if (gComp == 'CWOY'){
                                        parent.$('#CBO_PRG_COMPARE').val("Complete Same Week Last Year");parent.$('#gCompq').val("Complete Same Week Last Year");
                                        }
                                        converteddate = day + "," + lastday + "," + selectedmonthval + "," + selectedyear;
                                        }
                                        }
                                        var reportid = '<%=reportId1%>'
                                                if (datetype!=undefined &&datetype=='Quarterly'){
                                        parent.$('#CBO_PRG_PERIOD_TYPE').val("Qtr")
                                                var selectedqtr = $("#select2-selectQuater" + id + "-container").text(); parent.$("#qtrdate").val(selectedqtr);
                                                var gCompq = $("#select2-compSelect" + id + "-container").text();
                                                var selectedqtryear = $("#select2-selectYear" + id + "-container").text();parent.$("#newUIyear").val(selectedqtryear);
                                                parent.$('#CBO_PRG_COMPARE').val("Last Qtr")
                                                if (gCompq == 'QOQ'){
                                        parent.$('#CBO_PRG_COMPARE').val("Last Qtr");parent.$('#gCompq').val("Last Qtr");
                                        } else if (gCompq == 'QOY'){
                                        parent.$('#CBO_PRG_COMPARE').val("Same Qtr Last Year");parent.$('#gCompq').val("Same Qtr Last Year");
                                        } else if (gCompq == 'CQOQ'){
                                        parent.$('#CBO_PRG_COMPARE').val("Complete Last Qtr");parent.$('#gCompq').val("Complete Last Qtr");
                                        } else if (gCompq == 'CQOY'){
                                        parent.$('#CBO_PRG_COMPARE').val("Complete Same Qtr Last Year");parent.$('#gCompq').val("Complete Same Qtr Last Year");
                                        }
           
                                        $.ajax({
                                        type: 'POST',
                                                async: false,
                                                cache: false,

                                                url:"<%=request.getContextPath()%>/reportViewerAction.do?reportBy=getQtrData&selectedqtr=" + selectedqtr + "&reportid=" + reportid + "&selectedqtryear=" + selectedqtryear + "&gComp=" + gComp,
                                                success:function(data)
                                        {
                                        var data1 = new Array();
//                                            alert(JSON.stringify(data))
                                                data1 = data.toString().split(" ");
                                                var periodsplit = new Array()
                                                var perioddate = data1[0].split("-");
                                                var month = perioddate[1].replace("0", "")

                                                var year = perioddate[0];
                                                var days = perioddate[2]
                                                var monthname = months[month];
                                                var qdateformat = monthname + " " + days + "," + year;
                                                var qd = new Date(qdateformat);
                                                var qday = weekday[qd.getDay()];
                                                converteddate = qday + "," + days + "," + monthname + "," + year;
                                                //  parent.$('#datepicker').val(perioddate);
                                        }
                                        });
                                        }


                                        if (datetype!=undefined &&datetype=='Yearly') {
                                        var year = $("#select2-selectYear" + id + "-container").text(); parent.$("#newUIyear").val(year);
                                           var gCompy = $("#select2-compSelect" + id + "-container").text();
                                                parent.$('#CBO_PRG_PERIOD_TYPE').val("Year")
                                                parent.$('#CBO_PRG_COMPARE').val("Last Year")
                                                if (gCompy == 'YOY'){
                                        parent.$('#CBO_PRG_COMPARE').val("Last Year");parent.$('#gCompq').val("Last Year");
                                        }
                                        $.ajax({
                                        type: 'POST',
                                                async: false,
                                                cache: false,
//                                                timeout: 30000,
                                                url:"<%=request.getContextPath()%>/reportViewerAction.do?reportBy=getYearData&year=" + year + "&reportid=" + reportid + "&gComp=" + gComp,
                                                success:function(data)
                                        {
                                        var data2 = new Array()

                                                data2 = data.toString().split(" ");
                                                var periodsplit1 = new Array()
                                                var perioddate1 = data2[0].split("-");
                                                var month1 = perioddate1[1].replace("0", "")

                                                var year1 = perioddate1[0];
                                                var days1 = perioddate1[2]
                                                var monthname1 = months[month1];
                                                var ydateformat = monthname1 + " " + days1 + "," + year1;
                                                var yd = new Date(ydateformat);
                                                var yday = weekday[yd.getDay()];
                                                converteddate = yday + "," + days1 + "," + monthname1 + "," + year1;
                                                //  parent.$('#datepicker').val(perioddate);
                                        }
                                        });
                                        }

                                        parent.$('#perioddate').val(converteddate);
                                                // alert(parent.$('#perioddate'))

                                                var perioddate = parent.$('#perioddate').val();
                                                parent.$('#datetext').val('topdate');
                                                $.ajax({
                                        type: 'POST',
                                                async: false,
                                                cache: false,
//                                                timeout: 30000,
                                                url:"<%=request.getContextPath()%>/reportViewer.do?reportBy=dateParse&perioddate=" + perioddate,
                                                success:function(data)
                                        {
                                        var data1 = new Array()
                                                data1 = data.toString().split(",");
                                                var perioddate = data1[0];
                                                parent.$('#datepicker').val(perioddate);
                                        }
                                        });
                                                if (time == 'load'){} else{
                                        submitform();
                                        }
                                        }

               function ShowFiltersreportpage(ctxpath,reportid){
               var viewbynamess='<%=viewbynamess.size()%>';
               ShowFilters(ctxpath,reportid,viewbynamess);
               }
    function savePointXtCharts(){
            $.ajax({
                                type: 'POST',
                                async: false,
                                cache: false,
//                                timeout: 30000,
                                url:"<%=request.getContextPath()%>/reportViewerAction.do?reportBy=setSavePoint&savePoint="+true,
                                success:function(data)
                                {
                                 saveLocalXtCharts();
               }
                                });
             }
             function loadGlobalGraphs(){
            $.ajax({
                                type: 'POST',
                                async: false,
                                cache: false,
//                                timeout: 30000,
                                url:"<%=request.getContextPath()%>/reportViewerAction.do?reportBy=setSavePoint&savePoint="+false,
                                success:function(data)
                                {
                                 generateJsonData('<%=request.getParameter("REPORTID")%>', 'Graph', gridster1);
                                }
                                });
             }
             function publishGraphs(ctxPath,isRestrictedPowerAnalyserEnable,isMultiCompany,isPowerAnalyserEnableforUser){
            $.ajax({
                                type: 'POST',
                                async: false,
                                cache: false,
//                                timeout: 30000,
                                url:"<%=request.getContextPath()%>/reportViewerAction.do?reportBy=setSavePoint&savePoint="+true,
                                success:function(data)
                                {
                                 saveXtCharts(ctxPath,isRestrictedPowerAnalyserEnable,isMultiCompany,isPowerAnalyserEnableforUser);
                                }
                                });
             }
        /*** Ended by Ashutosh***/
       function updatefiltersrow(){
        var html=""
        <%
          session = request.getSession(false);
            session.setAttribute("parameterlistold",parameterlistold);
        session.setAttribute("viewbynamesold",viewbynamesold);
        %>
                var flag=false;
        var updatefilter=false
          $.ajax({
                                        type: 'POST',
                                                async: false,
                                                cache: false,
                                                timeout: 30000,
                                                url:"<%=request.getContextPath()%>/reportViewerAction.do?reportBy=getapplyedfilters&infilters="+JSON.stringify(parent.filterMapgraphs)+"&notinfilters="+JSON.stringify(parent.filterMapNotinDB)+"&reportId="+document.forms.frmParameter.REPORTID.value,
                                                success:function(data)
                                        {
                                        var jsonVar = eval('(' + data + ')');
                                      var html1=jsonVar.completeContent[0];
                                       parameterlistold=jsonVar.parameterlistnew;
                                       viewbynamesold=jsonVar.viewbynamesnew;
                                    
                                   $("#updaterowgraph").html(html1);
                                             }   
                                    });
                                    <%
                                     if (allFiltersnames != null && !allFiltersnames.isEmpty() && viewbynames != null && !viewbynames.isEmpty()) {
                                         String key15; Set keySet15 = allFiltersnames.keySet();
                                            for (int j15 = 0; j15 < viewbynames.size(); j15++) {
                        List<String> parameterlistNames = new ArrayList<String>();
                        Iterator itr15 = keySet15.iterator();
                        while (itr15.hasNext()) {

                            key15 = itr15.next().toString();
                            if (key15.equalsIgnoreCase(viewbynames.get(j15))) {
                                key15 = key15.replace(" ", "1q1");String selectidg=parameterlist.get(j15)+"__"+key15;
                               

            %>
                    displayFilt1('<%=selectidg%>');
                 
            
                                          
                                            <%}  }}}%>
                                        
                                    
    }
            </script>
            </body>
            <input type="hidden" value="<%=request.getContextPath()%>"   id="contextpath"/>
            </html>

            <%}%>
