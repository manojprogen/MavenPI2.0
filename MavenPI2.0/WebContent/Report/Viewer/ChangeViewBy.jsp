<%@page contentType="text/html" pageEncoding="UTF-8" import ="java.util.ArrayList,java.util.HashMap,com.progen.i18n.TranslaterHelper,java.util.Locale,prg.db.Session,prg.db.Container,com.progen.reportview.bd.PbReportViewerBD"%>
<%@page import="com.progen.reportview.db.PbReportViewerDAO,com.progen.report.PbReportCollection,com.progen.report.PbReportRequestParameter"%>
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
            String iskpidashboard="";
            String fromdashboard="";
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
                ArrayList<String> allViewIds = null;
                ArrayList<String> allViewNames = null;
                ArrayList<String> rowViewIdList = null;
                ArrayList<String> colViewIdList = null;
                ArrayList<String> rowNamesLst = null;
                ArrayList<String> colNamesLst = null;
                String rowName = "";
                String colName = "";
                String rowViewListSTR = "";
                String rowViewNamesListSTR = "";
                String colViewListSTR = "";
                String colViewNamesListSTR = "";
                String rowViewByStr = "";
                String colViewByStr = "";
                 String imgpathRow = request.getContextPath() + "/icons pinvoke/arrow-curve.png";
                String imgpathCol = request.getContextPath() + "/icons pinvoke/arrow-curve-270.png";
                String imgpath = request.getContextPath() + "/icons pinvoke/tick-small.png";
                String delimgpath = request.getContextPath() + "/icons pinvoke/cross-small.png";
                String reportId = request.getParameter("REPORTID");
                String ctxPath = request.getParameter("ctxPath");
                String globalfilter = request.getParameter("globalfilter");
                String flag=request.getParameter("flag")+"";
             iskpidashboard = request.getParameter("iskpidashboard");
             fromdashboard = request.getParameter("fromdashboard");
                String themeColor="blue";
               String ReportLayout=String.valueOf(session.getAttribute("ReportLayout")); //added by mohit for kpi and none

                if (session.getAttribute("allViewIds") != null) {
                    allViewIds = (ArrayList<String>) session.getAttribute("allViewIds");
                } else {
                    allViewIds = new ArrayList<String>();
                }
                if (session.getAttribute("allViewNames") != null) {
                    allViewNames = (ArrayList<String>) session.getAttribute("allViewNames");
                } else {
                    allViewNames = new ArrayList<String>();
                }
                if (session.getAttribute("rowViewIdList") != null) {
                    if(flag!=null && flag.equalsIgnoreCase("true")){
                         rowViewIdList = (ArrayList<String>) session.getAttribute("Viewbyidnew");
                    rowNamesLst = (ArrayList<String>) session.getAttribute("Viewbynamenew");
                    }else{
                    rowViewIdList = (ArrayList<String>) session.getAttribute("rowViewIdList");
                    rowNamesLst = (ArrayList<String>) session.getAttribute("rowNamesLst");
                                       }
                    rowViewByStr = reportViewDAO.buildChangeViewBy(rowViewIdList, rowNamesLst, "RowViewBy", ctxPath);
                } else {
                    rowViewIdList = new ArrayList<String>();
                    rowNamesLst = new ArrayList<String>();
                }
                if (session.getAttribute("colViewIdList") != null) {
                    colViewIdList = (ArrayList<String>) session.getAttribute("colViewIdList");
                    colNamesLst = (ArrayList<String>) session.getAttribute("colNamesLst");
                    colViewByStr = reportViewDAO.buildChangeViewBy(colViewIdList, colNamesLst, "ColViewBy", ctxPath);

                } else {
                    colViewIdList = new ArrayList<String>();
                    colNamesLst = new ArrayList<String>();
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
<!--        <script type="text/javascript" src="<%=contextPath%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>-->
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
        
        <link type="text/css" href="<%=contextPath%>/css/global.css" rel="stylesheet" />
    </head>
    <body>
        <input type="hidden" id="reportId" name="reportId" value="<%=reportId%>">
        <input type="hidden" id="ctxPath" name="ctxPath" value="<%=ctxPath%>">
        <form name="ViewByForm" id="ViewByForm" method="post">
            <table style="width:100%;height:300px;" border="1">
                <tr>
                    <td width="50%" valign="top" class="draggedTable1">
                        <div style="height:20px;" class="bgcolor" align="center"><font size="2" class="gFontFamily gFontSize12" style="font-weight:bold;color:#369"><%=TranslaterHelper.getTranslatedInLocale("ViewBy_s", cL)%></font></div>
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
                                            <td width="100"><span title="" id="<%=allViewIds.get(i)%>" class="gFontFamily gFontSize12 viewBys"><%=allViewNames.get(i)%></span></td>
                                            <td title="<bean:message bundle="Tooltips" key="changeViewBy.rowViewBy"/>">
                                                <img alt=""  src='<%=imgpathRow%>' onclick="createViewBY('<%=allViewIds.get(i)%>','<%=allViewNames.get(i)%>','rowViewUL')"/>
                                           </td>
                                                                          <%if(flag!=null && !flag.equalsIgnoreCase("undefined") && flag.equalsIgnoreCase("true")){

       }else{%>
                                            <td title="<bean:message bundle="Tooltips" key="changeViewBy.colViewBy"/>"><img src='<%=imgpathCol%>' onclick="createViewBY('<%=allViewIds.get(i)%>','<%=allViewNames.get(i)%>','colViewUL')"/></td>
                                     <%}%>
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
                                    <div style="height:20px;" align="center" class="bgcolor"><font size="2" class="gFontFamily gFontSize12" style="font-weight:bold;"><%=TranslaterHelper.getTranslatedInLocale("Row_ViewBys", cL)%></font></div>
                                
                                      <% if(globalfilter!=null && globalfilter.equalsIgnoreCase("true") || (flag!=null && !flag.equalsIgnoreCase("undefined") &&  flag.equalsIgnoreCase("true"))){%>
                                              <div style="height:250px;overflow-y:auto;border: 1px dotted rgb(228, 228, 228);box-shadow: 1px 0px 0px 2px rgb(228, 228, 228);" id="RowViewBy">
                                          <%}else{%>
                                    <div style="height:125px;overflow-y:auto;border: 1px dotted rgb(228, 228, 228);box-shadow: 1px 0px 0px 2px rgb(228, 228, 228);" id="RowViewBy">
                                            <%}%>
                                        <ul id="rowViewUL" class="sortable">
                                         
                                            <%=rowViewByStr%>

                                        </ul>
                                    </div>
                                </td>
                            </tr>
                            <% if(globalfilter!=null && globalfilter.equalsIgnoreCase("true")){

}else{
                                            %>
                            <tr style="height:50%">
                                <td>
                                    <%if(flag!=null && !flag.equalsIgnoreCase("undefined") && flag.equalsIgnoreCase("true")){
                                        
       }else{%>
                                    <div style="height:20px;" align="center" class="bgcolor"><font size="2" class="gFontFamily gFontSize12" style="font-weight:bold;"><%=TranslaterHelper.getTranslatedInLocale("Column_ViewBys", cL)%></font></div>
                                    <div  style="height:125px;overflow-y:auto;border: 1px dotted rgb(228, 228, 228);box-shadow: 1px 0px 0px 2px rgb(228, 228, 228);" id="ColViewBy">
                                        <ul id="colViewUL" class="sortable">
                                            <%=colViewByStr%>
                                        </ul>
                                    </div>
                                        <%}%>
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
                          <% if(globalfilter!=null && globalfilter.equalsIgnoreCase("true")){%>
                           <input id="ChangeViewbyButton" type="button" class="navtitle-hover" style="width:auto" value="<%=TranslaterHelper.getTranslatedInLocale("done", cL)%>" onclick="savegblfilters()">

                          <% }else if(fromdashboard!=null && fromdashboard.equalsIgnoreCase("true")){%>
                           <input id="ChangeViewbyButton" type="button" class="navtitle-hover" style="width:auto" value="<%=TranslaterHelper.getTranslatedInLocale("done", cL)%>" onclick="editviewbysdb()">
                           <%}else if(flag!=null && !flag.equalsIgnoreCase("undefined") && flag.equalsIgnoreCase("true")){ %>
                          <input id="ChangeViewbyButton" type="button" class="gFontFamily gFontSize12 navtitle-hover" style="width:auto" value="<%=TranslaterHelper.getTranslatedInLocale("done", cL)%>" onclick="saveSelectedViewBy()">
                        <%} else{%>
                        <input id="ChangeViewbyButton" type="button" class="gFontFamily gFontSize12 navtitle-hover" style="width:auto" value="<%=TranslaterHelper.getTranslatedInLocale("done", cL)%>" onclick="saveViewBy()">
                        <%}%>
                    </td>
                </tr>
            </table>
        </form>
                    <script type="text/javascript">
              var isKpiDashboard1 = '<%=iskpidashboard%>'.toString();
            var ViewByArray=new Array();
            <%if (rowViewIdList != null && rowViewIdList.size() != 0) {
                    for (int i = 0; i < rowViewIdList.size(); i++) {
                        rowViewListSTR += "," + rowViewIdList.get(i);
                        rowViewNamesListSTR += "," + rowNamesLst.get(i);
                    }
                    rowViewListSTR = rowViewListSTR.trim().substring(1);
                    rowViewNamesListSTR = rowViewNamesListSTR.trim().substring(1);
            %>
                var rowViewStr = '<%=rowViewListSTR%>';
                var rowNamesStr = '<%=rowViewNamesListSTR%>'
                var RowViewByArray  = rowViewStr.split(",");
                var rowViewNamesArr = rowNamesStr.split(",");
                ViewByArray = rowViewStr.split(",");
            <%
} else {%>
    var RowViewByArray=new Array();
    var rowViewNamesArr=new Array();

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
                    $('ul#ViewByList li').quicksearch({
                        position: 'before',
                        attached: 'ul#ViewByList',
                        loaderText: '',
                        delay: 100
                    })
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
                           createViewBY(newid,name,"colViewUL");
                                }
                                else
                                    {
                                        createViewBY(ui.draggable.attr('id'),ui.draggable.html(),"colViewUL");
                                    }
                        }}
                    });
                    var From="";
                    if(parent.document.getElementById("Designer")!=null)
                     From=parent.document.getElementById("Designer").value;
                    if(From=="fromDesigner"){
                        $("#ChangeViewbyButton").val("Next")
                        //alert("automatic assigment"); //added by mohit for create report
                        //<//%rowViewByStr="";colViewByStr=""; %>
                                <% if(ReportLayout.equalsIgnoreCase("KPI")){%>
                        createViewBY('KPI','KPI','rowViewUL')

                                              <%}else if(ReportLayout.equalsIgnoreCase("None")){%>
                            createViewBY('None','None','rowViewUL')
                         <%}else{%>
                        createViewBY('<%=allViewIds.get(0)%>','<%=allViewNames.get(0)%>','rowViewUL')
                              <%}%>
                                   saveViewBy();
                    }
                });
                function savegblfilters(){
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

$.post(ctxpath+"/reportViewer.do?reportBy=setqfilters&RowViewByArray="+rowViewByArray+"&REPORTID="+reportId, $("#ViewByForm").serialize(),
                             function(data){
                                 if( isKpiDashboard1=="true"){
                                 submitChangeViewBykpi();
                                 }else{
                                submitChangeViewBy();
                                 }
                        });
                }
                  var viewByArray=new Array();
                function editviewbysdb(){
                   var From="";
                if(parent.document.getElementById("Designer") != null)
                From=parent.document.getElementById("Designer").value;

                    var reportId  = document.getElementById("reportId").value;
                    var ctxpath = document.getElementById("ctxPath").value;
                    var count=0;
//                    var viewByArray=new Array();
                    var rowViewByArray=new Array();
                    var colViewByArray=new Array();

                     var ul = document.getElementById("rowViewUL");
//                     alert(ul)
                    if(ul!=undefined || ul!=null){
                    var colIds=ul.getElementsByTagName("li");
                    if(colIds.length>2)
                        {
                            alert("Don't drop More Than 2 Row ViewBys")               // added by mohit
                            return false;
                        }else{
//                     alert(colIds)
                    if(colIds!=null && colIds.length!=0){
                        for(var i=0;i<colIds.length;i++){
//                              alert(colIds[i].id)
                            //cols = cols+","+colIds[i].id.replace("GrpCol","");
                            rowViewByArray.push(colIds[i].id.replace("ViewBy",""));
                            viewByArray.push(colIds[i].id.replace("ViewBy",""));
                        }

                    }
                }
                    }


 parent.setMultiFilter()
    //    alert($("#ViewBy").val())
      submitChangeViewBydb(reportId);
//                    }
                }
                function saveSelectedViewBy(){
                    var reportId  = document.getElementById("reportId").value;
                    var ctxpath = document.getElementById("ctxPath").value;
                    var count=0;
                    var viewByArray=new Array();
                    var rowViewByArray=new Array();
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
                    
                           $.post(ctxpath+"/reportTemplateAction.do?templateParam=changeSelectedViewBy&ViewByArray="+viewByArray+"&RowViewByArray="+rowViewByArray+"&reportId="+reportId+"&ctxpath="+ctxpath,
                             function(data){
                                parent.$("#editViewByDiv").dialog('close');
                                   submitChangeViewBy();
                        });
                        
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
                                $.post(ctxpath+"/reportTemplateAction.do?templateParam=designerViewbys&ViewByArray="+viewByArray+"&RowViewByArray="+rowViewByArray+"&ColViewByArray="+colViewByArray+"&ChangeView=ChangeViewBy&reportId="+reportId+"&ctxpath="+ctxpath+"&rowViewNamesArr="+rowViewNamesArr+"&colViewNamesArr="+colViewNamesArr, $("#ViewByForm").serialize(),
                             function(data){//
                               parent.$("#editViewByDiv").dialog('close');
                               parent.$("#paramDesign").hide();
                    var prevREPIds=parent.document.getElementById("REPIds").value
//                    alert(prevREPIds)
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
//                    alert(rowViewByArray+"=======rowViewByArray")
                    if(rowViewByArray!=""){
                        if(flag==0){
                            //alert("mohit");
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
                                //modified by anitha for crosstab report toget edit viewby work properly
                                var result = false;
                                 <%
                                  ArrayList<String> sortCols = null;
                                                     if (request.getSession(false) != null) {
                                                         try {
                                                             Container container = null;
                                                             HashMap map = (HashMap) request.getSession(false).getAttribute("PROGENTABLES");
                                                             if (map != null) {
                                                                 container = (Container) map.get(reportId);
                                                                 sortCols = container.getSortColumns();
                                                             }
                                                         } catch (Exception e) {
                                                             e.printStackTrace();
                                                         }
                                                     }
                               if (colViewIdList != null && !colViewIdList.isEmpty() && sortCols!= null &&!sortCols.isEmpty()) {%>
                                    alert("All Table filters are removed. Please update them.");
                           <% session.setAttribute("refreshFrmChViewby", "Y"); %>
                                    <%}%>
//                                        if(result){//
//                                            parent.document.getElementById('loading').style.display='none';
//                                            parent.$("#editViewByDiv").dialog('close');
//                                        }else{
                                    //end of code by anitha
                         $.post(ctxpath+"/reportViewer.do?reportBy=changeViewBy&ViewByArray="+viewByArray+"&RowViewByArray="+rowViewByArray+"&ColViewByArray="+colViewByArray+"&ChangeView=ChangeViewBy&reportId="+reportId+"&ctxpath="+ctxpath+"&rowViewNamesArr="+rowViewNamesArr+"&colViewNamesArr="+colViewNamesArr, $("#ViewByForm").serialize(),
                             function(data){
                                 if( isKpiDashboard1=="true"){
                                 submitChangeViewBykpi();
                                 }else{
                                submitChangeViewBy();
                                 }
                        });
                    //}
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
 function submitChangeViewBykpi(){
      parent.$("#editViewByDiv").dialog('close');
                    var reportId  = document.getElementById("reportId").value;
                    var ctxpath = document.getElementById("ctxPath").value;
                    parent.document.forms.submitReportForm.action = ctxpath+"/reportViewer.do?reportBy=viewReport&action=ChangeViewBy&REPORTID="+reportId+"&isKPIDashboard=true";
                    parent.document.forms.submitReportForm.submit();
                   
                }
 function submitChangeViewBydb(REPORTID){
       parent.document.forms.frmParameter.action = "dashboardViewer.do?reportBy=viewDashboard&action=paramChange&REPORTID="+REPORTID+"&RowViewByArray="+viewByArray;
  parent.document.forms.frmParameter.submit();

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
                                childLI.style.height='18px';//modified by Dinanath
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
                      if(x.match(elmntId)==null){
                            ViewByArray.push(elmntId);
                            RowViewByArray.push(elmntId);
                            rowViewNamesArr.push(elementName);
                            var childLI=document.createElement("li");
                            childLI.id='ViewBy'+elmntId;
                            childLI.style.width='auto';
                            childLI.style.height='18px';//modified by Dinanath
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
                        }
                    }
                    $(".sortable").sortable();
                    $(".sortable").disableSelection();
                }
                function deleteColumn(index,dropLoc,name){
                    var LiObj=document.getElementById(index);
                    var parentUL=document.getElementById(LiObj.parentNode.id);
                    parentUL.removeChild(LiObj);;
                    var x=index.replace("ViewBy","");
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
                    }else if(dropLoc == 'ColViewBy'){
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
        </script>
    </body>
    <%
            } catch (Exception e) {
                e.printStackTrace();
            }
    %>
</html>
