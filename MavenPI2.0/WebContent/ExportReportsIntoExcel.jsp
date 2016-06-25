<%--
    Document   : ExportReportsIntoExcel
    Created on : 16 June, 2014, 3:47:50 PM
    Author     : Amar
--%>

<%@page import="java.util.ArrayList"%>
<%@page import="prg.db.PbReturnObject"%>
<%@page import="prg.db.PbDb"%>
<%@page import="prg.db.DataTracker"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<% String userIdStr = "";
             String[] scheduleday={"Mon","Tue","Wed","Thur","Fri","Sat","Sun"};
             String[] sday={"2","3","4","5","6","7","1"};
             String[] frequency={"Daily","Weekly","Monthly","Hourly"};
             String themeColor="blue";
            if (session.getAttribute("USERID") != null) {
                userIdStr = (String) session.getAttribute("USERID");
            }
            if(session.getAttribute("theme")==null)
                session.setAttribute("theme",themeColor);
            else
                themeColor=String.valueOf(session.getAttribute("theme")); 
             String contextPath=request.getContextPath();
             %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script type="text/javascript" src="<%=contextPath%>/javascript/lib/jquery/js/jquery-1.4.2.min.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>
         <script type="text/javascript" src="<%=contextPath%>/javascript/scripts.js"></script>
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=themeColor %>/ReportCss.css" rel="stylesheet"/>
        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/themes/<%=themeColor%>/style.css" type="text/css" media="print, projection, screen"/>
        <script src="<%=contextPath%>/javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/blue/jquery-ui-1.7.3.custom.css" rel="stylesheet" />
        <script src="<%=contextPath%>/javascript/treeview/jquery.cookie.js" type="text/javascript"></script>
        <script src="<%=contextPath%>/javascript/treeview/jquery.treeview.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/treeview/demo.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/quicksearch.js"></script>
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=themeColor%>/metadataButton.css" rel="stylesheet" />
        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/treeviewstyle/jquery.treeview.css" />
        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/treeviewstyle/screen.css" />
        <link rel="stylesheet" href="stylesheets/treeviewstyle/screen.css" />
        <script type="text/javascript" src="<%= contextPath%>//dragAndDropTableforexcel.js"></script>
<!--        <link type="text/css" href="<%=contextPath%>/datedesign.css" rel="stylesheet"/>-->

    

                        <script type="text/javascript">
