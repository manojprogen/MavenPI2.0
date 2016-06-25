<%--
    Document   : pbBIManager
    Created on : 1 Aug, 2012, 2:47:11 PM
    Author     : Anil
--%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%@page  contentType="text/html" pageEncoding="UTF-8" import="com.progen.reportview.db.ProgenReportViewerDAO,com.progen.i18n.TranslaterHelper,java.text.SimpleDateFormat"%>
<%@page import="prg.db.PbReturnObject,prg.db.Container,prg.db.PbDb,java.util.*,utils.db.ProgenConnection,com.progen.action.UserStatusHelper,java.lang.management.ManagementFactory"%>
<%@page import="javax.management.Attribute,javax.management.AttributeList,javax.management.ObjectName,javax.management.MBeanServer"%>
<%
//added By Mohit Gupta for Session validation
if (session.getAttribute("USERID") == null || request.getSession(false) == null) {
        response.sendRedirect(request.getContextPath() + "/baseAction.do?param=logoutApplication");
    }else{
        String status = String.valueOf(session.getAttribute("status"));
        ProgenReportViewerDAO dao=new ProgenReportViewerDAO();
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
            }
        }

        //added by Nazneen for enabling tabs based on Users
//         ArrayList talentIds = new ArrayList();
//         PreparedStatement psGetDetails;
//        Connection conSource = ProgenConnection.getInstance().getConnection();
//        String getTalentQuery = "select TALENT_ID,ISENABLE from prg_pb_talent order by ID";
//        psGetDetails = conSource.prepareStatement(getTalentQuery);
//        ResultSet pbro = psGetDetails.executeQuery();
//         PbDb pbdb=new PbDb();

        PbReturnObject pbro1=dao.selectprgpbtalent();
//        PbReturnObject pbro1= pbdb.executeSelectSQL("select TALENT_ID,ISENABLE from prg_pb_talent order by ID");
        List<String> talentId = new ArrayList();
        List<String> talentEnable = new ArrayList();
        HashMap <String,List<String>>hashTable = new HashMap();
        if(pbro1.getRowCount()>0){
            for (int i = 0; i < pbro1.getRowCount(); i++) {
               talentId.add(pbro1.getFieldValueString(i, 0));
               talentEnable.add(pbro1.getFieldValueString(i, 1).toUpperCase());
            }
//            hashTable.put("talentId", talentId);
//            hashTable.put("talentEnable", talentEnable);
        }
//         SimpleDateFormat dateFormat = new SimpleDateFormat();
//          Date varDate=null;
//        dateFormat = new SimpleDateFormat("dd-MMM-yy");
//         varDate= dateFormat.parse("3-May-4");
        //

         //MBeanServer mbs    = ManagementFactory.getPlatformMBeanServer();
    //ObjectName name    = ObjectName.getInstance("java.lang:type=OperatingSystem");
    //AttributeList list = mbs.getAttributes(name, new String[]{ "ProcessCpuLoad" });

   // if (list.isEmpty())
       // double d=Double.NaN;

    //Attribute att = (Attribute)list.get(0);
   // Double value  = (Double)att.getValue();
//
    //if (value == -1.0)       Double.NaN;
      // Double d= ((int)(value * 1000) / 10.0);
//long heapSize = Runtime.getRuntime().totalMemory();

        //Print the jvm heap size.
        //
        Container container = (Container) session.getAttribute("isExcelAdded");
        Container container1 = (Container) session.getAttribute("isLoadedData");
//
        //added by Dinanath
            Locale clocale = null;
            clocale = (Locale) session.getAttribute("UserLocaleFormat");
             String themeColor="blue";
    if(session.getAttribute("theme")==null)
        session.setAttribute("theme",themeColor);
   else
       themeColor=String.valueOf(session.getAttribute("theme"));
             String contextPath=request.getContextPath();

   %>


