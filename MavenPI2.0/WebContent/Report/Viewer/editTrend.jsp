<%@page contentType="text/html" pageEncoding="UTF-8" import="java.util.List,java.util.ArrayList,java.util.HashMap,prg.db.Session,prg.db.Container,com.progen.reportview.bd.PbReportViewerBD,com.progen.reportview.db.PbReportViewerDAO,com.progen.report.PbReportCollection,com.progen.report.PbReportRequestParameter"%>
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
                String rowViewByStr = "";
                String colViewByStr = "";
                String aggTypeString = "";
                 String imgpathRow = request.getContextPath() + "/icons pinvoke/arrow-curve.png";
                String imgpathCol = request.getContextPath() + "/icons pinvoke/arrow-curve-270.png";
                String imgpath = request.getContextPath() + "/icons pinvoke/tick-small.png";
                String delimgpath = request.getContextPath() + "/icons pinvoke/cross-small.png";
                String reportId = request.getParameter("REPORTID");
                String ctxPath = request.getParameter("ctxPath");
                String localAddEdit = request.getParameter("localAddEdit");
                String themeColor="blue";
                String trenFlag = "Trend";

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
                if (session.getAttribute("rowViewIdList") != null) {
                    rowViewIdList = (List<String>) session.getAttribute("rowViewIdList");
                    rowNamesLst = (List<String>) session.getAttribute("rowNamesLst");
                    rowMeasIdList = (List<String>) session.getAttribute("rowMeasIdList");
                    rowMeasNamesLst = (List<String>) session.getAttribute("rowMeasNamesLst");
                   Container container = null;
                    container = Container.getContainerFromSession(request, reportId);
                    if(request.getParameter("from")!=null && request.getParameter("from")!="" && request.getParameter("from").equalsIgnoreCase("viewer")){
//                        rowViewByStr = reportViewDAO.buildTrendDragableViewBy(reportId,"RowViewBy", ctxPath,container,"Local",localAddEdit);
//                        colViewByStr = reportViewDAO.buildTrendDragableMeasBy(reportId,"ColViewBy", ctxPath,container,"Local",localAddEdit);
//

                          rowViewByStr = reportViewDAO.buildDragableViewBy(reportId,"RowViewBy", ctxPath,container,"Local",trenFlag);
                        colViewByStr = reportViewDAO.buildDragableMeasBy(reportId,"ColViewBy", ctxPath,container,"Local",trenFlag);
                    }
                    else if(request.getParameter("chartId")!=null && !request.getParameter("chartId").equalsIgnoreCase("undefined")){
//                    rowViewByStr = reportViewDAO.buildTrendDragableViewBy(reportId,"RowViewBy", ctxPath,container,request.getParameter("chartId"),localAddEdit);
//                    colViewByStr = reportViewDAO.buildTrendDragableMeasBy(reportId,"ColViewBy", ctxPath,container,request.getParameter("chartId"),localAddEdit);
                     rowViewByStr = reportViewDAO.buildDragableViewBy(reportId,"RowViewBy", ctxPath,container,request.getParameter("chartId"),trenFlag);
                    colViewByStr = reportViewDAO.buildDragableMeasBy(reportId,"ColViewBy", ctxPath,container,request.getParameter("chartId"),trenFlag);
                    }
                    else{
//                    rowViewByStr = reportViewDAO.buildChangeViewBy(rowViewIdList, rowNamesLst, "RowViewBy", ctxPath);
                    }
                } else {
                    rowViewIdList = new ArrayList<String>();
                    rowMeasIdList = new ArrayList<String>();
                    rowNamesLst = new ArrayList<String>();
                    rowMeasNamesLst = new ArrayList<String>();
                }
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
                    String ContextPath=request.getContextPath();


    %>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>piEE</title>

      <script src="<%=ContextPath%>/javascript/lib/jquery/js/jquery-1.7.2.min.js" type="text/javascript"></script>
<!--         <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/lib/jquery/js/jquery-1.3.2.min.js"></script>-->
<!--        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>-->
        <script type="text/javascript" src="<%=ContextPath%>/javascript/lib/jquery/js/jquery-1.8.23-custom.min.js"></script>
