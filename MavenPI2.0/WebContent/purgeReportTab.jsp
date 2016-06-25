<%-- 
    Document   : purgeReportTab
    Created on : Apr 13, 2010, 3:37:20 PM
    Author     : Administrator
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" import="prg.db.PbReturnObject"%>
<% String contXPath=request.getContextPath();%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <link rel="stylesheet" href="<%=contXPath%>/stylesheets/style.css" type="text/css" media="print, projection, screen">
        <link type="text/css" href="<%=contXPath%>/stylesheets/metadataButton.css" rel="stylesheet" />
       

    </head>
    <%
           PbReturnObject list = null;
            //   ////.println("kkjkjkjkj-------"+session.getAttribute("repPurgeSortList"));
            if (session.getAttribute("repPurgeSortList") == null) {
           if (request.getAttribute("repPurgeList") != null) {
               list = (PbReturnObject) request.getAttribute("repPurgeList");
                  }
                } else {
                    list = (PbReturnObject) session.getAttribute("repPurgeSortList");
           }
    %>

    <body>
         <%
            String selectValue = "";
            String seloption = "";
            selectValue = (String) session.getAttribute("selectValuePurge");
            seloption = (String) session.getAttribute("seloptionPurge");
            if (session.getAttribute("selectValuePurge") == null) {
                selectValue = "report_name";
            }
            if (session.getAttribute("seloptionPurge") == null) {
                seloption = "ASC";
            }
