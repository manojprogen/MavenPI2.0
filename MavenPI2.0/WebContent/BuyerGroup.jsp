<%--
    Document   : CustomSetting
    Created on : 26 Aug, 2012, 2:47:11 PM
    Author     : Mohit
--%>
<%@page contentType="text/html" pageEncoding="UTF-8" import="com.progen.metadata.Cube,prg.db.PbReturnObject,java.sql.Connection,java.sql.ResultSet,java.sql.PreparedStatement,prg.db.PbDb,utils.db.ProgenConnection"%>

<!DOCTYPE html>
<!--Added by mohit for drl enhancement,        *********ONLY FOR DRL*********           -->

         <% String themeColor = "blue";
            if (session.getAttribute("theme") == null) {
                session.setAttribute("theme", themeColor);
            } else {
                themeColor = String.valueOf(session.getAttribute("theme"));
            }
         String userId = String.valueOf(session.getAttribute("USERID"));
         Connection con = null;
         String query = "";
                PbDb pbdb = new PbDb();
                con = ProgenConnection.getInstance().getConnectionByConId("65");
                //ItsReturnObject list = null;
                //list = pbdb.execSelectSQL(query);
                String contextpath=request.getContextPath();
         %>

         <html>
    <head>
       <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script src="<%=contextpath%>/javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=contextpath%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>
        <link type="text/css" href="<%=contextpath%>/stylesheets/themes/blue/jquery-ui-1.7.3.custom.css" rel="stylesheet" />
        <script src="<%=contextpath%>/javascript/treeview/jquery.cookie.js" type="text/javascript"></script>
        <script src="<%=contextpath%>/javascript/treeview/jquery.treeview.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=contextpath%>/javascript/treeview/demo.js"></script>
        <script type="text/javascript" src="<%=contextpath%>/javascript/quicksearch.js"></script>
        <link type="text/css" href="<%=contextpath%>/stylesheets/themes/<%=themeColor%>/metadataButton.css" rel="stylesheet" />
        <link rel="stylesheet" href="<%=contextpath%>/stylesheets/treeviewstyle/jquery.treeview.css" />
        <link rel="stylesheet" href="<%=contextpath%>/stylesheets/treeviewstyle/screen.css" />
        <link rel="stylesheet" href="stylesheets/treeviewstyle/screen.css" />
        <link type="text/css" href="<%=contextpath%>/stylesheets/themes/<%=themeColor%>/TableCss.css" rel="stylesheet" />
<!--        <link href="http://code.google.com/apis/maps/documentation/javascript/examples/standard.css" rel="stylesheet" type="text/css" />-->
         <script type="text/javascript" src="<%=contextpath%>/jQuery/jquery/ui/ui.datepicker.js"></script>
        <script type="text/javascript" src="<%= contextpath%>//dragAndDropTable.js"></script>

     <style type="text/css">
         .migrate
            {
                font-family: Verdana, Arial, Helvetica, sans-serif;
                font-size: 10pt;
                font-weight: bold;
                color: #3A457C;
                padding-left:12px;
                background-color:#b4d9ee;
                border:0px;
            }
      </style>


    </head>
    <body>

         <table align="center" style=" width:40% ">
             <tr><td>
<!--                <form id="SaveSettings" name=SaveSettings" method="post" action="">-->
                    <table align ="center" id="secDetTab" class="tablesorter"  border="0" cellpadding="0" cellspacing="0" style=" width: 100%">
                       <tr>
                           <td colspan="1">&nbsp;</td>
                       </tr>
                       <tr>
                           <td colspan="1">&nbsp;</td>
                       </tr>
                       <tr>
                           <td colspan="1">&nbsp;</td>
                       </tr>
                       <tr><td><center><input type="radio" name="add" style="width:auto;color:black" value="Add Buyer" id="addbuyer" onclick="openAddBuyer()"/>
                                  Add Buyer  &nbsp;&nbsp;</center>
                       </td><td><center><input type="radio" name="add" style="width:auto;color:black" value="Search Buyer" id="searchbuyer" onclick="openAddBuyer()"/>
                                  Search Buyer / Edit Buyer &nbsp;&nbsp;&nbsp;&nbsp;</center>
                       </td> <td colspan="1">&nbsp;</td><td colspan="1">&nbsp;</td>
                       </tr>
