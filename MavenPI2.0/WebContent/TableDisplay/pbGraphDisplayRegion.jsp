<%--
    Document   : pbGraphDisplayRegion
    Created on : Jul 16, 2009, 4:28:31 PM
    Author     : santhosh.kumar@progenbusiness.com
--%>
<%@page import="com.progen.users.UserLayerDAO"%>
<%@page import="com.progen.report.data.DataFacade"%>
<%@page import="com.progen.report.PbReportCollection"%>
<%@page import="com.progen.reportview.db.PbReportViewerDAO"%>
<%@page import="com.progen.bd.ProgenJqplotGraphBD"%>
<%@page import="com.progen.jqplot.ProGenJqPlotChartTypes"%>
<%@page import="com.progen.db.ProgenDataSet"%>
<%@page import="com.progen.users.PrivilegeManager"%>
<%@page pageEncoding="UTF-8"%>
<%@page contentType="text/html"%>
<%@page import="java.awt.Font" %>
<%@page import="utils.db.*" %>
<%@page language="java" import="java.sql.*"%>
<%@page import="java.awt.*" %>
<%@page import="java.io.*" %>
<%@page import="com.progen.report.charts.*"%>
<%@page import="java.util.HashMap" %>
<%@page import="java.text.NumberFormat"%>
<%@page import="java.util.Locale"%>
<%@page import="prg.db.*"%>
<%@page import="java.util.*" %>
<%@page import="prg.db.Container" %>
<%@page import="prg.db.PbReturnObject" %>
<%@page import="com.progen.graphs.client.*" %>
<%@page import="com.progen.charts.*"%>
<%@page import="javax.servlet.ServletContext" %>
<%@page import="com.progen.charts.ProGenChartUtilities"%>
<%@page import="com.progen.query.RTMeasureElement"%>
<%@page import="com.progen.reportdesigner.db.ReportTemplateDAO"%>

<%
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
            PbFxXML FxXML = new PbFxXML();
            ReportTemplateDAO DAO = new ReportTemplateDAO();
            Container container = null;
            HashMap GraphTypesHashMap = null;
            HashMap GraphSizesDtlsHashMap = null;
            String[] graphTypesArray = null;
            String[] grpTypeskeys = new String[0];
            String[] jqgrapharray={"Area","Area-Line","Bar-Horizontal","Bar-Vertical","Block","Bubble","Bubble(log)","ColumnPie","Dot-Graph","DualAxis(Bar-Line)","DualAxis(Area-Line)","Donut-Single","Donut-Double","Funnel","Funnel(INV)","Line","Line(Dashed)","Line(Simple)","Line(Simple-R)","Line(Smooth)","Line(Std)","Mekko","Overlaid(Bar-Line)","Overlaid(Bar-Dot)","Pie","Pie-Empty","Scatter","Scatter(Partial)","Scatter(Regression)","StackedArea","StackedBar(V)","StackedBar(H)","StackedBar(Percent)","StackedH(Percent)","Waterfall","Waterfall(GT)"};
            String[] jqgraphIds={"5500","5501","5502","5503","5504","5505","5506","5507","5508","5509","5510","5511","5512","5513","5514","5515","5516","5517","5518","5519","5520","5521","5522","5523","5524","5525","5526","5527","5528","5529","5530","5531","5532","5533","5534","5535"};
            ArrayList jqGraphId=new ArrayList();
            String grphIds = "";
            String[] graphId;
            ServletContext context = getServletContext();
            boolean isFxCharts = Boolean.parseBoolean(context.getInitParameter("isFxCharts"));
            String reportId = null;
            String PbUserId = null;
            String ProGenImgPath = getServletContext().getRealPath("/") + "tempFolder/";
            String themeColor="blue";
               String graph=null;
                String grptype=null;
                String grpid=null;
            if (request.getParameter("tabId") != null) {
                reportId = request.getParameter("tabId");

            } else if (request.getAttribute("tabId") != null) {
                reportId = String.valueOf(request.getAttribute("tabId"));
            }
             if(session.getAttribute("theme")==null)
                session.setAttribute("theme",themeColor);
            else
                themeColor=String.valueOf(session.getAttribute("theme"));

                int USERID=Integer.parseInt(String.valueOf(session.getAttribute("USERID")));
                UserLayerDAO userdao = new UserLayerDAO();
                String userType=userdao.getUserTypeForFeatures(USERID);
                 HashMap paramhashmapPA=new HashMap();
                 paramhashmapPA=userdao.getFeatureListAnaLyzer(userType,USERID);
%>
<html>
    <head>
        <title>piEE</title>

        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/lib/jquery/js/jquery-1.4.2.min.js"></script>
<!--        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/lib/jquery/js/jquery-1.3.2.min.js"></script>-->
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>

<!--        <script type="text/javascript" src="<%=request.getContextPath()%>/jQuery/jquery/jquery-1.3.2.js"></script>-->
<!--        <script type="text/javascript" src="<%=request.getContextPath()%>/jQuery/jquery/ui/ui.core.js"></script>-->
<!--        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/ui.dialog.js"></script>-->



        <script type="text/javascript" src="<%=request.getContextPath()%>/TableDisplay/JS/pbGraphDisplayRegionJS.js"></script>
