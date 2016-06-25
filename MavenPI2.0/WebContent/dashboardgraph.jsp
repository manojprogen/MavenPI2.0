<%--
    Document   : dashboardgraph
    Created on : Jul 2, 2015, 10:05:25 AM
    Author     : Sandeep
--%>


<%@page import="prg.db.PbDb"%>
<%@page import="prg.db.PbReturnObject"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%
String dashboardId=request.getParameter("dashboardId");
String dashletId=request.getParameter("dashletId");
String ElementId=request.getParameter("ElementId");
String measurename=request.getParameter("measurename");
//added by sruthi for jqplotgraph
String targetElementId=request.getParameter("targetElementId");
String targetElemName=request.getParameter("targetElemName");
//ended by sruthi
//added by sruthi for prior jqplotgraph
String priorElementId=request.getParameter("priorid");
String viewbyid=request.getParameter("viewbyid");
String viewbyname=request.getParameter("viewbyname");
 PbReturnObject allRepNameObj=null;
      String priorname=null;
     String query="select user_col_desc,REF_ELEMENT_TYPE from  prg_user_all_info_details where sub_folder_type = 'Facts'and ref_element_id = "+ElementId+" and REF_ELEMENT_TYPE=2";
       PbDb pbdb = new PbDb();
        allRepNameObj = pbdb.execSelectSQL(query);
        if (allRepNameObj != null && allRepNameObj.rowCount > 0)  {
        priorname=allRepNameObj.getFieldValueString(0,0);
        }
%>
<html>
    <head>
         <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <link rel="stylesheet" href="<%= request.getContextPath()%>/resources/css/bootstrap.min.css" />
        <link rel="stylesheet" href="<%= request.getContextPath()%>/resources/css/bootstrap-theme.min.css">
        <!--<link rel="stylesheet" href="<%= request.getContextPath()%>/jQuery/multiSelect/css/jquery.multiselect.css">-->
        <script src="<%= request.getContextPath()%>/resources/js/bootstrap.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/jQuery/d3/customtooltip.js"></script>
          <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/lib/jquery/js/jquery-1.7.2.min.js"></script>
         <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/JS/Grid/jquery.gridster.js"></script>
        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/themes/blue/jquery-ui-1.7.3.custom.css" rel="stylesheet" />
        <link type="text/css" href="<%=request.getContextPath()%>/css/d3/tooltip.css" rel="stylesheet"/>
<!--         <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/themes/blue/TableCss.css" rel="stylesheet" />-->
        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/themes/blue/pbReportViewerCSS.css" rel="stylesheet" />
         <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/pbReportViewerJS.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/reportviewer/ReportViewer.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/TableDisplay/JS/pbTableMapJSForPopUp.js"></script>
          <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jqplot/jquery.js"></script>
           <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jqplot/jquery.min.js"></script>
           <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jqplot/jquery.jqplot.js"></script>
           <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jqplot/jquery.jqplot.min.js"></script>
           <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jqplot/jqplot.barRenderer.min.js"></script>
           <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jqplot/jqplot.categoryAxisRenderer.min.js"></script>
           <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jqplot/jqplot.pointLabels.min.js"></script>
           <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jqplot/jqplot.pieRenderer.min.js"></script>
           <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jqplot/jqplot.donutRenderer.min.js"></script>
           <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jqplot/jqplot.canvasAxisLabelRenderer.min.js"></script>
           <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jqplot/jqplot.canvasTextRenderer.min.js"></script>
           <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jqplot/jqplot.dateAxisRenderer.min.js"></script>
           <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jqplot/jqplot.logAxisRenderer.min.js"></script>
           <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jqplot/jqplot.canvasAxisTickRenderer.min.js"></script>
           <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jqplot/jqplot.highlighter.min.js"></script>
           <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jqplot/jqplot.bubbleRenderer.min.js"></script>
           <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jqplot/jqplot.funnelRenderer.js"></script>
           <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jqplot/jqplot.ClickableBars.js"></script>
         <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jqplot/jqplot.EnhancedLegendRenderer.js"></script>
           <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jqplot/jqplot.trendline.js"></script>
             <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jqplot/jqplot.cursor.min.js"></script>
          <script type="text/javascript" src="<%=request.getContextPath()%>/TableDisplay/JS/pbGraphDisplayRegionJS.js"></script>
             <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jqplot/jqplot.canvasOverlay.min.js"></script>