<tr>
                           <td colspan="1">&nbsp;</td>
                       </tr><tr>
                           <td colspan="1">&nbsp;</td>
                       </tr>
<!-- <tr><td>Start Date</td><td><input id="sddatepicker" type="text" value="" style="width: 120px;" maxlength="100" name="" readonly=""></td>
                  </tr>
                       <tr><td>End Date</td><td><input id="eddatepicker" type="text" value="" style="width: 120px;" maxlength="100" name="" readonly=""></td>
                  </tr>-->
                       <tr></tr>


                   </table>
<!--                </form>-->
                 </td></tr>
        </table>

 <div id='loadingmetadata' class='loading_image' style="display:none;">
                <img alt=""  id='imgId' src='images/help-loading.gif'  border='0px' style='position:absolute; left: 450px; top: 150px;'/>
</div>
<div id="AddBuyer" style="display:none" title="Add Buyer" align="center">
    <form id="formAddBuyer" name=SaveSettings" method="post" action="">
    <table>

        <tr><td align="left"><label>Buyer Name:&nbsp;&nbsp;</label></td>
                          <td><input id="bName" type="text"></td>
                       </tr>
                       <tr><td align="left"><label>Buyer Group:&nbsp;&nbsp;</label></td>
                          <td><input id="bGroup" type="text"></td>
                       </tr>
                       <tr><td align="left"><label>Buyer Unit:&nbsp;&nbsp;</label></td>
                          <td><input id="bUnit" type="text"></td>
                       </tr>
                       <tr><td align="left"><label>Team:&nbsp;&nbsp;</label></td>
                          <td><input id="team" type="text"></td>
                       </tr>
                       <tr><td>Start Date:&nbsp;&nbsp;</td><td><input id="sddatepicker" type="text" value="" style="width: 120px;" maxlength="100" name="" readonly="" onclick=""></td>
                  </tr>
                       <tr><td>End Date:&nbsp;&nbsp;</td><td><input id="eddatepicker" type="text" value="" style="width: 120px;" maxlength="100" name="" readonly="" onclick=""></td>
                  </tr>
                   <tr>
                           <td colspan="2">&nbsp;</td>
                       </tr>
                        <tr>
                           <td colspan="2">&nbsp;</td>
                       </tr>


                                  <tr><td colspan="2"><center><input type="button" style="width:auto;color:black" value="Submit" id="" onclick="submitBuyer()"/>
                                  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="reset" style="width:auto;color:black" value="Reset" id="" onclick=""/></center>
                       </td></tr>
        </table></form></div>


        <div id="SearchBuyer" style="display:none" title="Add Buyer" align="center">
    <form id="formSearchBuyer" name=SaveSettings" method="post" action="">
    <table>
        <tr>
                           <td colspan="2">&nbsp;</td>
                       </tr>
        <tr><td align="left"><label>By Buyer Group: &nbsp;&nbsp;&nbsp;&nbsp;</label></td>
                           <td align="left"><select name="bbg" id="bbg" style="width:100px;" onchange="getbbg()">
                             </select></td>
                       </tr>
                       <tr>
                           <td colspan="2">&nbsp;</td>
                       </tr><tr>
                           <td colspan="2">&nbsp;</td>
                       </tr>
                       <tr><td align="left"><label>By Buyer Name: &nbsp;&nbsp;&nbsp;&nbsp;</label></td>
                           <td align="left"><select name="bbn" id="bbn" style="width:120px;" onchange="">
                             </select></td>
                       </tr>
<tr>
                           <td colspan="2">&nbsp;</td>
                       </tr><tr>
                           <td colspan="2">&nbsp;</td>
                       </tr>
                           <tr><td colspan="2"><center><input type="button" style="width:auto;color:black" value="Submit" id="" onclick="getBuyer()"/></center></td></tr>

        </table></form></div>

