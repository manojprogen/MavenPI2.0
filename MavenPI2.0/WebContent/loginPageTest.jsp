<%--
    Document   : loginPageTest
    Created on : 18 Apr, 2013, 7:13:56 PM
    Author     : progen
--%>


<%@page contentType="text/html" pageEncoding="UTF-8" import="java.io.FileInputStream,java.io.ObjectInputStream,com.progen.target.PbAlertQuery,java.math.BigDecimal,com.progen.query.RTMeasureElement,com.progen.report.display.util.NumberFormatter,com.progen.handsontable.bd.HandsonTableBD,prg.db.ContainerConstants"%>

<%@page import="prg.db.PbReturnObject,prg.db.PbDb,com.progen.report.PbReportCollection,java.util.TreeSet,com.progen.report.data.DataFacade,java.io.FileInputStream,com.progen.db.ProgenDataSet"%>

<%@page import="java.util.ArrayList,com.progen.reportview.bd.PbReportViewerBD,java.util.HashMap,prg.db.Container,com.progen.charts.JqplotGraphProperty,com.progen.reportview.db.PbReportViewerDAO"%>
<!DOCTYPE html>

<%
    ArrayList repid = new ArrayList();
    ArrayList repName = new ArrayList();
    String themeColor = "blue";
    if (session.getAttribute("theme") == null) {
        session.setAttribute("theme", themeColor);
    } else {
        themeColor = String.valueOf(session.getAttribute("theme"));
    }
    String advHtmlFileProps = (String) request.getSession(false).getAttribute("advHtmlFileProps");

    ArrayList fileName = new ArrayList();
    ArrayList graphorRep = new ArrayList();
//    ArrayList hotNames = new ArrayList();
    String hotNames = "";
    String ctxPath = request.getContentType();
        repid = (ArrayList) session.getAttribute("repId");
        repName = (ArrayList) session.getAttribute("repNames");
        String contextPath=request.getContextPath();

%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=themeColor%>/jquery-ui-1.7.3.custom.css" rel="stylesheet" />
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=themeColor%>/ReportCss.css" rel="stylesheet"/>
        <link rel="stylesheet" href="stylesheets/treeviewstyle/jquery.treeview.css" />
        <link rel="stylesheet" href="stylesheets/treeviewstyle/screen.css" />
        <link type="text/css" href="<%=contextPath%>/jQuery/jquery/themes/base/BreadCrumb.css" rel="stylesheet" />
        <link type="text/css" href="<%=contextPath%>/jQuery/jquery/themes/base/Base.css" rel="stylesheet" />
        <link type="text/css" href="stylesheets/ui.all.css" rel="stylesheet" />
        <link type="text/css" href="stylesheets/demos.css" rel="stylesheet" />
<!--        <link rel="stylesheet" href="<%=request.getContextPath()%>/stylesheets/style.css" type="text/css" media="print, projection, screen">-->
        <link href="stylesheets/StyleSheet.css" rel="stylesheet" type="text/css" />
        <link href="stylesheets/confirm.css" rel="stylesheet" type="text/css" />
        <link href="stylesheets/jquery.contextMenu.css" rel="stylesheet" type="text/css" />

        <script src="javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <script type="text/javascript" src="javascript/draggable/ui.core.js"></script>
        <script src="javascript/treeview/jquery.cookie.js" type="text/javascript"></script>
        <script src="javascript/treeview/jquery.treeview.js" type="text/javascript"></script>
        <script type="text/javascript" src="javascript/treeview/demo.js"></script>

        <script type="text/javascript" src="javascript/draggable/ui.draggable.js"></script>
        <script type="text/javascript" src="javascript/draggable/ui.droppable.js"></script>
        <script type="text/javascript" src="javascript/ui.resizable.js"></script>
        <script type="text/javascript" src="javascript/ui.accordion.js"></script>
        <script type="text/javascript" src="javascript/ui.dialog.js"></script>
        <script type="text/javascript" src="javascript/ui.sortable.js"></script>
        <script type="text/javascript" src="javascript/effects.core.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/jQuery/jquery/ui/jquery.jBreadCrumb.1.1.js"></script>
        <script type="text/javascript"  language="JavaScript" src="<%=contextPath%>/querydesigner/JS/jquery.columnfilters.js"></script>
        <script src="javascript/jquery.simplemodal-1.1.1.js" type="text/javascript"></script>
        <script src="javascript/jquery.contextMenu.js" type="text/javascript"></script>
        <script type="text/javascript" src="javascript/ui.tabs.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/TableDisplay/JS/pbGraphDisplayRegionJS.js"></script>

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

        .box{
  float:left;
  padding:0px;
  background:#aab;
  width: 150px;
  border-radius:4px;
  margin:8px;
  box-shadow: 0 1px 2px #334;
  -webkit-transition:0.3s;
  transition:0.3s;
}
.box img{
  width:150px;
}

