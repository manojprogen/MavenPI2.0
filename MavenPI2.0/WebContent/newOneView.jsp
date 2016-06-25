<%--
    Document   : newOneView
    Created on : Apr 17, 2015, 1:21:53 PM
    Author     : Sandeep Nagapuri
--%>

<%@page import="java.io.ObjectInputStream"%>
<%@page import="java.io.FileInputStream"%>
<%@page import="com.progen.reportdesigner.db.ReportTemplateDAO"%>
<%@page import="java.io.File"%>
<%@page import="prg.db.OnceViewContainer"%>
<%@page import="com.google.gson.Gson"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.net.InetAddress"%>
<%@page import="com.progen.action.UserStatusHelper"%>
<%@page import="java.util.HashMap"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<jsp:useBean id="duration" scope="session" class="utils.db.ProgenParam"/>

<%
 boolean isPowerAnalyserEnableforUser=false;
 String[] duration1={"Day","Week","Month","Quarter","Year"};
            String[] comparision={"Last Period","Last Year","Period Complete","Year Complete"};
            String[] Aggr={"SUM","MIN","MAX","AVG","COUNT","COUNTDISTINCT"};
     String oneviewname=(String)request.getParameter("oneviewname");
     if(oneviewname.equalsIgnoreCase("null") || oneviewname==null){
         oneviewname=(String) request.getAttribute("oneviewname");
     }
      String oneviewtypedate =(String) session.getAttribute("oneviewdatetype");
     String fromopen=(String)request.getParameter("fromopen");
     String action=(String)request.getParameter("action");
     String data=(String)request.getParameter("data");
      ArrayList viewbys = new ArrayList();
  String oldAdvHtmlFileProps;
            String advHtmlFileProps = null;
//            ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();
            String fileName = null;
            File file=null;
     String fromviewer=(String)request.getParameter("fromviewer");
     String oneViewIdValue=(String)request.getParameter("oneViewIdValue");
      List<String>viewByNamesSeq=new ArrayList<String>();
      List<String>filtervalues=new ArrayList<String>();
      String withglobal="";
      Map<String, List<String>> allFiltersnames = new HashMap<String, List<String>>();
      Map<String, List<String>> FilterMap = new HashMap<String, List<String>>();
      Map<String, List<String>> allFilters = new HashMap<String, List<String>>();
       List<String>viewByIdsSeq=new ArrayList<String>();
        List<String>parameterlist=new ArrayList<String>();
        List<String>viewbynames=new ArrayList<String>();
        List<String>parameternamelist=new ArrayList<String>();
         viewByIdsSeq = (List<String>) session.getAttribute("viewByIdsSeq");
       viewByNamesSeq = (List<String>) session.getAttribute("viewByNamesSeq");
       int oneviewssize=viewByIdsSeq.size();
       allFiltersnames =  ( Map<String, List<String>>) session.getAttribute("allFiltersnames");
       allFilters =  ( Map<String, List<String>>) session.getAttribute("allFilters");
       parameterlist =  (List<String>) session.getAttribute("parameterlist");
       viewbynames =  (List<String>) session.getAttribute("viewbynames");
      ServletContext context = getServletContext();
  UserStatusHelper helper=new UserStatusHelper();
   HashMap<String,UserStatusHelper> statushelper;
     statushelper=(HashMap)context.getAttribute("helperclass");

OnceViewContainer onecontainer1 = null;
           if((fromopen!=null && fromopen.equalsIgnoreCase("gblfilter")) ||( action!=null && action.equalsIgnoreCase("globalftr"))){

               }else{
    if(fromviewer!=null && fromviewer.equalsIgnoreCase("true")){
       String report ="";
        String userId = String.valueOf(request.getSession(false).getAttribute("USERID"));

            String bizzRoleName="";
 oldAdvHtmlFileProps=(String) request.getSession(false).getAttribute("oldAdvHtmlFileProps");
            advHtmlFileProps=(String) request.getSession(false).getAttribute("advHtmlFileProps");
            String isseurity=(String) request.getSession(false).getAttribute("isseurity");
            ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();

             fileName = reportTemplateDAO.getOneviewFileName(oneViewIdValue);

                  fileName=session.getAttribute("tempFileName").toString();
              file = new File(advHtmlFileProps+"/"+fileName);
        if(file.exists()){

                FileInputStream fis2 = new FileInputStream(advHtmlFileProps+"/"+fileName);
                ObjectInputStream ois2 = new ObjectInputStream(fis2);
                onecontainer1 = (OnceViewContainer) ois2.readObject();
                ois2.close();
            }

allFiltersnames=onecontainer1.getallFiltersnames();
allFilters=onecontainer1.getallFilters();
viewbynames=onecontainer1.getviewbygblname();
parameterlist=onecontainer1.getparameterlist();

if(!allFiltersnames.isEmpty() && allFiltersnames != null  ){
withglobal="true";
FilterMap=onecontainer1.getFilterMap();
}
}
    }
 Gson gson = new Gson();
     if(!statushelper.isEmpty()){
        helper=statushelper.get(request.getSession(false).getId());
        if(helper!=null){
       // isPortalEnableforUser=helper.getPortalViewer();

        isPowerAnalyserEnableforUser=helper.getPowerAnalyser();
     //   isOneViewEnableforUser=helper.getOneView();
     //   isScoreCardsEnableforUser=helper.getScoreCards();

        }}
     session.setAttribute("isPowerAnalyserEnableforUser", isPowerAnalyserEnableforUser);
    String themeColor = "blue";
    if (session.getAttribute("theme") == null) {
        session.setAttribute("theme", themeColor);
    } else {
        themeColor = String.valueOf(session.getAttribute("theme"));
    }
String globalvalue="";
%>
<html>
    <style type="text/css">
        #fixedtop1 { position: fixed; top: -5.5%; left:200px; margin: auto; right: 800px; width: 70px; border: none; z-index: 50; }
#center250a { width: auto;height: 65px;  background:none; }
#center250b { width: auto;height: 65px;   background:none; }
        .ui-datepicker {
    padding: 0.2em 0.2em 0;
    width: 27em;
    z-index: 1000000;

}
 #arrowL{width:20px; height:10px;  float:left;cursor:pointer;}
  #arrowR{width:20px;height:10px;float:right;cursor:pointer;}
   #resetgraph{width:40px;height:10px;float:right;cursor:pointer;}
    #list-container {overflow:hidden;width:85vw;height:30px;float:left;}
     .item{ margin:5px; float:left; position:relative;}
    #gottabId11{ width:20px; height:10px; float:right; cursor:pointer; }
            .list1{min-width:15000px; float:left;}
    </style>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">

        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/lib/jquery/js/jquery-1.7.2.min.js"></script>
<!--         <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/lib/jquery/js/jquery-1.7.3.min.js"></script>-->
        <script type="text/javascript" src="<%= request.getContextPath()%>/JS/jquery-ui.min.js"></script>
<!--         <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>-->
        <script type="text/javascript" src="<%=request.getContextPath()%>/JS/oneview/Grid/jquery.gridster.js"></script>

<!--         <link rel="stylesheet" type="text/css" href="css/jquery-ui.css" />-->
        <link type="text/css" href="<%=request.getContextPath()%>/css/d3/tooltip.css" rel="stylesheet"/>

        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/themes/<%=themeColor%>/pbReportViewerCSS.css" rel="stylesheet" />
         <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/pbReportViewerJS.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/reportviewer/ReportViewer.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/TableDisplay/JS/pbTableMapJSForPopUp.js"></script>
        <script src="<%=request.getContextPath()%>/TableDisplay/JS/pbDisplayJS.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/jQuery/d3/customtooltip.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/jQuery/d3/d3.v3.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/jQuery/d3/colpick.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/jQuery/d3/d3.layout.cloud.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/jQuery/d3/chartTypes.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/jQuery/d3/chartTypesGroup.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/jQuery/d3/chartTypeOthers.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/jQuery/d3/chartTypeBars.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/jQuery/d3/chartTypeCircular.js"></script>
          <script type="text/javascript" src="<%=request.getContextPath()%>/jQuery/d3/chartTypeLine.js"></script>
         <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/reportviewer/graphViewer.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/OneViewJS.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/JS/d3.geo.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/JS/d3.geo.tile.js.js"></script>
        <script type="text/javascript" src="<%= request.getContextPath()%>/JS/dragAndDropTable.js"></script>
<!--        <script type="text/javascript" src="<%= request.getContextPath()%>/JS/jquery-ui.min.js"></script>-->
<!--        <link href="<%=request.getContextPath()%>/css/d3/xtendChart.css" rel="stylesheet" type="text/css" />-->
        <script type="text/javascript" src="<%= request.getContextPath()%>/JS/oneview/jquery.multiselect.js"></script>
        <script type="text/javascript" src="<%= request.getContextPath()%>/JS/jquery.multiselect.filter.js"></script>