<div id="SearchResult" align="center" style="display:none">
<!--                <div style="overflow: hidden; height: 300px ">-->
<!--                    <div class="hover" onmouseover="hover" style="  overflow: auto; height: 380px">-->
                    <table  style="width:86%;border-collapse: collapse;  " id="SearchResultTable">
                <thead style="  ; background-color: #DDD ; text-align: center; ">

                    <tr>
                        <th  style="background-color: #B4D9EE; width: 1.0em;  padding: 0.6em; border: 1px solid #CCC; " id="S_no">S.No.</th>
                        <th  style="background-color: #B4D9EE; width: 5.0em; padding: 0.6em; border: 1px solid #CCC; " id="Buyer_Id">Buyer Id</th>
                        <th  style="background-color: #B4D9EE; width: 4.0em;  padding: 0.6em; border: 1px solid #CCC; " id="Buyer_Unit">Buyer Unit</th>
                        <th  style="background-color: #B4D9EE; width: 4.0em;  padding: 0.6em; border: 1px solid #CCC; " id="Buyer_Group">Buyer Group</th>
                        <th  style="background-color: #B4D9EE; width: 12.0em; padding: 0.6em; border: 1px solid #CCC; " id="Name_of_Buyer">Name of Buyer</th>
                        <th  style="background-color: #B4D9EE; width: 6.0em; padding: 0.6em; border: 1px solid #CCC; " id="Team">Team</th>
                        <th  style="background-color: #B4D9EE; width: 8.0em; padding: 0.6em; border: 1px solid #CCC; " id="Start_Date">Start Date</th>
                        <th  style="background-color: #B4D9EE; width: 8.0em;  padding: 0.6em; border: 1px solid #CCC; " id="End_Date">End Date</th>
                        <th  style="background-color: #B4D9EE; width: 8.0em;  padding: 0.6em; border: 1px solid #CCC; " id="Update">Update</th>
                        <th  style="background-color: #B4D9EE; width: 8.0em;  padding: 0.6em; border: 1px solid #CCC ;" id="Delet">Delet</th>
                        <th  style="background-color: #B4D9EE; width: 8.0em;  padding: 0.6em; border: 1px solid #CCC ;" id="Delet">Delet</th>

<!--                        <th  style="background-color: #B4D9EE; width: 10.0em; padding: 0.6em; border: 1px solid #CCC; " id="Exp_Fix_Date">Exp Fix Date </th>-->
                        </tr>

                </thead>
                <tbody  id="SearchResultBody" style=" border-collapse: collapse ;   text-align: center " >

</tbody>
                    </table> </div>
 <script type="text/javascript">
            $(document).ready(function(){

 $.get("<%= request.getContextPath()%>/userLayerAction.do?userParam=getBuyer&temp=bg&flag=false&querytype=select",function(data){
//                           alert('Record Deleted Successfully............')
//                           alert("data"+data)
                        $("#bbg").html(data)
                        });

                $.get("<%= request.getContextPath()%>/userLayerAction.do?userParam=getBuyer&temp=bn&flag=false&querytype=select",function(data){
//                           alert('Record Deleted Successfully............')
//                           alert("data"+data)
                        $("#bbn").html(data)
                        });





               if ($.browser.msie == true){
//                    $("#AddBuyer").dialog({
//                    autoOpen: false,
//                    height: 140,
//                    width: 210,
//                    position: 'top',
////                    top: '23px',
////                    left: '420.5px',
//                    modal: false
//
//                });
                $('#sddatepicker').datepicker({
                    changeMonth: true,
                    changeYear: true,
                    yearRange: "-20:+20",
                    dateFormat: "dd/mm/yy"
                    });




                    $('#eddatepicker').datepicker({
                    changeMonth: true,
                    changeYear: true,
                    yearRange: "-20:+20",
                    dateFormat: "dd/mm/yy"
                    });


               }
               else {
//                    $("#AddBuyer").dialog({
//                    autoOpen: false,
//                    height: 500,
//                    width: 500,
//                    position: 'Center',
////                    top: '23px',
////                    left: '420.5px',
//                    modal: false
//
//                });

                $('#sddatepicker').datepicker({
                    changeMonth: true,
                    changeYear: true,
                    yearRange: "-20:+20",
                    dateFormat: "dd/mm/yy"
                    });$('#eddatepicker').datepicker({
                    changeMonth: true,
                    changeYear: true,
                    yearRange: "-20:+20",
                    dateFormat: "dd/mm/yy"
                    });

               }
           });


           function openAddBuyer(){
               if(document.getElementById("addbuyer") != null && document.getElementById("addbuyer").checked){
                   $("#SearchBuyer").hide();
                   $("#formAddBuyer").trigger('reset')
                $("#AddBuyer").show();
//           $("#overWriteReport").height(500);
        } else if(document.getElementById("searchbuyer") != null && document.getElementById("searchbuyer").checked){
                $("#AddBuyer").hide();
//
                $("#SearchBuyer").show();
//                $("#SearchResult").hide();

        }

           }


            function getbbg(){
            var bbg=$("#bbg").val()
            $.get("<%= request.getContextPath()%>/userLayerAction.do?userParam=getBuyer&temp="+bbg+"&flag=bbg&querytype=select",function(data){
//                           alert('Record Deleted Successfully............')
//                           alert("data"+data)
                        $("#bbn").html(data)
                        });

            }
