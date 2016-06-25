<%-- 
    Document   : excelToSqlServer.jsp
    Created on : Jul 22, 2010, 3:36:40 PM
    Author     : Sathish
--%>

<%@page import="utils.db.ProgenConnection"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.Connection"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>PiEE ETL</title>
        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/metadataButton.css" rel="stylesheet" />-
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/ui.datepicker.js"></script>

        <script type="text/javascript">
            $(function(){
                $("#tabs").tabs().find(".ui-tabs-nav").sortable({axis:'x'});
            });
        </script>

        <script type="text/javascript">
            $(function() {
                $('#startDateId').datepicker({
                    changeMonth: true,
                    changeYear: true,
                    showButtonPanel: true,
                    numberOfMonths:1,
                    stepMonths: 1
                });
                $('#endDateId').datepicker({
                    changeMonth: true,
                    changeYear: true,
                    showButtonPanel: true,
                    numberOfMonths: 1,
                    stepMonths: 1

                });
            });

        </script>
        <script type="text/javascript">
            function send(sdate,edate,selectedTab){
                var selTab=document.getElementById(selectedTab).value;
                // var path=document.getElementById("path").value;
                //  var ext = path.substring(path.lastIndexOf('.') + 1);
                // if(ext == "xls" )
                if(selTab=="")
                {
                    alert("Please select Table from the list")

                }
                //                else if(sdate!=null||edate!=null){
                //                    if((sdate=="")||(edate=="")){
                //                       alert("Please enter Start date and End date")
                //                    }
                //                }
                else
                {
                    $.ajax({
                        url: '<%=request.getContextPath()%>/etlUpload.do?etlUploadParam=checkDates&selTab='+selTab+'&stDate='+sdate+'&endDate='+edate,
                        success: function(data){
                            //alert("data "+data)
                            if(data=="CANNOT_UPLOAD"){
                                alert("Data exists in the table for the selected date range, you cannot upload data for the same range")
                            }else if(data=="CONFIRM_UPLOAD"){
                                var conf=confirm("Data exists in the table for the selected date range, do you want to delete and reload the data?")
                                if(conf==true){
                                    uploadData(selTab,sdate,edate);
                                }
                            }else{
                                uploadData(selTab,sdate,edate);
                            }
                        }
                    });
                }
            }
            function uploadData(tabName,stDate,endDate){
                document.getElementById("imgdiv").style.display=''
                $.ajax({
                    url: '<%=request.getContextPath()%>/etlUpload.do?etlUploadParam=loadData&selTab='+tabName+'&stDate='+stDate+'&endDate='+endDate,
                    success: function(data){
                        //alert("data---"+data)
                        document.getElementById("imgdiv").style.display='none'
                        alert(data)
                    }
                });
            }
            function lastUploadTime(elementId, lastUploadDiv){
                var tabName=document.getElementById(elementId).value;
                var dovObj=document.getElementById(lastUploadDiv);
                document.getElementById(lastUploadDiv).style.display='none'
                $.ajax({
                    url: '<%=request.getContextPath()%>/etlUpload.do?etlUploadParam=lastUploadTime&selTab='+tabName,
                    success: function(data){
                        if(data!=""){
                            document.getElementById(lastUploadDiv).style.display=''
                            dovObj.innerHTML=tabName+" Table was Last Uploaded on "+data;
                        }
                    }
                        
                });
            }
            function displayDateRange(elementId,dateDivId){
                var selectedVal=document.getElementById(elementId).value;
                var dateDivIdJ = '#'+dateDivId;
                var dateHtml = '<table id="dateTable"><tr><td>Start Date</td><td><input type="text" id="startDateId" name="startDateName" ></td></tr><tr><td>End Date</td><td><input type="text" id="endDateId" name="endDateName" ></td></tr></table>'
                $.ajax({
                    url: '<%=request.getContextPath()%>/etlUpload.do?etlUploadParam=showDates&selTab='+selectedVal,
                    success: function(data){
                        if(data=="y"){
                            document.getElementById(dateDivId).style.display=''
                              
                            if ( $('#dateTable').length == 0 )
                            {
                                //                                alert("Create dateTable");
                                $(dateDivIdJ).html(dateHtml);
                                $('#startDateId').datepicker({
                                    changeMonth: true,
                                    changeYear: true,
                                    showButtonPanel: true,
                                    numberOfMonths:1,
                                    stepMonths: 1
                                });
                                $('#endDateId').datepicker({
                                    changeMonth: true,
                                    changeYear: true,
                                    showButtonPanel: true,
                                    numberOfMonths: 1,
                                    stepMonths: 1
                                });
                            }
                            else
                            {
                                //                                alert("Move dateTable "+dateDivId);
                                $("#dateTable").appendTo(dateDivIdJ);
                                $("#startDateId").val('');
                                $("#endDateId").val('');
                            }
                        }else{
                            document.getElementById(dateDivId).style.display='none'
                        }
                    }
                });
            }
            function checkDate(selectedTab)
            {
                
                if ( $("#dateDiv:hidden").length == 0 || $("#dateDivAccess:hidden").length ==0 )
                {
                    var stDate= Date.parse(document.getElementById("startDateId").value)
                    var endDate=Date.parse(document.getElementById("endDateId").value)

                    if (stDate > endDate)
                    {
                        alert("End Date should be greater than to Start Date ")
                        return document.getElementById("endDateId").focus()
                    }else{
                        send(document.getElementById("startDateId").value,document.getElementById("endDateId").value,selectedTab)
                    }
                }
                else
                    send(null,null,selectedTab)
            }
            
            $(document).ready(function(){
                $.get('<%=request.getContextPath()%>/etlUpload.do?etlUploadParam=getListOfTables', function(data) {
                    var selectExcelObj=document.getElementById("selTab");
                    var selectAccessObj=document.getElementById("accessTabName");
                    // alert(data)
                    var optionList=data.split("@");
                   //  selectExcelObj.innerHTML=optionList[0];
                  //   selectAccessObj.innerHTML=optionList[1];
                    $("#selTab").html(optionList[0]);
                    $("#accessTabName").html(optionList[1]);
                });

            });


        </script>

    </head>
    <body >
        <form name="excelForm" method="post">
            <center>
                <%-- <div style="width:200px;height:100px;border:1px solid blue;">
            HTML borders are best created with CSS.
            </div>--%>

                <div align="center" style="width:50%;height:500px;border:1px solid  rgb(180, 217, 238);">

                    <hr/>
                    <div id="tabs">

                        <div style="width:80.5%;height: 19px;  background-color: rgb(180, 217, 238); color: black;display:block;" ><h4>Excel Upload</h4>
                        </div>
                        <div style="width:80%;height: auto; border-color:rgb(180, 217, 238);border:1px solid  rgb(180, 217, 238)">
                            <TABLE><tr>
                                    <td>
                                        <h2> Tables</h2>
                                    </td>
                                    <td>
                                        <select id="selTab" name="selTab" onchange="lastUploadTime('selTab','lastUploadDiv'),displayDateRange('selTab','dateDiv')">
                                        </select>
                                    </td>
                                </tr>
                            </TABLE>

                            <div id="dateDiv" style="display:none" >

                            </div>
                            <div id="imgdiv" style="display: none">
                                <img src="<%=request.getContextPath()%>/images/ajax.gif" align="middle"   width="75px" height="75px"  style="position:absolute;">
                            </div>
                            <br>
                            <div id="lastUploadDiv" style="display: none;width:410px;height: 10px;color: red"></div>
                            <br>
                            <!--                            <input type="file" id="path" value="" name="path" >-->
                            <input type="button" value="Load" onclick="checkDate('selTab')"  class="navtitle-hover">
                            <br><br>
                            <p> <font color="red">*</font>Please run the Loading task on the server machine
                        </div>
                        <br>
                        <!--                        </div>-->
                        <!--                        <div id="tabs-2">-->
                        <div style="width:80.5%;height: 19px;  background-color: rgb(180, 217, 238); color: black;display:block;" ><h4>Access Upload</h4>
                        </div>
                        <div style="width:80%;height: auto; border-color:rgb(180, 217, 238);border:1px solid  rgb(180, 217, 238)">
                            <table>
                                <tr>
                                    <td>
                                        Table Name
                                    </td>
                                    <td>
                                        <select id="accessTabName" name="accessTabName" onchange="lastUploadTime('accessTabName','lastUploadAccessDiv'),displayDateRange('accessTabName','dateDivAccess')">
                                        </select>

                                    </td>
                                </tr>
                            </table>
                            <br>
                            <div id="lastUploadAccessDiv" style="display: none;width:410px;height: 10px;color: red"></div><br>
                            <div id="dateDivAccess" style="display:none" >
                            </div>
                            <table><tr><td><input type="button" name="accessload" id="accessload" onclick="checkDate('accessTabName')" value="Load" class="navtitle-hover" ></td></tr></table>



                        </div>
                        <div id="imgdiv1" style="display: none">
                            <img src="<%=request.getContextPath()%>/images/ajax.gif" align="middle"   width="75px" height="75px"  style="position:absolute;">
                        </div>
                        <!--                        </div>-->
                    </div>

                    <br><br><br>
                </div>
            </center>

            <div id="imgdiv" style="display: none">
                <img src="<%=request.getContextPath()%>/images/ajax.gif" align="middle"   width="75px" height="75px"  style="position:absolute;">
            </div>
        </form>
    </body>
</html>
