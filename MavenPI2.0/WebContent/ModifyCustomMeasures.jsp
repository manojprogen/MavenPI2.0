<%@page contentType="text/html" pageEncoding="UTF-8" import="com.progen.i18n.TranslaterHelper,java.util.Locale"%>
<%--
    Document   : modify Custom Measures
    Created on : Jan 20, 2014, 06:50:01 PM
    Author     : Nazneen Khan
--%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<%
            String themeColor = "";
            if (session.getAttribute("theme") == null) {
                session.setAttribute("theme", themeColor);
            } else {
                themeColor = String.valueOf(session.getAttribute("theme"));
            }
            String DefaultArrregations[] = {"--", "sum", "avg", "min", "max", "count", "COUNTDISTINCT"};
            String userId = "";
            userId = String.valueOf(request.getSession(false).getAttribute("USERID"));

            //added by Dinanath dec 2015
            Locale cle = null;
            cle = (Locale) session.getAttribute("UserLocaleFormat");
            String contextPath=request.getContextPath();

%>

<html>
    <head>
        <link rel="stylesheet" href="<%=contextPath%>/tablesorter/docs/css/jq.css" type="text/css" media="print, projection, screen" />
        <link rel="stylesheet" href="<%=contextPath%>/tablesorter/themes/<%=themeColor%>/style.css" type="text/css" media="print, projection, screen" />
        <script type="text/javascript" src="<%=contextPath%>/javascript/quicksearch.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/tablesorter/jquery.tablesorter.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/tablesorter/addons/pager/jquery.tablesorter.pager.js"></script>
        <script type="text/javascript"  language="JavaScript" src="<%=contextPath%>/tablesorter/jquery.columnfilters.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/tablesorter/docs/js/chili/chili-1.8b.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/tablesorter/docs/js/docs.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/pi.js"></script>
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=themeColor%>/metadataButton.css" rel="stylesheet" />
        <style type="text/css">
            #measureDetTab{background-color: #8BC34A;color: white;}
            * {
               
                font-size: 11px;
                font-size-adjust: none;
                font-stretch: normal;
                font-style: normal;
                font-variant: normal;
                font-weight: normal;
                line-height: normal;
            }
        </style>
       
    </head>
    <body>
        <form id="customMeasureModifyForm" name="customMeasureModifyForm" method="post" action="">
            <table align="left"><tr valign="top"><td>
                        <%=TranslaterHelper.getTranslatedInLocale("connection", cle)%>:<select name="conn" id="conn" onchange="getFolderDetails1()">
                        </select>
                    </td>
                    <td> &nbsp;&nbsp;&nbsp;<%=TranslaterHelper.getTranslatedInLocale("roles", cle)%>:&nbsp;&nbsp;&nbsp;<select name="fldrSelect" id="fldrSelect" onchange="getTableDetails()"></select> </td>
                    <td> &nbsp;&nbsp;&nbsp; <%=TranslaterHelper.getTranslatedInLocale("tables", cle)%>: &nbsp;&nbsp;&nbsp;<select name="TabSelect" id="TabSelect" ></select> </td>
                    <td><input type="button" class="navtitle-hover" value="<%=TranslaterHelper.getTranslatedInLocale("go", cle)%>" onclick="getAllMeasuresDetails()"></td></tr></table><br/><br/>
                        <%--<table align="left">
                            <trgetTableDetails
                                <td>search<input type="text" name="serach_id" id="serach_id" /></td>
                            </tr>
                </table>--%>
            <table align="left" id="id_search">
                <%--<div id="id_search"></div>--%>
            </table>
            <div>
               <table align="center"  id="tableBodyDisp" class="tablesorter" width="100%" border="0px solid" cellpadding="0" cellspacing="1">
                </table>
                <div id="pagermodifyMeasures" class="pager" align="left" >
                    <img src="<%=request.getContextPath()%>/tablesorter/addons/pager/icons/first.png" class="first"/>
                    <img src="<%=request.getContextPath()%>/tablesorter/addons/pager/icons/prev.png" class="prev"/>
                    <input type="text" readonly class="pagedisplay" style="width:60px"/>
                    <img src="<%=request.getContextPath()%>/tablesorter/addons/pager/icons/next.png" class="next"/>
                    <img src="<%=request.getContextPath()%>/tablesorter/addons/pager/icons/last.png" class="last"/>
                    <select name="sortOrder" class="pagesize">
                        <option value="10" selected>10</option>
                        <option value="15">15</option>
                        <option id="allModifyMeasr" value="">All</option>
                    </select>
                </div>
                <table id="measureDetTab" class="tablesorter"  border="1px solid" cellpadding="0" cellspacing="0">
                    <thead>
                        <tr>
                           <th nowrap valign="" ><%=TranslaterHelper.getTranslatedInLocale("table_name", cle)%></th>
                            <th nowrap valign=""><%=TranslaterHelper.getTranslatedInLocale("Measure_names", cle)%></th>
                            <th nowrap valign="" ><%=TranslaterHelper.getTranslatedInLocale("formula", cle)%></th>
                            <th nowrap valign="" ><%=TranslaterHelper.getTranslatedInLocale("Delete", cle)%></th>
                            <!--                            <th nowrap valign="" >Group Name</th>-->
                        </tr>

                    </thead>
                    <tbody id="measureDetTabBody">
                    </tbody>
                    <!-- <tbody id="measureDetTabBody">

                    </tbody>-->
                    <div id="popupwindow"></div>
                </table>
            </div><input type="hidden" name="changeinreport" id="changeinreport" value="">
            <input type="hidden" name="changedependentmeasure" id="changedependentmeasure" value="">
            <input type="hidden" name="changeingroup" id="changeingroup" value="">
            <!--                <table  border="0px solid" width="100%">
                            <tr valign="top">
                                <td align="left" width="25%">
                                    <div id="pagerMeasureDetails" class="pager" align="left" >
                                        <img alt=""  src="<%=request.getContextPath()%>/tablesorter/addons/pager/icons/first.png"class="first"/>
                                        <img alt="" src="<%=request.getContextPath()%>/tablesorter/addons/pager/icons/prev.png" class="prev"/>
                                        <input type="text" readonly class="pagedisplay" style="width:60px"/>
                                        <img alt="" src="<%=request.getContextPath()%>/tablesorter/addons/pager/icons/next.png" class="next"/>
                                        <img alt="" src="<%=request.getContextPath()%>/tablesorter/addons/pager/icons/last.png" class="last"/>
                                        <select class="pagesize" id="selPagerRep">
                                            <option selected value="10">10</option>
                                            <option value="15">15</option>
                                            <option id="pageValeID" value="">All</option>
                                        </select>
                                    </div>
                                </td>
                                <td align="right" width="38%">
                                    <input type="button" value="Done" class="navtitle-hover" style="width:auto" onclick="updateMeasureDetails()">
                                </td>
                            </tr>
                        </table>-->
        </form>
 <script type="text/javascript">
            $(document).ready(function()
            {
                    $.get("<%= request.getContextPath()%>/userLayerAction.do?userParam=getAllConnectionForModifyMeasure",function(data){
                        $("#conn").html(data)
                    });
                });
                function getFolderDetails1(){
                    var connectionID= $("#conn").val()

                    $.get("<%= request.getContextPath()%>/userLayerAction.do?userParam=getFolderDetails&connectionID="+connectionID,function(data){
                        $("#fldrSelect").html(data)
                    });
                }
                function getAllMeasuresDetails(){
                    var connectionID= $("#conn").val()
                    var foldersSelected= $("#fldrSelect").val()
                    var tablesSelected= $("#TabSelect").val()
                    $.get("<%= request.getContextPath()%>/userLayerAction.do?userParam=getAllCustomMeasures&connectionID="+connectionID+"&foldersSelected="+foldersSelected+"&tablesSelected="+tablesSelected,function(data){
                        var jsonVar=eval('('+data+')')
                        var length=data.length
                        $("#allModifyMeasr").val(length);
                        var measureName=jsonVar.memberName
                        var aggregateType=jsonVar.aggregationType
                        var formula=jsonVar.actualColFormula
                        var tableName=jsonVar.bussTableName
                        var columnName=jsonVar.bussColName
                        var elementIDs=jsonVar.elementId
                        for(var i=0;i<measureName.length;i++){
                            var htmlVar ="<tr>"                         
                            htmlVar+="<td style='width: 40px'><input type='text'  style='width: 100%' readonly name='"+elementIDs[i]+"' id='Table Name"+i+"' value='"+tableName[i]+"'>\n\
                                        <input type='hidden' readonly  style='width: 100%' name='temp_"+elementIDs[i]+"' id='Table Name"+i+"' value='"+tableName[i]+"'></td>"
                            if(measureName[i]!=null && measureName[i]!="null"){
                                htmlVar+="<td style='width: 40px'><input type='text'  style='width: 100%' readonly name='"+elementIDs[i]+"' class='measureNames'  id='MeasureNames(old)"+i+"' value='"+measureName[i]+"' Style='background-color: #FCFBE0;'>"
                                htmlVar+="<input type='hidden'  style='width: 100%' name='temp_"+elementIDs[i]+"' id='MeasureNames(old)"+i+"' value='"+measureName[i]+"'></td>"
                            }
                            else {
                                htmlVar+="<td style='width: 40px'><input type='text'  style='width: 100%' readonly name='"+elementIDs[i]+"' class='measureNames' id='MeasureNames(old)"+i+"' value='--'>"
                                htmlVar+="<input type='hidden'  style='width: 100%' name='temp_"+elementIDs[i]+"' id='MeasureNames(old)"+i+"' value='--'></td>"
                            }
                            if(formula[i]!=null && formula[i]!="null"){
                                htmlVar+="<td style='width: 60%'><input type='text'  style='width: 100%' readonly name='"+elementIDs[i]+"' id='Formula"+i+"' value='"+formula[i].replace("'","", "gi")+"'>"
                                htmlVar+="<input type='hidden'  style='width: 100%' name='temp_"+elementIDs[i]+"' id='Formula"+i+"' value='"+formula[i].replace("'","", "gi")+"'></td>"
                            }
                            else {
                                htmlVar+="<td style='width: 60%'><input type='text'  style='width: 100%' readonly name='"+elementIDs[i]+"' id='Formula"+i+"' value='--'>"
                                htmlVar+="<input type='hidden'  style='width: 100%' name='temp_"+elementIDs[i]+"' id='Formula"+i+"' value='--'></td> "
                            }                            
                            htmlVar+="<td style='width: 5px'><a class='ui-icon ui-icon-closethick' style='background-color: white;' href='javascript:void(0);' title='Clear All' onclick='deleteRow("+elementIDs[i]+")'></a></td></tr>"
          
                            if(i==0){
                                $("#measureDetTabBody").html(htmlVar)
                            }else{
                                $('#measureDetTabBody tr:last').after(htmlVar);
                            }
                        }
                        $("#measureDetTab ").tablesorter( {headers : {0:{sorter:false}}} ).tablesorterPager({container: $("#pagermodifyMeasures") });
                       document.customMeasureModifyForm.sortOrder.selectedIndex = 0;
                       
                });
            }           
            function getTableDetails(){
                   var connectionID= $("#conn").val()
                   var folderID= $("#fldrSelect").val()

                   $.get("<%= request.getContextPath()%>/userLayerAction.do?userParam=getTabDetails&connectionID="+connectionID+"&folderID="+folderID,function(data){
                       $("#TabSelect").html(data)
                  });
            }
            function deleteRow(refID)
            {
                 var retVal = confirm("Are you sure you want to delete the record!");
                    if (retVal == true){
                         $.get("<%= request.getContextPath()%>/userLayerAction.do?userParam=deleteRow&refID="+refID,function(data){
                           alert('Record Deleted Successfully............')
                           getAllMeasuresDetails();
//                           $('#sortOrder option[value=10]').attr('selected','selected');
                        });
                    }   
            }
        </script>
    </body>
</html>