<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<!--        <title>pi</title>
         <title><bean:message key="ProGen.Title"/></title>-->
        <link rel="stylesheet" href="stylesheets/treeviewstyle/jquery.treeview.css" />
        <link rel="stylesheet" href="stylesheets/treeviewstyle/screen.css" />
          <link type="text/css" href="<%=contextPath%>/jQuery/jquery/themes/base/BreadCrumb.css" rel="stylesheet" />
        <link type="text/css" href="<%=contextPath%>/jQuery/jquery/themes/base/Base.css" rel="stylesheet" />
        <link type="text/css" href="stylesheets/ui.all.css" rel="stylesheet" />
        <link type="text/css" href="stylesheets/demos.css" rel="stylesheet" />
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=themeColor%>/jquery-ui-1.7.3.custom.css" rel="stylesheet" />
        <link href="stylesheets/StyleSheet.css" rel="stylesheet" type="text/css" />
        <link href="stylesheets/confirm.css" rel="stylesheet" type="text/css" />
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
        <script type="text/javascript" src="javascript/queryDesign.js"></script>
        <script type="text/javascript" src="javascript/effects.core.js"></script>
        <script type="text/javascript" src="javascript/effects.explode.js"></script>
        <script language="JavaScript" src="<%=contextPath%>/querydesigner/JS/jquery.columnfilters.js"></script>
        <!-- below five lines only added by bharathi reddy on 26-08-09 -->
        <script src="javascript/jquery.simplemodal-1.1.1.js" type="text/javascript"></script>
        <script src="javascript/jquery.contextMenu.js" type="text/javascript"></script>
        <script type="text/javascript" src="javascript/ui.tabs.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/jQuery/jquery/ui/jquery.jBreadCrumb.1.1.js"></script>
         <script type="text/javascript" src="<%=contextPath%>/javascript/ui.dialog.js"></script>
<!--          <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.tablesorter.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/tablesorter/addons/pager/jquery.tablesorter.pager.js"></script>-->
         <script type="text/javascript" src="<%= contextPath%>/JS/global.js"></script>
        
                      <style type="text\css" >
.list{text-align:center}
                           #extratabs ul li{list-style-type: none;text-align: center;}
