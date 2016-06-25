<%--
    Document   : homepageGraphFile
    Created on : 22 Apr, 2013, 12:37:31 PM
    Author     : progen
--%>

<%@page import="java.io.ObjectInputStream"%>
<%@page import="java.io.FileInputStream"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%
  String fileName = request.getParameter("fileName");
   String advHtmlFileProps=(String) request.getSession(false).getAttribute("reportAdvHtmlFileProps");

   FileInputStream fis2 = new FileInputStream(advHtmlFileProps+"/"+fileName);
                                   String finalVal = "";
                                    ObjectInputStream ois2 = new ObjectInputStream(fis2);
                                     finalVal = (String) ois2.readObject();
                                      ois2.close();
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
           <link href="<%=request.getContextPath()%>/javascript/jqplot/jquery.jqplot.min.css" rel="stylesheet" type="text/css" />
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
                  <script  type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.contextMenu.js" ></script>
                    <link href="<%=request.getContextPath()%>/stylesheets/jquery.contextMenu.css" rel="stylesheet" type="text/css" />
           <!--<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/TableDisplay/css/TableDisplay.css" />-->
           <style type="text/css">

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
.jqplot-data-label{
    position:relative;
    color:#000000;
    font-family:"Verdana",Trebuchet MS,Helvetica,sans-serif;
    font-size:0.67em;
}

           </style>
           <script type="text/javascript">
 $(document).ready(function(){
   parent.$("#imgId").hide();
 });


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
           </script>
    </head>
    <body>
        <div id="homepageGraphId" width=100% height="200px" style="overflow-x:hidden;overflow-y:hidden;" ><%=finalVal%></div>
       <div id="OLAPGraphDialog" style="display:none;"  title="OLAP Graph">
<!--   <iframe id="OLAPGraphFrame" NAME='OLAPGraphFrame' width="250%" frameborder="0" SRC='about:blank' scrolling="no" onload='javascript:resizeIframe(this);'></iframe>-->
<iframe id="OLAPGraphFrame" NAME='OLAPGraphFrame' width="100%" height="100%"  frameborder="0" SRC='about:blank' scrolling="no" onload='javascript:resizeIframe(this.id);'></iframe>
</div>
    </body>
</html>