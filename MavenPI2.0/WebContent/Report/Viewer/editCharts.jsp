<%@page contentType="text/html" pageEncoding="UTF-8" import="com.progen.reportdesigner.db.ReportTemplateDAO,com.progen.reportdesigner.db.ReportTemplateDAO,java.util.List,com.progen.i18n.TranslaterHelper,java.util.Locale"%>
<%@page import ="java.util.ArrayList,java.util.HashMap,prg.db.Session,prg.db.Container,com.progen.reportview.bd.PbReportViewerBD,com.progen.reportview.db.PbReportViewerDAO,com.progen.report.PbReportCollection,com.progen.report.PbReportRequestParameter"%>
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
//                PbReportViewerBD reportViewBD = new PbReportViewerBD();
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
                String fromoneview = "false";
                String oneviewname = "";
                String rowViewListSTR = "";
                String rowViewNamesListSTR = "";
                String rowMeasListSTR = "";
                String rowMeasNamesListSTR = "";
                String colViewListSTR = "";
                String colViewNamesListSTR = "";
                String rowViewByStr = "";
                int rowViewByStrcount=0;
                String colViewByStr = "";
                String aggTypeString = "";
                 String imgpathRow = request.getContextPath() + "/icons pinvoke/arrow-curve.png";
                String imgpathCol = request.getContextPath() + "/icons pinvoke/arrow-curve-270.png";
                String imgpath = request.getContextPath() + "/icons pinvoke/tick-small.png";
                String delimgpath = request.getContextPath() + "/icons pinvoke/cross-small.png";
                String reportId = request.getParameter("REPORTID");
                String ctxPath = request.getParameter("ctxPath");
                String from=request.getParameter("from");
                if(session.getAttribute("fromoneview")!=null){
               fromoneview = (String)session.getAttribute("fromoneview");
                 oneviewname = (String)session.getAttribute("oneviewname");
               }
              //  added by manik for grid
            //    String grid = request.getParameter("grid");
                String themeColor="blue";
                String graphFlag = "Graph";
                String IsInnerView = "N";
                IsInnerView = request.getParameter("IsInnerView");
                if(IsInnerView==null){
                    IsInnerView = "N";
                }

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
                if (session.getAttribute("rowViewIdList") != null&& fromoneview!=null && fromoneview.equalsIgnoreCase("false")) {
                    rowViewIdList = (List<String>) session.getAttribute("rowViewIdList");
                    rowNamesLst = (List<String>) session.getAttribute("rowNamesLst");
                    rowMeasIdList = (List<String>) session.getAttribute("rowMeasIdList");
                    rowMeasNamesLst = (List<String>) session.getAttribute("rowMeasNamesLst");
                    Container container = null;
                    container = Container.getContainerFromSession(request, reportId);
                    String fileLocation ="";
            if(session != null){
            fileLocation = reportViewDAO.getFilePath(session);
        }else {
               fileLocation = "/usr/local/cache";
            }
                    if(request.getParameter("from")!=null && request.getParameter("from")!="" && request.getParameter("from").equalsIgnoreCase("viewer")){
                        rowViewByStr = reportViewDAO.buildDragableViewBy(reportId,"RowViewBy", ctxPath,container,"Local",graphFlag);
                        colViewByStr = reportViewDAO.buildDragableMeasBy(reportId,"ColViewBy", ctxPath,container,"Local",graphFlag);
                    }
                    else if(request.getParameter("from")!=null && request.getParameter("from")!="" && request.getParameter("from").equalsIgnoreCase("paramsequnce")){
                        rowViewByStr = reportViewDAO.buildDragableViewBy(reportId,"RowViewBy", ctxPath,container,"paramsequnce",graphFlag);

                    }
                    else if(request.getParameter("from")!=null && request.getParameter("from")!="" && request.getParameter("from").equalsIgnoreCase("isEdit")){
                        rowViewByStr = reportViewDAO.buildDragableViewBy(reportId,"RowViewBy", ctxPath,container,"Local",graphFlag);
                        colViewByStr = reportViewDAO.buildDragableMeasBy(reportId,"ColViewBy", ctxPath,container,"Local",graphFlag);
                    }
                    else if(request.getParameter("chartId")!=null && !request.getParameter("chartId").equalsIgnoreCase("undefined")){
                    rowViewByStr = reportViewDAO.buildDragableViewBy(reportId,"RowViewBy", ctxPath,container,request.getParameter("chartId"),graphFlag);
                    colViewByStr = reportViewDAO.buildDragableMeasBy(reportId,"ColViewBy", ctxPath,container,request.getParameter("chartId"),graphFlag);
                    }
                    else{
                    rowViewByStr = reportViewDAO.buildDragableViewByObject(reportId,container,"RowViewBy",ctxPath,fileLocation);
                    colViewByStr = reportViewDAO.buildDragableMeasByObject(reportId,container,"ColViewBy", ctxPath,fileLocation);
                    }
                    rowViewByStrcount=container.getCountOfviewbys();
                } else {
                    rowViewIdList = new ArrayList<String>();
                    rowMeasIdList = new ArrayList<String>();
                    rowNamesLst = new ArrayList<String>();
                    rowMeasNamesLst = new ArrayList<String>();
                }
                ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();
              if(fromoneview!=null && !fromoneview.equalsIgnoreCase("false")) {
                request.setAttribute("session",session);
                session.setAttribute("oneviewglobal","true");
                session.setAttribute("ctxPath",ctxPath);
                session.removeAttribute("fromoneview");
                session.removeAttribute("allViewIds");
                session.removeAttribute("allViewNames");

                  rowViewByStr=reportTemplateDAO.getParameterForOneView(reportId,session);

  rowViewIdList = (List<String>) session.getAttribute("rowViewIdList");
                    rowNamesLst = (List<String>) session.getAttribute("rowNamesLst");
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
        <input type="hidden" id="reportId" name="reportId" value="<%=reportId%>">
        <input type="hidden" id="ctxPath" name="ctxPath" value="<%=ctxPath%>">
        <form name="ViewByForm" id="ViewByForm" method="post">
           <%if(IsInnerView.equalsIgnoreCase("Y")){%>
            <table width="100%"><tr>
                 <td><%=TranslaterHelper.getTranslatedInLocale("Inner_ViewBy", cL)%> : </td><td><select id='InnerViewbyEleId' name='InnerViewbyEleId'>
                         <option value="NONE">NONE</option>
                 <%
                for (int i = 0; i < allViewIds.size(); i++) {
                    if(!allViewIds.get(i).equalsIgnoreCase("TIME") && !allViewIds.get(i).contains("A_")){
                %>
                 <option value="<%=allViewIds.get(i)%>"><%=allViewNames.get(i)%></option>
                 <%}}%>
               </select>
<!--                 </td></tr>-->
<!--            </table>-->
               <%}%>
<!--              <table><tr>-->
                      <td><%=TranslaterHelper.getTranslatedInLocale("Graph_Object_On", cL)%> :</td><td><select id="goUpdate" name="goUpdate">

                              <option value="Complete Data Set">Complete Time Duration</option>
                              <option value="Calender Data Set">Report Time Duration</option>
                              <option value="3_Month">3 Month Time Duration</option>
                              <option value="6_Month">6 Month Time Duration</option>
                              <option value="1_Year">1 Year Time Duration</option>
                              <option value="2_Year">2 Year Time Duration</option>
                          </select></td>
                </tr></table>

            <table style="width:100%;height:300px;" border="1">
                <tr>
                      <% if(from!=null && from.equalsIgnoreCase("paramsequnce")){%>

    <%}else{%>


                    <td width="50%" valign="top" class="draggedTable1">
                        <div style="height:20px;" class="bgcolor" align="center"><font size="2" style="font-weight:bold;color:gray;"><%=TranslaterHelper.getTranslatedInLocale("ViewBy_s", cL)%></font></div>
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
                                            <td width="100"><span title="" id="<%=allViewIds.get(i)%>" class="viewBys"><%=allViewNames.get(i)%></span></td>
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
                     <%}%>
                    <td width="50%" valign="top">
                        <table width="100%">
                            <tr style="height:50%">
                                <td>

                                            <% if(from!=null && from.equalsIgnoreCase("paramsequnce")){%>
                                            <div  style="height:130px;overflow-y:auto;border: 1px dotted rgb(228, 228, 228);box-shadow: 1px 0px 0px 2px rgb(228, 228, 228);" id="RowViewBy">
                                            <%}else{%>
                                    <div style="height:20px;" align="center" class="bgcolor"><table width="100%"><tr><td style="width:20%;font-weight:bold; color: gray;" align="center"><%=TranslaterHelper.getTranslatedInLocale("ViewBys", cL)%></td>
                                                <td id="rowViewByStrcount" style="font-weight:bold;color:black"><font>(<%=rowViewByStrcount%>)</font></td>
                                            </tr></table>
                                        </div>
                                    <div  style="height:130px;overflow-y:auto;border: 1px dotted rgb(228, 228, 228);box-shadow: 1px 0px 0px 2px rgb(228, 228, 228);" id="RowViewBy">
                                        <%}%>
                                        <ul id="rowViewUL" class="sortable">
                                            <% if(fromoneview!=null && !fromoneview.equalsIgnoreCase("false")){

                                                }else{
                                            if(request.getParameter("from")==null){
                                               if(request.getParameter("editFlag").equals("add1")){
                                                    rowViewByStr="";
                                               }
                                            }
                                            }

                                            %>
                                            <%=rowViewByStr%>
                                        </ul>
                                    </div>
                                </td>
                            </tr>
                            <% if((fromoneview!=null && !fromoneview.equalsIgnoreCase("false"))||(from!=null && from.equalsIgnoreCase("paramsequnce"))){

                                }else{
%>
                            <tr style="height:50%">
                                <td>
                                    <div style="height:20px;" align="center" class="bgcolor"><font size="2" style="font-weight:bold;color: gray;"><%=TranslaterHelper.getTranslatedInLocale("Measures", cL)%></font></div>
                                    <div style="height:130px;overflow-y:auto;border: 1px dotted rgb(228, 228, 228);box-shadow: 1px 0px 0px 2px rgb(228, 228, 228);" id="RowMeasBy">
                                        <ul id="rowMeasUL" class="sortable">
                                            <%
                                            if(request.getParameter("from")==null){
                                               if(request.getParameter("editFlag").equals("add1")){
                                                    colViewByStr="";
                                               }
                                            }
                                            %>

                                            <%=colViewByStr%>
                                        </ul>
                                    </div>
                                </td>
                            </tr>
                            <tr style="height:00%;display:none">
                                <td>
                                    <div style="height:20px;" align="center" class="bgcolor"><font size="2" style="font-weight:bold;color: gray;"><%=TranslaterHelper.getTranslatedInLocale("Column_ViewBys", cL)%></font></div>
                                    <div  style="height:130px;overflow-y:auto;border: 1px dotted rgb(228, 228, 228);box-shadow: 1px 0px 0px 2px rgb(228, 228, 228);" id="ColViewBy">
                                        <ul id="colViewUL" class="sortable">
                                        </ul>
                                    </div>
                                </td>
                            </tr>
                            <%}%>
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
                      <%if(fromoneview!=null && !fromoneview.equalsIgnoreCase("false")){%>
                        <input id="ChangeViewbyButton" type="button" class="navtitle-hover" style="width:auto" value="<%=TranslaterHelper.getTranslatedInLocale("done", cL)%>" onclick="getglobalfilter('<%=reportId%>','<%=oneviewname%>')">
                        <%}else if((request.getParameter("from")!=null && !request.getParameter("from").equalsIgnoreCase("undefined")) && request.getParameter("from").equalsIgnoreCase("viewer")){ %>
                        <input id="ChangeViewbyButton" type="button" class="navtitle-hover" style="width:auto" value="<%=TranslaterHelper.getTranslatedInLocale("done", cL)%>" onclick="buildcharts()">
                       <%}else if((request.getParameter("from")!=null && !request.getParameter("from").equalsIgnoreCase("undefined")) && request.getParameter("from").equalsIgnoreCase("isEdit")){%>
                        <input id="ChangeViewbyButton" type="button" class="navtitle-hover" style="width:auto" value="<%=TranslaterHelper.getTranslatedInLocale("done", cL)%>" onclick="buildEditCharts()">
                        <%}else if((request.getParameter("from")!=null && !request.getParameter("from").equalsIgnoreCase("undefined")) && request.getParameter("from").equalsIgnoreCase("paramsequnce")){%>
                        <input id="ChangeViewbyButton" type="button" class="navtitle-hover" style="width:auto" value="<%=TranslaterHelper.getTranslatedInLocale("done", cL)%>" onclick="changeSequenceparam()">
                        <%}else if(request.getParameter("chartId")==null || request.getParameter("chartId").equalsIgnoreCase("undefined")){%>
                        <input id="ChangeViewbyButton" type="button" class="navtitle-hover" style="width:auto" value="<%=TranslaterHelper.getTranslatedInLocale("done", cL)%>" onclick="saveEditSingleChart()">
                        <%}else{%>
                        <input id="ChangeViewbyButton" type="button" class="navtitle-hover" style="width:auto" value="<%=TranslaterHelper.getTranslatedInLocale("done", cL)%>" onclick="saveEditSingleChart1('<%=request.getParameter("chartId")%>')">
                        <% } %>
                    </td>
                </tr>
            </table>
        </form>
                     <script type="text/javascript">
           var ViewByArray=new Array();
           var finalrowViewBycount="";
            var MeasByArray=new Array();
            <% if (rowMeasNamesLst != null && rowMeasNamesLst.size() != 0) {
                 for(int i = 0; i < rowMeasNamesLst.size(); i++) {
                     if(aggTypeString == "" && i==0){
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
                 <% if(fromoneview!=null && !fromoneview.equalsIgnoreCase("false")) {%>
                 rowViewNamesArr = rowNamesStr.split(",");
                  <%}%>

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
//                  if(parent.document.getElementById("Designer").value=="fromDesigner"){
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
                    try{
//                    From=parent.document.getElementById("Designer").value;
                    if(From=="fromDesigner"){
                        $("#ChangeViewbyButton").val("Next")
                        //alert("automatic assigment"); //added by mohit for create report
                        //<//%rowViewByStr="";colViewByStr=""; %>
                        createViewBY('<%=allViewIds.get(0)%>','<%=allViewNames.get(0)%>','rowViewUL')
                                              saveViewBy()

                    }
                    }catch(e){}
                    <%if(fromoneview!=null && !fromoneview.equalsIgnoreCase("false")) {%>


                    <%}%>
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
                $("rowViewByStrcount").hide();
                     var InnerViewbyEleId=$("#InnerViewbyEleId").val();
                     var goUpdate=$("#goUpdate").val();
                   var reportId = "";
                   try{
                       
                reportId  = document.getElementById("reportId").value;
                   }catch(e){
                     reportId = parent.$("#graphsId").val();  
                   }
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
//                    alert(colIds.length);
                    
                    if(colIds!=null && colIds.length!=0){
                        rowViewNamesArr = [];
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
//                        AggType.push(aggTypeListName[k]);
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
//                    var oldViewBys=[];
//                    oldViewBys.push(JSON.parse(parent.$("#viewby").val()));
//                    oldViewBys.concat(rowViewNamesArr);
//                    alert(rowViewNamesArr);
//
//
//                    var tempViewBys=[];
//                    tempViewBys.push(JSON.parse(parent.$("#viewby").val()));
//                    var finalViewBys=tempViewBys.concat(rowViewNamesArr);
//                    alert(finalViewBys);
//                        var arr = $('#rowViewUL td').map(function(){
//                        return $(this).text();
//                        }).get();
                    var finalViewBys = []
                    var tableUl = document.getElementById("rowViewUL");
                    var cells = tableUl.getElementsByTagName("td");
                    for (var i = 0; i < cells.length; i++) {
                        if(cells[i].textContent.trim()!==''){
                            finalViewBys .push( cells[i].textContent.trim());
                        }
                    }
                    var finalMeas = []
                    tableUl = document.getElementById("rowMeasUL");
                    cells = tableUl.getElementsByTagName("td");
                    for (var i = 0; i < cells.length; i++) {
                        if(cells[i].textContent.trim()!==''){
                            finalMeas .push( cells[i].textContent.trim());
                        }
                    }
                    for( var j=0;j<finalMeas.length;j++){
                    for( var k=0;k<rowMeasNamesArr.length;k++){

                    if(rowMeasNamesArr[k].trim()==finalMeas[j].trim()){
                        AggType.push(aggTypeListName[k]);
                    }

                    }
                    }
                   
//                    $('ul li').find('td').each(function() {
//                        values.push($(this).html());
//                    });
//                    alert(rowMeasArray);
                    var rowMeasArr=[]
                    for(var i=0;i<rowMeasArray.length;i++){
                        rowMeasArr[i]=rowMeasArray[i].replace("A_","","gi");
                    }
                    parent.$("#viewby").val(JSON.stringify(finalViewBys ));
//                    alert(JSON.stringify(parent.$("#viewby").val()));
                    parent.$("#viewbyIds").val(JSON.stringify(rowViewByArray));
//                    parent.$("#measure").val(JSON.stringify(rowMeasNamesArr));
                    parent.$("#measure").val(JSON.stringify(finalMeas));
                    parent.$("#measureIds").val(JSON.stringify(rowMeasArr));
                    parent.$("#aggregation").val(JSON.stringify(AggType));
                    
                    var userId = parent.$("#userId").val();
                     parent.$("#loading").show();
                      $.post(ctxpath+"/reportViewer.do?reportBy=addNewChartsUI&InnerViewbyEleId="+InnerViewbyEleId+"&rowViewByArray="+encodeURIComponent(rowViewByArray)+"&reportId="+reportId+"&userId="+userId+"&rowViewNamesArr="+encodeURIComponent(finalViewBys)+"&rowMeasArray="+encodeURIComponent(rowMeasArray)+"&AggType="+encodeURIComponent(AggType)+"&rowMeasNamesArr="+JSON.stringify(encodeURIComponent(finalMeas))+"&reportName="+parent.$("#graphName").val()+"&goUpdate="+goUpdate,parent.$("#graphForm").serialize(),
                             function(data){
                                 parent.$("#loading").hide();
//                                added by manik
//                                parent.generateJsonData(reportId,"Graph",grid);

                                parent.generateJsonData(reportId);
//                                 parent.generateJsonData(reportId);
                                 alert("Graph object created");
                                
                            }

                )

                }
                 parent.$("#editViewByDiv").dialog('close');
                }


               function buildcharts(){
                 parent.$("#loading").show();
                 $("rowViewByStrcount").show();
                 $("#driver").val("");
                  parent.$("#filters1").val("");
                  parent.$("#drillFormat").val("");
                  
//                    alert("viewBy "+parent.$("#viewby").val())
                    var viewOvName = JSON.parse(parent.$("#viewby").val());
//                     var viewOvName = JSON.parse($("#viewby").val());
    var viewOvIds = JSON.parse(parent.$("#viewbyIds").val());
    var measName = JSON.parse(parent.$("#measure").val());
    var measIds = JSON.parse(parent.$("#measureIds").val());
    var aggregNames = JSON.parse(parent.$("#aggregation").val());
                     var reportId = "";
                   try{
                       
                reportId  = document.getElementById("reportId").value;
                   }catch(e){
                     reportId = parent.$("#graphsId").val();  
                   }
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

for(var g=0;g<rowMeasArray.length;g++){
            MeasBy.push(measName[measIds.indexOf(rowMeasArray[g])]);
            AggType.push(aggregNames[measIds.indexOf(rowMeasArray[g])]);
//            AggType.push(aggTypeListName[g])
        }
        var draggableViewBys=[];

                    for(var i=0;i<rowViewByArray.length;i++){
                        var chartDetails={};
                        var viewBys=[];
                        var viewIds=[];
                        var meassures=[];
                        
                        viewIds.push(rowViewByArray[i]);
//                        for(var m=0;m<viewIds.length;m++){
                            viewBys.push(viewOvName[viewOvIds.indexOf(rowViewByArray[i])]);
//                        }
                         draggableViewBys.push(viewOvName[viewOvIds.indexOf(rowViewByArray[i])]);
                          chartDetails["defaultMeasures"] = MeasBy;
                            chartDetails["defaultMeasureIds"] = rowMeasArray;
                        chartDetails["viewBys"] = viewBys;
                            chartDetails["chartType"] = "Pie";
                            chartDetails["viewByLevel"] = "single";
                            chartDetails["meassures"] = MeasBy;
                            chartDetails["aggregation"] = AggType;
                            chartDetails["viewIds"] = viewIds;
                            chartDetails["dimensions"] = viewIds;
                            chartDetails["meassureIds"] = rowMeasArray;
                            chartDetails["records"] = "12";
                            chartDetails["size"] = "S";
                            chartDetails["othersL"]="N";
                            chartData["chart" + (i + 1)] = chartDetails;
                            
                    }
//                    var viewIds = JSON.parse(parent.$("viewbyIds").val());
//                    var viewName = JSON.parse(parent.$("viewby").val());
//var chartDataKey = chartData;
//for(var keys in chartData){
//    var views=[];
//    var meas=[];
//    for(var viewKeys in chartData[keys]["viewIds"]){
//        views.push(viewName[viewIds.indexOf(chartData[keys]["viewIds"][viewKeys])]);
//    }
//    for(var measKeys in chartData[keys]["meassureIds"]){
//        meas.push(viewName[viewIds.indexOf(chartData[keys]["meassureIds"][measKeys])]);
//    }
//    chartData[keys]["viewBys"]=views;
//    chartData[keys]["meassures"]=meas;
//}

//                    parent.$("#chartData").val(JSON.stringify(chartData));
//                    parent.$("#viewbyIds").val(JSON.stringify(rowViewNamesArr));
//                    parent.$("#viewby").val(JSON.stringify(rowViewNamesArr));
//                    parent.$("#measure").val(JSON.stringify(MeasBy));
//                    parent.$("#measureIds").val(JSON.stringify(rowMeasNamesArr));
//                    parent.$("#aggregation").val(JSON.stringify(AggType));
                    parent.$("#draggableViewBys").val(JSON.stringify(draggableViewBys));
                    var userId = parent.$("#userId").val();
                    parent.$("#chartData").val(JSON.stringify(chartData));
                   var ctxpath = document.getElementById("ctxPath").value;
                      $.ajax({
                          type:'POST',
                          data:parent.$("#graphForm").serialize(),
                     url: ctxpath+"/reportViewer.do?reportBy=buildchartsWithObject&reportId="+reportId+"&reportName="+encodeURIComponent(parent.$("#graphName").val())+"&chartData="+encodeURIComponent(parent.$("#chartData").val()),
//                     url: ctxpath+"/reportViewer.do?reportBy=buildCharts&reportId="+reportId+"&reportName="+encodeURIComponent(parent.$("#graphName").val())+"&chartData="+parent.$("#chartData").val(),
                            success: function(data){
                                 parent.$("#loading").hide();
//                                 alert(grid+"   lkiyu")
                               parent.generateChart(data);
                            }
                    }
                )
        parent.$("#editViewByDiv").dialog('close');
               }
           }
           function getglobalfilter(oneviewid,oneviewname){
           
                 parent.$("#AddMoreParamsDiv1").dialog('close');
// $("rowViewByStrcount").hide();
//                     var InnerViewbyEleId=$("#InnerViewbyEleId").val();
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
                            rowViewByArray.push(colIds[i].id.replace("ViewBy",""));
                            viewByArray.push(colIds[i].id.replace("ViewBy",""));
                        }

                    }

                    var userId = parent.$("#userId").val();
              parent.$("#globalviewby").val(rowViewNamesArr);
              parent.$("#globalviewbyIds").val(rowViewByArray);
               $.ajax({
                          type:'POST',

                     url: ctxpath+'/reportTemplateAction.do?templateParam=saveGlobalFilterOneview&oneviewId='+oneviewid+'&filterParameterIds='+rowViewByArray+'&filterParameterNames='+rowViewNamesArr,
//                     url: ctxpath+"/reportViewer.do?reportBy=buildCharts&reportId="+reportId+"&reportName="+encodeURIComponent(parent.$("#graphName").val())+"&chartData="+parent.$("#chartData").val(),
                            success: function(data){
parent.$("#filterdata").val(data);
 var dSrc = parent.document.getElementById("regionTableIdgrid");
                        var source="newOneView.jsp?fromopen=gblfilter&fromviewer=true&oneviewname="+oneviewname+"&oneViewIdValue="+oneviewid;

                        dSrc.src = source;
                            }
                    });

//$.ajax({
//        url:ctxpath+'reportTemplateAction.do?templateParam=saveGlobalFilterOneview&oneviewId='+oneviewid+'&filterParameterIds='+rowViewByArray+'&filterParameterNames='+rowViewNamesArr,
//        success:function(data)
//        {alert(data)
//parent.$("#filterdata").val(data);
// var dSrc = parent.document.getElementById("regionTableIdgrid");
//                        var source="newOneView.jsp?fromopen=gblfilter&fromviewer=true&oneviewname="+oneviewname+"&oneViewIdValue="+oneviewid;
//
//                        dSrc.src = source;
//        }});

}
           }
           function changeSequenceparam(){
    var newSequence=[];
//    $('#sortable li').map(function(i,n) {
//        newSequence.push($(n).attr('id'));
//    }).get().join(',');
  var ul = document.getElementById("rowViewUL");
  var ctxpath = document.getElementById("ctxPath").value;
    if(ul!=undefined || ul!=null){
                    var colIds=ul.getElementsByTagName("li");
                    if(colIds!=null && colIds.length!=0){
                        for(var i=0;i<colIds.length;i++){
                         newSequence.push(colIds[i].id.replace("ViewBy",""));
                        }
                    }
                        }
    var newJson = [];
    var newIds = [];
//    var charts = JSON.parse($("#visualChartType").val());
    var viewBy = JSON.parse(parent.$("#viewby").val());
    var viewId = JSON.parse(parent.$("#viewbyIds").val());
//alert(viewBy)
    for(var i=0;i<newSequence.length;i++){
       if(typeof viewBy[newSequence[i]] !=="undefined" && viewBy[newSequence[i]]!=="null"){
        newJson.push(viewBy[newSequence[i]]);
        newIds.push(viewId[newSequence[i]]);
       }
    }
//alert(JSON.stringify(newJson))

//    $("#visualChartType").val(JSON.stringify(newJson));
    parent.$("#viewby").val(JSON.stringify(newJson));
    parent.$("#viewbyIds").val(JSON.stringify(newIds));
     $.ajax({
        async:false,
        type:"POST",
        data:
            parent.$('#graphForm').serialize(),
        url:  ctxpath+'/reportViewer.do?reportBy=saveXtCharts',

        success:function(data) {
             parent.$("#reportSequence").dialog('close');
            alert("Sequence Changed");
//            generateJsonDataReset($("#graphsId").val());
 var reportId  = parent.document.getElementById("reportId").value;
parent.updateorderfilters(reportId);
        }
    });


}
           function buildEditCharts(){
           parent.$("#loading").show();
           $("rowViewByStrcount").show();
           $("#driver").val("");
           
             var viewOvName = JSON.parse(parent.$("#viewby").val());
    var viewOvIds = JSON.parse(parent.$("#viewbyIds").val());
    var measName = JSON.parse(parent.$("#measure").val());
    var measIds = JSON.parse(parent.$("#measureIds").val());
                    var reportId = "";
                   try{
                       
                reportId  = document.getElementById("reportId").value;
                   }catch(e){
                     reportId = parent.$("#graphsId").val();  
                   }
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
var chartData = JSON.parse(parent.$("#chartData").val());
  var charts = Object.keys(chartData);
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

for(var g=0;g<rowMeasArray.length;g++){
            MeasBy.push(measName[measIds.indexOf(rowMeasArray[g])]);
            AggType.push(aggTypeListName[g])
        }
        var draggableViewBys=[];
for(var f in charts){
    draggableViewBys.push(chartData[charts[f]]["viewBys"][0])
}
        
                    for(var i=charts.length;i<rowViewByArray.length;i++){
                        var chartDetails={};
                        var viewBys=[];
                        var viewIds=[];
                        var meassures=[];
                      
                        viewIds.push(rowViewByArray[i]);
//                        for(var m=0;m<viewIds.length;m++){
                            viewBys.push(viewOvName[viewOvIds.indexOf(rowViewByArray[i])]);
//                        }
                        draggableViewBys.push(viewOvName[viewOvIds.indexOf(rowViewByArray[i])]);
                        chartDetails["viewBys"] = viewBys;
                            chartDetails["chartType"] = "Pie";
                            chartDetails["viewByLevel"] = "single";
                            chartDetails["meassures"] = MeasBy;
                            chartDetails["aggregation"] = AggType;
                            chartDetails["viewIds"] = viewIds;
                            chartDetails["meassureIds"] = rowMeasArray;
                            chartDetails["records"] = "12";
                            chartDetails["size"] = "S";
                            chartDetails["othersL"]="N";
                            chartData["chart" + (i + 1)] = chartDetails;
                            
                    parent.$("#chartData").val(JSON.stringify(chartData));
                    }
                   
                    var newChart = Object.keys(JSON.parse(parent.$("#chartData").val()));
        
              for(var chart=charts; charts<newChart.length;charts++){
                         chartData["chart" + (chart + 1)]["meassures"] = MeasBy
                    }
                    
//                    var viewIds = JSON.parse(parent.$("viewbyIds").val());
//                    var viewName = JSON.parse(parent.$("viewby").val());
//var chartDataKey = chartData;
//for(var keys in chartData){
//    var views=[];
//    var meas=[];
//    for(var viewKeys in chartData[keys]["viewIds"]){
//        views.push(viewName[viewIds.indexOf(chartData[keys]["viewIds"][viewKeys])]);
//    }
//    for(var measKeys in chartData[keys]["meassureIds"]){
//        meas.push(viewName[viewIds.indexOf(chartData[keys]["meassureIds"][measKeys])]);
//    }
//    chartData[keys]["viewBys"]=views;
//    chartData[keys]["meassures"]=meas;
//}

//                    parent.$("#chartData").val(JSON.stringify(chartData));
//                    parent.$("#viewbyIds").val(JSON.stringify(rowViewNamesArr));
//                    parent.$("#viewby").val(JSON.stringify(rowViewNamesArr));
//                    parent.$("#measure").val(JSON.stringify(MeasBy));
//                    parent.$("#measureIds").val(JSON.stringify(rowMeasNamesArr));
//                    parent.$("#aggregation").val(JSON.stringify(AggType));
                    var userId = parent.$("#userId").val();
                     parent.$("#draggableViewBys").val(JSON.stringify(draggableViewBys));
                    parent.$("#chartData").val(JSON.stringify(chartData));
                   var ctxpath = document.getElementById("ctxPath").value;
                      $.ajax({
                          type:'POST',
                          data:parent.$("#graphForm").serialize() +"&reportId="+reportId+"&reportName="+encodeURIComponent(parent.$("#graphName").val())+"&chartData="+parent.$("#chartData").val()+"",
                     url: ctxpath+"/reportViewer.do?reportBy=buildchartsWithObject",
//                     url: ctxpath+"/reportViewer.do?reportBy=buildCharts&reportId="+reportId+"&reportName="+encodeURIComponent(parent.$("#graphName").val())+"&chartData="+parent.$("#chartData").val(),
                            success: function(data){
                                 parent.$("#loading").hide();
//                               parent.generateChart(data);
                               parent.generateChart(data);
                            }
                    }
                )
        parent.$("#editViewByDiv").dialog('close');
           }
           }
           
