<%--
    Document   : pbAdhocReport
    Created on : Nov 12, 2009, 1:26:25 PM
    Author     : sankeerthana@progenbusiness.com
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" import="prg.db.PbReturnObject,com.progen.reportdesigner.db.ReportTemplateDAO,utils.db.ProgenParam,com.progen.action.UserStatusHelper,com.progen.report.PbReportCollection"%>
<%@page import="prg.util.screenDimensions,java.util.ArrayList,prg.db.Container,java.util.HashMap,prg.db.PbDb,com.progen.report.display.DisplayParameters"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<%
                     ArrayList params = new ArrayList();
//                         DisplayParameters displayParameters=new DisplayParameters();
//                         params=displayParameters.designerParams(request.getContextPath());
                    String roleid=(String)request.getAttribute("roleid");
                    String bizRole = roleid;
                    String rolesid = roleid;
                    
                         response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
            String ReportId = "";
            ReportId = String.valueOf(request.getAttribute("ReportId"));
            if(ReportId.equalsIgnoreCase("null")){
            ReportId=request.getParameter("REPORTID");
            }
 PbReportCollection collect = new PbReportCollection();
         ArrayList<String> timeinfo = collect.timeDetailsArray;
Container container = Container.getContainerFromSession(request, ReportId);
//added by Nazneen
 PbReturnObject returnObject = null;
        String setUpCharVal = "";
        String sytm="No";
