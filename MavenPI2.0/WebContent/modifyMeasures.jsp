<%--
    Document   : modifyMeasures
    Created on : Aug 8, 2011, 10:50:01 AM
    Author     : malli
--%>


<%@page import="com.progen.i18n.TranslaterHelper"%>
<%@page import="java.util.Locale"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
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
//added by Dinanath
            Locale clocale = null;
            clocale = (Locale) session.getAttribute("UserLocaleFormat");
%>

<html>
    <head>
        <link rel="stylesheet" href="<%=request.getContextPath()%>/tablesorter/docs/css/jq.css" type="text/css" media="print, projection, screen" />
        <link rel="stylesheet" href="<%=request.getContextPath()%>/tablesorter/themes/<%=themeColor%>/style.css" type="text/css" media="print, projection, screen" />
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/quicksearch.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/tablesorter/jquery.tablesorter.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/tablesorter/addons/pager/jquery.tablesorter.pager.js"></script>
        <script type="text/javascript"  language="JavaScript" src="<%=request.getContextPath()%>/tablesorter/jquery.columnfilters.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/tablesorter/docs/js/chili/chili-1.8b.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/tablesorter/docs/js/docs.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/pi.js"></script>
        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/themes/<%=themeColor%>/metadataButton.css" rel="stylesheet" />
        <script type="text/javascript" src="<%= request.getContextPath()%>/JS/global.js"></script>
        <style type="text/css">
            .migrate{
                font-family: inherit;
                font-size: 10pt;
                color: #000;
                padding-left:12px;
                background-color:#8BC34A;
                border:0px;
            }
        </style>
        <script type="text/javascript">
            //var assinIdAndVales=new Array
            //var isMemberUseInOtherLevel="false"
            $(window).resize(function(){
                resizePage("modifyMeasures");
            });
            $(document).ready(function(){ 
                resizePage("modifyMeasures");                         
            <%--
                            $("#popupwindow").dialog({
                                    //bgiframe: true,
                                    autoOpen: false,
                                    height: 500,
                                    width: 500,
                                    position: 'top',
                                    modal: true
                                });
                                $('#popupwindow').dialog('open');
            --%>
                    $.get("<%= request.getContextPath()%>/userLayerAction.do?userParam=getAllConnectionForModifyMeasure",function(data){
                        $("#connectionsModMeas").html(data)
                    });
                });
                function getFolderDetails(){
                    var connectionID= $("#connectionsModMeas").val()

                    $.get("<%= request.getContextPath()%>/userLayerAction.do?userParam=getFolderDetails&connectionID="+connectionID,function(data){
                        $("#foldersSelectModMeas").html(data)
                    });
                }
