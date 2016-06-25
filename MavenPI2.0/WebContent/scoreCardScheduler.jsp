<%-- 
    Document   : scoreCardScheduler
    Created on : Apr 11, 2011, 11:15:42 AM
    Author     : progen
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" import="java.util.Locale,com.progen.i18n.TranslaterHelper"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%
            String themeColor = "blue";
            Locale locale = null;
            if (session.getAttribute("theme") == null) {
                session.setAttribute("theme", themeColor);
            } else {
                themeColor = String.valueOf(session.getAttribute("theme"));
            }
            locale = (Locale) session.getAttribute("userLocale");
            int userId = Integer.parseInt((String) session.getAttribute("USERID"));
            String mode=request.getParameter("mode");
            
            if(request.getAttribute("mode")!=null)
             mode=request.getAttribute("mode").toString();
            String json=(String)request.getAttribute("json");

            String isEdit="";
            if(request.getAttribute("isEdit")!=null && request.getAttribute("isEdit").toString().equalsIgnoreCase("readOnly"))
                isEdit="disabled";
            String contextpath=request.getContextPath();
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Scorecard Scheduler</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script type="text/javascript" src="<%=contextpath%>/javascript/lib/jquery/js/jquery-1.4.2.min.js"></script>
        <script type="text/javascript" src="<%=contextpath%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>
        <link rel="stylesheet" href="<%=contextpath%>/stylesheets/style.css" type="text/css" media="print, projection, screen">
        <link type="text/css" href="<%=contextpath%>/stylesheets/themes/<%=themeColor %>/jquery-ui-1.7.3.custom.css" rel="stylesheet" />
        <link type="text/css" href="<%=contextpath%>/stylesheets/themes/<%=themeColor %>/ReportCss.css" rel="stylesheet" />
        <Script type="text/javascript"  src="<%=contextpath%>/tracker/JS/testdate.js"></Script>
        <Script type="text/javascript"  src="<%=contextpath%>/ReportScheduler/dateSelection.js"></Script>
<!--        <Script type="text/javascript"  src="<%=request.getContextPath()%>/tracker/JS/dateSelection.js"></Script>-->
        <link type="text/css" href="<%=contextpath%>/stylesheets/themes/<%=themeColor %>/metadataButton.css" rel="stylesheet" />
        <link type="text/css" href="<%=contextpath%>/stylesheets/themes/<%=themeColor%>/TableCss.css" rel="stylesheet" />
        <link href="<%=contextpath%>/stylesheets/themes/<%=themeColor%>/style.css" rel="stylesheet" type="text/css">
       

    </head>
    <body>
        <table style="width:100%">
                <tr>
                    <td valign="top" style="width:50%;">
                        <jsp:include page="/Headerfolder/headerPage.jsp"/>
                    </td>
                </tr>
            </table>
        <center>
            <form action=""  name="scoreCardForm" id="scoreCardForm" method="post">
             <div class="navtitle1" style=" max-width: 100%; cursor: auto; height: 20px;"align="left">
                    <span> <font size="2" style="font-weight: bolder">Scorecard Tracker</font><b> </b></span>
             </div>
             <table align="right"><tr><td>
             <input type="button" value="<%=TranslaterHelper.getTranslatedString("SCHEDULER_HOME", locale)%>" onclick="javascript:goSchedulerHome()" class="navtitle-hover" style="width:auto"  style="font-size:10px;color:#2191C0;;font-weight:bold;text-decoration: none;font-family:Georgia">
             </td></tr></table><br><br>
         <div align="center" id="trackerDiv" style="width: 50%; height: auto; border: 4px solid rgb(180, 217, 238);">
                 <br><br>
            <table align="center" id="trackerTable">
                <tr>
                    <td class="myhead">
                       Schedule Scorecard Name
                    </td>
                    <td align="left">
                        <input type="text" name="sTrackerName" id="sTrackerName" value="" <%=isEdit%>>
                    </td>
                </tr>
                <tr>
                    <td class="myhead">
                      Business Role
                    </td>
                    <td align="left">
                        <select id="buzzRole" name="buzzRole" onchange="getAllScoreCards()" <%=isEdit%>>
                            <option>
                                select
                            </option>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td class="myhead">
                      Select Scorecard
                    </td>
                    <td align="left">
                        <select id="scoreCard" name="scoreCard" onchange="showTimeDetais()" <%=isEdit%>>
                            <option id="select" value="select">
                                select
                            </option>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td class="myhead">
                       Frequency
                    </td>
                    <td>
                         <table  align="center">
                        <tr>
