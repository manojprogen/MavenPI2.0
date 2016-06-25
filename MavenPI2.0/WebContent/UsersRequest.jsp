<%--
    Document   : CustomSetting
    Created on : 26 Aug, 2012, 2:47:11 PM
    Author     : Mohit
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" import="com.progen.metadata.Cube,java.util.Date,java.text.DateFormat,java.util.Locale,com.progen.i18n.TranslaterHelper,java.text.SimpleDateFormat"%>
<%@page import="utils.db.ProgenConnection,prg.db.PbDb,java.sql.PreparedStatement,java.sql.ResultSet,java.sql.Connection,prg.db.PbReturnObject" %>
<!DOCTYPE html>
<!--Added by mohit for user Registration,        ******************           -->

         <% String themeColor = "blue";
            if (session.getAttribute("theme") == null) {
                session.setAttribute("theme", themeColor);
            } else {
                themeColor = String.valueOf(session.getAttribute("theme"));
            }
         String userId = String.valueOf(session.getAttribute("USERID"));
        Date date = new Date();
  DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yy");
String todaysDate = dateFormat.format(date);

//added by Dinanath dec 2015
            Locale cle = null;
            cle = (Locale) session.getAttribute("UserLocaleFormat");
            String contextPath1=request.getContextPath();
         %>

         <html>
    <head>
       <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script src="<%=contextPath1%>/javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=contextPath1%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>
        <link type="text/css" href="<%=contextPath1%>/stylesheets/themes/Green/jquery-ui-1.7.3.custom.css" rel="stylesheet" />
        <script src="<%=contextPath1%>/javascript/treeview/jquery.cookie.js" type="text/javascript"></script>
        <script src="<%=contextPath1%>/javascript/treeview/jquery.treeview.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=contextPath1%>/javascript/treeview/demo.js"></script>
        <script type="text/javascript" src="<%=contextPath1%>/javascript/quicksearch.js"></script>
        <link type="text/css" href="<%=contextPath1%>/stylesheets/themes/Green/metadataButton.css" rel="stylesheet" />
        <link rel="stylesheet" href="<%=contextPath1%>/stylesheets/treeviewstyle/jquery.treeview.css" />
        <link rel="stylesheet" href="<%=contextPath1%>/stylesheets/treeviewstyle/screen.css" />
        <link rel="stylesheet" href="stylesheets/treeviewstyle/screen.css" />
        <link type="text/css" href="<%=contextPath1%>/stylesheets/themes/Green/TableCss.css" rel="stylesheet" />
<!--        <link href="http://code.google.com/apis/maps/documentation/javascript/examples/standard.css" rel="stylesheet" type="text/css" />-->
         <script type="text/javascript" src="<%=contextPath1%>/jQuery/jquery/ui/ui.datepicker.js"></script>
        <script type="text/javascript" src="<%= contextPath1%>//dragAndDropTable.js"></script>
           <script type="text/javascript" src="<%=contextPath1%>/javascript/pbReportViewerJS.js"></script>

    

        
    </head>
    <body>

<div id="SearchResult" align="center" style="display:none">
    <br>
    <br>
     <form name="UserInfoForm"  method="post" action="">
<!--                <div style="overflow: hidden; height: 300px ">-->
<!--                    <div class="hover" onmouseover="hover" style="  overflow: auto; height: 380px">-->
                    <table  style="width:95%;border-collapse: collapse;  " id="SearchResultTable">
                <thead style=" background-color: #DDD ; text-align: center; ">
<!--                    <tr><td>To</td><td><input id="tdatepicker" type="text" value="" style="width: 120px;" maxlength="100" name="fdate" readonly=""></td>
                  </tr>-->
                    <tr>
                        <td colspan="10" style="font-size:larger;  width: 100%;  padding: 0.6em; border: 1px solid #CCC; "><%=TranslaterHelper.getTranslatedInLocale("User_Request_Information", cle)%> </td>
                    <td align="left" style="font-size:larger;  width: 1%;  padding: 0.6em; border: 1px solid #CCC; "><label><%=TranslaterHelper.getTranslatedInLocale("Status", cle)%>:&nbsp;&nbsp;&nbsp;&nbsp;</label></td>
                           <td align="right" style="font-size:larger;  width: 1%;  padding: 0.6em; border: 1px solid #CCC;" ><select name="bbn" id="Status" style="width:80px;" onchange="GetUserTable(this.id)">
