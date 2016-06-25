<%-- 
    Document   : progenJqplotGraphBulider
    Created on : Sep 19, 2012, 4:37:53 PM
    Author     : ramesh janakuttu
--%>
<%@page contentType="text/html" pageEncoding="UTF-8" import="com.progen.users.UserLayerDAO,java.util.ArrayList,com.progen.db.ProgenDataSet,java.util.HashMap,prg.db.Container,prg.db.PbDb,prg.db.PbReturnObject"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">


<%
   response.setHeader("Cache-Control", "no-store");
   response.setHeader("Pragma", "no-cache");
   response.setDateHeader("Expires", 0);
   String graphid= request.getParameter("gid"); 
   String graphtypid = request.getParameter("grptypid");
   String graphChange = request.getParameter("graphChange");
   String graphIds = request.getParameter("grpIds");
   String reportid = request.getParameter("REPORTID");
    String grpidfrmrep = request.getParameter("grpidfrmrep");
   String selectedgraph=request.getParameter("selectedgraph");
   String rowValues=request.getParameter("rowValues");
   String graphcount=request.getParameter("graphCount");
   String tablecols=request.getParameter("tableCols");
   String fromHome=request.getParameter("fromHome");
   String hometabid=request.getParameter("hometabid");
   String graphSlide=request.getParameter("graphSlide");
   String screenheight="";
   if(fromHome!=null && fromHome.equalsIgnoreCase("true")){
        screenheight="210";
   }else{
       screenheight=request.getParameter("screenHeight");
   }

     ArrayList RowValues=new ArrayList();
     if(screenheight==null || screenheight.equalsIgnoreCase("null") || screenheight.isEmpty()){
         screenheight="440";
     }
     int USERID=0;
     if(session.getAttribute("USERID")!=null)
     USERID = Integer.parseInt((String) session.getAttribute("USERID"));
                 UserLayerDAO userdao = new UserLayerDAO();
                 HashMap paramhashmapPA=new HashMap();
                 String userType=userdao.getUserTypeForFeatures(USERID);
                 paramhashmapPA=userdao.getFeatureListAnaLyzer(userType,USERID);
                 String ctxPath1=request.getContextPath();
    