<!--                            <Td class="myhead" width="40%"><%=TranslaterHelper.getTranslatedString("FREQUENCY", locale)%></Td>-->
                            <Td width="10%">
                                <Select name="frequency" id="frequency" class="myTextbox5" onchange="addDate(this)"  style="width:120px" <%=isEdit%>>
                                    <Option value="1"><%=TranslaterHelper.getTranslatedString("DAILY", locale)%></Option>
                                    <Option value="2"><%=TranslaterHelper.getTranslatedString("MONTHLY", locale)%></Option>
                                    <Option value="3"><%=TranslaterHelper.getTranslatedString("QUARTELY", locale)%></Option>
                                    <Option value="4"><%=TranslaterHelper.getTranslatedString("YEARLY", locale)%></Option>
                                </Select>
                            </Td>

                            <td>
                                <div id="dateSelect" style="display:none">
                                    Month
                                    <select onchange="correctDate(this.form,this)" name="alertMonth" <%=isEdit%>>
                                        <script language="JavaScript" type="text/javascript">
                                            for(i=1; i<13; i++){
                                                if(i==month){
                                                    sel = "selected"
                                                }
                                                else{
                                                    sel = ""
                                                }
                                                document.write("<option value="+[i]+" "+sel+">"+months[i]+"\n")
                                            }

                                        </script>
                                    </select>
                                    <select name="alertDate" style="display:none" id="alertDate" <%=isEdit%>>

                                        <script language="JavaScript" type="text/javascript">
                                            var tl=(month==4 || month==6 || month==9 || month==11) ? 30 : (month==2) ? 28 : 31
                                            for(var j=1; j<=tl; j++){
                                                if(j==date){
                                                    sel = "selected"
                                                }
                                                else{
                                                    sel = ""
                                                }
                                                document.write("<option value="+j+" "+sel+">"+j+"\n")
                                            }
                                        </script>
                                    </select>


                                </div>
                            </td>
                            <td>
                                <div id="onlyDateSelect" style="display:none">
                                Day
                                    <select name="alertDate" <%=isEdit%>>

                                        <script language="JavaScript" type="text/javascript">
                                            var tl=(month==4 || month==6 || month==9 || month==11) ? 30 : (month==2) ? 28 : 31
                                            for(j=1; j<=tl; j++){
                                                if(j==date){
                                                    sel = "selected"
                                                }
                                                else{
                                                    sel = ""
                                                }
                                                document.write("<option value="+j+" "+sel+">"+j+"\n")
                                            }
                                            document.write("<option value='last'>Last\n")
                                        </script>
                                    </select>
                                </div>
                            </td>
                             <Td>
                                <div id="timeselect" style="display: block">
                                    <%=TranslaterHelper.getTranslatedString("HOURS", locale)%>
                                    <select name="hrs" id="hrs" <%=isEdit%>>
                                        <%for (int i = 00; i < 24; i++) {%>
                                        <option  value="<%=i%>"><%=i%></option>
                                        <%}%>
                                    </select>
                                   <%=TranslaterHelper.getTranslatedString("MINUTES", locale)%>
                                   <select name="mins" id="mins" <%=isEdit%>>
                                        <%for (int i = 00; i < 60; i++) {%>
                                        <option  value="<%=i%>"><%=i%></option>
                                        <%}%>
                                    </select>
                                </div>
                            </Td>