PbDb pbdb = new PbDb();

        String sql = "SELECT SETUP_CHAR_VALUE FROM PRG_GBL_SETUP_VALUES WHERE SETUP_KEY = 'GLOBAL_PARAMS'";
       try {
            returnObject= pbdb.execSelectSQL(sql);
	    if (returnObject.getRowCount() > 0) {
                setUpCharVal = returnObject.getFieldValueString(0, 0);
            }
            if(setUpCharVal!=null && !setUpCharVal.equalsIgnoreCase("") && !setUpCharVal.equalsIgnoreCase("null")){
                String arrSetUpCharVal[] = setUpCharVal.split(";") ;
                    for(int i=0;i<arrSetUpCharVal.length;i++){
                        String temp = arrSetUpCharVal[i];
                        if(temp.contains("isYearCal")){
                            sytm = temp.substring(temp.indexOf("~")+1);
                        }
                    }
            }
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        //ended  by Nazneen
        container.setcustomsetting(setUpCharVal);
         if(container.prevStateCnt == 55){
             container.prevStateCnt = 0;
         }else{
             container.prevStateCnt = container.prevStateCnt + 1 ;
         }
            boolean isReportAccessible = container.isReportAccessible();
         String isGraphThere = null;
         boolean isQDEnableforUser=false;

        %>
<%
            //for clearing cache
            ServletContext context = getServletContext();
            boolean isPowerAnalyserEnableforUser=false;
            String userType = null;
            HashMap<String,UserStatusHelper> statushelper;
     statushelper=(HashMap)context.getAttribute("helperclass");
     UserStatusHelper helper=new UserStatusHelper();
     if(!statushelper.isEmpty()){
        helper=statushelper.get(request.getSession(false).getId());
        if(helper!=null){
            isQDEnableforUser=helper.getQueryStudio();
        isPowerAnalyserEnableforUser=helper.getPowerAnalyser();
        userType=helper.getUserType();
        }
     }
            String ViewFrom = "Designer";
            session.setAttribute("ViewFrom", ViewFrom);
            boolean showExtraTabs = true;

            String loguserId = String.valueOf(session.getAttribute("USERID"));
            HashMap ParametersHashMap = null;
            HashMap TableHashMap = null;
            HashMap GraphHashMap = null;
            HashMap ReportHashMap = null;

            String PbReportId = "";
            String oldReportId = "";
            String reportId = "";
            String reportName = "";
            String reportDesc = "";
            HashMap map = new HashMap();
            String ReportFolders = "";
            String ReportDimensions = "";
            String ParamRegion = "";
            String ParamDispRegion = "";
            String TableRegion = "";
            String GraphRegion = "";
            String prevParamArray = "";
            String prevTimeParams = "";
           HashMap paramMap = new HashMap();
            HashMap paramTime = new HashMap();
            String prevREP = "";
            String prevCEP = "";
            String prevMeasures = "";
            String prevMeasureNames = "";
            String prevMeasureNamesList = "";

            ArrayList Parameters = new ArrayList();
            ArrayList ParametersNames = new ArrayList();

            ArrayList TimeParameters = new ArrayList();
            ArrayList TimeParametersNames = new ArrayList();


            ArrayList REP = new ArrayList();
            ArrayList CEP = new ArrayList();
            ArrayList Measures = new ArrayList();
            ArrayList MeasuresNames = new ArrayList();

            ArrayList repExclude = new ArrayList();
            ArrayList cepExclude = new ArrayList();

            String prevRepExclude = "";
            String prevCepExclude = "";
            HashMap GraphTypesHashMap = null;
            String[] grpTypeskeys = new String[0];
            String graphIds = "";
            String sqlStr = "";
             String parameters = "";
            String allParameters = "";
            String parametersTime = "";
             ArrayList timeArray = new ArrayList();
            if (request.getSession(false) != null && request.getSession(false).getAttribute("PROGENTABLES") != null) {

                try {
                    //commented by anitha in process of cleaning pi
                   // GraphTypesHashMap = (HashMap) session.getAttribute("GraphTypesHashMap");
                    //grpTypeskeys = (String[]) GraphTypesHashMap.keySet().toArray(new String[0]);

                    ReportId = String.valueOf(request.getAttribute("ReportId"));
                     if(ReportId.equalsIgnoreCase("null")){
                     ReportId=request.getParameter("REPORTID");
                        }
                    PbReportId = ReportId;
                    oldReportId = ReportId;
                    reportId = ReportId;
                    map = (HashMap) request.getSession(false).getAttribute("PROGENTABLES");

                    if (map.get(ReportId) != null) {
                        container = (prg.db.Container) map.get(ReportId);
                    } else {
                        container = new prg.db.Container();
                    }
                    reportName = container.getReportName();
                    reportDesc = container.getReportDesc();
                    // reportName=reportName.replace("'", "\'");
                    // reportDesc=reportDesc.replace("'", "\'");

                    ParametersHashMap = container.getParametersHashMap();
                    TableHashMap = container.getTableHashMap();
                    GraphHashMap = container.getGraphHashMap();
                    ReportHashMap = container.getReportHashMap();
                    sqlStr = container.getSqlStr();
                    // //("sqlstr in jsp is : " + sqlStr);
                    if (request.getAttribute("ReportFolders") != null) {
                        ReportFolders = String.valueOf(request.getAttribute("ReportFolders"));
                    }
                    if (request.getAttribute("ReportDimensions") != null) {
                        ReportDimensions = String.valueOf(request.getAttribute("ReportDimensions"));
                    }
                    if (request.getAttribute("ParamRegion") != null) {
                        ParamRegion = String.valueOf(request.getAttribute("ParamRegion"));
                    }
                    if (request.getAttribute("ParamDispRegion") != null) {
                        ParamDispRegion = String.valueOf(request.getAttribute("ParamDispRegion"));
                    }

                    if (request.getAttribute("TableRegion") != null) {
                        TableRegion = String.valueOf(request.getAttribute("TableRegion"));
                    }
                    if (request.getAttribute("GraphRegion") != null) {
                        GraphRegion = String.valueOf(request.getAttribute("GraphRegion"));
                    }

                    if (TableHashMap != null && TableHashMap.size() != 0) {

                        REP = (ArrayList) TableHashMap.get("REP");
                        CEP = (ArrayList) TableHashMap.get("CEP");
                        Measures = (ArrayList) TableHashMap.get("Measures");
                        MeasuresNames = (ArrayList) TableHashMap.get("MeasuresNames");

                        if (REP != null) {
                            for (int j = 0; j < REP.size(); j++) {
                                prevREP = prevREP + "," + REP.get(j);
                            }
                            if (!(prevREP.equalsIgnoreCase(""))) {
                                prevREP = prevREP.substring(1);
                            }
                        }
                        if (CEP != null) {
                            for (int j = 0; j < CEP.size(); j++) {
                                prevCEP = prevCEP + "," + CEP.get(j);
                            }
                            if (!(prevCEP.equalsIgnoreCase(""))) {
                                prevCEP = prevCEP.substring(1);
                            }
                        }
                        if (Measures != null && MeasuresNames != null) {
                            for (int j = 0; j < Measures.size(); j++) {
                                prevMeasures = prevMeasures + "," + Measures.get(j);
                                prevMeasureNames = prevMeasureNames + "," + MeasuresNames.get(j);
                                prevMeasureNamesList = prevMeasureNamesList + "," + MeasuresNames.get(j) + "-" + Measures.get(j);
                            }
                            if (!(prevMeasures.equalsIgnoreCase(""))) {
                                prevMeasures = prevMeasures.substring(1);
                                prevMeasureNames = prevMeasureNames.substring(1);
                                prevMeasureNamesList = prevMeasureNamesList.substring(1);
                            }
                        }
                    }
                    if (ParametersHashMap.get("Parameters") != null && ParametersHashMap.size() != 0) {
                        Parameters = (ArrayList) container.getParametersHashMap().get("Parameters");
                        ParametersNames = (ArrayList) container.getParametersHashMap().get("ParametersNames");
                    }

                    if (Parameters.size() != 0) {
                        for (int paramIndex = 0; paramIndex < Parameters.size(); paramIndex++) {
                            prevParamArray = prevParamArray + "," + String.valueOf(ParametersNames.get(paramIndex)) + "-" + String.valueOf(Parameters.get(paramIndex));
                        }
                        if (!(prevParamArray.equalsIgnoreCase(""))) {
                            prevParamArray = prevParamArray.substring(1);
                        }
                    }
                    if (ParametersHashMap.get("TimeParameters") != null) {
                        TimeParameters = (ArrayList) ParametersHashMap.get("TimeParameters");
                        TimeParametersNames = (ArrayList) ParametersHashMap.get("TimeParametersNames");
                    }
                    if (Parameters.size() != 0) {
                        for (int paramIndex = 0; paramIndex < Parameters.size(); paramIndex++) {
                            prevTimeParams = prevTimeParams + "," + String.valueOf(ParametersNames.get(paramIndex)) + "-" + String.valueOf(Parameters.get(paramIndex));
                        }
                        if (!(prevTimeParams.equalsIgnoreCase(""))) {
                            prevTimeParams = prevTimeParams.substring(1);
                        }
                    }

                    String UserFldsData = "";
                    if (request.getAttribute("UserFlds") != null) {
                        UserFldsData = String.valueOf(request.getAttribute("UserFlds"));
                    }
  paramMap = container.getParametersHashMap();
                    if (paramMap != null && paramMap.size() > 0) {

                       if(paramMap.get("Parameters")!=null){
                             params = (ArrayList) paramMap.get("Parameters");
                            }
                       if(params==null){
                        params=new ArrayList();
                    }

                        if((!params.isEmpty())&& params.get(0)!=null)
                            parameters = params.get(0).toString();

                        for (int i = 0; i < params.size(); i++) {
                            allParameters = allParameters + "," + params.get(i);
                        }
                        if(allParameters.length()>0)
                            allParameters = allParameters.substring(1);

                        paramTime = container.getParametersHashMap();
                        timeArray = (ArrayList) paramTime.get("TimeDetailstList");
                        HashMap timeDetsMap = (HashMap) paramTime.get("TimeDimHashMap");
                    }

                    if (ParametersHashMap.get("repExclude") != null && ParametersHashMap.get("cepExclude") != null) {
                        repExclude = (ArrayList) ParametersHashMap.get("repExclude");
                        cepExclude = (ArrayList) ParametersHashMap.get("cepExclude");

                        if (repExclude != null && repExclude.size() != 0) {
                            for (int j = 0; j < repExclude.size(); j++) {
                                prevRepExclude = prevRepExclude + "," + repExclude.get(j);
                            }
                            if (!(prevRepExclude.equalsIgnoreCase(""))) {
                                prevRepExclude = prevRepExclude.substring(1);
                            }
                        }
                        if (cepExclude != null && cepExclude.size() != 0) {
                            for (int j = 0; j < cepExclude.size(); j++) {
                                prevCepExclude = prevCepExclude + "," + cepExclude.get(j);
                            }
                            if (!(prevCepExclude.equalsIgnoreCase(""))) {
                                prevCepExclude = prevCepExclude.substring(1);
                            }
                        }
                    }
                    if (GraphHashMap != null && GraphHashMap.get("graphIds") != null) {
                        graphIds = String.valueOf(GraphHashMap.get("graphIds"));
                    }
                    //for clearing cache
                    response.setHeader("Cache-Control", "no-store");
                    response.setHeader("Pragma", "no-cache");
                    response.setDateHeader("Expires", 0);

                    screenDimensions dims = new screenDimensions();
                    int pageFont, anchorFont;
                    HashMap screenMap = dims.getFontSize(session, request, response);
                    ////.println("screenMap --" + screenMap.size());
                    if (!String.valueOf(screenMap.get("pageFont")).equalsIgnoreCase("NULL")) {
                        pageFont = Integer.parseInt(String.valueOf(screenMap.get("pageFont")));
                        anchorFont = Integer.parseInt(String.valueOf(screenMap.get("pageFont"))) + 1;
                        ////.println("pageFont--" + pageFont + "---anchorFont--" + anchorFont);
                    } else {
                        pageFont = 11;
                        anchorFont = 12;
                        ////.println("pageFont--" + pageFont + "---anchorFont--" + anchorFont);
                    }
                    Boolean IsrepAdhoc=container.getFlagg();
                   // PbDb pbdb = new PbDb();
                    String existparamvalues = "";
                    //String eledetsexistsQuery = "select MEMBER_VALUE from PRG_AR_PARAMETER_SECURITY where report_id=" + ReportId;
                    String eledetsexistsQuery = "delete from PRG_AR_PARAMETER_SECURITY where report_id=" + ReportId;
                    pbdb.execModifySQL(eledetsexistsQuery);

                     String ddformT = null;
                     if(session.getAttribute("dateFormat")!=null){
                    ddformT = session.getAttribute("dateFormat").toString();
                    }

                     if (session.getAttribute("USERID") == null || String.valueOf(screenMap.get("Redirect")).equalsIgnoreCase("Yes")) {
                        response.sendRedirect(request.getContextPath() + "/newpbLogin.jsp");
                    } else {

                     String contextPath=request.getContextPath();
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Report</title>
        <script src="<%=contextPath%>/javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/jquery.tablesorter.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/tablesorter/addons/pager/jquery.tablesorter.pager.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/jquery.columnfilters.js"></script>
        <script src="<%=contextPath%>/javascript/treeview/jquery.cookie.js" type="text/javascript"></script>
        <script src="<%=contextPath%>/javascript/treeview/jquery.treeview.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/treeview/demo.js"></script>
<script type="text/javascript" src="<%=contextPath%>/javascript/ui.datepicker.js"></script>
<!--        <script type="text/javascript" src="<%=request.getContextPath()%>/javascrip        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/queryDesign.js"></script>t/draggable/ui.core.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/draggable/ui.draggable.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/draggable/ui.droppable.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/ui.resizable.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/ui.accordion.js"></script>-->
<!--        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/ui.dialog.js"></script>-->
<!--        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/ui.sortable.js"></script>-->

<!--        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/effects.core.js"></script>-->
<!--        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/effects.explode.js"></script>-->
        <script type="text/javascript" src="<%=contextPath%>/javascript/reportDesign.js"></script>
<!--        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/chili-1.8b.js"></script>-->
<!--        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/docs.js"></script>-->
        <script src="javascript/jquery.contextMenu.js" type="text/javascript"></script>
        <link type="text/css" href="<%=contextPath%>/stylesheets/demos.css" rel="stylesheet" />
        <link type="text/css" href="<%=contextPath%>/stylesheets/pbReportViewerCSS.css" rel="stylesheet" />
        <link type="text/css" href="<%=contextPath%>/stylesheets/ui.all.css" rel="stylesheet" />
        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/treeviewstyle/jquery.treeview.css" />
        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/treeviewstyle/screen.css" />
        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/jq.css" type="text/css" media="print, projection, screen">
        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/style.css" type="text/css" media="print, projection, screen">
        <link href="<%=contextPath%>/stylesheets/javascript.css" type="text/css" rel="stylesheet">
        <link href="<%=contextPath%>/stylesheets/html.css" type="text/css" rel="stylesheet">
        <link href="<%=contextPath%>/stylesheets/css.css" type="text/css" rel="stylesheet">
        <link type="text/css" href="<%=contextPath%>/stylesheets/metadataButton.css" rel="stylesheet" />
        <link href="stylesheets/jquery.contextMenu.css" rel="stylesheet" type="text/css" />
        <link href="<%=contextPath%>/reportDesigner.css" type="text/css" rel="stylesheet">
        <script type="text/javascript" src="<%=contextPath%>/jQuery/jquery/ui/jquery.jBreadCrumb.1.1.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/jQuery/jquery/ui/jquery.easing.1.3.js"></script>
        <link type="text/css" href="<%=contextPath%>/jQuery/jquery/themes/base/BreadCrumb.css" rel="stylesheet" />
        <link type="text/css" href="<%=contextPath%>/jQuery/jquery/themes/base/Base.css" rel="stylesheet" />
        <link href="stylesheets/jquery.contextMenu.css" rel="stylesheet" type="text/css" />
        <script type="text/javascript" src="<%=contextPath%>/javascript/toolTip.js"></script>
        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/toolTip.css" type="text/css" />

        <script type="text/javascript" src="<%=contextPath%>/javascript/pbReportViewerJS.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/reportviewer/ReportViewer.js"></script>

        <script type="text/javascript" src="<%=contextPath%>/javascript/pbreporttemplateframejs.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/TableDisplay/JS/pbTableMapJS.js"></script>
         <script  type="text/javascript" src="<%=contextPath%>/javascript/pbReportViewerJS.js" ></script>
          <script type="text/javascript" src="<%=contextPath%>/TableDisplay/JS/pbGraphDisplayRegionJS.js"></script>
         <script type="text/javascript" src="<%=contextPath%>/javascript/pi.js"></script>
          <script type="text/javascript" src="<%=contextPath%>/javascript/quicksearch.js"></script>
            <script type="text/javascript" src="<%= contextPath%>//dragAndDropTable.js"></script>
            <link type="text/css" href="<%=contextPath%>/datedesign.css" rel="stylesheet"/>
          <style type="text/css">
.graphClass{
width:100%;
height:240px;
border-bottom:  skyblue groove;
  float:right;
}
.navtitle-hover{
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
    WIDTH: 100%;
    cursor:pointer;
}
.tableClass{
width:100%;
height:240px;
  float:right;
}
            .flagDiv{
                width:100px;
                height:100px;
                background-color:#ffffff;
                overflow:auto;
                position:absolute;
                text-align:left;
                border:1px solid #000000;
                border-top-width: 0px;
                z-index:1002;
            }
                        .flagDiv1{
                width:150px;
                height:300px;
                background-color:#ffffff;
                overflow:auto;
                position:absolute;
                text-align:left;
                border:1px solid #000000;
                border-top-width: 0px;
                z-index:1002;
                right: 0;

            }

               #ui-datepicker-div
            {
                z-index: 9999999;
            }
.myTextbox3 {
font-family: Verdana, Arial, Helvetica, sans-serif;
font-weight: normal;
font-size: 8pt;
color:#000000;
padding: 0px;
width:100px;
margin-left: 5px;
border-top: 1px groove #848484;
border-right: 1px inset #999999;
border-bottom: 1px inset #999999;
border-left: 1px groove #848484;
background-color:#FFFFFF;
/*apply this class to a TextBox/Textfield only*/
}


        </style>
         <style>
#dateregionDiv { position: fixed; top: 0px; left:200px; margin: auto; right: 800px; width: 50px; border: none; z-index: 50; }
#fixedtop1 { position: fixed; top: 0px; left:200px; margin: auto; right: 800px; width: 50px; border: none; z-index: 50; }
#center250a { width: auto;height: 65px;  background:none; }
#center250b { width: auto;height: 65px;   background:none; }
.ui-dialog{border: 1px solid #888;background-color: #fff}
.ui-dialog-titlebar{background-color: #8BC34A;}
.ui-icon-closethick{background-image: url(stylesheets/themes/Green/images/ui-icons_d8e7f3_256x240.png);
    background-position: -96px -128px;}
            </style>
            <%
            String reportId1= (String) request.getAttribute("REPORTID");

            %>
              <style>
#dateregionDiv { position: fixed; top: 0px; left:200px; margin: auto; right: 800px; width: 50px; border: none; z-index: 50; }
#fixedtop1 { position: fixed; top: 0px; left:200px; margin: auto; right: 800px; width: 50px; border: none; z-index: 50; }
#center250a { width: auto;height: 65px;  background:none; }
#center250b { width: auto;height: 65px;   background:none; }
            </style>
    </head>
    <body>
         <div id="dateregionDiv" style="display:none;">
     <div id="fixedtop1" >
     <div id="center250a">
     <div class="form clearFix">
      <span class="wr100">
       <table align="center" id="roundtrip">
       <tr width="100%">
        <td align="right">
            <% if(sytm.equalsIgnoreCase("No")){ %>
            <span class="top w100 mrtop" id="pfield1"><%=(container.fullName0)%></span>
        <span class="date">
        <small Style="font-weight: bold" id="pfield2"></small></span>
        <span class="bottom w100" id="pfield3"><%=container.day0%></span></td>
         <td> <input height="100px" width="100px" type="hidden" class="ui-datepicker " id="perioddate" name="perioddate" onchange="dateclick()" onclick="showdate()"/><td>
         <td> <input  type="hidden" id="datetext" name="datetext" value=""/></td>
         <%}else {%>
<!--         <td>
             <img src="<%=request.getContextPath()%>/images/calendar_18x16.gif" width="18" height="16" onclick=" saveyear2('perioddate')"></td>-->
<!--          <select id="calPeriodYear" name="calPeriodYear" style="width:90px" onchange="saveYearDetails('perioddate')">
        </select>-->
          <td>Year:&nbsp;</td><td align="right">
        <select id="calPeriodYear" name="calPeriodYear" style="width:90px" onchange="saveyear2('perioddate')">
                           <% String calndrYear = "mohit"+(container.fullName0);
                           if(!(calndrYear.equalsIgnoreCase("mohitnull")))
                                   {
                               calndrYear = (container.fullName0).substring(4);
                            for(int year=2005;year<=2020;year++){
                            if(calndrYear.equalsIgnoreCase(Integer.toString(year))){%>
                                <option selected value="<%=year%>"> <%=year%> </option>
                           <%}else {%>
                                <option value="<%=year%>"> <%=year%> </option>
                                <%}}}%>
        </select></td>
         <td><input height="100px" width="100px" type="hidden" id="perioddate" name="perioddate" onchange="" onclick=""/>
         </td>
         <td> <input  type="hidden" id="datetext" name="datetext" value=""/></td>
           <td>

       </td>
            <%}%>

          <td id="timebasistd"></td>
           <td id="saveTabId" ><a href="javascript:void(0)" style="text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;font-size: 9pt;" class="ui-icon ui-icon-disk" title="Global Save" onclick="saveRepotInDesigner()"></a></td>
           <td> <a onclick="openImgDiv()" href=""> Reset </a></td><td></td>
<!--           <td id="dateToggle"><a href="javascript:void(0)" class="ui-icon ui-icon-transferthick-e-w" title="Toggle" onclick="datetoggl('<%=reportId%>')"></a></td>-->
           <td width="35"><input  type="button" class="navtitle-hover" value=" Go "  style="height: 20px; width: 25px" onclick="dispTable1()"/></td>
    </tr></table></span></div></div></div></div>

           <div id="showCalyear" style="display:none" title="Period Selection" align="center">
    <table>
        <tr><td><br></td></tr>
        <tr><td>Select Year : </td> <td><select id="calyear" name="calyear" style="width:90px">
                    <option value="Select-Year"> Select-Year </option>
                    <% for(int year=2000;year<=2020;year++){ %>
                   <option value="<%=year%>"> <%=year%> </option>
                   <%}%>
              </select></td></tr><tr><td><br></td></tr><tr><td><br></td></tr>
                  <tr rowspan="2"><td colspan="2"align="center"><input id="myyear2" type="button" value="Ok" class="navtitle-hover" style="width:40px;height:25px;color:black" onclick="saveyeardash()"/>
    </td></tr></table></div>

           <div id="dateregionRange" style="display:none">
                                                    <div id="fixedtop1" >
                                                        <div id="center250b">
                                                            <div class="form clearFix">
                                                                <span class="wr100">
                                                                    <table align="center" id="roundtrip">
                                                                   <tr width="100%">
       <td  white-space:nowrap ;width: auto"></td>
           <td  align="right"  id="datetime"  tabindex="6">
        <span id="depShow">
        <span class="top w100 mrtop" id="field1"><%=container.fullName0%></span>
        <span class="date">
            <small Style="font-weight: bold" id="field2"><%=container.dated%></small></span>
        <span class="bottom w100" id="field3"><%=container.day0%></span></span></td>
    <td><input type="hidden" class="ui-datepicker" id="fromdate" name="fromdate" onchange="dateclick()" onclick="showdate()"/></td>
        <td><input  type="hidden" id="datetext" name="datetext" value=""/></td>

          <td align="left" style="white-space:nowrap ;width: auto;padding-left: 1.5em">To :</td>
         <td align="right" id="todatetime" tabindex="8"  style="padding-left: 0.5em">
             <span id="retShow" style="position:relative;">
         <span class="top w100 mrtop" id="tdfield1"><%=container.fullName%></span>
        <span class="date"><small class="init" Style="font-weight: bold" id="tdfield2"><%=container.date%></small></span>
        <span class="bottom w100" id="tdfield3"><%=container.day%></span></span></td>
        <td><input  type="hidden" class="ui-datepicker " name="todate" id="todate" onchange="dateclick()" onclick="showdate()"/> </td>
        <td><input  type="hidden" id="datetext" name="datetext" value=""/></td>
           <td align="right" style="font-weight:bold ; width: auto;padding-left: 1.5em"> COMPARE</td>
           <td align="right" style="width: auto;padding-left: 1.5em"></td>
         <td align="left" id="comparefromtime" tabindex="8"  style="padding-left: 0.5em">
             <span id="retShow" style="position:relative;">
         <span class="top w100 mrtop" id="cffield1"><%=container.cffullName%></span>
        <span class="date"><small class="init" Style="font-weight: bold" id="cffield2"><%=container.cfdate%></small></span>
        <span class="bottom w100" id="cffield3"><%=container.cfday%></span></span></td>
         <td><input type="hidden" class="ui-datepicker " id="comparefrom" name="comparefrom"  onchange="dateclick()" onclick="showdate()"/></td>
         <td><input  type="hidden" id="datetext" name="datetext" value=""/></td>
    <td align="right" style="width: auto;padding-left: 1.5em">To:</td>
    <td align="left" id="comparetoTime" tabindex="8"  style="padding-left: 0.5em"> <span  id="retShow" style="position:relative;">
         <span class="top w100 mrtop" id="ctfield1"><%=container.ctfullName%></span>
        <span class="date"><small class="init" Style="font-weight: bold" id="ctfield2"><%=container.ctdate%></small></span>
        <span class="bottom w100" id="ctfield3"><%=container.ctday%></span></span></td>
         <td><input  type="hidden" class="ui-datepicker1 " id="compareto" name="compareto" onchange="dateclick()" onclick="showdate()"/></td>
         <td><input  type="hidden" id="datetext" name="datetext" value=""/></td>
           <td id="saveTabId" ><a href="javascript:void(0)" style="text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;font-size: 9pt;" class="ui-icon ui-icon-disk" title="Global Save" onclick="saveRepotInDesigner()"></a></td>
           <td> <a onclick="openImgDiv()" href=""> Reset </a></td><td></td>
<!--           <td id="dateToggle"><a href="javascript:void(0)" class="ui-icon ui-icon-transferthick-e-w" title="Toggle" onclick="datetoggl('<%=reportId%>')"></a></td>-->
           <td width="35"><input  type="button" class="navtitle-hover" value=" Go "  style="height: 20px; width: 25px" onclick="dispTable2()"/></td>

    </tr>
        </table>
                                                                </span>
 </div>

                                                       </div>
                                                       </div>
</div>

                <table style="min-width:100%;font-family: Calibri, Calibri, Calibri, sans-serif;font-size:14pt;color: darkblue" max-height="auto" max-width="auto" >
            <tr>
                <td valign="top" style="min-width:100%;">
                    <jsp:include page="Headerfolder/headerPage.jsp"/>
                </td>
            </tr>
            <tr></tr>
        </table>
              <form name="submitReportForm" id="submitReportForm" method="post" action="">

                  <table width="100%" height="100%" valign="top">
                      <tr><td style="width:1%" nowrap>
                          <span id="reportName"  style="color: #4F4F4F;font-family:verdana;font-size:14px;font-weight:bold;text-decoration:none"   title="<%=reportName%>"><%=reportName%></span>&nbsp;<a href="javascript:void(0)"  style="text-decoration:underline" </a>
                          </td></tr>
                      </table>
                          <table>
                      <tr>
<!--                          <td valign="top" style="width:3%;">
                              <a href="javascript:void(0)" class="ui-icon ui-icon-clock" onclick="TimeSelect(document.getElementById('selectTime'))"></a>
                          </td>-->
                          <td valign="top" style="width:3%;position: relative">
                             <a href="javascript:void(0)" class="ui-icon ui-icon-plusthick" onclick="AddMoreDims(<%=IsrepAdhoc%>)"></a>
                          </td>
                              <td  id="" valign="top" style="width:1%;position: relative;display: ''">
                             <a href="javascript:void(0)" class="ui-icon ui-icon-disk" onclick="saveRepotInDesigner()"></a>
                          </td>
                          <td valign="top" style="width:83%;" ></td>
<!--                          <td id="AddGraphIcon" valign="top" align="center" style="width:10%;" valign="top">
                             <a href="javascript:void(0)" class="ui-icon ui-icon-circle-plus" onclick="addGraphInDesigner('<%=request.getContextPath()%>','<%=ReportId%>')"></a>
                          </td>-->
                           </tr>
                  </table>
                  <table width="99%" height="100%" valign="top" >
                      <tr>
                          <td width="1%" title="Click Here To Hide Left Pane" style="background-color:#e6e6e6;border:0px;cursor:pointer" class="ui-corner-all"  >
                                <div><img  alt="" id="tdImage" src="<%=request.getContextPath()%>/icons pinvoke/<%=String.valueOf(session.getAttribute("imageType"))%>.png"/></div>
                            </td>

                          <td>
                  <table width="99%" height="100%" valign="top" style=" border-width: 4px; border-style: groove; border-color: skyblue;  ">
                      <tr >
        <td id="GraphsRegOfReport" style="display:none;" align="left" width="100%" valign="top">

            <table style="height:100%" class="draggedTable" border="0" >
                <tr >
                    <td id="graphlist" style="display: none" align="right">
                        <table>
                            <td align="left">
                    <a  href="javascript:void(0)" class="ui-icon ui-icon-image" title="Type" onclick="graphTypesDisp(document.getElementById('dispgrptypes12'),'jq');" style='text-decoration:none'></a>
                    <div class="flagDiv1" style="display:none;position:absolute;"  id="dispgrptypes12">
                              </div>
                            <td>
                    </table>
                    </td>
                    <td>

                        <div id="tabGraphs1" style="background-color:#fff;display:none;border-width: 1px; border-style: groove; border-color: black">
                            <table  width="100%" style="height:auto;border:0px solid #369;">
                                <tr style="width:100%">
                                    <td valign="top" width="100%">
                                        <IFRAME NAME='iframe5' ID='iframe5'  SRC='about:blank' STYLE='width:100%;height:500px' frameborder="0"></IFRAME>
                                    </td>
                                </tr>
                            </table>
                        </div>
                    </td>
                </tr>

            </table>
        </td>
    </tr>
                      <tr>
        <td align="left" width="100%" height="180px" valign="top">
            <table style="height:100%" class="draggedTable" border="0" >

                <tr id="editTable">
<!--                    <td width="1%" title="Click Here To Hide Left Pane" style="background-color:#e6e6e6;border:0px;cursor:pointer" class="ui-corner-all"  >
                                <div><img  alt="" id="tdImage" src="<%=request.getContextPath()%>/icons pinvoke/<%=String.valueOf(session.getAttribute("imageType"))%>.png"/></div>
                            </td>-->
                    <td style="height:250px" valign="top">

                            <div align="center" id="desginerDiv" width="100%" max-height="auto" style="overflow: auto;  height: 500px; border-width: 1px; border-style: groove; border-color: grey;  ">

                                <div id="tableDiv">
                            <Table align="left">
                                <tr><td>
<!--                                        <table align="left"><tr><td><a href="javascript:void(0)" style="align:left;" title="Click to Select Measures" onclick="showMeasuresInDesigner()">Measures</a></td></tr></table>-->
                                    </td></tr>
                            </Table>
                                </div>
                        </div>

                    </td>
                </tr>
                <tr>

                </tr>

                <tr id="previewTable" style="display:none;">
                    <td  style="height:250px" valign="top" >


                        <iframe id="iframe1" style="width:100%;height:500px;overflow:auto"></iframe>
                    </td>
                </tr>

            </table>
        </td>
    </tr>
                      <div class="flagDiv" style="display: none" id="selectTime">
                          <input type="radio" id="RangeBasis" onclick="timeBasis()" name="time" value="RangeBasis"> Range Basis
                          <br> <input type="radio" id="StandardTime" onclick="timeBasis()" name="time" value="StandardTime">Standard Time
                  </div>
                  </table>
                  </td>
                  </tr>
                  </table>
<!--                         <div class="flagDiv" id="paramDesign" style="display:none;width:15%;height:100%;background-color:white; direction: ltr; float: right; position:absolute;text-align:left;border: 1px solid LightGrey;left:0px;top:112px;z-index: 10000;">
                      <%--<%=params %>--%>

                  </div>-->
                                              <div id="previewDispGraph" style="width:100%;height:auto;max-height:100%">
                            <%=GraphRegion%>
                        </div>

                         <div id="measuresDialog" style="display:none" title="Add Measures">
    <table><tr><td>
            <input class="classtableList" id="tableList" type="checkbox"  unchecked=""  onclick="getDisplayTablesInDesigner('<%=request.getContextPath() %>','','<%=ReportId%>')">All</td>
                    <td id="tabListDiv" ><input type="textbox" id="tabsListVals"><input type="textbox" style="display:none;" id="tabsListIds">
                        <div id="paramVals" class="ajaxboxstyle" style="display:none;overflow: auto;"></div></td>
                    <td id="tablistLink" ><a href="javascript:void(0)" class="ui-icon ui-icon-note" onclick="showListInDesigner('<%=request.getContextPath() %>','')" ></a></td>
                    <td id="goButton" onclick="setValueToContainerInDesigner('<%=request.getContextPath() %>','<%=ReportId%>')"><input type="button" value="GO" class="navtitle-hover"></td>
            </tr></table>
    <iframe id="dataDispmem" NAME='dataDispmem' frameborder="0" width="100%" height="100%" SRC='#'></iframe>

</div>



                         <div id="AddMoreParamsDiv" title="Add More Dimension ">
                         <table><tr> <td><a class="ui-icon ui-icon-clock" title="Time"></a></td>
                          <td>
                        <select style="width:120px" id="time" name="time">
<!--                            //added by Mohit for Custom setting-->
<!--                         <option id="st" onclick="timeBasis()">none</option>-->
                         <option id="StandardTime" value="StandardTime" onclick="timeBasis()" name="time">Standard Time</option>
<!--                         <option id="RangeBasis" value="RangeBasis" onclick="timeBasis()" name="time">Range Basis</option>-->
                        </select>
                          </td>
                          <td><a class="ui-icon ui-icon-copy" title="Report Layout"></a></td>
                          <td>
                        <select id ="ReportLayout" style="width:120px" id="time" name="time">
<!--                            //added by Mohit for Custom setting-->
<!--                         <option id="st" onclick="timeBasis()">none</option>-->
                         <option id="ViewBy" value="ViewBy"  name="ViewBy">ViewBy</option>
                          <option id="KPI" value="KPI"  name="KPI">KPI</option>
                           <option id="None" value="None"  name="None">None</option>
                        </select>
                          </td>
<!--//added by Mohit for Kpi and none viewby-->
                             </tr></table>
<!--                         <td> <div id="adhocre" style="display:none;">
                      <input type="radio" id="StandardTime"  onclick="timeBasis()" name="time" value="StandardTime">Standard Time
                      <input type="radio" id="RangeBasis" onclick="timeBasis()" name="time" value="RangeBasis"> Range Basis-->
           <iframe  id="addmoreParamFrame" name='addMoreParamFrame' width="100%" height="100%" frameborder="0"   src='about:blank'></iframe>
        </div>
                  <div id="editViewByDiv" title="Edit ViewBy" style="display:none">
            <iframe  id="editViewByFrame" name='editViewFrame' width="100%" height="100%" frameborder="0"   src='about:blank'></iframe>
        </div>

                         <div id="designerGraphList" style="display:none;"></div>
                      <table style="width:100%">
            <tr>
                <td valign="top" style="width:100%;">
                    <jsp:include page="Headerfolder/footerPage.jsp"/>
                </td>
            </tr>
        </table>
                <div id="changeNameDialog" title="Edit Report Name" style="display:none">
    <table width="100%" align="center">
        <tr>
            <td>Report Name :</td>
            <td><input type="text" name="repName2" style="font:11px verdana;background-color:white" id="repName2"></td>
        </tr>
        <tr>
            <td>&nbsp;</td>
            <td><span  style="font-family:verdana;color:red;font-size:11px;background-color:white" id="repMsg"></span></td>
        </tr>
        <tr>
            <td>Report Description :</td>
            <td><input type="text" name="repDesc2" style="font:11px verdana;background-color:white" id="repDesc2"></td>
        </tr>
        <tr>
            <td colspan="2" style="width:10px">&nbsp;</td>
        </tr>
        <tr>
            <td colspan="2" align="center"><input type="button" value="Done" class="navtitle-hover" onclick="changeReportNameTempInDesgner();"></td>
        </tr>
    </table>
</div>

                       <input type="hidden" id="roleid" name="" value="<%=roleid%>">
                          <input type="hidden" name="REPORTID" id="REPORTID" value="<%=ReportId%>">
                <input type="hidden" name="REPORTNAMEbeforeSave" id="REPORTNAMEbeforeSave"  value="<%=reportName%>">
<input type="hidden" name="allGraphIds" value="" id="allGraphIds">
<input type="hidden" name="allGraphTableIds" value="" id="allGraphTableIds">
<input type="hidden" name="graphTableHidden" value="" id="graphTableHidden">
<input type="hidden" name="REPIds" value="<%=prevREP%>" id="REPIds">
<input type="hidden" name="CEPIds" value="<%=prevCEP%>" id="CEPIds">
<input type="hidden" name="AOFlag" value="false" id="AOFlag">
<input type="hidden" name="MsrIds" value="<%=prevMeasures%>" id="MsrIds">
<input type="hidden" name="Measures" value="<%=prevMeasureNames%>" id="Measures">
<input type="hidden" name="repExc" value="<%=prevRepExclude%>" id="repExc">
<input type="hidden" name="cepExc" value="<%=prevCepExclude%>" id="cepExc">
 <input type="hidden" name="oldMsrName" id="oldMsrName" value="">
  <input type="hidden" name="colName" id="colName" value="">
    <input type="hidden" name="ctxPath" id="ctxPath" value="">
   <input type="hidden" id="Designer" name="Designer" value="fromDesigner">
   <Input TYPE="hidden" name="reportNameH" id="reportNameH" value="<%=reportName%>">
   <Input TYPE="hidden" name="reportDescH" id="reportDescH" value="<%=reportDesc%>">
 <input type="hidden" name="AOId" id="AOId">
<input type="hidden" id="h" value="<%=request.getContextPath()%>">
              </form>
              <span id="reptimeDetails">
        </span>
              <div id="DefinecustSeqDialog" title="Define Custom Sequence" style="display:none">
             <form id="custseqForm">
              <table  align="center" >
           <tr>
            <td id="DefinecustSeqtabid"></td>
           </tr>
          </table>

             </form>
        </div>


        <%-- added  satrt customReportDrill--%>
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
        <%--added by bharathi reddy for grp analysis--%>
        <div id="dispgrpAnalysis" title="Group by Analysis" style="display:none">
            <iframe id="dispgrpAnalysisFrame" NAME='dispgrpAnalysisFrame' width="100%" height="100%"  frameborder="0" SRC='about:blank'></iframe>
        </div>

        <%--added by bharathi reddy for grp analysis--%>
        <div id="dispbucketAnalysis" title="Bucket Analysis" style="display:none">
            <iframe id="dispbucketAnalysisFrame" NAME='dispbucketAnalysisFrame' width="100%" height="100%"  frameborder="0" SRC='about:blank'></iframe>
        </div>

        <div style="display:none" id="custmemDispDia" title="Custom Measures">
            <iframe  id="custmemDisp" NAME='custmemDisp' height="100%" width="100%" frameborder="0" SRC='createCustMemberinviewer.jsp'></iframe>
        </div>
        <div id="advanceFormulaDiv" title="Advance Formula" style="width:515px;height:200px;overflow-y:auto;overflow-x:auto">
            <iframe id="advanceFormulaFrame" NAME='advanceFormulaFrame' width="100%" height="100%" frameborder="0" SRC='about:blank'></iframe>
 </div>
        <div style="display:none" id="custmemMeasureDispDia" title="Custom Measures">
            <iframe  id="custmemMeasDisp"  height="100%" width="100%" frameborder="0" src='about:blank'></iframe>
        </div>
            <div id="TimeBasedDiaolgDisplay" name="TimeBasedDiaolgDisplay" title="" style="z-index: 1050">
        <iframe id="TimeBasedDisplay" name="TimeBasedDisplay" height="100%" width="100%" frameborder="0" src="about:blank"> </iframe>
        </div>
        <%-- added for Show sql query--%>
        <div id="showSqlStrDialog" title="SQL Query" style="display:none">
            <iframe  id="sqlQueryStr" NAME='sqlQueryStr' width="100%" height="100%" frameborder="0"   SRC='about:blank'></iframe>
        </div>

        <div id="performWhatIfDiv" style="display: none" title="Perform What If">
            <iframe  frameborder="0" id="performWhatIfFrame" name="performWhatIfFrame" style="width:750px; height:490px; "  src='about:blank'></iframe>

        </div>
        <div id="correlationId" style="display: none; font-size: 14px" title="Correlation Coefficient">


        </div>
          <div  id="columnPropertiesdiv" title="Column Properties" style="display:none">
            <iframe id="columnPropertiesframe" name="columnPropertiesframe" frameborder="0" marginheight="0" marginwidth="0" src='about:blank' width="100%" height="100%"></iframe>
        </div>
        <div id="shwparamAssis" title="Parameter Assistance Filter" style="display:none">
            <div id="shwparamAssisDisplay" style="height:180px;width:auto;overflow:auto">
            </div>
        </div>
            <div id="showExports" title="Exports" style="display:none">
            <iframe id="showExportsFrame" NAME='showExportsFrame' width="100%" height="100%"  frameborder="0" SRC='about:blank'></iframe>
        </div>
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
         <div id="QuickTimeBasedFormulaDiv" title="Quick TimeBased Formula" style="width:515px;height:300px;overflow-y:auto;overflow-x:auto">
            <iframe id="QuickTimeBasedFormulaFrame" NAME='QuickTimeBasedFormulaFrame' width="100%" height="100%" frameborder="0" SRC='about:blank'></iframe>

         </div>

        <div id="conversionFormula" title="Conversion Formula" style="display:none">
            <table align="center">
                <tr><td>New Measure Name</td><td><input type="text" name="newMsr" id="newMsr"></td></tr>
                <tr><td>Old Measure</td><td><input type="text" name="oldMsr" id="oldMsr" readonly></td></tr>
                <tr><td>Divide by</td><td><input type="text" name="divideby" id="divideby" onkeypress="javascript:return isNumberKey(event)" onblur="isZeroKey(this.id)"></td></tr>
                <tr><td>&nbsp;</td></tr>
                <tr align="center"><td colspan="2" align="center"><input type="submit" name="saveConversion" value="save" class="navtitle-hover" onclick="saveConversion('<%=request.getContextPath()%>')"></td></tr>
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
<!--            <iframe id="mapColorGrouping" name="mapColorGrouping" onclick="mainMeasures()" width="100%" height="100%" frameborder="0"   SRC='about:blank'></iframe>-->
        </div>

        <div id="showStickyNote" style="display:none;z-index:2000;position:absolute;width:150px;left:20px;top:300px">
        </div>
        <SPAN id="StickNoteSpan">

        </SPAN>
                <span id="reptimeDetails">

        </span>
        <div id="MapMeasures" style="display: none" title="Map Measures">
<!--            <iframe  frameborder="0" id="mapMeasureFrame" width="100%" height="100%" name="mapMeasureFrame" src="#"></iframe>-->

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
                                                <input type="button" class="navtitle-hover" value="Ok" onclick="changeEditSensitivity('<%=request.getContextPath() %>','<%=PbReportId%>')" style="width:100px" >
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
                                            <td colspan="1"><input type="text" id="comments" name="comments" style="display:none"></td>
                                        </tr>
                                        <tr>
                                                 <td width="20%" id="tomailtd" style="display:none"><b>Email To</b> </td>
                                                <td colspan="1" >

                                                    <textarea  id="userstextarea" name="userstextarea" cols="" rows=""  style="width:250px;height:80px;display:none"></textarea>
                                                </td>
                                        </tr>
                                        <tr> <td colspan="2">&nbsp; </td>  </tr>
                                         <tr>
                                                <td colspan="2" align="center">
                                                    <input class="navtitle-hover" type="button" id="share_report_button" value="Share Report" onclick="SendReportEmail()">
                                                  </td>
                                        </tr>
                                        <tr> <td colspan="2">&nbsp; </td>  </tr>
                                        <tr> <td colspan="2"> Please separate multiple Email Id's by comma(,). </td>  </tr>
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
<!--                 <tr><td><font size="2" style="font-weight: bold;">Enter Goal: </font><input type="text" id="goailId" align="center" value='' class="" onkeypress='return isNumberKey(event)'> </td></tr>-->
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

            <div id="editMsrName" style="display:none">
                <center>
                    <br>
                    <table style="width:100%" >
                        <tr>
                            <td valign="top" style="width:40%">Old Measure Name</td>
                            <td valign="top" style="width:60%">
                                <input type="text" name="oldMsrName" readonly style="width:150px" id="oldMsrName">
                            </td>
                        </tr>
                        <tr>
                            <td  valign="top" style="width:30%">New Measure Name</td>
                            <td valign="top" style="width:70%">
                                <input type="text" name="newMsrName" id="newMsrName"  style="width:150px" >
                            </td>
                        </tr>
                    </table>
                    <table>
                        <tr>
                            <td id="btnCol"></td>
                            <td><input type="button" class="navtitle-hover" style="width:auto" value="Update" onclick="updateMsrName('<%=PbReportId%>')"></td>
                        </tr>
                    </table>
                </center>
            </div>
                        <div id="createBucketdiv" style="display: none" title="Add Segment">
    <iframe  id="bucketDisp1" NAME='bucketDisp1'  frameborder="0"   height="100%" width="100%"  SRC=''></iframe>
</div>
           <div id="tableColsDialog" title="Add Measures" style="display:none" >
 <table><tr><td>

            <input id="tableList1" type="checkbox" onclick="getDisplayTables('<%=request.getContextPath() %>','<%=params%>')">All</td>
                    <td id="tabListDiv1" ><input type="textbox" id="tabsListVals1"><input type="textbox" style="display:none;" id="tabsListIds1">
                        <div id="paramVals1" class="ajaxboxstyle" style="display:none;overflow: auto;"></div></td>
                    <td id="tablistLink1" ><a href="javascript:void(0)" class="ui-icon ui-icon-note" onclick="showList('<%=request.getContextPath() %>','<%=params%>')" ></a></td>
                    <td id="goButton1" onclick="setValueToContainer('<%=request.getContextPath() %>','<%= PbReportId%>','<%=rolesid%>')"><input type="button" value="GO" class="navtitle-hover"></td>
            </tr></table>
            <iframe  id="tableColsFrame" name='tableColsFrame' width="100%" height="100%" frameborder="0"   src='TableDisplay/PbChangeTableColumnsRT.jsp'></iframe>
        </div>
<script type="text/javascript">

                $(document).ready(function() {
                    $("#changeNameDialog").dialog({
            //bgiframe: true,
            autoOpen: false,
            height: 100,
            width: 300,
            position: 'justify',
            modal: true,
            resizable:false
        });
        $("#AddMoreParamsDiv").dialog({
            autoOpen: false,
            height: 350,
            width: 450,
            position: 'justify',
            modal: true,
            resizable:true
        });
        $("#QuickTimeBasedFormulaDiv").dialog({
            //bgiframe: true,
            autoOpen: false,
            height: 550,
            width: 750,
            position: 'top',
            modal: true

        });
        $("#advanceFormulaDiv").dialog({
            //bgiframe: true,
            autoOpen: false,
            height: 480,
            width: 545,
            modal: true

        });
                        $("#createBucketdiv").dialog({
                        //bgiframe: true,
                        autoOpen: false,
                        height: 500,
                        width: 610,
                        position: 'top',
                        modal: true
                    });
                 });
                 var repExist='N';
                 var fromReport="fasle";
                 function initialog(){
                if ($.browser.msie == true){
                    $("#changeNameDialog").dialog({
                        //bgiframe: true,
                        autoOpen: false,
                        height: 100,
                        width: 300,
                        position: 'justify',
                        modal: true,
                        resizable:false
                    });
                }
                else{
                    $("#changeNameDialog").dialog({
                        //bgiframe: true,
                        autoOpen: false,
                        height: 100,
                        width: 300,
                        position: 'justify',
                        modal: true,
                        resizable:false
                    });
                }
            }
              function TimeSelect(dispgrptypObj){
    if(dispgrptypObj.style.display=='none'){
        dispgrptypObj.style.display='';
    }
    else{
        dispgrptypObj.style.display='none';
    }
              }

               if($.browser.msie==true){
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
                    }

                            function designerParam(dispgrptypObj){
                           if(dispgrptypObj.style.display=='none'){
        dispgrptypObj.style.display='';
    }
    else{
        dispgrptypObj.style.display='none';
    }
              }
              function saveRepotInDesigner(){
                  var repId=document.getElementById("REPORTID").value;
                var repName=document.getElementById("REPORTNAMEbeforeSave").value;
                // repName=repName.replace('&','^').replace('+','~').replace('#','`').replace('%','_');
                repName=encodeURIComponent(repName);
                var folders=buildFldIds();
                $.ajax({
                    url: 'reportTemplateAction.do?templateParam=checkReportNameatRoleLevel&repName='+repName+"&REPORTID="+repId+"&folderIds="+document.getElementById("roleid").value+"&fromRepDesigner=fromrepDesigner&action=open",
                    success: function(data){

                        if(data==1){
                            createReport();
                        }
                        else{
                            alert('Report Name Already Exists,Enter New Reportname')
                            initialog();
                            repExist='Y';
                            $("#changeNameDialog").dialog('open');
                        }
                    }
                });
              }
               function createReport(){
                       var REPORTID=document.getElementById('REPORTID').value;
                        var graphTableHidden = document.getElementById("graphTableHidden").value
//                        alert(REPORTID)
                        $.ajax({
                            url: 'reportTemplateAction.do?templateParam=checkRportNGraph&REPORTID='+REPORTID,
                            success: function(data){
//                                alert(data)
                                if(data=='Please Select Table'){
//                                    alert(data)
                                    return false;
                                }else if(data=='Please Select Graph'){
//                                    alert(data)
                                    return false;
                                }else if(data=='Please Select Graph Columns To Add Graph'){
//                                    alert(data)
                                }else if(data=='Please Select Table and Graph'){
//                                    alert(data);
                                    return false;
                                    //alert("else every thing satisfied");
                                    /* document.myForm2.action="reportTemplateAction.do?templateParam=saveReport";
                            document.myForm2.submit();*/
                                }else if(data=='3'){
                                    alert("Please Select Table and Graph");
                                    return false;
                                }else if(data=='4'){
                                    alert("please Select table");
                                    return false;
                                }else if(data=='5'){
                                    alert("please select measures");
                                    return false;
                                }else{
                                    //start of code by Nazneen for caching AO
                                    var cacheAo = "false";
                                     var isCacheAo = confirm("Do you want to create Analytical Object for this report (Cache Report)?");
                                     if (isCacheAo == true){
                                         cacheAo = "true";
                                     }
                                     //end of code by Nazneen for caching AO
//                                    alert("helllllllllo")
//                                    $.post("reportTemplateAction.do?templateParam=saveReport&graphTableHidden="+graphTableHidden+"&REPORTID="+REPORTID,$("#submitReportForm").serialize(),
//                                        function(data){{
                                          document.forms.submitReportForm.action = "reportTemplateAction.do?templateParam=saveReport&action=open&graphTableHidden="+graphTableHidden+"&cacheAo="+cacheAo;
//                                               document.forms.submitReportForm.method="POST";
                                           document.forms.submitReportForm.submit();
//                                        }


//                                    });
                                }
                            }
                        });

                    }
              function changeReportNameTempInDesgner(){
                var repId=document.getElementById("REPORTID").value;
                var  newRepName=document.getElementById("repName2").value;
                // newRepName=newRepName.replace('&','^').replace('+','~').replace('#','`').replace('%','_');
                var  newRepDesc= document.getElementById("repDesc2").value;

                // newRepDesc=newRepDesc.replace('&','^').replace('+','~').replace('#','`').replace('%','_');
                var encodednewRepDesc=encodeURIComponent(newRepDesc);
                var encodednewRepName=encodeURIComponent(newRepName);
                $.ajax({
                    url: "reportTemplateAction.do?templateParam=checkReportNameBeforeUpdate&newRepName="+encodednewRepName+"&REPORTID="+repId+'&newRepDesc='+encodednewRepDesc,
                    success: function(data){
                        if(data==1){
                            document.getElementById("reportNameH").value=newRepName;
                            document.getElementById("reportDescH").value=newRepDesc;
                            document.getElementById('repMsg').innerHTML ='';
                            closeChangeName(newRepName,newRepDesc,repExist);
                        }
                        else{
                            document.getElementById('repMsg').innerHTML = "Report Name already exists";
                        }
                    }
                });
            }
            function closeChangeName(newRepName,newRepDesc,repExist){
                var repId=document.getElementById("REPORTID").value;
                var encodednewRepDesc=encodeURIComponent(newRepDesc);
                var encodednewRepName=encodeURIComponent(newRepName);
                $.ajax({
                    url: "reportTemplateAction.do?templateParam=changeReportName&reportName="+encodednewRepName+"&reportID="+repId+'&newRepDesc='+encodednewRepDesc,
                    success: function(data){
                    }
                });
                // newRepName=newRepName.replace('^', '&').replace('~','+').replace('`', '#').replace('_','%');

                document.getElementById("reportName").innerHTML=newRepName;
                $("#changeNameDialog").dialog('close');
                if(repExist=='Y'){
                    createReport();
                }
            }
            function goPaths(path){
                parent.closeStart();
                document.forms.submitReportForm.action=path;
                document.forms.submitReportForm.submit();
            }      function getRoleIdForDesigner(){
    var foldersIds=buildFldIds();
    var branches="";
    var str;
 $.ajax({
        url: 'reportTemplateAction.do?templateParam=getRoleIdforDesigner&foldersIds='+foldersIds+'&REPORTID='+document.getElementById("REPORTID").value,
        success: function(data) {

            }

            });


       }
       function getDisplayTablesInDesigner(ctxpath,paramslist,repId){
       var repId=<%=ReportId%>

var frameObj=document.getElementById("dataDispmem");
//        getdimmap.getFact(Parameters);
        var check = $("#tableList").is(":checked")
        if($("#tableList").is(":checked")){
            $("#tabListDiv").hide();
            $("#tablistLink").hide();
            $("#goButton").hide();
            $("#tabsListIds").val("");
            $("#tabsListVals").val("");
                var source="reportTemplateAction.do?templateParam=getMeasures&foldersIds="+document.getElementById("roleid").value+'&REPORTID='+document.getElementById("REPORTID").value+'&tableList=true';
        //frameObj.src='<center><img id="imgId" src="images/ajax.gif" align="middle"  width="75px" height="75px"  style="position:absolute" ></center>';
        frameObj.src=source;
            //$("#tableList").attr('checked',true);
        }else{
            $("#tabListDiv").show();
            $("#tablistLink").show();
            $("#goButton").show();
            var htmlVar = "<table>";
           $.post(ctxpath+"/reportViewer.do?reportBy=getTableList&paramslist="+paramslist+'&currentBizRoles='+document.getElementById("roleid").value,
                function(data){
                    var jsonVar=eval('('+data+')')
                    var json1 = jsonVar.idsList.split(",");
                    var jsonname = jsonVar.namesList.split(",");
                    for(var i=0;i<json1.length;i++){
                        //alert(jsonname[i])
                        if(json1[i].replace(" ", "", "gi") != "0")
                        htmlVar += "<tr><td id='"+json1[i]+"' onclick=\"selectTablesInDesigner('"+json1[i]+"','"+jsonname[i]+"')\">"+jsonname[i]+"</td></tr>";
                    }
                    htmlVar += "</table>";
                    $("#paramVals").html(htmlVar);
                    var source="reportTemplateAction.do?templateParam=getMeasures&foldersIds="+document.getElementById("roleid").value+'&REPORTID='+document.getElementById("REPORTID").value;
        //frameObj.src='<center><img id="imgId" src="images/ajax.gif" align="middle"  width="75px" height="75px"  style="position:absolute" ></center>';
        frameObj.src=source;
                });

        }
    }
    function showListInDesigner(ctxpath,paramslist){
        if(document.getElementById("paramVals").style.display=='none'){
            $("#paramVals").show();
            $("#tabListDiv").show();
            $("#tablistLink").show();
            $("#goButton").show();
            var htmlVar = "<table>";
           $.post(ctxpath+"/reportViewer.do?reportBy=getTableList&paramslist="+paramslist+'&currentBizRoles='+document.getElementById("roleid").value,
                function(data){
                    var jsonVar=eval('('+data+')')
                    var json1 = jsonVar.idsList.split(",");
                    var jsonname = jsonVar.namesList.split(",");
                    for(var i=0;i<json1.length;i++){
                        //alert(jsonname[i])
                        if(json1[i].replace(" ", "", "gi") != "0")
                        htmlVar += "<tr><td id='"+json1[i]+"' onclick=\"selectTablesInDesigner('"+json1[i]+"','"+jsonname[i]+"')\">"+jsonname[i]+"</td></tr>";
                    }
                    htmlVar += "</table>";
                    $("#paramVals").html(htmlVar);

                });
        }else{
           $("#paramVals").hide();
        }

    }
    function selectTablesInDesigner(tdId,tname){
        //alert(tname)
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
    function setValueToContainerInDesigner(ctxpath,repId,bizRoles){
       var frameObj=document.getElementById("dataDispmem");
       $("#paramVals").hide();
       var tabLst = $("#tabsListIds").val();
       $("#tabsListVals").val('')
       $("#tabsListIds").val('')
       $("#tabListDiv").hide();
           $("#tablistLink").hide();
           $("#goButton").hide();
           $("#tabsListIds").val("");
           $("#tabsListVals").val("");
            $.post(ctxpath+"/reportViewer.do?reportBy=setTableListToContainer&repId="+repId+'&tabLst='+tabLst,
                function(data){
                    var source="reportTemplateAction.do?templateParam=getMeasures&foldersIds="+document.getElementById("roleid").value+'&REPORTID='+document.getElementById("REPORTID").value+'&tableList=true';
                    frameObj.src=source;
                });
   }
    function cancelDim(){
               $("#AddMoreParamsDiv").dialog('close');
            }
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
                        }
                    }
                }
            }
            function paramDisp(modifyColumnsObj){
                if(modifyColumnsObj.style.display=='none'){
                    modifyColumnsObj.style.display='';
                }
                else{
                    modifyColumnsObj.style.display='none';
                }
            }
            function addParamList(addParamsListObj,addParamsObj,reportId,path,tableId){
                if(addParamsListObj.style.display=='none'){
                    addParamsListObj.style.display='';
                }
                else{
                    addParamsListObj.style.display='none';
                    addParams(addParamsObj,reportId,path,tableId);
                }
            }
            function addParams(addParamsObj,reportId,path){
                var paramIds='';
                for(var i=0;i<addParamsObj.length;i++){
                    if( addParamsObj[i].checked){
                        paramIds=paramIds+','+addParamsObj[i].value;
                    }
                }
                if(paramIds!=''){
                    document.frmParameter.action='pbAddParams.jsp?reportId='+reportId+'&paramIds='+paramIds+'&path='+path;
                    document.frmParameter.submit();
                }
            }
            function getMessage(id,from,sub,message){
                var  src = "pbTakeMailAddress.jsp?from="+from+"&subject="+sub+"&message="+message+"&sample=sample";
                src=src.replace(" ","~","gi");
                var frameObj = document.getElementById("replyMessageFrame");
                frameObj.src = src;
                $('#replyMessageDialog').data('title.dialog','Reply Messages');
                $('#replyMessageDialog').dialog('open');
            }
            function cancelMessage(){
                $('#replyMessageDialog').dialog('close');
            }
            function cancelSnap(){
                $('#replyMessageDialog').dialog('close');
            }
            function cancelMessage1(){
                $('#composeMessageDialog').dialog('close');
                submitform();
            }
            function savecancelGrp(){
                $("#dispgrpAnalysis").dialog('close');
                //submitform();
            }
            function cancelFavLinks(){
                $('#favLinksDialog').dialog('close');
                setTimeout('reloadTimerfavlinks()',1000);
                // document.getElementById('favFrame').src = document.getElementById('favFrame').src;
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
            function dispGraphs(){
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
//                    document.getElementById("tabGraphs1").style.display = "none";
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
                                    //  frm1.style.display='none';
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
                            //                            else
                            //                                alert("Map is not applicable for this view.")
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
                // var sortValue = $("#sortValuesForMap option:selected").text();
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
                        //                            else
                        //                                alert("Map is not applicable for this view.")
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
               // alert("1")
                 if(document.getElementById("editActionsTab").style.display=="none"){
                     // alert("2")
                  document.getElementById("editActionsTab").style.display=""
                }else{
                  document.getElementById("editActionsTab").style.display="none"
                }
            }

             function displayactionlink(){
               // alert("1")
                 if(document.getElementById("ActionTab").style.display=="none"){
                     // alert("2")
                  document.getElementById("ActionTab").style.display=""
                }else{
                  document.getElementById("ActionTab").style.display="none"
                }
            }

             function displaynewactionlink(){

                 if(document.getElementById("actionidnew").style.display=="none"){
                    //  alert("2")
                  document.getElementById("actionidnew").style.display=""
                }else{
                 //   alert("3")
                  document.getElementById("actionidnew").style.display="none"
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
               // if($("#reportComparisionDiv").show()==true)
//               var attrval=$("#reportComparisionDiv").attr("style").valueOf();
//                    alert(attrval);
//               if(document.getElementById("compareReports").style.display=="none"){
//                    document.getElementById("snapshots").style.display = "block";
//                    var frameObj = document.getElementById("widgetframe");
//                    var source = 'divPersistent.jsp?method=forCompareReports&block=yes';
//                    frameObj.src = source;
//                }else{
//                    document.getElementById("snapshots").style.display = "none";
//                    var frameObj = document.getElementById("widgetframe");
//                    var source = 'divPersistent.jsp?method=forCompareReports&block=no';
//                    frameObj.src = source;
//                }

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
//                    var frameObj = document.getElementById("widgetframe");
//                    var source = 'divPersistent.jsp?method=forManageSticky&block=yes';
//                    frameObj.src = source;
                }else{
                    document.getElementById("scheds").style.display = "none";
//                    var frameObj = document.getElementById("widgetframe");
//                    var source = 'divPersistent.jsp?method=forManageSticky&block=no';
//                    frameObj.src = source;
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
                var bizRole="<%= bizRole %>";
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

            //for tracker
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
            //ends
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
            //end of sai code
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
            //added  start
            function displayreportCustom()
            {

                var frameObj = document.getElementById("customReportDrillFrame");
                var reportId=document.forms.frmParameter.REPORTID.value;
                var folderId='<%=rolesid%>'
                frameObj.src= "<%=request.getContextPath()%>/PbEditReportDrill.jsp?reportId="+reportId+"&folderId="+folderId;
                $("#customReportDrill").dialog('open');

            }
            //added  over
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
            //added by santhosh.k on 01-02-2010 for column properties
            function oldcolumnProperties(columnName,disColumnName,obj){
                if(obj!='null'){
                    //   obj.style.display='none';
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
                        $("#custmemDispDia").dialog('open');
                    }
                });
                if($.browser.msie == true)
                {
                    document.getElementById("custmemDisp").src="createCustMemberinviewer.jsp";
                }
            }


            function openImgDiv(resetPath){
//                var confirmText=confirm("Report comes to default view.\nDo you want to proceed?");
//                if(confirmText==true)
//                    {
                        parent.document.getElementById('loading').style.display='';
//                        document.forms.frmParameter.action=resetPath;
//                        document.forms.frmParameter.submit();
//                    }

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
//                                   $.post(
//                    'reportViewer.do?reportBy=getLeftTdStatus&repId='+<%=PbReportId%>+'&tdtype=lefttd',
//                         function(data){
//                              if(data=="no"){
//                                           document.getElementById("leftTd").style.display = "";
//                                           var frameObj = document.getElementById("widgetframe");
//                                           var source = 'divPersistent.jsp?method=forleftTd&block=no';
//                                           frameObj.src = source;
//                                           ifrmesizedynamic();
//                                           document.getElementById("tdImage").src = "<%=request.getContextPath()%>/icons pinvoke/control-180.png";
//                              }else{
                                  document.getElementById("leftTd").style.display = "none";
                                  var frameObj = document.getElementById("widgetframe");
                                  var source = 'divPersistent.jsp?method=forleftTd&block=yes';
                                  frameObj.src = source;
                                  ifrmesizedynamicResize();
                                  document.getElementById("tdImage").src = "<%=request.getContextPath()%>/icons pinvoke/control.png";
//                              }
//
//                           });
//                           $.post(
//                    'reportViewer.do?reportBy=getLeftTdStatus&repId='+<%=PbReportId%>+'&tdtype=paramtd',
//                         function(data){
//                              if(data=="no"){
                                  document.getElementById("tabParameters").style.display = "none";
                                  var frameObj = document.getElementById("widgetframe");
                                  var source = 'divPersistent.jsp?method=forParameters&block=no';
                                  frameObj.src = source;

//                                    document.getElementById("tabGraphs").style.display = "none";
//                                    var source='<%=request.getContextPath() + "/TableDisplay/pbGraphDisplayRegion.jsp"%>'+"?tabId="+REPORTID.value;
//                                    var gSrc = document.getElementById("iframe4");
//                                    gSrc.src = source;
//                                    var frameObj = document.getElementById("widgetframe");
//                                    var source = 'divPersistent.jsp?method=forGraph&block=yes';
//                                    frameObj.src = source;
//                              }else{
//                                  document.getElementById("tabParameters").style.display = "block";
//                                  var frameObj = document.getElementById("widgetframe");
//                                  var source = 'divPersistent.jsp?method=forParameters&block=yes';
//                                  frameObj.src = source;
//
//                              }
//
//                           });

                parent.document.getElementById('loading').style.display='none';
                displayRepTimeInfo('<%=PbReportId%>')
             }


            function shwStickyNote(stkid,reportId){

                var divObj=document.getElementsByName('name'+stkid);
                //                alert("divObj length "+divObj.length)
                if(divObj.length>0){
                    $.ajax({
                        type: 'GET',
                        async: false,
                        cache: false,
                        timeout: 30000,
                        url: 'stickyNoteAction.do?stickyNoteParam=forshowStickSession&disp=none&stickListId='+stkid,
                        success: function(data){
                            document.getElementById(stkid).style.display = "";
                            // window.parent.location.reload(true)
                            //                            document.getElementById(stkid).display="";
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
//                             alert("data--"+data)
//                            var divObj=document.getElementById("showStickyNote")
                            $("#showStickyNote").html(data);
                            $("#showStickyNote").show();
                            document.getElementById("showStickyNote").style.display = "";
                        }
                    });
                }

            }
            function deleteSticky(stkId){
                // var source = "pbUpdateNotes.jsp?operation=D&REPORTID="+parent.document.getElementById("REPORTID").value+"&noteId="+stkId;
                var cofText=confirm("Sure,Do You Want to Delete")
                if(cofText==true){
                    $.ajax({
                        type: 'GET',
                        async: false,
                        cache: false,
                        timeout: 30000,
                        url: 'stickyNoteAction.do?stickyNoteParam=deleteStickyNote&stickNodeId='+stkId,
                        success: function(data){
                            //$('#pbStickyNote').attr(stkId).remove();
                            //                            var $currentSpan = $('#StickNoteSpan');
                            //                            var id="#"+stkId;
                            //                            $currentSpan.contents().find("div "+id).remove();
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
                //              //  alert("applyColor")
                var divObj=document.getElementById(divid);
                var trObj=divObj.getElementsByTagName("tr")
                var txtObj=divObj.getElementsByTagName("textarea")
                var tabObj=divObj.getElementsByTagName("table")
                var tdObj=trObj[1].getElementsByTagName("td");
                tdObj[0].style.backgroundColor = color;
                trObj[1].style.backgroundColor = color;
                txtObj[0].style.backgroundColor = color;
                tabObj[0].style.backgroundColor = color;
                //                var divObj=document.getElementById(divid);
                //                divObj.style.backgroundColor=color;

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
var comments=$("#comments").val();
if(selectusers=="")
{
        alert("select Selective Users")
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
url:'<%=request.getContextPath()%>/reportViewer.do?reportBy=sendSharereportMail&varhtml=fromAdvancedHtml&repID='+<%=PbReportId%>+'&fileType='+fileType+'&selectusers='+selectusers+'&share_subject='+share_subject+'&userstextarea='+encodeURIComponent(userstextarea)+'&comments='+encodeURIComponent(comments),
success:function(data)
{
    $("#loading").hide();
    alert("Report shared sucessfully with the Users");
}
});
}
}

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

//    var ta= document.getElementById(id);
//ta.style.visibility='visible';
//if (obj.selected=="All"){
//ta.style.visibility='hidden';
}

    function storesnapshot(){
//        alert("123");

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
  //  var sqlString=sqlStr.replace("~","'","gi");
      $("#showSqlStrDialog").dialog('open');
      $("#showSqlStrDialog").html(document.getElementById("showSqlbox").value);
     }
     var schedulecnt;
     function openScheduleReport()
     {
         $("#scheduleName").val("");
         $("#usertextarea").val("");
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
          $("#ScheduleReport").dialog("close");
          $("#loading").show();
        // var url= parent.document.getElementById("txtcompleteurl").value;

         $.post("<%=request.getContextPath()%>/reportViewer.do?reportBy=dailyScheduleReport&reportId="+<%=PbReportId%>,$("#dailyReportScheduleForm").serialize(),
                                function(data){
                               alert("Report is Scheduled Sucessfully")
                                    $("#loading").hide();
                                });
     }
     function updateScheduleReport(){
         $("#ScheduleReport").dialog("close");
//          $("#loading").show();
           var sId=$("#schedulerId").val();
            $.post("<%=request.getContextPath()%>/reportViewer.do?reportBy=updateScheduleDetails&reportId="+<%=PbReportId%>+'&schedulerId='+sId,$("#dailyReportScheduleForm").serialize(),
                                function(data){
                               alert("Report is Scheduled Sucessfully");
                                    dispScheduleReports();
                                });

//                var confirmDel=confirm("Do you want to Overwrite the Schedule");
//             if(confirmDel==true){
//                var url= parent.document.getElementById("txtcompleteurl").value;
//        $.post("<%=request.getContextPath()%>/reportViewer.do?reportBy=dailyScheduleReport&reportId="+<%=PbReportId%>+'&url='+url+'&schedulerId='+sId,$("#dailyReportScheduleForm").serialize(),
//                                function(data){
//                               alert("Report is Scheduled Sucessfully")
//                                    $("#loading").hide();
//                                    dispScheduleReports();
//                                });
//             }
             }
   //  }
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
//            function dispScheduler(sId,sName)
//            {
//                $.ajax({
//                url:'<%=request.getContextPath()%>/reportViewer.do?reportBy=editSchedulerDetails&schedulerId='+sId,
//                success:function(data)
//                {
//                    if(data!='null'){
//                     var jsonVar=eval('('+data+')')
//                     $("#scheduleName").val(jsonVar.schedulerName);
//                     $("#usertextarea").val(jsonVar.ReportmailIds);
//                     $("select#Format").attr("value",jsonVar.contentType);
//                     var sd = new Date(jsonVar.startDate)
//                     var ed = new Date(jsonVar.endDate)
////                     var strtDate=1+(sd.getMonth())+"/"+sd.getDate()+"/"+sd.getFullYear();
////                     var endDate=1+(ed.getMonth())+"/"+ed.getDate()+"/"+ed.getFullYear();
//                     var strtDate=sd.getDate()+"/"+(1+(sd.getMonth()))+"/"+sd.getFullYear();
//                     var endDate=ed.getDate()+"/"+(1+(ed.getMonth()))+"/"+ed.getFullYear();
//                     $("#sDatepicker").val(strtDate);
//                     $("#eDatepicker").val(endDate);
//                    $("select#Data").attr("value",jsonVar.dataSelection);
//                    var time=jsonVar.scheduledTime.split(":");
//                      $("select#hrs").attr("value",time[0]);
//                      $("select#mins").attr("value",time[1]);
//                      $("#deleteScheduleReport").show();
//                      $("#updateScheduleReport").show();
//                      $("#saveScheduleReport").hide();
//                      $("#schedulerId").val(sId);
//                    $("#ScheduleReport").dialog("open");
//                    }
//                  }
//                  });
//
//                  }
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
 function advanceFormula(columnName,disColumnName,REPORTID,path){
    var ctxPath='<%=request.getContextPath()%>'
                $.ajax({
                    type: 'GET',
                        async: false,
                        cache: false,
                        timeout: 30000,
                    url:ctxPath+'/reportViewer.do?reportBy=loadDialogs&loadDialogs=true&reportId='+REPORTID+'&from=viewer',
                    success:function(data) {
//                          $("#advanceFormulaDiv").dialog('open');

                        var frameObj = document.getElementById("advanceFormulaFrame");
                        frameObj.src = "CreateAdvanceFormula.jsp?columnName="+columnName;
                        $('#advanceFormulaDiv').dialog('open');

                    }
                });
//                if($.browser.msie == true)
//                {
//                    document.getElementById("advanceFormula").src="CreateAdvanceFormula.jsp&columnName="+columnName;
//                }
 }
 //added by Nazneen for Segmentation
 function createSegmentation(columnName,disColumnName,REPORTID,path){
        $.post("<%= request.getContextPath()%>/userLayerAction.do?userParam=getElementColType&columnName="+columnName,function(data){
            var jsonVar=eval('('+data+')')
            var colType=jsonVar.userColType[0];
            folderId=jsonVar.folderId[0];
            if(colType=="NUMBER" || colType=="number" || colType=="calculated" || colType=="CALCULATED"){
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
                    showBucketDbExistance(bussTableId);
                    //      break;
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
            createBucket(colName,colId,tabId,colType,tabName,grpId,bussColId,bussTableId);
        }
        else{
            alert('Please create Bucket Tables in your Database');
        }
    }
}
function createBucket(colName,colId,tabId,colType,tabName,grpId,bussColId,bussTableId)
    {
        var tempBuck = 'yes';
        var frameObj=document.getElementById("bucketDisp1");
        var source = "createBucket.jsp?colName="+colName+"&colId="+colId+"&tabId="+tabId+"&colType="+colType+"&tabName="+tabName+"&grpId="+grpId+"&bussColId="+bussColId+"&bussTableId="+bussTableId+"&tempBuck="+tempBuck+"&folderId="+folderId;

        if(colType==undefined){
            alert("Please Select Measure Column")
        }else{
            frameObj.src=source;
            $("#createBucketdiv").dialog('open')
        }

    }
//end of segmentation code

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
                     $("select#Format").attr("value",jsonVar.contentType);
                     var sd = new Date(jsonVar.startDate)
                     var ed = new Date(jsonVar.endDate)
//                     var strtDate=1+(sd.getMonth())+"/"+sd.getDate()+"/"+sd.getFullYear();
//                     var endDate=1+(ed.getMonth())+"/"+ed.getDate()+"/"+ed.getFullYear();
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
           //  var url= parent.document.getElementById("txtcompleteurl").value;

             if(confirmDel==true){
               $.ajax({
                   type: 'GET',
                        async: false,
                        cache: false,
                        timeout: 30000,
                   url:'<%=request.getContextPath()%>/reportViewer.do?reportBy=updateScheduleParamMetadataDetails&reportId='+<%=PbReportId%>+'&schedulerId='+scheduleId,
                success:function(data)
                {
//                    alert("Report is Scheduled Sucessfully");
                    dispScheduleReports();
                }
                });
                }
        }
        $("#editReportSchedule").dialog("close");
            }
            function getDynamicDetails(){
                var timeDet = "";
                if(document.getElementById("static") != null && document.getElementById("static").checked){
                    $("#selectTimeDet").hide();
                }else if(document.getElementById("dynamic") != null && document.getElementById("dynamic").checked){
                    $("#selectTimeDet").show();
                }
            }

            //added  by anitha
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
                //$( "#BusRolesTab" ).hide();

            }
            function hideParamList()
            {
                $( "#tabParameters" ).hide();
//                $( "#paramRegionTop" ).toggle(500 );
            }
            function tabparamListDisp(){
                var viewbys=$("#numbuerOfViewbys").val();
                    if (!$('#tabParameters').is(':visible')) {
                        var divHeight=(4/5)*($(window).height());
                        $("#tabParameters").height(divHeight);
                        $(".dynamicClass").css('height', (((9/10)*(divHeight))-(viewbys*12)));

                    }
                $( "#tabParameters" ).toggle("slow" );
               // $( "#paramRegionTop" ).toggle(500 );
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
                //$( "#allParametersTab" ).hide();
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
                //$( "#BusRolesTab" ).hide();
                $( "#remainingRegions" ).hide();
                //$( "#allParametersTab" ).toggle("slow" );
            }
            function allActions(){
                $( "#paramRegionTop" ).hide();
                $( "#scheduledReps" ).hide();
                $( "#snapShotsDiv" ).hide();
                $( "#manageStickyDispDiv" ).hide();
                $( "#topBottomDispDiv" ).hide();
                $( "#allActions" ).toggle(500 );
                $( "#busRoleDispDiv" ).hide();
                //$( "#BusRolesTab" ).hide();
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
                //$( "#BusRolesTab" ).hide();
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
                //$( "#BusRolesTab" ).hide();
                $( "#remainingRegions" ).hide();
                    document.getElementById("snapshots").style.display = "block";
                    var frameObj = document.getElementById("widgetframe");
                    var source = 'divPersistent.jsp?method=forsnapshots&block=yes';
                    frameObj.src = source;
            }
            function getdisplayStickyNotes(){
                $("#manageStickyDispDiv").show();
                //$("#stickListDiv").toggle(500);
                $( "#paramRegionTop" ).hide();
                $( "#allActions" ).hide();
                $( "#scheduledReps" ).hide();
                $( "#topBottomDispDiv" ).hide();
                $( "#snapShotsDiv" ).hide();
                $( "#busRoleDispDiv" ).hide();
                //$( "#BusRolesTab" ).hide();
                $( "#remainingRegions" ).hide();
                    document.getElementById("manageSticky").style.display = "block";
                    var frameObj = document.getElementById("widgetframe");
                    var source = 'divPersistent.jsp?method=forManageSticky&block=yes';
                    frameObj.src = source;
            }
            function getdisplayTopBottom(){
                $("#manageStickyDispDiv").hide();
                $("#topBottomDispDiv").show();
                //$("#stickListDiv").toggle(500);
                $( "#paramRegionTop" ).hide();
                $( "#allActions" ).hide();
                $( "#scheduledReps" ).hide();
                $( "#snapShotsDiv" ).hide();
                $( "#busRoleDispDiv" ).hide();
                //$( "#BusRolesTab" ).hide();
                $( "#remainingRegions" ).hide();
                    document.getElementById("topBot").style.display = "block";
                    var frameObj = document.getElementById("widgetframe");
                    var source = 'divPersistent.jsp?method=fortopBot&block=yes';
                    frameObj.src = source;
            }
            function getdisplayBusRoles(){
                $("#manageStickyDispDiv").hide();
                $("#topBottomDispDiv").hide();
                //$("#stickListDiv").toggle(500);
                $( "#paramRegionTop" ).hide();
                $( "#allActions" ).hide();
                $( "#scheduledReps" ).hide();
                $( "#snapShotsDiv" ).hide();
                $( "#remainingRegions" ).hide();
                $( "#busRoleDispDiv" ).show();
                //$( "#BusRolesTab" ).show();
//                document.getElementById("BusRolesTab").style.display = "block";
//                    var frameObj = document.getElementById("widgetframe");
//                    var source = 'divPersistent.jsp?method=forBusRoles&block=yes';
//                    frameObj.src = source;
            }
            function remainingRegions(repId){
                $( "#paramRegionTop" ).hide();
                   $( "#allActions" ).hide();
                $( "#snapShotsDiv" ).hide();
                $( "#manageStickyDispDiv" ).hide();
                $( "#topBottomDispDiv" ).hide();
                $( "#scheduledReps" ).hide();
                $( "#busRoleDispDiv" ).hide();
                //$( "#BusRolesTab" ).hide();
                $( "#remainingRegions" ).show();
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
//                              html+="<td><input type='text' id=\'elementId"+i+"\'   value="+elementId[i]+"></td>";
//                              html+="<td><input type='text' id=\'paramdisId"+i+"\'   value="+paramdisId[i]+"></td>";
                              html+="<td><select id =\'paramid-"+paramdisId[i]+"\' name=\'paramid-"+paramdisId[i]+"\'>";
//                              alert("selected param"+parametertype[i])
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
                              html+="</select></td>";
                              html+="</tr>";

                         }

//                         html+="<tr>&nbsp;<tr>";
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
                    //          window.location.href= window.location.href;
                            });
}
            //added by Nazneen
                 function getFullCompNames(fullCompNames,totalComp){
                    var htmlVar = "<table>";
                    var  compNameArray = new Array();
                       compNameArray = fullCompNames.toString().split(",");
                       for(var i=0;i<totalComp;i++){
                           var k= i+1;
                          htmlVar += "&nbsp;<tr><td>"+k+".)&nbsp;"+compNameArray[i]+"</td></tr>";
                       }
                       htmlVar += "</table>";
                 $("#moreCompTab").html(htmlVar);
                 $("#moreCompanyDiv").dialog('open');
                 }

                 function showRepPrevState(pbreportid){
                     var url= "";
//                     if(repPrevStateCnt == 0)
//                         repPrevStateCnt = repPrevStateCnt
//                     else
//                     repPrevStateCnt = repPrevStateCnt + 1 ;
                     url = 'reportViewer.do?reportBy=viewReport&REPORTID='+pbreportid+'&action=paramChange'+'&repPrevStateCnt='+<%=container.prevStateCnt%>+'&ShowPrevState=true';
                     submiturls(url);

                 }
                 function savereportDetails(reportId,reportType,userId,userFolderId,grpIds){
                dispReports(reportId,reportType,userId,userFolderId,grpIds);
            }


