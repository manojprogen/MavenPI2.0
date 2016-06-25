<%--
    Document   : PbDbrdJQplot.jsp
    Created on : 13 Aug, 2012, 11:00:27 AM
    Author     : progen
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<%
    String graphType = request.getParameter("graphType");
    String graphId = request.getParameter("graphId");
    String dashboardId = request.getParameter("dashboardId");
    String dashletId = request.getParameter("dashletId");
    String themeColor = "blue";
    String height = request.getParameter("height");
    int pageFont = 11;
    int anchorFont = 12;

    String refReportId = request.getParameter("refReportId");
    String kpiMasterId = request.getParameter("kpiMasterId");
    String dispSequence = request.getParameter("dispSequence");
    String dispType = request.getParameter("dispType");
    String dashletName = request.getParameter("dashletName");
   String editDbrd = request.getParameter("editDbrd");
   String fromDesigner= request.getParameter("fromDesigner");
   String flag= request.getParameter("flag");
   String Type= request.getParameter("Type");
String contextPath=request.getContextPath();

%>

<html>
    <head>
         <title><bean:message key="ProGen.Title"/></title>
         <script type="text/javascript" src="<%=contextPath%>/javascript/dashboardDesign.js"></script>
        <script src="<%=contextPath%>/javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/lib/jquery/autocomplete/jquery.autocomplete-min.js"></script>

        <script src="<%=contextPath%>/javascript/lib/jquery/js/jquery.tablesorter.mod.js" type="text/javascript"></script>
        <script src="<%=contextPath%>/javascript/lib/jquery/js/jquery.tablesorter.collapsible.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/lib/jquery/js/jquery.tablesorter.innergrid.js"></script>
        <script src="<%=contextPath%>/javascript/treeview/jquery.treeview.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/quicksearch.js"></script>
        <script  type="text/javascript" src="<%=contextPath%>/javascript/jquery.contextMenu.js" ></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/dashboardDesignerViewer.js"></script>
        <link type="text/css" href="<%=contextPath%>/jQuery/jquery/themes/base/ui.all.css" rel="stylesheet" />
        <link type="text/css" href="<%=contextPath%>/jQuery/jquery/themes/base/ui.theme.css" rel="stylesheet" />
        <link type="text/css" href="<%=contextPath%>/jQuery/jquery/demos.css" rel="stylesheet" />
<!--        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/pbReportViewerCSS.css" rel="stylesheet" />-->
        <link type="text/css" href="<%=contextPath%>/css/css.css" rel="stylesheet" />
        <link type="text/css" href="<%=contextPath%>/css/style.css" rel="stylesheet" />
        <link type="text/css" href="<%=contextPath%>/stylesheets/newDashboard.css" rel="stylesheet" />
        <link rel="stylesheet" type="text/css" href="<%=contextPath%>/stylesheets/tablesorterStyle.css" />
        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/treeviewstyle/screen.css" />
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=themeColor%>/jquery-ui-1.7.3.custom.css" rel="stylesheet" />
        <link type="text/css" href="<%=contextPath%>/javascript/lib/jquery/autocomplete/styles.css" rel="stylesheet" />
        <link rel="stylesheet" href="<%=contextPath%>/tablesorter/themes/<%=themeColor%>/style.css" type="text/css" media="print, projection, screen" />
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=themeColor%>/pbReportViewerCSS.css" rel="stylesheet" />
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=themeColor%>/TableCss.css" rel="stylesheet" />
<!--        <link href="<%=request.getContextPath()%>/stylesheets/themes/<%=themeColor%>/css.css" rel="stylesheet" type="text/css">-->
        <link href="<%=contextPath%>/stylesheets/themes/<%=themeColor%>/style.css" rel="stylesheet" type="text/css">
        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/treeviewstyle/jquery.treeview.css" />
        <link href="<%=contextPath%>/stylesheets/jquery.contextMenu.css" rel="stylesheet" type="text/css" />
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=themeColor%>/metadataButton.css" rel="stylesheet" />
<!--        <script type="text/javascript" src="http://maps.google.com/maps/api/js?sensor=false"></script>-->
<!--        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/map.js"></script>-->
<!--        <link href="http://code.google.com/apis/maps/documentation/javascript/examples/standard.css" rel="stylesheet" type="text/css" />-->


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
           <link href="<%=contextPath%>/javascript/jqplot/jquery.jqplot.min.css" rel="stylesheet" type="text/css" />

