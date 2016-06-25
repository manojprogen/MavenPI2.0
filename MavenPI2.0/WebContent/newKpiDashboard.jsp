<%--
    Document   : newKpiDashboard
    Created on : 11 Feb, 2014, 2:35:26 PM
    Author     : progen
--%>

<%@page import="com.google.gson.Gson"%>
<%@page import="com.progen.db.ProgenDataSet"%>
<%@page import="prg.db.ContainerConstants"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.*"%>
<%@page import="org.apache.jasper.tagplugins.jstl.core.Set"%>
<%@page import="java.util.LinkedHashMap"%>
<%@page import="com.progen.reportview.bd.PbReportViewerBD"%>
<%@page import="prg.db.PbReturnObject"%>
<%@page import="com.progen.reportdesigner.db.ReportTemplateDAO"%>
<%@page import="utils.db.ProgenParam"%>
<%@page import="com.progen.action.UserStatusHelper"%>
<%@page import="com.progen.report.PbReportCollection"%>
<%@page import="prg.util.screenDimensions"%>
<%@page import="java.util.ArrayList"%>
<%@page import="prg.db.Container"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.google.gson.reflect.TypeToken"%>
<%@page import="com.progen.reportview.db.PbReportViewerDAO"%>
<%@page import="java.lang.reflect.Type"%>
<%@page import="prg.db.PbDb"%>
<%@page import="com.progen.report.display.DisplayParameters"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<%
                      String userid = String.valueOf(session.getAttribute("USERID"));
                    //  out.println(userid);
             ArrayList params = new ArrayList();
                    String roleid=(String)request.getAttribute("roleid");
                    String isKPIDashboard=(String)request.getAttribute("isKPIDashboard");
                    String istimedash=(String)request.getAttribute("istimedash");
                    
                    String fromDesignerkpi=(String)request.getAttribute("fromDesigner");
                    String[] listOfTimePeriods = {"Month till date","Quarter Till Date","Year till date"};
            String[] dependentTimePeriods = {"Current MTD","Prior MTD","change MTD","change% MTD","Prior YMTD","change PYMTD","change% PYMTD","Current QTD","Prior QTD","change QTD","change% QTD","Prior YQTD","change PYQTD","change% PYQTD","Current YTD","Prior YTD","change YTD","change% YTD"};
            String[] dependentTimePeriodsdisplay = {"MTD","PMTD","CPMTD","C%PMTD","PYMTD","CPYMTD","C%PYMTD","QTD","PQTD","CPQTD","C%PQTD","PYQTD","CPYQTD","C%PYQTD","YTD","PYTD","CPYTD","C%PYTD"};
                   String openevent = request.getParameter("action");
           // String[] dependentTimePeriodsdisplay = {"MTD","PMTD","CPMTD","C%PMTD","C%PYMTD","QTD","PQTD","CPQTD","C%PQTD","YTD","PYTD","C%PYTD"};
                    String bizRole = roleid;
                    String rolesid = roleid;

                    ArrayList<String> DisplayLabelskpi=new ArrayList();
     ArrayList<String> DisplayColumnskpi=new ArrayList();
     String measurevalintialkpi="";
     String val1kpi="";
    String valelementkpi="";

                     String prevREP = "";
                       HashMap paramMap = new HashMap();
                     String prevCEP = "";

                     StringBuilder result = new StringBuilder();
                     String autoDate = "";
                   String resetpath = "";
                      String[] vals1= null;
                       String ParamSectionDisplay = "";
                       boolean isPowerAnalyserEnableforUser=false;
                         String userType = null;
                      String result1="";

                       ArrayList<String> timeinfo=new ArrayList();
            String ReportId = "";
            ReportId = String.valueOf(request.getAttribute("ReportId"));
             PbReportCollection collect = new PbReportCollection();
            if(ReportId.equalsIgnoreCase("null")){
            ReportId=request.getAttribute("DashboardId").toString();

            }

           Container container = Container.getContainerFromSession(request, ReportId);
          
           DisplayLabelskpi=container.getDisplayLabels();
            DisplayColumnskpi=container.getDisplayColumns();
            PbReportViewerDAO dao=new PbReportViewerDAO();
            dao.setGlobalParameters(container);
           String Dashboardname="";
               String   reportDesc = "";
             String folderdetailsqquery = " select distinct FOLDER_ID from PRG_AR_REPORT_DETAILS  where REPORT_ID =" + ReportId;
                StringBuffer foldernames = new StringBuffer();
                String folderDetails = "";
                PbDb reportDetail = new PbDb();
                PbReturnObject retObj1 = new PbReturnObject();
 /*container.setcustomsetting(String.valueOf(session.getAttribute("settingvalue")));
                        String settingvalue=container.getcustomsetting();
                         String[] vals11= settingvalue.split(";");*/
String sytm=container.isYearCal;

                try {

                    retObj1 = reportDetail.execSelectSQL(folderdetailsqquery);
                    for (int j = 0; j < retObj1.getRowCount(); j++) {

                        foldernames = foldernames.append(retObj1.getFieldValueString(j, 0));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                folderDetails = foldernames.toString();
                if(folderDetails.isEmpty()){
                    folderDetails=" ";
                }
                if (isKPIDashboard =="true") {
HashMap map=new HashMap ();

                         Container containerNew=new Container();
                             if (session.getAttribute("PROGENTABLES") != null) {
                           map = (HashMap) session.getAttribute("PROGENTABLES");
                                 } else {
                           map = new HashMap();
                 }
            if (map.get(ReportId) != null) {
               containerNew = (Container) map.get(ReportId);
                             }

    boolean isQDEnableforUser=false;
  ServletContext context = getServletContext();
            HashMap<String,UserStatusHelper> statushelper;
            if(context.getAttribute("helperclass")!=null){
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
     }
                              request.setAttribute("roleid",containerNew.getFolderIdsForFact());
                               request.setAttribute("ReportId",ReportId);
Dashboardname=containerNew.getReportName();
PbReportViewerBD KPIDashboardBD=new PbReportViewerBD();
container.setContextPath(request.getContextPath());
if(roleid==null){
     PbDb pbdb = new PbDb();
            String rolesidqry = "select folder_id from prg_ar_report_details where report_id=" + ReportId;
            PbReturnObject roleobj = pbdb.execSelectSQL(rolesidqry);
           roleid = roleobj.getFieldValueString(0, 0);


}
        collect = container.getReportCollect();
         resetpath=collect.resetPath+"&isKPIDashboard=true";
        timeinfo = collect.timeDetailsArray;
        container.setIskpidasboard(true);
 paramMap = container.getParametersHashMap();
                    if (paramMap != null && paramMap.size() > 0) {

                       if(paramMap.get("Parameters")!=null){
                             params = (ArrayList) paramMap.get("Parameters");
                            }
                       if(params==null){
                        params=new ArrayList();
                    }
                       }
        String vals= " ";
        vals = timeinfo.toString();
        vals = vals.replace("[", "");
        vals = vals.replace("]", "");
        vals1 = vals.split(",");
        container.evaluateReportDateHeaders();
        if (container.getParamSectionDisplay() != null) {
                    ParamSectionDisplay = String.valueOf(container.getParamSectionDisplay());
                }
         ReportTemplateDAO rdao = new ReportTemplateDAO();
         if(rdao.getAutometicDate(ReportId)) {
        autoDate="checked";}
         String colAction = request.getParameter("sourceValue");
          String sort = request.getParameter("sort");
          Vector PresShownCols = new Vector();
                                    int topbottomCount;
                                    ArrayList<Integer> rowSequence;
                                    int topBottomRowCount;
          ArrayList<String> sortCols = null;
            boolean refreshGraph = true;
           String groupColumns=request.getParameter("groupColumn");
                                    ArrayList<String> sortColsubT=  null;
                                    char[] sortTypes = null;//ArrayList sortTypes = null;
                                    char[] sortDataTypes = null;
                                     PbReturnObject srchObj = null;
                                    Gson gson=new Gson();
                                    ProgenDataSet retObj = container.getRetObj();




                                    int rowCount;//getRowCount();
                                    int columnCount;
                                    String pagesPerSlide = container.getPagesPerSlide();

                                        sortCols = container.getSortColumns();
                                        sortTypes = container.getSortTypes();
                                            ArrayList<String> sortCols1 = new ArrayList();
                                            //start of code by govadhan
                                            char[] sortTypes1 =new char[1];


                                        topbottomCount = container.getTopBottomCount();
                                         if(container.isSubTotalTopBottomSet()){
                                         sortCols = container.getExplicitSortColumns();
                                        sortTypes = container.getExplicitSortTypes();


                                        topbottomCount = container.getSubTtlTopBottomCount();
                                         }

                                            if (sortCols != null && !sortCols.isEmpty() && (groupColumns==null || groupColumns.isEmpty())) //check if any previous sort is present retain them
                                        {
                                            sortCols = container.getSortColumns();


                  container.setIskpidasboard(true);


                                            sortCols = container.getSortColumns();
                                                sortTypes = container.getSortTypes();
                                                sortDataTypes = container.getSortDataTypes();

                                                rowSequence = retObj.sortDataSet(sortCols, sortTypes, sortDataTypes);//dataTypes, container.getOriginalColumns());

                                                retObj.setViewSequence(rowSequence);
                                                rowCount = rowSequence.size();

                                            refreshGraph = true;

                                            }


                                    result=KPIDashboardBD.DisplayKpiDashBoard(containerNew);
}
%>
<%
String piVersion ="piVersion2014";
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

        <script src="<%=request.getContextPath()%>/javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.tablesorter.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/tablesorter/addons/pager/jquery.tablesorter.pager.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.columnfilters.js"></script>
        <script src="<%=request.getContextPath()%>/javascript/treeview/jquery.cookie.js" type="text/javascript"></script>
        <script src="<%=request.getContextPath()%>/javascript/treeview/jquery.treeview.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/treeview/demo.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/ui.datepicker.js"></script>

        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/reportDesign.js"></script>

        <script src="javascript/jquery.contextMenu.js" type="text/javascript"></script>
        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/demos.css" rel="stylesheet" />
        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/pbReportViewerCSS.css" rel="stylesheet" />
        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/ui.all.css" rel="stylesheet" />
        <link rel="stylesheet" href="<%=request.getContextPath()%>/stylesheets/treeviewstyle/jquery.treeview.css" />
        <link rel="stylesheet" href="<%=request.getContextPath()%>/stylesheets/treeviewstyle/screen.css" />
        <link rel="stylesheet" href="<%=request.getContextPath()%>/stylesheets/jq.css" type="text/css" media="print, projection, screen">
        <link rel="stylesheet" href="<%=request.getContextPath()%>/stylesheets/style.css" type="text/css" media="print, projection, screen">
        <link href="<%=request.getContextPath()%>/stylesheets/javascript.css" type="text/css" rel="stylesheet">
        <link href="<%=request.getContextPath()%>/stylesheets/html.css" type="text/css" rel="stylesheet">
        <link href="<%=request.getContextPath()%>/stylesheets/css.css" type="text/css" rel="stylesheet">
        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/metadataButton.css" rel="stylesheet" />
        <link href="stylesheets/jquery.contextMenu.css" rel="stylesheet" type="text/css" />
        <link href="<%=request.getContextPath()%>/reportDesigner.css" type="text/css" rel="stylesheet">
         <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/stylesheets/tablesorterStyle.css" />
        <script type="text/javascript" src="<%=request.getContextPath()%>/jQuery/jquery/ui/jquery.jBreadCrumb.1.1.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/jQuery/jquery/ui/jquery.easing.1.3.js"></script>
        <link type="text/css" href="<%=request.getContextPath()%>/jQuery/jquery/themes/base/BreadCrumb.css" rel="stylesheet" />
        <link type="text/css" href="<%=request.getContextPath()%>/jQuery/jquery/themes/base/Base.css" rel="stylesheet" />
        <link href="stylesheets/jquery.contextMenu.css" rel="stylesheet" type="text/css" />
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/toolTip.js"></script>
        <link rel="stylesheet" href="<%=request.getContextPath()%>/stylesheets/toolTip.css" type="text/css" />
         <script type="text/javascript" src="<%=request.getContextPath()%>/jQuery/farbtastic12/farbtastic/farbtastic.js"></script>
          <link rel="stylesheet" href="<%=request.getContextPath()%>/jQuery/farbtastic12/farbtastic/farbtastic.css" type="text/css" />
        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/themes/blue/TableCss.css" rel="stylesheet" />

        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/pbReportViewerJS.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/reportviewer/ReportViewer.js"></script>
       <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/dashboardDesign.js"></script>

        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/pbreporttemplateframejs.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/TableDisplay/JS/pbTableMapJS.js"></script>
         <script  type="text/javascript" src="<%=request.getContextPath()%>/javascript/pbReportViewerJS.js" ></script>
         <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/pi.js"></script>
          <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/quicksearch.js"></script>
            <script type="text/javascript" src="<%= request.getContextPath()%>//dragAndDropTable.js"></script>
            <link type="text/css" href="<%=request.getContextPath()%>/datedesign.css" rel="stylesheet"/>

            <script type="text/javascript">
            var drillElmntId = "";
            var drillElmntName = "";
            var drillReportId = "";
            var kpiMasterIddrill = "";
            var selectedelementId=""
            var  isKPIDashboard1=""
 isKPIDashboard1= <%=isKPIDashboard%>

    $(document).ready(function(){
     $("#kpisDialog").dialog({
                        autoOpen: false,
                        height: 360,
                        width: 600,
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
          $("#editViewByDiv").dialog({
            autoOpen: false,
            height: 400,
            width: 520,
            position: 'justify',
            modal: true
        });

         $("#measuresDialog").dialog({
            autoOpen: false,
            height:400,
            width: 720,
            position: 'justify',
            modal: true
        });
        $("#timePeriodDialog").dialog({
            autoOpen: false,
            height:400,
            width: 273,
            position: 'justify',
            modal: true
     });
       });
      $(function() {
           <%if (isKPIDashboard =="true") {%>
                <% if(timeinfo.get(1).equalsIgnoreCase("PRG_DATE_RANGE")&& sytm.equalsIgnoreCase("No")) { %>
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
                       var b;
                     b=($("#todate").val());
                    var dateArr=new Array()
                    dateArr=b.split(",");
                     if(b!=""){
                    $("#tdfield1").html(dateArr[2]+"'"+dateArr[3].substring(2))
                    $("#tdfield2").html(dateArr[1])
                    $("#tdfield3").html(dateArr[0])
                }
                //added by Mayank.
                tdate=b;
                tdate1 = new Date(tdate).getTime();
                var d =($("#fromdate").val());
                fdate=d;
                fdate1 = new Date(fdate).getTime();
                       if(tdate1<fdate1){
                        alert("Wrong Date Selected");
                }

                //end of code.
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
                       var b;
                     b=($("#compareto").val());

var dateArr=new Array()
                    dateArr=b.split(",");
                     if(b!=""){
                    $("#ctfield1").html(dateArr[2]+"'"+dateArr[3].substring(2))
                    $("#ctfield2").html(dateArr[1])
                    $("#ctfield3").html(dateArr[0])
                }
                //added by Mayank.
                 ctdate=b;

                ctdate1 = new Date(ctdate).getTime();
                var e =($("#comparefrom").val());
                cfdate=e;

                cfdate1 = new Date(cfdate).getTime();
                       if(ctdate1<cfdate1){
                        alert("Wrong Date Selected");
                }
                            //end of code.
                }

            }).datepicker("setDate", new Date(('<%=vals1[5]%>')) );
           <%} else if(timeinfo.get(1).equalsIgnoreCase("PRG_STD") && sytm.equalsIgnoreCase("No")){%>

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
               <%}}%>
        });



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
 <%if (isKPIDashboard =="true") {%>
                              <% if(timeinfo.get(1).equalsIgnoreCase("PRG_DATE_RANGE")) { %>

                                       <% ReportTemplateDAO dao1 = new ReportTemplateDAO();
        String isopen=request.getParameter("action");
        ArrayList dateList=new ArrayList();
         if(dao1.getAutometicDate(ReportId)) {%>
        if(id=='fromdate'){

        <%dateList=dao1.getDefaultDate(ReportId,isopen);
        if(!dateList.isEmpty()){%>

 var a;
 a=($("#fromdate").val());
 var dateArr=new Array()
 dateArr=a.split(",");
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
                              <% }else{%>
                                  $("#field1").html(dateArr[2]+"'"+dateArr[3].substring(2))
                    $("#field2").html(dateArr[1])
                    $("#field3").html(dateArr[0])
                               <% }
                                %>
                    }
                    <%}else{%>
                    $("#field1").html(dateArr[2]+"'"+dateArr[3].substring(2))
                    $("#field2").html(dateArr[1])
                    $("#field3").html(dateArr[0])
                                  <% } %>

                              <%}else{%>
                                     $('#datepicker').val(perioddate);
                                  <%}%>
                                        <%}%>
     }
             });

     }


  function MTDchecked(ID,count){
//alert("ssasaa")
var dependentTimePeriodsdisplay = ["MTD","PMTD","CPMTD","C%PMTD","PYMTD","CPYMTD","C%PYMTD","QTD","PQTD","CPQTD","C%PQTD","PYQTD","CPYQTD","C%PYQTD","YTD","PYTD","CPYTD","C%PYTD"];

if(document.getElementById(ID).checked==true)
                    {if(count==0){
                            for(var i=0; i< 7; i++){

                        document.getElementById(dependentTimePeriodsdisplay[i]).checked=true;
                    }
                    }else if(count==7){
                        for(var i=count; i< 14; i++){

                        document.getElementById(dependentTimePeriodsdisplay[i]).checked=true;
                    }
                    }else{
                        for(var i=count; i< 18; i++){

                        document.getElementById(dependentTimePeriodsdisplay[i]).checked=true;
                    }
                    }

					}else if(document.getElementById(ID).checked==false){
                                            if(count==0){
                            for(var i=0; i< 7; i++){

                        document.getElementById(dependentTimePeriodsdisplay[i]).checked=false;
                    }
                    }else if(count==7){
                        for(var i=count; i< 14; i++){

                        document.getElementById(dependentTimePeriodsdisplay[i]).checked=false;
                    }
                    }else{
                        for(var i=count; i< 18; i++){

                        document.getElementById(dependentTimePeriodsdisplay[i]).checked=false;
                    }
                    }
                                        }
}
function checkSelectedPeriods(){
 var selectedList="";
 var measures = document.getElementById("Measures").value;

                var listOfTimePeriods = ["Month till date","Quarter Till Date","Year till date"];
		var dependentTimePeriodsdisplay = ["MTD","PMTD","CPMTD","C%PMTD","PYMTD","CPYMTD","C%PYMTD","QTD","PQTD","CPQTD","C%PQTD","PYQTD","CPYQTD","C%PYQTD","YTD","PYTD","CPYTD","C%PYTD"];
                var chk = 0;


					for(var i=0; i < dependentTimePeriodsdisplay.length ;i++){
					if(document.getElementById(dependentTimePeriodsdisplay[i]).checked==true)
                    {
                        selectedList = selectedList+dependentTimePeriodsdisplay[i]+",";
                        chk=1;

                    }
					}

                selectedList = selectedList.substring(0,selectedList.length-1);
 parent.$("#timePeriodDialog").dialog('close');
$.ajax({
                        url: 'reportTemplateAction.do?templateParam=buildTable&buildTableChange=Measures&istimedashboard=true&timeDetails='+encodeURIComponent(selectedList)+'&Msrs='+measures+'&MsrsNames='+measures+'&REPORTID=<%=ReportId%>',
                        success: function(data) {
                            parent.PreviewTableTD();
                            if(data!=""){
                            }
                        }
                    });
}