//                function saveViewBy(){
//                var From="";
//                if(parent.document.getElementById("Designer") != null)
//                From=parent.document.getElementById("Designer").value;
//
//                    var reportId  = document.getElementById("reportId").value;
//                    var ctxpath = document.getElementById("ctxPath").value;
//                    var count=0;
//                    var viewByArray=new Array();
//                    var rowViewByArray=new Array();
//                    var colViewByArray=new Array();
//
//                     var ul = document.getElementById("rowViewUL");
//                    if(ul!=undefined || ul!=null){
//                    var colIds=ul.getElementsByTagName("li");
//                    if(colIds!=null && colIds.length!=0){
//                        for(var i=0;i<colIds.length;i++){
//                            //cols = cols+","+colIds[i].id.replace("GrpCol","");
//                            rowViewByArray.push(colIds[i].id.replace("ViewBy",""));
//                            viewByArray.push(colIds[i].id.replace("ViewBy",""));
//                        }
//
//                    }
//                }
//                         for(var i=0;i<rowViewByArray.length;i++){
//                             for(var j=i+1;j<rowViewByArray.length;j++){
//                                 if(rowViewByArray[i]==rowViewByArray[j])
//                                     count =count+1;
//                             }
//                         }
//
//                    var colviewByUl = document.getElementById("colViewUL");
//                    if(colviewByUl!=undefined || colviewByUl!=null){
//                    var colIds=colviewByUl.getElementsByTagName("li");
//                    if(colIds!=null && colIds.length!=0){
//                        for(var i=0;i<colIds.length;i++){
//                            //cols = cols+","+colIds[i].id.replace("GrpCol","");
//                            colViewByArray.push(colIds[i].id.replace("ViewBy",""));
//                            viewByArray.push(colIds[i].id.replace("ViewBy",""));
//                        }
//
//                    }
//                }
//                         for(var i=0;i<colViewByArray.length;i++){
//                            for(var j=0;j<rowViewByArray.length;j++){
//                                 if(colViewByArray[i]==rowViewByArray[j])
//                                     count =count+1;
//                             }
//                         }
//                    if(colViewByArray.length == 0 && rowViewByArray.length == 0){
//                        alert("Please Select Atleast one Row Viewby")
//                    }else if(count>0){
//                        alert("Please Select different Row Viewby and Col Viewby")
//                    }
//                    else if(rowViewByArray.length>0 ){
//                         if(From!="fromDesigner"){
//                 $("#editViewByDiv").dialog('close')
//                 parent.$("#editViewByDiv").dialog('close')
//                   parent.document.getElementById('loading').style.display='';
//             }
////                        $.ajax({
////                            url: ctxpath+"/reportViewer.do?reportBy=changeViewBy&ViewByArray="+ViewByArray+"&RowViewByArray="+RowViewByArray+"&ColViewByArray="+ColViewByArray+"&ChangeView=ChangeViewBy&reportId="+reportId+"&ctxpath="+ctxpath+"&rowViewNamesArr="+rowViewNamesArr+"&colViewNamesArr="+colViewNamesArr,
////                            success: function(data){
////                                submitChangeViewBy();
////                            }
////                        });
//
//                            if(From=="fromDesigner"){
//                                $.post(ctxpath+"/reportTemplateAction.do?templateParam=designerViewbys&ViewByArray="+viewByArray+"&RowViewByArray="+rowViewByArray+"&ColViewByArray="+colViewByArray+"&ChangeView=ChangeViewBy&reportId="+reportId+"&ctxpath="+ctxpath+"&rowViewNamesArr="+rowViewNamesArr+"&colViewNamesArr="+colViewNamesArr, $("#ViewByForm").serialize(),
//                             function(data){//
//                               parent.$("#editViewByDiv").dialog('close');
//                               parent.$("#paramDesign").hide();
//                    var prevREPIds=parent.document.getElementById("REPIds").value
//                    var frameObj=parent.document.getElementById("dataDispmem");
//                    var prevCEPIds=parent.document.getElementById("CEPIds").value
//                    var roleid=parent.document.getElementById("roleid").value
//                    var RepIdsArray = new Array()
//                    var CepIdsArray = new Array()
//                    RepIdsArray = prevREPIds.split(",")
//                    CepIdsArray = prevCEPIds.split(",")
//                    parent.document.getElementById("REPIds").value = rowViewByArray;
//                    parent.document.getElementById("CEPIds").value = colViewByArray;
//                    var flag = 0;
//                    for(var i=0;i<rowViewByArray.length;i++){
//                        for(var j=0;j<colViewByArray.length;j++){
//                            if(RepIdsArray[i] == CepIdsArray[j]){
//                                flag=flag+1;
//                            }
//                        }
//                    }
//                    if(rowViewByArray!=""){
//                        if(flag==0){
//                            parent.$("#measuresDialog").dialog('open');
//                            var source="reportTemplateAction.do?templateParam=getMeasures&foldersIds="+roleid+'&REPORTID='+parent.document.getElementById("REPORTID").value;
//                            frameObj.src=source;
//                        }else{
//                            alert("Please select different Row Edge and Col Edge")
//                        }
//
//                    }
//                    else{
//                        if(rowViewByArray=="" || prevREPIds==undefined ){
//                            alert("Please select Row Edge ")
//                        }
//                    }
//
//                        });
//                            }else{
//                         $.post(ctxpath+"/reportViewer.do?reportBy=changeViewBy&ViewByArray="+viewByArray+"&RowViewByArray="+rowViewByArray+"&ColViewByArray="+colViewByArray+"&ChangeView=ChangeViewBy&reportId="+reportId+"&ctxpath="+ctxpath+"&rowViewNamesArr="+rowViewNamesArr+"&colViewNamesArr="+colViewNamesArr, $("#ViewByForm").serialize(),
//                             function(data){
//                                submitChangeViewBy();
//                        });
//                    }
//                }
//                }
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
//                     if(x.match(elmntId)==null){
//                        deleteColumn('MeasBy'+elmntId,'rowViewUL',elementName)
//                    }
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

                     var ul1 = document.getElementById("rowViewUL");
                    if(ul1!=undefined || ul1!=null){
                    var colIds1=ul1.getElementsByTagName("li");
                    }