<style type="text/css" >
         #fixedtop1 { position: fixed; top: 0px; left: 200px; right: 0; width: 50px; border: none; z-index: 50; }
#center250a { width: 900px;height: 65px; margin: auto; background:none; }
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
            }
            .black_overlay{
                display: none;
                position: absolute;
                top: 0%;
                left: 0%;
                width: 110%;
                height: 200%;
                background-color: black;
                z-index:1001;
                -moz-opacity: 0.5;
                opacity:.50;
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
                -moz-border-radius-bottomleft:6px;
                -moz-border-radius-bottomright:6px;
                -moz-border-radius-topleft:6px;
                -moz-border-radius-topright:6px;
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
                width:180px;
                overflow:auto;
                overflow-x:hidden;
                margin:0em 0.5em;
            }
            a {font-family:Verdana;cursor:pointer;font-size:<%=anchorFont%>px;}
            *{font:<%=pageFont%>px verdana}

            /* expand/collapse */
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

        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

       
    </head>
    <body  id="graphregion">
<!--        <form action="" name="frmParameter"  method=""></form>-->

<div id="OLAPGraphDialog" style="display:none" title="OLAP Graph">
    <iframe id="OLAPGraphFrame" NAME='OLAPGraphFrame' width="100%" height="100%" frameborder="0" SRC='about:blank'></iframe>
</div>
 <script type="text/javascript">
            $(document).ready(function(){
                var graphType = "<%=graphType%>";
                var graphId = "<%=graphId%>";
                var dashletId = "<%=dashletId%>";
                var dashboardId = "<%=dashboardId%>";
                var height = "<%=height%>";
                var Type="<%=Type%>";
                var refReportId="<%=refReportId%>";
                var kpiMasterId="<%=kpiMasterId%>";
                var dispSequence="<%=dispSequence%>";
                var dispType="<%=dispType%>";
                var dashletName="<%=dashletName%>";
                var editDbrd="<%=editDbrd%>";
                var fromDesigner="<%=fromDesigner%>";
                var flag="<%=flag%>";
                var dashletObj=document.getElementById("graphregion");
                //dashletObj.innerHTML='<div id="chart-'+dashletId+'" style="width:100%;height:'+height+'px;" align="left" position="top:10px"></div>';
                if(Type=="jqGraphProperty"){
                 $.ajax({
                    url: 'dashboardViewer.do?reportBy=displayGraph&dashletId='+dashletId+'&dashBoardId='+dashboardId+'&refReportId='+refReportId+'&graphId='+graphId+'&kpiMasterId='+kpiMasterId+'&dispSequence='+dispSequence+'&dispType='+dispType+'&dashletName='+dashletName+'&editDbrd='+editDbrd+'&fromDesigner='+fromDesigner+'&flag='+flag,
                    success: function(data){
                       // alert(data)
                      $("#graphregion").html(data);
                         }
                             });
               }else{
                 $.ajax({
        url: 'dashboardViewer.do?reportBy=buildDBJqplotGraph&graphType='+graphType+'&graphId='+graphId+'&dashboardId='+dashboardId+'&dashletId='+dashletId,
        success: function(data){
            $("#graphregion").html(data);
//            alert(data)
            // $("#chart-"+dashletId).html(data);
        }
            });
                }

            });

            function initialGraphdisplay(){


           }
           function OLAPGraph(dbrdId,dashletId,dashletname){
               parent.OLAPGraph(dbrdId,dashletId,dashletname);
           }
        </script>
    </body>
</html>
