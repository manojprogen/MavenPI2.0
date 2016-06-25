<%-- 
    Document   : alppyAlarm
    Created on : Sep 20, 2010, 10:53:10 AM
    Author     : Administrator
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%
            String stickyId = request.getParameter("stickId");
            String contxPath=request.getContextPath();
%>

<html>
    <head>
        <script type="text/javascript" src="<%=contxPath%>/javascript/lib/jquery/js/jquery-1.4.2.min.js"></script>
        <link type="text/css" href="<%=contxPath%>/jQuery/jquery/themes/base/ui.all.css" rel="stylesheet" />
        <link type="text/css" href="<%=contxPath%>/jQuery/jquery/themes/base/ui.theme.css" rel="stylesheet" />
        <link type="text/css" href="<%=contxPath%>/jQuery/jquery/themes/base/ui.datepicker.css" rel="stylesheet" />
        <script type="text/javascript" src="<%=contxPath%>/javascript/ui.datepicker.js"></script>

        <link type="text/css" href="<%=contxPath%>/jQuery/jquery/demos.css" rel="stylesheet" />
        <link type="text/css" href="<%=contxPath%>/stylesheets/metadataButton.css" rel="stylesheet" />


<!--        <link href="<%=request.getContextPath()%>/stylesheets/html.css" type="text/css" rel="stylesheet">
        <link href="<%=request.getContextPath()%>/stylesheets/css.css" type="text/css" rel="stylesheet">-->







        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Apply Alarm</title>
       
    </head>
    <body>
        <table align="center" id="apply">
            <tr id="dateTR">
                <td rowspan="">
                    <input type="radio" name="group" value="setDate" id="dateInput" onclick="openTextBox('date')">
                </td>
                <td style="font-size: 12px;">
                    Set Date
                </td>
                <td>
                    <input type="text" name="date" id="date" value="" style="display: none;" >
                </td>
            </tr>
            <tr id="dayTR">
                <td>
                    <input type="radio" name="group" value="chooseDay" id="dayInput" onclick="openTextBox('day')">
                </td>
                <td style="font-size: 12px;">
                    Choose Day
                </td>
                <td>
                    <select name="day" id="day" style="display: none;" >
                        <option value="Sunday">Sunday</option>
                        <option value="Monday">Monday</option>
                        <option value="Tuesday">Tuesday</option>
                        <option value="Wednesday">Wednesday</option>
                        <option value="Thursday">Thursday</option>
                        <option value="Friday">Friday</option>
                        <option value="Saturday">Saturday</option>
                    </select>

                </td>
            </tr >
            <tr id="monthTR">
                <td>
                    <input type="radio" name="group" value="chooseMonthDay" id="monthInput" onclick="openTextBox('month')">
                </td>
                <td style="font-size: 12px;">
                    Choose Month Day
                </td>
                <td>
                    <select name="month" id="month" style="display: none;" >
                        <option value="First">First</option>
                        <option value="Last">Last</option>
                    </select>
                </td>
            </tr>

        </table>
        <table align="center">
            <tr>
                <td>
                    <input type="button" class="navtitle-hover" value="Set Reminder" onclick="setRemainder()">
                </td>
                <td>
                    <input type="button" class="navtitle-hover" value="Clear Reminder" onclick="clearRemainder()">
                </td>

            </tr>
        </table>
 <script type="text/javascript">
            $(function(){
                $('#date').datepicker({
                    changeMonth: true,
                    changeYear: true,
                    showButtonPanel: true,
                    numberOfMonths:1,
                    stepMonths: 1
                });
            });

            function clearRemainder(){
                $.ajax({
                  url:"stickyNoteAction.do?stickyNoteParam=clearAlarmForSticky&stickyId=<%=stickyId%>",
                  success:function(data){
                       parent.closeAlarmDiv();
                   }
                  });
            }
            var selectedId="";
           $(document).ready(function(){
               $.ajax({
                  url:"stickyNoteAction.do?stickyNoteParam=getAlarmForSticky&stickyId=<%=stickyId%>",
                  success:function(data){
//                      alert("data------"+data)
                     if(data!=null&&data!=""){
                         var rem=data.split(":");
//                         alert("rem0--"+rem[0])
                        
                         if(rem[0]=='Day'){
                            $("#dayInput").attr("checked", "checked");
                            $("select#day").attr("value",rem[1]);
                            $("#day").show();
                            selectedId='day';
                         }else if(rem[0]=='Month'){
                            $("#monthInput").attr("checked", "checked");
                            $("select#month").attr("value",rem[1]);
                            $("#month").show();
                             selectedId='month';
                         }else if(rem[0]=='Date'){
                            $("#dateInput").attr("checked", "checked");
                            $("input#date").attr("value",rem[1]);
                            $("#date").show();
                             selectedId='date';
                         }
                     }
                  }
               });
           });


           
            function openTextBox(txtId){
                
                if(txtId=='date'){
                    document.getElementById(txtId).style.display="";
                    document.getElementById('day').style.display="none";
                    document.getElementById('month').style.display="none";
                    //                   $("#day").html("");
                    //                   $("#monthDay").html("");
                }
                else if(txtId=='day'){
                    document.getElementById(txtId).style.display="";
                    document.getElementById('date').style.display="none";
                    document.getElementById('month').style.display="none";
                    //                      $("#setDateInput").html("");
                    //                      $("#monthDay").html("");
                }
                else if(txtId=='month'){
                    document.getElementById(txtId).style.display="";
                    document.getElementById('date').style.display="none";
                    document.getElementById('day').style.display="none";
                    //                      $("#setDateInput").html("");
                    //                      $("#day").html("");
                }
                selectedId=txtId;
            }
            function setRemainder(){
                var totDt=new Date();
                var dt=totDt.getDate();
                var mnth=totDt.getMonth()+1;
                var year=totDt.getFullYear();
                var flag=false;
                var trId=selectedId+"TR";
                if(selectedId!=""){
                    var stickyId='<%=stickyId%>';
                    var trObj=document.getElementById(trId);
                    var tdObj=trObj.getElementsByTagName('td');
                    var selValue="";
                    var selType="";
                    if(selectedId=='date'){
                        var inputObj=tdObj[2].getElementsByTagName('input');
                        selValue=inputObj[0].value;
                        selType="Date";

                        var selMnth=selValue.substring(0, 2);
                        var selDt=selValue.substring(3, 5);
                        var selYr=selValue.substring(6, 10);
//                        alert("selYR--"+selYr+"; yr"+year)
                        if(selYr>year){
                            flag=true;
                        }else if(selYr==year){
//                            alert("sel--"+selMnth+"-mnth"+mnth)
                            if(selMnth>mnth){
                                flag=true;
                            }else if(selMnth==mnth){
//                                alert("seldt--"+selDt+"-dt"+dt)
                                if(selDt>dt)
                                    flag=true;
                            }
                        }
//                        alert("flag--"+flag)
//                        alert("seledate--"+selDt+"-m-"+selMnth+"--y-"+selYr)
                   
                    }else if(selectedId=='day'){
                        var inputObj=tdObj[2].getElementsByTagName('select');
                        selValue=inputObj[0].value;
                        selType="Day";
                        flag=true;
                    }else if(selectedId=='month'){
                        var inputObj=tdObj[2].getElementsByTagName('select');
                        selValue=inputObj[0].value;
                        selType="Month";
                        flag=true;
                    }

                    if(selType!=""&&selValue!=""&&flag==true){
                       $.ajax({
                            url:"stickyNoteAction.do?stickyNoteParam=saveRemainderForStrickyNote&selType="+selType+"&selValue="+selValue+"&stickyId="+stickyId,
                            success:function(data){
                                if(data=='success')
                                    alert("Reminder Saved Successfully");
                            }
                       });
                       parent.closeAlarmDiv();

                    }else{
                        if(selType=="")
                            alert("Please select one option")
                        else if(selValue=="")
                            alert("Please choose a date")
                        else if(flag==false)
                            alert("Reminder date should be after the current date")
                    }
                }else{
                     alert("Please select one option")
                }
                
            }
        </script>
    </body>
</html>