//                      if(x.match(elmntId)==null){
var rowViewByStrcount1=colIds1.length;
                            ViewByArray.push(elmntId);
                            RowViewByArray.push(elmntId);
                            rowViewByStrcount1++;
                            
                            var html="";
                           html +="<font>("+rowViewByStrcount1+")</font>";
                           finalrowViewBycount=rowViewByStrcount1;
                           $("#rowViewByStrcount").html(html);
                           
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
                if('<%=request.getParameter("from")%>'==='null' && '<%=request.getParameter("editFlag")%>'=='edit1'){
                    var chartData = JSON.parse(parent.$("#chartData").val());
                   var charts=Object.keys(chartData);
                   for(var key in charts){
                       if(chartData[charts[key]]["viewBys"].indexOf(name)!=-1){
                           alert("Unable to delete! Chart is created on selected View By");
                           return false;
                       }
                   }
                   for(var key in charts){
                       if(chartData[charts[key]]["meassures"].indexOf(name)!=-1){
                           alert("Unable to delete! Chart is created on selected Measure");
                           return false;
                       }
                   }
                   }
                var ul1 = document.getElementById("rowViewUL");
                    if(ul1!=undefined || ul1!=null){
                    var colIds1=ul1.getElementsByTagName("li");
                    }
                    var rowViewByStrcount1=colIds1.length-1;
                    var LiObj=document.getElementById(index);