<option value="All">All</option>
<option value="Pending">Pending</option>
<option value="Activated">Activated</option>
<option value="Deactivated">Deactivated</option>
<option value="Rejected">Rejected</option>


                             </select>
                             </td></tr>

                    <tr>
                        <td style=" width: 1%;  padding: 0.6em; border: 1px solid #CCC; ">&nbsp;</td>
<!--                        <td  style=" width: 1%;  padding: 0.6em; border: 1px solid #CCC; " id="S_no">S.No.</td>-->
                        <td  style=" width: 4%;  padding: 0.6em; border: 1px solid #CCC; " id="Fn"><%=TranslaterHelper.getTranslatedInLocale("Full_Name", cle)%></td>
                        <td  style=" width: 1%;  padding: 0.6em; border: 1px solid #CCC; " id="Email"><%=TranslaterHelper.getTranslatedInLocale("Email", cle)%></td>
                        <td  style=" width: 1%;  padding: 0.6em; border: 1px solid #CCC; " id="Mob"><%=TranslaterHelper.getTranslatedInLocale("Mobile", cle)%></td>
                        <td  style=" width: 1%;  padding: 0.6em; border: 1px solid #CCC; " id="Nationality"><%=TranslaterHelper.getTranslatedInLocale("Nationality", cle)%></td>
                        <td  style=" width: 1%;  padding: 0.6em; border: 1px solid #CCC; " id="Country"><%=TranslaterHelper.getTranslatedInLocale("Country", cle)%></td>
                        <td  style=" width: 1%; padding: 0.6em; border: 1px solid #CCC; " id="Prof"><%=TranslaterHelper.getTranslatedInLocale("Profession", cle)%></td>
                        <td  style=" width: 5%;  padding: 0.6em; border: 1px solid #CCC; " id="Pur"><%=TranslaterHelper.getTranslatedInLocale("Purpose", cle)%></td>
                        <td  style=" width: 1%;  padding: 0.6em; border: 1px solid #CCC; " id="Status"><%=TranslaterHelper.getTranslatedInLocale("Status", cle)%></td>
                         <td  style=" width: 5%;  padding: 0.6em; border: 1px solid #CCC; " id="ut"><%=TranslaterHelper.getTranslatedInLocale("User_Type", cle)%></td>
                        <td colspan="2" style=" width: 5%; padding: 0.6em; border: 1px solid #CCC; " id="Address"><%=TranslaterHelper.getTranslatedInLocale("Address", cle)%></td>
<!--                        <td   colspan="2"style=" width: 8%; border: 1px solid #CCC; " id="Yes">Activate</td><td></td>-->
                       
<!--                        <th  style=" width: 3.0em;  padding: 0.6em; border: 1px solid #CCC ;" id="No">No</th>-->
                        

<!--                        <th  style=" width: 10.0em; padding: 0.6em; border: 1px solid #CCC; " id="Exp_Fix_Date">Exp Fix Date </th>-->
</tr>

                </thead>
                <tbody  id="SearchResultBody" style=" border-collapse: collapse ;   text-align: center " >

</tbody>
<!--                    <tr><td colspan="5" style=" width: 5%;  padding: 0.6em; border: 1px solid #CCC; "><input id="Ar"  value="Assign Roles" type="button" onclick="activateUser()"></td>-->
<!--                        <td  colspan="5" style=" width: 5%;  padding: 0.6em; border: 1px solid #CCC; " ><input id="r"  value="Deactivate Users" type="button" onclick="dactUser()"></td>-->
<!--                    </tr>-->
                    </table> </form></div>
        <div id='loadingmetadata' class='loading_image' style="display:none;">
                <img alt=""  id='imgId' src='images/help-loading.gif'  border='0px' style='position:absolute; left: 450px; top: 150px;'/>