//                TA_EN_1004 : Multiple ajax calls are made
                function getAllMeasures(){
                    if($("#modifyMesurAjaxBk").is(":visible")){
                        alert("Please Wait!");
                        return;
                    }
                    if($("#connectionsModMeas").val() == "--SELECT--" || $("#foldersSelectModMeas").val() == "--SELECT--" || $("#TablesSelectModMeas").val() == "--SELECT--"){
//                        alert("Select All Options!");
                        alert("Select All Options!");
                    } 
                    else{
                        $("#modifyMesurAjaxBk").css({
                            height:$("#modifyMesurAjaxBk").next().height(),
                            width:$("#modifyMesurAjaxBk").next().width(),
                            display:"block"
                        });
                    
                    var connectionID= $("#connectionsModMeas").val()
                    var foldersSelected= $("#foldersSelectModMeas").val()
                    var tablesSelected= $("#TablesSelectModMeas").val()
                    $.get("<%= request.getContextPath()%>/userLayerAction.do?userParam=getAllMeasures&connectionID="+connectionID+"&foldersSelected="+foldersSelected+"&tablesSelected="+tablesSelected,function(data){
                        var jsonVar=eval('('+data+')')
                        var length=data.length                        
                        var measureName=jsonVar.memberName
                        var aggregateType=jsonVar.aggregationType
            <%--alert('DefaultArrregations---->'+DefaultArrregations.toString())--%>
                        var formula=jsonVar.actualColFormula
                        var tableName=jsonVar.bussTableName
                        var columnName=jsonVar.bussColName
                        var elementIDs=jsonVar.elementId
                        $("#allModifyMeasures").val(elementIDs.length);
                        if(measureName.length <= 0){$("#modifyMesurAjaxBk").hide();$("#measureDetTabBodyModMeas").html("");$("#measureDetTabBodyModMeas").append("<tr><td style='text-align: center;background-color: rgb(204, 0, 0);color: #fff;' colspan='10'>No Record Found</td></tr>");return;}
                        for(var i=0;i<measureName.length;i++){
                            var htmlVar ="<tr>\n\
                          <td style='width:60px'><input type='checkbox' name='ChangeDependentMeasure' id='ChangeDependentMeasure"+i+"' value='"+elementIDs[i]+"' checked disabled></td>\n\
                          <td style='width:60px'><input type='checkbox' name='ChangeInGroup' id='ChangeInGroup"+i+"' value='"+elementIDs[i]+"'></td>\n\
                          <td style='width:60px'><input type='checkbox' name='ChangeInReport' id='ChangeInReport"+i+"' value='"+elementIDs[i]+"'></td>"

                            if(measureName[i]!=null && measureName[i]!="null"){
                                htmlVar+="<td ><input type='text' readonly name='"+elementIDs[i]+"' class='measureNames'  id='MeasureNames(old)"+i+"' value='"+measureName[i]+"' Style='background-color: #FCFBE0;'>"
                                htmlVar+="<input type='hidden' name='temp_"+elementIDs[i]+"' id='MeasureNames(old)"+i+"' value='"+measureName[i]+"'></td>"
                            }
                            else {
                                htmlVar+="<td ><input type='text' readonly name='"+elementIDs[i]+"' class='measureNames' id='MeasureNames(old)"+i+"' value='--'>"
                                htmlVar+="<input type='hidden' name='temp_"+elementIDs[i]+"' id='MeasureNames(old)"+i+"' value='--'></td>"
                            }
                            if(measureName[i]!=null && measureName[i]!="null") {
                                htmlVar+="<td><input type='text' name='"+elementIDs[i]+"' id='MeasureNames(modified)"+i+"' value='"+measureName[i]+"'>"
                                htmlVar+="<input type='hidden' name='temp_"+elementIDs[i]+"' id='MeasureNames(modified)"+i+"' value='"+measureName[i]+"'></td>"
                            }
                            else {
                                htmlVar+="<td><input type='text' name='"+elementIDs[i]+"' id='MeasureNames(modified)"+i+"' value='--'>"
                                htmlVar+="<input type='text' name='temp_"+elementIDs[i]+"' id='MeasureNames(modified)"+i+"' value='--'></td>"
                            }
            <%--if(aggregateType[i]!=null && aggregateType[i]!="null")
                htmlVar+="<td><input type='text' name='"+elementIDs[i]+"' id='AggregateType"+i+"' value='"+aggregateType[i]+"'></td>"
            else
                htmlVar+="<td><input type='text' name='"+elementIDs[i]+"' id='AggregateType"+i+"' value='--'></td>"--%>
                                htmlVar+="<td><select id=\"AggregateType"+i+"\" style=\"width:147px;\" name='"+elementIDs[i]+"' onchange='checkVals(this,"+i+")'>"
            <%for (int j = 0; j < DefaultArrregations.length; j++) {%>

                            if(aggregateType[i]=='<%=DefaultArrregations[j]%>')
                            htmlVar+="<option selected value=<%=DefaultArrregations[j]%>><%=DefaultArrregations[j]%></option>"
                            else
                                htmlVar+="<option value=<%=DefaultArrregations[j]%>><%=DefaultArrregations[j]%></option>"
            <%}%>
                htmlVar+="</select>"
                 htmlVar+="<select id=\"AggregateType"+i+"\" name='temp_"+elementIDs[i]+"' hidden>"
            <%for (int j = 0; j < DefaultArrregations.length; j++) {%>

                            if(aggregateType[i]=='<%=DefaultArrregations[j]%>')
                            htmlVar+="<option selected value=<%=DefaultArrregations[j]%>><%=DefaultArrregations[j]%></option>"
                            else
                                htmlVar+="<option value=<%=DefaultArrregations[j]%>><%=DefaultArrregations[j]%></option>"
            <%}%>

                htmlVar+="</select></td>"

                            if(formula[i]!=null && formula[i]!="null"){
//                                modified by Nazneen on 30Jan14
//                                htmlVar+="<td><input type='text' name='"+elementIDs[i]+"' id='Formula"+i+"' value='"+formula[i].replace("'","", "gi")+"'>"
//                                htmlVar+="<input type='hidden' name='temp_"+elementIDs[i]+"' id='Formula"+i+"' value='"+formula[i].replace("'","", "gi")+"'></td>"
                                htmlVar+="<td><input type=\"text\" name=\""+elementIDs[i]+"\" id=\"Formula"+i+"\" value=\""+formula[i]+"\">"
                                 htmlVar+="<input type=\"hidden\" name=\"temp_"+elementIDs[i]+"\" id=\"Formula"+i+"\" value=\""+formula[i]+"\"></td>"
                            }
                            else {
                                htmlVar+="<td><input type='text' name='"+elementIDs[i]+"' id='Formula"+i+"' value='--'>"
                                htmlVar+="<input type='hidden' name='temp_"+elementIDs[i]+"' id='Formula"+i+"' value='--'></td> "
                            }
                            htmlVar+="<td><input type='text' readonly name='"+elementIDs[i]+"' id='Table Name"+i+"' value='"+tableName[i]+"'>\n\
                                        <input type='hidden' readonly name='temp_"+elementIDs[i]+"' id='Table Name"+i+"' value='"+tableName[i]+"'></td>\n\
                          <td><input type='text' readonly name='"+elementIDs[i]+"' id='Column Name"+i+"' value='"+columnName[i]+"'> \n\
                                <input type='hidden' name='temp_"+elementIDs[i]+"' id='Column Name"+i+"' value='"+columnName[i]+"'></td> \n\
                            <td style='width:60px'><a class='ui-icon ui-icon-closethick' style='background-color: white;' href='javascript:void(0);' title='Clear All' onclick='deleteRow("+elementIDs[i]+")'></a></td></tr>"
            <%--<td><input type='text' name='Group Name//"+i+"' id='Group Name"+i+"' value='"+groupName[i]+"'></td>\n\\n\--%>

                            if(i==0){
                                $("#measureDetTabBodyModMeas").html(htmlVar)
                            }else{
                                $('#measureDetTabBodyModMeas tr:last').after(htmlVar);
                            }
                        }
                        $("#measureDetTab ").tablesorter( {headers : {0:{sorter:false}}} ).tablesorterPager({container: $("#pagermodifyMeasures") });
						$("#modifyMesurAjaxBk").hide();
                       <%-- $("#measureDetTab tbody tr ").quicksearch({labelText: 'Search: ',attached: '#id_search',position: 'left',delay: 100,loaderText: 'Loading...'});
   --%>
            <%--$('#measureDetTabBodyModMeas tr').quicksearch({
            position: 'before',
            attached: '#id_search',
            loaderText: '',
            delay: 100
        })--%>

                });
            }
            }

            function updateMeasureDetails(){
                var rowCount = $('#measureDetTabBodyModMeas tr').length;
                var changedependentmeasure = new Array();
                var changeingroup = new Array();
                var changeinreport = new Array();
                $("input:checkbox[name=ChangeDependentMeasure]:checked").each(function(){
                    changedependentmeasure.push($(this).val());
                });
                $("input:checkbox[name=ChangeInGroup]:checked").each(function(){
                    changeingroup.push($(this).val());
                });
                $("input:checkbox[name=ChangeInReport]:checked").each(function(){
                    changeinreport.push($(this).val());
                });
                $("#changeinreport").val(changeinreport)
                $("#changedependentmeasure").val(changedependentmeasure)
                $("#changeingroup").val(changeingroup)
                $.post("<%= request.getContextPath()%>/userLayerAction.do?userParam=getUpdateMeasureDetails&rowCount="+rowCount, $("#measureModifyForm").serialize(), function(data){
//                    if(data=="true"){
                        alert("Update sucessfully")
                        //window.location.reload(true);
//                        $('#tabs').tabs('load', 5);
//                    }else{
//                        alert("Error in updation")
//                    }
                });
            }
            function getTabDetails(){
                   var connectionID= $("#connectionsModMeas").val()
                   var folderID= $("#foldersSelectModMeas").val()

                   $.get("<%= request.getContextPath()%>/userLayerAction.do?userParam=getTabDetails&connectionID="+connectionID+"&folderID="+folderID,function(data){
                       $("#TablesSelectModMeas").html(data)
                  });
                }
            function deleteRow(refID){
                   $.get("<%= request.getContextPath()%>/userLayerAction.do?userParam=deleteRow&refID="+refID,function(data){
                      getAllMeasures();
                  });
                }
        </script>
    </head>
    <body>
        <form id="measureModifyForm" name="measureModifyForm" method="post" action="">
            <div style='width:100%;height:30px'>
                <div style='width:25%;max-width:280px;float:left'><%=TranslaterHelper.getTranslatedInLocale("connection", clocale)%> :&nbsp;&nbsp;&nbsp;<select style='min-width:150px' name="connectionsModMeas" id="connectionsModMeas" onchange="getFolderDetails()"></select></div>
                <div style='width:25%;max-width:280px;float:left'><%=TranslaterHelper.getTranslatedInLocale("roles", clocale)%>　:&nbsp;&nbsp;&nbsp;<select style='min-width:150px' name="foldersSelectModMeas" id="foldersSelectModMeas" onchange="getTabDetails()"></select></div>
                <div style='width:25%;max-width:280px;float:left'><%=TranslaterHelper.getTranslatedInLocale("tables", clocale)%>　: &nbsp;&nbsp;&nbsp;<select style='min-width:150px' name="TablesSelectModMeas" id="TablesSelectModMeas" ></select></div>
                <div style='width:25%;max-width:280px;float:left'><input type="button" style="float: left;border:none" class="prgBtn" value="<%=TranslaterHelper.getTranslatedInLocale("go", clocale)%>" onclick="getAllMeasures()"></div>
            </div>