//        var userIds=new Array();
        var userIds = "";
        var reportIds = "";
        var mbrIds ="";
        var sheetIds = "";
        var lineIds = "";
        var filepath = "";
        $("#excelInfoTab").html('');
        function reportBasedOnRoles(id){
            $("#excelInfoTab").find("tr").each(function(event){
                $(this).remove();
   });
            repNames=[];
            grpColArray=[];
            var id=$("#"+id).val().split('~');
            var grpID=id[0];
            var folderID=id[1];
            $("#usersDiv").dialog('open');
            //alert('mohit');
            $.post('userLayerAction.do?userParam=reportsBasedOnRoles&groupID='+grpID+'&folderID='+folderID,
            function(data){
             // alert(data);
                $('#usersDiv').html(data);
               //var jsonVar=eval('('+data+')')
               var jsonVar=eval('('+data+')');
              // $("#usersDiv").html(jsonVar.htmlStr).append("</form></td></tr><center><input type='button' class='navtitle-hover' onclick='downloadExcels();' name='Download' value ='Download'/><input type='button' class='navtitle-hover'  onclick='scheduleExcels();' name='Schedule' value ='Schedule'/></center></table>");
              $("#usersDiv").html(jsonVar.htmlStr).append("</form></td></tr><center><input type='button' class='navtitle-hover' onclick='getExcelInfo();' name='ExcelInfo' value ='ExcelInfo'/></center></table>");
               isMemberUseInOtherLevel=jsonVar.isMemberUseInOtherLevel;
                        grpColArray=jsonVar.memberValues;
                //$('#usersDiv').dialog('open');
                // $("#AssignmentDiv").html(jsonVar.htmlStr).append("hii");
                 $("#myList3").treeview({
                            animated:"slow",
                            persist: "cookie"
            });
                        $('ul#myList3 li').quicksearch({
                            position: 'before',
                            attached: 'ul#myList3',
                            loaderText: '',
                            delay: 100
                        });
                        $(".myDragTabs").draggable({

                            helper:"clone",
                            effect:["", "fade"]
                        });
                        $("#dropTabs").droppable({
                            activeClass:"blueBorder",
                            accept:'.myDragTabs',
                            drop: function(ev, ui) {
                                createColumn(ui.draggable.attr('id'),ui.draggable.html(),"sortable");
        }
                        }
                    );

                        $(".sortable").sortable();

            });
        }
        function downloadExcels(){
             $("#excelInfoDiv").dialog('close');
            var sheetNs="";
            var lineNs="";
            reportIds="";
            var fileName = document.getElementById("selId").value;
//             var tabObj=sortable.getElementsByTagName("table");
//                var flag="true";
//                for(var i=0;i<tabObj.length;i++){
////                     var content
//                    mbrIds=(tabObj[i].id).split("_");
//                    reportIds = reportIds+","+mbrIds[0];
//                 }
                 for(var repC=1;repC<grpColArray.length;repC++){
                     reportIds = reportIds+","+grpColArray[repC];
                 }
                 var iForm = document.getElementById("excelInfoId");
                 iForm.setAttribute("action","reportViewer.do?reportBy=exportReportsIntoExcelsheets&reportIds="+reportIds+"&fileName="+fileName);
                 $("#excelInfoId").submit();
             //var dataString= $('#sortable').serialize();
//             $.ajax( {
//                 type: 'POST',
//                 url: 'reportViewer.do?reportBy=exportReportsIntoExcelsheets&reportIds='+reportIds,
//                 data: dataString,
//                     success: function(data) {
//                               //console.log(data);
//                          $("#loading").hide();
//                          //alert("report downloaded succefully");
//                          ("#dFrame").attr('src','url');
//                                }
//                       });
        }
        function getExcelInfo(){
            var a = grpColArray.length;

            $("#excelInfoTab").append('<tr bgcolor=#FF66CC><td width=40%>Report Name</td><td width=15%>Sheet No.</td><td width=12%>Line No.</td><td width=12%>Col No.</td><td>Header</td><td>GT</td></tr><tr></tr>');
            for(var i =1; i<grpColArray.length;i++){
                $("#excelInfoTab").append('<tr><td width=40% bgcolor=#FFCCCC>'+repNames[i-1]+'</td><td width=15%><input type=number style="width:90%" min=1 id =sheetId name='+grpColArray[i]+'_sheetName /></td><td width=12%><input type=number style="width:90%" min=1 name='+grpColArray[i]+'_lineName id=lineId></td><td width=12%><input type=number style="width:90%" min=1 id=colId name='+grpColArray[i]+'_colName></td><td width=12%><center><input type=checkbox id=headId name='+grpColArray[i]+'_headName /></center></td><td><input type=checkbox id=gtId name='+grpColArray[i]+'_gtName></td></tr>');
            }
            $("#usersDiv").dialog('close');
            $("#excelInfoDiv").dialog('open');
        }
     function scheduleExcels(){//added by Dinanath
         $("#excelInfoDiv").dialog('close');
         $("#ScheduleReports").dialog('open');
        //added by Dinanath for xlsm format support in scheduler and download
        var fileName = document.getElementById("selId").value;
         var ext = fileName.substring(fileName.lastIndexOf('.') + 1);
         $(".myFileType").html("");
         if( fileName!=null && (  ext =="csv" || ext=="xls" || ext == "xlsx" || ext=="xlsm") )
                {
                   if(ext=="csv"){
                            $(".myFileType").append("<option value='C' enabled>CSV</option>");
                          //  alert("csv "+tempFileName)
     }
                        if(ext=="xls"){
                            $(".myFileType").append("<option value='E' enabled>Excel-S</option>");
                           // alert("xls "+tempFileName)
                        }
                       if(ext=="xlsx"){
                            $(".myFileType").append("<option value='X' enabled>Excel-X</option>");
                           //  alert("xlsx "+tempFileName)
                         }
                        if(ext=="xlsm"){
                            $(".myFileType").append("<option value='M' enabled>Excel-M</option>");
                          //  alert("csv "+tempFileName)
                        }
                        if(ext=="pdf"){
                           $(".myFileType").append(" <option value='P' disabled>PDF</option>");
                          //  alert("csv "+tempFileName)
                        }
                        if(ext=="html"){
                            $(".myFileType").append("<option value='H' enabled>HTML</option>");
                          //  alert("csv "+tempFileName)
                        }

//                    return true;
                }
     }

     function SendScheduleReports(){
         $("#ScheduleReports").dialog('close');
         $("#loading").show();
         reportIds="";
         var fileName = document.getElementById("selId").value;
         //alert("filename "+fileName)
//                      var tabObj=sortable.getElementsByTagName("table");
//                      var flag="true";
//                      for(var i=0;i<tabObj.length;i++){
//                          mbrIds=(tabObj[i].id).split("_");
//                          reportIds = reportIds+","+mbrIds[0];
//                      }
                          var dataString = $("#excelInfoId, #exportReportsScheduleForm").serialize();
                           for(var repC=1;repC<grpColArray.length;repC++){
                     reportIds = reportIds+","+grpColArray[repC];
                 }
                // Log in console so you can see the final serialized data sent to AJAX
                         console.log(dataString);
                    // Do AJAX
                        $.ajax( {
                                  type: 'POST',
                                  url: 'reportViewer.do?reportBy=exportScheduleReportsInExcels&reportIds='+reportIds+'&fileName='+fileName,
                                  data: dataString,
                                  success: function(data) {
                                        if(data==1){
                                        $("#loading").hide();
                                            alert("Scheduler with this name already exists! Please enter another name.");
                                       }
                                        else{
                                            $("#loading").hide();
                                            alert(data);
                                        }
                                    }
                                 });
                        }

   function uploadFile(){
       $("#uploadFile").dialog('open');
   }
    function checkFrequency(id){
                if($("#"+id).val()=="Weekly"){
		 $("#weekday").show();
		 $("#monthday").hide();
		}else if($("#"+id).val()=="Monthly"){
		$("#weekday").hide();
		$("#monthday").show();
		}else{
		$("#monthday").hide();
		$("#weekday").hide();
		}
            }

            //Added by Amar to update exel info

           function updateExcelInfo(){
                alert('yes');
                ("#excelInfoDiv").dialog('opne');
            }