<!--         <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/lib/jquery/js/jquery-1.4.2.min.js"></script>-->
         <link type="text/css" href="<%=ContextPath%>/stylesheets/themes/<%=themeColor%>/ReportCss.css" rel="stylesheet" />
        <link rel="stylesheet" href="<%=ContextPath%>/stylesheets/treeviewstyle/jquery.treeview.css" />
        <link rel="stylesheet" href="<%=ContextPath%>/stylesheets/treeviewstyle/screen.css" />
        <link type="text/css" href="<%=ContextPath%>/stylesheets/themes/<%=themeColor%>/metadataButton.css" rel="stylesheet" />


<!--        <script src="<%=request.getContextPath()%>/javascript/draggable/jquery-1.3.2.js" type="text/javascript"></script>-->

        <script src="<%=ContextPath%>/javascript/treeview/jquery.cookie.js" type="text/javascript"></script>
        <script src="<%=ContextPath%>/javascript/treeview/jquery.treeview.js" type="text/javascript"></script>
        <%--<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/treeview/demo.js"></script>--%>
<!--        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/draggable/ui.core.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/draggable/ui.draggable.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/draggable/ui.droppable.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/ui.sortable.js"></script>-->
        <script type="text/javascript" src="<%=ContextPath%>/javascript/quicksearch.js"></script>
       <link type="text/css" href="<%=ContextPath%>/css/global.css" rel="stylesheet" />
        <style type="text/css">