<!--            <table align="left"><tr valign="top">
                    <td>
                        <%=TranslaterHelper.getTranslatedInLocale("connection", clocale)%>:<select name="connectionsModMeas" id="connectionsModMeas" onchange="getFolderDetails()">
                        </select>
                    </td>
                    <td> &nbsp;&nbsp;&nbsp;　<%=TranslaterHelper.getTranslatedInLocale("roles", clocale)%>　:&nbsp;&nbsp;&nbsp;<select name="foldersSelectModMeas" id="foldersSelectModMeas" onchange="getTabDetails()"></select> </td>
                    <td> &nbsp;&nbsp;&nbsp; <%=TranslaterHelper.getTranslatedInLocale("tables", clocale)%>　: &nbsp;&nbsp;&nbsp;<select name="TablesSelectModMeas" id="TablesSelectModMeas" ></select> </td>
                    <td><input type="button" class="navtitle-hover" value="<%=TranslaterHelper.getTranslatedInLocale("go", clocale)%>" onclick="getAllMeasures()"></td></tr></table><br/><br/>-->
                        <%--<table align="left">
                            <tr>
                                <td>search<input type="text" name="serach_id" id="serach_id" /></td>
                            </tr>
                </table>--%>
            <table align="left" id="id_search">
                <%--<div id="id_search"></div>--%>
            </table>
            <div style="width:100%">         
                
                <table align="center"  id="tablesorterDashboard" class="tablesorter" width="100%" border="0px solid" cellpadding="0" cellspacing="1">
                </table>
                
                <div id="modifyMesurAjaxBk" class='ajaxLoadingBk' style="margin-top:56px;display:none;z-index:1">
                    <i class="fa fa-refresh fa-spin fa-2x" style="margin-top:50px;color:#000"></i>
                </div>