#extratabs ul li a{text-decoration: none;border-bottom: 1px solid white;display:block}
                            .migrate{
                                font-family: inherit;
                                font-size: 10pt;
                                color: #000;
                                padding-left:12px;
                                background-color:#8BC34A;
                                border:0px;
                            }
                      </style>
    </head>
    <body>
         <table style="width:100%;">
                    <tr>
                            <td valign="top" style="width:100%;">
                                <%  if (status.equalsIgnoreCase("OK")) {%>
                                <jsp:include page="Headerfolder/headerPageVeraction.jsp"/>
                                <%} else {%>
                                <jsp:include page="Headerfolder/headerPage.jsp"/>
                                <%}%>
                        </td>
                    </tr>
                </table>

 <form  action="" id="baseForm" name="baseForm" method="post" style="padding:0pt;">
     <table width="99%" border="0" cellpadding="10" >
           <tbody>
           <tr>
            </tr>
            <tr>
                <td  valign="top" width="50%" align="center" >
                   <center >
                 <div id="tabs" style="width: 99%; min-height: 300px;padding:0px;border-color: #86A760" align="center">
                          <ul >
                              <!--added by Nazneen-->
                              <!--modified by sruthi-->
                              <div style="" id='biTabsMenu'>
                                     <%
                                  for (int i = 0; i < pbro1.getRowCount(); i++) {
                                if (talentId.get(i).equalsIgnoreCase("2") && talentEnable.get(i).equalsIgnoreCase("Y")) {
                                 %>
                              <li><a href="modifyMeasures.jsp" title="ModifyMeasures"  onclick="gotomore('ModifyMeasures')"><%=TranslaterHelper.getTranslatedInLocale("modify_measures", clocale)%></a></li>
                                 <% } if (talentId.get(i).equalsIgnoreCase("3") && talentEnable.get(i).equalsIgnoreCase("Y")) {
                                 %>
                             <li><a href="modifyMembers.jsp" title="modifyMembers" onclick="gotomore('modifyMembers')" ><%=TranslaterHelper.getTranslatedInLocale("modify_members", clocale)%></a></li>
                                <% } if (talentId.get(i).equalsIgnoreCase("4") && talentEnable.get(i).equalsIgnoreCase("Y")) {
                                 %>
                               <li> <a href="javascript:void(0)" onclick="gotomore('AdminTab')"><%=TranslaterHelper.getTranslatedInLocale("administrator", clocale)%></a></li>
                                <% } if (talentId.get(i).equalsIgnoreCase("5") && talentEnable.get(i).equalsIgnoreCase("Y")) {
                                 %>
                               <li><a href="editSchedulers.jsp" title="Manage Schedule" onclick="gotomore('Manage Schedule')"><%=TranslaterHelper.getTranslatedInLocale("manage_schedule", clocale)%></a></li>
                               <% } if (talentId.get(i).equalsIgnoreCase("6") && talentEnable.get(i).equalsIgnoreCase("Y")) {
                                 %>
                                  <li ><a href="ModifyDispName.jsp" title="ModifyDispFact" onclick="gotomore('ModifyDispName')"><%=TranslaterHelper.getTranslatedInLocale("modify_display_fact", clocale)%></a></li>
                                <% } if (talentId.get(i).equalsIgnoreCase("7") && talentEnable.get(i).equalsIgnoreCase("Y")) {
                                 %>

                               <li ><a href="ModifyDispDimName.jsp" title="ModifyDispDim" onclick="gotomore('ModifyDispName1')"><%=TranslaterHelper.getTranslatedInLocale("modify_display_dim", clocale)%></a></li>
                                <% } if (talentId.get(i).equalsIgnoreCase("8") && talentEnable.get(i).equalsIgnoreCase("Y")) {
                                 %>

                                   <li ><a href="MultiTalentSecurity.jsp" title="userSecurity" onclick="gotomore('userSecurity')"><%=TranslaterHelper.getTranslatedInLocale("security", clocale)%></a></li>
                                 <% } if (talentId.get(i).equalsIgnoreCase("9") && talentEnable.get(i).equalsIgnoreCase("Y")) {
                                 %>

                               <li ><a  href="MultiSecurity.jsp" title=" multiSecurity" onclick="gotomore('multiSecurity')"><%=TranslaterHelper.getTranslatedInLocale("multi_security", clocale)%></a></li>
                                  <% } if (talentId.get(i).equalsIgnoreCase("10") && talentEnable.get(i).equalsIgnoreCase("Y")) {
                                 %>
                              <li ><a href="CalenderManagement.jsp" title="CalenderManagement" onclick="gotomore('CalenderManagement')"><%=TranslaterHelper.getTranslatedInLocale("calendar_management", clocale)%></a></li>
                                 <% } if (talentId.get(i).equalsIgnoreCase("11") && talentEnable.get(i).equalsIgnoreCase("Y")) {
                                 %>
                               <li><a href="Uploadfile.jsp" title="DataUpload" onclick="gotomore('DataUpload')"><%=TranslaterHelper.getTranslatedInLocale("data_upload", clocale)%>Data Upload</a></li>
                                 <% } if (talentId.get(i).equalsIgnoreCase("12") && talentEnable.get(i).equalsIgnoreCase("Y")) {
                                 %>
                              <li><a href="uploadSSISData.jsp" title="UploadSSISData" onclick="gotomore('UploadSSISData')"><%=TranslaterHelper.getTranslatedInLocale("upload_ssis_data", clocale)%></a></li>

                                  </div>
                                   <div style='padding:5px;;cursor:pointer;position: absolute;right: 5px;top: 3px;'><a title="more" align="center" onclick="morelistDisp(event)" style='text-decoration: none;color:#000;' ><%=TranslaterHelper.getTranslatedInLocale("more", clocale)%></a>
                                   <div id="extradivs" style="display:none">
                                    <iframe name="extraframe" id="extraframe"  src="about:blank" ></iframe>
                                   </div>
<!--                                   TA_EN_1003 : More menu getting out of window screen-->
                                 <div id="extratabs" class="hideDropDownMenu" style="width: 200px;position: absolute; text-align: left; top:30px; right:-5px;display:none;background-color: #fff;box-shadow:0px 3px 3px 0px #d2d2d2 ;z-index: 1;overflow: hidden;overflow-y: auto;" >
                                       <ul>
                                         <% } if (talentId.get(i).equalsIgnoreCase("13") && talentEnable.get(i).equalsIgnoreCase("Y")){
                                 %>
                                   <li style="height:30%;width:100%;list-style-type:none;"><a  href="dataDownload.jsp" title="DataDownload" onclick="gotomore('DataDownload')"><%=TranslaterHelper.getTranslatedInLocale("data_download", clocale)%></a></li>
                                  <% } if (talentId.get(i).equalsIgnoreCase("14") && talentEnable.get(i).equalsIgnoreCase("Y")){
                                 %>
                                   <li style="height:30%;width:100%;list-style-type:none;"><a  href="DeleteData.jsp" title="DeleteData" onclick="gotomore('DeleteData')"><%=TranslaterHelper.getTranslatedInLocale("delete_data", clocale)%></a></li>
                                   <% }  if (talentId.get(i).equalsIgnoreCase("15") && talentEnable.get(i).equalsIgnoreCase("Y")) {
                                 %>
                               <li style="height:30%;width:100%;list-style-type:none;"><a href="favouriteReportsAssignment.jsp" title="FavouriteReportsAssignment" onclick="gotomore('FavouriteReportsAssignment')"><%=TranslaterHelper.getTranslatedInLocale("fav_reports_assignment", clocale)%></a></li>
                                <% } if (talentId.get(i).equalsIgnoreCase("16") && talentEnable.get(i).equalsIgnoreCase("Y")) {
                                 %>
                              <li style="height:30%;width:100%;list-style-type:none;" ><a href="businessPerformance.jsp" title="businessPerformance" onclick="gotomore('businessPerformance.jsp')"><%=TranslaterHelper.getTranslatedInLocale("targets", clocale)%></a></li>
                                 <% } if (talentId.get(i).equalsIgnoreCase("17") && talentEnable.get(i).equalsIgnoreCase("Y")) {
                                 %>
                              <li  style="height:30%;width:100%;list-style-type:none;"><a href="ConfigEtl.jsp" title="configEtl" onclick="gotomore('configEtl')"><%=TranslaterHelper.getTranslatedInLocale("config_etl", clocale)%></a></li>
                                 <% } if (talentId.get(i).equalsIgnoreCase("18") && talentEnable.get(i).equalsIgnoreCase("Y")) {
                                 %>
                                <li style="height:30%;width:100%;list-style-type:none;"><a href="defineRelatedMeasures.jsp" title="DefineRelatedMeasures" onclick="gotomore('DefineRelatedMeasures')"><%=TranslaterHelper.getTranslatedInLocale("define_related_measures", clocale)%></a></li>
                                 <% } if (talentId.get(i).equalsIgnoreCase("19") && talentEnable.get(i).equalsIgnoreCase("Y")) {
                                 %>
                               <li style="height:30%;width:100%;list-style-type:none;" ><a href="pbTargetMapping.jsp" title="TargetMapping" onclick="gotomore('TargetMapping')"><%=TranslaterHelper.getTranslatedInLocale("target_mapping", clocale)%></a></li>
                                 <% } if (talentId.get(i).equalsIgnoreCase("20") && talentEnable.get(i).equalsIgnoreCase("Y")) {
                                 %>
                                  <li style="height:30%;width:100%;list-style-type:none;"><a href="dbQueries.jsp" title="ETL" onclick="pagname('ETL')"><%=TranslaterHelper.getTranslatedInLocale("etl", clocale)%></a></li><br/>
                                 <% } if (talentId.get(i).equalsIgnoreCase("21") && talentEnable.get(i).equalsIgnoreCase("Y")) {
                                 %>
                                 <li style="height:30%;width:100%;list-style-type:none;"><a href="PlanCollection.jsp" title="PlanCollection" onclick="gotomore('PlanCollection')"><%=TranslaterHelper.getTranslatedInLocale("plan_collection", clocale)%></a></li>
                                 <% } if (talentId.get(i).equalsIgnoreCase("22") && talentEnable.get(i).equalsIgnoreCase("Y")) {
                                 %>
                                 <li style="height:30%;width:100%;list-style-type:none;"><a href="PlanPublish.jsp" title="PublishPlan" onclick="gotomore('PublishPlan')"><%=TranslaterHelper.getTranslatedInLocale("publish_plan", clocale)%></a></li>
                                  <% } if (talentId.get(i).equalsIgnoreCase("23") && talentEnable.get(i).equalsIgnoreCase("Y")) {
                                 %>
                                 <li style="height:30%;width:100%;list-style-type:none;"><a href="MonthlyPlanCollection.jsp" title="MonthlyPlanCollection" onclick="gotomore('MonthlyPlanCollection')"><%=TranslaterHelper.getTranslatedInLocale("monthly_plan_collection", clocale)%></a></li>
                                 <% } if (talentId.get(i).equalsIgnoreCase("24") && talentEnable.get(i).equalsIgnoreCase("Y")) {
                                 %>
                                <li style="height:30%;width:100%;list-style-type:none;"><a href="configEmail.jsp" title="ConfigEmail" onclick="gotomore('ConfigEmail')"><%=TranslaterHelper.getTranslatedInLocale("config_email", clocale)%></a></li>
                                  <%}
                                   if (talentId.get(i).equalsIgnoreCase("25") && talentEnable.get(i).equalsIgnoreCase("N")) {
                                 %>
                                <li style="height:30%;width:100%;list-style-type:none;"><a href="CollectedPlan.jsp" title="CollectedPlan"onclick="gotomore('CollectedPlan')"><%=TranslaterHelper.getTranslatedInLocale("master_collection", clocale)%></a></li>
                                  <%}
                                  if (talentId.get(i).equalsIgnoreCase("26") && talentEnable.get(i).equalsIgnoreCase("Y")) {
                                 %>
                                <li style="height:30%;width:100%;list-style-type:none;"><a href="UserDetails.jsp" title="UserDetails" onclick="gotomore('UserDetails')"><%=TranslaterHelper.getTranslatedInLocale("active_login_users", clocale)%></a></li>
                                  <%}
                                   if (talentId.get(i).equalsIgnoreCase("27") && talentEnable.get(i).equalsIgnoreCase("Y")) {
                                 %>
                                <li style="height:30%;width:100%;list-style-type:none;"><a href="RePublishFactAndDim.jsp" title="RePublishFactAndDim"  onclick="gotomore('RePublishFactAndDim')"><%=TranslaterHelper.getTranslatedInLocale("re_publish", clocale)%></a></li>
                                 <%}
                                  if (talentId.get(i).equalsIgnoreCase("28") && talentEnable.get(i).equalsIgnoreCase("Y")) {
                                 %>
                                <li  style="height:30%;width:100%;list-style-type:none;"> <a  href="javascript:void(0)" title="Group Measure" onclick="gotomore('G<roup Measure')" ><%=TranslaterHelper.getTranslatedInLocale("group_measure", clocale)%></a></li>
                                 <%}
                                     if (talentId.get(i).equalsIgnoreCase("29") && talentEnable.get(i).equalsIgnoreCase("Y")) {
                                 %>
                                 <li style="height:30%;width:100%;list-style-type:none;"><a href="ModifyCustomMeasures.jsp" title="ModifyCustomMeasures" onclick="gotomore('ModifyCustomMeasures')"><%=TranslaterHelper.getTranslatedInLocale("modify_custom_measures", clocale)%></a></li>
                                 <%}
                                }%>
                                    <li style="height:30%;width:100%;list-style-type:none;"><a href="globalParameters.jsp" title="GlobalParameters" onclick="gotomore('GlobalParameters')"><%=TranslaterHelper.getTranslatedInLocale("global_parameters", clocale)%></a></li>

                                 <% if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) { %>
                                 <li style="height:30%;width:100%;list-style-type:none;"><a href="javascript:void(0)" title="executeDimensionDrillScript" onclick="gotomore('executeDimensionDrillScript')"><%=TranslaterHelper.getTranslatedInLocale("dimension_drill_script", clocale)%></a></li>
                                 <% } %>

                                  <!--Added by mohit for drl enhancement,        *********ONLY FOR DRL*********           -->