function getDisplayTables(ctxpath,paramslist){
//        getdimmap.getFact(Parameters);
        var check = $("#tableList1").is(":checked")
        if($("#tableList1").is(":checked")){
            $("#tabListDiv1").hide();
            $("#tablistLink1").hide();
            $("#goButton1").hide();
            $("#tabsListIds1").val("");
            $("#tabsListVals1").val("");
            $.post(ctxpath+"/reportViewer.do?reportBy=setTableListToContainer&repId="+'<%= PbReportId%>',
                function(data){
                    document.getElementById("tableColsFrame").src="TableDisplay/PbChangeTableColumnsRT.jsp?loadDialogs=true&currentReportId="+'<%= PbReportId%>'+"&currentBizRoles="+'<%=rolesid%>'+'&tableList=true'+"";
                });
            //$("#tableList").attr('checked',true);
        }else{
            $("#tabListDiv1").show();
            $("#tablistLink1").show();
            $("#goButton1").show();
            var htmlVar = "<table>";
           $.post(ctxpath+"/reportViewer.do?reportBy=getTableList&paramslist="+paramslist+'&currentBizRoles='+'<%=rolesid%>',
                function(data){
                    var jsonVar=eval('('+data+')')
                    var json1 = jsonVar.idsList.split(",");
                    var jsonname = jsonVar.namesList.split(",");
                    for(var i=0;i<json1.length;i++){
                        //alert(jsonname[i])
                        if(json1[i].replace(" ", "", "gi") != "0")
                        htmlVar += "<tr><td id='"+json1[i]+"' onclick=\"selectTables('"+json1[i]+"','"+jsonname[i]+"')\">"+jsonname[i]+"</td></tr>";
                    }
                    htmlVar += "</table>";
                    $("#paramVals1").html(htmlVar);
                    document.getElementById("tableColsFrame").src="TableDisplay/PbChangeTableColumnsRT.jsp?loadDialogs=true&currentReportId="+'<%= PbReportId%>'+"&currentBizRoles="+'<%=rolesid%>'+"";
                });

        }
    }
       function selectTables(tdId,tname){
        //alert(tname)
        document.getElementById(tdId).style.display='none';
        if($("#tabsListVals1").val() == ""){
            $("#tabsListVals1").val(tname)
            $("#tabsListIds1").val(tdId)
        }else{
            var Ids = $("#tabsListIds1").val()+","+tdId
            var value = $("#tabsListVals1").val()+","+tname
            $("#tabsListIds1").val(Ids)
            $("#tabsListVals1").val(value)
        }
    }
    function showList(ctxpath,paramslist){
        if(document.getElementById("paramVals1").style.display=='none'){
            $("#paramVals1").show();
            $("#tabListDiv1").show();
            $("#tablistLink1").show();
            $("#goButton1").show();
            var htmlVar = "<table>";
           $.post(ctxpath+"/reportViewer.do?reportBy=getTableList&paramslist="+paramslist+'&currentBizRoles='+'<%=rolesid%>',
                function(data){
                    var jsonVar=eval('('+data+')')
                    var json1 = jsonVar.idsList.split(",");
                    var jsonname = jsonVar.namesList.split(",");
                    for(var i=0;i<json1.length;i++){
                        //alert(jsonname[i])
                        if(json1[i].replace(" ", "", "gi") != "0")
                        htmlVar += "<tr><td id='"+json1[i]+"' onclick=\"selectTables('"+json1[i]+"','"+jsonname[i]+"')\">"+jsonname[i]+"</td></tr>";
                    }
                    htmlVar += "</table>";
                    $("#paramVals1").html(htmlVar);

                });
        }else{
           $("#paramVals1").hide();
        }

    }
    function selectTables(tdId,tname){
        //alert(tname)
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


//    function showYearvsyear(calname){
////    alert("hii");
//    $("#showCalyear").dialog('open');
//
////     alert("hii");
//    document.getElementById("myyear2").onclick=function(){
//        saveyear2(calname);
//    }
//}

               function saveyear2(calname){

                //alert("hello saveyear1")
                if(calname=="Select-Year")
                {alert("Please Chosse Correct Year"); }
                else{
                $('#datetext').val('topdate');
                var calyear;var dateArr=new Array();
//                alert(calname)
                if(calname=="perioddate")
                {
                calyear = $("#calyear").val();
                calyear="Mon,01,Jan,".concat(calyear)
               dateArr=calyear.split(",");
                $('#perioddate').val(calyear);
                 if(calyear!=""){$("#pfield1").html(dateArr[3])}
                  $("#showCalyear").dialog('close');
               }
                if(calname=="fromdate")
                {
                calyear = $("#calyear").val();
                calyear="Mon,01,Jan,".concat(calyear)
               dateArr=calyear.split(",");
                $('#fromdate').val(calyear);
                 if(calyear!=""){$("#field1").html(dateArr[3])}
                  $("#showCalyear").dialog('close');
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
                      $("#showCalyear").dialog('close');
               }
                if(calname=="comparefrom")
                {
                calyear = $("#calyear").val();
                calyear="Mon,01,Jan,".concat(calyear)
               dateArr=calyear.split(",");
                $('#comparefrom').val(calyear);
                 if(calyear!=""){$("#cffield1").html(dateArr[3])}
                  $("#showCalyear").dialog('close');
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
                                  $("#showCalyear").dialog('close');
               }}
            }


    function setValueToContainer(ctxpath,repId,bizRoles){
        $("#paramVals1").hide();
        var tabLst = $("#tabsListIds1").val();
        $("#tabsListVals1").val('')
            $("#tabsListIds1").val('')
        $.post(ctxpath+"/reportViewer.do?reportBy=setTableListToContainer&repId="+repId+'&tabLst='+tabLst,
                function(data){
                    document.getElementById("tableColsFrame").src="TableDisplay/PbChangeTableColumnsRT.jsp?loadDialogs=true&currentReportId="+repId+"&currentBizRoles="+bizRoles+"";
                });
    }
    function getgraphDisplayTables(ctxpath,paramslist){
//        getdimmap.getFact(Parameters);
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
            //$("#tableList").attr('checked',true);
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
                        //alert(jsonname[i])
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
                        //alert(jsonname[i])
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
        //alert(tname)
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
                    // cell1.style.backgroundColor="#e6e6e6";

                    var a=document.createElement("a");
                    a.href="javascript:deleteColumn1('"+uid[1]+"')";
                    a.innerHTML="a";
                    a.className="ui-icon ui-icon-close";
                    cell1.appendChild(a);
                    var cell2=row.insertCell(1);
                    //cell2.style.backgroundColor="#e6e6e6";
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
                //parent.cancelreportDetails();
                if(sourcepage=="Designer"){
                document.forms.myFormDetails.action="reportViewer.do?reportBy=backToReportDesigner&REPORTID=<%=reportId%>";

                document.forms.myFormDetails.submit();
                }else{
                    document.forms.myFormDetails.action="reportViewer.do?reportBy=viewReport&REPORTID=<%=oldReportId%>";

                    document.forms.myFormDetails.submit();


                }
            }
  function savereportDetailsUser(reportId,reportType,userId,userFolderId,grpIds){
//  alert(reportId);
                dispReportsUser(reportId,reportType,userId,userFolderId,grpIds);
            }

            function moveAll(allReports,allNames){
                var allList=allReports.split(",");
                var allNamesList=allNames.split(",");
                for(var i=0;i<allList.length;i++){

                    createColumn(allList[i],allNamesList[i]);
                }
            }
            function deleteAll1(){

                msrArray1.splice(0,msrArray1.length);
                var parentUL=document.getElementById("sortable1");
                parentUL.innerHTML='';

            }
            function deleteAll(){

                msrArray.splice(0,msrArray.length);
                var parentUL=document.getElementById("sortable");
                parentUL.innerHTML='';

            }
            function moveAll1(allUsers,allNames){



                var allList=allUsers.split(",");
                var allNamesList=allNames.split(",");
                for(var i=0;i<allList.length;i++){

                    createColumn1(allList[i],allNamesList[i]);
                }

            }

            function checkExist(chk){

                var chkarr=chk.split("*");


                for(var i=0;i<chkarr.length;i++){

                    msrArray1.push(chkarr[i]);
                }
            }
            function deleteColumnParameters(viwById,viewByname){
                var LiObj=document.getElementById(viwById);

                var parentUL=document.getElementById("removeDimValues");

                parentUL.removeChild(LiObj);
            }
          </script>
                     <script>
        $(function() {

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

            })
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

            })
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

            })
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

            })