<!--       <link href="<%=request.getContextPath()%>/css/d3/xtendChart.css" rel="stylesheet" type="text/css" />-->
        <link rel="stylesheet" type="text/css" href="css/normalize.css"/>
        <link rel="stylesheet" type="text/css" href="css/tabs.css"/>
        <link rel="stylesheet" type="text/css" href="css/tabstyles.css"/>
        <link rel="stylesheet" type="text/css" href="css/menuTab.css"/>
        <link rel="stylesheet" type="text/css" href="css/d3/colpick.css"/>
        <link rel="stylesheet" type="text/css" href="css/animatedMenu.css"/>
        <link rel="stylesheet" type="text/css" href="css/Grid/jquery.gridster.css" />

             <link rel="stylesheet" type="text/css" href="css/slide.css"/>
             <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/themes/blue/jquery-ui-1.7.3.custom.css" rel="stylesheet" />
             <link type="text/css" href="<%=request.getContextPath()%>/datedesign.css" rel="stylesheet"/>
               <link type="text/css" href="<%=request.getContextPath()%>/jQuery/jquery/themes/base/ui.all.css" rel="stylesheet" />
        <link type="text/css" href="<%=request.getContextPath()%>/jQuery/jquery/themes/base/ui.theme.css" rel="stylesheet" />
        <link type="text/css" href="<%=request.getContextPath()%>/jQuery/jquery/demos.css" rel="stylesheet" />
        <link type="text/css" href="<%=request.getContextPath()%>/css/css.css" rel="stylesheet" />
        <link type="text/css" href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet" />
        <link rel="stylesheet" type="text/css" href="css/jquery.multiselect.css" />
             <link rel="stylesheet" type="text/css" href="css/jquery.multiselect.filter.css" />
               <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/themes/<%=themeColor%>/TableCss.css" rel="stylesheet" />
<!--        <link rel="stylesheet" type="text/css" href="css/jquery-ui.css" />-->
        <script type="text/javascript">
//        var  gridster5=[];
    $(function(){
 $("#customMsrdate1").datepicker({
                        changeMonth: true,
                        changeYear: true,
                        showButtonPanel: true,
                        numberOfMonths: 1,
                        stepMonths: 1
                    });
                   $("#customMsrdate").datepicker({
                        changeMonth: true,
                        changeYear: true,
                        showButtonPanel: true,
                        numberOfMonths: 1,
                        stepMonths: 1
                    });



    });
var oneViewName='<%=oneviewname%>';
var oneviewid='<%=oneViewIdValue%>';
var fromopen='<%=fromopen%>';
var fromviewer='<%=fromviewer%>';
var action='<%=action%>';
var filterData = {};
var value;
var filterMap = {};
var filterDisp=[];
var globalgrid;
var onecontainersave;
var action1='open';
// var defultdate;
//            var  datetype;
             var oneviewtypedate1='';
<%
 if((withglobal!=null && withglobal.equalsIgnoreCase("true"))){%>
parent.$("#filterdata").val('<%=gson.toJson(allFilters)%>');
   <%  } %>

//        added by Manik Srivastava
    function displayFilt(filName){
   var filName1=filName.replace("1q1", " ");
     if(filName1==""){
         filName1=filName;
     }
      $("#"+filName).multiselect({
   selectedText: "# of # selected",
   noneSelectedText: filName1,
   selectedList: 3
}).multiselectfilter();
    }
          $(document).ready(function() {
              $("#customMsrdate").datepicker({
                        changeMonth: true,
                        changeYear: true,
                        showButtonPanel: true,
                        numberOfMonths: 1,
                        stepMonths: 1
                    });
              oneviewtypedate1='<%=oneviewtypedate%>'
              parent.oneviewtypedate1='<%=oneviewtypedate%>'
//        added by Manik Srivastava
$("#loading").show();
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
parent.$("#showNameId").css("display", "none");
//$("#headDivTable").css("display", "none");
//$("#headDivTable").css({'display':'none'})
//$("#headDivTable").hide();
<%
if((fromopen!=null && fromopen.equalsIgnoreCase("gblfilter")) ||( action!=null && action.equalsIgnoreCase("globalftr")) || (withglobal!=null && withglobal.equalsIgnoreCase("true"))){
if(allFiltersnames!=null ){
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
                 key15 = key15.replace(" ", "1q1");
                                          %>
displayFilt('<%=key15%>');
<%}}}}
if (parameterlist != null) {
                 for (int j = 0; j < parameterlist.size() ; j++) {%>
                 parent.filterMapgraphs["<%=parameterlist.get(j)%>"]=new Array();  <%}
    }
}%>
      parent.$("#filters1").val('<%=gson.toJson(FilterMap)%>');
 $( "#dateId1" ).datepicker({
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
                     a=($("#dateId1").val());
                    var dateArr2=new Array()
                    dateArr2=a.split(",");
                     if(a!=""){
                    $("#pfield1").html(dateArr2[2]+"'"+dateArr2[3].substring(2))
                    $("#pfield2").html(dateArr2[1])
                    $("#pfield3").html(dateArr2[0])
                             }
                }


                    });
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
                    var dateArr1=new Array()
                    dateArr1=a.split(",");
                     if(a!=""){
                    $("#tdfield1").html(dateArr1[2]+"'"+dateArr1[3].substring(2))
                    $("#tdfield2").html(dateArr1[1])
                    $("#tdfield3").html(dateArr1[0])
                }
                }

            });

                $("#stdate").datepicker({
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
                     a=($("#stdate").val());
                 var dateArr=new Array()
                    dateArr=a.split(",");
                     if(a!=""){
                    $("#stfield1").html(dateArr[2]+"'"+dateArr[3].substring(2))
                    $("#stfield2").html(dateArr[1])
                    $("#stfield3").html(dateArr[0])
//                    alert(dateArr[2])
                }
                }
                    });
//                    var dateArr3=new Array()
                $("#comparefrom").datepicker({
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
                     a=($("#comparefrom").val());
                 var dateArr=new Array()
                    dateArr=a.split(",");
                     if(a!=""){
                    $("#cffield1").html(dateArr[2]+"'"+dateArr[3].substring(2))
                    $("#cffield2").html(dateArr[1])
                    $("#cffield3").html(dateArr[0])
//                    alert(dateArr[2])
                }
                }
                    });
                     $("#compareto").datepicker({
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
//                    alert(dateArr[2])
                }
                }
                    });
 $("#dateId").datepicker({
                        changeMonth: true,
                        changeYear: true,
                        showButtonPanel: true,
                        numberOfMonths: 1,
                        stepMonths: 1
                    });
                    $("#customMsrdate").datepicker({
                        changeMonth: true,
                        changeYear: true,
                        showButtonPanel: true,
                        numberOfMonths: 1,
                        stepMonths: 1
                    });
var widt=parseInt((((($(window).width()))-35)/4)/8);
 parent.gridster5 = $(".gridster > ul").gridster({
        widget_margins: [2, 2],
//        autogenerate_stylesheet: false,
        widget_base_dimensions: [widt,widt],
        min_cols: 29,
        resize: {
            enabled: true,
            axes:['both'],
            handle_class:'gs-resize-handle',
            start: function (e, ui, $widget) {
            },
            stop: function (e, ui, $widget) {
                var newHeight = this.resize_coords.data.height;
                var newWidth = this.resize_coords.data.width;
//                 gridResize(newHeight, newWidth);
//      alert("id  "+($($widget).attr('id')).replace("div","" ))
                var chartId=$($widget).attr('id').replace("div","" );
      //added by Ram
                var length=chartId.length;
                var regid = chartId.replace("chart","");
                  generateJsonDataNewOneview(chartId,oneviewid,regid,"localrefresh",fromviewer);
                 // localRefresh(chartId);
       }
        }
    }).data('gridster');
    globalgrid=parent.gridster5

     $.post('<%=request.getContextPath()%>/oneViewAction.do?templateParam2=getContainerFromSession&oneViewIdValue='+oneviewid,
                function(data){

        var jsonVar=eval('('+data+')');
                        $("#assignDiv").html("")
            parent.datetype=jsonVar.datetype
            var  timedetails=jsonVar.timedetails
              var  timedetails1=jsonVar.timedetails1
              var  durationvale=jsonVar.durationvale
                if(oneviewtypedate1=='false'){

              var  timedetailscf=jsonVar.timedetailscf
              var  timedetails1ct=jsonVar.timedetails1ct

            if(timedetailscf='"'){
 timedetailscf=timedetails;
                  timedetails1ct=timedetails1;
            }
                }
 parent.datetype=parent.datetype[0].replace("[","").replace("]","");
 if(parent.datetype=='PRG_STD'){
   $("#fdatetdspan").hide();
    $("#fdatetdcal").hide();
    $("#totd").hide();
     $("#tdatetdspan").hide();
     $("#tdatetdcal").hide();
      $("#timedetailsId1").show();
                $("#compareWithId1").show();
                $("#sdatetdspan").show();
                $("#sdatetdcal").show();

for(var i=0;i<timedetails.length;i++){
    var day=timedetails[0].replace("[","").replace("]","");
var date=timedetails[1].replace("[","").replace("]","");
var year=timedetails[2].replace("[","").replace("]","");
 $("#stfield1").html(year)
                    $("#stfield2").html(date)
                    $("#stfield3").html(day)

                 $("#defvaluedate").val(timedetails);
}
 var durationvale = durationvale[0].replace("[","").replace("]","");
                        var compareValue = durationvale[1].replace("[","").replace("]","");
$('#timedetailselectId1 option[value="'+durationvale.toString()+'"]').attr('selected','selected');
                        $('#compareSelectId1 option[value="'+compareValue.toString()+'"]').attr('selected','selected');
 }else if(parent.datetype=='PRG_DATE_RANGE'){
      $("#sdatetdspan").hide();
     $("#sdatetdcal").hide();
      $("#timedetailsId1").hide();
               $("#compareWithId1").hide();
      $("#fdatetdspan").show();
      $("#fdatetdcal").show();
      $("#totd").show();
      $("#tdatetdspan").show();
      $("#tdatetdcal").show();
      if(oneviewtypedate1=='false'){
          $("#compare").show();
 $("#cfdatetdspan").show();
      $("#cfdatetdcal").show();
      $("#totd1").show();
      $("#ctdatetdspan").show();
      $("#ctdatetdcal").show();
      }

for(var i=0;i<timedetails.length;i++){
    var day=timedetails[0].replace("[","").replace("]","");
var date=timedetails[1].replace("[","").replace("]","");
var year=timedetails[2].replace("[","").replace("]","");
 $("#pfield1").html(year)
                    $("#pfield2").html(date)
                    $("#pfield3").html(day)
    var day1=timedetails1[0].replace("[","").replace("]","");
var date1=timedetails1[1].replace("[","").replace("]","");
var year1=timedetails1[2].replace("[","").replace("]","");
 $("#tdfield1").html(year1)
                    $("#tdfield2").html(date1)
                    $("#tdfield3").html(day1)
                     $("#defvaluedate").val(timedetails);

                       if(oneviewtypedate1=='false'){
    var daycf=timedetailscf[0].replace("[","").replace("]","");
var datecf=timedetailscf[1].replace("[","").replace("]","");
var yearcf=timedetailscf[2].replace("[","").replace("]","");
 $("#cffield1").html(yearcf)
                    $("#cffield2").html(datecf)
                    $("#cffield3").html(daycf)
                     $("#defvaluedate").val(timedetails);
    var dayct=timedetails1ct[0].replace("[","").replace("]","");
var datect=timedetails1ct[1].replace("[","").replace("]","");
var yearct=timedetails1ct[2].replace("[","").replace("]","");
 $("#ctfield1").html(yearct)
                    $("#ctfield2").html(datect)
                    $("#ctfield3").html(dayct)
                     $("#defvaluedate").val(timedetails);

                       }
}

parent.defultdate=timedetails1;
}

  $("#goTabId1").show();
  $("#dateToggle").show();

                        });