<!--                                  <li><a href="BuyerGroup.jsp" title="Custom Settings" >Buyer Group</a></li>-->


                                 <%
                                 String userId=null;
                                if (session.getAttribute("USERID") != null) {
                                userId = (String) session.getAttribute("USERID");
                                        }
                                // String query = null;
                                 PbReturnObject retObj=null;
                                 //PbDb pbdb=new PbDb();
                                 //query = "select * from PRG_USER_ASSIGNMENTS where USER_ID="+userId;
                                 //retObj=dao.execSelectSQL(query);
                                 retObj=dao.selectprgUserAssingnment(userId);
                                %>
                                 <li style="height:30%;width:100%;list-style-type:none;"><a href="javascript:void(0)" title="UserManagement" onclick="gotomore('UserManagement')"><%=TranslaterHelper.getTranslatedInLocale("user_management", clocale)%></a></li>
                                 <%  if(retObj.getFieldValueString(0, 1).equalsIgnoreCase("PROGEN") || retObj.getFieldValueString(0, 19).equalsIgnoreCase("Y")){%>
                                         <li style="height:30%;width:100%;list-style-type:none;"><a href="javascript:void(0)" title="FeatureManagement" onclick="gotomore('FeatureManagement')"><%=TranslaterHelper.getTranslatedInLocale("feature_management", clocale)%></a></li>
                                 <% } %>
                                  <li style="height:30%;width:100%;list-style-type:none;"><a href="ExecuteQuery.jsp" title="ExecuteQuery" onclick="gotomore('ExecuteQuery')"><%=TranslaterHelper.getTranslatedInLocale("execute_query", clocale)%></a></li>
