<%--
    Document   : OLAPGraph
    Created on : 11 Sep, 2012, 7:38:19 PM
    Author     : progen
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" import="java.util.ArrayList"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%
            String themeColor = "blue";
            String dashBoardId = request.getParameter("dashBoardId");
            String dashletId=request.getParameter("dashletId");
            String callFrom=request.getParameter("callFrom");
            String reportId = request.getParameter("reportId");
            String graphId = request.getParameter("graphId");
            String graphNum = request.getParameter("graphNum");
            String width = request.getParameter("width");
            String height = request.getParameter("height");
           ArrayList timeDetails = (ArrayList<String>)request.getAttribute("timeDetails");
            String isRepDate = request.getParameter("isrepDate");
            String oneviewID = request.getParameter("oneviewID");
            String regid = request.getParameter("regid");
            String chartname = request.getParameter("chartname");
            String repname = request.getParameter("repname");
            String rolename = request.getParameter("rolename");
            String viewbyid = request.getParameter("viewbyid");
            String contextPath=request.getContextPath();

%>


<html>
    <head>
<!--        <script type="text/javascript" src="<%=request.getContextPath()%>/TableDisplay/JS/pbTableMapJS.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/pbReportViewerJS.js"></script>-->

<!--        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/dashboardDesign.js"></script>-->
        <script src="<%=contextPath%>/javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/lib/jquery/autocomplete/jquery.autocomplete-min.js"></script>

        <script src="<%=contextPath%>/javascript/lib/jquery/js/jquery.tablesorter.mod.js" type="text/javascript"></script>
        <script src="<%=contextPath%>/javascript/lib/jquery/js/jquery.tablesorter.collapsible.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/lib/jquery/js/jquery.tablesorter.innergrid.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/quicksearch.js"></script>
        <script  type="text/javascript" src="<%=contextPath%>/javascript/jquery.contextMenu.js" ></script>

         <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=themeColor%>/TableCss.css" rel="stylesheet" />
         <link href="<%=contextPath%>/css/styles.css" rel="stylesheet" type="text/css">
        <link rel="stylesheet" href="<%=contextPath%>/tablesorter/themes/<%=themeColor%>/style.css" type="text/css" media="print, projection, screen" />

        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/treeviewstyle/screen.css" />
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=themeColor%>/jquery-ui-1.7.3.custom.css" rel="stylesheet" />
        <link type="text/css" href="<%=contextPath%>/javascript/lib/jquery/autocomplete/styles.css" rel="stylesheet" />
        <link rel="stylesheet" href="<%=contextPath%>/tablesorter/themes/<%=themeColor%>/style.css" type="text/css" media="print, projection, screen" />
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=themeColor%>/pbReportViewerCSS.css" rel="stylesheet" />
<!--        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/themes/<%=themeColor%>/TableCss.css" rel="stylesheet" />-->
<!--        <link href="<%=request.getContextPath()%>/stylesheets/themes/<%=themeColor%>/css.css" rel="stylesheet" type="text/css">-->
        <link href="<%=contextPath%>/stylesheets/themes/<%=themeColor%>/style.css" rel="stylesheet" type="text/css">
        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/treeviewstyle/jquery.treeview.css" />
        <link href="<%=contextPath%>/stylesheets/jquery.contextMenu.css" rel="stylesheet" type="text/css" />
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=themeColor%>/metadataButton.css" rel="stylesheet" />
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=themeColor%>/TableCss.css" rel="stylesheet" />
        <script type="text/javascript" src="<%=contextPath%>/jQuery/d3/d3.v3.min.js"></script>
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=themeColor%>/ReportCss.css" rel="stylesheet"/>
                <script type="text/javascript" src="<%=contextPath%>/javascript/reportviewer/ReportViewer.js"></script>
                <script type="text/javascript" src="<%=contextPath%>/jQuery/d3/chartTypes.js"></script>
                 <script type="text/javascript" src="<%=contextPath%>/jQuery/d3/chartTypesGroup.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/jQuery/d3/chartTypeOthers.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/jQuery/d3/chartTypeBars.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/jQuery/d3/chartTypeCircular.js"></script>
         <script type="text/javascript" src="<%=contextPath%>/javascript/reportviewer/graphViewer.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/OneViewJS.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/jqplot/jquery.js"></script>
           <script type="text/javascript" src="<%=contextPath%>/javascript/jqplot/jquery.min.js"></script>
           <script type="text/javascript" src="<%=contextPath%>/javascript/jqplot/jquery.jqplot.js"></script>
           <script type="text/javascript" src="<%=contextPath%>/javascript/jqplot/jquery.jqplot.min.js"></script>
           <script type="text/javascript" src="<%=contextPath%>/javascript/jqplot/jqplot.barRenderer.min.js"></script>
           <script type="text/javascript" src="<%=contextPath%>/javascript/jqplot/jqplot.categoryAxisRenderer.min.js"></script>
           <script type="text/javascript" src="<%=contextPath%>/javascript/jqplot/jqplot.pointLabels.min.js"></script>
           <script type="text/javascript" src="<%=contextPath%>/javascript/jqplot/jqplot.pieRenderer.min.js"></script>
           <script type="text/javascript" src="<%=contextPath%>/javascript/jqplot/jqplot.donutRenderer.min.js"></script>
           <script type="text/javascript" src="<%=contextPath%>/javascript/jqplot/jqplot.canvasAxisLabelRenderer.min.js"></script>
           <script type="text/javascript" src="<%=contextPath%>/javascript/jqplot/jqplot.canvasTextRenderer.min.js"></script>
           <script type="text/javascript" src="<%=contextPath%>/javascript/jqplot/jqplot.dateAxisRenderer.min.js"></script>
           <script type="text/javascript" src="<%=contextPath%>/javascript/jqplot/jqplot.logAxisRenderer.min.js"></script>
           <script type="text/javascript" src="<%=contextPath%>/javascript/jqplot/jqplot.canvasAxisTickRenderer.min.js"></script>
           <script type="text/javascript" src="<%=contextPath%>/javascript/jqplot/jqplot.highlighter.min.js"></script>
           <script type="text/javascript" src="<%=contextPath%>/javascript/jqplot/jqplot.bubbleRenderer.min.js"></script>
           <script type="text/javascript" src="<%=contextPath%>/javascript/jqplot/jqplot.funnelRenderer.js"></script>
           <script type="text/javascript" src="<%=contextPath%>/javascript/jqplot/jqplot.ClickableBars.js"></script>
           <script type="text/javascript" src="<%=contextPath%>/javascript/jqplot/jqplot.EnhancedLegendRenderer.js"></script>
           <script type="text/javascript" src="<%=contextPath%>/javascript/jqplot/jqplot.canvasOverlay.min.js"></script>
           <script type="text/javascript" src="<%=contextPath%>/jQuery/d3/customtooltip.js"></script>
        <link type="text/css" href="<%=contextPath%>/css/d3/tooltip.css" rel="stylesheet"/>
           <link href="<%=contextPath%>/javascript/jqplot/jquery.jqplotOneview.min.css" rel="stylesheet" type="text/css" />
            <script type="text/javascript" src="<%=contextPath%>/javascript/jqplot/jqplot.cursor.min.js"></script>

        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>OLAP Graph</title>
        <style type="text/css">
            .custom-button-color{
                -moz-border-radius-bottomleft:4px;
                -moz-border-radius-bottomright:4px;
                -moz-border-radius-topleft:4px;
                -moz-border-radius-topright:4px;
                 background:#00FFFF url(images/ui-bg_glass_75_79c9ec_1x400.png) 50% 50% repeat-x;
                border:1px solid #448DAE;
                color:#000;
                FONT-SIZE: 11px;
                FONT-FAMILY: Verdana;
                VERTICAL-ALIGN: middle;
                HEIGHT: 20px;
                cursor:pointer;
            }
            tr.spaceUnder > td{
                padding-bottom: 1em;

            }
            #selectable .ui-selecting { background: #FECA40; }