//             function checkRefreshFrequency(id){
//                 if($("#"+id).val()=="Weekly"){
//		 $("#refreshweekday").show();
//		 $("#refreshmonthday").hide();
//		 $("#refreshhourly").hide();
//		}else if($("#"+id).val()=="Monthly"){
//		$("#refreshweekday").hide();
//		$("#refreshmonthday").show();
//		$("#refreshhourly").hide();
//		}else if($("#"+id).val()=="Hourly"){
//		$("#refreshweekday").hide();
//		$("#refreshmonthday").hide();
//		$("#refreshhourly").show();
//		}else{
//                $("#refreshweekday").hide();
//		$("#refreshmonthday").hide();
//		$("#refreshhourly").hide();
//                }
//             }
   $( "#uploadFile" ).submit(function( event ) {
       $("#uploadFile").dialog('close');
   });
//   $( "#usersDiv" ).submit(function( event ) {
//       //alert( "Handler for .submit() called." );
//              mbrIds="";
//              reportIds="";
//              var eleCount = sortable.getElementsByTagName("table").length;
//              var tabObj=sortable.getElementsByTagName("table");
//              var flag="true";
//              for(var i=0;i<tabObj.length;i++){
//                  mbrIds=(tabObj[i].id).split("_");
//                  alert(mbrIds);
//                  reportIds = reportIds+","+mbrIds[0];
//              }
//              $("#usersDiv").dialog('close');
//              $("#sortable").attr("action", "reportViewer.do?reportBy=exportScheduleReportsInExcels&reportIds="+reportIds);
//
//   });
   $("#usersDiv").dialog({
       autoOpen: false,
       height: 500,
       width: 600,
       position: 'justify',
       modal: true
   });
   $("#ScheduleReports").dialog({
       autoOpen: false,
       height: 410,
       width: 480,
       position: 'justify',
       modal: false
   });
   $('#sDatepicker').datepicker({
       changeMonth: true,
       changeYear: true
   });
   $('#eDatepicker').datepicker({
       changeMonth: true,
       changeYear: true
   });
   $("#uploadFile").dialog({
       autoOpen: false,
       height: 230,
       width: 400,
       position: 'justify',
       modal: true
   });
   $("#excelInfoDiv").dialog({
       title:'Reports Excel Info',
       autoOpen:false,
       height: 250,
       width: 450,
       position: 'justify',
       modal: true
   });
   $(".mydate").datepicker({
       changeMonth: true,
       changeYear: true
   });
  
    </script>
           <style type="text/css">
               #tablesorterUserList{
                   width:40%;
}
#ui-datepicker-div
{
    z-index: 9999999;
}
.loading_image{
        display: block;
        position: absolute;
        top: 0%;
        left: 0%;
        width: 100%;
        height: 170%;
        background-color: black;
        z-index:1001;
        -moz-opacity: 0.5;
        opacity:.50;
        filter:alpha(opacity=50);
        z-index:1001;
        overflow:hidden;
    }