%>
        <form name="purgeReportForm"  method="post" style="width:98%" action="#">
        <script type="text/javascript">
            $(document).ready(function(){
                $("#tablesorterPurge")
                .tablesorter({headers : {0:{sorter:false},1:{sorter:false},2:{sorter:false},3:{sorter:false},4:{sorter:false}}})
                .tablesorterPager({container: $('#pagerReportPurge')})
            });
        </script>
        <table  border="0px solid" width="100%">
            <tr valign="top">
                <td align="left" width="25%">
                    <div id="pagerReportPurge" class="pager" align="left" >
                        <img src="<%=request.getContextPath()%>/tablesorter/addons/pager/icons/first.png"class="first"/>
                        <img src="<%=request.getContextPath()%>/tablesorter/addons/pager/icons/prev.png" class="prev"/>
                        <input type="text" readonly class="pagedisplay"/>
                        <img src="<%=request.getContextPath()%>/tablesorter/addons/pager/icons/next.png" class="next"/>
                        <img src="<%=request.getContextPath()%>/tablesorter/addons/pager/icons/last.png" class="last"/>
                        <select class="pagesize" id="selPagerRep">
                            <option selected value="10">10</option>
                            <option value="15">15</option>
                            <option value="<%=list.getRowCount()%>">All</option>
                        </select>
                    </div>
                </td>
                <td align="left" width="6%" style="font-weight:bold;color:#369">Sort by :</td>
                <td align="left" width="10%">
                    <select id="sortBySelPurge" name="sortBySelPurge" onchange="orderBYPurgeReport()">
                        <% if (selectValue.equalsIgnoreCase("report_desc")) { %>
                        <option id="report_name" value="report_name">Report Name</option>
                        <option id="report_desc" value="report_desc" selected>Report Description</option>
                        <%} else {%>
                        <option id="report_name" value="report_name" selected>Report Name</option>
                        <option id="report_desc" value="report_desc">Report Description</option>
                        <%} %>
                    </select>
                </td>

                <td class="wordStyle" align="right" width="10%" style="font-weight:bold;" valign="top">
                    Sort Option :
                </td>
                <td align="right" width="1%" valign="top">
                    <%if (seloption.equalsIgnoreCase("ASC")) {%>
                    <input type="radio" id="asc" name="sortOption" checked value="ASC" onclick="orderBYPurgeReport()">
                    <%} else {%>
                    <input type="radio" id="asc" name="sortOption"  value="ASC" onclick="orderBYPurgeReport()">
                    <%}%>
                </td>
                <td class="wordStyle" align="right" width="4%" valign="top">Ascending</td>

                <td align="right" width="1%" valign="top">
                     <%if (seloption.equalsIgnoreCase("DESC")) {%>
                    <input type="radio" id="desc" name="sortOption" checked value="DESC" onclick="orderBYPurgeReport()">
                    <%} else {%>
                    <input type="radio" id="desc" name="sortOption" value="DESC" onclick="orderBYPurgeReport()">
                     <%}%>
                </td>
                <td class="wordStyle" align="right" width="4%" valign="top">Descending</td>
                <td align="right" width="40%">
                    <input type="button" value="Purge Report" class="navtitle-hover" style="width:auto"  onclick="javascript:purgeReport()">
                </td>
            </tr>
        </table>
        <table align="center" id="tablesorterPurge" class="tablesorter" width="100%" border="0px solid" cellpadding="0" cellspacing="1">
            <thead>
                <tr>
                    <th nowrap><input type="checkbox" name="PurgeRepSelect" id="PurgeRepSelect1" onclick="return AllPurgeReps()">Select All</th>
                    <th nowrap>Report Name</th>
                    <th nowrap>Report Description</th>

                    <th nowrap>Business Role</th>
                    <th nowrap>Created On</th>

                </tr>
            </thead>
            <tbody>
                <%--<tbody style="height:400px;overflow-x:hidden">--%>
                <%//int i = 0;
            for (int i = 0; i < list.getRowCount(); i++) {%>
                <tr>
                    <td style="width:30px"><input type="checkbox" name="PurgeRepSelect" id="PurgeRepSelect<%=(i + 1)%>" value="<%=list.getFieldValueString(i, 0)%>" onclick="unselPurgeAll()"></td>
                    <td>
                        <a href="javascript:void(0)" onclick='javascript:viewReport("reportViewer.do?reportBy=viewReport&REPORTID=<%=list.getFieldValueString(i, 0)%>")'> <%=list.getFieldValueString(i, 1)%></a>
                    </td>

                    <td>
                        <%=list.getFieldValueString(i, 2)%>
                    </td>

                    <td><%=list.getFieldValueString(i, 3)%></td>
                    <td><%=list.getFieldValueDateString(i, 4)%></td>


                </tr>
                <%}%>
            </tbody>
        </table>
           </form>
             <script type="text/javascript">
            function AllPurgeReps()
            {
                var PurgeRepSelectObj=document.getElementsByName("PurgeRepSelect");

                for(var i=0;i<PurgeRepSelectObj.length;i++){
                    if(PurgeRepSelectObj[0].checked){
                        PurgeRepSelectObj[i].checked=true;
                    }
                    else{
                        PurgeRepSelectObj[i].checked=false;
                    }
                }
            }
            function unselPurgeAll(){
                var allsel = document.getElementById("RepSelect1");
                allsel.checked = false;
            }
            function orderBYPurgeReport(){
                var selectValue = document.getElementById("sortBySelPurge").value;
                var seloption;
                var radioObj = document.forms.purgeReportForm.sortOption;
                for(var i=0;i<radioObj.length;i++){
                    if(radioObj[i].checked){
                        seloption=radioObj[i].value
                    }
                }
                $.ajax({
                    url: "reportTemplateAction.do?templateParam=repPurgeSort&selectValuePurge="+selectValue+"&seloptionPurge="+seloption,
                    success: function(data){
                        document.forms.purgeReportForm.action = "<%=request.getContextPath()%>/home.jsp#Purge_Report";
                        document.forms.purgeReportForm.submit();
                    }
                });
            }
            function viewReport(path){
                document.forms.purgeReportForm.action=path;
                document.forms.purgeReportForm.submit();
            }
            function purgeReport(){
                 var RepSelectObj=document.getElementsByName("PurgeRepSelect");
                var purgeRepids = new Array();
                for(var i=1;i<RepSelectObj.length;i++){
                    if(RepSelectObj[i].checked){
                        purgeRepids.push(RepSelectObj[i].value);
                    }
                    else{
                        RepSelectObj[i].checked=false;
                    }
                }
                if(purgeRepids.length!=0){
                    $.ajax({
                        url: "reportTemplateAction.do?templateParam=PurgeReports&purgeRepids="+purgeRepids.toString(),
                        success: function(data){
                            document.forms.purgeReportForm.action = "<%=request.getContextPath()%>/home.jsp#Purge_Report";
                            document.forms.purgeReportForm.submit();
                        }
                    });
                    // document.forms.reportForm.action="reportTemplateAction.do?templateParam=DeleteUserReports&purgeRepids="+purgeRepids.toString();
                    //  document.forms.reportForm.submit();
                }else{
                    alert("Please select Report(s)")
                }
            }
        </script>
    </body>
</html>