</div>
         <div id="userAssignRoles" title="Assign Roles To User" STYLE='display:none' >
<!--            <iframe src="userFolderAssignment.jsp" scrolling="no" STYLE='display:block;' height="100%" width="560px" frameborder="0" id="userframe"></iframe>--%>-->
        </div>

        <div id="UserDateInterval" style="display:none" title="UserDateInterval">
            <table><tr><td>From</td><td><input id="" type="text" value="<%=todaysDate%>" style="width: 120px;" maxlength="100" name="fdate" readonly="true"></td>
                  </tr>
            <tr><td>To</td><td><input id="tdatepicker" type="text" value="" style="width: 120px;" maxlength="100" name="fdate" readonly=""></td>
                  </tr></table>
        </div>
<script type="text/javascript">
            $(document).ready(function(){
 $.get("<%= request.getContextPath()%>/userLayerAction.do?userParam=getPendingUsers&select=all&status=all",function(data){
//      alert($("#Status").val())
      if(data=='')
          {
//              data="<tr><td colspan='10'>No data</td<</tr>";
//              alert(data)
              alert("No Data To Display")
//              $("#SearchResultBody").append(data)
          }
                        $("#SearchResultBody").html(data)
                        });
                        $("#SearchResult").show();

                        
 if ($.browser.msie == true){
                  $("#userAssignRoles").dialog({
                        autoOpen: false,
                        height: 450,
                        width:750,
                        position: 'justify',
                        modal: true
                    });
//                     $("#UserDateInterval").dialog({
//                        autoOpen: false,
//                        height: 450,
//                        width:750,
//                        position: 'justify',
//                        modal: false
//                    });
                    $('#tdatepicker').datepicker({
                    changeMonth: true,
                    changeYear: true,
                    yearRange: "-20:+20",
                    dateFormat: "dd/mm/yy"
                    });

               }
               else {
                    $("#userAssignRoles").dialog({
                        autoOpen: false,
                        height: 450,
                        width:750,
                        position: 'justify',
                        modal: true
                    });
//                     $("#UserDateInterval").dialog({
//                        autoOpen: false,
//                        height: 450,
//                        width:750,
//                        position: 'justify',
//                        modal: false
//                    });
                    $('#tdatepicker').datepicker({
                    changeMonth: true,
                    changeYear: true,
                    yearRange: "-20:+20",
                    dateFormat: "dd/mm/yy"
                    });
               }



});
              function cancelUsersFolders(){
//             alert("cancelUsersFolders")
                $('#userAssignRoles').dialog('close');

            }


      function GetUserTable(id)
      {
//          alert(id)
          id=$("#"+id).val();
//          alert(id)
          $.get("<%= request.getContextPath()%>/userLayerAction.do?userParam=getPendingUsers&select=all&status="+id,function(data){
//      alert(data)
      if(data=='')
          {
//              data="<tr><td colspan='10'>No data</td<</tr>";
//              alert(data)
              alert("No Data To Display")
//              $("#SearchResultBody").append(data)
          }
                        $("#SearchResultBody").html(data)
                        });
                        $("#SearchResult").show();


      }
          
function activateUser()
{
//     alert("chkusersobjlength");
//     var k="hello";
//   $("#UserDateInterval").dialog('open');

//     var chkusersobj = document.forms.UserInfoForm.chkRUser;
     var checkedIds = $(":checkbox:checked").map(function() {
        return this.id;
    }).get();
//    alert(checkedIds)
    if(checkedIds=="")
        {
            alert("Please Select Atleast One User")
            return false;
        }
//    alert("..."+checkedIds.length)
//     alert(chkusersobj.length)
         
//var mailid;
var id;
var puids="";

                      for(var j=0;j<checkedIds.length;j++)
                    {
                        $("#loadingmetadata").show();
                       id=checkedIds[j].split("::")
//                        alert(id[1])
//                        alert(id[0])
//                        var id=$("#"+checkedIds[j]).val()
//                        alert(id)
         $.ajax({
                    async:false,
                    url:'userLayerAction.do?userParam=getPendingUsers&select=one&id='+id[1],
                    success:function(data) {
//                        alert(data)
                        $("#loadingmetadata").hide();
                        if(data.contains("activated")){
                      var name=data.split("activated");
                      alert("User "+name[1]+" Activated Successfully...\n\n\
                                An Email Notification has been Sent to user"
                              );

                      }
                     else if(data.contains("AlreadyActivated"))
                          {
                            var name=data.split("AlreadyActivated");
                             alert("User "+name[1]+" is Already Activated ")
                          }
                    else
                        {
                            alert('Record Not Saved')
                        }
//                        window.location.href = "<%= request.getContextPath()%>/UserManagement.jsp"


}

     });

      $.ajax({
                    async:false,
                    url:'userLayerAction.do?userParam=getPUid&pmid='+id[0],
                    success:function(data) {
//              alert("pmid"+data)
              puids=puids+data+"::";
                    }
      });
                    }

     assignRoles(puids);
//     location.reload();
     reloadpage();

            

}