//            function getbbn(){
//                var bbn=$("#bbn").val()
//            $.get("<%= request.getContextPath()%>/userLayerAction.do?userParam=getBuyer&temp="+bbn+"&flag=bbn",function(data){
////                           alert('Record Deleted Successfully............')
////                           alert("data"+data)
//                        $("#bbg").html(data)
//                        });
//
//            }
function updateBuyer(id){
//    alert("welcome"+id)
    var bid= id;
    var bName= document.getElementById("bName"+id).value;
//    alert(bName)
           var bGroup = document.getElementById("bGroup"+id).value;
             var bUnit = document.getElementById("bUnit"+id).value;
              var team = document.getElementById("team"+id).value;
               var sddatepicker = document.getElementById("sddatepicker"+id).value;
               var eddatepicker = document.getElementById("eddatepicker"+id).value;

    $.ajax({
                    async:false,
                    url:'userLayerAction.do?userParam=getBuyer&bName='+bName+'&bGroup='+bGroup+'&bUnit='+bUnit+'&team='+team+'&sddatepicker='+sddatepicker+'&eddatepicker='+eddatepicker+'&bid='+bid+'&querytype=update',
                    success:function(data) {
//                        alert(data)
                        if(data==1 || data=="1"){
                        alert('Record Updated Successfully');}
                    else
                        {
                            alert('Oops.....Record Not Updated')
                        }
                }
                });
            }
   //added by sruthi