//          }
//  gridster1.generate_stylesheet({rows: 78, cols: 29});
// gridster5.add_widget("<li>1</li>",4,3,1,1)
//  alert("grid  "+parent.gridster5);
   });
  $('.menu-item a').click(function(){
               $('.menu-item a').removeClass('active');
               $(this).addClass('active');
           });

function allowDrop(ev)
{
ev.preventDefault();
}

function drop(ev)
{
var tData = ev.dataTransfer.getData("Text");
ev.preventDefault();
VtagDivId=ev.target.id.match(/\d/g);
if(VtagDivId==null){
VtagDivId=1;
}

 var gridsPos=parent.gridster5.serialize1();
 var regid=gridsPos.length;
 if(gridsPos.length>0){
 var regid1=gridsPos[regid-1]["id"];
regid1=regid1.replace("divchart","");
regid=parseInt(regid1)+1;
 }else{
 regid=regid+1;
 }
  parent.$("#oneviewVersion").val("2.5");
//alert(parent.oneViewIdValue)
//gridster5.add_widget("<li>1</li>",4,3,1,1)
// if(isviewr=='viewer'){
//    VoneViewId=oneviewID;
var chartid=parent.chartid;
 generateJsonDataOneview(chartid,oneviewid,regid,oneViewName,"add",parent.gridster5,fromviewer);
//getoneviewlets(oneViewName,oneviewID,VrepId,VrepGraphId,VrepGraphName,Vgrpno,VbusRoleId,VoneViewId,VtagDivId,measurename1,mid1,regionName,isviewr);
// }else{
////createdynamicDivRegion(oneViewName,oneViewIdValue,VrepId,VrepGraphId,VrepGraphName,Vgrpno,VbusRoleId,VoneViewId,VtagDivId,measurename1,mid1,regionName,isviewr);
// }


}
function setallfiltersone(elname,elmentid,flag,type){
     var filterValues=[];
//     var filterMap = {};
       elname=elname.replace("1q1", " ");
       elname=elname.replace("1q1", " ");
      if(typeof $("#filters1").val()!=="undefined" && $("#filters1").val()!==""){
        filterMap = JSON.parse($("#filters1").val());
    }
    filterData= JSON.parse(parent.$("#filterdata").val());
 elname=elname.replace("1q1", " ");
//     delete filterMap[elmentid];
 if(flag!=null && flag=='uncheckall'){
filterMap[elmentid] = filterValues;
 }else{
       filterValues=filterData[elmentid];
 }
         delete filterMap[elmentid];
         parent.$("#filters1").val(JSON.stringify(filterMap));

}