</style>
    </head>
    <%
      try{
          PbDb pbdb = new PbDb();
          PbReturnObject  pbro=null;
          String getfolderList = "SELECT FOLDER_ID, GRP_ID, FOLDER_NAME FROM PRG_USER_FOLDER";
          pbro = pbdb.execSelectSQL(getfolderList);
    %>

    <body>

         <form name="favouritesform" method="post">
            <table style="width:100%">
                <tr>
                    <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
                    <td align="right"><h5>Roles</h5></td>
                    <td align="left">
                        <h5><select id="bsrSelect" name="BusinessRoles" onchange="reportBasedOnRoles(this.id);">
                    <%if(pbro.getRowCount()>0){%>
                  <option value="select"> ---Select--- </option>

                <%for(int i=0;i<pbro.getRowCount();i++){%>

                <option value="<%=pbro.getFieldValueString(i,1)%>~<%=pbro.getFieldValueString(i,0)%>">

                    <%=pbro.getFieldValueString(i,2)%></option>
          <%}
             }
           }  catch (Exception e) {
                                    e.printStackTrace();
                       }%>
            </select></h5>
                    </td>
                </tr>
           </table>
     </form>
            <IFRAME NAME="dFrame" ID="dFrame" STYLE="display:none;width:0px;height:0px" SRC="" frameborder="0"></IFRAME>
            <div id="AssignmentDiv" STYLE='display:none'>

        </div>
             <div id="usersDiv" STYLE='display:none'>

       </div>

            <div id="ScheduleReports" style="display:none" title="Schedule Reports">
             <form action="" name="exportReportsScheduleForm" id="exportReportsScheduleForm" method="post">
             <table>
                 <tr><td class="myhead">Schedule Name</td><td><input id="scheduleName" type="text" value="" style="width: auto;" maxlength="100" name="scheduleName"></td></tr>
                 <tr><td class="myhead">Email To</td><td><textarea id="usertextarea" style="width: 250px; height: 80px;" rows="" cols="" name="usertextarea"></textarea></td></tr>
                 <tr>
                   <td class="myhead">Format</td>
                   <td><select style="width: 130px;" id="fileType" class="myFileType" name="fileType" >
                   <option value="H" disabled>HTML</option>
                   <option value="E">Excel-S</option>
                   <option value="X">Excel-X</option>
                   <option value="M">Excel-M</option>
                   <option value="P" disabled>PDF</option>
                   </select>
                    </td>
                    </tr>
                 <tr><td class="myhead">StartDate</td><td><input id="sDatepicker"   type="text" value="" style="width: 120px;" maxlength="100" name="startdate" readonly=""></td></tr>
                 <tr><td class="myhead">End Date</td><td><input id="eDatepicker"   type="text" value="" style="width: 120px;" maxlength="100" name="enddate" readonly=""></td></tr>
                 <tr><td class="myhead">Time</td>
                 <td><table><tr><td>
                     hrs<select name="hrs" id="hrs" >
                       <%for (int i = 00; i < 24; i++) {%>
                       <option  value="<%=i%>"><%=i%></option>
                       <%}%>
                     </select></td>
                     <td>mins
                     <select name="mins" id="mins">
                     <%for (int i = 00; i < 60; i++) {%>
                     <option  value="<%=i%>"><%=i%></option>
                     <%}%>
                     </select></td></tr></table>
                 </td>
                 </tr>

                 <tr>
                     <td class="myhead">Period</td>
                     <td>
                         <select id="Data" name="Data" >
                            <option value="Current Day">Current Day</option>
                            <option value="Previous Day">Previous Day</option>
                            <option value="Report Date">Report Date</option>
                         </select>
                     </td>
                 </tr>

                 <tr>
                    <td class="myhead">Frequency</td>
                     <td>
                         <select id="frequency" name="frequency" onchange="checkFrequency(this.id);">
                           <option value="Daily">Daily</option>
                           <option value="Weekly">Weekly</option>
                           <option value="Monthly">Monthly</option>
                        </select>
                    </td>
                   </tr>
		 <tr id="weekday" style="display:none;">
		  <td class="myhead">Week Day</td>
                    <td>
                        <select id="particularDay" name="particularDay">
                            <% for(int i=0;i<scheduleday.length;i++){%>
                            <option value="<%=sday[i]%>"><%=scheduleday[i]%></option>
                            <%}%>
                        </select>
                    </td>
		</tr>
                <tr id="monthday" style="display:none;">
                    <td class="myhead">Month Day</td>
                    <td>
                        <select id="monthParticularDay" name="monthParticularDay">
                            <% for(int i=1;i<=31;i++){%>
			   <option value="<%=i%>"><%=i%></option>
                            <%}%>
                        </select>
                    </td>
                </tr>
                 <tr><td colspan="2">&nbsp; </td></tr>
                 <tr>
                     <td  id="saveScheduleReport" align="center"><input id="saveScheduleReport" class="navtitle-hover" type="button" onclick="SendScheduleReports();" value="Save Schedule">
                     <!-- <td id="updateScheduleReport" align="center"><input class="navtitle-hover" type="button" onclick="updateScheduleReport()" value="Update Schedule"> -->