%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
           <link href="<%=ctxPath1%>/javascript/jqplot/jquery.jqplot.min.css" rel="stylesheet" type="text/css" />
            <script type="text/javascript" src="<%=ctxPath1%>/javascript/jqplot/jquery.js"></script>
           <script type="text/javascript" src="<%=ctxPath1%>/javascript/jqplot/jquery.min.js"></script>
           <script type="text/javascript" src="<%=ctxPath1%>/javascript/jqplot/jquery.jqplot.js"></script>
           <script type="text/javascript" src="<%=ctxPath1%>/javascript/jqplot/jquery.jqplot.min.js"></script>
           <script type="text/javascript" src="<%=ctxPath1%>/javascript/jqplot/jqplot.barRenderer.min.js"></script>
           <script type="text/javascript" src="<%=ctxPath1%>/javascript/jqplot/jqplot.categoryAxisRenderer.min.js"></script>
           <script type="text/javascript" src="<%=ctxPath1%>/javascript/jqplot/jqplot.pointLabels.min.js"></script>
           <script type="text/javascript" src="<%=ctxPath1%>/javascript/jqplot/jqplot.pieRenderer.min.js"></script>
           <script type="text/javascript" src="<%=ctxPath1%>/javascript/jqplot/jqplot.donutRenderer.min.js"></script>
           <script type="text/javascript" src="<%=ctxPath1%>/javascript/jqplot/jqplot.canvasAxisLabelRenderer.min.js"></script>
           <script type="text/javascript" src="<%=ctxPath1%>/javascript/jqplot/jqplot.canvasTextRenderer.min.js"></script>
           <script type="text/javascript" src="<%=ctxPath1%>/javascript/jqplot/jqplot.dateAxisRenderer.min.js"></script>
           <script type="text/javascript" src="<%=ctxPath1%>/javascript/jqplot/jqplot.logAxisRenderer.min.js"></script>
           <script type="text/javascript" src="<%=ctxPath1%>/javascript/jqplot/jqplot.canvasAxisTickRenderer.min.js"></script>
           <script type="text/javascript" src="<%=ctxPath1%>/javascript/jqplot/jqplot.highlighter.min.js"></script>
           <script type="text/javascript" src="<%=ctxPath1%>/javascript/jqplot/jqplot.bubbleRenderer.min.js"></script>
           <script type="text/javascript" src="<%=ctxPath1%>/javascript/jqplot/jqplot.funnelRenderer.js"></script>
           <script type="text/javascript" src="<%=ctxPath1%>/javascript/jqplot/jqplot.ClickableBars.js"></script>
         <script type="text/javascript" src="<%=ctxPath1%>/javascript/jqplot/jqplot.EnhancedLegendRenderer.js"></script>
           <script type="text/javascript" src="<%=ctxPath1%>/javascript/jqplot/jqplot.trendline.js"></script>
             <script type="text/javascript" src="<%=ctxPath1%>/javascript/jqplot/jqplot.cursor.min.js"></script>
          <script type="text/javascript" src="<%=ctxPath1%>/TableDisplay/JS/pbGraphDisplayRegionJS.js"></script>
             <script type="text/javascript" src="<%=ctxPath1%>/javascript/jqplot/jqplot.canvasOverlay.min.js"></script>
                  <script  type="text/javascript" src="<%=ctxPath1%>/javascript/jquery.contextMenu.js" ></script>
                    <link href="<%=ctxPath1%>/stylesheets/jquery.contextMenu.css" rel="stylesheet" type="text/css" />
              <script type="text/javascript" src="<%=ctxPath1%>/javascript/jqplot/jqplot.mekkoRenderer.min.js"></script>
                <script type="text/javascript" src="<%=ctxPath1%>/javascript/jqplot/jqplot.blockRenderer.min.js"></script>
                 <script type="text/javascript" src="<%=ctxPath1%>/javascript/jqplot/jqplot.blockRenderer.js"></script>
             <script type="text/javascript" src="<%=ctxPath1%>/javascript/jqplot/jqplot.mekkoAxisRenderer.min.js"></script>
           <!--<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/TableDisplay/css/TableDisplay.css" />-->
           <style type="text/css">

               <%if(graphtypid.equalsIgnoreCase("Funnel(INV)")){%>
                .cla{
                     /*padding-top:28em;*/
                -moz-transform: rotate(180deg)
             }
             .jqplot-title{
                 -webkit-box-sizing: border-box;
                 /*padding-bottom:28em;*/
                     -moz-transform: rotate(-180deg);
                     /*bottom:0;*/
                     top:400px
             }
             td.jqplot-table-legend{-moz-transform: rotate(180deg);}
             .jqplot-cursor-tooltip{-moz-transform: rotate(180deg);}
             .jqplot-highlighter-tooltip{-moz-transform: rotate(-180deg);}
             .jqplot-data-label{-moz-transform: rotate(180deg);}
             <%}%>

    .flagDiv{
                width:150px;
                height:200px;
                background-color:#ffffff;
                overflow:auto;
                position:absolute;
                 font-size: 10px;
                text-align:left;
                border:1px solid #000000;
                border-top-width: 0px;
                z-index:1002;
            }
            .calcTitle {
    background-color: #FFF;
    color: black;
    font-family: verdana, arial, helvetica, sans-serif;
    font-weight: bold;
    font-size: 10px;
    text-align: left;
}
.contextMenu {
	position: absolute;
	width: 200px;
	z-index: 99999;
/*	border: solid 1px #CCC;*/
	/*background: #EEE;*/
	padding: 0px;
	margin: 0px;
	display: none;
}
<% if(fromHome!=null && fromHome.equalsIgnoreCase("true")){%>
.jqplot-data-label{
    position:relative;
    color:#000000;
    font-family:"Verdana",Trebuchet MS,Helvetica,sans-serif;
    font-size:0.67em;
}

<%}%>

           </style>
    
    </head>
    <body>
                <ul id="mymenu" class="contextMenu">
                    <li class="select" style="border: none"><a href="#select"><font size="2px">Save As Image</font></a></li>
        </ul>
            <div id="GraphRegion" width=100% height=<%=screenheight%>px style="overflow-x:hidden;overflow-y:hidden;"></div>
           <script  type="text/javascript">                     
                      graphBuilder();                       
                      
                </script>
         <div id="testForId" class="jqplot-image-container-content"  style="display:none" title="Right Click and Save as Image">
                    
               </div> 
                      <div id="OLAPGraphDialog" style="display:none;" title="OLAP Graph">