function displayTimeDB(measures,timeperiods,isTimeDbrd){
    var divIdObj=document.getElementById("TimeDashboardDiv");

   // divIdObj.innerHTML='<center><img id="imgId" src="images/ajax.gif" align="middle"  width="75px" height="75px"  style="position:absolute" ></center>';
    $.ajax({
        url: 'reportViewer.do?reportBy=DisplayTimeBasedDashBoard&reportId='+<%=ReportId%>+'&measures='+measures+'&timeDetails='+encodeURIComponent(timeperiods)+'&isTimeDbrd='+isTimeDbrd,
        success: function(data){
            divIdObj.innerHTML =data;
        }
    });
}

function kpirounding(columnName,disColumnName,REPORTID,precision){


     $.post("<%=request.getContextPath()%>/reportViewer.do?reportBy=kpitableChanges&tableChange=tableProperties&sourceValue=rounding&source=S&columnName="+columnName+"&precision="+precision+"&disColumnName="+disColumnName+"&tabId="+REPORTID,
                function(data){
                   $("#KpiDashboardDiv").html(data);
                });
}
function numberformat(colname,dispColName,REPORTID,ctxPath,Ref_element,numberformat){

  $.ajax({
                   url: '<%=request.getContextPath()%>/reportViewer.do?reportBy=updatenumbrformat&tableChange=columnProperties&msrname='+colname+'&REPORTID='+REPORTID+'&Nfrmt='+numberformat+'&dispColName='+dispColName+'&Ref_element='+Ref_element,
                   success: function(data){
                   document.forms.submitReportForm.action = "reportViewer.do?reportBy=viewReport&action=measChange&REPORTID="+document.forms.submitReportForm.REPORTID.value+"&isKPIDashboard=true";
                   document.forms.submitReportForm.submit();
                }
      });
}
//function updategttype(colname,dispColName,REPORTID,ctxPath,refelement,gttype){
//
//  $.ajax({
//                   url: '<%=request.getContextPath()%>/reportViewer.do?reportBy=updategttype&tableChange=columnProperties&msrname='+colname+'&REPORTID='+REPORTID+'&Aggregation='+gttype+'&Refement='+refelement+'&dispColName='+dispColName,
//                   success: function(data){
//                   document.forms.submitReportForm.action = "reportViewer.do?reportBy=viewReport&action=measChange&REPORTID="+document.forms.submitReportForm.REPORTID.value+"&isKPIDashboard=true";
//                   document.forms.submitReportForm.submit();
//                }
//      });
//}
function submitEditTablePro(){
$("#editDBTable").dialog('close');
    var isGtReq=document.getElementById("GrandTotalReq").value;
    var isStReq=document.getElementById("SubTotalReqForkpi").value;
//    alert(refelement);

  $.ajax({
                   url: '<%=request.getContextPath()%>/reportViewer.do?reportBy=editDbTableProperties&REPORTID='+<%=ReportId%>+'&isGtReq='+isGtReq+'&isStReq='+isStReq,
                   success: function(data){
                   document.forms.submitReportForm.action = "reportViewer.do?reportBy=viewReport&action=measChange&REPORTID="+document.forms.submitReportForm.REPORTID.value+"&isKPIDashboard=true";
                   document.forms.submitReportForm.submit();
                }
      });
}
function updategttype2(){
$("#getGTvalues").dialog('close');
var gttype=document.getElementById("AggregationType").value;
    var colname=document.getElementById("colname").value;
    var refelement=document.getElementById("refelement").value;
    var dispColName=document.getElementById("dispColName").value;
    var isGtReq=document.getElementById("GrandTotalReq").value;
//    alert(refelement);

  $.ajax({
                   url: '<%=request.getContextPath()%>/reportViewer.do?reportBy=updategttype&tableChange=columnProperties&msrname='+colname+'&REPORTID='+<%=ReportId%>+'&Aggregation='+gttype+'&Refement='+refelement+'&dispColName='+dispColName+'&isGtReq='+isGtReq,
                   success: function(data){
                   document.forms.submitReportForm.action = "reportViewer.do?reportBy=viewReport&action=measChange&REPORTID="+document.forms.submitReportForm.REPORTID.value+"&isKPIDashboard=true";
                   document.forms.submitReportForm.submit();
                }
      });
}
function requiredValue(value,msrId){
 $.post('<%=request.getContextPath()%>/reportViewer.do?reportBy=kpitableChanges&tableChange=tableProperties&msrname='+msrId+'&tabId='+<%=ReportId%>+'&value='+value,
                function(data){
//                   $("#KpiDashboardDiv").html(data);
      });
}
//kpi sorting
function sortkpi(colname,dispColName,REPORTID,ctxPath,refelement){
$("#applaySorting").dialog({
            bgiframe: true,
            autoOpen: false,

            position: 'justify',
            modal: true,
            title:'Sort'
    });
     document.getElementById("dispColName").value=dispColName;
    document.getElementById("colname").value=colname;
    document.getElementById("ctxPath").value=ctxPath;
    document.getElementById("refelement").value=refelement;
  $("#applaySorting").dialog('open');
}
function submitSortOrder(){
$("#applaySorting").dialog('close');
var gttype=document.getElementById("AggregationType").value;
    var colname=document.getElementById("colname").value;
    var refelement=document.getElementById("refelement").value;
    var dispColName=document.getElementById("dispColName").value;
//    var isGtReq=document.getElementById("GrandTotalReq").value;
//    alert(refelement);

  $.ajax({
                   url: '<%=request.getContextPath()%>/reportViewer.do?reportBy=updategttype&tableChange=columnProperties&msrname='+colname+'&REPORTID='+<%=ReportId%>+'&Aggregation='+gttype+'&Refement='+refelement+'&dispColName='+dispColName+'&isGtReq='+isGtReq,
                   success: function(data){
                   document.forms.submitReportForm.action = "reportViewer.do?reportBy=viewReport&action=measChange&REPORTID="+document.forms.submitReportForm.REPORTID.value+"&isKPIDashboard=true";
                   document.forms.submitReportForm.submit();
                }
      });
}
function updategttype1(colname,dispColName,REPORTID,ctxPath,refelement){
$("#getGTvalues").dialog({
            bgiframe: true,
            autoOpen: false,

            position: 'justify',
            modal: true,
            title:'GT properties'
    });
     document.getElementById("dispColName").value=dispColName;
    document.getElementById("colname").value=colname;
    document.getElementById("ctxPath").value=ctxPath;
    document.getElementById("refelement").value=refelement;
  $("#getGTvalues").dialog('open');
}