/*            *{font:11px verdana;}*/
        </style>

    </head>
    <body>
        <input type="hidden" id="reportId" name="reportId" value="<%=reportId%>">
        <input type="hidden" id="ctxPath" name="ctxPath" value="<%=ctxPath%>">
        <form name="ViewByForm" id="ViewByForm" method="post">
            <table style="width:100%;height:300px;" border="1">
                <tr>
                    <td width="50%" valign="top" class="draggedTable1">
                        <div style="height:20px;" class="bgcolor" align="center"><font size="2" class="gFontFamily gFontSize12" style="font-weight:bold;color:gray;">ViewBy's</font></div>
                        <div style="height:280px;overflow-y:auto">
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
                                    <div style="height:20px;" align="center" class="bgcolor"><font size="2" class="gFontFamily gFontSize12" style="font-weight:bold;color:gray;">ViewBys</font></div>
                                    <div style="height:130px;overflow-y:auto;border: 1px dotted rgb(228, 228, 228);box-shadow: 1px 0px 0px 2px rgb(228, 228, 228);" id="RowViewBy">
                                        <ul id="rowViewUL" class="sortable gFontFamily gFontSize12">
                                            <%=rowViewByStr%>
                                        </ul>
                                    </div>
                                </td>
                            </tr>
                            <tr style="height:50%">
                                <td>
                                    <div style="height:20px;" align="center" class="bgcolor"><font size="2" class="gFontFamily gFontSize12" style="font-weight:bold;color:gray;">Measures</font></div>
                                    <div style="height:130px;overflow-y:auto;border: 1px dotted rgb(228, 228, 228);box-shadow: 1px 0px 0px 2px rgb(228, 228, 228);" id="RowMeasBy">
                                        <ul id="rowMeasUL" class="sortable gFontFamily gFontSize12">
                                            <%=colViewByStr%>
                                        </ul>
                                    </div>
                                </td>
                            </tr>
                            <tr style="height:00%;display:none">
                                <td>
                                    <div style="height:20px;" align="center" class="bgcolor"><font size="2" class="gFontFamily gFontSize12" style="font-weight:bold;color:gray;">Column ViewBys</font></div>
                                    <div  style="height:125px;overflow-y:auto;border: 1px dotted rgb(228, 228, 228);box-shadow: 1px 0px 0px 2px rgb(228, 228, 228);" id="ColViewBy">
                                        <ul id="colViewUL" class="sortable gFontFamily gFontSize12">
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
                        <% if((request.getParameter("from")!=null && !request.getParameter("from").equalsIgnoreCase("undefined")) && request.getParameter("from").equalsIgnoreCase("viewer")){ %>
                        <input id="ChangeViewbyButton" type="button" class="navtitle-hover gFontFamily gFontSize12" style="width:auto" value="Done" onclick="buildcharts()">
                        <%}else if(request.getParameter("chartId")==null || request.getParameter("chartId").equalsIgnoreCase("undefined")){%>
                        <input id="ChangeViewbyButton" type="button" class="navtitle-hover gFontFamily gFontSize12" style="width:auto" value="Done" onclick="saveEditSingleTrend()">
                        <%}else{%>
                        <input id="ChangeViewbyButton" type="button" class="navtitle-hover gFontFamily gFontSize12" style="width:auto" value="Done" onclick="saveEditSingleTrend1('<%=request.getParameter("chartId")%>')">
                        <% } %>
                    </td>
                </tr>
            </table>
        </form>
                     <script type="text/javascript">
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
//                    var v=parent.document.getElementById("Designer").value;
//                   // /
//                  if(parent.document.getElementByIrowViewByStrd("Designer").value=="fromDesigner"){
//                        // <//%rowViewByStr="";colViewByStr=""; %>
//                    }
                    $("#ViewByList").treeview({
                        animated:"slow"
//                        persist: "cookie"
                    });
                    $(".sortable").sortable();
                    $(".sortable").disableSelection();
                    $("#Rowdrop").sortable();
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
                    $("#RowViewBy").droppable({
                        activeClass:"blueBorder",
                         accept:function(d){return true;},
                         drop: function(ev, ui) {
                             //Code added by mohit for drag and drop in rows and cols
                             //changed by sruthi for drag and drop in rows and rows
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
                     From=parent.document.getElementById("Designer").value;
                    if(From=="fromDesigner"){
                        $("#ChangeViewbyButton").val("Next")
                        //alert("automatic assigment"); //added by mohit for create report
                        //<//%rowViewByStr="";colViewByStr=""; %>
                        createViewBY('<%=allViewIds.get(0)%>','<%=allViewNames.get(0)%>','rowViewUL')
                                              saveViewBy()

                    }
                });


                function saveEditChart(){
                 var reportId  = document.getElementById("reportId").value;
                    var ctxpath = document.getElementById("ctxPath").value;
                    var count=0;
                    var viewByArray=new Array();
                    var rowViewByArray=new Array();
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
                     url: ctxpath+"/reportViewer.do?reportBy=addNewChartsUI&rowViewByArray="+encodeURIComponent(rowViewByArray)+"&reportId="+reportId+"&userId="+userId+"&chartData="+JSON.stringify(chartData)+"&reportName="+parent.$("#graphName").val(),
                            success: function(data){
//                                 $("#loading").show();
                            }
                    }
                )

                }
                 $("#editViewByDiv").dialog('close')
                 parent.$("#editViewByDiv").dialog('close')
                }
                function saveEditSingleTrend(){
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
//                    parent.$("#viewby").val(JSON.stringify(rowViewNamesArr));
//                    parent.$("#viewbyIds").val(JSON.stringify(rowViewByArray));
//                    parent.$("#measure").val(JSON.stringify(MeasBy));
//                    parent.$("#measureIds").val(JSON.stringify(MeasBy));
//                    parent.$("#aggregation").val(JSON.stringify(AggType));
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

                     var viewOvName = JSON.parse(parent.$("#viewby").val());
    var viewOvIds = JSON.parse(parent.$("#viewbyIds").val());
    var measName = JSON.parse(parent.$("#measure").val());
    var measIds = JSON.parse(parent.$("#measureIds").val());
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
var chartData = {};
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
                    for( var j=0;j<dropMeas.length;j++){
                    for( var k=0;k<rowMeasNamesArr.length;k++){

                        if(rowMeasNamesArr[k]==dropMeas[j]){
                        MeasBy.push(rowMeasNamesArr[k]);
                        AggType.push(aggTypeListName[k]);
                        meassureIds.push(rowMeasArray[k]);
                    }

                    }
                    }
                    for(var i=0;i<rowMeasArray.length;i++){
                        var chartDetails={};
                        var viewBys=[];
                        var viewIds=[];
                        var meassures=[];
//                        viewBys.push(rowViewNamesArr[i]);
//                         meassures.push(MeasBy[i]);
                         meassureIds=[];
                        meassureIds.push(rowMeasArray[i]);
//                        for(var m=0;m<rowViewByArray.length;m++){
                            viewBys.push(viewOvName[viewOvIds.indexOf(rowViewByArray[0])]);
                               viewIds.push(rowViewByArray[0]);
//                        }
                        for(var m=0;m<meassureIds.length;m++){
                            meassures.push(measName[measIds.indexOf(meassureIds[m])]);
                        }
//                        aggregation.push(AggType[i]);
//                        meassures.push("COUNT");
//                        aggregation.push("SUM");
//                        aggregation.push("SUM");
                        chartDetails["viewBys"] = viewBys;
//                            chartDetails["chartType"] = "Pie";
                            chartDetails["viewByLevel"] = "single";
                            chartDetails["meassures"] = meassures;
                            chartDetails["aggregation"] = AggType;
                            chartDetails["viewIds"] = viewIds;
                            chartDetails["meassureIds"] = meassureIds;
                            chartDetails["size"] = "S";
                             chartDetails["records"] = "12";
                            chartDetails["chartType"] = "Line";
                            chartData["chart" + (i + 1)] = chartDetails;
                    }

//                    for(var i=0;i<rowViewNamesArr.length;i++){
//                        var chartDetails={};
//                        var viewBys=[];
//                        var meassures=[];
//                        viewBys.push(rowViewNamesArr[i]);
////                        meassures.push(MeasBy[i]);
////                        aggregation.push(AggType[i]);
////                        meassures.push("COUNT");
////                        aggregation.push("SUM");
////                        aggregation.push("SUM");
//                        chartDetails["viewBys"] = viewBys;
//                            chartDetails["chartType"] = "Pie";
//                            chartDetails["viewByLevel"] = "single";
//                            chartDetails["meassures"] = MeasBy;
//                            chartDetails["aggregation"] = AggType;
//                            chartDetails["size"] = "S";
//                            chartDetails["chartType"] = "Line";
//                            chartData["chart" + (i + 1)] = chartDetails;
//                    }


//                    parent.$("#chartData").val(JSON.stringify(chartData));
//                    parent.$("#viewby").val(JSON.stringify(rowViewNamesArr));
//                    parent.$("#measure").val(JSON.stringify(MeasBy));
//                    parent.$("#aggregation").val(JSON.stringify(AggType));
                    var userId = parent.$("#userId").val();
                    parent.$("#chartData").val(JSON.stringify(chartData));
                   var ctxpath = document.getElementById("ctxPath").value;
                      $.ajax({
                          data:parent.$("#graphForm").serialize(),
                     url: ctxpath+"/reportViewer.do?reportBy=buildTrend&reportId="+reportId+"&reportName="+parent.$("#graphName").val()+"&chartData="+encodeURIComponent(parent.$("#chartData").val()),
                            success: function(data){

//                                 parent.$("#loading").hide();
//                               parent.generateTrend(data);
                               parent.generateChart(data);
                            }
                    }
                )
        parent.$("#editViewByDiv").dialog('close');
               }
           }
                function saveViewBy(){
                var From="";
                if(parent.document.getElementById("Designer") != null)
                From=parent.document.getElementById("Designer").value;

                    var reportId  = document.getElementById("reportId").value;
                    var ctxpath = document.getElementById("ctxPath").value;
                    var count=0;
                    var viewByArray=new Array();
                    var rowViewByArray=new Array();
                    var colViewByArray=new Array();

                     var ul = document.getElementById("rowViewUL");
                    if(ul!=undefined || ul!=null){
                    var colIds=ul.getElementsByTagName("li");
                    if(colIds!=null && colIds.length!=0){
                        for(var i=0;i<colIds.length;i++){
                            //cols = cols+","+colIds[i].id.replace("GrpCol","");
                            rowViewByArray.push(colIds[i].id.replace("ViewBy",""));
                            viewByArray.push(colIds[i].id.replace("ViewBy",""));
                        }

                    }
                }
                         for(var i=0;i<rowViewByArray.length;i++){
                             for(var j=i+1;j<rowViewByArray.length;j++){
                                 if(rowViewByArray[i]==rowViewByArray[j])
                                     count =count+1;
                             }
                         }

                    var colviewByUl = document.getElementById("colViewUL");
                    if(colviewByUl!=undefined || colviewByUl!=null){
                    var colIds=colviewByUl.getElementsByTagName("li");
                    if(colIds!=null && colIds.length!=0){
                        for(var i=0;i<colIds.length;i++){
                            //cols = cols+","+colIds[i].id.replace("GrpCol","");
                            colViewByArray.push(colIds[i].id.replace("ViewBy",""));
                            viewByArray.push(colIds[i].id.replace("ViewBy",""));
                        }

                    }
                }
                         for(var i=0;i<colViewByArray.length;i++){
                            for(var j=0;j<rowViewByArray.length;j++){
                                 if(colViewByArray[i]==rowViewByArray[j])
                                     count =count+1;
                             }
                         }
                    if(colViewByArray.length == 0 && rowViewByArray.length == 0){
                        alert("Please Select Atleast one Row Viewby")
                    }else if(count>0){
                        alert("Please Select different Row Viewby and Col Viewby")
                    }
                    else if(rowViewByArray.length>0 ){
                         if(From!="fromDesigner"){
                 $("#editViewByDiv").dialog('close')
                 parent.$("#editViewByDiv").dialog('close')
                   parent.document.getElementById('loading').style.display='';
             }
//                        $.ajax({
//                            url: ctxpath+"/reportViewer.do?reportBy=changeViewBy&ViewByArray="+ViewByArray+"&RowViewByArray="+RowViewByArray+"&ColViewByArray="+ColViewByArray+"&ChangeView=ChangeViewBy&reportId="+reportId+"&ctxpath="+ctxpath+"&rowViewNamesArr="+rowViewNamesArr+"&colViewNamesArr="+colViewNamesArr,
//                            success: function(data){
//                                submitChangeViewBy();
//                            }
//                        });

                            if(From=="fromDesigner"){
                                $.post(ctxpath+"/reportTemplateAction.do?templateParam=designerViewbys&ViewByArray="+encodeURIComponent(viewByArray)+"&RowViewByArray="+encodeURIComponent(rowViewByArray)+"&ColViewByArray="+encodeURIComponent(colViewByArray)+"&ChangeView=ChangeViewBy&reportId="+reportId+"&ctxpath="+ctxpath+"&rowViewNamesArr="+encodeURIComponent(rowViewNamesArr)+"&colViewNamesArr="+encodeURIComponent(colViewNamesArr), $("#ViewByForm").serialize(),
                             function(data){//
                               parent.$("#editViewByDiv").dialog('close');
                               parent.$("#paramDesign").hide();
                    var prevREPIds=parent.document.getElementById("REPIds").value
                    var frameObj=parent.document.getElementById("dataDispmem");
                    var prevCEPIds=parent.document.getElementById("CEPIds").value
                    var roleid=parent.document.getElementById("roleid").value
                    var RepIdsArray = new Array()
                    var CepIdsArray = new Array()
                    RepIdsArray = prevREPIds.split(",")
                    CepIdsArray = prevCEPIds.split(",")
                    parent.document.getElementById("REPIds").value = rowViewByArray;
                    parent.document.getElementById("CEPIds").value = colViewByArray;
                    var flag = 0;
                    for(var i=0;i<rowViewByArray.length;i++){
                        for(var j=0;j<colViewByArray.length;j++){
                            if(RepIdsArray[i] == CepIdsArray[j]){
                                flag=flag+1;
                            }
                        }
                    }
                    if(rowViewByArray!=""){
                        if(flag==0){
                            parent.$("#measuresDialog").dialog('open');
                            var source="reportTemplateAction.do?templateParam=getMeasures&foldersIds="+roleid+'&REPORTID='+parent.document.getElementById("REPORTID").value;
                            //frameObj.src='<center><img id="imgId" src="images/ajax.gif" align="middle"  width="75px" height="75px"  style="position:absolute" ></center>';
                            frameObj.src=source;
                        }else{
                            alert("Please select different Row Edge and Col Edge")
                        }

                    }
                    else{
                        if(rowViewByArray=="" || prevREPIds==undefined ){
                            alert("Please select Row Edge ")
                        }
                    }

                        });
                            }else{
                         $.post(ctxpath+"/reportViewer.do?reportBy=changeViewBy&ViewByArray="+viewByArray+"&RowViewByArray="+rowViewByArray+"&ColViewByArray="+colViewByArray+"&ChangeView=ChangeViewBy&reportId="+reportId+"&ctxpath="+ctxpath+"&rowViewNamesArr="+rowViewNamesArr+"&colViewNamesArr="+colViewNamesArr, $("#ViewByForm").serialize(),
                             function(data){
                                submitChangeViewBy();
                        });
                    }
                }
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
//                          if(x.match(elmntId)==null){
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
//                            }
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
                            dropMeas.push(elementName);
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

                 function saveEditSingleTrend1(chartId){
                     var viewOvName = JSON.parse(parent.$("#viewby").val());
    var viewOvIds = JSON.parse(parent.$("#viewbyIds").val());
    var measName = JSON.parse(parent.$("#measure").val());
    var measIds = JSON.parse(parent.$("#measureIds").val());
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
var aggregation=[];
                    MeasBy = [];
                    AggType = [];
                    var meassureIds=[];
                    for( var j=0;j<dropMeas.length;j++){
                    for( var k=0;k<rowMeasNamesArr.length;k++){

                        if(rowMeasNamesArr[k]==dropMeas[j]){
                        MeasBy.push(rowMeasNamesArr[k]);
                        AggType.push(aggTypeListName[k]);
                        meassureIds.push(measArray[k]);
                    }

                    }
                    }
//                    for(var i=0;i<rowViewByArray.length;i++){
                        var chartDetails=chartData[chartId];
                        var viewBys=[];
                        var viewIds=[];
                        viewIds.push(rowViewNamesArr[0]);
                        if(chartData[chartId]["chartType"]=="Grouped-Bar"){
                           viewIds.push(rowViewNamesArr[1]);
                        }
                        if(chartData[chartId]["chartType"]=="Table" || chartData[chartId]["chartType"]=="Transpose-Table"){
                             viewIds =[];
                          for(var n=0;n<rowViewNamesArr.length;n++){
                              viewIds.push(rowViewNamesArr[n]);
                          }

                         }

                         for(var m=0;m<viewIds.length;m++){
                            viewBys.push(viewOvName[viewOvIds.indexOf(viewIds[m])])
                        }
                        MeasBy=[];
                         for(var m=0;m<meassureIds.length;m++){
                            MeasBy.push(measName[measIds.indexOf(meassureIds[m])])
                        }
                            chartDetails["viewBys"] = viewBys;
                            chartDetails["viewByLevel"] = "single";
                            chartDetails["meassures"] = MeasBy;
                            chartDetails["viewIds"] = viewIds;
                            chartDetails["meassureIds"] = meassureIds;
                            chartDetails["aggregation"] = AggType;
                            chartDetails["dimensions"] = rowViewNamesArr;
                            chartData[chartId] = chartDetails;
//                    }
            var currChartData={};
             currChartData[chartId] = chartDetails;

                    var prevChartData = chartData;
                    parent.$("#chartData").val(JSON.stringify(currChartData));
                    $.ajax({
                        type:"POST",
                         data:parent.$("#graphForm").serialize(),
                     url: ctxpath+"/reportViewer.do?reportBy=editCharts&rowViewByArray="+encodeURIComponent(rowViewByArray)+"&reportId="+reportId+"&rowViewNamesArr="+encodeURIComponent(rowViewNamesArr)+"&rowMeasArray="+encodeURIComponent(rowMeasArray)+"&rowMeasNamesArr="+JSON.stringify(encodeURIComponent(MeasBy))+"&reportName="+parent.$("#graphName").val()+"&isEdit=Y"+"&editId="+chartId,
                            success: function(data){
                                 var jsondata = JSON.parse(data)["data"];
                                parent.$("#chartData").val(JSON.stringify(prevChartData));
                              parent.generateSingleTrend(jsondata,chartId);

                            }
                    }
                )

                }
                 parent.$("#editViewByDiv").dialog('close');
                }
        </script>
    </body>
    <%
            } catch (Exception e) {
                e.printStackTrace();
            }
    %>
</html>
