<%@page contentType="text/html" pageEncoding="UTF-8" import="java.util.List,com.progen.i18n.TranslaterHelper,java.util.Locale,java.util.ArrayList,java.util.HashMap,prg.db.Session,prg.db.Container"%>
<%@page import="com.progen.reportview.bd.PbReportViewerBD,com.progen.reportview.db.PbReportViewerDAO,com.progen.report.PbReportCollection,com.progen.report.PbReportRequestParameter"%>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<%--
    Document   : ChangeViewBy
    Created on : Jun 14, 2010, 8:27:04 PM
    Author     : Administrator
--%>

<%//for clearing cache
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
            String newone="";
%>
<html>
    <%
            try {
                //added by Mohit Gupta for default locale
                Locale cL=null;
                cL=(Locale)session.getAttribute("UserLocaleFormat");
                //ended By Mohit Gupta
                PbReportViewerBD reportViewBD = new PbReportViewerBD();
                PbReportViewerDAO reportViewDAO = new PbReportViewerDAO();
                List<String> allViewIds = null;
                List<String> allViewNames = null;
                List<String> rowViewIdList = null;
                List<String> rowMeasIdList = null;
                List<String> colViewIdList = null;
                List<String> rowNamesLst = null;
                List<String> rowMeasNamesLst = null;
                List<String> colNamesLst = null;
                List<String> aggTypes = null ;
                String rowName = "";
                String colName = "";
                String rowViewListSTR = "";
                String rowViewNamesListSTR = "";
                String rowMeasListSTR = "";
                String rowMeasNamesListSTR = "";
                String colViewListSTR = "";
                String colViewNamesListSTR = "";
                String parentViewBy = "";
                String childViewBys = "";
                String childMeasBys = "";
                String aggTypeString = "";
                 String imgpathRow = request.getContextPath() + "/icons pinvoke/arrow-curve.png";
                String imgpathCol = request.getContextPath() + "/icons pinvoke/arrow-curve-270.png";
                String imgpath = request.getContextPath() + "/icons pinvoke/tick-small.png";
                String delimgpath = request.getContextPath() + "/icons pinvoke/cross-small.png";
                String reportId = request.getParameter("REPORTID");
                String ctxPath = request.getParameter("ctxPath");
                String themeColor="blue";
                String graphFlag = "advance";
                if (session.getAttribute("allViewIds") != null) {
                    allViewIds = (List<String>) session.getAttribute("allViewIds");
                } else {
                    allViewIds = new ArrayList<String>();
                }
                if (session.getAttribute("allViewNames") != null) {
                    allViewNames = (List<String>) session.getAttribute("allViewNames");
                } else {
                    allViewNames = new ArrayList<String>();
                }
                Container container = null;
                container = Container.getContainerFromSession(request, reportId);
                parentViewBy = reportViewDAO.buildParentViewByForMultiDB(reportId,"RowViewBy", ctxPath,container,"Local",graphFlag);
                childViewBys = reportViewDAO.buildChildViewByForMultiDB(reportId,"ColViewBy", ctxPath,container,"Local",graphFlag);
                childMeasBys = reportViewDAO.buildMeasBysForMultiDB(reportId,"ColViewBy", ctxPath,container,"Local",graphFlag);
//                if (session.getAttribute("rowViewIdList") != null) {
//                    rowViewIdList = (List<String>) session.getAttribute("rowViewIdList");
//                    rowNamesLst = (List<String>) session.getAttribute("rowNamesLst");
//                    rowMeasIdList = (List<String>) session.getAttribute("rowMeasIdList");
//                    rowMeasNamesLst = (List<String>) session.getAttribute("rowMeasNamesLst");
//                    Container container = null;
//                    container = Container.getContainerFromSession(request, reportId);
//                    if(request.getParameter("from")!=null && request.getParameter("from")!="" && request.getParameter("from").equalsIgnoreCase("viewer")){
////                        rowViewByStr = reportViewDAO.buildDragableViewBy(reportId,"RowViewBy", ctxPath,container,"Local",graphFlag);
////                        colViewByStr = reportViewDAO.buildDragableMeasBy(reportId,"ColViewBy", ctxPath,container,"Local",graphFlag);
//                    }
//                    else if(request.getParameter("from")!=null && request.getParameter("from")!="" && request.getParameter("from").equalsIgnoreCase("isEdit")){
//                   parentViewBy = reportViewDAO.buildParentViewByForMultiDB(reportId,"RowViewBy", ctxPath,container,"Local",graphFlag);
//                   childViewBys = reportViewDAO.buildChildViewByForMultiDB(reportId,"ColViewBy", ctxPath,container,"Local",graphFlag);
//                    }
//                    else{
////                    rowViewByStr = reportViewDAO.buildDragableViewByObject(reportId,container,"RowViewBy",ctxPath);
////                    colViewByStr = reportViewDAO.buildDragableMeasByObject(reportId,container,"ColViewBy", ctxPath);
//                    }
//                } else {
//                    rowViewIdList = new ArrayList<String>();
//                    rowMeasIdList = new ArrayList<String>();
//                    rowNamesLst = new ArrayList<String>();
//                    rowMeasNamesLst = new ArrayList<String>();
//                }
//                if (session.getAttribute("colViewIdList") != null) {
//                    colViewIdList = (ArrayList<String>) session.getAttribute("colViewIdList");
//                    colNamesLst = (ArrayList<String>) session.getAttribute("colNamesLst");
//                    colViewByStr = reportViewDAO.buildChangeViewBy(colViewIdList, colNamesLst, "ColViewBy", ctxPath);
//
//                } else {
                    colViewIdList = new ArrayList<String>();
                    colNamesLst = new ArrayList<String>();
//                }
                if (session.getAttribute("aggType") !=null) {
                    aggTypes = (List<String>) session.getAttribute("aggType");
                } else {
                    aggTypes = new ArrayList();
                }
                 if(session.getAttribute("theme")==null)
                session.setAttribute("theme",themeColor);
            else
                themeColor=String.valueOf(session.getAttribute("theme"));
                    String contextPath=request.getContextPath();


    %>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>piEE</title>

      <script src="<%=contextPath%>/javascript/lib/jquery/js/jquery-1.7.2.min.js" type="text/javascript"></script>
<!--         <script type="text/javascript" src="<%=contextPath%>/javascript/lib/jquery/js/jquery-1.3.2.min.js"></script>-->
<!--        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>-->
        <script type="text/javascript" src="<%=contextPath%>/javascript/lib/jquery/js/jquery-1.8.23-custom.min.js"></script>
<!--         <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/lib/jquery/js/jquery-1.4.2.min.js"></script>-->
         <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=themeColor%>/ReportCss.css" rel="stylesheet" />
        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/treeviewstyle/jquery.treeview.css" />
        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/treeviewstyle/screen.css" />
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=themeColor%>/metadataButton.css" rel="stylesheet" />


<!--        <script src="<%=request.getContextPath()%>/javascript/draggable/jquery-1.3.2.js" type="text/javascript"></script>-->

        <script src="<%=contextPath%>/javascript/treeview/jquery.cookie.js" type="text/javascript"></script>
        <script src="<%=contextPath%>/javascript/treeview/jquery.treeview.js" type="text/javascript"></script>
        <%--<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/treeview/demo.js"></script>--%>
<!--        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/draggable/ui.core.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/draggable/ui.draggable.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/draggable/ui.droppable.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/ui.sortable.js"></script>-->
        <script type="text/javascript" src="<%=contextPath%>/javascript/quicksearch.js"></script>

        <style type="text/css">
            *{font:11px verdana;}
        </style>
        
    </head>
    <body>
        <script type="text/javascript">
            var parentViewBy='';
            var childViewBys=[];
            var childMeasBys=[];
            var ViewByArray=new Array();
            var MeasByArray=new Array();
            <% if (rowMeasNamesLst != null && rowMeasNamesLst.size() != 0) {
                 for(int i = 0; i < rowMeasNamesLst.size(); i++) {
                     if(aggTypeString == ""){
                         aggTypeString = aggTypes.get(0);
                     }else {
                         aggTypeString += "," + aggTypes.get(i);
                     }
                 }
            %>
                var aggTypeList = '<%=aggTypeString %>';
                var aggTypeListName = aggTypeList.split(",");
                <% } else { %>
                    var aggTypeList = new Array();
                    var aggTypeListName = new Array();
                    <% } %>

            <%if (rowViewIdList != null && rowViewIdList.size() != 0) {
                    for (int i = 0; i < rowViewIdList.size(); i++) {
                        rowViewListSTR += "," + rowViewIdList.get(i);
                        rowViewNamesListSTR += "," + rowNamesLst.get(i);
                    }
                    rowViewListSTR = rowViewListSTR.trim().substring(1);
                    rowViewNamesListSTR = rowViewNamesListSTR.trim().substring(1);
            %>
                var rowViewStr = '<%=rowViewListSTR%>';
                var rowNamesStr = '<%=rowViewNamesListSTR%>';
                var RowViewByArray  = rowViewStr.split(",");
                var rowViewNamesArr = [];//rowNamesStr.split(",");

                ViewByArray = rowViewStr.split(",");
            <%
} else {%>
    var RowViewByArray=new Array();
    var rowViewNamesArr=new Array();

            <%}%>
            <%if (rowViewIdList != null && rowViewIdList.size() != 0 && rowMeasIdList !=null && rowMeasIdList.size() !=0) {
                    for (int i = 0; i < rowMeasIdList.size(); i++) {
                        rowMeasListSTR += "," + rowMeasIdList.get(i);
                        rowMeasNamesListSTR += "," + rowMeasNamesLst.get(i);
                    }
                    rowMeasListSTR = rowMeasListSTR.trim().substring(1);
                    rowMeasNamesListSTR = rowMeasNamesListSTR.trim().substring(1);
            %>
                var rowMeasStr = '<%=rowMeasListSTR%>';
                var rowMeasNamesStr = '<%=rowMeasNamesListSTR%>';
                var RowMeasArray  = rowMeasStr.split(",");
                var rowMeasNamesArr = rowMeasNamesStr.split(",");
                var dropMeas = new Array();
                 var MeasBy=[];
                 var AggType=[];
                MeasByArray = rowMeasStr.split(",");
            <%
} else {%>
    var RowMeasArray=new Array();
    var rowMeasNamesArr=new Array();

            <%}%>
            <%if (colViewIdList != null && colViewIdList.size() != 0) {
                    for (int i = 0; i < colViewIdList.size(); i++) {
                        colViewListSTR += "," + colViewIdList.get(i);
                        colViewNamesListSTR += "," + colNamesLst.get(i);
                    }
                    colViewListSTR = colViewListSTR.substring(1);
                    colViewNamesListSTR = colViewNamesListSTR.substring(1);
            %>
                var colViewStr = '<%=colViewListSTR%>';
                var colNamesStr = '<%=colViewNamesListSTR%>'
                var ColViewByArray  =  colViewStr.split(",");
                var colViewNamesArr = colNamesStr.split(",");
                ViewByArray = colViewStr.split(",");
                var colFlag = 1;
            <%
} else {%>
    var ColViewByArray=new Array();
    var colViewNamesArr=new Array();
    var colFlag = 0;
            <%}%>
                $(document).ready(function() {
                       <%if(request.getParameter("from")!=null && request.getParameter("from").equalsIgnoreCase("isedit")) {%>
var visualName = parent.$("#currType").val();
document.getElementById("visualName").value=visualName;
var visualChartType = {};
                    if(typeof parent.$("#visualChartType").val()!="undefined" && parent.$("#visualChartType").val()!=""){
                        visualChartType = JSON.parse(parent.$("#visualChartType").val());
                    }
                    $("#chartType").val(visualChartType[visualName]);
<% } %>
//                    var v=parent.document.getElementById("Designer").value;
//                   // /
//                  if(parent.document.getElementById("Designer").value=="fromDesigner"){
//                        // <//%rowViewByStr="";colViewByStr=""; %>
//                    }
                    $("#ViewByList").treeview({
                        animated:"slow"
//                        persist: "cookie"
                    });
                    $(".sortable").sortable();
                    $(".sortable").disableSelection();
//                    $("#Rowdrop").sortable();
//                    $('ul#ViewByList li').quicksearch({
//                        position: 'before',
//                        attached: 'ul#ViewByList',
//                        loaderText: '',
//                        delay: 100
//                    })
                    $(".viewBys").draggable({
                        helper:"clone",
                        effect:["", "fade"]
                    });
                    var viewByCounter=0;
                    $("#RowViewBy").droppable({
                        activeClass:"blueBorder",
                         accept:function(d){return true;},
                         drop: function(ev, ui) {
                             //Code added by mohit for drag and drop in rows and cols
                             //changed by sruthi for drag and drop in rows and rows
                        var ul1 = document.getElementById("rowViewUL");      
                        var colIds1=ul1.getElementsByTagName("li");
                        if(colIds1.length!=0){
                            alert("Parent can only have single View By");
                            return;
                        }
                        var parentid=  $("#"+ui.draggable.attr('id')).parent().attr('id');//added by sruthi
                        if(parentid=="rowViewUL")//added by sruthi
                            {
                            }
                        else
                            {
                          if(ui.draggable.attr('id').substring(0,6)=="ViewBy")
                            {
                           var oldid=ui.draggable.attr('id');
                           deleteColumn(ui.draggable.attr('id'),'ColViewBy',ui.draggable.html());
                           var newid=ui.draggable.attr('id').substring(ui.draggable.attr('id').lastIndexOf("y")+1);
                           var str=ui.draggable.html().substring(0,ui.draggable.html().lastIndexOf("</td>"));
                           var name=str.substring(str.lastIndexOf(">")+1);
                           createViewBY(newid,name,"rowViewUL");
                                }
                                else
                                    {

                                        createViewBY(ui.draggable.attr('id'),ui.draggable.html(),"rowViewUL");
                                    }
                            }}
                    });
                    
                    $("#RowMeasBy").droppable({
                        activeClass:"blueBorder",
                         accept:function(d){return true;},
                         drop: function(ev, ui) {
                             //Code added by mohit for drag and drop in rows and cols
                             //changed by sruthi for drag and drop in rows and rows
                       var parentid=  $("#"+ui.draggable.attr('id')).parent().attr('id');//added by sruthi
                        if(parentid=="rowMeasUL")//added by sruthi
                            {
                            }
                        else
                            {
                          if(ui.draggable.attr('id').substring(0,6)=="MeasBy")
                            {
                           var oldid=ui.draggable.attr('id');
                           deleteColumn(ui.draggable.attr('id'),'ColViewBy',ui.draggable.html());
                           var newid=ui.draggable.attr('id').substring(ui.draggable.attr('id').lastIndexOf("y")+1);
                           var str=ui.draggable.html().substring(0,ui.draggable.html().lastIndexOf("</td>"));
                           var name=str.substring(str.lastIndexOf(">")+1);
                           createViewBY(newid,name,"rowMeasUL");
                                }
                                else
                                    {
                                        createViewBY(ui.draggable.attr('id'),ui.draggable.html(),"rowMeasUL");
                                    }
                            }}
                    });
                    $("#RowMeasBy1").droppable({
                        activeClass:"blueBorder",
                         accept:function(d){return true;},
                         drop: function(ev, ui) {
                             //Code added by mohit for drag and drop in rows and cols
                             //changed by sruthi for drag and drop in rows and rows
                       var parentid=  $("#"+ui.draggable.attr('id')).parent().attr('id');//added by sruthi
                        if(parentid=="rowMeasUL1")//added by sruthi
                            {
                            }
                        else
                            {
                          if(ui.draggable.attr('id').substring(0,6)=="MeasBy")
                            {
                           var oldid=ui.draggable.attr('id');
                           deleteColumn(ui.draggable.attr('id'),'ColViewBy',ui.draggable.html());
                           var newid=ui.draggable.attr('id').substring(ui.draggable.attr('id').lastIndexOf("y")+1);
                           var str=ui.draggable.html().substring(0,ui.draggable.html().lastIndexOf("</td>"));
                           var name=str.substring(str.lastIndexOf(">")+1);
                           createViewBY(newid,name,"rowMeasUL1");
                                }
                                else
                                    {
                                        createViewBY(ui.draggable.attr('id'),ui.draggable.html(),"rowMeasUL1");
                                    }
                            }}
                    });
                    $("#ColViewBy").droppable({
                        activeClass:"blueBorder",
                       accept:function(d){return true;},
                        drop: function(ev, ui) {
                            //Code added by mohit for drag and drop in rows and cols
                            //changed by sruthi for drag and drop in rows to rows
                             var parentid=  $("#"+ui.draggable.attr('id')).parent().attr('id');//added by sruthi
                             if(parentid=="colViewUL")//added by sruthi
                            {
                            }
                            else
                                {
                          if(ui.draggable.attr('id').substring(0,6)=="ViewBy")
                          {
                           var oldid=ui.draggable.attr('id');
                           deleteColumn(ui.draggable.attr('id'),'RowViewBy',ui.draggable.html());
                           var newid=ui.draggable.attr('id').substring(ui.draggable.attr('id').lastIndexOf("y")+1);
                           var str=ui.draggable.html().substring(0,ui.draggable.html().lastIndexOf("</td>"));
                           var name=str.substring(str.lastIndexOf(">")+1);
//                           createViewBY(newid,name,"colViewUL");
                                }
                                else
                                    {
//                                        createViewBY(ui.draggable.attr('id'),ui.draggable.html(),"colViewUL");
                                    }
                        }}
                    });
                });


                function saveEditChart(){
                 var reportId  = document.getElementById("reportId").value;
                    var ctxpath = document.getElementById("ctxPath").value;
                    var viewByArray=new Array();
                    var rowViewByArray=new Array();
                     var ul = document.getElementById("rowViewUL");
                    if(ul!=undefined || ul!=null){
                    var colIds=ul.getElementsByTagName("li");
                    if(colIds!=null && colIds.length!=0){
                        for(var i=0;i<colIds.length;i++){
                            if(i==0){
                                rowViewNamesArr=[];
                            }
                            //cols = cols+","+colIds[i].id.replace("GrpCol","");
                            rowViewByArray.push(colIds[i].id.replace("ViewBy",""));
                            //cols = cols+","+colIds[i].id.replace("GrpCol","");
                            rowViewByArray.push(colIds[i].id.replace("ViewBy",""));
                            viewByArray.push(colIds[i].id.replace("ViewBy",""));
                        }

                    }

                    var chartData = {};
                    for(var i=0;i<rowViewByArray.length;i++){
                        var chartDetails={};
                        var viewBys=[];
                        var meassures=[];
                        var aggregation=[];
                        viewBys.push(rowViewByArray[i]);
                        meassures.push("COUNT");
                        aggregation.push("COUNT");
                        chartDetails["viewBys"] = viewBys;
                            chartDetails["chartType"] = "Pie";
                            chartDetails["viewByLevel"] = "single";
                            chartDetails["meassures"] = meassures;
                            chartDetails["aggregation"] = aggregation;
                            chartDetails["size"] = "S";
                            chartData["chart" + (i + 1)] = chartDetails;
                    }
                    parent.$("#chartData").val(JSON.stringify(chartData));
                    var userId = parent.$("#userId").val();
//                     $("#loading").show();
                    $.ajax({
                     url: ctxpath+"/reportViewer.do?reportBy=addNewChartsUI&rowViewByArray="+rowViewByArray+"&reportId="+reportId+"&userId="+userId+"&chartData="+JSON.stringify(chartData)+"&reportName="+parent.$("#graphName").val(),
                            success: function(data){
//                                 $("#loading").show();
                            }
                    }
                )

                }
                 $("#editViewByDiv").dialog('close')
                 parent.$("#editViewByDiv").dialog('close')
                }
                function saveEditSingleChart(){
                 var reportId  = document.getElementById("reportId").value;
                    var ctxpath = document.getElementById("ctxPath").value;
                    var count=0;
                    var viewByArray=new Array();
                    var rowViewByArray=new Array();
                    var measArray=new Array();
                    var rowMeasArray=new Array();
                    var colViewByArray=new Array();

                     var ul = document.getElementById("rowViewUL");
                    if(ul!=undefined || ul!=null){
                    var colIds=ul.getElementsByTagName("li");
                    
                    if(colIds!=null && colIds.length!=0){
                        for(var i=0;i<colIds.length;i++){
//                            alert(document.getElementById("rowViewUL").childNodes[i].lastIndexOf("</td>"))
//                            if(i==0){
//                                rowViewNamesArr=[];
//                            }
                            //cols = cols+","+colIds[i].id.replace("GrpCol","");
//                            rowViewByArray.push(colIds[i].id.replace("ViewBy",""));
                            //cols = cols+","+colIds[i].id.replace("GrpCol","");
                            rowViewByArray.push(colIds[i].id.replace("ViewBy",""));
                            viewByArray.push(colIds[i].id.replace("ViewBy",""));
                        }

                    }
                     var ul1 = document.getElementById("rowMeasUL");
//                    if(ul1!=undefined || ul!1=null){
                    var colIds1=ul1.getElementsByTagName("li");
                    if(colIds1!=null && colIds1.length!=0){
                        for(var i=0;i<colIds1.length;i++){
                            //cols = cols+","+colIds[i].id.replace("GrpCol","");
//                            if(i==0){
//                                rowMeasNamesArr=[];
//                                dropMeas=[];
//                            }
//                            //cols = cols+","+colIds[i].id.replace("GrpCol","");
//                            rowMeasNamesArr.push(colIds1[i].id.replace("MeasBy",""));
//                            dropMeas.push(colIds1[i].id.replace("MeasBy",""));
                            rowMeasArray.push(colIds1[i].id.replace("MeasBy",""));
                            measArray.push(colIds1[i].id.replace("MeasBy",""));
                        }

                    }
//var chartData = JSON.parse(parent.$("#chartData").val());
//chartData[chartId]["dimensionsIds"] = rowViewByArray;
//chartData[chartId]["dimensionsNames"] = rowViewNamesArr;
//parent.$("#chartData").val(JSON.stringify(chartData));
var chartData = {};
var aggregation=[];
var measVals=[];
for(var j=0;j<MeasByArray.length;j++){
    measVals.push(MeasByArray[j].replace("A_","","gi"));
}
//var arr = rowMeasNamesArr.toString().split(",");
//                    var newArr = [];
//                    for(var k=0;k<arr.length;k++){
//                        if(k!=0){
////                        newArr.push(arr[k]);
//                        aggregation.push("SUM");
//                    }
//                    }
                    MeasBy = [];
                    AggType = [];
                    for( var j=0;j<dropMeas.length;j++){
                    for( var k=0;k<rowMeasNamesArr.length;k++){

                        if(rowMeasNamesArr[k]==dropMeas[j]){
                        MeasBy.push(rowMeasNamesArr[k]);
                        AggType.push(aggTypeListName[k]);
                    }

                    }
                    }

//                    for(var i=0;i<rowViewByArray.length;i++){
//                        var chartDetails={};
//                        var meassures=[];
//                        viewBys.push(rowViewNamesArr[i]);
////                        meassures.push("COUNT");
////                        aggregation.push("SUM");
////                        aggregation.push("SUM");
//                        chartDetails["viewBys"] = viewBys;
//                            chartDetails["chartType"] = "Pie";
//                            chartDetails["viewByLevel"] = "single";
//                            chartDetails["meassures"] = MeasBy;
//                            chartDetails["aggregation"] = AggType;
//                            chartDetails["size"] = "S";
//                            chartData["chart" + (i + 1)] = chartDetails;
//                    }

//                    parent.$("#chartData").val(JSON.stringify(chartData));
                    parent.$("#chartType").val($("#chartType").val());
                    parent.$("#viewby").val(JSON.stringify(rowViewNamesArr));
                    parent.$("#viewbyIds").val(JSON.stringify(rowViewByArray));
                    parent.$("#measure").val(JSON.stringify(rowMeasNamesArr));
                    parent.$("#measureIds").val(JSON.stringify(measVals));
                    parent.$("#aggregation").val(JSON.stringify(AggType));
                    var userId = parent.$("#userId").val();
                     parent.$("#loading").show();
                      $.post(ctxpath+"/reportViewer.do?reportBy=addNewChartsUI&rowViewByArray="+encodeURIComponent(rowViewByArray)+"&reportId="+reportId+"&userId="+userId+"&rowViewNamesArr="+encodeURIComponent(rowViewNamesArr)+"&rowMeasArray="+encodeURIComponent(rowMeasArray)+"&rowMeasNamesArr="+JSON.stringify(encodeURIComponent(MeasBy))+"&reportName="+parent.$("#graphName").val(),parent.$("#graphForm").serialize(),
                             function(data){
                                 parent.$("#loading").hide();
                                 alert("Graph object created");
                            }

                )

                }
                 parent.$("#editViewByDiv").dialog('close');
                }


               function buildcharts(){
                    parentViewBy='';
                    childViewBys=[];
                    childMeasBys=[];
                    var ul1 = document.getElementById("rowViewUL");      
                    var colIds1=ul1.getElementsByTagName("li");
                    parentViewBy=(colIds1[0].id.replace("ViewBy",""));
                    var ul2 = document.getElementById("rowMeasUL");      
                    var colIds2=ul2.getElementsByTagName("li");
                    for(var i=0;i<colIds2.length;i++){
                        childViewBys.push(colIds2[i].id.replace("MeasBy",""));
                    }
                    var ul3 = document.getElementById("rowMeasUL1");      
                    var colIds3=ul3.getElementsByTagName("li");
                    for(var i=0;i<colIds3.length;i++){
                        childMeasBys.push(colIds3[i].id.replace("MeasBy",""));
                    }
                      parent.$("#parentViewBy").val(parentViewBy);
                      parent.$("#childViewBys").val(JSON.stringify(childViewBys));
                      parent.$("#childMeasBys").val(JSON.stringify(childMeasBys));
                      parent.$("#defineParentChildDB").dialog("close");
                      
//                      alert("View bys : "+childViewBys);
//                   alert("Meas by ids : "+childMeasBys);
//                   alert("Agg types : "+childAggType);
                      editAdvance();
                }
                function submitChangeViewBy(){
                    var reportId  = document.getElementById("reportId").value;
                    var ctxpath = document.getElementById("ctxPath").value;
                    parent.document.forms.frmParameter.action = ctxpath+"/reportViewer.do?reportBy=viewReport&action=ChangeViewBy&REPORTID="+reportId
                    parent.document.forms.frmParameter.submit();
                    parent.$("#editViewByDiv").dialog('close');
                }

                function createViewBY(elmntId,elementName,tarLoc){
                    var parentUL=document.getElementById(tarLoc);
                    var x=ViewByArray.toString();
                    //alert(x);
                    if(tarLoc == "colViewUL"){
//                        if(colFlag == 0){
                          if(x.match(elmntId)==null){
                                ViewByArray.push(elmntId);
                                ColViewByArray.push(elmntId);
                                colViewNamesArr.push(elementName);
                                var childLI=document.createElement("li");
                                childLI.id='ViewBy'+elmntId;
                                childLI.style.width='auto';
                                childLI.style.height='auto';
                                childLI.style.color='white';
                                var table=document.createElement("table");
                                table.id="viewTab"+elmntId;
                                var row=table.insertRow(0);
                                var cell1=row.insertCell(0);
                                cell1.style.cursor = "pointer";
                                cell1.onclick =function(){javascript:deleteColumn('ViewBy'+elmntId,'ColViewBy',elementName)};
                                var img=document.createElement("img");
                                img.src = "<%=delimgpath%>"
                                cell1.appendChild(img);
                                var cell2=row.insertCell(1);
                                cell2.style.color='black';
                                cell2.innerHTML=elementName;
                                childLI.appendChild(table);
                                parentUL.appendChild(childLI);
                                colFlag = 1;
                            }
//                        }else{
//                            alert("You Can Select One Column ViewBy Only")
//                        }
                    }else if(tarLoc == "rowViewUL"){
//                      if(x.match(elmntId)==null){
                            ViewByArray.push(elmntId);
                            RowViewByArray.push(elmntId);
                            rowViewNamesArr.push(elementName);
                            var childLI=document.createElement("li");
                            childLI.id='ViewBy'+elmntId;
                            childLI.style.width='auto';
                            childLI.style.height='auto';
                            childLI.style.color='white';
                            var table=document.createElement("table");
                            table.id="viewTab"+elmntId;
                            var row=table.insertRow(0);
                            var cell1=row.insertCell(0);
                            cell1.style.cursor = "pointer";
                            cell1.onclick =function(){javascript:deleteColumn('ViewBy'+elmntId,'RowViewBy',elementName)};
                            var img=document.createElement("img");
                            img.src = "<%=delimgpath%>"
                            cell1.appendChild(img);
                            var cell2=row.insertCell(1);
                            cell2.style.color='black';
                            cell2.innerHTML=elementName;
                            childLI.appendChild(table);
                            parentUL.appendChild(childLI);
//                        }
                    }
                    else if(tarLoc == "rowMeasUL"){
//                      if(x.match(elmntId)==null){
                            MeasByArray.push(elmntId);
                            RowMeasArray.push(elmntId);
//                            dropMeas.push(elementName);
                            var childLI=document.createElement("li");
                            childLI.id='MeasBy'+elmntId;
                            childLI.style.width='auto';
                            childLI.style.height='auto';
                            childLI.style.color='white';
                            var table=document.createElement("table");
                            table.id="measab"+elmntId;
                            var row=table.insertRow(0);
                            var cell1=row.insertCell(0);
                            cell1.style.cursor = "pointer";
                            cell1.onclick =function(){javascript:deleteColumn('MeasBy'+elmntId,'RowMeasBy',elementName)};
                            var img=document.createElement("img");
                            img.src = "<%=delimgpath%>"
                            cell1.appendChild(img);
                            var cell2=row.insertCell(1);
                            cell2.style.color='black';
                            cell2.innerHTML=elementName;
                            childLI.appendChild(table);
                            parentUL.appendChild(childLI);
//                        }
                        }
                    else if(tarLoc == "rowMeasUL1"){
//                      if(x.match(elmntId)==null){
                            MeasByArray.push(elmntId);
                            RowMeasArray.push(elmntId);
//                            dropMeas.push(elementName);
                            var childLI=document.createElement("li");
                            childLI.id='MeasBy'+elmntId;
                            childLI.style.width='auto';
                            childLI.style.height='auto';
                            childLI.style.color='white';
                            var table=document.createElement("table");
                            table.id="measab"+elmntId;
                            var row=table.insertRow(0);
                            var cell1=row.insertCell(0);
                            cell1.style.cursor = "pointer";
                            cell1.onclick =function(){javascript:deleteColumn('MeasBy'+elmntId,'RowMeasBy',elementName)};
                            var img=document.createElement("img");
                            img.src = "<%=delimgpath%>"
                            cell1.appendChild(img);
                            var cell2=row.insertCell(1);
                            cell2.style.color='black';
                            cell2.innerHTML=elementName;
                            childLI.appendChild(table);
                            parentUL.appendChild(childLI);
//                        }
                        }
                    $(".sortable").sortable();
                    $(".sortable").disableSelection();
                }
                function deleteColumn(index,dropLoc,name){
                    var LiObj=document.getElementById(index);
                    var parentUL=document.getElementById(LiObj.parentNode.id);
                    parentUL.removeChild(LiObj);;
                    var x=index.replace("ViewBy","");
                    var y=index.replace("MeasBy","");
                    var i=0;
                    for(i=0;i<ViewByArray.length;i++){
                        if(ViewByArray[i]==x)
                            ViewByArray.splice(i,1);
                    }
                    if(dropLoc == 'RowViewBy'){
                        for(i=0;i<RowViewByArray.length;i++){
                            if(RowViewByArray[i]==x)
                                RowViewByArray.splice(i,1);
                        }
                        for(i=0;i<rowViewNamesArr.length;i++){
                            if(rowViewNamesArr[i]==name)
                                rowViewNamesArr.splice(i,1);
                        }
                    }
        else if(dropLoc == 'RowMeasBy'){
                        for(i=0;i<MeasByArray.length;i++){
                            if(MeasByArray[i]==y)
                                MeasByArray.splice(i,1);
                        }
                        for(i=0;i<dropMeas.length;i++){
                            if(dropMeas[i]==name)
                                dropMeas.splice(i,1);
                        }
                    }
        else if(dropLoc == 'ColViewBy'){
                        for(i=0;i<ColViewByArray.length;i++){
                            if(ColViewByArray[i]==x)
                                ColViewByArray.splice(i,1);
                        }
                        for(i=0;i<colViewNamesArr.length;i++){
                            if(colViewNamesArr[i]==name)
                                colViewNamesArr.splice(i,1);
                        }
                        colFlag = 0;
                    }
                }

                 function saveEditSingleChart1(chartId){
                 var reportId  = parent.document.getElementById("reportId").value;
                    var ctxpath = parent.$("#ctxpath").val();
                    var count=0;
                    var viewByArray=new Array();
                    var rowViewByArray=new Array();
                    var measArray=new Array();
                    var rowMeasArray=new Array();
                    var colViewByArray=new Array();

                     var ul = document.getElementById("rowViewUL");
                    if(ul!=undefined || ul!=null){
                    var colIds=ul.getElementsByTagName("li");
                    if(colIds!=null && colIds.length!=0){
                        for(var i=0;i<colIds.length;i++){
                            if(i==0){
                                rowViewNamesArr=[];
                            }
                            //cols = cols+","+colIds[i].id.replace("GrpCol","");
                            rowViewByArray.push(colIds[i].id.replace("ViewBy",""));
                            rowViewNamesArr.push(colIds[i].id.replace("ViewBy",""));
                            viewByArray.push(colIds[i].id.replace("ViewBy",""));
                        }

                    }
                     var ul1 = document.getElementById("rowMeasUL");
//                    if(ul1!=undefined || ul!1=null){
                    var colIds1=ul1.getElementsByTagName("li");
                    if(colIds1!=null && colIds1.length!=0){
                        for(var i=0;i<colIds1.length;i++){
                            if(i==0){
                                rowMeasNamesArr=[];
                                dropMeas=[];
                            }
//                            //cols = cols+","+colIds[i].id.replace("GrpCol","");
                            rowMeasNamesArr.push(colIds1[i].id.replace("MeasBy",""));
                            dropMeas.push(colIds1[i].id.replace("MeasBy",""));
                            rowMeasArray.push(colIds1[i].id.replace("MeasBy",""));
                            measArray.push(colIds1[i].id.replace("MeasBy",""));
                        }

                    }
var chartData = JSON.parse(parent.$("#chartData").val());
//chartData[chartId]["dimensionsIds"] = rowViewByArray;
//chartData[chartId]["dimensionsNames"] = rowViewNamesArr;
//parent.$("#chartData").val(JSON.stringify(chartData));
//var chartData = {};
var aggregation=[];
//var arr = rowMeasNamesArr.toString().split(",");
//                    var newArr = [];
//                    for(var k=0;k<arr.length;k++){
//                        if(k!=0){
////                        newArr.push(arr[k]);
//                        aggregation.push("SUM");
//                    }
//                    }
                    MeasBy = [];
                    AggType = [];
                    for( var j=0;j<dropMeas.length;j++){
                    for( var k=0;k<rowMeasNamesArr.length;k++){

                        if(rowMeasNamesArr[k]==dropMeas[j]){
                        MeasBy.push(rowMeasNamesArr[k]);
                        AggType.push(aggTypeListName[k]);
                    }

                    }
                    }
//                    for(var i=0;i<rowViewByArray.length;i++){
                        var chartDetails=chartData[chartId];
                        var viewBys=[];
                        var meassures=[];

                        viewBys.push(rowViewNamesArr[0]);
                        if(chartData[chartId]["chartType"]=="Grouped-Bar"){
                           viewBys.push(rowViewNamesArr[1]);
                        }
//                        meassures.push("COUNT");
//                        aggregation.push("SUM");
//                        aggregation.push("SUM");
                            chartDetails["viewBys"] = viewBys;
//                            chartDetails["chartType"] = "Pie";
                            chartDetails["viewByLevel"] = "single";
                            chartDetails["meassures"] = MeasBy;
                            chartDetails["aggregation"] = AggType;
                            chartDetails["dimensions"] = rowViewNamesArr;
//                            chartDetails["size"] = "S";
                            chartData[chartId] = chartDetails;
//                    }
            var currChartData={};
             currChartData[chartId] = chartDetails;
                    var prevChartData = chartData;
                    parent.$("#chartData").val(JSON.stringify(prevChartData));
                    $.ajax({
                        type:"POST",
                     url: ctxpath+"/reportViewer.do?reportBy=editCharts&rowViewByArray="+encodeURIComponent(rowViewByArray)+"&reportId="+reportId+"&rowViewNamesArr="+encodeURIComponent(rowViewNamesArr)+"&rowMeasArray="+encodeURIComponent(rowMeasArray)+"&rowMeasNamesArr="+JSON.stringify(MeasBy)+"&chartData="+JSON.stringify(currChartData)+"&reportName="+parent.$("#graphName").val(),
                            success: function(data){
                                parent.$("#chartData").val(JSON.stringify(prevChartData));
                               parent.generateSingleChart(data,chartId);

                            }
                    }
                )

                }
                 parent.$("#editViewByDiv").dialog('close');
                }
                
                   
                function editAdvance(){
                      var viewOvName = JSON.parse(parent.$("#viewby").val());
    var viewOvIds = JSON.parse(parent.$("#viewbyIds").val());
    var measName = JSON.parse(parent.$("#measure").val());
    var measIds = JSON.parse(parent.$("#measureIds").val());
    parent.$("#drills").val("");
    parent.$("#driver").val("");
   parent.checkButtonClick = false;
    var nammeVal = $("#visualName").val();
                    var visualChartType = {};
                    if(typeof parent.$("#visualChartType").val()!="undefined" && parent.$("#visualChartType").val()!=""){
                        visualChartType = JSON.parse(parent.$("#visualChartType").val());
                    }
                    
                   var reportId  = parent.document.getElementById("reportId").value;
                    var ctxpath = parent.document.getElementById("ctxPath").value;
                    var count=0;
                    var viewByArray=new Array();
                    var rowViewByArray=new Array();
                    var measArray=new Array();
                    var rowMeasArray=new Array();
                    var colViewByArray=new Array();

                     var ul = document.getElementById("rowViewUL");
                    if(ul!=undefined || ul!=null){
                    var colIds=ul.getElementsByTagName("li");
                    if(colIds!=null && colIds.length!=0){
                        for(var i=0;i<colIds.length;i++){
                            //cols = cols+","+colIds[i].id.replace("GrpCol","");
                            if(i==0){
                                rowViewNamesArr=[];
                            }
                            //cols = cols+","+colIds[i].id.replace("GrpCol","");
//                            rowViewByArray.push(colIds[i].id.replace("ViewBy",""));
                            rowViewByArray.push(colIds[i].id.replace("ViewBy",""));
                            rowViewNamesArr.push(colIds[i].id.replace("ViewBy",""));
                            viewByArray.push(colIds[i].id.replace("ViewBy",""));
                        }

                    }
                     var ul1 = document.getElementById("rowMeasUL");
//                    if(ul1!=undefined || ul!1=null){
                    var colIds1=ul1.getElementsByTagName("li");
                    if(colIds1!=null && colIds1.length!=0){
                        for(var i=0;i<colIds1.length;i++){
                            //cols = cols+","+colIds[i].id.replace("GrpCol","");
//                            if(i==0){
//                                rowMeasNamesArr=[];
//                                dropMeas=[];
//                            }
////                            //cols = cols+","+colIds[i].id.replace("GrpCol","");
//                            rowMeasNamesArr.push(colIds1[i].id.replace("MeasBy",""));
//                            dropMeas.push(colIds1[i].id.replace("MeasBy",""));
                            rowMeasArray.push(colIds1[i].id.replace("MeasBy",""));
                            measArray.push(colIds1[i].id.replace("MeasBy",""));
                        }

                    }
//                     var temp1 = rowMeasNamesArr;
////                     alert(rowMeasNamesArr)
//                    rowMeasNamesArr=[];
//                    dropMeas=[];
                    var allIds =[];
                    var allNames =[];
                    var allIds1 = '<%=allViewIds%>';
                    allIds = allIds1.replace("[","gi","").replace("]","gi","").split(",");
                    var allNames1 = '<%=allViewNames%>';
                    allNames=allNames1.replace("[","","gi").replace("]","","gi").split(",");
//                    for(var t=0;t<temp1.length;t++){
//                        allIds.indexOf(temp1[t]);
//                        rowMeasNamesArr.push(allNames[t]);
//                        dropMeas.push(allNames[t]);
//                    }
                    
//var chartData = JSON.parse(parent.$("#chartData").val());
//chartData[chartId]["dimensionsIds"] = rowViewByArray;
//chartData[chartId]["dimensionsNames"] = rowViewNamesArr;
//parent.$("#chartData").val(JSON.stringify(chartData));
//var chartData = {};alert
var chartData = JSON.parse(parent.$("#chartData").val());

var aggregation=[];
//var arr = rowMeasNamesArr.toString().split(",");
//                    var newArr = [];
//                    for(var k=0;k<arr.length;k++){
//                        if(k!=0){
////                        newArr.push(arr[k]);
//                        aggregation.push("SUM");
//                    }
//                    }
                    MeasBy = [];
                    AggType = [];
                    var meassureIds=[];
                    var temp = rowViewNamesArr;
                    rowViewNamesArr=[];
//                    var allIds =[];
//                    var allNames =[];
//                    var allIds1 = '<%=allViewIds%>';
//                    allIds = allIds1.replace("[","gi","").replace("]","gi","").split(",");
//                    var allNames1 = '<%=allViewNames%>';
//                    allNames=allNames1.replace("[","","gi").replace("]","","gi").split(",");
                    for(var t=0;t<temp.length;t++){
                        allIds.indexOf(temp[t]);
                        rowViewNamesArr.push(allNames[t]);
                    }
//                    for( var j=0;j<dropMeas.length;j++){
//                    for( var k=0;k<rowMeasNamesArr.length;k++){
//
////                        if(rowMeasNamesArr[k]==dropMeas[j]){
//                        MeasBy.push(rowMeasNamesArr[k]);
//                        AggType.push(aggTypeListName[k]);
//                        meassureIds.push(rowMeasArray[k]);
////                    }
//
//                    }
//                    }
var ids = [];
for(var g=0;g<rowMeasArray.length;g++){
            MeasBy.push(measName[measIds.indexOf(rowMeasArray[g].replace("A_","","gi"))]);
            ids.push(rowMeasArray[g].replace("A_","","gi"));
        }
                    var childMeasBy=[];
                    var childMeasByIds=[];
                    var childAggType=[];
                    var childViewBys=[];
//                    alert(measIds);
                   if(typeof parent.$("#childViewBys").val()!=='undefined' && parent.$("#childViewBys").val()!=''){
                       childViewBys=JSON.parse(parent.$("#childViewBys").val());
                       for(var i1 in childViewBys){
                           childViewBys[i1]=childViewBys[i1].replace("ViewBy","");
                       }
                       var selectedMeasByIds=JSON.parse(parent.$("#childMeasBys").val());
//                        alert("selected : "+selectedMeasByIds);
                       for(var k in selectedMeasByIds){
//                           alert(selectedMeasByIds[k]);
//                           alert(measIds);
                           childMeasBy.push(measName[measIds.indexOf(selectedMeasByIds[k].replace("ViewBy",""))]);
                           childMeasByIds.push(selectedMeasByIds[k].replace("ViewBy",""));
                           childAggType.push(AggType[measIds.indexOf(selectedMeasByIds[k].replace("ViewBy",""))]);
                       } 
                   }
                   else{
                       childViewBys=rowViewByArray;
                       childMeasBy=MeasBy;
                       childMeasByIds=ids;
                       childAggType=AggType;
                   }
//                   alert(selectedMeasByIds);
//                   alert("Meas by ids : "+childViewBys);
//                   alert("Meas bys : "+childMeasBy);
//                   alert("Agg types : "+childMeasByIds);
                   for(var i=0;i<childViewBys.length;i++){
                        var chartDetails={};
                        var viewBys=[];
                        var viewIds=[];
                        var meassures=[];
                        
                        viewIds.push(childViewBys[i]);
                            viewBys.push(viewOvName[viewOvIds.indexOf(childViewBys[i])]);
                            chartDetails["viewBys"] = viewBys;
                            chartDetails["chartType"] = "Pie";
                            chartDetails["viewByLevel"] = "single";
                            chartDetails["meassures"] = childMeasBy;
                            chartDetails["aggregation"] = childAggType;
    //                           if($("#chartType").val()=="Pie-Dashboard"){
    //                               chartDetails["dimensions"] = viewOvIds;
    //                           }else{
                           chartDetails["dimensions"] = childViewBys;
//                           }
                            chartDetails["viewIds"] = viewIds;
                            chartDetails["meassureIds"] = childMeasByIds;
                            chartDetails["size"] = "S";
//                            if($("#chartType").val().indexOf("India")!=-1 || $("#chartType").val().indexOf("US")!=-1 || $("#chartType").val().indexOf("World")!=-1 || $("#chartType").val().indexOf("Word")!=-1){
//                             chartDetails["records"] = "";
//                            }
//                            else{
                              chartDetails["records"] = "12";
//                            }
                            chartData["chart" + (i + 2)] = chartDetails;
                    }
                            var chartDetails={};
                            var viewBys=[];
                            var viewIds=[];
                            var meassures=[];
                            var allviewBys=JSON.parse(parent.$("#viewby").val());
                            var allviewIds=JSON.parse(parent.$("#viewbyIds").val());;
                            var meassures=[];
                            var viewBys=[],viewByIds=[];
                            viewBys.push(allviewBys[allviewIds.indexOf(parentViewBy)]);
                            viewByIds.push(parentViewBy);  
                            var index=childViewBys.indexOf(parentViewBy);
                            var dims=[];
                            dims.push(parentViewBy);
                            for(var i in childViewBys){
                                if(i!=index){
                                    dims.push(childViewBys[i]);
                                }
                            }
//                            alert("viewbys : "+viewBys);
//                            alert("ids : "+viewIds);
//                            alert("Dim ids : "+childViewBys);
                            chartDetails["viewBys"] = viewBys;
                            chartDetails["chartType"] = "Pie";
                            chartDetails["viewByLevel"] = "single";
                            chartDetails["meassures"] = childMeasBy;
                            chartDetails["aggregation"] = childAggType;
                            chartDetails["dimensions"] = dims;
                            chartDetails["viewIds"] = viewByIds;
                            chartDetails["meassureIds"] = childMeasByIds;
                            chartDetails["size"] = "S";
                            chartDetails["records"] = "12";
                            chartData["chart1"] = chartDetails;
                            chartData["chart1"]["isDashboardDefined"]="Y";
//                            alert(childViewBys.length+2+":"+Object.keys(chartData).length);
                            for(var i=childViewBys.length+2;Object.keys(chartData).length>childViewBys.length+1;i++){
                                delete chartData["chart"+i];
                            }
//                }
                  
                    parent.$("#visualChartType").val(JSON.stringify(visualChartType));
                    parent.$("#currType").val(nammeVal);
                    parent.$("#chartType").val($("#chartType").val());
                    parent.$("#aggregation").val(JSON.stringify(AggType));
                    parent.$("#chartData").val(JSON.stringify(chartData));
//                    parent.$("#chartData").val(JSON.stringify(chartData));
                    if($("#chartType").val()=="OverLay"){
                        parent.$("#isOverlay").val(true);
                    }else{
                         parent.$("#isOverlay").val(false);
                    }
                    
                   var ctxpath = document.getElementById("ctxPath").value;
                      if($("#chartType").val()=="Trend-Dashboard" || $("#chartType").val()=="Multi-Dashboard"){
                      $.ajax({
            
            async : false,     
            type:"POST",
            data:parent.$("#graphForm").serialize(),
            url:ctxpath+"/reportViewer.do?reportBy=GTKPICalculateFunction&repId="+reportId+"&measId="+childMeasByIds+"&aggType="+AggType+"&measName="+JSON.stringify(encodeURIComponent(childMeasBy))+"&chartId=chart1",
            //                     url: $("#ctxpath").val()+"/reportViewer.do?reportBy=editCharts&rowViewByArray="+encodeURIComponent(viewIds)+"&reportId="+reportId+"&rowViewNamesArr="+encodeURIComponent(viewIds)+"&rowMeasArray="+encodeURIComponent(MeasIds)+"&rowMeasNamesArr="+JSON.stringify(MeasBy)+"&chartData="+encodeURIComponent(JSON.stringify(currChartData))+"&reportName="+parent.$("#graphName").val()+"&isEdit=Y"+"&editId="+divId,
            success: function(data){
                chartData["chart1"]["GTValueList"] = JSON.parse(data);
               
                parent.$("#chartData").val(JSON.stringify(chartData));
                $.ajax({
                          type:'POST',
                          data:parent.$("#graphForm").serialize(),
                          
                     url: ctxpath+"/reportViewer.do?reportBy=editCharts&reportId="+reportId+"&reportName="+encodeURIComponent(parent.$("#graphName").val()),
                            success: function(data){
                                 parent.$("#loading").hide();
                               parent.generateAdavanceVisual(data);
                            }
                    }
                )
                //                var dataValue =   parent.GTValue(chartId,reportId,meassureIds,AggType,MeasBy)
            }})
    }
                      else{
                          $.ajax({
                          type:'POST',
                          data:parent.$("#graphForm").serialize(),
                          
                     url: ctxpath+"/reportViewer.do?reportBy=editCharts&reportId="+reportId+"&reportName="+encodeURIComponent(parent.$("#graphName").val())+"&chartData="+parent.$("#chartData").val(),
                            success: function(data){
                                 parent.$("#loading").hide();
                               parent.generateAdavanceVisual(data);
                            }
                    }
                )
                }
                      
        parent.$("#editViewByDiv").dialog('close');
               }
                }
        </script>
        <input type="hidden" id="reportId" name="reportId" value="<%=reportId%>">
        <input type="hidden" id="ctxPath" name="ctxPath" value="<%=ctxPath%>">
        <table style="display:none">
            <tr><td><%=TranslaterHelper.getTranslatedInLocale("Visual_Type", cL)%></td>
                <td>
                    <select id='chartType'>
                    <option value='Fish-Eye'> Fish Eye</option>
                        <option value='CoffeeWheel'> CoffeeWheel</option>
                        <option value='Tree-Map'> Tree Map</option>
                        <option value='Tree-Map-Single'> Tree Map Single</option>
                        <option value='India-map'> India Map</option>
                        <option value='India-map-with-chart'> India Map with chart</option>
                        <option value='India-City-Map'> India City Map</option>
                        <option value='India-District-Map'>India District Map</option>
                        <option value='us-map'> US Map</option>
                        <option value='us-map-city'> US City Map</option>
                        <option value='world-map-animation'> World City Animation</option>
                        <option value='world-map'> World Map</option>
                        <option value='CollapsibleTree'> CollapsibleTree</option>
                        <option value='Split-Graph'> Split Graph</option>
                        <option value='Multi-View-Bubble'> Multi View Bubble</option>
                        <option value='Multi-View-Bubble'> Multi View Bubble</option>
                        <option value="KPI-Dashboard"> KPI Dashboard</option>
                        <option value="KPI-Bar"> KPI Bar</option>
                        <option value="Bar-Dashboard"> Bar Dashboard</option>
                        <option value="Advance-Pie"> Advance Pie</option>
                        <option value="Advance-Horizontal"> Advance Horizontal</option>
                        <option value="Bubble-Dashboard"> Bubble Dashboard</option>
                        <option value="Scatter-Dashboard"> Scatter Dashboard</option>
                        <option value="Pie-Dashboard"> Pie Dashboard</option>
                        <option value="Trend-Dashboard"> Trend Dashboard</option>
                        <option value="Multi-Measure-Dashboard"> Multi Measure Dashboard</option>
                        <option value="Multi-View-Wordcloud"> Multi View Wordcloud</option>
                        <option value="Wordcloud"> Wordcloud</option>
                        <option value="Overlay"> Overlay</option>
                        <option value="multi-kpi"> Multi Pie</option>
                        <option value="combo-chart"> Combo Chart</option>
                        <option value="Multi-Dashboard"> Multi-Dashboard</option>
                    </select>
                    </td>
                    
                    <td>
                        
                        &nbsp;&nbsp;<%=TranslaterHelper.getTranslatedInLocale("Name", cL)%>:&nbsp&nbsp<input type="text" id="visualName" >
                    </td>
                </tr>
        </table>
        <form name="ViewByForm" id="ViewByForm" method="post">
            <table style="width:100%;height:300px;" border="1">
                <tr>
                    <td width="50%" valign="top" class="draggedTable1">
                        <div style="height:20px;" class="bgcolor" align="center"><font size="2" style="font-weight:bold;color:#369" class="gFontFamily gFontSize12"><%=TranslaterHelper.getTranslatedInLocale("ViewBy_s", cL)%></font></div>
                        <div style="height:280px;overflow-y:auto;border: 1px dotted rgb(228, 228, 228);box-shadow: 1px 0px 0px 2px rgb(228, 228, 228);">
                            <ul id="ViewByList" class="filetree treeview-famfamfam">
                                <li>
                                    <ul>
                                        <%
                for (int i = 0; i < allViewIds.size(); i++) {
                                        %>
                                        <li class="closed">

                                               <table><tr>
                                                       <td><img alt=""  src='<%=imgpath%>'/></td>
                                            <td width="100"><span title="" id="<%=allViewIds.get(i)%>" class="viewBys gFontFamily gFontSize12"><%=allViewNames.get(i)%></span></td>
                                            <td title="<bean:message bundle="Tooltips" key="changeViewBy.rowViewBy"/>">
                                                <!--<img alt=""  src='<%=imgpathRow%>' onclick="createViewBY('<%=allViewIds.get(i)%>','<%=allViewNames.get(i)%>','rowViewUL')"/>-->
                                           </td>
                                            <td title="<bean:message bundle="Tooltips" key="changeViewBy.colViewBy"/>" style="display:none"><img src='<%=imgpathCol%>' onclick="createViewBY('<%=allViewIds.get(i)%>','<%=allViewNames.get(i)%>','colViewUL')"/></td>
                                          </tr></table>
                                        </li>
                                        <%}%>
                                    </ul>
                                </li>
                            </ul>
                        </div>
                    </td>
                    <td width="50%" valign="top">
                        <table width="100%">
                            <tr style="height:50%">
                                <td>
                                    <div style="height:20px;" align="center" class="bgcolor"><font size="2" class="gFontFamily gFontSize12" style="font-weight:bold;"><%=TranslaterHelper.getTranslatedInLocale("Parent_ViewBys", cL)%></font></div>
                                    <div style="height:50px;overflow-y:auto;border: 1px dotted rgb(228, 228, 228);box-shadow: 1px 0px 0px 2px rgb(228, 228, 228);" id="RowViewBy">
                                        <ul id="rowViewUL" class="sortable gFontFamily gFontSize12">
                                            <%=parentViewBy%>
                                        </ul>
                                    </div>
                                </td>
                            </tr>
                            <tr style="height:50%">
                                <td>
                                    <div style="height:20px;" align="center" class="bgcolor"><font size="2" style="font-weight:bold;" class="gFontFamily gFontSize12"><%=TranslaterHelper.getTranslatedInLocale("Child_ViewBys", cL)%></font></div>
                                    <div style="height:130px;overflow-y:auto;border: 1px dotted rgb(228, 228, 228);box-shadow: 1px 0px 0px 2px rgb(228, 228, 228);" id="RowMeasBy">
                                        <ul id="rowMeasUL" class="sortable gFontFamily gFontSize12">
                                            <%=childViewBys%>
                                        </ul>
                                    </div>
                                </td>
                            </tr>
                            <tr style="height:50%">
                                <td>
                                    <div style="height:20px;" align="center" class="bgcolor"><font size="2" style="font-weight:bold;" class="gFontFamily gFontSize12"><%=TranslaterHelper.getTranslatedInLocale("Child_Measures", cL)%></font></div>
                                    <div style="height:130px;overflow-y:auto;border: 1px dotted rgb(228, 228, 228);box-shadow: 1px 0px 0px 2px rgb(228, 228, 228);" id="RowMeasBy1">
                                        <ul id="rowMeasUL1" class="sortable gFontFamily gFontSize12">
                                            <%=childMeasBys%>
                                        </ul>
                                    </div>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>
            <table style="width:100%" align="center">
                <tr>
                    <td colspan="2" style="height:10px"></td>
                </tr>
                <tr>
                    <td colspan="2" align="center">
                        <%if(request.getParameter("from")!=null && request.getParameter("from").equalsIgnoreCase("isedit")) {%>
                        <input id="ChangeViewbyButton" type="button" class="navtitle-hover gFontFamily gFontSize12" style="width:auto" value="<%=TranslaterHelper.getTranslatedInLocale("done", cL)%>" onclick="buildcharts()">
                        <% }else{%>
                        <input id="ChangeViewbyButton" type="button" class="navtitle-hover gFontFamily gFontSize12" style="width:auto" value="<%=TranslaterHelper.getTranslatedInLocale("done", cL)%>" onclick="buildcharts()">
                        <% } %>
                    </td>
                </tr>
            </table>
        </form>
    </body>
    <%
            } catch (Exception e) {
                e.printStackTrace();
            }
    %>
</html>