<!--        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/draggable/ui.draggable.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/draggable/ui.droppable.js"></script>-->

        <script type="text/javascript" src="<%=request.getContextPath()%>/TableDisplay/overlib.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/toolTip.js"></script>
        <link rel="stylesheet" href="<%=request.getContextPath()%>/stylesheets/toolTip.css" type="text/css">
        <style type="text/css">
            .column { width:auto; float: left; padding-bottom: 5px;height:auto }
            .portlet { margin: 0 1em 1em 0;width:auto;height:auto }

            .portlet-header .ui-icon { float: right;width:auto;height:auto;background-color:#B4D9EE}
            .portlet-content { padding: 0.4em;width:auto;height:auto }
            body {
                scrollbar-face-color: #DEE3E7;
                scrollbar-highlight-color: #FFFFFF;
                scrollbar-3dlight-color: #D1D7DC;
                scrollbar-darkshadow-color: #98AAB1;
                scrollbar-shadow-color: #DEE3E7;
                scrollbar-arrow-color: #006699;
                scrollbar-track-color: #EFEFEF;
                overflow:hidden;
            }
            *{font:11px verdana}
            .appletDiv{
                z-index:1000;
                -moz-border-radius-bottomleft:4px;
                -moz-border-radius-bottomright:4px;
                -moz-border-radius-topleft:4px;
                -moz-border-radius-topright:4px;
                border:1px solid #448DAE;
                height:300px
            }
            .flagDiv{
                width:150px;
                height:200px;
                background-color:#ffffff;
                overflow:auto;
                position:absolute;
                text-align:left;
                border:1px solid #000000;
                border-top-width: 0px;
                z-index:1002;
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
}

.graphTypesStyle li:hover{
    font-size: 12px;
  background: #f6f6f6;
  color:black;
}
            td{font-family:verdana}
        </style>
        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/themes/<%=themeColor %>/jquery-ui-1.7.3.custom.css" rel="stylesheet" />

         <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/TableDisplay/css/TableDisplay.css" />
        <link href="<%=request.getContextPath()%>/css/styles.css" rel="stylesheet" type="text/css">
        <link href="<%=request.getContextPath()%>/stylesheets/themes/<%=themeColor %>/css.css" rel="stylesheet" type="text/css">
<!--        <link type="text/css" href="<%=request.getContextPath()%>/jQuery/jquery/themes/base/ui.theme.css" rel="stylesheet" />-->
<!--        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/demos.css" rel="stylesheet" />
        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/ui.all.css" rel="stylesheet" />
        <link rel="stylesheet" href="<%=request.getContextPath()%>/stylesheets/jq.css" type="text/css" media="print, projection, screen">
-->        <link rel="stylesheet" href="<%=request.getContextPath()%>/stylesheets/themes/<%=themeColor %>/style.css" type="text/css" media="print, projection, screen">
        <link href="<%=request.getContextPath()%>/reportDesigner.css" type="text/css" rel="stylesheet">
        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/themes/<%=themeColor%>/metadataButton.css" rel="stylesheet" />

        <%if (isFxCharts) {%>
        <script type="text/javascript" >
            var ctxPath="<%=request.getContextPath()%>"
        </script>
        <script type="text/javascript"  src="<%=request.getContextPath()%>/javascript/dtfx.js"></script><!-- including Java FX charts related js file-->
        <script type="text/javascript" >
            function loadJavaFXScript(appletName,graphType,xmlStr,visibleType,hgt,wdt){
                var myApp;
                if(visibleType=='Normal'){
                    javafx({
                        archive: "<%=request.getContextPath()%>/FxFiles/JavaFXApplication3.jar",
                        draggable: true,
                        height:hgt,
                        width:wdt,
                        code: "misc.MyChart",
                        name: appletName,
                        id: appletName
                    });
                    myApp = document.getElementById(appletName);
                    myApp.script.xmlStr = xmlStr;
                }else{
                    javafx({
                        archive: "<%=request.getContextPath()%>/FxFiles/JavaFXApplication3.jar",
                        draggable: true,
                        height:hgt,
                        width:wdt,
                        code: "misc.MyChart",
                        name: appletName,
                        id: appletName
                    });
                    myApp = document.getElementById(appletName);
                    myApp.script.xmlStr = xmlStr;
                }
            <%=FxXML.getFxChartsFunNames()%>
                }
                function submitGrpForm(path){
                    parent.submiturls1(path);
                }
              
        </script>
        <%}%>


        <script type="text/javascript">

            var drillUrl="";
            var drillpoint="";
           

            //added by k

            function previousRows(graphId,reportId,selecttype)
            {
                // alert("graphId="+graphId)
                // alert("reportId="+reportId)
                // alert("selecttype="+selecttype)
                var reportId='<%=reportId%>'
                //alert("1")
                $.post("<%=request.getContextPath()%>/reportViewer.do?reportBy=changeGraphDataset&graphChange=graphProperties&recordset=previous&REPORTID="+reportId+"&graphId="+graphId+"&selecttype="+selecttype, $("#myGrpForm").serialize(), function(data){

                parent.refreshReportGraphs('<%=request.getContextPath()%>',reportId);
                })
//                document.forms.myGrpForm.action="<%=request.getContextPath()%>/reportViewer.do?reportBy=changeGraphDataset&graphChange=graphProperties&recordset=previous&REPORTID="+reportId+"&graphId="+graphId+"&selecttype="+selecttype;
                //alert("2")
//                document.forms.myGrpForm.submit();
                //alert("3")
//                parent.refreshReportGraphs('<%=request.getContextPath()%>',reportId);


            }


            function nextRows(graphId,reportId,selecttype)
            {
                // alert("selecttype="+selecttype)
                // alert("graphId="+graphId)
                // alert("reportId="+reportId)
                var reportId='<%=reportId%>'
                document.forms.myGrpForm.action="<%=request.getContextPath()%>/reportViewer.do?reportBy=changeGraphDataset&graphChange=graphProperties&recordset=next&REPORTID="+reportId+"&graphId="+graphId+"&selecttype="+selecttype;
                document.forms.myGrpForm.submit();
                parent.refreshReportGraphs('<%=request.getContextPath()%>',reportId);


            }
            function submiturls1(url){
//                var url = 'reportViewer.do?reportBy=viewReport&REPORTID='+reportid+'&CBOVIEW_BY'+presviewby+'='+nextviewbyid+'&CBOARP'+cboarpval+'='+ticks+'&type='+drillgr;
//              alert("reportid"+reportid)
             parent.submiturls1(url)
            }
            function updateUrlDetails(url,point){
//                alert('updateUrlDetails');
                drillUrl=url;
                drillpoint=point;
//                alert(drillUrl+''+drillpoint);
            }


            function adhocDrillForReport(presentViewby,nextViewby,paramurl){
//                alert('presentViewby ::'+presentViewby+' nextViewby::'+nextViewby)
//                alert(drillpoint+' '+drillUrl);
                var reportId='<%=reportId%>';
                var ctxpath='<%=request.getContextPath()%>';
                viewAdhocDrillForReportGraph(reportId,ctxpath,nextViewby,drillpoint,null,presentViewby,'drilldown',drillUrl,paramurl);
                
            }
            
     function viewAdhocDrillForReportGraph(reportId,ctxPath,nextViewById,measureName,viewById,presentViewById,adhocDrillType,url,paramurl){
//      alert('inPbDisplayJsForReportGraph');
      var presentViewBy=presentViewById.replace(/\A_/g,'')
     // alert(presentViewBy)
      if(adhocDrillType=="drillside"){
      //alert(url)
      $.post(ctxPath+"/reportViewer.do?reportBy=adhocChangeViewBy&newViewById="+nextViewById+"&reportId="+reportId+"&Type=tbody",
          function(data){
             if(nextViewById=="Day" || nextViewById=="Week" || nextViewById=="Month" || nextViewById=="Qtr" || nextViewById=="Year"){
                 if(url.indexOf("CBO_PRG_PERIOD_TYPE")!=-1)
                     {
                         var sideurl=null;
                         var n1=url.indexOf("&CBO_PRG_PERIOD_TYPE");
                         var n2=url.indexOf("&DDrill=Y");
                         sideurl+=url.substring(0,n1)+"&CBO_PRG_PERIOD_TYPE="+nextViewById+url.substr(n2);
                         url=sideurl.replace("null","");
                     }else{
                         url+="&CBO_PRG_PERIOD_TYPE="+nextViewById+"&DDrill=Y";
                     }
             }
             //alert(url)
            // var path=ctxPath+"/reportViewer.do?reportBy=viewReport&action=ChangeViewBy&adhocChangeViewby=true&REPORTID="+reportId +"&CBOARP" + presentViewBy + "="+measureName;
             var path=ctxPath+"/reportViewer.do?reportBy=viewReport&action=ChangeViewBy&adhocChangeViewby=true&REPORTID="+reportId +url;
           // alert(path)
            // var path=ctxPath+"/reportViewer.do?reportBy=viewReport&action=ChangeViewBy&adhocChangeViewby=true&REPORTID="+reportId +"&DDrill=Y&DrillMonth=Apr-2011";
             parent.submiturls1(path);

                     });
      }else{
          var paramUrl=paramurl;
//       alert("123"+paramUrl+".."+presentViewById+",,,,,"+nextViewById)
          if(paramUrl.indexOf(presentViewBy)!=-1)
         {
             if(nextViewById=="Day" || nextViewById=="Week" || nextViewById=="Month" || nextViewById=="Qtr" || nextViewById=="Year")
               paramUrl=paramUrl.replace(presentViewBy,"TIME");
               else
               paramUrl=paramUrl.replace(presentViewBy,nextViewById);
         }
          var path;
        if(nextViewById=="Time Drill")
          path=url+measureName;
       else if(presentViewBy=="TIME"){
           if(nextViewById=="Day" || nextViewById=="Week" || nextViewById=="Month" || nextViewById=="Qtr" || nextViewById=="Year"){
               //alert(nextViewById)
               path=null;
               var arr=new Array;
               arr=url.split("&");
               for(var i=0;i<arr.length;i++){
                  if(arr[i].indexOf("CBO_PRG_PERIOD_TYPE")!=-1){
                   path+="&CBO_PRG_PERIOD_TYPE="+nextViewById;
                  }else
                    path+="&"+arr[i];
               }
               path+=measureName;
              // path+=measureName+"&CBOVIEW_BY" + viewById + "=TIME";
               if(path.indexOf("DrillDate")!=-1)
                path+="&CBO_PRG_PERIOD_TYPE="+nextViewById+"&DDrill=Y";
                path+=paramUrl;
               path=path.replace("&","").replace("null","");
            //  alert(path)
                //path=ctxPath+'/reportViewer.do?reportBy=viewReport&REPORTID='+reportId+"&CBO_PRG_PERIOD_TYPE="+nextViewById+"&DDrill=Y";
           }
          else
          path=url+measureName+"&DDrillAcross=Y&DDrill=Y"+paramUrl;
         // path=url+measureName+"&DDrillAcross=Y&CBOVIEW_BY" + viewById + "=" + nextViewById ;
       }
        else{
          if(nextViewById=="Day" || nextViewById=="Week" || nextViewById=="Month" || nextViewById=="Qtr" || nextViewById=="Year")
          path=ctxPath+'/reportViewer.do?reportBy=viewReport&REPORTID='+reportId+"&CBO_PRG_PERIOD_TYPE="+nextViewById+"&DDrill=Y&CBOARP" + presentViewBy + "="+measureName+paramUrl;
         // path=ctxPath+'/reportViewer.do?reportBy=viewReport&REPORTID='+reportId+"&CBO_PRG_PERIOD_TYPE="+nextViewById+"&DDrill=Y&CBOVIEW_BY" + viewById + "=TIME&CBOARP" + presentViewBy + "="+measureName;
          else
          path=ctxPath+'/reportViewer.do?reportBy=viewReport&REPORTID='+reportId+"&CBOARP" + presentViewBy + "="+measureName+paramUrl;
         // path=ctxPath+'/reportViewer.do?reportBy=viewReport&REPORTID='+reportId+"&CBOVIEW_BY" + viewById + "=" + nextViewById +"&CBOARP" + presentViewBy + "="+measureName;
        // alert(path)
        }
        parent.submiturls1(path);
      }
     }

     function closeloading(){
         $("#imgId").hide();
     }
        </script>

    </head>
    <body class="body">
        <form name="myGrpForm" id="myGrpForm" action="pbGraphChanges.jsp" method="post">
            <%
                        if (request.getSession(false) != null && request.getSession(false).getAttribute("PROGENTABLES") != null) {

                            PbUserId = session.getAttribute("USERID").toString();
                            String[] viewbysTemp = null;
                            String[] barChartColumnNames = null;
                            String[] barChartColumnTitles = null;
                            String[] ViewBys = null;
                            String[] paths = null;
                            String showTable = "";
                            Integer shwRows = 0;
                            PbReturnObject retObj=new PbReturnObject();
                            PbDb pbdb = new PbDb();
                            //String[] grpTitles = null;
                            ProgenChartDisplay[] pcharts = null;
                            //String[] pathsZoom = null;
                            ProgenChartDisplay[] pchartsZoom = null;
                            String PercentColumn = "_percentwise";
                            String[] graphIds = null;
                            //String[] graphsXML = null;
                          //  ArrayList UserGraphPrevileges = (ArrayList) session.getAttribute("UserGraphPrevileges");
                            try {
                                HashMap map = (HashMap) request.getSession(false).getAttribute("PROGENTABLES");
                                container = (Container) map.get(reportId);
                                PbReportCollection collect=container.getReportCollect();
                                HashMap ParametersHashMap = container.getParametersHashMap();
                                ArrayList paramList = (ArrayList) ParametersHashMap.get("Parameters");
                                ArrayList parametersNames = (ArrayList) ParametersHashMap.get("ParametersNames");
                                String rowViewBy=collect.reportRowViewbyValues.get(0);
                                GraphTypesHashMap = (HashMap) session.getAttribute("GraphTypesHashMap");
                                GraphSizesDtlsHashMap = (HashMap) session.getAttribute("GraphSizesDtlsHashMap");
                                grpTypeskeys = (String[]) GraphTypesHashMap.keySet().toArray(new String[0]);
                                graphTypesArray = (String[]) (new TreeSet(GraphTypesHashMap.values())).toArray(new String[0]);
                                DataFacade facade = new DataFacade(container);
                                  String defaultViewId=facade.getViewbyId();
                                  String paramurl=facade.getadhocParamUrl();
                               // for(int i=0;i<graphTypesArray.length;i++)
                                //{
                                  //  
                                //}
                                

                                PbGraphDisplay GraphDisplay = new PbGraphDisplay();
                                GraphDisplay.setGraphSizesDtlsHashMap(GraphSizesDtlsHashMap);
                                GraphDisplay.setProGenImgPath(ProGenImgPath);
                                ProgenDataSet recordsRetObj = container.getRetObj();
                               GraphDisplay.setCurrentDispRetObjRecords(recordsRetObj);//works 4 fx
                               // GraphDisplay.setCurrentDispRetObjRecords(container.getDisplayedSetRetObj());//works 4 jfree
                             //   GraphDisplay.setCurrentDispRecordsRetObjWithGT(container.getDisplayedSetRetObjWithGT());
                                GraphDisplay.setAllDispRecordsRetObj(recordsRetObj);
                                GraphDisplay.setNoOfDays(container.getNoOfDays());
                                //added by santhosh.k on 01-03-2010 for reading info of entire dataset
                                GraphDisplay.setColumnAverages(recordsRetObj.getColumnAverages());
                                GraphDisplay.setColumnGrandTotals(recordsRetObj.getColumnGrandTotals());
                                GraphDisplay.setColumnOverAllMinimums(recordsRetObj.getColumnOverAllMinimums());
                                GraphDisplay.setColumnOverAllMaximums(recordsRetObj.getColumnOverAllMaximums());
                                GraphDisplay.setResolution(Integer.parseInt(String.valueOf(session.getAttribute("screenwidth"))));
                                //
                                //GraphDisplay.setPiChartMeasurelabel(container.getTableMeasureNames().get(0).toString());
                                // for setting graph color as per the theme
                                if(themeColor!=null)
                                GraphDisplay.setUITheme((String)session.getAttribute("theme"));
                                //
                                String c_img_separator = request.getContextPath() + "/TableDisplay/Images/separator.gif";
                                ArrayList dataTypes = container.getDataTypes();
                                ArrayList originalCols = container.getOriginalColumns();
                                ArrayList mesLabel = container.getDisplayLabels();
                                ArrayList<String> orginalMes=new ArrayList<String>();
                                ArrayList<String> taMeas=container.getDisplayColumns();
                                 ArrayList<String> tableMeas=new ArrayList<String>();
                                 if(taMeas!=null && mesLabel!=null ){
                                for(int i=0;i<taMeas.size();i++){
                                    tableMeas.add(taMeas.get(i));
                                }
                                for(int j=0;j<mesLabel.size();j++){
                                    orginalMes.add(mesLabel.get(j).toString());
                                }
                                                               }
                                ArrayList tableMeasureNames=container.getTableMeasureNames();
                                String[] dispHeaders = null;
                                dispHeaders = (String[]) originalCols.toArray(new String[0]);
                                ArrayList viewByColNames = container.getViewByColNames();
                                ArrayList viewByElementIds = container.getViewByElementIds();
                                int start = viewByElementIds.size();
                                String TableId = null;
                                String grpheight = null;
                                String crosstabGrpElementIdQry="select GRAPH_ID,ELEMENT_ID from PRG_AR_GRAPH_DETAILS where GRAPH_ID in(select GRAPH_ID from PRG_AR_GRAPH_MASTER where REPORT_ID="+reportId+")";
                                retObj=pbdb.execSelectSQL(crosstabGrpElementIdQry);
                                String crosstabGraphColumnMeasure="";
                               if(retObj.getRowCount()>0)
                                crosstabGraphColumnMeasure=String.valueOf(container.getReportCollect().getNonViewByMap().get("A_"+retObj.getFieldValueString(0, "ELEMENT_ID")));
                                // GraphDisplay.setViewByColNames((String[]) viewByColNames.toArray(new String[0]));
                                ViewBys = (String[]) viewByElementIds.toArray(new String[0]);
                                for (int viewIndex = 0; viewIndex < ViewBys.length; viewIndex++) {
                                    if (ViewBys[viewIndex].equalsIgnoreCase("Time")) {
                                        ViewBys[viewIndex] = ViewBys[viewIndex];
                                    } else {
                                        if (ViewBys[viewIndex].contains("A_")) {
                                            ViewBys[viewIndex] = ViewBys[viewIndex];
                                        } else {
                                            ViewBys[viewIndex] = "A_" + ViewBys[viewIndex];
                                        }
                                    }
                                }
                                GraphDisplay.setViewByColNames((String[]) viewByColNames.toArray(new String[0]));
                                GraphDisplay.setViewByElementIds(ViewBys);
                                GraphDisplay.setCtxPath(request.getContextPath());
                                GraphDisplay.setTimelevel(container.getTimeLevel());

                                ArrayList links = container.getLinks();
                                if (links != null && links.size() != 0) {
                                    GraphDisplay.setJscal(String.valueOf(links.get(0)));
                                } else {
                                    GraphDisplay.setJscal("reportViewer.do?reportBy=viewReport&REPORTID=" + reportId + "&");
                                }
                                GraphDisplay.setSession(request.getSession(false));
                                GraphDisplay.setResponse(response);
                                GraphDisplay.setOut(out);
                                GraphDisplay.setReportId(reportId);

                                HashMap[] graphMapDetails = container.getGraphMapDetails();

                                GraphDisplay.setGraphHashMap(container.getGraphHashMap());
                                GraphDisplay.setGraphMapDetails(graphMapDetails);
                                GraphDisplay.setIsCrosstab(container.isReportCrosstab());
                                GraphDisplay.setSortColumns(container.getSortColumns());
                                //code to change graph types

                                ArrayList grpDetails = new ArrayList();//= GraphDisplay.getGraphHeaders();

                               /* if (Integer.parseInt(container.getColumnViewByCount()) != 0) {
                                    viewbysTemp = (String[]) container.getViewByElementIds().toArray(new String[0]);//new String[container.getViewByElementIds().size()];
                                    ArrayList<String> chartCols;
                                    ArrayList<String> chartLabels;

                                    chartCols = new ArrayList<String>();
                                    chartLabels = new ArrayList<String>();
                       --for ( int c = 0; c < container.getDisplayColumns().size(); c++ )

                                    {
                                        //inserted new logic to eleminate %wise column in barchart columns
                                        //%wise columns are now part of crosstab and should not be included in graphs
                                        //if ( ((String)container.getDisplayColumnsInColumnPagination().get(c)).lastIndexOf(PercentColumn) == -1 )
                                        if ( ! RTMeasureElement.isRunTimeMeasure((String)container.getDisplayColumns().get(c)) )
                                        {
                                            chartCols.add((String)container.getDisplayColumns().get(c));
                                            if(String.valueOf(container.getDisplayLabels().get(c)).contains(","))
                                            chartLabels.add(String.valueOf(container.getDisplayLabels().get(c)).substring(1, String.valueOf(container.getDisplayLabels().get(c)).indexOf(",")));
                                            else
                                            chartLabels.add(String.valueOf(container.getDisplayLabels().get(c)));

                                        }--

                                    barChartColumnNames = new String[chartCols.size()];
                                    barChartColumnTitles = new String[chartLabels.size()];
                                    for (int i = 0; i < barChartColumnNames.length; i++) {
                                        barChartColumnNames[i] = String.valueOf(chartCols.get(i));
                                        barChartColumnTitles[i] = String.valueOf(chartLabels.get(i));
                                    }
                                    for (int h = 0; h < viewbysTemp.length; h++) {
                                        viewbysTemp[h] = String.valueOf(container.getDisplayColumns().get(h));
                                        barChartColumnNames[h] = String.valueOf(container.getDisplayColumns().get(h));
                                    }
                                    GraphDisplay.setViewByElementIds(viewbysTemp);
                                }*/


                                if (isFxCharts) {
                                    if (Integer.parseInt(container.getColumnViewByCount()) != 0) {
                                        grpDetails = GraphDisplay.get2dGraphHeadersFX(barChartColumnNames, barChartColumnTitles, viewbysTemp);
                                    } else {
                                        grpDetails = GraphDisplay.getGraphHeadersFX();
                                    }
                                } else {
                                    if (Integer.parseInt(container.getColumnViewByCount()) != 0) {
                                        //grpDetails = GraphDisplay.get2dGraphHeaders(barChartColumnNames, barChartColumnTitles, viewbysTemp,container);
                                        grpDetails = GraphDisplay.get2dGraphHeaders(container);
                                    } else {
                                        grpDetails = GraphDisplay.getGraphHeaders(container.getNoOfDays(),container);
                                    }
                                }
                                container.setGraphHashMap(GraphDisplay.getGraphHashMap());
                                grphIds = String.valueOf(container.getGraphHashMap().get("graphIds"));
                                graphId=grphIds.split(",");
                                
                                if (session.getAttribute("updateShowTable") != null) {
                                    showTable = session.getAttribute("updateShowTable").toString();
                                } else {
                                    String shwtabQry = "select SHOW_TABLE from prg_ar_graph_master where report_id =" + reportId;
                                    PbReturnObject shwTabRo = new PbReturnObject();
                                    shwTabRo = (new PbDb()).execSelectSQL(shwtabQry);
                                    ////.println("shwtabQry"+shwtabQry);
                                    if ( shwTabRo != null && shwTabRo.getRowCount() > 0 ) {
                                        if(shwTabRo.getFieldValue(0, 0)!=null)

                                        if (!shwTabRo.getFieldValueString(0, 0).equalsIgnoreCase("NULL"))
                                            showTable = shwTabRo.getFieldValueString(0, 0);
                                            //   ////.println("in GDR---"+showTable);

                                    }
                                    if (showTable.equalsIgnoreCase("")) {
                                        showTable = "GM";
                                    }
                                }
            %>

            <input type="hidden" id="showTable" name="showTable" value="<%=showTable%>">
            <input type="hidden" id="grptypid" name="grptypid" value="">
            <input type="hidden" id="grpsizeid" name="grpsizeid" value="">
            <input type="hidden" id="gid" name="gid" value="">
            <input type="hidden" id="tabId" name="tabId" value="<%=reportId%>">
            <input type="hidden" id="TableId" name="TableId" value="<%=TableId%>">
            <input type="hidden" id="graphChange" name="graphChange" value="">
            <!-- here tabId is report id not Table Id -->
            <input type="hidden" id="columnKeys" name="columnKeys" value="">
            <input type="hidden" id="columnAxis" name="columnAxis" value="">
            <input type="hidden" name="swapBy" id="swapBy" value="">
            <input type="hidden" name="ctxPath" id="ctxPath" value="<%=request.getContextPath()%>">
            <!-- added by malli.-->
            <div id="showid">
                <center><img id="imgId" src="<%= request.getContextPath()%>/images/ajax1.gif" align="middle"  width="100px" height="100px"  style="top:100px; position:absolute" /></center>
                <%           if(grpDetails != null && grpDetails.size() == 0){
                              %>
                                <script type="text/javascript">
                                parent.document.getElementById("tabGraphs").style.display='none';

                             </script>

                                  <%}else if (grpDetails != null && grpDetails.size() != 0) {

                                        paths = grpDetails.get(0).toString().split(";");//grpDetails[0].split(";");
                                        //grpTitles = grpDetails.get(1).toString().split(";");
                                        pcharts = (ProgenChartDisplay[]) grpDetails.get(2);
                                        //pathsZoom = grpDetails.get(3).toString().split(";");//grpDetails[0].split(";");
                                        pchartsZoom = (ProgenChartDisplay[]) grpDetails.get(4);
                                        graphMapDetails = (HashMap[]) grpDetails.get(5);
                                        container.setGraphMapDetails(graphMapDetails);
                                        graphIds = grphIds.split(",");

                                        if (graphIds != null && graphIds.length != 0 && !"".equalsIgnoreCase(graphIds[0])) {
                                            Vector[] grapVector = new Vector[graphIds.length];
                                            Vector[] orgGrapVector = new Vector[graphIds.length];
                                            for (int k = 0; k < grapVector.length; k++) {
                                                grapVector[k] = new Vector();
                                                orgGrapVector[k] = new Vector();
                                            }
                                            container.setImgPaths(paths);

                                            HashMap ReportHashMap = container.getReportHashMap();
                                            int colViewByCount = 0;

                                            if (container.getColumnViewByCount() != null) {
                                                colViewByCount = Integer.parseInt(container.getColumnViewByCount());
                                            }
                                            String[] repBisRoles = (String[]) ReportHashMap.get("ReportFolders");
                                            StringBuffer sbufRoles = new StringBuffer("");
                                            StringBuffer sbufGrpIds = new StringBuffer("");

                                            for (int k = 0; k < repBisRoles.length; k++) {
                                                sbufRoles.append("," + repBisRoles[k]);
                                            }
                                            for (int k = 0; k < graphIds.length; k++) {
                                                sbufGrpIds.append("," + graphIds[k]);
                                            }
                                                HashMap singeGraphDetails1 = (HashMap) container.getGraphHashMap().get(graphIds[0]);
                                                  String[] grprgbColorCode1=null;
                                                    grprgbColorCode1=(String[])singeGraphDetails1.get("rgbColorArr");
                                                 String grpTitle1 = singeGraphDetails1.get("graphName").toString();
                            %>
                            
            <Table width="100%" style="border:0px solid #369;overflow: auto;" cellpadding="1" cellspacing="0">

                <% if (!showTable.equalsIgnoreCase("GTM")) {
                                        if ((grphIds != null && !grphIds.equalsIgnoreCase("") && grphIds.split(",").length < 2)) {%>
<!--                <Tr valign="top" >
                    <Td valign="top"><a href="javascript:void(0)" style="text-decoration:none;cursor:pointer" onclick="showGraphsList()" title="click to add graphs(s)">Add Graphs...</a></Td>
                </Tr>-->
                
                
                <%}%>
                <Tr valign="top" style="width: 100%">
                    <Td valign="top" colspan="2">
                                    <Table class="progenTable" STYLE="width:auto"   border='0' align="left" cellpadding="1" cellspacing="0">
                                                    <tr valign="top">
                                                        <td>
                                                            <div id='DrillDiv' class="overlapDiv" style='display:none;width:100%;height:90%;overflow-y:auto;position:absolute;overflow-x: hidden;'>
                                                                <ul class="graphTypesStyle">
                                                                    <% for(int i=0;i<paramList.size();i++){%>
                                                                        <li  onclick='adhocDrillForReport("A_<%=rowViewBy%>","<%=paramList.get(i)%>","<%=paramurl%>")'><%=parametersNames.get(i)%></li>
                                                                    <% }%>
                                                                    <li onclick='adhocDrillForReport("A_<%=rowViewBy%>","Day","<%=paramurl%>")'>Daily</li>
                                                                    <li onclick='adhocDrillForReport("A_<%=rowViewBy%>","Week","<%=paramurl%>")'>Weekly</li>
                                                                    <li onclick='adhocDrillForReport("A_<%=rowViewBy%>","Month","<%=paramurl%>")'>Month</li>
                                                                    <li onclick='adhocDrillForReport("A_<%=rowViewBy%>","Qtr","<%=paramurl%>")'>Qtr</li>
                                                                    <li onclick='adhocDrillForReport("A_<%=rowViewBy%>","Year","<%=paramurl%>")'>Year</li>
                                                                </ul>
                                                            </div>
                                                        </td>

                                                       <% if ((grphIds != null && !grphIds.equalsIgnoreCase("") && grphIds.split(",").length < 2)) {
                                                          if(userdao.getFeatureEnable("Add Graph") || userType.equalsIgnoreCase("SUPERADMIN"))  {
                                                                     String tabSeqName;
                                    int sequence;
                                     if(ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER))
                                     {
                                        tabSeqName = "-100";//IDENT_CURRENT('PRG_AR_GRAPH_MASTER')";
                                        sequence = -100;
                                     }else if(ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL))
                                     {
                                        //tabSeqName = "select LAST_INSERT_ID('GRAPH_ID') from PRG_AR_GRAPH_MASTER order by 1 desc limit 1";
                                       tabSeqName = "-100";//IDENT_CURRENT('PRG_AR_GRAPH_MASTER')";
                                        sequence = -100;
                                     }
                                     else
                                     {
                                        tabSeqName = "PRG_AR_GRAPH_MASTER_SEQ";
                                        sequence =  DAO.getSequence(tabSeqName);
                                     }
     
                                                  %>
                                                   <Td valign="top"><a href="javascript:void(0)" style="text-decoration:none;cursor:pointer" onclick="addMoreGraphs('<%=reportId%>','<%=grphIds%>','Bar','<%=request.getContextPath()%>','<%=sequence%>')" title="Add graphs(s)" class="ui-icon ui-icon-plus">Add Graphs...</a></Td>
<!--                    <Td valign="top"><a href="javascript:void(0)" style="text-decoration:none;cursor:pointer" onclick="showGraphsList()" title="Add graphs(s)" class="ui-icon ui-icon-plus">Add Graphs...</a></Td>-->
                              <% }  }if(userdao.getFeatureEnable("Graph Save") || userType.equalsIgnoreCase("SUPERADMIN"))  {%>
                           <Td align="left" valign="top"><a  href="javascript:void(0)" class="ui-icon ui-icon-disk" title="saveGraphRegion" onclick="saveGraphRegion('<%=request.getContextPath()%>','<%=reportId%>')" style='text-decoration:none'>saveGraphRegion</a></Td>
                                                                 <% }if(userdao.getFeatureEnable("Graph Types") || userType.equalsIgnoreCase("SUPERADMIN"))  {
                                        for (int i = 0; i < graphId.length; i++) {%>
                                                        
                                                      <Td align="left" valign="top">
                                                            <a  href="javascript:void(0)" class="ui-icon ui-icon-image" title="Type<%=i + 1%>" onclick="graphTypesDisp(document.getElementById('dispgrptypes12<%=jqgraphIds[i]%>'),'jq');" style='text-decoration:none'>Types2 </a>
                                                        </Td>
                                                      
                                                         <%} 
                                        }
                                        ArrayList graphTypename=new ArrayList();
                                  ArrayList graphTypeid=new ArrayList();
                                  ArrayList graphid=new ArrayList();
                                        for (int i = 0; i < graphId.length; i++) {
                                         String selectedgraphtype1=null;  
                              JqplotGraphProperty graphproperty=new JqplotGraphProperty();
                              HashMap singleGraphDetails = (HashMap) container.getGraphHashMap().get(graphId[i]);
                              PbReportViewerDAO reportViewerdao=new PbReportViewerDAO();  
                              if(singleGraphDetails.get("jqgraphproperty"+graphIds[i])!=null){
                                 graphproperty= (JqplotGraphProperty)singleGraphDetails.get("jqgraphproperty"+graphIds[i]); 
                                 graphTypename.add(graphproperty.getGraphTypename());
                                 graphTypeid.add(graphproperty.getGraphTypeId());
                              }else{
                                 graphproperty = reportViewerdao.getJqGraphDetails(graphIds[i]); 
                                 if(graphproperty!=null){
                                 graphTypename.add(graphproperty.getGraphTypename());
                                 graphTypeid.add(graphproperty.getGraphTypeId());  
                              }
                                   if(graphTypename.isEmpty() || graphId.length==2 && (graphproperty==null || graphproperty.getGraphTypename().isEmpty() || graphproperty.getGraphTypename().equalsIgnoreCase(""))){
                                   graphTypename.add("Bar-Vertical");
                                   graphTypeid.add("5500");
                               } 
                              } if(userdao.getFeatureEnable("Table Columns") || userType.equalsIgnoreCase("SUPERADMIN"))  {
                                                    if(!container.isReportCrosstab()){%>
                                                        <Td align="left" valign="top">
                                                            <a  href="javascript:void(0)" class="ui-icon ui-icon-calculator" title="TableColumns<%=i + 1%>" onclick="tableColumns('tableCol<%=i%>','tableMeasN<%=i%>','<%=reportId%>','<%=graphId[i]%>','<%=i%>','<%=graphTypeid.get(i)%>','<%=graphTypename.get(i)%>');" style='text-decoration:none'>Types2 </a>
                                                        </Td>
                                                        <%}}if(userdao.getFeatureEnable("Zoom") || userType.equalsIgnoreCase("SUPERADMIN"))  { %>
                                                         <Td align="left" valign="top">
                <a  href="javascript:void(0)" class="ui-icon ui-icon-zoomin" title="ZoomGraph<%=i + 1%>" onclick="zoomgraph('<%=reportId%>','<%=graphTypename.get(i)%>','<%=selectedgraphtype1%>','<%=graphTypeid.get(i)%>','<%=graphIds[i]%>','<%=i%>')" style='text-decoration:none'>zoom </a>
               </Td>
                                                          
                                                        <%}
                                        }if(userdao.getFeatureEnable("Columns") || userType.equalsIgnoreCase("SUPERADMIN"))  {
                                                             for (int i = 0; i < graphId.length; i++) {%>
                                                        <Td align="left" valign="top" > <a  href="javascript:void(0)" onclick="dispChangeGrpColumns('<%=request.getContextPath()%>','<%=sbufGrpIds.toString().substring(1)%>','<%=graphId[i]%>','<%=sbufRoles.toString().substring(1)%>','<%=reportId%>' )" style='text-decoration:none' class="ui-icon ui-icon-circlesmall-plus" title="Columns <%=i + 1%>">Columns<%=i + 1%></a></Td>
                                                            
                                                        <% }
                                                             }if(userdao.getFeatureEnable("Properties") || userType.equalsIgnoreCase("SUPERADMIN"))  {
                                                          for (int i = 0; i < graphId.length; i++) {%>
                                                         <Td>
                                                            <a  href="javascript:void(0)" onclick="showGrpProperties('<%=reportId%>','<%=graphId[i]%>','<%=request.getContextPath()%>','graph');" style='text-decoration:none' class="ui-icon ui-icon-pencil"   title="Properties <%=i + 1%>"></a>
                                                        </Td>
                                                      
                                                        <% }
                                                            }if(userdao.getFeatureEnable("Delete") || userType.equalsIgnoreCase("SUPERADMIN"))  {
                                                            for (int i = 0; i < graphId.length; i++) {%>
                                                        
                                                      <Td COLSPAN="1" valign="top" >                                   
                                                            <a href="javascript:void(0)" onclick="deleteGraph('<%=graphIds[i]%>','<%=reportId%>')" title="Delete Graph<%=i + 1%>" class="ui-icon ui-icon-trash"></a>
                                                        </Td>  <% }
                                                            }if(userdao.getFeatureEnable("Rename") || userType.equalsIgnoreCase("SUPERADMIN"))  {
                                                            for (int i = 0; i < graphId.length; i++) {
                                                           singeGraphDetails1 = (HashMap) container.getGraphHashMap().get(graphIds[i]);
                                                                         grpTitle1 = singeGraphDetails1.get("graphName").toString();%>
                                       <Td COLSPAN="1" valign="top" >  
                                                            <a href="javascript:void(0)" style="text-decoration:none;cursor:pointer; font-size: 12;" class="ui-icon ui-icon-key" onclick="editGraphTitle('<%=graphIds[i]%>','<%=grpTitle1%>')" title="Title <%=i + 1%>"><%=grpTitle1%></a>
                                      </Td>
                                                         <%}  }%>
                                                        
                                                    </tr>
                                                    <% for (int i = 0; i < graphId.length; i++) {%>
                                                    <tr>
                                                        <td valign="top">                                                                                                                   
                                                            <div class="flagDiv" style="display:none"  id="tableCol<%=i%>">
                                                                <Table>
                                                                    <% String columnId ="";
                                                                    StringBuffer columnName=null;
                                                                    String[] graphMeas=null;
                                                                    String graphmeas="";
                                                                    java.util.List <String> graphMeaslist=new java.util.ArrayList<String> ();
                                                                    HashMap singleGraphDetails = (HashMap) container.getGraphHashMap().get(graphId[i]);
                                                                        JqplotGraphProperty graphproperty = new JqplotGraphProperty();
                                                                        PbReportViewerDAO reportViewerdao = new PbReportViewerDAO();
                                                                        if (singleGraphDetails.get("jqgraphproperty" + graphIds[i]) != null) {
                                                                            graphMeas = (String[]) singleGraphDetails.get("barChartColumnNames");
                                                                        } else {
                                                                            graphproperty = reportViewerdao.getJqGraphDetails(graphIds[i]);
                                                                            if (graphproperty != null) {
                                                                                graphMeas = (String[]) graphproperty.getTableColumns();
                                                                                    
                                                                            }
                                                                        }
                                                                    if(graphMeas==null)
                                                                    graphMeas=(String[]) singleGraphDetails.get("barChartColumnNames");
                                                                    String[] graphmeastitle=(String[]) singleGraphDetails.get("barChartColumnTitles");
                                                                    graphMeaslist = Arrays.asList(graphMeas);  
                                                                      for(int j=0;j<graphMeas.length;j++){
                                                                          if(!tableMeas.contains(graphMeas[j])){
                                                                              tableMeas.add(graphMeas[j]);
                                                                              orginalMes.add(graphmeastitle[j]);

                                                                          }
                                                                      }
                                                                    for(int k=viewByElementIds.size();k<tableMeas.size();k++){  
                                                                        columnName = new StringBuffer("");                                                                                                                                             
                                                                        columnId=String.valueOf(tableMeas.get(k));
                                                                        columnName.append("-"+orginalMes.get(k));
                                                                       
                                                                    %> 
                                                                   
                                                            <Tr valign="top" >
                                                                        <Td valign="top"  align="left">
                                                                      <%if (graphMeaslist!=null && graphMeaslist.contains(columnId)) {%>
                                                                            <input type="checkbox" checked value="<%=columnId%>" id="tableMeasN<%=i%>" name="tableMeasN<%=i%>"><%=columnName.toString().substring(1)%>
                                                                            <%} else if (tableMeasureNames!=null && !tableMeasureNames.isEmpty()) {%> 
                                                                            <input type="checkbox" value="<%=columnId%>" id="tableMeasN<%=i%>" name="tableMeasN<%=i%>"><%=columnName.toString().substring(1)%>
                                                                           <%}%> 
                                                                        </Td>
                                                                    </Tr>
                                                                    <%columnName = null;
                                                                    }%>
                                                                </Table>
                                                            </div>
                                                        </td>
                                                            
                                                    </tr>
                                                      <% }for (int i = 0; i < graphId.length; i++) {%>
                                                    <tr>
                                                        <td valign="top">                                                                                                                   
                                                            <div class="flagDiv" style="display:none"  id="dispgrptypes12<%=jqgraphIds[i]%>">
                                                                <Table>
                                                                    <%for (int gqtype = 0; gqtype < jqgrapharray.length; gqtype++) {%>               
                                                                    <Tr>
                                                                      
                                                                        <Td id="<%=gqtype%>"><a href='javascript:void(0)' onclick='buildJqGraph("<%=jqgrapharray[gqtype]%>","<%=graphId[i]%>","<%=jqgraphIds[i]%>","<%=reportId%>","<%=i%>")'><%=jqgrapharray[gqtype]%></a></Td>
                                                                    </Tr>
                                                                  
                                                                    
                                                                         <%}%>
                                                                </Table>
                                                            </div>
                                                        </td>
                                                        
                                                    </tr>
                                                      <%}%> 
                                                   
                                                </Table>
                                            </Td>
                                    </Tr>
                                    <%}%>
                                   
                                 
                                                  
                                                        
                                          
                  <%if(grprgbColorCode1!=null && grprgbColorCode1.length>0){%>
                            <Tr valign="top" style="background-color:background-color:rgb(<%=Integer.parseInt(grprgbColorCode1[0].trim())%>,<%=Integer.parseInt(grprgbColorCode1[1].trim())%>,<%=Integer.parseInt(grprgbColorCode1[2].trim())%>)" >
                                <%}else{%>
                <Tr valign="top">
                                <%}%>
                    <%for (int grpCnt = 0; grpCnt < graphIds.length; grpCnt++) {
                        
                                                    int gqtype; 
                                                    ArrayList RowValues = null;
                                                    String[] barCol = null;
                                                    String[] barColTit = null;
                                                    String rowVals;
                                                    Integer dispRows = 0;
                                                    String[] grprgbColorCode=null;
                                                    HashMap singeGraphDetails = (HashMap) container.getGraphHashMap().get(graphIds[grpCnt]);
                                                    String selectedgraphtype=container.getSlectedGraphType(graphIds[grpCnt]); 
                                                    String[] colorSeries= null;
                                                    if (singeGraphDetails != null) {
                                                        barCol = (String[]) singeGraphDetails.get("barChartColumnNames");
                                                        barColTit = (String[]) singeGraphDetails.get("barChartColumnTitles");
                                                        colorSeries = (String[]) singeGraphDetails.get("colorSeries");
                                                        pcharts[grpCnt].overWriteColorSeries(colorSeries);
                                                        rowVals = singeGraphDetails.get("graphDisplayRows").toString();
                                                        //     ////.println("---rowVals----"+rowVals);
                                                        grprgbColorCode=(String[])singeGraphDetails.get("rgbColorArr");
                                                        if (!rowVals.equalsIgnoreCase("") && !rowVals.equalsIgnoreCase("NULL") && !rowVals.equalsIgnoreCase("ALL")) {
                                                            dispRows = Integer.parseInt(rowVals);
                                                        } else if (rowVals.equalsIgnoreCase("ALL")) {
                                                            dispRows = recordsRetObj.getRowCount();
                                                        } else {
                                                            dispRows = (recordsRetObj.getRowCount() > 10) ? 10 : recordsRetObj.getRowCount();
                                                        }

                                                    }
                                                    FxXML = new PbFxXML();
                                                    String grpTitle = singeGraphDetails.get("graphName").toString();
                                                    if (singeGraphDetails.get("RowValuesList") != null) {
                                                        RowValues = (ArrayList) ((ArrayList) singeGraphDetails.get("RowValuesList")).clone();
                                                    }
                                                    if (RowValues == null) {
                                                        RowValues = new ArrayList();
                                                    }
                                                    String graphType = "";
                                                    StringBuffer sbuffer = new StringBuffer("");
                                                    //code specific for fx charts only
                                                    graphType = String.valueOf(singeGraphDetails.get("graphTypeName"));
                                                    String graphWidth = String.valueOf(singeGraphDetails.get("graphWidth"));
                                                    String graphHeight = String.valueOf(singeGraphDetails.get("graphHeight"));
                                                    if (isFxCharts) {


                                                        //need to be removed code from here to PbGrphDisplay file where java fx code is exists
                                                        GraphDisplay.setProGenImgPath(ProGenImgPath);

                                                        if (Integer.parseInt(container.getColumnViewByCount()) != 0) {
                                                            GraphDisplay.setViewByElementIds(viewbysTemp);
                                                        } else {
                                                            GraphDisplay.setViewByElementIds((String[]) singeGraphDetails.get("viewByElementIds"));
                                                        }


                                                        GraphDisplay.setBarChartColumnNames((String[]) singeGraphDetails.get("barChartColumnNames"));
                                                        GraphDisplay.setBarChartColumnTitles((String[]) singeGraphDetails.get("barChartColumnTitles"));
                                                        GraphDisplay.setPieChartColumns((String[]) singeGraphDetails.get("pieChartColumns"));
                                                        //GraphDisplay.setViewByElementIds((String[]) singeGraphDetails.get("viewByElementIds"));

                                                        String[] aa=(String[])singeGraphDetails.get("barChartColumnNames1");

                                                        GraphDisplay.setBarChartColumnNames1((String[]) singeGraphDetails.get("barChartColumnNames1"));
                                                        GraphDisplay.setBarChartColumnTitles2((String[]) singeGraphDetails.get("barChartColumnTitles1"));
                                                        GraphDisplay.setBarChartColumnNames2((String[]) singeGraphDetails.get("barChartColumnNames2"));
                                                        GraphDisplay.setBarChartColumnTitles2((String[]) singeGraphDetails.get("barChartColumnTitles2"));

                                                        GraphDisplay.graphTypeName = String.valueOf(singeGraphDetails.get("graphTypeName"));
                                                        graphType = GraphDisplay.graphTypeName;
                                                        //end of code to be commented later
                                                        HashMap singleRecord = new HashMap();
                                                        boolean SwapColumn = Boolean.parseBoolean(String.valueOf(singeGraphDetails.get("SwapColumn")));
                                                        String graphDispRows = singeGraphDetails.get("graphDisplayRows").toString();

                                                        SwapColumn = graphType.equalsIgnoreCase("ColumnPie") || graphType.equalsIgnoreCase("Spider") ? !SwapColumn : SwapColumn;
                                                        ArrayList rowValuesList = (singeGraphDetails.get("RowValuesList") != null) ? (ArrayList) singeGraphDetails.get("RowValuesList") : new ArrayList();
                                                        singleRecord.put("graphDispRows", graphDispRows);
                                                        singleRecord.put("rowValuesList", rowValuesList);
                                                        singleRecord.put("TimeLevel", GraphDisplay.getTimelevel());
                                                        singleRecord.put("columnviewby",String.valueOf(container.getColumnViewByCount()));
                                                        if (SwapColumn) {
                                                            GraphDisplay.getFxDataSet(singleRecord);
                                                        } else {
                                                            GraphDisplay.getFxDataSetForMeasureAnalysis(singleRecord);
                                                        }
                                                        sbuffer = FxXML.getFxXML(singleRecord, singeGraphDetails, PbUserId, reportId, graphIds[grpCnt], links);

                                                    } else {
                                                        if (showTable.equalsIgnoreCase("GTM")) {
                                                            pcharts[grpCnt].setCtxPath(request.getContextPath());
                                                        } else {
                                                            pcharts[grpCnt].setCtxPath(request.getContextPath());
                                                            pchartsZoom[grpCnt].setCtxPath(request.getContextPath());
                                                        }
                                                    }
                                                     String selectedgraphtype1=null;  
                                  String graphTypename=null;
                                  String graphTypeid=null;
                                  String graphid=null;  
                         JqplotGraphProperty graphproperty=new JqplotGraphProperty();
                              HashMap singleGraphDetails = (HashMap) container.getGraphHashMap();
                              PbReportViewerDAO reportViewerdao=new PbReportViewerDAO();                        
                                                             
                              if(singleGraphDetails.get("jqgraphproperty"+graphIds[grpCnt])!=null){
                                 graphproperty= (JqplotGraphProperty)singleGraphDetails.get("jqgraphproperty"+graphIds[grpCnt]); 
                                 selectedgraphtype1 = graphproperty.getSlectedGraphType(graphIds[grpCnt]);
                                 graphTypename=graphproperty.getGraphTypename();
                                 graphTypeid=graphproperty.getGraphTypeId();
                                 graphid=graphproperty.getGraphId();
                                if(graphproperty.getRowValues()!=null){
                                String[] rowvalues=graphproperty.getRowValues();
                                 for(int i=0;i<rowvalues.length;i++)
                                 RowValues.add(rowvalues[i]);
                                 } 
                              }else{
                                 graphproperty = reportViewerdao.getJqGraphDetails(graphIds[grpCnt]); 
                                 if(graphproperty!=null){
                                 selectedgraphtype1 = graphproperty.getSlectedGraphType(graphIds[grpCnt]);
                                 graphTypename=graphproperty.getGraphTypename();
                                 graphTypeid=graphproperty.getGraphTypeId();
                                 graphid=graphproperty.getGraphId();                  
                                 if(graphproperty.getRowValues()!=null){
                                String[] rowvalues=graphproperty.getRowValues();
                                 for(int i=0;i<rowvalues.length;i++)
                                 RowValues.add(rowvalues[i]);
                                                                 }
                              }
                              }
                              if(selectedgraphtype1==null){
                                  selectedgraphtype1=selectedgraphtype;
                              }
                 
                          int frwidth=100;
                                   if(grpCnt>0){
                                      frwidth=(frwidth/(grpCnt+1));
                                   }                                                   
                                                    if (showTable.equalsIgnoreCase("GTM")) {%>
                    <td width="50%" valign="top" style="border:0px solid #454545">
                        <div align="center" class="portlet-header  ui-corner-all" style="font-weight:bold">

                            <a href="javascript:void(0)" style="text-decoration:none;cursor:pointer;" onclick="editGraphTitle('<%=graphIds[grpCnt]%>','<%=grpTitle%>')" title="click to edit title"><%=grpTitle%></a>

                        </div>
                        <div style="height:300px;overflow:auto" align="center">
                            <table align="center" class="tablesorter" cellpadding="0" cellspacing="1" width="100%">
                                <thead>
                                    <tr>
                                        <%for (int view = 0; view < barCol.length; view++) {%>
                                        <th style="font-family:verdana">
                                            <%=barColTit[view]%>
                                        </th>
                                        <%}%>
                                    </tr>
                                </thead>
                                <tbody>
                                    <% if (dispRows != null) {
                                                                            dispRows = (recordsRetObj.getRowCount() > dispRows) ? dispRows : recordsRetObj.getRowCount();
                                                                        }
                                                                        ////////.println("dispRows---"+dispRows);
                                                                        for (int ret = 0; ret < dispRows; ret++) {%>
                                    <tr>
                                        <%for (int view = 0; view < barCol.length; view++) {%>
                                        <td style="font-family:verdana">
                                            <%=recordsRetObj.getFieldValueString(ret, barCol[view])%>
                                        </td>
                                        <%}%>
                                    </tr>
                                    <%}%>
                                </tbody>
                            </table>
                        </div>
                    </td>
                    <%}%>
                     <%if(grprgbColorCode!=null && grprgbColorCode.length>0){%>
                              <Td valign="top" width="50%" id="graphtd<%=graphIds[grpCnt]%>" style="background-color:rgb(<%=Integer.parseInt(grprgbColorCode[0].trim())%>,<%=Integer.parseInt(grprgbColorCode[1].trim())%>,<%=Integer.parseInt(grprgbColorCode[2].trim())%>)">
                                <%}else if((grprgbColorCode1!=null && grprgbColorCode1.length>0)){%>
                                  <Td valign="top" width="50%" id="graphtd<%=graphIds[grpCnt]%>" style="background-color:rgb(<%=Integer.parseInt(grprgbColorCode1[0].trim())%>,<%=Integer.parseInt(grprgbColorCode1[1].trim())%>,<%=Integer.parseInt(grprgbColorCode1[2].trim())%>)">
                                <%}else{%>
                    <Td valign="top" width="50%" id="graphtd<%=graphIds[grpCnt]%>">
                                <%}%>
                    <!--<Td valign="top" width="50%" id="graphtd<%=graphIds[grpCnt]%>">-->
                        <Table width="100%" cellpadding="0" cellspacing="0" class="dashboardtableborder" style="border:0px solid #454545;background-color:#454545;-moz-border-radius-bottomleft:0px;-moz-border-radius-bottomright:0px;-moz-border-radius-topleft:0px;-moz-border-radius-topright:0px;">
<!--                            <Tr valign="top">
                                <Td COLSPAN="1" valign="top">
                                    <div align="center" class="portlet-header" style="font-weight:bold;height:20px">
                                        <a href="javascript:void(0)" style="text-decoration:none;cursor:pointer; font-size: 12;" onclick="editGraphTitle('<%=graphIds[grpCnt]%>','<%=grpTitle%>')" title="click to edit title"><%=grpTitle%></a>

                                    </div>
                                </Td>
                                <Td COLSPAN="1" valign="top" >
                                    <div align="center" class="bgcolorTrash" style="height:24px">
                                    <a href="javascript:void(0)" onclick="deleteGraph('<%=graphIds[grpCnt]%>','<%=reportId%>')" title="Delete Graph" class="ui-icon ui-icon-trash"></a>
                                    </div>
                                </Td>

                            </Tr>-->
                            <Tr valign="top">
                                <Td valign="top">
                                    <Table class="progenTable" STYLE="width:auto"   border='0' align="left">
                                        <Tr valign="top">
                                            <%//if(PrivilegeManager.isComponentEnabledForUser("REPORT","GRAPHTYPES",Integer.parseInt(PbUserId))){%>
<!--                                            <Td valign="top"  align="left">
                                                <table>
                                                    <tr valign="top">
                                                        <Td align="left" valign="top">
                                                            <a  href="javascript:void(0)" onclick="graphTypesDisp(document.getElementById('dispgrptypes<%=graphIds[grpCnt]%>'),'jf');" style='text-decoration:none' class="calcTitle" title="Graph Types">Types </a>
                                                        </Td>
                                                    </tr>
                                                    <tr valign="top">
                                                        <td valign="top">
                                                            <div class="flagDiv" style="display:none"  id="dispgrptypes<%=graphIds[grpCnt]%>">
                                                                <Table>
                                                                    <%for (int gtype = 0; gtype < graphTypesArray.length; gtype++) {
                                                                             if (String.valueOf(singeGraphDetails.get("graphTypeName")).equalsIgnoreCase(graphTypesArray[gtype])) {%>
                                                                    <Tr>
                                                                        <Td id="<%=gtype%>"><b><%=graphTypesArray[gtype]%></b></Td>
                                                                    </Tr>
                                                                    <%graphType = graphTypesArray[gtype];
                                                                             } else {%>
                                                                             <% if(!graphTypesArray[gtype].equalsIgnoreCase("FeverChart") && !graphTypesArray[gtype].equalsIgnoreCase("Meter") && !graphTypesArray[gtype].equalsIgnoreCase("Bubble") && !graphTypesArray[gtype].equalsIgnoreCase("Dial") && !graphTypesArray[gtype].equalsIgnoreCase("ProGenChart") && !graphTypesArray[gtype].equalsIgnoreCase("Thermometer")) {%>
                                                                    <Tr>
                                                                        
                                                                        <Td id="<%=gtype%>"><a href='javascript:void(0)' onclick='changeGrpType("<%=graphTypesArray[gtype]%>","<%=graphIds[grpCnt]%>","<%=reportId%>")'><%=graphTypesArray[gtype]%></a></Td>
                                                                    </Tr>
                                                                    <%}%>
                                                                    <%}
                                                                         }%>
                                                                </Table>
                                                            </div>
                                                        </td>
                                                    </tr>
                                                </table>
                                            </Td>-->
<!--                                            <Td valign="top"  align="left"><img src=<%=c_img_separator%> style="border:0"  alt="Separator" /></Td>
                                            <Td valign="top"  align="left">
                                              <Td valign="top"  align="left">
                                                <table>
                                                    <tr valign="top">
                                                        <Td align="left" valign="top">
                                                            <a  href="javascript:void(0)" onclick="graphTypesDisp(document.getElementById('dispgrptypes<%=jqgraphIds[grpCnt]%>'),'jq');" style='text-decoration:none' class="calcTitle" title="Graph Types">Types2 </a>
                                            </Td>
                                                    </tr>
                                                    <tr valign="top">
                                                        <td valign="top">
                                                                                                                   
                                                            <div class="flagDiv" style="display:none"  id="dispgrptypes<%=jqgraphIds[grpCnt]%>">
                                                                <Table>
                                                                    <%for (gqtype = 0; gqtype < jqgrapharray.length; gqtype++) {%>
                                                                
                                                                           
                                                                    <Tr>
                                                                      
                                                                        <Td id="<%=gqtype%>"><a href='javascript:void(0)' onclick='buildJqGraph("<%=jqgrapharray[gqtype]%>","<%=graphIds[grpCnt]%>","<%=jqgraphIds[gqtype]%>","<%=reportId%>","<%=grpCnt%>",")'><%=jqgrapharray[gqtype]%></a></Td>
                                                                    </Tr>
                                                                  
                                                                    
                                                                         <%}%>
                                                                </Table>
                                                            </div>
                                                        </td>
                                                    </tr>
                                                </table>
                                            </Td>-->
                                            <%//}
                                            //        if(PrivilegeManager.isComponentEnabledForUser("REPORT","GRAPHCOLS",Integer.parseInt(PbUserId))){
                                                %>
<!--                                            <Td valign="top"  align="left"><img src=<%=c_img_separator%> style="border:0"  alt="Separator" /></Td>
                                            <Td valign="top"  align="left">
                                                <table>
                                                    <tr valign="top" >
                                                        <Td align="left" valign="top" > <a  href="javascript:void(0)" onclick="dispChangeGrpColumns('<%=request.getContextPath()%>','<%=sbufGrpIds.toString().substring(1)%>','<%=graphIds[grpCnt]%>','<%=sbufRoles.toString().substring(1)%>','<%=reportId%>')" style='text-decoration:none' class="calcTitle" title="Graph columns">Columns</a></Td>
                                                    </tr>
                                                </table>
                                            </Td>-->
                                            <%//}
                                               //     if(PrivilegeManager.isComponentEnabledForUser("REPORT","ROWS",Integer.parseInt(PbUserId))){
//                                                 if(!selectedgraphtype1.equalsIgnoreCase("null") && selectedgraphtype1!=null && selectedgraphtype1.equalsIgnoreCase("jq")) {
//                                                     {%>
                                                       
                                            <!--<Td valign="top"  align="left"><img src=<%=c_img_separator%> style="border:0"  alt="Separator" /></Td>-->
                                         
                                            <Td valign="top"  align="left">
                                                <div id="columnpie<%=grpCnt%>" style="display: none">
                                                <table  border='0' align="left">
                                                    <tr valign="top" >
                                                        <Td valign="top"  align="left">
                                                            &nbsp;<a  href="javascript:void(0)" onclick="dispRowValues1('<%=selectedgraphtype1%>','<%=graphTypeid%>','ColumnPie','dispRowValues<%=grpCnt%>','rowValues<%=grpCnt%>','<%=graphIds[grpCnt]%>','<%=reportId%>','<%=grpCnt%>');" style='text-decoration:none' class="calcTitle" title="Column Pie rows ">Rows</a>
                                                        </Td>
                                                    </tr>
                                                    <tr valign="top" >
                                                        <Td valign="top"  align="left">
                                                            <div class="flagDiv"  style="display:none"  id="dispRowValues<%=grpCnt%>">
                                                                <Table>
                                                                    <%String columnName = null;
                                                                                String dataType = null;
                                                                                StringBuffer RowValuesbuffer = null;
                                                                                String rowcount =null;
                                                                                    if(singleGraphDetails.get("graphDisplayRows")!=null){
                                                                                       rowcount = singleGraphDetails.get("graphDisplayRows").toString();
                                                                                    }
                                                                                    int actualrowcount=recordsRetObj.getRowCount();
                                                                                    int graphrowcount=10;
                                                                                    if (rowcount != null && !rowcount.equalsIgnoreCase("null") && !rowcount.isEmpty() && !rowcount.equalsIgnoreCase("")) {
                                                                                        if (rowcount.equalsIgnoreCase("All") || actualrowcount < Integer.parseInt(rowcount)) {
                                                                                            graphrowcount = actualrowcount;
                                                                                        } else {
                                                                                            graphrowcount = Integer.parseInt(rowcount);
                                                                                        }
                                                                                    }else{
                                                                                        if(actualrowcount < graphrowcount){
                                                                                        graphrowcount = actualrowcount;
                                                                                        }
                                                                                    }
                                                                                for (int rowCnt = 0; rowCnt < graphrowcount && rowCnt<recordsRetObj.getViewSequence().size(); rowCnt++) {
//                                                                                for (int rowCnt = 0; rowCnt < graphrowcount && rowCnt < recordsRetObj.getViewSequence().size(); rowCnt++) {
                                                                                    RowValuesbuffer = new StringBuffer("");

                                                                                    for (int j = 0; j < originalCols.size(); j++) {

                                                                                        columnName = String.valueOf(originalCols.get(j));

                                                                                        if(j<dataTypes.size())
                                                                                            dataType = String.valueOf(dataTypes.get(j));
                                                                                        else
                                                                                            dataType = "N";

                                                                                        if (dataType.equalsIgnoreCase("C") || (dataTypes.get(0).toString().equalsIgnoreCase("N") && j==0)) {
                                                                                            if (RowValues.size() == 0) {
                                                                                                RowValues.add(recordsRetObj.getFieldValueString(recordsRetObj.getViewSequence().get(rowCnt), (String) columnName));
                                                                                               
                                                                                            }
                                                                                              RowValuesbuffer.append("-" + recordsRetObj.getFieldValueString(recordsRetObj.getViewSequence().get(rowCnt), (String) columnName) );
                                                                                        }
                                                                                    }%>
                                                                    <Tr valign="top" >
                                                                        <Td valign="top"  align="left">
                                                                            <%if (RowValues!=null && !RowValues.isEmpty() && RowValues.contains(RowValuesbuffer.toString().substring(1))) {%>
                                                                            <input type="checkbox" checked value="<%=RowValuesbuffer.toString().substring(1)%>" id="rowValues<%=rowCnt%>" name="rowValues<%=grpCnt%>"><%=RowValuesbuffer.toString().substring(1)%>
                                                                            <%} else if (RowValues!=null && !RowValues.isEmpty()) {%>
                                                                            <input type="checkbox" value="<%=RowValuesbuffer.toString().substring(1)%>" id="rowValues<%=rowCnt%>" name="rowValues<%=grpCnt%>"><%=RowValuesbuffer.toString().substring(1)%>
                                                                            <%}%>
                                                                        </Td>
                                                                    </Tr>
                                                                    <%RowValuesbuffer = null;
                                                                                }%>
                                                                </Table>
                                                            </div>

                                                    </tr>
                                                </table>
                                                                  </div>
                                            </Td>


                                           
<!--                                               // }
                                                }
                                                //    if(PrivilegeManager.isComponentEnabledForUser("REPORT","GRAPHPROPS",Integer.parseInt(PbUserId))){
                                                %>-->
<!--                                            <Td align="left" valign="top" ><img src=<%=c_img_separator%> style="border:0"  alt="Separator" /></Td>
                                            <Td align="left" valign="top" >
                                                <table>
                                                    <tr valign="top" >
                                                        <Td align="left" valign="top" >
                                                            <a  href="javascript:void(0)" onclick="showGrpProperties('<%=reportId%>','<%=graphIds[grpCnt]%>','<%=request.getContextPath()%>','<%=grpTitle%>');" style='text-decoration:none' class="calcTitle" title="Graph Properties">Properties</a>
                                                        </Td>
                                                    </tr>
                                                </table>
                                            </Td>-->
<!--                                              <Td align="left" valign="top" ><img src=<%=c_img_separator%> style="border:0"  alt="Separator" /></Td>
                                              <Td align="left" valign="top" >
                                                <table>
                                                    <tr valign="top" >
                                                        <Td align="left" valign="top" >
                                                            <a  href="javascript:void(0)" onclick="jqgraphSize('<%=reportId%>','<%=graphIds[grpCnt]%>','<%=graphTypename%>','<%=selectedgraphtype1%>','<%=graphTypeid%>')" style='text-decoration:none' class="calcTitle" title="Big Graph">BigGraph</a>
                                            </Td>
                                                    </tr>
                                                </table>
                                            </Td>-->
<!--                                                <Td align="left" valign="top" ><img src=<%=c_img_separator%> style="border:0"  alt="Separator" /></Td>
                                            <Td align="left" valign="top" >
                                                <table>
                                                    <tr valign="top" >
                                                        <Td align="left" valign="top" >
                                                            <a  href="javascript:void(0)" onclick="saveJqGraph('<%=reportId%>','<%=request.getContextPath()%>');" style='text-decoration:none' class="calcTitle" title="Graph Properties">Save Graph</a>
                                                        </Td>
                                                    </tr>
                                                </table>
                                            </Td>-->
                                            <%//}%>
<!--                                            <Td align="left" valign="top" ><img src=<%=c_img_separator%> style="border:0"  alt="Separator" /></Td>
                                            <Td align="left" valign="top" >
                                                <table>
                                                    <tr valign="top" >
                                                        <Td align="left" valign="top" > <a  href="javascript:void(0)" onclick="previousRows('<%=graphIds[grpCnt]%>','<%=reportId%>','Transpose')"  style='text-decoration:none' class="calcTitle" title="Transpose">Transpose</a></Td>
                                                    </tr>
                                                </table>
                                            </Td>-->
                                            <%
                                                if (!graphType.equalsIgnoreCase("ColumnPie3D") && !graphType.equalsIgnoreCase("ColumnPie") && !graphType.equalsIgnoreCase("Spider") && !graphType.trim().equalsIgnoreCase("TimeSeries")) {

                                            %>



                                            <%--<Td align="left" valign="top" ><img src=<%=c_img_separator%> style="border:0"  alt="Separator" /></Td>--%>
<!--                                            <Td align="right" valign="top" width="150%" >
                                                <table>
                                                    <tr valign="top" align="right">
                                                        <Td ALIGN="left" valign="top"><Img src="<%=request.getContextPath()%>/tablesorter/addons/pager/icons/first.png" id="FIRST" name="FIRST" style="cursor:pointer;cursor:hand" onclick="previousRows('<%=graphIds[grpCnt]%>','<%=reportId%>','firstrecord')" title="First Records" />&nbsp;&nbsp;</Td>
                                                        <Td ALIGN="left" valign="top"><Img src="<%=request.getContextPath()%>/tablesorter/addons/pager/icons/prev.png" id="UP" name="UP" style="cursor:pointer;cursor:hand" onclick="previousRows('<%=graphIds[grpCnt]%>','<%=reportId%>','prevrecord')" title="Previous Records" />&nbsp;&nbsp;</Td>
                                                        <Td ALIGN="left" valign="top">&nbsp;&nbsp;</Td>
                                                        <Td ALIGN="left" valign="top"><Img src="<%=request.getContextPath()%>/tablesorter/addons/pager/icons/next.png" id="DOWN" name="DOWN" style="cursor:pointer;cursor:hand" onclick="nextRows('<%=graphIds[grpCnt]%>','<%=reportId%>','nextrecord')" title="Next Records" />&nbsp;&nbsp;</Td>
                                                        <Td ALIGN="left" valign="top"><Img src="<%=request.getContextPath()%>/tablesorter/addons/pager/icons/last.png" id="LAST" name="LAST" style="cursor:pointer;cursor:hand" onclick="nextRows('<%=graphIds[grpCnt]%>','<%=reportId%>','lastrecord')" title="Last Records" />&nbsp;&nbsp;</Td>
                                                    </tr>
                                                </table>
                                            </Td>-->


                                            <%}%>







                                        </Tr>
                                    </Table>
                                </Td>
                                <Td >&nbsp;</Td>
                            </Tr>
                        
                            <Tr valign="top">
                                
                                <Td COLSPAN="2" valign="top" >
                                    <%if (container.getRetObj() != null && container.getRetObj().getRowCount() != 0) {
                                            if (isFxCharts) {%>
                                    <div align="left" class="appletDiv" >
                                        <script>
                                            loadJavaFXScript('applet_<%=reportId%>_<%=graphIds[grpCnt]%>','<%=graphType%>','<%=sbuffer.toString()%>','Normal','<%=graphHeight%>', '<%=Integer.parseInt(graphWidth)%>');
                                        </script>
                                    </div>
                                    <%--<div class="zoom"   style="width:100%;height:auto;z-index:1000;"  id="zoom-<%=graphIds[grpCnt]%>"    align="center">
                                        <table>
                                            <tr valign="top" >
                                                <td valign="top" valign="top" >
                                                    <script>
                                                        loadJavaFXScript('applet1_<%=reportId%>_<%=graphIds[grpCnt]%>','<%=graphType%>','<%=sbuffer.toString()%>','Zoom','<%=graphHeight%>', '<%=Integer.parseInt(graphWidth) * 2%>');
                                                    </script>
                                                </td>
                                                <td  valign="top">
                                                    <Img src="<%=request.getContextPath()%>/TableDisplay/Images/sign_cancel.png"  style="cursor:pointer;cursor:hand" onClick="closeZoomImg('zoom-<%=graphIds[grpCnt]%>');" title="close" />
                                                </td>
                                            </tr>
                                        </table>
                                    </div>--%>
                                    <%}
            else{%>
            <%            
                                                                           
              
 if(!selectedgraphtype1.equalsIgnoreCase("null") && selectedgraphtype1!=null && selectedgraphtype1.equalsIgnoreCase("jq")) {%>      
                             
                                    <%if(grprgbColorCode!=null && grprgbColorCode.length>0){%>
                                    <div id="jfchartid<%=grpCnt%>" style="width:100%;height:auto;background-color:rgb(<%=Integer.parseInt(grprgbColorCode[0].trim())%>,<%=Integer.parseInt(grprgbColorCode[1].trim())%>,<%=Integer.parseInt(grprgbColorCode[2].trim())%>)" align="center">
                                    <%} else{%>
                                    <%if(grprgbColorCode1!=null && grprgbColorCode1.length>0){%>
                                    <div style="width:100%;height:auto;background-color:rgb(<%=Integer.parseInt(grprgbColorCode1[0].trim())%>,<%=Integer.parseInt(grprgbColorCode1[1].trim())%>,<%=Integer.parseInt(grprgbColorCode1[2].trim())%>)" id="jfchartid<%=grpCnt%>" align="center">                                        
                                         <%}else{%>
                                         <div style="width:100%;height:auto;" id="jfchartid<%=grpCnt%>" align="center">                                        
                                    <%}%>
                                          <%}%>
                <iframe class=frame1 frameBorder='0' STYLE='width:100%;left:100;margin-top: -20; overflow:auto;height:500px' src="<%=request.getContextPath()%>/TableDisplay/progenJqplotGraphBulider.jsp?REPORTID=<%=reportId%>&grptypid=<%=graphTypename%>&selectedgraph=<%=selectedgraphtype1%>&gid=<%=graphTypeid%>&grpidfrmrep=<%=graphIds[grpCnt]%>&graphCount=<%=grpCnt%>&graphChange=default"></iframe>
                </div>
                <%}else if(selectedgraphtype1.equalsIgnoreCase("null") ||selectedgraphtype1.equalsIgnoreCase("jf")){%>
                <%if(grprgbColorCode!=null && grprgbColorCode.length>0){%>
                                    <div id="jfchartid<%=grpCnt%>" style="width:100%;height:auto;background-color:rgb(<%=Integer.parseInt(grprgbColorCode[0].trim())%>,<%=Integer.parseInt(grprgbColorCode[1].trim())%>,<%=Integer.parseInt(grprgbColorCode[2].trim())%>)" align="center"><%=pcharts[grpCnt].chartDisplay%>
                                    <%} else{%>
                                    <%if(grprgbColorCode1!=null && grprgbColorCode1.length>0){%>
                                    <div style="width:100%;height:auto;background-color:rgb(<%=Integer.parseInt(grprgbColorCode1[0].trim())%>,<%=Integer.parseInt(grprgbColorCode1[1].trim())%>,<%=Integer.parseInt(grprgbColorCode1[2].trim())%>)" id="jfchartid<%=grpCnt%>" align="center">
                                        <%--<%=pcharts[grpCnt].chartDisplay%>--%>
                                        <%}else{%>
                                        <div style="width:100%;height:auto;" id="jfchartid<%=grpCnt%>" align="center">
                                        <!--added by srikanth.p to convert old jfree charts to jqplot-start-->
                                            <%
                                                HashMap jqpToJfNameMap=(HashMap)request.getSession(false).getAttribute("JqpToJfNameMap");
                                                HashMap jqpMap=(HashMap)request.getSession(false).getAttribute("JqpMap");
                                                String jqGraphTypeName=(String) jqpToJfNameMap.get(graphType);
                                                String jqgraphId=(String)jqpMap.get(jqGraphTypeName);
                                                if(jqGraphTypeName==null || jqGraphTypeName.isEmpty()){
                                                    jqGraphTypeName=graphType;
                                                }
                                               // 
                                            %>
                                            <iframe class=frame1 frameBorder='0' STYLE='width:100%;left:100; overflow:auto;height:500px' src="<%=request.getContextPath()%>/TableDisplay/progenJqplotGraphBulider.jsp?REPORTID=<%=reportId%>&grptypid=<%=jqGraphTypeName%>&selectedgraph=<%="jq"%>&gid=<%=jqgraphId%>&grpidfrmrep=<%=graphIds[grpCnt]%>&graphChange=default"></iframe>
                                            <%--<%=pcharts[grpCnt].chartDisplay%>--%>
                                        <!--ended here by srikanth.p-->
                                        <%}%>
                                        <%}%>
                                         <%}%>
                                    </div>
                               
                                    <%--<div style="width:100%;height:auto;"  class="zoom" id="zoom-<%=graphIds[grpCnt]%>"    align="center">
                                        <table>
                                            <tr valign="top" >
                                                <td valign="top"><%=//pchartsZoom[grpCnt].chartDisplay%></td>
                                                <td  valign="top">
                                                    <Img src="<%=request.getContextPath()%>/TableDisplay/Images/sign_cancel.png"  style="cursor:pointer;cursor:hand" onClick="closeZoomImg('zoom-<%=graphIds[grpCnt]%>');" title="close" />
                                                </td>
                                            </tr>
                                        </table>
                                    </div>--%>
                                    <%}
                                    } else {%>
                                    <div align="left"  class="appletDiv">
                                         <script  type="text/javascript">
                                         closeloading();
                                          </script>
                                        <table align="center" style="width:100%;height:100%">
                                            <tr align="center" valign="middle">
                                                <td align="center" valign="middle"> No data to display</td>
                                            </tr>
                                        </table>
                                    </div>
                                    <%}%>
                                </Td>
                            </Tr>
                        </Table>
                    </Td>
                    <%}%>
                </Tr>

                <%for (int i = 0; i < grapVector.length; i++) {
                                                Vector grpList = grapVector[i];
                                                String temp = "";
                                                for (int m = 0; m < grpList.size(); m++) {
                                                    temp = temp + "," + grpList.get(m);
                                                }
                                                if (!(temp.equalsIgnoreCase(""))) {
                                                    temp = temp.substring(1);
                                                }%>
                <input type="hidden" id="grpcollist<%=i%>" name="grpcollist<%=i%>"value="<%=temp%>" />
                <input type="hidden" id="presCollist" name="presCollist"value="" />
                <%}%>

                <%request.getSession(false).setAttribute("grapVector", grapVector);
                                        }
                                    }%>
            </Table></div>
            <input type="hidden" id="showTable" name="showTable" value="<%=showTable%>">
            <div id="fade" class="black_overlap"></div>
            <%} catch (Exception exp) {
                                exp.printStackTrace();
                                exp.getMessage();
            %>
            <%--<script type="text/javascript">
                alert("Exception occurred...")
                parent.document.forms.form[0].action="<%=request.getContextPath()%>/newpbLogin.jsp";
                parent.document.forms.form[0].submit();
            </script>--%>
            <%}
                        } else {
                            response.sendRedirect("baseAction.do?param=logoutApplication");
                        }%>

            <div id="editGraphTitle" title="Edit Graph Title"  style="display:none">
                <input type="hidden" name="graphId" id="graphId" value="">
                <table width="100%" align="center">
                    <tr>
                        <td>GraphTitle :</td>
                        <td><input type="text" name="grpTitle" style="font:11px verdana;background-color:white" id="grpTitle" value=""></td>
                    </tr>
                    <tr>
                        <td colspan="2" style="width:10px">&nbsp;</td>
                    </tr>
                    <tr>
                        <td colspan="2" align="center"><input type="button" value="Done" class="navtitle-hover" onclick="updateGraphTitle();"></td>
                    </tr>
                </table>
            </div>
            <%--List of Graphs display Div--%>
            <div id="graphList" title="Add Graphs"  style="display:none">
                <table>
                    <tbody>
                        <%

                        if (grphIds != null && !grphIds.equalsIgnoreCase("") && grphIds.split(",").length < 2) {
                                    ProGenChartUtilities utilities = new ProGenChartUtilities();
                                    String tabSeqName;
                                    int sequence;
                                     if(ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER))
                                     {
                                        tabSeqName = "-100";//IDENT_CURRENT('PRG_AR_GRAPH_MASTER')";
                                        sequence = -100;
                                     }else if(ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL))
                                     {
                                        //tabSeqName = "select LAST_INSERT_ID('GRAPH_ID') from PRG_AR_GRAPH_MASTER order by 1 desc limit 1";
                                       tabSeqName = "-100";//IDENT_CURRENT('PRG_AR_GRAPH_MASTER')";
                                        sequence = -100;
                                     }
                                     else
                                     {
                                        tabSeqName = "PRG_AR_GRAPH_MASTER_SEQ";
                                        sequence =  DAO.getSequence(tabSeqName);
                                     }

                                    String  str = utilities.buildAddMoreGraphTypesDiv(request.getContextPath(), grpTypeskeys, GraphTypesHashMap, "addMoreGraphs('" + reportId + "','" + grphIds + "','¥','" + request.getContextPath() + "','" + sequence + "')");
                                    ////.println("str"+str);
                                    out.print(str);
                           }
                        %>
                    </tbody>
                </table>
            </div>
        </form>
                     <div id="testForId" class="jqplot-image-container-content"  style="display:none; z-index: 500"  title="Right Click and Save as Image">

                    </div> 
    </body>
</html>