.box:hover{
   box-shadow: 0 2px 8px #334;
   background:#ccd;
   -webkit-transform: scale(1.05);
   transform: scale(1.05);
   opacity:1;
}</style>
        
    </head>
    <body>
        <div style="width:99%;height:670px;display: ''">

            <div style="width:99%;height:100%;border-bottom: medium hidden LightGrey ; border-left: medium hidden LightGrey ; border-right: medium hidden LightGrey ;border-top: medium hidden LightGrey ;" >
                <table  style="table-layout:fixed; width:100%; border-spacing: 15px;">
                    <%int p = 0;
                        if (!repid.isEmpty() && !repName.isEmpty()) {
                    %>
                      <center><img id="imgId" src="<%= request.getContextPath()%>/images/ajax1.gif" align="middle"  width="100px" height="100px"  style="top:200px; position:absolute" /></center>
                    <% for (int i = 0; i < 2; i++) {%>

                    <tr width='100%' >
                        <% for (int j = 0; j < 3; j++) {%>
                        <td id="" width=''  height='290%'  style=''>
                            <div  id='' class='box' style=' border-color: LightGrey ; width: 100%; height: 100%; margin-left: 10px; margin-right: 10px;overflow:auto;background-color: #FCFCFC;-moz-border-radius-bottomright: 50px;
border-bottom-right-radius: 50px;'>
<!--                                <a id="hyperLinkId<%=p%>" href="javascript:void(0)" style="font-size: 13px;display: ''" onclick="gotoDBCON1('<%=request.getContextPath()%>','<%=repid.get(p)%>')"><%=repName.get(p).toString()%></a>-->

                                <table style="vertical-align: top">
<!--                                    <tr><td id="pargraph<%=p%>" style="display: none" onmouseover="showParam('pargraph<%=p%>','imageValueId<%=p%>')"  onmouseout="showParam('pargraph<%=p%>','imageValueId<%=p%>')">
                                            <table width="430px" height="220px" border="1" >
                                                <tr>
                                                    <td align="center">10</td><td align="center">20</td><td align="center">30</td>
                                                </tr>
                                                <tr>
                                                    <td align="center">40</td><td align="center">50</td><td align="center">60</td>
                                                </tr>
                                                <tr>
                                                    <td align="center">70</td><td align="center">80</td><td align="center">90</td>
                                                </tr>
                                            </table></td></tr>-->
                                    <%

                                               repid = (ArrayList) session.getAttribute("repId");
                                                repName = (ArrayList) session.getAttribute("repNames");
                                                PbReportViewerDAO repDao = new PbReportViewerDAO();
                                                  PbReturnObject pbReturnObject = null;
    pbReturnObject = repDao.getAllHomeGraphs(repid.get(p).toString());
    if (pbReturnObject != null && pbReturnObject.rowCount > 0) {
        for (int k = 0; k < pbReturnObject.getRowCount(); k++) {
            fileName.add(pbReturnObject.getFieldValueString(k, "FILE_NAME"));
            repid.add(pbReturnObject.getFieldValueString(k, "REPORT_ID"));
            repName.add(pbReturnObject.getFieldValueString(k, "REP_NAME"));
            graphorRep.add(pbReturnObject.getFieldValueString(k, "GRAP_OR_TABLE"));
            if(pbReturnObject.getFieldValueString(k, "GRAPH_HOT_TABLE").toString()!=null && !pbReturnObject.getFieldValueString(k, "GRAPH_HOT_TABLE").equalsIgnoreCase(""))
            hotNames =(pbReturnObject.getFieldValueString(k, "GRAPH_HOT_TABLE"));
        }

    }
                                        if (fileName.isEmpty()) {
                                             String selectedgraphtype1 = null;
                                            String graphTypename = null;
                                            String graphTypeid = null;
                                            String graphid = null;

                                            StringBuilder headerNames = new StringBuilder();
                                            StringBuilder graphData = new StringBuilder();
                                            String reportId ="";
                                            String repNameVal ="";
                                            String finalGrapData = "";
                                            String headerDta = "";


                                             ArrayList graphDetails = new ArrayList();
                                              HandsonTableBD hotBd = new HandsonTableBD();
                                              HashMap<String ,ArrayList> hashMap = new HashMap<String ,ArrayList>();
                                             hashMap =hotBd.getAllFilesdata(repid.get(p).toString(),session,request,response);
                                             if(!hashMap.isEmpty()){
                                                graphDetails = hashMap.get("graphDetails");
                                             if (!graphDetails.isEmpty()) {
                                                selectedgraphtype1 = "jq";
                                                graphTypename = graphDetails.get(0).toString();
                                                graphTypeid = graphDetails.get(1).toString();
                                                graphid = graphDetails.get(2).toString();
                                                for(int l=0;l<hashMap.get("graphData").size();l++){
                                                    headerNames.append(",").append(hashMap.get("graphData").get(l).toString());
                                                }
                                                 for(int e=0;e<hashMap.get("graphHeaders").size();e++){
                                                    graphData.append(",").append(hashMap.get("graphHeaders").get(e).toString());
                                                }
                                               
                                                finalGrapData = graphData.toString().substring(1);
                                                headerDta = headerNames.toString().substring(1);
                                                reportId = hashMap.get("repNameandId").get(0).toString();
                                                repNameVal = hashMap.get("repNameandId").get(1).toString();
                                            }
                                                                                               }
                                    %>
                                    <%if (!graphDetails.isEmpty()) {%>
                                    <tr><td id="imageValueId<%=p%>" width='500px'  style="" align="center" onmouseover="showParam('pargraph<%=p%>','imageValueId<%=p%>')"  onmouseout="showParam('pargraph<%=p%>','imageValueId<%=p%>')"><iframe class=frame1 frameBorder='0' STYLE='width:100%;left:100; height:250px' src="<%=request.getContextPath()%>/TableDisplay/progenJqplotGraphBulider.jsp?REPORTID=<%=repid.get(p)%>&grptypid=<%=graphTypename%>&selectedgraph=<%=selectedgraphtype1%>&gid=<%=graphTypeid%>&grpidfrmrep=<%=graphid%>&graphCount=<%=1%>&graphChange=default&fromHome=true&hometabid=imageValueId<%=p%>"></iframe></td></tr>
                                    <!--<tr><td id="pargraph<%=p%>" width='500px' style="display: none" onmouseover="showParam('pargraph<%=p%>','imageValueId<%=p%>')"  onmouseout="showParam('pargraph<%=p%>','imageValueId<%=p%>')"><iframe class=frame1 frameBorder='0' STYLE='width:100%;left:100; overflow:auto;height:290px' src="<%=request.getContextPath()%>/handsontable.jsp?finalGrapData=<%=finalGrapData%>&headerDta=<%=headerDta%>&reportId=<%=reportId%>&repNameVal=<%=repNameVal%>"></iframe></td></tr>-->
                                    <%} else {%>
                                    <tr><td id="imageValueId<%=p%>" width='500px' style="" align="center"><iframe class=frame1 frameBorder='0' STYLE='width:100%;left:100; overflow:auto;height:250px' src="<%=request.getContextPath()%>/handsontable.jsp?reportId=<%=repid.get(p)%>&fromHomePage=fromHome"></iframe></td></tr>
                                            <%}%>
                                            <%} else if (graphorRep.get(p).toString().equalsIgnoreCase("graphType")) {%>
                                    <tr><td id="imageValueId<%=p%>" width='500px'   style="" align="center" onmouseover="showParam('pargraph<%=p%>','imageValueId<%=p%>')"  onmouseout="showParam('pargraph<%=p%>','imageValueId<%=p%>')"><iframe class=frame1 frameBorder='0' STYLE='width:100%;left:100;height:250px' src="<%=request.getContextPath()%>/homepageGraphFile.jsp?fileName=<%=fileName.get(p)%>"></iframe></td></tr>
                                    <!--<tr><td id="pargraph<%=p%>" width='500px' style="display: none" onmouseover="showParam('pargraph<%=p%>','imageValueId<%=p%>')"  onmouseout="showParam('pargraph<%=p%>','imageValueId<%=p%>')"><iframe class=frame1 frameBorder='0' STYLE='width:100%;left:100; overflow:auto;height:290px' src="<%=request.getContextPath()%>/handsontable.jsp?hotFileName=<%=repid.get(p)%>"></iframe></td></tr>-->
                                            <%} else {%>
                                    <tr><td id="imageValueId<%=p%>" width='500px' style="" align="center"><iframe class=frame1 frameBorder='0' STYLE='width:100%;left:100; overflow:auto;height:250px' src="<%=request.getContextPath()%>/handsontable.jsp?reportId=<%=repid.get(p)%>&fromHomePage=fromHome&fileName=<%=fileName.get(p)%>"></iframe></td></tr>
                                            <%}%>
                                </table>
                            </div>
                        </td>
                        <% p++;
                           }%>
                    </tr>
                    <% }
                        } else {%>
                    <tr><td id="imageValueId<%=p%>" width='500px'  style="" align="center" >No Favorite Reports and Dashboards</td></tr>
                    <%}%>
                </table>
            </div>
        </div>
                <input type="hidden" name="REPORTID" id="REPORTID" value="<%=repid%>">
                <input type="hidden" name="ctxPath" id="ctxPath" value="<%=request.getContextPath()%>">
                <div id="OLAPGraphDialog" style="display:none;" title="OLAP Graph">