//                    finalrowViewBycount--;
                    var html="";
                    html +="<font>("+rowViewByStrcount1+")</font>";
                           $("#rowViewByStrcount").html(html);
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
    var viewOvName = JSON.parse(parent.$("#viewby").val());
    var viewOvIds = JSON.parse(parent.$("#viewbyIds").val());
    var measName = JSON.parse(parent.$("#measure").val());
    var defaultMeasure;
    var defaultMeasureId;
   try{
   defaultMeasure = JSON.parse(parent.$("#defaultMeasure").val());
    defaultMeasureId = JSON.parse(parent.$("#defaultMeasureId").val());
    }catch(e){}
    var measIds = JSON.parse(parent.$("#measureIds").val());
                 var reportId = "";
                 try{
                     
                 reportId  = parent.document.getElementById("reportId").value;
                 }catch(e){
                     reportId = parent.$("#graphsId").val();
                 }
                 
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
    parent.$("#editViewByDiv").dialog('close');
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
                        var columnId =[];
                        var columnBys = [];
                        viewIds.push(rowViewNamesArr[0]);
                        if(chartData[chartId]["chartType"]=="Trend-Combo"){
                            chartData[chartId]["ParentMeasures"] = [];
                        }
                         if(chartData[chartId]["chartType"]=="Split-Bubble" || chartData[chartId]["chartType"]=="Scatter-Bubble" || chartData[chartId]["chartType"]=="Grouped-Bar" || chartData[chartId]["chartType"]=="GroupedHorizontal-Bar" || chartData[chartId]["chartType"]=="Grouped-Map" || chartData[chartId]["chartType"]=="Grouped-Table" || chartData[chartId]["chartType"]=="Grouped-MultiMeasureBar" || chartData[chartId]["chartType"]=="Multi-View-Bubble"  || chartData[chartId]["chartType"]=="Multi-View-Tree"  || chartData[chartId]["chartType"]=="Grouped-Line" || chartData[chartId]["chartType"]=="GroupedStackedH-Bar" || chartData[chartId]["chartType"]=="GroupedStacked-Bar" || chartData[chartId]["chartType"] == "GroupedStacked-BarLine" || chartData[chartId]["chartType"] == "GroupedStacked-Bar%" || chartData[chartId]["chartType"] == "GroupedStackedH-Bar%"  ||(chartData[chartId]["chartType"]=="Trend-Combo" && typeof chartData[chartId]["trendViewMeasures"]!="undefined" && typeof chartData[chartId]["trendViewMeasures"]!="Measures" )){
                       viewIds.push(rowViewNamesArr[1]);
                         }
                         if(chartData[chartId]["chartType"]=="Cross-Table"){
                            columnId.push(rowViewNamesArr[1]); 
                         }
                         if(chartData[chartId]["chartType"]=="Table" || chartData[chartId]["chartType"]=="Scatter-Analysis" || chartData[chartId]["chartType"]=="Bar-Table"
                             ||chartData[chartId]["chartType"]== "Combo-Analysis" 
                             || chartData[chartId]["chartType"]=="Transpose-Table"  
                             || (typeof chartData[chartId]["tableWithSymbol"]!=='undefined' 
                             && chartData[chartId]["tableWithSymbol"]==='Y' && chartData[chartId]["chartType"]==='Horizontal-Bar')){
                             viewIds =[];
                             if(rowViewNamesArr.length > 0){
                          for(var n=0;n<rowViewNamesArr.length;n++){
                              viewIds.push(rowViewNamesArr[n]);
                          }
                          }
                          
                         }
                         for(var m=0;m<viewIds.length;m++){
                            viewBys.push(viewOvName[viewOvIds.indexOf(viewIds[m])])
                        }
                        if(chartData[chartId]["chartType"]=="Cross-Table"){
                        for(var j=0;j<columnId.length;j++){
                             columnBys.push(viewOvName[viewOvIds.indexOf(columnId[j])])
                        }
                        }
                        MeasBy=[];
                         for(var m=0;m<meassureIds.length;m++){
                            MeasBy.push(measName[measIds.indexOf(meassureIds[m])])
                        }