<!--                                 < added By Amar for Multiple Excel Export-->
                                  <li style="height:30%;width:100%;list-style-type:none;"><a href="UploadExcelfile.jsp" title="UploadExcelfile" onclick="gotomore('')"><%=TranslaterHelper.getTranslatedInLocale("upload_template_file", clocale)%></a></li>
                                  <li style="height:30%;width:100%;list-style-type:none;"><a href="ExportReportsIntoExcel.jsp" title="ExportReportsIntoExcel" onclick="//gotomore('ExportReportsIntoExcel2')"><%=TranslaterHelper.getTranslatedInLocale("reports_into_excelSheets", clocale)%></a></li>
<!--                                         //added by Mohit for user Registration-->
                                   <li style="height:30%;width:100%;list-style-type:none;"><a href="UsersRequest.jsp" title="UsersRequest" onclick="gotomore('UsersRequest')" ><%=TranslaterHelper.getTranslatedInLocale("users_request", clocale)%></a></li>
                                    <li style="height:30%;width:100%;list-style-type:none;"><a href="AddExcel.jsp" title="AddExcel" onclick="gotomore('AddExcel')" ><%=TranslaterHelper.getTranslatedInLocale("add_excel", clocale)%></a></li>
                                    <li style="height:30%;width:100%;list-style-type:none;"><a href="LoadData.jsp" title="LoadData" onclick="gotomore('LoadData')" ><%=TranslaterHelper.getTranslatedInLocale("load_data", clocale)%></a></li>
                                    <li style="height:30%;width:100%;list-style-type:none;"><a href="CreateHeaderAndFooterTemplate.jsp" title="Create Header Footer Logo" onclick="gotomore('HtmlHeaderFooter')" ><%=TranslaterHelper.getTranslatedInLocale("html_headerfooter", clocale)%></a></li>
                                    <li style="height:30%;width:100%;list-style-type:none;"><a href="CallingProcedure.jsp" title="Calling Procedure" onclick="" ><%=TranslaterHelper.getTranslatedInLocale("calling_procedure", clocale)%></a></li>
                                       </ul>
                                 </div>
                                  </div>
                                    </ul>
                                    <form action=""  id="roleFavForm" >
                <div id="RolesTab1"  style="width:99%;" align="center">
              <table border="1px solid "  align="center" width="100%"></table></div></form>
                      </div>
                     </center>
                </td>
             </tr>
           </tbody>
        </table>

 </form>
            <table style="width:100%">
                <tr>
                    <td valign="top" style="width:100%;">
                        <jsp:include page="Headerfolder/footerPage.jsp"/>
                </td>
            </tr>
        </table>
                 <script type="text/javascript">
            $(window).resize(function(){
                resizePage("pbBIManager");                               
            });
            $(document).ready(function(){
                resizePage("pbBIManager");                
                $("#breadCrumb").jBreadCrumb();

            });
             <%
                        if (container != null) {
                            if ((container.isExcelAdded == true)) {
            %>

                    alert('Your Excel Has been Added Successfully\n\
                       Please Click on Load Data Tab');
                    $("#loadingmetadata").hide();
            <%
                                container.isExcelAdded = false;
                            }
                        }
                        if (container1 != null) {
                            if ((container1.isLoadedData == true)) {
            %>
                    alert('Your Excel Data has been uploaded Successfully');
                    $("#loadingmetadata").hide();
            <%
                                container1.isLoadedData = false;
                            }

                        }
            %>
            $(function() {
                $("#tabs").tabs().find(".ui-tabs-nav").sortable({axis:'x'});


            });
            function goAdmin(path){
               document.forms.baseForm.action=path;
                document.forms.baseForm.submit();
            }
            function goUserManagement(path){
               document.forms.baseForm.action=path;
                document.forms.baseForm.submit();
            }
             function goFeatureManagement(path){
               document.forms.baseForm.action=path;
                document.forms.baseForm.submit();
            }
            function gotoDBCON(ctxPath){
                 if(!<%=isQDEnableforUser%>){
                        alert("You do not have the sufficient previlages")
                    }else{
                document.forms.baseForm.action=ctxPath+"/pbBase.jsp?pagename=Query Studio&curntpage=Database Connection#Database_Connection";
                document.forms.baseForm.submit();
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
            function executeDimensionDrillScript(){
            $.post('<%=request.getContextPath()%>/reportViewer.do?reportBy=executeDimensionScript&REPORTID',
            function(data){
            alert("Dimension Script Executed Successfully");
             });
             }
     <!--added by sruthi-->

//             function Morevalue()
//             {
//                 $("#extratabs").dialog({
//                    autoOpen: false,
//                     height: 415,
//                     width: 268,
//                    //position: 'justify',j
//                    modal: true,
//                    resizeble:true,
//                   title:"More..."
//
//
//                });
//
//                $("#extratabs").dialog('open');
////             $("#extratabs").show();
////             $("#extratabs").toggle();
//             }
//           function morelistclose()
//           {
//               $("#extratabs").hide();
//           }
           function morelistDisp(e) {
               e.stopPropagation();               
               $("#extratabs").css("overflow","auto");               
               $("#extratabs").slideToggle(function(){
                   $("#extratabs").css("overflow-x","hidden");
               });
           }
              function gotomore(tabname){   
                  setTimeout(function(){$(window).resize(); }, 1000);
                // $("#extratabs").dialog('close');
                 document.getElementById("extradivs").style.display='show';
                 document.getElementById("extratabs").style.display='none';
                  var gSrc = document.getElementById("extraframe");
                  if(tabname=='ETL') {
                       var changedpage = tabname;
                       document.getElementById("crntpage").style.display = "none";
                       document.getElementById("chngepage").style.display = "block";
                       document.getElementById("getname").value = tabname;
                       gSrc.src='dbQueries.jsp';
                      }

                 if(tabname=='ModifyMeasures'){
                          var changedpage = tabname;
                        document.getElementById("crntpage").style.display = "none";
                        document.getElementById("chngepage").style.display = "block";
                        document.getElementById("getname").value = tabname;
                         gSrc.src='modifyMeasures.jsp';
                     }
                  if(tabname=='modifyMembers')
                 gSrc.src='modifyMembers.jsp';
                  if(tabname=='AdminTab')
                    {
                     gSrc.src='AdminTab.jsp';
                     document.forms.baseForm.action=gSrc.src;
                     document.forms.baseForm.submit();
                    }
                    if(tabname=='Manage Schedule')
                    gSrc.src='editSchedulers.jsp';
                 if(tabname=='ModifyDispName')
                  gSrc.src='ModifyDispName.jsp';
              if(tabname=='ModifyDispName1')
                  gSrc.src='ModifyDispDimName.jsp';
                 if(tabname=='userSecurity')
                 gSrc.src='MultiTalentSecurity.jsp';
                 if(tabname=='multiSecurity')
                   gSrc.src='MultiSecurity.jsp';
                  if(tabname=='CalenderManagement')
                  gSrc.src='CalenderManagement.jsp';
                  if(tabname=='DataUpload')
                   gSrc.src='Uploadfile.jsp';
                  if(tabname=='UploadSSISData')
                   gSrc.src='uploadSSISData.jsp';
                  if(tabname=='DataDownload')
                   gSrc.src='dataDownload.jsp';
                if(tabname=='Generate Prior')
                    gSrc.src='GeneratePrior.jsp';
                if(tabname=='DeleteData')
                   gSrc.src='DeleteData.jsp';
                if(tabname=='FavouriteReportsAssignment')
                   gSrc.src='favouriteReportsAssignment.jsp';
               if(tabname=='businessPerformance')
                   gSrc.src='businessPerformance.jsp';
                if(tabname=='configEtl')
                   gSrc.src='ConfigEtl.jsp';
               if(tabname=='DefineRelatedMeasures')
                   gSrc.src='defineRelatedMeasures.jsp';
               if(tabname=='TargetMapping')
                   gSrc.src='pbTargetMapping.jsp';
               if(tabname=='PlanCollection')
                   gSrc.src='PlanCollection.jsp';
               if(tabname=='MonthlyPlanCollection')
                   gSrc.src='MonthlyPlanCollection.jsp';
               if(tabname=='ConfigEmail')
                   gSrc.src='configEmail.jsp';
               if(tabname=='PublishPlan')
                   gSrc.src='PlanPublish.jsp';
               if(tabname=='CollectedPlan')
                   gSrc.src='CollectedPlan.jsp';
               if(tabname=='UserDetails')
                   gSrc.src='UserDetails.jsp';
               if(tabname=='RePublishFactAndDim')
                   gSrc.src='RePublishFactAndDim.jsp';
               if(tabname=='PublishPlan')
                   gSrc.src='PlanPublish.jsp';
               if(tabname=='UsersRequest')
                   {
                   gSrc.src='UsersRequest.jsp';
                   }
               if(tabname=='Group Measure')
                   {
                        gSrc.src='srchQueryAction.do?srchParam=groupMeassure'
                      document.forms.searchForm.action=gSrc.src;
                      document.forms.searchForm.submit();
                   }
                   if(tabname=='Custom Settings')
                   {
                        gSrc.src='CustomSettings.jsp';

                   }
                   if(tabname=='UploadExcels')
                   {
                       alert("hello")
                        gSrc.src='UploadExcelIntoDB.jsp';

                   }
               if(tabname=='ModifyCustomMeasures')
                   gSrc.src='ModifyCustomMeasures.jsp';
               if(tabname=='GlobalParameters')
                   gSrc.src='globalParameters.jsp';
               if(tabname=='UserManagement')
                   {
                       gSrc.src='UserManagement.jsp';
                      document.forms.baseForm.action= gSrc.src;
                      document.forms.baseForm.submit();
                   }
               if(tabname=='FeatureManagement')
                   {
                        gSrc.src='FeatureManagement.jsp';
                       document.forms.baseForm.action=gSrc.src;
                       document.forms.baseForm.submit();
                   }
               if(tabname=='ExecuteQuery')
                   gSrc.src='ExecuteQuery.jsp';
               if(tabname=='executeDimensionDrillScript')
                   {
                      gSrc.src=$.post('<%=request.getContextPath()%>/reportViewer.do?reportBy=executeDimensionScript&REPORTID',
                      function(data){
                       alert("Dimension Script Executed Successfully");
                          });
                   }
              if(tabname=='UploadExcelfile')
                   {
                        gSrc.src='UploadExcelfile.jsp';
                       document.forms.baseForm.action=gSrc.src;
                       document.forms.baseForm.submit();
             }
               if(tabname=='ExportReportsIntoExcel')
                   {
                        gSrc.src='ExportReportsIntoExcel.jsp';
                       document.forms.baseForm.action=gSrc.src;
                       document.forms.baseForm.submit();
                   }
                   if(tabname=='LoadData')
                   {
                   gSrc.src='LoadData.jsp';
             }
             }

        <!--ended by sruthi-->
            </script>
    </body>
</html>

<%}%>