function applyGlobalFilterinOneview(id,nameArr,chekid){
   $("#loading").show();
   var filterValues=[];
    //        var filterKeys = Object.keys(filterData);
//    var filterMap = {};
//    var filterValues=[];
    var name ,name1;
     parent.$("#filters1").val(JSON.stringify(filterMap))
    name = nameArr.split("*,")[1];
    var view = parent.$("#globalviewby").val();
    var viewIds = parent.$("#globalviewbyIds").val();

    filterData= JSON.parse(parent.$("#filterdata").val())
    if(typeof parent.$("#filters1").val()!=="undefined" && parent.$("#filters1").val()!==""){
        filterMap = JSON.parse(parent.$("#filters1").val());
        if(typeof filterMap[name]!=="undefined"){
            filterValues=filterMap[name];
        }
    }


    if(document.getElementById(chekid).checked){
        filterValues.push(filterData[name][id.split("_")[1]]);
        filterMap[name] = filterValues;
    }
    else{
        var index = filterValues.indexOf(filterData[name][id.split("_")[1]]);
        filterValues.splice(index, 1);

    }
    filterMap[name] = filterValues;
    parent.$("#filters1").val(JSON.stringify(filterMap));
// alert( parent.$("#filters1").val())
}
function open1view(viewname,viewId,numi){
    var  id = new Array();
    id  = viewId.toString().split(",");
   var oneviewId1=id[numi].replace("]", "", "gi").replace("[", "", "gi").replace(" ", "", "gi")
    parent.viewOneBy(viewname,oneviewId1);

}

        </script>

        <style class="cp-pen-styles">
            .closed span a{
               color:white;
            }
            .ui-datepicker {
    padding: 0.2em 0.2em 0;
    width: 27em;
    z-index: 1000000;
}

            .container{
                /*margin-left: 2%;*/
                width: 100%;
            }
            .dragClass {
            font-family: Tahoma, sans;
            color: black;
            background: white;
            border: 1px solid skyblue;
            padding: 0px;

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
        </style>
    <body style="background-color: white;" >
       <div class="cbp-spmenu cbp-spmenu-horizontal cbp-spmenu-top" id="cbp-spmenu-s3">
			<h3></h3>
			<a href="#"></a>
			<a href="#"></a>
			<a href="#"></a>
			<a href="#"></a>
			<a href="#"></a>
			<a href="#"></a>
			<a href="#"></a>
			<a href="#"></a>
			<a href="#"></a>
			<a href="#"></a>
			<a href="#"></a>
			<a href="#"></a>
           </div>
        <table id="tabel1" class="container" >
            <tr>
                <td valign="top" style="width:100%;">
                    <div class="container"  style="background-color: white;padding-top: 1%">
                <jsp:include page="Headerfolder/headerpageone.jsp"/>
                </div>
                <div id="fixedtop1"style="" > <div id="center250b">

                                                            <div class="form clearFix">
                                                                <span class="wr100">
                                                                    <table align="center" id="roundtrip">
                                                                   <tr width="100%">
                                                                       <td id="fdatetdspan" style="display: none;">
        <span id="depShow">
        <span class="top w100 mrtop" id="pfield1"></span>
        <span class="date">
        <small Style="font-weight: bold" id="pfield2"></small></span>
        <span class="bottom w100" id="pfield3"></span></span>
                    </td>

                   <td id="fdatetdcal"style="display: none;"> <input height="100px" width="100px" type="hidden" class="datepicker" id="dateId1"  name="dateId1" onclick="showdate()" /></td><td width="10%"></td> &nbsp;&nbsp;&nbsp;&nbsp;


          <td  id="totd"align="left" style="white-space:nowrap ; display: none;width: auto;padding-left: 1.5em">To :</td>
         <td id="tdatetdspan" style="display: none">
        <span class="top w100 mrtop" id="tdfield1"></span>
        <span class="date">
        <small Style="font-weight: bold" id="tdfield2"></small></span>
        <span class="bottom w100" id="tdfield3"></span>
                    </td>
                   <td id="tdatetdcal"style="display: none;"> <input height="100px" width="100px" type="hidden" class="datepicker" id="todate"  name="todate" onclick="showdate()" /></td><td width="10%"></td> &nbsp;&nbsp;&nbsp;&nbsp;
                   <td id="compare" align="right" style="font-weight:bold ;display: none; width: auto;padding-left: 1.5em"> COMPARE</td>
                    <td align="right" style="width: auto;padding-left: 1.5em"></td>
                  <td id="cfdatetdspan" style="display: none;">
        <span class="top w100 mrtop" id="cffield1"></span>
        <span class="date">
        <small Style="font-weight: bold" id="cffield2"></small></span>
        <span class="bottom w100" id="cffield3"></span>
                    </td>
                   <td id="cfdatetdcal"style="display: none;"> <input height="100px" width="100px" type="hidden" class="datepicker" id="comparefrom"  name="comparefrom" onclick="showdate()" /></td><td width="10%"></td> &nbsp;&nbsp;&nbsp;&nbsp;
          <td  id="totd1"align="left" style="white-space:nowrap ; display: none;width: auto;padding-left: 1.5em">To :</td>
         <td id="ctdatetdspan" style="display: none">
        <span class="top w100 mrtop" id="ctfield1"></span>
        <span class="date">
        <small Style="font-weight: bold" id="ctfield2"></small></span>
        <span class="bottom w100" id="ctfield3"></span>
                    </td>
                   <td id="ctdatetdcal"style="display: none;"> <input height="100px" width="100px" type="hidden" class="datepicker" id="compareto"  name="compareto" onclick="showdate()" /></td><td width="10%"></td> &nbsp;&nbsp;&nbsp;&nbsp;
         <td id="sdatetdspan"style="display: none"><span id="depShow1">
        <span class="top w100 mrtop" id="stfield1"></span>
        <span class="date">
        <small Style="font-weight: bold" id="stfield2"></small></span>
        <span class="bottom w100" id="stfield3"></span></span>
                    </td>
                   <td id="sdatetdcal"style="display: none;"> <input height="100px" width="100px" type="hidden" class="datepicker" id="stdate"  name="stdate" onclick="showdate()" /></td><td width="10%"></td> &nbsp;&nbsp;&nbsp;&nbsp;


                    <td id="timedetailsId1" style="display: none;padding-left: 1.5em;border:large;" width="5%">
                        <select id="timedetailselectId1" style="border: 1px solid black">
                            <option id="Day" value="Day">Day</option>
                            <option id="Week" value="Week">Week</option>
                            <option id="Month" value="Month">Month</option>
                            <option id="Quarter" value="Quarter">Quarter</option>
                            <option id="Year" value="Year">Year</option>
                        </select>
                    </td><td width="3%"></td>  &nbsp;&nbsp;&nbsp;
                    <td id="compareWithId1" style="display: none ; padding-left: 1.5em;border:large;" width="5%">
                        <select id="compareSelectId1" style="border: 1px solid black">
                            <option id="Last Period" value="Last Period">Last Period</option>
                            <option id="Last Year" value="Last Year">Last Year</option>
                            <option id="Period Complet" value="Period Complete">Period Complete</option>
                            <option id="Year Complet" value="Year Complete">Year Complete</option>
                        </select>
                    </td><td width="3%"></td>  &nbsp;&nbsp;&nbsp;
<td id="dateToggle" style="padding-left: 1.5em;display: none;"><a class="ui-icon ui-icon-transferthick-e-w" onclick="oneviewdatetoggle(oneviewID)" title="Toggle" ></a></td>

                    <td id="goTabId1" style="display: none; padding-left: 1.5em" ><input type="button" name=""  class="navtitle-hover" id="gottabId1" style='width:25px' value="Go" onclick="gotForOneviewcharts('Go',parent.gridster5)"></td>
            </tr>
        </table>
                                                                </span>
 </div>


                                                       </div></div>
                    </td>
            </tr>
            <tr id="hideefilters" style="display:none" ></tr>

<!--            <tr>
        <div id="header21 " class="container" style="border: 1px solid #000000;height:21px;background-color: rgb(86, 86, 86)">
            <font size="4" face="verdana" color="black"><strong style="font-size: larger; color:#fff; margin-left: 2%;float: left"><%=oneviewname%></strong></font>
added by manik for Search
<form id="searchbox3" action="Search.jsp" target="_blank" onsubmit=" return checkInputValue()" name="myform" style="float: left">
                              <input id="submit3" style="float: right" type="submit" value="Search"  >
                                  <input id="search3" style="float: right" type="text" name="data" value="" placeholder="Please enter the Search criteria  eg: Enrollment" autocomplete="off" >
                        </form>
        </div>
        </tr>-->
<tr><td>
           <div id="header21 " class="container" style="height:30px;background-color: #006699">
 <font size="4" face="verdana" color="black"><strong style="font-size: larger; color:#fff; margin-left: 2%;float: left; padding-top: 8px;"><%=oneviewname%></strong></font>
 <div style="width: 2%;margin-left: 92.5%;">

      <a   style="margin-left: 10px;" onclick="oneviewSelect()"> <img alt="" border="0px"  width="22px" height="22px"  src="<%=request.getContextPath()%>/icons pinvoke/Tasks_list_16.png"/> </a>
 <ul class="dropdown-menu" class="drpcontent" id="oneviewlist" style="display: none; top: unset; left: auto; position: absolute; margin-left: -70px;">
<%for(int j=0; j<oneviewssize; j++){
   String[] id  = viewByIdsSeq.toString().split(",");
                    String[]  name = viewByNamesSeq.toString().split(",");
                    String enmae=name[j].replace("[", "").replace("]", "");
                    String edval=id[j].replace("[", "").replace("]", "").replace(" ", "");
                    String function="openoneview('" + enmae + "','" + edval + "')";
    %>
     <li >

                            <a data-color='color1' onclick="openoneview('<%=enmae%>','<%=edval%>')" style='padding:10px 0px 0px 24px;' ><%=name[j].toString().replace("[", "").replace("]", "")%></a>
  </li>
  <%}%>
       </ul>
 </div>
 <div style="width: 2%;margin-left: 94.5%;margin-top:-1.9%;">

      <a  id="lmOption" style="margin-left: 10px;" onclick="optionSelect()"> <img alt="" border="0px"  width="25px" height="25px"  title="Options" src="<%=request.getContextPath()%>/images/icons/opt.png"/> </a>
 <ul class="dropdown-menu" class="drpcontent" id="themeselect2" style="display: none; top: unset; left: auto; position: absolute; margin-left: -70px;">


       <%if(fromopen!=null && fromopen.equalsIgnoreCase("true")||action!=null && action.equalsIgnoreCase("save")||fromopen!=null && fromopen.equalsIgnoreCase("gblfilter")){%>
                         <li >

                            <a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='saveOneVIewRegnew(oneviewid,oneViewName,parent.gridster5,value)' >Save Oneview</a>
<!--                            <a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='parent.saveOneVIewReg(parent.gridster5)' >Save Oneview</a>-->

                        </li>
                         <li >

                            <a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='parent.copyOneView(oneviewid,oneViewName)'>Save As New Oneview</a>
<!--                            <a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='parent.saveOneVIewReg(parent.gridster5)' >Save Oneview</a>-->

                        </li>
    <li >

  <a data-color='color1' style='padding:10px 0px 0px 24px;' onclick="parent.applyGlobalFilter('<%=request.getContextPath()%>',oneviewid,oneViewName)">Global Filters</a>

                        </li>
                        <li >

  <a data-color='color1' style='padding:10px 0px 0px 24px;' onclick="parent.viewOneBy(oneViewName,oneviewid)" >Reset</a>

                        </li>

                        <%}else{%>
                        <li >

                            <a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='parent.saveOneview(parent.gridster5,value)' >Save Oneview</a>

                        </li>
                            <li >

  <a data-color='color1' style='padding:10px 0px 0px 24px;' onclick="parent.applyGlobalFilter('<%=request.getContextPath()%>',oneviewid,oneViewName)">Global Filters</a>

                        </li>
                        <li >

  <a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='refreshoneview(oneviewid,parent.gridster5,value,"designrfresh")'>Refresh</a>

                        </li>

                        <%}%>


                    </ul>
 </div>
                        <div style="width: 1%;margin-left: 97%;;margin-top:-1.7%;">
                                  <a  id="Reports" style="margin-left: 10px;background-color:#EBEEF5;"  class ="ui-icon ui-icon-circle-triangle-s" onclick='parent.createfixedDivRegion(oneViewName,oneviewid)' ></a>
                                  </div>
<!--<div style="width: 1%;margin-left: 96%;margin-top:-1.2%;">
                            <img id="globalFilter" href="javascript:void(0)"  title="globalFilter" src="<%=request.getContextPath()%>/images/filter.png"  onclick="parent.applyGlobalFilter('<%=request.getContextPath()%>',oneviewid,oneViewName)" style="cursor: pointer;background-color:#EBEEF5; ">
            </div>-->

           </div>
<!--            <div  class="container" style="height:25px;">
        <div id="Tabdiv" class=""  style="height:auto" >
            <ul class="navbar color2" style="width:100%;background-color: white" >
                <%
                    String val = "";
                    int loop=0;
                    int loopstart=0;
                    if (!viewByIdsSeq.isEmpty()) {
                    // for (int i = (viewByIdsSeq.size()-4); i < (viewByIdsSeq.size()-1); i++) {
                        if(viewByIdsSeq.size()>3){
                        loop=(viewByIdsSeq.size()-1);
                        loopstart=(viewByIdsSeq.size()-4);
                        }else{
                        loop=viewByIdsSeq.size();
                        loopstart=0;
                        }
                     for (int i = loopstart; i < loop; i++) {
                   if (viewByNamesSeq.get(i).length() < 22) {
                    val = viewByNamesSeq.get(i);
                   } else {
                    val = viewByNamesSeq.get(i).substring(0, 19)+"..";
                    }
   if(!val.equalsIgnoreCase(oneviewname)){
   %>
                  <li class="menu-item " style="width:15%;z-index:00000;"><a  onclick="open1view('<%=val%>','<%=viewByIdsSeq%>','<%=i%>')" style="padding: 1px;"><span><font size="3" face="verdana" style="padding:0px 5px 0px 10px;font-size: 14px; color: white"><%=val%></font></span></a></li>
                     <%
                                         }}
}%>
<li class="menu-item " style="width:15%;z-index:00000;"><a class="active" style="padding: 1px;"><span><font size="3" face="verdana" style="padding:0px 5px 0px 10px;font-size: 14px; color: white"><%=oneviewname%></font></span></a></li>
<li class="menu-item " style="width:7%;z-index:00000;"><a onclick="parent.getRemainingOneViews('<%=oneviewssize%>','<%=viewByNamesSeq%>','<%=viewByIdsSeq%>')" style="cursor: pointer ;padding: 1px;"><span><font size="3" face="verdana" style="padding:0px 5px 0px 10px;font-size: 14px; color: white">More...</font></span></a></li>
                <li id="testCase" class="drpdown menu-item" style="width: 15%;"><a href="#" style="padding: 1px;width:100%"><span><font size="3" face="verdana" style="font-size: 14px; color: white">Options</font></span></a>
                    <ul class="drpcontent" id="themeselect" style="z-index:99999;">
                        <li style='text-align'>
                            <a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='dispChangeTableColumns()' >Add Region</a>
                        </li>
                        <%if(fromopen!=null && fromopen.equalsIgnoreCase("true")||action!=null && action.equalsIgnoreCase("save")||fromopen!=null && fromopen.equalsIgnoreCase("gblfilter")){%>
                         <li >

                            <a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='saveOneVIewRegnew(oneviewid,oneViewName,parent.gridster5,value)' >Save Oneview</a>
                            <a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='parent.saveOneVIewReg(parent.gridster5)' >Save Oneview</a>

                        </li>
                         <li >

                            <a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='parent.copyOneView(oneviewid,oneViewName)'>Save As New Oneview</a>
                            <a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='parent.saveOneVIewReg(parent.gridster5)' >Save Oneview</a>

                        </li>

                        <%}else{%>
                        <li >

                            <a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='parent.saveOneview(parent.gridster5)' >Save Oneview</a>

                        </li>
                        <%}%>
                        <li >

  <a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='refreshoneview(oneviewid,parent.gridster5,value)'>Refresh</a>

                        </li>


                    </ul>       </li>
<ul style="float:right;margin-left: 88%;margin-top: -1.3%;position: relative">
                    <li class ="ui-icon ui-icon-circle-triangle-s" title="Reports" style="background-color: white; float: right;" onclick='parent.createfixedDivRegion(oneViewName,oneviewid)' >
                            <img src="images/sceduler.png" title="Schedule Graph" onclick="scheduleGO()" style="margin-top: 4px;cursor: pointer ">


                    </li>
                    <li style="background-color: white; float: right;"  >
                            <img id="globalFilter" href="javascript:void(0)"  title="globalFilter" src="<%=request.getContextPath()%>/images/filter.png"  onclick="parent.applyGlobalFilter('<%=request.getContextPath()%>',oneviewid,oneViewName)" style="cursor: pointer ">


                    </li>
                    <li style="background-color: white; float: right;"  >
                            <img id="moreoneview" href="javascript:void(0)" title="More"  src="<%=request.getContextPath()%>/images/moreIcon.png"  onclick="parent.getRemainingOneViews('<%=oneviewssize%>','<%=viewByNamesSeq%>','<%=viewByIdsSeq%>')" style="cursor: pointer ">


                    </li>
                                           <td><a onclick="applyGlobalFilter('<%=request.getContextPath()%>',oneViewId,oneviewName)" id="globalFilter" href="javascript:void(0)" style="display: none;" title="Global Filter"><img alt="Filters" src="<%=request.getContextPath()%>/images/filter.png" /></a></td>

                </ul>

            </ul>
        </div>
    </div>-->
    </td></tr>
 <tr><td>
         <%
 if((fromopen!=null && fromopen.equalsIgnoreCase("gblfilter")) ||( action!=null && action.equalsIgnoreCase("globalftr")) || (withglobal!=null && withglobal.equalsIgnoreCase("true"))){
if(allFiltersnames!=null ){%>
    <div id="rightDivone" class="" style="">
        <div id="arrowL" style="">
            <span id="prev" style=""><img   style="height:25px;" src="<%=request.getContextPath()%>/images/_arrow-left-.png" /></span>
<!--                      <span id="next11" style="" ><img title="Click for next reports" style="height:20px;" src="<%=request.getContextPath()%>/images/_arrow-right-.png" /></span>-->
        </div>
        <div id="arrowR" style="">
            <span id="next1" style="" ><img  style="height:25px;margin-left: -30px"  src="<%=request.getContextPath()%>/images/_arrow-right-.png" /></span>
        </div>
        <div id="resetgraph" style="margin-top:0.3%;">
          <img title="Reset"  style=' background:white;height:20px; border-radius:10px ;margin-left: -20px'  onclick='generateJsonDataReset(parent.$("#graphsId").val())' src="<%=request.getContextPath()%>/images/refersh_image.png" />
<!--           <span id="" style=""><a onclick='generateJsonDataReset(parent.$("#graphsId").val())' style="margin-left: -30px"> Reset </a></span>-->
        </div>
        <div id="gottabId11" style="margin-top:0.3%;">
            <input id='gottabId1' class='navtitle-hover' type='button'  value='Go' onclick='gotforoneviewgbl(oneviewid,oneViewName,parent.gridster5,value)' style='width:25px; background:white; border-radius:10px ;margin-left: -40px'>
        </div>

        <div id="list-container" style="margin-top: 5px;">
                                        <div  id="updaterowgraph" class='list1'>

<%

 StringBuilder stringbuilder = new StringBuilder();

    Set keySet1 = allFiltersnames.keySet();
  List<String>filterids=new ArrayList<String>();

                            String key1;
                            int i1=0;
 int setgblflag = 0;
                             Set keySet = allFilters.keySet();
                            Iterator itr = keySet.iterator();
                            String key;
                                for(int j1=0; j1<viewbynames.size();j1++){
                                 List<String>parameterlistNames=new ArrayList<String>();
                                  Iterator itr1 = keySet1.iterator();
                                if (setgblflag == j1) {
                                String idgbl = "globalfilterrow" + setgblflag;
                %>
                <div  id="<%=idgbl%>" class='item' style="vertical-align: top;margin-top: -2px;margin-left: 10px;">
                    <%setgblflag = setgblflag + 6;
                        }
                           while(itr1.hasNext()){
                                key1 = itr1.next().toString();

                               if(key1.equalsIgnoreCase(viewbynames.get(j1))){
                                   key1=key1.toString().replace(" ","1q1");
        %>
            <select name="<%=parameterlist.get(i1)%>" id=<%=key1%> multiple style="">

                                  <% filtervalues=  allFiltersnames.get(viewbynames.get(j1));

                            for(int j=0; j<filtervalues.size();j++){
                                 parameterlistNames.add(filtervalues.get(j));
                                }

   String viewgblid=parameterlist.get(i1);
   List<String>filtevalues=new ArrayList<String>();
   if(FilterMap!=null){
filtevalues=FilterMap.get(viewgblid);
}
for(int i=0; i<parameterlistNames.size();i++){
    String flag="true";
     String value=parameterlistNames.get(i)+"_"+i+"_"+parameterlist.get(i1);
     String id="ui-multiselect-"+key1+"-option-"+i;
     if(filtevalues!=null){
          String valuefilter=parameterlistNames.get(i).toString().replace("]", "").replace("[", "");
        for(int g=0;g<filtevalues.size();g++){

if(valuefilter.equalsIgnoreCase(filtevalues.get(g))){
    value=value+"_selecttrue";
%>
<option value="<%=value%>" ><%=parameterlistNames.get(i).toString().replace("]", "").replace("[", "")%></option>
     <% flag="false";
     break;
     }
}if(flag.equalsIgnoreCase("true")){
%>
<option value="<%=value%>" ><%=parameterlistNames.get(i).toString().replace("]", "").replace("[", "")%></option>

                           <%flag="false";
}


  }else{%>
         <option value="<%=value%>"  ><%=parameterlistNames.get(i).toString().replace("]", "").replace("[", "")%></option>
     <%  }
   }
                               }
}%>
                           </select>
             <input type="hidden" id="paramList_"<%=parameterlist.get(i1)%>  value="">

<%i1++;
 if ((setgblflag - 1) == j1) {%>

                </div>
                <% }
                            }

                                                       }

                            %>
<!--                </td><td>-->
<%}%>

            </div>

        </div>


    </div></td></tr>
<tr><td>
    <div id="xtendChartssTD" style="display: block;vertical-align: top; margin-top:0px;">
<!--
        <div id="filterDiv" style="width: 100%;height: 25px">



        </div>-->
    <div id="gridsterDiv" class="gridster" style="float:left;" ondrop="drop(event)" class="dragClass" ondragover="allowDrop(event)" >
                <ul id="gridUL" type="none" style="height:500px;border:white;" >
            </ul>
        <div style="margin-top: 20px">
                    <table style="width:100%;">
                        <tr>
                            <td valign="top" style="width:50%;">
                               <jsp:include page="Headerfolder/footerPage.jsp"/>

                            </td>
            </tr>
                    </table>
<!-- <button id="showTop">Show/Hide Top Slide Menu</button>-->
             </div>
                               <div id='newloading' class='loading_image' style="display:none;">
                <img alt=""  id='imgId' src='images/help-loading.gif'  border='0px' style='position:absolute; left: 150px; top: 10px;'/>
             </div>
             </div>
             </div>
    </td></tr>
</table>
                               <div id="tempDashletDiv1" style="display: none;"></div>
                                 <div id="renameDivId" style="display: none" title="Rename">
                <table>
                    <tr>
                        <td>
                            Enter Name:<input type="text" name="" value="" id="renameId">
                        </td>
                    </tr>

                    <tr  style="height:10px;">
                        <td align="center">
                            <input type="button" name="" value="Done" onclick="renamingfunction()">
                        </td>
                    </tr>
                </table>

            </div>
                                <div id="customTimeMsr" style="display: none" title="Custom Time Aggregation">
                    <table>
                        <tr>
                            <td colspan="2">
                            <input type="checkbox" id="customTimeOption" value="customTimeOption" onclick="customTimeSelection()"/>
                            <label  for="customTimeOption">Enable Custom Time Aggregation</label>
                            <table id="customTimeTable" style="display:none;">
                                <tr>
                                    <td align="left" width="50%">From Date</td>
                                    <td align="right" width="50%"><input id="customMsrdate"  type="text" size="15" value="" name="customMsrdate"></td>
                                </tr>
                                <br>
                <br>
                                <tr><td align="left" width="50%">To Date</td>
                                    <td align="right" width="50%"><input id="customMsrdate1"  type="text" size="15" value="" name="customMsrdate1"></td>
<!--                                    <td align="right" width="50%"> <select id="customMsrDuration" name="customMsrDuration" style="width: 125px;">
                                      <%for(int i=0;i<duration1.length;i++){%>
                                       <option value="<%=duration1[i]%>"><%=duration1[i]%></option>
                                       <%}%>
                                    </select></td>-->
                                </tr>

<!--                                <tr>
                                    <td align="left" width="50%">Comparision</td>
                                    <td align="right" width="50%">
                                        <select id="customMscompare" name="customMscompare" style="width: 125px;">
                                      <%for(int i=0;i<comparision.length;i++){%>
                                           <option value="<%=comparision[i]%>" ><%=comparision[i]%></option>
                                           <%}%>
                                    </select>
                                    </td>
                                </tr>-->
                            </table>
                        </td>
                    </tr>
<!--                    <tr>
                       <td align="left" width="50%">Default Aggregation
                       </td>
                       <td align="right" width="50%">
                           <select id="msrAggr" style="width: 125px" name="msrAggr">
                               <%for(int i=0;i<Aggr.length;i++){%>
                                <option value="<%=Aggr[i]%>" ><%=Aggr[i]%></option>
                                  <%}%>
                           </select>
                       </td>
                    </tr>-->
                  </table>
                 <br>
                <br>
                <table width="100%">
                    <tr>
                        <td align="center">
                            <input type="button" name="" value="Done" class="navtitle-hover" onclick="enableCustomTimeMeasure()">
                        </td>
                    </tr>
                </table>
                </div>
                           <div id="showFiltersoview" style="overflow-y: auto;display: none;border-radius: 10px;border: 1px solid grey;background-color: white;padding: 20px;width: auto;height: auto;"></div>

                               <script type="text/javascript" src="<%= request.getContextPath()%>/JS/classie.js"></script>
<script type="text/javascript">
         var menuTop = document.getElementById('cbp-spmenu-s3');
showTop.onclick = function() {

				classie.toggle( this, 'active' );
				classie.toggle( menuTop, 'cbp-spmenu-open' );
				//disableOther( 'showTop' );
			};

                        function trashOneview(){
//                          var menuTop = document.getElementById( 'cbp-spmenu-s3' );

                            classie.toggle( this, 'active' );
				classie.toggle( menuTop, 'cbp-spmenu-open' );
                        }
 </script>
    </body>
      <script type="text/javascript">
$(document).ready(function() {
  value= document.getElementById("gridUL");
    if(action=='desginer'){
 $("#rightDivone").hide();

    }
//                value.innerHTML='<center><img id="imgId" width="200px" height="200px" src=\"<%=request.getContextPath()%>/images/ajax1.gif\" align="middle"  width="75px" height="75px"  style="position:absolute" ></center>';
 fromopen='<%=fromopen%>';
 action='<%=action%>';
    if(fromopen!=''&& fromopen=='true' || fromopen=='gblfilter'){
//         $("#imgId").hide();
 $("#newloading").show();
       buildd3charts1(oneviewid,parent.gridster5)
     var data= parent.$("#filterdata").val();
     if(data!=""){
  filterData=JSON.parse(data);
     }
   parent.$("#filters1").val('<%=gson.toJson(FilterMap)%>');
     if(parent.$("#filters1").val()!=""){
  filterMap= JSON.parse(parent.$("#filters1").val());
     }
    }
    if(action!=''&& action=='globalftr'){
var data= parent.$("#filterdata").val();
filterData = {};
  filterMap = {};
   if(data!=""){
  filterData=JSON.parse(data);
   }
    }
    if(action!=''&& action=='save'){
//        saveoneviewjsons(oneviewid,parent.gridster5,"true")
         var data= parent.$("#filterdata").val();
          if(data!=""){
  filterData=JSON.parse(data);
          }
          globalgrid=parent.gridster5
         buildd3charts1(oneviewid,parent.gridster5)
    }
     parent.value= document.getElementById("gridUL");
       });
     function  saveoneviewjsons1(oneViewIdValue){
saveoneviewjsons(oneviewid,parent.gridster5)
     }
     function optionSelect(){
//        $("#themeselect2").toggle();
//$("#lmOption").onmouseover()
        $("#themeselect2").toggle();
    }
     function oneviewSelect(){
//        $("#themeselect2").toggle();
//$("#lmOption").onmouseover()
        $("#oneviewlist").toggle();
    }
     function openoneview(oneViewName,oneViewId){
window.open('srchQueryAction.do?srchParam=oneViewBy&homeFlag=true&oneViewId='+oneViewId+'&oneviewname='+oneViewName,'_blank')
    }
     function refreshoneview(oneviewid,gridster5,value,refershtype){
                         value.innerHTML='<center><img id="imgId" width="100px" height="100px" src=\"'+parent.ctxpath+'/images/ajax1.gif\" align="right"  width="55px" height="55px"  style="position:absolute" ></center>';
 gridster5.remove_all_widgets();
onecontainersave=refershtype;
fromopen='refresh';
action1=refershtype;
 buildd3charts1(oneviewid, globalgrid);
// $("#imgId").hide();
     }
       function saveOneVIewRegnew(ivvalue,oneViewName,grdster,value){
        value= document.getElementById("gridUL");
    var widt=parseInt((((($(window).width()))-35)/4)/8);

                value.innerHTML='<center><img id="imgId" width="100px" height="100px" src=\"'+parent.ctxpath+'/images/ajax1.gif\" align="right"  width="55px" height="55px"  style="position:absolute" ></center>';
   var icons="";
            if(parent.$("#saveTabId0").is(':visible')){
                    icons="hide";
                }else{
                    icons="visible";
                }
                 var data= parent.$("#filterdata").val();
          if(data!=""){
  filterData=JSON.parse(data);
          }
                 var innerWidth=parseInt(window.innerWidth)-120;
                var date = parent.$("#dateId").val();
                var duration = parent.$("#timedetailselectId").val();
                var compare = parent.$("#compareSelectId").val();
                var confirmText='';
                 $.ajax({
                       async:false,
                       type:"POST",
             url: parent.ctxpath+'/oneViewAction.do?templateParam2=saveOneViewReg&reportBy=viewReport&oneViewIdValue='+oneviewid+'&oneviewName='+oneViewName+'&date='+date+'&duration='+duration+'&compare='+compare+'&innerWidth='+innerWidth+'&action=save&icons='+icons+'&filters1='+parent.$("#filters1").val(),
                            success: function(data){
saveoneviewjsons(oneviewid,parent.gridster5,"true")

                            }
                             });
                grdster.remove_all_widgets();
 parent.gridster5 = $(".gridster > ul").gridster({
        widget_margins: [2, 2],
//        autogenerate_stylesheet: false,
        widget_base_dimensions: [widt,widt],
        min_cols: 29,
        resize: {
            enabled: true,
            axes:['both'],
            handle_class:'gs-resize-handle',
            start: function (e, ui, $widget) {
            },
            stop: function (e, ui, $widget) {
                var newHeight = this.resize_coords.data.height;
                var newWidth = this.resize_coords.data.width;
//                 gridResize(newHeight, newWidth);
//      alert("id  "+($($widget).attr('id')).replace("div","" ))
                var chartId=$($widget).attr('id').replace("div","" );
      //added by Ram
                var length=chartId.length;
                var regid = chartId.replace("chart","");
                  generateJsonDataNewOneview(chartId,oneviewid,regid,"localrefresh");
       }
        }
    }).data('gridster');
    fromopen='save';
    action1='open';
                 buildd3charts1(oneviewid,parent.gridster5)
//$("#imgId").hide();
    }
    function gotForOneviewcharts(action,grdster){
     $("#newloading").show();
   gotForOneview(action);
//var widt=parseInt((((($(window).width()))-35)/4)/8);
//    grdster.remove_all_widgets();
// parent.gridster5 = $(".gridster > ul").gridster({
//        widget_margins: [2, 2],
//        widget_base_dimensions: [widt,widt],
//        min_cols: 29,
//        resize: {
//            enabled: true,
//            axes:['both'],
//            handle_class:'gs-resize-handle',
//            start: function (e, ui, $widget) {
//            },
//            stop: function (e, ui, $widget) {
//                var newHeight = this.resize_coords.data.height;
//                var newWidth = this.resize_coords.data.width;
//                var chartId=$($widget).attr('id').replace("div","" );
//                  localRefresh(chartId);
//       }
//        }
//    }).data('gridster');
    action1='GO'
     buildd3charts1(oneviewid,parent.gridster5)

    }
    function gotForOneview(action1){
                if(parent.$("#BisiessRole").is(":visible")){
                $("#globalFilterDiv").hide();
                }
                var version =$("#oneviewversion").val();
if(version !='' && version=='2.5'){
var formisgo='true';
}else{
var formisgo='null';
}
                var innerWidth=parseInt(window.innerWidth)-120;

                          var  oneviewtypedate1=parent.oneviewtypedate1;
                if(oneviewtypedate1=='true'||oneviewtypedate1=='false'){
                var date = $("#stdate").val();
             var isdefult;

                var date1 = $("#dateId1").val();
                var duration1 = $("#todate").val();
                var comparefrom = $("#comparefrom").val();
                var compareto = $("#compareto").val();
// by sandeep code for range and std date
                if((date!='')||(date1!='' && duration1!='')){
                         if(parent.datetype=='PRG_DATE_RANGE'){
                          if(date1==''){
                              isdefult='date1';
                            date1 =$("#defvaluedate").val();
                          }if(duration1==''){
                               isdefult='duration1';
                              duration1= parent.defultdate;
                          }
                      }


                }else{
                    isdefult='true';

                     if(parent.datetype=='PRG_DATE_RANGE'){
                         duration1= parent.defultdate;
                         date1 =$("#defvaluedate").val();
                     }else{
                    date =$("#defvaluedate").val();
                     }

                }
                var duration = $("#timedetailselectId1").val();
                var compare = $("#compareSelectId1").val();
                }else{
                 var date = $("#dateId").val();
                  var duration = $("#timedetailselectId").val();
                var compare = $("#compareSelectId").val();
                }
                var value= document.getElementById("regionTableId");
                var action1=action1;
                var confirmText='';
                if(action1=='Save'){
                     confirmText= confirm("These Changes Are Permanently Stored.\n Do You Want to Save?");
                }
                else {
                    confirmText=true;
                }
                if(confirmText==true){
                $("#designId").css({'height':'400px'})
//                value.innerHTML='<center><img id="imgId" width="200px" height="200px" src=\"<%=request.getContextPath()%>/images/ajax1.gif\" align="middle"  width="75px" height="75px"  style="position:absolute" ></center>';
                $.post('<%=request.getContextPath()%>/oneViewAction.do?templateParam2=getingOneviews&reportBy=viewReport&oneViewIdValue='+oneviewid+'&oneviewName='+parent.oneViewName+'&date='+date+'&date1='+date1+'&duration='+duration+'&duration1='+duration1+'&compare='+compare+'&formisgo='+formisgo+'&innerWidth='+innerWidth+'&action1='+action1+'&isdefult='+isdefult+'&comparefrom='+comparefrom+'&compareto='+compareto+'&fromoneviewspeedreduce=true&action=GO',
                function(data){
                    if(data!='NO DATA'){
//                        document.getElementById("designId").style.display='';
                        $("#designId").css({'padding-bottom':''})
                        $("#designId").css({'height':''})
//                        $("#regionTableId").html(data);
//                          buildd3charts(oneviewid);
 var size;
                     var visible;

//                        initCollapser("");
//                         home();
                    }

                });
            }
            }
     function gotforoneviewgbl(ivvalue,oneViewName,grdster,value){
 value= document.getElementById("gridUL");
    var widt=parseInt((((($(window).width()))-35)/4)/8);
    grdster.remove_all_widgets();
 parent.gridster5 = $(".gridster > ul").gridster({
        widget_margins: [2, 2],
        widget_base_dimensions: [widt,widt],
        min_cols: 29,
        resize: {
            enabled: true,
            axes:['both'],
            handle_class:'gs-resize-handle',
            start: function (e, ui, $widget) {
            },
            stop: function (e, ui, $widget) {
                var newHeight = this.resize_coords.data.height;
                var newWidth = this.resize_coords.data.width;
                var chartId=$($widget).attr('id').replace("div","" );
                  localRefresh(chartId);
       }
        }
    }).data('gridster');
                value.innerHTML='<center><img id="imgId" width="100px" height="100px" src=\"'+parent.ctxpath+'/images/ajax1.gif\" align="right"  width="55px" height="55px"  style="position:absolute" ></center>';
onecontainersave="refresh";
// var source = parent.ctxpath+'/newOneView.jsp?action=globalftr&oneviewname='+parent.oneViewName+'&oneViewIdValue='+ivvalue;
//                        var dSrc = parent.document.getElementById("regionTableIdgrid");
//                        dSrc.src = source;
buildd3chartsglobal(ivvalue,parent.gridster5)

}
function setTimecluase(){
var viewOvName = [];
        var viewOvIds = [];
        var filterMaptime = JSON.parse(parent.$("#filters1").val());
          if(typeof filterMaptime !=="undefined"){
          var drillKeys = Object.keys(filterMaptime);
        <%  if(viewbynames!=null && !viewbynames.isEmpty()){
for(int i=0;i<viewbynames.size();i++){%>
            viewOvName.push('<%=viewbynames.get(i)%>');
            viewOvIds.push('<%=parameterlist.get(i)%>')
    <%}
    }%>

           for(var key in drillKeys){
             var quickViewname = viewOvName[viewOvIds.indexOf(drillKeys[key])];


             if(quickViewname.toString().trim() == "Month Year" || quickViewname.toString().trim() == "Month-Year" || quickViewname.toString().trim() == "Month - Year" || quickViewname.toString().trim() == "Month") {
                parent.$("#drillFormat").val("time");break;
             }else if(quickViewname.toString().trim() == "Qtr" || quickViewname.toString().trim() == "Time" || quickViewname.toString().trim() == "qtr" || quickViewname.toString().trim() == "Qtr Year" || quickViewname.toString().trim() == "Year"){
                 parent.$("#drillFormat").val("time");break;
             }else {
                parent.$("#drillFormat").val("none");
             }
             }
             }
}
     function buildd3chartsglobal(idvalue,gridster){
   $.post(parent.ctxpath+'/reportViewer.do?reportBy=gettingJsonData&oneviewid='+idvalue+'&action='+onecontainersave,
                function(data){
                     var jsonVar=eval('('+data+')');
                 var  regionids=jsonVar.regionids;
                 var  reportids=jsonVar.reportids;
                 var  repnames=jsonVar.repnames;
                 var  chartnames=jsonVar.chartnames;
                 var  busrolename=jsonVar.busrolename;
                 var  idArr=jsonVar.idArr;
                 var  drillviewby=jsonVar.drillviewby;
                 for(var i=0;i<regionids.length;i++){
                      parent.$("#graphsId").val(reportids[i]);
                      parent.$("#graphName").val(repnames[i]);
                      parent.$("#busrolename").val(busrolename[i]);
                        parent.$("#chartname").val(chartnames[i]);
                        parent.$("#oneViewId").val(idvalue);
                        var idArr1=idArr[i];

                  $.ajax({
                       async:false,
                       type:"POST",
                          data: parent.$("#oneviewgraphForm").serialize()+"&reportId="+ reportids[i]+"&reportName="+encodeURIComponent(parent.$("#graphName").val()),

             url: parent.ctxpath+"/reportViewer.do?reportBy=getAvailableCharts1&&fromoneview=true&action=open&fromoneview=true&oneviewid="+idvalue+"&regid="+regionids[i],

                            success: function(data){
//                $("#loading").hide();
                if(data=="false"){

            }
            else{
                var jsondata = JSON.parse(data)["data"];
                parent.$("#chartData").val(JSON.stringify(JSON.parse(JSON.parse(data)["meta"])["chartData"]));

                var meta = JSON.parse(JSON.parse(data)["meta"]);
                parent.$("#viewby").val(JSON.stringify(meta["viewbys"]));
                 parent.$("#viewbyIds").val(JSON.stringify(meta["viewbyIds"]));
                parent.$("#measure").val(JSON.stringify(meta["measures"]));
                 parent.$("#measureIds").val(JSON.stringify(meta["measureIds"]));
                parent.$("#aggregation").val(JSON.stringify(meta["aggregations"]));
                parent.$("#drilltype").val((meta["drilltype"]));
                 parent.$("#timedetails").val(JSON.parse(data)["Timedetails"]);
                  parent.$("#timeMap").val(JSON.stringify(meta["timeMap"]));
//                parent.$("#filterMap").val(JSON.stringify(meta["filterMap"]));
var filterMap1 = {};
filterMap1=JSON.stringify(meta["filterMap"]);
//parent.$("#filterMap").val(JSON.stringify(meta["filterMap"]));

            }
var viewOvName = [];
        var viewOvIds = [];
        var filterMaptime = JSON.parse(parent.$("#filters1").val());
          if(typeof filterMaptime !=="undefined"){
          var drillKeys = Object.keys(filterMaptime);
           <%   if(viewbynames!=null && !viewbynames.isEmpty()){
for(int i=0;i<viewbynames.size();i++){%>
            viewOvName.push('<%=viewbynames.get(i)%>');
            viewOvIds.push('<%=parameterlist.get(i)%>')
    <%}}%>

           for(var key in drillKeys){
             var quickViewname = viewOvName[viewOvIds.indexOf(drillKeys[key])];


             if(quickViewname.toString().trim() == "Month Year" || quickViewname.toString().trim() == "Month-Year" || quickViewname.toString().trim() == "Month - Year" || quickViewname.toString().trim() == "Month") {
                parent.$("#drillFormat").val("time");break;
             }else if(quickViewname.toString().trim() == "Qtr" || quickViewname.toString().trim() == "Time" || quickViewname.toString().trim() == "qtr" || quickViewname.toString().trim() == "Qtr Year" || quickViewname.toString().trim() == "Year"){
                 parent.$("#drillFormat").val("time");break;
             }else {
                parent.$("#drillFormat").val("none");
             }
             }
             }
//                             var  Dashlets="Dashlets-"+regionids[i];
//                              var wid ;
//                        var hgt1 ;
  var chartData = JSON.parse(parent.$("#chartData").val());
//  chartData[chartnames[i]]["viewBys"][0]=drillviewby[i];
    parent.$("#chartData").val(JSON.stringify(chartData));
         $.ajax({
                       async:false,
                       type:"POST",
                          data: parent.$("#oneviewgraphForm").serialize()+"&reportId="+ reportids[i]+"&reportName="+parent.$("#graphName").val()+'&chartname='+chartnames[i],

             url: parent.ctxpath+"/reportViewer.do?reportBy=drillCharts&fromoneview=true&oneviewID="+idvalue+"&regid="+regionids[i],

                            success: function(data){   var resultset = data;
 for(var t in chartData){
                 if(chartData[t]["chartType"]=="KPI-Table" ||chartData[t]["chartType"]=="Expression-Table" ||chartData[t]["chartType"]=="Emoji-Chart" ||chartData[t]["chartType"]=="Stacked-KPI" ||chartData[t]["chartType"]=="KPIDash" ||chartData[t]["chartType"]=="Bullet-Horizontal" ||chartData[t]["chartType"]=="TileChart" ||chartData[t]["chartType"]=="RadialProgress"||chartData[t]["chartType"]=="LiquidFilledGauge" ||chartData[t]["chartType"]=="Dial-Gauge" ||chartData[t]["chartType"]=="Emoji-Chart"){
                      
                     parent.$("#chartData").val(JSON.stringify(JSON.parse(JSON.parse(resultset)["meta"])["chartData"]))
                    }
                }
  var data1 = JSON.parse(resultset)["data"];
                                 generateglobalDataOneview(gridster,idvalue,regionids[i],chartnames[i],"open",data1);
viewid=[];
filternames=[];
//generateChart(data);
                                }
                     });

//                    drillWithinchart11(idArr1,"Dashlets-"+regionids[i],wid,hgt1,regionids[i],idvalue,repnames[i],reportids[i],chartnames[i],"globalparam");
//setTimeout(function(){drillWithinchart11(idArr1,"Dashlets-"+regionids[i],wid,hgt1,regionids[i],idvalue,repnames[i],reportids[i],chartnames[i],drillviewby[i],"open") }, 200);
//                          drillWithinchart1(idArr1,"Dashlets-"+regionids[i],wid,hgt1,regionids[i],idvalue,repnames[i],reportids[i],chartnames[i],drillviewby[i])
             }
            });
//                         }
                 }
                     });
                     filterMap= JSON.parse(parent.$("#filters1").val());

}
function  buildd3charts1(oneviewid,gridster5){
    $.ajax({
                       async:false,
                       type:"POST",
             url: parent.ctxpath+'/reportViewer.do?reportBy=gettingJsonData&oneviewid='+oneviewid+'&action='+onecontainersave,
               success: function(data){
                     var jsonVar=eval('('+data+')');
                 var  regionids=jsonVar.regionids;
                 var  reportids=jsonVar.reportids
                 var  repnames=jsonVar.repnames
                 var  chartnames=jsonVar.chartnames;
                 var  busrolename=jsonVar.busrolename;
                 var  idArr=jsonVar.idArr;
                 var  drillviewby=jsonVar.drillviewby;
                 for(var i=0;i<regionids.length;i++){
                      parent.$("#graphsId").val(reportids[i]);
                      parent.$("#graphName").val(repnames[i]);
                      parent.$("#busrolename").val(busrolename[i]);
                        parent.$("#chartname").val(chartnames[i]);
                        parent.$("#oneViewId").val(oneviewid);
                        var idArr1=idArr[i];
//                         $("#chart"+regionids[i]).html('<div class="tooltip" id="my_tooltip" style="display: none"></div>');
                          if(idArr1==null || idArr1=='null'){
// var myVar= setTimeout(function(){ generateJsonDataOneview("Dashlets-"+regionids[i],idvalue,regionids[i],chartnames[i],"add")}, 1000);
//clearTimeout(myVar);
if(fromopen!=''&& fromopen=='save'){

}else{
    fromopen="null";
}
//alert(regionids[i])
if(onecontainersave=='designrfresh'){
$("#imgId").hide();
}else{
                    setTimeout(generateJsonDataOneview(chartnames[i],oneviewid,regionids[i],parent.oneViewName,action1,gridster5,fromopen), 20000);
}
                         }else{
//                                $.post(
//            'reportViewer.do?reportBy=getAvailableCharts&fromoneview=true&reportId='+ reportids[i]+"&reportName="+$("#graphName").val(), $("#oneviewgraphForm").serialize(),
//            function(data) {
                  $.ajax({
                       async:false,
                       type:"POST",
                          data: parent.$("#oneviewgraphForm").serialize()+"&reportId="+ reportids[i]+"&reportName="+encodeURIComponent(parent.$("#graphName").val()),

             url: parent.ctxpath+"/reportViewer.do?reportBy=getAvailableCharts&fromoneview=true",

                            success: function(data){
                $("#loading").hide();
                if(data=="false"){

            }
            else{
                var jsondata = JSON.parse(data)["data"];
                parent.$("#chartData").val(JSON.stringify(JSON.parse(JSON.parse(data)["meta"])["chartData"]));

                var meta = JSON.parse(JSON.parse(data)["meta"]);
                parent.$("#viewby").val(JSON.stringify(meta["viewbys"]));
                parent.$("#measure").val(JSON.stringify(meta["measures"]));
                 parent.$("#viewbyIds").val(JSON.stringify(meta["viewbyIds"]));
                parent.$("#measureIds").val(JSON.stringify(meta["measureIds"]));
                parent.$("#aggregation").val(JSON.stringify(meta["aggregation"]));
                parent.$("#drilltype").val((meta["drilltype"]));

            }

                             var  Dashlets="Dashlets-"+regionids[i];
                              var wid ;
                        var hgt1 ;
  var chartData = JSON.parse(parent.$("#chartData").val());
  chartData[chartnames[i]]["viewBys"][0]=drillviewby[i];
    parent.$("#chartData").val(JSON.stringify(chartData));
                        hgt1 = document.getElementById(Dashlets).offsetHeight;
                        wid = document.getElementById(Dashlets).offsetWidth;
                    drillWithinchart11(idArr1,"Dashlets-"+regionids[i],wid,hgt1,regionids[i],idvalue,repnames[i],reportids[i],chartnames[i],drillviewby[i]);
//setTimeout(function(){drillWithinchart11(idArr1,"Dashlets-"+regionids[i],wid,hgt1,regionids[i],idvalue,repnames[i],reportids[i],chartnames[i],drillviewby[i],"open") }, 200);
//                          drillWithinchart1(idArr1,"Dashlets-"+regionids[i],wid,hgt1,regionids[i],idvalue,repnames[i],reportids[i],chartnames[i],drillviewby[i])
             }
            });
                         }
                 }
                 $("#newloading").hide();
               }
                     });


}
function deleteGraph(chrtId,oneviewid,regid){
var txt;
    var r = confirm("Are you sure you want to delete ?");
    if (r == true) {
       parent.gridster5.remove_widget( $("#"+chrtId) );
        $.ajax({
                       async:false,
                       type:"POST",
                          data: parent.$("#oneviewgraphForm").serialize(),

             url: parent.ctxpath+"/oneViewAction.do?templateParam2=deleteregion&fromoneview=true&oneviewID="+oneviewid+"&regid="+regid,

                            success: function(data){
//                                alert(data)

                            }
                });
//        txt = "You pressed OK!";
    } else {
//        txt = "You pressed Cancel!";
    }

}
function enableCustomTimeMeasure(){
  var  oneViewIdValue= parent.$("#oneViewId").val();
   var chartname=parent.$("#chartname").val();
   var colNumber=parent.$("#regid1").val();
    var isCustomTimeApplied=false;
    var customMsrDate='';
    var customMsrDate1='';
    var customMsrDuration='';
    var customMsrCompre='';
    var customMsrAgr='';
    if(document.getElementById("customTimeOption") != null && document.getElementById("customTimeOption").checked){
         isCustomTimeApplied=true;
         customMsrDate=$("#customMsrdate").val();
         customMsrDate1=$("#customMsrdate1").val();
         customMsrDuration=$("#customMsrDuration").val();
         customMsrCompre=$("#customMscompare").val();
          }
//      customMsrAgr=$("#msrAggr").val();
     $("#customTimeMsr").dialog('close');
       $("#chart"+colNumber).html('<center><img id="imgId" src="images/ajax.gif" align="middle"  width="45px" height="45px"  style="position:absolute" /></center>');
    var loadImg='<img id="loadImg" width="15px" height="15px" src=\"<%=request.getContextPath()%>/images/ajax-loader.gif\" align="right"  width="5px" height="5px"  style="position:absolute;float:right;margin-left:45px;" >';
//    $("#Dashlets-"+colNumber).html('<center><img id="imgId" src="images/ajax1.gif" align="middle"  width="45px" height="45px"  style="position:absolute" /></center>');
    $.post(parent.ctxpath+'/oneViewAction.do?templateParam2=enableCustomTimeMeasure&oneViewIdValue='+oneViewIdValue+'&colNo='+colNumber+'&isCustomTimeApplied='+isCustomTimeApplied
                  +'&customMsrDate='+customMsrDate+'&customMsrDuration='+customMsrDuration+'&customMsrCompre='+customMsrCompre+'&customMsrAgr='+customMsrAgr+'&customMsrDate1='+customMsrDate1,
                function(data){
                    var chartid="chart"+colNumber;
//                    customtimeaggregation(chartname,oneViewIdValue,colNumber,parent.oneViewName,"customtimeaggre",parent.gridster1,"null")
//                    generateJsonDataNewOneview(chartid,oneViewIdValue,colNumber,"customtimeaggre","true")
                    generateJsonDataOneview(chartname,oneViewIdValue,colNumber,parent.oneViewName,"customtimeaggre",parent.gridster1,"null")
//                    parent.$("#"+colNumber).html(data);
                });
}
      </script>
</html>