<% if( sytm.equalsIgnoreCase("No")) { %>
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


            })
          <%}%>
        })

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
     function dateclick()
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

                              <% //if(timeinfo.get(1).equalsIgnoreCase("PRG_DATE_RANGE")) { %>
                              $('#datepicker1').val(fromdate);
                              $('#datepicker2').val(todate);
                              $('#datepicker3').val(comparefrom);
                                $('#datepicker4').val(compareto);
                              <%// }else{%>
                                     $('#datepicker').val(perioddate);
                                  <%//}%>
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
//                              html+="<td><input type='text' id=\'elementId"+i+"\'   value="+elementId[i]+"></td>";
//                              html+="<td><input type='text' id=\'paramdisId"+i+"\'   value="+paramdisId[i]+"></td>";
                              html+="<td><select id =\'paramid-"+paramdisId[i]+"\' name=\'paramid-"+paramdisId[i]+"\'>";
//                              alert("selected param"+parametertype[i])
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
                              html+="</select></td>";
                              html+="</tr>";

                         }

//                         html+="<tr>&nbsp;<tr>";
                         foothtml+="<tr><td align='center' colspan='4' align='center' rowspan='2'><center><input type='button' value='Done' class='navtitle-hover' style='width:auto;height:20px;color:black' onclick=updateRepParams(<%=reportId1%>,'"+paramdisId+"') ></center></td></tr>";
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
                    //          window.location.href= window.location.href;
                            });
}
            //added by Nazneen
                 function getFullCompNames(fullCompNames,totalComp){
                    var htmlVar = "<table>";
                    var  compNameArray = new Array();
                       compNameArray = fullCompNames.toString().split(",");
                       for(var i=0;i<totalComp;i++){
                           var k= i+1;
                          htmlVar += "&nbsp;<tr><td>"+k+".)&nbsp;"+compNameArray[i]+"</td></tr>";
                       }
                       htmlVar += "</table>";
                 $("#moreCompTab").html(htmlVar);
                 $("#moreCompanyDiv").dialog('open');
                 }

                 function showRepPrevState(pbreportid){
                     var url= "";
//                     if(repPrevStateCnt == 0)
//                         repPrevStateCnt = repPrevStateCnt
//                     else
//                     repPrevStateCnt = repPrevStateCnt + 1 ;
                     url = 'reportViewer.do?reportBy=viewReport&REPORTID='+pbreportid+'&action=paramChange'+'&repPrevStateCnt='+<%=container.prevStateCnt%>+'&ShowPrevState=true';
                     submiturls(url);

                 }
                    function datetoggl(reportId){
   var ctxPath=document.getElementById("h").value;
            $.post(
                  ctxPath+'/reportViewer.do?reportBy=dateToggle&REPORTID='+reportId,
                   function(data){
                    if(data=='Success'){
                          document.forms.submitReportForm.action=ctxPath+"/reportTemplateAction.do?templateParam=selectRoleGoToDesin&repId="+reportId+"&roleId="+<%=rolesid%>+"&isToggle=Y";
                         document.forms.submitReportForm.submit();
                    }

                });
}