/*            #selectable .ui-selected { background: #F39814; color: white; }*/
/*            #selectable { list-style-type: none; margin: 0; padding: 0; width: 60%; }*/

            #selectable .ui-selected {
                    background:#357EC7 url(images/ui-bg_glass_75_79c9ec_1x400.png) repeat-x scroll 50% 50%;
                    color:white;
                    }


               #selectable {
                    background:none repeat scroll 0 0 white;
                    list-style:none outside none;
                    width:60%;
                    }

                #selectable li {
                    font-family:verdana;
                    font-size:11px;
/*                    height:18px;*/
                    margin-bottom:1px;
                    margin-left:1px;
                    margin-right:1px;
                    margin-top:1px;
                    padding-bottom:0.4em;
                    padding-left:0.4em;
                    padding-right:0.4em;
                    padding-top:0.4em;
                    width:120px;
                    background:#CCCCCC;
                    border:1px solid #CCCCCC;
                    cursor:pointer;
                    color:black;
                    }

             .overlapDiv{
            z-index: 10000;
            }
            .alinkclass a {font-family:Verdana; font-size:11px;color:black}
            .alinkclass a:link {color:grey;}
/*            .example1 a:visited {color:seagreen;}*/
            .alinkclass a:hover {text-decoration:none;color:#357EC7;}

            .alinkclass-selected a {font-family:Verdana; font-size:11px;color:#357EC7;}
            .alinkclass-selected a:link {color:#357EC7;}
/*            .example1 a:visited {color:seagreen;}*/
            .alinkclass-selected a:hover {text-decoration:none;color:grey;}
/*            .example1 a:active {color:red;text-decoration: none} font-weight:bold;*/


/*            #selectable li { margin: 3px; padding: 0.4em; font-size: 1.4em; height: 18px; }*/

/*            .ui-buttonset { margin-right: 7px; }
            .ui-buttonset .ui-button { margin-left: 0; margin-right: -.3em; }*/
/*ul					{ list-style: none; }
ul.dropDownMenu                         { position: relative }
ul.dropDownMenu li                      { font-weight: bold; float: left; zoom: 1; background: #b4d9ee;z-index: 1002 }
ul.dropDownMenu a:hover		        { color: #000; text-decoration: none }
ul.dropDownMenu a:active                { color: #b4d9ee; }
ul.dropDownMenu li a                    { display: block; padding: 4px 8px; border-right: 1px solid #b4d9ee;color: #000 }
ul.dropDownMenu li:last-child a         { border-right: none; }  Doesn't work in IE
ul.dropDownMenu li.hover,
ul.dropDownMenu li:hover                { background: #fff; color: black; position: relative; }
ul.dropDownMenu li.hover a              { color: #000; }



	LEVEL TWO

ul.dropDownMenu ul 						{ width: 100px; visibility: hidden; position: absolute; top: 100%; left: 0; }
ul.dropDownMenu ul li 					{ font-weight: normal; background: #64B2FF; color: #000;border:1px solid #ccc; float: none;}

 IE 6 & 7 Needs Inline Block
ul.dropDownMenu ul li a					{ border-right: none; width: 100%; display: inline-block; }


	LEVEL THREE

ul.dropDownMenu ul ul 					{ left: 100%; top: 0; }
ul.dropDownMenu li:hover > ul 			{ visibility:visible; }*/

        .customLi{
            font-family: verdana;
            font-size: 11px;
            margin: 1px 1px 0px 0px;
            padding: 0.4em;
            width: 80px;
            background: #357EC7 url(images/ui-bg_glass_75_79c9ec_1x400.png) repeat-x scroll 50% 50%; /*#64B2FF;*/
            cursor: pointer;
            color: white;
}
li.customLi:hover {
    background:white;
    color:black;
}
.graphTypesStyle{
  list-style-type: none;
  margin: 0;
  padding: 0;
}
.graphTypesStyle li{
    font-family: verdana;
    font-size: 10px;
    padding: 0.3em;
    background: #357EC7 url(images/ui-bg_glass_75_79c9ec_1x400.png) repeat-x scroll 50% 50%; /*#64B2FF;*/
    color:white;
    border-bottom: 1px solid #ccc;
    width: 101px;
    cursor:pointer;
}

.graphTypesStyle li:hover{
    font-size: 12px;
  background: #f6f6f6;
  color:black;
}
#selecteGraphLI{
    font-size: 11px;
    background: #f6f6f6;
  color:black;
}



        </style>

       
    </head>
    <body>

        <form name="OLAPGraphForm" method="post" action="">

            <div id="OLAPGraphDiv" class="tabs" style=" max-width: 100%;">
                <div id="GraphRegion" width="<%=width%>" height="<%=height%>" style="overflow-x:hidden;overflow-y:hidden;"></div>
                <script  type="text/javascript">
                    if(callFrom == "oneView"){

                       loadOLAPGraphforOneView();

                    }else{
                    loadBasicView();
                    }
                </script>


            </div>


        </form>
                 <script type="text/javascript">
            var dashletId=<%=dashletId%>;
//            var oneviewID=<%=oneviewID%>;
//            var regid=<%=regid%>;
//            var chartname=<%=chartname%>;
            var dashBoardId=<%=dashBoardId%>;
            var callFrom='<%=callFrom%>';
            var reportId='<%=reportId%>';
            var graphId='<%=graphId%>';
            var graphNum='<%=graphNum%>';
            var width='<%=width%>';
            var height='<%=height%>';
            var timeDetails='<%=timeDetails%>';
            var isRepDate='<%=isRepDate%>';
            var initialtime='<%=timeDetails%>';
            var drillURL="";
            var drillPoint="";
            var drillType="standardDrill";
            var ctxpath='<%=request.getContextPath()%>';
            var curentdispType="graph";
var oneviewID=<%=oneviewID%>;
            var regid=<%=regid%>;
            var repname='<%=repname%>';
            var rolename='<%=rolename%>';
            var chartname='<%=chartname%>';
            var viewbyid='<%=viewbyid%>';
            var dialog = parent.$("#OLAPGraphDialog").dialog();
                var titlebar = dialog.parents('.ui-dialog').find('.ui-dialog-titlebar-close');
                $(dialog.parents('.ui-dialog').find('#reset')).remove();

                $('<span id="reset" style="float:right; margin-right: 3em; cursor: pointer;">Reset</span>')
                    .insertBefore(titlebar)
                    .click(function() {
                        drillType="standardDrill";

                        $("#OLAPGraphRegion").html('<center><img id="imgId" src="images/ajax1.gif" align="middle"  width="75px" height="75px"  style="position:absolute" /></center>');
                       $.ajax({
                    url: "dashboardViewer.do?reportBy=getOLAPGraphforOneViewd3&viewById="+viewbyid+"&REPORTID="+reportId+"&graphId="+graphId+"&regid="+regid+"&action=reset"+"&graphNum="+graphNum+"&width="+width+"&height="+height+"&timeDetails="+initialtime+"&isRepDate="+isRepDate+"&isOLAPInitial=true&drillType="+drillType+"&oneviewID="+oneviewID+"&dispType="+curentdispType+"&repname="+encodeURIComponent(repname)+"&rolename="+rolename+"&chartname="+chartname,
                    success: function(data){
                    parent.$("#adhocdrills").val("");
                        $("#GraphRegion").html(data);
                        generateJsonDataOneview1("OLAPGraphRegion",oneviewID,regid,chartname,"add",reportId,repname,'null','null')
                    }
                });
                    });
            function loadBasicView(){
                $("#GraphRegion").html('<center><img id="imgId" src="images/ajax1.gif" align="middle"  width="75px" height="75px"  style="position:absolute" /></center>');
                $.ajax({
                    url: "dashboardViewer.do?reportBy=getInitialViewofOLAPGraph&dashletId="+dashletId+"&dashBoardId="+dashBoardId,
                    success: function(data){
                        $("#GraphRegion").html(data);
                    }
                });
            }
            function buildGraphOnViewby(dashboardId,dashletId,viewById,graphId){
            $("#zoom"+graphId).html('<center><img id="imgId" src="images/ajax1.gif" align="middle"  width="75px" height="75px"  style="position:absolute" /></center>');
            $.ajax({
                    url: "dashboardViewer.do?reportBy=buildGraphOnViewby&dashletId="+dashletId+"&dashBoardId="+dashboardId+"&viewById="+viewById,
                    success: function(data){
                        $("#GraphRegion").html(data);
                    }
                });

            }
            function closeOLAPVew(dashboardId,dashletId){
//                $.ajax({
//                    url: "dashboardViewer.do?reportBy=closeOLAPView&dashletId="+dashletId+"&dashBoardId="+dashboardId,
//                    success: function(data){
                        parent.$("#OLAPGraphDialog").dialog('close');
//                    }
//                });

     }
     function loadOLAPGraphforOneView(){
         $("#GraphRegion").html('<center><img id="imgId" src="images/ajax1.gif" align="middle"  width="75px" height="75px"  style="position:absolute" /></center>');
//                alert("reportId="+reportId+" graphId="+graphId);
//                alert(reportId);
  var chartData = JSON.parse(parent.$("#chartData").val());
var viewbyid=chartData[chartname]["viewIds"][0];
                $.ajax({
                    url: "dashboardViewer.do?reportBy=getOLAPGraphforOneViewd3&viewById="+viewbyid+"&REPORTID="+reportId+"&oneviewID="+oneviewID+"&regid="+regid+"&graphId="+graphId+"&action=open"+"&graphNum="+graphNum+"&width="+width+"&height="+height+"&timeDetails="+timeDetails+"&isRepDate="+isRepDate+"&isOLAPInitial=true"+"&drillType="+drillType+"&repname="+encodeURIComponent(repname)+"&rolename="+rolename+"&chartname="+chartname,
                    success: function(data){
                        drillType=$("#olapDrillComboBox").val();
                        $("#GraphRegion").html(data);
  parent.$("#graphsId").val(reportId);
                     parent.$("#graphName").val(repname);
                     parent.$("#busrolename").val(rolename);
                      parent.$("#chartname").val(chartname);
                       $("#OLAPGraphRegion").html('<div class="tooltip" id="my_tooltip1" style="display: none"></div>');
                        generateJsonDataOneview1("OLAPGraphRegion",oneviewID,regid,chartname,"add",reportId,repname,'null','null')
//                        generateJsonDataOneview("OLAPGraphRegion",oneviewID,regid,chartname,"add")
                    }
                });
     }
     function buildGraphOnViewbyForOneView(reportId,viewById,graphId,defaultViewbyId,viewbyame){
     var drillUrlStr="&CBOVIEW_BY"+defaultViewbyId+"="+viewById;
     parent.$("#adhocdrills").val("");
     //alert(drillUrlStr);
     $("#OLAPGraphRegion").html('<center><img id="imgId" src="images/ajax1.gif" align="middle"  width="75px" height="75px"  style="position:absolute" /></center>');
            $.ajax({
                    url: "dashboardViewer.do?reportBy=getOLAPGraphforOneViewd3&REPORTID="+reportId+"&oneviewID="+oneviewID+"&graphId="+graphId+"&regid="+regid+"&viewById="+viewById+"&graphNum="+graphNum+"&timeDetails="+timeDetails+"&isRepDate="+isRepDate+"&width="+width+"&height="+height+"&drillType="+drillType+drillUrlStr+"&repname="+encodeURIComponent(repname)+"&rolename="+rolename+"&chartname="+chartname,
                    success: function(data){
                        $("#GraphRegion").html("");
                        $("#GraphRegion").html(data);
                        parent.$("#graphsId").val(reportId);
                     parent.$("#graphName").val(repname);
                     parent.$("#busrolename").val(rolename);
                      parent.$("#chartname").val(chartname);
                       var chartData = JSON.parse(parent.$("#chartData").val());
                       chartData[chartname]["viewIds"][0]=viewById
                       chartData[chartname]["viewBys"][0]=viewbyame
                        parent.$("#chartData").val(JSON.stringify(chartData));
                                 parent.$("#loading").hide();
                                 var dashletid="OLAPGraphRegion";
 generateJsonDataOneview1("OLAPGraphRegion",oneviewID,regid,chartname,"olap",reportId,repname,viewbyame,viewById)
               }
                });
     }
     function buildtableOnViewbyForOneView(reportId,viewById,graphId,defaultViewbyId,viewbyame){
     var drillUrlStr="&CBOVIEW_BY"+defaultViewbyId+"="+viewById;
     parent.$("#adhocdrills").val("");
     //alert(drillUrlStr);
     $("#OLAPGraphRegion").html('<center><img id="imgId" src="images/ajax1.gif" align="middle"  width="75px" height="75px"  style="position:absolute" /></center>');
            $.ajax({
                    url: "dashboardViewer.do?reportBy=getOLAPGraphforOneViewd3&REPORTID="+reportId+"&oneviewID="+oneviewID+"&graphId="+graphId+"&regid="+regid+"&viewById="+viewById+"&graphNum="+graphNum+"&timeDetails="+timeDetails+"&isRepDate="+isRepDate+"&width="+width+"&height="+height+"&drillType="+drillType+drillUrlStr+"&repname="+encodeURIComponent(repname)+"&rolename="+rolename+"&chartname="+chartname,
                    success: function(data){
                        $("#GraphRegion").html("");
                        $("#GraphRegion").html(data);
                        parent.$("#graphsId").val(reportId);
                     parent.$("#graphName").val(repname);
                     parent.$("#busrolename").val(rolename);
                      parent.$("#chartname").val(chartname);
                       var chartData = JSON.parse(parent.$("#chartData").val());
                       chartData[chartname]["viewIds"][0]=viewById
                       chartData[chartname]["viewBys"][0]=viewbyame
                       var charttype= chartData[chartname]["chartType"];
                                              chartData[chartname]["chartType"]="Table";
                        parent.$("#chartData").val(JSON.stringify(chartData));
                                 parent.$("#loading").hide();
                                 var dashletid="OLAPGraphRegion";
 generateJsonDataOneview1("OLAPGraphRegion",oneviewID,regid,chartname,"olap",reportId,repname,viewbyame,viewById)

  chartData[chartname]["chartType"]=charttype;
  parent.$("#chartData").val(JSON.stringify(chartData));
               }
                });
     }
     function drillOLAPGraph(oneUrl,datapoint){
            drillURL=oneUrl;
            drillPoint=datapoint;
            var drillType=$("#olapDrillComboBox").val();
            if(drillType == "standardDrill"){
                localOLAPDrill();
            }else{
                $("#DrillDiv").toggle(500);
            }


     }
          function drilldualOLAPGraph(oneUrl,datapoint,i){
            drillURL=oneUrl;
            drillPoint=datapoint;
            var drillType=$("#olapDrillComboBox").val();
            if(drillType == "standardDrill"){
                localOLAPDrill();
            }else{
                if(i%2==0)
                $("#DrillDiv").toggle(500);
            }


     }

     function updateTimeDetails(viewbyType){
     if(timeDetails instanceof Array){
         timeDetails.splice(3,1,viewbyType);
     }else{
         var details=new Array();
          var time=timeDetails.split(",");
         for(var i=0;i<time.length;i++){
             if(i==3){
                 details.push(viewbyType);
             }else{
                 details.push(time[i]);
             }
        }
     timeDetails=details;
     alert("timememdetails"+timeDetails)
     }
     }
     function buildTimeBaseLine(reportId,viewbyType,graphId,viewById){
//     alert(timeDetails);

//     var time=timeDetails.split(",");
//       alert(timeDetails);
       if(isRepDate=='false'){
//           alert(isRepDate)
       updateTimeDetails(viewbyType);
       }
//       alert(timeDetails);

//
//     timeDetails=timeDetails.replace(time[3],viewbyType);
//     alert(timeDetails);
     $("#OLAPGraphRegion").html('<center><img id="imgId" src="images/ajax1.gif" align="middle"  width="75px" height="75px"  style="position:absolute" /></center>');
            $.ajax({
                    url: "dashboardViewer.do?reportBy=getOLAPGraphforOneView&REPORTID="+reportId+"&graphId="+graphId+"&viewById="+viewById+"&graphNum="+graphNum+"&timeDetails="+timeDetails+"&isRepDate="+isRepDate+"&width="+width+"&height="+height+"&viewbyType="+viewbyType+"&drillType="+drillType,
                    success: function(data){
                        $("#GraphRegion").html(data);
                    }
                });

     }
     function buildGraphOnTimeForOneView(reportId,Viewtype,graphId,defaultViewbyId){
//     alert('Trend Graph To be build...!!');
     var drillUrlStr="&CBOVIEW_BY"+defaultViewbyId+"="+Viewtype;
     $("#OLAPGraphRegion").html('<center><img id="imgId" src="images/ajax1.gif" align="middle"  width="75px" height="75px"  style="position:absolute" /></center>');
            $.ajax({
                    url: "dashboardViewer.do?reportBy=getOLAPGraphforOneView&REPORTID="+reportId+"&graphId="+graphId+"&viewById="+Viewtype+"&graphNum="+graphNum+"&timeDetails="+timeDetails+"&isRepDate="+isRepDate+"&width="+width+"&height="+height+"&drillType="+drillType+drillUrlStr,
                    success: function(data){
                        $("#GraphRegion").html(data);
                    }
                });
     }

//     function resetOLAP(){
//     alert('helloWorld');
//     }
function buildProgenTable(reportid){
            //alert(reportid)
        var divIdObj="";
        divIdObj = document.getElementById("progenTable");
        divIdObj.innerHTML ='<iframe name=\"progenTableFrame\" id=\"progenTableFrame\" width=\"100%\" height=\"'+height+'px\" frameborder=\"0\" style=\"overflow:auto;\" src=\"OLAPTable.jsp?tabId='+reportid+'&height='+height+'\"></iframe>';

       //alert(reportid)
//        var divIdObj="";
//        divIdObj = document.getElementById("progenTable");
//        divIdObj.innerHTML="";
//        divIdObj.innerHTML ='<iframe name=\"iframe1\" id=\"iframe1\" width=\"100%\" height=\"'+height+'px\" frameborder=\"0\" style=\"overflow-x: hidden;overflow-y: hidden;\" src=\"TableDisplay/pbDisplay.jsp?tabId='+reportid+'\"></iframe>';

        //var dispType="table";
        //buildTableORGraph(reportid,graphId,"",graphNum,dispType);
      //  window.location.href=window.location.href;
//         $("#OLAPGraphRegion").html('<center><img id="imgId" src="images/ajax.gif" align="middle"  width="75px" height="75px"  style="position:absolute" /></center>');
//            $.ajax({
//                    url: "dashboardViewer.do?reportBy=getOLAPGraphforOneView&REPORTID="+reportId+"&graphId="+graphId+"&viewById="+viewById+"&graphNum="+graphNum+"&timeDetails="+timeDetails+"&isRepDate="+isRepDate+"&width="+width+"&height="+height,
//                    success: function(data){
//                        $("#GraphRegion").html(data);
//                    }
//                });
        }
        function submiturls1($ch){
               var url = $ch;
               var view=(url).substring($ch.lastIndexOf("&")+1);
                    if(view.indexOf("CBOARP") != -1){
               var value=($ch).substring($ch.lastIndexOf("=")+1);
               $.post(
                   'reportViewer.do?reportBy=drillView&value='+value+'&repId='+reportId,
                   function(data){

               });
               }
                $("#GraphRegion").html('<center><img id="imgId" src="images/ajax1.gif" align="middle"  width="75px" height="75px"  style="position:absolute" /></center>');
                var drillUrl=url.replace("reportViewer.do?reportBy=viewReport", "dashboardViewer.do?reportBy=getOLAPGraphforOneView");
                $.ajax({
                    url: drillUrl+"&REPORTID="+reportId+"&graphId="+ graphId+"&graphNum="+graphNum+"&width="+width+"&height="+height+"&isRepDate="+isRepDate+"&dispType=table"+"&drillType="+drillType,
                    success: function(data){
                        $("#GraphRegion").html("");
                        $("#GraphRegion").html(data);
                    }
                });


            }
            function buildTableORGraph(reportId,graphId,viewById,dispType){
       // alert("test")
                    curentdispType=dispType;
//                    alert(timeDetails);
       $("#GraphRegion").html('<center><img id="imgId" src="images/ajax1.gif" align="middle"  width="75px" height="75px"  style="position:absolute" /></center>');
                $.ajax({
                    url: "dashboardViewer.do?reportBy=getOLAPGraphforOneView&REPORTID="+reportId+"&graphId="+graphId+"&graphNum="+graphNum+"&width="+width+"&height="+height+"&timeDetails="+timeDetails+"&isRepDate="+isRepDate+"&dispType="+dispType+"&drillType="+drillType,
                    success: function(data){

                        $("#GraphRegion").html(data);
                    }
                });
       }
       function localOLAPDrill(){
         $("#OLAPGraphRegion").html('<center><img id="imgId" src="images/ajax1.gif" align="middle"  width="75px" height="75px"  style="position:absolute" /></center>');
         var drillPointJson=JSON.stringify(new Array(drillPoint));
            $.ajax({
                    url: drillURL+drillPointJson+"&graphId="+graphId+"&timeDetails="+timeDetails+"&isRepDate="+isRepDate+"&width="+width+"&height="+height+"&graphNum="+graphNum+"&drillValue="+drillPoint+"&drillType="+drillType,
                    success: function(data){
                        $("#GraphRegion").html(data);
                    }
                });

       }
       function adhocOLAPDrill(currViewbyId,nextViewbyId){
//           alert(drillURL +' drillPOint'+ drillPoint+' currViewbyId:'+currViewbyId+' nextViewbyId:'+nextViewbyId);
           if(nextViewbyId != 'TIME' && currViewbyId!= 'TIME'){

               if(currViewbyId != nextViewbyId){
                   $("#OLAPGraphRegion").html('<center><img id="imgId" src="images/ajax1.gif" align="middle"  width="75px" height="75px"  style="position:absolute" /></center>');
                    if(nextViewbyId =='Day' || nextViewbyId== 'Week' || nextViewbyId== 'Month' || nextViewbyId == 'Quarter' || nextViewbyId== 'Year'){
                        updateTimeDetails(nextViewbyId);
//                        alert(timeDetails);
                        $.ajax({
                            url: drillURL+drillPoint+"&graphId="+graphId+"&timeDetails="+timeDetails+"&isRepDate="+isRepDate+"&width="+width+"&height="+height+"&graphNum="+graphNum+"&drillValue="+drillPoint+"&drillType="+drillType,
                            success: function(data){
                                $("#GraphRegion").html(data);
                            }
                        });
                   }else{
               var tempURL=drillURL;
               var tempSubStr=tempURL.substr(tempURL.indexOf("CBOVIEW_BY",0)).split("&");
//                       alert(tempSubStr);
               var oldnxtViewbyId=tempSubStr[0].substr(tempSubStr[0].indexOf("=",0)+1);
//                       alert(oldnxtViewbyId);
               var finalURL=drillURL.replace(oldnxtViewbyId, nextViewbyId);
//                       alert(finalURL +' currViewbyId:'+currViewbyId+' nextViewbyId:'+nextViewbyId);
                    $.ajax({
                       url: finalURL+drillPoint+"&graphId="+graphId+"&timeDetails="+timeDetails+"&isRepDate="+isRepDate+"&width="+width+"&height="+height+"&graphNum="+graphNum+"&drillValue="+drillPoint+"&drillType="+drillType,
                    success: function(data){
                        $("#GraphRegion").html(data);
                    }
                });
               }
           }
       }
       }
       function updateDrillType(){
        drillType=$("#olapDrillComboBox").val();

       }

       function viewAdhocDrillForOLAP(presentViewById,nextViewById,viewById,viewbyame,idArr){
          var path;
           var drillMap = [];
 idArr=  parent.$("#idArradhoc").val();
               if(typeof parent.$("#adhocdrills").val()!="undefined" && parent.$("#adhocdrills").val()!=""){
                try{
                drillMap=JSON.parse(parent.$("#adhocdrills").val());
            }catch(e){
            }
            }
             drillMap.push(idArr.split(":")[0]);
               parent.$("#adhocdrills").val(JSON.stringify(drillMap));
        if(nextViewById=="Time Drill")
          path=drillURL+drillPoint;
       else if(presentViewById=="TIME"){
           //alert("true")
           if(nextViewById=="Day" || nextViewById=="Week" || nextViewById=="Month" || nextViewById=="Qtr" || nextViewById=="Year"){
//               alert(nextViewById)
               updateTimeDetails(nextViewById);
               path=null;
               var arr=new Array;
               arr=drillURL.split("&");
               for(var i=0;i<arr.length;i++){
                  if(arr[i].indexOf("CBO_PRG_PERIOD_TYPE")!=-1){
                   path+="&CBO_PRG_PERIOD_TYPE="+nextViewById;
                  }else
                    path+="&"+arr[i];
               }
               path+=drillPoint.trim()+"&CBOVIEW_BY" + viewById + "=TIME";
               if(path.indexOf("DrillDate")!=-1)
                path+="&CBO_PRG_PERIOD_TYPE="+nextViewById+"&DDrill=Y";
               path=path.replace("&","").replace("null","");

            //  alert(path)
                //path=ctxPath+'/reportViewer.do?reportBy=viewReport&REPORTID='+reportId+"&CBO_PRG_PERIOD_TYPE="+nextViewById+"&DDrill=Y";
           }
          else
          path=drillURL+drillPoint.trim()+"&DDrillAcross=Y&CBOVIEW_BY" + viewById + "=" + nextViewById ;
       }
        else{
            var drillPointJson=JSON.stringify(new Array(drillPoint));
            //alert("1"+drillPointJson);
          if(nextViewById=="Day" || nextViewById=="Week" || nextViewById=="Month" || nextViewById=="Qtr" || nextViewById=="Year"){
          updateTimeDetails(nextViewById);
          path=ctxpath+'/dashboardViewer.do?reportBy=getOLAPGraphforOneViewd3&REPORTID='+reportId+"&CBO_PRG_PERIOD_TYPE="+nextViewById+"&DDrill=Y&CBOVIEW_BY" + viewById + "=TIME&CBOARP" + presentViewById + "="+drillPointJson;
          }
          else{
          path=ctxpath+'/dashboardViewer.do?reportBy=getOLAPGraphforOneViewd3&REPORTID='+reportId+"&CBOVIEW_BY" + viewById + "=" + nextViewById +"&CBOARP" + presentViewById + "="+drillPointJson+"&adhocdrills="+parent.$("#adhocdrills").val();
        //alert("2"+path)
          }
        // alert(path)
        }
        if(nextViewById!=presentViewById ){
            //alert("3"+presentViewById);
            $("#OLAPGraphRegion").html('<center><img id="imgId" src="images/ajax1.gif" align="middle"  width="75px" height="75px"  style="position:absolute" /></center>');
            $.ajax({
                       url: path+"&graphId="+graphId+"&viewById="+nextViewById+"&width="+width+"&height="+height+"&regid="+regid+"&graphNum="+graphNum+"&drillValue="+drillPoint+"&drillType="+drillType+"&timeDetails="+timeDetails+"&isRepDate="+isRepDate+"&oneviewID="+oneviewID+"&repname="+encodeURIComponent(repname)+"&rolename="+rolename+"&chartname="+chartname+"&adhocdrills="+parent.$("#adhocdrills").val(),
                         success: function(data){
                         $("#GraphRegion").html(data);

                           parent.$("#graphsId").val(reportId);
                     parent.$("#graphName").val(repname);
                     parent.$("#busrolename").val(rolename);
                      parent.$("#chartname").val(chartname);
                       var chartData = JSON.parse(parent.$("#chartData").val());
                       chartData[chartname]["viewBys"][0]=viewbyame
//                       chartData[chartname]["viewIds"][0]=viewById

                                 parent.$("#loading").hide();
                                 var dashletid="OLAPGraphRegion";
                           idArr=  parent.$("#idArradhoc").val();
//                            parent.$("#viewby").val(JSON.stringify(chartData[chartname]["viewBys"]));
//                parent.$("#viewbyIds").val(JSON.stringify(chartData[chartname]["viewIds"]));
//                parent.$("#measure").val(JSON.stringify(chartData[chartname]["meassures"]));
//                parent.$("#aggregation").val(JSON.stringify(chartData[chartname]["aggregation"]));
            parent.$("#chartData").val(JSON.stringify(chartData));
//                           alert(parent.$("#chartData").val())
// generateJsonDataOneview1("OLAPGraphRegion",oneviewID,regid,chartname,"olap",reportId,repname,viewbyame,viewById)
 adhocolapdrill(idArr,"OLAPGraphRegion",viewbyame,nextViewById,regid,oneviewID,repname,reportId,chartname,"olap")
                    }
                });
        }
  }
  function getOLAPGraphTypes(){
  $("#olapGraphTypesDiv").toggle(500);
  }
function changeOLAPGraphType(jqGraphName,jqGraphId){
    $("#OLAPGraphRegion").html('<center><img id="imgId" src="images/ajax1.gif" align="middle"  width="75px" height="75px"  style="position:absolute" /></center>');
     $.ajax({
                    url: "dashboardViewer.do?reportBy=getOLAPGraphforOneView&REPORTID="+reportId+"&graphId="+graphId+"&graphNum="+graphNum+"&width="+width+"&height="+height+"&changedGraphType="+jqGraphName+"&changedGraphId="+jqGraphId+"&isGraphTypeChanged=true&drillType="+drillType,
                    success: function(data){
                        $("#GraphRegion").html(data);
                    }
                });
}



        </script>
    </body>
</html>
