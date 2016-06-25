<%-- 
Document   : TimeBased
Created on : 16 Feb, 2011, 3:30:42 PM
Author     : progen
--%>

<%@page import="prg.db.PbReturnObject,prg.db.PbDb"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%
            String themeColor = "blue";
            if (session.getAttribute("theme") == null) {
                session.setAttribute("theme", themeColor);
            } else {
                themeColor = String.valueOf(session.getAttribute("theme"));
                
            }
            String contextPath1=request.getContextPath();
%>

<html>
    <head>
        <script type="text/javascript" src="<%=contextPath1%>/javascript/lib/jquery/js/jquery-1.4.2.min.js"></script>
        <script type="text/javascript" src="<%=contextPath1%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>
<!--         <script type="text/javascript" src="<%=request.getContextPath()%>/jQuery/jquery/ui/ui.datepicker.js"></script>-->
        <!--        <Script type="text/javascript"  src="../JS/dateSelection.js"></Script>
                <Script type="text/javascript"  src="../JS/timePicker.js"></Script>-->
        <script type="text/javascript" src="<%=contextPath1%>/javascript/pbReportViewerJS.js"></script>
        <link type="text/css" href="<%=contextPath1%>/stylesheets/themes/<%=themeColor%>/jquery-ui-1.7.3.custom.css" rel="stylesheet" />
        <link type="text/css" href="<%=contextPath1%>/stylesheets/themes/<%=themeColor%>/ReportCss.css" rel="stylesheet" />
        <link type="text/css" href="<%=contextPath1%>/stylesheets/themes/<%=themeColor%>/pbReportViewerCSS.css" rel="stylesheet" />

        <style type="text/css">

            * {
                font-family: verdana;
                font-size: 12px;
                font-size-adjust: none;
                font-stretch: normal;
                font-style: normal;
                font-variant: normal;
                font-weight: normal;
                line-height: normal;
            }
        </style>

        <%
                    java.sql.Date datestr = new java.sql.Date(System.currentTimeMillis());
                    String elementId = request.getParameter("elementId");
                    String elementName = request.getParameter("elementName");
                    String query = "select AGGREGATION_TYPE from PRG_USER_ALL_INFO_DETAILS where ELEMENT_ID="+elementId;
                    PbDb pbdb = new PbDb();
                    PbReturnObject retObject = pbdb.execSelectSQL(query);
                    String aggType = retObject.getFieldValueString(0, 0);
        %>



       
    </head>
    <body onload="changeTimeBasedType()">
        <form name="TimeBasedForm" id="TimeBasedForm" method="post" action="">

            <br>
            <div  width="100%">
                <table align="center">
                    <tr>
                        <td width="50%" >Measure : </td>
                        <td width="50%"><%=elementName%></td>
                    </tr>
                </table>
            </div>
            <br>
            <br>
            <div width="100%">

                <table width="80%" align="center" id="timeBasedTable">
                    <tr>
                        <td align="left" class="myhead" width="40%"><font>Select Time Based</font></td>
                        <td>
                            <select name="TypeDropDown" id="TypeDropDown">
                                <option value="Rolling" selected onclick="changeTimeBasedType()">Rolling</option>
                                <option value="Discrete" onclick="changeTimeBasedType()">Discrete</option>
                                <option value="Range" onclick="changeTimeBasedType()">Range</option>
                                <option value="Trailing" onclick="changeTimeBasedType()">Trailing</option>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td align="left" class="myhead" width="40%"><font>Level</font></td>
                        <td  align="left" width="30%">
                            <select name="level" id="level" onChange="changeLevel()">
                                <option value="Year" selected>Year</option>
                                <option value="Quarter">Quarter</option>
                                <option value="Month">Month</option>
                                <option value="Day">Day</option>
                            </select>
                        </td>
                    </tr>
                </table>
            </div>

            <br>
            <br>
            <div align="center">
                <table width="10%">
                    <tr>
                        <td>
                            <input type="button" class ="navtitle-hover" onclick="saveTimeBasedValue()" value="Save">
                        </td>
                    </tr>
                </table>
            </div>
            <input type="hidden" name="elementId" id="elementId"     value="<%=elementId%>">
            <input type="hidden" name="TypeDropDown" id="TypeDropDown" value="Range">
            <input type="hidden" name="aggType" id="aggType" value="<%=aggType%>">

        </form>
             <script type="text/javascript" >
            $(document).ready(function(){
                $('#datepicker2').datepicker({
                    changeMonth: true,
                    changeYear: true
                    //showButtonPanel: true

                });

                $('#datepicker3').datepicker({
                    changeMonth: true,
                    changeYear: true
                    //showButtonPanel: true

                });
                $('#datepicker1').datepicker({
                    showOn: "button",
                    buttonImage: "icons pinvoke/calendar-select.png",
                    buttonImageOnly: true

                });
            });
            function test(){
                        
                $('#datepicker1').datepicker({
                    showOn: "button",
                    buttonImage: "icons pinvoke/calendar-select.png",
                    buttonImageOnly: true

                });
                $('#datepicker2').datepicker({
                    showOn: "button",
                    buttonImage: "icons pinvoke/calendar-select.png",
                    buttonImageOnly: true
                });
                $('#datepicker3').datepicker({
                    showOn: "button",
                    buttonImage: "icons pinvoke/calendar-select.png",
                    buttonImageOnly: true
                });
            }
            function testDiscrete(rowCount){
                $('#datepickerDiscrete'+rowCount).datepicker({
                    showOn: "button",
                    buttonImage: "icons pinvoke/calendar-select.png",
                    buttonImageOnly: true

                });
            }

            var cust_name;
            var cust_st_date;
            var cust_end_date;
            var cust_year_array =  new Array();
            var cust_month_array =  new Array();
            var cust_quarter_array = new Array();

            function saveTimeBasedValue(){
                var tableObj = document.getElementById("timeBasedTable");
                var rowCount = tableObj.rows.length;
                if($("#TypeDropDown").val()=='Range'&&$("#level").val()=='Day'){
                    var startDate = document.getElementById("datepicker2").value;
                    var endDate =  document.getElementById("datepicker3").value;
                    $.ajax({
                        url:'<%=request.getContextPath()%>/calenderSetUpAction.do?timeCalenders=valiDateCalnder&startYear='+startDate+'&endYear='+endDate+'&scheduler='+"scheduler",
                        success:function(data){
                            if(data=='true'){
                                $.post("<%=request.getContextPath()%>/reportViewer.do?reportBy=createTimeBasedFormula&rowCount="+rowCount, $("#TimeBasedForm").serialize() ,
                                function(data){
                                    parent.$("#aggVal").val(data);
                                    parent.$("#fromTimeBased").val("true");
                                    parent.$("#TimeBasedDiaolgDisplay").dialog('close');
                                });
                            }
                            else{
                                alert("Please select End Year correctly.")
                            }

                        }
                    });
                }else{
                    submitJSPForm();
                }
                //parent.saveCustMember(parent.tableListVal,parent.ConnectionIdval,parent.reportidval,parent.elementIdListString);
                //parent.$("#custmemDispDia").dialog('close');
            }

            function submitJSPForm(){
                var tableObj = document.getElementById("timeBasedTable");
                var rowCount = tableObj.rows.length;

                $.post("<%=request.getContextPath()%>/reportViewer.do?reportBy=createTimeBasedFormula&rowCount="+rowCount, $("#TimeBasedForm").serialize() ,
                function(data){
                    parent.$("#aggVal").val(data);
                    parent.$("#fromTimeBased").val("true");
                    parent.$("#TimeBasedDiaolgDisplay").dialog('close');
                });

            }

            function isNumberevent(evt)
            {
                var charCode = (evt.which) ? evt.which : event.keyCode
                if (charCode > 31 && (charCode < 48 || charCode > 57) && charCode != 46)
                    return false;
                return true;
            }


            function enableQuarter2(){
                var val = $('#selectQuarter1').val();
                var rangeHtml;
                var index = jQuery.inArray(val,cust_st_date);
                for(var i=index;i<cust_end_date.length;i++){
                    if(i==index)
                        rangeHtml =  rangeHtml+"<option value="+cust_end_date[i]+" selected>"+cust_end_date[i]+"</option>";
                    else
                        rangeHtml =  rangeHtml+"<option value="+cust_end_date[i]+">"+cust_end_date[i]+"</option>";
                }
                $('#selectQuarter2').html("");
                $('#selectQuarter2').html(rangeHtml);

            }
            function enableMonth2(){
                var val = $('#selectMonth1').val();
                var rangeHtml;
                var index = jQuery.inArray(val,cust_st_date);
                for(var i=index;i<cust_end_date.length;i++){
                    if(i==index)
                        rangeHtml =  rangeHtml+"<option value="+cust_end_date[i]+" selected>"+cust_end_date[i]+"</option>";
                    else
                        rangeHtml =  rangeHtml+"<option value="+cust_end_date[i]+">"+cust_end_date[i]+"</option>";
                }
                $('#selectMonth2').html("");
                $('#selectMonth2').html(rangeHtml);

            }

            function enableWeek2(){
                var val = $('#selectWeek1').val();
                var rangeHtml;
                var index = jQuery.inArray(val,cust_st_date);
                for(var i=index;i<cust_end_date.length;i++){
                    if(i==index)
                        rangeHtml =  rangeHtml+"<option value="+cust_end_date[i]+" selected>"+cust_end_date[i]+"</option>";
                    else
                        rangeHtml =  rangeHtml+"<option value="+cust_end_date[i]+">"+cust_end_date[i]+"</option>";
                }
                $('#selectWeek2').html("");
                $('#selectWeek2').html(rangeHtml);
            }

            function changeLevel(){
                var type = $("#TypeDropDown").val();
                if(type=='Range'){
                    changeRangeLevel();
                }else if(type=='Discrete'){
                    changeDiscreteLevel();
                }
            }
            function changeRangeLevel(){
                    var level = $("#level").val();
                    if(level=='Day'){
                        $("#timeBasedTable").find("tr:gt(1)").remove();
                        var tableObj = document.getElementById("timeBasedTable");
                        var rowCount = tableObj.rows.length;
                        var row = tableObj.insertRow(rowCount);
                        var rangeHtml = "<td width='40%' class='myhead'><font>Start Day<font></td><td width='60%'><input type='text'  name='datepicker2' id='datepicker2'value=''></td>";
                        row.innerHTML = rangeHtml;
                        rowCount++;
                        var row1 = tableObj.insertRow(rowCount);
                        rangeHtml = "<td width='40%' class='myhead'><font>End Day</font></td><td width = '60%'><input type='text'  name='datepicker3' id='datepicker3' value=''></td>";
                        row1.innerHTML = rangeHtml;
                        test();
                    }else if(level=='Quarter'){
                        $.ajax({
                            url: '<%=request.getContextPath()%>/reportViewer.do?reportBy=getLevelDetails&elementId='+<%=elementId%>+'&levelType=Quarter',
                            success: function(data){
                                var details = eval('('+data+')');
                                cust_name = details.cust_name;
                                cust_st_date = details.cust_st_date;
                                cust_end_date =  details.cust_end_date;

                                $("#timeBasedTable").find("tr:gt(1)").remove();
                                var tableObj = document.getElementById("timeBasedTable");
                                var rowCount = tableObj.rows.length;
                                var row = tableObj.insertRow(rowCount);
                                var rangeHtml = "<td width='40%' class='myhead'><font>Start Quarter</td><td width='60%'>";
                                rangeHtml =  rangeHtml+"<select name='selectQuarter1' id='selectQuarter1' name='selectQuarter1' onChange=\'enableQuarter2()\'>";
                                for(var i=0;i<cust_st_date.length;i++){
                                    if(i==0)
                                        rangeHtml =  rangeHtml+"<option value="+cust_st_date[i]+" selected>"+cust_st_date[i]+"</option>";
                                    else
                                        rangeHtml =  rangeHtml+"<option value="+cust_st_date[i]+">"+cust_st_date[i]+"</option>";
                                }
                                rangeHtml = rangeHtml+"</select></td>"
                                row.innerHTML = rangeHtml;
                                rowCount++;
                                var row1 = tableObj.insertRow(rowCount);
                                rangeHtml = "<td width='40%' class='myhead'><font>End Quarter</td><td width='60%'>";
                                rangeHtml = rangeHtml+"<select name='selectQuarter2' id='selectQuarter2' name='selectQuarter2'>";
                                for(var i=0;i<cust_end_date.length;i++){
                                    if(i==0)
                                        rangeHtml = rangeHtml+"<option value="+cust_end_date[i]+" selected>"+cust_end_date[i]+"<option>";
                                    else
                                        rangeHtml = rangeHtml+"<option value="+cust_end_date[i]+" >"+cust_end_date[i]+"<option>";
                                }
                                rangeHtml = rangeHtml+"</select></td>"
                                row1.innerHTML = rangeHtml;

                            }
                        });
                    }else if(level=='Month'){
                        $.ajax({
                            url: '<%=request.getContextPath()%>/reportViewer.do?reportBy=getLevelDetails&elementId='+<%=elementId%>+'&levelType=Month',
                            success: function(data){
                                var details = eval('('+data+')');
                                cust_name = details.cust_name;
                                cust_st_date = details.cust_st_date;
                                cust_end_date =  details.cust_end_date;

                                $("#timeBasedTable").find("tr:gt(1)").remove();
                                var tableObj = document.getElementById("timeBasedTable");
                                var rowCount = tableObj.rows.length;
                                var row = tableObj.insertRow(rowCount);
                                var rangeHtml = "<td width='40%' class='myhead'><font>Start Month</td><td width='60%'>";
                                rangeHtml =  rangeHtml+"<select name='selectMonth1' id='selectMonth1' name='selectMonth1' onChange=\'enableMonth2()\'>";
                                for(var i=0;i<cust_st_date.length;i++){
                                    if(i==0)
                                        rangeHtml =  rangeHtml+"<option value="+cust_st_date[i]+" selected>"+cust_st_date[i]+"</option>";
                                    else
                                        rangeHtml =  rangeHtml+"<option value="+cust_st_date[i]+">"+cust_st_date[i]+"</option>";
                                }

                                rangeHtml = rangeHtml+"</select></td>"
                                row.innerHTML = rangeHtml;
                                rowCount++;
                                var row1 = tableObj.insertRow(rowCount);
                                rangeHtml = "<td width='40%' class='myhead'><font>End Month</td><td width='60%'>";
                                rangeHtml = rangeHtml+"<select name='selectMonth2' id='selectMonth2' name='selectMonth2' >";
                                for(var i=0;i<cust_end_date.length;i++){
                                    if(i==0)
                                        rangeHtml = rangeHtml+"<option value="+cust_end_date[i]+" selected>"+cust_end_date[i]+"<option>";
                                    else
                                        rangeHtml = rangeHtml+"<option value="+cust_end_date[i]+" >"+cust_end_date[i]+"<option>";
                                }
                                rangeHtml = rangeHtml+"</select></td>"
                                row1.innerHTML = rangeHtml;
                            }
                        });

                    }else if(level=='Week'){
                        $.ajax({
                            url: '<%=request.getContextPath()%>/reportViewer.do?reportBy=getLevelDetails&elementId='+<%=elementId%>+'&levelType=Week',
                            success: function(data){
                                var details = eval('('+data+')');
                                cust_name = details.cust_name;
                                cust_st_date = details.cust_st_date;
                                cust_end_date =  details.cust_end_date;

                                $("#timeBasedTable").find("tr:gt(1)").remove();
                                var tableObj = document.getElementById("timeBasedTable");
                                var rowCount = tableObj.rows.length;
                                //var row = tableObj.insertRow(rowCount);
                                var rangeHtml = "<td width='40%' class='myhead'><font>Start Week</td><td width='60%'>";
                                rangeHtml =  rangeHtml+"<select name='selectWeek1' id='selectWeek1' name='selectWeek1' onChange=\'enableWeek2()\'>";
                                for(var i=0;i<cust_st_date.length;i++){
                                    if(i==0)
                                        rangeHtml =  rangeHtml+"<option value="+cust_st_date[i]+" selected>"+cust_st_date[i]+"</option>";
                                    else
                                        rangeHtml =  rangeHtml+"<option value="+cust_st_date[i]+">"+cust_st_date[i]+"</option>";
                                }


                                rangeHtml = rangeHtml+"</select></td>"
                                $('#timeBasedTable tr:last').after('<tr>'+rangeHtml+'</tr>');
                                rangeHtml = "<td width='40%' class='myhead'><font>End Week</td><td width='60%'>";
                                rangeHtml = rangeHtml+"<select name='selectWeek2' id='selectWeek2' >";
                                for(var i=0;i<cust_end_date.length;i++){
                                    if(i==0)
                                        rangeHtml = rangeHtml+"<option value="+cust_end_date[i]+" selected>"+cust_end_date[i]+"<option>";
                                    else
                                        rangeHtml = rangeHtml+"<option value="+cust_end_date[i]+" >"+cust_end_date[i]+"<option>";
                                }
                                rangeHtml = rangeHtml+"</select></td>"
                                $('#timeBasedTable tr:last').after('<tr>'+rangeHtml+'</tr>');
                            }
                        });

                    }else if(level=='Year'){
                        $.ajax({
                            url: '<%=request.getContextPath()%>/reportViewer.do?reportBy=getLevelDetailsForDiscrete&elementId='+<%=elementId%>+'&levelType=Year',
                            success: function(data){
                                var details = eval('('+data+')');
                                cust_year_array= details.cust_year_details;
                                $("#timeBasedTable").find("tr:gt(1)").remove();
                                var tableObj = document.getElementById("timeBasedTable");
                                var rowCount = tableObj.rows.length;
                                //var row = tableObj.insertRow(rowCount);
                                var rangeHtml = "<td width='40%' class='myhead'><font>Start Year</td><td width='60%'>";
                                rangeHtml =  rangeHtml+"<select id='selectYear1' name='selectYear1' onChange=\'enableYear2()\'>";
                                for(var i=0;i<cust_year_array.length;i++){
                                    if(i==0)
                                        rangeHtml =  rangeHtml+"<option value="+cust_year_array[i]+" selected>"+cust_year_array[i]+"</option>";
                                    else
                                        rangeHtml =  rangeHtml+"<option value="+cust_year_array[i]+">"+cust_year_array[i]+"</option>";
                                }


                                rangeHtml = rangeHtml+"</select></td>"
                                $('#timeBasedTable tr:last').after('<tr>'+rangeHtml+'</tr>');
                                rangeHtml = "<td width='40%' class='myhead'><font>End Year</td><td width='60%'>";
                                rangeHtml = rangeHtml+"<select name='selectYear2' id='selectYear2' >";
                                for(var i=0;i<cust_year_array.length;i++){
                                    if(i==0)
                                        rangeHtml = rangeHtml+"<option value="+cust_year_array[i]+" selected>"+cust_year_array[i]+"<option>";
                                    else
                                        rangeHtml = rangeHtml+"<option value="+cust_year_array[i]+" >"+cust_year_array[i]+"<option>";
                                }
                                rangeHtml = rangeHtml+"</select></td>"
                                $('#timeBasedTable tr:last').after('<tr>'+rangeHtml+'</tr>');
                            }
                            });
                    }
            }
            function enableYear2(){
                 var val = $('#selectYear1').val();
                var rangeHtml;
                var index = jQuery.inArray(val,cust_year_array);
                for(var i=index+1;i<cust_year_array.length;i++){
                    if(i==index)
                        rangeHtml =  rangeHtml+"<option value="+cust_year_array[i]+" selected>"+cust_year_array[i]+"</option>";
                    else
                        rangeHtml =  rangeHtml+"<option value="+cust_year_array[i]+">"+cust_year_array[i]+"</option>";
                }
                $('#selectYear2').html("");
                $('#selectYear2').html(rangeHtml);
            }

            function changeDiscreteLevel(){
                var levelType = $('#level').val();
                $.ajax({
                    url: '<%=request.getContextPath()%>/reportViewer.do?reportBy=getLevelDetailsForDiscrete&elementId='+<%=elementId%>+'&levelType='+levelType,
                    success: function(data){
                        var details = eval('('+data+')');
                        cust_year_array= details.cust_year_details;

               if(levelType=='Day'){
                    $("#timeBasedTable").find("tr:gt(1)").remove();
                    addDiscreteRow('false');
                }else if(levelType=='Year'){
                    $("#timeBasedTable").find("tr:gt(1)").remove();
                    addDiscreteRow('false');
                }else if(levelType=='Quarter'){
                    $("#timeBasedTable").find("tr:gt(1)").remove();
                    addDiscreteRow('false');
                }else if(levelType=='Month'){
                    $("#timeBasedTable").find("tr:gt(1)").remove();
                    addDiscreteRow('false');
                }

                     }
                });
            }
            function changeTrailingLevel(){
                var levelType = $('#level').val();
                var trailingHtml="";

                if(levelType=='Period'){                    
                    trailingHtml += "<option value='1 Period'>1 Period</option><option value='2 Period'>2 Period</option>";
                    trailingHtml += "<option value='3 Period'>3 Period</option><option value='4 Period'>4 Period</option>";
                    trailingHtml += "<option value='5 Period'>5 Period</option><option value='6 Period'>6 Period</option>";
                }
                
                if(levelType=='Year'){
                    trailingHtml += "<option value='1 Year'>1 Year</option><option value='2 Year'>2 Year</option>";
                    trailingHtml += "<option value='3 Year'>3 Year</option><option value='4 Year'>4 Year</option>";
                }
                
                 else if(levelType=='Quarter'){
                    trailingHtml += "<option value='1 Quarter'>1 Quarter</option><option value='2 Quarter'>2 Quarter</option>";
                    trailingHtml += "<option value='3 Quarter'>3 Quarter</option><option value='4 Quarter'>4 Quarter</option>";
                    trailingHtml += "<option value='5 Quarter'>5 Quarter</option><option value='6 Quarter'>6 Quarter</option>";
                    trailingHtml += "<option value='7 Quarter'>7 Quarter</option><option value='8 Quarter'>8 Quarter</option>";
                }  
                
                else if(levelType=='Month'){
                    trailingHtml += "<option value='1 Month'>1 Month</option><option value='2 Month'>2 Month</option>";
                    trailingHtml += "<option value='3 Month'>3 Month</option><option value='4 Month'>4 Month</option>";
                    trailingHtml += "<option value='5 Month'>5 Month</option><option value='6 Month'>6 Month</option>";
                    trailingHtml += "<option value='7 Month'>7 Month</option><option value='8 Month'>8 Month</option>";
                    trailingHtml += "<option value='9 Month'>9 Month</option><option value='10 Month'>10 Month</option>";
                    trailingHtml += "<option value='11 Month'>11 Month</option><option value='12 Month'>12 Month</option>";
               }
                else if(levelType=='Day'){
                    trailingHtml += "<option value='1 Day'>1 Day</option><option value='2 Day'>2 Day</option><option value='3 Day'>3 Day</option>";
                    trailingHtml += "<option value='4 Day'>4 Day</option><option value='5 Day'>5 Day</option><option value='6 Day'>6 Day</option>";
                    trailingHtml += "<option value='7 Day'>7 Day</option><option value='8 Day'>8 Day</option><option value='9 Day'>9 Day</option>";
                    trailingHtml += "<option value='10 Day'>10 Day</option><option value='11 Day'>11 Day</option><option value='12 Day'>12 Day</option>";
                    trailingHtml += "<option value='13 Day'>13 Day</option><option value='14 Day'>14 Day</option><option value='15 Day'>15 Day</option>";
                    trailingHtml += "<option value='16 Day'>16 Day</option><option value='17 Day'>17 Day</option><option value='18 Day'>18 Day</option>";
                    trailingHtml += "<option value='19 Day'>19 Day</option><option value='20 Day'>20 Day</option><option value='21 Day'>21 Day</option>";
                    trailingHtml += "<option value='22 Day'>22 Day</option><option value='22 Day'>22 Day</option><option value='23 Day'>23 Day</option>";
                    trailingHtml += "<option value='25 Day'>25 Day</option><option value='26 Day'>26 Day</option><option value='27 Day'>27 Day</option>";
                    trailingHtml += "<option value='28 Day'>28 Day</option><option value='29 Day'>29 Day</option><option value='30 Day'>30 Day</option>";
                }        
                
                $("#trailingPeriod").html(trailingHtml);
            }

            function addDiscreteRow(flag){
                var levelType = $('#level').val();
                if(levelType=='Year'){
                    var tableObj = document.getElementById("timeBasedTable");
                    var rowCount = tableObj.rows.length;
                    var discreteHtml = "<td width='40%' class='myhead' align='left'><font>Year</font></td><td width='30%'>";
                    discreteHtml =  discreteHtml+"<select name='selectDiscreteYear"+rowCount+"' id='selectDiscreteYear"+rowCount+"'> ";
                    for(var i=0;i<cust_year_array.length;i++){
                        if(i==0)
                            discreteHtml = discreteHtml+"<option value="+cust_year_array[i]+" selected>"+cust_year_array[i]+"</option>";
                        else
                            discreteHtml =  discreteHtml+"<option value="+cust_year_array[i]+" >"+cust_year_array[i]+"</option>";
                    }

                    discreteHtml =  discreteHtml+"</select></td>";
                    if(flag=='true')
                    discreteHtml =  discreteHtml+"<td width='2%'><img border=\"0\" align=\"middle\" title=\"Add Row\" alt=\"\" src=\"/pi/icons pinvoke/plus-circle.png\" onclick=\"addDiscreteRow('true')\"></td><td width='20%'><img border=\"0\" align=\"middle\" title=\"Add Row\" alt=\"\" src=\"/pi/icons pinvoke/cross-circle.png\" onclick=\"deleteDiscreteRow()\"></td>";
                    else
                    discreteHtml =  discreteHtml+"<td width='2%'><img border=\"0\" align=\"middle\" title=\"Add Row\" alt=\"\" src=\"/pi/icons pinvoke/plus-circle.png\" onclick=\"addDiscreteRow('true')\"></td>";
                    $('#timeBasedTable tr:last').after('<tr>'+discreteHtml+'</tr>');
                }else if(levelType=='Quarter'){
                    var tableObj = document.getElementById("timeBasedTable");
                    var rowCount = tableObj.rows.length;
                    var discreteHtml = "<td width='40%' class='myhead' align='left'><font>Quarter</font></td><td width='30%'>";
                    discreteHtml =  discreteHtml+"<select name='selectDiscreteQuarter"+rowCount+"' id='selectDiscreteQuarter"+rowCount+"'> ";
                    for(var i=0;i<cust_year_array.length;i++){
                        if(i==0)
                            discreteHtml = discreteHtml+"<option value="+cust_year_array[i]+" selected>"+cust_year_array[i]+"</option>";
                        else
                            discreteHtml =  discreteHtml+"<option value="+cust_year_array[i]+" >"+cust_year_array[i]+"</option>";
                    }
                    discreteHtml =  discreteHtml+"</select></td>";
                    if(flag=='true')
                    discreteHtml =  discreteHtml+"<td width='2%'><img border=\"0\" align=\"middle\" title=\"Add Row\" alt=\"\" src=\"/pi/icons pinvoke/plus-circle.png\" onclick=\"addDiscreteRow('true')\"></td><td width='20%'><img border=\"0\" align=\"middle\" title=\"Add Row\" alt=\"\" src=\"/pi/icons pinvoke/cross-circle.png\" onclick=\"deleteDiscreteRow()\"></td>";
                    else
                    discreteHtml =  discreteHtml+"<td width='2%'><img border=\"0\" align=\"middle\" title=\"Add Row\" alt=\"\" src=\"/pi/icons pinvoke/plus-circle.png\" onclick=\"addDiscreteRow('true')\"></td>";

                    $('#timeBasedTable tr:last').after('<tr>'+discreteHtml+'</tr>');
                }else if(levelType=='Month'){
                     var tableObj = document.getElementById("timeBasedTable");
                     var rowCount = tableObj.rows.length;
                     var discreteHtml = "<td width='40%' class='myhead' align='left'><font>Month</font></td><td width='30%'>";
                     discreteHtml =  discreteHtml+"<select name='selectDiscreteMonth"+rowCount+"' id='selectDiscreteMonth"+rowCount+"'> ";
                    for(var i=0;i<cust_year_array.length;i++){
                        if(i==0)
                            discreteHtml = discreteHtml+"<option value="+cust_year_array[i]+" selected>"+cust_year_array[i]+"</option>";
                        else
                            discreteHtml =  discreteHtml+"<option value="+cust_year_array[i]+" >"+cust_year_array[i]+"</option>";
                    }

                      discreteHtml =  discreteHtml+"</select></td>";
                      if(flag=='true')
                      discreteHtml =  discreteHtml+"<td width='2%'><img border=\"0\" align=\"middle\" title=\"Add Row\" alt=\"\" src=\"/pi/icons pinvoke/plus-circle.png\" onclick=\"addDiscreteRow('true)\"></td><td width='20%'><img border=\"0\" align=\"middle\" title=\"Add Row\" alt=\"\" src=\"/pi/icons pinvoke/cross-circle.png\" onclick=\"deleteDiscreteRow()\"></td>";
                      else
                      discreteHtml =  discreteHtml+"<td width='2%'><img border=\"0\" align=\"middle\" title=\"Add Row\" alt=\"\" src=\"/pi/icons pinvoke/plus-circle.png\" onclick=\"addDiscreteRow('true)\"></td>";

                      $('#timeBasedTable tr:last').after('<tr>'+discreteHtml+'</tr>');
                }else 
                    if(levelType=='Day'){
                    var tableObj = document.getElementById("timeBasedTable");
                    var rowCount = tableObj.rows.length;
                    var discreteHtml;
                    if(flag=='true')
                    discreteHtml = "<td width='40%' class='myhead' align='left'><font>Day</font></td><td width='60%'><input type='text' id='datepickerDiscrete"+rowCount+"' name='datepickerDiscrete"+rowCount+"' maxlength=100 value=''></td><td width='1%'><img border=\"0\" align=\"middle\" title=\"Add Row\" alt=\"\" src=\"/pi/icons pinvoke/plus-circle.png\" onclick=\"addDiscreteRow('true')\"></td><td width='1%'><img border=\"0\" align=\"middle\" title=\"Add Row\" alt=\"\" src=\"/pi/icons pinvoke/cross-circle.png\" onclick=\"deleteDiscreteRow()\"></td>";
                    else
                    discreteHtml = "<td width='40%' class='myhead' align='left'><font>Day</font></td><td width='60%'><input type='text' id='datepickerDiscrete"+rowCount+"' name='datepickerDiscrete"+rowCount+"' maxlength=100 value=''></td><td width='1%'><img border=\"0\" align=\"middle\" title=\"Add Row\" alt=\"\" src=\"/pi/icons pinvoke/plus-circle.png\" onclick=\"addDiscreteRow('true')\"></td>";

                    $('#timeBasedTable tr:last').after('<tr id=row'+rowCount+">"+discreteHtml+'</tr>');
                    testDiscrete(rowCount);
                }

            }
            function changeDiscreteRow(){
                var levelType = $("#levelType").val();
                if(levelType=='Year'){
                    var tableObj = document.getElementById("timeBasedTable");
                    var rowCount = tableObj.rows.length;

                }
            }


            function changeTimeBasedType(){
                var type = $("#TypeDropDown").val();
                if(type=='Rolling'){
                    buildRolling();
                }
                if(type=='Discrete'){
                    buildDiscrete();
                }
                if(type=='Range'){
                    buildRange();
                }
                if(type=='Trailing'){
                    buildTrailing();
                }

            }

            function buildRolling(){
                $("#timeBasedTable").find("tr:gt(1)").remove();
                var divhtml = "<option value ='Day'>Day</option>";
                $("#level").val("");
                $("#level").html(divhtml);
                var tableObj = document.getElementById("timeBasedTable");
                var rowCount = tableObj.rows.length;
                var row = tableObj.insertRow(rowCount);
                var rollingHtml = "<td width='40%' class='myhead' align='left'><font>Start Value<font></td><td width='60%' align='left'><input type = 'text'  onkeypress='javascript:return isNumberevent(event)'  name='RollingStartVal' id = 'RollingStartVal'></td>";
                row.innerHTML = rollingHtml;
                rowCount++;
                var row1 = tableObj.insertRow(rowCount);
                rollingHtml = "<td width='40%' class='myhead' align='left'><font>End Value</font></td><td width='60%'><input type='text' onkeypress='javascript:return isNumberevent(event)' name='RollingEndVal' id='RollingEndVal'></td>";
                row1.innerHTML = rollingHtml;
            }

            function buildDiscrete(){

                var levelHtml = "<option value='Year'>Year</option><option value='Quarter'>Quarter</option><option value='Month'>Month</option><option value='Day'>Day</option>";
                $("#level").html(levelHtml);
                changeDiscreteLevel();

            }
            function buildTrailing(){
                $("#timeBasedTable").find("tr:gt(1)").remove();
                var levelHtml = "<option value='' selected>--Select--</option><option value='Period' onclick=\'changeTrailingLevel()\'>Period</option><option value='Year' onclick=\'changeTrailingLevel()\'>Year</option><option value='Quarter' onclick=\'changeTrailingLevel()\'>Quarter</option><option value='Month' onclick=\'changeTrailingLevel()\'>Month</option><option value='Day' onclick=\'changeTrailingLevel()\'>Day</Day>";
                $("#level").html(levelHtml);
                var tableObj = document.getElementById("timeBasedTable");
                var rowCount = tableObj.rows.length;
                var row = tableObj.insertRow(rowCount);
                var trailingHtml = "";
                trailingHtml += "<td width='40%' class='myhead' align='left'><font>For Last how many periods: </font></td>";
                trailingHtml += "<td><select name='trailingPeriod' id='trailingPeriod'></td>";
                row.innerHTML = trailingHtml;             

            }

            function deleteDiscreteRow(){
                $('#timeBasedTable tr:last').remove();
            }


            function buildRange(){
                var levelHtml = "<option value='Year'>Year</option><option value='Quarter'>Quarter</option><option value='Month'>Month</option><option value='Day'>Day</option>";
                $("#level").html(levelHtml);
                changeLevel();
            }

        </script>
    </body>
</html>