<!-- <script type="text/javascript" src="<%= request.getContextPath()%>/JS/jquery-ui.min.js"></script>
   <link rel="stylesheet" type="text/css" href="css/jquery-ui.css" />
     <script type="text/javascript" src="<%= request.getContextPath()%>/JS/jquery.multiselect.js"></script>
        <link rel="stylesheet" type="text/css" href="css/jquery.multiselect.css" />-->
<link href="<%=request.getContextPath()%>/javascript/jqplot/jquery.jqplot.min.css" rel="stylesheet" type="text/css" />

       <script type="text/javascript">
           var dashboardId='<%=dashboardId%>';

           var dashletId='<%=dashletId%>';
           var ElementId='<%=ElementId%>';
           var measurename='<%=measurename%>';
             var viewbyname='<%=viewbyname%>';
               var viewbyid='<%=viewbyid%>';
           var priorid1='<%=priorElementId%>';//added by sruthi for prior jqplotgraph
           var targetElementId='<%=targetElementId%>';//added by sruthi for target jqplotgraph
            var targetElemName='<%=targetElemName%>';//added by sruthi for target jqplotgraph
           var viewchange;
           var targetflag=false;
           var measureflag=true;
           var priorflag=true;//added by sruthi for prior jqplotgraph
           $(document).ready(function(){
               //changed by sruthi
                  if(targetflag){
                                      viewchange="null"
                                        var priorid2=null;
               dashgenerateQuickTrend1(dashboardId,dashletId,ElementId,measurename,targetElementId,targetElemName,priorid2,viewbyid,viewbyname);
                  }
//                  if(priorflag){
//                        var targetElementId1=null;
//                          var targetElemName1=null;
//                       viewchange="null"
//               dashgenerateQuickTrend1(dashboardId,dashletId,ElementId,measurename,targetElementId1,targetElemName1,priorid1);
//                  }
                  if(measureflag)
                      {
                          var targetElementId1=null;
                           var targetElemName1=null;
                           //var priorid1=null;
                         viewchange="null"
                        dashgenerateQuickTrend1(dashboardId,dashletId,ElementId,measurename,targetElementId1,targetElemName1,priorid1,viewbyid,viewbyname);
                      }

           });
            function viewbychange(dashboardId,dashletId,ElementId,measurename,viewchange1){
//                     targetflag= document.getElementById("targetid").checked;
                  if(targetflag){
                      var priorid=null;
                          viewchange=viewchange1;
                  dashgenerateQuickTrend1(dashboardId,dashletId,ElementId,measurename,targetElementId,targetElemName,priorid,viewbyid,viewbyname);
                  }
//                  else if(priorflag){ //added by sruthi for prior jqplotgraph
//                       viewchange="null"
//                         var targetElementId1=null;
//                           var targetElemName1=null;
//               dashgenerateQuickTrend1(dashboardId,dashletId,ElementId,measurename,targetElementId1,targetElementId1,priorid1);
//                  } //ended  by sruthi
                  else{
                          var targetElementId=null; //added by sruthi for jqplotgraph
                           var targetElemName=null; //added by sruthi for jqplotgraph
                          //  var priorid=null;
                      viewchange=viewchange1;
                  dashgenerateQuickTrend1(dashboardId,dashletId,ElementId,measurename,targetElementId,targetElemName,priorid1,viewbyid,viewbyname);
                  }

            }
            function currnetviewbychange(dashboardId,dashletId,ElementId,measurename,viewchange1,targetElementId,targetElemName,priorid){
                targetflag= document.getElementById("targetid").checked;

                     priorflag=document.getElementById("priorid").checked;
                     measureflag=document.getElementById("measureid").checked;

                          viewchange=viewchange1;
                          if(!priorflag){
                           var priorid=null;
                          }
                          if(!measureflag){
                           var ElementId=null;
                           var measurename=null;
                          }
                          if(!targetflag){
                           var targetElementId=null;
                           var targetElemName=null;
                          }
                  currnetQuickTrend1(dashboardId,dashletId,ElementId,measurename,targetElementId,targetElemName,priorid);


            }
            function  currnetQuickTrend1(dashboardId,dashletId,ElementId,measurename,targetElementId,targetElemName,priorid){

                var ctxpath = parent.document.getElementById("ctxPath1").value;alert(ctxpath)
 var htmlVar="";
  var paramDisp=parent.document.getElementById("iddiv");
//    $("#dashquickTrend").dialog('open');
//    document.getElementById("favParams").style.display='none';
   var html = "";
html += "<center><img id='imgId1' src='images/ajax.gif' align='middle'  width='100px' height='80px'  style='position:absolute' ></center>";
$("#iddiv").html(html);

            $.post(ctxpath+'/reportViewer.do?reportBy=designGraphInDesigner&reportId='+dashboardId,
             function(data){
            var jsonVar=eval('('+data+')')
            var graphIds ='';
            var graphCount=0;
            var parentDiv = '';
            graphCount++;
            graphIds = graphIds+","+graphCount;

            graphIds=jsonVar;
//            parent.$("#graphlist").hide();
//            parentDiv = parent.document.getElementById('tabGraphs');
//            parentDiv.style.height = '250'
            $.ajax({
//                url: 'reportTemplateAction.do?templateParam=buildGraphs&gid='+graphIds+'&grptypid=Bar&graphChange=default&grpIds='+graphIds+'&REPORTID='+document.getElementById("REPORTID").value,
                url: 'reportTemplateAction.do?templateParam=buildGraphs&height=450&width=750&gid='+graphIds+'&grptypid=Line&isdashboard=true&graphChange=default&grpIds='+graphIds+'&REPORTID='+dashboardId+'&measurename='+measurename+'&targetElementId='+targetElementId+'&targetElemName='+targetElemName+'&priorid='+priorid+'&measureid='+ElementId, //changed by sruthi for jqplotgraph
                success: function(data){
                    if(data != ""){
                       generateQuickTrendChart1(data);
                }
}
            });


        })
            }
            //added by sruthi for jqplotgraph
               function targetviewbychange(dashboardId,dashletId,ElementId,measurename,viewchange1,targetElementId,targetElemName,priorid){
       if(targetElementId !=null &&targetElementId !="null"){
                     targetflag= document.getElementById("targetid").checked;
       }
                     priorflag=document.getElementById("priorid").checked;
                     measureflag=document.getElementById("measureid").checked;

                          viewchange=viewchange1;
                          if(!priorflag){
                           var priorid=null;
                          }
                          if(!measureflag){
                           var ElementId=null;
                           var measurename=null;
                          }
                          if(!targetflag){
                           var targetElementId=null;
                           var targetElemName=null;
                          }
                  dashgenerateQuickTrend1(dashboardId,dashletId,ElementId,measurename,targetElementId,targetElemName,priorid,viewbyid,viewbyname);

//                  if(priorflag){
//                       viewchange="null"
//                         var targetElementId1=null;
//                           var targetElemName1=null;
//               dashgenerateQuickTrend1(dashboardId,dashletId,ElementId,measurename,targetElementId1,targetElemName1,priorid1);
//                  }
//                  if(measureflag){
//                          var targetElementId=null; //added by sruthi for jqplotgraph
//                           var targetElemName=null; //added by sruthi for jqplotgraph
//                           var priorid=null;
//                      viewchange=viewchange1;
//                  dashgenerateQuickTrend1(dashboardId,dashletId,ElementId,measurename,targetElementId,targetElemName,priorid);
//                  }


            }
            //ended by sruthi
            function dashgenerateQuickTrend1(dashboardId,dashletId,ElementId,measurename,targetElementId,targetElemName,priorid,viewbyid,viewbyname){  //changed by sruthi for jqplotgraph
//  parent.$("#chartData").val("");

 var ctxpath = parent.document.getElementById("ctxPath1").value;
 var htmlVar="";
  var paramDisp=parent.document.getElementById("iddiv");
//    $("#dashquickTrend").dialog('open');
//    document.getElementById("favParams").style.display='none';
   var html = "";
html += "<center><img id='imgId1' src='images/ajax.gif' align='middle'  width='100px' height='80px'  style='position:absolute' ></center>";
$("#iddiv").html(html);
  $.ajax({
            //url: 'dashboardTemplateViewerAction.do?templateParam2=buildKpis&Kpis='+kpis+'&KpiNames='+kpisName+'&divId='+divId+'&kpiType='+kpiType+'&dashboardId='+dbrdId+'&countkpis='+more+"&kpiMasterId="+divId,
           url:ctxpath+'/dashboardViewer.do?reportBy=buildqucktrend&dashboardId='+dashboardId+'&dashletId='+dashletId+'&viewbyid='+viewbyid+'&viewbyname='+viewbyname+'&Kpis='+ElementId+'&targetElementId='+targetElementId+'&targetElemName='+targetElemName+'&measurename='+encodeURIComponent(measurename)+'&viewchange='+viewchange,
           success: function(data){
            if(data=="false"){
//            $("#quickTrend").dialog('open');
               htmlVar+='<div style="background-color: #eee; height: 750px;cursor: pointer" "><span><h2 style="font-family: cursive; color: #870E30; font-size: large;text-align: center;cursor: pointer" onclick="editViewBys()">No Graph Available for this Report.</h2><h2 style="font-family: cursive; color: #870E30; font-size: large;text-align: center;cursor: pointer" onclick="editViewBys()">Please Add Graphs from Option above.</h2></span> </div>';
               $("#quickTrend").append(htmlVar);
//                $("#xtendChartTD").show();
//                $("#loading").hide();
            }
            else{
//                 $("#dashquickTrend").dialog('open');

//                var jsondata = JSON.parse(data)["data"];
                  var chartData ={};
            var rowMeasId =[];
            var rowMeasName =[];
            var rowViewIds = [];
            var rowViewName =[];
            var aggregation =[];
            if(typeof JSON.parse(data)["measureids"] !== "undefined"){
                rowMeasId = JSON.parse(data)["measureids"];
            }
            if(typeof JSON.parse(data)["measures"] !="undefined"){
                rowMeasName = JSON.parse(data)["measures"];
            }
            if(typeof JSON.parse(data)["viewbyids"] !== "undefined"){
                rowViewIds = JSON.parse(data)["viewbyids"];
            }
            if(typeof JSON.parse(data)["viewbynames"] !="undefined"){
                rowViewName = JSON.parse(data)["viewbynames"];
            }
            if(typeof JSON.parse(data)["Aggregation"] !="undefined"){
                aggregation= JSON.parse(data)["Aggregation"];
            }
//                $("#chartData").val(JSON.stringify(JSON.parse(JSON.parse(data)["meta"])["chartData"]));
//                var meta = JSON.parse(JSON.parse(data)["meta"]);
                $("#viewby").val(rowViewName);
                $("#viewbyIds").val(rowViewIds);
                $("#measure").val(rowMeasName);
                $("#measureIds").val(rowMeasId);
                $("#aggregation").val(aggregation);
//                $("#drilltype").val((meta["drillType"]));
//              $("#draggableViewBys").val('');
                $("#type").val("quick");



            var chartDetails={};
                        var viewBys=rowViewName;
                        var viewIds=rowViewIds;


                        chartDetails["viewBys"] = viewBys;
                            chartDetails["chartType"] = "quickAnalysis";
                            chartDetails["viewByLevel"] = "single";
                            chartDetails["meassures"] = rowMeasName;
                            chartDetails["aggregation"] = aggregation;
                            chartDetails["viewIds"] = viewIds;
                            chartDetails["meassureIds"] = rowMeasId;
                            chartDetails["dimensions"] = viewIds;
                            chartDetails["size"] = "S";
                            chartData["chart1"] = chartDetails;

              var ctxPath=parent.document.getElementById("h").value;
           $("#chartData").val(JSON.stringify(chartData));
            $.post(ctxPath+'/reportViewer.do?reportBy=designGraphInDesigner&reportId='+dashboardId,
             function(data){
            var jsonVar=eval('('+data+')')
            var graphIds ='';
            var graphCount=0;
            var parentDiv = '';
            graphCount++;
            graphIds = graphIds+","+graphCount;

            graphIds=jsonVar;
//            parent.$("#graphlist").hide();
//            parentDiv = parent.document.getElementById('tabGraphs');
//            parentDiv.style.height = '250'
            $.ajax({
//                url: 'reportTemplateAction.do?templateParam=buildGraphs&gid='+graphIds+'&grptypid=Bar&graphChange=default&grpIds='+graphIds+'&REPORTID='+document.getElementById("REPORTID").value,
                url: 'reportTemplateAction.do?templateParam=buildGraphs&height=450&width=750&gid='+graphIds+'&grptypid=Line&isdashboard=true&graphChange=default&grpIds='+graphIds+'&REPORTID='+dashboardId+'&measurename='+measurename+'&targetElementId='+targetElementId+'&targetElemName='+targetElemName+'&priorid='+priorid+'&measureid='+ElementId, //changed by sruthi for jqplotgraph
                success: function(data){
                    if(data != ""){
                       generateQuickTrendChart1(data);
                }
}
            });


        })
//             $.ajax({
//                          type:'POST',
//                          data:parent.$("#graphForm").serialize(),
//                     url: ctxPath+"/reportViewer.do?repo
//                     rtBy=buildchartsWithObject&reportId="+reportId+"&reportName="+encodeURIComponent(parent.$("#graphName").val())+"&chartData="+encodeURIComponent(parent.$("#chartData").val()),
////                     url: ctxPath+"/reportViewer.do?reportBy=buildCharts&reportId="+reportId+"&reportName="+encodeURIComponent(parent.$("#graphName").val())+"&chartData="+parent.$("#chartData").val(),
//                            success: function(data){
//                                 parent.$("#loading").hide();
//                                 alert(grid+"   lkiyu")
//                          generateQuickTrendChart1(jsondata);
//                            }
//                    });

            }

  }
        });
//   parent.$("#dashquickTrend").html("");
//$("#dashquickTrend").dialog('open');
}
function generateQuickTrendChart1(data){
$("#iddiv").html("");
//$("#quickTrend").html('');
var parameterName = "";
if(typeof viewByFilterName !="undefined"){
    parameterName = viewByFilterName;
}
var chartId = "chart1";
var chartData = JSON.parse($("#chartData").val());
var rowViewBy = $("#viewby").val();
var rowViewId = $("#viewbyIds").val();
var quickViewname = [];
var quickViewId =[];
var selectedView = chartData[chartId]["viewBys"];
var selectedViewId = chartData[chartId]["viewIds"];
//alert(JSON.stringify(chartData))
var timeDim = ["Month Year","Month - Year","Month-Year","Time","Year","year","Month","Qtr","qtr","Month "];
for(var i=0;i<rowViewId.length;i++){

    for(var k=0;k<timeDim.length;k++){

//    alert(rowViewBy[i]+"::::"+timeDim[k])
    if(rowViewBy[i] == timeDim[k] ){
    quickViewname.push(rowViewBy[i]);
    quickViewId.push(rowViewId[i]);
    }
}
}

var html = "";
//html += "<div style='height:20px; width:100%'>";
//html += "<ul style='float:left'><li><strong>"+parameterName+"</strong></li></ul>";
html += "<div style='float:right'>"
html += "<select style='float:right;margin-right:10px'>";
for(var i=0;i<chartData[chartId]["meassures"].length;i++){
    html += "<option id='"+i+"' value='"+chartData[chartId]["meassures"][i]+"' onclick='changeMeasureArray(this.id)'>"+chartData[chartId]["meassures"][i]+"</option>";
}
html += "</select></div>";

//if(chartCount>0){
//    html += "<div style='float:right;display:none'>"
//}
//else{
//html += "<div style='float:right'>"
//}
//html += "<select style='float:right;margin-right:10px'>";
//for(var l=0;l< quickViewname.length; l++ ){
//    if(l==0)
//    html += "<option id='"+l+"' value='"+selectedView+":"+selectedViewId+"' onclick='changeQuickGroup(this.value)'>"+selectedView+"</option>";
//   if(quickViewname[l] !=selectedView)
//   html += "<option id='"+l+"' value='"+quickViewname[l]+":"+quickViewId[l]+"' onclick='changeQuickGroup(this.value)'>"+quickViewname[l]+"</option>";
//}
//html += "</select></div>";
//html += "</div>";
//html +="<div id='Hchart1' style='display:block;float:left;width:85%'></div>";

$("#iddiv").html(html);
$("#iddiv").parent().css({ top: '30px' });
generateTrendChart1(data);
}