//                         if(chartData[chartId]["chartType"]=="Grouped-Bar"){
//                             viewBys.push(viewOvName[viewOvIds.indexOf(viewIds[1])])
//                        }
//            if(chartData[chartId]["chartType"]=="KPI-Dashboard"){
                var measures=chartData[chartId]["meassures"];
                var comparedMeasure = chartData[chartId]["comparedMeasure"];
                var comparedMeasureId = chartData[chartId]["comparedMeasureId"];
                var measureIds=[];
                try{
                for(var i in measures){
                    measureIds.push(defaultMeasureId[defaultMeasure.indexOf(measures[i])]);
                }
                if(typeof comparedMeasureId!=="undefined"){
                    for(var i=0;i<measureIds.length;i++){
                        if(measures.indexOf(comparedMeasure[i])!==-1){
                            measures.splice(measures.indexOf(comparedMeasure[i]),1);
                            measureIds.splice(measureIds.indexOf(comparedMeasureId[i]),1);
                            
                        }
                        
                    } 
                }
                }catch(e){}
               
                            chartDetails["defaultMeasures"] = MeasBy;
                            chartDetails["defaultMeasureIds"] = meassureIds;
//                        }
                            if(chartData[chartId]["chartType"]=="Cross-Table"){
                                chartDetails["colViewBys"]=columnBys;
                                chartDetails["colViewIds"]=columnId;
                            }
                            chartDetails["viewBys"] = viewBys;
                            chartDetails["viewByLevel"] = "single";
                            chartDetails["meassures"] = MeasBy;
                            chartDetails["viewIds"] = viewIds;
                            chartDetails["meassureIds"] = meassureIds;
                            chartDetails["aggregation"] = AggType;
                            chartDetails["dimensions"] = rowViewNamesArr;
                            chartDetails["KPIName"] = MeasBy[0];
                            if(chartData[chartId]["chartType"]=="DualAxis-Bar"){
                                chartDetails["measures1"] = [];
                                chartDetails["measures2"]= [];
                                chartDetails["chartList"] = [];
                            }

                            chartData[chartId] = chartDetails;
