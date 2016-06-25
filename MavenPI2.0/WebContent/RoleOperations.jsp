<!doctype html>
<%@page import="prg.db.PbDb,prg.db.PbReturnObject" %>


<%//for clearing cache
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
            String contextPath=request.getContextPath();
%>
<html lang="en">
     <script src="<%=contextPath%>/javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <script type="text/javascript" src="javascript/draggable/ui.core.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/jquery.tablesorter.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/tablesorter/addons/pager/jquery.tablesorter.pager.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/tablesorter/docs/js/chili/chili-1.8b.js"></script>
       <script type="text/javascript" src="<%=contextPath%>/tablesorter/dragTable.js"></script>
        <script language="JavaScript" src="<%=contextPath%>/querydesigner/JS/jquery.columnfilters.js"></script>
           <script type="text/javascript" src="<%=contextPath%>/javascript/ui.dialog.js"></script>
<!--        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/style.css" type="text/css" media="print, projection, screen">-->
          <link type="text/css" href="<%=contextPath%>/jQuery/jquery/themes/base/ui.all.css" rel="stylesheet" />
        <link type="text/css" href="<%=contextPath%>/jQuery/jquery/themes/base/ui.theme.css" rel="stylesheet" />
<!--        <link type="text/css" href="<%=contextPath%>/jQuery/jquery/demos.css" rel="stylesheet" />-->
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=session.getAttribute("theme")%>/metadataButton.css" rel="stylesheet" />
          <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=session.getAttribute("theme")%>/ReportCss.css" rel="stylesheet" />
          <style>
              *{
                  font-size: 12px;
              }
          </style>
    <head>

	

</head>
<%String Query = " SELECT CONNECTION_ID, CONNECTION_NAME FROM PRG_USER_CONNECTIONS";
        PbReturnObject pbro = new PbDb().execSelectSQL(Query);
        String connIds=String.valueOf(session.getAttribute("connId"));
     