function assignRoles(puids)
{
//    alert("puids"+puids);
                  document.getElementById("userAssignRoles").innerHTML = "<iframe class=frame1 src=userRoleAssignment.jsp?userId="+puids+"&multiple=true style='width:700px;height:350px' frameborder='0'></iframe>";
//                    alert( $("#userAssignRoles").html())
                        $("#userAssignRoles").dialog('open');
//                        $("#userAssignRoles").dialog('close');

}

function dactUser()
{  var id;
    var mailids="";
    var status=$("#Status").val();
    
    if(status==='Activated' || status==='All')
        {
       
    var res=confirm("Are You Sure to Deactivate user?");
    if(res){
   var checkedIds = $(":checkbox:checked").map(function() {
        return this.id;
    }).get();
//    alert(checkedIds)
    if(checkedIds==="")
        {
            alert("Please Select Atleast One User");
            return false;
        }

                    for(var j=0;j<checkedIds.length;j++)
                    {
                        $("#loadingmetadata").show();
                       id=checkedIds[j].split("::");
                       mailids=mailids+id[0]+"::";
                     }
   $("#loadingmetadata").show();
     $.ajax({
                    async:false,
                    url:'userLayerAction.do?userParam=dActUsers&id='+mailids,
                    success:function(data) {
                        $("#loadingmetadata").hide();
                        if(data.contains("success")){
                        alert("Users Deactivated Successfully"
                              );

                      }
                    else
                        {
                            alert('Deactivation Failed');
                        }


}

     });


   reloadpage();
                       
}
}
else
{
 alert("Please Select Activated User");
        return false;
       
}
}

function rejectUsers()
{

    var id;
    var mailids="";
    var status=$("#Status").val();
//    alert(status);
//     
    if(status=='Pending' || status=='All')
        {
    var res=confirm("Are You Sure to Reject users?");
    if(res){
   var checkedIds = $(":checkbox:checked").map(function() {
        return this.id;
    }).get();
//    alert(checkedIds)
    if(checkedIds=="")
        {
            alert("Please Select Atleast One User")
            return false;
        }

                    for(var j=0;j<checkedIds.length;j++)
                    {
                        $("#loadingmetadata").show();
                       id=checkedIds[j].split("::")
                       mailids=mailids+id[0]+"::";
                     }
   $("#loadingmetadata").show();
     $.ajax({
                    async:false,
                    url:'userLayerAction.do?userParam=rejectUsers&id='+mailids,
                    success:function(data) {
                        $("#loadingmetadata").hide();
                        if(data.contains("success")){
                        alert("Users Rejected Successfully"
                              );

                      }
                    else
                        {
                            alert('Request Failed')
                        }


}

     });


   reloadpage();

}
}
else
{
alert("Please Select Pending User")
        return false;
}
}


function reloadpage()
{
     $.get("<%= request.getContextPath()%>/userLayerAction.do?userParam=getPendingUsers&select=all&status=all",function(data){
                        $("#SearchResultBody").html(data)
                        });
                        $("#Status").val("All")
                        $("#SearchResult").show();
}
       </script>
    </body>

</html>
<!--Added by Mohit jain-->