<!--                            </td>-->
                        </tr>
                    </table>
                    </td>
                </tr>
                <tr>
                    <td class="myhead">
                       Start Date
                    </td>
                    <td align="left">
                        <input type="text" name="startDate" id="startDate" <%=isEdit%>>
                    </td>
                </tr>
                <tr>
                    <td class="myhead">
                       End Date
                    </td>
                    <td align="left">
                        <input type="text" name="endDate" id="endDate" <%=isEdit%> >
                    </td>
                </tr>
                </table>

                <br>
                <table id="timeDetailsTable" width="85%">
                    
                </table>
                <br>
                <table align="center" width="85%" >
                    <tr>
                        <td align="center" class="myhead" colspan="2">
                            Score Rules
                        </td>
                    </tr>
                </table>
                <table align="center" width="85%" id="scoreRulesTable">
                    <tr id="row0" >
                          
                                    <td>
                                        Score When
                                    </td>
                                    <td>
                                       <select name="operator" id="oper0" onchange="showEndValue(this.id)" <%=isEdit%>>
                                          <option value="<"><</option>
                                          <option value="<="><=</option>
                                          <option value=">">></option>
                                          <option value=">=">>=</option>
                                          <option value="<>"><></option>
                                          <option value="=">=</option>
                                          <option value="!=">!=</option>
                                        </select>
                                    </td>
                                    <td colspan="2" id="startTd0">
                                        <input type="text" name="stScore" id="stScore0" style="width:30px" <%=isEdit%>>
                                    </td>
                                    <td id="endTd0" style="display: none">
                                        And &nbsp;<input type="text" name="endScore" id="endScore0" style="width:30px" <%=isEdit%>>
                                    </td>
                                    <td>
                                        Send Mail to
                                    </td>
                                    <td>
                                        <input type="text" name="email" id="email0" style="" <%=isEdit%>>
                                    </td>
                                    <td id="addRow0">
                                        <span class="ui-icon ui-icon-circle-plus" onclick="addRow()" title="Add Row"></span>
                                    </td>
                                    <td id="deleteRow0">
                                        <span class="ui-icon ui-icon-circle-close" onclick="deleteRow('row0')" title="Delete Row"></span>
                                    </td>
                               
                          
                    </tr>
                </table>
                <br><br>
                <table align="center">
                <tr>
                    <%if(!isEdit.equalsIgnoreCase("disabled")){%>
                                <td colspan="2">
                                    <input type="button" style='width:80px' class="navtitle-hover" value="Save" onclick="saveScorecardTracker()">
                                </td>
                     <%}%>
                                <td>
                                    <input type="button" style='width:80px' class="navtitle-hover" value="Run/Execute" onclick="runScorecardTracker()">
                                </td>
                </tr>
            </table>
                <br>
             </div>
            </form>
        </center>
                                 <script type="text/javascript">

       

            $(document).ready(function(){
                    $('#startDate').datepicker({
                    changeMonth: true,
                    changeYear: true
                    });
                    $('#endDate').datepicker({
                        changeMonth: true,
                        changeYear: true
                    });

                $.ajax({
                        url:"<%=request.getContextPath()%>/scheduler.do?reportBy=getAllBusRoles",
                        success: function(data)
                        {
                            $("#buzzRole").html("<option id='select' value='select'>select</option>"+data)
                            if('<%=mode%>'=='Edit')
                            {
                                var json=eval("("+'<%=json%>'+")")
                                $("#sTrackerName").val(json.trackerName)
                                $("select#buzzRole").attr('value',json.folderId);
                                var buzzRoleId=$("#buzzRole").val();
                                $.ajax({
                                            url:"<%=request.getContextPath()%>/scheduler.do?reportBy=getScoreCardsForScheduler&folderId="+buzzRoleId,
                                            success: function(data)
                                            {
                                                $("#scoreCard").html("<option id='select' value='select'>select</option>"+data)
                                                $("select#scoreCard").attr('value',json.sCardId);
//                                                showTimeDetais();
                                                //code to wait for 2 seconds
                                                 
                                                 var asOfDt=new Date(json.asOfDate);
                                                 var sd = new Date(json.startDate)
                                                 var ed = new Date(json.endDate)
                                                 var strtDate=1+(sd.getMonth())+"/"+sd.getDate()+"/"+sd.getFullYear();
                                                 var endDate=1+(ed.getMonth())+"/"+ed.getDate()+"/"+ed.getFullYear();
                                                 var asOfDate=1+(asOfDt.getMonth())+"/"+asOfDt.getDate()+"/"+asOfDt.getFullYear();

                                                 var particularDay=json.particularDay;
                                                 $("#startDate").val(strtDate);
                                                 $("#endDate").val(endDate);
                                                 var time=json.scheduleTime.split(":");
                                                 $("select#hrs").attr("value",time[0]);
                                                 $("select#mins").attr("value",time[1]);
                                                 
                                                 
                                                 for(var i=0;i<json.ruleLst.length;i++)
                                                 {
                                                    if(i>0)
                                                     {
                                                         addRow();
                                                     }
                                                    
                                                    $("select#oper"+i).attr('value',json.ruleLst[i].operator)
                                                    $("#stScore"+i).val(json.ruleLst[i].startValue)
                                                    if(json.ruleLst[i].operator=='<>')
                                                    {
                                                       $("#startTd"+i).attr('colspan',1);
                                                       $("#endTd"+i).show();
                                                       $("#endScore"+i).val(json.ruleLst[i].endValue)
                                                    }
                                                    else
                                                    {
                                                       $("#startTd"+i).attr('colspan',2);
                                                       $("#endTd"+i).hide();
                                                    }
                                                    $("#email"+i).val(json.ruleLst[i].email)
                                                    if('<%=isEdit%>'=='disabled')
                                                    {
                                                        $("#oper"+i).attr("disabled",true);
                                                        $("#stScore"+i).attr("disabled",true);
                                                        $("#endScore"+i).attr("disabled",true);
                                                        $("#email"+i).attr("disabled",true);
                                                        $("#addRow"+i).hide();
                                                        $("#deleteRow"+i).hide();
                                                    }
                                                 }
//                                                 var start = new Date().getTime();
//                                                 var cur = start
//                                                 while(cur - start < 2000)
//                                                 {
//                                                   cur = new Date().getTime();
//                                                 }
                                                 $.ajax({
                                                 url:"<%=request.getContextPath()%>/scheduler.do?reportBy=getScorecardTimeDetails&folderId="+json.folderId+"&scoreId="+json.sCardId,
                                                        success: function(data)
                                                        {
                                                           $("#timeDetailsTable").html(data)
                                                           $("#datepicker1").attr('onclick','');
                                                           $("#datepicker1").attr('onchange','');
                                                           $('#datepicker1').datepicker({
                                                             changeMonth: true,
                                                              changeYear: true
                                                           });
                                                           $("table#timeDetailsTable").animate({
                                                            width: "20%"
                                                            }, 1500 );
                                                            $("table#timeDetailsTable").animate({
                                                            width: "85%"
                                                            }, 1500 );
                                                            $("#datepicker1").val(asOfDate);
                                                            $("#CBO_PRG_COMPARE").attr("value",json.compareWith);
                                                            $("#CBO_PRG_PERIOD_TYPE").attr("value",json.period);
                                //                           $("#trackerTable").html($("#trackerTable").html()+data)
                                                            if('<%=isEdit%>'=='disabled')
                                                            {
                                                                $("#datepicker1").attr("disabled",true);
                                                                $("#CBO_PRG_COMPARE").attr("disabled",true);
                                                                $("#CBO_PRG_PERIOD_TYPE").attr("disabled",true);

                                                            }
                                                        }

                                                });

                                            }

                                 });
                            }
                        }

                });
            });
         function showEndValue(rowId)
         {
             var rowNum=rowId.replace("oper","");
             var oper=$("#"+rowId).val();
             if(oper=="<>")
                 {
                 $("#endTd"+rowNum).show();
                 $("#startTd"+rowNum).attr("colspan",'1')
                 }
             else{
                 $("#endTd"+rowNum).hide();
                 $("#startTd"+rowNum).attr("colspan",'2')
             }
         }
         function addRow()
         {
             var scoreTable=document.getElementById("scoreRulesTable");
             var rowCount=scoreTable.rows.length;
             var rowCountArr=new Array();
             $("#scoreRulesTable > tbody > tr " ).each(function() {
                  rowCountArr.push($(this).attr('id').replace("row",""));
             })
             var max=Math.max.apply(Math, rowCountArr);
             var newRow=max+1;
             var row=scoreTable.insertRow(rowCount);
             row.id='row'+newRow;
             var html="";
//             html+="<tr id='row"+newRow+"'>"
             html+="<td>"
             html+="Score When"
             html+="</td>"
             html+="<td>"
             html+="<select onchange=showEndValue(this.id) id='oper"+newRow+"' name='operator'>"
             html+="<option value='<'><</option>"
             html+="<option value='<='><=</option>"
             html+="<option value='>'>></option>"
             html+="<option value='>='>>=</option>"
             html+="<option value='<>'><></option>"
             html+="<option value='='>=</option>"
             html+="<option value='!='>!=</option>"
             html+="</select>"
             html+="</td>"
             html+="<td colspan='2' id='startTd"+newRow+"'><input type='text' style='width: 30px;' id='stScore"+newRow+"' name='stScore'></td>"
             html+="<td style='display: none;' id='endTd"+newRow+"'>"
             html+="And &nbsp;<input type='text' style='width: 30px;' id='endScore"+newRow+"' name='endScore'>"
             html+="</td>"
             html+="<td>"
             html+=" Send Mail to"
             html+="</td>"
             html+="<td>"
             html+="<input type='text' style='' id='email"+newRow+"' name='email'>"
             html+="</td>"
             html+="<td id='addRow"+newRow+"'>"
             html+="<span onclick='addRow()' class='ui-icon ui-icon-circle-plus' title='Add Row'></span>"
             html+="</td>"
             html+="<td id='deleteRow"+newRow+"'>"
             html+="<span onclick=deleteRow('"+row.id+"') class='ui-icon ui-icon-circle-close' title='Delete Row'></span>";
             html+="</td>"
//             html+="</tr>";

//             row.innerHTML=html;
        $("#"+row.id).append(html)

         }
         function goSchedulerHome()
         {
                document.forms.scoreCardForm.action = "<%=request.getContextPath()%>/home.jsp#Scheduler";
                document.forms.scoreCardForm.submit();
          }
         function deleteRow(id)
         {
             var scoreTable=document.getElementById("scoreRulesTable");
             var rowCount=scoreTable.rows.length;
             if(rowCount>1)
                 {
                     $("#"+id).remove();
                 }
         }
         function getAllScoreCards()
         {
             var buzzRoleId=$("#buzzRole").val();
             $.ajax({
                        url:"<%=request.getContextPath()%>/scheduler.do?reportBy=getScoreCardsForScheduler&folderId="+buzzRoleId,
                        success: function(data)
                        {
                            $("#scoreCard").html("<option id='select' value='select'>select</option>"+data)
                        }

                });
         }
         function saveScorecardTracker()
         {
               var strdate=$("#startDate").val();
               var enddate=$("#endDate").val();
                $.ajax({
                        url:'<%=request.getContextPath()%>/calenderSetUpAction.do?timeCalenders=valiDateCalnder&startYear='+strdate+'&endYear='+enddate+'&scheduler='+"ScorecardTracker",
                        success:function(data){
                            if(data=='true'){
                                $.post("<%=request.getContextPath()%>/scheduler.do?reportBy=saveScorecardTracker&mode=<%=mode%>", $("#scoreCardForm").serialize() ,
                                function(data){
                                     alert("Scorecard Tracker saved successfully")
                                });
                             }else
                             {
                                alert("Please select End Year correctly.")
                             }
                         }
                     });
         }

         function runScorecardTracker()
         {
               var strdate=$("#startDate").val();
               var enddate=$("#endDate").val();
                $.ajax({
                        url:'<%=request.getContextPath()%>/calenderSetUpAction.do?timeCalenders=valiDateCalnder&startYear='+strdate+'&endYear='+enddate+'&scheduler='+"ScorecardTracker",
                        success:function(data){
                            if(data=='true'){
                                $.post("<%=request.getContextPath()%>/scheduler.do?reportBy=saveScorecardTracker&mode=<%=mode%>&from=ScorePage&isEdit=<%=isEdit%>", $("#scoreCardForm").serialize() ,
                                function(data){
                                     $.ajax({
                                        url:"<%=request.getContextPath()%>/scheduler.do?reportBy=runScorecardTracker&from=ScorePage",
                                        success: function(data)
                                        {
                                             document.forms.scoreCardForm.action = "<%=request.getContextPath()%>/home.jsp#Scheduler";
                                             document.forms.scoreCardForm.submit();
                                        }

                                     });
                                });
                         }else{
                            alert("Please select End Year correctly.")
                         }
                        }
                   });
         }
         function showTimeDetais()
         {
             var buzzRoleId=$("#buzzRole").val();
             var scoreId=$("#scoreCard").val();
             $.ajax({
                        url:"<%=request.getContextPath()%>/scheduler.do?reportBy=getScorecardTimeDetails&folderId="+buzzRoleId+"&scoreId="+scoreId,
                        success: function(data)
                        {
                           $("#timeDetailsTable").html(data)
                           $("#datepicker1").attr('onclick','');
                           $("#datepicker1").attr('onchange','');
                           $('#datepicker1').datepicker({
                             changeMonth: true,
                              changeYear: true
                           });
                           $("table#timeDetailsTable").animate({
                            width: "20%"
                            }, 1500 );
                            $("table#timeDetailsTable").animate({
                            width: "85%"
                            }, 1500 );
//                           $("#trackerTable").html($("#trackerTable").html()+data)
                        }

                });
         }
         function validate()
         {
             var trackerName=$("#sTrackerName").val();
             var buzzrole=$("#buzzRole").val();
             var scorecard=$("#scoreCard").val();
             if(trackerName==undefined||trackerName=="")
             {
                     alert("Please enter Scorecard tracker name")
                     return false;
             }
            if(buzzrole=="select")
            {
                    alert("please select a business role")
                    return false;
            }
            if(scorecard=="select")
            {
                    alert("please select a scorecard")
                    return false;
            }
            else
            {
              return true;
            }

         }
        </script>
    </body>
</html>