function dispGrandTotalForkpi(){
                var GrandTotalReqObj=document.getElementById("GrandTotalReq");
                if(GrandTotalReqObj.checked){
                    GrandTotalReqObj.value="true";
                }else{
                    GrandTotalReqObj.value="false";
                }
            }
            function dispSubTotalForkpi(){
                var SubTotalReqObj=document.getElementById("SubTotalReqForkpi");
                if(SubTotalReqObj.checked){
                    SubTotalReqObj.value="true";
                }else{
                    SubTotalReqObj.value="false";
                }
            }
   // end of code by bhargavi
function enableComparision(msrId,isdisable){

          $.ajax({
                   url: '<%=request.getContextPath()%>/reportViewer.do?reportBy=enableComparisionInKpiDash&REPORTID='+<%=ReportId%>+'&CmprenableMsr='+msrId+'&isdisable='+isdisable,
                   success: function(data){
                   document.forms.submitReportForm.action = "reportViewer.do?reportBy=viewReport&action=measChange&REPORTID="+document.forms.submitReportForm.REPORTID.value+"&isKPIDashboard=true";
                   document.forms.submitReportForm.submit();
                }
      });
}
 function getenablevalues(msrdata,priorvalue,msryear,prioryear){

     $("#getenblevalues").dialog({
            autoOpen: false,
            height: 130,
            width: 264,
            position: 'justify',
            title:'Comparison',
            modal: true
        });
        var html="";
                       html+="<table width='100%'>";

                         // html+="<tr><td width='50%'>"+msryear+":</td>";
                          html+="<tr><td width='50%'>Current Period:</td>";
                          html+="<td width='50%'><input type='text'  value="+msrdata+" readonly></td></tr><br>";

                       html+="<tr><td width='50%'>Previous Period:</td>";
                        html+="<td width='50%'><input type='text'  value="+priorvalue+" readonly></td></tr></table>";

         $("#getenblevalues").dialog('open');
          $("#getenblevalues").html(html);
}
function editformat(colname,dispColName,REPORTID,ctxPath,refelement,fontsize,textalign,fontweight){
$("#getformattingstyles").dialog({
            autoOpen: false,
            height: 200,
            width: 264,
            position: 'justify',
            title:'Edit Format',
            modal: true
        });
var fontsizes=new Array();
var textaligns=new Array();
var fontweights=new Array();
fontsizes.push("Normal","Small","Large");
textaligns.push("Center","Left","Right");
fontweights.push("Normal","Bold");
    document.getElementById("colname").value=colname;
    document.getElementById("ctxPath").value=ctxPath;
     var htmlVar="";
    for(var i=0; i<fontsizes.length;i++){

        if(fontsize==fontsizes[i]){
             htmlVar +=  "<option selected value='"+fontsize+"'>"+fontsize+"</option>";
        }else{
             htmlVar +=  "<option  value='"+fontsizes[i]+"'>"+fontsizes[i]+"</option>";
        }
    }
     var htmlVar1="";
    for(var i=0; i<textaligns.length;i++){

        if(textalign==textaligns[i]){
             htmlVar1 +=  "<option selected value='"+textalign+"'>"+textalign+"</option>";
        }else{
             htmlVar1 +=  "<option  value='"+textaligns[i]+"'>"+textaligns[i]+"</option>";
        }
    }
     var htmlVar12="";
    for(var i=0; i<fontweights.length;i++){

        if(fontweight==fontweights[i]){
             htmlVar12 +=  "<option selected value='"+fontweight+"'>"+fontweight+"</option>";
        }else{
             htmlVar12 +=  "<option  value='"+fontweights[i]+"'>"+fontweights[i]+"</option>";
        }
    }
    $("#FontsizeType").html(htmlVar);
    $("#FontstyleType").html(htmlVar12);
    $("#Textalignment").html(htmlVar1);
        $("#getformattingstyles").dialog('open');
}
function formatsubmit(){

   $("#getformattingstyles").dialog('close');
       var fontsize=document.getElementById("FontsizeType").value;
       var fontstyle=document.getElementById("FontstyleType").value;
       var textalign=document.getElementById("Textalignment").value;
       var colname=document.getElementById("colname").value;


 $.post('<%=request.getContextPath()%>/reportViewer.do?reportBy=kpitableChanges&tableChange=tableProperties&msrname='+colname+'&tabId='+<%=ReportId%>+'&fontsize='+fontsize+'&fontstyle='+fontstyle+'&textalign='+textalign+'&isformatchanged=true',
                function(data){
                   $("#KpiDashboardDiv").html(data);
      });


}
function rowcoloring(colname,dispColName,REPORTID,ctxPath,refelement){
    $('#colorpicker1').farbtastic('#color1');
    $("#REPORTID").val(REPORTID);
    $("#COLUMNID").val(colname);
    $("#colorsDiv1").dialog({
                            bgiframe: true,
                            autoOpen: false,
                            height:300,
                            width: 300,
                            modal: true,
                            Cancel: function() {
                                $(this).dialog('close');
                            }
                    });
     $("#colorsDiv1").dialog('open');

}
function saveSelectedColor1()
          {
              var colorCode=$("#color1").val();
               var colname= $("#COLUMNID").val();
                var REPORTID= $("#REPORTID").val();
             $("#colorsDiv1").dialog('close');
             $.post('<%=request.getContextPath()%>/reportViewer.do?reportBy=kpitableChanges&tableChange=tableProperties&msrname='+colname+'&tabId='+REPORTID+'&colorCode='+encodeURIComponent(colorCode)+'&iscolorapplied=true',
                function(data){
                   $("#KpiDashboardDiv").html(data);
      });
          }
  function resetrowcolor(colname,dispColName,REPORTID,ctxPath,refelement)
  {
       var colorCode="#12345"
$.post('<%=request.getContextPath()%>/reportViewer.do?reportBy=kpitableChanges&tableChange=tableProperties&msrname='+colname+'&tabId='+REPORTID+'&colorCode='+encodeURIComponent(colorCode)+'&iscolorapplied=true',
                function(data){
                   $("#KpiDashboardDiv").html(data);
      });
  }
function toggleYearVisibility() {
       var e = document.getElementById('toggleYear1');
       var e1 = document.getElementById('toggleYear');
       if(e.style.display == 'block'){
          e.style.display = 'none';
          e1.style.display = 'block'}
       else{
          e.style.display = 'block';
          e1.style.display = 'none';
      }
}
 function properties1(eachRow){
  var excludeIconId = "kpi_properties"+eachRow;
                 $('#'+excludeIconId).show();
            }
             function properties2(eachRow){
                 var excludeIconId = "kpi_properties"+eachRow;
                $('#'+excludeIconId).hide();
            }
            function saveYearDetails(calname){
                if(calname=="Select-Year")
                {alert("Please Chosse Correct Year"); }
                else{
                $('#datetext').val('topdate');
                var calyear;var dateArr=new Array();var frmDate;var toDate;var dateArr1=new Array();
                if(calname=="perioddate")
                {
                calyear = $('#calPeriodYear option:selected').val()
                calyear="Mon,1,Jan,".concat(calyear)
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
                 if(calyear!=""){$("#field1").html(dateArr[3])}

               }
                if(calname=="todate")
                {
                calyear = $('#calToYear option:selected').val()
                calyear="Mon,31,Dec,".concat(calyear)
               dateArr=calyear.split(",");
                $('#todate').val(calyear);
                if(new Date($('#todate').val()).getTime()<new Date($('#fromdate').val()).getTime()){
                        alert("Wrong Date Selected");
                }
                 if(calyear!=""){$("#tdfield1").html(dateArr[3])}

               }
                if(calname=="comparefrom")
                {
                calyear = $('#calCmpFromYear option:selected').val()
                calyear="Mon,01,Jan,".concat(calyear)
               dateArr=calyear.split(",");
                $('#comparefrom').val(calyear);
                 if(calyear!=""){$("#cffield1").html(dateArr[3])}

               }
               if(calname=="compareto")
                {
                calyear = $('#calCmpToYear option:selected').val()
                calyear="Mon,31,Dec,".concat(calyear)
               dateArr=calyear.split(",");
                $('#compareto').val(calyear);
                 if(new Date($('#compareto').val()).getTime()<new Date($('#comparefrom').val()).getTime()){
                        alert("Wrong Date Selected");
                }

                 if(calyear!=""){$("#ctfield1").html(dateArr[3])}

               }
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
                        alert("Wrong Date Selected");
                }
                 if(calyear!=""){$("#tdfield1").html(dateArr[3])}

               }

               if(calname=="comparefromcompareto")
                {
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
if(calyear!=""){$("#ctfield1").html(dateArr[3])}
                }
           }
       }


    function goPaths(path){
                parent.closeStart();
                document.forms.submitReportForm.action=path;
                document.forms.submitReportForm.submit();
            }
            function EditKpi(){
             var frameObj=document.getElementById("dataDispmem");
                    var source="dashboardTemplateViewerAction.do?templateParam2=getKpis&foldersIds="+<%=roleid%>+'&divId='+divId.id+'&kpiType='+type+'&dashboardId='+<%=ReportId%>;
                    frameObj.src=source;
                    $("#kpisDialog").dialog('option','title','Edit KPI')
                    $("#kpisDialog").dialog('open');
                }

         function AddMoreDims1(){


              var roleId =<%=roleid%> ;
              var DshbrdId=<%=ReportId%>;


                    var frameObj=document.getElementById("addmoreParamFrame");
                    var source="reportTemplateAction.do?templateParam=addMoreDimensions&foldersIds="+roleId+"&IsrepAdhoc=true&REPORTID="+DshbrdId+"&isnewkpidashboard=true";
                    frameObj.src=source;
                    $("#AddMoreParamsDiv").dialog('open');
                }

     function showKpisforKpiDashboard(dshbrdID,roleId){


    document.getElementById('tableList').checked=false;
    $("#tabListDiv").show();
            $("#tablistLink").show();
            $("#goButton").show();

    var frameObj=document.getElementById("kpidataDispmem");
    var source="dashboardTemplateAction.do?templateParam2=getKpis&foldersIds="+roleId+'&kpiType=newKpiDashboard&dashboardId='+dshbrdID;
    frameObj.src=source;
    $("#kpisDialog").dialog('option','title','Edit KPI')
    $("#kpisDialog").dialog('open');
}
function includeParameter(img,elementId,id1, id2, id3,id4)
{
    var excludeIconId = elementId+"-excimg";
    parent.$('#'+excludeIconId).hide();
    parent.changeImage(img,elementId,id1,id2,id3,id4);
    excludeBox = false;

}

 function submiturlsinNewTab1($ch){
//     alert($ch);
$('#datetext').val('topdate');
              document.submitReportForm.action = $ch;
              document.submitReportForm.target = "_blank";
              document.submitReportForm.submit();
              document.submitReportForm.target = "";
            }
function openImgDiv(resetPath){
                         parent.document.getElementById('loading').style.display='';

            }