function DeleteBuyer(id)
{
 var did=id;
 alert(id);
$.ajax({
         async:false,
       url:'userLayerAction.do?userParam=getBuyer&dname='+did+'&querytype=Delete',
           success:function(data) {
                        alert(data)
                        if(data==1||data=="1")
                            {
                               $("#"+id).remove();
                            alert("Recode Deleted Successfully");
                           }
                       else
                           {
                               alert("Recode Not Deleted")
                           }
                }

});
} //ended by sruthi
function submitBuyer(){

    var bName= document.getElementById("bName").value;
//    alert(bName)
           var bGroup = document.getElementById("bGroup").value;
             var bUnit = document.getElementById("bUnit").value;
              var team = document.getElementById("team").value;
               var sddatepicker = document.getElementById("sddatepicker").value;
               var eddatepicker = document.getElementById("eddatepicker").value;
               // alert($("#repTitle").val());

                         if(bName=='')
                {
                    alert('Please Enter Buyer Name');
                    //window.location.href='#CustomSettings.jsp';
//                  return false;

                }else if(bGroup==''){
                    alert('Please Enter Buyer Group');
                     //window.location.href='#CustomSettings.jsp';
//                    return false;

                }else if(bUnit==''){
                   alert('Please Enter Buyer Unit');
                    // window.location.href='#CustomSettings.jsp';
//                    return false;

                }else if(team==''){
                    alert('Please Enter Team');
                     //window.location.href='#CustomSettings.jsp';
//                  return false;

                }else if(sddatepicker==''){
                    alert('Please Enter Start Date ');
                     //window.location.href='#CustomSettings.jsp';
//                    return false;

                }else if(eddatepicker==''){
                   alert('Please Enter End Date ');
                     //window.location.href='#CustomSettings.jsp';
//                    return false;

                }
                else{
                    $.ajax({
                    async:false,
                    url:'userLayerAction.do?userParam=getBuyer&bName='+bName+'&bGroup='+bGroup+'&bUnit='+bUnit+'&team='+team+'&sddatepicker='+sddatepicker+'&eddatepicker='+eddatepicker+'&querytype=insert',
                    success:function(data) {
//                        alert(data)
                        var newid=(data.substring(8)).substring(0,(data.substring(8)).indexOf("'"))
//                        alert(newid)
                        if(data.contains("Update")){
                        alert('Record Inserted Successfully');
                    $("#SearchResultBody").append(data)

                    $('#sddatepicker'+newid).datepicker({
                    changeMonth: true,
                    changeYear: true,
                    yearRange: "-20:+20",
                    dateFormat: "dd/mm/yy"
                    });
                    $('#eddatepicker'+newid).datepicker({
                    changeMonth: true,
                    changeYear: true,
                    yearRange: "-20:+20",
                    dateFormat: "dd/mm/yy"
                    });





                    $("#SearchResult").show();

                      }
                    else
                        {
                            alert('Record Not Saved')
                        }




                }
                });
 $("#formAddBuyer").trigger('reset')
            }}

function getBuyer(){


var bbg=$("#bbg").val()
var bbn=$("#bbn").val()

//alert(bbg+"..."+bbn)
                 if(bbg=='--SELECT--' && bbn=='--SELECT--' )
                {
                    alert('Please Select Atleast One Option');
                    $("#SearchResult").hide();
                }else{
                  if(bbg=='--SELECT--'){
                    $.ajax({
                    async:false,
                    url:'userLayerAction.do?userParam=getBuyer&temp='+bbn+'&flag=getbuyerbbn&querytype=select',
                    success:function(data) {
//               alert(data)
               $("#SearchResultBody").html(data)

                }
                });
                }else if(bbn=='--SELECT--'){

                  $.ajax({
                    async:false,
                    url:'userLayerAction.do?userParam=getBuyer&temp='+bbg+'&flag=getbuyerbbg&querytype=select',
                    success:function(data) {
//alert(data)
$("#SearchResultBody").html(data)

                }
                });

                }else{
//                 alert(data)
                     $.ajax({
                    async:false,
                    url:'userLayerAction.do?userParam=getBuyer&temp='+bbg+'MOHIT'+bbn+'&flag=getbuyerboth&querytype=select',
                    success:function(data) {
//                 alert("hello"+data)
$("#SearchResultBody").html(data)

                }
                });

                }
              $("#SearchResultTable > tbody > tr").each(function(){
             var id=this.id
             $('#sddatepicker'+id).datepicker({
                    changeMonth: true,
                    changeYear: true,
                    yearRange: "-20:+20",
                    dateFormat: "dd/mm/yy"
                    });
                    $('#eddatepicker'+id).datepicker({
                    changeMonth: true,
                    changeYear: true,
                    yearRange: "-20:+20",
                    dateFormat: "dd/mm/yy"
                    });
//                        alert(this.id);
                 });


                $("#SearchResult").show();
                }
                $("#formAddBuyer").trigger('reset')

                }




//               function pickmycalender(id){
//                   $('#'+id).datepicker({
//                    changeMonth: true,
//                    changeYear: true,
//                    dateFormat: "dd/mm/yy"
//                    });
//
//               }






       </script>
    </body>

</html>
<!--Added by Mohit jain-->