//                    }
            var currChartData={};
             currChartData[chartId] = chartDetails;
                    var prevChartData = chartData;
                    parent.$("#chartData").val(JSON.stringify(prevChartData));
            //Added by Ashutosh
            try{
                var tempchartData = JSON.parse(parent.$("#chartData").val());
                var tempdataSlider=tempchartData[chartId]["dataSlider"];
                var tempmeassureIds=tempchartData[chartId]["meassureIds"];
                var tempmeassureIdsMap={};
                for(var k in tempmeassureIds){
                    tempmeassureIdsMap[tempmeassureIds[k]]="";
                }
                for(var l in tempdataSlider){
                    if(!(l in tempmeassureIdsMap)){
                        tempdataSlider[l]='No'; 
                    }
                }
                tempchartData[chartId]["dataSlider"]=tempdataSlider;
                parent.$("#chartData").val(JSON.stringify(tempchartData));
            }catch(e){}
                       var divIndex=parseInt(chartId.replace("chart", ""));
    var h=$("#divchart"+divIndex).height();
    var w=$("#divchart"+divIndex).width();
    var top=(h/2)-25;
    var left=(w/2)-25;
    $("#chart"+divIndex).html("<div id='chart_loading' style='position:absolute;top:"+top+"px;left:"+left+"px;display:block;z-index: 99;background-color: #fff;opacity: 0.7;'><img id='loading-image' width='50px' src='"+$("#ctxpath").val()+"/images/chart_loading.gif' alt='Loading...' /></div>");
                 if(chartData[chartId]["chartType"]=="KPI-Table" || chartData[chartId]["chartType"]== "DualAxis-Target" ||chartData[chartId]["chartType"]=="Expression-Table" ||chartData[chartId]["chartType"]=="Emoji-Chart" ||chartData[chartId]["chartType"]=="Stacked-KPI" ||chartData[chartId]["chartType"]=="Combo-Analysis" || chartData[chartId]["chartType"]=="KPIDash" ||chartData[chartId]["chartType"]=="TileChart" ||chartData[chartId]["chartType"]=="RadialProgress"||chartData[chartId]["chartType"]=="LiquidFilledGauge" ||chartData[chartId]["chartType"]=="Dial-Gauge" ||chartData[chartId]["chartType"]=="Emoji-Chart" ||chartData[chartId]["chartType"]=="Trend-KPI"){
                 $.ajax({
                     
                     async : false,     
                     type:"POST",
                        data:parent.$("#graphForm").serialize(),
                        url:ctxpath+"/reportViewer.do?reportBy=GTKPICalculateFunction&repId="+reportId+"&measId="+meassureIds+"&aggType="+AggType+"&measName="+JSON.stringify(encodeURIComponent(MeasBy))+"&chartId="+chartId,
        //                     url: $("#ctxpath").val()+"/reportViewer.do?reportBy=editCharts&rowViewByArray="+encodeURIComponent(viewIds)+"&reportId="+reportId+"&rowViewNamesArr="+encodeURIComponent(viewIds)+"&rowMeasArray="+encodeURIComponent(MeasIds)+"&rowMeasNamesArr="+JSON.stringify(MeasBy)+"&chartData="+encodeURIComponent(JSON.stringify(currChartData))+"&reportName="+parent.$("#graphName").val()+"&isEdit=Y"+"&editId="+divId,
        success: function(data){
              chartData[chartId]["GTValueList"] = JSON.parse(data);
            parent.$("#chartData").val(JSON.stringify(chartData));
                var dataValue =   JSON.parse(data)
//                var dataValue =   parent.GTValue(chartId,reportId,meassureIds,AggType,MeasBy)
                                 if(chartData[chartId]["chartType"]=="Trend-KPI" ||chartData[chartId]["chartType"]=="Combo-Analysis"){
					chartData[chartId]["currentMeasures"] = chartData[chartId]["meassures"];
						 parent.$("#chartData").val(JSON.stringify(chartData));
						
                                            $.ajax({
                                                type:"POST",
                                                data:parent.$("#graphForm").serialize(),
                                                url: ctxpath+"/reportViewer.do?reportBy=editCharts&rowViewByArray="+encodeURIComponent(rowViewByArray)+"&reportId="+reportId+"&rowViewNamesArr="+encodeURIComponent(rowViewNamesArr)+"&rowMeasArray="+encodeURIComponent(rowMeasArray)+"&rowMeasNamesArr="+JSON.stringify(MeasBy)+"&chartData="+encodeURIComponent(JSON.stringify(currChartData))+"&reportName="+parent.$("#graphName").val()+"&isEdit=Y"+"&editId="+chartId,
                                                success: function(data){
//                                                    parent.$("#chartData").val(JSON.stringify(prevChartData));
                                                    var meta = JSON.parse(JSON.parse(data)["meta"]);
                                                    parent.$("#measure").val(JSON.stringify(meta["measures"]));
                                                    parent.$("#chartData").val(JSON.stringify(meta["chartData"]));
//                                                    var chartData = JSON.parse(parent.$("#chartData").val());
//                                                    var  QuickFilterid="viewbys_"+chartId+"_"+viewBys[0]+"_"+viewIds[0]
//                                                    if(typeof  chartData[chartId]["QuickFilterValue"]!="undefined"){
//
//                                                        showQuickFilter(QuickFilterid)
//                                                    }
                                                    parent.generateSingleChart(data,chartId);

                                                }
                                            }
                                        )
                                 }
                                 else{
                                 parent.chartTypeFunction(chartId,chartData[chartId]["chartType"],chartData[chartId]["KPIName"],dataValue)
                                 }
        }})
 }else{
                    $.ajax({
                    async:false,
                        type:"POST",
                        data:parent.$("#graphForm").serialize(),
                     url: ctxpath+"/reportViewer.do?reportBy=editCharts&rowViewByArray="+encodeURIComponent(rowViewByArray)+"&reportId="+reportId+"&rowViewNamesArr="+encodeURIComponent(rowViewNamesArr)+"&rowMeasArray="+encodeURIComponent(rowMeasArray)+"&rowMeasNamesArr="+JSON.stringify(MeasBy)+"&reportName="+parent.$("#graphName").val()+"&isEdit=Y"+"&editId="+chartId,
                            success: function(data){
//                                parent.$("#chartData").val(JSON.stringify(prevChartData));
                                    var meta = JSON.parse(JSON.parse(data)["meta"]);
                            parent.$("#measure").val(JSON.stringify(meta["measures"]));
                            parent.$("#chartData").val(JSON.stringify(meta["chartData"]));
                                    var chartData = JSON.parse(parent.$("#chartData").val());
//                            if(typeof chartData[chartId]["trendChartData"]==='undefined'){
//                                alert(JSON.stringify(JSON.parse(JSON.parse(data)["data"])[chartId]));
                                if(chartData[chartId]["chartType"]!="Multi-View-Tree" && chartData[chartId]["chartType"]!=="Scatter-Analysis"){
                                chartData[chartId]["trendChartData"]=JSON.parse(JSON.parse(data)["data"])[chartId];
//                            }
                            parent.$("#chartData").val(JSON.stringify(chartData));
                            }
//       var completeViewbyIds = JSON.parse($("#viewbyIds").val());
//      var completeViewby = JSON.parse($("#viewby").val());
//      var viewbys22 = [];
//      var viewbyIds22 = [];
//      var dimensions = chartData[chartId]["dimensions"];
//      for(var i in dimensions){
//      for(var j in completeViewbyIds){
//
//      if(dimensions[i]==completeViewbyIds[j])  {
//          viewbys22.push(completeViewby[j])
//          viewbyIds22.push(completeViewbyIds[j])
//      }
//      }
//      }

//       var  QuickFilterid="viewbys_"+chartId+"_"+viewBys[0]+"_"+viewIds[0]
//      if(typeof  chartData[chartId]["QuickFilterValue"]!="undefined"){
//
//    showQuickFilter(QuickFilterid)
//      }
                               parent.generateSingleChart(data,chartId);

                            }
                    }
                )
                }
                }
             
                }
        </script>
    </body>
    <%
            } catch (Exception e) {
                e.printStackTrace();
            }
    %>
</html>