<iframe id="OLAPGraphFrame" NAME='OLAPGraphFrame' width="100%" height="100%"  frameborder="0" SRC='about:blank' scrolling="no"></iframe>
</div>
                <script type="text/javascript">

$(function(){

   $('.box').on('mouseenter mouseleave', function( e ){
       $(this).siblings().stop().fadeTo(300, e.type=='mouseenter' ? 0.6 : 1 );
   });

});

            function showParam(id,imgId){
                if(document.getElementById(id)!=null && document.getElementById(id).style.display=='none'){
                    document.getElementById(id).style.display=''
                    document.getElementById(imgId).style.display='none'
                }else if(document.getElementById(id)!=null){
                    document.getElementById(id).style.display='none'
                    document.getElementById(imgId).style.display=''
                }
            }
            function gotoDBCON1(ctxPath,repid){
                parent.document.forms.hometab.action="<%=request.getContextPath()%>/reportViewer.do?reportBy=viewReport&REPORTID="+repid+"&action=open";
                parent.document.forms.hometab.submit();

            }

function resizeIframe(obj)
	 {

	   obj.style.height = obj.contentWindow.document.body.scrollHeight + 'px';
	  // obj.style.width = obj.contentWindow.document.body.scrollWidth + 'px';
	 }
        </script>
    </body>
</html>