function saveKPIDashboard(){
     parent.document.getElementById('loading').style.display='';
                  var repId=document.getElementById("REPORTID").value;
                var repName=document.getElementById("Dashboardname").value;

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

function dispChangekpiTableColumns(ctxPath,bizRoles,reportId){
            $.ajax({
        url:ctxPath+'/reportViewer.do?reportBy=loadDialogs&loadDialogs=true&currentReportId='+reportId+'&currentBizRoles='+bizRoles,
        success:function(data) {
            $("#tableColsDialog").dialog('open');
         document.getElementById("tableColsFrame").src="TableDisplay/PbChangeTableColumnsRT.jsp?loadDialogs=true&currentReportId="+reportId+"&currentBizRoles="+bizRoles+"&from=true";

        }
            });
    }

 function createReport(){

                       var REPORTID=document.getElementById('REPORTID').value;
                        var graphTableHidden ="";

                        $.ajax({
                            url: 'reportTemplateAction.do?templateParam=checkRportNGraph&REPORTID='+REPORTID,
                            success: function(data){

                                if(data=='Please Select Table'){
                                    alert(data)
                                    return false;
                                }else if(data=='Please Select Graph'){
                                    alert(data)
                                    return false;
                                }else if(data=='Please Select Graph Columns To Add Graph'){
                                    alert(data)
                                }else if(data=='Please Select Table and Graph'){
                                    alert(data);
                                    return false;

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
                                          document.forms.submitReportForm.action = "reportTemplateAction.do?templateParam=saveReport&isTimeDashboard=true&isKpiDashboard=true&action=open&graphTableHidden="+graphTableHidden+"&REPORTID="+REPORTID;

                                           document.forms.submitReportForm.submit();

                                }
                            }
                        });

                    }
          function datetogglkpi(userType,isPAEnableforUser){
   var ctxPath=document.getElementById("h").value;
   var reportId= document.forms.submitReportForm.REPORTID.value;
//    if(userType=="Admin" || isPAEnableforUser=="true"){
            $.post(
                  ctxPath+'/reportViewer.do?reportBy=dateToggle&REPORTID='+reportId,
                   function(data){
                    if(data=='Success'){
                          document.forms.submitReportForm.action=ctxPath+"/reportViewer.do?reportBy=viewReport&REPORTID="+reportId+"&isToggle=Y&isKPIDashboard=true";
                          document.forms.submitReportForm.submit();
                    }

                });

}
var globalval;
function submitformkpi(){

    parent.document.getElementById('loading').style.display='';
    document.forms.submitReportForm.action = "reportViewer.do?reportBy=viewReport&action=paramChange&REPORTID="+document.forms.submitReportForm.REPORTID.value+"&isKPIDashboard=true&globalkpifilter="+globalval;
    document.forms.submitReportForm.submit();
}
function submitform1(){

    parent.document.getElementById('loading').style.display='';
    document.forms.submitReportForm.action = "reportViewer.do?reportBy=viewReport&action=paramChange&REPORTID="+document.forms.submitReportForm.REPORTID.value+"&isKPIDashboard=true&globalkpifilter=true";
    document.forms.submitReportForm.submit();
}
function submitFormMeasChangekpi(){
     parent.document.getElementById('loading').style.display='';
    document.forms.submitReportForm.action = "reportViewer.do?reportBy=viewReport&action=measChange&REPORTID="+document.forms.submitReportForm.REPORTID.value+"&isKPIDashboard=true";
    document.forms.submitReportForm.submit();
}
 function closeparamsTab()
            {
                parent.$( "#tabParameters1" ).hide();
            }
function displayKPIDashboard(dashletId,dashBoardId,refReportId,graphId,kpiMasterId,dispSequence,dispType,dashletName,fromDesigner,editDbrd){
    var kpiType=document.getElementById("kpiType");
    var divIdObj=document.getElementById("Dashlets");
    if (divIdObj == null){
        divIdObj = document.getElementById(dashletId);
        if (document.getElementById("hideDiv") != null){
            var  hideDiv=parent.document.getElementById("hideDiv").value;
            document.getElementById(hideDiv).style.display='bldisplayock';
        }
    }
    $.ajax({
        url: 'dashboardViewer.do?reportBy=displayDataforKpiDashboard&dashletId='+dashletId+'&dashBoardId='+dashBoardId+'&kpiMasterId='+kpiMasterId+'&kpiDrill=Y&fromDesigner='+fromDesigner+'&editDbrd='+editDbrd,
        success: function(data){
             document.getElementById("handsonTableId").style.display="";
             var source = '<%=request.getContextPath()%>/DashboardHOT.jsp?kpidashid='+dashletId+"&dashBoardId="+dashBoardId+"&kpiMasterId="+kpiMasterId+"&kpiType="+kpiType;
             var dSrc = document.getElementById("handsonTableFrame");
               dSrc.src = source;

        }
    });
}



function selectKpiHeaders(dashBoardId){

          $("#getKPiHeadersDiv").dialog({
            autoOpen: false,
            height: 350,
            width: 450,
            position: 'justify',
            modal: true,
            resizable:true
        });

                    var frameObj=document.getElementById("getKPiHeadersFrame");
                    var source='dashboardViewer.do?reportBy=getKpiHeaders&dashletId='+0+'&dashBoardId='+dashBoardId;
                    frameObj.src=source;
                    $("#getKPiHeadersDiv").dialog('open');

}

//     function changeColor(index,kpiMasterId,PbReportId,dashBoardId){
//            $("#index").val(index);
//           $("#kpiMasterId").val(kpiMasterId);
//           $("#PbReportId").val(PbReportId);
//           $("#dashBoardId").val(dashBoardId);
//            $('#colorpicker').farbtastic('#color');
//            $("#colorsDiv").dialog({
//                            bgiframe: true,
//                            autoOpen: false,
//                            height:300,
//                            width: 300,
//                            modal: true,
//                            Cancel: function() {
//                                $(this).dialog('close');
//                            }
//
//                    });
//           $("#colorsDiv").dialog('open');
//           }
//            function cancelColor(){
//
//             $("#colorsDiv").dialog('close');
//
//            }

           function saveSelectedColor()
          {
              var colorCode=$("#color").val();

             var index= $("#index").val();
             var kpiMasterId= $("#kpiMasterId").val();
              var PbReportId= $("#PbReportId").val();
               var dashBoardId= $("#dashBoardId").val();
             $("#colorsDiv").dialog('close');
             $.ajax({
              url:'<%=request.getContextPath()%>/handsontableaction.do?handsonParam=removeHandsontableSession',
              success:function(){
           var source = '<%=request.getContextPath()%>/DashboardHOT.jsp?kpidashid='+PbReportId+"&dashBoardId="+dashBoardId+"&kpiMasterId="+kpiMasterId+"&rowindex="+index+"&colorCode="+encodeURIComponent(colorCode);
           var dSrc = document.getElementById("handsonTableFrame");

               dSrc.src = source;
              }
            })
          }
//         function tabparamListDisp(globalval1){
//                    globalval=globalval1;
//                  var divIdObj=document.getElementById("tabParameters1");
//
//                  $.ajax({
//                url: 'reportTemplateAction.do?templateParam=getParameterdrill&reportId='+document.getElementById("REPORTID").value,
//                   success: function(data){
//
//            alert(data);
//            if(globalval=='globalleft'){
//                 var divIdObj1=document.getElementById("tabParametersglobal");
//                 divIdObj1.innerHTML =data;
//             parent.document.getElementById('globalfilter').style.display='';
//            }else{
//                divIdObj.innerHTML =data;
//          }
//          }
//
//        });
//
//         if(globalval=='globalleft'){
//              $( "#tabParametersglobal" ).toggle("slow" );
//         }else{
//          $( "#tabParameters1" ).toggle("slow" );
//         }
//
//
//            }
            function tabparamListDisp(globalval1){
            var paramValq=$("#tabParameters1").attr('title');
            var widt=($(window).width())-(($(window).width())/10);
var rght=(($(window).width())/20);

if(paramValq=='show_param'){
              var viewbys=$("#numbuerOfViewbys").val();
    var divHeight=(3/5)*($(window).height());
    $("#tabParameters1").css('height', divHeight);
var h2=$("#headerTableData").height();
                                               var heit=(divHeight-h2);

// $(".dynamicClass").css('height', (((9/10)*(divHeight))-(viewbys*12)));
 $(".dynamicClass").css('height', heit);
 $(".dynamicClass").css('width', widt);
 $(".dynamicClass").css('overflow', 'auto');
                $("#tabParameters1").animate({
      right:rght,

      height:divHeight,
      width:widt
    });
       $("#headerDiv").css('display', 'block');
     $("#paramRegion").css('display', 'block');
           $('#tabParameters1').attr('title', 'hide_param');
        $('#tabParameters1').css('display', 'block');
}else{
  $("#tabParameters1").animate({
//      left:'50px',

      height:'0px',
      width:'0px'
    });
    $("#headerDiv").css('display', 'none');
     $("#paramRegion").css('display', 'none');

$('#tabParameters1').attr('title', 'show_param');
            }
            }
            function closeHideParamkpi(){
    document.getElementById("tabParameters1").style.display= 'none';
}



         function leftAlign(alignment,column,kpiMasterId,PbReportId,dashBoardId){

         var source = '<%=request.getContextPath()%>/DashboardHOT.jsp?kpidashid='+PbReportId+"&dashBoardId="+dashBoardId+"&kpiMasterId="+kpiMasterId+"&leftAlign="+column+"&alignment="+alignment;
           var dSrc = document.getElementById("handsonTableFrame");
               dSrc.src = source;
         }

         function addtoformula(selected){

          var actformula=document.getElementById("handsontxt2").value;
             actformula=actformula+ selected;
             document.getElementById("handsontxt2").value=actformula;


        }
         function addelementtoformula(selected,elementid){
             var actformula=document.getElementById("handsontxt2").value;
             actformula=actformula+ selected;
             document.getElementById("handsontxt2").value=actformula;

        }

    function handsontableFormula(elementids,actualElements,PbReportId,kpiMasterId,dashBoardId){
            var idArray=[];
            var names=[];
            idArray=elementids.toString().split(",");
            names=actualElements.toString().split(",")

            var actformula=document.getElementById("handsontxt2").value;
            var formulamsrID=document.getElementById("formulaRow").value;
             for (var j=0;j<idArray.length;j++){
              var finalformula=actformula.replace(names[j],idArray[j],"gi")
              actformula=finalformula;
              }
            var source = '<%=request.getContextPath()%>/DashboardHOT.jsp?kpidashid='+PbReportId+"&dashBoardId="+dashBoardId+"&kpiMasterId="+kpiMasterId+"&coustomformula=true&formula="+encodeURIComponent(finalformula)+"&formulamsrID="+formulamsrID;
           var dSrc = document.getElementById("handsonTableFrame");
           $("#HandsonFormulaID").dialog('close');
           dSrc.src = source;
        }


function kpiDrillToReport(elementId,elementname)
            {
              drillElmntId = elementId;
              drillElmntName = elementname;
             $("#KpiElementname").val(drillElmntName);
             $.post("<%=request.getContextPath()%>/dashboardTemplateViewerAction.do?templateParam2=GetReportNames&elmntId="+drillElmntId+"&foldersIds="+<%=folderDetails%>+"&elementname="+elementname,
                        function(data){
                            if(datatml != ""){
                                var htmlVal=data.split("~")
                                $("#getDrillReports").html(htmlVal[0]);
                                selectedelementId=htmlVal[1];
                            }

                        });
                        $("#drillToRep").dialog('option','title','Drill To Report')
              $("#drillToRep").dialog('open');


            }
function openNewReportDtlskpi(ctxPath){
$("#saveNewdashboard").dialog({
            autoOpen: false,
            height: 250,
            width: 350,
            position: 'justify',
            modal: true,
             title:"Save dashboard"
        });
              $("#saveNewdashboard").dialog('open')
}

 function kpiDrillToReport(elementId,elementname)
            {
              drillElmntId = elementId;
              drillElmntName = elementname;
             $("#KpiElementname").val(drillElmntName);
             $.post("<%=request.getContextPath()%>/dashboardTemplateViewerAction.do?templateParam2=GetReportNames&elmntId="+drillElmntId+"&foldersIds="+<%=folderDetails%>+"&elementname="+elementname,
                        function(data){
                            if(data != ""){
                                var htmlVal=data.split("~")
                                $("#getDrillReports").html(htmlVal[0]);
                                selectedelementId=htmlVal[1];
                            }
                        });
              $("#drillToRep").dialog('option','title','Drill To Report')
              $("#drillToRep").dialog('open');
            }



function getDisplayTables1(ctxpath,paramslist){
        var check = $("#tableList").is(":checked")
        if($("#tableListkpi").is(":checked")){
            $("#tabListDiv").hide();
            $("#tablistLink").hide();
            $("#goButton").hide();
            $("#tabsListIds").val("");
            $("#tabsListVals").val("");

            $.post(ctxpath+"/reportViewer.do?reportBy=setTableListToContainer&repId="+'<%= ReportId%>',
                function(data){
                    document.getElementById("tableColsFrame").src="TableDisplay/PbChangeTableColumnsRT.jsp?loadDialogs=true&currentReportId="+'<%= ReportId%>'+"&currentBizRoles="+'<%=roleid%>'+'&isKPIDashboard=true&tableList=true'+"";
                });

        }else{
            $("#tabListDiv").show();
            $("#tablistLink").show();
            $("#goButton").show();
            var htmlVar = "<table>";
           $.post(ctxpath+"/reportViewer.do?reportBy=getTableList&paramslist="+paramslist+'&currentBizRoles='+'<%=roleid%>',
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
                    document.getElementById("tableColsFrame").src="TableDisplay/PbChangeTableColumnsRT.jsp?loadDialogs=true&isKPIDashboard=true&currentReportId="+'<%= ReportId%>'+"&currentBizRoles="+'<%=roleid%>'+"";
                });

        }
    }
     function getDisplayTablesInDesigner1(ctxpath,paramslist,repId){
       var repId=<%=ReportId%>

    var frameObj=document.getElementById("dataDispmem");

        var check = $("#tableList").is(":checked")
        if($("#tableList").is(":checked")){
            $("#tabListDiv").hide();
            $("#tablistLink").hide();
            $("#goButton").hide();
            $("#tabsListIds").val("");
            $("#tabsListVals").val("");
                var source="reportTemplateAction.do?templateParam=getMeasures1&foldersIds="+document.getElementById("roleid").value+'&REPORTID='+document.getElementById("REPORTID").value+'&tableList=true&isTimeDashboard=true';

        frameObj.src=source;

        }else{
            $("#tabListDiv").show();
            $("#tablistLink").show();
            $("#goButton").show();
            var htmlVar = "<table>";
           $.post(ctxpath+"/reportViewer.do?reportBy=getTableList&paramslist="+paramslist+'&currentBizRoles='+document.getElementById("roleid").value,
                function(data){

                    var jsonVar=eval('('+data+')');

                    var json1 = jsonVar.idsList.split(",");
                    var jsonname = jsonVar.namesList.split(",");
                    for(var i=0;i<json1.length;i++){

                        if(json1[i].replace(" ", "", "gi") != "0")
                        htmlVar += "<tr><td id='"+json1[i]+"' onclick=\"selectTablesInDesigner('"+json1[i]+"','"+jsonname[i]+"')\">"+jsonname[i]+"</td></tr>";
                    }
                    htmlVar += "</table>";
                    $("#paramVals").html(htmlVar);
                    var source="reportTemplateAction.do?templateParam=getMeasures1&foldersIds="+document.getElementById("roleid").value+'&REPORTID='+document.getElementById("REPORTID").value;

        frameObj.src=source;
                });

        }
    }


   function getDisplayTablesInDesigner(ctxpath,paramslist,repId){
       var repId=<%=ReportId%>

    var frameObj=document.getElementById("dataDispmem");

        var check = $("#tableList").is(":checked")
        if($("#tableList").is(":checked")){
            $("#tabListDiv").hide();
            $("#tablistLink").hide();
            $("#goButton").hide();
            $("#tabsListIds").val("");
            $("#tabsListVals").val("");
                var source="reportTemplateAction.do?templateParam=getMeasures&foldersIds="+document.getElementById("roleid").value+'&REPORTID='+document.getElementById("REPORTID").value+'&tableList=true';

        frameObj.src=source;

        }else{
            $("#tabListDiv").show();
            $("#tablistLink").show();
            $("#goButton").show();
            var htmlVar = "<table>";
           $.post(ctxpath+"/reportViewer.do?reportBy=getTableList&paramslist="+paramslist+'&currentBizRoles='+document.getElementById("roleid").value,
                function(data){

                    var jsonVar=eval('('+data+')');

                    var json1 = jsonVar.idsList.split(",");
                    var jsonname = jsonVar.namesList.split(",");
                    for(var i=0;i<json1.length;i++){

                        if(json1[i].replace(" ", "", "gi") != "0")
                        htmlVar += "<tr><td id='"+json1[i]+"' onclick=\"selectTablesInDesigner('"+json1[i]+"','"+jsonname[i]+"')\">"+jsonname[i]+"</td></tr>";
                    }
                    htmlVar += "</table>";
                    $("#paramVals").html(htmlVar);
                    var source="reportTemplateAction.do?templateParam=getMeasures&foldersIds="+document.getElementById("roleid").value+'&REPORTID='+document.getElementById("REPORTID").value;

        frameObj.src=source;
                });

        }
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

 function dispChangeTableColumnskpi(ctxPath,bizRoles,reportId){
            $.ajax({
        url:ctxPath+'/reportViewer.do?reportBy=loadDialogs&loadDialogs=true&currentReportId='+reportId+'&currentBizRoles='+bizRoles,
        success:function(data) {
            $("#tableColsDialog").dialog('open');
           document.getElementById("tableColsFrame").src="TableDisplay/PbChangeTableColumnsRT.jsp?loadDialogs=true&currentReportId="+reportId+"&currentBizRoles="+bizRoles+"&isKPIDashboard=true&from=true";

        }
            });

    }

    function saveKPIDrillAssignReports(reportId,ctxtPath){
    var isKPIDashboard1='<%=isKPIDashboard%>'

    $.post(ctxtPath+'/reportViewer.do?reportBy=saveDrillAssignReports&isKPIDashboard='+isKPIDashboard1+'&reportId='+reportId,$("#reportDrillFrm").serialize() ,
             function(data){
                $("#reportDrillDiv").dialog('close');
$("#KpiDashboardDiv").html(data);
             });
  }
     function reportDrillAssignmentkpi(ctxtPath,reportId,multireport){
    $.post(ctxtPath+'/reportViewer.do?reportBy=reportDrillAssignment&reportId='+reportId,
                 function(data){
                if(data!='null'){
                    var jsonVar=eval('('+data+')')
                    var roleName=jsonVar.roleName;
                    var MsrIds=jsonVar.MsrIds;
                    var MsrNames=jsonVar.MsrNames;
                    var reportIds=jsonVar.reportIds;
                    var reportNames=jsonVar.reportNames;
                    var assignRepIds=jsonVar.assignRepIds;
                    var htmlVar="";
                                 $("#reportDrillDiv").dialog({
                     autoOpen: false,
                     height: 250,
                     width: 350,
                     position: 'justify',
                     modal: true,
                     resizable:true
                });
                     var displayRepo="";
                     var displayRepo1="";
                        if(multireport=='multi report'){
                        displayRepo="none";
                    htmlVar+="<table width='100%'><tr><td width='40%'><input type='radio' name='reportselection' id='singlereport' onclick=\"selectSingleReport('"+MsrIds+"')\" value='single report'>Single Report</td>";
                    htmlVar+="<td colspan='2' width='60%' style=\"display:none\"><input type='radio' name='reportselection' id='multireport' onclick=\"selectMultieReport('"+MsrIds+"')\" value='multi report' checked>Multi Report</td></tr></br>";
                            }
                         else{
                       displayRepo1="none";
                    htmlVar+="<table width='100%'><tr><td width='40%'><input type='radio' name='reportselection' id='singlereport' onclick=\"selectSingleReport('"+MsrIds+"')\" value='single report' checked>Single Report</td>";
                    htmlVar+="<td colspan='2' width='60%' style=\"display:none\"><input type='radio' name='reportselection' id='multireport' onclick=\"selectMultieReport('"+MsrIds+"')\" value='multi report'>Multi Report</td></tr></br>";
                       }
                    htmlVar+="<tr><td align='center' colspan='3'><b>"+roleName[0]+" Role</b></td></tr><tr><td width='50%' align='center' style='background-color:#b4d9ee;font-size:12px;font-family:verdana'>Measure Name</td><td>&nbsp;</td>";
                    htmlVar+="<td width='90%' align='center' style='background-color:#b4d9ee;font-size:12px;font-family:verdana'>ReportName</td></tr>";
                    for(var i=0;i<MsrIds.length;i++){
                        var reportNames1="";

                   htmlVar+="<tr><td ><input type='text' value='"+MsrNames[i]+"' style='background-color:white;width:90%' readonly=''id='"+MsrIds[i]+"' name='msrName'></td><td><input type='hidden' value='"+MsrIds[i]+"' style='background-color:white' id='"+MsrIds[i]+"' name='msrId'></td>";

                     htmlVar+="<td id='singleReportTd"+MsrIds[i]+"'  style='display:"+displayRepo+"'><select id='MsrReport"+MsrIds[i]+"' style='width:90%;' name='MsrReport'>";
                    if(assignRepIds[i]=='0'){
                    htmlVar+="<option selected value='0'>NOT_SELECTED</option>";
                }else{
                    htmlVar+="<option  value='0'>NOT_SELECTED</option>";
                }
                       for(var j=0;j<reportIds.length;j++){
                          if(assignRepIds[i]==reportIds[j])
                           htmlVar+="<option selected value='"+reportIds[j]+"'>"+reportNames[j]+"</option>";
                           else
                            htmlVar+="<option value='"+reportIds[j]+"'>"+reportNames[j]+"</option>";
                       }
                       htmlVar+="</select></td>";
                   $.post(ctxtPath+'/reportViewer.do?reportBy=getMultiSelectReportNames&msrId='+MsrIds[i]+"&reportId="+reportId,
                   function(data){
                   var jsonVar=eval('('+data+')');
                   reportNames1=jsonVar.reportNames;
                   var msrId=jsonVar.msrId;
                   $("#multireport"+msrId).val(reportNames1);
                     });
                       htmlVar+="<td id='multiReportTd"+MsrIds[i]+"' style='display:"+displayRepo1+"'><input type='text' value='' onclick=\"getmultiReportIds('"+ctxtPath+"','"+reportId+"',this.id)\" style='background-color:white;width:90%;' id='multireport"+MsrIds[i]+"' name='multireportNames' ></td>";

                       htmlVar+="</tr>";
                       htmlVar+="<tr><td colspan='3'><input type='hidden' value='0' style='background-color:white' id='multireportId"+MsrIds[i]+"' name='multireportIds'></td></tr>";
                       }
                       htmlVar+="<tr><td>&nbsp;</td></tr><tr><td colspan='3'  align='center'><input type='button' value='submit' class='navtitle-hover'  style='width:auto' onclick=\"saveKPIDrillAssignReports('"+reportId+"','"+ctxtPath+"')\"></td></tr>";
                       htmlVar+="</table>";
                       $("#reportDrillFrm").html(htmlVar);
                      $("#reportDrillDiv").dialog('open');
                   }

                    });
        }
            </script>
   <style>
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
}
#ui-datepicker-div
            {
                z-index: 9999999;
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
            .ajaxboxstyle1 {
                position: relative; background-color: #FFFFFF;text-align: left;border: 1px solid #000000;border-top-width:1px;height:150px;width:230px;overflow:auto;overflow:hidden; margin:0em 0.5em;z-index: 9999999;
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
.dimensionCell1
{
    background-color: rgb(230, 230, 230);
    color:#336699;
    font-family: verdana;
    text-align: left;
}
.tabMenuCol1
            {
                background-color:#ffffff;
            }
#dateregionDiv { position: fixed; top: 0px; left:200px; margin: auto; right: 800px; width: 50px; border: none;  }
#fixedtop1 { position: fixed; top: 0px; left:200px; margin: auto; right: 800px; width: 50px; border: none;  }
#filterdivtop { position: fixed; top: 12%; left:200px; margin: auto; right: 800px; width: 50px; border: none;  }
#center250a { width: auto;height: 65px;  background:none; }
#center250b { width: auto;height: 65px;   background:none; }
 .mydivBodyClass {
                height: 25px;
                overflow: auto;
            }
            </style>

    </head>
    <body>
         <div width="99%" height="100%" valign="top" >
  <form name="submitReportForm" id="submitReportForm" method="POST" action="">
       <%if (isKPIDashboard =="true") {%>
                              <% if(timeinfo.get(1).equalsIgnoreCase("PRG_DATE_RANGE")) { %>
       <div id="dateregionRange" style="display:none">
     <div id="fixedtop1" >

                                                        <div id="center250b">

                                                            <div class="form clearFix">
                                                                <span class="wr100">
                                                                    <table align="center" id="roundtrip">
                <% if(sytm.equalsIgnoreCase("No")){ %>                                                   <tr width="100%"> <%--<% if(userdao.getFeatureEnable("Date Selection") || userType.equalsIgnoreCase("SUPERADMIN"))  {%>--%>
       <td  white-space:nowrap ;width: auto"></td>
           <td  align="right"  id="datetime"  tabindex="6">
        <span id="depShow">
        <span class="top w100 mrtop" id="field1"><%=(container.fullName0).substring(0,4)+(container.fullName0).substring(6)%></span>
        <span class="date">
            <small Style="font-weight: bold" id="field2"><%=container.dated%></small></span>
        <span class="bottom w100" id="field3"><%=container.day0%></span></span></td>
    <td><input type="hidden" class="ui-datepicker" id="fromdate" name="fromdate" onchange="dateclick(this.id)" onclick="showdate()"/></td>
        <td><input  type="hidden" id="datetext" name="datetext" value=""/></td>

          <td align="left" style="white-space:nowrap ;width: auto;padding-left: 1.5em">To :</td>
         <td align="right" id="todatetime" tabindex="8"  style="padding-left: 0.5em">
             <span id="retShow" style="position:relative;">
         <span class="top w100 mrtop" id="tdfield1"><%=(container.fullName).substring(0,4)+(container.fullName).substring(6)%></span>
        <span class="date"><small class="init" Style="font-weight: bold" id="tdfield2"><%=container.date%></small></span>
        <span class="bottom w100" id="tdfield3"><%=container.day%></span></span></td>
        <td><input  type="hidden" class="ui-datepicker " name="todate" id="todate" onchange="dateclick(this.id)" onclick="showdate()"/> </td>
        <td><input  type="hidden" id="datetext" name="datetext" value=""/></td>
           <td align="right" style="font-weight:bold ; width: auto;padding-left: 1.5em"> COMPARE</td>
           <td align="right" style="width: auto;padding-left: 1.5em"></td>
         <td align="left" id="comparefromtime" tabindex="8"  style="padding-left: 0.5em">
             <span id="retShow" style="position:relative;">
         <span class="top w100 mrtop" id="cffield1"><%=(container.cffullName).substring(0,4)+(container.cffullName).substring(6)%></span>
        <span class="date"><small class="init" Style="font-weight: bold" id="cffield2"><%=container.cfdate%></small></span>
        <span class="bottom w100" id="cffield3"><%=container.cfday%></span></span></td>
         <td><input type="hidden" class="ui-datepicker " id="comparefrom" name="comparefrom"  onchange="dateclick(this.id)" onclick="showdate()"/></td>
         <td><input  type="hidden" id="datetext" name="datetext" value=""/></td>

    <td align="right" style="width: auto;padding-left: 1.5em">To:</td>
    <td align="left" id="comparetoTime" tabindex="8"  style="padding-left: 0.5em"> <span  id="retShow" style="position:relative;">
         <span class="top w100 mrtop" id="ctfield1"><%=(container.ctfullName).substring(0,4)+(container.ctfullName).substring(6)%></span>
        <span class="date"><small class="init" Style="font-weight: bold" id="ctfield2"><%=container.ctdate%></small></span>
        <span class="bottom w100" id="ctfield3"><%=container.ctday%></span></span></td>
         <td><input  type="hidden" class="ui-datepicker1 " id="compareto" name="compareto" onchange="dateclick(this.id)" onclick="showdate()"/></td>
         <td><input  type="hidden" id="datetext" name="datetext" value=""/></td>
<%}else{%>
         <td><table><tr style="display: block" id="toggleYear1" title="Toggle Year">
                 <td  align="right"  id="datetime"  tabindex="6">
<!--                 <td style="display: block" id="showHideYear" title="Show/Hide Year"><table><tr>-->
                 <td> From:&nbsp;<select id="calFromYear" name="calFromYear" style="width:90px" onchange="saveYearDetails('fromdate')">
                         <%
                              String fromYear = (container.fullName0).substring(4);
                              for (int year = 2005; year <= 2020; year++) {
                                     if (fromYear.equalsIgnoreCase(Integer.toString(year))) {%>
                         <option selected value="<%=year%>"> <%=year%> </option>
                         <%} else {%>
                         <option value="<%=year%>"> <%=year%> </option>
                         <%}
                                     }%>

                     </select></td>
                 <td>To:&nbsp;<select id="calToYear" name="calToYear" style="width:90px" onchange="saveYearDetails('todate')">
                         <%
                              String toYear = (container.fullName).substring(4);
                              for (int year = 2005; year <= 2020; year++) {
                                     if (toYear.equalsIgnoreCase(Integer.toString(year))) {%>
                         <option selected value="<%=year%>"> <%=year%> </option>
                         <%} else {%>
                         <option value="<%=year%>"> <%=year%> </option>
                         <%}
                                     }%>
                     </select></td>

                 <%if (container.cfdate != null && container.ctdate != null) {%>

<!--                 <td style="display: block" id="showHideCompareYear" title="Show/Hide Compare Year"><table><tr>-->
                             <td align="right" style="font-weight:bold ; width: auto;padding-left: 1.5em"> COMPARE</td>
                             <td align="right" style="width: auto;padding-left: 1.5em"></td>
                             <td> From:&nbsp;<select id="calCmpFromYear" name="calCmpFromYear" style="width:90px" onchange="saveYearDetails('comparefrom')">
                                     <%
                                         String fromCmpYear = (container.cffullName).substring(4);
                                         for (int year = 2005; year <= 2020; year++) {
                                    if (fromCmpYear.equalsIgnoreCase(Integer.toString(year))) {%>
                                     <option selected value="<%=year%>"> <%=year%> </option>
                                     <%} else {%>
                                     <option value="<%=year%>"> <%=year%> </option>
                                     <%}
                                    }%>
                                 </select></td>
                             <td>To:&nbsp;<select id="calCmpToYear" name="calCmpYear" style="width:90px" onchange="saveYearDetails('compareto')">
                                     <%
                                         String toCmpYear = (container.ctfullName).substring(4);
                                         for (int year = 2005; year <= 2020; year++) {
                                    if (toCmpYear.equalsIgnoreCase(Integer.toString(year))) {%>
                                     <option selected value="<%=year%>"> <%=year%> </option>
                                     <%} else {%>
                                     <option value="<%=year%>"> <%=year%> </option>
                                     <%}
                                    }%>
                                 </select></td>




                         <%}%>


             </tr></table></td>
             <td style="display: none" id="toggleYear" title="Toggle Year"><table><tr>
                 <td  align="right"  id="datetime"  tabindex="6">

                 <td>Year:&nbsp;</td>
                 <td id="showHideYear1" title="Show/Hide Year1"><table><tr>
                 <td> <select id="calFromToyear" name="calyear" style="width:90px" onchange="saveYearDetails('fromdatetodate')">
                         <%
                              String fromYear1 = (container.fullName0).substring(4);
                              String toYear1 = (container.fullName).substring(4);
                              String combYear = fromYear1 + " - " + toYear1;
                              
                              for (int year = 2005; year <= 2020; year++) {
                                  String combYear1 = Integer.toString(year) + " - " + Integer.toString(year + 1);
                                     if (combYear1.equalsIgnoreCase(combYear)) {%>
                         <option selected value="<%=combYear1%>"> <%=combYear1%> </option>
                         <%} else {%>
                         <option value="<%=combYear1%>"> <%=combYear1%> </option>
                         <%}
                                     }%>
                     </select>
                 </td>
                 </tr></table></td>

                 <%if (container.cfdate != null && container.ctdate != null) {%>
                 <td id="showHideCompareYear1" title="Show/Hide Compare Year"><table><tr>
                             <td>Compare:&nbsp;&nbsp;&nbsp; </td><td><select id="calCmpFromToYear" name="calCmpFromToYear" style="width:90px" onchange="saveYearDetails('comparefromcompareto')">
                                     <%
                                         String fromCmpYear1 = (container.cffullName).substring(4);
                                         String toCmpYear1 = (container.ctfullName).substring(4);
                                         String combCmpYear1 = fromCmpYear1 + " - " + toCmpYear1;
                                         
                                         for (int year = 2005; year <= 2020; year++) {
                                             String combCmpYear11 = Integer.toString(year) + " - " + Integer.toString(year + 1);
                                    if (combCmpYear11.equalsIgnoreCase(combCmpYear1)) {%>
                                     <option selected value="<%=combCmpYear11%>"> <%=combCmpYear11%> </option>
                                     <%} else {%>
                                     <option value="<%=combCmpYear11%>"> <%=combCmpYear11%> </option>
                                     <%}
                                    }%>
                                 </select></td>

                         </tr></table></td>
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
       <script type="text/javascript">
                                     var setyear = $('#calFromYear option:selected').val()
                setyear="Mon,01,Jan,".concat(setyear)
                             $('#fromdate').val(setyear);

                setyear = $('#calToYear option:selected').val()
                setyear="Mon,31,Dec,".concat(setyear)
                $('#todate').val(setyear);

                setyear = $('#calCmpFromYear option:selected').val()
                setyear="Mon,01,Jan,".concat(setyear)
                $('#comparefrom').val(setyear);

                setyear = $('#calCmpToYear option:selected').val()
                setyear="Mon,31,Dec,".concat(setyear)
                $('#compareto').val(setyear);

                                     </script>



     <%}%>



   <%if (isKPIDashboard =="true") {%>
           <td id="saveTabId" ><a style="text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;font-size: 9pt;" class="ui-icon ui-icon-disk" title="Global Save" href="javascript:overWriteReport1('<%=request.getContextPath()%>','overWrite')"></a></td>
          <%}else{%>
           <td id="saveTabId" ><a href="javascript:void(0)" style="text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;font-size: 9pt;" class="ui-icon ui-icon-disk" title="Global Save" onclick="saveKPIDashboard()"></a></td>
          <%}%>
           <td> <a onclick="openImgDiv()" href="<%=resetpath%>"> Reset </a></td><td></td>
            <td id="dateToggle"><a href="javascript:void(0)" class="ui-icon ui-icon-transferthick-e-w" title="Toggle" onclick="datetogglkpi('<%=userType%>','<%=isPowerAnalyserEnableforUser%>')"></a></td>
            <td width="35"><input  type="button" class="navtitle-hover" value=" Go "  style="height: 20px; width: 25px" onclick="submitformkpi()"/></td>
          </tr></table></span></div></div></div>
       </div>
              <%}%>
              <%}%>

              <% if((fromDesignerkpi!=null && fromDesignerkpi.equalsIgnoreCase("true"))||!timeinfo.get(1).equalsIgnoreCase("PRG_DATE_RANGE")) { %>
         <div id="dateregionDiv" style="display:none;">
     <div id="fixedtop1" >
     <div id="center250a">
     <div class="form clearFix">
      <span class="wr100">
       <table align="center" id="roundtrip">
       <tr width="100%">
        <td align="right">
            <% if(sytm.equalsIgnoreCase("No")){
                if(fromDesignerkpi!=null && fromDesignerkpi.equalsIgnoreCase("true"))
                    {%><span class="top w100 mrtop" id="pfield1"><%=(container.fullName0)%></span>
    <%}else
        {%>
        <span class="top w100 mrtop" id="pfield1"><%=(container.fullName0).substring(0,4)+(container.fullName0).substring(6)%></span><%}%>
        <span class="date">
        <small Style="font-weight: bold" id="pfield2"><%=container.dated%></small></span>
        <span class="bottom w100" id="pfield3"><%=container.day0%></span></td>
         <td> <input height="100px" width="100px" type="hidden" class="ui-datepicker " id="perioddate" name="perioddate" onchange="dateclick(this.id)" onclick="showdate()"/><td>
         <td> <input  type="hidden" id="datetext" name="datetext" value=""/></td>
         <%}else{%>
         <td>Year:&nbsp;</td><td align="right">
        <select id="calPeriodYear" name="calPeriodYear" style="width:90px" onchange="saveYearDetails('perioddate')">
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
        <td><input height="100px" width="100px" type="hidden" id="perioddate" name="perioddate" onchange="" onclick=""/></td>
         <td> <input  type="hidden" id="datetext" name="datetext" value=""/></td>
         <script type="text/javascript">
                                     var setyear1 = $('#calPeriodYear option:selected').val()
                setyear1="Mon,01,Jan,".concat(setyear1)
                             $('#perioddate').val(setyear1);

                                     </script>
          <%}%>
        <%  if (isKPIDashboard =="true") {%>
         <td>
       <%

           DisplayParameters dur=new DisplayParameters();
               String duration1= String.valueOf(dur.displayTime(collect.timeDetailsMap)) ;
     %>
      <%=duration1%>
       </td>

         <%}else{%>
<td id="timebasistd"></td>
       <%}%>
        <%if (isKPIDashboard =="true") {%>
           <td id="saveTabId" ><a style="text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;font-size: 9pt;" class="ui-icon ui-icon-disk" title="Global Save" href="javascript:overWriteReport1('<%=request.getContextPath()%>','overWrite')"></a></td>
          <%}else{%>
           <td id="saveTabId" ><a href="javascript:void(0)" style="text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;font-size: 9pt;" class="ui-icon ui-icon-disk" title="Global Save" onclick="saveKPIDashboard()"></a></td>
          <%}%>
           <td> <a onclick="openImgDiv()" href="<%=resetpath%>"> Reset </a></td><td></td>
            <td id="dateToggle"><a href="javascript:void(0)" class="ui-icon ui-icon-transferthick-e-w" title="Toggle" onclick="datetogglkpi('<%=userType%>','<%=isPowerAnalyserEnableforUser%>')"></a></td>
            <td width="35"><input  type="button" class="navtitle-hover" value=" Go "  style="height: 20px; width: 25px" onclick="submitformkpi()"/></td>
  </tr>
        </table>
                                                                </span>
 </div>

                                                       </div>
                                                       </div>
</div>
<%}%>



                <table style="min-width:100%;font-family: Calibri, Calibri, Calibri, sans-serif;font-size:14pt;color: darkblue" max-height="auto" max-width="auto" >
            <tr>

                <td>
                    <table style="width:100%;">
                        <tr>
                            <td valign="top" style="width:50%;">
                                <%if(piVersion=="piVersion2014") { if (isKPIDashboard =="true") {%>
                                 <jsp:include page="Headerfolder/headerPage.jsp"/>
                                                <jsp:include page="dashboardPage.jsp"/>
                                <% }} else { %>
                                <jsp:include page="Headerfolder/headerPage.jsp"/>
                                <% } %>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
 <tr><td>
                    <%if (isKPIDashboard =="true") {%>
           <div id="filterdivtop" style="width:70%; ">

               <%
              DisplayParameters dispParams = new DisplayParameters();
               result1 =dispParams.buildfilreregion(collect,container,openevent);
               %>
            <%=result1%>

           </div>
<%}%>
                </td></tr>
        </table>




                   <table>
                      <tr>
                         <%if (isKPIDashboard !="true") {%>
                          <td valign="top" style="width:3%;position: relative">
                             <a href="javascript:void(0)" class="ui-icon ui-icon-plusthick" onclick="AddMoreDims('KpiDash')"></a>
                          </td>
                          <%}else{%>
                          <td width="97%"></td>
                          <td><a onclick="tabparamListDisp('paramregion')" href="javascript:void(0)" title="Parameter Region"><img alt="Parameter Region" class="ui-icon ui-icon-contact"/></a></td>




                          <%}%>
                              <td  id="saveadhoc" valign="top" style="width:1%;position: relative;display:none;">
                             <a href="javascript:void(0)" class="ui-icon ui-icon-disk" onclick="saveKpiDashboard()"></a>
                          </td>
                          <td valign="top" style="width:80%;" ></td>
                           </tr>
                  </table>
                  <input type="hidden" name="dbrdId" value="<%=ReportId%>" id="dbrdId">
                  <input type="hidden" name="dbrdId" value="<%=ReportId%>" id="REPORTID">
                   <input type="hidden" name="dbrdId" value="<%=Dashboardname%>" id="Dashboardname">
                   <input type="hidden" name="roleid" id="roleid" value="<%=roleid%>">
                  <input type="hidden" name="folderId" id="folderId" value="<%=roleid%>">
                  <input type="hidden" name="divId" value="" id="divId">
                  <input type="hidden" name="kpiIds" value="" id="kpiIds">
                  <input type="hidden" name="Name" id="Name" value="<%=Dashboardname%>">
                                     <input type="hidden" name="Desc" id="Desc" value="<%=reportDesc%>">
                  <input type="hidden" name="kpis" value="" id="kpis">
                  <input type="hidden" name="kpiType" id="kpiType">
                    <input type="hidden" name="isKpiDashboard" id="isKpiDashboard" value="true">
                  <input type="hidden" name="MsrIds" value="" id="MsrIds">
                      <input type="hidden" name="Measures" value="" id="Measures">
                  <input type="hidden" name="REPIds" value="<%=prevREP%>" id="REPIds">
                    <input type="hidden" name="CEPIds" value="<%=prevCEP%>" id="CEPIds">
                       <input type="hidden" id="Designer" name="Designer" value="fromDesigner">
                  <input type="hidden" id="Designer" name="Designer" value="fromDesigner">
                   <input type="hidden" name="cntxtpath" id="cntxtpath" value="<%=request.getContextPath()%>">
                   <input type="hidden" id="h" value="<%=request.getContextPath()%>">
<!--                     <div id="tabParameters1" style="display:none;width:22%;height:100%;background-color:white; direction: ltr; float: right; position:absolute;text-align:left;border: 1px solid LightGrey;left:0px;top:122px;z-index: 10000;">
                                                                    </div>-->
                     <div id="tabParameters1" title="show_param" style="display:none;width:0px;height:0px;background-color:white; position:absolute;text-align:left;border: 1px solid LightGrey;top:122px;z-index: 1000;">
                                                                   <%=ParamSectionDisplay%> </div>


                 </form></div>
                      <%if (isKPIDashboard =="true") {%>
                     <div id="overWriteReport" style="display:none" title="OverWriteReport">
     <table>
        <tr><td colspan="2"><font size="2" style="font-weight: bold;">Do you want to over write the report ?</font></td></tr>
        <% if(!timeinfo.get(1).toString().equalsIgnoreCase("PRG_DATE_RANGE")){%>
          <tr><td><input type="radio" name="Date" id="sysDate" value="sysDate">Global Date</td><td>&nbsp;&nbsp;&nbsp;<input type="radio" name="Date" id="reportDate" value="reportDate" checked>Report Date</td><td>&nbsp;&nbsp;&nbsp;<input type="radio" name="Date" id="currdetails" value="currdetails" checked>Current Details</td>
              <td>&nbsp;&nbsp;&nbsp;<input type="checkbox" name="autometicDate" id="autometicDate" <%=autoDate%> onclick="" value="autometicDate">Autometic Date</td></tr>
          <tr><td><br></td></tr>
          <tr><td><input type="radio" name="Date" id="yestrday" value="yestrday">YesterDay</td><td>&nbsp;&nbsp;&nbsp;<input type="radio" name="Date" id="tomorow" value="tomorow">Tomorrow</td>
          <td>&nbsp;&nbsp;&nbsp;<input type="checkbox" name="Cache" id="cacheAO"  onclick="" value="cacheAO">Cache Analytical Object</td></tr>
          <tr><td><br></td></tr>
          <tr><td><input type="radio" name="Date" id="newSysDate" value="newSysDate">System Date</td><td><select id="sysSign" name="sign">
                   <option value="+">+</option>
                   <option value="-">-</option>
                   </select></td><td><input id="newSysVal" type="text">Days </td></tr>
              <tr><td><input type="radio" name="Date" id="globalDate" value="globalDate" >Global Date</td><td><select id="globalSign" name="globalSign">
                   <option value="+">+</option>
                   <option value="-">-</option>
                   </select></td><td><input id="newGlobVal" type="text">Days </td></tr>
              <%} else{ %>
              <tr><td><input type="radio" name="Date" id="reportDate" onclick="getCustomDate()" value="reportDate" checked>Report Date</td><td>&nbsp;&nbsp;&nbsp;<input type="radio" name="Date" id="customDate" onclick="getCustomDate()" value="customDate">Custom Date</td>
                  <td>&nbsp;&nbsp;&nbsp;<input type="radio" name="Date" id="currdetails" onclick="getCustomDate()" value="currdetails">Current Details</td>
             <td>&nbsp;&nbsp;&nbsp;<input type="checkbox" name="autometicDate" id="autometicDate" <%=autoDate%> onclick="" value="autometicDate">Autometic Date</td>
               <td>&nbsp;&nbsp;&nbsp;<input type="checkbox" name="Cache" id="cacheAO"  onclick="" value="cacheAO">Cache Analytical Object</td></tr>
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
                  <tr><td><input type="radio" name="CmpToDate" id="cmptoglobalDate" value="fromToglobalDate" >Global Date</td><td><select id="cmptoglobalSign" name="globalSign">
                              <option value="+">+</option>
                              <option value="-">-</option>
                          </select></td><td><input id="cmptoGlobVal" type="text">Days </td></tr>
              </table>
              <%}%>
          <tr><td><br></td></tr>
          <tr><td><br></td></tr>
          <tr><td colspan="2" align="center"><input type="button" value="Ok" class="navtitle-hover" style="width:40px;height:25px;color:black" onclick="kpiOverWriteReport('<%=request.getContextPath()%>','<%=ReportId%>')"/>&nbsp;&nbsp;&nbsp;
         <input type="button" value="Cancel" class="navtitle-hover" style="width:50px;height:25px;color:black" onclick="cancelOverWriteReport()"/></td></tr>
        </table>
 </div>
<%}%>
               <table width="99%" height="100%" valign="top" >
                      <tr>
                          <%if (isKPIDashboard =="true") {%>
                          <td width="1%"  style="background-color:#e6e6e6;border:0px;cursor:pointer"  valign="top" >

                            </td> <%}%>
                          <td>
                  <table width="99%" height="100%" valign="top" style=" border-width: 4px; border-style: groove; "><tbody valign="top">

                          <tr><td>
                  <table id="tablePortlet"style="width:99%;height:100%;" valign="top">
                      <tbody valign="top">

                         <tr id="previewTable" style="height:100%;">
                             <td  style="height:100%;" valign="top" >
                                 <center><img id="imgId" src="<%= request.getContextPath()%>/images/ajax1.gif" align="middle"  width="100px" height="100px"  style="top:100px; display:none;position:absolute" /></center>
                                 <center><img id="imgId" src="<%= request.getContextPath()%>/images/ajax1.gif" align="middle"  width="100px" height="100px"  style="top:100px; display:none;position:absolute" /></center>

                                 <div id=KpiDashboardDiv class='ui-tabs ui-widget ui-widget-content ui-corner-all' style='border-bottom: medium hidden LightGrey ; border-left: medium hidden LightGrey ; border-right: medium hidden LightGrey ; border-color: LightGrey ; width: 100%; height: 100%; margin-left: 10px; margin-right: 10px;'>
 <%if (isKPIDashboard =="true") {%>
     <%=result%>
 <%}%>
                                 </div>
                    </td>
                </tr>
                      </tbody>
                  </table>
                          </td> </tr>
                      </tbody>
                  </table>
                  </td>
                    </tr>
               </table>

                      <table style="width:100%">
            <tr>
                <td valign="top" style="width:100%;">
                    <jsp:include page="Headerfolder/footerPage.jsp"/>
                </td>
            </tr>
        </table>
                  <div id="AddMoreParamsDiv" title="Add More Dimension " style="display:none">
                         <table><tr> <td><a class="ui-icon ui-icon-clock"></a></td>
                          <td>
                        <select style="width:120px" id="time" name="time">

                         <option id="StandardTime" value="StandardTime" onclick="timeBasis()" name="time">Standard Time</option>

                        </select>
                          </td></tr></table>

           <iframe  id="addmoreParamFrame" name='addMoreParamFrame' width="100%" height="100%" frameborder="0"   src='about:blank'></iframe>
        </div>
         <div id="getKPiHeadersDiv" title="get kpi Headers" style="display:none">

           <iframe  id="getKPiHeadersFrame" name='getKPiHeadersFrame' width="100%" height="100%" frameborder="0"   src='about:blank'></iframe>
        </div>
        <div id="kpisDialog" style="display:none">
    <table><tr><td>
            <input id="tableList1" type="checkbox" onclick="getDisplayTablesdesigner('<%=request.getContextPath() %>','','<%=ReportId%>')">All</td>
                    <td id="tabListDiv1" ><input type="textbox" id="tabsListVals1"><input type="textbox" style="display:none;" id="tabsListIds1">
                        <div id="paramVal1" class="ajaxboxstyle1" style="overflow: auto;"></div></td>
                    <td id="tablistLink1" ><a href="javascript:void(0)" class="ui-icon ui-icon-note" onclick="showListdesigner('<%=request.getContextPath() %>','')" ></a></td>
                    <td id="goButton1" onclick="setValueToContainerdesigner('<%=request.getContextPath() %>')"><input type="button" value="GO" class="navtitle-hover"></td>
            </tr></table>
    <iframe id="kpidataDispmem" NAME='kpidataDispmem' width="100%" height="100%" frameborder="0" src='about:blank'></iframe>
</div>
 <div id="timePeriodDialog" style="display:none" title="Select Period">
                <table style="width:100%">
                    <%int count=0;
                for(int i=0; i<listOfTimePeriods.length;i++){%>
                    <tr><td width="45%"><input id='<%=listOfTimePeriods[i]%>' type="checkbox" onclick=MTDchecked(this.id,'<%=count%>')><%=listOfTimePeriods[i]%></td></tr>
                 <%  if(i==0){
                    for(int j=0; j < 7; j++){%>
                    <tr><td></td><td><input id='<%=dependentTimePeriodsdisplay[count]%>' type="checkbox"/><%=dependentTimePeriods[count]%></td></tr>
                 <%   count++;}
                }
if(i==1){
for(int j=count; j < 14; j++){%>
                    <tr><td></td><td><input id='<%=dependentTimePeriodsdisplay[count]%>' type="checkbox"/><%=dependentTimePeriods[count]%></td></tr>
                            <%count++;}%>

<%}if(i==2){for(int j=count; j <18 ; j++){%>
<tr><td></td><td><input id='<%=dependentTimePeriodsdisplay[count]%>' type="checkbox"/><%=dependentTimePeriods[count]%></td></tr>
                    <%count++;}}}%>
                    <tr></tr>
                    <tr><td></td><td align="left"><input type="button" value="Done" onclick="checkSelectedPeriods()"></td></tr>
                </table>
            </div>
<div id="tableColsDialog"  style="display:none" >
            <table><tr><td>
            <input id="tableListkpi" type="checkbox" onclick="getDisplayTables1('<%=request.getContextPath() %>','<%=params%>')">All</td>
                    <td id="tabListDiv" ><input type="textbox" id="tabsListVals"><input type="textbox" style="display:none;" id="tabsListIds">
                        <div id="paramVals" class="ajaxboxstyle" style="display:none;overflow: auto;"></div></td>
                    <td id="tablistLink" ><a href="javascript:void(0)" class="ui-icon ui-icon-note" onclick="showList('<%=request.getContextPath() %>','<%=params%>')" ></a></td>
                    <td id="goButton" onclick="setValueToContainer('<%=request.getContextPath() %>','<%=ReportId%>','<%=rolesid%>')"><input type="button" value="GO" class="navtitle-hover"></td>
            </tr></table>
            <iframe  id="tableColsFrame" name='tableColsFrame' width="100%" height="100%" frameborder="0"   src='TableDisplay/PbChangeTableColumnsRT.jsp'></iframe>
        </div>
<div id="fadestart" class="black_start"></div>
<div id="kpidispTabProp"  style="display:none">
            <iframe id="kpidispTabPropFrame" NAME='kpidispTabPropFrame' width="100%" height="100%"  frameborder="0" SRC='about:blank'></iframe>
        </div>
<div id="editDBTable"  style="display:none">
           <table style="width: 100%;"><Tr>
                    <Td style="width: -moz-min-content;">Grand Total</Td>
                    <Td>
                        <%if (container.getGrandTotalReqForkpi()) {%>
                        <input  type="checkbox" checked name="GrandTotalReqForkpi" id="GrandTotalReq" value="true" onclick="dispGrandTotalForkpi()">
                        <%} else {%>
                        <input type="checkbox" name="GrandTotalReqForkpi" id="GrandTotalReq" value="false" onclick="dispGrandTotalForkpi()">
                        <%}%>
                    </Td>
               <td style="width: -moz-min-content;">Sub Total</td>
                 <td>  <%if (container.getNetTotalReqForkpi()) {%>
                        <input  type="checkbox" checked name="SubTotalReqForkpi" id="SubTotalReqForkpi" value="true" onclick="dispSubTotalForkpi()">
                        <%} else {%>
                        <input type="checkbox" name="SubTotalReqForkpi" id="SubTotalReqForkpi" value="false" onclick="dispSubTotalForkpi()">
                        <%}%>
               </td></Tr>
           <tr align="center"><td></td><td>
                   <input type="button" name="Submit" value="Done" class="navtitle-hover" onclick="submitEditTablePro()">
               </td></tr></table>
        </div>
<!--<div id="colorsDiv" style="display: none" title="Select color">
                                                  <center>
                                                    <input type="text" id="color" style="" value="#12345" >
                                                  <div id="colorpicker" style=""></div>
                                                  <input type="button" align="center" value="Done" class="navtitle-hover" onclick="saveSelectedColor()">
                                                  <input type="button" align="center" value="Cancel" class="navtitle-hover" onclick="cancelColor()">
                                                  <input type="hidden" id="index" value="">
                                                   <input type="hidden" id="kpiMasterId" value="">
                                                    <input type="hidden" id="PbReportId" value="">
                                                    <input type="hidden" id="dashBoardId" value="">

                                                </center>
                                       </div>         -->
                 <div id="saveNewdashboard" style="display:none">
            <table border="0" style="width:100%;">
                <tr valign="top">
                    <td valign="top"   style="width:40%;">
                        <label class="label">Report Name</label>
                    </td>
                    <td  valign="top"  style="width:60%;">
                        <input type="text" class="inputbox"  maxlength="200" name="reportName1" style="width:80%" id="reportName1" onblur="tabmsg1()" onkeyup="tabmsg1()" onfocus="document.getElementById('save1').disabled = false;">
                        <br>
                        <span id="duplicate1" style="color:red"></span>
                    </td>
                </tr>
                <tr valign="top">
                    <td colspan="">&nbsp;</td>
                </tr>
                <tr valign="top">
                    <td valign="top"  style="width:40%;">
                        <label class="label">Description</label>
                    </td>
                    <td   valign="top" style="width:60%;">
                        <input type="text" class="inputbox"  name="reportDesc1" maxlength="200" id="reportDesc1" style="width:80%;overflow:auto;height:50px">
                    </td>
                </tr>
                <tr valign="top">
                    <td valign="top"  colspan="">&nbsp;</td>
                </tr>
                <tr>

                    <td  valign="top" colspan="2" align="center">

                        <input type="button"  class="navtitle-hover" style="width:auto" value="Next" id="save1" onclick="saveAsNewDashboard('<%=ReportId%>','<%=request.getContextPath()%>');">

                    </td>
                </tr>
            </table>
        </div>
                     <div id="drillToRep" style="display:none" name="Drill To Report" tilte =" ">
                                                                        <form id="DrilltoRepForm" name ="DrilltoRepForm" method="post">
                                                                            <table id="DrillTable" name ="DrillTab">

                                                                                <tbody id="getDrillReports">

                                                                                </tbody>
                                                                                <tfoot>

                                                                                <tr></tr>
                                                                                <tr></tr>
                                                                                <tr></tr>
                                                                                <tr>
                                                                                    <td colspan="4" align="center" rowspan="2">
                                                                                        <input type="button" class="navtitle-hover" style="width:auto" name="Done" value="GO" onclick="DrillToReport()" /><br><br>
                                                                                    </td>
                                                                                </tr>
                                                                                </tfoot>
                                                                               </table>
                                                                            </form>

                                   </div>
                         <div id="editViewByDiv" title="Edit ViewBy" style="display:none">
            <iframe  id="editViewByFrame" name='editViewFrame' width="100%" height="100%" frameborder="0"   src='about:blank'></iframe>
        </div>
                         <div id="editViewByDivkpi" title="Edit ViewBy" style="display:none">
            <iframe  id="editViewByFramekpi" name='editViewFramekpi' width="100%" height="100%" frameborder="0"   src='about:blank'></iframe>
        </div>
                         <div id="getenblevalues" style="display:none"></div>

                         <div id="colorsDiv1" style="display: none" title="Select color">
                                                  <center>
                                                    <input type="text" id="color1" style="" value="#12345" >
                                                  <div id="colorpicker1" style=""></div>
                                                  <input type="button" align="center" value="Done" class="navtitle-hover" onclick="saveSelectedColor1()">
                                                  <input type="button" align="center" value="Cancel" class="navtitle-hover" onclick="cancelColor()">
                                                 <input type="hidden" id="REPORTID" value="">
                                                 <input type="hidden" id="COLUMNID" value="">

                                                </center>
                                       </div>
                         <div id="getGTvalues" style="display:none">
                             <table width="100%">
<!--                                 <Tr>
                    <Td>Grand Total</Td>
                    <Td>
                        <%if (container.getGrandTotalReqForkpi()) {%>
                        <input  type="checkbox" checked name="GrandTotalReqForkpi" id="GrandTotalReq" value="true" onclick="dispGrandTotalForkpi()">
                        <%} else {%>
                        <input type="checkbox" name="GrandTotalReqForkpi" id="GrandTotalReq" value="false" onclick="dispGrandTotalForkpi()">
                        <%}%>
                    </Td>
               <td>Sub Total<td>
                   <%if (container.getNetTotalReqForkpi()) {%>
                        <input  type="checkbox" checked name="SubTotalReqForkpi" id="SubTotalReqForkpi" value="true" onclick="dispSubTotalForkpi()">
                        <%} else {%>
                        <input type="checkbox" name="SubTotalReqForkpi" id="SubTotalReqForkpi" value="false" onclick="dispSubTotalForkpi()">
                        <%}%>
               <td></td>
                </Tr>-->
                <tr><td></td></tr>
                <Tr>
                <Td>AggregationType</Td>
                <Td>
                <select id="AggregationType" name="AggregationType" class="myTextbox5">
                     <option value="SUM">SUM</option>
                     <option value="AVG">AVG</option>
                     <option value="MIN">MIN</option>
                     <option value="MAX">MAX</option>
                     <option value="COUNT">COUNT</option>
                     <option value="COUNTDISTINCT">COUNTDISTINCT</option>
                 </select>
                    </Td>
                    <td><input type="hidden" name="colname" id="colname" value="">
                                     <input type="hidden" name="ctxPath" id="ctxPath" value="">
                                     <input type="hidden" name="dispColName" id="dispColName" value="">
                                     <input type="hidden" name="refelement" id="refelement" value=""></td>
                </Tr> <tr><td></td></tr>
                <Tr><td width="20%"></td><Td><input type="button" name="Done" value="Done" class="navtitle-hover" onclick="updategttype2()"></Td></Tr>
                             </table>
                         </div>
               <!--sorting-->
               <%if(container.isReportCrosstab()){%>
                       <div id="applaySorting" style="display:none">
                           <table width="100%">
                           <tr width="50%" height="45px">
                    <td width="50%" align="center" > Sort On:</td>
                   <td width="%" ><select id="SortMeasure" name="SortMeasure" class="myTextbox5">
   <%
 for (int MsrsLngth = 1; MsrsLngth < DisplayLabelskpi.size(); MsrsLngth++) {
    ArrayList<String> crosstabvales = new ArrayList<String>();
 Object paramObject=null;
  paramObject=DisplayLabelskpi.get(MsrsLngth);

 Type listOfTestObject = new TypeToken<ArrayList>(){}.getType();
                Gson gson = new Gson();
   String  paramObject1=gson.toJson(paramObject, listOfTestObject);
//                 paramObject1=(String) paramObject;

                                    String[] vals= paramObject1.split(",");
                                    for(int i=0; i<vals.length;i++){
                                        String v1=vals[i];
                                        v1=v1.replace("[", "");
                                      crosstabvales.add(v1);
}


        String measureval = crosstabvales.get(1).replace("[", "").replace("\"", "").replace("]", "");
   if(measurevalintialkpi!=null && measurevalintialkpi!="" && measurevalintialkpi.equals(measureval)){

break;
}

if(measurevalintialkpi=="" || measurevalintialkpi==null){
measurevalintialkpi=measureval;
}
        val1kpi=measureval;
 valelementkpi = DisplayColumnskpi.get(MsrsLngth);
%>

 <option value="<%=valelementkpi%>"><%=val1kpi%></option>
     <%}%>
                   </select></td></tr></table>
               </div>
                   <%}%>


                   <div id="getformattingstyles" style="display:none">
                   <table width="100%" height="80%">
                       <tr><td>Font Size</td>
                           <td><select id="FontsizeType" name="FontsizeType" class="myTextbox3"></select>
                           </td>
                       </tr>
                   <tr>
                       <td>Font Style</td>
                       <td><select id="FontstyleType" name="FontstyleType" class="myTextbox3"></select></td>
                   </tr>
                   <tr>
                       <td>Text Align</td>
                       <td><select id="Textalignment" name="Textalignment" class="myTextbox3"></select></td>
                   </tr>
                   <tr>
                       <td><input type="hidden" name="colname" id="colname" value="">
                                     <input type="hidden" name="ctxPath" id="ctxPath" value=""></td> </tr>
                   <Tr><Td align="right"><input type="button" name="Submit" value="Submit" class="navtitle-hover" onclick="formatsubmit()"></Td></Tr>
                   </table>
                   </div>
<div id='loading' class='loading_image' style="display:none;">
                    <img alt=""  id='imgId' src='images/help-loading.gif'  border='0px' style='position:absolute; left: 450px; top: 150px;'/>
                </div>


       <div id="measuresDialog" style="display:none" title="Add Measures">
    <table><tr>
             <%if(istimedash!=null && istimedash.equalsIgnoreCase("timedash")){%>
            <td>
            <input id="tableList" type="checkbox" onclick="getDisplayTablesInDesigner1('<%=request.getContextPath() %>','','<%=ReportId%>')">All</td>
            <%}else{%>
             <td>
            <input id="tableList" type="checkbox" onclick="getDisplayTablesInDesigner('<%=request.getContextPath() %>','','<%=ReportId%>')">All</td>

            <%}%>
                    <td id="tabListDiv" ><input type="textbox" id="tabsListVals"><input type="textbox" style="display:none;" id="tabsListIds">
                        <div id="paramVals" class="ajaxboxstyle" style="display:none;overflow: auto;"></div></td>
                    <td id="tablistLink" ><a href="javascript:void(0)" class="ui-icon ui-icon-note" onclick="showListInDesigner('<%=request.getContextPath() %>','')" ></a></td>
                    <td id="goButton" onclick="setValueToContainerInDesigner('<%=request.getContextPath() %>','<%=ReportId%>')"><input type="button" value="GO" class="navtitle-hover"></td>
            </tr></table>
    <iframe id="dataDispmem" NAME='dataDispmem' frameborder="0" width="100%" height="100%" SRC='#'></iframe>

</div>
            <div id="DimFilterBy" style="display:none;"></div>
            <div id="reportDrillDiv" name="reportDrillDiv" title="Assigning KPI Dashboard To Report Drill">
       <form id="reportDrillFrm" name="reportDrillFrm" action="" method="post"></form>
     </div>
           <form name="searchForm" method="post" style="padding:0pt" action="">

           </form>
           <script type="text/javascript">
                  <%if (isKPIDashboard =="true") {%>
 $("#dateregionDiv").show();
                <%}%>
                    <% if(isKPIDashboard =="true"&& timeinfo.get(1).equalsIgnoreCase("PRG_DATE_RANGE")) { %>
                           $("#dateregionDiv").hide();
                           $("#dateregionRange").show();

                             <%}%>

        document.getElementById('loading').style.display='none';

                </script>
    </body>
</html>