<iframe id="OLAPGraphFrame" NAME='OLAPGraphFrame' width="100%" height="100%"  frameborder="0" SRC='about:blank' scrolling="no"></iframe>
</div>

                <input type="hidden" name="ctxPath" id="ctxPath" value="<%=request.getContextPath()%>">
                <script language="javascript" type="text/javascript">
    
function submitform(url,datapoint,isAdhoc){
    var furl=url+"+[\""+datapoint+"\"]";
//    alert('url::'+url+'datapoint::'+datapoint+'isAdhoc'+isAdhoc)

    if(isAdhoc=='true'){
        parent.updateUrlDetails(url,"[\""+datapoint+"\"]");
        parent.$("#DrillDiv").toggle(500);
    }else{
    parent.submiturls1(furl);
}


}
 $(document).ready(function(){
 $("#chart<%=grpidfrmrep%>").contextMenu({menu:"mymenu",rightButton: true},
                        function(action, el, pos) {
                           graphImage(<%=grpidfrmrep%>);
                        });
 });
 function graphBuilder(){
     var graphhtml="";
     var height="";
     height='<%=screenheight%>';
    var fromHomeTest = ""
    fromHomeTest = <%=fromHome%>
    if(fromHomeTest!=null && fromHomeTest=="true"){

     }
//     else{
//     if(height!=null && height=='440' )
//         <% if(userdao.getFeatureEnable("Save as Image") || userType.equalsIgnoreCase("SUPERADMIN")){%>
//         graphhtml+="  <INPUT type='image' src='<%= request.getContextPath()%>/icons pinvoke/chart.png' onclick='graphImage(<%=grpidfrmrep%>)' value='' title='Save_AsImage'>"
//         <%}%>
//     }
    var gid1='<%=graphid%>';
    var grptypid1='<%=graphtypid%>';
    var graphChange1='<%=graphChange%>';
    var grpId1='<%=graphIds%>';
    var reportid1='<%=reportid%>';
    var selectedgraph='<%=selectedgraph%>';
    var rowValues='<%=rowValues%>';
    var tablecols='<%=tablecols%>';
    var hometabid='<%=hometabid%>'
    if(grptypid1=='ColumnPie'){
       parent.$("#columnpie"+<%=graphcount%>).show();
      
   }
   else{
        parent.$("#columnpie"+<%=graphcount%>).hide();
   }
                $.ajax({
                    type: 'GET',
            async: false,
            cache: false,
            timeout: 30000,
                    url: '<%= request.getContextPath()%>/reportViewer.do?reportBy=buildJqPlotGraphs&gid='+gid1+'&grptypid='+grptypid1+'&graphChange=default&grpIds='+grpId1+'&REPORTID='+reportid1+'&grpidfrmrep='+<%=grpidfrmrep%>+'&selectedgraph='+selectedgraph+'&rowValues='+rowValues+'&tablecols='+tablecols+'&screenheight='+height+"&fromHome="+'<%=fromHome%>'+'&graphcount='+<%=graphcount%>+'&hometabid='+hometabid+"&graphSlide="+'<%=graphSlide%>',
                    success: function(data){
                        graphhtml+=data 
                        parent.$("#imgId").hide();
                        $("#GraphRegion").html(graphhtml);
                    }
});
//parent.$("#imgId").hide();
}
                           function olapGraph(dashletName,reportId,graphid,graphnum,timeDetails,isrepDate){

              parent.$("#OLAPGraphDialog").dialog({
                        autoOpen: false,
                        height: 500,
                        width: 900,
                        modal: true,
                        resizable:true,
                        top:-30,
                         position: 'absolute'


             });
                     parent.$("#OLAPGraphDialog").dialog('option', 'title', dashletName);
                     parent.$("#OLAPGraphDialog").dialog("option", "position", 'top');
                     parent.$("#OLAPGraphDialog").dialog('open');

                    var frameObj =parent.document.getElementById("OLAPGraphFrame");
                    var source ="OLAPGraph.jsp?reportId="+reportId+"&graphId="+graphid+"&callFrom=oneView"+"&graphNum="+graphnum+"&width="+(900-50)+"&height="+(500-50)+"&timeDetails="+timeDetails+"&isrepDate="+isrepDate;
                    frameObj.src=source;


            }
  function graphImage(divId){
  var divId="chart"+divId
        $(".overlapDiv").hide();
        var obj = $("#"+divId);
        var newCanvas = document.createElement("canvas");
        var size = findPlotSize(obj);
        newCanvas.width = size.width;
        newCanvas.height = size.height;
        // check for plot error
        var baseOffset = obj.offset();
        if (obj.find("canvas.jqplot-base-canvas").length) {
            baseOffset = obj.find("canvas.jqplot-base-canvas").offset();
            baseOffset.left -= parseInt(obj.css('margin-left').replace('px', ''));
        }
 
        // fix background color for pasting
        var context = newCanvas.getContext("2d");
        var backgroundColor = "rgba(255,255,255,1)";
        obj.children(':first-child').parents().each(function () {
            if ($(this).css('background-color') != 'transparent') {
                backgroundColor = $(this).css('background-color');
                return false;
                    }
});
        context.fillStyle = backgroundColor;
        context.fillRect(0, 0, newCanvas.width, newCanvas.height);
        
        // add main plot area
        obj.find('canvas').each(function () {
            var offset = $(this).offset();
            newCanvas.getContext("2d").drawImage(this,
            offset.left - baseOffset.left,
            offset.top - baseOffset.top
        );
        });
        
        obj.find(".jqplot-series-canvas > div").each(function() {
            var offset = $(this).offset();
            var context = newCanvas.getContext("2d");
            context.fillStyle = $(this).css('background-color');
            context.fillRect(
            offset.left - baseOffset.left - parseInt($(this).css('padding-left').replace('px', '')),
            offset.top - baseOffset.top,
            $(this).width() + parseInt($(this).css('padding-left').replace('px', '')) + parseInt($(this).css('padding-right').replace('px', '')),
            $(this).height() + parseInt($(this).css('padding-top').replace('px', '')) + parseInt($(this).css('padding-bottom').replace('px', ''))
        );
            context.font = [$(this).css('font-style'), $(this).css('font-size'), $(this).css('font-family')].join(' ');
            context.fillStyle = $(this).css('color');
            context.textAlign = getTextAlign($(this));
            var txt = $.trim($(this).html()).replace(/<br style="">/g, ' ');
            var lineheight = getLineheight($(this));
            printAtWordWrap(context, txt, offset.left-baseOffset.left, offset.top - baseOffset.top - parseInt($(this).css('padding-top').replace('px', '')), $(this).width(), lineheight);
        });
        
        // add x-axis labels, y-axis labels, point labels
        obj.find('div.jqplot-axis > div, div.jqplot-point-label, div.jqplot-error-message, .jqplot-data-label, div.jqplot-meterGauge-tick, div.jqplot-meterGauge-label').each(function() {
            var offset = $(this).offset();
            var context = newCanvas.getContext("2d");
            context.font = [$(this).css('font-style'), $(this).css('font-size'), $(this).css('font-family')].join(' ');
            context.fillStyle = $(this).css('color');
            var txt = $.trim($(this).text());
            var lineheight = getLineheight($(this));
            printAtWordWrap(context, txt, offset.left-baseOffset.left, offset.top - baseOffset.top - 2.5, $(this).width(), lineheight);
        });
        
        // add the title
        obj.children("div.jqplot-title").each(function() {
            var offset = $(this).offset();
            var context = newCanvas.getContext("2d");
            context.font = [$(this).css('font-style'), $(this).css('font-size'), $(this).css('font-family')].join(' ');
//            context.textAlign = getTextAlign($(this));
            context.fillStyle = $(this).css('color');
            var txt = $.trim($(this).text());
            var lineheight = getLineheight($(this));
            printAtWordWrap(context, txt, offset.left-baseOffset.left, offset.top - baseOffset.top, newCanvas.width - parseInt(obj.css('margin-left').replace('px', ''))- parseInt(obj.css('margin-right').replace('px', '')), lineheight);
        });
        
        // add the legend
        obj.children("table.jqplot-table-legend").each(function() {
            var offset = $(this).offset();
            var context = newCanvas.getContext("2d");
            context.strokeStyle = $(this).css('border-top-color');
            context.strokeRect(
            offset.left - baseOffset.left,
            offset.top - baseOffset.top,
            $(this).width(),$(this).height()
        );
            context.fillStyle = $(this).css('background-color');
            context.fillRect(
            offset.left - baseOffset.left,
            offset.top - baseOffset.top,
            $(this).width(),$(this).height()
        );
        });
        
        // add the swatches
        obj.find("div.jqplot-table-legend-swatch").each(function() {
            var offset = $(this).offset();
            var context = newCanvas.getContext("2d");
            context.fillStyle = $(this).css('border-top-color');
            context.fillRect(
            offset.left - baseOffset.left,
            offset.top - baseOffset.top,
            $(this).parent().width(),$(this).parent().height()
        );
        });
        
        obj.find("td.jqplot-table-legend").each(function() {
            var offset = $(this).offset();
            var context = newCanvas.getContext("2d");
            context.font = [$(this).css('font-style'), $(this).css('font-size'), $(this).css('font-family')].join(' ');
            context.fillStyle = $(this).css('color');
            context.textAlign = getTextAlign($(this));
            context.textBaseline = $(this).css('vertical-align');
            var txt = $.trim($(this).text());
            var lineheight = getLineheight($(this));
            printAtWordWrap(context, txt, offset.left-baseOffset.left, offset.top - baseOffset.top + parseInt($(this).css('padding-top').replace('px','')), $(this).width(), lineheight);
        });
        
        parent.$("#testForId").html("<img src='"+newCanvas.toDataURL()+"'>");
        parent.$("#testForId").dialog({
            autoOpen: false,
            //                        height: (newCanvas.height+50),
            //                        width: (newCanvas.width+50),
            position: 'justify',
            modal: true
        });
       
        parent.$("#testForId").dialog('option', 'height', (newCanvas.height+60));
         if(newCanvas.width<=1000)
        parent.$("#testForId").dialog('option', 'width', (newCanvas.width+60));
       else
            parent.$("#testForId").dialog('option', 'width', (newCanvas.width));
        parent.$("#testForId").dialog('open');
        
        //                       
}
    function getLineheight(obj) {
        var lineheight;
        if (obj.css('line-height') == 'normal') {
            lineheight = obj.css('font-size');
        } else {
            lineheight = obj.css('line-height');
        }
        return parseInt(lineheight.replace('px',''));
    }
    
    function getTextAlign(obj) {
        var textalign = obj.css('text-align');
        if (textalign == '-webkit-auto') {
            textalign = 'left';
        }
        return textalign;
    }
    
    function printAtWordWrap(context, text, x, y, fitWidth, lineheight) {
        var textArr = [];
        fitWidth = fitWidth || 0;
        
        if (fitWidth <= 0) {
            textArr.push(text);
        }
        
        var words = text.split(' ');
        var idx = 1;
        while (words.length > 0 && idx <= words.length) {
            var str = words.slice(0, idx).join(' ');
            var w = context.measureText(str).width;
            if (w > fitWidth) {
                if (idx == 1) {
                    idx = 2;
                }
                textArr.push(words.slice(0, idx - 1).join(' '));
                words = words.splice(idx - 1);
                idx = 1;
            } else {
                idx++;
            }
        }
        if (words.length && idx > 0) {
            textArr.push(words.join(' '));
        }
        if (context.textAlign == 'center') {
            x += fitWidth/2;
        }
        if (context.textBaseline == 'middle') {
            y -= lineheight/2;
        } else if(context.textBaseline == 'top') {
            y -= lineheight;
        }
        for (idx = textArr.length - 1; idx >= 0; idx--) {
            var line = textArr.pop();
            if (context.measureText(line).width > fitWidth && context.textAlign == 'center') {
                x -= fitWidth/2;
                context.textAlign = 'left';
                context.fillText(line, x, y + (idx+1) * lineheight);
                context.textAlign = 'center';
                x += fitWidth/2;
            } else {
                context.fillText(line, x, y + (idx+1) * lineheight);
            }
        }
    }
    
    function findPlotSize(obj) {
        var width = obj.width();
        var height = obj.height();
        var legend = obj.find('.jqplot-table-legend');
        if (legend.position()) {
            height = legend.position().top + legend.height();
        }
        obj.find('*').each(function() {
            var offset = $(this).offset();
            tempWidth =  $(this).width()
            tempHeight = $(this).height()
            if(tempWidth > width) {width = tempWidth;}
            if(tempHeight > height) {height = tempHeight;}
        });
        return {width: width, height: height};
    }
    function submitformStandard(url,tickVal,dataPoint,j,drillMap,rowviewby,colviewby,tool){
          var grptypid1='<%=graphtypid%>';
        var jsondrillMap=eval('('+drillMap+')');
        var drillRepId='';
        if(parseInt(dataPoint) <=  jsondrillMap.length){
            drillRepId=jsondrillMap[dataPoint];
        }
       if(drillRepId.toString().trim()=='null' ||drillRepId == '0' || drillRepId == '' ){
//            var furl=url+tickVal;
//            parent.submiturls1(furl);
        }else{
            if(rowviewby!=null &&  rowviewby!='null' && colviewby.toString().trim!='null' && drillRepId!= '' ){

                         if(tool.indexOf(":") !=-1|| grptypid1=='ColumnPie' || grptypid1=='Pie'){
                         if(tool.indexOf(":") !=-1){
                       var tool=tool.substring(0, tool.indexOf(":"))
                         }else var tool=tool
                        var furl='<%=request.getContextPath()%>'+'/reportViewer.do?reportBy=viewReport&REPORTID='+drillRepId+'&CBOARP'+rowviewby+'='+"[\""+tickVal+"\"]"+'&CBOARP'+colviewby+'='+"[\""+tool+"\"]"+'&reportDrill=Y';
                         }else{
                          var tool=tool
                       var furl='<%=request.getContextPath()%>'+'/reportViewer.do?reportBy=viewReport&REPORTID='+drillRepId+'&CBOARP'+rowviewby+'='+"[\""+tickVal+"\"]"+'&reportDrill=Y';
                       }
                       }
                       else{
                        var furl='<%=request.getContextPath()%>'+'/reportViewer.do?reportBy=viewReport&REPORTID='+drillRepId
                       }
            parent.submiturls1(furl)
        }
    }
</script>
    </body>
</html>