%>
<body>
    <div id="selectConnection1" title="Select Connection">
        <form name="myForm1">
            <table cellpadding="5">
                <tr><td>
                        <label>Connection Name&nbsp;</label></td>
                    <td> <select id="connSelect" name="connSelect" style="width:146px" onchange="getBusinessGroupsDetails()">
                            <option value="0000">----SELECT----</option>
                            <%for (int i = 0; i < pbro.getRowCount(); i++) {%>
                            <option value="<%=pbro.getFieldValueInt(i, 0)%>"><%=pbro.getFieldValueString(i, 1)%></option>
                            <%}%>
                        </select>
                    </td>
                    <td>
                        <label for="name">&nbsp;&nbsp;&nbsp;&nbsp;Group Name&nbsp;</label></td>
                    <td> <select id="groupSelect" name="groupSelect" style="width:146px">
                            <option value ='0000'>--SELECT--</option>
                        </select></td>
                    <td>
                        &nbsp;&nbsp;<input TYPE="button" value="Go.." onclick="getRoleDetails()" class="themeColor">
                    </td>
                </tr>
            </table>
            <%--<input type="button"  value="Connect" onclick="saveTables()">--%>
        </form>
    </div>
    <div align="center" style=" width:100% ">
        <form id="roleDetails" name="roleDetails" method="post" action="">
            <table align ="center" id="measureDetTab">
                        <tr><td id ="measureDetTabBody">

                    </td></tr>
            </table>
        </form>
    </div>

    <div id="userAssigned" title="Assign To Users">
        <iframe  id="userAssignDisp" NAME='userAssignDisp' height="100%" width="100%"  frameborder="0" SRC='#'></iframe>
    </div>

    <div id="editRoleNameDiv" title="Edit Role Name" style="display:none" style="height: 398px; width: 676px;">
        <form id="editRoleNameForm" name="editRoleNameForm" action="" method="post">
            <table id="editRoleName" class="tablesorter" cellpadding="0" cellspacing="0" align="center">
                <tr>
                        <td id="editRoleNameTab" name="editRoleNameTab">

                    </td>
                </tr>
            </table>
            <br> <br>
            <center><input type="button" class="themeColor" style="width:auto;color:black" value="Update Details" id="btnn" onclick="updateRoleDetails()"/>&nbsp;&nbsp;&nbsp;&nbsp;<input type="button"  class="themeColor" style="width:auto" value="Cancel" onclick="cancelEditRole()"/></center>
        </form>
    </div>
        <script type="text/javascript">
            $(document).ready(function(){
               if ($.browser.msie == true){
                    $("#userAssigned").dialog({
                    autoOpen: false,
                    height: 550,
                    width: 700,
                    position: 'justify',
                    modal: true
                     });
                     $("#editRoleNameDiv").dialog({
                        autoOpen: false,
                        height: 250,
                        width: 437,
                        position: 'justify',
                        modal: true
                    });
               }
               else {
                    $("#userAssigned").dialog({
                    autoOpen: false,
                    height: 550,
                    width: 700,
                    position: 'justify',
                    modal: true
                     });
                     $("#editRoleNameDiv").dialog({
                        autoOpen: false,
                        height: 250,
                        width: 437,
                        position: 'justify',
                        modal: true
                    });
               }
            });

        function getBusinessGroupsDetails(){
            var connId=document.getElementById("connSelect").value;
            if (connId=='0000')
                 $("#connId").html("")
            else
                $.get("<%= request.getContextPath()%>/userLayerAction.do?userParam=getGroupsDetails&connectionID="+connId,function(data){
                    $("#groupSelect").html(data)
                });
        }
        function getRoleDetails(){
            var groupId=document.getElementById("groupSelect").value;
             var connId=document.getElementById("connSelect").value;
            if (groupId=='0000' || connId=='0000') {
                alert('Please Select Details ! ')
               $("#measureDetTabBody").html("")
            }
            else {
                 $("#measureDetTabBody").html("")
                 $.post('userLayerAction.do?userParam=getRolesDetails&groupId='+groupId,function(data){
                    var jsonVar=eval('('+data+')')
                    var folderId=jsonVar.folderId
                    var folderName=jsonVar.folderName
                    var isPublished=jsonVar.isPublished
                    var folderDesc=jsonVar.folderDesc

                    var htmlVar = "";
               htmlVar+="<br><table id=\"measureDetTab\" class=\"tablesorter\"  border=\"0\" cellpadding=\"1\" cellspacing=\"1\">\n\
                                    <tr><td><table id=\"showProcessTable\" class=\"tablesorter\"  border=\"1\" cellpadding=\"1\" cellspacing=\"1\">\n\
                                         <thead><tr>\n\
                                                <th class=\"themeColor\"><label>Role Name </label></th>\n\
                                                <th class=\"themeColor\"><label>Publish </label></th>\n\
                                                <th class=\"themeColor\"><label>Assignment </label></th>\n\
                                                <th class=\"themeColor\"><label>Table Publish </label></th>\n\
                                                <th class=\"themeColor\"><label>Dimension Publish </label></th>\n\
                                                <th class=\"themeColor\"><label>Action  </label></th>\n\
                                    </tr></thead>";
            $("#measureDetTabBody").html(htmlVar)
              for(var i=0;i<folderId.length;i++)
                   {
                       htmlVar+= "<tr ><td style=\ text-align: center\">"+folderName[i]+"</td>"
                       if(isPublished[i]=="Y")
//                            htmlVar+= "<td class=\"themeColor\"><input type=\"button\" value = \"Published\" disabled=\"true\" onclick=\"rePublishRole("+folderId[i]+")\")\"></td>"
                            htmlVar+= "<td><a tiltle=\"Publish Role\" href=\"javascript:rePublishRole("+folderId[i]+")\">Re-Publish</a></td>"
                        else
                            htmlVar+= "<td><a tiltle=\"Publish Role\" href=\"javascript:publishRole("+folderId[i]+")\">Publish</a></td>"
                       htmlVar+="<td><a title=\"Assign Users\" href=\"javascript:userAssignment("+groupId+","+folderId[i]+")\">Assign</a></td>\n\
                                    <td ><input type=\"button\" disabled=\"true\" value=\"Publish Tables\" onclick=\"tablePublish("+folderId[i]+")\"></td>\n\
                                  <td ><input type=\"button\" disabled=\"true\" value=\"Publish Facts\" onclick=\"factPublish("+folderId[i]+")\"></td>\n\
                                        <td><input type=\"hidden\" name=\"folderId"+[i]+"\" id=\""+folderId[i]+"\" value=\""+folderId[i]+"\">\n\
                                            <img width=\"22px\" height=\"22px\" border=\"0\" src=\"icons pinvoke/pencil-small.png\" alt=\"\" title=\"Edit Role Name\" onclick=\"editRoleName('"+folderName[i]+"',"+folderId[i]+",'"+folderDesc[i]+"')\"></img>\n\
                                            <img width=\"22px\" height=\"22px\" border=\"0\" src=\"icons pinvoke/cross-small.png\" alt=\"\" title=\"Delete Role\" onclick=\"deleteRole("+folderId[i]+")\"></img>\n\
                                        </td>\n\
                                    </tr>";
                         $("#measureDetTabBody").html(htmlVar)
                   }
                });
            }
        }
        function editRoleName(folderName,folderId,folderDesc){
        $("#editRoleNameDiv").dialog('open')
        var htmlVar = "";
            htmlVar+="<table id=\"editRoleName\" cellpadding=\"1\" cellspacing=\"1\">\n\
                        <tr><td align=\"left\" class=\"themeColor\"><label>Business Role Name : &nbsp;&nbsp;&nbsp;&nbsp;</label></td>\n\
                            <td align=\"left\"><input type=\"text\" class=\"\" value=\""+folderName+"\" id=\"roleName\" name=\"roleName\" onkeyup=\"copyDetails1()\" size=\"35px\"/></td>\n\
                        </tr>\n\
                        <tr><td align=\"left\" class=\"themeColor\"><label>Role Description : &nbsp;&nbsp;&nbsp;&nbsp;</label></td>\n\
                            <td align=\"left\"><input type=\"text\" class=\"\" value=\""+folderDesc+"\" id=\"roleDesc\" name=\"roleDesc\" size=\"35px\"/></td>\n\
                        </tr>\n\
                        <tr><td ><input type=\"hidden\" name=\"folderId\" id=\"folderId\" value=\""+folderId+"\"></td>\n\
                                <td>&nbsp;</td></tr>\n\
                            </table>";
            $("#editRoleNameTab").html(htmlVar)
        }
        function copyDetails1(){
                var name=document.editRoleNameForm.roleName.value;
                document.editRoleNameForm.roleDesc.value=name;
        }
         function updateRoleDetails(){
              var roleName=document.forms.editRoleNameForm.roleName.value;
              var roleDesc=document.forms.editRoleNameForm.roleDesc.value;
              var folderId=document.forms.editRoleNameForm.folderId.value;

               var roleName_len = roleName.length;
               var roleDesc_len = roleDesc.length;
                if(roleName_len==0 || roleDesc_len==0){
                    alert('Please Enter Complete Details!')
                }
                else {
                    $.post('userLayerAction.do?userParam=updateRoleDetails&roleName='+roleName+'&roleDesc='+roleDesc+'&folderId='+folderId,function(data)  {
                    if(data==1){
                            alert('Details Updated Successfully')
                            $("#editRoleNameDiv").dialog('close')
                       $("#measureDetTabBody").html("")
                    }
                    else {
                            alert('Error ! Details Not Updated')
                            $("#editRoleNameDiv").dialog('close')
                    }

                    });
                }
          }
            function cancelEditRole(){
                $("#editRoleNameDiv").dialog('close');
           }
        function userAssignment(groupId,folderId){
           $("#userAssigned").dialog('open')

            var frameObj=document.getElementById("userAssignDisp");
            var source="getUserAssignList.jsp?grpId="+groupId+'&roleId='+folderId;
            frameObj.src=source;
        }
        function publishRole(folderId){
            var retVal = confirm("Are you sure you want to Publish Role !");
               if( retVal == true ){
                   $.post('userLayerAction.do?userParam=publishBussinessRole&folderId='+folderId,function(data)  {
                                if(data==1){
                                        alert('Role published successfully')
                                        $("#measureDetTabBody").html("")
                                }
                                else{
                                    alert('Error ! Role can not be published')
                                }

                   });
               }
        }
        function rePublishRole(folderId){
            var retVal = confirm("This Role is already published , Are you sure you want to Re-Publish this Role !");
               if( retVal == true ){
                   $.post('userLayerAction.do?userParam=rePublishBussinessRole&folderId='+folderId,function(data)  {
                                if(data==1){
                                        alert('Role Re-Published successfully')
                                        $("#measureDetTabBody").html("")
                                }
                                else{
                                    alert('Error ! Role can not be Re-Published')
                                }
                   });
               }
        }
        function deleteRole(folderId){
           var retVal = confirm("Are you sure you want to delete Role !");
               if( retVal == true ){
                   $.post('userLayerAction.do?userParam=deleteRole&folderId='+folderId,function(data)  {
                                if(data==2){
                                        alert('Role deleted successfully')
                                        $("#measureDetTabBody").html("")
                                }
                                else{
                                    alert('Error ! Role can not be deleted')
                                    $("#measureDetTabBody").html("")
                                }
                   });
               }
        }
	</script>
</body>
</html>
