<%-- 
    Document   : createUserCalender
    Created on : Dec 17, 2009, 12:57:02 PM
    Author     : Saurabh
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" import="prg.db.*"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <script src="javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <!-- below five lines only added by bharathi reddy on 26-08-09 -->


        <script src="javascript/jquery.simplemodal-1.1.1.js" type="text/javascript"></script>
        <script src="javascript/jquery.contextMenu.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/jQuery/jquery/ui/ui.datepicker.js"></script>
        <link type="text/css" href="stylesheets/demos.css" rel="stylesheet" />
        <link type="text/css" href="stylesheets/ui.all.css" rel="stylesheet" />
        <link rel="stylesheet" href="stylesheets/treeviewstyle/jquery.treeview.css" />
        <link href="stylesheets/jquery.contextMenu.css" rel="stylesheet" type="text/css" />
        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/metadataButton.css" rel="stylesheet" />
                       <%
                                String query = "select connection_id, connection_name from prg_user_connections";
                                PbDb pbdb = new PbDb();
                                PbReturnObject pbconobj = new PbReturnObject();
                                pbconobj = pbdb.execSelectSQL(query);
                                String connId = request.getParameter("connId");
                    %>
      
    </head>
    <body>
        <table align="center" id="selectConnId" style="display: none">
            <tr>
                <td>
     
                    <label>Select Connection</label>
                    <select name="connectionid" id="connectionid">
                        <option value="0">-select connection-</option>
                        <%
                                    for (int i = 0; i < pbconobj.getRowCount(); i++) {
                        %>
                        <option value="<%=pbconobj.getFieldValueString(i, "CONNECTION_ID")%>"><%=pbconobj.getFieldValueString(i, "CONNECTION_NAME")%></option>
                        <%}
                        %>
                    </select>
                </td>
            </tr>
        </table>

        <table align="center" style="width:80%">
            <%-- <tr>
                     <td><label class="label" >Calender Type</label></td>
                     <td>
                     <select id="ccalType" name="ccalType" style="width:130px">
                         <option value="Enterprise">Enterprise</option>
                     </select>
                    </td>
             </tr>--%>
            <tr>
                <td><label class="label">Calender Name</label></td>
                <td><input type="text" name="ccalName" id="ccalName" style="width:130px" onblur=" duplicateCalName()"> </td>
            </tr>


            <tr>
                <td><label class="label">Year Start Date</label></td>
                <td><input type="text" name="strYear" id="datepicker" style="width:130px" title="Enter start year start date"<%--onchange="getEndDate()"--%>></td>
            </tr><tr>
                <td><label class="label">Year End Date</label></td>
                <td><input type="text" name="endYear" id="endYear" style="width:130px;background:white" title="Enter end year end date"></td>
            </tr>


            <br>
        </table>
        <table align="center">
            <tr>
                <td>
                    <input type="button" name="Save" class="navtitle-hover" value="Save" onclick="SaveCalender()">
                </td>
                <%--<td>
                    <input type="button" name="Save" class="navtitle-hover" value="Cancel" onclick="CancelCalender()">
                </td>
                  <td>
                     <input type="button" name="Cancel" value="Cancel" class="navtitle-hover" onclick="cancelCraeatetimesetUp()">
                 </td>--%>
            </tr>
        </table>

        <input type="hidden" name="h" id="h" value="<%=request.getContextPath()%>">
        <input type="hidden" name="numofdaysyr" id="numofdaysyr" >
        <%--   <input type="hidden" name="connId" id="connId" value="<%=connId%>" >--%>

  <script>
            var fromStandTimeDim = false;
            $(function() {
                if("null"!='<%=connId%>'){
                    $("#connectionid").val('<%=connId%>');
                    fromStandTimeDim = true;
                }
                else{
                    document.getElementById("selectConnId").style.display = "";
                }
                $('#datepicker').datepicker({
                    changeMonth: true,
                    changeYear: true,
                    showButtonPanel: true,
                    numberOfMonths: 3,
                    stepMonths: 3
                });
                $('#endYear').datepicker({
                    changeMonth: true,
                    changeYear: true,
                    showButtonPanel: true,
                    numberOfMonths: 3,
                    stepMonths: 3
                });
            });

            function getEndDate(){
                                   
                var startYear=document.getElementById("datepicker").value;
                //alert( 'calenderSetUpAction.do?timeCalenders=getEndDate&startYear='+startYear)
                $.ajax({
                    url: 'calenderSetUpAction.do?timeCalenders=getEndDate&startYear='+startYear,
                    success: function(data) {
                        document.getElementById("endYear").value=data;
                    }
                });
                                    
            }
            function valiDateCalnder(startYear,endYear){
                var calenderName= document.getElementById("ccalName").value;
                var conid = document.getElementById("connectionid").value;

                if(conid==0)
                {
                    alert("Please Select the connection type");
                    return false;
                }

                if(calenderName==""){
                    alert('Please enter Calender Name');
                }else if(startYear==""){
                    alert('Please enter year start date');
                }else if(endYear==""){
                    alert('Please enter year end date');

                }else{
                    $.ajax({
                        url:'calenderSetUpAction.do?timeCalenders=valiDateCalnder&startYear='+startYear+'&endYear='+endYear,
                        success:function(data){
                            if(data=='true'){
                                supportSaveCalender(calenderName,conid,startYear,endYear)
                            }else{
                                alert("Please select EndYear correctly.")
                            }

                        }
                    });
                }
               
            }
            function   supportSaveCalender(calenderName,conid,startYear,endYear){
              
                 parent.parent.document.getElementById('loading').style.display='';
                var CalenderName=document.getElementById("ccalName").value;
                $.ajax({
                    url: 'calenderSetUpAction.do?timeCalenders=duplicateCalenderName&CalenderName='+CalenderName+'&conid='+conid,
                    success: function(data) {
                        // alert(data);
                        if(data=='YES'){
                            alert('Duplicate Calender Name,Please enter another name');
                        }else{
                            $.ajax({
                                url: 'calenderSetUpAction.do?timeCalenders=saveCalender&startYear='+startYear+'&endYear='+endYear+"&calenderName="+calenderName+"&conid="+conid+"&fromStandTimeDim="+fromStandTimeDim,
                                success: function(data) {
                                   // alert("data\t"+data)
                                   if(data=='true'){
                               parent.parent.document.getElementById('loading').style.display='none';
                               parent.testing();
                                   }else{
                                       alert("Error in Calender creation")
                                       parent.testing();
                                   }
                                }
                            });
                            // alert("in out")
                            
                        }
                    }
                });


                

            }

            function SaveCalender(){

                var startYear=document.getElementById("datepicker").value;
                var endYear=  document.getElementById("endYear").value;
                valiDateCalnder(startYear,endYear)
               
            }

        
            function duplicateCalName(){
                                   
                var CalenderName=document.getElementById("ccalName").value;
                var conid = document.getElementById("connectionid").value;
                $.ajax({
                    url: 'calenderSetUpAction.do?timeCalenders=duplicateCalenderName&CalenderName='+CalenderName+'&conid='+conid,
                    success: function(data){
                        // alert(data);
                        if(data=='YES'){
                            alert('Duplicate Calender Name,Please enter another name');
                            document.getElementById("ccalName").focus()
                        }
                    }
                });
            }

        </script>
    </body>
</html>