function  dispTable1(){
    var previewDispTableDiv;
    var action="paramChange";
    var perioddate=$('#perioddate').val();
    var CBO_PRG_COMPARE=$('#CBO_PRG_COMPARE').val();
    var CBO_PRG_PERIOD_TYPE=$('#CBO_PRG_PERIOD_TYPE').val();
    var datetext=$('#datetext').val();
     $('#iframe1').contents().find('body').append('<center><img id="imgId" src="images/ajax1.gif" align="middle"  width="75px" height="75px"  style="position:absolute" ></center>');

    $.ajax({
        url: 'reportTemplateAction.do?templateParam=dispTable'+'&REPORTID='+document.getElementById("REPORTID").value+'&action='+action+'&paramChange1=paramChange1&perioddate='+perioddate+'&CBO_PRG_COMPARE='+CBO_PRG_COMPARE+'&CBO_PRG_PERIOD_TYPE='+CBO_PRG_PERIOD_TYPE+'&datetext='+datetext,
        success: function(data){
            //
            if(data!=""){
                //previewDispTableDiv.innerHTML=data.split("@!")[0];
                sqlStr=data.split("@!")[1];
                var source = "TableDisplay/pbDisplay.jsp?source=L&tabId="+document.getElementById("REPORTID").value;
                var dSrc = document.getElementById("iframe1");

                dSrc.src = source;
            }
            else{
                //previewDispTableDiv.innerHTML="";
            }
        }
    });
}
function  dispTable2(){
    var previewDispTableDiv;
    var action="paramChange";
    var CBO_PRG_COMPARE=$('#CBO_PRG_COMPARE').val();
    var CBO_PRG_PERIOD_TYPE=$('#CBO_PRG_PERIOD_TYPE').val();
    var datetext=$('#datetext').val();
    var fromdate=$('#fromdate').val();
    var todate=$('#todate').val();
    var comparefrom=$('#comparefrom').val();
    var compareto=$('#compareto').val();
     $('#iframe1').contents().find('body').append('<center><img id="imgId" src="images/ajax1.gif" align="middle"  width="75px" height="75px"  style="position:absolute" ></center>');

    $.ajax({
        url: 'reportTemplateAction.do?templateParam=dispTable'+'&REPORTID='+document.getElementById("REPORTID").value+'&action='+action+'&paramChange1=paramChange1&CBO_PRG_COMPARE='+CBO_PRG_COMPARE+'&CBO_PRG_PERIOD_TYPE='+CBO_PRG_PERIOD_TYPE+'&datetext='+datetext+'&fromdate='+fromdate+'&todate='+todate+'&comparefrom='+comparefrom+'&compareto='+compareto,
        success: function(data){
            //
            if(data!=""){
                //previewDispTableDiv.innerHTML=data.split("@!")[0];
                sqlStr=data.split("@!")[1];
                var source = "TableDisplay/pbDisplay.jsp?source=L&tabId="+document.getElementById("REPORTID").value;
                var dSrc = document.getElementById("iframe1");

                dSrc.src = source;
            }
            else{
                //previewDispTableDiv.innerHTML="";
            }
        }
    });
}
 </script>
    </body>
</html>
<%             }
                } catch (Exception exp) {
                    exp.printStackTrace();
                }

            } else {
                response.sendRedirect(request.getContextPath() + "/pbSessionExpired.jsp");
            }
%>