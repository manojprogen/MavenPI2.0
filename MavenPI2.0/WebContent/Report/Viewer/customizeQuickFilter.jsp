
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@page contentType="text/html" pageEncoding="UTF-8" import="java.util.List,java.util.ArrayList,java.util.HashMap,prg.db.Session,prg.db.Container,com.progen.reportview.bd.PbReportViewerBD" %>
<%@page import="com.progen.reportview.db.PbReportViewerDAO,com.progen.report.PbReportCollection,com.progen.report.PbReportRequestParameter"%>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
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
                List<String> selectedViewBys= null;
                List<String> allViewNames = null;
                List<String> rowViewIdList = null;
                List<String> rowMeasIdList = null;
                List<String> colViewIdList = null;
                List<String> rowNamesLst = null;
                List<String> rowMeasNamesLst = null;
                List<String> colNamesLst = null;
                List<String> aggTypes = null ;
                String currentViewBy = "";
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
                if (session.getAttribute("viewByFilters") != null) {
                    allViewIds = (List<String>) session.getAttribute("viewByFilters");
                } else {
                    allViewIds = new ArrayList<String>();
                }
                if (session.getAttribute("selectedViewBys") != null) {
                    selectedViewBys = (List<String>) session.getAttribute("selectedViewBys");
                } else {
                    selectedViewBys = new ArrayList<String>();
                }
                if (session.getAttribute("currentViewBy") != null) {
                    currentViewBy = (String)session.getAttribute("currentViewBy");
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
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/lib/jquery/js/jquery-1.8.23-custom.min.js"></script>
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
        <script type="text/javascript">
            var onLoad=true;
            var viewByData1=[];
            var parentViewBy='';
            var dataCounter=20;
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
                    $("#viewByData").scroll(function(){
                    if ($("#viewByData").scrollTop() == this.scrollHeight - this.clientHeight){
                        loadMoreViewByData();
                    }
                });
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
                
                function loadMoreViewByData(){
                var beg=dataCounter;
                var viewByData='';
                if(onLoad){
                    viewByData=JSON.parse('<%=allViewIds%>');
                }
                else{
                    viewByData=viewByData1;
                }
                var end=(dataCounter+20)<viewByData.length?dataCounter+20:viewByData.length;
                dataCounter+=20;
                var innerHtml='';
                var imgPath="<%=imgpath%>";
                var imgPathCol="<%=imgpathCol%>";
                for(var i=beg;i<end;i++){
                    innerHtml+='<li class="closed">';
                    innerHtml+='<table><tr><td><img alt=""  src="'+imgPath+'"/></td>';
                    innerHtml += "<td width='100'><span id='"+i+"' class='viewBys'>"+viewByData[i]+"</span></td>";
                    innerHtml +='<td>';
                    innerHtml +='</td>';
                    innerHtml += '<td style="display:none"><img src="'+imgPathCol+'" onclick="createViewBY('+i+','+viewByData[i]+',"colViewUL")"/></td>';
                    innerHtml +="</tr></table></li>";
                }
                $("#viewByDataList").append(innerHtml);
                $(".viewBys").draggable({
                    helper:"clone",
                    effect:["", "fade"]
                });
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

                
                   
                function changeFilterViewBy(flag){
                    onLoad=false;
                    var viewBy;
                    var allViewBys=JSON.parse(parent.$("#viewby").val());
                    if(typeof flag==='undefined' || flag ===''){
                        viewBy=allViewBys[1];
                    }
                    else{
                        viewBy=flag;
                    }
                    var REPORTID = parent.document.getElementById("REPORTID").value;
                    if(typeof REPORTID ==="undefined" || REPORTID===""){
                        REPORTID = $("#graphsId").val();
                    }
                    var ctxPath=parent.document.getElementById("h").value;
                    $.ajax({
                        async:false,
                        type:"POST",
                        data:
                            $('#ViewByForm').serialize() + "&REPORTID="+REPORTID+"&ctxPath="+ctxPath+"&userId="+$("#usersId").val()+"&visualName="+$("#currType").val()+"&filterData="+encodeURIComponent(JSON.stringify(parent.filterData))+"&viewBy="+viewBy,
                        url: ctxPath+"/reportTemplateAction.do?templateParam=getSelectedDims",
                        success:function(data){
                            viewByData1=data.split(/\[|\]|,/) //to make array
                            .map(function(c){return c.trim();}) //to get rid of spaces
                            .filter(function(c){return c.length > 0}) // to get rid of empty elements and '[' and ']'
                            dataCounter=0;
                            var beg=dataCounter;
                            var end=(dataCounter+20)<viewByData1.length?dataCounter+20:viewByData1.length;
                            dataCounter+=20;
                            var innerHtml='';
                            var imgPath="<%=imgpath%>";
                            var imgPathCol="<%=imgpathCol%>";
                            for(var i=beg;i<end;i++){
                                innerHtml+='<li class="closed">';
                                innerHtml+='<table><tr><td><img alt=""  src="'+imgPath+'"/></td>';
                                innerHtml += "<td width='100'><span id='"+i+"' class='viewBys'>"+viewByData1[i]+"</span></td>";
                                innerHtml +='<td>';
                                innerHtml +='</td>';
                                innerHtml += '<td style="display:none"><img src="'+imgPathCol+'" onclick="createViewBY('+i+','+viewByData1[i]+',"colViewUL")"/></td>';
                                innerHtml +="</tr></table></li>";
                            }
                            $("#viewByDataList").html(innerHtml);
                            $(".viewBys").draggable({
                                    helper:"clone",
                                    effect:["", "fade"]
                                });
                        }
                    });
                }
        </script>
    </head>
    <body>
        <input type="hidden" id="reportId" name="reportId" value="<%=reportId%>">
        <input type="hidden" id="ctxPath" name="ctxPath" value="<%=ctxPath%>">
        <table style="display:none">
            <tr><td>Visual Type</td>
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
                        
                        &nbsp;&nbsp;Name:&nbsp&nbsp<input type="text" id="visualName" >
                    </td>
                </tr>
        </table>
        <form name="ViewByForm" id="ViewByForm" method="post">
            <table style="width:100%;height:300px;" border="1">
                <tr>
                    <td width="50%" valign="top" class="draggedTable1">
                        <div style="height:20px;" class="bgcolor" align="center"><font size="2" style="font-weight:bold;color:#369">ViewBy's</font></div>
                        <div id="viewByData1" style="height:20px;">
                            
                            <div id="viewBysList" style="height:20px;">
                                <label>View bys </label><select id='viewBys' style='font-size:9pt' onchange="changeFilterViewBy(this.value)">
                                    <%
                                        for (int i = 0; i < selectedViewBys.size(); i++) {
                                            if (currentViewBy.equals(selectedViewBys.get(i))) {
                                    %>
                                    <option value="<%=selectedViewBys.get(i)%>" selected><%=selectedViewBys.get(i)%></option>
                                    <%
                                    } else {
                                    %>
                                    <option value="<%=selectedViewBys.get(i)%>"><%=selectedViewBys.get(i)%></option>
                                    <%
                                            }
                                        }
                                    %>
                                </select>
                            </div>
                            <div id="viewByData" style="height:335px;overflow-y: auto">
                            <ul id="ViewByList" class="filetree treeview-famfamfam">
                                <li>
                                    <ul id="viewByDataList">
                                        <%
                                            for (int i = 0; i < (allViewIds.size()<20?allViewIds.size():20); i++) {
                                        %>
                                        <li class="closed">
                                            <table>
                                                <tr>
                                                    <td><img alt=""  src='<%=imgpath%>'/></td>
                                                    <td width="100"><span title="" id="<%=i%>" class="viewBys"><%=allViewIds.get(i)%></span></td>
                                                    <td title="<bean:message bundle="Tooltips" key="changeViewBy.rowViewBy"/>">
                                                    </td>
                                                    <td title="<bean:message bundle="Tooltips" key="changeViewBy.colViewBy"/>" style="display:none"><img src='<%=imgpathCol%>' onclick="createViewBY('<%=i%>','<%=allViewIds.get(i)%>','colViewUL')"/></td>
                                                </tr>
                                            </table>
                                        </li>
                                        <%
                                            }
                                        %>
                                    </ul>
                                </li>
                            </ul>
                            </div>
                        </div>
                    </td>
                    <td width="50%" valign="top">
                        <table width="100%">
<!--                            <tr style="height:50%">
                                <td>
                                    <div style="height:20px;" align="center" class="bgcolor"><font size="2" style="font-weight:bold;">Parent View By</font></div>
                                    <div style="height:50px;overflow-y:auto" id="RowViewBy">
                                        <ul id="rowViewUL" class="sortable">
                                            <%=parentViewBy%>
                                        </ul>
                                    </div>
                                </td>
                            </tr>-->
                            <tr style="height:50%">
                                <td>
                                    <div style="height:20px;" align="center" class="bgcolor"><font size="2" style="font-weight:bold;">Child View Bys</font></div>
                                    <div style="height:355px;overflow-y:auto;border: 1px dotted rgb(228, 228, 228);box-shadow: 1px 0px 0px 2px rgb(228, 228, 228);" id="RowMeasBy">
                                        <ul id="rowMeasUL" class="sortable">
                                            <%=childViewBys%>
                                        </ul>
                                    </div>
                                </td>
                            </tr>
<!--                            <tr style="height:50%">
                                <td>
                                    <div style="height:20px;" align="center" class="bgcolor"><font size="2" style="font-weight:bold;">Child Measures</font></div>
                                    <div style="height:130px;overflow-y:auto" id="RowMeasBy1">
                                        <ul id="rowMeasUL1" class="sortable">
                                            <%=childMeasBys%>
                                        </ul>
                                    </div>
                                </td>
                            </tr>-->
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
                        <input id="ChangeViewbyButton" type="button" class="navtitle-hover" style="width:auto" value="Done" onclick="buildcharts()">
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