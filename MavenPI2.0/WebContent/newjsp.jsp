
<html>
    <head>
          <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jqplot/jquery.js"></script>
           <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jqplot/jquery.min.js"></script>
           <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jqplot/jquery.jqplot.js"></script>
           <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jqplot/jquery.jqplot.min.js"></script>
           <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jqplot/jqplot.barRenderer.min.js"></script>
           <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jqplot/jqplot.categoryAxisRenderer.min.js"></script>
           <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jqplot/jqplot.pointLabels.min.js"></script>
           <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jqplot/jqplot.pieRenderer.min.js"></script>
           <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jqplot/jqplot.donutRenderer.min.js"></script>
<!--           <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jqplot/jqplot.canvasAxisLabelRenderer.min.js"></script>-->
<!--           <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jqplot/jqplot.canvasTextRenderer.min.js"></script>-->
           <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jqplot/jqplot.dateAxisRenderer.min.js"></script>
           <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jqplot/jqplot.logAxisRenderer.min.js"></script>
<!--           <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jqplot/jqplot.canvasAxisTickRenderer.min.js"></script>-->
           <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jqplot/jqplot.highlighter.min.js"></script>
           <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jqplot/jqplot.bubbleRenderer.min.js"></script>
           <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jqplot/jqplot.funnelRenderer.js"></script>
           <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jqplot/jqplot.ClickableBars.js"></script>
         <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jqplot/jqplot.EnhancedLegendRenderer.js"></script>
           <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jqplot/jqplot.trendline.js"></script>
             <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jqplot/jqplot.cursor.min.js"></script>
          <script type="text/javascript" src="<%=request.getContextPath()%>/TableDisplay/JS/pbGraphDisplayRegionJS.js"></script>
             <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jqplot/jqplot.canvasOverlay.min.js"></script>
                  <script  type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.contextMenu.js" ></script>
                    <link href="<%=request.getContextPath()%>/stylesheets/jquery.contextMenu.css" rel="stylesheet" type="text/css" />
              <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jqplot/jqplot.mekkoRenderer.min.js"></script>
                <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jqplot/jqplot.blockRenderer.min.js"></script>
                 <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jqplot/jqplot.blockRenderer.js"></script>
             <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jqplot/jqplot.mekkoAxisRenderer.min.js"></script>
         
        <script type="text/javascript">
            $(document).ready(function(){
         (function($) {$.jqplot.euroFormatter = function (format, val) {if (!format) {format = '%.1f'; }
                 return numberWithCommas($.jqplot.sprintf(format, val));};
             function numberWithCommas(x) {  if(x==0 || x==0.0 || x==0.00) return '';
                 else return x.toString().replace(/\B(?=(?:\d{3})+(?!\d))/g, ","); }})(jQuery);
         var ticks=["C-BM", "A-BM", "B-BM", "D-BM"];var tooltip=null;var plot2 = $.jqplot('chart104092',[[17, 15, 15, 11]],{animate: true,seriesColors:['#357EC7', '#667c26', '#C24641', '#A0C544', '#53B3FF', '#737CA1', '#7E354D', '#E66C2C', '#A74AC7', '#307D7E'],
             title:{text: 'Invoice Report',fontSize: 12},series:[{disableStack : true,renderer:$.jqplot.BarRenderer},
                 {disableStack : true,renderer:$.jqplot.LineRenderer,lineWidth:2,yaxis:'y2axis'}, {label:'',}],
             legend: { show: true, placement: 'outsideGrid',location: 's',labels:["Total Inv Numbers"],
                 showLabels:true,renderer:$.jqplot.EnhancedLegendRenderer, rendererOptions: {numberColumns: 8}}, seriesDefaults:{rendererOptions: {highlightMouseDown: true},pointLabels: { show: false,formatString:'%.0f',location: 's',edgeTolerance: -15,formatter: $.jqplot.euroFormatter }},
             axes: {xaxis: { renderer: $.jqplot.CategoryAxisRenderer,label:" ",labelRenderer: $.jqplot.CanvasAxisLabelRenderer,tickOptions:{showGridline:true,fontSize: '7.35pt',fontFamily:'Verdana',textColor: "black",angle:-30, formatter: function(format, value) {var tick=ticks[value-1];if(tick===undefined){ return ""; }
                             else{if(tick.length>15) return tick.substring(0,15)+"...";  else return tick;}}},
                     tickRenderer:$.jqplot.CanvasAxisTickRenderer },
                 yaxis: { label:" ",labelRenderer: $.jqplot.CanvasAxisLabelRenderer, pad:0,min:0,tickOptions: { showGridline:true,formatString:'%.0f',fontSize:'7pt' , textColor:'black', fontFamily:'Verdana',formatter: $.jqplot.euroFormatter} },
                 y2axis: { label:" ",labelRenderer: $.jqplot.CanvasAxisLabelRenderer, pad:0,min:0,tickOptions: { showGridline:true,formatString:'%.0f',fontSize:'7pt' , textColor:'black', fontFamily:'Verdana',formatter: $.jqplot.euroFormatter} },},
             highlighter:{ show:true,sizeAdjust:-8, tooltipLocation: 'n',tooltipAxes: 'yref',useAxesFormatters:true,tooltipContentEditor:tooltipContentEditor },
             grid:{gridLineColor:'#F2F2F2',background:'transparent',borderWidth:0,borderColor:'black',shadow:false,shadowAmgle:0,shadowWidth:0,shadowOffset:0,shadowDepth:0}});
         function tooltipContentEditor(str, seriesIndex, pointIndex, plot)
         {var pointlables=ticks[pointIndex];var roundvalue=Math.round(plot.data[seriesIndex][pointIndex]*Math.pow(10,0))/Math.pow(10,0); return pointlables.toString()+":" +roundvalue.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",")+""}
         $("#chart104092").bind("jqplotDataClick", function(ev, i, j, data)
         {var reportid=null; var value=ticks[j];
             var viewbyid=291448;submitform('/piEE/reportViewer.do?reportBy=viewReport&REPORTID=25841&CBOVIEW_BY24310=291448&CBOARP291470=',tickschart104092[j],'false');})

        </script>
    </head>
    <body>
        <div id="chart80683"></div>
    </body>
</html>