<!--                     <td id="deleteScheduleReport" align="left"><input  class="navtitle-hover" type="button" onclick="deleteScheduleReport()" value="Delete Schedule">-->
                 </tr>
                 <tr><td colspan="2">&nbsp; </td></tr>
                 <tr><td colspan="2" align="center"><font size="1" color="red">*</font>Please separate multiple Email Id's by comma(,).</td></tr>
             </table>
             </form>
             <input type="hidden" id="schedulerId" name="schedulerId" value="">
         </div>
                        <div  id="loading" style="display:none" class='loading_image'>
                            <img alt=""  id='imgId' src='images/help-loading.gif'  border='0px' style='position:absolute; left: 450px; top: 150px;'/>
                        </div>
                        <div id="excelInfoDiv" class="ui-dialog-titlebar" style="display:none" >
                          <form name="excelInfoName" id="excelInfoId" action="" method="post">
                              <table id="excelInfoTab">

                              </table>


                          </form>
                            <center><input type='button' class='navtitle-hover' onclick='downloadExcels();' name='Download' value ='Download'/>&nbsp;&nbsp;<input type='button' class='navtitle-hover'  onclick='scheduleExcels();' name='Schedule' value ='Schedule'/></center>
                           <p>*Please start sheet, row and column number from 1</p>
                        </div>
    </body>
</html>