function generateTrendChart1(jsonData){
//  graphData=JSON.parse(jsonData);
// $("#Hchart1").html('');

$("#iddiv").html("");
//$("#quickTrend").html('');
var parameterName = "";
if(typeof viewByFilterName !="undefined"){
    parameterName = viewByFilterName;
}
var chartId = "chart1";
var chartData = JSON.parse($("#chartData").val());
var rowViewBy = $("#viewby").val();
var rowViewId = $("#viewbyIds").val();
var quickViewname = [];
var quickViewId =[];
var selectedView = chartData[chartId]["viewBys"];
var selectedViewId = chartData[chartId]["viewIds"];
//added by sruthi for target and prior jqplotgraph
 var   targetElementId='<%=targetElementId%>';
  var targetElemName='<%=targetElemName%>';
  var priorid='<%=priorElementId%>';
  var priorname='<%=priorname%>';
  //ended by sruthi
//alert(JSON.stringify(chartData))
var timeDim = ["Month Year","Month - Year","Month-Year","Time","Year","year","Month","Qtr","qtr","Month "];
//for(var i=0;i<rowViewId.length;i++){
//
//    for(var k=0;k<timeDim.length;k++){

//    alert(rowViewBy[i]+"::::"+timeDim[k])
//    if(rowViewBy[i] == timeDim[k] ){
if(rowViewBy[0]=="TIME"){
    quickViewname.push(rowViewBy[0]);

    quickViewId.push(rowViewBy[0]);

}else{
    quickViewname.push(rowViewBy[0]);
    quickViewId.push(rowViewId[0]);
}
//    }
//}
//}

var html = "";
//html += "<div style='height:20px; width:100%'>";
//html += "<ul style='float:left'><li><strong>"+parameterName+"</strong></li></ul>";
html += "<div style='float:right'>"
//html += "<select style='float:right;margin-right:10px'>";
//for(var i=0;i<chartData[chartId]["meassures"].length;i++){
//    html += "<option id='"+i+"' value='"+chartData[chartId]["meassures"][i]+"' onclick='changeMeasureArray(this.id)'>"+chartData[chartId]["meassures"][i]+"</option>";
//}
//html += "</select></div>";
//changed  by sruthi for jqplotgraph
if(priorflag){
 html=html+"<table align='center'><tr><td><input id='priorid' type='checkbox' checked name='priorid' value='priorid'  onclick=\"targetviewbychange('"+dashboardId+"','"+dashletId+"','"+ElementId+"','"+measurename+"','"+selectedView+"','"+targetElementId+"','"+targetElemName+"','"+priorid+"')\">"+priorname+"</td>";
}else
 html=html+"<table align='center'><tr><td><input id='priorid' type='checkbox'  name='priorid' value='priorid'  onclick=\"targetviewbychange('"+dashboardId+"','"+dashletId+"','"+ElementId+"','"+measurename+"','"+selectedView+"','"+targetElementId+"','"+targetElemName+"','"+priorid+"')\">"+priorname+"</td>";
if(targetElementId !=null &&targetElementId !="null"){
if(targetflag)
html=html+"<td><input id='targetid' type='checkbox' checked name='targetid' value='targetid'  onclick=\"targetviewbychange('"+dashboardId+"','"+dashletId+"','"+ElementId+"','"+measurename+"','"+selectedView+"','"+targetElementId+"','"+targetElemName+"','"+priorid+"')\">"+targetElemName+"</td>";
else
html=html+"<td><input id='targetid' type='checkbox'  name='targetid' value='targetid'  onclick=\"targetviewbychange('"+dashboardId+"','"+dashletId+"','"+ElementId+"','"+measurename+"','"+selectedView+"','"+targetElementId+"','"+targetElemName+"','"+priorid+"')\">"+targetElemName+"</td>";
}
for(var i=0;i<chartData[chartId]["meassures"].length;i++){
    if(measureflag){
 html=html+"<td><input id='measureid' type='checkbox' checked name='measureid' value='measureid' onclick=\"currnetviewbychange('"+dashboardId+"','"+dashletId+"','"+ElementId+"','"+measurename+"','"+selectedView+"','"+targetElementId+"','"+targetElemName+"','"+priorid+"')\">"+chartData[chartId]["meassures"][i]+"</td></tr>";
}else
 html=html+"<td><input id='measureid' type='checkbox'  name='measureid' value='measureid' onclick=\"currnetviewbychange('"+dashboardId+"','"+dashletId+"','"+ElementId+"','"+measurename+"','"+selectedView+"','"+targetElementId+"','"+targetElemName+"','"+priorid+"')\">"+chartData[chartId]["meassures"][i]+"</td></tr>";

}


html += "</table></div>";

//if(chartCount>0){
//    html += "<div style='float:right;display:none'>"
//}
//else{
html += "<div style='float:right'>"
//}
html += "<select style='float:right;margin-right:10px;border:;'>";
for(var l=0;l< quickViewname.length; l++ ){
    if(l==0)
    html += "<option id='"+l+"' value='"+selectedView+":"+selectedViewId+"'onclick=\"viewbychange('"+dashboardId+"','"+dashletId+"','"+ElementId+"','"+measurename+"','"+selectedView+"')\">"+selectedView+"</option>";
   if(quickViewname[l] ==selectedView)
   html += "<option id='"+l+"' value='"+quickViewname[0]+":"+quickViewId[0]+"' onclick=\"viewbychange('"+dashboardId+"','"+dashletId+"','"+ElementId+"','"+measurename+"','null')\">"+quickViewname[0]+"</option>";
if(selectedView =="TIME"){
     html += "<option id='1' onclick=\"viewbychange('"+dashboardId+"','"+dashletId+"','"+ElementId+"','"+measurename+"','Month Year')\">Month Year</option>";
//     html += "<option id='"+l+"' value='"+quickViewname[0]+":"+quickViewId[0]+"' onclick=\"viewbychange('"+dashboardId+"','"+dashletId+"','"+ElementId+"','"+measurename+"','null')\">"+quickViewname[0]+"</option>";

}
if(selectedView =="Month Year" || selectedView =="Month - Year" || selectedView =="Month-Year"){
         html += "<option id='1' onclick=\"viewbychange('"+dashboardId+"','"+dashletId+"','"+ElementId+"','"+measurename+"','TIME')\">TIME</option>";

}
}
html += "</select></div>";
html += "</div>";
//html +="<div id='Hchart1' style='display:block;float:left;width:85%'></div>";

//$("#quickTrend").html(html);

//  var chartData = JSON.parse(parent.$("#chartData").val());
$("#iddiv").html(html);
$("#loading").hide();
 var charts = Object.keys(chartData);
// var data = JSON.parse(jsonData);
 for(var l=0;l<charts.length;l++){
 var divId = chartId;
html +="<div id='"+divId+"' style='display:block;float:left;width:85%'></div>";
html += "<div class='tooltip' id='my_tooltip1' style='display: none'></div>";

}
$("#iddiv").html(html);
$("#"+divId).html(jsonData);


// for (var kq = 0; kq < charts.length; kq++) {
//              var ch = "chart1";
//              var html = "";
//              var currData=[];
//              var records = 12;
//              var chartType = chartData[ch]["chartType"];
//              var chartSize = chartData[ch]["size"];
//              var width = $(window).width() * .45;
//              if (typeof chartData[ch]["records"] !== "undefined" && chartData[ch]["records"] !== "") {
//            records = chartData[ch]["records"];
//        }
//              for(var m=0;m<(data[ch].length < records ? data[ch].length : records);m++){
//                  currData.push(data[ch][m]);
//              }
//
//
////     measureData = currData;
//   if(chartType == "quickAnalysis"){
//
//       if(chartSize == "SS"){
//            buildQuickLine(ch, currData, chartData[ch]["viewBys"], chartData[ch]["meassures"],width*1.5,"250");
//       }else {
//           buildLine(ch, currData, chartData[ch]["viewBys"], chartData[ch]["meassures"],width,"500");
////           parent.generateQuickTrendChart11(ch, currData, chartData[ch]["viewBys"], chartData[ch]["meassures"],width,"500");
////buildQuickLine(ch, currData, chartData[ch]["viewBys"], chartData[ch]["meassures"],width,"500");
//   }
//   }else {
//       if(chartSize=="SS"){
//           $("#Hchart1").css("width","50%");
//           $("#Hchart2").css("width","50%");
//           buildQuickBar(ch, currData, chartData[ch]["viewBys"], chartData[ch]["meassures"],width,"200");
//       }
//       else{
//       buildQuickBar(ch, currData, chartData[ch]["viewBys"], chartData[ch]["meassures"],width,"300");
//   }
// }
//}
}
           </script>
    </head>
    <body>

        <div id="iddiv" style="height:450px;width:750px;">

        </div>
        <form name="scheduleDbrdFqorm" id="scheduleDqbrdForm">
 <input type="hidden" id="chartData" name="chartData" />
                                          <input type="hidden" id="viewby" name="viewby"/>
                                           <input type="hidden" id="fromoneview" name="fromoneview" value="dashboard" />
                 <input type="hidden" id="viewbyIds" name="viewbyIds"/>
                  <input type="hidden" name="measure" id="measure"/>
            <input type="hidden" name="measureIds" id="measureIds"/>
            <input type="hidden" name="aggregation" id="aggregation"/>
             <input type="hidden" id="type" name="type"  />
        </form>
    </body>
</html>
