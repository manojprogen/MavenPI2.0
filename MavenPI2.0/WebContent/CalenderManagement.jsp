<%@page contentType="text/html" pageEncoding="UTF-8" import="com.progen.i18n.TranslaterHelper,java.util.Locale"%>
<%--
    Document   : Calender Management
    Created on : July 31, 2013, 11:30:01 AM
    Author     : Nazneen Khan
--%>

<!DOCTYPE html>

         <% String themeColor = "blue";
            if (session.getAttribute("theme") == null) {
                session.setAttribute("theme", themeColor);
            } else {
                themeColor = String.valueOf(session.getAttribute("theme"));
            }

          //added by Dinanath dec 2015
            Locale cle = null;
            cle = (Locale) session.getAttribute("UserLocaleFormat");
      String contexTPath=request.getContextPath();      
         %>
         <html>
    <head>
       <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script src="<%=contexTPath%>/javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=contexTPath%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>
        <link type="text/css" href="<%=contexTPath%>/stylesheets/themes/<%=themeColor%>/jquery-ui-1.7.3.custom.css" rel="stylesheet" />
        <link type="text/css" href="<%=contexTPath%>/stylesheets/themes/<%=themeColor%>/TableCss.css" rel="stylesheet" />
        <link href="http://code.google.com/apis/maps/documentation/javascript/examples/standard.css" rel="stylesheet" type="text/css" />
         <script type="text/javascript" src="<%=contexTPath%>/jQuery/jquery/ui/ui.datepicker.js"></script>

     <style type="text/css">
         .migrate
            {
                font-family: Verdana, Arial, Helvetica, sans-serif;
                font-size: 10pt;
                font-weight: bold;
                color: #3A457C;
                padding-left:12px;
                background-color:#8BC34A;
                border:0px;
            }
      </style>

        
    </head>
    <body>
        <script type="text/javascript">
           
           $(document).ready(function(){
               if ($.browser.msie == true){
//                    $("#addCalenderDiv").dialog({
//                        autoOpen: false,
//                        height: 230,
//                        width: 337,
//                        position: 'justify',
//                        modal: true
//                    });
//                    $('#datepicker').datepicker({
//                    changeMonth: true,
//                    changeYear: true
//                    });
//                    $('#endYear').datepicker({
//                        changeMonth: true,
//                        changeYear: true
//                    });
               }
               else {
//                    $("#addCalenderDiv").dialog({
//                        autoOpen: false,
//                        height: 230,
//                        width: 337,
//                        position: 'justify',
//                        modal: true
//                    });
//                    $('#datepicker').datepicker({
//                    changeMonth: true,
//                    changeYear: true
//                    });
//                    $('#endYear').datepicker({
//                        changeMonth: true,
//                        changeYear: true
//                    });
               }
           });
           $(function() {               
               $('#datepicker').datepicker({
                beforeShow:function(input) {
                    $(input).css({
                        "position": "relative",
                        "z-index": 1
                    });
                }
                });
                $('#endYear').datepicker({
                beforeShow:function(input) {
                    $(input).css({
                        "position": "relative",
                        "z-index": 1
                    });
                }
                });
            });
           function addCalender(){   
            $("#addCalenderToCompDiv").hide();
            $("#makeDefaultCalDiv").hide();
            $("#addCalenderDiv").show();
            document.getElementById("ccalName").value = "";
            document.getElementById("denomTable").value = "";
            document.getElementById("connectionid").value = "--SELECT--";
            $.get("<%= request.getContextPath()%>/userLayerAction.do?userParam=getAllConnectionForModifyMeasure",function(data){
            $("#connectionid").html(data)
            });
        }

            function SaveCalender(){
                var calenderName= document.getElementById("ccalName").value;
                var conid = document.getElementById("connectionid").value;
                var denomTable = document.getElementById("denomTable").value;
                
                if(conid=='--SELECT--')
                {
                    alert("Please Select the connection type");
                    return false;
                }

                if(calenderName==""){
                    alert('Please enter Calender Name');
                }else if(denomTable==""){
                    alert('Please enter Denom Table Name');                
                }else{
                    $.ajax({
                        url:'userLayerAction.do?userParam=saveMgmtCalender&calenderName='+calenderName+'&conid='+conid+'&denomTable='+denomTable,
                        success:function(data){
                            if(data==1 || data=='1'){
                                alert('Calender Saved Successfully')
                                $("#addCalenderDiv").hide();
                            }else{
                                alert("Error! Calender not Saved")
                                $("#addCalenderDiv").hide();
                            }
                        }
                    });
                }               
            }          
            function CancelCalender(CalenderDiv){
                $("#"+CalenderDiv).hide();
            }
            
            function addCalenderToComp(){
                $("#addCalenderDiv").hide();
                $("#makeDefaultCalDiv").hide();
                $("#addCalenderToCompDiv").show();  
                document.getElementById("calenderId").value = "--SELECT--";
                document.getElementById("companyId").value = "--SELECT--";
                document.getElementById("connection").value = "--SELECT--";
                $.get("<%= request.getContextPath()%>/userLayerAction.do?userParam=getAllConnectionForModifyMeasure",function(data){
                $("#connection").html(data)
                }); 
            }
            function getCompanyDetails(){
                var conid = document.getElementById("connection").value;
                $.get("<%= request.getContextPath()%>/userLayerAction.do?userParam=getCompanyDetailsForConn",function(data){
                $("#companyId").html(data)
                }); 
            }
            function getCompCalDetails(){
                var conid = document.getElementById("connection").value;
                var companyId = document.getElementById("companyId").value;                
                $.get("<%= request.getContextPath()%>/userLayerAction.do?userParam=getCompanyCalDetails&conid="+conid+"&companyId="+companyId,function(data){
                $("#calenderId").html(data)
                }); 
            }
            function SaveCalenderToComp(){
                 var connection= document.getElementById("connection").value;
                var companyId = document.getElementById("companyId").value;
                var calenderId = document.getElementById("calenderId").value;
                
                if(connection=='--SELECT--')
                {
                    alert("Please Select the connection type");
                    return false;
                }

                if(companyId=="--SELECT--"){
                    alert('Please Select Company');
                }else if(calenderId==""){
                    alert('Please Select Calender');                
                }else{
                    $.ajax({
                        url:'userLayerAction.do?userParam=SaveCalenderToComp&companyId='+companyId+'&calenderId='+calenderId,
                        success:function(data){
                            if(data==1 || data=='1'){
                                alert('Calender Saved To Company Successfully')
                                $("#addCalenderToCompDiv").hide();
                            }else{
                                alert("Error! Calender not Saved To Company")
                                $("#addCalenderToCompDiv").hide();
                            }
                        }
                    });
                }      
            }
            function makeDefaultCal(){
                $("#addCalenderDiv").hide();                
                $("#addCalenderToCompDiv").hide();
                $("#makeDefaultCalDiv").show();
                 $("#calDetailsTab").html("");
                document.getElementById("companyIds").value = "--SELECT--";
                document.getElementById("conn").value = "--SELECT--";
                $.get("<%= request.getContextPath()%>/userLayerAction.do?userParam=getAllConnectionForModifyMeasure",function(data){
                $("#conn").html(data)
                }); 
            }
            function getCompanyDetail(){
                var conid = document.getElementById("conn").value;
                $.get("<%= request.getContextPath()%>/userLayerAction.do?userParam=getCompanyDetailsForConn",function(data){
                $("#companyIds").html(data)
                }); 
            }
            function getCompanyCalanders(){
                var htmlVar = "";
                var conn = document.getElementById("conn").value;
                var companyId = document.getElementById("companyIds").value;                
                $.get("<%= request.getContextPath()%>/userLayerAction.do?userParam=getCompanyCalanders&conid="+conn+"&companyId="+companyId,function(data){
                    var jsonVar=eval('('+data+')')
                    var length=data.length
                    var calenderId=jsonVar.calenderId
                    var calenderName=jsonVar.calenderName
                    var defaulVal=jsonVar.defaulVal
                    htmlVar+="<td align=\"top\"><label class=\"label\">Mark Default :</label></td>\n\
                              <td><table id=\"detTabl\" border=\"0\" cellpadding=\"0\" cellspacing=\"2\" style=\"width: 100%\">";
                    for(var i=0;i<calenderId.length;i++){  
                         if(defaulVal[i]=='y' || defaulVal[i]=='Y'){
                             htmlVar+="<tr><td><input type=\"radio\" name=\"calenderChk\" value="+calenderId[i]+" id="+calenderId[i]+" checked >"+calenderName[i]+"</td></tr>";
//                             htmlVar+="<tr><td><option selected value="+calenderId[i]+">"+calenderName[i]+"</option></td></tr>"
                         }
                         else {
                             htmlVar+="<tr><td><input type=\"radio\" name=\"calenderChk\" value="+calenderId[i]+" id="+calenderId[i]+">"+calenderName[i]+"</td></tr>";
                         }
                     }
                     htmlVar+="</td></table></td>";
                     $("#calDetailsTab").html(htmlVar)
                }); 
            }
            function SaveMarkDetails(){   
                var conn = document.getElementById("conn").value;
                 var companyId = document.getElementById("companyIds").value;
                 var calenderId = $('input:radio[name=calenderChk]:checked').val();
                if(conn=='--SELECT--')
                {
                    alert("Please Select the connection type");
                    return false;
                }

                if(companyId=="--SELECT--"){
                    alert('Please Select Company');
                }else if(calenderId==""){
                    alert('Please Select Calander to mark default');                
                }else{
                    $.ajax({
                        url:'userLayerAction.do?userParam=saveMarkCalender&companyId='+companyId+'&calenderId='+calenderId,
                        success:function(data){
                            if(data==1 || data=='1'){
                                alert('Calender Marked Default Successfully')
                                $("#makeDefaultCalDiv").hide();
                            }else{
                                alert("Error! Calender not be Marked as Default")
                                $("#makeDefaultCalDiv").hide();
                            }
                        }
                    });
                }      
            }

       </script>
       <form id="configEtlForm" name="calMagmtForm" method="post" action="">
            <table align="center">
                <tr valign="top">
                    <td><input type="button" class="migrate" style="width:auto;color:black" value="<%=TranslaterHelper.getTranslatedInLocale("Add_Calender", cle)%>" onclick="addCalender()"></td>
                    <td>&nbsp;&nbsp;</td>
                    <td><input type="button" class="migrate" style="width:auto;color:black" value="<%=TranslaterHelper.getTranslatedInLocale("Add_Calender_To_Company", cle)%>" onclick="addCalenderToComp()"></td>
                    <td>&nbsp;&nbsp;</td>
                    <td><input type="button" class="migrate" style="width:auto;color:black" value="<%=TranslaterHelper.getTranslatedInLocale("Mark_Default_Calender", cle)%>" onclick="makeDefaultCal()"></td>
                </tr>
            </table><br/>
       </form>
        <!--<div id="addCalenderDiv" title="Add Calender" style=" width:100%;display: none">-->
             <div id="addCalenderDiv" title="Add Calender" style="display:none" style="height: 398px; width: 676px;">
                <table align ="center" id="addCalenderTab" border="0" cellpadding="0" cellspacing="0">
                    <br>
                    <tr id="addCalenderTable">
                    <td>
                    <table id="addCalenderTab" border="0" cellpadding="0" cellspacing="2" style="width: 100%">
                    <tr>
                        <td>&nbsp;&nbsp;</td>                        
                        <td id ="secDetTabBody">
                                <table id="measureDetTab" border="0" cellpadding="0" cellspacing="2" style="width: 100%">
                                <tr><td align="left"><label><%=TranslaterHelper.getTranslatedInLocale("Select_Connection", cle)%> :</label></td>
                                    <td align="right"><select name="connectionid" id="connectionid" style="width:130px"></select></td>
                                 </tr>
                                <tr><td align="left"><label class="label"><%=TranslaterHelper.getTranslatedInLocale("Calender_Name", cle)%> :</label></td>
                                    <td align="right"><input type="text" name="ccalName" id="ccalName" style="width:130px" ></td>
                                 </tr>
                                 <tr><td align="left"><label class="label"><%=TranslaterHelper.getTranslatedInLocale("Calender_Type", cle)%> :</label></td>
                                     <td align="right"><input type="text" name="ccalType" id="ccalType" value="Enterprise" style="width:130px" ></td>
                                 </tr>
                                <tr><td align="left"><label><%=TranslaterHelper.getTranslatedInLocale("Denom_Table", cle)%> : &nbsp;&nbsp;&nbsp;&nbsp;</label></td>
                                     <td align="right"><input type="text" name="denomTable" id="denomTable" style="width:130px"></td>
                                 </tr>                                                             
                                 </table>
                        </td>
                    </tr>
                    <tr>
                        <td>&nbsp;&nbsp;</td> 
                         <td>&nbsp;&nbsp;</td> 
                    </tr>
                    <tr>
                    
                         <td>&nbsp;&nbsp;</td>  
                         <td>
                            <center>
                            <input class="migrate" type="button" name="Save" class="navtitle-hover" value="<%=TranslaterHelper.getTranslatedInLocale("Save_Calender", cle)%>" onclick="SaveCalender()">
                            &nbsp;&nbsp;&nbsp;
                            <input class="migrate" type="button" name="Save" class="navtitle-hover" value="<%=TranslaterHelper.getTranslatedInLocale("cancel", cle)%>" onclick="CancelCalender('addCalenderDiv')"></center>
                         </td>
                    </tr>
                 </table>
               </td>
              </tr>
             </table>
        </div>
        <div id="addCalenderToCompDiv" title="Add Calender To Company" style="display:none" style="height: 398px; width: 676px;">
                <table align ="center" id="addCalenderToCompTab" border="0" cellpadding="0" cellspacing="0">
                    <br>                    
                    <tr>
                        <td>&nbsp;&nbsp;</td>                        
                        <td id ="secDetTabBody">
                                <table id="measureDetTab" border="0" cellpadding="0" cellspacing="2" style="width: 100%">
                                <tr><td align="left"><label><%=TranslaterHelper.getTranslatedInLocale("Select_Connection", cle)%> :</label></td>
                                    <td align="right"><select name="connection" id="connection" style="width:130px" onchange="getCompanyDetails()"></select></td>
                                 </tr>
                                <tr><td align="left"><label class="label"><%=TranslaterHelper.getTranslatedInLocale("Select_Company", cle)%> :</label></td>
                                    <td align="right"><select name="companyId" id="companyId" style="width:130px" onchange="getCompCalDetails()"></select></td>
                                 </tr>
                                 <tr><td align="left"><label class="label"><%=TranslaterHelper.getTranslatedInLocale("Select_Calender", cle)%> :</label></td>
                                     <td align="right"><select name="calenderId" id="calenderId" style="width:130px"></select></td>
                                 </tr>                                                                                          
                                 </table>
                        </td>
                    </tr>
                    <tr>
                        <td>&nbsp;&nbsp;</td> 
                         <td>&nbsp;&nbsp;</td> 
                    </tr>
                    <tr>
                    
                         <td>&nbsp;&nbsp;</td>  
                         <td>
                            <center>
                            <input class="migrate" type="button" name="Save" class="navtitle-hover" value="<%=TranslaterHelper.getTranslatedInLocale("Save_Details", cle)%>" onclick="SaveCalenderToComp()">
                            &nbsp;&nbsp;&nbsp;
                            <input class="migrate" type="button" name="Save" class="navtitle-hover" value="<%=TranslaterHelper.getTranslatedInLocale("cancel", cle)%>" onclick="CancelCalender('addCalenderToCompDiv')"></center>
                         </td>
                    </tr>
                 </table>               
        </div>
         <div id="makeDefaultCalDiv" title="Make Default Calender" style="display:none" style="height: 398px; width: 676px;">
                <table align ="center" id="makeDefaultCalTab" border="0" cellpadding="0" cellspacing="0">
                    <br>                    
                    <tr>
                        <td>&nbsp;&nbsp;</td>                        
                        <td id ="secDetTabBody">
                                <table id="measureDetTab" border="0" cellpadding="0" cellspacing="2" style="width: 100%">
                                <tr><td align="left"><label><%=TranslaterHelper.getTranslatedInLocale("Select_Connection", cle)%> :</label></td>
                                    <td align="right"><select name="conn" id="conn" style="width:130px" onchange="getCompanyDetail()"></select></td>
                                 </tr>
                                <tr><td align="left"><label class="label"><%=TranslaterHelper.getTranslatedInLocale("Select_Company", cle)%> :</label></td>
                                    <td align="right"><select name="companyIds" id="companyIds" style="width:130px" onchange="getCompanyCalanders()"></select></td>
                                 </tr>                                
                                 <tr id="calDetailsTab">
                                 </tr>                                                                                          
                                 </table>
                        </td>
                    </tr>
                    <tr>
                        <td>&nbsp;&nbsp;</td> 
                         <td>&nbsp;&nbsp;</td> 
                    </tr>
                    <tr>
                    
                         <td>&nbsp;&nbsp;</td>  
                         <td>
                            <center>
                            <input class="migrate" type="button" name="Save" class="navtitle-hover" value="<%=TranslaterHelper.getTranslatedInLocale("Save_Details", cle)%>" onclick="SaveMarkDetails()">
                            &nbsp;&nbsp;&nbsp;
                            <input class="migrate" type="button" name="Save" class="navtitle-hover" value="<%=TranslaterHelper.getTranslatedInLocale("cancel", cle)%>" onclick="CancelCalender('makeDefaultCalDiv')"></center>
                         </td>
                    </tr>
                 </table>               
        </div>
         <div id='loadingmetadata' class='loading_image' style="display:none;">
                <img alt=""  id='imgId' src='images/help-loading.gif'  border='0px' style='position:absolute; left: 450px; top: 150px;'/>
</div>
    </body>

</html>