<!--                TA_EN_1002 : Table resize after getting data -->
                <section class="fixTbl">
                    <div class="container" id='pbiTblContainer'>
                        <table id="measureDetTab" class="" border="1px solid #888" cellpadding="0" cellspacing="0" >
                    <thead>
                                <tr class="header" id='pbiTbl'>
                                    <th nowrap valign="" style='width:60px'><div style='width:60px'><%=TranslaterHelper.getTranslatedInLocale("ch_depen_msr", clocale)%></div></th>
                                    <th nowrap valign="" style='width:60px'><div style='width:60px'><%=TranslaterHelper.getTranslatedInLocale("ch_in_group", clocale)%></div></th>
                                    <th nowrap valign="" style='width:60px'><div style='width:60px'><%=TranslaterHelper.getTranslatedInLocale("ch_in_report", clocale)%></div></th>
                                    <th nowrap valign="" ><div><%=TranslaterHelper.getTranslatedInLocale("msr_names", clocale)%></div></th>
                                    <th nowrap valign="" ><div><%=TranslaterHelper.getTranslatedInLocale("msr_names_mod", clocale)%></div></th>
                                    <th nowrap valign="" ><div><%=TranslaterHelper.getTranslatedInLocale("aggregation", clocale)%></div></th>
                                    <th nowrap valign="" ><div><%=TranslaterHelper.getTranslatedInLocale("formula", clocale)%></div></th>
                                    <th nowrap valign="" ><div><%=TranslaterHelper.getTranslatedInLocale("table_name", clocale)%></div></th>
                                    <th nowrap valign="" ><div><%=TranslaterHelper.getTranslatedInLocale("column_name", clocale)%></div></th>
                                    <th nowrap valign="" style='width:60px'><div style='width:60px'><%=TranslaterHelper.getTranslatedInLocale("Delete", clocale)%></div></th>
                            <!--                            <th nowrap valign="" >Group Name</th>-->
                        </tr>
                    </thead>
                    <tbody id="measureDetTabBodyModMeas">
                    </tbody>
                    <!-- <tbody id="measureDetTabBodyModMeas">

                    </tbody>-->
                            
                        </table>
                    </div>
                </section>
                    <div id="popupwindow"></div>
                <div style="width:100%;margin-top: 10px;">
                    <div id="pagermodifyMeasures" class="pager" style="float:left">
                        <i class="fa fa-fast-backward first"></i>
                        <i class="fa fa-backward prev"></i>
                        <input type="text" readonly class="pagedisplay" style="width:60px;height:13px"/>
                        <i class="fa fa-forward next"></i>
                        <i class="fa fa-fast-forward last"></i>
                        <select class="pagesize" style='height:18px'>
                            <option selected >10</option>
                            <option value="15">15</option>
                            <option value="100">100</option>
                            <option value="200">200</option>
                            <option id="allModifyMeasures" value="">All</option>
                        </select>                        
                    </div>
                    <input type="button" id="btn" style="float:right;border:none" value="<%=TranslaterHelper.getTranslatedInLocale("done", clocale)%>" class="prgBtn" style="width:auto" onclick="updateMeasureDetails()">
                </div>
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

    </body>
</html>
